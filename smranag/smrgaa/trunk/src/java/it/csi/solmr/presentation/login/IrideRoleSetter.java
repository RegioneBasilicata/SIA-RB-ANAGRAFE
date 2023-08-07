package it.csi.solmr.presentation.login;

import it.csi.iride2.policy.entity.Identita;
import it.csi.papua.papuaserv.dto.legacy.axis.RuoloUtenzaPapua;
import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.smrcomms.rpparser.util.Validator;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.iride2.Iride2AbilitazioniVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.profile.AgriConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.SolmrLogger;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * Title: SMRGAA
 * </p>
 * <p>
 * Description: SMRGAA
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: TOBECONFIG
 * </p>
 * 
 * @author TOBECONFIG
 * @version 1.0
 */

public class IrideRoleSetter extends HttpServlet
{
  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -1533171252690506205L;

  private static final String MONITORAGGIO_HOME = "../layout/monitoraggioswhttp.htm";
  /*private static final String APPLICATIVO_HOME = "../layout/indexswhttp.htm";
  private static final String APPLICATIVO_HOME_AZIENDA = "../layout/indexAziendaswhttp.htm";
  private static final String APPLICATIVO_HOME_PAGE = "../layout/indexswhttpHome.htm";*/
  private static final String APPLICATIVO_HOME_MSG = "../layout/messaggi_utente_login.htm";

  /**
   * Metodo di gestione delle richieste http del servlet
   * 
   * @param request
   *          HttpServletRequest
   * @param response
   *          HttpServletResponse
   * @throws ServletException
   * @throws IOException
   */
  public void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
  {
	SolmrLogger.debug(this," IrideRoleSetter service BEGIN");
    String messaggioErrore = null;
    HttpSession session = null;
    UtenteAbilitazioni utenteAbilitazioni = null;
    AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

    try
    {

      session = request.getSession(false);
      SolmrLogger.debug(this,"-- request.getSession");
      //String appName = SolmrConstants.APP_NAME_IRIDE2_SMRGAA;

      
      //String codiceFiscale = (String) session.getAttribute("codiceFiscale");
      if(session == null)
    	  SolmrLogger.debug(this,"-- session == null");
      String codiceRuolo = (String) session.getAttribute("codiceRuolo");
      SolmrLogger.debug(this,"-- codiceRuolo in sessione ="+codiceRuolo);
      
      //String idProcedimento = new Integer(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE).toString();
      //String denominazione = (String) session.getAttribute("denominazione");
      //Hashtable<?,?> htIride2 = (Hashtable<?,?>) session.getAttribute("htIride2");
      //String ruoloSelezionato = (String) session.getAttribute("ruoloSelezionato");

      
      // Controlli di verifica
      //codiceFiscale = (codiceFiscale == null) ? "" : codiceFiscale;
      //codiceRuolo = (codiceRuolo == null) ? "" : codiceRuolo;

      
      //denominazione = (denominazione == null) ? "" : denominazione;

      
      // Log Profilo
      /*UtenteIride2VO utenteIride2VO = new UtenteIride2VO();
      utenteIride2VO.setCodiceFiscale(codiceFiscale);
      utenteIride2VO.setCodiceRuolo(codiceRuolo);

      utenteIride2VO.setIdProcedimento(new Long(idProcedimento));
      utenteIride2VO.setDenominazione(denominazione);

      utenteIride2VO = loadUtenteIride2VO(htIride2, utenteIride2VO);

      messaggioErrore = AgriConstants.UTENTE_NON_ABILITATO_PROCEDIMENTO;

      // todo
      Boolean isUtenteAbilitatoProcedimento = null;
      try
      {
        anagFacadeClient = new AnagFacadeClient();
        TipoProcedimentoVO tipoProcedimentoVO = anagFacadeClient
            .serviceFindTipoProcedimentoByDescrizioneProcedimento(appName);

        if (!(codiceRuolo.toString().equalsIgnoreCase(
            new String(AgriConstants.TITOLARE_CF)) || codiceRuolo.toString()
            .equalsIgnoreCase(new String(AgriConstants.LEGALE_RAPPRESENTANTE))))
        {
          // Se codiceRuolo diverso da TITOLARE_CF e LEGALE_RAPPRESENTANTE
          // allora verifico su DB_TIPO_PROCEDIMENTO.FLAG_CONTROLLI_ABILITAZIONI
          // se il controllo sulle abilitazioni configurate in SMRCOMUNE va
          // effettuato oppure no
          if (tipoProcedimentoVO != null
              && tipoProcedimentoVO.getFlagControlloUtenteAbilitazioni() != null
              && tipoProcedimentoVO.getFlagControlloUtenteAbilitazioni()
                  .equalsIgnoreCase(
                      (String) AgriConstants
                          .get("FLAG_CONTROLLI_ABILITAZIONI_ATTIVO")))
          {
            // Se il controllo va effettuato allora lo effettuo
            isUtenteAbilitatoProcedimento = anagFacadeClient
                .isUtenteAbilitatoProcedimento(utenteIride2VO);
          }
          else
          {
            // Se il controllo non va effettuato allora non lo effettuo
            isUtenteAbilitatoProcedimento = new Boolean(true);
          }
        }
        else
        {
          // Se codiceRuolo uguale a TITOLARE_CF o LEGALE_RAPPRESENTANTE
          // allora non effettuo il controllo
          isUtenteAbilitatoProcedimento = new Boolean(true);
        }

      }
      catch (RuntimeException ruExc)
      {
        SolmrLogger.dumpStackTrace(this,
            "[IrideRoleSetter:service] RuntimeException codice #1", ruExc);
        messaggioErrore = AgriConstants.PROBLEMI_ACCESSO_COMUNE;
        throw new SolmrException(messaggioErrore);
      }

      if (!isUtenteAbilitatoProcedimento.booleanValue())
      {
        // todo
        throw new SolmrException(messaggioErrore);
      }*/

      /*try
      {
        anagFacadeClient.writeAccessLogUser(utenteIride2VO);
      }
      catch (RuntimeException ruExc)
      {
        SolmrLogger.dumpStackTrace(this,
            "[IrideRoleSetter:service] RuntimeException codice #2", ruExc);
        messaggioErrore = AgriConstants.PROBLEMI_ACCESSO_COMUNE;
        throw new SolmrException(messaggioErrore);
      }*/

      //qui devo chiamare il metodo nuovo di stefano!!!!
      Identita id = null;

      if (session.getAttribute(SolmrConstants.IDENTITA) != null)
      {
        id = (it.csi.iride2.policy.entity.Identita) session
            .getAttribute(SolmrConstants.IDENTITA);
      }
      //non dovrebbe mai succedere però segnaliamo!!
      else
      {
    	SolmrLogger.error(this, "--  Oggetto identità NULL!!! --");
        throw new SolmrException("Oggetto identità NULL!!!");        
      }
      
      try
      {
    	SolmrLogger.debug(this, "-- prima di loginPapua");
    	SolmrLogger.debug(this, "-- codFiscale ="+id.getCodFiscale()); 
    	SolmrLogger.debug(this, "-- cognome ="+id.getCognome());
    	SolmrLogger.debug(this, "-- nome ="+id.getNome());
    	SolmrLogger.debug(this, "-- livelloAutenticazione ="+id.getLivelloAutenticazione());
    	SolmrLogger.debug(this, "-- codiceRuolo ="+codiceRuolo);
    	
        utenteAbilitazioni = anagFacadeClient.loginPapua(id.getCodFiscale(), 
            id.getCognome(), id.getNome(), id.getLivelloAutenticazione(), codiceRuolo);
       
        SolmrLogger.debug(this, "-- dopo di loginPapua");
      }
      catch (SolmrException se)
      {
        messaggioErrore = se.getMessage();
        SolmrLogger.dumpStackTrace(this,
            "[IrideRoleSetter:service] SolmrException in loginPapua", se);
        throw se;
      }
      
      SolmrLogger.debug(this, "-- richiamo il costruttore di RuoloUtenzaPapua passando l'oggetto utenteAbilitazioni");
      RuoloUtenzaPapua ruoloUtenza = new RuoloUtenzaPapua(utenteAbilitazioni);
      SolmrLogger.debug(this, "-- dopo il il costruttore di RuoloUtenzaPapua passando l'oggetto utenteAbilitazioni");

      ruoloUtenza.setRuoloSelezionato(codiceRuolo);      

      ruoloUtenza.setIdentita(id);

      ruoloUtenza.setIstatRegioneAttiva(
          SolmrConstants.ISTAT_REGIONE_ATTIVA);
   
           
      // carico i dati relativi al tipo gruppo ruolo dell'utente: questo
      // caricamento
      // andrebbe fatto in comune ma per ora lo facciamo in anagrafe
      ruoloUtenza.setTipoGruppoRuolo(
          anagFacadeClient.getGruppoRuolo(ruoloUtenza
              .getRuoloSelezionato()));
      ruoloUtenza.setIdProcedimentoOriginale(new Long(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE));

      // 050718 - Caricamento Identita - End
      session = request.getSession(true);
      //session.setAttribute("profilo", pu);
      session.setAttribute("utenteAbilitazioni", utenteAbilitazioni);
      SolmrLogger.debug(this, "-- metto in sessione RuoloUtenza");      
      session.setAttribute("ruoloUtenza", ruoloUtenza);
      
      // Controllo le abilitazioni di iride
      Iride2AbilitazioniVO iride2AbilitazioniVO = new Iride2AbilitazioniVO(utenteAbilitazioni.getMacroCU());
      SolmrLogger.debug(this, " ---- metto in sessione iride2AbilitazioniVO");
      session.setAttribute("iride2AbilitazioniVO", iride2AbilitazioniVO);
      
      
      LoggerUtils loggUt = new LoggerUtils();
      loggUt.dumpHttpSessionAttributes(session);
      
      // Carico gli attori per il ruolo scelto.
      //Per questo uso ruolo/attore sono 1/1
      //quindi carico solo l'attore
      /*Actor[] actor = anagFacadeClient
          .findActorsForPersonaInApplication(id);
      if(Validator.isNotEmpty(actor))
      {
        Vector<String> vActor = new Vector<String>();
        for(int i=0;i<actor.length;i++)
        {
          vActor.add(actor[i].getId());
        }
        session.setAttribute("vActor", vActor);
      }*/
      
      
      if(Validator.isNotEmpty(utenteAbilitazioni.getAttori()))
      {
        Vector<String> vActor = new Vector<String>();
        for(int i=0;i<utenteAbilitazioni.getAttori().length;i++)
        {
          vActor.add(utenteAbilitazioni.getAttori()[i].getCodice());
        }
        session.setAttribute("vActor", vActor);
      }
      
     
      // Se l'utente è quello di monitoraggio devo mandarlo su di una pagina
      // apposta
      String homePage = APPLICATIVO_HOME_MSG;
      if (codiceRuolo.toString().equalsIgnoreCase(
          (String) AgriConstants.get("MONITORAGGIO")))
        homePage = MONITORAGGIO_HOME;
      

      // 050426 - jsessionid - Begin
      String homePageSessionId = null;
      String postSwitchHttp = ";jsessionid=" + session.getId();
      homePageSessionId = homePage + postSwitchHttp;

      // 050426 - jsessionid - End
      

      // Inserisco in sessione le informazioni sul path dell'immagine ko.gif
      // per il portale scelto
      /*
       * String portalName=(String)session.getAttribute("PORTAL_NAME");
       * 
       * if (portalName!=null &&
       * portalName.equals(SolmrConstants.NOME_PORTALE_SISPIE)) {
       * session.setAttribute("pathToFollow","sispie"); } else { if
       * (portalName!=null &&
       * portalName.equals(SolmrConstants.NOME_PORTALE_RUPAR)) {
       * session.setAttribute("pathToFollow", "rupar"); } else{ if
       * (portalName!=null &&
       * portalName.equals(SolmrConstants.NOME_PORTALE_RAS)) {
       * session.setAttribute("pathToFollow", "ras"); } } }
       */

      // #############################################

      response.sendRedirect(homePageSessionId);
      return;
    }
    /*catch (UserNotFoundException uex)
    {
      SolmrLogger.dumpStackTrace(this, "[IrideRoleSetter:service] UserNotFoundException (Utente non trovato nella view) ", uex);

      //setAttribute di URL_ACCESS_POINT non esiste da nessuna parte! 
      //String urlLoginWhithMessageError = (String) session.getAttribute("URL_ACCESS_POINT");
      String urlLoginWhithMessageError = "../view/erroreView.jsp";

      if (urlLoginWhithMessageError.indexOf("?") < 0)
      {
        urlLoginWhithMessageError += "?";
      }
      else
      {
        urlLoginWhithMessageError += "&";
      }

      messaggioErrore = AgriConstants.UTENTE_NON_PRESENTE_SU_VISTA_UTENTE_IRIDE;

      urlLoginWhithMessageError += "messaggioErrore=" + messaggioErrore;

      session.setAttribute("pageBack","../view/sceltaRuoloPEP.jsp");
      response.sendRedirect(urlLoginWhithMessageError);
      return;
    }*/
    catch (Exception ex)
    {
      SolmrLogger.dumpStackTrace(this,
          "[IrideRoleSetter:service] Exception (Eccezione nell'accesso ad "
              + "Iride o Comune o Autenticazione ) codice #4", ex);
              
      /* setAttribute di URL_ACCESS_POINT non esiste da nessuna parte! */
      //String urlLoginWhithMessageError = (String) session.getAttribute("URL_ACCESS_POINT");
      String urlLoginWhithMessageError = "../view/erroreView.jsp";

      if (urlLoginWhithMessageError.indexOf("?") < 0)
      {
        urlLoginWhithMessageError += "?";
      }
      else
      {
        urlLoginWhithMessageError += "&";
      }

      urlLoginWhithMessageError += "messaggioErrore=" + messaggioErrore;

      session.setAttribute("pageBack","../view/sceltaRuoloPEP.jsp");
      response.sendRedirect(urlLoginWhithMessageError);
    }
  }

  /*private UtenteIride2VO loadUtenteIride2VO(Hashtable<?,?> htIride2,
      UtenteIride2VO utenteIride2VO)
  {
    String ruolo = (String) htIride2.get(AgriConstants.INFO_PERSONA_RUOLO);
    String ente = (String) htIride2.get(AgriConstants.INFO_PERSONA_ENTE);

    String appName = "";

    appName = SolmrConstants.APP_NAME_IRIDE2_SMRGAA;

    utenteIride2VO.setDirittoAccesso((String) htIride2.get(appName));

    utenteIride2VO.setCodiceEnte(ente);
    utenteIride2VO.setCodiceRuolo(ruolo);

    return utenteIride2VO;
  }*/

  public String leggiCookie(HttpServletRequest request, String keyCookie)
  {
    String pageNameLogin = null;
    Cookie[] cookies = request.getCookies();

    if (cookies != null)
    {
      for (int i = 0; i < cookies.length; i++)
      {
        if (cookies[i].getName().equalsIgnoreCase(keyCookie))
        {
          pageNameLogin = cookies[i].getValue();

          break;
        }
      }
    }

    return pageNameLogin;
  }

  /*public static Iride2AbilitazioniVO findUseCaseByPersonaAndRuolo(
      HttpServletRequest request, RuoloUtenza ruoloUtenzaVO,
      AnagFacadeClient anagFacadeClient) throws SolmrException
  {
    try
    {
      String appName = SolmrConstants.APP_NAME_IRIDE2_SMRGAA;
      Application app = new Application(appName);
      UseCase[] useCaseVApplication = null;
      Vector<UseCase> useCaseVAbilitati = new Vector<UseCase>();
      UseCase useCase = null;

      Identita id = ruoloUtenzaVO.getIdentita();

      Ruolo[] ruoliVAbilitati = null;
      useCaseVApplication = anagFacadeClient.findUseCasesForApplication(app);
      for (int i = 0; i < useCaseVApplication.length; i++)
      {
        useCase = useCaseVApplication[i];
        ruoliVAbilitati = anagFacadeClient.findRuoliForPersonaInUseCase(id,
            useCase);

        for (int j = 0; j < ruoliVAbilitati.length; j++)
        {
          String nomeRuolo = ruoliVAbilitati[j].toString();
          if (ruoloUtenzaVO.getRuoloSelezionato().equalsIgnoreCase(nomeRuolo))
          {
            useCaseVAbilitati.add(useCase);
          }
        }
      }
      Iride2AbilitazioniVO iride2AbilitazioniVO = new Iride2AbilitazioniVO(
          (UseCase[]) useCaseVAbilitati.toArray(new UseCase[0]));
      return iride2AbilitazioniVO;
    }
    catch (Exception e)
    {
      throw new SolmrException(AnagErrors.ERRORE_PROBLEMI_ACCESSO_CU_IRIDE2);
    }
  }*/
  

  /*public static Iride2AbilitazioniVO findActorsForPersonaInUseCase(RuoloUtenza ruoloUtenzaVO,
      String nomeAttore, AnagFacadeClient anagFacadeClient) throws SolmrException
  {
    try
    {
      String appName = SolmrConstants.APP_NAME_IRIDE2_SMRGAA;
      Application app = new Application(appName);
      UseCase[] useCaseVApplication = null;
      Vector<UseCase> useCaseVAbilitati = new Vector<UseCase>();
      UseCase useCase = null;

      Identita id = ruoloUtenzaVO.getIdentita();

      Actor[] actor = null;
      useCaseVApplication = anagFacadeClient.findUseCasesForApplication(app);
      for (int i = 0; i < useCaseVApplication.length; i++)
      {
        useCase = useCaseVApplication[i];
        actor = anagFacadeClient.findActorsForPersonaInUseCase(id,
            useCase);

        for (int j = 0; j < actor.length; j++)
        {
          String nomeActorTmp = actor[j].getId();
          if (nomeActorTmp.equalsIgnoreCase(nomeAttore))
          {
            useCaseVAbilitati.add(useCase);
          }
        }
      }
      Iride2AbilitazioniVO iride2AbilitazioniVO = new Iride2AbilitazioniVO(
          (UseCase[]) useCaseVAbilitati.toArray(new UseCase[0]));
      return iride2AbilitazioniVO;
    }
    catch (Exception e)
    {
      throw new SolmrException(AnagErrors.ERRORE_PROBLEMI_ACCESSO_CU_IRIDE2);
    }
  }*/
  
  
  /**
   * Verifica con gli attori se si tratta di un caa in sola lettura!!!!
   * 
   * 
   * 
   * @param ruoloUtenzaVO
   * @param anagFacadeClient
   * @return
   * @throws SolmrException
   */
  /*public static boolean isCaaROForActors(RuoloUtenza ruoloUtenzaVO,
      AnagFacadeClient anagFacadeClient) throws SolmrException
  {
    try
    {

      boolean solaLettura = true;
      Identita id = ruoloUtenzaVO.getIdentita();

      Actor[] actor = anagFacadeClient.findActorsForPersonaInApplication(id);
      for(int i=0;i<actor.length;i++)
      {
        String nomeActorTmp = actor[i].getId();
        if(nomeActorTmp.equalsIgnoreCase("CAA_RW"))
        {
          solaLettura = false;
          break;
        }
      }
      
      return solaLettura;
    }
    catch (Exception e)
    {
      throw new SolmrException(AnagErrors.ERRORE_PROBLEMI_ACCESSO_CU_IRIDE2);
    }
  }*/
  

}
