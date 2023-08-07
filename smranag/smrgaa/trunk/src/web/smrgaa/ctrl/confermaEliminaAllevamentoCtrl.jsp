<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%!
  public static String ELENCO_URL = "../layout/allevamenti.htm";
  public static String VALIDATE_URL = "/ctrl/allevamentiCtrl.jsp";
  public static String VIEW = "/view/confermaEliminaAllevamentoView.jsp";
  public static String DETTAGLIO_URL = "../layout/allevamentiDet.htm";
  public static String VALIDATE_DET_URL = "/ctrl/allevamentiDetCtrl.jsp";
%>

<%

  String iridePageName = "confermaEliminaAllevamentoCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  SolmrLogger.debug(this, " - confermaEliminaAllevamentoCtrl.jsp - INIZIO PAGINA");

  try{
    AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

    SolmrLogger.debug(this, "request.getParameter(conferma.x): " + request.getParameter("conferma.x"));
    SolmrLogger.debug(this, "request.getParameter(annulla.x): " + request.getParameter("annulla.x"));
    SolmrLogger.debug(this, "request.getParameter(radiobutton): " + request.getParameter("radiobutton"));

    if (request.getParameter("conferma.x") != null)
    {
      anagFacadeClient.deleteAllevamentoAll(new Long(request.getParameter("radiobutton")));
      response.sendRedirect(ELENCO_URL);
      return;
    }
    else if (request.getParameter("annulla.x") != null)
    {
      if (request.getParameter("pageFrom") != null && request.getParameter("pageFrom").equalsIgnoreCase("dettaglio") ){
        SolmrLogger.debug(this, "1request.getParameter(\"pageFrom\"): "+request.getParameter("pageFrom"));
        session.setAttribute("idAllevamenti", new Long(request.getParameter("radiobutton")));
        response.sendRedirect(DETTAGLIO_URL);
        return;
      }
      SolmrLogger.debug(this, "request.getParameter(\"pageFrom\"): "+request.getParameter("pageFrom"));
      response.sendRedirect(ELENCO_URL);
      return;
    }
    else
    {
      AllevamentoAnagVO all = anagFacadeClient.getAllevamento(new Long(request.getParameter("radiobutton")));
      if(all.getDataFine()!=null)
      {
        ValidationErrors err = new ValidationErrors();
        err.add("error", new ValidationError("Allevamento storicizzato. Impossibile procedere."));
        request.setAttribute("errors", err);
      %><jsp:forward page="../ctrl/allevamentiCtrl.jsp" /><%
        return;
      }
    }

  }
  catch(Exception e)
  {
    if (request.getParameter("pageFrom") != null && request.getParameter("pageFrom").equalsIgnoreCase("dettaglio"))
    {
      //rimanda al dettaglio allevamenti
      throwValidation(e.getMessage(), VALIDATE_DET_URL);
    }
    else
    {
      //rimanda all'elenco allevamenti
      throwValidation(e.getMessage(), VALIDATE_URL);
    }
  }

  %><jsp:forward page="<%= VIEW %>"/><%

  SolmrLogger.debug(this, " - confermaEliminaAllevamentoCtrl.jsp - FINE PAGINA");
%>

<%!
  private void throwValidation(String msg, String validateUrl) throws ValidationException
  {
    ValidationException valEx = new ValidationException(msg, validateUrl);
    valEx.addMessage(msg, "exception");
    throw valEx;
  }
%>
