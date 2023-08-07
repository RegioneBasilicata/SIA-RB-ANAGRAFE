<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.sian.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.sian.SianAnagTributariaVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%
java.io.InputStream layout = application.getResourceAsStream("/layout/contitolariAT.htm");
Htmpl htmpl = new Htmpl(layout);


%>
   <%@include file = "/view/remoteInclude.inc" %>
<%

	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	PersonaFisicaVO personaFisicaVO = (PersonaFisicaVO)request.getAttribute("personaFisicaVO");
	SianAnagTributariaVO anagTrib = (SianAnagTributariaVO)session.getAttribute("anagTrib");
	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
	
	// Nuova gestione fogli di stile
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);


	// Settaggio parametri hidden
	//HtmplUtil.setValues(htmpl, anagAziendaVO);
	//HtmplUtil.setValues(htmpl, profile);
	
	
	
	
	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
	if(Validator.isNotEmpty(messaggioErrore)) 
	{
	  htmpl.newBlock("blkErrore");
	  htmpl.set("blkErrore.messaggio", messaggioErrore);
	}
	else 
	{  
	  if(anagTrib.getMessaggioErrore() !=null) 
	  {
	    htmpl.newBlock("blkErrore");
	    htmpl.set("blkErrore.messaggio", (String)AnagErrors.get("ERR_SERVIZIO_SIAN_ERRATO")+ anagTrib.getMessaggioErrore());
	  }
	  else
	  {
	    HtmplUtil.setErrors(htmpl, errors, request, application);
	    if(anagTrib != null)
	    {
	      htmpl.newBlock("blkSoggetto");
	      htmpl.set("blkSoggetto.codFiscale", anagTrib.getCodiceFiscale());
	      htmpl.set("blkSoggetto.partitaIva", anagTrib.getPartitaIva());
	      htmpl.set("blkSoggetto.cognome", anagTrib.getCognome());
	      htmpl.set("blkSoggetto.nome", anagTrib.getNome());
	
	      String comuneNascita = anagTrib.getComuneNascita();
	      if (comuneNascita==null) comuneNascita="";
	      String provinciaNascita = anagTrib.getProvinciaNascita();
	      if (provinciaNascita==null) provinciaNascita="";
	      htmpl.set("blkSoggetto.luogoNascita",comuneNascita +" ("+provinciaNascita+")");
	
	      if(Validator.isNotEmpty(anagTrib.getDataNascita()))
	        htmpl.set("blkSoggetto.dataNascita", anagTrib.getDataNascita().substring(0,10));
	      if(Validator.isNotEmpty(anagTrib.getDataDecesso())) {
          htmpl.set("blkSoggetto.dataDecesso", anagTrib.getDataDecesso().substring(0, 10));
        }
	      htmpl.set("blkSoggetto.sesso", anagTrib.getSesso());
	      htmpl.set("blkSoggetto.denominazione", anagTrib.getDenominazione());
	      
	      htmpl.set("blkSoggetto.codiceAteco", anagTrib.getCodiceAteco());
        htmpl.set("blkSoggetto.descAttivitaAteco", anagTrib.getDescAttivitaAteco());
	
	      //Dati sulla residenza
	      htmpl.set("blkSoggetto.indirizzoResidenza", anagTrib.getIndirizzoResidenza());
	
	      String comuneResidenza = anagTrib.getComuneResidenza();
	      if (comuneResidenza==null) comuneResidenza="";
	      String provinciaResidenza = anagTrib.getProvinciaResidenza();
	      if (provinciaResidenza==null) provinciaResidenza="";
	      htmpl.set("blkSoggetto.luogoResidenza",comuneResidenza +" ("+provinciaResidenza+")");
	
	      htmpl.set("blkSoggetto.capResidenza", anagTrib.getCapResidenza());
	
	      //Sede Legale
	      if (anagTrib.getComuneSedeLegale()!=null)
	      {
	        htmpl.newBlock("blkSoggetto.blkSedeLegale");
	
	        htmpl.set("blkSoggetto.blkSedeLegale.indirizzoSedeLegale", anagTrib.getIndirizzoSedeLegale());
	
	        String comuneSedeLegale = anagTrib.getComuneSedeLegale();
	        if (comuneSedeLegale==null) comuneSedeLegale="";
	        String provinciaSedeLegale = anagTrib.getProvinciaSedeLegale();
	        if (provinciaSedeLegale==null) provinciaSedeLegale="";
	        htmpl.set("blkSoggetto.blkSedeLegale.comuneSedeLegale",comuneSedeLegale +" ("+provinciaSedeLegale+")");
	
	        htmpl.set("blkSoggetto.blkSedeLegale.capSedeLegale", anagTrib.getCapSedeLegale());
	      }
	      //Domicilio fiscale
	      if (anagTrib.getComuneDomicilioFiscale()!=null)
	      {
	        htmpl.newBlock("blkSoggetto.blkDomicilioFiscale");
	
	        htmpl.set("blkSoggetto.blkDomicilioFiscale.indirizzoDomicilioFiscale", anagTrib.getIndirizzoDomicilioFiscale());
	
	        String comuneDomicilioFiscale = anagTrib.getComuneDomicilioFiscale();
	        if (comuneDomicilioFiscale==null) comuneDomicilioFiscale="";
	        String provinciaDomicilioFiscale = anagTrib.getProvinciaDomicilioFiscale();
	        if (provinciaDomicilioFiscale==null) provinciaDomicilioFiscale="";
	        htmpl.set("blkSoggetto.blkDomicilioFiscale.comuneDomicilioFiscale",comuneDomicilioFiscale +" ("+provinciaDomicilioFiscale+")");
	
	        htmpl.set("blkSoggetto.blkDomicilioFiscale.capDomicilioFiscale", anagTrib.getCapDomicilioFiscale());
	      }
	      //Società rappresentate
	      /*ISATSocietaRappresentata soc[]=anagTrib.getSocietaRappresentate();
	      if (soc!=null && soc.length>0)
	      {
	        htmpl.newBlock("blkSoggetto.blkSocietaRappresentate");
	        for (int i=0;i<soc.length;i++)
	        {
	          htmpl.newBlock("blkSoggetto.blkSocietaRappresentate.blkSocietaRappresentata");
	          htmpl.set("blkSoggetto.blkSocietaRappresentate.blkSocietaRappresentata.codiceFiscaleRappresentato", soc[i].getCodiceFiscaleRappresentato());
	          htmpl.set("blkSoggetto.blkSocietaRappresentate.blkSocietaRappresentata.decoTipoRapp", soc[i].getDecoTipoRapp());
	          if(Validator.isNotEmpty(soc[i].getDataInizio()))
	            htmpl.set("blkSoggetto.blkSocietaRappresentate.blkSocietaRappresentata.dataInizioSocRap", soc[i].getDataInizio().substring(0,10));
	          if(Validator.isNotEmpty(soc[i].getDataFine()))
	          {
	            if (SolmrConstants.SIAN_DATA_NULL.equals(soc[i].getDataFine().substring(0,10)))
	              htmpl.set("blkSoggetto.blkSocietaRappresentate.blkSocietaRappresentata.dataFineSocRap","" );
	            else
	              htmpl.set("blkSoggetto.blkSocietaRappresentate.blkSocietaRappresentata.dataFineSocRap", soc[i].getDataFine().substring(0,10));
	          }
	        }
	      }*/
	
	      //Codici fiscali collegati
	      /* if (iSATDett.getCfCollegati()!= null)
	       {
	         CUAAType[] cuaaType= iSATDett.getCfCollegati();
	         if (cuaaType!=null && cuaaType.length!=0)
	         {
	           htmpl.newBlock("blkSoggetto.blkCodiciFiscaleCollegati");
	
	           for(int i=0;i<cuaaType.length;i++)
	           {
	             htmpl.newBlock("blkSoggetto.blkCodiciFiscaleCollegati.blkCodiceFiscaleCollegato");
	             htmpl.set("blkSoggetto.blkCodiciFiscaleCollegati.blkCodiceFiscaleCollegato.codPersonaFisica",cuaaType[i].getCodiceFiscalePersonaFisica() );
	             htmpl.set("blkSoggetto.blkCodiciFiscaleCollegati.blkCodiceFiscaleCollegato.codPersonaGiuridica",cuaaType[i].getCodiceFiscalePersonaGiuridica());
	           }
	         }
	       }*/
	       
	       
	       //Partite Iva Attribuite
	       /*if (iSATDett.getPartiteIvaAttribuite()!= null)
	       {
	         ISATPartitaIvaAttribuita[] partitaIvaAttribuita = iSATDett.getPartiteIvaAttribuite();
	         if (partitaIvaAttribuita!=null && partitaIvaAttribuita.length>0)
	         {
	           htmpl.newBlock("blkSoggetto.blkPartitaIvaAttribuite");
	
	           for(int i=0;i<partitaIvaAttribuita.length;i++)
	           {
	             htmpl.newBlock("blkSoggetto.blkPartitaIvaAttribuite.blkElencoPartitaIvaAttribuite");
	             if(partitaIvaAttribuita[i].getPartitaIva() != null)
	             {
	                htmpl.set("blkSoggetto.blkPartitaIvaAttribuite.blkElencoPartitaIvaAttribuite.partitaIva",
	                  partitaIvaAttribuita[i].getPartitaIva());
	             }
	             if(partitaIvaAttribuita[i].getDataFine() != null)
	             {
	                htmpl.set("blkSoggetto.blkPartitaIvaAttribuite.blkElencoPartitaIvaAttribuite.dataFine",
	                  partitaIvaAttribuita[i].getDataFine().substring(0,10));
	             }
	             if(partitaIvaAttribuita[i].getPartitaIvaConfluenza() != null)
	             {
	                htmpl.set("blkSoggetto.blkPartitaIvaAttribuite.blkElencoPartitaIvaAttribuite.partitaIvaConfluenza",
	                  partitaIvaAttribuita[i].getPartitaIvaConfluenza());
	             }     
	             
	           }
	         }
	       }*/
	       
	       
	       
	      //Variazioni di residenza
	
	      /*ISATResidenzaVariata residenza[]=iSATDett.getResidenzeVariate();
	      if (residenza!=null && residenza.length>0)
	      {
	        htmpl.newBlock("blkSoggetto.blkVariazioniResidenza");
	        for (int i=0;i<residenza.length;i++)
	        {
	          htmpl.newBlock("blkSoggetto.blkVariazioniResidenza.blkVariazioneResidenza");
	
	          String comune=residenza[i].getComuneResidenza();
	          if (comune==null) comune="";
	          String provincia=residenza[i].getProvinciaResidenza();
	          if (provincia==null) provincia="";
	          htmpl.set("blkSoggetto.blkVariazioniResidenza.blkVariazioneResidenza.comuneVariazioneResidenza",comune +" ("+provincia+")");
	          htmpl.set("blkSoggetto.blkVariazioniResidenza.blkVariazioneResidenza.indirizzoVariazioneResidenza",residenza[i].getIndirizzoResidenza());
	          htmpl.set("blkSoggetto.blkVariazioniResidenza.blkVariazioneResidenza.capVariazioneResidenza",residenza[i].getCapResidenza());
	
	          if(Validator.isNotEmpty(residenza[i].getDataInizio()))
	            htmpl.set("blkSoggetto.blkVariazioniResidenza.blkVariazioneResidenza.dataInizioVariazioneResidenza",residenza[i].getDataInizio().substring(0,10));
	          if(Validator.isNotEmpty(residenza[i].getDataFine()))
	            htmpl.set("blkSoggetto.blkVariazioniResidenza.blkVariazioneResidenza.dataFineVariazioneResidenza",residenza[i].getDataFine().substring(0,10));
	        }
	      }*/
	
	     }
	   }
	}
%>
<%= htmpl.text()%>
