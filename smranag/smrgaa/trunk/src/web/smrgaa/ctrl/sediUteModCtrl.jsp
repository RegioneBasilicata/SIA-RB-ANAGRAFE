<%@page language="java" contentType="text/html"%>

<%@ page import="it.csi.solmr.util.*"%>
<%@ page import="it.csi.solmr.dto.anag.*"%>
<%@ page import="it.csi.solmr.dto.*"%>
<%@ page import="it.csi.solmr.client.anag.*"%>
<%@ page import="it.csi.solmr.exception.*"%>
<%@ page import="it.csi.solmr.etc.anag.*"%>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.ErrorUtils" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>

<%

	String iridePageName = "sediUteModCtrl.jsp";
  %>
   <%@include file = "/include/autorizzazione.inc" %>
  <%

  String viewUrl = "/view/sediUteModView.jsp";
  String precUrl = "/view/sediView.jsp";
  String precUrlOk = "/ctrl/sediCtrl.jsp";
  
  String actionUrl = "../layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione modifica ute. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  
  
  AnagFacadeClient afClient = new AnagFacadeClient();
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO) session.getAttribute("anagAziendaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni)session.getAttribute("utenteAbilitazioni");
  UteVO voModifica = (UteVO) session.getAttribute("voModifica");
  UteVO voModificato = null;
  ValidationErrors errors = new ValidationErrors();
  UteVO uteErrorsVO = new UteVO();
  // Effettuo il controllo sullo stato dell'azienda: CONSISTENZA VARIATA
  // E NOTIFICHE. Il controllo va fatto sempre quindi lo inserisco qui
  // in modo che venga sempre effettuato
  try 
  {
   	afClient.checkStatoAzienda(anagAziendaVO.getIdAzienda());
  }
  catch (SolmrException se) 
  {
   	request.setAttribute("statoAzienda", se);
  }
  	
  // Recupero le zone altimetriche
  it.csi.solmr.dto.CodeDescription[] elencoZonaAltimetrica = null;
  try 
  {
  	elencoZonaAltimetrica = afClient.getListTipoZonaAltimetrica(SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION);
  	request.setAttribute("elencoZonaAltimetrica", elencoZonaAltimetrica);
 	}
  catch(SolmrException se) 
  {
  	ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_ZONA_ALTIMETRICA);
  	errors.add("error", error);
  	request.setAttribute("errors", errors);
  	request.getRequestDispatcher(viewUrl).forward(request, response);
  	return;
  }
  	
  if(request.getParameter("salva") != null) 
  {
   	String istatComuneOLD = null;
   	String istatComuneNEW = null;
   	String zonaAltimetrica = request.getParameter("idZonaAltimetrica");
   	// validazione
   	uteErrorsVO.setIstat(request.getParameter("istatComune"));
   	
   	uteErrorsVO.setIndirizzo(request.getParameter("indirizzo"));
    uteErrorsVO.setProvincia(request.getParameter("provincia"));
    uteErrorsVO.setComune(request.getParameter("comune"));
    uteErrorsVO.setCap(request.getParameter("cap"));
    uteErrorsVO.setDenominazione(request.getParameter("denominazione"));
    uteErrorsVO.setTelefono(request.getParameter("telefono"));
    uteErrorsVO.setFax(request.getParameter("fax"));
    uteErrorsVO.setCodeOte(request.getParameter("codeOte"));
    uteErrorsVO.setDescOte(request.getParameter("descOte"));
    uteErrorsVO.setCodeAteco(request.getParameter("codeAteco"));
    uteErrorsVO.setDescAteco(request.getParameter("descAteco"));
    
    uteErrorsVO.setDataInizioAttivitaStr(request.getParameter("dataInizioAttivitaStr"));
    uteErrorsVO.setNote(request.getParameter("note"));
    uteErrorsVO.setMotivoModifica(request.getParameter("motivoModifica"));
    
   	if(Validator.isNotEmpty(zonaAltimetrica)) 
   	{
   		uteErrorsVO.setIdZonaAltimetrica(Long.decode(request.getParameter("idZonaAltimetrica")));
    }
    errors = uteErrorsVO.validateModUTE();
    if(!(errors == null || errors.size() == 0)) 
    {
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(viewUrl).forward(request, response);
    	return;
    }
    if(session.getAttribute("voModifica") != null) 
    {
    	istatComuneOLD = voModifica.getIstat();
    	istatComuneNEW = request.getParameter("istatComune");
    	if(utenteAbilitazioni.getRuolo().isUtenteIntermediario() && ruoloUtenza.isReadWrite()) 
    	{
      	try 
      	{
          afClient.utenteConDelega(utenteAbilitazioni, voModifica.getIdAzienda());
        }
        catch (SolmrException se) 
        {
        	ValidationError error = new ValidationError(se.getMessage());
        	errors.add("error", error);
        	request.setAttribute("errors", errors);
        	request.getRequestDispatcher(viewUrl).forward(request, response);
        	return;
        }
      }
      if(voModifica.getDataInizioAttivita() != null) 
      {
      	int result = voModifica.getDataInizioAttivita().compareTo(DateUtils.parseDate(DateUtils.getCurrentDateString()));
        if(result > 0) 
        {
        	errors.add("dataInizioAttivitaStr", new ValidationError("Impossibile effettuare la modifica. La data inizio attivita'' e'' maggiore della data del giorno."));
        	request.setAttribute("errors", errors);
        	request.getRequestDispatcher(viewUrl).forward(request, response);
        	return;
        }
            
        java.util.Date dataInizioAttivitaDate = DateUtils.parseDate(request.getParameter("dataInizioAttivitaStr"));
        java.util.Date dataInizioConduzione = afClient.getMinDataInizioConduzione(voModifica.getIdUte());
        if(dataInizioConduzione!=null)
        {
          result = dataInizioAttivitaDate.compareTo(dataInizioConduzione);
          if(result > 0) 
          {
            errors.add("dataInizioAttivitaStr", new ValidationError("Impossibile effettuare la modifica. La data inizio attivita'' deve essere antecedente alla data inizio conduzione("+DateUtils.formatDate(dataInizioConduzione)+")."));
            request.setAttribute("errors", errors);
            request.getRequestDispatcher(viewUrl).forward(request, response);
            return;
          }
        }
            
        java.util.Date dataInizioAllevamento = afClient.getMinDataInizioAllevamento(voModifica.getIdUte());
        if(dataInizioAllevamento!=null)
        {
          result = dataInizioAttivitaDate.compareTo(dataInizioAllevamento);
          if(result > 0) 
          {
            errors.add("dataInizioAttivitaStr", new ValidationError("Impossibile effettuare la modifica. La data inizio attivita'' deve essere inferiore alla data inizio allevamento("+DateUtils.formatDate(dataInizioAllevamento)+")."));
            request.setAttribute("errors", errors);
            request.getRequestDispatcher(viewUrl).forward(request, response);
            return;
            }
          }
            
          java.util.Date dataInizioFabbricati = afClient.getMinDataInizioFabbricati(voModifica.getIdUte());
          if(dataInizioFabbricati!=null)
          {
            result = dataInizioAttivitaDate.compareTo(dataInizioFabbricati);
            if(result > 0) 
            {
              errors.add("dataInizioAttivitaStr", new ValidationError("Impossibile effettuare la modifica. La data inizio attivita'' deve essere inferiore alla data inizio validita''("+DateUtils.formatDate(dataInizioFabbricati)+")."));
              request.setAttribute("errors", errors);
              request.getRequestDispatcher(viewUrl).forward(request, response);
              return;
            }
          }
        }
      	try 
      	{
          voModificato = afClient.getUteById(voModifica.getIdUte());
        	voModificato.setIdAzienda(voModifica.getIdAzienda());
      	}
      	catch (SolmrException sex) 
      	{
        	ValidationError error = new ValidationError(sex.getMessage());
        	errors.add("error", error);
        	request.setAttribute("errors", errors);
        	request.getRequestDispatcher(viewUrl).forward(request, response);
        	return;
      	}
      	voModificato.setIstat(request.getParameter("istatComune"));
      	voModificato.setDenominazione(request.getParameter("denominazione"));
      	voModificato.setIndirizzo(request.getParameter("indirizzo"));
      	voModificato.setIdComune(request.getParameter("istatComune"));
      	voModificato.setCap(request.getParameter("cap"));
      	voModificato.setTelefono(request.getParameter("telefono"));
      	voModificato.setFax(request.getParameter("fax"));
      	if(Validator.isNotEmpty(zonaAltimetrica)) 
      	{
      		voModificato.setIdZonaAltimetrica(Long.decode(zonaAltimetrica));
      	}
      	voModificato.setNote(request.getParameter("note"));
      	voModificato.setMotivoModifica(request.getParameter("motivoModifica"));
      	if(request.getParameter("dataInizioAttivitaStr") != null 
      	  && !request.getParameter("dataInizioAttivitaStr").equals("")) 
      	{
          voModificato.setDataInizioAttivitaStr(request.getParameter("dataInizioAttivitaStr"));
        	voModificato.setDataInizioAttivita(DateUtils.parseDate(voModificato.getDataInizioAttivitaStr()));
      	}
      	voModificato.setCodeOte(request.getParameter("codeOte"));
      	voModificato.setCodeAteco(request.getParameter("codeAteco"));
      	voModificato.setDescOte(request.getParameter("descOte"));
      	voModificato.setDescAteco(request.getParameter("descAteco"));
      	voModificato.setUtenteAggiornamento(utenteAbilitazioni.getIdUtenteLogin());
    	}
    	if(voModificato.getCodeOte() != null 
    	  && !voModificato.getCodeOte().equals("")) 
    	{
      	try 
      	{
        	afClient.ricercaIdAttivitaOTE(voModificato.getCodeOte().toString(), voModificato.getDescOte(), false);
        	CodeDescription descriptionAttivitaOTE = new CodeDescription(Integer.decode(voModificato.getCodeOte().toString()), voModificato.getDescOte());
        	descriptionAttivitaOTE.setSecondaryCode(voModificato.getCodeOte().toString());
        	voModificato.setTipoAttivitaOTE(descriptionAttivitaOTE);
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
    		if(Validator.isNotEmpty(voModificato.getDescOte())) 
    		{
        	if(!Validator.isNotEmpty(voModificato.getCodeOte())) 
        	{
          	errors.add("codeOte", new ValidationError((String) AnagErrors.get("ERR_CODICE_OTE_OBBLIGATORIO_WITH_DESCRIZIONE")));
        	}
      	}
    	}
    	
    	if(Validator.isNotEmpty(voModificato.getCodeAteco()))
    	{
    	  if(voModifica.getvUteAtecoSec() != null)
	      {
	        if(voModifica.getvUteAtecoSec().size() > 0)
	        {
	          for(int j=0;j<voModifica.getvUteAtecoSec().size();j++)
	          {
	            UteAtecoSecondariVO uteAtecoSecondariVO = voModifica.getvUteAtecoSec().get(j);
	            if(uteAtecoSecondariVO.getCodiceAteco().equalsIgnoreCase(voModificato.getCodeAteco()))
	            {
	              errors = ErrorUtils.setValidErrNoNull(errors, "codeAteco",AnagErrors.ERR_COD_ATECO_SEC_GIA_PRESENTE);
	              break;
	            } 
	          }
	        }
	      }
    	
      	try 
      	{
        	afClient.ricercaIdAttivitaATECO(voModificato.getCodeAteco().toString(), voModificato.getDescAteco(), false);
        	//CodeDescription descriptionAttivitaATECO = new CodeDescription(Integer.decode(voModificato.getCodeAteco().toString()), voModificato.getDescAteco());
        	CodeDescription descriptionAttivitaATECO = new CodeDescription();
          //descriptionAttivitaATECO.setSecondaryCode(voModificato.getCodeAteco().toString());
          descriptionAttivitaATECO.setDescription(voModificato.getDescAteco());
          descriptionAttivitaATECO.setSecondaryCode(voModificato.getCodeAteco().toString());
        	voModificato.setTipoAttivitaATECO(descriptionAttivitaATECO);
      	}
      	catch (SolmrException ex) 
      	{
        	ValidationError error = new ValidationError(ex.getMessage());
        	errors.add("codeAteco", error);
        	request.setAttribute("errors", errors);
      	}
    	}
    	// Se il codice ATECO non è valorizzato ma la descrizione sì,
    	// costringo l'utente a valorizzare anche il codice OTE
    	else 
    	{
    	  //codice ateco null e presenti ateco secondari...
        if((voModifica.getvUteAtecoSec() != null) && (voModifica.getvUteAtecoSec().size() > 0))
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "codeAteco",AnagErrors.ERR_NO_COD_ATECO_PRINC_E_ATECO_SEC);
        }
    	  else
    	  {
	    		if(Validator.isNotEmpty(voModificato.getDescAteco())) 
	    		{
	        	if(!Validator.isNotEmpty(voModificato.getCodeAteco())) 
	        	{
	          	errors.add("codeAteco", new ValidationError((String) AnagErrors.get("ERR_CODICE_ATECO_OBBLIGATORIO_WITH_DESCRIZIONE")));
	        	}
	      	}
	      }
    	}
    	
    	voModificato.setvUteAtecoSec(voModifica.getvUteAtecoSec());
    	
    	if(!(errors == null || errors.size() == 0)) 
    	{
    		request.setAttribute("errors", errors);
      	request.getRequestDispatcher(viewUrl).forward(request, response);
      	return;
    	}
    	String istatComune = null;
    	try 
    	{
    		istatComune = afClient.ricercaCodiceComuneNonEstinto(request.getParameter("comune"),
       	request.getParameter("provincia"));
      	voModificato.setIstat(istatComune);
      	voModificato.setIdComune(istatComune);
      	// se l'utente è passato dal pulsante cerca del comune avrò l'istat valorizzato
      	if(istatComuneNEW != null && !istatComuneNEW.equals("")) 
      	{
        	if(istatComuneOLD == null || istatComuneOLD.equals("") || !istatComuneOLD.equals(istatComuneNEW)) 
        	{
          	try 
          	{
            	afClient.countUteByAziendaAndComune(voModificato.getIdAzienda(), voModificato.getIstat());
          	}
          	catch (SolmrException se) 
          	{
            	if(se.getMessage().equalsIgnoreCase((String) AnagErrors.get("ESISTE_UTE_CON_COMUNE"))) 
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
  				}
  			}
    		afClient.updateUte(voModificato);
  			request.getRequestDispatcher(precUrlOk).forward(request, response);
  			return;
  		} 
    	catch (SolmrException ex) 
    	{
    		ValidationError error = new ValidationError(ex.getMessage());
    		if(ex.getMessage().equals(AnagErrors.RICERCACOMUNI) || ex.getMessage().equals(AnagErrors.RICERCATROPPICOMUNI)) {
      			errors.add("comune", error);
    		}
    		else 
    		{
      		errors.add("error", error);
    		}
    		request.setAttribute("errors", errors);
    		request.getRequestDispatcher(viewUrl).forward(request, response);
    		return;
  		}
  	}
  	if(request.getParameter("annulla") != null) 
  	{
  		request.getRequestDispatcher(precUrl).forward(request, response);
  	}
  	else if (request.getParameter("elenco") != null) 
  	{
    	request.getRequestDispatcher(precUrl).forward(request, response);
  	}
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
      
      if(voModifica.getvUteAtecoSec() != null)
      {
	      if(voModifica.getvUteAtecoSec().size() > 0)
	      {
	        for(int j=0;j<voModifica.getvUteAtecoSec().size();j++)
	        {
	          UteAtecoSecondariVO uteAtecoSecondariVO = voModifica.getvUteAtecoSec().get(j);
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
        
        if(voModifica.getvUteAtecoSec() == null)
	      {
	        voModifica.setvUteAtecoSec(new Vector<UteAtecoSecondariVO>());
	      }
        
        voModifica.getvUteAtecoSec().add(uteAtecoSecondariVO);
        
        session.setAttribute("voModifica", voModifica);
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
          voModifica.getvUteAtecoSec().remove(new Integer(chkAttivitaAtecoSec[j]).intValue());
        }
      }
      else
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "eliminaATECOSec",AnagErrors.ERR_NO_ELEMENTI);
      }
      
      if (errors==null || errors.size()==0) 
      {
        if(voModifica.getvUteAtecoSec().size() == 0)
        {
          voModifica.setvUteAtecoSec(null);
        }
        
        session.setAttribute("voModifica", voModifica);
      }
      else
      {
        request.setAttribute("errors", errors);
      }
      
      request.getRequestDispatcher(viewUrl).forward(request, response);
      return;
    }
  	else if (Validator.isNotEmpty(request.getParameter("operazioneIns"))) 
  	{
    	// validazione
    	uteErrorsVO.setIstat(request.getParameter("istatComune"));
    	errors = uteErrorsVO.validateModUTE();
    	if(!(errors == null || errors.size() == 0)) 
    	{
      		request.setAttribute("errors", errors);
      		request.getRequestDispatcher(viewUrl).forward(request, response);
      		return;
    	}
    	if(session.getAttribute("voModifica") != null) 
    	{
      	if(utenteAbilitazioni.getRuolo().isUtenteIntermediario() && ruoloUtenza.isReadWrite())
      	{
        	try 
        	{
          	afClient.utenteConDelega(utenteAbilitazioni, voModifica.getIdAzienda());
        	}
        	catch (SolmrException se) 
        	{
         		ValidationError error = new ValidationError(se.getMessage());
          	errors.add("error", error);
          	request.setAttribute("errors", errors);
          	request.getRequestDispatcher(viewUrl).forward(request, response);
          	return;
        	}
      	}
      	else if(!ruoloUtenza.isReadWrite() 
      	  || (!utenteAbilitazioni.getRuolo().isUtenteIntermediario() && !utenteAbilitazioni.getRuolo().isUtenteProvinciale())) 
      	{
        	ValidationError error = new ValidationError(AnagErrors.ERROREPROFILO);
        	errors.add("error", error);
        	request.setAttribute("errors", errors);
        	request.getRequestDispatcher(viewUrl).forward(request, response);
        	return;
      	}
      	if(voModifica.getDataInizioAttivita() != null) 
      	{
        	int result = voModifica.getDataInizioAttivita().compareTo(DateUtils.parseDate(DateUtils.getCurrentDateString()));
        	if(result > 0) 
        	{
          	errors.add("dataInizioAttivitaStr", new ValidationError("Impossibile effettuare la modifica. La data inizio attivita'' e'' maggiore della data del giorno."));
          	request.setAttribute("errors", errors);
          	request.getRequestDispatcher(viewUrl).forward(request, response);
          	return;
        	}
      	}
      	try 
      	{
        	voModificato = afClient.getUteById(voModifica.getIdUte());
        	voModificato.setIdAzienda(voModifica.getIdAzienda());
      	}
      	catch(SolmrException sex) 
      	{
        	ValidationError error = new ValidationError(sex.getMessage());
        	errors.add("error", error);
        	request.setAttribute("errors", errors);
        	request.getRequestDispatcher(viewUrl).forward(request, response);
        	return;
      	}
      	voModificato.setIstat(request.getParameter("istatComune"));
      	voModificato.setDenominazione(request.getParameter("denominazione"));
      	voModificato.setIndirizzo(request.getParameter("indirizzo"));
      	voModificato.setIdComune(request.getParameter("istatComune"));
      	voModificato.setCap(request.getParameter("cap"));
      	voModificato.setTelefono(request.getParameter("telefono"));
      	voModificato.setFax(request.getParameter("fax"));
      	if(Validator.isNotEmpty(request.getParameter("idZonaAltimetrica"))) 
      	{
      		voModificato.setIdZonaAltimetrica(Long.decode(request.getParameter("idZonaAltimetrica")));
      	}
      	voModificato.setNote(request.getParameter("note"));
      	voModificato.setMotivoModifica(request.getParameter("motivoModifica"));
      	if(request.getParameter("dataInizioAttivitaStr") != null 
      	  && !request.getParameter("dataInizioAttivitaStr").equals("")) 
      	{
        	voModificato.setDataInizioAttivitaStr(request.getParameter("dataInizioAttivitaStr"));
        	voModificato.setDataInizioAttivita(DateUtils.parseDate(voModificato.getDataInizioAttivitaStr()));
      	}
      	voModificato.setCodeOte(request.getParameter("codeOte"));
      	voModificato.setCodeAteco(request.getParameter("codeAteco"));
     	 	voModificato.setDescOte(request.getParameter("descOte"));
      	voModificato.setDescAteco(request.getParameter("descAteco"));
      	voModificato.setUtenteAggiornamento(utenteAbilitazioni.getIdUtenteLogin());
      	if(voModificato.getCodeOte() != null && !voModificato.getCodeOte().equals("")) 
      	{
        	try 
        	{
          	afClient.ricercaIdAttivitaOTE(voModificato.getCodeOte().toString(), voModificato.getDescOte(), false);
          	CodeDescription descriptionAttivitaOTE = new CodeDescription(Integer.decode(voModificato.getCodeOte().toString()), voModificato.getDescOte());
          	descriptionAttivitaOTE.setSecondaryCode(voModificato.getCodeOte().toString());
          	voModificato.setTipoAttivitaOTE(descriptionAttivitaOTE);
        	}
        	catch(SolmrException ex) 
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
        	if(Validator.isNotEmpty(voModificato.getDescOte())) 
        	{
          	if(!Validator.isNotEmpty(voModificato.getCodeOte())) 
          	{
            	errors.add("codeOte", new ValidationError((String) AnagErrors.get("ERR_CODICE_OTE_OBBLIGATORIO_WITH_DESCRIZIONE")));
          	}
        	}
      	}
      	if(voModificato.getCodeAteco() != null && !voModificato.getCodeAteco().equals("")) 
      	{
        	try 
        	{
          	afClient.ricercaIdAttivitaATECO(voModificato.getCodeAteco().toString(), voModificato.getDescAteco(), false);
          	CodeDescription descriptionAttivitaATECO = new CodeDescription(Integer.decode(voModificato.getCodeAteco().toString()), voModificato.getDescAteco());
          	descriptionAttivitaATECO.setSecondaryCode(voModificato.getCodeAteco().toString());
          	voModificato.setTipoAttivitaATECO(descriptionAttivitaATECO);
        	}
        	catch(SolmrException ex) 
        	{
          	ValidationError error = new ValidationError(ex.getMessage());
          	errors.add("codeAteco", error);
          	request.setAttribute("errors", errors);
        	}
      	}
      	// Se il codice ATECO non è valorizzato ma la descrizione sì,
      	// costringo l'utente a valorizzare anche il codice OTE
      	else 
      	{
        	if(Validator.isNotEmpty(voModificato.getDescAteco())) 
        	{
          	if(!Validator.isNotEmpty(voModificato.getCodeAteco())) 
          	{
            	errors.add("codeAteco", new ValidationError((String) AnagErrors.get("ERR_CODICE_ATECO_OBBLIGATORIO_WITH_DESCRIZIONE")));
          	}
        	}
      	}
    	}
    	if(!(errors == null || errors.size() == 0)) 
    	{
      	request.setAttribute("errors", errors);
      	request.getRequestDispatcher(viewUrl).forward(request, response);
      	return;
    	}
    	String istatComune = null;
    	try 
    	{
      	istatComune = afClient.ricercaCodiceComuneNonEstinto(request.getParameter("comune"),
       	request.getParameter("provincia"));
      	voModificato.setIstat(istatComune);
      	voModificato.setIdComune(istatComune);
    	}
    	catch (SolmrException se) 
    	{
      	ValidationError error = new ValidationError(se.getMessage());
      	errors.add("error", error);
      	request.setAttribute("errors", errors);
      	request.getRequestDispatcher(viewUrl).forward(request, response);
      	return;
    	}
    	try 
    	{
      	afClient.updateUte(voModificato);
      	request.getRequestDispatcher(precUrlOk).forward(request, response);
      	return;
    	}
    	catch(SolmrException se) 
    	{
      	ValidationError error = new ValidationError(se.getMessage());
      	errors.add("error", error);
      	request.setAttribute("errors", errors);
      	request.getRequestDispatcher(viewUrl).forward(request, response);
      	return;
    	}
  	}
%>