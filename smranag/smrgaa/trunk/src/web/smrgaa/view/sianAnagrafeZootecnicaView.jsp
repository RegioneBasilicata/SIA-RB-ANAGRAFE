<%@page language="java" contentType="text/html" isErrorPage="true"%>

<%@page import="it.csi.jsf.htmpl.*"%>
<%@page import="it.csi.solmr.dto.anag.sian.*"%>
<%@page import="java.util.*"%>
<%@page import="it.csi.solmr.util.*"%>
<%@ page import="it.csi.solmr.dto.anag.AnagAziendaVO" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@page import="it.csi.solmr.etc.*"%>

<%

  java.io.InputStream layout = application.getResourceAsStream("/layout/visuraBDN.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  Hashtable elencoAllevamentiSian = (Hashtable) session.getAttribute("elencoAllevamentiSian");
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  Vector<SianAllevamentiVO> vDetenzione = null;
  Vector<SianAllevamentiVO> vProprieta = null;

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  String messaggioErrore = (String) request.getAttribute("messaggioErrore");
  if (Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggio", messaggioErrore);
  }
  else 
  {
    if(elencoAllevamentiSian != null) 
    {
      Enumeration enumeraAllevamenti = elencoAllevamentiSian.elements();
      htmpl.newBlock("blkSiAllevamenti");
      
      
      while(enumeraAllevamenti.hasMoreElements()) 
      {     
        SianAllevamentiVO sianAllevamentiVO = (SianAllevamentiVO)enumeraAllevamenti.nextElement();
        if(sianAllevamentiVO.getCodFiscaleDeten().equalsIgnoreCase(anagAziendaVO.getCUAA()))
        {    
          if(vDetenzione == null)
          {
            vDetenzione = new Vector<SianAllevamentiVO>();
          }
          vDetenzione.add(sianAllevamentiVO);
        }
        else
        {
          if(vProprieta == null)
          {
            vProprieta = new Vector<SianAllevamentiVO>();
          }
          vProprieta.add(sianAllevamentiVO);
        }
      }
      
      
      if(vDetenzione != null) 
      { 
        htmpl.newBlock("blkSiAllevamenti.blkDetenzione");
        for(int i=0;i<vDetenzione.size();i++)
        {
          htmpl.newBlock("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti");    
          SianAllevamentiVO sianAllevamentiVO = vDetenzione.get(i);
	        //controllo se l'allevamento è stato selezionato (provengo dall'indietro)
	        //nel qual caso selezioni il checkbox
	        if (sianAllevamentiVO.isSelect())
	          htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.checked", "checked=\"checked\"",null);
	          
	          
	        htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.idAllevamento", sianAllevamentiVO.getAllevId().toString());
	        
	        if(Validator.isNotEmpty(sianAllevamentiVO.getDtFineAttivita())) 
	          htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.disabled", "disabled=\"disabled\"",null);
	        
	        
	        if(sianAllevamentiVO.isPresenteInAnagrafe())
	          htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.isPresenteInAnagrafe", "(*)");
	        
	        
	        htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.codiceAllevamento", sianAllevamentiVO.getAziendaCodice());
	        htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.specie", sianAllevamentiVO.getDescrizioneSpecie());
	        htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.indirizzo", sianAllevamentiVO.getIndirizzo());
	        htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.cap", sianAllevamentiVO.getCap());
	        htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.localita", sianAllevamentiVO.getLocalita());
	        htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.comune", sianAllevamentiVO.getComune());
	        htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.denominazione", sianAllevamentiVO.getDenominazione());
	        if(Validator.isNotEmpty(sianAllevamentiVO.getCapiTotali())) {
	          htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.numero", sianAllevamentiVO.getCapiTotali().toString());
	        }
	
	        if(Validator.isNotEmpty(sianAllevamentiVO.getDataCalcoloCapi())) {
	          htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.dataCalcoloCapi", StringUtils.parseDateFieldToEuropeStandard("yyyy-MM-dd", "dd/MM/yyyy", sianAllevamentiVO.getDataCalcoloCapi()));
	        }
	
	        htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.tipoProduzione", sianAllevamentiVO.getDescTipoProduzione());
	        htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.orientamentoProduttivo", sianAllevamentiVO.getDescOrientamentoProduttivo() );
	        htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.autorizzazioneLatte", sianAllevamentiVO.getAutorizzazioneLatte());
	        if(Validator.isNotEmpty(sianAllevamentiVO.getDtInizioAttivita())) {
	          htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.inizioattivita", StringUtils.parseDateFieldToEuropeStandard("yyyy-MM-dd", "dd/MM/yyyy", sianAllevamentiVO.getDtInizioAttivita()));
	        }
	        if(Validator.isNotEmpty(sianAllevamentiVO.getDtFineAttivita())) {
	          htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.fineAttivita", StringUtils.parseDateFieldToEuropeStandard("yyyy-MM-dd", "dd/MM/yyyy", sianAllevamentiVO.getDtFineAttivita()));
	        }
	        htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.cfProprietario", sianAllevamentiVO.getCodFiscaleProp());
	        htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.nomeProprietario", sianAllevamentiVO.getDenomProprietario());
	        htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.soccida", sianAllevamentiVO.getSoccida());
	        htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.cfDetentore", sianAllevamentiVO.getCodFiscaleDeten());
	        htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.nomeDetentore", sianAllevamentiVO.getDenomDetentore());
	        if(Validator.isNotEmpty(sianAllevamentiVO.getDtInizioDetentore())) {
	          htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.inizioDetentore", StringUtils.parseDateFieldToEuropeStandard("yyyy-MM-dd", "dd/MM/yyyy", sianAllevamentiVO.getDtInizioDetentore()));
	        }
	        if(Validator.isNotEmpty(sianAllevamentiVO.getDtFineDetentore())) {
	          htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.fineDetentore", StringUtils.parseDateFieldToEuropeStandard("yyyy-MM-dd", "dd/MM/yyyy", sianAllevamentiVO.getDtFineDetentore()));
	        }
	        if(Validator.isNotEmpty(sianAllevamentiVO.getComune()))
	        {
	          if(Validator.isNotEmpty(sianAllevamentiVO.getSiglaProvincia()))
	            htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.comuneProv", sianAllevamentiVO.getComune() + "("+ sianAllevamentiVO.getSiglaProvincia() +")");
	          else  
	            htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.comuneProv", sianAllevamentiVO.getComune());
	        }
	
	        htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.sez",sianAllevamentiVO.getSezione());
	        htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.foglio", sianAllevamentiVO.getFoglioCatastale());
	        htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.parti", sianAllevamentiVO.getParticella());
	        htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.sub", sianAllevamentiVO.getSubalterno());
	        htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.longitudine", Formatter.formatDouble6(sianAllevamentiVO.getLongitudine()));
	        htmpl.set("blkSiAllevamenti.blkDetenzione.blkElencoAllevamenti.latitudine", Formatter.formatDouble6(sianAllevamentiVO.getLatitudine()));
	      }
	    }
	    
	    if(vProprieta != null)
	    {
	      htmpl.newBlock("blkSiAllevamenti.blkProprieta");
	      for(int i=0;i<vProprieta.size();i++)
	      { 
	        htmpl.newBlock("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti");
	        SianAllevamentiVO sianAllevamentiVO = vProprieta.get(i);
	        
	        
	        //controllo se l'allevamento è stato selezionato (provengo dall'indietro)
          //nel qual caso selezioni il checkbox
          if (sianAllevamentiVO.isSelect())
            htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.checked", "checked=\"checked\"",null);
            
            
          htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.idAllevamento", sianAllevamentiVO.getAllevId().toString());
          
          if(Validator.isNotEmpty(sianAllevamentiVO.getDtFineAttivita())) 
            htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.disabled", "disabled=\"disabled\"",null);
          
          
          if(sianAllevamentiVO.isPresenteInAnagrafe())
            htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.isPresenteInAnagrafe", "(*)");
	        
	        
	         	                  
          
          htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.codiceAllevamento", sianAllevamentiVO.getAziendaCodice());
          htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.specie", sianAllevamentiVO.getDescrizioneSpecie());
          htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.indirizzo", sianAllevamentiVO.getIndirizzo());
          htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.cap", sianAllevamentiVO.getCap());
          htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.localita", sianAllevamentiVO.getLocalita());
          htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.comune", sianAllevamentiVO.getComune());
          htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.denominazione", sianAllevamentiVO.getDenominazione());
          if(Validator.isNotEmpty(sianAllevamentiVO.getCapiTotali())) {
            htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.numero", sianAllevamentiVO.getCapiTotali().toString());
          }
  
          if(Validator.isNotEmpty(sianAllevamentiVO.getDataCalcoloCapi())) {
            htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.dataCalcoloCapi", StringUtils.parseDateFieldToEuropeStandard("yyyy-MM-dd", "dd/MM/yyyy", sianAllevamentiVO.getDataCalcoloCapi()));
          }
  
          htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.tipoProduzione", sianAllevamentiVO.getDescTipoProduzione());
          htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.orientamentoProduttivo", sianAllevamentiVO.getDescOrientamentoProduttivo() );
          htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.autorizzazioneLatte", sianAllevamentiVO.getAutorizzazioneLatte());
          if(Validator.isNotEmpty(sianAllevamentiVO.getDtInizioAttivita())) {
            htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.inizioattivita", StringUtils.parseDateFieldToEuropeStandard("yyyy-MM-dd", "dd/MM/yyyy", sianAllevamentiVO.getDtInizioAttivita()));
          }
          if(Validator.isNotEmpty(sianAllevamentiVO.getDtFineAttivita())) {
            htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.fineAttivita", StringUtils.parseDateFieldToEuropeStandard("yyyy-MM-dd", "dd/MM/yyyy", sianAllevamentiVO.getDtFineAttivita()));
          }
          htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.cfProprietario", sianAllevamentiVO.getCodFiscaleProp());
          htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.nomeProprietario", sianAllevamentiVO.getDenomProprietario());
          htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.soccida", sianAllevamentiVO.getSoccida());
          htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.cfDetentore", sianAllevamentiVO.getCodFiscaleDeten());
          htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.nomeDetentore", sianAllevamentiVO.getDenomDetentore());
          if(Validator.isNotEmpty(sianAllevamentiVO.getDtInizioDetentore())) {
            htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.inizioDetentore", StringUtils.parseDateFieldToEuropeStandard("yyyy-MM-dd", "dd/MM/yyyy", sianAllevamentiVO.getDtInizioDetentore()));
          }
          if(Validator.isNotEmpty(sianAllevamentiVO.getDtFineDetentore())) {
            htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.fineDetentore", StringUtils.parseDateFieldToEuropeStandard("yyyy-MM-dd", "dd/MM/yyyy", sianAllevamentiVO.getDtFineDetentore()));
          }
          if(Validator.isNotEmpty(sianAllevamentiVO.getComune()))
          {
            if(Validator.isNotEmpty(sianAllevamentiVO.getSiglaProvincia()))
              htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.comuneProv", sianAllevamentiVO.getComune() + "("+ sianAllevamentiVO.getSiglaProvincia() +")");
            else  
              htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.comuneProv", sianAllevamentiVO.getComune());
          }
  
          htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.sez",sianAllevamentiVO.getSezione());
          htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.foglio", sianAllevamentiVO.getFoglioCatastale());
          htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.parti", sianAllevamentiVO.getParticella());
          htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.sub", sianAllevamentiVO.getSubalterno());
          htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.longitudine", Formatter.formatDouble6(sianAllevamentiVO.getLongitudine()));
          htmpl.set("blkSiAllevamenti.blkProprieta.blkElencoAllevamenti.latitudine", Formatter.formatDouble6(sianAllevamentiVO.getLatitudine()));
	      
	      }
	    }
	    
	    if(((vDetenzione != null) || (vProprieta != null))
	      && (request.getAttribute(SolmrConstants.MODIFICA_BDN) != null))
	    {
	      htmpl.newBlock("blkAnagrafeZootecnicaButtons");
	    }
	    
	    
    }
  }

  // SEZIONE ERRORI GENERICI DA IMPORTA DATI
  if(errors != null && errors.size() > 0) {
    HtmplUtil.setErrors(htmpl, errors, request, application);
  }

%>
<%= htmpl.text()%>
