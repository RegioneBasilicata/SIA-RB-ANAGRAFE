<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="java.util.Vector" %>
<%@ page import="it.csi.smranag.smrgaa.dto.stampe.TipoReportVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>

<%

  String iridePageName = "scelta_stampaCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%
  
  String VIEW_SCELTA_STAMPA="/view/scelta_stampaView.jsp";
  String stampaUrl = "/layout/stampe.htm";
  String actionUrl = "/layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione scelta stampa" +
    "Contattare l'assistenza comunicando il seguente messaggio: ";
    
  final String codiceStampa = "STAMPE";
  
  String operazione = request.getParameter("operazione");
    
    
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  
  
  
  if (Validator.isNotEmpty(operazione) && "stampa".equalsIgnoreCase(operazione))
  {
   String tipoStampa = request.getParameter("tipoStampa");
   String idDichiarazioneConsistenza = request.getParameter("idDichiarazioneConsistenza");
      
   %>
     <jsp:forward page="<%=stampaUrl%>" >
        <jsp:param name="tipoStampa" value="<%=tipoStampa %>"/>
        <jsp:param name="idDichiarazioneConsistenza" value="<%=idDichiarazioneConsistenza %>"/>
     </jsp:forward>
   <%
  }
  else
  {
    String idDichiarazioneConsistenza = request.getParameter("idDichiarazioneConsistenza");
    Long idPianoRiferimento = null;
	  // Nel caso in cui non sia valorizzato vuol dire che è la prima volta che accedo alla pagina e quindi imposto il piano di riferimento alla data odierna
	  if(!Validator.isNotEmpty(idDichiarazioneConsistenza)) 
	  {
	    idPianoRiferimento = new Long("-1");
	  }
	  else 
	  {
	    idPianoRiferimento = Long.decode(idDichiarazioneConsistenza);
	  }
  
    try
    {
      Vector<TipoReportVO> vTipoReport = null;
	    if(idPianoRiferimento.compareTo(new Long(0)) > 0)
	    {
	      Vector<TipoReportVO> vTipoReportTmp = gaaFacadeClient.getElencoTipoReportByValidazione(codiceStampa, idPianoRiferimento);
	      vTipoReport = vTipoReportTmp;
	    }
	    else
	    {
	      Vector<TipoReportVO> vTipoReportTmp = gaaFacadeClient.getElencoTipoReport(codiceStampa, null);
	      for(int i=0;i<vTipoReportTmp.size();i++)
	      {
	        if(!"S".equalsIgnoreCase(vTipoReportTmp.get(i).getFlagInseribile()))
	        {
	          if(vTipoReport == null)
	             vTipoReport = new Vector<TipoReportVO>();
	             
	          vTipoReport.add(vTipoReportTmp.get(i)); 
	        }
	      }
	      
	    }
	    request.setAttribute("vTipoReport", vTipoReport);
  
    }
    catch (SolmrException se) {
      SolmrLogger.info(this, " - scelta_stampaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+""+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
  
  
    %>
      <jsp:forward page="<%=VIEW_SCELTA_STAMPA%>" />
    <%
  }
%>