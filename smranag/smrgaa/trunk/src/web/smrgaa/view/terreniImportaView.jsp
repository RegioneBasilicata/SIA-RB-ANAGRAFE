<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>

<%

 java.io.InputStream layout = application.getResourceAsStream("/layout/terreniImporta.htm");

 Htmpl htmpl = new Htmpl(layout);

 %>
    <%@include file = "/view/remoteInclude.inc" %>
 <%
 String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 Vector elencoAziende = (Vector)request.getAttribute("elencoAziende");
 String cuaa = (String)session.getAttribute("cuaa");
 ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
 Long idAnagraficaAzienda = (Long)request.getAttribute("idAnagraficaAzienda");
 it.csi.solmr.dto.CodeDescription[] elencoTipoTitoloPossesso = (it.csi.solmr.dto.CodeDescription[])request.getAttribute("elencoTipoTitoloPossesso");
 String idTitoloPossesso = request.getParameter("idTitoloPossesso");

 // Nuova gestione fogli di stile
 htmpl.set("head", head, null);
 htmpl.set("header", header, null);
 htmpl.set("footer", footer, null);
 
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
        if(idTitoloPossesso != null && idTitoloPossesso.equals(codeDescription.getCode().toString()))
          htmpl.set("blkTitoliPossesso.selected", "selected=\"selected\"");
        
        htmpl.newBlock("blkblkTitoliPossessoJavaScript");
        htmpl.set("blkblkTitoliPossessoJavaScript.index", ""+i);
        
        if (SolmrConstants.FLAG_S.equals(codeDescription.getCodeFlag()))
          htmpl.set("blkblkTitoliPossessoJavaScript.value", "../layout/terreniImporta.htm",null);
        else
          htmpl.set("blkblkTitoliPossessoJavaScript.value", "../layout/terreniImportaAsservimento.htm",null);  
      }
    }
  }

 if(Validator.isNotEmpty(cuaa)) {
   htmpl.set("cuaa", cuaa.toUpperCase());
 }

 if(Validator.isNotEmpty(messaggioErrore)) {
   htmpl.newBlock("blkErrore");
   htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
 }
 else 
 {
   if(elencoAziende != null && elencoAziende.size() > 0) {
     Iterator iteraAziende = elencoAziende.iterator();
     htmpl.newBlock("blkElencoAziende");
     while(iteraAziende.hasNext()) {
       AnagAziendaVO anagAziendaElencoVO = (AnagAziendaVO)iteraAziende.next();
       htmpl.newBlock("blkElencoAziende.blkElenco");
       htmpl.set("blkElencoAziende.blkElenco.idAnagraficaAzienda", anagAziendaElencoVO.getIdAnagAzienda().toString());
       if(Validator.isNotEmpty(idAnagraficaAzienda) && idAnagraficaAzienda.compareTo(anagAziendaElencoVO.getIdAnagAzienda()) == 0) {
         htmpl.set("blkElencoAziende.blkElenco.checked", "checked=\"checked\"");
       }
       htmpl.set("blkElencoAziende.blkElenco.partitaIva", anagAziendaElencoVO.getPartitaIVA());
       htmpl.set("blkElencoAziende.blkElenco.denominazione", anagAziendaElencoVO.getDenominazione());
       htmpl.set("blkElencoAziende.blkElenco.descComuneSedeLegale", anagAziendaElencoVO.getDescComune());
       if(Validator.isNotEmpty(anagAziendaElencoVO.getSedelegProv())) {
         htmpl.set("blkElencoAziende.blkElenco.siglaProvSedeLegale", " ("+anagAziendaElencoVO.getSedelegProv()+")");
       }
       htmpl.set("blkElencoAziende.blkElenco.indirizzoSedeLegale", anagAziendaElencoVO.getSedelegIndirizzo());
       if(Validator.isNotEmpty(anagAziendaElencoVO.getDataCessazione())) {
         htmpl.set("blkElencoAziende.blkElenco.dataCessazione", DateUtils.formatDate(anagAziendaElencoVO.getDataCessazione()));
       }
       if(anagAziendaElencoVO.isFlagAziendaProvvisoria()) {
         htmpl.set("blkElencoAziende.blkElenco.flagFaseInsediamento", SolmrConstants.FLAG_S);
       }
       else {
         htmpl.set("blkElencoAziende.blkElenco.flagFaseInsediamento", SolmrConstants.FLAG_N);
       }
     }
   }
   if(errors != null && errors.size() > 0) {
     HtmplUtil.setErrors(htmpl, errors, request, application);
   }
 }

%>
<%= htmpl.text()%>
