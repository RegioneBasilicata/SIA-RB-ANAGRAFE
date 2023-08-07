package it.csi.solmr.presentation.security;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.smranag.smrgaa.util.MessaggisticaUtils;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.StringcodeDescription;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.services.DelegaAnagrafeVO;
import it.csi.solmr.dto.anag.services.MandatoVO;
import it.csi.solmr.dto.iride2.Iride2AbilitazioniVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.exception.ValidationException;
import it.csi.solmr.util.JavaScriptStringProcessor;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * <p>
 * Title: S.O.L.M.R.
 * </p>
 * <p>
 * Description: Servizi On-Line per il Mondo Rurale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: TOBECONFIG
 * </p>
 * 
 * @author Chiriotti/Einaudi
 * @version 1.0
 */
public abstract class Autorizzazione implements Serializable
{
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -1818800041972807401L;
  
  
  protected String  cuName;          // Da ridefinire nelle classi derivate
  protected boolean isCUForReadWrite;

  public boolean isUtenteAbilitato(Iride2AbilitazioniVO iride2AbilitazioniVO, boolean readWrite)
  {
    if (iride2AbilitazioniVO != null)
    {
      SolmrLogger.debug(this, "Case of iride2AbilitazioniVO!=null in method isUtenteAbilitato in Autorizzazione/n");
      SolmrLogger.debug(this, "Value of parameter [READ_WRITE] and [IS_CU_FOR_READ_WRITE] in method isUtenteAbilitato in Autorizzazione: " + iride2AbilitazioniVO.isUtenteAbilitato(cuName) + "\n");
      if (iride2AbilitazioniVO.isUtenteAbilitato(cuName))
        return readWrite || isCUForReadWrite == readWrite;
      else
        return false;
    }
    else
      return false;
  }

  /**
   * Esegue i controlli sulla competenza di dato per l'utente in questione.
   * 
   */
  public abstract String hasCompetenzaDato(HttpServletRequest request, HttpServletResponse response, RuoloUtenza ruoloUtenza, AnagFacadeClient anagFacadeClient);

  /**
   * Genera il menu delle pagine html relative a questo macro CU Iride2
   * 
   * @param htmpl
   *          Htmpl
   * @param request
   *          HttpServletRequest
   * @return java.lang.String
   */
  public abstract String writeMenu(Htmpl htmpl, HttpServletRequest request);

  /**
   * Metodo che si occupa di settare i banner relativi ai portali di RUPAR e
   * SISTEMA PIEMONTE in relazione alla tipologia del profilo
   * 
   * @param htmpl
   *          Htmpl
   * @param request
   *          HttpServletRequest
   */
  public void writeBanner(Htmpl htmpl, HttpServletRequest request)
  {
    RuoloUtenza ruoloUtenza = (RuoloUtenza) request.getSession().getAttribute("ruoloUtenza");
    String pathToFollow = (String) request.getSession().getAttribute("pathToFollow");
    
    if (ruoloUtenza != null)
    {
      htmpl.set("utente", ruoloUtenza.getDenominazione());
      htmpl.set("ente", ruoloUtenza.getDescrizioneEnte());
      // Utente provinciale
      if (ruoloUtenza.isUtenteProvinciale())
      {
        htmpl.bset("pathProvincia", "_" + StringUtils.getProvinciaByDescrizioneIride2(ruoloUtenza.getProvincia()).toLowerCase());
        // htmpl.set("pathProvincia", "_" + ruoloUtenza.getCodiceEnte());
      }
      else
      {
        // Utente regionale
        if (ruoloUtenza.isUtenteRegionale())
        {
          if (pathToFollow != null && pathToFollow.equalsIgnoreCase((String) SolmrConstants.PATH_TO_FOLLOW_RUPAR))
          {
            htmpl.bset("pathProvincia", "_regione");
          }
          else if(pathToFollow != null && pathToFollow.equalsIgnoreCase((String) SolmrConstants.PATH_TO_FOLLOW_SISTEMAPIEMONTE))        	 
          {
            htmpl.bset("pathProvincia", "");
          }
          else if(pathToFollow != null && pathToFollow.equalsIgnoreCase((String) SolmrConstants.PATH_TO_FOLLOW_TOBECONFIG))        	 
          {
              htmpl.bset("pathProvincia", "");
          }
          // htmpl.set("pathProvincia", "_" + ruoloUtenza.getIstatRegione());
        }
        // Comunità montana
        else
        {
          if (ruoloUtenza.isUtenteComunitaMontana())
          {
            htmpl.bset("pathProvincia", "_regione");
            /*
             * String istatRegione = null; try {
             * it.csi.solmr.dto.CodeDescription code =
             * anagFacadeClient.findRegioneByIstatProvincia(ruoloUtenza.getIstatComunitaMontana());
             * istatRegione = code.getCode().toString(); }
             * catch(SolmrException se) { } htmpl.set("pathProvincia",
             * "sistema_"+istatRegione);
             */
          }
          // Intermediario(Utenti CAA)
          else
          {
            if (ruoloUtenza.isUtenteIntermediario())
            {
              htmpl.bset("pathProvincia", "");
              /*
               * String istatRegione = null; try {
               * it.csi.solmr.dto.CodeDescription code =
               * anagFacadeClient.findRegioneByCodiceFiscaleIntermediario(ruoloUtenza.getCodiceEnte());
               * istatRegione = code.getCode().toString(); }
               * catch(SolmrException se) { } htmpl.set("pathProvincia",
               * "sistema_"+istatRegione);
               */
            }
            // Profilo Azienda agricola
            else
            {
              if (ruoloUtenza.isUtenteNonIscrittoCIIA())
              {
                htmpl.bset("pathProvincia", "");
                // Chiedere al dominio il corretto funzionamento per il
                // riconoscimento della regione di appartenenza dell'azienda
                // PROV_COMPETENZA.
              }
              // Assistenza CSI
              else
              {
                if (ruoloUtenza.isUtenteAssistenzaCsi() || ruoloUtenza.isUtenteServiziAgri())
                {
                  // Lo gestico solo ed esclusivamente in funzione del
                  // PIEMONTE e del CSI dal momento che si tratta di un utente
                  // specifico
                  htmpl.bset("pathProvincia", "_regione");
                }
                // Utente OPR
                else
                {
                  if (ruoloUtenza.isUtenteOPR() || ruoloUtenza.isUtenteOPRGestore())
                  {
                    // Lo setto in modo errato dal momento che non abbiamo
                    // ancora il banner a disposizione
                    htmpl.bset("pathProvincia", "_opr");                   
                  }
                  // Utente titolare o rappresentante legale
                  else
                  {
                    if (ruoloUtenza.isUtenteTitolareCf() || ruoloUtenza.isUtenteLegaleRappresentante())
                    {
                      htmpl.bset("pathProvincia", "");                      
                    }
                    else
                    {
                      if (ruoloUtenza.isUtenteASL() || ruoloUtenza.isUtenteGuardiaFinanza() || ruoloUtenza.isUtenteInps())
                      {
                        htmpl.bset("pathProvincia", "");                      
                      }
                      // Utente comune
                      else
                      {
                        if (ruoloUtenza.isUtenteComunale())
                        {
                          htmpl.bset("pathProvincia", "_regione");
                        }
                      }
                    }
                  }
                }
              }
            }            
          }
        }
        
        // Visualizzazione striscia con messaggi utente
        try
        {
           // prendo i dati da visualizzare dalla sessione
           StringcodeDescription message = MessaggisticaUtils.getMessaggiTestata(request.getSession());
           if(message!=null)
           {
             if(Validator.isNotEmpty(message.getDescription()))
             {
               htmpl.set("txtMessaggioTestataUtente",message.getDescription().replace("\"", "&quot;"), new JavaScriptStringProcessor());
             }
             
             
             //htmpl.set("txtMessaggioTestataUtente",message.getDescription().replace("\"", "&quot;"), new JavaScriptStringProcessor());
             
             if(message.getCode()!=null && !message.getCode().equals("0"))
             {
               if("1".equals(message.getCode()))
               {
                 htmpl.set("txtNumMessaggiDaLeggere","C'è un messaggio da leggere", new JavaScriptStringProcessor());      
               }
               else
               {
                 htmpl.set("txtNumMessaggiDaLeggere","Ci sono "+message.getCode()+" messaggi da leggere");   
               }
             }
           }
         }
         catch (Exception e) 
         {
           SolmrLogger.error(this, "Errore richiamo messaggistica "+ LoggerUtils.getStackTrace(e));
         }
        
        
      }
    }
  }

  /**
   * Si occupa di scrivere il messaggio delle eccezioni lanciate: lo implemento
   * perchè è utilizzato in molti sezioni "vecchie" di anagrafe
   * 
   * @param exception
   *          Throwable
   * @param htmpl
   *          HttpServletRequest
   */
  public void writeException(Throwable exception, Htmpl htmpl)
  {
    if (exception != null && exception instanceof ValidationException)
    {
      ValidationException v = (ValidationException) exception;
      Map<Object,Object> m = v.getMessages();
      Set<Object> keys = m.keySet();
      Iterator<Object> iter = keys.iterator();
      while (iter.hasNext())
      {
        String position = (String) iter.next();
        String message = (String) m.get(position);
        htmpl.set(position, message);
      }
    }
  }

  /**
   * Metodo che si occupa di scrivere la barra di stato in relazione alle
   * notifiche presenti per l'azienda selezionata
   * 
   * @param se
   *          SolmrException
   * @param htmpl
   *          Htmpl
   */
  public void writeBarraStato(SolmrException se, Htmpl htmpl)
  {
    String notifica = "";
    String afterNotifica = "";
    String titleNotifica = "";
    if (Validator.isNotEmpty(se))
    {
      int separatore = se.getMessage().indexOf("%");
      if (separatore != -1)
      {
        afterNotifica = se.getMessage().substring(0, separatore);
        notifica = se.getMessage().substring(separatore + 1);
      }
      else
      {
        notifica = se.getMessage();
      }
      
      int separatoreCat = notifica.indexOf("*");
      if (separatoreCat != -1)
      {
        titleNotifica = notifica.substring(separatoreCat+1);
        notifica = notifica.substring(0,separatoreCat);
      }
      
      if (Validator.isNotEmpty(se.getMessage()))
      {
        htmpl.newBlock("blkNotifica");
        if (separatore != -1)
        {
          htmpl.set("blkNotifica.messaggioNotifica", notifica);
          htmpl.newBlock("blkNotifica.blkINNotifica");
          htmpl.set("blkNotifica.blkINNotifica.notifica", afterNotifica);
        }
        else
        {
          htmpl.set("blkNotifica.messaggioNotifica", notifica);
        }        
        
        if (se.getErrorType() != 1)
        {
          if (se.getErrorType() == 2)
          {
            htmpl.set("blkNotifica.immagine", (String) SolmrConstants.get("STATO_BLOCCATO"));
          }
          else if (se.getErrorType() == 3)
          {
            htmpl.set("blkNotifica.immagine", (String) SolmrConstants.get("STATO_WARNING"));
          }
          else if (se.getErrorType() == 5)
          {
            htmpl.set("blkNotifica.immagine", SolmrConstants.STATO_BLOCCO_PROCEDIMENTI);
          }
          else if (se.getErrorType() == 6)
          {
            htmpl.set("blkNotifica.immagine", SolmrConstants.STATO_NOTIFICHE_PARTICELLARI);
          }
          else
          {
            htmpl.set("blkNotifica.immagine", (String) SolmrConstants.get("STATO_SEGNALAZIONI"));
          }
        }
        
        if (separatoreCat != -1)
        {
          htmpl.newBlock("blkTabElencoNotifica");
          StringTokenizer strToken = new StringTokenizer(titleNotifica, "*");
          while (strToken.hasMoreTokens())
          {
            htmpl.newBlock("blkElencoNotifica");
            StringTokenizer strTokenNot = new StringTokenizer(strToken.nextToken(), ",");
            String idTipologiaNotifica = strTokenNot.nextToken();
            Long idTipologiaNotificaLg = new Long(idTipologiaNotifica);
            if (idTipologiaNotificaLg.compareTo((Long) SolmrConstants.get("ID_TIPO_TIPOLOGIA_BLOCCANTE")) == 0)
            {
              htmpl.set("blkNotifica.blkTabElencoNotifica.blkElencoNotifica.elencoImmagine", (String) SolmrConstants.get("STATO_BLOCCATO"));
            }
            else if (idTipologiaNotificaLg.compareTo((Long) SolmrConstants.get("ID_TIPO_TIPOLOGIA_WARNING")) == 0)
            {
              htmpl.set("blkNotifica.blkTabElencoNotifica.blkElencoNotifica.elencoImmagine", (String) SolmrConstants.get("STATO_WARNING"));
            }
            else if (idTipologiaNotificaLg.compareTo(SolmrConstants.ID_TIPO_TIPOLOGIA_BLOCCAPROCEDIMENTI) == 0)
            {
              htmpl.set("blkNotifica.blkTabElencoNotifica.blkElencoNotifica.elencoImmagine", SolmrConstants.STATO_BLOCCO_PROCEDIMENTI);
            }
            else if (idTipologiaNotificaLg.compareTo(SolmrConstants.ID_TIPO_TIPOLOGIA_VARIAZIONECATASTALE) == 0)
            {
              htmpl.set("blkNotifica.blkTabElencoNotifica.blkElencoNotifica.elencoImmagine", SolmrConstants.STATO_NOTIFICHE_PARTICELLARI);
            }
            else
            {
              htmpl.set("blkNotifica.blkTabElencoNotifica.blkElencoNotifica.elencoImmagine", (String) SolmrConstants.get("STATO_SEGNALAZIONI"));
            }
            String elencoMessaggioNotifica = strTokenNot.nextToken();
            htmpl.set("blkNotifica.blkTabElencoNotifica.blkElencoNotifica.elencoMessaggioNotifica", elencoMessaggioNotifica);
          }
        }
        
       
      }
    }
  }

  /**
   * Metodo che si occupa di valorizzare la barra iniziale contenente i dati
   * principale dell'azienda agricola selezionata
   * 
   * @param anagAziendaVO
   *          AnagAziendaVO
   * @param htmpl
   *          Htmpl
   */
  public void writeBarraDatiAzienda(AnagAziendaVO anagAziendaVO, Htmpl htmpl)
  {
    if (anagAziendaVO != null)
    {
      //Se il nostro CUAA è diverso da quello dell'anagrafe tributaria devo segnalarlo
      //con un messaggio
      
      if (anagAziendaVO.getCUAAAnagrafeTributaria()!=null 
          && !anagAziendaVO.getCUAAAnagrafeTributaria().equals(anagAziendaVO.getCUAA()))
      {      
        htmpl.newBlock("blkNotifica");
        htmpl.set("blkNotifica.messaggioNotifica", AnagErrors.ERR_CUAA_DIVERSO_CUAA_SIAN+" "+anagAziendaVO.getCUAAAnagrafeTributaria());
        htmpl.set("blkNotifica.immagine", (String) SolmrConstants.get("STATO_WARNING"));
      }
      if (Validator.isNotEmpty(anagAziendaVO.getCUAA()))
      {
        htmpl.set("CUAAIntestazione", anagAziendaVO.getCUAA().toUpperCase() + " - ");
      }
      /*if (anagAziendaVO.getDenominazione().length() > 100)
      {
        htmpl.set("denominazioneIntestazione", anagAziendaVO.getDenominazione().substring(0, 100) + " [...]");
      }
      else
      {
        htmpl.set("denominazioneIntestazione", anagAziendaVO.getDenominazione());
      }*/
      String denominazioneIntestazione = anagAziendaVO.getDenominazione();
      if(Validator.isNotEmpty(anagAziendaVO.getIntestazionePartitaIva()))
        denominazioneIntestazione += " - "+anagAziendaVO.getIntestazionePartitaIva();
      
      htmpl.set("denominazioneIntestazione", denominazioneIntestazione);
      if (Validator.isNotEmpty(anagAziendaVO.getDataSituazioneAlStr()))
      {
        htmpl.set("dataSituazioneAlStr", anagAziendaVO.getDataSituazioneAlStr());
      }
      
      
      //Icona dematerializzato     
      if("S".equalsIgnoreCase(anagAziendaVO.getFascicoloDematerializzato()))
      {
        htmpl.set("aziendaIntestazione", "fascicolovirtuale");
        htmpl.set("tooltipIntestazione", SolmrConstants.DESC_FASCICOLO_VIRTUALE);
      }
      else
      {
        htmpl.set("aziendaIntestazione", "azienda");
      }
      
      
      
    }
  }

  /**
   * Metodo che si occupa di far visualizzare tutte le macro sezioni relative
   * all'anagrafe non soggette a vincoli di profilazione o al numero e tipo
   * records diversi dall'oggetto anagAziendaVO
   * 
   * @param ruoloUtenza
   * @param anagAziendaVO
   * @param htmpl
   * @param request
   */
  public void writeGenericMenu(RuoloUtenza ruoloUtenza, AnagAziendaVO anagAziendaVO, Htmpl htmpl, HttpServletRequest request)
  {
    
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni)request.getSession().getAttribute("utenteAbilitazioni");
    Iride2AbilitazioniVO iride2AbilitazioniVO=(Iride2AbilitazioniVO) request.getSession().getAttribute("iride2AbilitazioniVO");
    
    
    // Abilitazione voce di menu 'sincronizza fascicolo' per l'utente intermediario e regionale
   // if(ruoloUtenza.isUtenteIntermediario() || ruoloUtenza.isUtenteRegionale()){
      htmpl.newBlock("blkSincronizzaFascicolo");
    //}
    
    if (iride2AbilitazioniVO.isUtenteAbilitato("RICERCA_VARIAZIONI_AZIENDA"))
      htmpl.newBlock("blkVariazioniAzienda");

    
    // Verifico che il profilo ci sia e sia corretto
    if (ruoloUtenza != null)
    {      
      if (iride2AbilitazioniVO.isUtenteAbilitato("AZIENDE_TITOLARE"))
        htmpl.newBlock("blkElencoAziendeRapLegale");
      else
      {
        if (iride2AbilitazioniVO.isUtenteAbilitato("RICERCA_AZIENDA"))
          htmpl.newBlock("blkRicercaAzienda");
      }
      
      
      if (iride2AbilitazioniVO.isUtenteAbilitato("RICHIESTA_AZIENDA"))
        htmpl.newBlock("blkNuovaIscrizione");
      
      
      if (iride2AbilitazioniVO.isUtenteAbilitato("RICERCA_TERRENO"))
        htmpl.newBlock("blkRicercaTerreno");
      
      //Se l'utente è intermediario è sta adata la possibilità di inserite nuove particelle
      //La pagine che chiama sono usate anche nel menu Terreno per gli utenti Regionale, prov, e opr!!!!
      if(ruoloUtenza.isUtenteIntermediario())
      {
        htmpl.newBlock("blkNuovaParticella");
      }
      //*****************************************************************************
      //ruoloUtenza.isUtenteNonIscrittoCIIA() corrisponde sul foglio iride all'azienda_agricola.
      //Mentre ruoloUtenza.isUtenteNonIscrittoCIIA(), ruoloUtenza.isUtenteLegaleRappresentante(),
      // ruoloUtenza.isUtenteTitolareCf() corrispondono a ruoloUtenza.isUtenteAziendaAgricola(),
      //cioè quest'ultimo li raggruppa tutti e tre!!!
      //******************************************************************************
      
      if (iride2AbilitazioniVO.isUtenteAbilitato("ESTRAZIONE_DATI"))
        htmpl.newBlock("blkEstrazioneDati");
      
      
      if (iride2AbilitazioniVO.isUtenteAbilitato("RICERCA_NOTIFICA"))
        htmpl.newBlock("blkRicercaNotifica");
      
      if (iride2AbilitazioniVO.isUtenteAbilitato("RICERCA_PERSONA"))
        htmpl.newBlock("blkRicercaPersona");
      
      if (iride2AbilitazioniVO.isUtenteAbilitato("REPORTISTICA"))
      {
        htmpl.newBlock("blkReportistica");
        htmpl.newBlock("blkReportisticaRiepilogoMandati");
        htmpl.newBlock("blkReportisticaElencoAziendeMandato");
        htmpl.newBlock("blkReportisticaRiepilogoValidazioni");        
      }
      
      
      if (ruoloUtenza.isReadWrite())
      {
        if (iride2AbilitazioniVO.isUtenteAbilitato("NUOVA_AZIENDA"))
        {
          htmpl.newBlock("blkNuova");
          htmpl.newBlock("bloccoInserimento");
        }
        
        if (ruoloUtenza.isUtenteProvinciale() || ruoloUtenza.isUtenteRegionale() || ruoloUtenza.isUtenteOPRGestore() || ruoloUtenza.isUtenteIntermediario())
        {
          // Il blocco va visualizzato solo nel caso
          // l'intermediario (CAA) sia regionale o provinciale!
          if (ruoloUtenza.isUtenteIntermediario())
          {
           if(utenteAbilitazioni != null){	  
            if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario() != null)
            {
              if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equals("P") 
                  || utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equals("R"))
              {
                htmpl.newBlock("blkServizi");
              }
            }
           } 
          }
          }
          else
          {
            htmpl.newBlock("blkServizi");
          }
        }
        
        
        if (iride2AbilitazioniVO.isUtenteAbilitato("RICERCA_RICHIESTA"))
        {
          htmpl.newBlock("blkRicercaRichiesta");
        }
      }
      // Controllo che l'azienda ci sia e che l'utente non stia visualizzando un
      // record storicizzato
      if (anagAziendaVO != null && !Validator.isNotEmpty(anagAziendaVO.getDataFineVal()))
      {

        // Visualizzo tutte le voci di menù(macro sezioni) non soggette a
        // vincoli di profilazione

        // Visualizzo il blocco solo se trattasi di aziende associate
        if (((anagAziendaVO.getFlagFormaAssociata() != null) && (anagAziendaVO.getFlagFormaAssociata().equalsIgnoreCase(SolmrConstants.FLAG_S)))
              || anagAziendaVO.isFlagEnteAppartenenza())
        {
          
          GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
          
          try
          {
            if(gaaFacadeClient.isAziendeCollegataMenu(anagAziendaVO.getIdAzienda().longValue())
              || ruoloUtenza.isReadWrite())
            {
            
              htmpl.newBlock("blkAziendeCollegate");
              // Michele, 27/11/2009 - INIZIO
              String labelElencoAssociati = null;
              labelElencoAssociati = anagAziendaVO.getLabelElencoAssociati();
              labelElencoAssociati = (labelElencoAssociati == null ? "Elenco associati": labelElencoAssociati);
              htmpl.set("blkAziendeCollegate.aziendeCollegateLabel", labelElencoAssociati);
    
              try
              {
                htmpl.bset("aziendeCollegateArianna", labelElencoAssociati);
              }
              catch (Exception ex)
              {
                SolmrLogger.debug(this, "##aziendeCollegateArianna non esistente");
              }
    
              String labelSubAssociati = null;
              labelSubAssociati = anagAziendaVO.getLabelSubAssociati();
              labelSubAssociati = (labelSubAssociati == null ? "soci collegati": labelSubAssociati);
              htmpl.set("blkAziendeCollegate.aziendeCollegateList", labelSubAssociati);
              // Michele, 27/11/2009 - FINE
                
            }
          }        
          catch (Exception ex)
          {
            SolmrLogger.debug(this, "problemi nella verifica se l'azienda si trova su db_azienda_collegata");
          }
        }

        htmpl.newBlock("blkSoggettiCollegati");
        htmpl.newBlock("blkUnitaProduttive");
        htmpl.newBlock("blkTerreni");
        if (iride2AbilitazioniVO.isUtenteAbilitato("AZIENDE_TITOLARE"))
          htmpl.newBlock("blkTerreni.blkLinkRiepiloghi");
        else
          htmpl.newBlock("blkTerreni.blkLinkElenco");
        
        
        htmpl.newBlock("blkUnitaVitate");
        if (iride2AbilitazioniVO.isUtenteAbilitato("AZIENDE_TITOLARE"))
          htmpl.newBlock("blkUnitaVitate.blkLinkRiepiloghi");
        else
          htmpl.newBlock("blkUnitaVitate.blkLinkElenco");
        
        
        htmpl.newBlock("blkFabbricati");
        htmpl.newBlock("blkAllevamenti");
        htmpl.newBlock("blkManodopera");
        htmpl.newBlock("blkMotoriAgricoli");
        
        
        
        //Stampe
        if (iride2AbilitazioniVO.isUtenteAbilitato("STAMPA_FASCICOLO"))
        {
          htmpl.newBlock("blkStampaFascicolo");
        }        
        //***********************
        
        //Documenti
        if (iride2AbilitazioniVO.isUtenteAbilitato("VISUALIZZA_DOCUMENTI"))
        {
          htmpl.newBlock("blkDocumenti");
        }        
        //***********************
        
        //Fascicolo dematerializzato
        if (iride2AbilitazioniVO.isUtenteAbilitato("FASCICOLO_DEM_VIEW"))
        {
          htmpl.newBlock("blkFascicoloDematerializzato");
        }
        //********************************
        
        //Comunicazione 10R
        if (iride2AbilitazioniVO.isUtenteAbilitato("VISUALIZZA_COMUNICAZIONE_10R"))
        {
          htmpl.newBlock("blkComunicazione10R");
        }        
        //***********************
        
        //Pratiche
        if (iride2AbilitazioniVO.isUtenteAbilitato("PRATICHE"))
        {
          htmpl.newBlock("blkPratiche");
        }        
        //***********************
        
        
        
        //Fonti certificate Anagrafica
        boolean flagFontiCertificate = false;
        if (iride2AbilitazioniVO.isUtenteAbilitato("VISUALIZZA_FONTE_AAEP"))
          flagFontiCertificate = true;
        
        if (iride2AbilitazioniVO.isUtenteAbilitato("VISUALIZZA_FONTE_AT"))
        {
          flagFontiCertificate = true;
        }
        
        
        
        if(flagFontiCertificate)
        {
          htmpl.newBlock("blkFontiCertificate");
        }
        
        if (iride2AbilitazioniVO.isUtenteAbilitato("VISUALIZZA_FONTE_AT"))
        {
          htmpl.newBlock("linkSianAnagrafeTributaria");
          htmpl.newBlock("blkSianAnagrafeTributaria");
        }
        
        if (iride2AbilitazioniVO.isUtenteAbilitato("VISUALIZZA_FONTE_SIAN"))
        {
          htmpl.newBlock("linkSianAltriDati");
          htmpl.newBlock("blkSianAltriDati");
        }
        
        if (iride2AbilitazioniVO.isUtenteAbilitato("VISUALIZZA_FONTE_AAEP"))
        {
          //if (piemonte)
          //{
            htmpl.newBlock("linkAnagraficaVisuraCamerale");
            htmpl.newBlock("blkAnagraficaVisuraCamerale");
          //}
        }
        
        //Fonti certificate Soggetti AT
        if (iride2AbilitazioniVO.isUtenteAbilitato("VISUALIZZA_SOGGETTI_AT"))
        {
          htmpl.newBlock("blkSoggettiCollegati.linkSianAnagrafeTributaria");
        }
        //******************************************************
        
        
        //Fonti certificate Soggetti AAEP
        if (iride2AbilitazioniVO.isUtenteAbilitato("VISUALIZZA_SOGGETTI_AAEP"))
        {
         // if (piemonte)
          //{
            htmpl.newBlock("blkSoggettiCollegati.blkFontiCertificate");
          //}
        }        
        //***********************
        
        //Fonti certificate Ute AAEP
        if (iride2AbilitazioniVO.isUtenteAbilitato("VISUALIZZA_UTE_AAEP"))
        {
          //if (piemonte)
          //{
            htmpl.newBlock("blkUnitaProduttive.blkFontiCertificate");
          //}
        }        
        //***********************
        
        
        //Conti Correnti
        if (iride2AbilitazioniVO.isUtenteAbilitato("VISUALIZZA_CONTI_CORRENTI"))
        {
          htmpl.newBlock("blkContiCorrenti");
        }        
        //***********************
        
        
        //Diritti
        if (iride2AbilitazioniVO.isUtenteAbilitato("DIRITTI_PRODUZIONE"))
        {
          htmpl.newBlock("blkDiritti");
        }        
        //***********************
        
        
        //Notifiche
        if (iride2AbilitazioniVO.isUtenteAbilitato("NOTIFICHE"))
        {
          htmpl.newBlock("blkGestioneNotifiche");
          htmpl.newBlock("blkGestioneNotifiche.blkNuovaNotifica");
          htmpl.newBlock("blkGestioneNotifiche.blkModificaNotifica");
        }
        
        if (iride2AbilitazioniVO.isUtenteAbilitato("CHIUSURA_NOTIFICHE"))
        {
          htmpl.newBlock("blkGestioneNotifiche.blkChiusuraNotifica");
        }
        //***********************
        
        
        //Dichiarazione Consistenza
        if (iride2AbilitazioniVO.isUtenteAbilitato("VISUALIZZA_CONSISTENZA"))
        {
          if (!anagAziendaVO.isFlagAziendaProvvisoria())
          {
            htmpl.newBlock("blkDichiarazioniConsistenza");
          }
        }
        
        
        //Attestazioni
        if (iride2AbilitazioniVO.isUtenteAbilitato("VISUALIZZA_ATTESTATI"))
        {
          htmpl.newBlock("blkAttestazioni");
          //htmpl.newBlock("blkAllegati");
        }        
        //***********************
        
        //Storico mandati
        if (iride2AbilitazioniVO.isUtenteAbilitato("STORICO_MANDATI"))
        {
          htmpl.newBlock("blkStoricoMandati");
        }        
        //****************************
        
        //Storico azienda
        if (iride2AbilitazioniVO.isUtenteAbilitato("STORICO_AZIENDA"))
        {
          htmpl.newBlock("blkStoricoAzienda");
        }        
        //****************************
        
        //Gestore fascicolo
        if (iride2AbilitazioniVO.isUtenteAbilitato("VISUALIZZA_GEST_FASCICOLO"))
        {
          htmpl.newBlock("linkGestoreFascicolo");
        }        
        //****************************

        if (!ruoloUtenza.isUtentePALocale() && !ruoloUtenza.isUtentePALocaleSuper()
            && !ruoloUtenza.isUtenteAziendaAgricola() && !ruoloUtenza.isUtenteLegaleRappresentante()
            && !ruoloUtenza.isUtenteTitolareCf())
        {          
          if (anagAziendaVO.getDataCessazione() == null && anagAziendaVO.getDataCessazioneStr() == null && anagAziendaVO.getDataFineVal() == null)
          {
            htmpl.newBlock("blkTerreni.blkVerifiche");
          }
        }
      }
    }
  

  /**
   * Valida l'accesso di un CAA ad una azienda
   * 
   * @param ruoloUtenza
   *          RuoloUtenza
   * @param anagFacadeClient
   *          AnagFacadeClient
   * @param anagAziendaVO
   *          AnagAziendaVO
   * @return java.lang.String
   */
  protected String validateCAA(RuoloUtenza ruoloUtenza, AnagFacadeClient anagFacadeClient, AnagAziendaVO anagAziendaVO)
  {
    SolmrLogger.debug(this, "Sono nel metodo validateCAA in Autorizzazione\n");
    try
    {
      SolmrLogger.debug(this, "Valore del parametro isCUForReadWrite nel metodo validateCAA in Autorizzazione:" + this.isCUForReadWrite + "\n");
      if (this.isCUForReadWrite)
      {
        SolmrLogger.debug(this, "Valore del parametro data cessazione nel metodo validateCAA in Autorizzazione:" + anagAziendaVO.getDataCessazione() + "\n");
        SolmrLogger.debug(this, "Valore del parametro data cessazione str nel metodo validateCAA in Autorizzazione:" + anagAziendaVO.getDataCessazioneStr() + "\n");
        SolmrLogger.debug(this, "Valore del parametro data fine validita nel metodo validateCAA in Autorizzazione:" + anagAziendaVO.getDataFineVal() + "\n");
        if (anagAziendaVO.getDataCessazione() != null || anagAziendaVO.getDataCessazioneStr() != null || anagAziendaVO.getDataFineVal() != null)
        {
          return AnagErrors.ERRORE_AUT_AZIENDA_STORICIZZATA_CESSATA;
        }
      }
      Long idAzienda = anagAziendaVO.getIdAzienda();
      SolmrLogger.debug(this, "Valore del parametro id azienda nel metodo validateCAA in Autorizzazione:" + idAzienda + "\n");
      String codiceIntermediario = ruoloUtenza.getCodiceEnte();
      SolmrLogger.debug(this, "Valore del parametro codice intermediario nel metodo validateCAA in Autorizzazione:" + codiceIntermediario + "\n");
      MandatoVO mandatoVO = anagFacadeClient.serviceGetMandato(idAzienda, codiceIntermediario);
      SolmrLogger.debug(this, "Value of parameter [MANDATO_VO] in method validateCAA in Autorizzazione.java: " + mandatoVO + "\n");
      // Mi estraggo la delega
      DelegaAnagrafeVO delegaAnagrafeVO = mandatoVO.getDelegaAnagrafe();
      SolmrLogger.debug(this, "Value of parameter [DELEGA_ANAGRAFE_VO] in method validateCAA in Autorizzazione.java: " + delegaAnagrafeVO + "\n");
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
        SolmrLogger.debug(this, "Value of parameter [CODICE_INTERMEDIARIO] in method validateCAA in Autorizzazione.java: " + codiceIntermediario + "\n");
        SolmrLogger.debug(this, "Value of parameter [CODICE_FISCALE_INTERMEDIARIO_DELEGA] in method validateCAA in Autorizzazione.java: " + delegaAnagrafeVO.getCodiceFiscIntermediario() + "\n");
        SolmrLogger.debug(this, "Value of parameter [FLAG_FIGLIO_DELEGA] in method validateCAA in Autorizzazione.java: " + delegaAnagrafeVO.getFlagFiglio() + "\n");
        if (!(codiceIntermediario.equalsIgnoreCase(delegaAnagrafeVO.getCodiceFiscIntermediario()) || SolmrConstants.FLAG_S.equalsIgnoreCase(delegaAnagrafeVO.getFlagFiglio())))
        {
          return AnagErrors.ERRORE_AUT_UTENTE_SENZA_MANDATO;
        }
      }
      else
      {
        // Se non c'è la delega ==> Errore
        return AnagErrors.ERRORE_AUT_UTENTE_SENZA_MANDATO;
      }
      // Se è arrivato qui, è abilitato!
      return null;
    }
    catch (SolmrException ex)
    {
      return ex.getMessage();
    }
    catch (Exception ex)
    {
      return AnagErrors.ERRORE_DI_SISTEMA;
    }
  }

  /**
   * Valida l'accesso di un regionale sulle aziende prive di mandato
   * 
   * @param anagAziendaVO
   *          AnagAziendaVO
   * @return java.lang.String
   */
  protected String validateRegionale(AnagAziendaVO anagAziendaVO)
  {
    if (this.isCUForReadWrite)
    {
      if (anagAziendaVO.getDataCessazione() != null || anagAziendaVO.getDataCessazioneStr() != null || anagAziendaVO.getDataFineVal() != null)
      {
        return AnagErrors.ERRORE_AUT_AZIENDA_STORICIZZATA_CESSATA;
      }
    }
    else
      return null;
    
    if (!anagAziendaVO.isPossiedeDelegaAttiva())
      return null;
    else
      return AnagErrors.ERRORE_AUT_AZIENDA_CON_MANDATO;
  }

  /**
   * Valida l'accesso di un provinciale sulle aziende prive di mandato e della
   * propria provincia di competenza
   * 
   * @param ruoloUtenza
   *          RuoloUtenza
   * @param anagAziendaVO
   *          AnagAziendaVO
   * @return java.lang.String
   */
  protected String validateProvinciale(RuoloUtenza ruoloUtenza, AnagAziendaVO anagAziendaVO)
  {
    if (this.isCUForReadWrite)
    {
      if (anagAziendaVO.getDataCessazione() != null || anagAziendaVO.getDataCessazioneStr() != null || anagAziendaVO.getDataFineVal() != null)
      {
        return AnagErrors.ERRORE_AUT_AZIENDA_STORICIZZATA_CESSATA;
      }
    }
    else
      return null;
    try
    {
      if (!ruoloUtenza.getIstatProvincia().equals(anagAziendaVO.getProvCompetenza()))
        return AnagErrors.ERRORE_AUT_NO_PROV_COMPETENZA;
      if (anagAziendaVO.isPossiedeDelegaAttiva())
        return AnagErrors.ERRORE_AUT_AZIENDA_CON_MANDATO;
      return null;
    }
    catch (Exception ex)
    {
      return AnagErrors.ERRORE_DI_SISTEMA;
    }
  }

  /**
   * Valida l'accesso di un OPR sulle aziende prive di mandato
   * 
   * @param anagAziendaVO
   *          AnagAziendaVO
   * @return java.lang.String
   */
  protected String validateOPR(AnagAziendaVO anagAziendaVO)
  {
    if (this.isCUForReadWrite)
    {
      if (anagAziendaVO.getDataCessazione() != null || anagAziendaVO.getDataCessazioneStr() != null || anagAziendaVO.getDataFineVal() != null)
      {
        return AnagErrors.ERRORE_AUT_AZIENDA_STORICIZZATA_CESSATA;
      }
    }
    else
      return null;
    if (!anagAziendaVO.isPossiedeDelegaAttiva())
      return null;
    else
      return AnagErrors.ERRORE_AUT_AZIENDA_CON_MANDATO;
  }

  /**
   * Valida l'accesso di un AZIENDA_AGRICOLA sulla propria azienda
   * 
   * @param anagAziendaVO
   *          AnagAziendaVO
   * @param ruoloUtenza
   *          RuoloUtenza
   * @return java.lang.String
   */
  @SuppressWarnings("unchecked")
  protected String validateAziendaAgricola(AnagAziendaVO anagAziendaVO, RuoloUtenza ruoloUtenza, HttpServletRequest request, AnagFacadeClient anagFacadeClient)
  {
    try
    {
      Vector<String> elencoCUAATitolare = (Vector<String>) request.getSession().getAttribute("elencoCUAATitolare");
      if (ruoloUtenza.getCUAA().equals(anagAziendaVO.getCUAA()))
        return null;
      
      
      // Deve poter vedere oltre ai dati dell'azienda agricola legata anche
      // quello di tutte quelle associate e dei rispettivi figli      
      if(elencoCUAATitolare == null)
      {
        elencoCUAATitolare = new Vector<String>();
        elencoCUAATitolare.add(ruoloUtenza.getCUAA());
        request.getSession().setAttribute("elencoCUAATitolare", elencoCUAATitolare);
      }
      
      //inserisco sempre il CUAA se non già presente
      if(!elencoCUAATitolare.contains(anagAziendaVO.getCUAA()))
      {
        elencoCUAATitolare.add(anagAziendaVO.getCUAA());
        request.getSession().setAttribute("elencoCUAATitolare", elencoCUAATitolare);
      }
      
      //controllo tutti CUAA, anche l'ultimo inserito tanto non potrà mai essere
      //figlio di se stesso!!!
      for (int i = 0; i < elencoCUAATitolare.size(); i++)
      {
        if (anagFacadeClient.controlloAziendeAssociate(
              (String)elencoCUAATitolare.get(i), anagAziendaVO.getIdAzienda()))
          return null;
      }
      
      //Se non è presente tra nessuno dei figli di tutti i CUAA presenti
      //viene eliminato e bloccato l'accesso!!!!
      elencoCUAATitolare.remove(anagAziendaVO.getCUAA());
      request.getSession().setAttribute("elencoCUAATitolare", elencoCUAATitolare);
      
      
      return AnagErrors.ERRORE_AUT_AZIENDA_NON_PROPRIA;
    }
    catch (SolmrException ex)
    {
      return ex.getMessage();
    }
    catch (Exception ex)
    {
      return AnagErrors.ERRORE_DI_SISTEMA;
    }
  }

  /**
   * Valida l'accesso di un TITOLARE sulla propria azienda
   * 
   * @param anagAziendaVO
   *          AnagAziendaVO
   * @param ruoloUtenza
   *          RuoloUtenza
   * @param request
   *          HttpServletRequest
   * @return java.lang.String
   */
  @SuppressWarnings({ "unchecked", "unused" })
  protected String validateTitolare(AnagAziendaVO anagAziendaVO, RuoloUtenza ruoloUtenza, HttpServletRequest request, AnagFacadeClient anagFacadeClient)
  {
    try
    {
      Vector<String> elencoCUAATitolare = (Vector<String>) request.getSession().getAttribute("elencoCUAATitolare");
      if (elencoCUAATitolare == null)
        return AnagErrors.ERRORE_AUT_AZIENDA_NON_PROPRIA;
      for (int i = 0; i < elencoCUAATitolare.size(); i++)
        if (anagAziendaVO.getCUAA().equals((String)elencoCUAATitolare.get(i)))
          return null;

      // Deve poter vedere oltre ai dati del TITOLARE anche
      // quello di tutte quelle associate e dei rispettivi figli      
      if(elencoCUAATitolare == null)
      {
        elencoCUAATitolare = new Vector<String>();
        elencoCUAATitolare.add(ruoloUtenza.getCUAA());
        request.getSession().setAttribute("elencoCUAATitolare", elencoCUAATitolare);
      }
      
      
      //inserisco sempre il CUAA se non già presente
      if(!elencoCUAATitolare.contains(anagAziendaVO.getCUAA()))
      {
        elencoCUAATitolare.add(anagAziendaVO.getCUAA());
        request.getSession().setAttribute("elencoCUAATitolare", elencoCUAATitolare);
      }
      
      //Controllo tutti CUAA, anche l'ultimo inserito tanto non potrà mai essere
      //figlio di se stesso!!!
      for (int i = 0; i < elencoCUAATitolare.size(); i++)
      {
        if (anagFacadeClient.controlloAziendeAssociate(
              (String)elencoCUAATitolare.get(i), anagAziendaVO.getIdAzienda()))
          return null;
      }
        
      
      //Se non è presente tra nessuno dei figli di tutti i CUAA presenti
      //viene eliminato e bloccato l'accesso!!!!
      elencoCUAATitolare.remove(anagAziendaVO.getCUAA());
      request.getSession().setAttribute("elencoCUAATitolare", elencoCUAATitolare);

      return AnagErrors.ERRORE_AUT_AZIENDA_NON_PROPRIA;
    }
    catch (SolmrException ex)
    {
      return ex.getMessage();
    }
    catch (Exception ex)
    {
      return AnagErrors.ERRORE_DI_SISTEMA;
    }
  }

  /**
   * Valida l'accesso di un OPR Gestore (OPR con delega) ad un'azienda
   * 
   * @param ruoloUtenza
   *          RuoloUtenza
   * @param anagFacadeClient
   *          AnagFacadeClient
   * @param anagAziendaVO
   *          AnagAziendaVO
   * @return java.lang.String
   */
  protected String validateORPGestore(RuoloUtenza ruoloUtenza, AnagFacadeClient anagFacadeClient, AnagAziendaVO anagAziendaVO)
  {
    try
    {
      if (this.isCUForReadWrite)
      {
        if (anagAziendaVO.getDataCessazione() != null || anagAziendaVO.getDataCessazioneStr() != null || anagAziendaVO.getDataFineVal() != null)
        {
          return AnagErrors.ERRORE_AUT_AZIENDA_STORICIZZATA_CESSATA;
        }
        Long idAzienda = anagAziendaVO.getIdAzienda();
        String codiceIntermediario = ruoloUtenza.getCodiceEnte();
        MandatoVO mandatoVO = anagFacadeClient.serviceGetMandato(idAzienda, codiceIntermediario);
        // Mi estraggo la delega
        DelegaAnagrafeVO delegaAnagrafeVO = mandatoVO.getDelegaAnagrafe();
        if (delegaAnagrafeVO != null)
        {
          // Se esiste una delega devo controllare che il codice dell'ente
          // intermediario a cui appartiene questa delega sia lo stesso
          // dell'ente
          // intemediario dell'utente connesso o che sia stato valorizzato a "S"
          // il
          // FLAG_FIGLIO che è valorizzato in questo modo SE E SOLO SE l'ente
          // che ha
          // la delega è un figlio dell'ente il cui codice fiscale è stato
          // passato
          // al metodo serviceGetMandato
          if (!codiceIntermediario.equalsIgnoreCase(delegaAnagrafeVO.getCodiceFiscIntermediario()) && !SolmrConstants.FLAG_S.equalsIgnoreCase(delegaAnagrafeVO.getFlagFiglio()))
          {
            return AnagErrors.ERRORE_AUT_UTENTE_SENZA_MANDATO;
          }
          else
          {
            return null;
          }
        }
        else
        {
          // Se non c'è la delega ==> Errore
          return AnagErrors.ERRORE_AUT_UTENTE_SENZA_MANDATO;
        }
      }
      else
      {
        return null;
      }
    }
    catch (SolmrException ex)
    {
      return ex.getMessage();
    }
    catch (Exception ex)
    {
      return AnagErrors.ERRORE_DI_SISTEMA;
    }
  }

  /**
   * Valida l'accesso del profilo relativo alla comunità montana
   * 
   * @param anagAziendaVO
   *          AnagAziendaVO
   * @return java.lang.String
   */
  protected String validateComunitaMontana(AnagAziendaVO anagAziendaVO)
  {
    if (this.isCUForReadWrite)
    {
      if (anagAziendaVO.getDataCessazione() != null || anagAziendaVO.getDataCessazioneStr() != null || anagAziendaVO.getDataFineVal() != null)
      {
        return AnagErrors.ERRORE_AUT_AZIENDA_STORICIZZATA_CESSATA;
      }
    }
    return null;
  }

  /**
   * Esegue la validazione standard per gli utenti (in base al tipo di attore).
   * 
   * @param ruoloUtenza
   *          RuoloUtenza
   * @param anagAziendaVO
   *          AnagAziendaVO
   * @param anagFacadeClient
   *          AnagFacadeClient
   * @param request
   *          HttpServletRequest
   * @return java.lang.String
   */
  protected String validateGenericUser(RuoloUtenza ruoloUtenza, AnagAziendaVO anagAziendaVO, AnagFacadeClient anagFacadeClient, HttpServletRequest request)
  {
    if (ruoloUtenza.isUtenteRegionale())
    {
      return validateRegionale(anagAziendaVO);
    }
    if (ruoloUtenza.isUtenteProvinciale())
    {
      return validateProvinciale(ruoloUtenza, anagAziendaVO);
    }
    if (ruoloUtenza.isUtenteOPR())
    {
      return validateOPR(anagAziendaVO);
    }
    if (ruoloUtenza.isUtenteOPRGestore())
    {
      return validateORPGestore(ruoloUtenza, anagFacadeClient, anagAziendaVO);
    }
    if (ruoloUtenza.isUtenteIntermediario())
    {
      SolmrLogger.debug(this, "Sono nel caso di utente intermediario del metodo validateGenericUser in Autorizzazione\n");
      
      String validateCAAStr = validateCAA(ruoloUtenza, anagFacadeClient, anagAziendaVO);
      //Se è valorizzato (senza delega o delega ad un altro caa) devo verificare se il
      //padre (attraverso l'elenco soci ha la delega, in quel caso posso andare avanti
      //lo stesso....
      if(Validator.isNotEmpty(validateCAAStr))
      {
        try
        {
          //Solo se non sono  in ricerca
          if(request.getAttribute("RICERCA") == null)
          {
            //il padre ha la delega 
            if(anagFacadeClient.getDelegaBySocio(ruoloUtenza.getCodiceEnte(), anagAziendaVO.getIdAzienda()))
              return null;
          }
        }
        catch (SolmrException ex)
        {
          return ex.getMessage();
        }
        catch (Exception ex)
        {
          return AnagErrors.ERRORE_DI_DELEGA_SOCI;
        }
      }
      
      return validateCAAStr;
    }
    if (ruoloUtenza.isUtenteComunitaMontana())
    {
      return validateComunitaMontana(anagAziendaVO);
    }
    if (ruoloUtenza.isUtenteTitolareCf() || ruoloUtenza.isUtenteLegaleRappresentante())
    {
      // TITOLARE
      return validateTitolare(anagAziendaVO, ruoloUtenza, request, anagFacadeClient);
    }
    if (ruoloUtenza.isUtenteNonIscrittoCIIA())
    {
      // isUtenteAziendaAgricola
      return validateAziendaAgricola(anagAziendaVO, ruoloUtenza, request, anagFacadeClient);
    }
    return null;
  }

  /**
   * Metodo che si occupa di verificare se esistono le condizioni standard per
   * modificare gli elementi di un'azienda agricola
   * 
   * @param ruoloUtenza
   *          RuoloUtenza
   * @param anagAziendaVO
   *          AnagAziendaVO
   * @return boolean
   */
  public boolean hasPropertiesToModifyAnagrafe(RuoloUtenza ruoloUtenza, AnagAziendaVO anagAziendaVO)
  {
    boolean hasProperties = false;
    if (ruoloUtenza.isReadWrite())
    {
      // Il profilo comunità montana non ha accesso alle operazioni di modifica
      // dei dati dell'azienda
      // menù anagrafica, soggetti collegati, unità produttive
      if (ruoloUtenza.isUtenteComunitaMontana())
      {
        return hasProperties;
      }
      if (!ruoloUtenza.isUtenteProvinciale() || ruoloUtenza.isUtenteProvinciale() && ruoloUtenza.getCodiceEnte() != null && ruoloUtenza.getCodiceEnte().equals(anagAziendaVO.getProvCompetenza())
          || anagAziendaVO.getProvCompetenza() == null)
      {
        if (anagAziendaVO.getDataCessazione() == null && anagAziendaVO.getDataCessazioneStr() == null && anagAziendaVO.getDataFineVal() == null)
        {
          if (ruoloUtenza.isUtenteOPRGestore())
          {
            AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
            MandatoVO mandatoVO = null;
            DelegaAnagrafeVO delegaAnagrafeVO = null;
            try
            {
              mandatoVO = anagFacadeClient.serviceGetMandato(anagAziendaVO.getIdAzienda(), ruoloUtenza.getCodiceEnte());
              if (mandatoVO != null)
              {
                delegaAnagrafeVO = mandatoVO.getDelegaAnagrafe();
                if (delegaAnagrafeVO != null)
                {
                  if (anagAziendaVO.isPossiedeDelegaAttiva()
                      && (ruoloUtenza.getCodiceEnte().equalsIgnoreCase(anagAziendaVO.getDelegaVO().getCodiceFiscaleIntermediario()) || SolmrConstants.FLAG_S.equalsIgnoreCase(delegaAnagrafeVO
                          .getFlagFiglio())))
                  {
                    hasProperties = true;
                  }
                }
              }
            }
            catch (Exception e)
            {
            }
          }
          else
          {
            if (ruoloUtenza.isUtenteIntermediario() || !anagAziendaVO.isPossiedeDelegaAttiva())
            {
              hasProperties = true;
            }
          }
        }
      }
    }
    return hasProperties;
  }
  
  /*
   * Il controllo se l'intermediario può accedere o meno all'azienda è fatto all'inizio 
   * dopo la ricerca. Quindi se sono qua vuol dire che cmq ho accesso ai dati dell'azienda.
   * 
   */
  public boolean hasPropertiesToModifyAnagrafeNew(RuoloUtenza ruoloUtenza, AnagAziendaVO anagAziendaVO, 
      Vector<String> vActor)
  {
    boolean hasProperties = false;
    if (ruoloUtenza.isReadWrite())
    {
      if (anagAziendaVO.getDataCessazione() == null && anagAziendaVO.getDataCessazioneStr() == null && anagAziendaVO.getDataFineVal() == null)
      {
        if(vActor.contains(SolmrConstants.CAA_SCRITTURA))
        {
          hasProperties = true;
        }
        else if(!vActor.contains(SolmrConstants.CAA_SCRITTURA) && !anagAziendaVO.isPossiedeDelegaAttiva())
        {
          hasProperties = true;
        }
      }
    }
    return hasProperties;
  }

}
