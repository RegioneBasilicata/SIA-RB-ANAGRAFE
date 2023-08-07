<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="java.math.BigDecimal" %>
<%@page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<%

  // Creo la pagina
  java.io.InputStream layout = application.getResourceAsStream("/layout/ricercaDettaglioNotifica.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
    <%@include file = "/view/remoteInclude.inc" %>
  <%

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  // Recupero gli oggetti che mi servono dalla request
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  NotificaVO dettaglioRicercaNotificaVO = (NotificaVO)request.getAttribute("dettaglioRicercaNotificaVO");
  ConsistenzaVO consistenzaVO = (ConsistenzaVO)request.getAttribute("consistenzaVO");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);


  // Setto i valori della view
  htmpl.set("primaryKey", dettaglioRicercaNotificaVO.getIdNotifica()+"/"+dettaglioRicercaNotificaVO.getIdAnagraficaAzienda());
 
  Boolean storico = (Boolean) request.getAttribute("storico");
  if (Validator.isNotEmpty(storico))
  {
    if (storico.booleanValue())
    {
      htmpl.set("storico", "true");
    }
  }
 
 
  // Setto i valori della view
  htmpl.set("descTipologiaNotifica", dettaglioRicercaNotificaVO.getDescTipologiaNotifica());
  htmpl.set("descCategoriaNotifica", dettaglioRicercaNotificaVO.getDescCategoriaNotifica());
  htmpl.set("strDataInserimento", dettaglioRicercaNotificaVO.getStrDataInserimento());
 
  //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
  //al ruolo!
  ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
    "ultimaModificaVw", null,dettaglioRicercaNotificaVO.getDenominazioneUtenteInserimento(),
    dettaglioRicercaNotificaVO.getDescEnteAppartenenzaUtenteInserimento(), null);
    
  htmpl.set("descrizione", dettaglioRicercaNotificaVO.getDescrizione());
  
  if(Validator.isNotEmpty(consistenzaVO))
  {
    htmpl.newBlock("bloccoDichiarazione");
    htmpl.set("bloccoDichiarazione.dataDichiarazione", consistenzaVO.getData());
    htmpl.set("bloccoDichiarazione.numeroProtocollo", consistenzaVO.getNumeroProtocollo());
  }      

  htmpl.set("noteChiusura", dettaglioRicercaNotificaVO.getNoteChisura());
  htmpl.set("strDataChiusura", dettaglioRicercaNotificaVO.getStrDataChiusura());  
    
  ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
    "ultimaModificaChiusuraVw", null,dettaglioRicercaNotificaVO.getDenominazioneUtenteChiusura(),
    dettaglioRicercaNotificaVO.getDescEnteAppartenenzaUtenteChiusura(), null);
    
  if(dettaglioRicercaNotificaVO.getvAllegatoDocumento() != null)
  {
    for(int i=0;i<dettaglioRicercaNotificaVO.getvAllegatoDocumento().size();i++)
    {
      htmpl.newBlock("blkFileAllegato");
      htmpl.set("blkFileAllegato.idAllegato", 
        ""+dettaglioRicercaNotificaVO.getvAllegatoDocumento().get(i).getIdAllegato());
      htmpl.set("blkFileAllegato.titleAllegato", 
        dettaglioRicercaNotificaVO.getvAllegatoDocumento().get(i).getNomeLogico() +" (" +
        dettaglioRicercaNotificaVO.getvAllegatoDocumento().get(i).getNomeFisico()+")");
    }
  }
  
  if(Validator.isNotEmpty(dettaglioRicercaNotificaVO.getElencoParticelle())
        && (dettaglioRicercaNotificaVO.getElencoParticelle().size() > 0))
  {
    if(dettaglioRicercaNotificaVO.getIdTipoEntita().compareTo(new Integer(SolmrConstants.TIPO_ENTITA_UV)) == 0)
    { 
	    htmpl.newBlock("blkUv");
	    for(int i = 0; i < dettaglioRicercaNotificaVO.getElencoParticelle().size(); i++) 
	    {
	      htmpl.newBlock("blkUv.blkElencoUv");
	      StoricoParticellaVO storicoParticellaElencoVO = dettaglioRicercaNotificaVO.getElencoParticelle().get(i);
	         
	      htmpl.set("blkUv.blkElencoUv.idStoricoUnitaArborea", ""+storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getIdStoricoUnitaArborea());
	      
	      htmpl.set("blkUv.blkElencoUv.progrUnar", storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getProgrUnar());
	      
	      if(Validator.isNotEmpty(storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getTipoUtilizzoVO()))
	        htmpl.set("blkUv.blkElencoUv.destProd", "["+storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getTipoUtilizzoVO().getCodice()+"] "+
	          storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getTipoUtilizzoVO().getDescrizione());
	      if(Validator.isNotEmpty(storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getTipoVarietaVO()))
	        htmpl.set("blkUv.blkElencoUv.descVitigno", "["+storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getTipoVarietaVO().getCodiceVarieta()+"] "+
	          storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getTipoVarietaVO().getDescrizione());
	      htmpl.set("blkUv.blkElencoUv.annoImpianto", 
	          storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getAnnoImpianto());
	      if(Validator.isNotEmpty(storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getTipoTipologiaVinoVO()))
	        htmpl.set("blkUv.blkElencoUv.descIdoneita", 
	          storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getTipoTipologiaVinoVO().getDescrizione());
	      htmpl.set("blkUv.blkElencoUv.annoIdoneita", 
	          storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getAnnoIscrizioneAlbo());      
	      htmpl.set("blkUv.blkElencoUv.supVitata", StringUtils.parseSuperficieField(
	        storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getArea()));
	      
	      htmpl.set("blkUv.blkElencoUv.descComuneParticella", storicoParticellaElencoVO.getComuneParticellaVO().getDescom());
	      htmpl.set("blkUv.blkElencoUv.siglaProvinciaParticella", storicoParticellaElencoVO.getComuneParticellaVO().getSiglaProv());
	      if(Validator.isNotEmpty(storicoParticellaElencoVO.getSezione())) {
	        htmpl.set("blkUv.blkElencoUv.sezione", storicoParticellaElencoVO.getSezione().toUpperCase());
	      }
	      htmpl.set("blkUv.blkElencoUv.foglio", storicoParticellaElencoVO.getFoglio());
	      if(Validator.isNotEmpty(storicoParticellaElencoVO.getParticella())) {
	        htmpl.set("blkUv.blkElencoUv.particella", storicoParticellaElencoVO.getParticella());
	      }
	      if(Validator.isNotEmpty(storicoParticellaElencoVO.getSubalterno())) {
	        htmpl.set("blkUv.blkElencoUv.subalterno", storicoParticellaElencoVO.getSubalterno());
	      }
	      
	       
	      for(int j=0;j<dettaglioRicercaNotificaVO.getvNotificaEntita().size();j++)
        {
          if(storicoParticellaElencoVO.getStoricoUnitaArboreaVO()
                  .getIdUnitaArborea().compareTo(dettaglioRicercaNotificaVO.getvNotificaEntita().get(j).getIdentificativo()) == 0)
          { 
            NotificaEntitaVO notificaEntitaVO = dettaglioRicercaNotificaVO.getvNotificaEntita().get(j);       
            htmpl.set("blkUv.blkElencoUv.noteUv", notificaEntitaVO.getNote());
			      htmpl.set("blkUv.blkElencoUv.noteChiusuraEntita", notificaEntitaVO.getNoteChiusuraEntita());
			      htmpl.set("blkUv.blkElencoUv.dataChiusura", DateUtils.formatDateTimeNotNull(notificaEntitaVO.getDataFineValidita()));
			      htmpl.set("blkUv.blkElencoUv.denomUtente", notificaEntitaVO.getDenUtente()+"\n"
			        +notificaEntitaVO.getDenEnteUtente());
			    }
			  }
	      
	     
	    }
	  }
	  else
    {
      htmpl.newBlock("blkParticelle");
      for(int i = 0; i < dettaglioRicercaNotificaVO.getElencoParticelle().size(); i++) 
      {
        htmpl.newBlock("blkParticelle.blkElencoParticelle");
        htmpl.newBlock("blkParticelle.blkElencoParticelle.blkChiaveParticelle");
        StoricoParticellaVO storicoParticellaElencoVO = dettaglioRicercaNotificaVO.getElencoParticelle().get(i);
        int numRigheTot = 0;
        for(int j=0;j<storicoParticellaElencoVO.getvConduzioniDichiarate().size();j++)
        {
          ConduzioneDichiarataVO conduzioneDichiarataVO = storicoParticellaElencoVO.getvConduzioniDichiarate().get(j);
          if(Validator.isNotEmpty(conduzioneDichiarataVO.getvUtilizzi()))
          {
            numRigheTot = numRigheTot+conduzioneDichiarataVO.getvUtilizzi().size();
          }
          else
          {         
            numRigheTot = numRigheTot+1;
          }
        }
        
        htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.rowTot", ""+numRigheTot);      
        htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.idStoricoParticella", ""+storicoParticellaElencoVO.getIdStoricoParticella());
        htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.descComuneParticella", storicoParticellaElencoVO.getComuneParticellaVO().getDescom());
        htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.siglaProvinciaParticella", storicoParticellaElencoVO.getComuneParticellaVO().getSiglaProv());
        if(Validator.isNotEmpty(storicoParticellaElencoVO.getSezione())) {
          htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.sezione", storicoParticellaElencoVO.getSezione().toUpperCase());
        }
        htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.foglio", storicoParticellaElencoVO.getFoglio());
        if(Validator.isNotEmpty(storicoParticellaElencoVO.getParticella())) {
          htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.particella", storicoParticellaElencoVO.getParticella());
        }
        if(Validator.isNotEmpty(storicoParticellaElencoVO.getSubalterno())) {
          htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.subalterno", storicoParticellaElencoVO.getSubalterno());
        }
        
        for(int j=0;j<dettaglioRicercaNotificaVO.getvNotificaEntita().size();j++)
        {
          if(storicoParticellaElencoVO.getIdParticella()
            .compareTo(dettaglioRicercaNotificaVO.getvNotificaEntita().get(j).getIdentificativo()) == 0)
          {
            NotificaEntitaVO notificaEntitaVO = dettaglioRicercaNotificaVO.getvNotificaEntita().get(j);            
		        htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.noteParticelle", notificaEntitaVO.getNote());
		        htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.noteChiusuraEntita", notificaEntitaVO.getNoteChiusuraEntita());
		        htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.dataChiusura", DateUtils.formatDateTimeNotNull(notificaEntitaVO.getDataFineValidita()));
		        htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.denomUtente", notificaEntitaVO.getDenUtente()+"\n"
		          +notificaEntitaVO.getDenEnteUtente());
		      }
		    }
        
        for(int j=0;j<storicoParticellaElencoVO.getvConduzioniDichiarate().size();j++)
        {
          ConduzioneDichiarataVO conduzioneDichiarataVO = storicoParticellaElencoVO.getvConduzioniDichiarate().get(j);
          if(j==0)
          {
            int numRigheCond = 0;
            if(Validator.isNotEmpty(conduzioneDichiarataVO.getvUtilizzi()))
            {
              numRigheCond = conduzioneDichiarataVO.getvUtilizzi().size();
            }
            else
            {         
              numRigheCond = 1;
            }
            htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.rowCond", ""+numRigheCond);
            htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.idTitoloPosseso", ""+conduzioneDichiarataVO.getIdTitoloPossesso());
            BigDecimal percentualePossessoTmp = conduzioneDichiarataVO.getPercentualePossesso();
            if(percentualePossessoTmp.compareTo(new BigDecimal(1)) < 0)
            {
              percentualePossessoTmp = new BigDecimal(1);
            }  
            htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.percentualPossesso", Formatter.formatAndRoundBigDecimal0(percentualePossessoTmp));  
            if(Validator.isNotEmpty(conduzioneDichiarataVO.getvUtilizzi()))
            {
	            for(int z=0;z<conduzioneDichiarataVO.getvUtilizzi().size();z++)
	            {
	              UtilizzoDichiaratoVO utilizzoDichiaratoVO = conduzioneDichiarataVO.getvUtilizzi().get(z);
	              if(z==0)
	              {
	                String descTipologiaProduttiva = "["+utilizzoDichiaratoVO.getTipoUtilizzoVO().getCodice()
	                  +"] "+utilizzoDichiaratoVO.getTipoUtilizzoVO().getDescrizione()+"<br>"
	                  +"["+utilizzoDichiaratoVO.getTipoVarietaVO().getCodiceVarieta()
	                  +"]"+utilizzoDichiaratoVO.getTipoVarietaVO().getDescrizione();
	                htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.descTipologiaProduttiva", descTipologiaProduttiva, null);
	                htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.supUtilizzo", Formatter.formatDouble4(utilizzoDichiaratoVO.getSupUtilizzataBg()));
	              }
	              else
	              {
	                htmpl.newBlock("blkParticelle.blkElencoParticelle");
	                htmpl.newBlock("blkParticelle.blkElencoParticelle.blkChiaveUtilizzo");
	                String descTipologiaProduttiva = "["+utilizzoDichiaratoVO.getTipoUtilizzoVO().getCodice()
	                  +"] "+utilizzoDichiaratoVO.getTipoUtilizzoVO().getDescrizione()+"<br>"
	                  +"["+utilizzoDichiaratoVO.getTipoVarietaVO().getCodiceVarieta()
	                  +"]"+utilizzoDichiaratoVO.getTipoVarietaVO().getDescrizione();
	                htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveUtilizzo.descTipologiaProduttiva", descTipologiaProduttiva, null);
	                htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveUtilizzo.supUtilizzo", Formatter.formatDouble4(utilizzoDichiaratoVO.getSupUtilizzataBg()));
	              }
	            }
	          }
          }
          else
          {
            htmpl.newBlock("blkParticelle.blkElencoParticelle");
            htmpl.newBlock("blkParticelle.blkElencoParticelle.blkChiaveConduzione");
            int numRigheCond = 0;
            if(Validator.isNotEmpty(conduzioneDichiarataVO.getvUtilizzi()))
            {
              numRigheCond = conduzioneDichiarataVO.getvUtilizzi().size();
            }
            else
            {         
              numRigheCond = 1;
            }
            htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveConduzione.rowCond", ""+numRigheCond);
            htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveConduzione.idTitoloPosseso", ""+conduzioneDichiarataVO.getIdTitoloPossesso());
            BigDecimal percentualePossessoTmp = conduzioneDichiarataVO.getPercentualePossesso();
            if(percentualePossessoTmp.compareTo(new BigDecimal(1)) < 0)
            {
              percentualePossessoTmp = new BigDecimal(1);
            }  
            htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveConduzione.percentualPossesso", Formatter.formatAndRoundBigDecimal0(percentualePossessoTmp));  
            if(Validator.isNotEmpty(conduzioneDichiarataVO.getvUtilizzi()))
            {
	            for(int z=0;z<conduzioneDichiarataVO.getvUtilizzi().size();z++)
	            {
	              UtilizzoDichiaratoVO utilizzoDichiaratoVO = conduzioneDichiarataVO.getvUtilizzi().get(z);
	              if(z==0)
	              {
	                String descTipologiaProduttiva = "["+utilizzoDichiaratoVO.getTipoUtilizzoVO().getCodice()
	                  +"] "+utilizzoDichiaratoVO.getTipoUtilizzoVO().getDescrizione()+"<br>"
	                  +"["+utilizzoDichiaratoVO.getTipoVarietaVO().getCodiceVarieta()
	                  +"]"+utilizzoDichiaratoVO.getTipoVarietaVO().getDescrizione();
	                htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveConduzione.descTipologiaProduttiva", descTipologiaProduttiva, null);
	                htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveConduzione.supUtilizzo", Formatter.formatDouble4(utilizzoDichiaratoVO.getSupUtilizzataBg()));
	              }
	              else
	              {
	                htmpl.newBlock("blkParticelle.blkElencoParticelle");
	                htmpl.newBlock("blkParticelle.blkElencoParticelle.blkChiaveUtilizzo");
	                String descTipologiaProduttiva = "["+utilizzoDichiaratoVO.getTipoUtilizzoVO().getCodice()
	                  +"] "+utilizzoDichiaratoVO.getTipoUtilizzoVO().getDescrizione()+"<br>"
	                  +"["+utilizzoDichiaratoVO.getTipoVarietaVO().getCodiceVarieta()
	                  +"]"+utilizzoDichiaratoVO.getTipoVarietaVO().getDescrizione();
	                htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveUtilizzo.descTipologiaProduttiva", descTipologiaProduttiva, null);
	                htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveUtilizzo.supUtilizzo", Formatter.formatDouble4(utilizzoDichiaratoVO.getSupUtilizzataBg()));
	              }
	            }
	          }
          }
        
        }
      }     
    }   
      
    
  }
 

 

  // Sezione relativa ai messaggi di errore
  HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>
