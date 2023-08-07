<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>

<%

  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/confermaNuovaAzienda.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  String pageNext=(String)request.getAttribute("pageNext");
  String pageBack=(String)request.getAttribute("pageBack");
  String domanda=(String)request.getAttribute("domanda");
  String CUAA=(String)request.getAttribute("CUAA");
  String CUAASubentro=(String)request.getAttribute("CUAASubentro");
  String radiobuttonAzienda=(String)request.getAttribute("radiobuttonAzienda");
  String idAziendaSubentro=(String)request.getAttribute("idAziendaSubentro");
  String cuaaProvenienza=(String)request.getAttribute("cuaaProvenienza");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  htmpl.set("pageNext", pageNext);
  htmpl.set("pageBack", pageBack);
  htmpl.set("domanda", domanda);
  htmpl.set("CUAA", CUAA);
  htmpl.set("CUAASubentro", CUAASubentro);
  htmpl.set("radiobuttonAzienda", radiobuttonAzienda);
  htmpl.set("idAziendaSubentro", idAziendaSubentro);
  htmpl.set("cuaaProvenienza", cuaaProvenienza);
  out.print(htmpl.text());

%>
