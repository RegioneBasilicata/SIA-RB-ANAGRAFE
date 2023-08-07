  <%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
  %>

<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>

<%

String msg = ((Exception)session.getAttribute("exception")).getMessage();
session.removeAttribute("exception");
java.io.InputStream layout = application.getResourceAsStream("/layout/errore.htm");
Htmpl htmpl = new Htmpl(layout);

%>
   <%@include file = "/view/remoteInclude.inc" %>
<%

// Nuova gestione fogli di stile
htmpl.set("head", head, null);
htmpl.set("header", header, null);
htmpl.set("footer", footer, null);

String daLayout = (String)request.getAttribute("layout");

if (daLayout!=null) {
  StringTokenizer st = new StringTokenizer(daLayout, "/");
  int counter = 0;
  while (st.hasMoreTokens()) {
    counter++;
    st.nextToken();
  }
  String daPath = "";
  for (int i=0; i<(counter-2); i++) {
    daPath = "../"+daPath;
  }
  if (daPath.length()!=0) {
    String path = (String)session.getAttribute("pathToFollow");
    if (path == null) path = "sispie";
    daPath += path;
    htmpl.bset("pathToFollow", daPath);
    htmpl.set("pathToFollow", daPath);
  }
}
htmpl.set("exception", msg);

%>

<%= htmpl.text() %>

