<%@ page language="java"
    contentType="text/html"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.util.ErrorUtils" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%
	String iridePageName = "anagraficaAltreInformazioniModCtrl.jsp";
 	%>
 		<%@include file = "/include/autorizzazione.inc" %>
 	<%

 	AnagFacadeClient client = new AnagFacadeClient();
 	String url = "/view/anagraficaAltreInformazioniModView.jsp";
 	String operazione = request.getParameter("operazione");
 	String richiestaModifica = "ok";
 	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
 	String idTipoIntermediarioDelegato = null;
 	boolean salva = false;
 	AnagAziendaVO voAnagModifica = null;
 	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 	ValidationErrors errors = new ValidationErrors();
  String actionUrl = "../layout/anagrafica.htm";
  String dettaglioUrl = "../layout/anagraficaAltreInformazioni.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione modifica azienda. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  
  if (request.getParameter("annulla")!=null) {
    response.sendRedirect(dettaglioUrl);
    return;
 	}
  
  String obbligoGF = null;
  try 
  {
    if((anagAziendaVO.getTipoFormaGiuridica() != null)
      && (anagAziendaVO.getTipoFormaGiuridica().getCode() != null))
    {
      obbligoGF = client.getObbligoGfFromFormaGiuridica(new Long(anagAziendaVO.getTipoFormaGiuridica().getCode().intValue()));
      request.setAttribute("obbligoGF", obbligoGF);
    }
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - anagraficaAltreInformazioniModCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+" "+AnagErrors.ERRORE_KO_OBBLIGO_CF+": "+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }

  
  //Arrivo da una pagina esterna
  if(!Validator.isNotEmpty(request.getParameter("regime")))
  {
    WebUtils.removeUselessFilter(session,null);
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
   	
   	
   	String flagCCIAA = null;
    if (Validator.isNotEmpty(voAnagModifica.getTipoFormaGiuridica()) 
      && Validator.isNotEmpty(voAnagModifica.getTipoFormaGiuridica().getCode()))
    {
      try 
      {
        flagCCIAA = client.getFormaGiuridicaFlagCCIAA(new Long(voAnagModifica.getTipoFormaGiuridica().getCode().intValue()));
      }
      catch(SolmrException se) 
      {
        ValidationError error = new ValidationError(""+AnagErrors.get("ERR_RICERCA_FORMA_GIURIDICA"));
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
    }
    voAnagModifica.setFlagCCIAA(flagCCIAA);
   	
   	if(request.getParameter("CCIAAprovREA")!=null) 
    {
      voAnagModifica.setCCIAAprovREA(request.getParameter("CCIAAprovREA").toUpperCase());
    }
    else 
    {
      voAnagModifica.setCCIAAprovREA("");
    }
    voAnagModifica.setStrCCIAAnumeroREA(request.getParameter("strCCIAAnumeroREA"));
    if(request.getParameter("CCIAAnumRegImprese") != null) 
    {
      voAnagModifica.setCCIAAnumRegImprese(request.getParameter("CCIAAnumRegImprese"));
    }
    if(request.getParameter("CCIAAannoIscrizione") != null) 
    {
      voAnagModifica.setCCIAAannoIscrizione(request.getParameter("CCIAAannoIscrizione"));
    }
    
    errors = voAnagModifica.validateUpdateAnagIndicatori();
    
    
    // Controllo la validità della provincia REA
    if(Validator.isNotEmpty(voAnagModifica.getCCIAAprovREA())) 
    {
      try 
      {
        boolean isValida = client.isProvinciaReaValida(voAnagModifica.getCCIAAprovREA());
        if(!isValida) 
        {
          ValidationError error = new ValidationError(""+AnagErrors.get("ERR_PROV_REA"));
          errors.add("CCIAAprovREA",error);
        }
      }
      catch(SolmrException se) 
      {
        ValidationError error = new ValidationError(se.getMessage());
        errors.add("error",error);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
    }
    
    if(Validator.isNotEmpty(request.getParameter("strCCIAAnumeroREA")))
    {
      try 
      {
        voAnagModifica.setCCIAAnumeroREA(Long.decode(request.getParameter("strCCIAAnumeroREA")));
      }
      catch(Exception ex) 
      {
        ValidationError error = new ValidationError(""+AnagErrors.get("ERR_NUMERO_REA_ERRATO"));
        errors.add("strCCIAAnumeroREA", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
    }
    else 
    {
      voAnagModifica.setCCIAAnumeroREA(null);
    }
    
    String flagIap = "N";
    if(Validator.isNotEmpty(request.getParameter("chkFlagIap")))
      flagIap = request.getParameter("chkFlagIap");
      
    voAnagModifica.setFlagIap(flagIap);
    
    //Setto l'esoneroPAgamentoGF col valore impostato nell'interfaccia 
    String  esoneroPagamentoGf = request.getParameter("esoneroPagamentoGf");
    if(Validator.isNotEmpty(esoneroPagamentoGf))
    {
      voAnagModifica.setEsoneroPagamentoGF(esoneroPagamentoGf);
    }
   	
    //Si imposta il flag di modifica delle attivita economiche secondarie a false, dato che in questa
    //pagina non sono presenti (sono invece presenti nella pagina degli Indicatori aziendali)
   	voAnagModifica.setModificaAtecoSec(false);
   	
   	voAnagModifica.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
   	voAnagModifica.setIdIntermediarioDelegato(null);
    
   	if(errors != null && errors.size() != 0) 
    {
      request.setAttribute("errors", errors);
     	request.getRequestDispatcher(url).forward(request, response);
     	return;
   	}
    session.setAttribute("voAnagModifica", voAnagModifica);
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
     	// Effettuo la modifica dell'azienda
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
 	else if (operazione != null) 
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
      <jsp:forward page = "/view/anagraficaAltreInformazioniModView.jsp" />
   	<%
 	}

%>
