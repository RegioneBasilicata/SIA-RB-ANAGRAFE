<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>

<%

	Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/nuovaAziendaElenco.htm");

  	%>
    	<%@include file = "/view/remoteInclude.inc" %>
  	<%

  	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

	Object vectRange = (Object)session.getAttribute("listRange");
  	if(vectRange instanceof Vector) {
  		vectRange = (Vector)session.getAttribute("listRange");
  	}
  	else {
  		vectRange = (AnagAziendaVO[])session.getAttribute("listRange");
  	}

  	// Nuova gestione fogli di stile
  	htmpl.set("head", head, null);
  	htmpl.set("header", header, null);
  	htmpl.set("footer", footer, null);

  	htmpl.set("CUAA",(String)request.getAttribute("CUAA"));
  	htmpl.set("CUAASubentro",(String)request.getAttribute("CUAASubentro"));
  	htmpl.set("radiobuttonAzienda",(String)request.getAttribute("radiobuttonAzienda"));
  	htmpl.set("cuaaProvenienza",(String)request.getAttribute("cuaaProvenienza"));

  	if(vectRange instanceof Vector) {
		if(vectRange!=null && ((Vector)vectRange).size()>0){
	    	htmpl.set("numeroRecord",""+((Vector)vectRange).size());
	    	htmpl.set("dataSituazioneAlStr",((AnagAziendaVO)((Vector)vectRange).elementAt(0)).getDataSituazioneAlStr());
	    	for(int i = 0; i < ((Vector)vectRange).size(); i++){
	      		AnagAziendaVO aaVO = (AnagAziendaVO)((Vector)vectRange).elementAt(i);
	      		htmpl.newBlock("rigaAnagrafica");
	      		htmpl.set("rigaAnagrafica.idAzienda",""+aaVO.getIdAzienda());
	      		htmpl.set("rigaAnagrafica.denominazione",aaVO.getDenominazione());
	      		htmpl.set("rigaAnagrafica.cuaa",aaVO.getCUAA());
	      		htmpl.set("rigaAnagrafica.partitaIVA",aaVO.getPartitaIVA());
	      		if(aaVO.getDescComune() != null && !aaVO.getDescComune().equals("")) {
	        		htmpl.set("rigaAnagrafica.comune",aaVO.getDescComune());
	      		}
	      		else if(aaVO.getSedelegCittaEstero() != null && !aaVO.getSedelegCittaEstero().equals("")) {
	        		htmpl.set("rigaAnagrafica.comune",aaVO.getSedelegCittaEstero());
	      		}
	      		if(aaVO.getSedelegProv()!=null&&!aaVO.getSedelegProv().equals("")) {
	        		htmpl.set("rigaAnagrafica.prov",aaVO.getSedelegProv());
	      		}
	      		else if(aaVO.getSedelegEstero()!=null && !aaVO.getSedelegEstero().equals("")) {
	        		htmpl.set("rigaAnagrafica.prov",aaVO.getSedelegEstero());
	      		}
	      		htmpl.set("rigaAnagrafica.indirizzo",aaVO.getSedelegIndirizzo());
	      		htmpl.set("rigaAnagrafica.inizioVal",DateUtils.formatDate(aaVO.getDataInizioVal()));
	      		if(aaVO.getDataFineVal() != null) {
	        		htmpl.set("rigaAnagrafica.fineVal",DateUtils.formatDate(aaVO.getDataFineVal()));
	      		}
	     		else if(Validator.isNotEmpty(aaVO.getDataCessazione())) {
	        		htmpl.set("rigaAnagrafica.fineVal",DateUtils.formatDate(aaVO.getDataCessazione()));
	      		}
	    	}
	  	}
  	}
  	else if(vectRange instanceof AnagAziendaVO[]) {
  		if(vectRange!=null && ((AnagAziendaVO[])vectRange).length > 0){
	    	htmpl.set("numeroRecord",""+((AnagAziendaVO[])vectRange).length);
	    	htmpl.set("dataSituazioneAlStr",((AnagAziendaVO)((AnagAziendaVO[])vectRange)[0]).getDataSituazioneAlStr());
	    	for(int i = 0; i < ((AnagAziendaVO[])vectRange).length; i++){
	      		AnagAziendaVO aaVO = (AnagAziendaVO)((AnagAziendaVO[])vectRange)[i];
	      		htmpl.newBlock("rigaAnagrafica");
	      		htmpl.set("rigaAnagrafica.idAzienda",""+aaVO.getIdAzienda());
	      		htmpl.set("rigaAnagrafica.denominazione",aaVO.getDenominazione());
	      		htmpl.set("rigaAnagrafica.cuaa",aaVO.getCUAA());
	      		htmpl.set("rigaAnagrafica.partitaIVA",aaVO.getPartitaIVA());
	      		if(aaVO.getDescComune() != null && !aaVO.getDescComune().equals("")) {
	        		htmpl.set("rigaAnagrafica.comune",aaVO.getDescComune());
	      		}
	      		else if(aaVO.getSedelegCittaEstero() != null && !aaVO.getSedelegCittaEstero().equals("")) {
	        		htmpl.set("rigaAnagrafica.comune",aaVO.getSedelegCittaEstero());
	      		}
	      		if(aaVO.getSedelegProv()!=null&&!aaVO.getSedelegProv().equals("")) {
	        		htmpl.set("rigaAnagrafica.prov",aaVO.getSedelegProv());
	      		}
	      		else if(aaVO.getSedelegEstero()!=null && !aaVO.getSedelegEstero().equals("")) {
	        		htmpl.set("rigaAnagrafica.prov",aaVO.getSedelegEstero());
	      		}
	      		htmpl.set("rigaAnagrafica.indirizzo",aaVO.getSedelegIndirizzo());
	      		htmpl.set("rigaAnagrafica.inizioVal",DateUtils.formatDate(aaVO.getDataInizioVal()));
	      		if(aaVO.getDataFineVal() != null) {
	        		htmpl.set("rigaAnagrafica.fineVal",DateUtils.formatDate(aaVO.getDataFineVal()));
	      		}
	     		else if(Validator.isNotEmpty(aaVO.getDataCessazione())) {
	        		htmpl.set("rigaAnagrafica.fineVal",DateUtils.formatDate(aaVO.getDataCessazione()));
	      		}
	    	}
	  	}
  	}
  	else {
    	htmpl.set("numeroRecord","0");
  	}
  	HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>
