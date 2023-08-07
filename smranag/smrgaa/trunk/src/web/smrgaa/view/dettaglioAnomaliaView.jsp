<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>


<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%
  
	Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/dettaglioAnomalia.htm");

  	%>
    	<%@include file = "/view/remoteInclude.inc" %>
  	<%
    
    RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

  	AnagFacadeClient client = new AnagFacadeClient();
  	AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

  	HtmplUtil.setErrors(htmpl, (ValidationErrors)request.getAttribute("errors"), request, application);

  	// Nuova gestione fogli di stile
  	htmpl.set("head", head, null);
  	htmpl.set("header", header, null);
  	htmpl.set("footer", footer, null);

  	ErrAnomaliaDicConsistenzaVO errAnomaliaDicConsistenzaVO = (ErrAnomaliaDicConsistenzaVO)request.getAttribute("errAnomaliaDicConsistenzaVO");
  	DocumentoVO documentoVO = (DocumentoVO)request.getAttribute("documentoVO");

  	String percorsoErrori = null;
  	if(pathToFollow.equalsIgnoreCase("rupar")) {
    	percorsoErrori = "/css_rupar/agricoltura/im/";
  	}
  	else if(pathToFollow.equalsIgnoreCase("sispie")) {
  	    percorsoErrori = "/css/agricoltura/im/";
  	}
  	else if(pathToFollow.equalsIgnoreCase("TOBECONFIG")){
  	    percorsoErrori="/css/agricoltura/im/";  
  	}

  	if(errAnomaliaDicConsistenzaVO != null) {
	    htmpl.set("codiceControllo", errAnomaliaDicConsistenzaVO.getCodiceControllo());
	    htmpl.set("tipoAnomaliaErrore", errAnomaliaDicConsistenzaVO.getTipoAnomaliaErrore());
	    htmpl.set("descAnomaliaErrore", errAnomaliaDicConsistenzaVO.getDescAnomaliaErrore());
	    htmpl.set("dataEsecuzione", errAnomaliaDicConsistenzaVO.getDataEsecuzione());
    	if(errAnomaliaDicConsistenzaVO.isBloccante()) {
      		// Reperisco l'immagine da ANDROMEDA
      		htmpl.set("immagine", percorsoErrori + "Bloccante.gif");
      		htmpl.set("descImmagine",(String)it.csi.solmr.etc.anag.AnagErrors.get("ERR_ANOMALIA_BLOCCANTE"));
      		if(errAnomaliaDicConsistenzaVO.getIdDichiarazioneCorrezione()!= null && !"".equals(errAnomaliaDicConsistenzaVO.getIdDichiarazioneCorrezione())) {
        		htmpl.set("risolto",(String)SolmrConstants.get("VALORE_SI"));
      		}
    	}
    	else {
      		// Reperisco l'immagine da ANDROMEDA
      		htmpl.set("immagine", percorsoErrori + "Warning.gif");
      		htmpl.set("descImmagine",(String)it.csi.solmr.etc.anag.AnagErrors.get("ERR_ANOMALIA_NON_BLOCCANTE"));
    	}
    	if(errAnomaliaDicConsistenzaVO.getIdDichiarazioneCorrezione() != null) {
    		if(documentoVO != null) {
    			String descrizione = documentoVO.getTipoDocumentoVO().getDescrizione();
    			if(Validator.isNotEmpty(documentoVO.getNumeroProtocollo())) {
    				descrizione += " Prot. "+StringUtils.parseNumeroProtocolloField(documentoVO.getNumeroProtocollo())+" del "+DateUtils.formatDate(documentoVO.getDataProtocollo());
    			}
    			htmpl.set("riferimentoDocumento", descrizione);
    		}
    		else {
    			String descrizione = errAnomaliaDicConsistenzaVO.getTipoDocumento();
    			if(Validator.isNotEmpty(errAnomaliaDicConsistenzaVO.getRiferimentoDocumento())) {
    				descrizione += " Rif: "+errAnomaliaDicConsistenzaVO.getRiferimentoDocumento();
    			}
    			htmpl.set("riferimentoDocumento", descrizione);		
    		}
    	}
      
      //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
      //al ruolo!
      String dateUlt = "";
      if(errAnomaliaDicConsistenzaVO.getDataUltimaModifica() !=null)
      {
        dateUlt = DateUtils.formatDate(errAnomaliaDicConsistenzaVO.getDataUltimaModifica());
      } 
      ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
        "ultimaModificaVw", dateUlt, errAnomaliaDicConsistenzaVO.getUtenteUltimaModifica(),
        errAnomaliaDicConsistenzaVO.getEnteUltimaModifica(),null);
    	//if(!" (-)".equals(errAnomaliaDicConsistenzaVO.getUltimaModifica())) {
      		//htmpl.set("ultimaModifica", errAnomaliaDicConsistenzaVO.getUltimaModifica());
    	//}
  	}
  	out.print(htmpl.text());
%>
