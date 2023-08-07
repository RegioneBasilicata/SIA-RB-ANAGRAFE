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

	String iridePageName = "popElencoParticelleNotificaCtrl.jsp";
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
 	else 
 	{
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
    
    // Recupero i valori relativi ai tipi uso suolo(attivi e non) utilizzati dall'azienda in esame
    Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = new Vector<TipoUtilizzoVO>();
    try 
    {
      String[] orderBy = {(String)SolmrConstants.ORDER_BY_TIPO_UTILIZZO_DESCRIZIONE};
      elencoTipiUsoSuolo = anagFacadeClient.getListTipiUsoSuoloByIdDichCons(new Long(consistenzaVO.getIdDichiarazioneConsistenza()).longValue(), orderBy);
      request.setAttribute("elencoTipiUsoSuoloPart", elencoTipiUsoSuolo);
    }
    catch(SolmrException se) 
    {
      error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
      errors.add("idTipoUtilizzo", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(notificaUrl).forward(request, response);
      return;
    }
    
    
    //Combo anomalie
    TipoControlloVO[] elencoTipiControllo = null;
    try 
    {
      String[] orderBy = {SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION};
      elencoTipiControllo = anagFacadeClient.getListTipoControlloForAziendaByIdGruppoControlloAndDichCons(
         SolmrConstants.ID_GRUPPO_CONTROLLO_PARTICELLARE, new Long(consistenzaVO.getIdDichiarazioneConsistenza()), orderBy);
      request.setAttribute("elencoTipiControllo", elencoTipiControllo);
    }
    catch(Exception se)
    {
      error = new ValidationError(AnagErrors.ERRORE_KO_ELENCO_ANOMALIE);
      errors.add("idAnomalie", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(notificaUrl).forward(request, response);
      return;
    }
    
    
    
    
  }
    
	
 	// Recupero i valori selezionati
 	String[] idStoricoParticellaSel = request.getParameterValues("idStoricoParticella");    
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
    
  	for(int i = 0; i < idStoricoParticellaSel.length; i++) 
    {
      StoricoParticellaVO storicoParticellaVO =null;
  		int countParticelleGiaCensite = 0;
      storicoParticellaVO = gaaFacadeClient.findStoricoParticellaDichCompleto(
        new Long(idStoricoParticellaSel[i]).longValue(), idDichiarazioneConsistenza);
      
   		if(temp.size() == 0) 
   		{
     		temp.add(storicoParticellaVO);
   		}
   		else 
   		{
     		for(int j = 0; j < temp.size(); j++) 
     		{
     			StoricoParticellaVO storicoParticellaElencoVO = temp.get(j);
     			if(storicoParticellaElencoVO.getIdStoricoParticella()
     			  .compareTo(storicoParticellaVO.getIdStoricoParticella()) == 0) 
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
   	  Collections.sort(temp, new StoricoParticellaComparator());
   	}
 	}
	catch (SolmrException se) 
	{
    SolmrLogger.info(this, " - popElencoParticelleNotificaCtrl.jsp - FINE PAGINA");
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
  	error = new ValidationError(AnagErrors.ERRORE_PARTICELLA_NOTIFICA_GIA_INSERITO);
   	errors.add("error", error);
   	request.setAttribute("errors", errors);
   	request.getRequestDispatcher(notificaUrl).forward(request, response);
   	return;
 	}

 	// Torno alla pagina di inserimento o modifica
 	%>
  	<jsp:forward page="<%= notificaUrl %>" />
 	
