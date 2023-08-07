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
<%@ page import="it.csi.smranag.smrgaa.dto.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.stampe.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

	SolmrLogger.debug(this, " - dichiarazioneConsistenzaAllegatiView.jsp - INIZIO PAGINA");

	java.io.InputStream layout = application.getResourceAsStream("/layout/dichiarazioneConsistenzaAllegati.htm");
	Htmpl htmpl = new Htmpl(layout);

	%>
  	<%@include file = "/view/remoteInclude.inc" %>
	<%
  	
	// Nuova gestione fogli di stile
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);
	
	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");


	ConsistenzaVO consistenzaVO = (ConsistenzaVO)request.getAttribute("consistenzaVO");
	AllegatoDichiarazioneVO allegatoDichiarazioneVO = (AllegatoDichiarazioneVO)request.getAttribute("allegatoDichiarazioneVO");
  Vector<TipoAllegatoVO> vTipoAllegato = (Vector<TipoAllegatoVO>)request.getAttribute("vTipoAllegato");
  String idTipoAllegato = request.getParameter("idTipoAllegato");


	htmpl.set("idDichiarazioneConsistenza", consistenzaVO.getIdDichiarazioneConsistenza());
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
    
  
    
  if(Validator.isNotEmpty(vTipoAllegato)) 
  {    
    for(int i = 0; i < vTipoAllegato.size(); i++) 
    {
      TipoAllegatoVO tipoAllegatoVO = vTipoAllegato.get(i);
      htmpl.newBlock("blkTipoAllegato");
      htmpl.set("blkTipoAllegato.idTipoAllegato", ""+tipoAllegatoVO.getIdTipoAllegato());
      htmpl.set("blkTipoAllegato.descrizione", tipoAllegatoVO.getDescrizioneTipoAllegato());
      if(Validator.isNotEmpty(idTipoAllegato))
      {
        if(new Long(idTipoAllegato).compareTo(new Long(tipoAllegatoVO.getIdTipoAllegato())) == 0) {
          htmpl.set("blkTipoAllegato.selected", "selected=\"selected\"", null);
        }
      }
    }
  }
    
  
  // Se si è verificato qualche errore visualizzo il messaggio all'utente
  if(Validator.isNotEmpty(messaggioErrore)) {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
    
    
    
   
  HtmplUtil.setErrors(htmpl, errors, request, application);
 	SolmrLogger.debug(this, " - dichiarazioneConsistenzaAllegatiView.jsp - FINE PAGINA");
%>
<%= htmpl.text()%>
