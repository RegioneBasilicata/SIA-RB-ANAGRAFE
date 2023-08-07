package it.csi.solmr.business.anag;

import it.csi.csi.wrapper.SystemException;
import it.csi.csi.wrapper.UnrecoverableException;
import it.csi.jsf.htmpl.Htmpl;
import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.MacroCU;
import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni;
import it.csi.sigop.dto.services.PagamentiErogatiVO;
import it.csi.sigop.dto.services.RecuperiPregressiVO;
import it.csi.sigop.dto.services.SchedaCreditoVO;
import it.csi.smranag.smrgaa.dto.BaseCodeDescription;
import it.csi.smranag.smrgaa.dto.ParticellaDettaglioValidazioniVO;
import it.csi.smranag.smrgaa.dto.PlSqlCodeDescription;
import it.csi.smranag.smrgaa.dto.SuperficieDescription;
import it.csi.smranag.smrgaa.dto.search.FiltriRicercaTerrenoImportaAsservimentoVO;
import it.csi.smranag.smrgaa.dto.search.FiltriRicercaTerrenoVO;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaTerreniImportaAsservimentoVO;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaTerreniVO;
import it.csi.smranag.smrgaa.dto.servizi.vitiserv.DirittoGaaVO;
import it.csi.smranag.smrgaa.dto.stampe.ConsistenzaZootecnicaStampa;
import it.csi.smranag.smrgaa.dto.stampe.QuadroDTerreni;
import it.csi.smranag.smrgaa.dto.stampe.TipoAllegatoVO;
import it.csi.smranag.smrgaa.dto.ws.cciaa.UvResponseCCIAA;
import it.csi.smrcomms.reportdin.dto.TipologiaReportVO;
import it.csi.smrcomms.siapcommws.InvioPostaCertificata;
import it.csi.smrcomms.smrcomm.dto.datientiprivati.DatiEntePrivatoVO;
import it.csi.smrcomms.smrcomm.dto.filtro.EntePrivatoFiltroVO;
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
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.DataControlException;
import it.csi.solmr.exception.NotFoundException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.exception.services.InvalidParameterException;
import it.csi.solmr.exception.services.ServiceSystemException;
import it.csi.solmr.ws.infoc.Azienda;
import it.csi.solmr.ws.infoc.Sede;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

@Local
public interface AnagFacadeLocal
{
  public CodeDescription[] getCodeDescriptionsNew(String tableName,
      String filtro, String valFiltro, String orderBy) throws Exception;

  public it.csi.solmr.dto.profile.CodeDescription getGruppoRuolo(String ruolo)
      throws Exception;

  public AnagAziendaVO getAziendaById(Long idAnagAzienda)
      throws NotFoundException, Exception, SolmrException;

  public AnagAziendaVO getAziendaByIdAzienda(Long idAzienda)
      throws NotFoundException, Exception, SolmrException;

  public Vector<AnagAziendaVO> getAssociazioniCollegateByIdAzienda(
      Long idAzienda, Date dataFineValidita) throws Exception,
      SolmrException;

  public AnagAziendaVO getAziendaCUAA(String CUAA, Date data)
      throws NotFoundException, Exception, SolmrException;

  public Vector<AnagAziendaVO> getAziendaCUAA(String CUAA)
      throws NotFoundException, Exception, SolmrException;

  public Vector<AnagAziendaVO> getAziendaByCriterioCessataAndProvvisoria(
      String CUAA) throws Exception, SolmrException;

  public AnagAziendaVO getAziendaPartitaIVA(String partitaIVA, Date data)
      throws NotFoundException, Exception, SolmrException;

  public Vector<Long> getListIdAziende(AnagAziendaVO aVO, Date data,
      boolean attivitaBool) throws NotFoundException, Exception,
      SolmrException;

  public Vector<Long> getListIdAziendeFlagProvvisorio(AnagAziendaVO aaVO,
      Date dataSituazioneAl, boolean attivitaBool, boolean provvisorio)
      throws NotFoundException, Exception, SolmrException;

  public Vector<Long> getListOfIdAzienda(AnagAziendaVO aVO, Date data,
      boolean attivitaBool) throws NotFoundException, Exception,
      SolmrException;

  public Vector<AnagAziendaVO> getListAziendeByIdRange(Vector<Long> collAzienda)
      throws NotFoundException, Exception, SolmrException;

  public Vector<AnagAziendaVO> getAziendeByIdAziendaRange(
      Vector<Long> collAzienda) throws NotFoundException, Exception,
      SolmrException;

  public Vector<AnagAziendaVO> getListAziendeByIdRangeFromIdAzienda(
      Vector<Long> idAzienda) throws NotFoundException, Exception,
      SolmrException;

  public void storicizzaAziendeCollegateBlocco(RuoloUtenza ruoloUtenza,
      Vector<AziendaCollegataVO> vAnagAziendaCollegateVO)
      throws Exception, SolmrException;

  public void eliminaAziendeCollegateBlocco(long idUtenteAggiornamento,
      Long idAziendaFather, Vector<Long> vIdAziendaVO) throws Exception,
      SolmrException;

  public PersonaFisicaVO getTitolareORappresentanteLegaleAzienda(
      Long idAzienda, Date data) throws NotFoundException, Exception,
      SolmrException;

  public Vector<UteVO> getUTE(Long idAzienda, Boolean storico)
      throws SolmrException, Exception;

  public UteVO getUteById(Long idUte) throws Exception, SolmrException;

  public void checkCUAAandCodFiscale(String cuaa, String partitaIVA)
      throws DataAccessException, Exception, SolmrException;

  public AnagAziendaVO getAziendaCUAAandCodFiscale(String cuaa,
      String partitaIVA) throws DataAccessException, Exception,
      SolmrException;

  public AnagAziendaVO getAltraAziendaFromPartitaIVA(String partitaIVA,
      Long idAzienda) throws DataAccessException, Exception,
      SolmrException;

  public void checkPartitaIVA(String partitaIVA, Long idAzienda)
      throws DataAccessException, Exception, SolmrException;

  public void checkCUAA(String cuaa) throws DataAccessException,
      Exception, SolmrException;

  public void checkIsCUAAPresent(String cuaa, Long idAzienda)
      throws Exception, SolmrException;

  public PersonaFisicaVO getPersonaFisica(String cuaa)
      throws DataAccessException, Exception, SolmrException;

  public Long insertAzienda(AnagAziendaVO aaVO, PersonaFisicaVO pfVO,
      UteVO ute, long idUtenteAggiornamento) throws DataAccessException,
      Exception, SolmrException;

  public void insertUte(UteVO uVO, long idUtenteAggiornamento)
      throws DataAccessException, Exception, SolmrException;

  public void countUteByAziendaAndComune(Long idAzienda, String comune)
      throws DataAccessException, Exception, SolmrException;

  public Vector<CodeDescription> getTipiAttivitaOTE() throws Exception,
      NotFoundException;

  public Vector<CodeDescription> getTipiAttivitaATECO(String code,
      String description) throws Exception;

  public Vector<CodeDescription> getTipiAttivitaOTE(String code,
      String description) throws Exception, NotFoundException;

  public Vector<CodeDescription> getTipiAzienda() throws NotFoundException,
      Exception;

  public Vector<CodeDescription> getTipiFabbricato() throws NotFoundException,
      Exception;

  public Vector<CodeDescription> getTipiFormaGiuridica(Long idTipologiaAzienda)
      throws Exception, NotFoundException;
  
  public Vector<CodeDescription> getCodeDescriptionsFormaGiuridica() 
      throws Exception, NotFoundException;

  public TipoTipologiaAziendaVO getTipologiaAzienda(Long idTipologiaAzienda)
      throws Exception, NotFoundException;

  public Vector<TipoTipologiaAziendaVO> getTipiTipologiaAzienda(
      Boolean flagControlliUnivocita, Boolean flagAziendaProvvisoria)
      throws Exception, NotFoundException;

  public Vector<CodeDescription> getTipiFormaGiuridica()
      throws NotFoundException, Exception;

  public Vector<CodeDescription> getTipiIntermediario()
      throws NotFoundException, Exception;

  public Vector<CodeDescription> getTipiIntermediarioUmaProv()
      throws NotFoundException, Exception;

  public Vector<CodeDescription> getTipiUtilizzo() throws NotFoundException,
      Exception;

  public Vector<CodeDescription> getTipiMotivoDichiarazione()
      throws Exception;

  public String getProcedimento(Integer code) throws NotFoundException,
      Exception;

  public CodeDescription getTipoAttivitaATECO(Integer code)
      throws Exception;

  public CodeDescription getTipoAttivitaOTE(Integer code)
      throws Exception;

  public String getTipoAzienda(Integer code) throws NotFoundException,
      Exception;

  public String getTipoCasoParticolare(Integer code) throws NotFoundException,
      Exception;

  public String getTipoFabbricato(Integer code) throws NotFoundException,
      Exception;

  public String getTipoFormaGiuridica(Integer code) throws NotFoundException,
      Exception;

  public String getTipoIntermediario(Integer code) throws NotFoundException,
      Exception;

  public String getTipoUtilizzo(Integer code) throws NotFoundException,
      Exception;

  public String getTipoZonaAltimetrica(Integer code) throws NotFoundException,
      Exception;

  public Vector<CodeDescription> getTipiTipologiaDocumento()
      throws Exception;

  public Vector<CodeDescription> getTipiTipologiaDocumento(boolean cessata)
      throws Exception;

  public Vector<ProvinciaVO> getProvinceByRegione(String idRegione)
      throws Exception;
  
  public Vector<ProvinciaVO> getProvince()
      throws Exception;

  public Vector<ComuneVO> getComuniLikeByRegione(String idRegione, String like)
      throws NotFoundException, SolmrException, Exception;

  public Vector<ComuneVO> getComuniLikeProvAndCom(String provincia,
      String comune) throws NotFoundException, SolmrException, Exception;

  public Vector<ComuneVO> getComuniByDescCom(String comune)
      throws NotFoundException, SolmrException, Exception;

  public Vector<ComuneVO> getComuniNonEstintiLikeProvAndCom(String provincia,
      String comune, String flagEstero) throws NotFoundException,
      SolmrException, Exception;

  public Vector<ComuneVO> ricercaStatoEstero(String statoEstero,
      String estinto, String flagCatastoAttivo) throws Exception;

  public void updateSedeLegale(AnagAziendaVO anagAziendaVO,
      long idUtenteAggiornamento) throws SQLException, NotFoundException,
      DataAccessException, Exception, DataControlException;

  public void storicizzaSedeLegale(AnagAziendaVO anagAziendaVO) throws SQLException, NotFoundException,
      DataAccessException, SolmrException, Exception,
      DataControlException;

  public void checkDataCessazione(Long anagAziendaPK, String dataCessazione)
      throws Exception, SolmrException;

  public void cessaAzienda(AnagAziendaVO anagVO, Date dataCess, String causale,
      long idUtenteAggiornamento) throws Exception, SolmrException;

  public String ricercaCodiceComune(String descrizioneComune,
      String siglaProvincia) throws DataAccessException, NotFoundException,
      DataControlException, Exception;

  public void deleteUTE(Long idUte) throws SolmrException, DataAccessException,
      Exception;

  public AnagAziendaVO findAziendaAttiva(Long idAzienda)
      throws DataAccessException, NotFoundException, Exception;

  public void cambiaRappresentanteLegale(Long aziendaPK,
      PersonaFisicaVO personaVO, long idUtenteAggiornamento)
      throws Exception, SolmrException;

  public ComuneVO getComuneByCUAA(String cuaa) throws Exception,
      SolmrException;

  public String getFormaGiuridicaFlagCCIAA(Long idFormaGiuridica)
      throws Exception, SolmrException;

  public ComuneVO getComuneByISTAT(String istat) throws Exception,
      SolmrException;

  public String getComuneFromCF(String codiceFiscale) throws Exception,
      SolmrException;

  public Long ricercaIdAttivitaOTE(String codice, String descrizione)
      throws Exception, SolmrException;

  public Long ricercaIdAttivitaOTE(String codice, String descrizione,
      boolean forPopup) throws Exception, SolmrException;

  public Long ricercaIdAttivitaATECO(String codice, String descrizione)
      throws Exception, SolmrException;

  public Long ricercaIdAttivitaATECO(String codice, String descrizione,
      boolean forPopup) throws Exception, SolmrException;

  public void updateUTE(UteVO uteVO) throws SolmrException,
      DataAccessException, Exception;

  public String ricercaCodiceFiscaleComune(String descrizioneComune,
      String siglaProvincia) throws Exception, DataControlException;

  public String getDescriptionFromCode(String tableName, Integer code)
      throws DataAccessException, Exception;

  public void updateAzienda(AnagAziendaVO anagVO)
      throws Exception, SolmrException;

  public void updateRappLegale(PersonaFisicaVO pfVO, long idUtenteAggiornamento)
      throws Exception, SolmrException;

  public void storicizzaAzienda(AnagAziendaVO anagVO, long idUtenteAggiornamento)
      throws Exception, SolmrException;

  public Vector<CodeDescription> getTipiFormaGiuridicaNonIndividuale()
      throws NotFoundException, Exception;

  public Vector<CodeDescription> getTipiRuoloNonTitolare()
      throws NotFoundException, Exception;

  public void utenteConDelega(UtenteAbilitazioni utenteAbilitazioni, Long idAzienda)
      throws Exception, SolmrException;

  public Vector<PersonaFisicaVO> getSoggetti(Long idAzienda, Date data)
      throws Exception, SolmrException;

  public Vector<PersonaFisicaVO> getSoggetti(Long idAzienda, Boolean storico)
      throws Exception, SolmrException;

  public void checkForDeleteSoggetto(Long idContitolare)
      throws Exception, SolmrException;

  public void deleteContitolare(Long idContitolare) throws Exception,
      SolmrException;

  public PersonaFisicaVO getDettaglioSoggetti(Long idSoggetto, Long idAzienda)
      throws Exception;

  public void inserisciSoggetto(PersonaFisicaVO pfVO, long idUtenteAggiornamento)
      throws Exception, SolmrException;

  public void updateSoggetto(PersonaFisicaVO pfVO, long idUtenteAggiornamento)
      throws Exception, SolmrException;

  public String getFlagPartitaIva(Long idTipoFormaGiuridica)
      throws Exception, SolmrException;
  
  public String getObbligoGfFromFormaGiuridica(Long idTipoFormaGiuridica)
      throws Exception, SolmrException;

  public Long getIdTipologiaAziendaByFormaGiuridica(Long idTipoFormaGiuridica,
      Boolean flagAziendaProvvisoria) throws Exception, SolmrException;

  public Long getIdTipoFormaGiuridica(String idTipoFormaGiuridicaAAEP)
      throws Exception, SolmrException;

  public String getDescTipoFormaGiuridica(String idTipoFormaGiuridicaAAEP)
      throws Exception, SolmrException;

  public boolean isProvinciaReaValida(String siglaProvincia)
      throws Exception, SolmrException;

  public boolean isFlagUnivocitaAzienda(Integer idTipoAzienda)
      throws Exception, SolmrException;

  public PersonaFisicaVO getRappresentanteLegaleFromIdAnagAzienda(
      Long idAnagAzienda) throws Exception, SolmrException;

  public PersonaFisicaVO getRappresentanteLegaleFromIdAnagAziendaAndDichCons(
      long idAnagAzienda, Date dataDichiarazione) throws Exception,
      SolmrException;
  
  public Vector<PersonaFisicaVO> getVAltriSoggettiFromIdAnagAziendaAndDichCons(
      long idAnagAzienda, Date dataDichiarazione) throws Exception,
      SolmrException;

  public UtenteIrideVO getUtenteIrideById(Long idUtente)
      throws Exception, SolmrException;

  public Vector<Long> getAnniRilevamento() throws Exception,
      SolmrException;

  public Vector<CodeDescription> getUnitaProduttive(Long idAzienda)
      throws Exception, SolmrException;

  public TerreniVO getTerreni(Long idAzienda, Long idUte, Long anno,
      String criterio) throws Exception, SolmrException;

  public Vector<Vector<Long>> getIdParticelle(Long idAzienda, Long idUte,
      Long anno, String criterio, Long valore) throws Exception,
      SolmrException;

  public Vector<ParticelleVO> getParticelleByIdRange(
      Vector<Vector<Long>> idRange) throws Exception, SolmrException;
  
  public Vector<TipoSettoreAbacoVO> getListSettoreAbaco()
    throws Exception;

  public Long updateTitolareAzienda(AnagAziendaVO anagAziendaVO,
      PersonaFisicaVO personaTitolareOldVO,
      PersonaFisicaVO personaTitolareNewVO, long idUtenteAggiornamento)
      throws Exception, SolmrException;

  public AnagAziendaVO findAziendaAttivabyCriterio(String cuaa,
      String partitaIva) throws Exception, SolmrException;

  public Vector<StringcodeDescription> getProvincePiemonte()
      throws Exception, SolmrException;

  public ProvinciaVO getProvinciaByCriterio(String criterio)
      throws Exception, SolmrException;

  public String ricercaCodiceComuneNonEstinto(String descrizioneComune,
      String siglaProvincia) throws Exception, SolmrException;
  
  public String ricercaCodiceComuneFlagEstinto(String descrizioneComune,
      String siglaProvincia, String estinto)
          throws Exception, SolmrException;

  public void insertRappLegaleTitolare(Long idAzienda, PersonaFisicaVO pfVO,
      long idUtenteAggiornamento) throws Exception, SolmrException;

  public Vector<Long> getIdPersoneFisiche(String codFiscale, String cognome,
      String nome, String dataNascita, String istatNascita,
      String istatResidenza, boolean personaAttiva) throws Exception,
      SolmrException;

  public Vector<PersonaFisicaVO> getListPersoneFisicheByIdRange(
      Vector<Long> collIdPF) throws Exception, SolmrException;

  public PersonaFisicaVO findByPrimaryKey(Long idPersonaFisica)
      throws Exception, SolmrException;

  public String insediamentoAtomico(AnagAziendaVO modAnagAziendaVO,
      PersonaFisicaVO modPersonaVO, HttpServletRequest request,
      AnagAziendaVO anagAziendaVO, SianUtenteVO sianUtenteVO, RuoloUtenza ruoloUtenza)
      throws Exception, SolmrException;

  public Vector<PersonaFisicaVO> findPersonaFisicaByIdSoggettoAndIdAzienda(
      Long idSoggetto, Long idAzienda) throws Exception, SolmrException;

  public String getDenominazioneByIdAzienda(Long idAzienda)
      throws Exception, SolmrException;

  public String getRappLegaleTitolareByIdAzienda(Long idAzienda)
      throws Exception, SolmrException;

  public Vector<Long> getIdAziendeBySoggetto(Long idSoggetto)
      throws Exception, SolmrException;

  public Vector<AnagAziendaVO> findAziendeByIdAziende(Vector<Long> idAziendeVect)
      throws Exception, SolmrException;

  public String getSiglaProvinciaByIstatProvincia(String istatProvincia)
      throws Exception, SolmrException;

  public PersonaFisicaVO findPersonaFisica(Long idPersonaFisica)
      throws Exception, SolmrException;

  public PersonaFisicaVO getDatiSoggettoPerMacchina(Long idPersonaFisica)
      throws Exception, SolmrException;

  public AnagraficaAzVO getDatiAziendaPerMacchine(Long idAzienda)
      throws Exception, SolmrException;

  public Vector<AnagAziendaVO> getListaStoricoAzienda(Long idAzienda)
      throws Exception, SolmrException;

  // Metodo per recuperare l'elenco delle unità produttive valide associate ad
  // un'azienda agricola
  public Vector<UteVO> getElencoUteAttiveForAzienda(Long idAzienda)
      throws SolmrException, Exception;

  // Metodo per recuperare l'elenco delle sezioni relative ad uno specifico
  // comune
  public Vector<CodeDescription> getSezioniByComune(String istatComune)
      throws SolmrException, Exception;

  public Vector<FoglioVO> getFogliByComuneAndSezione(String istatComune,
      String sezione, Long foglio) throws SolmrException, Exception;

  public Vector<ParticellaVO> getParticelleByParametri(
      String descrizioneComune, Long foglio, String sezione, Long particella,
      String flagEstinto) throws SolmrException, Exception;

  public String ricercaSezione(String istatComune, String sezione)
      throws SolmrException, Exception;

  public FoglioVO ricercaFoglio(String istatComune, String sezione, Long foglio)
      throws SolmrException, Exception;

  public ParticellaVO ricercaParticellaAttiva(String istatComune,
      String sezione, Long foglio, Long particella, String subalterno)
      throws SolmrException, Exception;

  public void checkParticellaByAzienda(Long idParticella, Long idAzienda)
      throws SolmrException, Exception;

  public Vector<ElencoAziendeParticellaVO> elencoAziendeByParticellaAndConduzione(
      Long idParticella, Long idAzienda) throws SolmrException, Exception;

  public Vector<CodeDescription> getTipiTitoloPossessoExceptProprieta()
      throws SolmrException, Exception;

  public java.util.Date getMaxDataFineConduzione(Long idParticella,
      Long idAzienda) throws SolmrException, Exception;

  public Vector<CodeDescription> getTipiUtilizzoAttivi() throws SolmrException,
      Exception;

  public Vector<CodeDescription> getTipiUtilizzoAttivi(int idIndirizzo)
      throws SolmrException, Exception;

  public Long insertParticella() throws SolmrException, Exception;

  public ParticellaVO ricercaParticellaProvvisoriaAttiva(String istatComune,
      String sezione, Long foglio) throws SolmrException, Exception;

  public ParticellaVO ricercaParticella(String istatComune, String sezione,
      Long foglio, Long particella, String subalterno) throws SolmrException,
      Exception;

  public void cessaParticelleByIdParticellaRange(long idParticella[])
      throws SolmrException, Exception;

  public double getMaxSupCatastaleInseribile(Long idParticella)
      throws SolmrException, Exception;

  public ParticellaVO findParticellaByPrimaryKey(Long idStoricoParticella)
      throws SolmrException, Exception;

  public Vector<CodeDescription> getTitoliStudio() throws SolmrException,
      Exception;

  public Vector<CodeDescription> getPrefissiCellulare() throws SolmrException,
      Exception;

  public Vector<CodeDescription> getIndirizzoStudioByTitolo(Long idTitoloStudio)
      throws SolmrException, Exception;

  public Vector<ContoCorrenteVO> getContiCorrenti(Long idAzienda,
      java.util.Date dataSituazioneAl) throws Exception;

  public Vector<ContoCorrenteVO> getContiCorrenti(Long idAzienda,
      boolean estinto) throws Exception;

  public void deleteContoCorrente(Long idConto, long idUtenteAggiornamento)
      throws Exception, SolmrException;

  public BancaSportelloVO[] searchBanca(String abi, String denominazione)
      throws Exception, SolmrException;

  public BancaSportelloVO[] searchSportello(String abi, String cab,
      String comune) throws Exception, SolmrException;

  public Vector<CodeDescription> getTipiTipologiaFabbricato()
      throws SolmrException, Exception;

  public Vector<CodeDescription> getTipiFormaFabbricato(
      Long idTipologiaFabbricato) throws SolmrException, Exception;

  public Vector<CodeDescription> getTipiColturaSerra(Long idTipologiaFabbricato)
      throws SolmrException, Exception;

  public String getUnitaMisuraByTipoFabbricato(Long idTipologiaFabbricato)
      throws SolmrException, Exception;

  public int getMesiRiscaldamentoBySerra(String tipologiaColturaSerra)
      throws SolmrException, Exception;

  public double getFattoreCubaturaByFormaFabbricato(String idFormaFabbricato)
      throws SolmrException, Exception;

  public Vector<ParticellaVO> getElencoParticelleFabbricatoByUte(Long idUte,
      boolean serra) throws SolmrException, Exception;

  public String getSuperficiFabbricatiByParticella(Long idUte, Long idParticella)
      throws SolmrException, Exception;

  public Long inserisciFabbricato(FabbricatoVO fabbricatoVO,
      Vector<ParticellaVO> elencoParticelleSelezionate, long idUtenteAggiornamento)
      throws SolmrException, Exception;

  public Vector<FabbricatoVO> ricercaFabbricatiByAzienda(Long idAzienda,
      String dataSituazioneAl) throws SolmrException, Exception;

  public void insertContoCorrente(ContoCorrenteVO conto,
      long idUtenteAggiornamento) throws Exception, SolmrException;
  
  public void storicizzaContoCorrente(ContoCorrenteVO conto, Long idUtente) 
    throws Exception, SolmrException;

  public void desistsAccountCorrent(Long idAzienda, Long idUtente)
      throws SolmrException, Exception;

  public ContoCorrenteVO getContoCorrente(String idContoCorrente)
      throws Exception;

  public FabbricatoVO findFabbricatoByPrimaryKey(Long idFabbricato)
      throws SolmrException, Exception;

  public Vector<ParticellaVO> getElencoParticelleByFabbricato(
      FabbricatoVO fabbricatoVO, boolean modifica) throws Exception;

  public Vector<ParticellaVO> getElencoParticelleFabbricatoByUteAssociabili(
      Long idUte, Vector<Long> elencoParticelle, boolean serra)
      throws SolmrException, Exception;

  public void cessaUtilizzoParticellaFabbricato(FabbricatoVO fabbricatoVO)
      throws SolmrException, Exception;

  public Long modificaFabbricato(FabbricatoVO fabbricatoVO,
      Vector<ParticellaVO> particelleForFabbricato,
      Vector<ParticellaVO> elencoParticelleAssociate,
      Vector<ParticellaVO> elencoParticelleAssociabili, long idUtenteAggiornamento,
      long idAzienda) throws SolmrException, Exception;

  public void deleteParticellaFabbricatoByIdFabbricato(FabbricatoVO fabbricatoVO)
      throws SolmrException, Exception;

  public void deleteFabbricato(FabbricatoVO fabbricatoVO)
      throws SolmrException, Exception;

  public void eliminaFabbricato(FabbricatoVO fabbricatoVO, long idAzienda)
      throws SolmrException, Exception;

  public Vector<UteVO> getElencoUteAttiveForDateAndAzienda(Long idAzienda,
      String data) throws SolmrException, Exception;

  public Vector<ParticellaVO> getElencoParticelleForUteAndAzienda(Long idAzienda)
      throws SolmrException, Exception;

  public Vector<ParticellaVO> getElencoParticelleForUteAndAziendaPossAndComune(
      Long idAzienda) throws SolmrException, Exception;

  public Vector<ParticellaVO> getElencoParticelleForUteAndAziendaComune(
      Long idAzienda) throws SolmrException, Exception;

  public Vector<ParticellaVO> ricercaParticelleAttiveByParametri(
      ParticellaVO particellaVO, String data, Long idAzienda)
      throws SolmrException, Exception;

  public void checkCessaAziendaByConduzioneParticella(Long idUte)
      throws SolmrException, Exception;

  public boolean isDataInizioValida(long idAzienda, Date dataInizio)
      throws SolmrException, Exception;

  public Vector<CodeDescription> getListaDateConsistenza(Long idAzienda)
      throws SolmrException, Exception;

  public Vector<ParticellaVO> getElencoConsistenzaParticelleForAzienda(
      Long idAzienda, Long idDichiarazioneConsistenza) throws SolmrException,
      Exception;

  public void checkLastAggiornamentoAfterMaxDichConsistenza(Long idAzienda)
      throws SolmrException, Exception;

  public Vector<ParticellaVO> getElencoConsistenzaParticelleForAziendaAndPossessoAndComune(
      Long idAzienda, Long idDichiarazioneConsistenza) throws SolmrException,
      Exception;

  public Vector<ParticellaVO> getElencoConsistenzaParticelleForAziendaAndComune(
      Long idAzienda, Long idDichiarazioneConsistenza) throws SolmrException,
      Exception;

  public String getAnnoPrevisioneUtilizzi(Long idAzienda)
      throws SolmrException, Exception;

  public String getTotaleSupCondotteByAzienda(Long idAzienda, String data)
      throws SolmrException, Exception;

  public Vector<ParticellaVO> getElencoConsistenzaParticelleForAziendaAndDestinazioneUso(
      Long idAzienda, Long idDichiarazioneConsistenza,
      Long idConduzioneParticella) throws SolmrException, Exception;

  public Vector<ParticellaVO> ricercaParticelleStoricizzateByParametri(
      ParticellaVO particellaVO, Long idDichiarazioneConsistenza)
      throws SolmrException, Exception;

  public ParticellaVO getDettaglioParticellaDatiTerritoriali(
      Long idStoricoParticella) throws SolmrException, Exception;

  public Vector<ParticellaUtilizzoVO> getElencoParticellaUtilizzoVO(
      Long idConduzioneParticella, String anno) throws SolmrException,
      Exception;

  public double getTotaleSupUtilizzateByIdConduzioneParticella(
      Long idConduzioneParticella, String anno) throws SolmrException,
      Exception;

  public ParticellaVO getDettaglioParticellaStoricizzataDatiTerritoriali(
      Long idConduzioneDichiarata) throws SolmrException, Exception;

  public ParticellaVO getDettaglioParticellaStoricizzataConduzione(
      Long idConduzioneDichiarata) throws SolmrException, Exception;

  public Vector<ParticellaUtilizzoVO> getElencoStoricoParticellaUtilizzoVO(
      Long idConduzioneDichiarata, Long idDichiarazioneConsistenza)
      throws SolmrException, Exception;

  public Vector<CodeDescription> getElencoIndirizziUtilizzi()
      throws SolmrException, Exception;

  public Vector<CodeDescription> getTipiUtilizzoForIdIndirizzoTipoUtilizzo(
      Long idTipoIndirizzoUtilizzo) throws SolmrException, Exception;

  public Vector<ParticellaVO> ricercaParticelleByParametriAndUtilizzi(
      ParticellaVO particellaRicercaVO, Long idAzienda) throws SolmrException,
      Exception;

  public Vector<ParticellaVO> ricercaParticelleByParametriAndUtilizzoSpecificato(
      Long idAzienda, ParticellaVO particellaRicercaVO) throws SolmrException,
      Exception;

  public Vector<ParticellaVO> ricercaParticelleByParametriSenzaUsoSuolo(
      ParticellaVO particellaRicercaVO, Long idAzienda) throws SolmrException,
      Exception;

  public Vector<ParticellaVO> ricercaParticelleStoricizzateByParametriAndUtilizzo(
      ParticellaVO particellaRicercaVO) throws SolmrException, Exception;

  public Vector<ParticellaVO> ricercaParticelleStoricizzateByParametriAndUtilizzoSpecificato(
      ParticellaVO particellaRicercaVO) throws Exception, SolmrException;

  public Vector<ParticellaVO> ricercaParticelleStoricizzateByParametriSenzaUsoSuolo(
      ParticellaVO particellaRicercaVO) throws SolmrException, Exception;

  public Vector<CodeDescription> getTipiRuoloNonTitolareAndNonSpecificato()
      throws Exception, SolmrException;

  public Long storicizzaDatiResidenza(PersonaFisicaVO pfVO, long idUtenteAggiornamento)
      throws Exception, SolmrException;

  public void changeLegameBetweenPersoneAndAziende(Long newIdSoggetto,
      Long oldIdSoggetto, Long idAzienda) throws Exception, SolmrException;

  public void cessaLegameBetweenPersonaAndAzienda(Long idSoggetto,
      Long idAzienda) throws Exception, SolmrException;

  public void checkUpdateSuperficie(Long idAzienda) throws SolmrException,
      Exception;

  public ParticellaVO getParticellaVOByIdUtilizzoParticella(
      Long idUtilizzoParticella) throws SolmrException, Exception;

  public ParticellaVO getParticellaVOByIdConduzioneParticella(
      Long idConduzioneParticella) throws SolmrException, Exception;

  public double getTotSupUtilizzateByIdCondParticellaNotIncludeIdUtilizzo(
      Long idConduzioneParticella, Long idUtilizzoParticella, String anno)
      throws SolmrException, Exception;

  public String getValoreFromParametroByIdCode(String codice)
      throws Exception, SolmrException;


  public Date getMaxDataDichiarazioneConsistenza(Long idAzienda)
      throws SolmrException, Exception;

  public String getTotaleSupCondotteByIdParticellaNotIncludeIdConduzioneParticella(
      Long idConduzioneParticella, Long idParticella) throws SolmrException,
      Exception;

  public Vector<ParticellaVO> getElencoDichiarazioniConsistenzaByIdAzienda(
      Long idAzienda) throws SolmrException, Exception;

  public String getTotaleSupCondotteDichiarateByAzienda(Long idAzienda,
      Long idDichiarazioneConsistenza) throws SolmrException, Exception;

  public void checkEliminaUtilizziVigneto(
      Vector<String> elencoIdUtilizziParticella) throws SolmrException,
      Exception;

  public void checkParticellaLegataDichiarazioneConsistenza(
      Vector<Long> elencoParticelle) throws Exception, SolmrException;

  public void deleteUtilizzoParticellaByIdConduzioneParticella(
      Long idConduzioneParticella) throws Exception, SolmrException;

  public void deleteConduzioneParticella(Long idConduzioneParticella)
      throws SolmrException, Exception;

  public void eliminaParticelle(Vector<Long> elencoConduzioni, Long idAzienda, RuoloUtenza ruoloUtenza)
      throws SolmrException, Exception;

  public void cessaFabbricato(Long idFabbricato, long idUtenteAggiornamento)
      throws SolmrException, Exception;

  public void storicizzaManodopera(ManodoperaVO manodoperaVO)
      throws SolmrException, Exception;
  
  public Vector<TipoIscrizioneINPSVO> getElencoTipoIscrizioneINPSAttivi()
      throws SolmrException, Exception;
  
  public ManodoperaVO findManodoperaAttivaByIdAzienda(long idAzienda)
      throws SolmrException, Exception;

  public void cessazioneUTE(UteVO uteVO, long idUtenteAggiornamento)
      throws SolmrException, Exception;

  public ParticellaVO getParticellaVOByIdConduzioneParticellaSenzaUsoSuolo(
      Long idConduzioneParticella, String anno) throws SolmrException,
      Exception;

  public ParticellaVO getDettaglioParticellaDatiTerritorialiByIdConduzioneParticella(
      Long idConduzioneParticella) throws SolmrException, Exception;

  public void allegaUtilizziToNewConduzioneParticella(
      Long newIdConduzioneParticella, Long oldIdConduzioneParticella)
      throws SolmrException, Exception;

  public PersonaFisicaVO getDettaglioSoggettoByIdContitolare(Long idContitolare)
      throws Exception, SolmrException;
  
  public  TesserinoFitoSanitarioVO getTesserinoFitoSanitario(String codiceFiscale) 
      throws Exception, SolmrException;

  public Vector<String> getListCUAAByIdAzienda(Long idAzienda) throws Exception;

  public String getDenominazioneAziendaByCuaaAndIdAzienda(Long idAzienda,
      String cuaa) throws Exception;

  public Vector<AziendaCollegataVO> getAziendeCollegateByIdAzienda(
      Long idAzienda, boolean flagStorico) throws Exception,
      SolmrException;

  public Vector<AnagAziendaVO> getEntiAppartenenzaByIdAzienda(Long idAzienda,
      boolean flagStorico) throws Exception, SolmrException;

  public AnagAziendaVO findAziendaByIdAnagAzienda(Long anagAziendaPK)
      throws Exception, SolmrException;

  public Vector<AziendaCollegataVO> getAziendeCollegateByRangeIdAziendaCollegata(
      Vector<Long> vIdAziendaCollegata) throws Exception, SolmrException;

  public Vector<AnagAziendaVO> getIdAziendaCollegataAncestor(Long idAzienda)
      throws Exception, SolmrException;

  public Vector<AnagAziendaVO> getIdAziendaCollegataDescendant(Long idAzienda)
      throws Exception, SolmrException;

  public boolean controlloAziendeAssociate(String CUAApadre,
      Long idAziendaCollegata) throws Exception;

  public Vector<AnagAziendaVO> getAziendeCollegateByRangeIdAzienda(
      Vector<Long> vIdAzienda, Long idAziendaPadre) throws Exception,
      SolmrException;

  // INIZIO TERRENI
  public Vector<ParticellaVO> ricercaTerreniByParametri(
      ParticellaVO particellaRicercaTerrenoVO) throws SolmrException,
      Exception;

  public boolean isParticellaContenziosoOnAzienda(Long idStoricoParticella)
      throws SolmrException, Exception;

  public String getTotSupCondotteByIdStoricoParticella(Long idStoricoParticella)
      throws SolmrException, Exception;

  public Vector<ParticellaAziendaVO> getElencoAziendeAndConduzioniByIdStoricoParticella(
      Long idStoricoParticella, boolean attive) throws Exception,
      SolmrException;

  public Vector<ParticellaUtilizzoVO> getElencoUtilizziAttiviByAnnoAndIdStoricoParticella(
      Long idStoricoParticella, String anno) throws Exception,
      SolmrException;

  public Vector<ParticellaUtilizzoVO> getElencoParticelleForAziendaAndUsoSecondario(
      Long idAzienda, String anno) throws SolmrException, Exception;

  public Vector<ParticellaUtilizzoVO> getElencoConsistenzaParticelleForAziendaAndUsoSecondario(
      Long idAzienda, Long idDichiarazioneConsistenza) throws SolmrException,
      Exception;

  public Vector<EsitoControlloParticellaVO> getElencoEsitoControlloParticella(
      Long idConduzioneParticella) throws Exception, SolmrException;

  public String getDataUltimaEsecuzioneControlli(Long idAzienda)
      throws Exception;

  public ParticellaCertificataVO findParticellaCertificataByParametri(
      ParticellaVO particellaVO) throws Exception;

  public Vector<CodeDescription> getTipiDocumento() throws Exception;

  public Vector<ParticellaVO> getElencoStoricoParticella(Long idParticella)
      throws SolmrException, Exception;

  public void eliminaUtilizzoParticella(Vector<String> elencoConduzioni,
      Vector<String> elencoIdUtilizzoParticella, long idUtenteAggiornamento)
      throws SolmrException, Exception;

  public Vector<ParticellaVO> getElencoParticelleForImportByAzienda(
      AnagAziendaVO searchAnagAziendaVO, AnagAziendaVO anagAziendaVO,
      RuoloUtenza ruoloUtenza) throws Exception, SolmrException;

  public ParticellaVO getStoricoParticella(Long idStoricoParticella)
      throws Exception;

  public Vector<CodeDescription> getIndirizziTipiUtilizzoAttivi()
      throws SolmrException, Exception;

  public Vector<CodeDescription> getTipiAreaA() throws NotFoundException,
      Exception;

  public Vector<CodeDescription> getTipiAreaB() throws NotFoundException,
      Exception;

  public Vector<CodeDescription> getTipiAreaC() throws NotFoundException,
      Exception;

  public Vector<CodeDescription> getTipiAreaD() throws NotFoundException,
      Exception;

  public Vector<CodeDescription> getTipiZonaAltimetrica()
      throws NotFoundException, Exception;

  public Vector<CodeDescription> getTipiCasoParticolare()
      throws Exception, NotFoundException;

  // FINE TERRENI

  // NOTIFICHE
  public ElencoNotificheVO ricercaNotificheByParametri(NotificaVO notificaVO,
      UtenteAbilitazioni utenteAbilitazioni, RuoloUtenza ruoloUtenza, Boolean storico, int maxRecord) throws Exception, SolmrException;

  public NotificaVO findNotificaByPrimaryKey(Long idNotifica, String provenienza)
      throws SolmrException, Exception;

  public Vector<NotificaVO> getElencoNotificheByIdAzienda(NotificaVO notificaVO,
      Boolean storico, String ordinamento) throws Exception,
      SolmrException;
  
  public Vector<NotificaVO> getElencoNotifichePopUp(NotificaVO notificaVO) 
      throws Exception;

  public Long insertNotifica(NotificaVO notificaVO, long idUtenteAggiornamento)
      throws Exception, SolmrException;
  
  public void updateNotifica(NotificaVO notificaVO, long idUtenteAggiornamento)
      throws Exception, SolmrException;

  public void closeNotifica(NotificaVO notificaVO, long idUtenteAggiornamento)
      throws Exception, SolmrException;
  
  public Long getIdTipologiaNotificaFromCategoria(Long idCategoriaNotifica)
      throws Exception;
  
  public boolean isChiusuraNotificaRuoloPossibile(RuoloUtenza ruoloUtenza, long idCategoriaNotifica)
      throws Exception;
  
  public Vector<NotificaVO> getElencoNotificheForIdentificato(long identificativo, 
      String codiceTipo, long idAzienda, Long idDichiarazioneConsistenza) 
    throws Exception;
  
  public boolean isModificaNotificaRuoloPossibile(RuoloUtenza ruoloUtenza, long idCategoriaNotifica)
      throws Exception;
  
  public HashMap<Long,Vector<NotificaEntitaVO>> getNotificheEntitaUvFromIdNotifica(
      long ids[]) throws Exception;
  
  public HashMap<Long,Vector<NotificaEntitaVO>> getNotificheEntitaParticellaFromIdNotifica(
      long ids[]) throws Exception;

  public Vector<CodeDescription> getTipiTipologiaNotifica() throws SolmrException,
      Exception;
  
  public Vector<CodeDescription> getTipologiaNotificaFromRuolo(RuoloUtenza ruoloUtenza) 
      throws SolmrException, Exception;
  
  public Vector<CodeDescription> getTipiTipologiaNotificaFromEntita(String tipoEntita)
      throws SolmrException, Exception;
  
  public Vector<TipoCategoriaNotificaVO> getTipiCategoriaNotificaFromRuolo(RuoloUtenza ruoloUtenza) 
      throws SolmrException, Exception;
  
  public Vector<TipoCategoriaNotificaVO> getTipiCategoriaNotificaFromEntita(String tipoEntita)
      throws SolmrException, Exception;
  
  public Vector<CodeDescription> getCategoriaNotifica() throws SolmrException, Exception;

  // NOTIFICHE

  // PROFILAZIONE
  public DelegaVO intermediarioConDelega(UtenteAbilitazioni utenteAbilitazioni, Long idAzienda)
      throws Exception, SolmrException;

  public boolean isIntermediarioConDelegaDiretta(long idIntermediario,
      Long idAzienda) throws Exception;

  public boolean isIntermediarioPadre(long idIntermediario, Long idAzienda)
      throws Exception;

  // PROFILAZIONE

  // ALTRE RICERCHE
  public Vector<AnagAziendaVO> getElencoAziendeByCAA(DelegaVO delegaVO)
      throws Exception, SolmrException;

  // PRATICHE
  public Vector<ProcedimentoAziendaVO> updateAndGetPraticheByAzienda(Long idAzienda)
      throws SolmrException, Exception;

  public Vector<ProcedimentoAziendaVO> getElencoProcedimentiByIdAzienda(
      Long idAzienda, Long annoProc, Long idProcedimento,
      Long idAziendaSelezionata) throws NotFoundException, Exception,
      SolmrException;

  public CodeDescription[] getListCuaaAttiviProvDestByIdAzienda(Long idAzienda)
      throws Exception;

  // PRATICHE

  // SIAN
  public String getDescriptionSIANFromCode(String tableName, String code)
      throws SolmrException, Exception;

  public StringcodeDescription getSianTipoSpecieByCodiceSpecie(
      String codiceSpecie) throws Exception, SolmrException;

  public StringcodeDescription getSianTipoSpecieByIdSpecieAnimale(
      long idTipoSpecieAnimale) throws Exception;

  public void sianAggiornaDatiTributaria(String[] elencoCuaa, SianUtenteVO sianUtenteVO)
      throws Exception;

  public DoubleStringcodeDescription[] getListSianTipoOpr(boolean principale,
      String orderBy[]) throws Exception;

  public void sianAggiornaDatiBDN(String CUAA) throws SolmrException,
      Exception;

  public String getOrganismoPagatoreFormatted(String codiceSianOpr)
      throws Exception;

  // SIAN

  // ANAGRAFICA AZIENDA
  public void updateAnagrafe(AnagAziendaVO anagAziendaVO,
      long idUtenteAggiornamento, PersonaFisicaVO pfVO, 
      boolean isCuaaChanged, PersonaFisicaVO pfVOTributaria, Vector<Long> vIdAtecoTrib) throws Exception;

  public void updateAnagrafeSemplice(AnagAziendaVO anagAziendaVO, long idUtenteAggiornamento) 
      throws Exception;
  
  public void checkStatoAzienda(Long idAzienda) throws SolmrException,
      Exception;

  public Date getMinDataInizioValiditaAnagraficaAzienda(Long idAzienda)
      throws Exception;

  public void modificaGestoreFascicolo(AnagAziendaVO anagAziendaVO)
      throws Exception;

  public java.util.Date getDataMaxFineMandato(Long idAzienda)
      throws Exception;

  public Long insertDelegaForMandato(AnagAziendaVO anagAziendaVO,
      RuoloUtenza ruoloUtenza, DelegaVO delegaVO, DocumentoVO documentoVO)
      throws Exception, SolmrException;

  public DelegaVO getDelegaByAziendaAndIdProcedimento(Long idAzienda,
      Long idProcedimento) throws Exception;

  public java.util.Date getDataMaxInizioMandato(Long idAzienda)
      throws Exception;

  public DelegaVO[] getStoricoDelegheByAziendaAndIdProcedimento(Long idAzienda,
      Long idProcedimento, String[] orderBy) throws Exception;

  public AnagAziendaVO[] getListAnagAziendaVOByCuaa(String cuaa,
      boolean onlyActive, boolean isCessata, String[] orderBy)
      throws Exception;

  public Date getMaxDataInizioValiditaAnagraficaAzienda(Long idAzienda)
      throws Exception;

  public AnagAziendaVO[] getListAnagAziendaDestinazioneByIdAzienda(
      Long idAzienda, boolean onlyActive, String[] orderBy)
      throws Exception;

  public AnagAziendaVO[] getListAziendeParticelleAsservite(
      Long idStoricoParticella, Long idAzienda, Long idTitoloPossesso, Date dataInserimentoDichiarazione)
      throws Exception;

  // ANAGRAFICA AZIENDA

  // SOGGETTI COLLEGATI
  public void updateDatiSoggettoAndStoricizzaResidenza(
      PersonaFisicaVO newPersonaFisicaVO, PersonaFisicaVO oldPersonaFisicaVO,
      long idUtenteAggiornamento) throws Exception, SolmrException;

  // SOGGETTI COLLEGATI

  /* Gestione Allevamenti */
  public Vector<AllevamentoAnagVO> getAllevamentiByIdUTE(Long idUTE, int anno)
      throws SolmrException, Exception;

  public Vector<Vector<AllevamentoAnagVO>> getAllevamentiByIdAzienda(Long idAzienda, int anno)
      throws SolmrException, Exception;

  public AllevamentoAnagVO getAllevamento(Long idAllevamento)
      throws SolmrException, Exception;

  public Vector<CategorieAllevamentoAnagVO> getCategorieAllevamento(Long idAllevamento)
      throws SolmrException, Exception;

  public Long insertAllevamento(AllevamentoAnagVO allevamentoVO,
      long idUtenteAggiornamento) throws SolmrException, Exception;

  public Vector<UteVO> getElencoIdUTEByIdAzienda(Long idAzienda)
      throws SolmrException, Exception;

  public Vector<TipoASLAnagVO> getTipiASL() throws SolmrException, Exception;

  public Vector<TipoSpecieAnimaleAnagVO> getTipiSpecieAnimale() throws SolmrException, Exception;

  public Vector<TipoSpecieAnimaleAnagVO> getTipiSpecieAnimaleAzProv() throws SolmrException,
      Exception;

  public void deleteAllevamentoAll(Long idAllevamento) throws SolmrException,
      Exception;

  public Vector<TipoCategoriaAnimaleAnagVO> getCategorieByIdSpecie(Long idSpecie) throws SolmrException,
      Exception;

  public void updateAllevamento(AllevamentoAnagVO all,
      long idUtenteAggiornamento) throws SolmrException, Exception;

  public Integer[] getAnniByIdAzienda(Long idAzienda) throws SolmrException,
      Exception;

  public Vector<CodeDescription> getTipoTipoProduzione(long idSpecie) throws SolmrException, Exception;

  public Vector<CodeDescription> getOrientamentoProduttivo(long idSpecie, long idTipoProduzione)
      throws SolmrException, Exception;

  public Vector<CodeDescription> getTipoProduzioneCosman(long idSpecie, long idTipoProduzione, long idOrientamentoProduttivo) 
      throws SolmrException, Exception;
  
  public Vector<CodeDescription> getSottocategorieCosman(long idSpecie, 
      long idTipoProduzione, long idOrientamentoProduttivo, long idTipoProduzioneCosman, String flagEsiste)
      throws SolmrException, Exception;
  
  
  public void storicizzaAllevamento(AllevamentoAnagVO all,
      long idUtenteAggiornamento) throws SolmrException, Exception;

  public Vector<AllevamentoAnagVO> getAllevamentiByIdAziendaOrdinati(Long idAzienda, int anno)
      throws SolmrException, Exception;

  public TipoSpecieAnimaleAnagVO getTipoSpecieAnimale(Long idSpecieAnimale)
      throws Exception;

  public boolean isRecordSianInAnagrafe(AnagAziendaVO anagAziendaVO,
      SianAllevamentiVO sianAllevamentiVO) throws Exception;

  public String getIstatProvinciaBySiglaProvincia(String siglaProvincia)
      throws Exception;
  
  public String getRegioneByProvincia(String siglaProvincia) throws Exception;

  public Vector<Long> getListIdUteByIstatComuneAndIdAzienda(String istatComune,
      Long idAzienda, boolean isActive) throws Exception, SolmrException;

  public TipoASLAnagVO getTipoASLAnagVOByExtIdAmmCompetenza(
      Long idAmmCompetenza, boolean isActive) throws Exception,
      SolmrException;

  public AllevamentoAnagVO[] getListAllevamentiAziendaByPianoRifererimento(
      Long idAzienda, Long idPianoRiferimento, Long idUte, String[] orderBy)
      throws Exception;
  
  public TipoCategoriaAnimaleAnagVO getTipoCategoriaAnimale(Long idCategoriaAnimale)
      throws Exception;

  /* Gestione Allevamenti */

  /* Manodopera */
  public Vector<FrmManodoperaVO> getManodoperaAnnua(ManodoperaVO manodoperaVO)
      throws SolmrException, Exception;

  public Vector<FrmManodoperaVO> getManodoperaByPianoRifererimento(ManodoperaVO manodoperaVO,
      Long idPianoRiferimento) throws SolmrException, Exception;

  public ManodoperaVO dettaglioManodopera(Long idManodopera)
      throws SolmrException, Exception;

  public void deleteManodopera(Long idManodopera) throws SolmrException,
      Exception;

  public void insertManodopera(ManodoperaVO manodoperaVO, Long idAzienda)
      throws SolmrException, Exception;

  public Vector<CodeDescription> getTipoFormaConduzione() throws SolmrException, Exception;

  public Vector<CodeDescription> getTipoAttivitaComplementari() throws SolmrException,
      Exception;

  public Vector<CodeDescription> getTipoClassiManodopera() throws SolmrException,
      Exception;

  public ManodoperaVO findLastManodopera(Long idAzienda) throws SolmrException,
      Exception;

  public String isManodoperaValida(Long idManodopera, Long idAzienda)
      throws SolmrException, Exception;

  public void updateManodopera(ManodoperaVO manodoperaVO, Long idAzienda)
      throws SolmrException, Exception;
  
  public void updateManodoperaSian(ManodoperaVO manodoperaChgVO, long idAzienda, long idUtente)
      throws SolmrException, Exception;

  /* Manodopera */

  public Vector<ParticellaVO> getElencoParticelleForAziendaAndUtilizzo(Long idAzienda,
      String anno) throws SolmrException, Exception;

  /* Consistenza */
  public boolean previsioneAnnoSucessivo(Long idAzienda) throws Exception;

  public boolean controlloUltimeModifiche(Long idAzienda, Integer anno)
      throws Exception;

  public String controlliDichiarazionePLSQL(Long idAzienda, Integer anno,
      Long idMotivoDichiarazione, Long idUtente) throws Exception, SolmrException;

  public void controlliParticellarePLSQL(Long idAzienda, Integer anno, Long idUtente)
      throws Exception, SolmrException;

  public String controlliVerificaPLSQL(Long idAzienda, Integer anno,
      Integer idGruppoControllo, Long idUtente) throws Exception, SolmrException;

  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichiarazioneConsistenza(Long idAzienda,
      Long idMotivoDichiarazione) throws Exception;
  
  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichConsPG(long idDichiarazioneConsistenza, long fase) 
      throws Exception;

  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichConsistenzaTerreni(Long idAzienda)
      throws Exception;

  public Long salvataggioDichiarazionePLQSL(ConsistenzaVO consistenzaVO,
      Long idAzienda, Integer anno, RuoloUtenza ruoloUtenza)
      throws Exception, SolmrException;

  public Vector<ConsistenzaVO> getDichiarazioniConsistenza(Long idAzienda)
      throws Exception;

  public Vector<ConsistenzaVO> getDichiarazioniConsistenzaMinimo(Long idAzienda)
      throws Exception;

  public ConsistenzaVO getDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza) throws Exception;

  public Vector<TemporaneaPraticaAziendaVO> aggiornaPraticaAziendaPLQSL(
      Long idAzienda, Long idUtente, Long idDichiarazioneConsistenza)
      throws Exception, SolmrException;
  
  public void aggiornaPraticaAziendaPLQSL(Long idAzienda) 
      throws Exception, SolmrException;

  public boolean deleteDichConsAmmessa(Long idAzienda, Long idUtente,
      Long idDichiarazioneConsistenza) throws Exception, SolmrException;

  public boolean newDichConsAmmessa(Long idAzienda, Long idUtente,
      Long idMotivoDichiarazione) throws Exception, SolmrException;

  public ErrAnomaliaDicConsistenzaVO getAnomaliaDichiarazioneConsistenza(
      Long idDichiarazioneSegnalazione) throws Exception;

  public Vector<ErrAnomaliaDicConsistenzaVO> getAnomaliePerCorrezione(
      Long elencoIdDichiarazioneSegnalazione[], long idMotivoDichiarazione)
      throws Exception;

  public Vector<DichiarazioneSegnalazioneVO> getListDichiarazioneSegnalazioni(Long idAzienda,
      Long idDichiarazioneConsistenza, Long idStoricoParticella)
      throws Exception;

  public ErrAnomaliaDicConsistenzaVO[] getListAnomalieByIdDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza, Long idFase,
      ErrAnomaliaDicConsistenzaVO errAnomaliaDicConsistenzaRicercaVO,
      String[] orderBy) throws Exception;

  public ConsistenzaVO[] getListDichiarazioniConsistenzaByIdAzienda(
      Long idAzienda, String[] orderBy) throws Exception;
  
  public ConsistenzaVO[] getListDichiarazioniConsistenzaByIdAziendaVarCat(
      Long idAzienda, String[] orderBy) throws Exception;

  public void ripristinaPianoRiferimento(Long idDichiarazioneConsistenza,
      Long idUtente) throws SolmrException, Exception;

  public Vector<TipoMotivoDichiarazioneVO> getListTipoMotivoDichiarazione(long idAzienda) 
      throws Exception;

  public Vector<DichiarazioneSegnalazioneVO> getListDichiarazioneSegnalazioniUnar(Long idAzienda,
      Long idDichiarazioneConsistenza, Long idStoricoUnitaArborea,
      String[] orderBy) throws Exception;
  
  public Vector<DichiarazioneSegnalazioneVO> getListDichiarazioneSegnalazioniConsolidamentoUV(
      Long idAzienda) throws Exception;

  public ConsistenzaVO findDichiarazioneConsistenzaByPrimaryKey(
      Long idDichiarazioneConsistenza) throws Exception;
  
  public FascicoloNazionaleVO getInfoRisultatiSianDichiarazioneConsistenza(
      long idDichiarazioneConsistenza) throws Exception;
  
  public Vector<ConsistenzaVO> getListDichiarazioniPianoGrafico(Long idAzienda)
      throws Exception;
  
  public EsitoPianoGraficoVO getEsitoPianoGraficoFromAccesso(long idAccessoPianoGrafico)
      throws Exception;
  
  public int preCaricamentoPianoGrafico(long idDichiarazioneConsistenza) throws Exception;

  public EsitoPianoGraficoVO getEsitoPianoGraficoFromPK(long idEsitoGrafico) throws Exception;
  
  public PlSqlCodeDescription controlliValidazionePlSql(long idAzienda, int idFase, 
      long idUtente, long idDichiarazioneConsistenza) throws Exception;
  
  public Long insertStatoIncorsoPG(long idAccessoPianoGrafico, long idUtente) 
      throws Exception;
  
  public void protocollaDichiarazioniConsistenza(
      Long[] elencoIdDichiarazioniConsistenza, RuoloUtenza ruoloUtenza,
      String anno) throws Exception;

  public TipoControlloVO[] getListTipoControlloByIdDichiarazioneConsistenzaAndIdGruppoControllo(
      Long idDicharazioneConsistenza, Long idGruppoControllo, String orderBy[])
      throws Exception;

  public TipoControlloVO[] getListTipoControlloByIdGruppoControllo(
      Long idGruppoControllo, String orderBy[]) throws Exception;
  
  public TipoControlloVO[] getListTipoControlloByIdGruppoControlloAttivi(Long idGruppoControllo, String orderBy[])
      throws Exception;
  
  public TipoControlloVO[] getListTipoControlloForAziendaByIdGruppoControllo(
      Long idGruppoControllo, Long idAzienda, String orderBy[]) throws Exception;
  
  public TipoControlloVO[] getListTipoControlloForAziendaByIdGruppoControlloAndDichCons(
      Long idGruppoControllo, Long idDichiarazioneConsistenza, String orderBy[])
  throws Exception;

  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieForControlliTerreni(Long idAzienda,
      Long idControllo, Vector<String> vTipoErrori, boolean flagOK, String ordinamento)
      throws Exception;
  
  public String getLastAnnoCampagnaFromDichCons(long idAzienda)
      throws Exception;

  public TipoMotivoDichiarazioneVO findTipoMotivoDichiarazioneByPrimaryKey(
      Long idMotivoDichiarazione) throws Exception;

  public String getLastDichConsNoCorrettiva(long idAzienda)
    throws Exception;
  
  public ConsistenzaVO getUltimaDichConsNoCorrettiva(long idAzienda) throws Exception;
  
  public Date getLastDateDichConsNoCorrettiva(long idAzienda) throws Exception;
  
  public Long getLastIdDichConsProtocollata(long idAzienda) throws Exception;
  
  public void updateDichiarazioneConsistenzaRichiestaStampa(Long idDichiarazioneConsistenza)
      throws Exception;
  
  public ConsistenzaVO getUltimaDichiarazioneConsistenza(Long idAzienda)
      throws Exception;

  /* Consistenza */

  public boolean controllaRegistrazioneMandato(AnagAziendaVO aziendaVO,
      String codiceEnte, DelegaAnagrafeVO delegaAnagrafeVO)
      throws Exception, SolmrException;
  
  public boolean controllaRevocaMandato(AnagAziendaVO aziendaVO,
      RuoloUtenza ruoloUtenza, DelegaAnagrafeVO delegaAnagrafeVO)
      throws Exception, SolmrException;

  public boolean controllaPresenzaDelega(AnagAziendaVO aziendaVO)
      throws Exception;

  /* Gestione Utiliti */

  public Vector<AnagAziendaVO> getAziendaByIntermediarioAndCuaa(
      Long intermediario, String cuaa) throws NotFoundException,
      Exception, SolmrException;

  public Vector<AnagAziendaVO> getAziendeByListOfId(Vector<Long> vIdAzienda)
      throws NotFoundException, Exception, SolmrException;

  public void storicizzaDelegaBlocco(RuoloUtenza ruoloUtenza,
      Vector<AnagAziendaVO> vAnagAziendaVO, String oldIntermediario,
      String newIntermediario) throws Exception, SolmrException;

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
      throws Exception, SolmrException;

  /** *********************************************************************** */
  /** ****************************** AAEP *********************************** */
  /** *********************************************************************** */

  /** *************** */
  /** *************** */
  /** *** Stampe **** */
  /** *************** */

  public Vector<ParticellaVO> getElencoParticelleQuadroI1(Long idAzienda,
      Long codFotografia) throws SolmrException, Exception;

  public BigDecimal[] getTotSupQuadroI1CondottaAndAgronomica(Long idAzienda, Long codFotografia)
      throws SolmrException, Exception;
  
  public BigDecimal[] getTotSupQuadroI1CatastaleAndGrafica(Long idAzienda, Long codFotografia)
      throws SolmrException, Exception;

  public Long getCodFotTerreniQuadroI1(Long idDichiarazioneConsistenza)
      throws SolmrException, Exception;

  public Vector<UteVO> getUteQuadroB(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException, Exception;

  public Vector<TipoFormaConduzioneVO> getFormeConduzioneQuadroD()
      throws SolmrException, Exception;

  public Long getFormaConduzioneQuadroD(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException, Exception;

  public Vector<CodeDescription> getAttivitaComplementariQuadroE(
      Long idAzienda, java.util.Date dataRiferimento) throws SolmrException,
      Exception;

  public Long getIdManodoperaQuadroF(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException, Exception;

  public Vector<Long> getAllevamentiQuadroG(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException, Exception;

  public Vector<ParticellaVO> getFabbricatiParticelle(Long idFabbricato,
      java.util.Date dataRiferimento) throws SolmrException, Exception;
  
  public Vector<FabbricatoVO> getFabbricati(
      Long idAzienda, java.util.Date dataRiferimento,boolean comunicazione10R) 
      throws SolmrException, Exception;

  public DelegaVO getIntermediarioPerDelega(Long idIntermediario)
      throws Exception, SolmrException;

  public IntermediarioAnagVO getIntermediarioAnagByIdIntermediario(
      long idIntermediario) throws Exception;
  
  public IntermediarioAnagVO findIntermediarioVOByCodiceEnte(String codEnte)
      throws Exception;
  
  public IntermediarioAnagVO findIntermediarioVOByIdAzienda(long idAzienda)
      throws Exception;
  
  public boolean isAziendaIntermediario(long idAzienda)
      throws Exception;
  
  public Vector<IntermediarioAnagVO> findFigliIntermediarioVOById(long idIntemediario)
      throws Exception;
  
  public Vector<IntermediarioAnagVO> findFigliIntermediarioAziendaVOById(long idIntemediario)
      throws Exception;

  public Vector<BaseCodeDescription> getTerreniQuadroI4(Long idAzienda,
      Long codFotografia) throws SolmrException, Exception;

  public Vector<BaseCodeDescription> getTerreniQuadroI5(Long idAzienda,
      java.util.Date dataRiferimento, Long codFotografia)
      throws SolmrException, Exception;

  public Vector<ConsistenzaZootecnicaStampa> getAllevamentiQuadroC10R(
      Long idAzienda, java.util.Date dataRiferimento) throws SolmrException,
      Exception;

  public Vector<QuadroDTerreni> getTerreniQuadroD10R(Long idAzienda)
      throws SolmrException, Exception;

  public Vector<QuadroDTerreni> getTerreniQuadroD10R(
      java.util.Date dataRiferimento, Long codFotografia)
      throws SolmrException, Exception;
  
  public Vector<String[]> getAnomalie(Long idAzienda, Long idDichiarazioneConsistenza)
      throws SolmrException, Exception;
  
  public Vector<DocumentoVO> getDocumentiStampa(Long idAzienda, Long idDichiarazioneConsistenza,
      String cuaa, java.util.Date dataInserimentoDichiarazione)
    throws SolmrException, Exception;

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
      throws Exception, SolmrException;

  public void storicizzaDelega(DelegaVO dVO, RuoloUtenza ruoloUtenza,
      DocumentoVO documentoVO, AnagAziendaVO anagAziendaVO)
      throws Exception;
  
  public int storicizzaDelegaTemporanea(DelegaVO delegaVO, RuoloUtenza ruoloUtenza, 
      AnagAziendaVO anagAziendaVO) throws Exception;

  
  // ** INIZIO SERVIZI INFOCAMERE
  public Azienda cercaPerCodiceFiscale(String codiceFiscale)throws SolmrException, Exception;
  
  public it.csi.solmr.ws.infoc.Sede cercaPuntualeSede(String codiceFiscale, String progrSede, String codFonte) throws SolmrException, Exception;
  
  public it.csi.solmr.ws.infoc.PersonaRIInfoc cercaPuntualePersona(String codiceFiscale, String progrPersona, String codFonte) throws SolmrException, Exception;
	  
  public List<it.csi.solmr.ws.infoc.ListaPersonaCaricaInfoc> cercaPerFiltriCodFiscFonte(String codiceFiscale, String codFonte) throws SolmrException, Exception;
  
  public ParametroRitornoVO serviceGetAziendeInfocAnagrafe(String codiceFiscale,
	      boolean controllaPresenzaSuAAEP, Boolean bloccaAssenzaAAEP,
	      Boolean controllaLegameRLsuAnagrafe,
	      boolean controllaPresenzaValidazione, boolean aziendeAttive)
	      throws SolmrException, ServiceSystemException, SystemException,
	      Exception;
  
  
  public ParametroRitornoVO serviceGetAziendeInfocAnagrafe(String codiceFiscale,
	      boolean controllaPresenzaSuAAEP, Boolean bloccaAssenzaAAEP,
	      boolean controllaLegameRLsuAnagrafe, boolean controllaPresenzaValidazione)
	      throws SolmrException, ServiceSystemException, SystemException,
	      Exception;
  
  
  public Boolean aggiornaDatiAAEP(String CUAA) throws SystemException, SolmrException, Exception;
  
  // ** FINE SERVIZI INFOCAMERE
  
  
  


  public String isFlagPrevalente(Long[] idAteco) throws Exception;
  
  public TipoSezioniAaepVO getTipoSezioneAaepByCodiceSez(String codiceSezione)
      throws Exception;


  public void deleteDichiarazioneConsistenza(Long idDichiarazioneConsistenza)
      throws Exception;

  public void updateInsediamentoGiovani(Long idAzienda) throws Exception;

  public Vector<StringcodeDescription> getListaComuniTerreniByIdAzienda(Long idAzienda)
      throws Exception, SolmrException;

  public String getTotaleSupCondotteAttiveByIdParticella(Long idParticella,
      Long idAzienda) throws Exception, SolmrException;

  public String[] cessazioneAziendaPLQSL(Long idAzienda)
      throws Exception, SolmrException;

  public boolean controllaObbligoFascicolo(AnagAziendaVO aziendaVO)
      throws Exception;

  public Vector<CodeDescription> getElencoUfficiZonaIntermediarioByIdIntermediario(
      UtenteAbilitazioni utenteAbilitazioni) throws Exception;
  
  public Vector<CodeDescription> getElencoUfficiZonaIntermediarioAttiviByIdIntermediario(
      UtenteAbilitazioni utenteAbilitazioni) throws Exception;

  public UfficioZonaIntermediarioVO findUfficioZonaIntermediarioVOByPrimaryKey(
      Long idUfficioZonaIntermediario) throws Exception;

  public ElencoRegistroDiStallaVO elencoRegistriStalla(String codiceAzienda,
      String codiceSpecie, String cuaa, String tipo, String ordinamento, SianUtenteVO sianUtenteVO)
      throws SolmrException, Exception;
  
  public ElencoRegistroDiStallaVO elencoRegistriStallaNoProfile(String codiceAzienda,
      String codiceSpecie, String cuaa, String tipo, String ordinamento)
      throws SolmrException, Exception;

  public SianTerritorioVO[] leggiTerritorio(String cuaa,
      StringBuffer annoCampagna) throws SolmrException, Exception;

  public Vector<SianTitoliAggregatiVO> titoliProduttoreAggregati(String cuaa,
      String campagna, SianUtenteVO sianUtenteVO) throws SolmrException, Exception;

  public SianTitoloRispostaVO titoliProduttoreConInfoPegni(String cuaa, SianUtenteVO sianUtenteVO)
      throws SolmrException, Exception;

  public Vector<SianQuoteLatteAziendaVO> quoteLatte(String cuaa, String campagna, SianUtenteVO sianUtenteVO)
      throws SolmrException, Exception;

  public SianTitoloRispostaVO titoliProduttore(String CUAA, String campagna, SianUtenteVO sianUtenteVO)
      throws SolmrException, Exception;

  // Fine gestione metodi di collegamento al WEB-SERVICE SIAN

  /**
   * I metodi seguenti sono dei servizi erogati da comune
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
  //public Vector<UtenteProcedimento> serviceGetUtenteProcedimento(
    //  String codiceFiscale, Long idProcedimento, String ruolo,
    //  String codiceEnte, String dirittoAccesso, Long idLivello)
    //  throws SystemException, SolmrException, Exception;

  public AmmCompetenzaVO[] serviceGetListAmmCompetenzaByComuneCollegato(
      String istatComune, String tipoAmministrazione) throws SystemException,
      SolmrException, Exception;

  public AmmCompetenzaVO serviceFindAmmCompetenzaByCodiceAmm(String codiceAmm)
      throws SolmrException, Exception;

  public IntermediarioVO serviceFindIntermediarioByCodiceFiscale(
      String codiceFiscale) throws Exception, SolmrException;

  public IntermediarioVO serviceFindIntermediarioByIdIntermediario(
      Long idIntermediario) throws Exception, SolmrException;

  public AmmCompetenzaVO[] serviceGetListAmmCompetenza()
      throws Exception, SolmrException;

  //public boolean serviceVerificaGerarchia(Long idUtenteConnesso,
      //Long idUtentePratica) throws Exception, SolmrException;

  public AmmCompetenzaVO[] serviceFindAmmCompetenzaByIdRange(
      String idAmmCompetenza[]) throws Exception, SolmrException;
  
  public TecnicoAmministrazioneVO[] serviceGetTecnicoAmmByIdAmmCompAndIdProcedimento(Long idAmmCompetenza,
      Long idProcedimento)
    throws SolmrException, Exception;
  
  public long[] smrcommSearchListIdEntiPrivatiByCodiceEntePrivatoRange(String[] arrCodiceEntePrivato,
      boolean flagCessazione) throws SolmrException, Exception;
  
  public DatiEntePrivatoVO[] smrcommGetEntiPrivatiByIdEntePrivatoRange(long[] idEntePrivato,
      int tipoRisultato, EntePrivatoFiltroVO filtro) throws SolmrException, Exception;

  /**
   * FINE metodi erogati da comune
   * 
   * @param idControllo
   *          Long
   * @throws Exception
   * @return Vector
   */
  public Vector<CodeDescription> getDocumentiByIdControllo(Long idControllo)
      throws Exception;

  // Metodo per effettuare l'inserimento di una dichiarazione di correzione
  public void deleteInsertDichiarazioneCorrezione(
      String elencoIdDichiarazioneSegnalazione[], Vector<ErrAnomaliaDicConsistenzaVO> corrErr, long idUtente)
      throws Exception;

  // Metodo per effettuare la cancellazione di una dichiarazione di correzione
  public void deleteDichiarazioneCorrezione(
      String elencoIdDichiarazioneSegnalazione[]) throws Exception;

  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichiarazioneConsistenza(
      Long elencoIdDichiarazioneSegnalazione[], long idMotivoDichiarazione)
      throws Exception;

  public boolean isRuolosuAnagrafe(String codiceFiscale, long idAzienda)
      throws Exception;

  public PersonaFisicaVO getRuolosuAnagrafe(String codiceFiscale,
      long idAzienda, String codRuoloAAEP) throws Exception;

  public boolean getRuolosuAnagrafe(String codiceFiscale, long idAzienda)
      throws Exception;

  public String getIstatByDescComune(String descComune) throws Exception;

  public void importaSoggCollAAEP(AnagAziendaVO anagAziendaVO,
		  Azienda impresaInfoc, HashMap<?,?> listaPersone, String idParametri[],
      Long idUtenteAggiornamento) throws Exception, SolmrException;

  public Vector<DocumentoVO> getDocumenti(String idDocumenti[])
      throws Exception;

  public CodeDescription getAttivitaATECObyCode(String codiceAteco)
      throws Exception;

  public CodeDescription getAttivitaATECObyCodeParametroCATE(String codiceAteco)
      throws Exception;

  public void importaUteAAEP(AnagAziendaVO anagAziendaVO,
      Sede[] sedeInfocamere, String idParametri[], Long idUtenteAggiornamento)
      throws Exception;

  public CodeDescription[] getElencoAtecoNew(String codiceAteco, Long idAzienda)
      throws Exception;

  public Boolean isUtenteConRuoloSuProcedimento(String codiceFiscale,
      Long idProcedimento) throws SystemException, InvalidParameterException,
      UnrecoverableException, SolmrException, Exception;

  public it.csi.solmr.dto.profile.TipoProcedimentoVO serviceFindTipoProcedimentoByDescrizioneProcedimento(
      String descrizione) throws SystemException, InvalidParameterException,
      UnrecoverableException, SolmrException, Exception;

  public Boolean isUtenteAbilitatoProcedimento(UtenteIride2VO utenteIride2VO)
      throws SystemException, InvalidParameterException,
      UnrecoverableException, SolmrException, Exception;

  public Long writeAccessLogUser(UtenteIride2VO utenteIride2VO)
      throws SystemException, InvalidParameterException,
      UnrecoverableException, SolmrException, Exception;

  public RuoloUtenza loadRoleUser(UtenteIride2VO utenteIride2VO)
      throws SystemException, InvalidParameterException,
      UnrecoverableException, SolmrException, Exception;


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
      UnrecoverableException;
  
  public boolean getDelegaBySocio(String codFiscIntermediario, Long idAziendaAssociata) 
    throws Exception;
  
  public AziendaCollegataVO findAziendaCollegataByFatherAndSon(Long idAziendaFather, Long idAziendaSon,
      Date dataSituazione) throws Exception;
  
  public boolean isSoggettoAssociatoByFatherAndSon(Long idAziendaFather, String cuaaSon,
      Date dataSituazione) throws Exception;

  // INIZIO REPORTISTICA
  public Vector<DelegaVO> getMandatiByUtente(UtenteAbilitazioni utenteAbilitazioni, boolean forZona,
      java.util.Date dataDal, java.util.Date dataAl) throws SolmrException,
      Exception;

  public Vector<CodeDescription> getElencoCAAByUtente(UtenteAbilitazioni utenteAbilitazioni)
      throws Exception, SolmrException;

  public Vector<DelegaVO> getMandatiValidatiByUtente(UtenteAbilitazioni utenteAbilitazioni,
      boolean forZona, java.util.Date dataDal, java.util.Date dataAl)
      throws Exception, SolmrException;

  // FINE REPORTISTICA
  public IntermediarioVO getIntermediarioVOByIdUfficioZonaIntermediario(
      Long idUfficioZonaIntermediario) throws Exception;

  //public IntermediarioVO getIntermediarioVOByCodiceFiscale(String codice_fiscale)
      //throws Exception;

  public IntermediarioVO findIntermediarioVOByPrimaryKey(Long idIntermediario)
      throws Exception;

  public CodeDescription findRegioneByIstatProvincia(String istatProvincia)
      throws Exception;

  public CodeDescription findRegioneByCodiceFiscaleIntermediario(
      String codiceFiscaleIntermediario) throws Exception;

  public CodeDescription[] getListTipoGruppoControlloByIdDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza, String[] orderBy) throws Exception;

  public AnagAziendaVO[] serviceGetListAziendeByIdRange(Vector<Long> collIdAziende)
      throws Exception, SolmrException;

  public SianTitoloMovimentazioneRispostaVO serviceSianMovimentazioneTitoli(
      String cuaa, String idDocumento, String campagna, String fattispecie,
      String cedente, SianTitoliMovimentatiVO[] elencoTitoli)
      throws SystemException, InvalidParameterException, Exception,
      UnrecoverableException;

  public SianEsitiRicevutiRispostaVO serviceSianEsitiRicevuti(String ultimoEsito)
      throws SystemException, InvalidParameterException, Exception,
      UnrecoverableException;

  public SianEsitoDomandeRispostaVO serviceSianEsitoDomande()
      throws SystemException, InvalidParameterException, Exception,
      UnrecoverableException;

  public SianTerritorioVO[] verificaCensimentoFoglio(
      SianTerritorioVO[] elencoSian) throws Exception;

  /*public SianAnagDettaglioVO anagraficaDettaglio(String CUAA,
      boolean orderSocRapp, boolean storicoRappLeg, SianUtenteVO sianUtenteVO) throws SolmrException,
      Exception;*/
  
  public SianAnagTributariaGaaVO ricercaAnagrafica(String cuaa, SianUtenteVO sianUtenteVO) 
      throws SolmrException, Exception;

  public SianFascicoloResponseVO trovaFascicolo(String cuaa)
      throws SolmrException, Exception;

  public RespAnagFascicoloVO getRespAnagFascicolo(
      Long idAzienda) throws SolmrException, Exception;

  // NUOVO TERRITORIALE
  public Vector<ComuneVO> getListComuniParticelleByIdAzienda(Long idAzienda,
      boolean onlyActive, String[] orderBy) throws Exception;
  
  public Vector<ComuneVO> getListComuniParticelleByIdDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza, String[] orderBy)
      throws Exception;

  //public Vector<TipoUtilizzoVO> getListTipiUsoSuoloByIdAzienda(Long idAzienda,
      //boolean onlyActive, String[] orderBy) throws Exception;
  
  //public Vector<TipoUtilizzoVO> getListTipiDestinazProdPrimSecByIdAzienda(Long idAzienda)
      //throws Exception;
  
  
  public Vector<TipoUtilizzoVO> getListUtilizziElencoTerrPianoLavorazione(Long idAzienda)
      throws Exception;
    
  public Vector<TipoUtilizzoVO> getListUtilizziElencoTerrPianoLavorazioneStor(Long idAzienda) 
    throws Exception;
    
  public Vector<TipoUtilizzoVO> getListUtilizziElencoTerrValidazione(Long idDichiarazioneConsistenza) 
    throws Exception;
  
  public Vector<TipoUtilizzoVO> getListTipiUsoSuoloByIdDichCons(long idDichiarazioneConsistenza, String[] orderBy)
      throws Exception;

  public java.lang.String getMaxDataEsecuzioneControlliConduzioneParticella(
      Long idAzienda) throws Exception;

  public java.lang.String getMaxDataEsecuzioneControlliConduzioneDichiarata(
      Long idAzienda) throws Exception;

  public Vector<StoricoParticellaVO> searchListParticelleByParameters(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda)
      throws Exception;

  public StoricoParticellaVO findStoricoParticellaVOByIdConduzioneParticella(
      Long idConduzioneParticella) throws Exception;
  
  
  public Long findIdUteByIdCondPartIdAz(Long idConduzioneParticella, Long idAzienda) throws Exception;

  public long getIdConduzioneDichiarata(long idConduzioneParticella,
      long idDichiarazioneConsistenza) throws Exception;

  public StoricoParticellaVO findStoricoParticellaVOByIdConduzioneDichiarata(
      Long idConduzioneDichiarata) throws Exception;

  public ConduzioneParticellaVO findConduzioneParticellaByPrimaryKey(
      Long idConduzioneParticella) throws Exception;

  public ConduzioneDichiarataVO findConduzioneDichiarataByPrimaryKey(
      Long idConduzioneDichiarata) throws Exception;

  public StoricoParticellaVO getDettaglioParticella(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idConduzione)
      throws Exception;

  public Vector<TipoPiantaConsociataVO> getListPianteConsociate(boolean onlyActive)
      throws Exception;

  public UtilizzoParticellaVO[] getListUtilizzoParticellaVOByIdConduzioneParticella(
      Long idConduzioneParticella, String[] orderBy, boolean onlyActive)
      throws Exception;

  public UtilizzoDichiaratoVO[] getListUtilizzoDichiaratoVOByIdDichiarazioneConsistenzaAndIdConduzioneParticella(
      Long idDichiarazioneConsistenza, Long idConduzioneParticella,
      String[] orderBy) throws Exception;

  public ParticellaCertificataVO findParticellaCertificataByParameters(
      String istatComune, String sezione, String foglio, String particella,
      String subalterno, boolean onlyActive,
      java.util.Date dataDichiarazioneConsistenza) throws Exception;

  public ParticellaCertificataVO findParticellaCertificataByParametersNewElegFit(
      String istatComune, String sezione, String foglio, String particella,
      String subalterno, boolean onlyActive,
      java.util.Date dataDichiarazioneConsistenza) throws Exception;
  
  public ParticellaCertificataVO findParticellaCertificataAllaDichiarazione(
      Long idParticella, ConsistenzaVO consistenzaVO) throws Exception;
  
  public ParticellaCertificataVO findParticellaCertificataByIdParticella(Long idParticella, 
      Date dataDichiarazioneConsistenza) throws Exception;
  
  public Date getDataFotoInterpretazione(long idParticellaCertificata, Date dataRichiestaRiesame)
    throws Exception;
  
  //public Date getDataRichiestaRiesameAlaDich(long idStoricoParticella, long idDichiarazioneConsistenza)
    //throws Exception;
  
  public Vector<ParticellaCertElegVO> getEleggibilitaByIdParticella(long idParticella)
    throws Exception;
  
  public Vector<Vector<ParticellaCertElegVO>> getListStoricoParticellaCertEleg(long idParticella)
    throws Exception;
  
  public HashMap<Long,Vector<SuperficieDescription>> getEleggibilitaTooltipByIdParticella(
      Vector<Long> listIdParticella)
  throws Exception;
  
  public Vector<ProprietaCertificataVO> getListProprietaCertifByIdParticella(long idParticella) 
      throws Exception;
  
  public Vector<ProprietaCertificataVO> getListDettaglioProprietaCertifByIdParticella(
      long idParticella, Date dataInserimentoValidazione) 
      throws Exception;

  public TipoVarietaVO[] getListTipoVarietaByIdUtilizzo(Long idUtilizzo,
      boolean onlyActive) throws Exception;

  public StoricoParticellaVO getDettaglioParticellaByIdConduzioneAndIdUtilizzo(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO,
      Long idConduzione, Long idUtilizzo, Long idAzienda) throws Exception;

  public BigDecimal getSumSuperficieUtilizzoUsoAgronomico(
      long idConduzioneParticella) throws Exception;
  
  public BigDecimal getSumSuperficieUtilizzoUsoAgronomicoParticella(long idParticella, long idAzienda) 
      throws Exception;
  
  public BigDecimal getSumSuperficieAgronomicaAltreconduzioni(long idParticella, long idConduzioneParticella, long idAzienda) 
      throws Exception;
  
  public BigDecimal getSumSuperficieFromParticellaAndLastDichCons(
      long idParticella, Long idAzienda, boolean flagEscludiAzienda)
      throws Exception;

  public String[] getIstatProvFromConduzione(long idAzienda)
      throws Exception;
  
  public BigDecimal getPercentualePosesso(long idAzienda, long idParticella)
    throws Exception;

  public TipoUtilizzoVO[] getListTipiUsoSuoloByIdIndirizzoUtilizzo(
      Long idIndirizzoUtilizzo, boolean onlyActive, String[] orderBy,
      String colturaSecondaria) throws Exception;

  public TipoUtilizzoVO[] getListTipiUsoSuoloByCodice(String codice,
      boolean onlyActive, String[] orderBy, String colturaSecondaria,
      String flagPrincipale) throws Exception;
  
  public TipoVarietaVO[] getListTipoVarietaByIdUtilizzoAndCodice(
      Long idUtilizzo, String codiceVarieta, boolean onlyActive, 
      String[] orderBy) throws Exception;

  public void associaUso(long idAzienda, StoricoParticellaVO[] elencoParticelle,
      RuoloUtenza ruoloUtenza) throws Exception;
  
  public void associaUsoEleggibilitaGis(long idAzienda, StoricoParticellaVO[] elencoParticelle,
      RuoloUtenza ruoloUtenza) throws Exception;

  public void aggiornaIrrigazione(
      StoricoParticellaVO[] elencoStoricoParticella, RuoloUtenza ruoloUtenza,
      boolean flagIrrigabile, Long idIrrigazione) throws Exception;

  public void cambiaTitoloPossesso(Long[] elencoIdConduzioneParticella,
      RuoloUtenza ruoloUtenza, Long idAzienda, Long idTitoloPossesso)
      throws Exception;

  public java.util.Date getMaxDataInizioConduzioneParticella(
      Vector<Long> elencoConduzioni) throws Exception;

  public void cessaParticelle(Long[] elencoIdConduzioneParticella,
      long idUtenteAggiornamento, Long idAzienda, java.util.Date dataCessazione, String provenienza)
      throws Exception;

  public void cambiaUte(Long[] elencoIdConduzioneParticella,
      RuoloUtenza ruoloUtenza, Long idUte, Long idAzienda)
      throws Exception;

  public void associaDocumento(Long[] elencoIdConduzioneParticella,
      Long idDocumento) throws Exception;

  public CodeDescription[] getListTipiTitoloPossesso(
      String orderBy) throws Exception;

  public CodeDescription[] getListTipoCasoParticolare(
      String orderBy) throws Exception;

  public CodeDescription[] getListTipoZonaAltimetrica(
      String orderBy) throws Exception;

  public CodeDescription[] getListTipoAreaA(String orderBy)
      throws Exception;

  public CodeDescription[] getListTipoAreaB(String orderBy)
      throws Exception;

  public CodeDescription[] getListTipoAreaC(String orderBy)
      throws Exception;

  public CodeDescription[] getListTipoAreaD(String orderBy)
      throws Exception;
  
  public CodeDescription[] getListTipoAreaM(String orderBy) 
      throws Exception;
  
  public CodeDescription[] getValidListTipoAreaA(String orderBy)
    throws Exception;
  
  public CodeDescription[] getValidListTipoAreaB(String orderBy)
    throws Exception;
  
  public CodeDescription[] getValidListTipoAreaC(String orderBy)
    throws Exception;
  
  public CodeDescription[] getValidListTipoAreaD(String orderBy)
    throws Exception;

  public CodeDescription[] getListTipoAreaE(String orderBy)
      throws Exception;

  public CodeDescription[] getListTipoAreaF(String orderBy)
      throws Exception;

  public CodeDescription[] getListTipoAreaPSN(String orderBy)
      throws Exception;

  public CodeDescription[] getListTipoFasciaFluviale(
      String orderBy) throws Exception;

  public CodeDescription[] getListTipoAreaG(String orderBy)
      throws Exception;

  public CodeDescription[] getListTipoAreaH(String orderBy)
      throws Exception;
  
  public CodeDescription[] getValidListTipoAreaM(String orderBy)
      throws Exception;

  public CodeDescription[] getListTipoCausaleModParticella(
      String orderBy) throws Exception;

  public TipoImpiantoVO[] getListTipoImpianto(boolean onlyActive, String orderBy)
      throws Exception;

  public CodeDescription[] getListTipoIndirizzoUtilizzo(
      String colturaSecondaria, String orderBy, boolean onlyActive)
      throws Exception;

  public TipoIrrigazioneVO[] getListTipoIrrigazione(String orderBy,
      boolean onlyActive) throws Exception;
  
  public TipoPotenzialitaIrriguaVO[] getListTipoPotenzialitaIrrigua(String orderBy, 
      Date dataRiferiemento) throws Exception;
  
  public TipoTerrazzamentoVO[] getListTipoTerrazzamento(String orderBy,
      Date dataRiferiemento) throws Exception;
  
  public TipoRotazioneColturaleVO[] getListTipoRotazioneColturale(String orderBy,
      Date dataRiferiemento) throws Exception;
  
  //Fine territorialie ******************************
  

  public TipoImpiantoVO findTipoImpiantoByPrimaryKey(Long idImpianto)
      throws Exception;

  public Vector<UtilizzoConsociatoVO> getListUtilizziConsociatiByIdUtilizzoParticella(
      Long idUtilizzoParticella, String[] orderBy) throws Exception;

  public Vector<TipoUtilizzoVO> findListTipiUsoSuoloByIdAzienda(Long idAzienda,
      String colturaSecondaria)  throws Exception;

  public Vector<TipoUtilizzoVO> findListTipiUsoSuoloByIdAziendaCess(Long idAzienda,
      String[] orderBy) throws Exception;

  public void modificaParticelle(StoricoParticellaVO[] elencoStoricoParticella,
      RuoloUtenza ruoloUtenza, AnagAziendaVO anagAziendaVO)
      throws Exception;

  public ComuneVO getComuneByParameters(String descComune,
      String siglaProvincia, String flagEstinto, String flagCatastoAttivo,
      String flagEstero) throws Exception;

  public Vector<ComuneVO> getComuniByParameters(String descComune, String siglaProvincia,
      String flagEstinto, String flagCatastoAttivo, String flagEstero,
      String[] orderBy) throws SolmrException, Exception;
  
  public Vector<ComuneVO> getComuniAttiviByIstatProvincia(String istatProvincia) 
      throws Exception, SolmrException;

  public Vector<AnagParticellaExcelVO> searchParticelleExcelByParameters(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda)
      throws Exception;

  public StoricoParticellaVO[] getListParticelleForDocument(
      StoricoParticellaVO storicoParticellaVO, AnagAziendaVO anagAziendaVO,
      boolean hasUnitToDocument, Long idAnomalie, String orderBy) throws Exception;

  public StoricoParticellaVO[] getListParticelleForDocument(
      StoricoParticellaVO storicoParticellaVO, AnagAziendaVO anagAziendaVO,
      long idDichiarazioneConsistenza, boolean hasUnitToDocument, Long idAnomalie, String orderBy)
      throws Exception;
  
  public StoricoParticellaVO[] getListParticelleForDocumentValoreC( 
      AnagAziendaVO anagAziendaVO, int anno, boolean hasUnitToDocument)
    throws Exception;
  
  public StoricoParticellaVO[] getListParticelleForDocumentExtraSistema( 
      AnagAziendaVO anagAziendaVO, int anno, boolean hasUnitToDocument) 
    throws Exception;
  
  public Vector<StoricoParticellaVO> getListParticelleUvBasic(
      FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO, AnagAziendaVO anagAziendaVO) 
    throws Exception;

  public StoricoParticellaVO findStoricoParticellaVOByParameters(
      String istatComune, String sezione, String foglio, String particella,
      String subalterno, java.util.Date dataInizioValidita)
      throws Exception;

  public Vector<TipoEventoVO> getListTipiEvento()
      throws Exception;

  public SezioneVO getSezioneByParameters(String istatComune, String sezione)
      throws Exception;

  public FoglioVO findFoglioByParameters(String istatComune, String foglio,
      String sezione) throws Exception;

  public FoglioVO[] getFogliByParameters(String istatComune, String sezione,
      String foglio) throws Exception;

  public StoricoParticellaVO[] getListStoricoParticellaVOByParameters(
      String istatComune, String sezione, String foglio, String particella,
      String subalterno, boolean onlyActive, String orderBy, Long idAzienda)
      throws Exception, SolmrException;

  public StoricoParticellaVO[] getListStoricoParticellaVOByParametersImpUnar(
      String istatComune, String sezione, String foglio, String particella,
      String subalterno, long idAzienda) throws Exception, SolmrException;

  public StoricoParticellaVO findStoricoParticellaByPrimaryKey(
      Long idStoricoParticella) throws Exception;

  public StoricoParticellaVO findCurrStoricoParticellaByIdParticella(
      Long idParticella) throws Exception;

  public String inserisciParticella(StoricoParticellaVO storicoParticellaVO,
      RuoloUtenza ruoloUtenza, StoricoParticellaVO[] elencoParticelleEvento,
      Long idEvento, Long idAzienda, String[] arrVIdStoricoUnitaArborea, String[] arrVAreaUV) throws Exception;

  public String inserisciParticella(StoricoParticellaVO storicoParticellaVO,
      RuoloUtenza ruoloUtenza, StoricoParticellaVO[] elencoParticelleEvento,
      Long idEvento) throws Exception;

  public BigDecimal getTotSupCondottaByAziendaAndParticella(
      Long idAzienda, Long idParticella)
      throws Exception;

  public EventoParticellaVO[] getEventiParticellaByIdParticellaNuovaOrCessata(
      Long idParticella, String[] orderBy) throws Exception;

  public StoricoParticellaVO[] getListParticelleForFabbricato(
      Long idFabbricato, Long idPianoRiferimento, String[] orderBy)
      throws Exception;

  public boolean isPianoRiferimentoRipristinato(Long idAzienda)
      throws Exception;

  public StoricoUnitaArboreaVO[] getListStoricoUnitaArboreaByLogicKey(
      Long idParticella, Long idPianoRiferimento, Long idAzienda,
      String[] orderBy) throws Exception;

  public StoricoParticellaVO[] getListStoricoUnitaArboreaByIdAzienda(
      Long idAzienda, Long idPianoRiferimento, String[] orderBy)
      throws Exception;

  public java.lang.String getMaxDataEsecuzioneControlliUnitaArborea(
      Long idAzienda) throws Exception;

  public EsitoControlloUnarVO[] getListEsitoControlloUnarByIdStoricoUnitaArborea(
      Long idStoricoUnitaArborea, String[] orderBy) throws Exception;

  public StoricoParticellaVO findStoricoParticellaArborea(
      Long idStoricoUnitaArborea) throws Exception;
  
  public StoricoParticellaVO findStoricoParticellaArboreaBasic(
      Long idStoricoUnitaArborea) throws Exception;
  
  public StoricoParticellaVO findStoricoParticellaArboreaConduzione(
      Long idStoricoUnitaArborea, long idAzienda) throws Exception;

  public StoricoParticellaVO findStoricoParticellaArboreaTolleranza(
      Long idStoricoUnitaArborea, long idAzienda, String nomeLib) throws Exception;

  public AltroVitignoVO[] getListAltroVitignoByIdStoricoUnitaArborea(
      Long idStoricoUnitaArborea, String[] orderBy) throws Exception;

  public TipoUtilizzoVO[] getListTipiUsoSuoloByTipo(String tipo,
      boolean onlyActive, String[] orderBy) throws Exception;

  public TipoFormaAllevamentoVO[] getListTipoFormaAllevamento(
      boolean onlyActive, String[] orderBy) throws Exception;

  public TipoCausaleModificaVO[] getListTipoCausaleModifica(boolean onlyActive,
      String[] orderBy) throws Exception;
  
  public Vector<TipoCausaleModificaVO> getListTipoCuasaleModificaByIdAzienda(long idAzienda)
    throws Exception;

  public void modificaUnitaArboree(
      StoricoParticellaVO[] elencoParticelleArboree, RuoloUtenza ruoloUtenza, String provenienza)
      throws Exception, SolmrException;

  public void cessaUnitaArboree(Long[] elencoIdStoricoUnitaArboree,
      RuoloUtenza ruoloUtenza, Long idCausaleModifica, String note) throws Exception,
      SolmrException;

  public TipoCessazioneUnarVO[] getListTipoCessazioneUnar(boolean onlyActive,
      String[] orderBy) throws Exception;

  public StoricoParticellaVO[] getListStoricoParticelleArboreeImportabili(
      Long idAzienda, String[] orderBy) throws Exception;

  public void importUnitaArboreeBySchedario(Long[] elencoIdStoricoUnitaArborea,
      RuoloUtenza ruoloUtenza, AnagAziendaVO anagAziendaVO,
      Long idCausaleModifica, Long newIdParticella) throws Exception,
      SolmrException;

  public void inserisciUnitaArborea(
      StoricoUnitaArboreaVO storicoUnitaArboreaVO,
      StoricoParticellaVO storicoParticellaVO, AnagAziendaVO anagAziendaVO,
      RuoloUtenza ruoloUtenza, String provenienza) throws Exception, SolmrException;

  public StoricoParticellaVO[] searchStoricoUnitaArboreaByParametersForStampa(
      Long idAzienda, FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO,
      String[] orderBy) throws Exception;
  
  public StoricoParticellaVO[] searchStoricoUnitaArboreaByParameters(String nomeLib,
      Long idAzienda, FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO,
      String[] orderBy) throws Exception;

  public Vector<StoricoParticellaArboreaExcelVO> searchStoricoUnitaArboreaExcelByParameters(
      String nomeLib, Long idAzienda, FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO) 
      throws Exception;

  public ConduzioneParticellaVO[] getListConduzioneParticellaByIdAzienda(
      Long idAzienda, boolean onlyActive, String[] orderBy)
      throws Exception;

  public void importParticelle(String[] elencoConduzioni, Long idUte,
      AnagAziendaVO anagAziendaVO, AnagAziendaVO anagAziendaSearchVO,
      RuoloUtenza ruoloUtenza, Long idTitoloPossesso) throws Exception;

  public Vector<Object> importParticelleAsservite(String[] elencoConduzioni,
      Long idUte, AnagAziendaVO anagAziendaSearchVO, RuoloUtenza ruoloUtenza,
      Long idTitoloPossesso) throws Exception;

  public Vector<Object> importParticelleAsserviteFromRicercaParticella(
      String[] elencoIdParticelle, Long idUte, AnagAziendaVO anagAziendaVO,
      RuoloUtenza ruoloUtenza, Long idTitoloPossesso) throws Exception;

  public StoricoParticellaVO[] getListStoricoParticelleArboreeByParameters(
      String istatComune, String sezione, String foglio, String particella,
      String subalterno, boolean onlyActive, String[] orderBy)
      throws Exception, SolmrException;

  public StoricoParticellaVO findParticellaArboreaDichiarata(
      Long idUnitaArboreaDichiarata) throws Exception;
  
  public StoricoParticellaVO findParticellaArboreaDichiarataBasic(Long idUnitaArboreaDichiarata)
      throws Exception;

  public AltroVitignoDichiaratoVO[] getListAltroVitignoDichiaratoByIdUnitaArboreaDichiarata(
      Long idUnitaArboreaDichiarata, String[] orderBy) throws Exception;

  public void ribaltaUVOnPianoColturale(Long idAzienda, BigDecimal[] idStoricoUnitaArborea, Long idUtente)
      throws SolmrException, Exception;

  public TipoVinoVO[] getListTipoVino(boolean onlyActive, String[] orderBy)
      throws Exception;

  public TipoTipologiaVinoVO[] getListTipoTipologiaVino(boolean onlyActive,
      String[] orderBy) throws Exception;

  public TipoTipologiaVinoVO[] getListTipoTipologiaVinoForAzienda(long idAzienda)
      throws Exception;
  
  public TipoTipologiaVinoVO[] getListTipoTipologiaVinoForDichCons(long idDichiarazioneConsistenza)
      throws Exception;

  public TipoUtilizzoVO findTipoUtilizzoByPrimaryKey(Long idUtilizzo)
      throws Exception;
  
  public TipoUtilizzoVO[] getListTipoUtilizzoByIdAzienda(String tipo, long idAzienda)
      throws Exception;
  
  public TipoUtilizzoVO[] getListTipoUtilizzoByIdDichiarazioneConsistenza(String tipo, long idDichiarazioneConsistenza)
      throws Exception;

  public TipoVarietaVO findTipoVarietaByPrimaryKey(Long idVarieta)
      throws Exception;
  
  public TipoVarietaVO[] getListTipoVarietaByIdAzienda(long idAzienda)
      throws Exception;
  
  public TipoVarietaVO[] getListTipoVarietaByIdDichiarazioneConsistenza(long idDichiarazioneConsistenza)
      throws Exception;

  public TipoTipologiaVinoVO[] getListTipoTipologiaVinoByIdVino(Long idVino,
      boolean onlyActive, String[] orderBy) throws Exception;

  public Vector<TipoTipologiaVinoVO> getListActiveTipoTipologiaVinoByComune(
      String istatComune) throws Exception;
  
  public Vector<TipoTipologiaVinoVO> getListActiveTipoTipologiaVinoByComuneAndVarieta(
      String istatComune, Long idVarieta, Long idParticella) throws Exception;
  
  //public Vector<VignaVO> getListActiveVignaByIdTipologiaVinoAndParticella(long idTipologiaVino, long idParticella)
      //throws Exception;
  
  public Vector<TipoTipologiaVinoVO> getListTipoTipologiaVinoRicaduta(
      String istatComune, long idVarieta, long idTipologiaVino, java.util.Date dataInserimentoDichiarazione) 
  throws Exception;
  
  public TipoTipologiaVinoVO getTipoTipologiaVinoByPrimaryKey(long idTipologiaVino) 
    throws Exception;

  public void dichiaraUsoAgronomico(StoricoParticellaVO[] elencoParticelle,
      RuoloUtenza ruoloUtenza, Long idAzienda) throws Exception;

  public void validaUVPlSql(Long[] elencoIdUnitaArboreaDichiarata)
      throws SolmrException, Exception;

  public StoricoParticellaVO[] riepilogoTitoloPossesso(Long idAzienda) throws Exception;

  public StoricoParticellaVO[] riepilogoTitoloPossessoDichiarato(
      Long idAzienda, Long idDichiarazioneConsistenza)
      throws Exception;

  public StoricoParticellaVO[] riepilogoPossessoComune(Long idAzienda) throws Exception;

  public StoricoParticellaVO[] riepilogoPossessoComuneDichiarato(
      Long idAzienda, Long idDichiarazioneConsistenza)
      throws Exception;

  public StoricoParticellaVO[] riepilogoComune(Long idAzienda, String escludiAsservimento)
      throws Exception;

  public StoricoParticellaVO[] riepilogoComuneDichiarato(Long idAzienda,
      String escludiAsservimento, Long idDichiarazioneConsistenza) throws Exception;

  public UtilizzoParticellaVO[] riepilogoUsoPrimario(Long idAzienda)
      throws Exception;
  
  public BigDecimal getTotSupSfriguAndAsservimento(Long idAzienda, String escludiAsservimento)
      throws Exception;

  public UtilizzoDichiaratoVO[] riepilogoUsoPrimarioDichiarato(Long idAzienda,
      Long idDichiarazioneConsistenza) throws Exception;

  public UtilizzoParticellaVO[] riepilogoUsoSecondario(Long idAzienda)
      throws Exception;

  public UtilizzoDichiaratoVO[] riepilogoUsoSecondarioDichiarato(
      Long idAzienda, Long idDichiarazioneConsistenza) throws Exception;

  public UtilizzoParticellaVO[] riepilogoMacroUso(Long idAzienda)
      throws Exception;

  public UtilizzoDichiaratoVO[] riepilogoMacroUsoDichiarato(Long idAzienda,
      Long idDichiarazioneConsistenza) throws Exception;
  
  public BigDecimal getTotSupAsservimento(Long idAzienda, Long idDichiarazioneConsistenza)
    throws Exception;

  public TipoMacroUsoVO[] getListTipoMacroUsoByIdAzienda(Long idAzienda,
      boolean onlyActive, String[] orderBy) throws Exception;

  public StoricoParticellaVO[] riepilogoZVNParticellePiemonte(Long idAzienda,
      String escludiAsservimento)
      throws Exception;

  public StoricoParticellaVO[] riepilogoZVNParticelleDichiaratePiemonte(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws Exception;

  public StoricoParticellaVO[] riepilogoZVNFasciaFluviale(Long idAzienda,
      String escludiAsservimento) throws Exception;

  public StoricoParticellaVO[] riepilogoZVNFasciaFluvialeDichiarate(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws Exception;

  public StoricoParticellaVO[] riepilogoZVFParticellePiemonte(Long idAzienda,
      String escludiAsservimento)
      throws Exception;

  public StoricoParticellaVO[] riepilogoZVFParticelleDichiaratePiemonte(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws Exception;

  public StoricoParticellaVO[] riepilogoLocalizzazioneParticellePiemonte(
      Long idAzienda, String escludiAsservimento) throws Exception;

  public StoricoParticellaVO[] riepilogoLocalizzazioneParticelleDichiaratePiemonte(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws Exception;

  public StoricoParticellaVO[] riepilogoFasciaFluvialeParticelle(
      Long idAzienda, String escludiAsservimento) throws Exception;

  public StoricoParticellaVO[] riepilogoFasciaFluvialeParticelleDichiarate(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws Exception;

  public StoricoParticellaVO[] riepilogoAreaG(Long idAzienda,
      String escludiAsservimento) throws Exception;

  public StoricoParticellaVO[] riepilogoAreaGParticelleDichiarate(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws Exception;

  public StoricoParticellaVO[] riepilogoAreaH(Long idAzienda,
      String escludiAsservimento) throws Exception;

  public StoricoParticellaVO[] riepilogoAreaHParticelleDichiarate(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws Exception;

  public StoricoParticellaVO[] riepilogoZonaAltimetrica(Long idAzienda,
      String escludiAsservimento) throws Exception;

  public StoricoParticellaVO[] riepilogoZonaAltimetricaParticelleDichiarate(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws Exception;

  public StoricoParticellaVO[] riepilogoCasoParticolare(Long idAzienda,
      String escludiAsservimento) throws Exception;

  public StoricoParticellaVO[] riepilogoCasoParticolareParticelleDichiarate(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws Exception;

  public Vector<TipoVarietaVO> getListTipoVarietaVitignoByMatriceAndComune(long idUtilizzo, 
      long idTipoDestinazione, long idTipoDettaglioUso, long idTipoQualitaUso, 
      String istatComune) throws Exception;

  public java.lang.String getTotSupCondottaByAzienda(Long idAzienda,
      boolean onlyActive) throws Exception;

  public StoricoParticellaVO[] riepilogoTipoArea(Long idAzienda,
      String escludiAsservimento, String tipoArea) throws Exception;

  public StoricoParticellaVO[] riepilogoTipoAreaDichiarato(Long idAzienda,
      String escludiAsservimento, Long idDichiarazioneConsistenza,
      String tipoArea) throws Exception;

  public UnitaArboreaDichiarataVO[] getListUnitaArboreaDichiarataByPianoRiferimentoAndIdStoricoUnitaArborea(
      Long idStoricoParticella, Long idAzienda, Long idPianoRiferimento,
      String[] orderBy) throws Exception;
  
  public long getIdUnitaArboreaDichiarata(long idStoricoUnitaArborea, long idDichiarazioneConsistenza)
      throws Exception;
  
  public Long getDefaultIdGenereIscrizione() throws Exception;
  
  public Vector<TipoGenereIscrizioneVO> getListTipoGenereIscrizione() throws Exception;
  
  public BigDecimal getSupEleggPlSqlTotale(long idParticellaCertificata, long idCatalogoMatrice)
    throws Exception;

  public BigDecimal getSupEleggNettaPlSqlTotale(long idParticellaCertificata, long idCatalogoMatrice)
    throws Exception;
  
  public Vector<TipoMenzioneGeograficaVO> getListTipoMenzioneGeografica(long idParticella, 
      Long idTipologiaVino, java.util.Date dataInserimentoDichiarazione)
    throws Exception;
  
  public Vector<StoricoParticellaVO> getListParticelleByIdDocumento(Long idDocumento, String[] orderBy)
      throws Exception;
  
  public void cambiaPercentualePossesso(Long[] elencoIdConduzioneParticella, Vector<Long> vIdParticella,
      RuoloUtenza ruoloUtenza, Long idAzienda, BigDecimal percentualePossesso) throws Exception;
  
  public void cambiaPercentualePossessoSupUtilizzata(Vector<Long> vIdConduzioni,
      RuoloUtenza ruoloUtenza, Long idAzienda)  throws Exception;

  public long[] ricercaIdParticelleTerreni(
      FiltriRicercaTerrenoVO filtriRicercaTerrenoVO) throws Exception;

  public long[] ricercaIdConduzioneTerreniImportaAsservimento(
      FiltriRicercaTerrenoImportaAsservimentoVO filtriRicercaTerrenoVO)
      throws Exception;

  public RigaRicercaTerreniVO[] getRigheRicercaTerreniByIdParticellaRange(
      long ids[]) throws Exception;

  public RigaRicercaTerreniImportaAsservimentoVO[] getRigheRicercaTerreniImportaAsservimentoByIdConduzioneRange(
      long ids[]) throws Exception;

  public RigaRicercaTerreniImportaAsservimentoVO[] getRigheRicercaTerreniImportaAsservimentoByIdParticellaRange(
      long ids[]) throws Exception;

  public Vector<String> findProvinciaStoricoParticellaArborea(
      long[] idStoricoUnitaArborea) throws Exception;
  
  public Vector<String> findProvinciaStoricoParticellaArboreaIsolaParcella(long idAzienda, long[] idIsolaParcella)
      throws Exception;
  
  public BigDecimal getSumAreaUVParticella(long idAzienda, long idParticella) 
    throws Exception;
  
  public int getNumUVParticella(long idAzienda, long idParticella)
    throws Exception;

  public Vector<String> findProvinciaParticellaArboreaDichiarata(
      long[] idUnitaArboreaDichiarata) throws Exception;
  
  public StoricoUnitaArboreaVO findStoricoUnitaArborea(Long idStoricoUnitaArborea) throws Exception;

  // FINE NUOVO TERRITORIALE

  // NUOVO FABBRICATO
  public FabbricatoParticellaVO[] getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella(
      Long idConduzioneParticella, Long idAzienda, String[] orderBy,
      boolean onlyActive) throws Exception;

  public FabbricatoVO[] getListFabbricatiAziendaByPianoRifererimento(
      Long idAzienda, Long idPianoRiferimento, Long idUte, String[] orderBy)
      throws Exception;

  // FINE NUOVO FABBRICATO

  // NUOVA GESTIONE UTE
  public Vector<UteVO> getListUteByIdAzienda(Long idAzienda,
      boolean onlyActive, String[] orderBy) throws Exception;

  public Vector<UteVO> getListUteByIdAziendaAndIdPianoRiferimento(
      Long idAzienda, long idPianoRiferimento) throws Exception;

  public UteVO findUteByPrimaryKey(Long idUte) throws Exception;

  public Date getMinDataInizioConduzione(Long idUte) throws Exception;

  public Date getMinDataInizioAllevamento(Long idUte) throws Exception;

  public Date getMinDataInizioFabbricati(Long idUte) throws Exception;

  // FINE NUOVA GESTIONE UTE

  // INIZIO GESTIONE DOCUMENTI
  public Vector<TipoStatoDocumentoVO> getListTipoStatoDocumento(boolean isActive)
      throws Exception, SolmrException;

  public Vector<DocumentoVO> searchDocumentiByParameters(DocumentoVO documentoVO,
      String protocollazione, String[] orderBy) throws Exception;

  public TipoDocumentoVO[] getListTipoDocumentoByIdTipologiaDocumento(
      Long idTipologiaDocumento, boolean isActive) throws Exception,
      SolmrException;

  public TipoDocumentoVO findTipoDocumentoVOByPrimaryKey(Long idDocumento)
      throws Exception;

  public DocumentoVO findDocumentoVOBydDatiAnagrafici(
      DocumentoVO documentoFiltroVO) throws Exception;

  public Long inserisciDocumento(DocumentoVO documentoVO,
      RuoloUtenza ruoloUtenza, String anno, Vector<DocumentoProprietarioVO> elencoProprietari,
      StoricoParticellaVO[] elencoParticelle, Vector<ParticellaAssVO> particelleAssociate)
      throws Exception;

  public DocumentoVO findDocumentoVOByPrimaryKey(Long idDocumento)
      throws Exception;

  public DocumentoVO getDettaglioDocumento(Long idDocumento,
      boolean legamiAttivi) throws Exception, SolmrException;

  public DocumentoVO getDettaglioDocumento(Long idDocumento,
      java.util.Date dataConsistenza, Long idDichiarazioneConsistenza)
      throws Exception, SolmrException;

  public Long aggiornaDocumento(DocumentoVO documentoVO,
      RuoloUtenza ruoloUtenza, String operazioneRichiesta)
      throws Exception;
  
  public Long aggiornaDocumentoIstanzaLimitato(DocumentoVO documentoVO,
      RuoloUtenza ruoloUtenza, String operazioneRichiesta) throws Exception;

  public void deleteDocumenti(String[] documentiDaEliminare, String note,
      long idUtenteAggiornamento) throws Exception;

  public void protocollaDocumenti(String[] documentiDaProtocollare, Long idAzienda,
      RuoloUtenza ruoloUtenza) throws Exception;

  public Vector<DocumentoVO> getListDettaglioDocumentoByIdConduzione(Long idConduzione,
      boolean isStorico, boolean altreParticelle) throws Exception,
      SolmrException;
  
  public Vector<DocumentoVO> getListDettaglioDocumentoByIdConduzionePopUp(Long idConduzioneParticella)
      throws Exception, SolmrException;
  
  public Vector<DocumentoVO> getListDettaglioDocumentoByIdConduzioneDichiarataPopUp(Long idConduzioneDichiarata) 
      throws Exception,  SolmrException;

  public DocumentoVO[] getListDocumentiByIdAzienda(Long idAzienda,
      boolean onlyActive, String[] orderBy, Long idTipologiaDocumento)
      throws Exception;

  public TipoCategoriaDocumentoVO[] getListTipoCategoriaDocumentoByIdTipologiaDocumento(
      Long idTipologiaDocumento, String orderBy[]) throws Exception;

  public TipoDocumentoVO[] getListTipoDocumentoByIdCategoriaDocumento(
      Long idCategoriaDocumento, String orderBy[], boolean onlyActive,
      Boolean cessata) throws Exception;

  public TipoCategoriaDocumentoVO findTipoCategoriaDocumentoByPrimaryKey(
      Long idCategoriaDocumento) throws Exception;

  public TipoDocumentoVO[] getListTipoDocumentoByIdCategoriaDocumentoAndIdAzienda(
      Long idCategoriaDocumento, Long idAzienda, String orderBy[])
      throws Exception;

  public String getDataMaxEsecuzioneDocumento(Long idAzienda)
      throws Exception;

  public String[] getCuaaProprietariDocumentiAzienda(Long idAzienda,
      String cuaa, boolean onlyActive) throws Exception;

  public EsitoControlloDocumentoVO[] getListEsitoControlloDocumentoByIdDocumento(
      Long idDocumento, String[] orderBy) throws Exception;

  public DocumentoVO[] getListDocumentiAziendaByIdControllo(
      AnagAziendaVO anagAziendaVO, Long idControllo, boolean onlyActive,
      String[] orderBy) throws Exception;

  public TipoDocumentoVO[] getListTipoDocumentoByIdControllo(Long idControllo,
      boolean onlyActive, String orderBy[]) throws Exception;

  public DocumentoVO[] getListDocumentiAziendaByIdControlloAndParticella(
      AnagAziendaVO anagAziendaVO, Long idControllo, Long idStoricoParticella,
      boolean onlyActive, String[] orderBy) throws Exception;

  public DocumentoVO[] getListDocumentiPerDichCorrettiveAziendaByIdControlloAndParticella(
      AnagAziendaVO anagAziendaVO, Long idControllo, Long idStoricoParticella,
      String annoCampagna, String[] orderBy) throws Exception;

  public DocumentoVO[] getListDocumentiPerDichCorrettiveAziendaByIdControllo(
      AnagAziendaVO anagAziendaVO, Long idControllo, String annoCampagna,
      String[] orderBy) throws Exception;
  
  public Vector<DocumentoConduzioneVO> getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella(
      Long idDocumento, Long idConduzioneParticella, boolean onlyActive) throws Exception;
  
  public Vector<StoricoParticellaVO> getListParticelleByIdDocumentoAlPianoCorrente(
      Long idDocumento) throws Exception;

  // FINE GESTIONE DOCUMENTI

  // INIZIO ANAGRAFICA
  public TipoCessazioneVO[] getListTipoCessazione(String orderBy,
      boolean onlyActive) throws Exception;

  // FINE ANAGRAFICA

  // INIZIO ATTESTAZIONI
  public TipoAttestazioneVO[] getListTipoAttestazioneOfPianoRiferimento(
      Long idAzienda, String codiceFotografiaTerreni,
      java.util.Date dataAnnoCampagna, java.util.Date dataVariazione, String[] orderBy, 
      String voceMenu) throws Exception;

  public boolean isAttestazioneDichiarata(String codiceFotografiaTerreni)
      throws Exception;
  
  public boolean isAttestazioneAzienda(long idAzienda)
      throws Exception;

  public void aggiornaAttestazioniPlSql(Long idAzienda, Long idUtente,
      Long idDichiarazioneConsistenza, String codAttestazione) throws SolmrException, Exception;

  public void aggiornaAttestazioni(String[] elencoIdAttestazioni,
      AnagAziendaVO anagAziendaVO, RuoloUtenza ruoloUtenza,
      Hashtable<?,?> elencoParametri, String voceMenu, ConsistenzaVO consistenzaVO)
      throws Exception;
  
  public void aggiornaElencoAllegatiAttestazioni(String[] elencoIdAttestazioni, 
      RuoloUtenza ruoloUtenza, Hashtable<Long,ParametriAttDichiarataVO> elencoParametri, 
      TipoAllegatoVO tipoAllegatoVO, ConsistenzaVO consistenzaVO) throws Exception;

  public TipoAttestazioneVO[] getListTipoAttestazioneForUpdate(Long idAzienda,
      String[] orderBy, String voceMenu) throws Exception;

  public TipoParametriAttestazioneVO findTipoParametriAttestazioneByIdAttestazione(
      Long idAttestazione) throws Exception;

  public boolean getDataAttestazioneAllaDichiarazione(
      String codiceFotografiaTerreni,
      java.util.Date dataInserimentoDichiarazione, boolean flagVideo,
      boolean flagStampa, String codiceAttestazione, String voceMenu)
      throws Exception;

  public TipoAttestazioneVO[] getElencoTipoAttestazioneAlPianoAttuale(
      boolean flagVideo, boolean flagStampa, String[] orderBy,
      String codiceAttestazione, String voceMenu) throws Exception;

  public TipoAttestazioneVO[] getTipoAttestazioneAllegatiAllaDichiarazione(
      String codiceFotografiaTerreni, java.util.Date dataAnnoCampagna, 
      java.util.Date dataVariazione, String[] orderBy) throws Exception;
  
  public Vector<TipoAttestazioneVO> getElencoAttestazioniAllaDichiarazione(
      String codiceFotografiaTerreni, Date dataAnnoCampagna, 
      String codiceAttestazione) throws Exception;
  
  public AttestazioneAziendaVO getFirstAttestazioneAzienda(long idAzienda) throws Exception;
  
  public AttestazioneDichiarataVO getFirstAttestazioneDichiarata(long codiceFotografia) throws Exception;
  
  public Vector<java.util.Date> getDateVariazioniAllegati(long codiceFotografia) throws Exception;

  // FINE ATTESTAZIONI

  public IntermediarioVO serviceFindResponsabileByIdIntermediarioAndIdProcToDateRiferimento(
      Long idIntermediario, Long idProcedimento, Date dataRiferimento)
      throws Exception, SolmrException;

  /*****************************************************************************
   * /* I METODI SEGUENTI SERVONO AI REPORT DINAMICI
   */
  /** ********************************************* */
  public HashMap<?,?> getQueryPopolamento(String queryPopolamento,
      String idRptVariabileReport) throws Exception, SolmrException;

  public Htmpl getRisultatoQuery(TipologiaReportVO tipologiaReportVO,
      HashMap<?,?> parametriFissiHtmpl, Htmpl layout) throws Exception,
      SolmrException;

  public HSSFWorkbook getRisultatoQuery(TipologiaReportVO tipologiaReportVO,
      HashMap<?,?> parametriFissiHtmpl, HSSFWorkbook workBook, String nomeFoglio)
      throws Exception, SolmrException;

  public boolean isCUAAAlreadyPresentInsediate(String cuaa)
      throws Exception;

  /**
   * Servizi che accedono ai WS CCIAA START
   */
  public UvResponseCCIAA elencoUnitaVitatePerCUAA(String cuaa, Long idAzienda, RuoloUtenza ruolo)
      throws SolmrException, Exception;

  public UvResponseCCIAA elencoUnitaVitatePerParticella(String istat,
      String sezione, String foglio, String particella, Long idAzienda, RuoloUtenza ruolo) throws SolmrException,
      Exception;

  public void sianAggiornaDatiAlboVigneti(AnagAziendaVO anagAziendaVO)
      throws SolmrException, Exception;

  /**
   * Servizi che accedono ai WS CCIAA STOP
   */
  
  public StringcodeDescription getDescTipoIscrizioneInpsByCodice(String codiceTipoIscrizioneInps)
      throws Exception;

  public Long getAnnoDichiarazione(Long idDichiarazioneConsistenza)
      throws Exception;

  public Long getProcedimento(Long idAzienda, Long idDichiarazioneConsistenza)
      throws Exception;

  public long getLastIdDichiazioneConsistenza(Long idAzienda, Long anno)
      throws Exception;

  public ParticellaDettaglioValidazioniVO[] getParticellaDettaglioValidazioni(
      long idParticella, Long anno, int tipoOrdinamento,
      boolean ordineAscendente[]) throws Exception;

  public long[] getElencoAnniDichiarazioniConsistenzaByIdParticella(
      long idParticella) throws Exception;

  public boolean areParticelleNonCessateByIdParticelle(long idParticelle[])
      throws Exception;

  public StoricoParticellaVO findStoricoParticellaVOByIdParticella(
      long idParticella) throws Exception;

  public int countParticelleConConduzioniAttive(long idParticella[])
      throws SolmrException, Exception;

  public SianAnagTributariaVO selectDatiAziendaTributaria(String cuaa)
      throws Exception;

  public String[] getCUAACollegati(String cuaa, SianUtenteVO sianUtenteVO) throws Exception;

  public Vector<Long> getIdAnagAziendeCollegatebyCUAA(String cuaa)
      throws Exception;

  public ParticellaAssVO[] getParticellaForDocAzCessata(Long idParticella)
      throws Exception;

  public Vector<ParticellaAssVO> getParticelleDocCor(Long idDocumento) throws Exception;

  // **********SITI

  public String serviceParticellaUrl3D(String istatComune, String sezione,
      String foglio, String particella, String subalterno)
      throws InvalidParameterException, Exception, UnrecoverableException;

  // ***********SITI

  /*
   * Servizi di vitiserv Begin
   */
  public DirittoGaaVO[] getDiritti(long idAzienda, boolean flagAttivi,
      int tipoOrdinamento, int tipoRisultato) throws Exception;

  /*
   * Servizi di vitiserv End
   */

  /*****************************************************************************
   * ********* Servizi di SigopServ BEGIN **********************************
   ****************************************************************************/
  
  public SchedaCreditoVO[] sigopservVisualizzaDebiti(String cuaa)
      throws Exception;
  
  public PagamentiErogatiVO sigopservEstraiPagamentiErogati(String cuaa, String settore,
      Integer anno) throws Exception;
   
  public RecuperiPregressiVO sigopservEstraiRecuperiPregressi(String cuaa, String settore,
      Integer anno) throws Exception;

  /*****************************************************************************
   * ********* Servizi di SigopServ END **********************************
   ****************************************************************************/
  
  /***************************** COMMON BEGIN ***************************
   ******************************************************************* 
   ******************************************************************/
  
  public Object getValoreParametroAltriDati(String codiceParametro)
    throws Exception, SolmrException;
  
  /***************************** COMMON END ***************************
   ******************************************************************* 
   ******************************************************************/
  
  
  /***************************** WS COMUNE START ***************************
   ******************************************************************* 
   ******************************************************************/
  
  public String serviceInviaPostaCertificata(InvioPostaCertificata invioPosta) 
      throws Exception, SolmrException;
  
  /***************************** WS COMUNE END ***************************
   ******************************************************************* 
   ******************************************************************/
  
  
  /***************************** WS PAPUA START ***************************
   ******************************************************************* 
   ******************************************************************/
  
  public it.csi.papua.papuaserv.presentation.ws.profilazione.axis.Ruolo[] findRuoliForPersonaInApplicazione(String codiceFiscale, int livelloAutenticazione)
      throws SolmrException, Exception;
  
  public UtenteAbilitazioni loginPapua(String codiceFiscale, String cognome, 
      String nome, int livelloAutenticazione, String codiceRuolo)
      throws SolmrException, Exception;
  
  public MacroCU[] findMacroCUForAttoreInApplication(String codiceAttore)
      throws SolmrException, Exception;
  
  public boolean verificaGerarchia(long idUtente1, long idUtente2)
      throws SolmrException, Exception;
  
  public UtenteAbilitazioni getUtenteAbilitazioniByIdUtenteLogin(long idUtente)
      throws SolmrException, Exception;
  
  public UtenteAbilitazioni[] getUtenteAbilitazioniByIdUtenteLoginRange(long[] idUtente)
      throws SolmrException, Exception;
  
  /***************************** WS PAPUA END ***************************
   ******************************************************************* 
   ******************************************************************/
  

  public String testDB() throws Exception;

}
