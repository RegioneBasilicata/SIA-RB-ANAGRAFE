<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.smranag.smrgaa.ws.sianfa.SianEsitoAggFascicolo"%>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>

<%

 java.io.InputStream layout = application.getResourceAsStream("/layout/sincronizzaFascicolo.htm");
 Htmpl htmpl = new Htmpl(layout);

 %>
    <%@include file = "/view/remoteInclude.inc" %>
 <%
 
 SolmrLogger.debug(this, "BEGIN sincronizzaFascicoloView");
 
 AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
 
 AnagFacadeClient client= new AnagFacadeClient();

 // Nuova gestione fogli di stile
 htmpl.set("head", head, null);
 htmpl.set("header", header, null);
 htmpl.set("footer", footer, null);
 
 String sourceImage = null;
 if(pathToFollow.equalsIgnoreCase("rupar")) {
   sourceImage = application.getInitParameter("erroriRupar");
 }
 else if (pathToFollow.equalsIgnoreCase("sispie")) {
   sourceImage = application.getInitParameter("erroriSispie");
 }
 else if (pathToFollow.equalsIgnoreCase("TOBECONFIG")) {
   sourceImage = application.getInitParameter("erroriTOBECONFIG");
 }

  //Storico aggiornamenti del fascicolo
  SolmrLogger.debug(this, "-- Recupero i dati dello storico aggiornamento del fascicolo");
  List<SianEsitoAggFascicolo> esitoAggFascicolo = (List<SianEsitoAggFascicolo>)request.getAttribute("esitoAggFascicolo");
    
  SolmrLogger.debug(this, "-- cuaa ="+anagAziendaVO.getCUAA());
  htmpl.set("cuaa", anagAziendaVO.getCUAA());
 
  if(esitoAggFascicolo != null && esitoAggFascicolo.size() != 0){
	SolmrLogger.debug(this, "-- Sono stati trovati aggiornamenti del fascicolo");  
    SianEsitoAggFascicolo aggiornamentoFasc = null;
	for(int i=0;i<esitoAggFascicolo.size();i++){	  
      htmpl.newBlock("blkRiga");
      aggiornamentoFasc = esitoAggFascicolo.get(i);
     
      if(aggiornamentoFasc != null){
    	// setto la variabile globale js con l'ultimo stato di aggiornamento (primo elemento della lista in quanto sono ordinati)
    	if(i == 0){
    	  SolmrLogger.debug(this, "-- id ultima fase di aggiornamento ="+aggiornamentoFasc.getIdFaseAggiornamento());
    	  htmpl.set("ultimoStatoAggiornamento",""+aggiornamentoFasc.getIdFaseAggiornamento());
    	}
    	  
	      htmpl.set("blkRiga.cuaa", aggiornamentoFasc.getCuaa());
	      
	      if(aggiornamentoFasc.getIdUtente() != null){
	    	SolmrLogger.debug(this, "-- idUtente che ha richiesto la sincronizzazione ="+aggiornamentoFasc.getIdUtente());
	    	UtenteAbilitazioni utenteAbilitazioni = client.getUtenteAbilitazioniByIdUtenteLogin(aggiornamentoFasc.getIdUtente());
	    	SolmrLogger.debug(this, "-- nome cognome utente = "+utenteAbilitazioni.getNome()+" "+utenteAbilitazioni.getCognome());
	        htmpl.set("blkRiga.utente",utenteAbilitazioni.getNome()+" "+utenteAbilitazioni.getCognome());      
	      }  		
	      if(aggiornamentoFasc.getDataInizio() != null)
	        htmpl.set("blkRiga.dataInizioSincronizz", DateUtils.convertXmlGregorianToString(aggiornamentoFasc.getDataInizio()));              
	      if(aggiornamentoFasc.getEsitoAggiornamento() != null)
	      	htmpl.set("blkRiga.faseAggiornamento", aggiornamentoFasc.getEsitoAggiornamento());
	      if(aggiornamentoFasc.getDataFine() != null)
	    	htmpl.set("blkRiga.dataFineSincronizz", DateUtils.convertXmlGregorianToString(aggiornamentoFasc.getDataFine()));
	      htmpl.set("blkRiga.segnalazioneSian", aggiornamentoFasc.getSegnalazioneOutputServizio());
	      if(aggiornamentoFasc.getDescrErrorePl() != null)
	        htmpl.set("blkRiga.erroreAggiornamento", aggiornamentoFasc.getDescrErrorePl()); 
      }
    }
  }
  else{
    SolmrLogger.debug(this, "-- Non sono stati trovati aggiornamenti del fascicolo");
    htmpl.newBlock("blkErr");
  }
  
  
 
 /*if ("si".equals(request.getParameter("storico"))) {
   htmpl.set("checkedStorico", "checked");
 }

 Vector v_soggetti = (Vector)session.getAttribute("v_soggetti");
 ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
 htmpl.set("denominazione", anagAziendaVO.getDenominazione());
  if(anagAziendaVO.getCUAA()!=null && !anagAziendaVO.getCUAA().equals("")) {
    htmpl.set("CUAA", anagAziendaVO.getCUAA()+" - ");
  }
  htmpl.set("dataSituazioneAlStr", anagAziendaVO.getDataSituazioneAlStr());

 if(v_soggetti!=null&&v_soggetti.size()!=0) {
   htmpl.newBlock("blkNoErr");
   Iterator iter = v_soggetti.iterator();
   while(iter.hasNext()) {
	  htmpl.newBlock("blkRiga");
	  personaFisicaVO = (PersonaFisicaVO)iter.next();
	     if(Validator.isNotEmpty(personaFisicaVO.getIdContitolare())) {
	       htmpl.set("blkNoErr.blkRiga.idContitolare", personaFisicaVO.getIdContitolare().toString());
	     }
	     htmpl.set("blkNoErr.blkRiga.ruolo", personaFisicaVO.getRuolo());
	     htmpl.set("blkNoErr.blkRiga.cognome", personaFisicaVO.getCognome());
	     htmpl.set("blkNoErr.blkRiga.nome", personaFisicaVO.getNome());
	     htmpl.set("blkNoErr.blkRiga.codiceFiscale", personaFisicaVO.getCodiceFiscale());
	     htmpl.set("blkNoErr.blkRiga.dataInizioRuolo", DateUtils.formatDate(personaFisicaVO.getDataInizioRuolo()));
	     
       if(personaFisicaVO.getDataFineRuolo() != null)
	       htmpl.set("blkNoErr.blkRiga.dataFineRuolo", DateUtils.formatDate(personaFisicaVO.getDataFineRuolo()));
	     else htmpl.set("blkNoErr.blkRiga.dataFineRuolo", "");
       
       if(personaFisicaVO.getDataInizioRuoloMod() != null)
         htmpl.set("blkNoErr.blkRiga.dataInizioRuoloMod", DateUtils.formatDate(personaFisicaVO.getDataInizioRuoloMod()));
       else htmpl.set("blkNoErr.blkRiga.dataInizioRuoloMod", "");
         
       if(personaFisicaVO.getDataFineRuoloMod() != null)
         htmpl.set("blkNoErr.blkRiga.dataFineRuoloMod", DateUtils.formatDate(personaFisicaVO.getDataFineRuoloMod()));
       else htmpl.set("blkNoErr.blkRiga.dataFineRuoloMod", "");
       
       
	     if(personaFisicaVO.getdesNumeroCellulare() != null){
	     	htmpl.newBlock("blkCellulare"); 		
	   		htmpl.set("blkNoErr.blkRiga.blkCellulare.descimgCellulare", 
	   			StringUtils.replace((String)SolmrConstants.DESC_IMG_CELLULARE,"@@c@@n",
	   			personaFisicaVO.getCognome() + " " + personaFisicaVO.getNome())); 
	   	}
   }
 }*/
 
 
 
 HtmplUtil.setErrors(htmpl, errors, request, application);
 
 
 SolmrLogger.debug(this, "END sincronizzaFascicoloView");

%>
<%= htmpl.text()%>
