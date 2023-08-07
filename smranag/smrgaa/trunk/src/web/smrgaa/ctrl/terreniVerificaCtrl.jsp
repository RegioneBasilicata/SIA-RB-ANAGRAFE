<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>




<%!
  public static String PREVISIONE_PIANO_URL="/view/terreniVerificaView.jsp";
  public static String ATTENDERE_URL="/view/terreniVerificaAttendereView.jsp";
%>


<%

  String iridePageName = "terreniVerificaCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  try
  {
    WebUtils.removeUselessFilter(session, null);
    AnagFacadeClient anagClient = new AnagFacadeClient();
    AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

    if (anagClient.previsioneAnnoSucessivo(anagVO.getIdAzienda()))
    {
      //E' stata fatta una previsione del piano colturale
      %><jsp:forward page="<%=PREVISIONE_PIANO_URL%>" /><%
    }
    else
    {
      //Non è stata fatta una previsione del piano colturale
      Integer anno;
      anno=DateUtils.getCurrentYear();
      request.setAttribute("anno",anno);
      %><jsp:forward page="<%=ATTENDERE_URL%>" /><%
    }
  }
  catch(SolmrException s)
  {
    /**
     * La ValidationErrors è una collezione di ValidationError
     */
    ValidationErrors ve=new ValidationErrors();
    /**
     * Il ValidationError rappresenta un'errore
     */
    ve.add("error",new ValidationError(s.getMessage()));
    request.setAttribute("errors",ve);
    s.printStackTrace();
  }
  catch(Exception e)
  {
    /**
     * La ValidationErrors è una collezione di ValidationError
     */
    it.csi.solmr.util.ValidationErrors ve=new ValidationErrors();
    /**
     * Il ValidationError rappresenta un'errore
     */
    ve.add("error",new it.csi.solmr.util.ValidationError((String)SolmrErrors.get("GENERIC_SYSTEM_EXCEPTION")));
    request.setAttribute("errors",ve);
    e.printStackTrace();
  }
%>

