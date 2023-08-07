<%@ page language="java" contentType="text/html" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	String iridePageName = "nuovaAziendaSedeCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

	String url = "/view/nuovaAziendaSedeView.jsp";
	String annullaUrl = "/view/nuovaAziendaTitolareView.jsp";
	String annullaUrl2 = "/view/nuovaAziendaRappLegView.jsp";
	String anagraficaUrl = "/view/anagraficaView.jsp";
	//String registrazioneDelegaURL = "/view/registrazioneDelegaView.jsp";
	String indicatoriUrl = "/view/nuovaAziendaIndicatoriView.jsp";

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("insAnagVO");
	
	PersonaFisicaVO personaFisicaVO = (PersonaFisicaVO)session.getAttribute("insPerFisVO");
	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

	String radiobuttonAzienda = (String)request.getAttribute("radiobuttonAzienda");
	request.setAttribute("radiobuttonAzienda", radiobuttonAzienda);
	String cuaaProvenienza = (String)request.getAttribute("cuaaProvenienza");  
	request.setAttribute("cuaaProvenienza", cuaaProvenienza);
  	
	String nascitaStatoEstero = (String)session.getAttribute("nascitaStatoEstero");
	//session.removeAttribute("nascitaStatoEstero");
	// L'utente ha premuto il tasto avanti e procedo verso altre informazioni
	if (request.getParameter("avanti") != null) 
  {
  	// Recupero i parametri
  	String sedeLegaleIndirizzo = request.getParameter("sedelegIndirizzo");
  	String sedeLegaleProvincia = request.getParameter("sedelegProv");
  	String descrizioneSedeComune = request.getParameter("sedelegComune");
  	String sedeLegaleCap = request.getParameter("sedelegCAP");
  	String sedeLegaleEstero = request.getParameter("sedelegEstero");
  	String cittaEsteroSede = request.getParameter("sedelegCittaEstero");
  	String unitaProduttiva = request.getParameter("unitaProduttiva");
  	// risetto i valori del VO
  	anagAziendaVO.setSedelegIndirizzo(sedeLegaleIndirizzo);
  	anagAziendaVO.setSedelegProv(sedeLegaleProvincia);
  	anagAziendaVO.setDescComune(descrizioneSedeComune);
  	anagAziendaVO.setSedelegComune(descrizioneSedeComune);
  	if(Validator.isEmpty(descrizioneSedeComune))
  	{
  	  anagAziendaVO.setSedelegIstatComune(null);
  	  anagAziendaVO.setSedelegComune(null);
  	}
  	anagAziendaVO.setSedelegCAP(sedeLegaleCap);
  	anagAziendaVO.setStatoEstero(sedeLegaleEstero);
  	anagAziendaVO.setSedelegEstero(sedeLegaleEstero);
  	if(Validator.isEmpty(sedeLegaleEstero))
    {
      anagAziendaVO.setSedelegIstatEstero(null);
      anagAziendaVO.setSedelegEstero(null);
    }
  	anagAziendaVO.setSedelegCittaEstero(cittaEsteroSede);
  	anagAziendaVO.setUnitaProduttiva(unitaProduttiva);
  	// Effettuo la validazione formale dei dati.
  	ValidationErrors errors =  anagAziendaVO.validateInsSedeLegale();
  	if(errors != null && errors.size() != 0) 
    {
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(url).forward(request, response);
    	return;
  	}
  	// Ricerco l'istat del comune della sede legale
  	String istatComune = null;
  	String istatStatoEstero = null;
  	try 
    {
    	if(descrizioneSedeComune != null && !descrizioneSedeComune.equals("")) 
      {
      	istatComune = anagFacadeClient.ricercaCodiceComuneNonEstinto(descrizioneSedeComune,sedeLegaleProvincia);
    	}
    	else 
      {
      	istatStatoEstero = anagFacadeClient.ricercaCodiceComune(sedeLegaleEstero,"");
    	}
  	}
  	catch(SolmrException se) 
    {
    	ValidationError error = new ValidationError(se.getMessage());
    	errors.add("error", error);
   	 	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(url).forward(request, response);
    	return;
  	}
  	anagAziendaVO.setSedelegIstatComune(istatComune);
  	anagAziendaVO.setSedelegIstatEstero(istatStatoEstero);
  	String tipiFormaGiuridica = anagFacadeClient.getDescriptionFromCode(SolmrConstants.TAB_TIPO_FORMA_GIURIDICA, anagAziendaVO.getTipoFormaGiuridica().getCode());
  	anagAziendaVO.setTipiFormaGiuridica(tipiFormaGiuridica);
  	anagAziendaVO.setDataSituazioneAlStr(DateUtils.formatDate(new Date(System.currentTimeMillis())));
  	// Controllo nuovamente che non esista già un'azienda valida con la stessa partita iva o CUAA
  	try 
    {
    	Integer idTipoAzienda = null;
    	idTipoAzienda = Integer.decode(anagAziendaVO.getTipiAzienda());
    	// Se è un'azienda provvisoria non devo controllare se il CUAA è già esistente
    	// o meno
    	if(!anagAziendaVO.isFlagAziendaProvvisoria() 
        && anagFacadeClient.isFlagUnivocitaAzienda(idTipoAzienda)) 
      {
      	// L'azienda che si vuole inserire non è provvisoria e presenta il controllo
      	// di univocità quindi procedo con i controlli consueti
      	// Controllo che non esista già in archivio un'azienda valida con il CUAA e la partita IVA indicati
      	anagFacadeClient.checkCUAAandCodFiscale(anagAziendaVO.getCUAA(), anagAziendaVO.getPartitaIVA());
    	}
  	}
  	catch(SolmrException se) 
    {
    	ValidationError error = new ValidationError(se.getMessage());
    	if(se.getMessage().equalsIgnoreCase(AnagErrors.CUAA_GIA_ESISTENTE)) 
      {
      	errors.add("CUAA", error);
    	}
    	else if(se.getMessage().equalsIgnoreCase(AnagErrors.PIVA_GIA_ESISTENTE)) 
      {
      	errors.add("partitaIVA", error);
    	}
    	errors.add("error", error);
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(url).forward(request, response);
    	return;
  	}
  	// Se l'utente ha indicato che la sede legale che sta inserendo è anche
  	// un'unità produttiva
  	UteVO uteVO = null;
  	if(Validator.isNotEmpty(unitaProduttiva) && unitaProduttiva.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
    {
  		uteVO = new UteVO();
  		uteVO.setComune(anagAziendaVO.getSedelegComune());
  		uteVO.setDataInizioAttivita(new java.util.Date(System.currentTimeMillis()));
  		ComuneVO comune = null;
  		try 
      {
    		if(anagAziendaVO.getSedelegComune() != null && !anagAziendaVO.getSedelegComune().equals("")) 
        {
      		comune = anagFacadeClient.getComuneByISTAT(anagAziendaVO.getSedelegIstatComune());
    		}
    		else 
        {
      		comune = anagFacadeClient.getComuneByISTAT(anagAziendaVO.getSedelegIstatEstero());
    		}
  		}
  		catch(SolmrException se) 
      {
    		ValidationError error = new ValidationError(se.getMessage());
    		errors.add("error", error);
    		request.setAttribute("errors", errors);
    		request.getRequestDispatcher(url).forward(request, response);
    		return;
  		}
  		if(comune.getZonaAlt() != null && comune.getZonaAlt().compareTo(new Long(0)) != 0) 
      {
    		uteVO.getTipoZonaAltimetrica().setCode(new Integer(String.valueOf(comune.getZonaAlt().longValue())));
  		}
  	}
  	
  	session.setAttribute("insAnagVO",anagAziendaVO);
  	session.setAttribute("insUteVO", uteVO);
  	
  	
  	
  	%>
      <jsp:forward page="<%= indicatoriUrl %>"/>
    <%
	}
	// L'utente ha premuto il tasto annulla
	if(request.getParameter("indietro") != null) 
  {
  	String sedeLegaleIndirizzo = request.getParameter("sedelegIndirizzo");
    String sedeLegaleProvincia = request.getParameter("sedelegProv");
    String descrizioneSedeComune = request.getParameter("sedelegComune");
    String sedeLegaleCap = request.getParameter("sedelegCAP");
    String sedeLegaleEstero = request.getParameter("sedelegEstero");
    String sedelegCittaEstero = request.getParameter("sedelegCittaEstero");
    String unitaProduttiva = request.getParameter("unitaProduttiva");
    anagAziendaVO.setSedelegIndirizzo(sedeLegaleIndirizzo);
    anagAziendaVO.setSedelegProv(sedeLegaleProvincia);
    anagAziendaVO.setDescComune(descrizioneSedeComune);
    anagAziendaVO.setSedelegComune(descrizioneSedeComune);
    anagAziendaVO.setSedelegCAP(sedeLegaleCap);
    anagAziendaVO.setSedelegEstero(sedeLegaleEstero);
    anagAziendaVO.setSedelegCittaEstero(sedelegCittaEstero);
    anagAziendaVO.setUnitaProduttiva(unitaProduttiva);
    personaFisicaVO.setNascitaStatoEstero(nascitaStatoEstero);
    session.setAttribute("indietro","indietro");
    session.setAttribute("insAnagVO",anagAziendaVO);
  	if(anagAziendaVO.getTipoFormaGiuridica().getCode()
      .equals(SolmrConstants.TIPO_FORMA_GIURIDICA_INDIVIDUALE)) 
    {
    	request.getRequestDispatcher(annullaUrl).forward(request, response);
    	return;
  	}
  	else 
    {
    	request.getRequestDispatcher(annullaUrl2).forward(request, response);
    	return;
  	}
	}
%>
