<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@page import="it.csi.solmr.etc.SolmrConstants"%>

<%

  java.io.InputStream layout = application.getResourceAsStream("/layout/importaTerreniGenerico.htm");
  
  it.csi.solmr.dto.CodeDescription[] elencoTipoTitoloPossesso = (it.csi.solmr.dto.CodeDescription[])request.getAttribute("elencoTipoTitoloPossesso");
  
  Htmpl htmpl = new Htmpl(layout);
  
  %>
      <%@include file = "/view/remoteInclude.inc" %>
  <%
  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  
  // Se si è verificato un errore 
  if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  
  // Carico la combo con il titolo possesso
  if(elencoTipoTitoloPossesso != null && elencoTipoTitoloPossesso.length > 0) 
  {
    for(int i = 0; i < elencoTipoTitoloPossesso.length; i++) 
    {
      CodeDescription codeDescription = (CodeDescription)elencoTipoTitoloPossesso[i];
      if (codeDescription!=null)
      {
        htmpl.newBlock("blkTitoliPossesso");
        htmpl.set("blkTitoliPossesso.idTitoloPossesso", codeDescription.getCode().toString());
        htmpl.set("blkTitoliPossesso.descrizione", codeDescription.getDescription());
        htmpl.newBlock("blkblkTitoliPossessoJavaScript");
        htmpl.set("blkblkTitoliPossessoJavaScript.index", ""+i);
        
        if (SolmrConstants.FLAG_S.equals(codeDescription.getCodeFlag()))
          htmpl.set("blkblkTitoliPossessoJavaScript.value", "../layout/terreniImporta.htm",null);
        else
          htmpl.set("blkblkTitoliPossessoJavaScript.value", "../layout/terreniImportaAsservimento.htm",null);  
      }
    }
  }
%>
<%= htmpl.text()%>
 