<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@page import="it.csi.solmr.dto.anag.terreni.*"%>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popTerreniElencoUnar.htm");
 	Htmpl htmpl = new Htmpl(layout);

	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%


 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);
	
 	FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)session.getAttribute("filtriParticellareRicercaVO");
	StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)request.getAttribute("storicoParticellaVO");
	double totArea = 0;
	
 	htmpl.set("descComuneParticella", storicoParticellaVO.getComuneParticellaVO().getDescom());
 	htmpl.set("siglaProvinciaParticella", "("+storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia()+")");
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

 	String messaggio = (String)request.getAttribute("messaggio");
 	Object[] elencoUnitaArboree = (Object[])request.getAttribute("elencoUnitaArboree");

 	// Non sono state trovate unita arboree
 	if(Validator.isNotEmpty(messaggio) || (elencoUnitaArboree == null || elencoUnitaArboree.length == 0)) {
   		htmpl.newBlock("blkNoUnitaArboree");
   		htmpl.set("blkNoUnitaArboree.messaggio", messaggio);
 	}
 	else {
   		htmpl.newBlock("blkUnitaArboree");
   		for(int i = 0; i < elencoUnitaArboree.length; i++) {
     		htmpl.newBlock("blkUnitaArboree.blkElenco");
     		if(filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() <= 0) {
	     		StoricoUnitaArboreaVO storicoUnitaArboreaVO = (StoricoUnitaArboreaVO)elencoUnitaArboree[i];
	     		if(storicoUnitaArboreaVO.getIdTipologiaUnar() != null) {
	     			htmpl.set("blkUnitaArboree.blkElenco.descTipologiaUnar", storicoUnitaArboreaVO.getTipoTipologiaUnitaArboreaVO().getDescrizione());
	     		}
	     		htmpl.set("blkUnitaArboree.blkElenco.progrUnar", storicoUnitaArboreaVO.getProgrUnar());
	     		htmpl.set("blkUnitaArboree.blkElenco.sestoSuFila", storicoUnitaArboreaVO.getSestoSuFila());
	     		htmpl.set("blkUnitaArboree.blkElenco.sestoTraFile", storicoUnitaArboreaVO.getSestoTraFile());
	     		htmpl.set("blkUnitaArboree.blkElenco.numCeppi", storicoUnitaArboreaVO.getNumCeppi());
	     		htmpl.set("blkUnitaArboree.blkElenco.annoImpianto", storicoUnitaArboreaVO.getAnnoImpianto());
	     		if(storicoUnitaArboreaVO.getIdFormaAllevamento() != null) {
	     			htmpl.set("blkUnitaArboree.blkElenco.descFormaAllevamento", storicoUnitaArboreaVO.getTipoFormaAllevamentoVO().getDescrizione());
	     		}
	     		if(Validator.isNotEmpty(storicoUnitaArboreaVO.getArea())) {
	     			htmpl.set("blkUnitaArboree.blkElenco.area", StringUtils.parseSuperficieField(storicoUnitaArboreaVO.getArea()));
	     			totArea += Double.valueOf(storicoUnitaArboreaVO.getArea().replace(',', '.')).doubleValue();
	     		}
	     		if(storicoUnitaArboreaVO.getIdUtilizzo() != null) {
	     			String codice = "";
					if(Validator.isNotEmpty(storicoUnitaArboreaVO.getTipoUtilizzoVO().getCodice())) {
						codice += "["+storicoUnitaArboreaVO.getTipoUtilizzoVO().getCodice()+"] ";
						htmpl.set("blkUnitaArboree.blkElenco.destProduttiva", codice + storicoUnitaArboreaVO.getTipoUtilizzoVO().getDescrizione());
					}
	     		}
	     		if(storicoUnitaArboreaVO.getIdVarieta() != null) {
	     			htmpl.set("blkUnitaArboree.blkElenco.varieta", "["+storicoUnitaArboreaVO.getTipoVarietaVO().getCodiceVarieta()+"] "+storicoUnitaArboreaVO.getTipoVarietaVO().getDescrizione());
	     		}
	     		htmpl.set("blkUnitaArboree.blkElenco.percentualeVarieta", storicoUnitaArboreaVO.getPercentualeVarieta());
	     		if(storicoUnitaArboreaVO.getPresenzaAltriVitigni().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
	     			htmpl.set("blkUnitaArboree.blkElenco.altriVitigni", SolmrConstants.FLAG_SI);
	     		}
	     		else {
	     			htmpl.set("blkUnitaArboree.blkElenco.altriVitigni", SolmrConstants.FLAG_NO);
	     		}
     		}
     		else {
     			UnitaArboreaDichiarataVO unitaArboreaDichiarataVO = (UnitaArboreaDichiarataVO)elencoUnitaArboree[i];
     			if(unitaArboreaDichiarataVO.getIdTipologiaUnar() != null) {
	     			htmpl.set("blkUnitaArboree.blkElenco.descTipologiaUnar", unitaArboreaDichiarataVO.getTipoTipologiaUnitaArboreaVO().getDescrizione());
	     		}
	     		htmpl.set("blkUnitaArboree.blkElenco.progrUnar", unitaArboreaDichiarataVO.getProgrUnar());
	     		htmpl.set("blkUnitaArboree.blkElenco.sestoSuFila", unitaArboreaDichiarataVO.getSestoSuFila());
	     		htmpl.set("blkUnitaArboree.blkElenco.sestoTraFile", unitaArboreaDichiarataVO.getSestoTraFile());
	     		htmpl.set("blkUnitaArboree.blkElenco.numCeppi", unitaArboreaDichiarataVO.getNumCeppi());
	     		htmpl.set("blkUnitaArboree.blkElenco.annoImpianto", unitaArboreaDichiarataVO.getAnnoImpianto());
	     		if(unitaArboreaDichiarataVO.getIdFormaAllevamento() != null) {
	     			htmpl.set("blkUnitaArboree.blkElenco.descFormaAllevamento", unitaArboreaDichiarataVO.getTipoFormaAllevamentoVO().getDescrizione());
	     		}
	     		if(Validator.isNotEmpty(unitaArboreaDichiarataVO.getArea())) {
	     			htmpl.set("blkUnitaArboree.blkElenco.area", StringUtils.parseSuperficieField(unitaArboreaDichiarataVO.getArea()));
	     			totArea += Double.valueOf(unitaArboreaDichiarataVO.getArea().replace(',', '.')).doubleValue();
	     		}
	     		if(unitaArboreaDichiarataVO.getIdUtilizzo() != null) {
	     			String codice = "";
					if(Validator.isNotEmpty(unitaArboreaDichiarataVO.getTipoUtilizzoVO().getCodice())) {
						codice += "["+unitaArboreaDichiarataVO.getTipoUtilizzoVO().getCodice()+"] ";
						htmpl.set("blkUnitaArboree.blkElenco.destProduttiva", codice + unitaArboreaDichiarataVO.getTipoUtilizzoVO().getDescrizione());
					}
	     		}
	     		if(unitaArboreaDichiarataVO.getIdVarieta() != null) {
	     			htmpl.set("blkUnitaArboree.blkElenco.varieta", "["+unitaArboreaDichiarataVO.getTipoVarietaVO().getCodiceVarieta()+"] "+unitaArboreaDichiarataVO.getTipoVarietaVO().getDescrizione());
	     		}
	     		htmpl.set("blkUnitaArboree.blkElenco.percentualeVarieta", unitaArboreaDichiarataVO.getPercentualeVarieta());
	     		if(unitaArboreaDichiarataVO.getPresenzaAltriVitigni().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
	     			htmpl.set("blkUnitaArboree.blkElenco.altriVitigni", SolmrConstants.FLAG_SI);
	     		}
	     		else {
	     			htmpl.set("blkUnitaArboree.blkElenco.altriVitigni", SolmrConstants.FLAG_NO);
	     		}
     		}
   		}
		// Inserisco il totale delle aree
		htmpl.set("blkUnitaArboree.totArea", StringUtils.parseSuperficieField(String.valueOf(totArea)));
 	}


%>
<%= htmpl.text()%>
