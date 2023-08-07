<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="java.util.Date" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.*" %>
<%@ page import="java.text.*"%>
<%@ page import="java.math.BigDecimal"%>

<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/newInserimentoAllegati.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  htmpl.set("testoHelp", (String)request.getAttribute("testoHelp"), null);
  
  if(request.getAttribute("messaggio") != null)
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", (String)request.getAttribute("messaggio"));
  }
  
  String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                          "title=\"{2}\" border=\"0\"></a>";
  String imko = "ko.gif";
  StringProcessor jssp = new JavaScriptStringProcessor();
  
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors"); 
  Vector<RichiestaAziendaDocumentoVO> vAllegatiAziendaNuova = (Vector<RichiestaAziendaDocumentoVO>)request.getAttribute("vAllegatiAziendaNuova");   
  Vector<TipoDocumentoVO> vTipoDocumento = (Vector<TipoDocumentoVO>)request.getAttribute("vTipoDocumento");
  String extIdDocumento = request.getParameter("extIdDocumento");
  BigDecimal idDocIdentita = (BigDecimal)request.getAttribute("idDocIdentita");
  AziendaNuovaVO aziendaNuovaVO = (AziendaNuovaVO)request.getAttribute("aziendaNuovaVO");
  
  
  String numRow = "3";
  if(vTipoDocumento != null)
  {
	  for(int j=0;j<vTipoDocumento.size();j++)
	  {
	    htmpl.newBlock("blkTipoDocumento");
	    htmpl.set("blkTipoDocumento.extIdDocumento", ""+vTipoDocumento.get(j).getIdDocumento());
	    htmpl.set("blkTipoDocumento.descDocumento", vTipoDocumento.get(j).getDescrizione()); 
	    
	    if(Validator.isNotEmpty(extIdDocumento)
	      && extIdDocumento.equalsIgnoreCase(vTipoDocumento.get(j).getIdDocumento().toString()))
		  {
		    htmpl.set("blkTipoDocumento.selected","selected", null);
		    if("S".equalsIgnoreCase(vTipoDocumento.get(j).getFlagObbligoEnteNumero()))
		    {
			    htmpl.newBlock("blkFlagEnteNumero");
			    numRow = "5";
			    htmpl.set("blkFlagEnteNumero.numeroDocumento", request.getParameter("numeroDocumento"));
			    htmpl.set("blkFlagEnteNumero.enteRilascioDocumento", request.getParameter("enteRilascioDocumento")); 
			  }
			  
			  		  
		  }
	  }
	}
	
	htmpl.set("numRow", numRow);
	
	htmpl.set("dataInizioValidita", request.getParameter("dataInizioValidita"));
	htmpl.set("dataFineValidita", request.getParameter("dataFineValidita"));
	
	
	
	
  
  
  
  boolean trovatoAllegato = false;
  if(vAllegatiAziendaNuova != null)
  {
    htmpl.newBlock("blkAllegati");
    
    htmpl.set("blkAllegati.testoDichiarazione", (String)request.getAttribute("testoDichiarazione"));
    
    for(int i=0;i<vAllegatiAziendaNuova.size();i++)
    {
      htmpl.newBlock("blkAllegati.blkElencoAllegati");
      RichiestaAziendaDocumentoVO richiestaAziendaDocumentoVO = vAllegatiAziendaNuova.get(i);
      htmpl.set("blkAllegati.blkElencoAllegati.idRiga", ""+i);
      htmpl.set("blkAllegati.blkElencoAllegati.descDocumento", ""+richiestaAziendaDocumentoVO.getDescDocumento());
      htmpl.set("blkAllegati.blkElencoAllegati.dataInizioValidita", DateUtils.formatDateNotNull(richiestaAziendaDocumentoVO.getDataInizioValidita()));
      htmpl.set("blkAllegati.blkElencoAllegati.dataFineValidita", DateUtils.formatDateNotNull(richiestaAziendaDocumentoVO.getDataFineValidita()));
      htmpl.set("blkAllegati.blkElencoAllegati.numeroDocumento", richiestaAziendaDocumentoVO.getNumeroDocumento());
      htmpl.set("blkAllegati.blkElencoAllegati.enteRilascioDocumento", richiestaAziendaDocumentoVO.getEnteRilascioDocumento());
      htmpl.set("blkAllegati.blkElencoAllegati.idRichiestaAziendaDocumento", ""+richiestaAziendaDocumentoVO.getIdRichiestaAziendaDocumento());
      
      
      if(richiestaAziendaDocumentoVO.getvAllegatoDocumento() != null)
		  {
		    for(int j=0;j<richiestaAziendaDocumentoVO.getvAllegatoDocumento().size();j++)
		    {
		      htmpl.newBlock("blkAllegati.blkElencoAllegati.blkFileAllegato");
		      htmpl.set("blkAllegati.blkElencoAllegati.blkFileAllegato.idAllegato", 
		        ""+richiestaAziendaDocumentoVO.getvAllegatoDocumento().get(j).getIdAllegato());
		      htmpl.set("blkAllegati.blkElencoAllegati.blkFileAllegato.titleAllegato", 
		        richiestaAziendaDocumentoVO.getvAllegatoDocumento().get(j).getNomeLogico() +" (" +
		        richiestaAziendaDocumentoVO.getvAllegatoDocumento().get(j).getNomeFisico()+")");
		    }
		  }
		  
		  if(richiestaAziendaDocumentoVO.getExtIdDocumento().longValue()
         == idDocIdentita.longValue())
      {
        if(!trovatoAllegato
          && Validator.isNotEmpty(richiestaAziendaDocumentoVO.getvAllegatoDocumento()))
        {
          trovatoAllegato = true;
        }
      }
    }
  }
  
  if(trovatoAllegato)
  {
    htmpl.set("blkAllegati.disabledDichiarazione", "disabled=\"true\"", null);
  }
  else if(request.getParameter("regimeInserimentoAllegati") == null)
  {
    if("S".equalsIgnoreCase(aziendaNuovaVO.getFlagDichiarazioneAllegati()))
    {
      htmpl.set("blkAllegati.checkedDichiarazione", "checked=\"true\"", null);
    }
  }  
  else if(Validator.isNotEmpty(request.getParameter("chkDichiarazione"))
    && "S".equalsIgnoreCase(request.getParameter("chkDichiarazione")))
  {
    htmpl.set("blkAllegati.checkedDichiarazione", "checked=\"true\"", null);
  }
  
  
  
  
  
  
  
  HtmplUtil.setErrors(htmpl, errors, request, application);
  
  
	
	


      
  

%>
<%= htmpl.text() %>
