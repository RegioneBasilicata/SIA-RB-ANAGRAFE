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
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>


<%

  String iridePageName = "newInserimentoRichiestaVariazioneCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  WebUtils.removeUselessFilter(session, null); 
  
  String newInserimentoRichiestaVariazioneUrl = "/view/newInserimentoRichiestaVariazioneView.jsp";
  String newInserimentoConfermaVariazioneUrl = "/ctrl/newInserimentoConfermaVariazioneCtrl.jsp";
  String newInserimentoRichVarIrrUrl = "/ctrl/newInserimentoRichVarIrrCtrl.jsp"; 
  String newInserimentoRichSoggAssUrl = "/ctrl/newInserimentoRichSoggAssCtrl.jsp"; 
  String newInserimentoRichSoggAssCaaUrl = "/ctrl/newInserimentoRichSoggAssCaaCtrl.jsp"; 
  
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  //prima volta che entro
  if(Validator.isEmpty(request.getParameter("regimeInserimentoRichiestaVariazione")))
  {
    session.removeAttribute("anagAziendaVO");
  }
  
  
  String testoHelp = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_DV_ET_PAG1);
  request.setAttribute("testoHelp", testoHelp);
  
  
  String msgErrore = "";
  
  String idAzienda = request.getParameter("idAzienda");
  if(Validator.isEmpty(idAzienda))
    idAzienda = request.getParameter("idAziendaInd");
    
    
  AnagAziendaVO anagAziendaVO = anagFacadeClient.findAziendaAttiva(new Long(idAzienda));
  if(Validator.isNotEmpty(anagAziendaVO.getDataCessazione()))
  {
    msgErrore = "Impossibile inserire una richiesta di cessazione: l'azienda risulta gia' cessata";
  }
  request.setAttribute("anagAziendaVO", anagAziendaVO);
  
  Vector<CodeDescription> vTipoRichiestaVariazione = gaaFacadeClient
    .getListTipoRichiestaVariazione(ruoloUtenza.getCodiceRuolo());
  if(!"S".equalsIgnoreCase(anagAziendaVO.getFlagFormaAssociata()))
  {
    for(int i=0;i<vTipoRichiestaVariazione.size();i++)
    {
      if(vTipoRichiestaVariazione.get(i).getCode().compareTo(new Integer(6)) == 0)
      {
        vTipoRichiestaVariazione.remove(i);
        break; 
      }
    }
  }     
  request.setAttribute("vTipoRichiestaVariazione", vTipoRichiestaVariazione);  
  
  
  
  String note = request.getParameter("note");
  String idTipoRichiesta = request.getParameter("idTipoRichiesta");
  String chkSoloAggiunta = request.getParameter("chkSoloAggiunta");
  if(request.getParameter("avantiInsAzienda") != null) 
  {
    Long idTipoRichiestaLg = null;
    ValidationErrors errors = new ValidationErrors();
    if(Validator.isNotEmpty(note))
    {
      if(note.length() > 1000) 
      {
        errors.add("note", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
      }
    }
    if (Validator.isEmpty(idTipoRichiesta))
    {
      errors.add("idTipoRichiesta", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
    }
    else
    {
      idTipoRichiestaLg = new Long(idTipoRichiesta);
    }
    
    
    
    AziendaNuovaVO aziendaNuovaVO = null;  
    
	  if (errors.size() != 0) 
	  {  
	    request.setAttribute("errors", errors);
	    %>
	      <jsp:forward page = "<%=newInserimentoRichiestaVariazioneUrl%>" />
	    <%
	    return;
	  }
	  else
	  {
	    aziendaNuovaVO = gaaFacadeClient.getRichAzByIdAzienda(new Long(idAzienda).longValue(), 
        idTipoRichiestaLg.longValue());
      request.setAttribute("aziendaNuovaVO", aziendaNuovaVO);
      
      if(Validator.isNotEmpty(aziendaNuovaVO))
      {
        if((aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_STAMPATA) > 0)
          && (aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_VALIDATA) < 0))
        {
          {
            //vado direttamente nella pagina di conferma...
            %>
              <jsp:forward page = "<%=newInserimentoConfermaVariazioneUrl%>" />
            <%
            return;
          }
        }
      }  
	  
	  }	  
	  
    
    
  
	  
	  if(Validator.isEmpty(msgErrore))
	  {
	    //Salvataggio dati....
	    RichiestaAziendaVO richiestaAziendaVO = new RichiestaAziendaVO();
      richiestaAziendaVO.setNote(note);
      richiestaAziendaVO.setIdTipoRichiesta(idTipoRichiestaLg);
      richiestaAziendaVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
      if(Validator.isNotEmpty(chkSoloAggiunta))
        richiestaAziendaVO.setFlagSoloAggiunta("S");
      else
        richiestaAziendaVO.setFlagSoloAggiunta("N");
	    //update
	    if(Validator.isNotEmpty(aziendaNuovaVO)
	      && (aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_BOZZA) == 0))	        
	    {
	      richiestaAziendaVO.setIdRichiestaAzienda(aziendaNuovaVO.getIdRichiestaAzienda());
	      gaaFacadeClient.updateRichiestaAzienda(richiestaAziendaVO);
	    }
	    else if(Validator.isNotEmpty(aziendaNuovaVO)
        && (aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_STAMPATA) == 0))         
      {
        richiestaAziendaVO.setIdRichiestaAzienda(aziendaNuovaVO.getIdRichiestaAzienda());
        gaaFacadeClient.updateRichiestaAzienda(richiestaAziendaVO);
        IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
        iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_BOZZA);
        gaaFacadeClient.aggiornaStatoRichiestaValCess(aziendaNuovaVO, ruoloUtenza.getIdUtente().longValue(), 
          iterRichiestaAziendaVO);
      }
      //Prima volta
      else
      {
        richiestaAziendaVO.setIdAzienda(new Long(idAzienda));
		    gaaFacadeClient.insertAziendaNuovaRichiestaVariazione(richiestaAziendaVO);	
		  }
		  
		  if(idTipoRichiestaLg.intValue() == SolmrConstants.RICHIESTA_VAR_IRRORATRICI)
		  {
		    %>
	        <jsp:forward page = "<%=newInserimentoRichVarIrrUrl %>" />
	      <%
	      return; 
		  }
		  else if(idTipoRichiestaLg.intValue() == SolmrConstants.RICHIESTA_VAR_SOCI)
		  {
		    Vector<String> vActor = (Vector<String>)session.getAttribute("vActor");
        if(vActor.contains(SolmrConstants.GESTORE_CAA))
        {
          %>
            <jsp:forward page="<%= newInserimentoRichSoggAssCaaUrl %>"/>
          <%
          return;
        }
        else
        {
          %>
            <jsp:forward page="<%= newInserimentoRichSoggAssUrl %>"/>
          <%
          return;
        }
		  }
		  else if(idTipoRichiestaLg.intValue() == SolmrConstants.RICHIESTA_VAR_SOGGETTI)
      {
      
      }
		  	    
	  }  
	  else
	  {
	    request.setAttribute("msgErrore", msgErrore);
	    %>
        <jsp:forward page = "<%=newInserimentoRichiestaVariazioneUrl%>" />
      <%
      return;
	  }
	  
	  

    
  }
%>
  <jsp:forward page = "<%=newInserimentoRichiestaVariazioneUrl%>" />

