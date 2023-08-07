<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%@page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.RichiestaAziendaVO" %>
<%@page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String iridePageName = "newInserimentoRichiestaCessazioneCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  WebUtils.removeUselessFilter(session, null); 
  
  String newInserimentoRichiestaCessazioneUrl = "/view/newInserimentoRichiestaCessazioneView.jsp";
  String newInserimentoConfermaCessazioneUrl = "/ctrl/newInserimentoConfermaCessazioneCtrl.jsp"; 
  
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  //prima volta che entro
  if(Validator.isEmpty(request.getParameter("regimeInserimentoRichiestaCessazione")))
  {
    session.removeAttribute("anagAziendaVO");
  }
  
  
  String testoHelp = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_DC_AP_ANA1);
  request.setAttribute("testoHelp", testoHelp);
  
  
  String msgErrore = "";
  String idAzienda = request.getParameter("idAzienda");
  AnagAziendaVO anagAziendaVO = anagFacadeClient.findAziendaAttiva(new Long(idAzienda));
  //Eliminato controllo su delega la regine può cessare....
  /*if(anagAziendaVO.getDelegaVO() == null)
  {
    msgErrore = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_MSG_NAP_PAG1_CAA);
  }*/
  if(Validator.isNotEmpty(anagAziendaVO.getDataCessazione()))
  {
    msgErrore = "Impossibile inserire una richiesta di cessazione: l'azienda risulta gia' cessata";
  }
  request.setAttribute("anagAziendaVO", anagAziendaVO);
  
  TipoCessazioneVO[] aTipoCessazione = anagFacadeClient.getListTipoCessazione(null,true);
  request.setAttribute("aTipoCessazione", aTipoCessazione);
  
  AziendaNuovaVO aziendaNuovaVO = gaaFacadeClient.getRichAzByIdAzienda(new Long(idAzienda).longValue(), 
      SolmrConstants.RICHIESTA_CESSAZIONE);
  request.setAttribute("aziendaNuovaVO", aziendaNuovaVO);
  
  if(Validator.isEmpty(msgErrore))
  {    
      
    if(Validator.isNotEmpty(aziendaNuovaVO))
    {
      if((aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_BOZZA) > 0)
        && (aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_VALIDATA) < 0))
      {
        {
		      //vado direttamente nella pagina di conferma...
		      %>
		        <jsp:forward page = "<%=newInserimentoConfermaCessazioneUrl%>" />
		      <%
		      return;
		    }
      }
    }    
  
  }
  
  
  
  
  String note = request.getParameter("note");
  String idCessazione = request.getParameter("idCessazione");
  String dataCessazioneStr = request.getParameter("dataCessazione");
  String cuaaSubentrante = request.getParameter("cuaaSubentrante");  
  String denominazioneSubentrante = request.getParameter("denominazioneSubentrante");    
  if(request.getParameter("avantiInsAzienda") != null) 
  {
    ValidationErrors errors = new ValidationErrors();
    if(Validator.isEmpty(note))
    {
      //Selezionato "Altro"
      if(Validator.isNotEmpty(idCessazione) && idCessazione.equalsIgnoreCase("6"))
        errors.add("note", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
    }
    else
    {
      if(note.length() > 1000) 
      {
        errors.add("note", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
      }
    }
    if (Validator.isEmpty(idCessazione))
    {
      errors.add("idCessazione", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
    }
    if (Validator.isEmpty(dataCessazioneStr))
    {
      errors.add("dataCessazione", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
    }
    else
    {
      if (!Validator.validateDateF(dataCessazioneStr))
      {
        errors.add("dataCessazione", new ValidationError(
            "Inserire la data cessazione nel formato corretto"));
      }
    }
    
    if(Validator.isNotEmpty(cuaaSubentrante))
    {
      cuaaSubentrante = cuaaSubentrante.trim();
      if(!(cuaaSubentrante.length() == 11 || cuaaSubentrante.length() == 16))
      {
        errors.add("cuaaSubentrante", new ValidationError(
            "Il Cuaa non e'' stato inserito correttamente"));
      }
    }
    
    if(Validator.isNotEmpty(denominazioneSubentrante))
    {
      denominazioneSubentrante = denominazioneSubentrante.trim();
    }
    
      
    
	  if (! (errors == null || errors.size() == 0)) 
	  {
	    request.setAttribute("errors", errors);
	    %>
	      <jsp:forward page = "<%=newInserimentoRichiestaCessazioneUrl%>" />
	    <%
	    return;
	  }	  
	  
    
    
  
	  
	  if(Validator.isEmpty(msgErrore))
	  {
	    //Salvataggio dati....
	    RichiestaAziendaVO richiestaAziendaVO = new RichiestaAziendaVO();
      richiestaAziendaVO.setNote(note);
      richiestaAziendaVO.setIdCessazione(new Long(idCessazione));
      richiestaAziendaVO.setDataCessazione(DateUtils.parseDate(dataCessazioneStr));
      //Ricerco se l'aziuenda subentrante è già presente su anagrafe...
      if(Validator.isNotEmpty(cuaaSubentrante))
      {
        AnagAziendaVO anagAziendaVOTmp = new AnagAziendaVO();
        anagAziendaVOTmp.setCUAA(cuaaSubentrante);
        Vector<Long> vectIdAnagAzienda = anagFacadeClient.getListOfIdAzienda(anagAziendaVOTmp,
          null, false);
        if(vectIdAnagAzienda.size() > 0)
        {
          //prendo il primo come specifiche di terry il 27/08/2014
          richiestaAziendaVO.setIdAziendaSubentrante(vectIdAnagAzienda.get(0));
        }
        //cuaa e denominazione vengono solo valorizzati se non preenti in anagrafe
        else
        {
          richiestaAziendaVO.setCuaaSubentrante(cuaaSubentrante);
          richiestaAziendaVO.setDenominazioneSubentrante(denominazioneSubentrante);
        }  
      }
      
      richiestaAziendaVO.setIdTipoRichiesta(new Long(SolmrConstants.RICHIESTA_CESSAZIONE));
      richiestaAziendaVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
	    //update
	    if(Validator.isNotEmpty(aziendaNuovaVO)
	      && (aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_BOZZA) == 0))	        
	    {
	      richiestaAziendaVO.setIdRichiestaAzienda(aziendaNuovaVO.getIdRichiestaAzienda());
	      gaaFacadeClient.updateRichiestaAzienda(richiestaAziendaVO);
	    }
      //Prima volta
      else
      {
        richiestaAziendaVO.setIdAzienda(new Long(idAzienda));
		    gaaFacadeClient.insertAziendaNuovaRichiestaValCess(richiestaAziendaVO);	
		  }  
		   
	    %>
	      <jsp:forward page = "<%=newInserimentoConfermaCessazioneUrl%>" />
	    <%
	    return;	    
	  }  
	  else
	  {
	    request.setAttribute("msgErrore", msgErrore);
	    %>
        <jsp:forward page = "<%=newInserimentoRichiestaCessazioneUrl%>" />
      <%
      return;
	  }

    
  }
%>
  <jsp:forward page = "<%=newInserimentoRichiestaCessazioneUrl%>" />

