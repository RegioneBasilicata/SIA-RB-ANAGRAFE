package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.presentation.security.Autorizzazione;
import it.csi.solmr.util.SolmrLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CessazioneAttivitaCU extends Autorizzazione
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1571402211837549770L;

	public CessazioneAttivitaCU()
	{
		this.cuName="CESSAZIONE_ATTIVITA";
		this.isCUForReadWrite=true;
	}

	/**
	 * Genera il menu delle pagine html relative a questo macro CU Iride2
	 *
	 * @param htmpl Htmpl
	 * @param request HttpServletRequest
	 * @return java.lang.String
	 */
	public String writeMenu(Htmpl htmpl, HttpServletRequest request) {
		// Gestisce solo il banner perchè si tratta di una pop-up
		super.writeBanner(htmpl, request);
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
		AnagAziendaVO anagAziendaVO = (AnagAziendaVO) request.getSession().getAttribute("anagAziendaVO");
		if(anagAziendaVO == null) 
		{
		  SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO+": anagAziendaVO null");		  
			return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO;
		}
		return validateGenericUser(ruoloUtenza,anagAziendaVO,anagFacadeClient,request);
	}

}
