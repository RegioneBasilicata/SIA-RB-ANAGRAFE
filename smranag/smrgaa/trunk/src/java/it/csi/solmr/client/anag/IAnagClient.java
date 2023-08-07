package it.csi.solmr.client.anag;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author EF
 * @version 1.0
 */

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
import it.csi.solmr.dto.anag.sian.SianFascicoloResponseVO;
import it.csi.solmr.dto.anag.sian.SianQuoteLatteAziendaVO;
import it.csi.solmr.dto.anag.sian.SianTerritorioVO;
import it.csi.solmr.dto.anag.sian.SianTitoliAggregatiVO;
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
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.exception.services.InvalidParameterException;
import it.csi.solmr.exception.services.ServiceSystemException;
import it.csi.solmr.ws.infoc.Azienda;
import it.csi.solmr.ws.infoc.Sede;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public interface IAnagClient
{
  public AnagAziendaVO getAziendaById(Long idAnagAzienda) throws SolmrException;

  public Vector<AnagAziendaVO> getAssociazioniCollegateByIdAzienda(
      Long idAzienda, Date dataFineValidita) throws SolmrException;

  public CodeDescription[] getCodeDescriptionsNew(String tableName,
      String filtro, String valFiltro, String orderBy) throws SolmrException;

  public it.csi.solmr.dto.profile.CodeDescription getGruppoRuolo(String ruolo)
      throws SolmrException;

  public AnagAziendaVO getAziendaByIdAzienda(Long idAzienda)
      throws SolmrException;

  public Vector<AnagAziendaVO> getAziendaCUAA(String CUAA)
      throws SolmrException;

  public Vector<AnagAziendaVO> getAziendaByCriterioCessataAndProvvisoria(
      String CUAA) throws SolmrException;

  public AnagAziendaVO getAziendaCUAA(String CUAA, Date dataSituazioneAl)
      throws SolmrException;

  public AnagAziendaVO getAziendaPartitaIVA(String partitaIVA,
      Date dataSituazioneAl) throws SolmrException;

  public Vector<Long> getListIdAziende(AnagAziendaVO aaVO,
      Date dataSituazioneAl, boolean attivitaBool) throws SolmrException;

  public Vector<Long> getListIdAziendeFlagProvvisorio(AnagAziendaVO aaVO,
      Date dataSituazioneAl, boolean attivitaBool, boolean provvisorio)
      throws SolmrException;

  public Vector<Long> getListOfIdAzienda(AnagAziendaVO aaVO,
      Date dataSituazioneAl, boolean attivitaBool) throws SolmrException;

  public Vector<AnagAziendaVO> getListAziendeByIdRange(
      Vector<Long> idAnagAzienda) throws SolmrException;

  public Vector<AnagAziendaVO> getAziendeByIdAziendaRange(
      Vector<Long> idAnagAzienda) throws SolmrException;

  public Vector<AnagAziendaVO> getListAziendeByIdRangeFromIdAzienda(
      Vector<Long> idAzienda) throws SolmrException;

  public void storicizzaAziendeCollegateBlocco(RuoloUtenza ruoloUtenza,
      Vector<AziendaCollegataVO> vAnagAziendaCollegateVO) throws SolmrException;

  public void eliminaAziendeCollegateBlocco(long idUtenteAggiornamento,
      Long idAziendaFather, Vector<Long> vIdAziendaVO) throws SolmrException;

  public PersonaFisicaVO getTitolareORappresentanteLegaleAzienda(
      Long idAzienda, Date dataSituazioneAl) throws SolmrException;

  public Vector<UteVO> getUTE(Long idAzienda, Boolean storico)
      throws SolmrException;

  public UteVO getUteById(Long idUte) throws SolmrException;

  public Date getMinDataInizioConduzione(Long idUte) throws Exception;

  public Date getMinDataInizioAllevamento(Long idUte) throws Exception;

  public Date getMinDataInizioFabbricati(Long idUte) throws Exception;

  public Vector<CodeDescription> getTipiAttivita() throws SolmrException;

  public Vector<CodeDescription> getTipiAzienda() throws SolmrException;

  public Vector<CodeDescription> getTipiFabbricato() throws SolmrException;

  public Vector<CodeDescription> getTipiFormaGiuridica(Long idTipologiaAzienda)
      throws SolmrException;
  
  public Vector<CodeDescription> getCodeDescriptionsFormaGiuridica()
      throws SolmrException;

  public Vector<CodeDescription> getTipiMotivoDichiarazione()
      throws SolmrException;

  public TipoTipologiaAziendaVO getTipologiaAzienda(Long idTipologiaAzienda)
      throws SolmrException;

  public Vector<TipoTipologiaAziendaVO> getTipiTipologiaAzienda(
      Boolean flagControlliUnivocita, Boolean flagAziendaProvvisoria)
      throws SolmrException;

  public Vector<CodeDescription> getTipiFormaGiuridica() throws SolmrException;

  public Vector<CodeDescription> getTipiIntermediario() throws SolmrException;

  public Vector<CodeDescription> getTipiIntermediarioUmaProv()
      throws SolmrException;

  public Vector<CodeDescription> getTipiUtilizzo() throws SolmrException;

  public Vector<ProvinciaVO> getProvinceByRegione(String idRegione)
      throws SolmrException;
  
  public Vector<ProvinciaVO> getProvince()
      throws SolmrException;

  public Vector<ComuneVO> getComuniLikeByRegione(String idRegione, String like)
      throws SolmrException;

  public Vector<ComuneVO> getComuniLikeProvAndCom(String provincia,
      String comune) throws SolmrException;

  public Vector<ComuneVO> getComuniByDescCom(String comune)
      throws SolmrException;

  public Vector<ComuneVO> getComuniNonEstintiLikeProvAndCom(String provincia,
      String comune, String flagEstero) throws SolmrException;

  public Vector<CodeDescription> getTipiAttivitaOTE(String codice,
      String descrizione) throws SolmrException;

  public Vector<CodeDescription> getTipiAttivitaATECO(String codice,
      String descrizione) throws SolmrException;

  public String getProcedimento(Integer code) throws SolmrException;

  public String getTipoAttivita(Integer code) throws SolmrException;

  public String getTipoAzienda(Integer code) throws SolmrException;

  public String getTipoCasoParticolare(Integer code) throws SolmrException;

  public String getTipoFabbricato(Integer code) throws SolmrException;

  public String getTipoFormaGiuridica(Integer code) throws SolmrException;

  public String getTipoIntermediario(Integer code) throws SolmrException;

  public String getTipoUtilizzo(Integer code) throws SolmrException;

  public String getTipoZonaAltimetrica(Integer code) throws SolmrException;

  public Vector<CodeDescription> getTipiTipologiaDocumento()
      throws SolmrException;

  public Vector<CodeDescription> getTipiTipologiaDocumento(boolean cessata)
      throws SolmrException;

  public Vector<ComuneVO> ricercaStatoEstero(String statoEstero,
      String estinto, String flagCatastoAttivo) throws SolmrException;

  public void updateSedeLegale(AnagAziendaVO anagAziendaVO,
      long idUtenteAggiornamento) throws SolmrException;

  public void checkDataCessazione(Long anagAziendaPK, String dataCessazione)
      throws SolmrException;

  public void cessaAzienda(AnagAziendaVO anagVO, Date dataCess, String causale,
      long idUtenteAggiornamento) throws SolmrException;

  public void storicizzaSedeLegale(AnagAziendaVO anagAziendaVO) throws SolmrException;

  public String ricercaCodiceComune(String descrizioneComune,
      String siglaProvincia) throws SolmrException;

  public void deleteUTE(Long idUTE) throws SolmrException;

  public void insertUte(UteVO uVO, long idUtenteAggiornamento)
      throws SolmrException;

  public AnagAziendaVO findAziendaAttiva(Long idAzienda) throws SolmrException;

  public void countUteByAziendaAndComune(Long idAzienda, String comune)
      throws SolmrException;

  public PersonaFisicaVO getPersonaFisica(String cuaa) throws SolmrException;

  public void cambiaRappresentanteLegale(Long aziendaPK,
      PersonaFisicaVO personaVO, long idUtenteAggiornamento)
      throws SolmrException;

  public ComuneVO getComuneByCUAA(String cuaa) throws SolmrException;

  public ComuneVO getComuneByISTAT(String istat) throws SolmrException;

  public String getFormaGiuridicaFlagCCIAA(Long idFormaGiuridica)
      throws SolmrException;

  public void checkCUAAandCodFiscale(String cuaa, String partitaIVA)
      throws SolmrException;

  public AnagAziendaVO getAltraAziendaFromPartitaIVA(String partitaIVA,
      Long idAzienda) throws SolmrException;

  public AnagAziendaVO getAziendaCUAAandCodFiscale(String cuaa,
      String partitaIVA) throws SolmrException;

  public void checkPartitaIVA(String partitaIVA, Long idAzienda)
      throws SolmrException;

  public void checkCUAA(String cuaa) throws SolmrException;

  public boolean isCUAAAlreadyPresentInsediate(String cuaa)
      throws SolmrException;

  public void checkIsCUAAPresent(String cuaa, Long idAzienda)
      throws SolmrException;

  public String getComuneFromCF(String codiceFiscale) throws SolmrException;

  public Long ricercaIdAttivitaOTE(String codice, String descrizione)
      throws SolmrException;

  public Long ricercaIdAttivitaOTE(String codice, String descrizione,
      boolean forPopup) throws SolmrException;

  public Long ricercaIdAttivitaATECO(String codice, String descrizione)
      throws SolmrException;

  public Long ricercaIdAttivitaATECO(String codice, String descrizione,
      boolean forPopup) throws SolmrException;

  public void updateUTE(UteVO uteVO) throws SolmrException;

  public String ricercaCodiceFiscaleComune(String descrizioneComune,
      String siglaProvincia) throws SolmrException;

  public Long insertAzienda(AnagAziendaVO aaVO, PersonaFisicaVO pfVO,
      UteVO ute, long idUtenteAggiornamento) throws SolmrException;

  public String getDescriptionFromCode(String tableName, Integer code)
      throws SolmrException;

  public void updateAzienda(AnagAziendaVO anagVO)
      throws SolmrException;

  public void updateRappLegale(PersonaFisicaVO pfVO, long idUtenteAggiornamento)
      throws SolmrException;

  public void storicizzaAzienda(AnagAziendaVO anagVO, long idUtenteAggiornamento)
      throws SolmrException;

  public Vector<CodeDescription> getTipiFormaGiuridicaNonIndividuale()
      throws SolmrException;

  public Vector<CodeDescription> getTipiRuoloNonTitolare()
      throws SolmrException;

  public void utenteConDelega(UtenteAbilitazioni utenteAbilitazioni, Long idAzienda)
      throws SolmrException;

  public Vector<PersonaFisicaVO> getSoggetti(Long idAzienda, Date data)
      throws SolmrException;

  public Vector<PersonaFisicaVO> getSoggetti(Long idAzienda, Boolean storico)
      throws SolmrException;

  public void checkForDeleteSoggetto(Long idContitolare) throws SolmrException;

  public void deleteContitolare(Long idContitolare) throws SolmrException;

  public PersonaFisicaVO getDettaglioSoggetti(Long idSoggetto, Long idAzienda)
      throws SolmrException;

  public void inserisciSoggetto(PersonaFisicaVO pfVO, long idUtenteAggiornamento)
      throws SolmrException;

  public void updateSoggetto(PersonaFisicaVO pfVO, long idUtenteAggiornamento)
      throws SolmrException;

  public String getFlagPartitaIva(Long idTipoFormaGiuridica)
      throws SolmrException;
  
  public String getObbligoGfFromFormaGiuridica(Long idTipoFormaGiuridica)
      throws SolmrException;

  public Long getIdTipologiaAziendaByFormaGiuridica(Long idTipoFormaGiuridica,
      Boolean flagAziendaProvvisoria) throws SolmrException;

  public Long getIdTipoFormaGiuridica(String idTipoFormaGiuridicaAAEP)
      throws SolmrException;

  public String getDescTipoFormaGiuridica(String idTipoFormaGiuridicaAAEP)
      throws SolmrException;

  public boolean isProvinciaReaValida(String siglaProvincia)
      throws SolmrException;

  public boolean isFlagUnivocitaAzienda(Integer idTipoAzienda)
      throws SolmrException;

  public PersonaFisicaVO getRappresentanteLegaleFromIdAnagAzienda(
      Long idAnagAzienda) throws SolmrException;

  public PersonaFisicaVO getRappresentanteLegaleFromIdAnagAziendaAndDichCons(
      long idAnagAzienda, Date dataDichiarazione) throws SolmrException;
  
  public Vector<PersonaFisicaVO> getVAltriSoggettiFromIdAnagAziendaAndDichCons(
      long idAnagAzienda, Date dataDichiarazione) throws Exception,
      SolmrException;

  public UtenteIrideVO getUtenteIrideById(Long idUtente) throws SolmrException;

  public Vector<Long> getAnniRilevamento() throws SolmrException;

  public Vector<CodeDescription> getUnitaProduttive(Long idAzienda)
      throws SolmrException;

  public TerreniVO getTerreni(Long idAzienda, Long idUte, Long anno,
      String criterio) throws SolmrException;

  public Vector<Vector<Long>> getIdParticelle(Long idAzienda, Long idUte,
      Long anno, String criterio, Long valore) throws SolmrException;

  public Vector<ParticelleVO> getParticelleByIdRange(
      Vector<Vector<Long>> idRange) throws SolmrException;
  
  public Vector<TipoSettoreAbacoVO> getListSettoreAbaco() 
      throws SolmrException;

  public Long updateTitolareAzienda(AnagAziendaVO anagAziendaVO,
      PersonaFisicaVO personaTitolareOldVO,
      PersonaFisicaVO personaTitolareNewVO, long idUtenteAggiornamento)
      throws SolmrException;

  public AnagAziendaVO findAziendaAttivabyCriterio(String cuaa,
      String partitaIva) throws SolmrException;

  public Vector<StringcodeDescription> getProvincePiemonte()
      throws SolmrException;

  public ProvinciaVO getProvinciaByCriterio(String criterio)
      throws SolmrException;

  public String ricercaCodiceComuneNonEstinto(String descrizioneComune,
      String siglaProvincia) throws SolmrException;
  
  public String ricercaCodiceComuneFlagEstinto(String descrizioneComune,
      String siglaProvincia, String estinto)  throws SolmrException;

  public void insertRappLegaleTitolare(Long idAzienda, PersonaFisicaVO pfVO,
      long idUtenteAggiornamento) throws SolmrException;

  public Vector<Long> getIdPersoneFisiche(String codFiscale, String cognome,
      String nome, String dataNascita, String istatNascita,
      String istatResidenza, boolean personaAttiva) throws SolmrException;

  public Vector<PersonaFisicaVO> getListPersoneFisicheByIdRange(
      Vector<Long> collIdPF) throws SolmrException;

  public PersonaFisicaVO findByPrimaryKey(Long idPersonaFisica)
      throws SolmrException;

  public Vector<PersonaFisicaVO> findPersonaFisicaByIdSoggettoAndIdAzienda(
      Long idSoggetto, Long idAzienda) throws SolmrException;

  public String getDenominazioneByIdAzienda(Long idAzienda)
      throws SolmrException;

  public String getRappLegaleTitolareByIdAzienda(Long idAzienda)
      throws SolmrException;

  public Vector<Long> getIdAziendeBySoggetto(Long idSoggetto)
      throws SolmrException;

  public Vector<AnagAziendaVO> findAziendeByIdAziende(Vector<Long> idAziendeVect)
      throws SolmrException;

  public String getSiglaProvinciaByIstatProvincia(String istatProvincia)
      throws SolmrException;

  public PersonaFisicaVO findPersonaFisica(Long idPersonaFisica)
      throws SolmrException;

  public PersonaFisicaVO getDatiSoggettoPerMacchina(Long idPersonaFisica)
      throws SolmrException;

  public AnagraficaAzVO getDatiAziendaPerMacchine(Long idAzienda)
      throws SolmrException;

  public Vector<AnagAziendaVO> getListaStoricoAzienda(Long idAzienda)
      throws SolmrException;

  public Vector<UteVO> getElencoUteAttiveForAzienda(Long idAzienda)
      throws SolmrException;

  public Vector<CodeDescription> getSezioniByComune(String istatComune)
      throws SolmrException;

  public Vector<FoglioVO> getFogliByComuneAndSezione(String istatComune,
      String sezione, Long foglio) throws SolmrException;

  public Vector<ParticellaVO> getParticelleByParametri(
      String descrizioneComune, Long foglio, String sezione, Long particella,
      String flagEstinto) throws SolmrException;

  public String ricercaSezione(String istatComune, String sezione)
      throws SolmrException;

  public FoglioVO ricercaFoglio(String istatComune, String sezione, Long foglio)
      throws SolmrException;

  public ParticellaVO ricercaParticellaAttiva(String istatComune,
      String sezione, Long foglio, Long particella, String subalterno)
      throws SolmrException;

  public void checkParticellaByAzienda(Long idParticella, Long idAzienda)
      throws SolmrException;

  public Vector<ElencoAziendeParticellaVO> elencoAziendeByParticellaAndConduzione(
      Long idParticella, Long idAzienda) throws SolmrException;

  public Vector<CodeDescription> getTipiTitoloPossessoExceptProprieta()
      throws SolmrException;

  public java.util.Date getMaxDataFineConduzione(Long idParticella,
      Long idAzienda) throws SolmrException;

  public Vector<CodeDescription> getTipiUtilizzoAttivi() throws SolmrException;

  public Vector<CodeDescription> getTipiUtilizzoAttivi(int idIndirizzo)
      throws SolmrException;

  public Long insertParticella() throws SolmrException;

  public ParticellaVO ricercaParticellaProvvisoriaAttiva(String istatComune,
      String sezione, Long foglio) throws SolmrException;

  public ParticellaVO ricercaParticella(String istatComune, String sezione,
      Long foglio, Long particella, String subalterno) throws SolmrException;

  public void cessaParticelleByIdParticellaRange(long idParticella[])
      throws SolmrException;

  public double getMaxSupCatastaleInseribile(Long idParticella)
      throws SolmrException;

  public ParticellaVO findParticellaByPrimaryKey(Long idStoricoParticella)
      throws SolmrException;

  public Vector<CodeDescription> getTitoliStudio() throws SolmrException;

  public Vector<CodeDescription> getPrefissiCellulare() throws SolmrException;

  public Vector<CodeDescription> getIndirizzoStudioByTitolo(Long idTitoloStudio)
      throws SolmrException;

  public Vector<ContoCorrenteVO> getContiCorrenti(Long idAzienda,
      java.util.Date dataSituazioneAl) throws SolmrException;

  public Vector<ContoCorrenteVO> getContiCorrenti(Long idAzienda,
      boolean estinto) throws SolmrException;

  public void deleteContoCorrente(Long idConto, long idUtenteAggiornamento)
      throws SolmrException;

  public BancaSportelloVO[] searchBanca(String abi, String denominazione)
      throws SolmrException;

  public BancaSportelloVO[] searchSportello(String abi, String cab,
      String comune) throws SolmrException;

  public Vector<CodeDescription> getTipiTipologiaFabbricato()
      throws SolmrException;

  public Vector<CodeDescription> getTipiFormaFabbricato(
      Long idTipologiaFabbricato) throws SolmrException;

  public Vector<CodeDescription> getTipiColturaSerra(Long idTipologiaFabbricato)
      throws SolmrException;

  public String getUnitaMisuraByTipoFabbricato(Long idTipologiaFabbricato)
      throws SolmrException;

  public int getMesiRiscaldamentoBySerra(String tipologiaColturaSerra)
      throws SolmrException;

  public double getFattoreCubaturaByFormaFabbricato(String idFormaFabbricato)
      throws SolmrException;

  public Vector<ParticellaVO> getElencoParticelleFabbricatoByUte(Long idUte,
      boolean serra) throws SolmrException;

  public String getSuperficiFabbricatiByParticella(Long idUte, Long idParticella)
      throws SolmrException;

  public Long inserisciFabbricato(FabbricatoVO fabbricatoVO,
      Vector<ParticellaVO> elencoParticelleSelezionate, long idUtenteAggiornamento)
      throws SolmrException;

  public Vector<FabbricatoVO> ricercaFabbricatiByAzienda(Long idAzienda,
      String dataSituazioneAl) throws SolmrException;

  public String getSearchIdSportello(String abi, String cab)
      throws SolmrException;

  public void insertContoCorrente(ContoCorrenteVO conto,
      long idUtenteAggiornamento) throws SolmrException;
  
  public void storicizzaContoCorrente(ContoCorrenteVO conto, Long idUtente) 
      throws SolmrException;

  public ContoCorrenteVO getContoCorrente(String idContoCorrente)
      throws SolmrException;

  public void desistsAccountCorrent(Long idAzienda, Long idUtente)
      throws SolmrException;

  public FabbricatoVO findFabbricatoByPrimaryKey(Long idFabbricato)
      throws SolmrException;

  public Vector<ParticellaVO> getElencoParticelleByFabbricato(
      FabbricatoVO fabbricatoVO, boolean modifica) throws SolmrException;

  public Vector<ParticellaVO> getElencoParticelleFabbricatoByUteAssociabili(
      Long idUte, Vector<Long> elencoParticelle, boolean serra)
      throws SolmrException;

  public void cessaUtilizzoParticellaFabbricato(FabbricatoVO fabbricatoVO)
      throws SolmrException;

  public void deleteParticellaFabbricato(ParticellaVO particellaFabbricatoVO)
      throws SolmrException;

  public Long modificaFabbricato(FabbricatoVO fabbricatoVO,
      Vector<ParticellaVO> particelleForFabbricato,
      Vector<ParticellaVO> elencoParticelleAssociate,
      Vector<ParticellaVO> elencoParticelleAssociabili, long idUtenteAggiornamento,
      long idAzienda) throws SolmrException;

  public void deleteParticellaFabbricatoByIdFabbricato(FabbricatoVO fabbricatoVO)
      throws SolmrException;

  public void deleteFabbricato(FabbricatoVO fabbricatoVO) throws SolmrException;

  public void eliminaFabbricato(FabbricatoVO fabbricatoVO, long idAzienda)
      throws SolmrException;

  public Vector<UteVO> getElencoUteAttiveForDateAndAzienda(Long idAzienda,
      String data) throws SolmrException;

  public Vector<ParticellaVO> getElencoParticelleForUteAndAzienda(Long idAzienda)
      throws SolmrException;

  public Vector<ParticellaVO> getElencoParticelleForUteAndAziendaPossAndComune(
      Long idAzienda) throws SolmrException;

  public Vector<ParticellaVO> getElencoParticelleForUteAndAziendaComune(
      Long idAzienda) throws SolmrException;

  public Vector<ParticellaVO> ricercaParticelleAttiveByParametri(
      ParticellaVO particellaVO, String data, Long idAzienda)
      throws SolmrException;

  public void checkCessaAziendaByConduzioneParticella(Long idUte)
      throws SolmrException;

  public Vector<CodeDescription> getListaDateConsistenza(Long idAzienda)
      throws SolmrException;

  public String insediamentoAtomico(AnagAziendaVO modAnagAziendaVO,
      PersonaFisicaVO modPersonaVO, HttpServletRequest request,
      AnagAziendaVO anagAziendaVO, SianUtenteVO sianUtenteVO, RuoloUtenza ruoloUtenza)
      throws SolmrException;

  public Vector<ParticellaVO> getElencoConsistenzaParticelleForAzienda(
      Long idAzienda, Long idDichiarazioneConsistenza) throws SolmrException;

  public void checkLastAggiornamentoAfterMaxDichConsistenza(Long idAzienda)
      throws SolmrException;

  public Vector<ParticellaVO> getElencoConsistenzaParticelleForAziendaAndPossessoAndComune(
      Long idAzienda, Long idDichiarazioneConsistenza) throws SolmrException;

  public Vector<ParticellaVO> getElencoConsistenzaParticelleForAziendaAndComune(
      Long idAzienda, Long idDichiarazioneConsistenza) throws SolmrException;

  public String getAnnoPrevisioneUtilizzi(Long idAzienda) throws SolmrException;

  public String getTotaleSupCondotteByAzienda(Long idAzienda, String data)
      throws SolmrException;

  public Vector<ParticellaVO> getElencoConsistenzaParticelleForAziendaAndDestinazioneUso(
      Long idAzienda, Long idDichiarazioneConsistenza,
      Long idConduzioneParticella) throws SolmrException;

  public Vector<ParticellaVO> ricercaParticelleStoricizzateByParametri(
      ParticellaVO particellaVO, Long idDichiarazioneConsistenza)
      throws SolmrException;

  public ParticellaVO getDettaglioParticellaDatiTerritoriali(
      Long idStoricoParticella) throws SolmrException;

  public Vector<ParticellaUtilizzoVO> getElencoParticellaUtilizzoVO(
      Long idConduzioneParticella, String anno) throws SolmrException;

  public double getTotaleSupUtilizzateByIdConduzioneParticella(
      Long idConduzioneParticella, String anno) throws SolmrException;

  public ParticellaVO getDettaglioParticellaStoricizzataDatiTerritoriali(
      Long idConduzioneDichiarata) throws SolmrException;

  public ParticellaVO getDettaglioParticellaStoricizzataConduzione(
      Long idConduzioneDichiarata) throws SolmrException;

  public Vector<ParticellaUtilizzoVO> getElencoStoricoParticellaUtilizzoVO(
      Long idConduzioneDichiarata, Long idDichiarazioneConsistenza)
      throws SolmrException;

  public Vector<CodeDescription> getElencoIndirizziUtilizzi()
      throws SolmrException;

  public Vector<CodeDescription> getTipiUtilizzoForIdIndirizzoTipoUtilizzo(
      Long idTipoIndirizzoUtilizzo) throws SolmrException;

  public Vector<ParticellaVO> ricercaParticelleByParametriAndUtilizzi(
      ParticellaVO particellaRicercaVO, Long idAzienda) throws SolmrException;

  public Vector<ParticellaVO> ricercaParticelleByParametriAndUtilizzoSpecificato(
      Long idAzienda, ParticellaVO particellaRicercaVO) throws SolmrException;

  public Vector<ParticellaVO> ricercaParticelleByParametriSenzaUsoSuolo(
      ParticellaVO particellaRicercaVO, Long idAzienda) throws SolmrException;

  public Vector<ParticellaVO> ricercaParticelleStoricizzateByParametriAndUtilizzo(
      ParticellaVO particellaRicercaVO) throws SolmrException;

  public Vector<ParticellaVO> ricercaParticelleStoricizzateByParametriAndUtilizzoSpecificato(
      ParticellaVO particellaRicercaVO) throws SolmrException;

  public Vector<ParticellaVO> ricercaParticelleStoricizzateByParametriSenzaUsoSuolo(
      ParticellaVO particellaRicercaVO) throws SolmrException;

  public Vector<CodeDescription> getTipiRuoloNonTitolareAndNonSpecificato()
      throws SolmrException;

  public Long storicizzaDatiResidenza(PersonaFisicaVO pfVO, long idUtenteAggiornamento)
      throws SolmrException;

  public void changeLegameBetweenPersoneAndAziende(Long newIdSoggetto,
      Long oldIdSoggetto, Long idAzienda) throws SolmrException;

  public void cessaLegameBetweenPersonaAndAzienda(Long idSoggetto,
      Long idAzienda) throws SolmrException;

  public void checkUpdateSuperficie(Long idAzienda) throws SolmrException;

  public ParticellaVO getParticellaVOByIdUtilizzoParticella(
      Long idUtilizzoParticella) throws SolmrException;

  public ParticellaVO getParticellaVOByIdConduzioneParticella(
      Long idConduzioneParticella) throws SolmrException;

  public double getTotSupUtilizzateByIdCondParticellaNotIncludeIdUtilizzo(
      Long idConduzioneParticella, Long idUtilizzoParticella, String anno)
      throws SolmrException;

  public String getValoreFromParametroByIdCode(String codice)
      throws SolmrException;

  public Date getMaxDataDichiarazioneConsistenza(Long idAzienda)
      throws SolmrException;

  public String getTotaleSupCondotteByIdParticellaNotIncludeIdConduzioneParticella(
      Long idConduzioneParticella, Long idParticella) throws SolmrException;

  public Vector<ParticellaVO> getElencoDichiarazioniConsistenzaByIdAzienda(
      Long idAzienda) throws SolmrException;

  public String getTotaleSupCondotteDichiarateByAzienda(Long idAzienda,
      Long idDichiarazioneConsistenza) throws SolmrException;

  public void checkEliminaUtilizziVigneto(
      Vector<String> elencoIdUtilizziParticella) throws SolmrException;

  public void checkParticellaLegataDichiarazioneConsistenza(
      Vector<Long> elencoParticelle) throws SolmrException;

  public void deleteUtilizzoParticellaByIdConduzioneParticella(
      Long idConduzioneParticella) throws SolmrException;

  public void deleteConduzioneParticella(Long idConduzioneParticella)
      throws SolmrException;

  public void eliminaParticelle(Vector<Long> elencoConduzioni, Long idAzienda, RuoloUtenza ruoloUtenza)
      throws SolmrException;

  public void cessaFabbricato(Long idFabbricato, long idUtenteAggiornamento)
      throws SolmrException;

  public void storicizzaManodopera(ManodoperaVO manodoperaVO)
      throws SolmrException;
  
  public Vector<TipoIscrizioneINPSVO> getElencoTipoIscrizioneINPSAttivi()
      throws SolmrException;
  
  public ManodoperaVO findManodoperaAttivaByIdAzienda(long idAzienda)
      throws SolmrException;

  public void cessazioneUTE(UteVO uteVO, long idUtenteAggiornamento)
      throws SolmrException;

  public ParticellaVO getParticellaVOByIdConduzioneParticellaSenzaUsoSuolo(
      Long idConduzioneParticella, String anno) throws SolmrException;

  public ParticellaVO getDettaglioParticellaDatiTerritorialiByIdConduzioneParticella(
      Long idConduzioneParticella) throws SolmrException;

  public void allegaUtilizziToNewConduzioneParticella(
      Long newIdConduzioneParticella, Long oldIdConduzioneParticella)
      throws SolmrException;

  public PersonaFisicaVO getDettaglioSoggettoByIdContitolare(Long idContitolare)
      throws SolmrException;
  
  public  TesserinoFitoSanitarioVO getTesserinoFitoSanitario(String codiceFiscale) 
      throws SolmrException;

  public void updateDatiSoggettoAndStoricizzaResidenza(
      PersonaFisicaVO newPersonaFisicaVO, PersonaFisicaVO oldPersonaFisicaVO,
      long idUtenteAggiornamento) throws SolmrException;

  // INIZIO TERRENI
  public Vector<ParticellaVO> ricercaTerreniByParametri(
      ParticellaVO particellaRicercaTerrenoVO) throws SolmrException;

  public boolean isParticellaContenziosoOnAzienda(Long idStoricoParticella)
      throws SolmrException;

  public String getTotSupCondotteByIdStoricoParticella(Long idStoricoParticella)
      throws SolmrException;

  public Vector<ParticellaAziendaVO> getElencoAziendeAndConduzioniByIdStoricoParticella(
      Long idStoricoParticella, boolean attive) throws SolmrException;

  public Vector<ParticellaUtilizzoVO> getElencoUtilizziAttiviByAnnoAndIdStoricoParticella(
      Long idStoricoParticella, String anno) throws SolmrException;

  public int countParticelleConConduzioniAttive(long idParticella[])
      throws SolmrException;

  public Vector<CodeDescription> getTipiTipologiaNotifica() throws SolmrException;
  
  public Vector<CodeDescription> getTipologiaNotificaFromRuolo(RuoloUtenza ruoloUtenza) 
      throws SolmrException;
  
  public Vector<CodeDescription> getTipiTipologiaNotificaFromEntita(String tipoEntita)
      throws SolmrException;
  
  public Vector<TipoCategoriaNotificaVO> getTipiCategoriaNotificaFromRuolo(RuoloUtenza ruoloUtenza) 
      throws SolmrException;
  
  public Vector<TipoCategoriaNotificaVO> getTipiCategoriaNotificaFromEntita(String tipoEntita)
      throws SolmrException;
  
  public Vector<CodeDescription> getCategoriaNotifica() throws SolmrException;

  public Vector<ParticellaUtilizzoVO> getElencoParticelleForAziendaAndUsoSecondario(
      Long idAzienda, String anno) throws SolmrException;

  public Vector<ParticellaUtilizzoVO> getElencoConsistenzaParticelleForAziendaAndUsoSecondario(
      Long idAzienda, Long idDichiarazioneConsistenza) throws SolmrException;

  public Vector<EsitoControlloParticellaVO> getElencoEsitoControlloParticella(
      Long idConduzioneParticella) throws SolmrException;

  public String getDataUltimaEsecuzioneControlli(Long idAzienda)
      throws SolmrException;

  public ParticellaCertificataVO findParticellaCertificataByParametri(
      ParticellaVO particellaVO) throws SolmrException;

  public Vector<CodeDescription> getTipiDocumento() throws SolmrException;

  public Vector<ParticellaVO> getElencoStoricoParticella(Long idParticella)
      throws SolmrException;

  public void eliminaUtilizzoParticella(Vector<String> elencoConduzioni,
      Vector<String> elencoIdUtilizzoParticella, long idUtenteAggiornamento)
      throws SolmrException;

  public Vector<ParticellaVO> getElencoParticelleForImportByAzienda(
      AnagAziendaVO searchAnagAziendaVO, AnagAziendaVO anagAziendaVO,
      RuoloUtenza ruoloUtenza) throws SolmrException;

  public ParticellaVO getStoricoParticella(Long idStoricoParticella)
      throws SolmrException;

  public Vector<CodeDescription> getIndirizziTipiUtilizzoAttivi()
      throws SolmrException;

  public Vector<CodeDescription> getTipiAreaA() throws SolmrException;

  public Vector<CodeDescription> getTipiAreaB() throws SolmrException;

  public Vector<CodeDescription> getTipiAreaC() throws SolmrException;

  public Vector<CodeDescription> getTipiAreaD() throws SolmrException;

  public Vector<CodeDescription> getTipiZonaAltimetrica() throws SolmrException;

  public Vector<CodeDescription> getTipiCasoParticolare() throws SolmrException;

  // FINE TERRENI

  // INIZIO NOTIFICHE
  public ElencoNotificheVO ricercaNotificheByParametri(NotificaVO notificaVO,
      UtenteAbilitazioni utenteAbilitazioni, RuoloUtenza ruoloUtenza, Boolean storico, int maxRecord) throws SolmrException;

  public NotificaVO findNotificaByPrimaryKey(Long idNotifica, String provenienza)
      throws SolmrException;

  public Vector<NotificaVO> getElencoNotificheByIdAzienda(NotificaVO notificaVO,
      Boolean storico, String ordinamento) throws SolmrException;
  
  public Vector<NotificaVO> getElencoNotifichePopUp(NotificaVO notificaVO) 
      throws SolmrException;

  public Long insertNotifica(NotificaVO notificaVO, long idUtenteAggiornamento)
      throws SolmrException;
  
  public void updateNotifica(NotificaVO notificaVO, long idUtenteAggiornamento)
      throws SolmrException;

  public void closeNotifica(NotificaVO notificaVO, long idUtenteAggiornamento)
      throws SolmrException;
  
  public Long getIdTipologiaNotificaFromCategoria(Long idCategoriaNotifica)
      throws SolmrException;
  
  public boolean isChiusuraNotificaRuoloPossibile(RuoloUtenza ruoloUtenza, long idCategoriaNotifica)
      throws SolmrException;
  
  public Vector<NotificaVO> getElencoNotificheForIdentificato(long identificativo, 
      String codiceTipo, long idAzienda, Long idDichiarazioneConsistenza) 
    throws SolmrException;
  
  public boolean isModificaNotificaRuoloPossibile(RuoloUtenza ruoloUtenza, long idCategoriaNotifica)
      throws SolmrException;
  
  public HashMap<Long,Vector<NotificaEntitaVO>> getNotificheEntitaUvFromIdNotifica(
      long ids[]) throws SolmrException;
  
  public HashMap<Long,Vector<NotificaEntitaVO>> getNotificheEntitaParticellaFromIdNotifica(
      long ids[]) throws SolmrException;

  // FINE NOTIFICHE

  // INIZIO PROFILAZIONE
  public DelegaVO intermediarioConDelega(UtenteAbilitazioni utenteAbilitazioni, Long idAzienda)
      throws SolmrException;

  public boolean isIntermediarioConDelegaDiretta(long idIntermediario,
      Long idAzienda) throws SolmrException;

  public boolean isIntermediarioPadre(long idIntermediario, Long idAzienda)
      throws SolmrException;

  // FINE PROFILAZIONE

  // INIZIO ALTRE RICERCHE
  public Vector<AnagAziendaVO> getElencoAziendeByCAA(DelegaVO delegaVO) throws SolmrException;

  // FINE RICERCHE

  // INIZIO PRATICHE
  public Vector<ProcedimentoAziendaVO> updateAndGetPraticheByAzienda(Long idAzienda)
      throws SolmrException;

  public Vector<ProcedimentoAziendaVO> getElencoProcedimentiByIdAzienda(
      Long idAzienda, Long annoProc, Long idProcedimento,
      Long idAziendaSelezionata) throws SolmrException;

  public CodeDescription[] getListCuaaAttiviProvDestByIdAzienda(Long idAzienda)
      throws Exception;

  // FINE PRATICHE

  // SIAN
  public String getDescriptionSIANFromCode(String tableName, String code)
      throws SolmrException;

  public void sianAggiornaDatiTributaria(String[] elencoCuaa, SianUtenteVO sianUtenteVO)
      throws SolmrException;

  public DoubleStringcodeDescription[] getListSianTipoOpr(boolean principale,
      String orderBy[]) throws SolmrException;

  public String getOrganismoPagatoreFormatted(String codiceSianOpr)
      throws SolmrException;

  // SIAN

  // TERAMO
  public StringcodeDescription getSianTipoSpecieByIdSpecieAnimale(
      long idTipoSpecieAnimale) throws SolmrException;

  public StringcodeDescription getSianTipoSpecieByCodiceSpecie(
      String codiceSpecie) throws SolmrException;

  public void sianAggiornaDatiBDN(String CUAA) throws SolmrException;

  // TERAMO

  // INIZIO ANAGRAFICA AZIENDA
  public void updateAnagrafe(AnagAziendaVO anagAziendaVO,
      long idUtenteAggiornamento, PersonaFisicaVO pfVO, boolean isCuaaChanged, 
      PersonaFisicaVO pfVOTributaria, Vector<Long> vIdAtecoTrib) throws SolmrException;
  
  public void updateAnagrafeSemplice(AnagAziendaVO anagAziendaVO, long idUtenteAggiornamento) 
      throws SolmrException;

  public void checkStatoAzienda(Long idAzienda) throws SolmrException;

  public Vector<String> getListCUAAByIdAzienda(Long idAzienda) throws SolmrException;

  public String getDenominazioneAziendaByCuaaAndIdAzienda(Long idAzienda,
      String cuaa) throws SolmrException;

  public Date getMinDataInizioValiditaAnagraficaAzienda(Long idAzienda)
      throws SolmrException;

  public void modificaGestoreFascicolo(AnagAziendaVO anagAziendaVO)
      throws SolmrException;

  public java.util.Date getDataMaxFineMandato(Long idAzienda)
      throws SolmrException;

  public Long insertDelegaForMandato(AnagAziendaVO anagAziendaVO,
      RuoloUtenza ruoloUtenza, DelegaVO delegaVO, DocumentoVO documentoVO)
      throws SolmrException;

  public DelegaVO getDelegaByAziendaAndIdProcedimento(Long idAzienda,
      Long idProcedimento) throws SolmrException;

  public java.util.Date getDataMaxInizioMandato(Long idAzienda)
      throws SolmrException;

  public DelegaVO[] getStoricoDelegheByAziendaAndIdProcedimento(Long idAzienda,
      Long idProcedimento, String[] orderBy) throws SolmrException;

  public AnagAziendaVO[] getListAnagAziendaVOByCuaa(String cuaa,
      boolean onlyActive, boolean isCessata, String[] orderBy)
      throws SolmrException;

  public Date getMaxDataInizioValiditaAnagraficaAzienda(Long idAzienda)
      throws SolmrException;

  public AnagAziendaVO[] getListAnagAziendaDestinazioneByIdAzienda(
      Long idAzienda, boolean onlyActive, String[] orderBy)
      throws SolmrException;

  public Vector<AziendaCollegataVO> getAziendeCollegateByIdAzienda(
      Long idAzienda, boolean flagStorico) throws SolmrException;

  public Vector<AnagAziendaVO> getEntiAppartenenzaByIdAzienda(Long idAzienda,
      boolean flagStorico) throws SolmrException;

  public AnagAziendaVO findAziendaByIdAnagAzienda(Long anagAziendaPK)
      throws SolmrException;

  public AnagAziendaVO[] getListAziendeParticelleAsservite(
      Long idStoricoParticella, Long idAzienda, Long idTitoloPossesso, Date dataInserimentoDichiarazione)
      throws SolmrException;

  public Vector<AziendaCollegataVO> getAziendeCollegateByRangeIdAziendaCollegata(
      Vector<Long> vIdAziendaCollegata) throws SolmrException;

  public Vector<AnagAziendaVO> getIdAziendaCollegataAncestor(Long idAzienda)
      throws SolmrException;

  public Vector<AnagAziendaVO> getIdAziendaCollegataDescendant(Long idAzienda)
      throws SolmrException;

  public boolean controlloAziendeAssociate(String CUAApadre,
      Long idAziendaCollegata) throws SolmrException;

  public Vector<AnagAziendaVO> getAziendeCollegateByRangeIdAzienda(
      Vector<Long> vIdAzienda, Long idAziendaPadre) throws SolmrException;

  // FINE ANAGRAFICA AZIENDA

  /* Gestione Allevamenti */
  public Vector<AllevamentoAnagVO> getAllevamentiByIdUTE(Long idUTE, int anno)
      throws SolmrException;

  public Vector<Vector<AllevamentoAnagVO>> getAllevamentiByIdAzienda(Long idAzienda, int anno)
      throws SolmrException;

  public AllevamentoAnagVO getAllevamento(Long idAllevamento)
      throws SolmrException;

  public Vector<CategorieAllevamentoAnagVO> getCategorieAllevamento(Long idAllevamento)
      throws SolmrException;

  public Long insertAllevamento(AllevamentoAnagVO allevamentoVO,
      long idUtenteAggiornamento) throws SolmrException;

  public Vector<UteVO> getElencoIdUTEByIdAzienda(Long idAzienda) throws SolmrException;

  public Vector<TipoASLAnagVO> getTipiASL() throws SolmrException;

  public Vector<TipoSpecieAnimaleAnagVO> getTipiSpecieAnimale() throws SolmrException;

  public Vector<TipoSpecieAnimaleAnagVO> getTipiSpecieAnimaleAzProv() throws SolmrException;

  public void deleteAllevamentoAll(Long idAllevamento) throws SolmrException;

  public Vector<TipoCategoriaAnimaleAnagVO> getCategorieByIdSpecie(Long idSpecie) throws SolmrException;

  public void updateAllevamento(AllevamentoAnagVO all,
      long idUtenteAggiornamento) throws SolmrException;

  public Integer[] getAnniByIdAzienda(Long idAzienda) throws SolmrException;

  public Vector<CodeDescription> getTipoTipoProduzione(long idSpecie) throws SolmrException;

  public Vector<CodeDescription> getOrientamentoProduttivo(long idSpecie, long idTipoProduzione)
      throws SolmrException;
  
  public Vector<CodeDescription> getTipoProduzioneCosman(
      long idSpecie, long idTipoProduzione, long idOrientamentoProduttivo) throws SolmrException;
  
  public Vector<CodeDescription> getSottocategorieCosman(long idSpecie, 
      long idTipoProduzione, long idOrientamentoProduttivo, long idTipoProduzioneCosman, String flagEsiste)
      throws SolmrException;

  public void storicizzaAllevamento(AllevamentoAnagVO all,
      long idUtenteAggiornamento) throws SolmrException;

  public Vector<AllevamentoAnagVO> getAllevamentiByIdAziendaOrdinati(Long idAzienda, int anno)
      throws SolmrException;

  public TipoSpecieAnimaleAnagVO getTipoSpecieAnimale(Long idSpecieAnimale)
      throws SolmrException;

  public boolean isRecordSianInAnagrafe(AnagAziendaVO anagAziendaVO,
      SianAllevamentiVO sianAllevamentiVO) throws SolmrException;
  
  public String getRegioneByProvincia(String siglaProvincia) throws SolmrException;

  public String getIstatProvinciaBySiglaProvincia(String siglaProvincia)
      throws SolmrException;

  public Vector<Long> getListIdUteByIstatComuneAndIdAzienda(String istatComune,
      Long idAzienda, boolean isActive) throws SolmrException;

  public TipoASLAnagVO getTipoASLAnagVOByExtIdAmmCompetenza(
      Long idAmmCompetenza, boolean isActive) throws SolmrException;

  public AllevamentoAnagVO[] getListAllevamentiAziendaByPianoRifererimento(
      Long idAzienda, Long idPianoRiferimento, Long idUte, String[] orderBy)
      throws SolmrException;
  
  public TipoCategoriaAnimaleAnagVO getTipoCategoriaAnimale(Long idCategoriaAnimale)
      throws SolmrException;

  /* Gestione Allevamenti */

  /* Manodopera */
  public Vector<FrmManodoperaVO> getManodoperaAnnua(ManodoperaVO manodoperaVO)
      throws SolmrException;

  public Vector<FrmManodoperaVO> getManodoperaByPianoRifererimento(ManodoperaVO manodoperaVO,
      Long idPianoRiferimento) throws SolmrException;

  public ManodoperaVO dettaglioManodopera(Long idManodopera)
      throws SolmrException;

  public void deleteManodopera(Long idManodopera) throws SolmrException;

  public void insertManodopera(ManodoperaVO manodoperaVO, Long idAzienda)
      throws SolmrException;

  public Vector<CodeDescription> getTipoFormaConduzione() throws SolmrException;

  public Vector<CodeDescription> getTipoAttivitaComplementari() throws SolmrException;

  public Vector<CodeDescription> getTipoClassiManodopera() throws SolmrException;

  public ManodoperaVO findLastManodopera(Long idAzienda) throws SolmrException;

  public String isManodoperaValida(Long idManodopera, Long idAzienda)
      throws SolmrException;

  public void updateManodopera(ManodoperaVO manodoperaVO, Long idAzienda)
      throws SolmrException;
  
  public void updateManodoperaSian(ManodoperaVO manodoperaChgVO, long idAzienda, long idUtente)
      throws SolmrException;

  /* Manodopera */

  //public Vector getComuniAzienda(Long idAzienda) throws SolmrException;

  public boolean isDataInizioValida(long idAzienda, Date dataInizio)
      throws SolmrException;

  public Vector<ParticellaVO> getElencoParticelleForAziendaAndUtilizzo(Long idAzienda,
      String anno) throws SolmrException;

  /* Consistenza */
  public boolean previsioneAnnoSucessivo(Long idAzienda) throws SolmrException;

  public boolean controlloUltimeModifiche(Long idAzienda, Integer anno)
      throws SolmrException;

  public String controlliDichiarazionePLSQL(Long idAzienda, Integer anno,
      Long idMotivoDichiarazione, Long idUtente) throws SolmrException;

  public void controlliParticellarePLSQL(Long idAzienda, Integer anno, Long idUtente)
      throws SolmrException;

  public String controlliVerificaPLSQL(Long idAzienda, Integer anno,
      Integer idGruppoControllo, Long idUtente) throws SolmrException;

  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichiarazioneConsistenza(Long idAzienda,
      Long idMotivoDichiarazione) throws SolmrException;
  
  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichConsPG(long idDichiarazioneConsistenza, long fase) 
      throws SolmrException;

  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichConsistenzaTerreni(Long idAzienda)
      throws SolmrException;

  public Long salvataggioDichiarazionePLQSL(ConsistenzaVO consistenzaVO,
      Long idAzienda, Integer anno, RuoloUtenza ruoloUtenza)
      throws SolmrException;

  public Vector<ConsistenzaVO> getDichiarazioniConsistenza(Long idAzienda)
      throws SolmrException;

  public Vector<ConsistenzaVO> getDichiarazioniConsistenzaMinimo(Long idAzienda)
      throws SolmrException;

  public ConsistenzaVO getDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza) throws SolmrException;

  public ErrAnomaliaDicConsistenzaVO getAnomaliaDichiarazioneConsistenza(
      Long idDichiarazioneSegnalazione) throws SolmrException;

  public Vector<ErrAnomaliaDicConsistenzaVO> getAnomaliePerCorrezione(
      Long elencoIdDichiarazioneSegnalazione[], long idMotivoDichiarazione)
      throws SolmrException;

  public Vector<DichiarazioneSegnalazioneVO> getListDichiarazioneSegnalazioni(Long idAzienda,
      Long idDichiarazioneConsistenza, Long idStoricoParticella)
      throws SolmrException;

  public ErrAnomaliaDicConsistenzaVO[] getListAnomalieByIdDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza, Long idFase,
      ErrAnomaliaDicConsistenzaVO errAnomaliaDicConsistenzaRicercaVO,
      String[] orderBy) throws SolmrException;

  public ConsistenzaVO[] getListDichiarazioniConsistenzaByIdAzienda(
      Long idAzienda, String[] orderBy) throws SolmrException;
  
  public ConsistenzaVO[] getListDichiarazioniConsistenzaByIdAziendaVarCat(
      Long idAzienda, String[] orderBy) throws SolmrException;

  public void ripristinaPianoRiferimento(Long idDichiarazioneConsistenza,
      Long idUtente) throws SolmrException;

  public Vector<TipoMotivoDichiarazioneVO> getListTipoMotivoDichiarazione(long idAzienda) 
      throws SolmrException;

  public Vector<DichiarazioneSegnalazioneVO> getListDichiarazioneSegnalazioniUnar(Long idAzienda,
      Long idDichiarazioneConsistenza, Long idStoricoUnitaArborea,
      String[] orderBy) throws SolmrException;
  
  public Vector<DichiarazioneSegnalazioneVO> getListDichiarazioneSegnalazioniConsolidamentoUV(
      Long idAzienda) throws SolmrException;

  public ConsistenzaVO findDichiarazioneConsistenzaByPrimaryKey(
      Long idDichiarazioneConsistenza) throws SolmrException;
  
  public FascicoloNazionaleVO getInfoRisultatiSianDichiarazioneConsistenza(
      long idDichiarazioneConsistenza) throws SolmrException;
  
  public Vector<ConsistenzaVO> getListDichiarazioniPianoGrafico(Long idAzienda)
      throws SolmrException;
  
  public EsitoPianoGraficoVO getEsitoPianoGraficoFromAccesso(long idAccessoPianoGrafico)
      throws SolmrException;
  
  public int preCaricamentoPianoGrafico(long idDichiarazioneConsistenza) throws SolmrException;
  
  public EsitoPianoGraficoVO getEsitoPianoGraficoFromPK(long idEsitoGrafico) throws SolmrException;
  
  public PlSqlCodeDescription controlliValidazionePlSql(long idAzienda, int idFase, 
      long idUtente, long idDichiarazioneConsistenza) throws SolmrException;
  
  public Long insertStatoIncorsoPG(long idAccessoPianoGrafico, long idUtente) 
      throws SolmrException;

  public void protocollaDichiarazioniConsistenza(
      Long[] elencoIdDichiarazioniConsistenza, RuoloUtenza ruoloUtenza,
      String anno) throws SolmrException;

  public TipoControlloVO[] getListTipoControlloByIdGruppoControllo(
      Long idGruppoControllo, String orderBy[]) throws SolmrException;
  
  public TipoControlloVO[] getListTipoControlloByIdGruppoControlloAttivi(Long idGruppoControllo, String orderBy[])
      throws SolmrException;
  
  public TipoControlloVO[] getListTipoControlloForAziendaByIdGruppoControllo(
      Long idGruppoControllo, Long idAzienda, String orderBy[]) throws SolmrException;
  
  public TipoControlloVO[] getListTipoControlloForAziendaByIdGruppoControlloAndDichCons(
      Long idGruppoControllo, Long idDichiarazioneConsistenza, String orderBy[])
          throws SolmrException;

  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieForControlliTerreni(Long idAzienda,
      Long idControllo, Vector<String> vTipoErrori, boolean flagOK, String ordinamento)
      throws SolmrException;

  public String getLastAnnoCampagnaFromDichCons(long idAzienda)
      throws SolmrException;

  public TipoMotivoDichiarazioneVO findTipoMotivoDichiarazioneByPrimaryKey(
      Long idMotivoDichiarazione) throws SolmrException;
  
  public String getLastDichConsNoCorrettiva(long idAzienda)
    throws SolmrException;
  
  public ConsistenzaVO getUltimaDichConsNoCorrettiva(long idAzienda) throws SolmrException;
  
  public Date getLastDateDichConsNoCorrettiva(long idAzienda) throws SolmrException;
  
  public Long getLastIdDichConsProtocollata(long idAzienda) throws SolmrException;
  
  public void updateDichiarazioneConsistenzaRichiestaStampa(Long idDichiarazioneConsistenza)
      throws SolmrException;

  public ConsistenzaVO getUltimaDichiarazioneConsistenza(Long idAzienda)
      throws SolmrException;

  /* Consistenza */

  public boolean controllaRegistrazioneMandato(AnagAziendaVO aziendaVO,
      String codiceEnte, DelegaAnagrafeVO delegaAnagrafeVO)
      throws SolmrException;
  
  public boolean controllaRevocaMandato(AnagAziendaVO aziendaVO,
      RuoloUtenza ruoloUtenza, DelegaAnagrafeVO delegaAnagrafeVO)
      throws SolmrException;

  public boolean controllaPresenzaDelega(AnagAziendaVO aziendaVO)
      throws SolmrException;

  /** ********************** */
  /** ********************** */
  /** ********************** */
  /** ****** AEEP ******* */
  /** ********************** */
  /** ********************** */
  /**
   * 
   */
  public Long importaDatiAAEP(AnagAAEPAziendaVO anagAAEPAziendaVO,
      AnagAziendaVO anagAziendaVO, long idUtenteAggiornamento, boolean denominazione,
      boolean partitaIVA, boolean descrizioneAteco, boolean provinciaREA,
      boolean numeroREA, boolean annoIscrizione, boolean numeroRegistroImprese, boolean pec,
      boolean sedeLegale, boolean titolareRappresentante, boolean formaGiuridica,boolean sezione,
      boolean descrizioneAtecoSec, boolean dataIscrizioneREA, boolean dataCancellazioneREA, boolean dataIscrizioneRI)
      throws SolmrException;

  /** ********************** */
  /** ********************** */
  /** ********************** */
  /** ****** AEEP ******* */
  /** ********************** */
  /** ********************** */
  /**
   * 
   * @param idIntermediario
   *          Long
   * @throws SolmrException
   * @return DelegaVO
   */
  public DelegaVO getIntermediarioPerDelega(Long idIntermediario)
      throws SolmrException;

  public IntermediarioAnagVO getIntermediarioAnagByIdIntermediario(
      long idIntermediario) throws SolmrException;
  
  public IntermediarioAnagVO findIntermediarioVOByCodiceEnte(String codEnte)
      throws SolmrException;
  
  public IntermediarioAnagVO findIntermediarioVOByIdAzienda(long idAzienda)
      throws SolmrException;
  
  public boolean isAziendaIntermediario(long idAzienda)
      throws SolmrException;
  
  public Vector<IntermediarioAnagVO> findFigliIntermediarioVOById(long idIntemediario)
      throws SolmrException;
  
  public Vector<IntermediarioAnagVO> findFigliIntermediarioAziendaVOById(long idIntemediario)
      throws SolmrException;

  /** *************** */
  /** *************** */
  /** *** Stampe **** */
  /** *************** */

  public Vector<ParticellaVO> getElencoParticelleQuadroI1(Long idAzienda,
      Long codFotografia) throws SolmrException;

  public BigDecimal[] getTotSupQuadroI1CondottaAndAgronomica(Long idAzienda, Long codFotografia)
      throws SolmrException;
  
  public BigDecimal[] getTotSupQuadroI1CatastaleAndGrafica(Long idAzienda, Long codFotografia)
      throws SolmrException;

  public Long getCodFotTerreniQuadroI1(Long idDichiarazioneConsistenza)
      throws SolmrException;

  public Vector<UteVO> getUteQuadroB(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException;

  public Vector<TipoFormaConduzioneVO> getFormeConduzioneQuadroD()
      throws SolmrException;

  public Long getFormaConduzioneQuadroD(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException;

  public Vector<CodeDescription> getAttivitaComplementariQuadroE(
      Long idAzienda, java.util.Date dataRiferimento) throws SolmrException;

  public Long getIdManodoperaQuadroF(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException;

  public Vector<Long> getAllevamentiQuadroG(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException;

  public Vector<BaseCodeDescription> getTerreniQuadroI5(Long idAzienda,
      java.util.Date dataRiferimento, Long codFotografia) throws SolmrException;

  public Vector<BaseCodeDescription> getTerreniQuadroI4(Long idAzienda,
      Long codFotografia) throws SolmrException;

  public Vector<ParticellaVO> getFabbricatiParticelle(Long idFabbricato,
      java.util.Date dataRiferimento) throws SolmrException;
  
  public Vector<FabbricatoVO> getFabbricati(
      Long idAzienda, java.util.Date dataRiferimento,boolean comunicazione10R) 
      throws SolmrException;

  public Vector<ConsistenzaZootecnicaStampa> getAllevamentiQuadroC10R(
      Long idAzienda, java.util.Date dataRiferimento) throws SolmrException;

  public Vector<QuadroDTerreni> getTerreniQuadroD10R(Long idAzienda)
      throws SolmrException;

  public Vector<QuadroDTerreni> getTerreniQuadroD10R(
      java.util.Date dataRiferimento, Long codFotografia) throws SolmrException;
  
  public Vector<String[]> getAnomalie(Long idAzienda, Long idDichiarazioneConsistenza)
      throws SolmrException;
  
  public Vector<DocumentoVO> getDocumentiStampa(Long idAzienda, Long idDichiarazioneConsistenza,
      String cuaa, java.util.Date dataInserimentoDichiarazione)
    throws SolmrException;

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
   * @throws SolmrException
   * @return DittaUMAVO
   */
  public DittaUMAVO getDittaUmaByIdAzienda(Long idAzienda)
      throws SolmrException;

  public void storicizzaDelega(DelegaVO dVO, RuoloUtenza ruoloUtenza,
      DocumentoVO documentoVO, AnagAziendaVO anagAziendaVO)
      throws SolmrException;
  
  public int storicizzaDelegaTemporanea(DelegaVO delegaVO, RuoloUtenza ruoloUtenza, 
      AnagAziendaVO anagAziendaVO) throws SolmrException;

  
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
  
  
  
  public Boolean aggiornaDatiAAEP(String CUAA) throws SystemException, SolmrException;
  
  // ** FINE SERVIZI INFOCAMERE 
    

  

  public String isFlagPrevalente(Long[] idAteco) throws Exception;
  
  public TipoSezioniAaepVO getTipoSezioneAaepByCodiceSez(String codiceSezione)
      throws Exception;

  public Vector<TemporaneaPraticaAziendaVO> aggiornaPraticaAziendaPLQSL(
      Long idAzienda, Long idUtente, Long idDichiarazioneConsistenza)
      throws SolmrException;
  
  public void aggiornaPraticaAziendaPLQSL(Long idAzienda) 
      throws SolmrException;
  

  public boolean deleteDichConsAmmessa(Long idAzienda, Long idUtente,
      Long idDichiarazioneConsistenza) throws SolmrException;

  public boolean newDichConsAmmessa(Long idAzienda, Long idUtente,
      Long idMotivoDichiarazione) throws SolmrException;

  public void deleteDichiarazioneConsistenza(Long idDichiarazioneConsistenza)
      throws SolmrException;

  public void updateInsediamentoGiovani(Long idAzienda) throws SolmrException;

  public Vector<StringcodeDescription> getListaComuniTerreniByIdAzienda(Long idAzienda)
      throws SolmrException;

  public String getTotaleSupCondotteAttiveByIdParticella(Long idParticella,
      Long idAzienda) throws SolmrException;

  public String[] cessazioneAziendaPLQSL(Long idAzienda) throws SolmrException;

  public boolean controllaObbligoFascicolo(AnagAziendaVO aziendaVO)
      throws SolmrException;

  public Vector<CodeDescription> getElencoUfficiZonaIntermediarioByIdIntermediario(
      UtenteAbilitazioni utenteAbilitazioni) throws SolmrException;
  
  public Vector<CodeDescription> getElencoUfficiZonaIntermediarioAttiviByIdIntermediario(
      UtenteAbilitazioni utenteAbilitazioni) throws SolmrException;

  public UfficioZonaIntermediarioVO findUfficioZonaIntermediarioVOByPrimaryKey(
      Long idUfficioZonaIntermediario) throws SolmrException;

  //public Hashtable<BigDecimal, SianAllevamentiVO> leggiAllevamenti(String cuaa, SianUtenteVO sianUtenteVO) 
      //throws SolmrException;

  public Hashtable<BigDecimal, SianAllevamentiVO> leggiAllevamentiNoProfile(String cuaa)
      throws SolmrException;
  
  public ElencoRegistroDiStallaVO elencoRegistriStalla(String codiceAzienda,
      String codiceSpecie, String cuaa, String tipo, String ordinamento, SianUtenteVO sianUtenteVO)
      throws SolmrException;
  
  public ElencoRegistroDiStallaVO elencoRegistriStallaNoProfile(String codiceAzienda,
      String codiceSpecie, String cuaa, String tipo, String ordinamento)
      throws SolmrException;

  public SianTerritorioVO[] leggiTerritorio(String cuaa,
      StringBuffer annoCampagna) throws SolmrException;

  public SianTerritorioVO[] verificaCensimentoFoglio(
      SianTerritorioVO[] elencoSian) throws SolmrException;

  public Vector<SianTitoliAggregatiVO> titoliProduttoreAggregati(String cuaa,
      String campagna, SianUtenteVO sianUtenteVO) throws SolmrException;

  public SianTitoloRispostaVO titoliProduttoreConInfoPegni(String cuaa, SianUtenteVO sianUtenteVO)
      throws SolmrException;

  public Vector<SianQuoteLatteAziendaVO> quoteLatte(String cuaa, String campagna, SianUtenteVO sianUtenteVO)
      throws SolmrException;

  public SianTitoloRispostaVO titoliProduttore(String CUAA, String campagna, SianUtenteVO sianUtenteVO)
      throws SolmrException;

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
   * @return Vector
   */
  /*public Vector<UtenteProcedimento> serviceGetUtenteProcedimento(
      String codiceFiscale, Long idProcedimento, String ruolo,
      String codiceEnte, String dirittoAccesso, Long idLivello)
      throws SystemException, SolmrException;*/

  public AmmCompetenzaVO[] serviceGetListAmmCompetenzaByComuneCollegato(
      String istatComune, String tipoAmministrazione) throws SolmrException,
      SystemException;

  public AmmCompetenzaVO serviceFindAmmCompetenzaByCodiceAmm(String codiceAmm)
      throws SolmrException;

  public IntermediarioVO serviceFindIntermediarioByCodiceFiscale(
      String codiceFiscale) throws SolmrException;

  public IntermediarioVO serviceFindIntermediarioByIdIntermediario(
      Long idIntermediario) throws SolmrException;

  public IntermediarioVO serviceFindResponsabileByIdIntermediarioAndIdProcToDateRiferimento(
      Long idIntermediario, Long idProcedimento, Date dataRiferimento)
      throws SolmrException;

  public AmmCompetenzaVO[] serviceGetListAmmCompetenza() throws SolmrException;

  //public boolean serviceVerificaGerarchia(Long idUtenteConnesso,
      //Long idUtentePratica) throws SolmrException;

  public AmmCompetenzaVO[] serviceFindAmmCompetenzaByIdRange(
      String idAmmCompetenza[]) throws SolmrException;
  
  public TecnicoAmministrazioneVO[] serviceGetTecnicoAmmByIdAmmCompAndIdProcedimento(Long idAmmCompetenza,
      Long idProcedimento)
    throws SolmrException;
  
  public long[] smrcommSearchListIdEntiPrivatiByCodiceEntePrivatoRange(String[] arrCodiceEntePrivato,
      boolean flagCessazione) throws SolmrException;
  
  public DatiEntePrivatoVO[] smrcommGetEntiPrivatiByIdEntePrivatoRange(long[] idEntePrivato,
      int tipoRisultato, EntePrivatoFiltroVO filtro) throws SolmrException;

  /**
   * FINE metodi erogati da comune
   * 
   * @param idControllo
   *          Long
   * @throws SolmrException
   * @return Vector
   */
  public Vector<CodeDescription> getDocumentiByIdControllo(Long idControllo)
      throws SolmrException;

  public Vector<DocumentoVO> getDocumenti(String idDocumenti[])
      throws SolmrException;

  // Metodo per effettuare l'inserimento di una dichiarazione di correzione
  public void deleteInsertDichiarazioneCorrezione(
      String elencoIdDichiarazioneSegnalazione[], Vector<ErrAnomaliaDicConsistenzaVO> corrErr, long idUtente)
      throws SolmrException;

  // Metodo per effettuare la cancellazione di una dichiarazione di correzione
  public void deleteDichiarazioneCorrezione(
      String elencoIdDichiarazioneSegnalazione[]) throws SolmrException;

  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichiarazioneConsistenza(
      Long elencoIdDichiarazioneSegnalazione[], long idMotivoDichiarazione)
      throws SolmrException;

  public boolean isRuolosuAnagrafe(String codiceFiscale, long idAzienda)
      throws SolmrException;

  public PersonaFisicaVO getRuolosuAnagrafe(String codiceFiscale,
      long idAzienda, String codRuoloAAEP) throws SolmrException;

  public boolean getRuolosuAnagrafe(String codiceFiscale, long idAzienda)
      throws SolmrException;

  public String getIstatByDescComune(String descComune) throws SolmrException;

  public void importaSoggCollAAEP(AnagAziendaVO anagAziendaVO,
      Azienda impresaInfoc, HashMap<?,?> listaPersone, String idParametri[],
      Long idUtenteAggiornamento) throws SolmrException;

  public CodeDescription getAttivitaATECObyCode(String codiceAteco)
      throws SolmrException;

  public CodeDescription getAttivitaATECObyCodeParametroCATE(String codiceAteco)
      throws Exception;

  public void importaUteAAEP(AnagAziendaVO anagAziendaVO,
      Sede[] sedeInfocamere, String idParametri[], Long idUtenteAggiornamento)
      throws SolmrException;

  public CodeDescription[] getElencoAtecoNew(String codiceAteco, Long idAzienda)
      throws Exception;

  public it.csi.solmr.dto.profile.TipoProcedimentoVO serviceFindTipoProcedimentoByDescrizioneProcedimento(
      String descrizione) throws InvalidParameterException, SolmrException;

  public Boolean isUtenteAbilitatoProcedimento(UtenteIride2VO utenteIride2VO)
      throws InvalidParameterException, SolmrException;

  public Long writeAccessLogUser(UtenteIride2VO utenteIride2VO)
      throws InvalidParameterException, SolmrException;

  public RuoloUtenza loadRoleUser(UtenteIride2VO utenteIride2VO)
      throws InvalidParameterException, SolmrException;

  public Boolean isUtenteConRuoloSuProcedimento(String codiceFiscale,
      Long idProcedimento) throws InvalidParameterException, SolmrException;


  public MandatoVO serviceGetMandato(Long idAzienda, String codiceEnte)
      throws SystemException, InvalidParameterException,
      UnrecoverableException, SolmrException;
  
  public boolean getDelegaBySocio(String codFiscIntermediario, Long idAziendaAssociata) 
    throws SolmrException;
  
  public AziendaCollegataVO findAziendaCollegataByFatherAndSon(Long idAziendaFather, Long idAziendaSon,
      Date dataSituazione) throws SolmrException;
  
  public boolean isSoggettoAssociatoByFatherAndSon(Long idAziendaFather, String cuaaSon,
      Date dataSituazione) throws SolmrException;

  public Vector<DelegaVO> getMandatiByUtente(UtenteAbilitazioni utenteAbilitazioni, boolean forZona,
      java.util.Date dataDal, java.util.Date dataAl) throws SolmrException;

  public Vector<CodeDescription> getElencoCAAByUtente(UtenteAbilitazioni utenteAbilitazioni)
      throws SolmrException;

  public Vector<DelegaVO> getMandatiValidatiByUtente(UtenteAbilitazioni utenteAbilitazioni,
      boolean forZona, java.util.Date dataDal, java.util.Date dataAl)
      throws SolmrException;

  public IntermediarioVO getIntermediarioVOByIdUfficioZonaIntermediario(
      Long idUfficioZonaIntermediario) throws SolmrException;

  //public IntermediarioVO getIntermediarioVOByCodiceFiscale(String codice_fiscale)
      //throws SolmrException;

  public IntermediarioVO findIntermediarioVOByPrimaryKey(Long idIntermediario)
      throws SolmrException;

  public CodeDescription findRegioneByIstatProvincia(String istatProvincia)
      throws SolmrException;

  public CodeDescription findRegioneByCodiceFiscaleIntermediario(
      String codiceFiscaleIntermediario) throws SolmrException;

  public CodeDescription[] getListTipoGruppoControlloByIdDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza, String[] orderBy) throws SolmrException;

  public TipoControlloVO[] getListTipoControlloByIdDichiarazioneConsistenzaAndIdGruppoControllo(
      Long idDicharazioneConsistenza, Long idGruppoControllo, String orderBy[])
      throws SolmrException;

  public ParametroRitornoVO serviceGetAziendeAAEPAnagrafe(String codiceFiscale,
      boolean controllaPresenzaSuAAEP, Boolean bloccaAssenzaAAEP,
      boolean controllaLegameRLsuAnagrafe, boolean controllaPresenzaValidazione)
      throws SystemException, InvalidParameterException,
      UnrecoverableException, SolmrException, ServiceSystemException;

  public AnagAziendaVO[] serviceGetListAziendeByIdRange(Vector<Long> collIdAziende)
      throws SolmrException;
  
  public SianAnagTributariaGaaVO ricercaAnagrafica(String cuaa, SianUtenteVO sianUtenteVO) 
      throws SolmrException, Exception;

  public RespAnagFascicoloVO getRespAnagFascicolo(
      Long idAzienda) throws SolmrException;

  public SianFascicoloResponseVO trovaFascicolo(String cuaa)
      throws SolmrException, Exception;

  // NUOVO TERRITORIALE
  public Vector<ComuneVO> getListComuniParticelleByIdAzienda(Long idAzienda,
      boolean onlyActive, String[] orderBy) throws SolmrException;
  
  public Vector<ComuneVO> getListComuniParticelleByIdDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza, String[] orderBy)
      throws SolmrException;
  
  public Vector<TipoUtilizzoVO> getListUtilizziElencoTerrPianoLavorazione(Long idAzienda)
      throws SolmrException;
    
  public Vector<TipoUtilizzoVO> getListUtilizziElencoTerrPianoLavorazioneStor(Long idAzienda) 
      throws SolmrException;
    
  public Vector<TipoUtilizzoVO> getListUtilizziElencoTerrValidazione(Long idDichiarazioneConsistenza) 
      throws SolmrException;
  
  public Vector<TipoUtilizzoVO> getListTipiUsoSuoloByIdDichCons(long idDichiarazioneConsistenza, String[] orderBy)
      throws SolmrException;

  public java.lang.String getMaxDataEsecuzioneControlliConduzioneParticella(
      Long idAzienda) throws SolmrException;

  public java.lang.String getMaxDataEsecuzioneControlliConduzioneDichiarata(
      Long idAzienda) throws SolmrException;

  public Vector<StoricoParticellaVO> searchListParticelleByParameters(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda)
      throws SolmrException;

  public StoricoParticellaVO findStoricoParticellaVOByIdConduzioneParticella(
      Long idConduzioneParticella) throws SolmrException;
  
  public Long findIdUteByIdCondPartIdAz(Long idConduzioneParticella, Long idAzienda) throws SolmrException;

  public long getIdConduzioneDichiarata(long idConduzioneParticella,
      long idDichiarazioneConsistenza) throws SolmrException;

  public StoricoParticellaVO findStoricoParticellaVOByIdConduzioneDichiarata(
      Long idConduzioneDichiarata) throws SolmrException;

  public ConduzioneParticellaVO findConduzioneParticellaByPrimaryKey(
      Long idConduzioneParticella) throws SolmrException;

  public ConduzioneDichiarataVO findConduzioneDichiarataByPrimaryKey(
      Long idConduzioneDichiarata) throws SolmrException;

  public StoricoParticellaVO getDettaglioParticella(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idConduzione)
      throws SolmrException;

  public Vector<TipoPiantaConsociataVO> getListPianteConsociate(boolean onlyActive)
      throws SolmrException;

  public UtilizzoParticellaVO[] getListUtilizzoParticellaVOByIdConduzioneParticella(
      Long idConduzioneParticella, String[] orderBy, boolean onlyActive)
      throws SolmrException;

  public UtilizzoDichiaratoVO[] getListUtilizzoDichiaratoVOByIdDichiarazioneConsistenzaAndIdConduzioneParticella(
      Long idDichiarazioneConsistenza, Long idConduzioneParticella,
      String[] orderBy) throws SolmrException;

  public ParticellaCertificataVO findParticellaCertificataByParameters(
      String istatComune, String sezione, String foglio, String particella,
      String subalterno, boolean onlyActive,
      java.util.Date dataDichiarazioneConsistenza) throws SolmrException;

  public ParticellaCertificataVO findParticellaCertificataByParametersNewElegFit(
      String istatComune, String sezione, String foglio, String particella,
      String subalterno, boolean onlyActive,
      java.util.Date dataDichiarazioneConsistenza) throws SolmrException;
  
  public ParticellaCertificataVO findParticellaCertificataAllaDichiarazione(
      Long idParticella, ConsistenzaVO consistenzaVO) throws SolmrException;
  
  public ParticellaCertificataVO findParticellaCertificataByIdParticella(Long idParticella, 
      Date dataDichiarazioneConsistenza) throws SolmrException;
  
  public Date getDataFotoInterpretazione(long idParticellaCertificata, Date dataRichiestaRiesame)
    throws SolmrException;
  
  //public Date getDataRichiestaRiesameAlaDich(long idStoricoParticella, long idDichiarazioneConsistenza)
    //throws SolmrException;
  
  public Vector<ParticellaCertElegVO> getEleggibilitaByIdParticella(long idParticella)
    throws SolmrException;
  
  public Vector<Vector<ParticellaCertElegVO>> getListStoricoParticellaCertEleg(long idParticella)
    throws SolmrException;
  
  public HashMap<Long,Vector<SuperficieDescription>> getEleggibilitaTooltipByIdParticella(
      Vector<Long> listIdParticella)
  throws SolmrException;
  
  public Vector<ProprietaCertificataVO> getListProprietaCertifByIdParticella(long idParticella) 
      throws SolmrException;
  
  public Vector<ProprietaCertificataVO> getListDettaglioProprietaCertifByIdParticella(
      long idParticella, Date dataInserimentoValidazione) 
      throws SolmrException;

  public TipoVarietaVO[] getListTipoVarietaByIdUtilizzo(Long idUtilizzo,
      boolean onlyActive) throws SolmrException;

  public StoricoParticellaVO getDettaglioParticellaByIdConduzioneAndIdUtilizzo(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO,
      Long idConduzione, Long idUtilizzo, Long idAzienda) throws SolmrException;

  public BigDecimal getSumSuperficieUtilizzoUsoAgronomico(
      long idConduzioneParticella) throws SolmrException;
  
  public BigDecimal getSumSuperficieUtilizzoUsoAgronomicoParticella(long idParticella, long idAzienda) 
      throws SolmrException;
  
  public BigDecimal getSumSuperficieAgronomicaAltreconduzioni(long idParticella, long idConduzioneParticella, long idAzienda) 
      throws SolmrException;

  public String[] getIstatProvFromConduzione(long idAzienda)
      throws SolmrException;
  
  public BigDecimal getPercentualePosesso(long idAzienda, long idParticella)
    throws SolmrException;

  public BigDecimal getSumSuperficieFromParticellaAndLastDichCons(
      long idParticella, Long idAzienda, boolean flagEscludiAzienda)
      throws SolmrException;

  public TipoUtilizzoVO[] getListTipiUsoSuoloByIdIndirizzoUtilizzo(
      Long idIndirizzoUtilizzo, boolean onlyActive, String[] orderBy,
      String colturaSecondaria) throws SolmrException;

  public TipoUtilizzoVO[] getListTipiUsoSuoloByCodice(String codice,
      boolean onlyActive, String[] orderBy, String colturaSecondaria,
      String principale) throws SolmrException;
  
  public TipoVarietaVO[] getListTipoVarietaByIdUtilizzoAndCodice(
      Long idUtilizzo, String codiceVarieta, boolean onlyActive, 
      String[] orderBy) throws SolmrException;

  public void associaUso(long idAzienda, StoricoParticellaVO[] elencoParticelle,
      RuoloUtenza ruoloUtenza) throws SolmrException;
  
  public void associaUsoEleggibilitaGis(long idAzienda, StoricoParticellaVO[] elencoParticelle,
      RuoloUtenza ruoloUtenza) throws SolmrException;

  public void aggiornaIrrigazione(
      StoricoParticellaVO[] elencoStoricoParticella, RuoloUtenza ruoloUtenza,
      boolean flagIrrigabile, Long idIrrigazione) throws SolmrException;

  public void cambiaTitoloPossesso(Long[] elencoIdConduzioneParticella,
      RuoloUtenza ruoloUtenza, Long idAzienda, Long idTitoloPossesso)
      throws SolmrException;

  public java.util.Date getMaxDataInizioConduzioneParticella(
      Vector<Long> elencoConduzioni) throws SolmrException;

  public void cessaParticelle(Long[] elencoIdConduzioneParticella,
	  long idUtenteAggiornamento, Long idAzienda, java.util.Date dataCessazione, String provenienza)
      throws SolmrException;

  public void cambiaUte(Long[] elencoIdConduzioneParticella,
      RuoloUtenza ruoloUtenza, Long idUte, Long idAzienda)
      throws SolmrException;

  public void associaDocumento(Long[] elencoIdConduzioneParticella,
      Long idDocumento) throws SolmrException;

  public CodeDescription[] getListTipiTitoloPossesso(String orderBy)
      throws SolmrException;

  public it.csi.solmr.dto.CodeDescription[] getListTipoCasoParticolare(
      String orderBy) throws SolmrException;

  public it.csi.solmr.dto.CodeDescription[] getListTipoZonaAltimetrica(
      String orderBy) throws SolmrException;

  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaA(String orderBy)
      throws SolmrException;

  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaB(String orderBy)
      throws SolmrException;

  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaC(String orderBy)
      throws SolmrException;

  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaD(String orderBy)
      throws SolmrException;
  
  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaM(String orderBy)
      throws SolmrException;
  
  public it.csi.solmr.dto.CodeDescription[] getValidListTipoAreaA(String orderBy)
    throws SolmrException;
  
  public it.csi.solmr.dto.CodeDescription[] getValidListTipoAreaB(String orderBy)
    throws SolmrException;
  
  public it.csi.solmr.dto.CodeDescription[] getValidListTipoAreaC(String orderBy)
    throws SolmrException;
  
  public it.csi.solmr.dto.CodeDescription[] getValidListTipoAreaD(String orderBy)
    throws SolmrException;

  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaE(String orderBy)
      throws SolmrException;

  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaF(String orderBy)
      throws SolmrException;

  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaPSN(String orderBy)
      throws SolmrException;

  public it.csi.solmr.dto.CodeDescription[] getListTipoFasciaFluviale(
      String orderBy) throws SolmrException;

  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaG(String orderBy)
      throws SolmrException;

  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaH(String orderBy)
      throws SolmrException;
  
  public it.csi.solmr.dto.CodeDescription[] getValidListTipoAreaM(String orderBy)
      throws SolmrException;

  public it.csi.solmr.dto.CodeDescription[] getListTipoCausaleModParticella(
      String orderBy) throws SolmrException;

  public TipoImpiantoVO[] getListTipoImpianto(boolean onlyActive, String orderBy)
      throws SolmrException;

  public it.csi.solmr.dto.CodeDescription[] getListTipoIndirizzoUtilizzo(
      String colturaSecondaria, String orderBy, boolean onlyActive)
      throws SolmrException;

  public TipoIrrigazioneVO[] getListTipoIrrigazione(String orderBy,
      boolean onlyActive) throws SolmrException;  
  
  public TipoPotenzialitaIrriguaVO[] getListTipoPotenzialitaIrrigua(String orderBy, 
      Date dataRiferiemento) throws SolmrException;
    
  public TipoTerrazzamentoVO[] getListTipoTerrazzamento(String orderBy,
      Date dataRiferiemento) throws SolmrException;
  
  public TipoRotazioneColturaleVO[] getListTipoRotazioneColturale(String orderBy,
      Date dataRiferiemento) throws SolmrException;

  public TipoImpiantoVO findTipoImpiantoByPrimaryKey(Long idImpianto)
      throws SolmrException;

  public Vector<UtilizzoConsociatoVO> getListUtilizziConsociatiByIdUtilizzoParticella(
      Long idUtilizzoParticella, String[] orderBy) throws SolmrException;

  public Vector<TipoUtilizzoVO> findListTipiUsoSuoloByIdAzienda(Long idAzienda,
      String colturaSecondaria)  throws SolmrException;

  public Vector<TipoUtilizzoVO> findListTipiUsoSuoloByIdAziendaCess(Long idAzienda,
      String[] orderBy) throws SolmrException;

  public void modificaParticelle(StoricoParticellaVO[] elencoStoricoParticella,
      RuoloUtenza ruoloUtenza, AnagAziendaVO anagAziendaVO)
      throws SolmrException;

  public ComuneVO getComuneByParameters(String descComune,
      String siglaProvincia, String flagEstinto, String flagCatastoAttivo,
      String flagEstero) throws SolmrException;

  public Vector<ComuneVO> getComuniByParameters(String descComune, String siglaProvincia,
      String flagEstinto, String flagCatastoAttivo, String flagEstero,
      String[] orderBy) throws SolmrException;
  
  public Vector<ComuneVO> getComuniAttiviByIstatProvincia(String istatProvincia) 
      throws SolmrException;

  public Vector<AnagParticellaExcelVO> searchParticelleExcelByParameters(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda)
      throws SolmrException;

  public StoricoParticellaVO[] getListParticelleForDocument(
      StoricoParticellaVO storicoParticellaVO, AnagAziendaVO anagAziendaVO,
      boolean hasUnitToDocument, Long idAnomalia, String orderBy) throws SolmrException;

  public StoricoParticellaVO[] getListParticelleForDocument(
      StoricoParticellaVO storicoParticellaVO, AnagAziendaVO anagAziendaVO,
      long idDichiarazioneConsistenza, boolean hasUnitToDocument, Long idAnomalia, String orderBy)
      throws SolmrException;
  
  public StoricoParticellaVO[] getListParticelleForDocumentValoreC( 
      AnagAziendaVO anagAziendaVO, int anno, boolean hasUnitToDocument)
    throws SolmrException;
  
  public StoricoParticellaVO[] getListParticelleForDocumentExtraSistema( 
      AnagAziendaVO anagAziendaVO, int anno, boolean hasUnitToDocument) 
    throws SolmrException;
  
  public Vector<StoricoParticellaVO> getListParticelleUvBasic(
      FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO, AnagAziendaVO anagAziendaVO) 
    throws SolmrException;

  public StoricoParticellaVO findStoricoParticellaVOByParameters(
      String istatComune, String sezione, String foglio, String particella,
      String subalterno, java.util.Date dataInizioValidita)
      throws SolmrException;

  public Vector<TipoEventoVO> getListTipiEvento()
      throws SolmrException;

  public SezioneVO getSezioneByParameters(String istatComune, String sezione)
      throws SolmrException;

  public FoglioVO findFoglioByParameters(String istatComune, String foglio,
      String sezione) throws SolmrException;

  public FoglioVO[] getFogliByParameters(String istatComune, String sezione,
      String foglio) throws SolmrException;

  public StoricoParticellaVO[] getListStoricoParticellaVOByParameters(
      String istatComune, String sezione, String foglio, String particella,
      String subalterno, boolean onlyActive, String orderBy, Long idAzienda)
      throws SolmrException;

  public StoricoParticellaVO[] getListStoricoParticellaVOByParametersImpUnar(
      String istatComune, String sezione, String foglio, String particella,
      String subalterno, long idAzienda) throws SolmrException;

  public StoricoParticellaVO findStoricoParticellaByPrimaryKey(
      Long idStoricoParticella) throws SolmrException;

  public StoricoParticellaVO findCurrStoricoParticellaByIdParticella(
      Long idParticella) throws SolmrException;

  public String inserisciParticella(StoricoParticellaVO storicoParticellaVO,
      RuoloUtenza ruoloUtenza, StoricoParticellaVO[] elencoParticelleEvento,
      Long idEvento, Long idAzienda, String[] arrVIdStoricoUnitaArborea, String[] arrVAreaUV) throws SolmrException;

  public String inserisciParticella(StoricoParticellaVO storicoParticellaVO,
      RuoloUtenza ruoloUtenza, StoricoParticellaVO[] elencoParticelleEvento,
      Long idEvento) throws SolmrException;

  public BigDecimal getTotSupCondottaByAziendaAndParticella(
      Long idAzienda, Long idParticella)
      throws SolmrException;

  public EventoParticellaVO[] getEventiParticellaByIdParticellaNuovaOrCessata(
      Long idParticella, String[] orderBy) throws SolmrException;

  public StoricoParticellaVO[] getListParticelleForFabbricato(
      Long idFabbricato, Long idPianoRiferimento, String[] orderBy)
      throws SolmrException;

  public boolean isPianoRiferimentoRipristinato(Long idAzienda)
      throws SolmrException;

  public StoricoUnitaArboreaVO[] getListStoricoUnitaArboreaByLogicKey(
      Long idParticella, Long idPianoRiferimento, Long idAzienda,
      String[] orderBy) throws SolmrException;

  public StoricoParticellaVO[] getListStoricoUnitaArboreaByIdAzienda(
      Long idAzienda, Long idPianoRiferimento, String[] orderBy)
      throws SolmrException;

  public java.lang.String getMaxDataEsecuzioneControlliUnitaArborea(
      Long idAzienda) throws SolmrException;

  public EsitoControlloUnarVO[] getListEsitoControlloUnarByIdStoricoUnitaArborea(
      Long idStoricoUnitaArborea, String[] orderBy) throws SolmrException;

  public StoricoParticellaVO findStoricoParticellaArborea(
      Long idStoricoUnitaArborea) throws SolmrException;
  
  public StoricoParticellaVO findStoricoParticellaArboreaBasic(
      Long idStoricoUnitaArborea) throws SolmrException;
  
  public StoricoParticellaVO findStoricoParticellaArboreaConduzione(
      Long idStoricoUnitaArborea, long idAzienda) throws SolmrException;

  public StoricoParticellaVO findStoricoParticellaArboreaTolleranza(
      Long idStoricoUnitaArborea, long idAzienda, String nomeLib) throws SolmrException;

  public AltroVitignoVO[] getListAltroVitignoByIdStoricoUnitaArborea(
      Long idStoricoUnitaArborea, String[] orderBy) throws SolmrException;

  public TipoUtilizzoVO[] getListTipiUsoSuoloByTipo(String tipo,
      boolean onlyActive, String[] orderBy) throws SolmrException;

  public TipoFormaAllevamentoVO[] getListTipoFormaAllevamento(
      boolean onlyActive, String[] orderBy) throws SolmrException;

  public TipoCausaleModificaVO[] getListTipoCausaleModifica(boolean onlyActive,
      String[] orderBy) throws SolmrException;
  
  public Vector<TipoCausaleModificaVO> getListTipoCuasaleModificaByIdAzienda(long idAzienda)
    throws SolmrException;

  public void modificaUnitaArboree(
      StoricoParticellaVO[] elencoParticelleArboree, RuoloUtenza ruoloUtenza, String provenienza)
      throws SolmrException;

  public void cessaUnitaArboree(Long[] elencoIdStoricoUnitaArboree,
      RuoloUtenza ruoloUtenza, Long idCausaleModifica, String note) throws SolmrException;

  public TipoCessazioneUnarVO[] getListTipoCessazioneUnar(boolean onlyActive,
      String[] orderBy) throws SolmrException;

  public StoricoParticellaVO[] getListStoricoParticelleArboreeImportabili(
      Long idAzienda, String[] orderBy) throws SolmrException;

  public void importUnitaArboreeBySchedario(Long[] elencoIdStoricoUnitaArborea,
      RuoloUtenza ruoloUtenza, AnagAziendaVO anagAziendaVO,
      Long idCausaleModifica, Long newIdParticella) throws SolmrException;

  public void inserisciUnitaArborea(
      StoricoUnitaArboreaVO storicoUnitaArboreaVO,
      StoricoParticellaVO storicoParticellaVO, AnagAziendaVO anagAziendaVO,
      RuoloUtenza ruoloUtenza, String provenienza) throws SolmrException;

  public StoricoParticellaVO[] searchStoricoUnitaArboreaByParametersForStampa(
      Long idAzienda, FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO,
      String[] orderBy) throws SolmrException;
  
  public StoricoParticellaVO[] searchStoricoUnitaArboreaByParameters(String nomeLib,
      Long idAzienda, FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO,
      String[] orderBy) throws SolmrException;

  public Vector<StoricoParticellaArboreaExcelVO> searchStoricoUnitaArboreaExcelByParameters(
      String nomeLib, Long idAzienda, FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO) throws SolmrException;

  public ConduzioneParticellaVO[] getListConduzioneParticellaByIdAzienda(
      Long idAzienda, boolean onlyActive, String[] orderBy)
      throws SolmrException;

  public void importParticelle(String[] elencoConduzioni, Long idUte,
      AnagAziendaVO anagAziendaVO, AnagAziendaVO anagAziendaSearchVO,
      RuoloUtenza ruoloUtenza, Long idTitoloPossesso) throws SolmrException;

  public Vector<Object> importParticelleAsservite(String[] elencoConduzioni,
      Long idUte, AnagAziendaVO anagAziendaSearchVO, RuoloUtenza ruoloUtenza,
      Long idTitoloPossesso) throws SolmrException;

  public Vector<Object> importParticelleAsserviteFromRicercaParticella(
      String[] elencoIdParticelle, Long idUte, AnagAziendaVO anagAziendaVO,
      RuoloUtenza ruoloUtenza, Long idTitoloPossesso) throws SolmrException;

  public StoricoParticellaVO[] getListStoricoParticelleArboreeByParameters(
      String istatComune, String sezione, String foglio, String particella,
      String subalterno, boolean onlyActive, String[] orderBy)
      throws SolmrException;

  public StoricoParticellaVO findParticellaArboreaDichiarata(
      Long idUnitaArboreaDichiarata) throws SolmrException;
  
  public StoricoParticellaVO findParticellaArboreaDichiarataBasic(Long idUnitaArboreaDichiarata)
      throws SolmrException;

  public AltroVitignoDichiaratoVO[] getListAltroVitignoDichiaratoByIdUnitaArboreaDichiarata(
      Long idUnitaArboreaDichiarata, String[] orderBy) throws SolmrException;

  public void ribaltaUVOnPianoColturale(Long idAzienda, BigDecimal[] idStoricoUnitaArborea, Long idUtente)
      throws SolmrException;

  public TipoVinoVO[] getListTipoVino(boolean onlyActive, String[] orderBy)
      throws SolmrException;

  public TipoTipologiaVinoVO[] getListTipoTipologiaVino(boolean onlyActive,
      String[] orderBy) throws SolmrException;

  public TipoTipologiaVinoVO[] getListTipoTipologiaVinoForAzienda(long idAzienda)
      throws SolmrException;
  
  public TipoTipologiaVinoVO[] getListTipoTipologiaVinoForDichCons(long idDichiarazioneConsistenza)
      throws SolmrException;

  public TipoUtilizzoVO findTipoUtilizzoByPrimaryKey(Long idUtilizzo)
      throws SolmrException;
  
  public TipoUtilizzoVO[] getListTipoUtilizzoByIdAzienda(String tipo, long idAzienda)
      throws SolmrException;
  
  public TipoUtilizzoVO[] getListTipoUtilizzoByIdDichiarazioneConsistenza(String tipo, long idDichiarazioneConsistenza)
      throws SolmrException;

  public TipoVarietaVO findTipoVarietaByPrimaryKey(Long idVarieta)
      throws SolmrException;
  
  public TipoVarietaVO[] getListTipoVarietaByIdAzienda(long idAzienda)
      throws SolmrException;
  
  public TipoVarietaVO[] getListTipoVarietaByIdDichiarazioneConsistenza(long idDichiarazioneConsistenza)
      throws SolmrException;

  public TipoTipologiaVinoVO[] getListTipoTipologiaVinoByIdVino(Long idVino,
      boolean onlyActive, String[] orderBy) throws SolmrException;

  public Vector<TipoTipologiaVinoVO> getListActiveTipoTipologiaVinoByComune(
      String istatComune) throws SolmrException;
  
  public Vector<TipoTipologiaVinoVO> getListActiveTipoTipologiaVinoByComuneAndVarieta(
      String istatComune, Long idVarieta, Long idParticella) throws SolmrException;
  
  //public Vector<VignaVO> getListActiveVignaByIdTipologiaVinoAndParticella(long idTipologiaVino, long idParticella)
      //throws SolmrException;
  
  public Vector<TipoTipologiaVinoVO> getListTipoTipologiaVinoRicaduta(
      String istatComune, long idVarieta, long idTipologiaVino, java.util.Date dataInserimentoDichiarazione) throws SolmrException;
  
  public TipoTipologiaVinoVO getTipoTipologiaVinoByPrimaryKey(long idTipologiaVino) 
    throws SolmrException;

  public void dichiaraUsoAgronomico(StoricoParticellaVO[] elencoParticelle,
      RuoloUtenza ruoloUtenza, Long idAzienda) throws SolmrException;

  public void validaUVPlSql(Long[] elencoIdUnitaArboreaDichiarata)
      throws SolmrException;

  public StoricoParticellaVO[] riepilogoTitoloPossesso(Long idAzienda) throws SolmrException;

  public StoricoParticellaVO[] riepilogoTitoloPossessoDichiarato(
      Long idAzienda, Long idDichiarazioneConsistenza)
      throws SolmrException;

  public StoricoParticellaVO[] riepilogoPossessoComune(Long idAzienda) throws SolmrException;

  public StoricoParticellaVO[] riepilogoPossessoComuneDichiarato(
      Long idAzienda, Long idDichiarazioneConsistenza)
      throws SolmrException;

  public StoricoParticellaVO[] riepilogoComune(Long idAzienda,
      String escludiAsservimento)
      throws SolmrException;

  public StoricoParticellaVO[] riepilogoComuneDichiarato(Long idAzienda,
      String escludiAsservimento, Long idDichiarazioneConsistenza) throws SolmrException;

  public UtilizzoParticellaVO[] riepilogoUsoPrimario(Long idAzienda)
      throws SolmrException;
  
  public BigDecimal getTotSupSfriguAndAsservimento(Long idAzienda, String escludiAsservimento)
    throws SolmrException;

  public UtilizzoDichiaratoVO[] riepilogoUsoPrimarioDichiarato(Long idAzienda,
      Long idDichiarazioneConsistenza) throws SolmrException;

  public UtilizzoParticellaVO[] riepilogoUsoSecondario(Long idAzienda)
      throws SolmrException;

  public UtilizzoDichiaratoVO[] riepilogoUsoSecondarioDichiarato(
      Long idAzienda, Long idDichiarazioneConsistenza) throws SolmrException;

  public UtilizzoParticellaVO[] riepilogoMacroUso(Long idAzienda)
      throws SolmrException;

  public UtilizzoDichiaratoVO[] riepilogoMacroUsoDichiarato(Long idAzienda,
      Long idDichiarazioneConsistenza) throws SolmrException;
  
  public BigDecimal getTotSupAsservimento(Long idAzienda, Long idDichiarazioneConsistenza)
    throws SolmrException;

  public TipoMacroUsoVO[] getListTipoMacroUsoByIdAzienda(Long idAzienda,
      boolean onlyActive, String[] orderBy) throws SolmrException;

  public StoricoParticellaVO[] riepilogoZVNParticellePiemonte(Long idAzienda,
      String escludiAsservimento)
      throws SolmrException;

  public StoricoParticellaVO[] riepilogoZVNParticelleDichiaratePiemonte(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws SolmrException;

  public StoricoParticellaVO[] riepilogoZVNFasciaFluviale(Long idAzienda,
      String escludiAsservimento) throws SolmrException;

  public StoricoParticellaVO[] riepilogoZVNFasciaFluvialeDichiarate(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws SolmrException;

  public StoricoParticellaVO[] riepilogoZVFParticellePiemonte(Long idAzienda,
      String escludiAsservimento)
      throws SolmrException;

  public StoricoParticellaVO[] riepilogoZVFParticelleDichiaratePiemonte(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws SolmrException;

  public StoricoParticellaVO[] riepilogoLocalizzazioneParticellePiemonte(
      Long idAzienda, String escludiAsservimento) throws SolmrException;

  public StoricoParticellaVO[] riepilogoLocalizzazioneParticelleDichiaratePiemonte(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws SolmrException;

  public StoricoParticellaVO[] riepilogoFasciaFluvialeParticelle(
      Long idAzienda, String escludiAsservimento) throws SolmrException;

  public StoricoParticellaVO[] riepilogoFasciaFluvialeParticelleDichiarate(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws SolmrException;

  public StoricoParticellaVO[] riepilogoAreaG(Long idAzienda,
      String escludiAsservimento) throws SolmrException;

  public StoricoParticellaVO[] riepilogoAreaGParticelleDichiarate(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws SolmrException;

  public StoricoParticellaVO[] riepilogoAreaH(Long idAzienda,
      String escludiAsservimento) throws SolmrException;

  public StoricoParticellaVO[] riepilogoAreaHParticelleDichiarate(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws SolmrException;

  public StoricoParticellaVO[] riepilogoZonaAltimetrica(Long idAzienda,
      String escludiAsservimento) throws SolmrException;

  public StoricoParticellaVO[] riepilogoZonaAltimetricaParticelleDichiarate(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws SolmrException;

  public StoricoParticellaVO[] riepilogoCasoParticolare(Long idAzienda,
      String escludiAsservimento) throws SolmrException;

  public StoricoParticellaVO[] riepilogoCasoParticolareParticelleDichiarate(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws SolmrException;

  public Vector<TipoVarietaVO> getListTipoVarietaVitignoByMatriceAndComune(long idUtilizzo, 
      long idTipoDestinazione, long idTipoDettaglioUso, long idTipoQualitaUso, 
      String istatComune) throws SolmrException;

  public java.lang.String getTotSupCondottaByAzienda(Long idAzienda,
      boolean onlyActive) throws SolmrException;

  public StoricoParticellaVO[] riepilogoTipoArea(Long idAzienda,
      String escludiAsservimento, String tipoArea) throws SolmrException;

  public StoricoParticellaVO[] riepilogoTipoAreaDichiarato(Long idAzienda,
      String escludiAsservimento, Long idDichiarazioneConsistenza,
      String tipoArea) throws SolmrException;

  public UnitaArboreaDichiarataVO[] getListUnitaArboreaDichiarataByPianoRiferimentoAndIdStoricoUnitaArborea(
      Long idStoricoParticella, Long idAzienda, Long idPianoRiferimento,
      String[] orderBy) throws SolmrException;
  
  public long getIdUnitaArboreaDichiarata(long idStoricoUnitaArborea, long idDichiarazioneConsistenza)
      throws SolmrException;
  
  
  public Long getDefaultIdGenereIscrizione() throws SolmrException;
  
  public Vector<TipoGenereIscrizioneVO> getListTipoGenereIscrizione() throws SolmrException;
  
  public BigDecimal getSupEleggPlSqlTotale(long idParticellaCertificata, long idCatalogoMatrice)
    throws SolmrException;

  public BigDecimal getSupEleggNettaPlSqlTotale(long idParticellaCertificata, long idCatalogoMatrice)
    throws SolmrException;
  
  public Vector<TipoMenzioneGeograficaVO> getListTipoMenzioneGeografica(long idParticella, 
      Long idTipologiaVino, java.util.Date dataInserimentoDichiarazione)
    throws SolmrException;
  
  public Vector<StoricoParticellaVO> getListParticelleByIdDocumento(Long idDocumento, String[] orderBy)
      throws SolmrException;
  
  public void cambiaPercentualePossesso(Long[] elencoIdConduzioneParticella, Vector<Long> vIdParticella,
      RuoloUtenza ruoloUtenza, Long idAzienda, BigDecimal percentualePossesso) throws SolmrException;
  
  public void cambiaPercentualePossessoSupUtilizzata(Vector<Long> vIdConduzioni,
      RuoloUtenza ruoloUtenza, Long idAzienda)  throws SolmrException;

  public long[] ricercaIdParticelleTerreni(
      FiltriRicercaTerrenoVO filtriRicercaTerrenoVO) throws SolmrException;

  public long[] ricercaIdConduzioneTerreniImportaAsservimento(
      FiltriRicercaTerrenoImportaAsservimentoVO filtriRicercaTerrenoVO)
      throws SolmrException;

  public RigaRicercaTerreniVO[] getRigheRicercaTerreniByIdParticellaRange(
      long ids[]) throws SolmrException;

  public RigaRicercaTerreniImportaAsservimentoVO[] getRigheRicercaTerreniImportaAsservimentoByIdConduzioneRange(
      long ids[]) throws SolmrException;

  public RigaRicercaTerreniImportaAsservimentoVO[] getRigheRicercaTerreniImportaAsservimentoByIdParticellaRange(
      long ids[]) throws SolmrException;

  public Vector<String> findProvinciaStoricoParticellaArborea(
      long[] idStoricoUnitaArborea) throws SolmrException;
  
  public Vector<String> findProvinciaStoricoParticellaArboreaIsolaParcella(long idAzienda, long[] idIsolaParcella)
      throws SolmrException;
  
  public BigDecimal getSumAreaUVParticella(long idAzienda, long idParticella) 
    throws SolmrException;
  
  public int getNumUVParticella(long idAzienda, long idParticella)
    throws SolmrException;

  public Vector<String> findProvinciaParticellaArboreaDichiarata(
      long[] idUnitaArboreaDichiarata) throws SolmrException;
  
  public StoricoUnitaArboreaVO findStoricoUnitaArborea(Long idStoricoUnitaArborea) throws SolmrException;

  // FINE NUOVO TERRITORIALE

  // NUOVO FABBRICATO
  public FabbricatoParticellaVO[] getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella(
      Long idConduzioneParticella, Long idAzienda, String[] orderBy,
      boolean onlyActive) throws SolmrException;

  public FabbricatoVO[] getListFabbricatiAziendaByPianoRifererimento(
      Long idAzienda, Long idPianoRiferimento, Long idUte, String[] orderBy)
      throws SolmrException;

  // FINE NUOVO FABBRICATO

  // NUOVA GESTIONE UTE
  public Vector<UteVO> getListUteByIdAzienda(Long idAzienda,
      boolean onlyActive, String[] orderBy) throws SolmrException;

  public Vector<UteVO> getListUteByIdAziendaAndIdPianoRiferimento(
      Long idAzienda, long idPianoRiferimento) throws SolmrException;

  public UteVO findUteByPrimaryKey(Long idUte) throws SolmrException;

  // FINE NUOVA GESTIONE UTE

  // INIZIO GESTIONE DOCUMENTI
  public Vector<TipoStatoDocumentoVO> getListTipoStatoDocumento(boolean isActive)
      throws SolmrException;

  public Vector<DocumentoVO> searchDocumentiByParameters(DocumentoVO documentoVO,
      String protocollazione, String[] orderBy) throws SolmrException;

  public TipoDocumentoVO[] getListTipoDocumentoByIdTipologiaDocumento(
      Long idTipologiaDocumento, boolean isActive) throws SolmrException;

  public TipoDocumentoVO findTipoDocumentoVOByPrimaryKey(Long idDocumento)
      throws SolmrException;

  public DocumentoVO findDocumentoVOBydDatiAnagrafici(
      DocumentoVO documentoFiltroVO) throws SolmrException;

  public Long inserisciDocumento(DocumentoVO documentoVO,
      RuoloUtenza ruoloUtenza, String anno, Vector<DocumentoProprietarioVO> elencoProprietari,
      StoricoParticellaVO[] elencoParticelle, Vector<ParticellaAssVO> particelleAssociate)
      throws SolmrException;

  public DocumentoVO findDocumentoVOByPrimaryKey(Long idDocumento)
      throws SolmrException;

  public DocumentoVO getDettaglioDocumento(Long idDocumento,
      boolean legamiAttivi) throws SolmrException;

  public DocumentoVO getDettaglioDocumento(Long idDocumento,
      java.util.Date dataConsistenza, Long idDichiarazioneConsistenza)
      throws SolmrException;

  public Long aggiornaDocumento(DocumentoVO documentoVO,
      RuoloUtenza ruoloUtenza, String operazioneRichiesta)
      throws SolmrException;
  
  public Long aggiornaDocumentoIstanzaLimitato(DocumentoVO documentoVO,
      RuoloUtenza ruoloUtenza, String operazioneRichiesta) throws SolmrException;

  public void deleteDocumenti(String[] documentiDaEliminare, String note,
      long idUtenteAggiornamento) throws SolmrException;

  public void protocollaDocumenti(String[] documentiDaProtocollare, Long idAzienda,
      RuoloUtenza ruoloUtenza) throws SolmrException;

  public Vector<DocumentoVO> getListDettaglioDocumentoByIdConduzione(Long idConduzione,
      boolean isStorico, boolean altreParticelle) throws SolmrException;
  
  public Vector<DocumentoVO> getListDettaglioDocumentoByIdConduzionePopUp(Long idConduzioneParticella)
      throws SolmrException;
  
  public Vector<DocumentoVO> getListDettaglioDocumentoByIdConduzioneDichiarataPopUp(Long idConduzioneDichiarata) 
      throws SolmrException;

  public DocumentoVO[] getListDocumentiByIdAzienda(Long idAzienda,
      boolean onlyActive, String[] orderBy, Long idTipologiaDocumento)
      throws SolmrException;

  public TipoCategoriaDocumentoVO[] getListTipoCategoriaDocumentoByIdTipologiaDocumento(
      Long idTipologiaDocumento, String orderBy[]) throws SolmrException;

  public TipoDocumentoVO[] getListTipoDocumentoByIdCategoriaDocumento(
      Long idCategoriaDocumento, String orderBy[], boolean onlyActive,
      Boolean cessata) throws SolmrException;

  public TipoCategoriaDocumentoVO findTipoCategoriaDocumentoByPrimaryKey(
      Long idCategoriaDocumento) throws SolmrException;

  public TipoDocumentoVO[] getListTipoDocumentoByIdCategoriaDocumentoAndIdAzienda(
      Long idCategoriaDocumento, Long idAzienda, String orderBy[])
      throws SolmrException;

  public String getDataMaxEsecuzioneDocumento(Long idAzienda)
      throws SolmrException;

  public String[] getCuaaProprietariDocumentiAzienda(Long idAzienda,
      String cuaa, boolean onlyActive) throws SolmrException;

  public EsitoControlloDocumentoVO[] getListEsitoControlloDocumentoByIdDocumento(
      Long idDocumento, String[] orderBy) throws SolmrException;

  public DocumentoVO[] getListDocumentiAziendaByIdControllo(
      AnagAziendaVO anagAziendaVO, Long idControllo, boolean onlyActive,
      String[] orderBy) throws SolmrException;

  public TipoDocumentoVO[] getListTipoDocumentoByIdControllo(Long idControllo,
      boolean onlyActive, String orderBy[]) throws SolmrException;

  public DocumentoVO[] getListDocumentiAziendaByIdControlloAndParticella(
      AnagAziendaVO anagAziendaVO, Long idControllo, Long idStoricoParticella,
      boolean onlyActive, String[] orderBy) throws SolmrException;

  public DocumentoVO[] getListDocumentiPerDichCorrettiveAziendaByIdControlloAndParticella(
      AnagAziendaVO anagAziendaVO, Long idControllo, Long idStoricoParticella,
      String annoCampagna, String[] orderBy) throws SolmrException;

  public DocumentoVO[] getListDocumentiPerDichCorrettiveAziendaByIdControllo(
      AnagAziendaVO anagAziendaVO, Long idControllo, String annoCampagna,
      String[] orderBy) throws SolmrException;
  
  public Vector<DocumentoConduzioneVO> getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella(
      Long idDocumento, Long idConduzioneParticella, boolean onlyActive) throws SolmrException;
  
  public Vector<StoricoParticellaVO> getListParticelleByIdDocumentoAlPianoCorrente(
      Long idDocumento) throws SolmrException;

  // FINE GESTIONE DOCUMENTI

  // INIZIO ANAGRAFICA
  public TipoCessazioneVO[] getListTipoCessazione(String orderBy,
      boolean onlyActive) throws SolmrException;

  // FINE ANAGRAFICA

  // INIZIO ATTESTAZIONI
  public TipoAttestazioneVO[] getListTipoAttestazioneOfPianoRiferimento(
      Long idAzienda, String codiceFotografiaTerreni,
      java.util.Date dataAnnoCampagna, java.util.Date dataVariazione, String[] orderBy, 
      String voceMenu) throws SolmrException;

  public boolean getDataAttestazioneAllaDichiarazione(
      String codiceFotografiaTerreni,
      java.util.Date dataInserimentoDichiarazione, boolean flagVideo,
      boolean flagStampa, String codiceAttestazione, String voceMenu)
      throws SolmrException;

  public boolean isAttestazioneDichiarata(String codiceFotografiaTerreni)
      throws SolmrException;
  
  public boolean isAttestazioneAzienda(long idAzienda)
      throws SolmrException;

  public void aggiornaAttestazioniPlSql(Long idAzienda, Long idUtente,
      Long idDichiarazioneConsistenza, String codAttestazione) throws SolmrException;

  public void aggiornaAttestazioni(String[] elencoIdAttestazioni,
      AnagAziendaVO anagAziendaVO, RuoloUtenza ruoloUtenza,
      Hashtable<?,?> elencoParametri, String voceMenu, ConsistenzaVO consistenzaVO)
      throws SolmrException;
  
  public void aggiornaElencoAllegatiAttestazioni(String[] elencoIdAttestazioni, 
      RuoloUtenza ruoloUtenza, Hashtable<Long,ParametriAttDichiarataVO> elencoParametri, 
      TipoAllegatoVO tipoAllegatoVO, ConsistenzaVO consistenzaVO) throws SolmrException; 

  public TipoAttestazioneVO[] getListTipoAttestazioneForUpdate(Long idAzienda,
      String[] orderBy, String voceMenu) throws SolmrException;

  public TipoParametriAttestazioneVO findTipoParametriAttestazioneByIdAttestazione(
      Long idAttestazione) throws SolmrException;

  public TipoAttestazioneVO[] getElencoTipoAttestazioneAlPianoAttuale(
      boolean flagVideo, boolean flagStampa, String[] orderBy,
      String codiceAttestazione, String voceMenu) throws SolmrException;

  public TipoAttestazioneVO[] getTipoAttestazioneAllegatiAllaDichiarazione(
      String codiceFotografiaTerreni, java.util.Date dataAnnoCampagna, 
      java.util.Date dataVariazione, String[] orderBy) throws SolmrException;
  
  public Vector<TipoAttestazioneVO> getElencoAttestazioniAllaDichiarazione(
      String codiceFotografiaTerreni, Date dataAnnoCampagna, 
      String codiceAttestazione)  throws SolmrException; 
  
  public AttestazioneAziendaVO getFirstAttestazioneAzienda(long idAzienda) throws SolmrException;
  
  public AttestazioneDichiarataVO getFirstAttestazioneDichiarata(long codiceFotografia) throws SolmrException;
  
  public Vector<java.util.Date> getDateVariazioniAllegati(long codiceFotografia) throws SolmrException;

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
      Exception;

  public HSSFWorkbook getRisultatoQuery(TipologiaReportVO tipologiaReportVO,
      HashMap<?,?> parametriFissiHtmpl, HSSFWorkbook workBook, String nomeFoglio)
      throws Exception, Exception;

  /**
   * Elenco variabili e valori associate alla query selezionata
   * 
   * @param idTipologia
   * @return
   * @throws Exception
   */
  public HashMap<?,?> getQueryPopolamento(String queryPopolamento,
      String idRptVariabileReport) throws Exception, Exception;

  // Gestione Utiliti //

  public Vector<AnagAziendaVO> getAziendaByIntermediarioAndCuaa(
      Long intermediario, String cuaa) throws SolmrException;

  public Vector<AnagAziendaVO> getAziendeByListOfId(Vector<Long> vIdAzienda)
      throws SolmrException;

  public void storicizzaDelegaBlocco(RuoloUtenza ruoloUtenza,
      Vector<AnagAziendaVO> vAnagAziendaVO, String oldIntermediario,
      String newIntermediario) throws SolmrException;

  /**
   * Servizi che accedono ai WS CCIAA START
   */
  public UvResponseCCIAA elencoUnitaVitatePerCUAA(String cuaa, Long idAzienda, RuoloUtenza ruolo)
      throws SolmrException;

  public UvResponseCCIAA elencoUnitaVitatePerParticella(String istat,
      String sezione, String foglio, String particella, Long idAzienda, RuoloUtenza ruolo) throws SolmrException;

  public void sianAggiornaDatiAlboVigneti(AnagAziendaVO anagAziendaVO)
      throws SolmrException;

  /**
   * Servizi che accedono ai WS CCIAA STOP
   */
  
  public StringcodeDescription getDescTipoIscrizioneInpsByCodice(String codiceTipoIscrizioneInps)
      throws SolmrException;

  public Long getAnnoDichiarazione(Long idDichiarazioneConsistenza)
      throws SolmrException;

  public Long getProcedimento(Long idAzienda, Long idDichiarazioneConsistenza)
      throws SolmrException;

  public long getLastIdDichiazioneConsistenza(Long idAzienda, Long anno)
      throws SolmrException;

  public ParticellaDettaglioValidazioniVO[] getParticellaDettaglioValidazioni(
      long idParticella, Long anno, int tipoOrdinamento,
      boolean ordineAscendente[]) throws SolmrException;

  public long[] getElencoAnniDichiarazioniConsistenzaByIdParticella(
      long idParticella) throws SolmrException;

  public boolean areParticelleNonCessateByIdParticelle(long idParticelle[])
      throws SolmrException;

  public StoricoParticellaVO findStoricoParticellaVOByIdParticella(
      long idParticella) throws SolmrException;

  public SianAnagTributariaVO selectDatiAziendaTributaria(String cuaa)
      throws SolmrException;

  // Richiamo una funzione PL-SQL che mi restituisce l'elenco dei CUAA collegati
  public String[] getCUAACollegati(String cuaa, SianUtenteVO sianUtenteVO) throws SolmrException;

  public Vector<Long> getIdAnagAziendeCollegatebyCUAA(String cuaa)
      throws SolmrException;

  public ParticellaAssVO[] getParticellaForDocAzCessata(Long idParticella)
      throws SolmrException;

  public Vector<ParticellaAssVO> getParticelleDocCor(Long idDocumento) throws SolmrException;

  // ************* SITI
  public String serviceParticellaUrl3D(String istatComune, String sezione,
      String foglio, String particella, String subalterno)
      throws InvalidParameterException, Exception, UnrecoverableException;

  // ************* SITI

  /*
   * Servizi di vitiserv Begin
   */
  public DirittoGaaVO[] getDiritti(long idAzienda, boolean flagAttivi,
      int tipoOrdinamento, int tipoRisultato) throws SolmrException;

  /*
   * Servizi di vitiserv End
   */

  /*****************************************************************************
   * ********* Servizi di SigopServ BEGIN **********************************
   ****************************************************************************/
  
  public SchedaCreditoVO[] sigopservVisualizzaDebiti(String cuaa)
      throws SolmrException;
  
  public PagamentiErogatiVO sigopservEstraiPagamentiErogati(String cuaa, String settore,
      Integer anno) throws SolmrException;
   
  public RecuperiPregressiVO sigopservEstraiRecuperiPregressi(String cuaa, String settore,
      Integer anno) throws SolmrException;

  /*****************************************************************************
   * ********* Servizi di SigopServ END **********************************
   ****************************************************************************/
  
  
  
  /***************************** COMMON BEGIN ***************************
   ******************************************************************* 
   ******************************************************************/
  
  public Object getValoreParametroAltriDati(String codiceParametro)
    throws SolmrException;
  
  /***************************** COMMON END ***************************
   ******************************************************************* 
   ******************************************************************/
  
  
  /***************************** WS COMUNE START ***************************
   ******************************************************************* 
   ******************************************************************/
  
  public String serviceInviaPostaCertificata(InvioPostaCertificata invioPosta) 
      throws SolmrException;
  
  /***************************** WS COMUNE END ***************************
   ******************************************************************* 
   ******************************************************************/
  
  
  /***************************** WS PAPUA START ***************************
   ******************************************************************* 
   ******************************************************************/
  
  public it.csi.papua.papuaserv.presentation.ws.profilazione.axis.Ruolo[] findRuoliForPersonaInApplicazione(String codiceFiscale, int livelloAutenticazione)
      throws SolmrException;
  
  public UtenteAbilitazioni loginPapua(String codiceFiscale, String cognome, 
      String nome, int livelloAutenticazione, String codiceRuolo)
      throws SolmrException;
  
  public MacroCU[] findMacroCUForAttoreInApplication(String codiceAttore)
      throws SolmrException;
  
  public boolean verificaGerarchia(long idUtente1, long idUtente2)
      throws SolmrException;
  
  public UtenteAbilitazioni getUtenteAbilitazioniByIdUtenteLogin(long idUtente)
      throws SolmrException;
  
  public UtenteAbilitazioni[] getUtenteAbilitazioniByIdUtenteLoginRange(long[] idUtente)
      throws SolmrException;
  
  /***************************** WS PAPUA END ***************************
   ******************************************************************* 
   ******************************************************************/
  
  

  // Usato dall'utente di monitoraggio
  public String testDB() throws SolmrException;

}