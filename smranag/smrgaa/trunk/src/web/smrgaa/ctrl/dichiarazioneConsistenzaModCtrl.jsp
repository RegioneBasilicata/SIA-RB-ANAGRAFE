
<%@ page language="java"
         contentType="text/html"
%>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.consistenza.*"%>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>

<%@ page import="it.csi.smranag.smrgaa.dto.AllegatoDichiarazioneVO"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%!
	public final static String VIEW = "../view/dichiarazioneConsistenzaModView.jsp";
%>

<%

	String iridePageName = "dichiarazioneConsistenzaModCtrl.jsp";
 	%>
 		<%@include file = "/include/autorizzazione.inc" %>
 	<%
 	SolmrLogger.debug(this, " - dichiarazioneConsistenzaModCtrl.jsp - INIZIO PAGINA");
 	
 	final String errMsg = "Impossibile procedere nella sezione modifica validazione." +
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  String actionUrl = "../layout/dichiarazioneConsistenza.htm";
  String erroreViewUrl = "/view/erroreView.jsp";

 	
 	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
 	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
 	String[] orderBy = null;   	
 	Long idDichiarazioneConsistenza = null;
 	Long idAllegato = null;
 	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

  String radiobutton = request.getParameter("radiobutton");
 	SolmrLogger.debug(this, "radiobutton: " +radiobutton);

 	if(Validator.isNotEmpty(radiobutton)) 
 	{
 	  if(radiobutton.contains("_"))
 	  {
 	    StringTokenizer st = new StringTokenizer(radiobutton, "_");
      idDichiarazioneConsistenza = new Long(st.nextToken());
      idAllegato = new Long(st.nextToken());
      request.setAttribute("idAllegato", idAllegato);
  	}
  	else
  	{
  	  idDichiarazioneConsistenza = new Long(radiobutton);
  	}
 	}

 	SolmrLogger.debug(this, "idDichiarazioneConsistenza: " + idDichiarazioneConsistenza);

  AllegatoDichiarazioneVO allegatoDichiarazioneVO = null; 
  try
  {
 	  ConsistenzaVO consistenzaVO = anagFacadeClient.findDichiarazioneConsistenzaByPrimaryKey(idDichiarazioneConsistenza);
 	  request.setAttribute("consistenzaVO", consistenzaVO);
 	  if(Validator.isNotEmpty(idAllegato))
 	  {
 	    allegatoDichiarazioneVO = gaaFacadeClient.getAllegatoDichiarazioneFromIdAllegato(idAllegato);
 	  }
 	  else
 	  {
 	    allegatoDichiarazioneVO = gaaFacadeClient.getAllegatoDichiarazioneFromIdDichiarazione(idDichiarazioneConsistenza, 
 	      new Long(SolmrConstants.VALIDAZIONE_ALLEGATO));
 	  }
 	  request.setAttribute("allegatoDichiarazioneVO", allegatoDichiarazioneVO);
 	}
  catch (SolmrException se) 
  {
    SolmrLogger.info(this, " - dichiarazioneConsistenzaModCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+""+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  String operazione = request.getParameter("operazione");
  if(Validator.isNotEmpty(operazione)
    && "conferma".equalsIgnoreCase(operazione))
  {
  
    if(Validator.isNotEmpty(allegatoDichiarazioneVO.getExtIdDocumentoIndex()))
    {  
	    String note = request.getParameter("note");
	    String chkFirmato = request.getParameter("chkFirmato");
	    Long idTipoFirma = null;
	    if(Validator.isNotEmpty(chkFirmato))
	    {
	     try{
	       idTipoFirma = Long.parseLong(chkFirmato);
	     }catch(NumberFormatException numberFormatException){
	       SolmrLogger.error(numberFormatException, "Errore in fase di conversione del campo idTipoFirma.");
	       idTipoFirma = Long.parseLong("1");
	     }	     
	    }
	    
	    try
		  {
		    gaaFacadeClient.aggiornaValidazioneAgriWell(allegatoDichiarazioneVO, 
		      idTipoFirma, note, ruoloUtenza);
		      
		  //Controllo se sono su un ambiente JBoss 6.4 o un ambiente JBoss WildFly
			ResourceBundle res = ResourceBundle.getBundle("config");
			String ambienteDeploy = res.getString("ambienteDeploy");
			SolmrLogger.debug(this, "-- ambienteDeploy ="+ambienteDeploy);
			String dichiarazioneConsistenzaUrl ="";
			if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_CSI))
				dichiarazioneConsistenzaUrl = "../layout/";
			else if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_WILDFLY))
				dichiarazioneConsistenzaUrl = "/layout/";
			dichiarazioneConsistenzaUrl += "dichiarazioneConsistenza.htm";			
		    
		    
		    %>
		      <jsp:forward page="<%= dichiarazioneConsistenzaUrl %>" />
		    <%
		  }
		  catch (SolmrException se) 
		  {
		    SolmrLogger.info(this, " - dichiarazioneConsistenzaModCtrl.jsp - FINE PAGINA");
		    String messaggio = errMsg+""+se.toString();
		    request.setAttribute("messaggioErrore",messaggio);
		    request.setAttribute("pageBack", actionUrl);
		    %>
		      <jsp:forward page="<%= erroreViewUrl %>" />
		    <%
		    return;
		  }
		}
		else
		{
		  String messaggioErrore = "La validazione non ha ancora creato il documento";
          
      request.setAttribute("messaggioErrore", messaggioErrore);
		}    
  }
    

   	
 	

 	SolmrLogger.debug(this, " - dichiarazioneConsistenzaModCtrl.jsp - FINE PAGINA");
%>
  <jsp:forward page="<%= VIEW %>"/>

