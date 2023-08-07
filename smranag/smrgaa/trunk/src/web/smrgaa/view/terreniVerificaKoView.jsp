<%@ page language="java"
contentType="text/html"
isErrorPage="true"
%>


<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.consistenza.TipoControlloVO "%>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>

<%
  SolmrLogger.debug(this, " - terreniVerificaKoView.jsp - INIZIO PAGINA");

  java.io.InputStream layout = application.getResourceAsStream("/layout/terreniVerifica_ko.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  TipoControlloVO[] elencoTipiControllo = (TipoControlloVO[])request.getAttribute("elencoTipiControllo");
  String idControllo = request.getParameter("idControllo");
  String segnalazioneBloccante = request.getParameter("segnalazioneBloccante");
  String segnalazioneWarning = request.getParameter("segnalazioneWarning");
  String segnalazioneOk = request.getParameter("segnalazioneOk");
  //String ordinamento = (String)request.getAttribute("ordinamento");
  String operazione = request.getParameter("operazione");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  HtmplUtil.setValues(htmpl, anagAziendaVO);

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  SolmrLogger.debug(this, "anagAziendaVO.getCUAA(): " + anagAziendaVO.getCUAA());

  Vector anomalie=(Vector)session.getAttribute("anomalieDichiarazioniConsistenza");

  
  
  // Immagini relative alle segnalazioni
  String sourceImage = null;
  if(pathToFollow.equalsIgnoreCase("rupar")) {
    sourceImage = application.getInitParameter("erroriRupar");
  }
  else if(pathToFollow.equalsIgnoreCase("sispie")){
    sourceImage = application.getInitParameter("erroriSispie");
  }
  else if(pathToFollow.equalsIgnoreCase("TOBECONFIG")){
	  sourceImage = application.getInitParameter("erroriTOBECONFIG");
  }
  
  htmpl.newBlock("blkFiltriTabAnomalia");
  
  // Combo tipo controllo
  if(elencoTipiControllo != null && elencoTipiControllo.length > 0) {
    for(int i = 0; i < elencoTipiControllo.length; i++) {
      TipoControlloVO tipoControlloVO = (TipoControlloVO)elencoTipiControllo[i];
      htmpl.newBlock("blkTipoControllo");
      htmpl.set("blkFiltriTabAnomalia.blkTipoControllo.idControllo", tipoControlloVO.getIdControllo().toString());
      htmpl.set("blkFiltriTabAnomalia.blkTipoControllo.descrizione", tipoControlloVO.getDescrizione());
      if(Validator.isNotEmpty(idControllo)) {
        if(idControllo.equalsIgnoreCase(tipoControlloVO.getIdControllo().toString())) {
          htmpl.set("blkFiltriTabAnomalia.blkTipoControllo.selected", "selected=\"selected\"");
        }
      }
    }
  }
  
  htmpl.set("blkFiltriTabAnomalia.srcBloccante", sourceImage+(String)SolmrConstants.get("IMMAGINE_BLOCCANTE"));
  htmpl.set("blkFiltriTabAnomalia.descImmagineBloccante", (String)SolmrConstants.DESC_TITLE_BLOCCANTE);  
  htmpl.set("blkFiltriTabAnomalia.srcWarning", sourceImage+(String)SolmrConstants.get("IMMAGINE_WARNING"));
  htmpl.set("blkFiltriTabAnomalia.descImmagineWarning", (String)SolmrConstants.DESC_TITLE_WARNING);  
  htmpl.set("blkFiltriTabAnomalia.srcOk", sourceImage+(String)SolmrConstants.get("IMMAGINE_OK"));
  htmpl.set("blkFiltriTabAnomalia.descImmagineOk", (String)SolmrConstants.DESC_TITLE_POSITIVO);  
  if(Validator.isNotEmpty(segnalazioneBloccante)) {
    htmpl.set("blkFiltriTabAnomalia.checkedBloccante", "checked=\"checked\""); 
  }
  if(Validator.isNotEmpty(segnalazioneWarning)) {
    htmpl.set("blkFiltriTabAnomalia.checkedWarning", "checked=\"checked\""); 
  }
  if(Validator.isNotEmpty(segnalazioneOk)) {
    htmpl.set("blkFiltriTabAnomalia.checkedOk", "checked=\"checked\"");  
  }

  if (anomalie !=null)
  {
    htmpl.newBlock("blkTabAnomalia");
    // Gestisco le frecce relative all'ordinamento:
    if(Validator.isNotEmpty(operazione)) {
      if(operazione.equalsIgnoreCase("idTipologiaAsc")) {
        htmpl.set("blkTabAnomalia.ordinaTipologia", "giu");
        htmpl.set("blkTabAnomalia.descOrdinaTipologia", "ordine decrescente");
        htmpl.set("blkTabAnomalia.tipoOrdinaTipologia", "idTipologiaDisc");
      }
      else {
        htmpl.set("blkTabAnomalia.ordinaTipologia", "su");
        htmpl.set("blkTabAnomalia.descOrdinaTipologia", "ordine crescente");
        htmpl.set("blkTabAnomalia.tipoOrdinaTipologia", "idTipologiaAsc");
      }
      if(operazione.equalsIgnoreCase("comuneAsc")) {
        htmpl.set("blkTabAnomalia.ordinaComune", "giu");
        htmpl.set("blkTabAnomalia.descOrdinaComune", "ordine decrescente");                    
        htmpl.set("blkTabAnomalia.tipoOrdinaComune", "comuneDisc");
      }
      else {
        htmpl.set("blkTabAnomalia.ordinaComune", "su");
        htmpl.set("blkTabAnomalia.descOrdinaComune", "ordine crescente");
        htmpl.set("blkTabAnomalia.tipoOrdinaComune", "comuneAsc");
      }
    }
    else {
      htmpl.set("blkTabAnomalia.ordinaTipologia", "giu");
      htmpl.set("blkTabAnomalia.descOrdinaTipologia", "ordine decrescente");
      htmpl.set("blkTabAnomalia.tipoOrdinaTipologia", "idTipologiaDisc");
      htmpl.set("blkTabAnomalia.ordinaComune", "su");
      htmpl.set("blkTabAnomalia.descOrdinaComune", "ordine crescente");
      htmpl.set("blkTabAnomalia.tipoOrdinaComune", "comuneAsc");
    }

  
    int size=anomalie.size();
    if(size > 0)
    {
    
      Iterator iteraAnomalie = anomalie.iterator();
      ErrAnomaliaDicConsistenzaVO err = null;
      String descAnomaliaOld="";
      while (iteraAnomalie.hasNext())
      {
        err = (ErrAnomaliaDicConsistenzaVO)iteraAnomalie.next();
        
        if (!descAnomaliaOld.equals(err.getDescGruppoControllo()))
        {
          descAnomaliaOld=err.getDescGruppoControllo();
          
          htmpl.set("blkTabAnomalia.descrizioneAnomalia",descAnomaliaOld);
        }
        
        htmpl.newBlock("blkTabAnomalia.blkAnomalia");
        
        if(Validator.isNotEmpty(err.getBloccanteStr()) && err.getBloccanteStr().equalsIgnoreCase("P"))
        {
          htmpl.set("blkTabAnomalia.blkAnomalia.immagine", sourceImage+(String)SolmrConstants.get("IMMAGINE_OK"));
          htmpl.set("blkTabAnomalia.blkAnomalia.descImmagine",(String)SolmrConstants.DESC_TITLE_POSITIVO);
        }
        else
        {
          if (err.isBloccante())
          {
            htmpl.set("blkTabAnomalia.blkAnomalia.immagine", sourceImage+(String)SolmrConstants.get("IMMAGINE_BLOCCANTE"));
            htmpl.set("blkTabAnomalia.blkAnomalia.descImmagine",(String)AnagErrors.get("ERR_ANOMALIA_BLOCCANTE"));
          }
          else
          {
            htmpl.set("blkTabAnomalia.blkAnomalia.immagine", sourceImage+(String)SolmrConstants.get("IMMAGINE_WARNING"));
            htmpl.set("blkTabAnomalia.blkAnomalia.descImmagine",(String)AnagErrors.get("ERR_ANOMALIA_NON_BLOCCANTE"));
          }
        }
        
        htmpl.set("blkTabAnomalia.blkAnomalia.tipologia",err.getTipoAnomaliaErrore());
        if(Validator.isNotEmpty(err.getStoricoParticellaVO()))
        {
          htmpl.set("blkTabAnomalia.blkAnomalia.descComune",err.getStoricoParticellaVO()
            .getComuneParticellaVO().getDescom());
          htmpl.set("blkTabAnomalia.blkAnomalia.sezione",err.getStoricoParticellaVO()
            .getSezione());
          htmpl.set("blkTabAnomalia.blkAnomalia.foglio",err.getStoricoParticellaVO()
            .getFoglio());
          htmpl.set("blkTabAnomalia.blkAnomalia.particella",err.getStoricoParticellaVO()
            .getParticella());
          htmpl.set("blkTabAnomalia.blkAnomalia.subalterno",err.getStoricoParticellaVO()
            .getSubalterno());
          htmpl.set("blkTabAnomalia.blkAnomalia.supCatastale", Formatter.formatDouble4(err.getStoricoParticellaVO()
            .getSupCatastale()));        
        }
        
        if(Validator.isNotEmpty(err.getConduzioneParticellaVO()))
        {
          htmpl.set("blkTabAnomalia.blkAnomalia.supCondotta", Formatter.formatDouble4(err.getConduzioneParticellaVO()
            .getSuperficieCondotta()));    
        }        
        
        htmpl.set("blkTabAnomalia.blkAnomalia.descrizione",err.getDescAnomaliaErrore());
        
      }
    }
  }
  else
  {
    htmpl.newBlock("blkNoTabAnomalia");
    htmpl.set("blkNoTabAnomalia.noRecordTrovati",AnagErrors.ERRORE_NOAZIENDE_TROVATE);
  }


  HtmplUtil.setErrors(htmpl, errors, request, application);

  SolmrLogger.debug(this, " - terreniVerificaKoView.jsp - FINE PAGINA");
%>

<%= htmpl.text()%>

