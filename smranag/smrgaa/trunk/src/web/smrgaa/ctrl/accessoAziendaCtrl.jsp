
<%@ page import="it.csi.solmr.util.IrideFileParser"%>
<%@ page import="it.csi.solmr.presentation.security.cu.VisualizzaDatiAziendaCU" %>
<%@ page import="it.csi.solmr.dto.anag.AnagAziendaVO" %>
<%@ page import="it.csi.solmr.dto.UtenteIrideVO" %>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient"%>
<%@ page import="it.csi.solmr.util.WebUtils"%>
<%@ page import="it.csi.solmr.util.SolmrLogger" %>
<%@ page import="it.csi.solmr.util.DateUtils" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors"%>
<%@ page import="it.csi.solmr.etc.SolmrConstants"%>
<%@ page import="it.csi.solmr.exception.SolmrException"%>
<%@ page import="it.csi.solmr.dto.anag.sian.SianAnagTributariaVO"%>
<%@ page import="it.csi.solmr.util.ValidationError" %>
<%@ page import="it.csi.solmr.util.ValidationErrors" %>
<%@ page import="it.csi.solmr.presentation.login.IrideRoleSetter"%>
<%@ page import="it.csi.solmr.util.Validator" %>
<%@ page import="it.csi.solmr.util.ProfileUtils" %>
<%@ page import="it.csi.solmr.dto.anag.services.MandatoVO" %>
<%@ page import="it.csi.solmr.dto.anag.services.DelegaAnagrafeVO" %>
<%@ page import="it.csi.solmr.etc.profile.AgriConstants" %>
<%@ page import="it.csi.solmr.dto.iride2.Iride2AbilitazioniVO" %>
<%@ page import="it.csi.solmr.dto.anag.NotificaVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>

<%
{

  String dettaglioURL = "/view/anagraficaView.jsp";
  //String dettaglioCtrl = "/ctrl/anagraficaCtrl.jsp";
  
  final String errMsg = "Impossibile procedere. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
    
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni)session.getAttribute("utenteAbilitazioni");
    
  
     
  ValidationErrors errors = new ValidationErrors();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  
  VisualizzaDatiAziendaCU autorizzazione = (VisualizzaDatiAziendaCU)new IrideFileParser().getMacroCU("VISUALIZZA_DATI_AZIENDA");
  if (autorizzazione==null)
  {
    it.csi.solmr.util.SolmrLogger.debug(this,"[autorizzazione.inc::service] Autorizzazione è null");
    request.setAttribute("errorMessage",it.csi.solmr.etc.anag.AnagErrors.ERRORE_ABILITAZIONE_NO_ABILITAZIONE);
    %><jsp:forward page="<%=it.csi.solmr.etc.SolmrConstants.JSP_ERROR_PAGE%>" /><%
    return;
  }
  
  
  Iride2AbilitazioniVO iride2AbilitazioniVO =
  (Iride2AbilitazioniVO) session.getAttribute("iride2AbilitazioniVO");
  
  Iride2AbilitazioniVO iride2AbilitazioniVORipristino = null;
    
  
  //Controllo per l'elenco soci nel caso di ruolo azienda o titolare legale.
  //Se corrispondono gli idAzienda vuol dire che mi trovo nell'azienda in cui sono
  //titolare, se sono diversi sono in un socio!!!
  if(ruoloUtenza.isUtenteAziendaAgricola() 
    || ruoloUtenza.isUtenteLegaleRappresentante()
    || ruoloUtenza.isUtenteTitolareCf())
  {
  
    //Oggetto settato che mi permette di ripristinare i corretti macro casi d'uso
    //in caso di non autorizzazione all'accesso ai dati dell'azienda!!!
    iride2AbilitazioniVORipristino = iride2AbilitazioniVO;
  
    //Azienda da cui provengo
    // Se arrivo dalla ricerca è null!!
    //AnagAziendaVO anagAziendaVOOldTmp = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  
    Long idAziendaPartenza = (Long)session.getAttribute("idAziendaElencoSoci");
    String idAziendaNuova = (String)request.getAttribute("idAziendaAccesso");
    if((idAziendaPartenza != null) && Validator.isNotEmpty(idAziendaNuova))
    {
      if(idAziendaPartenza.compareTo(new Long(idAziendaNuova)) == 0 && utenteAbilitazioni != null)
      {
        //Ripristino i macro casi d'uso corretti       
        iride2AbilitazioniVO = new Iride2AbilitazioniVO(utenteAbilitazioni.getMacroCU());
        
        session.setAttribute("iride2AbilitazioniVO", iride2AbilitazioniVO);
        
      }
      else //Sono diverse devo limitare gli accessi
      {
        //Aspetto il metodo di stefano!!!!
        /*iride2AbilitazioniVO = IrideRoleSetter.findActorsForPersonaInUseCase(        
        profile.getRuoloUtenza(), "AZIENDA_LIMITATA", anagFacadeClient); */
        iride2AbilitazioniVO = new Iride2AbilitazioniVO(anagFacadeClient.findMacroCUForAttoreInApplication("AZIENDA_LIMITATA"));
        
        
        session.setAttribute("iride2AbilitazioniVO", iride2AbilitazioniVO);
      }    
    }  
  }
  
  //Controllo per l'elenco soci.
  //Se l'utente è intemediario e non ha delega su un figlio
  //deve però poter accedere in sola lettura su esso.
  //Se corrispondono gli idAzienda vuol dire che mi trovo nell'azienda in cui sono
  //titolare, se sono diversi sono in un socio!!!
  if(ruoloUtenza.isUtenteIntermediario())
  { 
  
    //Usato al momento per lo scambio dei MCU Iride nel passaggio
    //Dal "CAA_REALE" (Quello relativo al ruolo) e quello limitato
    //per poter accedere in sola visualizzazione alle aziende socie senza delega
    HashMap<String, Iride2AbilitazioniVO> hIride2Abilitazioni = (HashMap<String, Iride2AbilitazioniVO>)session.getAttribute("hIride2Abilitazioni");
    //Mantiene il diritto "Reale" relativo al ruolo!!! 
    String dirittoRW = (String)session.getAttribute("dirittoRW");
  
    //arrivo dalla ricerca azienda (request.getAttribute("RICERCAJSP") valorizzato!!!
    if((request.getAttribute("RICERCAJSP") != null))
    {
      //sono passato prima in qualche modo
      //nell'elenco soci devo ripristinare i MCU di default
      if(Validator.isNotEmpty(hIride2Abilitazioni))
      {
        //Ho di nuovo la delega sull'azienda corrente      
        ruoloUtenza.setDirittoAccesso(dirittoRW);
        session.setAttribute("iride2AbilitazioniVO", hIride2Abilitazioni.get("CAA_REALE"));
      }
    
    }
    //Arrivo dall'elenco soci
    else
    {
    
      String idAziendaNuova = (String)request.getAttribute("idAziendaAccesso");
      //Controllo l'azienda su cui voglio andare
      //se non ho il mandato devo limitare gli accessi    
      if(Validator.isNotEmpty(idAziendaNuova))
      {  
        boolean hasDelega = true;               
        Long idAzienda = new Long(idAziendaNuova);
        String codiceIntermediario = ruoloUtenza.getCodiceEnte();          
        MandatoVO mandatoVO = anagFacadeClient.serviceGetMandato(idAzienda, codiceIntermediario);
        // Mi estraggo la delega
        DelegaAnagrafeVO delegaAnagrafeVO = mandatoVO.getDelegaAnagrafe();
        if (delegaAnagrafeVO != null)
        {
          // Se esiste una delega devo controllare che il codice dell'ente
          // intermediario a cui appartiene questa delega sia lo stesso dell'ente
          // intemediario dell'utente connesso o che sia stato valorizzato a "S"
          // il
          // FLAG_FIGLIO che è valorizzato in questo modo SE E SOLO SE l'ente che
          // ha
          // la delega è un figlio dell'ente il cui codice fiscale è stato passato
          // al metodo serviceGetMandato
          if (!(codiceIntermediario.equalsIgnoreCase(delegaAnagrafeVO.getCodiceFiscIntermediario()) 
            || "S".equalsIgnoreCase(delegaAnagrafeVO.getFlagFiglio())))
          {
            hasDelega = false;
          }
        }
        else
        {
          // Se non c'è la delega ==> Errore
          hasDelega = false;
        }       
        
        
        if(hasDelega && Validator.isEmpty(hIride2Abilitazioni))
        {
          //Non devo fare nulla poichè ho delega e non 
          //ho mai selezionato un'azienda socia senza mandato 
          //o con mandato altro caa, mantengo i MCU iride originali
        }      
        //Se sono qui vuol dire che in precedenza sono entrato su una
        //azienda socia con mandato diverso da quello del caa corrente 
        else if(hasDelega && Validator.isNotEmpty(hIride2Abilitazioni))
        {          
          //Ho di nuovo la delega sull'azienda corrente      
          ruoloUtenza.setDirittoAccesso(dirittoRW);
          session.setAttribute("iride2AbilitazioniVO", hIride2Abilitazioni.get("CAA_REALE"));
          
        }
        else
        {        
          //Prima volta che visualizzo azienda socia 
          //con mandato diverso da quello del caa corrente
          if(hIride2Abilitazioni == null)
          {
            hIride2Abilitazioni = new HashMap<String, Iride2AbilitazioniVO>(); 
            //Inserisco i MCU reali relativi al ruolo corrente
            hIride2Abilitazioni.put("CAA_REALE", iride2AbilitazioniVO);
            //Inserisco i MCU reali al CAA LIMITATO
            //Aspetto il metodo di stefano!!!!
            /*iride2AbilitazioniVO = IrideRoleSetter.findActorsForPersonaInUseCase(
              profile.getRuoloUtenza(), "CAA_LIMITATO", anagFacadeClient);*/
              
            iride2AbilitazioniVO = new Iride2AbilitazioniVO(anagFacadeClient.findMacroCUForAttoreInApplication("CAA_LIMITATO"));
            hIride2Abilitazioni.put("CAA_LIMITATO", iride2AbilitazioniVO);
        
            session.setAttribute("hIride2Abilitazioni", hIride2Abilitazioni);
            session.setAttribute("dirittoRW", ruoloUtenza.getDirittoAccesso());
          }
        
          //Non ho la delega sull'azienda corrente
          ruoloUtenza.setDirittoAccesso(AgriConstants.LETTURA);
          session.setAttribute("iride2AbilitazioniVO", hIride2Abilitazioni.get("CAA_LIMITATO"));
        }    
      }
    } 
    
    
    
    
  }
  
  
  if (!autorizzazione.isUtenteAbilitato(iride2AbilitazioniVO,ruoloUtenza.isReadWrite()))
  {
    it.csi.solmr.util.SolmrLogger.debug(this,"[autorizzazione.inc::service] utente non abilitato da iride");
    request.setAttribute("errorMessage",it.csi.solmr.etc.anag.AnagErrors.ERRORE_ABILITAZIONE_NO_ABILITAZIONE);
    %><jsp:forward page="<%=it.csi.solmr.etc.SolmrConstants.JSP_ERROR_PAGE%>" /><%
    return;
  }
  
  //Azienda da cui provengo
  // Se arrivo dalla ricerca è null!!
  AnagAziendaVO anagAziendaVOOld = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  
  //Azienda su cui devo andare
  AnagAziendaVO anagAziendaVO = null;
  String idAzienda = (String)request.getAttribute("idAziendaAccesso");
  Vector<Long> vIdAz = new Vector<Long>();
  vIdAz.add(new Long(idAzienda));
  
  Vector<AnagAziendaVO> rangAnagAzienda = null;
  try
  {
    rangAnagAzienda = anagFacadeClient.getListAziendeByIdRangeFromIdAzienda(vIdAz);
    anagAziendaVO = (AnagAziendaVO)rangAnagAzienda.get(0);
    
    if (anagAziendaVO!=null)
    {
      //associo il cuaa di anagrafe tributaria all'azienda...è utilizzato per scoprire se il cuaa presente su
      //anagrafe tributaria è uguale a quello presente in anagrafe
      SianAnagTributariaVO sianAnagTributariaVO = anagFacadeClient.selectDatiAziendaTributaria(anagAziendaVO.getCUAA());
      if (sianAnagTributariaVO !=null) 
        anagAziendaVO.setCUAAAnagrafeTributaria(sianAnagTributariaVO.getCodiceFiscale());
      //Associo i cuaa collegati all'azienda
      anagAziendaVO.setCUAACollegati(anagFacadeClient.getCUAACollegati(anagAziendaVO.getCUAA(), ProfileUtils.getSianUtente(ruoloUtenza)));  
    }
  }
  catch(Exception ex)
  {
    request.setAttribute("errorMessage",errMsg+" "+SolmrLogger.getStackTrace(ex));
    %><jsp:forward page="<%=it.csi.solmr.etc.SolmrConstants.JSP_ERROR_PAGE%>" /><%
    return;
  }
  
  
  //mi srevica per rivara l'idIntermediario ma ora dovrei averlo sempre
  //non occorr epiù ricavarlo..
  /*if (ruoloUtenza.isUtenteIntermediario())
  {
    try
    {
      // Ricerco i dati dell'intermediario
      UtenteIrideVO utenteIrideVO = null;
      utenteIrideVO = anagFacadeClient.getUtenteIrideById(profile.getIdUtente());
      profile.setIdIntermediario(utenteIrideVO.getIdIntermediario());
    }
    catch(SolmrException se) 
    {
      request.setAttribute("errorMessage",errMsg+" "+SolmrLogger.getStackTrace(se) );
      %><jsp:forward page="<%=it.csi.solmr.etc.SolmrConstants.JSP_ERROR_PAGE%>" /><%
      return;
    }
  }*/
  
  if (ruoloUtenza!=null)
  {
    boolean delegaDiretta=false;
    boolean padre=false;
    if (ruoloUtenza.isUtenteIntermediario() && utenteAbilitazioni != null)
    {
      try
      {
        delegaDiretta=anagFacadeClient.isIntermediarioConDelegaDiretta(
          utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getIdIntermediario(),anagAziendaVO.getIdAzienda());
        padre=anagFacadeClient.isIntermediarioPadre(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getIdIntermediario(),
          anagAziendaVO.getIdAzienda());
      }
      catch(SolmrException se) 
      {
        request.setAttribute("errorMessage",errMsg+" "+SolmrLogger.getStackTrace(se));
        %><jsp:forward page="<%=it.csi.solmr.etc.SolmrConstants.JSP_ERROR_PAGE%>" /><%
        return;
      }
    }
    ruoloUtenza.setIsIntermediarioConDelega(delegaDiretta);
    ruoloUtenza.setIsIntermediarioPadre(padre);
    session.setAttribute("ruoloUtenza", ruoloUtenza);
  } 
  
  
  String _errorMessageAut= autorizzazione
    .validate(request,response, ruoloUtenza, new it.csi.solmr.client.anag.AnagFacadeClient(), anagAziendaVO);
  
  
  
  if (_errorMessageAut!=null)
  {
  
    if(ruoloUtenza.isUtenteAziendaAgricola() 
      || ruoloUtenza.isUtenteLegaleRappresentante()
      || ruoloUtenza.isUtenteTitolareCf())
    {
      //se non ho i diritti ad accedere ai dati dell'azienda ripristino i macro casi d'uso iride
      //corretti    
      session.setAttribute("iride2AbilitazioniVO", iride2AbilitazioniVORipristino);
    }
    
    session.setAttribute("anagAziendaVO",anagAziendaVOOld);
    it.csi.solmr.util.SolmrLogger.debug(this,"[autorizzazione.inc::service] utente abilitato da iride ma non dalla competenza del dato");
    String denominazione = "";
    String cuaa = "";
    if(anagAziendaVO !=null)
    {
      denominazione = anagAziendaVO.getDenominazione(); 
      cuaa = anagAziendaVO.getCUAA();
    }
    request.setAttribute("errorMessage"," "+_errorMessageAut +" dell'azienda " +cuaa+ " "+denominazione);
    %><jsp:forward page="<%=it.csi.solmr.etc.SolmrConstants.JSP_ERROR_PAGE%>" /><%
    return;
  }
  else //autenticazione sull'azienda andata a buon fine
  {
    if(anagAziendaVOOld !=null)
    {
      anagAziendaVO.setDataSituazioneAlStr(anagAziendaVOOld.getDataSituazioneAlStr());
    }
    else
    {
      anagAziendaVO.setDataSituazioneAlStr(DateUtils.getCurrentDateString());
    }
    
    WebUtils.removeUselessAttributes(session);
    //aggiungo ricerca associazioni collegate
    Vector<AnagAziendaVO> associazioniCollegateVector = anagFacadeClient.getAssociazioniCollegateByIdAzienda(anagAziendaVO.getIdAzienda(), anagAziendaVO.getDataFineVal());
    request.setAttribute("associazioniCollegateVector",associazioniCollegateVector);
    
    session.setAttribute("anagAziendaVO",anagAziendaVO);
    
    // Se l'azienda selezionata ha e id_azienda_provenienza
    // not null
    if(anagAziendaVO.getIdAziendaProvenienza() != null
                        || anagAziendaVO.getIdAziendaSubentro()!=null) 
    {
      AnagAziendaVO anagAziendaProvenienzaVO = null;
      try 
      {
        if (anagAziendaVO.getIdAziendaProvenienza() != null)
          anagAziendaProvenienzaVO = anagFacadeClient.getAziendaByIdAzienda(anagAziendaVO.getIdAziendaProvenienza());
        else
          anagAziendaProvenienzaVO = anagFacadeClient.getAziendaByIdAzienda(anagAziendaVO.getIdAziendaSubentro());
          
        request.setAttribute("anagAziendaProvenienzaVO", anagAziendaProvenienzaVO);
      }
      catch(SolmrException se) 
      {
        ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_AZIENDA_PROVENIENZA);
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(dettaglioURL).forward(request, response);
        return;
      }
    }
    
    // Recupero le eventuali aziende di destinazione dell'azienda in esame
    AnagAziendaVO[] elencoAziendeDestinazione = null;
    try {
      elencoAziendeDestinazione = anagFacadeClient.getListAnagAziendaDestinazioneByIdAzienda(anagAziendaVO.getIdAzienda(), true, null);
      request.setAttribute("elencoAziendeDestinazione", elencoAziendeDestinazione);
    }
    catch(SolmrException se) 
    {
      ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_AZIENDA_DESTINAZIONE);
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(dettaglioURL).forward(request, response);
      return;
    }
    
    
    
    if((request.getAttribute("RICERCAJSP") != null))
    {
      NotificaVO notificaVO = new NotificaVO();
		  notificaVO.setIdProcedimentoDestinatario((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
		  notificaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
		  
      Vector<NotificaVO> elencoNotifiche = anagFacadeClient.getElencoNotifichePopUp(notificaVO);
      request.setAttribute("elencoNotifiche", elencoNotifiche);
    
    }
    
    
    String iridePageName = "anagraficaCtrl.jsp";
    request.setAttribute("iridePageNameForCU",iridePageName);
    %>
      <jsp:forward page = "<%=dettaglioURL%>" />
    <%
  }
}
%>