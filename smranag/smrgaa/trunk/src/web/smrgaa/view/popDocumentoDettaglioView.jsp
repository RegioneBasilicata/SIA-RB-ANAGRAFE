<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@page import="it.csi.solmr.dto.anag.terreni.*"%>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popDocumentoDettaglio.htm");
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
 	if(storicoParticellaVO.getComuneParticellaVO().getProvinciaVO() != null) {
 		htmpl.set("siglaProvinciaParticella", "("+storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia()+")");
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
	if(filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() <= 0) 
	{
		ConduzioneParticellaVO conduzioneParticellaVO = (ConduzioneParticellaVO)storicoParticellaVO.getElencoConduzioni()[0];
		htmpl.set("idTitoloPossesso", conduzioneParticellaVO.getIdTitoloPossesso().toString());
		htmpl.set("superficieCondotta", StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieCondotta()));
		isCurrent = true;
	}
	else 
	{
		ConduzioneDichiarataVO conduzioneDichiarataVO = (ConduzioneDichiarataVO)storicoParticellaVO.getElencoConduzioniDichiarate()[0];
		htmpl.set("idTitoloPossesso", conduzioneDichiarataVO.getIdTitoloPossesso().toString());
		htmpl.set("superficieCondotta", StringUtils.parseSuperficieField(conduzioneDichiarataVO.getSuperficieCondotta()));		
	}
	
 	String messaggio = (String)request.getAttribute("messaggio");
 	Vector elencoDocumenti = (Vector)request.getAttribute("elencoDocumenti");

 	// Non sono state trovate anomalie
 	if(Validator.isNotEmpty(messaggio) || (elencoDocumenti == null || elencoDocumenti.size() == 0)) 
 	{
   	htmpl.newBlock("blkNoDocumenti");
   	htmpl.set("blkNoDocumenti.messaggio", messaggio);
 	}
 	else 
  {
 		htmpl.newBlock("blkElencoDocumenti");
 		Iterator iteraDocumenti = elencoDocumenti.iterator();
 		int count = 1;
 		while(iteraDocumenti.hasNext()) 
    {
   		htmpl.newBlock("blkElencoDocumenti.blkDatiDocumento");
   		htmpl.set("blkElencoDocumenti.blkDatiDocumento.count", String.valueOf(count));
   		count++;
   		DocumentoVO documentoVO = (DocumentoVO)iteraDocumenti.next();
   		htmpl.set("blkElencoDocumenti.blkDatiDocumento.descTipoDocumento", documentoVO.getTipoDocumentoVO().getDescrizione());
   		htmpl.set("blkElencoDocumenti.blkDatiDocumento.dataInizioValidita", DateUtils.formatDate(documentoVO.getDataInizioValidita()));
   		if(Validator.isNotEmpty(documentoVO.getDataFineValidita())) 
   		{
   			htmpl.set("blkElencoDocumenti.blkDatiDocumento.dataFineValidita", DateUtils.formatDate(documentoVO.getDataFineValidita()));
   		}
   		if(Validator.isNotEmpty(documentoVO.getNumeroProtocollo())) 
   		{
   			htmpl.set("blkElencoDocumenti.blkDatiDocumento.numeroProtocollo", StringUtils.parseNumeroProtocolloField(documentoVO.getNumeroProtocollo()));
   		}
   		if(Validator.isNotEmpty(documentoVO.getDataProtocollo())) 
   		{
   			htmpl.set("blkElencoDocumenti.blkDatiDocumento.dataProtocollo", DateUtils.formatDate(documentoVO.getDataProtocollo()));
   		}
   		Vector elencoProprietari = documentoVO.getElencoProprietari();
   		if(elencoProprietari != null && elencoProprietari.size() > 0) 
   		{
     		htmpl.newBlock("blkElencoDocumenti.blkDatiDocumento.blkEtichettaProp");
   			Iterator iteraProprietari = elencoProprietari.iterator();
     		while(iteraProprietari.hasNext()) 
     		{
     			htmpl.newBlock("blkElencoDocumenti.blkDatiDocumento.blkEtichettaProp.blkElencoProprietari");
			    DocumentoProprietarioVO documentoProprietarioVO = (DocumentoProprietarioVO)iteraProprietari.next();
			    htmpl.set("blkElencoDocumenti.blkDatiDocumento.blkEtichettaProp.blkElencoProprietari.codiceFiscale", documentoProprietarioVO.getCuaa());
			    htmpl.set("blkElencoDocumenti.blkDatiDocumento.blkEtichettaProp.blkElencoProprietari.denominazioneProprietario", documentoProprietarioVO.getDenominazione());
     		}
   		}
   		Vector elencoParticelle = documentoVO.getElencoParticelle();
   		if(elencoParticelle != null && elencoParticelle.size() > 0) 
   		{
     		htmpl.newBlock("blkElencoDocumenti.blkDatiDocumento.blkLabelPart");
   			Iterator iteraParticelle = elencoParticelle.iterator();
     		while(iteraParticelle.hasNext()) 
     		{
     			htmpl.newBlock("blkElencoDocumenti.blkDatiDocumento.blkLabelPart.blkElencoParticelle");
			    StoricoParticellaVO datiStoricoParticellaVO = (StoricoParticellaVO)iteraParticelle.next();
			    htmpl.set("blkElencoDocumenti.blkDatiDocumento.blkLabelPart.blkElencoParticelle.descComuneParticella", datiStoricoParticellaVO.getComuneParticellaVO().getDescom());
			    if(datiStoricoParticellaVO.getComuneParticellaVO().getProvinciaVO() != null) 
			    {
				    htmpl.set("blkElencoDocumenti.blkDatiDocumento.blkLabelPart.blkElencoParticelle.siglaProvinciaParticella", "("+datiStoricoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia()+")");
			    }
			    if(Validator.isNotEmpty(datiStoricoParticellaVO.getSezione())) 
			    {
			 		htmpl.set("blkElencoDocumenti.blkDatiDocumento.blkLabelPart.blkElencoParticelle.sezione", datiStoricoParticellaVO.getSezione());
				 	}
				 	htmpl.set("blkElencoDocumenti.blkDatiDocumento.blkLabelPart.blkElencoParticelle.foglio", datiStoricoParticellaVO.getFoglio());
				 	if(Validator.isNotEmpty(datiStoricoParticellaVO.getParticella())) 
				 	{
				 		htmpl.set("blkElencoDocumenti.blkDatiDocumento.blkLabelPart.blkElencoParticelle.particella", datiStoricoParticellaVO.getParticella());
				 	}
				 	if(Validator.isNotEmpty(datiStoricoParticellaVO.getSubalterno())) 
				 	{
				 		htmpl.set("blkElencoDocumenti.blkDatiDocumento.blkLabelPart.blkElencoParticelle.subalterno", datiStoricoParticellaVO.getSubalterno());
				 	}
				 	htmpl.set("blkElencoDocumenti.blkDatiDocumento.blkLabelPart.blkElencoParticelle.supCatastale", StringUtils.parseSuperficieField(datiStoricoParticellaVO.getSupCatastale()));			 	
				 	ConduzioneParticellaVO conduzioneParticellaVO = (ConduzioneParticellaVO)datiStoricoParticellaVO.getElencoConduzioni()[0];
					htmpl.set("blkElencoDocumenti.blkDatiDocumento.blkLabelPart.blkElencoParticelle.idTitoloPossesso", conduzioneParticellaVO.getIdTitoloPossesso().toString());
					htmpl.set("blkElencoDocumenti.blkDatiDocumento.blkLabelPart.blkElencoParticelle.supCondotta", StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieCondotta()));
					if(Validator.isNotEmpty(conduzioneParticellaVO.getElencoDocumentoConduzione()) && Validator.isNotEmpty(conduzioneParticellaVO.getElencoDocumentoConduzione()[0]))
					{
					  htmpl.set("blkElencoDocumenti.blkDatiDocumento.blkLabelPart.blkElencoParticelle.dataFineValidita", 
					    DateUtils.formatDateNotNull(conduzioneParticellaVO.getElencoDocumentoConduzione()[0].getDataFineValidita()));
					}
     		}
   		}
 		}
    
    //visualizzo solo se sono in un piano dichiarazione consistenza
    if(filtriParticellareRicercaVO.getIdPianoRiferimento() > 0)
    {
      htmpl.newBlock("blkElencoDocumenti.blkLegenda");
    }
 	}


%>
<%= htmpl.text()%>
