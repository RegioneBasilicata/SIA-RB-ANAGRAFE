<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smruma.umaserv.dto.AssegnazioneVO" %>
<%@ page import="it.csi.smruma.umaserv.dto.QtaAssegnazioneVO" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="it.csi.solmr.dto.comune.AmmCompetenzaVO" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>

<%


  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/assegnazioniDettaglio.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%
  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);


  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  
  AssegnazioneVO[] arrAssegnazioneDettaglio = 
    (AssegnazioneVO[])request.getAttribute("arrAssegnazioneDettaglio");
  
    
 
  htmpl.set("idDomandaAssegnazione",request.getParameter("idDomandaAssegnazione"));
  
  
  
  if(arrAssegnazioneDettaglio != null)
  {
    String styleColor="style=\"background-color: #999;\"";
    
    
    //E' per tutti i record uguale prendo il primo...
    htmpl.set("annoCampagna", 
      ""+DateUtils.extractYearFromDate(arrAssegnazioneDettaglio[0].getDataAssegnazione()));
    
    boolean trovaGasolio = false;
    boolean trovaBenzina = false;
    boolean alamenoUno = false;
    for(int i=0;i<arrAssegnazioneDettaglio.length;i++)
    {
      AssegnazioneVO assegnazione = arrAssegnazioneDettaglio[i];
      //per i consumi rimanenze prendo sempre il primo record che incontro..
      //uno per gasolio e uno per benzina
      
      
      for(int j=0;j<arrAssegnazioneDettaglio[i].getArrQtaAssegnazione().length;j++)
      { 
        QtaAssegnazioneVO qtaAssegnazione = arrAssegnazioneDettaglio[i].getArrQtaAssegnazione()[j];
        BigDecimal qtaRimanenzaCP = new BigDecimal(0);
        if(Validator.isNotEmpty(qtaAssegnazione.getQtaRimanenzaCP()))
          qtaRimanenzaCP = qtaAssegnazione.getQtaRimanenzaCP();
        BigDecimal qtaConsumoCP = new BigDecimal(0);
        if(Validator.isNotEmpty(qtaAssegnazione.getQtaConsumoCP()))
          qtaConsumoCP = qtaAssegnazione.getQtaConsumoCP();
        BigDecimal qtaRimanenzaCT = new BigDecimal(0);
        if(Validator.isNotEmpty(qtaAssegnazione.getQtaRimanenzaCT()))
          qtaRimanenzaCT = qtaAssegnazione.getQtaRimanenzaCT();
        BigDecimal qtaConsumoCT = new BigDecimal(0);
        if(Validator.isNotEmpty(qtaAssegnazione.getQtaConsumoCT()))
          qtaConsumoCT = qtaAssegnazione.getQtaConsumoCT();
        BigDecimal qtaRimanenzaSerra = new BigDecimal(0);
        if(Validator.isNotEmpty(qtaAssegnazione.getQtaRimanenzaSerra()))
          qtaRimanenzaSerra = qtaAssegnazione.getQtaRimanenzaSerra();
        BigDecimal qtaConsumoSerra = new BigDecimal(0);
        if(Validator.isNotEmpty(qtaAssegnazione.getQtaConsumoSerra()))
          qtaConsumoSerra = qtaAssegnazione.getQtaConsumoSerra();
        
        if("Benzina".equalsIgnoreCase(qtaAssegnazione.getTipoCarburante())
          && !trovaBenzina && 
          !(BigDecimal.ZERO.compareTo(qtaRimanenzaCP) ==0 && 
          BigDecimal.ZERO.compareTo(qtaConsumoCP) ==0 &&
          BigDecimal.ZERO.compareTo(qtaRimanenzaCT) ==0 &&
          BigDecimal.ZERO.compareTo(qtaConsumoCT) ==0 &&
          BigDecimal.ZERO.compareTo(qtaRimanenzaSerra) ==0 &&
          BigDecimal.ZERO.compareTo(qtaConsumoSerra) ==0))
        {
          trovaBenzina = true;
          if(!alamenoUno)
          {
            alamenoUno = true;
            htmpl.newBlock("blkElencoConsRim");
            htmpl.set("blkElencoConsRim.annoCampagnaMenoUno", 
              ""+(DateUtils.extractYearFromDate(arrAssegnazioneDettaglio[0].getDataAssegnazione())-1));
          }
          htmpl.newBlock("blkElencoConsRim.blkConsRim");
          htmpl.set("blkElencoConsRim.blkConsRim.tipoCarburante",""+qtaAssegnazione.getTipoCarburante());
          htmpl.set("blkElencoConsRim.blkConsRim.qtaRimanenzaCP", Formatter.formatDouble(qtaRimanenzaCP));
          htmpl.set("blkElencoConsRim.blkConsRim.qtaConsumoCP", Formatter.formatDouble(qtaConsumoCP));
          htmpl.set("blkElencoConsRim.blkConsRim.qtaRimanenzaCT", Formatter.formatDouble(qtaRimanenzaCT));
          htmpl.set("blkElencoConsRim.blkConsRim.qtaConsumoCT", Formatter.formatDouble(qtaConsumoCT));
          htmpl.set("blkElencoConsRim.blkConsRim.qtaRimanenzaSerra", Formatter.formatDouble(qtaRimanenzaSerra));
          htmpl.set("blkElencoConsRim.blkConsRim.qtaConsumoSerra", Formatter.formatDouble(qtaConsumoSerra));
        }
        
        if("Gasolio".equalsIgnoreCase(qtaAssegnazione.getTipoCarburante())
          && !trovaGasolio && 
          !(BigDecimal.ZERO.compareTo(qtaRimanenzaCP) ==0 && 
          BigDecimal.ZERO.compareTo(qtaConsumoCP) ==0 &&
          BigDecimal.ZERO.compareTo(qtaRimanenzaCT) ==0 &&
          BigDecimal.ZERO.compareTo(qtaConsumoCT) ==0 &&
          BigDecimal.ZERO.compareTo(qtaRimanenzaSerra) ==0 &&
          BigDecimal.ZERO.compareTo(qtaConsumoSerra) ==0))
        {
          trovaGasolio = true;
          if(!alamenoUno)
          {
            alamenoUno = true;
            htmpl.newBlock("blkElencoConsRim");
            htmpl.set("blkElencoConsRim.annoCampagnaMenoUno", 
              ""+(DateUtils.extractYearFromDate(arrAssegnazioneDettaglio[0].getDataAssegnazione())-1));
          }
          htmpl.newBlock("blkElencoConsRim.blkConsRim");
          htmpl.set("blkElencoConsRim.blkConsRim.tipoCarburante",""+qtaAssegnazione.getTipoCarburante());
          htmpl.set("blkElencoConsRim.blkConsRim.qtaRimanenzaCP", Formatter.formatDouble(qtaRimanenzaCP));
          htmpl.set("blkElencoConsRim.blkConsRim.qtaConsumoCP", Formatter.formatDouble(qtaConsumoCP));
          htmpl.set("blkElencoConsRim.blkConsRim.qtaRimanenzaCT", Formatter.formatDouble(qtaRimanenzaCT));
          htmpl.set("blkElencoConsRim.blkConsRim.qtaConsumoCT", Formatter.formatDouble(qtaConsumoCT));
          htmpl.set("blkElencoConsRim.blkConsRim.qtaRimanenzaSerra", Formatter.formatDouble(qtaRimanenzaSerra));
          htmpl.set("blkElencoConsRim.blkConsRim.qtaConsumoSerra", Formatter.formatDouble(qtaConsumoSerra));
        }
        
        
      }
      
    }
    
    
    
    
    
    htmpl.newBlock("blkElencoAssegnazioni");
    //E' per tutti i record uguale prendo il primo...
    htmpl.set("blkElencoAssegnazioni.annoCampagna", 
      ""+DateUtils.extractYearFromDate(arrAssegnazioneDettaglio[0].getDataAssegnazione()));
    
    int numeroAssegnazione = 0;
    boolean trovatoA = false;
    boolean trovatoB = false;
    HashMap<Integer, BigDecimal> hTotAmenoBCT = new HashMap<Integer, BigDecimal>();
    HashMap<Integer, BigDecimal> hTotAmenoBCP = new HashMap<Integer, BigDecimal>();
    HashMap<Integer, BigDecimal> hTotAmenoBSerra = new HashMap<Integer, BigDecimal>();  
    
    for(int i=0;i<arrAssegnazioneDettaglio.length;i++)
    {
      AssegnazioneVO assegnazione = arrAssegnazioneDettaglio[i];
      int numCarburante = 0;
      
      int numeroRighe = 0;
      {
        for(int j=0;j<arrAssegnazioneDettaglio[i].getArrQtaAssegnazione().length;j++)
	      { 
	        QtaAssegnazioneVO qtaAssegnazione = arrAssegnazioneDettaglio[i].getArrQtaAssegnazione()[j];
	        BigDecimal qtaAssegnataCP = new BigDecimal(0);
	        if(Validator.isNotEmpty(qtaAssegnazione.getQtaAssegnataCP()))
	          qtaAssegnataCP = qtaAssegnazione.getQtaAssegnataCP();
	        BigDecimal qtaAssegnataCT = new BigDecimal(0);
          if(Validator.isNotEmpty(qtaAssegnazione.getQtaAssegnataCT()))
            qtaAssegnataCT = qtaAssegnazione.getQtaAssegnataCT();
          BigDecimal qtaAssegnataSerra= new BigDecimal(0);
          if(Validator.isNotEmpty(qtaAssegnazione.getQtaAssegnataSerra()))
            qtaAssegnataSerra = qtaAssegnazione.getQtaAssegnataSerra();    
	          
	        if(!(BigDecimal.ZERO.compareTo(qtaAssegnataCP) == 0 && 
	          BigDecimal.ZERO.compareTo(qtaAssegnataCT) == 0 &&
	          BigDecimal.ZERO.compareTo(qtaAssegnataSerra) == 0))
	        {
	          numeroRighe++;
	        }
	      }
      }
      
     
      for(int j=0;j<arrAssegnazioneDettaglio[i].getArrQtaAssegnazione().length;j++)
      {	
        QtaAssegnazioneVO qtaAssegnazione = arrAssegnazioneDettaglio[i].getArrQtaAssegnazione()[j];
        
        BigDecimal qtaAssegnataCP = new BigDecimal(0);
        if(Validator.isNotEmpty(qtaAssegnazione.getQtaAssegnataCP()))
          qtaAssegnataCP = qtaAssegnazione.getQtaAssegnataCP();
        BigDecimal qtaAssegnataCT = new BigDecimal(0);
        if(Validator.isNotEmpty(qtaAssegnazione.getQtaAssegnataCT()))
          qtaAssegnataCT = qtaAssegnazione.getQtaAssegnataCT();
        BigDecimal qtaAssegnataSerra= new BigDecimal(0);
        if(Validator.isNotEmpty(qtaAssegnazione.getQtaAssegnataSerra()))
          qtaAssegnataSerra = qtaAssegnazione.getQtaAssegnataSerra(); 
       
        if(!(BigDecimal.ZERO.compareTo(qtaAssegnataCP) == 0 && 
          BigDecimal.ZERO.compareTo(qtaAssegnataCT) == 0 &&
          BigDecimal.ZERO.compareTo(qtaAssegnataSerra) == 0))
        {
       
          htmpl.newBlock("blkElencoAssegnazioni.blkAssegnazione");
          String blk = "blkElencoAssegnazioni.blkAssegnazione.blkSecondo";      
	      
	      
		      if(numCarburante == 0)
		      {
		        blk = "blkElencoAssegnazioni.blkAssegnazione.blkPrimo";
		        htmpl.newBlock(blk);	        
		        htmpl.set(blk+".numRow",""+numeroRighe);
		        htmpl.set(blk+".tipoAssegnazione",""+arrAssegnazioneDettaglio[i].getDescAssegnazione());
		      }
		      else
		      {
		        htmpl.newBlock(blk);
		      }
		      
		      if((numeroAssegnazione % 2) == 1)
	        {
	          htmpl.set(blk+".coloreRiga",styleColor, null);
	        }
		      
		      htmpl.set(blk+".tipoCarburante",""+qtaAssegnazione.getTipoCarburante());
		      if("A".equalsIgnoreCase(assegnazione.getTipoDomanda()))
          {
            BigDecimal totAmenoBCP = hTotAmenoBCP.get(new Integer(qtaAssegnazione.getIdCarburante()));
            if(Validator.isEmpty(totAmenoBCP))
              totAmenoBCP = new BigDecimal(0);
            totAmenoBCP = totAmenoBCP.subtract(qtaAssegnataCP);
            hTotAmenoBCP.put(new Integer(qtaAssegnazione.getIdCarburante()), totAmenoBCP);
            
            BigDecimal totAmenoBCT = hTotAmenoBCT.get(new Integer(qtaAssegnazione.getIdCarburante()));
            if(Validator.isEmpty(totAmenoBCT))
              totAmenoBCT = new BigDecimal(0);
            totAmenoBCT = totAmenoBCT.subtract(qtaAssegnataCT);
            hTotAmenoBCT.put(new Integer(qtaAssegnazione.getIdCarburante()), totAmenoBCT);
            
            BigDecimal totAmenoBSerra = hTotAmenoBSerra.get(new Integer(qtaAssegnazione.getIdCarburante()));
            if(Validator.isEmpty(totAmenoBSerra))
              totAmenoBSerra = new BigDecimal(0);
            totAmenoBSerra = totAmenoBSerra.subtract(qtaAssegnataSerra);
            hTotAmenoBSerra.put(new Integer(qtaAssegnazione.getIdCarburante()), totAmenoBSerra);
            
            trovatoA = true;
          }
          else if("B".equalsIgnoreCase(assegnazione.getTipoDomanda())
           && !"S".equalsIgnoreCase(assegnazione.getTipoAssegnazione()))
          {
            BigDecimal totAmenoBCP = hTotAmenoBCP.get(new Integer(qtaAssegnazione.getIdCarburante()));
            if(Validator.isEmpty(totAmenoBCP))
              totAmenoBCP = new BigDecimal(0);
            totAmenoBCP = totAmenoBCP.add(qtaAssegnataCP);
            hTotAmenoBCP.put(new Integer(qtaAssegnazione.getIdCarburante()), totAmenoBCP);
            
            BigDecimal totAmenoBCT = hTotAmenoBCT.get(new Integer(qtaAssegnazione.getIdCarburante()));
            if(Validator.isEmpty(totAmenoBCT))
              totAmenoBCT = new BigDecimal(0);
            totAmenoBCT = totAmenoBCT.add(qtaAssegnataCT);
            hTotAmenoBCT.put(new Integer(qtaAssegnazione.getIdCarburante()), totAmenoBCT);
            
            BigDecimal totAmenoBSerra = hTotAmenoBSerra.get(new Integer(qtaAssegnazione.getIdCarburante()));
            if(Validator.isEmpty(totAmenoBSerra))
              totAmenoBSerra = new BigDecimal(0);
            totAmenoBSerra = totAmenoBSerra.add(qtaAssegnataSerra);
            hTotAmenoBSerra.put(new Integer(qtaAssegnazione.getIdCarburante()), totAmenoBSerra);
            
            trovatoB = true;
          }
          
   
          if("B".equalsIgnoreCase(assegnazione.getTipoDomanda())
            && trovatoA 
            && trovatoB
            && !"S".equalsIgnoreCase(assegnazione.getTipoAssegnazione()))
          {
            qtaAssegnataCP = hTotAmenoBCP.get(new Integer(qtaAssegnazione.getIdCarburante()));
            qtaAssegnataCT = hTotAmenoBCT.get(new Integer(qtaAssegnazione.getIdCarburante()));
            qtaAssegnataSerra = hTotAmenoBSerra.get(new Integer(qtaAssegnazione.getIdCarburante()));
          }
		      
		      
		      
		      htmpl.set(blk+".qtaAssegnatoCP", Formatter.formatDouble(qtaAssegnataCP));
		      htmpl.set(blk+".qtaAssegnatoCT", Formatter.formatDouble(qtaAssegnataCT));
		      htmpl.set(blk+".qtaAssegnatoSerra", Formatter.formatDouble(qtaAssegnataSerra));
		      numCarburante++;
		    }
	    }
	    
	    numeroAssegnazione++;
	  }
	}
      

    
    
    
  
  
 
 
  
  
  

  
  if(Validator.isNotEmpty(messaggioErrore)) {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  
  
  HtmplUtil.setErrors(htmpl, errors, request, application);
  out.print(htmpl.text());
  
%>


