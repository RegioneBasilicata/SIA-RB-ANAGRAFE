<%@ page language="java"
contentType="text/html"
isErrorPage="true"
%>


<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.consistenza.TipoControlloVO "%>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="it.csi.smranag.smrgaa.dto.allevamenti.ControlloAllevamenti" %>

<%
  SolmrLogger.debug(this, " - terreniVerificaView.jsp - INIZIO PAGINA");

  java.io.InputStream layout = application.getResourceAsStream("/layout/allevamentiVerifica.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  TipoControlloVO[] elencoTipiControllo = (TipoControlloVO[])request.getAttribute("elencoTipiControllo");
  String idControllo = request.getParameter("idControllo");
  String segnalazioneBloccante = request.getParameter("segnalazioneBloccante");
  String segnalazioneWarning = request.getParameter("segnalazioneWarning");
  String segnalazioneOk = request.getParameter("segnalazioneOk");
  String operazione = request.getParameter("operazione");
  AnagAziendaVO aziendaVOTmp = (AnagAziendaVO)request.getAttribute("aziendaVOTmp");
  Vector<TipoControlloVO> elencoTipiControlloEsito = (Vector<TipoControlloVO>)request.getAttribute("elencoTipiControlloEsito");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  HtmplUtil.setValues(htmpl, anagAziendaVO);

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  SolmrLogger.debug(this, "anagAziendaVO.getCUAA(): " + anagAziendaVO.getCUAA());

  //Vector<ErrAnomaliaDicConsistenzaVO> anomalie=(Vector<ErrAnomaliaDicConsistenzaVO>)request.getAttribute("anomalie");

  HashMap<Long, ControlloAllevamenti> hEsitoControllo 
    = (HashMap<Long, ControlloAllevamenti>)request.getAttribute("hEsitoControllo");
  HashMap<Long, ControlloAllevamenti> hSegnalazioneControllo  
    = (HashMap<Long, ControlloAllevamenti>)request.getAttribute("hSegnalazioneControllo");
  
  
  
  
  if(Validator.isNotEmpty(aziendaVOTmp.getDataControlliAllevamenti()))
  {
    htmpl.set("dataControlli", DateUtils.formatDateTimeNotNull(aziendaVOTmp.getDataControlliAllevamenti()));
  }
  else
  {
    htmpl.set("dataControlli", "non sono stati eseguiti controlli di validit&agrave sugli allevamenti", null);
  }
  
  
  // Combo tipo controllo
  if(elencoTipiControllo != null && elencoTipiControllo.length > 0) 
  {
    for(int i = 0; i < elencoTipiControllo.length; i++) 
    {
      TipoControlloVO tipoControlloVO = (TipoControlloVO)elencoTipiControllo[i];
      htmpl.newBlock("blkTipoControllo");
      htmpl.set("blkTipoControllo.idControllo", tipoControlloVO.getIdControllo().toString());
      htmpl.set("blkTipoControllo.descrizione", tipoControlloVO.getDescrizione());
      if(Validator.isNotEmpty(idControllo)) 
      {
        if(idControllo.equalsIgnoreCase(tipoControlloVO.getIdControllo().toString())) 
        {
          htmpl.set("blkTipoControllo.selected", "selected=\"selected\"");
        }
      }
    }
  }
  
  String sourceImage = null;
  if(pathToFollow.equalsIgnoreCase("rupar")) {
    sourceImage = application.getInitParameter("erroriRupar");
  }
  else if (pathToFollow.equalsIgnoreCase("sispie")) {
    sourceImage = application.getInitParameter("erroriSispie");
  }
  else if (pathToFollow.equalsIgnoreCase("TOBECONFIG")) {
	    sourceImage = application.getInitParameter("erroriTOBECONFIG");
  }
  htmpl.set("srcBloccante", sourceImage+(String)SolmrConstants.get("IMMAGINE_BLOCCANTE"));
  htmpl.set("descImmagineBloccante", (String)SolmrConstants.DESC_TITLE_BLOCCANTE);  
  htmpl.set("srcWarning", sourceImage+(String)SolmrConstants.get("IMMAGINE_WARNING"));
  htmpl.set("descImmagineWarning", (String)SolmrConstants.DESC_TITLE_WARNING);  
  htmpl.set("srcOk", sourceImage+(String)SolmrConstants.get("IMMAGINE_OK"));
  htmpl.set("descImmagineOk", (String)SolmrConstants.DESC_TITLE_POSITIVO);  
  
  if(Validator.isNotEmpty(segnalazioneBloccante)) 
  {
    htmpl.set("checkedBloccante", "checked=\"checked\""); 
  }
  if(Validator.isNotEmpty(segnalazioneWarning)) 
  {
    htmpl.set("checkedWarning", "checked=\"checked\""); 
  }
  if(Validator.isNotEmpty(segnalazioneOk)) 
  {
    htmpl.set("checkedOk", "checked=\"checked\"");  
  }

  if(elencoTipiControlloEsito.size() > 0) 
  {
    htmpl.newBlock("blkTabAnomalia");
	  // Gestisco le frecce relative all'ordinamento:
	  if(Validator.isNotEmpty(operazione)) 
	  {
	    if(operazione.equalsIgnoreCase("idTipologiaAsc")) 
	    {
	      htmpl.set("blkTabAnomalia.ordinaTipologia", "giu");
	      htmpl.set("blkTabAnomalia.descOrdinaTipologia", "ordine decrescente");
	      htmpl.set("blkTabAnomalia.tipoOrdinaTipologia", "idTipologiaDisc");
	    }
	    else 
	    {
	      htmpl.set("blkTabAnomalia.ordinaTipologia", "su");
	      htmpl.set("blkTabAnomalia.descOrdinaTipologia", "ordine crescente");
	      htmpl.set("blkTabAnomalia.tipoOrdinaTipologia", "idTipologiaAsc");
	    }
	  }
	  else 
	  {
	    htmpl.set("blkTabAnomalia.ordinaTipologia", "giu");
	    htmpl.set("blkTabAnomalia.descOrdinaTipologia", "ordine decrescente");
	    htmpl.set("blkTabAnomalia.tipoOrdinaTipologia", "idTipologiaDisc");
	  }

    for(int i = 0; i < elencoTipiControlloEsito.size(); i++) 
    {
      htmpl.newBlock("blkTabAnomalia.blkTotale");
      TipoControlloVO tipoControllo = elencoTipiControlloEsito.get(i);
      Long idControlloTmp = tipoControllo.getIdControllo();
      if((hEsitoControllo != null) && (hEsitoControllo.get(idControlloTmp) != null))
      {
        ControlloAllevamenti ctrlAllevamenti = hEsitoControllo.get(idControlloTmp);
        String bloccante = ctrlAllevamenti.getBloccante();
        htmpl.newBlock("blkTabAnomalia.blkTotale.blkAnomalia");
        htmpl.set("blkTabAnomalia.blkTotale.blkAnomalia.aggancio", ""+i);
        String immagine = "";
        String descImmagine = "";
        if("S".equalsIgnoreCase(bloccante))
        {
          immagine = sourceImage+(String)SolmrConstants.get("IMMAGINE_BLOCCANTE");
          descImmagine = SolmrConstants.DESC_TITLE_BLOCCANTE;
        }
        else
        {
          immagine = sourceImage+(String)SolmrConstants.get("IMMAGINE_WARNING");
          descImmagine = SolmrConstants.DESC_TITLE_WARNING;
        }        
        htmpl.set("blkTabAnomalia.blkTotale.blkAnomalia.immagine", immagine);
        htmpl.set("blkTabAnomalia.blkTotale.blkAnomalia.descImmagine", descImmagine);
        
        htmpl.set("blkTabAnomalia.blkTotale.blkAnomalia.descrizione", tipoControllo.getDescrizione());
        htmpl.set("blkTabAnomalia.blkTotale.blkAnomalia.numeroAnomalie", ""+ctrlAllevamenti.getvAllevamenti().size());
      
        htmpl.newBlock("blkTabAnomalia.blkTotale.blkAnomalia.blkSingolo");
        htmpl.set("blkTabAnomalia.blkTotale.blkAnomalia.blkSingolo.aggancio", ""+i);
        
        htmpl.newBlock("blkTabAnomalia.blkAnomalia.blkSingolo.blkLungo");
        for(int k=0;k<ctrlAllevamenti.getvAllevamenti().size();k++)
        {
          htmpl.newBlock("blkTabAnomalia.blkTotale.blkAnomalia.blkSingolo.blkLungo.blkInternoLungo");
          htmpl.set("blkTabAnomalia.blkTotale.blkAnomalia.blkSingolo.blkLungo.blkInternoLungo.tipoSpecie", 
            ctrlAllevamenti.getvAllevamenti().get(k).getTipoSpecieAnimaleAnagVO().getDescrizione());
          htmpl.set("blkTabAnomalia.blkTotale.blkAnomalia.blkSingolo.blkLungo.blkInternoLungo.codiceAziendaZootecnica", 
            ctrlAllevamenti.getvAllevamenti().get(k).getCodiceAziendaZootecnica());
          String detentore = "";
          if(Validator.isNotEmpty(ctrlAllevamenti.getvAllevamenti().get(k).getCodiceFiscaleDetentore()))
          {
            detentore = ctrlAllevamenti.getvAllevamenti().get(k).getCodiceFiscaleDetentore() 
              +" - "+ctrlAllevamenti.getvAllevamenti().get(k).getDenominazioneDetentore();
          }
          htmpl.set("blkTabAnomalia.blkTotale.blkAnomalia.blkSingolo.blkLungo.blkInternoLungo.detentore", 
            detentore);
          String proprietario = "";
          if(Validator.isNotEmpty(ctrlAllevamenti.getvAllevamenti().get(k).getCodiceFiscaleProprietario()))
          {
            proprietario = ctrlAllevamenti.getvAllevamenti().get(k).getCodiceFiscaleProprietario() 
              +" - "+ctrlAllevamenti.getvAllevamenti().get(k).getDenominazioneProprietario();
          }
          htmpl.set("blkTabAnomalia.blkTotale.blkAnomalia.blkSingolo.blkLungo.blkInternoLungo.proprietario", 
            proprietario);
          htmpl.set("blkTabAnomalia.blkTotale.blkAnomalia.blkSingolo.blkLungo.blkInternoLungo.descAnomalia", 
            ctrlAllevamenti.getDescrizione());
        }
      }
      else if((hSegnalazioneControllo != null) && (hSegnalazioneControllo.get(idControlloTmp) != null))
      {
        ControlloAllevamenti ctrlAllevamenti = hSegnalazioneControllo.get(idControlloTmp);
        String bloccante = ctrlAllevamenti.getBloccante();
        htmpl.newBlock("blkTabAnomalia.blkTotale.blkAnomalia");
        htmpl.set("blkTabAnomalia.blkTotale.blkAnomalia.aggancio", ""+i);
        String immagine = "";
        String descImmagine = "";
        if("S".equalsIgnoreCase(bloccante))
        {
          immagine = sourceImage+(String)SolmrConstants.get("IMMAGINE_BLOCCANTE");
          descImmagine = SolmrConstants.DESC_TITLE_BLOCCANTE;
        }
        else
        {
          immagine = sourceImage+(String)SolmrConstants.get("IMMAGINE_WARNING");
          descImmagine = SolmrConstants.DESC_TITLE_WARNING;
        }        
        htmpl.set("blkTabAnomalia.blkTotale.blkAnomalia.immagine", immagine);
        htmpl.set("blkTabAnomalia.blkTotale.blkAnomalia.descImmagine", descImmagine);
        
        htmpl.set("blkTabAnomalia.blkTotale.blkAnomalia.descrizione", tipoControllo.getDescrizione());
        htmpl.set("blkTabAnomalia.blkTotale.blkAnomalia.numeroAnomalie", "1");
        
        htmpl.newBlock("blkTabAnomalia.blkTotale.blkAnomalia.blkSingolo");
        htmpl.set("blkTabAnomalia.blkTotale.blkAnomalia.blkSingolo.aggancio", ""+i);
        
        htmpl.newBlock("blkTabAnomalia.blkTotale.blkAnomalia.blkSingolo.blkCorto");
        htmpl.set("blkTabAnomalia.blkTotale.blkAnomalia.blkSingolo.blkCorto.descrizioneSegnalazione", 
          ctrlAllevamenti.getDescrizione());
        
      }
      else
      {
        htmpl.newBlock("blkTabAnomalia.blkTotale.blkPositivo");
        htmpl.set("blkTabAnomalia.blkTotale.blkPositivo.immagine", sourceImage+(String)SolmrConstants.get("IMMAGINE_OK"));
        htmpl.set("blkTabAnomalia.blkTotale.blkPositivo.descImmagine", SolmrConstants.DESC_TITLE_POSITIVO);
        
        htmpl.set("blkTabAnomalia.blkTotale.blkPositivo.descrizione", tipoControllo.getDescrizione());
        htmpl.set("blkTabAnomalia.blkTotale.blkPositivo.numeroAnomalie", "");
      }
      
            
       
    }
	  
  }
  else
  {
    htmpl.newBlock("blkNoTabAnomalia");
    htmpl.set("blkNoTabAnomalia.noRecordTrovati",AnagErrors.ERRORE_NOAZIENDE_TROVATE);
  }


  HtmplUtil.setErrors(htmpl, errors, request, application);

  SolmrLogger.debug(this, " - allevamentiVerificaView.jsp - FINE PAGINA");
%>

<%= htmpl.text()%>

