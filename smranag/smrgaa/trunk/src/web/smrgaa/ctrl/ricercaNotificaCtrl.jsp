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
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>

<%

  String iridePageName = "ricercaNotificaCtrl.jsp";
  
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%

  String ricercaNotificaUrl = "/view/ricercaNotificaView.jsp";

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni)session.getAttribute("utenteAbilitazioni");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  
  final String errMsg = "Impossibile procedere nella sezione ricerca notifiche."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

  ValidationErrors errors = new ValidationErrors();
  //Vector<NotificaVO> elencoNotifiche = null;
  ElencoNotificheVO elencoNotificheVO = null;
  Vector<ProvinciaVO> elencoProvincePiemonte = null;  
  
  BigDecimal parametroNotifiRmax = null;
  try 
  {
    parametroNotifiRmax = (BigDecimal)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_NOTIFI_RMAX);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - notificheCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_NOTIFI_RMAX+".\n"+se.toString();   
    ValidationError error = new ValidationError(messaggio);
    errors.add("error",error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(ricercaNotificaUrl).forward(request, response);
    return;
  }

  
  Vector<CodeDescription> vTipologiaNotifica = null;
  try 
  {
    vTipologiaNotifica = anagFacadeClient.getTipiTipologiaNotifica();
  }
  catch(SolmrException se) 
  {
    ValidationError error = new ValidationError(se.getMessage());
    errors.add("error",error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(ricercaNotificaUrl).forward(request, response);
    return;
  }
  // Metto il vettore in request
  request.setAttribute("vTipologiaNotifica", vTipologiaNotifica);
  
  
  Vector<CodeDescription> vCategoriaNotifica = null;
  try 
  {
    vCategoriaNotifica = anagFacadeClient.getCategoriaNotifica();
  }
  catch(SolmrException se) 
  {
    ValidationError error = new ValidationError(se.getMessage());
    errors.add("error",error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(ricercaNotificaUrl).forward(request, response);
    return;
  }
  // Metto il vettore in request
  request.setAttribute("vCategoriaNotifica", vCategoriaNotifica);
  
  
  // Carico la combo con le province del Piemonte più la voce "tutte"
  try 
  {
    elencoProvincePiemonte = anagFacadeClient.getProvinceByRegione(ruoloUtenza.getIstatRegioneAttiva());
    ProvinciaVO provinciaVO = new ProvinciaVO();
    provinciaVO.setIstatProvincia("0");
    provinciaVO.setSiglaProvincia("tutte");
    elencoProvincePiemonte.add(provinciaVO);
  }
  catch(SolmrException se) 
  {
    ValidationError error = new ValidationError(se.getMessage());
    errors.add("error",error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(ricercaNotificaUrl).forward(request, response);
    return;
  }

  // Metto il vettore in request
  request.setAttribute("elencoProvincePiemonte", elencoProvincePiemonte);
  
  
  
  // Recupero il tipo di elenco che vuole visualizzare l'utente
  boolean storico = request.getParameter("storico") != null;

  // Metto il valore in request
  request.setAttribute("storico", new Boolean(storico));


  // L'utente ha premuto il pulsante aggiorna
  if(request.getParameter("aggiorna") != null) 
  {

    // Rimuovo l'indice nel caso fossi già stato nella pagina e avessi proseguito
    // nella navigazione
    session.removeAttribute("indice");

    // Recupero i parametri inseriti dall'utente
    String idTipologiaNotifica = request.getParameter("idTipologiaNotifica");
    String idCategoriaNotifica = request.getParameter("idCategoriaNotifica");
    String istatProvincia = request.getParameter("istatProvincia");
    String dataDal = request.getParameter("dataDal");
    String dataAl = request.getParameter("dataAl");

    // Recupero il VO della ricerca
    NotificaVO notificaRicercaVO = (NotificaVO)session.getAttribute("notificaRicercaVO");
    notificaRicercaVO.setIdTipologiaNotifica(new Long(idTipologiaNotifica));
    if(Validator.isNotEmpty(idCategoriaNotifica))
    {
      notificaRicercaVO.setIdCategoriaNotifica(new Long(idCategoriaNotifica));
    }
    else
    {
      notificaRicercaVO.setIdCategoriaNotifica(null);
    }
    notificaRicercaVO.setIstatProvinciaUtente(istatProvincia);
    notificaRicercaVO.setDataDal(dataDal);
    notificaRicercaVO.setDataAl(dataAl);

    // Effettuo la valorizzazione formale dei dati
    errors = notificaRicercaVO.validateRicercaNotifiche();

    // Se ci sono errori li visualizzo
    if(errors != null && errors.size() > 0) 
    {
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(ricercaNotificaUrl).forward(request, response);
      return;
    }
    

    // Se passo i controlli effettuo nuovamente la ricerca
    try 
    {
      elencoNotificheVO = anagFacadeClient.ricercaNotificheByParametri(notificaRicercaVO, utenteAbilitazioni, ruoloUtenza, new Boolean(storico), parametroNotifiRmax.intValue());
    
      if(Validator.isNotEmpty(elencoNotificheVO.getMessaggioErrore()))
      {
        String messaggio = elencoNotificheVO.getMessaggioErrore();
        request.setAttribute("messaggio", messaggio);      
      }
      // Metto l'oggetto in sessione
      session.setAttribute("notificaRicercaVO", notificaRicercaVO);
    
    }
    catch(SolmrException se) 
    {
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("error",error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(ricercaNotificaUrl).forward(request, response);
      return;
    }

    // Metto il vettore in request
    //request.setAttribute("elencoNotifiche", elencoNotifiche);

   

  }
  // L'utente ha selezionato una delle due frecce per proseguire la navigazione
  // su più pagine
  else if(Validator.isNotEmpty(request.getParameter("operazione"))) 
  {

    // Recupero i filtri di ricerca precedentemente impostati
    NotificaVO notificaRicercaVO = (NotificaVO)session.getAttribute("notificaRicercaVO");

    // Effettuo la ricerca delle notifiche in relazione ai filtri di ricerca
    try 
    {
      elencoNotificheVO = anagFacadeClient.ricercaNotificheByParametri(notificaRicercaVO, utenteAbilitazioni, ruoloUtenza, new Boolean(storico), parametroNotifiRmax.intValue());
      if(Validator.isNotEmpty(elencoNotificheVO.getMessaggioErrore()))
      {
        String messaggio = elencoNotificheVO.getMessaggioErrore();
        request.setAttribute("messaggio", messaggio);
      }
    }
    catch(SolmrException se) 
    {
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("error",error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(ricercaNotificaUrl).forward(request, response);
      return;
    }

    // Metto il vettore in request
    //request.setAttribute("elencoNotifiche", elencoNotifiche);
    notificaRicercaVO.setDataAl(null);

    String indice = request.getParameter("valoreIndice");
    session.setAttribute("indice",indice);
    
  }
  // L'utente ha selezionato la funzione ricerca notifica
  else 
  {

    // Essendo un nodo nuovo di partenza dell'applicativo rimuovo tutti
    // gli oggetti presenti in sessione
    WebUtils.removeUselessAttributes(session);


    // Creo l'oggetto per che conterrà i filtri di default per effettuare la ricerca
    NotificaVO notificaRicercaVO = new NotificaVO();

    // Setto i filtri di default
    //notificaRicercaVO.setTipiTipologiaNotifica(((Long)SolmrConstants.get("ID_TIPO_TIPOLOGIA_BLOCCANTE")).toString());
    notificaRicercaVO.setIdTipologiaNotifica((Long)SolmrConstants.get("ID_TIPO_TIPOLOGIA_BLOCCANTE"));
    // Se l'utente loggato è un funzionario provinciale...
    if(ruoloUtenza.isUtenteProvinciale() || ruoloUtenza.isUtenteRegionale()) 
    {
      // Setto l'ente di appartenenza in questo modo se l'utente ha valorizzato
      // la provincia
      if(Validator.isNotEmpty(ruoloUtenza.getIstatProvincia())) 
      {
        notificaRicercaVO.setIstatProvinciaUtente(ruoloUtenza.getIstatProvincia());
      }
      // Altrimenti metto di default la provincia del capoluogo di regione
      else
      {
        boolean piemonte = SolmrConstants.ID_REG_PIEMONTE
          .equals(ruoloUtenza.getIstatRegioneAttiva());
        if (piemonte)
        {
          notificaRicercaVO.setIstatProvinciaUtente(SolmrConstants.ISTAT_CAPOLUOGO_PIEMONTE);
        }
        else
        {
          notificaRicercaVO.setIstatProvinciaUtente(SolmrConstants.ISTAT_CAPOLUOGO_SARDEGNA);
        }
      }
    }
    // Se al contrario è un intermediario
    else if(ruoloUtenza.isUtenteIntermediario()) 
    {
      notificaRicercaVO.setIstatProvinciaUtente(ruoloUtenza.getIstatProvinciaIntermediario());
    }
    // Per tutti gli altri profili che hanno accesso alla funzionalità seleziono di default la voce "tutte"
    else 
    {
      notificaRicercaVO.setIstatProvinciaUtente("0");
    }
    // Il giorno "dal" deve corrispondere al giorno odierno - 1
    String giorno = DateUtils.getCurrentDay().toString();
    String mese = DateUtils.getCurrentMonth().toString();
    String anno = DateUtils.getCurrentYear().toString();
    Date dataDal = DateUtils.rollDate(DateUtils.parseDate(giorno,mese,anno), Calendar.MONTH, -1);
    notificaRicercaVO.setDataDal(DateUtils.formatDate(dataDal));

    // Metto l'oggetto in sessione
    session.setAttribute("notificaRicercaVO", notificaRicercaVO);

    notificaRicercaVO.setIdProcedimentoDestinatario((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));

    // Effettuo la ricerca delle notifiche in relazione ai filtri di ricerca
    try 
    {
      elencoNotificheVO = anagFacadeClient.ricercaNotificheByParametri(notificaRicercaVO, utenteAbilitazioni, ruoloUtenza, new Boolean(storico), parametroNotifiRmax.intValue());
      if(Validator.isNotEmpty(elencoNotificheVO.getMessaggioErrore()))
      {
        String messaggio = elencoNotificheVO.getMessaggioErrore();
        request.setAttribute("messaggio", messaggio);
      }
    }
    catch(SolmrException se) 
    {
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("error",error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(ricercaNotificaUrl).forward(request, response);
      return;
    }

    // Metto il vettore in request
    //request.setAttribute("elencoNotifiche", elencoNotifiche);
    notificaRicercaVO.setDataAl(null);

    
  }
  
  request.setAttribute("elencoNotifiche", elencoNotificheVO.getvElencoNotifiche());
  session.setAttribute("elencoNotifiche", elencoNotificheVO.getvElencoNotifiche());

%>

  <jsp:forward page= "<%= ricercaNotificaUrl %>" />
