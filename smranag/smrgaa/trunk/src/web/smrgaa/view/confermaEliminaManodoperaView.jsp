<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>


<%

  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/confermaEliminaManodopera.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  htmpl.set("pageFrom", request.getParameter("pageFrom"));
  
  String idManodopera = request.getParameter("idManodopera");
  htmpl.set("idManodopera", idManodopera);
  
  HtmplUtil.setErrors(htmpl, (ValidationErrors)request.getAttribute("errors"), request, application);
  out.print(htmpl.text());

%>
