<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.client.profile.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smrcomms.siapcommws.InvioPostaCertificata" %>
<%@ page import="it.csi.smrcomms.siapcommws.DatiMailVO" %>
<%@ page import="it.csi.solmr.dto.comune.AmmCompetenzaVO" %>
<%@ page import="it.csi.solmr.dto.comune.TecnicoAmministrazioneVO" %>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.consistenza.TipoControlloVO"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  String iridePageName = "nuovaNotificaCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
		  
  SolmrLogger.debug(this, "-- BEGIN nuovaNotificaCtrl");
  
  final String errMsg = "Impossibile procedere nella sezione crea una notifica."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  String actionUrl = "../layout/notifiche.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  
  

  String nuovaNotificaUrl = "/view/nuovaNotificaView.jsp";
  String notificheUrl = "/view/notificheView.jsp";
  
  
  //Controllo se sono su un ambiente JBoss 6.4 o un ambiente JBoss WildFly
  ResourceBundle res = ResourceBundle.getBundle("config");
  String ambienteDeploy = res.getString("ambienteDeploy");
  SolmrLogger.debug(this, "-- ambienteDeploy ="+ambienteDeploy);
  String nuovaNotificaConfermaUrl ="";
  if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_CSI))
	  nuovaNotificaConfermaUrl = "../layout/";
  else if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_WILDFLY))
	  nuovaNotificaConfermaUrl = "/layout/";
  nuovaNotificaConfermaUrl += "nuovaNotificaConferma.htm";

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  ValidationErrors errors = new ValidationErrors();
  Vector elencoNotifiche = null;
  String operazione = request.getParameter("operazione");
  String idCategoriaNotifica = request.getParameter("idCategoriaNotifica");
  Vector<StoricoParticellaVO> elencoParticelle = (Vector<StoricoParticellaVO>)session.getAttribute("elencoParticelle");
  
  
  
  
  Vector<CodeDescription> vTipologiaNotifica = null;
  try 
  {
    vTipologiaNotifica = anagFacadeClient.getTipologiaNotificaFromRuolo(ruoloUtenza);
  }
  catch(SolmrException se) 
  {
	SolmrLogger.error(this, "--  Eccezione in getTipologiaNotificaFromRuolo ="+se.getMessage());
			
    ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_TIPOLOGIA_NOTIFICA);
    errors.add("idTipologiaNotifica", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(nuovaNotificaUrl).forward(request, response);
    return;
  }
  // Metto il vettore in request
  request.setAttribute("vTipologiaNotifica", vTipologiaNotifica);
  
  
  Vector<TipoCategoriaNotificaVO> vCategoriaNotifica = null;
  try 
  {
    vCategoriaNotifica = anagFacadeClient.
      getTipiCategoriaNotificaFromRuolo(ruoloUtenza);
  }
  catch(SolmrException se) 
  {
	SolmrLogger.error(this, "--  Eccezione in getTipiCategoriaNotificaFromRuolo ="+se.getMessage());
	
    ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_CATEGORIA_NOTIFICA);
    errors.add("idCategoriaNotifica", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(nuovaNotificaUrl).forward(request, response);
    return;
  }
  // Metto il vettore in request
  request.setAttribute("vCategoriaNotifica", vCategoriaNotifica);
  
  
  String idTipoEntita = "";
  if(Validator.isNotEmpty(operazione)
    && "cambioCategoria".equalsIgnoreCase(operazione))
  {
    for(int i=0; i<vCategoriaNotifica.size(); i++)
    {
      TipoCategoriaNotificaVO tipoCategoriaNotificaVO = vCategoriaNotifica.get(i);      
      if(Validator.isNotEmpty(idCategoriaNotifica) 
        && (idCategoriaNotifica.equalsIgnoreCase(tipoCategoriaNotificaVO.getIdCategoriaNotifica().toString()))) 
      {
        if(Validator.isNotEmpty(tipoCategoriaNotificaVO.getIdTipoEntita()))
        {
	        request.setAttribute("idTipoEntita", tipoCategoriaNotificaVO.getIdTipoEntita());
	        idTipoEntita = tipoCategoriaNotificaVO.getIdTipoEntita().toString();
	      }
        break;       
      }
    }
    session.removeAttribute("elencoParticelle");
    elencoParticelle = null;
  }
  else
  {
    idTipoEntita = request.getParameter("idTipoEntita");
  }
    
  
  
  //entro qui solo se selezionata notifica che prevede inserimento uv
  if(SolmrConstants.TIPO_ENTITA_UV.equalsIgnoreCase(idTipoEntita))
  {
    
    // nn è la prima volta che entro
	  if(Validator.isNotEmpty(elencoParticelle))
	  {
	    
	    String[] arrNoteUv = request.getParameterValues("noteUv"); 
	    for(int i=0;i<elencoParticelle.size();i++)
	    {
	      elencoParticelle.get(i).getUnitaArboreaDichiarataVO().setNote(arrNoteUv[i]);
	    }
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
		    ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_DESTINAZIONE_PRODUTTIVA);
		    errors.add("idTipoUtilizzoElenco", error);
		    request.setAttribute("errors", errors);
		    request.getRequestDispatcher(nuovaNotificaUrl).forward(request, response);
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
		    ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_VITIGNO);
		    errors.add("idVarieta", error);
		    request.setAttribute("errors", errors);
		    request.getRequestDispatcher(nuovaNotificaUrl).forward(request, response);
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
	      ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
	      errors.add("istatProvinciaConduzioniParticelle", error);
	      request.setAttribute("errors", errors);
	      request.getRequestDispatcher(nuovaNotificaUrl).forward(request, response);
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
	      ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
	      errors.add("istatComuniConduzioniParticelle", error);
	      request.setAttribute("errors", errors);
	      request.getRequestDispatcher(nuovaNotificaUrl).forward(request, response);
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
	      ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_TIPO_TIPOLOGIA_VINO);
	      errors.add("idTipologiaVino", error);
	      request.setAttribute("errors", errors);
	      request.getRequestDispatcher(nuovaNotificaUrl).forward(request, response);
	      return;
	    }
		  
		  
		  
		  
		}
  
  }
  //particelle
  else if(SolmrConstants.TIPO_ENTITA_PARTICELLE.equalsIgnoreCase(idTipoEntita))
  {
  
    // nn è la prima volta che entro
    if(Validator.isNotEmpty(elencoParticelle))
    {
      
      String[] arrNoteParticelle = request.getParameterValues("noteParticelle"); 
      for(int i=0;i<elencoParticelle.size();i++)
      {
        elencoParticelle.get(i).setNote(arrNoteParticelle[i]);
      }
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
        ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
        errors.add("istatProvinciaConduzioniParticelle", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(nuovaNotificaUrl).forward(request, response);
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
        ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
        errors.add("istatComuniConduzioniParticelle", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(nuovaNotificaUrl).forward(request, response);
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
		    ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
		    errors.add("idTipoUtilizzo", error);
		    request.setAttribute("errors", errors);
		    request.getRequestDispatcher(nuovaNotificaUrl).forward(request, response);
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
	    catch(SolmrException se)
	    {
	      ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_ELENCO_ANOMALIE);
        errors.add("idAnomalie", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(nuovaNotificaUrl).forward(request, response);
        return;
	    }
		  
		}
  
  }
  
  

  // L'utente ha premuto il pulsante salva
  if(request.getParameter("salva") != null) 
  {

    // Recupero i parametri inseriti dall'utente
    //String idCategoriaNotifica = request.getParameter("idCategoriaNotifica");
    String idTipologiaNotifica = request.getParameter("idTipologiaNotifica");
    String descrizione = request.getParameter("descrizione").trim();


    // Creo il VO e setto i parametri
    NotificaVO nuovaNotificaVO = new NotificaVO();
    nuovaNotificaVO.setTipiCategoriaNotifica(idCategoriaNotifica);
    nuovaNotificaVO.setTipologiaNotifica(idTipologiaNotifica);
    nuovaNotificaVO.setDescrizione(descrizione);
    // Metto l'oggetto in request
    request.setAttribute("nuovaNotificaVO", nuovaNotificaVO);

    // Effettuo i controlli di validazione formale
    errors = nuovaNotificaVO.validateInsertNotifica();

    // Se ci sono errori li visualizzo
    if(errors.size() > 0) 
    {
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(nuovaNotificaUrl).forward(request, response);
      return;
    }

    // Altrimenti procedo con l'inserimento
    nuovaNotificaVO.setIdProcedimentoDestinatario((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
    nuovaNotificaVO.setIdProcedimentoMittente((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
    nuovaNotificaVO.setIdAzienda(anagAziendaVO.getIdAzienda());

    // Se la notifica che si vuole inserire è di tipo bloccante, mando
    // all'utente un messaggio di richiesta conferma operazione
    if(nuovaNotificaVO.getIdTipologiaNotifica()
      .compareTo((Long)SolmrConstants.get("ID_TIPO_TIPOLOGIA_BLOCCANTE")) == 0) 
    {
      request.setAttribute("richiestaConferma", "richiestaConferma");
      %>
        <jsp:forward page= "<%= nuovaNotificaUrl %>" />
      <%
    }
    
    
    
    String parametroInvioMailNotif = "";
    try 
    {
      parametroInvioMailNotif = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_INVIO_MAIL_NOTIF);
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - nuovaNotificaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_INVIO_MAIL_NOTIF+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    SolmrLogger.debug(this, " --- ABILITA MAIL ="+parametroInvioMailNotif);
    
    //questa la ho solo se la notifica nn è bloccante e del tipo....
    elencoParticelle = (Vector<StoricoParticellaVO>)session.getAttribute("elencoParticelle");
    Integer idTipoEntitaIt = null;
    boolean flagInvioMail = false; 
    String descCategoria = "";  
    for(int i=0; i<vCategoriaNotifica.size(); i++)
    {
      TipoCategoriaNotificaVO tipoCategoriaNotificaVO = vCategoriaNotifica.get(i);      
      if(Validator.isNotEmpty(idCategoriaNotifica) 
        && (idCategoriaNotifica.equalsIgnoreCase(tipoCategoriaNotificaVO.getIdCategoriaNotifica().toString()))) 
      {
        if(Validator.isNotEmpty(tipoCategoriaNotificaVO.getIdTipoEntita()))
        {           
          idTipoEntitaIt = tipoCategoriaNotificaVO.getIdTipoEntita();
        }
        if("S".equalsIgnoreCase(tipoCategoriaNotificaVO.getInviaEmail())
          && "S".equalsIgnoreCase(parametroInvioMailNotif))
        {
          flagInvioMail = true;
          descCategoria = tipoCategoriaNotificaVO.getDescrizione();
        }
        break;       
      }
    }
    
    
    Vector<String> vIstatProvincia = new Vector<String>();
    if(Validator.isNotEmpty(elencoParticelle) && (elencoParticelle.size() > 0))
    {   
      Vector<NotificaEntitaVO> vNotificaEntita = new Vector<NotificaEntitaVO>();
      Vector<ValidationErrors> elencoErroriNote = new Vector<ValidationErrors>();
      int countErroriNote = 0;
      
      String idDichiarazioneConsistenza = request.getParameter("idDichiarazioneConsistenza"); 
	    for(int i=0;i<elencoParticelle.size();i++)
	    {
	      NotificaEntitaVO notificaEntitaVO = new NotificaEntitaVO();
	      notificaEntitaVO.setIdTipoEntita(idTipoEntitaIt);
	      if(Validator.isNotEmpty(idDichiarazioneConsistenza))
	        notificaEntitaVO.setIdDichiarazioneConsistenza(new Long(idDichiarazioneConsistenza));
	      ValidationErrors errorsNoteParticelle = new ValidationErrors();
	        
	      if(SolmrConstants.TIPO_ENTITA_UV.equalsIgnoreCase(idTipoEntita))
        {
		      notificaEntitaVO.setIdentificativo(elencoParticelle.get(i).getStoricoUnitaArboreaVO().getIdUnitaArborea());  
	        
	        if(Validator.isNotEmpty(elencoParticelle.get(i).getUnitaArboreaDichiarataVO().getNote()) 
	          && (elencoParticelle.get(i).getUnitaArboreaDichiarataVO().getNote().length() > 2000))
	        {
	          errorsNoteParticelle.add("noteUv", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
	          countErroriNote++;
	        }
	        else
	        {
	          notificaEntitaVO.setNote(elencoParticelle.get(i).getUnitaArboreaDichiarataVO().getNote());
	        }
	      }
	      else if(SolmrConstants.TIPO_ENTITA_PARTICELLE.equalsIgnoreCase(idTipoEntita))
	      {
	        notificaEntitaVO.setIdentificativo(elencoParticelle.get(i).getIdParticella());            
          
          if(Validator.isNotEmpty(elencoParticelle.get(i).getNote()) 
            && (elencoParticelle.get(i).getNote().length() > 2000))
          {
            errorsNoteParticelle.add("noteParticelle", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
            countErroriNote++;
          }
          else
          {
            notificaEntitaVO.setNote(elencoParticelle.get(i).getNote());
          }
	      }
        
        vNotificaEntita.add(notificaEntitaVO);
        
        elencoErroriNote.add(errorsNoteParticelle);
        
        //aggiungo istat prov per la mail...
        if(!vIstatProvincia.contains(elencoParticelle.get(i).getComuneParticellaVO().getIstatProvincia()))
        {
          vIstatProvincia.add(elencoParticelle.get(i).getComuneParticellaVO().getIstatProvincia());
        }              
	    
	    }
	    
	    if(countErroriNote > 0) 
      {
        request.setAttribute("elencoErroriNote", elencoErroriNote);
        request.getRequestDispatcher(nuovaNotificaUrl).forward(request, response);
        return;
      }	    
	    
	    nuovaNotificaVO.setvNotificaEntita(vNotificaEntita);
	  }
	  /*else
	  {
	    if(Validator.isNotEmpty(idTipoEntitaIt))
	    {
	       ValidationError error = null;
	      if(SolmrConstants.TIPO_ENTITA_UV.equalsIgnoreCase(idTipoEntita))
        {
	        error = new ValidationError(AnagErrors.ERR_NO_UV_NOTIFICA);
	      }
	      else
	      {
	        error = new ValidationError(AnagErrors.ERR_NO_PARTICELLE_NOTIFICA);
	      }
	      errors.add("salva", error);
	      request.setAttribute("errors", errors);
	      request.getRequestDispatcher(nuovaNotificaUrl).forward(request, response);
	      return;
	    }
	  }*/

    Long idNotifica = null;
    try 
    {
      idNotifica = anagFacadeClient.insertNotifica(nuovaNotificaVO, ruoloUtenza.getIdUtente());
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - nuovaNotificaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+se.getMessage();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    
    
    session.removeAttribute("notificaVO");
    session.removeAttribute("elencoParticelle");
    
    
    //Invio mail
    if(flagInvioMail)
    {
      try 
      {
        
        // ** Costruisco gli oggetti con i dati per l'invio mail      
        SolmrLogger.debug(this, " -- ** Ricerca dei destinatari delle mail da inviare");
        
        Vector<Long> vIdAmmCompetenza = new Vector<Long>();
        for(int i=0;i<vIstatProvincia.size();i++)
        {
          AmmCompetenzaVO ammCompetenza = anagFacadeClient.serviceFindAmmCompetenzaByCodiceAmm(vIstatProvincia.get(i));
          vIdAmmCompetenza.add(ammCompetenza.getIdAmmCompetenzaLong());
        }
        
        
        Vector<String> destinatariMailA = new Vector<String>(); 
        for(int i=0;i<vIdAmmCompetenza.size();i++)
        {
          TecnicoAmministrazioneVO[] arrTecnico = anagFacadeClient.serviceGetTecnicoAmmByIdAmmCompAndIdProcedimento(
            vIdAmmCompetenza.get(i), new Long(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE));
          if(Validator.isNotEmpty(arrTecnico))
          {
            for(int j=0;j<arrTecnico.length;j++)
            {
              TecnicoAmministrazioneVO tecnico = arrTecnico[j];
              for(int k=0;k<tecnico.getTecnico().getARecapitoTecnicoVO().length;k++)
              {
                //Prendo solo recapito mail
                if("5".equalsIgnoreCase(tecnico.getTecnico().getARecapitoTecnicoVO()[k].getIdTipoRecapito()))
                {
                  destinatariMailA.add(tecnico.getTecnico().getARecapitoTecnicoVO()[k].getRecapito());
                }
              }
            }
          }
        }
        
        String destinatarioMail = "";
        for(int i=0;i<destinatariMailA.size();i++)
        {
          destinatarioMail += destinatariMailA.get(i)+",";
        }
        SolmrLogger.debug(this, " --- sono stati trovati dei DESTINATARIO :"+destinatarioMail);
        
        if(destinatariMailA.size() > 0)
        {
          
	        // ----- Mittente -> VTMA di DB_ALTRI_DATI       
	        String mittente = "";
	        try 
	        {
	          mittente = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_MITT_MAIL_NOTIF);
	        }
	        catch(SolmrException se) 
	        {
	          SolmrLogger.info(this, " - nuovaNotificaCtrl.jsp - FINE PAGINA");
	          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_MITT_MAIL_NOTIF+".\n"+se.toString();
	          request.setAttribute("messaggioErrore",messaggio);
	          request.setAttribute("pageBack", actionUrl);
	          %>
	            <jsp:forward page="<%= erroreViewUrl %>" />
	          <%
	          return;
	        }
	        SolmrLogger.debug(this, " --- MITTENTE ="+mittente);
	        
	        String mittenteReplyTo = "";
	        try 
	        {
	          mittenteReplyTo = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_REPLY_TO_MAIL);
	        }
	        catch(SolmrException se) 
	        {
	          SolmrLogger.info(this, " - newInserimentoConfermaCessazioneCtrl.jsp - FINE PAGINA");
	          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_REPLY_TO_MAIL+".\n"+se.toString();
	          request.setAttribute("messaggioErrore",messaggio);
	          request.setAttribute("pageBack", actionUrl);
	          %>
	            <jsp:forward page="<%= erroreViewUrl %>" />
	          <%
	          return;
	        }
	        SolmrLogger.debug(this, " --- MITTENTE REPLYTO="+mittenteReplyTo);
	            
	        // ---- Oggetto
	        String oggettoMail = "";
	        try 
	        {
	          oggettoMail = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_OGG_MAIL_NOTIF);
	        }
	        catch(SolmrException se) 
	        {
	          SolmrLogger.info(this, " - nuovaNotificaCtrl.jsp - FINE PAGINA");
	          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_OGG_MAIL_NOTIF+".\n"+se.toString();
	          request.setAttribute("messaggioErrore",messaggio);
	          request.setAttribute("pageBack", actionUrl);
	          %>
	            <jsp:forward page="<%= erroreViewUrl %>" />
	          <%
	          return;
	        }
	        
	        if(Validator.isNotEmpty(oggettoMail))
	        { 
	          oggettoMail = oggettoMail.replaceAll("<<CUAA>>",anagAziendaVO.getCUAA());
	          oggettoMail = oggettoMail.replaceAll("<<TIPO_NOTIFICA>>", descCategoria);          
	        }
	          
	        SolmrLogger.debug(this, " --- OGGETTO ="+oggettoMail);
	            
	            
	        // ----- Testo
	        String testo = "";
	        try 
	        {
	          testo = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_MAIL_PER_NOTIF);
	        }
	        catch(SolmrException se) 
	        {
	          SolmrLogger.info(this, " - nuovaNotificaCtrl.jsp - FINE PAGINA");
	          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_MAIL_PER_NOTIF+".\n"+se.toString();
	          request.setAttribute("messaggioErrore",messaggio);
	          request.setAttribute("pageBack", actionUrl);
	          %>
	            <jsp:forward page="<%= erroreViewUrl %>" />
	          <%
	          return;
	        }
	        
	        if(Validator.isNotEmpty(testo))
	        {                   
	          testo = testo.replaceAll("<<DATA_CREAZIONE>>", DateUtils.getCurrent());
	          testo = testo.replaceAll("<<TIPO_NOTIFICA>>", descCategoria);
	          testo = testo.replaceAll("<<CUAA>>",anagAziendaVO.getCUAA());
	          testo = testo.replaceAll("<<DENOMINAZIONE>>",anagAziendaVO.getDenominazione());       
	          
	        }
	        SolmrLogger.debug(this, " --- TESTO ="+testo);
	            
	        // Setto i valori per le mail da inviare
          InvioPostaCertificata invioPosta = new InvioPostaCertificata();
          DatiMailVO[] arrDatiMail = new DatiMailVO[1]; 
          DatiMailVO datiMailVO = new DatiMailVO(); 
          
          String[] arrDestinatari = new String[destinatariMailA.size()];
          for(int k=0;k<destinatariMailA.size();k++)
          {
            arrDestinatari[k] = destinatariMailA.get(k);
          }
          datiMailVO.setDestinatariA(arrDestinatari);
          datiMailVO.setMittente(mittente);
          datiMailVO.setMittenteDisplayName(mittenteReplyTo);
          datiMailVO.setOggetto(oggettoMail);
          datiMailVO.setTesto(testo);
          
          
          arrDatiMail[0] = datiMailVO;
          invioPosta.setInput(arrDatiMail);
            
            
          anagFacadeClient.serviceInviaPostaCertificata(invioPosta);    
	            
	            
	        // Setto i valori per le mail da inviare
	        //destinatariMailA.add(destinatarioMail);           
	        
	        /*DatiMail datiMail = new DatiMail();
	        datiMail.setDestinatariA(destinatariMailA);
	        datiMail.setMittente(mittente);
	        datiMail.setMittenteReplyTo(mittenteReplyTo);           
	        datiMail.setOggetto(oggettoMail);           
	        datiMail.setTesto(testo);           
	        datiMailVect.add(datiMail); 
	                       
	         
	        
	        
	        // Se ci sono destinatari per l'invio mail, invio MAIL
	        SolmrLogger.debug(this, " --- controllo se devono essere inviate delle mail");
	        if(datiMailVect != null && datiMailVect.size()>0)
	        {
	          SolmrLogger.debug(this, " --- ******** INVIO DELLE MAIL dopo la TRASMISSIONE *****-----");
	          SolmrLogger.debug(this, " -- numero di mail da inviare ="+datiMailVect.size());
	          invioMail.sendMail(datiMailVect);     
	                  
	          //messaggio = AgriConstants.MESSAGGIO_INVIO_MAIL_TRASMISSIONE_OK;
	        }*/
	      }
        
      }
      catch (SolmrException e) 
      {
        SolmrLogger.info(this, " - nuovaNotificaCtrl.jsp - FINE PAGINA");
        String messaggio = AnagErrors.ERRORE_KO_INVIO_MAIL_NOTIFICA+".\n"+e.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
    
    
    
    
    
    }
    
    
    
    
    %>
      <jsp:forward page= "<%= nuovaNotificaConfermaUrl %>" >
        <jsp:param name="idNotifica" value="<%=idNotifica.toString() %>"/>
      </jsp:forward>
    <%
    return;

  }
  // L'utente ha selezionato il pulsante annulla
  else if(request.getParameter("annulla") != null) 
  {

    // Effettuo il controllo sullo stato dell'azienda: CONSISTENZA VARIATA
    // E NOTIFICHE. Il controllo va fatto sempre quindi lo inserisco qui
    // in modo che venga sempre effettuato
    try 
    {
      anagFacadeClient.checkStatoAzienda(anagAziendaVO.getIdAzienda());
    }
    catch(SolmrException se) 
    {
      request.setAttribute("statoAzienda", se);
    }

    // Effettuo la ricerca delle notifiche
    // Creo l'oggetto contenente i criteri per effettuare la ricerca
    NotificaVO notificaVO = new NotificaVO();
    notificaVO.setIdProcedimentoDestinatario((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
    notificaVO.setIdAzienda(anagAziendaVO.getIdAzienda());

    try 
    {
      elencoNotifiche = anagFacadeClient.getElencoNotificheByIdAzienda(notificaVO, new Boolean(false), null);
    }
    catch(SolmrException se) 
    {
      if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_NOTIFICHE_ATTIVE_FOR_AZIENDA"))) {
        ValidationError error = new ValidationError(se.getMessage());
        errors.add("error",error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(nuovaNotificaUrl).forward(request, response);
        return;
      }
      else 
      {
        request.setAttribute("messaggio", se.getMessage());
        %>
          <jsp:forward page= "<%= notificheUrl %>" />
        <%
      }
    }

    // Se trovo delle notifiche metto il vettore in request
    request.setAttribute("elencoNotifiche", elencoNotifiche);

    // Vado alla pagina di elenco delle notifiche
    %>
      <jsp:forward page= "<%= notificheUrl %>" />
    <%
  }
  // L'utente ha scelto di proseguire nell'inserimento di una notifica
  // bloccante
  else if(Validator.isNotEmpty(operazione)
    && "richiestaConferma".equalsIgnoreCase(operazione)) 
  {

    // Recupero i parametri inseriti dall'utente
    String strTipiCategoriaNotifica = request.getParameter("idCategoriaNotifica");
    String strTipologiaNotifica = request.getParameter("idTipologiaNotifica");
    String descrizione = request.getParameter("descrizione");

    // Creo il VO e setto i parametri
    NotificaVO nuovaNotificaVO = new NotificaVO();
    nuovaNotificaVO.setTipiCategoriaNotifica(strTipiCategoriaNotifica);
    nuovaNotificaVO.setTipologiaNotifica(strTipologiaNotifica);
    nuovaNotificaVO.setDescrizione(descrizione);

    // Metto l'oggetto in request
    request.setAttribute("nuovaNotificaVO", nuovaNotificaVO);

    // Effettuo i controlli di validazione formale
    errors = nuovaNotificaVO.validateInsertNotifica();

    // Se ci sono errori li visualizzo
    if(errors.size() > 0) 
    {
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(nuovaNotificaUrl).forward(request, response);
      return;
    }
    
    
    //nuovaNotificaVO.setIdTipologiaNotifica(anagFacadeClient
      //.getIdTipologiaNotificaFromCategoria(nuovaNotificaVO.getIdCategoriaNotifica()));

    nuovaNotificaVO.setIdProcedimentoDestinatario((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
    nuovaNotificaVO.setIdProcedimentoMittente((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
    nuovaNotificaVO.setIdAzienda(anagAziendaVO.getIdAzienda());

    Long idNotifica = null;
    try 
    {
      idNotifica = anagFacadeClient.insertNotifica(nuovaNotificaVO, ruoloUtenza.getIdUtente());
    }
    catch(SolmrException se) 
    {
    
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("error",error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(nuovaNotificaUrl).forward(request, response);
      return;
    }
    
    
    session.removeAttribute("notificaVO");
    session.removeAttribute("elencoParticelle");
    %>
      <jsp:forward page= "<%= nuovaNotificaConfermaUrl %>" >
        <jsp:param name="idNotifica" value="<%=idNotifica.toString() %>"/>
      </jsp:forward>
    <%
    return;

  }
  // E'stato selezionato il pulsante "elimina" relativo all'elenco delle particelle
  else if(request.getParameter("eliminaUv") != null) 
  {
    // Recupero gli elementi da eliminare
    String[] elementsToRemove = request.getParameterValues("idStoricoUnitaArborea");
    Vector<StoricoParticellaVO> temp = null;
    // Controllo che sia stato selezionato qualcosa dall'utente
    if(elementsToRemove == null || elementsToRemove.length == 0) 
    {
      errors.add("error", new ValidationError(AnagErrors.ERRORE_UV_NOTIFICA_ELIMA_KO));
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(nuovaNotificaUrl).forward(request, response);
      return;
    }
    // Se sì, elimino gli elementi selezionati
    else 
    {
      if(elementsToRemove.length == elencoParticelle.size()) 
      {
        session.removeAttribute("elencoParticelle");
      }
      else 
      {
        Hashtable<Long,StoricoParticellaVO> hash = new Hashtable<Long,StoricoParticellaVO>();
        for(int i =0; i < elencoParticelle.size(); i++) 
        {
          StoricoParticellaVO storicoParticellaVO = elencoParticelle.get(i);
          hash.put(storicoParticellaVO.getUnitaArboreaDichiarataVO().getIdStoricoUnitaArborea(), storicoParticellaVO);
        }
        for(int j = 0; j < elementsToRemove.length; j++) 
        {
          hash.remove(Long.decode((String)elementsToRemove[j]));
        }
        temp = new Vector<StoricoParticellaVO>();
        Enumeration<StoricoParticellaVO> enumeration = hash.elements();
        while(enumeration.hasMoreElements()) 
        {
          temp.add(enumeration.nextElement());
        }
      }
    }
    // Metto il vettore in sessione
    if(temp != null && temp.size() > 0) 
    {
      Collections.sort(temp, new StoricoParticellaUVComparator());
      session.setAttribute("elencoParticelle", temp);
    }
    // Torno alla pagina di inserimento
    %>
        <jsp:forward page="<%= nuovaNotificaUrl %>" />
    <%
  }
  // E'stato selezionato il pulsante "elimina" relativo all'elenco delle particelle
  else if(request.getParameter("eliminaParticelle") != null) 
  {
    // Recupero gli elementi da eliminare
    String[] elementsToRemove = request.getParameterValues("idStoricoParticella");
    Vector<StoricoParticellaVO> temp = null;
    // Controllo che sia stato selezionato qualcosa dall'utente
    if(elementsToRemove == null || elementsToRemove.length == 0) 
    {
      errors.add("error", new ValidationError(AnagErrors.ERRORE_PARTICELLE_NOTIFICA_ELIM_KO));
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(nuovaNotificaUrl).forward(request, response);
      return;
    }
    // Se sì, elimino gli elementi selezionati
    else 
    {
      if(elementsToRemove.length == elencoParticelle.size()) 
      {
        session.removeAttribute("elencoParticelle");
      }
      else 
      {
        Hashtable<Long,StoricoParticellaVO> hash = new Hashtable<Long,StoricoParticellaVO>();
        for(int i =0; i < elencoParticelle.size(); i++) 
        {
          StoricoParticellaVO storicoParticellaVO = elencoParticelle.get(i);
          hash.put(storicoParticellaVO.getIdStoricoParticella(), storicoParticellaVO);
        }
        for(int j = 0; j < elementsToRemove.length; j++) 
        {
          hash.remove(Long.decode((String)elementsToRemove[j]));
        }
        temp = new Vector<StoricoParticellaVO>();
        Enumeration<StoricoParticellaVO> enumeration = hash.elements();
        while(enumeration.hasMoreElements()) 
        {
          temp.add(enumeration.nextElement());
        }
      }
    }
    // Metto il vettore in sessione
    if(temp != null && temp.size() > 0) 
    {
      Collections.sort(temp, new StoricoParticellaComparator());
      session.setAttribute("elencoParticelle", temp);
    }
    // Torno alla pagina di inserimento
    %>
        <jsp:forward page="<%= nuovaNotificaUrl %>" />
    <%
  }
  // L'utente ha selezionato la funzione inserisci notifica
  else 
  {

    // Vado alla pagina di inserimento della notifica
    %>
      <jsp:forward page= "<%= nuovaNotificaUrl %>" />
    <%
  }

%>
