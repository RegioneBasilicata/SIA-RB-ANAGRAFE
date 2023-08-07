<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.sian.*" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.dto.anag.services.SianAnagTributariaGaaVO" %>
<%@ page import="java.util.Vector" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<%
  String iridePageName = "sianAltriDatiCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%

  String sianAltriDatiUrl = "/view/sianAltriDatiView.jsp";
  String action = "../layout/sianAltriDati.htm";
  String attenderePregoUrl = "/view/attenderePregoView.jsp";
  String anagraficaUrl = "/view/anagraficaView.jsp";
  
  
  String actionUrl = "../layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione sian delle fonti certificate "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  
  
  

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  String messaggioErrore = null;
  ValidationErrors errors = new ValidationErrors();
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  String operazione = request.getParameter("operazione");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  String regimeSianAltriDati = request.getParameter("regimeSianAltriDati");
  String idRegione = SolmrConstants.ID_REG_PIEMONTE;
  ManodoperaVO manodoperaVO = null;
  
  try
  {
    manodoperaVO = anagFacadeClient.findManodoperaAttivaByIdAzienda(anagAziendaVO.getIdAzienda());
    request.setAttribute("manodoperaVO", manodoperaVO);
    if(Validator.isNotEmpty(anagAziendaVO.getCCIAAprovREA()))
    {
      String regione = anagFacadeClient.getRegioneByProvincia(anagAziendaVO.getCCIAAprovREA());
      idRegione = regione;
    }
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - sianAltriDatiCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_DETT_MANODOPERA+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  

  // L'utente ha selezionato la voce di menù anagrafe tributaria e io lo mando alla
  // pagina di attesa per il caricamento dati
  if("attenderePrego".equalsIgnoreCase(operazione)) 
  {
    //prima volta che entro...
    WebUtils.removeUselessFilter(session, null);
    request.setAttribute("action", action);
    operazione = null;
    request.setAttribute("operazione", operazione);
    %>
       <jsp:forward page= "<%= attenderePregoUrl %>" />
    <%
  }
  else 
  {
    
    SianFascicoloResponseVO sianFascicoloResponseVO = (SianFascicoloResponseVO)session.getAttribute("sianFascicoloResponseVO");
    //prima volta che entro....
    SianTrovaFascicoloVO sianTrovaFascicoloVO = null;
    if(Validator.isNotEmpty(sianFascicoloResponseVO))
      sianTrovaFascicoloVO = (SianTrovaFascicoloVO)sianFascicoloResponseVO.getContenuto();
    //prima volta che entro....
    if(Validator.isEmpty(regimeSianAltriDati))
    {
	    session.removeAttribute("sianFascicoloResponseVO");	
	    // Se ha un CUAA valido cerco nell'anagrafe tributaria l'azienda
	    // corrispondente
	    try 
	    {
	      sianFascicoloResponseVO = anagFacadeClient.trovaFascicolo(anagAziendaVO.getCUAA());
	      if(sianFascicoloResponseVO != null)
	      {
		      sianTrovaFascicoloVO = (SianTrovaFascicoloVO)sianFascicoloResponseVO.getContenuto();
		      if(sianTrovaFascicoloVO != null)
		      {
		        StringcodeDescription strDesc = anagFacadeClient.getDescTipoIscrizioneInpsByCodice(sianTrovaFascicoloVO.getCodiceTipoIscrizioneInps());
		        if(Validator.isNotEmpty(strDesc))
		        {
			        String codiceTipoIscrizioneInps = strDesc.getDescription();
			        //transcodifica su tabelle di anagrafe...
			        sianTrovaFascicoloVO.setCodiceTipoIscrizioneInps(codiceTipoIscrizioneInps);
			        //usato il campi con nome che nn c'entra una fava 
			        //per evitare di mettere in sessione un nuovo oggetto!!
			        sianTrovaFascicoloVO.setDetentore(strDesc.getCode());
			      }
		      }
		      
		      session.setAttribute("sianFascicoloResponseVO", sianFascicoloResponseVO);
		    }
	    
	    }
	    catch(SolmrException se) 
	    {
	      SolmrLogger.info(this, " - sianAltriDatiCtrl.jsp - FINE PAGINA");
		    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_RICHIESTA_DATI_FASCICOLO_SIAN+".\n"+se.toString();
		    request.setAttribute("messaggioErrore",messaggio);
		    request.setAttribute("pageBack", actionUrl);
		    %>
		      <jsp:forward page="<%= erroreViewUrl %>" />
		    <%
		    return;
	    }
	  }
	  
	  
	  
	  if(sianTrovaFascicoloVO != null)
	  {
	    //poso importare solo se già preente un record della modopera attivo!!!
	    //Modifica 11/05/2016 Elisa Ravera /terry
	    /*if(Validator.isNotEmpty(manodoperaVO))
	    {
		    if(Validator.isNotEmpty(sianTrovaFascicoloVO.getNumeroIscrizioneInps()))
	        request.setAttribute("numIscrInpsTributariaAtt", "true");
	      if(Validator.isNotEmpty(sianTrovaFascicoloVO.getCodiceTipoIscrizioneInps()))
	        request.setAttribute("tipoIscrInpsTributariaAtt", "true");
	      if(Validator.isNotEmpty(sianTrovaFascicoloVO.getDataInizioIscrizioneInps()))  
	        request.setAttribute("datInInpsTributariaAtt", "true");
	      if(Validator.isNotEmpty(sianTrovaFascicoloVO.getDataFineIscrizioneInps()))
	        request.setAttribute("datFinInpsTributariaAtt", "true");
	    }*/
	    
	    if(Validator.isNotEmpty(sianTrovaFascicoloVO.getNumeroIscrizioneInps()))
        request.setAttribute("numIscrInpsTributariaAtt", "true");
      if(Validator.isNotEmpty(sianTrovaFascicoloVO.getCodiceTipoIscrizioneInps()))
        request.setAttribute("tipoIscrInpsTributariaAtt", "true");
      if(Validator.isNotEmpty(sianTrovaFascicoloVO.getDataInizioIscrizioneInps()))  
        request.setAttribute("datInInpsTributariaAtt", "true");
      if(Validator.isNotEmpty(sianTrovaFascicoloVO.getDataFineIscrizioneInps()))
        request.setAttribute("datFinInpsTributariaAtt", "true");
	  
		  if(!idRegione.equalsIgnoreCase(SolmrConstants.ID_REG_PIEMONTE))
		  {
		    if(Validator.isNotEmpty(sianTrovaFascicoloVO.getNumeroIscrizioneRI()))
          request.setAttribute("numIscrRITributariaAtt", "true");
        if(Validator.isNotEmpty(sianTrovaFascicoloVO.getDataInizioIscrizioneRI()))
          request.setAttribute("datInRITributariaAtt", "true");
        if(Validator.isNotEmpty(sianTrovaFascicoloVO.getDataFineIscrizioneRI()))  
          request.setAttribute("datFinRITributariaAtt", "true");
          
          
        if(Validator.isNotEmpty(sianTrovaFascicoloVO.getNumeroIscrizioneREA()))
          request.setAttribute("numIscrREATributariaAtt", "true");
        if(Validator.isNotEmpty(sianTrovaFascicoloVO.getDataInizioIscrizioneREA()))
          request.setAttribute("datInREATributariaAtt", "true");
        if(Validator.isNotEmpty(sianTrovaFascicoloVO.getDataFineIscrizioneREA()))  
          request.setAttribute("datFinREATributariaAtt", "true");
		  
		  }
		}
	  
	  
	  
	  
	  
	  
	  
	  if("importa".equalsIgnoreCase(operazione))
	  {
	    boolean isNumIscrInpsChecked = request.getParameter("chkNumIscrInps") != null;
	    boolean isTipoIscrInpsChecked = request.getParameter("chkTipoIscrInps") != null;
	    boolean isDatInInpsChecked = request.getParameter("chkDatInInps") != null;
	    boolean isDatFinInpsChecked = request.getParameter("chkDatFinInps") != null;
	    boolean isNumIscrRIChecked = request.getParameter("chkNumIscrRI") != null;
	    boolean isDatInRIChecked = request.getParameter("chkDatInRI") != null;
	    boolean isDatFinRIChecked = request.getParameter("chkDatFinRI")!= null;
	    boolean isNumIscrREAChecked = request.getParameter("chkNumIscrREA") != null;
      boolean isDatInREAChecked = request.getParameter("chkDatInREA") != null;
      boolean isDatFinREAChecked = request.getParameter("chkDatFinREA")!= null;
      
      
      if(!isNumIscrInpsChecked && !isTipoIscrInpsChecked && !isDatInInpsChecked 
        && !isDatFinInpsChecked && !isNumIscrRIChecked && !isDatInRIChecked
        && !isDatFinRIChecked && !isNumIscrREAChecked && !isDatInREAChecked
        && !isDatFinREAChecked)
	    {
	      ValidationError error = new ValidationError((String)AnagErrors.get("ERR_DATI_TRIBUTI_NON_SELEZIONATI"));
	      errors.add("error", error);
	      request.setAttribute("errors", errors);
	      request.getRequestDispatcher(sianAltriDatiUrl).forward(request, response);
	      return;
	    }
	    
	    
	    boolean isNumIscrInpsChanged = false;
      boolean isTipoIscrInpsChanged = false;
      boolean isDatInInpsChanged = false;
      boolean isDatFinInpsChanged = false;
      boolean isNumIscrRIChanged = false;
      boolean isDatInRIChanged = false;
      boolean isDatFinRIChanged = false;
      boolean isNumIscrREAChanged = false;
      boolean isDatInREAChanged = false;
      boolean isDatFinREAChanged = false;
	    
	    ManodoperaVO manodoperaChgVO = new ManodoperaVO(); 
	    if(isNumIscrInpsChecked) 
		  {
		    String numIscrInpsTributaria = request.getParameter("numIscrInpsTributaria");
		    String numIscrInpsTributariaAnagrafe = "";
		    if(Validator.isNotEmpty(manodoperaVO) && Validator.isNotEmpty(manodoperaVO.getCodiceInps()))
		    {
		      numIscrInpsTributariaAnagrafe = manodoperaVO.getCodiceInps();
		    }
		    if(!numIscrInpsTributaria.equalsIgnoreCase(numIscrInpsTributariaAnagrafe)) 
		    {
		      isNumIscrInpsChanged = true;
		      manodoperaChgVO.setCodiceInps(numIscrInpsTributaria);
		    }
		  }
		  
		  if(isTipoIscrInpsChecked) 
      {
        String idTipoIscrInpsTributaria = request.getParameter("idTipoIscrInpsTributaria");
        String idTipoIscrInpsAnagrafe = "";
        if(Validator.isNotEmpty(manodoperaVO)
          && Validator.isNotEmpty(manodoperaVO.getIdTipoIscrizioneINPS()))
        {
          idTipoIscrInpsAnagrafe = manodoperaVO.getIdTipoIscrizioneINPS().toString();
        }
        if(!idTipoIscrInpsTributaria.equalsIgnoreCase(idTipoIscrInpsAnagrafe)) 
        {
          isTipoIscrInpsChanged = true;
          manodoperaChgVO.setIdTipoIscrizioneINPS(new Integer(idTipoIscrInpsTributaria));
        }
      }
      
      if(isDatInInpsChecked) 
      {
        String datInInpsTributaria = request.getParameter("datInInpsTributaria");
        String datInInpsAnagrafe = "";
        if(Validator.isNotEmpty(manodoperaVO)
          && Validator.isNotEmpty(manodoperaVO.getDataInizioIscrizioneDate()))
        {
          datInInpsAnagrafe = DateUtils.formatDateNotNull(manodoperaVO.getDataInizioIscrizioneDate());
        }
        if(!datInInpsTributaria.equalsIgnoreCase(datInInpsAnagrafe)) 
        {       
          isDatInInpsChanged = true;
          manodoperaChgVO.setDataInizioIscrizioneDate(DateUtils.parseDate(datInInpsTributaria));
        }
      }
      
      if(isDatFinInpsChecked) 
      {
        String datFinInpsTributaria = request.getParameter("datFinInpsTributaria");
        String datFinInpsAnagrafe = "";
        if(Validator.isNotEmpty(manodoperaVO)
          && Validator.isNotEmpty(manodoperaVO.getDataCessazioneIscrizioneDate()))
        {
          datFinInpsAnagrafe = DateUtils.formatDateNotNull(manodoperaVO.getDataCessazioneIscrizioneDate());
        }
        if(!datFinInpsTributaria.equalsIgnoreCase(datFinInpsAnagrafe)) 
        {       
          isDatFinInpsChanged = true;
          manodoperaChgVO.setDataCessazioneIscrizioneDate(DateUtils.parseDate(datFinInpsTributaria));
        }
      }
      
      if(isNumIscrRIChecked) 
      {
        String numIscrRITributaria = request.getParameter("numIscrRITributaria");
        if(!numIscrRITributaria.equalsIgnoreCase(anagAziendaVO.getCCIAAnumRegImprese())) 
        {       
          isNumIscrRIChanged = true;
          anagAziendaVO.setCCIAAnumRegImprese(numIscrRITributaria);
        }
      }
      
      if(isDatInRIChecked) 
      {
        String datInRITributaria = request.getParameter("datInRITributaria");
        if(!datInRITributaria.equalsIgnoreCase(DateUtils.formatDateNotNull(anagAziendaVO.getDataIscrizioneRi()))) 
        {      
          isDatInRIChanged = true;
          anagAziendaVO.setDataIscrizioneRi(DateUtils.parseDate(datInRITributaria));
        }
      }
      
      if(isDatFinRIChecked) 
      {
        String datFinRITributaria = request.getParameter("datFinRITributaria");
        if(!datFinRITributaria.equalsIgnoreCase(DateUtils.formatDateNotNull(anagAziendaVO.getDataCessazioneRi()))) 
        {      
          isDatFinRIChanged = true;
          anagAziendaVO.setDataCessazioneRi(DateUtils.parseDate(datFinRITributaria));
        }
      }
      
      if(isNumIscrREAChecked) 
      {
        String numIscrREATributaria = request.getParameter("numIscrREATributaria");
        if(Validator.isEmpty(anagAziendaVO.getCCIAAnumeroREA())  
          || (Validator.isNotEmpty(anagAziendaVO.getCCIAAnumeroREA())
              && !numIscrREATributaria.equalsIgnoreCase(anagAziendaVO.getCCIAAnumeroREA().toString()))) 
        {       
          isNumIscrREAChanged = true;
          anagAziendaVO.setCCIAAnumeroREA(new Long(numIscrREATributaria));
        }
      }
      
      if(isDatInREAChecked) 
      {
        String datInREATributaria = request.getParameter("datInREATributaria");
        if(!datInREATributaria.equalsIgnoreCase(DateUtils.formatDateNotNull(anagAziendaVO.getDataIscrizioneRea()))) 
        {      
          isDatInREAChanged = true;
          anagAziendaVO.setDataIscrizioneRea(DateUtils.parseDate(datInREATributaria));
        }
      }
      
      if(isDatFinREAChecked) 
      {
        String datFinREATributaria = request.getParameter("datFinREATributaria");
        if(!datFinREATributaria.equalsIgnoreCase(DateUtils.formatDateNotNull(anagAziendaVO.getDataCessazioneRea()))) 
        {      
          isDatFinREAChanged = true;
          anagAziendaVO.setDataCessazioneRea(DateUtils.parseDate(datFinREATributaria));
        }
      }
      
      
      
      if(isNumIscrInpsChanged || isTipoIscrInpsChanged 
        || isDatInInpsChanged || isDatFinInpsChanged) 
      {
        try 
        {
          //eliminato controllo che tutti i campi devono essere salvati..
          //12/05/2016
          /*ManodoperaVO manodoperaAttVO = anagFacadeClient.findManodoperaAttivaByIdAzienda(
            anagAziendaVO.getIdAzienda());
          //se non è presente nessun valore attivo su db devono essere selzionati tutti
          //quelli valorizzati dal sian
          if(Validator.isEmpty(manodoperaAttVO))
          {
            if((request.getAttribute("numIscrInpsTributariaAtt") != null
                 && !isNumIscrInpsChecked)
              || (request.getAttribute("tipoIscrInpsTributariaAtt") != null
                 && !isTipoIscrInpsChecked)
              || (request.getAttribute("datInInpsTributariaAtt") != null
                 && !isDatInInpsChecked)
              || (request.getAttribute("datFinInpsTributariaAtt") != null
                 && !isDatFinInpsChecked))
            {            
              messaggioErrore = "Non sono presenti informazioni di manodopera relative all'azienda sul fascicolo, "+
                "è necessario selezionare tutte le informazioni disponibili";
              request.setAttribute("messaggioErrore", messaggioErrore);
              %>
                <jsp:forward page="<%= sianAltriDatiUrl %>"/>
              <%
            }
          }*/
                               
                 
                  
        
          anagFacadeClient.updateManodoperaSian(manodoperaChgVO, anagAziendaVO.getIdAzienda(), 
            ruoloUtenza.getIdUtente());
        }
        catch(SolmrException se) 
        {       
          SolmrLogger.info(this, " - sianAltriDatiCtrl.jsp - FINE PAGINA");
          String messaggio = errMsg+": "+se.toString();
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("pageBack", actionUrl);
          %>
            <jsp:forward page="<%= erroreViewUrl %>" />
          <%
          return;    
        }
      }
      
      
      
      if(isNumIscrRIChanged || isDatInRIChanged || isDatFinRIChanged 
		    || isNumIscrREAChanged || isDatInREAChanged || isDatFinREAChanged) 
		  {
		    // Setto il motivo della modifica
		    anagAziendaVO.setMotivoModifica((String)SolmrConstants.get("SIAN_MOTIVO_MODIFICA"));
		    // Procedo con la storicizzazione
		    try 
		    {
		      anagFacadeClient.updateAnagrafeSemplice(anagAziendaVO, ruoloUtenza.getIdUtente());
		    }
		    catch(SolmrException se) 
		    {       
		      SolmrLogger.info(this, " - sianAltriDatiCtrl.jsp - FINE PAGINA");
		      String messaggio = errMsg+": "+se.toString();
		      request.setAttribute("messaggioErrore",messaggio);
		      request.setAttribute("pageBack", actionUrl);
		      %>
		        <jsp:forward page="<%= erroreViewUrl %>" />
		      <%
		      return;    
		    }
		
		    // Ricerco i dati dell'anagrafe in modo da presentare i dati aggiornati dopo
		    // l'importazione dei dati dal SIAN
		    try 
		    {
		      boolean delegaAttiva=anagAziendaVO.isPossiedeDelegaAttiva();
		      DelegaVO delega=anagAziendaVO.getDelegaVO();
		      anagAziendaVO = anagFacadeClient.getAziendaByIdAzienda(anagAziendaVO.getIdAzienda());
		      anagAziendaVO.setPossiedeDelegaAttiva(delegaAttiva);
		      anagAziendaVO.setDelegaVO(delega);
		    }
		    catch(SolmrException se) 
		    {		       
		      SolmrLogger.info(this, " - sianAltriDatiCtrl.jsp - FINE PAGINA");
			    String messaggio = errMsg+": "+se.getMessage();
			    request.setAttribute("messaggioErrore",messaggio);
			    request.setAttribute("pageBack", actionUrl);
			    %>
			      <jsp:forward page="<%= erroreViewUrl %>" />
			    <%
			    return;
		    }
		
		    // Metto il nuovo oggetto in sessione
		    session.setAttribute("anagAziendaVO", anagAziendaVO);
		
		  }
		 
       
		   		  
	    
	  
	    session.removeAttribute("sianFascicoloResponseVO");
	    %>
        <jsp:forward page="<%= anagraficaUrl %>"/>
      <%
      return;
	  }
       
  }

  %>
   <jsp:forward page="<%= sianAltriDatiUrl %>"/>
  

