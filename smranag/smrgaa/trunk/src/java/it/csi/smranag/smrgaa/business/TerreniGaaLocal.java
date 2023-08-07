package it.csi.smranag.smrgaa.business;

import it.csi.smranag.smrgaa.dto.PlSqlCodeDescription;
import it.csi.smranag.smrgaa.dto.terreni.CasoParticolareVO;
import it.csi.smranag.smrgaa.dto.terreni.CatalogoMatriceSeminaVO;
import it.csi.smranag.smrgaa.dto.terreni.CatalogoMatriceVO;
import it.csi.smranag.smrgaa.dto.terreni.CompensazioneAziendaVO;
import it.csi.smranag.smrgaa.dto.terreni.ConduzioneEleggibilitaVO;
import it.csi.smranag.smrgaa.dto.terreni.DirittoCompensazioneVO;
import it.csi.smranag.smrgaa.dto.terreni.IsolaParcellaVO;
import it.csi.smranag.smrgaa.dto.terreni.IstanzaRiesameVO;
import it.csi.smranag.smrgaa.dto.terreni.ParticellaBioVO;
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
import it.csi.solmr.dto.ProvinciaVO;
import it.csi.solmr.dto.anag.AnagParticellaExcelVO;
import it.csi.solmr.dto.anag.ConsistenzaVO;
import it.csi.solmr.dto.anag.terreni.FiltriParticellareRicercaVO;
import it.csi.solmr.dto.anag.terreni.FiltriUnitaArboreaRicercaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaArboreaExcelVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.dto.anag.terreni.TipoUtilizzoVO;
import it.csi.solmr.dto.anag.terreni.TipoVarietaVO;
import it.csi.solmr.dto.anag.terreni.UtilizzoParticellaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.exception.SolmrException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
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
public interface TerreniGaaLocal 
{
  public PlSqlCodeDescription allineaSupEleggibilePlSql(long idAzienda, 
      long idUtenteAggiornamento)  throws SolmrException;
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaComune(long idAzienda)  
      throws SolmrException;
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaComuneDichiarato(long idDichiarazioneConsistenza)  
      throws SolmrException;
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaUvaDaVino(long idAzienda)  
      throws SolmrException;
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaUvaDaVinoDichiarato(long idDichiarazioneConsistenza)  
      throws SolmrException;
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaVinoDOP(long idAzienda)  
      throws SolmrException;
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaVinoDOPDichiarato(long idDichiarazioneConsistenza)  
      throws SolmrException;
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoProvinciaVinoDOP(long idAzienda)  
      throws SolmrException;
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoProvinciaVinoDOPDichiarato(
      long idDichiarazioneConsistenza) throws SolmrException;
  
  public TreeMap<String,Vector<RiepiloghiUnitaArboreaVO>> riepilogoElencoSociProvinciaVinoDOP(long idAzienda) 
      throws SolmrException;
  
  public Vector<Long> esisteUVValidataByConduzioneAndAzienda(Vector<Long> elencoConduzioni, Long idAzienda)
      throws SolmrException;
  
  public Vector<Long> esisteUVByConduzioneAndAzienda(Vector<Long> elencoConduzioni, Long idAzienda)
      throws SolmrException;
  
  public Vector<Long> esisteUVModProcVITIByConduzioneAndAzienda(Vector<Long> elencoConduzioni, Long idAzienda)
      throws SolmrException;
  
  public Vector<StoricoParticellaArboreaExcelVO> searchStoricoUnitaArboreaExcelSempliceByParameters( 
      Long idAzienda, FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO)
    throws SolmrException;
  
  public Vector<StoricoParticellaVO> getListUVForInserimento(Long idParticellaCurr, Vector<Long> vIdParticella, Long idAzienda)
    throws SolmrException;
  
  public BigDecimal getSupEleggibilePlSql(long idParticellaCertificata, long idCatalogoMatrice)
      throws SolmrException;
  
  public PlSqlCodeDescription compensazioneAziendalePlSql(long idAzienda, 
      long idUtenteAggiornamento)  throws SolmrException;
  
  public Vector<UVCompensazioneVO> getUVPerCompensazione(long idAzienda)
      throws SolmrException;
  
  public CompensazioneAziendaVO getCompensazioneAzienda(long idAzienda)
      throws SolmrException;
  
  public Vector<RiepilogoCompensazioneVO> getRiepilogoPostAllinea(long idAzienda)
      throws SolmrException;
  
  public Vector<RiepilogoCompensazioneVO> getRiepilogoDirittiVitati(long idAzienda)
      throws SolmrException;
  
  public CompensazioneAziendaVO getCompensazioneAziendaByIdAzienda(long idAzienda)
      throws SolmrException;
  
  public int countUVAllineabiliGis(long idAzienda)
      throws SolmrException;
  
  public int countUVIstRiesameCompen(long idAzienda)
      throws SolmrException;
  
  public int countSupUVIIrregolari(long idAzienda)
      throws SolmrException;
  
  public Date getMaxDataAggiornamentoConduzioniAndUV(long idAzienda)
      throws SolmrException;
  
  public Date getMaxDataFotoInterpretazioneUV(long idAzienda)
      throws SolmrException;
  
  public boolean existsIsoleParcDopoVarSched(long idAzienda, long isolaDichiarata)
      throws SolmrException;
  
  public BigDecimal getSupNonCompensabile(long idAzienda)
      throws SolmrException;
  
  public int countPercPossessoCompensazioneMag100(long idAzienda)
      throws SolmrException;
  
  public BigDecimal getPercUtilizzoEleggibile(long idAzienda, long idParticella)
      throws SolmrException;
  
  public Vector<Vector<DirittoCompensazioneVO>> getDirittiCalcolati(long idAzienda)
      throws SolmrException;
  
  public BigDecimal getSumAreaGiaAssegnata(long idUnitaArborea)
      throws SolmrException;
  
  public BigDecimal getSumAreaMaxAssegnabile(long idUnitaArborea)
      throws SolmrException;
  
  
  public void allineaUVaCompensazione(long idAzienda, RuoloUtenza ruoloUtenza)
      throws SolmrException;
  
  //public void allineaUVinTolleranzaGIS(long idAzienda, RuoloUtenza ruoloUtenza)
    //  throws SolmrException;
  
  public ParticellaBioVO getParticellaBio(long idParticella, long idAzienda, Date dataInserimentoDichiarazione)
      throws SolmrException;
  
  public BigDecimal getSumUtilizziPrimariNoIndicati(long idAzienda, long idParticella, Vector<String> vIdUtilizzo)
    throws SolmrException;
  
  public BigDecimal getSumUtilizziPrimariParticellaAltreConduzioni(long idAzienda, long idParticella,
      Vector<Long> vIdConduzioneParticella) throws SolmrException;
  
  public BigDecimal getSumPercentualPossessoAltreConduzioni(long idAzienda, long idParticella,
      Vector<Long> vIdConduzioneParticella) throws SolmrException;
  
  public BigDecimal getSumSupCondottaAltreConduzioni(long idAzienda, long idParticella,
      Vector<Long> vIdConduzioneParticella) throws SolmrException;
  
  public Vector<IstanzaRiesameVO> getFasiIstanzaRiesame(long idAzienda, long idParticella,
      Date dataInserimentoDichiarazione) throws SolmrException;
  
  public int calcolaP30PlSql(long idStoricoParticella, Date dataInizioValidita)
      throws SolmrException;
  
  public int calcolaP25PlSql(long idStoricoParticella, Date dataInizioValidita)
      throws SolmrException;
  
  public int calcolaP26PlSql(long idAzienda, long idParticella, Long idParticellaCertificata)
      throws SolmrException;
  
  public PlSqlCodeDescription inserisciIstanzaPlSql(long idAzienda, int anno) 
      throws SolmrException;
  
  public Vector<CasoParticolareVO> getCasiParticolari(String particellaObbligatoria)
      throws SolmrException;
  
  public Vector<TipoRiepilogoVO> getTipoRiepilogo(String funzionalita, String codiceRuolo)
      throws SolmrException;
  
  public boolean isAltreUvDaSchedario(long idParticella)
      throws SolmrException;
  
  public Vector<TipoEsportazioneDatiVO> getTipoEsportazioneDati(String codMenu, String codiceRuolo)
      throws SolmrException;
  
  public boolean isParticellAttivaStoricoParticella(String istatComune, String sezione,
      String foglio, String particella, String subalterno) 
    throws SolmrException;
  
  public Vector<StoricoParticellaVO> getElencoParticelleForPopNotifica(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO)
    throws SolmrException;
  
  public StoricoParticellaVO findStoricoParticellaDichCompleto(long idStoricoParticella, 
      long idDichiarazioneConsistenza)
    throws SolmrException;
  
  public Vector<TipoEfaVO> getListTipoEfa()  throws SolmrException;
  
  public Vector<TipoEfaVO> getListLegendaTipoEfa()
      throws SolmrException;
  
  public TipoEfaVO getTipoEfaFromIdCatalogoMatrice(long idCatalogoMatrice)
      throws SolmrException;
  
  public Vector<TipoPeriodoSeminaVO> getListTipoPeriodoSemina() 
      throws SolmrException;
  
  public Vector<TipoUtilizzoVO> getListTipoUtilizzoEfa(long idTipoEfa)
      throws SolmrException;
   
  public Vector<TipoVarietaVO> getListTipoVarietaEfaByMatrice(long idTipoEfa, 
      long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso, long idTipoQualitaUso)
     throws SolmrException;
  
  public Vector<TipoDestinazioneVO> getListTipoDestinazioneEfa(long idTipoEfa, long idUtilizzo)
      throws SolmrException;
   
  public Vector<TipoDettaglioUsoVO> getListDettaglioUsoEfaByMatrice(long idTipoEfa,
      long idUtilizzo, long idTipoDestinazione) throws SolmrException;
  
  public Vector<TipoQualitaUsoVO> getListQualitaUsoEfaByMatrice(long idTipoEfa,
      long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso) 
          throws SolmrException;
  
  public Integer getAbbPonderazioneByMatrice(
      long idTipoEfa, long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso,
      long idTipoQualitaUso, long idVarieta)
    throws SolmrException;
  
  public String getValoreAttivoTipoAreaFromParticellaAndId(
      long idParticella, long idTipoArea)
          throws SolmrException;
  
  public Vector<TipoAreaVO> getValoriTipoAreaParticella(long idParticella, Date dataInserimentoValidazione)
      throws SolmrException;
  
  public Vector<TipoAreaVO> getAllValoriTipoArea()
      throws SolmrException;
  
  public Vector<TipoAreaVO> getDescTipoAreaBrogliaccio(Long idDichiarazioneConsistenza)
      throws SolmrException;
  
  public Vector<TipoAreaVO> getValoriTipoAreaFoglio(String comune, String foglio, String sezione)
      throws SolmrException;
  
  public Vector<TipoMetodoIrriguoVO> getElencoMetodoIrriguo()
      throws SolmrException;
  
  public Vector<TipoPeriodoSeminaVO> getListTipoPeriodoSeminaByCatalogo(long idUtilizzo,
      long idDestinazione, long idDettUso, long idQualiUso, long idVarieta)
      throws SolmrException;
  
  public Vector<TipoFonteVO> getElencoAllTipoFonte() throws SolmrException;
  
  public boolean isUtilizzoAttivoSuMatrice(long idUtilizzo) throws SolmrException;
  
  public Vector<TipoAreaVO> riepilogoTipoArea(long idAzienda)
      throws SolmrException;
  
  public Vector<TipoAreaVO> riepilogoTipoAreaDichiarato(long idDichiarazioneConsistenza)
      throws SolmrException;
  
  public Vector<TipoAreaVO> getValoriTipoAreaFiltroElenco(Long idDichiarazioneConsistenza)
      throws SolmrException;
  
  public boolean isDettaglioUsoObbligatorio(long idTipoEfa,
      long idVarieta) throws SolmrException;
  
  public TipoEfaVO getTipoEfaFromPrimaryKey(long idTipoEfa)
      throws SolmrException;
  
  public Vector<Long> getIdParticellaByIdConduzione(Vector<Long> vIdConduzioneParticella)
      throws SolmrException;
  
  public Vector<ProvinciaVO> getListProvincieParticelleByIdAzienda(Long idAzienda)
      throws SolmrException;
  
  public Vector<ProvinciaVO> getListProvincieParticelleByIdDichiarazioneConsistenza(Long idDichiarazioneConsistenza)
      throws SolmrException;
  
  public Vector<IsolaParcellaVO> getElencoIsoleParcelle(String nomeLib, long idAzienda)
    throws SolmrException;
  
  public Vector<StoricoParticellaVO> associaParcelleGisLettura(String nomeLib, long idAzienda,long idUtente)
    throws SolmrException;
  
  public void associaParcelleGisConferma(long idAzienda, long idUtente, Vector<Long> vIdUnarParcellaSel)
    throws SolmrException;
  
  public ConsistenzaVO getDichConsUVParcelleDoppie(long idAzienda)
    throws SolmrException;
  
  public void allineaUVaGIS(Vector<Long> vIdIsolaParcella, long idAzienda, long idDichiarazioneConsistenza, 
      RuoloUtenza ruoloUtenza)
    throws SolmrException;
  
  //public Integer getTolleranzaPlSql(String nomeLib, long idAzienda, long idUnitaArborea)
    //throws SolmrException;
  
  public Vector<RegistroPascoloVO> getRegistroPascoliPianoLavorazione(
      long idParticellaCertificata)  throws SolmrException;
  
  public Vector<RegistroPascoloVO> getRegistroPascoliPianoLavorazioneChiaveCatastale(
      String istatComune, String foglio, String particella, String sezione, String subalterno) 
    throws SolmrException;
  
  public Vector<RegistroPascoloVO> getRegistroPascoliDichCons(
      long idParticellaCertificata, int annoDichiarazione) throws SolmrException;
  
  public Vector<RegistroPascoloVO> getRegistroPascoliDichConsChiaveCatastale(
      String istatComune, String foglio, String particella, String sezione, String subalterno, int annoDichiarazione)
    throws SolmrException;
  
  public boolean isRegistroPascoliPratoPolifita(
      long idParticellaCertificata) throws SolmrException;
  
  public Vector<Long> getIdConduzioneFromIdAziendaIdParticella(long idAzienda, long idParticella)
      throws SolmrException;
  
  public Vector<AnagParticellaExcelVO> searchParticelleStabGisExcelByParameters(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda)
      throws SolmrException;
  
  public Vector<AnagParticellaExcelVO> searchParticelleAvvicExcelByParameters(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda)
      throws SolmrException;
  
  /*public HashMap<Long,HashMap<Integer,AvvicendamentoVO>> getElencoAvvicendamento( 
      Vector<Long> vIdParticella, long idAzienda, Integer annoPartenza, int numeroAnni, 
      Long idDichiarazioneConsistenza) 
    throws SolmrException;*/
  
  public Vector<Long> getIdUtilizzoFromIdIdConduzione(long idConduzioneParticella) 
      throws SolmrException;
  
  public BigDecimal getSumSupUtilizzoParticellaAzienda(long idAzienda, long idParticella) 
      throws SolmrException;
  
  public Vector<UtilizzoParticellaVO> riepilogoTipoEfaPianoLavorazione(long idAzienda) 
      throws SolmrException;
  
  public Vector<UtilizzoParticellaVO> riepilogoTipoEfaDichiarazione(long idDichiarazioneConsistenza)  
      throws SolmrException;
  
  public Vector<UtilizzoParticellaVO> riepilogoTipoGreeningPianoLavorazione(
      long idAzienda) throws SolmrException;
  
  public Vector<UtilizzoParticellaVO> riepilogoTipoGreeningDichiarazione(long idDichiarazioneConsistenza) 
      throws SolmrException;
  
  public Vector<TipoEfaVO> getElencoTipoEfaForAzienda(long idAzienda)
      throws SolmrException;
  
  public Vector<TipoDestinazioneVO> getElencoTipoDestinazioneByMatrice(long idUtilizzo)
      throws SolmrException;
  
  public Vector<TipoVarietaVO> getElencoTipoVarietaByMatrice(
      long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso, long idTipoQualitaUso)
    throws SolmrException;
  
  public Vector<TipoQualitaUsoVO> getElencoTipoQualitaUsoByMatrice(
      long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso) 
      throws SolmrException;
  
  public CatalogoMatriceVO getCatalogoMatriceFromMatrice(long idUtilizzo, long idVarieta,
      long idTipoDestinazione, long idTipoDettaglioUso, long idTipoQualitaUso) 
          throws SolmrException;
  
  public CatalogoMatriceVO getCatalogoMatriceFromPrimariKey(long idCatalogoMatrice) 
      throws SolmrException;
  
  public CatalogoMatriceSeminaVO getCatalogoMatriceSeminaDefault(long idCatalogoMatrice)
      throws SolmrException;
  
  public CatalogoMatriceSeminaVO getCatalogoMatriceSeminaByIdTipoPeriodo(long idCatalogoMatrice, 
      long idTipoPeriodoSemina) throws SolmrException;
  
  public Vector<Long> getListIdPraticaMantenimentoPlSql(
      long idCatalogoMatrice, String flagDefault) throws SolmrException;
  
  public Vector<TipoSeminaVO> getElencoTipoSemina() throws SolmrException;
  
  public Vector<TipoPraticaMantenimentoVO> getElencoPraticaMantenimento(
      Vector<Long> vIdMantenimento) throws SolmrException;
  
  public Vector<TipoFaseAllevamentoVO> getElencoFaseAllevamento() throws SolmrException;
  
  public String cambiaPercentualePossessoMassivo(RuoloUtenza ruoloUtenza, 
      long idAzienda, BigDecimal percentualePossesso) throws SolmrException;
  
  public BigDecimal getSumSupCondottaAsservimentoParticellaAzienda(long idAzienda, long idParticella) 
      throws SolmrException;
  
  public String cambiaPercentualePossessoSupUtilizzataMassivo(RuoloUtenza ruoloUtenza, Long idAzienda)
      throws SolmrException;
  
  public void avviaConsolidamento(long idAzienda, RuoloUtenza ruoloUtenza) throws SolmrException;
  
  public void modificaConduzioneEleggibileUV(HashMap<Long, ConduzioneEleggibilitaVO> hPartCondEleg)
      throws SolmrException;
  
  public void allineaPercUsoElegg(long idAzienda, Vector<Long> vIdParticella, long idUtente)
      throws SolmrException;
  
  public Vector<TipoDettaglioUsoVO> getListDettaglioUsoByMatrice(
      long idUtilizzo, long idTipoDestinazione) 
      throws SolmrException;
  
  public TipoDettaglioUsoVO findDettaglioUsoByPrimaryKey(
      long idTipoDettaglioUso) throws SolmrException;
}
