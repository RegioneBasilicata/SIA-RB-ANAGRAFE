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

 java.io.InputStream layout = application.getResourceAsStream("/layout/terreniImportaAsservimento.htm");

 Htmpl htmpl = new Htmpl(layout);

 %>
    <%@include file = "/view/remoteInclude.inc" %>
 <%

  //AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
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
  
  //Se sono in conferimento non permetto la ricerca per chiave catastale
  if("6".equals(idTitoloPossesso))
    htmpl.newBlock("blkConferimento");

  if(Validator.isNotEmpty(cuaa)) {
    htmpl.set("cuaa", cuaa.toUpperCase());
  }
  
  String provincia = request.getParameter("provincia");
  if(Validator.isNotEmpty(provincia)) {
    htmpl.set("sedelegProv", provincia);
  }
  String comune = request.getParameter("comune");
  if(Validator.isNotEmpty(comune)) {
    htmpl.set("descComune", comune);
  }
  String istatComune = request.getParameter("istatComune");
  if(Validator.isNotEmpty(istatComune)) {
    htmpl.set("istatComune", istatComune);
  }
  String sezione = request.getParameter("sezione");
  if(Validator.isNotEmpty(sezione)) {
    htmpl.set("sezione", sezione);
  }
  String foglio = request.getParameter("foglio");
  if(Validator.isNotEmpty(foglio)) {
    htmpl.set("foglio", foglio);
  }
  String particella = request.getParameter("particella");
  if(Validator.isNotEmpty(particella)) {
    htmpl.set("particella", particella);
  }

  
  if(elencoAziende != null) 
  {
    Iterator iteraAziende = elencoAziende.iterator();
    htmpl.newBlock("blkElencoAziende");
    while(iteraAziende.hasNext()) 
    {
      AnagAziendaVO anagAziendaElencoVO = (AnagAziendaVO)iteraAziende.next();
      htmpl.newBlock("blkElencoAziende.blkElenco");
      htmpl.set("blkElencoAziende.blkElenco.idAnagraficaAzienda", anagAziendaElencoVO.getIdAnagAzienda().toString());
      if(Validator.isNotEmpty(idAnagraficaAzienda) && idAnagraficaAzienda.compareTo(anagAziendaElencoVO.getIdAnagAzienda()) == 0) 
      {
        htmpl.set("blkElencoAziende.blkElenco.checked", "checked=\"checked\"");
      }
      htmpl.set("blkElencoAziende.blkElenco.partitaIva", anagAziendaElencoVO.getPartitaIVA());
      htmpl.set("blkElencoAziende.blkElenco.denominazione", anagAziendaElencoVO.getDenominazione());
      htmpl.set("blkElencoAziende.blkElenco.descComuneSedeLegale", anagAziendaElencoVO.getDescComune());
      if(Validator.isNotEmpty(anagAziendaElencoVO.getSedelegProv())) 
      {
        htmpl.set("blkElencoAziende.blkElenco.siglaProvSedeLegale", " ("+anagAziendaElencoVO.getSedelegProv()+")");
      }
      htmpl.set("blkElencoAziende.blkElenco.indirizzoSedeLegale", anagAziendaElencoVO.getSedelegIndirizzo());
    }
  }
   
  if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
   
  if(errors != null) 
  {
    HtmplUtil.setErrors(htmpl, errors, request, application);
  }
  

  %>
  <%= htmpl.text()%>
