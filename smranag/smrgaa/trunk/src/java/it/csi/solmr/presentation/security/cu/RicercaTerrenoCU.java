package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.iride2.Iride2AbilitazioniVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.presentation.security.Autorizzazione;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RicercaTerrenoCU extends Autorizzazione
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7004902668710310675L;

	public RicercaTerrenoCU()
	{
		this.cuName="RICERCA_TERRENO";
		this.isCUForReadWrite=false;
	}

	/**
	 * Genera il menu delle pagine html relative a questo macro CU Iride2
	 *
	 * @param htmpl Htmpl
	 * @param request HttpServletRequest
	 * @return java.lang.String
	 */
	public String writeMenu(Htmpl htmpl, HttpServletRequest request) {

		super.writeBanner(htmpl, request);
		RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
		Iride2AbilitazioniVO iride2AbilitazioniVO=(Iride2AbilitazioniVO) request.getSession().getAttribute("iride2AbilitazioniVO");
		super.writeGenericMenu(ruoloUtenza, null, htmpl, request);
		if(ruoloUtenza != null && ruoloUtenza.isValid()) {
			if(ruoloUtenza.isReadWrite()) {
				if(ruoloUtenza.isUtenteProvinciale() || ruoloUtenza.isUtenteRegionale() || ruoloUtenza.isUtenteOPRGestore()) 
				{
				  //La pagine che chiama sono usate anche nel menù ricerca terreno per l'intermediario!!!!
				  htmpl.newBlock("blkInserisciTerreno");
					//htmpl.newBlock("blkModificaTerreno");
					htmpl.newBlock("blkCessazioneTerreno");
				}
			}
		}
		
		
		if (iride2AbilitazioniVO.isUtenteAbilitato("RICERCA_TERRENO_SIGMATER"))
      htmpl.newBlock("blkSigmater");
		
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
		if (anagAziendaVO==null)
			return null;
		return validateGenericUser(ruoloUtenza,anagAziendaVO,anagFacadeClient,request);
	}

}
