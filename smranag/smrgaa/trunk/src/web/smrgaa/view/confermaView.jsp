
<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.jsf.htmpl.*"%>
<%@ page import="it.csi.solmr.util.*"%>
<%@ page import="it.csi.solmr.dto.anag.*"%>

<%
  java.io.InputStream layout = application
      .getResourceAsStream("/layout/conferma.htm");
  Htmpl htmpl = new Htmpl(layout);
%>
<%@include file="/view/remoteInclude.inc"%>
<%
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  // Recupero l'array di id
  String idParticella[] = request
      .getParameterValues("idParticella");
  int len = idParticella == null ? 0 : idParticella.length;
  for (int i = 0; i < len; ++i)
  {
    htmpl.newBlock("blkHidden");
    htmpl.set("blkHidden.name", "idParticella");
    htmpl.set("blkHidden.value", idParticella[i]);
  }
    htmpl.newBlock("blkHidden");
    htmpl.set("blkHidden.name", "anno");
    htmpl.set("blkHidden.value", request.getParameter("anno"));

    htmpl.set("nomeValore", "from");
    htmpl.set("idValore", request.getParameter("from"));

  String messaggio = (String) request.getAttribute("messaggio");
  if (Validator.isNotEmpty(messaggio))
  {
    htmpl.set("messaggio", messaggio);
  }

  String urlForm = (String) request.getAttribute("urlForm");
  if (Validator.isNotEmpty(urlForm))
  {
    htmpl.set("action", urlForm);
  }
%>
<%=htmpl.text()%>


