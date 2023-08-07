<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>

<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/nuovaAziendaCCIAA.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  String radiobuttonAzienda=(String)request.getAttribute("radiobuttonAzienda");
  if (radiobuttonAzienda==null || "".equals(radiobuttonAzienda))
    radiobuttonAzienda=(String)request.getParameter("radiobuttonAzienda");

	if("2".equals(radiobuttonAzienda)) {
    	htmpl.set("radiobuttonAziendaChecked2","checked");
	}
    else if ("3".equals(radiobuttonAzienda)) {
    	htmpl.set("radiobuttonAziendaChecked3","checked");
	}
    else if(Validator.isNotEmpty(radiobuttonAzienda) && radiobuttonAzienda.equalsIgnoreCase(SolmrConstants.FLAG_S)) {
    	htmpl.set("radiobuttonAziendaCheckedProvenienza", "checked=\"checked\"");
    }
    else {
    	htmpl.set("radiobuttonAziendaChecked1","checked");
    }

  HtmplUtil.setValues(htmpl, request);
  HtmplUtil.setErrors(htmpl, errors, request, application);

%>

<%= htmpl.text() %>
