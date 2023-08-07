<%@ page language="java" contentType="text/html" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.ws.infoc.*" %>
<%@ page import="it.csi.solmr.dto.anag.sian.SianAnagTributariaVO" %>
<%@page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>


<%

	// Pulisco la sessione dai filtri di altre sezioni
  String noRemove = "";
  WebUtils.removeUselessFilter(session, noRemove);

  String iridePageName = "nuovaAziendaAnagraficaCtrl.jsp";
  %>
  	<%@include file = "/include/autorizzazione.inc" %>
  <%

	String url = "/view/nuovaAziendaAnagraficaView.jsp";
  String urlCUAAdup = "/view/confermaNuovaAziendaCUAAdupView.jsp";


	String inserimentoTitolareUrl = "/view/nuovaAziendaTitolareView.jsp";
  String inserimentoRappresentanteLegaleUrl = "/view/nuovaAziendaRappLegView.jsp";

  String pageDup= "../layout/nuovaAziendaAnagraficaForward.htm";

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni)session.getAttribute("utenteAbilitazioni");

  PersonaFisicaVO personaFisicaVO = (PersonaFisicaVO)session.getAttribute("insPerFisVO");

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("insAnagVO");

  session.removeAttribute("anagAziendaVO");
  session.removeAttribute("anagPassaggioVO");
  session.removeAttribute("anagAziendaSocietaVO");

  boolean salva = false;

  String radiobuttonAzienda = (String)request.getParameter("radiobuttonAzienda");
	request.setAttribute("radiobuttonAzienda", radiobuttonAzienda);
  ValidationErrors errors = new ValidationErrors();
	String cuaaProvenienza = (String)request.getParameter("cuaaProvenienza");
	request.setAttribute("cuaaProvenienza", cuaaProvenienza);

	if(request.getParameter("indietro") != null) 
  {
  	String CUAA = (String)request.getParameter("CUAA");
  	String CUAASubentro = (String)request.getParameter("CUAASubentro");
  	request.setAttribute("CUAA",CUAA);


  	if(radiobuttonAzienda == null || "".equals(radiobuttonAzienda)) 
    {
  		if(anagAziendaVO.isFlagAziendaProvvisoria()) 
      {
    		if(anagAziendaVO.getIdAziendaSubentro() != null) 
        {
      		AnagAziendaVO temp = anagFacadeClient.getAziendaByIdAzienda(anagAziendaVO.getIdAziendaSubentro());
      		if(temp != null) 
          {
        		CUAASubentro=temp.getCUAA();
      		}
      		radiobuttonAzienda = "3";
    		}
    		else 
        {
    			radiobuttonAzienda = "2";
    		}
  		}
  		else 
      {
    		radiobuttonAzienda = "1";
  		}
  	}

  	if(CUAASubentro == null || "".equals(CUAASubentro)) 
    {
    	if(anagAziendaVO != null && anagAziendaVO.getIdAziendaSubentro() != null) 
      {
      	AnagAziendaVO temp = anagFacadeClient.getAziendaByIdAzienda(anagAziendaVO.getIdAziendaSubentro());
      	if(temp != null) 
        {
        	CUAASubentro = temp.getCUAA();
      	}
    	}
  	}

  	request.setAttribute("radiobuttonAzienda",radiobuttonAzienda);
  	%>
    		<jsp:forward page = "/view/nuovaAziendaCCIAAView.jsp">
      		<jsp:param name="CUAASubentro" value="<%= CUAASubentro %>" />
      		<jsp:param name="radiobuttonAzienda" value="<%= radiobuttonAzienda %>" />
      		<jsp:param name="cuaaProvenienza" value="<%= cuaaProvenienza %>" />
    		</jsp:forward>
  	<%
  	return;
	}

	if(request.getParameter("avanti") != null) 
  {
  	// Prelievo dei parametri per la costruzione del value object.
  	String cuaa = request.getParameter("CUAA");
  	String partitaIVA = request.getParameter("partitaIVA");
  	String denominazione = request.getParameter("denominazione");
  	String intestazionePartitaIva = request.getParameter("intestazionePartitaIva");
  	String tipiFormaGiuridica = request.getParameter("tipiFormaGiuridica");
  	String tipoIntermediarioDelegato = request.getParameter("idIntermediario");
  	String idTipoIntermediarioDelegato = null;
  	if(tipoIntermediarioDelegato != null && !tipoIntermediarioDelegato.equals("")) 
    {
    	idTipoIntermediarioDelegato = tipoIntermediarioDelegato;
  	}
  	else 
    {
    	if(utenteAbilitazioni.getRuolo().isUtenteIntermediario()) 
      {
        //era profiliUtenza.getIdEnte discutibile proviamo...
      	idTipoIntermediarioDelegato = new Long(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getIdIntermediario()).toString();
    	}
  	}
  	Integer idTipoFormaGiuridica = null;

  	if(tipiFormaGiuridica != null && !tipiFormaGiuridica.equals("")) 
    {
    	idTipoFormaGiuridica = Integer.decode(request.getParameter("tipiFormaGiuridica"));
  	}

    Integer idTipoAzienda = null;
    if(request.getParameter("tipiAzienda") != null 
      && !request.getParameter("tipiAzienda").equals("")) 
    {
    	idTipoAzienda = Integer.decode(request.getParameter("tipiAzienda"));
    }

  	// Recupero il valore della provincia competenza selezionata
  	String provinciaCompetenza = null;

  	if(utenteAbilitazioni.getRuolo().isUtenteProvinciale()) 
    {
      //Anche qui profiloUtenza.getIdEnte...speriamo
  		provinciaCompetenza = new Long(utenteAbilitazioni.getEnteAppartenenza().getAmmCompetenza().getIdAmmCompetenza()).toString();
  	}
  	else 
    {
  		provinciaCompetenza = request.getParameter("provincePiemonte");
  	}
  	
  	// Se l'oggetto AnagAziendaVO è nullo significa che è la prima volta che tento di effettuare
    // l'inserimento di una nuova azienda agricola è quindi lo istanzio.
    if(anagAziendaVO == null) 
    {
      anagAziendaVO = new AnagAziendaVO();
    }
  	
  	anagAziendaVO.setTelefono(request.getParameter("telefono"));
    anagAziendaVO.setFax(request.getParameter("fax"));
    anagAziendaVO.setSitoWEB(request.getParameter("sitoWEB"));
    anagAziendaVO.setMail(request.getParameter("mail"));
    anagAziendaVO.setPec(request.getParameter("pec"));
  	anagAziendaVO.setNote(request.getParameter("note"));


  	// Setto il value object
  	anagAziendaVO.setCUAA(cuaa.toUpperCase());
  	try 
    {
    	anagAziendaVO.setIdAziendaSubentro(new Long(request.getParameter("idAziendaSubentro")));
  	}
  	catch(Exception e) {}

  	anagAziendaVO.setOldCUAA(cuaa.toUpperCase());
  	anagAziendaVO.setPartitaIVA(partitaIVA);
  	anagAziendaVO.setDenominazione(denominazione.toUpperCase());
  	if(Validator.isNotEmpty(intestazionePartitaIva))
  	 anagAziendaVO.setIntestazionePartitaIva(intestazionePartitaIva.toUpperCase());
  	if(idTipoFormaGiuridica != null) 
    {
    	anagAziendaVO.setTipiFormaGiuridica(String.valueOf(idTipoFormaGiuridica));
    	anagAziendaVO.setTipoFormaGiuridica(new CodeDescription(Integer.decode(tipiFormaGiuridica),""));
    	String flagPartitaIva = anagFacadeClient.getFlagPartitaIva(Long.decode(tipiFormaGiuridica));
    	anagAziendaVO.setFlagPartitaIva(flagPartitaIva);
  	}
  	else 
    {
    	anagAziendaVO.setTipiFormaGiuridica(null);
    	anagAziendaVO.setTipoFormaGiuridica(null);
  	}
  	if(idTipoAzienda != null) 
    {
    	anagAziendaVO.setTipiAzienda(String.valueOf(idTipoAzienda));
    	String tipiAzienda = anagFacadeClient.getDescriptionFromCode(SolmrConstants.TAB_TIPO_TIPOLOGIA_AZIENDA, idTipoAzienda);
    	anagAziendaVO.setTipoTipologiaAzienda(new CodeDescription(idTipoAzienda,tipiAzienda));
  	}

    anagAziendaVO.setProvincePiemonte(provinciaCompetenza);
    anagAziendaVO.setProvCompetenza(provinciaCompetenza);
    
    
    
    
    
    
    
    
   

  	if(radiobuttonAzienda!=null && !"".equals(radiobuttonAzienda)) 
    {
    	if("2".equals(radiobuttonAzienda) || "3".equals(radiobuttonAzienda)) 
      {
      	anagAziendaVO.setFlagAziendaProvvisoria(true);
    	}
    	else 
      {
    		anagAziendaVO.setFlagAziendaProvvisoria(false);
    	}
  	}

  	// Controllo la validità dei dati inseriti dall'utente
  	errors = anagAziendaVO.validateInsertAnag();
  	
  	anagAziendaVO.setIdIntermediarioDelegato(idTipoIntermediarioDelegato);
  	session.setAttribute("insAnagVO",anagAziendaVO);
  	if(errors != null && errors.size() > 0) 
    {
  		request.setAttribute("errors", errors);
  		request.getRequestDispatcher(url).forward(request, response);
  		return;
  	}

  	
  	// Se l'azienda agricola è di tipo individuale e il codice fiscale è corretto controllo
  	// che esista già una persona fisica con codice fiscale uguale a quello inserito dall'utente

  	if("2".equals(radiobuttonAzienda) || "3".equals(radiobuttonAzienda)) 
    {
    	anagAziendaVO.setFlagAziendaProvvisoria(true);
  	}
  	else 
    {
  		anagAziendaVO.setFlagAziendaProvvisoria(false);
  	}
  	
  	
  	//controllo che nn esista un'azienda in fase di nuova registrazione nella sezione nuova iscrizione
  	AziendaNuovaVO aziendaNuovaVO = gaaFacadeClient.getAziendaNuovaIscrizione(
       anagAziendaVO.getCUAA(), new long[]{SolmrConstants.RICHIESTA_NUOVA_ISCRIZIONE,SolmrConstants.RICHIESTA_NI_AZIENDA_OBSOLETA});
  	if(Validator.isNotEmpty(aziendaNuovaVO) 
        && (aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_VALIDATA) < 0))
    {
  	  errors.add("error", new ValidationError(AnagErrors.ERRORE_NUOVA_AZIENDA_GIA_IN_NUOVA_ISCRIZIONE));
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;  	
  	}
  	

  	if(("1".equals(radiobuttonAzienda) || "3".equals(radiobuttonAzienda) 
      || radiobuttonAzienda.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      && anagFacadeClient.isFlagUnivocitaAzienda(idTipoAzienda)) 
    {
  		//L'azienda che si vuole inserire presenta il controllo
  		//di univocità quindi procedo con i controlli consueti
  		// Controllo che non esista già in archivio un'azienda valida con il CUAA e la partita IVA indicati
     	try 
      {
      	anagFacadeClient.checkCUAAandCodFiscale(anagAziendaVO.getCUAA(), anagAziendaVO.getPartitaIVA());
        session.setAttribute("insAnagVO",anagAziendaVO);
       	salva = true;
     	}
     	catch(SolmrException se) 
      {
      	ValidationError error = new ValidationError(se.getMessage());
      	if(se.getMessage().equalsIgnoreCase(""+AnagErrors.get("CUAA_GIA_ESISTENTE"))) 
        {
    	 		errors.add("CUAA", error);
   			}
   			else if(se.getMessage().equalsIgnoreCase(""+AnagErrors.get("PIVA_GIA_ESISTENTE"))) 
        {
     			errors.add("partitaIVA", error);
   			}
   			errors.add("error", error);
   			request.setAttribute("errors", errors);
   			request.getRequestDispatcher(url).forward(request, response);
   			return;
  		}
    }
    else 
    {
  		// L'azienda che si vuole inserire non presenta il controllo di univocitàquindi anche se trovo
  		// un CUAA o una partita IVA chiedo se vuole proseguire invece di bloccarlo
  		AnagAziendaVO anagTemp = anagFacadeClient.getAziendaCUAAandCodFiscale(anagAziendaVO.getCUAA(), anagAziendaVO.getPartitaIVA());
  		session.setAttribute("insAnagVO",anagAziendaVO);
  		if(anagTemp == null) {
     		// Non ho trovato record quindi procedo normalmente
    		salva = true;
  		}
  		else {
     		// Ho trovato un record quindi visualizzarlo e chedere all'utente se
     		// è sicuro di voler proseguire
    		request.setAttribute("pageDup",pageDup);
    		if(anagTemp.getCUAA().equalsIgnoreCase(anagAziendaVO.getCUAA())) {
      			request.setAttribute("domanda", (String)AnagErrors.get("ERR_CUAA_DUP"));
    		}
    		else {
      			request.setAttribute("domanda", (String)AnagErrors.get("ERR_PARTITA_IVA_DUP"));
    		}
    		request.setAttribute("radiobuttonAzienda", radiobuttonAzienda);
    		request.setAttribute("anagDup",anagTemp);
    		request.getRequestDispatcher(urlCUAAdup).forward(request, response);
    		return;
  		}
    }
  }

	if(request.getParameter("conferma") != null || salva) 
  {
  	// Recupero i dati del rappresentante legale di AAEP che ho memorizzato in sessione
  	RappresentanteLegale rappresentanteLegaleAAEP = (RappresentanteLegale)session.getAttribute("rappresentanteLegaleAAEP");
    SianAnagTributariaVO anagTribForInsert = (SianAnagTributariaVO)session.getAttribute("anagTribForInsert");
  	if(personaFisicaVO == null) 
    {
    	try 
      {
      	if(anagAziendaVO.getTipoFormaGiuridica().getCode()
          .compareTo(SolmrConstants.TIPO_FORMA_GIURIDICA_INDIVIDUALE) == 0 
          || anagAziendaVO.getTipoFormaGiuridica().getCode()
          .compareTo(Integer.decode(SolmrConstants.ID_SOGGETTO_NON_COSTITUITO)) == 0) 
        {
       		personaFisicaVO = anagFacadeClient.getPersonaFisica(anagAziendaVO.getCUAA().toUpperCase());
        	personaFisicaVO.setNewPersonaFisica(false);
        }
        else 
        {
        	// Se CUAA non è un codice fiscale
        	if(rappresentanteLegaleAAEP!=null 
            && Validator.isNotEmpty(rappresentanteLegaleAAEP.getCodiceFiscale())) 
          {
          	personaFisicaVO = anagFacadeClient.getPersonaFisica(rappresentanteLegaleAAEP.getCodiceFiscale().getValue());
            personaFisicaVO.setNewPersonaFisica(false);
          }
          else if (anagTribForInsert!=null && Validator.isNotEmpty(anagTribForInsert.getCodiceFiscaleRappresentante()))
          {
            personaFisicaVO = anagFacadeClient.getPersonaFisica(anagTribForInsert.getCodiceFiscaleRappresentante());
            personaFisicaVO.setNewPersonaFisica(false);
          }
          else 		personaFisicaVO = new PersonaFisicaVO();
        }
      }
      catch(SolmrException se) 
      {
      	if(!se.getMessage().equals(""+AnagErrors.get("CUAA_INESISTENTE"))) 
        {
        	ValidationError error = new ValidationError(se.getMessage());
        	errors.add("error", error);
        	request.setAttribute("errors", errors);
        	request.getRequestDispatcher(url).forward(request, response);
        	return;
        }
        else if(se.getMessage().equalsIgnoreCase(""+AnagErrors.get("CUAA_INESISTENTE"))) 
        {
        	personaFisicaVO = new PersonaFisicaVO();
        	personaFisicaVO.setNewPersonaFisica(true);
        }
      }
    }

  	Sede sedeAAEP = (Sede)session.getAttribute("sedeAAEP");
  	ComuneVO comune = null;
  	if(sedeAAEP != null) 
    {
      comune = anagFacadeClient.getComuneByISTAT(sedeAAEP.getCodComune().getValue());
    }
    
    /*PostaCertificata pecAAEP = (PostaCertificata)session.getAttribute("pecAAEP");
    if(Validator.isNotEmpty(pecAAEP)
       && Validator.isNotEmpty(pecAAEP.getPostaElettronicaCertificata()))
    {
      anagAziendaVO.setPec(pecAAEP.getPostaElettronicaCertificata());
      session.setAttribute("insAnagVO",anagAziendaVO);
    }*/
    
    if(rappresentanteLegaleAAEP != null) 
    {
    	if(rappresentanteLegaleAAEP.getCodiceFiscale() != null 
        && !"".equals(rappresentanteLegaleAAEP.getCodiceFiscale())) 
      {
      	personaFisicaVO.setCodiceFiscale(rappresentanteLegaleAAEP.getCodiceFiscale().getValue());
      }
      if(rappresentanteLegaleAAEP.getCognome() != null 
        && !"".equals(rappresentanteLegaleAAEP.getCognome())) 
      {
      	personaFisicaVO.setCognome(rappresentanteLegaleAAEP.getCognome().getValue());
      }
      if(rappresentanteLegaleAAEP.getNome() != null 
        && !"".equals(rappresentanteLegaleAAEP.getNome())) 
      {
      	personaFisicaVO.setNome(rappresentanteLegaleAAEP.getNome().getValue());
      }
  		if(rappresentanteLegaleAAEP.getSesso() != null && !"".equals(rappresentanteLegaleAAEP.getSesso())) 
      {
    		personaFisicaVO.setSesso(rappresentanteLegaleAAEP.getSesso().getValue());
  		}
  		if(rappresentanteLegaleAAEP.getDataNascita() != null) 
      {
    		personaFisicaVO.setNascitaData(DateUtils.parseDate(rappresentanteLegaleAAEP.getDataNascita().getValue()));
  		}
  		if(rappresentanteLegaleAAEP.getCodComuneNascita() != null 
        && !"".equals(rappresentanteLegaleAAEP.getCodComuneNascita())) 
      {
    		comune = anagFacadeClient.getComuneByISTAT(rappresentanteLegaleAAEP.getCodComuneNascita().getValue());
        //memorizzo il comune restituito da AAEP solo se è valido
        if (comune.getIstatComune()!=null)
          personaFisicaVO.setNascitaComune(rappresentanteLegaleAAEP.getCodComuneNascita().getValue());
    		personaFisicaVO.setNascitaProv(comune.getSiglaProv());
    		personaFisicaVO.setDescNascitaComune(comune.getDescom());
  		}
  		if(rappresentanteLegaleAAEP.getIndirizzo() != null 
        && !"".equals(rappresentanteLegaleAAEP.getIndirizzo())) 
      {
    		personaFisicaVO.setResIndirizzo(rappresentanteLegaleAAEP.getIndirizzo().getValue());
  		}
  		if(rappresentanteLegaleAAEP.getCodComuneResidenza()!= null 
        && !"".equals(rappresentanteLegaleAAEP.getCodComuneResidenza())) 
      {
    		comune = anagFacadeClient.getComuneByISTAT(rappresentanteLegaleAAEP.getCodComuneResidenza().getValue());
        //memorizzo il comune restituito da AAEP solo se è valido
        if (comune.getIstatComune()!=null)
          personaFisicaVO.setResComune(rappresentanteLegaleAAEP.getCodComuneResidenza().getValue());
    		personaFisicaVO.setResProvincia(comune.getSiglaProv());
    		personaFisicaVO.setDescResComune(comune.getDescom());
  		}
  		if(rappresentanteLegaleAAEP.getCap() != null 
        && !"".equals(rappresentanteLegaleAAEP.getCap())) 
      {
    		personaFisicaVO.setResCAP(rappresentanteLegaleAAEP.getCap().getValue());
  		}
    }
    else
    {
      if (anagTribForInsert!=null)
      {
        if(Validator.isNotEmpty(anagTribForInsert.getCodiceFiscaleRappresentante()))
          personaFisicaVO.setCodiceFiscale(anagTribForInsert.getCodiceFiscaleRappresentante());
            
        if(Validator.isNotEmpty(anagTribForInsert.getCognomeRappresentante()))
          personaFisicaVO.setCognome(anagTribForInsert.getCognomeRappresentante());
          
        if(Validator.isNotEmpty(anagTribForInsert.getNomeRappresentante()))
          personaFisicaVO.setNome(anagTribForInsert.getNomeRappresentante());
          
        if(anagTribForInsert.getSesso()!=null) 
          personaFisicaVO.setSesso(anagTribForInsert.getSesso());
          
        if(anagTribForInsert.getDataNascita() != null)
          personaFisicaVO.setNascitaData(DateUtils.parseDate(anagTribForInsert.getDataNascita()));
          
        if(Validator.isNotEmpty(anagTribForInsert.getComuneNascita())) 
        {
          try
          {
            comune = null;
            Vector<ComuneVO> comuni = anagFacadeClient.getComuniLikeProvAndCom(null, anagTribForInsert.getComuneNascita());
            if (comuni!=null && comuni.size()==1) comune=(ComuneVO)comuni.get(0);
            
            if (comune!=null)
            {
              //memorizzo il comune restituito da AT solo se è valido
              if (comune.getIstatComune()!=null)
                personaFisicaVO.setNascitaComune(anagTribForInsert.getComuneNascita());
              personaFisicaVO.setNascitaProv(comune.getSiglaProv());
              personaFisicaVO.setDescNascitaComune(comune.getDescom());
            }
          }
          catch(Exception e)
          {}
        }
         
        if(Validator.isNotEmpty(anagTribForInsert.getIndirizzoResidenza()))  
          personaFisicaVO.setResIndirizzo(anagTribForInsert.getIndirizzoResidenza());
          
        if(Validator.isNotEmpty(anagTribForInsert.getComuneResidenza()))   
        {
          try
          {
            comune = null;
            Vector<ComuneVO> comuni=anagFacadeClient.getComuniLikeProvAndCom(null, anagTribForInsert.getComuneResidenza());
            if (comuni!=null && comuni.size()==1) comune=(ComuneVO)comuni.get(0);
            
            if (comune!=null)
            {
              //memorizzo il comune restituito da AT solo se è valido
              if (comune.getIstatComune()!=null)
                personaFisicaVO.setResComune(anagTribForInsert.getComuneResidenza());
              personaFisicaVO.setResProvincia(comune.getSiglaProv());
              personaFisicaVO.setDescResComune(comune.getDescom());
            }
          }
          catch(Exception e)
          {}
        }
          
        if(Validator.isNotEmpty(anagTribForInsert.getCapResidenza()))    
          personaFisicaVO.setResCAP(anagTribForInsert.getCapResidenza());
            
            
            
        // Recupero solo i primi 100 caratteri dell'indirizzo sede di AAEP
        // e faccio la trim per eliminare gli spazi
        if(Validator.isNotEmpty(anagTribForInsert.getIndirizzoSedeLegale())) 
        {
          if(anagTribForInsert.getIndirizzoSedeLegale().length() > 100) 
          {
            anagAziendaVO.setSedelegIndirizzo(anagTribForInsert.getIndirizzoSedeLegale().substring(0, 100).trim());
          }
          else 
          {
            anagAziendaVO.setSedelegIndirizzo(anagTribForInsert.getIndirizzoSedeLegale().trim());
          }
        }
          
          
        if(Validator.isNotEmpty(anagTribForInsert.getComuneSedeLegale()))   
        {
          try
          {
            comune = null;
            Vector<ComuneVO> comuni=anagFacadeClient
              .getComuniLikeProvAndCom(null, anagTribForInsert.getComuneSedeLegale());
            if (comuni!=null && comuni.size()==1) comune=(ComuneVO)comuni.get(0);
              
            if (comune!=null)
            {
              //memorizzo il comune restituito da AT solo se è valido
              if (comune.getIstatComune()!=null)
                anagAziendaVO.setSedelegComune(anagTribForInsert.getComuneSedeLegale());
              anagAziendaVO.setSedelegProv(comune.getSiglaProv());
              anagAziendaVO.setSedelegComune(comune.getDescom());
            }
          }
          catch(Exception e)
          {}
        }
          
        anagAziendaVO.setSedelegCAP(anagTribForInsert.getCapSedeLegale());

        session.setAttribute("insAnagVO",anagAziendaVO);  
        
      }
    }
    
    session.setAttribute("insPerFisVO",personaFisicaVO);
    if(anagAziendaVO.getTipoFormaGiuridica().getCode()
      .compareTo(SolmrConstants.TIPO_FORMA_GIURIDICA_INDIVIDUALE) == 0 
      ||
      anagAziendaVO.getTipoFormaGiuridica().getCode()
        .compareTo(Integer.decode(SolmrConstants.ID_SOGGETTO_NON_COSTITUITO)) == 0) 
    {
    	// Se non trovo una persona fisica relativa al codice fiscale inserito
    	// lo mando alla pagina di inserimento dei dati del titolare.
    	if(personaFisicaVO.isNewPersonaFisica() 
        && anagAziendaVO.getTipoFormaGiuridica().getCode()
        .equals(SolmrConstants.TIPO_FORMA_GIURIDICA_INDIVIDUALE) 
      ||
        personaFisicaVO.isNewPersonaFisica() && anagAziendaVO.getTipoFormaGiuridica().getCode().equals(Integer.decode(SolmrConstants.ID_SOGGETTO_NON_COSTITUITO))) 
      {
        %>
      		<jsp:forward page="<%= inserimentoTitolareUrl %>"/>
      	<%
      	return;
      }
      // Se esiste già una persona con CUAA uguale a quello indicato dall'utente e la forma giuridica dell' azienda
      // è di tipo individuale lo mando alla pagina di inserimento titolare ma propongo i dati della persona
      // trovati su DB
      else if(!personaFisicaVO.isNewPersonaFisica() 
        && anagAziendaVO.getTipoFormaGiuridica().getCode()
        .equals(SolmrConstants.TIPO_FORMA_GIURIDICA_INDIVIDUALE) 
      || !personaFisicaVO.isNewPersonaFisica() 
        && anagAziendaVO.getTipoFormaGiuridica().getCode()
        .equals(Integer.decode(SolmrConstants.ID_SOGGETTO_NON_COSTITUITO))) 
      {
      	session.setAttribute("modPerFisVO",personaFisicaVO);
      	%>
       		 <jsp:forward page="<%= inserimentoTitolareUrl %>"/>
        <%
        return;
      }
    }
    // Se l'azienda ha forma giuridica diversa da "individuale" allora devo inserire un rappresentante legale
    else 
    {
    	session.setAttribute("codiceFiscale",anagAziendaVO.getCUAA());
    	%>
     		<jsp:forward page="<%= inserimentoRappresentanteLegaleUrl %>"/>
    	<%
    	return;
    }
  }
  else 
  {
   	//session.removeAttribute("insAnagVO");
   	session.removeAttribute("insPerFisVO");
    //session.removeAttribute("insUteVO");
   	session.removeAttribute("modPerFisVO");
  }
  session.removeAttribute("indietro");

	// Se l'utente è un utente provinciale setto il valore della provincia di appartenenza
  // nel VO in modo da proporlo di default nella view
	if(utenteAbilitazioni.getRuolo().isUtenteProvinciale()) 
  {
  	if(anagAziendaVO == null) 
    {
    	anagAziendaVO = new AnagAziendaVO();
  	}
  	//anche qui profiloUtenza.getIdEnte
  	anagAziendaVO.setProvincePiemonte(new Long(utenteAbilitazioni.getEnteAppartenenza().getAmmCompetenza().getIdAmmCompetenza()).toString());
  	String CUUAtemp = request.getParameter("CUAA");
  	if(CUUAtemp != null && !"".equals(CUUAtemp) 
      && (anagAziendaVO.getCUAA() == null 
      || "".equals(anagAziendaVO.getCUAA()))) 
    {
   		anagAziendaVO.setCUAA(CUUAtemp);
  	}
  	session.setAttribute("insAnagVO",anagAziendaVO);
	}

%>
<jsp:forward page="<%= url %>"/>
