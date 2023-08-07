<%@ page import="it.csi.solmr.util.*" %>

<%
	
	// Pulisco la sessione dai filtri di altre sezioni
  String noRemove = "";
  WebUtils.removeUselessFilter(session, noRemove);

  	String iridePageName = "dirittiCtrl.jsp";
  	%>
  		<%@include file = "/include/autorizzazione.inc" %>
  	<%

  	String dirittiUrl = "/view/dirittiView.jsp";

  	%>
    	<jsp:forward page = "<%=dirittiUrl%>" />
  	<%

%>

