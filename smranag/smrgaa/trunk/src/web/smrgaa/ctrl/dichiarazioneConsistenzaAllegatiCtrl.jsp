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
<%@ page import="it.csi.smranag.smrgaa.dto.stampe.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%!
	public final static String VIEW = "../view/dichiarazioneConsistenzaAllegatiView.jsp";
%>

<%

	String iridePageName = "dichiarazioneConsistenzaAllegatiCtrl.jsp";
 	%>
 		<%@include file = "/include/autorizzazione.inc" %>
 	<%
 	SolmrLogger.debug(this, " - dichiarazioneConsistenzaAllegatiCtrl.jsp - INIZIO PAGINA");
  String actionUrl = "../layout/dichiarazioneConsistenza.htm";
  String dichiarazioneConsistenzaAllegatiUrl = "../layout/dichiarazioneConsistenzaAllegati.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  String avantiUrl = "/ctrl/dichiarazioneConsistenzaAllegatiModCtrl.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione dichiarazione allegati."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

 	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
 	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
 	String[] orderBy = null;   	
 	Long idDichiarazioneConsistenza = null;
 	String operazione = request.getParameter("operazione");
 	
 	
 	String radiobutton = request.getParameter("radiobutton");

 	SolmrLogger.debug(this, "radiobutton: " + radiobutton);
 	
 	
 	
 	if(Validator.isNotEmpty(radiobutton)) 
  {
    if(radiobutton.contains("_"))
    {
      String messaggio = "Gli allegati possono essere inseriti solo a validazioni";
	    request.setAttribute("messaggioErrore",messaggio);
	    request.setAttribute("pageBack", actionUrl);
	    %>
	      <jsp:forward page="<%= erroreViewUrl %>" />
	    <%
	    return;
    }
  }
 	
 	
 	
 	

 	if(radiobutton != null) 
 	{
  	idDichiarazioneConsistenza = new Long(radiobutton);
 	}
 	//tasto indietro...
 	else if(request.getParameter("idDichiarazioneConsistenza") != null) 
  {
    idDichiarazioneConsistenza = new Long((String)request.getParameter("idDichiarazioneConsistenza"));
  }

 	SolmrLogger.debug(this, "idDichiarazioneConsistenza: " + idDichiarazioneConsistenza);
 	

  ConsistenzaVO consistenzaVO = null;
  try
  {
	 	consistenzaVO = anagFacadeClient.findDichiarazioneConsistenzaByPrimaryKey(idDichiarazioneConsistenza);
	 	request.setAttribute("consistenzaVO", consistenzaVO);
	 	
	 	
	 	 AllegatoDichiarazioneVO allegatoDichiarazioneVO = gaaFacadeClient
      .getAllegatoDichiarazioneFromIdDichiarazione(idDichiarazioneConsistenza, new Long(SolmrConstants.VALIDAZIONE_ALLEGATO));
     request.setAttribute("allegatoDichiarazioneVO", allegatoDichiarazioneVO);
	 	
	 	
	  
	  Vector<TipoAllegatoVO> vTipoAllegato = gaaFacadeClient
	    .getTipoAllegatoForValidazione(new Integer(consistenzaVO.getIdMotivo()), consistenzaVO.getDataInserimentoDichiarazione());
	  request.setAttribute("vTipoAllegato", vTipoAllegato);
	}
	catch(SolmrException se)
	{
	  SolmrLogger.info(this, " - dichiarazioneConsistenzaAllegatiCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
	}
	
	
	if(Validator.isEmpty(consistenzaVO.getNumeroProtocollo())) 
  {
    String messaggio = "impossibile inserire allegati a validazioni non protocollate";
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  
  
  
  if(Validator.isNotEmpty(operazione) && "avanti".equalsIgnoreCase(operazione))
  {
    String idTipoAllegato = request.getParameter("idTipoAllegato");
    if(Validator.isEmpty(idTipoAllegato))
    {
       String messaggio = "Occorre selezionare un tipo di allegato";
       request.setAttribute("messaggioErrore",messaggio);
       request.setAttribute("pageBack", dichiarazioneConsistenzaAllegatiUrl);
       %>
         <jsp:forward page="<%= erroreViewUrl %>" />
       <%
       return;
    }
    
    
    TipoAllegatoVO tipoAllegatoVO = gaaFacadeClient.getTipoAllegatoById(new Integer(idTipoAllegato));
    RichiestaTipoReportVO subReportElements[] = gaaFacadeClient
        .getElencoSubReportRichiesta(tipoAllegatoVO.getCodiceReportAllegato(), consistenzaVO.getDataInserimentoDichiarazione());
    if(Validator.isEmpty(subReportElements))
    {
      String messaggio = "Allegato non disponibile per la validazione selezionata";
      request.setAttribute("messaggioErrore",messaggio);
      %>
        <jsp:forward page="<%=VIEW%>" />
      <%
      return;
    }
    else
    {    
	    %>
	      <jsp:forward page="<%= avantiUrl %>" >
	        <jsp:param name="idDichiarazioneConsistenza" value="<%= idDichiarazioneConsistenza.toString() %>" /> 
	        <jsp:param name="idTipoAllegato" value="<%= idTipoAllegato %>" /> 
	      </jsp:forward>
	    <%
	    return;
	  }  
  }
   
  
  
  
  

 	
 	
 	
 	
 	

  SolmrLogger.debug(this, " - dichiarazioneConsistenzaAllegatiCtrl.jsp - FINE PAGINA");
 	%>
  <jsp:forward page="<%= VIEW %>"/>
 	
