<%@ page language="java"
  contentType="text/html"
  isErrorPage="true"
%>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.sql.Timestamp" %>

<%

  String iridePageName = "personaDettCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  PersonaFisicaVO pfVO = null;
  SolmrLogger.debug(this, "personaDettCtrl - INIZIO");
  String url = "/view/personaDettView.jsp";
  String indietro = "/view/elencoPersoneView.jsp";
  ValidationException valEx=null;
  Validator validator = null;
  ValidationErrors errors = new ValidationErrors();

  if(request.getParameter("indietro") != null){
    url = indietro;
  }
  else{
    try{
      if(request.getParameter("idPersonaFisica")!=null){
        pfVO = anagFacadeClient.findByPrimaryKey(new Long(request.getParameter("idPersonaFisica")));
        ComuneVO comune = anagFacadeClient.getComuneByISTAT(pfVO.getResComune());
        pfVO.setDescResProvincia(anagFacadeClient.getSiglaProvinciaByIstatProvincia(comune.getIstatProvincia()));

        


        if(comune.getFlagEstero()!=null && comune.getFlagEstero().equals(SolmrConstants.FLAG_S))
          pfVO.setStatoEsteroRes(pfVO.getDescResComune());
        session.setAttribute("personaFisicaVO",pfVO);
      }
      else {
        ValidationError error = new ValidationError("Selezionare una persona");
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(indietro).forward(request, response);
        return;
      }
    }catch(SolmrException ex){
      ValidationError error = new ValidationError(ex.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(indietro).forward(request, response);
    }

  }
%>
<jsp:forward page ="<%=url%>" />
<%
%>