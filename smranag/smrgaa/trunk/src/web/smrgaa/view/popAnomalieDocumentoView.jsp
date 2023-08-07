<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popAnomalieDocumento.htm");
 	Htmpl htmpl = new Htmpl(layout);

	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%


 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);
 	
	// Dati relativi al documento
	DocumentoVO documentoVO = (DocumentoVO)request.getAttribute("documentoVO");
	htmpl.set("descTipoTipologiaDocumento", documentoVO.getTipoTipologiaDocumento().getDescription());
	htmpl.set("descTipoCategoriaDocumento", documentoVO.getTipoCategoriaDocumentoVO().getDescrizione());
	htmpl.set("descTipoDocumento", documentoVO.getTipoDocumentoVO().getDescrizione());
	htmpl.set("dataInizioValidita", DateUtils.formatDate(documentoVO.getDataInizioValidita()));
	if(Validator.isNotEmpty(documentoVO.getDataFineValidita())) {
 		htmpl.set("dataFineValidita", DateUtils.formatDate(documentoVO.getDataFineValidita()));
	}
	if(Validator.isNotEmpty(documentoVO.getNumeroProtocollo())) {
 		htmpl.set("numeroProtocollo", StringUtils.parseNumeroProtocolloField(documentoVO.getNumeroProtocollo()));
	}
	if(Validator.isNotEmpty(documentoVO.getDataProtocollo())) {
 		htmpl.set("dataProtocollo", DateUtils.formatDate(documentoVO.getDataProtocollo()));
	}

 	EsitoControlloDocumentoVO[] elencoEsitoControlloDocumento = (EsitoControlloDocumentoVO[])request.getAttribute("elencoEsitoControlloDocumento");

 	// Non sono state trovate anomalie
 	if(elencoEsitoControlloDocumento == null || elencoEsitoControlloDocumento.length == 0) {
   		htmpl.newBlock("blkNoAnomalie");
   		htmpl.set("blkNoAnomalie.messaggio", AnagErrors.ERRORE_NO_ANOMALIE_DOCUMENTI);
 	}
 	else {
   		htmpl.newBlock("blkAnomalie");
   		for(int i = 0; i < elencoEsitoControlloDocumento.length; i++) {
     		htmpl.newBlock("blkAnomalie.blkElencoAnomalie");
     		EsitoControlloDocumentoVO esitoControlloDocumentoVO = (EsitoControlloDocumentoVO)elencoEsitoControlloDocumento[i];
			htmpl.set("blkAnomalie.blkElencoAnomalie.descrizioneTipoControllo", esitoControlloDocumentoVO.getControllo().getDescription());
			htmpl.set("blkAnomalie.blkElencoAnomalie.descrizione", esitoControlloDocumentoVO.getDescrizione());
			if(esitoControlloDocumentoVO.getBloccante().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
			    htmpl.set("blkAnomalie.blkElencoAnomalie.immagine", SolmrConstants.IMMAGINE_ESITO_BLOCCANTE);
			}
			else if(esitoControlloDocumentoVO.getBloccante().equalsIgnoreCase(SolmrConstants.FLAG_N)) {
			    htmpl.set("blkAnomalie.blkElencoAnomalie.immagine", SolmrConstants.IMMAGINE_ESITO_WARNING);
			}
   		}
 	}


%>
<%= htmpl.text()%>
