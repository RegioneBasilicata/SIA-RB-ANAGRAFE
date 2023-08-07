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
<%@ page import="it.csi.smranag.smrgaa.dto.fascicoli.InvioFascicoliVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%!
	public final static String VIEW = "../view/schedulaInvioASianView.jsp";
%>

<%

	String iridePageName = "schedulaInvioASianCtrl.jsp";
 	%>
 		<%@include file = "/include/autorizzazione.inc" %>
 	<%
 	SolmrLogger.debug(this, " - schedulaInvioASianCtrl.jsp - INIZIO PAGINA");
 	
 	ValidationError error = null;
 	String dichiarazioneConsistenzaCtrlUrl = "/ctrl/dichiarazioneConsistenzaCtrl.jsp";
 	String actionUrl = "../layout/dichiarazioneConsistenza.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione chedula invio a Sian. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
 	

 	ConsistenzaVO consistenzaVO = null;
 	InvioFascicoliVO invioFascicoliVO = null;
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  String[] orderBy = null;
  String operazione = request.getParameter("operazione");
  	
  Long idDichiarazioneConsistenza = null;
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

  
  //arrivo dall'elenco
  if(request.getParameter("radiobutton") != null) 
  {
   	idDichiarazioneConsistenza = new Long((String)request.getParameter("radiobutton"));
  }
  //sono nella pagina
  else
  {
    idDichiarazioneConsistenza = new Long((String)request.getParameter("idDichiarazioneConsistenza"));
  }  
  request.setAttribute("idDichiarazioneConsistenza", idDichiarazioneConsistenza);
  
 
  try
  {
    consistenzaVO = anagFacadeClient.findDichiarazioneConsistenzaByPrimaryKey(idDichiarazioneConsistenza);
  	request.setAttribute("consistenzaVO", consistenzaVO);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - schedulaInvioASianCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+"::"+AnagErrors.ERRORE_ESTRAZ_DATI_DICH_CONS+"::"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  try
  {
    invioFascicoliVO = gaaFacadeClient.getLastSchedulazione(idDichiarazioneConsistenza.longValue());
    request.setAttribute("invioFascicoliVO", invioFascicoliVO);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - schedulaInvioASianCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+"::"+AnagErrors.ERRORE_ESTRAZ_LAST_SCHED+"::"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  if("annullaInvio".equalsIgnoreCase(operazione))
  {
    if(Validator.isNotEmpty(invioFascicoliVO) && 
      SolmrConstants.SCHED_STATO_SCHEDULATO.equalsIgnoreCase(invioFascicoliVO.getStatoInvio()))
    {    
	    try
	    {     
	      gaaFacadeClient.deleteSchedulazione(invioFascicoliVO.getIdInvioFascicoli().longValue());
	    }
	    catch(SolmrException se) 
	    {
	      SolmrLogger.info(this, " - schedulaInvioASianCtrl.jsp - FINE PAGINA");
	      String messaggio = errMsg+"::"+AnagErrors.ERRORE_DELETE_SCHED+"::"+se.toString();
	      request.setAttribute("messaggioErrore",messaggio);
	      request.setAttribute("pageBack", actionUrl);
	      %>
	        <jsp:forward page="<%= erroreViewUrl %>" />
	      <%
	      return;
	    }
	    
	    try
		  {
		    invioFascicoliVO = gaaFacadeClient.getLastSchedulazione(idDichiarazioneConsistenza.longValue());
		    request.setAttribute("invioFascicoliVO", invioFascicoliVO);
		  }
		  catch(SolmrException se) 
		  {
		    SolmrLogger.info(this, " - schedulaInvioASianCtrl.jsp - FINE PAGINA");
		    String messaggio = errMsg+"::"+AnagErrors.ERRORE_ESTRAZ_LAST_SCHED+"::"+se.toString();
		    request.setAttribute("messaggioErrore",messaggio);
		    request.setAttribute("pageBack", actionUrl);
		    %>
		      <jsp:forward page="<%= erroreViewUrl %>" />
		    <%
		    return;
		  }
	    
	    request.setAttribute("messaggioErrore", SolmrConstants.MSG_SCHEDELAZIONE_OK_ANNULLATA);	    
	  }
	  else
	  {
      request.setAttribute("messaggioErrore", AnagErrors.ERRORE_DELETE_SCHED_CONFERMA);
      %>
        <jsp:forward page="<%= VIEW %>" />
      <%
      return;
	  }   
  
  }
  	
  if("conferma".equalsIgnoreCase(operazione))
  {
  
	  if(Validator.isEmpty(consistenzaVO.getDataProtocollo()))  
	  {	  
	    request.setAttribute("messaggioErrore", AnagErrors.ERRORE_VALIDAZ_NO_PROTOCOL);
      %>
        <jsp:forward page="<%= VIEW %>" />
      <%
      return;
	  }
	   
	  if(!"S".equalsIgnoreCase(consistenzaVO.getSchedulaFascicoli()))
	  {
	    request.setAttribute("messaggioErrore", AnagErrors.ERRORE_VALIDAZ_NO_FLAG_SCHEDULA);
      %>
        <jsp:forward page="<%= VIEW %>" />
      <%
      return;
	  }
  
	  boolean trovaSchedulazione = false;  
	  try
	  {
	    trovaSchedulazione = gaaFacadeClient.trovaSchedulazioneAttiva(
	      anagAziendaVO.getIdAzienda().longValue(), idDichiarazioneConsistenza.longValue());
	  }
	  catch(SolmrException se) 
	  {
	    SolmrLogger.info(this, " - schedulaInvioASianCtrl.jsp - FINE PAGINA");
	    String messaggio = errMsg+"::"+AnagErrors.ERRORE_ESTRAZ_AZIENDA_LAST_SCHED+"::"+se.toString();
	    request.setAttribute("messaggioErrore",messaggio);
	    request.setAttribute("pageBack", actionUrl);
	    %>
	      <jsp:forward page="<%= erroreViewUrl %>" />
	    <%
	    return;
	  }
  
  
	  if(trovaSchedulazione)
	  {
	    request.setAttribute("messaggioErrore", AnagErrors.ERRORE_VALIDAZ_GIA_SCHEDULATO_ALTRA_VALID);
      %>
        <jsp:forward page="<%= VIEW %>" />
      <%
      return;
	  }
	  
	  
	  if(Validator.isNotEmpty(invioFascicoliVO) && 
	    SolmrConstants.SCHED_STATO_INVIATO.equalsIgnoreCase(invioFascicoliVO.getStatoInvio()))
	  {
	    request.setAttribute("messaggioErrore", AnagErrors.ERRORE_VALIDAZ_GIA_SCHEDULATO);
      %>
        <jsp:forward page="<%= VIEW %>" />
      <%
      return;
	  }
	  
	  
	  try
    {
      InvioFascicoliVO invioFascicoliVOInvio = new InvioFascicoliVO();
      //Valorizzare l'id mi permette di cancellare il record. 
      //Cancello solo se stato schedulato!!!
      if(Validator.isNotEmpty(invioFascicoliVO) 
        && SolmrConstants.SCHED_STATO_SCHEDULATO.equalsIgnoreCase(invioFascicoliVO.getStatoInvio()))
      {
        invioFascicoliVOInvio.setIdInvioFascicoli(invioFascicoliVO.getIdInvioFascicoli());
      }
      invioFascicoliVOInvio.setIdDichiarazioneConsistenza(idDichiarazioneConsistenza);
      invioFascicoliVOInvio.setDataRichiesta(new Date());
      invioFascicoliVOInvio.setStatoInvio(SolmrConstants.SCHED_STATO_SCHEDULATO);
      //default sempre a S!!!!!
      invioFascicoliVOInvio.setFlagDatiAnagrafici("S");
      String flagTerreni = "N";
      if(request.getParameter("datiTerritoriali") != null)
      {
        flagTerreni = "S";
      }
      invioFascicoliVOInvio.setFlagTerreni(flagTerreni);
      String flagUv = "N";
      if(request.getParameter("datiUnitaVitate") != null)
      {
        flagUv = "S";
      }
      invioFascicoliVOInvio.setFlagUv(flagUv);
      String flagFabbricati = "N";
      if(request.getParameter("datiFabbricati") != null)
      {
        flagFabbricati = "S";
      }
      invioFascicoliVOInvio.setFlagFabbricati(flagFabbricati);
      String flagCc = "N";
      if(request.getParameter("datiContiCorrenti") != null)
      {
        flagCc = "S";
      }
      invioFascicoliVOInvio.setFlagCc(flagCc);
      invioFascicoliVOInvio.setNumTentativiInvio(1);
      invioFascicoliVOInvio.setDataAggiornamento(new Date());
      
      
      gaaFacadeClient.insertSchedulazione(invioFascicoliVOInvio, ruoloUtenza.getIdUtente().longValue());
      
      String msgNotifica = SolmrConstants.MSG_SCHEDELAZIONE_OK;
      Long idDichiarazioneConsistenzaLast = anagFacadeClient.getLastIdDichConsProtocollata(anagAziendaVO.getIdAzienda().longValue());
      if(Validator.isNotEmpty(idDichiarazioneConsistenzaLast))
      {
        if(idDichiarazioneConsistenzaLast.compareTo(idDichiarazioneConsistenza) != 0)
        {
          msgNotifica = SolmrConstants.MSG_SCHEDELAZIONE_NO_ULTIMA_PROTO;
        }
      }
      
      request.setAttribute("messaggioErrore", msgNotifica);
      
     
      
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - schedulaInvioASianCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+"::"+AnagErrors.ERRORE_INSERT_SCHED+"::"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    
    
    try
    {
      invioFascicoliVO = gaaFacadeClient.getLastSchedulazione(idDichiarazioneConsistenza.longValue());
      request.setAttribute("invioFascicoliVO", invioFascicoliVO);
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - schedulaInvioASianCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+"::"+AnagErrors.ERRORE_ESTRAZ_LAST_SCHED+"::"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }	  
	  
	  
	}
  
 

  %>
  	<jsp:forward page="<%= VIEW %>"/>
  <%

 	SolmrLogger.debug(this, " - schedulaInvioASianCtrl.jsp - FINE PAGINA");
%>


