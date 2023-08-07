<%@ page language="java"
    contentType="text/html"
%>

<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*"%>
<%@ page import="it.csi.smranag.smrgaa.util.ErrorUtils" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

	String iridePageName = "sediNewCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%

  String viewUrl = "/view/sediNewView.jsp";
  String precUrlOk = "/layout/sedi.htm";
  String actionUrl = "../layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione inserimento ute. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

  AnagFacadeClient afClient = new AnagFacadeClient();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  ValidationErrors errors = new ValidationErrors();  
  UteVO uteVO = new UteVO();
  
  String regimeSediNew = request.getParameter("regimeSediNew");
  
  //rimuovo da sessione prima volta che entro
  if(Validator.isEmpty(regimeSediNew))
  {
    WebUtils.removeUselessFilter(session, "");
  }
  
  Vector<UteAtecoSecondariVO> vAtecoSecUte = (Vector<UteAtecoSecondariVO>)session.getAttribute("vAtecoSecUte");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  
	
	// Recupero l'elenco delle zone altimetriche
	it.csi.solmr.dto.CodeDescription[] elencoZonaAltimetrica = null;
	try 
	{
		elencoZonaAltimetrica = afClient.getListTipoZonaAltimetrica(SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION);
		request.setAttribute("elencoZonaAltimetrica", elencoZonaAltimetrica);
	}
	catch(SolmrException se) 
	{
		ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_ZONA_ALTIMETRICA);
    errors.add("codeOte", error);
    request.setAttribute("errors", errors);
    return;
	}
  
  
  if(request.getParameter("salva") != null) 
  {
    uteVO.setIndirizzo(request.getParameter("indirizzo"));
    uteVO.setProvincia(request.getParameter("provincia"));
    uteVO.setComune(request.getParameter("comune"));
    uteVO.setCap(request.getParameter("cap"));
    uteVO.setDenominazione(request.getParameter("denominazione"));
    uteVO.setTelefono(request.getParameter("telefono"));
    uteVO.setFax(request.getParameter("fax"));
    uteVO.setCodeOte(request.getParameter("codeOte"));
    uteVO.setDescOte(request.getParameter("descOte"));
    uteVO.setCodeAteco(request.getParameter("codeAteco"));
    uteVO.setDescAteco(request.getParameter("descAteco"));
    
    if(Validator.isNotEmpty(request.getParameter("idZonaAltimetrica")))
      uteVO.setIdZonaAltimetrica(new Long(request.getParameter("idZonaAltimetrica")));
    
    uteVO.setDataInizioAttivitaStr(request.getParameter("dataInizioAttivitaStr"));
    uteVO.setNote(request.getParameter("note"));
    uteVO.setMotivoModifica(request.getParameter("motivoModifica"));
  	
  	uteVO.setIdAzienda(anagAziendaVO.getIdAzienda());
  	
  	
   	errors = uteVO.validateInsUTE();
   	// validazione
   	String codiceOTE = "";
   	if(request.getParameter("codeOte") != null && !request.getParameter("codeOte").equals("")) 
   	{
    	codiceOTE = request.getParameter("codeOte");
    }
    String descrizioneOTE = "";
    if(request.getParameter("descOte") != null && !request.getParameter("descOte").equals("")) 
    {
    	if(request.getParameter("descOte").startsWith("%")) 
    	{
      	descrizioneOTE = request.getParameter("descOte").substring(1);
      } 
      else 
      {
      	descrizioneOTE = request.getParameter("descOte");
      }
    }
    String codiceATECO = null;
    if(request.getParameter("codeAteco") != null 
      && !request.getParameter("codeAteco").equals("")) 
    {
    	codiceATECO = request.getParameter("codeAteco");
    }
    String descrizioneATECO = "";
    if(request.getParameter("descAteco") != null 
      && !request.getParameter("descAteco").equals("")) 
    {
    	if(request.getParameter("descAteco").startsWith("%"))
      	descrizioneATECO = request.getParameter("descAteco").substring(1);
      else 
      {
      	descrizioneATECO = request.getParameter("descAteco");
      }
    }

    Long idZonaAltimetrica = null;
    if(Validator.isNotEmpty(request.getParameter("idZonaAltimetrica"))) 
    {
    	idZonaAltimetrica = Long.decode(request.getParameter("idZonaAltimetrica"));
    }

    if(codiceOTE != null && !codiceOTE.equals("")) 
    {
    	Long idCodiceOTE = null;
    	try 
    	{
      	idCodiceOTE = afClient.ricercaIdAttivitaOTE(codiceOTE.toString(),descrizioneOTE, false);
      	CodeDescription descriptionAttivitaOTE = new CodeDescription(Integer.decode(idCodiceOTE.toString()),descrizioneOTE);
      	descriptionAttivitaOTE.setSecondaryCode(codiceOTE.toString());
      	uteVO.setTipoAttivitaOTE(descriptionAttivitaOTE);
      } 
      catch (SolmrException ex) 
      {
      	ValidationError error = new ValidationError(ex.getMessage());
      	errors.add("codeOte", error);
      	request.setAttribute("errors", errors);
      }
    }
    // Se il codice OTE non è valorizzato ma la descrizione sì,
    // costringo l'utente a valorizzare anche il codice OTE
    else 
    {
    	if(Validator.isNotEmpty(descrizioneOTE)) 
    	{
      	if(!Validator.isNotEmpty(codiceOTE)) 
      	{
        	errors.add("codeOte", new ValidationError((String)AnagErrors.get("ERR_CODICE_OTE_OBBLIGATORIO_WITH_DESCRIZIONE")));
        }
      }
    }

    if(Validator.isNotEmpty(codiceATECO))
    {
    
      if(vAtecoSecUte != null)
	    {
	      if(vAtecoSecUte.size() > 0)
	      {
	        for(int j=0;j<vAtecoSecUte.size();j++)
	        {
	          UteAtecoSecondariVO uteAtecoSecondariVO = vAtecoSecUte.get(j);
	          if(uteAtecoSecondariVO.getCodiceAteco().equalsIgnoreCase(codiceATECO))
	          {
	            errors = ErrorUtils.setValidErrNoNull(errors, "codeAteco",AnagErrors.ERR_COD_ATECO_SEC_GIA_PRESENTE);
	            break;
	          } 
	        }
	      }
	    }
    
    	Long idCodiceATECO = null;
    	try 
    	{
      	idCodiceATECO = afClient.ricercaIdAttivitaATECO(codiceATECO.toString(),descrizioneATECO, false);
      	CodeDescription descriptionAttivitaATECO = new CodeDescription(Integer.decode(idCodiceATECO.toString()),descrizioneATECO);
      	descriptionAttivitaATECO.setSecondaryCode(codiceATECO.toString());
      	uteVO.setTipoAttivitaATECO(descriptionAttivitaATECO);
      } 
      catch (SolmrException ex) 
      {
      	ValidationError error = new ValidationError(ex.getMessage());
      	errors.add("codeAteco", error);
      	request.setAttribute("errors", errors);
      }
    }
    else 
    {
      //codice ateco null e presenti ateco secondari...
      if((vAtecoSecUte != null) && (vAtecoSecUte.size() > 0))
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "codeAteco",AnagErrors.ERR_NO_COD_ATECO_PRINC_E_ATECO_SEC);
      }     
      else
      {
        // Se il codice ATECO non è valorizzato ma la descrizione sì,
        // costringo l'utente a valorizzare anche il codice OTE    
	    	if(Validator.isNotEmpty(descrizioneATECO)) 
	    	{
	      	if(!Validator.isNotEmpty(codiceATECO)) 
	      	{
	        	errors.add("codeAteco", new ValidationError((String)AnagErrors.get("ERR_CODICE_ATECO_OBBLIGATORIO_WITH_DESCRIZIONE")));
	        }
	      }
	    }
    }
    if(idZonaAltimetrica != null && !idZonaAltimetrica.equals(""))
    {
    	CodeDescription descriptionZonaAlt = new CodeDescription(new Integer(idZonaAltimetrica.intValue()), "");
    	uteVO.setTipoZonaAltimetrica(descriptionZonaAlt);
    }

    try 
    {
    	if(request.getParameter("comune") != null && !request.getParameter("comune").equals(""))
    	{
      	String comune = request.getParameter("comune");
      	String provincia = request.getParameter("provincia");
      	String istatComune = afClient.ricercaCodiceComuneNonEstinto(comune, provincia);
      	uteVO.setIstat(istatComune);
      }
    }
    catch(Exception ex) 
    {
    	ValidationError error = new ValidationError(ex.getMessage());
    	errors.add("comune", error);
    	request.setAttribute("errors", errors);
    }

    if(!(errors == null || errors.size() == 0)) 
    {
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(viewUrl).forward(request, response);
    	return;
    }

    // Controllo se esista già un'unità produttiva attiva nel comune selezionato
    // e in caso affermativo chiedo all'utente se intende proseguire con l'inserimento
    try 
    {
    	afClient.countUteByAziendaAndComune(uteVO.getIdAzienda(),uteVO.getIstat());
    }
    catch(SolmrException se) 
    {
    	if(se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ESISTE_UTE_CON_COMUNE"))) 
    	{
      	request.setAttribute("confermaOperazione", "ok");
      	%>
      		<jsp:forward page="<%= viewUrl %>"/>
      	<%
      }
      else 
      {
      	ValidationError error = new ValidationError(se.getMessage());
      	errors.add("error", error);
      	request.setAttribute("errors", error);
      	request.getRequestDispatcher(viewUrl).forward(request, response);
      	return;
      }
    }

    try 
    {
      uteVO.setvUteAtecoSec(vAtecoSecUte);
    	afClient.insertUte(uteVO, ruoloUtenza.getIdUtente());
    	ValidationErrors errorsVal = new ValidationErrors();
    	ValidationError error = new ValidationError("Inserimento eseguito");
    	errors.add("error",error);
    	request.setAttribute("errorsVal",errorsVal);
    	request.getRequestDispatcher(precUrlOk).forward(request, response);
    }
    catch(SolmrException ex) 
    {
    	ValidationError error = new ValidationError(ex.getMessage());
    	errors.add("error", error);
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(viewUrl).forward(request, response);
    }
	}
	//fine salva
	else if (Validator.isNotEmpty(request.getParameter("operazione"))
    && "inserisciATECOSec".equalsIgnoreCase(request.getParameter("operazione"))) 
  {
    
    String idAttivitaATECOSec = request.getParameter("idAttivitaATECOSec");
    String codiceATECOSec = request.getParameter("codiceATECOSec");
    if(Validator.isNotEmpty(codiceATECOSec))
        codiceATECOSec = codiceATECOSec.trim();
    String descrizioneATECOSec = request.getParameter("descrizioneATECOSec");
    if(Validator.isNotEmpty(descrizioneATECOSec))
        descrizioneATECOSec = descrizioneATECOSec.trim();
    
    String codeAteco = request.getParameter("codeAteco");
    
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
          tipiAttivitaATECO = afClient.getTipiAttivitaATECO(codiceATECOSec, descrizioneATECOSec);
        }
        catch(SolmrException se) 
        {      
          SolmrLogger.info(this, " - sediUteModCtrl.jsp - FINE PAGINA");
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
    
    
    if(Validator.isNotEmpty(codeAteco))
    {
      if(codeAteco.equalsIgnoreCase(codiceATECOSec))
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "inserisciATECOSec",AnagErrors.ERR_COD_ATECO_GIA_PRESENTE);
      }
    }
    
    if(vAtecoSecUte != null)
    {
      if(vAtecoSecUte.size() > 0)
      {
        for(int j=0;j<vAtecoSecUte.size();j++)
        {
          UteAtecoSecondariVO uteAtecoSecondariVO = vAtecoSecUte.get(j);
          if(uteAtecoSecondariVO.getCodiceAteco().equalsIgnoreCase(codiceATECOSec))
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "inserisciATECOSec",AnagErrors.ERR_COD_ATECO_SEC_GIA_PRESENTE);
            break;
          } 
        }
      }
    }
    
    if (errors==null || errors.size()==0) 
    {
      UteAtecoSecondariVO uteAtecoSecondariVO = new UteAtecoSecondariVO();
      uteAtecoSecondariVO.setIdAttivitaAteco(new Long(idAttivitaATECOSec).longValue());
      uteAtecoSecondariVO.setCodiceAteco(codiceATECOSec);
      uteAtecoSecondariVO.setDescAttivitaAteco(descrizioneATECOSec);
      
      if(vAtecoSecUte == null)
      {
        vAtecoSecUte = new Vector<UteAtecoSecondariVO>();
      }
      
      vAtecoSecUte.add(uteAtecoSecondariVO);
      
      session.setAttribute("vAtecoSecUte", vAtecoSecUte);
      //operazione = "";
    }
    else
    {
      request.setAttribute("errors", errors);
    }
    
    request.getRequestDispatcher(viewUrl).forward(request, response);
    return;
  }
  else if (Validator.isNotEmpty(request.getParameter("operazione"))
    && "eliminaATECOSec".equalsIgnoreCase(request.getParameter("operazione"))) 
  {
    String[] chkAttivitaAtecoSec = request.getParameterValues("chkAttivitaAtecoSec");
    if(Validator.isNotEmpty(chkAttivitaAtecoSec))
    {
      for(int j=(chkAttivitaAtecoSec.length - 1);j>=0;j--)
      {
        vAtecoSecUte.remove(new Integer(chkAttivitaAtecoSec[j]).intValue());
      }
    }
    else
    {
      errors = ErrorUtils.setValidErrNoNull(errors, "eliminaATECOSec",AnagErrors.ERR_NO_ELEMENTI);
    }
    
    if (errors==null || errors.size()==0) 
    {
      if(vAtecoSecUte.size() == 0)
      {
        vAtecoSecUte = null;
      }
      
      session.setAttribute("vAtecoSecUte", vAtecoSecUte);
    }
    else
    {
      request.setAttribute("errors", errors);
    }
    
    request.getRequestDispatcher(viewUrl).forward(request, response);
    return;
  }
  else 
  {
   	if(request.getParameter("annulla") != null)
   	{
    	request.getRequestDispatcher(precUrlOk).forward(request, response);
    }
    // L'utente ha deciso di proseguire l'inserimento nonostante esista
    // già un'unità produttiva per il comune selezionato
    else if(Validator.isNotEmpty(request.getParameter("operazioneIns"))) 
    {
    
      uteVO.setIndirizzo(request.getParameter("indirizzo"));
	    uteVO.setProvincia(request.getParameter("provincia"));
	    uteVO.setComune(request.getParameter("comune"));
	    uteVO.setCap(request.getParameter("cap"));
	    uteVO.setDenominazione(request.getParameter("denominazione"));
	    uteVO.setTelefono(request.getParameter("telefono"));
	    uteVO.setFax(request.getParameter("fax"));
	    uteVO.setCodeOte(request.getParameter("codeOte"));
	    uteVO.setDescOte(request.getParameter("descOte"));
	    uteVO.setCodeAteco(request.getParameter("codeAteco"));
	    uteVO.setDescAteco(request.getParameter("descAteco"));
	    
	    if(Validator.isNotEmpty(request.getParameter("idZonaAltimetrica")))
	      uteVO.setIdZonaAltimetrica(new Long(request.getParameter("idZonaAltimetrica")));
	    
	    uteVO.setDataInizioAttivitaStr(request.getParameter("dataInizioAttivitaStr"));
	    uteVO.setNote(request.getParameter("note"));
	    uteVO.setMotivoModifica(request.getParameter("motivoModifica"));
	    
	    uteVO.setIdAzienda(anagAziendaVO.getIdAzienda());
    
    
    	errors = uteVO.validateInsUTE();
    	// validazione
    	String codiceOTE = "";
    	if(request.getParameter("codeOte") != null && !request.getParameter("codeOte").equals("")) 
    	{
      	codiceOTE = request.getParameter("codeOte");
      }
      String descrizioneOTE = "";
      if(request.getParameter("descOte") != null && !request.getParameter("descOte").equals("")) 
      {
      	if(request.getParameter("descOte").startsWith("%")) 
      	{
        	descrizioneOTE = request.getParameter("descOte").substring(1);
        }
        else 
        {
        	descrizioneOTE = request.getParameter("descOte");
        }
      }
      String codiceATECO = null;
      if(request.getParameter("codeAteco") != null && !request.getParameter("codeAteco").equals("")) 
      {
      	codiceATECO = request.getParameter("codeAteco");
      }
      String descrizioneATECO = "";
      if(request.getParameter("descAteco") != null && !request.getParameter("descAteco").equals("")) 
      {
      	if(request.getParameter("descAteco").startsWith("%")) 
      	{
        	descrizioneATECO = request.getParameter("descAteco").substring(1);
        }
        else 
        {
        	descrizioneATECO = request.getParameter("descAteco");
        }
      }

      Integer idZonaAltimetrica = null;
      if(request.getParameter("idZonaAltimetrica") != null && !request.getParameter("idZonaAltimetrica").equals("")) 
      {
      	idZonaAltimetrica =  new Integer(Integer.parseInt(request.getParameter("idZonaAltimetrica")));
      }

      if(codiceOTE != null && !codiceOTE.equals("")) 
      {
      	Long idCodiceOTE = null;
      	try 
      	{
        	idCodiceOTE = afClient.ricercaIdAttivitaOTE(codiceOTE.toString(),descrizioneOTE, false);
        	CodeDescription descriptionAttivitaOTE = new CodeDescription(Integer.decode(idCodiceOTE.toString()),descrizioneOTE);
        	descriptionAttivitaOTE.setSecondaryCode(codiceOTE.toString());
        	uteVO.setTipoAttivitaOTE(descriptionAttivitaOTE);
      	} 
      	catch (SolmrException ex) 
      	{
        	ValidationError error = new ValidationError(ex.getMessage());
        	errors.add("codeOte", error);
        	request.setAttribute("errors", errors);
      	}
    	}
    	// Se il codice OTE non è valorizzato ma la descrizione sì,
    	// costringo l'utente a valorizzare anche il codice OTE
    	else 
    	{
      	if(Validator.isNotEmpty(descrizioneOTE)) 
      	{
        	if(!Validator.isNotEmpty(codiceOTE)) 
        	{
          	errors.add("codeOte", new ValidationError((String)AnagErrors.get("ERR_CODICE_OTE_OBBLIGATORIO_WITH_DESCRIZIONE")));
        	}
      	}
    	}

    	if(Validator.isNotEmpty(codiceATECO)) 
    	{
      	Long idCodiceATECO = null;
      	try 
      	{
        	idCodiceATECO = afClient.ricercaIdAttivitaATECO(codiceATECO.toString(),descrizioneATECO, false);
        	CodeDescription descriptionAttivitaATECO = new CodeDescription(Integer.decode(idCodiceATECO.toString()),descrizioneATECO);
        	descriptionAttivitaATECO.setSecondaryCode(codiceATECO.toString());
        	uteVO.setTipoAttivitaATECO(descriptionAttivitaATECO);
      	} 
      	catch (SolmrException ex) 
      	{
        	ValidationError error = new ValidationError(ex.getMessage());
        	errors.add("codeAteco", error);
        	request.setAttribute("errors", errors);
      	}
    	}
   	 	else 
   	 	{
   	 	  //codice ateco null e presenti ateco secondari...
	      if((vAtecoSecUte != null) && (vAtecoSecUte.size() > 0))
	      {
	        errors = ErrorUtils.setValidErrNoNull(errors, "codeAteco",AnagErrors.ERR_NO_COD_ATECO_PRINC_E_ATECO_SEC);
	      }
	      else
	      {
	        // Se il codice ATECO non è valorizzato ma la descrizione sì,
          // costringo l'utente a valorizzare anche il codice OTE 
	      	if(Validator.isNotEmpty(descrizioneATECO)) 
	      	{
	        	if(!Validator.isNotEmpty(codiceATECO)) 
	        	{
	          	errors.add("codeAteco", new ValidationError((String)AnagErrors.get("ERR_CODICE_ATECO_OBBLIGATORIO_WITH_DESCRIZIONE")));
	        	}
	      	}
	      }
    	}
      if(idZonaAltimetrica!=null && !idZonaAltimetrica.equals(""))
      {
      	CodeDescription descriptionZonaAlt = new CodeDescription(idZonaAltimetrica, "");
      	uteVO.setTipoZonaAltimetrica(descriptionZonaAlt);
      }

      try 
      {
      	if(request.getParameter("comune") != null && !request.getParameter("comune").equals(""))
      	{
        	String comune = request.getParameter("comune");
        	String provincia = request.getParameter("provincia");
        	String istatComune = afClient.ricercaCodiceComuneNonEstinto(comune, provincia);
        	uteVO.setIstat(istatComune);
        }
      }
      catch(Exception ex) 
      {
      	ValidationError error = new ValidationError(ex.getMessage());
      	errors.add("comune", error);
      	request.setAttribute("errors", errors);
      }

      if(!(errors == null || errors.size() == 0)) 
      {
      	request.setAttribute("errors", errors);
      	request.getRequestDispatcher(viewUrl).forward(request, response);
      	return;
      }
      try 
      {
        uteVO.setvUteAtecoSec(vAtecoSecUte);
      	afClient.insertUte(uteVO, ruoloUtenza.getIdUtente());
      	ValidationErrors errorsVal = new ValidationErrors();
      	ValidationError error = new ValidationError("Inserimento eseguito");
      	errors.add("error",error);
      	request.setAttribute("errorsVal",errorsVal);
      	request.getRequestDispatcher(precUrlOk).forward(request, response);
      }
      catch(SolmrException ex) 
      {
      	ValidationError error = new ValidationError(ex.getMessage());
      	errors.add("error", error);
      	request.setAttribute("errors", errors);
      	request.getRequestDispatcher(viewUrl).forward(request, response);
      	return;
      }
    }
    else 
    {
    	request.getRequestDispatcher("/view/sediNewView.jsp").forward(request, response);
    }
  }

%>
