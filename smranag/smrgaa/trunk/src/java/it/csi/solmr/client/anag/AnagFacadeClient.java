package it.csi.solmr.client.anag;

/**
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
 * @author EF
 * @version 1.0
 */

// Import per fare lookup senza passare da DD
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
import it.csi.solmr.client.DynamicDelegate;
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
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.SolmrErrors;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
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

public class AnagFacadeClient implements
    it.csi.smrcomms.reportdin.client.IReportDinUserClient
{
  IAnagClient iac;

  public AnagFacadeClient()
  {
    iac = (IAnagClient) DynamicDelegate
      .newInstance(SolmrConstants.I_ANAG_FACADE);

  }

  public CodeDescription[] getCodeDescriptionsNew(String tableName,
      String filtro, String valFiltro, String orderBy) throws SolmrException
  {
    return iac.getCodeDescriptionsNew(tableName, filtro, valFiltro, orderBy);
  }

  public it.csi.solmr.dto.profile.CodeDescription getGruppoRuolo(String ruolo)
      throws SolmrException
  {
    return iac.getGruppoRuolo(ruolo);
  }

  public Vector<AnagAziendaVO> getAziendaCUAA(String CUAA)
      throws SolmrException
  {
    return iac.getAziendaCUAA(CUAA);
  }

  public Vector<AnagAziendaVO> getAziendaByCriterioCessataAndProvvisoria(
      String CUAA) throws SolmrException
  {
    return iac.getAziendaByCriterioCessataAndProvvisoria(CUAA);
  }

  public AnagAziendaVO getAziendaCUAA(String CUAA, java.util.Date data)
      throws SolmrException
  {
    return iac.getAziendaCUAA(CUAA, data);
  }

  public AnagAziendaVO getAziendaPartitaIVA(String partitaIVA,
      java.util.Date data) throws SolmrException
  {
    return iac.getAziendaPartitaIVA(partitaIVA, data);
  }

  public Vector<Long> getListIdAziende(AnagAziendaVO aziendaVO,
      java.util.Date data, boolean attivitaBool) throws SolmrException
  {
    Vector<Long> result = new Vector<Long>();
    try
    {
      result = iac.getListIdAziende(aziendaVO, data, attivitaBool);
    }
    catch (Exception e)
    {
      if (e != null && e.toString().indexOf("TimedOutException") != -1)
        throw new SolmrException(SolmrErrors.EXC_TROPPI_RECORD_SELEZIONATI);
      else
        throw new SolmrException(e.getMessage());
    }
    return result;
  }

  public Vector<Long> getListIdAziendeFlagProvvisorio(AnagAziendaVO aaVO,
      Date dataSituazioneAl, boolean attivitaBool, boolean provvisorio)
      throws SolmrException
  {
    Vector<Long> result = new Vector<Long>();
    try
    {
      result = iac.getListIdAziendeFlagProvvisorio(aaVO, dataSituazioneAl,
          attivitaBool, provvisorio);
    }
    catch (Exception e)
    {
      if (e != null && e.toString().indexOf("TimedOutException") != -1)
        throw new SolmrException(SolmrErrors.EXC_TROPPI_RECORD_SELEZIONATI);
      else
        throw new SolmrException(e.getMessage());
    }
    return result;
  }

  public Vector<Long> getListOfIdAzienda(AnagAziendaVO aziendaVO,
      java.util.Date data, boolean attivitaBool) throws SolmrException
  {
    Vector<Long> result = new Vector<Long>();
    try
    {
      result = iac.getListOfIdAzienda(aziendaVO, data, attivitaBool);
    }
    catch (Exception e)
    {
      if (e != null && e.toString().indexOf("TimedOutException") != -1)
        throw new SolmrException(SolmrErrors.EXC_TROPPI_RECORD_SELEZIONATI);
      else
        throw new SolmrException(e.getMessage());
    }
    return result;
  }

  public Vector<AnagAziendaVO> getListAziendeByIdRange(Vector<Long> collAzienda)
      throws SolmrException
  {
    return iac.getListAziendeByIdRange(collAzienda);
  }

  public Vector<AnagAziendaVO> getAziendeByIdAziendaRange(
      Vector<Long> collAzienda) throws SolmrException
  {
    return iac.getAziendeByIdAziendaRange(collAzienda);
  }

  public Vector<AnagAziendaVO> getListAziendeByIdRangeFromIdAzienda(
      Vector<Long> idAzienda) throws SolmrException
  {
    return iac.getListAziendeByIdRangeFromIdAzienda(idAzienda);
  }

  public void storicizzaAziendeCollegateBlocco(RuoloUtenza ruoloUtenza,
      Vector<AziendaCollegataVO> vAnagAziendaCollegateVO) throws SolmrException
  {
    iac.storicizzaAziendeCollegateBlocco(ruoloUtenza, vAnagAziendaCollegateVO);
  }

  public void eliminaAziendeCollegateBlocco(long idUtenteAggiornamento,
      Long idAziendaFather, Vector<Long> vIdAziendaVO) throws SolmrException
  {
    iac.eliminaAziendeCollegateBlocco(idUtenteAggiornamento, idAziendaFather, vIdAziendaVO);
  }

  public PersonaFisicaVO getTitolareORappresentanteLegaleAzienda(
      Long idAzienda, java.util.Date data) throws SolmrException
  {
    return iac.getTitolareORappresentanteLegaleAzienda(idAzienda, data);
  }

  public AnagAziendaVO getAziendaById(Long idAnagraficaAzienda)
      throws SolmrException
  {
    return iac.getAziendaById(idAnagraficaAzienda);
  }

  public AnagAziendaVO getAziendaByIdAzienda(Long idAzienda)
      throws SolmrException
  {
    return iac.getAziendaByIdAzienda(idAzienda);
  }
  
  public Vector<AnagAziendaVO> getAssociazioniCollegateByIdAzienda(
      Long idAzienda, Date dataFineValidita) throws SolmrException
  {
    return iac.getAssociazioniCollegateByIdAzienda(idAzienda, dataFineValidita);
  }

  public Vector<UteVO> getUTE(Long idAzienda, Boolean storico)
      throws SolmrException
  {
    return iac.getUTE(idAzienda, storico);
  }

  public UteVO getUteById(Long idUte) throws SolmrException
  {
    return iac.getUteById(idUte);
  }

  public Date getMinDataInizioConduzione(Long idUte) throws Exception
  {
    return iac.getMinDataInizioConduzione(idUte);
  }

  public Date getMinDataInizioAllevamento(Long idUte) throws Exception
  {
    return iac.getMinDataInizioAllevamento(idUte);
  }

  public Date getMinDataInizioFabbricati(Long idUte) throws Exception
  {
    return iac.getMinDataInizioFabbricati(idUte);
  }

  public Vector<CodeDescription> getTipiAttivita() throws SolmrException
  {
    return iac.getTipiAttivita();
  }

  public Vector<CodeDescription> getTipiAzienda() throws SolmrException
  {
    return iac.getTipiAzienda();
  }

  public Vector<CodeDescription> getTipiFabbricato() throws SolmrException
  {
    return iac.getTipiFabbricato();
  }

  public Vector<CodeDescription> getTipiFormaGiuridica(Long idTipologiaAzienda)
      throws SolmrException
  {
    return iac.getTipiFormaGiuridica(idTipologiaAzienda);
  }
  
  public Vector<CodeDescription> getCodeDescriptionsFormaGiuridica() 
      throws SolmrException
  {
    return iac.getCodeDescriptionsFormaGiuridica();
  }

  public String insediamentoAtomico(AnagAziendaVO modAnagAziendaVO,
      PersonaFisicaVO modPersonaVO, HttpServletRequest request,
      AnagAziendaVO anagAziendaVO, SianUtenteVO sianUtenteVO, RuoloUtenza ruoloUtenza)
      throws SolmrException
  {
    return iac.insediamentoAtomico(modAnagAziendaVO, modPersonaVO, request,
        anagAziendaVO, sianUtenteVO, ruoloUtenza);
  }

  public TipoTipologiaAziendaVO getTipologiaAzienda(Long idTipologiaAzienda)
      throws SolmrException
  {
    return iac.getTipologiaAzienda(idTipologiaAzienda);
  }

  public Vector<CodeDescription> getTipiMotivoDichiarazione()
      throws SolmrException
  {
    return iac.getTipiMotivoDichiarazione();
  }

  public Vector<TipoTipologiaAziendaVO> getTipiTipologiaAzienda(
      Boolean flagControlliUnivocita, Boolean flagAziendaProvvisoria)
      throws SolmrException
  {
    return iac.getTipiTipologiaAzienda(flagControlliUnivocita,
        flagAziendaProvvisoria);
  }

  public Vector<CodeDescription> getTipiFormaGiuridica() throws SolmrException
  {
    return iac.getTipiFormaGiuridica();
  }

  public Vector<CodeDescription> getTipiIntermediario() throws SolmrException
  {
    return iac.getTipiIntermediario();
  }

  public Vector<CodeDescription> getTipiIntermediarioUmaProv()
      throws SolmrException
  {
    return iac.getTipiIntermediarioUmaProv();
  }

  public Vector<CodeDescription> getTipiUtilizzo() throws SolmrException
  {
    return iac.getTipiUtilizzo();
  }

  /**
   * Metodo per recuperare l'elenco dei dati dalla tabella di decodifica
   * DB_TIPO_TIPOLOGIA_DOCUMENTO
   * 
   * @return CodeDescription
   * @throws SolmrException
   */
  public Vector<CodeDescription> getTipiTipologiaDocumento()
      throws SolmrException
  {
    return iac.getTipiTipologiaDocumento();
  }

  public Vector<CodeDescription> getTipiTipologiaDocumento(boolean cessata)
      throws SolmrException
  {
    return iac.getTipiTipologiaDocumento(cessata);
  }

  public Vector<ProvinciaVO> getProvinceByRegione(String idRegione)
      throws SolmrException
  {
    return iac.getProvinceByRegione(idRegione);
  }
  
  public Vector<ProvinciaVO> getProvince()
      throws SolmrException
  {
    return iac.getProvince();
  }

  public Vector<ComuneVO> getComuniLikeByRegione(String idRegione, String like)
      throws SolmrException
  {
    return iac.getComuniLikeByRegione(idRegione, like);
  }

  // Metodo per recuperare l'elenco dei comuni a partire dalla provincia e dal
  // comune anche parziali
  public Vector<ComuneVO> getComuniLikeProvAndCom(String provincia,
      String comune) throws SolmrException
  {
    return iac.getComuniLikeProvAndCom(provincia, comune);
  }

  public Vector<ComuneVO> getComuniByDescCom(String comune)
      throws SolmrException
  {
    return iac.getComuniByDescCom(comune);
  }

  public Vector<ComuneVO> getComuniNonEstintiLikeProvAndCom(String provincia,
      String comune, String flagEstero) throws SolmrException
  {
    return iac.getComuniNonEstintiLikeProvAndCom(provincia, comune, flagEstero);
  }

  // Metodo per recuperare l'elenco delle attivita OTE a partire da codice e
  // descrizione anche parziali
  public Vector<CodeDescription> getTipiAttivitaOTE(String codice,
      String descrizione) throws SolmrException
  {
    return iac.getTipiAttivitaOTE(codice, descrizione);
  }

  // Metodo per recuperare l'elenco delle attivita OTE a partire da codice e
  // descrizione anche parziali
  public Vector<CodeDescription> getTipiAttivitaATECO(String codice,
      String descrizione) throws SolmrException
  {
    return iac.getTipiAttivitaATECO(codice, descrizione);
  }

  /**
   * Metodo che mi restituisce l'elenco degli stati esteri in relazione ai
   * parametri
   * 
   * @param statoEstero
   * @param estinto
   * @param flagCatastoAttivo
   * @return java.util.Vector
   * @throws SolmrException
   */
  public Vector<ComuneVO> ricercaStatoEstero(String statoEstero,
      String estinto, String flagCatastoAttivo) throws SolmrException
  {
    return iac.ricercaStatoEstero(statoEstero, estinto, flagCatastoAttivo);
  }

  // Metodo per modificare la sede legale
  public void updateSedeLegale(AnagAziendaVO anagAziendaVO,
      long idUtenteAggiornamento) throws SolmrException
  {
    iac.updateSedeLegale(anagAziendaVO, idUtenteAggiornamento);
  }

  public String getProcedimento(Integer code) throws SolmrException
  {
    return iac.getProcedimento(code);
  }

  public String getTipoAttivita(Integer code) throws SolmrException
  {
    return iac.getTipoAttivita(code);
  }

  public String getTipoAzienda(Integer code) throws SolmrException
  {
    return iac.getTipoAzienda(code);
  }

  public String getTipoCasoParticolare(Integer code) throws SolmrException
  {
    return iac.getTipoCasoParticolare(code);
  }

  public String getTipoFabbricato(Integer code) throws SolmrException
  {
    return iac.getTipoFabbricato(code);
  }

  public String getTipoFormaGiuridica(Integer code) throws SolmrException
  {
    return iac.getTipoFormaGiuridica(code);
  }

  public String getTipoIntermediario(Integer code) throws SolmrException
  {
    return iac.getTipoIntermediario(code);
  }

  public String getTipoUtilizzo(Integer code) throws SolmrException
  {
    return iac.getTipoUtilizzo(code);
  }

  public String getTipoZonaAltimetrica(Integer code) throws SolmrException
  {
    return iac.getTipoZonaAltimetrica(code);
  }

  public void checkDataCessazione(Long anagAziendaPK, String dataCessazione)
      throws SolmrException
  {
    iac.checkDataCessazione(anagAziendaPK, dataCessazione);
  }

  public void cessaAzienda(AnagAziendaVO anagVO, Date dataCess, String causale,
      long idUtenteAggiornamento) throws SolmrException
  {
    iac.cessaAzienda(anagVO, dataCess, causale, idUtenteAggiornamento);
  }

  public void storicizzaSedeLegale(AnagAziendaVO anagAziendaVO) throws SolmrException
  {
    iac.storicizzaSedeLegale(anagAziendaVO);
  }

  public String ricercaCodiceComune(String descrizioneComune,
      String siglaProvincia) throws SolmrException
  {
    return iac.ricercaCodiceComune(descrizioneComune, siglaProvincia);
  }

  public void deleteUte(Long idUte) throws SolmrException
  {
    iac.deleteUTE(idUte);
  }

  public AnagAziendaVO findAziendaAttiva(Long idAzienda) throws SolmrException
  {
    return iac.findAziendaAttiva(idAzienda);
  }

  public void countUteByAziendaAndComune(Long idAzienda, String comune)
      throws SolmrException
  {
    iac.countUteByAziendaAndComune(idAzienda, comune);
  }

  public void insertUte(UteVO uVO, long idUtenteAggiornamento)
      throws SolmrException
  {
    iac.insertUte(uVO, idUtenteAggiornamento);
  }

  public PersonaFisicaVO getPersonaFisica(String cuaa) throws SolmrException
  {
    return iac.getPersonaFisica(cuaa);
  }

  public void cambiaRappresentanteLegale(Long aziendaPK,
      PersonaFisicaVO personaVO, long idUtenteAggiornamento)
      throws SolmrException
  {
    iac.cambiaRappresentanteLegale(aziendaPK, personaVO, idUtenteAggiornamento);
  }

  public ComuneVO getComuneByCUAA(String cuaa) throws SolmrException
  {
    return iac.getComuneByCUAA(cuaa);
  }

  public ComuneVO getComuneByISTAT(String istat) throws SolmrException
  {
    return iac.getComuneByISTAT(istat);
  }

  // Metodo per recuperare il valore del flag CCIAA relativo alla forma
  // giuridica
  public String getFormaGiuridicaFlagCCIAA(Long idFormaGiuridica)
      throws SolmrException
  {
    return iac.getFormaGiuridicaFlagCCIAA(idFormaGiuridica);
  }

  public AnagAziendaVO getAziendaCUAAandCodFiscale(String cuaa,
      String partitaIVA) throws SolmrException
  {
    return iac.getAziendaCUAAandCodFiscale(cuaa, partitaIVA);
  }

  public AnagAziendaVO getAltraAziendaFromPartitaIVA(String partitaIVA,
      Long idAzienda) throws SolmrException
  {
    return iac.getAltraAziendaFromPartitaIVA(partitaIVA, idAzienda);
  }

  // Metodo per controllare che non esista già un'azienda agricola valida con
  // CUAA e partita IVA indicati
  public void checkCUAAandCodFiscale(String cuaa, String partitaIVA)
      throws SolmrException
  {
    iac.checkCUAAandCodFiscale(cuaa, partitaIVA);
  }

  public boolean isCUAAAlreadyPresentInsediate(String cuaa)
      throws SolmrException
  {
    return iac.isCUAAAlreadyPresentInsediate(cuaa);
  }

  public void checkPartitaIVA(String partitaIVA, Long idAzienda)
      throws SolmrException
  {
    iac.checkPartitaIVA(partitaIVA, idAzienda);
  }

  public void checkCUAA(String cuaa) throws SolmrException
  {
    iac.checkCUAA(cuaa);
  }

  public void checkIsCUAAPresent(String cuaa, Long idAzienda)
      throws SolmrException
  {
    iac.checkIsCUAAPresent(cuaa, idAzienda);
  }

  // Metodo per recuperare il comune a partire dal codiceFiscale
  public String getComuneFromCF(String codiceFiscale) throws SolmrException
  {
    return iac.getComuneFromCF(codiceFiscale);
  }

  // Metodo per recuperare l'id dell'attività OTE
  public Long ricercaIdAttivitaOTE(String codice, String descrizione)
      throws SolmrException
  {
    return iac.ricercaIdAttivitaOTE(codice, descrizione);
  }

  // Metodo per recuperare l'id dell'attività OTE con il codice e la descrizione
  public Long ricercaIdAttivitaOTE(String codice, String descrizione,
      boolean forPopup) throws SolmrException
  {
    return iac.ricercaIdAttivitaOTE(codice, descrizione, forPopup);
  }

  // Metodo per recuperare l'id dell'attività ATECO
  public Long ricercaIdAttivitaATECO(String codice, String descrizione)
      throws SolmrException
  {
    return iac.ricercaIdAttivitaATECO(codice, descrizione);
  }

  // Metodo per recuperare l'id dell'attività ATECO
  public Long ricercaIdAttivitaATECO(String codice, String descrizione,
      boolean forPopup) throws SolmrException
  {
    return iac.ricercaIdAttivitaATECO(codice, descrizione, forPopup);
  }

  // Metodo per recuperare il codice fiscale del comune
  public String ricercaCodiceFiscaleComune(String descrizioneComune,
      String siglaProvincia) throws SolmrException
  {
    return iac.ricercaCodiceFiscaleComune(descrizioneComune, siglaProvincia);
  }

  public void updateUte(UteVO uteVO) throws SolmrException
  {
    iac.updateUTE(uteVO);
  }

  // Metodo per inserire un'azienda agricola
  public Long insertAzienda(AnagAziendaVO aaVO, PersonaFisicaVO pfVO,
      UteVO ute, long idUtenteAggiornamento) throws SolmrException
  {
    return iac.insertAzienda(aaVO, pfVO, ute, idUtenteAggiornamento);
  }

  // Metodo per recuperare una descrizione a partire dal codice
  public String getDescriptionFromCode(String tableName, Integer code)
      throws SolmrException
  {
    return iac.getDescriptionFromCode(tableName, code);
  }

  public void updateAzienda(AnagAziendaVO anagVO)
      throws SolmrException
  {
    iac.updateAzienda(anagVO);
  }

  public void updateRappLegale(PersonaFisicaVO pfVO, long idUtenteAggiornamento)
      throws SolmrException
  {
    iac.updateRappLegale(pfVO, idUtenteAggiornamento);
  }

  public void storicizzaAzienda(AnagAziendaVO anagVO, long idUtenteAggiornamento)
      throws SolmrException
  {
    iac.storicizzaAzienda(anagVO, idUtenteAggiornamento);
  }

  public Vector<CodeDescription> getTipiFormaGiuridicaNonIndividuale()
      throws SolmrException
  {
    return iac.getTipiFormaGiuridicaNonIndividuale();
  }

  public Vector<CodeDescription> getTipiRuoloNonTitolare()
      throws SolmrException
  {
    return iac.getTipiRuoloNonTitolare();
  }

  public Vector<PersonaFisicaVO> getSoggetti(Long idAzienda, Date data)
      throws SolmrException
  {
    return iac.getSoggetti(idAzienda, data);
  }

  public Vector<PersonaFisicaVO> getSoggetti(Long idAzienda, Boolean storico)
      throws SolmrException
  {
    return iac.getSoggetti(idAzienda, storico);
  }

  public void checkForDeleteSoggetto(Long idContitolare) throws SolmrException
  {
    iac.checkForDeleteSoggetto(idContitolare);
  }

  public void deleteContitolare(Long idContitolare) throws SolmrException
  {
    iac.deleteContitolare(idContitolare);
  }

  public PersonaFisicaVO getDettaglioSoggetti(Long idSoggetti, Long idAzienda)
      throws SolmrException
  {
    return iac.getDettaglioSoggetti(idSoggetti, idAzienda);
  }

  public void inserisciSoggetto(PersonaFisicaVO pfVO, long idUtenteAggiornamento)
      throws SolmrException
  {
    iac.inserisciSoggetto(pfVO, idUtenteAggiornamento);
  }

  public void updateSoggetto(PersonaFisicaVO pfVO, long idUtenteAggiornamento)
      throws SolmrException
  {
    iac.updateSoggetto(pfVO, idUtenteAggiornamento);
  }

  public void utenteConDelega(UtenteAbilitazioni utenteAbilitazioni, Long idAzienda)
      throws SolmrException
  {
    iac.utenteConDelega(utenteAbilitazioni, idAzienda);
  }

  // Metodo per recuperare il flag partita iva relativo ad una specifica forma
  // giuridica
  public String getFlagPartitaIva(Long idTipoFormaGiuridica)
      throws SolmrException
  {
    return iac.getFlagPartitaIva(idTipoFormaGiuridica);
  }
  
  public String getObbligoGfFromFormaGiuridica(Long idTipoFormaGiuridica)
      throws SolmrException
  {
    return iac.getObbligoGfFromFormaGiuridica(idTipoFormaGiuridica);
  }

  public Long getIdTipologiaAziendaByFormaGiuridica(Long idTipoFormaGiuridica,
      Boolean flagAziendaProvvisoria) throws SolmrException
  {
    return iac.getIdTipologiaAziendaByFormaGiuridica(idTipoFormaGiuridica,
        flagAziendaProvvisoria);
  }

  // Metodo per recupare l'id della forma giuridica nostra dato il codice di
  // AAEP
  public Long getIdTipoFormaGiuridica(String idTipoFormaGiuridicaAAEP)
      throws SolmrException
  {
    return iac.getIdTipoFormaGiuridica(idTipoFormaGiuridicaAAEP);
  }

  // Metodo per recupare la descrizione di una nostra forma giuridica data il
  // codice
  // della forma giuridica di AAEP
  public String getDescTipoFormaGiuridica(String idTipoFormaGiuridicaAAEP)
      throws SolmrException
  {
    return iac.getDescTipoFormaGiuridica(idTipoFormaGiuridicaAAEP);
  }

  // Metodo per verificare la correttezza della provincia REA
  public boolean isProvinciaReaValida(String siglaProvincia)
      throws SolmrException
  {
    return iac.isProvinciaReaValida(siglaProvincia);
  }

  /**
   * Metodo che dato una tipologia di azienda mi dice se devo effettuare i
   * controlli sull'univocità della partita IVA / CUAA o no
   * 
   * @param idTipoAzienda
   *          Integer
   * @throws SolmrException
   * @return boolean
   */
  public boolean isFlagUnivocitaAzienda(Integer idTipoAzienda)
      throws SolmrException
  {
    return iac.isFlagUnivocitaAzienda(idTipoAzienda);
  }

  // Metodo per reperire il rappresentante legale di una società a partire
  // dall'id_anagrafica_azienda
  public PersonaFisicaVO getRappresentanteLegaleFromIdAnagAzienda(
      Long idAnagAzienda) throws SolmrException
  {
    return iac.getRappresentanteLegaleFromIdAnagAzienda(idAnagAzienda);
  }

  public PersonaFisicaVO getRappresentanteLegaleFromIdAnagAziendaAndDichCons(
      long idAnagAzienda, Date dataDichiarazione) throws SolmrException
  {
    return iac.getRappresentanteLegaleFromIdAnagAziendaAndDichCons(
        idAnagAzienda, dataDichiarazione);
  }
  
  public Vector<PersonaFisicaVO> getVAltriSoggettiFromIdAnagAziendaAndDichCons(
      long idAnagAzienda, Date dataDichiarazione) throws Exception,
      SolmrException
  {
    return iac.getVAltriSoggettiFromIdAnagAziendaAndDichCons(idAnagAzienda, dataDichiarazione);
  }

  public UtenteIrideVO getUtenteIrideById(Long idUtente) throws SolmrException
  {
    return iac.getUtenteIrideById(idUtente);
  }

  // Metodo per effettuare il passaggio da società a ditta individuale con nuovo
  // titolare
  public Long updateTitolareAzienda(AnagAziendaVO anagAziendaVO,
      PersonaFisicaVO personaTitolareOldVO,
      PersonaFisicaVO personaTitolareNewVO, long idUtenteAggiornamento)
      throws SolmrException
  {
    return iac.updateTitolareAzienda(anagAziendaVO, personaTitolareOldVO,
        personaTitolareNewVO, idUtenteAggiornamento);
  }

  // Gestione Terreni
  public Vector<Long> getAnniRilevamento() throws SolmrException
  {
    return iac.getAnniRilevamento();
  }

  public Vector<CodeDescription> getUnitaProduttive(Long idAzienda)
      throws SolmrException
  {
    return iac.getUnitaProduttive(idAzienda);
  }

  public TerreniVO getTerreni(Long idAzienda, Long idUte, Long anno,
      String criterio) throws SolmrException
  {
    return iac.getTerreni(idAzienda, idUte, anno, criterio);
  }

  public Vector<Vector<Long>> getIdParticelle(Long idAzienda, Long idUte,
      Long anno, String criterio, Long valore) throws SolmrException
  {
    return iac.getIdParticelle(idAzienda, idUte, anno, criterio, valore);
  }

  public Vector<ParticelleVO> getParticelleByIdRange(
      Vector<Vector<Long>> idRange) throws SolmrException
  {
    return iac.getParticelleByIdRange(idRange);
  }
  
  public Vector<TipoSettoreAbacoVO> getListSettoreAbaco() 
    throws SolmrException
  {
    return iac.getListSettoreAbaco();
  }

  public Vector<ParticellaUtilizzoVO> getElencoParticelleForAziendaAndUsoSecondario(
      Long idAzienda, String anno) throws SolmrException
  {
    return iac.getElencoParticelleForAziendaAndUsoSecondario(idAzienda, anno);
  }

  // Metodo per recuperare l'elenco delle fotografie relative alle particelle
  // attraverso l'id azienda e l'uso secondario
  public Vector<ParticellaUtilizzoVO> getElencoConsistenzaParticelleForAziendaAndUsoSecondario(
      Long idAzienda, Long idDichiarazioneConsistenza) throws SolmrException
  {
    return iac.getElencoConsistenzaParticelleForAziendaAndUsoSecondario(
        idAzienda, idDichiarazioneConsistenza);

  }

  // Metodo per recuperare le anomalie relative ad una particella
  public Vector<EsitoControlloParticellaVO> getElencoEsitoControlloParticella(
      Long idConduzioneParticella) throws SolmrException
  {
    return iac.getElencoEsitoControlloParticella(idConduzioneParticella);
  }

  // Metodo che restituisce la data di esecuzione controlli di una determinata
  // azienda agricola
  // in formato dd/mm/yyyy hh/mm/ss
  public String getDataUltimaEsecuzioneControlli(Long idAzienda)
      throws SolmrException
  {
    return iac.getDataUltimaEsecuzioneControlli(idAzienda);
  }

  // Metodo recuperare i dati della particella che abbiamo ricevuto dal SIAN in
  // relazione
  // alla particella selezionata dopo la ricerca particellare/piano colturale
  public ParticellaCertificataVO findParticellaCertificataByParametri(
      ParticellaVO particellaVO) throws SolmrException
  {
    return iac.findParticellaCertificataByParametri(particellaVO);
  }

  // Metodo per recuperare i tipi documento
  public Vector<CodeDescription> getTipiDocumento() throws SolmrException
  {
    return iac.getTipiDocumento();
  }

  // Metodo per recuperare l'elenco delle variazioni storiche di una determinata
  // particella a
  // partire dall'id storico particella
  public Vector<ParticellaVO> getElencoStoricoParticella(Long idParticella)
      throws SolmrException
  {
    return iac.getElencoStoricoParticella(idParticella);
  }

  // Metodo per effettuare l'eliminazione massiva degli utilizzi della
  // particella
  public void eliminaUtilizzoParticella(Vector<String> elencoConduzioni,
      Vector<String> elencoIdUtilizzoParticella, long idUtenteAggiornamento)
      throws SolmrException
  {
    iac.eliminaUtilizzoParticella(elencoConduzioni, elencoIdUtilizzoParticella,
        idUtenteAggiornamento);

  }

  /**
   * Metodo per recuperare l'elenco delle particelle in relazione ad un'azienda
   * e al suo stato
   * 
   */
  public Vector<ParticellaVO> getElencoParticelleForImportByAzienda(
      AnagAziendaVO searchAnagAziendaVO, AnagAziendaVO anagAziendaVO,
      RuoloUtenza ruoloUtenza) throws SolmrException
  {
    return iac.getElencoParticelleForImportByAzienda(searchAnagAziendaVO,
        anagAziendaVO, ruoloUtenza);
  }

  /**
   * Metodo per recuperare il record dalla tabella DB_STORICO_PARTICELLA a
   * partire dalla chiave primaria
   * 
   * @param idStoricoParticella
   *          Long
   * @return ParticellaVO
   * @throws SolmrException
   */
  public ParticellaVO getStoricoParticella(Long idStoricoParticella)
      throws SolmrException
  {
    return iac.getStoricoParticella(idStoricoParticella);
  }

  // Fine Gestione Terreni

  public AnagAziendaVO findAziendaAttivabyCriterio(String cuaa,
      String partitaIva) throws SolmrException
  {
    return iac.findAziendaAttivabyCriterio(cuaa, partitaIva);
  }

  public Vector<StringcodeDescription> getProvincePiemonte()
      throws SolmrException
  {
    return iac.getProvincePiemonte();
  }

  public ProvinciaVO getProvinciaByCriterio(String criterio)
      throws SolmrException
  {
    return iac.getProvinciaByCriterio(criterio);
  }

  public String ricercaCodiceComuneNonEstinto(String descrizioneComune,
      String siglaProvincia) throws SolmrException
  {
    return iac.ricercaCodiceComuneNonEstinto(descrizioneComune, siglaProvincia);
  }
  
  public String ricercaCodiceComuneFlagEstinto(String descrizioneComune,
      String siglaProvincia, String estinto)
          throws SolmrException
  {
    return iac.ricercaCodiceComuneFlagEstinto(descrizioneComune, siglaProvincia, estinto);
  }
  
  public Vector<StringcodeDescription> getSessi() throws SolmrException
  {
    Vector<StringcodeDescription> vect = new Vector<StringcodeDescription>();
    StringcodeDescription cd = new StringcodeDescription();
    cd.setCode("M");
    cd.setDescription("M");
    vect.addElement(cd);

    cd = new StringcodeDescription();
    cd.setCode("F");
    cd.setDescription("F");
    vect.addElement(cd);
    return vect;
  }

  public void insertRappLegaleTitolare(Long idAzienda, PersonaFisicaVO pfVO,
      long idUtenteAggiornamento) throws SolmrException
  {
    iac.insertRappLegaleTitolare(idAzienda, pfVO, idUtenteAggiornamento);
  }

  public Vector<Long> getIdPersoneFisiche(String codFiscale, String cognome,
      String nome, String dataNascita, String istatNascita,
      String istatResidenza, boolean personaAttiva) throws SolmrException
  {
    return iac.getIdPersoneFisiche(codFiscale, cognome, nome, dataNascita,
        istatNascita, istatResidenza, personaAttiva);
  }

  public Vector<PersonaFisicaVO> getListPersoneFisicheByIdRange(
      Vector<Long> collIdPF) throws SolmrException
  {
    return iac.getListPersoneFisicheByIdRange(collIdPF);
  }

  public PersonaFisicaVO findByPrimaryKey(Long idPersonaFisica)
      throws SolmrException
  {
    return iac.findByPrimaryKey(idPersonaFisica);
  }

  public String getDenominazioneByIdAzienda(Long idAzienda)
      throws SolmrException
  {
    return iac.getDenominazioneByIdAzienda(idAzienda);
  }

  public String getRappLegaleTitolareByIdAzienda(Long idAzienda)
      throws SolmrException
  {
    return iac.getRappLegaleTitolareByIdAzienda(idAzienda);
  }

  public Vector<Long> getIdAziendeBySoggetto(Long idSoggetto)
      throws SolmrException
  {
    return iac.getIdAziendeBySoggetto(idSoggetto);
  }

  public Vector<AnagAziendaVO> findAziendeByIdAziende(Vector<Long> idAziendeVect)
      throws SolmrException
  {
    return iac.findAziendeByIdAziende(idAziendeVect);
  }

  public String getSiglaProvinciaByIstatProvincia(String istatProvincia)
      throws SolmrException
  {
    return iac.getSiglaProvinciaByIstatProvincia(istatProvincia);
  }

  public PersonaFisicaVO findPersonaFisica(Long idPersonaFisica)
      throws SolmrException
  {
    return iac.findPersonaFisica(idPersonaFisica);
  }

  public Vector<PersonaFisicaVO> findPersonaFisicaByIdSoggettoAndIdAzienda(
      Long idSoggetto, Long idAzienda) throws SolmrException
  {
    return iac.findPersonaFisicaByIdSoggettoAndIdAzienda(idSoggetto, idAzienda);
  }

  public PersonaFisicaVO getDatiSoggettoPerMacchina(Long idPersonaFisica)
      throws SolmrException
  {
    return iac.getDatiSoggettoPerMacchina(idPersonaFisica);
  }

  public AnagraficaAzVO getDatiAziendaPerMacchine(Long idAzienda)
      throws SolmrException
  {
    return iac.getDatiAziendaPerMacchine(idAzienda);
  }

  public Vector<AnagAziendaVO> getListaStoricoAzienda(Long idAzienda)
      throws SolmrException
  {
    return iac.getListaStoricoAzienda(idAzienda);
  }

  // Metodo per recuperare l'elenco delle unità produttive valide associate ad
  // un'azienda agricola
  public Vector<UteVO> getElencoUteAttiveForAzienda(Long idAzienda)
      throws SolmrException
  {
    return iac.getElencoUteAttiveForAzienda(idAzienda);
  }

  // Metodo per recuperare l'elenco delle sezioni relative ad uno specifico
  // comune
  public Vector<CodeDescription> getSezioniByComune(String istatComune)
      throws SolmrException
  {
    return iac.getSezioniByComune(istatComune);
  }

  // Metodo per recuperare l'elenco dei fogli in relazione al comune o stato
  // estero
  // ed eventualmente la sezione
  public Vector<FoglioVO> getFogliByComuneAndSezione(String descrizioneComune,
      String sezione, Long foglio) throws SolmrException
  {
    return iac.getFogliByComuneAndSezione(descrizioneComune, sezione, foglio);
  }

  // Metodo per recuperare l'elenco delle particelle in relazione al comune o
  // stato estero
  // e il foglio
  public Vector<ParticellaVO> getParticelleByParametri(String istatComune,
      Long foglio, String sezione, Long particella, String flagEstinto)
      throws SolmrException
  {
    return iac.getParticelleByParametri(istatComune, foglio, sezione,
        particella, flagEstinto);
  }

  // Metodo per recuperare la sezione a partire dall'istat del comune e dalla
  // sezione stessa
  public String ricercaSezione(String istatComune, String sezione)
      throws SolmrException
  {
    return iac.ricercaSezione(istatComune, sezione);
  }

  // Metodo per recuperare il foglio in relazione al comune o stato estero, il
  // foglio
  // stesso ed eventualmente la sezione
  public FoglioVO ricercaFoglio(String istatComune, String sezione, Long foglio)
      throws SolmrException
  {
    return iac.ricercaFoglio(istatComune, sezione, foglio);
  }

  // Metodo per recuperare la particella in relazione al comune o stato estero
  // e il foglio
  public ParticellaVO ricercaParticellaAttiva(String istatComune,
      String sezione, Long foglio, Long particella, String subalterno)
      throws SolmrException
  {
    return iac.ricercaParticellaAttiva(istatComune, sezione, foglio,
        particella, subalterno);
  }

  // Metodo per controllare che una particella non sia già attribuita ad una
  // azienda
  public void checkParticellaByAzienda(Long idParticella, Long idAzienda)
      throws SolmrException
  {
    iac.checkParticellaByAzienda(idParticella, idAzienda);
  }

  // Metodo per recuperare l'elenco delle aziende che hanno in conduzione la
  // particella selezionata
  public Vector<ElencoAziendeParticellaVO> elencoAziendeByParticellaAndConduzione(
      Long idParticella, Long idAzienda) throws SolmrException
  {
    return iac.elencoAziendeByParticellaAndConduzione(idParticella, idAzienda);
  }

  // Metodo per recuperare i tipiConduzione tranne "proprietà"
  public Vector<CodeDescription> getTipiTitoloPossessoExceptProprieta()
      throws SolmrException
  {
    return iac.getTipiTitoloPossessoExceptProprieta();
  }

  // Metodo per recuperare il valore dell'ultima data fine conduzione delle
  // particelle
  // associate ad un azienda agricola
  public java.util.Date getMaxDataFineConduzione(Long idParticella,
      Long idAzienda) throws SolmrException
  {
    return iac.getMaxDataFineConduzione(idParticella, idAzienda);
  }

  // Metodo per recuperare i tipi utilizzo attivi
  public Vector<CodeDescription> getTipiUtilizzoAttivi() throws SolmrException
  {
    return iac.getTipiUtilizzoAttivi();
  }

  // Metodo per recuperare i tipi utilizzo attivi dato un indirizzo
  public Vector<CodeDescription> getTipiUtilizzoAttivi(int idIndirizzo)
      throws SolmrException
  {
    return iac.getTipiUtilizzoAttivi(idIndirizzo);
  }

  // Metodo per inserire un record in DB_PARTICELLA
  public Long insertParticella() throws SolmrException
  {
    return iac.insertParticella();
  }

  // Metodo per recuperare la particella provvisoria in relazione al comune o
  // stato estero
  // e il foglio
  public ParticellaVO ricercaParticellaProvvisoriaAttiva(String istatComune,
      String sezione, Long foglio) throws SolmrException
  {
    return iac.ricercaParticellaProvvisoriaAttiva(istatComune, sezione, foglio);
  }

  // Metodo per recuperare la particella in relazione al comune o stato estero
  // la sezione e il foglio indipendentemente dal fatto che sia attiva o meno
  public ParticellaVO ricercaParticella(String istatComune, String sezione,
      Long foglio, Long particella, String subalterno) throws SolmrException
  {
    return iac.ricercaParticella(istatComune, sezione, foglio, particella,
        subalterno);
  }

  public void cessaParticelleByIdParticellaRange(long idParticella[])
      throws SolmrException
  {
    iac.cessaParticelleByIdParticellaRange(idParticella);
  }

  // Metodo per recuperare il valore massimo inseribile per una particella non
  // presente in archivio
  // nata dal frazionamento di una particella già esistente in archivio che
  // presenta già una
  // particella nata dal suo frazionamento.....(Per chi lo legge in fututo so
  // che è complesso ma io l'hi
  // capito....) :)
  public double getMaxSupCatastaleInseribile(Long idParticella)
      throws SolmrException
  {
    return iac.getMaxSupCatastaleInseribile(idParticella);
  }

  // Metodo per recuperare la particella a partire dalla sua chiave primaria
  public ParticellaVO findParticellaByPrimaryKey(Long idStoricoParticella)
      throws SolmrException
  {
    return iac.findParticellaByPrimaryKey(idStoricoParticella);
  }

  public Vector<CodeDescription> getIndirizziTipiUtilizzoAttivi()
      throws SolmrException
  {
    return iac.getIndirizziTipiUtilizzoAttivi();
  }

  public Vector<CodeDescription> getTipiAreaA() throws SolmrException
  {
    return iac.getTipiAreaA();
  }

  public Vector<CodeDescription> getTipiAreaB() throws SolmrException
  {
    return iac.getTipiAreaB();
  }

  public Vector<CodeDescription> getTipiAreaC() throws SolmrException
  {
    return iac.getTipiAreaC();
  }

  public Vector<CodeDescription> getTipiAreaD() throws SolmrException
  {
    return iac.getTipiAreaD();
  }

  public Vector<CodeDescription> getTipiZonaAltimetrica() throws SolmrException
  {
    return iac.getTipiZonaAltimetrica();
  }

  public Vector<CodeDescription> getTipiCasoParticolare() throws SolmrException
  {
    return iac.getTipiCasoParticolare();
  }

  // Metodo per recuperare l'elenco dei titoli di studio
  public Vector<CodeDescription> getTitoliStudio() throws SolmrException
  {
    return iac.getTitoliStudio();
  }

  // Metodo per recuperare l'elenco dei prefissi per un telefono cellulare
  public Vector<CodeDescription> getPrefissiCellulare() throws SolmrException
  {
    return iac.getPrefissiCellulare();
  }

  // Metodo per recuperare l'indirizzo di studio in relazione al titolo
  // selezionato
  public Vector<CodeDescription> getIndirizzoStudioByTitolo(Long idTitoloStudio)
      throws SolmrException
  {
    return iac.getIndirizzoStudioByTitolo(idTitoloStudio);
  }

  // Metodo per recuperare la data dell'ultimo dichiarazione di consistenza
  public Date getMaxDataDichiarazioneConsistenza(Long idAzienda)
      throws SolmrException
  {
    return iac.getMaxDataDichiarazioneConsistenza(idAzienda);
  }

  // Restituisce l'elenco (ContoCorrenteVO) dei conti correnti legati ad
  // un'azienda
  public Vector<ContoCorrenteVO> getContiCorrenti(Long idAzienda,
      java.util.Date dataSituazioneAl) throws SolmrException
  {
    return iac.getContiCorrenti(idAzienda, dataSituazioneAl);
  }

  // Restituisce l'elenco (ContoCorrenteVO) dei conti correnti legati ad
  // un'azienda
  public Vector<ContoCorrenteVO> getContiCorrenti(Long idAzienda,
      boolean estinto) throws SolmrException
  {
    return iac.getContiCorrenti(idAzienda, estinto);
  }

  // Metodo per effettuare una cancellazione logica da un conto corrente
  public void deleteContoCorrente(Long idConto, long idUtenteAggiornamento)
      throws SolmrException
  {
    iac.deleteContoCorrente(idConto, idUtenteAggiornamento);
  }

  // Metodo per effettuare l'estinzione dei conti correnti collegati ad
  // un'azienda agricola
  public void desistsAccountCorrent(Long idAzienda, Long idUtente)
      throws SolmrException
  {
    iac.desistsAccountCorrent(idAzienda, idUtente);
  }

  public Vector<TemporaneaPraticaAziendaVO> aggiornaPraticaAziendaPLQSL(
      Long idAzienda, Long idUtente, Long idDichiarazioneConsistenza)
      throws SolmrException
  {
    return iac.aggiornaPraticaAziendaPLQSL(idAzienda, idUtente,
        idDichiarazioneConsistenza);
  }
  
  public void aggiornaPraticaAziendaPLQSL(Long idAzienda) 
      throws SolmrException
  {
    iac.aggiornaPraticaAziendaPLQSL(idAzienda);
  }

  public boolean deleteDichConsAmmessa(Long idAzienda, Long idUtente,
      Long idDichiarazioneConsistenza) throws SolmrException
  {
    return iac.deleteDichConsAmmessa(idAzienda, idUtente,
        idDichiarazioneConsistenza);
  }

  public boolean newDichConsAmmessa(Long idAzienda, Long idUtente,
      Long idMotivoDichiarazione) throws SolmrException
  {
    return iac.newDichConsAmmessa(idAzienda, idUtente, idMotivoDichiarazione);
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
   * @throws SolmrException
   */
  public BancaSportelloVO[] searchBanca(String abi, String denominazione)
      throws SolmrException
  {
    return iac.searchBanca(abi, denominazione);
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
   * @throws SolmrException
   */
  public BancaSportelloVO[] searchSportello(String abi, String cab,
      String comune) throws SolmrException
  {
    return iac.searchSportello(abi, cab, comune);
  }

  // Metodo per recuperare i tipi di fabbricato
  public Vector<CodeDescription> getTipiTipologiaFabbricato()
      throws SolmrException
  {
    return iac.getTipiTipologiaFabbricato();
  }

  // Metodo per recuperare le forme dei fabbricati
  public Vector<CodeDescription> getTipiFormaFabbricato(
      Long idTipologiaFabbricato) throws SolmrException
  {
    return iac.getTipiFormaFabbricato(idTipologiaFabbricato);
  }

  // Metodo per recuperare i tipi di coltura sotto serra
  public Vector<CodeDescription> getTipiColturaSerra(Long idTipologiaFabbricato)
      throws SolmrException
  {
    return iac.getTipiColturaSerra(idTipologiaFabbricato);
  }

  public String getUnitaMisuraByTipoFabbricato(Long idTipologiaFabbricato)
      throws SolmrException
  {
    return iac.getUnitaMisuraByTipoFabbricato(idTipologiaFabbricato);
  }

  public int getMesiRiscaldamentoBySerra(String tipologiaColturaSerra)
      throws SolmrException
  {
    return iac.getMesiRiscaldamentoBySerra(tipologiaColturaSerra);
  }

  public double getFattoreCubaturaByFormaFabbricato(String idFormaFabbricato)
      throws SolmrException
  {
    return iac.getFattoreCubaturaByFormaFabbricato(idFormaFabbricato);
  }

  // Metodo per recuperare l'elenco delle particelle ad uso fabbricato associate
  // ad una
  // unita produttiva
  public Vector<ParticellaVO> getElencoParticelleFabbricatoByUte(Long idUte,
      boolean serra) throws SolmrException
  {
    return iac.getElencoParticelleFabbricatoByUte(idUte, serra);
  }

  // Metodo per recuperare la somma delle superfici dei fabbricati che insistono
  // esclusivamente
  // sulla particella selezionata
  public String getSuperficiFabbricatiByParticella(Long idUte, Long idParticella)
      throws SolmrException
  {
    return iac.getSuperficiFabbricatiByParticella(idUte, idParticella);
  }

  // Metodo per l'inserimento dei fabbricati
  public Long inserisciFabbricato(FabbricatoVO fabbricatoVO,
      Vector<ParticellaVO> elencoParticelleSelezionate, long idUtenteAggiornamento)
      throws SolmrException
  {
    return iac.inserisciFabbricato(fabbricatoVO, elencoParticelleSelezionate,
        idUtenteAggiornamento);
  }

  // Metodo per effettuare la ricerca dei fabbricati relativi all'azienda
  // agricola selezionata
  public Vector<FabbricatoVO> ricercaFabbricatiByAzienda(Long idAzienda,
      String dataSituazioneAl) throws SolmrException
  {
    return iac.ricercaFabbricatiByAzienda(idAzienda, dataSituazioneAl);
  }

  // Metodo per recuperare il fabbricato a partire dalla sua chiave primaria
  public FabbricatoVO findFabbricatoByPrimaryKey(Long idFabbricato)
      throws SolmrException
  {
    return iac.findFabbricatoByPrimaryKey(idFabbricato);
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
   * @throws SolmrException
   */
  public Vector<ParticellaVO> getElencoParticelleByFabbricato(
      FabbricatoVO fabbricatoVO, boolean modifica) throws SolmrException
  {
    return iac.getElencoParticelleByFabbricato(fabbricatoVO, modifica);
  }

  // Metodo per recuperare l'elenco delle particelle ad uso fabbricato associate
  // ad una
  // unita produttiva associabili ad un fabbricato
  public Vector<ParticellaVO> getElencoParticelleFabbricatoByUteAssociabili(
      Long idUte, Vector<Long> elencoParticelle, boolean serra)
      throws SolmrException
  {
    return iac.getElencoParticelleFabbricatoByUteAssociabili(idUte,
        elencoParticelle, serra);
  }

  // Metodo per cessare l'utilizzo di una particella a fabbricato
  public void cessaUtilizzoParticellaFabbricato(FabbricatoVO fabbricatoVO)
      throws SolmrException
  {
    iac.cessaUtilizzoParticellaFabbricato(fabbricatoVO);
  }

  // Metodo per eliminare dei record da DB_FABBRICATO_PARTICELLA
  public void deleteParticellaFabbricato(ParticellaVO particellaFabbricatoVO)
      throws SolmrException
  {
    iac.deleteParticellaFabbricato(particellaFabbricatoVO);
  }

  // Metodo per effettuare la modifica del fabbricato
  public Long modificaFabbricato(FabbricatoVO fabbricatoVO,
      Vector<ParticellaVO> particelleForFabbricato,
      Vector<ParticellaVO> elencoParticelleAssociate,
      Vector<ParticellaVO> elencoParticelleAssociabili, long idUtenteAggiornamento,
      long idAzienda) throws SolmrException
  {
    return iac.modificaFabbricato(fabbricatoVO, particelleForFabbricato,
        elencoParticelleAssociate, elencoParticelleAssociabili, idUtenteAggiornamento,
        idAzienda);
  }

  // Metodo per eliminare dei record da DB_FABBRICATO_PARTICELLA a partire dal
  // fabbricato
  public void deleteParticellaFabbricatoByIdFabbricato(FabbricatoVO fabbricatoVO)
      throws SolmrException
  {
    iac.deleteParticellaFabbricatoByIdFabbricato(fabbricatoVO);
  }

  // Metodo per eliminare un record da DB_FABBRICATO a partire dal fabbricato
  public void deleteFabbricato(FabbricatoVO fabbricatoVO) throws SolmrException
  {
    iac.deleteFabbricato(fabbricatoVO);
  }

  // Metodo per eliminare un fabbricato e i suoi legami
  public void eliminaFabbricato(FabbricatoVO fabbricatoVO, long idAzienda)
      throws SolmrException
  {
    iac.eliminaFabbricato(fabbricatoVO, idAzienda);
  }

  // Metodo per recuperare l'elenco delle unità produttive valide ad una certa
  // data associate
  // ad un'azienda agricola
  public Vector<UteVO> getElencoUteAttiveForDateAndAzienda(Long idAzienda,
      String data) throws SolmrException
  {
    return iac.getElencoUteAttiveForDateAndAzienda(idAzienda, data);
  }

  // Metodo per recuperare l'elenco delle particelle relative ad un'azienda
  // agricola ed eventualmente
  // ad un'unità produttiva selezionata
  public Vector<ParticellaVO> getElencoParticelleForUteAndAzienda(Long idAzienda)
      throws SolmrException
  {
    return iac.getElencoParticelleForUteAndAzienda(idAzienda);
  }

  // Metodo per recuperare l'elenco delle particelle relative ad un'azienda
  // agricola,
  // ad un'unità produttiva e per riepilogo titolo possesso/comune
  public Vector<ParticellaVO> getElencoParticelleForUteAndAziendaPossAndComune(
      Long idAzienda) throws SolmrException
  {
    return iac.getElencoParticelleForUteAndAziendaPossAndComune(idAzienda);
  }

  // Metodo per recuperare l'elenco delle particelle relative ad un'azienda
  // agricola,
  // ad un'unità produttiva e per riepilogo comune
  public Vector<ParticellaVO> getElencoParticelleForUteAndAziendaComune(
      Long idAzienda) throws SolmrException
  {
    return iac.getElencoParticelleForUteAndAziendaComune(idAzienda);
  }

  // Metodo per effettuare la ricerca delle particelle attive in relazione ai
  // parametri di
  // ricerca
  public Vector<ParticellaVO> ricercaParticelleAttiveByParametri(
      ParticellaVO particellaVO, String data, Long idAzienda)
      throws SolmrException
  {
    return iac
        .ricercaParticelleAttiveByParametri(particellaVO, data, idAzienda);
  }

  // Metodo per controllare che non esistano conduzioni attive di particelle
  // associate ad
  // una azienda agricola
  public void checkCessaAziendaByConduzioneParticella(Long idUte)
      throws SolmrException
  {
    iac.checkCessaAziendaByConduzioneParticella(idUte);
  }

  // Metodo che mi restituisce un vettore contenente le date delle "fotografie"
  // effettuate
  // su una specifica azienda individuata attraverso l'id azienda
  public Vector<CodeDescription> getListaDateConsistenza(Long idAzienda)
      throws SolmrException
  {
    return iac.getListaDateConsistenza(idAzienda);
  }

  // Metodo per recuperare l'elenco delle fotografie relative alle particelle
  // attraverso l'id azienda e il codice fotografia terreni
  public Vector<ParticellaVO> getElencoConsistenzaParticelleForAzienda(
      Long idAzienda, Long idDichiarazioneConsistenza) throws SolmrException
  {
    return iac.getElencoConsistenzaParticelleForAzienda(idAzienda,
        idDichiarazioneConsistenza);
  }

  // Metodo per verificare se l'ultimo aggiornamento della consistenza sia
  // avvenuto dopo
  // l'ultima data di dichiarazione di consistenza
  public void checkLastAggiornamentoAfterMaxDichConsistenza(Long idAzienda)
      throws SolmrException
  {
    iac.checkLastAggiornamentoAfterMaxDichConsistenza(idAzienda);
  }

  // Metodo per recuperare l'elenco delle fotografie relative alle particelle
  // attraverso l'id azienda e il codice fotografia terreni
  public Vector<ParticellaVO> getElencoConsistenzaParticelleForAziendaAndPossessoAndComune(
      Long idAzienda, Long idDichiarazioneConsistenza) throws SolmrException
  {
    return iac.getElencoConsistenzaParticelleForAziendaAndPossessoAndComune(
        idAzienda, idDichiarazioneConsistenza);
  }

  // Metodo per recuperare l'elenco delle fotografie relative alle particelle
  // attraverso l'id azienda e il codice fotografia terreni
  public Vector<ParticellaVO> getElencoConsistenzaParticelleForAziendaAndComune(
      Long idAzienda, Long idDichiarazioneConsistenza) throws SolmrException
  {
    return iac.getElencoConsistenzaParticelleForAziendaAndComune(idAzienda,
        idDichiarazioneConsistenza);
  }

  // Metodo per recuperare l'anno successivo rispetto a quello di sistema nella
  // tabella degli utilizzi
  // inserito come previsione
  public String getAnnoPrevisioneUtilizzi(Long idAzienda) throws SolmrException
  {
    return iac.getAnnoPrevisioneUtilizzi(idAzienda);
  }

  // Metodo per recuperare il valore di tutte le superfici condotte da
  // un'azienda agricola
  public String getTotaleSupCondotteByAzienda(Long idAzienda, String data)
      throws SolmrException
  {
    return iac.getTotaleSupCondotteByAzienda(idAzienda, data);
  }

  // Metodo per recuperare l'elenco delle fotografie relative alle particelle
  // attraverso l'id azienda e la destinazione d'uso
  public Vector<ParticellaVO> getElencoConsistenzaParticelleForAziendaAndDestinazioneUso(
      Long idAzienda, Long idDichiarazioneConsistenza,
      Long idConduzioneParticella) throws SolmrException
  {
    return iac.getElencoConsistenzaParticelleForAziendaAndDestinazioneUso(
        idAzienda, idDichiarazioneConsistenza, idConduzioneParticella);
  }

  // Metodo per effettuare la ricerca delle particelle storicizzate in relazione
  // ai parametri
  // di ricerca
  public Vector<ParticellaVO> ricercaParticelleStoricizzateByParametri(
      ParticellaVO particellaVO, Long idDichiarazioneConsistenza)
      throws SolmrException
  {
    return iac.ricercaParticelleStoricizzateByParametri(particellaVO,
        idDichiarazioneConsistenza);
  }

  // Metodo per recuperare i dati territoriali della particella a partire
  // dall'id storico particella
  public ParticellaVO getDettaglioParticellaDatiTerritoriali(
      Long idStoricoParticella) throws SolmrException
  {
    return iac.getDettaglioParticellaDatiTerritoriali(idStoricoParticella);
  }

  // Metodo per recuperare i dati dell'uso del suolo e dei contratti della
  // particella a partire dall'id storico particella
  public Vector<ParticellaUtilizzoVO> getElencoParticellaUtilizzoVO(
      Long idConduzioneParticella, String anno) throws SolmrException
  {
    return iac.getElencoParticellaUtilizzoVO(idConduzioneParticella, anno);
  }

  // Metodo per recuperare il valore di tutte le superfici utilizzate da
  // un'azienda agricola
  public double getTotaleSupUtilizzateByIdConduzioneParticella(
      Long idConduzioneParticella, String anno) throws SolmrException
  {
    return iac.getTotaleSupUtilizzateByIdConduzioneParticella(
        idConduzioneParticella, anno);
  }

  // Metodo per recuperare i dati territoriali della particella a partire
  // dall'id dichiarazione
  // consistenza quindi si tratta di dati storicizzati
  public ParticellaVO getDettaglioParticellaStoricizzataDatiTerritoriali(
      Long idConduzioneDichiarata) throws SolmrException
  {
    return iac
        .getDettaglioParticellaStoricizzataDatiTerritoriali(idConduzioneDichiarata);
  }

  // Metodo per recuperare i dati della conduzione e dei contratti della
  // particella a partire dall'id conduzione
  // dichiarata e quindi si tratta di dati storicizzati
  public ParticellaVO getDettaglioParticellaStoricizzataConduzione(
      Long idConduzioneDichiarata) throws SolmrException
  {
    return iac
        .getDettaglioParticellaStoricizzataConduzione(idConduzioneDichiarata);
  }

  // Metodo per recuperare i dati dell'uso del suolo della particella a partire
  // dall'id conduzione dichiarata
  // quindi sto prelevando l'elenco degli utilizzi storicizzati della particella
  public Vector<ParticellaUtilizzoVO> getElencoStoricoParticellaUtilizzoVO(
      Long idConduzioneDichiarata, Long idDichiarazioneConsistenza)
      throws SolmrException
  {
    return iac.getElencoStoricoParticellaUtilizzoVO(idConduzioneDichiarata,
        idDichiarazioneConsistenza);
  }

  // Metodo per recuperare gli indirizzi degli utilizzi
  public Vector<CodeDescription> getElencoIndirizziUtilizzi()
      throws SolmrException
  {
    return iac.getElencoIndirizziUtilizzi();
  }

  // Metodo per recuperare i tipi utilizzo dato un indirizzo tipo utilizzo
  public Vector<CodeDescription> getTipiUtilizzoForIdIndirizzoTipoUtilizzo(
      Long idTipoIndirizzoUtilizzo) throws SolmrException
  {
    return iac
        .getTipiUtilizzoForIdIndirizzoTipoUtilizzo(idTipoIndirizzoUtilizzo);
  }

  // Metodo per recuperare l'elenco delle particelle in relazione a dei filtri
  // di ricerca e agli utilizzi
  public Vector<ParticellaVO> ricercaParticelleByParametriAndUtilizzi(
      ParticellaVO particellaRicercaVO, Long idAzienda) throws SolmrException
  {
    return iac.ricercaParticelleByParametriAndUtilizzi(particellaRicercaVO,
        idAzienda);
  }

  // Metodo per effettuare la ricerca delle particelle a partire dai parametri
  // relativi alla particella
  // e ad uno speficifico utilizzo
  public Vector<ParticellaVO> ricercaParticelleByParametriAndUtilizzoSpecificato(
      Long idAzienda, ParticellaVO particellaRicercaVO) throws SolmrException
  {
    return iac.ricercaParticelleByParametriAndUtilizzoSpecificato(idAzienda,
        particellaRicercaVO);
  }

  // Metodo per recuperare l'elenco delle particelle in relazione a dei filtri
  // di ricerca e all'assenza
  // di utilizzi o al fatto che la somma delle superfici utilizzate relative ad
  // una conduzione non
  // sia uguale alla superficie condotta
  public Vector<ParticellaVO> ricercaParticelleByParametriSenzaUsoSuolo(
      ParticellaVO particellaRicercaVO, Long idAzienda) throws SolmrException
  {
    return iac.ricercaParticelleByParametriSenzaUsoSuolo(particellaRicercaVO,
        idAzienda);
  }

  // Metodo per recuperare l'elenco delle fotografie relative alle particelle
  // attraverso i parametri relativi alla particella e l'id dichiarazione di
  // consistenza
  public Vector<ParticellaVO> ricercaParticelleStoricizzateByParametriAndUtilizzo(
      ParticellaVO particellaRicercaVO) throws SolmrException
  {
    return iac
        .ricercaParticelleStoricizzateByParametriAndUtilizzo(particellaRicercaVO);
  }

  // Metodo per effettuare la ricerca delle particelle storicizzate a partire
  // dai parametri relativi alla
  // particella e ad uno speficifico utilizzo
  public Vector<ParticellaVO> ricercaParticelleStoricizzateByParametriAndUtilizzoSpecificato(
      ParticellaVO particellaRicercaVO) throws SolmrException
  {
    return iac
        .ricercaParticelleStoricizzateByParametriAndUtilizzoSpecificato(particellaRicercaVO);
  }

  // Metodo per recuperare l'elenco delle particelle storicizzate in relazione a
  // dei filtri di ricerca e all'assenza
  // di utilizzi o al fatto che la somma delle superfici utilizzate relative ad
  // una conduzione non
  // sia uguale alla superficie condotta
  public Vector<ParticellaVO> ricercaParticelleStoricizzateByParametriSenzaUsoSuolo(
      ParticellaVO particellaRicercaVO) throws SolmrException
  {
    return iac
        .ricercaParticelleStoricizzateByParametriSenzaUsoSuolo(particellaRicercaVO);
  }

  // Metodo per recuperare i tipi ruolo ad esclusione di
  // "TITOLARE/RAPPRESENTANTE LEGALE"
  public Vector<CodeDescription> getTipiRuoloNonTitolareAndNonSpecificato()
      throws SolmrException
  {
    return iac.getTipiRuoloNonTitolareAndNonSpecificato();
  }

  // Metodo per effettuare la sostituzione del vecchio soggetto con il nuovo in
  // tutti i legami tra aziende
  // e persona fisiche
  public void changeLegameBetweenPersoneAndAziende(Long newIdSoggetto,
      Long oldIdSoggetto, Long idAzienda) throws SolmrException
  {
    iac.changeLegameBetweenPersoneAndAziende(newIdSoggetto, oldIdSoggetto, idAzienda);
  }

  // Metodo per cessare il legame tra una persona e l'azienda
  public void cessaLegameBetweenPersonaAndAzienda(Long idSoggetto,
      Long idAzienda) throws SolmrException
  {
    iac.cessaLegameBetweenPersonaAndAzienda(idSoggetto, idAzienda);
  }

  // Metodo per controllare se è possibile modificare un uso del suolo associato
  // ad un'azienda agricola
  public void checkUpdateSuperficie(Long idAzienda) throws SolmrException
  {
    iac.checkUpdateSuperficie(idAzienda);
  }

  // Metodo per recuperare i dati della particella e dell'utilizzo ad essa
  // relativa a partire
  // dall' id utilizzi particella
  public ParticellaVO getParticellaVOByIdUtilizzoParticella(
      Long idUtilizzoParticella) throws SolmrException
  {
    return iac.getParticellaVOByIdUtilizzoParticella(idUtilizzoParticella);
  }

  // Metodo per recuperare i dati della particella a partire dall'
  // id_conduzione_particella
  public ParticellaVO getParticellaVOByIdConduzioneParticella(
      Long idConduzioneParticella) throws SolmrException
  {
    return iac.getParticellaVOByIdConduzioneParticella(idConduzioneParticella);
  }

  // Metodo per recuperare il valore di tutte le superfici utilizzate da
  // un'azienda agricola esclusa
  // quella selezionata
  public double getTotSupUtilizzateByIdCondParticellaNotIncludeIdUtilizzo(
      Long idConduzioneParticella, Long idUtilizzoParticella, String anno)
      throws SolmrException
  {
    return iac.getTotSupUtilizzateByIdCondParticellaNotIncludeIdUtilizzo(
        idConduzioneParticella, idUtilizzoParticella, anno);
  }

  // Metodo per recuperare il valore dalla tabella parametro: importante per la
  // gestione dei terreni
  public String getValoreFromParametroByIdCode(String codice)
      throws SolmrException
  {
    return iac.getValoreFromParametroByIdCode(codice);
  }

  // Metodo per recuperare il valore di tutte le superfici condotte attive
  // partendo dall 'id particella
  // quella della particella su cui si sta lavorando
  public String getTotaleSupCondotteByIdParticellaNotIncludeIdConduzioneParticella(
      Long idConduzioneParticella, Long idParticella) throws SolmrException
  {
    return iac
        .getTotaleSupCondotteByIdParticellaNotIncludeIdConduzioneParticella(
            idConduzioneParticella, idParticella);
  }

  // Metodo per recuperare l'elenco delle dichiarazioni di consistenza
  // attraverso l'id azienda
  public Vector<ParticellaVO> getElencoDichiarazioniConsistenzaByIdAzienda(
      Long idAzienda) throws SolmrException
  {
    return iac.getElencoDichiarazioniConsistenzaByIdAzienda(idAzienda);
  }

  // Metodo per recuperare il valore di tutte le superfici condotte relative ad
  // una dichiarazione di
  // consistenza di un'azienda agricola
  public String getTotaleSupCondotteDichiarateByAzienda(Long idAzienda,
      Long idDichiarazioneConsistenza) throws SolmrException
  {
    return iac.getTotaleSupCondotteDichiarateByAzienda(idAzienda,
        idDichiarazioneConsistenza);
  }

  // Metodo per verificare se, in presenza di particelle ad utilizzo vigneto,
  // sia possibile effettuare
  // l'eliminazione
  public void checkEliminaUtilizziVigneto(
      Vector<String> elencoIdUtilizziParticella) throws SolmrException
  {
    iac.checkEliminaUtilizziVigneto(elencoIdUtilizziParticella);
  }

  // Metodo per controllare le particelle selezionate sono già state inserite
  // all'interno di una
  // dichiarazione di consistenza
  public void checkParticellaLegataDichiarazioneConsistenza(
      Vector<Long> elencoParticelle) throws SolmrException
  {
    iac.checkParticellaLegataDichiarazioneConsistenza(elencoParticelle);
  }

  // Metodo per effettuare la cancellazione degli utilizzi dalla tabella
  // DB_UTILIZZO_PARTICELLA
  // partendo dall'id conduzione particella
  public void deleteUtilizzoParticellaByIdConduzioneParticella(
      Long idConduzioneParticella) throws SolmrException
  {
    iac
        .deleteUtilizzoParticellaByIdConduzioneParticella(idConduzioneParticella);
  }

  // Metodo per effettuare la cancellazione delle conduzioni dalla tabella
  // DB_CONDUZIONE_PARTICELLA
  public void deleteConduzioneParticella(Long idConduzioneParticella)
      throws SolmrException
  {
    iac.deleteConduzioneParticella(idConduzioneParticella);
  }

  // Metodo per effettuare l'eliminazione delle particelle selezionate da un
  // utente a partire
  // dall'id conduzione particella
  public void eliminaParticelle(Vector<Long> elencoConduzioni, Long idAzienda, RuoloUtenza ruoloUtenza)
      throws SolmrException
  {
    iac.eliminaParticelle(elencoConduzioni, idAzienda, ruoloUtenza);
  }

  // Metodo per effettuare la cessazione di un allevamento
  public void storicizzaAllevamento(AllevamentoAnagVO all,
      long idUtenteAggiornamento) throws SolmrException
  {
    iac.storicizzaAllevamento(all, idUtenteAggiornamento);
  }

  // Metodo per effettuare la modifica di un record su DB_FABBRICATO
  public void cessaFabbricato(Long idFabbricato, long idUtenteAggiornamento)
      throws SolmrException
  {
    iac.cessaFabbricato(idFabbricato, idUtenteAggiornamento);
  }

  // Metodo per effettuare la cessazione di una manodopera
  public void storicizzaManodopera(ManodoperaVO manodoperaVO)
      throws SolmrException
  {
    iac.storicizzaManodopera(manodoperaVO);
  }
  
  public Vector<TipoIscrizioneINPSVO> getElencoTipoIscrizioneINPSAttivi()
      throws SolmrException
  {
    return iac.getElencoTipoIscrizioneINPSAttivi();
  }
  
  public ManodoperaVO findManodoperaAttivaByIdAzienda(long idAzienda)
      throws SolmrException
  {
    return iac.findManodoperaAttivaByIdAzienda(idAzienda);
  }

  // Metodo per effettuare la cessazione dell'unità produttiva selezionata
  public void cessazioneUTE(UteVO uteVO, long idUtenteAggiornamento)
      throws SolmrException
  {
    iac.cessazioneUTE(uteVO, idUtenteAggiornamento);
  }

  // Metodo per recuperare i dati della particella a partire dall'
  // id_conduzione_particella e la
  // superficie libera cioè senza uso del suolo specificato
  public ParticellaVO getParticellaVOByIdConduzioneParticellaSenzaUsoSuolo(
      Long idConduzioneParticella, String anno) throws SolmrException
  {
    return iac.getParticellaVOByIdConduzioneParticellaSenzaUsoSuolo(
        idConduzioneParticella, anno);
  }

  // Metodo per recuperare i dati territoriali della particella a partire
  // dall'id conduzione particella
  public ParticellaVO getDettaglioParticellaDatiTerritorialiByIdConduzioneParticella(
      Long idConduzioneParticella) throws SolmrException
  {
    return iac
        .getDettaglioParticellaDatiTerritorialiByIdConduzioneParticella(idConduzioneParticella);
  }

  // Metodo per ribaltare gli utilizzi da una conduzione particella ad un'altra
  public void allegaUtilizziToNewConduzioneParticella(
      Long newIdConduzioneParticella, Long oldIdConduzioneParticella)
      throws SolmrException
  {
    iac.allegaUtilizziToNewConduzioneParticella(newIdConduzioneParticella,
        oldIdConduzioneParticella);
  }

  // Metodo per recuperare gli elementi del dettaglio del soggetto collegato a
  // partire dall'id_contitolare
  public PersonaFisicaVO getDettaglioSoggettoByIdContitolare(Long idContitolare)
      throws SolmrException
  {
    return iac.getDettaglioSoggettoByIdContitolare(idContitolare);
  }
  
  public  TesserinoFitoSanitarioVO getTesserinoFitoSanitario(String codiceFiscale) 
      throws SolmrException
  {
    return iac.getTesserinoFitoSanitario(codiceFiscale);
  }

  // Metodo per effettuare la modifica del ruolo, dei dati della persona e
  // storicizzazione della residenza
  public void updateDatiSoggettoAndStoricizzaResidenza(
      PersonaFisicaVO newPersonaFisicaVO, PersonaFisicaVO oldPersonaFisicaVO,
      long idUtenteAggiornamento) throws SolmrException
  {
    iac.updateDatiSoggettoAndStoricizzaResidenza(newPersonaFisicaVO,
        oldPersonaFisicaVO, idUtenteAggiornamento);
  }

  // Metodo per effettuare la ricerca del terreno in relazione ai parametri di
  // ricerca
  public Vector<ParticellaVO> ricercaTerreniByParametri(
      ParticellaVO particellaRicercaTerrenoVO) throws SolmrException
  {
    return iac.ricercaTerreniByParametri(particellaRicercaTerrenoVO);
  }

  // Metodo verificare se esiste un contenzioso sulla particella selezionata,
  // cioè se sono presenti
  // delle conduzioni aperte a fronte di particelle selezionate su aziende
  // attive
  public boolean isParticellaContenziosoOnAzienda(Long idStoricoParticella)
      throws SolmrException
  {
    return iac.isParticellaContenziosoOnAzienda(idStoricoParticella);
  }

  // Metodo per recuperare la somma delle superfici condotte relativa ad una
  // particella
  public String getTotSupCondotteByIdStoricoParticella(Long idStoricoParticella)
      throws SolmrException
  {
    return iac.getTotSupCondotteByIdStoricoParticella(idStoricoParticella);
  }

  // Metodo per recuperare l'elenco delle azienda e delle conduzioni a partire
  // dall 'id storico
  // particella
  public Vector<ParticellaAziendaVO> getElencoAziendeAndConduzioniByIdStoricoParticella(
      Long idStoricoParticella, boolean attive) throws SolmrException
  {
    return iac.getElencoAziendeAndConduzioniByIdStoricoParticella(
        idStoricoParticella, attive);
  }

  // Metodo per recuperare l'elenco degli utilizzi relativi a conduzioni attive
  // in un anno
  // specificato
  public Vector<ParticellaUtilizzoVO> getElencoUtilizziAttiviByAnnoAndIdStoricoParticella(
      Long idStoricoParticella, String anno) throws SolmrException
  {
    return iac.getElencoUtilizziAttiviByAnnoAndIdStoricoParticella(
        idStoricoParticella, anno);
  }

  public int countParticelleConConduzioniAttive(long idParticella[])
      throws SolmrException
  {
    return iac.countParticelleConConduzioniAttive(idParticella);
  }

  // INIZIO NOTIFICHE

  // Metodo per recuperare i valori della tabella DB_TIPO_TIPOLOGIA_NOTIFICA
  public Vector<CodeDescription> getTipiTipologiaNotifica() throws SolmrException
  {
    return iac.getTipiTipologiaNotifica();
  }
  
  public Vector<CodeDescription> getTipologiaNotificaFromRuolo(RuoloUtenza ruoloUtenza) 
      throws SolmrException
  {
    return iac.getTipologiaNotificaFromRuolo(ruoloUtenza);
  }
  
  public Vector<CodeDescription> getTipiTipologiaNotificaFromEntita(String tipoEntita)
      throws SolmrException
  {
    return iac.getTipiTipologiaNotificaFromEntita(tipoEntita);
  }
  
  public Vector<TipoCategoriaNotificaVO> getTipiCategoriaNotificaFromRuolo(RuoloUtenza ruoloUtenza) 
      throws SolmrException
  {
    return iac.getTipiCategoriaNotificaFromRuolo(ruoloUtenza);
  }
  
  public Vector<TipoCategoriaNotificaVO> getTipiCategoriaNotificaFromEntita(String tipoEntita)
      throws SolmrException
  {
    return iac.getTipiCategoriaNotificaFromEntita(tipoEntita);
  }
  
  public Vector<CodeDescription> getCategoriaNotifica() throws SolmrException
  {
    return iac.getCategoriaNotifica();
  }

  // Metodo per effettuare la ricerca delle notifiche in relazione a dei
  // parametri di ricerca
  public ElencoNotificheVO ricercaNotificheByParametri(NotificaVO notificaVO,
      UtenteAbilitazioni utenteAbilitazioni, RuoloUtenza ruoloUtenza, Boolean storico, int maxRecord) throws SolmrException
  {
    return iac.ricercaNotificheByParametri(notificaVO, utenteAbilitazioni, ruoloUtenza, storico, maxRecord);
  }

  // Metodo per effettuare la ricerca della notifica a partire dalla chiave
  // primaria
  public NotificaVO findNotificaByPrimaryKey(Long idNotifica, String provenienza)
      throws SolmrException
  {
    return iac.findNotificaByPrimaryKey(idNotifica, provenienza);
  }

  // Metodo per recuperare l'elenco delle notifiche in relazione ad un'azienda
  // agricola e ad
  // una situazione(attuale o storica) relative ad un procedimento specificato
  public Vector<NotificaVO> getElencoNotificheByIdAzienda(NotificaVO notificaVO,
      Boolean storico, String ordinamento) throws SolmrException
  {
    return iac.getElencoNotificheByIdAzienda(notificaVO, storico, ordinamento);
  }
  
  public Vector<NotificaVO> getElencoNotifichePopUp(NotificaVO notificaVO) 
      throws SolmrException
  {
    return iac.getElencoNotifichePopUp(notificaVO);
  }
  

  // Metodo per effettuare l'inserimento di una notifica
  public Long insertNotifica(NotificaVO notificaVO, long idUtenteAggiornamento)
      throws SolmrException
  {
    return iac.insertNotifica(notificaVO, idUtenteAggiornamento);
  }
  
  public void updateNotifica(NotificaVO notificaVO, long idUtenteAggiornamento)
      throws Exception, SolmrException
  {
    iac.updateNotifica(notificaVO, idUtenteAggiornamento);
  }

  // Metodo per effettuare la chiusura di una notifica
  public void closeNotifica(NotificaVO notificaVO, long idUtenteAggiornamento)
      throws SolmrException
  {
    iac.closeNotifica(notificaVO, idUtenteAggiornamento);
  }
  
  public Long getIdTipologiaNotificaFromCategoria(Long idCategoriaNotifica)
      throws SolmrException
  {
    return iac.getIdTipologiaNotificaFromCategoria(idCategoriaNotifica);
  }
  
  public boolean isChiusuraNotificaRuoloPossibile(RuoloUtenza ruoloUtenza, long idCategoriaNotifica)
      throws SolmrException
  {
    return iac.isChiusuraNotificaRuoloPossibile(ruoloUtenza, idCategoriaNotifica);
  }
  
  public Vector<NotificaVO> getElencoNotificheForIdentificato(long identificativo, 
      String codiceTipo, long idAzienda, Long idDichiarazioneConsistenza) 
    throws SolmrException
  {
    return iac.getElencoNotificheForIdentificato(identificativo, codiceTipo, 
        idAzienda, idDichiarazioneConsistenza);
  }
  
  public boolean isModificaNotificaRuoloPossibile(RuoloUtenza ruoloUtenza, long idCategoriaNotifica)
      throws SolmrException
  {
    return iac.isModificaNotificaRuoloPossibile(ruoloUtenza, idCategoriaNotifica);
  }
  
  public HashMap<Long,Vector<NotificaEntitaVO>> getNotificheEntitaUvFromIdNotifica(
      long ids[]) throws SolmrException
  {
    return iac.getNotificheEntitaUvFromIdNotifica(ids);
  }
  
  public HashMap<Long,Vector<NotificaEntitaVO>> getNotificheEntitaParticellaFromIdNotifica(
      long ids[]) throws SolmrException
  {
    return iac.getNotificheEntitaParticellaFromIdNotifica(ids);
  }

  // FINE NOTIFICHE

  // INIZIO SEZIONE AZIENDA AGRICOLA
  public void checkStatoAzienda(Long idAzienda) throws SolmrException
  {
    iac.checkStatoAzienda(idAzienda);
  }

  /**
   * Metodo che mi restituisce la MIN relativa alla data inizio validita di
   * un'azienda
   * 
   * @param idAzienda
   * @return java.util.Date
   * @throws SolmrException
   */
  public Date getMinDataInizioValiditaAnagraficaAzienda(Long idAzienda)
      throws SolmrException
  {
    return iac.getMinDataInizioValiditaAnagraficaAzienda(idAzienda);
  }

  /**
   * Metodo utilizzato per effettuare la modifica del record su DB_AZIENDA in
   * funzione dell'OPR delegato al fascicolo
   * 
   * @param anagAziendaVO
   * @throws SolmrException
   */
  public void modificaGestoreFascicolo(AnagAziendaVO anagAziendaVO)
      throws SolmrException
  {
    iac.modificaGestoreFascicolo(anagAziendaVO);
  }

  /**
   * Metodo che mi restituisce la data max di fine mandato
   * 
   * @param idAzienda
   * @return java.util.Date maxDataFineMandato
   * @throws SolmrException
   */
  public java.util.Date getDataMaxFineMandato(Long idAzienda)
      throws SolmrException
  {
    return iac.getDataMaxFineMandato(idAzienda);
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
   * @throws SolmrException
   */
  public Long insertDelegaForMandato(AnagAziendaVO anagAziendaVO,
      RuoloUtenza ruoloUtenza, DelegaVO delegaVO, DocumentoVO documentoVO)
      throws SolmrException
  {
    return iac.insertDelegaForMandato(anagAziendaVO, ruoloUtenza, delegaVO,
        documentoVO);
  }

  /**
   * Metodo che mi restituisce la delega di un dato procedimento relativo ad una
   * determinata azienda agricola
   * 
   * @param idAzienda
   * @param idProcedimento
   * @return it.csi.solmr.dto.anag.DelegaVO
   * @throws SolmrException
   */
  public DelegaVO getDelegaByAziendaAndIdProcedimento(Long idAzienda,
      Long idProcedimento) throws SolmrException
  {
    return iac.getDelegaByAziendaAndIdProcedimento(idAzienda, idProcedimento);
  }

  /**
   * Metodo che mi restituisce la data max di inizio mandato
   * 
   * @param idAzienda
   * @return java.util.Date maxDataFineMandato
   * @throws SolmrException
   */
  public java.util.Date getDataMaxInizioMandato(Long idAzienda)
      throws SolmrException
  {
    return iac.getDataMaxInizioMandato(idAzienda);
  }

  /**
   * Metodo che mi restituisce lo storico delle deleghe di un dato procedimento
   * relativo ad una determinata azienda agricola
   * 
   * @param idAzienda
   * @param idProcedimento
   * @param orderBy[]
   * @return it.csi.solmr.dto.anag.DelegaVO[]
   * @throws SolmrException
   */
  public DelegaVO[] getStoricoDelegheByAziendaAndIdProcedimento(Long idAzienda,
      Long idProcedimento, String[] orderBy) throws SolmrException
  {
    return iac.getStoricoDelegheByAziendaAndIdProcedimento(idAzienda,
        idProcedimento, orderBy);
  }

  // FINE SEZIONE AZIENDA AGRICOLA

  // INIZIO PROFILAZIONE
  public DelegaVO intermediarioConDelega(UtenteAbilitazioni utenteAbilitazioni, Long idAzienda)
      throws SolmrException
  {
    return iac.intermediarioConDelega(utenteAbilitazioni, idAzienda);
  }

  public boolean isIntermediarioConDelegaDiretta(long idIntermediario,
      Long idAzienda) throws SolmrException
  {
    return iac.isIntermediarioConDelegaDiretta(idIntermediario, idAzienda);
  }

  public boolean isIntermediarioPadre(long idIntermediario, Long idAzienda)
      throws SolmrException
  {
    return iac.isIntermediarioPadre(idIntermediario, idAzienda);
  }

  // FINE PROFILAZIONE

  // SIAN

  // Metodo per recuperare le decodifiche dei codici SIAN
  public String getDescriptionSIANFromCode(String tableName, String code)
      throws SolmrException
  {
    return iac.getDescriptionSIANFromCode(tableName, code);
  }

  /**
   * Metodo che si occupa di invocare il webservice SIAN per ottenere i dati
   * dell'anagrafe tributaria e aggiornare le tabelle in anagrafe
   * 
   * @param elencoCuaa
   * @throws SolmrException
   */
  public void sianAggiornaDatiTributaria(String[] elencoCuaa, SianUtenteVO sianUtenteVO)
      throws SolmrException
  {
    iac.sianAggiornaDatiTributaria(elencoCuaa, sianUtenteVO);
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
   */
  public SianTitoloRispostaVO titoliProduttore(String CUAA, String campagna, SianUtenteVO sianUtenteVO)
      throws SolmrException
  {
    return iac.titoliProduttore(CUAA, campagna, sianUtenteVO);
  }

  public SianTitoloRispostaVO titoliProduttoreConInfoPegni(String cuaa, SianUtenteVO sianUtenteVO)
      throws SolmrException
  {
    return iac.titoliProduttoreConInfoPegni(cuaa, sianUtenteVO);
  }

  /**
   * Metodo che mi restituisce l'elenco dei TIPI_OPR decodificati da SIAN
   * 
   * @param principale
   * @param orderBy []
   * @return it.csi.solmr.dto.DoubleStringcodeDescription[]
   * @throws SolmrException
   */
  public DoubleStringcodeDescription[] getListSianTipoOpr(boolean principale,
      String orderBy[]) throws SolmrException
  {
    return iac.getListSianTipoOpr(principale, orderBy);
  }

  /**
   * Metodo che si occupa di invocare il web-service SIAN per l'aggiornamento
   * dei dati della BDN
   * 
   * @param CUAA
   * @throws SolmrException
   */
  public void sianAggiornaDatiBDN(String CUAA) throws SolmrException
  {
    iac.sianAggiornaDatiBDN(CUAA);
  }

  // SIAN

  // TERAMO
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
      String codiceSpecie) throws SolmrException
  {
    return iac.getSianTipoSpecieByCodiceSpecie(codiceSpecie);
  }

  // TERAMO
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
      long idTipoSpecieAnimale) throws SolmrException
  {
    return iac.getSianTipoSpecieByIdSpecieAnimale(idTipoSpecieAnimale);
  }

  /**
   * Metodo per recuperare l'elenco dei CUAA relativi ad un'id_azienda
   * 
   * @param idAzienda
   *          Long
   * @return Vector
   * @throws SolmrException
   */
  public Vector<String> getListCUAAByIdAzienda(Long idAzienda) throws SolmrException
  {
    return iac.getListCUAAByIdAzienda(idAzienda);
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
   * @throws SolmrException
   */
  public String getDenominazioneAziendaByCuaaAndIdAzienda(Long idAzienda,
      String cuaa) throws SolmrException
  {
    return iac.getDenominazioneAziendaByCuaaAndIdAzienda(idAzienda, cuaa);
  }

  public String getOrganismoPagatoreFormatted(String codiceSianOpr)
      throws SolmrException
  {
    return iac.getOrganismoPagatoreFormatted(codiceSianOpr);
  }

  // INIZIO AZIENDA AGRICOLA
  public void updateAnagrafe(AnagAziendaVO anagAziendaVO,
      long idUtenteAggiornamento, PersonaFisicaVO pfVO, boolean isCuaaChanged, 
      PersonaFisicaVO pfVOTributaria, Vector<Long> vIdAtecoTrib) throws SolmrException
  {
    iac.updateAnagrafe(anagAziendaVO, idUtenteAggiornamento, pfVO, isCuaaChanged, pfVOTributaria, vIdAtecoTrib);
  }
  
  public void updateAnagrafeSemplice(AnagAziendaVO anagAziendaVO, long idUtenteAggiornamento) 
      throws SolmrException
  {
    iac.updateAnagrafeSemplice(anagAziendaVO, idUtenteAggiornamento);
  }

  // FINE AZIENDA AGRICOLA

  // Restituisce l'id dello sportello ad un ABI/CAB
  public String getSearchIdSportello(String abi, String cab)
      throws SolmrException
  {
    return iac.getSearchIdSportello(abi, cab);
  }

  // Inserisce un nuovo conto corrente
  public void insertContoCorrente(ContoCorrenteVO conto,
      long idUtenteAggiornamento) throws SolmrException
  {
    iac.insertContoCorrente(conto, idUtenteAggiornamento);
  }
  
  public void storicizzaContoCorrente(ContoCorrenteVO conto, Long idUtente) 
      throws SolmrException
  {
    iac.storicizzaContoCorrente(conto, idUtente);
  }

  // Carica le informazioni su un conto corrente
  public ContoCorrenteVO getContoCorrente(String idContoCorrente)
      throws SolmrException
  {
    return iac.getContoCorrente(idContoCorrente);
  }

  /* Gestione Allevamenti */

  public Vector<AllevamentoAnagVO> getAllevamentiByIdUTE(Long idUTE, int anno)
      throws SolmrException
  {
    return iac.getAllevamentiByIdUTE(idUTE, anno);
  }

  public Vector<Vector<AllevamentoAnagVO>> getAllevamentiByIdAzienda(Long idAzienda, int anno)
      throws SolmrException
  {
    return iac.getAllevamentiByIdAzienda(idAzienda, anno);
  }

  public AllevamentoAnagVO getAllevamento(Long idAllevamento)
      throws SolmrException
  {
    return iac.getAllevamento(idAllevamento);
  }

  public Vector<CategorieAllevamentoAnagVO> getCategorieAllevamento(Long idAllevamento)
      throws SolmrException
  {
    return iac.getCategorieAllevamento(idAllevamento);
  }

  public Long insertAllevamento(AllevamentoAnagVO allevamentoVO,
      long idUtenteAggiornamento) throws SolmrException
  {
    return iac.insertAllevamento(allevamentoVO, idUtenteAggiornamento);
  }

  public Vector<UteVO> getElencoIdUTEByIdAzienda(Long idAzienda) throws SolmrException
  {
    return iac.getElencoIdUTEByIdAzienda(idAzienda);
  }

  public Vector<TipoASLAnagVO> getTipiASL() throws SolmrException
  {
    return iac.getTipiASL();
  }

  public Vector<TipoSpecieAnimaleAnagVO> getTipiSpecieAnimale() throws SolmrException
  {
    return iac.getTipiSpecieAnimale();
  }

  public Vector<TipoSpecieAnimaleAnagVO> getTipiSpecieAnimaleAzProv() throws SolmrException
  {
    return iac.getTipiSpecieAnimaleAzProv();
  }

  public void deleteAllevamentoAll(Long idAllevamento) throws SolmrException
  {
    iac.deleteAllevamentoAll(idAllevamento);
  }

  public Vector<TipoCategoriaAnimaleAnagVO> getCategorieByIdSpecie(Long idSpecie) throws SolmrException
  {
    return iac.getCategorieByIdSpecie(idSpecie);
  }

  public void updateAllevamento(AllevamentoAnagVO all,
      long idUtenteAggiornamento) throws SolmrException
  {
    iac.updateAllevamento(all, idUtenteAggiornamento);
  }

  public Integer[] getAnniByIdAzienda(Long idAzienda) throws SolmrException
  {
    return iac.getAnniByIdAzienda(idAzienda);
  }

  public Vector<CodeDescription> getTipoTipoProduzione(long idSpecie) throws SolmrException
  {
    return iac.getTipoTipoProduzione(idSpecie);
  }

  public Vector<CodeDescription> getOrientamentoProduttivo(long idSpecie, long idTipoProduzione)
      throws SolmrException
  {
    return iac.getOrientamentoProduttivo(idSpecie, idTipoProduzione);
  }
  
  public Vector<CodeDescription> getTipoProduzioneCosman(long idSpecie, long idTipoProduzione, long idOrientamentoProduttivo) 
      throws SolmrException
  {
    return iac.getTipoProduzioneCosman(idSpecie, idTipoProduzione, idOrientamentoProduttivo);
  }
  
  public Vector<CodeDescription> getSottocategorieCosman(long idSpecie, 
      long idTipoProduzione, long idOrientamentoProduttivo, long idTipoProduzioneCosman, String flagEsiste)
      throws SolmrException
  {
    return iac.getSottocategorieCosman(idSpecie, idTipoProduzione, idOrientamentoProduttivo, idTipoProduzioneCosman, flagEsiste);
  }

  public Long storicizzaDatiResidenza(PersonaFisicaVO pfVO, long idUtenteAggiornamento)
      throws SolmrException
  {
    return iac.storicizzaDatiResidenza(pfVO, idUtenteAggiornamento);
  }

  // Metodo che mi restituisce un elenco di allevamenti secondo un ordine
  // stabilito
  // (Metodo introdotto per correggere baco di ordinamento non risolvibile con
  // la struttura creata
  // in partenza) Mauro Vocale 24/01/2005
  public Vector<AllevamentoAnagVO> getAllevamentiByIdAziendaOrdinati(Long idAzienda, int anno)
      throws SolmrException
  {
    return iac.getAllevamentiByIdAziendaOrdinati(idAzienda, anno);
  }

  // Metodo che mi restituisce i dati relativi al tipo specie animale a partire
  // dall'id
  public TipoSpecieAnimaleAnagVO getTipoSpecieAnimale(Long idSpecieAnimale)
      throws SolmrException
  {
    return iac.getTipoSpecieAnimale(idSpecieAnimale);
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
   * @throws SolmrException
   */
  public boolean isRecordSianInAnagrafe(AnagAziendaVO anagAziendaVO,
      SianAllevamentiVO sianAllevamentiVO) throws SolmrException
  {
    return iac.isRecordSianInAnagrafe(anagAziendaVO, sianAllevamentiVO);
  }
  
  public String getRegioneByProvincia(String siglaProvincia) throws SolmrException
  {
    return iac.getRegioneByProvincia(siglaProvincia);
  }
  
  /**
   * Metodo per recuperare l'istat della provincia a partire dalla sigla
   * 
   * @param siglaProvincia
   *          String
   * @return String
   * @throws SolmrException
   */
  public String getIstatProvinciaBySiglaProvincia(String siglaProvincia)
      throws SolmrException
  {
    return iac.getIstatProvinciaBySiglaProvincia(siglaProvincia);
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
   * @throws SolmrException
   */
  public Vector<Long> getListIdUteByIstatComuneAndIdAzienda(String istatComune,
      Long idAzienda, boolean isActive) throws SolmrException
  {
    return iac.getListIdUteByIstatComuneAndIdAzienda(istatComune, idAzienda,
        isActive);
  }

  /**
   * Metodo per recuperare il tipo asl a partire dall'id_amm_competenza
   * 
   * @param idAmmCompetenza
   *          Long
   * @param isActive
   *          boolean
   * @return TipoASLAnagVO
   * @throws SolmrException
   */
  public TipoASLAnagVO getTipoASLAnagVOByExtIdAmmCompetenza(
      Long idAmmCompetenza, boolean isActive) throws SolmrException
  {
    return iac.getTipoASLAnagVOByExtIdAmmCompetenza(idAmmCompetenza, isActive);
  }

  /**
   * Metodo che mi permette di recuperare l'elenco degli allevamenti di
   * un'azienda agricola relativi ad un determinato piano di riferimento
   * 
   * @param idAzienda
   * @param idPianoRiferimento
   * @param orderBy
   * @return it.csi.solmr.dto.anag.AllevamentoAnagVO[]
   * @throws SolmrException
   */
  public AllevamentoAnagVO[] getListAllevamentiAziendaByPianoRifererimento(
      Long idAzienda, Long idPianoRiferimento, Long idUte, String[] orderBy)
      throws SolmrException
  {
    return iac.getListAllevamentiAziendaByPianoRifererimento(idAzienda,
        idPianoRiferimento, idUte, orderBy);
  }
  
  public TipoCategoriaAnimaleAnagVO getTipoCategoriaAnimale(Long idCategoriaAnimale)
      throws SolmrException
  {
    return iac.getTipoCategoriaAnimale(idCategoriaAnimale);
  }

  /* Gestione Allevamenti */

  /**
   * Elenco Manodopera
   * 
   * @param manodoperaVO
   *          ManodoperaVO
   * @return Vector
   * @throws SolmrException
   */
  public Vector<FrmManodoperaVO> getManodoperaAnnua(ManodoperaVO manodoperaVO)
      throws SolmrException
  {
    return iac.getManodoperaAnnua(manodoperaVO);
  }

  public Vector<FrmManodoperaVO> getManodoperaByPianoRifererimento(ManodoperaVO manodoperaVO,
      Long idPianoRiferimento) throws SolmrException
  {
    return iac.getManodoperaByPianoRifererimento(manodoperaVO,
        idPianoRiferimento);
  }

  /**
   * Dettaglio Manodopera
   * 
   * @param idManodopera
   *          Long
   * @return ManodoperaVO
   * @throws SolmrException
   */
  public ManodoperaVO dettaglioManodopera(Long idManodopera)
      throws SolmrException
  {
    return iac.dettaglioManodopera(idManodopera);
  }

  /**
   * Cancellazione di tutti i dati relativi alla manodopera
   * 
   * @param idManodopera
   *          Long
   * @throws SolmrException
   */
  public void deleteManodopera(Long idManodopera) throws SolmrException
  {
    iac.deleteManodopera(idManodopera);
  }

  /**
   * Inserimento di tutti i dati relativi alla manodopera
   * 
   * @param manodoperaVO
   *          ManodoperaVO
   * @param idAzienda
   *          Long
   * @throws SolmrException
   */
  public void insertManodopera(ManodoperaVO manodoperaVO, Long idAzienda)
      throws SolmrException
  {
    iac.insertManodopera(manodoperaVO, idAzienda);
  }

  /**
   * Tipo Forma Conduzione
   * 
   * @return Vector
   * @throws SolmrException
   */
  public Vector<CodeDescription> getTipoFormaConduzione() throws SolmrException
  {
    return iac.getTipoFormaConduzione();
  }

  /**
   * Tipo Attivita Complementari
   * 
   * @return Vector
   * @throws SolmrException
   */
  public Vector<CodeDescription> getTipoAttivitaComplementari() throws SolmrException
  {
    return iac.getTipoAttivitaComplementari();
  }

  /**
   * Tipo Classi Manodopera
   * 
   * @return Vector
   * @throws SolmrException
   */
  public Vector<CodeDescription> getTipoClassiManodopera() throws SolmrException
  {
    return iac.getTipoClassiManodopera();
  }

  /**
   * Ultima dichiarazione di manodopera non valida
   * 
   * @return ManodoperaVO
   * @throws SolmrException
   * @param idAzienda
   *          Long
   */
  public ManodoperaVO findLastManodopera(Long idAzienda) throws SolmrException
  {
    return iac.findLastManodopera(idAzienda);
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
   */
  public String isManodoperaValida(Long idManodopera, Long idAzienda)
      throws SolmrException
  {
    return iac.isManodoperaValida(idManodopera, idAzienda);
  }

  /**
   * Modifica di tutti i dati relativi alla manodopera
   * 
   * @param manodoperaVO
   *          ManodoperaVO
   * @param idAzienda
   *          Long
   * @throws SolmrException
   */
  public void updateManodopera(ManodoperaVO manodoperaVO, Long idAzienda)
      throws SolmrException
  {
    iac.updateManodopera(manodoperaVO, idAzienda);
  }
  
  public void updateManodoperaSian(ManodoperaVO manodoperaChgVO, long idAzienda, long idUtente)
      throws SolmrException
  {
    iac.updateManodoperaSian(manodoperaChgVO, idAzienda, idUtente);
  }

  // Ritorna i comuni legati all'azienda
  /*public Vector getComuniAzienda(Long idAzienda) throws SolmrException
  {
    return iac.getComuniAzienda(idAzienda);
  }*/

  /**
   * Contratti ******************
   * 
   * @param idAzienda
   *          long
   * @param dataInizio
   *          Date
   * @throws SolmrException
   * @return boolean
   */
  public boolean isDataInizioValida(long idAzienda, Date dataInizio)
      throws SolmrException
  {
    return iac.isDataInizioValida(idAzienda, dataInizio);
  }

  public Vector<ParticellaVO> getElencoParticelleForAziendaAndUtilizzo(Long idAzienda,
      String anno) throws SolmrException
  {
    return iac.getElencoParticelleForAziendaAndUtilizzo(idAzienda, anno);
  }

  public boolean controllaRegistrazioneMandato(AnagAziendaVO aziendaVO,
      String codiceEnte, DelegaAnagrafeVO delegaAnagrafeVO)
      throws SolmrException
  {
    return iac.controllaRegistrazioneMandato(aziendaVO, codiceEnte,
        delegaAnagrafeVO);
  }
  
  
  public boolean controllaRevocaMandato(AnagAziendaVO aziendaVO,
      RuoloUtenza ruoloUtenza, DelegaAnagrafeVO delegaAnagrafeVO)
      throws SolmrException
  {
    return iac.controllaRevocaMandato(aziendaVO, ruoloUtenza,
        delegaAnagrafeVO);
  }

  public boolean controllaPresenzaDelega(AnagAziendaVO aziendaVO)
      throws SolmrException
  {
    return iac.controllaPresenzaDelega(aziendaVO);
  }

  /* Consistenza */
  public boolean previsioneAnnoSucessivo(Long idAzienda) throws SolmrException
  {
    return iac.previsioneAnnoSucessivo(idAzienda);
  }

  public boolean controlloUltimeModifiche(Long idAzienda, Integer anno)
      throws SolmrException
  {
    return iac.controlloUltimeModifiche(idAzienda, anno);
  }

  public String controlliDichiarazionePLSQL(Long idAzienda, Integer anno,
      Long idMotivoDichiarazione, Long idUtente) throws SolmrException
  {
    return iac.controlliDichiarazionePLSQL(idAzienda, anno,
        idMotivoDichiarazione, idUtente);
  }

  public void controlliParticellarePLSQL(Long idAzienda, Integer anno, Long idUtente)
      throws SolmrException
  {
    iac.controlliParticellarePLSQL(idAzienda, anno, idUtente);
  }

  public String controlliVerificaPLSQL(Long idAzienda, Integer anno,
      Integer idGruppoControllo, Long idUtente) throws SolmrException
  {
    return iac.controlliVerificaPLSQL(idAzienda, anno, idGruppoControllo, idUtente);
  }

  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichiarazioneConsistenza(Long idAzienda,
      Long idMotivoDichiarazione) throws SolmrException
  {
    return iac.getErroriAnomalieDichiarazioneConsistenza(idAzienda,
        idMotivoDichiarazione);
  }
  
  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichConsPG(long idDichiarazioneConsistenza, long fase) 
      throws SolmrException
  {
    return iac.getErroriAnomalieDichConsPG(idDichiarazioneConsistenza, fase);
  }

  public ErrAnomaliaDicConsistenzaVO getAnomaliaDichiarazioneConsistenza(
      Long idDichiarazioneSegnalazione) throws SolmrException
  {
    return iac.getAnomaliaDichiarazioneConsistenza(idDichiarazioneSegnalazione);
  }

  public Vector<ErrAnomaliaDicConsistenzaVO> getAnomaliePerCorrezione(
      Long elencoIdDichiarazioneSegnalazione[], long idMotivoDichiarazione)
      throws SolmrException
  {
    return iac.getAnomaliePerCorrezione(elencoIdDichiarazioneSegnalazione,
        idMotivoDichiarazione);
  }

  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichConsistenzaTerreni(Long idAzienda)
      throws SolmrException
  {
    return iac.getErroriAnomalieDichConsistenzaTerreni(idAzienda);
  }

  /**
   * Metodo utilizzato per ripristinare il piano di riferimento con la
   * dichiarazione di consistenza selezionata
   * 
   * @param idDichiarazioneConsistenza
   * @param idUtentte
   * 
   * @throws SolmrException
   */
  public void ripristinaPianoRiferimento(Long idDichiarazioneConsistenza,
      Long idUtente) throws SolmrException
  {
    iac.ripristinaPianoRiferimento(idDichiarazioneConsistenza, idUtente);
  }

  
  public Vector<TipoMotivoDichiarazioneVO> getListTipoMotivoDichiarazione(long idAzienda) 
      throws SolmrException
  {
    return iac.getListTipoMotivoDichiarazione(idAzienda);
  }

  /**
   * Metodo per recuperare la dichiarazione di consistenza a partire dalla sua
   * chiave primaria
   * 
   * @param idDichiarazioneConsistenza
   * @return it.csi.solmr.dto.anag.ConsistenzaVO
   * @throws SolmrException
   */
  public ConsistenzaVO findDichiarazioneConsistenzaByPrimaryKey(
      Long idDichiarazioneConsistenza) throws SolmrException
  {
    return iac
        .findDichiarazioneConsistenzaByPrimaryKey(idDichiarazioneConsistenza);
  }
  
  public FascicoloNazionaleVO getInfoRisultatiSianDichiarazioneConsistenza(
      long idDichiarazioneConsistenza) throws SolmrException
  {
    return iac
        .getInfoRisultatiSianDichiarazioneConsistenza(idDichiarazioneConsistenza);
  }
  
  public Vector<ConsistenzaVO> getListDichiarazioniPianoGrafico(Long idAzienda)
      throws SolmrException
  {
    return iac.getListDichiarazioniPianoGrafico(idAzienda);
  }
  
  public EsitoPianoGraficoVO getEsitoPianoGraficoFromAccesso(long idAccessoPianoGrafico)
      throws SolmrException
  {
    return iac.getEsitoPianoGraficoFromAccesso(idAccessoPianoGrafico);
  }
  
  public int preCaricamentoPianoGrafico(long idDichiarazioneConsistenza) throws SolmrException
  {
    return iac.preCaricamentoPianoGrafico(idDichiarazioneConsistenza);
  }
  
  public EsitoPianoGraficoVO getEsitoPianoGraficoFromPK(long idEsitoGrafico) throws SolmrException
  {
    return iac.getEsitoPianoGraficoFromPK(idEsitoGrafico);
  }
  
  public Long insertStatoIncorsoPG(long idAccessoPianoGrafico, long idUtente) 
      throws SolmrException
  {
    return iac.insertStatoIncorsoPG(idAccessoPianoGrafico, idUtente);
  }

  public PlSqlCodeDescription controlliValidazionePlSql(long idAzienda, int idFase, 
      long idUtente, long idDichiarazioneConsistenza) throws SolmrException
  {
    return iac.controlliValidazionePlSql(idAzienda, idFase, idUtente, idDichiarazioneConsistenza);
  }
  
  /**
   * Metodo che si occupa di effettuare la protocollazione delle dichiarazioni
   * di consistenza
   * 
   * @param elencoIdDichiarazioniConsistenza
   * @param ruoloUtenza
   * @param anno
   * @throws SolmrException
   */
  public void protocollaDichiarazioniConsistenza(
      Long[] elencoIdDichiarazioniConsistenza, RuoloUtenza ruoloUtenza,
      String anno) throws SolmrException
  {
    iac.protocollaDichiarazioniConsistenza(elencoIdDichiarazioniConsistenza,
        ruoloUtenza, anno);
  }

  /**
   * Metodo per recuperare tutti i tipi controlli relativi ad un determinato
   * gruppo controllo
   * 
   * @param idGruppoControllo
   * @param orderBy
   * @return
   * @throws SolmrException
   */
  public TipoControlloVO[] getListTipoControlloByIdGruppoControllo(
      Long idGruppoControllo, String orderBy[]) throws SolmrException
  {
    return iac.getListTipoControlloByIdGruppoControllo(idGruppoControllo,
        orderBy);
  }
  
  public TipoControlloVO[] getListTipoControlloByIdGruppoControlloAttivi(
      Long idGruppoControllo, String orderBy[]) throws SolmrException
  {
    return iac.getListTipoControlloByIdGruppoControlloAttivi(idGruppoControllo,
        orderBy);
  }
  
  public TipoControlloVO[] getListTipoControlloForAziendaByIdGruppoControllo(
      Long idGruppoControllo, Long idAzienda, String orderBy[]) throws SolmrException
  {
    return iac.getListTipoControlloForAziendaByIdGruppoControllo(idGruppoControllo,
        idAzienda, orderBy);
  }
  
  public TipoControlloVO[] getListTipoControlloForAziendaByIdGruppoControlloAndDichCons(
      Long idGruppoControllo, Long idDichiarazioneConsistenza, String orderBy[])
          throws SolmrException
  {
    return iac.getListTipoControlloForAziendaByIdGruppoControlloAndDichCons(
        idGruppoControllo, idDichiarazioneConsistenza, orderBy);
  }

  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieForControlliTerreni(Long idAzienda,
      Long idControllo, Vector<String> vTipoErrori, boolean flagOK, String ordinamento)
      throws SolmrException
  {
    return iac.getErroriAnomalieForControlliTerreni(idAzienda, idControllo,
        vTipoErrori, flagOK, ordinamento);
  }

  public String getLastAnnoCampagnaFromDichCons(long idAzienda)
      throws SolmrException
  {
    return iac.getLastAnnoCampagnaFromDichCons(idAzienda);
  }

  public TipoMotivoDichiarazioneVO findTipoMotivoDichiarazioneByPrimaryKey(
      Long idMotivoDichiarazione) throws SolmrException
  {
    return iac.findTipoMotivoDichiarazioneByPrimaryKey(idMotivoDichiarazione);
  }
  
  public String getLastDichConsNoCorrettiva(long idAzienda)
    throws SolmrException
  {
    return iac.getLastDichConsNoCorrettiva(idAzienda);
  }
  
  public ConsistenzaVO getUltimaDichConsNoCorrettiva(long idAzienda) throws SolmrException
  {
    return iac.getUltimaDichConsNoCorrettiva(idAzienda);
  }
  
  public Date getLastDateDichConsNoCorrettiva(long idAzienda)
    throws SolmrException
  {
    return iac.getLastDateDichConsNoCorrettiva(idAzienda);
  }
  
  public Long getLastIdDichConsProtocollata(long idAzienda)
      throws SolmrException
  {
    return iac.getLastIdDichConsProtocollata(idAzienda);
  }
  
  public void updateDichiarazioneConsistenzaRichiestaStampa(Long idDichiarazioneConsistenza)
      throws SolmrException
  {
    iac.updateDichiarazioneConsistenzaRichiestaStampa(idDichiarazioneConsistenza);
  }

  public ConsistenzaVO getUltimaDichiarazioneConsistenza(Long idAzienda)
      throws SolmrException
  {
    return iac.getUltimaDichiarazioneConsistenza(idAzienda);
  }

  /* Consistenza */

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
      throws SolmrException
  {
    return iac.importaDatiAAEP(anagAAEPAziendaVO, anagAziendaVO, idUtenteAggiornamento,
        denominazione, partitaIVA, descrizioneAteco, provinciaREA, numeroREA,
        annoIscrizione, numeroRegistroImprese, pec, sedeLegale,
        titolareRappresentante, formaGiuridica, sezione,
        descrizioneAtecoSec, dataIscrizioneREA, dataCancellazioneREA, dataIscrizioneRI);
  }

  /** *********************************************************************** */
  /** ****************************** AAEP *********************************** */
  /**
   * 
   * @param idIntermediario
   *          Long
   * @throws SolmrException
   * @return DelegaVO
   */
  public DelegaVO getIntermediarioPerDelega(Long idIntermediario)
      throws SolmrException
  {
    return iac.getIntermediarioPerDelega(idIntermediario);
  }

  public IntermediarioAnagVO getIntermediarioAnagByIdIntermediario(
      long idIntermediario) throws SolmrException
  {
    return iac.getIntermediarioAnagByIdIntermediario(idIntermediario);
  }
  
  public IntermediarioAnagVO findIntermediarioVOByCodiceEnte(String codEnte)
      throws SolmrException
  {
    return iac.findIntermediarioVOByCodiceEnte(codEnte);
  }
  
  public IntermediarioAnagVO findIntermediarioVOByIdAzienda(long idAzienda)
      throws SolmrException
  {
    return iac.findIntermediarioVOByIdAzienda(idAzienda);
  }
  
  public boolean isAziendaIntermediario(long idAzienda)
      throws SolmrException
  {
    return iac.isAziendaIntermediario(idAzienda);
  }
  
  public Vector<IntermediarioAnagVO> findFigliIntermediarioVOById(long idIntemediario)
      throws SolmrException
  {
    return iac.findFigliIntermediarioVOById(idIntemediario);
  }
  
  public Vector<IntermediarioAnagVO> findFigliIntermediarioAziendaVOById(long idIntemediario)
      throws SolmrException
  {
    return iac.findFigliIntermediarioAziendaVOById(idIntemediario);
  }

  public Long salvataggioDichiarazionePLQSL(ConsistenzaVO consistenzaVO,
      Long idAzienda, Integer anno, RuoloUtenza ruoloUtenza)
      throws SolmrException
  {
    return iac.salvataggioDichiarazionePLQSL(consistenzaVO, idAzienda, anno,
        ruoloUtenza);
  }

  public Vector<ConsistenzaVO> getDichiarazioniConsistenza(Long idAzienda)
      throws SolmrException
  {
    return iac.getDichiarazioniConsistenza(idAzienda);
  }

  public Vector<ConsistenzaVO> getDichiarazioniConsistenzaMinimo(Long idAzienda)
      throws SolmrException
  {
    return iac.getDichiarazioniConsistenzaMinimo(idAzienda);
  }

  public ConsistenzaVO getDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza) throws SolmrException
  {
    return iac.getDichiarazioneConsistenza(idDichiarazioneConsistenza);
  }

  /**
   * Metodo che mi restituisce l'elenco delle segnalazioni relative all'azienda,
   * ad una determinata dichiarazione di consistenza di una particella
   * 
   * @param idAzienda
   * @param idDichiarazioneConsistenza
   * @param idStoricoParticella
   * @return java.util.Vector
   * @throws SolmrException
   */
  public Vector<DichiarazioneSegnalazioneVO> getListDichiarazioneSegnalazioni(Long idAzienda,
      Long idDichiarazioneConsistenza, Long idStoricoParticella)
      throws SolmrException
  {
    return iac.getListDichiarazioneSegnalazioni(idAzienda,
        idDichiarazioneConsistenza, idStoricoParticella);
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
   * @throws SolmrException
   */
  public Vector<DichiarazioneSegnalazioneVO> getListDichiarazioneSegnalazioniUnar(Long idAzienda,
      Long idDichiarazioneConsistenza, Long idStoricoUnitaArborea,
      String[] orderBy) throws SolmrException
  {
    return iac.getListDichiarazioneSegnalazioniUnar(idAzienda,
        idDichiarazioneConsistenza, idStoricoUnitaArborea, orderBy);
  }
  
  public Vector<DichiarazioneSegnalazioneVO> getListDichiarazioneSegnalazioniConsolidamentoUV(
      Long idAzienda) throws SolmrException
  {
    return iac.getListDichiarazioneSegnalazioniConsolidamentoUV(idAzienda);
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
   * @throws SolmrException
   */
  public ErrAnomaliaDicConsistenzaVO[] getListAnomalieByIdDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza, Long idFase,
      ErrAnomaliaDicConsistenzaVO errAnomaliaDicConsistenzaRicercaVO,
      String[] orderBy) throws SolmrException
  {
    return iac.getListAnomalieByIdDichiarazioneConsistenza(
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
   * @throws SolmrException
   */
  public ConsistenzaVO[] getListDichiarazioniConsistenzaByIdAzienda(
      Long idAzienda, String[] orderBy) throws SolmrException
  {
    return iac.getListDichiarazioniConsistenzaByIdAzienda(idAzienda, orderBy);
  }
  
  public ConsistenzaVO[] getListDichiarazioniConsistenzaByIdAziendaVarCat(
      Long idAzienda, String[] orderBy) throws SolmrException
  {
    return iac.getListDichiarazioniConsistenzaByIdAziendaVarCat(idAzienda, orderBy);
  }

  /* Consistenza */

  /** *************** */
  /** *************** */
  /** *** Stampe **** */
  /** *************** */

  public Vector<ParticellaVO> getElencoParticelleQuadroI1(Long idAzienda,
      Long codFotografia) throws SolmrException
  {
    return iac.getElencoParticelleQuadroI1(idAzienda, codFotografia);
  }

  public BigDecimal[] getTotSupQuadroI1CondottaAndAgronomica(Long idAzienda, Long codFotografia)
      throws SolmrException
  {
    return iac.getTotSupQuadroI1CondottaAndAgronomica(idAzienda, codFotografia);
  }
  
  public BigDecimal[] getTotSupQuadroI1CatastaleAndGrafica(Long idAzienda, Long codFotografia)
    throws SolmrException
  {
    return iac.getTotSupQuadroI1CatastaleAndGrafica(idAzienda, codFotografia);
  }

  public Long getCodFotTerreniQuadroI1(Long idDichiarazioneConsistenza)
      throws SolmrException
  {
    return iac.getCodFotTerreniQuadroI1(idDichiarazioneConsistenza);
  }

  public Vector<UteVO> getUteQuadroB(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException
  {
    return iac.getUteQuadroB(idAzienda, dataRiferimento);
  }

  public Vector<TipoFormaConduzioneVO> getFormeConduzioneQuadroD()
      throws SolmrException
  {
    return iac.getFormeConduzioneQuadroD();
  }

  public Long getFormaConduzioneQuadroD(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException
  {
    return iac.getFormaConduzioneQuadroD(idAzienda, dataRiferimento);
  }

  public Vector<CodeDescription> getAttivitaComplementariQuadroE(
      Long idAzienda, java.util.Date dataRiferimento) throws SolmrException
  {
    return iac.getAttivitaComplementariQuadroE(idAzienda, dataRiferimento);
  }

  public Long getIdManodoperaQuadroF(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException
  {
    return iac.getIdManodoperaQuadroF(idAzienda, dataRiferimento);
  }

  public Vector<Long> getAllevamentiQuadroG(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException
  {
    return iac.getAllevamentiQuadroG(idAzienda, dataRiferimento);
  }

  public Vector<BaseCodeDescription> getTerreniQuadroI5(Long idAzienda,
      java.util.Date dataRiferimento, Long codFotografia) throws SolmrException
  {
    return iac.getTerreniQuadroI5(idAzienda, dataRiferimento, codFotografia);
  }

  public Vector<BaseCodeDescription> getTerreniQuadroI4(Long idAzienda,
      Long codFotografia) throws SolmrException
  {
    return iac.getTerreniQuadroI4(idAzienda, codFotografia);
  }

  public Vector<ParticellaVO> getFabbricatiParticelle(Long idFabbricato,
      java.util.Date dataRiferimento) throws SolmrException
  {
    return iac.getFabbricatiParticelle(idFabbricato, dataRiferimento);
  }
  
  public Vector<FabbricatoVO> getFabbricati(
      Long idAzienda, java.util.Date dataRiferimento,boolean comunicazione10R) 
      throws SolmrException
  {
    return iac.getFabbricati(idAzienda, dataRiferimento, comunicazione10R);
  }

  public Vector<ConsistenzaZootecnicaStampa> getAllevamentiQuadroC10R(
      Long idAzienda, java.util.Date dataRiferimento) throws SolmrException
  {
    return iac.getAllevamentiQuadroC10R(idAzienda, dataRiferimento);
  }

  public Vector<QuadroDTerreni> getTerreniQuadroD10R(Long idAzienda)
      throws SolmrException
  {
    return iac.getTerreniQuadroD10R(idAzienda);
  }

  public Vector<QuadroDTerreni> getTerreniQuadroD10R(
      java.util.Date dataRiferimento, Long codFotografia) throws SolmrException
  {
    return iac.getTerreniQuadroD10R(dataRiferimento, codFotografia);
  }
  
  public Vector<String[]> getAnomalie(Long idAzienda, Long idDichiarazioneConsistenza)
     throws SolmrException
  {
    return iac.getAnomalie(idAzienda, idDichiarazioneConsistenza);
  }
  
  public Vector<DocumentoVO> getDocumentiStampa(Long idAzienda, Long idDichiarazioneConsistenza,
      String cuaa, java.util.Date dataInserimentoDichiarazione)
    throws SolmrException
  {
    return iac.getDocumentiStampa(
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
   * @throws SolmrException
   * @return DittaUMAVO
   */
  public DittaUMAVO getDittaUmaByIdAzienda(Long idAzienda)
      throws SolmrException
  {
    return iac.getDittaUmaByIdAzienda(idAzienda);
  }

  /**
   * Metodo che si occupa di storicizzare il mandato di delega in anagrafe, di
   * fatto revocandolo
   * 
   */
  public void storicizzaDelega(DelegaVO dVO, RuoloUtenza ruoloUtenza,
      DocumentoVO documentoVO, AnagAziendaVO anagAziendaVO)
      throws SolmrException
  {
    iac.storicizzaDelega(dVO, ruoloUtenza, documentoVO, anagAziendaVO);
  }
  
  public int storicizzaDelegaTemporanea(DelegaVO delegaVO, RuoloUtenza ruoloUtenza, 
      AnagAziendaVO anagAziendaVO) throws SolmrException
  {
    return iac.storicizzaDelegaTemporanea(delegaVO, ruoloUtenza, anagAziendaVO);
  }
  
  
  
  // ** INIZIO SERVIZI INFOCAMERE
    
  public Azienda cercaPerCodiceFiscale(String codiceFiscale)throws SolmrException, Exception{
	  return iac.cercaPerCodiceFiscale(codiceFiscale); 
  }
  
  public it.csi.solmr.ws.infoc.Sede cercaPuntualeSede(String codiceFiscale, String progrSede, String codFonte) throws SolmrException, Exception{
	  return iac.cercaPuntualeSede(codiceFiscale,progrSede,codFonte);
  }
  
  public it.csi.solmr.ws.infoc.PersonaRIInfoc cercaPuntualePersona(String codiceFiscale, String progrPersona, String codFonte) throws SolmrException, Exception{
	  return iac.cercaPuntualePersona(codiceFiscale, progrPersona, codFonte);
  }
  
  public List<it.csi.solmr.ws.infoc.ListaPersonaCaricaInfoc> cercaPerFiltriCodFiscFonte(String codiceFiscale, String codFonte) throws SolmrException, Exception{
	  return iac.cercaPerFiltriCodFiscFonte(codiceFiscale, codFonte);
  }
  
  public ParametroRitornoVO serviceGetAziendeInfocAnagrafe(String codiceFiscale,boolean controllaPresenzaSuAAEP, Boolean bloccaAssenzaAAEP,Boolean controllaLegameRLsuAnagrafe,boolean controllaPresenzaValidazione, boolean aziendeAttive)throws SolmrException, ServiceSystemException, SystemException,Exception{
    return iac.serviceGetAziendeInfocAnagrafe(codiceFiscale, controllaPresenzaSuAAEP, bloccaAssenzaAAEP, controllaLegameRLsuAnagrafe, controllaPresenzaValidazione,  aziendeAttive);
  }
  
  
  public ParametroRitornoVO serviceGetAziendeInfocAnagrafe(String codiceFiscale,
	      boolean controllaPresenzaSuAAEP, Boolean bloccaAssenzaAAEP,
	      boolean controllaLegameRLsuAnagrafe, boolean controllaPresenzaValidazione)
	      throws SolmrException, ServiceSystemException, SystemException,
	      Exception{
	  return iac.serviceGetAziendeInfocAnagrafe(codiceFiscale,
		       controllaPresenzaSuAAEP,  bloccaAssenzaAAEP,
		       controllaLegameRLsuAnagrafe,  controllaPresenzaValidazione);
  }
  
  
  
  // ** FINE SERVIZI INFOCAMERE
  

 


  public Boolean aggiornaDatiAAEP(String CUAA) throws SystemException,
      SolmrException
  {
    return iac.aggiornaDatiAAEP(CUAA);
  }

  public String isFlagPrevalente(Long[] idAteco) throws Exception
  {
    return iac.isFlagPrevalente(idAteco);
  }
  
  public TipoSezioniAaepVO getTipoSezioneAaepByCodiceSez(String codiceSezione)
      throws Exception
  {
    return iac.getTipoSezioneAaepByCodiceSez(codiceSezione);
  }
  
  public void deleteDichiarazioneConsistenza(Long idDichiarazioneConsistenza)
      throws SolmrException
  {
    iac.deleteDichiarazioneConsistenza(idDichiarazioneConsistenza);
  }

  public void updateInsediamentoGiovani(Long idAzienda) throws SolmrException
  {
    iac.updateInsediamentoGiovani(idAzienda);
  }

  // Metodo per recuperare l'elenco dei terreni su cui l'azienda ha o ha avuto
  // dei terreni
  public Vector<StringcodeDescription> getListaComuniTerreniByIdAzienda(Long idAzienda)
      throws SolmrException
  {
    return iac.getListaComuniTerreniByIdAzienda(idAzienda);
  }

  // Metodo per recuperare il totale delle superfici condotte attive associate
  // alla particella
  public String getTotaleSupCondotteAttiveByIdParticella(Long idParticella,
      Long idAzienda) throws SolmrException
  {
    return iac
        .getTotaleSupCondotteAttiveByIdParticella(idParticella, idAzienda);
  }

  public String[] cessazioneAziendaPLQSL(Long idAzienda) throws SolmrException
  {
    return iac.cessazioneAziendaPLQSL(idAzienda);
  }

  public boolean controllaObbligoFascicolo(AnagAziendaVO aziendaVO)
      throws SolmrException
  {
    return iac.controllaObbligoFascicolo(aziendaVO);
  }

  public Vector<CodeDescription> getElencoUfficiZonaIntermediarioByIdIntermediario(
      UtenteAbilitazioni utenteAbilitazioni) throws SolmrException
  {
    return iac.getElencoUfficiZonaIntermediarioByIdIntermediario(utenteAbilitazioni);
  }
  
  public Vector<CodeDescription> getElencoUfficiZonaIntermediarioAttiviByIdIntermediario(
      UtenteAbilitazioni utenteAbilitazioni) throws SolmrException
  {
    return iac.getElencoUfficiZonaIntermediarioAttiviByIdIntermediario(utenteAbilitazioni);
  }

  // Metodo per recuperare recuperare l'ufficio zona intermediario partendo
  // dalla chiave primaria
  public UfficioZonaIntermediarioVO findUfficioZonaIntermediarioVOByPrimaryKey(
      Long idUfficioZonaIntermediario) throws SolmrException
  {
    return iac
        .findUfficioZonaIntermediarioVOByPrimaryKey(idUfficioZonaIntermediario);
  }

  // Metodo per recuperare l'elenco delle aziende in relazione all'intermediario
  // delegato
  public Vector<AnagAziendaVO> getElencoAziendeByCAA(DelegaVO delegaVO) throws SolmrException
  {
    return iac.getElencoAziendeByCAA(delegaVO);
  }

  // Metodo per aggiornare e recuperare le pratiche relative ad un'azienda
  // agricola
  public Vector<ProcedimentoAziendaVO> updateAndGetPraticheByAzienda(Long idAzienda)
      throws SolmrException
  {
    return iac.updateAndGetPraticheByAzienda(idAzienda);
  }

  public Vector<ProcedimentoAziendaVO> getElencoProcedimentiByIdAzienda(
      Long idAzienda, Long annoProc, Long idProcedimento,
      Long idAziendaSelezionata) throws SolmrException
  {
    return iac.getElencoProcedimentiByIdAzienda(idAzienda, annoProc,
        idProcedimento, idAziendaSelezionata);
  }

  public CodeDescription[] getListCuaaAttiviProvDestByIdAzienda(Long idAzienda)
      throws Exception
  {
    return iac.getListCuaaAttiviProvDestByIdAzienda(idAzienda);
  }

  // Gestione metodi di collegamento al WEB-SERVICE SIAN
  
  public Hashtable<BigDecimal, SianAllevamentiVO> leggiAllevamentiNoProfile(String cuaa)
      throws SolmrException      
  {
    Hashtable<BigDecimal, SianAllevamentiVO> result = null;
    try
    {
      result = iac.leggiAllevamentiNoProfile(cuaa);
    }
    catch (Exception e)
    {
      if (e != null && e.toString().indexOf("TimedOutException") != -1)
        throw new SolmrException((String) AnagErrors
            .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
      else
        throw new SolmrException(e.getMessage());
    }
    return result;
  }
  
  public ElencoRegistroDiStallaVO elencoRegistriStalla(String codiceAzienda,
      String codiceSpecie, String cuaa, String tipo, String ordinamento, SianUtenteVO sianUtenteVO)
      throws SolmrException
  {
    return iac.elencoRegistriStalla(codiceAzienda, codiceSpecie, cuaa, tipo,
        ordinamento, sianUtenteVO);
  }
  
  public ElencoRegistroDiStallaVO elencoRegistriStallaNoProfile(String codiceAzienda,
      String codiceSpecie, String cuaa, String tipo, String ordinamento)
      throws SolmrException
  {
    return iac.elencoRegistriStallaNoProfile(codiceAzienda, codiceSpecie, cuaa, tipo, ordinamento);
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
      StringBuffer annoCampagna) throws SolmrException
  {
    SianTerritorioVO[] result = null;
    try
    {
      result = iac.leggiTerritorio(cuaa, annoCampagna);
    }
    catch (Exception e)
    {
      if (e != null && e.toString().indexOf("TimedOutException") != -1)
        throw new SolmrException((String) AnagErrors
            .get("ERR_SERVIZIO_SIAN_TIMEOUT"));
      else
        throw new SolmrException(e.getMessage());
    }
    return result;
  }

  public SianTerritorioVO[] verificaCensimentoFoglio(
      SianTerritorioVO[] elencoSian) throws SolmrException
  {
    return iac.verificaCensimentoFoglio(elencoSian);
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
   */
  public Vector<SianTitoliAggregatiVO> titoliProduttoreAggregati(String cuaa,
      String campagna, SianUtenteVO sianUtenteVO) throws SolmrException
  {
    Vector<SianTitoliAggregatiVO> result = new Vector<SianTitoliAggregatiVO>();
    try
    {
      result = iac.titoliProduttoreAggregati(cuaa, campagna, sianUtenteVO);
    }
    catch (Exception e)
    {
      if (e != null && e.toString().indexOf("TimedOutException") != -1)
        throw new SolmrException((String) AnagErrors
            .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
      else
        throw new SolmrException(e.getMessage());
    }
    return result;
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
   */
  public Vector<SianQuoteLatteAziendaVO> quoteLatte(String cuaa, String campagna, SianUtenteVO sianUtenteVO)
      throws SolmrException
  {
    Vector<SianQuoteLatteAziendaVO> result = new Vector<SianQuoteLatteAziendaVO>();
    try
    {
      result = iac.quoteLatte(cuaa, campagna, sianUtenteVO);
    }
    catch (Exception e)
    {
      if (e != null && e.toString().indexOf("TimedOutException") != -1)
        throw new SolmrException((String) AnagErrors
            .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
      else
        throw new SolmrException(e.getMessage());
    }
    return result;
  }

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
      throws SystemException, SolmrException
  {
    return iac.serviceGetUtenteProcedimento(codiceFiscale, idProcedimento,
        ruolo, codiceEnte, dirittoAccesso, idLivello);
  }*/

  public IntermediarioVO serviceFindResponsabileByIdIntermediarioAndIdProcToDateRiferimento(
      Long idIntermediario, Long idProcedimento, Date dataRiferimento)
      throws SolmrException
  {
    return iac
        .serviceFindResponsabileByIdIntermediarioAndIdProcToDateRiferimento(
            idIntermediario, idProcedimento, dataRiferimento);
  }

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
   */
  public AmmCompetenzaVO[] serviceGetListAmmCompetenzaByComuneCollegato(
      String istatComune, String tipoAmministrazione) throws SystemException,
      SolmrException
  {
    return iac.serviceGetListAmmCompetenzaByComuneCollegato(istatComune,
        tipoAmministrazione);
  }

  /**
   * Metodo che si occupa di invocare il servizio di comune che mi restituisce
   * il record della tabella DB_AMM_COMPETENZA a partire dal codice ente
   * 
   * @param codiceAmm
   *          String
   * @return AmmCompetenzaVO
   * @throws SolmrException
   */
  public AmmCompetenzaVO serviceFindAmmCompetenzaByCodiceAmm(String codiceAmm)
      throws SolmrException
  {
    return iac.serviceFindAmmCompetenzaByCodiceAmm(codiceAmm);
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
      String codiceFiscale) throws SolmrException
  {
    return iac.serviceFindIntermediarioByCodiceFiscale(codiceFiscale);
  }

  public IntermediarioVO serviceFindIntermediarioByIdIntermediario(
      Long idIntermediario) throws SolmrException
  {
    return iac.serviceFindIntermediarioByIdIntermediario(idIntermediario);
  }

  /**
   * Metodo che richiama un servizio di comune per ottenere l'elenco delle
   * amministrazione di competenza
   * 
   * @return it.csi.solmr.dto.comune.AmmCompetenzaVO[]
   * @throws SolmrException
   */
  public AmmCompetenzaVO[] serviceGetListAmmCompetenza() throws SolmrException
  {
    return iac.serviceGetListAmmCompetenza();
  }

  /**
   * Metodo che richiama un servizio di comune per verifica la gerarchia tra gli
   * utenti di tipo intermediario/OPR
   * 
   * @param idUtenteConnesso
   * @param idUtentePratica
   * @return boolean
   * @throws SolmrException
   */
  /*public boolean serviceVerificaGerarchia(Long idUtenteConnesso,
      Long idUtentePratica) throws SolmrException
  {
    return iac.serviceVerificaGerarchia(idUtenteConnesso, idUtentePratica);
  }*/

  public AmmCompetenzaVO[] serviceFindAmmCompetenzaByIdRange(
      String idAmmCompetenza[]) throws SolmrException
  {
    return iac.serviceFindAmmCompetenzaByIdRange(idAmmCompetenza);
  }
  
  public TecnicoAmministrazioneVO[] serviceGetTecnicoAmmByIdAmmCompAndIdProcedimento(Long idAmmCompetenza,
      Long idProcedimento)
    throws SolmrException
  {
    return iac.serviceGetTecnicoAmmByIdAmmCompAndIdProcedimento(idAmmCompetenza, idProcedimento);
  }
  
  public long[] smrcommSearchListIdEntiPrivatiByCodiceEntePrivatoRange(String[] arrCodiceEntePrivato,
      boolean flagCessazione) throws SolmrException
  {
    return iac.smrcommSearchListIdEntiPrivatiByCodiceEntePrivatoRange(arrCodiceEntePrivato, flagCessazione);
  }
  
  public DatiEntePrivatoVO[] smrcommGetEntiPrivatiByIdEntePrivatoRange(long[] idEntePrivato,
      int tipoRisultato, EntePrivatoFiltroVO filtro) throws SolmrException
  {
    return iac.smrcommGetEntiPrivatiByIdEntePrivatoRange(idEntePrivato, tipoRisultato, filtro);
  }

  
  

  /**
   * FINE metodi erogati da comune
   * 
   * @param idControllo
   *          Long
   * @throws SolmrException
   * @return Vector
   */
  public Vector<CodeDescription> getDocumentiByIdControllo(Long idControllo)
      throws SolmrException
  {
    return iac.getDocumentiByIdControllo(idControllo);
  }

  public Vector<DocumentoVO> getDocumenti(String idDocumenti[])
      throws SolmrException
  {
    return iac.getDocumenti(idDocumenti);
  }

  public void deleteInsertDichiarazioneCorrezione(
      String elencoIdDichiarazioneSegnalazione[], Vector<ErrAnomaliaDicConsistenzaVO> corrErr, long idUtente)
      throws SolmrException
  {
    iac.deleteInsertDichiarazioneCorrezione(elencoIdDichiarazioneSegnalazione,
        corrErr, idUtente);
  }

  // Metodo per effettuare la cancellazione di una dichiarazione di correzione
  public void deleteDichiarazioneCorrezione(
      String elencoIdDichiarazioneSegnalazione[]) throws SolmrException
  {
    iac.deleteDichiarazioneCorrezione(elencoIdDichiarazioneSegnalazione);
  }

  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichiarazioneConsistenza(
      Long elencoIdDichiarazioneSegnalazione[], long idMotivoDichiarazione)
      throws SolmrException
  {
    return iac.getErroriAnomalieDichiarazioneConsistenza(
        elencoIdDichiarazioneSegnalazione, idMotivoDichiarazione);
  }

  public boolean isRuolosuAnagrafe(String codiceFiscale, long idAzienda)
      throws SolmrException
  {
    return iac.isRuolosuAnagrafe(codiceFiscale, idAzienda);
  }

  public PersonaFisicaVO getRuolosuAnagrafe(String codiceFiscale,
      long idAzienda, String codRuoloAAEP) throws SolmrException
  {
    return iac.getRuolosuAnagrafe(codiceFiscale, idAzienda, codRuoloAAEP);
  }

  public boolean getRuolosuAnagrafe(String codiceFiscale, long idAzienda)
      throws SolmrException
  {
    return iac.getRuolosuAnagrafe(codiceFiscale, idAzienda);
  }

  public String getIstatByDescComune(String descComune) throws SolmrException
  {
    return iac.getIstatByDescComune(descComune);
  }

  public void importaSoggCollAAEP(AnagAziendaVO anagAziendaVO,
      Azienda impresaInfoc, HashMap<?,?> listaPersone, String idParametri[],
      Long idUtenteAggiornamento) throws SolmrException
  {
    iac.importaSoggCollAAEP(anagAziendaVO, impresaInfoc, listaPersone,
        idParametri, idUtenteAggiornamento);
  }

  public CodeDescription getAttivitaATECObyCode(String codiceAteco)
      throws SolmrException
  {
    return iac.getAttivitaATECObyCode(codiceAteco);
  }

  public CodeDescription getAttivitaATECObyCodeParametroCATE(String codiceAteco)
      throws Exception
  {
    return iac.getAttivitaATECObyCodeParametroCATE(codiceAteco);
  }

  public void importaUteAAEP(AnagAziendaVO anagAziendaVO,
      Sede[] sedeInfocamere, String idParametri[], Long idUtenteAggiornamento)
      throws SolmrException
  {
    iac.importaUteAAEP(anagAziendaVO, sedeInfocamere, idParametri,
        idUtenteAggiornamento);
  }

  public CodeDescription[] getElencoAtecoNew(String codiceAteco, Long idAzienda)
      throws Exception
  {
    return iac.getElencoAtecoNew(codiceAteco, idAzienda);
  }

  public Boolean isUtenteConRuoloSuProcedimento(String codiceFiscale,
      Long idProcedimento) throws InvalidParameterException, SolmrException
  {
    return iac.isUtenteConRuoloSuProcedimento(codiceFiscale, idProcedimento);
  }

  public it.csi.solmr.dto.profile.TipoProcedimentoVO serviceFindTipoProcedimentoByDescrizioneProcedimento(
      String descrizione) throws InvalidParameterException, SolmrException
  {
    return iac
        .serviceFindTipoProcedimentoByDescrizioneProcedimento(descrizione);
  }

  public Boolean isUtenteAbilitatoProcedimento(UtenteIride2VO utenteIride2VO)
      throws InvalidParameterException, SolmrException
  {
    return iac.isUtenteAbilitatoProcedimento(utenteIride2VO);
  }

  public Long writeAccessLogUser(UtenteIride2VO utenteIride2VO)
      throws InvalidParameterException, SolmrException
  {
    return iac.writeAccessLogUser(utenteIride2VO);
  }

  public RuoloUtenza loadRoleUser(UtenteIride2VO utenteIride2VO)
      throws InvalidParameterException, SolmrException
  {
    return iac.loadRoleUser(utenteIride2VO);
  }

  public MandatoVO serviceGetMandato(Long idAzienda, String codiceEnte)
      throws SystemException, InvalidParameterException,
      UnrecoverableException, SolmrException
  {
    return iac.serviceGetMandato(idAzienda, codiceEnte);
  }
  
  
  public boolean getDelegaBySocio(String codFiscIntermediario, Long idAziendaAssociata) 
    throws Exception, SolmrException
  {
    return iac.getDelegaBySocio(codFiscIntermediario, idAziendaAssociata);
  }
  
  public AziendaCollegataVO findAziendaCollegataByFatherAndSon(Long idAziendaFather, Long idAziendaSon,
      Date dataSituazione) throws Exception, SolmrException
  {
    return iac.findAziendaCollegataByFatherAndSon(idAziendaFather, idAziendaSon, dataSituazione);
  }
  
  public boolean isSoggettoAssociatoByFatherAndSon(Long idAziendaFather, String cuaaSon,
      Date dataSituazione) throws Exception, SolmrException
  {
    return iac.isSoggettoAssociatoByFatherAndSon(idAziendaFather, cuaaSon, dataSituazione);
  }

  // INIZIO REPORTISTICA

  /**
   * Metodo per recuperare l'elenco dei mandati a partire dall'utente
   * 
   */
  public Vector<DelegaVO> getMandatiByUtente(UtenteAbilitazioni utenteAbilitazioni, boolean forZona,
      java.util.Date dataDal, java.util.Date dataAl) throws SolmrException
  {
    return iac.getMandatiByUtente(utenteAbilitazioni, forZona, dataDal, dataAl);
  }

  /**
   * Recupero l'elenco di CAA(espresso con un vector di CODE-DESCRIPTION perchè
   * mi serve per popolare una combo) in relazione all'utente che si è loggato
   * 
   */
  public Vector<CodeDescription> getElencoCAAByUtente(UtenteAbilitazioni utenteAbilitazioni)
      throws Exception, SolmrException
  {
    return iac.getElencoCAAByUtente(utenteAbilitazioni);
  }

  /**
   * Metodo per recuperare l'elenco dei mandati validati in un determinato
   * periodo di tempo
   * 
   */
  public Vector<DelegaVO> getMandatiValidatiByUtente(UtenteAbilitazioni utenteAbilitazioni,
      boolean forZona, java.util.Date dataDal, java.util.Date dataAl)
      throws SolmrException
  {
    return iac.getMandatiValidatiByUtente(utenteAbilitazioni, forZona, dataDal, dataAl);
  }

  // FINE REPORTISTICA

  /**
   * Metodo per reperire tutte le informazioni relative all'utente di tipo
   * intermediario associato ad uno specifico ufficio di zona
   * 
   * @param idUfficioZonaIntermediario
   *          Long
   * @return IntermediarioVO
   * @throws SolmrException
   */
  public IntermediarioVO getIntermediarioVOByIdUfficioZonaIntermediario(
      Long idUfficioZonaIntermediario) throws SolmrException
  {
    return iac
        .getIntermediarioVOByIdUfficioZonaIntermediario(idUfficioZonaIntermediario);
  }

  /**
   * Ricavo l'oggeto intermediario VO passando il suo codice fiscale.
   * 
   * @param codice_fiscale
   * @return
   * @throws SolmrException
   */
  /*public IntermediarioVO getIntermediarioVOByCodiceFiscale(String codice_fiscale)
      throws SolmrException
  {
    return iac.getIntermediarioVOByCodiceFiscale(codice_fiscale);
  }*/

  /**
   * Metodo per recuperare l'intermediario a partire dalla chiave primaria
   * 
   * @param idIntermediario
   *          Long
   * @return IntermediarioVO
   * @throws SolmrException
   */
  public IntermediarioVO findIntermediarioVOByPrimaryKey(Long idIntermediario)
      throws SolmrException
  {
    return iac.findIntermediarioVOByPrimaryKey(idIntermediario);
  }

  /**
   * Metodo che si occupa di i dati dalla tabella REGIONE in relazione all'istat
   * della provincia
   * 
   * @param istatProvincia
   *          String
   * @return CodeDescription
   * @throws SolmrException
   */
  public CodeDescription findRegioneByIstatProvincia(String istatProvincia)
      throws SolmrException
  {
    return iac.findRegioneByIstatProvincia(istatProvincia);
  }

  /**
   * Metodo che si occupa di reperire i dati dalla tabella REGIONE relativi
   * all'intermediario loggato partendo dal codice_fiscale
   * 
   * @param codiceFiscaleIntermediario
   *          String
   * @return CodeDescription
   * @throws SolmrException
   */
  public CodeDescription findRegioneByCodiceFiscaleIntermediario(
      String codiceFiscaleIntermediario) throws SolmrException
  {
    return iac
        .findRegioneByCodiceFiscaleIntermediario(codiceFiscaleIntermediario);
  }

  /**
   * Metodo che si occupa di estrarre tutti i gruppo controllo associati ad una
   * determinata dichiarazione di consistenza
   * 
   * @param idDichiarazioneConsistenza
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws SolmrException
   */
  public CodeDescription[] getListTipoGruppoControlloByIdDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza, String[] orderBy) throws SolmrException
  {
    return iac.getListTipoGruppoControlloByIdDichiarazioneConsistenza(
        idDichiarazioneConsistenza, orderBy);
  }

  /**
   * Metodo per recuperare tutti i controlli svolti in una determinata
   * dichiarazione di consistenza e relativi ad un determinato gruppo controllo
   * 
   * @param idDichiarazioneConsistenza
   * @param idGruppoControllo
   * @param orderBy[]
   * @return it.csi.solmr.dto.anag.consistenza.TipoControlloVO[]
   * @throws SolmrException
   */
  public TipoControlloVO[] getListTipoControlloByIdDichiarazioneConsistenzaAndIdGruppoControllo(
      Long idDicharazioneConsistenza, Long idGruppoControllo, String orderBy[])
      throws SolmrException
  {
    return iac
        .getListTipoControlloByIdDichiarazioneConsistenzaAndIdGruppoControllo(
            idDicharazioneConsistenza, idGruppoControllo, orderBy);
  }

  public ParametroRitornoVO serviceGetAziendeAAEPAnagrafe(String codiceFiscale,
      boolean controllaPresenzaSuAAEP, Boolean bloccaAssenzaAAEP,
      boolean controllaLegameRLsuAnagrafe, boolean controllaPresenzaValidazione)
      throws SystemException, InvalidParameterException,
      UnrecoverableException, SolmrException, ServiceSystemException
  {
    return iac.serviceGetAziendeAAEPAnagrafe(codiceFiscale,
        controllaPresenzaSuAAEP, bloccaAssenzaAAEP,
        controllaLegameRLsuAnagrafe, controllaPresenzaValidazione);
  }

  public AnagAziendaVO[] serviceGetListAziendeByIdRange(Vector<Long> collIdAziende)
      throws SolmrException
  {
    return iac.serviceGetListAziendeByIdRange(collIdAziende);
  }

  /*public SianAnagDettaglioVO anagraficaDettaglio(String CUAA,
      boolean orderSocRapp, boolean storicoRappLeg, SianUtenteVO sianUtenteVO) throws SolmrException,
      Exception
  {
    return iac.anagraficaDettaglio(CUAA, orderSocRapp, storicoRappLeg, sianUtenteVO);
  }

  public SianAnagDettaglioVO anagraficaDettaglio(String CUAA, SianUtenteVO sianUtenteVO)
      throws SolmrException, Exception
  {
    return iac.anagraficaDettaglio(CUAA, false, false, sianUtenteVO);
  }*/
  
  public SianAnagTributariaGaaVO ricercaAnagrafica(String cuaa, SianUtenteVO sianUtenteVO) 
      throws SolmrException, Exception
  {
    return iac.ricercaAnagrafica(cuaa, sianUtenteVO);
  }

  public RespAnagFascicoloVO getRespAnagFascicolo(
      Long idAzienda) throws SolmrException, Exception
  {
    return iac.getRespAnagFascicolo(idAzienda);
  }

  public SianFascicoloResponseVO trovaFascicolo(String cuaa)
      throws SolmrException, Exception
  {
    return iac.trovaFascicolo(cuaa);
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
   * @throws SolmrException
   */
  public Vector<ComuneVO> getListComuniParticelleByIdAzienda(Long idAzienda,
      boolean onlyActive, String[] orderBy) throws SolmrException
  {
    return iac.getListComuniParticelleByIdAzienda(idAzienda, onlyActive,
        orderBy);
  }
  
  public Vector<ComuneVO> getListComuniParticelleByIdDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza, String[] orderBy)
      throws SolmrException
  {
    return iac
        .getListComuniParticelleByIdDichiarazioneConsistenza(idDichiarazioneConsistenza, orderBy);
  }

  /**
   * Metodo che mi restituisce l'elenco di tutti gli uso del suolo utilizzati
   * dall'azienda in esame
   * 
   * @param idAzienda
   * @param onlyActive
   * @param orderBy
   * @return java.util.Vector
   * @throws SolmrException
   */
  /*public Vector<TipoUtilizzoVO> getListTipiUsoSuoloByIdAzienda(Long idAzienda,
      boolean onlyActive, String[] orderBy) throws SolmrException
  {
    return iac.getListTipiUsoSuoloByIdAzienda(idAzienda, onlyActive, orderBy);
  }*/
  
  /*public Vector<TipoUtilizzoVO> getListTipiDestinazProdPrimSecByIdAzienda(Long idAzienda)
      throws SolmrException
  {
    return iac.getListTipiDestinazProdPrimSecByIdAzienda(idAzienda);
  }*/
  
  public Vector<TipoUtilizzoVO> getListUtilizziElencoTerrPianoLavorazione(Long idAzienda)
      throws SolmrException
  {
    return iac.getListUtilizziElencoTerrPianoLavorazione(idAzienda);
  }
  
  public Vector<TipoUtilizzoVO> getListUtilizziElencoTerrPianoLavorazioneStor(Long idAzienda) 
    throws SolmrException
  {
    return iac.getListUtilizziElencoTerrPianoLavorazioneStor(idAzienda);
  }
  
  public Vector<TipoUtilizzoVO> getListUtilizziElencoTerrValidazione(Long idDichiarazioneConsistenza) 
    throws SolmrException
  {
    return iac.getListUtilizziElencoTerrValidazione(idDichiarazioneConsistenza);
  }
  
  public Vector<TipoUtilizzoVO> getListTipiUsoSuoloByIdDichCons(long idDichiarazioneConsistenza, String[] orderBy)
      throws SolmrException
  {
    return iac.getListTipiUsoSuoloByIdDichCons(idDichiarazioneConsistenza, orderBy);
  }
      
  /**
   * Metodo che mi restituisce l'ultima data nella quale sono stati effettuati
   * controlli di validità relativi alla tabella DB_CONDUZIONE_PARTICELLA
   * 
   * @param idAzienda
   * @return java.lang.String
   * @throws SolmrException
   */
  public java.lang.String getMaxDataEsecuzioneControlliConduzioneParticella(
      Long idAzienda) throws SolmrException
  {
    return iac.getMaxDataEsecuzioneControlliConduzioneParticella(idAzienda);
  }

  /**
   * Metodo che mi restituisce l'ultima data nella quale sono stati effettuati
   * controlli di validità relativi alla tabella DB_CONDUZIONE_DICHIARATA
   * 
   * @param idAzienda
   * @return java.lang.String
   * @throws SolmrException
   */
  public java.lang.String getMaxDataEsecuzioneControlliConduzioneDichiarata(
      Long idAzienda) throws SolmrException
  {
    return iac.getMaxDataEsecuzioneControlliConduzioneDichiarata(idAzienda);
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
   * @throws SolmrException
   */
  public Vector<StoricoParticellaVO> searchListParticelleByParameters(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda)
      throws SolmrException
  {
    return iac.searchListParticelleByParameters(filtriParticellareRicercaVO,
        idAzienda);
  }

  /**
   * Metodo che mi restituisce il record di DB_STORICO_PARTICELLA a partire
   * dall'id_conduzione_particella
   * 
   * @param idConduzioneParticella
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO
   * @throws SolmrException
   */
  public StoricoParticellaVO findStoricoParticellaVOByIdConduzioneParticella(
      Long idConduzioneParticella) throws SolmrException
  {
    return iac
        .findStoricoParticellaVOByIdConduzioneParticella(idConduzioneParticella);
  }

  public long getIdConduzioneDichiarata(long idConduzioneParticella,
      long idDichiarazioneConsistenza) throws SolmrException
  {
    return iac.getIdConduzioneDichiarata(idConduzioneParticella,
        idDichiarazioneConsistenza);
  }
  
  
  public Long findIdUteByIdCondPartIdAz(Long idConduzioneParticella, Long idAzienda) throws SolmrException{
	  return iac.findIdUteByIdCondPartIdAz(idConduzioneParticella, idAzienda);
  }
  
  

  /**
   * Metodo che mi restituisce il record di DB_STORICO_PARTICELLA a partire
   * dall'id_conduzione_dichiarata
   * 
   * @param idConduzioneDichiarata
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO
   * @throws SolmrException
   */
  public StoricoParticellaVO findStoricoParticellaVOByIdConduzioneDichiarata(
      Long idConduzioneDichiarata) throws SolmrException
  {
    return iac
        .findStoricoParticellaVOByIdConduzioneDichiarata(idConduzioneDichiarata);
  }

  /**
   * Metodo che mi restituisce il record su DB_CONDUZIONE_PARTICELLA a partire
   * dalla chiave primaria
   * 
   * @param idConduzioneParticella
   * @return it.csi.solmr.dto.anag.terreni.ConduzioneParticellaVO
   * @throws SolmrException
   */
  public ConduzioneParticellaVO findConduzioneParticellaByPrimaryKey(
      Long idConduzioneParticella) throws SolmrException
  {
    return iac.findConduzioneParticellaByPrimaryKey(idConduzioneParticella);
  }

  /**
   * Metodo che mi restituisce il record su DB_CONDUZIONE_DICHIARATA a partire
   * dalla chiave primaria
   * 
   * @param idConduzioneDichiarata
   * @return it.csi.solmr.dto.anag.terreni.ConduzioneDichiarataVO
   * @throws SolmrException
   */
  public ConduzioneDichiarataVO findConduzioneDichiarataByPrimaryKey(
      Long idConduzioneDichiarata) throws SolmrException
  {
    return iac.findConduzioneDichiarataByPrimaryKey(idConduzioneDichiarata);
  }

  /**
   * Metodo che si occupa di reperire il dettaglio dei dati relativi alla
   * particella a partire dall'id_conduzione_particella/dichiarata
   * 
   * @param filtriParticellareRicercaVO
   * @param idConduzione
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO
   * @throws SolmrException
   */
  public StoricoParticellaVO getDettaglioParticella(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idConduzione)
      throws SolmrException
  {
    return iac
        .getDettaglioParticella(filtriParticellareRicercaVO, idConduzione);
  }

  /**
   * Metodo che si occupa di reperire i dati dalla tabella
   * DB_TIPO_PIANTE_CONSOCIATE
   * 
   * @param onlyActive
   * @return java.util.Vector
   * @throws SolmrException
   */
  public Vector<TipoPiantaConsociataVO> getListPianteConsociate(boolean onlyActive)
      throws SolmrException
  {
    return iac.getListPianteConsociate(onlyActive);
  }

  /**
   * Metodo che mi restituisce l'elenco degli utilizzi a partire
   * dall'id_conduzione_particella
   * 
   * @param idConduzioneParticella
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.UtilizzoParticellaVO[]
   * @throws SolmrException
   */
  public UtilizzoParticellaVO[] getListUtilizzoParticellaVOByIdConduzioneParticella(
      Long idConduzioneParticella, String[] orderBy, boolean onlyActive)
      throws SolmrException
  {
    return iac.getListUtilizzoParticellaVOByIdConduzioneParticella(
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
   * @throws SolmrException
   */
  public UtilizzoDichiaratoVO[] getListUtilizzoDichiaratoVOByIdDichiarazioneConsistenzaAndIdConduzioneParticella(
      Long idDichiarazioneConsistenza, Long idConduzioneParticella,
      String[] orderBy) throws SolmrException
  {
    return iac
        .getListUtilizzoDichiaratoVOByIdDichiarazioneConsistenzaAndIdConduzioneParticella(
            idDichiarazioneConsistenza, idConduzioneParticella, orderBy);
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
   * @throws SolmrException
   */
  public ParticellaCertificataVO findParticellaCertificataByParameters(
      String istatComune, String sezione, String foglio, String particella,
      String subalterno, boolean onlyActive,
      java.util.Date dataDichiarazioneConsistenza) throws SolmrException
  {
    return iac.findParticellaCertificataByParameters(istatComune, sezione,
        foglio, particella, subalterno, onlyActive,
        dataDichiarazioneConsistenza);
  }

  /**
   * Metodo che mi restituisce la particella certificata presente su
   * DB_PARTICELLA_CERTIFICATA in relazione alla chiave logica(comune, sezione,
   * foglio, particella, subalterno) + la nuova eleggibilità fittizia
   * 
   * @param istatComune
   * @param sezione
   * @param foglio
   * @param particella
   * @param subalterno
   * @param onlyActive
   * @param dataDichiarazioneConsistenza
   * @return it.csi.solmr.dto.anag.ParticellaCertificataVO
   * @throws SolmrException
   */
  public ParticellaCertificataVO findParticellaCertificataByParametersNewElegFit(
      String istatComune, String sezione, String foglio, String particella,
      String subalterno, boolean onlyActive,
      java.util.Date dataDichiarazioneConsistenza) throws SolmrException
  {
    return iac.findParticellaCertificataByParametersNewElegFit(istatComune,
        sezione, foglio, particella, subalterno, onlyActive,
        dataDichiarazioneConsistenza);
  }
  
  public ParticellaCertificataVO findParticellaCertificataAllaDichiarazione(
      Long idParticella, ConsistenzaVO consistenzaVO) throws SolmrException
  {
    return iac.findParticellaCertificataAllaDichiarazione(idParticella, consistenzaVO);
  }
  
  public ParticellaCertificataVO findParticellaCertificataByIdParticella(Long idParticella, 
      Date dataDichiarazioneConsistenza) throws SolmrException
  {
    return iac.findParticellaCertificataByIdParticella(idParticella, dataDichiarazioneConsistenza);
  }
  
  public Date getDataFotoInterpretazione(long idParticellaCertificata, Date dataRichiestaRiesame)
    throws SolmrException
  {
    return iac.getDataFotoInterpretazione(idParticellaCertificata, dataRichiestaRiesame);
  }
  
  /*public Date getDataRichiestaRiesameAlaDich(long idStoricoParticella, long idDichiarazioneConsistenza)
    throws SolmrException
  {
    return iac.getDataRichiestaRiesameAlaDich(idStoricoParticella, idDichiarazioneConsistenza);
  }
  */
  
  public Vector<ParticellaCertElegVO> getEleggibilitaByIdParticella(long idParticella)
    throws SolmrException
  {
    return iac.getEleggibilitaByIdParticella(idParticella);
  }
  
  public Vector<Vector<ParticellaCertElegVO>> getListStoricoParticellaCertEleg(long idParticella)
    throws SolmrException
  {
    return iac.getListStoricoParticellaCertEleg(idParticella);
  }
  
  public HashMap<Long,Vector<SuperficieDescription>> getEleggibilitaTooltipByIdParticella(
      Vector<Long> listIdParticella)
  throws SolmrException
  {
    return iac.getEleggibilitaTooltipByIdParticella(listIdParticella);
  }
  
  public Vector<ProprietaCertificataVO> getListProprietaCertifByIdParticella(long idParticella) 
      throws SolmrException
  {
    return iac.getListProprietaCertifByIdParticella(idParticella);
  }
  
  public Vector<ProprietaCertificataVO> getListDettaglioProprietaCertifByIdParticella(
      long idParticella, Date dataInserimentoValidazione) 
      throws SolmrException
  {
    return iac.getListDettaglioProprietaCertifByIdParticella(idParticella, dataInserimentoValidazione);
  }

  /**
   * Metodo che mi restituisce l'elenco delle varieta a partire dall'id_utilizzo
   * 
   * @param idUtilizzo
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.TipoVarietaVO
   * @throws SolmrException
   */
  public TipoVarietaVO[] getListTipoVarietaByIdUtilizzo(Long idUtilizzo,
      boolean onlyActive) throws SolmrException
  {
    return iac.getListTipoVarietaByIdUtilizzo(idUtilizzo, onlyActive);
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
   * @throws SolmrException
   */
  public StoricoParticellaVO getDettaglioParticellaByIdConduzioneAndIdUtilizzo(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO,
      Long idConduzione, Long idUtilizzo, Long idAzienda) throws SolmrException
  {
    return iac.getDettaglioParticellaByIdConduzioneAndIdUtilizzo(
        filtriParticellareRicercaVO, idConduzione, idUtilizzo, idAzienda);
  }

  public BigDecimal getSumSuperficieUtilizzoUsoAgronomico(
      long idConduzioneParticella) throws SolmrException
  {
    return iac.getSumSuperficieUtilizzoUsoAgronomico(idConduzioneParticella);
  }
  
  public BigDecimal getSumSuperficieUtilizzoUsoAgronomicoParticella(long idParticella, long idAzienda) 
      throws SolmrException
  {
    return iac.getSumSuperficieUtilizzoUsoAgronomicoParticella(idParticella, idAzienda);
  }
  
  public BigDecimal getSumSuperficieAgronomicaAltreconduzioni(long idParticella, long idConduzioneParticella, long idAzienda) 
      throws SolmrException
  {
    return iac.getSumSuperficieAgronomicaAltreconduzioni(idParticella, idConduzioneParticella, idAzienda);
  }

  public BigDecimal getSumSuperficieFromParticellaAndLastDichCons(
      long idParticella, Long idAzienda, boolean flagEscludiAzienda)
      throws SolmrException
  {
    return iac.getSumSuperficieFromParticellaAndLastDichCons(idParticella,
        idAzienda, flagEscludiAzienda);
  }

  public String[] getIstatProvFromConduzione(long idAzienda)
      throws SolmrException
  {
    return iac.getIstatProvFromConduzione(idAzienda);
  }
  
  public BigDecimal getPercentualePosesso(long idAzienda, long idParticella)
    throws SolmrException
  {
    return iac.getPercentualePosesso(idAzienda, idParticella);
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
   * @throws SolmrException
   */
  public TipoUtilizzoVO[] getListTipiUsoSuoloByIdIndirizzoUtilizzo(
      Long idIndirizzoUtilizzo, boolean onlyActive, String[] orderBy,
      String colturaSecondaria) throws SolmrException
  {
    return iac.getListTipiUsoSuoloByIdIndirizzoUtilizzo(idIndirizzoUtilizzo,
        onlyActive, orderBy, colturaSecondaria);
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
   * @throws SolmrException
   */
  public TipoUtilizzoVO[] getListTipiUsoSuoloByCodice(String codice,
      boolean onlyActive, String[] orderBy, String colturaSecondaria,
      String flagPrincipale) throws SolmrException
  {
    return iac.getListTipiUsoSuoloByCodice(codice, onlyActive, orderBy,
        colturaSecondaria, flagPrincipale);
  }
  
  public TipoVarietaVO[] getListTipoVarietaByIdUtilizzoAndCodice(
      Long idUtilizzo, String codiceVarieta, boolean onlyActive, 
      String[] orderBy) throws SolmrException
  {
    return iac.getListTipoVarietaByIdUtilizzoAndCodice(
        idUtilizzo, codiceVarieta, onlyActive, orderBy);
  }

  /**
   * Metodo per effettuare l'associa uso a più particelle
   * 
   * @param elencoParticelle
   * @param ruoloUtenza
   * @throws SolmrException
   */
  public void associaUso(long idAzienda, StoricoParticellaVO[] elencoParticelle,
      RuoloUtenza ruoloUtenza) throws SolmrException
  {
    iac.associaUso(idAzienda, elencoParticelle, ruoloUtenza);
  }
  
  public void associaUsoEleggibilitaGis(long idAzienda, StoricoParticellaVO[] elencoParticelle,
      RuoloUtenza ruoloUtenza) throws SolmrException
  {
    iac.associaUsoEleggibilitaGis(idAzienda, elencoParticelle, ruoloUtenza);
  }

  /**
   * Metodo utilizzato nella modifica multipla per aggiornare su
   * DB_STORICO_PARTICELLA i dati dell'irrigazione
   * 
   * @param elencoStoricoParticella
   * @param ruoloUtenza
   * @param flagIrrigabile
   * @param idIrrigazione
   * @throws SolmrException
   */
  public void aggiornaIrrigazione(
      StoricoParticellaVO[] elencoStoricoParticella, RuoloUtenza ruoloUtenza,
      boolean flagIrrigabile, Long idIrrigazione) throws SolmrException
  {
    iac.aggiornaIrrigazione(elencoStoricoParticella, ruoloUtenza,
        flagIrrigabile, idIrrigazione);
  }

  /**
   * Metodo che si occupa di effettuare il cambio titolo di possesso delle
   * particelle selezionate
   * 
   * @param elencoIdConduzioneParticella
   * @param ruoloUtenza
   * @param idAzienda
   * @param idTitoloPossesso
   * @throws SolmrException
   */
  public void cambiaTitoloPossesso(Long[] elencoIdConduzioneParticella,
      RuoloUtenza ruoloUtenza, Long idAzienda, Long idTitoloPossesso)
      throws SolmrException
  {
    iac.cambiaTitoloPossesso(elencoIdConduzioneParticella, ruoloUtenza,
        idAzienda, idTitoloPossesso);
  }

  /**
   * Metodo che mi restituisce la data più recente relativa ad un'elenco di
   * conduzioni selezionate
   * 
   * @param idConduzioneParticella
   * @return java.util.Date
   * @throws SolmrException
   */
  public java.util.Date getMaxDataInizioConduzioneParticella(
      Vector<Long> elencoConduzioni) throws SolmrException
  {
    return iac.getMaxDataInizioConduzioneParticella(elencoConduzioni);
  }

  /**
   * Metodo utilizzato per effettuare la cessazione delle particelle
   * 
   * @param elencoIdConduzioneParticella
   * @param ruoloUtenza
   * @param idAzienda
   * @param dataCessazione
   * @throws SolmrException
   */
  public void cessaParticelle(Long[] elencoIdConduzioneParticella,
		long idUtenteAggiornamento, Long idAzienda, java.util.Date dataCessazione, String provenienza)
      throws SolmrException
  {
    iac.cessaParticelle(elencoIdConduzioneParticella, idUtenteAggiornamento, idAzienda,
        dataCessazione, provenienza);
  }

  /**
   * Metodo che si occupa di effettuare il cambio U.T.E. delle particelle
   * selezionate
   * 
   * @param elencoIdConduzioneParticella
   * @param ruoloUtenza
   * @param idUte
   * @param idAzienda
   * @throws SolmrException
   */
  public void cambiaUte(Long[] elencoIdConduzioneParticella,
      RuoloUtenza ruoloUtenza, Long idUte, Long idAzienda)
      throws SolmrException
  {
    iac.cambiaUte(elencoIdConduzioneParticella, ruoloUtenza, idUte, idAzienda);
  }

  /**
   * Metodo utilizzato per associare un documento alle particelle selezionate
   * 
   * @param elencoIdConduzioneParticella
   * @param idDocumento
   * @throws SolmrException
   */
  public void associaDocumento(Long[] elencoIdConduzioneParticella,
      Long idDocumento) throws SolmrException
  {
    iac.associaDocumento(elencoIdConduzioneParticella, idDocumento);
  }

  /**
   * Metodo che mi restituisce l'elenco dei titoli di possesso in relazione al
   * criterio di ordinamento indicato dall'utente
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws SolmrException
   */
  public CodeDescription[] getListTipiTitoloPossesso(String orderBy)
      throws SolmrException
  {
    return iac.getListTipiTitoloPossesso(orderBy);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco dei casi particolari
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws SolmrException
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoCasoParticolare(
      String orderBy) throws SolmrException
  {
    return iac.getListTipoCasoParticolare(orderBy);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco delle zone altimetriche
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws SolmrException
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoZonaAltimetrica(
      String orderBy) throws SolmrException
  {
    return iac.getListTipoZonaAltimetrica(orderBy);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco delle area A
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws SolmrException
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaA(String orderBy)
      throws SolmrException
  {
    return iac.getListTipoAreaA(orderBy);
  }
  
  /**
   * Metodo utilizzato per estrarre l'elenco delle area A
   * con data fine validità a null
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws SolmrException
   */
  public it.csi.solmr.dto.CodeDescription[] getValidListTipoAreaA(String orderBy)
      throws SolmrException
  {
    return iac.getValidListTipoAreaA(orderBy);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco delle area B
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws SolmrException
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaB(String orderBy)
      throws SolmrException
  {
    return iac.getListTipoAreaB(orderBy);
  }
  
  /**
   * Metodo utilizzato per estrarre l'elenco delle area B
   * con data fine validità a null
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws SolmrException
   */
  public it.csi.solmr.dto.CodeDescription[] getValidListTipoAreaB(String orderBy)
      throws SolmrException
  {
    return iac.getValidListTipoAreaB(orderBy);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco delle area C
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws SolmrException
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaC(String orderBy)
      throws SolmrException
  {
    return iac.getListTipoAreaC(orderBy);
  }
  
  /**
   * Metodo utilizzato per estrarre l'elenco delle area C
   * con data fine validità a null
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws SolmrException
   */
  public it.csi.solmr.dto.CodeDescription[] getValidListTipoAreaC(String orderBy)
      throws SolmrException
  {
    return iac.getValidListTipoAreaC(orderBy);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco delle area D
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws SolmrException
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaD(String orderBy)
      throws SolmrException
  {
    return iac.getListTipoAreaD(orderBy);
  }
  
  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaM(String orderBy)
      throws SolmrException
  {
    return iac.getListTipoAreaM(orderBy);
  }
  
  /**
   * Metodo utilizzato per estrarre l'elenco delle area D
   * con data fine validità a null
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws SolmrException
   */
  public it.csi.solmr.dto.CodeDescription[] getValidListTipoAreaD(String orderBy)
      throws SolmrException
  {
    return iac.getValidListTipoAreaD(orderBy);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco delle area E
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws SolmrException
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaE(String orderBy)
      throws SolmrException
  {
    return iac.getListTipoAreaE(orderBy);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco delle area F
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws SolmrException
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaF(String orderBy)
      throws SolmrException
  {
    return iac.getListTipoAreaF(orderBy);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco delle area PSN
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws SolmrException
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaPSN(String orderBy)
      throws SolmrException
  {
    return iac.getListTipoAreaPSN(orderBy);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco dalla fascia fluviale
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws SolmrException
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoFasciaFluviale(
      String orderBy) throws SolmrException
  {
    return iac.getListTipoFasciaFluviale(orderBy);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco dall' Area G
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws SolmrException
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaG(String orderBy)
      throws SolmrException
  {
    return iac.getListTipoAreaG(orderBy);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco dall' Area H
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws SolmrException
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoAreaH(String orderBy)
      throws SolmrException
  {
    return iac.getListTipoAreaH(orderBy);
  }
  
  public it.csi.solmr.dto.CodeDescription[] getValidListTipoAreaM(String orderBy)
      throws SolmrException
  {
    return iac.getValidListTipoAreaM(orderBy);
  }

  /**
   * Metodo utilizzato per estrarre l'elenco delle causali modifica particella
   * 
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoCausaleModParticella(
      String orderBy) throws SolmrException
  {
    return iac.getListTipoCausaleModParticella(orderBy);
  }

  /**
   * Metodo che si occupa di reperire i dati dalla tabella DB_TIPO_IMPIANTO
   * 
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.TipoImpiantoVO[]
   * @throws SolmrException
   */
  public TipoImpiantoVO[] getListTipoImpianto(boolean onlyActive, String orderBy)
      throws SolmrException
  {
    return iac.getListTipoImpianto(onlyActive, orderBy);
  }

  /**
   * Metodo che si occupa di reperire i dati dalla tabella
   * DB_TIPO_INDIRIZZO_UTILIZZO
   * 
   * @param colturaSecondaria
   * @param orderBy
   * @param onlyActive
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws SolmrException
   */
  public it.csi.solmr.dto.CodeDescription[] getListTipoIndirizzoUtilizzo(
      String colturaSecondaria, String orderBy, boolean onlyActive)
      throws SolmrException
  {
    return iac.getListTipoIndirizzoUtilizzo(colturaSecondaria, orderBy,
        onlyActive);
  }

  /**
   * Metodo che si occupa di reperire i dati dalla tabella DB_TIPO_IRRIGAZIONE
   * 
   * @param orderBy
   * @param onlyActive
   * @return it.csi.solmr.dto.anag.terreni.TipoIrrigazioneVO[]
   * @throws SolmrException
   */
  public TipoIrrigazioneVO[] getListTipoIrrigazione(String orderBy,
      boolean onlyActive) throws SolmrException
  {
    return iac.getListTipoIrrigazione(orderBy, onlyActive);
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
      Date dataRiferiemento) throws SolmrException
  {
    return iac.getListTipoPotenzialitaIrrigua(orderBy, dataRiferiemento);
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
      Date dataRiferiemento) throws SolmrException
  {
    return iac.getListTipoTerrazzamento(orderBy, dataRiferiemento);
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
      Date dataRiferiemento) throws SolmrException
  {
    return iac.getListTipoRotazioneColturale(orderBy, dataRiferiemento);
  }

  /**
   * Metodo che si occupa di estrarre l'impianto a partire dalla sua chiave
   * primaria
   * 
   * @param idImpianto
   * @return it.csi.solmr.dto.anag.terreni.TipoImpiantoVO
   * @throws SolmrException
   */
  public TipoImpiantoVO findTipoImpiantoByPrimaryKey(Long idImpianto)
      throws SolmrException
  {
    return iac.findTipoImpiantoByPrimaryKey(idImpianto);
  }

  /**
   * Metodo che mi restituisce l'elenco degli utilizzi consociati a partire
   * dall'id_utilizzo_particella
   * 
   * @param idUtilizzoParticella
   * @param orderBy
   * @return java.util.Vector
   * @throws SolmrException
   */
  public Vector<UtilizzoConsociatoVO> getListUtilizziConsociatiByIdUtilizzoParticella(
      Long idUtilizzoParticella, String[] orderBy) throws SolmrException
  {
    return iac.getListUtilizziConsociatiByIdUtilizzoParticella(
        idUtilizzoParticella, orderBy);
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
   * @throws SolmrException
   */
  public Vector<TipoUtilizzoVO> findListTipiUsoSuoloByIdAzienda(Long idAzienda,
      String colturaSecondaria) throws SolmrException
  {
    return iac.findListTipiUsoSuoloByIdAzienda(idAzienda, colturaSecondaria);
  }

  public Vector<TipoUtilizzoVO> findListTipiUsoSuoloByIdAziendaCess(Long idAzienda,
      String[] orderBy) throws SolmrException
  {
    return iac.findListTipiUsoSuoloByIdAziendaCess(idAzienda, orderBy);
  }

  /**
   * Metodo che si occupa di effettuare la modifica delle particelle selezionate
   * 
   * @param elencoStoricoParticella
   * @param ruoloUtenza
   * @param anagAziendaVO
   * @throws SolmrException
   */
  public void modificaParticelle(StoricoParticellaVO[] elencoStoricoParticella,
      RuoloUtenza ruoloUtenza, AnagAziendaVO anagAziendaVO)
      throws SolmrException
  {
    iac.modificaParticelle(elencoStoricoParticella, ruoloUtenza, anagAziendaVO);
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
   * @throws SolmrException
   */
  public ComuneVO getComuneByParameters(String descComune,
      String siglaProvincia, String flagEstinto, String flagCatastoAttivo,
      String flagEstero) throws SolmrException
  {
    return iac.getComuneByParameters(descComune, siglaProvincia, flagEstinto,
        flagCatastoAttivo, flagEstero);
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
   * @throws SolmrException
   */
  public Vector<ComuneVO> getComuniByParameters(String descComune, String siglaProvincia,
      String flagEstinto, String flagCatastoAttivo, String flagEstero,
      String[] orderBy) throws SolmrException
  {
    return iac.getComuniByParameters(descComune, siglaProvincia, flagEstinto,
        flagCatastoAttivo, flagEstero, orderBy);
  }
  
  public Vector<ComuneVO> getComuniAttiviByIstatProvincia(String istatProvincia) 
      throws SolmrException
  {
    return iac.getComuniAttiviByIstatProvincia(istatProvincia);
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
   * @throws SolmrException
   */
  public Vector<AnagParticellaExcelVO> searchParticelleExcelByParameters(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda)
      throws SolmrException
  {
    return iac.searchParticelleExcelByParameters(filtriParticellareRicercaVO,
        idAzienda);
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
   * @throws SolmrException
   */
  public StoricoParticellaVO[] getListParticelleForDocument(
      StoricoParticellaVO storicoParticellaVO, AnagAziendaVO anagAziendaVO,
      boolean hasUnitToDocument, Long idAnomalia, String orderBy) throws SolmrException
  {
    return iac.getListParticelleForDocument(storicoParticellaVO, anagAziendaVO,
        hasUnitToDocument, idAnomalia, orderBy);
  }

  public StoricoParticellaVO[] getListParticelleForDocument(
      StoricoParticellaVO storicoParticellaVO, AnagAziendaVO anagAziendaVO,
      long idDichiarazioneConsistenza, boolean hasUnitToDocument, Long idAnomalia, String orderBy)
      throws SolmrException
  {
    return iac.getListParticelleForDocument(storicoParticellaVO, anagAziendaVO,
        idDichiarazioneConsistenza, hasUnitToDocument, idAnomalia, orderBy);
  }
  
  public StoricoParticellaVO[] getListParticelleForDocumentValoreC( 
      AnagAziendaVO anagAziendaVO, int anno, boolean hasUnitToDocument)
    throws SolmrException
  {
    return iac.getListParticelleForDocumentValoreC(anagAziendaVO, anno, hasUnitToDocument);
  }
  
  public StoricoParticellaVO[] getListParticelleForDocumentExtraSistema( 
      AnagAziendaVO anagAziendaVO, int anno, boolean hasUnitToDocument) 
    throws SolmrException
  {
    return iac.getListParticelleForDocumentExtraSistema(
        anagAziendaVO, anno, hasUnitToDocument);
  }
  
  public Vector<StoricoParticellaVO> getListParticelleUvBasic(
      FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO, AnagAziendaVO anagAziendaVO) 
    throws SolmrException
  {
    return iac.getListParticelleUvBasic(filtriUnitaArboreaRicercaVO, anagAziendaVO);
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
      throws SolmrException
  {
    return iac.findStoricoParticellaVOByParameters(istatComune, sezione,
        foglio, particella, subalterno, dataInizioValidita);
  }

  /**
   * Metodo che mi restituisce l'elenco dei tipi evento in relazione al criterio
   * di ordinamento indicato dall'utente
   * 
   
   * @throws SolmrException
   */
  public Vector<TipoEventoVO> getListTipiEvento()
      throws SolmrException
  {
    return iac.getListTipiEvento();
  }

  /**
   * Metodo che mi permette di recuperare la sezione a partire dai parametri
   * 
   * @param istatComune
   * @param sezione
   * @return it.csi.solmr.dto.anag.terreni.SezioneVO
   * @throws SolmrException
   */
  public SezioneVO getSezioneByParameters(String istatComune, String sezione)
      throws SolmrException
  {
    return iac.getSezioneByParameters(istatComune, sezione);
  }

  /**
   * Metodo che mi restituisce il foglio a partire dai parametri che
   * rappresentano la sua chiave logica
   * 
   * @param istatComune
   * @param foglio
   * @param sezione
   * @return it.csi.solmr.dto.anag.FoglioVO
   * @throws SolmrException
   */
  public FoglioVO findFoglioByParameters(String istatComune, String foglio,
      String sezione) throws SolmrException
  {
    return iac.findFoglioByParameters(istatComune, foglio, sezione);
  }

  /**
   * Metodo che mi permette di recuperare l'elenco dei fogli a partire dai
   * parametri
   * 
   * @param istatComune
   * @param sezione
   * @param foglio
   * @return it.csi.solmr.dto.anag.FoglioVO[]
   * @throws SolmrException
   */
  public FoglioVO[] getFogliByParameters(String istatComune, String sezione,
      String foglio) throws SolmrException
  {
    return iac.getFogliByParameters(istatComune, sezione, foglio);
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
   * @throws SolmrException
   */
  public StoricoParticellaVO[] getListStoricoParticellaVOByParameters(
      String istatComune, String sezione, String foglio, String particella,
      String subalterno, boolean onlyActive, String orderBy, Long idAzienda)
      throws SolmrException
  {
    return iac.getListStoricoParticellaVOByParameters(istatComune, sezione,
        foglio, particella, subalterno, onlyActive, orderBy, idAzienda);
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
   * @throws SolmrException
   */
  public StoricoParticellaVO[] getListStoricoParticellaVOByParametersImpUnar(
      String istatComune, String sezione, String foglio, String particella,
      String subalterno, long idAzienda) throws SolmrException
  {
    return iac.getListStoricoParticellaVOByParametersImpUnar(istatComune,
        sezione, foglio, particella, subalterno, idAzienda);
  }

  /**
   * Metodo che mi restituisce il record di DB_STORICO_PARTICELLA a partire
   * dalla sua chiave primaria
   * 
   * @param idConduzioneParticella
   * @return it.csi.solmr.dto.StoricoParticellaVO
   * @throws SolmrException
   */
  public StoricoParticellaVO findStoricoParticellaByPrimaryKey(
      Long idStoricoParticella) throws SolmrException
  {
    return iac.findStoricoParticellaByPrimaryKey(idStoricoParticella);
  }

  /**
   * Metodo che mi restituisce il record di DB_STORICO_PARTICELLA a partire
   * dall'idParticella
   * 
   * @param idParticella
   * @return it.csi.solmr.dto.StoricoParticellaVO
   * @throws SolmrException
   */
  public StoricoParticellaVO findCurrStoricoParticellaByIdParticella(
      Long idParticella) throws SolmrException
  {
    return iac.findCurrStoricoParticellaByIdParticella(idParticella);
  }

  /**
   * Metodo che mi permette di inserire una particella territoriale associata ad
   * un'azienda
   * 
   * @param storicoParticellaVO
   * @param ruoloUtenza
   * @param elencoParticelleEvento
   * @param idEvento
   * @param idAzienda
   * @return java.lang.String
   * @throws SolmrException
   */
  public String inserisciParticella(StoricoParticellaVO storicoParticellaVO,
      RuoloUtenza ruoloUtenza, StoricoParticellaVO[] elencoParticelleEvento,
      Long idEvento, Long idAzienda, String[] arrVIdStoricoUnitaArborea, String[] arrVAreaUV) throws SolmrException
  {
    return iac.inserisciParticella(storicoParticellaVO, ruoloUtenza,
        elencoParticelleEvento, idEvento, idAzienda, arrVIdStoricoUnitaArborea, arrVAreaUV);
  }

  /**
   * Metodo che mi permette di inserire una particella territoriale non
   * associata a nessuna azienda
   * 
   * @param storicoParticellaVO
   * @param ruoloUtenza
   * @param elencoParticelleEvento
   * @param idEvento
   * @param idAzienda
   * @return java.lang.String
   * @throws SolmrException
   */
  public String inserisciParticella(StoricoParticellaVO storicoParticellaVO,
      RuoloUtenza ruoloUtenza, StoricoParticellaVO[] elencoParticelleEvento,
      Long idEvento) throws SolmrException
  {
    return iac.inserisciParticella(storicoParticellaVO, ruoloUtenza,
        elencoParticelleEvento, idEvento);
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
   * @throws SolmrException
   */
  public BigDecimal getTotSupCondottaByAziendaAndParticella(
      Long idAzienda, Long idParticella)
      throws SolmrException
  {
    return iac.getTotSupCondottaByAziendaAndParticella(idAzienda, idParticella);
  }

  /**
   * Metodo che mi permette di estrarre tutti gli eventi relativi ad una
   * particella
   * 
   * @param idParticella
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.EventoParticellaVO[]
   * @throws SolmrException
   */
  public EventoParticellaVO[] getEventiParticellaByIdParticellaNuovaOrCessata(
      Long idParticella, String[] orderBy) throws SolmrException
  {
    return iac.getEventiParticellaByIdParticellaNuovaOrCessata(idParticella,
        orderBy);
  }

  /**
   * Metodo che mi permette di estrarre l'elenco delle particelle associabili ad
   * un fabbricato relative ad un determinato piano di riferimento
   * 
   * @param idFabbricato
   * @param idPianoRiferimento
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws SolmrException
   */
  public StoricoParticellaVO[] getListParticelleForFabbricato(
      Long idFabbricato, Long idPianoRiferimento, String[] orderBy)
      throws SolmrException
  {
    return iac.getListParticelleForFabbricato(idFabbricato, idPianoRiferimento,
        orderBy);
  }

  /**
   * Metodo che si occupa di verificare se il piano di riferimento corrente è
   * frutto di un ripristino di una dichiarazione di consistenza
   * 
   * @param idAzienda
   * @return boolean
   * @throws SolmrException
   */
  public boolean isPianoRiferimentoRipristinato(Long idAzienda)
      throws SolmrException
  {
    return iac.isPianoRiferimentoRipristinato(idAzienda);
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
   * @throws SolmrException
   */
  public StoricoUnitaArboreaVO[] getListStoricoUnitaArboreaByLogicKey(
      Long idParticella, Long idPianoRiferimento, Long idAzienda,
      String[] orderBy) throws SolmrException
  {
    return iac.getListStoricoUnitaArboreaByLogicKey(idParticella,
        idPianoRiferimento, idAzienda, orderBy);
  }

  /**
   * Metodo che mi permette di estrarre l'elenco delle unità arboree relative
   * all'azienda agricola(stato attuale o piano di riferimento).
   * 
   * @param idAzienda
   * @param idPianoRiferimento
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws SolmrException
   */
  public StoricoParticellaVO[] getListStoricoUnitaArboreaByIdAzienda(
      Long idAzienda, Long idPianoRiferimento, String[] orderBy)
      throws SolmrException
  {
    return iac.getListStoricoUnitaArboreaByIdAzienda(idAzienda,
        idPianoRiferimento, orderBy);
  }

  /**
   * Metodo per recuperare la max data esecuzione controlli relativa alle unità
   * arboree
   * 
   * @param idAzienda
   * @return java.lang.String
   * @throws SolmrException
   */
  public java.lang.String getMaxDataEsecuzioneControlliUnitaArborea(
      Long idAzienda) throws SolmrException
  {
    return iac.getMaxDataEsecuzioneControlliUnitaArborea(idAzienda);
  }

  /**
   * Metodo che mi restituisce l'elenco delle anomalie relative all'unità
   * arborea selezionata
   * 
   * @param idStoricoUnitaArborea
   * @param orderBy
   * @return it.csi.solmr.dto.anag.EsitoControlloUnarVO[]
   * @throws SolmrException
   */
  public EsitoControlloUnarVO[] getListEsitoControlloUnarByIdStoricoUnitaArborea(
      Long idStoricoUnitaArborea, String[] orderBy) throws SolmrException
  {
    return iac.getListEsitoControlloUnarByIdStoricoUnitaArborea(
        idStoricoUnitaArborea, orderBy);
  }

  /**
   * Metodo che mi permette di recuperare l'unita arborea e i dati della
   * particella in funzione della chiave primaria
   * 
   * @param idStoricoUnitaArborea
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO
   * @throws SolmrException
   */
  public StoricoParticellaVO findStoricoParticellaArborea(
      Long idStoricoUnitaArborea) throws SolmrException
  {
    return iac.findStoricoParticellaArborea(idStoricoUnitaArborea);
  }
  
  public StoricoParticellaVO findStoricoParticellaArboreaBasic(
      Long idStoricoUnitaArborea) throws SolmrException
  {
    return iac.findStoricoParticellaArboreaBasic(idStoricoUnitaArborea);
  }
  
  public StoricoParticellaVO findStoricoParticellaArboreaConduzione(
      Long idStoricoUnitaArborea, long idAzienda) throws SolmrException
  {
    return iac.findStoricoParticellaArboreaConduzione(idStoricoUnitaArborea, idAzienda);
  }

  public StoricoParticellaVO findStoricoParticellaArboreaTolleranza(
      Long idStoricoUnitaArborea, long idAzienda, String nomeLib) throws SolmrException
  {
    return iac.findStoricoParticellaArboreaTolleranza(idStoricoUnitaArborea, idAzienda, nomeLib);
  }

  /**
   * Metodo che mi restituisce l'elenco degli altri vitigni associati all'unità
   * arborea
   * 
   * @param idStoricoUnitaArborea
   * @param orderBy
   * @return it.csi.solmr.dto.anag.AltroVitignoVO[]
   * @throws SolmrException
   */
  public AltroVitignoVO[] getListAltroVitignoByIdStoricoUnitaArborea(
      Long idStoricoUnitaArborea, String[] orderBy) throws SolmrException
  {
    return iac.getListAltroVitignoByIdStoricoUnitaArborea(
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
   * @throws SolmrException
   */
  public TipoUtilizzoVO[] getListTipiUsoSuoloByTipo(String tipo,
      boolean onlyActive, String[] orderBy) throws SolmrException
  {
    return iac.getListTipiUsoSuoloByTipo(tipo, onlyActive, orderBy);
  }

  /**
   * Metodo che mi restituisce l'elenco delle forme allevamento
   * 
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.TipoFormaAllevamentoVO
   * @throws SolmrException
   */
  public TipoFormaAllevamentoVO[] getListTipoFormaAllevamento(
      boolean onlyActive, String[] orderBy) throws SolmrException
  {
    return iac.getListTipoFormaAllevamento(onlyActive, orderBy);
  }

  /**
   * Metodo che mi restituisce l'elenco delle causali modifica
   * 
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.TipoCausaleModificaVO[]
   * @throws SolmrException
   */
  public TipoCausaleModificaVO[] getListTipoCausaleModifica(boolean onlyActive,
      String[] orderBy) throws SolmrException
  {
    return iac.getListTipoCausaleModifica(onlyActive, orderBy);
  }
  
  public Vector<TipoCausaleModificaVO> getListTipoCuasaleModificaByIdAzienda(long idAzienda)
    throws SolmrException
  {
    return iac.getListTipoCuasaleModificaByIdAzienda(idAzienda);
  }

  /**
   * Metodo che si occupa di modificare le unità arboree
   * 
   * @param elencoParticelleArboree
   * @param ruoloUtenza
   * @throws SolmrException
   */
  public void modificaUnitaArboree(
      StoricoParticellaVO[] elencoParticelleArboree, RuoloUtenza ruoloUtenza, String provenienza)
      throws SolmrException
  {
    iac.modificaUnitaArboree(elencoParticelleArboree, ruoloUtenza, provenienza);
  }

  /**
   * Metodo utilizzato per effettuare la cessazione delle unità arboree
   * 
   * @param elencoIdStoricoUnitaArboree
   * @param ruoloUtenza
   * @param idCausaleModifica
   * @param dataCessazione
   * @param note
   * @throws SolmrException
   */
  public void cessaUnitaArboree(Long[] elencoIdStoricoUnitaArboree,
      RuoloUtenza ruoloUtenza, Long idCausaleModifica, String note) throws SolmrException
  {
    iac.cessaUnitaArboree(elencoIdStoricoUnitaArboree, ruoloUtenza,
        idCausaleModifica, note);
  }

  /**
   * Metodo che mi restituisce l'elenco delle cessazioni unar
   * 
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.TipoCessazioneUnarVO[]
   * @throws SolmrException
   */
  public TipoCessazioneUnarVO[] getListTipoCessazioneUnar(boolean onlyActive,
      String[] orderBy) throws SolmrException
  {
    return iac.getListTipoCessazioneUnar(onlyActive, orderBy);
  }

  /**
   * Metodo che mi consente di recuperare l'elenco delle particelle importabili
   * 
   * @param idAzienda
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws SolmrException
   */
  public StoricoParticellaVO[] getListStoricoParticelleArboreeImportabili(
      Long idAzienda, String[] orderBy) throws SolmrException
  {
    return iac.getListStoricoParticelleArboreeImportabili(idAzienda, orderBy);
  }

  /**
   * Metodo che mi consente di importare le unità arboree da schedario
   * 
   * @param elencoIdStoricoUnitaArborea
   * @param ruoloUtenza
   * @param anagAziendaVO
   * @param idCausaleModifica
   * @param newIdParticella
   * @throws SolmrException
   */
  public void importUnitaArboreeBySchedario(Long[] elencoIdStoricoUnitaArborea,
      RuoloUtenza ruoloUtenza, AnagAziendaVO anagAziendaVO,
      Long idCausaleModifica, Long newIdParticella) throws SolmrException
  {
    iac.importUnitaArboreeBySchedario(elencoIdStoricoUnitaArborea, ruoloUtenza,
        anagAziendaVO, idCausaleModifica, newIdParticella);
  }

  /**
   * Metodo utilizzato per inserire un'unità arborea
   * 
   * @param storicoUnitaArboreaVO
   * @param storicoParticellaVO
   * @param anagAziendaVO
   * @param ruoloUtenza
   * @throws SolmrException
   */
  public void inserisciUnitaArborea(
      StoricoUnitaArboreaVO storicoUnitaArboreaVO,
      StoricoParticellaVO storicoParticellaVO, AnagAziendaVO anagAziendaVO,
      RuoloUtenza ruoloUtenza, String provenienza) throws SolmrException
  {
    iac.inserisciUnitaArborea(storicoUnitaArboreaVO, storicoParticellaVO,
        anagAziendaVO, ruoloUtenza, provenienza);
  }

  /**
   * Metodo utilizzato per ricercare le UV
   * 
   * @param idAzienda
   * @param filtriUnitaArboreaRicercaVO
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws SolmrException
   */
  public StoricoParticellaVO[] searchStoricoUnitaArboreaByParametersForStampa(
      Long idAzienda, FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO,
      String[] orderBy) throws SolmrException
  {
    return iac.searchStoricoUnitaArboreaByParametersForStampa(idAzienda,
        filtriUnitaArboreaRicercaVO, orderBy);
  }
  
  public StoricoParticellaVO[] searchStoricoUnitaArboreaByParameters(String nomeLib,
      Long idAzienda, FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO,
      String[] orderBy) throws SolmrException
  {
    return iac.searchStoricoUnitaArboreaByParameters(nomeLib,idAzienda,
        filtriUnitaArboreaRicercaVO, orderBy);
  }

  /**
   * Metodo che mi consente di effettuare la ricerca delle UV in funzione del
   * loro scarico in excel
   * 
   * @param idAzienda
   * @param idPianoRiferimento
   * @param orderBy
   * @return java.util.Vector
   * @throws SolmrException
   */
  public Vector<StoricoParticellaArboreaExcelVO> searchStoricoUnitaArboreaExcelByParameters(
      String nomeLib, Long idAzienda, FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO) 
    throws SolmrException
  {
    return iac.searchStoricoUnitaArboreaExcelByParameters(nomeLib, idAzienda,
        filtriUnitaArboreaRicercaVO);
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
   * @throws SolmrException
   */
  public ConduzioneParticellaVO[] getListConduzioneParticellaByIdAzienda(
      Long idAzienda, boolean onlyActive, String[] orderBy)
      throws SolmrException
  {
    return iac.getListConduzioneParticellaByIdAzienda(idAzienda, onlyActive,
        orderBy);
  }

  /**
   * Metodo utilizzato per importare le particelle da un'altra azienda
   * 
   * @param elencoConduzioni
   * @param idUte
   * @param anagAziendaVO
   * @param anagAziendaSearchVO
   * @param ruoloUtenza
   * @throws SolmrException
   */
  public void importParticelle(String[] elencoConduzioni, Long idUte,
      AnagAziendaVO anagAziendaVO, AnagAziendaVO anagAziendaSearchVO,
      RuoloUtenza ruoloUtenza, Long idTitoloPossesso) throws SolmrException
  {
    iac.importParticelle(elencoConduzioni, idUte, anagAziendaVO,
        anagAziendaSearchVO, ruoloUtenza, idTitoloPossesso);
  }

  public Vector<Object> importParticelleAsservite(String[] elencoConduzioni,
      Long idUte, AnagAziendaVO anagAziendaSearchVO, RuoloUtenza ruoloUtenza,
      Long idTitoloPossesso) throws SolmrException
  {
    return iac.importParticelleAsservite(elencoConduzioni, idUte,
       anagAziendaSearchVO, ruoloUtenza, idTitoloPossesso);
  }

  public Vector<Object> importParticelleAsserviteFromRicercaParticella(
      String[] elencoIdParticelle, Long idUte, AnagAziendaVO anagAziendaVO,
      RuoloUtenza ruoloUtenza, Long idTitoloPossesso) throws SolmrException
  {
    return iac
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
   * @throws SolmrException
   */
  public StoricoParticellaVO[] getListStoricoParticelleArboreeByParameters(
      String istatComune, String sezione, String foglio, String particella,
      String subalterno, boolean onlyActive, String[] orderBy)
      throws SolmrException
  {
    return iac.getListStoricoParticelleArboreeByParameters(istatComune,
        sezione, foglio, particella, subalterno, onlyActive, orderBy);
  }

  /**
   * Metodo che mi consente di recuperare il dettaglio dell'unità arborea
   * dichiarata
   * 
   * @param idUnitaArboreaDichiarata
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO
   * @throws SolmrException
   */
  public StoricoParticellaVO findParticellaArboreaDichiarata(
      Long idUnitaArboreaDichiarata) throws SolmrException
  {
    return iac.findParticellaArboreaDichiarata(idUnitaArboreaDichiarata);
  }
  
  public StoricoParticellaVO findParticellaArboreaDichiarataBasic(Long idUnitaArboreaDichiarata)
      throws SolmrException
  {
    return iac.findParticellaArboreaDichiarataBasic(idUnitaArboreaDichiarata);
  }

  /**
   * Metodo che mi restituisce l'elenco degli altri vitigni dichiarati associati
   * all'unità arborea
   * 
   * @param idUnitaArboreaDichiarata
   * @param orderBy
   * @return it.csi.solmr.dto.anag.AltroVitignoDichiaratoVO[]
   * @throws SolmrException
   */
  public AltroVitignoDichiaratoVO[] getListAltroVitignoDichiaratoByIdUnitaArboreaDichiarata(
      Long idUnitaArboreaDichiarata, String[] orderBy) throws SolmrException
  {
    return iac.getListAltroVitignoDichiaratoByIdUnitaArboreaDichiarata(
        idUnitaArboreaDichiarata, orderBy);
  }

  /**
   * Metodo che richiama una procedura PL-SQL che ribalta le UV sul piano
   * colturale allineandolo
   * 
   * @param idAzienda
   * @param idUtente
   * @throws SolmrException
   */
  public void ribaltaUVOnPianoColturale(Long idAzienda, BigDecimal[] idStoricoUnitaArborea, Long idUtente)
      throws SolmrException
  {
    iac.ribaltaUVOnPianoColturale(idAzienda, idStoricoUnitaArborea, idUtente);
  }

  /**
   * Metodo che mi restituisce l'elenco dei tipi vino
   * 
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.TipoVinoVO[]
   * @throws SolmrException
   */
  public TipoVinoVO[] getListTipoVino(boolean onlyActive, String[] orderBy)
      throws SolmrException
  {
    return iac.getListTipoVino(onlyActive, orderBy);
  }

  /**
   * Metodo che mi restituisce l'elenco delle tipologie vino
   * 
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.TipoTipologiaVinoVO[]
   * @throws SolmrException
   */
  public TipoTipologiaVinoVO[] getListTipoTipologiaVino(boolean onlyActive,
      String[] orderBy) throws SolmrException
  {
    return iac.getListTipoTipologiaVino(onlyActive, orderBy);
  }

  public TipoTipologiaVinoVO[] getListTipoTipologiaVinoForAzienda(long idAzienda)
      throws SolmrException
  {
    return iac.getListTipoTipologiaVinoForAzienda(idAzienda);
  }
  
  public TipoTipologiaVinoVO[] getListTipoTipologiaVinoForDichCons(long idDichiarazioneConsistenza)
      throws SolmrException
  {
    return iac.getListTipoTipologiaVinoForDichCons(idDichiarazioneConsistenza);
  }

  /**
   * Metodo per recuperare il tipo utilizzo a partire dalla sua chiave primaria
   * 
   * @param idUtilizzo
   * @return it.csi.solmr.dto.anag.terreni.TipoUtilizzoVO
   * @throws SolmrException
   */
  public TipoUtilizzoVO findTipoUtilizzoByPrimaryKey(Long idUtilizzo)
      throws SolmrException
  {
    return iac.findTipoUtilizzoByPrimaryKey(idUtilizzo);
  }
  
  public TipoUtilizzoVO[] getListTipoUtilizzoByIdAzienda(String tipo, long idAzienda)
    throws SolmrException
  {
    return iac.getListTipoUtilizzoByIdAzienda(tipo, idAzienda);
  }
  
  public TipoUtilizzoVO[] getListTipoUtilizzoByIdDichiarazioneConsistenza(String tipo, long idDichiarazioneConsistenza)
    throws SolmrException
  {
    return iac.getListTipoUtilizzoByIdDichiarazioneConsistenza(tipo, idDichiarazioneConsistenza);
  }

  /**
   * Metodo che mi permette di recuperare il tipo varietà a partire dalla sua
   * chiave primaria
   * 
   * @param idVarieta
   * @return it.csi.solmr.dto.anag.terreni.TipoVarietaVO
   * @throws SolmrException
   */
  public TipoVarietaVO findTipoVarietaByPrimaryKey(Long idVarieta)
      throws SolmrException
  {
    return iac.findTipoVarietaByPrimaryKey(idVarieta);
  }
  
  public TipoVarietaVO[] getListTipoVarietaByIdAzienda(long idAzienda)
      throws SolmrException
  {
    return iac.getListTipoVarietaByIdAzienda(idAzienda);
  }
  
  public TipoVarietaVO[] getListTipoVarietaByIdDichiarazioneConsistenza(long idDichiarazioneConsistenza)
      throws SolmrException
  {
    return iac.getListTipoVarietaByIdDichiarazioneConsistenza(idDichiarazioneConsistenza);
  }

  /**
   * Metodo per recuperare l'elenco delle tipologie vino a partire dalla
   * categoria
   * 
   * @param idVino
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.TipoTipologiaVinoVO[]
   * @throws SolmrException
   */
  public TipoTipologiaVinoVO[] getListTipoTipologiaVinoByIdVino(Long idVino,
      boolean onlyActive, String[] orderBy) throws SolmrException
  {
    return iac.getListTipoTipologiaVinoByIdVino(idVino, onlyActive, orderBy);
  }

  public Vector<TipoTipologiaVinoVO> getListActiveTipoTipologiaVinoByComune(
      String istatComune) throws SolmrException
  {
    return iac.getListActiveTipoTipologiaVinoByComune(istatComune);
  }
  
  public Vector<TipoTipologiaVinoVO> getListActiveTipoTipologiaVinoByComuneAndVarieta(
      String istatComune, Long idVarieta, Long idParticella) throws SolmrException
  {
    return iac.getListActiveTipoTipologiaVinoByComuneAndVarieta(istatComune, idVarieta, idParticella);
  }
  
  /*public Vector<VignaVO> getListActiveVignaByIdTipologiaVinoAndParticella(long idTipologiaVino, long idParticella)
      throws SolmrException
  {
    return iac.getListActiveVignaByIdTipologiaVinoAndParticella(idTipologiaVino, idParticella);
  }*/
  
  public Vector<TipoTipologiaVinoVO> getListTipoTipologiaVinoRicaduta(
      String istatComune, long idVarieta, long idTipologiaVino, java.util.Date dataInserimentoDichiarazione) 
  throws SolmrException
  {
    return iac.getListTipoTipologiaVinoRicaduta(istatComune, idVarieta, idTipologiaVino, dataInserimentoDichiarazione);
  }
  
  public TipoTipologiaVinoVO getTipoTipologiaVinoByPrimaryKey(long idTipologiaVino) 
    throws SolmrException
  {
    return iac.getTipoTipologiaVinoByPrimaryKey(idTipologiaVino);
  }

  /**
   * Metodo utilizzato per effettuare la dichiarazione dell'uso agronomico
   * 
   * @param elencoParticelle
   * @param ruoloUtenza
   * @param idAzienda
   * @throws SolmrException
   */
  public void dichiaraUsoAgronomico(StoricoParticellaVO[] elencoParticelle,
      RuoloUtenza ruoloUtenza, Long idAzienda) throws SolmrException
  {
    iac.dichiaraUsoAgronomico(elencoParticelle, ruoloUtenza, idAzienda);
  }

  /**
   * Metodo che si occupa di validare le unità arboree alla dichiarazione di
   * consistenza
   * 
   * @param elencoIdUnitaArboreaDichiarata
   * @throws SolmrException
   */
  public void validaUVPlSql(Long[] elencoIdUnitaArboreaDichiarata)
      throws SolmrException
  {
    iac.validaUVPlSql(elencoIdUnitaArboreaDichiarata);
  }

  /**
   * Metodo utilizzato per effettuare i riepiloghi per titolo di possesso
   * relativi ad'azienda agricola
   * 
   * @param idAzienda
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.ConduzioneParticellaVO[]
   * @throws SolmrException
   */
  public StoricoParticellaVO[] riepilogoTitoloPossesso(Long idAzienda) throws SolmrException
  {
    return iac.riepilogoTitoloPossesso(idAzienda);
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
   * @throws SolmrException
   */
  public StoricoParticellaVO[] riepilogoTitoloPossessoDichiarato(
      Long idAzienda, Long idDichiarazioneConsistenza)
      throws SolmrException
  {
    return iac.riepilogoTitoloPossessoDichiarato(idAzienda,
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
   * @throws SolmrException
   */
  public StoricoParticellaVO[] riepilogoPossessoComune(Long idAzienda) throws SolmrException
  {
    return iac.riepilogoPossessoComune(idAzienda);
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
   * @throws SolmrException
   */
  public StoricoParticellaVO[] riepilogoPossessoComuneDichiarato(
      Long idAzienda, Long idDichiarazioneConsistenza)
      throws SolmrException
  {
    return iac.riepilogoPossessoComuneDichiarato(idAzienda,
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
   * @throws SolmrException
   */
  public StoricoParticellaVO[] riepilogoComune(Long idAzienda,
      String escludiAsservimento)
      throws SolmrException
  {
    return iac.riepilogoComune(idAzienda, escludiAsservimento);
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
   * @throws SolmrException
   */
  public StoricoParticellaVO[] riepilogoComuneDichiarato(Long idAzienda,
      String escludiAsservimento, Long idDichiarazioneConsistenza) throws SolmrException
  {
    return iac.riepilogoComuneDichiarato(idAzienda, escludiAsservimento,
        idDichiarazioneConsistenza);
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
   * @throws SolmrException
   */
  public UtilizzoParticellaVO[] riepilogoUsoPrimario(Long idAzienda)
      throws SolmrException
  {
    return iac.riepilogoUsoPrimario(idAzienda);
  }
  
  public BigDecimal getTotSupSfriguAndAsservimento(Long idAzienda, String escludiAsservimento)
    throws SolmrException
  {
    return iac.getTotSupSfriguAndAsservimento(idAzienda, escludiAsservimento);
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
   * @throws SolmrException
   */
  public UtilizzoDichiaratoVO[] riepilogoUsoPrimarioDichiarato(Long idAzienda,
      Long idDichiarazioneConsistenza) throws SolmrException
  {
    return iac.riepilogoUsoPrimarioDichiarato(idAzienda,
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
   * @throws SolmrException
   */
  public UtilizzoParticellaVO[] riepilogoUsoSecondario(Long idAzienda)
      throws SolmrException
  {
    return iac.riepilogoUsoSecondario(idAzienda);
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
   * @throws SolmrException
   */
  public UtilizzoDichiaratoVO[] riepilogoUsoSecondarioDichiarato(
      Long idAzienda, Long idDichiarazioneConsistenza) throws SolmrException
  {
    return iac.riepilogoUsoSecondarioDichiarato(idAzienda, idDichiarazioneConsistenza);
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
   * @throws SolmrException
   */
  public UtilizzoParticellaVO[] riepilogoMacroUso(Long idAzienda)
      throws SolmrException
  {
    return iac.riepilogoMacroUso(idAzienda);
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
   * @throws SolmrException
   */
  public UtilizzoDichiaratoVO[] riepilogoMacroUsoDichiarato(Long idAzienda,
      Long idDichiarazioneConsistenza) throws SolmrException
  {
    return iac.riepilogoMacroUsoDichiarato(idAzienda, idDichiarazioneConsistenza);
  }
  
  public BigDecimal getTotSupAsservimento(Long idAzienda, Long idDichiarazioneConsistenza)
    throws SolmrException
  {
    return iac.getTotSupAsservimento(idAzienda, idDichiarazioneConsistenza);
  }

  /**
   * Metodo per estrarre i tipi macro uso associati ad un'azienda agricola
   * 
   * @param idAzienda
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.TipoMacroUsoVO[]
   * @throws SolmrException
   */
  public TipoMacroUsoVO[] getListTipoMacroUsoByIdAzienda(Long idAzienda,
      boolean onlyActive, String[] orderBy) throws SolmrException
  {
    return iac.getListTipoMacroUsoByIdAzienda(idAzienda, onlyActive, orderBy);
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
   * @throws SolmrException
   */
  public StoricoParticellaVO[] riepilogoZVNParticellePiemonte(Long idAzienda,
      String escludiAsservimento)
      throws SolmrException
  {
    return iac.riepilogoZVNParticellePiemonte(idAzienda, escludiAsservimento);
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
   * @throws SolmrException
   */
  public StoricoParticellaVO[] riepilogoZVNParticelleDichiaratePiemonte(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws SolmrException
  {
    return iac.riepilogoZVNParticelleDichiaratePiemonte(idAzienda,
        escludiAsservimento, idDichiarazioneConsistenza);
  }

  /**
   * Metodo per effettuare il riepilogo delle particelle per ZVN comprensivo di
   * fascia fluviale
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws SolmrException
   */
  public StoricoParticellaVO[] riepilogoZVNFasciaFluviale(Long idAzienda,
      String escludiAsservimento) throws SolmrException
  {
    return iac.riepilogoZVNFasciaFluviale(idAzienda, escludiAsservimento);
  }

  /**
   * Metodo per effettuare il riepilogo delle particelle per ZVN comprensivo di
   * fascia fluviale ad una determiinata dichiarazione di consistenza
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws SolmrException
   */
  public StoricoParticellaVO[] riepilogoZVNFasciaFluvialeDichiarate(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws SolmrException
  {
    return iac.riepilogoZVNFasciaFluvialeDichiarate(idAzienda,
        escludiAsservimento, idDichiarazioneConsistenza);
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
   * @throws SolmrException
   */
  public StoricoParticellaVO[] riepilogoZVFParticellePiemonte(Long idAzienda,
      String escludiAsservimento)
      throws SolmrException
  {
    return iac.riepilogoZVFParticellePiemonte(idAzienda, escludiAsservimento);
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
   * @throws SolmrException
   */
  public StoricoParticellaVO[] riepilogoZVFParticelleDichiaratePiemonte(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws SolmrException
  {
    return iac.riepilogoZVFParticelleDichiaratePiemonte(idAzienda,
        escludiAsservimento, idDichiarazioneConsistenza);
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
   * @throws SolmrException
   */
  public StoricoParticellaVO[] riepilogoLocalizzazioneParticellePiemonte(
      Long idAzienda, String escludiAsservimento) throws SolmrException
  {
    return iac.riepilogoLocalizzazioneParticellePiemonte(idAzienda,
        escludiAsservimento);
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
   * @throws SolmrException
   */
  public StoricoParticellaVO[] riepilogoLocalizzazioneParticelleDichiaratePiemonte(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws SolmrException
  {
    return iac.riepilogoLocalizzazioneParticelleDichiaratePiemonte(idAzienda,
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
   * @throws SolmrException
   */
  public StoricoParticellaVO[] riepilogoFasciaFluvialeParticelle(
      Long idAzienda, String escludiAsservimento) throws SolmrException
  {
    return iac.riepilogoFasciaFluvialeParticelle(idAzienda,
        escludiAsservimento);
  }

  /**
   * Metodo per effettuare il riepilogo delle particelle per FasciaFluviale ad
   * una determinata dichiarazione di consistenza
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws SolmrException
   */
  public StoricoParticellaVO[] riepilogoFasciaFluvialeParticelleDichiarate(
      Long idAzienda, String escludiAsservimento,
      Long idDichiarazioneConsistenza) throws SolmrException
  {
    return iac.riepilogoFasciaFluvialeParticelleDichiarate(idAzienda,
        escludiAsservimento, idDichiarazioneConsistenza);
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
      String escludiAsservimento) throws SolmrException
  {
    return iac.riepilogoAreaG(idAzienda, escludiAsservimento);
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
      Long idDichiarazioneConsistenza) throws SolmrException
  {

    return iac.riepilogoAreaGParticelleDichiarate(idAzienda,
        escludiAsservimento, idDichiarazioneConsistenza);
  }

  /**
   * 
   * Metodo per effettuare il riepilogo delle particelle con area soggetta ad
   * erosione
   * 
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @return
   * @throws Exception
   */
  public StoricoParticellaVO[] riepilogoAreaH(Long idAzienda,
      String escludiAsservimento) throws SolmrException
  {
    return iac.riepilogoAreaH(idAzienda, escludiAsservimento);
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
      Long idDichiarazioneConsistenza) throws SolmrException
  {

    return iac.riepilogoAreaHParticelleDichiarate(idAzienda,
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
      String escludiAsservimento) throws SolmrException
  {

    return iac.riepilogoZonaAltimetrica(idAzienda, escludiAsservimento);
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
      Long idDichiarazioneConsistenza) throws SolmrException
  {
    return iac.riepilogoZonaAltimetricaParticelleDichiarate(idAzienda,
        escludiAsservimento, idDichiarazioneConsistenza);
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
      String escludiAsservimento) throws SolmrException
  {
    return iac.riepilogoCasoParticolare(idAzienda, escludiAsservimento);
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
      Long idDichiarazioneConsistenza) throws SolmrException
  {
    return iac.riepilogoCasoParticolareParticelleDichiarate(idAzienda,
        escludiAsservimento, idDichiarazioneConsistenza);
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
   * @throws SolmrException
   */
  public Vector<TipoVarietaVO> getListTipoVarietaVitignoByMatriceAndComune(long idUtilizzo, 
      long idTipoDestinazione, long idTipoDettaglioUso, long idTipoQualitaUso, 
      String istatComune) throws SolmrException
  {
    return iac.getListTipoVarietaVitignoByMatriceAndComune(idUtilizzo, 
        idTipoDestinazione, idTipoDettaglioUso, idTipoQualitaUso, istatComune);
  }

  /**
   * Metodo che mi permette di estrarre la somma delle superfici condotte
   * relative ad una determinata azienda agriocola
   * 
   * @param idAzienda
   * @param onlyActive
   * @return java.lang.String
   * @throws SolmrException
   */
  public java.lang.String getTotSupCondottaByAzienda(Long idAzienda,
      boolean onlyActive) throws SolmrException
  {
    return iac.getTotSupCondottaByAzienda(idAzienda, onlyActive);
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
   * @throws SolmrException
   */
  public StoricoParticellaVO[] riepilogoTipoArea(Long idAzienda,
      String escludiAsservimento, String tipoArea) throws SolmrException
  {
    return iac.riepilogoTipoArea(idAzienda, escludiAsservimento, tipoArea);
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
   * @throws SolmrException
   */
  public StoricoParticellaVO[] riepilogoTipoAreaDichiarato(Long idAzienda,
      String escludiAsservimento, Long idDichiarazioneConsistenza,
      String tipoArea) throws SolmrException
  {
    return iac.riepilogoTipoAreaDichiarato(idAzienda, escludiAsservimento,
        idDichiarazioneConsistenza, tipoArea);
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
   * @throws SolmrException
   */
  public UnitaArboreaDichiarataVO[] getListUnitaArboreaDichiarataByPianoRiferimentoAndIdStoricoUnitaArborea(
      Long idStoricoParticella, Long idAzienda, Long idPianoRiferimento,
      String[] orderBy) throws SolmrException
  {
    return iac
        .getListUnitaArboreaDichiarataByPianoRiferimentoAndIdStoricoUnitaArborea(
            idStoricoParticella, idAzienda, idPianoRiferimento, orderBy);
  }
  
  public long getIdUnitaArboreaDichiarata(long idStoricoUnitaArborea, long idDichiarazioneConsistenza)
      throws SolmrException
  {
    return iac.getIdUnitaArboreaDichiarata(idStoricoUnitaArborea, idDichiarazioneConsistenza);
  }
  
  public Long getDefaultIdGenereIscrizione() throws SolmrException
  {
    return iac.getDefaultIdGenereIscrizione();
  }
  
  public Vector<TipoGenereIscrizioneVO> getListTipoGenereIscrizione() throws SolmrException
  {
    return iac.getListTipoGenereIscrizione();
  }
  
  public BigDecimal getSupEleggPlSqlTotale(long idParticellaCertificata, long idCatalogoMatrice)
    throws SolmrException
  {
    return iac.getSupEleggPlSqlTotale(idParticellaCertificata, idCatalogoMatrice);
  }

  public BigDecimal getSupEleggNettaPlSqlTotale(long idParticellaCertificata, long idCatalogoMatrice)
    throws SolmrException
  {    
    return iac.getSupEleggNettaPlSqlTotale(idParticellaCertificata, idCatalogoMatrice);
  }
  
  public Vector<TipoMenzioneGeograficaVO> getListTipoMenzioneGeografica(long idParticella, 
      Long idTipologiaVino, java.util.Date dataInserimentoDichiarazione)
    throws SolmrException
  {
    return iac.getListTipoMenzioneGeografica(idParticella, idTipologiaVino, dataInserimentoDichiarazione);
  }
  
  public Vector<StoricoParticellaVO> getListParticelleByIdDocumento(Long idDocumento, String[] orderBy)
      throws SolmrException
  {
    return iac.getListParticelleByIdDocumento(idDocumento, orderBy);
  }
  
  public void cambiaPercentualePossesso(Long[] elencoIdConduzioneParticella, Vector<Long> vIdParticella,
      RuoloUtenza ruoloUtenza, Long idAzienda, BigDecimal percentualePossesso) throws SolmrException
  {
    iac.cambiaPercentualePossesso(elencoIdConduzioneParticella, vIdParticella, ruoloUtenza, idAzienda, percentualePossesso);
  }
  
  public void cambiaPercentualePossessoSupUtilizzata(Vector<Long> vIdConduzioni,
      RuoloUtenza ruoloUtenza, Long idAzienda)  throws SolmrException
  {
    iac.cambiaPercentualePossessoSupUtilizzata(vIdConduzioni, ruoloUtenza, idAzienda);
  }

  public RigaRicercaTerreniVO[] getRigheRicercaTerreniByIdParticellaRange(
      long ids[]) throws SolmrException
  {
    return iac.getRigheRicercaTerreniByIdParticellaRange(ids);
  }

  public RigaRicercaTerreniImportaAsservimentoVO[] getRigheRicercaTerreniImportaAsservimentoByIdConduzioneRange(
      long ids[]) throws SolmrException
  {
    return iac
        .getRigheRicercaTerreniImportaAsservimentoByIdConduzioneRange(ids);
  }

  public RigaRicercaTerreniImportaAsservimentoVO[] getRigheRicercaTerreniImportaAsservimentoByIdParticellaRange(
      long ids[]) throws SolmrException
  {
    return iac
        .getRigheRicercaTerreniImportaAsservimentoByIdParticellaRange(ids);
  }

  public long[] ricercaIdParticelleTerreni(
      FiltriRicercaTerrenoVO filtriRicercaTerrenoVO) throws SolmrException
  {
    return iac.ricercaIdParticelleTerreni(filtriRicercaTerrenoVO);
  }

  public long[] ricercaIdConduzioneTerreniImportaAsservimento(
      FiltriRicercaTerrenoImportaAsservimentoVO filtriRicercaTerrenoVO)
      throws SolmrException
  {
    return iac
        .ricercaIdConduzioneTerreniImportaAsservimento(filtriRicercaTerrenoVO);
  }

  /**
   * Metodo che mi permette di recuperare le provincie delle unita arboree
   * 
   * @param idStoricoUnitaArborea
   * @return Vector di String
   * @throws DataAccessException
   */
  public Vector<String> findProvinciaStoricoParticellaArborea(
      long[] idStoricoUnitaArborea) throws SolmrException
  {
    return iac.findProvinciaStoricoParticellaArborea(idStoricoUnitaArborea);
  }
  
  public Vector<String> findProvinciaStoricoParticellaArboreaIsolaParcella(long idAzienda, long[] idIsolaParcella)
    throws SolmrException
  {
    return iac.findProvinciaStoricoParticellaArboreaIsolaParcella(idAzienda, idIsolaParcella);
  }
  
  public BigDecimal getSumAreaUVParticella(long idAzienda, long idParticella) 
    throws SolmrException
  {
    return iac.getSumAreaUVParticella(idAzienda, idParticella);
  }
  
  public int getNumUVParticella(long idAzienda, long idParticella)
    throws SolmrException
  {
    return iac.getNumUVParticella(idAzienda, idParticella);
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
      long[] idUnitaArboreaDichiarata) throws SolmrException
  {
    return iac
        .findProvinciaParticellaArboreaDichiarata(idUnitaArboreaDichiarata);
  }
  
  public StoricoUnitaArboreaVO findStoricoUnitaArborea(Long idStoricoUnitaArborea) 
    throws SolmrException
  {
    return iac.findStoricoUnitaArborea(idStoricoUnitaArborea);
  }

  // FINE NUOVO TERRITORIALE

  // NUOVO FABBRICATO

  /**
   * Metodo utilizzato per estrarre i fabbricati di un'azienda relativi ad una
   * specifica particella
   * 
   * @param idConduzioneParticella
   * @param orderBy
   * @param onlyActive
   * @return
   * @throws SolmrException
   */
  public FabbricatoParticellaVO[] getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella(
      Long idConduzioneParticella, Long idAzienda, String[] orderBy,
      boolean onlyActive) throws SolmrException
  {
    return iac.getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella(
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
   * @throws SolmrException
   */
  public FabbricatoVO[] getListFabbricatiAziendaByPianoRifererimento(
      Long idAzienda, Long idPianoRiferimento, Long idUte, String[] orderBy)
      throws SolmrException
  {
    return iac.getListFabbricatiAziendaByPianoRifererimento(idAzienda,
        idPianoRiferimento, idUte, orderBy);
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
   * @throws SolmrException
   */
  public Vector<UteVO> getListUteByIdAzienda(Long idAzienda,
      boolean onlyActive, String[] orderBy) throws SolmrException
  {
    return iac.getListUteByIdAzienda(idAzienda, onlyActive, orderBy);
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
      Long idAzienda, long idPianoRiferimento) throws SolmrException
  {
    return iac.getListUteByIdAziendaAndIdPianoRiferimento(idAzienda,
        idPianoRiferimento);
  }

  /**
   * Metodo per estrarre l'unità produttiva a partire dalla sua chiave primaria
   * 
   * @param idUte
   * @return it.csi.solmr.dto.anag.UteVO
   * @throws SolmrException
   */
  public UteVO findUteByPrimaryKey(Long idUte) throws SolmrException
  {
    return iac.findUteByPrimaryKey(idUte);
  }

  // FINE NUOVA GESTIONE UTE

  // INIZIO GESTIONE DOCUMENTI

  /**
   * Metodo per recuperare tutti i dati presenti nella tabella
   * DB_TIPO_STATO_DOCUMENTO
   * 
   * @param isActive
   *          boolean
   * @return Vector
   * @throws SolmrException
   */
  public Vector<TipoStatoDocumentoVO> getListTipoStatoDocumento(boolean isActive)
      throws SolmrException
  {
    return iac.getListTipoStatoDocumento(isActive);
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
   * @throws SolmrException
   */
  public Vector<DocumentoVO> searchDocumentiByParameters(DocumentoVO documentoVO,
      String protocollazione, String[] orderBy) throws SolmrException
  {
    return iac.searchDocumentiByParameters(documentoVO, protocollazione,
        orderBy);
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
   * @throws SolmrException
   */
  public TipoDocumentoVO[] getListTipoDocumentoByIdTipologiaDocumento(
      Long idTipologiaDocumento, boolean isActive) throws SolmrException
  {
    return iac.getListTipoDocumentoByIdTipologiaDocumento(idTipologiaDocumento,
        isActive);
  }

  /**
   * Metodo per recuperare il record tipo documento a partire dalla chiave
   * primaria
   * 
   * @param idDocumento
   *          Long
   * @return TipoDocumentoVO
   * @throws SolmrException
   */
  public TipoDocumentoVO findTipoDocumentoVOByPrimaryKey(Long idDocumento)
      throws SolmrException
  {
    return iac.findTipoDocumentoVOByPrimaryKey(idDocumento);
  }

  /**
   * Metodo che si occupa di verificare se esiste già un record su DB_DOCUMENTO
   * con i dati chiave dell'area anagrafica del documento
   * 
   * @param documentoFiltroVO
   *          DocumentoVO
   * @return DocumentoVO
   * @throws SolmrException
   */
  public DocumentoVO findDocumentoVOBydDatiAnagrafici(
      DocumentoVO documentoFiltroVO) throws SolmrException
  {
    return iac.findDocumentoVOBydDatiAnagrafici(documentoFiltroVO);
  }

  /**
   * Metodo che si occupa di inserire il documento in tutte le sue parti
   * 
   * @param documentoVO
   *          DocumentoVO
   * @param ruoloUtenza
   *          RuoloUtenza
   * @param anno
   *          String
   * @param elencoProprietari
   *          Vector
   * @param elencoParticelle
   *          StoricoParticellaVO[]
   * @return Long
   * @throws SolmrException
   */
  public Long inserisciDocumento(DocumentoVO documentoVO,
      RuoloUtenza ruoloUtenza, String anno, Vector<DocumentoProprietarioVO> elencoProprietari,
      StoricoParticellaVO[] elencoParticelle, Vector<ParticellaAssVO> particelleAssociate)
      throws SolmrException
  {
    return iac.inserisciDocumento(documentoVO, ruoloUtenza, anno,
        elencoProprietari, elencoParticelle, particelleAssociate);
  }

  /**
   * Metodo per recuperare il documento a partire dalla chiave primaria
   * 
   * @param idDocumento
   *          Long
   * @return DocumentoVO
   * @throws SolmrException
   */
  public DocumentoVO findDocumentoVOByPrimaryKey(Long idDocumento)
      throws SolmrException
  {
    return iac.findDocumentoVOByPrimaryKey(idDocumento);
  }

  /**
   * Metodo che si occupa di reperire tutti i dati del dettaglio del documento
   * 
   * @param idDocumento
   *          Long
   * @param legamiAttivi
   * @return DocumentoVO
   * @throws SolmrException
   */
  public DocumentoVO getDettaglioDocumento(Long idDocumento,
      boolean legamiAttivi) throws SolmrException
  {
    return iac.getDettaglioDocumento(idDocumento, legamiAttivi);
  }

  /**
   * Metodo che si occupa di reperire i proprietari del documento relativamente
   * ad una data di dichiarazione della consistenza
   * 
   * @param idDocumento
   *          Long
   * @param dataConsistenza
   *          Date
   * @param idDichiarazioneConsistenza
   *          Long
   * @return Vector
   * @throws SolmrException
   */
  public DocumentoVO getDettaglioDocumento(Long idDocumento,
      java.util.Date dataConsistenza, Long idDichiarazioneConsistenza)
      throws SolmrException
  {
    return iac.getDettaglioDocumento(idDocumento, dataConsistenza,
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
   * @throws SolmrException
   */
  public Long aggiornaDocumento(DocumentoVO documentoVO,
      RuoloUtenza ruoloUtenza, String operazioneRichiesta)
      throws SolmrException
  {
    return iac.aggiornaDocumento(documentoVO, ruoloUtenza, operazioneRichiesta);
  }
  
  public Long aggiornaDocumentoIstanzaLimitato(DocumentoVO documentoVO,
      RuoloUtenza ruoloUtenza, String operazioneRichiesta) throws SolmrException
  {
    return iac.aggiornaDocumentoIstanzaLimitato(documentoVO, ruoloUtenza, operazioneRichiesta);
  }

  /**
   * Metodo che si occupa di eliminare i documenti
   * 
   */
  public void deleteDocumenti(String[] documentiDaEliminare, String note,
      long idUtenteAggiornamento) throws SolmrException
  {
    iac.deleteDocumenti(documentiDaEliminare, note, idUtenteAggiornamento);
  }

  /**
   * Metodo che si occupa di effettuare la protocollazione dei documenti
   * 
   */
  public void protocollaDocumenti(String[] documentiDaProtocollare, Long idAzienda,
      RuoloUtenza ruoloUtenza) throws SolmrException
  {
    iac.protocollaDocumenti(documentiDaProtocollare, idAzienda, ruoloUtenza);
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
   * @throws SolmrException
   */
  public Vector<DocumentoVO> getListDettaglioDocumentoByIdConduzione(Long idConduzione,
      boolean isStorico, boolean altreParticelle) throws SolmrException
  {
    return iac.getListDettaglioDocumentoByIdConduzione(idConduzione, isStorico,
        altreParticelle);
  }
  
  
  public Vector<DocumentoVO> getListDettaglioDocumentoByIdConduzionePopUp(Long idConduzioneParticella)
      throws SolmrException
  {
    return iac.getListDettaglioDocumentoByIdConduzionePopUp(idConduzioneParticella);
  }
  
  
  public Vector<DocumentoVO> getListDettaglioDocumentoByIdConduzioneDichiarataPopUp(Long idConduzioneDichiarata)
     throws SolmrException
  {
    return iac.getListDettaglioDocumentoByIdConduzioneDichiarataPopUp(idConduzioneDichiarata);
  }

  /**
   * Metodo che mi restituisce l'elenco dei documenti associati ad un'azienda
   * 
   * @param idAzienda
   * @param onlyActive
   * @param orderBy
   * @param idTipologiaDocumento
   * @return it.csi.solmr.dto.anag.DocumentoVO[]
   * @throws SolmrException
   */
  public DocumentoVO[] getListDocumentiByIdAzienda(Long idAzienda,
      boolean onlyActive, String[] orderBy, Long idTipologiaDocumento)
      throws SolmrException
  {
    return iac.getListDocumentiByIdAzienda(idAzienda, onlyActive, orderBy,
        idTipologiaDocumento);
  }

  /**
   * Metodo per recuperare tutti i dati presenti nella tabella
   * DB_CATEGORIA_DOCUMENTO
   * 
   * @param idTipologiaDocumento
   * @param orderBy[]
   * @return it.csi.solmr.dto.anag.TipoCategoriaDocumentoVO[]
   * @throws SolmrException
   */
  public TipoCategoriaDocumentoVO[] getListTipoCategoriaDocumentoByIdTipologiaDocumento(
      Long idTipologiaDocumento, String orderBy[]) throws SolmrException
  {
    return iac.getListTipoCategoriaDocumentoByIdTipologiaDocumento(
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
   * @throws SolmrException
   */
  public TipoDocumentoVO[] getListTipoDocumentoByIdCategoriaDocumento(
      Long idCategoriaDocumento, String orderBy[], boolean onlyActive,
      Boolean cessata) throws SolmrException
  {
    return iac.getListTipoDocumentoByIdCategoriaDocumento(idCategoriaDocumento,
        orderBy, onlyActive, cessata);
  }

  public TipoDocumentoVO[] getListTipoDocumentoByIdCategoriaDocumento(
      Long idCategoriaDocumento, String orderBy[], boolean onlyActive)
      throws SolmrException
  {
    return iac.getListTipoDocumentoByIdCategoriaDocumento(idCategoriaDocumento,
        orderBy, onlyActive, null);
  }

  /**
   * Metodo per recuperare il record su DB_CATEGORIA_DOCUMENTO a partire dalla
   * chiave primaria
   * 
   * @param idCategoriaDocumento
   * @return it.csi.solmr.dto.anag.TipoCategoriaDocumentoVO
   * @throws SolmrException
   */
  public TipoCategoriaDocumentoVO findTipoCategoriaDocumentoByPrimaryKey(
      Long idCategoriaDocumento) throws SolmrException
  {
    return iac.findTipoCategoriaDocumentoByPrimaryKey(idCategoriaDocumento);
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
   * @throws SolmrException
   */
  public TipoDocumentoVO[] getListTipoDocumentoByIdCategoriaDocumentoAndIdAzienda(
      Long idCategoriaDocumento, Long idAzienda, String orderBy[])
      throws SolmrException
  {
    return iac.getListTipoDocumentoByIdCategoriaDocumentoAndIdAzienda(
        idCategoriaDocumento, idAzienda, orderBy);
  }

  /**
   * Metodo che mi permette di estrarre la data max di esecuzione controlli dei
   * documenti relativi ad una determinata azienda
   * 
   * @param idAzienda
   * @return dataEsecuzione
   * @throws SolmrException
   */
  public String getDataMaxEsecuzioneDocumento(Long idAzienda)
      throws SolmrException
  {
    return iac.getDataMaxEsecuzioneDocumento(idAzienda);
  }

  /**
   * Metodo per estrarre i cuaa di tutti i proprietari dei documenti
   * dell'azienda agricola indicata
   * 
   * @param idAzienda
   * @param cuaa
   * @param onlyActive
   * @return java.lnag.String[]
   * @throws SolmrException
   */
  public String[] getCuaaProprietariDocumentiAzienda(Long idAzienda,
      String cuaa, boolean onlyActive) throws SolmrException
  {
    return iac.getCuaaProprietariDocumentiAzienda(idAzienda, cuaa, onlyActive);
  }

  /**
   * Metodo che mi restituisce l'elenco delle anomalie relative al documento
   * selezionato
   * 
   * @param idDocumento
   * @param orderBy
   * @return it.csi.solmr.dto.anag.EsitoControlloDocumentoVO[]
   * @throws SolmrException
   */
  public EsitoControlloDocumentoVO[] getListEsitoControlloDocumentoByIdDocumento(
      Long idDocumento, String[] orderBy) throws SolmrException
  {
    return iac
        .getListEsitoControlloDocumentoByIdDocumento(idDocumento, orderBy);
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
   * @throws SolmrException
   */
  public DocumentoVO[] getListDocumentiAziendaByIdControllo(
      AnagAziendaVO anagAziendaVO, Long idControllo, boolean onlyActive,
      String[] orderBy) throws SolmrException
  {
    return iac.getListDocumentiAziendaByIdControllo(anagAziendaVO, idControllo,
        onlyActive, orderBy);
  }

  /**
   * Metodo che mi permette di recuperare l'elenco dei tipi documenti relativi
   * ad un determinato id controllo
   * 
   * @param idControllo
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.TipoDocumentoVO[]
   * @throws SolmrException
   */
  public TipoDocumentoVO[] getListTipoDocumentoByIdControllo(Long idControllo,
      boolean onlyActive, String orderBy[]) throws SolmrException
  {
    return iac.getListTipoDocumentoByIdControllo(idControllo, onlyActive,
        orderBy);
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
   * @throws SolmrException
   */
  public DocumentoVO[] getListDocumentiAziendaByIdControlloAndParticella(
      AnagAziendaVO anagAziendaVO, Long idControllo, Long idStoricoParticella,
      boolean onlyActive, String[] orderBy) throws SolmrException
  {
    return iac.getListDocumentiAziendaByIdControlloAndParticella(anagAziendaVO,
        idControllo, idStoricoParticella, onlyActive, orderBy);
  }

  public DocumentoVO[] getListDocumentiPerDichCorrettiveAziendaByIdControlloAndParticella(
      AnagAziendaVO anagAziendaVO, Long idControllo, Long idStoricoParticella,
      String annoCampagna, String[] orderBy) throws SolmrException
  {
    return iac
        .getListDocumentiPerDichCorrettiveAziendaByIdControlloAndParticella(
            anagAziendaVO, idControllo, idStoricoParticella, annoCampagna,
            orderBy);
  }

  public DocumentoVO[] getListDocumentiPerDichCorrettiveAziendaByIdControllo(
      AnagAziendaVO anagAziendaVO, Long idControllo, String annoCampagna,
      String[] orderBy) throws SolmrException
  {
    return iac.getListDocumentiPerDichCorrettiveAziendaByIdControllo(
        anagAziendaVO, idControllo, annoCampagna, orderBy);
  }
  
  public Vector<DocumentoConduzioneVO> getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella(
      Long idDocumento, Long idConduzioneParticella, boolean onlyActive) throws SolmrException
  {
    return iac.getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella(
        idDocumento, idConduzioneParticella, onlyActive);
  }
  
  public Vector<StoricoParticellaVO> getListParticelleByIdDocumentoAlPianoCorrente(
      Long idDocumento) throws SolmrException
  {
    return iac.getListParticelleByIdDocumentoAlPianoCorrente(idDocumento);
  }

  // FINE GESTIONE DOCUMENTI

  // INIZIO ANAGRAFICA

  /**
   * Metodo che si occupa di reperire i dati dalla tabella DB_TIPO_CESSAZIONE
   * 
   * @param orderBy
   * @param onlyActive
   * @return it.csi.solmr.dto.anag.TipoCessazioneVO[]
   * @throws SolmrException
   */
  public TipoCessazioneVO[] getListTipoCessazione(String orderBy,
      boolean onlyActive) throws SolmrException
  {
    return iac.getListTipoCessazione(orderBy, onlyActive);
  }

  /**
   * Metodo utilizzato per recuperare l'elenco dell'aziende in funzione del CUAA
   * 
   * @param cuaa
   * @param onlyActive
   * @param isCessata
   * @param orderBy
   * @return it.csi.solmr.dto.anag.AnagAziendaVO[]
   * @throws SolmrException
   */
  public AnagAziendaVO[] getListAnagAziendaVOByCuaa(String cuaa,
      boolean onlyActive, boolean isCessata, String[] orderBy)
      throws SolmrException
  {
    return iac.getListAnagAziendaVOByCuaa(cuaa, onlyActive, isCessata, orderBy);
  }

  /**
   * Metodo che mi restituisce la MAX relativa alla data inizio validita di
   * un'azienda
   * 
   * @param idAzienda
   * @return java.util.Date
   * @throws SolmrException
   */
  public Date getMaxDataInizioValiditaAnagraficaAzienda(Long idAzienda)
      throws SolmrException
  {
    return iac.getMaxDataInizioValiditaAnagraficaAzienda(idAzienda);
  }

  /**
   * Metodo che mi permette di estrarre tutte le occorrenze dalla tabella
   * DB_ANAGRAFICA_AZIENDA in relazione all'id_azienda di destinazione
   * 
   * @param idAzienda
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.AnagAziendaVO[]
   * @throws SolmrException
   */
  public AnagAziendaVO[] getListAnagAziendaDestinazioneByIdAzienda(
      Long idAzienda, boolean onlyActive, String[] orderBy)
      throws SolmrException
  {
    return iac.getListAnagAziendaDestinazioneByIdAzienda(idAzienda, onlyActive,
        orderBy);
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
   * @throws SolmrException
   */
  public AnagAziendaVO[] getListAziendeParticelleAsservite(
      Long idStoricoParticella, Long idAzienda, Long idTitoloPossesso, Date dataInserimentoDichiarazione)
      throws SolmrException
  {
    return iac.getListAziendeParticelleAsservite(idStoricoParticella,
        idAzienda, idTitoloPossesso, dataInserimentoDichiarazione);
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
   * @throws SolmrException
   */
  public TipoAttestazioneVO[] getListTipoAttestazioneOfPianoRiferimento(
      Long idAzienda, String codiceFotografiaTerreni,
      java.util.Date dataAnnoCampagna, java.util.Date dataVariazione, 
      String[] orderBy, String voceMenu) throws SolmrException
  {
    return iac.getListTipoAttestazioneOfPianoRiferimento(idAzienda,
        codiceFotografiaTerreni, dataAnnoCampagna, dataVariazione, orderBy, voceMenu);
  }

  /**
   * Metodo utilizzato per confrontare la dataInserimentoDichiarazione con
   * MIN(TA.DATA_INIZIO_VALIDITA) e MAX(TA.DATA_FINE_VALIDITA) della tabella
   * DB_TIPO_ATTESTAZIONE
   * 
   * @param codiceFotografiaTerreni
   * @param dataInserimentoDichiarazione
   * @param flagVideo
   * @param flagStampa
   * @param codiceAttestazione
   * @param voceMenu
   * @return Integer possibili valori restituiti dal metodo null: non è stato
   *         trovato alcun record sul DB -1: la data
   *         dataInserimentoDichiarazione è minore della data
   *         MIN(TA.DATA_INIZIO_VALIDITA) 0: la data
   *         dataInserimentoDichiarazione è compresa fra la data
   *         MIN(TA.DATA_INIZIO_VALIDITA) e la data MAX(TA.DATA_FINE_VALIDITA)
   *         +1: la data dataInserimentoDichiarazione è maggiore della data
   *         MAX(TA.DATA_FINE_VALIDITA)
   * @throws SolmrException
   */
  public boolean getDataAttestazioneAllaDichiarazione(
      String codiceFotografiaTerreni,
      java.util.Date dataInserimentoDichiarazione, boolean flagVideo,
      boolean flagStampa, String codiceAttestazione, String voceMenu)
      throws SolmrException
  {
    return iac.getDataAttestazioneAllaDichiarazione(codiceFotografiaTerreni,
        dataInserimentoDichiarazione, flagVideo, flagStampa,
        codiceAttestazione, voceMenu);
  }

  /**
   * Metodo per verificare la presenza di attestazioni dichiarate
   * 
   * @param codiceFotografiaTerreni
   * @return boolean
   * @throws SolmrException
   */
  public boolean isAttestazioneDichiarata(String codiceFotografiaTerreni)
      throws SolmrException
  {
    return iac.isAttestazioneDichiarata(codiceFotografiaTerreni);
  }
  
  /**
   * Metodo per verificare la presenza di attestazioni azienda
   * 
   * @param idAzienda
   * @return boolean
   * @throws SolmrException
   */
  public boolean isAttestazioneAzienda(long idAzienda)
      throws SolmrException
  {
    return iac.isAttestazioneAzienda(idAzienda);
  }

  /**
   * Metodo che si occupa di richiamare una procedura PL-SQL per calcolare le
   * dichiarazioni che possono già essere selezionate dal sistema
   * 
   * @param idAzienda
   * @param idUtente
   * @param voceMenu
   * @throws SolmrException
   */
  public void aggiornaAttestazioniPlSql(Long idAzienda, Long idUtente,
      Long idDichiarazioneConsistenza, String codAttestazione) throws SolmrException
  {
    iac.aggiornaAttestazioniPlSql(idAzienda, idUtente, idDichiarazioneConsistenza, codAttestazione);
  }

  /**
   * Metodo che mi permette di effettuare l'aggiornamento delle dichiarazioni
   * associate all'azienda
   * 
   * @param elencoIdAttestazioni
   * @param anagAziendaVO
   * @param ruoloUtenza
   * @param elencoParametri
   * @param voceMenu
   * @param codiceFotografiaTerreni
   * @throws SolmrException
   */
  public void aggiornaAttestazioni(String[] elencoIdAttestazioni,
      AnagAziendaVO anagAziendaVO, RuoloUtenza ruoloUtenza,
      Hashtable<?,?> elencoParametri, String voceMenu, ConsistenzaVO consistenzaVO)
      throws SolmrException
  {
    iac.aggiornaAttestazioni(elencoIdAttestazioni, anagAziendaVO, ruoloUtenza,
        elencoParametri, voceMenu, consistenzaVO);
  }
  
  public void aggiornaElencoAllegatiAttestazioni(String[] elencoIdAttestazioni, 
      RuoloUtenza ruoloUtenza, Hashtable<Long,ParametriAttDichiarataVO> elencoParametri, 
      TipoAllegatoVO tipoAllegatoVO, ConsistenzaVO consistenzaVO) throws SolmrException
  {
    iac.aggiornaElencoAllegatiAttestazioni(elencoIdAttestazioni, ruoloUtenza, elencoParametri, tipoAllegatoVO, consistenzaVO);
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
   * @throws SolmrException
   */
  public TipoAttestazioneVO[] getListTipoAttestazioneForUpdate(Long idAzienda,
      String[] orderBy, String voceMenu) throws SolmrException
  {
    return iac.getListTipoAttestazioneForUpdate(idAzienda,
        orderBy, voceMenu);
  }

  /**
   * Metodo utilizzato per estrarre i tipi parametri previsti per una
   * determinata attestazione
   * 
   * @param idAttestazione
   * @return it.csi.solmr.dto.anag.attestazioni.TipoParametriAttestazioneVO
   * @throws SolmrException
   */
  public TipoParametriAttestazioneVO findTipoParametriAttestazioneByIdAttestazione(
      Long idAttestazione) throws SolmrException
  {
    return iac.findTipoParametriAttestazioneByIdAttestazione(idAttestazione);
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
      String codiceAttestazione, String voceMenu) throws SolmrException
  {
    return iac.getElencoTipoAttestazioneAlPianoAttuale(flagVideo, flagStampa,
        orderBy, codiceAttestazione, voceMenu);
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
   * @throws SolmrException
   */
  public TipoAttestazioneVO[] getTipoAttestazioneAllegatiAllaDichiarazione(
      String codiceFotografiaTerreni, java.util.Date dataAnnoCampagna, 
      java.util.Date dataVariazione, String[] orderBy) throws SolmrException
  {
    return iac.getTipoAttestazioneAllegatiAllaDichiarazione(codiceFotografiaTerreni, 
        dataAnnoCampagna, dataVariazione, orderBy);
  }
  
  public Vector<TipoAttestazioneVO> getElencoAttestazioniAllaDichiarazione(
      String codiceFotografiaTerreni, Date dataAnnoCampagna, 
      String codiceAttestazione)  throws SolmrException
  {
    return iac.getElencoAttestazioniAllaDichiarazione(codiceFotografiaTerreni, dataAnnoCampagna, codiceAttestazione);
  }
  
  
  public AttestazioneAziendaVO getFirstAttestazioneAzienda(long idAzienda) throws SolmrException
  {
    return iac.getFirstAttestazioneAzienda(idAzienda);
  }
  
  public AttestazioneDichiarataVO getFirstAttestazioneDichiarata(long codiceFotografia) throws SolmrException
  {
    return iac.getFirstAttestazioneDichiarata(codiceFotografia);
  }
  
  public Vector<java.util.Date> getDateVariazioniAllegati(long codiceFotografia) throws SolmrException
  {
    return iac.getDateVariazioniAllegati(codiceFotografia);
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
  @SuppressWarnings("rawtypes")
  public Htmpl getRisultatoQuery(TipologiaReportVO tipologiaReportVO,
      HashMap parametriFissiHtmpl, Htmpl layout) throws Exception,
      Exception
  {
    return iac
        .getRisultatoQuery(tipologiaReportVO, parametriFissiHtmpl, layout);
  }

  @SuppressWarnings("rawtypes")
  public HSSFWorkbook getRisultatoQuery(TipologiaReportVO tipologiaReportVO,
      HashMap parametriFissiHtmpl, HSSFWorkbook workBook, String nomeFoglio)
      throws Exception, Exception
  {
    return iac.getRisultatoQuery(tipologiaReportVO, parametriFissiHtmpl,
        workBook, nomeFoglio);
  }

  /**
   * Elenco variabili e valori associate alla query selezionata
   * 
   * @param idTipologia
   * @return
   * @throws Exception
   */
  public HashMap<?,?> getQueryPopolamento(String queryPopolamento,
      String idRptVariabileReport) throws Exception, Exception
  {
    return iac.getQueryPopolamento(queryPopolamento, idRptVariabileReport);
  }

  // Gestione Utiliti Inizio //

  public Vector<AnagAziendaVO> getAziendaByIntermediarioAndCuaa(
      Long intermediario, String cuaa) throws SolmrException
  {
    return iac.getAziendaByIntermediarioAndCuaa(intermediario, cuaa);
  }

  public Vector<AnagAziendaVO> getAziendeByListOfId(Vector<Long> vIdAzienda)
      throws SolmrException
  {
    return iac.getAziendeByListOfId(vIdAzienda);
  }

  public void storicizzaDelegaBlocco(RuoloUtenza ruoloUtenza,
      Vector<AnagAziendaVO> vAnagAziendaVO, String oldIntermediario,
      String newIntermediario) throws SolmrException
  {
    iac.storicizzaDelegaBlocco(ruoloUtenza, vAnagAziendaVO, oldIntermediario,
        newIntermediario);
  }

  // Gestione Utiliti Fine //

  public Vector<AziendaCollegataVO> getAziendeCollegateByIdAzienda(
      Long idAzienda, boolean flagStorico) throws SolmrException
  {
    return iac.getAziendeCollegateByIdAzienda(idAzienda, flagStorico);
  }

  public Vector<AnagAziendaVO> getEntiAppartenenzaByIdAzienda(Long idAzienda,
      boolean flagStorico) throws SolmrException
  {
    return iac.getEntiAppartenenzaByIdAzienda(idAzienda, flagStorico);
  }

  public AnagAziendaVO findAziendaByIdAnagAzienda(Long anagAziendaPK)
      throws SolmrException
  {
    return iac.findAziendaByIdAnagAzienda(anagAziendaPK);
  }

  public Vector<AziendaCollegataVO> getAziendeCollegateByRangeIdAziendaCollegata(
      Vector<Long> vIdAziendaCollegata) throws SolmrException
  {
    return iac
        .getAziendeCollegateByRangeIdAziendaCollegata(vIdAziendaCollegata);
  }

  public Vector<AnagAziendaVO> getIdAziendaCollegataAncestor(Long idAzienda)
      throws SolmrException
  {
    return iac.getIdAziendaCollegataAncestor(idAzienda);
  }

  public Vector<AnagAziendaVO> getIdAziendaCollegataDescendant(Long idAzienda)
      throws SolmrException
  {
    return iac.getIdAziendaCollegataDescendant(idAzienda);
  }

  public boolean controlloAziendeAssociate(String CUAApadre,
      Long idAziendaCollegata) throws SolmrException
  {
    return iac.controlloAziendeAssociate(CUAApadre, idAziendaCollegata);
  }

  public Vector<AnagAziendaVO> getAziendeCollegateByRangeIdAzienda(
      Vector<Long> vIdAzienda, Long idAziendaPadre) throws SolmrException
  {
    return iac.getAziendeCollegateByRangeIdAzienda(vIdAzienda, idAziendaPadre);
  }

  /**
   * Servizi che accedono ai WS CCIAA START
   */
  public UvResponseCCIAA elencoUnitaVitatePerCUAA(String cuaa, Long idAzienda, RuoloUtenza ruolo)
      throws SolmrException
  {
    return iac.elencoUnitaVitatePerCUAA(cuaa, idAzienda, ruolo);
  }

  public UvResponseCCIAA elencoUnitaVitatePerParticella(String istat,
      String sezione, String foglio, String particella, Long idAzienda, RuoloUtenza ruolo) throws SolmrException
  {
    return iac.elencoUnitaVitatePerParticella(istat, sezione, foglio,
        particella, idAzienda, ruolo);
  }

  /**
   * Metodo che si occupa di invocare il web-service CCIIA per l'aggiornamento
   * dei dati relativi all'albo vigneti
   * 
   * @param AnagAziendaVO
   * @throws SolmrException
   */
  public void sianAggiornaDatiAlboVigneti(AnagAziendaVO anagAziendaVO)
      throws SolmrException
  {
    iac.sianAggiornaDatiAlboVigneti(anagAziendaVO);
  }

  /**
   * Servizi che accedono ai WS CCIAA STOP
   */
  
  public StringcodeDescription getDescTipoIscrizioneInpsByCodice(String codiceTipoIscrizioneInps)
      throws SolmrException
  {
    return iac.getDescTipoIscrizioneInpsByCodice(codiceTipoIscrizioneInps);
  }

  public Long getAnnoDichiarazione(Long idDichiarazioneConsistenza)
      throws SolmrException
  {
    return iac.getAnnoDichiarazione(idDichiarazioneConsistenza);
  }

  public Long getProcedimento(Long idAzienda, Long idDichiarazioneConsistenza)
      throws SolmrException
  {
    return iac.getProcedimento(idAzienda, idDichiarazioneConsistenza);
  }

  public long getLastIdDichiazioneConsistenza(Long idAzienda, Long anno)
      throws SolmrException
  {
    return iac.getLastIdDichiazioneConsistenza(idAzienda, anno);
  }

  public ParticellaDettaglioValidazioniVO[] getParticellaDettaglioValidazioni(
      long idParticella, Long anno, int tipoOrdinamento,
      boolean ordineAscendente[]) throws SolmrException
  {
    return iac.getParticellaDettaglioValidazioni(idParticella, anno,
        tipoOrdinamento, ordineAscendente);
  }

  public long[] getElencoAnniDichiarazioniConsistenzaByIdParticella(
      long idParticella) throws SolmrException
  {
    return iac
        .getElencoAnniDichiarazioniConsistenzaByIdParticella(idParticella);
  }

  public boolean areParticelleNonCessateByIdParticelle(long idParticelle[])
      throws SolmrException
  {
    return iac.areParticelleNonCessateByIdParticelle(idParticelle);
  }

  public StoricoParticellaVO findStoricoParticellaVOByIdParticella(
      long idParticella) throws SolmrException
  {
    return iac.findStoricoParticellaVOByIdParticella(idParticella);
  }

  public SianAnagTributariaVO selectDatiAziendaTributaria(String cuaa)
      throws SolmrException
  {
    return iac.selectDatiAziendaTributaria(cuaa);
  }

  // Richiamo una funzione PL-SQL che mi restituisce l'elenco dei CUAA collegati
  public String[] getCUAACollegati(String cuaa, SianUtenteVO sianUtenteVO) throws SolmrException
  {
    return iac.getCUAACollegati(cuaa, sianUtenteVO);
  }

  public Vector<Long> getIdAnagAziendeCollegatebyCUAA(String cuaa)
      throws SolmrException
  {
    return iac.getIdAnagAziendeCollegatebyCUAA(cuaa);
  }

  public ParticellaAssVO[] getParticellaForDocAzCessata(Long idParticella)
      throws SolmrException
  {
    return iac.getParticellaForDocAzCessata(idParticella);
  }

  public Vector<ParticellaAssVO> getParticelleDocCor(Long idDocumento) throws SolmrException
  {
    return iac.getParticelleDocCor(idDocumento);
  }

  // ************** SITI
  public String serviceParticellaUrl3D(String istatComune, String sezione,
      String foglio, String particella, String subalterno)
      throws InvalidParameterException, UnrecoverableException,
      Exception, SolmrException
  {
    return iac.serviceParticellaUrl3D(istatComune, sezione, foglio, particella,
        subalterno);
  }

  // *************** SITI

  /*
   * Servizi di vitiserv Begin
   */
  public DirittoGaaVO[] getDiritti(long idAzienda, boolean flagAttivi,
      int tipoOrdinamento, int tipoRisultato) throws SolmrException
  {
    return iac
        .getDiritti(idAzienda, flagAttivi, tipoOrdinamento, tipoRisultato);
  }

  /*
   * Servizi di vitiserv End
   */

  /*****************************************************************************
   * ********* Servizi di SigopServ BEGIN **********************************
   ****************************************************************************/
  
  public SchedaCreditoVO[] sigopservVisualizzaDebiti(String cuaa)
      throws SolmrException
  {
    return iac.sigopservVisualizzaDebiti(cuaa);
  }
  
  public PagamentiErogatiVO sigopservEstraiPagamentiErogati(String cuaa, String settore,
    Integer anno) throws SolmrException
  {
    return iac.sigopservEstraiPagamentiErogati(cuaa, settore, anno);
  }
   
  public RecuperiPregressiVO sigopservEstraiRecuperiPregressi(String cuaa, String settore,
      Integer anno) throws SolmrException
  {
     return iac.sigopservEstraiRecuperiPregressi(cuaa, settore, anno);
  }

  /*****************************************************************************
   * ********* Servizi di SigopServ END **********************************
   ****************************************************************************/
  
  
  
  
  /***************************** COMMON BEGIN ***************************
   ******************************************************************* 
   ******************************************************************/
  
  public Object getValoreParametroAltriDati(String codiceParametro)
    throws SolmrException
  {
    return iac.getValoreParametroAltriDati(codiceParametro);
  }
  
  /***************************** COMMON END ***************************
   ******************************************************************* 
   ******************************************************************/
  
  
  
  /***************************** WS COMUNE START ***************************
   ******************************************************************* 
   ******************************************************************/
  
  public String serviceInviaPostaCertificata(InvioPostaCertificata invioPosta) 
      throws SolmrException
  {
    return iac.serviceInviaPostaCertificata(invioPosta);
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
    return iac.findRuoliForPersonaInApplicazione(codiceFiscale, livelloAutenticazione);
  }
  
  public UtenteAbilitazioni loginPapua(String codiceFiscale, String cognome, 
      String nome, int livelloAutenticazione, String codiceRuolo)
      throws SolmrException, Exception
  {
    return iac.loginPapua(codiceFiscale, cognome, nome, livelloAutenticazione, codiceRuolo);
  }
  
  public MacroCU[] findMacroCUForAttoreInApplication(String codiceAttore)
      throws SolmrException, Exception
  {
    return iac.findMacroCUForAttoreInApplication(codiceAttore);
  }
  
  public boolean verificaGerarchia(long idUtente1, long idUtente2)
      throws SolmrException, Exception
  {
    return iac.verificaGerarchia(idUtente1, idUtente2);
  }
  
  public UtenteAbilitazioni getUtenteAbilitazioniByIdUtenteLogin(long idUtente)
      throws SolmrException, Exception
  {
    return iac.getUtenteAbilitazioniByIdUtenteLogin(idUtente);
  }
  
  public UtenteAbilitazioni[] getUtenteAbilitazioniByIdUtenteLoginRange(long[] idUtente)
      throws SolmrException, Exception
  {
    return iac.getUtenteAbilitazioniByIdUtenteLoginRange(idUtente);
  }
  
  /***************************** WS PAPUA END ***************************
   ******************************************************************* 
   ******************************************************************/
  
  

  // Usato dall'utente di monitoraggio
  public String testDB() throws SolmrException
  {
    return iac.testDB();
  }
}
