<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.dto.comune.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.math.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoFolderVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	

	String iridePageName = "fascicoloDematerializzatoCtrl.jsp";
	                       

	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

 	String fascicoloDematerializzatoUrl = "/view/fascicoloDematerializzatoView.jsp";
 	
 	String actionUrl = "../layout/documentiElenco.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione fascicolo dematerializzato."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  
  
       

 	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
 	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
 	
 	int idProcedimento = SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE;
  String codRuoloUtente = ruoloUtenza.getCodiceRuolo();
  Long idFolderMadre = null;
  boolean noEmptyFolder = true;
  Long idAzienda = anagAziendaVO.getIdAzienda();
  
  AgriWellEsitoFolderVO agriWellEsitoFolderVO = null;
  try
  {
    agriWellEsitoFolderVO = gaaFacadeClient
      .agriwellFindFolderByPadreProcedimentoRuolo(idProcedimento, codRuoloUtente, idFolderMadre, noEmptyFolder, idAzienda);
    request.setAttribute("agriWellEsitoFolderVO", agriWellEsitoFolderVO);
    
  }
  catch(Exception ex)
  {
    SolmrLogger.info(this, " - fascicoloDematerializzatoCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }  
  
 	      

%>

<jsp:forward page="<%= fascicoloDematerializzatoUrl %>"/>
