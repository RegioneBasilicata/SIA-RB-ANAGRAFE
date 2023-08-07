<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/unitaArboreeInserisciFrazionate.htm");
 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%
	
	// Nuova gestione fogli di stile:(Eclipse da errore a video ma funziona perchè
	// le variabili sono presenti dentro il file include)
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);
 	
	TipoCausaleModificaVO[] elencoCausaleModifica = (TipoCausaleModificaVO[])request.getAttribute("elencoCausaleModifica");
	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
	StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)session.getAttribute("storicoParticellaVO");
 	Hashtable elencoUnitaArboree = (Hashtable)session.getAttribute("elencoUnitaArboree");
	
	if(storicoParticellaVO != null && elencoUnitaArboree == null) {
		htmpl.set("esito", "A");
		htmpl.set("descComuneUv", storicoParticellaVO.getComuneParticellaVO().getDescom());
		htmpl.set("siglaProvUv", storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia());
		htmpl.set("sezioneUv", storicoParticellaVO.getSezione());
		htmpl.set("foglioUv", storicoParticellaVO.getFoglio());
		htmpl.set("particellaUv", storicoParticellaVO.getParticella());
		htmpl.set("subalternoUv", storicoParticellaVO.getSubalterno());
		htmpl.set("supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
    htmpl.set("superficieGrafica", StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica()));
	}
	else if(storicoParticellaVO != null && elencoUnitaArboree != null) {
		htmpl.set("esito", "S");
		htmpl.set("descComuneUv", storicoParticellaVO.getComuneParticellaVO().getDescom());
		htmpl.set("siglaProvUv", storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia());
		htmpl.set("sezioneUv", storicoParticellaVO.getSezione());
		htmpl.set("foglioUv", storicoParticellaVO.getFoglio());
		htmpl.set("particellaUv", storicoParticellaVO.getParticella());
		htmpl.set("subalternoUv", storicoParticellaVO.getSubalterno());
		htmpl.set("supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
    htmpl.set("superficieGrafica", StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica()));
		// Visualizzo i dati relativi alle unità arboree che desidero importare
		Enumeration enumeraUnitaArboree = elencoUnitaArboree.keys();
		while(enumeraUnitaArboree.hasMoreElements()) {
			htmpl.newBlock("blkElenco");
			Long key = (Long)enumeraUnitaArboree.nextElement();
			StoricoParticellaVO storicoParticellaArboreaVO = (StoricoParticellaVO)elencoUnitaArboree.get(key);
			StoricoUnitaArboreaVO storicoUnitaArboreaVO = storicoParticellaArboreaVO.getStoricoUnitaArboreaVO();
			htmpl.set("blkElenco.idStoricoUnitaArborea", storicoUnitaArboreaVO.getIdStoricoUnitaArborea().toString());
			htmpl.set("blkElenco.descComuneParticellaElenco", storicoParticellaArboreaVO.getComuneParticellaVO().getDescom());
			htmpl.set("blkElenco.siglaProvinciaParticellaElenco", storicoParticellaArboreaVO.getComuneParticellaVO().getSiglaProv());
			htmpl.set("blkElenco.sezioneElenco", storicoParticellaArboreaVO.getSezione());
			htmpl.set("blkElenco.foglioElenco", storicoParticellaArboreaVO.getFoglio());
			htmpl.set("blkElenco.particellaElenco", storicoParticellaArboreaVO.getParticella());
			htmpl.set("blkElenco.subalternoElenco", storicoParticellaArboreaVO.getSubalterno());
			if(storicoUnitaArboreaVO.getIdUtilizzo() != null) {
	 			String codice = "";
				if(Validator.isNotEmpty(storicoUnitaArboreaVO.getTipoUtilizzoVO().getCodice())) {
					codice += "["+storicoUnitaArboreaVO.getTipoUtilizzoVO().getCodice()+"] ";
					htmpl.set("blkElenco.destProduttiva", codice + storicoUnitaArboreaVO.getTipoUtilizzoVO().getDescrizione());
				}
	 		}
			if(storicoUnitaArboreaVO.getIdVarieta() != null) {
	 			htmpl.set("blkElenco.vitigno", "["+storicoUnitaArboreaVO.getTipoVarietaVO().getCodiceVarieta()+"] "+storicoUnitaArboreaVO.getTipoVarietaVO().getDescrizione());
	 		}
			if(Validator.isNotEmpty(storicoUnitaArboreaVO.getArea())) {
	 			htmpl.set("blkElenco.area", StringUtils.parseSuperficieField(storicoUnitaArboreaVO.getArea()));
	 		}
      if(storicoUnitaArboreaVO.getDataImpianto() != null) {
        htmpl.set("blkElenco.dataImpianto", DateUtils.formatDate(storicoUnitaArboreaVO.getDataImpianto()));
      }
      if(storicoUnitaArboreaVO.getDataPrimaProduzione() != null) {
        htmpl.set("blkElenco.dataPrimaProduzione", DateUtils.formatDate(storicoUnitaArboreaVO.getDataPrimaProduzione()));
      }
			htmpl.set("blkElenco.sestoSuFila", storicoUnitaArboreaVO.getSestoSuFila());
			htmpl.set("blkElenco.sestoTraFile", storicoUnitaArboreaVO.getSestoTraFile());
			htmpl.set("blkElenco.percentualeVarieta", storicoUnitaArboreaVO.getPercentualeVarieta());
			if(storicoUnitaArboreaVO.getPresenzaAltriVitigni().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
	 			htmpl.set("blkElenco.altriVitigni", SolmrConstants.FLAG_SI);
	 		}
	 		else {
	 			htmpl.set("blkElenco.altriVitigni", SolmrConstants.FLAG_NO);
	 		}
			if(storicoUnitaArboreaVO.getIdFormaAllevamento() != null) {
	 			htmpl.set("blkElenco.descFormaAllevamento", storicoUnitaArboreaVO.getTipoFormaAllevamentoVO().getDescrizione());
	 		}
		}
		// CAUSALI MODIFICA
		if(elencoCausaleModifica != null && elencoCausaleModifica.length > 0) {
			for(int f = 0; f < elencoCausaleModifica.length; f++) {
				TipoCausaleModificaVO tipoCausaleModificaVO = (TipoCausaleModificaVO)elencoCausaleModifica[f];
				htmpl.newBlock("blkTipiCausaliModifica");
				htmpl.set("blkTipiCausaliModifica.idCausaleModifica", tipoCausaleModificaVO.getIdCausaleModifica().toString());
				htmpl.set("blkTipiCausaliModifica.descrizione", tipoCausaleModificaVO.getDescrizione());
				/*if(storicoUnitaArboreaVO != null && storicoUnitaArboreaVO.getIdCausaleModifica() != null && storicoUnitaArboreaVO.getIdCausaleModifica().compareTo(tipoCausaleModificaVO.getIdCausaleModifica()) == 0) {
					htmpl.set("blkTipiCausaliModifica.selected", "selected=\"selected\"");
				}*/
			}
		}
	}
	
	// Se ci sono errori li visualizzo
	if(errors != null && errors.size() > 0) {
		HtmplUtil.setErrors(htmpl, errors, request, application);
	}

%>
<%= htmpl.text()%>

