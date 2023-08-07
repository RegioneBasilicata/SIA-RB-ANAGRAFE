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

  java.io.InputStream layout = application.getResourceAsStream("/layout/chiusuraNotifica.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
    <%@include file = "/view/remoteInclude.inc" %>
  <%

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  NotificaVO chiusuraNotificaVO = (NotificaVO)request.getAttribute("chiusuraNotificaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  Vector<ValidationErrors> elencoErroriNote = (Vector<ValidationErrors>)request.getAttribute("elencoErroriNote");
  
  String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                      "title=\"{2}\" border=\"0\"></a>";
  String imko = "ko.gif";
  StringProcessor jssp = new JavaScriptStringProcessor(); 

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  //HtmplUtil.setValues(htmpl, chiusuraNotificaVO);
  //HtmplUtil.setValues(htmpl, anagAziendaVO);
  htmpl.set("idNotifica", ""+chiusuraNotificaVO.getIdNotifica());
  htmpl.set("descTipologiaNotifica", chiusuraNotificaVO.getDescTipologiaNotifica());
  htmpl.set("descCategoriaNotifica", chiusuraNotificaVO.getDescCategoriaNotifica());
  htmpl.set("strDataInserimento", chiusuraNotificaVO.getStrDataInserimento());
  
  //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
  //al ruolo!
  ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
    "ultimaModificaVw", null,chiusuraNotificaVO.getDenominazioneUtenteInserimento(),
    chiusuraNotificaVO.getDescEnteAppartenenzaUtenteInserimento(), null);
    
  htmpl.set("descrizione", chiusuraNotificaVO.getDescrizione());
  
  if(chiusuraNotificaVO != null) 
  {
    if(Validator.isNotEmpty(chiusuraNotificaVO.getNoteChisura())) 
    {
      htmpl.set("noteChiusura", chiusuraNotificaVO.getNoteChisura());
    }
  }

  String paginaChiamante = (String)request.getAttribute("paginaChiamante");
  htmpl.set("paginaChiamante", paginaChiamante);
  
  
  
  Vector<StoricoParticellaVO> elencoParticelle = chiusuraNotificaVO.getElencoParticelle();
  if(Validator.isNotEmpty(elencoParticelle)
    && (elencoParticelle.size() > 0))
  {
    request.setAttribute("elencoParticelle", elencoParticelle);
	  if(Validator.isNotEmpty(chiusuraNotificaVO.getIdTipoEntita()) 
	    && SolmrConstants.TIPO_ENTITA_UV.equalsIgnoreCase(chiusuraNotificaVO.getIdTipoEntita().toString()))
	  {
	    String[] arrNoteChiusuraUV = request.getParameterValues("noteChiusuraUV");
  
	    htmpl.newBlock("blkUv");
	    for(int i = 0; i < elencoParticelle.size(); i++) 
	    {
	      htmpl.newBlock("blkUv.blkElencoUv");
	      StoricoParticellaVO storicoParticellaElencoVO = elencoParticelle.get(i);
	      
	      htmpl.set("blkUv.blkElencoUv.idStoricoUnitaArborea", ""+storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getIdStoricoUnitaArborea());
	      String[] idStoricoUnitaArborea = request.getParameterValues("idStoricoUnitaArborea");
	      if(Validator.isNotEmpty(idStoricoUnitaArborea))
	      {
		      for(int j=0;j<idStoricoUnitaArborea.length;j++)
		      {
		        if(storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getIdStoricoUnitaArborea()
		          .toString().equalsIgnoreCase(idStoricoUnitaArborea[j]))
		        {
		          htmpl.set("blkUv.blkElencoUv.checkedStorico", "checked=\"true\"", null);    
		        }
		      }
		    }
	      
	      
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
	        
	      if(Validator.isNotEmpty(arrNoteChiusuraUV)
	        && Validator.isNotEmpty(arrNoteChiusuraUV[i]))
	      {
	        htmpl.set("blkUv.blkElencoUv.noteChiusuraUV", arrNoteChiusuraUV[i]);
	      }
	      
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
	      
	        
	      
	      
	      
	      // GESTIONE ERRORI Note
	      if(elencoErroriNote != null && elencoErroriNote.size() > 0) 
	      {
	        ValidationErrors errorsNote = (ValidationErrors)elencoErroriNote.elementAt(i);
	        if(errorsNote != null && errorsNote.size() > 0) 
	        {
	          Iterator<String> iter = htmpl.getVariableIterator();
	          while(iter.hasNext()) {
	            String key = (String)iter.next();
	              if(key.startsWith("err_")) 
	              {
	                String property = key.substring(4);
	                Iterator<ValidationError> errorIterator = errorsNote.get(property);
	                if(errorIterator != null) 
	                {
	                  ValidationError error = (ValidationError)errorIterator.next();
	                  htmpl.set("blkUv.blkElencoUv.err_"+property,
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
      
    }
    else if(Validator.isNotEmpty(chiusuraNotificaVO.getIdTipoEntita()) 
      && SolmrConstants.TIPO_ENTITA_PARTICELLE.equalsIgnoreCase(chiusuraNotificaVO.getIdTipoEntita().toString()))
    {
      String[] arrNoteChiusuraParticelle = request.getParameterValues("noteChiusuraParticelle");
     
      htmpl.newBlock("blkParticelle");
      
      for(int i = 0; i < elencoParticelle.size(); i++) 
      {
        htmpl.newBlock("blkParticelle.blkElencoParticelle");
        
        StoricoParticellaVO storicoParticellaElencoVO = elencoParticelle.get(i);
        
        htmpl.set("blkParticelle.blkElencoParticelle.idStoricoParticella", ""+storicoParticellaElencoVO.getIdStoricoParticella());
        String[] idStoricoParticella = request.getParameterValues("idStoricoParticella");
        if(Validator.isNotEmpty(idStoricoParticella))
        {
          for(int j=0;j<idStoricoParticella.length;j++)
          {
            if(storicoParticellaElencoVO.getIdStoricoParticella()
              .toString().equalsIgnoreCase(idStoricoParticella[j]))
            {
              htmpl.set("blkParticelle.blkElencoParticelle.checkedStorico", "checked=\"true\"", null);    
            }
          }
        }
        
        
        
        htmpl.newBlock("blkParticelle.blkElencoParticelle.blkChiaveParticelle");
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
        if(Validator.isNotEmpty(storicoParticellaElencoVO.getSezione())) 
        {
          htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.sezione", storicoParticellaElencoVO.getSezione().toUpperCase());
        }
        htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.foglio", storicoParticellaElencoVO.getFoglio());
        if(Validator.isNotEmpty(storicoParticellaElencoVO.getParticella())) 
        {
          htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.particella", storicoParticellaElencoVO.getParticella());
        }
        if(Validator.isNotEmpty(storicoParticellaElencoVO.getSubalterno())) 
        {
          htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.subalterno", storicoParticellaElencoVO.getSubalterno());
        }
         
         
        if(Validator.isNotEmpty(arrNoteChiusuraParticelle)
          && Validator.isNotEmpty(arrNoteChiusuraParticelle[i]))
        {
          htmpl.set("blkParticelle.blkElencoParticelle.blkChiaveParticelle.noteChiusuraParticelle", arrNoteChiusuraParticelle[i]);
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
         
           
         
         
         
        // GESTIONE ERRORI Note
        if(elencoErroriNote != null && elencoErroriNote.size() > 0) 
        {
          ValidationErrors errorsNote = (ValidationErrors)elencoErroriNote.elementAt(i);
          if(errorsNote != null && errorsNote.size() > 0) 
          {
            Iterator<String> iter = htmpl.getVariableIterator();
            while(iter.hasNext()) 
            {
              String key = (String)iter.next();
              if(key.startsWith("err_")) 
              {
                String property = key.substring(4);
                Iterator<ValidationError> errorIterator = errorsNote.get(property);
                if(errorIterator != null) 
                {
                  ValidationError error = (ValidationError)errorIterator.next();
                  htmpl.set("blkParticelle.blkElencoParticelle.err_"+property,
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
    
    
    }
  }
  
  
  
  

  // Sezione relativa ai messaggi di errore
  HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>
