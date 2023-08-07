<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*"%>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popAggiungiUso.htm");

 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
 	HtmplUtil.setErrors(htmpl, errors, request, application);
 	Long idIndirizzoUtilizzo = (Long)request.getAttribute("idIndirizzoUtilizzo");
 	String provenienza = request.getParameter("provenienza");
 	String codiceUso = request.getParameter("codiceUso");
 	String idParticellaCertificata = request.getParameter("idParticellaCertificata");
 	
 	String[] arrVIdStoricoUnitaArborea = request.getParameterValues("idStoricoUnitaArborea");
 	String[] arrVAreaUV = request.getParameterValues("area");

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	
 	String type = (String)request.getAttribute("type");
 	htmpl.set("type", type);

 	if(Validator.isNotEmpty(provenienza)) {
 		htmpl.set("provenienza", provenienza);
 	}
 	
 	if(Validator.isNotEmpty(codiceUso)) {
    htmpl.set("codiceUso", codiceUso);
  }
 	
 	if(Validator.isNotEmpty(idParticellaCertificata)) {
    htmpl.set("idParticellaCertificata", idParticellaCertificata);
  }
 	
 	if(Validator.isNotEmpty(request.getParameter("numTotaleUtilizzo")))
 	{
 	  htmpl.set("numTotaleUtilizzo", request.getParameter("numTotaleUtilizzo"));
 	}
  
 	
 	if(Validator.isNotEmpty(arrVIdStoricoUnitaArborea)) 
 	{
 	  for(int i=0;i<arrVIdStoricoUnitaArborea.length;i++)
 	  {
 	    htmpl.newBlock("blkUvSel");
      htmpl.set("blkUvSel.idStoricoUnitaArborea", arrVIdStoricoUnitaArborea[i]);
    }
  }
  
  if(Validator.isNotEmpty(arrVAreaUV)) 
  {
    for(int i=0;i<arrVAreaUV.length;i++)
    {
      htmpl.newBlock("blkAreaUv");
      htmpl.set("blkAreaUv.area", arrVAreaUV[i]);
    }
  }
 	
 	it.csi.solmr.dto.CodeDescription[] elencoIndirizziUtilizzi = (it.csi.solmr.dto.CodeDescription[])request.getAttribute("elencoIndirizziUtilizzi");
 	if(elencoIndirizziUtilizzi != null && elencoIndirizziUtilizzi.length > 0) {
 		for(int i = 0; i < elencoIndirizziUtilizzi.length; i++) {
 			CodeDescription codeDescription = (CodeDescription)elencoIndirizziUtilizzi[i];
 			htmpl.newBlock("blkIndirizziUtilizzo");
 			htmpl.set("blkIndirizziUtilizzo.idIndirizzoUtilizzo", codeDescription.getCode().toString());
 			htmpl.set("blkIndirizziUtilizzo.descrizione", codeDescription.getDescription());
 			if(idIndirizzoUtilizzo != null) {
 				if(idIndirizzoUtilizzo.compareTo(Long.decode(codeDescription.getCode().toString())) == 0) {
 					htmpl.set("blkIndirizziUtilizzo.selected", "selected=\"selected\"");
 				}
 			}
 		}
 	}
 	
 	TipoUtilizzoVO[] elencoTipiUtilizzo = (TipoUtilizzoVO[])request.getAttribute("elencoTipiUsoSuolo"+type);
 	if(elencoTipiUtilizzo != null && elencoTipiUtilizzo.length > 0) {
 		for(int i = 0; i < elencoTipiUtilizzo.length; i++) {
 			TipoUtilizzoVO tipoUtilizzoVO = (TipoUtilizzoVO)elencoTipiUtilizzo[i];
 			htmpl.newBlock("blkTipiUtilizzo");
 			htmpl.set("blkTipiUtilizzo.idTipoUtilizzo", tipoUtilizzoVO.getIdUtilizzo().toString());
 			String descrizione = null;
 			if(Validator.isNotEmpty(tipoUtilizzoVO.getCodice())) {
 				descrizione = "["+tipoUtilizzoVO.getCodice()+"] ";
 			}
 			descrizione += tipoUtilizzoVO.getDescrizione();
 			htmpl.set("blkTipiUtilizzo.descrizione", descrizione);
 		}
 	}

%>
<%=htmpl.text()%>
