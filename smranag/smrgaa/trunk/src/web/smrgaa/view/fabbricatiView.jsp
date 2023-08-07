<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.PianoRiferimentoUtils" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/fabbricati.htm");
	Htmpl htmpl = new Htmpl(layout);
	FabbricatoVO[] elencoFabbricati = (FabbricatoVO[])request.getAttribute("elencoFabbricati");
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  String idDichiarazioneConsistenza = request.getParameter("idDichiarazioneConsistenza");
  Vector<UteVO> elencoUte = (Vector<UteVO>)request.getAttribute("elencoUte");
	
 	%>
    <%@include file = "/view/remoteInclude.inc" %>
 	<%


 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);

 	HtmplUtil.setValues(htmpl,anagAziendaVO);
 	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

 	Long idElemento = (Long)request.getAttribute("idElemento");

	// Combo Piano di riferimento
  String bloccoDichiarazioneConsistenza =  "blkPianoRiferimento";
  PianoRiferimentoUtils pianoRiferimentoUtils = new PianoRiferimentoUtils();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  pianoRiferimentoUtils.popolaComboPianoRiferimento(htmpl, anagFacadeClient,
    anagAziendaVO.getIdAzienda(), bloccoDichiarazioneConsistenza, idDichiarazioneConsistenza, SolmrConstants.PIANO_LAVORAZIONE_NORMALE,
    ruoloUtenza);
    
  // Combo unità produttive
  String idUte = request.getParameter("idUte");
  if(elencoUte != null && elencoUte.size() > 0) 
  {
    Iterator<UteVO> iteraUte = elencoUte.iterator();
    while(iteraUte.hasNext()) 
    {
      UteVO uteVO = (UteVO)iteraUte.next();
      htmpl.newBlock("blkElencoUte");
      htmpl.set("blkElencoUte.idUte", uteVO.getIdUte().toString());
      String descrizione = uteVO.getComuneUte().getDescom()+" - "+uteVO.getIndirizzo();
      if(Validator.isNotEmpty(uteVO.getDenominazione()))
        descrizione += " - "+uteVO.getDenominazione();
      if(Validator.isNotEmpty(uteVO.getDataFineAttivita())) {
        descrizione += " fino al "+DateUtils.formatDate(uteVO.getDataFineAttivita());
      }
      htmpl.set("blkElencoUte.descUte", descrizione);
      if(Validator.isNotEmpty(idUte)) 
      {
        if(new Long(idUte).compareTo(uteVO.getIdUte()) == 0) 
        {
          htmpl.set("blkElencoUte.selected", "selected=\"selected\"", null);
        }
      }
    }
  }
    
    

 	String idFabbricato = request.getParameter("idFabbricato");
 	if(elencoFabbricati != null && elencoFabbricati.length > 0) 
  {
    htmpl.newBlock("etichettaFabbricati");
   	for(int i = 0; i < elencoFabbricati.length; i++) 
    {
      htmpl.newBlock("elencoFabbricati");
     	FabbricatoVO fabbricatoVO = (FabbricatoVO)elencoFabbricati[i];
     	if(idElemento != null) 
      {
        if(idElemento.compareTo(fabbricatoVO.getIdFabbricato()) == 0) 
        {
          htmpl.set("elencoFabbricati.checked", "checked");
       	}
    	}
     	else 
      {
        if(idFabbricato != null && !idFabbricato.equals("")) 
        {
          if(fabbricatoVO.getIdFabbricato().compareTo(Long.decode(idFabbricato)) == 0) 
          {
            htmpl.set("elencoFabbricati.checked", "checked");
         	}
       	}
     	}
     	htmpl.set("elencoFabbricati.idFabbricato", fabbricatoVO.getIdFabbricato().toString());
      
      
      // Icona relativa al biologico
      if(fabbricatoVO.getBiologico() != null) 
      {
        htmpl.set("elencoFabbricati.descBiologico", SolmrConstants.DESC_TITLE_BIOLOGICO);
        htmpl.set("elencoFabbricati.classBiologico", SolmrConstants.CLASS_BIOLOGICO);
      }
      
      
     	htmpl.set("elencoFabbricati.siglaProvUte", fabbricatoVO.getSiglaProvUte());
     	htmpl.set("elencoFabbricati.descComuneUte", fabbricatoVO.getDescComuneUte());
     	htmpl.set("elencoFabbricati.descrizioneTipoFabbricato", fabbricatoVO.getTipoTipologiaFabbricatoVO().getDescrizione());
     	htmpl.set("elencoFabbricati.dimensioneFabbricato", StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getDimensioneFabbricato()));
     	htmpl.set("elencoFabbricati.unitaMisura", fabbricatoVO.getTipoTipologiaFabbricatoVO().getUnitaMisura());
     	htmpl.set("elencoFabbricati.superficieFabbricato", StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getSuperficieFabbricato()));
     	if(fabbricatoVO.getDataFineValiditaFabbricato() != null) 
      {
        htmpl.set("elencoFabbricati.dataFineValidita", DateUtils.formatDate(fabbricatoVO.getDataFineValiditaFabbricato()));
     	}
   	}
 	}
 	else 
  {
    htmpl.newBlock("noFabbricati");
 	}

 	HtmplUtil.setErrors(htmpl,errors,request, application);

%>
<%= htmpl.text()%>
