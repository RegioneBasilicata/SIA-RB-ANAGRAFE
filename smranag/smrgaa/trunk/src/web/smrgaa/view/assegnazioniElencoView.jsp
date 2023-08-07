<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smruma.umaserv.dto.DittaUmaVO" %>
<%@ page import="it.csi.smruma.umaserv.dto.AnnoRiferimentoVO" %>
<%@ page import="it.csi.smruma.umaserv.dto.AssegnazioneVO" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="it.csi.solmr.dto.comune.AmmCompetenzaVO" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@page import="it.csi.solmr.etc.SolmrConstants"%>

<%


  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/assegnazioniElenco.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%
  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);


  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  
  DittaUmaVO[] assegnazioniElenco = 
    (DittaUmaVO[])request.getAttribute("assegnazioniElenco");
  
    
  String idAnno = request.getParameter("idAnno");
  String regimeAssegnazioni = request.getParameter("regimeAssegnazioni");
  TreeMap<Integer,String> tAnno = (TreeMap<Integer,String>)request.getAttribute("tAnno");
 
 
  if(assegnazioniElenco != null)
  {    
      
    if(tAnno != null) 
    {
      Iterator<String> iteraAnno = tAnno.values().iterator();
      
      while(iteraAnno.hasNext()) 
      {
        String anno = iteraAnno.next();
        htmpl.newBlock("blkElencoAnno");
        htmpl.set("blkElencoAnno.anno", anno);
        
        if(Validator.isNotEmpty(idAnno) && idAnno.equalsIgnoreCase(anno)) 
        {
          htmpl.set("blkElencoAnno.selected", "selected=\"selected\"", null);
        }
               
      }
    }    
 
  }
  
  
  
 
  DittaUmaVO[] assegnazioniElencoFiltro = 
    (DittaUmaVO[])request.getAttribute("assegnazioniElencoFiltro");
  
  BigDecimal totImporto = new BigDecimal(0);
  BigDecimal totImportoRecuperato = new BigDecimal(0);  
  BigDecimal totImportoInLiquidazione = new BigDecimal(0);    
  if((assegnazioniElencoFiltro != null) && (assegnazioniElencoFiltro.length > 0))
  {
    int numeRecord = 0;
    for(int i=0;i<assegnazioniElencoFiltro.length;i++)
    {
      if(assegnazioniElencoFiltro[i].getArrAnnoRiferimentoVO() != null)
      {
        numeRecord += assegnazioniElencoFiltro[i].getArrAnnoRiferimentoVO().length;
      }
      else
      {
        numeRecord++;
      }
    }
    
    htmpl.newBlock("blkElencoAssegnazioni");
    htmpl.set("blkElencoAssegnazioni.numeroRecord",""+numeRecord);
    
    int aggancio = 0;
    for(int i=0;i<assegnazioniElencoFiltro.length;i++)
    {
      DittaUmaVO dittaUma = assegnazioniElencoFiltro[i];
	    if(assegnazioniElencoFiltro[i].getArrAnnoRiferimentoVO() != null)
	    {
	      for(int j=0;j<assegnazioniElencoFiltro[i].getArrAnnoRiferimentoVO().length;j++)
	      {
	        //calcolo totali
	        BigDecimal rimanenza = new BigDecimal(0);
	        BigDecimal consumo = new BigDecimal(0);
	        BigDecimal totAssegnata = new BigDecimal(0);
	        BigDecimal totPrelevata = new BigDecimal(0);
	        boolean trovatoA = false;
	        boolean trovatoB = false; 
	        AnnoRiferimentoVO annoRiferimento = assegnazioniElencoFiltro[i].getArrAnnoRiferimentoVO()[j];
	        String idDomandaAssegnazione = "";
	        Vector<Long> vIdDomandaAssegnazione = new Vector<Long>();
	        for(int k=0;k<annoRiferimento.getArrAssegnazioneVO().length;k++)
	        {
	          AssegnazioneVO assegnazione = annoRiferimento.getArrAssegnazioneVO()[k];
	          if(k==0)
	          {
	            if(Validator.isNotEmpty(assegnazione.getQtaRimanenza()))
                rimanenza = rimanenza.add(assegnazione.getQtaRimanenza());
	            if(Validator.isNotEmpty(assegnazione.getQtaConsumo()))
                consumo = consumo.add(assegnazione.getQtaConsumo());
	          }
	          
	          
	          if(annoRiferimento.getArrAssegnazioneVO().length == 1)
	          {
	            totAssegnata = assegnazione.getQtaAssegnata();
	          }
	          else
	          {
	            if(Validator.isNotEmpty(assegnazione.getQtaAssegnata())
	              && !assegnazione.getDescAssegnazione().contains("Acconto"))
	            {
	              totAssegnata = totAssegnata.add(assegnazione.getQtaAssegnata());
	            }
	          }
	          if(Validator.isNotEmpty(assegnazione.getQtaPrelevata()))
	            totPrelevata = totPrelevata.add(assegnazione.getQtaPrelevata());
	          
	          if(!vIdDomandaAssegnazione.contains(new Long(assegnazione.getIdDomandaAssegnazione())))
	          {
		          if(k!=0)
		          {
		            idDomandaAssegnazione += ",";
		          }
		          idDomandaAssegnazione += ""+assegnazione.getIdDomandaAssegnazione();
		          
		          vIdDomandaAssegnazione.add(new Long(assegnazione.getIdDomandaAssegnazione()));
		        }
	        }
	        
	        htmpl.newBlock("blkElencoAssegnazioni.blkAssegnazione");
	        htmpl.newBlock("blkElencoAssegnazioni.blkAssegnazione.blkTotale");
	        htmpl.set("blkElencoAssegnazioni.blkAssegnazione.blkTotale.aggancio","tr"+aggancio);
	        htmpl.set("blkElencoAssegnazioni.blkAssegnazione.blkTotale.idDomandaAssegnazione",idDomandaAssegnazione);
	        String idDomandaAssegnazionePar = request.getParameter("idDomandaAssegnazione");
	        if(Validator.isNotEmpty(idDomandaAssegnazionePar) && idDomandaAssegnazionePar.equalsIgnoreCase(idDomandaAssegnazione)) 
	        {
	          htmpl.set("blkElencoAssegnazioni.blkAssegnazione.blkTotale.checkedTotale", "checked=\"true\"", null);
	        }
	        else
	        {
	          if(aggancio == 0)
	          {
	            htmpl.set("blkElencoAssegnazioni.blkAssegnazione.blkTotale.checkedTotale", "checked=\"true\"", null);
	          }
	        }
	        
	        
	        htmpl.set("blkElencoAssegnazioni.blkAssegnazione.blkTotale.annoRiferimento",""+annoRiferimento.getAnnoRiferimento());
	        htmpl.set("blkElencoAssegnazioni.blkAssegnazione.blkTotale.dittaUma",""+dittaUma.getDittaUmaNum()+"/"+dittaUma.getSglProv());
	        htmpl.set("blkElencoAssegnazioni.blkAssegnazione.blkTotale.totRimanenza", Formatter.formatDouble(rimanenza));
	        htmpl.set("blkElencoAssegnazioni.blkAssegnazione.blkTotale.totConsumo", Formatter.formatDouble(consumo));
	        htmpl.set("blkElencoAssegnazioni.blkAssegnazione.blkTotale.totAssegnata", Formatter.formatDouble(totAssegnata));
	        htmpl.set("blkElencoAssegnazioni.blkAssegnazione.blkTotale.totPrelevata", Formatter.formatDouble(totPrelevata));
	        
	        
	        BigDecimal totAmenoB = new BigDecimal(0); 
	        for(int k=0;k<annoRiferimento.getArrAssegnazioneVO().length;k++)
          {
            AssegnazioneVO assegnazione = annoRiferimento.getArrAssegnazioneVO()[k];
            htmpl.newBlock("blkElencoAssegnazioni.blkAssegnazione");
            htmpl.newBlock("blkElencoAssegnazioni.blkAssegnazione.blkSingolo");
            htmpl.set("blkElencoAssegnazioni.blkAssegnazione.blkSingolo.aggancio","tr"+aggancio);
            
            htmpl.set("blkElencoAssegnazioni.blkAssegnazione.blkSingolo.tipoAssegnazione", assegnazione.getDescAssegnazione());
            htmpl.set("blkElencoAssegnazioni.blkAssegnazione.blkSingolo.statoAssegnazione", assegnazione.getStatoAssegnazione());
            htmpl.set("blkElencoAssegnazioni.blkAssegnazione.blkSingolo.dataAssegnazione", DateUtils.formatDateNotNull(assegnazione.getDataAssegnazione()));
            htmpl.set("blkElencoAssegnazioni.blkAssegnazione.blkSingolo.rimanenza", "-");
            htmpl.set("blkElencoAssegnazioni.blkAssegnazione.blkSingolo.consumo", "-");
            if("A".equalsIgnoreCase(assegnazione.getTipoDomanda()))
            {
              totAmenoB = totAmenoB.subtract(assegnazione.getQtaAssegnata());
              trovatoA = true;
            }
            else if("B".equalsIgnoreCase(assegnazione.getTipoDomanda())
             && !"S".equalsIgnoreCase(assegnazione.getTipoAssegnazione()))
            {
              if(Validator.isNotEmpty(assegnazione.getQtaAssegnata()))
                totAmenoB = totAmenoB.add(assegnazione.getQtaAssegnata());
              trovatoB = true;
            }
            
            BigDecimal qtaAssegnata = assegnazione.getQtaAssegnata();
            if("B".equalsIgnoreCase(assegnazione.getTipoDomanda())
              && trovatoA 
              && trovatoB
              &&  !"S".equalsIgnoreCase(assegnazione.getTipoAssegnazione()))
            {
              qtaAssegnata = totAmenoB;
            }
            htmpl.set("blkElencoAssegnazioni.blkAssegnazione.blkSingolo.assegnata", Formatter.formatDouble(qtaAssegnata));
            htmpl.set("blkElencoAssegnazioni.blkAssegnazione.blkSingolo.prelevata", "-");
            
          }
	        
	        aggancio++;
	      }
	    }
	    else
	    {
	      htmpl.newBlock("blkElencoAssegnazioni.blkAssegnazione");
        htmpl.newBlock("blkElencoAssegnazioni.blkAssegnazione.blkTotale");
	      htmpl.set("blkElencoAssegnazioni.blkAssegnazione.blkTotale.aggancio","tr"+aggancio);
        htmpl.set("blkElencoAssegnazioni.blkAssegnazione.blkTotale.dittaUma",""+dittaUma.getDittaUmaNum()+"/"+dittaUma.getSglProv());
	      htmpl.newBlock("blkElencoAssegnazioni.blkAssegnazione");
        htmpl.newBlock("blkElencoAssegnazioni.blkAssegnazione.blkVuotoSingolo");
        htmpl.set("blkElencoAssegnazioni.blkAssegnazione.blkVuotoSingolo.aggancio","tr"+aggancio);
	      
	      aggancio++;
	    }
	    
	    
	  }
      

    
    
    
  
  
  }
  else 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggio", SolmrConstants.NO_BUONI_CARBURANTE);
  } 
 
  
  
  

  
  if(Validator.isNotEmpty(messaggioErrore)) {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  
  
  HtmplUtil.setErrors(htmpl, errors, request, application);
  out.print(htmpl.text());
  
%>


