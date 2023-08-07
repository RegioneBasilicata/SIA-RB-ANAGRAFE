<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.util.*" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popCercaParticelleInserimento.htm");
 	Htmpl htmpl = new Htmpl(layout);

	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);
 	
	StoricoParticellaVO[] elencoParticelle = (StoricoParticellaVO[])request.getAttribute("elencoParticelleEvento");
	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
	String provinciaEvento = (String)request.getAttribute("provinciaEvento");
	String comuneEvento = (String)request.getAttribute("comuneEvento");
	String sezioneEvento = (String)request.getAttribute("sezioneEvento");
	String foglioEvento = (String)request.getAttribute("foglioEvento");
	String particellaEvento = (String)request.getAttribute("particellaEvento");
	String noAziende = (String)request.getAttribute("noAziende");
	String ute = (String)request.getAttribute("ute");
	String siglaProvincia = (String)request.getAttribute("siglaProvincia");
	String descComune = (String)request.getAttribute("descComune");
	String descStatoEstero = (String)request.getAttribute("descStatoEstero");
	String sezione = (String)request.getAttribute("sezione");
	String foglio = (String)request.getAttribute("foglio");
	String particella = (String)request.getAttribute("particella");
	String provvisoria = (String)request.getAttribute("provvisoria");
	String subalterno = (String)request.getAttribute("subalterno");
	String evento = (String)request.getAttribute("evento");
	
	// Settaggio dei parametri necessati per rieffettuare la ricerca da pulsante
	// aggiorna e per mantenere i parametri inseriti nella pagina chiamante
	if(Validator.isNotEmpty(provinciaEvento)) {
		htmpl.set("provinciaEvento", provinciaEvento);
	}
	if(Validator.isNotEmpty(comuneEvento)) {
		htmpl.set("comuneEvento", comuneEvento);
	}
	if(Validator.isNotEmpty(sezioneEvento)) {
		htmpl.set("sezioneEvento", sezioneEvento);
	}
	if(Validator.isNotEmpty(foglioEvento)) {
		htmpl.set("foglioEvento", foglioEvento);
	}
	if(Validator.isNotEmpty(particellaEvento)) {
		htmpl.set("particellaEvento", particellaEvento);
	}
	if(Validator.isNotEmpty(siglaProvincia)) {
		htmpl.set("siglaProvincia", siglaProvincia);
	}
	if(Validator.isNotEmpty(descComune)) {
		htmpl.set("descComune", descComune);
	}
	if(Validator.isNotEmpty(descStatoEstero)) {
		htmpl.set("descStatoEstero", descStatoEstero);
	}
	if(Validator.isNotEmpty(sezione)) {
		htmpl.set("sezione", sezione);
	}
	if(Validator.isNotEmpty(foglio)) {
		htmpl.set("foglio", foglio);
	}
	if(Validator.isNotEmpty(particella)) {
		htmpl.set("particella", particella);
	}
	if(Validator.isNotEmpty(provvisoria)) {
		htmpl.set("provvisoria", provvisoria);
	}
	if(Validator.isNotEmpty(subalterno)) {
		htmpl.set("subalterno", subalterno);
	}
	if(Validator.isNotEmpty(evento)) {
		htmpl.set("idEvento", evento);
	}
	if(Validator.isNotEmpty(ute)) {
		htmpl.set("idUte", ute);
	}
	
	// Elenco delle particelle trovate
	if(elencoParticelle != null && elencoParticelle.length > 0) {
		htmpl.newBlock("blkParticelle");
		if(Validator.isNotEmpty(noAziende)) {
			htmpl.set("blkParticelle.checkedNoAzienda", "checked=\"checked\"");
		}
		for(int i = 0; i < elencoParticelle.length; i++) {
			htmpl.newBlock("blkParticelle.blkElencoParticelle");
			StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)elencoParticelle[i];
			htmpl.set("blkParticelle.blkElencoParticelle.idStoricoParticella", storicoParticellaVO.getIdStoricoParticella().toString());
			htmpl.set("blkParticelle.blkElencoParticelle.descComuneParticella", storicoParticellaVO.getComuneParticellaVO().getDescom());
			htmpl.set("blkParticelle.blkElencoParticelle.siglaProvinciaParticella", storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia());
			htmpl.set("blkParticelle.blkElencoParticelle.sezione", storicoParticellaVO.getSezione());
			htmpl.set("blkParticelle.blkElencoParticelle.foglio", storicoParticellaVO.getFoglio());
			htmpl.set("blkParticelle.blkElencoParticelle.particella", storicoParticellaVO.getParticella());
			htmpl.set("blkParticelle.blkElencoParticelle.subalterno", storicoParticellaVO.getSubalterno());
			htmpl.set("blkParticelle.blkElencoParticelle.supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
		}
	}
	else {
		htmpl.newBlock("blkNoParticelle");
		if(Validator.isNotEmpty(noAziende)) {
			htmpl.set("blkNoParticelle.checkedNoAzienda", "checked=\"checked\"");
		}
		htmpl.set("blkNoParticelle.messaggioErrore", messaggioErrore);
	}
	
%>
<%= htmpl.text()%>
