<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>


<%@ page import="it.csi.solmr.dto.anag.*"%>
<%@ page import="it.csi.solmr.dto.anag.terreni.*"%>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="java.math.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.PlSqlCodeDescription" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%
	
	String iridePageName = "unitaArboreeConsolidaCtrl.jsp";
  
	%>
		 <%@include file="/include/autorizzazione.inc" %>
	<%
  
  SolmrLogger.debug(this, " - unitaArboreeConsolidaCtrl.jsp - INIZIO PAGINA");
  
  final String errMsg = "Impossibile procedere nella sezione consolida. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
    
  String actionUrl = "../layout/terreniUnitaArboreeElenco.htm";	
	String unitaArboreeConsolidaUrl = "/view/unitaArboreeConsolidaView.jsp";
	String terreniUnitaArboreeElencoCtrlUrl = "/ctrl/terreniUnitaArboreeElencoCtrl.jsp";
  String erroreViewUrl = "/view/erroreView.jsp";
  String operazione = request.getParameter("operazione");
  
  
  
	
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  
  String regimeUnitaArboreeConsolida = request.getParameter("regimeUnitaArboreeConsolida");
  
  // L'utente ha cliccato il pulsante annulla
  if(request.getParameter("annulla") != null) 
  {
    %>
      <jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" />
    <%
    return;
  }
  
  CompensazioneAziendaVO compensazioneAziendaVO = null;
  try
  {             
    compensazioneAziendaVO = gaaFacadeClient.getCompensazioneAzienda(anagAziendaVO.getIdAzienda().longValue());  
    request.setAttribute("compensazioneAziendaVO", compensazioneAziendaVO);    
  }
  catch(Exception ex)
  {
    SolmrLogger.info(this, " - unitaArboreeConsolidaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  if(compensazioneAziendaVO == null)
  {
    String messaggio = "L'azienda non ha mai compensato / consolidato la propria situazione aziendale";
    request.setAttribute("messaggio", messaggio);
  }
  else
  {  
  
	  if("excel".equals(request.getParameter("operazione")))
	  {      
	    String excelUrlCompensazione = "/servlet/ExcelUnitaVitateElencoCompensazioneServlet";
	    String descrizionePiano = "";
	    if(Validator.isNotEmpty(compensazioneAziendaVO)
	      && Validator.isNotEmpty(compensazioneAziendaVO.getDataConsolidamentoGis()))
	    {
	      descrizionePiano = DateUtils.formatDate(compensazioneAziendaVO.getDataConsolidamentoGis());
	    } 
	    request.setAttribute("descrizionePiano", descrizionePiano);
	    
	    %>
	      <jsp:forward page="<%=excelUrlCompensazione%>" />
	    <%
	    return;
	  }
	  
	  
	  try
	  {          
	    Vector<RiepilogoCompensazioneVO> vPostAllinea = gaaFacadeClient.getRiepilogoPostAllinea(
	      anagAziendaVO.getIdAzienda().longValue());
	    Vector<RiepilogoCompensazioneVO> vDirittiVitati = gaaFacadeClient.getRiepilogoDirittiVitati(
	      anagAziendaVO.getIdAzienda().longValue());
	    Vector<Vector<DirittoCompensazioneVO>> vDirittiCalcolati = gaaFacadeClient.getDirittiCalcolati(
	      anagAziendaVO.getIdAzienda().longValue());   
	   
	    request.setAttribute("vPostAllinea", vPostAllinea);
	    request.setAttribute("vDirittiVitati", vDirittiVitati);
	    request.setAttribute("vDirittiCalcolati", vDirittiCalcolati);  
	    
	    
	  }
	  catch(Exception ex)
	  {
	    SolmrLogger.info(this, " - unitaArboreeConsolidaCtrl.jsp - FINE PAGINA");
	    String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
	    request.setAttribute("messaggioErrore",messaggio);
	    request.setAttribute("pageBack", actionUrl);
	    %>
	      <jsp:forward page="<%= erroreViewUrl %>" />
	    <%
	    return;
	  }
	}  
			
 	%>
		<jsp:forward page="<%= unitaArboreeConsolidaUrl %>" />

