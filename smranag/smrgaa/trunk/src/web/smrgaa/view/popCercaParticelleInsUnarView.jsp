<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%

 	java.io.InputStream layout = application.getResourceAsStream("/layout/popCercaParticelleInsUnar.htm");

 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);

 	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 	StoricoParticellaVO[] elencoParticelle = (StoricoParticellaVO[])request.getAttribute("elencoParticelle");
 	String importa = (String)request.getAttribute("importa");
 	if(Validator.isNotEmpty(importa)) {
 		htmpl.set("importa", importa);
 	}
 	// Se ci sono errori li visualizzo
 	if(Validator.isNotEmpty(messaggioErrore) || elencoParticelle.length == 0) {
   		htmpl.newBlock("blkNoParticelle");
   		if(Validator.isNotEmpty(messaggioErrore)) {
	   		htmpl.set("blkNoParticelle.messaggioErrore", messaggioErrore);
   		}
   		else {
   			htmpl.set("blkNoParticelle.messaggioErrore", AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND);
   		}
 	}
 	// Altrimenti visualizzo l'elenco delle particelle trovate
 	else {
     	htmpl.newBlock("blkParticelle");
     	for(int i = 0; i < elencoParticelle.length; i++) {
       		htmpl.newBlock("blkParticelle.blkElencoParticelle");
       		StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)elencoParticelle[i];
       		htmpl.set("blkParticelle.blkElencoParticelle.idStoricoParticella", storicoParticellaVO.getIdStoricoParticella().toString());
       		htmpl.set("blkParticelle.blkElencoParticelle.descComune", storicoParticellaVO.getComuneParticellaVO().getDescom());
       		htmpl.set("blkParticelle.blkElencoParticelle.siglaProvinciaParticella", storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia());
         	htmpl.set("blkParticelle.blkElencoParticelle.sezione", storicoParticellaVO.getSezione());
       		htmpl.set("blkParticelle.blkElencoParticelle.foglio", storicoParticellaVO.getFoglio());
         	htmpl.set("blkParticelle.blkElencoParticelle.particella", storicoParticellaVO.getParticella());
	       	htmpl.set("blkParticelle.blkElencoParticelle.subalterno", storicoParticellaVO.getSubalterno());
       		htmpl.set("blkParticelle.blkElencoParticelle.supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
          htmpl.set("blkParticelle.blkElencoParticelle.superficieGrafica", StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica()));
     	}
 	}

%>
<%= htmpl.text()%>
