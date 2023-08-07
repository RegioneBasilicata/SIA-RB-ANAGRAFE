<%@ page language="java"
    contentType="text/html"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.CodeDescription" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.util.ErrorUtils" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.AziendaAtecoSecVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.GruppoGreeningVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.PlSqlCalcoloOteVO" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.smranag.smrgaa.dto.PlSqlCodeDescription"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%
  String rlsStr = request.getParameter("rlsStr");
  String uluStr = request.getParameter("uluStr");
%>

<jsp:useBean id="anagErrorVO" scope="request" class="it.csi.solmr.dto.anag.AnagAziendaVO">
<jsp:setProperty name="anagErrorVO" property="*" />
<%
  if(Validator.isNotEmpty(rlsStr))
    anagErrorVO.setRls(new BigDecimal(Formatter.parseDouble2SeparatorForBigDecimal(rlsStr)));
  else anagErrorVO.setRls(null);
  if(Validator.isNotEmpty(uluStr))
    anagErrorVO.setUlu(new BigDecimal(Formatter.parseDouble2SeparatorForBigDecimal(uluStr)));
  else anagErrorVO.setUlu(null);
%>
</jsp:useBean>

<%! @SuppressWarnings("unchecked") %>

<%
	String iridePageName = "anagraficaIndicatoriModCtrl.jsp";
 	%>
 		<%@include file = "/include/autorizzazione.inc" %>
 	<%

 	AnagFacadeClient client = new AnagFacadeClient();
 	String url = "/view/anagraficaIndicatoriModView.jsp";
 	String operazione = request.getParameter("operazione");
 	String richiestaModifica = "ok";
 	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
 	String idTipoIntermediarioDelegato = null;
 	boolean salva = false;
 	AnagAziendaVO voAnagModifica = null;
 	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 	ValidationErrors errors = new ValidationErrors();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  String actionUrl = "../layout/anagrafica.htm";
  String dettaglioUrl = "../layout/anagraficaIndicatori.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione modifica azienda. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  
  //Vettore di AziendaAtecoSecVO
  Vector<AziendaAtecoSecVO> vAziendaAtecoSec = (Vector<AziendaAtecoSecVO>)session.getAttribute("vAziendaAtecoSec");
  
  if (request.getParameter("annulla")!=null) {
 		response.sendRedirect(dettaglioUrl);
    return;
 	}
  
  //Arrivo da una pagina esterna
  if(!Validator.isNotEmpty(request.getParameter("regime")))
  {
    WebUtils.removeUselessFilter(session,null);
    try
    {
      vAziendaAtecoSec = gaaFacadeClient.getListActiveAziendaAtecoSecByIdAzienda(
        anagAziendaVO.getIdAzienda().longValue());
    }
    catch(SolmrException se) 
    {      
      SolmrLogger.info(this, " - anagraficaIndicatoriModCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+""+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    
    session.setAttribute("vAziendaAtecoSec",vAziendaAtecoSec);
  }
  
  //Se non deve essere effettuato il calcolo del greening, si ricercano i pagamenti ecologici 
  //per il piano di lavorazione attuale. Se invece deve essere effettuato, la ricerca verrà
  //effettuata a calcolo terminato
  if (!"ricalcolaIndicatori".equalsIgnoreCase(request.getParameter("operazione"))) { 
		try {
			Vector<GruppoGreeningVO> gruppiGreening = 
				gaaFacadeClient.getListGruppiGreening(null, anagAziendaVO.getIdAzienda());
	  	request.setAttribute("gruppiGreening", gruppiGreening);
		}catch (SolmrException se) {
			String messaggio = AnagErrors.ERR_KO_SEARCH_ANAG_GREENING;
	 		request.setAttribute("messaggioErrore", messaggio);
		}
  }

 	if(request.getParameter("salva") != null) 
  {
    voAnagModifica = new AnagAziendaVO();
   	try 
    {
      voAnagModifica = client.getAziendaById(anagAziendaVO.getIdAnagAzienda());
     	session.removeAttribute("voAnagModifica");
   	}
   	catch(SolmrException sex) 
    {
      errors = new ValidationErrors();
     	ValidationError error = new ValidationError(sex.getMessage());
     	errors.add("error", error);
     	request.setAttribute("errors", errors);
     	request.getRequestDispatcher(url).forward(request, response);
     	return;
   	}
   	
    String idAttivitaOTE = "";
    if(Validator.isNotEmpty(request.getParameter("idAttivitaOTE"))) 
    {
      idAttivitaOTE = request.getParameter("idAttivitaOTE");
    }
    
    //Setto L'ote corretta
    if(Validator.isNotEmpty(idAttivitaOTE))
    {
      CodeDescription descriptionAttivitaOTE = new CodeDescription(
      Integer.decode(request.getParameter("idAttivitaOTE")),request.getParameter("descrizioneOTE"));
      descriptionAttivitaOTE.setSecondaryCode(request.getParameter("codiceOTE"));
      voAnagModifica.setTipoAttivitaOTE(descriptionAttivitaOTE);
      
    }
    
    String codiceATECO = null;
    if(Validator.isNotEmpty(request.getParameter("codiceATECO"))) 
    {
      codiceATECO = request.getParameter("codiceATECO");
    }
    String descrizioneATECO = "";
    if(Validator.isNotEmpty(request.getParameter("descrizioneATECO"))) 
    {
      descrizioneATECO = request.getParameter("descrizioneATECO");
    }

   	voAnagModifica.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
   	voAnagModifica.setIdIntermediarioDelegato(null);

    //Setto la dimensione azienda col valore impostato nell'interfaccia 
    String idDimensioneAzienda = request.getParameter("idDimensioneAzienda");
    if(Validator.isNotEmpty(idDimensioneAzienda))
    {
      voAnagModifica.setIdDimensioneAzienda(new Long(idDimensioneAzienda));
    }
    
    //Setto le aziende Secondarie ATECO
    voAnagModifica.setVAziendaATECOSec((Vector<AziendaAtecoSecVO>)session.getAttribute("vAziendaAtecoSec"));
      
    
    //Controllo se cambiate le aziende ateco sec
    Vector<AziendaAtecoSecVO> vAziendaAtecoDBSec = null;    
    try
    {
      vAziendaAtecoDBSec = gaaFacadeClient.getListActiveAziendaAtecoSecByIdAzienda(
        anagAziendaVO.getIdAzienda().longValue());
    }
    catch(SolmrException se) 
    {      
      SolmrLogger.info(this, " - modificaAziendaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+""+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    
    vAziendaAtecoSec = (Vector<AziendaAtecoSecVO>)session.getAttribute("vAziendaAtecoSec");
    
    if((vAziendaAtecoSec == null) && (vAziendaAtecoDBSec != null))
    {
      voAnagModifica.setModificaAtecoSec(true);
    }
    else if((vAziendaAtecoSec != null) && (vAziendaAtecoDBSec == null))
    {
      voAnagModifica.setModificaAtecoSec(true);
    }
    else if((vAziendaAtecoSec != null) && (vAziendaAtecoDBSec != null))
    {
      if(vAziendaAtecoSec.size() != vAziendaAtecoDBSec.size())
      {
        voAnagModifica.setModificaAtecoSec(true);
      }
      else
      {
        //controllo se l'elenco attività ateco 
        //secondario sono uguali a quelle presenti su DB
        Vector<Long> vTmp = new Vector<Long>();
        for(int i=0;i<vAziendaAtecoDBSec.size();i++)
        {
          AziendaAtecoSecVO aziendaAtecoSecDBVO = vAziendaAtecoDBSec.get(i);
          vTmp.add(new Long(aziendaAtecoSecDBVO.getIdAttivitaAteco()));
        }
        
        for(int i=0;i<vAziendaAtecoSec.size();i++)
        {
          AziendaAtecoSecVO aziendaAtecoSecVO = vAziendaAtecoSec.get(i);
          //Non è contenuto almeno un elemento quindi sono diversi
          // setto la variabile della modifica ed esco...
          if(!vTmp.contains(new Long(aziendaAtecoSecVO.getIdAttivitaAteco())))
          {
            voAnagModifica.setModificaAtecoSec(true);
            break;
          }
        }
        
      }    
    }
    
    if(Validator.isNotEmpty(codiceATECO))
    {
    
      if(voAnagModifica.getVAziendaATECOSec() != null)
      {
        if(voAnagModifica.getVAziendaATECOSec().size() > 0)
        {
          for(int j=0;j<voAnagModifica.getVAziendaATECOSec().size();j++)
          {
            AziendaAtecoSecVO aziendaAtecoSecVO = (AziendaAtecoSecVO)voAnagModifica.getVAziendaATECOSec().get(j);
            if(aziendaAtecoSecVO.getCodAttivitaAteco().equalsIgnoreCase(codiceATECO))
            {
              errors = ErrorUtils.setValidErrNoNull(errors, "codiceATECO",AnagErrors.ERR_COD_ATECO_SEC_GIA_PRESENTE);
              break;
            } 
          }
        }
      }
    } 
    
    
   	if(Validator.isNotEmpty(codiceATECO)) 
    {
      Long idCodiceATECO = null;
     	try 
      {
        idCodiceATECO = client.ricercaIdAttivitaATECO(codiceATECO.toString(),descrizioneATECO);
     	}
     	catch(SolmrException ex) 
      {
        ValidationError error = new ValidationError(""+AnagErrors.get("ERR_CODICEATECO_NON_TROVATO"));
       	errors.add("codiceATECO", error);
     	}
     	if(idCodiceATECO != null) 
      {
        CodeDescription descriptionAttivitaATECO = new CodeDescription();
        //Integer.decode(idCodiceATECO.toString()),descrizioneATECO);
        descriptionAttivitaATECO.setCode(new Integer(idCodiceATECO.intValue()));
        descriptionAttivitaATECO.setDescription(descrizioneATECO);
       	descriptionAttivitaATECO.setSecondaryCode(codiceATECO.toString());
       	voAnagModifica.setTipoAttivitaATECO(descriptionAttivitaATECO);
     	}
   	}
   	else 
    {
      // Se il codice ATECO non è valorizzato ma la descrizione sì,
     	// costringo l'utente a valorizzare anche il codice OTE
     	
     	//codice ateco null e presenti ateco secondari...
	    if((voAnagModifica.getVAziendaATECOSec() != null) && (voAnagModifica.getVAziendaATECOSec().size() > 0))
	    {
	      errors = ErrorUtils.setValidErrNoNull(errors, "codiceATECO",AnagErrors.ERR_NO_COD_ATECO_PRINC_E_ATECO_SEC);
	    }
	    else
	    {   
     	  if(Validator.isNotEmpty(descrizioneATECO)) 
        {
          if(!Validator.isNotEmpty(codiceATECO)) 
          {
            errors.add("codiceATECO", new ValidationError((String) AnagErrors.get("ERR_CODICE_ATECO_OBBLIGATORIO_WITH_DESCRIZIONE")));
       	  }
     	  }
     	}
     	voAnagModifica.setTipoAttivitaATECO(new CodeDescription());
    }
    
   	if(errors != null && errors.size() != 0) 
    {
      request.setAttribute("errors", errors);
     	request.getRequestDispatcher(url).forward(request, response);
     	return;
   	}
    session.setAttribute("voAnagModifica",voAnagModifica);
    session.setAttribute("isOk", "true");
    
    salva = true;
	} //if salva
  
 	if(request.getParameter("conferma") != null || salva) 
  {
    voAnagModifica = (AnagAziendaVO)session.getAttribute("voAnagModifica");
   	anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
   	errors = new ValidationErrors();
    
   	if(DateUtils.isToday(anagAziendaVO.getDataInizioVal())
      || 
      voAnagModifica.equalsForUpdateNewNoAtecoSec(anagAziendaVO)) 
    {
      // Non è possibile modificare i dati di una azienda storicizzata.
     	if(anagAziendaVO.getDataFineVal() != null || anagAziendaVO.getDataCessazione() != null) 
      {
        ValidationError error = new ValidationError(""+AnagErrors.get("NO_MOD_AZIENDA_CESSATA"));
       	errors.add("error", error);
       	request.setAttribute("errors", errors);
       	request.getRequestDispatcher(url).forward(request, response);
       	return;
     	}
     	// Effettuo la modifica della sede legale
     	try 
      {
        client.updateAzienda(voAnagModifica);
     	}
     	catch(SolmrException se) 
      {
        ValidationError error = new ValidationError(se.getMessage());
       	errors.add("error", error);
       	request.setAttribute("errors", errors);
       	request.getRequestDispatcher(url).forward(request, response);
       	return;
     	}
     	
     	AnagAziendaVO nuovoAnagAziendaVO = new AnagAziendaVO();
     	try 
      {
        nuovoAnagAziendaVO = client.findAziendaAttiva(voAnagModifica.getIdAzienda());
     	}
     	catch(SolmrException se) 
      {
        ValidationError error = new ValidationError(""+AnagErrors.get("ERR_RICERCA_AZIENDA"));
       	errors.add("error", error);
       	request.setAttribute("errors", errors);
       	request.getRequestDispatcher(url).forward(request, response);
       	return;
     	}
     	session.removeAttribute("anagAziendaVO");
     	session.setAttribute("anagAziendaVO",nuovoAnagAziendaVO);
     	
     	response.sendRedirect(dettaglioUrl);
    	return;
   	}
   	else 
    {
      // Sono stati modificati i dati quindi avviso l'utente che sta per effettuare una storicizzazione
     	// dell'azienda
      session.setAttribute("richiestaModifica", richiestaModifica);
     	%>
        <jsp:forward page = "<%= url %>" />
     	<%
     	return;
   	}
	}
 	// Arrivo dalla POP-UP
 	else if (request.getParameter("operazione") != null) 
  {
    // Arrivo dal pop-up e ho scelto di storicizzare
    if(operazione.equalsIgnoreCase("S")) 
    {
      voAnagModifica = (AnagAziendaVO)session.getAttribute("voAnagModifica");
      errors = new ValidationErrors();
      // Recupero il motivo della modifica da salvare solo in caso di storicizzazione
      String motivoModifica = request.getParameter("motivoModifica");
      voAnagModifica.setMotivoModifica(motivoModifica);
      // Storicizzo l'azienda
      try 
      {
        client.storicizzaAzienda(voAnagModifica,ruoloUtenza.getIdUtente());
      }
      catch(SolmrException se) 
      {
        ValidationError error = new ValidationError(se.getMessage());
        errors.add("error", error);
        session.removeAttribute("richiestaModifica");
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
      catch(Exception se) 
      {
        ValidationError error = new ValidationError(""+SolmrErrors.get("GENERIC_SYSTEM_EXCEPTION"));
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
      
      // Recupero i nuovi dati della ditta modificata
      AnagAziendaVO nuovoAnagAziendaVO = new AnagAziendaVO();
      try 
      {
        nuovoAnagAziendaVO = client.findAziendaAttiva(voAnagModifica.getIdAzienda());
      }
      catch(SolmrException se) 
      {
        ValidationError error = new ValidationError(""+AnagErrors.get("ERR_RICERCA_AZIENDA"));
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
      catch(Exception se) 
      {
        ValidationError error = new ValidationError(""+SolmrErrors.get("GENERIC_SYSTEM_EXCEPTION"));
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
      session.removeAttribute("anagAziendaVO");
      session.setAttribute("anagAziendaVO",nuovoAnagAziendaVO);
      
      response.sendRedirect(dettaglioUrl);
    	return;
    }
    else if(operazione.equalsIgnoreCase("inserisciATECOSec")) 
    {
      if(vAziendaAtecoSec == null)
      {
        vAziendaAtecoSec = new Vector<AziendaAtecoSecVO>();
      }
      String idAttivitaATECOSec = request.getParameter("idAttivitaATECOSec");
      String codiceATECOSec = request.getParameter("codiceATECOSec");
      String descrizioneATECOSec = request.getParameter("descrizioneATECOSec");
      
      String codiceATECO = request.getParameter("codiceATECO");
      
      //Ho cliccato su inserisci
      if(Validator.isNotEmpty(request.getParameter("insAtecoSec")))
      {
        if(!Validator.isNotEmpty(codiceATECOSec) && !Validator.isNotEmpty(descrizioneATECOSec)) 
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "codiceATECOSec", AnagErrors.ERR_COD_DESC_ATECO_OBBLIGATORI);
          errors = ErrorUtils.setValidErrNoNull(errors, "descrizioneATECOSec", AnagErrors.ERR_COD_DESC_ATECO_OBBLIGATORI);
        }
        else
        {
          Vector<CodeDescription> tipiAttivitaATECO = null;
          try 
          {
            tipiAttivitaATECO = client.getTipiAttivitaATECO(codiceATECOSec, descrizioneATECOSec);
          }
          catch(SolmrException se) 
          {      
            SolmrLogger.info(this, " - anagraficaIndicatoriModCtrl.jsp - FINE PAGINA");
            String messaggio = errMsg+""+se.toString();
            request.setAttribute("messaggioErrore",messaggio);
            request.setAttribute("pageBack", actionUrl);
            %>
              <jsp:forward page="<%= erroreViewUrl %>" />
            <%
            return;
          }
          
          if(tipiAttivitaATECO == null)
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "inserisciATECOSec", AnagErrors.RICERCAATTIVITAATECO);
          }
          else
          {
            if(tipiAttivitaATECO.size() > 1)
            {
              errors = ErrorUtils.setValidErrNoNull(errors, "inserisciATECOSec", AnagErrors.ERRORE_FILTRI_GENERICI);
            }
            else
            {
              CodeDescription cd = (CodeDescription)tipiAttivitaATECO.get(0);
              idAttivitaATECOSec = cd.getCode().toString();
              codiceATECOSec = cd.getSecondaryCode();
              descrizioneATECOSec = cd.getDescription();
            }
          }
        }
      }  
      
      
      if(Validator.isNotEmpty(codiceATECO))
      {
        if(codiceATECO.equalsIgnoreCase(codiceATECOSec))
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "inserisciATECOSec",AnagErrors.ERR_COD_ATECO_GIA_PRESENTE);
        }
      }
      
      if(vAziendaAtecoSec.size() > 0)
      {
        for(int j=0;j<vAziendaAtecoSec.size();j++)
        {
          AziendaAtecoSecVO aziendaAtecoSecVOtmp = (AziendaAtecoSecVO)vAziendaAtecoSec.get(j);
          if(aziendaAtecoSecVOtmp.getCodAttivitaAteco().equalsIgnoreCase(codiceATECOSec))
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "inserisciATECOSec",AnagErrors.ERR_COD_ATECO_SEC_GIA_PRESENTE);
            break;
          } 
        }
      }
      
      if (errors==null || errors.size()==0) {
	      AziendaAtecoSecVO aziendaAtecoSecVO = new AziendaAtecoSecVO();
	      aziendaAtecoSecVO.setIdAttivitaAteco(new Long(idAttivitaATECOSec).longValue());
	      aziendaAtecoSecVO.setCodAttivitaAteco(codiceATECOSec);
	      aziendaAtecoSecVO.setDescAttivitaAteco(descrizioneATECOSec);
	      
	      vAziendaAtecoSec.add(aziendaAtecoSecVO);
	      
	      session.setAttribute("vAziendaAtecoSec", vAziendaAtecoSec);
	      operazione = "";
      }
    }
    else if(operazione.equalsIgnoreCase("eliminaATECOSec")) 
    {
      String[] chkAttivitaAtecoSec = request.getParameterValues("chkAttivitaAtecoSec");
      if(Validator.isNotEmpty(chkAttivitaAtecoSec))
      {
        for(int j=(chkAttivitaAtecoSec.length - 1);j>=0;j--)
        {
          vAziendaAtecoSec.remove(new Integer(chkAttivitaAtecoSec[j]).intValue());
        }
      }
      else
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "eliminaATECOSec",AnagErrors.ERR_NO_ELEMENTI);
      }
      
      if (errors==null || errors.size()==0) {
	      if(vAziendaAtecoSec.size() == 0)
	      {
	        vAziendaAtecoSec = null;
	      }
	      
	      session.setAttribute("vAziendaAtecoSec",vAziendaAtecoSec);
      }
    }
    /*else if(operazione.equalsIgnoreCase("calcolaOTE"))
    {
      PlSqlCalcoloOteVO plCode = null;
      PlSqlCalcoloOteVO plCodeUlu = null;
       
      try
      {
        plCode = gaaFacadeClient.calcolaIneaPlSql(anagAziendaVO.getIdAzienda().longValue());
        plCodeUlu = gaaFacadeClient.calcolaUluPlSql(anagAziendaVO.getIdAzienda(), plCode.getIdUde());
      }
      catch (SolmrException se) 
      {
        SolmrLogger.info(this, " - anagraficaIndicatoriModCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+""+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
      
      if((plCode !=null) && (plCode.getDescError() == null)) //Non ci sono errori nel PLSQL
      {        
        request.setAttribute("plCode", plCode);
      }
      else
      {
        if(plCode !=null)
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "calcolaOTE", plCode.getDescError());
        }
        else
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "calcolaOTE", "pl null");
        }
      }
      
      if((plCodeUlu !=null) && (plCodeUlu.getDescError() == null)) //Non ci sono errori nel PLSQL
      {        
        request.setAttribute("plCodeUlu", plCodeUlu);
      }
      else
      {
        if(plCodeUlu !=null)
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "calcolaULU", plCodeUlu.getDescError());
        }
        else
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "calcolaULU", "pl null");
        }
      }   
    
    }*/
    else if (operazione.equalsIgnoreCase("ricalcolaIndicatori")) 
    {
      PlSqlCodeDescription plCode = null;
      
      
      try 
      {
        plCode = gaaFacadeClient.calcolaEfaPlSql(
          anagAziendaVO.getIdAzienda().longValue(), null, ruoloUtenza.getIdUtente());
      }
      catch (SolmrException se) 
      {
        SolmrLogger.info(this, " - anagraficaIndicatoriModCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+""+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
      
      if (plCode==null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "ricalcolaIndicatori", "pl null");
      }
      else if (plCode.getDescription()!=null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "ricalcolaIndicatori", plCode.getDescription());
      }
       
      try 
      {
        plCode = gaaFacadeClient.calcolaGreeningPlSql(
        	anagAziendaVO.getIdAzienda().longValue(), ruoloUtenza.getIdUtente(), null);
      }
      catch (SolmrException se) 
      {
        SolmrLogger.info(this, " - anagraficaIndicatoriModCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+""+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
      
      if (plCode==null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "ricalcolaIndicatori", "pl null");
      }
      else if (plCode.getDescription()!=null) 
      {
      	errors = ErrorUtils.setValidErrNoNull(errors, "ricalcolaIndicatori", plCode.getDescription());
      }
      
      //Si ottengono i dati aggiornati del greening
      try 
      {
				Vector<GruppoGreeningVO> gruppiGreening = 
					gaaFacadeClient.getListGruppiGreening(null, anagAziendaVO.getIdAzienda());
		  	request.setAttribute("gruppiGreening", gruppiGreening);
			}
			catch (SolmrException se) 
			{
				String messaggio = AnagErrors.ERR_KO_SEARCH_ANAG_GREENING;
		 		request.setAttribute("messaggioErrore", messaggio);
			}
    }
    
    if (errors!=null && errors.size()>0) {
			request.setAttribute("errors", errors);
 		}
    
    %>
      <jsp:forward page= "<%= url %>" />
    <%
    return;
    
  } 
 	// E' la prima volta che entro
 	else 
  {
    // recupero dalla sessione i dati dell'anagrafica
   	session.removeAttribute("voAnagModifica");
   	session.removeAttribute("isOk");
   	anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
   	errors = new ValidationErrors();
   	
   	try 
    {
     	anagAziendaVO = client.findAziendaAttiva(anagAziendaVO.getIdAzienda());
   	}
   	catch(SolmrException se) 
    {
      ValidationError error = new ValidationError(""+AnagErrors.get("ERR_RICERCA_AZIENDA"));
     	errors.add("error", error);
     	session.setAttribute(SolmrConstants.SESSION_ERRORI_PAGINA, errors);
     	
     	response.sendRedirect(dettaglioUrl);
     	return;
   	}
   	session.setAttribute("load","true");
   	
   	if(anagAziendaVO.getDelegaVO() != null) 
    {
      idTipoIntermediarioDelegato = anagAziendaVO.getDelegaVO().getIdIntermediario().toString();
     	anagAziendaVO.setIdIntermediarioDelegato(idTipoIntermediarioDelegato);
   	}
   	session.setAttribute("voAnagModifica", anagAziendaVO);
    
    session.setAttribute("isOk", "true");
   	%>
      <jsp:forward page = "/view/anagraficaIndicatoriModView.jsp" />
   	<%
 	}

%>
