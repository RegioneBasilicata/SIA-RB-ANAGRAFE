<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.attestazioni.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.etc.anag.*"%>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.*" %>
<%@ page import="java.text.*" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/dichiarazioneConsistenzaAllegatiMod.htm");
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
 	//ConsistenzaVO consistenzaVO = (ConsistenzaVO)request.getAttribute("consistenzaVO");
 	Hashtable elencoErrori = (Hashtable)request.getAttribute("elencoErrori");
 	Long idDichiarazioneConsistenza = (Long)request.getAttribute("idDichiarazioneConsistenza");
	ConsistenzaVO consistenzaVO = (ConsistenzaVO)request.getAttribute("consistenzaVO");
  AllegatoDichiarazioneVO allegatoDichiarazioneVO = (AllegatoDichiarazioneVO)request.getAttribute("allegatoDichiarazioneVO");
  
	Vector<TipoAttestazioneVO> elencoAttestazioni = (Vector<TipoAttestazioneVO>)request.getAttribute("elencoAttestazioni");
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
 	else 
  {
  
    htmpl.set("idDichiarazioneConsistenza", consistenzaVO.getIdDichiarazioneConsistenza());
    htmpl.set("idTipoAllegato", request.getParameter("idTipoAllegato"));
	  htmpl.set("anno", consistenzaVO.getAnno());
	  htmpl.set("data", new SimpleDateFormat(DateUtils.DATA_ORA).format(consistenzaVO.getDataDichiarazione()));
	  htmpl.set("motivo",consistenzaVO.getMotivo());
	  if(Validator.isNotEmpty(consistenzaVO.getNumeroProtocollo())) 
	  {
	    htmpl.set("numeroProtocollo", StringUtils.parseNumeroProtocolloField(consistenzaVO.getNumeroProtocollo()));
	    htmpl.set("dataProtocollo", new SimpleDateFormat(DateUtils.DATA_ORA).format(consistenzaVO.getDataProtocollo()));
	  }
	    
	  if(Validator.isNotEmpty(allegatoDichiarazioneVO))
	  {
	    String descTipoFirma = "Validazione non firmata";
	    if(Validator.isNotEmpty(allegatoDichiarazioneVO.getDescTipoFirma()))
	    {
	      descTipoFirma = allegatoDichiarazioneVO.getDescTipoFirma();
	    }
	    htmpl.set("descTipoFirma", descTipoFirma);
	    
	    if(Validator.isNotEmpty(allegatoDichiarazioneVO.getIdTipoFirma()))
	    {
	      if(allegatoDichiarazioneVO.getIdTipoFirma().compareTo(new Long(1)) == 0)
	      { 
	        htmpl.set("dataFirma", DateUtils.formatDateTimeNotNull(consistenzaVO.getDataProtocollo()));
	      }
	      else if((allegatoDichiarazioneVO.getIdTipoFirma().compareTo(new Long(2)) == 0)
	        || (allegatoDichiarazioneVO.getIdTipoFirma().compareTo(new Long(4)) == 0))  
	      {
	        htmpl.set("dataFirma", DateUtils.formatDateTimeNotNull(allegatoDichiarazioneVO.getDataUltimoAggiornamento()));
	      }
	      
	    }
	  }
  
  
  
  
 		htmpl.newBlock("blkDati");
    
    htmpl.set("blkDati.idDichiarazioneConsistenza", ""+idDichiarazioneConsistenza);
 	
    
    
 		// Elenco attestazioni
 		if(Validator.isNotEmpty(elencoAttestazioni)) 
 		{
	 		for(int i = 0; i < elencoAttestazioni.size(); i++) 
	 		{
	 			TipoAttestazioneVO tipoAttestazioneVO = elencoAttestazioni.get(i);
	 			
	 			htmpl.newBlock("blkDati.blkElencoAttestazioni");
	 			if(tipoAttestazioneVO.getIdAttestazionePadre().compareTo(SolmrConstants.ID_ATTESTAZIONE_PADRE_INTESTAZIONE) == 0) 
	 			{
	 				htmpl.newBlock("blkDati.blkElencoAttestazioni.blkTitoloTable");
	 				htmpl.set("blkDati.blkElencoAttestazioni.blkTitoloTable.descrizione", tipoAttestazioneVO.getDescrizione());
	 			}
	 			else 
	 			{
		 			if(tipoAttestazioneVO.getNumeroColonneRiga().equalsIgnoreCase(SolmrConstants.NUMERO_RIGHE_INTESTAZIONE_ATTESTAZIONE)) 
		 			{
		 				if(tipoAttestazioneVO.getTipoCarattere().equalsIgnoreCase(SolmrConstants.TIPO_ATTESTAZIONE_CARATTERE_BOLD)) 
		 				{
		 					htmpl.newBlock("blkDati.blkElencoAttestazioni.blkGruppoBold");
		 					htmpl.set("blkDati.blkElencoAttestazioni.blkGruppoBold.descrizione", tipoAttestazioneVO.getDescrizione());
		 				}
		 				else 
		 				{
		 					htmpl.newBlock("blkDati.blkElencoAttestazioni.blkGruppoNormal");
		 					htmpl.set("blkDati.blkElencoAttestazioni.blkGruppoNormal.descrizione", tipoAttestazioneVO.getDescrizione());
		 				}
		 			}
		 			else if(tipoAttestazioneVO.getNumeroColonneRiga().equalsIgnoreCase(SolmrConstants.NUMERO_RIGHE_MONO_ATTESTAZIONE)) 
		 			{
		 				htmpl.newBlock("blkDati.blkElencoAttestazioni.blkCorrelataMono");
		 				if(tipoAttestazioneVO.getTipoRiga().equalsIgnoreCase(SolmrConstants.TIPO_RIGHE_ATTESTAZIONE_CHECKBOX)) 
		 				{
		 					htmpl.newBlock("blkDati.blkElencoAttestazioni.blkCorrelataMono.blkCheck");
		 					htmpl.set("blkDati.blkElencoAttestazioni.blkCorrelataMono.blkCheck.valueIdAttestazione", tipoAttestazioneVO.getIdAttestazione().toString());
		 					if(tipoAttestazioneVO.getDisabilitato().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
		 					{
	 							htmpl.set("blkDati.blkElencoAttestazioni.blkCorrelataMono.blkCheck.disabledMono", "disabled=\"disabled\"", null);
	 						}
		 					if(tipoAttestazioneVO.isAttestazioneAzienda()) {
		 						htmpl.set("blkDati.blkElencoAttestazioni.blkCorrelataMono.blkCheck.checked", "checked=\"checked\"", null);
		 						if(tipoAttestazioneVO.getDisabilitato().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
		 						{
		 							htmpl.newBlock("blkDati.blkElencoAttestazioni.blkCorrelataMono.blkCheck.blkHidden");
		 							htmpl.set("blkDati.blkElencoAttestazioni.blkCorrelataMono.blkCheck.blkHidden.valueIdAttestazione", tipoAttestazioneVO.getIdAttestazione().toString());
		 						}
		 					}
		 					else 
		 					{
		 						if(reload && elencoIdSelezionati != null && elencoIdSelezionati.size() > 0) {
		 							Long idAtt = (Long)elencoIdSelezionati.get(tipoAttestazioneVO.getIdAttestazione());
		 							if(idAtt != null && idAtt.compareTo(tipoAttestazioneVO.getIdAttestazione()) == 0) 
		 							{
		 								htmpl.set("blkDati.blkElencoAttestazioni.blkCorrelataMono.blkCheck.checked", "checked=\"checked\"", null);		 								
		 							}
		 						}
		 					}
		 				}
		 				else if(!tipoAttestazioneVO.getTipoRiga().equalsIgnoreCase(SolmrConstants.FLAG_N)) 
		 				{
		 					htmpl.newBlock("blkDati.blkElencoAttestazioni.blkCorrelataMono.blkLabel");
		 					htmpl.set("blkDati.blkElencoAttestazioni.blkCorrelataMono.blkLabel.tipoRiga", tipoAttestazioneVO.getTipoRiga());
		 				}
		 				htmpl.set("blkDati.blkElencoAttestazioni.blkCorrelataMono.descrizione", HtmplUtil.creaLayoutForAttestazioni(tipoAttestazioneVO.getParametriAttAziendaVO(), tipoAttestazioneVO.getParametriAttDichiarataVO(), idDichiarazioneConsistenza , tipoAttestazioneVO, tipoAttestazioneVO.getTipoParametriAttestazioneVO(), elencoErrori, pathErrori, reload, request), null);
		 			}
		 			else if(tipoAttestazioneVO.getNumeroColonneRiga().equalsIgnoreCase(SolmrConstants.NUMERO_RIGHE_DOUBLE_ATTESTAZIONE)) 
		 			{
		 				htmpl.newBlock("blkDati.blkElencoAttestazioni.blkCorrelataDoppia");
		 				if(tipoAttestazioneVO.getTipoRiga().equalsIgnoreCase(SolmrConstants.TIPO_RIGHE_ATTESTAZIONE_CHECKBOX)) 
		 				{
		 					htmpl.newBlock("blkDati.blkElencoAttestazioni.blkCorrelataDoppia.blkCheck");
		 					htmpl.set("blkDati.blkElencoAttestazioni.blkCorrelataDoppia.blkCheck.idAttestazionePadre", tipoAttestazioneVO.getIdAttestazionePadre().toString());
		 					if(tipoAttestazioneVO.isAttestazioneAzienda()) 
		 					{
		 						htmpl.set("blkDati.blkElencoAttestazioni.blkCorrelataDoppia.blkCheck.checked", "checked=\"checked\"", null);
		 					}
		 				}
		 				else if(!tipoAttestazioneVO.getTipoRiga().equalsIgnoreCase(SolmrConstants.FLAG_N)) 
		 				{
		 					htmpl.newBlock("blkDati.blkElencoAttestazioni.blkCorrelataDoppia.blkLabel");
		 					htmpl.set("blkDati.blkElencoAttestazioni.blkCorrelataDoppia.blkLabel.tipoRiga", tipoAttestazioneVO.getTipoRiga());
		 				}
		 				htmpl.set("blkDati.blkElencoAttestazioni.blkCorrelataDoppia.descrizione", tipoAttestazioneVO.getDescrizione());
		 			}
	 			}
	 		}
 		}
 		else 
 		{
 			htmpl.newBlock("blkErrore");
 	 		htmpl.set("blkErrore.messaggioErrore", AnagErrors.ERR_NO_ATTESTAZIONI_FOUND);
 		}
 	}
 	

%>
<%= htmpl.text()%>
