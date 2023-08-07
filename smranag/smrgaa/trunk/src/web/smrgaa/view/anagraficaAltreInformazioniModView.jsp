<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>

<%
	Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/anagraficaAltreInformazioni_mod.htm");

  %>
    <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  AnagAziendaVO vo = (AnagAziendaVO)session.getAttribute("voAnagModifica");

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
  
  if(errors == null) 
  {
    if(vo != null && session.getAttribute("load") != null) 
    {
      session.removeAttribute("load");
      HtmplUtil.setValues(htmpl, vo);
      HtmplUtil.setErrors(htmpl, errors, request, application);
    }
    else 
    {
      HtmplUtil.setValues(htmpl, request);
      HtmplUtil.setErrors(htmpl, errors, request, application);
    }
  }
  else 
  {
    HtmplUtil.setErrors(htmpl, errors, request, application);
    HtmplUtil.setValues(htmpl, request);
  }
  
  htmpl.newBlock("blkImprenditore");
  String chkFlagIap = request.getParameter("chkFlagIap");
  if(Validator.isEmpty(request.getParameter("regime")))
  {
    if(Validator.isNotEmpty(anagAziendaVO.getFlagIap())
      && "S".equalsIgnoreCase(anagAziendaVO.getFlagIap()))
    {
      htmpl.set("blkImprenditore.chkFlagIap", "checked=\"checked\"", null);
    }
  }
  else
  {
    if("S".equalsIgnoreCase(chkFlagIap))
    {
      htmpl.set("blkImprenditore.chkFlagIap", "checked=\"checked\"", null);
    }
  }
  
  String obbligoGF = (String)request.getAttribute("obbligoGF");
  if("S".equalsIgnoreCase(obbligoGF))
  {
    htmpl.newBlock("blkCCSpeciale");
    
    if(session.getAttribute("modificaCampiAnagrafici") != null)
    {
      htmpl.set("blkCCSpeciale.disabled","disabled=\"disabled\"", null);
      htmpl.newBlock("blkCCSpeciale.blkFieldDisabled");
      htmpl.set("blkCCSpeciale.blkFieldDisabled.esoneroPagamentoGf", anagAziendaVO.getEsoneroPagamentoGF());
    }
    
    String esoneroPagamentoGf = request.getParameter("esoneroPagamentoGf");
	  if(Validator.isEmpty(request.getParameter("regime")))
	  {
	    if(Validator.isNotEmpty(anagAziendaVO.getEsoneroPagamentoGF())
	      && "S".equalsIgnoreCase(anagAziendaVO.getEsoneroPagamentoGF()))
	    {
	      htmpl.set("blkCCSpeciale.checkedEsoneroPagamentoGfS", "checked=\"checked\"", null);
	    }
	    else
	    {
	      htmpl.set("blkCCSpeciale.checkedEsoneroPagamentoGfN", "checked=\"checked\"", null);
	    } 
	  }
	  else
	  {
	    if("S".equalsIgnoreCase(esoneroPagamentoGf))
      {
        htmpl.set("blkCCSpeciale.checkedEsoneroPagamentoGfS", "checked=\"checked\"", null);
      }
      else
      {
        htmpl.set("blkCCSpeciale.checkedEsoneroPagamentoGfN", "checked=\"checked\"", null);
      } 
	  }
  }
  
%>
<%= htmpl.text()%>
