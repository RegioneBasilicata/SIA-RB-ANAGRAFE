<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>

<%

 Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/anagraficaSocieta_cambiaRappresentante.htm");

 %>
    <%@include file = "/view/remoteInclude.inc" %>
 <%

 AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
 PersonaFisicaVO personaVO = (PersonaFisicaVO)session.getAttribute("personaRappresentanteVO");
 if(personaVO == null) {
   personaVO = (PersonaFisicaVO)request.getAttribute("rappresentanteVO");
 }
 AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

 // Nuova gestione fogli di stile
 htmpl.set("head", head, null);
 htmpl.set("header", header, null);
 htmpl.set("footer", footer, null);
 ValidationErrors errors=(ValidationErrors)request.getAttribute("errors");

 if (errors != null && errors.get("warning") != null) {
   htmpl.set("controlloCF","true");
   errors=null;
 }

 if (errors!=null) {
   HtmplUtil.setErrors(htmpl,errors,request, application);
 }

 if (request.getParameter("cerca.x") != null) {
   HtmplUtil.setValues(htmpl, personaVO, (String)session.getAttribute("pathToFollow"));
 }
 else {
   if (request.getParameter("salva.x") != null) {
     if (errors!=null) {
       HtmplUtil.setErrors(htmpl,(ValidationErrors)request.getAttribute("errors"),request, application);
     }
     HtmplUtil.setValues(htmpl, personaVO, (String)session.getAttribute("pathToFollow"));
     HtmplUtil.setValues(htmpl,request);
   }
   else {
     HtmplUtil.setValues(htmpl, personaVO, (String)session.getAttribute("pathToFollow"));
     HtmplUtil.setValues(htmpl,request);
   }
 }

 String sesso=personaVO==null?null:personaVO.getSesso();
 String nome=personaVO==null?null:personaVO.getNome();
 if (sesso!=null) {
   if ("F".equalsIgnoreCase(sesso)) {
     htmpl.set("sessoF","checked");
   }
   else
     if ("M".equalsIgnoreCase(sesso)) {
       htmpl.set("sessoM","checked");
     }
   }


 // Informazioni relative al titolo di studio
 Vector elencoTitoliStudio = null;
 Long idTitoloStudio = (Long)session.getAttribute("idTitoloStudio");
 try {
   elencoTitoliStudio = anagFacadeClient.getTitoliStudio();
 }
 catch(SolmrException se) {}

 if(elencoTitoliStudio != null) {
   Iterator iteraTitoliStudio = elencoTitoliStudio.iterator();
   while(iteraTitoliStudio.hasNext()) {
     htmpl.newBlock("elencoTitoliStudio");
     CodeDescription code = (CodeDescription)iteraTitoliStudio.next();
     if(personaVO.getIdTitoloStudio() != null) {
       if(personaVO.getIdTitoloStudio().compareTo(Long.decode(code.getCode().toString())) == 0) {
         htmpl.set("elencoTitoliStudio.check","selected");
       }
     }
     htmpl.set("elencoTitoliStudio.idCodice", code.getCode().toString());
     htmpl.set("elencoTitoliStudio.descrizione", code.getDescription());
   }
   if(personaVO.getIdTitoloStudio() != null) {
     Vector elencoIndirizziStudio = null;
     try {
       elencoIndirizziStudio = anagFacadeClient.getIndirizzoStudioByTitolo(personaVO.getIdTitoloStudio());
     }
     catch(SolmrException se) {
     }
     if(elencoIndirizziStudio != null) {
       Iterator iteraIndirizziStudio = elencoIndirizziStudio.iterator();
       while(iteraIndirizziStudio.hasNext()) {
         htmpl.newBlock("elencoIndirizziStudio");
         CodeDescription code = (CodeDescription)iteraIndirizziStudio.next();
         if(personaVO.getIdIndirizzoStudio() != null) {
           if(personaVO.getIdIndirizzoStudio().compareTo(Long.decode(code.getCode().toString())) == 0) {
             htmpl.set("elencoIndirizziStudio.check","selected");
           }
         }
         htmpl.set("elencoIndirizziStudio.idCodice",code.getCode().toString());
         htmpl.set("elencoIndirizziStudio.descrizione",code.getDescription());
       }
     }
   }
 }
 Date dataInizioIncarico = (Date)session.getAttribute("dataInizioCarico");
 if(Validator.isNotEmpty(dataInizioIncarico)) {
   htmpl.set("dataInizioCarico", DateUtils.formatDate(dataInizioIncarico));
 }

 out.print(htmpl.text());
%>
