<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%

  String iridePageName = "fontiCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  String fontiUrl = "/view/fontiView.jsp";

  %>
    <jsp:forward page = "<%=fontiUrl%>" />
  <%

%>

