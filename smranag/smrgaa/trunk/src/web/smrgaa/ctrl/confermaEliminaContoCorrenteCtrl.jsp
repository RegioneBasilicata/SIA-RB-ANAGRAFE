<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String iridePageName = "confermaEliminaContoCorrenteCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  try
  {
    AnagFacadeClient anagClient = new AnagFacadeClient();
    RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
    if (request.getParameter("conferma")!=null)
    {
      anagClient.deleteContoCorrente(new Long(request.getParameter("idContoCorrente")),ruoloUtenza.getIdUtente());
      response.sendRedirect("../layout/conti_correnti.htm");
      return;
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
  if (request.getParameter("conferma")==null) {
    %>
      <jsp:forward page="../view/confermaEliminaContoCorrenteView.jsp" />
    <%
  }
  else
  {
    if ("dettaglio".equals(request.getParameter("pageFrom")))
    {
      %><jsp:forward page="../ctrl/contiCorrentiDetCtrl.jsp" /><%
    }
    else
    {
      %><jsp:forward page="../ctrl/contiCorrentiCtrl.jsp" /><%
    }
  }
%>
