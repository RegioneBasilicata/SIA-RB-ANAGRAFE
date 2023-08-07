<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>


<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>

<%
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/terreniVerifica.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagFacadeClient client = new AnagFacadeClient();
  AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

  HtmplUtil.setErrors(htmpl, (ValidationErrors)request.getAttribute("errors"), request, application);

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  htmpl.set("annoCorrente",""+DateUtils.getCurrentYear().intValue());
  htmpl.set("annoSuccessivo",""+(DateUtils.getCurrentYear().intValue()+1));

  out.print(htmpl.text());
%>

<%!
  private void errErrorValExc(Htmpl htmpl, HttpServletRequest request, Throwable exc, javax.servlet.ServletContext application)
  {

    if (exc instanceof it.csi.solmr.exception.ValidationException){
      ValidationErrors valErrs = new ValidationErrors();
      valErrs.add("error", new ValidationError(exc.getMessage()) );
      HtmplUtil.setErrors(htmpl, valErrs, request, application);
    }
  }
%>
