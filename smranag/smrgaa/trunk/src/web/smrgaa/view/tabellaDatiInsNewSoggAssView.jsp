<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.*" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/tabellaDatiInsNewSoggAss.htm");

 	Htmpl htmpl = new Htmpl(layout);

  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  
  HashMap hElencoErrori = (HashMap)request.getAttribute("hElencoErrori");
  String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                          "title=\"{2}\" border=\"0\"></a>";
  String imko = "ko.gif";
  StringProcessor jssp = new JavaScriptStringProcessor();
  
  
  String pathErrori = null;
  String pathToFollow = (String)session.getAttribute("pathToFollow");
  if(pathToFollow.equalsIgnoreCase("rupar"))        
  {
    pathErrori = application.getInitParameter("erroriRupar");
  }
  else if(pathToFollow.equalsIgnoreCase("sispie"))
  {
    pathErrori = application.getInitParameter("erroriSispie");
  }
  else if(pathToFollow.equalsIgnoreCase("TOBECONFIG")){
	  pathErrori = application.getInitParameter("erroriTOBECONFIG");
  }
 
  
  try
  {
    ValidationErrors errors = null;
    Long idAziendaFigliaL = null;
    String idAziendaFiglia = request.getParameter("idAzienda");
    boolean inserimentoManuale = false;
    String cuaaIns = "";
    String partitaIvaIns = "";
    String denominazioneIns = "";
    String indirizzoIns = "";
    String comuneIns = "";
    String provinciaIns = "";
    String istatComuneIns = "";
    String capIns = "";
        
    if(Validator.isNotEmpty(idAziendaFiglia)) //Passaggio dal popup
    {    
      idAziendaFigliaL = new Long(idAziendaFiglia);
      htmpl.set("idAzienda", ""+idAziendaFigliaL);
    }
    else //non siamo passati attraverso il popup
    {
      errors = new AziendaCollegataVO().validateRicAziendeCollegateForSoci(request); //controllo correttezza cuaa
      if (errors != null) 
      {
        request.setAttribute("errors", errors);
      }
      else //il cuaa se inserito è corretto!!!
      {
        String cuaa = request.getParameter("cuaa");
        String partitaIva = request.getParameter("partitaIva");
        String denominazione = request.getParameter("denominazione");
        
        
        if(Validator.isNotEmpty(cuaa))
        {
          cuaa = cuaa.trim();
          cuaa = cuaa.toUpperCase();
        }        
        if(Validator.isNotEmpty(partitaIva))
        {
          partitaIva = partitaIva.trim();
          partitaIva = partitaIva.toUpperCase();
        }
        if(Validator.isNotEmpty(denominazione))
        {
          denominazione = denominazione.trim();
          denominazione = denominazione.toUpperCase();
        }
        
        
        AnagAziendaVO azVO = new AnagAziendaVO();
        //se sono valorizzati sia il cuaa e la denominazione 
        //uso solo il cuaa nella ricerca
        if(Validator.isNotEmpty(cuaa))
        {
          azVO.setCUAA(cuaa);
        }
        else if(Validator.isNotEmpty(partitaIva))
        {
          azVO.setPartitaIVA(partitaIva);
        }
        else if(Validator.isNotEmpty(denominazione))
        {
          azVO.setDenominazione(denominazione);
        }
        
        
        try
        {
          Vector<Long> vectIdAziendaTmp = anagFacadeClient.getListOfIdAzienda(azVO, null,true);
          if((vectIdAziendaTmp !=null) && (vectIdAziendaTmp.size() == 1))
          {           
            idAziendaFigliaL =  (Long)vectIdAziendaTmp.get(0);
          }
          else if((vectIdAziendaTmp == null) || ((vectIdAziendaTmp !=null) && (vectIdAziendaTmp.size() == 0)))
          {                
            //Caso inserimento a mano
            inserimentoManuale = true;
            //Non ho trovato aziende coi parametri di ricerca impostati
            //quindi devo settare in sessione una valore per
            //sbloccare l'inserimento a mano dell'azienda
            htmpl.set("cuaaIns",cuaa);
				    htmpl.set("denominazioneIns",denominazione);
				    htmpl.set("partitaIvaIns",partitaIva);
				    htmpl.set("disabledIndirizzo", "");
				    htmpl.set("disabledProvincia", "");
				    htmpl.set("disabledComune", "");
				    htmpl.set("disabledCAP", "");
				    htmpl.set("disabledCerca", "javascript: scegliComune('../view/sceltaComuneView.jsp','scegli_comune',"+
				      "'600','300','yes',document.form1.provinciaIns.value,document.form1.comuneIns.value,"+
				      "'newInserimentoAzAss');"); 
            
            //session.setAttribute("sbloccaSociInsAMano","true"); 
          }
          else
          {
            if(errors == null)
            {
              errors = new ValidationErrors();
            }
            errors.add("inserisciAzienda", new ValidationError(AnagErrors.ERRORE_TROPPEAZIENDE_TROVATE));
          }
        }
        catch (SolmrException se) //Spero che sia eccezzione di max numero record trovati! 
        {
           
          if(errors == null)
          {
            errors = new ValidationErrors();
          }
          errors.add("inserisciAzienda", new ValidationError(AnagErrors.ERRORE_TROPPEAZIENDE_TROVATE));
        }
      }
    }
    
    AziendaCollegataVO azzCollVO = null;
    
    if(inserimentoManuale)
    {
      SoggettoAssociatoVO soggAssVO = new SoggettoAssociatoVO();      
      soggAssVO.setCuaa(request.getParameter("cuaa"));
      soggAssVO.setDenominazione(request.getParameter("denominazione"));
      soggAssVO.setPartitaIva(request.getParameter("partitaIva"));
      String indirizzo = request.getParameter("indirizzoIns");
      if(Validator.isNotEmpty(indirizzo))
      {
        indirizzoIns = indirizzo.trim();
      }
      soggAssVO.setIndirizzo(indirizzoIns);
      String comuneDesc =  request.getParameter("comuneIns");
      String provinciaDesc =  request.getParameter("provinciaIns");
      String istatComuneDesc = request.getParameter("istatComuneIns");
      String capDesc = request.getParameter("capIns");
      if(Validator.isNotEmpty(comuneDesc))
      {
        comuneDesc = comuneDesc.trim();
      }
      soggAssVO.setDenominazioneComune(comuneDesc);
      if(Validator.isNotEmpty(provinciaDesc))
      {
        provinciaDesc = provinciaDesc.trim();
      }
      soggAssVO.setSglProv(provinciaDesc);
      soggAssVO.setComune(istatComuneDesc);
      if(Validator.isNotEmpty(capDesc))
      {
        capDesc = capDesc.trim();
      }
      soggAssVO.setCap(capDesc);
      errors = soggAssVO.validateInserimento(errors);
      
      
      if(errors == null)
      {    
        //Non inserito inserito il comune da pop Up            
        if(Validator.isNotEmpty(comuneDesc) && Validator.isNotEmpty(provinciaDesc))
        { 
          //caso in cui inseriti provincia e comune a mano
          //String istatComune = null;
          try 
          {
            istatComuneDesc = anagFacadeClient.ricercaCodiceComuneNonEstinto(comuneDesc, provinciaDesc);
            soggAssVO.setComune(istatComuneDesc);
            if(Validator.isNotEmpty(istatComuneDesc))
            {
              ComuneVO comVO = anagFacadeClient.getComuneByISTAT(istatComuneDesc);                  
              if(!capDesc.equalsIgnoreCase(comVO.getCap()))
              {
                errors = ErrorUtils.setValidErrNoNull(errors, "capIns", AnagErrors.ERRORE_CAP_NON_COERENTE);
              }
            } 
          }
          catch(SolmrException se) 
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "comuneIns", se.getMessage());
          }        
        }
        else
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "comuneIns", AnagErrors.ERRORE_NO_COMUNE_PROVINCIA);
          errors = ErrorUtils.setValidErrNoNull(errors, "provinciaIns", AnagErrors.ERRORE_NO_COMUNE_PROVINCIA);
        }
        
      }
      
      if(errors == null)
      {            
        cuaaIns = soggAssVO.getCuaa();
	      denominazioneIns = soggAssVO.getDenominazione();
	      partitaIvaIns = soggAssVO.getPartitaIva();
	      indirizzoIns = soggAssVO.getIndirizzo();
	      comuneIns =  soggAssVO.getDenominazioneComune();
        provinciaIns =  soggAssVO.getSglProv();
        istatComuneIns = soggAssVO.getComune();
        capIns = soggAssVO.getCap();
      }
      
    }
    
    
    
    if(errors == null)
    {
      /*htmpl.set("disabledIndirizzo", "disabled=true");
	    htmpl.set("disabledProvincia", "disabled=true");
	    htmpl.set("disabledComune", "disabled=true");
	    htmpl.set("disabledCAP", "disabled=true");
	    htmpl.set("disabledCerca", "#");*/
	    if(inserimentoManuale)
	    {
	      htmpl.set("cuaaIns", cuaaIns);
	      htmpl.set("partitaIvaIns", partitaIvaIns);
        htmpl.set("denominazioneIns", denominazioneIns);
        htmpl.set("indirizzoIns", indirizzoIns);
        htmpl.set("comuneIns", comuneIns);
        htmpl.set("provinciaIns", provinciaIns);
        htmpl.set("istatComuneIns", istatComuneIns);
        htmpl.set("capIns", capIns);
	    }
	    //inserimento riconosciuto...
	    else
	    {
	       htmpl.set("idAzienda", ""+idAziendaFigliaL);
	       htmpl.set("disabledIndirizzo", "disabled=true");
	       htmpl.set("disabledProvincia", "disabled=true");
	       htmpl.set("disabledComune", "disabled=true");
	       htmpl.set("disabledCAP", "disabled=true");
	       htmpl.set("disabledCerca", "");
	    }
	    
	    
	    htmpl.set("erroreIns", "false");        
    }
    else
    {
      htmpl.set("erroreIns", "true");  
      if(errors.size() > 0) 
		  {
		    Iterator iter = htmpl.getVariableIterator();
		    while(iter.hasNext()) 
		    {
		      String key = (String)iter.next();
		      if(key.startsWith("err_")) 
		      {
		        String property = key.substring(4);
		        Iterator errorIterator = errors.get(property);
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
    
    
    
    }
	  
		  
	}
	catch(Throwable ex)
	{
	  htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex)); 	
	}
    
  
	
 	

%>
<%= htmpl.text()%>
