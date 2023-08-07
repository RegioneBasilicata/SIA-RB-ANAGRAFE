<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="java.text.MessageFormat"%>

<%


  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/aziendeCollegateInserisci.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%
  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  htmpl.set("headMenuScroll", headMenuScroll, null);
  htmpl.set("headMenuScrollIE6", headMenuScrollIE6, null);
 
  Vector vAziendaIns = (Vector)session.getAttribute("vAziendaIns");
  AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  //tutte le aziende per associare correttamente le date
  String[] elencoDataIngressoAzzCollAll = (String[])session.getAttribute("elencoDataIngressoAzCollAll");
  String[] elencoDataUscitaAzzCollAll = (String[])session.getAttribute("elencoDataUscitaAzCollAll");
  
  
  //Gestione errori
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  ValidationErrors errorsExt = (ValidationErrors)request.getAttribute("errors");
  
  HashMap hElencoErrori = (HashMap)request.getAttribute("hElencoErrori");
  String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                          "title=\"{2}\" border=\"0\"></a>";
  String imko = "ko.gif";
  StringProcessor jssp = new JavaScriptStringProcessor();
  //**************************
  
  
  
  String cuaa = request.getParameter("cuaa");
  String denominazione = request.getParameter("denominazione");
  String partitaIva = request.getParameter("partitaIva");
  String indirizzoIns = request.getParameter("indirizzoIns");
  String capIns = request.getParameter("capIns");
  String istatComuneIns = request.getParameter("istatComuneIns");
  String comuneIns = request.getParameter("comuneIns");
  String provinciaIns = request.getParameter("provinciaIns");
  
  
  
  //javascript: scegliComune('../view/sceltaComuneView.jsp','scegli_comune','600','300','yes',document.form1.provinciaIns.value,document.form1.comuneIns.value,'AziendeCollegateInserisci');
  
  //Visualizzo le stringhe solo se c'e' stato un errore!!!
  if(errorsExt !=null)
  {
    htmpl.set("cuaaIns",cuaa);
    htmpl.set("denominazioneIns",denominazione);
    htmpl.set("partitaIvaIns",partitaIva);
    htmpl.set("indirizzoIns",indirizzoIns);
    htmpl.set("capIns",capIns);
    htmpl.set("istatComuneIns",istatComuneIns);
    htmpl.set("comuneIns",comuneIns);
    htmpl.set("provinciaIns",provinciaIns);
  }

  if(session.getAttribute("sbloccaSociInsAMano") !=null)
  {
    htmpl.set("cuaaIns",cuaa);
    htmpl.set("denominazioneIns",denominazione);
    htmpl.set("partitaIvaIns",partitaIva);
    htmpl.set("disabledIndirizzo", "");
    htmpl.set("disabledProvincia", "");
    htmpl.set("disabledComune", "");
    htmpl.set("disabledCAP", "");
    htmpl.set("disabledCerca", "javascript: scegliComune('../view/sceltaComuneView.jsp','scegli_comune',"+
      "'600','300','yes',document.form1.provinciaIns.value,document.form1.comuneIns.value,"+
      "'AziendeCollegateInserisci');");
  }
  else
  {
    htmpl.set("disabledIndirizzo", "disabled=true");
    htmpl.set("disabledProvincia", "disabled=true");
    htmpl.set("disabledComune", "disabled=true");
    htmpl.set("disabledCAP", "disabled=true");
    htmpl.set("disabledCerca", "#");    
  }
  

  
  // GESTIONE ERRORI RELATIVI ALL'INSERIMENTO
  if(errorsExt != null && errorsExt.size() > 0) 
  {
    Iterator iter = htmpl.getVariableIterator();
    while(iter.hasNext()) 
    {
      String key = (String)iter.next();
      if(key.startsWith("err_")) 
      {
        String property = key.substring(4);
        Iterator errorIterator = errorsExt.get(property);
        if(errorIterator != null) 
        {
          ValidationError error = (ValidationError)errorIterator.next();
          htmpl.set("err_"+property,
            MessageFormat.format(htmlStringKO,
            new Object[] {
            pathErrori + "/"+ imko,
            "'"+jssp.process(error.getMessage())+"'",
            error.getMessage()}),
            null);
        }
      }
    }
  }
  

  
  if(vAziendaIns != null && vAziendaIns.size() > 0) 
  {
    htmpl.newBlock("blkAziende");
    
    
    
    // Michele, 27/11/2009 - INIZIO
    String labelElencoAssociati = null;
    labelElencoAssociati = anagVO.getLabelElencoAssociati();
    labelElencoAssociati = (labelElencoAssociati == null ? "Elenco soci": labelElencoAssociati);
    htmpl.set("blkAziende.aziendeCollegateLabel", labelElencoAssociati);
    // Michele, 27/11/2009 - FINE
    
    htmpl.set("blkAziende.azPadreCUAA",anagVO.getCUAA());
    htmpl.set("blkAziende.azPadreDenominazione", anagVO.getDenominazione());
    
    
    
    Iterator iteraAziende = vAziendaIns.iterator();
    int count = 0;
    while(iteraAziende.hasNext()) 
    {
      AziendaCollegataVO azVO = (AziendaCollegataVO)iteraAziende.next();
      htmpl.newBlock("blkElencoAziende");
      
      htmpl.set("blkAziende.blkElencoAziende.chkIdCount",new Integer(count).toString());
      
      
      //String idAzienda = azVO.getIdAzienda().toString();
      if(azVO.getSoggettoAssociato() != null)
      {
        SoggettoAssociatoVO soggVO = azVO.getSoggettoAssociato(); 
        htmpl.set("blkAziende.blkElencoAziende.cuaa",soggVO.getCuaa());
        htmpl.set("blkAziende.blkElencoAziende.partitaIva",soggVO.getPartitaIva());
        htmpl.set("blkAziende.blkElencoAziende.nomeAzienda",soggVO.getDenominazione());
        
        String sedeLegale = "";        
        String comune = sedeLegale += soggVO.getDenominazioneComune() +" ("+soggVO.getSglProv()+") ";
        htmpl.set("blkAziende.blkElencoAziende.comune",comune);
        htmpl.set("blkAziende.blkElencoAziende.indirizzo", soggVO.getIndirizzo());
        htmpl.set("blkAziende.blkElencoAziende.cap", soggVO.getCap());        
      
      }
      else
      {
        htmpl.set("blkAziende.blkElencoAziende.cuaa",azVO.getCuaa());
        htmpl.set("blkAziende.blkElencoAziende.partitaIva",azVO.getPartitaIva());
        htmpl.set("blkAziende.blkElencoAziende.nomeAzienda",azVO.getDenominazione());
        
        String sedeLegale = "";
        if(Validator.isNotEmpty(azVO.getIstatComune()))
        {
          String comune = sedeLegale += azVO.getDenominazioneComune() +" ("+azVO.getSglProv()+") ";
          htmpl.set("blkAziende.blkElencoAziende.comune",comune);
          htmpl.set("blkAziende.blkElencoAziende.indirizzo", azVO.getIndirizzo());
          htmpl.set("blkAziende.blkElencoAziende.cap", azVO.getCap());
        }
        else //comune straniero
        {
          htmpl.set("blkAziende.blkElencoAziende.comune",azVO.getDenominazioneComune());
          htmpl.set("blkAziende.blkElencoAziende.indirizzo",azVO.getIndirizzo());
        }
      
      }
      
      
      if((elencoDataIngressoAzzCollAll !=null) && (count < elencoDataIngressoAzzCollAll.length))
      {
        htmpl.set("blkAziende.blkElencoAziende.dataIngresso",elencoDataIngressoAzzCollAll[count]);
      }
      else
      {
        htmpl.set("blkAziende.blkElencoAziende.dataIngresso", DateUtils.formatDate(azVO.getDataIngresso()));
      }
      
      if((elencoDataUscitaAzzCollAll !=null) && (count < elencoDataUscitaAzzCollAll.length))
      {
        htmpl.set("blkAziende.blkElencoAziende.dataUscita", elencoDataUscitaAzzCollAll[count]);
      }
        
        
      // GESTIONE ERRORI RELATIVI ALLA CONFERMA
      if(hElencoErrori != null) 
      {
        ValidationErrors errorsRecord = (ValidationErrors)hElencoErrori.get(new Long(count));
        if(errorsRecord != null && errorsRecord.size() > 0) 
        {
          Iterator iter = htmpl.getVariableIterator();
          while(iter.hasNext()) 
          {
            String key = (String)iter.next();
            if(key.startsWith("err_")) 
            {
              String property = key.substring(4);
              Iterator errorIterator = errorsRecord.get(property);
              if(errorIterator != null) 
              {
                ValidationError error = (ValidationError)errorIterator.next();
                htmpl.set("blkAziende.blkElencoAziende.err_"+property,
                  MessageFormat.format(htmlStringKO,
                  new Object[] {
                  pathErrori + "/"+ imko,
                  "'"+jssp.process(error.getMessage())+"'",
                  error.getMessage()}),
                  null);
              }
            }
          }
        }
      }
      
      count++;
    }
  }
  
  
  
  

  
  if(Validator.isNotEmpty(messaggioErrore)) {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  
  
  out.print(htmpl.text());
  
%>
