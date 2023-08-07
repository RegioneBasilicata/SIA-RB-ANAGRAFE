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
import it.csi.smranag.smrgaa.integration.StampaGaaDAO;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.ContoCorrenteVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.FabbricatoVO;
import it.csi.solmr.dto.anag.ParticellaVO;
import it.csi.solmr.dto.anag.TipoFormaConduzioneVO;
import it.csi.solmr.dto.anag.attestazioni.ParametriAttAziendaVO;
import it.csi.solmr.dto.anag.attestazioni.ParametriAttDichiarataVO;
import it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneParticellaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.integration.anag.stampe.StampeDAO;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

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

@Stateless(name="comp/env/solmr/anag/StampaGaa",mappedName="comp/env/solmr/anag/StampaGaa")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(value=javax.ejb.TransactionAttributeType.REQUIRED)
public class StampaGaaBean implements StampaGaaLocal
{

  

  /**
   * 
   */
  private static final long serialVersionUID = 6493443773126776429L;

  SessionContext                   sessionContext;

  private transient StampaGaaDAO sGaaDAO;
  private transient StampeDAO sDAO;

  private void initializeDAO() throws EJBException
  {
    try
    {
      sGaaDAO = new StampaGaaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      sDAO = new StampeDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
    }
    catch (ResourceAccessException ex)
    {
      SolmrLogger.fatal(this, ex.getMessage());
      throw new EJBException(ex);
    }
  }

 
  @Resource
  public void setSessionContext(SessionContext sessionContext)
  {
    this.sessionContext = sessionContext;
    initializeDAO();
  }

  public RichiestaTipoReportVO[] getElencoSubReportRichiesta(
      String codiceReport, java.util.Date dataRiferimento) throws SolmrException
  {    
    try
    {
      return sGaaDAO.getElencoSubReportRichiesta(codiceReport, dataRiferimento);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public AnagAziendaVO getAnagraficaAzienda(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException
  {    
    try
    {
      return sGaaDAO.getAnagraficaAzienda(idAzienda, dataRiferimento);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<ParticellaVO> getParticelleUtilizzoIstanzaRiesame(long idDocumento) 
    throws SolmrException
  {    
    try
    {
      return sGaaDAO.getParticelleUtilizzoIstanzaRiesame(idDocumento);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public BigDecimal getSumSupCatastaleTerreniIstanzaRiesame(long idDocumento) 
   throws SolmrException
  {    
    try
    {
      return sGaaDAO.getSumSupCatastaleTerreniIstanzaRiesame(idDocumento);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public BigDecimal[] getSumSupAgronomicaAndCondottaTerreniIstanzaRiesame(long idDocumento) 
    throws SolmrException
  {    
    try
    {
      return sGaaDAO.getSumSupAgronomicaAndCondottaTerreniIstanzaRiesame(idDocumento);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  
  public Vector<TipoReportVO> getElencoTipoReport(
      String codiceTipoReport, java.util.Date dataRiferimento) throws SolmrException
  {    
    try
    {
      return sGaaDAO.getElencoTipoReport(codiceTipoReport, dataRiferimento);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<TipoReportVO> getElencoTipoReportByValidazione(
      String codiceTipoReport, Long idDichiarazioneConsistenza) 
    throws SolmrException
  {    
    try
    {
      return sGaaDAO.getElencoTipoReportByValidazione(codiceTipoReport, idDichiarazioneConsistenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  public DichiarazioneConsistenzaGaaVO getDatiDichiarazioneConsistenza(
      long idDichiarazioneConsistenza) throws SolmrException
  {    
    try
    {
      return sGaaDAO.getDatiDichiarazioneConsistenza(idDichiarazioneConsistenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public LogoVO getLogo(String istatRegione, String provincia) 
    throws SolmrException
  {   
    try
    {
      LogoVO logoVO = new LogoVO();
      
      logoVO.setLogoProvincia(sDAO
          .getLogoProvincia(provincia));
      logoVO.setLogoRegione(sDAO
          .getLogoRegione(istatRegione));
      
      return logoVO;
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  
  public InfoFascicoloVO getInfoFascicolo(
      long idAzienda, java.util.Date dataRiferimento, Long codFotografia) throws SolmrException
  {    
    try
    {
      return sGaaDAO.getInfoFascicolo(idAzienda, dataRiferimento, codFotografia);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  
  public ValoriCondizionalitaVO getValoriCondizionalita(Long idAzienda, Long codiceFotografia) 
    throws SolmrException
  {    
    try
    {
      ValoriCondizionalitaVO valVO = new ValoriCondizionalitaVO();
      valVO.setTotSupCondotta(sDAO.getTotSupCond(idAzienda, codiceFotografia));
      valVO.setSic(sDAO.getSIC(idAzienda, codiceFotografia));
      valVO.setZps(sDAO.getZPS(idAzienda, codiceFotografia));
      valVO.setZvn(sDAO.getZVN(idAzienda, codiceFotografia));
      valVO.setFasceFluviali(sDAO.getFasceFluviali(idAzienda, codiceFotografia));      
      
      return valVO;
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  
  public TipoAttestazioneVO[] getListAttestazioniAlPianoAttuale(
      Long idAzienda, Long idAllegato) throws SolmrException
  {    
    try
    {
      TipoAttestazioneVO[] elencoAttestazioni =  sGaaDAO
        .getListAttestazioniAlPianoAttuale(idAzienda, idAllegato);
      
      for(int i = 0; i < elencoAttestazioni.length; i++) {
        TipoAttestazioneVO tipoAttestazioneVO = elencoAttestazioni[i];
        ParametriAttAziendaVO parametriAttAziendaVO = tipoAttestazioneVO.getParametriAttAziendaVO();
        StringUtils.parsificaDescrizioneAttestazioni(parametriAttAziendaVO, tipoAttestazioneVO);
        elencoAttestazioni[i] = tipoAttestazioneVO;
      }
      return elencoAttestazioni;
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public TipoAttestazioneVO[] getListAttestazioneAllaDichiarazione(
      Long codiceFotografiaTerreni, java.util.Date dataAnnoCampagna, Long idAllegato) 
    throws SolmrException
  {    
    try
    {
      TipoAttestazioneVO[] elencoAttestazioni =  sGaaDAO
        .getListAttestazioneAllaDichiarazione(
            codiceFotografiaTerreni, dataAnnoCampagna, idAllegato);
      
      for(int i = 0; i < elencoAttestazioni.length; i++) {
        TipoAttestazioneVO tipoAttestazioneVO = elencoAttestazioni[i];
        ParametriAttDichiarataVO parametriAttDichiarataVO = tipoAttestazioneVO.getParametriAttDichiarataVO();
        StringUtils.parsificaDescrizioneAttestazioniDichiarate(parametriAttDichiarataVO, tipoAttestazioneVO);
        elencoAttestazioni[i] = tipoAttestazioneVO;
      }
      
      return elencoAttestazioni;
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  
  public Vector<ContoCorrenteVO> getStampaContiCorrenti(
        Long idAzienda, java.util.Date dataInserimentoDichiarazione)
      throws SolmrException
  {
    try
    {
      return sGaaDAO.getStampaContiCorrenti(idAzienda, dataInserimentoDichiarazione);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  
  public Vector<TipoReportVO> getListTipiDocumentoStampaProtocollo(long idDocumento[]) throws SolmrException
  {
    try
    {
      return sGaaDAO.getListTipiDocumentoStampaProtocollo(idDocumento);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public String getTipoDocumentoStampaProtocollo(long idTipoReport) throws SolmrException
  {
    try
    {
      return sGaaDAO.getTipoDocumentoStampaProtocollo(idTipoReport);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public int getCountDocumentoCompatibile(long idTipoReport, long[] idDocumento) 
      throws SolmrException
  {
    try
    {
      return sGaaDAO.getCountDocumentoCompatibile(idTipoReport, idDocumento);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<CodeDescription> getElencoPropietariDocumento(long[] idDocumento)
      throws SolmrException
  {
    try
    {
      return sGaaDAO.getElencoPropietariDocumento(idDocumento);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public TipoReportVO getTipoReport(long idTipoReport)
      throws SolmrException
  {
    try
    {
      return sGaaDAO.getTipoReport(idTipoReport);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public TipoReportVO getTipoReportByCodice(String codiceReport)
      throws SolmrException
  {
    try
    {
      return sGaaDAO.getTipoReportByCodice(codiceReport);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoAttestazioneVO> getAttestStampaProtoc(long idReportSubReport)
      throws SolmrException
  {
    try
    {
      return sGaaDAO.getAttestStampaProtoc(idReportSubReport);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public boolean isNumeroProtocolloValido(String numeroProtocollo)
      throws SolmrException
  {
    try
    {
      return sGaaDAO.isNumeroProtocolloValido(numeroProtocollo);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<ParticellaVO> getElencoParticelleVarCat(long idAzienda, long idDichiarazioneConsistenza)
      throws SolmrException
  {
    try
    {
      return sGaaDAO.getElencoParticelleVarCat(idAzienda, idDichiarazioneConsistenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  
  public Vector<RiepiloghiUnitaArboreaVO> getStampaUVRiepilogoIdoneita(Long idAzienda, 
      Long idDichiarazioneConsistenza) throws SolmrException
  {
    try
    {
      if(idDichiarazioneConsistenza != null)
        return sGaaDAO.getStampaUVRiepilogoIdoneitaAllaDichiarazione(idDichiarazioneConsistenza);
      else
        return sGaaDAO.getStampaUVRiepilogoIdoneita(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoFormaConduzioneVO> getFormaConduzioneSezioneAnagrafica(Long idAzienda,
      Date dataRiferimento) throws SolmrException
  {
    try
    {
      return sGaaDAO.getFormaConduzioneSezioneAnagrafica(idAzienda, dataRiferimento);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoInfoAggiuntivaVO> getTipoInfoAggiuntiveStampa(long idAzienda, 
      Date dataInserimentoDichiarazione) 
    throws SolmrException
  {
    try
    {
      return sGaaDAO.getTipoInfoAggiuntiveStampa(idAzienda, dataInserimentoDichiarazione);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<AnagAziendaVO> getAziendeAssociateStampa(long idAzienda, 
      Date dataInserimentoDichiarazione) 
    throws SolmrException
  {
    try
    {
      return sGaaDAO.getAziendeAssociateStampa(idAzienda, dataInserimentoDichiarazione);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<ConduzioneParticellaVO> getRiepilogoConduzioneStampa(long idAzienda, 
      Long codFotografia) throws SolmrException
  {
    try
    {
      return sGaaDAO.getRiepilogoConduzioneStampa(idAzienda, codFotografia);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<StoricoParticellaVO> getRiepilogoComuneStampa(long idAzienda, 
      Long codFotografia) throws SolmrException
  {
    try
    {
      return sGaaDAO.getRiepilogoComuneStampa(idAzienda, codFotografia);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<RiepiloghiUnitaArboreaVO> getStampaUVRiepilogoVitignoIdoneita(Long idAzienda, 
      Long idDichiarazioneConsistenza) throws SolmrException
  {
    try
    {
      if(idDichiarazioneConsistenza != null)
        return sGaaDAO.getStampaUVRiepilogoVitignoIdoneitaAllaDich(idDichiarazioneConsistenza);
      else
        return sGaaDAO.getStampaUVRiepilogoVitignoIdoneita(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<FabbricatoVO> getStampaFabbricati(
      Long idAzienda, Date dataRiferimento)  throws SolmrException
  {
    try
    {
      return sGaaDAO.getStampaFabbricati(idAzienda, dataRiferimento);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<DocumentoVO> getDocumentiStampaMd(Long idAzienda, Long idDichiarazioneConsistenza,
      String cuaa, java.util.Date dataInserimentoDichiarazione) throws SolmrException
  {
    try
    {
      if(idDichiarazioneConsistenza != null)
        return sGaaDAO.getStampaDocumentiAllaDichMd(idAzienda, dataInserimentoDichiarazione);
      else
        return sGaaDAO.getStampaDocumentiMd(idAzienda, cuaa);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<RiepilogoStampaTerrVO> getStampaRiepiloghiStoricoPartTipoArea(
      String tipoArea, Long idAzienda, Long codFotografia) throws SolmrException
  {
    try
    {
      return sGaaDAO.getStampaRiepiloghiStoricoPartTipoArea(tipoArea, idAzienda, codFotografia);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<RiepilogoStampaTerrVO> getStampaRiepiloghiFoglioTipoArea(
      String tipoArea, Long idAzienda, Long codFotografia)  throws SolmrException
  {
    try
    {
      return sGaaDAO.getStampaRiepiloghiFoglioTipoArea(tipoArea, idAzienda, codFotografia);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoAttestazioneVO> getListAttestazioniDichiarazioniAlPianoAttuale(
      Long idAzienda, String voceMenu, boolean flagCondizionalita)  throws SolmrException
  {
    try
    {
      return sGaaDAO.getListAttestazioniDichiarazioniAlPianoAttuale(idAzienda, voceMenu, flagCondizionalita);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoAttestazioneVO> getListAttestazioniDichiarazioniAllaValidazione(
      Long codiceFotografiaTerreni, Date dataInserimentoDichiarazione, String voceMenu, boolean flagCondizionalita)  
    throws SolmrException
  {
    try
    {
      return sGaaDAO.getListAttestazioniDichiarazioniAllaValidazione(
          codiceFotografiaTerreni, dataInserimentoDichiarazione, voceMenu, flagCondizionalita);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoAttestazioneVO> getElencoAllegatiAllaValidazionePerStampa(
      Long codiceFotografiaTerreni, Date dataInserimentoDichiarazione, String codiceAttestazione)
    throws SolmrException
  {
    try
    {
      return sGaaDAO.getElencoAllegatiAllaValidazionePerStampa(codiceFotografiaTerreni, dataInserimentoDichiarazione,
          codiceAttestazione);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoAttestazioneVO> getAttestazioniFromSubReport(long idReportSubReport, 
      Date dataInserimentoDichiarazione) throws SolmrException
  {
    try
    {
      return sGaaDAO.getAttestazioniFromSubReport(idReportSubReport, 
          dataInserimentoDichiarazione);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoAttestazioneVO> getAttestazioniFromSubReportValidDettagli(long idReportSubReport, 
      Date dataInserimentoDichiarazione, Long codiceFotografiaTerreni) throws SolmrException
  {
    try
    {
      return sGaaDAO.getAttestazioniFromSubReportValidDettagli(idReportSubReport, 
          dataInserimentoDichiarazione, codiceFotografiaTerreni);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public String getTipoReportByValidazioneEAllegato(
      long idDichiarazioneConsistenza, int idTipoAllegato)
          throws SolmrException
  {
    try
    {
      return sGaaDAO.getTipoReportByValidazioneEAllegato(idDichiarazioneConsistenza,
          idTipoAllegato);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public TipoAllegatoVO getTipoAllegatoById(int idTipoAllegato)
      throws SolmrException
  {
    try
    {
      return sGaaDAO.getTipoAllegatoById(idTipoAllegato);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoAllegatoVO> getTipoAllegatoForValidazione(int idMotivoDichiarazione,
      Date dataInserimentoDichiarazione) 
      throws SolmrException
  {
    try
    {
      return sGaaDAO.getTipoAllegatoForValidazione(idMotivoDichiarazione, dataInserimentoDichiarazione);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoAllegatoVO> getTipoAllegatoObbligatorioForValidazione(int idMotivoDichiarazione) 
      throws SolmrException
  {
    try
    {
      return sGaaDAO.getTipoAllegatoObbligatorioForValidazione(idMotivoDichiarazione);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public TipoFirmatarioVO getTipoFirmatarioById(int idTipoFirmatario)
      throws SolmrException
  {
    try
    {
      return sGaaDAO.getTipoFirmatarioById(idTipoFirmatario);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<RichiestaTipoReportVO> getElencoCoordinateFirma(int idTipoAllegato)
    throws SolmrException
  {
    try
    {
      return sGaaDAO.getElencoCoordinateFirma(idTipoAllegato);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public boolean isInseribileAllegatoAuto(String query, long valore)
      throws SolmrException
  {
    try
    {
      return sGaaDAO.isInseribileAllegatoAuto(query, valore);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  
  
}
