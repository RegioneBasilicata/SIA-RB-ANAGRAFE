<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.consistenza.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/dichiarazioneAnomaliaScegliMotivo.htm");
 	Htmpl htmpl = new Htmpl(layout);
 	
 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);

 	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 	Vector<TipoMotivoDichiarazioneVO> vMotivoDichiarazione = (Vector<TipoMotivoDichiarazioneVO>)request.getAttribute("vMotivoDichiarazione");
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  String idMotivoDichiarazioneStr = request.getParameter("idMotivoDichiarazione");
 	
 	// Se si è verificato qualche errore visualizzo il messaggio all'utente
 	if(Validator.isNotEmpty(messaggioErrore)) 
 	{
 		htmpl.newBlock("blkErrore");
 		htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
 	}
 	// Altrimenti estraggo i dati
 	else 
 	{
 	
 	  //pop notifiche
 	  boolean isValidazioneRichiesta = false;
	  AziendaNuovaVO aziendaNuovaVO = (AziendaNuovaVO)request.getAttribute("aziendaNuovaVO");
	  if(Validator.isNotEmpty(aziendaNuovaVO) && Validator.isEmpty(request.getParameter("regimeAnomaliaScegliMotivo")))
	  {
	    if(aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_VALIDATA) == 0)
	    {
	      htmpl.newBlock("blkjQuery");	
	      htmpl.set("desctTipoValidazione", aziendaNuovaVO.getDescMotivoDichiarazione());
	      isValidazioneRichiesta = true;	        
	    }
	  }
 	
 	
 		if(vMotivoDichiarazione != null) 
 		{
 			htmpl.newBlock("blkDati");
			// Combo tipo motivo dichiarazione
 			for(int i = 0; i < vMotivoDichiarazione.size(); i++) 
 			{
 				TipoMotivoDichiarazioneVO tipoMotivoDichiarazioneVO = (TipoMotivoDichiarazioneVO)vMotivoDichiarazione.get(i);
 				htmpl.newBlock("blkDati.blkMotiviDichiarazione");
 				htmpl.set("blkDati.blkMotiviDichiarazione.idMotivoDichiarazione", tipoMotivoDichiarazioneVO.getIdMotivoDichiarazione().toString());
 				htmpl.set("blkDati.blkMotiviDichiarazione.descrizione", tipoMotivoDichiarazioneVO.getDescrizione());
        
        if((isValidazioneRichiesta) && (Validator.isEmpty(request.getParameter("regimeAnomaliaScegliMotivo")))
          && (aziendaNuovaVO.getIdMotivoDichiarazione().intValue() == tipoMotivoDichiarazioneVO.getIdMotivoDichiarazione().intValue()))
        {
          htmpl.set("blkDati.blkMotiviDichiarazione.selected", "selected=\"selected\"", null);
        }
        else if(Validator.isNotEmpty(idMotivoDichiarazioneStr) && idMotivoDichiarazioneStr.equalsIgnoreCase(tipoMotivoDichiarazioneVO.getIdMotivoDichiarazione().toString())) 
        {
          htmpl.set("blkDati.blkMotiviDichiarazione.selected", "selected=\"selected\"", null);
        }
 			}
 		}
 	}
  
  HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>
