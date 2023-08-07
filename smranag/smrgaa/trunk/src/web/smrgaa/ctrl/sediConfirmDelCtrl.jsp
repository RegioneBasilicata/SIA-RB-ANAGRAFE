<%@ page language="java"
    contentType="text/html"
%>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  String iridePageName = "sediConfirmDelCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  AnagFacadeClient client = new AnagFacadeClient();
  AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  UteVO uteVO = null;
  ValidationException valEx = null;
  ValidationError error = null;
  ValidationErrors errors = null;
  String url = "/view/sediView.jsp";
  Long idUte = null;

  if(request.getParameter("submit")!= null){
    idUte = (Long)session.getAttribute("idUte");
    session.removeAttribute("idUte");
    try {
      if(anagVO.getDataCessazione()!=null || anagVO.getDataFineVal()!=null){
        error = new ValidationError(""+AnagErrors.get("AZIENDA_CESSATA_O_DATA_FINEVAL"));
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher("/view/sediView.jsp").forward(request, response);
        return;
      }
      else{
        client.deleteUte(idUte);
        url = "/layout/sedi.htm";
      }
    }
    catch (SolmrException ex) {
      error=new ValidationError(ex.getMessage());
      errors = new ValidationErrors();
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }
  }

  else if(request.getParameter("submit2")!=null){
    url = "/layout/sedi.htm";
  }

  else{
    url = "/view/confirmDelView.jsp";
  }
%>
<jsp:forward page="<%=url%>"/>