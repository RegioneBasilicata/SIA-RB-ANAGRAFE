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
<%@ page import="it.csi.smranag.smrgaa.dto.fascicoli.InvioFascicoliVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	SolmrLogger.debug(this, " - schedulazioneInvioASianView.jsp - INIZIO PAGINA");

	java.io.InputStream layout = application.getResourceAsStream("/layout/schedulazioneInvioASian.htm");
	Htmpl htmpl = new Htmpl(layout);

	%>
  	<%@include file = "/view/remoteInclude.inc" %>
	<%

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");  
  
  	
	// Nuova gestione fogli di stile
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);

	ConsistenzaVO consistenzaVO = (ConsistenzaVO)request.getAttribute("consistenzaVO");
	InvioFascicoliVO invioFascicoliVO = (InvioFascicoliVO)request.getAttribute("invioFascicoliVO");
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
	
	htmpl.set("idDichiarazioneConsistenza", consistenzaVO.getIdDichiarazioneConsistenza());
	htmpl.set("anno", consistenzaVO.getAnno());
	htmpl.set("data", new SimpleDateFormat(DateUtils.DATA_ORA).format(consistenzaVO.getDataDichiarazione()));
	if(Validator.isNotEmpty(consistenzaVO.getNumeroProtocollo())) 
	{
	  htmpl.set("numeroProtocollo", StringUtils.parseNumeroProtocolloField(consistenzaVO.getNumeroProtocollo()));
	  htmpl.set("dataProtocollo", new SimpleDateFormat(DateUtils.DATA_ORA).format(consistenzaVO.getDataProtocollo()));
	}
	htmpl.set("motivo",consistenzaVO.getMotivo());
	
	
	if(request.getParameter("regimeSchedulaInvioASian") != null)
	{
	  if(request.getParameter("datiTerritoriali") != null)
    {
      htmpl.set("checkedTerritoriali", "checked=\"checked\"", null);
    }
    else
    {
      htmpl.set("checkedTerritoriali", "");
    }
    
    if(request.getParameter("datiUnitaVitate") != null)
    {
      htmpl.set("checkedVitate", "checked=\"checked\"", null);
    }
    else
    {
      htmpl.set("checkedVitate", "");
    }
    
    if(request.getParameter("datiFabbricati") != null)
    {
      htmpl.set("checkedFabbricati", "checked=\"checked\"", null);
    }
    else
    {
      htmpl.set("checkedFabbricati", "");
    }
    
    if(request.getParameter("datiContiCorrenti") != null)
    {
      htmpl.set("checkedContiCorrenti", "checked=\"checked\"", null);
    }
    else
    {
      htmpl.set("checkedContiCorrenti", "");
    }	
	}
	else
	{	
		if(Validator.isNotEmpty(invioFascicoliVO))
		{
	    if("S".equalsIgnoreCase(invioFascicoliVO.getFlagTerreni()))
      {
        htmpl.set("checkedTerritoriali", "checked=\"checked\"", null);
      }
      else
      {
        htmpl.set("checkedTerritoriali", "");
      }	
      
		  if("S".equalsIgnoreCase(invioFascicoliVO.getFlagUv()))
	    {
		    htmpl.set("checkedVitate", "checked=\"checked\"", null);
		  }
		  else
		  {
		    htmpl.set("checkedVitate", "");
		  }
		  
		  if("S".equalsIgnoreCase(invioFascicoliVO.getFlagFabbricati()))
	    {
		    htmpl.set("checkedFabbricati", "checked=\"checked\"", null);
		  }
		  else
		  {
		    htmpl.set("checkedFabbricati", "");
		  }
		  
		  if("S".equalsIgnoreCase(invioFascicoliVO.getFlagCc()))
	    {
		    htmpl.set("checkedContiCorrenti", "checked=\"checked\"", null);
		  }
		  else
		  {
		    htmpl.set("checkedContiCorrenti", "");
		  }
		  
		}
  }
  
	if(Validator.isNotEmpty(invioFascicoliVO))
  {	
		String statoInvio = "";
		if(SolmrConstants.SCHED_STATO_INVIATO.equalsIgnoreCase(invioFascicoliVO.getStatoInvio()))
		{
		  statoInvio = "Invio in corso";
		}
		else if(SolmrConstants.SCHED_STATO_SCHEDULATO.equalsIgnoreCase(invioFascicoliVO.getStatoInvio()))
		{
		  statoInvio = "Schedulato";
		}
		else if(SolmrConstants.SCHED_STATO_CHIUSO.equalsIgnoreCase(invioFascicoliVO.getStatoInvio()))
		{
		  statoInvio = "Chiuso";
		}
		
		htmpl.set("statoInvio", statoInvio);
		htmpl.set("numTentativi",""+invioFascicoliVO.getNumTentativiInvio());
	}
	
	if(Validator.isNotEmpty(messaggioErrore)) {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
    
    
    

  HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>
