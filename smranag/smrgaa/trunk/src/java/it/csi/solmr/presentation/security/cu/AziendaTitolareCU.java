package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.iride2.Iride2AbilitazioniVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.presentation.security.Autorizzazione;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AziendaTitolareCU extends Autorizzazione
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2390194754271883462L;

	public AziendaTitolareCU()
	{
		this.cuName="AZIENDE_TITOLARE";
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
		
		if(iride2AbilitazioniVO.isUtenteAbilitato("ELENCO_RICHIESTE_AZIENDA"))
    {
      htmpl.newBlock("blkElencoRichiesteAzienda");
    }
		
		
		if(iride2AbilitazioniVO.isUtenteAbilitato("RICERCA_RICHIESTA"))
    {
      request.getSession().setAttribute("scritturaRichiesta","true");
    }
		
		
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
		return null;
	}

}
