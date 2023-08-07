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

public class ChiusuraNotificheCU extends Autorizzazione
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6019531291860424756L;

	public ChiusuraNotificheCU()
	{
		this.cuName="CHIUSURA_NOTIFICHE";
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

		AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");
		RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");

		super.writeBanner(htmpl, request);
		super.writeBarraDatiAzienda(anagAziendaVO, htmpl);
		super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, request);
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
		  SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO+": anagAziendaVO null ");
			return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO;
		}
		boolean hasProperties = false;
		
    if(anagAziendaVO.getDataCessazione() == null 
    	&& anagAziendaVO.getDataCessazioneStr() == null 
    	&& anagAziendaVO.getDataFineVal() == null) 
    {
    	hasProperties = true;
    }
    
		if(!hasProperties) 
		{
		  SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO+": hasPropeties false");
			return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO;
		}
		else 
		{
			return null;
		}
	}

}
