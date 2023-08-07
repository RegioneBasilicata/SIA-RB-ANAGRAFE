package it.csi.solmr.business.anag;

import it.csi.csi.wrapper.SystemException;
import it.csi.csi.wrapper.UserException;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaAtecoSecVO;
import it.csi.smranag.smrgaa.integration.AnagrafeGaaDAO;
import it.csi.smrcomms.reportdin.util.Validator;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.StringcodeDescription;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.ParametroRitornoVO;
import it.csi.solmr.dto.anag.TipoSezioniAaepVO;
import it.csi.solmr.dto.anag.UteAtecoSecondariVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.DataControlException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.exception.services.ErrorTypes;
import it.csi.solmr.exception.services.ServiceSystemException;
import it.csi.solmr.integration.CommonDAO;
import it.csi.solmr.integration.anag.AAEPDAO;
import it.csi.solmr.integration.anag.AnagWriteDAO;
import it.csi.solmr.integration.anag.AnagrafeDAO;
import it.csi.solmr.integration.anag.UteDAO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SianConstants;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.ws.infoc.AtecoRI2007Infoc;
import it.csi.solmr.ws.infoc.Azienda;
import it.csi.solmr.ws.infoc.LSICService;
import it.csi.solmr.ws.infoc.LSICServicePortType;
import it.csi.solmr.ws.infoc.ListaSedi;
import it.csi.solmr.ws.infoc.ProcConcors;
import it.csi.solmr.ws.infoc.RappresentanteLegale;
import it.csi.solmr.ws.infoc.Sede;

import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

@Stateless(name="comp/env/solmr/anag/Aaep",mappedName="comp/env/solmr/anag/AAep")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(value=javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
public class AaepBean implements AaepLocal
{
  /**
   * 
   */
  private static final long serialVersionUID = -3647666245886746179L;

  //String aaepPortaDelegataXML = (String) SolmrConstants.get("FILE_PD_AAEP");

  SessionContext sessionContext;
  private transient CommonDAO commonDAO;
  private transient AnagWriteDAO wDAO = null;
  private transient AnagrafeDAO aDAO = null;
  private transient AAEPDAO aaepDAO;
  private transient UteDAO uteDAO;
  private transient AnagrafeGaaDAO aGaaDAO = null;
  
  
  public static LSICServicePortType portStub = null;
  
  static {
	  try{		  
		  String endPointUrl = SianConstants.INFOCAMERE_URL_WS;
		  SolmrLogger.debug("AaepBean", " --- INFOCAMERE_URL_WS ="+endPointUrl);

		  LSICService ss = new LSICService();
		  SolmrLogger.debug("AaepBean", " --- prima di getLSICServiceHttpSoap12Endpoint");
		  portStub = ss.getLSICServiceHttpSoap12Endpoint();

		  BindingProvider bp = (BindingProvider) portStub;

		  Map<String, Object> context = bp.getRequestContext();
		  context.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,endPointUrl);
		}
		catch(Throwable e){
			e.printStackTrace();
		}
	}

  
  // ---  ********** Attenzione : le parti che usano la PD sono poi da rimuovere (al  momento le teniamo per non avere problemi di compilazione nei punti dove vengono chiamati i servizi)
  
  
  // ***************************** INIZIO PARTE CHIAMATA SERVIZI INFOCAMERE WS *******************************************
  
  public Azienda cercaPerCodiceFiscale(String codiceFiscale)throws SolmrException, Exception{
    SolmrLogger.debug(this,"BEGIN cercaPerCodiceFiscale");
    Azienda azienda = null;
	if ("".equals(codiceFiscale) || codiceFiscale == null)
	  throw new SolmrException((String) AnagErrors.get("ERR_AAEP_NO_CODICE_FISCALE"));
	
	try{
	  SolmrLogger.debug(this, "-- codiceFiscale ="+codiceFiscale);
	  azienda = portStub.cercaPerCodiceFiscale(codiceFiscale, SolmrConstants.FONTE_DATO_INFOCAMERE_STR);
	}
	catch (Exception ex){
      SolmrLogger.fatal(this, "cercaPerCodiceFiscale - Exception: "+ ex.getMessage());
      ex.printStackTrace();
      throw new SolmrException((String) AnagErrors.get("ERR_AAEP_TO_CONNECT"));
    }
	finally{
	    	SolmrLogger.debug(this,"END cercaPerCodiceFiscale");
	}
	return azienda;
  }
  
  
  public it.csi.solmr.ws.infoc.Sede cercaPuntualeSede(String codiceFiscale, String progrSede, String codFonte) throws SolmrException, Exception{
	SolmrLogger.debug(this,"BEGIN cercaPuntualeSede");
	it.csi.solmr.ws.infoc.Sede sede = null;
	try{
	  SolmrLogger.debug(this, "-- ** chiamata a cercaPuntualeSede");
	  SolmrLogger.debug(this, "-- codiceFiscale ="+codiceFiscale);
	  SolmrLogger.debug(this, "-- progrSede ="+progrSede);
	  SolmrLogger.debug(this, "-- codFonte ="+codFonte);
	  sede = portStub.cercaPuntualeSede(codiceFiscale,progrSede,codFonte);
    }	
	catch (Exception ex){
	  SolmrLogger.fatal(this, "cercaPuntualeSede - Exception: "+ ex.getMessage());
	  throw new SolmrException((String) AnagErrors.get("ERR_AAEP_TO_CONNECT"));
	}
	finally{
      SolmrLogger.debug(this,"END cercaPuntualeSede");
	}
	return sede;
  }
  
  
  public it.csi.solmr.ws.infoc.PersonaRIInfoc cercaPuntualePersona(String codiceFiscale, String progrPersona, String codFonte) throws SolmrException, Exception {
	    it.csi.solmr.ws.infoc.PersonaRIInfoc persona = null;
	    try{
	      persona = portStub.cercaPuntualePersonaRI(codiceFiscale, progrPersona, codFonte);
	    }	    
	    catch (Exception ex){
	      SolmrLogger.fatal(this, "cercaPuntualePersonaRI - Exception: "+ ex.getMessage());
	      throw new SolmrException((String) AnagErrors.get("ERR_AAEP_TO_CONNECT"));
	    }
	    return persona;
  }
  
  public List<it.csi.solmr.ws.infoc.ListaPersonaCaricaInfoc> cercaPerFiltriCodFiscFonte(String codiceFiscale, String codFonte) throws SolmrException, Exception {
	  List<it.csi.solmr.ws.infoc.ListaPersonaCaricaInfoc> personaList = null;
	  try{
	    personaList = portStub.cercaPerFiltriCodFiscFonte(codiceFiscale, codFonte);
	  }	    
	  catch (Exception ex){
	    SolmrLogger.fatal(this, "cercaPuntualePersonaRI - Exception: "+ ex.getMessage());
	    throw new SolmrException((String) AnagErrors.get("ERR_AAEP_TO_CONNECT"));
	  }
	  return personaList; 
  }
  
  
  
  /**
   * Il metodo seguente viene utilizzato dai servizi Questo serve solo ai
   * servizi
   */
  public ParametroRitornoVO serviceGetAziendeInfocAnagrafe(String codiceFiscale,
      boolean controllaPresenzaSuAAEP, Boolean bloccaAssenzaAAEP,
      boolean controllaLegameRLsuAnagrafe, boolean controllaPresenzaValidazione)
      throws SolmrException, ServiceSystemException, SystemException,
      Exception
  {
    SolmrLogger.debug(this,
        "\n\n\n\nInizio servizio serviceGetAziendeInfocAnagrafe\n\n\n\n");
    boolean controllaPresenzaSuAAEPOLD = controllaPresenzaSuAAEP;
    ParametroRitornoVO parametroRitornoVO = null;
    parametroRitornoVO = new ParametroRitornoVO();
    Vector<Long> idAzienda = new Vector<Long>();
    Vector<String> messaggio = new Vector<String>();
    if ("".equals(codiceFiscale) || codiceFiscale == null)
      throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER,
          ErrorTypes.INVALID_PARAMETER);
    if (controllaPresenzaSuAAEP && bloccaAssenzaAAEP == null)
      throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER,
          ErrorTypes.INVALID_PARAMETER);

    // Se controllaPresenza su AAEP = true
    SolmrLogger.debug(this," -- controllaPresenzaSuAAEP ="+controllaPresenzaSuAAEP);
    if (controllaPresenzaSuAAEP)
    {
      try
      {
        List<it.csi.solmr.ws.infoc.ListaPersonaCaricaInfoc> listaPersonaCaricaInfoc = null;
        try
        {
          listaPersonaCaricaInfoc = portStub.cercaPerFiltriCodFiscFonte(codiceFiscale,"" + (Long) SolmrConstants.get("FONTE_DATO_INFOCAMERE"));
        }
        catch (Exception e)
        {
          SolmrLogger.error(this,"-- Exception in fase di chiamata al servizio cercaPerFiltriCodFiscFonte ="+e.getMessage());
          /**
           * Codice fiscale non trovato
           */
        }

        if (listaPersonaCaricaInfoc == null
            || listaPersonaCaricaInfoc.size() == 0)
        {
          /**
           * Codice fiscale non trovato
           */

          // Se AAEP restituisce una lista vuota allora restituire array vuoto
          // e MSG2 se bloccaAssenzaAAEP=true,
          // se invece bloccaAssenzaAAEP=false concatenare il MSG3 e proseguire
          // con il
          // caso d'uso controllaPresenzaSuAAEP=false
          if (bloccaAssenzaAAEP.booleanValue())
            messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG2"));
          else
          {
            messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG3"));
            controllaPresenzaSuAAEP = false;
          }
        }
        else
        {
          SolmrLogger.debug(this,
              "\n\n\n\n\n\n\nlistaPersonaCaricaInfoc.length() "
                  + listaPersonaCaricaInfoc.size());

          Vector<CodeDescription> result = eliminaCUAADoppi(listaPersonaCaricaInfoc);

          String CUAA[] = new String[result.size()];

          for (int i = 0; i < CUAA.length; i++)
          {
            CodeDescription codeDescription = (CodeDescription) result.get(i);
            CUAA[i] = codeDescription.getCodeFlag();
          }
          // Controlla che le aziende per cui la persona risulta essere rapp leg
          // in AAEP siano:
          // 1. censite in anagrafe
          // 2. censite univocamente in anagrafe
          // 3. non cessate in anagrafe
          // 4. se controllaLegameRLsuAnagrafe =true controllare che la persona
          // sia rappresentante legale di tali aziende in anagrafe (id_ruolo =1)
          // 5. se controllaLegameRLsuAnagrafe =false controllare che la persona
          // abbia un ruolo attivo in anagrafe
          // 6. se controllaPresenzaValidazione=true controllare che sia
          // presente per quell’azienda un record su
          // db_dichiarazione_consistenza con data >= del valore del parametro
          // DICH (su db_parametro)

          for (int i = 0; i < CUAA.length; i++)
          {
            CodeDescription codeDescription[] = commonDAO
                .getIdAziendaCUAA(CUAA[i]);

            if (codeDescription != null && codeDescription.length > 0)
            {
              // Azienda censita
              SolmrLogger.debug(this, "codeDescription.length "
                  + codeDescription.length);

              for (int j = 0; j < codeDescription.length; j++)
              {
                SolmrLogger.debug(this, "codeDescription.getCode "
                    + codeDescription[j].getCode());
                SolmrLogger.debug(this, "codeDescription.getDescription "
                    + codeDescription[j].getDescription());
                SolmrLogger.debug(this, "codeDescription.getSecondaryCode "
                    + codeDescription[j].getSecondaryCode());
              }

              int risp = univocitaCUAAeCessazione(codeDescription);
              if (risp != -1 && risp != -2)
              {
                // Controllo Rappresentante legale o ruolo
                if (commonDAO.isRuolosuAnagrafe(codiceFiscale,
                    codeDescription[risp].getCode().longValue(),
                    controllaLegameRLsuAnagrafe))
                {
                  if (controllaPresenzaValidazione)
                  {
                    if (commonDAO.isPresenzaDichiarazione(codeDescription[risp]
                        .getCode().longValue()))
                      idAzienda.add(new Long(codeDescription[risp].getCode()
                          .longValue()));
                    else
                      messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG9_1")
                          + " " + CUAA[i] + " "
                          + (String) AnagErrors.get("ERR_AAEP_MSG9_2"));
                  }
                  else
                    idAzienda.add(new Long(codeDescription[risp].getCode()
                        .longValue()));
                }
                else
                {
                  if (controllaLegameRLsuAnagrafe)
                    messaggio
                        .add((String) AnagErrors.get("ERR_AAEP_MSG7")
                            + " "
                            + CUAA[i]
                            + " oppure non è abilitato ad operare sull'azienda [MSG7]");
                  else
                    messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG8")
                        + " " + CUAA[i] + " [MSG8]");
                }
              }
              else
              {
                if (risp == -1)
                  // azienda non univoca
                  messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG") + " "
                      + CUAA[i] + " "
                      + (String) AnagErrors.get("ERR_AAEP_MSG5"));
                else
                  // azienda cessata
                  messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG") + " "
                      + CUAA[i] + " "
                      + (String) AnagErrors.get("ERR_AAEP_MSG6"));
              }
            }
            else
            {
              // //Azienda non censita
              messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG") + " "
                  + CUAA[i] + " " + (String) AnagErrors.get("ERR_AAEP_MSG4"));
            }
          }
        }
      }
      catch (DataAccessException dae)
      {
        throw new Exception(dae.getMessage());
      }     
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "serviceGetAziendeInfocAnagrafe - Exception: "
            + ex.getMessage());
        // ex.printStackTrace();
        throw new ServiceSystemException(ErrorTypes.STR_SERVICE_AAEP_EXCEPTION,
            ErrorTypes.SERVICE_AAEP_EXCEPTION);
      }
    }
    /**
     * Non ho usato ELSE perchè all'interno del IF precedente
     * (controllaPresenzaSuAAEP==true) c'è un caso che mi imposta
     * controllaPresenzaSuAAEP=false e quindi in questo caso è giusto che vada a
     * finire in questo ramo.
     */
    if (!controllaPresenzaSuAAEP)
    {
      try
      {
        CodeDescription codeDescription[] = wDAO
            .getRappresentanteLegaleFromIdAnagAzienda(codiceFiscale);

        if (codeDescription != null && codeDescription.length > 0)
        {
          SolmrLogger.debug(this,
              "serviceGetAziendeInfocAnagrafe - codeDescription.length: "
                  + codeDescription.length);
          for (int i = 0; i < codeDescription.length; i++)
          {
            if (controllaLegameRLsuAnagrafe)
            {
              if (codeDescription[i].getSecondaryCode().equals(
                  SolmrConstants.TIPORUOLO_TITOL_RAPPR_LEG)
                  || "S".equalsIgnoreCase(codeDescription[i].getCodeFlag()))
              {
                if (controllaPresenzaValidazione
                    || (controllaPresenzaSuAAEPOLD && !bloccaAssenzaAAEP
                        .booleanValue()))
                {
                  if (commonDAO.isPresenzaDichiarazione(codeDescription[i]
                      .getCode().longValue()))
                    idAzienda.add(new Long(codeDescription[i].getCode()
                        .longValue()));
                  else
                    messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG12_1")
                        + " " + codeDescription[i].getDescription() + " "
                        + (String) AnagErrors.get("ERR_AAEP_MSG12_2"));
                }
                else
                {
                  idAzienda.add(new Long(codeDescription[i].getCode()
                      .longValue()));
                }
              }
              else
              {
                messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG11") + " "
                    + codeDescription[i].getDescription()
                    + " oppure non è abilitato ad operare [MSG11]");
              }
            }
            else
            {
              if (controllaPresenzaValidazione
                  || (controllaPresenzaSuAAEPOLD && !bloccaAssenzaAAEP
                      .booleanValue()))
              {
                if (commonDAO.isPresenzaDichiarazione(codeDescription[i]
                    .getCode().longValue()))
                  idAzienda.add(new Long(codeDescription[i].getCode()
                      .longValue()));
                else
                  messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG12_1")
                      + " " + codeDescription[i].getDescription() + " "
                      + (String) AnagErrors.get("ERR_AAEP_MSG12_2"));
              }
              else
              {
                idAzienda
                    .add(new Long(codeDescription[i].getCode().longValue()));
              }
            }
          }
        }
        else
        {
          parametroRitornoVO.setCodeResult(SolmrConstants.NESSUNA_AZIENDA_CENSITA);
          messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG10"));
        }
      }
      catch (DataAccessException dae)
      {
        throw new Exception(dae.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "serviceGetAziendeInfocAnagrafe - Exception: "
            + ex.getMessage());
        // ex.printStackTrace();
        throw new ServiceSystemException(ErrorTypes.STR_UNKNOWN_ERROR,
            ErrorTypes.UNKNOWN_ERROR);
      }
    }
    /*
     * Imposto i valori di ritorno
     * 
     */
    parametroRitornoVO.setMessaggio(messaggio.size() == 0 ? null
        : (String[]) messaggio.toArray(new String[0]));
    parametroRitornoVO.setIdAzienda(idAzienda.size() == 0 ? null
        : (Long[]) idAzienda.toArray(new Long[0]));
    SolmrLogger.debug(this,
        "\n\n\n\nFine servizio serviceGetAziendeInfocAnagrafe\n\n\n\n");
    return parametroRitornoVO;
  }

  
  /**
   * Il metodo seguente viene utilizzato dai servizi Questo serve solo ai
   * servizi
   */
  public ParametroRitornoVO serviceGetAziendeInfocAnagrafe(String codiceFiscale,
      boolean controllaPresenzaSuAAEP, Boolean bloccaAssenzaAAEP,
      Boolean controllaLegameRLsuAnagrafe,
      boolean controllaPresenzaValidazione, boolean aziendeAttive)
      throws SolmrException, ServiceSystemException, SystemException,
      Exception
  {
    SolmrLogger.debug(this,
        "\n\n\n\nInizio servizio serviceGetAziendeInfocAnagrafe \n\n\n\n");
    boolean controllaPresenzaSuAAEPOLD = controllaPresenzaSuAAEP;
    ParametroRitornoVO parametroRitornoVO = null;
    parametroRitornoVO = new ParametroRitornoVO();
    Vector<Long> idAzienda = new Vector<Long>();
    Vector<String> messaggio = new Vector<String>();
    StringcodeDescription ruoliAAEP[] = null;
    Vector<StringcodeDescription> vRuoliAAEP = new Vector<StringcodeDescription>();
    if ("".equals(codiceFiscale) || codiceFiscale == null)
      throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER,
          ErrorTypes.INVALID_PARAMETER);
    if (controllaPresenzaSuAAEP && bloccaAssenzaAAEP == null)
      throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER,
          ErrorTypes.INVALID_PARAMETER);

    // Se controllaPresenza su AAEP = true
    SolmrLogger.debug(this,"--- controllaPresenzaSuAAEP ="+controllaPresenzaSuAAEP);
    if (controllaPresenzaSuAAEP)
    {
      try
      {
        List<it.csi.solmr.ws.infoc.ListaPersonaCaricaInfoc> listaPersonaCaricaInfoc = null;
        try
        {
          listaPersonaCaricaInfoc = portStub.cercaPerFiltriCodFiscFonte(codiceFiscale,"" + (Long) SolmrConstants.get("FONTE_DATO_INFOCAMERE"));
        }
        catch (Exception e)
        {
          SolmrLogger.debug(this,"--- Exception in fase di chiamata al servizio cercaPerFiltriCodFiscFonte "+e.getMessage());
          /**
           * Codice fiscale non trovato
           */
        }

        if (listaPersonaCaricaInfoc == null
            || listaPersonaCaricaInfoc.size() == 0)
        {
          /**
           * Codice fiscale non trovato
           */

          // Se AAEP restituisce una lista vuota allora restituire array vuoto
          // e MSG2 se bloccaAssenzaAAEP=true,
          // se invece bloccaAssenzaAAEP=false concatenare il MSG3 e proseguire
          // con il
          // caso d'uso controllaPresenzaSuAAEP=false
          if (bloccaAssenzaAAEP.booleanValue())
            messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG2"));
          else
          {
            messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG3"));
            controllaPresenzaSuAAEP = false;
          }
        }
        else
        {
          SolmrLogger.debug(this,
              "\n\n\n\n\n\n\nlistaPersonaCaricaInfoc.length() "
                  + listaPersonaCaricaInfoc.size());

          Vector<CodeDescription> result = eliminaCUAADoppi(listaPersonaCaricaInfoc);

          ruoliAAEP = new StringcodeDescription[result.size()];
          String CUAA[] = new String[result.size()];

          for (int i = 0; i < CUAA.length; i++)
          {
            CodeDescription codeDescription = (CodeDescription) result.get(i);
            CUAA[i] = codeDescription.getCodeFlag();
            ruoliAAEP[i] = new StringcodeDescription();
            ruoliAAEP[i].setCode(codeDescription.getSecondaryCode());
            ruoliAAEP[i].setDescription(codeDescription.getDescription());
          }

          // Controlla che le aziende per cui la persona risulta essere rapp leg
          // in AAEP siano:
          // 1. censite in anagrafe
          // 2. censite univocamente in anagrafe
          // 3. non cessate in anagrafe
          // 4. se controllaLegameRLsuAnagrafe =true controllare che la persona
          // sia rappresentante legale di tali aziende in anagrafe (id_ruolo =1)
          // 5. se controllaLegameRLsuAnagrafe =false controllare che la persona
          // abbia un ruolo attivo in anagrafe
          // 6. se controllaPresenzaValidazione=true controllare che sia
          // presente per quell’azienda un record su
          // db_dichiarazione_consistenza con data >= del valore del parametro
          // DICH (su db_parametro)

          for (int i = 0; i < CUAA.length; i++)
          {
            CodeDescription codeDescription[] = commonDAO
                .getIdAziendaCUAA(CUAA[i]);

            if (codeDescription != null && codeDescription.length > 0)
            {
              // Azienda censita
              SolmrLogger.debug(this, "codeDescription.length "
                  + codeDescription.length);

              for (int j = 0; j < codeDescription.length; j++)
              {
                SolmrLogger.debug(this, "codeDescription.getCode "
                    + codeDescription[j].getCode());
                SolmrLogger.debug(this, "codeDescription.getDescription "
                    + codeDescription[j].getDescription());
                SolmrLogger.debug(this, "codeDescription.getSecondaryCode "
                    + codeDescription[j].getSecondaryCode());
              }

              int risp = cessazioneCUAA(codeDescription);
              if (risp == -1 && aziendeAttive)
              {
                messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG") + " "
                    + CUAA[i] + " " + (String) AnagErrors.get("ERR_AAEP_MSG6"));
              }
              else
              {
                // Ciclo su tutte le aziende
                for (int j = 0; j < codeDescription.length; j++)
                {
                  boolean cessata = (codeDescription[j].getDescription() != null && !""
                      .equals(codeDescription[j].getDescription()));

                  // se l'utente ha selezionato l'opzione aziende attive devo
                  // escludere le aziende cessate
                  if (!aziendeAttive || (aziendeAttive && !cessata))
                  {

                    // Controllo Rappresentante legale o ruolo
                    if (controllaLegameRLsuAnagrafe == null
                        || commonDAO.isRuolosuAnagrafe(codiceFiscale,
                            codeDescription[j].getCode().longValue(),
                            controllaLegameRLsuAnagrafe.booleanValue()))
                    {
                      if (controllaPresenzaValidazione)
                      {
                        if (commonDAO
                            .isPresenzaDichiarazione(codeDescription[j]
                                .getCode().longValue()))
                        {
                          idAzienda.add(new Long(codeDescription[j].getCode()
                              .longValue()));
                          vRuoliAAEP.add(ruoliAAEP[i]);
                        }
                        else
                          messaggio.add((String) AnagErrors
                              .get("ERR_AAEP_MSG9_1")
                              + " "
                              + CUAA[i]
                              + " "
                              + (String) AnagErrors.get("ERR_AAEP_MSG9_2"));
                      }
                      else
                      {
                        idAzienda.add(new Long(codeDescription[j].getCode()
                            .longValue()));
                        vRuoliAAEP.add(ruoliAAEP[i]);
                      }
                    }
                    else
                    {
                      if (controllaLegameRLsuAnagrafe.booleanValue())
                        messaggio
                            .add((String) AnagErrors.get("ERR_AAEP_MSG7")
                                + " "
                                + CUAA[i]
                                + " oppure non è abilitato ad operare sull'azienda [MSG7]");
                      else
                        messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG8")
                            + " " + CUAA[i] + " [MSG8]");
                    }
                  }
                }
              }
            }
            else
            {
              // //Azienda non censita
              messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG") + " "
                  + CUAA[i] + " " + (String) AnagErrors.get("ERR_AAEP_MSG4"));
            }
          }
        }
      }
      catch (DataAccessException dae)
      {
        throw new Exception(dae.getMessage());
      }      
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "serviceGetAziendeInfocAnagrafe - Exception: "
            + ex.getMessage());
        // ex.printStackTrace();
        throw new ServiceSystemException(ErrorTypes.STR_SERVICE_AAEP_EXCEPTION,
            ErrorTypes.SERVICE_AAEP_EXCEPTION);
      }
    }
    /**
     * Non ho usato ELSE perchè all'interno del IF precedente
     * (controllaPresenzaSuAAEP==true) c'è un caso che mi imposta
     * controllaPresenzaSuAAEP=false e quindi in questo caso è giusto che vada a
     * finire in questo ramo.
     */
    if (!controllaPresenzaSuAAEP)
    {
      try
      {
        CodeDescription codeDescription[] = wDAO
            .getRappresentanteLegaleFromIdAnagAzienda(codiceFiscale,
                aziendeAttive);

        if (codeDescription != null && codeDescription.length > 0)
        {
          SolmrLogger.debug(this,
              "serviceGetAziendeInfocAnagrafe - codeDescription.length: "
                  + codeDescription.length);
          for (int i = 0; i < codeDescription.length; i++)
          {
            if (controllaLegameRLsuAnagrafe != null
                && controllaLegameRLsuAnagrafe.booleanValue())
            {
              if (codeDescription[i].getSecondaryCode().equals(
                  SolmrConstants.TIPORUOLO_TITOL_RAPPR_LEG)
                  || "S".equalsIgnoreCase(codeDescription[i].getCodeFlag()))
              {
                if (controllaPresenzaValidazione
                    || (controllaPresenzaSuAAEPOLD && !bloccaAssenzaAAEP
                        .booleanValue()))
                {
                  if (commonDAO.isPresenzaDichiarazione(codeDescription[i]
                      .getCode().longValue()))
                    idAzienda.add(new Long(codeDescription[i].getCode()
                        .longValue()));
                  else
                    messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG12_1")
                        + " " + codeDescription[i].getDescription() + " "
                        + (String) AnagErrors.get("ERR_AAEP_MSG12_2"));
                }
                else
                {
                  idAzienda.add(new Long(codeDescription[i].getCode()
                      .longValue()));
                }
              }
              else
              {
                messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG11") + " "
                    + codeDescription[i].getDescription()
                    + " oppure non è abilitato ad operare [MSG11]");
              }
            }
            else
            {
              if (controllaPresenzaValidazione
                  || (controllaPresenzaSuAAEPOLD && !bloccaAssenzaAAEP
                      .booleanValue()))
              {
                if (commonDAO.isPresenzaDichiarazione(codeDescription[i]
                    .getCode().longValue()))
                  idAzienda.add(new Long(codeDescription[i].getCode()
                      .longValue()));
                else
                  messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG12_1")
                      + " " + codeDescription[i].getDescription() + " "
                      + (String) AnagErrors.get("ERR_AAEP_MSG12_2"));
              }
              else
              {
                idAzienda
                    .add(new Long(codeDescription[i].getCode().longValue()));
              }
            }
          }
        }
        else
        {
          messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG10"));
        }
      }
      catch (DataAccessException dae)
      {
        throw new Exception(dae.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "serviceGetAziendeInfocAnagrafe - Exception: "
            + ex.getMessage());
        // ex.printStackTrace();
        throw new ServiceSystemException(ErrorTypes.STR_UNKNOWN_ERROR,
            ErrorTypes.UNKNOWN_ERROR);
      }
    }
    /*
     * Imposto i valori di ritorno
     * 
     */
    parametroRitornoVO.setMessaggio(messaggio.size() == 0 ? null
        : (String[]) messaggio.toArray(new String[0]));
    parametroRitornoVO.setIdAzienda(idAzienda.size() == 0 ? null
        : (Long[]) idAzienda.toArray(new Long[0]));
    parametroRitornoVO.setRuoloAAEP(vRuoliAAEP.size() == 0 ? null
        : (StringcodeDescription[]) vRuoliAAEP
            .toArray(new StringcodeDescription[0]));
    SolmrLogger.debug(this,
        "\n\n\n\nFine servizio serviceGetAziendeInfocAnagrafe\n\n\n\n");
    return parametroRitornoVO;
  }

  
  
  // ***************************** FINE PARTE CHIAMATA SERVIZI INFOCAMERE WS *******************************************
  
 
  @Resource
  public void setSessionContext(SessionContext sessionContext)
  {
    this.sessionContext = sessionContext;
    try
    {
      initializeDAO();
    }
    catch (Exception ex)
    {
    }
  }

  private void initializeDAO() throws EJBException
  {
    try
    {
      commonDAO = new CommonDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      wDAO = new AnagWriteDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      aDAO = new AnagrafeDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      aaepDAO = new AAEPDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      uteDAO = new UteDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      aGaaDAO = new AnagrafeGaaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
    }
    catch (ResourceAccessException ex)
    {
      SolmrLogger.fatal(this, ex.getMessage());
      throw new EJBException(ex);
    }
  }

  /**
   * 
   * @throws Exception
   */
 /* private AAEPInterface2 getAaepClient() throws Exception
  {
    // caricamento porta delegata
    // informazioniPortaDelegata = PDConfigReader.read(aaepPortaDelegataXML);
    InfoPortaDelegata informazioniPortaDelegata = PDConfigReader
        .read(getClass().getResourceAsStream(aaepPortaDelegataXML));
    // SolmrLogger.debug(this, "HO LE INFO ! " + informazioniPortaDelegata);
    // creazione proxy di PD
    return (AAEPInterface2) PDProxy.newInstance(informazioniPortaDelegata);
  }*/
  

 /* public AziendaAAEP cercaPerCodiceFiscaleAAEP(String codiceFiscale, String comune,
      String siglaProv) throws SystemException, SolmrException, Exception
  {
    AziendaAAEP azienda = null;
    if ("".equals(codiceFiscale) || codiceFiscale == null)
      throw new SolmrException((String) AnagErrors
          .get("ERR_AAEP_NO_CODICE_FISCALE"));
    try
    {
      azienda = getAaepClient().cercaPerCodiceFiscaleAAEP(codiceFiscale, comune,
          siglaProv);
    }
    catch (UserException usex)
    {
      
      CharSequence charSequence = "Nessun record"; 
      //if ((usex.getMessage().trim().substring(0, 13))
        //  .equalsIgnoreCase(("Nessun record").trim()))
      //{
      if(usex.getMessage().contains(charSequence))
      {        
        //Codice fiscale non trovato
        SolmrLogger.error(this, "cercaPerCodiceFiscaleAAEP - No data found");
        throw new SolmrException((String) AnagErrors
            .get("ERR_AAEP_NO_ANAGRAFICA"));
      }
      else
      {
        
        //Errore generico
        SolmrLogger.error(this,
            "cercaPerCodiceFiscaleAAEP - ERRORE AAEP GENERICO: "
                + usex.getMessage());
        throw new SolmrException((String) AnagErrors.get("ERR_AAEP_GENERICO"));
      }
    }
    catch (java.io.IOException ioex)
    {
      SolmrLogger.fatal(this, "cercaPerCodiceFiscaleAAEP - IOException: "
          + ioex.getMessage());
      throw new SolmrException((String) AnagErrors.get("ERR_AAEP_TO_CONNECT"));
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "cercaPerCodiceFiscaleAAEP - Exception: "
          + ex.getMessage());
      throw new SolmrException((String) AnagErrors.get("ERR_AAEP_TO_CONNECT"));
    }
    return azienda;
  }
  */

  /**
   * I campi in input obbligatori per effettuare la ricerca sono codice fiscale
   * e codice fonte (InfoCamere=3). - Restituisce un oggetto ImpresaInfoc.
   */
 /* ImpresaInfoc cercaPerCodiceFiscaleINFOC(String codiceFiscale,
      String codFonte, String cognome, String codIstatComune, String siglaProvUL)
      throws SystemException, SolmrException, Exception
  {
    if ("".equals(codiceFiscale) || codiceFiscale == null)
      throw new SolmrException((String) AnagErrors
          .get("ERR_AAEP_NO_CODICE_FISCALE"));
    ImpresaInfoc azienda = null;
    try
    {
      azienda = getAaepClient().cercaPerCodiceFiscaleINFOC(codiceFiscale,
          codFonte, cognome, codIstatComune, siglaProvUL);
    }
    catch (UserException usex)
    {
      if ((usex.getMessage().trim().substring(0, 13))
          .equalsIgnoreCase(("Nessun record").trim()))
      {
        
        //Codice fiscale non trovato
        SolmrLogger.error(this,
            "cercaPerCodiceFiscaleINFOC - No data found");
        throw new SolmrException((String) AnagErrors
            .get("ERR_AAEP_NO_ANAGRAFICA"));
      }
      else
      {
        
        //Errore generico
        SolmrLogger.error(this,
            "cercaPerCodiceFiscaleINFOC - ERRORE AAEP GENERICO: "
                + usex.getMessage());
        throw new SolmrException((String) AnagErrors.get("ERR_AAEP_GENERICO"));
      }
    }
    catch (java.io.IOException ioex)
    {
      SolmrLogger.fatal(this, "cercaPerCodiceFiscaleINFOC - IOException: "
          + ioex.getMessage());
      throw new SolmrException((String) AnagErrors.get("ERR_AAEP_TO_CONNECT"));
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "cercaPerCodiceFiscaleINFOC - Exception: "
          + ex.getMessage());
      throw new SolmrException((String) AnagErrors.get("ERR_AAEP_TO_CONNECT"));
    }
    return azienda;
  }*/

  /**
   * I campi in input obbligatori per effettuare la ricerca sono codice fiscale
   * e codice fonte (InfoCamere=3). - Restituisce un oggetto ImpresaInfoc.
   */
 /* public PersonaRIInfoc cercaPuntualePersonaRI(String idAAEPAz,
      String idAAEPFonteDato, String progrPe) throws SystemException,
      SolmrException, Exception
  {
    PersonaRIInfoc persona = null;
    try
    {
      persona = getAaepClient().cercaPuntualePersonaRI(idAAEPAz,
          idAAEPFonteDato, progrPe);
    }
    catch (UserException usex)
    {
      
      //Errore generico
      SolmrLogger.error(this, "cercaPuntualePersonaRI - ERRORE AAEP GENERICO: "
          + usex.getMessage());
      throw new SolmrException((String) AnagErrors.get("ERR_AAEP_GENERICO"));
    }
    catch (java.io.IOException ioex)
    {
      SolmrLogger.fatal(this, "cercaPuntualePersonaRI - IOException: "
          + ioex.getMessage());
      throw new SolmrException((String) AnagErrors.get("ERR_AAEP_TO_CONNECT"));
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "cercaPuntualePersonaRI - Exception: "
          + ex.getMessage());
      throw new SolmrException((String) AnagErrors.get("ERR_AAEP_TO_CONNECT"));
    }
    return persona;
  }*/

  // Metodo che va su AAEP per ottenere le property della sede assocaiata
  // all'azienda
 /* Sede cercaPuntualeSedeAAEP(String idAAEPAzienda, String idAAEPSede,
      String dataInizioVal) throws SystemException, SolmrException,
      Exception
  {
    Sede sede = null;
    try
    {
      sede = getAaepClient().cercaPuntualeSedeAAEP(idAAEPAzienda, idAAEPSede,
          dataInizioVal);
    }
    catch (UserException ue)
    {
      SolmrLogger.fatal(this, "cercaPuntualeSedeAAEP - UserException: "
          + ue.getMessage());
      throw new SolmrException((String) AnagErrors.get("ERR_AAEP_TO_CONNECT"));
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "cercaPuntualeSedeAAEP - Exception: "
          + ex.getMessage());
      throw new SolmrException((String) AnagErrors.get("ERR_AAEP_TO_CONNECT"));
    }
    return sede;
  }*/
  
 /* public SedeInfocamere cercaPuntualeSedeInfoc(String idAAEPAzienda, String idAAEPSede,
      String dataInizioVal) throws SystemException, SolmrException,
      Exception
  {
    SedeInfocamere sede = null;
    try
    {
      sede = getAaepClient().cercaPuntualeSedeInfoc(idAAEPAzienda, idAAEPSede,
          dataInizioVal);
    }
    catch (UserException ue)
    {
      SolmrLogger.fatal(this, "cercaPuntualeSedeInfoc - UserException: "
          + ue.getMessage());
      throw new SolmrException((String) AnagErrors.get("ERR_AAEP_TO_CONNECT"));
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "cercaPuntualeSedeInfoc - Exception: "
          + ex.getMessage());
      throw new SolmrException((String) AnagErrors.get("ERR_AAEP_TO_CONNECT"));
    }
    return sede;
  }*/
  
  /**
   * Questo metodo viene utilizzato per recuperare i dati dell'azienda di AAEP e
   * aggiornare i dati provenienti da AAEP sulle tabelle DB_AZIENDA_AAEP,
   * DB_SEDE_AAEP, DB_RAPPRESENTANTE_LEGALE_AAEP
   */
  public Boolean serviceAggiornaDatiAAEP(String cuaa) throws SolmrException,
      ServiceSystemException, SystemException, Exception
  {
	SolmrLogger.debug(this, "BEGIN serviceAggiornaDatiAAEP");  
    Azienda aziendaAAEP = null;
    Azienda impresaInfoc = null;
    
    
    if ("".equals(cuaa) || cuaa == null)
      throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER,
          ErrorTypes.INVALID_PARAMETER);
    try
    {
      cuaa = cuaa.trim().toUpperCase();
      
      SolmrLogger.debug(this, "-- cuaa ="+cuaa);
      
      SolmrLogger.debug(this, "-- PRIMA DI chiamata al servizio INFOCAMERE cercaPerCodiceFiscale");
      aziendaAAEP = portStub.cercaPerCodiceFiscale(cuaa, SolmrConstants.FONTE_DATO_INFOCAMERE_STR);
      SolmrLogger.debug(this, "-- DOPO DI chiamata al servizio INFOCAMERE cercaPerCodiceFiscale");
      
      //Controllo che il VO restituito non valga null (anche se in teoria
      //questa situazione non dovrebbe mai verificarsi)
      if (aziendaAAEP == null)
      {
    	SolmrLogger.debug(this, "-- aziendaAAEP == null");
        aaepDAO.aggiornaDatiAAEP(cuaa);
        return new Boolean(false);
      }

      
      //Controllo che il dato che mi viene restituito provenga da infocamere,
      //altrimenti non mi interessa
      /*if(!SolmrConstants.FONTE_DATO_INFOCAMERE_STR.equalsIgnoreCase(aziendaAAEP.getIdFonteDato()))
      {
        aaepDAO.aggiornaDatiAAEP(cuaa);
        return new Boolean(false);
      }*/
      
      //Vado a leggere i dati del titolare/rappresentante legale
      SolmrLogger.debug(this, "-- Vado a leggere i dati del titolare/rappresentante legale");
      if(aziendaAAEP.getRappresentanteLegale() == null)
    	  SolmrLogger.debug(this, "-- rapresentanteLegale e' NULL!!!");
      RappresentanteLegale rappresentanteLegaleAAEP = aziendaAAEP.getRappresentanteLegale().getValue();

      
      //Mi faccio restituire la lista delle sedi legali
      List<ListaSedi> sedeRidottaAAEP = aziendaAAEP.getListaSedi();
      Sede sedeAAEP[] = null;

      if (sedeRidottaAAEP != null && sedeRidottaAAEP.size()>0)
      {
    	SolmrLogger.debug(this, "-- sedeRidottaAAEP.size() ="+sedeRidottaAAEP.size());
        sedeAAEP = new Sede[sedeRidottaAAEP.size()];
        for (int i = 0; i < sedeRidottaAAEP.size(); i++){          
          SolmrLogger.debug(this, "-- aziendaAAEP.getCodiceFiscale() ="+aziendaAAEP.getCodiceFiscale());
          if(sedeRidottaAAEP.get(i) != null){
            SolmrLogger.debug(this, "-- sedeRidottaAAEP.get(i).getProgrSede() ="+sedeRidottaAAEP.get(i).getProgrSede());
            sedeAAEP[i] = cercaPuntualeSede(aziendaAAEP.getCodiceFiscale().getValue(), sedeRidottaAAEP.get(i).getProgrSede().getValue(), SolmrConstants.FONTE_DATO_INFOCAMERE_STR);
          }  
          else
        	SolmrLogger.debug(this, "-- sedeRidottaAAEP.get(i) e' NULL!!!, non chiamo cercaPuntualeSede");            
        }  
      }
           
      List<ProcConcors> procConcorsInfoc = null;
      if(Validator.isNotEmpty(aziendaAAEP))
      {
    	SolmrLogger.debug(this, "-- getListaProcConcors");
        procConcorsInfoc = aziendaAAEP.getListaProcConcors();
      }
      
      //A questo punto vado ad inserire i dati sul DB
      SolmrLogger.debug(this, "-- vado ad inserire i dati sul DB");
      aaepDAO.aggiornaDatiAAEP(aziendaAAEP, rappresentanteLegaleAAEP, sedeAAEP, procConcorsInfoc,cuaa);
    }
    catch (UserException usex)
    {      
      SolmrLogger.error(this, "--UserException ="+usex.getMessage());
      if (usex.getMessage().contains("Nessun record"))
      {
        try
        {
          aaepDAO.aggiornaDatiAAEP(cuaa);
        }
        catch (DataAccessException e)
        {
          throw new Exception(e.getMessage());
        }
        return new Boolean(false);
      }
      else
      {
        
        //L'azienda trovata ha un numero di sedi superire a 520
        throw new SolmrException(ErrorTypes.STR_MAX_RECORD,
            ErrorTypes.MAX_RECORD);
      }
    }
    catch (DataAccessException e)
    {
      SolmrLogger.error(this, "--DataAccessException ="+e.getMessage());	
      throw new Exception(e.getMessage());
    }
    catch (java.io.IOException ioex)
    {
      SolmrLogger.fatal(this, "serviceAggiornaDatiAAEP - IOException: "
          + ioex.getMessage());
      throw new ServiceSystemException(ErrorTypes.STR_SERVICE_AAEP_EXCEPTION,
          ErrorTypes.SERVICE_AAEP_EXCEPTION);
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "serviceAggiornaDatiAAEP - Exception: "
          + ex.getMessage());
      throw new ServiceSystemException(ErrorTypes.STR_SERVICE_AAEP_EXCEPTION,
          ErrorTypes.SERVICE_AAEP_EXCEPTION);
    }
    finally{
    	SolmrLogger.debug(this, "END serviceAggiornaDatiAAEP");
    }
    return new Boolean(true);
  }

  

  /**
   * Il metodo seguente viene utilizzato dai servizi Questo serve solo ai
   * servizi
   * 
   * Riscritto per via di un comportamento anomalo di AAEP L'assenza di
   * rappresentanti legali ha portato all'esclusione temporanea della chiamata
   * ad AAEP.
   */
  public ParametroRitornoVO serviceGetAziendeAAEPAnagrafe(String codiceFiscale)
      throws SolmrException, ServiceSystemException, SystemException,
      Exception
  {
    SolmrLogger.debug(this,
        "\n\n\n\nInizio servizio serviceGetAziendeAAEPAnagrafe\n\n\n\n");
    ParametroRitornoVO parametroRitornoVO = null;
    parametroRitornoVO = new ParametroRitornoVO();
    Vector<Long> idAzienda = new Vector<Long>();
    Vector<String> messaggio = new Vector<String>();
    if ("".equals(codiceFiscale) || codiceFiscale == null)
      throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER,
          ErrorTypes.INVALID_PARAMETER);
    try
    {
      // idAzienda =
      // anagAziendaRemote.serviceGetAziendeByCfPersona(codiceFiscale, new
      // Long(1), true);
      idAzienda = wDAO.serviceGetAziendeByCfPersona(codiceFiscale, new Long(1),
          true);

      if (idAzienda == null || idAzienda.size() == 0)
        messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG10"));
    }
    catch (DataControlException e)
    {
      messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG10"));
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "serviceGetAziendeAAEPAnagrafe - Exception: "
          + ex.getMessage());
      // ex.printStackTrace();
      throw new ServiceSystemException(ErrorTypes.STR_UNKNOWN_ERROR,
          ErrorTypes.UNKNOWN_ERROR);
    }

   
    parametroRitornoVO.setMessaggio(messaggio.size() == 0 ? null
        : (String[]) messaggio.toArray(new String[0]));
    parametroRitornoVO.setIdAzienda(idAzienda.size() == 0 ? null
        : (Long[]) idAzienda.toArray(new Long[0]));
    SolmrLogger.debug(this,
        "\n\n\n\nFine servizio serviceGetAziendeAAEPAnagrafe\n\n\n\n");
    return parametroRitornoVO;
  }
  
  /**
   * Il metodo seguente viene utilizzato dai servizi Questo serve solo ai
   * servizi
   */
 /* public ParametroRitornoVO serviceGetAziendeAAEPAnagrafe(String codiceFiscale,
      boolean controllaPresenzaSuAAEP, Boolean bloccaAssenzaAAEP,
      boolean controllaLegameRLsuAnagrafe, boolean controllaPresenzaValidazione)
      throws SolmrException, ServiceSystemException, SystemException,
      Exception
  {
    SolmrLogger.debug(this,
        "\n\n\n\nInizio servizio serviceGetAziendeAAEPAnagrafe\n\n\n\n");
    boolean controllaPresenzaSuAAEPOLD = controllaPresenzaSuAAEP;
    ParametroRitornoVO parametroRitornoVO = null;
    parametroRitornoVO = new ParametroRitornoVO();
    Vector<Long> idAzienda = new Vector<Long>();
    Vector<String> messaggio = new Vector<String>();
    if ("".equals(codiceFiscale) || codiceFiscale == null)
      throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER,
          ErrorTypes.INVALID_PARAMETER);
    if (controllaPresenzaSuAAEP && bloccaAssenzaAAEP == null)
      throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER,
          ErrorTypes.INVALID_PARAMETER);

    // Se controllaPresenza su AAEP = true
    if (controllaPresenzaSuAAEP)
    {
      try
      {
        ListaPersonaCaricaInfoc listaPersonaCaricaInfoc[] = null;
        try
        {
          listaPersonaCaricaInfoc = getAaepClient().cercaPerFiltriCodFiscFonte(
              codiceFiscale,
              "" + (Long) SolmrConstants.get("FONTE_DATO_INFOCAMERE"));
        }
        catch (UserException usex)
        {
          
        }

        if (listaPersonaCaricaInfoc == null
            || listaPersonaCaricaInfoc.length == 0)
        {
          

          // Se AAEP restituisce una lista vuota allora restituire array vuoto
          // e MSG2 se bloccaAssenzaAAEP=true,
          // se invece bloccaAssenzaAAEP=false concatenare il MSG3 e proseguire
          // con il
          // caso d'uso controllaPresenzaSuAAEP=false
          if (bloccaAssenzaAAEP.booleanValue())
            messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG2"));
          else
          {
            messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG3"));
            controllaPresenzaSuAAEP = false;
          }
        }
        else
        {
          SolmrLogger.debug(this,
              "\n\n\n\n\n\n\nlistaPersonaCaricaInfoc.length() "
                  + listaPersonaCaricaInfoc.length);

          Vector<CodeDescription> result = eliminaCUAADoppiAaep(listaPersonaCaricaInfoc);

          String CUAA[] = new String[result.size()];

          for (int i = 0; i < CUAA.length; i++)
          {
            CodeDescription codeDescription = (CodeDescription) result.get(i);
            CUAA[i] = codeDescription.getCodeFlag();
          }
          // Controlla che le aziende per cui la persona risulta essere rapp leg
          // in AAEP siano:
          // 1. censite in anagrafe
          // 2. censite univocamente in anagrafe
          // 3. non cessate in anagrafe
          // 4. se controllaLegameRLsuAnagrafe =true controllare che la persona
          // sia rappresentante legale di tali aziende in anagrafe (id_ruolo =1)
          // 5. se controllaLegameRLsuAnagrafe =false controllare che la persona
          // abbia un ruolo attivo in anagrafe
          // 6. se controllaPresenzaValidazione=true controllare che sia
          // presente per quell’azienda un record su
          // db_dichiarazione_consistenza con data >= del valore del parametro
          // DICH (su db_parametro)

          for (int i = 0; i < CUAA.length; i++)
          {
            CodeDescription codeDescription[] = commonDAO
                .getIdAziendaCUAA(CUAA[i]);

            if (codeDescription != null && codeDescription.length > 0)
            {
              // Azienda censita
              SolmrLogger.debug(this, "codeDescription.length "
                  + codeDescription.length);

              for (int j = 0; j < codeDescription.length; j++)
              {
                SolmrLogger.debug(this, "codeDescription.getCode "
                    + codeDescription[j].getCode());
                SolmrLogger.debug(this, "codeDescription.getDescription "
                    + codeDescription[j].getDescription());
                SolmrLogger.debug(this, "codeDescription.getSecondaryCode "
                    + codeDescription[j].getSecondaryCode());
              }

              int risp = univocitaCUAAeCessazione(codeDescription);
              if (risp != -1 && risp != -2)
              {
                // Controllo Rappresentante legale o ruolo
                if (commonDAO.isRuolosuAnagrafe(codiceFiscale,
                    codeDescription[risp].getCode().longValue(),
                    controllaLegameRLsuAnagrafe))
                {
                  if (controllaPresenzaValidazione)
                  {
                    if (commonDAO.isPresenzaDichiarazione(codeDescription[risp]
                        .getCode().longValue()))
                      idAzienda.add(new Long(codeDescription[risp].getCode()
                          .longValue()));
                    else
                      messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG9_1")
                          + " " + CUAA[i] + " "
                          + (String) AnagErrors.get("ERR_AAEP_MSG9_2"));
                  }
                  else
                    idAzienda.add(new Long(codeDescription[risp].getCode()
                        .longValue()));
                }
                else
                {
                  if (controllaLegameRLsuAnagrafe)
                    messaggio
                        .add((String) AnagErrors.get("ERR_AAEP_MSG7")
                            + " "
                            + CUAA[i]
                            + " oppure non è abilitato ad operare sull'azienda [MSG7]");
                  else
                    messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG8")
                        + " " + CUAA[i] + " [MSG8]");
                }
              }
              else
              {
                if (risp == -1)
                  // azienda non univoca
                  messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG") + " "
                      + CUAA[i] + " "
                      + (String) AnagErrors.get("ERR_AAEP_MSG5"));
                else
                  // azienda cessata
                  messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG") + " "
                      + CUAA[i] + " "
                      + (String) AnagErrors.get("ERR_AAEP_MSG6"));
              }
            }
            else
            {
              // //Azienda non censita
              messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG") + " "
                  + CUAA[i] + " " + (String) AnagErrors.get("ERR_AAEP_MSG4"));
            }
          }
        }
      }
      catch (DataAccessException dae)
      {
        throw new Exception(dae.getMessage());
      }
      catch (java.io.IOException ioex)
      {
        SolmrLogger.fatal(this,
            "serviceGetAziendeAAEPAnagrafe - SQLException: "
                + ioex.getMessage());
        // ioex.printStackTrace();
        throw new ServiceSystemException(ErrorTypes.STR_SERVICE_AAEP_EXCEPTION,
            ErrorTypes.SERVICE_AAEP_EXCEPTION);
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "serviceGetAziendeAAEPAnagrafe - Exception: "
            + ex.getMessage());
        // ex.printStackTrace();
        throw new ServiceSystemException(ErrorTypes.STR_SERVICE_AAEP_EXCEPTION,
            ErrorTypes.SERVICE_AAEP_EXCEPTION);
      }
    }
    
    if (!controllaPresenzaSuAAEP)
    {
      try
      {
        CodeDescription codeDescription[] = wDAO
            .getRappresentanteLegaleFromIdAnagAzienda(codiceFiscale);

        if (codeDescription != null && codeDescription.length > 0)
        {
          SolmrLogger.debug(this,
              "serviceGetAziendeAAEPAnagrafe - codeDescription.length: "
                  + codeDescription.length);
          for (int i = 0; i < codeDescription.length; i++)
          {
            if (controllaLegameRLsuAnagrafe)
            {
              if (codeDescription[i].getSecondaryCode().equals(
                  SolmrConstants.TIPORUOLO_TITOL_RAPPR_LEG)
                  || "S".equalsIgnoreCase(codeDescription[i].getCodeFlag()))
              {
                if (controllaPresenzaValidazione
                    || (controllaPresenzaSuAAEPOLD && !bloccaAssenzaAAEP
                        .booleanValue()))
                {
                  if (commonDAO.isPresenzaDichiarazione(codeDescription[i]
                      .getCode().longValue()))
                    idAzienda.add(new Long(codeDescription[i].getCode()
                        .longValue()));
                  else
                    messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG12_1")
                        + " " + codeDescription[i].getDescription() + " "
                        + (String) AnagErrors.get("ERR_AAEP_MSG12_2"));
                }
                else
                {
                  idAzienda.add(new Long(codeDescription[i].getCode()
                      .longValue()));
                }
              }
              else
              {
                messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG11") + " "
                    + codeDescription[i].getDescription()
                    + " oppure non è abilitato ad operare [MSG11]");
              }
            }
            else
            {
              if (controllaPresenzaValidazione
                  || (controllaPresenzaSuAAEPOLD && !bloccaAssenzaAAEP
                      .booleanValue()))
              {
                if (commonDAO.isPresenzaDichiarazione(codeDescription[i]
                    .getCode().longValue()))
                  idAzienda.add(new Long(codeDescription[i].getCode()
                      .longValue()));
                else
                  messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG12_1")
                      + " " + codeDescription[i].getDescription() + " "
                      + (String) AnagErrors.get("ERR_AAEP_MSG12_2"));
              }
              else
              {
                idAzienda
                    .add(new Long(codeDescription[i].getCode().longValue()));
              }
            }
          }
        }
        else
        {
          parametroRitornoVO.setCodeResult(SolmrConstants.NESSUNA_AZIENDA_CENSITA);
          messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG10"));
        }
      }
      catch (DataAccessException dae)
      {
        throw new Exception(dae.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "serviceGetAziendeAAEPAnagrafe - Exception: "
            + ex.getMessage());
        // ex.printStackTrace();
        throw new ServiceSystemException(ErrorTypes.STR_UNKNOWN_ERROR,
            ErrorTypes.UNKNOWN_ERROR);
      }
    }
   
    parametroRitornoVO.setMessaggio(messaggio.size() == 0 ? null
        : (String[]) messaggio.toArray(new String[0]));
    parametroRitornoVO.setIdAzienda(idAzienda.size() == 0 ? null
        : (Long[]) idAzienda.toArray(new Long[0]));
    SolmrLogger.debug(this,
        "\n\n\n\nFine servizio serviceGetAziendeAAEPAnagrafe\n\n\n\n");
    return parametroRitornoVO;
  }*/
  /**
   * Il metodo seguente viene utilizzato dai servizi Questo serve solo ai
   * servizi
   */
 /* public ParametroRitornoVO serviceGetAziendeAAEPAnagrafe(String codiceFiscale,
      boolean controllaPresenzaSuAAEP, Boolean bloccaAssenzaAAEP,
      Boolean controllaLegameRLsuAnagrafe,
      boolean controllaPresenzaValidazione, boolean aziendeAttive)
      throws SolmrException, ServiceSystemException, SystemException,
      Exception
  {
    SolmrLogger.debug(this,
        "\n\n\n\nInizio servizio serviceGetAziendeAAEPAnagrafe\n\n\n\n");
    boolean controllaPresenzaSuAAEPOLD = controllaPresenzaSuAAEP;
    ParametroRitornoVO parametroRitornoVO = null;
    parametroRitornoVO = new ParametroRitornoVO();
    Vector<Long> idAzienda = new Vector<Long>();
    Vector<String> messaggio = new Vector<String>();
    StringcodeDescription ruoliAAEP[] = null;
    Vector<StringcodeDescription> vRuoliAAEP = new Vector<StringcodeDescription>();
    if ("".equals(codiceFiscale) || codiceFiscale == null)
      throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER,
          ErrorTypes.INVALID_PARAMETER);
    if (controllaPresenzaSuAAEP && bloccaAssenzaAAEP == null)
      throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER,
          ErrorTypes.INVALID_PARAMETER);

    // Se controllaPresenza su AAEP = true
    if (controllaPresenzaSuAAEP)
    {
      try
      {
        ListaPersonaCaricaInfoc listaPersonaCaricaInfoc[] = null;
        try
        {
          listaPersonaCaricaInfoc = getAaepClient().cercaPerFiltriCodFiscFonte(
              codiceFiscale,
              "" + (Long) SolmrConstants.get("FONTE_DATO_INFOCAMERE"));
        }
        catch (UserException usex)
        {
          
        }

        if (listaPersonaCaricaInfoc == null
            || listaPersonaCaricaInfoc.length == 0)
        {
          

          // Se AAEP restituisce una lista vuota allora restituire array vuoto
          // e MSG2 se bloccaAssenzaAAEP=true,
          // se invece bloccaAssenzaAAEP=false concatenare il MSG3 e proseguire
          // con il
          // caso d'uso controllaPresenzaSuAAEP=false
          if (bloccaAssenzaAAEP.booleanValue())
            messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG2"));
          else
          {
            messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG3"));
            controllaPresenzaSuAAEP = false;
          }
        }
        else
        {
          SolmrLogger.debug(this,
              "\n\n\n\n\n\n\nlistaPersonaCaricaInfoc.length() "
                  + listaPersonaCaricaInfoc.length);

          Vector<CodeDescription> result = eliminaCUAADoppiAaep(listaPersonaCaricaInfoc);

          ruoliAAEP = new StringcodeDescription[result.size()];
          String CUAA[] = new String[result.size()];

          for (int i = 0; i < CUAA.length; i++)
          {
            CodeDescription codeDescription = (CodeDescription) result.get(i);
            CUAA[i] = codeDescription.getCodeFlag();
            ruoliAAEP[i] = new StringcodeDescription();
            ruoliAAEP[i].setCode(codeDescription.getSecondaryCode());
            ruoliAAEP[i].setDescription(codeDescription.getDescription());
          }

          // Controlla che le aziende per cui la persona risulta essere rapp leg
          // in AAEP siano:
          // 1. censite in anagrafe
          // 2. censite univocamente in anagrafe
          // 3. non cessate in anagrafe
          // 4. se controllaLegameRLsuAnagrafe =true controllare che la persona
          // sia rappresentante legale di tali aziende in anagrafe (id_ruolo =1)
          // 5. se controllaLegameRLsuAnagrafe =false controllare che la persona
          // abbia un ruolo attivo in anagrafe
          // 6. se controllaPresenzaValidazione=true controllare che sia
          // presente per quell’azienda un record su
          // db_dichiarazione_consistenza con data >= del valore del parametro
          // DICH (su db_parametro)

          for (int i = 0; i < CUAA.length; i++)
          {
            CodeDescription codeDescription[] = commonDAO
                .getIdAziendaCUAA(CUAA[i]);

            if (codeDescription != null && codeDescription.length > 0)
            {
              // Azienda censita
              SolmrLogger.debug(this, "codeDescription.length "
                  + codeDescription.length);

              for (int j = 0; j < codeDescription.length; j++)
              {
                SolmrLogger.debug(this, "codeDescription.getCode "
                    + codeDescription[j].getCode());
                SolmrLogger.debug(this, "codeDescription.getDescription "
                    + codeDescription[j].getDescription());
                SolmrLogger.debug(this, "codeDescription.getSecondaryCode "
                    + codeDescription[j].getSecondaryCode());
              }

              int risp = cessazioneCUAA(codeDescription);
              if (risp == -1 && aziendeAttive)
              {
                messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG") + " "
                    + CUAA[i] + " " + (String) AnagErrors.get("ERR_AAEP_MSG6"));
              }
              else
              {
                // Ciclo su tutte le aziende
                for (int j = 0; j < codeDescription.length; j++)
                {
                  boolean cessata = (codeDescription[j].getDescription() != null && !""
                      .equals(codeDescription[j].getDescription()));

                  // se l'utente ha selezionato l'opzione aziende attive devo
                  // escludere le aziende cessate
                  if (!aziendeAttive || (aziendeAttive && !cessata))
                  {

                    // Controllo Rappresentante legale o ruolo
                    if (controllaLegameRLsuAnagrafe == null
                        || commonDAO.isRuolosuAnagrafe(codiceFiscale,
                            codeDescription[j].getCode().longValue(),
                            controllaLegameRLsuAnagrafe.booleanValue()))
                    {
                      if (controllaPresenzaValidazione)
                      {
                        if (commonDAO
                            .isPresenzaDichiarazione(codeDescription[j]
                                .getCode().longValue()))
                        {
                          idAzienda.add(new Long(codeDescription[j].getCode()
                              .longValue()));
                          vRuoliAAEP.add(ruoliAAEP[i]);
                        }
                        else
                          messaggio.add((String) AnagErrors
                              .get("ERR_AAEP_MSG9_1")
                              + " "
                              + CUAA[i]
                              + " "
                              + (String) AnagErrors.get("ERR_AAEP_MSG9_2"));
                      }
                      else
                      {
                        idAzienda.add(new Long(codeDescription[j].getCode()
                            .longValue()));
                        vRuoliAAEP.add(ruoliAAEP[i]);
                      }
                    }
                    else
                    {
                      if (controllaLegameRLsuAnagrafe.booleanValue())
                        messaggio
                            .add((String) AnagErrors.get("ERR_AAEP_MSG7")
                                + " "
                                + CUAA[i]
                                + " oppure non è abilitato ad operare sull'azienda [MSG7]");
                      else
                        messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG8")
                            + " " + CUAA[i] + " [MSG8]");
                    }
                  }
                }
              }
            }
            else
            {
              // //Azienda non censita
              messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG") + " "
                  + CUAA[i] + " " + (String) AnagErrors.get("ERR_AAEP_MSG4"));
            }
          }
        }
      }
      catch (DataAccessException dae)
      {
        throw new Exception(dae.getMessage());
      }
      catch (java.io.IOException ioex)
      {
        SolmrLogger.fatal(this,
            "serviceGetAziendeAAEPAnagrafe - SQLException: "
                + ioex.getMessage());
        // ioex.printStackTrace();
        throw new ServiceSystemException(ErrorTypes.STR_SERVICE_AAEP_EXCEPTION,
            ErrorTypes.SERVICE_AAEP_EXCEPTION);
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "serviceGetAziendeAAEPAnagrafe - Exception: "
            + ex.getMessage());
        // ex.printStackTrace();
        throw new ServiceSystemException(ErrorTypes.STR_SERVICE_AAEP_EXCEPTION,
            ErrorTypes.SERVICE_AAEP_EXCEPTION);
      }
    }
    
    if (!controllaPresenzaSuAAEP)
    {
      try
      {
        CodeDescription codeDescription[] = wDAO
            .getRappresentanteLegaleFromIdAnagAzienda(codiceFiscale,
                aziendeAttive);

        if (codeDescription != null && codeDescription.length > 0)
        {
          SolmrLogger.debug(this,
              "serviceGetAziendeAAEPAnagrafe - codeDescription.length: "
                  + codeDescription.length);
          for (int i = 0; i < codeDescription.length; i++)
          {
            if (controllaLegameRLsuAnagrafe != null
                && controllaLegameRLsuAnagrafe.booleanValue())
            {
              if (codeDescription[i].getSecondaryCode().equals(
                  SolmrConstants.TIPORUOLO_TITOL_RAPPR_LEG)
                  || "S".equalsIgnoreCase(codeDescription[i].getCodeFlag()))
              {
                if (controllaPresenzaValidazione
                    || (controllaPresenzaSuAAEPOLD && !bloccaAssenzaAAEP
                        .booleanValue()))
                {
                  if (commonDAO.isPresenzaDichiarazione(codeDescription[i]
                      .getCode().longValue()))
                    idAzienda.add(new Long(codeDescription[i].getCode()
                        .longValue()));
                  else
                    messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG12_1")
                        + " " + codeDescription[i].getDescription() + " "
                        + (String) AnagErrors.get("ERR_AAEP_MSG12_2"));
                }
                else
                {
                  idAzienda.add(new Long(codeDescription[i].getCode()
                      .longValue()));
                }
              }
              else
              {
                messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG11") + " "
                    + codeDescription[i].getDescription()
                    + " oppure non è abilitato ad operare [MSG11]");
              }
            }
            else
            {
              if (controllaPresenzaValidazione
                  || (controllaPresenzaSuAAEPOLD && !bloccaAssenzaAAEP
                      .booleanValue()))
              {
                if (commonDAO.isPresenzaDichiarazione(codeDescription[i]
                    .getCode().longValue()))
                  idAzienda.add(new Long(codeDescription[i].getCode()
                      .longValue()));
                else
                  messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG12_1")
                      + " " + codeDescription[i].getDescription() + " "
                      + (String) AnagErrors.get("ERR_AAEP_MSG12_2"));
              }
              else
              {
                idAzienda
                    .add(new Long(codeDescription[i].getCode().longValue()));
              }
            }
          }
        }
        else
        {
          messaggio.add((String) AnagErrors.get("ERR_AAEP_MSG10"));
        }
      }
      catch (DataAccessException dae)
      {
        throw new Exception(dae.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "serviceGetAziendeAAEPAnagrafe - Exception: "
            + ex.getMessage());
        // ex.printStackTrace();
        throw new ServiceSystemException(ErrorTypes.STR_UNKNOWN_ERROR,
            ErrorTypes.UNKNOWN_ERROR);
      }
    }
    
    parametroRitornoVO.setMessaggio(messaggio.size() == 0 ? null
        : (String[]) messaggio.toArray(new String[0]));
    parametroRitornoVO.setIdAzienda(idAzienda.size() == 0 ? null
        : (Long[]) idAzienda.toArray(new Long[0]));
    parametroRitornoVO.setRuoloAAEP(vRuoliAAEP.size() == 0 ? null
        : (StringcodeDescription[]) vRuoliAAEP
            .toArray(new StringcodeDescription[0]));
    SolmrLogger.debug(this,
        "\n\n\n\nFine servizio serviceGetAziendeAAEPAnagrafe\n\n\n\n");
    return parametroRitornoVO;
  }
  */
  
  
  /**
   * Questo metodo viene utilizzato per corregere un baco dei servizi di AAEP
   * che a volte restituiscono dei CUAA doppi
   */
 /* private Vector<CodeDescription> eliminaCUAADoppiAaep(
      ListaPersonaCaricaInfoc listaPersonaCaricaInfoc[])
  {

    Vector<CodeDescription> result = new Vector<CodeDescription>();
    Hashtable<String,CodeDescription> map = new Hashtable<String,CodeDescription>();
    for (int i = 0; i < listaPersonaCaricaInfoc.length; i++)
    {
      SolmrLogger.debug(this, "\n\n");
      SolmrLogger.debug(this, "getCodFiscaleAzienda() "
          + listaPersonaCaricaInfoc[i].getCodFiscaleAzienda().getValue());
      SolmrLogger.debug(this, "getCodFiscalePersona() "
          + listaPersonaCaricaInfoc[i].getCodFiscalePersona().getValue());
      SolmrLogger.debug(this, "getCodiceCarica() "
          + listaPersonaCaricaInfoc[i].getCodiceCarica().getValue());
      SolmrLogger.debug(this, "getDescrCarica() "
          + listaPersonaCaricaInfoc[i].getDescrCarica().getValue());     
      SolmrLogger.debug(this, "getDataFineCarica() "
          + listaPersonaCaricaInfoc[i].getDataFineCarica());
      CodeDescription codeDescription = new CodeDescription();
      String CUAA = listaPersonaCaricaInfoc[i].getCodFiscaleAzienda();
      codeDescription.setCodeFlag(CUAA);
      codeDescription.setSecondaryCode(listaPersonaCaricaInfoc[i]
          .getCodiceCarica());
      codeDescription.setDescription(listaPersonaCaricaInfoc[i]
          .getDescrCarica());
      map.put(CUAA, codeDescription);
    }

    Collection<CodeDescription> collection = map.values();
    Iterator<CodeDescription> iterator = collection.iterator();

    while (iterator.hasNext())
      result.add(iterator.next());

    if (result.size() == 0)
      return null;
    return result;

  }*/

  public CodeDescription getAttivitaATECObyCode(String codiceAteco)
      throws Exception
  {
    try
    {
      return commonDAO.getAttivitaATECObyCode(codiceAteco);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  public CodeDescription getAttivitaATECObyCodeParametroCATE(String codiceAteco)
      throws Exception
  {
    try
    {
      return commonDAO.getAttivitaATECObyCodeParametroCATE(codiceAteco);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  public String isFlagPrevalente(Long[] idAteco) throws Exception
  {
    try
    {
      return aaepDAO.isFlagPrevalente(idAteco);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public TipoSezioniAaepVO getTipoSezioneAaepByCodiceSez(String codiceSezione)
      throws Exception
  {
    try
    {
      return aaepDAO.getTipoSezioneAaepByCodiceSez(codiceSezione);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }

  }

  public void importaUteAAEP(AnagAziendaVO anagAziendaVO,
      Sede[] sedeInfocamere, String idParametri[], Long idUtenteAggiornamento)
      throws Exception
  {
    try
    {
      if (sedeInfocamere != null)
      {
        int num = 0;
        for (int i = 0; i < sedeInfocamere.length; i++)
        {
          if (("" + i).equals(idParametri[num]))
          {
            // valore selezionato
            if (num < idParametri.length - 1)
              num++;
            List<AtecoRI2007Infoc> atecoRI2007Infoc = sedeInfocamere[i].getListaAtecoRI2007Infoc();
            Integer idAteco = null;
            CodeDescription ateco = null;
            boolean consideraPI = false;
            if (atecoRI2007Infoc != null)
            {
              int k = 0;
              boolean cicla = true;
              String codiceAteco = null;
              while (k < atecoRI2007Infoc.size() && cicla)
              {
                if ("P".equalsIgnoreCase(atecoRI2007Infoc.get(k).getCodImportanzaRI().getValue()))
                  cicla = false;
                else
                  k++;
              }
              if (!cicla)
              {
                codiceAteco = atecoRI2007Infoc.get(k).getCodAteco2007().getValue();
                if(Validator.isNotEmpty(codiceAteco))
                {
                  codiceAteco = codiceAteco.trim();
                  codiceAteco = codiceAteco.replaceAll("\\.", "");
                }
                
                ateco = commonDAO.getAttivitaATECObyCodeParametroCATE(codiceAteco);
              }
              
              if(Validator.isEmpty(codiceAteco))
              {
                consideraPI = true;
                k = 0;
                cicla = true;
                while (k < atecoRI2007Infoc.size() && cicla)
                {
                  if ("PI".equalsIgnoreCase(atecoRI2007Infoc.get(k).getCodImportanzaRI().getValue()))
                    cicla = false;
                  else
                    k++;
                }
                if (!cicla)
                {
                  codiceAteco = atecoRI2007Infoc.get(k).getCodAteco2007().getValue();
                  if(Validator.isNotEmpty(codiceAteco))
                  {
                    codiceAteco = codiceAteco.trim();
                    codiceAteco = codiceAteco.replaceAll("\\.", "");
                  }
                  
                  ateco = commonDAO.getAttivitaATECObyCodeParametroCATE(codiceAteco);
                }
                
                
              }
              
              
              
              
            }

            if (sedeInfocamere[i].getDescrStatoEstero().getValue() != null)
            {
              try
              {
            	String codComune = commonDAO.getIstatByDescComune(sedeInfocamere[i].getDescrStatoEstero().getValue());
            	
            	QName codcomuneQName = new QName("http://servizio.frontend.ls.com", "codComune");
            	JAXBElement<String> codComuneValue = new JAXBElement<String>(codcomuneQName, String.class, codComune);
                sedeInfocamere[i].setCodComune(codComuneValue);
              }
              catch (Exception e)
              {
              }
            }
            ComuneVO comune = null;
            if (sedeInfocamere[i].getCodComune().getValue() != null)
            {
              try
              {
                comune = commonDAO.getComuneByISTAT(sedeInfocamere[i].getCodComune().getValue());
              }
              catch (Exception e)
              {
              }
            }
            //E' impossibile che accade perchè filtrato nella jsp però....
            if(ateco !=null)
            {
              idAteco = ateco.getCode();
            }
            Long idUte = wDAO.insertUTEforAAEP(anagAziendaVO, sedeInfocamere[i],
                idUtenteAggiornamento, idAteco, comune);            
            //importa ateco secondari...
            
            //Ateco Secondari
            Vector<String> vCodtecoSec = new Vector<String>();
            Vector<Date> vDataAtecoSec = new Vector<Date>();
            if (atecoRI2007Infoc != null)
            {
              Vector<AziendaAtecoSecVO> vAzienaAtecoSec = aGaaDAO.getListActiveAziendaAtecoSecByIdAzienda(anagAziendaVO.getIdAzienda());
              
              for(int g=0;g<atecoRI2007Infoc.size();g++)
              {
                if(consideraPI)
                {
                  if (!"PI".equalsIgnoreCase(atecoRI2007Infoc.get(g).getCodImportanzaRI().getValue()))
                  {
                    if(!vCodtecoSec.contains(atecoRI2007Infoc.get(g).getCodAteco2007().getValue()))
                    {
                      vCodtecoSec.add(atecoRI2007Infoc.get(g).getCodAteco2007().getValue());
                      if(Validator.isNotEmpty(atecoRI2007Infoc.get(g).getDataInizioAteco2007().getValue()))
                        vDataAtecoSec.add(DateUtils.convert(atecoRI2007Infoc.get(g).getDataInizioAteco2007().getValue()));
                      else
                        vDataAtecoSec.add(new Date());
                    }
                  }
                }
                else
                {
                  if (!"P".equalsIgnoreCase(atecoRI2007Infoc.get(g).getCodImportanzaRI().getValue()))
                  {
                    if(!vCodtecoSec.contains(atecoRI2007Infoc.get(g).getCodAteco2007().getValue()))
                    {
                      vCodtecoSec.add(atecoRI2007Infoc.get(g).getCodAteco2007().getValue());
                      if(Validator.isNotEmpty(atecoRI2007Infoc.get(g).getDataInizioAteco2007().getValue()))
                        vDataAtecoSec.add(DateUtils.convert(atecoRI2007Infoc.get(g).getDataInizioAteco2007().getValue()));
                      else
                        vDataAtecoSec.add(new Date());
                    }
                  }
                }
              
              }
              
              
              for(int g=0;g<vCodtecoSec.size();g++)
              {
                String codiceAteco = vCodtecoSec.get(g).trim();
                codiceAteco = codiceAteco.replaceAll("\\.","");
                CodeDescription cdAteco = commonDAO.getAttivitaATECObyCodeParametroCATE(codiceAteco);
                if (cdAteco!=null)
                {
                  UteAtecoSecondariVO uteAtecoSecondariVO = new UteAtecoSecondariVO();
                  uteAtecoSecondariVO.setIdUte(idUte);
                  uteAtecoSecondariVO.setIdAttivitaAteco(new Long(cdAteco.getCode().longValue()));
                  uteAtecoSecondariVO.setDataInizioValidita(vDataAtecoSec.get(g));
                  uteDAO.insertUteAtecoSecondari(uteAtecoSecondariVO);
                  
                  //Controllo che non sia già presente nell'azienda eventualmente lo aggiungo!!
                  if(vAzienaAtecoSec == null)
                  {
                    //Aggiungo tutto
                    AziendaAtecoSecVO aziendaAtecoSecVO = new AziendaAtecoSecVO();
                    aziendaAtecoSecVO.setIdAzienda(anagAziendaVO.getIdAzienda());
                    aziendaAtecoSecVO.setIdAttivitaAteco(new Long(cdAteco.getCode().longValue()));
                    aziendaAtecoSecVO.setDataInizioValidita(vDataAtecoSec.get(g));
                    aGaaDAO.insertAziendaAtecoSec(aziendaAtecoSecVO);
                  }
                  else
                  {
                    //Controllo se nn c'e' aggiungo!!!
                    Boolean trovato = false;
                    for(int h=0;h<vAzienaAtecoSec.size();h++)
                    {
                      if(vAzienaAtecoSec.get(h).getIdAttivitaAteco() == new Long(cdAteco.getCode().longValue()))
                      {
                        trovato = true;
                        break;
                      }                      
                    }
                    
                    if(!trovato)
                    {
                      AziendaAtecoSecVO aziendaAtecoSecVO = new AziendaAtecoSecVO();
                      aziendaAtecoSecVO.setIdAzienda(anagAziendaVO.getIdAzienda());
                      aziendaAtecoSecVO.setIdAttivitaAteco(new Long(cdAteco.getCode().longValue()));
                      aziendaAtecoSecVO.setDataInizioValidita(vDataAtecoSec.get(g));
                      aGaaDAO.insertAziendaAtecoSec(aziendaAtecoSecVO);                      
                    }
                    
                  }
                  
                } 
                
                
              }
            }
          }
        }
      }
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(ex.getMessage());
    }
  }

  /*
   * restituisce gli ateco_new corrispondenti all'id_attivita_ateco dell'azienda
   */
  public CodeDescription[] getElencoAtecoNew(String codiceAteco, Long idAzienda)
      throws Exception
  {
    CodeDescription[] result = null;
    try
    {
      CodeDescription cd = commonDAO
          .getAttivitaATECObyCodeParametroCATE(codiceAteco);
      
      if(Validator.isNotEmpty(cd))
      {
        CodeDescription[] cdList = aaepDAO.getAttivitaATECONewByOld(cd.getCode());
        if (cdList != null)
        {
          Long[] idAteco = new Long[cdList.length];
          for (int i = 0; i < cdList.length; i++)
          {
            idAteco[i] = cdList[i].getCode().longValue();
          }
  
          result = aDAO.getListAtecoInAnag(idAteco, idAzienda);
          // CASO 1: il codiceAteco presente sul record in db_anagrafica_azienda
          // non è presente
          // nell'insieme di ateco_new (cdList) quindi devo visualizzare l'elenco
          // di ateco_new
          if (result == null)
          {
            result = commonDAO.getAttivitaATECOById(idAteco);
            // return result;
          }
          /*
           * else if(result.length==1) return result;
           */
        }
      }
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(ex.getMessage());
    }
    return result;
  }

  /**
   * Controlla se l'azienda trovata è cessata, è univoca oppure se è tutto OK.
   * 
   * @param codeDescription
   * @return -1:
   */
  private int cessazioneCUAA(CodeDescription codeDescription[])
  {
    int result = 0;
    if (codeDescription.length == 1)
    {
      if (!(codeDescription[0].getDescription() == null || ""
          .equals(codeDescription[0].getDescription())))
        result = -1;
    }
    else
    {
      if (!(codeDescription[codeDescription.length - 1].getDescription() == null || ""
          .equals(codeDescription[codeDescription.length - 1].getDescription())))
        result = -1;
    }
    return result;
  }

  /**
   * Controlla se l'azienda trovata è cessata, è univoca oppure se è tutto OK.
   * 
   * @param codeDescription
   * @return -1: azienda non univoca -2: azienda cessata altri valori contengono
   *         la posizione all'interno del vettore che contiene l'idAzienda
   */
  private int univocitaCUAAeCessazione(CodeDescription codeDescription[])
  {
    int result = 0;
    if (codeDescription.length == 1)
    {
      if (!(codeDescription[0].getDescription() == null || ""
          .equals(codeDescription[0].getDescription())))
        result = -2;
      else
        result = 0;
    }
    else
    {
      if (codeDescription[codeDescription.length - 2].getDescription() == null
          || "".equals(codeDescription[codeDescription.length - 2]
              .getDescription()))
      {
        result = -1;
      }
      else
      {
        if (!(codeDescription[codeDescription.length - 1].getDescription() == null || ""
            .equals(codeDescription[codeDescription.length - 1]
                .getDescription())))
          result = -2;
        else
          result = codeDescription.length - 1;
      }
    }
    return result;
  }

  /**
   * Questo metodo viene utilizzato per corregere un baco dei servizi di AAEP
   * che a volte restituiscono dei CUAA doppi
   */
  private Vector<CodeDescription> eliminaCUAADoppi(List<it.csi.solmr.ws.infoc.ListaPersonaCaricaInfoc> listaPersonaCaricaInfoc)
  {

    Vector<CodeDescription> result = new Vector<CodeDescription>();
    Hashtable<String,CodeDescription> map = new Hashtable<String,CodeDescription>();
    for (int i = 0; i < listaPersonaCaricaInfoc.size(); i++)
    {
      SolmrLogger.debug(this, "\n\n");
      SolmrLogger.debug(this, "getCodFiscaleAzienda() "+ listaPersonaCaricaInfoc.get(i).getCodFiscaleAzienda().getValue());
      SolmrLogger.debug(this, "getCodFiscalePersona() "+ listaPersonaCaricaInfoc.get(i).getCodFiscalePersona().getValue());
      SolmrLogger.debug(this, "getCodiceCarica() "+ listaPersonaCaricaInfoc.get(i).getCodiceCarica().getValue());
      SolmrLogger.debug(this, "getDescrCarica() "+ listaPersonaCaricaInfoc.get(i).getDescrCarica().getValue());
      SolmrLogger.debug(this, "getDataFineCarica() "+ listaPersonaCaricaInfoc.get(i).getDataFineCarica().getValue());
      CodeDescription codeDescription = new CodeDescription();
      String CUAA = listaPersonaCaricaInfoc.get(i).getCodFiscaleAzienda().getValue();
      codeDescription.setCodeFlag(CUAA);
      codeDescription.setSecondaryCode(listaPersonaCaricaInfoc.get(i).getCodiceCarica().getValue());
      codeDescription.setDescription(listaPersonaCaricaInfoc.get(i).getDescrCarica().getValue());
      map.put(CUAA, codeDescription);
    }

    Collection<CodeDescription> collection = map.values();
    Iterator<CodeDescription> iterator = collection.iterator();

    while (iterator.hasNext())
      result.add(iterator.next());

    if (result.size() == 0)
      return null;
    return result;

  }
  
  
 /* public PostaCertificata getPostaElettronicaCertificata(String idAAEPAz) throws SystemException,
      SolmrException, Exception
  {
    PostaCertificata postaCertificata = null;
    try
    {
      postaCertificata = getAaepClient().getPostaElettronicaCertificata(idAAEPAz);
    }
    catch (UserException usex)
    {
      
      //Errore generico
      SolmrLogger.error(this, "getPostaElettronicaCertificata - ERRORE AAEP GENERICO: "
          + usex.getMessage());
      throw new SolmrException((String) AnagErrors.get("ERR_AAEP_GENERICO"));
    }
    catch (java.io.IOException ioex)
    {
      SolmrLogger.fatal(this, "getPostaElettronicaCertificata - IOException: "
          + ioex.getMessage());
      throw new SolmrException((String) AnagErrors.get("ERR_AAEP_TO_CONNECT"));
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getPostaElettronicaCertificata - Exception: "
          + ex.getMessage());
      throw new SolmrException((String) AnagErrors.get("ERR_AAEP_TO_CONNECT"));
    }
    return postaCertificata;
  }*/
}
