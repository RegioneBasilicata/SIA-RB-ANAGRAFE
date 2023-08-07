<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.attestazioni.*" %>
<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.etc.anag.*"%>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/attestazioniModifica.htm");
 	Htmpl htmpl = new Htmpl(layout);
 	
 	%>
		<%@include file = "/view/remoteInclude.inc" %>
	<%

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);
 	htmpl.set("headMenuScroll", headMenuScroll, null);
  htmpl.set("headMenuScrollIE6", headMenuScrollIE6, null);

 	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
	TipoAttestazioneVO[] elencoAttestazioni = (TipoAttestazioneVO[])request.getAttribute("elencoAttestazioni");
	Hashtable elencoErrori = (Hashtable)request.getAttribute("elencoErrori");
	Boolean reloadValue = (Boolean)request.getAttribute("reload");
	boolean reload = false;
	if(reloadValue != null) {
		reload = true;
	}
	Hashtable elencoIdSelezionati = (Hashtable)request.getAttribute("elencoIdSelezionati");
 	
 	
 	// Se si è verificato qualche errore visualizzo il messaggio all'utente
 	if(Validator.isNotEmpty(messaggioErrore)) {
 		htmpl.newBlock("blkErrore");
 		htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
 	}
 	// Altrimenti estraggo i dati
 	else {
 		htmpl.newBlock("blkDati");
 		// Elenco attestazioni
 		if(elencoAttestazioni.length > 0) {
	 		for(int i = 0; i < elencoAttestazioni.length; i++) {
	 			TipoAttestazioneVO tipoAttestazioneVO = elencoAttestazioni[i];
	 			htmpl.newBlock("blkDati.blkElencoAttestazioni");
	 			if(tipoAttestazioneVO.getIdAttestazionePadre().compareTo(SolmrConstants.ID_ATTESTAZIONE_PADRE_INTESTAZIONE) == 0) {
	 				htmpl.newBlock("blkDati.blkElencoAttestazioni.blkTitoloTable");
	 				htmpl.set("blkDati.blkElencoAttestazioni.blkTitoloTable.descrizione", tipoAttestazioneVO.getDescrizione());
	 			}
	 			else {
		 			if(tipoAttestazioneVO.getNumeroColonneRiga().equalsIgnoreCase(SolmrConstants.NUMERO_RIGHE_INTESTAZIONE_ATTESTAZIONE)) {
		 				if(tipoAttestazioneVO.getTipoCarattere().equalsIgnoreCase(SolmrConstants.TIPO_ATTESTAZIONE_CARATTERE_BOLD)) {
		 					htmpl.newBlock("blkDati.blkElencoAttestazioni.blkGruppoBold");
		 					htmpl.set("blkDati.blkElencoAttestazioni.blkGruppoBold.descrizione", tipoAttestazioneVO.getDescrizione());
		 				}
		 				else {
		 					htmpl.newBlock("blkDati.blkElencoAttestazioni.blkGruppoNormal");
		 					htmpl.set("blkDati.blkElencoAttestazioni.blkGruppoNormal.descrizione", tipoAttestazioneVO.getDescrizione());
		 				}
		 			}
		 			else if(tipoAttestazioneVO.getNumeroColonneRiga().equalsIgnoreCase(SolmrConstants.NUMERO_RIGHE_MONO_ATTESTAZIONE)) {
		 				htmpl.newBlock("blkDati.blkElencoAttestazioni.blkCorrelataMono");
		 				if(tipoAttestazioneVO.getTipoRiga().equalsIgnoreCase(SolmrConstants.TIPO_RIGHE_ATTESTAZIONE_CHECKBOX)) {
		 					htmpl.newBlock("blkDati.blkElencoAttestazioni.blkCorrelataMono.blkCheck");
		 					htmpl.set("blkDati.blkElencoAttestazioni.blkCorrelataMono.blkCheck.valueIdAttestazione", tipoAttestazioneVO.getIdAttestazione().toString());
		 					if(tipoAttestazioneVO.getDisabilitato().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
	 							htmpl.set("blkDati.blkElencoAttestazioni.blkCorrelataMono.blkCheck.disabledMono", "disabled=\"disabled\"");
	 						}
		 					if(tipoAttestazioneVO.isAttestazioneAzienda()) {
		 						htmpl.set("blkDati.blkElencoAttestazioni.blkCorrelataMono.blkCheck.checked", "checked=\"checked\"");
		 						if(tipoAttestazioneVO.getDisabilitato().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
		 							htmpl.newBlock("blkDati.blkElencoAttestazioni.blkCorrelataMono.blkCheck.blkHidden");
		 							htmpl.set("blkDati.blkElencoAttestazioni.blkCorrelataMono.blkCheck.blkHidden.valueIdAttestazione", tipoAttestazioneVO.getIdAttestazione().toString());
		 						}
		 					}
		 					else {
		 						if(reload && elencoIdSelezionati != null && elencoIdSelezionati.size() > 0) {
		 							Long idAtt = (Long)elencoIdSelezionati.get(tipoAttestazioneVO.getIdAttestazione());
		 							if(idAtt != null && idAtt.compareTo(tipoAttestazioneVO.getIdAttestazione()) == 0) {
		 								htmpl.set("blkDati.blkElencoAttestazioni.blkCorrelataMono.blkCheck.checked", "checked=\"checked\"");		 								
		 							}
		 						}
		 					}
		 				}
		 				else if(!tipoAttestazioneVO.getTipoRiga().equalsIgnoreCase(SolmrConstants.FLAG_N)) {
		 					htmpl.newBlock("blkDati.blkElencoAttestazioni.blkCorrelataMono.blkLabel");
		 					htmpl.set("blkDati.blkElencoAttestazioni.blkCorrelataMono.blkLabel.tipoRiga", tipoAttestazioneVO.getTipoRiga());
		 				}
		 				htmpl.set("blkDati.blkElencoAttestazioni.blkCorrelataMono.descrizione", HtmplUtil.creaLayoutForAttestazioni(tipoAttestazioneVO.getParametriAttAziendaVO(), null, new Long(0), tipoAttestazioneVO, tipoAttestazioneVO.getTipoParametriAttestazioneVO(), elencoErrori, pathErrori, reload, request), null);
		 			}
		 			else if(tipoAttestazioneVO.getNumeroColonneRiga().equalsIgnoreCase(SolmrConstants.NUMERO_RIGHE_DOUBLE_ATTESTAZIONE)) {
		 				htmpl.newBlock("blkDati.blkElencoAttestazioni.blkCorrelataDoppia");
		 				if(tipoAttestazioneVO.getTipoRiga().equalsIgnoreCase(SolmrConstants.TIPO_RIGHE_ATTESTAZIONE_CHECKBOX)) {
		 					htmpl.newBlock("blkDati.blkElencoAttestazioni.blkCorrelataDoppia.blkCheck");
		 					htmpl.set("blkDati.blkElencoAttestazioni.blkCorrelataDoppia.blkCheck.idAttestazionePadre", tipoAttestazioneVO.getIdAttestazionePadre().toString());
		 					if(tipoAttestazioneVO.isAttestazioneAzienda()) {
		 						htmpl.set("blkDati.blkElencoAttestazioni.blkCorrelataDoppia.blkCheck.checked", "checked=\"checked\"");
		 					}
		 				}
		 				else if(!tipoAttestazioneVO.getTipoRiga().equalsIgnoreCase(SolmrConstants.FLAG_N)) {
		 					htmpl.newBlock("blkDati.blkElencoAttestazioni.blkCorrelataDoppia.blkLabel");
		 					htmpl.set("blkDati.blkElencoAttestazioni.blkCorrelataDoppia.blkLabel.tipoRiga", tipoAttestazioneVO.getTipoRiga());
		 				}
		 				htmpl.set("blkDati.blkElencoAttestazioni.blkCorrelataDoppia.descrizione", tipoAttestazioneVO.getDescrizione());
		 			}
	 			}
	 		}
 		}
 		else {
 			htmpl.newBlock("blkDati.blkErrore");
 	 		htmpl.set("blkDati.blkErrore.messaggioErrore", AnagErrors.ERR_NO_ATTESTAZIONI_FOUND);
 		}
 	}
 	

%>
<%= htmpl.text()%>
