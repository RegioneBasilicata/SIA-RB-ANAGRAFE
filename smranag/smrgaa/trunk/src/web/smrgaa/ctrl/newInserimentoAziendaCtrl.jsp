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
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO" %>

<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String iridePageName = "newInserimentoAziendaCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  WebUtils.removeUselessFilter(session, "idAziendaNuova"); 
  
  String newInserimentoAziendaUrl = "/view/newInserimentoAziendaView.jsp";
  String newInserimentoAnagraficaImpreseEnti = "/ctrl/newInserimentoAnagraficaImpreseEntiCtrl.jsp";
  String newInserimentoAnagraficaPrivati = "/ctrl/newInserimentoAnagraficaPrivatiCtrl.jsp";
  String newInserimentoConfermaUrl = "/ctrl/newInserimentoConfermaCtrl.jsp"; 
  
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  
  
  String testoHelp = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ET_UT_NAP_ANA1);
  request.setAttribute("testoHelp", testoHelp);
  
  
  //arrivo dalla selezione del tasto nuova iscrizione dopo diversi passaggi!!!
  Long idAziendaNuova = (Long)session.getAttribute("idAziendaNuova");
  AziendaNuovaVO aziendaNuovaVO = null;
  if(Validator.isNotEmpty(idAziendaNuova)
    && (request.getParameter("avantiInsAzienda") == null))
  {
    aziendaNuovaVO = gaaFacadeClient.getAziendaNuovaIscrizioneByPrimaryKey(
        idAziendaNuova);
    if(Validator.isNotEmpty(aziendaNuovaVO.getCodEnte()))
    {
      request.setAttribute("cuaaAziendaNuova", aziendaNuovaVO.getCodEnte());
    }
    else
    {
      request.setAttribute("cuaaAziendaNuova", aziendaNuovaVO.getCuaa());
    }
  }
  
  //idAziendaNuova = null;
  session.removeAttribute("idAziendaNuova");
  
  String cuaa = request.getParameter("cuaaInserimento");
  String partitaIva = request.getParameter("partitaIvaInserimento");
  String cuaaCaa = request.getParameter("cuaaCaaInserimento");
  String partitaIvaCaa = request.getParameter("partitaIvaCaaInserimento");
  int countSel = 0;  
  if(request.getParameter("avantiInsAzienda") != null) 
  {
    ValidationErrors errors = new ValidationErrors();
    if(Validator.isNotEmpty(cuaa)) 
    {
      countSel++;
    }
    if(Validator.isNotEmpty(partitaIva))
    {
      countSel++;
    }
    if(Validator.isNotEmpty(cuaaCaa))
    {
      countSel++;
    }
    if(Validator.isNotEmpty(partitaIvaCaa))
    {
      countSel++;
    }
    
    
    if(countSel == 0)
    {
      errors.add("partitaIvaInserimento", new ValidationError(
          "Inserire un campo"));
      errors.add("cuaaInserimento", new ValidationError(
          "Inserire un campo"));
      errors.add("cuaaCaaInserimento", new ValidationError(
          "Inserire un campo"));
      errors.add("partitaIvaCaaInserimento", new ValidationError(
          "Inserire un campo"));
    }
    else if(countSel == 1)
    {
      if(Validator.isNotEmpty(cuaaCaa) || Validator.isNotEmpty(partitaIvaCaa))
      {
        errors.add("cuaaCaaInserimento", new ValidationError(
          "Entrambi i campi sono obbligatori"));
        errors.add("partitaIvaCaaInserimento", new ValidationError(
          "Entrambi i campi sono obbligatori"));      
      }
    }
    else if(countSel > 1)
    {
      if(Validator.isEmpty(cuaaCaa))
      {
        errors.add("cuaaInserimento", new ValidationError(
          "Inserire un solo campo!"));
      } 
    }
    
    
    if (Validator.isNotEmpty(cuaa))
    {
      cuaa = cuaa.trim();
      if (cuaa.length() != 11 && cuaa.length() != 16)
      {
        errors.add("cuaaInserimento", new ValidationError((String) AnagErrors
            .get("ERR_CUAA_ERRORE")));
      }
      else if (cuaa.length() == 11 && !Validator.controlloPIVA(cuaa))
      {
        errors.add("cuaaInserimento", new ValidationError((String) AnagErrors
            .get("ERR_CUAA_ERRORE")));
      }
      else if (cuaa.length() == 16 && !Validator.controlloCf(cuaa))
      {
        errors.add("cuaaInserimento", new ValidationError((String) AnagErrors
            .get("ERR_CUAA_ERRORE")));
      }
    }
    if (Validator.isNotEmpty(partitaIva)
        && !Validator.controlloPIVA(partitaIva))
    {
      errors.add("partitaIvaInserimento", new ValidationError("Partita IVA errata"));
    }
    if (Validator.isNotEmpty(cuaaCaa))
    {
      if (cuaaCaa.length() != 9)
      {
        errors.add("cuaaCaaInserimento", new ValidationError("Il codice ente inserito non e'' valido. "+
          "Deve essere un codice numerico di nove cifre"));
      }
      else
      {
        RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
        String caaApp = ruoloUtenza.getCodiceEnte().substring(0,3);
        if(!caaApp.equalsIgnoreCase(cuaaCaa.substring(0,3)))
        {
          errors.add("cuaaCaaInserimento", new ValidationError("Il codice ente inserito non e'' valido: "+
            "non corrisponde con l''ente dell''utente"));
        }
      }
    }
    if (Validator.isNotEmpty(partitaIvaCaa)
        && !Validator.controlloPIVA(partitaIvaCaa))
    {
      errors.add("partitaIvaCaaInserimento", new ValidationError("Partita IVA errata"));
    }
      
    
	  if (! (errors == null || errors.size() == 0)) 
	  {
	    request.setAttribute("errors", errors);
	    %>
	      <jsp:forward page = "<%=newInserimentoAziendaUrl%>" />
	    <%
	    return;
	  }	  
	  
	  String cuaaValido = "";
	  if (Validator.isNotEmpty(cuaa))
    {
      cuaaValido = cuaa;
    }
    else if (Validator.isNotEmpty(partitaIva))
    {
      cuaaValido = partitaIva;
    }
    else
    {
      cuaaValido = cuaaCaa;
    }
    
    
    if(cuaaValido.length() == 9)
    {
      aziendaNuovaVO = gaaFacadeClient.getAziendaNuovaIscrizioneEnte(
        cuaaValido, new long[]{SolmrConstants.RICHIESTA_NUOVA_ISCRIZIONE,SolmrConstants.RICHIESTA_NI_AZIENDA_OBSOLETA});
    }
    else
    {
      aziendaNuovaVO = gaaFacadeClient.getAziendaNuovaIscrizione(
        cuaaValido, new long[]{SolmrConstants.RICHIESTA_NUOVA_ISCRIZIONE,SolmrConstants.RICHIESTA_NI_AZIENDA_OBSOLETA});
    }
    
    if(Validator.isNotEmpty(aziendaNuovaVO))
      idAziendaNuova = aziendaNuovaVO.getIdAziendaNuova();
    else
      idAziendaNuova = null;
      
    session.setAttribute("idAziendaNuova", idAziendaNuova);
    
    
    
    String msgErrore = "";
    long idTipoRichiesta = SolmrConstants.RICHIESTA_NUOVA_ISCRIZIONE;
    if(cuaaValido.length() == 9)
    {
      IntermediarioAnagVO intermediarioVO = anagFacadeClient.findIntermediarioVOByCodiceEnte(cuaaValido);
      if(Validator.isNotEmpty(intermediarioVO)
        && Validator.isNotEmpty(intermediarioVO.getExtIdAzienda()))
      {
        msgErrore = "Attenzione! Impossibile procedere in quanto l'azienda risulta già registrata all'interno dell'Anagrafe Agricola Unica del Piemonte.";      
      }
    }
    else
    {
		  AnagAziendaVO anagAziendaVO = new AnagAziendaVO();
		  anagAziendaVO.setCUAA(cuaaValido);
		  
		  Vector<Long> vectIdAnagAzienda = anagFacadeClient.getListIdAziende(anagAziendaVO, null, false);
		 
		  if(Validator.isNotEmpty(vectIdAnagAzienda) && (vectIdAnagAzienda.size() > 0))
		  {
		    if(vectIdAnagAzienda.size() == 1)
		    {
		      Date parametroOldestValAllowed = (Date)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_OLDEST_VAL_ALLOWED);
		      AnagAziendaVO anagAziendaVOTmp = anagFacadeClient.getAziendaById(vectIdAnagAzienda.get(0));
		      ConsistenzaVO consistenzaVO = anagFacadeClient.getUltimaDichiarazioneConsistenza(anagAziendaVOTmp.getIdAzienda());
		      if(Validator.isEmpty(consistenzaVO) && parametroOldestValAllowed.after(anagAziendaVOTmp.getDataInizioVal())
		        && Validator.isEmpty(anagAziendaVOTmp.getIdAziendaProvenienza()) 
		        && !gaaFacadeClient.isInAziendaProvenienza(anagAziendaVOTmp.getIdAzienda().longValue()))
		      {
		        idTipoRichiesta = SolmrConstants.RICHIESTA_NI_AZIENDA_OBSOLETA;
		      }
		      else
		      {
		        
		        //in questo caso possibile duplicazione del cuaa (es. Consorzi)
		        String flagUnivocita = "S";
		        if((anagAziendaVOTmp.getTipoTipologiaAzienda() != null)
		          && (anagAziendaVOTmp.getTipoTipologiaAzienda().getCode() != null))
		        {
		          TipoTipologiaAziendaVO tipoTipologiaAziendaVO = anagFacadeClient.getTipologiaAzienda(
		            new Long(anagAziendaVOTmp.getTipoTipologiaAzienda().getCode()));
		          flagUnivocita = tipoTipologiaAziendaVO.getFlagControlliUnivocita();
		        }
		        
            if("S".equalsIgnoreCase(flagUnivocita))
            {		        
			        String testoErroreDelega = "";
			        if(anagAziendaVOTmp.isPossiedeDelegaAttiva())
			        {
			          testoErroreDelega = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_MSG_NAP_PAG1_CAA);
			        }
			        else
			        {
			          testoErroreDelega = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_MSG_NAP_PAG1_NOCAA);
			        }
		         
			        msgErrore = testoErroreDelega;
			      }
		      }
		    }
		    else
		    {
		      msgErrore = "Attenzione! Impossibile procedere in quanto l'azienda risulta già registrata all'interno dell'Anagrafe Agricola Unica del Piemonte.";
		    }
		  }
		}
	  
	  if(Validator.isEmpty(msgErrore))
	  {
	    if(Validator.isNotEmpty(aziendaNuovaVO) 
        && (aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_ANNULLAMENTO) == 0))
      {
        //E' annullata resetto tutto 
        WebUtils.removeUselessFilter(session, null); 
      }     	  
	    else if(Validator.isNotEmpty(aziendaNuovaVO) 
	      && (aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_FIRMA_DIGITALE) >=0))
	    {
	      //msgErrore = "Attenzione: l'azienda/persona fisica è già stata trasmessa a PA.";
	      %>
	        <jsp:forward page = "<%=newInserimentoConfermaUrl%>" />
	      <%
	      return;
	    }
	    
	  }
	  
	  
	  if(Validator.isNotEmpty(msgErrore))
	  {
	    request.setAttribute("msgErrore", msgErrore);
	    %>
        <jsp:forward page = "<%=newInserimentoAziendaUrl%>" />
      <%
      return;
	  }
	  
	  
	  //session.setAttribute("idAziendaNuova", idAziendaNuova);
	  
	  
	  if(Validator.isNotEmpty(cuaa))
	  {
	    %>
        <jsp:forward page = "<%=newInserimentoAnagraficaPrivati%>" >
          <jsp:param name="idTipoRichiesta" value="<%= idTipoRichiesta %>" />
        </jsp:forward>
      <%
      return;
	  }
	  else
	  {
	    %>
        <jsp:forward page = "<%=newInserimentoAnagraficaImpreseEnti%>" >
          <jsp:param name="idTipoRichiesta" value="<%= idTipoRichiesta %>" />
        </jsp:forward>
      <%
      return;
	  }

    
  }
%>
  <jsp:forward page = "<%=newInserimentoAziendaUrl%>" />

