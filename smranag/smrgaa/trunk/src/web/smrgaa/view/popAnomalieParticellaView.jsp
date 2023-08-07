<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@page import="it.csi.solmr.dto.anag.terreni.*"%>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popAnomalieParticella.htm");
 	Htmpl htmpl = new Htmpl(layout);

	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%


 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);
	
 	FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)session.getAttribute("filtriParticellareRicercaVO");
 	boolean isCurrent = false;
 	
	//Dati relativi alla particella
	StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)request.getAttribute("storicoParticellaVO");
 	htmpl.set("descComuneParticella", storicoParticellaVO.getComuneParticellaVO().getDescom());
 	if(Validator.isNotEmpty(storicoParticellaVO.getComuneParticellaVO().getSiglaProv())) {
 		htmpl.set("siglaProvinciaParticella", storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia());
 	}
 	if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) {
 		htmpl.set("sezione", storicoParticellaVO.getSezione());
 	}
 	htmpl.set("foglio", storicoParticellaVO.getFoglio());
 	if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) {
 		htmpl.set("particella", storicoParticellaVO.getParticella());
 	}
 	if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno())) {
 		htmpl.set("subalterno", storicoParticellaVO.getSubalterno());
 	}
 	htmpl.set("supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
  htmpl.set("superficieGrafica", StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica()));
	if(filtriParticellareRicercaVO!=null && filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() <= 0) {
		ConduzioneParticellaVO conduzioneParticellaVO = (ConduzioneParticellaVO)storicoParticellaVO.getElencoConduzioni()[0];
		htmpl.set("idTitoloPossesso", conduzioneParticellaVO.getIdTitoloPossesso().toString());
		//htmpl.set("superficieCondotta", StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieCondotta()));
		isCurrent = true;
	}
	else {
		ConduzioneDichiarataVO conduzioneDichiarataVO = (ConduzioneDichiarataVO)storicoParticellaVO.getElencoConduzioniDichiarate()[0];
		htmpl.set("idTitoloPossesso", conduzioneDichiarataVO.getIdTitoloPossesso().toString());
		//htmpl.set("superficieCondotta", StringUtils.parseSuperficieField(conduzioneDichiarataVO.getSuperficieCondotta()));		
	}

 	String messaggio = (String)request.getAttribute("messaggio");
 	Vector elencoAnomalie = (Vector)request.getAttribute("elencoAnomalie");

 	// Non sono state trovate anomalie
 	if(Validator.isNotEmpty(messaggio) || (elencoAnomalie == null || elencoAnomalie.size() == 0)) {
   		htmpl.newBlock("blkNoAnomalie");
   		htmpl.set("blkNoAnomalie.messaggio", messaggio);
 	}
 	else {
   		htmpl.newBlock("blkAnomalie");
   		Iterator iteraAnomalie = elencoAnomalie.iterator();
   		while(iteraAnomalie.hasNext()) {
     		htmpl.newBlock("blkAnomalie.blkElencoAnomalie");
     		if(isCurrent) {
     			EsitoControlloParticellaVO esitoControlloParticellaVO = (EsitoControlloParticellaVO)iteraAnomalie.next();
			    htmpl.set("blkAnomalie.blkElencoAnomalie.descrizioneTipoControllo", esitoControlloParticellaVO.getDescrizioneControllo());
			    htmpl.set("blkAnomalie.blkElencoAnomalie.descrizione", esitoControlloParticellaVO.getDescrizione());
			    if(esitoControlloParticellaVO.getBloccante().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
			    	htmpl.set("blkAnomalie.blkElencoAnomalie.immagine", (String)SolmrConstants.get("IMMAGINE_ESITO_BLOCCANTE"));
			    }
			    if(esitoControlloParticellaVO.getBloccante().equalsIgnoreCase(SolmrConstants.FLAG_N)) {
			    	htmpl.set("blkAnomalie.blkElencoAnomalie.immagine", (String)SolmrConstants.get("IMMAGINE_ESITO_WARNING"));
			    }
     		}
     		else {
     			DichiarazioneSegnalazioneVO dichiarazioneSegnalazioneVO = (DichiarazioneSegnalazioneVO)iteraAnomalie.next();
     			htmpl.set("blkAnomalie.blkElencoAnomalie.descrizioneTipoControllo", dichiarazioneSegnalazioneVO.getControllo().getDescription());
			    htmpl.set("blkAnomalie.blkElencoAnomalie.descrizione", dichiarazioneSegnalazioneVO.getDescrizione());
			    if(dichiarazioneSegnalazioneVO.getBloccante().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
			    	htmpl.set("blkAnomalie.blkElencoAnomalie.immagine", (String)SolmrConstants.get("IMMAGINE_ESITO_BLOCCANTE"));
			    }
			    if(dichiarazioneSegnalazioneVO.getBloccante().equalsIgnoreCase(SolmrConstants.FLAG_N)) {
			    	htmpl.set("blkAnomalie.blkElencoAnomalie.immagine", (String)SolmrConstants.get("IMMAGINE_ESITO_WARNING"));
			    }
     		}
   		}
 	}


%>
<%= htmpl.text()%>
