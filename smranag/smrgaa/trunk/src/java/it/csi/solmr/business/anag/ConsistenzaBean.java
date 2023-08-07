package it.csi.solmr.business.anag;

import it.csi.smranag.smrgaa.dto.AllegatoDichiarazioneVO;
import it.csi.smranag.smrgaa.dto.AllegatoDocumentoVO;
import it.csi.smranag.smrgaa.dto.PlSqlCodeDescription;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.integration.AnagrafeGaaDAO;
import it.csi.smranag.smrgaa.integration.DocumentoGaaDAO;
import it.csi.smranag.smrgaa.integration.StampaGaaDAO;
import it.csi.smranag.smrgaa.integration.terreni.ParticellaGaaDAO;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.ConsistenzaVO;
import it.csi.solmr.dto.anag.ErrAnomaliaDicConsistenzaVO;
import it.csi.solmr.dto.anag.EsitoPianoGraficoVO;
import it.csi.solmr.dto.anag.TemporaneaPraticaAziendaVO;
import it.csi.solmr.dto.anag.consistenza.FascicoloNazionaleVO;
import it.csi.solmr.dto.anag.consistenza.NumeroProtocolloGtfoVO;
import it.csi.solmr.dto.anag.consistenza.TipoControlloVO;
import it.csi.solmr.dto.anag.consistenza.TipoMotivoDichiarazioneVO;
import it.csi.solmr.dto.anag.terreni.DichiarazioneSegnalazioneVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.exception.services.ErrorTypes;
import it.csi.solmr.integration.CommonDAO;
import it.csi.solmr.integration.anag.ConsistenzaDAO;
import it.csi.solmr.integration.anag.DichiarazioneSegnalazioneDAO;
import it.csi.solmr.integration.anag.NumeroProtocolloGtfoDAO;
import it.csi.solmr.integration.anag.TipoControlloDAO;
import it.csi.solmr.integration.anag.TipoMotivoDichiarazioneDAO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.naming.InitialContext;

@Stateless(name="comp/env/solmr/anag/Consistenza",mappedName="comp/env/solmr/anag/Consistenza")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(value=javax.ejb.TransactionAttributeType.REQUIRED)
public class ConsistenzaBean implements ConsistenzaLocal
{

  private static final long                      serialVersionUID             = 2361070349803309657L;

  SimpleDateFormat                               format                       = new SimpleDateFormat(
                                                                                  "dd/MM/yyyy HH:mm:ss");
  SessionContext                                 sessionContext;
  private transient ConsistenzaDAO               consistenzaDAO               = null;
  private transient CommonDAO                    cDAO                         = null;
  private transient DichiarazioneSegnalazioneDAO dichiarazioneSegnalazioneDAO = null;
  private transient TipoMotivoDichiarazioneDAO   tipoMotivoDichiarazioneDAO   = null;
  private transient TipoControlloDAO             tipoControlloDAO             = null;
  private transient AnagrafeGaaDAO               anagrafeGaaDAO               = null;
  private transient NumeroProtocolloGtfoDAO      numeroProtocolloGtfoDAO      = null;
  private transient ParticellaGaaDAO             particellaGaaDAO             = null;
  //private transient NuovaIscrizioneDAO           nuovaIscrizioneDAO           = null;
  private transient DocumentoGaaDAO              documentoGaaDAO              = null;
  private transient StampaGaaDAO                 stampaGaaDAO              = null;

  
  @EJB
  DocumentoLocal documentoLocal = null;

 
  @Resource
  public void setSessionContext(SessionContext sessionContext)
  {
    this.sessionContext = sessionContext;
    initializeDAO();
  }

  private void initializeDAO() throws EJBException
  {
    try
    {
      consistenzaDAO = new ConsistenzaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      cDAO = new CommonDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      dichiarazioneSegnalazioneDAO = new DichiarazioneSegnalazioneDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      tipoMotivoDichiarazioneDAO = new TipoMotivoDichiarazioneDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      tipoControlloDAO = new TipoControlloDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      anagrafeGaaDAO = new AnagrafeGaaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      numeroProtocolloGtfoDAO = new NumeroProtocolloGtfoDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      particellaGaaDAO = new ParticellaGaaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      //nuovaIscrizioneDAO = new NuovaIscrizioneDAO(
          //SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      documentoGaaDAO = new DocumentoGaaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      stampaGaaDAO = new StampaGaaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
    }
    catch (ResourceAccessException ex)
    {
      SolmrLogger.fatal(this, ex.getMessage());
      throw new EJBException(ex);
    }
  }

  // Metodo per controllare se l'azienda idAzienda ha fatto una previsione del
  // piano
  // colturale per l'anno sucessivo. Restituisce true se è stata fatta la
  // previsione,
  // false se non è stata fatta
  public boolean previsioneAnnoSucessivo(Long idAzienda) throws Exception
  {
    try
    {
      return consistenzaDAO.previsioneAnnoSucessivo(idAzienda);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }

  // Metodo per controllare se sono state fatte delle modifiche a seguito
  // dell'ultima
  // dichiarazione. Restituisce true se sono state fatte modifiche, false
  // altrimenti
  public boolean controlloUltimeModifiche(Long idAzienda, Integer anno)
      throws Exception
  {
    try
    {
      return consistenzaDAO.controlloUltimeModifiche(idAzienda, anno);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }

  /**
   * Metodo per richiamare la procedura plsql per i controlli sulla consistenza
   * Il parametro restituito può assumere tre valori: - N: richiedere la
   * motivazione della dichiarazione e permettere il salvataggio della
   * dichiarazione di consistenza - E: visualizzare gli errori in ordine di
   * argomento e non permettere il proseguimento - A: visualizzare le anomalie,
   * richiedere la motivazione della dichiarazione e permettere il salvataggio
   */
  public String controlliDichiarazionePLSQL(Long idAzienda, Integer anno,
      Long idMotivoDichiarazione, Long idUtente) throws Exception, SolmrException
  {
    try
    {
      return consistenzaDAO.controlliDichiarazionePLSQL(idAzienda, anno,
          idMotivoDichiarazione, idUtente);
    }
    catch (SolmrException d)
    {
      throw new SolmrException(d.getMessage());
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }

  /**
   * Metodo per richiamare la procedura plsql per i controlli sulla particelle
   */
  public void controlliParticellarePLSQL(Long idAzienda, Integer anno, Long idUtente)
      throws Exception, SolmrException
  {
    try
    {
      consistenzaDAO.controlliParticellarePLSQL(idAzienda, anno, idUtente);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }

  /**
   * Metodo per richiamare la procedura plsql per i controlli sull'insediamento
   * giovani Il parametro restituito può assumere tre valori: - uguale ad E:
   * chiedere conferma per il proseguimento - diverso da E: non permettere di
   * proseguire à E necessario risolvere le anomalie bloccanti
   */
  public String controlliInsediamentoPLSQL(Long idAzienda, Long idUtente)
      throws Exception, SolmrException
  {
    try
    {
      return consistenzaDAO.controlliInsediamentoPLSQL(idAzienda, idUtente);
    }
    catch (SolmrException d)
    {
      throw new SolmrException(d.getMessage());
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }

  /**
   * Metodo per richiamare la procedura plsql per la verifica dei terreni (
   * idGruppoControllo = 1) o della consistenza (idGruppoControllo = null) Il
   * parametro restituito può assumere tre valori: - N: I dati relativi alla
   * consistenza risultano corretti - S: visualizzare le anomalie
   */
  public String controlliVerificaPLSQL(Long idAzienda, Integer anno,
      Integer idGruppoControllo, Long idUtente) throws Exception, SolmrException
  {
    try
    {
      return consistenzaDAO.controlliVerificaPLSQL(idAzienda, anno,
          idGruppoControllo, idUtente);
    }
    catch (SolmrException d)
    {
      throw new SolmrException(d.getMessage());
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }

  // Metodo per recuperare l'elenco degli errori o delle anomalie dovute ad
  // una dichiarazione di consistenza
  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichiarazioneConsistenza(Long idAzienda, Long fase, Long idMotivoDichiarazione)
      throws Exception
  {
    try
    {
      return consistenzaDAO
          .getErroriAnomalieDichiarazioneConsistenza(idAzienda, fase, idMotivoDichiarazione);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }
  
  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichConsPG(long idDichiarazioneConsistenza, 
      long fase) throws Exception
  {
    try
    {
      return consistenzaDAO
          .getErroriAnomalieDichConsPG(idDichiarazioneConsistenza, fase);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }

  // Metodo per recuperare l'elenco degli errori o delle anomalie dovute ad
  // una dichiarazione di consistenza relativamente ai terreni
  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichConsistenzaTerreni(Long idAzienda)
      throws Exception
  {
    try
    {
      return consistenzaDAO.getErroriAnomalieDichConsistenzaTerreni(idAzienda);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }


  /**
   * Questo metodo viene utilizzato per fare il salvataggio della dichiarazione: -
   * PARTE AGGIUNTA PER DOMANDA UNICA 2007 - salvataggio della testata su
   * db_dichiarazione_consistenza - fare una fotografia dei terreni dell'azienda
   * nel caso in cui siano stati modificati(db_conduzione_particella in
   * db_conduzione_dichiarata e db_utilizzo_particella in db_utilizzo
   * dichiarato) - salvataggio delle anomalie riscontrate
   * 
   * Viene usata una procedura PLSQL
   */
  public Long salvataggioDichiarazionePLQSL(ConsistenzaVO consistenzaVO,
      Long idAzienda, Integer anno, RuoloUtenza ruoloUtenza)
      throws Exception, SolmrException
  {
    consistenzaVO.setIdAzienda(idAzienda);
    consistenzaVO.setAnno(anno.toString());
    // variabili utilizzate dalla PARTE AGGIUNTA PER DOMANDA UNICA 2007
    //boolean correzioneDomandaUnica = false;
    String dataSRPU = null;
    String dataDGTF = null;
    String dataVRPU = null;
    String numMinuti = "1";
    String paramCRPU = null;
    Long idDichiarazioneConsistenza = null;
    Long idCodFotrografiaTerreni = null;
    boolean protocollata = false;
    Date lastDichConsDate = null;
    boolean sovraScriviDichiarazione = false;
    boolean nuovaDichiarazione = false;
    Date dataCorrezione = null;
    boolean sysdate = false;
    //ConsistenzaVO consVO = null;
    try
    {
      //Serve per evitare salvataggi concorrenti della stessa dichiarazione
      consistenzaDAO.selectForUpdateDichiarazione(idAzienda);
      
      
      
      
      //Chiusura se richiesta validazione da richiesta azienda ***INIZIO
      /*AziendaNuovaVO aziendaNuovaVO = nuovaIscrizioneDAO.getRichAzByIdAzienda(idAzienda.longValue(), 
          SolmrConstants.RICHIESTA_VALIDAZIONE);
      if(Validator.isNotEmpty(aziendaNuovaVO))
      {
        if(aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_TRASMESSA_PA) == 0)
        {
          nuovaIscrizioneDAO.storicizzaIterRichiestaAzienda(
              aziendaNuovaVO.getIdIterRichiestaAzienda().longValue());
          
          IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
          iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_VALIDATA);
          iterRichiestaAziendaVO.setIdRichiestaAzienda(aziendaNuovaVO.getIdRichiestaAzienda());
          iterRichiestaAziendaVO.setDataInizioValidita(new Date());
          iterRichiestaAziendaVO.setDataAggiornamento(new Date());
          iterRichiestaAziendaVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
          nuovaIscrizioneDAO.insertIterRichiestaAzienda(iterRichiestaAziendaVO);        
        }
      }*/
      //Chiusura se richiesta validazione da richiesta azienda ***FINE
      
      
      
      
      
      
      /**
       * INIZIO PARTE AGGIUNTA PER VARIAZIONE CULTURALE 2011
       */
      // Ricerco il dettaglio del tipo motivo dichiarazione selezionato
      TipoMotivoDichiarazioneVO tipoMotivoDichiarazioneVO = tipoMotivoDichiarazioneDAO
          .findTipoMotivoDichiarazioneByPrimaryKey(Long.decode(consistenzaVO
              .getIdMotivo()));
      // Se il motivo scelto a il parametro GTFO = S
      if (SolmrConstants.FLAG_S.equalsIgnoreCase(tipoMotivoDichiarazioneVO.getGtfo()))
      {
        //correzioneDomandaUnica = true;
        // Vado a leggere tutti i parametri che mi servono
        //usato nel vecchio fermo orologi
        try
        {
          dataSRPU = cDAO.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_SRPU);
        }
        catch (Exception e)
        {}
        
        try
        {
          dataDGTF = cDAO.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_DGTF);
        }
        catch (Exception e)
        {}        
        
        try
        {
          dataVRPU = cDAO.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_VRPU);
        }
        catch (Exception e)
        {}
        
        try
        {
          numMinuti = cDAO.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_MDIC);
        }
        catch (Exception e)
        {}
        
        try
        {
          paramCRPU = cDAO.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_CRPU);
        }
        catch (Exception e)
        {}

        SolmrLogger.debug(this,
            "\n\n\n\nsalvataggioDichiarazionePLQSL : dataDDGTF " + dataDGTF);
        SolmrLogger.debug(this,
            "\n\n\n\nsalvataggioDichiarazionePLQSL : dataVRPU " + dataVRPU);
        SolmrLogger.debug(this, "\n\n\n\nsalvataggioDichiarazionePLQSL : MDIC "
            + numMinuti);
        SolmrLogger.debug(this,
            "\n\n\n\nsalvataggioDichiarazionePLQSL : paramCRPU " + paramCRPU);

        // Cerco l’ultima dichiarazione di consistenza in anagrafe con data
        // validazione (priva di ora, minuti secondi)
        // compresa tra le date contenute nei parametri “VRPU” e “SRPU” presenti
        // sulla tabella DB_PARAMETRO
        // dell’utente SRMCOMUNE
        ConsistenzaVO lastDichConsVO  = consistenzaDAO.getLastDichiarazionebetweenVRPUeSRPU(
            idAzienda, dataVRPU, dataSRPU);
        if (lastDichConsVO != null)
        {
          idDichiarazioneConsistenza = new Long(lastDichConsVO.getIdDichiarazioneConsistenza());
          idCodFotrografiaTerreni = lastDichConsVO.getCodiceFotografiaTerreni();
          if(Validator.isNotEmpty(lastDichConsVO.getNumeroProtocollo()))
            protocollata = true;
          lastDichConsDate = lastDichConsVO.getDataDichiarazione();
        }
        
        if (idDichiarazioneConsistenza != null)
        {
          // Se esiste una dichiarazione di consistenza verifico
          // se tale dichiarazione è legata a delle pratiche.
          consistenzaDAO.aggiornaPraticaAziendaPLQSL(idAzienda);
          long numPratiche = consistenzaDAO.isEsistePratica(idAzienda,
              idDichiarazioneConsistenza, null, null, false);
          
          String lastDichConsDateTruncStr = DateUtils.formatDateNotNull(lastDichConsDate);
          Date lastDichConsDateTrunc = format.parse(lastDichConsDateTruncStr+" 00:00:00");
          Date dataParametroSRPU = format.parse(dataSRPU+" 00:00:00");
          if (numPratiche != 0)
          {
            
            Date dataValiditaStato = consistenzaDAO.getMaxValiditaStato(idAzienda, idDichiarazioneConsistenza);
            if(Validator.isNotEmpty(dataValiditaStato))
            {
              String dataValiditStatoTruncStr = DateUtils.formatDateNotNull(dataValiditaStato);
              Date dataValiditaStatoTrunc = format.parse(dataValiditStatoTruncStr+" 00:00:00");
              if(dataValiditaStatoTrunc.after(dataParametroSRPU))
              {
                //devo prendere la data del parametro SRPU
                //deve cmq avere ore minuti e secondi successivi all'ultima dichiarazione 
                //di consitenza
                if(lastDichConsDateTrunc.equals(dataParametroSRPU))
                {
                  int oraDVS = DateUtils.extractHourFromDate(lastDichConsDate);
                  int rangeOra =23-oraDVS+1;
                  int ora = (int) ((Math.random() * rangeOra) + oraDVS);
                  String oreStr = "" + ora;
                  if (oreStr.length() == 1)
                    oreStr = "0" + oreStr;
                  String secondiStr = "";
                  String minutiStr = "";
                  if(oraDVS != ora)
                  {
                    secondiStr = "" + (int) (Math.random() * 60);
                    if (secondiStr.length() == 1)
                      secondiStr = "0" + secondiStr;
                    minutiStr = "" + (int) (Math.random() * 60);
                    if (minutiStr.length() == 1)
                      minutiStr = "0" + minutiStr;
                  }
                  else
                  {
                    int minutiDVS = DateUtils.extractMinuteFromDate(lastDichConsDate);
                    int rangeMinuti =59-minutiDVS+1;
                    int minuti = (int) ((Math.random() * rangeMinuti) + minutiDVS);
                    minutiStr = "" + minuti;
                    if (minutiStr.length() == 1)
                      minutiStr = "0" + minutiStr;
                    if(minutiDVS != minuti)
                    {
                      secondiStr = "" + (int) (Math.random() * 60);
                      if (secondiStr.length() == 1)
                        secondiStr = "0" + secondiStr;
                    }
                    else
                    {
                      int secondiDVS = DateUtils.extractSecondFromDate(lastDichConsDate);
                      int rangeSecondi =59-secondiDVS+1;
                      int secondi = (int) ((Math.random() * rangeSecondi) + secondiDVS);
                      secondiStr = "" + secondi;
                    }
                  }
                  
                  dataCorrezione = format.parse(dataSRPU+" "+oreStr+":"+minutiStr+":"+secondiStr);
                }
                else
                {
                  String secondi = "" + (int) (Math.random() * 60);
                  if (secondi.length() == 1)
                    secondi = "0" + secondi;
                  String minuti = "" + (int) (Math.random() * 60);
                  if (minuti.length() == 1)
                    minuti = "0" + minuti;
                  String ore = "" + (int) (Math.random() * 24);
                  if (ore.length() == 1)
                    ore = "0" + ore;
                  dataCorrezione = format.parse(dataSRPU+" "+ore+":"+minuti+":"+secondi);
                }
              }
              //Se minore o uguale a SRPU
              //Devo prendere la max tra la data validita stato e la data dell'ultima dichiarazione di
              //consitenza ma con ore minuti secondi maggiori di quella data
              else
              {
                Date lastDateTmp = dataValiditaStato;
                String lastDateTmpStr = dataValiditStatoTruncStr;
                if(lastDateTmp.before(lastDichConsDate))
                {
                  lastDateTmp = lastDichConsDate;
                  lastDateTmpStr = lastDichConsDateTruncStr;
                }
                
                int oraDVS = DateUtils.extractHourFromDate(lastDateTmp);
                int rangeOra =23-oraDVS+1;
                int ora = (int) ((Math.random() * rangeOra) + oraDVS);
                String oreStr = "" + ora;
                if (oreStr.length() == 1)
                  oreStr = "0" + oreStr;
                String secondiStr = "";
                String minutiStr = "";
                if(oraDVS != ora)
                {
                  secondiStr = "" + (int) (Math.random() * 60);
                  if (secondiStr.length() == 1)
                    secondiStr = "0" + secondiStr;
                  minutiStr = "" + (int) (Math.random() * 60);
                  if (minutiStr.length() == 1)
                    minutiStr = "0" + minutiStr;
                }
                else
                {
                  int minutiDVS = DateUtils.extractMinuteFromDate(lastDateTmp);
                  int rangeMinuti =59-minutiDVS+1;
                  int minuti = (int) ((Math.random() * rangeMinuti) + minutiDVS);
                  minutiStr = "" + minuti;
                  if (minutiStr.length() == 1)
                    minutiStr = "0" + minutiStr;
                  if(minutiDVS != minuti)
                  {
                    secondiStr = "" + (int) (Math.random() * 60);
                    if (secondiStr.length() == 1)
                      secondiStr = "0" + secondiStr;
                  }
                  else
                  {
                    int secondiDVS = DateUtils.extractSecondFromDate(lastDateTmp);
                    int rangeSecondi =59-secondiDVS+1;
                    int secondi = (int) ((Math.random() * rangeSecondi) + secondiDVS);
                    secondiStr = "" + secondi;
                  }
                }
                
                dataCorrezione = format.parse(lastDateTmpStr+" "+oreStr+":"+minutiStr+":"+secondiStr);
              }
            }
            //e null uso la data SRPU
            else
            {
              //Devo prendere la max tra la dataSRPU e la data dell'ultima dichiarazione di
              //consitenza ma con ore minuti secondi maggiori di quella data
              if(lastDichConsDateTrunc.equals(dataParametroSRPU))
              {
                int oraDVS = DateUtils.extractHourFromDate(lastDichConsDate);
                int rangeOra =23-oraDVS+1;
                int ora = (int) ((Math.random() * rangeOra) + oraDVS);
                String oreStr = "" + ora;
                if (oreStr.length() == 1)
                  oreStr = "0" + oreStr;
                String secondiStr = "";
                String minutiStr = "";
                if(oraDVS != ora)
                {
                  secondiStr = "" + (int) (Math.random() * 60);
                  if (secondiStr.length() == 1)
                    secondiStr = "0" + secondiStr;
                  minutiStr = "" + (int) (Math.random() * 60);
                  if (minutiStr.length() == 1)
                    minutiStr = "0" + minutiStr;
                }
                else
                {
                  int minutiDVS = DateUtils.extractMinuteFromDate(lastDichConsDate);
                  int rangeMinuti =59-minutiDVS+1;
                  int minuti = (int) ((Math.random() * rangeMinuti) + minutiDVS);
                  minutiStr = "" + minuti;
                  if (minutiStr.length() == 1)
                    minutiStr = "0" + minutiStr;
                  if(minutiDVS != minuti)
                  {
                    secondiStr = "" + (int) (Math.random() * 60);
                    if (secondiStr.length() == 1)
                      secondiStr = "0" + secondiStr;
                  }
                  else
                  {
                    int secondiDVS = DateUtils.extractSecondFromDate(lastDichConsDate);
                    int rangeSecondi =59-secondiDVS+1;
                    int secondi = (int) ((Math.random() * rangeSecondi) + secondiDVS);
                    secondiStr = "" + secondi;
                  }
                }
                
                dataCorrezione = format.parse(dataSRPU+" "+oreStr+":"+minutiStr+":"+secondiStr);
              }
              else
              {
                String secondi = "" + (int) (Math.random() * 60);
                if (secondi.length() == 1)
                  secondi = "0" + secondi;
                String minuti = "" + (int) (Math.random() * 60);
                if (minuti.length() == 1)
                  minuti = "0" + minuti;
                String ore = "" + (int) (Math.random() * 24);
                if (ore.length() == 1)
                  ore = "0" + ore;
                dataCorrezione = format.parse(dataSRPU+" "+ore+":"+minuti+":"+secondi);
              }
            }
            
             
            
            nuovaDichiarazione = true;
            
          }
          // Se non esiste alcuna pratica legata alla dichiarazione in esame
          else
          {
            //Se la dichiarazione è protocollata
            if(protocollata)
            {
              // viene creata una dichiarazione di consistenza con data uguale
              // ma le ore/minuti/secondi devono essere successivi.
              try
              {
                
                int oraDVS = DateUtils.extractHourFromDate(lastDichConsDate);
                int rangeOra =23-oraDVS+1;
                int ora = (int) ((Math.random() * rangeOra) + oraDVS);
                String oreStr = "" + ora;
                if (oreStr.length() == 1)
                  oreStr = "0" + oreStr;
                String secondiStr = "";
                String minutiStr = "";
                if(oraDVS != ora)
                {
                  secondiStr = "" + (int) (Math.random() * 60);
                  if (secondiStr.length() == 1)
                    secondiStr = "0" + secondiStr;
                  minutiStr = "" + (int) (Math.random() * 60);
                  if (minutiStr.length() == 1)
                    minutiStr = "0" + minutiStr;
                }
                else
                {
                  int minutiDVS = DateUtils.extractMinuteFromDate(lastDichConsDate);
                  int rangeMinuti =59-minutiDVS+1;
                  int minuti = (int) ((Math.random() * rangeMinuti) + minutiDVS);
                  minutiStr = "" + minuti;
                  if (minutiStr.length() == 1)
                    minutiStr = "0" + minutiStr;
                  if(minutiDVS != minuti)
                  {
                    secondiStr = "" + (int) (Math.random() * 60);
                    if (secondiStr.length() == 1)
                      secondiStr = "0" + secondiStr;
                  }
                  else
                  {
                    int secondiDVS = DateUtils.extractSecondFromDate(lastDichConsDate);
                    int rangeSecondi =59-secondiDVS+1;
                    int secondi = (int) ((Math.random() * rangeSecondi) + secondiDVS);
                    secondiStr = "" + secondi;
                  }
                }
                
                dataCorrezione = format.parse(lastDichConsDateTruncStr+" "+oreStr+":"+minutiStr+":"+secondiStr);
                
              }
              catch (Exception e)
              {
                SolmrLogger.fatal(this,
                    "\n\n\n\n Exception in salvataggioDichiarazionePLQSL:dataCorrezione "
                        + e.toString());
              }
              nuovaDichiarazione = true;
              
            }
            //Se la dichiarazione non è protocollata
            // sovrascrivere la dichiarazione di consistenza trovata.
            // La data di dichiarazione non deve cambiare.
            else
            {
              sovraScriviDichiarazione = true;
            }
            
          }
        }
        else
        {
          // Se non esiste una dichiarazione di consistenza in questo range
          // viene creata una dichiarazione di consistenza con data uguale
          // alla data contenuta nel parametro e “DGTF” presente
          // sulla tabella DB_PARAMETRO dell’utente SRMCOMUNE.
          try
          {
            String ore = "" + (int) ((Math.random() * 11) + 8);
            if (ore.length() == 1)
              ore = "0" + ore;
            dataCorrezione = format.parse(dataDGTF + " " + ore
                + DateUtils.getCurrent(":mm:ss"));
            SolmrLogger.debug(this,
                "\n\n\n\nsalvataggioDichiarazionePLQSL : dataCorrezione"
                    + dataCorrezione);
          }
          catch (Exception e)
          {
            SolmrLogger.fatal(this,
                "\n\n\n\n Exception in salvataggioDichiarazionePLQSL:dataCorrezione "
                    + e.toString());
          }
          nuovaDichiarazione = true;
        }
      } //fine if GTFO
      //eliminato per il non gtfo la sovrascrizione
      else
      {      
        Long result[] = consistenzaDAO.verificaConsistenzaStessoGiorno(idAzienda);
        if (result != null)
        {
          idDichiarazioneConsistenza = result[0];
          idCodFotrografiaTerreni = result[1];
        }
        if (idDichiarazioneConsistenza != null)
        {
          // Se esiste già una dichiarazione di consistenza in giornata
          // verificare se tale dichiarazione
          // è già legata ad una pratica
          consistenzaDAO.aggiornaPraticaAziendaPLQSL(idAzienda);
          /*long numPratiche = consistenzaDAO.isEsistePratica(idAzienda,
              idDichiarazioneConsistenza, null, null, false);
          if (numPratiche == 0)
          {
            // Se non è legata ad alcuna pratica procedere con la sovrascrittura
            // della dichiarazione
            // di consistenza dove la data della dichiarazione è la data di
            // sistema comprensiva di ore, min e sec.
            
            
            sysdate = true;
            sovraScriviDichiarazione = true;
          }
          else
          {
            // Se è legata ad una pratica procedere con la creazione
            // di una nuova dichiarazione di consistenza
            nuovaDichiarazione = true;
          }*/
        }
        else
        {
          // Se non esiste già una dichiarazione di consistenza in giornata
          // procedere
          // con la creazione di una nuova dichiarazione di consistenza
          //nuovaDichiarazione = true;
        }
      }
      // Se la dichiarazione è risultata sovrascrivibile...
      /*if (sovraScriviDichiarazione)
      {
        consVO = consistenzaDAO
            .findDichiarazioneConsistenzaByPrimaryKey(idDichiarazioneConsistenza);
        // Verifico se possiede già un numero protocollo...
        if (Validator.isNotEmpty(consVO.getNumeroProtocollo()))
        {
          // Non è più sovrascrivibile ma deve essere ricreata
          sovraScriviDichiarazione = false;
          nuovaDichiarazione = true;
          //Commentato per Gaa14-02 v6
          //dataCorrezione = consistenzaDAO.getDataLastDichiarazione(
              //idDichiarazioneConsistenza, numMinuti);
          //********************************
        }
      }*/

      /*if (sovraScriviDichiarazione)
      {
        // UPDATE su DB_DICHIARAZIONE_CONSISTENZA
        consistenzaDAO
            .deleteDichiarazioneSegnalazione(idDichiarazioneConsistenza);
        consistenzaDAO.deleteUtilizziAndConduzioni(idCodFotrografiaTerreni);
        consistenzaDAO
            .deleteDichiarazioneCorrezione(idDichiarazioneConsistenza);
        // Elimino i parametri associati alle attestazioni dichiarate
        parametriAttDichiarataDAO
            .deleteParametriAttDichiarataByCodiceFotografiaTerreni(
                idCodFotrografiaTerreni.toString(), null);
        // Elimino le attestazioni dichiarate
        tipoAttestazioneDAO
            .deleteAttestazioneDichiarataByCodiceFotografiaTerreni(
                idCodFotrografiaTerreni.toString(), null);
        consistenzaVO.setCodiceFotografiaTerreni(idCodFotrografiaTerreni);
        // Se per il tipo motivazione selezionato è valorizzato...
        if (Validator.isNotEmpty(tipoMotivoDichiarazioneVO.getAnnoCampagna()))
        {
          // ... valorizzo il campo su DB_DICHIARAZIONE_CONSISTENZA
          consistenzaVO.setAnnoCampagna(tipoMotivoDichiarazioneVO
              .getAnnoCampagna());
        }
        // Altrimenti
        else
        {
          // Ricerco su DB_PARAMETRO il parametro DCMP
          String dataDCMP = null;
          try
          {
            dataDCMP = cDAO.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_DCMP);
          }
          catch (Exception e)
          {}
          
          try
          {
            // Se la data contenuta nel parametro DCMP è >= di SYSDATE
            if (DateUtils.parseDate(dataDCMP).getTime() >= System.currentTimeMillis())
            {
              // Anno campagna = anno corrente
              consistenzaVO.setAnnoCampagna(DateUtils.getCurrentYear().toString());
            }
            // Altrimenti
            else
            {
              // è uguale a anno DCMP + 1
              String annoCa = dataDCMP.substring(6);
              consistenzaVO.setAnnoCampagna(String.valueOf(Integer.parseInt(annoCa) + 1));
            }
          }
          catch (Exception e)
          {}
        }
        
        // Se l'utente ha scelto di riprotocollare la dichiarazione
        if (SolmrConstants.FLAG_S.equals(consistenzaVO.getFlagProtocollo()))
        {
          // Ricalcolo il numero protocollo in funzione del parametro GTFO
          if (SolmrConstants.FLAG_S.equalsIgnoreCase(tipoMotivoDichiarazioneVO.getGtfo()))
          {
            // Calcolo da DB_NUMERO_PROTOCOLLO_GTFO
            NumeroProtocolloGtfoVO numeroProtocolloGtfoVO = numeroProtocolloGtfoDAO
                .getNumeroProtocolloGtfo(ruoloUtenza, DateUtils.getCurrentYear().toString());
            if (numeroProtocolloGtfoVO != null)
            {
              consistenzaVO.setNumeroProtocollo(numeroProtocolloGtfoVO.getNumeroProtocollo());
              consistenzaVO.setDataProtocollo(numeroProtocolloGtfoVO.getDataProtocollo());
              numeroProtocolloGtfoDAO.aggiornaProgressivoNumeroProtocolloGtfo(numeroProtocolloGtfoVO
                      .getIdNumeroProtocolloGtfo());
            }
          }
          else
          {
            // Calcolo da DB_NUMERO_PROTOCOLLO
            // Per far questo devo andare a fare la lookup dell'ejb dei
            // documenti
            InitialContext ctx = new InitialContext();
            DocumentoRemote documentoRemote = ((DocumentoHome) ctx.lookup("java:comp/env/solmr/anag/Documento")).create();
            consistenzaVO.setNumeroProtocollo(documentoRemote.mathNumeroProtocollo(ruoloUtenza, 
                DateUtils.getCurrentYear().toString()));
            consistenzaVO.setDataProtocollo(new Date(System.currentTimeMillis()));
          }
        }
        else
        {
          consistenzaVO.setNumeroProtocollo(consVO.getNumeroProtocollo());
          consistenzaVO.setDataProtocollo(consVO.getDataProtocollo());
        }
        consistenzaDAO.updateDichiarazioneConsistenza(consistenzaVO, idAzienda, idDichiarazioneConsistenza, 
            sysdate);
      } */ 
      //fine if sovrascrivi dichiarazione
      //else
      //{
        // INSERT su DB_DICHIARAZIONE_CONSISTENZA
        // Se per il tipo motivazione selezionato è valorizzato...
        if (Validator.isNotEmpty(tipoMotivoDichiarazioneVO.getAnnoCampagna()))
        {
          // ... valorizzo il campo su DB_DICHIARAZIONE_CONSISTENZA
          consistenzaVO.setAnnoCampagna(tipoMotivoDichiarazioneVO.getAnnoCampagna());
        }
        // Altrimenti
        else
        {
          // Ricerco su DB_PARAMETRO il parametro DCMP
          String dataDCMP = null;
          try
          {
            dataDCMP = cDAO.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_DCMP);
          }
          catch (Exception e)
          {}
          
          try
          {
            // Se la data contenuta nel parametro DCMP è >= di SYSDATE
            if (DateUtils.parseDate(dataDCMP).getTime() >= System.currentTimeMillis())
            {
              // Anno campagna = anno corrente
              consistenzaVO.setAnnoCampagna(DateUtils.getCurrentYear().toString());
            }
            // Altrimenti
            else
            {
              // è uguale a anno DCMP + 1
              String annoCa = dataDCMP.substring(6);
              consistenzaVO.setAnnoCampagna(String.valueOf(Integer.parseInt(annoCa) + 1));
            }
          }
          catch (Exception e)
          {}
        }
        consistenzaVO.setCodiceFotografiaTerreni(consistenzaDAO.getSeqCodiceFotografiaTerreni());

        if (SolmrConstants.FLAG_S.equals(consistenzaVO.getFlagProtocollo()))
        {
          if (SolmrConstants.FLAG_S.equalsIgnoreCase(tipoMotivoDichiarazioneVO.getGtfo()))
          {
            // Calcolo da DB_NUMERO_PROTOCOLLO_GTFO
            NumeroProtocolloGtfoVO numeroProtocolloGtfoVO = numeroProtocolloGtfoDAO
                .getNumeroProtocolloGtfo(ruoloUtenza, DateUtils.getCurrentYear().toString());
            if (numeroProtocolloGtfoVO != null)
            {
              consistenzaVO.setNumeroProtocollo(numeroProtocolloGtfoVO.getNumeroProtocollo());
              consistenzaVO.setDataProtocollo(numeroProtocolloGtfoVO.getDataProtocollo());
              //consistenzaVO.setDataProtocollo(dataCorrezione);
              numeroProtocolloGtfoDAO.aggiornaProgressivoNumeroProtocolloGtfo(numeroProtocolloGtfoVO
                      .getIdNumeroProtocolloGtfo());
            }
          }
          else
          {
            // Calcolo da DB_NUMERO_PROTOCOLLO
            // Per far questo devo andare a fare la lookup dell'ejb dei
            // documenti
            InitialContext ctx = new InitialContext();
            consistenzaVO.setNumeroProtocollo(documentoLocal.mathNumeroProtocollo(ruoloUtenza, 
                DateUtils.getCurrentYear().toString()));
            consistenzaVO.setDataProtocollo(new Date(System.currentTimeMillis()));
          }
        }
        idDichiarazioneConsistenza = consistenzaDAO
            .insertDichiarazioneConsistenza(consistenzaVO, idAzienda, dataCorrezione);
      //}
      consistenzaDAO.updateDichiarazioneSegnalazione(idAzienda, idDichiarazioneConsistenza);
      consistenzaDAO.updateDichiarazioneCorrezione(idAzienda, idDichiarazioneConsistenza);
      consistenzaDAO.updateStoricoUnitaArborea(idAzienda);
      // INSERT TERRENI
      consistenzaDAO.inserimentoTerreniPLQSL(idAzienda, anno, consistenzaVO
          .getCodiceFotografiaTerreni(), consistenzaVO.getIdUtente());
      
      if (SolmrConstants.FLAG_S.equals(consistenzaVO.getFlagProtocollo()))
      {
        PlSqlCodeDescription plCode = particellaGaaDAO.inserisciIstanzaPlSql(idAzienda.longValue(), anno.intValue());
        
        if(plCode !=null)
        {
          if(Validator.isNotEmpty(plCode.getDescription()))
          {
            throw new Exception("Aggiorna documento errore al plsql inserisciIstanza: "+plCode.getDescription()+"-"
                +plCode.getOtherdescription());       
          }
        }        
      }
      
      //
      /**
       * FINE PARTE AGGIUNTA PER DOMANDA UNICA 2007
       */
      
      
      
      SolmrLogger.debug(this, "\n\n\n\nsalvataggioDichiarazionePLQSL : sysdate " + sysdate);
      SolmrLogger.debug(this, "\n\n\n\nsalvataggioDichiarazionePLQSL : dataCorrezione "
              + dataCorrezione);
      SolmrLogger.debug(this, "\n\n\n\nsalvataggioDichiarazionePLQSL : nuovaDichiarazione "
              + nuovaDichiarazione);
      SolmrLogger.debug(this, "\n\n\n\nsalvataggioDichiarazionePLQSL : sovraScriviDichiarazione "
              + sovraScriviDichiarazione);
      SolmrLogger.debug(this, "\n\n\n\nsalvataggioDichiarazionePLQSL : idDichiarazioneConsistenza "
              + idDichiarazioneConsistenza);
      SolmrLogger.debug(this, "\n\n\n\nsalvataggioDichiarazionePLQSL : idCodFotrografiaTerreni "
              + idCodFotrografiaTerreni);
      SolmrLogger.debug(this, "\n\n\n\nsalvataggioDichiarazionePLQSL : END");
      
      
      
      PlSqlCodeDescription plCode = anagrafeGaaDAO.calcolaEfaPlSql(idAzienda.longValue(),
          idDichiarazioneConsistenza.longValue(), ruoloUtenza.getIdUtente().longValue());
      
      if(plCode !=null)
      {
        if(Validator.isNotEmpty(plCode.getDescription()))
        {
          throw new DataAccessException("calcolaEfaPlSql errore nella generazione della validazione: "+plCode.getDescription()+"-"
              +plCode.getOtherdescription());       
        }
      }
      
      
     plCode = anagrafeGaaDAO.calcolaGreeningPlSql(idAzienda.longValue(), 
          ruoloUtenza.getIdUtente().longValue(), idDichiarazioneConsistenza.longValue());
      
      
      
      if(plCode !=null)
      {
        if(Validator.isNotEmpty(plCode.getDescription()))
        {
          throw new DataAccessException("calcolaEsitoGreeningPlSql errore nella generazione della validazione: "+plCode.getDescription()+"-"
              +plCode.getOtherdescription());       
        }
      }
      
      
      
      //Salvataggio allegati!!
      //Validazione inserisco uno vuoto senza stampa
      AllegatoDocumentoVO allegatoDocumentoVO = new AllegatoDocumentoVO();
      allegatoDocumentoVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
      Date dataAttuale = new Date();
      allegatoDocumentoVO.setDataRagistrazione(dataAttuale);
      allegatoDocumentoVO.setDataUltimoAggiornamento(dataAttuale);          
      String nomeFile = "";
      if(Validator.isNotEmpty(consistenzaVO.getNumeroProtocollo()))
      {
        nomeFile = "Validazione_"+consistenzaVO.getNumeroProtocollo();
        if(ruoloUtenza.isUtentePA())
          allegatoDocumentoVO.setIdTipoFirma(new Long(4));
      }
      else
      {
        nomeFile = "Validazione_"+idDichiarazioneConsistenza;
      }
      nomeFile += ".pdf";          
      allegatoDocumentoVO.setNomeFisico(nomeFile);
      allegatoDocumentoVO.setNomeLogico(nomeFile);
      allegatoDocumentoVO.setIdTipoAllegato(new Long(SolmrConstants.VALIDAZIONE_ALLEGATO));
      Long idAllegato = documentoGaaDAO.insertFileAllegatoNoFile(allegatoDocumentoVO);
      documentoGaaDAO.insertAllegatoDichiarazione(idDichiarazioneConsistenza, 
          idAllegato.longValue());
      
      
      
      //Salvataggio allegati!!
      //Verifica se esiste già una stampa valida memorizzata attiva per il tipo di allegato
      Vector<AllegatoDichiarazioneVO>  vAllAuto = documentoGaaDAO.getElencoAllegatiDichiarazioneDefault(new Integer(consistenzaVO.getIdMotivo()));
      if(Validator.isNotEmpty(vAllAuto))
      {
        for(int h=0;h<vAllAuto.size();h++)
        {
          AllegatoDichiarazioneVO allegatoDichiarazioneVO = vAllAuto.get(h);
          if(stampaGaaDAO.isInseribileAllegatoAuto(allegatoDichiarazioneVO.getQueryAbilitazione(),
              consistenzaVO.getCodiceFotografiaTerreni()))
          {
            //inserisco uno vuoto senza stampa
            allegatoDocumentoVO = new AllegatoDocumentoVO();
            allegatoDocumentoVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
            
            allegatoDocumentoVO.setDataRagistrazione(dataAttuale);
            allegatoDocumentoVO.setDataUltimoAggiornamento(dataAttuale);
            nomeFile = allegatoDichiarazioneVO.getDescTipoAllegato().replace(" ", "");
            if(Validator.isNotEmpty(consistenzaVO.getNumeroProtocollo()))
            {
              nomeFile += "_"+consistenzaVO.getNumeroProtocollo();
              if(ruoloUtenza.isUtentePA())
                allegatoDocumentoVO.setIdTipoFirma(new Long(4));
            }
            else
            {
              nomeFile += "_"+idDichiarazioneConsistenza;
            }          
            nomeFile += ".pdf";          
            allegatoDocumentoVO.setNomeFisico(nomeFile);
            allegatoDocumentoVO.setNomeLogico(nomeFile);
            allegatoDocumentoVO.setIdTipoAllegato(new Long(allegatoDichiarazioneVO.getIdTipoAllegato()));
            idAllegato = documentoGaaDAO.insertFileAllegatoNoFile(allegatoDocumentoVO);
            documentoGaaDAO.insertAllegatoDichiarazione(idDichiarazioneConsistenza, 
                idAllegato.longValue());
          }
        }
      }
      
      
      return idDichiarazioneConsistenza;
    }
    catch (SolmrException se)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(se.getMessage());
    }
    catch (DataAccessException d)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(d.getMessage());
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(e.getMessage());
    }
  }

  // Metodo per recuperare l'elenco delle dichiarazione di consistenza di
  // un azienda
  public Vector<ConsistenzaVO> getDichiarazioniConsistenza(Long idAzienda)
      throws Exception
  {
    Vector<ConsistenzaVO> result = new Vector<ConsistenzaVO>(); 
    try
    {
      // Vado a leggere l'elenco delle dichiarazioni associate all'azienda
      result = consistenzaDAO.getDichiarazioniConsistenza(idAzienda);
      HashMap<Long, Vector<AllegatoDichiarazioneVO>> hAllegValid = documentoGaaDAO
            .getAllegatiDichiarazioneInseriti(idAzienda);
      
      for (int i = 0; i < result.size(); i++)
      {
        ConsistenzaVO consistenzaVO = (ConsistenzaVO) result.get(i);
        
        Long idDichiarazioneConsistenza = new Long(consistenzaVO.getIdDichiarazioneConsistenza());
        if(Validator.isNotEmpty(hAllegValid)
          && Validator.isNotEmpty(hAllegValid.get(idDichiarazioneConsistenza)))
        {
          Vector<AllegatoDichiarazioneVO> vDocAllPresenti = hAllegValid.get(
              new Long(consistenzaVO.getIdDichiarazioneConsistenza()));
        
          
          consistenzaVO.setvAllegatoDichiarazioneVO(vDocAllPresenti);
        }      
      }    
      
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
    try
    {
      // Visualizzare l’icona relativa alle presenza delle pratiche se
      // legata alla dichiarazione di consistenza vi è almeno una pratica
      // legata.
      // Richiamare la stored procedure PACK_PRATICA_AZIENDA.
      // AGGIORNA_PRATICA_AZIENDA
      // passandogli come parametro di input l’identificativo dell’azienda
      // in esame.Se il richiamo alla stored procedure termina con esito
      // positivo
      // (parametri di output a null), per ogni identificativo della
      // dichiarazione di consistenza,
      // accedere alla tabella db_pratica_azienda per id_azienda,
      // id_dichiarazione_consistenza.
      // Se viene trovato almeno un recod visualizzare l’icona relativa
      // alla presenza di pratiche legate alla dichiarazione.
      // Nel caso in cui non venga trovato alcun record, non visualizzare alcuna
      // icona.

      consistenzaDAO.aggiornaPraticaAziendaPLQSL(idAzienda);
      int size = result.size();
      for (int i = 0; i < size; i++)
      {
        ConsistenzaVO consistenzaVO = (ConsistenzaVO) result.get(i);

        if (consistenzaDAO.isEsistePratica(idAzienda, new Long(consistenzaVO
            .getIdDichiarazioneConsistenza()), null, null, false) > 0)
          consistenzaVO.setEsistePratica(new Boolean(true));
        else
          consistenzaVO.setEsistePratica(new Boolean(false));
        
        //result.add(consistenzaVO);
      }
    }
    catch (Exception se)
    {
      // Nel caso in cui si verifichi un errore nel richiamo della
      // stored procedure visualizzare comunque l’elenco delle dichiarazioni
      // senza icona pratiche e dare comunque messaggio all’utente
      // dell’anomalia.
      return result;
    }
    return result;

  }

  // Metodo per recuperare l'elenco minimo delle dichiarazioni di consistenza di
  // un azienda
  public Vector<ConsistenzaVO> getDichiarazioniConsistenzaMinimo(Long idAzienda)
      throws Exception
  {
    try
    {
      return consistenzaDAO.getDichiarazioniConsistenzaMinimo(idAzienda);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }

  // Metodo per recuperare il dettaglio di una dichiarazione di consistenza dato
  // un idDichiarazioneConsistenza
  public ConsistenzaVO getDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza) throws Exception
  {
    try
    {
      return consistenzaDAO
          .getDichiarazioneConsistenza(idDichiarazioneConsistenza);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }

  /*public ConsistenzaVO serviceGetUltimaDichiarazioneConsistenza(Long idAzienda)
      throws Exception, SolmrException
  {
    if (idAzienda == null)
    {
      throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER,
          ErrorTypes.INVALID_PARAMETER);
    }

    try
    {
      return consistenzaDAO.getUltimaDichiarazioneConsistenza(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
  }*/
  
  //Usata non nei servizi!!!
  public ConsistenzaVO getUltimaDichiarazioneConsistenza(Long idAzienda)
    throws Exception
  {
    
    try
    {
      return consistenzaDAO.getUltimaDichiarazioneConsistenza(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
  }

  public ConsistenzaVO serviceGetDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza) throws Exception, SolmrException
  {
    if (idDichiarazioneConsistenza == null)
    {
      throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER,
          ErrorTypes.INVALID_PARAMETER);
    }

    try
    {
      return consistenzaDAO
          .serviceGetDichiarazioneConsistenza(idDichiarazioneConsistenza);
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
  }

  /**
   * Metodo per richiamare la procedura plsql per i controlli delle pratiche che
   * hanno utilizzato quella dichiarazione di consistenza
   * 
   * @param idAzienda:
   *          idAzienda selezionata
   * @param idUtente:
   *          Idutente connesso
   * @param idDichiarazioneConsistenza:
   *          idDichiarazioneConsistenza
   * @return l'elenco delle pratiche che hanno utilizzato la dichiarazione di
   *         consistenza
   * @throws DataAccessException
   * @throws SolmrException
   */
  public Vector<TemporaneaPraticaAziendaVO> aggiornaPraticaAziendaPLQSL(Long idAzienda, Long idUtente,
      Long idDichiarazioneConsistenza) throws Exception, SolmrException
  {
    try
    {
      return consistenzaDAO.aggiornaPraticaAziendaPLQSL(idAzienda, idUtente,
          idDichiarazioneConsistenza);
    }
    catch (SolmrException d)
    {
      throw new SolmrException(d.getMessage());
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }
  
  public void aggiornaPraticaAziendaPLQSL(Long idAzienda) 
      throws Exception, SolmrException
  {
    try
    {
      consistenzaDAO.aggiornaPraticaAziendaPLQSL(idAzienda);
    }
    catch (SolmrException d)
    {
      throw new SolmrException(d.getMessage());
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }

  /**
   * Restituisce true se posso cancellare la dichiarazione, false altrimenti
   */
  public boolean deleteDichConsAmmessa(Long idAzienda, Long idUtente,
      Long idDichiarazioneConsistenza) throws Exception, SolmrException
  {
    try
    {
      return consistenzaDAO.operazioneAmmessaPLQSL(idAzienda, idUtente,
          idDichiarazioneConsistenza, (String) SolmrConstants
              .get("CONSISTENZA_OPERAZIONE_DELETE"));
    }
    catch (SolmrException d)
    {
      throw new SolmrException(d.getMessage());
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }
  
  
  /**
   * Restituisce true se posso creare una nuova dichiarazione, false altrimenti
   */
  public boolean newDichConsAmmessa(Long idAzienda, Long idUtente,
      Long idMotivoDichiarazione) throws Exception, SolmrException
  {
    try
    {
      return consistenzaDAO.operazioneAmmessaPLQSL(idAzienda, idUtente,
          idMotivoDichiarazione, (String) SolmrConstants.
          NUOVA_DICHIARAZIONE_CONSISTENZA);
    }
    catch (SolmrException d)
    {
      throw new SolmrException(d.getMessage());
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }

  /**
   * Cancella una dichiarazione di consistenza
   */
  public void deleteDichiarazioneConsistenza(Long idDichiarazioneConsistenza)
      throws Exception
  {
    try
    {
      consistenzaDAO.deleteDichiarazioneConsistenza(idDichiarazioneConsistenza);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }

  // Metodo per recuperare il dettaglio di un'anomalia
  public ErrAnomaliaDicConsistenzaVO getAnomaliaDichiarazioneConsistenza(
      Long idDichiarazioneSegnalazione) throws Exception
  {
    try
    {
      return consistenzaDAO
          .getAnomaliaDichiarazioneConsistenza(idDichiarazioneSegnalazione);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }

  // Metodo per recuperare l'elenco degli errori o delle anomalie associati
  // all'elenco di IdDichiarazioneSegnalazione e vedre se possono essere
  // corrette.

  public Vector<ErrAnomaliaDicConsistenzaVO> getAnomaliePerCorrezione(
      Long elencoIdDichiarazioneSegnalazione[],long idMotivoDichiarazione) throws Exception
  {
    try
    {
      return consistenzaDAO
          .getAnomaliePerCorrezione(elencoIdDichiarazioneSegnalazione,idMotivoDichiarazione);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }

  public Vector<CodeDescription> getDocumentiByIdControllo(Long idControllo)
      throws Exception
  {
    try
    {
      return cDAO.getDocumentiByIdControllo(idControllo);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }

  public void deleteInsertDichiarazioneCorrezione(
      String elencoIdDichiarazioneSegnalazione[], Vector<ErrAnomaliaDicConsistenzaVO> corrErr, long idUtente)
      throws Exception
  {
    try
    {
      consistenzaDAO.deleteInsertDichiarazioneCorrezione(
          elencoIdDichiarazioneSegnalazione, corrErr, idUtente);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }

  // Metodo per effettuare la cancellazione di una dichiarazione di correzione
  public void deleteDichiarazioneCorrezione(
      String elencoIdDichiarazioneSegnalazione[]) throws Exception
  {
    try
    {
      consistenzaDAO
          .deleteDichiarazioneCorrezione(elencoIdDichiarazioneSegnalazione);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }

  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichiarazioneConsistenza(
      Long elencoIdDichiarazioneSegnalazione[], long idMotivoDichiarazione) throws Exception
  {
    try
    {
      return consistenzaDAO
          .getErroriAnomalieDichiarazioneConsistenza(elencoIdDichiarazioneSegnalazione, idMotivoDichiarazione);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce l'elenco delle segnalazioni relative all'azienda,
   * ad una determinata dichiarazione di consistenza di una particella
   * 
   * @param idAzienda
   * @param idDichiarazioneConsistenza
   * @param idStoricoParticella
   * @return java.util.Vector
   * @throws Exception
   */
  public Vector<DichiarazioneSegnalazioneVO> getListDichiarazioneSegnalazioni(Long idAzienda,
      Long idDichiarazioneConsistenza, Long idStoricoParticella)
      throws Exception
  {
    try
    {
      return dichiarazioneSegnalazioneDAO.getListDichiarazioneSegnalazioni(
          idAzienda, idDichiarazioneConsistenza, idStoricoParticella);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi permette di recuperare l'elenco dei controlli effettuati e le
   * rispettive segnalazioni\correzioni associate
   * 
   * @param idDichiarazioneConsistenza
   * @param idFase
   * @param errAnomaliaDicConsistenzaRicercaVO
   * @param orderBy
   * @return it.csi.solmr.dto.anag.ErrAnomaliaDicConsistenzaVO[]
   * @throws Exception
   */
  public ErrAnomaliaDicConsistenzaVO[] getListAnomalieByIdDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza, Long idFase,
      ErrAnomaliaDicConsistenzaVO errAnomaliaDicConsistenzaRicercaVO,
      String[] orderBy) throws Exception
  {
    try
    {
      return consistenzaDAO.getListAnomalieByIdDichiarazioneConsistenza(
          idDichiarazioneConsistenza, idFase,
          errAnomaliaDicConsistenzaRicercaVO, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce l'elenco delle dichiarazioni di consistenza
   * relative ad un'azienda agricola con le informazioni relative al motivo
   * della dichiarazione
   * 
   * @param idAzienda
   * @param orderBy
   * @return it.csi.solmr.dto.anag.ConsistenzaVO[]
   * @throws Exception
   */
  public ConsistenzaVO[] getListDichiarazioniConsistenzaByIdAzienda(
      Long idAzienda, String[] orderBy) throws Exception
  {
    try
    {
      return consistenzaDAO.getListDichiarazioniConsistenzaByIdAzienda(
          idAzienda, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public ConsistenzaVO[] getListDichiarazioniConsistenzaByIdAziendaVarCat(
      Long idAzienda, String[] orderBy) throws Exception
  {
    try
    {
      return consistenzaDAO.getListDichiarazioniConsistenzaByIdAziendaVarCat(
          idAzienda, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo utilizzato per ripristinare il piano di riferimento con la
   * dichiarazione di consistenza selezionata
   * 
   * @param idDichiarazioneConsistenza
   * @param idUtentte
   * 
   * @throws SolmrException
   * @throws Exception
   */
  public void ripristinaPianoRiferimento(Long idDichiarazioneConsistenza,
      Long idUtente) throws SolmrException, Exception
  {
    try
    {
      consistenzaDAO.ripristinaPianoRiferimento(idDichiarazioneConsistenza,
          idUtente);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  
  public Vector<TipoMotivoDichiarazioneVO> getListTipoMotivoDichiarazione(long idAzienda) 
      throws Exception
  {
    try
    {
      return tipoMotivoDichiarazioneDAO.getListTipoMotivoDichiarazione(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce l'elenco delle segnalazioni relative all'azienda,
   * ad una determinata dichiarazione di consistenza di una determinata unità
   * arborea
   * 
   * @param idAzienda
   * @param idDichiarazioneConsistenza
   * @param idStoricoUnitaArborea
   * @return java.util.Vector
   * @throws Exception
   */
  public Vector<DichiarazioneSegnalazioneVO> getListDichiarazioneSegnalazioniUnar(Long idAzienda,
      Long idDichiarazioneConsistenza, Long idStoricoUnitaArborea,
      String[] orderBy) throws Exception
  {
    try
    {
      return dichiarazioneSegnalazioneDAO
          .getListDichiarazioneSegnalazioniUnar(idAzienda,
              idDichiarazioneConsistenza, idStoricoUnitaArborea, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public Vector<DichiarazioneSegnalazioneVO> getListDichiarazioneSegnalazioniConsolidamentoUV(Long idAzienda) 
      throws Exception
  {
    try
    {
      return dichiarazioneSegnalazioneDAO.getListDichiarazioneSegnalazioniConsolidamentoUV(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  /**
   * Metodo per effettuare l'update di una dichiarazione di consistenza
   * 
   * @param consistenzaVO
   * @throws Exception
   */
  public void modificaDichiarazioneConsistenza(ConsistenzaVO consistenzaVO)
      throws Exception
  {
    try
    {
      consistenzaDAO.modificaDichiarazioneConsistenza(consistenzaVO);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per recuperare la dichiarazione di consistenza a partire dalla sua
   * chiave primaria
   * 
   * @param idDichiarazioneConsistenza
   * @return it.csi.solmr.dto.anag.ConsistenzaVO
   * @throws Exception
   */
  public ConsistenzaVO findDichiarazioneConsistenzaByPrimaryKey(
      Long idDichiarazioneConsistenza) throws Exception
  {
    try
    {
      return consistenzaDAO
          .findDichiarazioneConsistenzaByPrimaryKey(idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per recuperare tutti i controlli svolti in una determinata
   * dichiarazione di consistenza e relativi ad un determinato gruppo controllo
   * 
   * @param idDichiarazioneConsistenza
   * @param idGruppoControllo
   * @param orderBy[]
   * @return it.csi.solmr.dto.anag.consistenza.TipoControlloVO[]
   * @throws Exception
   */
  public TipoControlloVO[] getListTipoControlloByIdDichiarazioneConsistenzaAndIdGruppoControllo(
      Long idDicharazioneConsistenza, Long idGruppoControllo, String orderBy[])
      throws Exception
  {
    try
    {
      return tipoControlloDAO
          .getListTipoControlloByIdDichiarazioneConsistenzaAndIdGruppoControllo(
              idDicharazioneConsistenza, idGruppoControllo, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per recuperare tutti i tipi controlli relativi ad un determinato
   * gruppo controllo
   * 
   * @param idGruppoControllo
   * @param orderBy
   * @return
   * @throws Exception
   */
  public TipoControlloVO[] getListTipoControlloByIdGruppoControllo(
      Long idGruppoControllo, String orderBy[]) throws Exception
  {
    try
    {
      return tipoControlloDAO.getListTipoControlloByIdGruppoControllo(
          idGruppoControllo, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public TipoControlloVO[] getListTipoControlloByIdGruppoControlloAttivi(Long idGruppoControllo, String orderBy[])
      throws Exception
  {
    try
    {
      return tipoControlloDAO.getListTipoControlloByIdGruppoControlloAttivi(
          idGruppoControllo, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public TipoControlloVO[] getListTipoControlloForAziendaByIdGruppoControllo(
      Long idGruppoControllo, Long idAzienda, String orderBy[]) throws Exception
  {
    try
    {
      return tipoControlloDAO.getListTipoControlloForAziendaByIdGruppoControllo(
          idGruppoControllo, idAzienda, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public TipoControlloVO[] getListTipoControlloForAziendaByIdGruppoControlloAndDichCons(
      Long idGruppoControllo, Long idDichiarazioneConsistenza, String orderBy[])
  throws Exception
  {
    try
    {
      return tipoControlloDAO.getListTipoControlloForAziendaByIdGruppoControlloAndDichCons(
          idGruppoControllo, idDichiarazioneConsistenza, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  public Long getAnnoDichiarazione(Long idDichiarazioneConsistenza)
      throws Exception
  {
    try
    {
      return consistenzaDAO.getAnnoDichiarazione(idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  public Long getProcedimento(Long idAzienda, Long idDichiarazioneConsistenza)
      throws Exception
  {
    try
    {
      return consistenzaDAO.getProcedimento(idAzienda,idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  public long getLastIdDichiazioneConsistenza(Long idAzienda, Long anno)
      throws Exception
  {
    try
    {
      return consistenzaDAO.getLastIdDichiazioneConsistenza(idAzienda, anno);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  public long[] getElencoAnniDichiarazioniConsistenzaByIdParticella(
      long idParticella) throws Exception
    {
      try
      {
        SolmrLogger
            .debug(
                this,
                "[ConsistenzaBean::getElencoAnniDichiarazioniConsistenzaByIdParticella] BEGIN.");
        return consistenzaDAO
            .getElencoAnniDichiarazioniConsistenzaByIdParticella(idParticella);
      }
      catch (Exception e)
      {
        // Log dell'errore
        Parametro parametri[] = new Parametro[]
        { new Parametro("idStoricoParticella", idParticella) };
        LoggerUtils
            .logEJBError(
                this,
                "[ConsistenzaBean::getElencoAnniDichiarazioniConsistenzaByIdParticella]",
                e, null, parametri);
        throw new Exception(e.getMessage());
      }
      finally
      {
        SolmrLogger
            .debug(
                this,
                "[ConsistenzaBean::getElencoAnniDichiarazioniConsistenzaByIdParticella] END.");
      }
    }
  
  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieForControlliTerreni(Long idAzienda, Long idControllo, 
      Vector<String> vTipoErrori, boolean flagOK, String ordinamento) throws Exception
  {
    try
    {
      return consistenzaDAO
          .getErroriAnomalieForControlliTerreni(idAzienda, idControllo, vTipoErrori, flagOK, ordinamento);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }
  
  
  public String getLastAnnoCampagnaFromDichCons(long idAzienda) throws Exception
  {
    try
    {
      return consistenzaDAO
          .getLastAnnoCampagnaFromDichCons(idAzienda);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }
  
  public TipoMotivoDichiarazioneVO findTipoMotivoDichiarazioneByPrimaryKey(Long idMotivoDichiarazione) throws Exception
  {
    try
    {
      return tipoMotivoDichiarazioneDAO
          .findTipoMotivoDichiarazioneByPrimaryKey(idMotivoDichiarazione);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }
  
  public String getLastDichConsNoCorrettiva(long idAzienda) throws Exception
  {
    try
    {
      return consistenzaDAO
          .getLastDichConsNoCorrettiva(idAzienda);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }
  
  public ConsistenzaVO getUltimaDichConsNoCorrettiva(long idAzienda) throws Exception
  {
    try
    {
      return consistenzaDAO.getUltimaDichConsNoCorrettiva(idAzienda);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }
  
  public Date getLastDateDichConsNoCorrettiva(long idAzienda) throws Exception
  {
    try
    {
      return consistenzaDAO.getLastDateDichConsNoCorrettiva(idAzienda);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }
  
  public Long getLastIdDichConsProtocollata(long idAzienda) throws Exception
  {
    try
    {
      return consistenzaDAO.getLastIdDichConsProtocollata(idAzienda);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }
  
  public void updateDichiarazioneConsistenzaRichiestaStampa(Long idDichiarazioneConsistenza)
      throws Exception
  {
    try
    {
      consistenzaDAO.updateDichiarazioneConsistenzaRichiestaStampa(idDichiarazioneConsistenza);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }
  
  public void updateNoteDichiarazioneConsistenza(String note,
      long idDichiarazioneConsistenza) throws Exception
  {
    try
    {
      consistenzaDAO.updateNoteDichiarazioneConsistenza(note, idDichiarazioneConsistenza);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }
  
  public FascicoloNazionaleVO getInfoRisultatiSianDichiarazioneConsistenza(
      long idDichiarazioneConsistenza) throws Exception
  {
    try
    {
      return consistenzaDAO
          .getInfoRisultatiSianDichiarazioneConsistenza(idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public Vector<ConsistenzaVO> getListDichiarazioniPianoGrafico(Long idAzienda)
    throws Exception
  {
    try
    {
      return consistenzaDAO.getListDichiarazioniPianoGrafico(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public EsitoPianoGraficoVO getEsitoPianoGraficoFromAccesso(long idAccessoPianoGrafico)
      throws Exception
  {
    try
    {
      return consistenzaDAO.getEsitoPianoGraficoFromAccesso(idAccessoPianoGrafico);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public int preCaricamentoPianoGrafico(long idDichiarazioneConsistenza) throws Exception
  {
    try
    {
      return consistenzaDAO.preCaricamentoPianoGrafico(idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public EsitoPianoGraficoVO getEsitoPianoGraficoFromPK(long idEsitoGrafico) throws Exception
  {
    try
    {
      return consistenzaDAO.getEsitoPianoGraficoFromPK(idEsitoGrafico);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  
  public Long insertStatoIncorsoPG(long idAccessoPianoGrafico, long idUtente) 
    throws Exception
  {
    Long idAccessoPianoGraficoRt = null;
    try
    {
      consistenzaDAO.storicizzaAccessoPianoGrafico(idAccessoPianoGrafico); 
      idAccessoPianoGraficoRt = consistenzaDAO.insertAccessoPianoGraficoStatoInCorso(idAccessoPianoGrafico, idUtente);
      
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(ex.getMessage());
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(e.getMessage());
    }
    
    return idAccessoPianoGraficoRt;
  }
  
  public PlSqlCodeDescription controlliValidazionePlSql(long idAzienda, int idFase, 
      long idUtente, long idDichiarazioneConsistenza) throws Exception
  {
    try
    {
      return consistenzaDAO.controlliValidazionePlSql(idAzienda, idFase, 
          idUtente, idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
}
