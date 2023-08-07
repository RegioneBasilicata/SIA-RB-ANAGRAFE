<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>

<%


  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/anagraficaSede_mod.htm");
  
  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%
  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  String richiestaModifica = (String)session.getAttribute("richiestaModifica");
  session.removeAttribute("richiestaModifica");
  if(richiestaModifica != null) 
  {
    htmpl.set("richiestaModifica",richiestaModifica);
  }
  else
  {
    htmpl.set("richiestaModifica","");
  }
  
  
  if(session.getAttribute("modificaCampiAnagrafici") != null)
  {
    htmpl.set("disabled","disabled=\"disabled\"", null);
    htmpl.newBlock("blkFieldDisabled");
    htmpl.set("blkFieldDisabled.sedelegIndirizzo", anagAziendaVO.getSedelegIndirizzo());
    htmpl.set("blkFieldDisabled.sedelegProv", anagAziendaVO.getSedelegProv());
    htmpl.set("blkFieldDisabled.descComune", anagAziendaVO.getDescComune());
    htmpl.set("blkFieldDisabled.sedelegCAP", anagAziendaVO.getSedelegCAP());
    htmpl.set("blkFieldDisabled.sedelegEstero", anagAziendaVO.getSedelegEstero());
    htmpl.set("blkFieldDisabled.sedelegCittaEstero", anagAziendaVO.getSedelegCittaEstero());
  }
  else
  {
    htmpl.newBlock("blkFieldEnabled");
  }
  
  
  
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  if(errors == null) 
  {}
  else 
  {
    HtmplUtil.setErrors(htmpl, errors, request, application);
    HtmplUtil.setValues(htmpl, request);
  }
  
  if(anagAziendaVO!=null)
    HtmplUtil.setValues(htmpl, anagAziendaVO);
  
  %>
<%= htmpl.text()%>


