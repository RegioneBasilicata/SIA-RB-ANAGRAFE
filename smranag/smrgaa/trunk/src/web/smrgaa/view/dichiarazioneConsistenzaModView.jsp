<%@ page language="java"
contentType="text/html"
isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.consistenza.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@page import="it.csi.smranag.smrgaa.dto.AllegatoDichiarazioneVO"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

	SolmrLogger.debug(this, " - dichiarazioneConsistenzaModView.jsp - INIZIO PAGINA");

	java.io.InputStream layout = application.getResourceAsStream("/layout/dichiarazioneConsistenzaMod.htm");
	Htmpl htmpl = new Htmpl(layout);

	%>
  	<%@include file = "/view/remoteInclude.inc" %>
	<%

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  
  String htmlStringKO = "<img src=\"{0}\" "+
                              "title=\"{1}\" border=\"0\"></a>";
  String imko = "ko.gif";
  String imok = "ok.gif";
  
  
  
  	
	// Nuova gestione fogli di stile
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);

	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
	String regimeDichiarazioneConsistenzaMod = request.getParameter("regimeDichiarazioneConsistenzaMod");


	SolmrLogger.debug(this, "anagAziendaVO.getCUAA(): " + anagAziendaVO.getCUAA());

	ConsistenzaVO consistenzaVO = (ConsistenzaVO)request.getAttribute("consistenzaVO");
	AllegatoDichiarazioneVO allegatoDichiarazioneVO = (AllegatoDichiarazioneVO)request.getAttribute("allegatoDichiarazioneVO");
  String idDichiarazioneConsistenza = consistenzaVO.getIdDichiarazioneConsistenza();
  if(Validator.isNotEmpty(request.getAttribute("idAllegato")))
  {
    idDichiarazioneConsistenza += "_"+(Long)request.getAttribute("idAllegato");
  }

	htmpl.set("idDichiarazioneConsistenza", idDichiarazioneConsistenza);
 	htmpl.set("anno", consistenzaVO.getAnno());
 	htmpl.set("data", new SimpleDateFormat(DateUtils.DATA_ORA).format(consistenzaVO.getDataDichiarazione()));
 	htmpl.set("motivo",consistenzaVO.getMotivo());
  if(Validator.isNotEmpty(consistenzaVO.getNumeroProtocollo())) 
  {
  	htmpl.set("numeroProtocollo", StringUtils.parseNumeroProtocolloField(consistenzaVO.getNumeroProtocollo()));
  	htmpl.set("dataProtocollo", new SimpleDateFormat(DateUtils.DATA_ORA).format(consistenzaVO.getDataProtocollo()));
  }
  htmpl.set("note",consistenzaVO.getNote()); 
   
   
   
  String descTipoFirma = "Firmata su carta";
  if(Validator.isNotEmpty(allegatoDichiarazioneVO))
  { 
     htmpl.newBlock("blkTipoAllegato");
     htmpl.set("blkTipoAllegato.descTipoAllegato", allegatoDichiarazioneVO.getDescTipoAllegato());
     htmpl.set("blkTipoAllegato.dataAllegato", DateUtils.formatDateTimeNotNull(allegatoDichiarazioneVO.getDataInizioValidita()));
  
    if(Validator.isNotEmptyNotZero(allegatoDichiarazioneVO.getIdTipoFirma()))
	  {
	    descTipoFirma = allegatoDichiarazioneVO.getDescTipoFirma();
	    if(SolmrConstants.MODOL_STAMPA_NON_FIRMATA.compareTo(allegatoDichiarazioneVO.getIdTipoFirma()) != 0)
	    {
	      htmpl.newBlock("blkCheckFirma");
	      htmpl.set("blkCheckFirma.checkedFirma", "checked=\"checked\"", null);
	      htmpl.set("blkCheckFirma.valFirma", ""+allegatoDichiarazioneVO.getIdTipoFirma());
	    }else if(SolmrConstants.MODOL_STAMPA_NON_FIRMATA.compareTo(allegatoDichiarazioneVO.getIdTipoFirma()) == 0)
	    {
        htmpl.newBlock("blkRadioFirma");
        htmpl.newBlock("blkRadioFirmaLabel");
      } 
	  }
	  else
	  {
	    htmpl.newBlock("blkCheckFirma");
	    htmpl.set("blkCheckFirma.valFirma", "1");
	  }
	}
	if(!Validator.isNotEmptyNotZero(allegatoDichiarazioneVO.getIdTipoFirma()) || SolmrConstants.MODOL_STAMPA_NON_FIRMATA.compareTo(allegatoDichiarazioneVO.getIdTipoFirma()) != 0)
	{
	 htmpl.set("descTipoFirma", descTipoFirma); 
  }
  
  if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore, null);
  }     	
		
		
    	

  HtmplUtil.setErrors(htmpl, errors, request, application);

  SolmrLogger.debug(this, " - dichiarazioneConsistenzaModView.jsp - FINE PAGINA");
%>
<%= htmpl.text()%>
