<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.sian.SianAnagTributariaVO" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popSoggettoAT.htm");
 	Htmpl htmpl = new Htmpl(layout);

	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);

 	SianAnagTributariaVO anagTrib = (SianAnagTributariaVO)request.getAttribute("anagTrib");

 	if(anagTrib == null) 
 	{
 		htmpl.newBlock("blkErrore");
 		htmpl.set("blkErrore.messaggio", AnagErrors.ERRORE_CUAA_NOT_FOUND_AT);
 	}
 	else 
 	{
 		htmpl.newBlock("blkSoggettoAT");
 		htmpl.set("blkSoggettoAT.codiceFiscale", anagTrib.getCodiceFiscale());
 		htmpl.set("blkSoggettoAT.partitaIva", anagTrib.getPartitaIva());
 		htmpl.set("blkSoggettoAT.cognome", anagTrib.getCognome());
 		htmpl.set("blkSoggettoAT.nome", anagTrib.getNome());
 		htmpl.set("blkSoggettoAT.comuneNascita", anagTrib.getComuneNascita());
 		if(Validator.isNotEmpty(anagTrib.getProvinciaNascita())) {
 			htmpl.set("blkSoggettoAT.provinciaNascita", "("+anagTrib.getProvinciaNascita()+")");
 		}
 		if(Validator.isNotEmpty(anagTrib.getDataNascita())) {
 			htmpl.set("blkSoggettoAT.dataNascita", anagTrib.getDataNascita().substring(0, 10));
 		}
 		if(Validator.isNotEmpty(anagTrib.getDataDecesso())) {
      htmpl.set("blkSoggettoAT.dataDecesso", anagTrib.getDataDecesso().substring(0, 10));
    }
 		htmpl.set("blkSoggettoAT.sesso", anagTrib.getSesso());
 		htmpl.set("blkSoggettoAT.denominazione", anagTrib.getDenominazione());
 		htmpl.set("blkSoggettoAT.codiceAteco", anagTrib.getCodiceAteco());
    htmpl.set("blkSoggettoAT.descAttivitaAteco", anagTrib.getDescAttivitaAteco());
 		htmpl.set("blkSoggettoAT.indirizzoResidenza", anagTrib.getIndirizzoResidenza());
 		htmpl.set("blkSoggettoAT.comuneResidenza", anagTrib.getComuneResidenza());
 		if(Validator.isNotEmpty(anagTrib.getProvinciaResidenza())) {
 			htmpl.set("blkSoggettoAT.provinciaResidenza", "("+anagTrib.getProvinciaResidenza()+")");
 		}
 		htmpl.set("blkSoggettoAT.capResidenza", anagTrib.getCapResidenza());
 		
 		if(Validator.isNotEmpty(anagTrib.getComuneSedeLegale())) 
 		{
 			htmpl.newBlock("blkSoggettoAT.blkSezioneSedeLegale");
 			htmpl.set("blkSoggettoAT.blkSezioneSedeLegale.indirizzoSedeLegale", anagTrib.getIndirizzoSedeLegale());
 			htmpl.set("blkSoggettoAT.blkSezioneSedeLegale.comuneSedeLegale", anagTrib.getComuneSedeLegale());
 			if(Validator.isNotEmpty(anagTrib.getProvinciaSedeLegale())) {
 				htmpl.set("blkSoggettoAT.blkSezioneSedeLegale.provinciaSedeLegale", "("+anagTrib.getProvinciaSedeLegale()+")");
 			}
 			htmpl.set("blkSoggettoAT.blkSezioneSedeLegale.capSedeLegale", anagTrib.getCapSedeLegale());
 		}
 		if(Validator.isNotEmpty(anagTrib.getComuneDomicilioFiscale())) {
 			htmpl.newBlock("blkSoggettoAT.blkSezioneDomicilioFiscale");
 			htmpl.set("blkSoggettoAT.blkSezioneDomicilioFiscale.indirizzoDomicilioFiscale", anagTrib.getIndirizzoDomicilioFiscale());
 			htmpl.set("blkSoggettoAT.blkSezioneDomicilioFiscale.comuneDomicilioFiscale", anagTrib.getComuneDomicilioFiscale());
 			if(Validator.isNotEmpty(anagTrib.getProvinciaDomicilioFiscale())) {
 				htmpl.set("blkSoggettoAT.blkSezioneDomicilioFiscale.provinciaDomicilioFiscale", "("+anagTrib.getProvinciaDomicilioFiscale()+")");
 			}
 			htmpl.set("blkSoggettoAT.blkSezioneDomicilioFiscale.capDomicilioFiscale", anagTrib.getCapDomicilioFiscale());
 		}
 		
 	}



%>
<%= htmpl.text()%>
