package it.csi.smranag.smrgaa.business;

import it.csi.smranag.smrgaa.dto.anagrafe.TipoInfoAggiuntivaVO;
import it.csi.smranag.smrgaa.dto.modol.RiepilogoStampaTerrVO;
import it.csi.smranag.smrgaa.dto.stampe.DichiarazioneConsistenzaGaaVO;
import it.csi.smranag.smrgaa.dto.stampe.InfoFascicoloVO;
import it.csi.smranag.smrgaa.dto.stampe.LogoVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.TipoAllegatoVO;
import it.csi.smranag.smrgaa.dto.stampe.TipoFirmatarioVO;
import it.csi.smranag.smrgaa.dto.stampe.TipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.ValoriCondizionalitaVO;
import it.csi.smranag.smrgaa.dto.terreni.RiepiloghiUnitaArboreaVO;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.ContoCorrenteVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.FabbricatoVO;
import it.csi.solmr.dto.anag.ParticellaVO;
import it.csi.solmr.dto.anag.TipoFormaConduzioneVO;
import it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneParticellaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.exception.SolmrException;

import java.math.BigDecimal;
import java.util.Date;
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
public interface StampaGaaLocal 
{
  public RichiestaTipoReportVO[] getElencoSubReportRichiesta(
      String codiceReport, java.util.Date dataRiferimento) throws SolmrException;
  
  public AnagAziendaVO getAnagraficaAzienda(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException;
  
  public Vector<ParticellaVO> getParticelleUtilizzoIstanzaRiesame(long idDocumento) 
      throws SolmrException;
  
  public BigDecimal getSumSupCatastaleTerreniIstanzaRiesame(long idDocumento) 
    throws SolmrException;
  
  public BigDecimal[] getSumSupAgronomicaAndCondottaTerreniIstanzaRiesame(long idDocumento) 
    throws SolmrException;
  
  public Vector<TipoReportVO> getElencoTipoReport(
      String codiceTipoReport, java.util.Date dataRiferimento) 
  throws SolmrException;
  
  public Vector<TipoReportVO> getElencoTipoReportByValidazione(
      String codiceTipoReport, Long idDichiarazioneConsistenza) 
    throws SolmrException;
  
  public DichiarazioneConsistenzaGaaVO getDatiDichiarazioneConsistenza(
      long idDichiarazioneConsistenza) throws SolmrException;
  
  public LogoVO getLogo(String istatRegione, String provincia) 
      throws SolmrException;
  
  public InfoFascicoloVO getInfoFascicolo(
      long idAzienda, java.util.Date dataRiferimento, Long codFotografia) throws SolmrException;
  
  public ValoriCondizionalitaVO getValoriCondizionalita(Long idAzienda, Long codiceFotografia) 
    throws SolmrException;
  
  public TipoAttestazioneVO[] getListAttestazioniAlPianoAttuale(
      Long idAzienda, Long idAllegato) throws SolmrException;
  
  public TipoAttestazioneVO[] getListAttestazioneAllaDichiarazione(
      Long codiceFotografiaTerreni, java.util.Date dataAnnoCampagna, Long idAllegato) 
    throws SolmrException;
  
  public Vector<ContoCorrenteVO> getStampaContiCorrenti(
      Long idAzienda, java.util.Date dataInserimentoDichiarazione)
    throws SolmrException;
  
  public Vector<TipoReportVO> getListTipiDocumentoStampaProtocollo(long idDocumento[]) 
    throws SolmrException;
  
  public String getTipoDocumentoStampaProtocollo(long idTipoReport) throws SolmrException;
  
  public int getCountDocumentoCompatibile(long idTipoReport, long[] idDocumento) 
      throws SolmrException;
  
  public Vector<CodeDescription> getElencoPropietariDocumento(long[] idDocumento)
      throws SolmrException;
  
  public TipoReportVO getTipoReport(long idTipoReport)
      throws SolmrException;
  
  public TipoReportVO getTipoReportByCodice(String codiceReport)
      throws SolmrException;
  
  public Vector<TipoAttestazioneVO> getAttestStampaProtoc(long idReportSubReport)
      throws SolmrException;
  
  public boolean isNumeroProtocolloValido(String numeroProtocollo)
      throws SolmrException;
  
  public Vector<ParticellaVO> getElencoParticelleVarCat(long idAzienda, long idDichiarazioneConsistenza)
      throws SolmrException;
  
  public Vector<RiepiloghiUnitaArboreaVO> getStampaUVRiepilogoIdoneita(Long idAzienda, 
      Long idDichiarazioneConsistenza) throws SolmrException;
  
  public Vector<TipoFormaConduzioneVO> getFormaConduzioneSezioneAnagrafica(Long idAzienda,
      Date dataRiferimento) throws SolmrException;
  
  public Vector<TipoInfoAggiuntivaVO> getTipoInfoAggiuntiveStampa(long idAzienda, 
      Date dataInserimentoDichiarazione) 
    throws SolmrException;
  
  public Vector<AnagAziendaVO> getAziendeAssociateStampa(long idAzienda, 
      Date dataInserimentoDichiarazione) 
    throws SolmrException;
  
  public Vector<ConduzioneParticellaVO> getRiepilogoConduzioneStampa(long idAzienda, 
      Long codFotografia) throws SolmrException;
  
  public Vector<StoricoParticellaVO> getRiepilogoComuneStampa(long idAzienda, 
      Long codFotografia) throws SolmrException;
  
  public Vector<RiepiloghiUnitaArboreaVO> getStampaUVRiepilogoVitignoIdoneita(Long idAzienda, 
      Long idDichiarazioneConsistenza) throws SolmrException;
  
  public Vector<FabbricatoVO> getStampaFabbricati(
      Long idAzienda, Date dataRiferimento)  throws SolmrException;
  
  public Vector<DocumentoVO> getDocumentiStampaMd(Long idAzienda, Long idDichiarazioneConsistenza,
      String cuaa, java.util.Date dataInserimentoDichiarazione) throws SolmrException;
  
  public Vector<RiepilogoStampaTerrVO> getStampaRiepiloghiStoricoPartTipoArea(
      String tipoArea, Long idAzienda, Long codFotografia) throws SolmrException;
  
  public Vector<RiepilogoStampaTerrVO> getStampaRiepiloghiFoglioTipoArea(
      String tipoArea, Long idAzienda, Long codFotografia)  throws SolmrException;
  
  public Vector<TipoAttestazioneVO> getListAttestazioniDichiarazioniAlPianoAttuale(
      Long idAzienda, String voceMenu, boolean flagCondizionalita)  throws SolmrException;
  
  public Vector<TipoAttestazioneVO> getListAttestazioniDichiarazioniAllaValidazione(
      Long codiceFotografiaTerreni, Date dataInserimentoDichiarazione, String voceMenu, 
      boolean flagCondizionalita)  throws SolmrException;
  
  public Vector<TipoAttestazioneVO> getElencoAllegatiAllaValidazionePerStampa(
      Long codiceFotografiaTerreni, Date dataInserimentoDichiarazione, String codiceAttestazione)
    throws SolmrException;
  
  public Vector<TipoAttestazioneVO> getAttestazioniFromSubReport(long idReportSubReport, 
      Date dataInserimentoDichiarazione) throws SolmrException;
  
  public Vector<TipoAttestazioneVO> getAttestazioniFromSubReportValidDettagli(long idReportSubReport, 
      Date dataInserimentoDichiarazione, Long codiceFotografiaTerreni) throws SolmrException;
  
  public String getTipoReportByValidazioneEAllegato(
      long idDichiarazioneConsistenza, int idTipoAllegato) throws SolmrException;
  
  public TipoAllegatoVO getTipoAllegatoById(int idTipoAllegato)
      throws SolmrException;
  
  public Vector<TipoAllegatoVO> getTipoAllegatoForValidazione(int idMotivoDichiarazione, 
      Date dataInserimentoDichiarazione) 
      throws SolmrException;
  
  public Vector<TipoAllegatoVO> getTipoAllegatoObbligatorioForValidazione(int idMotivoDichiarazione) 
      throws SolmrException;
  
  public TipoFirmatarioVO getTipoFirmatarioById(int idTipoFirmatario)
      throws SolmrException;
  
  public Vector<RichiestaTipoReportVO> getElencoCoordinateFirma(int idTipoAllegato)
      throws SolmrException;
  
  public boolean isInseribileAllegatoAuto(String query, long valore)
      throws SolmrException;
  
}
