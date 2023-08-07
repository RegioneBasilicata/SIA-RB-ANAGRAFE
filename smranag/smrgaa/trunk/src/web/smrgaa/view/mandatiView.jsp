<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>



<%

 java.io.InputStream layout = application.getResourceAsStream("/layout/mandati.htm");
 Htmpl htmpl = new Htmpl(layout);

 %>
    <%@include file = "/view/remoteInclude.inc" %>
 <%

 RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
 Vector elencoMandati = (Vector)request.getAttribute("elencoMandati");
 UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni)session.getAttribute("utenteAbilitazioni");

 // Nuova gestione fogli di stile
 htmpl.set("head", head, null);
 htmpl.set("header", header, null);
 htmpl.set("footer", footer, null);

 ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
 Boolean forZona = (Boolean)request.getAttribute("forZona");
 String dal = request.getParameter("dal");
 String al = request.getParameter("al");


 if(Validator.isNotEmpty(request.getAttribute("isDefault"))){
	 dal =  "01/01/" + DateUtils.getCurrentYear();
	 al = DateUtils.getCurrentDateString();
 }
 else{
	 if(!Validator.isNotEmpty(dal))
			 dal =  DateUtils.getCurrentDateString();

	 if(!Validator.isNotEmpty(al))
			 al = DateUtils.getCurrentDateString();
 }
 // PROFILO INTERMEDIARIO: profilazione del check suddivisione
 // per ufficio di zona...
 if(utenteAbilitazioni.getRuolo().isUtenteIntermediario()) 
 {
   if(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase((String)SolmrConstants.get("LIVELLO_INTERMEDIARIO_REGIONALE"))) 
   {
     htmpl.newBlock("blkIntermediario");
     if(Validator.isNotEmpty(forZona)) 
     {
       if(forZona.booleanValue()) 
       {
         htmpl.set("blkIntermediario.checkedZona", "checked=\"checked\"", null);
       }
     }
   }
   else
  	 htmpl.newBlock("blkAggUtenteIntermediarioOther");
 }
 else {
   htmpl.newBlock("blkUtentePA");
   if(Validator.isNotEmpty(forZona)) {
     if(forZona.booleanValue()) {
       htmpl.set("blkUtentePA.checkedZona", "checked=\"checked\"");
     }
   }
 }
 htmpl.set("dal",dal);
 htmpl.set("al",al);


 DelegaVO delegaVO = null;
 if(elencoMandati != null && elencoMandati.size() > 0) {
   htmpl.newBlock("blkMandati");
   Iterator iteraMandati = elencoMandati.iterator();
   int i = 0;
   int totNumMandati = 0;
   int totNumMandatiValidate = 0;
   String colspan = null;
   while(iteraMandati.hasNext()) {
		delegaVO = (DelegaVO)iteraMandati.next();

     // CASI INTERMEDIARIO
     if(utenteAbilitazioni.getRuolo().isUtenteIntermediario()) 
     {
       // CASO INTERMEDIARIO CON LIVELLO "Z" CIOE' UFFICI SPECIFICI DI ZONA
       if(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase((String)SolmrConstants.get("LIVELLO_INTERMEDIARIO_UFFICIO_ZONA"))) {
    	   colspan = "5";
         if(i == 0) {
           htmpl.newBlock("blkMandati.blkIntermediarioLivelloZ");
         }
         htmpl.newBlock("blkMandati.blkElencoMandatiLivelloZ");
         htmpl.set("blkMandati.blkElencoMandatiLivelloZ.denominazione", utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getDenominazioneEnte());
         htmpl.set("blkMandati.blkElencoMandatiLivelloZ.indirizzo", delegaVO.getIndirizzo());
         htmpl.set("blkMandati.blkElencoMandatiLivelloZ.comune", delegaVO.getDescComune());
         htmpl.set("blkMandati.blkElencoMandatiLivelloZ.provincia", delegaVO.getSiglaProvincia());
         htmpl.set("blkMandati.blkElencoMandatiLivelloZ.codiceAgea", delegaVO.getCodiceFiscaleIntermediario());
         htmpl.set("blkMandati.blkElencoMandatiLivelloZ.numeroMandati", delegaVO.getTotaleMandati().toString());
         htmpl.set("blkMandati.blkElencoMandatiLivelloZ.numMandatiV", delegaVO.getTotaleMandatiValidati().toString());
       }
       // CASO INTERMEDIARIO CON LIVELLO "P" CIOE' PROVINCIALE
       else if(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase((String)SolmrConstants.get("LIVELLO_INTERMEDIARIO_PROVINCIALE"))) {
    	   colspan = "5";
         if(i == 0) {
           htmpl.newBlock("blkMandati.blkIntermediarioLivelloP");
         }
         htmpl.newBlock("blkMandati.blkElencoMandatiLivelloP");
         htmpl.set("blkMandati.blkElencoMandatiLivelloP.denominazione", delegaVO.getDenomIntermediario());
         htmpl.set("blkMandati.blkElencoMandatiLivelloP.indirizzo", delegaVO.getIndirizzo());
         htmpl.set("blkMandati.blkElencoMandatiLivelloP.comune", delegaVO.getDescComune());
         htmpl.set("blkMandati.blkElencoMandatiLivelloP.provincia", delegaVO.getSiglaProvincia());
         htmpl.set("blkMandati.blkElencoMandatiLivelloP.codiceAgea", delegaVO.getCodiceFiscaleIntermediario());
         htmpl.set("blkMandati.blkElencoMandatiLivelloP.numeroMandati", delegaVO.getTotaleMandati().toString());
         htmpl.set("blkMandati.blkElencoMandatiLivelloP.numMandatiV", delegaVO.getTotaleMandatiValidati().toString());
       }
       // CASO INTERMEDIARIO CON LIVELLO "R" CIOE' REGIONALE
       else if(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase((String)SolmrConstants.get("LIVELLO_INTERMEDIARIO_REGIONALE"))) {
    	   // CASO NON PER UFFICIO DI ZONA
         if(forZona == null || !forZona.booleanValue()) {
           colspan = "5";
           if(i == 0) {
             htmpl.newBlock("blkMandati.blkIntermediarioLivelloRNotForArea");
           }
           htmpl.newBlock("blkMandati.blkElencoMandatiLivelloRNotForArea");
           htmpl.set("blkMandati.blkElencoMandatiLivelloRNotForArea.denominazione", delegaVO.getDenomIntermediario());
           htmpl.set("blkMandati.blkElencoMandatiLivelloRNotForArea.indirizzo", delegaVO.getIndirizzo());
           htmpl.set("blkMandati.blkElencoMandatiLivelloRNotForArea.comune", delegaVO.getDescComune());
           htmpl.set("blkMandati.blkElencoMandatiLivelloRNotForArea.provincia", delegaVO.getSiglaProvincia());
           htmpl.set("blkMandati.blkElencoMandatiLivelloRNotForArea.codiceAgea", delegaVO.getCodiceFiscaleIntermediario());
           htmpl.set("blkMandati.blkElencoMandatiLivelloRNotForArea.numeroMandati", delegaVO.getTotaleMandati().toString());
           htmpl.set("blkMandati.blkElencoMandatiLivelloRNotForArea.numMandatiV", delegaVO.getTotaleMandatiValidati().toString());
         }
         // CASO UFFICI DI ZONA SPECIFICATI
         else {
           colspan = "5";
           if(i == 0) {
             htmpl.newBlock("blkMandati.blkIntermediarioLivelloRForArea");
           }
           htmpl.newBlock("blkMandati.blkElencoMandatiLivelloRForArea");
           htmpl.set("blkMandati.blkElencoMandatiLivelloRForArea.denominazione", delegaVO.getDenomIntermediario());
           htmpl.set("blkMandati.blkElencoMandatiLivelloRForArea.indirizzo", delegaVO.getIndirizzo());
           htmpl.set("blkMandati.blkElencoMandatiLivelloRForArea.comune", delegaVO.getDescComune());
           htmpl.set("blkMandati.blkElencoMandatiLivelloRForArea.provincia", delegaVO.getSiglaProvincia());
           htmpl.set("blkMandati.blkElencoMandatiLivelloRForArea.codiceAgea", delegaVO.getCodiceFiscaleIntermediario());
           htmpl.set("blkMandati.blkElencoMandatiLivelloRForArea.numeroMandati", delegaVO.getTotaleMandati().toString());
           htmpl.set("blkMandati.blkElencoMandatiLivelloRForArea.numMandatiV", delegaVO.getTotaleMandatiValidati().toString());
         }
       }
     }
     // CASO DI UTENTE APPARTENENTE ALLA PA(PROVINCIALI E REGIONALI)
     else {
       // CASO NON PER PROVINCIA
       if(forZona == null || !forZona.booleanValue()) {
         colspan = "2";
         if(i == 0) {
           htmpl.newBlock("blkMandati.blkPANotForArea");
         }
         htmpl.newBlock("blkMandati.blkElencoMandatiPANotForArea");
         htmpl.set("blkMandati.blkElencoMandatiPANotForArea.denominazione", delegaVO.getDenomIntermediario());
         htmpl.set("blkMandati.blkElencoMandatiPANotForArea.codiceAgea", delegaVO.getCodiceFiscaleIntermediario());
         htmpl.set("blkMandati.blkElencoMandatiPANotForArea.numeroMandati", delegaVO.getTotaleMandati().toString());
         htmpl.set("blkMandati.blkElencoMandatiPANotForArea.numMandatiV", delegaVO.getTotaleMandatiValidati().toString());

       }
       // CASO SUDDIVISI PER PROVINCIA
       else {
         colspan = "3";
         if(i == 0) {
           htmpl.newBlock("blkMandati.blkPAForArea");
         }
         htmpl.newBlock("blkMandati.blkElencoMandatiPAForArea");
         htmpl.set("blkMandati.blkElencoMandatiPAForArea.denominazione", delegaVO.getDenomIntermediario());
         htmpl.set("blkMandati.blkElencoMandatiPAForArea.provincia", delegaVO.getSiglaProvincia());
         htmpl.set("blkMandati.blkElencoMandatiPAForArea.codiceAgea", delegaVO.getCodiceFiscaleIntermediario());
         htmpl.set("blkMandati.blkElencoMandatiPAForArea.numeroMandati", delegaVO.getTotaleMandati().toString());
         htmpl.set("blkMandati.blkElencoMandatiPAForArea.numMandatiV", delegaVO.getTotaleMandatiValidati().toString());
       }
     }
     i++;
     totNumMandati += delegaVO.getTotaleMandati().intValue();
     totNumMandatiValidate += delegaVO.getTotaleMandatiValidati().intValue();
   }
   htmpl.set("blkMandati.valoreColspan", colspan);
   htmpl.set("blkMandati.totaleMandati", String.valueOf(totNumMandati));
   htmpl.set("blkMandati.totaleMandatiV", String.valueOf(totNumMandatiValidate));
 }
 else {
   String messaggioErrore = (String)request.getAttribute("messaggioErrore");
   htmpl.newBlock("blkErrore");
   htmpl.set("blkErrore.messaggio", messaggioErrore);

   if(errors != null && errors.size() > 0) {
	   HtmplUtil.setErrors(htmpl, errors, request, application);
	 }
 }

%>
<%= htmpl.text()%>
