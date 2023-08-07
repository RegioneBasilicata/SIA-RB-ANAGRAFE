package it.csi.smranag.smrgaa.business;

import it.csi.papua.papuaserv.dto.messaggistica.DettagliMessaggio;
import it.csi.papua.papuaserv.dto.messaggistica.ListaMessaggi;
import it.csi.papua.papuaserv.exception.messaggistica.LogoutException;
import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni;
import it.csi.sigmater.sigtersrv.dto.daticatastali.DettaglioFabbricato;
import it.csi.sigmater.sigtersrv.dto.daticatastali.DettaglioTerreno;
import it.csi.sigmater.sigtersrv.dto.daticatastali.Titolarita;
import it.csi.smranag.smrgaa.dto.AllegatoDichiarazioneVO;
import it.csi.smranag.smrgaa.dto.AllegatoDocumentoVO;
import it.csi.smranag.smrgaa.dto.BaseCodeDescription;
import it.csi.smranag.smrgaa.dto.PlSqlCodeDescription;
import it.csi.smranag.smrgaa.dto.RitornoAgriservUvVO;
import it.csi.smranag.smrgaa.dto.RitornoAgriservVO;
import it.csi.smranag.smrgaa.dto.RitornoPraticheCCAgriservVO;
import it.csi.smranag.smrgaa.dto.TipoFirmaVO;
import it.csi.smranag.smrgaa.dto.abio.OperatoreBiologicoVO;
import it.csi.smranag.smrgaa.dto.abio.PosizioneOperatoreVO;
import it.csi.smranag.smrgaa.dto.allevamenti.AllevamentoAcquaLavaggio;
import it.csi.smranag.smrgaa.dto.allevamenti.AllevamentoBioVO;
import it.csi.smranag.smrgaa.dto.allevamenti.AllevamentoVO;
import it.csi.smranag.smrgaa.dto.allevamenti.ControlloAllevamenti;
import it.csi.smranag.smrgaa.dto.allevamenti.EsitoControlloAllevamento;
import it.csi.smranag.smrgaa.dto.allevamenti.SottoCategoriaAllevamento;
import it.csi.smranag.smrgaa.dto.allevamenti.SottoCategoriaAnimStab;
import it.csi.smranag.smrgaa.dto.allevamenti.StabulazioneTrattamento;
import it.csi.smranag.smrgaa.dto.allevamenti.TipoDestinoAcquaLavaggio;
import it.csi.smranag.smrgaa.dto.allevamenti.TipoMungitura;
import it.csi.smranag.smrgaa.dto.allevamenti.TipoSottoCategoriaAnimale;
import it.csi.smranag.smrgaa.dto.allevamenti.TipoTrattamento;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaAtecoSecVO;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaColVarExcelVO;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaSezioniVO;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaUmaExcelVO;
import it.csi.smranag.smrgaa.dto.anagrafe.GruppoGreeningVO;
import it.csi.smranag.smrgaa.dto.anagrafe.PlSqlCalcoloOteVO;
import it.csi.smranag.smrgaa.dto.anagrafe.TipoAttivitaOteVO;
import it.csi.smranag.smrgaa.dto.anagrafe.TipoDimensioneAziendaVO;
import it.csi.smranag.smrgaa.dto.anagrafe.TipoInfoAggiuntivaVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.AcquaExtraVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.Comunicazione10RVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.EffluenteCesAcqVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.EffluenteStocExtVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.EffluenteVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.RefluoEffluenteVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.TipoAcquaAgronomicaVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.TipoCausaleEffluenteVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.TipoEffluenteVO;
import it.csi.smranag.smrgaa.dto.fabbricati.FabbricatoBioVO;
import it.csi.smranag.smrgaa.dto.fascicoli.InvioFascicoliVO;
import it.csi.smranag.smrgaa.dto.manuali.ManualeVO;
import it.csi.smranag.smrgaa.dto.modol.AttributiModuloVO;
import it.csi.smranag.smrgaa.dto.modol.RiepilogoStampaTerrVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AllevamentoAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AzAssAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.CCAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.FabbricatoAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.IterRichiestaAziendaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.MacchinaAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.MotivoRichiestaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.ParticellaAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.RichiestaAziendaDocumentoVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.RichiestaAziendaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.SoggettoAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.StatoRichiestaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.UnitaMisuraVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.UteAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.polizze.PolizzaDettaglioVO;
import it.csi.smranag.smrgaa.dto.polizze.PolizzaVO;
import it.csi.smranag.smrgaa.dto.search.FiltriRicercaAziendeCollegateVO;
import it.csi.smranag.smrgaa.dto.search.FiltriRicercaMacchineAgricoleVO;
import it.csi.smranag.smrgaa.dto.search.FiltriRicercaVariazioniAziendaliVO;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaAziendeCollegateVO;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaMacchineAgricoleVO;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaVariazioniAziendaliVO;
import it.csi.smranag.smrgaa.dto.stampe.DichiarazioneConsistenzaGaaVO;
import it.csi.smranag.smrgaa.dto.stampe.InfoFascicoloVO;
import it.csi.smranag.smrgaa.dto.stampe.LogoVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.TipoAllegatoVO;
import it.csi.smranag.smrgaa.dto.stampe.TipoFirmatarioVO;
import it.csi.smranag.smrgaa.dto.stampe.TipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.ValoriCondizionalitaVO;
import it.csi.smranag.smrgaa.dto.terreni.CasoParticolareVO;
import it.csi.smranag.smrgaa.dto.terreni.CatalogoMatriceSeminaVO;
import it.csi.smranag.smrgaa.dto.terreni.CatalogoMatriceVO;
import it.csi.smranag.smrgaa.dto.terreni.CompensazioneAziendaVO;
import it.csi.smranag.smrgaa.dto.terreni.ConduzioneEleggibilitaVO;
import it.csi.smranag.smrgaa.dto.terreni.DirittoCompensazioneVO;
import it.csi.smranag.smrgaa.dto.terreni.FaseRiesameDocumentoVO;
import it.csi.smranag.smrgaa.dto.terreni.IsolaParcellaVO;
import it.csi.smranag.smrgaa.dto.terreni.IstanzaRiesameVO;
import it.csi.smranag.smrgaa.dto.terreni.ParticellaBioVO;
import it.csi.smranag.smrgaa.dto.terreni.ParticellaIstanzaRiesameVO;
import it.csi.smranag.smrgaa.dto.terreni.RegistroPascoloVO;
import it.csi.smranag.smrgaa.dto.terreni.RiepiloghiUnitaArboreaVO;
import it.csi.smranag.smrgaa.dto.terreni.RiepilogoCompensazioneVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoAreaVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoDestinazioneVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoDettaglioUsoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoEfaVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoEsportazioneDatiVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoFaseAllevamentoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoFonteVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoMetodoIrriguoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoPeriodoSeminaVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoPraticaMantenimentoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoQualitaUsoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoRiepilogoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoSeminaVO;
import it.csi.smranag.smrgaa.dto.terreni.UVCompensazioneVO;
import it.csi.smranag.smrgaa.dto.uma.PossessoMacchinaVO;
import it.csi.smranag.smrgaa.dto.uma.TipoCategoriaGaaVO;
import it.csi.smranag.smrgaa.dto.uma.TipoFormaPossessoGaaVO;
import it.csi.smranag.smrgaa.dto.uma.TipoGenereMacchinaGaaVO;
import it.csi.smranag.smrgaa.dto.uma.TipoMacchinaGaaVO;
import it.csi.smranag.smrgaa.dto.ws.ResponseWsBridgeVO;
import it.csi.smranag.smrgaa.ws.sianfa.SianEsito;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoDocumentoVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoFolderVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoIdDocVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoVO;
import it.csi.smruma.umaserv.dto.AssegnazioneVO;
import it.csi.smruma.umaserv.dto.DittaUmaVO;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.ProvinciaVO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.AnagParticellaExcelVO;
import it.csi.solmr.dto.anag.ConsistenzaVO;
import it.csi.solmr.dto.anag.ContoCorrenteVO;
import it.csi.solmr.dto.anag.DelegaVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.FabbricatoVO;
import it.csi.solmr.dto.anag.ParticellaVO;
import it.csi.solmr.dto.anag.TipoDocumentoVO;
import it.csi.solmr.dto.anag.TipoFormaConduzioneVO;
import it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO;
import it.csi.solmr.dto.anag.fabbricati.TipoFormaFabbricatoVO;
import it.csi.solmr.dto.anag.fabbricati.TipoTipologiaFabbricatoVO;
import it.csi.solmr.dto.anag.sian.SianAllevamentiVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneParticellaVO;
import it.csi.solmr.dto.anag.terreni.FiltriParticellareRicercaVO;
import it.csi.solmr.dto.anag.terreni.FiltriUnitaArboreaRicercaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaArboreaExcelVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.dto.anag.terreni.TipoUtilizzoVO;
import it.csi.solmr.dto.anag.terreni.TipoVarietaVO;
import it.csi.solmr.dto.anag.terreni.UtilizzoParticellaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.dto.uma.MacchinaVO;
import it.csi.solmr.dto.uma.UtilizzoVO;
import it.csi.solmr.exception.SolmrException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.Vector;

import javax.ejb.Local;

/**
 * <p>
 * Title: S.O.L.M.R.
 * </p>
 * <p>
 * Description: Servizi On-Line per il Mondo Rurale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: TOBECONFIG
 * </p>
 * 
 * @author TOBECONFIG
 * @version 1.0
 */

@Local
public interface GaaFacadeLocal
{

  /** ******************************************** */
  /** ***** ALLEVAMENTI START ****** */
  /** ******************************************** */
  public TipoSottoCategoriaAnimale[] getTipiSottoCategoriaAnimale(long idCategoriaAnimale) throws SolmrException, Exception;

  public TipoSottoCategoriaAnimale getTipoSottoCategoriaAnimale(long idSottoCategoriaAnimale) throws SolmrException, Exception;

  public TipoMungitura[] getTipiMungitura() throws SolmrException, Exception;
  
  public Vector<SottoCategoriaAllevamento> getTipiSottoCategoriaAllevamento(long idAllevamento) throws SolmrException, Exception;
  
  public Vector<StabulazioneTrattamento> getStabulazioni(long idAllevamento,boolean modifica) throws SolmrException, Exception;
  
  public BaseCodeDescription[] getTipiStabulazione(long idSottoCategoriaAnimale) throws SolmrException, Exception;
  
  public Vector<TipoTrattamento> getTipiTrattamento() throws SolmrException, Exception;
  
  public TipoTrattamento getTipoTrattamento(long idTrattamento) throws SolmrException, Exception;
  
  public SottoCategoriaAnimStab getSottoCategoriaAnimStab(long idSottoCategoriaAnimale,boolean palabile, long idStabulazione) throws SolmrException, Exception;
  
  public Vector<AllevamentoVO> getElencoAllevamentiExcel(long idAzienda, Date dataInserimentiDich, Long idUte) throws SolmrException, Exception;
  
  public Vector<AllevamentoBioVO> getAllevamentiBio(Date dataInserimentoDichiarazione, long idAllevamento) 
    throws SolmrException, Exception;
  
  public Vector<TipoDestinoAcquaLavaggio> getElencoDestAcquaLavaggio()  
      throws SolmrException, Exception;
  
  public Vector<AllevamentoAcquaLavaggio> getElencoAllevamentoAcquaLavaggio(long idAllevamento)
      throws SolmrException, Exception;
  
  public FabbricatoBioVO getFabbricatoBio(long idFabbricato, long idAzienda, 
      Date dataInserimentoDichiarazione) throws SolmrException, Exception;
  
  public HashMap<Long,String> esisteFabbricatoAttivoByConduzioneAndAzienda(
      Vector<Long> elencoConduzioni, Long idAzienda)
  throws SolmrException, Exception;
  
  public Vector<Long> getElencoSpecieAzienda(long idAzienda) 
      throws SolmrException, Exception;
  
  //public HashMap<Long,BigDecimal> getAllevamentoTotAcquaLavaggio(long idAzienda)  
      //throws SolmrException, Exception;
  
  public Vector<AllevamentoVO> getElencoAllevamentiSian(String cuaa, 
      long idTipoSpecie, String codAziendaZoo) 
      throws SolmrException, Exception;
  
  public Vector<EsitoControlloAllevamento> getElencoEsitoControlloAllevamento(long idAllevamento)
      throws SolmrException, Exception;
  
  public HashMap<Long, ControlloAllevamenti> getEsitoControlliAllevamentiAzienda(long idAzienda)
      throws SolmrException, Exception;
  
  public HashMap<Long, ControlloAllevamenti> getSegnalazioniControlliAllevamentiAzienda(long idAzienda)
      throws SolmrException, Exception;
  
  /** ******************************************** */
  /** ***** ALLEVAMENTI END ****** */
  /** ******************************************** */
  
  /** ******************************************** */
  /** ***** FABBRICATI BEGIN ****** */
  /** ******************************************** */
  
  public TipoTipologiaFabbricatoVO getInfoTipologiaFabbricato(Long idTipologiaFabbricato) 
    throws SolmrException, Exception;
  
  public TipoFormaFabbricatoVO getTipoFormaFabbricato(Long idTipologiaFabbricato)
    throws SolmrException, Exception;
  
  public Vector<TipoTipologiaFabbricatoVO> getListTipoFabbricatoStoccaggio() throws SolmrException, Exception;

  
  /** ******************************************** */
  /** ***** FABBRICATI END ****** */
  /** ******************************************** */
  
  /** ******************************************** */
  /** ***** AGRISERV BEGIN ****** */
  /** ******************************************** */
  
  public RitornoAgriservVO searchPraticheProcedimento(long idParticella, 
      Long idAzienda, int tipologiaStati, Long idDichiarazioneConsistenza, 
      Long idProcedimento, Long annoCampagna, int tipoOrdinamento) throws SolmrException,
      Exception;

  public BaseCodeDescription[] baseDecodeUtilizzoByIdRange(long ids[])
      throws SolmrException, Exception;
  
  public RitornoPraticheCCAgriservVO searchPraticheContoCorrente(long[] idContoCorrente, 
      int tipologiaStati, Long idDichiarazioneConsistenza, Long idProcedimento, 
      Long annoCampagna, int tipoOrdinamento) throws SolmrException, Exception;
  
  public RitornoAgriservUvVO existPraticheEstirpoUV(long[] idParticella, Long idAzienda, 
      Long idDichiarazioneConsistenza, Long idProcedimento, 
      Long annoCampagna, int tipoOrdinamento) throws SolmrException, Exception;
  
  /** ******************************************** */
  /** ***** AGRISERV END ****** */
  /** ******************************************** */
  
  /** ******************************************** */
  /** ***** COMUNICAZIONE10R START ****** */
  /** ******************************************** */
  
  
  public Comunicazione10RVO getComunicazione10RByIdUteAndPianoRifererimento(long idUte, long idPianoRiferimento)
    throws SolmrException, Exception;
  
  public Vector<EffluenteVO> getListEffluenti(long idComunicazione10R) throws SolmrException, Exception;
  
  public Vector<EffluenteVO> getListEffluenti(long idComunicazione10R[]) throws SolmrException, Exception;
  
  public Vector<EffluenteVO> getListEffluentiStampa(long idComunicazione10R[]) throws SolmrException, Exception;
  
  public Vector<TipoCausaleEffluenteVO> getListTipoCausaleEffluente() throws SolmrException, Exception;
  
  public Vector<TipoEffluenteVO> getListTipoEffluente() throws SolmrException, Exception;
  
  public TipoEffluenteVO getTipoEffluenteById(long idEffluente) 
      throws SolmrException, Exception;
  
  public Vector<TipoAcquaAgronomicaVO> getListTipoAcquaAgronomica() throws SolmrException, Exception;
  
  public Vector<EffluenteCesAcqVO> getListEffluentiCessAcquByidComunicazione(long idComunicazione10R, Long idTipoCausale) 
    throws SolmrException, Exception;
  
  public Vector<EffluenteCesAcqVO> getListEffluentiCessAcquPerStampa(long idComunicazione10R[]) 
    throws SolmrException, Exception;
  
  public Vector<EffluenteCesAcqVO> getListEffluentiCessAcquByidComunicazione(long idComunicazione10R[], Long idTipoCausale) 
  throws SolmrException, Exception;

  public Vector<EffluenteStocExtVO> getListStoccaggiExtrAziendali(long idComunicazione10R) 
    throws SolmrException, Exception;
  
  public Vector<EffluenteStocExtVO> getListStoccaggiExtrAziendali(long idComunicazione10R[]) 
    throws SolmrException, Exception;
  
  public Vector<AcquaExtraVO> getListAcquaExtra(long idComunicazione10R) 
    throws SolmrException, Exception;
  
  public Vector<AcquaExtraVO> getListAcquaExtra(long idComunicazione10R[]) 
    throws SolmrException, Exception;
  
  public PlSqlCodeDescription storicizzaComunicazione10R(long idUtenteAggiornamento, long idAzienda,
	      Vector<Comunicazione10RVO> vCom10r, Vector<EffluenteVO> vEffluentiTratt, 
	      Vector<EffluenteCesAcqVO> vCessioniAcquisizioni, Vector<EffluenteCesAcqVO> vCessioni,
	      Vector<EffluenteStocExtVO> vStoccaggi, Vector<AcquaExtraVO> vAcqueExtra) throws SolmrException, Exception;
  
  public boolean hasEffluenteProdotto(long idEffluente, long idUte) 
    throws SolmrException, Exception;
  
  public Comunicazione10RVO[] getComunicazione10RByPianoRifererimento(long idAzienda, long idPianoRiferimento) 
    throws SolmrException, Exception;
  
  public PlSqlCodeDescription controlloQuantitaEffluentePlSql(long idUte, long idEffluente) 
    throws SolmrException, Exception;

  public PlSqlCodeDescription calcolaQuantitaAzotoPlSql(long idUte, long idComunicazione10r, 
      long idCausaleEffluente, long idEffluente, BigDecimal quantita) 
    throws SolmrException, Exception;
  
  public PlSqlCodeDescription ricalcolaPlSql(long idAzienda, 
      long idUtente)  throws SolmrException, Exception;
  
  public Vector<BigDecimal> getSommaEffluentiCessAcquPerStampa(long idComunicazione10R) 
      throws SolmrException, Exception;
  
  public BigDecimal getSommaEffluenti10RPerStampa(long idComunicazione10R, boolean palabile) 
      throws SolmrException, Exception;
  
  public PlSqlCodeDescription calcolaM3EffluenteAcquisitoPlSql(long idAzienda, 
      long idAziendaCess, long idCausaleEffluente, long idEffluente) 
    throws SolmrException, Exception;
  
  public boolean controlloRefluoPascolo(long idUte) 
      throws SolmrException, Exception;
  
  public Vector<TipoEffluenteVO> getListTipoEffluenteTrattamenti(long idUte)
      throws SolmrException, Exception;
  
  public Vector<TipoEffluenteVO> getListTipoEffluenteByLegameId(long idEffluente) 
      throws SolmrException, Exception;
  
  public Vector<TipoEffluenteVO> getListTipoEffluenteAndValueByLegameId(
      long idComunicazione10R, long idEffluente) 
    throws SolmrException, Exception;
  
  public Vector<EffluenteVO> getListTrattamenti(long idComunicazione10R[]) 
      throws SolmrException, Exception;
  
  public Vector<EffluenteVO> getListTrattamenti(long idComunicazione10R) 
      throws SolmrException, Exception;
  
  public Vector<EffluenteVO> getListEffluentiNoTratt(long idComunicazione10R[]) 
      throws SolmrException, Exception;
  
  public Vector<EffluenteVO> getListEffluentiNoTratt(long idComunicazione10R) 
      throws SolmrException, Exception;
  
  public Vector<RefluoEffluenteVO> getRefluiComunocazione10r(long idUte, long idComunicazione10r, 
      Date dataInserimentoDichiarazione) 
    throws SolmrException, Exception;
  
  public Vector<RefluoEffluenteVO> getListRefluiStampa(long idComunicazione10R[]) 
    throws SolmrException, Exception;
  
  public PlSqlCodeDescription calcolaVolumePioggeM3PlSql(long idUte)
      throws SolmrException, Exception;
  
  public PlSqlCodeDescription calcolaAcqueMungituraPlSql(long idUte)
      throws SolmrException, Exception;
  
  public PlSqlCodeDescription calcolaCesAcquisizionePlSql(long idComunicazione10r) 
      throws SolmrException, Exception;

  /** ******************************************** */
  /** ***** COMUNICAZIONE10R END ****** */
  /** ******************************************** */
  
  /** ******************************************** */
  /** ***** ANAGRAFE START ****** */
  /** ******************************************** */
  
  public PlSqlCalcoloOteVO calcolaIneaPlSql(long idAzienda)
    throws SolmrException, Exception;
  
  public PlSqlCalcoloOteVO calcolaUluPlSql(long idAzienda, Long idUde)
   throws SolmrException, Exception;
  
  public Vector<AziendaAtecoSecVO> getListActiveAziendaAtecoSecByIdAzienda(long idAzienda)
    throws SolmrException, Exception;
  
  public Vector<AziendaAtecoSecVO> getListActiveAziendaAtecoSecByIdAziendaAndValid(long idAzienda,
      long idDichiarazioneConsistenza) throws SolmrException, Exception;
  
  public CodeDescription getAttivitaAtecoAllaValid(long idAzienda,
      long idDichiarazioneConsistenza) throws SolmrException, Exception;
  
  public Vector<TipoDimensioneAziendaVO> getListActiveTipoDimensioneAzienda() 
    throws SolmrException, Exception;
  
  public TipoAttivitaOteVO getTipoAttivitaOteByPrimaryKey(long idAttivitaOte)
    throws SolmrException, Exception;
  
  public long[] ricercaIdAziendeCollegate(
      FiltriRicercaAziendeCollegateVO filtriRicercaAziendeCollegateVO) 
  throws SolmrException, Exception;
  
  public RigaRicercaAziendeCollegateVO[] getRigheRicercaAziendeCollegateByIdAziendaCollegata(
      long ids[]) throws SolmrException, Exception;
  
  public int ricercaNumVariazioni(FiltriRicercaVariazioniAziendaliVO filtriRicercaVariazioniAziendaliVO, 
      UtenteAbilitazioni utenteAbilitazioni, RuoloUtenza ruoloUtenza) 
    throws SolmrException, Exception;
  
  public void insertVisioneVariazione(Vector<Long> elencoIdPresaVisione, RuoloUtenza ruoloUtenza) 
    throws SolmrException, Exception;
  
  public RigaRicercaVariazioniAziendaliVO[] getRigheRicercaVariazioni(FiltriRicercaVariazioniAziendaliVO filtriRicercaVariazioniAziendaliVO,
      UtenteAbilitazioni utenteAbilitazioni, RuoloUtenza ruoloUtenza,boolean excel) 
    throws SolmrException, Exception;
  
  public RigaRicercaVariazioniAziendaliVO[] getRigheVariazioniVisione(Vector<Long> elencoIdPresaVisione,FiltriRicercaVariazioniAziendaliVO filtriRicercaVariazioniAziendaliVO) 
    throws SolmrException, Exception;
  
  public boolean isPresaVisione(Vector<Long> elencoIdPresaVisione, RuoloUtenza ruoloUtenza)
    throws SolmrException, Exception;
  
  public BigDecimal[] getTOTSupCondottaAndSAU(long idAzienda) throws SolmrException, Exception;
  
  public HashMap<Long,DelegaVO> getDelegaAndIntermediario(long ids[]) throws SolmrException, Exception;
  
  public boolean isAziendeCollegataFiglia(long idAzienda, long idAziendaSearch) 
    throws SolmrException, Exception;
  
  public boolean isAziendeCollegataMenu(
      long idAzienda) throws SolmrException, Exception;
  
  public Vector<AziendaUmaExcelVO> getScaricoExcelElencoSociUma(
      long idAzienda) throws SolmrException, Exception;
  
  public Vector<AziendaColVarExcelVO> getScaricoExcelElencoSociColturaVarieta(
      long idAzienda) throws SolmrException, Exception;
  
  public Vector<AziendaColVarExcelVO> getScaricoExcelElencoSociFruttaGuscio(
      long idAzienda) throws SolmrException, Exception;
  
  public boolean isInAziendaProvenienza(long idAzienda) 
      throws SolmrException,  Exception;
  
  public Vector<TipoInfoAggiuntivaVO> getTipoInfoAggiuntive(long idAzienda)
      throws SolmrException,  Exception;
  
  public Vector<GruppoGreeningVO> getListGruppiGreening(Long idDichConsistenza, Long idAzienda) 
  		throws SolmrException, Exception;
  
  public PlSqlCodeDescription calcolaGreeningPlSql(long idAzienda, long IdUtente, 
  		Long idDichiarazioneConsistenza) throws SolmrException, Exception;
  
  public PlSqlCodeDescription calcolaEfaPlSql(long idAzienda, Long idDichiarazioneConsistenza,
      long IdUtente) throws SolmrException, Exception;
  
  public Vector<AziendaSezioniVO> getListActiveAziendaSezioniByIdAzienda(long idAzienda)
      throws SolmrException, Exception;
  		
  /** ******************************************** */
  /** ***** ANAGRAFE END ****** */
  /** ******************************************** */
  
  
  /** ******************************************** */
  /** ***** SIGMATER BEGIN ****** */
  /** ******************************************** */
  public DettaglioTerreno cercaTerreno(String codIstatComune,
			 String codBelfioreComune,
			 String sezione,
			 String foglio,
			 String numero,
			 String subalterno,
			 String progressivo)
	throws SolmrException, Exception;
  
  public DettaglioFabbricato cercaFabbricato(String codIstatComune,
										   String codBelfioreComune,
										   String sezione,
										   String foglio,
										   String numero,
										   String subalterno,
										   String progressivo)
  	throws SolmrException, Exception;

  public Titolarita[] cercaTitolaritaOggettoCatastale(String codIstatComune,
													 String codBelfioreComune,
													 String sezione,
													 String idImmobile,
													 String tipoImmobile,
													 String dataDa,
													 String dataA)
  	throws SolmrException, Exception;
  
  
  public void scaricoTitolarita(String codIstat, String cuaa, long idAzienda,
      Date dataDa, Date dataA)
  throws SolmrException, Exception;
  
  
  public Long getIdTipoDiritto(String codice)
      throws SolmrException, Exception;
  
  public void importaTitolaritaSigmater(long idParticella, 
      Titolarita[] titolarita, long idUtente) throws SolmrException, Exception;
  
  /** ******************************************** */
  /** ***** SIGMATER END ****** */
  /** ******************************************** */ 
  
  /** ******************************************** */
  /** ***** ABIO BEGIN ****** */
  /** ******************************************** */
  
  public OperatoreBiologicoVO getOperatoreBiologicoByIdAzienda(Long idAzienda, Date dataInizioAttivita) 
  	throws SolmrException, Exception;
  
  public PosizioneOperatoreVO[] getAttivitaBiologicheByIdAzienda(
	Long idOperatoreBiologico, Date dataFineValidita, boolean checkStorico) 
  	throws SolmrException, Exception;

  public CodeDescription[] getODCbyIdOperatoreBiologico(
	Long idOperatoreBiologico, Date dataInizioValidita, boolean pianoCorrente) 
  	throws SolmrException, Exception;
  
  public OperatoreBiologicoVO getOperatoreBiologicoAttivo(Long idAzienda)
    throws SolmrException, Exception;

  /** ******************************************** */
  /** ***** ABIO END ****** */
  /** ******************************************** */
  
  
  /** ******************************************** */
  /** ***** DOCUMENTO BEGIN ****** */
  /** ******************************************** */
  
  public Vector<BaseCodeDescription> getCategoriaDocumentiTerritorialiAzienda(
      long idAzienda) throws SolmrException, Exception;
  
  public Vector<BaseCodeDescription> getDocumentiTerritorialiAzienda(
      long idAzienda) throws SolmrException, Exception;
  
  public Vector<BaseCodeDescription> getProtocolliDocumentiTerritorialiAzienda(
      long idAzienda) throws SolmrException, Exception;
  
  public boolean isDocumentoIstanzaRiesame(long idDocumento) throws SolmrException, Exception;
  
  public Vector<BaseCodeDescription> getCausaleModificaDocumentoValid() 
    throws SolmrException, Exception;
  
  public Vector<Long> esisteDocumentoAttivoByConduzioneAndAzienda(
      Vector<Long> elencoConduzioni, Long idAzienda)
  throws SolmrException, Exception;
  
  public Vector<AllegatoDocumentoVO> getElencoFileAllegati(
      long idDocumento)
  throws SolmrException,  Exception;
  
  public void deleteFileAllegato(long idAllegato, long idDocumento) 
      throws SolmrException, Exception;
  
  public void deleteFileAllegatoAgriWell(AllegatoDocumentoVO allegatoDocumentoVO) 
      throws SolmrException, Exception;
  
  public void deleteFileAllegatoRichiesta(long idAllegato, long idRichiestaDocumento) 
      throws SolmrException, Exception;
  
  public void deleteFileAllegatoNotifica(long idAllegato, long idNotifica) 
      throws SolmrException, Exception;
  
  public Long insertFileAllegato(AllegatoDocumentoVO allegatoDocumentoVO) 
      throws SolmrException, Exception;
  
  public Long insertFileAllegatoRichiesta(AllegatoDocumentoVO allegatoDocumentoVO) 
      throws SolmrException, Exception;
  
  public Long insertFileAllegatoNotifica(AllegatoDocumentoVO allegatoDocumentoVO) 
      throws SolmrException, Exception;
  
  public AllegatoDocumentoVO getFileAllegato(long idAllegato)
      throws SolmrException, Exception;
  
  public Date getFirstDataInserimentoDocumento(long idDocumento)
      throws SolmrException, Exception;
  
  public boolean isIstanzaRiesameFotoInterpretataByDocumento(long idDocumento) 
      throws SolmrException, Exception;
  
  public boolean exitsOtherDocISForParticellaAndAzienda(long idParticella, long idAzienda, Long idDocumento) 
      throws SolmrException, Exception;

  //public boolean isFaseIstanzaRiesameEvasa(long idAzienda, int fase, Vector<Long> vIdParticella, String parametro) 
      //throws SolmrException, Exception;
  
  
  public boolean exsitsDocFaseIstanzaRiesameFasePrec(long idAzienda, int fase, 
      long idParticella, String parametro) throws SolmrException, Exception;
  
  public boolean isPossCreateDocFaseIstanzaRiesameFaseSucc(long idAzienda, long idParticella,
      int idFase, String parametro) throws SolmrException, Exception;
  
  public Date getDateLastBatchIstanzaRiesameOk() throws SolmrException, Exception;
  
  public boolean isSitiConvocaValid(long idAzienda, int anno, int fase) throws SolmrException, Exception;
  
  public boolean isDataSospensioneScaduta(long idAzienda, long idParticella, int anno) throws SolmrException, 
    Exception;
  
  public boolean isParticellaEvasa(long idAzienda, long idParticella, int fase, int anno) 
      throws SolmrException, Exception;
  
  public boolean isVisibleTastoElimina(long idAzienda, int anno, int fase)  throws SolmrException, 
    Exception;
  
  public Vector<DocumentoVO> getListDocFromRicerca(String strDescDocumento, boolean attivi) 
      throws SolmrException, Exception;
  
  public Vector<ParticellaIstanzaRiesameVO> getLisParticellaFromIstanzaFoto(long idAzienda, int anno)  
      throws SolmrException, Exception;
  
  public String annullaIstanzaRiesame(Vector<Long> vIdIstanzaRiesame, long idAzienda, int anno, 
      RuoloUtenza ruoloUtenza) throws SolmrException, Exception;
  
  public boolean existAltraFaseFotoParticella(long idAzienda, long idParticella, int anno, long idFase)
      throws SolmrException, Exception;
  
  public Vector<TipoDocumentoVO> getDocumentiNuovaIscrizione() 
      throws SolmrException, Exception;
  
  public Vector<AllegatoDocumentoVO> getElencoFileAllegatiRichiesta(
      long idRichiestaDocumento) throws SolmrException, Exception;
  
  public Vector<AllegatoDocumentoVO> getElencoFileAllegatiNotifica(
      long idNotifica) throws SolmrException, Exception;
  
  public boolean isIstanzaAttiva(long idAzienda) throws SolmrException, Exception;
  
  public boolean isParticellaIstRiesameCancellabile(long idAzienda, 
      Vector<Long> vIdConduzioneParticella) throws SolmrException, Exception;
  
  public boolean existIstanzaEsameAttivaFase(long idAzienda, 
      Long idParticella, long idFase) throws SolmrException, Exception;
  
  public FaseRiesameDocumentoVO getFaseRiesameDocumentoByIdDocumento(long idDocumento) 
      throws SolmrException, Exception;
  
  public boolean isParticellaInPotenziale(long idAzienda, 
      long idParticella, int anno) throws SolmrException, Exception;
  
  public boolean isParticellaInPotenzialeContra(long idAzienda, 
      long idParticella, int anno) throws SolmrException, Exception;
  
  public boolean isNotPossibleIstanzaRiesameFaseSuccessiva(long idAzienda, 
      long idParticella, int idFase, int anno, String parametro) 
    throws SolmrException, Exception;
  
  public AllegatoDichiarazioneVO getAllegatoDichiarazioneFromIdDichiarazione(
      long idDichiarazioneConsistenza, long idTipoAllegato)
    throws SolmrException, Exception;
  
  public AllegatoDichiarazioneVO getAllegatoDichiarazioneFromTipoStampaAndIdDichiarazione(
      long idDichiarazioneConsistenza, String tipoStampa)
    throws SolmrException, Exception;
  
  public AllegatoDichiarazioneVO getAllegatoDichiarazioneFromIdAllegato(
      long idAllegato)
    throws SolmrException, Exception;
  
  /*public Vector<AllegatoDichiarazioneVO> getElencoAllegatiDichiarazioneDefault(
      Date dataInserimentoValidazione, int idMotivoDichiarazione) 
    throws SolmrException, Exception;*/
  
  public Vector<AllegatoDichiarazioneVO> getElencoAllegatiAttiviDichiarazione(
      long idDichiarazioneConsistenza) 
    throws SolmrException, Exception;
  
  public Vector<AllegatoDichiarazioneVO> getAllElencoAllegatiDichiarazione(
      long idDichiarazioneConsistenza) 
    throws SolmrException, Exception;
  
  public Vector<TipoFirmaVO> getElencoTipoFirma()
      throws SolmrException, Exception;

  /** ******************************************** */
  /** ***** DOCUMENTO END ****** */
  /** ******************************************** */
  
  
  /** ******************************************** */
  /** ***** TERRENI START ****** */
  /** ******************************************** */
  
  public PlSqlCodeDescription allineaSupEleggibilePlSql(long idAzienda, 
      long idUtenteAggiornamento)
    throws SolmrException, Exception;
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaComune(long idAzienda)
    throws SolmrException, Exception;
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaComuneDichiarato(long idDichiarazioneConsistenza)
    throws SolmrException, Exception;
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaUvaDaVino(long idAzienda)
    throws SolmrException, Exception;
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaUvaDaVinoDichiarato(long idDichiarazioneConsistenza)
    throws SolmrException, Exception;
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaVinoDOP(long idAzienda)
    throws SolmrException, Exception;
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaVinoDOPDichiarato(long idDichiarazioneConsistenza)
    throws SolmrException, Exception;
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoProvinciaVinoDOP(long idAzienda)
    throws SolmrException, Exception;
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoProvinciaVinoDOPDichiarato(
      long idDichiarazioneConsistenza) throws SolmrException, Exception;
  
  public TreeMap<String,Vector<RiepiloghiUnitaArboreaVO>> riepilogoElencoSociProvinciaVinoDOP(long idAzienda) 
    throws SolmrException, Exception;
  
  public Vector<Long> esisteUVValidataByConduzioneAndAzienda(Vector<Long> elencoConduzioni, Long idAzienda)
    throws SolmrException, Exception;
  
  public Vector<Long> esisteUVByConduzioneAndAzienda(Vector<Long> elencoConduzioni, Long idAzienda)
    throws SolmrException, Exception;
  
  public Vector<Long> esisteUVModProcVITIByConduzioneAndAzienda(Vector<Long> elencoConduzioni, Long idAzienda)
    throws SolmrException, Exception;
  
  public Vector<StoricoParticellaArboreaExcelVO> searchStoricoUnitaArboreaExcelSempliceByParameters( 
      Long idAzienda, FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO)
    throws SolmrException, Exception;
  
  public Vector<StoricoParticellaVO> getListUVForInserimento(Long idParticellaCurr, Vector<Long> vIdParticella, Long idAzienda)
    throws SolmrException, Exception;
  
  public BigDecimal getSupEleggibilePlSql(long idParticellaCertificata, long idCatalogoMatrice)
    throws SolmrException, Exception;
  
  public PlSqlCodeDescription compensazioneAziendalePlSql(long idAzienda, 
      long idUtenteAggiornamento)  throws SolmrException, Exception;
  
  public Vector<UVCompensazioneVO> getUVPerCompensazione(long idAzienda)
      throws SolmrException, Exception;
  
  public CompensazioneAziendaVO getCompensazioneAzienda(long idAzienda)
      throws SolmrException, Exception;
  
  public Vector<RiepilogoCompensazioneVO> getRiepilogoPostAllinea(long idAzienda)
      throws SolmrException, Exception;
  
  public Vector<RiepilogoCompensazioneVO> getRiepilogoDirittiVitati(long idAzienda)
      throws SolmrException, Exception;
  
  public CompensazioneAziendaVO getCompensazioneAziendaByIdAzienda(long idAzienda)
      throws SolmrException, Exception;
  
  public int countUVAllineabiliGis(long idAzienda)
      throws SolmrException, Exception;
  
  public int countUVIstRiesameCompen(long idAzienda)
      throws SolmrException, Exception;
  
  public int countSupUVIIrregolari(long idAzienda)
      throws SolmrException, Exception;
  
  public Date getMaxDataAggiornamentoConduzioniAndUV(long idAzienda)
      throws SolmrException, Exception;
  
  public Date getMaxDataFotoInterpretazioneUV(long idAzienda)
      throws SolmrException, Exception;
  
  public boolean existsIsoleParcDopoVarSched(long idAzienda, long isolaDichiarata)
      throws SolmrException, Exception;
  
  public BigDecimal getSupNonCompensabile(long idAzienda)
      throws SolmrException, Exception;
  
  public int countPercPossessoCompensazioneMag100(long idAzienda)
      throws SolmrException, Exception;
  
  public BigDecimal getPercUtilizzoEleggibile(long idAzienda, long idParticella)
      throws SolmrException, Exception;
  
  public Vector<Vector<DirittoCompensazioneVO>> getDirittiCalcolati(long idAzienda)
      throws SolmrException, Exception;
  
  public BigDecimal getSumAreaGiaAssegnata(long idUnitaArborea)
      throws SolmrException, Exception;
  
  public BigDecimal getSumAreaMaxAssegnabile(long idUnitaArborea)
      throws SolmrException, Exception;
  
  public void allineaUVaCompensazione(long idAzienda, RuoloUtenza ruoloUtenza)
      throws SolmrException, Exception;
  
  //public void allineaUVinTolleranzaGIS(long idAzienda, RuoloUtenza ruoloUtenza)
    //  throws SolmrException, Exception;
  
  public ParticellaBioVO getParticellaBio(long idParticella, long idAzienda, Date dataInserimentoDichiarazione)
    throws SolmrException, Exception;
  
  public BigDecimal getSumUtilizziPrimariNoIndicati(long idAzienda, long idParticella, Vector<String> vIdUtilizzo)
    throws SolmrException, Exception;
  
  public BigDecimal getSumUtilizziPrimariParticellaAltreConduzioni(long idAzienda, long idParticella,
      Vector<Long> vIdConduzioneParticella) throws SolmrException, Exception;
  
  public BigDecimal getSumPercentualPossessoAltreConduzioni(long idAzienda, long idParticella,
      Vector<Long> vIdConduzioneParticella) throws SolmrException, Exception;
  
  public BigDecimal getSumSupCondottaAltreConduzioni(long idAzienda, long idParticella,
      Vector<Long> vIdConduzioneParticella) throws SolmrException, Exception;
  
  public Vector<IstanzaRiesameVO> getFasiIstanzaRiesame(long idAzienda, long idParticella,
      Date dataInserimentoDichiarazione) throws SolmrException, Exception;
  
  public int calcolaP30PlSql(long idStoricoParticella, Date dataInizioValidita)
      throws SolmrException, Exception;
  
  public int calcolaP25PlSql(long idStoricoParticella, Date dataInizioValidita)
      throws SolmrException, Exception;
  
  public int calcolaP26PlSql(long idAzienda, long idParticella, Long idParticellaCertificata)
      throws SolmrException, Exception;
  
  public Vector<CasoParticolareVO> getCasiParticolari(String particellaObbligatoria)
      throws SolmrException, Exception;
  
  public Vector<TipoRiepilogoVO> getTipoRiepilogo(String funzionalita, String codiceRuolo)
      throws SolmrException, Exception;
  
  public Vector<TipoEsportazioneDatiVO> getTipoEsportazioneDati(String codMenu, String codiceRuolo)
      throws SolmrException, Exception;
  
  public boolean isParticellAttivaStoricoParticella(String istatComune, String sezione,
      String foglio, String particella, String subalterno) 
    throws SolmrException, Exception;
  
  public Vector<StoricoParticellaVO> getElencoParticelleForPopNotifica(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO)
    throws SolmrException, Exception;
  
  public StoricoParticellaVO findStoricoParticellaDichCompleto(long idStoricoParticella, 
      long idDichiarazioneConsistenza)
    throws SolmrException, Exception;
  
  public Vector<TipoEfaVO> getListTipoEfa()  throws SolmrException, Exception;
  
  public Vector<TipoEfaVO> getListLegendaTipoEfa() throws SolmrException, Exception;
  
  public TipoEfaVO getTipoEfaFromIdCatalogoMatrice(long idCatalogoMatrice)
      throws SolmrException, Exception;
  
  public Vector<TipoPeriodoSeminaVO> getListTipoPeriodoSemina() 
      throws SolmrException, Exception;
  
  public Vector<TipoUtilizzoVO> getListTipoUtilizzoEfa(long idTipoEfa)
      throws SolmrException, Exception;
   
  public Vector<TipoVarietaVO> getListTipoVarietaEfaByMatrice(long idTipoEfa, 
      long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso, long idTipoQualitaUso)
     throws SolmrException, Exception;
  
  public Vector<TipoDestinazioneVO> getListTipoDestinazioneEfa(long idTipoEfa, long idUtilizzo)
      throws SolmrException, Exception;
   
  public Vector<TipoDettaglioUsoVO> getListDettaglioUsoEfaByMatrice(long idTipoEfa,
      long idUtilizzo, long idTipoDestinazione)  throws SolmrException, Exception;
  
  public Vector<TipoQualitaUsoVO> getListQualitaUsoEfaByMatrice(long idTipoEfa,
      long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso) 
          throws SolmrException, Exception;
  
  public Integer getAbbPonderazioneByMatrice(
      long idTipoEfa, long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso,
      long idTipoQualitaUso, long idVarieta)
    throws SolmrException, Exception;
  
  public String getValoreAttivoTipoAreaFromParticellaAndId(
      long idParticella, long idTipoArea)
          throws SolmrException, Exception;
  
  public Vector<TipoAreaVO> getValoriTipoAreaParticella(long idParticella, Date dataInserimentoValidazione)
      throws SolmrException, Exception;
  
  public Vector<TipoAreaVO> getAllValoriTipoArea()
      throws SolmrException, Exception;
  
  public Vector<TipoAreaVO> getDescTipoAreaBrogliaccio(Long idDichiarazioneConsistenza)
      throws SolmrException, Exception;
  
  public Vector<TipoAreaVO> getValoriTipoAreaFoglio(String comune, String foglio, String sezione)
      throws SolmrException, Exception;
  
  public Vector<TipoMetodoIrriguoVO> getElencoMetodoIrriguo()
      throws SolmrException, Exception;
  
  public Vector<TipoPeriodoSeminaVO> getListTipoPeriodoSeminaByCatalogo(long idUtilizzo,
      long idDestinazione, long idDettUso, long idQualiUso, long idVarieta)
      throws SolmrException, Exception;
  
  public Vector<TipoFonteVO> getElencoAllTipoFonte() throws SolmrException, Exception;
  
  public boolean isUtilizzoAttivoSuMatrice(long idUtilizzo) throws SolmrException, Exception;
  
  public Vector<TipoAreaVO> riepilogoTipoArea(long idAzienda)
      throws SolmrException, Exception;
  
  public Vector<TipoAreaVO> riepilogoTipoAreaDichiarato(long idDichiarazioneConsistenza)
      throws SolmrException, Exception;
  
  public Vector<TipoAreaVO> getValoriTipoAreaFiltroElenco(Long idDichiarazioneConsistenza)
      throws SolmrException, Exception;
  
  public boolean isDettaglioUsoObbligatorio(long idTipoEfa,
      long idVarieta) throws SolmrException, Exception;
  
  public TipoEfaVO getTipoEfaFromPrimaryKey(long idTipoEfa)
      throws SolmrException, Exception;
  
  public boolean isAltreUvDaSchedario(long idParticella)
      throws SolmrException, Exception;
  
  public Vector<Long> getIdParticellaByIdConduzione(Vector<Long> vIdConduzioneParticella)
    throws SolmrException, Exception;
  
  public Vector<ProvinciaVO> getListProvincieParticelleByIdAzienda(Long idAzienda)
    throws SolmrException, Exception;
  
  public Vector<ProvinciaVO> getListProvincieParticelleByIdDichiarazioneConsistenza(Long idDichiarazioneConsistenza)
      throws SolmrException, Exception;
  
  public Vector<IsolaParcellaVO> getElencoIsoleParcelle(String nomeLib, long idAzienda)
    throws SolmrException, Exception;
  
  public Vector<StoricoParticellaVO> associaParcelleGisLettura(String nomeLib, long idAzienda, long idUtente)
    throws SolmrException, Exception;
  
  public void associaParcelleGisConferma(long idAzienda, long idUtente, Vector<Long> vIdUnarParcellaSel)
    throws SolmrException, Exception;
  
  public ConsistenzaVO getDichConsUVParcelleDoppie(long idAzienda)
    throws SolmrException, Exception;
  
  public void allineaUVaGIS(Vector<Long> vIdIsolaParcella, long idAzienda, long idDichiarazioneConsistenza, RuoloUtenza ruoloUtenza)
    throws SolmrException, Exception;
  
  //public Integer getTolleranzaPlSql(String nomeLib, long idAzienda, long idUnitaArborea)
    //throws SolmrException, Exception;
  
  public Vector<RegistroPascoloVO> getRegistroPascoliPianoLavorazione(
      long idParticellaCertificata) throws SolmrException, Exception;
  
  public Vector<RegistroPascoloVO> getRegistroPascoliPianoLavorazioneChiaveCatastale(
      String istatComune, String foglio, String particella, String sezione, String subalterno) 
    throws SolmrException, Exception;
  
  public Vector<RegistroPascoloVO> getRegistroPascoliDichCons(
      long idParticellaCertificata, int annoDichiarazione)
    throws SolmrException, Exception;
  
  public Vector<RegistroPascoloVO> getRegistroPascoliDichConsChiaveCatastale(
      String istatComune, String foglio, String particella, String sezione, String subalterno, int annoDichiarazione)
    throws SolmrException, Exception;
  
  public boolean isRegistroPascoliPratoPolifita(
      long idParticellaCertificata) throws SolmrException, Exception;
  
  public Vector<Long> getIdConduzioneFromIdAziendaIdParticella(long idAzienda, long idParticella)
      throws SolmrException, Exception;
  
  public Vector<AnagParticellaExcelVO> searchParticelleStabGisExcelByParameters(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda)
      throws SolmrException, Exception;
  
  public Vector<AnagParticellaExcelVO> searchParticelleAvvicExcelByParameters(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda)
      throws SolmrException, Exception;
  
 /* public HashMap<Long,HashMap<Integer,AvvicendamentoVO>> getElencoAvvicendamento( 
      Vector<Long> vIdParticella, long idAzienda, Integer annoPartenza, int numeroAnni, 
      Long idDichiarazioneConsistenza) 
    throws SolmrException, Exception;*/
  
  public Vector<Long> getIdUtilizzoFromIdIdConduzione(long idConduzioneParticella) 
      throws SolmrException, Exception;
  
  public BigDecimal getSumSupUtilizzoParticellaAzienda(long idAzienda, long idParticella) 
      throws SolmrException, Exception;
  
  public Vector<UtilizzoParticellaVO> riepilogoTipoEfaPianoLavorazione(long idAzienda) 
      throws SolmrException, Exception;
  
  public Vector<UtilizzoParticellaVO> riepilogoTipoEfaDichiarazione(long idDichiarazioneConsistenza)  
      throws SolmrException, Exception;
  
  public Vector<UtilizzoParticellaVO> riepilogoTipoGreeningPianoLavorazione(
      long idAzienda) throws SolmrException, Exception;
  
  public Vector<UtilizzoParticellaVO> riepilogoTipoGreeningDichiarazione(long idDichiarazioneConsistenza) 
      throws SolmrException, Exception;
  
  public Vector<TipoEfaVO> getElencoTipoEfaForAzienda(long idAzienda)
      throws SolmrException, Exception;
  
  public Vector<TipoDestinazioneVO> getElencoTipoDestinazioneByMatrice(long idUtilizzo)
      throws SolmrException, Exception;
  
  public Vector<TipoVarietaVO> getElencoTipoVarietaByMatrice(
      long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso, long idTipoQualitaUso)
    throws SolmrException, Exception;
  
  public Vector<TipoQualitaUsoVO> getElencoTipoQualitaUsoByMatrice(
      long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso) 
      throws SolmrException, Exception;
  
  public CatalogoMatriceVO getCatalogoMatriceFromMatrice(long idUtilizzo, long idVarieta,
      long idTipoDestinazione, long idTipoDettaglioUso, long idTipoQualitaUso) 
          throws SolmrException, Exception;
  
  public CatalogoMatriceVO getCatalogoMatriceFromPrimariKey(long idCatalogoMatrice) 
      throws SolmrException, Exception;
  
  public CatalogoMatriceSeminaVO getCatalogoMatriceSeminaDefault(long idCatalogoMatrice)
      throws SolmrException, Exception;
  
  public CatalogoMatriceSeminaVO getCatalogoMatriceSeminaByIdTipoPeriodo(long idCatalogoMatrice, 
      long idTipoPeriodoSemina) throws SolmrException, Exception;
  
  public Vector<Long> getListIdPraticaMantenimentoPlSql(
      long idCatalogoMatrice, String flagDefault) throws SolmrException, Exception;
  
  public Vector<TipoSeminaVO> getElencoTipoSemina() throws SolmrException, Exception;
  
  public Vector<TipoPraticaMantenimentoVO> getElencoPraticaMantenimento(
      Vector<Long> vIdMantenimento) throws SolmrException, Exception;
  
  public Vector<TipoFaseAllevamentoVO> getElencoFaseAllevamento() throws SolmrException, Exception;
  
  public String cambiaPercentualePossessoMassivo(RuoloUtenza ruoloUtenza, 
      long idAzienda, BigDecimal percentualePossesso) throws SolmrException, Exception;
  
  public BigDecimal getSumSupCondottaAsservimentoParticellaAzienda(long idAzienda, long idParticella) 
      throws SolmrException, Exception;
  
  public String cambiaPercentualePossessoSupUtilizzataMassivo(RuoloUtenza ruoloUtenza, Long idAzienda)
      throws SolmrException, Exception;
  
  public void avviaConsolidamento(long idAzienda, RuoloUtenza ruoloUtenza) 
      throws SolmrException, Exception;
  
  public void modificaConduzioneEleggibileUV(HashMap<Long, ConduzioneEleggibilitaVO> hPartCondEleg)
      throws SolmrException, Exception;
  
  public void allineaPercUsoElegg(long idAzienda, Vector<Long> vIdParticella, long idUtente)
      throws SolmrException, Exception;  
  
  public Vector<TipoDettaglioUsoVO> getListDettaglioUsoByMatrice(
      long idUtilizzo, long idTipoDestinazione) 
      throws SolmrException, Exception;
  
  public TipoDettaglioUsoVO findDettaglioUsoByPrimaryKey(
      long idTipoDettaglioUso) throws SolmrException, Exception;
  
  
  /** ******************************************** */
  /** ***** TERRENI END ****** */
  /** ******************************************** */
  
  
  /** ******************************************** */
  /** ***** STAMPA START ****** */
  /** ******************************************** */
  
  public RichiestaTipoReportVO[] getElencoSubReportRichiesta(
      String codiceReport, java.util.Date dataRiferimento) throws SolmrException, Exception;
  
  public AnagAziendaVO getAnagraficaAzienda(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException, Exception;
  
  public Vector<ParticellaVO> getParticelleUtilizzoIstanzaRiesame(long idDocumento)  
      throws SolmrException, Exception;
  
  public BigDecimal getSumSupCatastaleTerreniIstanzaRiesame(long idDocumento)  
      throws SolmrException, Exception;
  
  public BigDecimal[] getSumSupAgronomicaAndCondottaTerreniIstanzaRiesame(long idDocumento)
      throws SolmrException, Exception;
  
  public Vector<TipoReportVO> getElencoTipoReport(
      String codiceTipoReport, java.util.Date dataRiferimento) 
      throws SolmrException, Exception;
  
  public Vector<TipoReportVO> getElencoTipoReportByValidazione(
      String codiceTipoReport, Long idDichiarazioneConsistenza) 
    throws SolmrException, Exception;
  
  public DichiarazioneConsistenzaGaaVO getDatiDichiarazioneConsistenza(
      long idDichiarazioneConsistenza) throws SolmrException, Exception;
  
  public LogoVO getLogo(String istatRegione, String provincia) 
      throws SolmrException, Exception;
  
  public InfoFascicoloVO getInfoFascicolo(
      long idAzienda, java.util.Date dataRiferimento, Long codFotografia) 
    throws SolmrException, Exception;
  
  public ValoriCondizionalitaVO getValoriCondizionalita(Long idAzienda, Long codiceFotografia) 
    throws SolmrException, Exception;
  
  public TipoAttestazioneVO[] getListAttestazioniAlPianoAttuale(
      Long idAzienda, Long idAllegato) throws SolmrException, Exception;
  
  public TipoAttestazioneVO[] getListAttestazioneAllaDichiarazione(
      Long codiceFotografiaTerreni, java.util.Date dataAnnoCampagna, Long idAllegato) 
    throws SolmrException, Exception;
  
  public Vector<ContoCorrenteVO> getStampaContiCorrenti(
      Long idAzienda, java.util.Date dataInserimentoDichiarazione)
    throws SolmrException, Exception;  
  
  public Vector<TipoReportVO> getListTipiDocumentoStampaProtocollo(long idDocumento[]) 
    throws SolmrException, Exception;
  
  public String getTipoDocumentoStampaProtocollo(long idTipoReport) 
    throws SolmrException, Exception;
  
  public int getCountDocumentoCompatibile(long idTipoReport, long[] idDocumento) 
      throws SolmrException, Exception;
  
  public Vector<CodeDescription> getElencoPropietariDocumento(long[] idDocumento)
      throws SolmrException, Exception;
  
  public TipoReportVO getTipoReport(long idTipoReport)
      throws SolmrException, Exception;
  
  public TipoReportVO getTipoReportByCodice(String codiceReport)
      throws SolmrException, Exception;
  
  public Vector<TipoAttestazioneVO> getAttestStampaProtoc(long idReportSubReport)
      throws SolmrException, Exception;
  
  public boolean isNumeroProtocolloValido(String numeroProtocollo)
      throws SolmrException, Exception;
  
  public Vector<ParticellaVO> getElencoParticelleVarCat(long idAzienda, long idDichiarazioneConsistenza)
      throws SolmrException, Exception;
  
  public Vector<RiepiloghiUnitaArboreaVO> getStampaUVRiepilogoIdoneita(Long idAzienda, 
      Long idDichiarazioneConsistenza) throws SolmrException, Exception;
  
  public Vector<TipoFormaConduzioneVO> getFormaConduzioneSezioneAnagrafica(Long idAzienda,
      Date dataRiferimento) throws SolmrException, Exception;
  
  public Vector<TipoInfoAggiuntivaVO> getTipoInfoAggiuntiveStampa(long idAzienda, 
      Date dataInserimentoDichiarazione) 
    throws SolmrException, Exception;
  
  public Vector<AnagAziendaVO> getAziendeAssociateStampa(long idAzienda, 
      Date dataInserimentoDichiarazione) 
    throws SolmrException, Exception;
  
  public Vector<ConduzioneParticellaVO> getRiepilogoConduzioneStampa(long idAzienda, 
      Long codFotografia) throws SolmrException, Exception;
  
  public Vector<StoricoParticellaVO> getRiepilogoComuneStampa(long idAzienda, 
      Long codFotografia) throws SolmrException, Exception;
  
  public Vector<RiepiloghiUnitaArboreaVO> getStampaUVRiepilogoVitignoIdoneita(Long idAzienda, 
      Long idDichiarazioneConsistenza) throws SolmrException, Exception;
  
  public Vector<FabbricatoVO> getStampaFabbricati(
      Long idAzienda, Date dataRiferimento)  throws SolmrException, Exception;
  
  public Vector<DocumentoVO> getDocumentiStampaMd(Long idAzienda, Long idDichiarazioneConsistenza,
      String cuaa, java.util.Date dataInserimentoDichiarazione) throws SolmrException, Exception;
  
  public Vector<RiepilogoStampaTerrVO> getStampaRiepiloghiStoricoPartTipoArea(
      String tipoArea, Long idAzienda, Long codFotografia) throws SolmrException, Exception;
  
  public Vector<RiepilogoStampaTerrVO> getStampaRiepiloghiFoglioTipoArea(
      String tipoArea, Long idAzienda, Long codFotografia)  throws SolmrException, Exception;
      
  public Vector<TipoAttestazioneVO> getListAttestazioniDichiarazioniAlPianoAttuale(
      Long idAzienda, String voceMenu, boolean flagCondizionalita)  throws SolmrException, Exception;
  
  public Vector<TipoAttestazioneVO> getListAttestazioniDichiarazioniAllaValidazione(
      Long codiceFotografiaTerreni, Date dataInserimentoDichiarazione, String voceMenu, boolean flagCondizionalita)  
    throws SolmrException, Exception;
  
  public Vector<TipoAttestazioneVO> getElencoAllegatiAllaValidazionePerStampa(
      Long codiceFotografiaTerreni, Date dataInserimentoDichiarazione, String codiceAttestazione)
    throws SolmrException, Exception;
  
  public Vector<TipoAttestazioneVO> getAttestazioniFromSubReport(long idReportSubReport, 
      Date dataInserimentoDichiarazione) throws SolmrException, Exception;
  
  public Vector<TipoAttestazioneVO> getAttestazioniFromSubReportValidDettagli(long idReportSubReport, 
      Date dataInserimentoDichiarazione, Long codiceFotografiaTerreni) throws SolmrException, Exception;
  
  public String getTipoReportByValidazioneEAllegato(
      long idDichiarazioneConsistenza, int idTipoAllegato) throws SolmrException, Exception;
  
  public TipoAllegatoVO getTipoAllegatoById(int idTipoAllegato)
      throws SolmrException, Exception;
  
  public Vector<TipoAllegatoVO> getTipoAllegatoForValidazione(int idMotivoDichiarazione, Date dataInserimentoDichiarazione) 
      throws SolmrException, Exception;
  
  public Vector<TipoAllegatoVO> getTipoAllegatoObbligatorioForValidazione(int idMotivoDichiarazione) 
      throws SolmrException, Exception;
  
  public TipoFirmatarioVO getTipoFirmatarioById(int idTipoFirmatario)
      throws SolmrException, Exception;
  
  /** ******************************************** */
  /** ***** STAMPA END ****** */
  /** ******************************************** */
  
  /** ******************************************** */
  /** ***** POLIZZA START ****** */
  /** ******************************************** */
  
  public Vector<Integer> getElencoAnnoCampagnaByIdAzienda(long idAzienda) 
    throws SolmrException, Exception;
  
  public Vector<BaseCodeDescription> getElencoInterventoByIdAzienda(long idAzienda)
    throws SolmrException, Exception;
  
  public Vector<PolizzaVO> getElencoPolizze(long idAzienda, Integer annoCampagna, Long idIntervento)
    throws SolmrException, Exception;
  
  public PolizzaVO getDettaglioPolizza(long idPolizzaAssicurativa)
    throws SolmrException, Exception;
  
  public TreeMap<Long,Vector<PolizzaDettaglioVO>> getDettaglioPolizzaColtura(long idPolizzaAssicurativa)
    throws SolmrException, Exception;
  
  public TreeMap<Long,Vector<PolizzaDettaglioVO>> getDettaglioPolizzaStruttura(long idPolizzaAssicurativa)
    throws SolmrException, Exception;
  
  public TreeMap<Long,Vector<PolizzaDettaglioVO>> getDettaglioPolizzaZootecnia(long idPolizzaAssicurativa)
    throws SolmrException, Exception;
  
  /** ******************************************** */
  /** ***** POLIZZA END ****** */
  /** ******************************************** */
  
  
  /** ******************************************** */
  /** ***** MANUALE START ****** */
  /** ******************************************** */
  
  public Vector<ManualeVO> getElencoManualiFromRuoli(String descRuolo) 
      throws SolmrException, Exception;
  
  public ManualeVO getManuale(long idManuale) throws SolmrException, Exception;
  
  /** ******************************************** */
  /** ***** MANUALE END ****** */
  /** ******************************************** */
  
  
  /** ******************************************** */
  /** ***** CONSISTENZA START ****** */
  /** ******************************************** */
  
  public InvioFascicoliVO getLastSchedulazione(long idDichiarazioneConsistenza)
      throws SolmrException, Exception;
  
  public boolean trovaSchedulazioneAttiva(long idAzienda, long idDichiarazioneConsistenza)
      throws SolmrException, Exception;  
  
  public void insertSchedulazione(InvioFascicoliVO invioFascicoliVO, long idUtente)
      throws SolmrException, Exception;
  
  public void deleteSchedulazione(long idInvioFascicoli) 
      throws SolmrException, Exception;
  /** ******************************************** */
  /** ***** CONSISTENZA END ****** */
  /** ******************************************** */
  
  
  /** ******************************************** */
  /** ***** NUOVA ISCRIZIONE START ****** */
  /** ******************************************** */
  public AziendaNuovaVO getAziendaNuovaIscrizione(String cuaa, long[] arrTipoRichiesta)      
      throws SolmrException, Exception;
  
  public AziendaNuovaVO getAziendaNuovaIscrizioneEnte(String codEnte, long[] arrTipoRichiesta)      
      throws SolmrException, Exception;
  
  public AziendaNuovaVO getAziendaNuovaIscrizioneByPrimaryKey(Long idAziendaNuova)     
      throws SolmrException, Exception;
  
  public Long insertAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento)     
      throws SolmrException, Exception;
  
  public Long insertAziendaNuovaRichiestaValCess(RichiestaAziendaVO richiestaAziendaVO)     
      throws SolmrException, Exception;
  
  public Long insertAziendaNuovaRichiestaVariazione(RichiestaAziendaVO richiestaAziendaVO)
      throws SolmrException, Exception;
  
  public void updateAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento)     
      throws SolmrException, Exception;
  
  public Vector<UteAziendaNuovaVO> getUteAziendaNuovaIscrizione(
      long idAziendaNuova)  throws SolmrException, Exception;
  
  public void aggiornaUteAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento, Vector<UteAziendaNuovaVO> vUteAziendaNuova)     
      throws SolmrException, Exception;
  
  public Vector<FabbricatoAziendaNuovaVO> getFabbrAziendaNuovaIscrizione(long idAziendaNuova)     
      throws SolmrException, Exception;
  
  public void aggiornaFabbrAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento, 
      Vector<FabbricatoAziendaNuovaVO> vFabbrAziendaNuova)  throws SolmrException, Exception;
  
  public boolean existsDependenciesUte(long idUteAziendaNuova)      
      throws SolmrException, Exception;
  
  public Vector<ParticellaAziendaNuovaVO> getParticelleAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException, Exception;
  
  public Vector<UnitaMisuraVO> getListUnitaMisura()  throws SolmrException, Exception;
  
  public void aggiornaParticelleAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento, 
      Vector<ParticellaAziendaNuovaVO> vParticelleAziendaNuova) 
    throws SolmrException, Exception;
  
  public Vector<AllevamentoAziendaNuovaVO> getAllevamentiAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException, Exception;
  
  public void aggiornaAllevamentiAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento, 
      Vector<AllevamentoAziendaNuovaVO> vAllevamentiAziendaNuova) throws SolmrException, Exception;
  
  public Vector<CCAziendaNuovaVO> getCCAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException, Exception;
  
  public void aggiornaCCAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento,
      Vector<CCAziendaNuovaVO> vCCAziendaNuova) throws SolmrException, Exception;
  
  public Vector<RichiestaAziendaDocumentoVO> getAllegatiAziendaNuovaIscrizione(
      long idAziendaNuova, long idTipoRichiesta) throws SolmrException, Exception;
  
  public Long insertRichAzDocAziendaNuova(RichiestaAziendaDocumentoVO richiestaAziendaDocumentoVO)
      throws SolmrException, Exception;
  
  public void deleteDocumentoRichiesta(long idRichiestaDocumento) throws SolmrException,
      Exception;
  
  public void insertFileStampa(long idRichiestaAzienda,  byte ba[])
      throws SolmrException, Exception;
  
  public void aggiornaStatoNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, 
      long idUtenteAggiornamento, IterRichiestaAziendaVO iterRichiestaAziendaVO)     
        throws SolmrException, Exception;
  
  public void aggiornaStatoRichiestaValCess(AziendaNuovaVO aziendaNuovaVO, 
      long idUtenteAggiornamento, IterRichiestaAziendaVO iterRichiestaAziendaVO)     
      throws SolmrException, Exception;
  
  public Vector<Long> getElencoIdRichiestaAzienda(
      Long idTipoRichiesta, Long idStatoRichiesta, String cuaa, String partitaIva, String denominazione,
      Long idAzienda, RuoloUtenza ruoloUtenza) throws SolmrException, Exception;
  
  public Vector<Long> getElencoIdRichiestaAziendaGestCaa(
      Long idTipoRichiesta, Long idStatoRichiesta, String cuaa, String partitaIva, String denominazione, 
      RuoloUtenza ruoloUtenza) throws SolmrException, Exception;
  
  public Vector<Long> getElencoRichieseteAziendaByIdRichiestaAzienda(long idAzienda, String codiceRuolo) 
      throws SolmrException, Exception;
  
  public Vector<AziendaNuovaVO> getElencoAziendaNuovaByIdRichiestaAzienda(
      Vector<Long> vIdRichiestaAzienda) throws SolmrException, Exception;
  
  public Vector<CodeDescription> getListTipoRichiesta() throws SolmrException, Exception;
  
  public Vector<CodeDescription> getListTipoRichiestaVariazione(String codiceRuolo) throws SolmrException,
    Exception;
  
  public Vector<StatoRichiestaVO> getListStatoRichiesta() throws SolmrException, Exception;
  
  public RichiestaAziendaVO getPdfAziendaNuova(
      long idRichiestaAzienda) throws SolmrException, Exception;
  
  public PlSqlCodeDescription ribaltaAziendaPlSql(long idRichiestaAzienda) 
      throws SolmrException, Exception;
  
  public boolean isPartitaIvaPresente(String partitaIva, long[] arrTipoRichiesta) 
      throws SolmrException, Exception;
  
  public void updateFlagDichiarazioneAllegati(long idRichiestaAzienda, 
      String flagDichiarazioneAllegati) throws SolmrException, Exception;
  
  public Vector<MotivoRichiestaVO> getListMotivoRichiesta(int idTipoRichiesta)
      throws SolmrException, Exception;
  
  public AziendaNuovaVO getRichAzByIdAzienda(long idAzienda, long idTipoRichiesta)
      throws SolmrException, Exception;
  
  public AziendaNuovaVO getRichAzByIdAziendaConValida(
      long idAzienda, long idTipoRichiesta) throws SolmrException, Exception;
  
  public void updateRichiestaAzienda(RichiestaAziendaVO richiestaAziendaVO) 
      throws SolmrException, Exception;
  
  public Vector<SoggettoAziendaNuovaVO> getSoggettiAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException, Exception;
  
  public SoggettoAziendaNuovaVO getRappLegaleNuovaIscrizione(
      long idAziendaNuova) throws SolmrException, Exception;
  
  public void aggiornaSoggettiAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento,
      Vector<SoggettoAziendaNuovaVO> vSoggettoAziendaNuova)     
        throws SolmrException, Exception;
  
  public void aggiornaMacchineIrrAziendaNuova(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento,
      Vector<MacchinaAziendaNuovaVO> vMacchineNuovaRichiesta)     
        throws SolmrException, Exception;
  
  public void aggiornaAzAssociateCaaAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, 
      long idUtenteAggiornamento, Vector<AzAssAziendaNuovaVO> vAssAziendaNuova)     
        throws SolmrException, Exception;
  
  public void aggiornaAzAssociateCaaRichiestaVariazione(AziendaNuovaVO aziendaNuovaVO, 
      long idUtenteAggiornamento, Vector<AzAssAziendaNuovaVO> vAssAziendaNuova)     
        throws SolmrException, Exception;
  
  public void aggiornaAzAssociateRichiestaVariazione(AziendaNuovaVO aziendaNuovaVO, 
      long idUtenteAggiornamento, Vector<AzAssAziendaNuovaVO> vAssAziendaNuova)     
        throws SolmrException, Exception;
  
  public HashMap<String, AzAssAziendaNuovaVO> getAziendeAssociateCaaAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException, Exception;
  
  public HashMap<String, AzAssAziendaNuovaVO> getAziendeAssociateCaaAziendaRichVariazione(
      long idRichiestaAzienda) throws SolmrException, Exception;
  
  public Vector<AzAssAziendaNuovaVO> getAziendeAssociateAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException, Exception;
  
  public Vector<AzAssAziendaNuovaVO> getAziendeAssociateAziendaRichVariazione(
      long idRichiestaAzienda) throws SolmrException, Exception;
  
  public void aggiornaAzAssociateAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, 
      long idUtenteAggiornamento, Vector<AzAssAziendaNuovaVO> vAssAziendaNuova)     
        throws SolmrException, Exception;
  
  public Vector<AzAssAziendaNuovaVO> getAziendeAssociateCaaStampaAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException, Exception;
  
  public void caricaMacchineNuovaRichiesta(long idAzienda, long idRichiestaAzienda) 
      throws SolmrException, Exception;
  
  public void caricaAziendeAssociateRichiesta(long idAzienda, long idRichiestaAzienda, String flagSoloAggiunta)     
      throws SolmrException, Exception;
  
  public void caricaAziendeAssociateCaaRichiesta(long idAzienda, long idRichiestaAzienda)     
      throws SolmrException, Exception;
  
  public void ribaltaMacchineNuovaRichiesta(long idRichiestaAzienda, long idUtenteAggiornamento)     
      throws SolmrException, Exception;
  
  public Vector<MacchinaAziendaNuovaVO> getMacchineAzNuova(long idRichiestaAzienda)
      throws SolmrException, Exception;
  
  public boolean isUtenteAbilitatoPresaInCarico(long idTipoRichiesta, String codiceRuolo) 
      throws SolmrException, Exception;
   
  /** ******************************************** */
  /** ***** NUOVA ISCRIZIONE END ****** */
  /** ******************************************** */

  /** ******************************************** */
  /** ***** MESSAGGISTICA START ****** */
  /** ******************************************** */
  public void confermaLetturaMessaggio(long idElencoMessaggi, String codiceFiscale) 
      throws SolmrException, Exception;
  
  public byte[] getAllegato(long idAllegato) 
      throws SolmrException, Exception;
  
  public ListaMessaggi getListaMessaggi(int idProcedimento, String codiceRuolo, 
      String codiceFiscale, int tipoMessaggio, Boolean letto, Boolean obbligatorio, Boolean visibile ) 
    throws SolmrException, LogoutException, Exception;
  
  public DettagliMessaggio getDettagliMessaggio(long idElencoMessaggi, String codiceFiscale) 
    throws SolmrException, Exception;
  
  /** ******************************************** */
  /** ***** MESSAGGISTICA END ****** */
  /** ******************************************** */
  
  
  /** ******************************************** */
  /** ***** WSBRIDGE START ****** */
  /** ******************************************** */
  
  public Hashtable<BigDecimal, SianAllevamentiVO> leggiAnagraficaAllevamenti(
    String cuaa, String dataRichiesta)
      throws SolmrException, Exception;
  
  public Hashtable<BigDecimal, SianAllevamentiVO> leggiAnagraficaAllevamentiNoProfile(String cuaa, String dataRichiesta)
      throws SolmrException, Exception;
  
  public ResponseWsBridgeVO serviceConsistenzaStatisticaMediaAllevamento(
      String cuaa)  throws SolmrException, Exception;
  
  public ResponseWsBridgeVO serviceConsistenzaUbaCensimOvini(
      String cuaa)  throws SolmrException, Exception;
  
  public void serviceWsBridgeAggiornaDatiBDN(String cuaa) 
      throws SolmrException, Exception;
  
  /** ******************************************** */
  /** ***** WSBRIDGE END ****** */
  /** ******************************************** */
  
  
  /** ******************************************** */
  /** ***** AgriWell START ****** */
  /** ******************************************** */
  
  public void insertFileAllegatoAgriWell(AllegatoDocumentoVO allegatoDocumentoVO) 
      throws SolmrException, Exception;
  
  public Long insertFileValidazioneAgriWell(AnagAziendaVO anagAziendaVO,
      Long idDichiarazioneConsistenza, byte[] fileStampa,
      RuoloUtenza ruoloUtenza) throws SolmrException, Exception;
  
  public Long insertFileValidazioneRigeneraAgriWell(AnagAziendaVO anagAziendaVO,
      Long idDichiarazioneConsistenza, byte[] fileStampa,
      RuoloUtenza ruoloUtenza, int idTipoAllegato) throws SolmrException, Exception;
  
  public Long insertFileValidazioneAllegatiRigeneraAgriWell(AnagAziendaVO anagAziendaVO,
      Long idDichiarazioneConsistenza, byte[] fileStampa,
      RuoloUtenza ruoloUtenza, int idTipoAllegato) throws SolmrException, Exception;
  
  public Long insertFileValidazioneAllegatiAgriWell(AnagAziendaVO anagAziendaVO,
      Long idDichiarazioneConsistenza, byte[] fileStampa,
      RuoloUtenza ruoloUtenza, int tipoAllegato) throws SolmrException, Exception;
  
  public Long insertFileRichiestaAgriWell(AziendaNuovaVO aziendaNuovaVO, byte[] fileStampa, 
      RuoloUtenza ruoloUtenza) throws SolmrException, Exception;
  
  public Long insertFileRichiestaValidazioneAgriWell(AziendaNuovaVO aziendaNuovaVO, byte[] fileStampa, 
      RuoloUtenza ruoloUtenza) throws SolmrException, Exception;
  
  public Long insertFileRichiestaVariazioneAgriWell(
      AziendaNuovaVO aziendaNuovaVO, byte[] fileStampa, RuoloUtenza ruoloUtenza)
      throws SolmrException, Exception;
  
  public AgriWellEsitoVO agriwellServiceLeggiDoquiAgri(long idDocumentoIndex)
      throws SolmrException, Exception;
  
  public AgriWellEsitoFolderVO agriwellFindFolderByPadreProcedimentoRuolo(int idProcedimento,
      String codRuoloUtente, Long idFolderMadre, boolean noEmptyFolder, Long idAzienda)
    throws SolmrException, Exception;
  
  public AgriWellEsitoIdDocVO agriwellFindListaDocumentiByIdFolder(long idFolder, int idProcedimento,
      String codRuoloUtente, Long idAzienda)
    throws SolmrException, Exception;
  
  public AgriWellEsitoDocumentoVO agriwellFindDocumentoByIdRange(long[] idDoc)
    throws SolmrException, Exception;
  
  public void aggiornaValidazioneAgriWell(AllegatoDichiarazioneVO allegatoDichiarazioneVO,
      Long idTipoFirma, String note, RuoloUtenza ruoloUtenza) throws SolmrException,
      Exception;
  
  public AllegatoDichiarazioneVO getAllegatoDichiarazioneFromIdDichiarazioneMaxStoric(
      long idDichiarazioneConsistenza, long idTipoAllegato) 
    throws SolmrException, Exception;
  
  /** ******************************************** */
  /** ***** AgriWell END ****** */
  /** ******************************************** */
  
  /** ******************************************** */
  /** ***** UmaServGaa START ****** */
  /** ******************************************** */
  
  public DittaUmaVO[] umaservGetAssegnazioniByIdAzienda(
      long idAzienda, String[] arrCodiceStatoAnag)  throws SolmrException, Exception;
  
  public AssegnazioneVO[] umaservGetDettAssegnazioneByRangeIdDomAss(
      long[] arrIdDomandaAssegnazione)  throws SolmrException, Exception;
  
  public Vector<MacchinaVO> serviceGetElencoMacchineByIdAzienda(Long idAzienda,
      Boolean storico, Long idGenereMacchina)  throws SolmrException, Exception;
  
  public Vector<Long> serviceGetElencoAziendeUtilizzatrici(Long idMacchina)
      throws SolmrException, Exception;
  
  public UtilizzoVO serviceGetUtilizzoByIdMacchinaAndIdAzienda(
      Long idMacchina, Long idAzienda) throws SolmrException, Exception;
  
  /** ******************************************** */
  /** ***** UmaServGaa END ****** */
  /** ******************************************** */
  
  
  /** ******************************************** */
  /** ***** ModolServGaa START ****** */
  /** ******************************************** */
  
  public byte[] callModol(byte[] xmlInput, AttributiModuloVO attributiModuloVO)  
      throws SolmrException, Exception;
  
  public byte[] trasformStaticPDF(byte[] xmlInput, AttributiModuloVO attributiModuloVO)
      throws SolmrException, Exception;
  
  /** ******************************************** */
  /** ***** ModolServGaa END ****** */
  /** ******************************************** */
  
  
  
  /** ******************************************** */
  /** ***** MacchineAgricoleGaa START ****** */
  /** ******************************************** */
  
  public long[] ricercaIdPossessoMacchina(FiltriRicercaMacchineAgricoleVO filtriRicercaMacchineAgricoleVO)
      throws SolmrException, Exception;
  
  public long[] ricercaIdPossessoMacchinaImport(
      FiltriRicercaMacchineAgricoleVO filtriRicercaMacchineAgricoleVO) throws SolmrException, Exception;
  
  public RigaRicercaMacchineAgricoleVO[] getRigheRicercaMacchineAgricoleById(
      long ids[]) throws SolmrException, Exception;
  
  public Vector<PossessoMacchinaVO> getElencoMacchineAgricoleForStampa(
      long idAzienda, Date dataInserimentoValidazione) throws SolmrException, Exception;
  
  public void popolaTabelleMacchineAgricoleConServizio(
      long idAzienda) throws SolmrException, Exception;
  
  public Vector<TipoGenereMacchinaGaaVO> getElencoTipoGenereMacchina() 
      throws SolmrException, Exception;
  
  public TipoGenereMacchinaGaaVO getTipoGenereMacchinaByPrimaryKey(long idGenereMacchina) 
      throws SolmrException, Exception;
  
  public PossessoMacchinaVO getPosessoMacchinaFromId(long idPossessoMacchina)
      throws SolmrException, Exception;
  
  public Vector<PossessoMacchinaVO> getElencoDitteUtilizzatrici(
      long idMacchina, Date dataScarico) throws SolmrException, Exception;
  
  public Vector<PossessoMacchinaVO> getElencoPossessoDitteUtilizzatrici(
      long idMacchina, long idAzienda) throws SolmrException, Exception;
  
  public Vector<TipoMacchinaGaaVO> getElencoTipoMacchina(String flaIrroratrice) 
      throws SolmrException, Exception;
  
  public Vector<TipoGenereMacchinaGaaVO> getElencoTipoGenereMacchinaFromRuolo(
      long idTipoMacchina, String codiceRuolo) throws SolmrException, Exception;
  
  public Vector<TipoCategoriaGaaVO> getElencoTipoCategoria(long idGenereMacchina) 
      throws SolmrException, Exception;
  
  public Vector<CodeDescription> getElencoTipoMarca()
      throws SolmrException, Exception;
  
  public Vector<CodeDescription> getElencoTipoMarcaByIdGenere(long idGenereMacchina) 
      throws SolmrException, Exception;
    
  public Vector<TipoFormaPossessoGaaVO> getElencoTipoFormaPossesso()
      throws SolmrException, Exception;
  
  public boolean existsMotoreAgricolo(Long idMarca, String modello,
      Integer annoCostruzione, String matricolaTelaio) throws SolmrException, Exception;
  
  public void inserisciMacchinaAgricola(PossessoMacchinaVO possessoMacchinaVO) 
      throws SolmrException, Exception;
  
  public void modificaMacchinaAgricola(long idPossessoMacchina, PossessoMacchinaVO possessoMacchinaVO, boolean flagModUte) 
      throws SolmrException, Exception;
  
  public void importaMacchinaAgricola(long idPossessoMacchina, PossessoMacchinaVO possessoMacchinaVO) 
      throws SolmrException, Exception;
  
  public boolean isMacchinaModificabile(long idPossessoMacchina, String codiceRuolo)
      throws SolmrException, Exception;
  
  public boolean isMacchinaPossMultiplo(long idMacchina)
      throws SolmrException, Exception;
  
  public Vector<CodeDescription> getElencoTipoScarico()
      throws SolmrException, Exception;
  
  public boolean isMacchinaGiaPossesso(long idMacchina, long idAzienda)
      throws SolmrException, Exception;
  
  public BigDecimal percMacchinaGiaInPossesso(long idMacchina)
      throws SolmrException, Exception;
  
  public TipoCategoriaGaaVO getTipoCategoriaFromPK(long idCategoria) 
      throws SolmrException, Exception;
  
  /** ******************************************** */
  /** ***** MacchineAgricoleGaa END ****** */
  /** ******************************************** */

  /** ******************************************** */
  /** ***** MARCATEMPORALEGAA START ****** */
  /** ******************************************** */
  
  public byte[] getMarcaTemporale(byte[] fileToMark) 
	      throws SolmrException, Exception;
  
  /** ******************************************** */
  /** ***** MARCATEMPORALEGAA END ****** */
  /** ******************************************** */
  
  
  /*
   * INIZIO SIANFA
   */
  
  public SianEsito getAggiornamentiFascicolo(String cuaa) throws SolmrException, Exception;
  
  public Boolean aggiornaFascicoloAziendale(String cuaa, Long idUtente) throws SolmrException, Exception;
  
  
  /*
   * FINE SIANFA
   */
  
  
  
  
}
