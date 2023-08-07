package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.presentation.security.AutorizzazioneCUStandard;
import it.csi.solmr.util.SolmrLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VisualizzaAttestazioniCU extends AutorizzazioneCUStandard {

	private static final long serialVersionUID = -5886699766180178468L;

	public VisualizzaAttestazioniCU() {
		this.cuName = "VISUALIZZA_ATTESTATI";
		this.isCUForReadWrite = false;
	}

	/**
	 * Genera il menu delle pagine html relative a questo macro CU Iride2
	 *
	 * @param htmpl Htmpl
	 * @param request HttpServletRequest
	 * @return java.lang.String
	 */
	public String writeMenu(Htmpl htmpl, HttpServletRequest request) {

		SolmrException se = (SolmrException)request.getAttribute("statoAzienda");
		AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");
		RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
		//String allegati = (String)request.getAttribute("allegati");
		TipoAttestazioneVO[] elencoAttestazioni = (TipoAttestazioneVO[])request.getAttribute("elencoAttestazioni");
		
		super.writeBanner(htmpl, request);
		super.writeBarraDatiAzienda(anagAziendaVO, htmpl);
		super.writeBarraStato(se, htmpl);
		super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, request);
		
		if(super.hasPropertiesToModifyAnagrafe(ruoloUtenza, anagAziendaVO) 
		    && (elencoAttestazioni != null && elencoAttestazioni.length > 0)) 
		{
      htmpl.newBlock("blkAttestazioni.blkModifica");
		}
		
		/*if(!Validator.isNotEmpty(allegati)) {
			if(super.hasPropertiesToModifyAnagrafe(ruoloUtenza, anagAziendaVO) && (elencoAttestazioni != null && elencoAttestazioni.length > 0)) {
				htmpl.newBlock("blkAttestazioni.blkModifica");
			}
		}
		else {
			if(super.hasPropertiesToModifyAnagrafe(ruoloUtenza, anagAziendaVO)) {
				htmpl.newBlock("blkAllegati.blkModifica");
			}
		}*/
		return null;
	}

	/**
	 * Esegue i controlli sulla competenza di dato per l'utente in questione.
	 *
	 */
	public String hasCompetenzaDato(HttpServletRequest request, HttpServletResponse response, 
	    RuoloUtenza ruoloUtenza, AnagFacadeClient anagFacadeClient) 
	{
		AnagAziendaVO anagAziendaVO = (AnagAziendaVO) request.getSession().getAttribute("anagAziendaVO");
		if(anagAziendaVO == null) 
		{
		  SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO+": anagAziendaVO null");
			return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO;
		}
		return validateGenericUser(ruoloUtenza,anagAziendaVO,anagFacadeClient,request);
	}

}
