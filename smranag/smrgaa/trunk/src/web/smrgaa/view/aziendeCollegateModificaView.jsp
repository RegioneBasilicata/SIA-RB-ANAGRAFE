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
<%@ page import="java.text.MessageFormat"%>

<%


  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/aziendeCollegateModifica.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%
  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  htmpl.set("headMenuScroll", headMenuScroll, null);
  htmpl.set("headMenuScrollIE6", headMenuScrollIE6, null);
 
  Vector elencoCollegate = (Vector)request.getAttribute("elencoCollegate");
  AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  //String[] elencoIdAziendaAzCollAll = (String[])session.getAttribute("elencoIdAziendaAzCollAll");
  //tutte le aziende per associare correttamente le date
  String[] elencoDataIngressoAzzCollAll = (String[])session.getAttribute("elencoDataIngressoAzCollAll");
  String[] elencoDataUscitaAzzCollAll = (String[])session.getAttribute("elencoDataUscitaAzCollAll");
  
  
  
  //Gestione errori
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  //ValidationErrors errorsExt = (ValidationErrors)request.getAttribute("errors");
  
  HashMap hElencoErrori = (HashMap)request.getAttribute("hElencoErrori");
  String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                          "title=\"{2}\" border=\"0\"></a>";
  String imko = "ko.gif";
  StringProcessor jssp = new JavaScriptStringProcessor();
  //**************************
  

  
  if(elencoCollegate != null && elencoCollegate.size() > 0) 
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
    
    
    
    Iterator iteraAziende = elencoCollegate.iterator();
    int count = 0;
    while(iteraAziende.hasNext()) 
    {
    
      AziendaCollegataVO azVO = (AziendaCollegataVO)iteraAziende.next();
      boolean flagCensita = true;
      
      if(azVO.getIdSoggettoAssociato() != null)
      {
        flagCensita = false;
      }
      
      
      htmpl.newBlock("blkElencoAziende");
      
      
      String idAziendaCollegata = azVO.getIdAziendaCollegata().toString();
      htmpl.set("blkAziende.blkElencoAziende.idAziendaCollegataHid",idAziendaCollegata);
      
      
      
      if(flagCensita)
      {
      
        htmpl.set("blkAziende.blkElencoAziende.cuaa",azVO.getCuaa());
        htmpl.set("blkAziende.blkElencoAziende.partitaIva",azVO.getPartitaIva());
        htmpl.set("blkAziende.blkElencoAziende.nomeAzienda",azVO.getDenominazione());
        
        String sedeLegale = "";
        if(Validator.isNotEmpty(azVO.getSedeCittaEstero()))
        {
          htmpl.set("blkAziende.blkElencoAziende.comune",azVO.getSedeCittaEstero());
          htmpl.set("blkAziende.blkElencoAziende.indirizzo",azVO.getIndirizzo());
        }
        else
        {
          String comune = sedeLegale += azVO.getDenominazioneComune() +" ("+azVO.getSglProv()+") ";
          htmpl.set("blkAziende.blkElencoAziende.comune",comune);
          htmpl.set("blkAziende.blkElencoAziende.indirizzo", azVO.getIndirizzo());
          htmpl.set("blkAziende.blkElencoAziende.cap", azVO.getCap());
        }
      }
      else
      {
      
        SoggettoAssociatoVO soggVO = azVO.getSoggettoAssociato();
        htmpl.set("blkAziende.blkElencoAziende.cuaa",soggVO.getCuaa());
        htmpl.set("blkAziende.blkElencoAziende.partitaIva",soggVO.getPartitaIva());
        htmpl.set("blkAziende.blkElencoAziende.nomeAzienda",soggVO.getDenominazione());
                
        String comune = soggVO.getDenominazioneComune() +" ("+soggVO.getSglProv()+") ";
        htmpl.set("blkAziende.blkElencoAziende.comune",comune);
        htmpl.set("blkAziende.blkElencoAziende.indirizzo", soggVO.getIndirizzo());
        htmpl.set("blkAziende.blkElencoAziende.cap", soggVO.getCap());
        
      
      }
      
      if((elencoDataIngressoAzzCollAll !=null) && (count < elencoDataIngressoAzzCollAll.length))
      {
        htmpl.set("blkAziende.blkElencoAziende.dataIngresso",elencoDataIngressoAzzCollAll[count]);
      }
      else
      {
        htmpl.set("blkAziende.blkElencoAziende.dataIngresso",
          DateUtils.formatDateNotNull(azVO.getDataIngresso()));
      }
      
      if((elencoDataUscitaAzzCollAll !=null) && (count < elencoDataUscitaAzzCollAll.length))
      {
        htmpl.set("blkAziende.blkElencoAziende.dataUscita", elencoDataUscitaAzzCollAll[count]);
      }
      else
      {
        htmpl.set("blkAziende.blkElencoAziende.dataUscita",
          DateUtils.formatDateNotNull(azVO.getDataUscita()));
      }
        
        
      // GESTIONE ERRORI RELATIVI ALLA CONFERMA
      if(hElencoErrori != null) 
      {
        ValidationErrors errorsRecord = (ValidationErrors)hElencoErrori.get(new Long(idAziendaCollegata));
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
  
  
  //HtmplUtil.setErrors(htmpl, errors, request, application);
  out.print(htmpl.text());
  
%>
