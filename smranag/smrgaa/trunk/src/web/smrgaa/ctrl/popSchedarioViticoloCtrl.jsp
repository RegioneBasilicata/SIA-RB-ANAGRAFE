<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>


<%
	
	String iridePageName = "popSchedarioViticoloCtrl.jsp";
	%>
		<%@include file="/include/autorizzazione.inc"%>
	<%
	
	String popSchedarioViticoloUrl = "/view/popSchedarioViticoloView.jsp";
	
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	String messaggioErrore = null;
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	
	// Recupero i valori relativi alla destinazione produttiva
  TipoUtilizzoVO[] elencoTipiUsoSuolo = null;
  try 
  {
    String[] orderBy = {(String)SolmrConstants.ORDER_BY_TIPO_UTILIZZO_DESCRIZIONE};
    elencoTipiUsoSuolo = anagFacadeClient.getListTipiUsoSuoloByTipo(SolmrConstants.TIPO_UTILIZZO_VIGNETO, true, orderBy);
    request.setAttribute("elencoTipiUsoSuolo", elencoTipiUsoSuolo);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - popSchedarioViticoloCtrl.jsp - FINE PAGINA");
    String messaggio = AnagErrors.ERRORE_KO_DESTINAZIONE_PRODUTTIVA+".\n"+se.toString();
    request.setAttribute("messaggioErrore", messaggio);
    %>
      <jsp:forward page="<%= popSchedarioViticoloUrl %>" />
    <%
  }
	
	// Cerco le unità arboree importabili
	StoricoParticellaVO[] elencoUnitaArboreeImportabili = null;
	try 
	{
		String[] orderBy = {SolmrConstants.ORDER_BY_DEFAULT_UV_IMPORTABILI};
		elencoUnitaArboreeImportabili = anagFacadeClient.getListStoricoParticelleArboreeImportabili(anagAziendaVO.getIdAzienda(), orderBy);
		request.setAttribute("elencoUnitaArboreeImportabili", elencoUnitaArboreeImportabili);
	}
	catch(SolmrException se) 
	{
		messaggioErrore = AnagErrors.ERRORE_KO_SEARCH_UV_IMPORT_BY_SCHEDARIO;
		request.setAttribute("messaggioErrore", messaggioErrore);
		%>
			<jsp:forward page="<%= popSchedarioViticoloUrl %>" />
		<%
	}
 	
 	// Vado alla pop-up di elenco delle particelle importabili
 	%>
		<jsp:forward page="<%= popSchedarioViticoloUrl %>" />
	<%
%>

