package it.csi.smranag.smrgaa.util;

import it.csi.papua.papuaserv.dto.messaggistica.ListaMessaggi;
import it.csi.papua.papuaserv.dto.messaggistica.Messaggio;
import it.csi.papua.papuaserv.exception.messaggistica.LogoutException;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.StringcodeDescription;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.util.SolmrLogger;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpSession;

public class MessaggisticaUtils  implements Serializable{

	

	/**
   * 
   */
  private static final long serialVersionUID = -4205656849449010671L;

  /**
	 * Chiama ogni X minuti il servizio di messaggistica, visualizzando per Y minuti i messaggi di testata
	 * Carica i dati in sessione
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public static StringcodeDescription caricaMessaggiTestata(HttpSession session) 
	    throws LogoutException
	{
		RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
		if(ruoloUtenza==null) return null;
		MessaggiTestata messaggiTestata = (MessaggiTestata)session.getAttribute(SolmrConstants.SESSION_MESSAGGI_TESTATA);
		// messaggio non presente o da ricaricare
		if(messaggiTestata==null || messaggiTestata.isToRefresh())
		{
			StringBuffer testoTestata = new StringBuffer();
			GaaFacadeClient gaaFacadeClient= GaaFacadeClient.getInstance();
			try
			{
				// messaggi di testata o di logout da visualizzare in testata
				ListaMessaggi listaMessaggi = gaaFacadeClient.getListaMessaggi(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE, ruoloUtenza.getCodiceRuolo(), ruoloUtenza.getCodiceFiscale(),ListaMessaggi.FLAG.TESTATA | ListaMessaggi.FLAG.LOGOUT, Boolean.FALSE, null, Boolean.TRUE);
				if(listaMessaggi!=null && listaMessaggi.getMessaggi()!=null 
				    && listaMessaggi.getMessaggi().length>0)
				{
					SolmrLogger.debug(MessaggisticaUtils.class, "Sono presenti messaggi");
					for(Messaggio mess : listaMessaggi.getMessaggi())
					{
						if(testoTestata.length()>0)
						{
							testoTestata.append( " *** ");
						}
						testoTestata.append( mess.getTitolo());
					}
				}
				// serve il numero messaggi generici non ancora letti
				long numMessaggi= 0;
				if(listaMessaggi!=null){
					numMessaggi = listaMessaggi.getNumeroMessaggi(ListaMessaggi.FLAG.GENERICO);
				}
						
				messaggiTestata = initializeMessaggiTestata(messaggiTestata);
				messaggiTestata.setMessaggio(testoTestata.toString());
				messaggiTestata.setNumMessaggiDaLeggere(numMessaggi);
				messaggiTestata.calcolaDataScadenza();
				
				session.setAttribute(SolmrConstants.SESSION_MESSAGGI_TESTATA, messaggiTestata);
			}
			catch(LogoutException ex)
			{
				SolmrLogger.error(MessaggisticaUtils.class, "Forzare il logout"); // gestito nella dispatcherServlet
				throw ex;
			}		
			catch (Exception e) {
			  SolmrLogger.error(MessaggisticaUtils.class, "Errore nel richiamo della messaggistica: " +LoggerUtils.getStackTrace(e));
			}
		}
		else if(messaggiTestata.isToCheckLogout())
		{
			// controllo solamente se ci sono messaggi di loguot attivi (quindi logoutexception
			SolmrLogger.debug(MessaggisticaUtils.class, "Controllo periodico MSG logout");
			GaaFacadeClient gaaFacadeClient= GaaFacadeClient.getInstance();
			try
			{
				messaggiTestata.dataInvCheckLogout = new Date();
				session.setAttribute(SolmrConstants.SESSION_MESSAGGI_TESTATA, messaggiTestata);
				gaaFacadeClient.getListaMessaggi(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE, ruoloUtenza.getCodiceRuolo(), ruoloUtenza.getCodiceFiscale(),ListaMessaggi.FLAG.NESSUNO, Boolean.FALSE, null, Boolean.TRUE);
			}
			catch(LogoutException ex)
			{
				SolmrLogger.error(MessaggisticaUtils.class, "Forzare il logout 2"); // gestito nella dispatcherServlet
				throw ex;
			}				
			catch (Exception e) {
				SolmrLogger.error(MessaggisticaUtils.class, "Errore nel richiamo della messaggistica: " +LoggerUtils.getStackTrace(e));
			}
			 
		}
		StringcodeDescription result = new StringcodeDescription();
		if(messaggiTestata!=null){
			if(messaggiTestata.isToShow()){
				result.setDescription(messaggiTestata.getMessaggio());
			}
			result.setCode(""+messaggiTestata.getNumMessaggiDaLeggere());
		}
		return result;
	}
	
	/**
	 * Recupera in sessione di dati caricati periodicamente nella dispatcherServlet
	 * e determina cosa riportare nella testatina
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public static StringcodeDescription getMessaggiTestata(HttpSession session)
	{
		StringcodeDescription result=null;
		MessaggiTestata messaggiTestata = (MessaggiTestata)session.getAttribute(SolmrConstants.SESSION_MESSAGGI_TESTATA);
		if(messaggiTestata!=null)
		{
			result = new StringcodeDescription();
			if(messaggiTestata.isToShow())
			{
				result.setDescription(messaggiTestata.getMessaggio());
			}
			result.setCode(""+messaggiTestata.getNumMessaggiDaLeggere());
		}
		return result;
	}	
	

	private class MessaggiTestata implements Serializable
	{
	  
	  
		/**
     * 
     */
    private static final long serialVersionUID = -5743659959587683350L;
    
    
    private String messaggio = null;
		private Date dataScadenza = null;
		private Date dataInvocazione = null;
		private Date dataInvCheckLogout = null;
		private int minutiVisualizzazione = 5;
		private int minutiRefresh = 30;
		private int minutiLogout = 5;
		private long numMessaggiDaLeggere = 0;
		
	  public MessaggiTestata() 
	  {
	  	super();
	  	dataInvocazione = new Date();
		}
	  public boolean isToRefresh()
	  {
	  	if(dataInvocazione==null)
	  	{
	   		return true;
	    }
	    Calendar cal= Calendar.getInstance();
	    cal.setTime(dataInvocazione);
	    cal.add(Calendar.MINUTE, minutiRefresh);
	    if(cal.getTime().before(new Date()))
	    {
	    	return true;
	    }
	    return false;
	  }
	  public boolean isToCheckLogout()
	  {
	   	if(dataInvCheckLogout==null)
	   	{
	    	return true;
	    }
	    Calendar cal= Calendar.getInstance();
	    cal.setTime(dataInvCheckLogout);
	    cal.add(Calendar.MINUTE, minutiLogout);
	    if(cal.getTime().before(new Date()))
	    {
	    	return true;
	    }
	    return false;
	  }
	  public boolean isToShow()
	  {
	   	if(dataScadenza==null || dataScadenza.before(new Date()))
	   	{
	  		return false;
	   	}
	    return true;
	  }
	  public Date calcolaDataScadenza()
	  {
	   	if(dataInvocazione==null)
	   	{
	    	dataScadenza = null;
	    	return null;
	    }
	    Calendar cal= Calendar.getInstance();
	    cal.setTime(dataInvocazione);
	    cal.add(Calendar.MINUTE, minutiVisualizzazione);
	    dataScadenza = cal.getTime();
	    return dataScadenza;
	  }
		public String getMessaggio() {
			return messaggio;
		}
		public void setMessaggio(String messaggio) {
			this.messaggio = messaggio;
		}
		public int getMinutiVisualizzazione() {
			return minutiVisualizzazione;
		}
		public void setMinutiVisualizzazione(int minutiVisualizzazione) {
			this.minutiVisualizzazione = minutiVisualizzazione;
		}
		public int getMinutiRefresh() {
			return minutiRefresh;
		}
		public void setMinutiRefresh(int minutiRefresh) {
			this.minutiRefresh = minutiRefresh;
		}
		public long getNumMessaggiDaLeggere() {
			return numMessaggiDaLeggere;
		}
		public void setNumMessaggiDaLeggere(long numMessaggiDaLeggere) {
			this.numMessaggiDaLeggere = numMessaggiDaLeggere;
		}
		public int getMinutiLogout() {
			return minutiLogout;
		}
		public void setMinutiLogout(int minutiLogout) 
		{
			this.minutiLogout = minutiLogout;
		}
	}
	  
  /**
   * Valorizza minutiRefresh e minutiVisualizzazione leggendo due parametri da DB
   * @param messaggiTestata
   * @return
   * @throws Exception
   */
	private static MessaggiTestata initializeMessaggiTestata(MessaggiTestata messaggiTestata) 
	  throws Exception
	{
	  if(messaggiTestata==null)
	  {
		  MessaggisticaUtils mu = new MessaggisticaUtils();
		  messaggiTestata = mu.new MessaggiTestata();
			   
		  AnagFacadeClient anagFacadeClient = new AnagFacadeClient(); 
			  
			BigDecimal refresh = (BigDecimal)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_MESSAGGI_REFRESH);
			messaggiTestata.setMinutiRefresh(refresh.intValue());
			  
			BigDecimal elapsed = (BigDecimal)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_MESSAGGI_ELAPSED);
			messaggiTestata.setMinutiVisualizzazione(elapsed.intValue());
				  
			BigDecimal logoutCheck = (BigDecimal)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_MESSAGGI_LOGOUT);
			messaggiTestata.setMinutiLogout(logoutCheck.intValue());
	  }
				 
		messaggiTestata.dataInvocazione = new Date();
		messaggiTestata.dataInvCheckLogout = new Date();
		  
		return messaggiTestata;
	  
	}
	
}