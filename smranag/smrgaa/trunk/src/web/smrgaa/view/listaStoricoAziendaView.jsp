  <%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
  %>
<%@ page import="it.csi.solmr.business.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<%

  java.io.InputStream layout = application.getResourceAsStream("/layout/listaStoricoAzienda.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

  htmpl.set("nomeCognomeUtente", ruoloUtenza.getDenominazione());
  htmpl.set("ente", ruoloUtenza.getDescrizioneEnte());

  HtmplUtil.setValues(htmpl,anagAziendaVO);

  // Informazioni relative all'eventuale azienda di provenienza
  AnagAziendaVO anagAziendaProvenienzaVO = (AnagAziendaVO)request.getAttribute("anagAziendaProvenienzaVO");
  if(Validator.isNotEmpty(anagAziendaProvenienzaVO)) {
    htmpl.newBlock("blkProvenienzaAzienda");
    if(Validator.isNotEmpty(anagAziendaProvenienzaVO.getCUAA())) {
      htmpl.set("blkProvenienzaAzienda.cuaa", anagAziendaProvenienzaVO.getCUAA());
    }
    htmpl.set("blkProvenienzaAzienda.denominazione", anagAziendaProvenienzaVO.getDenominazione());
    if(Validator.isNotEmpty(anagAziendaProvenienzaVO.getSedelegEstero())) {
      htmpl.set("blkProvenienzaAzienda.sedeLegale", anagAziendaProvenienzaVO.getSedelegIndirizzo() + " - " +
                                                    anagAziendaProvenienzaVO.getSedelegEstero() + " " +
                                                    anagAziendaProvenienzaVO.getSedelegCittaEstero());
    }
    else {
      htmpl.set("blkProvenienzaAzienda.sedeLegale", anagAziendaProvenienzaVO.getSedelegIndirizzo() + " - " +
                                                    anagAziendaProvenienzaVO.getDescComune() + " (" +
                                                    anagAziendaProvenienzaVO.getSedelegProv() + ") ");
    }
  }


  Vector listaAziende = (Vector)session.getAttribute("listaStoricoAziende");
  if(listaAziende != null) {
    if(listaAziende.size() > 0) {
      Iterator iteraAziende = listaAziende.iterator();
      htmpl.newBlock("elencoAziende");
      while(iteraAziende.hasNext()) {
        AnagAziendaVO anagStoricoAziendaVO = (AnagAziendaVO)iteraAziende.next();
        htmpl.set("elencoAziende.idAnagAzienda",anagStoricoAziendaVO.getIdAnagAzienda().toString());
        htmpl.set("elencoAziende.storicoCuaa",anagStoricoAziendaVO.getCUAA());
        htmpl.set("elencoAziende.dataInizioValidita",DateUtils.formatDate(anagStoricoAziendaVO.getDataInizioVal()));
        if(anagStoricoAziendaVO.getDataFineVal() != null && !anagStoricoAziendaVO.getDataFineVal().equals("")) {
          htmpl.set("elencoAziende.dataFineValidita",DateUtils.formatDate(anagStoricoAziendaVO.getDataFineVal()));
        }
        
        if(anagStoricoAziendaVO.getMotivoModifica() != null) {
          htmpl.set("elencoAziende.motivoModifica",anagStoricoAziendaVO.getMotivoModifica());
        }
        
        //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
        //al ruolo!
        ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
          "elencoAziende.ultimaModificaVw", null,anagStoricoAziendaVO.getDescrizioneUtenteModifica(), 
          anagStoricoAziendaVO.getDescrizioneEnteUtenteModifica(), null);
          
          
        //htmpl.set("elencoAziende.denominazioneUtente",anagStoricoAziendaVO.getDescrizioneUtenteModifica());
        //if(anagStoricoAziendaVO.getDescrizioneEnteUtenteModifica() != null &&
           //!anagStoricoAziendaVO.getDescrizioneEnteUtenteModifica().equals("")) {
          //htmpl.set("elencoAziende.denominazioneEnteUtente"," - "+anagStoricoAziendaVO.getDescrizioneEnteUtenteModifica());
        //}
      }
    }
  }

%>
<%= htmpl.text()%>
