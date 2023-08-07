<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.consistenza.TipoControlloVO"%>
<%@ page import="it.csi.smranag.smrgaa.dto.BaseCodeDescription" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.solmr.dto.*" %>

<%

	String iridePageName = "popElencoUvNotificaCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
	
	String erroreViewUrl = "/view/erroreView.jsp";

 	String notificaUrl = "";
 	NotificaVO notificaVO = (NotificaVO)session.getAttribute("notificaModificaVO");
  String urlChiamante = request.getParameter("urlChiamante");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  

 	if(urlChiamante.indexOf("nuovaNotifica") != -1) 
 	{
  	notificaUrl = "/view/nuovaNotificaView.jsp";
 	}
 	else {
  	notificaUrl = "/view/modificaNotificaView.jsp";
 	}

 	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
 	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
 	String messaggioErrore = null;
 	ValidationErrors errors = new ValidationErrors();
 	ValidationError error = null;
 	Vector<StoricoParticellaVO> elencoParticelle = null;
 	if(notificaVO == null) 
 	{
 		elencoParticelle = (Vector<StoricoParticellaVO>)session.getAttribute("elencoParticelle");
 	}
 	else 
 	{
 	  notificaVO.setDescrizione(request.getParameter("descrizione"));
 		// Se la notifica selezionata aveva già delle particelle associate
 		if(notificaVO.getElencoParticelle() != null) 
 		{
 			elencoParticelle = (Vector<StoricoParticellaVO>)notificaVO.getElencoParticelle();
 		}
 		// In caso contrario...
 		else 
 		{
 			//... creo un array di size 0 al quale inserire poi le particelle
 			elencoParticelle = new Vector<StoricoParticellaVO>();   			
 		}
 	}
  
  
  if(urlChiamante.indexOf("nuovaNotifica") != -1) 
  {
    Vector<CodeDescription> vTipologiaNotifica = null;
	  try 
	  {
	    vTipologiaNotifica = anagFacadeClient.getTipologiaNotificaFromRuolo(ruoloUtenza);
	  }
	  catch(SolmrException se) 
	  {
	    error = new ValidationError(AnagErrors.ERRORE_KO_CATEGORIA_NOTIFICA);
	    errors.add("idTipologiaNotifica", error);
	    request.setAttribute("errors", errors);
	    request.getRequestDispatcher(notificaUrl).forward(request, response);
	    return;
	  }
	  // Metto il vettore in request
	  request.setAttribute("vTipologiaNotifica", vTipologiaNotifica);
	}
	  
	if((urlChiamante.indexOf("nuovaNotifica") != -1)
	  || (urlChiamante.indexOf("modificaNotifica") != -1))
  {   
	  Vector<TipoCategoriaNotificaVO> vCategoriaNotifica = null;
	  try 
	  {
	    vCategoriaNotifica = anagFacadeClient.
	      getTipiCategoriaNotificaFromRuolo(ruoloUtenza);
	  }
	  catch(SolmrException se) 
	  {
	    error = new ValidationError(AnagErrors.ERRORE_KO_CATEGORIA_NOTIFICA);
	    errors.add("idCategoriaNotifica", error);
	    request.setAttribute("errors", errors);
	    request.getRequestDispatcher(notificaUrl).forward(request, response);
	    return;
	  }
	  // Metto il vettore in request
	  request.setAttribute("vCategoriaNotifica", vCategoriaNotifica);
	}
	
	
	ConsistenzaVO consistenzaVO = anagFacadeClient.getUltimaDichConsNoCorrettiva(
    anagAziendaVO.getIdAzienda().longValue());
  request.setAttribute("consistenzaVO", consistenzaVO);
  
  if(Validator.isNotEmpty(consistenzaVO))
  {
    // Recupero i valori relativi alla destinazione produttiva
    TipoUtilizzoVO[] elencoTipiUsoSuolo = null;
    try 
    {
      //String[] ordinamento = {(String)SolmrConstants.ORDER_BY_TIPO_UTILIZZO_DESCRIZIONE};
      elencoTipiUsoSuolo = anagFacadeClient.getListTipoUtilizzoByIdDichiarazioneConsistenza(SolmrConstants.TIPO_UTILIZZO_VIGNETO,
        new Long(consistenzaVO.getIdDichiarazioneConsistenza()).longValue());
      request.setAttribute("elencoTipiUsoSuolo", elencoTipiUsoSuolo);
    }
    catch(SolmrException se) 
    {
      error = new ValidationError(AnagErrors.ERRORE_KO_DESTINAZIONE_PRODUTTIVA);
      errors.add("idTipoUtilizzoElenco", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(notificaUrl).forward(request, response);
      return;
    }
    
    // Recupero il vitigno in funzione della destinazione produttiva selezionata
    TipoVarietaVO[] elencoVarieta = null;
    try 
    {
      elencoVarieta = anagFacadeClient.getListTipoVarietaByIdDichiarazioneConsistenza(
        new Long(consistenzaVO.getIdDichiarazioneConsistenza()).longValue());
      request.setAttribute("elencoVarieta", elencoVarieta);
    }
    catch(SolmrException se) 
    {
      error = new ValidationError(AnagErrors.ERRORE_KO_VITIGNO);
      errors.add("idVarieta", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(notificaUrl).forward(request, response);
      return;
    }
    
    
    // Recupero i valori relativi alle provincie su cui insistono le particelle
    Vector<ProvinciaVO> elencoProvincie = null;
    try 
    {
      elencoProvincie = gaaFacadeClient.getListProvincieParticelleByIdDichiarazioneConsistenza(new Long(consistenzaVO.getIdDichiarazioneConsistenza()));
      request.setAttribute("elencoProvincie", elencoProvincie);
    }
    catch(SolmrException se) 
    {
      error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
      errors.add("istatProvinciaConduzioniParticelle", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(notificaUrl).forward(request, response);
      return;
    }
    
    // Recupero i valori relativi ai comuni su cui insistono le particelle
    Vector<ComuneVO> elencoComuni = null;
    try 
    {
      String[] ordinamento = {(String)SolmrConstants.ORDER_BY_DESC_COMUNE};
      elencoComuni = anagFacadeClient.getListComuniParticelleByIdDichiarazioneConsistenza(new Long(consistenzaVO.getIdDichiarazioneConsistenza()), ordinamento);
      request.setAttribute("elencoComuni", elencoComuni);
    }
    catch(SolmrException se) 
    {
      error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
      errors.add("istatComuniConduzioniParticelle", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(notificaUrl).forward(request, response);
      return;
    }
    
    // Recupero i valori relativi alla tipologia Vino
    TipoTipologiaVinoVO[] elencoTipiTipologiaVino = null;
    try 
    {
      TipoTipologiaVinoVO[] elencoTipiVino = null;
      elencoTipiTipologiaVino = anagFacadeClient.getListTipoTipologiaVinoForAzienda(
        new Long(consistenzaVO.getIdDichiarazioneConsistenza()).longValue());
      int dim = elencoTipiTipologiaVino.length;
      
      elencoTipiVino = new TipoTipologiaVinoVO[dim+2];
      TipoTipologiaVinoVO ttvAll = new TipoTipologiaVinoVO();
      TipoTipologiaVinoVO ttvNn = new TipoTipologiaVinoVO();
      ttvAll.setIdTipologiaVino(new Long(-1));
      ttvAll.setDescrizione("qualunque tipologia di vino");
      ttvNn.setIdTipologiaVino(new Long(0));
      ttvNn.setDescrizione("senza tipologia di vino");
      
      elencoTipiVino[0] = ttvAll;
      elencoTipiVino[1] = ttvNn;
      
      for(int i=2; i<dim+2; i++)
        elencoTipiVino[i] = elencoTipiTipologiaVino[i-2];
        
      request.setAttribute("elencoTipiTipologiaVino", elencoTipiVino);
    }
    catch(SolmrException se) 
    {
      error = new ValidationError(AnagErrors.ERRORE_KO_TIPO_TIPOLOGIA_VINO);
      errors.add("idTipologiaVino", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(notificaUrl).forward(request, response);
      return;
    }
    
    
    
    
  }
    
	
 	// Recupero i valori selezionati
 	String[] idStoricoUnitaArboreaSel = request.getParameterValues("idStoricoUnitaArborea");    
 	if(urlChiamante.indexOf("nuovaNotifica") != -1) 
 	{
   	request.setAttribute("idTipologiaNotifica", Long.decode(request.getParameter("idTipologiaNotifica")));
    request.setAttribute("idCategoriaNotifica", Long.decode(request.getParameter("idCategoriaNotifica")));
    request.setAttribute("idTipoEntita", request.getParameter("idTipoEntita"));
 	}
  request.setAttribute("urlChiamante", request.getParameter("urlChiamante"));
  

  Vector<StoricoParticellaVO> temp = new Vector<StoricoParticellaVO>();
  if(elencoParticelle != null && elencoParticelle.size() > 0) 
  {
  	for(int i = 0; i < elencoParticelle.size(); i++) 
  	{
  		temp.add(elencoParticelle.get(i));
  	}
  }
 	// Ricerco i dati richiesti dall'utente
 	boolean isCensite = false;
 	try 
  {
    String idDichiarazioneConsistenzaStr = (String)request.getParameter("idDichiarazioneConsistenza");
    
    long idDichiarazioneConsistenza=-1;
    try
    { 
      idDichiarazioneConsistenza=Long.parseLong(idDichiarazioneConsistenzaStr);
    }
    catch(Exception e){}   
    
  	for(int i = 0; i < idStoricoUnitaArboreaSel.length; i++) 
    {
      StoricoParticellaVO storicoParticellaVO =null;
  		int countParticelleGiaCensite = 0;
      if (idDichiarazioneConsistenzaStr!=null && !idDichiarazioneConsistenzaStr.equals("0"))
      {
        long idUnitaArboreaDichiarata = anagFacadeClient
          .getIdUnitaArboreaDichiarata(Long.parseLong((String)idStoricoUnitaArboreaSel[i]),idDichiarazioneConsistenza);
        storicoParticellaVO = anagFacadeClient.findParticellaArboreaDichiarataBasic(new Long(idUnitaArboreaDichiarata));
      }
      else
      {
        storicoParticellaVO = anagFacadeClient.findStoricoParticellaArboreaBasic(Long.decode(idStoricoUnitaArboreaSel[i]));
      }
   
   		if(temp.size() == 0) 
   		{
     		temp.add(storicoParticellaVO);
   		}
   		else 
   		{
     		for(int j = 0; j < temp.size(); j++) 
     		{
     			StoricoParticellaVO storicoParticellaElencoVO = temp.get(j);
     			if(storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getIdUnitaArborea()
     			  .compareTo(storicoParticellaVO.getStoricoUnitaArboreaVO().getIdUnitaArborea()) == 0) 
     			{
       			countParticelleGiaCensite++;
       			isCensite = true;
       			break;
     			}
     		}
     		if(countParticelleGiaCensite == 0) 
     		{
       		temp.add(storicoParticellaVO);
     		}
   		}
   	}
   	
   	if(Validator.isNotEmpty(temp) && temp.size() > 0)
   	{
   	  Collections.sort(temp, new StoricoParticellaUVComparator());
   	}
 	}
	catch (SolmrException se) 
	{
    SolmrLogger.info(this, " - popElencoUvNotificaCtrl.jsp - FINE PAGINA");
    String messaggio = se.getMessage();
    request.setAttribute("messaggioErrore",messaggio);
    session.setAttribute("chiudi", "chiudi");
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
 	if(notificaVO == null) 
 	{
  	session.setAttribute("elencoParticelle", temp);
 	}
	else 
	{
  	notificaVO.setElencoParticelle(temp);
  	session.setAttribute("notificaModificaVO", notificaVO);
 	}

 	// Comunico l'eventuale selezione di particelle precedentemente selezionate
 	if(isCensite) 
 	{
  	error = new ValidationError(AnagErrors.ERRORE_UV_NOTIFICA_GIA_INSERITO);
   	errors.add("error", error);
   	request.setAttribute("errors", errors);
   	request.getRequestDispatcher(notificaUrl).forward(request, response);
   	return;
 	}

 	// Torno alla pagina di inserimento o modifica
 	%>
  	<jsp:forward page="<%= notificaUrl %>" />
 	
