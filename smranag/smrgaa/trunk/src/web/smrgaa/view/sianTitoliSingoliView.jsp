<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.sian.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.util.*" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/titoliSingoli.htm");
	Htmpl htmpl = new Htmpl(layout);

	%>
   		<%@include file = "/view/remoteInclude.inc" %>
	<%

	SianTitoloRispostaVO sianTitoloRispostaVO = (SianTitoloRispostaVO)session.getAttribute("sianTitoloRispostaVO");
	Vector elencoDescTipoTitolo = (Vector)request.getAttribute("elencoDescTipoTitolo");
	Vector elencoDescOrigineTitolo = (Vector)request.getAttribute("elencoDescOrigineTitolo");
	Vector elencoDescMovimento = (Vector)request.getAttribute("elencoDescMovimento");

	// Nuova gestione fogli di stile
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);

	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
	if(Validator.isNotEmpty(messaggioErrore)) {
  		htmpl.newBlock("blkErrore");
  		htmpl.set("blkErrore.messaggio", messaggioErrore);
	}
	else {
		if(sianTitoloRispostaVO == null || sianTitoloRispostaVO.getRisposta() == null || sianTitoloRispostaVO.getRisposta().length == 0) {
			htmpl.newBlock("blkErrore");
	  		htmpl.set("blkErrore.messaggio", AnagErrors.ERRORE_SIAN_NO_TITOLI);
		}
  		else  {
  			htmpl.newBlock("blkTitoli");
  			htmpl.set("blkTitoli.numeroTotTitoli", String.valueOf(sianTitoloRispostaVO.getRisposta().length));
  			double valTitolo = 0;
  			double valSuperficie = 0;
    		for(int i = 0; i < sianTitoloRispostaVO.getRisposta().length; i++) {
      			htmpl.newBlock("blkTitoli.blkElencoTitoli");
		      	SianTitoloVO sianTitoloVO = (SianTitoloVO)sianTitoloRispostaVO.getRisposta()[i];
  		      	htmpl.set("blkTitoli.blkElencoTitoli.identificativo", sianTitoloVO.getIdentificativo());
  		      	if(Validator.isNotEmpty(sianTitoloVO.getValoreTitolo())) {
			    	htmpl.set("blkTitoli.blkElencoTitoli.valoreTitolo", StringUtils.parseEuroField(sianTitoloVO.getValoreTitolo()));
			    	valTitolo += Double.parseDouble(StringUtils.parseEuroField(sianTitoloVO.getValoreTitolo()).replace(',', '.'));
  		      	}
  		      	if(Validator.isNotEmpty(sianTitoloVO.getSuperficie())) {
  		      		double sup = Double.parseDouble(sianTitoloVO.getSuperficie());
		      		htmpl.set("blkTitoli.blkElencoTitoli.superficie", StringUtils.parseSuperficieField(String.valueOf(sup)));
		      		valSuperficie += Double.parseDouble(StringUtils.parseSuperficieField(String.valueOf(sup)).replace(',', '.'));
  		      	}
  		      	if(Validator.isNotEmpty(sianTitoloVO.getUBAobbligatori())) {
		      		htmpl.set("blkTitoli.blkElencoTitoli.UBAobbligatori", StringUtils.parseDoubleField(sianTitoloVO.getUBAobbligatori()));
  		      	}
		      	htmpl.set("blkTitoli.blkElencoTitoli.tipoTitolo", sianTitoloVO.getTipoTitolo());
		      	htmpl.set("blkTitoli.blkElencoTitoli.origine", sianTitoloVO.getOrigine());
		      	htmpl.set("blkTitoli.blkElencoTitoli.tipoMovimento", sianTitoloVO.getTipoMovimento());
		      	if(Validator.isNotEmpty(sianTitoloVO.getDataMovimento())) {
		      		String dataMovimento = StringUtils.parseDateFieldToEuropeStandard(SolmrConstants.DATE_AMERICAN_FORMAT, SolmrConstants.DATE_EUROPEAN_STANDARD_FORMAT, sianTitoloVO.getDataMovimento());
		      		if(!dataMovimento.equalsIgnoreCase(SolmrConstants.ORACLE_FINAL_DATE)) {
		      			htmpl.set("blkTitoli.blkElencoTitoli.dataMovimento", dataMovimento);
		      		}
		      	}
		      	if(Validator.isNotEmpty(sianTitoloVO.getCodiceValidazioneMovimento())) {
		      		if(sianTitoloVO.getCodiceValidazioneMovimento().equalsIgnoreCase(String.valueOf(SolmrConstants.CODICE_ATTESA_VALIDAZIONE_SIAN))) {
		      			htmpl.set("blkTitoli.blkElencoTitoli.codiceValidazioneMovimento", SolmrConstants.DESCRIZIONE_ATTESA_VALIDAZIONE_SIAN);
		      		}
		      		else if(sianTitoloVO.getCodiceValidazioneMovimento().equalsIgnoreCase(String.valueOf(SolmrConstants.CODICE_RESPINTO_SIAN))) {
		      			htmpl.set("blkTitoli.blkElencoTitoli.codiceValidazioneMovimento", SolmrConstants.DESCRIZIONE_RESPINTO_SIAN);
		      		}
		      		else if(sianTitoloVO.getCodiceValidazioneMovimento().equalsIgnoreCase(String.valueOf(SolmrConstants.CODICE_VALIDATO_SIAN))) {
		      			htmpl.set("blkTitoli.blkElencoTitoli.codiceValidazioneMovimento", SolmrConstants.DESCRIZIONE_VALIDATO_SIAN);
		      		}
		      	}
		      	if(Validator.isNotEmpty(sianTitoloVO.getDataUltimoUtilizzo())) {
		      		String dataUltimoUtilizzo = StringUtils.parseDateFieldToEuropeStandard(SolmrConstants.DATE_AMERICAN_FORMAT, SolmrConstants.DATE_EUROPEAN_STANDARD_FORMAT, sianTitoloVO.getDataUltimoUtilizzo());
		      		if(!dataUltimoUtilizzo.equalsIgnoreCase(SolmrConstants.ORACLE_FINAL_DATE)) {
		      			htmpl.set("blkTitoli.blkElencoTitoli.dataUltimoUtilizzo", dataUltimoUtilizzo);
		      		}
		      	}
		      	if(Validator.isNotEmpty(sianTitoloVO.getDataFinePossesso())) {
		      		String dataFinePossesso = StringUtils.parseDateFieldToEuropeStandard(SolmrConstants.DATE_AMERICAN_FORMAT, SolmrConstants.DATE_EUROPEAN_STANDARD_FORMAT, sianTitoloVO.getDataFinePossesso());
		      		if(!dataFinePossesso.equalsIgnoreCase(SolmrConstants.ORACLE_FINAL_DATE)) {
		      			htmpl.set("blkTitoli.blkElencoTitoli.dataFinePossesso", dataFinePossesso);
		      		}
		      	}
		      	if(Validator.isNotEmpty(sianTitoloVO.getSALtitolo())) {
		      		if(sianTitoloVO.getSALtitolo().equalsIgnoreCase(String.valueOf(SolmrConstants.SAL_CODICE_TITOLO_DEFINITIVO_SIAN))) {
		      			htmpl.set("blkTitoli.blkElencoTitoli.SALtitolo", SolmrConstants.SAL_DESCRIZIONE_TITOLO_DEFINITIVO_SIAN);
		      		}
		      		else if(sianTitoloVO.getSALtitolo().equalsIgnoreCase(String.valueOf(SolmrConstants.SAL_CODICE_TITOLO_PROVVISORIO_SIAN))) {
		      			htmpl.set("blkTitoli.blkElencoTitoli.SALtitolo", SolmrConstants.SAL_DESCRIZIONE_TITOLO_PROVVISORIO_SIAN);
		      		}
		      	}
		      	htmpl.set("blkTitoli.blkElencoTitoli.cuaaProprietario", sianTitoloVO.getCuaaProprietario());
		      	htmpl.set("blkTitoli.blkElencoTitoli.cuaaSoccidario", sianTitoloVO.getCUAASoccidario());
		      	if(Validator.isNotEmpty(sianTitoloVO.getIdTitoloFrazionato())) {
		      		htmpl.set("blkTitoli.blkElencoTitoli.idTitoloFrazionato", sianTitoloVO.getIdTitoloFrazionato().toString());
		      	}
		      	if(Validator.isNotEmpty(sianTitoloVO.getZonaPrimoUtilizzo())) {
		      		htmpl.set("blkTitoli.blkElencoTitoli.zonaPrimoUtilizzo", sianTitoloVO.getZonaPrimoUtilizzo());
		      	}
		      	htmpl.set("blkTitoli.blkElencoTitoli.campagnaInizioVali", sianTitoloVO.getCampagnaInizioVali());
		      	if(Validator.isNotEmpty(sianTitoloVO.getCampagnaFineVali())) {
		      		if(!sianTitoloVO.getCampagnaFineVali().equalsIgnoreCase(SolmrConstants.ORACLE_DEFAULT_YEAR)) {
		      			htmpl.set("blkTitoli.blkElencoTitoli.campagnaFineVali", sianTitoloVO.getCampagnaFineVali());
		      		}
		      	}
    		}
    		// TOTALI
    		htmpl.set("blkTitoli.totTitolo", StringUtils.parseEuroField(String.valueOf(valTitolo)));
    		htmpl.set("blkTitoli.totSuperficie", StringUtils.parseSuperficieField(String.valueOf(valSuperficie)));
   	 		// LEGENDA
   	 		if(elencoDescTipoTitolo != null) {
      			for(int i = 0; i < elencoDescTipoTitolo.size(); i++) {
        			htmpl.newBlock("blkTitoli.blkLegendaTipoTitoli");
        			StringcodeDescription stringCode = (StringcodeDescription)elencoDescTipoTitolo.elementAt(i);
        			htmpl.set("blkTitoli.blkLegendaTipoTitoli.codiceTipoTitolo", stringCode.getCode());
        			htmpl.set("blkTitoli.blkLegendaTipoTitoli.descrizioneTipoTitolo", stringCode.getDescription());
      			}
    		}
    		if(elencoDescOrigineTitolo != null) {
      			for(int i = 0; i < elencoDescOrigineTitolo.size(); i++) {
        			htmpl.newBlock("blkTitoli.blkLegendaTitoli");
        			StringcodeDescription stringCode = (StringcodeDescription)elencoDescOrigineTitolo.elementAt(i);
        			htmpl.set("blkTitoli.blkLegendaTitoli.codiceOrigine", stringCode.getCode());
        			htmpl.set("blkTitoli.blkLegendaTitoli.descrizioneCodiceOrigine", stringCode.getDescription());
      			}
    		}
    		if(elencoDescMovimento != null) {
      			for(int i = 0; i < elencoDescMovimento.size(); i++) {
        			htmpl.newBlock("blkTitoli.blkLegendaMovimento");
        			StringcodeDescription stringCode = (StringcodeDescription)elencoDescMovimento.elementAt(i);
        			htmpl.set("blkTitoli.blkLegendaMovimento.codiceMovim", stringCode.getCode());
        			htmpl.set("blkTitoli.blkLegendaMovimento.descrizioneCodiceMovim", stringCode.getDescription());
      			}
    		}
  		}
	}


%>
<%= htmpl.text()%>
