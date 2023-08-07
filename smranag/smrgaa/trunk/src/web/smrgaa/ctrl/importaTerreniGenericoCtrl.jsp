<%@ page language="java"
         contentType="text/html"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>

<%

  String iridePageName = "importaTerreniGenericoCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  String terreniImportaUrl = "/view/importaTerreniGenericoView.jsp";
  String messaggioErrore = null;
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
   
   // TITOLO DI POSSESSO
  it.csi.solmr.dto.CodeDescription[] elencoTipoTitoloPossesso = null;
  try 
  {
    elencoTipoTitoloPossesso = anagFacadeClient.getListTipiTitoloPossesso(SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION);
    request.setAttribute("elencoTipoTitoloPossesso", elencoTipoTitoloPossesso);
  }
  catch(Exception se) 
  {
    messaggioErrore = (String)AnagErrors.ERRORE_KO_TITOLO_POSSESSO;
    request.setAttribute("messaggioErrore", messaggioErrore);
    %>
      <jsp:forward page="<%= terreniImportaUrl %>" />
    <%
  }
   

  // Mando alla view che permette di selezionare cosa si vuole importare
  %>
    <jsp:forward page= "<%= terreniImportaUrl %>" />
  <%

%>

