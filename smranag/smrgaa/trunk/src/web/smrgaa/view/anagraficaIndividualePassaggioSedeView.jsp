  <%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
  %>
<%@ page import="it.csi.solmr.business.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.sql.Timestamp" %>

<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/anagraficaIndividualePassaggio_sede.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  PersonaFisicaVO personaTitolareVO = (PersonaFisicaVO)session.getAttribute("personaTitolareVO");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  AnagAziendaVO aziendaPassaggioVO = (AnagAziendaVO)session.getAttribute("anagPassaggioVO");

  String indietro = (String)session.getAttribute("indietro");

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  if(errors == null) {
    if(indietro == null || indietro.equals("")) {
      htmpl.set("sedelegIndirizzo",personaTitolareVO.getResIndirizzo());
      if(personaTitolareVO.getDescResProvincia() != null && !personaTitolareVO.getDescResProvincia().equals("")) {
        htmpl.set("sedelegProv",personaTitolareVO.getDescResProvincia());
        htmpl.set("sedelegComune",personaTitolareVO.getDescResComune());
        htmpl.set("sedelegCAP",personaTitolareVO.getResCAP());
      }
      else {
        htmpl.set("statoEstero",personaTitolareVO.getDescStatoEsteroResidenza());
        htmpl.set("sedelegCittaEstero",personaTitolareVO.getCittaResidenza());
        htmpl.set("sedelegProv","");
        htmpl.set("sedelegComune","");
        htmpl.set("sedelegCAP","");
      }
      HtmplUtil.setValues(htmpl,anagAziendaVO);
    }
    else {
      HtmplUtil.setValues(htmpl,aziendaPassaggioVO);
    }
  }

  else {
    HtmplUtil.setErrors(htmpl, errors, request, application);
    HtmplUtil.setValues(htmpl, request);
  }

%>
<%= htmpl.text()%>
