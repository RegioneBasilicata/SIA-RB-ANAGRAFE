<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.smranag.smrgaa.dto.AllegatoDocumentoVO"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/documentoAllegati.htm");
 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

 	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 	DocumentoVO documentoVO = (DocumentoVO)session.getAttribute("documentoAllegatoVO");
  
  

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);

 	// Se si è verificato un errore
 	if(Validator.isNotEmpty(messaggioErrore)) 
  {
 		htmpl.newBlock("blkDettaglioDocumentoKo");
 		htmpl.set("blkDettaglioDocumentoKo.messaggioErrore", messaggioErrore);
 	}
 	// Altrimenti popolo la sezione relativa al dettaglio
 	else 
  {
  
  
 		htmpl.set("idDocumento", documentoVO.getIdDocumento().toString());
 		htmpl.newBlock("blkDettaglioDocumentoOk");
 		htmpl.set("blkDettaglioDocumentoOk.descTipoTipologiaDocumento", documentoVO.getTipoTipologiaDocumento().getDescription());
 		htmpl.set("blkDettaglioDocumentoOk.descTipoCategoriaDocumento", documentoVO.getTipoCategoriaDocumentoVO().getDescrizione());
 		htmpl.set("blkDettaglioDocumentoOk.descTipoDocumento", documentoVO.getTipoDocumentoVO().getDescrizione());
 		htmpl.set("blkDettaglioDocumentoOk.dataInizioValidita", DateUtils.formatDate(documentoVO.getDataInizioValidita()));
 		if(Validator.isNotEmpty(documentoVO.getDataFineValidita())) {
   		htmpl.set("blkDettaglioDocumentoOk.dataFineValidita", DateUtils.formatDate(documentoVO.getDataFineValidita()));
 		}
 		if(Validator.isNotEmpty(documentoVO.getNumeroProtocollo())) {
   		htmpl.set("blkDettaglioDocumentoOk.numeroProtocollo", StringUtils.parseNumeroProtocolloField(documentoVO.getNumeroProtocollo()));
 		}
 		if(Validator.isNotEmpty(documentoVO.getDataProtocollo())) {
   		htmpl.set("blkDettaglioDocumentoOk.dataProtocollo", DateUtils.formatDate(documentoVO.getDataProtocollo()));
 		}
 		if(Validator.isNotEmpty(documentoVO.getNumeroProtocolloEsterno())) {
   		htmpl.set("blkDettaglioDocumentoOk.numeroProtocolloEsterno", documentoVO.getNumeroProtocolloEsterno());
 		}
 		
 		/*if(documentoVO.getvAllegatoDocumento() != null)
	  {
	    for(int i=0;i<documentoVO.getvAllegatoDocumento().size();i++)
	    {
	      htmpl.newBlock("blkDettaglioDocumentoOk.blkFileAllegato");
	      
	      AllegatoDocumentoVO allegatoDocumentoVO = documentoVO.getvAllegatoDocumento().get(i);
	      
	      htmpl.set("blkDettaglioDocumentoOk.blkFileAllegato.idAllegato", 
	        ""+allegatoDocumentoVO.getIdAllegato());
	        
	        
	        
	     String immagineStampa = "";
       if(Validator.isNotEmpty(allegatoDocumentoVO.getIdTipoFirma()))
       {
         if(allegatoDocumentoVO.getIdTipoFirma().longValue() == 1)
         {
           immagineStampa = "firmaCarta";
         }
         else if(allegatoDocumentoVO.getIdTipoFirma().longValue() == 2)
         {
           immagineStampa = "firmaTablet";
         }
         else if(allegatoDocumentoVO.getIdTipoFirma().longValue() == 3)
         {
           immagineStampa = "firmaElettronica";
         }
       }
       else
       {
         immagineStampa = "noFirma";
       }
       
       htmpl.set("blkDettaglioDocumentoOk.blkFileAllegato.immagineStampa", 
         immagineStampa);
	        
	        
	        
	        
	        
	      htmpl.set("blkDettaglioDocumentoOk.blkFileAllegato.titleAllegato", 
	        allegatoDocumentoVO.getNomeLogico() +" (" +
	        allegatoDocumentoVO.getNomeFisico()+")");
	    }
	  }*/
  }
  
  
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  
  
  AllegatoDocumentoVO allegatoDocumentoVO = (AllegatoDocumentoVO) request.getAttribute("allegatoDocumentoVO");
  htmpl.set("idDocumento", ""+allegatoDocumentoVO.getIdDocumento());
  //htmpl.set("nomeAllegato", allegatoDocumentoVO.getNomeLogico());
  
  Vector<AllegatoDocumentoVO> vElencoFileAllegati = (Vector<AllegatoDocumentoVO>) request.getAttribute("vElencoFileAllegati");
  if (vElencoFileAllegati != null &&
      vElencoFileAllegati.size() >0)
  {
    htmpl.newBlock("blkDettaglioDocumentoOk.fileAllegatiBlk");

    for ( int i=0; i<vElencoFileAllegati.size(); i++)
    {
      allegatoDocumentoVO = (AllegatoDocumentoVO) vElencoFileAllegati.get(i);
      htmpl.newBlock("blkDettaglioDocumentoOk.fileAllegatiBlk.fileBlk");
      
      htmpl.set("blkDettaglioDocumentoOk.fileAllegatiBlk.fileBlk.idAllegato", allegatoDocumentoVO.getIdAllegato().toString());
      htmpl.set("blkDettaglioDocumentoOk.fileAllegatiBlk.fileBlk.nome", allegatoDocumentoVO.getNomeLogico());
     
      String immagineStampa = "";
      if(Validator.isNotEmpty(allegatoDocumentoVO.getIdTipoFirma()))
      {
        if(allegatoDocumentoVO.getIdTipoFirma().longValue() == 1)
        {
          immagineStampa = "firmaCarta";
        }
        else if(allegatoDocumentoVO.getIdTipoFirma().longValue() == 2)
        {
          immagineStampa = "firmaTablet";
        }
        else if(allegatoDocumentoVO.getIdTipoFirma().longValue() == 3)
        {
          immagineStampa = "firmaElettronica";
        }
      }
      else
      {
        immagineStampa = "noFirma";
      }
       
      htmpl.set("blkDettaglioDocumentoOk.fileAllegatiBlk.fileBlk.immagineStampa", 
         immagineStampa); 
      
      htmpl.set("blkDettaglioDocumentoOk.fileAllegatiBlk.fileBlk.titleAllegato", allegatoDocumentoVO.getNomeLogico()+" ("
        +allegatoDocumentoVO.getNomeFisico()+")");
      htmpl.set("blkDettaglioDocumentoOk.fileAllegatiBlk.fileBlk.dataInserimento", DateUtils.formatDateTimeNotNull(allegatoDocumentoVO.getDataRagistrazione()));       
    }
  }
  
  if(Validator.isNotEmpty(errors) && (errors.size() == 0))
    errors = null;
  
  HtmplUtil.setErrors(htmpl, errors, request, application);
    
    
   	
   	
%>
<%= htmpl.text()%>
