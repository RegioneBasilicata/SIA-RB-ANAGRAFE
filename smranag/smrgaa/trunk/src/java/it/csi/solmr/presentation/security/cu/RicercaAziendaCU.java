package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.iride2.Iride2AbilitazioniVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.presentation.security.Autorizzazione;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RicercaAziendaCU extends Autorizzazione
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6290044979058724270L;

	public RicercaAziendaCU()
	{
		this.cuName="RICERCA_AZIENDA";
		this.isCUForReadWrite=false;
	}

	/**
	 * Genera il menu delle pagine html relative a questo macro CU Iride2
	 *
	 * @param htmpl Htmpl
	 * @param request HttpServletRequest
	 * @return java.lang.String
	 */
	public String writeMenu(Htmpl htmpl, HttpServletRequest request) 
	{
		super.writeBanner(htmpl, request);
		RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
		super.writeGenericMenu(ruoloUtenza, null, htmpl, request);
		
		Iride2AbilitazioniVO iride2AbilitazioniVO=(Iride2AbilitazioniVO) request.getSession().getAttribute("iride2AbilitazioniVO");
		
		if (iride2AbilitazioniVO.isUtenteAbilitato("GESTIONE_MANDATO"))
      htmpl.newBlock("blkRegistrazioneMandato");
		
	  //Nuova gestione revoca delega del 01/09/2011
    //Iride2AbilitazioniVO iride2AbilitazioniVO=(Iride2AbilitazioniVO) request.getSession().getAttribute("iride2AbilitazioniVO");
    if(iride2AbilitazioniVO.isUtenteAbilitato("REVOCA_DELEGA"))
    {
      htmpl.newBlock("blkRevocaMandato");
    }
    
    if(iride2AbilitazioniVO.isUtenteAbilitato("ELENCO_RICHIESTE_AZIENDA"))
    {
      htmpl.newBlock("blkElencoRichiesteAzienda");
    }
    
    if(iride2AbilitazioniVO.isUtenteAbilitato("RICERCA_RICHIESTA"))
    {
      request.getSession().setAttribute("scritturaRichiesta","true");
    }
		
		/*if(ruoloUtenza != null && ruoloUtenza.isValid()) {
			if(ruoloUtenza.isReadWrite()) {
				if((ruoloUtenza.isUtenteIntermediario() && SolmrConstants.TIPO_INTERMEDIARIO_CAA.equalsIgnoreCase(ruoloUtenza.getTipoIntermediario())) ||
						ruoloUtenza.isUtenteOPRGestore()) {
					htmpl.newBlock("blkRegistrazioneMandato");
				}
			}
		}*/
		return null;
	}

	/**
	 * Esegue i controlli sulla competenza di dato per l'utente in questione.
	 *
	 */
	public String hasCompetenzaDato(HttpServletRequest request,
			HttpServletResponse response,
			RuoloUtenza ruoloUtenza,
			AnagFacadeClient anagFacadeClient)
	{
		AnagAziendaVO anagAziendaVO = (AnagAziendaVO) request.
		  getSession().getAttribute("anagAziendaVO");
		
		//Segnalo che arrivo dalla ricerca, per evitare
		//di accedere dalla ricerca anche sulle aziende socie
		//su cui è stata richiesta la visualizzazione nell'elenco dsoci in modo limitato
		request.setAttribute("RICERCA", "RICERCA");
		if (anagAziendaVO==null)
			return null;
		else
			return validateGenericUser(ruoloUtenza,anagAziendaVO,anagFacadeClient,request);
	}

}
