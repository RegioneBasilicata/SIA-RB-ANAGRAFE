<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>

<%

 java.io.InputStream layout = application.getResourceAsStream("/layout/fascicoli.htm");
 Htmpl htmpl = new Htmpl(layout);

 %>
    <%@include file = "/view/remoteInclude.inc" %>
 <%

 // Nuova gestione fogli di stile
 htmpl.set("head", head, null);
 htmpl.set("header", header, null);
 htmpl.set("footer", footer, null);

 Vector elencoCAA = (Vector)request.getAttribute("elencoCAA");
 Long idIntermediario = (Long)request.getAttribute("idIntermediario");
 String ricarica = (String)request.getAttribute("ricarica");
 String statoFascicolo = (String)request.getAttribute("statoFascicolo");
 Vector elencoAziende = (Vector)session.getAttribute("elencoAziende");

 if(Validator.isNotEmpty(ricarica)) {
   htmpl.set("ricarica", ricarica);
 }
 ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

 if(elencoCAA != null && elencoCAA.size() > 0) {
   Iterator iteraCAA = elencoCAA.iterator();
   while(iteraCAA.hasNext()) {
     htmpl.newBlock("blkElencoCAA");
     CodeDescription code = (CodeDescription)iteraCAA.next();
     htmpl.set("blkElencoCAA.codice", code.getCode().toString());
     htmpl.set("blkElencoCAA.descrizione", code.getDescription());
     if(elencoCAA.size() == 1) {
       htmpl.set("blkElencoCAA.selected", "selected=\"selected\"");
     }
     else if(Validator.isNotEmpty(idIntermediario) && idIntermediario.compareTo(Long.decode(code.getCode().toString())) == 0) {
       htmpl.set("blkElencoCAA.selected", "selected=\"selected\"");
     }
   }
 }

 // SEZIONE RELATIVA ALLO STATO DEL FASCICOLO
 if(Validator.isNotEmpty(statoFascicolo)) {
   if(statoFascicolo.equalsIgnoreCase("tutti")) {
     htmpl.set("checkedTutti", "checked=\"checked\"");
   }
   else if(statoFascicolo.equalsIgnoreCase("incompleto")) {
     htmpl.set("checkedIncompleto", "checked=\"checked\"");
   }
   else if(statoFascicolo.equalsIgnoreCase("daValidare")) {
     htmpl.set("checkedDaValidare", "checked=\"checked\"");
   }
   else {
     htmpl.set("checkedValidatiDalAl", "checked=\"checked\"");
   }
 }
 else {
   htmpl.set("checkedTutti", "checked=\"checked\"");
 }

 HtmplUtil.setValues(htmpl, request);

 // SEZIONE RELATIVA ALL'ELENCO DELLE AZIENDE
 if(elencoAziende != null && elencoAziende.size() > 0) {
   htmpl.newBlock("blkAreaResults");
   htmpl.set("blkAreaResults.numeroTotAziende", String.valueOf(elencoAziende.size()));

   // Paginazione 1
   String indice = (String)session.getAttribute("indice");
   int i = 0;
   if(indice != null) {
     i = Integer.parseInt(indice);
     if(i <= 0) {
       i=0;
     }
     else if(i >= elencoAziende.size()) {
       i = (elencoAziende.size()-1)-(elencoAziende.size()-1)%25;
     }
   }
   int j = i+24;


   // Dati
   Iterator iteraAziende = elencoAziende.iterator();
   if(iteraAziende.hasNext()) {
     for(; i < elencoAziende.size() && i <= j; i++) {
       AnagAziendaVO anagAziendaVO = (AnagAziendaVO)elencoAziende.elementAt(i);
       htmpl.newBlock("blkAreaResults.blkListAziende");
       htmpl.set("blkAreaResults.blkListAziende.idAzienda", anagAziendaVO.getIdAzienda().toString());
       htmpl.set("blkAreaResults.blkListAziende.denominazione", anagAziendaVO.getDenominazione());
       htmpl.set("blkAreaResults.blkListAziende.CUAA", anagAziendaVO.getCUAA());
       htmpl.set("blkAreaResults.blkListAziende.partitaIVA", anagAziendaVO.getPartitaIVA());
       htmpl.set("blkAreaResults.blkListAziende.descSedelegComune", anagAziendaVO.getDescComune());
       htmpl.set("blkAreaResults.blkListAziende.sedelegProvincia", anagAziendaVO.getSedelegProv());
       htmpl.set("blkAreaResults.blkListAziende.sedelegIndirizzo", anagAziendaVO.getSedelegIndirizzo());
     }
   }

   // Paginazione 2
   int barretta = 0;
   if(i > 25) {
     htmpl.newBlock("blkAreaResults.frecciaSinistra");
     htmpl.set("blkAreaResults.frecciaSinistra.valore",""+(i-50+(25-i%25)%25));
     barretta++;
   }

   int valoreTotale = 1;
   int numParziale = 1;

   valoreTotale = (int)Math.ceil(elencoAziende.size()/25.0);
   htmpl.set("blkAreaResults.numeroTotale", String.valueOf(valoreTotale));

   String numeroParziale = (String)request.getAttribute("numeroParziale");
   if(!Validator.isNotEmpty(numeroParziale)) {
     numParziale = ((i-1)/25)+1;
     htmpl.set("blkAreaResults.numeroParziale",String.valueOf(numParziale));
   }
   else {
     htmpl.set("blkAreaResults.numeroParziale",numeroParziale);
   }
   if(elencoAziende.size() > 25 && i <elencoAziende.size()) {
     htmpl.newBlock("blkAreaResults.frecciaDestra");
     htmpl.set("blkAreaResults.frecciaDestra.valore",""+i);
     htmpl.set("blkAreaResults.frecciaDestra.valoreUltimaPagina",""+25*valoreTotale);
     barretta++;
   }
   if(barretta == 2) {
     htmpl.set("blkAreaResults.barretta", "|");
   }

   String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                         "alt=\"{2}\" border=\"0\"></a>";
   String imko = "ko.gif";
   StringProcessor jssp = new JavaScriptStringProcessor();
   Vector errori = (Vector)request.getAttribute("errori");
   if(errori != null && errori.size() > 0) {
     String erroreNumeroPagina = (String)errori.elementAt(0);
     if(Validator.isNotEmpty(erroreNumeroPagina)) {
       htmpl.set("blkAreaResults.err_numeroParziale",
                 MessageFormat.format(htmlStringKO,
                 new Object[] {
                 pathErrori + "/"+ imko,
                 "'"+jssp.process(erroreNumeroPagina)+"'",
                 erroreNumeroPagina}),
                 null);
     }
   }
 }

 // SEZIONE RELATIVA AGLI ERRORI
 String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 if(Validator.isNotEmpty(messaggioErrore)) {
   htmpl.newBlock("blkErrore");
   htmpl.set("blkErrore.messaggio", messaggioErrore);
 }

 if(errors != null && errors.size() > 0) {
   HtmplUtil.setErrors(htmpl, errors, request, application);
 }


%>
<%= htmpl.text()%>
