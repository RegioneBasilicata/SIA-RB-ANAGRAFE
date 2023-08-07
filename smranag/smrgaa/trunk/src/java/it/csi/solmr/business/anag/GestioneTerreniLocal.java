package it.csi.solmr.business.anag;

import it.csi.smranag.smrgaa.dto.ParticellaDettaglioValidazioniVO;
import it.csi.smranag.smrgaa.dto.SuperficieDescription;
import it.csi.smranag.smrgaa.dto.search.FiltriRicercaTerrenoImportaAsservimentoVO;
import it.csi.smranag.smrgaa.dto.search.FiltriRicercaTerrenoVO;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaTerreniImportaAsservimentoVO;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaTerreniVO;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.AnagParticellaExcelVO;
import it.csi.solmr.dto.anag.ConsistenzaVO;
import it.csi.solmr.dto.anag.FoglioVO;
import it.csi.solmr.dto.anag.ParticellaAssVO;
import it.csi.solmr.dto.anag.ParticellaCertElegVO;
import it.csi.solmr.dto.anag.ParticellaCertificataVO;
import it.csi.solmr.dto.anag.ProprietaCertificataVO;
import it.csi.solmr.dto.anag.terreni.AltroVitignoDichiaratoVO;
import it.csi.solmr.dto.anag.terreni.AltroVitignoVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneDichiarataVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneParticellaVO;
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
import it.csi.solmr.dto.anag.terreni.UtilizzoConsociatoVO;
import it.csi.solmr.dto.anag.terreni.UtilizzoDichiaratoVO;
import it.csi.solmr.dto.anag.terreni.UtilizzoParticellaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.exception.SolmrException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.ejb.Local;

@Local
public interface GestioneTerreniLocal
{
  public Vector<ComuneVO> getListComuniParticelleByIdAzienda(Long idAzienda,
      boolean onlyActive, String[] orderBy) throws Exception;
  
  public Vector<ComuneVO> getListComuniParticelleByIdDichiarazioneConsistenza(Long idDichiarazioneConsistenza, String[] orderBy)
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

  public String getMaxDataEsecuzioneControlliConduzioneParticella(Long idAzienda)
      throws Exception;

  public String getMaxDataEsecuzioneControlliConduzioneDichiarata(Long idAzienda)
      throws Exception;

  public Vector<StoricoParticellaVO> searchListParticelleByParameters(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda)
      throws Exception;

  public StoricoParticellaVO findStoricoParticellaVOByIdConduzioneParticella(
      Long idConduzioneParticella) throws Exception;
  
  public Long findIdUteByIdCondPartIdAz(Long idConduzioneParticella, Long idAzienda) throws Exception;

  public StoricoParticellaVO findStoricoParticellaVOByIdConduzioneDichiarata(
      Long idConduzioneDichiarata) throws Exception;

  public long getIdConduzioneDichiarata(long idConduzioneParticella,
      long idDichiarazioneConsistenza) throws Exception;

  public ConduzioneParticellaVO findConduzioneParticellaByPrimaryKey(
      Long idConduzioneParticella) throws Exception;

  public ConduzioneDichiarataVO findConduzioneDichiarataByPrimaryKey(
      Long idConduzioneDichiarata) throws Exception;

  public StoricoParticellaVO getDettaglioParticella(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idConduzione)
      throws Exception;

  public Vector<TipoPiantaConsociataVO> getListPianteConsociate(
      boolean onlyActive) throws Exception;

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

  public TipoImpiantoVO findTipoImpiantoByPrimaryKey(Long idImpianto)
      throws Exception;

  public Vector<UtilizzoConsociatoVO> getListUtilizziConsociatiByIdUtilizzoParticella(
      Long idUtilizzoParticella, String[] orderBy) throws Exception;

  public Vector<TipoUtilizzoVO> findListTipiUsoSuoloByIdAzienda(Long idAzienda,
      String colturaSecondaria)  throws Exception;

  public Vector<TipoUtilizzoVO> findListTipiUsoSuoloByIdAziendaCess(
      Long idAzienda, String[] orderBy) throws Exception;

  public void modificaParticelle(StoricoParticellaVO[] elencoStoricoParticella,
      RuoloUtenza ruoloUtenza, AnagAziendaVO anagAziendaVO)
      throws Exception;

  public Vector<AnagParticellaExcelVO> searchParticelleExcelByParameters(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda)
      throws Exception;

  public StoricoParticellaVO[] getListParticelleForDocument(
      StoricoParticellaVO storicoParticellaVO, AnagAziendaVO anagAziendaVO,
      boolean hasUnitToDocument, Long idAnomalia, String orderBy) throws Exception;

  public StoricoParticellaVO[] getListParticelleForDocument(
      StoricoParticellaVO storicoParticellaVO, AnagAziendaVO anagAziendaVO,
      long idDichiarazioneConsistenza, boolean hasUnitToDocument, Long idAnomalia, String orderBy)
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
      String nomeLib, Long idAzienda, FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO) throws Exception;

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
  
  public TipoVarietaVO[] getListTipoVarietaByIdAzienda(
      long idAzienda)
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
    //  throws Exception;
  
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

  public StoricoParticellaVO[] riepilogoComune(Long idAzienda,
      String escludiAsservimento)
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

  public UtilizzoParticellaVO[] riepilogoMacroUso(Long idAzienda)
      throws Exception;

  public UtilizzoDichiaratoVO[] riepilogoMacroUsoDichiarato(Long idAzienda,
      Long idDichiarazioneConsistenza) throws Exception;
  
  public BigDecimal getTotSupAsservimento(Long idAzienda, Long idDichiarazioneConsistenza)
    throws Exception;

  public TipoMacroUsoVO[] getListTipoMacroUsoByIdAzienda(Long idAzienda,
      boolean onlyActive, String[] orderBy) throws Exception;

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

  public ParticellaDettaglioValidazioniVO[] getParticellaDettaglioValidazioni(
      long idParticella, Long anno, int tipoOrdinamento,
      boolean ordineAscendente[]) throws Exception;

  public boolean areParticelleNonCessateByIdParticelle(long idParticelle[])
      throws Exception;

  public StoricoParticellaVO findStoricoParticellaVOByIdParticella(
      long idParticella) throws Exception;

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

  public void aggiornaIrrigazione(
      StoricoParticellaVO[] elencoStoricoParticella, RuoloUtenza ruoloUtenza,
      boolean flagIrrigabile, Long idIrrigazione) throws Exception;

  public ParticellaAssVO[] getParticellaForDocAzCessata(Long idParticella)
      throws Exception;

  public Vector<ParticellaAssVO> getParticelleDocCor(Long idDocumento)
      throws Exception;

  public String getDescrizioneByIdTipologiaVino(Long idTipologiaVino)
      throws Exception;

  public String getVarietaByIdVitigno(Long idVitigno) throws Exception;
  
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
}
