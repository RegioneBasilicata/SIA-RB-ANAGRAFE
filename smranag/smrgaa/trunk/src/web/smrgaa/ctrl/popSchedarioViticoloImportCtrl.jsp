<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>

<%
	
	String iridePageName = "popSchedarioViticoloImportCtrl.jsp";
	%>
		<%@include file="/include/autorizzazione.inc"%>
	<%
	
	String popSchedarioViticoloUrl = "/view/popSchedarioViticoloView.jsp";
	
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
	
	// Recupero le unita arboree importabili selezionate
	String[] storicoUnitaArborea = request.getParameterValues("idStoricoUnitaArborea");
	Vector temp = new Vector();
	for(int i = 0; i < storicoUnitaArborea.length; i++) {
		String id = (String)storicoUnitaArborea[i];
		temp.add(Long.decode(id));
	}
	Long[] elencoIdStoricoUnitaArborea = (Long[])temp.toArray(new Long[temp.size()]);
	
	// Effettuo importazione delle unità arboree selezionate
	try {
		anagFacadeClient.importUnitaArboreeBySchedario(elencoIdStoricoUnitaArborea, ruoloUtenza, anagAziendaVO, null, null);
	}
	catch(SolmrException se) {
		String messaggioErrore = AnagErrors.ERRORE_KO_UV_IMPORT_BY_SCHEDARIO;
		request.setAttribute("messagggioErrore", messaggioErrore);
		%>
			<jsp:forward page="<%= popSchedarioViticoloUrl %>" />
		<%
	}	
%>
<html>
	<head>
		<script type="text/javascript">
			window.opener.location.href='../layout/terreniUnitaArboreeElenco.htm';
			window.close();
		</script>
	</head>
</html>
	

