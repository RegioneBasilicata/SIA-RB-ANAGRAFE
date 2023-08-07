<%@ page language="java"

    contentType="text/html"

    isErrorPage="true"

%>

<%@ page import="it.csi.solmr.exception.*"%>

<%@ page import="it.csi.solmr.util.*"%>

<%@ page import="it.csi.jsf.htmpl.*"%>

<%@ page import="java.util.Vector" %>

<%

try {

  String sLayout = (String) request.getAttribute("layout");

  java.io.InputStream is = application.getResourceAsStream(sLayout);

  Htmpl html = new Htmpl(is);

  response.setContentType("text/html");

  out.println(html.text());

}

catch ( Exception e ) {

  SolmrException exc = new SolmrException("La pagina richiesta non esiste");

  session.setAttribute("exception", exc);

  session.setAttribute("backPage", "javascript:history.back()");

%>

  <jsp:forward page ="errorPage.jsp"/>

<%

}

%>
