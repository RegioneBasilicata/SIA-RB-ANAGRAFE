package it.csi.solmr.business.anag;

import it.csi.smranag.smrgaa.dto.ParticellaDettaglioValidazioniVO;
import it.csi.smranag.smrgaa.dto.PlSqlCodeDescription;
import it.csi.smranag.smrgaa.dto.SuperficieDescription;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.search.FiltriRicercaTerrenoImportaAsservimentoVO;
import it.csi.smranag.smrgaa.dto.search.FiltriRicercaTerrenoVO;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaTerreniImportaAsservimentoVO;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaTerreniVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoAreaVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoEfaVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoValoreAreaVO;
import it.csi.smranag.smrgaa.integration.ConduzioneParticellaGaaDAO;
import it.csi.smranag.smrgaa.integration.DocumentoGaaDAO;
import it.csi.smranag.smrgaa.integration.terreni.ParticellaGaaDAO;
import it.csi.smranag.smrgaa.integration.terreni.StoricoUnitaArboreaGaaDAO;
import it.csi.smranag.smrgaa.integration.terreni.UtilizzoParticellaGaaDAO;
import it.csi.smranag.smrgaa.util.AnagUtils;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.AnagParticellaExcelVO;
import it.csi.solmr.dto.anag.ConsistenzaVO;
import it.csi.solmr.dto.anag.DocumentoConduzioneVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.FoglioVO;
import it.csi.solmr.dto.anag.ParticellaAssVO;
import it.csi.solmr.dto.anag.ParticellaCertElegVO;
import it.csi.solmr.dto.anag.ParticellaCertificataVO;
import it.csi.solmr.dto.anag.ParticellaVO;
import it.csi.solmr.dto.anag.ProprietaCertificataVO;
import it.csi.solmr.dto.anag.TipoDocumentoVO;
import it.csi.solmr.dto.anag.services.FabbricatoParticellaVO;
import it.csi.solmr.dto.anag.terreni.AltroVitignoDichiaratoVO;
import it.csi.solmr.dto.anag.terreni.AltroVitignoVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneDichiarataVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneParticellaVO;
import it.csi.solmr.dto.anag.terreni.EsitoControlloUnarVO;
import it.csi.solmr.dto.anag.terreni.EventoParticellaVO;
import it.csi.solmr.dto.anag.terreni.FiltriParticellareRicercaVO;
import it.csi.solmr.dto.anag.terreni.FiltriUnitaArboreaRicercaVO;
import it.csi.solmr.dto.anag.terreni.MenzioneParticellaVO;
import it.csi.solmr.dto.anag.terreni.PorzioneCertificataVO;
import it.csi.solmr.dto.anag.terreni.SezioneVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaArboreaExcelVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.dto.anag.terreni.StoricoUnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.TipoCausaleModificaVO;
import it.csi.solmr.dto.anag.terreni.TipoCessazioneUnarVO;
import it.csi.solmr.dto.anag.terreni.TipoFormaAllevamentoVO;
import it.csi.solmr.dto.anag.terreni.TipoGenereIscrizioneVO;
import it.csi.solmr.dto.anag.terreni.TipoImpiantoVO;
import it.csi.solmr.dto.anag.terreni.TipoMacroUsoVO;
import it.csi.solmr.dto.anag.terreni.TipoMenzioneGeograficaVO;
import it.csi.solmr.dto.anag.terreni.TipoPiantaConsociataVO;
import it.csi.solmr.dto.anag.terreni.TipoTipologiaVinoVO;
import it.csi.solmr.dto.anag.terreni.TipoUtilizzoVO;
import it.csi.solmr.dto.anag.terreni.TipoVarietaVO;
import it.csi.solmr.dto.anag.terreni.TipoVinoVO;
import it.csi.solmr.dto.anag.terreni.UnitaArboreaDichiarataVO;
import it.csi.solmr.dto.anag.terreni.UnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.UtilizzoConsociatoDichiaratoVO;
import it.csi.solmr.dto.anag.terreni.UtilizzoConsociatoVO;
import it.csi.solmr.dto.anag.terreni.UtilizzoDichiaratoVO;
import it.csi.solmr.dto.anag.terreni.UtilizzoParticellaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.integration.CommonDAO;
import it.csi.solmr.integration.anag.AllevamentoAnagDAO;
import it.csi.solmr.integration.anag.AltroVitignoDAO;
import it.csi.solmr.integration.anag.AltroVitignoDichiaratoDAO;
import it.csi.solmr.integration.anag.AnagrafeDAO;
import it.csi.solmr.integration.anag.ConduzioneDichiarataDAO;
import it.csi.solmr.integration.anag.ConduzioneParticellaDAO;
import it.csi.solmr.integration.anag.ConsistenzaDAO;
import it.csi.solmr.integration.anag.DichiarazioneSegnalazioneDAO;
import it.csi.solmr.integration.anag.DocumentoDAO;
import it.csi.solmr.integration.anag.EsitoControlloUnarDAO;
import it.csi.solmr.integration.anag.EventoParticellaDAO;
import it.csi.solmr.integration.anag.FabbricatoDAO;
import it.csi.solmr.integration.anag.FoglioDAO;
import it.csi.solmr.integration.anag.ParticellaCertificataDAO;
import it.csi.solmr.integration.anag.ParticellaDAO;
import it.csi.solmr.integration.anag.PorzioneCertificataDAO;
import it.csi.solmr.integration.anag.StoricoParticellaDAO;
import it.csi.solmr.integration.anag.StoricoUnitaArboreaDAO;
import it.csi.solmr.integration.anag.TipoCausaleModificaDAO;
import it.csi.solmr.integration.anag.TipoCessazioneUnarDAO;
import it.csi.solmr.integration.anag.TipoDocumentoDAO;
import it.csi.solmr.integration.anag.TipoEventoDAO;
import it.csi.solmr.integration.anag.TipoFormaAllevamentoDAO;
import it.csi.solmr.integration.anag.TipoGenereIscrizioneDAO;
import it.csi.solmr.integration.anag.TipoMenzioneGeograficaDAO;
import it.csi.solmr.integration.anag.TipoTipologiaVinoDAO;
import it.csi.solmr.integration.anag.TipoUtilizzoDAO;
import it.csi.solmr.integration.anag.TipoVarietaDAO;
import it.csi.solmr.integration.anag.TipoVinoDAO;
import it.csi.solmr.integration.anag.UnitaArboreaDAO;
import it.csi.solmr.integration.anag.UnitaArboreaDichiarataDAO;
import it.csi.solmr.integration.anag.UtilizzoConsociatoDAO;
import it.csi.solmr.integration.anag.UtilizzoConsociatoDichDAO;
import it.csi.solmr.integration.anag.UtilizzoDichiaratoDAO;
import it.csi.solmr.integration.anag.UtilizzoParticellaDAO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.NumberUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;
import it.csi.solmr.util.services.AgriLogger;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Stateless(name="comp/env/solmr/anag/GestioneTereni",mappedName="comp/env/solmr/anag/GestioneTerreni")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(value=javax.ejb.TransactionAttributeType.REQUIRED)
public class GestioneTerreniBean implements GestioneTerreniLocal
{

  private static final long serialVersionUID = 5338343328677239653L;
  SessionContext sessionContext;
  private transient ParticellaDAO particellaDAO = null;
  private transient ParticellaGaaDAO particellaGaaDAO = null;
  private transient StoricoParticellaDAO storicoParticellaDAO = null;
  private transient ConduzioneParticellaDAO conduzioneParticellaDAO = null;
  private transient ConduzioneParticellaGaaDAO conduzioneParticellaGaaDAO = null;
  private transient ConduzioneDichiarataDAO conduzioneDichiarataDAO = null;
  private transient UtilizzoParticellaDAO utilizzoParticellaDAO = null;
  private transient UtilizzoParticellaGaaDAO utilizzoParticellaGaaDAO = null;
  private transient UtilizzoDichiaratoDAO utilizzoDichiaratoDAO = null;
  private transient CommonDAO commonDAO = null;
  private transient ParticellaCertificataDAO particellaCertificataDAO = null;
  private transient PorzioneCertificataDAO porzioneCertificataDAO = null;
  private transient FoglioDAO foglioDAO = null;
  private transient DocumentoDAO documentoDAO = null;
  private transient DocumentoGaaDAO documentoGaaDAO = null;
  private transient TipoDocumentoDAO tipoDocumentoDAO = null;
  private transient TipoVarietaDAO tipoVarietaDAO = null;
  private transient TipoUtilizzoDAO tipoUtilizzoDAO = null;
  private transient FabbricatoDAO fabbricatoDAO = null;
  private transient UtilizzoConsociatoDAO utilizzoConsociatoDAO = null;
  private transient UtilizzoConsociatoDichDAO utilizzoConsociatoDichDAO = null;
  private transient EventoParticellaDAO eventoParticellaDAO = null;
  private transient AllevamentoAnagDAO allevamentoAnagDAO = null;
  private transient StoricoUnitaArboreaDAO storicoUnitaArboreaDAO = null;
  private transient StoricoUnitaArboreaGaaDAO storicoUnitaArboreaGaaDAO = null;
  private transient EsitoControlloUnarDAO esitoControlloUnarDAO = null;
  private transient AltroVitignoDAO altroVitignoDAO = null;
  private transient TipoFormaAllevamentoDAO tipoFormaAllevamentoDAO = null;
  private transient TipoCausaleModificaDAO tipoCausaleModificaDAO = null;
  private transient TipoCessazioneUnarDAO tipoCessazioneUnarDAO = null;
  private transient UnitaArboreaDAO unitaArboreaDAO = null;
  private transient UnitaArboreaDichiarataDAO unitaArboreaDichiarataDAO = null;
  private transient AltroVitignoDichiaratoDAO altroVitignoDichiaratoDAO = null;
  private transient TipoVinoDAO tipoVinoDAO = null;
  private transient TipoTipologiaVinoDAO tipoTipologiaVinoDAO = null;
  private transient ConsistenzaDAO consistenzaDAO = null;
  private transient AnagrafeDAO anagrafeDAO = null;
  private transient DichiarazioneSegnalazioneDAO dichiarazioneSegnalazioneDAO = null;
  private transient TipoMenzioneGeograficaDAO tipoMenzioneGeograficaDAO = null;
  private transient TipoGenereIscrizioneDAO tipoGenereIscrizioneDAO = null;
  private transient TipoEventoDAO tipoEventoDAO = null;
  //private transient RegistroPascoloGaaDAO registroPascoloGaaDAO = null;

 
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
      particellaDAO = new ParticellaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      particellaGaaDAO = new ParticellaGaaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      storicoParticellaDAO = new StoricoParticellaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      conduzioneParticellaDAO = new ConduzioneParticellaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      conduzioneParticellaGaaDAO = new ConduzioneParticellaGaaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      conduzioneDichiarataDAO = new ConduzioneDichiarataDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      utilizzoParticellaDAO = new UtilizzoParticellaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      utilizzoParticellaGaaDAO = new UtilizzoParticellaGaaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      utilizzoDichiaratoDAO = new UtilizzoDichiaratoDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      commonDAO = new CommonDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      particellaCertificataDAO = new ParticellaCertificataDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      porzioneCertificataDAO = new PorzioneCertificataDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      foglioDAO = new FoglioDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      documentoDAO = new DocumentoDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      documentoGaaDAO = new DocumentoGaaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      tipoDocumentoDAO = new TipoDocumentoDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      tipoVarietaDAO = new TipoVarietaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      tipoUtilizzoDAO = new TipoUtilizzoDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      fabbricatoDAO = new FabbricatoDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      utilizzoConsociatoDAO = new UtilizzoConsociatoDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      utilizzoConsociatoDichDAO = new UtilizzoConsociatoDichDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      eventoParticellaDAO = new EventoParticellaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      allevamentoAnagDAO = new AllevamentoAnagDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      storicoUnitaArboreaDAO = new StoricoUnitaArboreaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      storicoUnitaArboreaGaaDAO = new StoricoUnitaArboreaGaaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      esitoControlloUnarDAO = new EsitoControlloUnarDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      altroVitignoDAO = new AltroVitignoDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      tipoFormaAllevamentoDAO = new TipoFormaAllevamentoDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      tipoCausaleModificaDAO = new TipoCausaleModificaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      tipoCessazioneUnarDAO = new TipoCessazioneUnarDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      unitaArboreaDAO = new UnitaArboreaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      unitaArboreaDichiarataDAO = new UnitaArboreaDichiarataDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      altroVitignoDichiaratoDAO = new AltroVitignoDichiaratoDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      tipoVinoDAO = new TipoVinoDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      tipoTipologiaVinoDAO = new TipoTipologiaVinoDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      consistenzaDAO = new ConsistenzaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      anagrafeDAO = new AnagrafeDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      dichiarazioneSegnalazioneDAO = new DichiarazioneSegnalazioneDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      tipoMenzioneGeograficaDAO = new TipoMenzioneGeograficaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      tipoGenereIscrizioneDAO = new TipoGenereIscrizioneDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      tipoEventoDAO = new TipoEventoDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      //registroPascoloGaaDAO = new RegistroPascoloGaaDAO(
        //  SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
    }
    catch (ResourceAccessException ex)
    {
      SolmrLogger.fatal(this, ex.getMessage());
      throw new EJBException(ex);
    }
  }

  /**
   * Metodo che mi restituisce l'elenco dei comuni su cui insistono le
   * particelle
   * 
   * @param idAzienda
   * @param onlyActive
   * @param orderBy
   * @return java.util.Vector
   * @throws Exception
   */
  public Vector<ComuneVO> getListComuniParticelleByIdAzienda(Long idAzienda,
      boolean onlyActive, String[] orderBy) throws Exception
  {
    try
    {
      return storicoParticellaDAO.getListComuniParticelleByIdAzienda(idAzienda,
          onlyActive, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public Vector<ComuneVO> getListComuniParticelleByIdDichiarazioneConsistenza(Long idDichiarazioneConsistenza, String[] orderBy)
    throws Exception
  {
    try
    {
      return storicoParticellaDAO.getListComuniParticelleByIdDichiarazioneConsistenza(idDichiarazioneConsistenza, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce l'elenco di tutti gli uso del suolo utilizzati
   * dall'azienda in esame
   * 
   * @param idAzienda
   * @param onlyActive
   * @param orderBy
   * @return java.util.Vector
   * @throws Exception
   */
  /*public Vector<TipoUtilizzoVO> getListTipiUsoSuoloByIdAzienda(Long idAzienda,
      boolean onlyActive, String[] orderBy) throws Exception
  {
    try
    {
      return tipoUtilizzoDAO.getListTipiUsoSuoloByIdAzienda(idAzienda,
          onlyActive, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }*/
  
  /*public Vector<TipoUtilizzoVO> getListTipiDestinazProdPrimSecByIdAzienda(Long idAzienda)
    throws Exception
  {
    try
    {
      return tipoUtilizzoDAO.getListTipiDestinazProdPrimSecByIdAzienda(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }*/
  
  public Vector<TipoUtilizzoVO> getListUtilizziElencoTerrPianoLavorazione(Long idAzienda)
    throws Exception
  {
    try
    {
      return tipoUtilizzoDAO.getListUtilizziElencoTerrPianoLavorazione(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public Vector<TipoUtilizzoVO> getListUtilizziElencoTerrPianoLavorazioneStor(Long idAzienda) 
    throws Exception
  {
    try
    {
      return tipoUtilizzoDAO.getListUtilizziElencoTerrPianoLavorazioneStor(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public Vector<TipoUtilizzoVO> getListUtilizziElencoTerrValidazione(Long idDichiarazioneConsistenza) 
    throws Exception
  {
    try
    {
      return tipoUtilizzoDAO.getListUtilizziElencoTerrValidazione(idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public Vector<TipoUtilizzoVO> getListTipiUsoSuoloByIdDichCons(long idDichiarazioneConsistenza, String[] orderBy)
    throws Exception
  {
    try
    {
      return tipoUtilizzoDAO.getListTipiUsoSuoloByIdDichCons(idDichiarazioneConsistenza, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce l'ultima data nella quale sono stati effettuati
   * controlli di validità relativi alla tabella DB_CONDUZIONE_PARTICELLA
   * 
   * @param idAzienda
   * @return java.lang.String
   * @throws Exception
   */
  public java.lang.String getMaxDataEsecuzioneControlliConduzioneParticella(
      Long idAzienda) throws Exception
  {
    try
    {
      return conduzioneParticellaDAO
          .getMaxDataEsecuzioneControlliConduzioneParticella(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce l'ultima data nella quale sono stati effettuati
   * controlli di validità relativi alla tabella DB_CONDUZIONE_DICHIARATA
   * 
   * @param idAzienda
   * @return java.lang.String
   * @throws Exception
   */
  public java.lang.String getMaxDataEsecuzioneControlliConduzioneDichiarata(
      Long idAzienda) throws Exception
  {
    try
    {
      return conduzioneDichiarataDAO
          .getMaxDataEsecuzioneControlliConduzioneDichiarata(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce l'elenco delle particelle in tutte le sue
   * componenti (DB_STORICO_PARTICELLA,
   * DB_CONDUZIONE_PARTICELLA/DB_CONDUZIONE_DICHIARATA,
   * DB_UTILIZZO_PARTICELLA/DB_UTILIZZO_DICHIARATO) in relazione dei parametri
   * di ricerca impostati, di un criterio di ordinamento e dell'azienda
   * selezionata
   * 
   * @param filtriParticellareRicercaVO
   * @param orderBy
   * @param java.lang.Long
   *          idAzienda
   * @return java.util.Vector
   * @throws Exception
   */
  public Vector<StoricoParticellaVO> searchListParticelleByParameters(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda)
      throws Exception
  {
	SolmrLogger.debug(this,"  BEGIN searchListParticelleByParameters");  
    try
    {
      // Ricerca per piano di lavorazione corrente o comprensivo di conduzioni
      // storicizzate
      if (filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() <= 0)
      {
    	SolmrLogger.debug(this,"-- Ricerca per piano di lavorazione corrente o comprensivo di conduzioni storicizzate");  
        String parametroP26 = "";
        BigDecimal p26Valore = null;
        try
        {
          parametroP26 = commonDAO.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_P26P);
          SolmrLogger.debug(this,"-- parametroP26 ="+parametroP26);
        }
        catch (SolmrException sme)
        {
          throw new Exception(sme.getMessage());
        }
        
        try
        {
          parametroP26 = parametroP26.replace(',','.');
          p26Valore = new BigDecimal(parametroP26);
          SolmrLogger.debug(this,"-- p26Valore ="+p26Valore);
        }
        catch (Exception sme)
        {
          p26Valore = new BigDecimal(0);
        }
        
        return conduzioneParticellaDAO.searchParticelleByParameters(
            filtriParticellareRicercaVO, idAzienda, p26Valore);
      }
      // Altrimenti vado per dichiarazioni di consistenza
      else
      {
    	SolmrLogger.debug(this,"-- Ricerca per dichiarazioni di consistenza");
        ConsistenzaVO consistenzaVO = consistenzaDAO
            .findDichiarazioneConsistenzaByPrimaryKey(filtriParticellareRicercaVO
                .getIdPianoRiferimento());
        filtriParticellareRicercaVO
            .setDataInserimentoDichiarazione(consistenzaVO
                .getDataInserimentoDichiarazione());
        return conduzioneDichiarataDAO.searchParticelleDichiarateByParameters(
            filtriParticellareRicercaVO, idAzienda);
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    finally{
    	SolmrLogger.debug(this,"  END searchListParticelleByParameters");
    }
  }

  /**
   * Metodo che mi restituisce il record di DB_STORICO_PARTICELLA a partire
   * dall'id_conduzione_particella
   * 
   * @param idConduzioneParticella
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO
   * @throws Exception
   */
  public StoricoParticellaVO findStoricoParticellaVOByIdConduzioneParticella(
      Long idConduzioneParticella) throws Exception
  {
    StoricoParticellaVO storicoParticellaVO = null;
    DocumentoVO documentoVO = null;
    TipoDocumentoVO tipoDocumentoVO = null;
    try
    {
      storicoParticellaVO = storicoParticellaDAO
          .findStoricoParticellaVOByIdConduzioneParticella(idConduzioneParticella);
      FoglioVO foglioVO = foglioDAO.findFoglioByParameters(storicoParticellaVO
          .getIstatComune(), storicoParticellaVO.getFoglio(),
          storicoParticellaVO.getSezione());
      storicoParticellaVO.setFoglioVO(foglioVO);
      if (Validator
          .isNotEmpty(storicoParticellaVO.getIdDocumentoProtocollato()))
      {
        documentoVO = documentoDAO
            .findDocumentoVOByPrimaryKey(storicoParticellaVO
                .getIdDocumentoProtocollato());
        tipoDocumentoVO = tipoDocumentoDAO
            .findTipoDocumentoVOByPrimaryKey(documentoVO.getExtIdDocumento());
        documentoVO.setTipoDocumentoVO(tipoDocumentoVO);
        storicoParticellaVO.setDocumentoVO(documentoVO);
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return storicoParticellaVO;
  }
  
  
  public Long findIdUteByIdCondPartIdAz(Long idConduzioneParticella, Long idAzienda) throws Exception{    
	try{
	  return storicoParticellaDAO.findIdUteByIdCondPartIdAz(idConduzioneParticella, idAzienda);				  		
	}
	catch (DataAccessException dae){
	  AgriLogger.error(this, "-- DataAccessException in findIdUteByIdCondPartIdAz ="+dae.getMessage());
      throw new Exception(dae.getMessage());
	}	
  }
  
  
  public long getIdConduzioneDichiarata(long idConduzioneParticella,long idDichiarazioneConsistenza) 
    throws Exception
  {
    try
    {
      return storicoParticellaDAO.getIdConduzioneDichiarata(idConduzioneParticella,idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce il record di DB_STORICO_PARTICELLA a partire
   * dall'id_conduzione_dichiarata
   * 
   * @param idConduzioneDichiarata
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO
   * @throws Exception
   */
  public StoricoParticellaVO findStoricoParticellaVOByIdConduzioneDichiarata(
      Long idConduzioneDichiarata) throws Exception
  {
    StoricoParticellaVO storicoParticellaVO = null;
    DocumentoVO documentoVO = null;
    TipoDocumentoVO tipoDocumentoVO = null;
    try
    {
      storicoParticellaVO = storicoParticellaDAO
          .findStoricoParticellaVOByIdConduzioneDichiarata(idConduzioneDichiarata);
      FoglioVO foglioVO = foglioDAO.findFoglioByParameters(storicoParticellaVO
          .getIstatComune(), storicoParticellaVO.getFoglio(),
          storicoParticellaVO.getSezione());
      storicoParticellaVO.setFoglioVO(foglioVO);
      if (Validator
          .isNotEmpty(storicoParticellaVO.getIdDocumentoProtocollato()))
      {
        documentoVO = documentoDAO
            .findDocumentoVOByPrimaryKey(storicoParticellaVO
                .getIdDocumentoProtocollato());
        tipoDocumentoVO = tipoDocumentoDAO
            .findTipoDocumentoVOByPrimaryKey(documentoVO.getExtIdDocumento());
        documentoVO.setTipoDocumentoVO(tipoDocumentoVO);
        storicoParticellaVO.setDocumentoVO(documentoVO);
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return storicoParticellaVO;
  }

  /**
   * Metodo che mi restituisce il record su DB_CONDUZIONE_PARTICELLA a partire
   * dalla chiave primaria
   * 
   * @param idConduzioneParticella
   * @return it.csi.solmr.dto.anag.terreni.ConduzioneParticellaVO
   * @throws Exception
   */
  public ConduzioneParticellaVO findConduzioneParticellaByPrimaryKey(
      Long idConduzioneParticella) throws Exception
  {
    try
    {
      return conduzioneParticellaDAO
          .findConduzioneParticellaByPrimaryKey(idConduzioneParticella);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce il record su DB_CONDUZIONE_DICHIARATA a partire
   * dalla chiave primaria
   * 
   * @param idConduzioneDichiarata
   * @return it.csi.solmr.dto.anag.terreni.ConduzioneDichiarataVO
   * @throws Exception
   */
  public ConduzioneDichiarataVO findConduzioneDichiarataByPrimaryKey(
      Long idConduzioneDichiarata) throws Exception
  {
    try
    {
      return conduzioneDichiarataDAO
          .findConduzioneDichiarataByPrimaryKey(idConduzioneDichiarata);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che si occupa di reperire il dettaglio dei dati relativi alla
   * particella (DB_STORICO_PARTICELLA, DB_CONDUZIONE_PARTICELLA/DICHIARATA,
   * DB_UTILIZZO_PARTICELLA/DICHIARATA) a partire
   * dall'id_conduzione_particella/dichiarata
   * 
   * @param filtriParticellareRicercaVO
   * @param idConduzione
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO
   * @throws Exception
   */
  public StoricoParticellaVO getDettaglioParticella(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idConduzione)
      throws Exception
  {
    StoricoParticellaVO storicoParticellaVO = null;
    try
    {
      if (filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() <= 0)
      {
        storicoParticellaVO = storicoParticellaDAO
            .findStoricoParticellaVOByIdConduzioneParticella(idConduzione);
        
        // Cerco i dati del foglio associati allo storico particella
        FoglioVO foglioVO = foglioDAO.findFoglioByParameters(storicoParticellaVO
            .getIstatComune(), storicoParticellaVO.getFoglio(),
            storicoParticellaVO.getSezione());
        storicoParticellaVO.setFoglioVO(foglioVO);
        
        storicoParticellaVO.setvValoriTipoArea(particellaGaaDAO
            .getValoriTipoAreaParticella(storicoParticellaVO.getIdParticella(), null));
        
        ConduzioneParticellaVO conduzioneParticellaVO = conduzioneParticellaDAO
            .findConduzioneParticellaByPrimaryKey(idConduzione);
        UtilizzoParticellaVO[] elencoUtilizzi = null;
        if (filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() < 0)
        {
          elencoUtilizzi = utilizzoParticellaDAO
              .getListUtilizzoParticellaVOByIdConduzioneParticella(
                  idConduzione, null, true);
        }
        else
        {
          elencoUtilizzi = utilizzoParticellaDAO
              .getListUtilizzoParticellaVOByIdConduzioneParticella(
                  idConduzione, null, false);
        }
        UtilizzoParticellaVO[] newElencoUtilizzi = null;
        
        /*double maxSupGrafCatNormalizzata = 0;
        if(Validator.isNotEmpty(foglioVO)
            && Validator.isNotEmpty(foglioVO.getFlagStabilizzazione())
            && (foglioVO.getFlagStabilizzazione().compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0)
            && Validator.validateDouble(storicoParticellaVO.getSuperficieGrafica(), SolmrConstants.FORMAT_SUP_UTILIZZATA) != null 
            && Double.parseDouble(storicoParticellaVO.getSuperficieGrafica().replace(',', '.')) > 0)
        {
          maxSupGrafCatNormalizzata = conduzioneParticellaDAO
              .getSupGrafNormalizIdConduzioneParticella(idConduzione);
        }
        
        if(maxSupGrafCatNormalizzata == 0)
        {
          maxSupGrafCatNormalizzata = conduzioneParticellaDAO
              .getMaxSupGrafCatIdConduzioneParticella(idConduzione);  
        }*/
        
        double supGrafCatNormalizzata = 0;
        if(Validator.validateDouble(storicoParticellaVO.getSuperficieGrafica(), SolmrConstants.FORMAT_SUP_UTILIZZATA) != null 
            && Double.parseDouble(storicoParticellaVO.getSuperficieGrafica().replace(',', '.')) > 0)
        {
          supGrafCatNormalizzata = conduzioneParticellaDAO
              .getSupGrafNormalizIdConduzioneParticella(idConduzione);
        }
        
        if(supGrafCatNormalizzata == 0)
        {
          supGrafCatNormalizzata = conduzioneParticellaDAO
              .getSupCatNormalizIdConduzioneParticella(idConduzione);  
        }
        
        if (elencoUtilizzi != null && elencoUtilizzi.length > 0)
        {
          double totSupUtilizzata = utilizzoParticellaDAO
              .getTotSupUtilizzataByIdConduzioneParticella(idConduzione);
          // Se il totale delle superfici utilizzate non raggiunge la
          // min sup grafica/catastale normalizzata alla percentuale possesso
          // rappresento il caso del "senza uso del suolo specificato"
          if (NumberUtils.arrotonda(totSupUtilizzata, 4) < NumberUtils
              .arrotonda(supGrafCatNormalizzata, 4))
          {
            newElencoUtilizzi = new UtilizzoParticellaVO[elencoUtilizzi.length + 1];
            newElencoUtilizzi[(newElencoUtilizzi.length) - 1] = new UtilizzoParticellaVO();
            newElencoUtilizzi[(newElencoUtilizzi.length) - 1]
                .setIdUtilizzoParticella(new Long(-1));
            newElencoUtilizzi[(newElencoUtilizzi.length) - 1]
                .setIdConduzioneParticella(idConduzione);
            newElencoUtilizzi[(newElencoUtilizzi.length) - 1]
                .setSuperficieUtilizzata(String.valueOf(NumberUtils
                    .arrotonda(supGrafCatNormalizzata, 4) - NumberUtils.arrotonda(totSupUtilizzata, 4)));
            // Creo inoltre un utilizzo consociato vuoto
            Vector<UtilizzoConsociatoVO> elencoConsociati = utilizzoConsociatoDAO
                .getListUtilizziConsociatiByIdUtilizzoParticella(
                    newElencoUtilizzi[(newElencoUtilizzi.length) - 1]
                        .getIdUtilizzoParticella(), null);
            if (elencoConsociati != null && elencoConsociati.size() > 0)
            {
              UtilizzoConsociatoVO[] elencoUtilizziConsociati = new UtilizzoConsociatoVO[elencoConsociati
                  .size()];
              for (int a = 0; a < elencoConsociati.size(); a++)
              {
                elencoUtilizziConsociati[a] = (UtilizzoConsociatoVO) elencoConsociati
                    .elementAt(a);
              }
              newElencoUtilizzi[(newElencoUtilizzi.length) - 1]
                  .setElencoUtilizziConsociati(elencoUtilizziConsociati);
            }
          }
          else
          {
            newElencoUtilizzi = new UtilizzoParticellaVO[elencoUtilizzi.length];
          }
          for (int i = 0; i < elencoUtilizzi.length; i++)
          {
            UtilizzoParticellaVO utilizzoParticellaVO = (UtilizzoParticellaVO) elencoUtilizzi[i];
            totSupUtilizzata += Double.parseDouble(utilizzoParticellaVO
                .getSuperficieUtilizzata().replace(',', '.'));
            Vector<UtilizzoConsociatoVO> elencoConsociati = utilizzoConsociatoDAO
                .getListUtilizziConsociatiByIdUtilizzoParticella(
                    utilizzoParticellaVO.getIdUtilizzoParticella(), null);
            if (elencoConsociati != null && elencoConsociati.size() > 0)
            {
              UtilizzoConsociatoVO[] elencoUtilizziConsociati = new UtilizzoConsociatoVO[elencoConsociati
                  .size()];
              for (int a = 0; a < elencoConsociati.size(); a++)
              {
                elencoUtilizziConsociati[a] = (UtilizzoConsociatoVO) elencoConsociati
                    .elementAt(a);
              }
              utilizzoParticellaVO
                  .setElencoUtilizziConsociati(elencoUtilizziConsociati);
            }
            if (utilizzoParticellaVO.getIdImpianto() != null)
            {
              TipoImpiantoVO tipoImpiantoVO = commonDAO
                  .findTipoImpiantoByPrimaryKey(utilizzoParticellaVO
                      .getIdImpianto());
              utilizzoParticellaVO.setTipoImpiantoVO(tipoImpiantoVO);
            }
            newElencoUtilizzi[i] = utilizzoParticellaVO;
          }
        }
        // Se non ho reperito nessun utilizzo allora ne creo uno che
        // rappresenti il caso del "senza uso del suolo specificato"
        else
        {
          newElencoUtilizzi = new UtilizzoParticellaVO[1];
          UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
          utilizzoParticellaVO.setIdUtilizzoParticella(new Long(-1));
          utilizzoParticellaVO.setIdConduzioneParticella(idConduzione);
          utilizzoParticellaVO.setSuperficieUtilizzata(String.valueOf(NumberUtils
              .arrotonda(supGrafCatNormalizzata, 4)));
          // Creo inoltre un utilizzo consociato vuoto
          Vector<UtilizzoConsociatoVO> elencoConsociati = utilizzoConsociatoDAO
              .getListUtilizziConsociatiByIdUtilizzoParticella(
                  utilizzoParticellaVO.getIdUtilizzoParticella(), null);
          if (elencoConsociati != null && elencoConsociati.size() > 0)
          {
            UtilizzoConsociatoVO[] elencoUtilizziConsociati = new UtilizzoConsociatoVO[elencoConsociati
                .size()];
            for (int a = 0; a < elencoConsociati.size(); a++)
            {
              elencoUtilizziConsociati[a] = (UtilizzoConsociatoVO) elencoConsociati
                  .elementAt(a);
            }
            utilizzoParticellaVO
                .setElencoUtilizziConsociati(elencoUtilizziConsociati);
          }
          newElencoUtilizzi[0] = utilizzoParticellaVO;
        }
        conduzioneParticellaVO.setElencoUtilizzi(newElencoUtilizzi);
        ConduzioneParticellaVO[] elencoConduzioni = { conduzioneParticellaVO };
        storicoParticellaVO.setElencoConduzioni(elencoConduzioni);
      }
      else
      {
        storicoParticellaVO = storicoParticellaDAO
            .findStoricoParticellaVOByIdConduzioneDichiarata(idConduzione);
        
        // Cerco i dati del foglio associati allo storico particella
        FoglioVO foglioVO = foglioDAO.findFoglioByParameters(storicoParticellaVO
            .getIstatComune(), storicoParticellaVO.getFoglio(),
            storicoParticellaVO.getSezione());
        storicoParticellaVO.setFoglioVO(foglioVO);
        
        ConsistenzaVO consistenzaVO = consistenzaDAO
            .findDichiarazioneConsistenzaByPrimaryKey(filtriParticellareRicercaVO.getIdPianoRiferimento());
        storicoParticellaVO.setvValoriTipoArea(particellaGaaDAO
            .getValoriTipoAreaParticella(storicoParticellaVO.getIdParticella(), consistenzaVO.getDataInserimentoDichiarazione()));
        
        ConduzioneDichiarataVO conduzioneDichiarataVO = conduzioneDichiarataDAO
            .findConduzioneDichiarataByPrimaryKey(idConduzione);
        UtilizzoDichiaratoVO[] elencoUtilizziDichiarati = utilizzoDichiaratoDAO
            .getListUtilizzoDichiaratoVOByIdDichiarazioneConsistenzaAndIdConduzioneParticella(
                filtriParticellareRicercaVO.getIdPianoRiferimento(),
                conduzioneDichiarataVO.getIdConduzioneParticella(), null);
        if (elencoUtilizziDichiarati != null
            && elencoUtilizziDichiarati.length > 0)
        {
          for (int i = 0; i < elencoUtilizziDichiarati.length; i++)
          {
            UtilizzoDichiaratoVO utilizzoDichiaratoVO = (UtilizzoDichiaratoVO) elencoUtilizziDichiarati[i];
            UtilizzoConsociatoDichiaratoVO[] elencoUtilizzoConsociatiDichiarati = utilizzoDichiaratoDAO
                .getListUtilizziConsociatiDichiaratiByIdUtilizzoDichiarato(
                    utilizzoDichiaratoVO.getIdUtilizzoDichiarato(), null);
            utilizzoDichiaratoVO
                .setElencoUtilizziConsociatiDich(elencoUtilizzoConsociatiDichiarati);
            if (utilizzoDichiaratoVO.getIdImpianto() != null)
            {
              TipoImpiantoVO tipoImpiantoVO = commonDAO
                  .findTipoImpiantoByPrimaryKey(utilizzoDichiaratoVO
                      .getIdImpianto());
              utilizzoDichiaratoVO.setTipoImpiantoVO(tipoImpiantoVO);
            }
            elencoUtilizziDichiarati[i] = utilizzoDichiaratoVO;
          }
        }
        conduzioneDichiarataVO.setElencoUtilizzi(elencoUtilizziDichiarati);
        ConduzioneDichiarataVO[] elencoConduzioniDichiarate = { conduzioneDichiarataVO };
        storicoParticellaVO
            .setElencoConduzioniDichiarate(elencoConduzioniDichiarate);
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return storicoParticellaVO;
  }

  /**
   * Metodo che si occupa di reperire i dati dalla tabella
   * DB_TIPO_PIANTE_CONSOCIATE
   * 
   * @param onlyActive
   * @return java.util.Vector
   * @throws Exception
   */
  public Vector<TipoPiantaConsociataVO> getListPianteConsociate(boolean onlyActive)
      throws Exception
  {
    try
    {
      return commonDAO.getListPianteConsociate(onlyActive);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce l'elenco degli utilizzi a partire
   * dall'id_conduzione_particella
   * 
   * @param idConduzioneParticella
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.UtilizzoParticellaVO[]
   * @throws Exception
   */
  public UtilizzoParticellaVO[] getListUtilizzoParticellaVOByIdConduzioneParticella(
      Long idConduzioneParticella, String[] orderBy, boolean onlyActive)
      throws Exception
  {
    UtilizzoParticellaVO[] elencoUtilizzi = null;
    try
    {
      elencoUtilizzi = utilizzoParticellaDAO
          .getListUtilizzoParticellaVOByIdConduzioneParticella(
              idConduzioneParticella, orderBy, onlyActive);
      if (elencoUtilizzi != null && elencoUtilizzi.length > 0)
      {
        Vector<UtilizzoConsociatoVO> elencoConsociati = null;
        for (int i = 0; i < elencoUtilizzi.length; i++)
        {
          UtilizzoParticellaVO utilizzoParticellaVO = (UtilizzoParticellaVO) elencoUtilizzi[i];
          elencoConsociati = utilizzoConsociatoDAO
              .getListUtilizziConsociatiByIdUtilizzoParticella(
                  utilizzoParticellaVO.getIdUtilizzoParticella(), orderBy);
          UtilizzoConsociatoVO[] elencoUtilizziConsociati = new UtilizzoConsociatoVO[elencoConsociati
              .size()];
          for (int a = 0; a < elencoConsociati.size(); a++)
          {
            elencoUtilizziConsociati[a] = (UtilizzoConsociatoVO) elencoConsociati
                .elementAt(a);
          }
          utilizzoParticellaVO
              .setElencoUtilizziConsociati(elencoUtilizziConsociati);
          elencoUtilizzi[i] = utilizzoParticellaVO;
        }
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return elencoUtilizzi;
  }

  /**
   * Metodo che mi restituisce l'elenco degli utilizzi a partire
   * dall'id_dichiarazione_consistenza e dell'id_conduzione_particella in modo
   * da reperire solo quelli della particella selezionata
   * 
   * @param idDichiarazioneConsistenza
   * @param idConduzioneParticella
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.UtilizzoDichiaratoVO[]
   * @throws Exception
   */
  public UtilizzoDichiaratoVO[] getListUtilizzoDichiaratoVOByIdDichiarazioneConsistenzaAndIdConduzioneParticella(
      Long idDichiarazioneConsistenza, Long idConduzioneParticella,
      String[] orderBy) throws Exception
  {
    UtilizzoDichiaratoVO[] elencoUtilizziDichiarati = null;
    try
    {
      elencoUtilizziDichiarati = utilizzoDichiaratoDAO
          .getListUtilizzoDichiaratoVOByIdDichiarazioneConsistenzaAndIdConduzioneParticella(
              idDichiarazioneConsistenza, idConduzioneParticella, orderBy);
      if (elencoUtilizziDichiarati != null
          && elencoUtilizziDichiarati.length > 0)
      {
        // Vector elencoConsociati = null;
        for (int i = 0; i < elencoUtilizziDichiarati.length; i++)
        {
          UtilizzoDichiaratoVO utilizzoDichiaratoVO = (UtilizzoDichiaratoVO) elencoUtilizziDichiarati[i];
          UtilizzoConsociatoDichiaratoVO[] elencoConsociatiDich = utilizzoDichiaratoDAO
              .getListUtilizziConsociatiDichiaratiByIdUtilizzoDichiarato(
                  utilizzoDichiaratoVO.getIdUtilizzoDichiarato(), orderBy);
          utilizzoDichiaratoVO
              .setElencoUtilizziConsociatiDich(elencoConsociatiDich);
          elencoUtilizziDichiarati[i] = utilizzoDichiaratoVO;
        }
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return elencoUtilizziDichiarati;
  }

  /**
   * Metodo che mi restituisce la particella certificata presente su
   * DB_PARTICELLA_CERTIFICATA in relazione alla chiave logica(comune, sezione,
   * foglio, particella, subalterno) (Metodo utilizzato da SMRGAASV per servizio
   * serviceGetParticellaCertificata)
   * 
   * @param istatComune
   * @param sezione
   * @param foglio
   * @param particella
   * @param subalterno
   * @param onlyActive
   * @param dataDichiarazioneConsistenza
   * @return it.csi.solmr.dto.anag.ParticellaCertificataVO
   * @throws Exception
   */
  public ParticellaCertificataVO findParticellaCertificataByParameters(
      String istatComune, String sezione, String foglio, String particella,
      String subalterno, boolean onlyActive,
      java.util.Date dataDichiarazioneConsistenza) throws Exception
  {
    ParticellaCertificataVO particellaCertificataVO = null;
    try
    {
      particellaCertificataVO = particellaCertificataDAO
          .findParticellaCertificataByParameters(istatComune, sezione, foglio,
              particella, subalterno, onlyActive, dataDichiarazioneConsistenza);
    
      particellaCertificataVO.setVParticellaCertEleg(null);
      if (particellaCertificataVO.isCertificata()
          && particellaCertificataVO.isUnivoca())
      {
        PorzioneCertificataVO[] elencoPorzioniCertificate = porzioneCertificataDAO
            .getListPorzioneCertificataByIdParticellaCertificata(particellaCertificataVO
                .getIdParticellaCertificata(), dataDichiarazioneConsistenza);
        particellaCertificataVO
            .setElencoPorzioniCertificate(elencoPorzioniCertificate);
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return particellaCertificataVO;
  }
  
  
  /**
   * Metodo che mi restituisce la particella certificata presente su
   * DB_PARTICELLA_CERTIFICATA in relazione alla chiave logica(comune, sezione,
   * foglio, particella, subalterno) + la nuova eleggibilià fittizia
   * 
   * @param istatComune
   * @param sezione
   * @param foglio
   * @param particella
   * @param subalterno
   * @param onlyActive
   * @param dataDichiarazioneConsistenza
   * @return it.csi.solmr.dto.anag.ParticellaCertificataVO
   * @throws Exception
   */
  public ParticellaCertificataVO findParticellaCertificataByParametersNewElegFit(
      String istatComune, String sezione, String foglio, String particella,
      String subalterno, boolean onlyActive,
      java.util.Date dataDichiarazioneConsistenza) throws Exception
  {
    ParticellaCertificataVO particellaCertificataVO = null;
    try
    {
      particellaCertificataVO = particellaCertificataDAO
          .findParticellaCertificataByParameters(istatComune, sezione, foglio,
              particella, subalterno, onlyActive, dataDichiarazioneConsistenza);
      
      if (particellaCertificataVO.isCertificata()
          && particellaCertificataVO.isUnivoca())
      {
        //carico anche l'eleggibilità fittizia
        particellaCertificataVO.setVParticellaCertEleg(
            particellaCertificataDAO.getEleggibilitaByIdPartCertificata(
                particellaCertificataVO.getIdParticellaCertificata()));
        
        PorzioneCertificataVO[] elencoPorzioniCertificate = porzioneCertificataDAO
            .getListPorzioneCertificataByIdParticellaCertificata(particellaCertificataVO
                .getIdParticellaCertificata(), dataDichiarazioneConsistenza);
        particellaCertificataVO
            .setElencoPorzioniCertificate(elencoPorzioniCertificate);
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return particellaCertificataVO;
  }
  
  
  
  public ParticellaCertificataVO findParticellaCertificataAllaDichiarazione(
      Long idParticella, ConsistenzaVO consistenzaVO) throws Exception
  {
    ParticellaCertificataVO particellaCertificataVO = null;
    try
    {
      particellaCertificataVO = particellaCertificataDAO
          .findParticellaCertificataAllaDichiarazione(idParticella, consistenzaVO.getDataInserimentoDichiarazione());
      
      if (particellaCertificataVO.isCertificata() && particellaCertificataVO.isUnivoca())
      {
        //carico anche l'eleggibilità fittizia
        particellaCertificataVO.setVParticellaCertEleg(
            particellaCertificataDAO.getEleggibilitaAllaDichiarazione(
                idParticella, consistenzaVO.getCodiceFotografiaTerreni()));
        
        PorzioneCertificataVO[] elencoPorzioniCertificate = porzioneCertificataDAO
            .getListPorzioneCertificataByIdParticellaCertificata(particellaCertificataVO
                .getIdParticellaCertificata(), consistenzaVO.getDataInserimentoDichiarazione());
        particellaCertificataVO
            .setElencoPorzioniCertificate(elencoPorzioniCertificate);
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return particellaCertificataVO;
  }
  
  
  
  
  public ParticellaCertificataVO findParticellaCertificataByIdParticella(Long idParticella, 
    Date dataDichiarazioneConsistenza) throws Exception
  {
    try
    {
      return particellaCertificataDAO.findParticellaCertificataByIdParticella(idParticella, 
          dataDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }  
  }
  
  public Date getDataFotoInterpretazione(long idParticellaCertificata, Date dataRichiestaRiesame)
    throws Exception
  {
    try
    {
      return particellaCertificataDAO.getDataFotoInterpretazione(idParticellaCertificata, 
          dataRichiestaRiesame);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }  
  }
  
  
  public Vector<ParticellaCertElegVO> getEleggibilitaByIdParticella(long idParticella)
    throws Exception
  {
    try
    {
      return particellaCertificataDAO.getEleggibilitaByIdParticella(idParticella);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }  
  }
  
  public Vector<Vector<ParticellaCertElegVO>> getListStoricoParticellaCertEleg(long idParticella)
    throws Exception
  {
    try
    {
      return particellaCertificataDAO.getListStoricoParticellaCertEleg(idParticella);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }  
  }
  
  public HashMap<Long,Vector<SuperficieDescription>> getEleggibilitaTooltipByIdParticella(
      Vector<Long> listIdParticella)
  throws Exception
  {
    try
    {
      return particellaCertificataDAO.getEleggibilitaTooltipByIdParticella(listIdParticella);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }  
  }
  
  public Vector<ProprietaCertificataVO> getListProprietaCertifByIdParticella(long idParticella) 
    throws Exception
  {
    try
    {
      return particellaCertificataDAO.getListProprietaCertifByIdParticella(idParticella);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }  
  }
  
  public Vector<ProprietaCertificataVO> getListDettaglioProprietaCertifByIdParticella(long idParticella,
      Date dataInserimentoValidazione) 
    throws Exception
  {
    try
    {
      return particellaCertificataDAO.getListDettaglioProprietaCertifByIdParticella(idParticella, dataInserimentoValidazione);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }  
  }
  
  /**
   * Metodo che mi restituisce l'elenco delle varieta a partire dall'id_utilizzo
   * 
   * @param idUtilizzo
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.TipoVarietaVO
   * @throws Exception
   */
  public TipoVarietaVO[] getListTipoVarietaByIdUtilizzo(Long idUtilizzo,
      boolean onlyActive) throws Exception
  {
    try
    {
      return tipoVarietaDAO.getListTipoVarietaByIdUtilizzo(idUtilizzo,
          onlyActive);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che si occupa di reperire il dettaglio dei dati relativi alla
   * particella (DB_STORICO_PARTICELLA, DB_CONDUZIONE_PARTICELLA/DICHIARATA,
   * DB_UTILIZZO_PARTICELLA/DICHIARATA) a partire
   * dall'id_conduzione_particella/dichiarata e
   * dall'id_utilizzo_particella/dichiarato
   * 
   * @param filtriParticellareRicercaVO
   * @param idConduzione
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO
   * @throws Exception
   */
  public StoricoParticellaVO getDettaglioParticellaByIdConduzioneAndIdUtilizzo(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO,
      Long idConduzione, Long idUtilizzo, Long idAzienda) throws Exception
  {
    StoricoParticellaVO storicoParticellaVO = null;
    try
    {
      if (filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() <= 0)
      {
        storicoParticellaVO = storicoParticellaDAO
            .findStoricoParticellaVOByIdConduzioneParticella(idConduzione);
        
        // Cerco i dati del foglio associati allo storico particella
        FoglioVO foglioVO = foglioDAO.findFoglioByParameters(storicoParticellaVO
            .getIstatComune(), storicoParticellaVO.getFoglio(),
            storicoParticellaVO.getSezione());
        storicoParticellaVO.setFoglioVO(foglioVO);
        
        ConduzioneParticellaVO conduzioneParticellaVO = conduzioneParticellaDAO
            .findConduzioneParticellaByPrimaryKey(idConduzione);
        UtilizzoParticellaVO[] elencoUtilizzi = new UtilizzoParticellaVO[1];
        // Cerco le informazioni relative agli utilizzi solo se lavoro
        // su usi del suolo censiti e specificati non su quelli che
        // rappresentano il "senza uso del suolo specificato"
        if (idUtilizzo.intValue() != -1)
        {
          elencoUtilizzi[0] = utilizzoParticellaDAO
              .findUtilizzoParticellaByPrimaryKey(idUtilizzo);
          
          //aggiungo particelel EFa
          //elencoUtilizzi[0].setvParticellaEfa(utilizzoParticellaDAO
              //.getElencoParticelleEfaByUtilizzo(idUtilizzo.longValue()));
          
          
          if((storicoParticellaVO.getParticellaCertificataVO() != null)
              && (elencoUtilizzi[0] != null) 
              && (elencoUtilizzi[0].getIdVarieta() != null))
          {
            BigDecimal supEleggibile = conduzioneParticellaDAO.getSupEleggPlSql(
                idAzienda.longValue(), 
                storicoParticellaVO.getIdParticella().longValue(), 
                storicoParticellaVO.getParticellaCertificataVO().getIdParticellaCertificata().longValue(),
                idUtilizzo.longValue());       
            
            Vector<ParticellaCertElegVO> vPartCertEleg = new Vector<ParticellaCertElegVO>();
            ParticellaCertElegVO partCertElegVO = new ParticellaCertElegVO();
            partCertElegVO.setSuperficie(supEleggibile);
            vPartCertEleg.add(partCertElegVO);
            storicoParticellaVO.getParticellaCertificataVO().setVParticellaCertEleg(vPartCertEleg);
          }
          
          Vector<UtilizzoConsociatoVO> elencoConsociati = utilizzoConsociatoDAO
              .getListUtilizziConsociatiByIdUtilizzoParticella(
                  ((UtilizzoParticellaVO) elencoUtilizzi[0])
                      .getIdUtilizzoParticella(), null);
          if (elencoConsociati != null && elencoConsociati.size() > 0)
          {
            UtilizzoConsociatoVO[] elencoUtilizziConsociati = new UtilizzoConsociatoVO[elencoConsociati
                .size()];
            for (int a = 0; a < elencoConsociati.size(); a++)
            {
              elencoUtilizziConsociati[a] = (UtilizzoConsociatoVO) elencoConsociati
                  .elementAt(a);
            }
            ((UtilizzoParticellaVO) elencoUtilizzi[0])
                .setElencoUtilizziConsociati(elencoUtilizziConsociati);
          }
        }
        // Altrimenti gestisco l'utilizzo senza uso del suolo specificato
        // e calcolo la superficie disponibile
        else
        {
          elencoUtilizzi[0] = new UtilizzoParticellaVO();
          double totSupUtilizzata = utilizzoParticellaDAO
              .getTotSupUtilizzataByIdConduzioneParticella(idConduzione);
          
          
          double maxSupGrafCatNormalizzata = 0;
          if(Validator.isNotEmpty(foglioVO)
              && Validator.isNotEmpty(foglioVO.getFlagStabilizzazione())
              && (foglioVO.getFlagStabilizzazione().compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0)
              && Validator.validateDouble(storicoParticellaVO.getSuperficieGrafica(), SolmrConstants.FORMAT_SUP_UTILIZZATA) != null 
              && Double.parseDouble(storicoParticellaVO.getSuperficieGrafica().replace(',', '.')) > 0)
          {
            maxSupGrafCatNormalizzata = conduzioneParticellaDAO
                .getSupGrafNormalizIdConduzioneParticella(idConduzione);
          }
          
          if(maxSupGrafCatNormalizzata == 0)
          {
            maxSupGrafCatNormalizzata = conduzioneParticellaDAO
                .getMaxSupGrafCatIdConduzioneParticella(idConduzione);  
          }
          
          elencoUtilizzi[0].setIdUtilizzoParticella(new Long(-1));
          elencoUtilizzi[0].setIdConduzioneParticella(idConduzione);
          double supUtilizzata = maxSupGrafCatNormalizzata
              - totSupUtilizzata;
          elencoUtilizzi[0].setSuperficieUtilizzata(String
              .valueOf(supUtilizzata));
        }
        conduzioneParticellaVO.setElencoUtilizzi(elencoUtilizzi);
        ConduzioneParticellaVO[] elencoConduzioni = { conduzioneParticellaVO };
        storicoParticellaVO.setElencoConduzioni(elencoConduzioni);
      }
      else
      {
        storicoParticellaVO = storicoParticellaDAO
            .findStoricoParticellaVOByIdConduzioneDichiarata(idConduzione);
        // Cerco i dati del foglio associati allo storico particella
        FoglioVO foglioVO = foglioDAO.findFoglioByParameters(storicoParticellaVO
            .getIstatComune(), storicoParticellaVO.getFoglio(),
            storicoParticellaVO.getSezione());
        storicoParticellaVO.setFoglioVO(foglioVO);
        
        ConduzioneDichiarataVO conduzioneDichiarataVO = conduzioneDichiarataDAO
            .findConduzioneDichiarataByPrimaryKey(idConduzione);
        UtilizzoDichiaratoVO utilizzoDichiaratoVO = utilizzoDichiaratoDAO
            .findUtilizzoDichiaratoByPrimaryKey(idUtilizzo);
        UtilizzoConsociatoDichiaratoVO[] elencoUtilizzoConsociatiDichiarati = utilizzoDichiaratoDAO
            .getListUtilizziConsociatiDichiaratiByIdUtilizzoDichiarato(
                utilizzoDichiaratoVO.getIdUtilizzoDichiarato(), null);
        utilizzoDichiaratoVO
            .setElencoUtilizziConsociatiDich(elencoUtilizzoConsociatiDichiarati);
        UtilizzoDichiaratoVO[] elencoUtilizziDichiarati = { utilizzoDichiaratoVO };
        conduzioneDichiarataVO.setElencoUtilizzi(elencoUtilizziDichiarati);
        ConduzioneDichiarataVO[] elencoConduzioniDichiarate = { conduzioneDichiarataVO };
        storicoParticellaVO
            .setElencoConduzioniDichiarate(elencoConduzioniDichiarate);
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return storicoParticellaVO;
  }

  /**
   * Metodo che mi restituisce l'elenco di tutti gli uso del suolo in relazione
   * all'id_indirizzo_utilizzo
   * 
   * @param idIndirizzoUtilizzo
   * @param onlyActive
   * @param orderBy
   * @param colturaSecondaria
   * @return it.csi.solmr.dto.anag.terreni.TipoUtilizzoVO[]
   * @throws Exception
   */
  public TipoUtilizzoVO[] getListTipiUsoSuoloByIdIndirizzoUtilizzo(
      Long idIndirizzoUtilizzo, boolean onlyActive, String[] orderBy,
      String colturaSecondaria) throws Exception
  {
    try
    {
      return tipoUtilizzoDAO.getListTipiUsoSuoloByIdIndirizzoUtilizzo(
          idIndirizzoUtilizzo, onlyActive, orderBy, colturaSecondaria);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce l'elenco di tutti gli uso del suolo in relazione
   * al codice
   * 
   * @param codice
   * @param onlyActive
   * @param orderBy
   * @param colturaSecondaria
   * @param flagPrincipale
   * @return it.csi.solmr.dto.anag.terreni.TipoUtilizzoVO[]
   * @throws Exception
   */
  public TipoUtilizzoVO[] getListTipiUsoSuoloByCodice(String codice,
      boolean onlyActive, String[] orderBy, String colturaSecondaria,
      String flagPrincipale) throws Exception
  {
    try
    {
      return tipoUtilizzoDAO.getListTipiUsoSuoloByCodice(codice, onlyActive,
          orderBy, colturaSecondaria, flagPrincipale);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public TipoVarietaVO[] getListTipoVarietaByIdUtilizzoAndCodice(
      Long idUtilizzo, String codiceVarieta, boolean onlyActive, 
      String[] orderBy) throws Exception
  {
    try
    {
      return tipoVarietaDAO.getListTipoVarietaByIdUtilizzoAndCodice(
          idUtilizzo, codiceVarieta, onlyActive, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per effettuare l'associa uso a più particelle
   * 
   * @param elencoParticelle
   * @param ruoloUtenza
   * @throws Exception
   */
  public void associaUso(long idAzienda, StoricoParticellaVO[] elencoParticelle,
      RuoloUtenza ruoloUtenza) throws Exception
  {
    try
    {
      
      //Lock della tabella per impedire più accessi contemporanei!!
      long[] arrIdConduzioneParticella = new long[elencoParticelle.length]; 
      for (int i = 0; i < elencoParticelle.length; i++)
      {
        StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) elencoParticelle[i];
        ConduzioneParticellaVO conduzioneParticellaVO = storicoParticellaVO
            .getElencoConduzioni()[0];   
        arrIdConduzioneParticella[i] = conduzioneParticellaVO.getIdConduzioneParticella().longValue();        
      }
      conduzioneParticellaDAO.lockTableConduzioneParticella(arrIdConduzioneParticella);
      
      //**********************************
      
      HashMap<Long,Vector<String>> hIdUtilizzo = new HashMap<Long,Vector<String>>();
      for (int i = 0; i < elencoParticelle.length; i++)
      {
        StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) elencoParticelle[i];
        ConduzioneParticellaVO conduzioneParticellaVO = storicoParticellaVO.getElencoConduzioni()[0];
        
        //eseguo le operaziono solo se il titolo possesso non è di asservimento
        if(conduzioneParticellaVO.getIdTitoloPossesso().longValue() !=5)
        {
        
          if(hIdUtilizzo.get(conduzioneParticellaVO.getIdConduzioneParticella()) != null)
          {
            Vector<String> vIdUtilizzo = hIdUtilizzo.get(conduzioneParticellaVO.getIdConduzioneParticella());
            vIdUtilizzo.add(conduzioneParticellaVO.getElencoUtilizzi()[0].getIdUtilizzoParticella().toString());
            hIdUtilizzo.put(conduzioneParticellaVO.getIdConduzioneParticella(), vIdUtilizzo);
          }
          else
          {
            Vector<String> vIdUtilizzo = new Vector<String>(); 
            vIdUtilizzo.add(conduzioneParticellaVO.getElencoUtilizzi()[0].getIdUtilizzoParticella().toString());
            hIdUtilizzo.put(conduzioneParticellaVO.getIdConduzioneParticella(), vIdUtilizzo);
          }
        }
        
        
      }
      
      //Calcolo la superficieUtilizzata per valorizzare la superficieCondotta
      HashMap<Long,String> hIdConduzioneParticella = new HashMap<Long,String>();
      for (int i = 0; i < elencoParticelle.length; i++)
      {
        StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) elencoParticelle[i];
        ConduzioneParticellaVO conduzioneParticellaVO = storicoParticellaVO
            .getElencoConduzioni()[0];
        
        //eseguo le operaziono solo se il titolo possesso non è di asservimento
        if(conduzioneParticellaVO.getIdTitoloPossesso().longValue() !=5)
        {        
          if(hIdConduzioneParticella.get(conduzioneParticellaVO.getIdConduzioneParticella()) != null)
          {
            String supUtilizzata = hIdConduzioneParticella.get(conduzioneParticellaVO.getIdConduzioneParticella());
            double supUtilizataDbHash = Double.parseDouble(supUtilizzata.replace(',', '.'));
            double supUtilizataDb = Double.parseDouble(conduzioneParticellaVO.getElencoUtilizzi()[0].getSuperficieUtilizzata().replace(',', '.'));
            supUtilizataDbHash = supUtilizataDbHash + supUtilizataDb;
            supUtilizzata = StringUtils.parseSuperficieField(new Double(supUtilizataDbHash).toString());
            hIdConduzioneParticella.put(conduzioneParticellaVO.getIdConduzioneParticella(), supUtilizzata);
          }
          else
          { 
            String supUtilizzata = conduzioneParticellaVO.getElencoUtilizzi()[0].getSuperficieUtilizzata();
            double supUtilizataDbHash = Double.parseDouble(supUtilizzata.replace(',', '.'));
            //mi ricavo gli utilizzi non presenti a video
            double supUtilizataNoVideoDb = particellaGaaDAO.getSumUtilizziPrimariConConduzioneNoIndicati(
                conduzioneParticellaVO.getIdConduzioneParticella().longValue(), 
                hIdUtilizzo.get(conduzioneParticellaVO.getIdConduzioneParticella())).doubleValue();
            supUtilizataDbHash = supUtilizataDbHash + supUtilizataNoVideoDb;
            supUtilizzata = StringUtils.parseSuperficieField(new Double(supUtilizataDbHash).toString());
            hIdConduzioneParticella.put(conduzioneParticellaVO.getIdConduzioneParticella(),supUtilizzata);
          }
        }
      }      
      
      
      for (int i = 0; i < elencoParticelle.length; i++)
      {
        StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) elencoParticelle[i];
        ConduzioneParticellaVO conduzioneParticellaVO = (ConduzioneParticellaVO) storicoParticellaVO
            .getElencoConduzioni()[0];
        
        if(conduzioneParticellaVO.getIdTitoloPossesso().longValue() !=5)
        {
          UtilizzoParticellaVO utilizzoParticellaVO = (UtilizzoParticellaVO) conduzioneParticellaVO
              .getElencoUtilizzi()[0];
          utilizzoParticellaVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
          
          
          TipoEfaVO tipoEfaVO = particellaGaaDAO.getTipoEfaFromIdCatalogoMatrice(utilizzoParticellaVO.getIdCatalogoMatrice());
          if(Validator.isNotEmpty(tipoEfaVO))
          {
            utilizzoParticellaVO.setIdTipoEfa(new Long(tipoEfaVO.getIdTipoEfa()));
                       
            BigDecimal supUtilizzata = new BigDecimal(utilizzoParticellaVO.getSuperficieUtilizzata().replace(",", ".")); 
            BigDecimal valoreOriginale = supUtilizzata.multiply(new BigDecimal(10000));
            valoreOriginale = valoreOriginale.divide(tipoEfaVO.getFattoreDiConversione(),4,BigDecimal.ROUND_HALF_UP);
            utilizzoParticellaVO.setValoreOriginale(valoreOriginale);
            utilizzoParticellaVO.setValoreDopoConversione(supUtilizzata);
            BigDecimal valoreDopoPonderazione = supUtilizzata.multiply(tipoEfaVO.getFattoreDiPonderazione());
            utilizzoParticellaVO.setValoreDopoPonderazione(valoreDopoPonderazione);
          }
          else
          {
            utilizzoParticellaVO.setIdTipoEfa(null);
            utilizzoParticellaVO.setValoreOriginale(null);
            utilizzoParticellaVO.setValoreDopoConversione(null);
            utilizzoParticellaVO.setValoreDopoPonderazione(null);
          }
          
          // Se l'utilizzo aveva un uso del suolo specificato
          if (utilizzoParticellaVO.getIdUtilizzoParticella().intValue() != -1)
          {
            // Aggiorno DB_UTILIZZO_PARTICELLA
            utilizzoParticellaDAO.updateUtilizzoParticella(utilizzoParticellaVO);
          }
          // Altrimenti lo inserisco
          else
          {
            utilizzoParticellaVO.setAnno(DateUtils.getCurrentYear().toString());
            utilizzoParticellaDAO.insertUtilizzoParticella(utilizzoParticellaVO);
          }
          // Aggiorno DB_CONDUZIONE_PARTICELLA
          conduzioneParticellaVO.setRecordModificato(SolmrConstants.FLAG_S);
          conduzioneParticellaVO.setEsitoControllo(null);
          conduzioneParticellaVO.setDataEsecuzione(null);
          if(hIdConduzioneParticella.get(conduzioneParticellaVO.getIdConduzioneParticella()) != null)
          {
            String supCondotta = hIdConduzioneParticella.get(conduzioneParticellaVO.getIdConduzioneParticella());
            if(Double.parseDouble(supCondotta.replace(',', '.')) == 0)
            {
              supCondotta = AnagUtils.valSupCatGraf(storicoParticellaVO.getSupCatastale(), 
                  storicoParticellaVO.getSuperficieGrafica());
            }
            conduzioneParticellaVO.setSuperficieCondotta(
                hIdConduzioneParticella.get(conduzioneParticellaVO.getIdConduzioneParticella()));
          }
          else
          {
            conduzioneParticellaVO.setSuperficieCondotta(AnagUtils
                .valSupCatGraf(storicoParticellaVO.getSupCatastale(), 
                    storicoParticellaVO.getSuperficieGrafica()));
          }
          
          conduzioneParticellaDAO
              .updateConduzioneParticella(conduzioneParticellaVO);
        }
      }
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
  }
  
  
  public void associaUsoEleggibilitaGis(long idAzienda, StoricoParticellaVO[] elencoParticelle,
      RuoloUtenza ruoloUtenza) throws Exception
  {
    try
    {
      
      //Lock della tabella per impedire più accessi contemporanei!!
      long[] arrIdConduzioneParticella = new long[elencoParticelle.length]; 
      for (int i = 0; i < elencoParticelle.length; i++)
      {
        StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) elencoParticelle[i];
        ConduzioneParticellaVO conduzioneParticellaVO = storicoParticellaVO
            .getElencoConduzioni()[0];   
        arrIdConduzioneParticella[i] = conduzioneParticellaVO.getIdConduzioneParticella().longValue();        
      }
      conduzioneParticellaDAO.lockTableConduzioneParticella(arrIdConduzioneParticella);      
      //**********************************     
     
      
      HashMap<Long,Vector<String>> hIdUtilizzo = new HashMap<Long,Vector<String>>();
      for (int i = 0; i < elencoParticelle.length; i++)
      {
        StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) elencoParticelle[i];
        ConduzioneParticellaVO conduzioneParticellaVO = storicoParticellaVO.getElencoConduzioni()[0];
        
        //eseguo le operaziono solo se il titolo possesso non è di asservimento
        if(conduzioneParticellaVO.getIdTitoloPossesso().longValue() !=5)
        {
          if(hIdUtilizzo.get(conduzioneParticellaVO.getIdConduzioneParticella()) != null)
          {
            Vector<String> vIdUtilizzo = hIdUtilizzo.get(conduzioneParticellaVO.getIdConduzioneParticella());
            vIdUtilizzo.add(conduzioneParticellaVO.getElencoUtilizzi()[0].getIdUtilizzoParticella().toString());
            hIdUtilizzo.put(conduzioneParticellaVO.getIdConduzioneParticella(), vIdUtilizzo);
          }
          else
          {
            Vector<String> vIdUtilizzo = new Vector<String>(); 
            vIdUtilizzo.add(conduzioneParticellaVO.getElencoUtilizzi()[0].getIdUtilizzoParticella().toString());
            hIdUtilizzo.put(conduzioneParticellaVO.getIdConduzioneParticella(), vIdUtilizzo);
          }
        }
        
        
      }
      
      //Calcolo la superficieUtilizzata per valorizzare la superficieCondotta
      HashMap<Long,String> hIdConduzioneParticella = new HashMap<Long,String>();
      for (int i = 0; i < elencoParticelle.length; i++)
      {
        StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) elencoParticelle[i];
        ConduzioneParticellaVO conduzioneParticellaVO = storicoParticellaVO
            .getElencoConduzioni()[0];
        
        //eseguo le operazioni solo se il titolo possesso non è di asservimento
        if(conduzioneParticellaVO.getIdTitoloPossesso().longValue() !=5)
        {
          if(hIdConduzioneParticella.get(conduzioneParticellaVO.getIdConduzioneParticella()) != null)
          {
            String supUtilizzata = hIdConduzioneParticella.get(conduzioneParticellaVO.getIdConduzioneParticella());
            double supUtilizataDbHash = Double.parseDouble(supUtilizzata.replace(',', '.'));
            double supUtilizataDb = Double.parseDouble(conduzioneParticellaVO.getElencoUtilizzi()[0].getSuperficieUtilizzata().replace(',', '.'));
            supUtilizataDbHash = supUtilizataDbHash + supUtilizataDb;
            supUtilizzata = StringUtils.parseSuperficieField(new Double(supUtilizataDbHash).toString());
            hIdConduzioneParticella.put(conduzioneParticellaVO.getIdConduzioneParticella(), supUtilizzata);
          }
          else
          { 
            String supUtilizzata = conduzioneParticellaVO.getElencoUtilizzi()[0].getSuperficieUtilizzata();
            double supUtilizataDbHash = Double.parseDouble(supUtilizzata.replace(',', '.'));
            //mi ricavo gli utilizzi non presenti a video
            double supUtilizataNoVideoDb = particellaGaaDAO.getSumUtilizziPrimariConConduzioneNoIndicati(
                conduzioneParticellaVO.getIdConduzioneParticella().longValue(), 
                hIdUtilizzo.get(conduzioneParticellaVO.getIdConduzioneParticella())).doubleValue();
            supUtilizataDbHash = supUtilizataDbHash + supUtilizataNoVideoDb;
            supUtilizzata = StringUtils.parseSuperficieField(new Double(supUtilizataDbHash).toString());
            hIdConduzioneParticella.put(conduzioneParticellaVO.getIdConduzioneParticella(),supUtilizzata);
          }
        }
      }      
      
      
      
      for (int i = 0; i < elencoParticelle.length; i++)
      {
        StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) elencoParticelle[i];
        ConduzioneParticellaVO conduzioneParticellaVO = (ConduzioneParticellaVO) storicoParticellaVO
            .getElencoConduzioni()[0];
        
        
        //eseguo le operaziono solo se il titolo possesso non è di asservimento
        if(conduzioneParticellaVO.getIdTitoloPossesso().longValue() !=5)
        {        
          UtilizzoParticellaVO utilizzoParticellaVO = (UtilizzoParticellaVO) conduzioneParticellaVO
              .getElencoUtilizzi()[0];
          if (utilizzoParticellaVO.getIdUtilizzoParticella().intValue() != -1)
          {
            
            if(Validator.isNotEmpty(utilizzoParticellaVO.getIdTipoEfa()))
            {
              TipoEfaVO tipoEfaVO = particellaGaaDAO.getTipoEfaFromPrimaryKey(utilizzoParticellaVO.getIdTipoEfa());
            
              BigDecimal supUtilizzata = new BigDecimal(utilizzoParticellaVO.getSuperficieUtilizzata().replace(",", ".")); 
              BigDecimal valoreOriginale = supUtilizzata.multiply(new BigDecimal(10000));
              valoreOriginale = valoreOriginale.divide(tipoEfaVO.getFattoreDiConversione(),4,BigDecimal.ROUND_HALF_UP);
              utilizzoParticellaVO.setValoreOriginale(valoreOriginale);
              utilizzoParticellaVO.setValoreDopoConversione(supUtilizzata);
              BigDecimal valoreDopoPonderazione = supUtilizzata.multiply(tipoEfaVO.getFattoreDiPonderazione());
              utilizzoParticellaVO.setValoreDopoPonderazione(valoreDopoPonderazione);
            }
            else
            {
              utilizzoParticellaVO.setIdTipoEfa(null);
              utilizzoParticellaVO.setValoreOriginale(null);
              utilizzoParticellaVO.setValoreDopoConversione(null);
              utilizzoParticellaVO.setValoreDopoPonderazione(null);
            }
            
            
            utilizzoParticellaVO
              .setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
          
            // Aggiorno DB_UTILIZZO_PARTICELLA
            utilizzoParticellaDAO.updateUtilizzoParticella(utilizzoParticellaVO);
            
            // Aggiorno DB_CONDUZIONE_PARTICELLA
            conduzioneParticellaVO.setRecordModificato(SolmrConstants.FLAG_S);
            conduzioneParticellaVO.setEsitoControllo(null);
            conduzioneParticellaVO.setDataEsecuzione(null);
            if(hIdConduzioneParticella.get(conduzioneParticellaVO.getIdConduzioneParticella()) != null)
            {
              //String supCondotta = hIdConduzioneParticella.get(conduzioneParticellaVO.getIdConduzioneParticella());
              /*if(Double.parseDouble(supCondotta.replace(',', '.')) == 0)
              {
                supCondotta = AnagUtils.valSupCatGraf(storicoParticellaVO.getSupCatastale(), 
                    storicoParticellaVO.getSuperficieGrafica());
              }*/
              conduzioneParticellaVO.setSuperficieCondotta(
                  hIdConduzioneParticella.get(conduzioneParticellaVO.getIdConduzioneParticella()));
            }
            else
            {
              conduzioneParticellaVO.setSuperficieCondotta(AnagUtils
                  .valSupCatGraf(storicoParticellaVO.getSupCatastale(), 
                      storicoParticellaVO.getSuperficieGrafica()));
            }
            
            
            conduzioneParticellaDAO
              .updateConduzioneParticella(conduzioneParticellaVO);
          }
        }
      }
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
  }
  

  /**
   * Metodo che si occupa di effettuare il cambio titolo di possesso delle
   * particelle selezionate
   * 
   * @param elencoIdConduzioneParticella
   * @param ruoloUtenza
   * @param idAzienda
   * @param idTitoloPossesso
   * @throws Exception
   */
  public void cambiaTitoloPossesso(Long[] elencoIdConduzioneParticella,
      RuoloUtenza ruoloUtenza, Long idAzienda, Long idTitoloPossesso)
      throws Exception
  {
    try
    {
      
      //Lock della tabella per impedire più accessi contemporanei!!
      /*long[] arrIdConduzioneParticella = new long[elencoIdConduzioneParticella.length]; 
      for (int i = 0; i < elencoIdConduzioneParticella.length; i++)
      { 
        arrIdConduzioneParticella[i] = elencoIdConduzioneParticella[i].longValue();        
      }*/
      //conduzioneParticellaDAO.lockTableConduzioneParticella(arrIdConduzioneParticella);      
      //**********************************
      
      Vector<Long> vIdConduzioneParticella = new Vector<Long>();
      for (int i = 0; i < elencoIdConduzioneParticella.length; i++)
      {
        if(!vIdConduzioneParticella.contains(elencoIdConduzioneParticella[i]))
        {
          vIdConduzioneParticella.add(elencoIdConduzioneParticella[i]);
        }
      }
      
      elencoIdConduzioneParticella = (Long[])vIdConduzioneParticella.toArray(new Long[vIdConduzioneParticella.size()]);
      
      for (int i = 0; i < elencoIdConduzioneParticella.length; i++)
      {
        Long idConduzioneParticella = (Long) elencoIdConduzioneParticella[i];
        // Recupero i dati della conduzione particella
        ConduzioneParticellaVO oldConduzioneParticella = conduzioneParticellaDAO
            .findConduzioneParticellaByPrimaryKey(idConduzioneParticella);
        if (Validator.isNotEmpty(oldConduzioneParticella
            .getSuperficieAgronomica()))
        {
          oldConduzioneParticella.setSuperficieAgronomica(StringUtils
              .parseSuperficieField(oldConduzioneParticella
                  .getSuperficieAgronomica()));
        }
        // Se il nuovo id titolo possesso è diverso dal vecchio
        if (oldConduzioneParticella.getIdTitoloPossesso().compareTo(
            idTitoloPossesso) != 0)
        {
          
          String supCondotta = null;
          
          StoricoParticellaVO strVO = storicoParticellaDAO
            .findStoricoParticellaVOByIdConduzioneParticella(idConduzioneParticella);
          
          if(idTitoloPossesso.longValue() == 5)
          {            
            supCondotta = strVO.getSupCatastale();
            if(Double.parseDouble(supCondotta.replace(',', '.')) == 0)
            {
              supCondotta = strVO.getSuperficieGrafica();
            }
            
            if(Double.parseDouble(supCondotta.replace(',', '.')) > 0)
            {
              double supCondottaDb = Double.parseDouble(supCondotta.replace(',', '.'));
              double percentualePossessoDb = Double.parseDouble(strVO.getElencoConduzioni()[0]
                .getPercentualePossesso().toString());              
              supCondottaDb = supCondottaDb * percentualePossessoDb / 100.0; 
              
              supCondotta = StringUtils.parseSuperficieField(new Double(NumberUtils.arrotonda(supCondottaDb,4)).toString());
            
            }
            
            
          }
          else
          {
            double totUtilizata = particellaGaaDAO.getSumUtilizziPrimariConConduzioneNoIndicati(
                idConduzioneParticella.longValue(),null).doubleValue();
            supCondotta = StringUtils.parseSuperficieField(new Double(totUtilizata).toString());
          }
          
          if(Double.parseDouble(supCondotta.replace(',', '.')) == 0)
          {
            supCondotta = AnagUtils.valSupCatGraf(strVO.getSupCatastale(), strVO.getSuperficieGrafica());
          }
          
          
          
          // Cerco se esiste almeno una dichiarazione di consistenza
          ConduzioneDichiarataVO[] elencoConduzioniDichiarate = conduzioneDichiarataDAO
              .getListConduzioneDichiarataByIdConduzioneParticellaAndIdAzienda(
                  idConduzioneParticella, idAzienda, false, null);
          // Se esiste almeno una dichiarazione di consistenza
          if (elencoConduzioniDichiarate != null
              && elencoConduzioniDichiarate.length > 0)
          {
            // Cesso il record su DB_CONDUZIONE_PARTICELLA
            oldConduzioneParticella.setDataFineConduzione(new Date());
            oldConduzioneParticella.setDataAggiornamento(new Date());
            conduzioneParticellaDAO
                .updateConduzioneParticella(oldConduzioneParticella);
            // Creo il nuovo oggetto da inserire
            ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
            // Setto i parametri
            conduzioneParticellaVO.setIdParticella(oldConduzioneParticella
                .getIdParticella());
            conduzioneParticellaVO.setIdUte(oldConduzioneParticella.getIdUte());
            conduzioneParticellaVO.setIdTitoloPossesso(idTitoloPossesso);
            //conduzioneParticellaVO.setSuperficieCondotta(oldConduzioneParticella.getSuperficieCondotta());
            conduzioneParticellaVO
              .setSuperficieCondotta(supCondotta);
            conduzioneParticellaVO
                .setPercentualePossesso(oldConduzioneParticella
                .getPercentualePossesso());
            conduzioneParticellaVO.setFlagUtilizzoParte(null);
            conduzioneParticellaVO.setNote(null);
            conduzioneParticellaVO.setDataInizioConduzione(new Date());
            conduzioneParticellaVO.setDataFineConduzione(null);
            conduzioneParticellaVO.setDataAggiornamento(new Date());
            conduzioneParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
                .getIdUtente());
            conduzioneParticellaVO.setRecordModificato(SolmrConstants.FLAG_S);
            conduzioneParticellaVO.setEsitoControllo(null);
            conduzioneParticellaVO.setDataEsecuzione(null);
            if (Validator.isNotEmpty(oldConduzioneParticella
                .getSuperficieAgronomica()))
            {
              conduzioneParticellaVO.setSuperficieAgronomica(StringUtils
                  .parseSuperficieField(oldConduzioneParticella
                      .getSuperficieAgronomica()));
            }
            // Inserisco il nuovo record
            Long newIdConduzioneParticella = conduzioneParticellaDAO
                .insertConduzioneParticella(conduzioneParticellaVO);
            // Recupero i documenti attivi legati alla conduzione storicizzata
            Vector<DocumentoVO> elencoDocumenti = documentoDAO
                .getListDocumentiByIdConduzioneParticella(
                    idConduzioneParticella, true);
            // Se ce ne sono
            if (elencoDocumenti != null && elencoDocumenti.size() > 0)
            {
              Vector<DocumentoConduzioneVO> elencoConduzioni = null;
              for (int c = 0; c < elencoDocumenti.size(); c++)
              {
                DocumentoVO documentoVO = (DocumentoVO) elencoDocumenti
                    .elementAt(c);                
                // Ricerco le conduzioni attive legate al precedente documento
                elencoConduzioni = documentoDAO
                    .getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella(
                        documentoVO.getIdDocumento(), oldConduzioneParticella
                            .getIdConduzioneParticella(), true);
                // Se ne trovo
                if (elencoConduzioni != null && elencoConduzioni.size() > 0)
                {
                  for (int d = 0; d < elencoConduzioni.size(); d++)
                  {
                    DocumentoConduzioneVO documentoConduzioneVO = (DocumentoConduzioneVO) elencoConduzioni
                        .elementAt(d);
                    // Storicizzo i records presenti su DB_DOCUMENTO_CONDUZIONE
                    documentoConduzioneVO
                        .setDataFineValidita(new java.util.Date(new Timestamp(
                            System.currentTimeMillis()).getTime()));
                    documentoDAO
                        .updateDocumentoConduzione(documentoConduzioneVO);
                    //Fatto solo se istanza riesame fase 1 e titolo possesso diverso asservimento
                    // o di fasi sucessive
                    if(documentoDAO.isDocIstanzaRiesame(documentoVO.getExtIdDocumento()))
                    {
                      if(((documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_FOTO)
                        && (idTitoloPossesso.compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) != 0))
                        || documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_CONTRO
                        || documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_SOPRA)
                      {
                        documentoConduzioneVO.setIdDocumentoConduzione(null);
                        documentoConduzioneVO
                            .setIdConduzioneParticella(newIdConduzioneParticella);
                        documentoConduzioneVO
                            .setDataInserimento(new java.util.Date(new Timestamp(
                                System.currentTimeMillis()).getTime()));
                        documentoConduzioneVO
                            .setDataInizioValidita(new java.util.Date(
                                new Timestamp(System.currentTimeMillis())
                                    .getTime()));
                        documentoConduzioneVO.setDataFineValidita(null);
                        documentoDAO
                            .insertDocumentoConduzione(documentoConduzioneVO);
                      }
                    }
                  }
                }
                
                //Fatto solo se istanza riesame fase 1
                if(idTitoloPossesso.compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) == 0)
                {                  
                  if(documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_FOTO)
                  {                   
                    if (elencoConduzioni != null && elencoConduzioni.size() > 0)
                    {
                      if(!documentoGaaDAO.existAltroLegameIstRiesameParticella(
                         idAzienda, oldConduzioneParticella.getIdParticella().longValue(), 
                         DateUtils.getCurrentYear().intValue(), 
                         documentoVO.getExtIdDocumento().longValue()))
                      {                 
                        
                        Long idIstanaRiesame = documentoGaaDAO.getIstRiesameParticellaFaseAnno(
                            idAzienda, oldConduzioneParticella.getIdParticella().longValue(), 
                            DateUtils.getCurrentYear().intValue(), documentoVO.getFaseIstanzaRiesame());
                        if(Validator.isNotEmpty(idIstanaRiesame))
                        {
                          Vector<Long> vIdIstanzaRiesame = new Vector<Long>();
                          vIdIstanzaRiesame.add(idIstanaRiesame);
                          documentoGaaDAO.annullaIstanzaFromId(vIdIstanzaRiesame, ruoloUtenza.getIdUtente());
                          PlSqlCodeDescription plCode = particellaGaaDAO.annullaIstanzaPlSql(documentoVO.getIdAzienda().longValue(), 
                              DateUtils.extractYearFromDate(documentoVO.getDataInserimento()), documentoVO.getFaseIstanzaRiesame(), 
                              ruoloUtenza.getIdUtente().longValue());
                          
                          if(plCode !=null)
                          {
                            if(Validator.isNotEmpty(plCode.getDescription()))
                            {
                              throw new DataAccessException("Aggiorna documento errore al plsql annullaIstanzaRiesame: "+plCode.getDescription()+"-"
                                  +plCode.getOtherdescription());       
                            }
                          }
                          
                          
                        }
                          
                      }                
                      
                    }
                    
                    
                    //cessazione del documento
                    Vector<DocumentoConduzioneVO> vDocumentoConduzione = documentoDAO
                        .getListDocumentoConduzioni(documentoVO.getIdDocumento(), false);
                    Vector<Long> vIdParticella = null;
                    boolean trovataUnaValida = false;
                    for(int a=0;a<vDocumentoConduzione.size();a++)
                    {
                      if(Validator.isEmpty(vDocumentoConduzione.get(a).getDataFineValidita()))
                      {
                        if(!trovataUnaValida)
                          trovataUnaValida = true;                 
                      }
                      else
                      {
                        if(vIdParticella == null)
                        {
                          vIdParticella = new Vector<Long>();
                        }
                        if(!vIdParticella.contains(vDocumentoConduzione.get(a).getIdParticella()))
                        {
                          vIdParticella.add(vDocumentoConduzione.get(a).getIdParticella());
                        }
                      }                
                    }
                    //non ho trovato conduzioni attive sul documento
                    if(!trovataUnaValida)
                    {
                      Long idStatoDocumento = null;
                      if(documentoGaaDAO.isAllIstanzaAnnullata(idAzienda, vIdParticella, 
                          DateUtils.getCurrentYear().intValue(), documentoVO.getFaseIstanzaRiesame()))
                      {
                        idStatoDocumento = new Long(1);
                      }               
                      documentoDAO.updateDocumentoIstanza(documentoVO.getIdDocumento(), idStatoDocumento);
                      
                    }
                    
                  }
                  
                  
                  
                }
                
                
                
              } //elenco documenti
              
            }
            // Recupero gli utilizzi legati alla vecchia conduzione
            UtilizzoParticellaVO[] elencoUtilizzi = getListUtilizzoParticellaVOByIdConduzioneParticella(
                idConduzioneParticella, null, false);
            // Se ne trovo
            if (elencoUtilizzi != null && elencoUtilizzi.length > 0)
            {
              // Li duplico associandoli alla nuova condizione appena inserita
              for (int b = 0; b < elencoUtilizzi.length; b++)
              {
                UtilizzoParticellaVO oldUtilizzoParticellaVO = (UtilizzoParticellaVO) elencoUtilizzi[b];
                
                oldUtilizzoParticellaVO.setIdConduzioneParticella(newIdConduzioneParticella);
                oldUtilizzoParticellaVO.setDataAggiornamento(new java.util.Date(
                    new Timestamp(System.currentTimeMillis()).getTime()));
                oldUtilizzoParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
                    .getIdUtente());
                oldUtilizzoParticellaVO.setAnno(DateUtils.getCurrentYear()
                    .toString());
                oldUtilizzoParticellaVO.setNote(null);
                // Inserisco il nuovo utilizzo
                utilizzoParticellaDAO
                    .insertUtilizzoParticella(oldUtilizzoParticellaVO);
              }
            }
          }
          // Se invece non risultano dichiarazioni di consistenza
          else
          {
            // Effettuo semplicemente un'update su DB_CONDUZIONE_PARTICELLA
            oldConduzioneParticella.setIdTitoloPossesso(idTitoloPossesso);
            oldConduzioneParticella.setSuperficieCondotta(supCondotta);
            oldConduzioneParticella.setDataAggiornamento(new java.util.Date(
                new Timestamp(System.currentTimeMillis()).getTime()));
            oldConduzioneParticella.setIdUtenteAggiornamento(ruoloUtenza
                .getIdUtente());
            oldConduzioneParticella.setRecordModificato(SolmrConstants.FLAG_S);
            oldConduzioneParticella.setEsitoControllo(null);
            oldConduzioneParticella.setDataEsecuzione(null);
            conduzioneParticellaDAO
                .updateConduzioneParticella(oldConduzioneParticella);
            // Recupero i documenti attivi legati alla conduzione modificata
            Vector<DocumentoVO> elencoDocumenti = documentoDAO
                .getListDocumentiByIdConduzioneParticella(
                    oldConduzioneParticella.getIdConduzioneParticella(), true);
            // Se ce ne sono ...
            if (elencoDocumenti != null && elencoDocumenti.size() > 0)
            {
              Vector<DocumentoConduzioneVO> elencoConduzioni = null;
              for (int c = 0; c < elencoDocumenti.size(); c++)
              {
                DocumentoVO documentoVO = (DocumentoVO) elencoDocumenti
                    .elementAt(c);
                // Ricerco le conduzioni attive legate al precedente documento
                elencoConduzioni = documentoDAO
                    .getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella(
                        documentoVO.getIdDocumento(), oldConduzioneParticella
                            .getIdConduzioneParticella(), true);
                
                
                for (int d = 0; d < elencoConduzioni.size(); d++)
                {
                  DocumentoConduzioneVO documentoConduzioneVO = (DocumentoConduzioneVO) elencoConduzioni
                      .elementAt(d);
                  // Storicizzo i records presenti su DB_DOCUMENTO_CONDUZIONE
                  documentoConduzioneVO
                      .setDataFineValidita(new java.util.Date(new Timestamp(
                          System.currentTimeMillis()).getTime()));
                  documentoDAO
                      .updateDocumentoConduzione(documentoConduzioneVO);
                  //Fatto solo se istanza riesame fase 1 e titolo possesso diverso asservimento
                  // o di fasi sucessive
                  if(documentoDAO.isDocIstanzaRiesame(documentoVO.getExtIdDocumento()))
                  {
                    if(((documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_FOTO)
                      && (idTitoloPossesso.compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) != 0))
                      || documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_CONTRO
                      || documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_SOPRA)
                    {
                      documentoConduzioneVO.setIdDocumentoConduzione(null);
                      documentoConduzioneVO
                          .setIdConduzioneParticella(oldConduzioneParticella
                              .getIdConduzioneParticella());
                      documentoConduzioneVO
                          .setDataInserimento(new java.util.Date(new Timestamp(
                              System.currentTimeMillis()).getTime()));
                      documentoConduzioneVO
                          .setDataInizioValidita(new java.util.Date(
                              new Timestamp(System.currentTimeMillis())
                                  .getTime()));
                      documentoConduzioneVO.setDataFineValidita(null);
                      documentoDAO
                          .insertDocumentoConduzione(documentoConduzioneVO);
                    }
                  }
                }
              }
            }
          }
        }
      }
      
     
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce la data più recente relativa ad un'elenco di
   * conduzioni selezionate
   * 
   * @param idConduzioneParticella
   * @return java.util.Date
   * @throws Exception
   */
  public java.util.Date getMaxDataInizioConduzioneParticella(
      Vector<Long> elencoConduzioni) throws Exception
  {
    try
    {
      return conduzioneParticellaDAO
          .getMaxDataInizioConduzioneParticella(elencoConduzioni);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo utilizzato per effettuare la cessazione delle particelle
   * 
   * @param elencoIdConduzioneParticella
   * @param ruoloUtenza
   * @param idAzienda
   * @param dataCessazione
   * @param note
   * @throws Exception
   */
  public void cessaParticelle(Long[] elencoIdConduzioneParticella,
      long idUtenteAggiornamento, Long idAzienda, java.util.Date dataCessazione, String provenienza)
      throws Exception
  {
    try
    {
      
      //Lock della tabella per impedire più accessi contemporanei!!
      long[] arrIdConduzioneParticella = new long[elencoIdConduzioneParticella.length]; 
      for (int i = 0; i < elencoIdConduzioneParticella.length; i++)
      { 
        arrIdConduzioneParticella[i] = elencoIdConduzioneParticella[i].longValue();        
      }
      //Ci sono problemi se si cessa iun azienda con più di mille particelle
      //la in supporta solo mille elementi
      long[] idAziendaArr = { idAzienda.longValue() };
      if(elencoIdConduzioneParticella.length > 999)
      {
        anagrafeDAO.lockTableAnagraficaAzienda(idAziendaArr);
      }
      else
      {
        conduzioneParticellaDAO.lockTableConduzioneParticella(arrIdConduzioneParticella);
      }
      //**********************************
      
      for (int i = 0; i < elencoIdConduzioneParticella.length; i++)
      {
        Long idConduzioneParticella = (Long) elencoIdConduzioneParticella[i];
        // Recupero i dati della conduzione particella
        ConduzioneParticellaVO oldConduzioneParticella = conduzioneParticellaDAO
            .findConduzioneParticellaByPrimaryKey(idConduzioneParticella);
        if (Validator.isNotEmpty(oldConduzioneParticella
            .getSuperficieAgronomica()))
        {
          oldConduzioneParticella.setSuperficieAgronomica(StringUtils
              .parseSuperficieField(oldConduzioneParticella
                  .getSuperficieAgronomica()));
        }
        // Cesso il record su DB_CONDUZIONE_PARTICELLA
        oldConduzioneParticella.setDataFineConduzione(dataCessazione);
        oldConduzioneParticella.setDataAggiornamento(new java.util.Date(
            new Timestamp(System.currentTimeMillis()).getTime()));
        oldConduzioneParticella.setIdUtenteAggiornamento(idUtenteAggiornamento);
        oldConduzioneParticella.setRecordModificato(SolmrConstants.FLAG_S);
        oldConduzioneParticella.setEsitoControllo(null);
        oldConduzioneParticella.setDataEsecuzione(null);
        conduzioneParticellaDAO
            .updateConduzioneParticella(oldConduzioneParticella);
        // Verifico se ci sono altre conduzioni attive associate all'azienda
        ConduzioneParticellaVO[] elencoConduzioniAttive = conduzioneParticellaDAO
            .getListConduzioneParticellaByIdAziendaAndIdParticella(idAzienda,
                oldConduzioneParticella.getIdParticella(), true, null);
        // Se non ci sono
        if (elencoConduzioniAttive == null
            || elencoConduzioniAttive.length == 0)
        {
          // Recupero le unità arboree associate alla particella e all'azienda
          // selezionata
          StoricoUnitaArboreaVO[] elencoUnitaArboree = storicoUnitaArboreaDAO
              .getListStoricoUnitaArboreaByLogicKey(oldConduzioneParticella
                  .getIdParticella(), new Long(-1), idAzienda, null);
          // Se ce ne sono
          if (elencoUnitaArboree != null && elencoUnitaArboree.length > 0)
          {
            // Le storicizzo
            for (int a = 0; a < elencoUnitaArboree.length; a++)
            {
              StoricoUnitaArboreaVO storicoUnitaArboreaVO = (StoricoUnitaArboreaVO) elencoUnitaArboree[a];
              // Imposto data fine validita
              //storicoUnitaArboreaVO.setDataFineValidita(new java.util.Date(
                  //new Timestamp(System.currentTimeMillis()).getTime()));
              //storicoUnitaArboreaVO.setDataAggiornamento(new java.util.Date(
                  //new Timestamp(System.currentTimeMillis()).getTime()));
              storicoUnitaArboreaDAO.storicizzaStoricoUnitaArborea(storicoUnitaArboreaVO.getIdStoricoUnitaArborea());
                  //.updateStoricoUnitaArborea(storicoUnitaArboreaVO);
              // Inserisco nuovo record su DB_STORICO_UNITA_ARBOREA
              storicoUnitaArboreaVO.setDataFineValidita(null);
              storicoUnitaArboreaVO.setIdStoricoUnitaArborea(null);
              storicoUnitaArboreaVO.setDataInizioValidita(new java.util.Date(
                  new Timestamp(System.currentTimeMillis()).getTime()));
              storicoUnitaArboreaVO.setDataAggiornamento(new java.util.Date(
                  new Timestamp(System.currentTimeMillis()).getTime()));
              storicoUnitaArboreaVO.setIdUtenteAggiornamento(idUtenteAggiornamento);
              storicoUnitaArboreaVO.setRecordModificato(null);
              storicoUnitaArboreaVO.setDataEsecuzione(null);
              storicoUnitaArboreaVO.setEsitoControllo(null);
              storicoUnitaArboreaVO.setIdAzienda(null);
              storicoUnitaArboreaVO.setIdGenereIscrizione(null);
              storicoUnitaArboreaDAO
                  .insertStoricoUnitaArborea(storicoUnitaArboreaVO);
            }
          }
        }
        // Controllo se ci sono dei fabbricati attivi relativi alla particella
        // e all'azienda selezionati
        FabbricatoParticellaVO[] elencoFabbricati = fabbricatoDAO
            .getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella(
                idConduzioneParticella, idAzienda, null, true);
        if (elencoFabbricati != null && elencoFabbricati.length > 0)
        {
          // Se li ho trovati li cesso
          for (int b = 0; b < elencoFabbricati.length; b++)
          {
            FabbricatoParticellaVO fabbricatoParticellaVO = (FabbricatoParticellaVO) elencoFabbricati[b];
            fabbricatoParticellaVO.setDataFineValidita(new java.util.Date(
                new Timestamp(System.currentTimeMillis()).getTime()));
            fabbricatoDAO.updateFabbricatoParticella(fabbricatoParticellaVO);
          }
        }
        // Recupero i documenti attivi legati alla conduzione storicizzata
        Vector<DocumentoVO> elencoDocumenti = documentoDAO
            .getListDocumentiByIdConduzioneParticella(idConduzioneParticella,
                true);
        // Se ce ne sono
        if (elencoDocumenti != null && elencoDocumenti.size() > 0)
        {
          Vector<DocumentoConduzioneVO> elencoConduzioni = null;
          for (int c = 0; c < elencoDocumenti.size(); c++)
          {
            DocumentoVO documentoVO = (DocumentoVO) elencoDocumenti
                .elementAt(c);
            // Ricerco le conduzioni attive legate al precedente documento
            elencoConduzioni = documentoDAO
                .getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella(
                    documentoVO.getIdDocumento(), oldConduzioneParticella
                        .getIdConduzioneParticella(), true);
            if(SolmrConstants.MODIFICA_MULTIPLA.equalsIgnoreCase(provenienza)
                && (documentoVO.getFaseIstanzaRiesame() != SolmrConstants.FASE_IST_RIESAM_CONTRO))
            {
              // Se ne trovo
              if (elencoConduzioni != null && elencoConduzioni.size() > 0)
              {
                for (int d = 0; d < elencoConduzioni.size(); d++)
                {
                  DocumentoConduzioneVO documentoConduzioneVO = (DocumentoConduzioneVO) elencoConduzioni
                      .elementAt(d);
                  
                  // Storicizzo i records presenti su DB_DOCUMENTO_CONDUZIONE
                  documentoConduzioneVO.setDataFineValidita(new java.util.Date(
                      new Timestamp(System.currentTimeMillis()).getTime()));
                  documentoDAO.updateDocumentoConduzione(documentoConduzioneVO);
                }
              }
            }
            
            if(SolmrConstants.MODIFICA_MULTIPLA.equalsIgnoreCase(provenienza)
              && (documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_FOTO))
            {
              
              if (elencoConduzioni != null && elencoConduzioni.size() > 0)
              {
                if(!documentoGaaDAO.existAltroLegameIstRiesameParticella(
                   idAzienda, oldConduzioneParticella.getIdParticella().longValue(), 
                   DateUtils.getCurrentYear().intValue(), 
                   documentoVO.getExtIdDocumento().longValue()))
                {                 
                  
                  Long idIstanaRiesame = documentoGaaDAO.getIstRiesameParticellaFaseAnno(
                      idAzienda, oldConduzioneParticella.getIdParticella().longValue(), 
                      DateUtils.getCurrentYear().intValue(), documentoVO.getFaseIstanzaRiesame());
                  if(Validator.isNotEmpty(idIstanaRiesame))
                  {
                    Vector<Long> vIdIstanzaRiesame = new Vector<Long>();
                    vIdIstanzaRiesame.add(idIstanaRiesame);
                    documentoGaaDAO.annullaIstanzaFromId(vIdIstanzaRiesame, idUtenteAggiornamento);
                    PlSqlCodeDescription plCode = particellaGaaDAO.annullaIstanzaPlSql(documentoVO.getIdAzienda().longValue(), 
                        DateUtils.extractYearFromDate(documentoVO.getDataInserimento()), documentoVO.getFaseIstanzaRiesame(), 
                        idUtenteAggiornamento);
                    
                    if(plCode !=null)
                    {
                      if(Validator.isNotEmpty(plCode.getDescription()))
                      {
                        throw new DataAccessException("Aggiorna documento errore al plsql annullaIstanzaRiesame: "+plCode.getDescription()+"-"
                            +plCode.getOtherdescription());       
                      }
                    }
                    
                    
                  }
                    
                }                
                
              }
              
              
              //cessazione del documento
              Vector<DocumentoConduzioneVO> vDocumentoConduzione = documentoDAO
                  .getListDocumentoConduzioni(documentoVO.getIdDocumento(), false);
              Vector<Long> vIdParticella = null;
              boolean trovataUnaValida = false;
              for(int a=0;a<vDocumentoConduzione.size();a++)
              {
                if(Validator.isEmpty(vDocumentoConduzione.get(a).getDataFineValidita()))
                {
                  if(!trovataUnaValida)
                    trovataUnaValida = true;                 
                }
                else
                {
                  if(vIdParticella == null)
                  {
                    vIdParticella = new Vector<Long>();
                  }
                  if(!vIdParticella.contains(vDocumentoConduzione.get(a).getIdParticella()))
                  {
                    vIdParticella.add(vDocumentoConduzione.get(a).getIdParticella());
                  }
                }                
              }
              //non ho trovato conduzioni attive sul documento
              if(!trovataUnaValida)
              {
                Long idStatoDocumento = null;
                if(documentoGaaDAO.isAllIstanzaAnnullata(idAzienda, vIdParticella, 
                    DateUtils.getCurrentYear().intValue(), documentoVO.getFaseIstanzaRiesame()))
                {
                  idStatoDocumento = new Long(1);
                }               
                documentoDAO.updateDocumentoIstanza(documentoVO.getIdDocumento(), idStatoDocumento);
                
              }
              
            }
            
            
          } //elenco documenti
        } //if documenti
      }
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che si occupa di effettuare il cambio U.T.E. delle particelle
   * selezionate
   * 
   * @param elencoIdConduzioneParticella
   * @param ruoloUtenza
   * @param idUte
   * @param idAzienda
   * @throws Exception
   */
  public void cambiaUte(Long[] elencoIdConduzioneParticella,
      RuoloUtenza ruoloUtenza, Long idUte, Long idAzienda)
      throws Exception
  {
    try
    {
      
      //Lock della tabella per impedire più accessi contemporanei!!
      long[] arrIdConduzioneParticella = new long[elencoIdConduzioneParticella.length]; 
      for (int i = 0; i < elencoIdConduzioneParticella.length; i++)
      { 
        arrIdConduzioneParticella[i] = elencoIdConduzioneParticella[i].longValue();        
      }
      conduzioneParticellaDAO.lockTableConduzioneParticella(arrIdConduzioneParticella);      
      //**********************************
      
      for (int i = 0; i < elencoIdConduzioneParticella.length; i++)
      {
        Long idConduzioneParticella = (Long) elencoIdConduzioneParticella[i];
        // Recupero i dati della conduzione particella
        ConduzioneParticellaVO oldConduzioneParticella = conduzioneParticellaDAO
            .findConduzioneParticellaByPrimaryKey(idConduzioneParticella);
        if (Validator.isNotEmpty(oldConduzioneParticella
            .getSuperficieAgronomica()))
        {
          oldConduzioneParticella.setSuperficieAgronomica(StringUtils
              .parseSuperficieField(oldConduzioneParticella
                  .getSuperficieAgronomica()));
        }
        // Se la particella selezionata era legata ad un'UTE diversa da quella
        // selezionata dall'utente
        if (oldConduzioneParticella.getIdUte().compareTo(idUte) != 0)
        {
          // Cerco se esiste almeno una dichiarazione di consistenza
          ConduzioneDichiarataVO[] elencoConduzioniDichiarate = conduzioneDichiarataDAO
              .getListConduzioneDichiarataByIdConduzioneParticellaAndIdAzienda(
                  idConduzioneParticella, idAzienda, false, null);
          // Se esiste almeno una dichiarazione di consistenza
          if (elencoConduzioniDichiarate != null
              && elencoConduzioniDichiarate.length > 0)
          {
            // Cesso il record su DB_CONDUZIONE_PARTICELLA
            oldConduzioneParticella.setDataFineConduzione(new java.util.Date(
                new Timestamp(System.currentTimeMillis()).getTime()));
            conduzioneParticellaDAO
                .updateConduzioneParticella(oldConduzioneParticella);
            // Creo il nuovo oggetto da inserire
            ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
            // Setto i parametri
            conduzioneParticellaVO.setIdParticella(oldConduzioneParticella
                .getIdParticella());
            conduzioneParticellaVO.setIdUte(idUte);
            conduzioneParticellaVO.setIdTitoloPossesso(oldConduzioneParticella
                .getIdTitoloPossesso());
            conduzioneParticellaVO
                .setSuperficieCondotta(oldConduzioneParticella
                    .getSuperficieCondotta());
            conduzioneParticellaVO
              .setPercentualePossesso(oldConduzioneParticella
                .getPercentualePossesso());
            conduzioneParticellaVO.setFlagUtilizzoParte(null);
            conduzioneParticellaVO.setNote(null);
            conduzioneParticellaVO.setDataInizioConduzione(new java.util.Date(
                new Timestamp(System.currentTimeMillis()).getTime()));
            conduzioneParticellaVO.setDataFineConduzione(null);
            conduzioneParticellaVO.setDataAggiornamento(new java.util.Date(
                new Timestamp(System.currentTimeMillis()).getTime()));
            conduzioneParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
                .getIdUtente());
            conduzioneParticellaVO.setRecordModificato(SolmrConstants.FLAG_S);
            conduzioneParticellaVO.setEsitoControllo(null);
            conduzioneParticellaVO.setDataEsecuzione(null);
            if (Validator.isNotEmpty(oldConduzioneParticella
                .getSuperficieAgronomica()))
            {
              conduzioneParticellaVO.setSuperficieAgronomica(StringUtils
                  .parseSuperficieField(oldConduzioneParticella
                      .getSuperficieAgronomica()));
            }
            // Inserisco il nuovo record
            Long newIdConduzioneParticella = conduzioneParticellaDAO
                .insertConduzioneParticella(conduzioneParticellaVO);
            // Recupero i documenti attivi legati alla conduzione storicizzata
            Vector<DocumentoVO> elencoDocumenti = documentoDAO
                .getListDocumentiByIdConduzioneParticella(
                    idConduzioneParticella, true);
            // Se ce ne sono
            if (elencoDocumenti != null && elencoDocumenti.size() > 0)
            {
              Vector<DocumentoConduzioneVO> elencoConduzioni = null;
              for (int c = 0; c < elencoDocumenti.size(); c++)
              {
                DocumentoVO documentoVO = (DocumentoVO) elencoDocumenti
                    .elementAt(c);
                // Ricerco le conduzioni attive legate al precedente documento
                elencoConduzioni = documentoDAO
                    .getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella(
                        documentoVO.getIdDocumento(), oldConduzioneParticella
                            .getIdConduzioneParticella(), true);
                // Se ne trovo
                if (elencoConduzioni != null && elencoConduzioni.size() > 0)
                {
                  for (int d = 0; d < elencoConduzioni.size(); d++)
                  {
                    DocumentoConduzioneVO documentoConduzioneVO = (DocumentoConduzioneVO) elencoConduzioni
                        .elementAt(d);
                    // Storicizzo i records presenti su DB_DOCUMENTO_CONDUZIONE
                    documentoConduzioneVO
                        .setDataFineValidita(new java.util.Date(new Timestamp(
                            System.currentTimeMillis()).getTime()));
                    documentoDAO
                        .updateDocumentoConduzione(documentoConduzioneVO);
                    // Inserisco i nuovi records su DB_DOCUMENTO_CONDUZIONE
                    documentoConduzioneVO.setIdDocumentoConduzione(null);
                    documentoConduzioneVO
                        .setIdConduzioneParticella(newIdConduzioneParticella);
                    documentoConduzioneVO
                        .setDataInserimento(new java.util.Date(new Timestamp(
                            System.currentTimeMillis()).getTime()));
                    documentoConduzioneVO
                        .setDataInizioValidita(new java.util.Date(
                            new Timestamp(System.currentTimeMillis()).getTime()));
                    documentoConduzioneVO.setDataFineValidita(null);
                    documentoDAO
                        .insertDocumentoConduzione(documentoConduzioneVO);
                  }
                }
              }
            }
            // Recupero gli utilizzi legati alla vecchia conduzione
            UtilizzoParticellaVO[] elencoUtilizzi = getListUtilizzoParticellaVOByIdConduzioneParticella(
                idConduzioneParticella, null, false);
            // Se ne trovo
            if (elencoUtilizzi != null && elencoUtilizzi.length > 0)
            {
              // Li duplico associandoli alla nuova condizione appena inserita
              for (int b = 0; b < elencoUtilizzi.length; b++)
              {
                UtilizzoParticellaVO oldUtilizzoParticellaVO = (UtilizzoParticellaVO) elencoUtilizzi[b];
                // Creo il nuovo oggetto da inserire
                /*UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
                utilizzoParticellaVO.setIdUtilizzo(oldUtilizzoParticellaVO
                    .getIdUtilizzo());
                utilizzoParticellaVO
                    .setIdConduzioneParticella(newIdConduzioneParticella);
                utilizzoParticellaVO
                    .setSuperficieUtilizzata(oldUtilizzoParticellaVO
                        .getSuperficieUtilizzata());
                utilizzoParticellaVO.setDataAggiornamento(new java.util.Date(
                    new Timestamp(System.currentTimeMillis()).getTime()));
                utilizzoParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
                    .getIdUtente());
                utilizzoParticellaVO.setAnno(DateUtils.getCurrentYear()
                    .toString());
                utilizzoParticellaVO.setNote(null);
                utilizzoParticellaVO
                    .setIdUtilizzoSecondario(oldUtilizzoParticellaVO
                        .getIdUtilizzoSecondario());
                utilizzoParticellaVO
                    .setSupUtilizzataSecondaria(oldUtilizzoParticellaVO
                        .getSupUtilizzataSecondaria());
                utilizzoParticellaVO.setIdVarieta(oldUtilizzoParticellaVO
                    .getIdVarieta());
                utilizzoParticellaVO
                    .setIdVarietaSecondaria(oldUtilizzoParticellaVO
                        .getIdVarietaSecondaria());
                utilizzoParticellaVO.setAnnoImpianto(oldUtilizzoParticellaVO
                    .getAnnoImpianto());
                utilizzoParticellaVO.setIdImpianto(oldUtilizzoParticellaVO
                    .getIdImpianto());
                utilizzoParticellaVO.setSestoSuFile(oldUtilizzoParticellaVO
                    .getSestoSuFile());
                utilizzoParticellaVO.setSestoTraFile(oldUtilizzoParticellaVO
                    .getSestoTraFile());
                utilizzoParticellaVO
                    .setNumeroPianteCeppi(oldUtilizzoParticellaVO
                        .getNumeroPianteCeppi());*/
                // Inserisco il nuovo utilizzo
                oldUtilizzoParticellaVO
                  .setIdConduzioneParticella(newIdConduzioneParticella);
                oldUtilizzoParticellaVO.setDataAggiornamento(new java.util.Date(
                    new Timestamp(System.currentTimeMillis()).getTime()));
                oldUtilizzoParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
                    .getIdUtente());
                oldUtilizzoParticellaVO.setAnno(DateUtils.getCurrentYear()
                    .toString());
                oldUtilizzoParticellaVO.setNote(null);
                
                utilizzoParticellaDAO
                    .insertUtilizzoParticella(oldUtilizzoParticellaVO);
              }
            }
          }
          // Se invece non risultano dichiarazioni di consistenza
          else
          {
            // Effettuo semplicemente un'update su DB_CONDUZIONE_PARTICELLA
            oldConduzioneParticella.setIdUte(idUte);
            oldConduzioneParticella.setDataAggiornamento(new java.util.Date(
                new Timestamp(System.currentTimeMillis()).getTime()));
            oldConduzioneParticella.setIdUtenteAggiornamento(ruoloUtenza
                .getIdUtente());
            oldConduzioneParticella.setRecordModificato(SolmrConstants.FLAG_S);
            oldConduzioneParticella.setEsitoControllo(null);
            oldConduzioneParticella.setDataEsecuzione(null);
            conduzioneParticellaDAO
                .updateConduzioneParticella(oldConduzioneParticella);
          }
        }
      }
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo utilizzato per associare un documento alle particelle selezionate
   * 
   * @param elencoIdConduzioneParticella
   * @param idDocumento
   * @throws Exception
   */
  public void associaDocumento(Long[] elencoIdConduzioneParticella,
      Long idDocumento) throws Exception
  {
    try
    {
      //Lock della tabella per impedire più accessi contemporanei!!
      long[] arrIdConduzioneParticella = new long[elencoIdConduzioneParticella.length]; 
      for (int i = 0; i < elencoIdConduzioneParticella.length; i++)
      { 
        arrIdConduzioneParticella[i] = elencoIdConduzioneParticella[i].longValue();        
      }
      conduzioneParticellaDAO.lockTableConduzioneParticella(arrIdConduzioneParticella);      
      //**********************************
      
      Vector<DocumentoConduzioneVO> elencoDocumentiConduzioni = null;
      DocumentoVO documentoVO = documentoDAO.findDocumentoVOByPrimaryKey(idDocumento);
      for (int i = 0; i < elencoIdConduzioneParticella.length; i++)
      {
        Long idConduzioneParticella = (Long) elencoIdConduzioneParticella[i];
        // Ricerco se esistono già documenti_conduzioni attive per il documento
        // che si vuole
        // inserire e la particella selezionata
        elencoDocumentiConduzioni = documentoDAO
            .getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella(
                idDocumento, idConduzioneParticella, true);
        // Se non ci sono
        if (elencoDocumentiConduzioni == null
            || elencoDocumentiConduzioni.size() == 0)
        {
          // Inserisco un nuovo record sulla tabella DB_DOCUMENTO_CONDUZIONE
          DocumentoConduzioneVO documentoConduzioneVO = new DocumentoConduzioneVO();
          documentoConduzioneVO.setIdDocumento(idDocumento);
          documentoConduzioneVO
              .setIdConduzioneParticella(idConduzioneParticella);
          documentoConduzioneVO.setDataInserimento(new java.util.Date(
              new Timestamp(System.currentTimeMillis()).getTime()));
          documentoConduzioneVO.setDataInizioValidita(new java.util.Date(
              new Timestamp(System.currentTimeMillis()).getTime()));
          if (documentoVO.getDataFineValidita() != null)
          {
            documentoConduzioneVO.setDataFineValidita(new Timestamp(
                documentoVO.getDataFineValidita().getTime()));
          }
          documentoDAO.insertDocumentoConduzione(documentoConduzioneVO);
        }
      }
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo utilizzato per effettuare la dichiarazione dell'uso agronomico
   * 
   * @param elencoParticelle
   * @param ruoloUtenza
   * @param idAzienda
   * @throws Exception
   */
  public void dichiaraUsoAgronomico(StoricoParticellaVO[] elencoParticelle,
      RuoloUtenza ruoloUtenza, Long idAzienda) throws Exception
  {
    try
    {
      //Lock della tabella per impedire più accessi contemporanei!!
      long[] arrIdConduzioneParticella = new long[elencoParticelle.length]; 
      for (int i = 0; i < elencoParticelle.length; i++)
      {
        StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) elencoParticelle[i];
        ConduzioneParticellaVO conduzioneParticellaVO = storicoParticellaVO
            .getElencoConduzioni()[0];   
        arrIdConduzioneParticella[i] = conduzioneParticellaVO.getIdConduzioneParticella().longValue();        
      }
      conduzioneParticellaDAO.lockTableConduzioneParticella(arrIdConduzioneParticella);      
      //**********************************
      Long oldIdConduzioneParticella = null;
      for (int i = 0; i < elencoParticelle.length; i++)
      {
        StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) elencoParticelle[i];
        ConduzioneParticellaVO conduzioneParticellaVO = storicoParticellaVO
            .getElencoConduzioni()[0];
        if (oldIdConduzioneParticella == null
            || oldIdConduzioneParticella.compareTo(conduzioneParticellaVO
                .getIdConduzioneParticella()) != 0)
        {
          // Recupero i dati della conduzione particella
          ConduzioneParticellaVO oldConduzioneParticella = conduzioneParticellaDAO
              .findConduzioneParticellaByPrimaryKey(conduzioneParticellaVO
                  .getIdConduzioneParticella());
          if (Validator.isNotEmpty(oldConduzioneParticella
              .getSuperficieAgronomica()))
          {
            oldConduzioneParticella.setSuperficieAgronomica(StringUtils
                .parseSuperficieField(oldConduzioneParticella
                    .getSuperficieAgronomica()));
          }
          // Se la nuova superficie agronomica è diversa dalla precedente
          if ((Validator.isNotEmpty(conduzioneParticellaVO
              .getSuperficieAgronomica()) && !Validator
              .isNotEmpty(oldConduzioneParticella.getSuperficieAgronomica()))
              || (!Validator.isNotEmpty(conduzioneParticellaVO
                  .getSuperficieAgronomica()) && Validator
                  .isNotEmpty(oldConduzioneParticella.getSuperficieAgronomica()))
              || (!StringUtils.parseSuperficieField(
                  conduzioneParticellaVO.getSuperficieAgronomica())
                  .equalsIgnoreCase(
                      oldConduzioneParticella.getSuperficieAgronomica())))
          {
            // Cerco se esiste almeno una dichiarazione di consistenza
            ConduzioneDichiarataVO[] elencoConduzioniDichiarate = conduzioneDichiarataDAO
                .getListConduzioneDichiarataByIdConduzioneParticellaAndIdAzienda(
                    conduzioneParticellaVO.getIdConduzioneParticella(),
                    idAzienda, false, null);
            // Se esiste almeno una dichiarazione di consistenza
            if (elencoConduzioniDichiarate != null
                && elencoConduzioniDichiarate.length > 0)
            {
              // Cesso il record su DB_CONDUZIONE_PARTICELLA
              oldConduzioneParticella.setDataFineConduzione(new java.util.Date(
                  new Timestamp(System.currentTimeMillis()).getTime()));
              conduzioneParticellaDAO
                  .updateConduzioneParticella(oldConduzioneParticella);
              // Creo il nuovo oggetto da inserire
              ConduzioneParticellaVO newConduzioneParticellaVO = new ConduzioneParticellaVO();
              // Setto i parametri
              newConduzioneParticellaVO.setIdParticella(oldConduzioneParticella
                  .getIdParticella());
              newConduzioneParticellaVO.setIdUte(oldConduzioneParticella
                  .getIdUte());
              newConduzioneParticellaVO
                  .setIdTitoloPossesso(oldConduzioneParticella
                      .getIdTitoloPossesso());
              newConduzioneParticellaVO
                  .setSuperficieCondotta(oldConduzioneParticella
                      .getSuperficieCondotta());
              newConduzioneParticellaVO
                .setPercentualePossesso(oldConduzioneParticella
                  .getPercentualePossesso());
              newConduzioneParticellaVO.setFlagUtilizzoParte(null);
              newConduzioneParticellaVO.setNote(null);
              newConduzioneParticellaVO
                  .setDataInizioConduzione(new java.util.Date(new Timestamp(
                      System.currentTimeMillis()).getTime()));
              newConduzioneParticellaVO.setDataFineConduzione(null);
              newConduzioneParticellaVO
                  .setDataAggiornamento(new java.util.Date(new Timestamp(System
                      .currentTimeMillis()).getTime()));
              newConduzioneParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
                  .getIdUtente());
              newConduzioneParticellaVO
                  .setRecordModificato(SolmrConstants.FLAG_S);
              newConduzioneParticellaVO.setEsitoControllo(null);
              newConduzioneParticellaVO.setDataEsecuzione(null);
              String supAgr =  StringUtils.parseSuperficieField(conduzioneParticellaVO
                  .getSuperficieAgronomica());
              if(supAgr.equalsIgnoreCase("0,0000"))
              {
                newConduzioneParticellaVO.setSuperficieAgronomica(null);
              }
              else
              {
                newConduzioneParticellaVO.setSuperficieAgronomica(supAgr);
              }
              // Inserisco il nuovo record
              Long newIdConduzioneParticella = conduzioneParticellaDAO
                  .insertConduzioneParticella(newConduzioneParticellaVO);
              // Recupero i documenti attivi legati alla conduzione storicizzata
              Vector<DocumentoVO> elencoDocumenti = documentoDAO
                  .getListDocumentiByIdConduzioneParticella(
                      conduzioneParticellaVO.getIdConduzioneParticella(), true);
              // Se ce ne sono
              if (elencoDocumenti != null && elencoDocumenti.size() > 0)
              {
                Vector<DocumentoConduzioneVO> elencoConduzioni = null;
                for (int c = 0; c < elencoDocumenti.size(); c++)
                {
                  DocumentoVO documentoVO = (DocumentoVO) elencoDocumenti
                      .elementAt(c);
                  // Ricerco le conduzioni attive legate al precedente documento
                  elencoConduzioni = documentoDAO
                      .getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella(
                          documentoVO.getIdDocumento(), oldConduzioneParticella
                              .getIdConduzioneParticella(), true);
                  // Se ne trovo
                  if (elencoConduzioni != null && elencoConduzioni.size() > 0)
                  {
                    for (int d = 0; d < elencoConduzioni.size(); d++)
                    {
                      DocumentoConduzioneVO documentoConduzioneVO = (DocumentoConduzioneVO) elencoConduzioni
                          .elementAt(d);
                      // Storicizzo i records presenti su
                      // DB_DOCUMENTO_CONDUZIONE
                      documentoConduzioneVO
                          .setDataFineValidita(new java.util.Date(
                              new Timestamp(System.currentTimeMillis())
                                  .getTime()));
                      documentoDAO
                          .updateDocumentoConduzione(documentoConduzioneVO);
                      // Inserisco i nuovi records su DB_DOCUMENTO_CONDUZIONE
                      documentoConduzioneVO.setIdDocumentoConduzione(null);
                      documentoConduzioneVO
                          .setIdConduzioneParticella(newIdConduzioneParticella);
                      documentoConduzioneVO
                          .setDataInserimento(new java.util.Date(new Timestamp(
                              System.currentTimeMillis()).getTime()));
                      documentoConduzioneVO
                          .setDataInizioValidita(new java.util.Date(
                              new Timestamp(System.currentTimeMillis())
                                  .getTime()));
                      documentoConduzioneVO.setDataFineValidita(null);
                      documentoDAO
                          .insertDocumentoConduzione(documentoConduzioneVO);
                    }
                  }
                }
              }
              // Recupero gli utilizzi legati alla vecchia conduzione
              UtilizzoParticellaVO[] elencoUtilizzi = getListUtilizzoParticellaVOByIdConduzioneParticella(
                  oldConduzioneParticella.getIdConduzioneParticella(), null,
                  false);
              // Se ne trovo
              if (elencoUtilizzi != null && elencoUtilizzi.length > 0)
              {
                // Li duplico associandoli alla nuova condizione appena inserita
                for (int b = 0; b < elencoUtilizzi.length; b++)
                {
                  UtilizzoParticellaVO oldUtilizzoParticellaVO = (UtilizzoParticellaVO) elencoUtilizzi[b];
                  // Creo il nuovo oggetto da inserire
                  /*UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
                  utilizzoParticellaVO.setIdUtilizzo(oldUtilizzoParticellaVO
                      .getIdUtilizzo());
                  utilizzoParticellaVO
                      .setIdConduzioneParticella(newIdConduzioneParticella);
                  utilizzoParticellaVO
                      .setSuperficieUtilizzata(oldUtilizzoParticellaVO
                          .getSuperficieUtilizzata());
                  utilizzoParticellaVO.setDataAggiornamento(new java.util.Date(
                      new Timestamp(System.currentTimeMillis()).getTime()));
                  utilizzoParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
                      .getIdUtente());
                  utilizzoParticellaVO.setAnno(DateUtils.getCurrentYear()
                      .toString());
                  utilizzoParticellaVO.setNote(null);
                  utilizzoParticellaVO
                      .setIdUtilizzoSecondario(oldUtilizzoParticellaVO
                          .getIdUtilizzoSecondario());
                  utilizzoParticellaVO
                      .setSupUtilizzataSecondaria(oldUtilizzoParticellaVO
                          .getSupUtilizzataSecondaria());
                  utilizzoParticellaVO.setIdVarieta(oldUtilizzoParticellaVO
                      .getIdVarieta());
                  utilizzoParticellaVO
                      .setIdVarietaSecondaria(oldUtilizzoParticellaVO
                          .getIdVarietaSecondaria());
                  utilizzoParticellaVO.setAnnoImpianto(oldUtilizzoParticellaVO
                      .getAnnoImpianto());
                  utilizzoParticellaVO.setIdImpianto(oldUtilizzoParticellaVO
                      .getIdImpianto());
                  utilizzoParticellaVO.setSestoSuFile(oldUtilizzoParticellaVO
                      .getSestoSuFile());
                  utilizzoParticellaVO.setSestoTraFile(oldUtilizzoParticellaVO
                      .getSestoTraFile());
                  utilizzoParticellaVO
                      .setNumeroPianteCeppi(oldUtilizzoParticellaVO
                          .getNumeroPianteCeppi());*/
                  oldUtilizzoParticellaVO
                    .setIdConduzioneParticella(newIdConduzioneParticella);
                  oldUtilizzoParticellaVO.setDataAggiornamento(new java.util.Date(
                      new Timestamp(System.currentTimeMillis()).getTime()));
                  oldUtilizzoParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
                      .getIdUtente());
                  oldUtilizzoParticellaVO.setAnno(DateUtils.getCurrentYear()
                      .toString());
                  oldUtilizzoParticellaVO.setNote(null);
                  // Inserisco il nuovo utilizzo
                  utilizzoParticellaDAO
                      .insertUtilizzoParticella(oldUtilizzoParticellaVO);
                }
              }
            }
            // Se invece non risultano dichiarazioni di consistenza
            else
            {
              // Effettuo semplicemente un'update su DB_CONDUZIONE_PARTICELLA
              
              String supAgr =  StringUtils.parseSuperficieField(conduzioneParticellaVO
                  .getSuperficieAgronomica());
              
              if(supAgr.equalsIgnoreCase("0,0000"))
              {
                oldConduzioneParticella.setSuperficieAgronomica(null);
              }
              else
              {
                oldConduzioneParticella.setSuperficieAgronomica(supAgr);
              }
              
              oldConduzioneParticella.setDataAggiornamento(new java.util.Date(
                  new Timestamp(System.currentTimeMillis()).getTime()));
              oldConduzioneParticella.setIdUtenteAggiornamento(ruoloUtenza
                  .getIdUtente());
              oldConduzioneParticella
                  .setRecordModificato(SolmrConstants.FLAG_S);
              oldConduzioneParticella.setEsitoControllo(null);
              oldConduzioneParticella.setDataEsecuzione(null);
              conduzioneParticellaDAO
                  .updateConduzioneParticella(oldConduzioneParticella);
            }
          }
        }
        oldIdConduzioneParticella = conduzioneParticellaVO
            .getIdConduzioneParticella();
      }
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che si occupa di estrarre l'impianto a partire dalla sua chiave
   * primaria
   * 
   * @param idImpianto
   * @return it.csi.solmr.dto.anag.terreni.TipoImpiantoVO
   * @throws Exception
   */
  public TipoImpiantoVO findTipoImpiantoByPrimaryKey(Long idImpianto)
      throws Exception
  {
    try
    {
      return commonDAO.findTipoImpiantoByPrimaryKey(idImpianto);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce l'elenco degli utilizzi consociati a partire
   * dall'id_utilizzo_particella
   * 
   * @param idUtilizzoParticella
   * @param orderBy
   * @return java.util.Vector
   * @throws Exception
   */
  public Vector<UtilizzoConsociatoVO> getListUtilizziConsociatiByIdUtilizzoParticella(
      Long idUtilizzoParticella, String[] orderBy) throws Exception
  {
    try
    {
      return utilizzoConsociatoDAO
          .getListUtilizziConsociatiByIdUtilizzoParticella(
              idUtilizzoParticella, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce l'elenco degli usi del suolo associati
   * all'azienda: al contrario del metodo getListTipiUsoSuoloByIdAzienda()
   * questo mi restituisce solo l'uso primario o secondario a seconda del valore
   * del parametro colturaSecondaria
   * 
   * @param idAzienda
   * @param onlyActive
   * @param orderBy
   * @param colturaSecondaria
   * @return java.util.Vector
   * @throws Exception
   */
  public Vector<TipoUtilizzoVO> findListTipiUsoSuoloByIdAzienda(Long idAzienda,
      String colturaSecondaria)
      throws Exception
  {
    try
    {
      return tipoUtilizzoDAO.findListTipiUsoSuoloByIdAzienda(idAzienda,
          colturaSecondaria);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public Vector<TipoUtilizzoVO> findListTipiUsoSuoloByIdAziendaCess(Long idAzienda, String[] orderBy) throws Exception
  {
    try
    {
      return tipoUtilizzoDAO.findListTipiUsoSuoloByIdAziendaCess(idAzienda,orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che si occupa di effettuare la modifica delle particelle selezionate
   * 
   * @param elencoStoricoParticella
   * @param ruoloUtenza
   * @param anagAziendaVO
   * @throws Exception
   */
  public void modificaParticelle(StoricoParticellaVO[] elencoStoricoParticella,
      RuoloUtenza ruoloUtenza, AnagAziendaVO anagAziendaVO)
      throws Exception
  {
    try
    {
      java.util.Date dataInizio = new java.util.Date(new Timestamp(System
          .currentTimeMillis()).getTime());
      // Ciclo le particelle selezionate
      for (int i = 0; i < elencoStoricoParticella.length; i++)
      {
        // TABELLA DB_STORICO_PARTICELLA
        StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) elencoStoricoParticella[i];
        // Recupero il dato corrispettivo presente sul DB per verificare
        // se sono state effettuate delle modifiche
        StoricoParticellaVO oldStoricoParticellaVO = storicoParticellaDAO
            .findStoricoParticellaByPrimaryKey(storicoParticellaVO
                .getIdStoricoParticella());
        // Setto i parametri in questo modo così da permettere al metodo equals
        // di funzionare in modo corretto
        oldStoricoParticellaVO.setElencoConduzioni(storicoParticellaVO
            .getElencoConduzioni());
        oldStoricoParticellaVO
            .setElencoConduzioniDichiarate(storicoParticellaVO
                .getElencoConduzioniDichiarate());
        oldStoricoParticellaVO.setDocumentoVO(storicoParticellaVO
            .getDocumentoVO());
        oldStoricoParticellaVO.setFoglioVO(storicoParticellaVO.getFoglioVO());
        ConduzioneDichiarataVO[] elencoConduzioneDichiarataVO = conduzioneDichiarataDAO
            .getListConduzioneDichiarataByidStoricoParticella(storicoParticellaVO
                .getIdStoricoParticella());
        // Se sono stati modificati i dati territoriali
        if (!storicoParticellaVO.equalsDatiTerritoriali(oldStoricoParticellaVO))
        {
          // Se la data di aggiornamento è uguale a SYSDATE
          if (DateUtils.formatDate(
              oldStoricoParticellaVO.getDataAggiornamento()).equalsIgnoreCase(
              DateUtils.formatDate(new java.util.Date(System
                  .currentTimeMillis()))))
          {
            // ... se esiste un record su DB_CONDUZIONE_DICHIARATA per
            // id_storico
            // particella selezionato
            if (elencoConduzioneDichiarataVO.length > 0)
            {
              // Storicizzo il record su DB_STORICO_PARTICELLA
              //oldStoricoParticellaVO.setDataFineValidita(new java.util.Date(
                  //new Timestamp(System.currentTimeMillis()).getTime()));
              storicoParticellaDAO.storicizzaStoricoParticella(oldStoricoParticellaVO
                  .getIdStoricoParticella().longValue());
                 // .updateStoricoParticella(oldStoricoParticellaVO);
              // Ricerco il tipo documento in funzione del documento selezionato
              if (storicoParticellaVO.getIdDocumentoProtocollato() != null)
              {
                DocumentoVO documentoVO = documentoDAO
                    .findDocumentoVOByPrimaryKey(storicoParticellaVO
                        .getIdDocumentoProtocollato());
                storicoParticellaVO.setIdDocumento(documentoVO
                    .getExtIdDocumento());
              }
              // ... inserisco un nuovo record su DB_STORICO_PARTICELLA
              storicoParticellaVO.setDataInizioValidita(dataInizio);
              storicoParticellaVO.setDataAggiornamento(new java.util.Date(
                  new Timestamp(System.currentTimeMillis()).getTime()));
              storicoParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
                  .getIdUtente());
              storicoParticellaDAO.insertStoricoParticella(storicoParticellaVO);
              // Faccio update su DB_PARTICELLA
              ParticellaVO particellaVO = new ParticellaVO();
              particellaVO.setDataInizioValidita(dataInizio);
              particellaVO.setIdParticella(storicoParticellaVO
                  .getIdParticella());
              particellaDAO.updateParticella(particellaVO);
            }
            // in caso contrario modifico il record selezionato
            else
            {
              // Ricerco il tipo documento in funzione del documento selezionato
              if (storicoParticellaVO.getIdDocumentoProtocollato() != null)
              {
                DocumentoVO documentoVO = documentoDAO
                    .findDocumentoVOByPrimaryKey(storicoParticellaVO
                        .getIdDocumentoProtocollato());
                storicoParticellaVO.setIdDocumento(documentoVO
                    .getExtIdDocumento());
              }
              // ... modifico il record su DB_STORICO_PARTICELLA
              storicoParticellaVO.setDataAggiornamento(new java.util.Date(
                  new Timestamp(System.currentTimeMillis()).getTime()));
              storicoParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
                  .getIdUtente());
              storicoParticellaDAO.updateStoricoParticella(storicoParticellaVO);
            }
          }
          // ... Altrimenti...
          else
          {
            // Storicizzo il record su DB_STORICO_PARTICELLA
            //oldStoricoParticellaVO.setDataFineValidita(new java.util.Date(
                //new Timestamp(System.currentTimeMillis()).getTime()));
            //storicoParticellaDAO
                //.updateStoricoParticella(oldStoricoParticellaVO);
            storicoParticellaDAO.storicizzaStoricoParticella(oldStoricoParticellaVO
                .getIdStoricoParticella().longValue());
            // Ricerco il tipo documento in funzione del documento selezionato
            if (storicoParticellaVO.getIdDocumentoProtocollato() != null)
            {
              DocumentoVO documentoVO = documentoDAO
                  .findDocumentoVOByPrimaryKey(storicoParticellaVO
                      .getIdDocumentoProtocollato());
              storicoParticellaVO.setIdDocumento(documentoVO
                  .getExtIdDocumento());
            }
            // ... inserisco un nuovo record su DB_STORICO_PARTICELLA
            storicoParticellaVO.setDataInizioValidita(dataInizio);
            storicoParticellaVO.setDataAggiornamento(new java.util.Date(
                new Timestamp(System.currentTimeMillis()).getTime()));
            storicoParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
                .getIdUtente());
            storicoParticellaDAO.insertStoricoParticella(storicoParticellaVO);
            // Faccio update su DB_PARTICELLA
            ParticellaVO particellaVO = new ParticellaVO();
            particellaVO.setDataInizioValidita(dataInizio);
            particellaVO.setIdParticella(storicoParticellaVO.getIdParticella());
            particellaDAO.updateParticella(particellaVO);
          }
        }
        
        
        
        
        //Carico tipi area
        //confronto i tipi area e modifico solo quelli cambiati...
        if(storicoParticellaVO.getvValoriTipoArea() != null)
        {
          for(int j=0;j<storicoParticellaVO.getvValoriTipoArea().size();j++)
          {
            TipoAreaVO tipoAreaVO = storicoParticellaVO.getvValoriTipoArea().get(j);
            TipoValoreAreaVO tipoAreaValoreVO = tipoAreaVO.getvTipoValoreArea().get(0);
            if(Validator.isNotEmpty(tipoAreaValoreVO.getValore())
              && !"S".equalsIgnoreCase(tipoAreaVO.getFlagEsclusivoFoglio()))
            { 
              //qui devo confrontare......
              String valore = particellaGaaDAO.getValoreAttivoTipoAreaFromParticellaAndId(storicoParticellaVO.getIdParticella(),
                  tipoAreaVO.getIdTipoArea());
              if(Validator.isNotEmpty(valore))
              {
                //se lo trovo su db ma sono diversi....
                if(Validator.isNotEmpty(tipoAreaValoreVO.getValore()) && !valore.equalsIgnoreCase(tipoAreaValoreVO.getValore()))
                {
                  //storicizzo
                  particellaGaaDAO.storicizzaRParticellaArea(storicoParticellaVO.getIdParticella(), 
                      tipoAreaValoreVO.getIdTipoValoreArea());
                  
                  //inserisco
                  Long idTipoValoreArea = particellaGaaDAO.getIdTipoValoreArea(tipoAreaVO.getIdTipoArea(), 
                      tipoAreaValoreVO.getValore());
                  particellaGaaDAO.insertRParticellaArea(storicoParticellaVO.getIdParticella(), idTipoValoreArea);
                }
              }
              //non esiste su db inserisco
              else
              {
                Long idTipoValoreArea = particellaGaaDAO.getIdTipoValoreArea(tipoAreaVO.getIdTipoArea(), 
                    tipoAreaValoreVO.getValore());
                particellaGaaDAO.insertRParticellaArea(storicoParticellaVO.getIdParticella(), idTipoValoreArea);
              }              
            }
            
          }                 
        }
        
        
        
        
        // DB_CONDUZIONE_PARTICELLA
        ConduzioneParticellaVO conduzioneParticellaVO = (ConduzioneParticellaVO) storicoParticellaVO
            .getElencoConduzioni()[0];
        ConduzioneParticellaVO oldConduzioneParticella = conduzioneParticellaDAO
            .findConduzioneParticellaByPrimaryKey(conduzioneParticellaVO
                .getIdConduzioneParticella());
        if (Validator.isNotEmpty(oldConduzioneParticella
            .getSuperficieAgronomica()))
        {
          oldConduzioneParticella.setSuperficieAgronomica(StringUtils
              .parseSuperficieField(oldConduzioneParticella
                  .getSuperficieAgronomica()));
        }
        if (Validator.isNotEmpty(conduzioneParticellaVO
            .getSuperficieAgronomica()))
        {
          conduzioneParticellaVO.setSuperficieAgronomica(StringUtils
              .parseSuperficieField(conduzioneParticellaVO
                  .getSuperficieAgronomica()));
        }
        
        oldConduzioneParticella.setSuperficieCondotta(StringUtils
            .parseSuperficieField(oldConduzioneParticella
                .getSuperficieCondotta()));      
        conduzioneParticellaVO.setSuperficieCondotta(StringUtils
            .parseSuperficieField(conduzioneParticellaVO
                .getSuperficieCondotta()));
        
        oldConduzioneParticella.setElencoUtilizzi(conduzioneParticellaVO
            .getElencoUtilizzi());
        UtilizzoParticellaVO[] oldElencoUtilizzi = utilizzoParticellaDAO
            .getListUtilizzoParticellaVOByIdConduzioneParticella(
                conduzioneParticellaVO.getIdConduzioneParticella(), null, true);
        // Se sono stati modificati i dati della conduzione ...
        if (!conduzioneParticellaVO
            .equalsDatiConduzione(oldConduzioneParticella))
        {
          ConduzioneDichiarataVO[] elencoConduzioniDichiarate = conduzioneDichiarataDAO
              .getListConduzioneDichiarataByIdConduzioneParticellaAndIdAzienda(
                  conduzioneParticellaVO.getIdConduzioneParticella(),
                  anagAziendaVO.getIdAzienda(), false, null);
          // Se la conduzione non è presente in nessuna dichiarazione di
          // consistenza
          // legata all'azienda...
          if (elencoConduzioniDichiarate.length == 0)
          {
            // ... Effettuo update su DB_CONDUZIONE_PARTICELLA
            conduzioneParticellaVO.setDataAggiornamento(new java.util.Date(
                new Timestamp(System.currentTimeMillis()).getTime()));
            conduzioneParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
                .getIdUtente());
            conduzioneParticellaVO.setRecordModificato(SolmrConstants.FLAG_S);
            conduzioneParticellaVO.setEsitoControllo(null);
            conduzioneParticellaVO.setDataEsecuzione(null);
            conduzioneParticellaDAO
                .updateConduzioneParticella(conduzioneParticellaVO);
            // Se è stato modificato il titolo di possesso
            if (oldConduzioneParticella.getIdTitoloPossesso().compareTo(
                conduzioneParticellaVO.getIdTitoloPossesso()) != 0)
            {
              // Recupero i documenti attivi legati alla conduzione modificata
              Vector<DocumentoVO> elencoDocumenti = documentoDAO
                  .getListDocumentiByIdConduzioneParticella(
                      conduzioneParticellaVO.getIdConduzioneParticella(), true);
              // Se ce ne sono ...
              if (elencoDocumenti != null && elencoDocumenti.size() > 0)
              {
                Vector<DocumentoConduzioneVO> elencoConduzioni = null;
                for (int a = 0; a < elencoDocumenti.size(); a++)
                {
                  DocumentoVO documentoVO = (DocumentoVO) elencoDocumenti
                      .elementAt(a);
                  // Ricerco le conduzioni attive legate al precedente documento
                  elencoConduzioni = documentoDAO
                      .getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella(
                          documentoVO.getIdDocumento(), oldConduzioneParticella
                              .getIdConduzioneParticella(), true);
                  // Se ne trovo
                  if (elencoConduzioni != null && elencoConduzioni.size() > 0)
                  {
                    for (int d = 0; d < elencoConduzioni.size(); d++)
                    {
                      DocumentoConduzioneVO documentoConduzioneVO = (DocumentoConduzioneVO) elencoConduzioni
                          .elementAt(d);
                      // Storicizzo i records presenti su DB_DOCUMENTO_CONDUZIONE
                      documentoConduzioneVO
                          .setDataFineValidita(new java.util.Date(new Timestamp(
                              System.currentTimeMillis()).getTime()));
                      documentoDAO
                          .updateDocumentoConduzione(documentoConduzioneVO);                   
                      
                      if (oldConduzioneParticella
                          .getIdTitoloPossesso()
                          .compareTo(conduzioneParticellaVO.getIdTitoloPossesso()) == 0)
                      {
                        documentoConduzioneVO.setIdDocumentoConduzione(null);
                        documentoConduzioneVO
                            .setIdConduzioneParticella(conduzioneParticellaVO
                                .getIdConduzioneParticella());
                        documentoConduzioneVO
                            .setDataInserimento(new java.util.Date(new Timestamp(
                                System.currentTimeMillis()).getTime()));
                        documentoConduzioneVO
                            .setDataInizioValidita(new java.util.Date(
                                new Timestamp(System.currentTimeMillis())
                                    .getTime()));
                        documentoConduzioneVO.setDataFineValidita(null);
                        documentoDAO
                            .insertDocumentoConduzione(documentoConduzioneVO);
                      }
                      //E' cambiato il titolo possesso
                      else
                      {
                        // Inserisco i nuovi records su DB_DOCUMENTO_CONDUZIONE solo
                        // sempre se istanza di riesame di fase 1 e il titolo possesso diverso asservimento
                        // o di altre fasi
                        if(documentoDAO.isDocIstanzaRiesame(documentoVO.getExtIdDocumento().longValue()))
                        {
                          if(((documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_FOTO)
                            && (conduzioneParticellaVO.getIdTitoloPossesso().compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) != 0))
                            || (documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_CONTRO)
                            || (documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_SOPRA))
                          {                        
                            documentoConduzioneVO.setIdDocumentoConduzione(null);
                            documentoConduzioneVO
                                .setIdConduzioneParticella(conduzioneParticellaVO
                                    .getIdConduzioneParticella());
                            documentoConduzioneVO
                                .setDataInserimento(new java.util.Date(new Timestamp(
                                    System.currentTimeMillis()).getTime()));
                            documentoConduzioneVO
                                .setDataInizioValidita(new java.util.Date(
                                    new Timestamp(System.currentTimeMillis())
                                        .getTime()));
                            documentoConduzioneVO.setDataFineValidita(null);
                            documentoDAO
                                .insertDocumentoConduzione(documentoConduzioneVO);                        
                          }
                        }
                      }                  
                      
                      
  
                    }
                  }
                  
                  
                  
                  if(conduzioneParticellaVO.getIdTitoloPossesso().compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) == 0)
                  {                  
                    if(documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_FOTO)
                    {                                            
                      if (elencoConduzioni != null && elencoConduzioni.size() > 0)
                      {
                        if(!documentoGaaDAO.existAltroLegameIstRiesameParticella(
                            anagAziendaVO.getIdAzienda(), oldConduzioneParticella.getIdParticella().longValue(), 
                           DateUtils.getCurrentYear().intValue(), 
                           documentoVO.getExtIdDocumento().longValue()))
                        {                 
                          
                          Long idIstanaRiesame = documentoGaaDAO.getIstRiesameParticellaFaseAnno(
                              anagAziendaVO.getIdAzienda(), oldConduzioneParticella.getIdParticella().longValue(), 
                              DateUtils.getCurrentYear().intValue(), documentoVO.getFaseIstanzaRiesame());
                          if(Validator.isNotEmpty(idIstanaRiesame))
                          {
                            Vector<Long> vIdIstanzaRiesame = new Vector<Long>();
                            vIdIstanzaRiesame.add(idIstanaRiesame);
                            documentoGaaDAO.annullaIstanzaFromId(vIdIstanzaRiesame, ruoloUtenza.getIdUtente());
                            PlSqlCodeDescription plCode = particellaGaaDAO.annullaIstanzaPlSql(documentoVO.getIdAzienda().longValue(), 
                                DateUtils.extractYearFromDate(documentoVO.getDataInserimento()), documentoVO.getFaseIstanzaRiesame(), 
                                ruoloUtenza.getIdUtente().longValue());
                            
                            if(plCode !=null)
                            {
                              if(Validator.isNotEmpty(plCode.getDescription()))
                              {
                                throw new DataAccessException("Aggiorna documento errore al plsql annullaIstanzaRiesame: "+plCode.getDescription()+"-"
                                    +plCode.getOtherdescription());       
                              }
                            }
                            
                            
                          }
                            
                        }                
                        
                      }
                      
                      
                      //cessazione del documento
                      Vector<DocumentoConduzioneVO> vDocumentoConduzione = documentoDAO
                          .getListDocumentoConduzioni(documentoVO.getIdDocumento(), false);
                      Vector<Long> vIdParticella = null;
                      boolean trovataUnaValida = false;
                      for(int b=0;b<vDocumentoConduzione.size();b++)
                      {
                        if(Validator.isEmpty(vDocumentoConduzione.get(b).getDataFineValidita()))
                        {
                          if(!trovataUnaValida)
                            trovataUnaValida = true;                 
                        }
                        else
                        {
                          if(vIdParticella == null)
                          {
                            vIdParticella = new Vector<Long>();
                          }
                          if(!vIdParticella.contains(vDocumentoConduzione.get(b).getIdParticella()))
                          {
                            vIdParticella.add(vDocumentoConduzione.get(b).getIdParticella());
                          }
                        }                
                      }
                      //non ho trovato conduzioni attive sul documento
                      if(!trovataUnaValida)
                      {
                        Long idStatoDocumento = null;
                        if(documentoGaaDAO.isAllIstanzaAnnullata(anagAziendaVO.getIdAzienda(), vIdParticella, 
                            DateUtils.getCurrentYear().intValue(), documentoVO.getFaseIstanzaRiesame()))
                        {
                          idStatoDocumento = new Long(1);
                        }               
                        documentoDAO.updateDocumentoIstanza(documentoVO.getIdDocumento(), idStatoDocumento);
                        
                      }
                      
                    }
                    
                    
                    
                  }
                  
                  
                  
                  
                  
                  
                  
                  
                }
              }
            }
            // Se per la particella selezionata sono stati specificati gli
            // usi del suolo
            if (oldElencoUtilizzi != null && oldElencoUtilizzi.length > 0)
            {
              // Elimino tutti i records dalla tabella DB_UTILIZZO_CONSOCIATO
              // legati alla conduzione in esame
              for (int b = 0; b < oldElencoUtilizzi.length; b++)
              {
                UtilizzoParticellaVO utilizzoParticellaVO = (UtilizzoParticellaVO) oldElencoUtilizzi[b];
                utilizzoConsociatoDAO
                    .deleteUtilizzoConsociatoByIdUtilizzoParticella(utilizzoParticellaVO
                        .getIdUtilizzoParticella());
              }
              // Elimino tutti i records dalla tabella DB_UTILIZZO_PARTICELLA
              // legati alla conduzione in esame
              utilizzoParticellaDAO
                  .deleteUtilizzoParticellaByIdConduzioneParticella(conduzioneParticellaVO
                      .getIdConduzioneParticella());
            }
          }
          // Se invece è presente in una dichiarazione di consistenza...
          else
          {
            // ... Cesso il record su DB_CONDUZIONE_PARTICELLA
            oldConduzioneParticella.setDataFineConduzione(new java.util.Date(
                new Timestamp(System.currentTimeMillis()).getTime()));
            conduzioneParticellaDAO
                .updateConduzioneParticella(oldConduzioneParticella);
            // Inserisco il nuovo record su DB_CONDUZIONE_PARTICELLA
            conduzioneParticellaVO.setFlagUtilizzoParte(null);
            conduzioneParticellaVO.setNote(null);
            conduzioneParticellaVO.setDataInizioConduzione(new java.util.Date(
                System.currentTimeMillis()));
            conduzioneParticellaVO.setDataAggiornamento(new java.util.Date(
                new Timestamp(System.currentTimeMillis()).getTime()));
            conduzioneParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
                .getIdUtente());
            conduzioneParticellaVO.setRecordModificato(SolmrConstants.FLAG_S);
            conduzioneParticellaVO.setEsitoControllo(null);
            conduzioneParticellaVO.setDataEsecuzione(null);
            // Inserisco un nuovo record su DB_CONDUZIONE_PARTICELLA
            Long idConduzioneParticella = conduzioneParticellaDAO
                .insertConduzioneParticella(conduzioneParticellaVO);
            conduzioneParticellaVO
                .setIdConduzioneParticella(idConduzioneParticella);
            // Recupero i documenti attivi legati alla conduzione storicizzata
            Vector<DocumentoVO> elencoDocumenti = documentoDAO
                .getListDocumentiByIdConduzioneParticella(
                    oldConduzioneParticella.getIdConduzioneParticella(), true);
            // Se ce ne sono ...
            if (elencoDocumenti != null && elencoDocumenti.size() > 0)
            {
              Vector<DocumentoConduzioneVO> elencoConduzioni = null;
              for (int c = 0; c < elencoDocumenti.size(); c++)
              {
                DocumentoVO documentoVO = (DocumentoVO) elencoDocumenti
                    .elementAt(c);
                // Ricerco le conduzioni attive legate al precedente documento
                elencoConduzioni = documentoDAO
                    .getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella(
                        documentoVO.getIdDocumento(), oldConduzioneParticella
                            .getIdConduzioneParticella(), true);
                // Se ne trovo
                if (elencoConduzioni != null && elencoConduzioni.size() > 0)
                {
                  for (int d = 0; d < elencoConduzioni.size(); d++)
                  {
                    DocumentoConduzioneVO documentoConduzioneVO = (DocumentoConduzioneVO) elencoConduzioni
                        .elementAt(d);
                    // Storicizzo i records presenti su DB_DOCUMENTO_CONDUZIONE
                    documentoConduzioneVO
                        .setDataFineValidita(new java.util.Date(new Timestamp(
                            System.currentTimeMillis()).getTime()));
                    documentoDAO
                        .updateDocumentoConduzione(documentoConduzioneVO);                   
                    
                    if (oldConduzioneParticella
                        .getIdTitoloPossesso()
                        .compareTo(conduzioneParticellaVO.getIdTitoloPossesso()) == 0)
                    {
                      documentoConduzioneVO.setIdDocumentoConduzione(null);
                      documentoConduzioneVO
                          .setIdConduzioneParticella(conduzioneParticellaVO
                              .getIdConduzioneParticella());
                      documentoConduzioneVO
                          .setDataInserimento(new java.util.Date(new Timestamp(
                              System.currentTimeMillis()).getTime()));
                      documentoConduzioneVO
                          .setDataInizioValidita(new java.util.Date(
                              new Timestamp(System.currentTimeMillis())
                                  .getTime()));
                      documentoConduzioneVO.setDataFineValidita(null);
                      documentoDAO
                          .insertDocumentoConduzione(documentoConduzioneVO);
                    }
                    //E' cambiato il titolo possesso
                    else
                    {
                      // Inserisco i nuovi records su DB_DOCUMENTO_CONDUZIONE solo
                      // sempre se istanza di riesame
                      if(documentoDAO.isDocIstanzaRiesame(documentoVO.getExtIdDocumento().longValue()))
                      {
                        //Se nn è asservimento
                        if((conduzioneParticellaVO.getIdTitoloPossesso().compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) !=0)
                          || ((conduzioneParticellaVO.getIdTitoloPossesso().compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) ==0)
                               && (documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_CONTRO)))                               
                        {                        
                          documentoConduzioneVO.setIdDocumentoConduzione(null);
                          documentoConduzioneVO
                              .setIdConduzioneParticella(conduzioneParticellaVO
                                  .getIdConduzioneParticella());
                          documentoConduzioneVO
                              .setDataInserimento(new java.util.Date(new Timestamp(
                                  System.currentTimeMillis()).getTime()));
                          documentoConduzioneVO
                              .setDataInizioValidita(new java.util.Date(
                                  new Timestamp(System.currentTimeMillis())
                                      .getTime()));
                          documentoConduzioneVO.setDataFineValidita(null);
                          documentoDAO
                              .insertDocumentoConduzione(documentoConduzioneVO);                        
                        }
                      }                     
                    }                  
                    
                    
                  }
                }
                
                
                
                if(conduzioneParticellaVO.getIdTitoloPossesso().compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) == 0)
                {                  
                  if(documentoDAO.isDocIstanzaRiesame(documentoVO.getExtIdDocumento().longValue()))
                  {
                    
                    if(documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_FOTO)
                    {
                      if (elencoConduzioni != null && elencoConduzioni.size() > 0)
                      {
                        if(!documentoGaaDAO.existAltroLegameIstRiesameParticella(
                            anagAziendaVO.getIdAzienda(), oldConduzioneParticella.getIdParticella().longValue(), 
                           DateUtils.getCurrentYear().intValue(), 
                           documentoVO.getExtIdDocumento().longValue()))
                        {                 
                          
                          Long idIstanaRiesame = documentoGaaDAO.getIstRiesameParticellaFaseAnno(
                              anagAziendaVO.getIdAzienda(), oldConduzioneParticella.getIdParticella().longValue(), 
                              DateUtils.getCurrentYear().intValue(), documentoVO.getFaseIstanzaRiesame());
                          if(Validator.isNotEmpty(idIstanaRiesame))
                          {
                            Vector<Long> vIdIstanzaRiesame = new Vector<Long>();
                            vIdIstanzaRiesame.add(idIstanaRiesame);
                            documentoGaaDAO.annullaIstanzaFromId(vIdIstanzaRiesame, ruoloUtenza.getIdUtente());
                            PlSqlCodeDescription plCode = particellaGaaDAO.annullaIstanzaPlSql(documentoVO.getIdAzienda().longValue(), 
                                DateUtils.extractYearFromDate(documentoVO.getDataInserimento()), documentoVO.getFaseIstanzaRiesame(), 
                                ruoloUtenza.getIdUtente().longValue());
                            
                            if(plCode !=null)
                            {
                              if(Validator.isNotEmpty(plCode.getDescription()))
                              {
                                throw new DataAccessException("Aggiorna documento errore al plsql annullaIstanzaRiesame: "+plCode.getDescription()+"-"
                                    +plCode.getOtherdescription());       
                              }
                            }
                            
                            
                          }
                            
                        }                
                        
                      }
                    }
                    
                    
                    //cessazione del documento
                    Vector<DocumentoConduzioneVO> vDocumentoConduzione = documentoDAO
                        .getListDocumentoConduzioni(documentoVO.getIdDocumento(), false);
                    Vector<Long> vIdParticella = null;
                    boolean trovataUnaValida = false;
                    for(int a=0;a<vDocumentoConduzione.size();a++)
                    {
                      if(Validator.isEmpty(vDocumentoConduzione.get(a).getDataFineValidita()))
                      {
                        if(!trovataUnaValida)
                          trovataUnaValida = true;                 
                      }
                      else
                      {
                        if(vIdParticella == null)
                        {
                          vIdParticella = new Vector<Long>();
                        }
                        if(!vIdParticella.contains(vDocumentoConduzione.get(a).getIdParticella()))
                        {
                          vIdParticella.add(vDocumentoConduzione.get(a).getIdParticella());
                        }
                      }                
                    }
                    //non ho trovato conduzioni attive sul documento
                    if(!trovataUnaValida)
                    {
                      Long idStatoDocumento = null;
                      if(documentoGaaDAO.isAllIstanzaAnnullata(anagAziendaVO.getIdAzienda(), vIdParticella, 
                          DateUtils.getCurrentYear().intValue(), documentoVO.getFaseIstanzaRiesame()))
                      {
                        idStatoDocumento = new Long(1);
                      }               
                      documentoDAO.updateDocumentoIstanza(documentoVO.getIdDocumento(), idStatoDocumento);
                      
                    }
                    
                  }
                  
                  
                  
                }
                
             
                
              }
            }
          }
        }
        // Se non sono stati modificati i dati della conduzione...
        else
        {
          // ... aggiorno comunque DB_CONDUZIONE_PARTICELLA perchè verranno
          // modificati gli utilizzi
          conduzioneParticellaVO.setRecordModificato(SolmrConstants.FLAG_S);
          conduzioneParticellaVO.setEsitoControllo(null);
          conduzioneParticellaVO.setDataEsecuzione(null);
          conduzioneParticellaDAO
              .updateConduzioneParticella(conduzioneParticellaVO);
          // Se per la particella selezionata sono stati specificati gli
          // usi del suolo
          if (oldElencoUtilizzi != null && oldElencoUtilizzi.length > 0)
          {
            // Elimino tutti gli utilizzi consociati e l'utilizzo particella
            for (int e = 0; e < oldElencoUtilizzi.length; e++)
            {
              UtilizzoParticellaVO oldUtilizzoParticella = (UtilizzoParticellaVO) oldElencoUtilizzi[e];
              utilizzoConsociatoDAO
                  .deleteUtilizzoConsociatoByIdUtilizzoParticella(oldUtilizzoParticella
                      .getIdUtilizzoParticella());
            }
            utilizzoParticellaDAO
                .deleteUtilizzoParticellaByIdConduzioneParticella(oldConduzioneParticella
                    .getIdConduzioneParticella());
          }
        }
        // DB_UTILIZZO_PARTICELLA
        UtilizzoParticellaVO[] elencoUtilizzi = storicoParticellaVO
            .getElencoConduzioni()[0].getElencoUtilizzi();
        for (int f = 0; f < elencoUtilizzi.length; f++)
        {
          UtilizzoParticellaVO utilizzoParticellaVO = (UtilizzoParticellaVO) elencoUtilizzi[f];
          // Se è stato valorizzato l'uso primario dell'utilizzo allora...
          if (utilizzoParticellaVO.getIdUtilizzo() != null)
          {
            // Inserisco il record su DB_UTILIZZO_PARTICELLA
            utilizzoParticellaVO.setIdUtilizzoParticella(null);
            utilizzoParticellaVO
                .setIdConduzioneParticella(conduzioneParticellaVO
                    .getIdConduzioneParticella());
            utilizzoParticellaVO.setDataAggiornamento(new java.util.Date(
                new Timestamp(System.currentTimeMillis()).getTime()));
            utilizzoParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
                .getIdUtente());
            utilizzoParticellaVO.setAnno(DateUtils.getCurrentYear().toString());
            utilizzoParticellaVO.setNote(null);
            Long idUtilizzoParticella = utilizzoParticellaDAO
                .insertUtilizzoParticella(utilizzoParticellaVO);
            utilizzoParticellaVO.setIdUtilizzoParticella(idUtilizzoParticella);
            // Se sono stati indicati gli utilizzi consociati
            UtilizzoConsociatoVO[] elencoUtilizzoConsociato = utilizzoParticellaVO
                .getElencoUtilizziConsociati();
            if (elencoUtilizzoConsociato != null
                && elencoUtilizzoConsociato.length > 0)
            {
              for (int g = 0; g < elencoUtilizzoConsociato.length; g++)
              {
                UtilizzoConsociatoVO utilizzoConsociatoVO = (UtilizzoConsociatoVO) elencoUtilizzoConsociato[g];
                // ... Li inserisco solo a patto che il numero piante
                // specificato sia > 0
                if (Validator
                    .isNotEmpty(utilizzoConsociatoVO.getNumeroPiante())
                    && !utilizzoConsociatoVO.getNumeroPiante()
                        .equalsIgnoreCase("0"))
                {
                  utilizzoConsociatoVO.setIdUtilizzoConsociato(null);
                  utilizzoConsociatoVO
                      .setIdUtilizzoParticella(utilizzoParticellaVO
                          .getIdUtilizzoParticella());
                  utilizzoConsociatoDAO
                      .insertUtilizzoConsociato(utilizzoConsociatoVO);
                }
              }
            }
          }
        }
      }
     
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      AgriLogger.error(this, "-- DataAccessException in modificaParticelle ="+dae.getMessage());
      throw new Exception(dae.getMessage());
    }
  }


  /**
   * Metodo che mi restituisce l'elenco delle particelle in tutte le sue
   * componenti (DB_STORICO_PARTICELLA, DB_CONDUZIONE_PARTICELLA,
   * DB_UTILIZZO_PARTICELLA) in relazione dei parametri di ricerca impostati, di
   * un criterio di ordinamento e dell'azienda selezionata utilizzato per il
   * brogliaccio del nuovo territoriale
   * 
   * @param filtriParticellareRicercaVO
   * @param idAzienda
   * @return java.util.Vector
   * @throws Exception
   */
  public Vector<AnagParticellaExcelVO> searchParticelleExcelByParameters(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda)
      throws Exception
  {
    try
    {
      if (filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() <= 0)
      {
        return conduzioneParticellaDAO.searchParticelleExcelByParameters(
            filtriParticellareRicercaVO, idAzienda);
      }
      else
      {
        return conduzioneDichiarataDAO
            .searchParticelleDichiarateExcelByParameters(
                filtriParticellareRicercaVO, idAzienda);
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per recuperare l'elenco delle particelle: se il boolean è true le
   * estraggo solo in relazione ai filtri della chiave logica della particella,
   * in caso contrario estraggo solo le particelle la cui conduzione non risulti
   * essere legata già ai documenti inseriti sull'azienda selezionata
   * 
   * @param storicoParticellaVO
   * @param anagAziendaVO
   * @param hasUnitToDocument
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] getListParticelleForDocument(
      StoricoParticellaVO storicoParticellaVO, AnagAziendaVO anagAziendaVO,
      boolean hasUnitToDocument, Long idAnomalia, String orderBy) throws Exception
  {
    try
    {
      if(idAnomalia != null)
      {
        boolean trovato = dichiarazioneSegnalazioneDAO
          .isSegnalazioneForDocumentoParticella(anagAziendaVO.getIdAzienda());
        
        if(trovato)
        {
          return storicoParticellaDAO.getListParticelleForDocument(
              storicoParticellaVO, anagAziendaVO, hasUnitToDocument, idAnomalia, orderBy);
        }
        else
        {
          boolean trovatoPartAttive = conduzioneParticellaDAO
            .isConduzionePopParticelleFromEsitoControllo(anagAziendaVO.getIdAzienda());
          if(trovatoPartAttive)
          {
            long idDichiarazioneConsistenza = consistenzaDAO
              .getLastIdDichiazioneConsistenza(anagAziendaVO.getIdAzienda(), null);
            
            if(idDichiarazioneConsistenza != 0)
            {
              ConsistenzaVO consistenzaVO = consistenzaDAO.findDichiarazioneConsistenzaByPrimaryKey(idDichiarazioneConsistenza);
              return storicoParticellaDAO.getListParticelleForDocument(
                  storicoParticellaVO, anagAziendaVO, consistenzaVO, hasUnitToDocument, 
                  idAnomalia, orderBy);
            }
          }
        }
      }
      
      
      
      return storicoParticellaDAO.getListParticelleForDocument(
          storicoParticellaVO, anagAziendaVO, hasUnitToDocument, idAnomalia, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public StoricoParticellaVO[] getListParticelleForDocument(
      StoricoParticellaVO storicoParticellaVO, AnagAziendaVO anagAziendaVO,
      long idDichiarazioneConsistenza, boolean hasUnitToDocument, Long idAnomalia, String orderBy) throws Exception
  {
    try
    {
      ConsistenzaVO consistenzaVO = consistenzaDAO.findDichiarazioneConsistenzaByPrimaryKey(idDichiarazioneConsistenza);
      return storicoParticellaDAO.getListParticelleForDocument(
          storicoParticellaVO, anagAziendaVO, consistenzaVO, hasUnitToDocument, 
          idAnomalia, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public StoricoParticellaVO[] getListParticelleForDocumentValoreC( 
      AnagAziendaVO anagAziendaVO, int anno, boolean hasUnitToDocument)  throws Exception
  {
    try
    {
      return storicoParticellaDAO.getListParticelleForDocumentValoreC(
          anagAziendaVO, anno, hasUnitToDocument);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public StoricoParticellaVO[] getListParticelleForDocumentExtraSistema( 
      AnagAziendaVO anagAziendaVO, int anno, boolean hasUnitToDocument) 
  throws Exception
  {
    try
    {
      return storicoParticellaDAO.getListParticelleForDocumentExtraSistema(
          anagAziendaVO, anno, hasUnitToDocument);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  
  public Vector<StoricoParticellaVO> getListParticelleUvBasic(
      FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO, AnagAziendaVO anagAziendaVO) throws Exception
  {
    try
    {
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento())
        && (filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() > 0))
      {
        return unitaArboreaDichiarataDAO.getElencoStoricoUnitaArboreaDichiarataBasic(
            anagAziendaVO.getIdAzienda().longValue(), filtriUnitaArboreaRicercaVO);
      }
      else
      {
        return storicoUnitaArboreaDAO.getElencoStoricoUnitaArboreaBasic(
            anagAziendaVO.getIdAzienda().longValue(), filtriUnitaArboreaRicercaVO);
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce il record su DB_STORICO_PARTICELLA a partire
   * dalla chiave logica della particella(COMUNE, SEZIONE, FOGLIO, PARTICELLA,
   * SUBALTERNO) Se specifico la data inizio validita ricerco il record valido a
   * quella data, in caso contrario ricerco il record attivo. Metodo utilizzato
   * da SMRGAASV
   * 
   * @param istatComune
   * @param sezione
   * @param foglio
   * @param particella
   * @param subalterno
   * @param dataInizioValidita
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO
   * @throws Exception
   */
  public StoricoParticellaVO findStoricoParticellaVOByParameters(
      String istatComune, String sezione, String foglio, String particella,
      String subalterno, java.util.Date dataInizioValidita)
      throws Exception
  {
    try
    {
      return storicoParticellaDAO.findStoricoParticellaVOByParameters(
          istatComune, sezione, foglio, particella, subalterno,
          dataInizioValidita);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi permette di recuperare la sezione a partire dai parametri
   * 
   * @param istatComune
   * @param sezione
   * @return it.csi.solmr.dto.anag.terreni.SezioneVO
   * @throws Exception
   */
  public SezioneVO getSezioneByParameters(String istatComune, String sezione)
      throws Exception
  {
    try
    {
      return commonDAO.getSezioneByParameters(istatComune, sezione);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce il foglio a partire dai parametri che
   * rappresentano la sua chiave logica
   * 
   * @param istatComune
   * @param foglio
   * @param sezione
   * @return it.csi.solmr.dto.anag.FoglioVO
   * @throws Exception
   */
  public FoglioVO findFoglioByParameters(String istatComune, String foglio,
      String sezione) throws Exception
  {
    try
    {
      return foglioDAO.findFoglioByParameters(istatComune, foglio, sezione);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi permette di recuperare l'elenco dei fogli a partire dai
   * parametri
   * 
   * @param istatComune
   * @param sezione
   * @param foglio
   * @return it.csi.solmr.dto.anag.FoglioVO[]
   * @throws Exception
   */
  public FoglioVO[] getFogliByParameters(String istatComune, String sezione,
      String foglio) throws Exception
  {
    try
    {
      return foglioDAO.getFogliByParameters(istatComune, sezione, foglio);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce l'elenco dei records su DB_STORICO_PARTICELLA a
   * partire dalla chiave logica della particella(COMUNE, SEZIONE, FOGLIO,
   * PARTICELLA, SUBALTERNO) Se ricerco per onlyActive == true l'elenco dovrà
   * contenere un solo elemento altrimenti avrò tutte le fotografie dello
   * storico particella
   * 
   * @param istatComune
   * @param sezione
   * @param foglio
   * @param particella
   * @param subalterno
   * @param onlyActive
   * @param orderBy[]
   * @param idAzienda
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   * @throws SolmrException
   */
  public StoricoParticellaVO[] getListStoricoParticellaVOByParameters(
      String istatComune, String sezione, String foglio, String particella,
      String subalterno, boolean onlyActive, String orderBy, Long idAzienda)
      throws Exception, SolmrException
  {
    try
    {
      return storicoParticellaDAO.getListStoricoParticellaVOByParameters(
          istatComune, sezione, foglio, particella, subalterno, onlyActive,
          orderBy, idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  
  /**
   * Metodo che mi restituisce l'elenco dei records su DB_STORICO_PARTICELLA a partire
   * dalla chiave logica della particella(COMUNE, SEZIONE, FOGLIO, PARTICELLA, SUBALTERNO)
   * Se ricerco per onlyActive == true l'elenco dovrà contenere un solo elemento
   * altrimenti avrò tutte le fotografie dello storico particella
   * NB
   * Sono esclusi i titoli di possesso asservimento (5) e conferimento (6)
   * 
   * @param istatComune
   * @param sezione
   * @param foglio
   * @param particella
   * @param subalterno
   * @param idAzienda
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws DataAccessException
   * @throws SolmrException
   */
  public StoricoParticellaVO[] getListStoricoParticellaVOByParametersImpUnar(String istatComune, 
      String sezione, String foglio, String particella, String subalterno, long idAzienda)
      throws Exception, SolmrException
  {
    try
    {
      return storicoParticellaDAO.getListStoricoParticellaVOByParametersImpUnar(istatComune, 
          sezione, foglio, particella, subalterno, idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce il record di DB_STORICO_PARTICELLA a partire
   * dalla sua chiave primaria
   * 
   * @param idConduzioneParticella
   * @return it.csi.solmr.dto.StoricoParticellaVO
   * @throws Exception
   */
  public StoricoParticellaVO findCurrStoricoParticellaByIdParticella(
      Long idParticella) throws Exception
  {
    try
    {
      return storicoParticellaDAO
          .findCurrStoricoParticellaByIdParticella(idParticella);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  /**
   * Metodo che mi restituisce il record di DB_STORICO_PARTICELLA a partire
   * dall'idParticella
   * 
   * @param idParticella
   * @return it.csi.solmr.dto.StoricoParticellaVO
   * @throws Exception
   */
  public StoricoParticellaVO findStoricoParticellaByPrimaryKey(
      Long idStoricoParticella) throws Exception
  {
    
    StoricoParticellaVO storicoParticellaVO = null;
    
    try
    {
      storicoParticellaVO = storicoParticellaDAO
          .findStoricoParticellaByPrimaryKey(idStoricoParticella);
      
      return storicoParticellaVO;
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi permette di inserire una particella territoriale
   * dall'azienda
   * 
   * @param storicoParticellaVO
   * @param ruoloUtenza
   * @param elencoParticelleEvento
   * @param idEvento
   * @param idAzienda
   * @return java.lang.String
   * @throws Exception
   */
  public String inserisciParticella(StoricoParticellaVO storicoParticellaVO,
      RuoloUtenza ruoloUtenza, StoricoParticellaVO[] elencoParticelleEvento,
      Long idEvento, Long idAzienda, String[] arrVIdStoricoUnitaArborea, String[] arrVAreaUV) throws Exception
  {
    String result=null;
    try
    {
      
      
      //ricerco in caso di accorpamento o franzionamento se la nuova particella è identica al padre
      boolean flagPartUgualePadre = false;
      if (idEvento != null && idEvento.longValue() != 0)
      {
        for (int i = 0; i < elencoParticelleEvento.length; i++)
        {
          StoricoParticellaVO storicoParticellaEvento = (StoricoParticellaVO) elencoParticelleEvento[i];
          if(storicoParticellaEvento.equalsKey(storicoParticellaVO)) 
          {
            flagPartUgualePadre = true;
            break;
          }
        }
      }
      
      
      
      Long idParticella = null;
      //Long idParticellaCertificata = null;
      
      
      
      //se la particella nuova è già censita su DB_STORICO_PARTICELLA
      StoricoParticellaVO oldStoricoParticellaVO = null;
      
      // Se la particella non è censita su DB_STORICO_PARTICELLA
      if (storicoParticellaVO.getIdStoricoParticella() == null)
      {
        // Inserisco un record su DB_PARTICELLA
        ParticellaVO particellaVO = new ParticellaVO();
        particellaVO.setDataCreazione(new java.util.Date(new Timestamp(System
            .currentTimeMillis()).getTime()));
        particellaVO.setDataInizioValidita(new java.util.Date(System
            .currentTimeMillis()));
        idParticella = particellaDAO.insertParticella(particellaVO);
        // Inserisco un record sulla tabella DB_STORICO_PARTICELLA
        storicoParticellaVO.setIdParticella(idParticella);
        storicoParticellaVO.setDataInizioValidita(new java.util.Date(System
            .currentTimeMillis()));
        storicoParticellaVO.setDataAggiornamento(new java.util.Date(
            new Timestamp(System.currentTimeMillis()).getTime()));
        storicoParticellaVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
        storicoParticellaDAO
            .insertStoricoParticella(storicoParticellaVO);
        // Se era comunque censita su una delle fonti ufficiali
        if (storicoParticellaVO.getParticellaCertificataVO() != null)
        {
          // Creo il legame con DB_PARTICELLA_CERTIFICATA
          ParticellaCertificataVO particellaCertificataVO = storicoParticellaVO
              .getParticellaCertificataVO();
          particellaCertificataVO.setIdParticella(idParticella);
          //idParticellaCertificata = particellaCertificataVO.getIdParticellaCertificata();
          particellaCertificataDAO
              .allineaParticellaCertificata(particellaCertificataVO);
        }
        
        
        //devo aggiungere i tipi area....perchè non censita
        if(storicoParticellaVO.getvValoriTipoArea() != null)
        {
          for(int i=0;i<storicoParticellaVO.getvValoriTipoArea().size();i++)
          {
            TipoAreaVO tipoAreaVO = storicoParticellaVO.getvValoriTipoArea().get(i);
            TipoValoreAreaVO tipoAreaValoreVO = tipoAreaVO.getvTipoValoreArea().get(0);
            if(Validator.isNotEmpty(tipoAreaValoreVO.getValore())
              && !"S".equalsIgnoreCase(tipoAreaVO.getFlagEsclusivoFoglio()))
            {
              Long idTipoValoreArea = particellaGaaDAO.getIdTipoValoreArea(tipoAreaVO.getIdTipoArea(), 
                tipoAreaValoreVO.getValore());
              particellaGaaDAO.insertRParticellaArea(idParticella, idTipoValoreArea);
            }
            /*if("S".equalsIgnoreCase(tipoAreaVO.getFlagAreaModificabile()))
            { 
              Long idTipoValoreArea = particellaGaaDAO.getIdTipoValoreArea(tipoAreaVO.getIdTipoArea(), 
                  tipoAreaValoreVO.getValore());
              particellaGaaDAO.insertRParticellaArea(idParticella, idTipoValoreArea);
            }*/
            
          }          
        }
        
      }
      // Se invece è censita
      else
      {
        oldStoricoParticellaVO = storicoParticellaDAO
            .findStoricoParticellaByPrimaryKey(storicoParticellaVO
                .getIdStoricoParticella());
        
        // Setto i parametri in questo modo così da permettere al metodo equals
        // di funzionare in modo corretto
        //oldStoricoParticellaVO.setElencoConduzioni(storicoParticellaVO
            //.getElencoConduzioni());
        //oldStoricoParticellaVO
            //.setElencoConduzioniDichiarate(storicoParticellaVO
                //.getElencoConduzioniDichiarate());
        //oldStoricoParticellaVO.setDocumentoVO(storicoParticellaVO
            //.getDocumentoVO());
        //oldStoricoParticellaVO.setFoglioVO(storicoParticellaVO.getFoglioVO());
        idParticella = oldStoricoParticellaVO.getIdParticella();
        
        
        oldStoricoParticellaVO.setSupCatastale(StringUtils
            .parseSuperficieField(oldStoricoParticellaVO.getSupCatastale()));      
        storicoParticellaVO.setSupCatastale(StringUtils
            .parseSuperficieField(storicoParticellaVO.getSupCatastale()));
        
        
        ParticellaCertificataVO particellaCertificataVO = particellaCertificataDAO
            .findParticellaCertificataByIdParticella(idParticella, null);
        Date dataSoppressione = null;
        if(Validator.isNotEmpty(particellaCertificataVO))
        {
          //idParticellaCertificata = particellaCertificataVO.getIdParticellaCertificata();
          dataSoppressione = particellaCertificataVO.getDataSoppressione();
        }
        ParticellaVO particellaVO = particellaDAO.findParticellaByPrimaryKey(idParticella);
        if(Validator.isNotEmpty(particellaVO.getDataCessazione())
             && Validator.isEmpty(dataSoppressione))
        {
          particellaDAO.attivaParticella(idParticella);            
        }
        
        
        
        // Se sono stati modificati i dati territoriali
        if(!storicoParticellaVO.equalsDatiTerritorialiInserimento(oldStoricoParticellaVO)
          || flagPartUgualePadre)
        {
          // Storicizzo su DB_STORICO_PARTICELLA        
          
          storicoParticellaDAO.storicizzaStoricoParticella(oldStoricoParticellaVO
              .getIdStoricoParticella().longValue());
          // Inserisco il nuovo record su DB_STORICO_PARTICELLA
          storicoParticellaVO.setIdParticella(oldStoricoParticellaVO
              .getIdParticella());
          storicoParticellaVO.setDataInizioValidita(new java.util.Date(System
              .currentTimeMillis()));
          storicoParticellaVO.setDataAggiornamento(new java.util.Date(
              new Timestamp(System.currentTimeMillis()).getTime()));
          storicoParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
              .getIdUtente());          
          if(flagPartUgualePadre)
          {
            storicoParticellaVO.setMotivoModifica("AGGIORNAMENTO IN SEGUITO A FRAZIONAMENTO O ACCORPAMENTO SU SE STESSA");
          }
          
          storicoParticellaDAO
              .insertStoricoParticella(storicoParticellaVO);
          if(flagPartUgualePadre)
          {
            //ParticellaCertificataVO particellaCertificataVO = particellaCertificataDAO
                //.findParticellaCertificataByIdParticella(idParticella, null);
            particellaCertificataDAO.updateParticellaCertificataFrazAcc(particellaCertificataVO);
          
            ConduzioneParticellaVO[] elencoConduzioni = conduzioneParticellaDAO
                .getListConduzioneParticellaByIdAziendaAndIdParticella(idAzienda, idParticella, true, null);
            //storicizzo le vecchie conduzioni
            if(Validator.isNotEmpty(elencoConduzioni) 
              && (elencoConduzioni.length > 0))
            {
              for(int h=0;h<elencoConduzioni.length;h++)
              {
                ConduzioneParticellaVO conduzioneParticellaTmp 
                  = elencoConduzioni[h]; 
                conduzioneParticellaTmp.setNote("CHIUSA PER ACCORPAMENTO " +
                		"O FRAZIONEMENTO");
                conduzioneParticellaDAO
                  .storicizzaConduzioneParticella(conduzioneParticellaTmp, ruoloUtenza.getIdUtente());
              }
            }
            
            
          }
        }
        
        
        //confronto i tipi area e modifico solo quelli cambiati...
        if(storicoParticellaVO.getvValoriTipoArea() != null)
        {
          for(int i=0;i<storicoParticellaVO.getvValoriTipoArea().size();i++)
          {
            TipoAreaVO tipoAreaVO = storicoParticellaVO.getvValoriTipoArea().get(i);
            TipoValoreAreaVO tipoAreaValoreVO = tipoAreaVO.getvTipoValoreArea().get(0);
            if(Validator.isNotEmpty(tipoAreaValoreVO.getValore())
                && !"S".equalsIgnoreCase(tipoAreaVO.getFlagEsclusivoFoglio()))
            { 
              //qui devo confrontare......
              String valore = particellaGaaDAO.getValoreAttivoTipoAreaFromParticellaAndId(storicoParticellaVO.getIdParticella(),
                  tipoAreaVO.getIdTipoArea());
              if(Validator.isNotEmpty(valore))
              {
                //se lo trovo su db ma sono diversi....
                if(Validator.isNotEmpty(tipoAreaValoreVO.getValore()) && !valore.equalsIgnoreCase(tipoAreaValoreVO.getValore()))
                {
                  //storicizzo
                  particellaGaaDAO.storicizzaRParticellaArea(storicoParticellaVO.getIdParticella(), 
                      tipoAreaValoreVO.getIdTipoValoreArea());
                  
                  //inserisco
                  Long idTipoValoreArea = particellaGaaDAO.getIdTipoValoreArea(tipoAreaVO.getIdTipoArea(), 
                      tipoAreaValoreVO.getValore());
                  particellaGaaDAO.insertRParticellaArea(idParticella, idTipoValoreArea);
                }
              }
              //non esiste su db inserisco
              else
              {
                Long idTipoValoreArea = particellaGaaDAO.getIdTipoValoreArea(tipoAreaVO.getIdTipoArea(), 
                    tipoAreaValoreVO.getValore());
                particellaGaaDAO.insertRParticellaArea(idParticella, idTipoValoreArea);
              }              
            }
            
          }          
        }
        
        
      }
        
        
          
      long idTitoloPos=-1;
      if (storicoParticellaVO.getElencoConduzioni()[0].getIdTitoloPossesso()!=null)
      {
      	idTitoloPos=storicoParticellaVO.getElencoConduzioni()[0].getIdTitoloPossesso();
      }
      //lavoro sulle uv solo se titolo possesso diverso da asservimento e conferimento
      // e se esistono dell uv da importare selezionate
      if (idTitoloPos!=5 && idTitoloPos!=6)
      {
        if(Validator.isNotEmpty(arrVIdStoricoUnitaArborea))
        {
          StoricoUnitaArboreaVO[] elencoUnitaArboree = null;
          long[] idStoricoUVLg = new long[arrVIdStoricoUnitaArborea.length];
          for(int j=0;j<arrVIdStoricoUnitaArborea.length;j++)
          {
            idStoricoUVLg[j] = new Long(arrVIdStoricoUnitaArborea[j]).longValue();
          }
          elencoUnitaArboree = storicoUnitaArboreaDAO
            .getListStoricoUnitaArboreaByVidSoricoUnitaArborea(idStoricoUVLg);         
          
          Long idGenereIscrizioneDefault = tipoGenereIscrizioneDAO.getDefaultIdGenereIscrizione();
          Long idGenereIscrizioneProvvisorio = tipoGenereIscrizioneDAO.getIdGenereIscrizioneByFlag("N");         
            
          // Le modifico impostando come id_azienda quello dell'azienda agricola
          // sulla quale sto lavorando
          String progrUnar=null;
          for (int i = 0; i < elencoUnitaArboree.length; i++)
          {                         
            StoricoUnitaArboreaVO storicoUnitaArboreaVO = (StoricoUnitaArboreaVO) elencoUnitaArboree[i];
            Long idUnitaArboreaMadre = storicoUnitaArboreaVO.getIdUnitaArborea();
            boolean primoCasoUpadate = false;
            //se l'uv è presente sulla particella ma non è associata all'azienda....
            //caso in cui ovviamente la particella è già censita
            if(Validator.isNotEmpty(oldStoricoParticellaVO))
            {
              if(Validator.isEmpty(storicoUnitaArboreaVO.getIdAzienda())
                && Validator.isNotEmpty(storicoUnitaArboreaVO.getIdParticella())
                && (storicoUnitaArboreaVO.getIdParticella().compareTo(oldStoricoParticellaVO.getIdParticella()) == 0))
              {
                primoCasoUpadate = true;
              }
            }
              
                        
            //Campi comuni
            storicoUnitaArboreaVO.setIdAzienda(idAzienda);
            if(Validator.isEmpty(storicoUnitaArboreaVO.getStatoUnitaArborea()))
            {
              if (ruoloUtenza.isUtenteIntermediario() || ruoloUtenza.isUtenteOPRGestore())
              {
                storicoUnitaArboreaVO.setStatoUnitaArborea(SolmrConstants.STATO_UV_PROPOSTO_CAA);
              }
              else
              {
                if (ruoloUtenza.isUtenteRegionale() || ruoloUtenza.isUtenteProvinciale())
                {
                  storicoUnitaArboreaVO.setStatoUnitaArborea(SolmrConstants.STATO_UV_VALIDATO_PA);
                }
              }
            }
              
            if(ruoloUtenza.isUtentePA())
            {
              storicoUnitaArboreaVO.setIdGenereIscrizione(idGenereIscrizioneDefault);
            }
            else
            {
              storicoUnitaArboreaVO.setIdGenereIscrizione(idGenereIscrizioneProvvisorio);
            }
              
            if(Validator.isEmpty(storicoUnitaArboreaVO.getDataImpianto())
              && Validator.isNotEmpty(storicoUnitaArboreaVO.getAnnoImpianto())
              && (new Integer(storicoUnitaArboreaVO.getAnnoImpianto()).intValue() >0))
            {
              Date dataImpianto = DateUtils.parseDate("01/08/"+storicoUnitaArboreaVO.getAnnoImpianto());
              storicoUnitaArboreaVO.setDataImpianto(dataImpianto);
            }
              
            if(Validator.isEmpty(storicoUnitaArboreaVO.getDataPrimaProduzione())
              && Validator.isNotEmpty(storicoUnitaArboreaVO.getAnnoPrimaProduzione())
              && (new Integer(storicoUnitaArboreaVO.getAnnoPrimaProduzione()).intValue() >0))
            {
              Date dataPrimaProduzione = DateUtils.parseDate("01/08/"+storicoUnitaArboreaVO.getAnnoPrimaProduzione());
              storicoUnitaArboreaVO.setDataPrimaProduzione(dataPrimaProduzione);
            }     
              
            // Calcolo il progr_unar
            if (i == 0)
            {
              progrUnar = storicoUnitaArboreaDAO.getProgressivoUnar(idAzienda);
            }
            else
            {
              progrUnar = new BigDecimal(Integer.decode(progrUnar).intValue() + 1).toString();
            }
              
            storicoUnitaArboreaVO.setProgrUnar(progrUnar);
            storicoUnitaArboreaVO.setDataAggiornamento(new Date());
            storicoUnitaArboreaVO.setRecordModificato("S");
            storicoUnitaArboreaVO.setDataEsecuzione(null);
            storicoUnitaArboreaVO.setEsitoControllo(null);
            storicoUnitaArboreaVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
            storicoUnitaArboreaVO.setDataCessazione(null); 
            storicoUnitaArboreaVO.setDataEsecuzione(null);
            
            //l'uv è già associata alla particella.....la fisso all'azienda
            if(primoCasoUpadate && (idEvento.compareTo(SolmrConstants.INSERIMENTO_FRAZIONAMENTO) != 0))
            {            
              storicoUnitaArboreaDAO.updateStoricoUnitaArborea(storicoUnitaArboreaVO);
            }
            else
            { 
              boolean flagStoricizza = true;
              boolean flagUpdate = false;
              if(idEvento.compareTo(SolmrConstants.INSERIMENTO_FRAZIONAMENTO) == 0)
              {
                BigDecimal areaBd = new BigDecimal(storicoUnitaArboreaVO.getArea().replace(",", "."));
                BigDecimal areaNuovaBd = new BigDecimal(arrVAreaUV[i].replace(",", "."));
                if((storicoParticellaVO.getIdParticella().compareTo(storicoUnitaArboreaVO.getIdParticella()) == 0)
                  && (areaBd.compareTo(areaNuovaBd) == 0))
                {
                  flagStoricizza = false;
                }
                else if((storicoParticellaVO.getIdParticella().compareTo(storicoUnitaArboreaVO.getIdParticella()) == 0)
                  && (areaBd.compareTo(areaNuovaBd) != 0))
                {                  
                  flagUpdate = true;
                  flagStoricizza = false;
                }
              }
              else if(idEvento.compareTo(SolmrConstants.INSERIMENTO_ACCORPAMENTO) == 0)
              {
                if(storicoParticellaVO.getIdParticella().compareTo(storicoUnitaArboreaVO.getIdParticella()) == 0)
                {
                  flagStoricizza = false;
                }
              }
              
              //non creo un nuovo idUnitaArborea
              if(flagUpdate)
              {
                AltroVitignoVO[] elencoAltroVitignoVO = altroVitignoDAO
                    .getListAltroVitignoByIdStoricoUnitaArborea(storicoUnitaArboreaVO
                        .getIdStoricoUnitaArborea(), null);              
                StoricoUnitaArboreaVO storicoUnitaArboreaUpdVO = storicoUnitaArboreaDAO.findStoricoUnitaArborea(storicoUnitaArboreaVO
                    .getIdStoricoUnitaArborea().longValue());
                storicoUnitaArboreaDAO.storicizzaStoricoUnitaArboreaDataAggiornamento(storicoUnitaArboreaVO
                  .getIdStoricoUnitaArborea().longValue(), ruoloUtenza.getIdUtente());                
                
                
                storicoUnitaArboreaUpdVO.setArea(arrVAreaUV[i]);
                storicoUnitaArboreaUpdVO.setIdUnitaArboreaMadre(idUnitaArboreaMadre);                
                storicoUnitaArboreaUpdVO.setNote("Area modificata in seguito a frazionamento della particella su se stessa");
                
                if(Validator.isNotEmpty(storicoUnitaArboreaUpdVO.getSuperficieDaIscrivereAlbo()))
                {
                  BigDecimal supIscr = new BigDecimal(storicoUnitaArboreaUpdVO.getSuperficieDaIscrivereAlbo().replace(",", "."));  
                  if(supIscr.compareTo(new BigDecimal(0)) > 0)
                  {
                    storicoUnitaArboreaUpdVO.setSuperficieDaIscrivereAlbo(storicoUnitaArboreaUpdVO.getArea());
                  }
                }
                
                
                //storicoUnitaArboreaUpdVO.setIdParticella(idParticella);
                storicoUnitaArboreaUpdVO.setDataInizioValidita(new Date());
                storicoUnitaArboreaUpdVO.setDataFineValidita(null);
                storicoUnitaArboreaUpdVO.setDataAggiornamento(new Date());
                storicoUnitaArboreaUpdVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
                storicoUnitaArboreaUpdVO.setDataEsecuzione(null);
                Long idStoricoUnitaArboreaNew = storicoUnitaArboreaDAO.insertStoricoUnitaArborea(storicoUnitaArboreaUpdVO);
                  
                //Aggiungo altri vitigni
                if(Validator.isNotEmpty(elencoAltroVitignoVO)
                    && (elencoAltroVitignoVO.length > 0))
                {
                  for(int j=0;j<elencoAltroVitignoVO.length;j++)
                  {
                    elencoAltroVitignoVO[j].setIdStoricoUnitaArborea(idStoricoUnitaArboreaNew);
                    altroVitignoDAO.insertAltroVitigno(elencoAltroVitignoVO[j]);                  
                  }               
                }
                
                      
              }
              
              if(flagStoricizza)
              {
                AltroVitignoVO[] elencoAltroVitignoVO = altroVitignoDAO
                    .getListAltroVitignoByIdStoricoUnitaArborea(storicoUnitaArboreaVO
                        .getIdStoricoUnitaArborea(), null);              
                storicoUnitaArboreaDAO.storicizzaStoricoUnitaArboreaDataAggiornamento(storicoUnitaArboreaVO
                  .getIdStoricoUnitaArborea().longValue(), ruoloUtenza.getIdUtente());
                
                UnitaArboreaVO unitaArboreaVO = new UnitaArboreaVO();
                unitaArboreaVO.setDataInizioValidita(new java.util.Date(new Timestamp(
                    System.currentTimeMillis()).getTime()));
                Long idUnitaArborea = unitaArboreaDAO.insertUnitaArborea(unitaArboreaVO);              
                storicoUnitaArboreaVO.setIdUnitaArborea(idUnitaArborea);
                
                
                if(idEvento.compareTo(SolmrConstants.INSERIMENTO_FRAZIONAMENTO) == 0)
                {
                  storicoUnitaArboreaVO.setArea(arrVAreaUV[i]);
                  storicoUnitaArboreaVO.setIdUnitaArboreaMadre(idUnitaArboreaMadre);                
                }
                
                if(Validator.isNotEmpty(storicoUnitaArboreaVO.getSuperficieDaIscrivereAlbo()))
                {
                  BigDecimal supIscr = new BigDecimal(storicoUnitaArboreaVO.getSuperficieDaIscrivereAlbo().replace(",", "."));  
                  if(supIscr.compareTo(new BigDecimal(0)) > 0)
                  {
                    storicoUnitaArboreaVO.setSuperficieDaIscrivereAlbo(storicoUnitaArboreaVO.getArea());
                  }
                }
                
                
                storicoUnitaArboreaVO.setNote("Inserimento nuova particella");
                storicoUnitaArboreaVO.setIdParticella(idParticella);
                storicoUnitaArboreaVO.setDataInizioValidita(new Date());
                storicoUnitaArboreaVO.setDataFineValidita(null);             
                Long idStoricoUnitaArboreaNew = storicoUnitaArboreaDAO.insertStoricoUnitaArborea(storicoUnitaArboreaVO);
                  
                //Aggiungo altri vitigni
                if(Validator.isNotEmpty(elencoAltroVitignoVO)
                    && (elencoAltroVitignoVO.length > 0))
                {
                  for(int j=0;j<elencoAltroVitignoVO.length;j++)
                  {
                    elencoAltroVitignoVO[j].setIdStoricoUnitaArborea(idStoricoUnitaArboreaNew);
                    altroVitignoDAO.insertAltroVitigno(elencoAltroVitignoVO[j]);                  
                  }               
                }
                      
              }
            }
          }
          
          
          // Aggiorno lo stato dello schedario delle particelle su cui insistevano
          // UV selezionate
          BigDecimal[] particella=new BigDecimal[1];
          particella[0]=new BigDecimal(idParticella);          
          particellaDAO.aggiornaSchedarioParticellaPlSql(particella);
                
        } //if uv selezionate
                  
      } //if titolo possesso  
      
      
      // Se l'utente ha specificato un evento di creazione della particella
      //diverso da nuova particella
      Vector<Long> vIdMenzionegeografica = null;
      if (idEvento != null && idEvento.longValue() != 0)
      {
        String cessaParticella = tipoEventoDAO.getFlagEventoCessaParticella(idEvento.longValue());
        // Per tutte le particelle indicate...
        for (int i = 0; i < elencoParticelleEvento.length; i++)
        {
          StoricoParticellaVO storicoParticellaEvento = (StoricoParticellaVO) elencoParticelleEvento[i];                    
          //enro solo se flag è D o S oppure sono diverso dalla particella che sto inserendo...
          if(("S".equalsIgnoreCase(cessaParticella) || "D".equalsIgnoreCase(cessaParticella))
              && !storicoParticellaEvento.equalsKey(storicoParticellaVO))
          {
            Vector<MenzioneParticellaVO> vMenzioneParticella = tipoMenzioneGeograficaDAO
                .getVMenzioneParticellaAttiva(storicoParticellaEvento.getIdParticella().longValue());
            if(vMenzioneParticella != null)
            {
              for(int j=0;j<vMenzioneParticella.size();j++)
              {
                if(vIdMenzionegeografica == null)
                {
                  vIdMenzionegeografica = new Vector<Long>();
                }
                
                MenzioneParticellaVO menzioneParticellaVO = vMenzioneParticella.get(j);
                Long idMenzioneParticella = menzioneParticellaVO.getIdMenzioneParticella();
                if(!vIdMenzionegeografica.contains(menzioneParticellaVO.getIdMenzioneGeografica()))
                {
                  vIdMenzionegeografica.add(menzioneParticellaVO.getIdMenzioneGeografica());
                                  
                  menzioneParticellaVO.setIdParticella(idParticella);
                  menzioneParticellaVO.setDataInizioValidita(new Date());
                  tipoMenzioneGeograficaDAO.insertMenzioneParticella(menzioneParticellaVO);
                }
                
                if (storicoParticellaEvento.isCessaParticella())
                {
                  tipoMenzioneGeograficaDAO.storicizzaMenzioneParticella(idMenzioneParticella);                
                }
              }
            }
            
            
            
            if(storicoParticellaEvento.isCessaParticella()
                    || "D".equalsIgnoreCase(cessaParticella))
            {
              if(Validator.isEmpty(storicoParticellaEvento.getDataCessazione()))
              {
                // Le cesso su DB_PARTICELLA
                particellaDAO.cessaParticella(storicoParticellaEvento
                  .getIdParticella());
              }
              
              ConduzioneParticellaVO[] elencoConduzioni = conduzioneParticellaDAO
                  .getListConduzioneParticellaByIdAziendaAndIdParticella(idAzienda, 
                      storicoParticellaEvento.getIdParticella(), true, null);
              //storicizzo le vecchie conduzioni
              if(Validator.isNotEmpty(elencoConduzioni) 
                && (elencoConduzioni.length > 0))
              {
                for(int h=0;h<elencoConduzioni.length;h++)
                {
                  ConduzioneParticellaVO conduzioneParticellaTmp 
                    = elencoConduzioni[h]; 
                  conduzioneParticellaTmp.setNote("CHIUSA PER ACCORPAMENTO " +
                      "O FRAZIONEMENTO");
                  conduzioneParticellaDAO
                    .storicizzaConduzioneParticella(conduzioneParticellaTmp, ruoloUtenza.getIdUtente());
                }
              }
            }
            
          }
          
          
          
          
          
          
          
          
          
          // Inserisco i records su DB_EVENTO_PARTICELLA solo se non esistono
          // già degli
          // eventi associati alla particella che si sta inserendo
          result = eventoParticellaDAO.getEventiParticellaByIdParticellaNuovaAndCessata(idParticella,
                  storicoParticellaEvento.getIdParticella(), idEvento.longValue());
          if (result == null)
          {
            EventoParticellaVO eventoParticellaVO = new EventoParticellaVO();
            eventoParticellaVO.setIdEvento(idEvento);
            eventoParticellaVO.setDataAggiornamento(new java.util.Date(
                new Timestamp(System.currentTimeMillis()).getTime()));
            eventoParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
                .getIdUtente());
            eventoParticellaVO.setIdParticellaNuova(idParticella);
            eventoParticellaVO.setIdParticellaCessata(storicoParticellaEvento
                .getIdParticella());
            eventoParticellaDAO.insertEventoParticella(eventoParticellaVO);
          }
          else
          {
            sessionContext.setRollbackOnly();
            return result;
          }
        }
        
        //Lavoro su db_esisto_pascolo_magro
        /*if(!flagPartUgualePadre && Validator.isNotEmpty(idParticellaCertificata))
        {
          if(!registroPascoloGaaDAO.isRegistroPascoliPratoPolifita(idParticellaCertificata.longValue()))
          {
            RegistroPascoloVO registroPascoloVO = new RegistroPascoloVO();
            registroPascoloVO.setAnnoCampagna(DateUtils.getCurrentYear());
            registroPascoloVO.setComune(storicoParticellaVO.getIstatComune());
            registroPascoloVO.setIdFonte(new Long(9));
            registroPascoloVO.setSezione(storicoParticellaVO.getSezione());
            registroPascoloVO.setIdParticellaCertificata(idParticellaCertificata);
            registroPascoloVO.setFoglio(new Integer(storicoParticellaVO.getFoglio()));
            registroPascoloVO.setParticella(new Long(storicoParticellaVO.getParticella()));
            registroPascoloVO.setSubalterno(storicoParticellaVO.getSubalterno());
            registroPascoloVO.setSuperficie(new BigDecimal(storicoParticellaVO.getSupCatastale().replace(",", ".")));
            registroPascoloVO.setDataInizioValidita(new Date());
            
            registroPascoloGaaDAO.insertRegistroPascolo(registroPascoloVO);
          }
          
        }*/
        
        
      }
      
         
      // Inserisco il record su DB_CONDUZIONE_PARTICELLA
      storicoParticellaVO.getElencoConduzioni()[0]
          .setIdParticella(idParticella);
      storicoParticellaVO.getElencoConduzioni()[0]
          .setDataInizioConduzione(new java.util.Date(System
              .currentTimeMillis()));
      storicoParticellaVO.getElencoConduzioni()[0]
          .setDataAggiornamento(new java.util.Date(new Timestamp(System
              .currentTimeMillis()).getTime()));
      storicoParticellaVO.getElencoConduzioni()[0]
          .setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
      Long idConduzioneParticella = conduzioneParticellaDAO
          .insertConduzioneParticella(storicoParticellaVO.getElencoConduzioni()[0]);
      // Inserisco i records su DB_UTILIZZO_PARTICELLA
      UtilizzoParticellaVO[] elencoUtilizzi = storicoParticellaVO
          .getElencoConduzioni()[0].getElencoUtilizzi();
      if (elencoUtilizzi != null && elencoUtilizzi.length > 0)
      {
        for (int f = 0; f < elencoUtilizzi.length; f++)
        {
          UtilizzoParticellaVO utilizzoParticellaVO = (UtilizzoParticellaVO) elencoUtilizzi[f];
          // Se è stato valorizzato l'uso primario dell'utilizzo allora...
          if (utilizzoParticellaVO.getIdUtilizzo() != null)
          {
            // Inserisco il record su DB_UTILIZZO_PARTICELLA
            utilizzoParticellaVO.setIdUtilizzoParticella(null);
            utilizzoParticellaVO
                .setIdConduzioneParticella(idConduzioneParticella);
            utilizzoParticellaVO.setDataAggiornamento(new java.util.Date(
                new Timestamp(System.currentTimeMillis()).getTime()));
            utilizzoParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
                .getIdUtente());
            utilizzoParticellaVO.setAnno(DateUtils.getCurrentYear().toString());
            utilizzoParticellaVO.setNote(null);
            Long idUtilizzoParticella = utilizzoParticellaDAO
                .insertUtilizzoParticella(utilizzoParticellaVO);
            utilizzoParticellaVO.setIdUtilizzoParticella(idUtilizzoParticella);
            // Se sono stati indicati gli utilizzi consociati
            UtilizzoConsociatoVO[] elencoUtilizzoConsociato = utilizzoParticellaVO
                .getElencoUtilizziConsociati();
            if (elencoUtilizzoConsociato != null
                && elencoUtilizzoConsociato.length > 0)
            {
              for (int g = 0; g < elencoUtilizzoConsociato.length; g++)
              {
                UtilizzoConsociatoVO utilizzoConsociatoVO = (UtilizzoConsociatoVO) elencoUtilizzoConsociato[g];
                // ... Li inserisco solo a patto che il numero piante
                // specificato sia > 0
                if (Validator
                    .isNotEmpty(utilizzoConsociatoVO.getNumeroPiante())
                    && !utilizzoConsociatoVO.getNumeroPiante()
                        .equalsIgnoreCase("0"))
                {
                  utilizzoConsociatoVO.setIdUtilizzoConsociato(null);
                  utilizzoConsociatoVO
                      .setIdUtilizzoParticella(utilizzoParticellaVO
                          .getIdUtilizzoParticella());
                  utilizzoConsociatoDAO
                      .insertUtilizzoConsociato(utilizzoConsociatoVO);
                }
              }
            }
          }
        }
      }    
      
    }
    catch (Exception dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
    return result;
  }
  
  
  
  
  
  
  
  
  //Usato per inserire particelle non legate a nessuna azienda
  public String inserisciParticella(StoricoParticellaVO storicoParticellaVO,
      RuoloUtenza ruoloUtenza, StoricoParticellaVO[] elencoParticelleEvento,
      Long idEvento) throws Exception
  {
    String result=null;
    try
    {
      Long idParticella = null;
      //Long idParticellaCertificata = null;
      
      
      //ricerco in caso di accorpamento o franzionamento se la nuova particella è identica al padre
      boolean flagPartUgualePadre = false;
      if (idEvento != null && idEvento.longValue() != 0)
      {
        for (int i = 0; i < elencoParticelleEvento.length; i++)
        {
          StoricoParticellaVO storicoParticellaEvento = (StoricoParticellaVO) elencoParticelleEvento[i];
          if(storicoParticellaEvento.equalsKey(storicoParticellaVO)) 
          {
            flagPartUgualePadre = true;
            break;
          }
        }
      }
      
      
      
      // Se la particella non è censita su DB_STORICO_PARTICELLA
      if (storicoParticellaVO.getIdStoricoParticella() == null)
      {
        // Inserisco un record su DB_PARTICELLA
        ParticellaVO particellaVO = new ParticellaVO();
        particellaVO.setDataCreazione(new java.util.Date(new Timestamp(System
            .currentTimeMillis()).getTime()));
        particellaVO.setDataInizioValidita(new java.util.Date(System
            .currentTimeMillis()));
        idParticella = particellaDAO.insertParticella(particellaVO);
        // Inserisco un record sulla tabella DB_STORICO_PARTICELLA
        storicoParticellaVO.setIdParticella(idParticella);
        storicoParticellaVO.setDataInizioValidita(new java.util.Date(System
            .currentTimeMillis()));
        storicoParticellaVO.setDataAggiornamento(new java.util.Date(
            new Timestamp(System.currentTimeMillis()).getTime()));
        storicoParticellaVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
        storicoParticellaDAO
            .insertStoricoParticella(storicoParticellaVO);
        // Se era comunque censita su una delle fonti ufficiali
        if (storicoParticellaVO.getParticellaCertificataVO() != null)
        {
          // Creo il legame con DB_PARTICELLA_CERTIFICATA
          ParticellaCertificataVO particellaCertificataVO = storicoParticellaVO
              .getParticellaCertificataVO();
          //idParticellaCertificata = particellaCertificataVO.getIdParticellaCertificata();
          particellaCertificataVO.setIdParticella(idParticella);
          particellaCertificataDAO
              .allineaParticellaCertificata(particellaCertificataVO);
        }
        
        
        //devo aggiungere i tipi area....perchè non censita
        if(storicoParticellaVO.getvValoriTipoArea() != null)
        {
          for(int i=0;i<storicoParticellaVO.getvValoriTipoArea().size();i++)
          {
            TipoAreaVO tipoAreaVO = storicoParticellaVO.getvValoriTipoArea().get(i);
            TipoValoreAreaVO tipoAreaValoreVO = tipoAreaVO.getvTipoValoreArea().get(0);
            if(Validator.isNotEmpty(tipoAreaValoreVO.getValore())
              && !"S".equalsIgnoreCase(tipoAreaVO.getFlagEsclusivoFoglio()))
            {
              Long idTipoValoreArea = particellaGaaDAO.getIdTipoValoreArea(tipoAreaVO.getIdTipoArea(), 
                tipoAreaValoreVO.getValore());
              particellaGaaDAO.insertRParticellaArea(idParticella, idTipoValoreArea);
            }
            
            /*if("S".equalsIgnoreCase(tipoAreaVO.getFlagAreaModificabile()))
            { 
              Long idTipoValoreArea = particellaGaaDAO.getIdTipoValoreArea(tipoAreaVO.getIdTipoArea(), 
                  tipoAreaValoreVO.getValore());
              particellaGaaDAO.insertRParticellaArea(idParticella, idTipoValoreArea);
            }*/
            
          }          
        }
        
        
      }
      // Se invece è censita
      else
      {
        StoricoParticellaVO oldStoricoParticellaVO = storicoParticellaDAO
            .findStoricoParticellaByPrimaryKey(storicoParticellaVO
                .getIdStoricoParticella());
        // Setto i parametri in questo modo così da permettere al metodo equals
        // di funzionare in modo corretto
        //oldStoricoParticellaVO.setSupCatastale(StringUtils
            //.parseSuperficieField(oldStoricoParticellaVO.getSupCatastale()));
        //oldStoricoParticellaVO.setElencoConduzioni(storicoParticellaVO
            //.getElencoConduzioni());
        //oldStoricoParticellaVO
            //.setElencoConduzioniDichiarate(storicoParticellaVO
                //.getElencoConduzioniDichiarate());
        //oldStoricoParticellaVO.setDocumentoVO(storicoParticellaVO
            //.getDocumentoVO());
        oldStoricoParticellaVO.setFoglioVO(storicoParticellaVO.getFoglioVO());
        idParticella = oldStoricoParticellaVO.getIdParticella();
        oldStoricoParticellaVO.setSupCatastale(StringUtils
            .parseSuperficieField(oldStoricoParticellaVO.getSupCatastale()));      
        storicoParticellaVO.setSupCatastale(StringUtils
            .parseSuperficieField(storicoParticellaVO.getSupCatastale()));
        
        
        ParticellaCertificataVO particellaCertificataVO = particellaCertificataDAO
            .findParticellaCertificataByIdParticella(idParticella, null);
        Date dataSoppressione = particellaCertificataVO.getDataSoppressione();
        if(Validator.isNotEmpty(particellaCertificataVO))
        {
          //idParticellaCertificata = particellaCertificataVO.getIdParticellaCertificata();
          dataSoppressione = particellaCertificataVO.getDataSoppressione();
        }
        ParticellaVO particellaVO = particellaDAO.findParticellaByPrimaryKey(idParticella);
        if(Validator.isNotEmpty(particellaVO.getDataCessazione())
             && Validator.isEmpty(dataSoppressione))
        {
          particellaDAO.attivaParticella(idParticella);            
        }
        
        // Se sono stati modificati i dati territoriali
        if (!storicoParticellaVO.equalsDatiTerritorialiInserimento(oldStoricoParticellaVO)
          || flagPartUgualePadre)
        {
          // Storicizzo su DB_STORICO_PARTICELLA
          //oldStoricoParticellaVO.setDataFineValidita(new java.util.Date(
              //new Timestamp(System.currentTimeMillis()).getTime()));
          //storicoParticellaDAO.updateStoricoParticella(oldStoricoParticellaVO);
          storicoParticellaDAO.storicizzaStoricoParticella(oldStoricoParticellaVO
              .getIdStoricoParticella().longValue());
          // Inserisco il nuovo record su DB_STORICO_PARTICELLA
          storicoParticellaVO.setIdParticella(oldStoricoParticellaVO
              .getIdParticella());
          storicoParticellaVO.setDataInizioValidita(new java.util.Date(System
              .currentTimeMillis()));
          storicoParticellaVO.setDataAggiornamento(new java.util.Date(
              new Timestamp(System.currentTimeMillis()).getTime()));
          storicoParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
              .getIdUtente());
          
          if(flagPartUgualePadre)
          {
            storicoParticellaVO.setMotivoModifica("AGGIORNAMENTO IN SEGUITO A FRAZIONAMENTO O ACCORPAMENTO SU SE STESSA");
          }
          
          storicoParticellaDAO
              .insertStoricoParticella(storicoParticellaVO);
          
          if(flagPartUgualePadre)
          {
            particellaCertificataDAO.updateParticellaCertificataFrazAcc(particellaCertificataVO);
          }
        }
        
        
        
        //confronto i tipi area e modifico solo quelli cambiati...
        if(storicoParticellaVO.getvValoriTipoArea() != null)
        {
          for(int i=0;i<storicoParticellaVO.getvValoriTipoArea().size();i++)
          {
            TipoAreaVO tipoAreaVO = storicoParticellaVO.getvValoriTipoArea().get(i);
            TipoValoreAreaVO tipoAreaValoreVO = tipoAreaVO.getvTipoValoreArea().get(0);
            if(Validator.isNotEmpty(tipoAreaValoreVO.getValore())
                && !"S".equalsIgnoreCase(tipoAreaVO.getFlagEsclusivoFoglio()))
            { 
              //qui devo confrontare......
              String valore = particellaGaaDAO.getValoreAttivoTipoAreaFromParticellaAndId(storicoParticellaVO.getIdParticella(),
                  tipoAreaVO.getIdTipoArea());
              if(Validator.isNotEmpty(valore))
              {
                //se lo trovo su db ma sono diversi....
                if(Validator.isNotEmpty(tipoAreaValoreVO.getValore()) && !valore.equalsIgnoreCase(tipoAreaValoreVO.getValore()))
                {
                  //storicizzo
                  particellaGaaDAO.storicizzaRParticellaArea(storicoParticellaVO.getIdParticella(), 
                      tipoAreaValoreVO.getIdTipoValoreArea());
                  
                  //inserisco
                  Long idTipoValoreArea = particellaGaaDAO.getIdTipoValoreArea(tipoAreaVO.getIdTipoArea(), 
                      tipoAreaValoreVO.getValore());
                  particellaGaaDAO.insertRParticellaArea(idParticella, idTipoValoreArea);
                }
              }
              //non esiste su db inserisco
              else
              {
                Long idTipoValoreArea = particellaGaaDAO.getIdTipoValoreArea(tipoAreaVO.getIdTipoArea(), 
                    tipoAreaValoreVO.getValore());
                particellaGaaDAO.insertRParticellaArea(idParticella, idTipoValoreArea);
              }              
            }
            
          }          
        }
        
        
        
      }
      // Se l'utente ha specificato un evento di creazione della particella
      Vector<Long> vIdMenzionegeografica = null;
      if (idEvento != null && idEvento.longValue() != 0)
      {
        String cessaParticella = tipoEventoDAO.getFlagEventoCessaParticella(idEvento.longValue());
        // Per tutte le particelle indicate...
        for (int i = 0; i < elencoParticelleEvento.length; i++)
        {
          StoricoParticellaVO storicoParticellaEvento = (StoricoParticellaVO) elencoParticelleEvento[i];
        //enro solo se flag è D o S oppure sono diverso dalla particella che sto inserendo...
          if(("S".equalsIgnoreCase(cessaParticella) || "D".equalsIgnoreCase(cessaParticella))
              && !storicoParticellaEvento.equalsKey(storicoParticellaVO))
          {
            Vector<MenzioneParticellaVO> vMenzioneParticella = tipoMenzioneGeograficaDAO
                .getVMenzioneParticellaAttiva(storicoParticellaEvento.getIdParticella().longValue());
            if(vMenzioneParticella != null)
            {
              for(int j=0;j<vMenzioneParticella.size();j++)
              {
                if(vIdMenzionegeografica == null)
                {
                  vIdMenzionegeografica = new Vector<Long>();
                }
                
                MenzioneParticellaVO menzioneParticellaVO = vMenzioneParticella.get(j);
                Long idMenzioneParticella = menzioneParticellaVO.getIdMenzioneParticella();
                if(!vIdMenzionegeografica.contains(menzioneParticellaVO.getIdMenzioneGeografica()))
                {
                  vIdMenzionegeografica.add(menzioneParticellaVO.getIdMenzioneGeografica());
                                  
                  menzioneParticellaVO.setIdParticella(idParticella);
                  menzioneParticellaVO.setDataInizioValidita(new Date());
                  tipoMenzioneGeograficaDAO.insertMenzioneParticella(menzioneParticellaVO);
                }
                
                if (storicoParticellaEvento.isCessaParticella())
                {
                  tipoMenzioneGeograficaDAO.storicizzaMenzioneParticella(idMenzioneParticella);                
                }
              }
            }
            
            
            
            if(storicoParticellaEvento.isCessaParticella()
                || "D".equalsIgnoreCase(cessaParticella))
            {
              if(Validator.isEmpty(storicoParticellaEvento.getDataCessazione()))
              {
                // Le cesso su DB_PARTICELLA
                particellaDAO.cessaParticella(storicoParticellaEvento
                  .getIdParticella());
              }
            }
            
            
          }
          
          
          
          
          // Se non risultano cessate...
          if ((storicoParticellaEvento.getDataCessazione() == null)
              && storicoParticellaEvento.isCessaParticella())
          {
            // Le cesso su DB_PARTICELLA
            particellaDAO.cessaParticella(storicoParticellaEvento
                .getIdParticella());
          }
          // Inserisco i records su DB_EVENTO_PARTICELLA solo se non esistono
          // già degli
          // eventi associati alla particella che si sta inserendo
          result = eventoParticellaDAO.getEventiParticellaByIdParticellaNuovaAndCessata(idParticella,
                  storicoParticellaEvento.getIdParticella(), idEvento.longValue());
          if (result == null)
          {
            EventoParticellaVO eventoParticellaVO = new EventoParticellaVO();
            eventoParticellaVO.setIdEvento(idEvento);
            eventoParticellaVO.setDataAggiornamento(new java.util.Date(
                new Timestamp(System.currentTimeMillis()).getTime()));
            eventoParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
                .getIdUtente());
            eventoParticellaVO.setIdParticellaNuova(idParticella);
            eventoParticellaVO.setIdParticellaCessata(storicoParticellaEvento
                .getIdParticella());
            eventoParticellaDAO.insertEventoParticella(eventoParticellaVO);
          }
          else
          {
            sessionContext.setRollbackOnly();
            return result;
          }
        }
        
        //Lavoro su db_esisto_pascolo_magro
        //Eliminato per richista Terry/Elisa 27/05/2015
        /*if(!flagPartUgualePadre && Validator.isNotEmpty(idParticellaCertificata))
        {
          if(!registroPascoloGaaDAO.isRegistroPascoliPratoPolifita(idParticellaCertificata.longValue()))
          {
            RegistroPascoloVO registroPascoloVO = new RegistroPascoloVO();
            registroPascoloVO.setAnnoCampagna(DateUtils.getCurrentYear());
            registroPascoloVO.setComune(storicoParticellaVO.getIstatComune());
            registroPascoloVO.setIdFonte(new Long(9));
            registroPascoloVO.setSezione(storicoParticellaVO.getSezione());
            registroPascoloVO.setIdParticellaCertificata(idParticellaCertificata);
            registroPascoloVO.setFoglio(new Integer(storicoParticellaVO.getFoglio()));
            registroPascoloVO.setParticella(new Long(storicoParticellaVO.getParticella()));
            registroPascoloVO.setSubalterno(storicoParticellaVO.getSubalterno());
            registroPascoloVO.setSuperficie(new BigDecimal(storicoParticellaVO.getSupCatastale().replace(",", ".")));
            registroPascoloVO.setDataInizioValidita(new Date());
            
            registroPascoloGaaDAO.insertRegistroPascolo(registroPascoloVO);
          }
          
        }*/
        
        
        
        
      }
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
    return result;
  }
  
  
  
  
  
  
  
  
  
  
  
  

  /**
   * Metodo che mi permette di estrarre la somma delle superfici condotte
   * relative ad una determinata particella che insiste su una determinata
   * azienda agriocola
   * 
   * @param idAzienda
   * @param idParticella
   * @param onlyActive
   * @return java.lang.String
   * @throws Exception
   */
  public BigDecimal getTotSupCondottaByAziendaAndParticella(
      Long idAzienda, Long idParticella)
      throws Exception
  {
    try
    {
      return conduzioneParticellaDAO.getTotSupCondottaByAziendaAndParticella(
          idAzienda, idParticella);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi permette di estrarre tutti gli eventi relativi ad una
   * particella
   * 
   * @param idParticella
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.EventoParticellaVO[]
   * @throws Exception
   */
  public EventoParticellaVO[] getEventiParticellaByIdParticellaNuovaOrCessata(
      Long idParticella, String[] orderBy) throws Exception
  {
    try
    {
      return eventoParticellaDAO
          .getEventiParticellaByIdParticellaNuovaOrCessata(idParticella,
              orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi permette di estrarre l'elenco delle particelle associabili ad
   * un fabbricato relative ad un determinato piano di riferimento
   * 
   * @param idFabbricato
   * @param idPianoRiferimento
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] getListParticelleForFabbricato(
      Long idFabbricato, Long idPianoRiferimento, String[] orderBy)
      throws Exception
  {
    try
    {
      return storicoParticellaDAO.getListParticelleForFabbricato(idFabbricato,
          idPianoRiferimento, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che si occupa di verificare se il piano di riferimento corrente è
   * frutto di un ripristino di una dichiarazione di consistenza
   * 
   * @param idAzienda
   * @return boolean
   * @throws Exception
   */
  public boolean isPianoRiferimentoRipristinato(Long idAzienda)
      throws Exception
  {
    boolean isRipristinata = false;
    try
    {
      // Conduzione Particella
      isRipristinata = conduzioneParticellaDAO
          .isConduzioneRipristinata(idAzienda);
      // Fabbricati
      if (!isRipristinata)
      {
        isRipristinata = fabbricatoDAO.isFabbricatoRipristinato(idAzienda);
      }
      // Allevamenti
      if (!isRipristinata)
      {
        isRipristinata = allevamentoAnagDAO
            .isAllevamentoRipristinato(idAzienda);
      }
      return isRipristinata;
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi consente di visualizzare l'elenco delle unità arboree
   * associate ad una determinata particella ed un'azienda
   * 
   * @param idParticella
   * @param idPianoRiferimento
   * @param idAzienda
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoUnitaArboreaVO[]
   * @throws Exception
   */
  public StoricoUnitaArboreaVO[] getListStoricoUnitaArboreaByLogicKey(
      Long idParticella, Long idPianoRiferimento, Long idAzienda,
      String[] orderBy) throws Exception
  {
    try
    {
      return storicoUnitaArboreaDAO.getListStoricoUnitaArboreaByLogicKey(
          idParticella, idPianoRiferimento, idAzienda, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi permette di estrarre l'elenco delle unità arboree relative
   * all'azienda agricola(stato attuale o piano di riferimento).
   * 
   * @param idAzienda
   * @param idPianoRiferimento
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] getListStoricoUnitaArboreaByIdAzienda(
      Long idAzienda, Long idPianoRiferimento, String[] orderBy)
      throws Exception
  {
    try
    {
      return storicoUnitaArboreaDAO.getListStoricoUnitaArboreaByIdAzienda(
          idAzienda, idPianoRiferimento, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per recuperare la max data esecuzione controlli relativa alle unità
   * arboree
   * 
   * @param idAzienda
   * @return java.lang.String
   * @throws Exception
   */
  public java.lang.String getMaxDataEsecuzioneControlliUnitaArborea(
      Long idAzienda) throws Exception
  {
    try
    {
      return storicoUnitaArboreaDAO
          .getMaxDataEsecuzioneControlliUnitaArborea(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce l'elenco delle anomalie relative all'unità
   * arborea selezionata
   * 
   * @param idStoricoUnitaArborea
   * @param orderBy
   * @return it.csi.solmr.dto.anag.EsitoControlloUnarVO[]
   * @throws Exception
   */
  public EsitoControlloUnarVO[] getListEsitoControlloUnarByIdStoricoUnitaArborea(
      Long idStoricoUnitaArborea, String[] orderBy) throws Exception
  {
    try
    {
      return esitoControlloUnarDAO
          .getListEsitoControlloUnarByIdStoricoUnitaArborea(
              idStoricoUnitaArborea, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi permette di recuperare l'unita arborea e i dati della
   * particella in funzione della chiave primaria
   * 
   * @param idStoricoUnitaArborea
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO
   * @throws Exception
   */
  public StoricoParticellaVO findStoricoParticellaArborea(
      Long idStoricoUnitaArborea) throws Exception
  {
    StoricoParticellaVO storicoParticellaVO = null;
    try
    {
      storicoParticellaVO = storicoUnitaArboreaDAO
          .findStoricoParticellaArborea(idStoricoUnitaArborea);
      
      AltroVitignoVO[] elencoAltroVitignoVO = altroVitignoDAO
          .getListAltroVitignoByIdStoricoUnitaArborea(storicoParticellaVO
              .getStoricoUnitaArboreaVO().getIdStoricoUnitaArborea(), null);      
      storicoParticellaVO.getStoricoUnitaArboreaVO().setElencoAltriVitigni(
          elencoAltroVitignoVO);
      if(Validator.isNotEmpty(storicoParticellaVO
          .getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO()))
      {
        if(Validator.isNotEmpty(storicoParticellaVO.getStoricoUnitaArboreaVO()
              .getIdVigna()))
        {
          storicoParticellaVO.getStoricoUnitaArboreaVO().setVignaVO(tipoTipologiaVinoDAO.getVignaByByPrimary(storicoParticellaVO.getStoricoUnitaArboreaVO()
                .getIdVigna()));
          /*storicoParticellaVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO().setVignaVO(tipoTipologiaVinoDAO.getVignaByByPrimary(storicoParticellaVO.getStoricoUnitaArboreaVO()
              .getIdVigna()));*/
        }
      }
      
      if(Validator.isNotEmpty(storicoParticellaVO.getStoricoUnitaArboreaVO().getIdMenzioneGeografica()))
      {
        TipoMenzioneGeograficaVO tipoMenzioneGeograficaVO = 
          tipoMenzioneGeograficaDAO.getTipoMenzioneGeografica(
              storicoParticellaVO.getStoricoUnitaArboreaVO().getIdMenzioneGeografica().longValue());
        Vector<TipoMenzioneGeograficaVO> vTipoMenzioneGeograficaVO = new Vector<TipoMenzioneGeograficaVO>();
        vTipoMenzioneGeograficaVO.add(tipoMenzioneGeograficaVO);
        storicoParticellaVO.getStoricoUnitaArboreaVO().setElencoMenzioneGeografica(vTipoMenzioneGeograficaVO);
      }
      else
      {
        storicoParticellaVO.getStoricoUnitaArboreaVO().setElencoMenzioneGeografica(
          tipoMenzioneGeograficaDAO.getListTipoMenzioneGeografica(
              storicoParticellaVO.getIdParticella().longValue(), 
              storicoParticellaVO.getStoricoUnitaArboreaVO().getIdTipologiaVino(), null));
      }
      
      return storicoParticellaVO;
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public StoricoParticellaVO findStoricoParticellaArboreaBasic(
      Long idStoricoUnitaArborea) throws Exception
  {
    try
    {      
      return storicoUnitaArboreaDAO.findStoricoParticellaArboreaBasic(idStoricoUnitaArborea);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  
  
  public StoricoParticellaVO findStoricoParticellaArboreaConduzione(
      Long idStoricoUnitaArborea, long idAzienda) throws Exception
  {
    StoricoParticellaVO storicoParticellaVO = null;
    try
    {
      storicoParticellaVO = storicoUnitaArboreaDAO
          .findStoricoParticellaArboreaConduzione(idStoricoUnitaArborea, idAzienda);
      
      return storicoParticellaVO;
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public StoricoParticellaVO findStoricoParticellaArboreaTolleranza(
      Long idStoricoUnitaArborea, long idAzienda, String nomeLib) throws Exception
  {
    StoricoParticellaVO storicoParticellaVO = null;
    try
    {
      storicoParticellaVO = storicoUnitaArboreaDAO
          .findStoricoParticellaArboreaTolleranza(idStoricoUnitaArborea, idAzienda, nomeLib);
      
      AltroVitignoVO[] elencoAltroVitignoVO = altroVitignoDAO
          .getListAltroVitignoByIdStoricoUnitaArborea(storicoParticellaVO
              .getStoricoUnitaArboreaVO().getIdStoricoUnitaArborea(), null);      
      storicoParticellaVO.getStoricoUnitaArboreaVO().setElencoAltriVitigni(
          elencoAltroVitignoVO);
      
      
      if(Validator.isNotEmpty(storicoParticellaVO.getStoricoUnitaArboreaVO().getIdMenzioneGeografica()))
      {
        TipoMenzioneGeograficaVO tipoMenzioneGeograficaVO = 
          tipoMenzioneGeograficaDAO.getTipoMenzioneGeografica(
              storicoParticellaVO.getStoricoUnitaArboreaVO().getIdMenzioneGeografica().longValue());
        Vector<TipoMenzioneGeograficaVO> vTipoMenzioneGeograficaVO = new Vector<TipoMenzioneGeograficaVO>();
        vTipoMenzioneGeograficaVO.add(tipoMenzioneGeograficaVO);
        storicoParticellaVO.getStoricoUnitaArboreaVO().setElencoMenzioneGeografica(vTipoMenzioneGeograficaVO);
      }
      else
      {
        storicoParticellaVO.getStoricoUnitaArboreaVO().setElencoMenzioneGeografica(
          tipoMenzioneGeograficaDAO.getListTipoMenzioneGeografica(
              storicoParticellaVO.getIdParticella().longValue(), 
              storicoParticellaVO.getStoricoUnitaArboreaVO().getIdTipologiaVino(), null));
      }
      
      return storicoParticellaVO;
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce l'elenco degli altri vitigni associati all'unità
   * arborea
   * 
   * @param idStoricoUnitaArborea
   * @param orderBy
   * @return it.csi.solmr.dto.anag.AltroVitignoVO[]
   * @throws Exception
   */
  public AltroVitignoVO[] getListAltroVitignoByIdStoricoUnitaArborea(
      Long idStoricoUnitaArborea, String[] orderBy) throws Exception
  {
    try
    {
      return altroVitignoDAO.getListAltroVitignoByIdStoricoUnitaArborea(
          idStoricoUnitaArborea, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi permette di recuperare l'elenco dei tipi utilizzo in funzione
   * del tipo
   * 
   * @param tipo
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.TipoUtilizzoVO[]
   * @throws Exception
   */
  public TipoUtilizzoVO[] getListTipiUsoSuoloByTipo(String tipo,
      boolean onlyActive, String[] orderBy) throws Exception
  {
    try
    {
      return tipoUtilizzoDAO.getListTipiUsoSuoloByTipo(tipo, onlyActive,
          orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce l'elenco delle forme allevamento
   * 
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.TipoFormaAllevamentoVO
   * @throws Exception
   */
  public TipoFormaAllevamentoVO[] getListTipoFormaAllevamento(
      boolean onlyActive, String[] orderBy) throws Exception
  {
    try
    {
      return tipoFormaAllevamentoDAO.getListTipoFormaAllevamento(onlyActive,
          orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce l'elenco delle causali modifica
   * 
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.TipoCausaleModificaVO[]
   * @throws Exception
   */
  public TipoCausaleModificaVO[] getListTipoCausaleModifica(boolean onlyActive,
      String[] orderBy) throws Exception
  {
    try
    {
      return tipoCausaleModificaDAO.getListTipoCausaleModifica(onlyActive,
          orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public Vector<TipoCausaleModificaVO> getListTipoCuasaleModificaByIdAzienda(long idAzienda)  
      throws Exception
  {
    try
    {
      return tipoCausaleModificaDAO.getListTipoCuasaleModificaByIdAzienda(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che si occupa di modificare le unità arboree
   * 
   * @param elencoParticelleArboree
   * @param ruoloUtenza
   * @throws Exception
   * @throws SolmrException
   */
  public void modificaUnitaArboree(
      StoricoParticellaVO[] elencoParticelleArboree, RuoloUtenza ruoloUtenza, String provenienza)
      throws Exception, SolmrException
  {
    try
    {
      Hashtable<Long,Long> elencoParticelle = new Hashtable<Long,Long>();
      Vector<BigDecimal> elencoIdParticella = new Vector<BigDecimal>();
      // Ciclo le unità arboree selezionate dall'utente
      
      Long idGenereIscrizioneDefault = tipoGenereIscrizioneDAO.getDefaultIdGenereIscrizione();
      Long idGenereIscrizioneProvvisorio = tipoGenereIscrizioneDAO.getIdGenereIscrizioneByFlag("N");
      
      
      for (int i = 0; i < elencoParticelleArboree.length; i++)
      {
        // Recupero il record da DB
        StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) elencoParticelleArboree[i];
        StoricoUnitaArboreaVO storicoUnitaArboreaVO = storicoParticellaVO
            .getStoricoUnitaArboreaVO();
        StoricoParticellaVO oldStoricoParticellaVO = findStoricoParticellaArborea(storicoUnitaArboreaVO
            .getIdStoricoUnitaArborea());
        StoricoUnitaArboreaVO oldStoricoUnitaArboreaVO = oldStoricoParticellaVO
            .getStoricoUnitaArboreaVO();
        if (elencoParticelle.get(storicoUnitaArboreaVO.getIdParticella()) == null)
        {
          elencoParticelle.put(storicoUnitaArboreaVO.getIdParticella(),
              storicoUnitaArboreaVO.getIdParticella());
          elencoIdParticella.add(new BigDecimal(storicoUnitaArboreaVO
              .getIdParticella().longValue()));
        }
        
        
        if (ruoloUtenza.isUtenteIntermediario()
            || ruoloUtenza.isUtenteOPRGestore())
        {
          if(oldStoricoUnitaArboreaVO.getStatoUnitaArborea() != null)
          {
             storicoUnitaArboreaVO.setStatoUnitaArborea(oldStoricoUnitaArboreaVO.getStatoUnitaArborea());
          }
          else
          {
            storicoUnitaArboreaVO
              .setStatoUnitaArborea(SolmrConstants.STATO_UV_PROPOSTO_CAA);
          }
        }
        else if (ruoloUtenza.isUtenteRegionale()
            || ruoloUtenza.isUtenteProvinciale())
        {
          if(SolmrConstants.CONFERMA_PA.equalsIgnoreCase(provenienza))
          {
            storicoUnitaArboreaVO
              .setStatoUnitaArborea(SolmrConstants.STATO_UV_PROPOSTO_CAA);
          }
          else
          {
            storicoUnitaArboreaVO
              .setStatoUnitaArborea(SolmrConstants.STATO_UV_VALIDATO_PA);
          }
        }
        
        // Verifico se l'unità arborea selezionata è presente in una
        // dichiarazione di consistenza
        UnitaArboreaDichiarataVO[] elencoUnitaArboreeDichiarate = unitaArboreaDichiarataDAO
            .getListUnitaArboreaDichiarataByIdStoricoUnitaArborea(
                oldStoricoUnitaArboreaVO.getIdStoricoUnitaArborea(), null);
        // Se lo è...
        if (elencoUnitaArboreeDichiarate != null
            && elencoUnitaArboreeDichiarate.length > 0)
        {
          // Verifico se è stato modificato il record oppure l'utente loggato
          // presenta una tipologia
          // ruolo non compatibile con lo stato dell'unità vitata o questa non
          // era ancora stata specificata
          if (!storicoUnitaArboreaVO.isEqual(oldStoricoUnitaArboreaVO)
              || !Validator.isNotEmpty(oldStoricoUnitaArboreaVO
                  .getStatoUnitaArborea())
              || ((ruoloUtenza.isUtenteRegionale() || ruoloUtenza
                  .isUtenteProvinciale()) && SolmrConstants.STATO_UV_PROPOSTO_CAA
                  .equalsIgnoreCase(oldStoricoUnitaArboreaVO
                      .getStatoUnitaArborea()))
              || ((ruoloUtenza.isUtenteIntermediario() || ruoloUtenza
                  .isUtenteOPRGestore()) && SolmrConstants.STATO_UV_VALIDATO_PA
                  .equalsIgnoreCase(oldStoricoUnitaArboreaVO
                      .getStatoUnitaArborea())))
          {
            // Effettuo la storicizzazione del record
            //oldStoricoUnitaArboreaVO.setDataFineValidita(new java.util.Date(
                //new Timestamp(System.currentTimeMillis()).getTime()));
            storicoUnitaArboreaDAO.storicizzaStoricoUnitaArborea(oldStoricoUnitaArboreaVO.getIdStoricoUnitaArborea());
                //.updateStoricoUnitaArborea(oldStoricoUnitaArboreaVO);
            // Inserisco il nuovo record
            storicoUnitaArboreaVO.setDataInizioValidita(new java.util.Date(
                new Timestamp(System.currentTimeMillis()).getTime()));
            storicoUnitaArboreaVO.setDataAggiornamento(new java.util.Date(
                new Timestamp(System.currentTimeMillis()).getTime()));
            storicoUnitaArboreaVO.setIdUtenteAggiornamento(ruoloUtenza
                .getIdUtente());
            storicoUnitaArboreaVO.setRecordModificato(SolmrConstants.FLAG_S);
            storicoUnitaArboreaVO.setDataEsecuzione(null);
            storicoUnitaArboreaVO.setEsitoControllo(null);
            if(!storicoUnitaArboreaVO.isNoNoteNullable())
            {
              storicoUnitaArboreaVO.setNote(null);
            }
            
            
            
            if(SolmrConstants.IMPORTA_CCIAA.equalsIgnoreCase(provenienza))
            {
              storicoUnitaArboreaVO.setIdGenereIscrizione(
                  idGenereIscrizioneDefault);
            }
            else
            {
              if(ruoloUtenza.isUtentePA())
              {
                if(SolmrConstants.CONFERMA_PA.equalsIgnoreCase(provenienza))
                {
                  storicoUnitaArboreaVO.setIdGenereIscrizione(
                      oldStoricoUnitaArboreaVO.getIdGenereIscrizione());
                }
                else
                {
                  storicoUnitaArboreaVO.setIdGenereIscrizione(
                    idGenereIscrizioneDefault);
                }
              }
              else
              {
                storicoUnitaArboreaVO.setIdGenereIscrizione(
                    idGenereIscrizioneProvvisorio);
              }
            }
            
            
            Long idStoricoUnitaArborea = storicoUnitaArboreaDAO
                .insertStoricoUnitaArborea(storicoUnitaArboreaVO);
            // Inserisco il nuovo record su DB_ALTRO_VITIGNO
            AltroVitignoVO[] elencoAltriVitigni = storicoUnitaArboreaVO
                .getElencoAltriVitigni();
            if (elencoAltriVitigni != null && elencoAltriVitigni.length > 0)
            {
              for (int a = 0; a < elencoAltriVitigni.length; a++)
              {
                AltroVitignoVO altroVitignoVO = (AltroVitignoVO) elencoAltriVitigni[a];
                altroVitignoVO.setIdStoricoUnitaArborea(idStoricoUnitaArborea);
                altroVitignoDAO.insertAltroVitigno(altroVitignoVO);
              }
            }
          }
        }
        // Altrimenti
        else
        {
          // Se la data di aggiornamento è uguale a SYSDATE
          if (DateUtils.formatDate(
              oldStoricoUnitaArboreaVO.getDataAggiornamento())
              .equalsIgnoreCase(
                  DateUtils.formatDate(new java.util.Date(System
                      .currentTimeMillis()))))
          {
            // Se l'utente loggato presenta una tipologia ruolo non compatibile
            // con lo stato dell'unità vitata oppure quest'ultima non è ancora
            // stata specificata
            if (!Validator.isNotEmpty(oldStoricoUnitaArboreaVO
                .getStatoUnitaArborea())
                || ((ruoloUtenza.isUtenteRegionale() || ruoloUtenza
                    .isUtenteProvinciale()) && SolmrConstants.STATO_UV_PROPOSTO_CAA
                    .equalsIgnoreCase(oldStoricoUnitaArboreaVO
                        .getStatoUnitaArborea()))
                || ((ruoloUtenza.isUtenteIntermediario() || ruoloUtenza
                    .isUtenteOPRGestore()) && SolmrConstants.STATO_UV_VALIDATO_PA
                    .equalsIgnoreCase(oldStoricoUnitaArboreaVO
                        .getStatoUnitaArborea())))
            {
              // Effettuo la storicizzazione del record
              //oldStoricoUnitaArboreaVO.setDataFineValidita(new java.util.Date(
                  //new Timestamp(System.currentTimeMillis()).getTime()));
              storicoUnitaArboreaDAO.storicizzaStoricoUnitaArborea(oldStoricoUnitaArboreaVO.getIdStoricoUnitaArborea());
                  //.updateStoricoUnitaArborea(oldStoricoUnitaArboreaVO);
              // Inserisco il nuovo record
              storicoUnitaArboreaVO.setDataInizioValidita(new java.util.Date(
                  new Timestamp(System.currentTimeMillis()).getTime()));
              storicoUnitaArboreaVO.setDataAggiornamento(new java.util.Date(
                  new Timestamp(System.currentTimeMillis()).getTime()));
              storicoUnitaArboreaVO.setIdUtenteAggiornamento(ruoloUtenza
                  .getIdUtente());
              storicoUnitaArboreaVO.setRecordModificato(SolmrConstants.FLAG_S);
              storicoUnitaArboreaVO.setDataEsecuzione(null);
              storicoUnitaArboreaVO.setEsitoControllo(null);
              storicoUnitaArboreaVO.setNote(null);
              
              
              if(SolmrConstants.IMPORTA_CCIAA.equalsIgnoreCase(provenienza))
              {
                storicoUnitaArboreaVO.setIdGenereIscrizione(
                    idGenereIscrizioneDefault);
              }
              else
              {
                if(ruoloUtenza.isUtentePA())
                {
                  storicoUnitaArboreaVO.setIdGenereIscrizione(
                      idGenereIscrizioneDefault);
                }
                else
                {
                  storicoUnitaArboreaVO.setIdGenereIscrizione(
                      idGenereIscrizioneProvvisorio);
                }
              }
              
              Long idStoricoUnitaArborea = storicoUnitaArboreaDAO
                  .insertStoricoUnitaArborea(storicoUnitaArboreaVO);
              // Inserisco il nuovo record su DB_ALTRO_VITIGNO
              AltroVitignoVO[] elencoAltriVitigni = storicoUnitaArboreaVO
                  .getElencoAltriVitigni();
              if (elencoAltriVitigni != null && elencoAltriVitigni.length > 0)
              {
                for (int a = 0; a < elencoAltriVitigni.length; a++)
                {
                  AltroVitignoVO altroVitignoVO = (AltroVitignoVO) elencoAltriVitigni[a];
                  altroVitignoVO
                      .setIdStoricoUnitaArborea(idStoricoUnitaArborea);
                  altroVitignoDAO.insertAltroVitigno(altroVitignoVO);
                }
              }
            }
            // Se è stato effettivamente modificato il record
            else if (!storicoUnitaArboreaVO.isEqual(oldStoricoUnitaArboreaVO))
            {
              // Effettuo solo update del record
              storicoUnitaArboreaVO.setDataAggiornamento(new java.util.Date(
                  new Timestamp(System.currentTimeMillis()).getTime()));
              storicoUnitaArboreaVO.setIdUtenteAggiornamento(ruoloUtenza
                  .getIdUtente());
              storicoUnitaArboreaVO.setDataEsecuzione(null);
              storicoUnitaArboreaVO.setEsitoControllo(null);
              storicoUnitaArboreaVO.setRecordModificato(SolmrConstants.FLAG_S);
              
              
              if(SolmrConstants.IMPORTA_CCIAA.equalsIgnoreCase(provenienza))
              {
                storicoUnitaArboreaVO.setIdGenereIscrizione(
                    idGenereIscrizioneDefault);
              }
              else
              {
                if(ruoloUtenza.isUtentePA())
                {
                  storicoUnitaArboreaVO.setIdGenereIscrizione(
                      idGenereIscrizioneDefault);
                }
                else
                {
                  storicoUnitaArboreaVO.setIdGenereIscrizione(
                      idGenereIscrizioneProvvisorio);
                }
              }
              
              storicoUnitaArboreaDAO
                  .updateStoricoUnitaArborea(storicoUnitaArboreaVO);
              // Elimino i precedenti altri vitigni e inserisco i nuovi
              AltroVitignoVO[] elencoAltriVitigni = storicoUnitaArboreaVO
                  .getElencoAltriVitigni();
              if (elencoAltriVitigni != null && elencoAltriVitigni.length > 0)
              {
                altroVitignoDAO
                    .deleteAltroVitignoByIdStoricoUnitaArborea(storicoUnitaArboreaVO
                        .getIdStoricoUnitaArborea());
                for (int a = 0; a < elencoAltriVitigni.length; a++)
                {
                  AltroVitignoVO altroVitignoVO = (AltroVitignoVO) elencoAltriVitigni[a];
                  altroVitignoVO.setIdStoricoUnitaArborea(storicoUnitaArboreaVO
                      .getIdStoricoUnitaArborea());
                  altroVitignoDAO.insertAltroVitigno(altroVitignoVO);
                }
              }
              else
              {
                // Li cancello nel caso ci fossero precedentemente e siano stati
                // eliminati
                altroVitignoDAO
                    .deleteAltroVitignoByIdStoricoUnitaArborea(storicoUnitaArboreaVO
                        .getIdStoricoUnitaArborea());
              }
            }
          }
          else
          {
            // Altrimenti se sono stati modificati la destinazione, il vitigno,
            // la superficie vitata, la data impianto, la data prima produzione, la causale modifica,
            // la matricolaCCIAA, l'anno iscrizione albo, la tipologia vino,
            // oppure l'utente loggato presenta una
            // tipologia ruolo non compatibile con lo stato dell'unità vitata
            // o quest'ultima non è ancora stata specificata, storicizzo il
            // vecchio record
            if (!storicoUnitaArboreaVO
                .equalsForUpdate(oldStoricoUnitaArboreaVO)
                || !Validator.isNotEmpty(oldStoricoUnitaArboreaVO
                    .getStatoUnitaArborea())
                || ((ruoloUtenza.isUtenteRegionale() || ruoloUtenza
                    .isUtenteProvinciale()) && SolmrConstants.STATO_UV_PROPOSTO_CAA
                    .equalsIgnoreCase(oldStoricoUnitaArboreaVO
                        .getStatoUnitaArborea()))
                || ((ruoloUtenza.isUtenteIntermediario() || ruoloUtenza
                    .isUtenteOPRGestore()) && SolmrConstants.STATO_UV_VALIDATO_PA
                    .equalsIgnoreCase(oldStoricoUnitaArboreaVO
                        .getStatoUnitaArborea())))
            {
              //oldStoricoUnitaArboreaVO.setDataFineValidita(new java.util.Date(
                  //new Timestamp(System.currentTimeMillis()).getTime()));
              storicoUnitaArboreaDAO.storicizzaStoricoUnitaArborea(oldStoricoUnitaArboreaVO.getIdStoricoUnitaArborea());
                  //.updateStoricoUnitaArborea(oldStoricoUnitaArboreaVO);
              // Inserisco il nuovo record
              storicoUnitaArboreaVO.setDataInizioValidita(new java.util.Date(
                  new Timestamp(System.currentTimeMillis()).getTime()));
              storicoUnitaArboreaVO.setDataAggiornamento(new java.util.Date(
                  new Timestamp(System.currentTimeMillis()).getTime()));
              storicoUnitaArboreaVO.setIdUtenteAggiornamento(ruoloUtenza
                  .getIdUtente());
              storicoUnitaArboreaVO.setRecordModificato(SolmrConstants.FLAG_S);
              storicoUnitaArboreaVO.setDataEsecuzione(null);
              storicoUnitaArboreaVO.setEsitoControllo(null);
              storicoUnitaArboreaVO.setNote(null);
              
              
              if(SolmrConstants.IMPORTA_CCIAA.equalsIgnoreCase(provenienza))
              {
                storicoUnitaArboreaVO.setIdGenereIscrizione(
                    idGenereIscrizioneDefault);
              }
              else
              {
                if(ruoloUtenza.isUtentePA())
                {
                  storicoUnitaArboreaVO.setIdGenereIscrizione(
                      idGenereIscrizioneDefault);
                }
                else
                {
                  storicoUnitaArboreaVO.setIdGenereIscrizione(
                      idGenereIscrizioneProvvisorio);
                }
              }
              
              
              Long idStoricoUnitaArborea = storicoUnitaArboreaDAO
                  .insertStoricoUnitaArborea(storicoUnitaArboreaVO);
              // Inserisco il nuovo record su DB_ALTRO_VITIGNO
              AltroVitignoVO[] elencoAltriVitigni = storicoUnitaArboreaVO
                  .getElencoAltriVitigni();
              if (elencoAltriVitigni != null && elencoAltriVitigni.length > 0)
              {
                for (int a = 0; a < elencoAltriVitigni.length; a++)
                {
                  AltroVitignoVO altroVitignoVO = (AltroVitignoVO) elencoAltriVitigni[a];
                  altroVitignoVO
                      .setIdStoricoUnitaArborea(idStoricoUnitaArborea);
                  altroVitignoDAO.insertAltroVitigno(altroVitignoVO);
                }
              }
            }
            // Altrimenti, se è stato modificato il dato, effettuo solo UPDATE
            else if (!storicoUnitaArboreaVO.isEqual(oldStoricoUnitaArboreaVO))
            {
              storicoUnitaArboreaVO.setDataAggiornamento(new java.util.Date(
                  new Timestamp(System.currentTimeMillis()).getTime()));
              storicoUnitaArboreaVO.setIdUtenteAggiornamento(ruoloUtenza
                  .getIdUtente());
              storicoUnitaArboreaVO.setDataEsecuzione(null);
              storicoUnitaArboreaVO.setEsitoControllo(null);
              
              if(SolmrConstants.IMPORTA_CCIAA.equalsIgnoreCase(provenienza))
              {
                storicoUnitaArboreaVO.setIdGenereIscrizione(
                  idGenereIscrizioneDefault);
              }
              else
              {
                if(ruoloUtenza.isUtentePA())
                {
                  storicoUnitaArboreaVO.setIdGenereIscrizione(
                      idGenereIscrizioneDefault);
                }
                else
                {
                  storicoUnitaArboreaVO.setIdGenereIscrizione(
                      idGenereIscrizioneProvvisorio);
                }
              }
              
              storicoUnitaArboreaDAO
                  .updateStoricoUnitaArborea(storicoUnitaArboreaVO);
              // Elimino i precedenti altri vitigni e inserisco i nuovi
              AltroVitignoVO[] elencoAltriVitigni = storicoUnitaArboreaVO
                  .getElencoAltriVitigni();
              if (elencoAltriVitigni != null && elencoAltriVitigni.length > 0)
              {
                altroVitignoDAO
                    .deleteAltroVitignoByIdStoricoUnitaArborea(storicoUnitaArboreaVO
                        .getIdStoricoUnitaArborea());
                for (int a = 0; a < elencoAltriVitigni.length; a++)
                {
                  AltroVitignoVO altroVitignoVO = (AltroVitignoVO) elencoAltriVitigni[a];
                  altroVitignoVO.setIdStoricoUnitaArborea(storicoUnitaArboreaVO
                      .getIdStoricoUnitaArborea());
                  altroVitignoDAO.insertAltroVitigno(altroVitignoVO);
                }
              }
              else
              {
                // Li cancello nel caso ci fossero precedentemente e siano stati
                // eliminati
                altroVitignoDAO
                    .deleteAltroVitignoByIdStoricoUnitaArborea(storicoUnitaArboreaVO
                        .getIdStoricoUnitaArborea());
              }
            }
          }
        }
      }
      // Aggiorno lo stato dello schedario delle particelle su cui insistevano
      // UV selezionate
      particellaDAO
          .aggiornaSchedarioParticellaPlSql((BigDecimal[]) elencoIdParticella
              .toArray(new BigDecimal[elencoIdParticella.size()]));
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
    catch (SolmrException se)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(se.getMessage());
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(e.getMessage());
    }
  }

  /**
   * Metodo utilizzato per effettuare la cessazione delle unità arboree
   * 
   * @param elencoIdStoricoUnitaArboree
   * @param ruoloUtenza
   * @param idCausaleModifica
   * @param dataCessazione
   * @param note
   * @throws Exception
   */
  public void cessaUnitaArboree(Long[] elencoIdStoricoUnitaArboree,
      RuoloUtenza ruoloUtenza, Long idCausaleModifica, String note) throws Exception,
      SolmrException
  {
    try
    {
      
      //Inserisco le uv cessate come da richiestadi viti del 
      //27/10/2011 per evitare la sovrascrizione del campo
      // note ne caso di un estirpo parziale precedente
      
      //Data di fine validita nella 'uv da fare l'update
      // e data inizio validita nell'inserimento
      Date dataFineValidita =  new Date();
      GregorianCalendar dataCessazione = new GregorianCalendar();
      dataCessazione.setGregorianChange(dataFineValidita);
      dataCessazione.roll(Calendar.MINUTE, 1);
      Hashtable<Long,Long> elencoParticelle = new Hashtable<Long,Long>();
      Vector<BigDecimal> elencoIdParticella = new Vector<BigDecimal>();
      Vector<StoricoUnitaArboreaVO> elencoUv = new Vector<StoricoUnitaArboreaVO>();
      for (int i = 0; i < elencoIdStoricoUnitaArboree.length; i++)
      {
        Long idStoricoUnitaArborea = (Long) elencoIdStoricoUnitaArboree[i];
        // Recupero i dati dell'unità arborea
        StoricoParticellaVO storicoParticellaVO = storicoUnitaArboreaDAO
            .findStoricoParticellaArborea(idStoricoUnitaArborea);
        StoricoUnitaArboreaVO storicoUnitaArboreaVO = storicoParticellaVO
            .getStoricoUnitaArboreaVO();
        if (elencoParticelle.get(storicoUnitaArboreaVO.getIdParticella()) == null)
        {
          elencoParticelle.put(storicoUnitaArboreaVO.getIdParticella(),
              storicoUnitaArboreaVO.getIdParticella());
          elencoIdParticella.add(new BigDecimal(storicoUnitaArboreaVO
              .getIdParticella().longValue()));
        }
        // Cesso il record su DB_STORICO_UNITA_ARBOREA
        storicoUnitaArboreaVO.setDataInizioValidita(dataFineValidita);
        storicoUnitaArboreaVO.setDataCessazione(dataCessazione.getTime());
        storicoUnitaArboreaVO.setDataFineValidita(dataCessazione.getTime());
        storicoUnitaArboreaVO.setIdCessazioneUnar(idCausaleModifica);
        storicoUnitaArboreaVO.setNote(note);
        storicoUnitaArboreaVO.setDataAggiornamento(dataCessazione.getTime());
        storicoUnitaArboreaVO.setIdUtenteAggiornamento(ruoloUtenza
            .getIdUtente());
        storicoUnitaArboreaVO.setRecordModificato(SolmrConstants.FLAG_S);
        storicoUnitaArboreaVO.setEsitoControllo(null);
        storicoUnitaArboreaVO.setDataEsecuzione(null);
        if (ruoloUtenza.isUtenteIntermediario()
            || ruoloUtenza.isUtenteOPRGestore())
        {
          storicoUnitaArboreaVO
              .setStatoUnitaArborea(SolmrConstants.STATO_UV_PROPOSTO_CAA);
        }
        else if (ruoloUtenza.isUtenteRegionale()
            || ruoloUtenza.isUtenteProvinciale())
        {
          storicoUnitaArboreaVO
              .setStatoUnitaArborea(SolmrConstants.STATO_UV_VALIDATO_PA);
        }
        elencoUv.add(storicoUnitaArboreaVO);
      }
      
      // Effettuo la cessazione massiva delle UV selezionate
      storicoUnitaArboreaDAO
        .cessaAllStoricoUnitaArborea(elencoIdStoricoUnitaArboree, dataFineValidita);
      
      
      storicoUnitaArboreaDAO
          .insertAllStoricoUnitaArborea((StoricoUnitaArboreaVO[]) elencoUv
              .toArray(new StoricoUnitaArboreaVO[elencoUv.size()]));
      // Aggiorno lo stato dello schedario delle particelle su cui insistevano
      // UV selezionate
      particellaDAO
          .aggiornaSchedarioParticellaPlSql((BigDecimal[]) elencoIdParticella
              .toArray(new BigDecimal[elencoIdParticella.size()]));
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
    catch (SolmrException se)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(se.getMessage());
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(e.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce l'elenco delle cessazioni unar
   * 
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.TipoCessazioneUnarVO[]
   * @throws Exception
   */
  public TipoCessazioneUnarVO[] getListTipoCessazioneUnar(boolean onlyActive,
      String[] orderBy) throws Exception
  {
    try
    {
      return tipoCessazioneUnarDAO.getListTipoCessazioneUnar(onlyActive,
          orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi consente di recuperare l'elenco delle particelle importabili
   * 
   * @param idAzienda
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] getListStoricoParticelleArboreeImportabili(
      Long idAzienda, String[] orderBy) throws Exception
  {
    try
    {
      return storicoUnitaArboreaDAO.getListStoricoParticelleArboreeImportabili(
          idAzienda, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi consente di importare le unità arboree da schedario
   * 
   * @param elencoIdStoricoUnitaArborea
   * @param ruoloUtenza
   * @param anagAziendaVO
   * @param idCausaleModifica
   * @param newIdParticella
   * @throws Exception
   * @throws SolmrException
   */
  public void importUnitaArboreeBySchedario(Long[] elencoIdStoricoUnitaArborea,
      RuoloUtenza ruoloUtenza, AnagAziendaVO anagAziendaVO,
      Long idCausaleModifica, Long newIdParticella) throws Exception,
      SolmrException
  {
    try
    {
      Hashtable<Long,Long> elencoParticelle = new Hashtable<Long,Long>();
      Vector<BigDecimal> elencoIdParticella = new Vector<BigDecimal>();
      Vector<StoricoUnitaArboreaVO> elencoOldUv = new Vector<StoricoUnitaArboreaVO>();
      Vector<StoricoUnitaArboreaVO> elencoNewUv = new Vector<StoricoUnitaArboreaVO>();
      BigDecimal valueProgUnar = new BigDecimal(0);
      String progrUnar = null;
      
      Long idGenereIscrizioneDefault = tipoGenereIscrizioneDAO.getDefaultIdGenereIscrizione();
      //Long idGenereIscrizioneProvvisorio = tipoGenereIscrizioneDAO.getIdGenereIscrizioneByFlag("N");
      
      
      for (int i = 0; i < elencoIdStoricoUnitaArborea.length; i++)
      {
        Long idStoricoUnitaArborea = (Long) elencoIdStoricoUnitaArborea[i];
        // Cerco l'unità arborea
        StoricoParticellaVO storicoParticellaVO = storicoUnitaArboreaDAO
            .findStoricoParticellaArborea(idStoricoUnitaArborea);
        StoricoUnitaArboreaVO storicoUnitaArboreaVO = storicoParticellaVO
            .getStoricoUnitaArboreaVO();
        StoricoUnitaArboreaVO oldStoricoUnitaArboreaVO = new StoricoUnitaArboreaVO();
        oldStoricoUnitaArboreaVO.setIdStoricoUnitaArborea(storicoUnitaArboreaVO
            .getIdStoricoUnitaArborea());
        oldStoricoUnitaArboreaVO.setDataFineValidita(new java.util.Date(
            new Timestamp(System.currentTimeMillis()).getTime()));
        elencoOldUv.add(oldStoricoUnitaArboreaVO);
        // Calcolo il progr_unar
        if (i == 0)
        {
          progrUnar = storicoUnitaArboreaDAO.getProgressivoUnar(anagAziendaVO
              .getIdAzienda());
        }
        else
        {
          valueProgUnar = new BigDecimal(
              Integer.decode(progrUnar).intValue() + 1);
          progrUnar = valueProgUnar.toString();
        }
        // Inserisco il nuovo record su DB
        if (newIdParticella != null)
        {
          storicoUnitaArboreaVO.setIdParticella(newIdParticella);
        }
        storicoUnitaArboreaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
        storicoUnitaArboreaVO.setDataInizioValidita(new java.util.Date(
            new Timestamp(System.currentTimeMillis()).getTime()));
        storicoUnitaArboreaVO.setDataFineValidita(null);
        storicoUnitaArboreaVO.setDataCessazione(null);
        storicoUnitaArboreaVO.setIdCessazioneUnar(null);
        if (idCausaleModifica == null)
        {
          storicoUnitaArboreaVO
              .setIdCausaleModifica(SolmrConstants.ID_CAUSALE_MODIFICA_UNAR_IMPORTAZIONE_SCHEDARIO);
        }
        else
        {
          storicoUnitaArboreaVO.setIdCausaleModifica(idCausaleModifica);
        }
        storicoUnitaArboreaVO.setNote("Importazione da schedario del "
            + DateUtils.getCurrent());
        storicoUnitaArboreaVO.setDataAggiornamento(new java.util.Date(
            new Timestamp(System.currentTimeMillis()).getTime()));
        storicoUnitaArboreaVO.setIdUtenteAggiornamento(ruoloUtenza
            .getIdUtente());
        storicoUnitaArboreaVO.setEsitoControllo(null);
        storicoUnitaArboreaVO.setDataEsecuzione(null);
        storicoUnitaArboreaVO.setRecordModificato(SolmrConstants.FLAG_S);
        storicoUnitaArboreaVO.setProgrUnar(progrUnar);
        if (ruoloUtenza.isUtenteRegionale()
            || ruoloUtenza.isUtenteProvinciale())
        {
          storicoUnitaArboreaVO
              .setStatoUnitaArborea(SolmrConstants.STATO_UV_VALIDATO_PA);
        }
        
        
        
        if(ruoloUtenza.isUtentePA())
        {
          storicoUnitaArboreaVO.setIdGenereIscrizione(idGenereIscrizioneDefault);
        }
        
        
        
        elencoNewUv.add(storicoUnitaArboreaVO);
        if (elencoParticelle.get(storicoUnitaArboreaVO.getIdParticella()) == null)
        {
          elencoParticelle.put(storicoUnitaArboreaVO.getIdParticella(),
              storicoUnitaArboreaVO.getIdParticella());
          elencoIdParticella.add(new BigDecimal(storicoUnitaArboreaVO
              .getIdParticella().longValue()));
        }
      }
      // Storicizzo tutte le UV precedentemente selezionate
      storicoUnitaArboreaDAO
          .storicizzaAllStoricoUnitaArborea((StoricoUnitaArboreaVO[]) elencoOldUv
              .toArray(new StoricoUnitaArboreaVO[elencoOldUv.size()]));
      // Inserisco tutte le UV precedentemente selezionate
      storicoUnitaArboreaDAO
          .insertAllStoricoUnitaArborea((StoricoUnitaArboreaVO[]) elencoNewUv
              .toArray(new StoricoUnitaArboreaVO[elencoNewUv.size()]));
      // Aggiorno lo stato dello schedario delle particelle su cui insistevano
      // UV selezionate
      particellaDAO
          .aggiornaSchedarioParticellaPlSql((BigDecimal[]) elencoIdParticella
              .toArray(new BigDecimal[elencoIdParticella.size()]));
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
    catch (SolmrException se)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(se.getMessage());
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(e.getMessage());
    }
  }

  /**
   * Metodo utilizzato per inserire un'unità arborea
   * 
   * @param storicoUnitaArboreaVO
   * @param storicoParticellaVO
   * @param anagAziendaVO
   * @param ruoloUtenza
   * @throws Exception
   * @throws SolmrException
   */
  public void inserisciUnitaArborea(
      StoricoUnitaArboreaVO storicoUnitaArboreaVO,
      StoricoParticellaVO storicoParticellaVO, AnagAziendaVO anagAziendaVO,
      RuoloUtenza ruoloUtenza, String provenienza) throws Exception, SolmrException
  {
    try
    {
      // Inserisco il record su DB_UNITA_ARBOREA
      UnitaArboreaVO unitaArboreaVO = new UnitaArboreaVO();
      unitaArboreaVO.setDataInizioValidita(new java.util.Date(new Timestamp(
          System.currentTimeMillis()).getTime()));
      Long idUnitaArborea = unitaArboreaDAO.insertUnitaArborea(unitaArboreaVO);
      // Calcolo il progr_unar
      String progrUnar = storicoUnitaArboreaDAO
          .getProgressivoUnar(anagAziendaVO.getIdAzienda());
      // Inserisco il record su DB_STORICO_UNITA_ARBOREA
      storicoUnitaArboreaVO.setIdUnitaArborea(idUnitaArborea);
      storicoUnitaArboreaVO.setIdParticella(storicoParticellaVO
          .getIdParticella());
      storicoUnitaArboreaVO.setProgrUnar(progrUnar);
      storicoUnitaArboreaVO.setDataInizioValidita(new java.util.Date(
          new Timestamp(System.currentTimeMillis()).getTime()));
      storicoUnitaArboreaVO
          .setIdTipologiaUnar(SolmrConstants.ID_TIPOLOGIA_UNAR_VINO);
      storicoUnitaArboreaVO.setCampagna(DateUtils.getCurrentYear().toString());
      storicoUnitaArboreaVO.setDataAggiornamento(new java.util.Date(
          new Timestamp(System.currentTimeMillis()).getTime()));
      storicoUnitaArboreaVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
      storicoUnitaArboreaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
      storicoUnitaArboreaVO.setIdFonte(SolmrConstants.ID_FONTE_SIAP);
      if (ruoloUtenza.isUtenteIntermediario()
          || ruoloUtenza.isUtenteOPRGestore())
      {
        storicoUnitaArboreaVO
            .setStatoUnitaArborea(SolmrConstants.STATO_UV_PROPOSTO_CAA);
      }
      else if (ruoloUtenza.isUtenteRegionale()
          || ruoloUtenza.isUtenteProvinciale())
      {
        if(SolmrConstants.CONFERMA_PA.equalsIgnoreCase(provenienza))
        {
          storicoUnitaArboreaVO
            .setStatoUnitaArborea(SolmrConstants.STATO_UV_PROPOSTO_CAA);
        }
        else
        {
          storicoUnitaArboreaVO
            .setStatoUnitaArborea(SolmrConstants.STATO_UV_VALIDATO_PA);
        }        
      }
      
      if(SolmrConstants.CONFERMA_PA.equalsIgnoreCase(provenienza))
      {
        if(Validator.isEmpty(storicoUnitaArboreaVO.getIdGenereIscrizione()))
        {
          storicoUnitaArboreaVO.setIdGenereIscrizione(
              tipoGenereIscrizioneDAO.getDefaultIdGenereIscrizione());
        }
        //lascio quello che è già presente caso duplica
        else
        {}
      }
      else
      {
        if(ruoloUtenza.isUtentePA())
        {
          storicoUnitaArboreaVO.setIdGenereIscrizione(
              tipoGenereIscrizioneDAO.getDefaultIdGenereIscrizione());
        }
        else
        {
          storicoUnitaArboreaVO.setIdGenereIscrizione(
              tipoGenereIscrizioneDAO.getIdGenereIscrizioneByFlag("N"));
        }
      }
      
      
      Long idStoricoUnitaArborea = storicoUnitaArboreaDAO
          .insertStoricoUnitaArborea(storicoUnitaArboreaVO);
      // Inserisco il nuovo record su DB_ALTRO_VITIGNO
      AltroVitignoVO[] elencoAltriVitigni = storicoUnitaArboreaVO
          .getElencoAltriVitigni();
      if (elencoAltriVitigni != null && elencoAltriVitigni.length > 0)
      {
        for (int a = 0; a < elencoAltriVitigni.length; a++)
        {
          AltroVitignoVO altroVitignoVO = (AltroVitignoVO) elencoAltriVitigni[a];
          altroVitignoVO.setIdStoricoUnitaArborea(idStoricoUnitaArborea);
          altroVitignoDAO.insertAltroVitigno(altroVitignoVO);
        }
      }
      // Aggiorno lo stato dello schedario delle particelle su cui insistevano
      // UV selezionate
      BigDecimal[] elencoId = { new BigDecimal(storicoUnitaArboreaVO
          .getIdParticella().longValue()) };
      particellaDAO.aggiornaSchedarioParticellaPlSql(elencoId);
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
    catch (SolmrException se)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(se.getMessage());
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(e.getMessage());
    }
  }

  /**
   * Metodo utilizzato per ricercare le UV
   * nella stampa
   * 
   * @param idAzienda
   * @param filtriUnitaArboreaRicercaVO
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] searchStoricoUnitaArboreaByParametersForStampa(
      Long idAzienda, FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO,
      String[] orderBy) throws Exception
  {
    try
    {
      if (filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() <= 0)
      {
        return storicoUnitaArboreaDAO.searchStoricoUnitaArboreaByParametersForStampa(
            idAzienda, filtriUnitaArboreaRicercaVO, orderBy);
      }
      else
      {
        ConsistenzaVO consistenzaVO = consistenzaDAO
            .findDichiarazioneConsistenzaByPrimaryKey(filtriUnitaArboreaRicercaVO
                .getIdPianoRiferimento());
        filtriUnitaArboreaRicercaVO
            .setDataInserimentoDichiarazione(consistenzaVO
                .getDataInserimentoDichiarazione());
        return unitaArboreaDichiarataDAO
            .searchUnitaArboreaDichiarataByParametersForStampa(idAzienda,
                filtriUnitaArboreaRicercaVO, orderBy);
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  
  /**
   * Metodo utilizzato per ricercare le UV
   * 
   * @param idAzienda
   * @param filtriUnitaArboreaRicercaVO
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] searchStoricoUnitaArboreaByParameters(String nomeLib,
      Long idAzienda, FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO,
      String[] orderBy) throws Exception
  {
    try
    {
      if (filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() <= 0)
      {
        String parametroP26 = "";
        BigDecimal p26Valore = null;
        try
        {
          parametroP26 = commonDAO.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_P26P);
        }
        catch (SolmrException sme)
        {
          throw new Exception(sme.getMessage());
        }
        
        try
        {
          parametroP26 = parametroP26.replace(',','.');
          p26Valore = new BigDecimal(parametroP26);
        }
        catch (Exception sme)
        {
          p26Valore = new BigDecimal(0);
        }
        
        return storicoUnitaArboreaDAO.searchStoricoUnitaArboreaByParameters(nomeLib,
            idAzienda, filtriUnitaArboreaRicercaVO, p26Valore, orderBy);
      }
      else
      {
        ConsistenzaVO consistenzaVO = consistenzaDAO
            .findDichiarazioneConsistenzaByPrimaryKey(filtriUnitaArboreaRicercaVO
                .getIdPianoRiferimento());
        filtriUnitaArboreaRicercaVO
            .setDataInserimentoDichiarazione(consistenzaVO
                .getDataInserimentoDichiarazione());
        return unitaArboreaDichiarataDAO
            .searchUnitaArboreaDichiarataByParameters(idAzienda,
                filtriUnitaArboreaRicercaVO, orderBy);
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi consente di effettuare la ricerca delle UV in funzione del
   * loro scarico in excel
   * 
   * @param idAzienda
   * @param idPianoRiferimento
   * @param orderBy
   * @return java.util.Vector
   * @throws Exception
   */
  public Vector<StoricoParticellaArboreaExcelVO> searchStoricoUnitaArboreaExcelByParameters(String nomeLib, Long idAzienda,
      FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO)
      throws Exception
  {
    try
    {
      
      String parametroPTUV = null;
      String parametroMTUV = null;
      try
      {
        parametroPTUV = commonDAO.getValoreFromParametroByIdCodeNullValue(SolmrConstants.PARAMETRO_PTUV);
      }
      catch (DataAccessException se)
      {}
      try
      {
        parametroMTUV = commonDAO.getValoreFromParametroByIdCodeNullValue(SolmrConstants.PARAMETRO_MTUV);
      }
      catch (DataAccessException se)
      {}
      
      if(parametroPTUV == null)
      {
        parametroPTUV = "1";
      }
      if(parametroMTUV == null)
      {
        parametroMTUV = "1,5";
      }
      
      
      
      Vector<StoricoParticellaArboreaExcelVO> vExcel = null;
      if (filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() <= 0)
      {
        vExcel = storicoUnitaArboreaDAO
            .searchStoricoUnitaArboreaExcelByParameters(nomeLib, idAzienda,
                filtriUnitaArboreaRicercaVO, parametroPTUV, parametroMTUV);
        //aggiunta sup.post.allineaGis
        Long idParticellaTmp = new Long(0);
        BigDecimal superficieGis = new BigDecimal(0);
        BigDecimal sommaAreaUV = new BigDecimal(0);
        boolean trovataEleggibilita = false;
        for(int i=0;i<vExcel.size();i++)
        {
          StoricoParticellaArboreaExcelVO excelVO = vExcel.get(i);          
          Long idParticella = excelVO.getIdParticella();
          if(idParticellaTmp.compareTo(idParticella) !=0)
          {
            sommaAreaUV = new BigDecimal(0);
            if((excelVO.getIdParticellaCertificata() != null) && (excelVO.getIdCatalogoMatrice() != null))
            {
              superficieGis = storicoUnitaArboreaGaaDAO.getSupEleggibilePlSql(
                  excelVO.getIdParticellaCertificata(), excelVO.getIdCatalogoMatrice());
            }
            if(superficieGis.compareTo(new BigDecimal(0)) > 0)
            {
              trovataEleggibilita = true;
              
              //Mi ricavo la perncentuale utilizzo eleggibile a gis
              BigDecimal percentualePossesso = storicoUnitaArboreaGaaDAO.getPercUtilizzoEleggibile(
                  idAzienda, idParticella.longValue());
              
              if(percentualePossesso.compareTo(new BigDecimal(0)) == 0)
              {
                percentualePossesso = conduzioneParticellaDAO
                    .getPercentualePosesso(idAzienda, idParticella.longValue());
              }
              
              if(percentualePossesso.compareTo(new BigDecimal(100)) > 0)
              {
                percentualePossesso = new BigDecimal(100);
              }
               
              superficieGis = superficieGis.divide(new BigDecimal(100));
              superficieGis = superficieGis.multiply(percentualePossesso);
              sommaAreaUV = storicoUnitaArboreaDAO.getSumAreaUVParticella(idAzienda, idParticella.longValue());
              
            }
            else
            {
              trovataEleggibilita = false;
            }
          }
          
         /* if(trovataEleggibilita && 
           ((excelVO.getCodTolleranza().compareTo(SolmrConstants.IN_TOLLERANZA) == 0)
            || (excelVO.getCodTolleranza().compareTo(SolmrConstants.FUORI_TOLLERANZA) == 0)))
          {
             BigDecimal areaUV = new BigDecimal(excelVO.getArea().replace(',','.'));
             //riprorziono l'area della UV                
             areaUV = areaUV.multiply(superficieGis);
             areaUV = areaUV.divide(sommaAreaUV,4,BigDecimal.ROUND_HALF_UP);
             excelVO.setSupPostAllinea(StringUtils.parseSuperficieFieldBigDecimal(areaUV));
          }*/
               
          
          idParticellaTmp = idParticella;
           
        } //for
        
        
      }
      else
      {
        ConsistenzaVO consistenzaVO = consistenzaDAO
            .findDichiarazioneConsistenzaByPrimaryKey(filtriUnitaArboreaRicercaVO
                .getIdPianoRiferimento());
        filtriUnitaArboreaRicercaVO
            .setDataInserimentoDichiarazione(consistenzaVO
                .getDataInserimentoDichiarazione());
        vExcel =  unitaArboreaDichiarataDAO
            .searchUnitaArboreaDichiarataExcelByParameters(idAzienda,
                filtriUnitaArboreaRicercaVO, parametroPTUV, parametroMTUV);
      }
      
      return vExcel;
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce l'elenco delle conduzioni a partire
   * dall'id_azienda
   * 
   * @param Long
   *          idAzienda
   * @param boolean
   *          onlyActive
   * @param String[]
   *          orderBy
   * @return it.csi.solmr.dto.anag.terreni.ConduzioneParticellaVO[]
   * @throws Exception
   */
  public ConduzioneParticellaVO[] getListConduzioneParticellaByIdAzienda(
      Long idAzienda, boolean onlyActive, String[] orderBy)
      throws Exception
  {
    try
    {
      return conduzioneParticellaDAO.getListConduzioneParticellaByIdAzienda(
          idAzienda, onlyActive, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo utilizzato per importare le particelle da un'altra azienda
   * 
   * @param elencoConduzioni
   * @param idUte
   * @param anagAziendaVO
   * @param anagAziendaSearchVO
   * @param ruoloUtenza
   * @throws Exception
   */
  public void importParticelle(String[] elencoConduzioni, Long idUte,
      AnagAziendaVO anagAziendaVO, AnagAziendaVO anagAziendaSearchVO,
      RuoloUtenza ruoloUtenza, Long idTitoloPossesso) throws Exception
  {
    try
    {      
      Long idGenereIscrizioneDefault = tipoGenereIscrizioneDAO.getDefaultIdGenereIscrizione();
      Long idGenereIscrizioneProvvisorio = tipoGenereIscrizioneDAO.getIdGenereIscrizioneByFlag("N");
      
      java.util.Date dataCessazione = new java.util.Date(new Timestamp(System
          .currentTimeMillis()).getTime());
      
      Hashtable<Object,Object> elencoIdParticella=new Hashtable<Object,Object>();
      
      // Ciclo sulle conduzioni selezionate
      for (int i = 0; i < elencoConduzioni.length; i++)
      {
        Long idConduzione = Long.decode((String) elencoConduzioni[i]);
        // Ricerco la particella sul DB
        ConduzioneParticellaVO conduzioneParticellaVO = conduzioneParticellaDAO
            .findConduzioneParticellaByPrimaryKey(idConduzione);
        if (Validator.isNotEmpty(conduzioneParticellaVO
            .getSuperficieAgronomica()))
        {
          conduzioneParticellaVO.setSuperficieAgronomica(StringUtils
              .parseSuperficieField(conduzioneParticellaVO
                  .getSuperficieAgronomica()));
        }
        // Se la conduzione risulta attiva la cesso
        if (conduzioneParticellaVO.getDataFineConduzione() == null)
        {
          // Cesso il record su DB_CONDUZIONE_PARTICELLA
          conduzioneParticellaVO.setDataFineConduzione(dataCessazione);
          conduzioneParticellaVO.setDataAggiornamento(new java.util.Date(
              new Timestamp(System.currentTimeMillis()).getTime()));
          conduzioneParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
              .getIdUtente());
          conduzioneParticellaVO.setRecordModificato(SolmrConstants.FLAG_S);
          conduzioneParticellaVO.setEsitoControllo(null);
          conduzioneParticellaVO.setDataEsecuzione(null);
          conduzioneParticellaVO
              .setNote("Cessazione per trasferimento particelle all'azienda con CUAA "
                  + anagAziendaVO.getCUAA().toUpperCase());
          conduzioneParticellaDAO
              .updateConduzioneParticella(conduzioneParticellaVO);
          // Verifico se ci sono altre conduzioni attive associate all'azienda
          // di origine
          ConduzioneParticellaVO[] elencoConduzioniAttive = conduzioneParticellaDAO
              .getListConduzioneParticellaByIdAziendaAndIdParticella(
                  anagAziendaSearchVO.getIdAzienda(), conduzioneParticellaVO
                      .getIdParticella(), true, null);
          // Se non ci sono
          if (elencoConduzioniAttive == null
              || elencoConduzioniAttive.length == 0)
          {
            // Recupero le unità arboree associate alla particella e all'azienda
            // di origine
            StoricoUnitaArboreaVO[] elencoUnitaArboree = storicoUnitaArboreaDAO
                .getListStoricoUnitaArboreaByLogicKey(conduzioneParticellaVO
                    .getIdParticella(), new Long(-1), anagAziendaSearchVO
                    .getIdAzienda(), null);
            // Se ce ne sono
            if (elencoUnitaArboree != null && elencoUnitaArboree.length > 0)
            {              
            	elencoIdParticella.put(conduzioneParticellaVO.getIdParticella(),new BigDecimal(conduzioneParticellaVO.getIdParticella()));
              // Le storicizzo
              String progrUnar=null;
              for (int a = 0; a < elencoUnitaArboree.length; a++)
              {
                StoricoUnitaArboreaVO storicoUnitaArboreaVO = (StoricoUnitaArboreaVO) elencoUnitaArboree[a];
                String oldStatoUnitaArborea = storicoUnitaArboreaVO.getStatoUnitaArborea();
                // Imposto data fine validita
                storicoUnitaArboreaVO.setDataFineValidita(new java.util.Date(
                    new Timestamp(System.currentTimeMillis()).getTime()));
                storicoUnitaArboreaVO.setDataAggiornamento(new java.util.Date(
                    new Timestamp(System.currentTimeMillis()).getTime()));
                storicoUnitaArboreaVO
                    .setNote("Cessazione per trasferimento particelle all'azienda con CUAA "
                        + anagAziendaVO.getCUAA().toUpperCase());
                storicoUnitaArboreaDAO
                    .updateStoricoUnitaArborea(storicoUnitaArboreaVO);
                // Inserisco nuovo record su DB_STORICO_UNITA_ARBOREA
                storicoUnitaArboreaVO.setDataFineValidita(null);
                storicoUnitaArboreaVO.setIdStoricoUnitaArborea(null);
                storicoUnitaArboreaVO.setDataInizioValidita(new java.util.Date(
                    new Timestamp(System.currentTimeMillis()).getTime()));
                storicoUnitaArboreaVO.setDataAggiornamento(new java.util.Date(
                    new Timestamp(System.currentTimeMillis()).getTime()));
                storicoUnitaArboreaVO.setIdUtenteAggiornamento(ruoloUtenza
                    .getIdUtente());
                storicoUnitaArboreaVO.setRecordModificato(null);
                storicoUnitaArboreaVO.setDataEsecuzione(null);
                storicoUnitaArboreaVO.setEsitoControllo(null);
                storicoUnitaArboreaVO
                    .setIdAzienda(anagAziendaVO.getIdAzienda());
                storicoUnitaArboreaVO
                    .setNote("Importazione dall'azienda con CUAA "
                        + anagAziendaSearchVO.getCUAA().toUpperCase());
                  
                //Calcolo il progr_unar, per evitare di importare particelle che presentano lo stesso
                if (a == 0)
                  progrUnar = storicoUnitaArboreaDAO.getProgressivoUnar(anagAziendaVO.getIdAzienda());
                else
                  progrUnar = new BigDecimal(Integer.decode(progrUnar).intValue() + 1).toString();
                storicoUnitaArboreaVO.setProgrUnar(progrUnar);
                
                
                if (ruoloUtenza.isUtenteIntermediario()
                    || ruoloUtenza.isUtenteOPRGestore())
                {
                  if(oldStatoUnitaArborea != null)
                  {
                    storicoUnitaArboreaVO.setStatoUnitaArborea(oldStatoUnitaArborea);
                  }
                  else
                  {
                    storicoUnitaArboreaVO
                      .setStatoUnitaArborea(SolmrConstants.STATO_UV_PROPOSTO_CAA);
                  }
                }
                else if (ruoloUtenza.isUtenteRegionale()
                   || ruoloUtenza.isUtenteProvinciale())
                {
                  storicoUnitaArboreaVO
                    .setStatoUnitaArborea(SolmrConstants.STATO_UV_VALIDATO_PA);
                }
                
                
                
                
                if(ruoloUtenza.isUtentePA())
                {
                  storicoUnitaArboreaVO.setIdGenereIscrizione(idGenereIscrizioneDefault);
                }
                else
                {
                  storicoUnitaArboreaVO.setIdGenereIscrizione(idGenereIscrizioneProvvisorio);
                }
                
                storicoUnitaArboreaDAO
                    .insertStoricoUnitaArborea(storicoUnitaArboreaVO);
              }
            }
          }
          // Controllo se ci sono dei fabbricati attivi relativi alla particella
          // e all'azienda di origine
          FabbricatoParticellaVO[] elencoFabbricati = fabbricatoDAO
              .getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella(
                  conduzioneParticellaVO.getIdConduzioneParticella(),
                  anagAziendaSearchVO.getIdAzienda(), null, true);
          if (elencoFabbricati != null && elencoFabbricati.length > 0)
          {
            // Se li ho trovati li cesso
            for (int b = 0; b < elencoFabbricati.length; b++)
            {
              FabbricatoParticellaVO fabbricatoParticellaVO = (FabbricatoParticellaVO) elencoFabbricati[b];
              fabbricatoParticellaVO.setDataFineValidita(new java.util.Date(
                  new Timestamp(System.currentTimeMillis()).getTime()));
              fabbricatoDAO.updateFabbricatoParticella(fabbricatoParticellaVO);
            }
          }
          // Recupero i documenti attivi legati alla conduzione storicizzata
          Vector<DocumentoVO> elencoDocumenti = documentoDAO
              .getListDocumentiByIdConduzioneParticella(conduzioneParticellaVO
                  .getIdConduzioneParticella(), true);
          // Se ce ne sono
          if (elencoDocumenti != null && elencoDocumenti.size() > 0)
          {
            Vector<DocumentoConduzioneVO> elencoDocumentoConduzioni = null;
            for (int c = 0; c < elencoDocumenti.size(); c++)
            {
              DocumentoVO documentoVO = (DocumentoVO) elencoDocumenti
                  .elementAt(c);
              // Ricerco le conduzioni attive legate al precedente documento
              elencoDocumentoConduzioni = documentoDAO
                  .getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella(
                      documentoVO.getIdDocumento(), conduzioneParticellaVO
                          .getIdConduzioneParticella(), true);
              // Se ne trovo e sono diverse da doc contradditorio
              if(documentoVO.getFaseIstanzaRiesame() != SolmrConstants.FASE_IST_RIESAM_CONTRO)
              {
                if (elencoDocumentoConduzioni != null
                    && elencoDocumentoConduzioni.size() > 0)
                {
                  for (int d = 0; d < elencoDocumentoConduzioni.size(); d++)
                  {
                    DocumentoConduzioneVO documentoConduzioneVO = (DocumentoConduzioneVO) elencoDocumentoConduzioni
                        .elementAt(d);
                    // Storicizzo i records presenti su DB_DOCUMENTO_CONDUZIONE
                    documentoConduzioneVO.setDataFineValidita(new java.util.Date(
                        new Timestamp(System.currentTimeMillis()).getTime()));
                    documentoDAO.updateDocumentoConduzione(documentoConduzioneVO);
                  }
                }
              } //if  != Contro
              
              
              if(documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_FOTO)
              {
                
                if (elencoDocumentoConduzioni != null && elencoDocumentoConduzioni.size() > 0)
                {
                  if(!documentoGaaDAO.existAltroLegameIstRiesameParticella(
                     anagAziendaSearchVO.getIdAzienda(), conduzioneParticellaVO.getIdParticella().longValue(), 
                     DateUtils.getCurrentYear().intValue(), 
                     documentoVO.getExtIdDocumento().longValue()))
                  {                 
                    
                    Long idIstanaRiesame = documentoGaaDAO.getIstRiesameParticellaFaseAnno(
                        anagAziendaSearchVO.getIdAzienda(), conduzioneParticellaVO.getIdParticella().longValue(), 
                        DateUtils.getCurrentYear().intValue(), documentoVO.getFaseIstanzaRiesame());
                    if(Validator.isNotEmpty(idIstanaRiesame))
                    {
                      Vector<Long> vIdIstanzaRiesame = new Vector<Long>();
                      vIdIstanzaRiesame.add(idIstanaRiesame);
                      documentoGaaDAO.annullaIstanzaFromId(vIdIstanzaRiesame, ruoloUtenza.getIdUtente());
                      PlSqlCodeDescription plCode = particellaGaaDAO.annullaIstanzaPlSql(documentoVO.getIdAzienda().longValue(), 
                          DateUtils.extractYearFromDate(documentoVO.getDataInserimento()), documentoVO.getFaseIstanzaRiesame(), 
                          ruoloUtenza.getIdUtente().longValue());
                      
                      if(plCode !=null)
                      {
                        if(Validator.isNotEmpty(plCode.getDescription()))
                        {
                          throw new DataAccessException("Aggiorna documento errore al plsql annullaIstanzaRiesame: "+plCode.getDescription()+"-"
                              +plCode.getOtherdescription());       
                        }
                      }
                      
                      
                    }
                      
                  }                
                  
                }
                
                
                //cessazione del documento
                Vector<DocumentoConduzioneVO> vDocumentoConduzione = documentoDAO
                    .getListDocumentoConduzioni(documentoVO.getIdDocumento(), false);
                Vector<Long> vIdParticella = null;
                boolean trovataUnaValida = false;
                for(int a=0;a<vDocumentoConduzione.size();a++)
                {
                  if(Validator.isEmpty(vDocumentoConduzione.get(a).getDataFineValidita()))
                  {
                    if(!trovataUnaValida)
                      trovataUnaValida = true;                 
                  }
                  else
                  {
                    if(vIdParticella == null)
                    {
                      vIdParticella = new Vector<Long>();
                    }
                    if(!vIdParticella.contains(vDocumentoConduzione.get(a).getIdParticella()))
                    {
                      vIdParticella.add(vDocumentoConduzione.get(a).getIdParticella());
                    }
                  }                
                }
                //non ho trovato conduzioni attive sul documento
                if(!trovataUnaValida)
                {
                  Long idStatoDocumento = null;
                  if(documentoGaaDAO.isAllIstanzaAnnullata(anagAziendaSearchVO.getIdAzienda(), vIdParticella, 
                      DateUtils.getCurrentYear().intValue(), documentoVO.getFaseIstanzaRiesame()))
                  {
                    idStatoDocumento = new Long(1);
                  }               
                  documentoDAO.updateDocumentoIstanza(documentoVO.getIdDocumento(), idStatoDocumento);
                  
                }
                
              }
              
              
            }  // for elenco documenti
          } //if elenco documenti
          
          
          
        }
        conduzioneParticellaVO.setIdUte(idUte);
        conduzioneParticellaVO.setNote("Importazione dall'azienda con CUAA "
            + anagAziendaSearchVO.getCUAA().toUpperCase());
        conduzioneParticellaVO.setDataInizioConduzione(new java.util.Date(
            System.currentTimeMillis()));
        conduzioneParticellaVO.setDataFineConduzione(null);
        conduzioneParticellaVO.setDataAggiornamento(new java.util.Date(
            new Timestamp(System.currentTimeMillis()).getTime()));
        conduzioneParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
            .getIdUtente());
        conduzioneParticellaVO.setEsitoControllo(null);
        conduzioneParticellaVO.setDataEsecuzione(null);
        conduzioneParticellaVO.setRecordModificato(SolmrConstants.FLAG_S);
        
        double totSupUtilizzata = utilizzoParticellaDAO
          .getTotSupUtilizzataByIdConduzioneParticella(idConduzione);
        String strSupCondotta = StringUtils.parseSuperficieField(new Double(totSupUtilizzata).toString());
        conduzioneParticellaVO.setSuperficieCondotta(strSupCondotta);
        
        
        //Imposto il titolo di possesso selezionato dall'utente
        conduzioneParticellaVO.setIdTitoloPossesso(idTitoloPossesso);
        
        //calcolo la percentualePossesso
        double percentualePossessoDb = 100;
        StoricoParticellaVO strVO = storicoParticellaDAO
          .findStoricoParticellaVOByIdConduzioneParticella(idConduzione);
        String supConfronto = strVO.getSupCatastale();
        if(Double.parseDouble(supConfronto.replace(',', '.')) == 0)
        {
          supConfronto = strVO.getSuperficieGrafica();
        }
        
        if((Double.parseDouble(supConfronto.replace(',', '.')) > 0)
          && (totSupUtilizzata > 0))
        {
          double supConfrontoDb = Double.parseDouble(supConfronto.replace(',', '.'));             
          percentualePossessoDb = 100.0 * totSupUtilizzata / supConfrontoDb; 
          percentualePossessoDb = NumberUtils.arrotonda(percentualePossessoDb,2);
          if(percentualePossessoDb > 100)
          {
            percentualePossessoDb = 100;
          }
        }
        conduzioneParticellaVO.setPercentualePossesso(new BigDecimal(percentualePossessoDb));
        
        
        
        
        Long newIdConduzioneParticella = conduzioneParticellaDAO
            .insertConduzioneParticella(conduzioneParticellaVO);
        // Ricerco gli utilizzi relativi alla vecchia particella selezionata
        UtilizzoParticellaVO[] elencoUtilizzi = utilizzoParticellaDAO
            .getListUtilizzoParticellaVOByIdConduzioneParticella(idConduzione,
                null, false);
        if (elencoUtilizzi != null && elencoUtilizzi.length > 0)
        {
          for (int j = 0; j < elencoUtilizzi.length; j++)
          {
            UtilizzoParticellaVO utilizzoParticellaVO = (UtilizzoParticellaVO) elencoUtilizzi[j];
            //Controllo gli utilizzi consociati
            Vector<UtilizzoConsociatoVO> vUtilizzoConsociatoVO = utilizzoConsociatoDAO
                .getUtilizziConsociatiByIdUtilizzoParticellaForImport(utilizzoParticellaVO.getIdUtilizzoParticella());
            utilizzoParticellaVO
                .setIdConduzioneParticella(newIdConduzioneParticella);
            utilizzoParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
                .getIdUtente());
            utilizzoParticellaVO.setAnno(DateUtils.getCurrentYear().toString());
            Long idUtilizzoParticella = utilizzoParticellaDAO.insertUtilizzoParticella(utilizzoParticellaVO);
            //se esitono li ribalto...
            if(vUtilizzoConsociatoVO != null)
            {
              for(int k=0;k<vUtilizzoConsociatoVO.size();k++)
              {
                UtilizzoConsociatoVO utilizzoConsociatoVO = vUtilizzoConsociatoVO.get(k);
                utilizzoConsociatoVO.setIdUtilizzoParticella(idUtilizzoParticella);
                utilizzoConsociatoDAO.insertUtilizzoConsociato(utilizzoConsociatoVO);
              }              
            }
          }
        }
      }
      
      
      // Aggiorno lo stato dello schedario delle particelle su cui insistevano
      // UV selezionate
      if (elencoIdParticella.size()<=100)
      	particellaDAO.aggiornaSchedarioParticellaPlSql((BigDecimal[]) elencoIdParticella.values().toArray(new BigDecimal[elencoIdParticella.size()]));
      
      
      
    }
    catch (Exception dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi consente di estrarre le unità vitate in funzione della chiave
   * logica della particella alla quale sono legate
   * 
   * @param istatComune
   * @param sezione
   * @param foglio
   * @param particella
   * @param subalterno
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   * @throws SolmrException
   */
  public StoricoParticellaVO[] getListStoricoParticelleArboreeByParameters(
      String istatComune, String sezione, String foglio, String particella,
      String subalterno, boolean onlyActive, String[] orderBy)
      throws Exception, SolmrException
  {
    try
    {
      return storicoUnitaArboreaDAO
          .getListStoricoParticelleArboreeByParameters(istatComune, sezione,
              foglio, particella, subalterno, onlyActive, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi consente di recuperare il dettaglio dell'unità arborea
   * dichiarata
   * 
   * @param idUnitaArboreaDichiarata
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO
   * @throws Exception
   */
  public StoricoParticellaVO findParticellaArboreaDichiarata(
      Long idUnitaArboreaDichiarata) throws Exception
  {
    
    StoricoParticellaVO storicoParticellaVO = null;
    try
    {
      storicoParticellaVO =  unitaArboreaDichiarataDAO
          .findParticellaArboreaDichiarata(idUnitaArboreaDichiarata);
      
      
      
      if(Validator.isNotEmpty(storicoParticellaVO
          .getUnitaArboreaDichiarataVO().getTipoTipologiaVinoVO()))
      {
        if(Validator.isNotEmpty(storicoParticellaVO.getUnitaArboreaDichiarataVO().getIdVigna()))
        {
          storicoParticellaVO.getUnitaArboreaDichiarataVO()
            .setVignaVO(tipoTipologiaVinoDAO.getVignaByByPrimary(
                storicoParticellaVO.getUnitaArboreaDichiarataVO().getIdVigna()));
          
          /*storicoParticellaVO.getUnitaArboreaDichiarataVO().getTipoTipologiaVinoVO()
          .setVignaVO(tipoTipologiaVinoDAO.getVignaByByPrimary(
              storicoParticellaVO.getUnitaArboreaDichiarataVO().getIdVigna()));*/
        }
      }
      
      
      if(Validator.isNotEmpty(storicoParticellaVO.getUnitaArboreaDichiarataVO().getIdMenzioneGeografica()))
      {
        TipoMenzioneGeograficaVO tipoMenzioneGeograficaVO = 
          tipoMenzioneGeograficaDAO.getTipoMenzioneGeografica(
              storicoParticellaVO.getUnitaArboreaDichiarataVO().getIdMenzioneGeografica().longValue());
        Vector<TipoMenzioneGeograficaVO> vTipoMenzioneGeograficaVO = new Vector<TipoMenzioneGeograficaVO>();
        vTipoMenzioneGeograficaVO.add(tipoMenzioneGeograficaVO);
        storicoParticellaVO.getUnitaArboreaDichiarataVO().setElencoMenzioneGeografica(vTipoMenzioneGeograficaVO);
      }
      else
      {
        storicoParticellaVO.getUnitaArboreaDichiarataVO().setElencoMenzioneGeografica(
          tipoMenzioneGeograficaDAO.getListTipoMenzioneGeografica(
              storicoParticellaVO.getIdParticella().longValue(), 
              storicoParticellaVO.getUnitaArboreaDichiarataVO().getIdTipologiaVino(), null));
      }
      
      return storicoParticellaVO;
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public StoricoParticellaVO findParticellaArboreaDichiarataBasic(Long idUnitaArboreaDichiarata)
      throws Exception
  {
    try
    {
      return unitaArboreaDichiarataDAO.findParticellaArboreaDichiarataBasic(idUnitaArboreaDichiarata);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce l'elenco degli altri vitigni dichiarati associati
   * all'unità arborea
   * 
   * @param idUnitaArboreaDichiarata
   * @param orderBy
   * @return it.csi.solmr.dto.anag.AltroVitignoDichiaratoVO[]
   * @throws Exception
   */
  public AltroVitignoDichiaratoVO[] getListAltroVitignoDichiaratoByIdUnitaArboreaDichiarata(
      Long idUnitaArboreaDichiarata, String[] orderBy) throws Exception
  {
    try
    {
      return altroVitignoDichiaratoDAO
          .getListAltroVitignoDichiaratoByIdUnitaArboreaDichiarata(
              idUnitaArboreaDichiarata, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che richiama una procedura PL-SQL che ribalta le UV sul piano
   * colturale allineandolo
   * 
   * @param idAzienda
   * @param idUtente
   * @throws SolmrException
   * @throws Exception
   */
  public void ribaltaUVOnPianoColturale(Long idAzienda, BigDecimal[] idStoricoUnitaArborea, Long idUtente)
      throws SolmrException, Exception
  {
    try
    {
      storicoUnitaArboreaDAO.ribaltaUVOnPianoColturale(idAzienda, idStoricoUnitaArborea, idUtente);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce l'elenco dei tipi vino
   * 
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.TipoVinoVO[]
   * @throws Exception
   */
  public TipoVinoVO[] getListTipoVino(boolean onlyActive, String[] orderBy)
      throws Exception
  {
    try
    {
      return tipoVinoDAO.getListTipoVino(onlyActive, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce l'elenco delle tipologie vino
   * 
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.TipoTipologiaVinoVO[]
   * @throws Exception
   */
  public TipoTipologiaVinoVO[] getListTipoTipologiaVino(boolean onlyActive,
      String[] orderBy) throws Exception
  {
    try
    {
      return tipoTipologiaVinoDAO.getListTipoTipologiaVino(onlyActive, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public TipoTipologiaVinoVO[] getListTipoTipologiaVinoForAzienda(long idAzienda)
      throws Exception
  {
    try
    {
      return tipoTipologiaVinoDAO.getListTipoTipologiaVinoForAzienda(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public TipoTipologiaVinoVO[] getListTipoTipologiaVinoForDichCons(long idDichiarazioneConsistenza)
      throws Exception
  {
    try
    {
      return tipoTipologiaVinoDAO.getListTipoTipologiaVinoForDichCons(idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per recuperare il tipo utilizzo a partire dalla sua chiave primaria
   * 
   * @param idUtilizzo
   * @return it.csi.solmr.dto.anag.terreni.TipoUtilizzoVO
   * @throws Exception
   */
  public TipoUtilizzoVO findTipoUtilizzoByPrimaryKey(Long idUtilizzo)
      throws Exception
  {
    try
    {
      return tipoUtilizzoDAO.findTipoUtilizzoByPrimaryKey(idUtilizzo);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public TipoUtilizzoVO[] getListTipoUtilizzoByIdAzienda(String tipo, long idAzienda)
    throws Exception
  {
    try
    {
      return tipoUtilizzoDAO.getListTipoUtilizzoByIdAzienda(tipo, idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public TipoUtilizzoVO[] getListTipoUtilizzoByIdDichiarazioneConsistenza(String tipo, long idDichiarazioneConsistenza)
    throws Exception
  {
    try
    {
      return tipoUtilizzoDAO.getListTipoUtilizzoByIdDichiarazioneConsistenza(tipo, idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi permette di recuperare il tipo varietà a partire dalla sua
   * chiave primaria
   * 
   * @param idVarieta
   * @return it.csi.solmr.dto.anag.terreni.TipoVarietaVO
   * @throws Exception
   */
  public TipoVarietaVO findTipoVarietaByPrimaryKey(Long idVarieta)
      throws Exception
  {
    try
    {
      return tipoVarietaDAO.findTipoVarietaByPrimaryKey(idVarieta);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public TipoVarietaVO[] getListTipoVarietaByIdAzienda(
      long idAzienda)
    throws Exception
  {
    try
    {
      return tipoVarietaDAO.getListTipoVarietaByIdAzienda(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public TipoVarietaVO[] getListTipoVarietaByIdDichiarazioneConsistenza(long idDichiarazioneConsistenza)
    throws Exception
  {
    try
    {
      return tipoVarietaDAO.getListTipoVarietaByIdDichiarazioneConsistenza(idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per recuperare l'elenco delle tipologie vino a partire dalla
   * categoria
   * 
   * @param idVino
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.TipoTipologiaVinoVO[]
   * @throws Exception
   */
  public TipoTipologiaVinoVO[] getListTipoTipologiaVinoByIdVino(Long idVino,
      boolean onlyActive, String[] orderBy) throws Exception
  {
    try
    {
      return tipoTipologiaVinoDAO.getListTipoTipologiaVinoByIdVino(idVino,
          onlyActive, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  
  public Vector<TipoTipologiaVinoVO> getListActiveTipoTipologiaVinoByComune(String istatComune) 
    throws Exception
  {
    try
    {
      return tipoTipologiaVinoDAO
        .getListActiveTipoTipologiaVinoByComune(istatComune); 
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public Vector<TipoTipologiaVinoVO> getListActiveTipoTipologiaVinoByComuneAndVarieta(
      String istatComune, Long idVarieta, Long idParticella) 
    throws Exception
  {
    Vector<TipoTipologiaVinoVO> vTipoTipologiaVino = null;
    try
    {
      vTipoTipologiaVino = tipoTipologiaVinoDAO
        .getListActiveTipoTipologiaVinoByComuneAndVarieta(istatComune, idVarieta);
      
      if(Validator.isNotEmpty(vTipoTipologiaVino)
        && Validator.isNotEmpty(idParticella))
      {
        for(int i=0;i<vTipoTipologiaVino.size();i++)
        {
          vTipoTipologiaVino.get(i).setVignaVO(tipoTipologiaVinoDAO.getVignaByIdTipologiaVinoAndParticella(
            vTipoTipologiaVino.get(i).getIdTipologiaVino(), idParticella));
        }
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    
    return vTipoTipologiaVino;
  }
  
  /*public Vector<VignaVO> getListActiveVignaByIdTipologiaVinoAndParticella(long idTipologiaVino, long idParticella)
    throws Exception
  {
    try
    {
      return tipoTipologiaVinoDAO.getListActiveVignaByIdTipologiaVinoAndParticella(idTipologiaVino, idParticella);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }*/
  
  public Vector<TipoTipologiaVinoVO> getListTipoTipologiaVinoRicaduta(
      String istatComune, long idVarieta, long idTipologiaVino, java.util.Date dataInserimentoDichiarazione) 
    throws Exception
  {
    try
    {
      return tipoTipologiaVinoDAO
        .getListTipoTipologiaVinoRicaduta(istatComune, idVarieta, idTipologiaVino, dataInserimentoDichiarazione); 
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public TipoTipologiaVinoVO getTipoTipologiaVinoByPrimaryKey(long idTipologiaVino) 
    throws Exception
  {
    try
    {
      return tipoTipologiaVinoDAO
        .getTipoTipologiaVinoByPrimaryKey(idTipologiaVino);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che si occupa di validare le unità arboree alla dichiarazione di
   * consistenza
   * 
   * @param elencoIdUnitaArboreaDichiarata
   * @throws SolmrException
   * @throws Exception
   */
  public void validaUVPlSql(Long[] elencoIdUnitaArboreaDichiarata)
      throws SolmrException, Exception
  {
    try
    {
      unitaArboreaDichiarataDAO.validaUVPlSql(elencoIdUnitaArboreaDichiarata);
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
    catch (SolmrException se)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(se.getMessage());
    }
  }

  /**
   * Metodo utilizzato per effettuare i riepiloghi per titolo di possesso
   * relativi ad'azienda agricola
   * 
   * @param idAzienda
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.ConduzioneParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoTitoloPossesso(Long idAzienda) throws Exception
  {
    try
    {
      return conduzioneParticellaDAO.riepilogoTitoloPossesso(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo utilizzato per effettuare i riepiloghi per titolo di possesso
   * relativi ad'azienda agricola e ad una determinata dichiarazione di
   * consistenza
   * 
   * @param idAzienda
   * @param idDichiarazioneConsistenza
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.ConduzioneDichiarataVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoTitoloPossessoDichiarato(
      Long idAzienda, Long idDichiarazioneConsistenza)
      throws Exception
  {
    try
    {
      return conduzioneDichiarataDAO.riepilogoTitoloPossessoDichiarato(
          idAzienda, idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per effettuare il riepilogo delle particelle per titolo di possesso
   * e comune relativi ad un'azienda agricola
   * 
   * @param idAzienda
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoPossessoComune(Long idAzienda) throws Exception
  {
    try
    {
      return storicoParticellaDAO.riepilogoPossessoComune(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per effettuare il riepilogo delle particelle per titolo di possesso
   * e comune relativi ad un'azienda agricola ad una determinata dichiarazione
   * di consistenza
   * 
   * @param idAzienda
   * @param idDichiarazioneConsistenza
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoPossessoComuneDichiarato(
      Long idAzienda, Long idDichiarazioneConsistenza)
      throws Exception
  {
    try
    {
      return storicoParticellaDAO.riepilogoPossessoComuneDichiarato(idAzienda,
          idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per effettuare il riepilogo delle particelle per comune relative ad
   * un'azienda agricola
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoComune(Long idAzienda,
      String escludiAsservimento)
      throws Exception
  {
    try
    {
      return storicoParticellaDAO.riepilogoComune(idAzienda, escludiAsservimento);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per effettuare il riepilogo delle particelle per comune relative ad
   * un'azienda agricola ad una determinata dichiarazione di consistenza
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoComuneDichiarato(Long idAzienda,
      String escludiAsservimento, Long idDichiarazioneConsistenza) throws Exception
  {
    try
    {
      return storicoParticellaDAO.riepilogoComuneDichiarato(idAzienda,
          escludiAsservimento, idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo utilizzato per effettuare il riepilogo delle particelle relative ad
   * un'azienda al piano in lavorazione per uso primario
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.UtilizzoParticellaVO[]
   * @throws Exception
   */
  public UtilizzoParticellaVO[] riepilogoUsoPrimario(Long idAzienda)
      throws Exception
  {
    try
    {
      return utilizzoParticellaDAO.riepilogoUsoPrimario(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public BigDecimal getTotSupSfriguAndAsservimento(Long idAzienda, String escludiAsservimento)
    throws Exception
  {
    try
    {
      return utilizzoParticellaDAO.getTotSupSfriguAndAsservimento(idAzienda, escludiAsservimento);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }    
  }

  /**
   * Metodo utilizzato per effettuare il riepilogo delle particelle relative ad
   * un'azienda alla dichiarazione di consistenza per uso primario
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.UtilizzoDichiaratoVO[]
   * @throws Exception
   */
  public UtilizzoDichiaratoVO[] riepilogoUsoPrimarioDichiarato(Long idAzienda,
      Long idDichiarazioneConsistenza) throws Exception
  {
    try
    {
      return utilizzoDichiaratoDAO.riepilogoUsoPrimarioDichiarato(idAzienda,
          idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo utilizzato per effettuare il riepilogo delle particelle relative ad
   * un'azienda al piano in lavorazione per uso secondario
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.UtilizzoParticellaVO[]
   * @throws Exception
   */
  public UtilizzoParticellaVO[] riepilogoUsoSecondario(Long idAzienda)
      throws Exception
  {
    try
    {
      return utilizzoParticellaDAO.riepilogoUsoSecondario(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo utilizzato per effettuare il riepilogo delle particelle relative ad
   * un'azienda alla dichiarazione di consistenza per uso primario
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.UtilizzoDichiaratoVO[]
   * @throws Exception
   */
  public UtilizzoDichiaratoVO[] riepilogoUsoSecondarioDichiarato(
      Long idAzienda, Long idDichiarazioneConsistenza) throws Exception
  {
    try
    {
      return utilizzoDichiaratoDAO.riepilogoUsoSecondarioDichiarato(idAzienda,
          idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo utilizzato per effettuare il riepilogo delle particelle relative ad
   * un'azienda al piano in lavorazione per macro uso
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.UtilizzoParticellaVO[]
   * @throws Exception
   */
  public UtilizzoParticellaVO[] riepilogoMacroUso(Long idAzienda)
      throws Exception
  {
    try
    {
      return utilizzoParticellaDAO.riepilogoMacroUso(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo utilizzato per effettuare il riepilogo delle particelle relative ad
   * un'azienda alla dichiarazione di consistenza per macro uso
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.UtilizzoDichiaratoVO[]
   * @throws Exception
   */
  public UtilizzoDichiaratoVO[] riepilogoMacroUsoDichiarato(Long idAzienda,
      Long idDichiarazioneConsistenza) throws Exception
  {
    try
    {
      return utilizzoDichiaratoDAO.riepilogoMacroUsoDichiarato(idAzienda,
          idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public BigDecimal getTotSupAsservimento(Long idAzienda, Long idDichiarazioneConsistenza)
    throws Exception
  {
    try
    {
      return utilizzoDichiaratoDAO.getTotSupAsservimento(idAzienda, idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per effettuare il riepilogo delle particelle per ZVN solo dei comuni
   * TOBECONFIG
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoZVNParticellePiemonte(Long idAzienda,
      String escludiAsservimento)
      throws Exception
  {
    try
    {
      return storicoParticellaDAO.riepilogoZVNParticellePiemonte(idAzienda,
          escludiAsservimento);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per effettuare il riepilogo delle particelle per ZVN solo dei comuni
   * TOBECONFIG ad una determinata dichiarazione di consistenza
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoZVNParticelleDichiaratePiemonte(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws Exception
  {
    try
    {
      return storicoParticellaDAO.riepilogoZVNParticelleDichiaratePiemonte(
          idAzienda, escludiAsservimento, idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  /**
   * Metodo per effettuare il riepilogo delle particelle per ZVN 
   * comprensivo di fascia fluviale
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoZVNFasciaFluviale(Long idAzienda, 
      String escludiAsservimento)
      throws Exception
  {
    try
    {
      return storicoParticellaDAO.riepilogoZVNFasciaFluviale(idAzienda,
          escludiAsservimento);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  /**
   * Metodo per effettuare il riepilogo delle particelle per ZVN 
   * comprensivo di fascia fluviale ad una determiinata dichiarazione di 
   * consistenza
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoZVNFasciaFluvialeDichiarate(Long idAzienda, 
      String escludiAsservimento, Long idDichiarazioneConsistenza) throws Exception
  {
    try
    {
      return storicoParticellaDAO.riepilogoZVNFasciaFluvialeDichiarate(
          idAzienda, escludiAsservimento, idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per effettuare il riepilogo delle particelle per ZVF solo dei comuni
   * TOBECONFIG
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoZVFParticellePiemonte(Long idAzienda,
      String escludiAsservimento)
      throws Exception
  {
    try
    {
      return storicoParticellaDAO.riepilogoZVFParticellePiemonte(idAzienda,
          escludiAsservimento);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per effettuare il riepilogo delle particelle per ZVF solo dei comuni
   * TOBECONFIG ad una determinata dichiarazione di consistenza
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoZVFParticelleDichiaratePiemonte(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws Exception
  {
    try
    {
      return storicoParticellaDAO.riepilogoZVFParticelleDichiaratePiemonte(
          idAzienda, escludiAsservimento, idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  
  /**
   * Metodo per effettuare il riepilogo delle particelle per Localizzazione solo dei comuni
   * TOBECONFIG
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoLocalizzazioneParticellePiemonte(Long idAzienda,
      String escludiAsservimento)
      throws Exception
  {
    try
    {
      return storicoParticellaDAO.riepilogoLocalizzazioneParticellePiemonte(idAzienda,
          escludiAsservimento);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per effettuare il riepilogo delle particelle per Localizzazione solo dei comuni
   * TOBECONFIG ad una determinata dichiarazione di consistenza
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoLocalizzazioneParticelleDichiaratePiemonte(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws Exception
  {
    try
    {
      return storicoParticellaDAO.riepilogoLocalizzazioneParticelleDichiaratePiemonte(
          idAzienda, escludiAsservimento, idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  /**
   * Metodo per effettuare il riepilogo delle particelle per fascia fluviale
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoFasciaFluvialeParticelle(Long idAzienda,
      String escludiAsservimento)
      throws Exception
  {
    try
    {
      return storicoParticellaDAO.riepilogoFasciaFluvialeParticelle(idAzienda,
          escludiAsservimento);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per effettuare il riepilogo delle particelle per Fascia Fluviale
   * ad una determinata dichiarazione di consistenza
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoFasciaFluvialeParticelleDichiarate(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws Exception
  {
    try
    {
      return storicoParticellaDAO.riepilogoFasciaFluvialeParticelleDichiarate(
          idAzienda, escludiAsservimento, idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per estrarre i tipi macro uso associati ad un'azienda agricola
   * 
   * @param idAzienda
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.TipoMacroUsoVO[]
   * @throws Exception
   */
  public TipoMacroUsoVO[] getListTipoMacroUsoByIdAzienda(Long idAzienda,
      boolean onlyActive, String[] orderBy) throws Exception
  {
    try
    {
      return commonDAO.getListTipoMacroUsoByIdAzienda(idAzienda, onlyActive,
          orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi consente di estrarre l'elenco delle varietà vitigno relative
   * ad un determinato utilizzo di un certo comune
   * 
   * @param idUtilizzo
   * @param istatComune
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.TipoVarietaVO[]
   * @throws Exception
   */
  public Vector<TipoVarietaVO> getListTipoVarietaVitignoByMatriceAndComune(long idUtilizzo, 
      long idTipoDestinazione, long idTipoDettaglioUso, long idTipoQualitaUso, 
      String istatComune) throws Exception
  {
    try
    {
      return tipoVarietaDAO.getListTipoVarietaVitignoByMatriceAndComune(
          idUtilizzo, idTipoDestinazione, idTipoDettaglioUso, idTipoQualitaUso, istatComune);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi permette di estrarre la somma delle superfici condotte
   * relative ad una determinata azienda agriocola
   * 
   * @param idAzienda
   * @param onlyActive
   * @return java.lang.String
   * @throws Exception
   */
  public java.lang.String getTotSupCondottaByAzienda(Long idAzienda,
      boolean onlyActive) throws Exception
  {
    try
    {
      return conduzioneParticellaDAO.getTotSupCondottaByAzienda(idAzienda,
          onlyActive);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo utilizzato per effettuare il riepilogo per i vari tipi area censiti
   * su DB_STORICO_PARTICELLA al piano di lavorazione corrente
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param tipoArea
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoTipoArea(Long idAzienda,
      String escludiAsservimento, String tipoArea) throws Exception
  {
    try
    {
      return storicoParticellaDAO.riepilogoTipoArea(idAzienda,
          escludiAsservimento, tipoArea);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo utilizzato per effettuare il riepilogo per i vari tipi area censiti
   * su DB_STORICO_PARTICELLA ad una determinata dichiarazione di consistenza
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @param tipoArea
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoTipoAreaDichiarato(Long idAzienda,
      String escludiAsservimento, Long idDichiarazioneConsistenza,
      String tipoArea) throws Exception
  {
    try
    {
      return storicoParticellaDAO.riepilogoTipoAreaDichiarato(idAzienda,
          escludiAsservimento, idDichiarazioneConsistenza, tipoArea);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi consente di recuperare l'elenco delle unità arboree
   * dichiarate relative ad una determinata particella di un'azienda al piano di
   * riferimento dichiarato
   * 
   * @param idStoricoParticella
   * @param idAzienda
   * @param idPianoRiferimento
   * 
   * @return it.csi.solmr.dto.anag.UnitaArboreaDichiarataVO[]
   * @throws Exception
   */
  public UnitaArboreaDichiarataVO[] getListUnitaArboreaDichiarataByPianoRiferimentoAndIdStoricoUnitaArborea(
      Long idStoricoParticella, Long idAzienda, Long idPianoRiferimento,
      String[] orderBy) throws Exception
  {
    try
    {
      return unitaArboreaDichiarataDAO
          .getListUnitaArboreaDichiarataByPianoRiferimentoAndIdStoricoUnitaArborea(
              idStoricoParticella, idAzienda, idPianoRiferimento, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public long getIdUnitaArboreaDichiarata(long idStoricoUnitaArborea, long idDichiarazioneConsistenza)
      throws Exception
  {
    try
    {
      return unitaArboreaDichiarataDAO.getIdUnitaArboreaDichiarata(idStoricoUnitaArborea, 
          idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo utilizzato per importare le particelle in asservimento da un'altra
   * azienda
   * Restituisce un vettore contente in posizione 0 l'elenco delle particelle importate
   * ed in posizione 1 l'elenco delle particelle non importate
   * 
   * @param elencoConduzioni
   * @param idUte
   * @param anagAziendaVO
   * @param anagAziendaSearchVO
   * @param ruoloUtenza
   * @throws Exception
   * @throws SolmrException
   */
  public Vector<Object> importParticelleAsservite(String[] elencoConduzioni, Long idUte,
      AnagAziendaVO anagAziendaSearchVO,  RuoloUtenza ruoloUtenza, Long idTitoloPossesso) throws Exception
  {
    try
    {
      Vector<String> partImpor = new Vector<String>();
      Vector<String> partNonImpor = new Vector<String>();
      
      
      //usata per tenenere il totale parziale della superficie condotta relativa alla particella
      //HashMap<Long,BigDecimal> hSumSupCond = new HashMap<Long,BigDecimal>(); 
      //superficie spandibile relativa alla conduzione
      HashMap<Long,BigDecimal> hSupSpandibile = new HashMap<Long,BigDecimal>(); 
      
      // Ciclo sulle conduzioni selezionate
      for (int i = 0; i < elencoConduzioni.length; i++)
      {
        Long idConduzioneDichiarata = Long.decode((String) elencoConduzioni[i]);
        // Ricerco la particella sul DB
        ConduzioneDichiarataVO conduzioneDichiarataVO = conduzioneDichiarataDAO
            .findConduzioneDichiarataByPrimaryKey(idConduzioneDichiarata);
        ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
        conduzioneParticellaVO.setIdParticella(conduzioneDichiarataVO
            .getIdParticella());
        conduzioneParticellaVO.setIdUte(idUte);
        conduzioneParticellaVO.setIdTitoloPossesso(idTitoloPossesso);
        
        boolean flagInserimento = true;
        
        
        //Superficie_agronomica	
        //Null per l’asservimento
        //Superficie_agronomica della conduzione dichiarata per il conferimento
        if (idTitoloPossesso.longValue()==5)
        {
          //Aggiunta supSpandibile
          BigDecimal supSpandibile = new BigDecimal(0);
          //La sup della conduzione è già presente, mi trovo su un record con diverso utilizzo ma
          //identica conduzione, quindi devo prendere la sup già calcolata per la medesima conduzione 
          if(hSupSpandibile.get(idConduzioneDichiarata) != null)
          {
            supSpandibile = (BigDecimal)hSupSpandibile.get(idConduzioneDichiarata);
          }
          else
          {
            BigDecimal supUtilizzoAgronomicoConduzione = conduzioneDichiarataDAO
              .getSumSuperficieUtilizzoUsoAgronomico(idConduzioneDichiarata.longValue());
            
            BigDecimal supUtilizzoAgronomicoParticella = conduzioneDichiarataDAO
                .getSumSuperficieUtilizzoUsoAgronomicoParticella(conduzioneDichiarataVO.getIdParticella().longValue(), 
                    anagAziendaSearchVO.getIdAzienda().longValue());
            
            
            if(supUtilizzoAgronomicoConduzione !=null)
            {
              //Totale spandibile sulla particella come superficie agronomica
              supSpandibile = supUtilizzoAgronomicoParticella; 
              
              //Sottraggo l'asservimento/sup agronomica relativo alla particella
              //ma in conduzioni ad altre aziende!!
              BigDecimal sumSupPartDichTmp   = conduzioneDichiarataDAO
                .getSumSuperficieFromParticellaAndLastDichCons(
                    conduzioneDichiarataVO.getIdParticella().longValue(),
                    anagAziendaSearchVO.getIdAzienda(), true);
              if(sumSupPartDichTmp != null)
              {
                supSpandibile = supSpandibile.subtract(sumSupPartDichTmp);
              }
              //Sottraggo l'asservimento/sup agronomica relativo alla particella
              //in altre conduzioni dell'azienda corrente!!
              supSpandibile = supSpandibile.subtract(conduzioneDichiarataDAO
                  .getSumSuperficieAgronomicaAltreconduzioni(conduzioneDichiarataVO.getIdParticella().longValue(), 
                      idConduzioneDichiarata.longValue(), anagAziendaSearchVO.getIdAzienda().longValue()));              
              
              
              //Se il totale è maggiore della sup agronomica della conduzione
              // prendo ovviamente come riferimento supUtilizzoAgronomicoConduzione
              if(supSpandibile.compareTo(supUtilizzoAgronomicoConduzione) > 0)
              {
                supSpandibile = supUtilizzoAgronomicoConduzione;
              }
              
              
              BigDecimal supAgri = new BigDecimal("0");
              String supAgriStr = conduzioneDichiarataVO.getSuperficieAgronomica();
              if (Validator.isNotEmpty(supAgriStr))
              {
                supAgri = supAgri.add(new BigDecimal(Formatter.
                    formatDouble4ZeroOnErrorForBigDecimal(supAgriStr)));
              }              
              //sottraggo la superifice agronomica già usata per tale conduzione
              supSpandibile = supSpandibile.subtract(supAgri);
              
                
              if(supSpandibile.compareTo(new BigDecimal(0)) <= 0)
              {
                supSpandibile = new BigDecimal(0);
              }
              
            }
              
            hSupSpandibile.put(idConduzioneDichiarata, supSpandibile);
          }
          
          //se il valore è minore di 0 sostituisco col valore 0!!!
          if(supSpandibile.compareTo(new BigDecimal(0)) <= 0)
          {
            supSpandibile = new BigDecimal(0);
            flagInserimento = false;
          }           
          
          conduzioneParticellaVO.setSuperficieCondotta(
              StringUtils.parseSuperficieField(supSpandibile.toString()));
        	conduzioneParticellaVO.setSuperficieAgronomica(null);
        	conduzioneParticellaVO.setNote("Importazione Asservimento dall'azienda con CUAA: "+ anagAziendaSearchVO.getCUAA());
        }
        else //Conferimento
        {
          
          
          double totSupUtilizzata = utilizzoDichiaratoDAO
            .getTotSupUtilizzataByIdConduzioneDichiarata(idConduzioneDichiarata);
          String strSupCondotta = StringUtils.parseSuperficieField(new Double(totSupUtilizzata).toString());
          conduzioneParticellaVO.setSuperficieCondotta(strSupCondotta);
        	conduzioneParticellaVO.setSuperficieAgronomica(conduzioneDichiarataVO.getSuperficieAgronomica());
        	conduzioneParticellaVO.setNote("Importazione in Conferimento dall'azienda con CUAA: "+ anagAziendaSearchVO.getCUAA());
        }
        
        conduzioneParticellaVO.setFlagUtilizzoParte(null);
        
        
        conduzioneParticellaVO.setDataInizioConduzione(new Date());
        conduzioneParticellaVO.setDataFineConduzione(null);
        conduzioneParticellaVO.setDataAggiornamento(new Date());
        conduzioneParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
            .getIdUtente());
        conduzioneParticellaVO.setEsitoControllo(null);
        conduzioneParticellaVO.setDataEsecuzione(null);
        conduzioneParticellaVO.setRecordModificato("S");

        //inserisco il record solo se la superficie condotta è maggiore di 0!!!
        if(flagInserimento)
        {
          
          
          //calcolo la percentualePossesso
          double percentualePossessoDb = 100;
          StoricoParticellaVO strVO = storicoParticellaDAO
            .findStoricoParticellaByIdParticellaForImpAss(conduzioneDichiarataVO
                .getIdParticella().longValue());
          String supConfronto = strVO.getSupCatastale();
          if(Double.parseDouble(supConfronto.replace(',', '.')) == 0)
          {
            supConfronto = strVO.getSuperficieGrafica();
          }
          
          if(Double.parseDouble(supConfronto.replace(',', '.')) > 0)
          {
            double supCondottaDb = Double.parseDouble(conduzioneParticellaVO
                .getSuperficieCondotta().replace(',', '.'));
            double supConfrontoDb = Double.parseDouble(supConfronto.replace(',', '.'));             
            percentualePossessoDb = 100.0 * supCondottaDb / supConfrontoDb; 
            percentualePossessoDb = NumberUtils.arrotonda(percentualePossessoDb,2);
            if(percentualePossessoDb > 100)
            {
              percentualePossessoDb = 100;
            }
          }
          conduzioneParticellaVO.setPercentualePossesso(new BigDecimal(percentualePossessoDb));
          
          
          
          
          Long idConduzioneParticella = conduzioneParticellaDAO
              .insertConduzioneParticella(conduzioneParticellaVO);
          partImpor.add(elencoConduzioni[i]);
          
          //Per ogni conduzione inserita devo inserire anche gli utilizzi leagati alla conduzione di partenza, ossia
          // inserire tutti gli usi del suolo legati alla conduzione di partenza sulla tabella DB_UTILIZZO_PARTICELLA
          if (idTitoloPossesso.longValue()==6)
          {
            Vector<UtilizzoDichiaratoVO> vUtilizzoDichiarato = utilizzoDichiaratoDAO
                .getListUtilizzoDichiaratoByIdConduzioneDichiarata(idConduzioneDichiarata.longValue());
            if(vUtilizzoDichiarato != null)
            {
              for(int j=0;j<vUtilizzoDichiarato.size();j++)
              {
                UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
                utilizzoParticellaVO.setIdConduzioneParticella(idConduzioneParticella);
                convertUtilDichIntoUtilPart(vUtilizzoDichiarato.get(j), utilizzoParticellaVO);
                Long idUtilizzoParticella = utilizzoParticellaDAO.insertUtilizzoParticella(utilizzoParticellaVO);
                Vector<UtilizzoConsociatoDichiaratoVO> vUtilizzoConsociatoDichiarato = 
                    utilizzoConsociatoDichDAO.getListUtilizziConsociatiDichByIdUtilizzoDichiarato(
                        vUtilizzoDichiarato.get(j).getIdUtilizzoDichiarato());
                if(vUtilizzoConsociatoDichiarato != null)
                {
                  for(int k=0;k<vUtilizzoConsociatoDichiarato.size();k++)
                  {
                    UtilizzoConsociatoVO utilizzoConsociatoVO = new UtilizzoConsociatoVO();
                    utilizzoConsociatoVO.setIdUtilizzoParticella(idUtilizzoParticella);
                    convertUtilConsDichIntoUtilCons(vUtilizzoConsociatoDichiarato.get(k), utilizzoConsociatoVO);
                    utilizzoConsociatoDAO.insertUtilizzoConsociato(utilizzoConsociatoVO);
                  }                  
                }
                
              }
              
            }
            
          	//utilizzoParticellaDAO.importUtilizzoParticella(conduzioneDichiarataVO.getIdConduzioneParticella(),
          			//idConduzioneParticella, ruoloUtenza.getIdUtente());
          }
          
        }
        else partNonImpor.add(elencoConduzioni[i]);
      }
      
      Vector<Object> result = new Vector<Object>();
      result.add(partImpor.size() == 0 ? null : (String[]) partImpor.toArray(new String[0]));
      result.add(partNonImpor.size() == 0 ? null : (String[]) partNonImpor.toArray(new String[0]));
      result.add(SolmrConstants.IMPORTA_ASSERVIMENTO_CUAA);      
      
      return result;
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
  }
  
  
  /**
   * Metodo utilizzato per importare le particelle in asservimento dalla
   * ricerca Particelle, quindi partendo da altre particelle.
   * Restituisce un vettore contente in posizione 0 l'elenco delle particelle importate
   * ed in posizione 1 l'elenco delle particelle non importate
   * 
   * @param elencoIdParticelle
   * @param idUte
   * @param ruoloUtenza
   * @throws Exception
   * @throws SolmrException
   */
  public Vector<Object> importParticelleAsserviteFromRicercaParticella(String[] elencoIdParticelle, Long idUte,
      AnagAziendaVO anagAziendaVO, RuoloUtenza ruoloUtenza, Long idTitoloPossesso) throws Exception
  {
    try
    {
      Vector<String> partImpor=new Vector<String>();
      Vector<String> partNonImpor=new Vector<String>();
      
      // Ciclo sulle conduzioni selezionate
      for (int i = 0; i < elencoIdParticelle.length; i++)
      {
        Long idParticella = Long.decode((String) elencoIdParticelle[i]);
            
        ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
        conduzioneParticellaVO.setIdParticella(idParticella);
        conduzioneParticellaVO.setIdUte(idUte);
        conduzioneParticellaVO.setIdTitoloPossesso(new Long("5"));
        
        
        
        
        //devo verificare se sulla particella esiste almeno una
        //conduzione dichiarata <> 5 (asservimento) e <>6 conferimento
        boolean trovato = conduzioneDichiarataDAO.existsConduzioneDichiarataNotAsservimentoConferimento(idParticella.longValue(),anagAziendaVO.getIdAzienda().longValue());
        StoricoParticellaVO stVO = storicoParticellaDAO.findStoricoParticellaByIdParticellaForImpAss(idParticella);
        
        BigDecimal supCond = new BigDecimal(0);
        
        if(trovato)
        {
          BigDecimal supUtiliz = conduzioneDichiarataDAO
            .getSumSuperficieUtilizzata(idParticella.longValue());
          if(Validator.isNotEmpty(supUtiliz))
          {
            supCond = supCond.add(supUtiliz);
          }
          
          BigDecimal supFromParticAndDich = conduzioneDichiarataDAO
          .getSumSuperficieFromParticellaAndLastDichCons(
              idParticella.longValue(), null, false);
          if(Validator.isNotEmpty(supFromParticAndDich))
          {
            supCond = supCond.subtract(supFromParticAndDich);        
          }
        }
        else
        {
          String supCatastaleStr = stVO.getSupCatastale();
          if (Validator.isNotEmpty(supCatastaleStr))
          {
            supCond = supCond.add(new BigDecimal(Formatter.
                formatDouble4ZeroOnErrorForBigDecimal(supCatastaleStr)));
          }
          
          BigDecimal supCondottaAss = conduzioneDichiarataDAO
            .getSumSuperficieCondottaAsservita(idParticella.longValue(), 
                  anagAziendaVO.getIdAzienda().longValue());
          if(Validator.isNotEmpty(supCondottaAss))
          {
            supCond = supCond.subtract(supCondottaAss);        
          }
          
        }
        
        //se il valore è minore di 0 sostituisco col valore 0!!!
        boolean flagImporta = true; 
        if(supCond.compareTo(new BigDecimal(0)) <= 0)
        {
          supCond = new BigDecimal(0);
          flagImporta = false;
        }
        
        
        conduzioneParticellaVO.setSuperficieCondotta(StringUtils
            .parseSuperficieField(supCond.toString()));
        
        
        

        conduzioneParticellaVO.setSuperficieAgronomica(null);
        conduzioneParticellaVO.setFlagUtilizzoParte(null);
        conduzioneParticellaVO
            .setNote("Importazione Asservimento particella");
        conduzioneParticellaVO.setDataInizioConduzione(new Date());
        conduzioneParticellaVO.setDataFineConduzione(null);
        conduzioneParticellaVO.setDataAggiornamento(new Date());
        conduzioneParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
            .getIdUtente());
        conduzioneParticellaVO.setEsitoControllo(null);
        conduzioneParticellaVO.setDataEsecuzione(null);
        conduzioneParticellaVO.setRecordModificato("S");

        //Importa solo se la superficie condotta è maggiore di 0
        if(flagImporta)
        {         
          
          BigDecimal percentualePossessoBd = new BigDecimal(100);
          String supConfronto = stVO.getSupCatastale();
          if(Double.parseDouble(supConfronto.replace(',', '.')) == 0)
          {
            supConfronto = stVO.getSuperficieGrafica();
          }
          
          if(Double.parseDouble(supConfronto.replace(',', '.')) > 0)
          {
            BigDecimal supConfrontoBd = new BigDecimal(Formatter.
                formatDouble4ZeroOnErrorForBigDecimal(supConfronto));
            
            percentualePossessoBd = supCond;
            percentualePossessoBd = percentualePossessoBd.multiply(new BigDecimal(100));
            percentualePossessoBd = percentualePossessoBd.divide(supConfrontoBd,2,BigDecimal.ROUND_HALF_UP);
            
            
            
            if(percentualePossessoBd.compareTo(new BigDecimal(100)) > 0)
            {
              percentualePossessoBd = new BigDecimal(100);
            }
            else if(percentualePossessoBd.compareTo(new BigDecimal(0)) == 0)
            {
              percentualePossessoBd = new BigDecimal(0.01);
            }
          }
          conduzioneParticellaVO.setPercentualePossesso(percentualePossessoBd);
          
          
          
          conduzioneParticellaDAO
              .insertConduzioneParticella(conduzioneParticellaVO);
          partImpor.add(elencoIdParticelle[i]);
        }
        else partNonImpor.add(elencoIdParticelle[i]);
      }
      Vector<Object> result=new Vector<Object>();
      result.add(partImpor.size() == 0 ? null : (String[]) partImpor.toArray(new String[0]));
      result.add(partNonImpor.size() == 0 ? null : (String[]) partNonImpor.toArray(new String[0]));
      result.add(SolmrConstants.IMPORTA_ASSERVIMENTO_PARTICELLARE);
      return result;
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
  }

  public long[] ricercaIdParticelleTerreni(
      FiltriRicercaTerrenoVO filtriRicercaTerrenoVO) throws Exception
  {
    try
    {
      SolmrLogger.debug(this, "[GestioneTerreniBean::ricercaTerreni] BEGIN.");
      return particellaGaaDAO.ricercaIdParticelleTerreni(filtriRicercaTerrenoVO);
    }
    catch (Exception e)
    {
      // Log dell'errore
      Parametro parametri[] = new Parametro[] { new Parametro(
          "filtriRicercaTerrenoVO", filtriRicercaTerrenoVO) };
      LoggerUtils.logEJBError(this, "[GestioneTerreniBean::ricercaTerreni]", e,
          null, parametri);
      throw new Exception(e.getMessage());
    }
    finally
    {
      SolmrLogger.debug(this, "[GestioneTerreniBean::ricercaTerreni] END.");
    }
  }
  
  public long[] ricercaIdConduzioneTerreniImportaAsservimento(
      FiltriRicercaTerrenoImportaAsservimentoVO filtriRicercaTerrenoVO) throws Exception
  {
    try
    {
      SolmrLogger.debug(this, "[GestioneTerreniBean::ricercaIdConduzioneTerreniImportaAsservimento] BEGIN.");
      return particellaGaaDAO.ricercaIdConduzioneTerreniImportaAsservimento(filtriRicercaTerrenoVO);
    }
    catch (Exception e)
    {
      // Log dell'errore
      Parametro parametri[] = new Parametro[] { new Parametro(
          "filtriRicercaTerrenoVO", filtriRicercaTerrenoVO) };
      LoggerUtils.logEJBError(this, "[GestioneTerreniBean::ricercaIdConduzioneTerreniImportaAsservimento]", e,
          null, parametri);
      throw new Exception(e.getMessage());
    }
    finally
    {
      SolmrLogger.debug(this, "[GestioneTerreniBean::ricercaIdConduzioneTerreniImportaAsservimento] END.");
    }
  }

  public RigaRicercaTerreniVO[] getRigheRicercaTerreniByIdParticellaRange(
      long ids[]) throws Exception
  {
    try
    {
      SolmrLogger
          .debug(this,
              "[GestioneTerreniBean::getRigheRicercaTerreniByIdParticellaRange] BEGIN.");
      return particellaGaaDAO.getRigheRicercaTerreniByIdParticellaRange(ids);
    }
    catch (Exception e)
    {
      // Log dell'errore
      Parametro parametri[] = new Parametro[] { new Parametro("ids", ids) };
      LoggerUtils.logEJBError(this,
          "[GestioneTerreniBean::getRigheRicercaTerreniByIdParticellaRange]",
          e, null, parametri);
      throw new Exception(e.getMessage());
    }
    finally
    {
      SolmrLogger
          .debug(this,
              "[GestioneTerreniBean::getRigheRicercaTerreniByIdParticellaRange] END.");
    }
  }
  
  public RigaRicercaTerreniImportaAsservimentoVO[] getRigheRicercaTerreniImportaAsservimentoByIdConduzioneRange(
      long ids[]) throws Exception
  {
    try
    {
      SolmrLogger
          .debug(this,
              "[GestioneTerreniBean::getRigheRicercaTerreniImportaAsservimentoByIdConduzioneRange] BEGIN.");
      return particellaGaaDAO.getRigheRicercaTerreniImportaAsservimentoByIdConduzioneRange(ids);
    }
    catch (Exception e)
    {
      // Log dell'errore
      Parametro parametri[] = new Parametro[] { new Parametro("ids", ids) };
      LoggerUtils.logEJBError(this,
          "[GestioneTerreniBean::getRigheRicercaTerreniImportaAsservimentoByIdConduzioneRange]",
          e, null, parametri);
      throw new Exception(e.getMessage());
    }
    finally
    {
      SolmrLogger
          .debug(this,
              "[GestioneTerreniBean::getRigheRicercaTerreniImportaAsservimentoByIdConduzioneRange] END.");
    }
  }
  
  public RigaRicercaTerreniImportaAsservimentoVO[] getRigheRicercaTerreniImportaAsservimentoByIdParticellaRange(
      long ids[]) throws Exception
  {
    try
    {
      SolmrLogger
          .debug(this,
              "[GestioneTerreniBean::getRigheRicercaTerreniImportaAsservimentoByIdParticellaRange] BEGIN.");
      return particellaGaaDAO.getRigheRicercaTerreniImportaAsservimentoByIdParticellaRange(ids);
    }
    catch (Exception e)
    {
      // Log dell'errore
      Parametro parametri[] = new Parametro[] { new Parametro("ids", ids) };
      LoggerUtils.logEJBError(this,
          "[GestioneTerreniBean::getRigheRicercaTerreniImportaAsservimentoByIdParticellaRange]",
          e, null, parametri);
      throw new Exception(e.getMessage());
    }
    finally
    {
      SolmrLogger
          .debug(this,
              "[GestioneTerreniBean::getRigheRicercaTerreniImportaAsservimentoByIdParticellaRange] END.");
    }
  }

  public ParticellaDettaglioValidazioniVO[] getParticellaDettaglioValidazioni(
      long idParticella, Long anno, int tipoOrdinamento, boolean ordineAscendente[])
      throws Exception
  {
    try
    { 
      SolmrLogger.debug(this,
          "[GestioneTerreniBean::getParticellaDettaglioValidazioni] BEGIN.");
      return particellaGaaDAO.getParticellaDettaglioValidazioni(
          idParticella, anno, tipoOrdinamento, ordineAscendente);
    }
    catch (Exception e)
    {
      // Log dell'errore
      Parametro parametri[] = new Parametro[]
      { new Parametro("idParticella", idParticella),
          new Parametro("anno", anno),
          new Parametro("tipoOrdinamento", tipoOrdinamento),
          new Parametro("ordineAscendente", ordineAscendente)};
      LoggerUtils.logEJBError(this,
          "[GestioneTerreniBean::getParticellaDettaglioValidazioni]", e, null,
          parametri);
      throw new Exception(e.getMessage());
    }
    finally
    {
      SolmrLogger.debug(this,
          "[GestioneTerreniBean::getParticellaDettaglioValidazioni] END.");
    }
  }

  public boolean areParticelleNonCessateByIdParticelle(long idParticelle[])
      throws Exception
  {
    try
    {
      SolmrLogger.debug(this,
          "[GestioneTerreniBean::areParticelleNonCessateByIdParticelle] BEGIN.");
      return particellaGaaDAO
          .areParticelleNonCessateByIdParticelle(idParticelle);
    }
    catch (Exception e)
    {
      // Log dell'errore
      Parametro parametri[] = new Parametro[]
      { new Parametro("idParticelle", idParticelle) };
      LoggerUtils.logEJBError(this,
          "[GestioneTerreniBean::areParticelleNonCessateByIdParticelle]", e, null,
          parametri);
      throw new Exception(e.getMessage());
    }
    finally
    {
      SolmrLogger.debug(this,
          "[GestioneTerreniBean::areParticelleNonCessateByIdParticelle] END.");
    }
  }

  /**
   * Metodo che mi restituisce il record di DB_STORICO_PARTICELLA a partire
   * dall'id_particella
   * 
   * @param idConduzioneParticella
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO
   * @throws Exception
   */
  public StoricoParticellaVO findStoricoParticellaVOByIdParticella(long idParticella) throws Exception {
    StoricoParticellaVO storicoParticellaVO = null;
    DocumentoVO documentoVO = null;
    TipoDocumentoVO tipoDocumentoVO = null;
    try { 
      storicoParticellaVO = particellaGaaDAO.findStoricoParticellaVOByIdParticella(idParticella);
      FoglioVO foglioVO = foglioDAO.findFoglioByParameters(storicoParticellaVO.getIstatComune(), storicoParticellaVO.getFoglio(), storicoParticellaVO.getSezione());
      storicoParticellaVO.setFoglioVO(foglioVO);
      if(Validator.isNotEmpty(storicoParticellaVO.getIdDocumentoProtocollato())) {
        documentoVO = documentoDAO.findDocumentoVOByPrimaryKey(storicoParticellaVO.getIdDocumentoProtocollato());
        tipoDocumentoVO = tipoDocumentoDAO.findTipoDocumentoVOByPrimaryKey(documentoVO.getExtIdDocumento());
        documentoVO.setTipoDocumentoVO(tipoDocumentoVO);
        storicoParticellaVO.setDocumentoVO(documentoVO);
      }
    }
    catch(DataAccessException dae) {
      throw new Exception(dae.getMessage());
    }
    return storicoParticellaVO;
  }
  
  
  /**
   * Metodo che mi permette di recuperare le provincie delle unita arboree
   * 
   * @param idStoricoUnitaArborea
   * @return Vector di String
   * @throws Exception
   */
  public Vector<String> findProvinciaStoricoParticellaArborea(long[] idStoricoUnitaArborea) throws Exception
  {
    try
    {
      return storicoUnitaArboreaDAO
          .findProvinciaStoricoParticellaArborea(idStoricoUnitaArborea);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public Vector<String> findProvinciaStoricoParticellaArboreaIsolaParcella(long idAzienda, long[] idIsolaParcella)
     throws Exception
  {
    try
    {
      return storicoUnitaArboreaDAO
          .findProvinciaStoricoParticellaArboreaIsolaParcella(idAzienda, idIsolaParcella);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public BigDecimal getSumAreaUVParticella(long idAzienda, long idParticella) 
    throws Exception
  {
    try
    {
      return storicoUnitaArboreaDAO.getSumAreaUVParticella(idAzienda, idParticella);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public int getNumUVParticella(long idAzienda, long idParticella)
    throws Exception
  {
    try
    {
      return storicoUnitaArboreaDAO.getNumUVParticella(idAzienda, idParticella);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  
  
  /**
   * Metodo che mi permette di recuperare le provincie delle unita arboree dichiarate
   * 
   * @param idStoricoUnitaArborea
   * @return Vector di String
   * @throws Exception
   */
  public Vector<String> findProvinciaParticellaArboreaDichiarata(long[] idUnitaArboreaDichiarata) throws Exception
  {
    try
    {
      return storicoUnitaArboreaDAO
          .findProvinciaParticellaArboreaDichiarata(idUnitaArboreaDichiarata);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  
  public StoricoUnitaArboreaVO findStoricoUnitaArborea(Long idStoricoUnitaArborea) throws Exception
  {
    try
    {
      return storicoUnitaArboreaDAO
          .findStoricoUnitaArborea(idStoricoUnitaArborea);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  /**
   * Restitusce la sommatoria:
   * con db_conduzione_particella.id_conduzione_particella accedere a db_utilizzo_particella, 
   * estrarre tutti gli utilizzi per cui db_tipo_utilizzo. flag_uso_agronomico =’S’ (superficie spandibile) 
   * ed effettuare la sommatoria del campo superficie_utilizzata.
   * 
   * @param idConduzioneParticella
   * @return
   * @throws Exception
   */
  public BigDecimal getSumSuperficieUtilizzoUsoAgronomico(long idConduzioneParticella) 
    throws Exception
  {
    try
    {
      return conduzioneParticellaDAO
      .getSumSuperficieUtilizzoUsoAgronomico(idConduzioneParticella);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public BigDecimal getSumSuperficieUtilizzoUsoAgronomicoParticella(long idParticella, long idAzienda) 
      throws Exception
  {
    try
    {
      return conduzioneParticellaDAO
          .getSumSuperficieUtilizzoUsoAgronomicoParticella(idParticella, idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public BigDecimal getSumSuperficieAgronomicaAltreconduzioni(long idParticella, long idConduzioneParticella, long idAzienda) 
      throws Exception
  {
    try
    {
      return conduzioneParticellaDAO.getSumSuperficieAgronomicaAltreconduzioni(idParticella, idConduzioneParticella, idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  
  /**
   * 
   * 
   * Restituisce la sommatoria di:
   * considerare l’ultima dichiarazione di consistenza 
   * (escluse quelle dichiarazione per avversità "siccità 2006" 
   * ovvero id_motivo_dichiarazione=7)legata alla particella 
   * corrente di ciascuna azienda
   * effettuare la sommatoria della superficie
   * db_conduzione_dichiarata.superficie_condotta
   * se id_titolo_possesso=5 (conduzione=asservimento)
   * o db_conduzione_dichiarata.superficie_agronomica
   * se id_titolo_possesso<>5 (conduzione<>asservimento)
   * 
   * @param idParticella
   * @param idAzienda
   * @param flagEscludiAzienda
   * @return
   * @throws Exception
   */
  public BigDecimal getSumSuperficieFromParticellaAndLastDichCons(
      long idParticella, Long idAzienda, boolean flagEscludiAzienda)
      throws Exception
  {
    try
    {
      return conduzioneDichiarataDAO
      .getSumSuperficieFromParticellaAndLastDichCons(idParticella, idAzienda, flagEscludiAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  /**
   * 
   * Metodo utilizzato nella modifica multipla per aggiornare 
   * su DB_STORICO_PARTICELLA i dati dell'irrigazione
   * 
   * @param elencoStoricoParticella
   * @param ruoloUtenza
   * @param flagIrrigabile
   * @param idIrrigazione
   * @throws Exception
   */
  public void aggiornaIrrigazione(StoricoParticellaVO[] elencoStoricoParticella,
      RuoloUtenza ruoloUtenza, boolean flagIrrigabile, Long idIrrigazione) throws Exception
  {
    try
    {
      //Lock della tabella per impedire più accessi contemporanei!!
      
      //Qui lavoro sulla particella quindi nel caso di piu conduzini o utilizzi
      //potrebbero esserci più record, ma io devo lavorare sulla singola particella!!
      ArrayList<Long> listIdStoricoParticella = new ArrayList<Long>();
      for (int i = 0; i < elencoStoricoParticella.length; i++)
      {
        Long idStoricoParticellaLg = elencoStoricoParticella[i].getIdStoricoParticella();
        if(!listIdStoricoParticella.contains(idStoricoParticellaLg))
        {
          listIdStoricoParticella.add(idStoricoParticellaLg);
        }       
      }
      long[] arrIdStoricoParticella = new long[listIdStoricoParticella.size()];
      for (int i = 0; i < listIdStoricoParticella.size(); i++)
      {
        Long idStoricoParticellaLg = (Long)listIdStoricoParticella.get(i);
        arrIdStoricoParticella[i] = idStoricoParticellaLg.longValue();        
      }
      storicoParticellaDAO.lockTableStoricoParticella(arrIdStoricoParticella);      
      //**********************************
      
      java.util.Date dataInizio = new java.util.Date(new Timestamp(System
          .currentTimeMillis()).getTime());
      // Ciclo le particelle selezionate
      listIdStoricoParticella = new ArrayList<Long>();
      for (int i = 0; i < elencoStoricoParticella.length; i++)
      {
        // TABELLA DB_STORICO_PARTICELLA
        StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) elencoStoricoParticella[i];
        Long idStoricoParticellaLg = elencoStoricoParticella[i].getIdStoricoParticella();
        if(!listIdStoricoParticella.contains(idStoricoParticellaLg))
        {
          listIdStoricoParticella.add(idStoricoParticellaLg);
          // Recupero il dato corrispettivo presente sul DB per verificare
          // se sono state effettuate delle modifiche
          StoricoParticellaVO oldStoricoParticellaVO = storicoParticellaDAO
              .findStoricoParticellaByPrimaryKey(storicoParticellaVO
                  .getIdStoricoParticella());
          if(flagIrrigabile)
          {
            storicoParticellaVO.setFlagIrrigabile(SolmrConstants.FLAG_S);
          }
          else
          {
            storicoParticellaVO.setFlagIrrigabile(SolmrConstants.FLAG_N);
          }        
          storicoParticellaVO.setIdIrrigazione(idIrrigazione);        
          
          ConduzioneDichiarataVO[] elencoConduzioneDichiarataVO = conduzioneDichiarataDAO
              .getListConduzioneDichiarataByidStoricoParticella(storicoParticellaVO
                  .getIdStoricoParticella());
          // Se sono stati modificati i dati irrigabilità
          if (!storicoParticellaVO.equalsDatiIrrigabilita(oldStoricoParticellaVO))
          {
            
          
            // Se la data di aggiornamento è uguale a SYSDATE
            if (DateUtils.formatDate(
                oldStoricoParticellaVO.getDataAggiornamento()).equalsIgnoreCase(
                DateUtils.formatDate(new java.util.Date(System
                    .currentTimeMillis()))))
            {
              // ... se esiste un record su DB_CONDUZIONE_DICHIARATA per
              // id_storico
              // particella selezionato
              if (elencoConduzioneDichiarataVO.length > 0)
              {
                // Storicizzo il record su DB_STORICO_PARTICELLA
                //oldStoricoParticellaVO.setDataFineValidita(new java.util.Date(
                    //new Timestamp(System.currentTimeMillis()).getTime()));
                //storicoParticellaDAO
                    //.updateStoricoParticella(oldStoricoParticellaVO);
                storicoParticellaDAO.storicizzaStoricoParticella(oldStoricoParticellaVO
                    .getIdStoricoParticella().longValue());
                // ... inserisco un nuovo record su DB_STORICO_PARTICELLA
                storicoParticellaVO.setDataInizioValidita(dataInizio);
                storicoParticellaVO.setDataAggiornamento(new java.util.Date(
                    new Timestamp(System.currentTimeMillis()).getTime()));
                storicoParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
                    .getIdUtente());
                storicoParticellaDAO.insertStoricoParticella(storicoParticellaVO);
              }
              // in caso contrario modifico il record selezionato
              else
              {
                // ... modifico il record su DB_STORICO_PARTICELLA
                storicoParticellaVO.setDataAggiornamento(new java.util.Date(
                    new Timestamp(System.currentTimeMillis()).getTime()));
                storicoParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
                    .getIdUtente());
                storicoParticellaDAO.updateStoricoParticella(storicoParticellaVO);
              }
            }
            // ... Altrimenti...
            else
            {
              // Storicizzo il record su DB_STORICO_PARTICELLA
              //oldStoricoParticellaVO.setDataFineValidita(new java.util.Date(
                  //new Timestamp(System.currentTimeMillis()).getTime()));
              //storicoParticellaDAO
                  //.updateStoricoParticella(oldStoricoParticellaVO);
              storicoParticellaDAO.storicizzaStoricoParticella(oldStoricoParticellaVO
                  .getIdStoricoParticella().longValue());
              // ... inserisco un nuovo record su DB_STORICO_PARTICELLA
              storicoParticellaVO.setDataInizioValidita(dataInizio);
              storicoParticellaVO.setDataAggiornamento(new java.util.Date(
                  new Timestamp(System.currentTimeMillis()).getTime()));
              storicoParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
                  .getIdUtente());
              storicoParticellaDAO.insertStoricoParticella(storicoParticellaVO);
            }
            
            ConduzioneParticellaVO conduzioneParticellaVO = (ConduzioneParticellaVO) storicoParticellaVO
            .getElencoConduzioni()[0];
          
            ConduzioneParticellaVO oldConduzioneParticella = conduzioneParticellaDAO
              .findConduzioneParticellaByPrimaryKey(conduzioneParticellaVO
                .getIdConduzioneParticella());
            // ... aggiorno comunque DB_CONDUZIONE_PARTICELLA perchè verranno
            // modificati gli utilizzi
            oldConduzioneParticella.setRecordModificato(SolmrConstants.FLAG_S);
            oldConduzioneParticella.setEsitoControllo(null);
            oldConduzioneParticella.setDataEsecuzione(null);
            conduzioneParticellaDAO
                .updateConduzioneParticella(oldConduzioneParticella);
          }       
          
          
        } //for
      } //if
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
  }
  
  public ParticellaAssVO[] getParticellaForDocAzCessata(Long idParticella) 
    throws Exception 
  {
    try 
    {
      return eventoParticellaDAO.getParticellaForDocAzCessata(idParticella);
    }
    catch(DataAccessException dae) 
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public Vector<ParticellaAssVO> getParticelleDocCor(Long idDocumento) throws Exception
  {
    try 
    {
      return eventoParticellaDAO.getParticelleDocCor(idDocumento);
    }
    catch(DataAccessException dae) 
    {
      throw new Exception(dae.getMessage());
    }
  }

  public String getDescrizioneByIdTipologiaVino(Long idTipologiaVino) 
    throws Exception
  {
    try
    {
      return tipoTipologiaVinoDAO.getDescrizioneByIdTipologiaVino(idTipologiaVino);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public String getVarietaByIdVitigno(Long idVitigno)   
    throws Exception
  {
    try
    {
      return tipoTipologiaVinoDAO.getVarietaByIdVitigno(idVitigno);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  
  /**
   * 
   * Restituisce un array di stringhe in cui sono messe in disrtinct la sigla delle provincie delle
   * particelle
   * 
   * @param idUte
   * @return
   * @throws DataAccessException
   */
  public String[] getIstatProvFromConduzione(long idAzienda)
      throws Exception
  {
    try
    {
      return conduzioneParticellaDAO
        .getIstatProvFromConduzione(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  
  public BigDecimal getPercentualePosesso(long idAzienda, long idParticella)
    throws Exception
  {
    try
    {
      return conduzioneParticellaDAO.getPercentualePosesso(idAzienda, idParticella);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  /**
   * 
   * Metodo per effettuare il riepilogo delle particelle con basso contenuto 
   * carbonio organico 
   * 
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @return
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoAreaG(Long idAzienda, 
      String escludiAsservimento)
      throws Exception
  {
    try
    {
      return storicoParticellaDAO.riepilogoAreaG(idAzienda,
          escludiAsservimento);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  
  /**
   * 
   * Metodo per effettuare il riepilogo delle particelle 
   * per basso contenuto carbonio organico
   * ad una determiinata dichiarazione di consistenza
   * 
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @return
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoAreaGParticelleDichiarate(Long idAzienda, 
      String escludiAsservimento, Long idDichiarazioneConsistenza)
      throws Exception
  {
    try
    {
      return storicoParticellaDAO.riepilogoAreaGParticelleDichiarate(idAzienda,
          escludiAsservimento,idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  /**
   * 
   * Metodo per effettuare il riepilogo delle particelle con areea 
   * soggetta ad erosione  
   * 
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @return
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoAreaH(Long idAzienda, 
      String escludiAsservimento)
      throws Exception
  {
    try
    {
      return storicoParticellaDAO.riepilogoAreaH(idAzienda,
          escludiAsservimento);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  
  /**
   * 
   * Metodo per effettuare il riepilogo delle particelle 
   * per area soggetta ad erosione
   * ad una determiinata dichiarazione di consistenza
   * 
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @return
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoAreaHParticelleDichiarate(Long idAzienda, 
      String escludiAsservimento, Long idDichiarazioneConsistenza)
      throws Exception
  {
    try
    {
      return storicoParticellaDAO.riepilogoAreaHParticelleDichiarate(idAzienda,
          escludiAsservimento,idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  /**
   * 
   * Metodo per effettuare il riepilogo delle particelle per Zona Altimetrica
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @return
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoZonaAltimetrica(Long idAzienda, 
      String escludiAsservimento)
      throws Exception
  {
    try
    {
      return storicoParticellaDAO.riepilogoZonaAltimetrica(idAzienda, escludiAsservimento);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  /**
   * Metodo per effettuare il riepilogo delle particelle per Zona Altimetrica
   * ad una determiinata dichiarazione di consistenza
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @return
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoZonaAltimetricaParticelleDichiarate(Long idAzienda, 
      String escludiAsservimento, Long idDichiarazioneConsistenza)
      throws Exception
  {
    try
    {
      return storicoParticellaDAO.riepilogoZonaAltimetricaParticelleDichiarate(
          idAzienda, escludiAsservimento, idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  /**
   * 
   * Metodo per effettuare il riepilogo delle particelle per Caso Particolare
   * 
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @return
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoCasoParticolare(Long idAzienda, 
      String escludiAsservimento)
      throws Exception
  {
    try
    {
      return storicoParticellaDAO.riepilogoCasoParticolare(idAzienda, escludiAsservimento);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  /**
   * 
   * Metodo per effettuare il riepilogo delle particelle per Caso Particolare 
   * ad una determiinata dichiarazione di consistenza
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @return
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoCasoParticolareParticelleDichiarate(Long idAzienda, 
      String escludiAsservimento, Long idDichiarazioneConsistenza)
      throws Exception
  {
    try
    {
      return storicoParticellaDAO.riepilogoCasoParticolareParticelleDichiarate(
           idAzienda, escludiAsservimento, idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  
  public Long getDefaultIdGenereIscrizione()
      throws Exception
  {
    try
    {
      return tipoGenereIscrizioneDAO.getDefaultIdGenereIscrizione();
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public Vector<TipoGenereIscrizioneVO> getListTipoGenereIscrizione()
    throws Exception
  {
    try
    {
      return tipoGenereIscrizioneDAO.getListTipoGenereIscrizione();
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public BigDecimal getSupEleggPlSqlTotale(long idParticellaCertificata, long idCatalogoMatrice)
    throws Exception
  {
    try
    {
      return conduzioneParticellaDAO.getSupEleggPlSqlTotale(idParticellaCertificata, idCatalogoMatrice);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public BigDecimal getSupEleggNettaPlSqlTotale(long idParticellaCertificata, long idCatalogoMatrice)
    throws Exception
  {
    try
    {
      return conduzioneDichiarataDAO.getSupEleggNettaPlSqlTotale(idParticellaCertificata, idCatalogoMatrice);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public Vector<TipoMenzioneGeograficaVO> getListTipoMenzioneGeografica(long idParticella, 
      Long idTipologiaVino, java.util.Date dataInserimentoDichiarazione)
    throws Exception
  {
    try
    {
      return tipoMenzioneGeograficaDAO.getListTipoMenzioneGeografica(idParticella, idTipologiaVino, dataInserimentoDichiarazione);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public Vector<StoricoParticellaVO> getListParticelleByIdDocumento(Long idDocumento, String[] orderBy)
      throws Exception
  {
    try
    {
      return storicoParticellaDAO.getListParticelleByIdDocumento(idDocumento, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  
  
  /**
   * 
   * metodo usato nella modifica multipla aggiorna percentuale di posesso
   * 
   * 
   * @param elencoIdConduzioneParticella
   * @param vIdParticella
   * @param ruoloUtenza
   * @param idAzienda
   * @param percentualePossesso
   * @throws Exception
   */
  public void cambiaPercentualePossesso(Long[] elencoIdConduzioneParticella, Vector<Long> vIdParticella,
      RuoloUtenza ruoloUtenza, Long idAzienda, BigDecimal percentualePossesso)
      throws Exception
  {
    try
    {
      
      //Lock della tabella per impedire più accessi contemporanei!!
      long[] arrIdConduzioneParticella = new long[elencoIdConduzioneParticella.length]; 
      for (int i = 0; i < elencoIdConduzioneParticella.length; i++)
      { 
        arrIdConduzioneParticella[i] = elencoIdConduzioneParticella[i].longValue();        
      }
      conduzioneParticellaDAO.lockTableConduzioneParticella(arrIdConduzioneParticella);      
      //**********************************
      
      for (int i = 0; i < vIdParticella.size(); i++)
      {
        Vector<Long> vIdConduzioneNoAsservimento = conduzioneParticellaGaaDAO
            .getIdConduzioneNoAsservimentoFromIdAziendaIdParticella(idAzienda.longValue(), vIdParticella.get(i));
        Vector<Long> vIdConduzioneAsservimento = conduzioneParticellaGaaDAO.getIdConduzioniAsserviteFromIdAziendaIdParticella(
            idAzienda.longValue(), vIdParticella.get(i));
        //devono esistere almeno due conduzioni non in asservimento
        BigDecimal sumUtilizzoParticella = null;        
        if(Validator.isNotEmpty(vIdConduzioneNoAsservimento)
          && (vIdConduzioneNoAsservimento.size() > 1))
        {
          sumUtilizzoParticella = utilizzoParticellaGaaDAO.getSumSupUtilizzoParticellaAzienda(
              idAzienda.longValue(), vIdParticella.get(i));          
        }
        BigDecimal sumAsservimentoParticella = null;
        if(Validator.isNotEmpty(vIdConduzioneAsservimento)
            && (vIdConduzioneAsservimento.size() > 1))
        {
          sumAsservimentoParticella = conduzioneParticellaGaaDAO.getSumSupCondottaAsservimentoParticellaAzienda(
              idAzienda.longValue(), vIdParticella.get(i));          
        }
        
        
        //gestire lo sfrigu finale
        BigDecimal totalePercentualePossesso = new BigDecimal(0);
        if(Validator.isNotEmpty(vIdConduzioneNoAsservimento))
        {
          BigDecimal percentualePossessoTmp = percentualePossesso;
          for(int j=0; j < vIdConduzioneNoAsservimento.size(); j++)
          {
          
            Long idConduzioneParticella = vIdConduzioneNoAsservimento.get(j);
            // Recupero i dati della conduzione particella
            ConduzioneParticellaVO oldConduzioneParticella = conduzioneParticellaDAO
                .findConduzioneParticellaByPrimaryKey(idConduzioneParticella);
            if(vIdConduzioneNoAsservimento.size() > 1)
            {                   
              //ultima conduzione raccolgo gli sfrigu
              if(vIdConduzioneNoAsservimento.size()-1 == j)
              {
                percentualePossessoTmp = percentualePossesso.subtract(totalePercentualePossesso);
                
                if(percentualePossessoTmp.compareTo(new BigDecimal(0)) == 0)
                {
                  percentualePossessoTmp = new BigDecimal(0.01);
                }
              }
              else
              {
                BigDecimal sumUtilizzoConduzione = utilizzoParticellaGaaDAO.getSumSupUtilizzoConduzione(idConduzioneParticella);
                //riproporziono
                percentualePossessoTmp = sumUtilizzoConduzione.multiply(percentualePossesso);
                percentualePossessoTmp = percentualePossessoTmp.divide(sumUtilizzoParticella,2,BigDecimal.ROUND_HALF_UP);
                if(percentualePossessoTmp.compareTo(new BigDecimal(0)) == 0)
                {
                  percentualePossessoTmp = new BigDecimal(0.01);
                }
                
                totalePercentualePossesso = totalePercentualePossesso.add(percentualePossessoTmp);
              }            
              
            }
            
            if(oldConduzioneParticella.getPercentualePossesso().compareTo(percentualePossessoTmp) !=0)
            { 
              aggiornaConduzionePerPercentuale(idConduzioneParticella, 
                  idAzienda, oldConduzioneParticella, percentualePossessoTmp, ruoloUtenza);
            }
            
          } //for no asservimento         
        } //if no asservimento
        
        //Asservimento
        totalePercentualePossesso = new BigDecimal(0);
        if(Validator.isNotEmpty(vIdConduzioneAsservimento))
        {
          BigDecimal percentualePossessoTmp = percentualePossesso;
          for(int j=0; j < vIdConduzioneAsservimento.size(); j++)
          {
          
            Long idConduzioneParticella = vIdConduzioneAsservimento.get(j);
            // Recupero i dati della conduzione particella
            ConduzioneParticellaVO oldConduzioneParticella = conduzioneParticellaDAO
                .findConduzioneParticellaByPrimaryKey(idConduzioneParticella);
            if(vIdConduzioneAsservimento.size() > 1)
            {                   
              //ultima conduzione raccolgo gli sfrigu
              if(vIdConduzioneAsservimento.size()-1 == j)
              {
                percentualePossessoTmp = percentualePossesso.subtract(totalePercentualePossesso);
                
                if(percentualePossessoTmp.compareTo(new BigDecimal(0)) == 0)
                {
                  percentualePossessoTmp = new BigDecimal(0.01);
                }
              }
              else
              {
                BigDecimal supCondottaBg = new BigDecimal(oldConduzioneParticella.getSuperficieCondotta().replace(',','.'));
                //riproporziono
                percentualePossessoTmp = supCondottaBg.multiply(percentualePossesso);
                percentualePossessoTmp = percentualePossessoTmp.divide(sumAsservimentoParticella,2,BigDecimal.ROUND_HALF_UP);
                if(percentualePossessoTmp.compareTo(new BigDecimal(0)) == 0)
                {
                  percentualePossessoTmp = new BigDecimal(0.01);
                }
                
                totalePercentualePossesso = totalePercentualePossesso.add(percentualePossessoTmp);
              }            
              
            }
            
            if(oldConduzioneParticella.getPercentualePossesso().compareTo(percentualePossessoTmp) !=0)
            { 
              aggiornaConduzionePerPercentuale(idConduzioneParticella, 
                  idAzienda, oldConduzioneParticella, percentualePossessoTmp, ruoloUtenza);
            }
            
          } //for asservimento         
        } //if asservimento
          
            
          
          
        
        
        
      } // for vIdParticella
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
  }
  
  /**
   * 
   * metodo utilizzato nella modifca multipla allinea percentuale sup utilizzata
   * 
   * 
   * @param elencoIdConduzioneParticella
   * @param vIdParticella
   * @param ruoloUtenza
   * @param idAzienda
   * @throws Exception
   */
  public void cambiaPercentualePossessoSupUtilizzata(Vector<Long> vIdConduzioni,
      RuoloUtenza ruoloUtenza, Long idAzienda)
      throws Exception
  {
    try
    {     
      for(int i=0; i<vIdConduzioni.size(); i++)
      {
        BigDecimal percentualePossesso = new BigDecimal(0);
        Long idConduzioneParticella = vIdConduzioni.get(i);
        // Recupero i dati della conduzione particella
        ConduzioneParticellaVO oldConduzioneParticella = conduzioneParticellaDAO
            .findConduzioneParticellaByPrimaryKey(idConduzioneParticella);
        StoricoParticellaVO strVO = storicoParticellaDAO
            .findStoricoParticellaVOByIdConduzioneParticella(idConduzioneParticella);
        // Cerco i dati del foglio associati allo storico particella
        FoglioVO foglioVO = foglioDAO.findFoglioByParameters(strVO
            .getIstatComune(), strVO.getFoglio(),
            strVO.getSezione());
        
        
        BigDecimal supConfronto = null;
        
        if(Validator.isNotEmpty(foglioVO)
            && Validator.isNotEmpty(foglioVO.getFlagStabilizzazione())
            && (foglioVO.getFlagStabilizzazione().compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0)
            && Validator.validateDouble(strVO.getSuperficieGrafica(), SolmrConstants.FORMAT_SUP_UTILIZZATA) != null 
            && Double.parseDouble(strVO.getSuperficieGrafica().replace(',', '.')) > 0)
        {
          supConfronto =  new BigDecimal(strVO.getSuperficieGrafica().replace(',','.'));
        }
        else
        {  
          if(Validator.isNotEmpty(strVO.getSupCatastale())
              && (new BigDecimal(strVO.getSupCatastale().replace(',','.')).compareTo(new BigDecimal(0)) > 0))
          {
            supConfronto = new BigDecimal(strVO.getSupCatastale().replace(',','.'));
          }
          else if(Validator.isNotEmpty(strVO.getSuperficieGrafica()))
          {
            supConfronto = new BigDecimal(strVO.getSuperficieGrafica().replace(',','.'));
          }
        }
        
        if(oldConduzioneParticella.getIdTitoloPossesso().longValue() != 5)
        {
          BigDecimal sumUtilizzoConduzione = utilizzoParticellaGaaDAO.getSumSupUtilizzoConduzione(idConduzioneParticella);
          percentualePossesso = sumUtilizzoConduzione.divide(supConfronto,4,BigDecimal.ROUND_HALF_UP);
          percentualePossesso = percentualePossesso.multiply(new BigDecimal(100));
          percentualePossesso = percentualePossesso.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        else
        {
          BigDecimal supCondottaDB = new BigDecimal(oldConduzioneParticella.getSuperficieCondotta().replace(',','.'));
          percentualePossesso = supCondottaDB.divide(supConfronto,4,BigDecimal.ROUND_HALF_UP);
          percentualePossesso = percentualePossesso.multiply(new BigDecimal(100));
          percentualePossesso = percentualePossesso.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        
        if(percentualePossesso.compareTo(new BigDecimal(100)) > 0)
        {
          percentualePossesso = new BigDecimal(100);
        }
        else if(percentualePossesso.compareTo(new BigDecimal(0)) == 0)
        {
          percentualePossesso = new BigDecimal(0.01);
        }
        
          
        if(oldConduzioneParticella.getPercentualePossesso().compareTo(percentualePossesso) !=0)
        {            
          aggiornaConduzionePerPercentuale(idConduzioneParticella, 
              idAzienda, oldConduzioneParticella, percentualePossesso, ruoloUtenza);
        }
        
      } //for vIdConduzione
          
        
        
        
      
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
  }
  
  
  private void aggiornaConduzionePerPercentuale(Long idConduzioneParticella, 
      Long idAzienda, ConduzioneParticellaVO oldConduzioneParticella, BigDecimal percentualePossessoTmp,
      RuoloUtenza ruoloUtenza) throws DataAccessException, Exception
  {
    // Cerco se esiste almeno una dichiarazione di consistenza
    ConduzioneDichiarataVO[] elencoConduzioniDichiarate = conduzioneDichiarataDAO
        .getListConduzioneDichiarataByIdConduzioneParticellaAndIdAzienda(
            idConduzioneParticella, idAzienda, false, null);
    // Se esiste almeno una dichiarazione di consistenza
    if (elencoConduzioniDichiarate != null
        && elencoConduzioniDichiarate.length > 0)
    {
      // Cesso il record su DB_CONDUZIONE_PARTICELLA
      oldConduzioneParticella.setDataFineConduzione(new java.util.Date(
          new Timestamp(System.currentTimeMillis()).getTime()));
      conduzioneParticellaDAO
          .updateConduzioneParticella(oldConduzioneParticella);
      // Creo il nuovo oggetto da inserire
      ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
      // Setto i parametri
      conduzioneParticellaVO.setIdParticella(oldConduzioneParticella
          .getIdParticella());
      conduzioneParticellaVO.setIdUte(oldConduzioneParticella.getIdUte());
      conduzioneParticellaVO.setIdTitoloPossesso(oldConduzioneParticella.getIdTitoloPossesso());
      conduzioneParticellaVO.setSuperficieCondotta(oldConduzioneParticella.getSuperficieCondotta());
      conduzioneParticellaVO.setPercentualePossesso(percentualePossessoTmp);
      conduzioneParticellaVO.setFlagUtilizzoParte(null);
      conduzioneParticellaVO.setNote(null);
      conduzioneParticellaVO.setDataInizioConduzione(new java.util.Date(
          new Timestamp(System.currentTimeMillis()).getTime()));
      conduzioneParticellaVO.setDataFineConduzione(null);
      conduzioneParticellaVO.setDataAggiornamento(new java.util.Date(
          new Timestamp(System.currentTimeMillis()).getTime()));
      conduzioneParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
          .getIdUtente());
      conduzioneParticellaVO.setRecordModificato(SolmrConstants.FLAG_S);
      conduzioneParticellaVO.setEsitoControllo(null);
      conduzioneParticellaVO.setDataEsecuzione(null);
      if (Validator.isNotEmpty(oldConduzioneParticella
          .getSuperficieAgronomica()))
      {
        conduzioneParticellaVO.setSuperficieAgronomica(StringUtils
            .parseSuperficieField(oldConduzioneParticella
                .getSuperficieAgronomica()));
      }
      // Inserisco il nuovo record
      Long newIdConduzioneParticella = conduzioneParticellaDAO
          .insertConduzioneParticella(conduzioneParticellaVO);
      // Recupero i documenti attivi legati alla conduzione storicizzata
      Vector<DocumentoVO> elencoDocumenti = documentoDAO
          .getListDocumentiByIdConduzioneParticella(
              idConduzioneParticella, true);
      // Se ce ne sono
      if (elencoDocumenti != null && elencoDocumenti.size() > 0)
      {
        Vector<DocumentoConduzioneVO> elencoConduzioni = null;
        for (int c = 0; c < elencoDocumenti.size(); c++)
        {
          DocumentoVO documentoVO = (DocumentoVO) elencoDocumenti
              .elementAt(c);
          // Ricerco le conduzioni attive legate al precedente documento
          elencoConduzioni = documentoDAO
              .getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella(
                  documentoVO.getIdDocumento(), oldConduzioneParticella
                      .getIdConduzioneParticella(), true);
          // Se ne trovo
          if (elencoConduzioni != null && elencoConduzioni.size() > 0)
          {
            for (int d = 0; d < elencoConduzioni.size(); d++)
            {
              DocumentoConduzioneVO documentoConduzioneVO = (DocumentoConduzioneVO) elencoConduzioni
                  .elementAt(d);
              // Storicizzo i records presenti su DB_DOCUMENTO_CONDUZIONE
              documentoConduzioneVO
                  .setDataFineValidita(new java.util.Date(new Timestamp(
                      System.currentTimeMillis()).getTime()));
              documentoDAO
                  .updateDocumentoConduzione(documentoConduzioneVO);
              // Inserisco i nuovi records su DB_DOCUMENTO_CONDUZIONE
              documentoConduzioneVO.setIdDocumentoConduzione(null);
              documentoConduzioneVO
                  .setIdConduzioneParticella(newIdConduzioneParticella);
              documentoConduzioneVO
                  .setDataInserimento(new java.util.Date(new Timestamp(
                      System.currentTimeMillis()).getTime()));
              documentoConduzioneVO
                  .setDataInizioValidita(new java.util.Date(
                      new Timestamp(System.currentTimeMillis()).getTime()));
              documentoConduzioneVO.setDataFineValidita(null);
              documentoDAO
                  .insertDocumentoConduzione(documentoConduzioneVO);
            }
          }
        }
      }
      // Recupero gli utilizzi legati alla vecchia conduzione
      UtilizzoParticellaVO[] elencoUtilizzi = getListUtilizzoParticellaVOByIdConduzioneParticella(
          idConduzioneParticella, null, false);
      // Se ne trovo
      if (elencoUtilizzi != null && elencoUtilizzi.length > 0)
      {
        // Li duplico associandoli alla nuova condizione appena inserita
        for (int b = 0; b < elencoUtilizzi.length; b++)
        {
          UtilizzoParticellaVO oldUtilizzoParticellaVO = (UtilizzoParticellaVO) elencoUtilizzi[b];
          // Creo il nuovo oggetto da inserire
          /*UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
          utilizzoParticellaVO.setIdUtilizzo(oldUtilizzoParticellaVO
              .getIdUtilizzo());
          utilizzoParticellaVO
              .setIdConduzioneParticella(newIdConduzioneParticella);
          utilizzoParticellaVO
              .setSuperficieUtilizzata(oldUtilizzoParticellaVO
                  .getSuperficieUtilizzata());
          utilizzoParticellaVO.setDataAggiornamento(new java.util.Date(
              new Timestamp(System.currentTimeMillis()).getTime()));
          utilizzoParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
              .getIdUtente());
          utilizzoParticellaVO.setAnno(DateUtils.getCurrentYear()
              .toString());
          utilizzoParticellaVO.setNote(null);
          utilizzoParticellaVO
              .setIdUtilizzoSecondario(oldUtilizzoParticellaVO
                  .getIdUtilizzoSecondario());
          utilizzoParticellaVO
              .setSupUtilizzataSecondaria(oldUtilizzoParticellaVO
                  .getSupUtilizzataSecondaria());
          utilizzoParticellaVO.setIdVarieta(oldUtilizzoParticellaVO
              .getIdVarieta());
          utilizzoParticellaVO
              .setIdVarietaSecondaria(oldUtilizzoParticellaVO
                  .getIdVarietaSecondaria());
          utilizzoParticellaVO.setAnnoImpianto(oldUtilizzoParticellaVO
              .getAnnoImpianto());
          utilizzoParticellaVO.setIdImpianto(oldUtilizzoParticellaVO
              .getIdImpianto());
          utilizzoParticellaVO.setSestoSuFile(oldUtilizzoParticellaVO
              .getSestoSuFile());
          utilizzoParticellaVO.setSestoTraFile(oldUtilizzoParticellaVO
              .getSestoTraFile());
          utilizzoParticellaVO
              .setNumeroPianteCeppi(oldUtilizzoParticellaVO
                  .getNumeroPianteCeppi());*/
          
          oldUtilizzoParticellaVO
            .setIdConduzioneParticella(newIdConduzioneParticella);
          oldUtilizzoParticellaVO.setDataAggiornamento(new java.util.Date(
              new Timestamp(System.currentTimeMillis()).getTime()));
          oldUtilizzoParticellaVO.setIdUtenteAggiornamento(ruoloUtenza
              .getIdUtente());
          oldUtilizzoParticellaVO.setAnno(DateUtils.getCurrentYear()
              .toString());
          oldUtilizzoParticellaVO.setNote(null);
          // Inserisco il nuovo utilizzo
          utilizzoParticellaDAO
              .insertUtilizzoParticella(oldUtilizzoParticellaVO);
        }
      }
    }
    // Se invece non risultano dichiarazioni di consistenza
    else
    {
      // Effettuo semplicemente un'update su DB_CONDUZIONE_PARTICELLA
      oldConduzioneParticella.setPercentualePossesso(percentualePossessoTmp);
      oldConduzioneParticella.setDataAggiornamento(new java.util.Date(
          new Timestamp(System.currentTimeMillis()).getTime()));
      oldConduzioneParticella.setIdUtenteAggiornamento(ruoloUtenza
          .getIdUtente());
      oldConduzioneParticella.setRecordModificato(SolmrConstants.FLAG_S);
      oldConduzioneParticella.setEsitoControllo(null);
      oldConduzioneParticella.setDataEsecuzione(null);
      conduzioneParticellaDAO
          .updateConduzioneParticella(oldConduzioneParticella);              
    }
    
  }
  
  
  /**
   * mapping tra db_utilizzo_dichiarato e db_utilizzo_particella
   * 
   * 
   * 
   * @param utilizzoDichiaratoVO
   * @param utilizzoParticellaVO
   */
  public void convertUtilDichIntoUtilPart(UtilizzoDichiaratoVO utilizzoDichiaratoVO, 
      UtilizzoParticellaVO utilizzoParticellaVO)
  {
    utilizzoParticellaVO.setAnno(utilizzoDichiaratoVO.getAnno());
    utilizzoParticellaVO.setIdUtilizzo(utilizzoDichiaratoVO.getIdUtilizzo());
    utilizzoParticellaVO.setSuperficieUtilizzata(utilizzoDichiaratoVO.getSuperficieUtilizzata());
    utilizzoParticellaVO.setNote(utilizzoDichiaratoVO.getNote());
    utilizzoParticellaVO.setDataAggiornamento(utilizzoDichiaratoVO.getDataAggiornamento());
    utilizzoParticellaVO.setIdUtenteAggiornamento(utilizzoDichiaratoVO.getIdUtenteAggiornamento());
    utilizzoParticellaVO.setIdUtilizzoSecondario(utilizzoDichiaratoVO.getIdUtilizzoSecondario());
    utilizzoParticellaVO.setSupUtilizzataSecondaria(utilizzoDichiaratoVO.getSupUtilizzataSecondaria());
    utilizzoParticellaVO.setIdVarieta(utilizzoDichiaratoVO.getIdVarieta());
    utilizzoParticellaVO.setIdVarietaSecondaria(utilizzoDichiaratoVO.getIdVarietaSecondaria());
    utilizzoParticellaVO.setAnnoImpianto(utilizzoDichiaratoVO.getAnnoImpianto());
    utilizzoParticellaVO.setIdImpianto(utilizzoDichiaratoVO.getIdImpianto());
    utilizzoParticellaVO.setSestoSuFile(utilizzoDichiaratoVO.getSestoSuFile());
    utilizzoParticellaVO.setSestoTraFile(utilizzoDichiaratoVO.getSestoTraFile());
    utilizzoParticellaVO.setNumeroPianteCeppi(utilizzoDichiaratoVO.getNumeroPianteCeppi());
    utilizzoParticellaVO.setIdTipoDettaglioUso(utilizzoDichiaratoVO.getIdTipoDettaglioUso());
    utilizzoParticellaVO.setIdTipoDettaglioUsoSecondario(utilizzoDichiaratoVO.getIdTipoDettaglioUsoSecondario());
    utilizzoParticellaVO.setIdTipoEfa(utilizzoDichiaratoVO.getIdTipoEfa());
    utilizzoParticellaVO.setValoreOriginale(utilizzoDichiaratoVO.getValoreOriginale());
    utilizzoParticellaVO.setValoreDopoConversione(utilizzoDichiaratoVO.getValoreDopoConversione());
    utilizzoParticellaVO.setValoreDopoPonderazione(utilizzoDichiaratoVO.getValoreDopoPonderazione());
    utilizzoParticellaVO.setIdTipoPeriodoSemina(utilizzoDichiaratoVO.getIdTipoPeriodoSemina());
    utilizzoParticellaVO.setIdTipoPeriodoSeminaSecondario(utilizzoDichiaratoVO.getIdTipoPeriodoSeminaSecondario());
    utilizzoParticellaVO.setIdCatalogoMatrice(utilizzoDichiaratoVO.getIdCatalogoMatrice());
    utilizzoParticellaVO.setIdCatalogoMatriceSecondario(utilizzoDichiaratoVO.getIdCatalogoMatriceSecondario());
    utilizzoParticellaVO.setIdSemina(utilizzoDichiaratoVO.getIdSemina());
    utilizzoParticellaVO.setIdSeminaSecondario(utilizzoDichiaratoVO.getIdSeminaSecondario());
    utilizzoParticellaVO.setDataInizioDestinazione(utilizzoDichiaratoVO.getDataInizioDestinazione());
    utilizzoParticellaVO.setDataFineDestinazione(utilizzoDichiaratoVO.getDataFineDestinazione());
    utilizzoParticellaVO.setIdFaseAllevamento(utilizzoDichiaratoVO.getIdFaseAllevamento());
    utilizzoParticellaVO.setIdPraticaMantenimento(utilizzoDichiaratoVO.getIdPraticaMantenimento());
    utilizzoParticellaVO.setDataInizioDestinazioneSec(utilizzoDichiaratoVO.getDataInizioDestinazioneSec());
    utilizzoParticellaVO.setDataFineDestinazioneSec(utilizzoDichiaratoVO.getDataFineDestinazioneSec());
    
  }
  
  /**
   * 
   * mapping tra db_utilizo_consociato_dich e db_utilizzo_consociato
   * 
   * 
   * @param utilizzoConsociatoDichiaratoVO
   * @param utilizzoConsociatoVO
   */
  public void convertUtilConsDichIntoUtilCons(UtilizzoConsociatoDichiaratoVO utilizzoConsociatoDichiaratoVO, 
      UtilizzoConsociatoVO utilizzoConsociatoVO)
  {
    utilizzoConsociatoVO.setIdPianteConsociate(utilizzoConsociatoDichiaratoVO.getIdPianteConsociate());
    utilizzoConsociatoVO.setNumeroPiante(utilizzoConsociatoDichiaratoVO.getNumeroPiante());
  }
  
}
