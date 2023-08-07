<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%

  String iridePageName = "gestoreFascicolo_SIANCtrl.jsp";
%>
        <%@include file = "/include/autorizzazione.inc" %>

<%
    String gestoreFascicoloURL = "/view/gestoreFascicolo_SIANView.jsp";    

%>

<jsp:forward page = "<%=gestoreFascicoloURL%>"/>
