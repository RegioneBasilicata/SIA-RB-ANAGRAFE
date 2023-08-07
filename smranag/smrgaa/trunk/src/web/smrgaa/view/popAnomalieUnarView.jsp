<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@page import="it.csi.solmr.dto.anag.terreni.*"%>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popAnomalieUnar.htm");
 	Htmpl htmpl = new Htmpl(layout);

	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%


 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);
	
 	StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)request.getAttribute("storicoParticellaVO");
 	String messaggio = (String)request.getAttribute("messaggio");
 	EsitoControlloUnarVO[] elencoAnomalie = (EsitoControlloUnarVO[])request.getAttribute("elencoAnomalie");
 	Vector elencoAnomalieDichiarate = (Vector)request.getAttribute("elencoAnomalieDichiarate");
 	
	//Dati relativi alla particella	
 	htmpl.set("descComuneParticella", storicoParticellaVO.getComuneParticellaVO().getDescom());
 	if(Validator.isNotEmpty(storicoParticellaVO.getComuneParticellaVO().getSiglaProv())) {
 		htmpl.set("siglaProvinciaParticella", storicoParticellaVO.getComuneParticellaVO().getSiglaProv());
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
	StoricoUnitaArboreaVO storicoUnitaArboreaVO = storicoParticellaVO.getStoricoUnitaArboreaVO();
	if(storicoUnitaArboreaVO.getIdTipologiaUnar() != null) {
		htmpl.set("descTipoUnar", storicoUnitaArboreaVO.getTipoTipologiaUnitaArboreaVO().getDescrizione());
	}
	htmpl.set("progressivo", storicoUnitaArboreaVO.getProgrUnar());
	if(storicoUnitaArboreaVO.getIdUtilizzo() != null) {
			String codice = "";
		if(Validator.isNotEmpty(storicoUnitaArboreaVO.getTipoUtilizzoVO().getCodice())) {
			codice += "["+storicoUnitaArboreaVO.getTipoUtilizzoVO().getCodice()+"] ";
			htmpl.set("destinazioneProduttiva", codice + storicoUnitaArboreaVO.getTipoUtilizzoVO().getDescrizione());
		}
		}
	if(storicoUnitaArboreaVO.getIdVarieta() != null) {
			htmpl.set("varieta", "["+storicoUnitaArboreaVO.getTipoVarietaVO().getCodiceVarieta()+"] "+storicoUnitaArboreaVO.getTipoVarietaVO().getDescrizione());
		}
	if(Validator.isNotEmpty(storicoUnitaArboreaVO.getArea())) {
		htmpl.set("area", StringUtils.parseSuperficieField(storicoUnitaArboreaVO.getArea()));
	}

 	// Non sono state trovate anomalie
 	if(Validator.isNotEmpty(messaggio) || ((elencoAnomalie == null || elencoAnomalie.length == 0) && (elencoAnomalieDichiarate == null || elencoAnomalieDichiarate.size() == 0))) {
   		htmpl.newBlock("blkNoAnomalie");
   		htmpl.set("blkNoAnomalie.messaggio", AnagErrors.ERRORE_NO_ANOMALIE_UNAR);
 	}
 	else {
   		htmpl.newBlock("blkAnomalie");
   		if(elencoAnomalie != null && elencoAnomalie.length > 0) {
   			for(int i = 0; i < elencoAnomalie.length; i++) {
     			htmpl.newBlock("blkAnomalie.blkElencoAnomalie");
     			EsitoControlloUnarVO esitoControlloUnarVO = (EsitoControlloUnarVO)elencoAnomalie[i];
			    htmpl.set("blkAnomalie.blkElencoAnomalie.descrizioneTipoControllo", esitoControlloUnarVO.getControllo().getDescription());
			    htmpl.set("blkAnomalie.blkElencoAnomalie.descrizione", esitoControlloUnarVO.getDescrizione());
			    if(esitoControlloUnarVO.getBloccante().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
			    	htmpl.set("blkAnomalie.blkElencoAnomalie.immagine", SolmrConstants.IMMAGINE_ESITO_BLOCCANTE);
			    }
			    if(esitoControlloUnarVO.getBloccante().equalsIgnoreCase(SolmrConstants.FLAG_N)) {
			    	htmpl.set("blkAnomalie.blkElencoAnomalie.immagine", SolmrConstants.IMMAGINE_ESITO_WARNING);
			    }
     		}
   		}
   		else {
   			for(int i = 0; i < elencoAnomalieDichiarate.size(); i++) {
     			htmpl.newBlock("blkAnomalie.blkElencoAnomalie");
     			DichiarazioneSegnalazioneVO dichiarazioneSegnalazioneVO = (DichiarazioneSegnalazioneVO)elencoAnomalieDichiarate.elementAt(i);
			    htmpl.set("blkAnomalie.blkElencoAnomalie.descrizioneTipoControllo", dichiarazioneSegnalazioneVO.getControllo().getDescription());
			    htmpl.set("blkAnomalie.blkElencoAnomalie.descrizione", dichiarazioneSegnalazioneVO.getDescrizione());
			    if(dichiarazioneSegnalazioneVO.getBloccante().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
			    	htmpl.set("blkAnomalie.blkElencoAnomalie.immagine", SolmrConstants.IMMAGINE_ESITO_BLOCCANTE);
			    }
			    if(dichiarazioneSegnalazioneVO.getBloccante().equalsIgnoreCase(SolmrConstants.FLAG_N)) {
			    	htmpl.set("blkAnomalie.blkElencoAnomalie.immagine", SolmrConstants.IMMAGINE_ESITO_WARNING);
			    }
     		}
   		}
 	}


%>
<%= htmpl.text()%>
