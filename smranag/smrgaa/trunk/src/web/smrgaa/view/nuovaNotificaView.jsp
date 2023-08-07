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
<%@ page import="it.csi.smranag.smrgaa.util.PianoRiferimentoUtils" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.dto.anag.consistenza.TipoControlloVO"%>
<%@ page import="java.math.BigDecimal" %>
<%@page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<%

 java.io.InputStream layout = application.getResourceAsStream("/layout/nuovaNotifica.htm");
 Htmpl htmpl = new Htmpl(layout);

 %>
    <%@include file = "/view/remoteInclude.inc" %>
 <%

  SolmrLogger.debug(this, "-- BEGIN nuovaNotificaView");
 
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  NotificaVO nuovaNotificaVO = (NotificaVO)request.getAttribute("nuovaNotificaVO");  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  TipoUtilizzoVO[] elencoTipiUsoSuolo = (TipoUtilizzoVO[])request.getAttribute("elencoTipiUsoSuolo");
  TipoVarietaVO[] elencoVarieta = (TipoVarietaVO[])request.getAttribute("elencoVarieta");
  Vector<ComuneVO> elencoComuni = (Vector<ComuneVO>)request.getAttribute("elencoComuni");
  Vector<ProvinciaVO> elencoProvincie = (Vector<ProvinciaVO>)request.getAttribute("elencoProvincie");
  TipoTipologiaVinoVO[] elencoTipiTipologiaVino = (TipoTipologiaVinoVO[])request.getAttribute("elencoTipiTipologiaVino");
  Vector<StoricoParticellaVO> elencoParticelle = (Vector<StoricoParticellaVO>)session.getAttribute("elencoParticelle");
  Vector<ValidationErrors> elencoErroriNote = (Vector<ValidationErrors>)request.getAttribute("elencoErroriNote");
  Vector<TipoUtilizzoVO> elencoTipiUsoSuoloPart = (Vector<TipoUtilizzoVO>)request.getAttribute("elencoTipiUsoSuoloPart");
  TipoControlloVO[] elencoTipiControllo = (TipoControlloVO[])request.getAttribute("elencoTipiControllo");
  
  String operazione = request.getParameter("operazione");
  //Vector<CodeDescription> elencoCategoriaNotifica = (Vector<CodeDescription>)request.getAttribute("elencoCategoriaNotifica");
  String idCategoriaNotifica = request.getParameter("idCategoriaNotifica");
  String idTipologiaNotifica = request.getParameter("idTipologiaNotifica");
  String descrizione = request.getParameter("descrizione"); 
  String idTipoEntita = request.getParameter("idTipoEntita");
  String idTipoUtilizzo = request.getParameter("idTipoUtilizzo");
  String idAnomalie = request.getParameter("idAnomalie");
  
  
  // Creazione delle variabili necessarie alla gestione degli errori relativi
  // ai proprietari del documento
  String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                      "title=\"{2}\" border=\"0\"></a>";
  String imko = "ko.gif";
  StringProcessor jssp = new JavaScriptStringProcessor(); 

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  
  
  
  // Combo relativa ai tipi tipologia notifica
  Vector<CodeDescription> vTipologiaNotifica = (Vector<CodeDescription>)request.getAttribute("vTipologiaNotifica");
  SolmrLogger.error(this, "--  Caricamento combo Tipologia");
  if(vTipologiaNotifica != null)
  {
    for(int i = 0; i < vTipologiaNotifica.size(); i++) 
    {
      CodeDescription code = vTipologiaNotifica.get(i);
      htmpl.newBlock("blkTipologiaNotifica");  
      SolmrLogger.error(this, "-- idTipologiaNotifica ="+code.getCode().toString());
      htmpl.set("blkTipologiaNotifica.idTipologiaNotifica", code.getCode().toString());
      SolmrLogger.error(this, "-- descTipologiaNotifica ="+code.getDescription());
      htmpl.set("blkTipologiaNotifica.descTipologiaNotifica", code.getDescription());
      
      if(Validator.isNotEmpty(idTipologiaNotifica) 
        && (idTipologiaNotifica.equalsIgnoreCase(code.getCode().toString()))) 
      {
        htmpl.set("blkTipologiaNotifica.selected", "selected=\"selected\"", null);
      }
    }
  }
  
  // Combo Categoria
  Vector<TipoCategoriaNotificaVO> vCategoriaNotifica = (Vector<TipoCategoriaNotificaVO>)request.getAttribute("vCategoriaNotifica");
  SolmrLogger.error(this, "--  Caricamento combo Categoria");
  if(vCategoriaNotifica != null) 
  {
    for(int i=0; i<vCategoriaNotifica.size(); i++)
    {
      TipoCategoriaNotificaVO tipoCategoriaNotificaVO = vCategoriaNotifica.get(i);
      htmpl.newBlock("blkCategoriaCombo");
      SolmrLogger.error(this, "-- idCategoriaNotifica ="+tipoCategoriaNotificaVO.getIdCategoriaNotifica());
      htmpl.set("blkCategoriaCombo.idCategoriaNotifica", ""+tipoCategoriaNotificaVO.getIdCategoriaNotifica());
      SolmrLogger.error(this, "-- idTipologiaNotifica ="+tipoCategoriaNotificaVO.getIdTipologiaNotifica());
      htmpl.set("blkCategoriaCombo.idTipologiaNotifica", ""+tipoCategoriaNotificaVO.getIdTipologiaNotifica());
      SolmrLogger.error(this, "-- descCategoriaNotifica ="+tipoCategoriaNotificaVO.getDescrizione());
      htmpl.set("blkCategoriaCombo.descCategoriaNotifica", tipoCategoriaNotificaVO.getDescrizione());
      htmpl.set("blkCategoriaCombo.index", ""+i);
      
      if(Validator.isNotEmpty(idCategoriaNotifica) 
        && (idCategoriaNotifica.equalsIgnoreCase(tipoCategoriaNotificaVO.getIdCategoriaNotifica().toString()))) 
      {
    	SolmrLogger.error(this, "-- categoriaSel ="+tipoCategoriaNotificaVO.getIdCategoriaNotifica());
        htmpl.set("categoriaSel",""+tipoCategoriaNotificaVO.getIdCategoriaNotifica());        
      }
    }
  }
  
  
  //Cambiata categoria
  if(Validator.isNotEmpty(operazione)
    && "cambioCategoria".equalsIgnoreCase(operazione))
  {
    if(request.getAttribute("idTipoEntita") != null)
    {
      idTipoEntita = ((Integer)request.getAttribute("idTipoEntita")).toString();
    }
    else
    {
      idTipoEntita = "";
    }
  }
  else
  {
    idTipoEntita = request.getParameter("idTipoEntita");
  }  
  htmpl.set("idTipoEntita", idTipoEntita);
  
  if(Validator.isNotEmpty(descrizione))
    htmpl.set("descrizione", descrizione.trim());
  
  
  //Da valorizzare solo se la categoria prevede le uv
  if(SolmrConstants.TIPO_ENTITA_UV.equalsIgnoreCase(idTipoEntita))
  {
    // Combo Piano di riferimento
    ConsistenzaVO consistenzaVO = (ConsistenzaVO)request.getAttribute("consistenzaVO");
    if(Validator.isNotEmpty(consistenzaVO))
    {
      htmpl.newBlock("blkJavaScriptSezioneUv");
      htmpl.newBlock("blkSezioneUv");
      
      String descDichiarazioneConsistenza = consistenzaVO.getAnno()+" dichiarazione del "+StringUtils.parseDateFieldToEuropeStandard(SolmrConstants.FULL_DATE_ORACLE_FORMAT, SolmrConstants.FULL_DATE_EUROPE_FORMAT, consistenzaVO.getData());
		  htmpl.set("blkSezioneUv.idDichiarazioneConsistenza", consistenzaVO.getIdDichiarazioneConsistenza());
		  htmpl.set("blkSezioneUv.descDichiarazioneConsistenza", descDichiarazioneConsistenza);
		  
		  // DESTINAZIONE_PRODUTTIVA
		  if(elencoTipiUsoSuolo != null && elencoTipiUsoSuolo.length > 0)
		  {
		    for(int c = 0; c < elencoTipiUsoSuolo.length; c++) {
		      TipoUtilizzoVO tipoUtilizzoVO = (TipoUtilizzoVO)elencoTipiUsoSuolo[c];
		      htmpl.newBlock("blkSezioneUv.blkTipiUsoSuolo");
		      htmpl.set("blkSezioneUv.blkTipiUsoSuolo.idTipoUtilizzoElenco", tipoUtilizzoVO.getIdUtilizzo().toString());
		      htmpl.set("blkSezioneUv.blkTipiUsoSuolo.descrizione", "["+tipoUtilizzoVO.getCodice()+"] "+tipoUtilizzoVO.getDescrizione());
		    }
		  }
		  
		  // Combo Vitigno
		  if(elencoVarieta != null && elencoVarieta.length > 0) 
		  {
		    for(int i=0; i<elencoVarieta.length; i++)
		    {
		      TipoVarietaVO tipoVarietaVO = elencoVarieta[i];
		      htmpl.newBlock("blkJavaScriptSezioneUv.blkVarietaCombo");
		      htmpl.set("blkJavaScriptSezioneUv.blkVarietaCombo.idVarieta", ""+tipoVarietaVO.getIdVarieta());
		      htmpl.set("blkJavaScriptSezioneUv.blkVarietaCombo.idUtilizzo", ""+tipoVarietaVO.getIdUtilizzo());
		      htmpl.set("blkJavaScriptSezioneUv.blkVarietaCombo.descVarieta", tipoVarietaVO.getDescrizione());
		      htmpl.set("blkJavaScriptSezioneUv.blkVarietaCombo.index", ""+i);
		    }
		  }
		  
		  
		  // PROVINCE
		  if(Validator.isNotEmpty(elencoProvincie))
		  {
		    for(int c = 0; c < elencoProvincie.size(); c++) 
		    {
		      ProvinciaVO provinciaVO = elencoProvincie.get(c);
		      htmpl.newBlock("blkSezioneUv.blkProvinciaConduzioniParticelle");
		      htmpl.set("blkSezioneUv.blkProvinciaConduzioniParticelle.istatProvinciaConduzioniParticelle", ""+provinciaVO.getIstatProvincia());
		      htmpl.set("blkSezioneUv.blkProvinciaConduzioniParticelle.descrizione", provinciaVO.getDescrizione());
		    }
		  }
		  
		  // Combo dei comuni su cui insistono le particelle
		  if(elencoComuni != null && elencoComuni.size() > 0) 
		  {
		    Iterator<ComuneVO> iteraComuni = elencoComuni.iterator();
		    int i=0;
		    while(iteraComuni.hasNext()) 
		    {
		      ComuneVO comuneVO = (ComuneVO)iteraComuni.next();
		      htmpl.newBlock("blkJavaScriptSezioneUv.blkComuneCombo");
		      htmpl.set("blkJavaScriptSezioneUv.blkComuneCombo.istatComune", ""+comuneVO.getIstatComune());
		      htmpl.set("blkJavaScriptSezioneUv.blkComuneCombo.istatProvincia", ""+comuneVO.getIstatProvincia());
		      htmpl.set("blkJavaScriptSezioneUv.blkComuneCombo.descComune", comuneVO.getDescom()+" ("+comuneVO.getSiglaProv()+")");
		      htmpl.set("blkJavaScriptSezioneUv.blkComuneCombo.index", ""+i);
		      i++;      
		    }
		  }
		  
		  // Tipologia Vino
		  if(elencoTipiTipologiaVino != null && elencoTipiTipologiaVino.length > 0) 
		  {
		    for(int i = 0; i < elencoTipiTipologiaVino.length; i++) {
		      TipoTipologiaVinoVO tipoTipologiaVinoVO = elencoTipiTipologiaVino[i];
		      htmpl.newBlock("blkSezioneUv.blkTipiTipologiaVino");
		      htmpl.set("blkSezioneUv.blkTipiTipologiaVino.idTipologiaVino", tipoTipologiaVinoVO.getIdTipologiaVino().toString());
		      htmpl.set("blkSezioneUv.blkTipiTipologiaVino.descrizione", tipoTipologiaVinoVO.getDescrizione().trim());	      
		    }
		  }
	  
	    if(Validator.isNotEmpty(elencoParticelle)
		    && (elencoParticelle.size() > 0))
		  {
		    htmpl.newBlock("blkSezioneUv.blkUv");
		    for(int i = 0; i < elencoParticelle.size(); i++) 
		    {
		      htmpl.newBlock("blkSezioneUv.blkUv.blkElencoUv");
		      StoricoParticellaVO storicoParticellaElencoVO = elencoParticelle.get(i);
		      
		      if(Validator.isEmpty(storicoParticellaElencoVO.getUnitaArboreaDichiarataVO()))
		      {
		        htmpl.set("blkSezioneUv.blkUv.blkElencoUv.idStoricoUnitaArborea", ""+storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getIdStoricoUnitaArborea());
		        htmpl.set("blkSezioneUv.blkUv.blkElencoUv.progrUnar", storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getProgrUnar());
		        if(Validator.isNotEmpty(storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getTipoUtilizzoVO()))
		          htmpl.set("blkSezioneUv.blkUv.blkElencoUv.destProd", "["+storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getTipoUtilizzoVO().getCodice()+"] "+
		            storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getTipoUtilizzoVO().getDescrizione());
		        if(Validator.isNotEmpty(storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getTipoVarietaVO()))
		          htmpl.set("blkSezioneUv.blkUv.blkElencoUv.descVitigno", "["+storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getTipoVarietaVO().getCodiceVarieta()+"] "+
		            storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getTipoVarietaVO().getDescrizione());
		        htmpl.set("blkSezioneUv.blkUv.blkElencoUv.annoImpianto", 
		            storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getAnnoImpianto());
		        if(Validator.isNotEmpty(storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO()))
		          htmpl.set("blkSezioneUv.blkUv.blkElencoUv.descIdoneita", 
		            storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO().getDescrizione());
		        htmpl.set("blkSezioneUv.blkUv.blkElencoUv.annoIdoneita", 
		            storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getAnnoIscrizioneAlbo());      
		        htmpl.set("blkSezioneUv.blkUv.blkElencoUv.supVitata", StringUtils.parseSuperficieField(
		          storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getArea()));
		      }
		      else
		      {
		        htmpl.set("blkSezioneUv.blkUv.blkElencoUv.idStoricoUnitaArborea", ""+storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getIdStoricoUnitaArborea());
		        htmpl.set("blkSezioneUv.blkUv.blkElencoUv.progrUnar", storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getProgrUnar());
		        if(Validator.isNotEmpty(storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getTipoUtilizzoVO()))
	            htmpl.set("blkSezioneUv.blkUv.blkElencoUv.destProd", "["+storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getTipoUtilizzoVO().getCodice()+"] "+
	              storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getTipoUtilizzoVO().getDescrizione());
	          if(Validator.isNotEmpty(storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getTipoVarietaVO()))
	            htmpl.set("blkSezioneUv.blkUv.blkElencoUv.descVitigno", "["+storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getTipoVarietaVO().getCodiceVarieta()+"] "+
	              storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getTipoVarietaVO().getDescrizione());
	          htmpl.set("blkSezioneUv.blkUv.blkElencoUv.annoImpianto", 
	              storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getAnnoImpianto());
	          if(Validator.isNotEmpty(storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getTipoTipologiaVinoVO()))
	            htmpl.set("blkSezioneUv.blkUv.blkElencoUv.descIdoneita", 
	              storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getTipoTipologiaVinoVO().getDescrizione());
	          htmpl.set("blkSezioneUv.blkUv.blkElencoUv.annoIdoneita", 
	              storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getAnnoIscrizioneAlbo());      
	          htmpl.set("blkSezioneUv.blkUv.blkElencoUv.supVitata", StringUtils.parseSuperficieField(
	            storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getArea()));
		      }
		      htmpl.set("blkSezioneUv.blkUv.blkElencoUv.descComuneParticella", storicoParticellaElencoVO.getComuneParticellaVO().getDescom());
		      htmpl.set("blkSezioneUv.blkUv.blkElencoUv.siglaProvinciaParticella", storicoParticellaElencoVO.getComuneParticellaVO().getSiglaProv());
		      if(Validator.isNotEmpty(storicoParticellaElencoVO.getSezione())) {
		        htmpl.set("blkSezioneUv.blkUv.blkElencoUv.sezione", storicoParticellaElencoVO.getSezione().toUpperCase());
		      }
		      htmpl.set("blkSezioneUv.blkUv.blkElencoUv.foglio", storicoParticellaElencoVO.getFoglio());
		      if(Validator.isNotEmpty(storicoParticellaElencoVO.getParticella())) {
		        htmpl.set("blkSezioneUv.blkUv.blkElencoUv.particella", storicoParticellaElencoVO.getParticella());
		      }
		      if(Validator.isNotEmpty(storicoParticellaElencoVO.getSubalterno())) {
		        htmpl.set("blkSezioneUv.blkUv.blkElencoUv.subalterno", storicoParticellaElencoVO.getSubalterno());
		      }
		      
		        
		        
		      if(Validator.isNotEmpty(storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getNote()))
          {
            htmpl.set("blkSezioneUv.blkUv.blkElencoUv.noteUv", storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getNote());
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
	                    htmpl.set("blkSezioneUv.blkUv.blkElencoUv.err_"+property,
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
		    
		    htmpl.newBlock("blkSezioneUv.blkUv.blkEliminaElencoUv");
		    
		  }
		  
		  htmpl.newBlock("blkSalvaBottone");
		  
	  }	  
    else
    {
      String messaggioErrore = "Attenzione: non è presente una dichiarazione di consistenza valida per cui non è possibile inserire questa tipologia di notifica.";
      htmpl.newBlock("blkErrore");
      htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
    }
  
  
  }
  else if(SolmrConstants.TIPO_ENTITA_PARTICELLE.equalsIgnoreCase(idTipoEntita))
  {
    // Combo Piano di riferimento
    ConsistenzaVO consistenzaVO = (ConsistenzaVO)request.getAttribute("consistenzaVO");
    if(Validator.isNotEmpty(consistenzaVO))
    {
      htmpl.newBlock("blkJavaScriptSezioneParticelle");
      htmpl.newBlock("blkSezioneParticelle");
      
      String descDichiarazioneConsistenza = consistenzaVO.getAnno()+" dichiarazione del "+StringUtils.parseDateFieldToEuropeStandard(SolmrConstants.FULL_DATE_ORACLE_FORMAT, SolmrConstants.FULL_DATE_EUROPE_FORMAT, consistenzaVO.getData());
      htmpl.set("blkSezioneParticelle.idDichiarazioneConsistenza", consistenzaVO.getIdDichiarazioneConsistenza());
      htmpl.set("blkSezioneParticelle.descDichiarazioneConsistenza", descDichiarazioneConsistenza);
      
      // PROVINCE
      if(Validator.isNotEmpty(elencoProvincie))
      {
        for(int c = 0; c < elencoProvincie.size(); c++) 
        {
          ProvinciaVO provinciaVO = elencoProvincie.get(c);
          htmpl.newBlock("blkSezioneParticelle.blkProvinciaConduzioniParticelle");
          htmpl.set("blkSezioneParticelle.blkProvinciaConduzioniParticelle.istatProvinciaConduzioniParticelle", ""+provinciaVO.getIstatProvincia());
          htmpl.set("blkSezioneParticelle.blkProvinciaConduzioniParticelle.descrizione", provinciaVO.getDescrizione());
        }
      }
      
      // Combo dei comuni su cui insistono le particelle
      if(elencoComuni != null && elencoComuni.size() > 0) 
      {
        Iterator<ComuneVO> iteraComuni = elencoComuni.iterator();
        int i=0;
        while(iteraComuni.hasNext()) 
        {
          ComuneVO comuneVO = (ComuneVO)iteraComuni.next();
          htmpl.newBlock("blkJavaScriptSezioneParticelle.blkComuneCombo");
          htmpl.set("blkJavaScriptSezioneParticelle.blkComuneCombo.istatComune", ""+comuneVO.getIstatComune());
          htmpl.set("blkJavaScriptSezioneParticelle.blkComuneCombo.istatProvincia", ""+comuneVO.getIstatProvincia());
          htmpl.set("blkJavaScriptSezioneParticelle.blkComuneCombo.descComune", comuneVO.getDescom()+" ("+comuneVO.getSiglaProv()+")");
          htmpl.set("blkJavaScriptSezioneParticelle.blkComuneCombo.index", ""+i);
          i++;      
        }
      }
     
	    // Combo uso suolo
		  if(elencoTipiUsoSuoloPart != null && elencoTipiUsoSuoloPart.size() > 0) 
		  {
		    Iterator<TipoUtilizzoVO> iteraTipiUsoSuolo = elencoTipiUsoSuoloPart.iterator();
		    int indice = 0;
		    while(iteraTipiUsoSuolo.hasNext()) 
		    {
		      TipoUtilizzoVO tipoUtilizzoVO = (TipoUtilizzoVO)iteraTipiUsoSuolo.next();
		      htmpl.newBlock("blkSezioneParticelle.blkTipiUsoSuolo");
		      if(indice == 0) 
		      {
		        htmpl.set("blkSezioneParticelle.blkTipiUsoSuolo.idTipoUtilizzo", "-1");
		        htmpl.set("blkSezioneParticelle.blkTipiUsoSuolo.descrizione", "qualunque uso del suolo");
		        if("-1".equalsIgnoreCase(idTipoUtilizzo)) 
		        {
		          htmpl.set("blkSezioneParticelle.blkTipiUsoSuolo.selected", "selected=\"selected\"", null);
		        }
		        htmpl.set("blkSezioneParticelle.blkTipiUsoSuolo.idTipoUtilizzo", "0");
		        htmpl.set("blkSezioneParticelle.blkTipiUsoSuolo.descrizione", "senza uso del suolo");
		        if("0".equalsIgnoreCase(idTipoUtilizzo)) 
		        {
		          htmpl.set("blkSezioneParticelle.blkTipiUsoSuolo.selected", "selected=\"selected\"", null);
		        }
		      }
		      htmpl.set("blkSezioneParticelle.blkTipiUsoSuolo.idTipoUtilizzo", tipoUtilizzoVO.getIdUtilizzo().toString());
		      String codice = "";
		      String stato = "";
		      if(Validator.isNotEmpty(tipoUtilizzoVO.getCodice())) {
		        codice += "["+tipoUtilizzoVO.getCodice()+"] ";
		      }
		      if(Validator.isNotEmpty(tipoUtilizzoVO.getAnnoFineValidita())) {
		        stato = " [uso non attivo]";
		      }
		      htmpl.set("blkSezioneParticelle.blkTipiUsoSuolo.descrizione", codice + tipoUtilizzoVO.getDescrizione() + stato);
		      indice++;
		      if(Validator.isNotEmpty(idTipoUtilizzo)
		        && (new Long(idTipoUtilizzo).intValue() == tipoUtilizzoVO.getIdUtilizzo().intValue()))
		      {
		        htmpl.set("blkSezioneParticelle.blkTipiUsoSuolo.selected", "selected=\"selected\"", null);
		      }
		    }
		  }
		  else 
		  {
		    htmpl.newBlock("blkSezioneParticelle.blkTipiUsoSuolo");
		    htmpl.set("blkSezioneParticelle.blkTipiUsoSuolo.idTipoUtilizzo", "0");
		    htmpl.set("blkSezioneParticelle.blkTipiUsoSuolo.descrizione", "senza uso del suolo");
		    if(Validator.isNotEmpty(idTipoUtilizzo)
		      && ("0".equalsIgnoreCase(idTipoUtilizzo))) 
		    {
		      htmpl.set("blkSezioneParticelle.blkTipiUsoSuolo.selected", "selected=\"selected\"", null);
		    }
		  }
		  
		  
		  // Combo anomalie
	    if(elencoTipiControllo != null && elencoTipiControllo.length > 0) 
	    {
	      for(int i = 0; i < elencoTipiControllo.length; i++) 
	      {
	        htmpl.newBlock("blkSezioneParticelle.blkAnomalie");
	        TipoControlloVO tipoControlloVO = (TipoControlloVO)elencoTipiControllo[i];
	        htmpl.set("blkSezioneParticelle.blkAnomalie.idAnomalie", tipoControlloVO.getIdControllo().toString());
	        htmpl.set("blkSezioneParticelle.blkAnomalie.descrizione", tipoControlloVO.getDescrizione());
	        if(Validator.isNotEmpty(idAnomalie)) 
	        {
	          Long idAnomalieLg = new Long(idAnomalie);
	          if(idAnomalieLg.compareTo(tipoControlloVO.getIdControllo()) == 0) 
	          {
	            htmpl.set("blkSezioneParticelle.blkAnomalie.selected", "selected=\"selected\"", null);
	          }
	        }
	      }
	    }
	    
	    
	    if(Validator.isNotEmpty(elencoParticelle)
        && (elencoParticelle.size() > 0))
      {
        htmpl.newBlock("blkSezioneParticelle.blkParticelle");
      
        for(int i = 0; i < elencoParticelle.size(); i++) 
		    {
		      htmpl.newBlock("blkSezioneParticelle.blkParticelle.blkElencoParticelle");
		      htmpl.newBlock("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveParticelle");
		      StoricoParticellaVO storicoParticellaElencoVO = elencoParticelle.get(i);
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
		      
		      htmpl.set("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveParticelle.rowTot", ""+numRigheTot);      
		      htmpl.set("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveParticelle.idStoricoParticella", ""+storicoParticellaElencoVO.getIdStoricoParticella());
		      htmpl.set("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveParticelle.descComuneParticella", storicoParticellaElencoVO.getComuneParticellaVO().getDescom());
		      htmpl.set("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveParticelle.siglaProvinciaParticella", storicoParticellaElencoVO.getComuneParticellaVO().getSiglaProv());
		      if(Validator.isNotEmpty(storicoParticellaElencoVO.getSezione())) {
		        htmpl.set("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveParticelle.sezione", storicoParticellaElencoVO.getSezione().toUpperCase());
		      }
		      htmpl.set("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveParticelle.foglio", storicoParticellaElencoVO.getFoglio());
		      if(Validator.isNotEmpty(storicoParticellaElencoVO.getParticella())) {
		        htmpl.set("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveParticelle.particella", storicoParticellaElencoVO.getParticella());
		      }
		      if(Validator.isNotEmpty(storicoParticellaElencoVO.getSubalterno())) {
		        htmpl.set("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveParticelle.subalterno", storicoParticellaElencoVO.getSubalterno());
		      }
		      
		      
		      if(Validator.isNotEmpty(storicoParticellaElencoVO.getNote()))
          {
            htmpl.set("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveParticelle.noteParticelle", storicoParticellaElencoVO.getNote());
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
		          htmpl.set("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveParticelle.rowCond", ""+numRigheCond);
		          htmpl.set("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveParticelle.idTitoloPosseso", ""+conduzioneDichiarataVO.getIdTitoloPossesso());
		          BigDecimal percentualePossessoTmp = conduzioneDichiarataVO.getPercentualePossesso();
		          if(percentualePossessoTmp.compareTo(new BigDecimal(1)) < 0)
		          {
		            percentualePossessoTmp = new BigDecimal(1);
		          }  
		          htmpl.set("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveParticelle.percentualPossesso", Formatter.formatAndRoundBigDecimal0(percentualePossessoTmp));  
		          
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
			              htmpl.set("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveParticelle.descTipologiaProduttiva", descTipologiaProduttiva, null);
			              htmpl.set("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveParticelle.supUtilizzo", Formatter.formatDouble4(utilizzoDichiaratoVO.getSupUtilizzataBg()));
			            }
			            else
			            {
			              htmpl.newBlock("blkSezioneParticelle.blkParticelle.blkElencoParticelle");
			              htmpl.newBlock("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveUtilizzo");
			              String descTipologiaProduttiva = "["+utilizzoDichiaratoVO.getTipoUtilizzoVO().getCodice()
			                +"] "+utilizzoDichiaratoVO.getTipoUtilizzoVO().getDescrizione()+"<br>"
			                +"["+utilizzoDichiaratoVO.getTipoVarietaVO().getCodiceVarieta()
			                +"]"+utilizzoDichiaratoVO.getTipoVarietaVO().getDescrizione();
			              htmpl.set("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveUtilizzo.descTipologiaProduttiva", descTipologiaProduttiva, null);
			              htmpl.set("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveUtilizzo.supUtilizzo", Formatter.formatDouble4(utilizzoDichiaratoVO.getSupUtilizzataBg()));
			            }
			          }
			        }
		        }
		        else
		        {
		          htmpl.newBlock("blkSezioneParticelle.blkParticelle.blkElencoParticelle");
		          htmpl.newBlock("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveConduzione");
		          int numRigheCond = 0;
		          if(Validator.isNotEmpty(conduzioneDichiarataVO.getvUtilizzi()))
		          {
		            numRigheCond = conduzioneDichiarataVO.getvUtilizzi().size();
		          }
		          else
		          {         
		            numRigheCond = 1;
		          }
		          htmpl.set("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveConduzione.rowCond", ""+numRigheCond);
		          htmpl.set("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveConduzione.idTitoloPosseso", ""+conduzioneDichiarataVO.getIdTitoloPossesso());
		          BigDecimal percentualePossessoTmp = conduzioneDichiarataVO.getPercentualePossesso();
		          if(percentualePossessoTmp.compareTo(new BigDecimal(1)) < 0)
		          {
		            percentualePossessoTmp = new BigDecimal(1);
		          }  
		          htmpl.set("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveConduzione.percentualPossesso", Formatter.formatAndRoundBigDecimal0(percentualePossessoTmp));  
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
			              htmpl.set("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveConduzione.descTipologiaProduttiva", descTipologiaProduttiva, null);
			              htmpl.set("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveConduzione.supUtilizzo", Formatter.formatDouble4(utilizzoDichiaratoVO.getSupUtilizzataBg()));
			            }
			            else
			            {
			              htmpl.newBlock("blkSezioneParticelle.blkParticelle.blkElencoParticelle");
			              htmpl.newBlock("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveUtilizzo");
			              String descTipologiaProduttiva = "["+utilizzoDichiaratoVO.getTipoUtilizzoVO().getCodice()
			                +"] "+utilizzoDichiaratoVO.getTipoUtilizzoVO().getDescrizione()+"<br>"
			                +"["+utilizzoDichiaratoVO.getTipoVarietaVO().getCodiceVarieta()
			                +"]"+utilizzoDichiaratoVO.getTipoVarietaVO().getDescrizione();
			              htmpl.set("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveUtilizzo.descTipologiaProduttiva", descTipologiaProduttiva, null);
			              htmpl.set("blkSezioneParticelle.blkParticelle.blkElencoParticelle.blkChiaveUtilizzo.supUtilizzo", Formatter.formatDouble4(utilizzoDichiaratoVO.getSupUtilizzataBg()));
			            }
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
              while(iter.hasNext()) {
                String key = (String)iter.next();
                  if(key.startsWith("err_")) 
                  {
                    String property = key.substring(4);
                    Iterator<ValidationError> errorIterator = errorsNote.get(property);
                    if(errorIterator != null) 
                    {
                      ValidationError error = (ValidationError)errorIterator.next();
                      htmpl.set("blkSezioneParticelle.blkParticelle.blkElencoParticelle.err_"+property,
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
      
      
        htmpl.newBlock("blkSezioneParticelle.blkParticelle.blkEliminaElencoParticelle");
      
      }
      
      htmpl.newBlock("blkSalvaBottone");
	    
	    
	    
	  }
	  else
    {
      String messaggioErrore = "Attenzione: non è presente una dichiarazione di consistenza valida per cui non è possibile inserire questa tipologia di notifica.";
      htmpl.newBlock("blkErrore");
      htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
    }
	  
	  
	  
	  
  }
  else
  {
    htmpl.newBlock("blkSalvaBottone");
  }
  
  
  
  
  
  
  
 
 
 
 
 

 // Sezione relativa ai messaggi di errore
 HtmplUtil.setErrors(htmpl, errors, request, application);

 if(nuovaNotificaVO != null) 
 {
   if(Validator.isNotEmpty(nuovaNotificaVO.getDescrizione())) 
   {
     htmpl.set("descrizione", nuovaNotificaVO.getDescrizione());
   }
 }

 String richiestaConferma = (String)request.getAttribute("richiestaConferma");
 if(Validator.isNotEmpty(richiestaConferma)) 
 {
   htmpl.set("operazione", richiestaConferma);
   htmpl.set("messaggioOperazione", (String)SolmrConstants.get("RICHIESTA_CONFERMA_INSERIMENTO_NOTIFICA_BLOCCANTE"));
 }

 SolmrLogger.debug(this, "-- END nuovaNotificaView");
 
%>
<%= htmpl.text()%>
