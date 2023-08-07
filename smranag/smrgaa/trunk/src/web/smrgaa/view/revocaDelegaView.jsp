<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	SolmrLogger.debug(this, " - revocaDelegaView.jsp - INIZIO PAGINA");
  java.io.InputStream layout = application.getResourceAsStream("/layout/revocaDelega.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
  	<%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
    
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  PersonaFisicaVO personaVO = (PersonaFisicaVO)session.getAttribute("personaVO");
  String dataFineMandato = (String)request.getAttribute("dataFineMandato");

  htmpl.set("CUAA",anagAziendaVO.getCUAA() );
  htmpl.set("partitaIVA",anagAziendaVO.getPartitaIVA() );
  htmpl.set("denominazione",anagAziendaVO.getDenominazione() );

  if(anagAziendaVO.getDelegaVO() != null) 
  {
   	htmpl.set("denominazioneIntermediario",anagAziendaVO.getDelegaVO().getDenomIntermediario());
  }

  htmpl.set("cognome", personaVO.getCognome());
  htmpl.set("nome", personaVO.getNome());
  htmpl.set("codFiscale", personaVO.getCodiceFiscale());
  htmpl.set("dataNascita", personaVO.getNascitaData()==null?"":DateUtils.formatDate(personaVO.getNascitaData()));

  if(personaVO.getLuogoNascita() != null 
    && !"".equals(personaVO.getLuogoNascita())) 
  {
    htmpl.set("luogoNascita", personaVO.getLuogoNascita());
  }
  else 
  {
   	if(personaVO.getNascitaCittaEstero()!= null && !personaVO.getNascitaCittaEstero().equals("")) 
    {
    	htmpl.set("luogoNascita", personaVO.getNascitaCittaEstero());
    }
    else 
    {
    	htmpl.set("luogoNascita", "");
    }
  }
    
  //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
  //al ruolo!
  String dateUlt = "";
  if(anagAziendaVO.getDataUltimaModifica() !=null)
  {
    dateUlt = DateUtils.formatDate(anagAziendaVO.getDataUltimaModifica());
  }
  ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl, 
    "ultimaModificaVw", dateUlt, anagAziendaVO.getUtenteUltimaModifica(),
    anagAziendaVO.getEnteUltimaModifica(), null);
    
    
    
  String labelRevoca = "Data revoca *";
  if(Validator.isNotEmpty(request.getAttribute("intermediarioConDelega")))
  {
    labelRevoca = "Revoca valida dal *";
  }
  
  htmpl.set("labelRevoca", labelRevoca);
    
	htmpl.set("dataFineMandato", dataFineMandato);
  
  htmpl.set("dataRicRitorno", request.getParameter("dataRicRitorno"));
  
  
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  HtmplUtil.setErrors(htmpl,errors,request, application);

  out.print(htmpl.text());

  SolmrLogger.debug(this, " - revocaDelegaView.jsp - FINE PAGINA");
%>
