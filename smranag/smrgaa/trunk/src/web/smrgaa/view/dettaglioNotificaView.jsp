<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.math.BigDecimal" %>
<%@page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  // Creo la pagina
  java.io.InputStream layout = application.getResourceAsStream("/layout/dettaglioNotifica.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
    <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Recupero gli oggetti che mi servono dalla request
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
 
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  NotificaVO dettaglioNotificaVO = (NotificaVO)request.getAttribute("dettaglioNotificaVO");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  ConsistenzaVO consistenzaVO = (ConsistenzaVO)request.getAttribute("consistenzaVO");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  // Setto i valori della view
  //HtmplUtil.setValues(htmpl, anagAziendaVO);
  htmpl.set("descTipologiaNotifica", dettaglioNotificaVO.getDescTipologiaNotifica());
  htmpl.set("descCategoriaNotifica", dettaglioNotificaVO.getDescCategoriaNotifica());
  htmpl.set("strDataInserimento", dettaglioNotificaVO.getStrDataInserimento());
 
  //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
  //al ruolo!
  ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
    "ultimaModificaVw", null,dettaglioNotificaVO.getDenominazioneUtenteInserimento(),
    dettaglioNotificaVO.getDescEnteAppartenenzaUtenteInserimento(), null);
    
  htmpl.set("descrizione", dettaglioNotificaVO.getDescrizione());
  
  if(Validator.isNotEmpty(consistenzaVO))
  {
    htmpl.newBlock("bloccoDichiarazione");
    htmpl.set("bloccoDichiarazione.dataDichiarazione", consistenzaVO.getData());
    htmpl.set("bloccoDichiarazione.numeroProtocollo", consistenzaVO.getNumeroProtocollo());
  }    

  htmpl.set("noteChiusura", dettaglioNotificaVO.getNoteChisura());
  htmpl.set("strDataChiusura", dettaglioNotificaVO.getStrDataChiusura());  
    
  ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
    "ultimaModificaChiusuraVw", null,dettaglioNotificaVO.getDenominazioneUtenteChiusura(),
    dettaglioNotificaVO.getDescEnteAppartenenzaUtenteChiusura(), null);
    
    
    
  if(dettaglioNotificaVO.getvAllegatoDocumento() != null)
  {
    for(int i=0;i<dettaglioNotificaVO.getvAllegatoDocumento().size();i++)
    {
      htmpl.newBlock("blkFileAllegato");
      htmpl.set("blkFileAllegato.idAllegato", 
        ""+dettaglioNotificaVO.getvAllegatoDocumento().get(i).getIdAllegato());
      htmpl.set("blkFileAllegato.titleAllegato", 
        dettaglioNotificaVO.getvAllegatoDocumento().get(i).getNomeLogico() +" (" +
        dettaglioNotificaVO.getvAllegatoDocumento().get(i).getNomeFisico()+")");
    }
  }
  
  
  if(Validator.isNotEmpty(dettaglioNotificaVO.getElencoParticelle())
        && (dettaglioNotificaVO.getElencoParticelle().size() > 0))
  {      
    if(dettaglioNotificaVO.getIdTipoEntita().compareTo(new Integer(SolmrConstants.TIPO_ENTITA_UV)) == 0)
    {  
	    htmpl.newBlock("blkUv");
	    for(int i = 0; i < dettaglioNotificaVO.getElencoParticelle().size(); i++) 
	    {
	      htmpl.newBlock("blkUv.blkElencoUv");
	      StoricoParticellaVO storicoParticellaElencoVO = dettaglioNotificaVO.getElencoParticelle().get(i);
	         
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
	      
	 
	      for(int j=0;j<dettaglioNotificaVO.getvNotificaEntita().size();j++)
        {
          if(storicoParticellaElencoVO.getStoricoUnitaArboreaVO()
                  .getIdUnitaArborea().compareTo(dettaglioNotificaVO.getvNotificaEntita().get(j).getIdentificativo()) == 0)
          {	
            NotificaEntitaVO notificaEntitaVO = dettaglioNotificaVO.getvNotificaEntita().get(j);       
			      htmpl.set("blkUv.blkElencoUv.noteUv", notificaEntitaVO.getNote());
			      htmpl.set("blkUv.blkElencoUv.noteChiusuraEntita", notificaEntitaVO.getNoteChiusuraEntita());
			      htmpl.set("blkUv.blkElencoUv.dataChiusura", DateUtils.formatDateTimeNotNull(notificaEntitaVO.getDataFineValidita()));
			      
			      ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
              "blkUv.blkElencoUv.denomUtente", null,notificaEntitaVO.getDenUtente(),
              notificaEntitaVO.getDenEnteUtente(), null);
			    }
			  }
	    }     
    }
    else
    {
      htmpl.newBlock("blkParticelle");
      for(int i = 0; i < dettaglioNotificaVO.getElencoParticelle().size(); i++) 
	    {
	      htmpl.newBlock("blkParticelle.blkElencoParticelle");
	      htmpl.newBlock("blkParticelle.blkElencoParticelle.blkChiaveParticelle");
	      StoricoParticellaVO storicoParticellaElencoVO = dettaglioNotificaVO.getElencoParticelle().get(i);
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
	      
	      for(int j=0;j<dettaglioNotificaVO.getvNotificaEntita().size();j++)
        {
          if(storicoParticellaElencoVO.getIdParticella()
            .compareTo(dettaglioNotificaVO.getvNotificaEntita().get(j).getIdentificativo()) == 0)
          {
	          NotificaEntitaVO notificaEntitaVO = dettaglioNotificaVO.getvNotificaEntita().get(j);
				    htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.noteParticelle", notificaEntitaVO.getNote());
			      htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.noteChiusuraEntita", notificaEntitaVO.getNoteChiusuraEntita());
			      htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.dataChiusura", DateUtils.formatDateTimeNotNull(notificaEntitaVO.getDataFineValidita()));
			      
			      ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
            "blkParticelle.blkElencoParticelle.blkChiaveParticelle.denomUtente", null,notificaEntitaVO.getDenUtente(),
              notificaEntitaVO.getDenEnteUtente(), null);
              
			      break;
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
  
  

  // Sezione relativa ai messaggi di errore
  HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>
