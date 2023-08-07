package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.NotificaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.presentation.security.Autorizzazione;
import it.csi.solmr.util.SolmrLogger;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NotificheCU extends Autorizzazione {
		
	private static final long serialVersionUID = 1L;

	public NotificheCU() {
		this.cuName="NOTIFICHE";
		this.isCUForReadWrite=false;
	}

	/**
	 * Genera il menu delle pagine html relative a questo macro CU Iride2
	 *
	 * @param htmpl Htmpl
	 * @param request HttpServletRequest
	 * @return java.lang.String
	 */
	
  @SuppressWarnings("rawtypes")
  public String writeMenu(Htmpl htmpl, HttpServletRequest request) 
	{
		AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");
		RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
		Object objForMenu = (Vector)request.getAttribute("elencoNotifiche");
		if(objForMenu == null) {
			objForMenu = (NotificaVO)request.getAttribute("dettaglioNotificaVO");
		}
		super.writeBanner(htmpl, request);
		super.writeBarraDatiAzienda(anagAziendaVO, htmpl);
		super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, request);
		/*if(objForMenu != null) 
		{
			if(objForMenu instanceof Vector) 
			{
				if(((Vector)objForMenu).size() > 0) 
				{
					htmpl.newBlock("blkGestioneNotifiche.blkDettaglioNotifica");
					htmpl.newBlock("blkGestioneNotifiche.blkChiusuraNotifica");
				}
			}
			else {
				htmpl.newBlock("blkGestioneNotifiche.blkChiusuraNotifica");
			}
		}*/
		
		if(objForMenu != null) 
    {
      if(objForMenu instanceof Vector) 
      {
        if(((Vector)objForMenu).size() > 0) 
        {
          htmpl.newBlock("blkGestioneNotifiche.blkDettaglioNotifica");
        }
      }
    }
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
		String iridePageNameForCU = (String)request.getAttribute("iridePageNameForCU");
		if(anagAziendaVO == null) 
		{
		  SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO+": anagAziendaVO null");
			return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO;
		}
		boolean hasProperties = false;
		if("chiusuraNotificaCtrl.jsp".equals(iridePageNameForCU)) 
		{
			
    	if(anagAziendaVO.getDataCessazione() == null 
    	  && anagAziendaVO.getDataCessazioneStr() == null 
    	  && anagAziendaVO.getDataFineVal() == null) 
    	{
      	hasProperties = true;
      }
      
			if(!hasProperties) 
			{
			  SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO+": hasProperties false"); 
				return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO;
			}
			else {
				return null;
			}
		}
		else 
		{
			return validateGenericUser(ruoloUtenza,anagAziendaVO,anagFacadeClient,request);
		}
	}

}
