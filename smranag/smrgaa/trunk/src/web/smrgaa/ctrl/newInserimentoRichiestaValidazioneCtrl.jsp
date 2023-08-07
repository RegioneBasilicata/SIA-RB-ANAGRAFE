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

  String iridePageName = "newInserimentoRichiestaValidazioneCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  WebUtils.removeUselessFilter(session, null); 
  
  String newInserimentoRichiestaValidazioneUrl = "/view/newInserimentoRichiestaValidazioneView.jsp";
  String newInserimentoConfermaValidazioneUrl = "/ctrl/newInserimentoConfermaValidazioneCtrl.jsp"; 
  
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  //prima volta che entro
  if(Validator.isEmpty(request.getParameter("regimeInserimentoRichiestaValidazione")))
  {
    session.removeAttribute("anagAziendaVO");
  }
  
  
  String testoHelp = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_DV_AP_ANA1);
  request.setAttribute("testoHelp", testoHelp);
  
  
  String descDichiarazione = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_DICH_RICH_VAL_PAG);
  request.setAttribute("descDichiarazione", descDichiarazione);
  
  
  String msgErrore = "";
  //arrivo dalla selezione del tasto nuova iscrizione dopo diversi passaggi!!!
  String idAzienda = request.getParameter("idAzienda");
  AnagAziendaVO anagAziendaVO = anagFacadeClient.findAziendaAttiva(new Long(idAzienda));
  if(anagAziendaVO.getDelegaVO() != null)
  {
    msgErrore = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_MSG_NAP_PAG1_CAA);
  }
  request.setAttribute("anagAziendaVO", anagAziendaVO);
  
  Vector<CodeDescription> vMotivoDichiarazione = anagFacadeClient.getTipiMotivoDichiarazione();
  request.setAttribute("vMotivoDichiarazione", vMotivoDichiarazione);
  
  AziendaNuovaVO aziendaNuovaVO = gaaFacadeClient.getRichAzByIdAzienda(new Long(idAzienda).longValue(), 
      SolmrConstants.RICHIESTA_VALIDAZIONE);
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
		        <jsp:forward page = "<%=newInserimentoConfermaValidazioneUrl%>" />
		      <%
		      return;
		    }
      }
    }    
  
  }
  
  
  
  
  String note = request.getParameter("note");
  String idMotivoDichiarazione = request.getParameter("idMotivoDichiarazione");
  String dichiarazione = request.getParameter("dichiarazione");  
  if(request.getParameter("avantiInsAzienda") != null) 
  {
    ValidationErrors errors = new ValidationErrors();
    if(Validator.isEmpty(note))
    {
      errors.add("note", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
    }
    else
    {
      if(note.length() > 1000) 
      {
        errors.add("note", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
      }
    }
    if (Validator.isEmpty(idMotivoDichiarazione))
    {
      errors.add("idMotivoDichiarazione", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
    }
    
    if (Validator.isEmpty(dichiarazione))
    {
      errors.add("dichiarazione", new ValidationError("E' obbligatorio selezionare "+descDichiarazione));
    }
    
      
    
	  if (! (errors == null || errors.size() == 0)) 
	  {
	    request.setAttribute("errors", errors);
	    %>
	      <jsp:forward page = "<%=newInserimentoRichiestaValidazioneUrl%>" />
	    <%
	    return;
	  }	  
	  
    
    
  
	  
	  if(Validator.isEmpty(msgErrore))
	  {
	    //Salvataggio dati....
	    RichiestaAziendaVO richiestaAziendaVO = new RichiestaAziendaVO();
      richiestaAziendaVO.setNote(note);
      richiestaAziendaVO.setIdMotivoDichiarazione(new Integer(idMotivoDichiarazione));
      richiestaAziendaVO.setIdTipoRichiesta(new Long(SolmrConstants.RICHIESTA_VALIDAZIONE));
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
	      <jsp:forward page = "<%=newInserimentoConfermaValidazioneUrl%>" />
	    <%
	    return;	    
	  }  
	  else
	  {
	    request.setAttribute("msgErrore", msgErrore);
	    %>
        <jsp:forward page = "<%=newInserimentoRichiestaValidazioneUrl%>" />
      <%
      return;
	  }
	  
	  

    
  }
%>
  <jsp:forward page = "<%=newInserimentoRichiestaValidazioneUrl%>" />

