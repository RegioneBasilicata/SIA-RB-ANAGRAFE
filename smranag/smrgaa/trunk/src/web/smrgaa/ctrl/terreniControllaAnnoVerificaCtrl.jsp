<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>


<%!
  public static String ATTENDERE_URL="/view/terreniVerificaAttendereView.jsp";
%>


<%
  
  String iridePageName = "terreniControllaAnnoVerificaCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  try
  {
    AnagFacadeClient anagClient = new AnagFacadeClient();
    AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
    Integer anno;
    anno=new Integer(request.getParameter("radiobutton"));
    request.setAttribute("anno",anno);
    %><jsp:forward page="<%=ATTENDERE_URL%>" /><%
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

