package it.csi.solmr.business.anag;

import it.csi.csi.wrapper.SystemException;
import it.csi.csi.wrapper.UnrecoverableException;
import it.csi.jsf.htmpl.Htmpl;
import it.csi.papua.papuaserv.dto.legacy.axis.RuoloUtenzaPapua;
import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.MacroCU;
import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni;
import it.csi.sigop.dto.services.PagamentiErogatiVO;
import it.csi.sigop.dto.services.RecuperiPregressiVO;
import it.csi.sigop.dto.services.SchedaCreditoVO;
import it.csi.smranag.smrgaa.business.AgriWellGaaLocal;
import it.csi.smranag.smrgaa.business.DocumentoGaaLocal;
import it.csi.smranag.smrgaa.business.StampaGaaLocal;
import it.csi.smranag.smrgaa.dto.AllegatoDichiarazioneVO;
import it.csi.smranag.smrgaa.dto.AllegatoDocumentoVO;
import it.csi.smranag.smrgaa.dto.BaseCodeDescription;
import it.csi.smranag.smrgaa.dto.ParticellaDettaglioValidazioniVO;
import it.csi.smranag.smrgaa.dto.PlSqlCodeDescription;
import it.csi.smranag.smrgaa.dto.SuperficieDescription;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.dto.search.FiltriRicercaTerrenoImportaAsservimentoVO;
import it.csi.smranag.smrgaa.dto.search.FiltriRicercaTerrenoVO;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaTerreniImportaAsservimentoVO;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaTerreniVO;
import it.csi.smranag.smrgaa.dto.servizi.vitiserv.DirittoGaaVO;
import it.csi.smranag.smrgaa.dto.stampe.ConsistenzaZootecnicaStampa;
import it.csi.smranag.smrgaa.dto.stampe.QuadroDTerreni;
import it.csi.smranag.smrgaa.dto.stampe.TipoAllegatoVO;
import it.csi.smranag.smrgaa.dto.ws.cciaa.UvResponseCCIAA;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.smrcomms.reportdin.dto.TipologiaReportVO;
import it.csi.smrcomms.siapcommws.InvioPostaCertificata;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoVO;
import it.csi.smrcomms.smrcomm.dto.datientiprivati.DatiEntePrivatoVO;
import it.csi.smrcomms.smrcomm.dto.filtro.EntePrivatoFiltroVO;
import it.csi.smrvit.vitiserv.dto.diritto.DirittoVO;
import it.csi.solmr.business.anag.sian.SianLocal;
import it.csi.solmr.business.anag.stampe.StampeLocal;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.DoubleStringcodeDescription;
import it.csi.solmr.dto.ProvinciaVO;
import it.csi.solmr.dto.StringcodeDescription;
import it.csi.solmr.dto.UfficioZonaIntermediarioVO;
import it.csi.solmr.dto.UtenteIrideVO;
import it.csi.solmr.dto.anag.AllevamentoAnagVO;
import it.csi.solmr.dto.anag.AnagAAEPAziendaVO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.AnagParticellaExcelVO;
import it.csi.solmr.dto.anag.AnagraficaAzVO;
import it.csi.solmr.dto.anag.AziendaCollegataVO;
import it.csi.solmr.dto.anag.BancaSportelloVO;
import it.csi.solmr.dto.anag.CategorieAllevamentoAnagVO;
import it.csi.solmr.dto.anag.ConsistenzaVO;
import it.csi.solmr.dto.anag.ContoCorrenteVO;
import it.csi.solmr.dto.anag.DelegaVO;
import it.csi.solmr.dto.anag.DocumentoConduzioneVO;
import it.csi.solmr.dto.anag.DocumentoProprietarioVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.ElencoAziendeParticellaVO;
import it.csi.solmr.dto.anag.ElencoNotificheVO;
import it.csi.solmr.dto.anag.ErrAnomaliaDicConsistenzaVO;
import it.csi.solmr.dto.anag.EsitoControlloDocumentoVO;
import it.csi.solmr.dto.anag.EsitoControlloParticellaVO;
import it.csi.solmr.dto.anag.EsitoPianoGraficoVO;
import it.csi.solmr.dto.anag.FabbricatoVO;
import it.csi.solmr.dto.anag.FoglioVO;
import it.csi.solmr.dto.anag.FrmManodoperaVO;
import it.csi.solmr.dto.anag.IntermediarioAnagVO;
import it.csi.solmr.dto.anag.ManodoperaVO;
import it.csi.solmr.dto.anag.NotificaEntitaVO;
import it.csi.solmr.dto.anag.NotificaVO;
import it.csi.solmr.dto.anag.ParametroRitornoVO;
import it.csi.solmr.dto.anag.ParticellaAssVO;
import it.csi.solmr.dto.anag.ParticellaAziendaVO;
import it.csi.solmr.dto.anag.ParticellaCertElegVO;
import it.csi.solmr.dto.anag.ParticellaCertificataVO;
import it.csi.solmr.dto.anag.ParticellaUtilizzoVO;
import it.csi.solmr.dto.anag.ParticellaVO;
import it.csi.solmr.dto.anag.ParticelleVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.dto.anag.ProcedimentoAziendaVO;
import it.csi.solmr.dto.anag.ProprietaCertificataVO;
import it.csi.solmr.dto.anag.RespAnagFascicoloVO;
import it.csi.solmr.dto.anag.TemporaneaPraticaAziendaVO;
import it.csi.solmr.dto.anag.TerreniVO;
import it.csi.solmr.dto.anag.TesserinoFitoSanitarioVO;
import it.csi.solmr.dto.anag.TipoASLAnagVO;
import it.csi.solmr.dto.anag.TipoCategoriaAnimaleAnagVO;
import it.csi.solmr.dto.anag.TipoCategoriaDocumentoVO;
import it.csi.solmr.dto.anag.TipoCategoriaNotificaVO;
import it.csi.solmr.dto.anag.TipoCessazioneVO;
import it.csi.solmr.dto.anag.TipoDocumentoVO;
import it.csi.solmr.dto.anag.TipoFormaConduzioneVO;
import it.csi.solmr.dto.anag.TipoIscrizioneINPSVO;
import it.csi.solmr.dto.anag.TipoSezioniAaepVO;
import it.csi.solmr.dto.anag.TipoSpecieAnimaleAnagVO;
import it.csi.solmr.dto.anag.TipoStatoDocumentoVO;
import it.csi.solmr.dto.anag.TipoTipologiaAziendaVO;
import it.csi.solmr.dto.anag.UteVO;
import it.csi.solmr.dto.anag.attestazioni.AttestazioneAziendaVO;
import it.csi.solmr.dto.anag.attestazioni.AttestazioneDichiarataVO;
import it.csi.solmr.dto.anag.attestazioni.ParametriAttDichiarataVO;
import it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO;
import it.csi.solmr.dto.anag.attestazioni.TipoParametriAttestazioneVO;
import it.csi.solmr.dto.anag.consistenza.FascicoloNazionaleVO;
import it.csi.solmr.dto.anag.consistenza.TipoControlloVO;
import it.csi.solmr.dto.anag.consistenza.TipoMotivoDichiarazioneVO;
import it.csi.solmr.dto.anag.services.DelegaAnagrafeVO;
import it.csi.solmr.dto.anag.services.FabbricatoParticellaVO;
import it.csi.solmr.dto.anag.services.MandatoVO;
import it.csi.solmr.dto.anag.services.SianAnagTributariaGaaVO;
import it.csi.solmr.dto.anag.sian.SianAllevamentiVO;
import it.csi.solmr.dto.anag.sian.SianAnagTributariaVO;
import it.csi.solmr.dto.anag.sian.SianEsitiRicevutiRispostaVO;
import it.csi.solmr.dto.anag.sian.SianEsitoDomandeRispostaVO;
import it.csi.solmr.dto.anag.sian.SianFascicoloResponseVO;
import it.csi.solmr.dto.anag.sian.SianQuoteLatteAziendaVO;
import it.csi.solmr.dto.anag.sian.SianTerritorioVO;
import it.csi.solmr.dto.anag.sian.SianTitoliAggregatiVO;
import it.csi.solmr.dto.anag.sian.SianTitoliMovimentatiVO;
import it.csi.solmr.dto.anag.sian.SianTitoloMovimentazioneRispostaVO;
import it.csi.solmr.dto.anag.sian.SianTitoloRispostaVO;
import it.csi.solmr.dto.anag.sian.SianUtenteVO;
import it.csi.solmr.dto.anag.teramo.ElencoRegistroDiStallaVO;
import it.csi.solmr.dto.anag.terreni.AltroVitignoDichiaratoVO;
import it.csi.solmr.dto.anag.terreni.AltroVitignoVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneDichiarataVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneParticellaVO;
import it.csi.solmr.dto.anag.terreni.DichiarazioneSegnalazioneVO;
import it.csi.solmr.dto.anag.terreni.EsitoControlloUnarVO;
import it.csi.solmr.dto.anag.terreni.EventoParticellaVO;
import it.csi.solmr.dto.anag.terreni.FiltriParticellareRicercaVO;
import it.csi.solmr.dto.anag.terreni.FiltriUnitaArboreaRicercaVO;
import it.csi.solmr.dto.anag.terreni.SezioneVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaArboreaExcelVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.dto.anag.terreni.StoricoUnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.TipoCausaleModificaVO;
import it.csi.solmr.dto.anag.terreni.TipoCessazioneUnarVO;
import it.csi.solmr.dto.anag.terreni.TipoEventoVO;
import it.csi.solmr.dto.anag.terreni.TipoFormaAllevamentoVO;
import it.csi.solmr.dto.anag.terreni.TipoGenereIscrizioneVO;
import it.csi.solmr.dto.anag.terreni.TipoImpiantoVO;
import it.csi.solmr.dto.anag.terreni.TipoIrrigazioneVO;
import it.csi.solmr.dto.anag.terreni.TipoMacroUsoVO;
import it.csi.solmr.dto.anag.terreni.TipoMenzioneGeograficaVO;
import it.csi.solmr.dto.anag.terreni.TipoPiantaConsociataVO;
import it.csi.solmr.dto.anag.terreni.TipoPotenzialitaIrriguaVO;
import it.csi.solmr.dto.anag.terreni.TipoRotazioneColturaleVO;
import it.csi.solmr.dto.anag.terreni.TipoSettoreAbacoVO;
import it.csi.solmr.dto.anag.terreni.TipoTerrazzamentoVO;
import it.csi.solmr.dto.anag.terreni.TipoTipologiaVinoVO;
import it.csi.solmr.dto.anag.terreni.TipoUtilizzoVO;
import it.csi.solmr.dto.anag.terreni.TipoVarietaVO;
import it.csi.solmr.dto.anag.terreni.TipoVinoVO;
import it.csi.solmr.dto.anag.terreni.UnitaArboreaDichiarataVO;
import it.csi.solmr.dto.anag.terreni.UtilizzoConsociatoVO;
import it.csi.solmr.dto.anag.terreni.UtilizzoDichiaratoVO;
import it.csi.solmr.dto.anag.terreni.UtilizzoParticellaVO;
import it.csi.solmr.dto.comune.AmmCompetenzaVO;
import it.csi.solmr.dto.comune.IntermediarioVO;
import it.csi.solmr.dto.comune.TecnicoAmministrazioneVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.dto.profile.UtenteIride2VO;
import it.csi.solmr.dto.uma.DittaUMAVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.DataControlException;
import it.csi.solmr.exception.NotFoundException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.exception.services.ErrorTypes;
import it.csi.solmr.exception.services.InvalidParameterException;
import it.csi.solmr.exception.services.ServiceSystemException;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;
import it.csi.solmr.ws.infoc.Azienda;
import it.csi.solmr.ws.infoc.Sede;
import it.csi.util.performance.StopWatch;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.StatefulTimeout;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

@Stateless(name="comp/env/solmr/anag/AnagFacade",mappedName="comp/env/solmr/anag/AnagFacade")
@TransactionManagement(TransactionManagementType.CONTAINER)
@StatefulTimeout(unit = java.util.concurrent.TimeUnit.SECONDS, value = 295)
@TransactionAttribute(value=javax.ejb.TransactionAttributeType.REQUIRED)
public class AnagFacadeBean implements AnagFacadeLocal
{
  /**
   * 
   */
  private static final long serialVersionUID = 6831070391513108960L;

  SessionContext sessionContext;
  @EJB
  private transient AnagAziendaLocal anagAziendaLocal = null;
  @EJB 
  private transient AnagDenominazioniLocal anagDenominazioniLocal = null;
  @EJB
  private transient FascicoloLocal fascicoloLocal = null;
  @EJB
  private transient SoggettiLocal soggettiLocal = null;
  @EJB
  private transient TerreniLocal terreniLocal = null;
  @EJB
  private transient ContoCorrenteLocal contoCorrenteLocal = null;
  @EJB
  private transient AllevamentoAnagLocal allevamentoAnagLocal = null;
  @EJB
  private transient ManodoperaLocal manodoperaLocal = null;
  @EJB
  private transient ConsistenzaLocal consistenzaLocal = null;
  @EJB
  private transient StampeLocal stampeLocal = null;
  @EJB
  private transient AaepLocal aaepLocal = null;
  @EJB
  private transient NotificaLocal notificaLocal = null;
  @EJB
  private transient SianLocal sianLocal = null;
  @EJB
  private transient SmrCommLocal smrCommLocal = null;
  @EJB
  private transient VitiServLocal vitiServLocal = null;
  @EJB
  private transient DocumentoLocal documentoLocal = null;
  @EJB  
  private transient CommonLocal commonLocal = null;
  @EJB
  private transient GestioneTerreniLocal gestioneTerreniLocal = null;
  @EJB
  private transient UteLocal uteLocal = null;
  @EJB
  private transient FabbricatoLocal fabbricatoLocal = null;
  @EJB
  private transient AttestazioniLocal attestazioniLocal = null;
  @EJB
  private transient SitiLocal sitiLocal = null;
  @EJB
  private transient SigopServLocal sigopServLocal = null;
  @EJB
  private transient ReportDinLocal reportDinLocal = null;
  @EJB
  private transient DocumentoGaaLocal documentoGaaLocal = null;
  @EJB
  private transient AgriWellGaaLocal agriWellGaaLocal = null;
  @EJB
  private transient StampaGaaLocal stampaGaaLocal = null;

  
  

  /**
   * Usaato per restituire i dati relativi alle tabelle di decodifica di
   * anagrafe create con i nuovi standard
   * 
   * @param tableName
   *          nome della tabella da cui prendere i dati
   * @param filtro
   *          viene utilizzato quando si vuole filtrare la tabella per un id:
   *          usato quando si vogliono caricare combo che dipendano da altre
   *          combo
   * @param orderBy
   *          clausola di order by, se non viene inserita l'ordinamento viene
   *          fatto per descrizione
   * @return
   * @throws Exception
   */
  public CodeDescription[] getCodeDescriptionsNew(String tableName,
      String filtro, String valFiltro, String orderBy) throws Exception
  {
    return commonLocal.getCodeDescriptionsNew(tableName, filtro, valFiltro,
        orderBy);
  }

  public it.csi.solmr.dto.profile.CodeDescription getGruppoRuolo(String ruolo)
      throws Exception
  {
    return commonLocal.getGruppoRuolo(ruolo);
  }

  public Vector<CodeDescription> getTipiAreaA() throws NotFoundException,
      Exception
  {
    return anagDenominazioniLocal.getTipiAreaA();
  }

  public Vector<CodeDescription> getTipiAreaB() throws NotFoundException,
      Exception
  {
    return anagDenominazioniLocal.getTipiAreaB();
  }

  public Vector<CodeDescription> getTipiAreaC() throws NotFoundException,
      Exception
  {
    return anagDenominazioniLocal.getTipiAreaC();
  }

  public Vector<CodeDescription> getTipiAreaD() throws NotFoundException,
      Exception
  {
    return anagDenominazioniLocal.getTipiAreaD();
  }

  public Vector<CodeDescription> getTipiZonaAltimetrica()
      throws NotFoundException, Exception
  {
    return anagDenominazioniLocal.getTipiZonaAltimetrica();
  }

  public Vector<CodeDescription> getTipiCasoParticolare()
      throws Exception, NotFoundException
  {
    return anagDenominazioniLocal.getTipiCasoParticolare();
  }

  // Anagrafica azienda inizio
  public AnagAziendaVO getAziendaByIdAzienda(Long idAzienda)
      throws NotFoundException, Exception, SolmrException
  {
    AnagAziendaVO anagAziendaVO = anagAziendaLocal
        .getAziendaByIdAzienda(idAzienda);
    if (anagAziendaVO != null && anagAziendaVO.getCUAA() != null)
    {
      // associo il cuaa di anagrafe tributaria all'azienda...è utilizzato per
      // scoprire se il cuaa presente su
      // anagrafe tributaria è uguale a quello presente in anagrafe
      SianAnagTributariaVO sianAnagTributariaVO = sianLocal
          .selectDatiAziendaTributaria(anagAziendaVO.getCUAA());
      if (sianAnagTributariaVO != null)
        anagAziendaVO.setCUAAAnagrafeTributaria(sianAnagTributariaVO.getCodiceFiscale());
      // Associo i cuaa collegati all'azienda
      anagAziendaVO.setCUAACollegati(anagAziendaLocal
          .getCUAACollegati(anagAziendaVO.getCUAA()));
    }
    return anagAziendaVO;
  }
  

  public Vector<AnagAziendaVO> getAssociazioniCollegateByIdAzienda(
      Long idAzienda, Date dataFineValidita) throws Exception,
      SolmrException
  {
    return anagAziendaLocal.getAssociazioniCollegateByIdAzienda(idAzienda,
        dataFineValidita);
  }

  // Richiamo una funzione PL-SQL che mi restituisce l'elenco dei CUAA collegati
  public String[] getCUAACollegati(String cuaa, SianUtenteVO sianUtenteVO) throws Exception
  {
    try
    {
      // mi carico i dati sulle tabelle
      if (cuaa == null)
        return null;
      sianLocal.serviceSianAggiornaDatiTributaria(cuaa, sianUtenteVO);
      
      return anagAziendaLocal.getCUAACollegati(cuaa);
    }
    catch (Exception e)
    {
      throw new Exception(e.getMessage());
    }
  }

  public Vector<Long> getIdAnagAziendeCollegatebyCUAA(String cuaa)
      throws Exception
  {
    return anagAziendaLocal.getIdAnagAziendeCollegatebyCUAA(cuaa);
  }

  public SianAnagTributariaVO selectDatiAziendaTributaria(String cuaa)
      throws Exception
  {
    return sianLocal.selectDatiAziendaTributaria(cuaa);
  }

  public AnagAziendaVO getAziendaById(Long idAnagAzienda)
      throws NotFoundException, Exception, SolmrException
  {
    return anagAziendaLocal.getAziendaById(idAnagAzienda);
  }

  public Vector<AnagAziendaVO> getAziendaCUAA(String CUAA)
      throws NotFoundException, Exception, SolmrException
  {
    return anagAziendaLocal.getAziendaCUAA(CUAA);
  }

  public Vector<AnagAziendaVO> getAziendaByCriterioCessataAndProvvisoria(
      String CUAA) throws Exception, SolmrException
  {
    return anagAziendaLocal.getAziendaByCriterioCessataAndProvvisoria(CUAA);
  }

  public AnagAziendaVO getAziendaCUAA(String CUAA, java.util.Date data)
      throws NotFoundException, Exception, SolmrException
  {
    return anagAziendaLocal.getAziendaCUAA(CUAA, data);
  }

  public AnagAziendaVO getAziendaPartitaIVA(String partitaIVA,
      java.util.Date data) throws NotFoundException, Exception,
      SolmrException
  {
    return anagAziendaLocal.getAziendaPartitaIVA(partitaIVA, data);
  }

  public Vector<Long> getListIdAziende(AnagAziendaVO aVO, java.util.Date data,
      boolean attivitaBool) throws NotFoundException, Exception,
      SolmrException
  {
    return anagAziendaLocal.getListIdAziende(aVO, data, attivitaBool);
  }

  public Vector<Long> getListIdAziendeFlagProvvisorio(AnagAziendaVO aaVO,
      Date dataSituazioneAl, boolean attivitaBool, boolean provvisorio)
      throws NotFoundException, Exception, SolmrException
  {

    return anagAziendaLocal.getListIdAziendeFlagProvvisorio(aaVO,
        dataSituazioneAl, attivitaBool, provvisorio);

  }

  public Vector<Long> getListOfIdAzienda(AnagAziendaVO aVO,
      java.util.Date data, boolean attivitaBool) throws NotFoundException,
      Exception, SolmrException
  {
    return anagAziendaLocal.getListOfIdAzienda(aVO, data, attivitaBool);
  }

  public Vector<AnagAziendaVO> getListAziendeByIdRange(Vector<Long> collAzienda)
      throws NotFoundException, Exception, SolmrException
  {
    return anagAziendaLocal.getListAziendeByIdRange(collAzienda);
  }

  public Vector<AnagAziendaVO> getAziendeByIdAziendaRange(
      Vector<Long> collAzienda) throws NotFoundException, Exception,
      SolmrException
  {
    return anagAziendaLocal.getAziendeByIdAziendaRange(collAzienda);
  }

  public Vector<AnagAziendaVO> getListAziendeByIdRangeFromIdAzienda(
      Vector<Long> idAzienda) throws NotFoundException, Exception,
      SolmrException
  {
    return anagAziendaLocal.getListAziendeByIdRangeFromIdAzienda(idAzienda);
  }

  public void storicizzaAziendeCollegateBlocco(RuoloUtenza ruoloUtenza,
      Vector<AziendaCollegataVO> vAnagAziendaCollegateVO)
      throws Exception, SolmrException
  {
    anagAziendaLocal.storicizzaAziendeCollegateBlocco(ruoloUtenza,
        vAnagAziendaCollegateVO);
  }

  public void eliminaAziendeCollegateBlocco(long idUtenteAggiornamento,
      Long idAziendaFather, Vector<Long> vIdAziendaVO) throws Exception,
      SolmrException
  {
    anagAziendaLocal.eliminaAziendeCollegateBlocco(idUtenteAggiornamento, idAziendaFather,
        vIdAziendaVO);
  }

  public PersonaFisicaVO getTitolareORappresentanteLegaleAzienda(
      Long idAzienda, java.util.Date data) throws NotFoundException,
      Exception, SolmrException
  {
    return anagAziendaLocal.getTitolareORappresentanteLegaleAzienda(idAzienda,
        data);
  }

  public Vector<AnagAziendaVO> getAziendaByIntermediarioAndCuaa(
      Long intermediario, String cuaa) throws NotFoundException,
      Exception, SolmrException
  {
    return anagAziendaLocal.getAziendaByIntermediarioAndCuaa(intermediario,
        cuaa);
  }

  public Vector<AnagAziendaVO> getAziendeByListOfId(Vector<Long> vIdAzienda)
      throws NotFoundException, Exception, SolmrException
  {
    return anagAziendaLocal.getAziendeByListOfId(vIdAzienda);
  }

  public Vector<ProcedimentoAziendaVO> getElencoProcedimentiByIdAzienda(
      Long idAzienda, Long annoProc, Long idProcedimento,
      Long idAziendaSelezionata) throws Exception, NotFoundException,
      SolmrException
  {
    return anagAziendaLocal.getElencoProcedimentiByIdAzienda(idAzienda,
        annoProc, idProcedimento, idAziendaSelezionata);
  }

  public void storicizzaDelegaBlocco(RuoloUtenza ruoloUtenza,
      Vector<AnagAziendaVO> vAnagAziendaVO, String oldIntermediario,
      String newIntermediario) throws Exception, SolmrException
  {
    if (!ruoloUtenza.isUtenteIntermediario()
        && !ruoloUtenza.isUtenteOPRGestore())
    {
      AmmCompetenzaVO ammCompetenzaVO = smrCommLocal
          .serviceFindAmmCompetenzaByCodiceAmm(ruoloUtenza.getCodiceEnte());
      if (ammCompetenzaVO == null
          || ammCompetenzaVO.getSiglaAmministrazione() == null
          || ammCompetenzaVO.getSiglaAmministrazione().equalsIgnoreCase(""))
      {
        throw new SolmrException(
            ErrorTypes.STR_SERVICE_SIGLA_AMMINISTRAZIONE_EXCEPTION,
            ErrorTypes.CODE_SERVICE_SIGLA_AMMINISTRAZIONE_EXCEPTION);
      }
      else
      {
        ruoloUtenza.setSiglaAmministrazione(ammCompetenzaVO
            .getSiglaAmministrazione());
      }
    }

    anagAziendaLocal.storicizzaDelegaBlocco(ruoloUtenza, vAnagAziendaVO,
        oldIntermediario, newIntermediario);
  }

  public Vector<AziendaCollegataVO> getAziendeCollegateByIdAzienda(
      Long idAzienda, boolean flagStorico) throws Exception,
      SolmrException
  {
    return anagAziendaLocal.getAziendeCollegateByIdAzienda(idAzienda,
        flagStorico);
  }

  public Vector<AnagAziendaVO> getEntiAppartenenzaByIdAzienda(Long idAzienda,
      boolean flagStorico) throws Exception, SolmrException
  {
    return anagAziendaLocal.getEntiAppartenenzaByIdAzienda(idAzienda,
        flagStorico);
  }

  public AnagAziendaVO findAziendaByIdAnagAzienda(Long anagAziendaPK)
      throws Exception, SolmrException
  {
    return anagAziendaLocal.findAziendaByIdAnagAzienda(anagAziendaPK);
  }

  public Vector<AziendaCollegataVO> getAziendeCollegateByRangeIdAziendaCollegata(
      Vector<Long> vIdAziendaCollegata) throws Exception, SolmrException
  {
    return anagAziendaLocal
        .getAziendeCollegateByRangeIdAziendaCollegata(vIdAziendaCollegata);
  }

  public Vector<AnagAziendaVO> getIdAziendaCollegataAncestor(Long idAzienda)
      throws Exception, SolmrException
  {
    return anagAziendaLocal.getIdAziendaCollegataAncestor(idAzienda);
  }

  public Vector<AnagAziendaVO> getIdAziendaCollegataDescendant(Long idAzienda)
      throws Exception, SolmrException
  {
    return anagAziendaLocal.getIdAziendaCollegataDescendant(idAzienda);
  }

  public boolean controlloAziendeAssociate(String CUAApadre,
      Long idAziendaCollegata) throws Exception
  {
    return anagAziendaLocal.controlloAziendeAssociate(CUAApadre,
        idAziendaCollegata);
  }

  public Vector<AnagAziendaVO> getAziendeCollegateByRangeIdAzienda(
      Vector<Long> vIdAzienda, Long idAziendaPadre) throws Exception,
      SolmrException
  {
    return anagAziendaLocal.getAziendeCollegateByRangeIdAzienda(vIdAzienda,
        idAziendaPadre);
  }

  // Anagrafica azienda fine

  public Vector<UteVO> getUTE(Long idAzienda, Boolean storico)
      throws SolmrException, Exception
  {
    return fascicoloLocal.getUTE(idAzienda, storico);
  }

  public UteVO getUteById(Long idUte) throws Exception
  {
    return fascicoloLocal.getUteById(idUte);
  }

  public Vector<CodeDescription> getTipiAttivitaOTE() throws Exception,
      NotFoundException
  {
    return anagDenominazioniLocal.getTipiAttivitaOTE();
  }

  public Vector<CodeDescription> getTipiAttivitaATECO(String code,
      String description) throws Exception
  {
    return anagDenominazioniLocal.getTipiAttivitaATECO(code, description);
  }

  public Vector<CodeDescription> getTipiAttivitaOTE(String code,
      String description) throws Exception, NotFoundException
  {
    return anagDenominazioniLocal.getTipiAttivitaOTE(code, description);
  }

  public Vector<CodeDescription> getTipiAzienda() throws NotFoundException,
      Exception
  {
    return anagDenominazioniLocal.getTipiAzienda();
  }

  public Vector<CodeDescription> getTipiFabbricato() throws NotFoundException,
      Exception
  {
    return anagDenominazioniLocal.getTipiFabbricato();
  }

  public Vector<CodeDescription> getTipiFormaGiuridica(Long idTipologiaAzienda)
      throws NotFoundException, Exception
  {
    return anagDenominazioniLocal.getTipiFormaGiuridica(idTipologiaAzienda);
  }
  
  public Vector<CodeDescription> getCodeDescriptionsFormaGiuridica() 
      throws Exception, NotFoundException
  {
    return anagDenominazioniLocal.getCodeDescriptionsFormaGiuridica();
  }

  public TipoTipologiaAziendaVO getTipologiaAzienda(Long idTipologiaAzienda)
      throws Exception, NotFoundException
  {
    return anagDenominazioniLocal.getTipologiaAzienda(idTipologiaAzienda);
  }

  public Vector<CodeDescription> getTipiMotivoDichiarazione()
      throws Exception
  {
    return anagDenominazioniLocal.getTipiMotivoDichiarazione();
  }

  public String insediamentoAtomico(AnagAziendaVO modAnagAziendaVO,
      PersonaFisicaVO modPersonaVO, HttpServletRequest request,
      AnagAziendaVO anagAziendaVO, SianUtenteVO sianUtenteVO, RuoloUtenza ruoloUtenza)
      throws Exception, SolmrException
  {
    try
    {
      String parametroAAEP = getValoreFromParametroByIdCode((String) SolmrConstants
          .get("ID_PARAMETRO_AAEP"));
      String parametroSIAN = getValoreFromParametroByIdCode((String) SolmrConstants
          .get("ID_PARAMETRO_TRIB"));

      // Se il parametro del SIAN è valorizzato a "S" significa
      // che devo effettuare i controlli e gli aggiornamenti
      // relativi al SIAN
      if (parametroSIAN.equalsIgnoreCase(SolmrConstants.FLAG_S))
      {
        // Aggiorno la tabella DB_AZIENDA_TRIBUTARIA in relazione
        // ai valori restituitimi dal SIAN
        try
        {
          String elencoCuaa[] = new String[1];
          elencoCuaa[0] = modAnagAziendaVO.getCUAA();
          sianLocal.sianAggiornaDatiTributaria(elencoCuaa, sianUtenteVO);
        }
        catch (Exception se)
        {
        }
      }

      anagAziendaLocal.insediamentoAtomico(modAnagAziendaVO, modPersonaVO,
          request, anagAziendaVO, ruoloUtenza.getIdUtente()); 

      String esito = consistenzaLocal
          .controlliInsediamentoPLSQL(modAnagAziendaVO.getIdAzienda(), ruoloUtenza.getIdUtente());

      Vector<ErrAnomaliaDicConsistenzaVO> anomalie = consistenzaLocal
          .getErroriAnomalieDichiarazioneConsistenza(modAnagAziendaVO
              .getIdAzienda(), (Long) SolmrConstants.get("FASE_INSEDIAMENTO"),
              null);

      request.getSession().setAttribute("anomalieDichiarazione", anomalie);

      // Se l'esito del plsql mi dice che non posso andare aventi devo
      // fare il rollback dei dati inseriti
      if (((String) SolmrConstants.get("P_ESITO_CONTR")).equals(esito))
        sessionContext.setRollbackOnly();
      return esito;
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(e.getMessage());
    }
  }

  public Vector<TipoTipologiaAziendaVO> getTipiTipologiaAzienda(
      Boolean flagControlliUnivocita, Boolean flagAziendaProvvisoria)
      throws NotFoundException, Exception
  {
    return anagDenominazioniLocal.getTipiTipologiaAzienda(
        flagControlliUnivocita, flagAziendaProvvisoria);
  }

  public Vector<CodeDescription> getTipiFormaGiuridica()
      throws NotFoundException, Exception
  {
    return anagDenominazioniLocal.getTipiFormaGiuridica();
  }

  public Vector<CodeDescription> getTipiIntermediario()
      throws NotFoundException, Exception
  {
    return anagDenominazioniLocal.getTipiIntermediario();
  }

  public Vector<CodeDescription> getTipiIntermediarioUmaProv()
      throws NotFoundException, Exception
  {
    return anagDenominazioniLocal.getTipiIntermediarioUmaProv();
  }

  public Vector<CodeDescription> getTipiUtilizzo() throws NotFoundException,
      Exception
  {
    return anagDenominazioniLocal.getTipiUtilizzo();
  }

  public String getProcedimento(Integer code) throws NotFoundException,
      Exception
  {
    return anagDenominazioniLocal.getProcedimento(code);
  }

  public CodeDescription getTipoAttivitaATECO(Integer code)
      throws Exception
  {
    return anagDenominazioniLocal.getTipoAttivitaATECO(code);
  }

  public CodeDescription getTipoAttivitaOTE(Integer code)
      throws Exception
  {
    return anagDenominazioniLocal.getTipoAttivitaOTE(code);
  }

  public String getTipoAzienda(Integer code) throws NotFoundException,
      Exception
  {
    return anagDenominazioniLocal.getTipoAzienda(code);
  }

  public String getTipoCasoParticolare(Integer code) throws NotFoundException,
      Exception
  {
    return anagDenominazioniLocal.getTipoCasoParticolare(code);
  }

  public String getTipoFabbricato(Integer code) throws NotFoundException,
      Exception
  {
    return anagDenominazioniLocal.getTipoFabbricato(code);
  }

  public String getTipoFormaGiuridica(Integer code) throws NotFoundException,
      Exception
  {
    return anagDenominazioniLocal.getTipoFormaGiuridica(code);
  }

  public String getTipoIntermediario(Integer code) throws NotFoundException,
      Exception
  {
    return anagDenominazioniLocal.getTipoIntermediario(code);
  }

  public String getTipoUtilizzo(Integer code) throws NotFoundException,
      Exception
  {
    return anagDenominazioniLocal.getTipoUtilizzo(code);
  }

  public String getTipoZonaAltimetrica(Integer code) throws NotFoundException,
      Exception
  {
    return anagDenominazioniLocal.getTipoZonaAltimetrica(code);
  }

  public Vector<ProvinciaVO> getProvinceByRegione(String idRegione)
      throws Exception
  {
    return anagDenominazioniLocal.getProvinceByRegione(idRegione);
  }
  
  public Vector<ProvinciaVO> getProvince()
      throws Exception
  {
    return anagDenominazioniLocal.getProvince();
  }

  public Vector<ComuneVO> getComuniLikeByRegione(String idRegione, String like)
      throws NotFoundException, SolmrException, Exception
  {
    return anagDenominazioniLocal.getComuniLikeByRegione(idRegione, like);
  }

  public boolean isDataInizioValida(long idAzienda, Date dataInizio)
      throws SolmrException, Exception
  {
    return fascicoloLocal.isDataInizioValida(idAzienda, dataInizio);
  }

  public Vector<CodeDescription> getAttivitaLikeCodAndDescOTE(String codice,
      String descrizione) throws NotFoundException, SolmrException
  {
    try
    {
      return anagDenominazioniLocal.getTipiAttivitaOTE(codice, descrizione);
    }
    catch (Exception ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }

  public Vector<ComuneVO> getComuniLikeProvAndCom(String provincia,
      String comune) throws NotFoundException, SolmrException
  {
    try
    {
      return anagDenominazioniLocal.getComuniLikeByProvAndCom(provincia,
          comune);
    }
    catch (Exception ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }

  public Vector<ComuneVO> getComuniByDescCom(String comune)
      throws NotFoundException, SolmrException
  {
    try
    {
      return anagDenominazioniLocal.getComuniByDescCom(comune);
    }
    catch (Exception ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }

  public Vector<ComuneVO> getComuniNonEstintiLikeProvAndCom(String provincia,
      String comune, String flagEstero) throws NotFoundException,
      SolmrException
  {
    try
    {
      return anagDenominazioniLocal.getComuniNonEstintiLikeByProvAndCom(
          provincia, comune, flagEstero);
    }
    catch (Exception ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce l'elenco degli stati esteri in relazione ai
   * parametri
   * 
   * @param statoEstero
   * @param estinto
   * @param flagCatastoAttivo
   * @return java.util.Vector
   * @throws DataAccessException
   */
  public Vector<ComuneVO> ricercaStatoEstero(String statoEstero,
      String estinto, String flagCatastoAttivo) throws Exception
  {
    return anagDenominazioniLocal.ricercaStatoEstero(statoEstero, estinto,
        flagCatastoAttivo);
  }

  /**
   * Metodo per recuperare l'elenco dei dati dalla tabella di decodifica
   * DB_TIPO_TIPOLOGIA_DOCUMENTO
   * 
   * @return CodeDescription
   * @throws Exception
   */
  public Vector<CodeDescription> getTipiTipologiaDocumento()
      throws Exception
  {
    return anagDenominazioniLocal.getTipiTipologiaDocumento();
  }

  /**
   * Metodo per recuperare l'elenco dei dati dalla tabella di decodifica
   * DB_TIPO_TIPOLOGIA_DOCUMENTO aventi ID_TIPOLOGIA_DOCUMENTO <> 4 se l’azienda
   * non è cessatao di tutti i record di DB_TIPO_TIPOLOGIA_DOCUMENTO se
   * l’azienda è cessata
   * 
   * @return CodeDescription
   * @throws Exception
   */
  public Vector<CodeDescription> getTipiTipologiaDocumento(boolean cessata)
      throws Exception
  {
    return anagDenominazioniLocal.getTipiTipologiaDocumento(cessata);
  }

  // Metodo per modificare la sede legale della sede legale
  public void updateSedeLegale(AnagAziendaVO anagAziendaVO,
      long idUtenteAggiornamento) throws SQLException, NotFoundException,
      DataAccessException, Exception, DataControlException
  {
    anagAziendaLocal.updateSedeLegale(anagAziendaVO, idUtenteAggiornamento);
  }

  // Metodo per effettuare la storicizzazione della sede legale
  public void storicizzaSedeLegale(AnagAziendaVO anagAziendaVO) throws SQLException, NotFoundException,
      DataAccessException, SolmrException, Exception,
      DataControlException
  {
    anagAziendaLocal.storicizzaSedeLegale(anagAziendaVO);
  }


  // INIZIO modifica cessa azienda con controlli se procedimenti
  // attivi!!!!!!!!!!!!!!!!!!
  public void checkDataCessazione(Long anagAziendaPK, String dataCessazione)
      throws SolmrException, Exception
  {
    anagAziendaLocal.checkDataCessazione(anagAziendaPK, dataCessazione);
  }

  /**
   * Metodo che mi permette di cessare l'azienda in tutte le sue componenti
   * 
   */
  public void cessaAzienda(AnagAziendaVO anagVO, Date dataCess, String causale,
      long idUtenteAggiornamento) throws Exception, SolmrException
  {
    try
    {
      anagAziendaLocal.cessaAzienda(anagVO, dataCess, causale, idUtenteAggiornamento);
      // Recupero l'elenco delle conduzioni attive legate all'azienda
      ConduzioneParticellaVO[] elencoConduzioni = gestioneTerreniLocal
          .getListConduzioneParticellaByIdAzienda(anagVO.getIdAzienda(), true,
              null);
      // Se ve ne sono
      Long[] elencoId = null;
      if (elencoConduzioni != null && elencoConduzioni.length > 0)
      {
        elencoId = new Long[elencoConduzioni.length];
        for (int i = 0; i < elencoConduzioni.length; i++)
        {
          ConduzioneParticellaVO conduzioneParticellaVO = (ConduzioneParticellaVO) elencoConduzioni[i];
          elencoId[i] = conduzioneParticellaVO.getIdConduzioneParticella();
        }
        // Cesso tutta la sezione territoriale dell'azienda(conduzione, UV,
        // fabbricati, documenti)
        gestioneTerreniLocal.cessaParticelle(elencoId, idUtenteAggiornamento, 
            anagVO.getIdAzienda(), dataCess, null);
      }
    }
    catch (Exception re)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(re.getMessage());
    }
  }

  // FINE modifica cessa azienda con controlli se procedimenti
  // attivi!!!!!!!!!!!!!!!!!!

  // Metodo per recuperare il codiceIstat del comune a partire dal comune
  public String ricercaCodiceComune(String descrizioneComune,
      String siglaProvincia) throws DataAccessException, NotFoundException,
      DataControlException, Exception
  {
    return anagDenominazioniLocal.ricercaCodiceComune(descrizioneComune,
        siglaProvincia);
  }

  public void deleteUTE(Long idUte) throws SolmrException, DataAccessException,
      Exception
  {
    fascicoloLocal.deleteUTE(idUte);
  }

  // Metodo per recuperare l'azienda attiva
  public AnagAziendaVO findAziendaAttiva(Long idAzienda)
      throws DataAccessException, NotFoundException, Exception
  {
    return anagAziendaLocal.findAziendaAttiva(idAzienda);
  }

  public AnagAziendaVO getAziendaCUAAandCodFiscale(String cuaa,
      String partitaIVA) throws DataAccessException, Exception,
      SolmrException
  {
    return anagAziendaLocal.getAziendaCUAAandCodFiscale(cuaa, partitaIVA);
  }

  public AnagAziendaVO getAltraAziendaFromPartitaIVA(String partitaIVA,
      Long idAzienda) throws DataAccessException, Exception,
      SolmrException
  {
    return anagAziendaLocal.getAltraAziendaFromPartitaIVA(partitaIVA,
        idAzienda);
  }

  public void checkCUAAandCodFiscale(String cuaa, String partitaIVA)
      throws DataAccessException, Exception, SolmrException
  {
    anagAziendaLocal.checkCUAAandCodFiscale(cuaa, partitaIVA);
  }

  public void checkPartitaIVA(String partitaIVA, Long idAzienda)
      throws DataAccessException, Exception, SolmrException
  {
    anagAziendaLocal.checkPartitaIVA(partitaIVA, idAzienda);
  }

  public PersonaFisicaVO getPersonaFisica(String cuaa)
      throws DataAccessException, Exception, SolmrException
  {
    return anagAziendaLocal.getPersonaFisica(cuaa);
  }

  public Long insertAzienda(AnagAziendaVO aaVO, PersonaFisicaVO pfVO,
      UteVO ute, long idUtenteAggiornamento) throws DataAccessException,
      Exception, SolmrException
  {
    SianFascicoloResponseVO sianFascicoloResponseVO = null;
    // Richiamare il ws Trova Fascicolo passando come parametro
    // di input il CUAA dell’azienda. Nel caso in cui il ws non termini a
    // buon fine per qualcunque motivo proseguire con l’elaborazione.
    // Il servizio restituisce in output l’oggetto ISWSRespAnagFascicolo.
    try
    {
      sianFascicoloResponseVO = sianLocal.trovaFascicolo(aaVO.getCUAA().toUpperCase());
    }
    catch (Exception e)
    {
    }
    return anagAziendaLocal.insertAzienda(aaVO, pfVO, ute, idUtenteAggiornamento, sianFascicoloResponseVO);
  }

  public void countUteByAziendaAndComune(Long idAzienda, String comune)
      throws DataAccessException, Exception, SolmrException
  {
    anagAziendaLocal.countUteByAziendaAndComune(idAzienda, comune);
  }

  public void insertUte(UteVO uVO, long idUtenteAggiornamento)
      throws DataAccessException, Exception, SolmrException
  {
    anagAziendaLocal.insertUte(uVO, idUtenteAggiornamento);
  }

  public void cambiaRappresentanteLegale(Long aziendaPK,
      PersonaFisicaVO personaVO, long idUtenteAggiornamento)
      throws Exception, SolmrException
  {
    anagAziendaLocal.cambiaRappresentanteLegale(aziendaPK, personaVO,
        idUtenteAggiornamento);
  }

  public ComuneVO getComuneByCUAA(String cuaa) throws Exception,
      SolmrException
  {
    return anagAziendaLocal.getComuneByCUAA(cuaa);
  }

  public ComuneVO getComuneByISTAT(String istat) throws Exception,
      SolmrException
  {
    return anagAziendaLocal.getComuneByISTAT(istat);
  }

  // Metodo per recuperare il valore del flagCCIAA relativo ad una forma
  // giuridica
  public String getFormaGiuridicaFlagCCIAA(Long idFormaGiuridica)
      throws Exception, SolmrException
  {
    return anagAziendaLocal.getFormaGiuridicaFlagCCIAA(idFormaGiuridica);
  }

  // Metodo per recuperare il comune a partire dal codice fiscale
  public String getComuneFromCF(String codiceFiscale) throws Exception,
      SolmrException
  {
    return anagAziendaLocal.getComuneFromCF(codiceFiscale);
  }

  // Metodo per recuperare l'id dell'attività OTE partendo dal codice e dalla
  // descrizione
  public Long ricercaIdAttivitaOTE(String codice, String descrizione)
      throws Exception, SolmrException
  {
    return anagDenominazioniLocal.ricercaIdAttivitaOTE(codice, descrizione);
  }

  // Metodo per recuperare l'id dell'attività OTE partendo dal codice e dalla
  // descrizione
  public Long ricercaIdAttivitaOTE(String codice, String descrizione,
      boolean forPopup) throws Exception, SolmrException
  {
    return anagDenominazioniLocal.ricercaIdAttivitaOTE(codice, descrizione,
        forPopup);
  }

  // Metodo per recuperare l'id dell'attività ATECO partendo dal codice e dalla
  // descrizione
  public Long ricercaIdAttivitaATECO(String codice, String descrizione)
      throws Exception, SolmrException
  {
    return anagDenominazioniLocal.ricercaIdAttivitaATECO(codice, descrizione);
  }

  // Metodo per recuperare l'id dell'attività ATECO partendo dal codice e dalla
  // descrizione
  public Long ricercaIdAttivitaATECO(String codice, String descrizione,
      boolean forPopup) throws Exception, SolmrException
  {
    return anagDenominazioniLocal.ricercaIdAttivitaATECO(codice, descrizione,
        forPopup);
  }

  public void updateUTE(UteVO uteVO) throws SolmrException,
      DataAccessException, Exception
  {
    fascicoloLocal.updateUTE(uteVO);
  }

  // Metodo per recuerare il codice fiscale del comune
  public String ricercaCodiceFiscaleComune(String descrizioneComune,
      String siglaProvincia) throws Exception, DataControlException
  {
    return anagDenominazioniLocal.ricercaCodiceFiscaleComune(
        descrizioneComune, siglaProvincia);
  }

  // Metodo per recuperare una descrizione a partire dal codice
  public String getDescriptionFromCode(String tableName, Integer code)
      throws DataAccessException, Exception
  {
    try
    {
      return anagDenominazioniLocal.getDescriptionFromCode(tableName, code);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  public void updateAzienda(AnagAziendaVO anagVO)
      throws Exception, SolmrException
  {
    anagAziendaLocal.updateAzienda(anagVO);
  }

  public void storicizzaAzienda(AnagAziendaVO anagVO, long idUtenteAggiornamento)
      throws SolmrException, Exception
  {
    anagAziendaLocal.storicizzaAzienda(anagVO, idUtenteAggiornamento);
  }

  public Vector<CodeDescription> getTipiFormaGiuridicaNonIndividuale()
      throws NotFoundException, Exception
  {
    return anagDenominazioniLocal.getTipiFormaGiuridicaNonIndividuale();
  }

  public Vector<CodeDescription> getTipiRuoloNonTitolare()
      throws NotFoundException, Exception
  {
    return anagDenominazioniLocal.getTipiRuoloNonTitolare();
  }

  public void utenteConDelega(UtenteAbilitazioni utenteAbilitazioni, Long idAzienda)
      throws Exception, SolmrException
  {
    anagAziendaLocal.utenteConDelega(utenteAbilitazioni, idAzienda);
  }

  public void updateRappLegale(PersonaFisicaVO pfVO, long idUtenteAggiornamento)
      throws Exception, SolmrException
  {
    anagAziendaLocal.updateRappLegale(pfVO, idUtenteAggiornamento);
  }

  // Inizio gestione soggetti collegati
  public Vector<PersonaFisicaVO> getSoggetti(Long idAzienda, Date data)
      throws Exception, SolmrException
  {
    return soggettiLocal.getSoggetti(idAzienda, data);
  }

  public Vector<PersonaFisicaVO> getSoggetti(Long idAzienda, Boolean storico)
      throws Exception, SolmrException
  {
    return soggettiLocal.getSoggetti(idAzienda, storico);
  }

  public void checkForDeleteSoggetto(Long idContitolare)
      throws Exception, SolmrException
  {
    soggettiLocal.checkForDeleteSoggetto(idContitolare);
  }

  public void deleteContitolare(Long idContitolare) throws Exception,
      SolmrException
  {
    soggettiLocal.deleteContitolare(idContitolare);
  }

  public PersonaFisicaVO getDettaglioSoggetti(Long idSoggetto, Long idAzienda)
      throws Exception
  {
    return soggettiLocal.getDettaglioSoggetti(idSoggetto, idAzienda);
  }

  public void inserisciSoggetto(PersonaFisicaVO pfVO, long idUtenteAggiornamento)
      throws Exception, SolmrException
  {
    soggettiLocal.insertSoggetto(pfVO, idUtenteAggiornamento);
  }

  public void updateSoggetto(PersonaFisicaVO pfVO, long idUtenteAggiornamento)
      throws Exception, SolmrException
  {
    soggettiLocal.updateSoggetto(pfVO, idUtenteAggiornamento);
  }

  // Fine gestione soggetti collegati

  // Metodo per recuperare il flag partita iva relativo ad una specifica forma
  // giuridica
  public String getFlagPartitaIva(Long idTipoFormaGiuridica)
      throws Exception, SolmrException
  {
    return anagAziendaLocal.getFlagPartitaIva(idTipoFormaGiuridica);
  }
  
  public String getObbligoGfFromFormaGiuridica(Long idTipoFormaGiuridica)
      throws Exception, SolmrException
  {
    return anagAziendaLocal.getObbligoGfFromFormaGiuridica(idTipoFormaGiuridica);
  }

  public Long getIdTipologiaAziendaByFormaGiuridica(Long idTipoFormaGiuridica,
      Boolean flagAziendaProvvisoria) throws Exception, SolmrException
  {
    return anagAziendaLocal.getIdTipologiaAziendaByFormaGiuridica(
        idTipoFormaGiuridica, flagAziendaProvvisoria);
  }

  // Metodo per recupare l'id della forma giuridica dala la sua descrizione
  public Long getIdTipoFormaGiuridica(String idTipoFormaGiuridicaAAEP)
      throws Exception, SolmrException
  {
    return anagAziendaLocal.getIdTipoFormaGiuridica(idTipoFormaGiuridicaAAEP);
  }

  // Metodo per recupare la descrizione di una nostra forma giuridica data il
  // codice
  // della forma giuridica di AAEP
  public String getDescTipoFormaGiuridica(String idTipoFormaGiuridicaAAEP)
      throws Exception, SolmrException
  {
    return anagAziendaLocal
        .getDescTipoFormaGiuridica(idTipoFormaGiuridicaAAEP);
  }

  // Metodo per verificare la correttezza della provincia REA
  public boolean isProvinciaReaValida(String siglaProvincia)
      throws Exception, SolmrException
  {
    return anagAziendaLocal.isProvinciaReaValida(siglaProvincia);
  }

  // Metodo per reperire il rappresentante legale di una società a partire
  // dall'id_anagrafica_azienda
  public PersonaFisicaVO getRappresentanteLegaleFromIdAnagAzienda(
      Long idAnagAzienda) throws Exception, SolmrException
  {
    return anagAziendaLocal
        .getRappresentanteLegaleFromIdAnagAzienda(idAnagAzienda);
  }

  public PersonaFisicaVO getRappresentanteLegaleFromIdAnagAziendaAndDichCons(
      long idAnagAzienda, Date dataDichiarazione) throws Exception,
      SolmrException
  {
    return anagAziendaLocal
        .getRappresentanteLegaleFromIdAnagAziendaAndDichCons(idAnagAzienda,
            dataDichiarazione);
  }
  
  public Vector<PersonaFisicaVO> getVAltriSoggettiFromIdAnagAziendaAndDichCons(
      long idAnagAzienda, Date dataDichiarazione) throws Exception,
      SolmrException
  {
    return anagAziendaLocal
        .getVAltriSoggettiFromIdAnagAziendaAndDichCons(idAnagAzienda,
            dataDichiarazione);
  }

  public UtenteIrideVO getUtenteIrideById(Long idUtente)
      throws Exception, SolmrException
  {
    return anagAziendaLocal.getUtenteIrideById(idUtente);
  }

  // Metodo per effettuare il passaggio da società a ditta individuale con nuovo
  // titolare
  public Long updateTitolareAzienda(AnagAziendaVO anagAziendaVO,
      PersonaFisicaVO personaTitolareOldVO,
      PersonaFisicaVO personaTitolareNewVO, long idUtenteAggiornamento)
      throws Exception, SolmrException
  {
    return anagAziendaLocal.updateTitolareAzienda(anagAziendaVO,
        personaTitolareOldVO, personaTitolareNewVO, idUtenteAggiornamento);
  }

  // Gestione Terreni

  public Vector<Long> getAnniRilevamento() throws Exception,
      SolmrException
  {
    return terreniLocal.getAnniRilevamento();
  }

  public Vector<CodeDescription> getUnitaProduttive(Long idAzienda)
      throws Exception, SolmrException
  {
    return terreniLocal.getUnitaProduttive(idAzienda);
  }

  public TerreniVO getTerreni(Long idAzienda, Long idUte, Long anno,
      String criterio) throws Exception, SolmrException
  {
    return terreniLocal.getTerreni(idAzienda, idUte, anno, criterio);
  }

  public Vector<Vector<Long>> getIdParticelle(Long idAzienda, Long idUte,
      Long anno, String criterio, Long valore) throws Exception,
      SolmrException
  {
    return terreniLocal.getIdParticelle(idAzienda, idUte, anno, criterio,
        valore);
  }

  public Vector<ParticelleVO> getParticelleByIdRange(
      Vector<Vector<Long>> idRange) throws Exception, SolmrException
  {
    return terreniLocal.getParticelleByIdRange(idRange);
  }
  
  public Vector<TipoSettoreAbacoVO> getListSettoreAbaco() 
      throws Exception
  {
    return terreniLocal.getListSettoreAbaco();
  }

  public Vector<ParticellaUtilizzoVO> getElencoParticelleForAziendaAndUsoSecondario(
      Long idAzienda, String anno) throws SolmrException, Exception
  {
    return fascicoloLocal.getElencoParticelleForAziendaAndUsoSecondario(
        idAzienda, anno);
  }

  public Vector<ParticellaUtilizzoVO> getElencoConsistenzaParticelleForAziendaAndUsoSecondario(
      Long idAzienda, Long idDichiarazioneConsistenza) throws SolmrException,
      Exception
  {
    return fascicoloLocal
        .getElencoConsistenzaParticelleForAziendaAndUsoSecondario(idAzienda,
            idDichiarazioneConsistenza);
  }

  // Metodo per recuperare le anomalie relative ad una particella
  public Vector<EsitoControlloParticellaVO> getElencoEsitoControlloParticella(
      Long idConduzioneParticella) throws Exception, SolmrException
  {
    return fascicoloLocal
        .getElencoEsitoControlloParticella(idConduzioneParticella);
  }

  // Metodo che restituisce la data di esecuzione controlli di una determinata
  // azienda agricola
  // in formato dd/mm/yyyy hh/mm/ss
  public String getDataUltimaEsecuzioneControlli(Long idAzienda)
      throws Exception
  {
    return fascicoloLocal.getDataUltimaEsecuzioneControlli(idAzienda);
  }

  // Metodo recuperare i dati della particella che abbiamo ricevuto dal SIAN in
  // relazione
  // alla particella selezionata dopo la ricerca particellare/piano colturale
  public ParticellaCertificataVO findParticellaCertificataByParametri(
      ParticellaVO particellaVO) throws Exception
  {
    return fascicoloLocal.findParticellaCertificataByParametri(particellaVO);
  }

  // Metodo per recuperare i tipi documento
  public Vector<CodeDescription> getTipiDocumento() throws Exception
  {
    return anagDenominazioniLocal.getTipiDocumento();
  }

  // Metodo per recuperare l'elenco delle variazioni storiche di una determinata
  // particella a
  // partire dall'id storico particella
  public Vector<ParticellaVO> getElencoStoricoParticella(Long idParticella)
      throws SolmrException, Exception
  {
    return fascicoloLocal.getElencoStoricoParticella(idParticella);
  }

  // Metodo per effettuare l'eliminazione massiva degli utilizzi della
  // particella
  public void eliminaUtilizzoParticella(Vector<String> elencoConduzioni,
      Vector<String> elencoIdUtilizzoParticella, long idUtenteAggiornamento)
      throws SolmrException, Exception
  {
    fascicoloLocal.eliminaUtilizzoParticella(elencoConduzioni,
        elencoIdUtilizzoParticella, idUtenteAggiornamento);
  }

  /**
   * Metodo per recuperare l'elenco delle particelle in relazione ad un'azienda
   * e al suo stato
   * 
   */
  public Vector<ParticellaVO> getElencoParticelleForImportByAzienda(
      AnagAziendaVO searchAnagAziendaVO, AnagAziendaVO anagAziendaVO,
      RuoloUtenza ruoloUtenza) throws Exception, SolmrException
  {
    return fascicoloLocal.getElencoParticelleForImportByAzienda(
        searchAnagAziendaVO, anagAziendaVO, ruoloUtenza);
  }

  /**
   * Metodo per recuperare il record dalla tabella DB_STORICO_PARTICELLA a
   * partire dalla chiave primaria
   * 
   * @param idStoricoParticella
   *          Long
   * @return ParticellaVO
   * @throws Exception
   */
  public ParticellaVO getStoricoParticella(Long idStoricoParticella)
      throws Exception
  {
    return fascicoloLocal.getStoricoParticella(idStoricoParticella);
  }

  // Fine Gestione Terreni

  // Gestione metodi di collegamento al WEB-SERVICE SIAN

  

  public SianAnagTributariaGaaVO ricercaAnagrafica(String cuaa, SianUtenteVO sianUtenteVO) throws SolmrException,
      Exception
  {
    return sianLocal.ricercaAnagraficaFromDB(cuaa, sianUtenteVO);
  }

  public RespAnagFascicoloVO getRespAnagFascicolo(
      Long idAzienda) throws SolmrException, Exception
  {
    return sianLocal.getRespAnagFascicolo(idAzienda);
  }

  public SianFascicoloResponseVO trovaFascicolo(String cuaa)
      throws SolmrException, Exception
  {
    return sianLocal.trovaFascicolo(cuaa);
  }

  /**
   * Metodo che mi restituisce l'elenco degli allevamenti relativi ad una
   * determinata azienda agricola tramite collegamento con web-service SIAN
   * 
   * @param cuaa
   *          String
   * @return Hashtable
   * @throws SolmrException
   * @throws Exception
   */
  /*public Hashtable<BigDecimal, SianAllevamentiVO> leggiAllevamenti(String cuaa, SianUtenteVO sianUtenteVO)
      throws SolmrException, Exception
  {
    Hashtable<BigDecimal, SianAllevamentiVO> allevamenti = null;
    try
    {
      allevamenti = sianLocal.leggiAllevamenti(cuaa);
    }
    catch (SolmrException s)
    {
      if (!s.getMessage().equals(
          (String) AnagErrors.get("ERR_SIAN_NO_ALLEVAMENTI")))
        throw new SolmrException(s.getMessage());
    }

    if (allevamenti == null || allevamenti.size() == 0)
    {
       //Non ho trovato nessun allevamento con questo CUAA, quindi provo a
       //vedere se ci sono CUAA collegati
       String cuaaColl[] = getCUAACollegati(cuaa, sianUtenteVO);
      if (cuaaColl != null && cuaaColl.length > 0)
      {
        // scorro tutti i CUAA in cerca di uno che abbia degli allevamenti... al
        // primo che mi risponde in
        // modo positivo mi fermo
        for (int i = 0; i < cuaaColl.length; i++)
        {

          try
          {
            allevamenti = sianLocal.leggiAllevamenti(cuaaColl[i]);
          }
          catch (SolmrException s)
          {
            if (!s.getMessage().equals(
                (String) AnagErrors.get("ERR_SIAN_NO_ALLEVAMENTI")))
              throw new SolmrException(s.getMessage());
          }

          // se trovo degli allevamenti esco dal ciclo
          if (allevamenti != null && allevamenti.size() > 0)
            break;
        }
      }
    }
    if (allevamenti == null || allevamenti.size() == 0)
      throw new SolmrException((String) AnagErrors
          .get("ERR_SIAN_NO_ALLEVAMENTI"));
    return allevamenti;
  }*/
  
  /*public Hashtable<BigDecimal, SianAllevamentiVO> leggiAllevamentiNoProfile(String cuaa)
      throws SolmrException, Exception
  {
    Hashtable<BigDecimal, SianAllevamentiVO> allevamenti = null;
    try
    {
      allevamenti = sianLocal.leggiAllevamenti(cuaa);
    }
    catch (SolmrException s)
    {
      if (!s.getMessage().equals(
          (String) AnagErrors.get("ERR_SIAN_NO_ALLEVAMENTI")))
        throw new SolmrException(s.getMessage());
    }
    
    return allevamenti;
  }*/

  public ElencoRegistroDiStallaVO elencoRegistriStalla(String codiceAzienda,
      String codiceSpecie, String cuaa, String tipo, String ordinamento, SianUtenteVO sianUtenteVO)
      throws SolmrException, Exception
  {
    ElencoRegistroDiStallaVO registroStalla = sianLocal.elencoRegistriStalla(
        codiceAzienda, codiceSpecie, cuaa, tipo, ordinamento);

    if (registroStalla == null || registroStalla.getRegistroDiStalla() == null)
    {
      /**
       * Non ho trovato nessun registro Stalla con questo CUAA, quindi provo a
       * vedere se ci sono CUAA collegati
       */
      String cuaaColl[] = getCUAACollegati(cuaa,sianUtenteVO);

      if (cuaaColl != null && cuaaColl.length > 0)
      {
        // scorro tutti i CUAA in cerca di uno che abbia dei registroStalla...
        // al primo che mi risponde in
        // modo positivo mi fermo
        for (int i = 0; i < cuaaColl.length; i++)
        {
          registroStalla = sianLocal.elencoRegistriStalla(codiceAzienda,
              codiceSpecie, cuaaColl[i], tipo, ordinamento);
          // se trovo dei registroStalla esco dal ciclo
          if (registroStalla != null
              && registroStalla.getRegistroDiStalla() != null)
            break;
        }
      }
    }
    return registroStalla;
  }
  
  public ElencoRegistroDiStallaVO elencoRegistriStallaNoProfile(String codiceAzienda,
      String codiceSpecie, String cuaa, String tipo, String ordinamento)
      throws SolmrException, Exception
  {
    ElencoRegistroDiStallaVO registroStalla = sianLocal.elencoRegistriStalla(
        codiceAzienda, codiceSpecie, cuaa, tipo, ordinamento);

    return registroStalla;
  }

  /**
   * Metodo che mi restituisce l'elenco delle particelle ad una determinata
   * azienda agricola, tramite collegamento con web-service SIAN, ordinate per
   * comune,sezione,foglio, particella e subalterno
   * 
   * @param cuaa
   *          String
   * @return SianTerritorioVO[]
   * @throws SolmrException
   * @throws Exception
   */
  public SianTerritorioVO[] leggiTerritorio(String cuaa,
      StringBuffer annoCampagna) throws SolmrException, Exception
  {
    return sianLocal.leggiPianoColturale(cuaa, annoCampagna);
  }

  /**
   * Metodo che si collega al web-service SIAN per recuperare l'elenco dei
   * titoli produttore aggregati
   * 
   * @param cuaa
   *          String
   * @param campagna
   *          String
   * @return Vector
   * @throws SolmrException
   * @throws Exception
   */
  public Vector<SianTitoliAggregatiVO> titoliProduttoreAggregati(String cuaa,
      String campagna, SianUtenteVO sianUtenteVO) throws SolmrException, Exception
  {
    Vector<SianTitoliAggregatiVO> elencoSianTitoliAggregatiVO = sianLocal
        .titoliProduttoreAggregati(cuaa, campagna);

    if (elencoSianTitoliAggregatiVO == null
        || elencoSianTitoliAggregatiVO.size() == 0)
    {
      /**
       * Non ho trovato nessun titolo con questo CUAA, quindi provo a vedere se
       * ci sono CUAA collegati
       */
      String cuaaColl[] = getCUAACollegati(cuaa, sianUtenteVO);
      if (cuaaColl != null && cuaaColl.length > 0)
      {
        // scorro tutti i CUAA in cerca di uno che abbia dei titoli... al primo
        // che mi risponde in
        // modo positivo mi fermo
        for (int i = 0; i < cuaaColl.length; i++)
        {
          elencoSianTitoliAggregatiVO = sianLocal.titoliProduttoreAggregati(
              cuaaColl[i], campagna);
          // se trovo dei titoli esco dal ciclo
          if (elencoSianTitoliAggregatiVO != null
              && elencoSianTitoliAggregatiVO.size() > 0)
            break;
        }
      }
    }
    if (elencoSianTitoliAggregatiVO == null
        || elencoSianTitoliAggregatiVO.size() == 0)
      throw new SolmrException((String) AnagErrors.get("ERR_SIAN_NO_TITOLI"));
    return elencoSianTitoliAggregatiVO;
  }

  /**
   * Metodo che richiama il web-service SIAN per la visualizzazione delle quote
   * latte
   * 
   * @param cuaa
   *          String
   * @param campagna
   *          String
   * @return Vector
   * @throws SolmrException
   * @throws Exception
   */
  public Vector<SianQuoteLatteAziendaVO> quoteLatte(String cuaa, String campagna, SianUtenteVO sianUtenteVO)
      throws SolmrException, Exception
  {
    Vector<SianQuoteLatteAziendaVO> quoteLatte = sianLocal.quoteLatte(cuaa,
        campagna);

    if (quoteLatte == null || quoteLatte.size() == 0)
    {
      /**
       * Non ho trovato nessuna quota latte con questo CUAA, quindi provo a
       * vedere se ci sono CUAA collegati
       */
      String cuaaColl[] = getCUAACollegati(cuaa, sianUtenteVO);
      if (cuaaColl != null && cuaaColl.length > 0)
      {
        // scorro tutti i CUAA in cerca di uno che abbia delle quote latte... al
        // primo che mi risponde in
        // modo positivo mi fermo
        for (int i = 0; i < cuaaColl.length; i++)
        {
          quoteLatte = sianLocal.quoteLatte(cuaaColl[i], campagna);
          // se trovo delle quote latte esco dal ciclo
          if (quoteLatte != null && quoteLatte.size() > 0)
            break;
        }
      }
    }
    if (quoteLatte == null || quoteLatte.size() == 0)
      throw new SolmrException((String) AnagErrors
          .get("ERR_SIAN_NO_QUOTE_LATTE"));
    return quoteLatte;
  }

  /**
   * Metodo per recuperare l'oggetto per decodificare la specie animale
   * proveniente dal SIAN in relazione al codice specie
   * 
   * @param codiceSpecie
   *          String
   * @return StringcodeDescription
   * @throws Exception
   * @throws SolmrException
   */
  public StringcodeDescription getSianTipoSpecieByCodiceSpecie(
      String codiceSpecie) throws Exception, SolmrException
  {
    return sianLocal.getSianTipoSpecieByCodiceSpecie(codiceSpecie);
  }

  /**
   * Metodo per recuperare l'oggetto per decodificare la specie animale
   * proveniente dal SIAN in relazione al idTipoSpecieAnimale
   * 
   * @param idTipoSpecieAnimale
   *          long
   * @return StringcodeDescription
   * @throws DataAccessException
   */
  public StringcodeDescription getSianTipoSpecieByIdSpecieAnimale(
      long idTipoSpecieAnimale) throws Exception
  {
    return sianLocal.getSianTipoSpecieByIdSpecieAnimale(idTipoSpecieAnimale);
  }

  /**
   * Metodo che si occupa di invocare il webservice SIAN per ottenere i dati
   * dell'anagrafe tributaria e aggiornare le tabelle in anagrafe
   * 
   * @param elencoCuaa
   * @throws Exception
   */
  public void sianAggiornaDatiTributaria(String[] elencoCuaa, SianUtenteVO sianUtenteVO)
      throws Exception
  {
    sianLocal.sianAggiornaDatiTributaria(elencoCuaa, sianUtenteVO);
  }

  /**
   * Metodo che mi consente, attraverso il richiamo del web-service SIAN di
   * recuperare i titoli produttore
   * 
   * @param CUAA
   *          String
   * @param campagna
   *          String
   * @return SianTitoloRispostaVO
   * @throws SolmrException
   * @throws Exception
   */
  public SianTitoloRispostaVO titoliProduttore(String CUAA, String campagna, SianUtenteVO sianUtenteVO)
      throws SolmrException, Exception
  {
    SianTitoloRispostaVO sianTitoloRispostaVO = sianLocal.titoliProduttore(
        CUAA, campagna);

    if (sianTitoloRispostaVO == null
        || sianTitoloRispostaVO.getRisposta() == null
        || sianTitoloRispostaVO.getRisposta().length == 0)
    {
      /**
       * Non ho trovato nessun titolo con questo CUAA, quindi provo a vedere se
       * ci sono CUAA collegati
       */
      String cuaaColl[] = getCUAACollegati(CUAA, sianUtenteVO);

      if (cuaaColl != null && cuaaColl.length > 0)
      {
        // scorro tutti i CUAA in cerca di uno che abbia dei titoli... al primo
        // che mi risponde in
        // modo positivo mi fermo
        for (int i = 0; i < cuaaColl.length; i++)
        {
          sianTitoloRispostaVO = sianLocal.titoliProduttore(cuaaColl[i],
              campagna);
          // se trovo dei titoli esco dal ciclo
          if (sianTitoloRispostaVO != null
              && sianTitoloRispostaVO.getRisposta() != null
              && sianTitoloRispostaVO.getRisposta().length > 0)
            break;
        }
      }
    }
    return sianTitoloRispostaVO;
  }

  /**
   * Metodo che mi consente, attraverso il richiamo del web-service SIAN di
   * recuperare i titoli produttore con info pegni
   * 
   * @param CUAA
   *          String
   * @return SianTitoloRispostaVO
   * @throws SolmrException
   * @throws Exception
   */
  public SianTitoloRispostaVO titoliProduttoreConInfoPegni(String CUAA, SianUtenteVO sianUtenteVO)
      throws SolmrException, Exception
  {
    SianTitoloRispostaVO sianTitoloRispostaVO = sianLocal
        .titoliProduttoreConInfoPegni(CUAA);

    if (sianTitoloRispostaVO == null
        || sianTitoloRispostaVO.getRisposta() == null
        || sianTitoloRispostaVO.getRisposta().length == 0)
    {
      /**
       * Non ho trovato nessun titolo con questo CUAA, quindi provo a vedere se
       * ci sono CUAA collegati
       */
      String cuaaColl[] = getCUAACollegati(CUAA, sianUtenteVO);

      if (cuaaColl != null && cuaaColl.length > 0)
      {
        // scorro tutti i CUAA in cerca di uno che abbia dei titoli... al primo
        // che mi risponde in
        // modo positivo mi fermo
        for (int i = 0; i < cuaaColl.length; i++)
        {
          sianTitoloRispostaVO = sianLocal
              .titoliProduttoreConInfoPegni(cuaaColl[i]);
          // se trovo dei titoli esco dal ciclo
          if (sianTitoloRispostaVO != null
              && sianTitoloRispostaVO.getRisposta() != null
              && sianTitoloRispostaVO.getRisposta().length > 0)
            break;
        }
      }
    }
    return sianTitoloRispostaVO;
  }

  /**
   * Metodo che mi restituisce l'elenco dei TIPI_OPR decodificati da SIAN
   * 
   * @param principale
   * @param orderBy []
   * @return it.csi.solmr.dto.DoubleStringcodeDescription[]
   * @throws Exception
   */
  public DoubleStringcodeDescription[] getListSianTipoOpr(boolean principale,
      String orderBy[]) throws Exception
  {
    return sianLocal.getListSianTipoOpr(principale, orderBy);
  }

  /**
   * Metodo che si occupa di invocare il web-service SIAN per l'aggiornamento
   * dei dati della BDN
   * 
   * @param CUAA
   * @throws SolmrException
   * @throws Exception
   */
  public void sianAggiornaDatiBDN(String CUAA) throws SolmrException,
      Exception
  {
    // Ricerco gli eventuali CUAA collegati
    // String cuaaColl[]=getCUAACollegati(CUAA);
    sianLocal.serviceSianAggiornaDatiBDN(CUAA);
  }

  /**
   * Metodo che accede al DB Sian, passa come parametro il codice sia operatore
   * e restituisce la codifica "CODICE-DESCRIZIONE" dell'operatore.
   * 
   * @param codiceSianOpr
   * @throws Exception
   */
  public String getOrganismoPagatoreFormatted(String codiceSianOpr)
      throws Exception
  {
    return sianLocal.getOrganismoPagatoreFormatted(codiceSianOpr);
  }

  // Fine gestione metodi di collegamento al WEB-SERVICE SIAN

  public AnagAziendaVO findAziendaAttivabyCriterio(String cuaa,
      String partitaIva) throws Exception, SolmrException
  {
    return anagAziendaLocal.findAziendaAttivabyCriterio(cuaa, partitaIva);
  }

  public Vector<StringcodeDescription> getProvincePiemonte()
      throws Exception, SolmrException
  {
    return anagDenominazioniLocal.getProvincePiemonte();
  }

  public ProvinciaVO getProvinciaByCriterio(String criterio)
      throws Exception, SolmrException
  {
    return anagDenominazioniLocal.getProvinciaByCriterio(criterio);
  }

  public String ricercaCodiceComuneNonEstinto(String descrizioneComune,
      String siglaProvincia) throws Exception, SolmrException
  {
    return anagDenominazioniLocal.ricercaCodiceComuneNonEstinto(
        descrizioneComune, siglaProvincia);
  }
  
  public String ricercaCodiceComuneFlagEstinto(String descrizioneComune,
      String siglaProvincia, String estinto)
          throws Exception, SolmrException
  {
    return anagDenominazioniLocal.ricercaCodiceComuneFlagEstinto(descrizioneComune, siglaProvincia, estinto);
  }

  public void insertRappLegaleTitolare(Long idAzienda, PersonaFisicaVO pfVO,
      long idUtenteAggiornamento) throws Exception, SolmrException
  {
    anagAziendaLocal.insertRappLegaleTitolare(idAzienda, pfVO, idUtenteAggiornamento);
  }

  public void checkCUAA(String cuaa) throws DataAccessException,
      Exception, SolmrException
  {
    anagAziendaLocal.checkCUAA(cuaa);
  }

  public void checkIsCUAAPresent(String cuaa, Long idAzienda)
      throws Exception, SolmrException
  {
    anagAziendaLocal.checkIsCUAAPresent(cuaa, idAzienda);
  }

  public Vector<Long> getIdPersoneFisiche(String codFiscale, String cognome,
      String nome, String dataNascita, String istatNascita,
      String istatResidenza, boolean personaAttiva) throws Exception,
      SolmrException
  {
    return soggettiLocal.getIdPersoneFisiche(codFiscale, cognome, nome,
        dataNascita, istatNascita, istatResidenza, personaAttiva);
  }

  public Vector<PersonaFisicaVO> getListPersoneFisicheByIdRange(
      Vector<Long> collIdPF) throws Exception, SolmrException
  {
    return soggettiLocal.getListPersoneFisicheByIdRange(collIdPF);
  }

  public PersonaFisicaVO findByPrimaryKey(Long idPersonaFisica)
      throws Exception, SolmrException
  {
    return soggettiLocal.findByPrimaryKey(idPersonaFisica);
  }

  public Vector<PersonaFisicaVO> findPersonaFisicaByIdSoggettoAndIdAzienda(
      Long idSoggetto, Long idAzienda) throws Exception, SolmrException
  {
    return soggettiLocal.findPersonaFisicaByIdSoggettoAndIdAzienda(idSoggetto,
        idAzienda);
  }

  public String getDenominazioneByIdAzienda(Long idAzienda)
      throws Exception, SolmrException
  {
    return anagAziendaLocal.getDenominazioneByIdAzienda(idAzienda);
  }

  public String getRappLegaleTitolareByIdAzienda(Long idAzienda)
      throws Exception, SolmrException
  {
    return anagAziendaLocal.getRappLegaleTitolareByIdAzienda(idAzienda);
  }

  public Vector<Long> getIdAziendeBySoggetto(Long idSoggetto)
      throws Exception, SolmrException
  {
    return soggettiLocal.getIdAziendeBySoggetto(idSoggetto);
  }

  public Vector<AnagAziendaVO> findAziendeByIdAziende(Vector<Long> idAziendeVect)
      throws Exception, SolmrException
  {
    return anagAziendaLocal.findAziendeByIdAziende(idAziendeVect);
  }

  public String getSiglaProvinciaByIstatProvincia(String istatProvincia)
      throws Exception, SolmrException
  {
    return anagAziendaLocal.getSiglaProvinciaByIstatProvincia(istatProvincia);
  }

  public PersonaFisicaVO findPersonaFisica(Long idPersonaFisica)
      throws Exception, SolmrException
  {
    return soggettiLocal.findPersonaFisica(idPersonaFisica);
  }

  public PersonaFisicaVO getDatiSoggettoPerMacchina(Long idPersonaFisica)
      throws Exception, SolmrException
  {
    return soggettiLocal.getDatiSoggettoPerMacchina(idPersonaFisica);
  }

  public AnagraficaAzVO getDatiAziendaPerMacchine(Long idAzienda)
      throws Exception, SolmrException
  {
    return anagAziendaLocal.getDatiAziendaPerMacchine(idAzienda);
  }

  public Vector<AnagAziendaVO> getListaStoricoAzienda(Long idAzienda)
      throws Exception, SolmrException
  {
    return anagAziendaLocal.getListaStoricoAzienda(idAzienda);
  }

  // Metodo per recuperare l'elenco delle unità produttive valide associate ad
  // un'azienda agricola
  public Vector<UteVO> getElencoUteAttiveForAzienda(Long idAzienda)
      throws SolmrException, Exception
  {
    return fascicoloLocal.getElencoUteAttiveForAzienda(idAzienda);
  }

  // Metodo per recuperare l'elenco delle sezioni relative ad uno specifico
  // comune
  public Vector<CodeDescription> getSezioniByComune(String istatComune)
      throws SolmrException, Exception
  {
    return fascicoloLocal.getSezioniByComune(istatComune);
  }

  // Metodo per recuperare l'elenco dei fogli in relazione al comune o stato
  // estero
  // ed eventualmente la sezione
  public Vector<FoglioVO> getFogliByComuneAndSezione(String istatComune,
      String sezione, Long foglio) throws SolmrException, Exception
  {
    return fascicoloLocal.getFogliByComuneAndSezione(istatComune, sezione,
        foglio);
  }

  // Metodo per recuperare l'elenco delle particelle in relazione al comune o
  // stato estero
  // e il foglio
  public Vector<ParticellaVO> getParticelleByParametri(
      String descrizioneComune, Long foglio, String sezione, Long particella,
      String flagEstinto) throws SolmrException, Exception
  {
    return fascicoloLocal.getParticelleByParametri(descrizioneComune, foglio,
        sezione, particella, flagEstinto);
  }

  // Metodo per recuperare la sezione a partire dall'istat del comune e dalla
  // sezione stessa
  public String ricercaSezione(String istatComune, String sezione)
      throws SolmrException, Exception
  {
    return fascicoloLocal.ricercaSezione(istatComune, sezione);
  }

  // Metodo per recuperare il foglio in relazione al comune o stato estero, il
  // foglio
  // stesso ed eventualmente la sezione
  public FoglioVO ricercaFoglio(String istatComune, String sezione, Long foglio)
      throws SolmrException, Exception
  {
    return fascicoloLocal.ricercaFoglio(istatComune, sezione, foglio);
  }

  // Metodo per recuperare la particella in relazione al comune o stato estero
  // e il foglio
  public ParticellaVO ricercaParticellaAttiva(String istatComune,
      String sezione, Long foglio, Long particella, String subalterno)
      throws SolmrException, Exception
  {
    return fascicoloLocal.ricercaParticellaAttiva(istatComune, sezione,
        foglio, particella, subalterno);
  }

  // Metodo per controllare che una particella non sia già attribuita ad una
  // azienda
  public void checkParticellaByAzienda(Long idParticella, Long idAzienda)
      throws SolmrException, Exception
  {
    fascicoloLocal.checkParticellaByAzienda(idParticella, idAzienda);
  }

  // Metodo per controllare che una particella non sia già attribuita ad una
  // azienda
  public Vector<ElencoAziendeParticellaVO> elencoAziendeByParticellaAndConduzione(
      Long idParticella, Long idAzienda) throws SolmrException, Exception
  {
    return fascicoloLocal.elencoAziendeByParticellaAndConduzione(idParticella,
        idAzienda);
  }

  // Metodo per recuperare i tipiConduzione tranne "proprietà"
  public Vector<CodeDescription> getTipiTitoloPossessoExceptProprieta()
      throws SolmrException, Exception
  {
    return anagDenominazioniLocal.getTipiTitoloPossessoExceptProprieta();
  }

  // Metodo per recuperare il valore dell'ultima data fine conduzione delle
  // particelle
  // associate ad un azienda agricola
  public java.util.Date getMaxDataFineConduzione(Long idParticella,
      Long idAzienda) throws SolmrException, Exception
  {
    return fascicoloLocal.getMaxDataFineConduzione(idParticella, idAzienda);
  }

  // Metodo per recuperare i tipi utilizzo attivi
  public Vector<CodeDescription> getTipiUtilizzoAttivi() throws SolmrException,
      Exception
  {
    return fascicoloLocal.getTipiUtilizzoAttivi();
  }

  // Metodo per recuperare i tipi utilizzo attivi dato un indirizzo
  public Vector<CodeDescription> getTipiUtilizzoAttivi(int idIndirizzo)
      throws SolmrException, Exception
  {
    return fascicoloLocal.getTipiUtilizzoAttivi(idIndirizzo);
  }

  // Metodo per inserire un record in DB_PARTICELLA
  public Long insertParticella() throws SolmrException, Exception
  {
    return fascicoloLocal.insertParticella();
  }

  // Metodo per recuperare la particella provvisoria in relazione al comune o
  // stato estero
  // e il foglio
  public ParticellaVO ricercaParticellaProvvisoriaAttiva(String istatComune,
      String sezione, Long foglio) throws SolmrException, Exception
  {
    return fascicoloLocal.ricercaParticellaProvvisoriaAttiva(istatComune,
        sezione, foglio);
  }

  // Metodo per recuperare la particella in relazione al comune o stato estero
  // la sezione e il foglio indipendentemente dal fatto che sia attiva o meno
  public ParticellaVO ricercaParticella(String istatComune, String sezione,
      Long foglio, Long particella, String subalterno) throws SolmrException,
      Exception
  {
    return fascicoloLocal.ricercaParticella(istatComune, sezione, foglio,
        particella, subalterno);
  }

  public void cessaParticelleByIdParticellaRange(long idParticella[])
      throws SolmrException, Exception
  {
    fascicoloLocal.cessaParticelleByIdParticellaRange(idParticella);
  }

  // Metodo per recuperare il valore massimo inseribile per una particella non
  // presente in archivio
  // nata dal frazionamento di una particella già esistente in archivio che
  // presenta già una
  // particella nata dal suo frazionamento.....(Per chi lo legge in fututo so
  // che è complesso ma io l'hi
  // capito....) :)
  public double getMaxSupCatastaleInseribile(Long idParticella)
      throws SolmrException, Exception
  {
    return fascicoloLocal.getMaxSupCatastaleInseribile(idParticella);
  }

  // Metodo per recuperare la particella a partire dalla sua chiave primaria
  public ParticellaVO findParticellaByPrimaryKey(Long idStoricoParticella)
      throws SolmrException, Exception
  {
    return fascicoloLocal.findParticellaByPrimaryKey(idStoricoParticella);
  }

  // Metodo per recuperare l'elenco dei titoli di studio
  public Vector<CodeDescription> getTitoliStudio() throws SolmrException,
      Exception
  {
    return anagDenominazioniLocal.getTitoliStudio();
  }

  // Metodo per recuperare l'elenco dei prefissi per un cellulare
  public Vector<CodeDescription> getPrefissiCellulare() throws SolmrException,
      Exception
  {
    return anagDenominazioniLocal.getPrefissiCellulare();
  }

  // Metodo per recuperare l'indirizzo di studio in relazione al titolo
  // selezionato
  public Vector<CodeDescription> getIndirizzoStudioByTitolo(Long idTitoloStudio)
      throws SolmrException, Exception
  {
    return anagDenominazioniLocal.getIndirizzoStudioByTitolo(idTitoloStudio);
  }

  // Metodo per recuperare i dati della particella e dell'utilizzo ad essa
  // relativa a partire
  // dall' id utilizzi particella
  public ParticellaVO getParticellaVOByIdUtilizzoParticella(
      Long idUtilizzoParticella) throws SolmrException, Exception
  {
    return fascicoloLocal
        .getParticellaVOByIdUtilizzoParticella(idUtilizzoParticella);
  }

  // Metodo per recuperare i dati della particella a partire dall'
  // id_conduzione_particella
  public ParticellaVO getParticellaVOByIdConduzioneParticella(
      Long idConduzioneParticella) throws SolmrException, Exception
  {
    return fascicoloLocal
        .getParticellaVOByIdConduzioneParticella(idConduzioneParticella);
  }

  // Metodo per recuperare il valore di tutte le superfici utilizzate da
  // un'azienda agricola esclusa
  // quella selezionata
  public double getTotSupUtilizzateByIdCondParticellaNotIncludeIdUtilizzo(
      Long idConduzioneParticella, Long idUtilizzoParticella, String anno)
      throws SolmrException, Exception
  {
    return fascicoloLocal
        .getTotSupUtilizzateByIdCondParticellaNotIncludeIdUtilizzo(
            idConduzioneParticella, idUtilizzoParticella, anno);
  }

  // Metodo per recuperare il valore dalla tabella parametro: importante per la
  // gestione dei terreni
  public String getValoreFromParametroByIdCode(String codice)
      throws Exception, SolmrException
  {
    return anagDenominazioniLocal.getValoreFromParametroByIdCode(codice);
  }


  // Metodo per recuperare gli elementi del dettaglio del soggetto collegato a
  // partire dall'id_contitolare
  public PersonaFisicaVO getDettaglioSoggettoByIdContitolare(Long idContitolare)
      throws Exception, SolmrException
  {
    return soggettiLocal.getDettaglioSoggettoByIdContitolare(idContitolare);
  }
  
  public  TesserinoFitoSanitarioVO getTesserinoFitoSanitario(String codiceFiscale) 
      throws Exception, SolmrException
  {
    return soggettiLocal.getTesserinoFitoSanitario(codiceFiscale);
  }

  // Restituisce l'elenco (ContoCorrenteVO) dei conti correnti legati ad
  // un'azienda
  public Vector<ContoCorrenteVO> getContiCorrenti(Long idAzienda,
      boolean estinto) throws Exception
  {
    return contoCorrenteLocal.getContiCorrenti(idAzienda, estinto);
  }

  // Restituisce l'elenco (ContoCorrenteVO) dei conti correnti legati ad
  // un'azienda
  public Vector<ContoCorrenteVO> getContiCorrenti(Long idAzienda,
      java.util.Date dataSituazioneAl) throws Exception
  {
    // Il terzo parametro a true indica che si considera significativo solo
    // l'anno nella dataSituazioneAl
    return contoCorrenteLocal.getContiCorrenti(idAzienda, dataSituazioneAl,
        new Boolean(true));
  }

  // Metodo per effettuare una cancellazione logica da un conto corrente
  public void deleteContoCorrente(Long idConto, long idUtenteAggiornamento)
      throws Exception, SolmrException
  {
    contoCorrenteLocal.deleteContoCorrente(idConto, idUtenteAggiornamento);
  }

  // Metodo per effettuare l'estinzione dei conti correnti collegati ad
  // un'azienda agricola
  public void desistsAccountCorrent(Long idAzienda, Long idUtente)
      throws SolmrException, Exception
  {
    contoCorrenteLocal.desistsAccountCorrent(idAzienda, idUtente);
  }

  /**
   * Restituisce l'elenco (ContoCorrenteVO) delle banche associate ad un ABI /
   * Denominazione
   * 
   * @param abi
   *          parametro di ricerca
   * @param denominazione
   *          parametro di ricerca
   * @return restituisce un vettore con l'elenco delle banche. Se non ci sono
   *         record restituisce un vettore di lunghezza zero
   * @throws Exception
   * @throws SolmrException
   */
  public BancaSportelloVO[] searchBanca(String abi, String denominazione)
      throws Exception, SolmrException
  {
    return contoCorrenteLocal.searchBanca(abi, denominazione);
  }

  /**
   * Restituisce l'elenco (ContoCorrenteVO) delle filiali associate ad un CAB /
   * Comune
   * 
   * @param abi
   *          parametro di ricerca
   * @param cab
   *          parametro di ricerca
   * @param comune
   *          parametro di ricerca
   * @return restituisce un vettore con l'elenco delle banche. Se non ci sono
   *         record restituisce un vettore di lunghezza zero
   * @throws Exception
   * @throws SolmrException
   */
  public BancaSportelloVO[] searchSportello(String abi, String cab,
      String comune) throws Exception, SolmrException
  {
    return contoCorrenteLocal.searchSportello(abi, cab, comune);
  }

  // Metodo per recuperare i tipi di fabbricato
  public Vector<CodeDescription> getTipiTipologiaFabbricato()
      throws SolmrException, Exception
  {
    return anagDenominazioniLocal.getTipiTipologiaFabbricato();
  }

  // Metodo per recuperare le forme dei fabbricati
  public Vector<CodeDescription> getTipiFormaFabbricato(
      Long idTipologiaFabbricato) throws SolmrException, Exception
  {
    return anagDenominazioniLocal
        .getTipiFormaFabbricato(idTipologiaFabbricato);
  }

  // Metodo per recuperare i tipi di coltura sotto serra
  public Vector<CodeDescription> getTipiColturaSerra(Long idTipologiaFabbricato)
      throws SolmrException, Exception
  {
    return anagDenominazioniLocal.getTipiColturaSerra(idTipologiaFabbricato);
  }

  public String getUnitaMisuraByTipoFabbricato(Long idTipologiaFabbricato)
      throws SolmrException, Exception
  {
    return anagDenominazioniLocal
        .getUnitaMisuraByTipoFabbricato(idTipologiaFabbricato);
  }

  public int getMesiRiscaldamentoBySerra(String tipologiaColturaSerra)
      throws SolmrException, Exception
  {
    return anagDenominazioniLocal
        .getMesiRiscaldamentoBySerra(tipologiaColturaSerra);
  }

  public double getFattoreCubaturaByFormaFabbricato(String idFormaFabbricato)
      throws SolmrException, Exception
  {
    return anagDenominazioniLocal
        .getFattoreCubaturaByFormaFabbricato(idFormaFabbricato);
  }

  // Metodo per recuperare l'elenco delle particelle ad uso fabbricato associate
  // ad una
  // unita produttiva
  public Vector<ParticellaVO> getElencoParticelleFabbricatoByUte(Long idUte,
      boolean serra) throws SolmrException, Exception
  {
    return fascicoloLocal.getElencoParticelleFabbricatoByUte(idUte, serra);
  }

  // Metodo per recuperare la somma delle superfici dei fabbricati che insistono
  // esclusivamente
  // sulla particella selezionata
  public String getSuperficiFabbricatiByParticella(Long idUte, Long idParticella)
      throws SolmrException, Exception
  {
    return fascicoloLocal.getSuperficiFabbricatiByParticella(idUte,
        idParticella);
  }

  // Metodo per l'inserimento dei fabbricati
  public Long inserisciFabbricato(FabbricatoVO fabbricatoVO,
      Vector<ParticellaVO> elencoParticelleSelezionate, long idUtenteAggiornamento)
      throws SolmrException, Exception
  {
    return fascicoloLocal.inserisciFabbricato(fabbricatoVO,
        elencoParticelleSelezionate, idUtenteAggiornamento);
  }

  // Metodo per effettuare la ricerca dei fabbricati relativi all'azienda
  // agricola selezionata
  public Vector<FabbricatoVO> ricercaFabbricatiByAzienda(Long idAzienda,
      String dataSituazioneAl) throws SolmrException, Exception
  {
    return fascicoloLocal.ricercaFabbricatiByAzienda(idAzienda,
        dataSituazioneAl);
  }

  // Metodo per recuperare il fabbricato a partire dalla sua chiave primaria
  public FabbricatoVO findFabbricatoByPrimaryKey(Long idFabbricato)
      throws SolmrException, Exception
  {
    return fascicoloLocal.findFabbricatoByPrimaryKey(idFabbricato);
  }

  /**
   * Metodo per recuperare l'elenco delle particelle su cui insiste il
   * fabbricato selezionato Se modifica è uguale a true significa che sto
   * costruendo l'elenco delle particelle da usare nella modifica, quindi non
   * devo far vedere quelle particelle che hanno la DataFineValidità impostata
   * 
   * @param fabbricatoVO
   *          FabbricatoVO
   * @param modifica
   *          boolean
   * @return Vector
   * @throws Exception
   */
  public Vector<ParticellaVO> getElencoParticelleByFabbricato(
      FabbricatoVO fabbricatoVO, boolean modifica) throws Exception
  {
    return fascicoloLocal.getElencoParticelleByFabbricato(fabbricatoVO,
        modifica);
  }

  // Metodo per recuperare l'elenco delle particelle ad uso fabbricato associate
  // ad una
  // unita produttiva associabili ad un fabbricato
  public Vector<ParticellaVO> getElencoParticelleFabbricatoByUteAssociabili(
      Long idUte, Vector<Long> elencoParticelle, boolean serra)
      throws SolmrException, Exception
  {
    return fascicoloLocal.getElencoParticelleFabbricatoByUteAssociabili(idUte,
        elencoParticelle, serra);
  }

  // Metodo per cessare l'utilizzo di una particella a fabbricato
  public void cessaUtilizzoParticellaFabbricato(FabbricatoVO fabbricatoVO)
      throws SolmrException, Exception
  {
    fascicoloLocal.cessaUtilizzoParticellaFabbricato(fabbricatoVO);
  }

  // Metodo per eliminare dei record da DB_FABBRICATO_PARTICELLA
  public void deleteParticellaFabbricato(ParticellaVO particellaFabbricatoVO)
      throws SolmrException, Exception
  {
    fascicoloLocal.deleteParticellaFabbricato(particellaFabbricatoVO);
  }

  // Metodo per effettuare la modifica del fabbricato
  public Long modificaFabbricato(FabbricatoVO fabbricatoVO,
      Vector<ParticellaVO> particelleForFabbricato,
      Vector<ParticellaVO> elencoParticelleAssociate,
      Vector<ParticellaVO> elencoParticelleAssociabili, long idUtenteAggiornamento,
      long idAzienda) throws SolmrException, Exception
  {
    return fascicoloLocal.modificaFabbricato(fabbricatoVO,
        particelleForFabbricato, elencoParticelleAssociate,
        elencoParticelleAssociabili, idUtenteAggiornamento, idAzienda);
  }

  // Metodo per eliminare dei record da DB_FABBRICATO_PARTICELLA a partire dal
  // fabbricato
  public void deleteParticellaFabbricatoByIdFabbricato(FabbricatoVO fabbricatoVO)
      throws SolmrException, Exception
  {
    fascicoloLocal.deleteParticellaFabbricatoByIdFabbricato(fabbricatoVO);
  }

  // Metodo per eliminare un record da DB_FABBRICATO a partire dal fabbricato
  public void deleteFabbricato(FabbricatoVO fabbricatoVO)
      throws SolmrException, Exception
  {
    fascicoloLocal.deleteFabbricato(fabbricatoVO);
  }

  // Metodo per eliminare un fabbricato e i suoi legami
  public void eliminaFabbricato(FabbricatoVO fabbricatoVO, long idAzienda)
      throws SolmrException, Exception
  {
    fascicoloLocal.eliminaFabbricato(fabbricatoVO, idAzienda);
  }

  // Metodo per recuperare l'elenco delle unità produttive valide ad una certa
  // data associate
  // ad un'azienda agricola
  public Vector<UteVO> getElencoUteAttiveForDateAndAzienda(Long idAzienda, String data)
      throws SolmrException, Exception
  {
    return fascicoloLocal.getElencoUteAttiveForDateAndAzienda(idAzienda, data);
  }

  // Metodo per recuperare l'elenco delle particelle relative ad un'azienda
  // agricola ed eventualmente
  // ad un'unità produttiva selezionata
  public Vector<ParticellaVO> getElencoParticelleForUteAndAzienda(Long idAzienda)
      throws SolmrException, Exception
  {
    return fascicoloLocal.getElencoParticelleForUteAndAzienda(idAzienda);
  }

  // Metodo per recuperare l'elenco delle particelle relative ad un'azienda
  // agricola,
  // ad un'unità produttiva e per riepilogo titolo possesso/comune
  public Vector<ParticellaVO> getElencoParticelleForUteAndAziendaPossAndComune(Long idAzienda)
      throws SolmrException, Exception
  {
    return fascicoloLocal
        .getElencoParticelleForUteAndAziendaPossAndComune(idAzienda);
  }

  // Metodo per recuperare l'elenco delle particelle relative ad un'azienda
  // agricola,
  // ad un'unità produttiva e per riepilogo comune
  public Vector<ParticellaVO> getElencoParticelleForUteAndAziendaComune(Long idAzienda)
      throws SolmrException, Exception
  {
    return fascicoloLocal.getElencoParticelleForUteAndAziendaComune(idAzienda);
  }

  // Metodo per effettuare la ricerca delle particelle attive in relazione ai
  // parametri di
  // ricerca
  public Vector<ParticellaVO> ricercaParticelleAttiveByParametri(ParticellaVO particellaVO,
      String data, Long idAzienda) throws SolmrException, Exception
  {
    return fascicoloLocal.ricercaParticelleAttiveByParametri(particellaVO,
        data, idAzienda);
  }

  // Metodo per controllare che non esistano conduzioni attive di particelle
  // associate ad
  // una azienda agricola
  public void checkCessaAziendaByConduzioneParticella(Long idUte)
      throws SolmrException, Exception
  {
    fascicoloLocal.checkCessaAziendaByConduzioneParticella(idUte);
  }

  // Metodo che mi restituisce un vettore contenente le date delle "fotografie"
  // effettuate
  // su una specifica azienda individuata attraverso l'id azienda
  public Vector<CodeDescription> getListaDateConsistenza(Long idAzienda) throws SolmrException,
      Exception
  {
    return fascicoloLocal.getListaDateConsistenza(idAzienda);
  }

  // Metodo per recuperare l'elenco delle fotografie relative alle particelle
  // attraverso l'id azienda e il codice fotografia terreni
  public Vector<ParticellaVO> getElencoConsistenzaParticelleForAzienda(Long idAzienda,
      Long idDichiarazioneConsistenza) throws SolmrException, Exception
  {
    return fascicoloLocal.getElencoConsistenzaParticelleForAzienda(idAzienda,
        idDichiarazioneConsistenza);
  }

  // Metodo per verificare se l'ultimo aggiornamento della consistenza sia
  // avvenuto dopo
  // l'ultima data di dichiarazione di consistenza
  public void checkLastAggiornamentoAfterMaxDichConsistenza(Long idAzienda)
      throws SolmrException, Exception
  {
    fascicoloLocal.checkLastAggiornamentoAfterMaxDichConsistenza(idAzienda);
  }

  // Metodo per recuperare l'elenco delle fotografie relative alle particelle
  // attraverso l'id azienda e il codice fotografia terreni
  public Vector<ParticellaVO> getElencoConsistenzaParticelleForAziendaAndPossessoAndComune(
      Long idAzienda, Long idDichiarazioneConsistenza) throws SolmrException,
      Exception
  {
    return fascicoloLocal
        .getElencoConsistenzaParticelleForAziendaAndPossessoAndComune(
            idAzienda, idDichiarazioneConsistenza);
  }

  // Metodo per recuperare l'elenco delle fotografie relative alle particelle
  // attraverso l'id azienda e il codice fotografia terreni
  public Vector<ParticellaVO> getElencoConsistenzaParticelleForAziendaAndComune(
      Long idAzienda, Long idDichiarazioneConsistenza) throws SolmrException,
      Exception
  {
    return fascicoloLocal.getElencoConsistenzaParticelleForAziendaAndComune(
        idAzienda, idDichiarazioneConsistenza);
  }

  // Metodo per recuperare l'anno successivo rispetto a quello di sistema nella
  // tabella degli utilizzi
  // inserito come previsione
  public String getAnnoPrevisioneUtilizzi(Long idAzienda)
      throws SolmrException, Exception
  {
    return fascicoloLocal.getAnnoPrevisioneUtilizzi(idAzienda);
  }

  // Metodo per recuperare l'elenco delle fotografie relative alle particelle
  // attraverso l'id azienda e la destinazione d'uso
  public Vector<ParticellaVO> getElencoConsistenzaParticelleForAziendaAndDestinazioneUso(
      Long idAzienda, Long idDichiarazioneConsistenza,
      Long idConduzioneParticella) throws SolmrException, Exception
  {
    return fascicoloLocal
        .getElencoConsistenzaParticelleForAziendaAndDestinazioneUso(idAzienda,
            idDichiarazioneConsistenza, idConduzioneParticella);
  }

  // Metodo per recuperare il valore di tutte le superfici condotte da
  // un'azienda agricola
  public String getTotaleSupCondotteByAzienda(Long idAzienda, String data)
      throws SolmrException, Exception
  {
    return fascicoloLocal.getTotaleSupCondotteByAzienda(idAzienda, data);
  }

  // Metodo per effettuare la ricerca delle particelle storicizzate in relazione
  // ai parametri
  // di ricerca
  public Vector<ParticellaVO> ricercaParticelleStoricizzateByParametri(
      ParticellaVO particellaVO, Long idDichiarazioneConsistenza)
      throws SolmrException, Exception
  {
    return fascicoloLocal.ricercaParticelleStoricizzateByParametri(
        particellaVO, idDichiarazioneConsistenza);
  }

  // Metodo per recuperare i dati territoriali della particella a partire
  // dall'id storico particella
  public ParticellaVO getDettaglioParticellaDatiTerritoriali(
      Long idStoricoParticella) throws SolmrException, Exception
  {
    return fascicoloLocal
        .getDettaglioParticellaDatiTerritoriali(idStoricoParticella);
  }

  // Metodo per recuperare i dati dell'uso del suolo e dei contratti della
  // particella a partire dall'id storico particella
  public Vector<ParticellaUtilizzoVO> getElencoParticellaUtilizzoVO(Long idConduzioneParticella,
      String anno) throws SolmrException, Exception
  {
    return fascicoloLocal.getElencoParticellaUtilizzoVO(
        idConduzioneParticella, anno);
  }

  // Metodo per recuperare il valore di tutte le superfici utilizzate da
  // un'azienda agricola
  public double getTotaleSupUtilizzateByIdConduzioneParticella(
      Long idConduzioneParticella, String anno) throws SolmrException,
      Exception
  {
    return fascicoloLocal.getTotaleSupUtilizzateByIdConduzioneParticella(
        idConduzioneParticella, anno);
  }

  // Metodo per recuperare i dati territoriali della particella a partire
  // dall'id dichiarazione
  // consistenza quindi si tratta di dati storicizzati
  public ParticellaVO getDettaglioParticellaStoricizzataDatiTerritoriali(
      Long idConduzioneDichiarata) throws SolmrException, Exception
  {
    return fascicoloLocal
        .getDettaglioParticellaStoricizzataDatiTerritoriali(idConduzioneDichiarata);
  }

  // Metodo per recuperare i dati della conduzione e dei contratti della
  // particella a partire dall'id conduzione
  // dichiarata e quindi si tratta di dati storicizzati
  public ParticellaVO getDettaglioParticellaStoricizzataConduzione(
      Long idConduzioneDichiarata) throws SolmrException, Exception
  {
    return fascicoloLocal
        .getDettaglioParticellaStoricizzataConduzione(idConduzioneDichiarata);
  }

  // Metodo per recuperare i dati dell'uso del suolo della particella a partire
  // dall'id conduzione dichiarata
  // quindi sto prelevando l'elenco degli utilizzi storicizzati della particella
  public Vector<ParticellaUtilizzoVO> getElencoStoricoParticellaUtilizzoVO(
      Long idConduzioneDichiarata, Long idDichiarazioneConsistenza)
      throws SolmrException, Exception
  {
    return fascicoloLocal.getElencoStoricoParticellaUtilizzoVO(
        idConduzioneDichiarata, idDichiarazioneConsistenza);
  }

  // Metodo per recuperare gli indirizzi degli utilizzi
  public Vector<CodeDescription> getElencoIndirizziUtilizzi() throws SolmrException,
      Exception
  {
    return anagDenominazioniLocal.getElencoIndirizziUtilizzi();
  }

  // Metodo per recuperare i tipi utilizzo dato un indirizzo tipo utilizzo
  public Vector<CodeDescription> getTipiUtilizzoForIdIndirizzoTipoUtilizzo(
      Long idTipoIndirizzoUtilizzo) throws SolmrException, Exception
  {
    return fascicoloLocal
        .getTipiUtilizzoForIdIndirizzoTipoUtilizzo(idTipoIndirizzoUtilizzo);
  }

  // Metodo per recuperare l'elenco delle particelle in relazione a dei filtri
  // di ricerca e agli utilizzi
  public Vector<ParticellaVO> ricercaParticelleByParametriAndUtilizzi(
      ParticellaVO particellaRicercaVO, Long idAzienda) throws SolmrException,
      Exception
  {
    return fascicoloLocal.ricercaParticelleByParametriAndUtilizzi(
        particellaRicercaVO, idAzienda);
  }

  // Metodo per effettuare la ricerca delle particelle a partire dai parametri
  // relativi alla particella
  // e ad uno speficifico utilizzo
  public Vector<ParticellaVO> ricercaParticelleByParametriAndUtilizzoSpecificato(
      Long idAzienda, ParticellaVO particellaRicercaVO) throws SolmrException,
      Exception
  {
    return fascicoloLocal.ricercaParticelleByParametriAndUtilizzoSpecificato(
        idAzienda, particellaRicercaVO);
  }

  // Metodo per recuperare l'elenco delle particelle in relazione a dei filtri
  // di ricerca e all'assenza
  // di utilizzi o al fatto che la somma delle superfici utilizzate relative ad
  // una conduzione non
  // sia uguale alla superficie condotta
  public Vector<ParticellaVO> ricercaParticelleByParametriSenzaUsoSuolo(
      ParticellaVO particellaRicercaVO, Long idAzienda) throws SolmrException,
      Exception
  {
    return fascicoloLocal.ricercaParticelleByParametriSenzaUsoSuolo(
        particellaRicercaVO, idAzienda);
  }

  // Metodo per recuperare l'elenco delle fotografie relative alle particelle
  // attraverso i parametri relativi alla particella e l'id dichiarazione di
  // consistenza
  public Vector<ParticellaVO> ricercaParticelleStoricizzateByParametriAndUtilizzo(
      ParticellaVO particellaRicercaVO) throws SolmrException, Exception
  {
    return fascicoloLocal
        .ricercaParticelleStoricizzateByParametriAndUtilizzo(particellaRicercaVO);
  }

  // Metodo per effettuare la ricerca delle particelle storicizzate a partire
  // dai parametri relativi alla
  // particella e ad uno speficifico utilizzo
  public Vector<ParticellaVO> ricercaParticelleStoricizzateByParametriAndUtilizzoSpecificato(
      ParticellaVO particellaRicercaVO) throws Exception, SolmrException
  {
    return fascicoloLocal
        .ricercaParticelleStoricizzateByParametriAndUtilizzoSpecificato(particellaRicercaVO);
  }

  // Metodo per recuperare l'elenco delle particelle storicizzate in relazione a
  // dei filtri di ricerca e all'assenza
  // di utilizzi o al fatto che la somma delle superfici utilizzate relative ad
  // una conduzione non
  // sia uguale alla superficie condotta
  public Vector<ParticellaVO> ricercaParticelleStoricizzateByParametriSenzaUsoSuolo(
      ParticellaVO particellaRicercaVO) throws SolmrException, Exception
  {
    return fascicoloLocal
        .ricercaParticelleStoricizzateByParametriSenzaUsoSuolo(particellaRicercaVO);
  }

  // Metodo per recuperare i tipi ruolo ad esclusione di
  // "TITOLARE/RAPPRESENTANTE LEGALE"
  public Vector<CodeDescription> getTipiRuoloNonTitolareAndNonSpecificato()
      throws Exception, SolmrException
  {
    return anagDenominazioniLocal.getTipiRuoloNonTitolareAndNonSpecificato();
  }

  // Metodo per effettuare la sostituzione del vecchio soggetto con il nuovo in
  // tutti i legami tra aziende
  // e persona fisiche
  public void changeLegameBetweenPersoneAndAziende(Long newIdSoggetto,
      Long oldIdSoggetto, Long idAzienda) throws SolmrException, Exception
  {
    anagAziendaLocal.changeLegameBetweenPersoneAndAziende(newIdSoggetto,
        oldIdSoggetto, idAzienda);
  }

  // Metodo per cessare il legame tra una persona e l'azienda
  public void cessaLegameBetweenPersonaAndAzienda(Long idSoggetto,
      Long idAzienda) throws SolmrException, Exception
  {
    anagAziendaLocal
        .cessaLegameBetweenPersonaAndAzienda(idSoggetto, idAzienda);
  }

  // Metodo per controllare se è possibile modificare un uso del suolo associato
  // ad un'azienda agricola
  public void checkUpdateSuperficie(Long idAzienda) throws SolmrException,
      Exception
  {
    fascicoloLocal.checkUpdateSuperficie(idAzienda);
  }

  // Metodo per recuperare la data dell'ultimo dichiarazione di consistenza
  public Date getMaxDataDichiarazioneConsistenza(Long idAzienda)
      throws SolmrException, Exception
  {
    return fascicoloLocal.getMaxDataDichiarazioneConsistenza(idAzienda);
  }


  // Metodo per recuperare il valore di tutte le superfici condotte attive
  // partendo dall 'id particella
  // quella della particella su cui si sta lavorando
  public String getTotaleSupCondotteByIdParticellaNotIncludeIdConduzioneParticella(
      Long idConduzioneParticella, Long idParticella) throws SolmrException,
      Exception
  {
    return fascicoloLocal
        .getTotaleSupCondotteByIdParticellaNotIncludeIdConduzioneParticella(
            idConduzioneParticella, idParticella);
  }

  // Metodo per recuperare l'elenco delle dichiarazioni di consistenza
  // attraverso l'id azienda
  public Vector<ParticellaVO> getElencoDichiarazioniConsistenzaByIdAzienda(Long idAzienda)
      throws SolmrException, Exception
  {
    return fascicoloLocal
        .getElencoDichiarazioniConsistenzaByIdAzienda(idAzienda);
  }

  // Metodo per recuperare il valore di tutte le superfici condotte relative ad
  // una dichiarazione di
  // consistenza di un'azienda agricola
  public String getTotaleSupCondotteDichiarateByAzienda(Long idAzienda,
      Long idDichiarazioneConsistenza) throws SolmrException, Exception
  {
    return fascicoloLocal.getTotaleSupCondotteDichiarateByAzienda(idAzienda,
        idDichiarazioneConsistenza);
  }

  // Metodo per verificare se, in presenza di particelle ad utilizzo vigneto,
  // sia possibile effettuare
  // l'eliminazione
  public void checkEliminaUtilizziVigneto(Vector<String> elencoIdUtilizziParticella)
      throws SolmrException, Exception
  {
    fascicoloLocal.checkEliminaUtilizziVigneto(elencoIdUtilizziParticella);
  }

  // Metodo per controllare le particelle selezionate sono già state inserite
  // all'interno di una
  // dichiarazione di consistenza
  public void checkParticellaLegataDichiarazioneConsistenza(
      Vector<Long> elencoParticelle) throws Exception, SolmrException
  {
    fascicoloLocal
        .checkParticellaLegataDichiarazioneConsistenza(elencoParticelle);
  }

  // Metodo per effettuare la cancellazione degli utilizzi dalla tabella
  // DB_UTILIZZO_PARTICELLA
  // partendo dall'id conduzione particella
  public void deleteUtilizzoParticellaByIdConduzioneParticella(
      Long idConduzioneParticella) throws Exception, SolmrException
  {
    fascicoloLocal
        .deleteUtilizzoParticellaByIdConduzioneParticella(idConduzioneParticella);
  }

  // Metodo per effettuare la cancellazione delle conduzioni dalla tabella
  // DB_CONDUZIONE_PARTICELLA
  public void deleteConduzioneParticella(Long idConduzioneParticella)
      throws SolmrException, Exception
  {
    fascicoloLocal.deleteConduzioneParticella(idConduzioneParticella);
  }

  // Metodo per effettuare l'eliminazione delle particelle selezionate da un
  // utente a partire
  // dall'id conduzione particella
  public void eliminaParticelle(Vector<Long> elencoConduzioni, Long idAzienda, RuoloUtenza ruoloUtenza) throws SolmrException,
      Exception
  {
    fascicoloLocal.eliminaParticelle(elencoConduzioni, idAzienda, ruoloUtenza);
  }

  // Metodo per effettuare la cessazione di un allevamento
  public void storicizzaAllevamento(AllevamentoAnagVO all,
      long idUtenteAggiornamento) throws SolmrException, Exception
  {
    allevamentoAnagLocal.storicizzaAllevamento(all, idUtenteAggiornamento);
  }

  // Metodo per effettuare la modifica di un record su DB_FABBRICATO
  public void cessaFabbricato(Long idFabbricato, long idUtenteAggiornamento)
      throws SolmrException, Exception
  {
    fascicoloLocal.cessaFabbricato(idFabbricato, idUtenteAggiornamento);
  }

  // Metodo per effettuare la cessazione di una manodopera
  public void storicizzaManodopera(ManodoperaVO manodoperaVO)
      throws SolmrException, Exception
  {
    manodoperaLocal.storicizzaManodopera(manodoperaVO);
  }
  
  public Vector<TipoIscrizioneINPSVO> getElencoTipoIscrizioneINPSAttivi()
      throws SolmrException, Exception
  {
    return manodoperaLocal.getElencoTipoIscrizioneINPSAttivi();
  }
  
  public ManodoperaVO findManodoperaAttivaByIdAzienda(long idAzienda)
      throws SolmrException, Exception
  {
    return manodoperaLocal.findManodoperaAttivaByIdAzienda(idAzienda);
  }
      

  // Metodo per effettuare la cessazione dell'unità produttiva selezionata
  public void cessazioneUTE(UteVO uteVO, long idUtenteAggiornamento)
      throws SolmrException, Exception
  {
    fascicoloLocal.cessazioneUTE(uteVO, idUtenteAggiornamento);
  }
  
  // Metodo per recuperare i dati della particella a partire dall'
  // id_conduzione_particella e la
  // superficie libera cioè senza uso del suolo specificato
  public ParticellaVO getParticellaVOByIdConduzioneParticellaSenzaUsoSuolo(
      Long idConduzioneParticella, String anno) throws SolmrException,
      Exception
  {
    return fascicoloLocal
        .getParticellaVOByIdConduzioneParticellaSenzaUsoSuolo(
            idConduzioneParticella, anno);
  }

  // Metodo per recuperare i dati territoriali della particella a partire
  // dall'id conduzione particella
  public ParticellaVO getDettaglioParticellaDatiTerritorialiByIdConduzioneParticella(
      Long idConduzioneParticella) throws SolmrException, Exception
  {
    return fascicoloLocal
        .getDettaglioParticellaDatiTerritorialiByIdConduzioneParticella(idConduzioneParticella);
  }

  // Metodo per ribaltare gli utilizzi da una conduzione particella ad un'altra
  public void allegaUtilizziToNewConduzioneParticella(
      Long newIdConduzioneParticella, Long oldIdConduzioneParticella)
      throws SolmrException, Exception
  {
    fascicoloLocal.allegaUtilizziToNewConduzioneParticella(
        newIdConduzioneParticella, oldIdConduzioneParticella);
  }

  /**
   * Questo metodo viene usato per storicizzare i dati di residenza
   * 
   */
  public Long storicizzaDatiResidenza(PersonaFisicaVO pfVO, long idUtenteAggiornamento)
      throws Exception, SolmrException
  {
    return anagAziendaLocal.storicizzaDatiResidenza(pfVO, idUtenteAggiornamento);
  }

  // Metodo per effettuare la modifica del ruolo, dei dati della persona e
  // storicizzazione della residenza
  public void updateDatiSoggettoAndStoricizzaResidenza(
      PersonaFisicaVO newPersonaFisicaVO, PersonaFisicaVO oldPersonaFisicaVO,
      long idUtenteAggiornamento) throws Exception, SolmrException
  {
    soggettiLocal.updateDatiSoggettoAndStoricizzaResidenza(newPersonaFisicaVO,
        oldPersonaFisicaVO, idUtenteAggiornamento);
  }

  // Metodo per effettuare la ricerca del terreno in relazione ai parametri di
  // ricerca
  public Vector<ParticellaVO> ricercaTerreniByParametri(
      ParticellaVO particellaRicercaTerrenoVO) throws SolmrException,
      Exception
  {
    return fascicoloLocal
        .ricercaTerreniByParametri(particellaRicercaTerrenoVO);
  }

  // Metodo verificare se esiste un contenzioso sulla particella selezionata,
  // cioè se sono presenti
  // delle conduzioni aperte a fronte di particelle selezionate su aziende
  // attive
  public boolean isParticellaContenziosoOnAzienda(Long idStoricoParticella)
      throws SolmrException, Exception
  {
    return fascicoloLocal
        .isParticellaContenziosoOnAzienda(idStoricoParticella);
  }

  // Metodo per recuperare la somma delle superfici condotte relativa ad una
  // particella
  public String getTotSupCondotteByIdStoricoParticella(Long idStoricoParticella)
      throws SolmrException, Exception
  {
    return fascicoloLocal
        .getTotSupCondotteByIdStoricoParticella(idStoricoParticella);
  }

  // Metodo per recuperare l'elenco delle azienda e delle conduzioni a partire
  // dall 'id storico
  // particella
  public Vector<ParticellaAziendaVO> getElencoAziendeAndConduzioniByIdStoricoParticella(
      Long idStoricoParticella, boolean attive) throws Exception,
      SolmrException
  {
    return fascicoloLocal.getElencoAziendeAndConduzioniByIdStoricoParticella(
        idStoricoParticella, attive);
  }

  // Metodo per recuperare l'elenco degli utilizzi relativi a conduzioni attive
  // in un anno
  // specificato
  public Vector<ParticellaUtilizzoVO> getElencoUtilizziAttiviByAnnoAndIdStoricoParticella(
      Long idStoricoParticella, String anno) throws Exception,
      SolmrException
  {
    return fascicoloLocal.getElencoUtilizziAttiviByAnnoAndIdStoricoParticella(
        idStoricoParticella, anno);
  }

  // Metodo per recuperare i valori della tabella DB_TIPO_TIPOLOGIA_NOTIFICA
  public Vector<CodeDescription> getTipiTipologiaNotifica() throws SolmrException,
      Exception
  {
    return anagDenominazioniLocal.getTipiTipologiaNotifica();
  }
  
  public Vector<CodeDescription> getTipologiaNotificaFromRuolo(RuoloUtenza ruoloUtenza) 
      throws SolmrException, Exception
  {
    return anagDenominazioniLocal.getTipologiaNotificaFromRuolo(ruoloUtenza);
  }
  
  public Vector<CodeDescription> getTipiTipologiaNotificaFromEntita(String tipoEntita)
      throws SolmrException, Exception
  {
    return anagDenominazioniLocal.getTipiTipologiaNotificaFromEntita(tipoEntita);
  }
  
  public Vector<TipoCategoriaNotificaVO> getTipiCategoriaNotificaFromRuolo(RuoloUtenza ruoloUtenza) 
    throws SolmrException, Exception
  {
    return anagDenominazioniLocal.getTipiCategoriaNotificaFromRuolo(ruoloUtenza);
  }
  
  public Vector<TipoCategoriaNotificaVO> getTipiCategoriaNotificaFromEntita(String tipoEntita)
    throws SolmrException, Exception
  {
    return anagDenominazioniLocal.getTipiCategoriaNotificaFromEntita(tipoEntita);
  }    
  
  public Vector<CodeDescription> getCategoriaNotifica() throws SolmrException, Exception
  {
    return anagDenominazioniLocal.getCategoriaNotifica();
  }

  // FINE NOTIFICHE

  // Metodo per effettuare la ricerca delle notifiche in relazione a dei
  // parametri di ricerca
  public ElencoNotificheVO ricercaNotificheByParametri(NotificaVO notificaVO,
      UtenteAbilitazioni utenteAbilitazioni, RuoloUtenza ruoloUtenza, Boolean storico, int maxRecord) throws Exception, SolmrException
  {
    return notificaLocal.ricercaNotificheByParametri(notificaVO, utenteAbilitazioni, ruoloUtenza, storico, maxRecord);
  }

  // Metodo per effettuare la ricerca della notifica a partire dalla chiave
  // primaria
  public NotificaVO findNotificaByPrimaryKey(Long idNotifica, String provenienza)
      throws SolmrException, Exception
  {
    return notificaLocal.findNotificaByPrimaryKey(idNotifica, provenienza);
  }

  // Metodo per recuperare l'elenco delle notifiche in relazione ad un'azienda
  // agricola e ad
  // una situazione(attuale o storica) relative ad un procedimento specificato
  public Vector<NotificaVO> getElencoNotificheByIdAzienda(NotificaVO notificaVO,
      Boolean storico, String ordinamento) throws Exception,
      SolmrException
  {
    return notificaLocal.getElencoNotificheByIdAzienda(notificaVO, storico,
        ordinamento);
  }
  
  public Vector<NotificaVO> getElencoNotifichePopUp(NotificaVO notificaVO) 
      throws Exception
  {
    return notificaLocal.getElencoNotifichePopUp(notificaVO);
  }

  // Metodo per effettuare l'inserimento di una notifica
  public Long insertNotifica(NotificaVO notificaVO, long idUtenteAggiornamento)
      throws Exception, SolmrException
  {
    return notificaLocal.insertNotifica(notificaVO, idUtenteAggiornamento);
  }
  
  public void updateNotifica(NotificaVO notificaVO, long idUtenteAggiornamento)
    throws Exception, SolmrException
  {
    notificaLocal.updateNotifica(notificaVO, idUtenteAggiornamento);
  }
     

  // Metodo per effettuare la chiusura di una notifica
  public void closeNotifica(NotificaVO notificaVO, long idUtenteAggiornamento)
      throws Exception, SolmrException
  {
    notificaLocal.closeNotifica(notificaVO, idUtenteAggiornamento);
  }
  
  public Long getIdTipologiaNotificaFromCategoria(Long idCategoriaNotifica)
      throws Exception
  {
    return notificaLocal.getIdTipologiaNotificaFromCategoria(idCategoriaNotifica);
  }
  
  public boolean isChiusuraNotificaRuoloPossibile(RuoloUtenza ruoloUtenza, long idCategoriaNotifica)
      throws Exception
  {
    return notificaLocal.isChiusuraNotificaRuoloPossibile(ruoloUtenza, idCategoriaNotifica);
  }
  
  public Vector<NotificaVO> getElencoNotificheForIdentificato(long identificativo, 
      String codiceTipo, long idAzienda, Long idDichiarazioneConsistenza) 
    throws Exception
  {
    return notificaLocal.getElencoNotificheForIdentificato(identificativo, codiceTipo, 
        idAzienda, idDichiarazioneConsistenza);
  }
  
  public boolean isModificaNotificaRuoloPossibile(RuoloUtenza ruoloUtenza, long idCategoriaNotifica)
      throws Exception
  {
    return notificaLocal.isModificaNotificaRuoloPossibile(ruoloUtenza, idCategoriaNotifica);
  }
  
  public HashMap<Long,Vector<NotificaEntitaVO>> getNotificheEntitaUvFromIdNotifica(
      long ids[]) throws Exception
  {
    return notificaLocal.getNotificheEntitaUvFromIdNotifica(ids);
  }
  
  public HashMap<Long,Vector<NotificaEntitaVO>> getNotificheEntitaParticellaFromIdNotifica(
      long ids[]) throws Exception
  {
    return notificaLocal.getNotificheEntitaParticellaFromIdNotifica(ids);
  }

  // FINE NOTIFICHE

  // INIZIO SEZIONE AZIENDA AGRICOLA

  // Metodo per verificare lo stato dell'azienda in relazione alle dichiarazioni
  // di consistenza
  // e alle notifiche
  public void checkStatoAzienda(Long idAzienda) throws SolmrException,
      Exception
  {
    anagAziendaLocal.checkStatoAzienda(idAzienda);
  }

  /**
   * Metodo che mi restituisce la MIN relativa alla data inizio validita di
   * un'azienda
   * 
   * @param idAzienda
   * @return java.util.Date
   * @throws Exception
   */
  public Date getMinDataInizioValiditaAnagraficaAzienda(Long idAzienda)
      throws Exception
  {
    return anagAziendaLocal
        .getMinDataInizioValiditaAnagraficaAzienda(idAzienda);
  }

  /**
   * Metodo utilizzato per effettuare la modifica del record su DB_AZIENDA in
   * funzione dell'OPR delegato al fascicolo
   * 
   * @param anagAziendaVO
   * @throws Exception
   */
  public void modificaGestoreFascicolo(AnagAziendaVO anagAziendaVO)
      throws Exception
  {
    anagAziendaLocal.modificaGestoreFascicolo(anagAziendaVO);
  }

  /**
   * Metodo che mi restituisce la data max di fine mandato
   * 
   * @param idAzienda
   * @return java.util.Date maxDataFineMandato
   * @throws Exception
   */
  public java.util.Date getDataMaxFineMandato(Long idAzienda)
      throws Exception
  {
    return anagAziendaLocal.getDataMaxFineMandato(idAzienda);
  }

  /**
   * Metodo che si occupa di registrare il mandato ed eventualmente di inserire
   * e protocollare il documento
   * 
   * @param anagAziendaVO
   * @param ruoloUtenza
   * @param delegaVO
   * @param documentoVO
   * @return
   * @throws Exception
   * @throws SolmrException
   */
  public Long insertDelegaForMandato(AnagAziendaVO anagAziendaVO,
      RuoloUtenza ruoloUtenza, DelegaVO delegaVO, DocumentoVO documentoVO)
      throws Exception, SolmrException
  {
    return anagAziendaLocal.insertDelegaForMandato(anagAziendaVO, ruoloUtenza,
        delegaVO, documentoVO);
  }

  /**
   * Metodo che mi restituisce la delega di un dato procedimento relativo ad una
   * determinata azienda agricola
   * 
   * @param idAzienda
   * @param idProcedimento
   * @return it.csi.solmr.dto.anag.DelegaVO
   * @throws Exception
   */
  public DelegaVO getDelegaByAziendaAndIdProcedimento(Long idAzienda,
      Long idProcedimento) throws Exception
  {
    return anagAziendaLocal.getDelegaByAziendaAndIdProcedimento(idAzienda,
        idProcedimento);
  }

  /**
   * Metodo che mi restituisce la data max di inizio mandato
   * 
   * @param idAzienda
   * @return java.util.Date maxDataFineMandato
   * @throws Exception
   */
  public java.util.Date getDataMaxInizioMandato(Long idAzienda)
      throws Exception
  {
    return anagAziendaLocal.getDataMaxInizioMandato(idAzienda);
  }

  /**
   * Metodo che mi restituisce lo storico delle deleghe di un dato procedimento
   * relativo ad una determinata azienda agricola
   * 
   * @param idAzienda
   * @param idProcedimento
   * @param orderBy[]
   * @return it.csi.solmr.dto.anag.DelegaVO[]
   * @throws Exception
   */
  public DelegaVO[] getStoricoDelegheByAziendaAndIdProcedimento(Long idAzienda,
      Long idProcedimento, String[] orderBy) throws Exception
  {
    return anagAziendaLocal.getStoricoDelegheByAziendaAndIdProcedimento(
        idAzienda, idProcedimento, orderBy);
  }

  // FINE SEZIONE AZIENDA AGRICOLA

  // INIZIO PROFILAZIONE

  // Metodo per controllare se l'utente intermediario che si è loggato possiede
  // una delega diretta
  // o tramite id_intermediario padre
  public DelegaVO intermediarioConDelega(UtenteAbilitazioni utenteAbilitazioni, Long idAzienda)
      throws Exception, SolmrException
  {
    return anagAziendaLocal.intermediarioConDelega(utenteAbilitazioni, idAzienda);
  }

  public boolean isIntermediarioConDelegaDiretta(long idIntermediario,
      Long idAzienda) throws Exception
  {
    return anagAziendaLocal
        .isIntermediarioConDelegaDiretta(idIntermediario, idAzienda);
  }

  public boolean isIntermediarioPadre(long idIntermediario, Long idAzienda)
      throws Exception
  {
    return anagAziendaLocal.isIntermediarioPadre(idIntermediario, idAzienda);
  }

  // FINE PROFILAZIONE

  // SIAN
  // Metodo per recuperare le decodifiche dei codici SIAN
  public String getDescriptionSIANFromCode(String tableName, String code)
      throws SolmrException, Exception
  {
    return anagDenominazioniLocal.getDescriptionSIANFromCode(tableName, code);
  }

  // SIAN

  // INIZIO AZIENDA AGRICOLA

  // Metodo che effettua l'aggiornamento dei dati dell'anagrafe in funzione
  // dell'importazione
  // di quelli dell'anagrafe tributaria
  public void updateAnagrafe(AnagAziendaVO anagAziendaVO,
      long idUtenteAggiornamento, PersonaFisicaVO pfVO, boolean isCuaaChanged, 
      PersonaFisicaVO pfVOTributaria, Vector<Long> vIdAtecoTrib) throws Exception
  {
    anagAziendaLocal.updateAnagrafe(anagAziendaVO, idUtenteAggiornamento, pfVO, isCuaaChanged, pfVOTributaria, vIdAtecoTrib);
  }
  
  public void updateAnagrafeSemplice(AnagAziendaVO anagAziendaVO, long idUtenteAggiornamento) 
      throws Exception
  {
    anagAziendaLocal.updateAnagrafeSemplice(anagAziendaVO, idUtenteAggiornamento);
  }

  // FINE AZIENDA AGRICOLA

  /**
   * Inserisce un nuovo conto corrente
   * 
   */
  public void insertContoCorrente(ContoCorrenteVO conto,
      long idUtenteAggiornamento) throws Exception, SolmrException
  {
    contoCorrenteLocal.insertContoCorrente(conto, idUtenteAggiornamento);
  }

  /**
   * Carica le informazioni su un conto corrente
   * 
   * @param idContoCorrente
   *          String
   * @return it.csi.solmr.dto.anag.ContoCorrenteVO
   * @throws Exception
   * @throws SolmrException
   */
  public ContoCorrenteVO getContoCorrente(String idContoCorrente)
      throws Exception
  {
    return contoCorrenteLocal.getContoCorrente(idContoCorrente);
  }
  
  public void storicizzaContoCorrente(ContoCorrenteVO conto, Long idUtente) 
    throws Exception, SolmrException
  {
    contoCorrenteLocal.storicizzaContoCorrente(conto, idUtente);
  }

  /* Gestione Allevamenti */
  public Vector<AllevamentoAnagVO> getAllevamentiByIdUTE(Long idUTE, int anno)
      throws SolmrException, Exception
  {
    return allevamentoAnagLocal.getAllevamentiByIdUTE(idUTE, anno);
  }

  public Vector<Vector<AllevamentoAnagVO>> getAllevamentiByIdAzienda(Long idAzienda, int anno)
      throws SolmrException, Exception
  {
    return allevamentoAnagLocal.getAllevamentiByIdAzienda(idAzienda, anno);
  }

  public AllevamentoAnagVO getAllevamento(Long idAllevamento)
      throws SolmrException, Exception
  {
    AllevamentoAnagVO allevamentoAnagVO = allevamentoAnagLocal
        .getAllevamento(idAllevamento);

    TipoASLAnagVO tipoASLVO = allevamentoAnagVO.getTipoASLAnagVO();
    if (tipoASLVO != null
        && Validator.isNotEmpty(tipoASLVO.getExtIdAmmCompetenza()))
    {
      tipoASLVO.setDescrizione((smrCommLocal
          .serviceFindAmmCompetenzaById(tipoASLVO.getExtIdAmmCompetenza()))
          .getDescrizione());
      allevamentoAnagVO.setTipoASLAnagVO(tipoASLVO);
    }
    return allevamentoAnagVO;
  }

  public Vector<CategorieAllevamentoAnagVO> getCategorieAllevamento(Long idAllevamento)
      throws SolmrException, Exception
  {
    return allevamentoAnagLocal.getCategorieAllevamento(idAllevamento);
  }

  public Long insertAllevamento(AllevamentoAnagVO allevamentoVO,
      long idUtenteAggiornamento) throws SolmrException, Exception
  {
    return allevamentoAnagLocal.insertAllevamento(allevamentoVO,
        idUtenteAggiornamento, true);
  }

  public Vector<UteVO> getElencoIdUTEByIdAzienda(Long idAzienda)
      throws SolmrException, Exception
  {
    return allevamentoAnagLocal.getElencoIdUTEByIdAzienda(idAzienda);
  }

  public Vector<TipoASLAnagVO> getTipiASL() throws SolmrException, Exception
  {
    Vector<TipoASLAnagVO> tipoASLAnagVOs = allevamentoAnagLocal.getTipiASL();
    int size = tipoASLAnagVOs.size();
    Vector<String> idAmmCompetenza = new Vector<String>();
    for (int i = 0; i < size; i++)
    {
      TipoASLAnagVO tipoASLAnagVO = (TipoASLAnagVO) tipoASLAnagVOs.get(i);
      if (tipoASLAnagVO.getExtIdAmmCompetenza() != null)
        idAmmCompetenza.add(tipoASLAnagVO.getExtIdAmmCompetenza().toString());
    }
    AmmCompetenzaVO[] ammCompetenzaVO = smrCommLocal
        .serviceFindAmmCompetenzaByIdRange((String[]) idAmmCompetenza
            .toArray(new String[0]));
    if (ammCompetenzaVO == null || ammCompetenzaVO.length == 0)
      return new Vector<TipoASLAnagVO>();
    HashMap<Long,AmmCompetenzaVO> map = new HashMap<Long,AmmCompetenzaVO>();
    for (int i = 0; i < ammCompetenzaVO.length; i++)
      map.put(ammCompetenzaVO[i].getIdAmmCompetenzaLong(), ammCompetenzaVO[i]);
    TreeMap<String,TipoASLAnagVO> ordinato = new TreeMap<String,TipoASLAnagVO>();
    for (int i = 0; i < size; i++)
    {

      try
      {
        TipoASLAnagVO tipoASLAnagVO = (TipoASLAnagVO) tipoASLAnagVOs.get(i);

        tipoASLAnagVO.setDescrizione(((AmmCompetenzaVO) map.get(tipoASLAnagVO
            .getExtIdAmmCompetenza())).getDescrizione());

        ordinato.put(tipoASLAnagVO.getDescrizione(), tipoASLAnagVO);
      }
      catch (Exception e)
      {
      }
    }
    return new Vector<TipoASLAnagVO>(ordinato.values());
  }

  public Vector<TipoSpecieAnimaleAnagVO> getTipiSpecieAnimale() throws SolmrException, Exception
  {
    return allevamentoAnagLocal.getTipiSpecieAnimale();
  }

  public Vector<TipoSpecieAnimaleAnagVO> getTipiSpecieAnimaleAzProv() throws SolmrException,
      Exception
  {
    return allevamentoAnagLocal.getTipiSpecieAnimaleAzProv();
  }

  public void deleteAllevamentoAll(Long idAllevamento) throws SolmrException,
      Exception
  {
    allevamentoAnagLocal.deleteAllevamentoAll(idAllevamento, true);
  }

  public Vector<TipoCategoriaAnimaleAnagVO> getCategorieByIdSpecie(Long idSpecie) throws SolmrException,
      Exception
  {
    return allevamentoAnagLocal.getCategorieByIdSpecie(idSpecie);
  }

  public void updateAllevamento(AllevamentoAnagVO all,
      long idUtenteAggiornamento) throws SolmrException, Exception
  {
    allevamentoAnagLocal.updateAllevamento(all, idUtenteAggiornamento);
  }

  public Integer[] getAnniByIdAzienda(Long idAzienda) throws SolmrException,
      Exception
  {
    return allevamentoAnagLocal.getAnniByIdAzienda(idAzienda);
  }

  public Vector<CodeDescription> getTipoTipoProduzione(long idSpecie) throws SolmrException, Exception
  {
    return allevamentoAnagLocal.getTipoTipoProduzione(idSpecie);
  }

  public Vector<CodeDescription> getOrientamentoProduttivo(long idSpecie, long idTipoProduzione)
      throws SolmrException, Exception
  {
    return allevamentoAnagLocal.getOrientamentoProduttivo(idSpecie,
        idTipoProduzione);
  }
  
  public Vector<CodeDescription> getTipoProduzioneCosman(long idSpecie, long idTipoProduzione, long idOrientamentoProduttivo) 
      throws SolmrException, Exception
  {
    return allevamentoAnagLocal.getTipoProduzioneCosman(idSpecie, 
        idTipoProduzione, idOrientamentoProduttivo);
  }
  
  public Vector<CodeDescription> getSottocategorieCosman(long idSpecie, 
      long idTipoProduzione, long idOrientamentoProduttivo, long idTipoProduzioneCosman, String flagEsiste)
      throws SolmrException, Exception
  {
    return allevamentoAnagLocal.getSottocategorieCosman(idSpecie, idTipoProduzione, idOrientamentoProduttivo, idTipoProduzioneCosman, flagEsiste);
  }

  // Metodo che mi restituisce un elenco di allevamenti secondo un ordine
  // stabilito
  // (Metodo introdotto per correggere baco di ordinamento non risolvibile con
  // la struttura creata
  // in partenza) Mauro Vocale 24/01/2005
  public Vector<AllevamentoAnagVO> getAllevamentiByIdAziendaOrdinati(Long idAzienda, int anno)
      throws SolmrException, Exception
  {
    return allevamentoAnagLocal.getAllevamentiByIdAziendaOrdinati(idAzienda,
        anno);
  }

  // Metodo che mi restituisce i dati relativi al tipo specie animale a partire
  // dall'id
  public TipoSpecieAnimaleAnagVO getTipoSpecieAnimale(Long idSpecieAnimale)
      throws Exception
  {
    return allevamentoAnagLocal.getTipoSpecieAnimale(idSpecieAnimale);
  }

  /**
   * Metodo per verificare se il record allevamento che restituisce il SIAN è
   * già stato censito in anagrafe
   * 
   * @param anagAziendaVO
   *          AnagAziendaVO
   * @param sianAllevamentiVO
   *          SianAllevamentiVO
   * @return boolean
   * @throws Exception
   */
  public boolean isRecordSianInAnagrafe(AnagAziendaVO anagAziendaVO,
      SianAllevamentiVO sianAllevamentiVO) throws Exception
  {
    return allevamentoAnagLocal.isRecordSianInAnagrafe(anagAziendaVO,
        sianAllevamentiVO);
  }

  /**
   * Metodo per recuperare l'istat della provincia a partire dalla sigla
   * 
   * @param siglaProvincia
   *          String
   * @return String
   * @throws Exception
   */
  public String getIstatProvinciaBySiglaProvincia(String siglaProvincia)
      throws Exception
  {
    return anagDenominazioniLocal
        .getIstatProvinciaBySiglaProvincia(siglaProvincia);
  }
  
  public String getRegioneByProvincia(String siglaProvincia) throws Exception
  {
    return anagDenominazioniLocal
        .getRegioneByProvincia(siglaProvincia);
  }

  /**
   * Metodo per recuperare il tipo asl a partire dall'id_amm_competenza
   * 
   * @param idAmmCompetenza
   *          Long
   * @param isActive
   *          boolean
   * @return TipoASLAnagVO
   * @throws Exception
   * @throws SolmrException
   */
  public TipoASLAnagVO getTipoASLAnagVOByExtIdAmmCompetenza(
      Long idAmmCompetenza, boolean isActive) throws Exception,
      SolmrException
  {
    return allevamentoAnagLocal.getTipoASLAnagVOByExtIdAmmCompetenza(
        idAmmCompetenza, isActive);
  }

  /**
   * Metodo che mi permette di recuperare l'elenco degli allevamenti di
   * un'azienda agricola relativi ad un determinato piano di riferimento
   * 
   * @param idAzienda
   * @param idPianoRiferimento
   * @param orderBy
   * @return it.csi.solmr.dto.anag.AllevamentoAnagVO[]
   * @throws Exception
   */
  public AllevamentoAnagVO[] getListAllevamentiAziendaByPianoRifererimento(
      Long idAzienda, Long idPianoRiferimento, Long idUte, String[] orderBy)
      throws Exception
  {
    return allevamentoAnagLocal.getListAllevamentiAziendaByPianoRifererimento(
        idAzienda, idPianoRiferimento, idUte, orderBy);
  }
  
  public TipoCategoriaAnimaleAnagVO getTipoCategoriaAnimale(Long idCategoriaAnimale)
      throws Exception
  {
    return allevamentoAnagLocal.getTipoCategoriaAnimale(idCategoriaAnimale);
  }

  /* Gestione Allevamenti */

  /**
   * Elenco Manodopera
   * 
   * @param manodoperaVO
   *          ManodoperaVO
   * @return Vector
   * @throws SolmrException
   * @throws Exception
   */
  public Vector<FrmManodoperaVO> getManodoperaAnnua(ManodoperaVO manodoperaVO)
      throws SolmrException, Exception
  {
    return manodoperaLocal.getManodoperaAnnua(manodoperaVO);
  }

  public Vector<FrmManodoperaVO> getManodoperaByPianoRifererimento(ManodoperaVO manodoperaVO,
      Long idPianoRiferimento) throws SolmrException, Exception
  {
    return manodoperaLocal.getManodoperaByPianoRifererimento(manodoperaVO,
        idPianoRiferimento);
  }

  /**
   * Dettaglio Manodopera
   * 
   * @param idManodopera
   *          Long
   * @return ManodoperaVO
   * @throws SolmrException
   * @throws Exception
   */
  public ManodoperaVO dettaglioManodopera(Long idManodopera)
      throws SolmrException, Exception
  {
    return manodoperaLocal.dettaglioManodopera(idManodopera);
  }

  /**
   * Cancellazione di tutti i dati relativi alla manodopera
   * 
   * @param idManodopera
   *          Long
   * @throws SolmrException
   * @throws Exception
   */
  public void deleteManodopera(Long idManodopera) throws SolmrException,
      Exception
  {
    manodoperaLocal.deleteManodopera(idManodopera);
  }

  /**
   * Inserimento di tutti i dati relativi alla manodopera
   * 
   * @param manodoperaVO
   *          ManodoperaVO
   * @param idAzienda
   *          Long
   * @throws SolmrException
   * @throws Exception
   */
  public void insertManodopera(ManodoperaVO manodoperaVO, Long idAzienda)
      throws SolmrException, Exception
  {
    manodoperaLocal.insertManodopera(manodoperaVO, idAzienda);
  }

  /**
   * Tipo Forma Conduzione
   * 
   * @return Vector
   * @throws SolmrException
   * @throws Exception
   */
  public Vector<CodeDescription> getTipoFormaConduzione() throws SolmrException, Exception
  {
    return anagDenominazioniLocal.getTipoFormaConduzione();
  }

  /**
   * Tipo Attivita Complementari
   * 
   * @return Vector
   * @throws SolmrException
   * @throws Exception
   */
  public Vector<CodeDescription> getTipoAttivitaComplementari() throws SolmrException,
      Exception
  {
    return anagDenominazioniLocal.getTipoAttivitaComplementari();
  }

  /**
   * Tipo Classi Manodopera
   * 
   * @return Vector
   * @throws SolmrException
   * @throws Exception
   */
  public Vector<CodeDescription> getTipoClassiManodopera() throws SolmrException,
      Exception
  {
    return anagDenominazioniLocal.getTipoClassiManodopera();
  }

  /**
   * Ultima dichiarazione di manodopera non valida
   * 
   * @return ManodoperaVO
   * @throws SolmrException
   * @throws Exception
   * @param idAzienda
   *          Long
   */
  public ManodoperaVO findLastManodopera(Long idAzienda) throws SolmrException,
      Exception
  {
    return manodoperaLocal.findLastManodopera(idAzienda);
  }

  /**
   * Controllo esistenza manodopera valida
   * 
   * @param idManodopera
   *          Long
   * @param idAzienda
   *          Long
   * @return boolean
   * @throws SolmrException
   * @throws Exception
   */
  public String isManodoperaValida(Long idManodopera, Long idAzienda)
      throws SolmrException, Exception
  {
    return manodoperaLocal.isManodoperaValida(idManodopera, idAzienda);
  }

  /**
   * Modifica di tutti i dati relativi alla manodopera
   * 
   * @param manodoperaVO
   *          ManodoperaVO
   * @param idAzienda
   *          Long
   * @throws SolmrException
   * @throws Exception
   */
  public void updateManodopera(ManodoperaVO manodoperaVO, Long idAzienda)
      throws SolmrException, Exception
  {
    manodoperaLocal.updateManodopera(manodoperaVO, idAzienda);
  }
  
  public void updateManodoperaSian(ManodoperaVO manodoperaChgVO, long idAzienda, long idUtente)
      throws SolmrException, Exception
  {
    manodoperaLocal.updateManodoperaSian(manodoperaChgVO, idAzienda, idUtente);
  }

  public Vector<ParticellaVO> getElencoParticelleForAziendaAndUtilizzo(Long idAzienda,
      String anno) throws SolmrException, Exception
  {
    return fascicoloLocal.getElencoParticelleForAziendaAndUtilizzo(idAzienda,
        anno);
  }

  /** *********************************************************************** */
  /** *************************** Consistenza ******************************* */
  /**
   * 
   * @param idAzienda
   *          Long
   * @throws Exception
   * @return boolean
   */
  public boolean previsioneAnnoSucessivo(Long idAzienda) throws Exception
  {
    return consistenzaLocal.previsioneAnnoSucessivo(idAzienda);
  }

  /**
   * Metodo che dato una tipologia di azienda mi dice se devo effettuare i
   * controlli sull'univocità della partita IVA / CUAA o no
   * 
   * @param idTipoAzienda
   *          Integer
   * @throws Exception
   * @throws SolmrException
   * @return boolean
   */
  public boolean isFlagUnivocitaAzienda(Integer idTipoAzienda)
      throws Exception, SolmrException
  {
    return anagAziendaLocal.isFlagUnivocitaAzienda(idTipoAzienda);
  }

  public boolean controlloUltimeModifiche(Long idAzienda, Integer anno)
      throws Exception
  {
    return consistenzaLocal.controlloUltimeModifiche(idAzienda, anno);
  }

  public String controlliDichiarazionePLSQL(Long idAzienda, Integer anno,
      Long idMotivoDichiarazione, Long idUtente) throws Exception, SolmrException
  {
    return consistenzaLocal.controlliDichiarazionePLSQL(idAzienda, anno,
        idMotivoDichiarazione, idUtente);
  }

  public void controlliParticellarePLSQL(Long idAzienda, Integer anno, Long idUtente)
      throws Exception, SolmrException
  {
    consistenzaLocal.controlliParticellarePLSQL(idAzienda, anno, idUtente);
  }

  public String controlliVerificaPLSQL(Long idAzienda, Integer anno,
      Integer idGruppoControllo, Long idUtente) throws Exception, SolmrException
  {
    return consistenzaLocal.controlliVerificaPLSQL(idAzienda, anno,
        idGruppoControllo, idUtente);
  }

  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichiarazioneConsistenza(Long idAzienda,
      Long idMotivoDichiarazione) throws Exception
  {
    return consistenzaLocal.getErroriAnomalieDichiarazioneConsistenza(
        idAzienda, (Long) SolmrConstants.get("FASE_DICHIARAZIONE"),
        idMotivoDichiarazione);
  }
  
  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichConsPG(long idDichiarazioneConsistenza,
      long fase) throws Exception
  {
    return consistenzaLocal.getErroriAnomalieDichConsPG(idDichiarazioneConsistenza, fase);
  }

  // Metodo per recuperare il dettaglio di un'anomalia
  public ErrAnomaliaDicConsistenzaVO getAnomaliaDichiarazioneConsistenza(
      Long idDichiarazioneSegnalazione) throws Exception
  {
    return consistenzaLocal
        .getAnomaliaDichiarazioneConsistenza(idDichiarazioneSegnalazione);
  }

  public Vector<ErrAnomaliaDicConsistenzaVO> getAnomaliePerCorrezione(
      Long elencoIdDichiarazioneSegnalazione[], long idMotivoDichiarazione)
      throws Exception
  {
    return consistenzaLocal.getAnomaliePerCorrezione(
        elencoIdDichiarazioneSegnalazione, idMotivoDichiarazione);
  }

  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichConsistenzaTerreni(Long idAzienda)
      throws Exception
  {
    return consistenzaLocal.getErroriAnomalieDichConsistenzaTerreni(idAzienda);
  }

  public Long salvataggioDichiarazionePLQSL(ConsistenzaVO consistenzaVO,
      Long idAzienda, Integer anno, RuoloUtenza ruoloUtenza)
      throws Exception, SolmrException
  {
    return consistenzaLocal.salvataggioDichiarazionePLQSL(consistenzaVO, idAzienda,
        anno, ruoloUtenza);
  }

  public Vector<ConsistenzaVO> getDichiarazioniConsistenza(Long idAzienda)
      throws Exception
  {
    return consistenzaLocal.getDichiarazioniConsistenza(idAzienda);
  }

  public Vector<ConsistenzaVO> getDichiarazioniConsistenzaMinimo(Long idAzienda)
      throws Exception
  {
    return consistenzaLocal.getDichiarazioniConsistenzaMinimo(idAzienda);
  }

  public ConsistenzaVO getDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza) throws Exception
  {
    return consistenzaLocal
        .getDichiarazioneConsistenza(idDichiarazioneConsistenza);
  }

  public Vector<TemporaneaPraticaAziendaVO> aggiornaPraticaAziendaPLQSL(Long idAzienda, Long idUtente,
      Long idDichiarazioneConsistenza) throws Exception, SolmrException
  {
    return consistenzaLocal.aggiornaPraticaAziendaPLQSL(idAzienda, idUtente,
        idDichiarazioneConsistenza);
  }
  
  public void aggiornaPraticaAziendaPLQSL(Long idAzienda) 
      throws Exception, SolmrException
  {
    consistenzaLocal.aggiornaPraticaAziendaPLQSL(idAzienda);
  }

  public boolean deleteDichConsAmmessa(Long idAzienda, Long idUtente,
      Long idDichiarazioneConsistenza) throws Exception, SolmrException
  {
    return consistenzaLocal.deleteDichConsAmmessa(idAzienda, idUtente,
        idDichiarazioneConsistenza);
  }

  public boolean newDichConsAmmessa(Long idAzienda, Long idUtente,
      Long idMotivoDichiarazione) throws Exception, SolmrException
  {
    return consistenzaLocal.newDichConsAmmessa(idAzienda, idUtente,
        idMotivoDichiarazione);
  }

  public void deleteDichiarazioneConsistenza(Long idDichiarazioneConsistenza)
      throws Exception
  {
    try
    {
      Vector<AllegatoDichiarazioneVO> vAllegatoDichiarazioneVO = documentoGaaLocal
        .getElencoAllegatoDichiarazioneFromIdDichiarazione(idDichiarazioneConsistenza);
      
      if(vAllegatoDichiarazioneVO != null)
      {
        Vector<Long> vIdAllegato = new Vector<Long>();
        Vector<Long> vIdDocIndex = new Vector<Long>();
        Vector<Long> vIdAllegatoDichiarazione = new Vector<Long>();
        for(int i=0;i<vAllegatoDichiarazioneVO.size();i++)
        {
          if(Validator.isNotEmpty(vAllegatoDichiarazioneVO.get(i).getExtIdDocumentoIndex()))
          {
            if(!vIdDocIndex.contains(vAllegatoDichiarazioneVO.get(i).getExtIdDocumentoIndex()))
              vIdDocIndex.add(vAllegatoDichiarazioneVO.get(i).getExtIdDocumentoIndex());
          }
          
          if(!vIdAllegato.contains(vAllegatoDichiarazioneVO.get(i).getIdAllegato()))
            vIdAllegato.add(vAllegatoDichiarazioneVO.get(i).getIdAllegato());
          
          if(!vIdAllegatoDichiarazione.contains(vAllegatoDichiarazioneVO.get(i).getIdAllegatoDichiarazione()))
            vIdAllegatoDichiarazione.add(vAllegatoDichiarazioneVO.get(i).getIdAllegatoDichiarazione());
          
        }
        
        documentoGaaLocal.deleteAllegatoDichiarazioneFromVId(vIdAllegatoDichiarazione);
        documentoGaaLocal.deleteAllegatoFromVId(vIdAllegato);
        
        for(int i=0;i<vIdDocIndex.size();i++)
        {
          AgriWellEsitoVO agriWellEsitoVO = agriWellGaaLocal
              .agriwellServiceCancellaDoquiAgri(vIdDocIndex.get(i));
        
          if (Validator.isEmpty(agriWellEsitoVO) 
              || (Validator.isNotEmpty(agriWellEsitoVO)
                  && SolmrConstants.ESITO_AGRIWELL_OK != agriWellEsitoVO.getEsito().intValue()))
          {
            String messaggio = "Attenzione si è verificato un problema nell'aggiornamento su agriwell ";
            if (Validator.isNotEmpty(agriWellEsitoVO))
            {
              messaggio += "-" + agriWellEsitoVO.getMessaggio();
            }
            throw new SolmrException(messaggio);          
          }
        }
      }
      
      consistenzaLocal.deleteDichiarazioneConsistenza(idDichiarazioneConsistenza);
    }
    catch (SolmrException re)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(re.getMessage());
    }
    catch (Exception re)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(re.getMessage());
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
    return consistenzaLocal.getListDichiarazioneSegnalazioni(idAzienda,
        idDichiarazioneConsistenza, idStoricoParticella);
  }

  /**
   * Metodo che mi permette di recuperare l'elenco dei controlli effettuati e le
   * rispettive segnalazioni\correzioni associate
   * 
   * @param idDichiarazioneConsistenza
   * @param idFase
   * @param errAnomaliaDicConsistenzaVO
   * @param orderBy
   * @return it.csi.solmr.dto.anag.ErrAnomaliaDicConsistenzaVO[]
   * @throws Exception
   */
  public ErrAnomaliaDicConsistenzaVO[] getListAnomalieByIdDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza, Long idFase,
      ErrAnomaliaDicConsistenzaVO errAnomaliaDicConsistenzaRicercaVO,
      String[] orderBy) throws Exception
  {
    return consistenzaLocal.getListAnomalieByIdDichiarazioneConsistenza(
        idDichiarazioneConsistenza, idFase, errAnomaliaDicConsistenzaRicercaVO,
        orderBy);
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
    return consistenzaLocal.getListDichiarazioniConsistenzaByIdAzienda(
        idAzienda, orderBy);
  }
  
  public ConsistenzaVO[] getListDichiarazioniConsistenzaByIdAziendaVarCat(
      Long idAzienda, String[] orderBy) throws Exception
  {
    return consistenzaLocal.getListDichiarazioniConsistenzaByIdAziendaVarCat(
        idAzienda, orderBy);
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
    consistenzaLocal.ripristinaPianoRiferimento(idDichiarazioneConsistenza,
        idUtente);
  }

  
  public Vector<TipoMotivoDichiarazioneVO> getListTipoMotivoDichiarazione(long idAzienda) 
      throws Exception
  {
    return consistenzaLocal.getListTipoMotivoDichiarazione(idAzienda);
  }

  /**
   * Metodo che mi restituisce l'elenco delle segnalazioni relative all'azienda,
   * ad una determinata dichiarazione di consistenza di una determinata unità
   * arborea
   * 
   * @param idAzienda
   * @param idDichiarazioneConsistenza
   * @param idStoricoUnitaArborea
   * @param orderBy
   * @return java.util.Vector
   * @throws Exception
   */
  public Vector<DichiarazioneSegnalazioneVO> getListDichiarazioneSegnalazioniUnar(Long idAzienda,
      Long idDichiarazioneConsistenza, Long idStoricoUnitaArborea,
      String[] orderBy) throws Exception
  {
    return consistenzaLocal.getListDichiarazioneSegnalazioniUnar(idAzienda,
        idDichiarazioneConsistenza, idStoricoUnitaArborea, orderBy);
  }
  
  public Vector<DichiarazioneSegnalazioneVO> getListDichiarazioneSegnalazioniConsolidamentoUV(
      Long idAzienda) throws Exception
  {
    return consistenzaLocal.getListDichiarazioneSegnalazioniConsolidamentoUV(idAzienda);
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
    return consistenzaLocal
        .findDichiarazioneConsistenzaByPrimaryKey(idDichiarazioneConsistenza);
  }
  
  public FascicoloNazionaleVO getInfoRisultatiSianDichiarazioneConsistenza(
      long idDichiarazioneConsistenza) throws Exception
  {
    return consistenzaLocal
        .getInfoRisultatiSianDichiarazioneConsistenza(idDichiarazioneConsistenza);
  }
  
  public Vector<ConsistenzaVO> getListDichiarazioniPianoGrafico(Long idAzienda)
      throws Exception
  {
    return consistenzaLocal.getListDichiarazioniPianoGrafico(idAzienda);
  }
  
  public EsitoPianoGraficoVO getEsitoPianoGraficoFromAccesso(long idAccessoPianoGrafico)
      throws Exception
  {
    return consistenzaLocal.getEsitoPianoGraficoFromAccesso(idAccessoPianoGrafico);
  }
  
  public int preCaricamentoPianoGrafico(long idDichiarazioneConsistenza) throws Exception
  {
    return consistenzaLocal.preCaricamentoPianoGrafico(idDichiarazioneConsistenza);
  }
  
  public EsitoPianoGraficoVO getEsitoPianoGraficoFromPK(long idEsitoGrafico) throws Exception
  {
    return consistenzaLocal.getEsitoPianoGraficoFromPK(idEsitoGrafico);
  }
  
  public Long insertStatoIncorsoPG(long idAccessoPianoGrafico, long idUtente) 
      throws Exception
  {
    return consistenzaLocal.insertStatoIncorsoPG(idAccessoPianoGrafico, idUtente);
  }
  
  public PlSqlCodeDescription controlliValidazionePlSql(long idAzienda, int idFase, 
      long idUtente, long idDichiarazioneConsistenza) throws Exception
  {
    return consistenzaLocal.controlliValidazionePlSql(idAzienda, idFase, 
        idUtente, idDichiarazioneConsistenza);
  }
  
  /**
   * Metodo che si occupa di effettuare la protocollazione delle dichiarazioni
   * di consistenza
   * 
   * @param elencoIdDichiarazioniConsistenza
   * @param ruoloUtenza
   * @param anno
   * @throws Exception
   */
  public void protocollaDichiarazioniConsistenza(
      Long[] elencoIdDichiarazioniConsistenza, RuoloUtenza ruoloUtenza,
      String anno) throws Exception
  {
    try
    {
      // Ciclo le dichiarazioni selezionate dall'utente
      for (int i = 0; i < elencoIdDichiarazioniConsistenza.length; i++)
      {
        Long idDichiarazioneConsistenza = (Long) elencoIdDichiarazioniConsistenza[i];
        // Calcolo il numero di protocollo da associare alla nuova dichiarazione
        // di consistenza
        String numeroProtocollo = documentoLocal.mathNumeroProtocollo(
            ruoloUtenza, anno);
        // Recupero dal DB i dati dettaglio della dichiarazione di consistenza
        ConsistenzaVO consistenzaVO = consistenzaLocal
            .findDichiarazioneConsistenzaByPrimaryKey(idDichiarazioneConsistenza);
        // Setto data e numero protocollo
        consistenzaVO.setDataProtocollo(new java.util.Date(new Timestamp(System
            .currentTimeMillis()).getTime()));
        consistenzaVO.setNumeroProtocollo(numeroProtocollo);
        // Procedo con update della dichiarazione di consistenza
        consistenzaLocal.modificaDichiarazioneConsistenza(consistenzaVO);
        //cancello record attivo nelle stampe non più valido...
        if(consistenzaVO.getIdAllegatoDichiarazione() != null)
          documentoGaaLocal.storicizzaAllegatoDichiarazione(consistenzaVO.getIdAllegatoDichiarazione());
        //cancello i record attivi degli allegati obbligatori
        documentoGaaLocal.storicizzaAllegatiAutomaticiDichiarazione(idDichiarazioneConsistenza);
        
        
        
        Date parametroDataSTMPLivecycle = (Date)commonLocal.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_DATA_STMP_LIVECYCLE);
        if(consistenzaVO.getDataDichiarazione().after(parametroDataSTMPLivecycle))
        {
          //Salvataggio allegati!!
          //Validazione inserisco uno vuoto senza stampa
          AllegatoDocumentoVO allegatoDocumentoVO = new AllegatoDocumentoVO();
          allegatoDocumentoVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
          Date dataAttuale = new Date();
          allegatoDocumentoVO.setDataRagistrazione(dataAttuale);
          allegatoDocumentoVO.setDataUltimoAggiornamento(dataAttuale);          
          String nomeFile = "Validazione_"+consistenzaVO.getNumeroProtocollo()+".pdf";   
          allegatoDocumentoVO.setNomeFisico(nomeFile);
          allegatoDocumentoVO.setNomeLogico(nomeFile);
          allegatoDocumentoVO.setIdTipoAllegato(new Long(SolmrConstants.VALIDAZIONE_ALLEGATO));
          if(ruoloUtenza.isUtentePA())
            allegatoDocumentoVO.setIdTipoFirma(new Long(4));
          
          Long idAllegato = documentoGaaLocal.insertFileAllegatoNoFile(allegatoDocumentoVO);
          documentoGaaLocal.insertAllegatoDichiarazione(idDichiarazioneConsistenza, 
              idAllegato.longValue());
          
          
          
          //Salvataggio allegati!!
          //Verifica se esiste già una stampa valida memorizzata attiva per il tipo di allegato
          Vector<AllegatoDichiarazioneVO>  vAllAll = documentoGaaLocal.getElencoAllegatiDichiarazioneDefault(new Integer(consistenzaVO.getIdMotivo()));
          if(vAllAll != null)
          {
            for(int h=0;h<vAllAll.size();h++)
            {
              AllegatoDichiarazioneVO allegatoDichiarazioneVO = vAllAll.get(h);
              if(stampaGaaLocal.isInseribileAllegatoAuto(allegatoDichiarazioneVO.getQueryAbilitazione(),
                  consistenzaVO.getCodiceFotografiaTerreni()))
              {
                //inserisco uno vuoto senza stampa
                allegatoDocumentoVO = new AllegatoDocumentoVO();
                allegatoDocumentoVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
                
                allegatoDocumentoVO.setDataRagistrazione(dataAttuale);
                allegatoDocumentoVO.setDataUltimoAggiornamento(dataAttuale);
                nomeFile = allegatoDichiarazioneVO.getDescTipoAllegato().replace(" ", "");
                nomeFile += "_"+consistenzaVO.getNumeroProtocollo()+".pdf";          
                allegatoDocumentoVO.setNomeFisico(nomeFile);
                allegatoDocumentoVO.setNomeLogico(nomeFile);
                allegatoDocumentoVO.setIdTipoAllegato(new Long(allegatoDichiarazioneVO.getIdTipoAllegato()));
                if(ruoloUtenza.isUtentePA())
                  allegatoDocumentoVO.setIdTipoFirma(new Long(4));
                
                idAllegato = documentoGaaLocal.insertFileAllegatoNoFile(allegatoDocumentoVO);
                documentoGaaLocal.insertAllegatoDichiarazione(idDichiarazioneConsistenza, 
                    idAllegato.longValue());
              }
            }
          }
          
          
          consistenzaLocal.updateDichiarazioneConsistenzaRichiestaStampa(idDichiarazioneConsistenza);
        }
        
        
      }
    }
    catch (SolmrException re)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(re.getMessage());
    }
    catch (Exception re)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(re.getMessage());
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
    return consistenzaLocal
        .getListTipoControlloByIdDichiarazioneConsistenzaAndIdGruppoControllo(
            idDicharazioneConsistenza, idGruppoControllo, orderBy);
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
    return consistenzaLocal.getListTipoControlloByIdGruppoControllo(
        idGruppoControllo, orderBy);
  }
  
  public TipoControlloVO[] getListTipoControlloByIdGruppoControlloAttivi(Long idGruppoControllo, String orderBy[])
      throws Exception
  {
    return consistenzaLocal.getListTipoControlloByIdGruppoControlloAttivi(
        idGruppoControllo, orderBy);
  }
  
  public TipoControlloVO[] getListTipoControlloForAziendaByIdGruppoControllo(
      Long idGruppoControllo, Long idAzienda, String orderBy[]) throws Exception
  {
    return consistenzaLocal.getListTipoControlloForAziendaByIdGruppoControllo(
        idGruppoControllo, idAzienda, orderBy);
  }
  
  public TipoControlloVO[] getListTipoControlloForAziendaByIdGruppoControlloAndDichCons(
      Long idGruppoControllo, Long idDichiarazioneConsistenza, String orderBy[])
  throws Exception
  {
    return consistenzaLocal.getListTipoControlloForAziendaByIdGruppoControlloAndDichCons(
        idGruppoControllo, idDichiarazioneConsistenza, orderBy);
  }

  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieForControlliTerreni(Long idAzienda,
      Long idControllo, Vector<String> vTipoErrori, boolean flagOK, String ordinamento)
      throws Exception
  {
    return consistenzaLocal.getErroriAnomalieForControlliTerreni(idAzienda,
        idControllo, vTipoErrori, flagOK, ordinamento);
  }

  public String getLastAnnoCampagnaFromDichCons(long idAzienda)
      throws Exception
  {
    return consistenzaLocal.getLastAnnoCampagnaFromDichCons(idAzienda);
  }

  public TipoMotivoDichiarazioneVO findTipoMotivoDichiarazioneByPrimaryKey(
      Long idMotivoDichiarazione) throws Exception
  {
    return consistenzaLocal
        .findTipoMotivoDichiarazioneByPrimaryKey(idMotivoDichiarazione);
  }
  
  public String getLastDichConsNoCorrettiva(long idAzienda)
    throws Exception
  {
    return consistenzaLocal.getLastDichConsNoCorrettiva(idAzienda);
  }
  
  public ConsistenzaVO getUltimaDichConsNoCorrettiva(long idAzienda) throws Exception
  {
    return consistenzaLocal.getUltimaDichConsNoCorrettiva(idAzienda);
  }
  
  public Date getLastDateDichConsNoCorrettiva(long idAzienda) throws Exception
  {
    return consistenzaLocal.getLastDateDichConsNoCorrettiva(idAzienda);
  }
  
  public Long getLastIdDichConsProtocollata(long idAzienda) throws Exception
  {
    return consistenzaLocal.getLastIdDichConsProtocollata(idAzienda);
  }
  
  public void updateDichiarazioneConsistenzaRichiestaStampa(Long idDichiarazioneConsistenza)
      throws Exception
  {
    consistenzaLocal.updateDichiarazioneConsistenzaRichiestaStampa(idDichiarazioneConsistenza);
  }

  public ConsistenzaVO getUltimaDichiarazioneConsistenza(Long idAzienda)
      throws Exception
  {
    return consistenzaLocal.getUltimaDichiarazioneConsistenza(idAzienda);
  }

  /** *********************************************************************** */
  /** *************************** Consistenza ******************************* */
  /** *********************************************************************** */

  /** *********************************************************************** */
  /** ****************************** AAEP *********************************** */
  /**
   * 
   */
  public Long importaDatiAAEP(AnagAAEPAziendaVO anagAAEPAziendaVO,
      AnagAziendaVO anagAziendaVO, long idUtenteAggiornamento, boolean denominazione,
      boolean partitaIVA, boolean descrizioneAteco, boolean provinciaREA,
      boolean numeroREA, boolean annoIscrizione, boolean numeroRegistroImprese, boolean pec,
      boolean sedeLegale, boolean titolareRappresentante, boolean formaGiuridica,boolean sezione,
      boolean descrizioneAtecoSec, boolean dataIscrizioneREA, boolean dataCancellazioneREA, boolean dataIscrizioneRI)
      throws Exception, SolmrException
  {
    try
    {

      return anagAziendaLocal.importaDatiAAEP(anagAAEPAziendaVO,
          anagAziendaVO, idUtenteAggiornamento, denominazione, partitaIVA, descrizioneAteco,
          provinciaREA, numeroREA, annoIscrizione, numeroRegistroImprese, pec,
          sedeLegale, titolareRappresentante, formaGiuridica, sezione, descrizioneAtecoSec, 
          dataIscrizioneREA, dataCancellazioneREA, dataIscrizioneRI);
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(e.getMessage());
    }
  }

  /** *********************************************************************** */
  /** ****************************** AAEP *********************************** */
  /**
   * 
   */
  public boolean controllaRegistrazioneMandato(AnagAziendaVO aziendaVO,
      String codiceEnte, DelegaAnagrafeVO delegaAnagrafeVO)
      throws Exception, SolmrException
  {
    return anagAziendaLocal.controllaRegistrazioneMandato(aziendaVO, codiceEnte,
        delegaAnagrafeVO);
  }
  
  public boolean controllaRevocaMandato(AnagAziendaVO aziendaVO,
      RuoloUtenza ruoloUtenza, DelegaAnagrafeVO delegaAnagrafeVO)
      throws Exception, SolmrException
  {
    return anagAziendaLocal.controllaRevocaMandato(aziendaVO, ruoloUtenza,
        delegaAnagrafeVO);
  }

  public boolean controllaPresenzaDelega(AnagAziendaVO aziendaVO)
      throws Exception
  {
    return anagAziendaLocal.controllaPresenzaDelega(aziendaVO);
  }

  /** *************** */
  /** *************** */
  /** *** Stampe **** */
  /** *************** */

  public Vector<ParticellaVO> getElencoParticelleQuadroI1(Long idAzienda,
      Long codFotografia) throws Exception, SolmrException
  {
    return stampeLocal.getElencoParticelleQuadroI1(idAzienda, codFotografia);
  }

  public BigDecimal[] getTotSupQuadroI1CondottaAndAgronomica(Long idAzienda, Long codFotografia)
      throws Exception, SolmrException
  {
    return stampeLocal.getTotSupQuadroI1CondottaAndAgronomica(idAzienda, codFotografia);
  }
  
  public BigDecimal[] getTotSupQuadroI1CatastaleAndGrafica(Long idAzienda, Long codFotografia)
    throws Exception, SolmrException
  {
    return stampeLocal.getTotSupQuadroI1CatastaleAndGrafica(idAzienda, codFotografia);
  }

  public Long getCodFotTerreniQuadroI1(Long idDichiarazioneConsistenza)
      throws Exception, SolmrException
  {
    return stampeLocal.getCodFotTerreniQuadroI1(idDichiarazioneConsistenza);
  }

  public Vector<UteVO> getUteQuadroB(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException, Exception
  {
    return stampeLocal.getUteQuadroB(idAzienda, dataRiferimento);
  }

  public Vector<TipoFormaConduzioneVO> getFormeConduzioneQuadroD()
      throws SolmrException, Exception
  {
    return stampeLocal.getFormeConduzioneQuadroD();
  }

  public Long getFormaConduzioneQuadroD(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException, Exception
  {
    return stampeLocal.getFormaConduzioneQuadroD(idAzienda, dataRiferimento);
  }

  public Vector<CodeDescription> getAttivitaComplementariQuadroE(
      Long idAzienda, java.util.Date dataRiferimento) throws SolmrException,
      Exception
  {
    return stampeLocal.getAttivitaComplementariQuadroE(idAzienda,
        dataRiferimento);
  }

  public Long getIdManodoperaQuadroF(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException, Exception
  {
    return stampeLocal.getIdManodoperaQuadroF(idAzienda, dataRiferimento);
  }

  public Vector<Long> getAllevamentiQuadroG(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException, Exception
  {
    return stampeLocal.getAllevamentiQuadroG(idAzienda, dataRiferimento);
  }

  public Vector<BaseCodeDescription> getTerreniQuadroI4(Long idAzienda,
      Long codFotografia) throws SolmrException, Exception
  {
    return stampeLocal.getTerreniQuadroI4(idAzienda, codFotografia);
  }

  public Vector<BaseCodeDescription> getTerreniQuadroI5(Long idAzienda,
      java.util.Date dataRiferimento, Long codFotografia)
      throws SolmrException, Exception
  {
    return stampeLocal.getTerreniQuadroI5(idAzienda, dataRiferimento,
        codFotografia);
  }

  public Vector<ParticellaVO> getFabbricatiParticelle(Long idFabbricato,
      java.util.Date dataRiferimento) throws SolmrException, Exception
  {
    return stampeLocal.getFabbricatiParticelle(idFabbricato, dataRiferimento);
  }
  
  public Vector<FabbricatoVO> getFabbricati(
      Long idAzienda, java.util.Date dataRiferimento,boolean comunicazione10R) 
      throws SolmrException, Exception
  {
    return stampeLocal.getFabbricati(idAzienda, dataRiferimento, comunicazione10R);
  }

  public DelegaVO getIntermediarioPerDelega(Long idIntermediario)
      throws Exception, SolmrException
  {
    return anagAziendaLocal.getIntermediarioPerDelega(idIntermediario);
  }

  public IntermediarioAnagVO getIntermediarioAnagByIdIntermediario(
      long idIntermediario) throws Exception
  {
    return anagAziendaLocal
        .getIntermediarioAnagByIdIntermediario(idIntermediario);
  }
  
  public IntermediarioAnagVO findIntermediarioVOByCodiceEnte(String codEnte)
      throws Exception
  {
    return anagAziendaLocal.findIntermediarioVOByCodiceEnte(codEnte);
  }
  
  public IntermediarioAnagVO findIntermediarioVOByIdAzienda(long idAzienda)
      throws Exception
  {
    return anagAziendaLocal.findIntermediarioVOByIdAzienda(idAzienda);
  }
  
  public boolean isAziendaIntermediario(long idAzienda)
      throws Exception
  {
    return anagAziendaLocal.isAziendaIntermediario(idAzienda);
  }
  
  public Vector<IntermediarioAnagVO> findFigliIntermediarioVOById(long idIntemediario)
      throws Exception
  {
    return anagAziendaLocal.findFigliIntermediarioVOById(idIntemediario);
  }
  
  public Vector<IntermediarioAnagVO> findFigliIntermediarioAziendaVOById(long idIntemediario)
      throws Exception
  {
    return anagAziendaLocal.findFigliIntermediarioAziendaVOById(idIntemediario);
  }
      
  public Vector<ConsistenzaZootecnicaStampa> getAllevamentiQuadroC10R(
      Long idAzienda, java.util.Date dataRiferimento) throws SolmrException,
      Exception
  {
    return stampeLocal.getAllevamentiQuadroC10R(idAzienda, dataRiferimento);
  }

  public Vector<QuadroDTerreni> getTerreniQuadroD10R(Long idAzienda)
      throws SolmrException, Exception
  {
    return stampeLocal.getTerreniQuadroD10R(idAzienda);
  }

  public Vector<QuadroDTerreni> getTerreniQuadroD10R(
      java.util.Date dataRiferimento, Long codFotografia)
      throws SolmrException, Exception
  {
    return stampeLocal.getTerreniQuadroD10R(dataRiferimento, codFotografia);
  }
  
  public Vector<String[]> getAnomalie(Long idAzienda, Long idDichiarazioneConsistenza)
      throws SolmrException, Exception
  {
    return stampeLocal.getAnomalie(idAzienda, idDichiarazioneConsistenza);
  }
  
  public Vector<DocumentoVO> getDocumentiStampa(Long idAzienda, Long idDichiarazioneConsistenza,
      String cuaa, java.util.Date dataInserimentoDichiarazione)
    throws SolmrException, Exception
  {
    return stampeLocal.getDocumentiStampa(
        idAzienda, idDichiarazioneConsistenza, cuaa, dataInserimentoDichiarazione);
  }

  /** *************** */
  /** *************** */
  /** *** Stampe **** */
  /** *************** */
  /**
   * / /* Aggiunto il 6/12/2004 per aggirare (temporaneamente) il mancato
   * inserimento della chiamata al metodo omonimo di DittaUmaDAO di UMA tramite
   * CSI / //#-#
   * 
   * @param idAzienda
   *          Long
   * @throws Exception
   * @throws SolmrException
   * @return DittaUMAVO
   */
  public DittaUMAVO getDittaUmaByIdAzienda(Long idAzienda)
      throws Exception, SolmrException
  {
    return anagAziendaLocal.getDittaUmaByIdAzienda(idAzienda);
  }

  /**
   * Metodo che si occupa di storicizzare il mandato di delega in anagrafe, di
   * fatto revocandolo
   * 
   */
  public void storicizzaDelega(DelegaVO dVO, RuoloUtenza ruoloUtenza,
      DocumentoVO documentoVO, AnagAziendaVO anagAziendaVO)
      throws Exception
  {
    anagAziendaLocal
        .storicizzaDelega(dVO, ruoloUtenza, documentoVO, anagAziendaVO);
  }
  
  public int storicizzaDelegaTemporanea(DelegaVO delegaVO, RuoloUtenza ruoloUtenza, 
      AnagAziendaVO anagAziendaVO) throws Exception
  {
    return anagAziendaLocal.storicizzaDelegaTemporanea(delegaVO, ruoloUtenza, anagAziendaVO);
  }
  
  
  // ** INIZIO SERVIZI INFOCAMERE

  public Azienda cercaPerCodiceFiscale(String codiceFiscale)throws SolmrException, Exception{
	  return aaepLocal.cercaPerCodiceFiscale(codiceFiscale);
  }
  
  public it.csi.solmr.ws.infoc.Sede cercaPuntualeSede(String codiceFiscale, String progrSede, String codFonte) throws SolmrException, Exception{
	  return aaepLocal.cercaPuntualeSede(codiceFiscale, progrSede, codFonte);
  }
  
  public it.csi.solmr.ws.infoc.PersonaRIInfoc cercaPuntualePersona(String codiceFiscale, String progrPersona, String codFonte) throws SolmrException, Exception{
	  return aaepLocal.cercaPuntualePersona(codiceFiscale, progrPersona, codFonte);
  }
  
  public List<it.csi.solmr.ws.infoc.ListaPersonaCaricaInfoc> cercaPerFiltriCodFiscFonte(String codiceFiscale, String codFonte) throws SolmrException, Exception {
	  return aaepLocal.cercaPerFiltriCodFiscFonte(codiceFiscale, codFonte);
  }
  
  
  public ParametroRitornoVO serviceGetAziendeInfocAnagrafe(String codiceFiscale,
	      boolean controllaPresenzaSuAAEP, Boolean bloccaAssenzaAAEP,
	      Boolean controllaLegameRLsuAnagrafe,
	      boolean controllaPresenzaValidazione, boolean aziendeAttive)
	      throws SolmrException, ServiceSystemException, SystemException,
	      Exception{
	  return aaepLocal.serviceGetAziendeInfocAnagrafe(codiceFiscale, controllaPresenzaSuAAEP, bloccaAssenzaAAEP, controllaLegameRLsuAnagrafe, controllaPresenzaValidazione, aziendeAttive);
  }
  
  public ParametroRitornoVO serviceGetAziendeInfocAnagrafe(String codiceFiscale,
	      boolean controllaPresenzaSuAAEP, Boolean bloccaAssenzaAAEP,
	      boolean controllaLegameRLsuAnagrafe, boolean controllaPresenzaValidazione)
	      throws SolmrException, ServiceSystemException, SystemException,
	      Exception{
	  return aaepLocal.serviceGetAziendeInfocAnagrafe(codiceFiscale,
		      controllaPresenzaSuAAEP, bloccaAssenzaAAEP,
		      controllaLegameRLsuAnagrafe, controllaPresenzaValidazione);
  }
	      
  public Boolean aggiornaDatiAAEP(String CUAA) throws SystemException, SolmrException, Exception{
     return aaepLocal.serviceAggiornaDatiAAEP(CUAA);
  }
  
  // ** FINE SERVIZI INFOCAMERE



  public String isFlagPrevalente(Long[] idAteco) throws Exception
  {
    return aaepLocal.isFlagPrevalente(idAteco);
  }
  
  public TipoSezioniAaepVO getTipoSezioneAaepByCodiceSez(String codiceSezione)
      throws Exception
  {
    return aaepLocal.getTipoSezioneAaepByCodiceSez(codiceSezione);
  }
  
 

  public void updateInsediamentoGiovani(Long idAzienda) throws Exception
  {
    anagAziendaLocal.updateInsediamentoGiovani(idAzienda);
  }

  // Metodo per recuperare l'elenco dei terreni su cui l'azienda ha o ha avuto
  // dei terreni
  public Vector<StringcodeDescription> getListaComuniTerreniByIdAzienda(Long idAzienda)
      throws Exception, SolmrException
  {
    return fascicoloLocal.getListaComuniTerreniByIdAzienda(idAzienda);
  }

  // Metodo per recuperare il totale delle superfici condotte attive associate
  // alla particella
  public String getTotaleSupCondotteAttiveByIdParticella(Long idParticella,
      Long idAzienda) throws Exception, SolmrException
  {
    return fascicoloLocal.getTotaleSupCondotteAttiveByIdParticella(
        idParticella, idAzienda);
  }

  public String[] cessazioneAziendaPLQSL(Long idAzienda)
      throws Exception, SolmrException
  {
    return anagAziendaLocal.cessazioneAziendaPLQSL(idAzienda);
  }

  public boolean controllaObbligoFascicolo(AnagAziendaVO aziendaVO)
      throws Exception
  {
    return anagAziendaLocal.controllaObbligoFascicolo(aziendaVO);
  }

  // Metodo per recuperare i tipi ufficio zona intermediario
  public Vector<CodeDescription> getElencoUfficiZonaIntermediarioByIdIntermediario(
      UtenteAbilitazioni utenteAbilitazioni) throws Exception
  {
    return anagDenominazioniLocal
        .getElencoUfficiZonaIntermediarioByIdIntermediario(utenteAbilitazioni);
  }
  
  public Vector<CodeDescription> getElencoUfficiZonaIntermediarioAttiviByIdIntermediario(
      UtenteAbilitazioni utenteAbilitazioni) throws Exception
  {
    return anagDenominazioniLocal
        .getElencoUfficiZonaIntermediarioAttiviByIdIntermediario(utenteAbilitazioni);
  }

  // Metodo per recuperare recuperare l'ufficio zona intermediario partendo
  // dalla chiave primaria
  public UfficioZonaIntermediarioVO findUfficioZonaIntermediarioVOByPrimaryKey(
      Long idUfficioZonaIntermediario) throws Exception
  {
    return anagAziendaLocal
        .findUfficioZonaIntermediarioVOByPrimaryKey(idUfficioZonaIntermediario);
  }

  // Metodo per recuperare l'elenco delle aziende in relazione all'intermediario
  // delegato
  public Vector<AnagAziendaVO> getElencoAziendeByCAA(DelegaVO delegaVO)
      throws Exception, SolmrException
  {
    return anagAziendaLocal.getElencoAziendeByCAA(delegaVO);
  }

  // Metodo per aggiornare e recuperare le pratiche relative ad un'azienda
  // agricola
  public Vector<ProcedimentoAziendaVO> updateAndGetPraticheByAzienda(Long idAzienda)
      throws SolmrException, Exception
  {
    return anagAziendaLocal.updateAndGetPraticheByAzienda(idAzienda);
  }

  public CodeDescription[] getListCuaaAttiviProvDestByIdAzienda(Long idAzienda)
      throws Exception
  {
    return anagAziendaLocal.getListCuaaAttiviProvDestByIdAzienda(idAzienda);
  }

  public Vector<CodeDescription> getIndirizziTipiUtilizzoAttivi() throws SolmrException,
      Exception
  {
    return fascicoloLocal.getIndirizziTipiUtilizzoAttivi();
  }

  /**
   * Metodo per recuperare l'elenco delle ute relative ad un comune
   * 
   * @param istatComune
   *          String
   * @param idAzienda
   *          Long
   * @param isActive
   *          boolean
   * @return Vector
   * @throws Exception
   * @throws SolmrException
   */
  public Vector<Long> getListIdUteByIstatComuneAndIdAzienda(String istatComune,
      Long idAzienda, boolean isActive) throws Exception, SolmrException
  {
    return fascicoloLocal.getListIdUteByIstatComuneAndIdAzienda(istatComune,
        idAzienda, isActive);
  }

  /**
   * Metodo per recuperare l'elenco dei CUAA relativi ad un'id_azienda
   * 
   * @param idAzienda
   *          Long
   * @return Vector
   * @throws Exception
   */
  public Vector<String> getListCUAAByIdAzienda(Long idAzienda) throws Exception
  {
    return anagAziendaLocal.getListCUAAByIdAzienda(idAzienda);
  }

  /**
   * Metodo che si occupa di estrarre la denominazione più recente di un'azienda
   * partendo dal CUAA e dall'id_azienda
   * 
   * @param idAzienda
   *          Long
   * @param cuaa
   *          String
   * @return String
   * @throws Exception
   */
  public String getDenominazioneAziendaByCuaaAndIdAzienda(Long idAzienda,
      String cuaa) throws Exception
  {
    return anagAziendaLocal.getDenominazioneAziendaByCuaaAndIdAzienda(
        idAzienda, cuaa);
  }

  // INIZIO GESTIONE DOCUMENTI

  /**
   * Metodo per recuperare tutti i dati presenti nella tabella
   * DB_TIPO_STATO_DOCUMENTO
   * 
   * @param isActive
   *          boolean
   * @return Vector
   * @throws Exception
   * @throws SolmrException
   */
  public Vector<TipoStatoDocumentoVO> getListTipoStatoDocumento(boolean isActive)
      throws Exception, SolmrException
  {
    return documentoLocal.getListTipoStatoDocumento(isActive);
  }

  /**
   * Metodo per ricercare i documenti in relazione ai filtri di ricerca
   * 
   * @param documentoVO
   *          DocumentoVO
   * @param protocollazione
   *          String
   * @param orderBy
   * @return Vector
   * @throws Exception
   */
  public Vector<DocumentoVO> searchDocumentiByParameters(DocumentoVO documentoVO,
      String protocollazione, String[] orderBy) throws Exception
  {
    return documentoLocal.searchDocumentiByParameters(documentoVO,
        protocollazione, orderBy);
  }

  /**
   * Metodo che si occupa di estrarre l'elenco dei tipi documento in funzione
   * della tipologia documento indicata: recupera solo i records attivi se
   * isActive == true
   * 
   * @param idTipologiaDocumento
   *          Long
   * @param isActive
   *          boolean
   * @return it.csi.solmr.dto.anag.TipoDocumentoVO[]
   * @throws Exception
   * @throws SolmrException
   */
  public TipoDocumentoVO[] getListTipoDocumentoByIdTipologiaDocumento(
      Long idTipologiaDocumento, boolean isActive) throws Exception,
      SolmrException
  {
    return documentoLocal.getListTipoDocumentoByIdTipologiaDocumento(
        idTipologiaDocumento, isActive);
  }

  /**
   * Metodo per recuperare il record tipo documento a partire dalla chiave
   * primaria
   * 
   * @param idDocumento
   *          Long
   * @return TipoDocumentoVO
   * @throws Exception
   */
  public TipoDocumentoVO findTipoDocumentoVOByPrimaryKey(Long idDocumento)
      throws Exception
  {
    return documentoLocal.findTipoDocumentoVOByPrimaryKey(idDocumento);
  }

  /**
   * Metodo che si occupa di verificare se esiste già un record su DB_DOCUMENTO
   * con i dati chiave dell'area anagrafica del documento
   * 
   * @param documentoFiltroVO
   *          DocumentoVO
   * @return DocumentoVO
   * @throws Exception
   */
  public DocumentoVO findDocumentoVOBydDatiAnagrafici(
      DocumentoVO documentoFiltroVO) throws Exception
  {
    return documentoLocal.findDocumentoVOBydDatiAnagrafici(documentoFiltroVO);
  }

  /**
   * Metodo che si occupa di inserire il documento in tutte le sue parti
   * 
   * @param documentoVO
   *          DocumentoVO
   * @param ruoloUtenza
   *          ruoloUtenza
   * @param anno
   *          String
   * @param elencoProprietari
   *          Vector
   * @param elencoParticelle
   *          StoricoParticellaVO[]
   * @return Long
   * @throws Exception
   */
  public Long inserisciDocumento(DocumentoVO documentoVO,
      RuoloUtenza ruoloUtenza, String anno, Vector<DocumentoProprietarioVO> elencoProprietari,
      StoricoParticellaVO[] elencoParticelle, Vector<ParticellaAssVO> particelleAssociate)
      throws Exception
  {
    return documentoLocal.inserisciDocumento(documentoVO, ruoloUtenza, anno,
        elencoProprietari, elencoParticelle, particelleAssociate);
  }

  /**
   * Metodo per recuperare il documento a partire dalla chiave primaria
   * 
   * @param idDocumento
   *          Long
   * @return DocumentoVO
   * @throws Exception
   */
  public DocumentoVO findDocumentoVOByPrimaryKey(Long idDocumento)
      throws Exception
  {
    return documentoLocal.findDocumentoVOByPrimaryKey(idDocumento);
  }

  /**
   * Metodo che si occupa di reperire tutti i dati del dettaglio del documento
   * 
   * @param idDocumento
   *          Long
   * @param legamiAttivi
   * @return DocumentoVO
   * @throws Exception
   * @throws SolmrException
   */
  public DocumentoVO getDettaglioDocumento(Long idDocumento,
      boolean legamiAttivi) throws Exception, SolmrException
  {
    return documentoLocal.getDettaglioDocumento(idDocumento, legamiAttivi);
  }

  /**
   * Metodo che si occupa di reperire tutti i dati del dettaglio del documento.
   * E' analogo al metodo getDettaglioDocumento che si trova in DocumentoBean.
   * L'unica differenza che questo cerca i proprietari e le particelle associate
   * relativamente ad una dichiarazione di consistenza
   * 
   * @param idDocumento
   *          Long
   * @param dataConsistenza
   *          Date
   * @param idDichiarazioneConsistenza
   *          Long
   * @return DocumentoVO
   * @throws Exception
   * @throws SolmrException
   */
  public DocumentoVO getDettaglioDocumento(Long idDocumento,
      java.util.Date dataConsistenza, Long idDichiarazioneConsistenza)
      throws Exception, SolmrException
  {
    return stampeLocal.getDettaglioDocumento(idDocumento, dataConsistenza,
        idDichiarazioneConsistenza);
  }

  /**
   * Metodo che si occupa di aggiornare i dati del documento
   * 
   * @param documentoVO
   *          DocumentoVO
   * @param RuoloUtenza
   *          ruoloUtenza
   * @param operazioneRichiesta
   *          String
   * @return Long
   * @throws Exception
   */
  public Long aggiornaDocumento(DocumentoVO documentoVO,
      RuoloUtenza ruoloUtenza, String operazioneRichiesta)
      throws Exception
  {
    return documentoLocal.aggiornaDocumento(documentoVO, ruoloUtenza,
        operazioneRichiesta);
  }
  
  public Long aggiornaDocumentoIstanzaLimitato(DocumentoVO documentoVO,
      RuoloUtenza ruoloUtenza, String operazioneRichiesta) throws Exception
  {
    return documentoLocal.aggiornaDocumentoIstanzaLimitato(documentoVO, 
        ruoloUtenza, operazioneRichiesta);
  }

  /**
   * Metodo che si occupa di eliminare i documenti
   * 
   */
  public void deleteDocumenti(String[] documentiDaEliminare, String note,
      long idUtenteAggiornamento) throws Exception
  {
    documentoLocal.deleteDocumenti(documentiDaEliminare, note, idUtenteAggiornamento);
  }

  /**
   * Metodo che si occupa di effettuare la protocollazione dei documenti
   * 
   */
  public void protocollaDocumenti(String[] documentiDaProtocollare, Long idAzienda,
      RuoloUtenza ruoloUtenza) throws Exception
  {
    documentoLocal.protocollaDocumenti(documentiDaProtocollare, idAzienda, ruoloUtenza);
  }

  /**
   * Metodo che si occupa di recuperare l'elenco dei documenti a partire
   * dall'id_conduzione_particella o dall'id_conduzione_dichiarata in relazione
   * al boolean isStorico
   * 
   * @param idConduzione
   * @param isStorico
   * @param altreParticelle
   * @return java.util.Vector
   * @throws Exception
   * @throws SolmrException
   */
  public Vector<DocumentoVO> getListDettaglioDocumentoByIdConduzione(Long idConduzione,
      boolean isStorico, boolean altreParticelle) throws Exception,
      SolmrException
  {
    return documentoLocal.getListDettaglioDocumentoByIdConduzione(
        idConduzione, isStorico, altreParticelle);
  }
  
  public Vector<DocumentoVO> getListDettaglioDocumentoByIdConduzionePopUp(Long idConduzioneParticella)
    throws Exception, SolmrException
  {
    return documentoLocal.getListDettaglioDocumentoByIdConduzionePopUp(idConduzioneParticella);
  }
  
  public Vector<DocumentoVO> getListDettaglioDocumentoByIdConduzioneDichiarataPopUp(Long idConduzioneDichiarata) 
    throws Exception, SolmrException
  {
    return documentoLocal.getListDettaglioDocumentoByIdConduzioneDichiarataPopUp(
        idConduzioneDichiarata);
  }

  /**
   * Metodo che mi restituisce l'elenco dei documenti associati ad un'azienda
   * 
   * @param idAzienda
   * @param onlyActive
   * @param orderBy
   * @param idTipologiaDocumento
   * @return it.csi.solmr.dto.anag.DocumentoVO[]
   * @throws Exception
   */
  public DocumentoVO[] getListDocumentiByIdAzienda(Long idAzienda,
      boolean onlyActive, String[] orderBy, Long idTipologiaDocumento)
      throws Exception
  {
    return documentoLocal.getListDocumentiByIdAzienda(idAzienda, onlyActive,
        orderBy, idTipologiaDocumento);
  }

  /**
   * Metodo per recuperare tutti i dati presenti nella tabella
   * DB_CATEGORIA_DOCUMENTO
   * 
   * @param idTipologiaDocumento
   * @param orderBy[]
   * @return it.csi.solmr.dto.anag.TipoCategoriaDocumentoVO[]
   * @throws Exception
   */
  public TipoCategoriaDocumentoVO[] getListTipoCategoriaDocumentoByIdTipologiaDocumento(
      Long idTipologiaDocumento, String orderBy[]) throws Exception
  {
    return documentoLocal.getListTipoCategoriaDocumentoByIdTipologiaDocumento(
        idTipologiaDocumento, orderBy);
  }

  /**
   * Metodo per recuperare tutti i dati presenti nella tabella DB_TIPO_DOCUMENTO
   * a partire dall'id_documento_categoria
   * 
   * @param idCategoriaDocumento
   * @param orderBy[]
   * @param onlyActive
   * @return it.csi.solmr.dto.anag.TipoDocumentoVO[]
   * @throws Exception
   */
  public TipoDocumentoVO[] getListTipoDocumentoByIdCategoriaDocumento(
      Long idCategoriaDocumento, String orderBy[], boolean onlyActive,
      Boolean cessata) throws Exception
  {
    return documentoLocal.getListTipoDocumentoByIdCategoriaDocumento(
        idCategoriaDocumento, orderBy, onlyActive, cessata);
  }

  /**
   * Metodo per recuperare il record su DB_CATEGORIA_DOCUMENTO a partire dalla
   * chiave primaria
   * 
   * @param idCategoriaDocumento
   * @return it.csi.solmr.dto.anag.TipoCategoriaDocumentoVO
   * @throws Exception
   */
  public TipoCategoriaDocumentoVO findTipoCategoriaDocumentoByPrimaryKey(
      Long idCategoriaDocumento) throws Exception
  {
    return documentoLocal
        .findTipoCategoriaDocumentoByPrimaryKey(idCategoriaDocumento);
  }

  /**
   * Metodo per recuperare tutti i dati attivi presenti nella tabella
   * DB_TIPO_DOCUMENTO a partire dall'id_documento_categoria più quelli cessati
   * relativi all'azienda a cui sono collegati i documento
   * 
   * @param idCategoriaDocumento
   * @param idAzienda
   * @param orderBy[]
   * @return it.csi.solmr.dto.anag.TipoDocumentoVO[]
   * @throws Exception
   */
  public TipoDocumentoVO[] getListTipoDocumentoByIdCategoriaDocumentoAndIdAzienda(
      Long idCategoriaDocumento, Long idAzienda, String orderBy[])
      throws Exception
  {
    return documentoLocal
        .getListTipoDocumentoByIdCategoriaDocumentoAndIdAzienda(
            idCategoriaDocumento, idAzienda, orderBy);
  }

  /**
   * Metodo che mi permette di estrarre la data max di esecuzione controlli dei
   * documenti relativi ad una determinata azienda
   * 
   * @param idAzienda
   * @return dataEsecuzione
   * @throws Exception
   */
  public String getDataMaxEsecuzioneDocumento(Long idAzienda)
      throws Exception
  {
    return documentoLocal.getDataMaxEsecuzioneDocumento(idAzienda);
  }

  /**
   * Metodo per estrarre i cuaa di tutti i proprietari dei documenti
   * dell'azienda agricola indicata
   * 
   * @param idAzienda
   * @param cuaa
   * @param onlyActive
   * @return java.lnag.String[]
   * @throws Exception
   */
  public String[] getCuaaProprietariDocumentiAzienda(Long idAzienda,
      String cuaa, boolean onlyActive) throws Exception
  {
    return documentoLocal.getCuaaProprietariDocumentiAzienda(idAzienda, cuaa,
        onlyActive);
  }

  /**
   * Metodo che mi restituisce l'elenco delle anomalie relative al documento
   * selezionato
   * 
   * @param idDocumento
   * @param orderBy
   * @return it.csi.solmr.dto.anag.EsitoControlloDocumentoVO[]
   * @throws Exception
   */
  public EsitoControlloDocumentoVO[] getListEsitoControlloDocumentoByIdDocumento(
      Long idDocumento, String[] orderBy) throws Exception
  {
    return documentoLocal.getListEsitoControlloDocumentoByIdDocumento(
        idDocumento, orderBy);
  }

  /**
   * Metodo che mi permette di reperire tutti i documenti di un'azienda in
   * funzione di un determinato id controllo: necessario per la correzione delle
   * anomalie
   * 
   * @param anagAziendaVO
   * @param idControllo
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.DocumentoVO[]
   * @throws Exception
   */
  public DocumentoVO[] getListDocumentiAziendaByIdControllo(
      AnagAziendaVO anagAziendaVO, Long idControllo, boolean onlyActive,
      String[] orderBy) throws Exception
  {
    return documentoLocal.getListDocumentiAziendaByIdControllo(anagAziendaVO,
        idControllo, onlyActive, orderBy);
  }

  /**
   * Metodo che mi permette di recuperare l'elenco dei tipi documenti relativi
   * ad un determinato id controllo
   * 
   * @param idControllo
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.TipoDocumentoVO[]
   * @throws Exception
   */
  public TipoDocumentoVO[] getListTipoDocumentoByIdControllo(Long idControllo,
      boolean onlyActive, String orderBy[]) throws Exception
  {
    return documentoLocal.getListTipoDocumentoByIdControllo(idControllo,
        onlyActive, orderBy);
  }

  /**
   * Metodo che mi permette di reperire tutti i documenti di un'azienda in
   * funzione di un determinato id controllo, particella e tipologia documento:
   * necessario per la correzione delle anomalie
   * 
   * @param anagAziendaVO
   * @param idControllo
   * @param idStoricoParticella
   * @param tipologiaDocumento
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.DocumentoVO[]
   * @throws Exception
   */
  public DocumentoVO[] getListDocumentiAziendaByIdControlloAndParticella(
      AnagAziendaVO anagAziendaVO, Long idControllo, Long idStoricoParticella,
      boolean onlyActive, String[] orderBy) throws Exception
  {
    return documentoLocal.getListDocumentiAziendaByIdControlloAndParticella(
        anagAziendaVO, idControllo, idStoricoParticella, onlyActive, orderBy);
  }

  public DocumentoVO[] getListDocumentiPerDichCorrettiveAziendaByIdControlloAndParticella(
      AnagAziendaVO anagAziendaVO, Long idControllo, Long idStoricoParticella,
      String annoCampagna, String[] orderBy) throws Exception
  {
    return documentoLocal
        .getListDocumentiPerDichCorrettiveAziendaByIdControlloAndParticella(
            anagAziendaVO, idControllo, idStoricoParticella, annoCampagna,
            orderBy);
  }

  public DocumentoVO[] getListDocumentiPerDichCorrettiveAziendaByIdControllo(
      AnagAziendaVO anagAziendaVO, Long idControllo, String annoCampagna,
      String[] orderBy) throws Exception
  {
    return documentoLocal
        .getListDocumentiPerDichCorrettiveAziendaByIdControllo(anagAziendaVO,
            idControllo, annoCampagna, orderBy);
  }
  
  public Vector<DocumentoConduzioneVO> getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella(
      Long idDocumento, Long idConduzioneParticella, boolean onlyActive) throws Exception
  {
    return documentoLocal.getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella(
        idDocumento, idConduzioneParticella, onlyActive);
  }
  
  public Vector<StoricoParticellaVO> getListParticelleByIdDocumentoAlPianoCorrente(
      Long idDocumento) throws Exception
  {
    return documentoLocal.getListParticelleByIdDocumentoAlPianoCorrente(idDocumento);
  }

  // FINE GESTIONE DOCUMENTI

  /*****************************************************************************
   * ************************ COMUNE BEGIN *************************
   ****************************************************************************/

  /**
   * 
   * 
   * @param codiceFiscale
   *          String
   * @param idProcedimento
   *          Long
   * @param ruolo
   *          String
   * @param codiceEnte
   *          String
   * @param dirittoAccesso
   *          String
   * @param idLivello
   *          Long
   * @throws SystemException
   * @throws SolmrException
   * @throws Exception
   * @return Vector
   */
  /*public Vector<UtenteProcedimento> serviceGetUtenteProcedimento(
      String codiceFiscale, Long idProcedimento, String ruolo,
      String codiceEnte, String dirittoAccesso, Long idLivello)
      throws SystemException, SolmrException, Exception
  {
    return smrCommLocal.serviceGetUtenteProcedimento(codiceFiscale,
        idProcedimento, ruolo, codiceEnte, dirittoAccesso, idLivello);
  }*/

  /**
   * Metodo che si aggancia ad un servizio erogato da "COMUNE" per recuperare
   * tutti i dati dell'amministrazione di competenza partendo dall'istat del
   * comune e dal tipo amministrazione specificato
   * 
   * @param istatComune
   *          String
   * @param tipoAmministrazione
   *          String
   * @return AmmCompetenzaVO[]
   * @throws SystemException
   * @throws SolmrException
   * @throws Exception
   */
  public AmmCompetenzaVO[] serviceGetListAmmCompetenzaByComuneCollegato(
      String istatComune, String tipoAmministrazione) throws SystemException,
      SolmrException, Exception
  {
    return smrCommLocal.serviceGetListAmmCompetenzaByComuneCollegato(
        istatComune, tipoAmministrazione);
  }

  /**
   * Metodo che si occupa di invocare il servizio di comune che mi restituisce
   * il record della tabella DB_AMM_COMPETENZA a partire dal codice ente
   * 
   * @param codiceAmm
   *          String
   * @return AmmCompetenzaVO
   * @throws SolmrException
   * @throws Exception
   */
  public AmmCompetenzaVO serviceFindAmmCompetenzaByCodiceAmm(String codiceAmm)
      throws SolmrException, Exception
  {
    return smrCommLocal.serviceFindAmmCompetenzaByCodiceAmm(codiceAmm);
  }

  /**
   * Metodo che invoca un servizio di comune che si occupa di reperire i dati
   * dell'intermediario gestendo contemporaneamente sia il profilo intermediario
   * che quello di OPR gestore
   * 
   * @param codiceFiscale
   *          String
   * @return IntermediarioVO
   * @throws Exception
   * @throws SolmrException
   */
  public IntermediarioVO serviceFindIntermediarioByCodiceFiscale(
      String codiceFiscale) throws Exception, SolmrException
  {
    return smrCommLocal.serviceFindIntermediarioByCodiceFiscale(codiceFiscale);
  }

  /**
   * Metodo che invoca un servizio di comune che si occupa di reperire i dati
   * dell'intermediario gestendo contemporaneamente sia il profilo intermediario
   * che quello di OPR gestore
   * 
   * @param idIntermediario
   *          Long
   * @return IntermediarioVO
   * @throws Exception
   * @throws SolmrException
   */
  public IntermediarioVO serviceFindIntermediarioByIdIntermediario(
      Long idIntermediario) throws Exception, SolmrException
  {
    return smrCommLocal
        .serviceFindIntermediarioByIdIntermediario(idIntermediario);
  }

  /**
   * Metodo che richiama un servizio di comune per ottenere l'elenco delle
   * amministrazione di competenza
   * 
   * @return it.csi.solmr.dto.comune.AmmCompetenzaVO[]
   * @throws Exception
   * @throws SolmrException
   */
  public AmmCompetenzaVO[] serviceGetListAmmCompetenza()
      throws Exception, SolmrException
  {
    return smrCommLocal.serviceGetListAmmCompetenza();
  }

  /**
   * Metodo che richiama un servizio di comune per verifica la gerarchia tra gli
   * utenti di tipo intermediario/OPR
   * 
   * @param idUtenteConnesso
   * @param idUtentePratica
   * @return boolean
   * @throws Exception
   * @throws SolmrException
   */
  /*public boolean serviceVerificaGerarchia(Long idUtenteConnesso,
      Long idUtentePratica) throws Exception, SolmrException
  {
    return smrCommLocal.serviceVerificaGerarchia(idUtenteConnesso,
        idUtentePratica);
  }*/

  public AmmCompetenzaVO[] serviceFindAmmCompetenzaByIdRange(
      String idAmmCompetenza[]) throws Exception, SolmrException
  {
    return smrCommLocal.serviceFindAmmCompetenzaByIdRange(idAmmCompetenza);
  }
  
  public TecnicoAmministrazioneVO[] serviceGetTecnicoAmmByIdAmmCompAndIdProcedimento(Long idAmmCompetenza,
      Long idProcedimento)
    throws SolmrException,Exception
  {
    return smrCommLocal.serviceGetTecnicoAmmByIdAmmCompAndIdProcedimento(idAmmCompetenza, idProcedimento);
  }
  
  public long[] smrcommSearchListIdEntiPrivatiByCodiceEntePrivatoRange(String[] arrCodiceEntePrivato,
      boolean flagCessazione) throws SolmrException, Exception
  {
    return smrCommLocal.smrcommSearchListIdEntiPrivatiByCodiceEntePrivatoRange(
        arrCodiceEntePrivato, flagCessazione);
  }
      
      
  public DatiEntePrivatoVO[] smrcommGetEntiPrivatiByIdEntePrivatoRange(long[] idEntePrivato,
      int tipoRisultato, EntePrivatoFiltroVO filtro) throws SolmrException,Exception
  {
    return smrCommLocal.smrcommGetEntiPrivatiByIdEntePrivatoRange(idEntePrivato, tipoRisultato, filtro);
  }
        
     

  /*****************************************************************************
   * ************************ COMUNE END *************************
   ****************************************************************************/

  /**
   * 
   * 
   * @param idControllo
   *          Long
   * @throws Exception
   * @return Vector
   */
  public Vector<CodeDescription> getDocumentiByIdControllo(Long idControllo)
      throws Exception
  {
    return consistenzaLocal.getDocumentiByIdControllo(idControllo);
  }

  public void deleteInsertDichiarazioneCorrezione(
      String elencoIdDichiarazioneSegnalazione[], Vector<ErrAnomaliaDicConsistenzaVO> corrErr, long idUtente)
      throws Exception
  {
    consistenzaLocal.deleteInsertDichiarazioneCorrezione(
        elencoIdDichiarazioneSegnalazione, corrErr, idUtente);
  }

  // Metodo per effettuare la cancellazione di una dichiarazione di correzione
  public void deleteDichiarazioneCorrezione(
      String elencoIdDichiarazioneSegnalazione[]) throws Exception
  {
    consistenzaLocal
        .deleteDichiarazioneCorrezione(elencoIdDichiarazioneSegnalazione);
  }

  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichiarazioneConsistenza(
      Long elencoIdDichiarazioneSegnalazione[], long idMotivoDichiarazione)
      throws Exception
  {
    return consistenzaLocal.getErroriAnomalieDichiarazioneConsistenza(
        elencoIdDichiarazioneSegnalazione, idMotivoDichiarazione);
  }

  public boolean isRuolosuAnagrafe(String codiceFiscale, long idAzienda)
      throws Exception
  {
    return anagDenominazioniLocal.isRuolosuAnagrafe(codiceFiscale, idAzienda);
  }

  public PersonaFisicaVO getRuolosuAnagrafe(String codiceFiscale,
      long idAzienda, String codRuoloAAEP) throws Exception
  {
    return anagDenominazioniLocal.getRuolosuAnagrafe(codiceFiscale, idAzienda,
        codRuoloAAEP);
  }

  public boolean getRuolosuAnagrafe(String codiceFiscale, long idAzienda)
      throws Exception
  {
    return anagDenominazioniLocal.getRuolosuAnagrafe(codiceFiscale, idAzienda);
  }

  public String getIstatByDescComune(String descComune) throws Exception
  {
    return anagDenominazioniLocal.getIstatByDescComune(descComune);
  }

  public void importaSoggCollAAEP(AnagAziendaVO anagAziendaVO,
      Azienda impresaInfoc, HashMap<?,?> listaPersone, String idParametri[],
      Long idUtenteAggiornamento) throws Exception, SolmrException
  {
    try
    {
      soggettiLocal.importaSoggCollAAEP(anagAziendaVO, impresaInfoc,
          listaPersone, idParametri, idUtenteAggiornamento);
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(e.getMessage());
    }
  }

  public Vector<DocumentoVO> getDocumenti(String idDocumenti[])
      throws Exception
  {
    return stampeLocal.getDocumenti(idDocumenti);
  }

  public CodeDescription getAttivitaATECObyCode(String codiceAteco)
      throws Exception
  {
    return aaepLocal.getAttivitaATECObyCode(codiceAteco);
  }

  public CodeDescription getAttivitaATECObyCodeParametroCATE(String codiceAteco)
      throws Exception
  {
    return aaepLocal.getAttivitaATECObyCodeParametroCATE(codiceAteco);
  }

  public void importaUteAAEP(AnagAziendaVO anagAziendaVO,
      Sede[] sedeInfocamere, String idParametri[], Long idUtenteAggiornamento)
      throws Exception
  {
    aaepLocal.importaUteAAEP(anagAziendaVO, sedeInfocamere, idParametri,
        idUtenteAggiornamento);
  }

  public CodeDescription[] getElencoAtecoNew(String codiceAteco, Long idAzienda)
      throws Exception
  {
    return aaepLocal.getElencoAtecoNew(codiceAteco, idAzienda);
  }

  public Boolean isUtenteAbilitatoProcedimento(UtenteIride2VO utenteIride2VO)
      throws SystemException, InvalidParameterException,
      UnrecoverableException, SolmrException, Exception
  {
    return smrCommLocal.isUtenteAbilitatoProcedimento(utenteIride2VO);
  }

  public Long writeAccessLogUser(UtenteIride2VO utenteIride2VO)
      throws SystemException, InvalidParameterException,
      UnrecoverableException, SolmrException, Exception
  {
    return smrCommLocal.writeAccessLogUser(utenteIride2VO);
  }

  public RuoloUtenza loadRoleUser(UtenteIride2VO utenteIride2VO)
      throws SystemException, InvalidParameterException,
      UnrecoverableException, SolmrException, Exception
  {
    return smrCommLocal.loadRoleUser(utenteIride2VO);
  }

  public Boolean isUtenteConRuoloSuProcedimento(String codiceFiscale,
      Long idProcedimento) throws SystemException, InvalidParameterException,
      UnrecoverableException, SolmrException, Exception
  {
    return smrCommLocal.isUtenteConRuoloSuProcedimento(codiceFiscale,
        idProcedimento);
  }

  public it.csi.solmr.dto.profile.TipoProcedimentoVO serviceFindTipoProcedimentoByDescrizioneProcedimento(
      String descrizione) throws SystemException, InvalidParameterException,
      UnrecoverableException, SolmrException, Exception
  {
    return smrCommLocal
        .serviceFindTipoProcedimentoByDescrizioneProcedimento(descrizione);
  }


  /**
   * Metodi aggiunti per il passaggio ad IRIDE2
   * 
   * @param idAzienda
   *          Long
   * @param codiceEnte
   *          String
   * @throws SystemException
   * @throws InvalidParameterException
   * @throws Exception
   * @throws UnrecoverableException
   * @return MandatoVO
   */
  public MandatoVO serviceGetMandato(Long idAzienda, String codiceEnte)
      throws SystemException, InvalidParameterException, Exception,
      UnrecoverableException
  {
    MandatoVO mandato = new MandatoVO();
    try
    {
      DelegaAnagrafeVO delegaAnagrafe = anagAziendaLocal.serviceGetDelega(
          idAzienda, codiceEnte, new Boolean(true), null);
      if (delegaAnagrafe == null)
      {
        delegaAnagrafe = anagAziendaLocal.serviceGetDelega(idAzienda, null,
            null, null);
      }
      mandato.setDelegaAnagrafe(delegaAnagrafe);
      mandato.setEsenteDelega(anagAziendaLocal
          .serviceIsEsenteDelega(idAzienda));
      return mandato;
    }
    catch (SolmrException s)
    {
      if (s.getErrorType() == ErrorTypes.INVALID_PARAMETER)
        throw new InvalidParameterException(s.getErrorDesc());
      else
        throw new UnrecoverableException(s.getErrorDesc());
    }   
    catch (Exception e)
    {
      throw new UnrecoverableException(e.getMessage());
    }
  }
  
  public boolean getDelegaBySocio(String codFiscIntermediario, Long idAziendaAssociata) 
    throws Exception
  {
    return anagAziendaLocal.getDelegaBySocio(codFiscIntermediario, idAziendaAssociata);
  }
  
  public AziendaCollegataVO findAziendaCollegataByFatherAndSon(Long idAziendaFather, Long idAziendaSon,
      Date dataSituazione) throws Exception
  {
    return anagAziendaLocal.findAziendaCollegataByFatherAndSon(idAziendaFather, idAziendaSon, dataSituazione);
  }
  
  public boolean isSoggettoAssociatoByFatherAndSon(Long idAziendaFather, String cuaaSon,
      Date dataSituazione) throws Exception
  {
    return anagAziendaLocal.isSoggettoAssociatoByFatherAndSon(idAziendaFather, cuaaSon, dataSituazione);
  }

  /**
   * Metodo per recuperare l'elenco dei mandati a partire dall'utente
   * 
   */
  public Vector<DelegaVO> getMandatiByUtente(UtenteAbilitazioni utenteAbilitazioni, boolean forZona,
      java.util.Date dataDal, java.util.Date dataAl) throws SolmrException,
      Exception
  {
    return anagDenominazioniLocal.getMandatiByUtente(utenteAbilitazioni, forZona,
        dataDal, dataAl);
  }

  /**
   * Recupero l'elenco di CAA(espresso con un vector di CODE-DESCRIPTION perchè
   * mi serve per popolare una combo) in relazione all'utente che si è loggato
   * 
   */
  public Vector<CodeDescription> getElencoCAAByUtente(UtenteAbilitazioni utenteAbilitazioni)
      throws Exception, SolmrException
  {
    return anagDenominazioniLocal.getElencoCAAByUtente(utenteAbilitazioni);
  }

  /**
   * Metodo per recuperare l'elenco dei mandati validati in un determinato
   * periodo di tempo
   * 
   */
  public Vector<DelegaVO> getMandatiValidatiByUtente(UtenteAbilitazioni utenteAbilitazioni,
      boolean forZona, java.util.Date dataDal, java.util.Date dataAl)
      throws Exception, SolmrException
  {
    return anagDenominazioniLocal.getMandatiValidatiByUtente(utenteAbilitazioni, forZona,
        dataDal, dataAl);
  }

  /**
   * Metodo per reperire tutte le informazioni relative all'utente di tipo
   * intermediario associato ad uno specifico ufficio di zona
   * 
   * @param idUfficioZonaIntermediario
   *          Long
   * @return it.csi.solmr.dto.IntermediarioVO
   * @throws Exception
   */
  public IntermediarioVO getIntermediarioVOByIdUfficioZonaIntermediario(
      Long idUfficioZonaIntermediario) throws Exception
  {
    return anagDenominazioniLocal
        .getIntermediarioVOByIdUfficioZonaIntermediario(idUfficioZonaIntermediario);
  }

  /**
   * Metodo per recuperare l'intermediario a partire dalla chiave primaria
   * 
   * @param idIntermediario
   *          Long
   * @return it.csi.solmr.dto.IntermediarioVO
   * @throws Exception
   */
  public IntermediarioVO findIntermediarioVOByPrimaryKey(Long idIntermediario)
      throws Exception
  {
    return anagDenominazioniLocal
        .findIntermediarioVOByPrimaryKey(idIntermediario);
  }

  /**
   * Ricavo l'oggeto intermediario VO passando il suo codice fiscale.
   * 
   * @param codice_fiscale
   * @return
   * @throws Exception
   */
  /*public IntermediarioVO getIntermediarioVOByCodiceFiscale(String codice_fiscale)
      throws Exception
  {
    return anagDenominazioniRemote
        .getIntermediarioVOByCodiceFiscale(codice_fiscale);
  }*/

  /**
   * Metodo che si occupa di i dati dalla tabella REGIONE in relazione all'istat
   * della provincia
   * 
   * @param istatProvincia
   *          String
   * @return CodeDescription
   * @throws Exception
   */
  public CodeDescription findRegioneByIstatProvincia(String istatProvincia)
      throws Exception
  {
    return commonLocal.findRegioneByIstatProvincia(istatProvincia);
  }

  /**
   * Metodo che si occupa di reperire i dati dalla tabella REGIONE relativi
   * all'intermediario loggato partendo dal codice_fiscale
   * 
   * @param codiceFiscaleIntermediario
   *          String
   * @return CodeDescription
   * @throws Exception
   */
  public CodeDescription findRegioneByCodiceFiscaleIntermediario(
      String codiceFiscaleIntermediario) throws Exception
  {
    return commonLocal
        .findRegioneByCodiceFiscaleIntermediario(codiceFiscaleIntermediario);
  }

  /**
   * Metodo che si occupa di estrarre tutti i gruppo controllo associati ad una
   * determinata dichiarazione di consistenza
   * 
   * @param idDichiarazioneConsistenza
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
  public CodeDescription[] getListTipoGruppoControlloByIdDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza, String[] orderBy) throws Exception
  {
    return commonLocal.getListTipoGruppoControlloByIdDichiarazioneConsistenza(
        idDichiarazioneConsistenza, orderBy);
  }

  /*
   * restituisce un vettore di aziende che hanno come ID_AZIENDA quelli
   * contenuti in collIdAziende @return restituisce un vettore di AnagAziendaVO
   * @throws SystemException @throws Exception @throws
   * UnrecoverableException
   */
  public AnagAziendaVO[] serviceGetListAziendeByIdRange(Vector<Long> collIdAziende)
      throws Exception, SolmrException
  {
    return anagAziendaLocal
        .serviceGetListAziendeByIdRange(collIdAziende, null);
  }

 

  public SianTitoloMovimentazioneRispostaVO serviceSianMovimentazioneTitoli(
      String cuaa, String idDocumento, String campagna, String fattispecie,
      String cedente, SianTitoliMovimentatiVO[] elencoTitoli)
      throws SystemException, InvalidParameterException, Exception,
      UnrecoverableException
  {
    // Creo l'oggetto Stop Watch per monitorare le operazioni eseguite
    // all'interno del metodo
    SianTitoloMovimentazioneRispostaVO sianTitoloMovimentazioneRispostaVO = null;
    StopWatch watcher = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
    try
    {
      // START
      watcher.start();
      sianTitoloMovimentazioneRispostaVO = sianLocal
          .serviceSianMovimentazioneTitoli(cuaa, idDocumento, campagna,
              fattispecie, cedente, elencoTitoli);
    }
    catch (SolmrException se)
    {
      if (se.getErrorType() == ErrorTypes.INVALID_PARAMETER)
        throw new InvalidParameterException(se.getErrorDesc());
      else
        throw new UnrecoverableException(se.getErrorDesc());
    }    
    catch (Exception e)
    {
      throw new UnrecoverableException(e.getMessage());
    }
    // Monitoraggio tempistica
    watcher.dumpElapsed("AnagServiceFacadeBean", "serviceMovimentazioneTitoli",
        "In serviceMovimentazioneTitoli method from the beginning to the end",
        "List of parameters: ");
    // STOP
    watcher.stop();
    return sianTitoloMovimentazioneRispostaVO;
  }

  public SianEsitiRicevutiRispostaVO serviceSianEsitiRicevuti(String ultimoEsito)
      throws SystemException, InvalidParameterException, Exception,
      UnrecoverableException
  {
    // Creo l'oggetto Stop Watch per monitorare le operazioni eseguite
    // all'interno del metodo
    SianEsitiRicevutiRispostaVO sianEsitiRicevutiRispostaVO = null;
    StopWatch watcher = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
    try
    {
      // START
      watcher.start();
      sianEsitiRicevutiRispostaVO = sianLocal
          .serviceSianEsitiRicevuti(ultimoEsito);
    }
    catch (SolmrException se)
    {
      if (se.getErrorType() == ErrorTypes.INVALID_PARAMETER)
        throw new InvalidParameterException(se.getErrorDesc());
      else
        throw new UnrecoverableException(se.getErrorDesc());
    }    
    catch (Exception e)
    {
      throw new UnrecoverableException(e.getMessage());
    }
    // Monitoraggio tempistica
    watcher.dumpElapsed("AnagServiceFacadeBean", "serviceSianEsitiRicevuti",
        "In serviceSianEsitiRicevuti method from the beginning to the end",
        "List of parameters: " + ultimoEsito);
    // STOP
    watcher.stop();
    return sianEsitiRicevutiRispostaVO;
  }

  public SianEsitoDomandeRispostaVO serviceSianEsitoDomande()
      throws SystemException, InvalidParameterException, Exception,
      UnrecoverableException
  {
    // Creo l'oggetto Stop Watch per monitorare le operazioni eseguite
    // all'interno del metodo
    SianEsitoDomandeRispostaVO sianEsitoDomandeRispostaVO = null;
    StopWatch watcher = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
    try
    {
      // START
      watcher.start();
      sianEsitoDomandeRispostaVO = sianLocal.serviceSianEsitoDomande();
    }
    catch (SolmrException se)
    {
      if (se.getErrorType() == ErrorTypes.INVALID_PARAMETER)
        throw new InvalidParameterException(se.getErrorDesc());
      else
        throw new UnrecoverableException(se.getErrorDesc());
    }   
    catch (Exception e)
    {
      throw new UnrecoverableException(e.getMessage());
    }
    // Monitoraggio tempistica
    watcher.dumpElapsed("AnagServiceFacadeBean", "serviceSianEsitoDomande",
        "In serviceSianEsitoDomande method from the beginning to the end",
        "List of parameters: ");
    // STOP
    watcher.stop();
    return sianEsitoDomandeRispostaVO;
  }

  public SianTerritorioVO[] verificaCensimentoFoglio(
      SianTerritorioVO[] elencoSian) throws Exception
  {
    return fascicoloLocal.verificaCensimentoFoglio(elencoSian);
  }

  // NUOVO TERRITORIALE

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
    return gestioneTerreniLocal.getListComuniParticelleByIdAzienda(idAzienda,
        onlyActive, orderBy);
  }
  
  public Vector<ComuneVO> getListComuniParticelleByIdDichiarazioneConsistenza(Long idDichiarazioneConsistenza, String[] orderBy)
      throws Exception
  {
    return gestioneTerreniLocal.getListComuniParticelleByIdDichiarazioneConsistenza(idDichiarazioneConsistenza, orderBy);
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
    return gestioneTerreniLocal.getListTipiUsoSuoloByIdAzienda(idAzienda,
        onlyActive, orderBy);
  }*/
  
  /*public Vector<TipoUtilizzoVO> getListTipiDestinazProdPrimSecByIdAzienda(Long idAzienda)
      throws Exception
  {
    return gestioneTerreniLocal.getListTipiDestinazProdPrimSecByIdAzienda(idAzienda);
  }*/
  
  
  public Vector<TipoUtilizzoVO> getListUtilizziElencoTerrPianoLavorazione(Long idAzienda)
      throws Exception
  {
    return gestioneTerreniLocal.getListUtilizziElencoTerrPianoLavorazione(idAzienda);
  }
    
  public Vector<TipoUtilizzoVO> getListUtilizziElencoTerrPianoLavorazioneStor(Long idAzienda) 
    throws Exception
  {
    return gestioneTerreniLocal.getListUtilizziElencoTerrPianoLavorazioneStor(idAzienda);
  }
    
  public Vector<TipoUtilizzoVO> getListUtilizziElencoTerrValidazione(Long idDichiarazioneConsistenza) 
      throws Exception
  {
    return gestioneTerreniLocal.getListUtilizziElencoTerrValidazione(idDichiarazioneConsistenza);
  }
  
  public Vector<TipoUtilizzoVO> getListTipiUsoSuoloByIdDichCons(long idDichiarazioneConsistenza, String[] orderBy)
      throws Exception
  {
    return gestioneTerreniLocal.getListTipiUsoSuoloByIdDichCons(idDichiarazioneConsistenza, orderBy);
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
    return gestioneTerreniLocal
        .getMaxDataEsecuzioneControlliConduzioneParticella(idAzienda);
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
    return gestioneTerreniLocal
        .getMaxDataEsecuzioneControlliConduzioneDichiarata(idAzienda);
  }

  /**
   * Metodo che mi restituisce l'elenco delle particelle in tutte le sue
   * componenti (DB_STORICO_PARTICELLA, DB_CONDUZIONE_PARTICELLA,
   * DB_UTILIZZO_PARTICELLA) in relazione dei parametri di ricerca impostati, di
   * un criterio di ordinamento e dell'azienda selezionata
   * 
   * @param filtriParticellareRicercaVO
   * @param java.lang.Long
   *          idAzienda
   * @return java.util.Vector
   * @throws Exception
   */
  public Vector<StoricoParticellaVO> searchListParticelleByParameters(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda)
      throws Exception
  {
    return gestioneTerreniLocal.searchListParticelleByParameters(
        filtriParticellareRicercaVO, idAzienda);
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
    return gestioneTerreniLocal
        .findStoricoParticellaVOByIdConduzioneParticella(idConduzioneParticella);
  }
  
  
  public Long findIdUteByIdCondPartIdAz(Long idConduzioneParticella, Long idAzienda) throws Exception{
	  return gestioneTerreniLocal.findIdUteByIdCondPartIdAz(idConduzioneParticella, idAzienda);
  }
  
  

  public long getIdConduzioneDichiarata(long idConduzioneParticella,
      long idDichiarazioneConsistenza) throws Exception
  {
    return gestioneTerreniLocal.getIdConduzioneDichiarata(
        idConduzioneParticella, idDichiarazioneConsistenza);
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
    return gestioneTerreniLocal
        .findStoricoParticellaVOByIdConduzioneDichiarata(idConduzioneDichiarata);
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
    return gestioneTerreniLocal
        .findConduzioneParticellaByPrimaryKey(idConduzioneParticella);
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
    return gestioneTerreniLocal
        .findConduzioneDichiarataByPrimaryKey(idConduzioneDichiarata);
  }

  /**
   * Metodo che si occupa di reperire il dettaglio dei dati relativi alla
   * particella a partire dall'id_conduzione_particella/dichiarata
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
    return gestioneTerreniLocal.getDettaglioParticella(
        filtriParticellareRicercaVO, idConduzione);
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
    return gestioneTerreniLocal.getListPianteConsociate(onlyActive);
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
    return gestioneTerreniLocal
        .getListUtilizzoParticellaVOByIdConduzioneParticella(
            idConduzioneParticella, orderBy, onlyActive);
  }

  /**
   * Metodo che mi restituisce l'elenco degli utilizzi a partire
   * dall'id_dichiarazione_consistenza e dell'id_conduzione_particella in modo
   * da reperire solo quelli della particella selezionata
   * 
   * @param idDichiarazioneConsistenza
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.UtilizzoDichiaratoVO[]
   * @throws Exception
   */
  public UtilizzoDichiaratoVO[] getListUtilizzoDichiaratoVOByIdDichiarazioneConsistenzaAndIdConduzioneParticella(
      Long idDichiarazioneConsistenza, Long idConduzioneParticella,
      String[] orderBy) throws Exception
  {
    return gestioneTerreniLocal
        .getListUtilizzoDichiaratoVOByIdDichiarazioneConsistenzaAndIdConduzioneParticella(
            idDichiarazioneConsistenza, idConduzioneParticella, orderBy);
  }

  /**
   * Metodo che mi restituisce la particella certificata presente su
   * DB_PARTICELLA_CERTIFICATA in relazione alla chiave logica(comune, sezione,
   * foglio, particella, subalterno) + la nuova eleggibilita fittizia
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
    return gestioneTerreniLocal
        .findParticellaCertificataByParametersNewElegFit(istatComune, sezione,
            foglio, particella, subalterno, onlyActive,
            dataDichiarazioneConsistenza);
  }
  
  public ParticellaCertificataVO findParticellaCertificataAllaDichiarazione(
      Long idParticella, ConsistenzaVO consistenzaVO) throws Exception
  {
    return gestioneTerreniLocal.findParticellaCertificataAllaDichiarazione(
        idParticella, consistenzaVO);
  }
  
  public ParticellaCertificataVO findParticellaCertificataByIdParticella(Long idParticella, 
      Date dataDichiarazioneConsistenza) throws Exception
  {
    return gestioneTerreniLocal.findParticellaCertificataByIdParticella(idParticella, 
        dataDichiarazioneConsistenza);
  }
  
  public Date getDataFotoInterpretazione(long idParticellaCertificata, Date dataRichiestaRiesame)
    throws Exception
  {
    return gestioneTerreniLocal.getDataFotoInterpretazione(idParticellaCertificata, 
        dataRichiestaRiesame);
  }
  
  /*public Date getDataRichiestaRiesameAlaDich(long idStoricoParticella, long idDichiarazioneConsistenza)
    throws Exception
  {
    return gestioneTerreniLocal.getDataRichiestaRiesameAlaDich(idStoricoParticella, 
        idDichiarazioneConsistenza);
  }*/
  
  public Vector<ParticellaCertElegVO> getEleggibilitaByIdParticella(long idParticella)
    throws Exception
  {
    return gestioneTerreniLocal.getEleggibilitaByIdParticella(idParticella);
  }
  
  public Vector<Vector<ParticellaCertElegVO>> getListStoricoParticellaCertEleg(long idParticella)
    throws Exception
  {
    return gestioneTerreniLocal.getListStoricoParticellaCertEleg(idParticella);
  }
  
  public HashMap<Long,Vector<SuperficieDescription>> getEleggibilitaTooltipByIdParticella(
      Vector<Long> listIdParticella)
  throws Exception
  {
    return gestioneTerreniLocal.getEleggibilitaTooltipByIdParticella(listIdParticella);
  }
  
  public Vector<ProprietaCertificataVO> getListProprietaCertifByIdParticella(long idParticella) 
      throws Exception
  {
    return gestioneTerreniLocal.getListProprietaCertifByIdParticella(idParticella);
  }
  
  public Vector<ProprietaCertificataVO> getListDettaglioProprietaCertifByIdParticella(
    long idParticella, Date dataInserimentoValidazione) 
      throws Exception
  {
    return gestioneTerreniLocal.getListDettaglioProprietaCertifByIdParticella(
        idParticella, dataInserimentoValidazione);
  }

  /**
   * Metodo che mi restituisce la particella certificata presente su
   * DB_PARTICELLA_CERTIFICATA in relazione alla chiave logica(comune, sezione,
   * foglio, particella, subalterno)
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
    return gestioneTerreniLocal.findParticellaCertificataByParameters(
        istatComune, sezione, foglio, particella, subalterno, onlyActive,
        dataDichiarazioneConsistenza);
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
    return gestioneTerreniLocal.getListTipoVarietaByIdUtilizzo(idUtilizzo,
        onlyActive);
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
    return gestioneTerreniLocal
        .getDettaglioParticellaByIdConduzioneAndIdUtilizzo(
            filtriParticellareRicercaVO, idConduzione, idUtilizzo, idAzienda);
  }

  public BigDecimal getSumSuperficieUtilizzoUsoAgronomico(
      long idConduzioneParticella) throws Exception
  {
    return gestioneTerreniLocal
        .getSumSuperficieUtilizzoUsoAgronomico(idConduzioneParticella);
  }
  
  public BigDecimal getSumSuperficieUtilizzoUsoAgronomicoParticella(long idParticella, long idAzienda) 
      throws Exception
  {
    return gestioneTerreniLocal.getSumSuperficieUtilizzoUsoAgronomicoParticella(idParticella, idAzienda);
  }
  
  public BigDecimal getSumSuperficieAgronomicaAltreconduzioni(long idParticella, long idConduzioneParticella, long idAzienda) 
      throws Exception
  {
    return gestioneTerreniLocal.getSumSuperficieAgronomicaAltreconduzioni(idParticella, idConduzioneParticella, idAzienda);
  }

  public BigDecimal getSumSuperficieFromParticellaAndLastDichCons(
      long idParticella, Long idAzienda, boolean flagEscludiAzienda)
      throws Exception
  {
    return gestioneTerreniLocal.getSumSuperficieFromParticellaAndLastDichCons(
        idParticella, idAzienda, flagEscludiAzienda);
  }

  public String[] getIstatProvFromConduzione(long idAzienda)
      throws Exception
  {
    return gestioneTerreniLocal.getIstatProvFromConduzione(idAzienda);
  }
  
  public BigDecimal getPercentualePosesso(long idAzienda, long idParticella)
    throws Exception
  {
    return gestioneTerreniLocal.getPercentualePosesso(idAzienda, idParticella);
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
    return gestioneTerreniLocal.getListTipiUsoSuoloByIdIndirizzoUtilizzo(
        idIndirizzoUtilizzo, onlyActive, orderBy, colturaSecondaria);
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
    return gestioneTerreniLocal.getListTipiUsoSuoloByCodice(codice,
        onlyActive, orderBy, colturaSecondaria, flagPrincipale);
  }
  
  public TipoVarietaVO[] getListTipoVarietaByIdUtilizzoAndCodice(
      Long idUtilizzo, String codiceVarieta, boolean onlyActive, 
      String[] orderBy) throws Exception
  {
    return gestioneTerreniLocal.getListTipoVarietaByIdUtilizzoAndCodice(
        idUtilizzo, codiceVarieta, onlyActive, orderBy);
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
    gestioneTerreniLocal.associaUso(idAzienda ,elencoParticelle, ruoloUtenza);
  }
  
  public void associaUsoEleggibilitaGis(long idAzienda, StoricoParticellaVO[] elencoParticelle,
      RuoloUtenza ruoloUtenza) throws Exception
  {
    gestioneTerreniLocal.associaUsoEleggibilitaGis(idAzienda, elencoParticelle, ruoloUtenza);
  }

  /**
   * 
   * Metodo utilizzato nella modifica multipla per aggiornare su
   * DB_STORICO_PARTICELLA i dati dell'irrigazione
   * 
   * @param elencoStoricoParticella
   * @param ruoloUtenza
   * @param flagIrrigabile
   * @param idIrrigazione
   * @throws Exception
   */
  public void aggiornaIrrigazione(
      StoricoParticellaVO[] elencoStoricoParticella, RuoloUtenza ruoloUtenza,
      boolean flagIrrigabile, Long idIrrigazione) throws Exception
  {
    gestioneTerreniLocal.aggiornaIrrigazione(elencoStoricoParticella,
        ruoloUtenza, flagIrrigabile, idIrrigazione);
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
    gestioneTerreniLocal.cambiaTitoloPossesso(elencoIdConduzioneParticella,
        ruoloUtenza, idAzienda, idTitoloPossesso);
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
    return gestioneTerreniLocal
        .getMaxDataInizioConduzioneParticella(elencoConduzioni);
  }

  /**
   * Metodo utilizzato per effettuare la cessazione delle particelle
   * 
   * @param elencoIdConduzioneParticella
   * @param ruoloUtenza
   * @param idAzienda
   * @param dataCessazione
   * @throws Exception
   */
  public void cessaParticelle(Long[] elencoIdConduzioneParticella,
      long idUtenteAggiornamento, Long idAzienda, java.util.Date dataCessazione, String provenienza)
      throws Exception
  {
    gestioneTerreniLocal.cessaParticelle(elencoIdConduzioneParticella,
        idUtenteAggiornamento, idAzienda, dataCessazione, provenienza);
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
    gestioneTerreniLocal.cambiaUte(elencoIdConduzioneParticella, ruoloUtenza,
        idUte, idAzienda);
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
    gestioneTerreniLocal.associaDocumento(elencoIdConduzioneParticella,
        idDocumento);
  }

  /**
   * Metodo che mi restituisce l'elenco dei titoli di possesso in relazione al
   * criterio di ordinamento indicato dall'utente
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
  public CodeDescription[] getListTipiTitoloPossesso(String orderBy)
      throws Exception
  {
    return anagDenominazioniLocal.getListTipiTitoloPossesso(orderBy);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco dei casi particolari
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoCasoParticolare(
      String orderBy) throws Exception
  {
    return anagDenominazioniLocal.getListTipoCasoParticolare(orderBy);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco delle zone altimetriche
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoZonaAltimetrica(
      String orderBy) throws Exception
  {
    return anagDenominazioniLocal.getListTipoZonaAltimetrica(orderBy);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco delle area A
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaA(String orderBy)
      throws Exception
  {
    return anagDenominazioniLocal.getListTipoAreaA(orderBy);
  }
  
  /**
   * Metodo utilizzato per estrarre l'elenco delle area A
   * con data fine validità a null
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
  public it.csi.solmr.dto.CodeDescription[] getValidListTipoAreaA(String orderBy)
      throws Exception
  {
    return anagDenominazioniLocal.getValidListTipoAreaA(orderBy);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco delle area B
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaB(String orderBy)
      throws Exception
  {
    return anagDenominazioniLocal.getListTipoAreaB(orderBy);
  }
  
  /**
   * Metodo utilizzato per estrarre l'elenco delle area B
   * con data fine validità a null
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
  public it.csi.solmr.dto.CodeDescription[] getValidListTipoAreaB(String orderBy)
      throws Exception
  {
    return anagDenominazioniLocal.getValidListTipoAreaB(orderBy);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco delle area C
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaC(String orderBy)
      throws Exception
  {
    return anagDenominazioniLocal.getListTipoAreaC(orderBy);
  }
  
  /**
   * Metodo utilizzato per estrarre l'elenco delle area C
   * con data fine validità a null
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
  public it.csi.solmr.dto.CodeDescription[] getValidListTipoAreaC(String orderBy)
      throws Exception
  {
    return anagDenominazioniLocal.getValidListTipoAreaC(orderBy);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco delle area D
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaD(String orderBy)
      throws Exception
  {
    return anagDenominazioniLocal.getListTipoAreaD(orderBy);
  }
  
  public CodeDescription[] getListTipoAreaM(String orderBy) 
      throws Exception
  {
    return anagDenominazioniLocal.getListTipoAreaM(orderBy);
  }
  
  /**
   * Metodo utilizzato per estrarre l'elenco delle area D
   * con data fine validità a null
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
  public it.csi.solmr.dto.CodeDescription[] getValidListTipoAreaD(String orderBy)
      throws Exception
  {
    return anagDenominazioniLocal.getValidListTipoAreaD(orderBy);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco delle area E
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaE(String orderBy)
      throws Exception
  {
    return anagDenominazioniLocal.getListTipoAreaE(orderBy);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco delle area F
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaF(String orderBy)
      throws Exception
  {
    return anagDenominazioniLocal.getListTipoAreaF(orderBy);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco delle area PSN
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaPSN(String orderBy)
      throws Exception
  {
    return anagDenominazioniLocal.getListTipoAreaPSN(orderBy);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco dalla fascia fluviale
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoFasciaFluviale(
      String orderBy) throws Exception
  {
    return anagDenominazioniLocal.getListTipoFasciaFluviale(orderBy);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco dal Area G
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaG(String orderBy)
      throws Exception
  {
    return anagDenominazioniLocal.getListTipoAreaG(orderBy);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco dal Area H
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaH(String orderBy)
      throws Exception
  {
    return anagDenominazioniLocal.getListTipoAreaH(orderBy);
  }
  
  
  public it.csi.solmr.dto.CodeDescription[] getValidListTipoAreaM(String orderBy) 
      throws Exception
  {
    return anagDenominazioniLocal.getValidListTipoAreaM(orderBy);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco delle causali modifica particella
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoCausaleModParticella(
      String orderBy) throws Exception
  {
    return anagDenominazioniLocal.getListTipoCausaleModParticella(orderBy);
  }

  /**
   * Metodo che si occupa di reperire i dati dalla tabella DB_TIPO_IMPIANTO
   * 
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.TipoImpiantoVO[]
   * @throws Exception
   */
  public TipoImpiantoVO[] getListTipoImpianto(boolean onlyActive, String orderBy)
      throws Exception
  {
    return anagDenominazioniLocal.getListTipoImpianto(onlyActive, orderBy);
  }

  /**
   * Metodo che si occupa di reperire i dati dalla tabella
   * DB_TIPO_INDIRIZZO_UTILIZZO
   * 
   * @param colturaSecondaria
   * @param orderBy
   * @param onlyActive
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoIndirizzoUtilizzo(
      String colturaSecondaria, String orderBy, boolean onlyActive)
      throws Exception
  {
    return anagDenominazioniLocal.getListTipoIndirizzoUtilizzo(
        colturaSecondaria, orderBy, onlyActive);
  }

  /**
   * Metodo che si occupa di reperire i dati dalla tabella DB_TIPO_IRRIGAZIONE
   * 
   * @param orderBy
   * @param onlyActive
   * @return it.csi.solmr.dto.anag.terreni.TipoIrrigazioneVO[]
   * @throws Exception
   */
  public TipoIrrigazioneVO[] getListTipoIrrigazione(String orderBy,
      boolean onlyActive) throws Exception
  {
    return anagDenominazioniLocal.getListTipoIrrigazione(orderBy, onlyActive);
  }
  
  /**
   * Metodo che si occupa di reperire i dati dalla tabella DB_TIPO_POTENZIALITA_IRRIGUA
   * 
   * @param orderBy
   * @param onlyActive
   * @return it.csi.solmr.dto.anag.terreni.TipoPotenzialitaIrriguaVO[]
   * @throws DataAccessException
   */
  public TipoPotenzialitaIrriguaVO[] getListTipoPotenzialitaIrrigua(String orderBy,
      Date dataRiferiemento) throws Exception
  {
    return anagDenominazioniLocal.getListTipoPotenzialitaIrrigua(orderBy, dataRiferiemento);
  }
  
  /**
   * Metodo che si occupa di reperire i dati dalla tabella DB_TIPO_TERRAZZAMENTO
   * 
   * @param orderBy
   * @param onlyActive
   * @return it.csi.solmr.dto.anag.terreni.TipoTerrazzamentoVO[]
   * @throws DataAccessException
   */
  public TipoTerrazzamentoVO[] getListTipoTerrazzamento(String orderBy,
      Date dataRiferiemento) throws Exception
  {
    return anagDenominazioniLocal.getListTipoTerrazzamento(orderBy, dataRiferiemento);
  }
  
  /**
   * Metodo che si occupa di reperire i dati dalla tabella DB_TIPO_ROTAZIONE_COLTURALE
   * 
   * @param orderBy
   * @param onlyActive
   * @return it.csi.solmr.dto.anag.terreni.TipoRotazioneColturaleVO[]
   * @throws DataAccessException
   */
  public TipoRotazioneColturaleVO[] getListTipoRotazioneColturale(String orderBy,
      Date dataRiferiemento) throws Exception
  {
    return anagDenominazioniLocal.getListTipoRotazioneColturale(orderBy, dataRiferiemento);
  }
  
  //Fine territorialie **************************************

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
    return gestioneTerreniLocal.findTipoImpiantoByPrimaryKey(idImpianto);
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
    return gestioneTerreniLocal
        .getListUtilizziConsociatiByIdUtilizzoParticella(idUtilizzoParticella,
            orderBy);
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
      String colturaSecondaria) throws Exception
  {
    return gestioneTerreniLocal.findListTipiUsoSuoloByIdAzienda(idAzienda,
        colturaSecondaria);
  }

  public Vector<TipoUtilizzoVO> findListTipiUsoSuoloByIdAziendaCess(Long idAzienda,
      String[] orderBy) throws Exception
  {
    return gestioneTerreniLocal.findListTipiUsoSuoloByIdAziendaCess(idAzienda,
        orderBy);
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
    gestioneTerreniLocal.modificaParticelle(elencoStoricoParticella,
        ruoloUtenza, anagAziendaVO);
  }


  /**
   * Metodo che mi restituisce il Comune in funzione dei parametri, del suo
   * stato di vita e dello stato del catasto.
   * 
   * @param descComune
   * @param siglaProvincia
   * @param flagEstinto
   * @param flagCatastoAttivo
   * @param flagEstero
   * @return it.csi.solmr.dto.ComuneVO
   * @throws Exception
   */
  public ComuneVO getComuneByParameters(String descComune,
      String siglaProvincia, String flagEstinto, String flagCatastoAttivo,
      String flagEstero) throws Exception
  {
    return anagDenominazioniLocal.getComuneByParameters(descComune,
        siglaProvincia, flagEstinto, flagCatastoAttivo, flagEstero);
  }

  /**
   * Metodo che mi consente di reperire l'elenco dei comuni in funzione dei
   * parametri di ricerca associati
   * 
   * @param descComune
   * @param siglaProvincia
   * @param flagEstinto
   * @param flagCatastoAttivo
   * @param flagEstero
   * @param orderBy
   * @return java.util.Vector
   * @throws Exception
   * @throws SolmrException
   */
  public Vector<ComuneVO> getComuniByParameters(String descComune, String siglaProvincia,
      String flagEstinto, String flagCatastoAttivo, String flagEstero,
      String[] orderBy) throws Exception, SolmrException
  {
    return anagDenominazioniLocal.getComuniByParameters(descComune,
        siglaProvincia, flagEstinto, flagCatastoAttivo, flagEstero, orderBy);
  }
  
  public Vector<ComuneVO> getComuniAttiviByIstatProvincia(String istatProvincia) 
      throws Exception, SolmrException
  {
    return anagDenominazioniLocal.getComuniAttiviByIstatProvincia(istatProvincia);
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
    return gestioneTerreniLocal.searchParticelleExcelByParameters(
        filtriParticellareRicercaVO, idAzienda);
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
    return gestioneTerreniLocal.getListParticelleForDocument(
        storicoParticellaVO, anagAziendaVO, hasUnitToDocument, idAnomalia, orderBy);
  }

  public StoricoParticellaVO[] getListParticelleForDocument(
      StoricoParticellaVO storicoParticellaVO, AnagAziendaVO anagAziendaVO,
      long idDichiarazioneConsistenza, boolean hasUnitToDocument, Long idAnomalia, String orderBy)
      throws Exception
  {
    return gestioneTerreniLocal.getListParticelleForDocument(
        storicoParticellaVO, anagAziendaVO, idDichiarazioneConsistenza,
        hasUnitToDocument, idAnomalia, orderBy);
  }
  
  public StoricoParticellaVO[] getListParticelleForDocumentValoreC( 
      AnagAziendaVO anagAziendaVO, int anno, boolean hasUnitToDocument)
    throws Exception
  {
    return gestioneTerreniLocal
        .getListParticelleForDocumentValoreC(anagAziendaVO, anno, hasUnitToDocument);
  }
  
  public StoricoParticellaVO[] getListParticelleForDocumentExtraSistema( 
      AnagAziendaVO anagAziendaVO, int anno, boolean hasUnitToDocument) 
    throws Exception
  {
    return gestioneTerreniLocal
        .getListParticelleForDocumentExtraSistema(anagAziendaVO, anno, hasUnitToDocument);
  
  }
  
  public Vector<StoricoParticellaVO> getListParticelleUvBasic(
      FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO, AnagAziendaVO anagAziendaVO) 
    throws Exception
  {
    return gestioneTerreniLocal.getListParticelleUvBasic(filtriUnitaArboreaRicercaVO, anagAziendaVO);
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
    return gestioneTerreniLocal.findStoricoParticellaVOByParameters(
        istatComune, sezione, foglio, particella, subalterno,
        dataInizioValidita);
  }

  /**
   * Metodo che mi restituisce l'elenco dei tipi evento in relazione al criterio
   * di ordinamento indicato dall'utente
   * 
   * 
   * @return 
   * @throws Exception
   */
  public Vector<TipoEventoVO> getListTipiEvento()
      throws Exception
  {
    return anagDenominazioniLocal.getListTipiEvento();
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
    return gestioneTerreniLocal.getSezioneByParameters(istatComune, sezione);
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
    return gestioneTerreniLocal.findFoglioByParameters(istatComune, foglio,
        sezione);
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
    return gestioneTerreniLocal.getFogliByParameters(istatComune, sezione,
        foglio);
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
    return gestioneTerreniLocal.getListStoricoParticellaVOByParameters(
        istatComune, sezione, foglio, particella, subalterno, onlyActive,
        orderBy, idAzienda);
  }

  /**
   * Metodo che mi restituisce l'elenco dei records su DB_STORICO_PARTICELLA a
   * partire dalla chiave logica della particella(COMUNE, SEZIONE, FOGLIO,
   * PARTICELLA, SUBALTERNO) N.B. Sono esclusi i titoli di possesso asservimento
   * (5) e conferimento (6)
   * 
   * @param istatComune
   * @param sezione
   * @param foglio
   * @param particella
   * @param subalterno
   * @param onlyActive
   * @param orderBy
   * @param idAzienda
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws DataAccessException
   * @throws SolmrException
   */
  public StoricoParticellaVO[] getListStoricoParticellaVOByParametersImpUnar(
      String istatComune, String sezione, String foglio, String particella,
      String subalterno, long idAzienda) throws Exception, SolmrException
  {
    return gestioneTerreniLocal.getListStoricoParticellaVOByParametersImpUnar(
        istatComune, sezione, foglio, particella, subalterno, idAzienda);
  }

  /**
   * Metodo che mi restituisce il record di DB_STORICO_PARTICELLA a partire
   * dalla sua chiave primaria
   * 
   * @param idConduzioneParticella
   * @return it.csi.solmr.dto.StoricoParticellaVO
   * @throws Exception
   */
  public StoricoParticellaVO findStoricoParticellaByPrimaryKey(
      Long idStoricoParticella) throws Exception
  {
    return gestioneTerreniLocal
        .findStoricoParticellaByPrimaryKey(idStoricoParticella);
  }

  /**
   * Metodo che mi restituisce il record di DB_STORICO_PARTICELLA a partire
   * dall'idParticella
   * 
   * @param idParticella
   * @return it.csi.solmr.dto.StoricoParticellaVO
   * @throws Exception
   */
  public StoricoParticellaVO findCurrStoricoParticellaByIdParticella(
      Long idParticella) throws Exception
  {
    return gestioneTerreniLocal
        .findCurrStoricoParticellaByIdParticella(idParticella);
  }

  /**
   * Metodo che mi permette di inserire una particella territoriale associata ad
   * un'azienda
   * 
   * @param storicoParticellaVO
   * @param ruoloUtenza
   * @param elencoParticelleEvento
   * @param idEvento
   * @return java.lang.String
   * @throws Exception
   */
  public String inserisciParticella(StoricoParticellaVO storicoParticellaVO,
      RuoloUtenza ruoloUtenza, StoricoParticellaVO[] elencoParticelleEvento,
      Long idEvento, Long idAzienda, String[] arrVIdStoricoUnitaArborea, String[] arrVAreaUV) throws Exception
  {
    return gestioneTerreniLocal.inserisciParticella(storicoParticellaVO,
        ruoloUtenza, elencoParticelleEvento, idEvento, idAzienda, arrVIdStoricoUnitaArborea,
        arrVAreaUV);
  }

  /**
   * Metodo che mi permette di inserire una particella territoriale non
   * associata a nessuna azineda
   * 
   * @param storicoParticellaVO
   * @param ruoloUtenza
   * @param elencoParticelleEvento
   * @param idEvento
   * @return java.lang.String
   * @throws Exception
   */
  public String inserisciParticella(StoricoParticellaVO storicoParticellaVO,
      RuoloUtenza ruoloUtenza, StoricoParticellaVO[] elencoParticelleEvento,
      Long idEvento) throws Exception
  {
    return gestioneTerreniLocal.inserisciParticella(storicoParticellaVO,
        ruoloUtenza, elencoParticelleEvento, idEvento);
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
    return gestioneTerreniLocal.getTotSupCondottaByAziendaAndParticella(
        idAzienda, idParticella);
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
    return gestioneTerreniLocal
        .getEventiParticellaByIdParticellaNuovaOrCessata(idParticella, orderBy);
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
    return gestioneTerreniLocal.getListParticelleForFabbricato(idFabbricato,
        idPianoRiferimento, orderBy);
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
    return gestioneTerreniLocal.isPianoRiferimentoRipristinato(idAzienda);
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
    return gestioneTerreniLocal.getListStoricoUnitaArboreaByLogicKey(
        idParticella, idPianoRiferimento, idAzienda, orderBy);
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
    return gestioneTerreniLocal.getListStoricoUnitaArboreaByIdAzienda(
        idAzienda, idPianoRiferimento, orderBy);
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
    return gestioneTerreniLocal
        .getMaxDataEsecuzioneControlliUnitaArborea(idAzienda);
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
    return gestioneTerreniLocal
        .getListEsitoControlloUnarByIdStoricoUnitaArborea(
            idStoricoUnitaArborea, orderBy);
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

    StopWatch stopWatch = null;

    StoricoParticellaVO storicoParticellaVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[AnagFacadeBean::findStoricoParticellaArborea] BEGIN.");
      // Stopwatch
      stopWatch = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
      stopWatch.start();

      storicoParticellaVO = gestioneTerreniLocal
          .findStoricoParticellaArborea(idStoricoUnitaArborea);

      if (storicoParticellaVO.getStoricoUnitaArboreaVO() != null)
      {

        UtenteAbilitazioni utenteAbilitazioni = sianLocal
            .getUtenteAbilitazioniByIdUtenteLogin(storicoParticellaVO
                .getStoricoUnitaArboreaVO().getIdUtenteAggiornamento());
        
        RuoloUtenza ruoloUtenza = new RuoloUtenzaPapua(utenteAbilitazioni);
        UtenteIrideVO utenteIrideVO = new UtenteIrideVO();
        utenteIrideVO.setIdUtente(ruoloUtenza.getIdUtente());
        utenteIrideVO.setDenominazione(ruoloUtenza.getDenominazione());
        utenteIrideVO.setDescrizioneEnteAppartenenza(ruoloUtenza
            .getDescrizioneEnte());
        storicoParticellaVO.getStoricoUnitaArboreaVO().setUtenteIrideVO(
            utenteIrideVO);
      }

      return storicoParticellaVO;

    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[] { new Variabile(
          "storicoParticellaVO", storicoParticellaVO) };
      Parametro parametri[] = new Parametro[] { new Parametro(
          "idStoricoUnitaArborea", idStoricoUnitaArborea) };
      LoggerUtils.logEJBError(this,
          "[AnagFacadeBean::findStoricoParticellaArborea]", t, variabili,
          parametri);
      throw new Exception(t.getMessage());
    }
    finally
    {
      try
      {
        stopWatch.dumpElapsed("AnagFacadeBean", "findStoricoParticellaArborea",
            "Metodo della facade", "");
        stopWatch.stop();
      }
      catch (Exception ex)
      {
        // Nessuna operazione
      }
      SolmrLogger.debug(this,
          "[AnagFacadeBean::findStoricoParticellaArborea] END.");
    }
  }
  
  public StoricoParticellaVO findStoricoParticellaArboreaBasic(
      Long idStoricoUnitaArborea) throws Exception
  {
    return gestioneTerreniLocal.findStoricoParticellaArboreaBasic(idStoricoUnitaArborea);
  }
  
  
  public StoricoParticellaVO findStoricoParticellaArboreaConduzione(
      Long idStoricoUnitaArborea, long idAzienda) throws Exception
  {
    return gestioneTerreniLocal.findStoricoParticellaArboreaConduzione(idStoricoUnitaArborea, idAzienda);
  }
  
  public StoricoParticellaVO findStoricoParticellaArboreaTolleranza(
      Long idStoricoUnitaArborea, long idAzienda, String nomeLib) throws Exception
  {

    StopWatch stopWatch = null;

    StoricoParticellaVO storicoParticellaVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[AnagFacadeBean::findStoricoParticellaArborea] BEGIN.");
      // Stopwatch
      stopWatch = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
      stopWatch.start();

      storicoParticellaVO = gestioneTerreniLocal
          .findStoricoParticellaArboreaTolleranza(idStoricoUnitaArborea, idAzienda, nomeLib);

      if (storicoParticellaVO.getStoricoUnitaArboreaVO() != null)
      {

                
        UtenteAbilitazioni utenteAbilitazioni = sianLocal
            .getUtenteAbilitazioniByIdUtenteLogin(storicoParticellaVO
                .getStoricoUnitaArboreaVO().getIdUtenteAggiornamento());
        
        RuoloUtenza ruoloUtenza = new RuoloUtenzaPapua(utenteAbilitazioni);
        UtenteIrideVO utenteIrideVO = new UtenteIrideVO();
        utenteIrideVO.setIdUtente(ruoloUtenza.getIdUtente());
        utenteIrideVO.setDenominazione(ruoloUtenza.getDenominazione());
        utenteIrideVO.setDescrizioneEnteAppartenenza(ruoloUtenza
            .getDescrizioneEnte());
        storicoParticellaVO.getStoricoUnitaArboreaVO().setUtenteIrideVO(
            utenteIrideVO);
      }

      return storicoParticellaVO;

    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[] { new Variabile(
          "storicoParticellaVO", storicoParticellaVO) };
      Parametro parametri[] = new Parametro[] { new Parametro(
          "idStoricoUnitaArborea", idStoricoUnitaArborea) };
      LoggerUtils.logEJBError(this,
          "[AnagFacadeBean::findStoricoParticellaArborea]", t, variabili,
          parametri);
      throw new Exception(t.getMessage());
    }
    finally
    {
      try
      {
        stopWatch.dumpElapsed("AnagFacadeBean", "findStoricoParticellaArborea",
            "Metodo della facade", "");
        stopWatch.stop();
      }
      catch (Exception ex)
      {
        // Nessuna operazione
      }
      SolmrLogger.debug(this,
          "[AnagFacadeBean::findStoricoParticellaArborea] END.");
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
    return gestioneTerreniLocal.getListAltroVitignoByIdStoricoUnitaArborea(
        idStoricoUnitaArborea, orderBy);
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
    return gestioneTerreniLocal.getListTipiUsoSuoloByTipo(tipo, onlyActive,
        orderBy);
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
    return gestioneTerreniLocal.getListTipoFormaAllevamento(onlyActive,
        orderBy);
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
    return gestioneTerreniLocal
        .getListTipoCausaleModifica(onlyActive, orderBy);
  }
  
  public Vector<TipoCausaleModificaVO> getListTipoCuasaleModificaByIdAzienda(long idAzienda)
    throws Exception
  {
    return gestioneTerreniLocal.getListTipoCuasaleModificaByIdAzienda(idAzienda);
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
    gestioneTerreniLocal.modificaUnitaArboree(elencoParticelleArboree,
        ruoloUtenza, provenienza);
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
   * @throws SolmrException
   */
  public void cessaUnitaArboree(Long[] elencoIdStoricoUnitaArboree,
      RuoloUtenza ruoloUtenza, Long idCausaleModifica, String note) throws Exception,
      SolmrException
  {
    gestioneTerreniLocal.cessaUnitaArboree(elencoIdStoricoUnitaArboree,
        ruoloUtenza, idCausaleModifica, note);
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
    return gestioneTerreniLocal.getListTipoCessazioneUnar(onlyActive, orderBy);
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
    return gestioneTerreniLocal.getListStoricoParticelleArboreeImportabili(
        idAzienda, orderBy);
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
    gestioneTerreniLocal.importUnitaArboreeBySchedario(
        elencoIdStoricoUnitaArborea, ruoloUtenza, anagAziendaVO,
        idCausaleModifica, newIdParticella);
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
    gestioneTerreniLocal.inserisciUnitaArborea(storicoUnitaArboreaVO,
        storicoParticellaVO, anagAziendaVO, ruoloUtenza, provenienza);
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
  public StoricoParticellaVO[] searchStoricoUnitaArboreaByParametersForStampa(
      Long idAzienda, FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO,
      String[] orderBy) throws Exception
  {
    return gestioneTerreniLocal.searchStoricoUnitaArboreaByParametersForStampa(
        idAzienda, filtriUnitaArboreaRicercaVO, orderBy);
  }
  
  public StoricoParticellaVO[] searchStoricoUnitaArboreaByParameters(String nomeLib,
      Long idAzienda, FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO,
      String[] orderBy) throws Exception
  {
    return gestioneTerreniLocal.searchStoricoUnitaArboreaByParameters(nomeLib,
        idAzienda, filtriUnitaArboreaRicercaVO, orderBy);
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
  public Vector<StoricoParticellaArboreaExcelVO> searchStoricoUnitaArboreaExcelByParameters(
      String nomeLib, Long idAzienda, FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO) throws Exception
  {
    return gestioneTerreniLocal.searchStoricoUnitaArboreaExcelByParameters(nomeLib,
        idAzienda, filtriUnitaArboreaRicercaVO);
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
    return gestioneTerreniLocal.getListConduzioneParticellaByIdAzienda(
        idAzienda, onlyActive, orderBy);
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
    gestioneTerreniLocal.importParticelle(elencoConduzioni, idUte,
        anagAziendaVO, anagAziendaSearchVO, ruoloUtenza, idTitoloPossesso);
  }

  /**
   * Metodo utilizzato per importare le particelle in asservimento da un'altra
   * azienda
   * 
   * @param elencoConduzioni
   * @param idUte
   * @param anagAziendaVO
   * @param anagAziendaSearchVO
   * @param ruoloUtenza
   * @throws Exception
   */
  public Vector<Object> importParticelleAsservite(String[] elencoConduzioni,
      Long idUte, AnagAziendaVO anagAziendaSearchVO, RuoloUtenza ruoloUtenza,
      Long idTitoloPossesso) throws Exception
  {
    return gestioneTerreniLocal.importParticelleAsservite(elencoConduzioni,
        idUte, anagAziendaSearchVO, ruoloUtenza,
        idTitoloPossesso);
  }

  /**
   * Metodo utilizzato per importare le particelle in asservimento dalla ricerca
   * Particelle, quindi partendo da altre particelle.
   * 
   * @param elencoIdParticelle
   * @param idUte
   * @param ruoloUtenza
   * @throws Exception
   * @throws SolmrException
   */
  public Vector<Object> importParticelleAsserviteFromRicercaParticella(
      String[] elencoIdParticelle, Long idUte, AnagAziendaVO anagAziendaVO,
      RuoloUtenza ruoloUtenza, Long idTitoloPossesso) throws Exception
  {
    return gestioneTerreniLocal
        .importParticelleAsserviteFromRicercaParticella(elencoIdParticelle,
            idUte, anagAziendaVO, ruoloUtenza, idTitoloPossesso);
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
    return gestioneTerreniLocal.getListStoricoParticelleArboreeByParameters(
        istatComune, sezione, foglio, particella, subalterno, onlyActive,
        orderBy);
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
    StopWatch stopWatch = null;

    StoricoParticellaVO storicoParticellaVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[AnagFacadeBean::findParticellaArboreaDichiarata] BEGIN.");
      // Stopwatch
      stopWatch = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
      stopWatch.start();

      storicoParticellaVO = gestioneTerreniLocal
          .findParticellaArboreaDichiarata(idUnitaArboreaDichiarata);

      if (storicoParticellaVO.getUnitaArboreaDichiarataVO() != null)
      {
        
        UtenteAbilitazioni utenteAbilitazioni = sianLocal
            .getUtenteAbilitazioniByIdUtenteLogin(storicoParticellaVO
                .getUnitaArboreaDichiarataVO().getIdUtenteAggiornamento());
        
        RuoloUtenza ruoloUtenza = new RuoloUtenzaPapua(utenteAbilitazioni);
        
        UtenteIrideVO utenteIrideVO = new UtenteIrideVO();
        utenteIrideVO.setIdUtente(ruoloUtenza.getIdUtente());
        utenteIrideVO.setDenominazione(ruoloUtenza.getDenominazione());
        utenteIrideVO.setDescrizioneEnteAppartenenza(ruoloUtenza
            .getDescrizioneEnte());
        storicoParticellaVO.getUnitaArboreaDichiarataVO().setUtenteIrideVO(
            utenteIrideVO);
      }

      return storicoParticellaVO;

    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[] { new Variabile(
          "storicoParticellaVO", storicoParticellaVO) };
      Parametro parametri[] = new Parametro[] { new Parametro(
          "idStoricoUnitaArborea", idUnitaArboreaDichiarata) };
      LoggerUtils.logEJBError(this,
          "[AnagFacadeBean::findParticellaArboreaDichiarata]", t, variabili,
          parametri);
      throw new Exception(t.getMessage());
    }
    finally
    {
      try
      {
        stopWatch.dumpElapsed("AnagFacadeBean",
            "findParticellaArboreaDichiarata", "Metodo della facade", "");
        stopWatch.stop();
      }
      catch (Exception ex)
      {
        // Nessuna operazione
      }
      SolmrLogger.debug(this,
          "[AnagFacadeBean::findParticellaArboreaDichiarata] END.");
    }
  }
  
  public StoricoParticellaVO findParticellaArboreaDichiarataBasic(Long idUnitaArboreaDichiarata)
      throws Exception
  {
    return gestioneTerreniLocal.findParticellaArboreaDichiarataBasic(idUnitaArboreaDichiarata);
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
    return gestioneTerreniLocal
        .getListAltroVitignoDichiaratoByIdUnitaArboreaDichiarata(
            idUnitaArboreaDichiarata, orderBy);
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
    gestioneTerreniLocal.ribaltaUVOnPianoColturale(idAzienda, idStoricoUnitaArborea, idUtente);
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
    return gestioneTerreniLocal.getListTipoVino(onlyActive, orderBy);
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
    return gestioneTerreniLocal.getListTipoTipologiaVino(onlyActive, orderBy);
  }

  public TipoTipologiaVinoVO[] getListTipoTipologiaVinoForAzienda(long idAzienda)
      throws Exception
  {
    return gestioneTerreniLocal.getListTipoTipologiaVinoForAzienda(idAzienda);
  }
  
  public TipoTipologiaVinoVO[] getListTipoTipologiaVinoForDichCons(long idDichiarazioneConsistenza)
      throws Exception
  {
    return gestioneTerreniLocal.getListTipoTipologiaVinoForDichCons(idDichiarazioneConsistenza);
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
    return gestioneTerreniLocal.findTipoUtilizzoByPrimaryKey(idUtilizzo);
  }
  
  public TipoUtilizzoVO[] getListTipoUtilizzoByIdAzienda(String tipo, long idAzienda)
    throws Exception
  {
    return gestioneTerreniLocal.getListTipoUtilizzoByIdAzienda(tipo, idAzienda);
  }
  
  public TipoUtilizzoVO[] getListTipoUtilizzoByIdDichiarazioneConsistenza(String tipo, long idDichiarazioneConsistenza)
      throws Exception
  {
    return gestioneTerreniLocal.getListTipoUtilizzoByIdDichiarazioneConsistenza(tipo, idDichiarazioneConsistenza);
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
    return gestioneTerreniLocal.findTipoVarietaByPrimaryKey(idVarieta);
  }
  
  public TipoVarietaVO[] getListTipoVarietaByIdAzienda(long idAzienda)
    throws Exception
  {
    return gestioneTerreniLocal.getListTipoVarietaByIdAzienda(idAzienda);
  }
  
  public TipoVarietaVO[] getListTipoVarietaByIdDichiarazioneConsistenza(long idDichiarazioneConsistenza)
      throws Exception
  {
    return gestioneTerreniLocal.getListTipoVarietaByIdDichiarazioneConsistenza(idDichiarazioneConsistenza);
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
    return gestioneTerreniLocal.getListTipoTipologiaVinoByIdVino(idVino,
        onlyActive, orderBy);
  }

  public Vector<TipoTipologiaVinoVO> getListActiveTipoTipologiaVinoByComune(
      String istatComune) throws Exception
  {
    return gestioneTerreniLocal
        .getListActiveTipoTipologiaVinoByComune(istatComune);
  }
  
  public Vector<TipoTipologiaVinoVO> getListActiveTipoTipologiaVinoByComuneAndVarieta(
      String istatComune, Long idVarieta, Long idParticella) throws Exception
  {
    return gestioneTerreniLocal
        .getListActiveTipoTipologiaVinoByComuneAndVarieta(istatComune, idVarieta, idParticella);
  }
  
  /*public Vector<VignaVO> getListActiveVignaByIdTipologiaVinoAndParticella(long idTipologiaVino, long idParticella)
      throws Exception
  {
    return gestioneTerreniRemote
        .getListActiveVignaByIdTipologiaVinoAndParticella(idTipologiaVino, idParticella);
  }*/
  
  public Vector<TipoTipologiaVinoVO> getListTipoTipologiaVinoRicaduta(
      String istatComune, long idVarieta, long idTipologiaVino, java.util.Date dataInserimentoDichiarazione) 
  throws Exception
  {
    return gestioneTerreniLocal
        .getListTipoTipologiaVinoRicaduta(istatComune, idVarieta, idTipologiaVino, dataInserimentoDichiarazione);
  }
  
  public TipoTipologiaVinoVO getTipoTipologiaVinoByPrimaryKey(long idTipologiaVino) 
    throws Exception
  {
    return gestioneTerreniLocal
        .getTipoTipologiaVinoByPrimaryKey(idTipologiaVino);
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
    gestioneTerreniLocal.dichiaraUsoAgronomico(elencoParticelle, ruoloUtenza,
        idAzienda);
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
    gestioneTerreniLocal.validaUVPlSql(elencoIdUnitaArboreaDichiarata);
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
    return gestioneTerreniLocal.riepilogoTitoloPossesso(idAzienda);
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
    return gestioneTerreniLocal.riepilogoTitoloPossessoDichiarato(idAzienda,
        idDichiarazioneConsistenza);
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
    return gestioneTerreniLocal.riepilogoPossessoComune(idAzienda);
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
    return gestioneTerreniLocal.riepilogoPossessoComuneDichiarato(idAzienda,
        idDichiarazioneConsistenza);
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
    return gestioneTerreniLocal.riepilogoComune(idAzienda, escludiAsservimento);
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
    return gestioneTerreniLocal.riepilogoComuneDichiarato(idAzienda,
        escludiAsservimento, idDichiarazioneConsistenza);
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
    return gestioneTerreniLocal.riepilogoUsoPrimario(idAzienda);
  }
  
  public BigDecimal getTotSupSfriguAndAsservimento(Long idAzienda, String escludiAsservimento)
      throws Exception
  {
    return gestioneTerreniLocal.getTotSupSfriguAndAsservimento(idAzienda, escludiAsservimento);
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
    return gestioneTerreniLocal.riepilogoUsoPrimarioDichiarato(idAzienda,
        idDichiarazioneConsistenza);
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
    return gestioneTerreniLocal.riepilogoUsoSecondario(idAzienda);
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
    return gestioneTerreniLocal.riepilogoUsoSecondarioDichiarato(idAzienda,
        idDichiarazioneConsistenza);
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
    return gestioneTerreniLocal.riepilogoMacroUso(idAzienda);
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
    return gestioneTerreniLocal.riepilogoMacroUsoDichiarato(idAzienda,
        idDichiarazioneConsistenza);
  }
  
  public BigDecimal getTotSupAsservimento(Long idAzienda, Long idDichiarazioneConsistenza)
    throws Exception
  {
    return gestioneTerreniLocal.getTotSupAsservimento(idAzienda, idDichiarazioneConsistenza);
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
    return gestioneTerreniLocal.getListTipoMacroUsoByIdAzienda(idAzienda,
        onlyActive, orderBy);
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
    return gestioneTerreniLocal.riepilogoZVNParticellePiemonte(idAzienda,
        escludiAsservimento);
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
    return gestioneTerreniLocal.riepilogoZVNParticelleDichiaratePiemonte(
        idAzienda, escludiAsservimento, idDichiarazioneConsistenza);
  }

  /**
   * Metodo per effettuare il riepilogo delle particelle per ZVN comprensivo di
   * fascia fluviale
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoZVNFasciaFluviale(Long idAzienda,
      String escludiAsservimento) throws Exception
  {
    return gestioneTerreniLocal.riepilogoZVNFasciaFluviale(idAzienda,
        escludiAsservimento);
  }

  /**
   * Metodo per effettuare il riepilogo delle particelle per ZVN comprensivo di
   * fascia fluviale ad una determiinata dichiarazione di consistenza
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoZVNFasciaFluvialeDichiarate(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws Exception
  {
    return gestioneTerreniLocal.riepilogoZVNFasciaFluvialeDichiarate(
        idAzienda, escludiAsservimento, idDichiarazioneConsistenza);
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
    return gestioneTerreniLocal.riepilogoZVFParticellePiemonte(idAzienda,
        escludiAsservimento);
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
    return gestioneTerreniLocal.riepilogoZVFParticelleDichiaratePiemonte(
        idAzienda, escludiAsservimento, idDichiarazioneConsistenza);
  }

  /**
   * Metodo per effettuare il riepilogo delle particelle per Localizzazione solo
   * dei comuni TOBECONFIG
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoLocalizzazioneParticellePiemonte(
      Long idAzienda, String escludiAsservimento) throws Exception
  {
    return gestioneTerreniLocal.riepilogoLocalizzazioneParticellePiemonte(
        idAzienda, escludiAsservimento);
  }

  /**
   * Metodo per effettuare il riepilogo delle particelle per Localizzazione solo
   * dei comuni TOBECONFIG ad una determinata dichiarazione di consistenza
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
    return gestioneTerreniLocal
        .riepilogoLocalizzazioneParticelleDichiaratePiemonte(idAzienda,
            escludiAsservimento, idDichiarazioneConsistenza);
  }

  /**
   * Metodo per effettuare il riepilogo delle particelle per Fascia Fluviale
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoFasciaFluvialeParticelle(
      Long idAzienda, String escludiAsservimento) throws Exception
  {
    return gestioneTerreniLocal.riepilogoFasciaFluvialeParticelle(idAzienda,
        escludiAsservimento);
  }

  /**
   * Metodo per effettuare il riepilogo delle particelle per ad una determinata
   * dichiarazione di consistenza
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
    return gestioneTerreniLocal.riepilogoFasciaFluvialeParticelleDichiarate(
        idAzienda, escludiAsservimento, idDichiarazioneConsistenza);
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
      String escludiAsservimento) throws Exception
  {
    return gestioneTerreniLocal.riepilogoAreaG(idAzienda, escludiAsservimento);
  }

  /**
   * 
   * Metodo per effettuare il riepilogo delle particelle per basso contenuto
   * carbonio organico ad una determiinata dichiarazione di consistenza
   * 
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @return
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoAreaGParticelleDichiarate(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws Exception
  {
    return gestioneTerreniLocal.riepilogoAreaGParticelleDichiarate(idAzienda,
        escludiAsservimento, idDichiarazioneConsistenza);
  }

  /**
   * 
   * Metodo per effettuare il riepilogo delle particelle con area soggeto ad
   * erosione
   * 
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @return
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoAreaH(Long idAzienda,
      String escludiAsservimento) throws Exception
  {
    return gestioneTerreniLocal.riepilogoAreaH(idAzienda, escludiAsservimento);
  }

  /**
   * 
   * Metodo per effettuare il riepilogo delle particelle per area soggetta ad
   * erosione ad una determiinata dichiarazione di consistenza
   * 
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @return
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoAreaHParticelleDichiarate(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws Exception
  {
    return gestioneTerreniLocal.riepilogoAreaHParticelleDichiarate(idAzienda,
        escludiAsservimento, idDichiarazioneConsistenza);
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
      String escludiAsservimento) throws Exception
  {
    return gestioneTerreniLocal.riepilogoZonaAltimetrica(idAzienda,
        escludiAsservimento);
  }

  /**
   * Metodo per effettuare il riepilogo delle particelle per Zona Altimetrica ad
   * una determiinata dichiarazione di consistenza
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @return
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoZonaAltimetricaParticelleDichiarate(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws Exception
  {

    return gestioneTerreniLocal.riepilogoZonaAltimetricaParticelleDichiarate(
        idAzienda, escludiAsservimento, idDichiarazioneConsistenza);
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
      String escludiAsservimento) throws Exception
  {
    return gestioneTerreniLocal.riepilogoCasoParticolare(idAzienda,
        escludiAsservimento);
  }

  /**
   * 
   * Metodo per effettuare il riepilogo delle particelle per Caso Particolare ad
   * una determiinata dichiarazione di consistenza
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @return
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoCasoParticolareParticelleDichiarate(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws Exception
  {
    return gestioneTerreniLocal.riepilogoCasoParticolareParticelleDichiarate(
        idAzienda, escludiAsservimento, idDichiarazioneConsistenza);
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
    return gestioneTerreniLocal
        .getListTipoVarietaVitignoByMatriceAndComune(idUtilizzo, 
            idTipoDestinazione, idTipoDettaglioUso, idTipoQualitaUso, istatComune);
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
    return gestioneTerreniLocal.getTotSupCondottaByAzienda(idAzienda,
        onlyActive);
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
    return gestioneTerreniLocal.riepilogoTipoArea(idAzienda,
        escludiAsservimento, tipoArea);
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
    return gestioneTerreniLocal.riepilogoTipoAreaDichiarato(idAzienda,
        escludiAsservimento, idDichiarazioneConsistenza, tipoArea);
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
    return gestioneTerreniLocal
        .getListUnitaArboreaDichiarataByPianoRiferimentoAndIdStoricoUnitaArborea(
            idStoricoParticella, idAzienda, idPianoRiferimento, orderBy);
  }
  
  public long getIdUnitaArboreaDichiarata(long idStoricoUnitaArborea, long idDichiarazioneConsistenza)
      throws Exception
  {
    return gestioneTerreniLocal.getIdUnitaArboreaDichiarata(idStoricoUnitaArborea, 
        idDichiarazioneConsistenza);
  }
  
  
  public Long getDefaultIdGenereIscrizione()
    throws Exception
  {
    return gestioneTerreniLocal.getDefaultIdGenereIscrizione();
  }
  
  public Vector<TipoGenereIscrizioneVO> getListTipoGenereIscrizione()
    throws Exception
  {
    return gestioneTerreniLocal.getListTipoGenereIscrizione();
  }
  
  public BigDecimal getSupEleggPlSqlTotale(long idParticellaCertificata, long idCatalogoMatrice)
    throws Exception
  {
    return gestioneTerreniLocal.getSupEleggPlSqlTotale(idParticellaCertificata, idCatalogoMatrice);
  }
  
  public BigDecimal getSupEleggNettaPlSqlTotale(long idParticellaCertificata, long idCatalogoMatrice)
    throws Exception
  {
    return gestioneTerreniLocal.getSupEleggNettaPlSqlTotale(idParticellaCertificata, idCatalogoMatrice);
  }
  
  public Vector<TipoMenzioneGeograficaVO> getListTipoMenzioneGeografica(long idParticella, 
      Long idTipologiaVino, java.util.Date dataInserimentoDichiarazione)
    throws Exception
  {
    return gestioneTerreniLocal.getListTipoMenzioneGeografica(idParticella, idTipologiaVino, dataInserimentoDichiarazione);
  }
  
  public Vector<StoricoParticellaVO> getListParticelleByIdDocumento(Long idDocumento, String[] orderBy)
      throws Exception
  {
    return gestioneTerreniLocal.getListParticelleByIdDocumento(idDocumento, orderBy);
  }
  
  public void cambiaPercentualePossesso(Long[] elencoIdConduzioneParticella, Vector<Long> vIdParticella,
      RuoloUtenza ruoloUtenza, Long idAzienda, BigDecimal percentualePossesso) throws Exception
  {
    gestioneTerreniLocal.cambiaPercentualePossesso(elencoIdConduzioneParticella, 
        vIdParticella, ruoloUtenza, idAzienda, percentualePossesso);
  }
  
  public void cambiaPercentualePossessoSupUtilizzata(Vector<Long> vIdConduzioni,
      RuoloUtenza ruoloUtenza, Long idAzienda)  throws Exception
  {
    gestioneTerreniLocal.cambiaPercentualePossessoSupUtilizzata(
        vIdConduzioni, ruoloUtenza, idAzienda);
  }
  

  // NUOVO TERRITORIALE

  // NUOVO FABBRICATO

  /**
   * Metodo utilizzato per estrarre i fabbricati di un'azienda relativi ad una
   * specifica particella
   * 
   * @param idConduzioneParticella
   * @param orderBy
   * @param onlyActive
   * @return
   * @throws Exception
   */
  public FabbricatoParticellaVO[] getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella(
      Long idConduzioneParticella, Long idAzienda, String[] orderBy,
      boolean onlyActive) throws Exception
  {
    return fabbricatoLocal
        .getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella(
            idConduzioneParticella, idAzienda, orderBy, onlyActive);
  }

  /**
   * Metodo che mi permette di recuperare l'elenco dei fabbricati di un'azienda
   * relativi ad un determinato piano di riferimento
   * 
   * @param idAzienda
   * @param idPianoRiferimento
   * @param orderBy
   * @return it.csi.solmr.dto.anag.FabbricatoParticellaVO[]
   * @throws DataAccessException
   */
  public FabbricatoVO[] getListFabbricatiAziendaByPianoRifererimento(
      Long idAzienda, Long idPianoRiferimento, Long idUte, String[] orderBy)
      throws Exception
  {
    return fabbricatoLocal.getListFabbricatiAziendaByPianoRifererimento(
        idAzienda, idPianoRiferimento, idUte, orderBy);
  }

  // FINE NUOVO FABBRICATO

  // NUOVA GESTIONE UTE

  /**
   * Metodo che mi restituisce l'elenco delle ute relative all'azienda agricola
   * selezionata
   * 
   * @param idAzienda
   * @param onlyActive
   * @param orderBy
   * @return java.util.Vector
   * @throws Exception
   */
  public Vector<UteVO> getListUteByIdAzienda(Long idAzienda,
      boolean onlyActive, String[] orderBy) throws Exception
  {
    return uteLocal.getListUteByIdAzienda(idAzienda, onlyActive, orderBy);
  }

  /**
   * Metodo che mi restituisce l'elenco delle ute attive relative all'azienda
   * agricola selezionata e al piano di riferimento
   * 
   * @param idAzienda
   * @param idPianoRiferimento
   * @return java.util.Vector
   * @throws DataAccessException
   */
  public Vector<UteVO> getListUteByIdAziendaAndIdPianoRiferimento(
      Long idAzienda, long idPianoRiferimento) throws Exception
  {
    return uteLocal.getListUteByIdAziendaAndIdPianoRiferimento(idAzienda,
        idPianoRiferimento);
  }

  /**
   * Metodo per estrarre l'unità produttiva a partire dalla sua chiave primaria
   * 
   * @param idUte
   * @return it.csi.solmr.dto.anag.UteVO
   * @throws Exception
   */
  public UteVO findUteByPrimaryKey(Long idUte) throws Exception
  {
    return uteLocal.findUteByPrimaryKey(idUte);
  }

  public Date getMinDataInizioConduzione(Long idUte) throws Exception
  {
    return uteLocal.getMinDataInizioConduzione(idUte);
  }

  public Date getMinDataInizioAllevamento(Long idUte) throws Exception
  {
    return uteLocal.getMinDataInizioAllevamento(idUte);
  }

  public Date getMinDataInizioFabbricati(Long idUte) throws Exception
  {
    return uteLocal.getMinDataInizioFabbricati(idUte);
  }

  // FINE NUOVA GESTIONE UTE

  public IntermediarioVO serviceFindResponsabileByIdIntermediarioAndIdProcToDateRiferimento(
      Long idIntermediario, Long idProcedimento, Date dataRiferimento)
      throws SolmrException, Exception
  {
    return smrCommLocal
        .serviceFindResponsabileByIdIntermediarioAndIdProcToDateRiferimento(
            idIntermediario, idProcedimento, dataRiferimento);
  }

  // INIZIO ANAGRAFICA

  /**
   * Metodo che si occupa di reperire i dati dalla tabella DB_TIPO_CESSAZIONE
   * 
   * @param orderBy
   * @param onlyActive
   * @return it.csi.solmr.dto.anag.TipoCessazioneVO[]
   * @throws Exception
   */
  public TipoCessazioneVO[] getListTipoCessazione(String orderBy,
      boolean onlyActive) throws Exception
  {
    return anagDenominazioniLocal.getListTipoCessazione(orderBy, onlyActive);
  }

  /**
   * Metodo utilizzato per recuperare l'elenco dell'aziende in funzione del CUAA
   * 
   * @param cuaa
   * @param onlyActive
   * @param isCessata
   * @param orderBy
   * @return it.csi.solmr.dto.anag.AnagAziendaVO[]
   * @throws Exception
   */
  public AnagAziendaVO[] getListAnagAziendaVOByCuaa(String cuaa,
      boolean onlyActive, boolean isCessata, String[] orderBy)
      throws Exception
  {
    return anagAziendaLocal.getListAnagAziendaVOByCuaa(cuaa, onlyActive,
        isCessata, orderBy);
  }

  /**
   * Metodo che mi restituisce la MAX relativa alla data inizio validita di
   * un'azienda
   * 
   * @param idAzienda
   * @return java.util.Date
   * @throws Exception
   */
  public Date getMaxDataInizioValiditaAnagraficaAzienda(Long idAzienda)
      throws Exception
  {
    return anagAziendaLocal
        .getMaxDataInizioValiditaAnagraficaAzienda(idAzienda);
  }

  /**
   * Metodo che mi permette di estrarre tutte le occorrenze dalla tabella
   * DB_ANAGRAFICA_AZIENDA in relazione all'id_azienda di destinazione
   * 
   * @param idAzienda
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.AnagAziendaVO[]
   * @throws Exception
   */
  public AnagAziendaVO[] getListAnagAziendaDestinazioneByIdAzienda(
      Long idAzienda, boolean onlyActive, String[] orderBy)
      throws Exception
  {
    return anagAziendaLocal.getListAnagAziendaDestinazioneByIdAzienda(
        idAzienda, onlyActive, orderBy);
  }

  /**
   * Metodo utilizzato per recuperare l'elenco delle aziende su cui insistono le
   * particelle ad asservimento: va utilizzato dopo il search delle particelle
   * in modo da ottenere gli id_storico_particelle indispensabili per la query
   * 
   * @param idStoricoParticella
   * @param idAzienda
   * @param idTitoloPossesso
   * @return it.csi.solmr.dto.anag.AnagAziendaVO[]
   * @throws Exception
   */
  public AnagAziendaVO[] getListAziendeParticelleAsservite(
      Long idStoricoParticella, Long idAzienda, Long idTitoloPossesso, Date dataInserimentoDichiarazione)
      throws Exception
  {
    return anagAziendaLocal.getListAziendeParticelleAsservite(
        idStoricoParticella, idAzienda, idTitoloPossesso, dataInserimentoDichiarazione);
  }

  // FINE ANAGRAFICA

  // INIZIO ATTESTAZIONI

  /**
   * Metodo utilizzato per estrarre le attestazioni relative ad un determinato
   * piano di riferimento
   * 
   * @param idAzienda
   * @param codiceFotografiaTerreni
   * @param dataInserimentoDichiarazione
   * @param flagVideo
   * @param flagStampa
   * @param orderBy
   * @param codiceAttestazione
   * @param voceMenu
   * @return it.csi.solmr.dto.attestazioni.TipoAttestazioneVO[]
   * @throws Exception
   */
  public TipoAttestazioneVO[] getListTipoAttestazioneOfPianoRiferimento(
      Long idAzienda, String codiceFotografiaTerreni,
      java.util.Date dataAnnoCampagna, java.util.Date dataVariazione, String[] orderBy, 
      String voceMenu) throws Exception
  {
    return attestazioniLocal.getListTipoAttestazioneOfPianoRiferimento(
        idAzienda, codiceFotografiaTerreni, dataAnnoCampagna, dataVariazione,
        orderBy, voceMenu);
  }

  public boolean getDataAttestazioneAllaDichiarazione(
      String codiceFotografiaTerreni,
      java.util.Date dataInserimentoDichiarazione, boolean flagVideo,
      boolean flagStampa, String codiceAttestazione, String voceMenu)
      throws Exception
  {
    return attestazioniLocal.getDataAttestazioneAllaDichiarazione(
        codiceFotografiaTerreni, dataInserimentoDichiarazione, flagVideo,
        flagStampa, codiceAttestazione, voceMenu);
  }

  /**
   * Metodo per verificare la presenza di attestazioni dichiarate
   * 
   * @param codiceFotografiaTerreni
   * @return boolean
   * @throws Exception
   */
  public boolean isAttestazioneDichiarata(String codiceFotografiaTerreni)
      throws Exception
  {
    return attestazioniLocal.isAttestazioneDichiarata(codiceFotografiaTerreni);
  }
  
  
  /**
   * Metodo per verificare la presenza di attestazioni azienda
   * 
   * @param idAzienda
   * @return boolean
   * @throws Exception
   */
  public boolean isAttestazioneAzienda(long idAzienda)
      throws Exception
  {
    return attestazioniLocal.isAttestazioneAzienda(idAzienda);
  }

  /**
   * Metodo che si occupa di richiamare una procedura PL-SQL per calcolare le
   * dichiarazioni che possono già essere selezionate dal sistema
   * 
   * @param idAzienda
   * @param idUtente
   * @param voceMenu
   * @throws SolmrException
   * @throws Exception
   */
  public void aggiornaAttestazioniPlSql(Long idAzienda, Long idUtente,
      Long idDichiarazioneConsistenza, String codAttestazione) throws SolmrException, Exception
  {
    attestazioniLocal.aggiornaAttestazioniPlSql(idAzienda, idUtente, idDichiarazioneConsistenza, codAttestazione);
  }

  /**
   * Metodo che mi permette di effettuare l'aggiornamento delle dichiarazioni
   * associate all'azienda
   * 
   * @param elencoIdAttestazioni
   * @param anagAziendaVO
   * @param ruoloUtenza
   * @param elencoAttestazioni
   * @param voceMenu
   * @param codiceFotografiaTerreni
   * @throws Exception
   */
  public void aggiornaAttestazioni(String[] elencoIdAttestazioni,
      AnagAziendaVO anagAziendaVO, RuoloUtenza ruoloUtenza,
      Hashtable<?,?> elencoParametri, String voceMenu, ConsistenzaVO consistenzaVO)
      throws Exception
  {
    attestazioniLocal.aggiornaAttestazioni(elencoIdAttestazioni,
        anagAziendaVO, ruoloUtenza, elencoParametri, voceMenu,
        consistenzaVO);
  }
  
  
  public void aggiornaElencoAllegatiAttestazioni(String[] elencoIdAttestazioni, 
      RuoloUtenza ruoloUtenza, Hashtable<Long,ParametriAttDichiarataVO> elencoParametri, 
      TipoAllegatoVO tipoAllegatoVO, ConsistenzaVO consistenzaVO) throws Exception 
  {
    attestazioniLocal.aggiornaElencoAllegatiAttestazioni(elencoIdAttestazioni, ruoloUtenza,
        elencoParametri, tipoAllegatoVO, consistenzaVO);
  }

  /**
   * Metodo che mi restituisce l'elenco delle attestazioni relative al piano
   * corrente Da usare solo nelle funzionalità di modifica in quanto mantiene il
   * campo "DESCRIZIONE" del DB senza la decodifica ma con i parametri: es
   * "$$parametro1".
   * 
   * @param idAzienda
   * @param codiceFotografiaTerreni
   * @param dataInserimentoDichiarazione
   * @param flagVideo
   * @param flagStampa
   * @param orderBy
   * @param codiceAttestazione
   * @param voceMenu
   * @return TipoAttestazioneVO[]
   * @throws Exception
   */
  public TipoAttestazioneVO[] getListTipoAttestazioneForUpdate(Long idAzienda,
      String[] orderBy, String voceMenu) throws Exception
  {
    return attestazioniLocal.getListTipoAttestazioneForUpdate(idAzienda,
        orderBy, voceMenu);
  }

  /**
   * Metodo utilizzato per estrarre i tipi parametri previsti per una
   * determinata attestazione
   * 
   * @param idAttestazione
   * @return it.csi.solmr.dto.anag.attestazioni.TipoParametriAttestazioneVO
   * @throws Exception
   */
  public TipoParametriAttestazioneVO findTipoParametriAttestazioneByIdAttestazione(
      Long idAttestazione) throws Exception
  {
    return attestazioniLocal
        .findTipoParametriAttestazioneByIdAttestazione(idAttestazione);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco delle attestazioni/allegati validi
   * al piano di lavoro corrente
   * 
   * @param flagVideo
   * @param flagStampa
   * @param orderBy
   * @param codiceAttestazione
   * @param voceMenu
   * @return it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO[]
   * @throws Exception
   */
  public TipoAttestazioneVO[] getElencoTipoAttestazioneAlPianoAttuale(
      boolean flagVideo, boolean flagStampa, String[] orderBy,
      String codiceAttestazione, String voceMenu) throws Exception
  {
    return attestazioniLocal.getElencoTipoAttestazioneAlPianoAttuale(
        flagVideo, flagStampa, orderBy, codiceAttestazione, voceMenu);
  }

  /**
   * Metodo utilizzato per estrarre le attestazioni/allegati validi ad una
   * determinata dichiarazione di consistenza
   * 
   * @param dataInserimentoDichiarazione
   * @param flagVideo
   * @param flagStampa
   * @param orderBy
   * @param codiceAttestazione
   * @param voceMenu
   * @return it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO[]
   * @throws Exception
   */
  public TipoAttestazioneVO[] getTipoAttestazioneAllegatiAllaDichiarazione(
      String codiceFotografiaTerreni, java.util.Date dataAnnoCampagna, 
      java.util.Date dataVariazione, String[] orderBy) throws Exception
  {
    return attestazioniLocal.getTipoAttestazioneAllegatiAllaDichiarazione(
      codiceFotografiaTerreni, dataAnnoCampagna, dataVariazione, orderBy);
  }
  
  public Vector<TipoAttestazioneVO> getElencoAttestazioniAllaDichiarazione(
      String codiceFotografiaTerreni, Date dataAnnoCampagna, 
      String codiceAttestazione)
    throws Exception 
  {
    return attestazioniLocal.getElencoAttestazioniAllaDichiarazione(codiceFotografiaTerreni, 
      dataAnnoCampagna, codiceAttestazione);
  }
  
  public AttestazioneAziendaVO getFirstAttestazioneAzienda(long idAzienda) throws Exception
  {
    return attestazioniLocal.getFirstAttestazioneAzienda(idAzienda);
  }
  
  public AttestazioneDichiarataVO getFirstAttestazioneDichiarata(long codiceFotografia) throws Exception
  {
    return attestazioniLocal.getFirstAttestazioneDichiarata(codiceFotografia);
  }
  
  public Vector<java.util.Date> getDateVariazioniAllegati(long codiceFotografia) throws Exception
  {
    return attestazioniLocal.getDateVariazioniAllegati(codiceFotografia);
  }

  // FINE ATTESTAZIONI

  /*****************************************************************************
   * /* I METODI SEGUENTI SERVONO AI REPORT DINAMICI
   */
  /** ********************************************* */

  /**
   * Esegue la query con i parametri ed i valori selezionati dell'utente
   * 
   * @param idTipologia
   * @param parameters
   * @return
   * @throws Exception
   */
  public Htmpl getRisultatoQuery(TipologiaReportVO tipologiaReportVO,
      HashMap<?,?> parametriFissiHtmpl, Htmpl layout) throws Exception,
      SolmrException
  {
    return reportDinLocal.getRisultatoQuery(tipologiaReportVO,
        parametriFissiHtmpl, layout);
  }

  public HSSFWorkbook getRisultatoQuery(TipologiaReportVO tipologiaReportVO,
      HashMap<?,?> parametriFissiHtmpl, HSSFWorkbook workBook, String nomeFoglio)
      throws Exception, SolmrException
  {
    return reportDinLocal.getRisultatoQuery(tipologiaReportVO,
        parametriFissiHtmpl, workBook, nomeFoglio);
  }

  /**
   * Elenco variabili e valori associate alla query selezionata
   * 
   * @param idTipologia
   * @return
   * @throws Exception
   */
  public HashMap<?,?> getQueryPopolamento(String queryPopolamento,
      String idRptVariabileReport) throws Exception, SolmrException

  {
    return reportDinLocal.getQueryPopolamento(queryPopolamento,
        idRptVariabileReport);
  }

  public boolean isCUAAAlreadyPresentInsediate(String cuaa)
      throws Exception
  {
    return anagAziendaLocal.isCUAAAlreadyPresentInsediate(cuaa);
  }

  /**
   * Servizi che accedono ai WS CCIAA START
   */

  /**
   * Metodo che richiama il WS elencoUnitaVitatePerCUAA (tramite la webApp
   * SIAN).
   * 
   * @param cuaa
   *          String
   * @param idAzienda
   *          Long
   * @throws SolmrException
   * @throws Exception
   * @return UvResponseCCIAA
   */
  public UvResponseCCIAA elencoUnitaVitatePerCUAA(String cuaa, Long idAzienda, RuoloUtenza ruolo)
      throws SolmrException, Exception
  {
    return sianLocal.elencoUnitaVitatePerCUAA(cuaa, idAzienda, ruolo);
  }

  public UvResponseCCIAA elencoUnitaVitatePerParticella(String istat,
      String sezione, String foglio, String particella, Long idAzienda, RuoloUtenza ruolo) throws SolmrException,
      Exception
  {
    return sianLocal.elencoUnitaVitatePerParticella(istat, sezione, foglio,
        particella, idAzienda, ruolo);
  }

  /**
   * Metodo che si occupa di invocare il web-service CCIIA per l'aggiornamento
   * dei dati relativi all'albo vigneti
   * 
   * @param AnagAziendaVO
   * @throws Exception
   * @throws SolmrException
   */
  public void sianAggiornaDatiAlboVigneti(AnagAziendaVO anagAziendaVO)
      throws SolmrException, Exception
  {
    sianLocal.sianAggiornaDatiAlboVigneti(anagAziendaVO);
  }

  /**
   * Servizi che accedono ai WS CCIAA STOP
   */
  
  public StringcodeDescription getDescTipoIscrizioneInpsByCodice(String codiceTipoIscrizioneInps)
      throws Exception
  {
    return sianLocal.getDescTipoIscrizioneInpsByCodice(codiceTipoIscrizioneInps);
  }

  public Long getAnnoDichiarazione(Long idDichiarazioneConsistenza)
      throws Exception
  {
    return consistenzaLocal.getAnnoDichiarazione(idDichiarazioneConsistenza);
  }

  public Long getProcedimento(Long idAzienda, Long idDichiarazioneConsistenza)
      throws Exception
  {
    return consistenzaLocal.getProcedimento(idAzienda,
        idDichiarazioneConsistenza);
  }

  public long getLastIdDichiazioneConsistenza(Long idAzienda, Long anno)
      throws Exception
  {
    return consistenzaLocal.getLastIdDichiazioneConsistenza(idAzienda, anno);
  }

  public long[] ricercaIdParticelleTerreni(
      FiltriRicercaTerrenoVO filtriRicercaTerrenoVO) throws Exception
  {
    StopWatch stopWatch = null;
    long results[] = null;
    try
    {
      SolmrLogger.debug(this, "[AnagFacadeBean::ricercaTerreni] BEGIN.");
      // Stopwatch
      stopWatch = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
      stopWatch.start();

      // Richiamo Logica di business
      results = gestioneTerreniLocal
          .ricercaIdParticelleTerreni(filtriRicercaTerrenoVO);
      return results;
    }
    catch (Exception e)
    {
      throw e;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[] { new Parametro(
          "filtriRicercaTerrenoVO", filtriRicercaTerrenoVO) };
      LoggerUtils.logEJBError(this, "[AnagFacadeBean::ricercaTerreni]", t,
          null, parametri);
      throw new Exception(t.getMessage());
    }
    finally
    {
      try
      {
        stopWatch.dumpElapsed("AnagFacadeBean", "ricercaTerreni",
            "Metodo della facade", "# elementi: "
                + (results == null ? 0 : results.length));
        stopWatch.stop();
      }
      catch (Exception ex)
      {
        // Nessuna operazione
      }
      SolmrLogger.debug(this, "[AnagFacadeBean::ricercaTerreni] END.");
    }
  }

  public long[] ricercaIdConduzioneTerreniImportaAsservimento(
      FiltriRicercaTerrenoImportaAsservimentoVO filtriRicercaTerrenoVO)
      throws Exception
  {
    StopWatch stopWatch = null;
    long results[] = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[AnagFacadeBean::ricercaIdConduzioneTerreniImportaAsservimento] BEGIN.");
      // Stopwatch
      stopWatch = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
      stopWatch.start();

      // Richiamo Logica di business
      results = gestioneTerreniLocal
          .ricercaIdConduzioneTerreniImportaAsservimento(filtriRicercaTerrenoVO);
      return results;
    }
    catch (Exception e)
    {
      throw e;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[] { new Parametro(
          "filtriRicercaTerrenoVO", filtriRicercaTerrenoVO) };
      LoggerUtils.logEJBError(this,
          "[AnagFacadeBean::ricercaIdConduzioneTerreniImportaAsservimento]", t,
          null, parametri);
      throw new Exception(t.getMessage());
    }
    finally
    {
      try
      {
        stopWatch.dumpElapsed("AnagFacadeBean",
            "ricercaIdConduzioneTerreniImportaAsservimento",
            "Metodo della facade", "# elementi: "
                + (results == null ? 0 : results.length));
        stopWatch.stop();
      }
      catch (Exception ex)
      {
        // Nessuna operazione
      }
      SolmrLogger
          .debug(this,
              "[AnagFacadeBean::ricercaIdConduzioneTerreniImportaAsservimento] END.");
    }
  }

  public RigaRicercaTerreniVO[] getRigheRicercaTerreniByIdParticellaRange(
      long ids[]) throws Exception
  {
    StopWatch stopWatch = null;
    RigaRicercaTerreniVO results[] = null;
    try
    {
      SolmrLogger.debug(this,
          "[AnagFacadeBean::getRigheRicercaTerreniByIdParticellaRange] BEGIN.");
      // Stopwatch
      stopWatch = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
      stopWatch.start();

      // Richiamo Logica di business
      results = gestioneTerreniLocal
          .getRigheRicercaTerreniByIdParticellaRange(ids);
      return results;
    }
    catch (Exception e)
    {
      throw e;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[] { new Parametro("ids", ids) };
      LoggerUtils.logEJBError(this,
          "[AnagFacadeBean::getRigheRicercaTerreniByIdParticellaRange]", t,
          null, parametri);
      throw new Exception(t.getMessage());
    }
    finally
    {
      try
      {
        stopWatch.dumpElapsed("AnagFacadeBean",
            "getRigheRicercaTerreniByIdParticellaRange", "Metodo della facade",
            "# elementi: " + (results == null ? 0 : results.length));
        stopWatch.stop();
      }
      catch (Exception ex)
      {
        // Nessuna operazione
      }
      SolmrLogger.debug(this,
          "[AnagFacadeBean::getRigheRicercaTerreniByIdParticellaRange] END.");
    }
  }

  public RigaRicercaTerreniImportaAsservimentoVO[] getRigheRicercaTerreniImportaAsservimentoByIdConduzioneRange(
      long ids[]) throws Exception
  {
    StopWatch stopWatch = null;
    RigaRicercaTerreniImportaAsservimentoVO results[] = null;
    try
    {
      SolmrLogger
          .debug(
              this,
              "[AnagFacadeBean::getRigheRicercaTerreniImportaAsservimentoByIdConduzioneRange] BEGIN.");
      // Stopwatch
      stopWatch = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
      stopWatch.start();

      // Richiamo Logica di business
      results = gestioneTerreniLocal
          .getRigheRicercaTerreniImportaAsservimentoByIdConduzioneRange(ids);
      return results;
    }
    catch (Exception e)
    {
      throw e;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[] { new Parametro("ids", ids) };
      LoggerUtils
          .logEJBError(
              this,
              "[AnagFacadeBean::getRigheRicercaTerreniImportaAsservimentoByIdConduzioneRange]",
              t, null, parametri);
      throw new Exception(t.getMessage());
    }
    finally
    {
      try
      {
        stopWatch.dumpElapsed("AnagFacadeBean",
            "getRigheRicercaTerreniImportaAsservimentoByIdConduzioneRange",
            "Metodo della facade", "# elementi: "
                + (results == null ? 0 : results.length));
        stopWatch.stop();
      }
      catch (Exception ex)
      {
        // Nessuna operazione
      }
      SolmrLogger
          .debug(
              this,
              "[AnagFacadeBean::getRigheRicercaTerreniImportaAsservimentoByIdConduzioneRange] END.");
    }
  }

  public RigaRicercaTerreniImportaAsservimentoVO[] getRigheRicercaTerreniImportaAsservimentoByIdParticellaRange(
      long ids[]) throws Exception
  {
    StopWatch stopWatch = null;
    RigaRicercaTerreniImportaAsservimentoVO results[] = null;
    try
    {
      SolmrLogger
          .debug(
              this,
              "[AnagFacadeBean::getRigheRicercaTerreniImportaAsservimentoByIdParticellaRange] BEGIN.");
      // Stopwatch
      stopWatch = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
      stopWatch.start();

      // Richiamo Logica di business
      results = gestioneTerreniLocal
          .getRigheRicercaTerreniImportaAsservimentoByIdParticellaRange(ids);
      return results;
    }
    catch (Exception e)
    {
      throw e;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[] { new Parametro("ids", ids) };
      LoggerUtils
          .logEJBError(
              this,
              "[AnagFacadeBean::getRigheRicercaTerreniImportaAsservimentoByIdParticellaRange]",
              t, null, parametri);
      throw new Exception(t.getMessage());
    }
    finally
    {
      try
      {
        stopWatch.dumpElapsed("AnagFacadeBean",
            "getRigheRicercaTerreniImportaAsservimentoByIdParticellaRange",
            "Metodo della facade", "# elementi: "
                + (results == null ? 0 : results.length));
        stopWatch.stop();
      }
      catch (Exception ex)
      {
        // Nessuna operazione
      }
      SolmrLogger
          .debug(
              this,
              "[AnagFacadeBean::getRigheRicercaTerreniImportaAsservimentoByIdParticellaRange] END.");
    }
  }

  public ParticellaDettaglioValidazioniVO[] getParticellaDettaglioValidazioni(
      long idParticella, Long anno, int tipoOrdinamento,
      boolean ordineAscendente[]) throws Exception
  {
    StopWatch stopWatch = null;
    ParticellaDettaglioValidazioniVO results[] = null;
    try
    {
      SolmrLogger.debug(this,
          "[AnagFacadeBean::getParticellaDettaglioValidazioni] BEGIN.");
      // Stopwatch
      stopWatch = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
      stopWatch.start();

      // Richiamo Logica di business
      results = gestioneTerreniLocal.getParticellaDettaglioValidazioni(
          idParticella, anno, tipoOrdinamento, ordineAscendente);
      return results;
    }
    catch (Exception e)
    {
      throw e;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[] {
          new Parametro("idParticella", idParticella),
          new Parametro("anno", anno),
          new Parametro("tipoOrdinamento", tipoOrdinamento),
          new Parametro("ordineAscendente", ordineAscendente) };
      LoggerUtils.logEJBError(this,
          "[AnagFacadeBean::getParticellaDettaglioValidazioni]", t, null,
          parametri);
      throw new Exception(t.getMessage());
    }
    finally
    {
      try
      {
        stopWatch.dumpElapsed("AnagFacadeBean",
            "getParticellaDettaglioValidazioni", "Metodo della facade",
            "# elementi: " + (results == null ? 0 : results.length));
        stopWatch.stop();
      }
      catch (Exception ex)
      {
        // Nessuna operazione
      }
      SolmrLogger.debug(this,
          "[AnagFacadeBean::getParticellaDettaglioValidazioni] END.");
    }
  }

  public long[] getElencoAnniDichiarazioniConsistenzaByIdParticella(
      long idParticella) throws Exception
  {
    StopWatch stopWatch = null;
    long results[] = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[AnagFacadeBean::getElencoAnniDichiarazioniConsistenzaByIdParticella] BEGIN.");
      // Stopwatch
      stopWatch = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
      stopWatch.start();

      // Richiamo Logica di business
      results = consistenzaLocal
          .getElencoAnniDichiarazioniConsistenzaByIdParticella(idParticella);
      return results;
    }
    catch (Exception e)
    {
      throw e;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[] { new Parametro(
          "idStoricoParticella", idParticella) };
      LoggerUtils
          .logEJBError(
              this,
              "[AnagFacadeBean::getElencoAnniDichiarazioniConsistenzaByIdParticella]",
              t, null, parametri);
      throw new Exception(t.getMessage());
    }
    finally
    {
      try
      {
        stopWatch.dumpElapsed("AnagFacadeBean",
            "getElencoAnniDichiarazioniConsistenzaByIdParticella",
            "Metodo della facade", "# elementi: "
                + (results == null ? 0 : results.length));
        stopWatch.stop();
      }
      catch (Exception ex)
      {
        // Nessuna operazione
      }
      SolmrLogger
          .debug(this,
              "[AnagFacadeBean::getElencoAnniDichiarazioniConsistenzaByIdParticella] END.");
    }
  }

  public boolean areParticelleNonCessateByIdParticelle(long idParticelle[])
      throws Exception
  {
    StopWatch stopWatch = null;
    boolean results = false;
    try
    {
      SolmrLogger.debug(this,
          "[AnagFacadeBean::areParticelleNonCessateByIdParticelle] BEGIN.");
      // Stopwatch
      stopWatch = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
      stopWatch.start();

      // Richiamo Logica di business
      results = gestioneTerreniLocal
          .areParticelleNonCessateByIdParticelle(idParticelle);
      return results;
    }
    catch (Exception e)
    {
      throw e;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[] { new Parametro("idParticelle",
          idParticelle) };
      LoggerUtils.logEJBError(this,
          "[AnagFacadeBean::areParticelleNonCessateByIdParticelle]", t, null,
          parametri);
      throw new Exception(t.getMessage());
    }
    finally
    {
      try
      {
        stopWatch.dumpElapsed("AnagFacadeBean",
            "areParticelleNonCessateByIdParticelle", "Metodo della facade",
            "# ritorno: " + results);
        stopWatch.stop();
      }
      catch (Exception ex)
      {
        // Nessuna operazione
      }
      SolmrLogger.debug(this,
          "[AnagFacadeBean::areParticelleNonCessateByIdParticelle] END.");
    }
  }

  public StoricoParticellaVO findStoricoParticellaVOByIdParticella(
      long idParticella) throws Exception
  {
    StopWatch stopWatch = null;
    StoricoParticellaVO results = null;
    try
    {
      SolmrLogger.debug(this,
          "[AnagFacadeBean::findStoricoParticellaVOByIdParticella] BEGIN.");
      // Stopwatch
      stopWatch = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
      stopWatch.start();

      // Richiamo Logica di business
      results = gestioneTerreniLocal
          .findStoricoParticellaVOByIdParticella(idParticella);
      return results;
    }
    catch (Exception e)
    {
      throw e;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[] { new Parametro("idParticella",
          idParticella) };
      LoggerUtils.logEJBError(this,
          "[AnagFacadeBean::findStoricoParticellaVOByIdParticella]", t, null,
          parametri);
      throw new Exception(t.getMessage());
    }
    finally
    {
      try
      {
        stopWatch.dumpElapsed("AnagFacadeBean",
            "findStoricoParticellaVOByIdParticella", "Metodo della facade",
            "# ritorno<>null: " + (results != null));
        stopWatch.stop();
      }
      catch (Exception ex)
      {
        // Nessuna operazione
      }
      SolmrLogger.debug(this,
          "[AnagFacadeBean::findStoricoParticellaVOByIdParticella] END.");
    }
  }

  public int countParticelleConConduzioniAttive(long idParticella[])
      throws SolmrException, Exception
  {
    StopWatch stopWatch = null;
    int results = -1;
    try
    {
      SolmrLogger.debug(this,
          "[AnagFacadeBean::countParticelleConConduzioniAttive] BEGIN.");
      // Stopwatch
      stopWatch = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
      stopWatch.start();

      // Richiamo Logica di business
      results = fascicoloLocal
          .countParticelleConConduzioniAttive(idParticella);
      return results;
    }
    catch (Exception e)
    {
      throw e;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[] { new Parametro("idParticella",
          idParticella) };
      LoggerUtils.logEJBError(this,
          "[AnagFacadeBean::countParticelleConConduzioniAttive]", t, null,
          parametri);
      throw new Exception(t.getMessage());
    }
    finally
    {
      try
      {
        stopWatch.dumpElapsed("AnagFacadeBean",
            "countParticelleConConduzioniAttive", "Metodo della facade",
            "# ritorno: " + results);
        stopWatch.stop();
      }
      catch (Exception ex)
      {
        // Nessuna operazione
      }
      SolmrLogger.debug(this,
          "[AnagFacadeBean::countParticelleConConduzioniAttive] END.");
    }
  }

  /**
   * Metodo che mi permette di recuperare le provincie delle unita arboree
   * 
   * @param idStoricoUnitaArborea
   * @return Vector di String
   * @throws DataAccessException
   */
  public Vector<String> findProvinciaStoricoParticellaArborea(
      long[] idStoricoUnitaArborea) throws Exception
  {
    return gestioneTerreniLocal
        .findProvinciaStoricoParticellaArborea(idStoricoUnitaArborea);
  }
  
  public Vector<String> findProvinciaStoricoParticellaArboreaIsolaParcella(long idAzienda, long[] idIsolaParcella)
    throws Exception
  {
    return gestioneTerreniLocal.findProvinciaStoricoParticellaArboreaIsolaParcella(idAzienda, idIsolaParcella);
  }
  
  public BigDecimal getSumAreaUVParticella(long idAzienda, long idParticella) 
    throws Exception
  {
    return gestioneTerreniLocal.getSumAreaUVParticella(idAzienda, idParticella);
  }
  
  public int getNumUVParticella(long idAzienda, long idParticella)
    throws Exception
  {
    return gestioneTerreniLocal.getNumUVParticella(idAzienda, idParticella);
  }

  /**
   * Metodo che mi permette di recuperare le provincie delle unita arboree
   * dichiarate
   * 
   * @param idStoricoUnitaArborea
   * @return Vector di String
   * @throws DataAccessException
   */
  public Vector<String> findProvinciaParticellaArboreaDichiarata(
      long[] idUnitaArboreaDichiarata) throws Exception
  {
    return gestioneTerreniLocal
        .findProvinciaParticellaArboreaDichiarata(idUnitaArboreaDichiarata);
  }
  
  public StoricoUnitaArboreaVO findStoricoUnitaArborea(Long idStoricoUnitaArborea) throws Exception
  {
    return gestioneTerreniLocal.findStoricoUnitaArborea(idStoricoUnitaArborea);
  }

  public Vector<ParticellaAssVO> getParticelleDocCor(Long idDocumento) throws Exception
  {
    return gestioneTerreniLocal.getParticelleDocCor(idDocumento);
  }
  
  
  

  // ********************** SITI BEGIN

  public String serviceParticellaUrl3D(String istatComune, String sezione,
      String foglio, String particella, String subalterno)
      throws InvalidParameterException, Exception, UnrecoverableException
  {
    try
    {
      return sitiLocal.serviceGetParticellaUrl3D(istatComune, sezione, foglio,
          particella, subalterno);
    }
    catch (SolmrException s)
    {
      if (s.getErrorType() == ErrorTypes.INVALID_PARAMETER)
        throw new InvalidParameterException(s.getErrorDesc());
      else
        throw new UnrecoverableException(ErrorTypes.STR_UNKNOWN_ERROR);
    }
    catch (Exception e)
    {
      throw new UnrecoverableException(e.getMessage());
    }
  }

  // ********************* SITI END

  public ParticellaAssVO[] getParticellaForDocAzCessata(Long idParticella)
      throws Exception
  {
    return gestioneTerreniLocal.getParticellaForDocAzCessata(idParticella);
  }

  /*
   * Servizi di vitiserv Begin
   */
  public DirittoGaaVO[] getDiritti(long idAzienda, boolean flagAttivi,
      int tipoOrdinamento, int tipoRisultato) throws Exception
  {
    DirittoGaaVO[] dirittiGaa = null;
    DirittoVO diritti[] = null;
    StopWatch stopWatch = null;
    long idDiritto[] = null;
    try
    {
      SolmrLogger.debug(this, "[AnagFacadeBean::getDiritti] BEGIN.");
      // Stopwatch
      stopWatch = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
      stopWatch.start();

      // Vado a leggermi gli idDiritto
      idDiritto = vitiServLocal.vitiservSearchListIdDiritto(idAzienda,
          flagAttivi, tipoOrdinamento);
      if (idDiritto != null && idDiritto.length > 0)
      {
        // Se ho trovato dei diritti vado a chiamare quello byRange
        diritti = vitiServLocal.vitiservGetListDirittoByIdRange(idDiritto,
            tipoRisultato);
        if (diritti != null && diritti.length > 0)
        {
          dirittiGaa = new DirittoGaaVO[diritti.length];
          for (int i = 0, j = diritti.length - 1; i < dirittiGaa.length; i++, j--)
          {
            // Scorro il vettore restituito in quanto i dati ci
            // vengonorestituiti ordinati per data scadenza asc
            // mentre noi vogliamo data scadenza desc
            dirittiGaa[i] = new DirittoGaaVO();
            // Se il campo data scadenza è antecedente alla sysdate visualizzare
            // i dati utilizzando il colore rosso
            if (diritti[j].getDataScadenza() != null
                && diritti[j].getDataScadenza().before(
                    new Date(System.currentTimeMillis())))
              dirittiGaa[i].setScaduto(true);

            dirittiGaa[i].setDiritto(diritti[j]);

            if (diritti[j].getIdVino() != null)
              dirittiGaa[i].setVino(gestioneTerreniLocal
                  .getDescrizioneByIdTipologiaVino(diritti[j].getIdVino()));

            if (diritti[j].getIstatProvinciaOrigine() != null)
              dirittiGaa[i].setSiglaProvinciaOrigine(anagAziendaLocal
                  .getSiglaProvinciaByIstatProvincia(diritti[j]
                      .getIstatProvinciaOrigine()));

            if (diritti[j].getIdAmmCompetenza() != 0)
            {
              AmmCompetenzaVO amm = smrCommLocal
                  .serviceFindAmmCompetenzaById(new Long(diritti[j]
                      .getIdAmmCompetenza()));
              if (amm != null)
                dirittiGaa[i].setDescAmmCompetenzaReimpianto(amm
                    .getDescrizione());
            }

            if (diritti[j].getIdVitigno() != null)
              dirittiGaa[i].setVarieta(gestioneTerreniLocal
                  .getVarietaByIdVitigno(diritti[j].getIdVitigno()));

          }
        }
      }
      return dirittiGaa;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[] {
          new Variabile("idDiritto", idDiritto),
          new Variabile("diritti", diritti),
          new Variabile("dirittiGaa", dirittiGaa) };
      Parametro parametri[] = new Parametro[] { new Parametro("idAzienda",
          idAzienda) };
      LoggerUtils.logEJBError(this, "[AnagFacadeBean::getDiritti]", t,
          variabili, parametri);
      throw new Exception(t.getMessage());
    }
    finally
    {
      try
      {
        stopWatch.dumpElapsed("AnagFacadeBean", "getDiritti",
            "Metodo della facade", "");
        stopWatch.stop();
      }
      catch (Exception ex)
      {
        // Nessuna operazione
      }
      SolmrLogger.debug(this, "[AnagFacadeBean::getDiritti] END.");
    }
  }

  /*
   * Servizi di vitiserv End
   */

  /*****************************************************************************
   * ********* Servizi di SigopServ BEGIN **********************************
   ****************************************************************************/
  
  public SchedaCreditoVO[] sigopservVisualizzaDebiti(String cuaa)
      throws Exception
  {
    SchedaCreditoVO[] schedaCreditori = null;
    StopWatch stopWatch = null;
    try
    {
      SolmrLogger.debug(this,
          "[AnagFacadeBean::sigopservVisualizzaDebiti] BEGIN.");
      // Stopwatch
      stopWatch = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
      stopWatch.start();

      schedaCreditori = sigopServLocal.sigopservVisualizzaDebiti(cuaa);

      if (schedaCreditori.length == 0)
        return null;

      return schedaCreditori;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[] { new Variabile(
          "schedaCreditori", schedaCreditori) };
      Parametro parametri[] = new Parametro[] { new Parametro("cuaa", cuaa) };
      LoggerUtils.logEJBError(this,
          "[AnagFacadeBean::sigopservVisualizzaDebiti]", t,
          variabili, parametri);
      throw new Exception(t.getMessage());
    }
    finally
    {
      try
      {
        stopWatch.dumpElapsed("AnagFacadeBean",
            "sigopservVisualizzaDebiti", "Metodo della facade", "");
        stopWatch.stop();
      }
      catch (Exception ex)
      {
        // Nessuna operazione
      }
      SolmrLogger.debug(this,
          "[AnagFacadeBean::sigopservVisualizzaDebiti] END.");
    }
  }
  
  public PagamentiErogatiVO sigopservEstraiPagamentiErogati(String cuaa, String settore,
    Integer anno)
      throws Exception
  {
    PagamentiErogatiVO pagamentiErogatiVO = null;
    StopWatch stopWatch = null;
    try
    {
      
      SolmrLogger.debug(this,
          "[AnagFacadeBean::sigopservEstraiPagamentiErogati] BEGIN.");
      // Stopwatch
      stopWatch = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
      stopWatch.start();

      pagamentiErogatiVO = sigopServLocal.sigopservEstraiPagamentiErogati(cuaa, settore, anno);

      return pagamentiErogatiVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[] { new Variabile(
          "pagamentiErogatiVO", pagamentiErogatiVO) };
      Parametro parametri[] = new Parametro[] { new Parametro("cuaa", cuaa) };
      LoggerUtils.logEJBError(this,
          "[AnagFacadeBean::sigopservEstraiPagamentiErogati]", t,
          variabili, parametri);
      throw new Exception(t.getMessage());
    }
    finally
    {
      try
      {
        stopWatch.dumpElapsed("AnagFacadeBean",
            "sigopservEstraiPagamentiErogati", "Metodo della facade", "");
        stopWatch.stop();
      }
      catch (Exception ex)
      {
        // Nessuna operazione
      }
      SolmrLogger.debug(this,
          "[AnagFacadeBean::sigopservEstraiPagamentiErogati] END.");
    }
  }
  
  public RecuperiPregressiVO sigopservEstraiRecuperiPregressi(String cuaa, String settore,
    Integer anno)
      throws Exception
  {
    RecuperiPregressiVO recuperiPregressiVO = null;
    StopWatch stopWatch = null;
    try
    {
      
      SolmrLogger.debug(this,
          "[AnagFacadeBean::sigopservEstraiRecuperiPregressi] BEGIN.");
      // Stopwatch
      stopWatch = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
      stopWatch.start();

      recuperiPregressiVO = sigopServLocal.sigopservEstraiRecuperiPregressi(cuaa, settore, anno);

      return recuperiPregressiVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[] { new Variabile(
          "recuperiPregressiVO", recuperiPregressiVO) };
      Parametro parametri[] = new Parametro[] { new Parametro("cuaa", cuaa) };
      LoggerUtils.logEJBError(this,
          "[AnagFacadeBean::sigopservEstraiRecuperiPregressi]", t,
          variabili, parametri);
      throw new Exception(t.getMessage());
    }
    finally
    {
      try
      {
        stopWatch.dumpElapsed("AnagFacadeBean",
            "sigopservEstraiRecuperiPregressi", "Metodo della facade", "");
        stopWatch.stop();
      }
      catch (Exception ex)
      {
        // Nessuna operazione
      }
      SolmrLogger.debug(this,
          "[AnagFacadeBean::sigopservEstraiRecuperiPregressi] END.");
    }
  }

  /*****************************************************************************
   * ********* Servizi di SigopServ END **********************************
   ****************************************************************************/
  
  
  /***************************** COMMON BEGIN ***************************
   ******************************************************************* 
   ******************************************************************/
  
  public Object getValoreParametroAltriDati(String codiceParametro)
    throws Exception, SolmrException
  {
    return commonLocal.getValoreParametroAltriDati(codiceParametro);
  }
  
  /***************************** COMMON END ***************************
   ******************************************************************* 
   ******************************************************************/
  
  
  
  
  /***************************** WS COMUNE START ***************************
   ******************************************************************* 
   ******************************************************************/
  
  public String serviceInviaPostaCertificata(InvioPostaCertificata invioPosta) 
      throws Exception, SolmrException
  {
    try
    {      
      return sianLocal.serviceInviaPostaCertificata(invioPosta);
    }
    catch (Exception e)
    {
      throw new Exception(e.getMessage());
    }
  }
  
  /***************************** WS COMUNE END ***************************
   ******************************************************************* 
   ******************************************************************/
  
  
  /***************************** WS PAPUA START ***************************
   ******************************************************************* 
   ******************************************************************/
  
  public it.csi.papua.papuaserv.presentation.ws.profilazione.axis.Ruolo[] findRuoliForPersonaInApplicazione(String codiceFiscale, int livelloAutenticazione)
      throws SolmrException, Exception
  {
    try
    {
      SolmrLogger.debug(this, "--- codiceFiscale ="+codiceFiscale);	
      SolmrLogger.debug(this, "--- livelloAutenticazione ="+livelloAutenticazione);
      return sianLocal.findRuoliForPersonaInApplicazione(codiceFiscale, livelloAutenticazione);
    }
    catch (SolmrException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new Exception(e.getMessage());
    }
  }
  
  public UtenteAbilitazioni loginPapua(String codiceFiscale, String cognome, 
      String nome, int livelloAutenticazione, String codiceRuolo)
      throws SolmrException, Exception
  {
    try
    {      
      return sianLocal.loginPapua(codiceFiscale, cognome, nome, livelloAutenticazione, codiceRuolo);
    }
    catch (SolmrException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new Exception(e.getMessage());
    }
  }
  
  public MacroCU[] findMacroCUForAttoreInApplication(String codiceAttore)
      throws SolmrException, Exception
  {
    try
    {      
      return sianLocal.findMacroCUForAttoreInApplication(codiceAttore);
    }
    catch (SolmrException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new Exception(e.getMessage());
    }
  }
  
  public boolean verificaGerarchia(long idUtente1, long idUtente2)
      throws SolmrException, Exception
  {
    try
    {      
      return sianLocal.verificaGerarchia(idUtente1, idUtente2);
    }
    catch (SolmrException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new Exception(e.getMessage());
    }    
  }
  
  
  public UtenteAbilitazioni getUtenteAbilitazioniByIdUtenteLogin(long idUtente)
      throws SolmrException, Exception
  {
    try
    {      
      return sianLocal.getUtenteAbilitazioniByIdUtenteLogin(idUtente);
    }
    catch (SolmrException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new Exception(e.getMessage());
    }    
  }
  
  public UtenteAbilitazioni[] getUtenteAbilitazioniByIdUtenteLoginRange(long[] idUtente)
      throws SolmrException, Exception
  {
    try
    {      
      return sianLocal.getUtenteAbilitazioniByIdUtenteLoginRange(idUtente);
    }
    catch (SolmrException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new Exception(e.getMessage());
    }    
  }
  
  /***************************** WS PAPUA END ***************************
   ******************************************************************* 
   ******************************************************************/
  
  

  // Usato dall'utente di monitoraggio
  public String testDB() throws Exception
  {
    return commonLocal.testDB();
  }
}
