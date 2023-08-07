<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>

<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/nuovaAziendaSede.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("insAnagVO");

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  	
  htmpl.set("radiobuttonAzienda",(String)request.getAttribute("radiobuttonAzienda"));
	request.setAttribute("radiobuttonAzienda", (String)request.getAttribute("radiobuttonAzienda"));
	htmpl.set("cuaaProvenienza",(String)request.getAttribute("cuaaProvenienza"));
	request.setAttribute("cuaaProvenienza", (String)request.getAttribute("cuaaProvenienza"));
	
  // Prima visualizzazione della pagina
  if(errors == null) {
    if(anagAziendaVO != null) {
      if(anagAziendaVO.getUnitaProduttiva() != null) {
        if(anagAziendaVO.getUnitaProduttiva().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
          htmpl.set("checkedSi","checked");
        }
        else {
          htmpl.set("checkedNo","checked");
        }
      }
      HtmplUtil.setValues(htmpl, anagAziendaVO);
    }
  }
  // In caso di errori.
  else {
    if(anagAziendaVO.getUnitaProduttiva() != null) {
      if(anagAziendaVO.getUnitaProduttiva().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
        htmpl.set("checkedSi","checked");
      }
      else {
        htmpl.set("checkedNo","checked");
      }
    }
    HtmplUtil.setValues(htmpl, request);
    HtmplUtil.setErrors(htmpl, errors, request, application);
  }

%>

<%= htmpl.text() %>
