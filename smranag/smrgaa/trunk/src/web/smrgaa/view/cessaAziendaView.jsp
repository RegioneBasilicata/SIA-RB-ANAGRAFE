<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO" %>

<%

	Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/anagrafica_cessa.htm");

 	%>
   	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
 	TipoCessazioneVO[] elencoTipiCessazione = (TipoCessazioneVO[])request.getAttribute("elencoTipiCessazione");
  String dataCessazione = (String)request.getAttribute("dataCessazione");
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 	
 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);

 	HtmplUtil.setValues(htmpl, request);
 	HtmplUtil.setErrors(htmpl, errors, request, application);


  // Se si è verificato qualche errore visualizzo il messaggio all'utente
  if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  else
  {
  
    //pop notifiche
    boolean isValidazioneRichiesta = false;
    AziendaNuovaVO aziendaNuovaVO = (AziendaNuovaVO)request.getAttribute("aziendaNuovaVO");
    if(Validator.isNotEmpty(aziendaNuovaVO) && Validator.isEmpty(request.getParameter("regimeAnagraficaCessa")))
    {
      if(aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_VALIDATA) == 0)
      {
        htmpl.newBlock("blkjQuery");  
        htmpl.set("descMotivoCessazione", aziendaNuovaVO.getDescCessazione());
        isValidazioneRichiesta = true;          
      }
    }
  
  
	 	AnagAziendaVO aaVO = null;
	
	 	if(session.getAttribute("anagAziendaVO")!=null)
	   	aaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	
	 	htmpl.set("idAnagAzienda",aaVO.getIdAnagAzienda()+"");
	 	htmpl.set("denominazione",aaVO.getDenominazione());
	 	htmpl.set("cFpIselezionata",aaVO.getCUAA());
	 	htmpl.set("cuaa",aaVO.getCUAA());
	 	htmpl.set("denominazioneSelezionata",aaVO.getDenominazione());
	 	htmpl.set("datiAl",aaVO.getDataSituazioneAlStr());
	 	
	 	// Data cessazione
	 	if((isValidazioneRichiesta) && Validator.isEmpty(request.getParameter("regimeAnagraficaCessa")))
	 	{
	 	  htmpl.set("dataCessazioneStr", DateUtils.formatDate(aziendaNuovaVO.getDataCessazione()));
	 	}
	 	else if(Validator.isNotEmpty(dataCessazione)) 
	 	{
	 		htmpl.set("dataCessazioneStr", dataCessazione);
	 	}
	 	// Motivo cessazione
	 	if(elencoTipiCessazione != null && elencoTipiCessazione.length > 0) 
	 	{
	 		for(int i = 0; i < elencoTipiCessazione.length; i++) 
	 		{
	 			htmpl.newBlock("blkTipoCessazione");
	 			TipoCessazioneVO tipoCessazioneVO = (TipoCessazioneVO)elencoTipiCessazione[i];
	 			htmpl.set("blkTipoCessazione.idCessazione", tipoCessazioneVO.getIdCessazione().toString());
	 			htmpl.set("blkTipoCessazione.descrizione", tipoCessazioneVO.getDescrizione());
	 			if((isValidazioneRichiesta) && Validator.isEmpty(request.getParameter("regimeAnagraficaCessa"))
	 			  && (aziendaNuovaVO.getIdCessazione().compareTo(tipoCessazioneVO.getIdCessazione()) == 0))
	 			{
	 			  htmpl.set("blkTipoCessazione.selected", "selected=\"selected\"", null);
	 			}
	 			else if(request.getAttribute("idCessazione") != null 
	 			  && ((Long)request.getAttribute("idCessazione")).compareTo(tipoCessazioneVO.getIdCessazione()) == 0) 
	 			{
	 				htmpl.set("blkTipoCessazione.selected", "selected=\"selected\"", null);
	 			}
	 		}
	 	}
	}

%>
<%= htmpl.text()%>
