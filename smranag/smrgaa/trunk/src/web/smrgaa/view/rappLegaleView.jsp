<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>

<%

 java.io.InputStream layout = application.getResourceAsStream("/layout/rappres_leg.htm");
 Htmpl htmpl = new Htmpl(layout);

 %>
    <%@include file = "/view/remoteInclude.inc" %>
 <%

 // Nuova gestione fogli di stile
 htmpl.set("head", head, null);
 htmpl.set("header", header, null);
 htmpl.set("footer", footer, null);

 PersonaFisicaVO personaVO = null;
 if(session.getAttribute("personaVO")!=null)
   personaVO = (PersonaFisicaVO)session.getAttribute("personaVO");
 
 AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

 htmpl.set("CUAA", anagVO.getCUAA());
 htmpl.set("denominazione", anagVO.getDenominazione());
 htmpl.set("dataSituazioneAlStr", anagVO.getDataSituazioneAlStr());

 //if(errors == null){
 if(personaVO != null){
   htmpl.set("cognome", personaVO.getCognome());
   htmpl.set("nome", personaVO.getNome());
   htmpl.set("codFiscale", personaVO.getCodiceFiscale());
   htmpl.set("dataNascita", personaVO.getNascitaData()==null?"":DateUtils.formatDate(personaVO.getNascitaData()));
   htmpl.set("sesso", personaVO.getSesso());
   htmpl.set("luogoNascita", personaVO.getLuogoNascita());
   if(personaVO.getNascitaCittaEstero()!= null && !personaVO.getNascitaCittaEstero().equals("")){
     htmpl.newBlock("blkNascitaEstero");
     htmpl.set("blkNascitaEstero.cittaEstero", personaVO.getNascitaCittaEstero());
   }
   htmpl.set("istat", personaVO.getNascitaComune());
   htmpl.set("indirizzo", personaVO.getResIndirizzo());
   if(personaVO.getStatoEsteroRes()==null||personaVO.getStatoEsteroRes().equals("")){
     htmpl.newBlock("blkStatoItalia");
     htmpl.set("blkStatoItalia.prov", personaVO.getDescResProvincia());
     htmpl.set("blkStatoItalia.comune", personaVO.getDescResComune());
     htmpl.set("blkStatoItalia.cap", personaVO.getResCAP());
   }
   else{
     htmpl.newBlock("blkStatoEstero");
     htmpl.set("blkStatoEstero.stato", personaVO.getStatoEsteroRes());
     htmpl.set("blkStatoEstero.citta", personaVO.getResCittaEstero());
   }
   htmpl.set("telefono", personaVO.getResTelefono());
   htmpl.set("fax", personaVO.getResFax());
   htmpl.set("numeroCellulare", personaVO.getNumeroCellulare());
   htmpl.set("mail", personaVO.getResMail());
   if((SolmrConstants.FLAG_S).equalsIgnoreCase(personaVO.getDomicilioFlagEstero())) {
     htmpl.newBlock("blkDomicilioEstero");
     htmpl.set("blkDomicilioEstero.domicilioStatoEstero", personaVO.getDomicilioStatoEstero());
     htmpl.set("blkDomicilioEstero.descCittaEsteroDomicilio", personaVO.getDescCittaEsteroDomicilio());
   }
   else {
     htmpl.newBlock("blkDomicilioItalia");
     htmpl.set("blkDomicilioItalia.domComune", personaVO.getDomComune());
     htmpl.set("blkDomicilioItalia.domCAP", personaVO.getDomCAP());
     htmpl.set("blkDomicilioItalia.domProvincia", personaVO.getDomProvincia());
   }
   htmpl.set("domIndirizzo", personaVO.getDomIndirizzo());
   // Inserisco le informazioni relative al titolo di studio
   String titoloIndirizzoStudio = null;
   if(personaVO.getIdTitoloStudio() != null && personaVO.getIdTitoloStudio().compareTo(new Long(0)) != 0) {
     titoloIndirizzoStudio = personaVO.getDescrizioneTitoloStudio();
   }
   if(personaVO.getIdIndirizzoStudio() != null && personaVO.getIdIndirizzoStudio().compareTo(new Long(0)) != 0){
     titoloIndirizzoStudio += " - "+personaVO.getDescrizioneIndirizzoStudio();
   }
   if(titoloIndirizzoStudio != null) {
     htmpl.set("titoloIndirizzoStudio",titoloIndirizzoStudio);
   }

   htmpl.set("note", personaVO.getNote());
   if(personaVO.getDataInizioRuolo()!= null)
     htmpl.set("dataInizio", DateUtils.formatDate(personaVO.getDataInizioRuolo()));
   else
     htmpl.set("dataInizio", "");
   if(personaVO.getDataFineRuolo()!= null)
     htmpl.set("dataFine", DateUtils.formatDate(personaVO.getDataFineRuolo()));
   else
     htmpl.set("dataFine", "");
 }
 else{
   HtmplUtil.setValues(htmpl, request);

 }
 if(errors!=null)
   HtmplUtil.setErrors(htmpl, errors, request, application);

%>

<%= htmpl.text()%>
