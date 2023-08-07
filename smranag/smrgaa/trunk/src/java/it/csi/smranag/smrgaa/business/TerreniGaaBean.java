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
import it.csi.smranag.smrgaa.integration.ConduzioneParticellaGaaDAO;
import it.csi.smranag.smrgaa.integration.terreni.ConduzioneEleggibilitaGaaDAO;
import it.csi.smranag.smrgaa.integration.terreni.IsolaParcellaGaaDAO;
import it.csi.smranag.smrgaa.integration.terreni.ParticellaGaaDAO;
import it.csi.smranag.smrgaa.integration.terreni.RegistroPascoloGaaDAO;
import it.csi.smranag.smrgaa.integration.terreni.StoricoUnitaArboreaGaaDAO;
import it.csi.smranag.smrgaa.integration.terreni.TipoDestinazioneUsoDAO;
import it.csi.smranag.smrgaa.integration.terreni.UnitaArboreaDichiarataGaaDAO;
import it.csi.smranag.smrgaa.integration.terreni.UtilizzoParticellaGaaDAO;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.ProvinciaVO;
import it.csi.solmr.dto.anag.AnagParticellaExcelVO;
import it.csi.solmr.dto.anag.ConsistenzaVO;
import it.csi.solmr.dto.anag.DocumentoConduzioneVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.FoglioVO;
import it.csi.solmr.dto.anag.terreni.AltroVitignoVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneDichiarataVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneParticellaVO;
import it.csi.solmr.dto.anag.terreni.FiltriParticellareRicercaVO;
import it.csi.solmr.dto.anag.terreni.FiltriUnitaArboreaRicercaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaArboreaExcelVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.dto.anag.terreni.StoricoUnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.TipoCausaleModificaVO;
import it.csi.solmr.dto.anag.terreni.TipoUtilizzoVO;
import it.csi.solmr.dto.anag.terreni.TipoVarietaVO;
import it.csi.solmr.dto.anag.terreni.UnitaArboreaDichiarataVO;
import it.csi.solmr.dto.anag.terreni.UtilizzoConsociatoVO;
import it.csi.solmr.dto.anag.terreni.UtilizzoParticellaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.NotFoundException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.integration.CommonDAO;
import it.csi.solmr.integration.anag.AltroVitignoDAO;
import it.csi.solmr.integration.anag.ConduzioneDichiarataDAO;
import it.csi.solmr.integration.anag.ConduzioneParticellaDAO;
import it.csi.solmr.integration.anag.DocumentoDAO;
import it.csi.solmr.integration.anag.FoglioDAO;
import it.csi.solmr.integration.anag.ParticellaCertificataDAO;
import it.csi.solmr.integration.anag.ParticellaDAO;
import it.csi.solmr.integration.anag.StoricoParticellaDAO;
import it.csi.solmr.integration.anag.StoricoUnitaArboreaDAO;
import it.csi.solmr.integration.anag.TipoGenereIscrizioneDAO;
import it.csi.solmr.integration.anag.UnitaArboreaDichiarataDAO;
import it.csi.solmr.integration.anag.UtilizzoConsociatoDAO;
import it.csi.solmr.integration.anag.UtilizzoParticellaDAO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.StatefulTimeout;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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
@Stateless(name=TerreniGaaBean.jndiName,mappedName=TerreniGaaBean.jndiName)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@StatefulTimeout(unit = TimeUnit.SECONDS, value = 249)
public class TerreniGaaBean implements TerreniGaaLocal
{
  

  /**
   * 
   */
  private static final long serialVersionUID = 4854598281037195673L;
  public final static String jndiName="comp/env/solmr/gaa/TerreniGaa";
  
  

  SessionContext                   sessionContext;

  private transient ParticellaGaaDAO pDAO;
  private transient StoricoUnitaArboreaGaaDAO suaDAO;
  private transient UnitaArboreaDichiarataGaaDAO uadDAO;
  private transient IsolaParcellaGaaDAO ipDAO;
  private transient CommonDAO commonDAO = null;
  private transient ParticellaDAO particellaDAO = null;
  private transient ConduzioneParticellaDAO conduzioneParticellaDAO = null;
  private transient StoricoUnitaArboreaDAO storicoUnitaArboreaDAO = null;
  private transient StoricoUnitaArboreaGaaDAO storicoUnitaArboreaGaaDAO = null;
  private transient UnitaArboreaDichiarataDAO unitaArboreaDichiarataDAO = null;
  private transient TipoGenereIscrizioneDAO tipoGenereIscrizioneDAO = null;
  private transient AltroVitignoDAO altroVitignoDAO = null;
  private transient RegistroPascoloGaaDAO registroPascoloGaaDAO = null;
  private transient ConduzioneParticellaGaaDAO conduzioneParticellaGaaDAO = null;
  private transient UtilizzoParticellaGaaDAO utilizzoParticellaGaaDAO = null;
  private transient StoricoParticellaDAO storicoParticellaDAO = null;
  private transient ConduzioneDichiarataDAO conduzioneDichiarataDAO = null;
  private transient DocumentoDAO documentoDAO = null;
  private transient UtilizzoParticellaDAO utilizzoParticellaDAO = null;
  private transient UtilizzoConsociatoDAO utilizzoConsociatoDAO = null;
  private transient ConduzioneEleggibilitaGaaDAO conduzioneEleggibilitaGaaDAO = null;
  private transient ParticellaCertificataDAO particellaCertificataDAO = null;
  private transient FoglioDAO foglioDAO = null;
  private transient TipoDestinazioneUsoDAO tipoDestinazioneUsoDAO = null;

  private void initializeDAO() throws EJBException
  {
    try
    {
      pDAO = new ParticellaGaaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      suaDAO = new StoricoUnitaArboreaGaaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      uadDAO = new UnitaArboreaDichiarataGaaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      ipDAO = new IsolaParcellaGaaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      commonDAO = new CommonDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      particellaDAO = new ParticellaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      conduzioneParticellaDAO = new ConduzioneParticellaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      storicoUnitaArboreaDAO = new StoricoUnitaArboreaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      storicoUnitaArboreaGaaDAO = new StoricoUnitaArboreaGaaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      unitaArboreaDichiarataDAO = new UnitaArboreaDichiarataDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      tipoGenereIscrizioneDAO = new TipoGenereIscrizioneDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      altroVitignoDAO = new AltroVitignoDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      registroPascoloGaaDAO = new RegistroPascoloGaaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      conduzioneParticellaGaaDAO = new ConduzioneParticellaGaaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      utilizzoParticellaGaaDAO = new UtilizzoParticellaGaaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      storicoParticellaDAO = new StoricoParticellaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      conduzioneDichiarataDAO = new ConduzioneDichiarataDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      documentoDAO = new DocumentoDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      utilizzoParticellaDAO = new UtilizzoParticellaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      utilizzoConsociatoDAO = new UtilizzoConsociatoDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      conduzioneEleggibilitaGaaDAO = new ConduzioneEleggibilitaGaaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      particellaCertificataDAO = new ParticellaCertificataDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      foglioDAO = new FoglioDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      tipoDestinazioneUsoDAO = new TipoDestinazioneUsoDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
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
  
  
  public PlSqlCodeDescription allineaSupEleggibilePlSql(long idAzienda, 
      long idUtenteAggiornamento) 
    throws SolmrException
  {
    
    PlSqlCodeDescription plSqlObl = null;
    
    try
    {
      plSqlObl = pDAO.allineaSupEleggibilePlSql(idAzienda, idUtenteAggiornamento);
      //Ce stato un errore nel plsql senza generare eccezioni
      if(Validator.isNotEmpty(plSqlObl.getDescription())
          && plSqlObl.getDescription().equalsIgnoreCase("1"))
      {
        sessionContext.setRollbackOnly();
      }
      
      return plSqlObl;
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
    
  }
  
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaComune(long idAzienda) 
    throws SolmrException
  {    
    try
    {
      return suaDAO.riepilogoDestinazioneProduttivaComune(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
    
  }
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaComuneDichiarato(long idDichiarazioneConsistenza) 
    throws SolmrException
  {    
    try
    {
      return uadDAO.riepilogoDestinazioneProduttivaComuneDichiarato(idDichiarazioneConsistenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
    
  }
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaUvaDaVino(long idAzienda) 
    throws SolmrException
  {    
    try
    {
      return suaDAO.riepilogoDestinazioneProduttivaUvaDaVino(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
    
  }
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaUvaDaVinoDichiarato(
      long idDichiarazioneConsistenza) 
    throws SolmrException
  {    
    try
    {
      return uadDAO.riepilogoDestinazioneProduttivaUvaDaVinoDichiarato(idDichiarazioneConsistenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
    
  }
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaVinoDOP(long idAzienda) 
    throws SolmrException
  {    
    try
    {
      return suaDAO.riepilogoDestinazioneProduttivaVinoDOP(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
    
  }  
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaVinoDOPDichiarato(
      long idDichiarazioneConsistenza) 
    throws SolmrException
  {    
    try
    {
      return uadDAO.riepilogoDestinazioneProduttivaVinoDOPDichiarato(idDichiarazioneConsistenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoProvinciaVinoDOP(long idAzienda) 
    throws SolmrException
  {    
    try
    {
      return suaDAO.riepilogoProvinciaVinoDOP(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoProvinciaVinoDOPDichiarato(
      long idDichiarazioneConsistenza) 
    throws SolmrException
  {    
    try
    {
      return uadDAO.riepilogoProvinciaVinoDOPDichiarato(idDichiarazioneConsistenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  
  public TreeMap<String,Vector<RiepiloghiUnitaArboreaVO>> riepilogoElencoSociProvinciaVinoDOP(long idAzienda) 
    throws SolmrException
  {    
    try
    {
      return suaDAO.riepilogoElencoSociProvinciaVinoDOP(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<Long> esisteUVValidataByConduzioneAndAzienda(Vector<Long> elencoConduzioni, Long idAzienda)
    throws SolmrException
  {    
    try
    {
      return suaDAO.esisteUVValidataByConduzioneAndAzienda(elencoConduzioni, idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<Long> esisteUVByConduzioneAndAzienda(Vector<Long> elencoConduzioni, Long idAzienda)
    throws SolmrException
  {    
    try
    {
      return suaDAO.esisteUVByConduzioneAndAzienda(elencoConduzioni, idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<Long> esisteUVModProcVITIByConduzioneAndAzienda(Vector<Long> elencoConduzioni, Long idAzienda)
    throws SolmrException
  {    
    try
    {
      return suaDAO.esisteUVModProcVITIByConduzioneAndAzienda(elencoConduzioni, idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  
  
  public Vector<StoricoParticellaArboreaExcelVO> searchStoricoUnitaArboreaExcelSempliceByParameters( 
      Long idAzienda, FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO)
    throws SolmrException
  {    
    try
    {
      if (filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() <= 0)
      {
        return suaDAO.searchStoricoUnitaArboreaExcelSempliceByParameters(idAzienda, filtriUnitaArboreaRicercaVO);
      }
      else
      {
        return uadDAO.searchUnitaArboreaDichiarataExcelSempliceByParameters(idAzienda, filtriUnitaArboreaRicercaVO);
      }
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  
  public Vector<StoricoParticellaVO> getListUVForInserimento(Long idParticellaCurr, Vector<Long> vIdParticella, Long idAzienda)
    throws SolmrException
  {
    try
    {
      return suaDAO.getListUVForInserimento(idParticellaCurr, vIdParticella, idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
    
  }
  
  
  public BigDecimal getSupEleggibilePlSql(long idParticellaCertificata, long idCatalogoMatrice)
    throws SolmrException
  {
    try
    {
      return suaDAO.getSupEleggibilePlSql(idParticellaCertificata, idCatalogoMatrice);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    } 
  }
  
  public PlSqlCodeDescription compensazioneAziendalePlSql(long idAzienda, 
      long idUtenteAggiornamento)
  throws SolmrException
  {
    PlSqlCodeDescription plSqlObl = null;
    try
    {
      plSqlObl = suaDAO.compensazioneAziendalePlSql(idAzienda, idUtenteAggiornamento);
            
      if(Validator.isNotEmpty(plSqlObl.getDescription()))
      {
        sessionContext.setRollbackOnly();
      }
      
      return plSqlObl;
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    } 
  }
  
  public Vector<UVCompensazioneVO> getUVPerCompensazione(long idAzienda)
      throws SolmrException
  {
    try
    {
      return suaDAO.getUVPerCompensazione(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    } 
  }
  
  
  public CompensazioneAziendaVO getCompensazioneAzienda(long idAzienda)
      throws SolmrException
  {
    try
    {
      return suaDAO.getCompensazioneAzienda(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    } 
  }
  
  public Vector<RiepilogoCompensazioneVO> getRiepilogoPostAllinea(long idAzienda)
      throws SolmrException
  {
    try
    {
      return suaDAO.getRiepilogoPostAllinea(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    } 
  }
  
  public Vector<RiepilogoCompensazioneVO> getRiepilogoDirittiVitati(long idAzienda)
      throws SolmrException
  {
    try
    {
      return suaDAO.getRiepilogoDirittiVitati(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    } 
  }
  
  public CompensazioneAziendaVO getCompensazioneAziendaByIdAzienda(long idAzienda)
      throws SolmrException
  {
    try
    {
      return suaDAO.getCompensazioneAziendaByIdAzienda(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    } 
  }
  
  public int countUVAllineabiliGis(long idAzienda)
      throws SolmrException
  {
    try
    {
      return suaDAO.countUVAllineabiliGis(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    } 
  }
  
  public int countUVIstRiesameCompen(long idAzienda)
      throws SolmrException
  {
    try
    {
      return suaDAO.countUVIstRiesameCompen(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    } 
  }
  
  public int countSupUVIIrregolari(long idAzienda)
     throws SolmrException
  {
    try
    {
      return suaDAO.countSupUVIIrregolari(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    } 
  }
  
  public Date getMaxDataAggiornamentoConduzioniAndUV(long idAzienda)
      throws SolmrException
  {
    try
    {
      return suaDAO.getMaxDataAggiornamentoConduzioniAndUV(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    } 
  }
  
  public Date getMaxDataFotoInterpretazioneUV(long idAzienda)
      throws SolmrException
  {
    try
    {
      return suaDAO.getMaxDataFotoInterpretazioneUV(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    } 
  }
  
  public boolean existsIsoleParcDopoVarSched(long idAzienda, long isolaDichiarata)
      throws SolmrException
  {
    
    boolean flagExists = false;
    try
    {
      flagExists = suaDAO.existsIsoleParcDopoVarSchedLavMinIso(idAzienda, isolaDichiarata);
      if(!flagExists)
      {
        flagExists = suaDAO.existsIsoleParcDopoVarSchedIsoMinLav(idAzienda, isolaDichiarata);
      }      
      return flagExists;
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    } 
  }
  
  public BigDecimal getSupNonCompensabile(long idAzienda)
      throws SolmrException
  {
    try
    {    
      return suaDAO.getSupNonCompensabile(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    } 
  }
  
  public int countPercPossessoCompensazioneMag100(long idAzienda)
      throws SolmrException
  {
    try
    {    
      return suaDAO.countPercPossessoCompensazioneMag100(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    } 
  }
  
  public BigDecimal getPercUtilizzoEleggibile(long idAzienda, long idParticella)
      throws SolmrException
  {
    try
    {    
      return suaDAO.getPercUtilizzoEleggibile(idAzienda, idParticella);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    } 
  }
  
  public Vector<Vector<DirittoCompensazioneVO>> getDirittiCalcolati(long idAzienda)
      throws SolmrException
  {
    try
    {    
      return suaDAO.getDirittiCalcolati(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    } 
  }
  
  public BigDecimal getSumAreaGiaAssegnata(long idUnitaArborea)
      throws SolmrException
  {
    try
    {    
      return suaDAO.getSumAreaGiaAssegnata(idUnitaArborea);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    } 
  }
  
  public BigDecimal getSumAreaMaxAssegnabile(long idUnitaArborea)
      throws SolmrException
  {
    try
    {    
      return suaDAO.getSumAreaMaxAssegnabile(idUnitaArborea);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    } 
  }
  
  public ParticellaBioVO getParticellaBio(long idParticella, long idAzienda, Date dataInserimentoDichiarazione)
    throws SolmrException
  {    
    try
    {
      return pDAO.getParticellaBio(idParticella, idAzienda, dataInserimentoDichiarazione);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
    
  }
  
  public BigDecimal getSumUtilizziPrimariNoIndicati(
      long idAzienda, long idParticella, Vector<String> vIdUtilizzo)
    throws SolmrException
  {    
    try
    {
      return pDAO.getSumUtilizziPrimariNoIndicati(idAzienda, idParticella, vIdUtilizzo);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
    
  }
  
  public BigDecimal getSumUtilizziPrimariParticellaAltreConduzioni(long idAzienda, long idParticella,
      Vector<Long> vIdConduzioneParticella)
    throws SolmrException
  {    
    try
    {
      return pDAO.getSumUtilizziPrimariParticellaAltreConduzioni(idAzienda, idParticella, vIdConduzioneParticella);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public BigDecimal getSumPercentualPossessoAltreConduzioni(long idAzienda, long idParticella,
      Vector<Long> vIdConduzioneParticella)
    throws SolmrException
  {    
    try
    {
      return pDAO.getSumPercentualPossessoAltreConduzioni(idAzienda, idParticella, vIdConduzioneParticella);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public BigDecimal getSumSupCondottaAltreConduzioni(long idAzienda, long idParticella,
      Vector<Long> vIdConduzioneParticella)
    throws SolmrException
  {    
    try
    {
      return pDAO.getSumSupCondottaAltreConduzioni(idAzienda, idParticella, vIdConduzioneParticella);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<IstanzaRiesameVO> getFasiIstanzaRiesame(long idAzienda, long idParticella,
      Date dataInserimentoDichiarazione)
  throws SolmrException
  {    
    try
    {
      Vector<IstanzaRiesameVO> vIstanzaRiesame = pDAO.getFasiIstanzaRiesame(idAzienda, 
          idParticella, dataInserimentoDichiarazione);
      //Solo se non ritorna risultati e solo al pianoin lavorazione
      if(Validator.isEmpty(dataInserimentoDichiarazione) && Validator.isEmpty(vIstanzaRiesame))
      {
        vIstanzaRiesame = pDAO.getFasiIstanzaRiesameNoRecord(idAzienda, idParticella);
      }
      
      return vIstanzaRiesame;
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public int calcolaP30PlSql(long idStoricoParticella, Date dataInizioValidita)
      throws SolmrException
  {    
    try
    {
      return pDAO.calcolaP30PlSql(idStoricoParticella, dataInizioValidita);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public int calcolaP25PlSql(long idStoricoParticella, Date dataInizioValidita)
      throws SolmrException
  {    
    try
    {
      return pDAO.calcolaP25PlSql(idStoricoParticella, dataInizioValidita);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public int calcolaP26PlSql(long idAzienda, long idParticella, Long idParticellaCertificata)
      throws SolmrException
  {    
    try
    {
      return pDAO.calcolaP26PlSql(idAzienda, idParticella, idParticellaCertificata);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public PlSqlCodeDescription inserisciIstanzaPlSql(long idAzienda, int anno) 
      throws SolmrException
  {    
    try
    {
      return pDAO.inserisciIstanzaPlSql(idAzienda, anno);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  
  public Vector<CasoParticolareVO> getCasiParticolari(String particellaObbligatoria)
    throws SolmrException
  {    
    try
    {
      return pDAO.getCasiParticolari(particellaObbligatoria);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoRiepilogoVO> getTipoRiepilogo(String funzionalita, String codiceRuolo)
    throws SolmrException
  {    
    try
    {
      return pDAO.getTipoRiepilogo(funzionalita, codiceRuolo);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public boolean isAltreUvDaSchedario(long idParticella)
      throws SolmrException
  {    
    try
    {
      return pDAO.isAltreUvDaSchedario(idParticella);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoEsportazioneDatiVO> getTipoEsportazioneDati(String codMenu, String codiceRuolo)
    throws SolmrException
  {    
    try
    {
      return pDAO.getTipoEsportazioneDati(codMenu, codiceRuolo);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public boolean isParticellAttivaStoricoParticella(String istatComune, String sezione,
      String foglio, String particella, String subalterno) 
    throws SolmrException
  {    
    try
    {
      return pDAO.isParticellAttivaStoricoParticella(istatComune, sezione, foglio, particella, subalterno);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<StoricoParticellaVO> getElencoParticelleForPopNotifica(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO)
    throws SolmrException
  {    
    try
    {
      return pDAO.getElencoParticelleForPopNotifica(filtriParticellareRicercaVO);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public StoricoParticellaVO findStoricoParticellaDichCompleto(long idStoricoParticella, 
      long idDichiarazioneConsistenza)
    throws SolmrException
  {    
    try
    {
      return pDAO.findStoricoParticellaDichCompleto(idStoricoParticella, idDichiarazioneConsistenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoEfaVO> getListTipoEfa()
    throws SolmrException
  {    
    try
    {
      return pDAO.getListTipoEfa();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoEfaVO> getListLegendaTipoEfa()
    throws SolmrException
  {    
    try
    {
      return pDAO.getListLegendaTipoEfa();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public TipoEfaVO getTipoEfaFromIdCatalogoMatrice(long idCatalogoMatrice)
      throws SolmrException
  {    
    try
    {
      return pDAO.getTipoEfaFromIdCatalogoMatrice(idCatalogoMatrice);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoPeriodoSeminaVO> getListTipoPeriodoSemina() 
    throws SolmrException
  {    
    try
    {
      return pDAO.getListTipoPeriodoSemina();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoUtilizzoVO> getListTipoUtilizzoEfa(long idTipoEfa)
     throws SolmrException
  {    
    try
    {
      return pDAO.getListTipoUtilizzoEfa(idTipoEfa);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoDestinazioneVO> getListTipoDestinazioneEfa(long idTipoEfa, long idUtilizzo)
    throws SolmrException
  {    
    try
    {
      return pDAO.getListTipoDestinazioneEfa(idTipoEfa, idUtilizzo);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoVarietaVO> getListTipoVarietaEfaByMatrice(long idTipoEfa, 
      long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso, long idTipoQualitaUso)
    throws SolmrException
  {    
    try
    {
      return pDAO.getListTipoVarietaEfaByMatrice(idTipoEfa, idUtilizzo, idTipoDestinazione, idTipoDettaglioUso, idTipoQualitaUso);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoDettaglioUsoVO> getListDettaglioUsoEfaByMatrice(long idTipoEfa,
      long idUtilizzo, long idTipoDestinazione)
    throws SolmrException
  {    
    try
    {
      return pDAO.getListDettaglioUsoEfaByMatrice(idTipoEfa, idUtilizzo, idTipoDestinazione);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoQualitaUsoVO> getListQualitaUsoEfaByMatrice(long idTipoEfa,
      long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso) 
          throws SolmrException
  {    
    try
    {
      return pDAO.getListQualitaUsoEfaByMatrice(idTipoEfa, idUtilizzo, 
          idTipoDestinazione, idTipoDettaglioUso);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Integer getAbbPonderazioneByMatrice(
      long idTipoEfa, long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso,
      long idTipoQualitaUso, long idVarieta)
    throws SolmrException
  {    
    try
    {
      return pDAO.getAbbPonderazioneByMatrice(idTipoEfa, idUtilizzo, 
          idTipoDestinazione, idTipoDettaglioUso, idTipoQualitaUso, idVarieta);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public String getValoreAttivoTipoAreaFromParticellaAndId(
      long idParticella, long idTipoArea)
    throws SolmrException
  {    
    try
    {
      return pDAO.getValoreAttivoTipoAreaFromParticellaAndId(idParticella, idTipoArea);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoAreaVO> getValoriTipoAreaParticella(long idParticella, Date dataInserimentoValidazione)
    throws SolmrException
  {    
    try
    {
      return pDAO.getValoriTipoAreaParticella(idParticella, dataInserimentoValidazione);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoAreaVO> getAllValoriTipoArea()
    throws SolmrException
  {    
    try
    {
      return pDAO.getAllValoriTipoArea();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoAreaVO> getDescTipoAreaBrogliaccio(Long idDichiarazioneConsistenza)
      throws SolmrException
  {    
    try
    {
      return pDAO.getDescTipoAreaBrogliaccio(idDichiarazioneConsistenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
   
  
  public Vector<TipoAreaVO> getValoriTipoAreaFoglio(String comune, String foglio, String sezione)
    throws SolmrException
  {    
    try
    {
      return pDAO.getValoriTipoAreaFoglio(comune, foglio, sezione);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoMetodoIrriguoVO> getElencoMetodoIrriguo()
    throws SolmrException
  {    
    try
    {
      return pDAO.getElencoMetodoIrriguo();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoPeriodoSeminaVO> getListTipoPeriodoSeminaByCatalogo(long idUtilizzo,
      long idDestinazione, long idDettUso, long idQualiUso, long idVarieta)
    throws SolmrException
  {    
    try
    {
      return pDAO.getListTipoPeriodoSeminaByCatalogo(idUtilizzo, idDestinazione, 
          idDettUso, idQualiUso, idVarieta);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoFonteVO> getElencoAllTipoFonte() throws SolmrException
  {    
    try
    {
      return pDAO.getElencoAllTipoFonte();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public boolean isUtilizzoAttivoSuMatrice(long idUtilizzo) throws SolmrException
  {    
    try
    {
      return pDAO.isUtilizzoAttivoSuMatrice(idUtilizzo);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoAreaVO> riepilogoTipoArea(long idAzienda)
    throws SolmrException
  {    
    try
    {
      return pDAO.riepilogoTipoArea(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoAreaVO> riepilogoTipoAreaDichiarato(long idDichiarazioneConsistenza)
    throws SolmrException
  {    
    try
    {
      return pDAO.riepilogoTipoAreaDichiarato(idDichiarazioneConsistenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoAreaVO> getValoriTipoAreaFiltroElenco(Long idDichiarazioneConsistenza)
    throws SolmrException
  {    
    try
    {
      return pDAO.getValoriTipoAreaFiltroElenco(idDichiarazioneConsistenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  
  public boolean isDettaglioUsoObbligatorio(long idTipoEfa,
      long idVarieta) throws SolmrException
  {    
    try
    {
      return pDAO.isDettaglioUsoObbligatorio(idTipoEfa, idVarieta);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public TipoEfaVO getTipoEfaFromPrimaryKey(long idTipoEfa)
      throws SolmrException
  {    
    try
    {
      return pDAO.getTipoEfaFromPrimaryKey(idTipoEfa);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  
  public Vector<Long> getIdParticellaByIdConduzione(Vector<Long> vIdConduzioneParticella)
    throws SolmrException
  {
    try
    {
      return pDAO.getIdParticellaByIdConduzione(vIdConduzioneParticella);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  
  public Vector<ProvinciaVO> getListProvincieParticelleByIdAzienda(Long idAzienda)
    throws SolmrException
  {
    try
    {
      return pDAO.getListProvincieParticelleByIdAzienda(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<ProvinciaVO> getListProvincieParticelleByIdDichiarazioneConsistenza(Long idDichiarazioneConsistenza)
    throws SolmrException
  {
    try
    {
      return pDAO.getListProvincieParticelleByIdDichiarazioneConsistenza(idDichiarazioneConsistenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<IsolaParcellaVO> getElencoIsoleParcelle(String nomeLib, long idAzienda)
    throws SolmrException
  {
    try
    {
      return ipDAO.getElencoIsoleParcelle(nomeLib, idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }  
  
  public Vector<StoricoParticellaVO> associaParcelleGisLettura(String nomeLib, long idAzienda,long idUtente)
    throws SolmrException
  {
    Vector<StoricoParticellaVO> vStoricoUV = null;
    try
    {
      //aggiorno la tabella db_dichiarazione_consistenza
      Vector<IsolaParcellaVO> vIsoleParcelle = ipDAO.getElencoIsoleParcelleNoToll(idAzienda);
      if(vIsoleParcelle != null)
      {
        ipDAO.updateDichCons(vIsoleParcelle.get(0).getIdDichiarazioneConsistenza().longValue());
      
        Vector<Long> vIdUnitaArboree = ipDAO.getElencoIdUVParcelleDoppie(idAzienda);
        if(vIdUnitaArboree != null)
        {
          vStoricoUV = ipDAO.getElencoUVIsoleParcelleByIdUnitaArborea(idAzienda, 
              vIdUnitaArboree, idUtente);
        }
      }
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(e.getMessage());
    }
    
    
    return vStoricoUV;
    
    
  }
  
  
  /**
   * Aggiorna la tabella DB_UNAR_PARCELLA in base 
   * alle selezioni delle combo
   * 
   * 
   * @param idAzienda
   * @param idUtente
   * @param vIdUnarParcellaSel
   * @throws SolmrException
   */
  public void associaParcelleGisConferma(long idAzienda, long idUtente, Vector<Long> vIdUnarParcellaSel)
    throws SolmrException
  {
    try
    {
      
      
      Vector<Long> vIdUnarParcella = ipDAO.getElencoIdUnarParcella(idAzienda);
    
      ipDAO.updateDBUnarParcellaConferma(vIdUnarParcella, vIdUnarParcellaSel, idUtente);
      
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(e.getMessage());
    }
    
    
  }
  
  
  public ConsistenzaVO getDichConsUVParcelleDoppie(long idAzienda)
    throws SolmrException
  {
    try
    {
      return ipDAO.getDichConsUVParcelleDoppie(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  
  }
  
  
  /**
   * 
   * allinea le uv al gis con la superficie eleggibile
   * 
   * @param vIdIsolaParcella
   * @param idAzienda
   * @param ruoloUtenza
   * @throws SolmrException
   */
  public void allineaUVaGIS(Vector<Long> vIdIsolaParcella, long idAzienda, 
      long idDichiarazioneConsistenza, RuoloUtenza ruoloUtenza)
    throws SolmrException
  {
    try
    {
      Vector<BigDecimal> elencoIdParticella = new Vector<BigDecimal>();
      // Ciclo le unità arboree selezionate dall'utente
      Long idGenereIscrizioneDefault = tipoGenereIscrizioneDAO.getDefaultIdGenereIscrizione();
      Long idGenereIscrizioneProvvisorio = tipoGenereIscrizioneDAO.getIdGenereIscrizioneByFlag("N");
      
      
      for (int i = 0; i < vIdIsolaParcella.size(); i++)
      {
        HashMap<Long,Vector<Long>> hIsolaParcella = ipDAO
          .getHashIdParticellaIdStoricoUnitaArborea(vIdIsolaParcella.get(i).longValue(), idAzienda);
        
        Iterator<Long> it = hIsolaParcella.keySet().iterator();
        //ciclo sulla particella all'interno della parcella
        while(it.hasNext()) 
        { 
          Long idParticella = it.next(); 
          Vector<Long> vIdStoricoUnitaArborea = hIsolaParcella.get(idParticella);
          
          //prendo la prima poiche la coppia idParticela/idVarieta all'interno della particella
          //Mi ritornano sempre la stessa superficie eleggibile gis per ogni uv
          StoricoParticellaVO storicoParticellaVO = storicoUnitaArboreaDAO
              .findStoricoParticellaArborea(vIdStoricoUnitaArborea.get(0));
          BigDecimal superficieGis = null;
          if(Validator.isNotEmpty(storicoParticellaVO.getParticellaCertificataVO().getVParticellaCertEleg())
            && Validator.isNotEmpty(storicoParticellaVO.getParticellaCertificataVO().getVParticellaCertEleg()
                .get(0).getSuperficie()))
          {
            superficieGis = storicoParticellaVO.getParticellaCertificataVO().getVParticellaCertEleg()
                .get(0).getSuperficie();
          }
          
          BigDecimal sommaAreaUV = new BigDecimal(0);
          BigDecimal sommaAreaUVAggiornate = new BigDecimal(0);
          if((superficieGis != null)
            && (superficieGis.compareTo(new BigDecimal(0)) > 0)) 
          {
          
            //Mi ricavo la percentuale utilizzo eleggibile a gis
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
            
            //Ciclo sulle uv della particella
            for(int z=0;z<vIdStoricoUnitaArborea.size();z++)
            {
              StoricoUnitaArboreaVO storicoUnitaArboreaVO = findStoricoUnitaArboreaConAltriVitigni(
                  vIdStoricoUnitaArborea.get(z));
              String oldArea = StringUtils.parseSuperficieField(storicoUnitaArboreaVO.getArea());
              String oldStatoUnitaArborea = storicoUnitaArboreaVO.getStatoUnitaArborea();
              TipoCausaleModificaVO oldTipoCausaleModificaVO = storicoUnitaArboreaVO.getTipoCausaleModificaVO();  
              
              BigDecimal areaUV = new BigDecimal(storicoUnitaArboreaVO.getArea().replace(',','.'));
              //riprorziono l'area della UV                
              areaUV = areaUV.multiply(superficieGis);
              areaUV = areaUV.divide(sommaAreaUV,4,BigDecimal.ROUND_HALF_UP);
              sommaAreaUVAggiornate = sommaAreaUVAggiornate.add(areaUV);
              //raccolgo gli sfrigu persi
              if(z == (vIdStoricoUnitaArborea.size() - 1))
              {
                if(superficieGis.compareTo(sommaAreaUVAggiornate) > 0)
                {
                  superficieGis = superficieGis.subtract(sommaAreaUVAggiornate);
                  areaUV = areaUV.add(superficieGis);
                }
              }
              
              storicoUnitaArboreaVO.setArea(StringUtils.parseSuperficieFieldBigDecimal(areaUV));
              
              //vado avanti solo il dato ricavato è diverso da quello resente su DB
              if(!storicoUnitaArboreaVO.getArea().equalsIgnoreCase(oldArea))
              {
                
                if (!elencoIdParticella.contains(new BigDecimal(idParticella.longValue())))
                {
                  elencoIdParticella.add(new BigDecimal(idParticella.longValue()));
                }
                
                
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
                
                
              
                // Verifico se l'unità arborea selezionata è presente in una
                // dichiarazione di consistenza
                UnitaArboreaDichiarataVO[] elencoUnitaArboreeDichiarate = unitaArboreaDichiarataDAO
                    .getListUnitaArboreaDichiarataByIdStoricoUnitaArborea(
                        storicoUnitaArboreaVO.getIdStoricoUnitaArborea(), null);
                
                // Se lo è...
                if (elencoUnitaArboreeDichiarate != null
                    && elencoUnitaArboreeDichiarate.length > 0)
                {
                  // Effettuo la storicizzazione del record
                  storicoUnitaArboreaDAO
                      .storicizzaStoricoUnitaArborea(storicoUnitaArboreaVO
                          .getIdStoricoUnitaArborea().longValue());
                  // Inserisco il nuovo record
                  storicoUnitaArboreaVO.setIdStoricoUnitaArborea(null);
                  storicoUnitaArboreaVO.setDataInizioValidita(new java.util.Date(
                      new Timestamp(System.currentTimeMillis()).getTime()));
                  storicoUnitaArboreaVO.setDataFineValidita(null);
                  storicoUnitaArboreaVO.setCampagna(DateUtils.getCurrentYear().toString());
                  storicoUnitaArboreaVO.setSuperficieDaIscrivereAlbo(storicoUnitaArboreaVO.getArea());
                  storicoUnitaArboreaVO.setDataAggiornamento(new java.util.Date(
                      new Timestamp(System.currentTimeMillis()).getTime()));
                  storicoUnitaArboreaVO.setIdUtenteAggiornamento(ruoloUtenza
                      .getIdUtente());
                  storicoUnitaArboreaVO.setRecordModificato(SolmrConstants.FLAG_S);
                  storicoUnitaArboreaVO.setDataEsecuzione(null);
                  storicoUnitaArboreaVO.setEsitoControllo(null);
                  storicoUnitaArboreaVO.setNote(null);
                  
                  if(Validator.isEmpty(oldTipoCausaleModificaVO)
                    || (Validator.isNotEmpty(oldTipoCausaleModificaVO))
                    && !"S".equalsIgnoreCase(oldTipoCausaleModificaVO.getAltroProcedimento()))
                  {
                    storicoUnitaArboreaVO.setIdCausaleModifica(SolmrConstants.CAUSALE_ALLINEA_GIS);
                  }
                  
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
                  
                } //if esistono unita arboree legate a dich consistenza               
                // Altrimenti
                else
                {                  
                  // Se l'utente loggato presenta una tipologia ruolo non compatibile
                  // con lo stato dell'unità vitata oppure quest'ultima non è ancora
                  // stata specificata
                  if (!Validator.isNotEmpty(oldStatoUnitaArborea)
                      || ((ruoloUtenza.isUtenteRegionale() || ruoloUtenza
                          .isUtenteProvinciale()) && SolmrConstants.STATO_UV_PROPOSTO_CAA
                          .equalsIgnoreCase(oldStatoUnitaArborea))
                      || ((ruoloUtenza.isUtenteIntermediario() || ruoloUtenza
                          .isUtenteOPRGestore()) && SolmrConstants.STATO_UV_VALIDATO_PA
                          .equalsIgnoreCase(oldStatoUnitaArborea)))
                  {
                    // Effettuo la storicizzazione del record
                    storicoUnitaArboreaDAO
                        .storicizzaStoricoUnitaArborea(storicoUnitaArboreaVO.getIdStoricoUnitaArborea());
                    // Inserisco il nuovo record
                    storicoUnitaArboreaVO.setIdStoricoUnitaArborea(null);
                    storicoUnitaArboreaVO.setDataInizioValidita(new java.util.Date(
                        new Timestamp(System.currentTimeMillis()).getTime()));
                    storicoUnitaArboreaVO.setDataFineValidita(null);
                    storicoUnitaArboreaVO.setCampagna(DateUtils.getCurrentYear().toString());
                    storicoUnitaArboreaVO.setSuperficieDaIscrivereAlbo(storicoUnitaArboreaVO.getArea());
                    storicoUnitaArboreaVO.setDataAggiornamento(new java.util.Date(
                        new Timestamp(System.currentTimeMillis()).getTime()));
                    storicoUnitaArboreaVO.setIdUtenteAggiornamento(ruoloUtenza
                        .getIdUtente());
                    storicoUnitaArboreaVO.setRecordModificato(SolmrConstants.FLAG_S);
                    storicoUnitaArboreaVO.setDataEsecuzione(null);
                    storicoUnitaArboreaVO.setEsitoControllo(null);
                    storicoUnitaArboreaVO.setNote(null);
                    
                    if(Validator.isEmpty(oldTipoCausaleModificaVO)
                        || (Validator.isNotEmpty(oldTipoCausaleModificaVO))
                        && !"S".equalsIgnoreCase(oldTipoCausaleModificaVO.getAltroProcedimento()))
                    {
                      storicoUnitaArboreaVO.setIdCausaleModifica(SolmrConstants.CAUSALE_ALLINEA_GIS);
                    }
                    
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
                  else
                  {
                    // Effettuo solo update del record
                    storicoUnitaArboreaVO.setDataAggiornamento(new java.util.Date(
                        new Timestamp(System.currentTimeMillis()).getTime()));
                    storicoUnitaArboreaVO.setIdUtenteAggiornamento(ruoloUtenza
                        .getIdUtente());
                    storicoUnitaArboreaVO.setDataEsecuzione(null);
                    storicoUnitaArboreaVO.setEsitoControllo(null);    
                    storicoUnitaArboreaVO.setCampagna(DateUtils.getCurrentYear().toString());
                    storicoUnitaArboreaVO.setSuperficieDaIscrivereAlbo(storicoUnitaArboreaVO.getArea());
                    storicoUnitaArboreaVO.setRecordModificato(SolmrConstants.FLAG_S);
                    storicoUnitaArboreaVO.setDataEsecuzione(null);
                    storicoUnitaArboreaVO.setEsitoControllo(null);
                    
                    if(Validator.isEmpty(oldTipoCausaleModificaVO)
                        || (Validator.isNotEmpty(oldTipoCausaleModificaVO))
                        && !"S".equalsIgnoreCase(oldTipoCausaleModificaVO.getAltroProcedimento()))
                    {
                      storicoUnitaArboreaVO.setIdCausaleModifica(SolmrConstants.CAUSALE_ALLINEA_GIS);
                    }
                    
                    
                    
                    
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
                    
                    storicoUnitaArboreaDAO
                        .updateStoricoUnitaArborea(storicoUnitaArboreaVO);
                    
                  }
                  
                  
                }
                
              } //if differenza area
              
            }//For delle uv
              
              
              
              
          }//end if superficieGIs
          //Se non trovato almeno un docfit == 26 non faccio nulla!!       
        
        } //ciclo particella
        
          
          
      } //for parcelle
      // Aggiorno lo stato dello schedario delle particelle su cui insistevano
      // UV selezionate
      if(elencoIdParticella.size() > 0)
      {
        particellaDAO
          .aggiornaSchedarioParticellaPlSql((BigDecimal[]) elencoIdParticella
              .toArray(new BigDecimal[elencoIdParticella.size()]));
        
        //aggiorno 
        ipDAO.updateDichCons(idDichiarazioneConsistenza);
      }
      
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(dae.getMessage());
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(e.getMessage());
    }
  }
  
  
  /*public Integer getTolleranzaPlSql(String nomeLib, long idAzienda, long idUnitaArborea)
    throws SolmrException
  {
    try
    {
      return ipDAO.getTolleranzaPlSql(nomeLib, idAzienda, idUnitaArborea);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }*/
  
  public Vector<RegistroPascoloVO> getRegistroPascoliPianoLavorazione(
      long idParticellaCertificata)
    throws SolmrException
  {
    try
    {
      
      Vector<RegistroPascoloVO> vRegistroPascoloVO = null;
      Vector<Long> vIdFonte = registroPascoloGaaDAO.getIdFonteRegistroPascoliPianoLavorazione(idParticellaCertificata);
      if(Validator.isNotEmpty(vIdFonte))
      { 
        for(int i=0;i<vIdFonte.size();i++)
        {
          RegistroPascoloVO registroPascoloVO = registroPascoloGaaDAO
              .getRegistroPascoloPianoLavorazioneIdFonte(idParticellaCertificata, vIdFonte.get(i));
          if(Validator.isNotEmpty(registroPascoloVO))
          {
            if(vRegistroPascoloVO == null)
              vRegistroPascoloVO = new Vector<RegistroPascoloVO>(); 
            
            vRegistroPascoloVO.add(registroPascoloVO);              
          }
            
        }
      }
      
      return vRegistroPascoloVO;
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  
  public Vector<RegistroPascoloVO> getRegistroPascoliPianoLavorazioneChiaveCatastale(
      String istatComune, String foglio, String particella, String sezione, String subalterno)
    throws SolmrException
  {
    try
    {
      
      Vector<RegistroPascoloVO> vRegistroPascoloVO = null;
      Vector<Long> vIdFonte = registroPascoloGaaDAO.getIdFonteRegistroPascoliPianoLavorazioneChiaveCatastale(
          istatComune, foglio, particella, sezione, subalterno);
      if(Validator.isNotEmpty(vIdFonte))
      { 
        for(int i=0;i<vIdFonte.size();i++)
        {
          RegistroPascoloVO registroPascoloVO = registroPascoloGaaDAO
              .getRegistroPascoloPianoLavorazioneChiaveCatastaleIdFonte(
                  istatComune, foglio, particella, sezione, subalterno, vIdFonte.get(i));
          if(Validator.isNotEmpty(registroPascoloVO))
          {
            if(vRegistroPascoloVO == null)
              vRegistroPascoloVO = new Vector<RegistroPascoloVO>(); 
            
            vRegistroPascoloVO.add(registroPascoloVO);              
          }
            
        }
      }
      
      return vRegistroPascoloVO;
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<RegistroPascoloVO> getRegistroPascoliDichCons(
      long idParticellaCertificata, int annoDichiarazione)
    throws SolmrException
  {
    try
    {
      return registroPascoloGaaDAO.getRegistroPascoliDichCons(idParticellaCertificata, annoDichiarazione);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<RegistroPascoloVO> getRegistroPascoliDichConsChiaveCatastale(
      String istatComune, String foglio, String particella, String sezione, String subalterno, int annoDichiarazione)
    throws SolmrException
  {
    try
    {
      return registroPascoloGaaDAO.getRegistroPascoliDichConsChiaveCatastale(
          istatComune, foglio, particella, sezione, subalterno, annoDichiarazione);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public boolean isRegistroPascoliPratoPolifita(
      long idParticellaCertificata) throws SolmrException
  {
    try
    {
      return registroPascoloGaaDAO.isRegistroPascoliPratoPolifita(idParticellaCertificata);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  
  public StoricoUnitaArboreaVO findStoricoUnitaArboreaConAltriVitigni(
      Long idStoricoUnitaArborea) throws Exception
  {
    StoricoUnitaArboreaVO storicoUnitaArboreaVO = null;
    try
    {
      storicoUnitaArboreaVO = storicoUnitaArboreaDAO
          .findStoricoUnitaArborea(idStoricoUnitaArborea);
      
      AltroVitignoVO[] elencoAltroVitignoVO = altroVitignoDAO
          .getListAltroVitignoByIdStoricoUnitaArborea(storicoUnitaArboreaVO.getIdStoricoUnitaArborea(), null);      
      storicoUnitaArboreaVO.setElencoAltriVitigni(elencoAltroVitignoVO);
      
      
      return storicoUnitaArboreaVO;
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  /**
   * Metodo usato per l'aggiornamento UV per la compensazione
   * 
   * 
   * 
   * 
   * @param idAzienda
   * @param ruoloUtenza
   * @throws SolmrException
   */
  public void allineaUVaCompensazione(long idAzienda, RuoloUtenza ruoloUtenza)
    throws SolmrException
  {
    try
    {
      Vector<Long> elencoIdParticella = new Vector<Long>();
      // Ciclo le unità arboree selezionate dall'utente
      
      Long idGenereIscrizioneDefault = tipoGenereIscrizioneDAO.getDefaultIdGenereIscrizione();
      Long idGenereIscrizioneProvvisorio = tipoGenereIscrizioneDAO.getIdGenereIscrizioneByFlag("N");
      
      
      Vector<RiepilogoCompensazioneVO> vIdStoricoCompensazioneUV = storicoUnitaArboreaGaaDAO
          .getVIdStoricoUVPerCompensazione(idAzienda);
      
      
      for (int i = 0; i < vIdStoricoCompensazioneUV.size(); i++)
      {        
        //Ciclo sulle uv della particella
        
        StoricoUnitaArboreaVO storicoUnitaArboreaVO = findStoricoUnitaArboreaConAltriVitigni(
            vIdStoricoCompensazioneUV.get(i).getIdStoricoUnitaArborea());
        String newArea = StringUtils.parseSuperficieFieldBigDecimal(
            vIdStoricoCompensazioneUV.get(i).getSupPastAllinea());
        String oldStatoUnitaArborea = storicoUnitaArboreaVO.getStatoUnitaArborea();
        TipoCausaleModificaVO oldTipoCausaleModificaVO = storicoUnitaArboreaVO.getTipoCausaleModificaVO();  
        
        
        //vado avanti solo il dato ricavato è diverso da quello resente su DB
        if(!newArea.equalsIgnoreCase(storicoUnitaArboreaVO.getArea()))
        {
          
          if (!elencoIdParticella.contains(storicoUnitaArboreaVO.getIdParticella()))
          {
            elencoIdParticella.add(storicoUnitaArboreaVO.getIdParticella());
          }
          
          storicoUnitaArboreaVO.setArea(newArea);
          
          
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
          
          
        
          // Verifico se l'unità arborea selezionata è presente in una
          // dichiarazione di consistenza
          UnitaArboreaDichiarataVO[] elencoUnitaArboreeDichiarate = unitaArboreaDichiarataDAO
              .getListUnitaArboreaDichiarataByIdStoricoUnitaArborea(
                  storicoUnitaArboreaVO.getIdStoricoUnitaArborea(), null);
          
          // Se lo è...
          if (elencoUnitaArboreeDichiarate != null
              && elencoUnitaArboreeDichiarate.length > 0)
          {
            // Effettuo la storicizzazione del record
            storicoUnitaArboreaDAO
                .storicizzaStoricoUnitaArborea(storicoUnitaArboreaVO
                    .getIdStoricoUnitaArborea().longValue());
            // Inserisco il nuovo record
            storicoUnitaArboreaVO.setIdStoricoUnitaArborea(null);
            storicoUnitaArboreaVO.setDataInizioValidita(new java.util.Date(
                new Timestamp(System.currentTimeMillis()).getTime()));
            storicoUnitaArboreaVO.setDataFineValidita(null);
            storicoUnitaArboreaVO.setCampagna(DateUtils.getCurrentYear().toString());
            storicoUnitaArboreaVO.setSuperficieDaIscrivereAlbo(storicoUnitaArboreaVO.getArea());
            storicoUnitaArboreaVO.setDataAggiornamento(new java.util.Date(
                new Timestamp(System.currentTimeMillis()).getTime()));
            storicoUnitaArboreaVO.setIdUtenteAggiornamento(ruoloUtenza
                .getIdUtente());
            storicoUnitaArboreaVO.setRecordModificato(SolmrConstants.FLAG_S);
            storicoUnitaArboreaVO.setDataEsecuzione(null);
            storicoUnitaArboreaVO.setEsitoControllo(null);
            storicoUnitaArboreaVO.setNote(null);
            
            if(Validator.isEmpty(oldTipoCausaleModificaVO)
              || (Validator.isNotEmpty(oldTipoCausaleModificaVO))
              && !"S".equalsIgnoreCase(oldTipoCausaleModificaVO.getAltroProcedimento()))
            {
              storicoUnitaArboreaVO.setIdCausaleModifica(SolmrConstants.CAUSALE_ALLINEA_GIS);
            }
            
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
            
          } //if esistono unita arboree legate a dich consistenza               
          // Altrimenti
          else
          {                  
            // Se l'utente loggato presenta una tipologia ruolo non compatibile
            // con lo stato dell'unità vitata oppure quest'ultima non è ancora
            // stata specificata
            if (!Validator.isNotEmpty(oldStatoUnitaArborea)
                || ((ruoloUtenza.isUtenteRegionale() || ruoloUtenza
                    .isUtenteProvinciale()) && SolmrConstants.STATO_UV_PROPOSTO_CAA
                    .equalsIgnoreCase(oldStatoUnitaArborea))
                || ((ruoloUtenza.isUtenteIntermediario() || ruoloUtenza
                    .isUtenteOPRGestore()) && SolmrConstants.STATO_UV_VALIDATO_PA
                    .equalsIgnoreCase(oldStatoUnitaArborea)))
            {
              // Effettuo la storicizzazione del record
              storicoUnitaArboreaDAO
                  .storicizzaStoricoUnitaArborea(storicoUnitaArboreaVO.getIdStoricoUnitaArborea());
              // Inserisco il nuovo record
              storicoUnitaArboreaVO.setIdStoricoUnitaArborea(null);
              storicoUnitaArboreaVO.setDataInizioValidita(new java.util.Date(
                  new Timestamp(System.currentTimeMillis()).getTime()));
              storicoUnitaArboreaVO.setDataFineValidita(null);
              storicoUnitaArboreaVO.setCampagna(DateUtils.getCurrentYear().toString());
              storicoUnitaArboreaVO.setSuperficieDaIscrivereAlbo(storicoUnitaArboreaVO.getArea());
              storicoUnitaArboreaVO.setDataAggiornamento(new java.util.Date(
                  new Timestamp(System.currentTimeMillis()).getTime()));
              storicoUnitaArboreaVO.setIdUtenteAggiornamento(ruoloUtenza
                  .getIdUtente());
              storicoUnitaArboreaVO.setRecordModificato(SolmrConstants.FLAG_S);
              storicoUnitaArboreaVO.setDataEsecuzione(null);
              storicoUnitaArboreaVO.setEsitoControllo(null);
              storicoUnitaArboreaVO.setNote(null);
              
              if(Validator.isEmpty(oldTipoCausaleModificaVO)
                  || (Validator.isNotEmpty(oldTipoCausaleModificaVO))
                  && !"S".equalsIgnoreCase(oldTipoCausaleModificaVO.getAltroProcedimento()))
              {
                storicoUnitaArboreaVO.setIdCausaleModifica(SolmrConstants.CAUSALE_ALLINEA_GIS);
              }
              
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
            else
            {
              // Effettuo solo update del record
              storicoUnitaArboreaVO.setDataAggiornamento(new java.util.Date(
                  new Timestamp(System.currentTimeMillis()).getTime()));
              storicoUnitaArboreaVO.setIdUtenteAggiornamento(ruoloUtenza
                  .getIdUtente());
              storicoUnitaArboreaVO.setDataEsecuzione(null);
              storicoUnitaArboreaVO.setEsitoControllo(null);    
              storicoUnitaArboreaVO.setCampagna(DateUtils.getCurrentYear().toString());
              storicoUnitaArboreaVO.setSuperficieDaIscrivereAlbo(storicoUnitaArboreaVO.getArea());
              storicoUnitaArboreaVO.setRecordModificato(SolmrConstants.FLAG_S);
              storicoUnitaArboreaVO.setDataEsecuzione(null);
              storicoUnitaArboreaVO.setEsitoControllo(null);
              
              if(Validator.isEmpty(oldTipoCausaleModificaVO)
                  || (Validator.isNotEmpty(oldTipoCausaleModificaVO))
                  && !"S".equalsIgnoreCase(oldTipoCausaleModificaVO.getAltroProcedimento()))
              {
                storicoUnitaArboreaVO.setIdCausaleModifica(SolmrConstants.CAUSALE_ALLINEA_GIS);
              }
              
              
              
              
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
              
              storicoUnitaArboreaDAO
                  .updateStoricoUnitaArborea(storicoUnitaArboreaVO);
              
            }
            
            
          }
          
        } //if differenza area        
          
          
      } //for uv
      // Aggiorno lo stato dello schedario delle particelle su cui insistevano
      // UV selezionate      
      if(elencoIdParticella.size() > 0)
      {
        pDAO.updateParticelleSchedarioS(elencoIdParticella);
        pDAO.updateParticelleSchedarioM(elencoIdParticella);
      }
      
      //Aggiorno compensazioneAzienda
      storicoUnitaArboreaGaaDAO.updateCompensazioneAzienda(idAzienda, ruoloUtenza.getIdUtente().longValue());
      
      
      
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(dae.getMessage());
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(e.getMessage());
    }
  }
  
  
  /**
   * 
   * Allinea la superificie a gis solo se tutte le uv della particella dell'azienda
   * sono in tolleranza!!!
   * 
   * @param idAzienda
   * @param ruoloUtenza
   * @throws SolmrException
   */
  /*public void allineaUVinTolleranzaGIS(long idAzienda, RuoloUtenza ruoloUtenza)
    throws SolmrException
  {
    try
    {
      Vector<BigDecimal> elencoIdParticella = new Vector<BigDecimal>();
      // Ciclo le unità arboree selezionate dall'utente
     
      Long idGenereIscrizioneDefault = tipoGenereIscrizioneDAO.getDefaultIdGenereIscrizione();
      Long idGenereIscrizioneProvvisorio = tipoGenereIscrizioneDAO.getIdGenereIscrizioneByFlag("N");
      
      
      
      HashMap<Long,Vector<TolleranzaVO>> hIdUVTolleranza = storicoUnitaArboreaGaaDAO
        .getHashIdParticellaIdStoricoUnitaArboreaTolleranza(idAzienda);
      
      Iterator<Long> it = hIdUVTolleranza.keySet().iterator();
      //ciclo sulla particella all'interno della parcella
      while(it.hasNext()) 
      { 
        Long idParticella = it.next(); 
        Vector<TolleranzaVO> vIdTolleranza = hIdUVTolleranza.get(idParticella);
        //Controllo che sono tutte in tolleranza
        boolean flagTutteTolleranza = true;
        for(int i=0;i<vIdTolleranza.size();i++)
        {
          if(vIdTolleranza.get(i).getTolleranza() !=0)
          {
            flagTutteTolleranza = false;
            break;
          }
        }
        
        BigDecimal sommaAreaUV = new BigDecimal(0);
        BigDecimal sommaAreaUVAggiornate = new BigDecimal(0);
        BigDecimal superficieGis = null;
        boolean flagValoreAssoluto = false;
        if(flagTutteTolleranza)
        {       
        
          
          //prendo la prima poiche la coppia idParticela/idVarieta all'interno della particella
          //Mi ritornano sempre la stessa superficie eleggibile gis
          superficieGis = storicoUnitaArboreaGaaDAO.getSupEleggibilePlSql(
              vIdTolleranza.get(0).getIdParticellaCertificata(), vIdTolleranza.get(0).getIdCatalogoMatrice());
          
          if(superficieGis.compareTo(new BigDecimal(0)) > 0)
          {
          
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
            
            BigDecimal valAssoluto = sommaAreaUV.subtract(superficieGis);
            valAssoluto = valAssoluto.abs();
            BigDecimal confronto = (BigDecimal)commonDAO.getValoreParametroAltriDati("TOL_ELEGG_UV");         
            
            if(valAssoluto.compareTo(confronto) <= 0)
            {
              flagValoreAssoluto = true;          
            }
          }
        }
        
        if(flagValoreAssoluto)
        {  
          //Ciclo sulle uv della particella
          for(int i=0;i<vIdTolleranza.size();i++)
          {
            StoricoUnitaArboreaVO storicoUnitaArboreaVO = findStoricoUnitaArboreaConAltriVitigni(
                vIdTolleranza.get(i).getIdStoricoUnitaArborea());
            String oldArea = StringUtils.parseSuperficieField(storicoUnitaArboreaVO.getArea());
            String oldStatoUnitaArborea = storicoUnitaArboreaVO.getStatoUnitaArborea();
            TipoCausaleModificaVO oldTipoCausaleModificaVO = storicoUnitaArboreaVO.getTipoCausaleModificaVO();  
            
            BigDecimal areaUV = new BigDecimal(storicoUnitaArboreaVO.getArea().replace(',','.'));
            //riprorziono l'area della UV                
            areaUV = areaUV.multiply(superficieGis);
            areaUV = areaUV.divide(sommaAreaUV,4,BigDecimal.ROUND_HALF_UP);
            sommaAreaUVAggiornate = sommaAreaUVAggiornate.add(areaUV);
            //raccolgo gli sfrigu persi
            if(i == (vIdTolleranza.size() - 1))
            {
              if(superficieGis.compareTo(sommaAreaUVAggiornate) > 0)
              {
                superficieGis = superficieGis.subtract(sommaAreaUVAggiornate);
                areaUV = areaUV.add(superficieGis);
              }
            }
            
            storicoUnitaArboreaVO.setArea(StringUtils.parseSuperficieFieldBigDecimal(areaUV));
            
            //vado avanti solo il dato ricavato è diverso da quello resente su DB
            if(!storicoUnitaArboreaVO.getArea().equalsIgnoreCase(oldArea))
            {
              
              if (!elencoIdParticella.contains(new BigDecimal(idParticella.longValue())))
              {
                elencoIdParticella.add(new BigDecimal(idParticella.longValue()));
              }
              
              
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
              
              
            
              // Verifico se l'unità arborea selezionata è presente in una
              // dichiarazione di consistenza
              UnitaArboreaDichiarataVO[] elencoUnitaArboreeDichiarate = unitaArboreaDichiarataDAO
                  .getListUnitaArboreaDichiarataByIdStoricoUnitaArborea(
                      storicoUnitaArboreaVO.getIdStoricoUnitaArborea(), null);
              
              // Se lo è...
              if (elencoUnitaArboreeDichiarate != null
                  && elencoUnitaArboreeDichiarate.length > 0)
              {
                // Effettuo la storicizzazione del record
                storicoUnitaArboreaDAO
                    .storicizzaStoricoUnitaArborea(storicoUnitaArboreaVO
                        .getIdStoricoUnitaArborea().longValue());
                // Inserisco il nuovo record
                storicoUnitaArboreaVO.setIdStoricoUnitaArborea(null);
                storicoUnitaArboreaVO.setDataInizioValidita(new java.util.Date(
                    new Timestamp(System.currentTimeMillis()).getTime()));
                storicoUnitaArboreaVO.setDataFineValidita(null);
                storicoUnitaArboreaVO.setCampagna(DateUtils.getCurrentYear().toString());
                storicoUnitaArboreaVO.setSuperficieDaIscrivereAlbo(storicoUnitaArboreaVO.getArea());
                storicoUnitaArboreaVO.setDataAggiornamento(new java.util.Date(
                    new Timestamp(System.currentTimeMillis()).getTime()));
                storicoUnitaArboreaVO.setIdUtenteAggiornamento(ruoloUtenza
                    .getIdUtente());
                storicoUnitaArboreaVO.setRecordModificato(SolmrConstants.FLAG_S);
                storicoUnitaArboreaVO.setDataEsecuzione(null);
                storicoUnitaArboreaVO.setEsitoControllo(null);
                storicoUnitaArboreaVO.setNote(null);
                
                if(Validator.isEmpty(oldTipoCausaleModificaVO)
                  || (Validator.isNotEmpty(oldTipoCausaleModificaVO))
                  && !"S".equalsIgnoreCase(oldTipoCausaleModificaVO.getAltroProcedimento()))
                {
                  storicoUnitaArboreaVO.setIdCausaleModifica(SolmrConstants.CAUSALE_ALLINEA_GIS);
                }
                
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
                
              } //if esistono unita arboree legate a dich consistenza               
              // Altrimenti
              else
              {                  
                // Se l'utente loggato presenta una tipologia ruolo non compatibile
                // con lo stato dell'unità vitata oppure quest'ultima non è ancora
                // stata specificata
                if (!Validator.isNotEmpty(oldStatoUnitaArborea)
                    || ((ruoloUtenza.isUtenteRegionale() || ruoloUtenza
                        .isUtenteProvinciale()) && SolmrConstants.STATO_UV_PROPOSTO_CAA
                        .equalsIgnoreCase(oldStatoUnitaArborea))
                    || ((ruoloUtenza.isUtenteIntermediario() || ruoloUtenza
                        .isUtenteOPRGestore()) && SolmrConstants.STATO_UV_VALIDATO_PA
                        .equalsIgnoreCase(oldStatoUnitaArborea)))
                {
                  // Effettuo la storicizzazione del record
                  storicoUnitaArboreaDAO
                      .storicizzaStoricoUnitaArborea(storicoUnitaArboreaVO.getIdStoricoUnitaArborea());
                  // Inserisco il nuovo record
                  storicoUnitaArboreaVO.setIdStoricoUnitaArborea(null);
                  storicoUnitaArboreaVO.setDataInizioValidita(new java.util.Date(
                      new Timestamp(System.currentTimeMillis()).getTime()));
                  storicoUnitaArboreaVO.setDataFineValidita(null);
                  storicoUnitaArboreaVO.setCampagna(DateUtils.getCurrentYear().toString());
                  storicoUnitaArboreaVO.setSuperficieDaIscrivereAlbo(storicoUnitaArboreaVO.getArea());
                  storicoUnitaArboreaVO.setDataAggiornamento(new java.util.Date(
                      new Timestamp(System.currentTimeMillis()).getTime()));
                  storicoUnitaArboreaVO.setIdUtenteAggiornamento(ruoloUtenza
                      .getIdUtente());
                  storicoUnitaArboreaVO.setRecordModificato(SolmrConstants.FLAG_S);
                  storicoUnitaArboreaVO.setDataEsecuzione(null);
                  storicoUnitaArboreaVO.setEsitoControllo(null);
                  storicoUnitaArboreaVO.setNote(null);
                  
                  if(Validator.isEmpty(oldTipoCausaleModificaVO)
                      || (Validator.isNotEmpty(oldTipoCausaleModificaVO))
                      && !"S".equalsIgnoreCase(oldTipoCausaleModificaVO.getAltroProcedimento()))
                  {
                    storicoUnitaArboreaVO.setIdCausaleModifica(SolmrConstants.CAUSALE_ALLINEA_GIS);
                  }
                  
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
                else
                {
                  // Effettuo solo update del record
                  storicoUnitaArboreaVO.setDataAggiornamento(new java.util.Date(
                      new Timestamp(System.currentTimeMillis()).getTime()));
                  storicoUnitaArboreaVO.setIdUtenteAggiornamento(ruoloUtenza
                      .getIdUtente());
                  storicoUnitaArboreaVO.setDataEsecuzione(null);
                  storicoUnitaArboreaVO.setEsitoControllo(null);    
                  storicoUnitaArboreaVO.setCampagna(DateUtils.getCurrentYear().toString());
                  storicoUnitaArboreaVO.setSuperficieDaIscrivereAlbo(storicoUnitaArboreaVO.getArea());
                  storicoUnitaArboreaVO.setRecordModificato(SolmrConstants.FLAG_S);
                  storicoUnitaArboreaVO.setDataEsecuzione(null);
                  storicoUnitaArboreaVO.setEsitoControllo(null);
                  
                  if(Validator.isEmpty(oldTipoCausaleModificaVO)
                      || (Validator.isNotEmpty(oldTipoCausaleModificaVO))
                      && !"S".equalsIgnoreCase(oldTipoCausaleModificaVO.getAltroProcedimento()))
                  {
                    storicoUnitaArboreaVO.setIdCausaleModifica(SolmrConstants.CAUSALE_ALLINEA_GIS);
                  }
                  
                  
                  
                  
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
                  
                  storicoUnitaArboreaDAO
                      .updateStoricoUnitaArborea(storicoUnitaArboreaVO);
                  
                }
                
                
              }
              
            } //if differenza area
            
          }//For delle uv
          
        } //if flagTutteTolleranza      
        
      }        
          
      
      // Aggiorno lo stato dello schedario delle particelle su cui insistevano
      // UV selezionate
      if(elencoIdParticella.size() > 0)
      {
        particellaDAO
          .aggiornaSchedarioParticellaPlSql((BigDecimal[]) elencoIdParticella
              .toArray(new BigDecimal[elencoIdParticella.size()]));
      }
      
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(dae.getMessage());
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(e.getMessage());
    }
  }*/
  
  public Vector<Long> getIdConduzioneFromIdAziendaIdParticella(long idAzienda, long idParticella)
    throws SolmrException
  {
    try
    {
      return conduzioneParticellaGaaDAO.getIdConduzioneFromIdAziendaIdParticella(idAzienda, idParticella);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<AnagParticellaExcelVO> searchParticelleStabGisExcelByParameters(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda)
      throws SolmrException
  {
    try
    {
      return conduzioneParticellaGaaDAO.searchParticelleStabGisExcelByParameters(
          filtriParticellareRicercaVO, idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<AnagParticellaExcelVO> searchParticelleAvvicExcelByParameters(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda)
      throws SolmrException
  {
    try
    {
      if (filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() <= 0)
      {        
        return conduzioneParticellaGaaDAO.searchParticelleAvvicExcelByParameters(
          filtriParticellareRicercaVO, idAzienda);
      }
      else
      {
        return conduzioneParticellaGaaDAO.searchParticelleDichiarateAvvicExcelByParameters(
            filtriParticellareRicercaVO, idAzienda);
      }
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  /*public HashMap<Long,HashMap<Integer,AvvicendamentoVO>> getElencoAvvicendamento( 
      Vector<Long> vIdParticella, long idAzienda, Integer annoPartenza, int numeroAnni, 
      Long idDichiarazioneConsistenza) 
    throws SolmrException
  {
    try
    {
      return conduzioneParticellaGaaDAO.getElencoAvvicendamento(vIdParticella, idAzienda,
          annoPartenza, numeroAnni, idDichiarazioneConsistenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
    finally
    {
      // Rimuovo i dati inseriti nel db dalla insert
      sessionContext.setRollbackOnly();
    }
  }*/
  
  public Vector<Long> getIdUtilizzoFromIdIdConduzione(long idConduzioneParticella) 
    throws SolmrException
  {
    try
    {
      return utilizzoParticellaGaaDAO.getIdUtilizzoFromIdIdConduzione(idConduzioneParticella);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public BigDecimal getSumSupUtilizzoParticellaAzienda(long idAzienda, long idParticella) 
      throws SolmrException
  {
    try
    {
      return utilizzoParticellaGaaDAO.getSumSupUtilizzoParticellaAzienda(idAzienda, idParticella);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<UtilizzoParticellaVO> riepilogoTipoEfaPianoLavorazione(long idAzienda) 
      throws SolmrException
  {
    try
    {
      return utilizzoParticellaGaaDAO.riepilogoTipoEfaPianoLavorazione(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<UtilizzoParticellaVO> riepilogoTipoEfaDichiarazione(long idDichiarazioneConsistenza)  
      throws SolmrException
  {
    try
    {
      return utilizzoParticellaGaaDAO.riepilogoTipoEfaDichiarazione(idDichiarazioneConsistenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  
  public Vector<UtilizzoParticellaVO> riepilogoTipoGreeningPianoLavorazione(
      long idAzienda) throws SolmrException
  {
    try
    {
      return utilizzoParticellaGaaDAO.riepilogoTipoGreeningPianoLavorazione(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<UtilizzoParticellaVO> riepilogoTipoGreeningDichiarazione(long idDichiarazioneConsistenza) 
      throws SolmrException
  {
    try
    {
      return utilizzoParticellaGaaDAO.riepilogoTipoGreeningDichiarazione(idDichiarazioneConsistenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<TipoEfaVO> getElencoTipoEfaForAzienda(long idAzienda)
    throws SolmrException
  {
    try
    {
      return utilizzoParticellaGaaDAO.getElencoTipoEfaForAzienda(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  
  public Vector<TipoDestinazioneVO> getElencoTipoDestinazioneByMatrice(long idUtilizzo)
    throws SolmrException
  {
    try
    {
      return utilizzoParticellaGaaDAO.getElencoTipoDestinazioneByMatrice(idUtilizzo);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<TipoVarietaVO> getElencoTipoVarietaByMatrice(
      long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso, long idTipoQualitaUso)
    throws SolmrException
  {
    try
    {
      return utilizzoParticellaGaaDAO.getElencoTipoVarietaByMatrice(idUtilizzo, idTipoDestinazione, idTipoDettaglioUso, idTipoQualitaUso);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<TipoQualitaUsoVO> getElencoTipoQualitaUsoByMatrice(
      long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso) 
    throws SolmrException
  {
    try
    {
      return utilizzoParticellaGaaDAO.getElencoTipoQualitaUsoByMatrice(
          idUtilizzo, idTipoDestinazione, idTipoDettaglioUso);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public CatalogoMatriceVO getCatalogoMatriceFromMatrice(long idUtilizzo, long idVarieta,
      long idTipoDestinazione, long idTipoDettaglioUso, long idTipoQualitaUso) 
          throws SolmrException
  {
    try
    {
      return utilizzoParticellaGaaDAO.getCatalogoMatriceFromMatrice(
          idUtilizzo, idVarieta, idTipoDestinazione, idTipoDettaglioUso, idTipoQualitaUso);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public CatalogoMatriceVO getCatalogoMatriceFromPrimariKey(long idCatalogoMatrice) throws SolmrException
  {
    try
    {
      return utilizzoParticellaGaaDAO.getCatalogoMatriceFromPrimariKey(idCatalogoMatrice);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public CatalogoMatriceSeminaVO getCatalogoMatriceSeminaDefault(long idCatalogoMatrice)
      throws SolmrException
  {
    try
    {
      return utilizzoParticellaGaaDAO.getCatalogoMatriceSeminaDefault(idCatalogoMatrice);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public CatalogoMatriceSeminaVO getCatalogoMatriceSeminaByIdTipoPeriodo(long idCatalogoMatrice, 
      long idTipoPeriodoSemina) throws SolmrException
  {
    try
    {
      return utilizzoParticellaGaaDAO.getCatalogoMatriceSeminaByIdTipoPeriodo(idCatalogoMatrice, 
          idTipoPeriodoSemina);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<Long> getListIdPraticaMantenimentoPlSql(
      long idCatalogoMatrice, String flagDefault) throws SolmrException
  {
    try
    {
      return utilizzoParticellaGaaDAO.getListIdPraticaMantenimentoPlSql(
          idCatalogoMatrice, flagDefault);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<TipoSeminaVO> getElencoTipoSemina() throws SolmrException
  {
    try
    {
      return utilizzoParticellaGaaDAO.getElencoTipoSemina();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<TipoPraticaMantenimentoVO> getElencoPraticaMantenimento(
      Vector<Long> vIdMantenimento) throws SolmrException
  {
    try
    {
      return utilizzoParticellaGaaDAO.getElencoPraticaMantenimento(vIdMantenimento);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<TipoFaseAllevamentoVO> getElencoFaseAllevamento() throws SolmrException
  {
    try
    {
      return utilizzoParticellaGaaDAO.getElencoFaseAllevamento();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  /**
   * 
   * Meotdo usato nell'agiornamento massivo della percentuale di possesso
   * 
   * 
   * @param ruoloUtenza
   * @param idAzienda
   * @param percentualePossesso
   * @return
   * @throws Exception
   */
  public String cambiaPercentualePossessoMassivo(RuoloUtenza ruoloUtenza, 
      long idAzienda, BigDecimal percentualePossesso)
    throws SolmrException
  {
    String msgErrore = "";
    try
    {           
      Vector<Long> vIdParticella = pDAO.getListIdParticellaFromIdAzienda(idAzienda);
      
      for (int i = 0; i < vIdParticella.size(); i++)
      {
        StoricoParticellaVO storicoParticellaVO = storicoParticellaDAO
            .findCurrStoricoParticellaByIdParticella(vIdParticella.get(i));
        // Cerco i dati del foglio associati allo storico particella
        FoglioVO foglioVO = foglioDAO.findFoglioByParameters(storicoParticellaVO
            .getIstatComune(), storicoParticellaVO.getFoglio(),
            storicoParticellaVO.getSezione());
        
        //Se è presente una particella provvisoria non si può fare niente
        if(Validator.isEmpty(storicoParticellaVO.getParticella()))
        {
          
          return msgErrore = "Questa funzionalità non puo' avere luogo per le particelle provvisorie: "
              +strParticella(storicoParticellaVO);          
        }
        
        if((Validator.validateDouble(storicoParticellaVO.getSupCatastale(), SolmrConstants.FORMAT_SUP_CATASTALE) == null)
            && (Validator.validateDouble(storicoParticellaVO.getSuperficieGrafica(), SolmrConstants.FORMAT_SUP_CATASTALE) == null))
        {
          
          return msgErrore = "La superficie catastale / grafica delle particelle deve essere maggiore di 0: "
              +strParticella(storicoParticellaVO);
        }
        
        
        BigDecimal supConfronto = null;
        if(Validator.isNotEmpty(foglioVO)
            && Validator.isNotEmpty(foglioVO.getFlagStabilizzazione())
            && (foglioVO.getFlagStabilizzazione().compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0)
            && Validator.validateDouble(storicoParticellaVO.getSuperficieGrafica(), SolmrConstants.FORMAT_SUP_UTILIZZATA) != null 
            && Double.parseDouble(storicoParticellaVO.getSuperficieGrafica().replace(',', '.')) > 0)
        {
          supConfronto =  new BigDecimal(storicoParticellaVO.getSuperficieGrafica().replace(',','.'));
        }
        else
        {        
          if(Validator.isNotEmpty(storicoParticellaVO.getSupCatastale())
            && (new BigDecimal(storicoParticellaVO.getSupCatastale().replace(',','.')).compareTo(new BigDecimal(0)) > 0))
          {
            supConfronto =  new BigDecimal(storicoParticellaVO.getSupCatastale().replace(',','.'));
          }
          else
          {
            supConfronto =  new BigDecimal(storicoParticellaVO.getSuperficieGrafica().replace(',','.'));
          }
        }
        
           
        
        Vector<Long> vIdConduzioneNoAsservimento = conduzioneParticellaGaaDAO
            .getIdConduzioneNoAsservimentoFromIdAziendaIdParticella(idAzienda, vIdParticella.get(i));
        Vector<Long> vIdConduzioneAsservimento = conduzioneParticellaGaaDAO.getIdConduzioniAsserviteFromIdAziendaIdParticella(
            idAzienda, vIdParticella.get(i));
        
        BigDecimal sumUtilizzoParticellaCalc = null;
        BigDecimal sumUtilizzoParticella = null;
        if(Validator.isNotEmpty(vIdConduzioneNoAsservimento))
        {
          sumUtilizzoParticella = utilizzoParticellaGaaDAO.getSumSupUtilizzoParticellaAzienda(
              idAzienda, vIdParticella.get(i));
          sumUtilizzoParticellaCalc = sumUtilizzoParticella;
          sumUtilizzoParticellaCalc = sumUtilizzoParticellaCalc.divide(supConfronto,4,BigDecimal.ROUND_HALF_UP);              
          sumUtilizzoParticellaCalc = sumUtilizzoParticellaCalc.multiply(new BigDecimal(100));            
          sumUtilizzoParticellaCalc = sumUtilizzoParticellaCalc.setScale(2, BigDecimal.ROUND_HALF_UP);
           
          if(percentualePossesso.compareTo(new BigDecimal(100)) < 0)
          {  
            if(sumUtilizzoParticellaCalc.compareTo(percentualePossesso) > 0)
            {
              return msgErrore = "La percentuale di possesso è incongruente con la superficie utilizzata e la superficie di riferimento della particella: "
                  +strParticella(storicoParticellaVO);
            }
          }
        }
        
        BigDecimal sumAsservimentoParticella = null;
        BigDecimal sumAsservimentoParticellaCalc = null;
        if(Validator.isNotEmpty(vIdConduzioneAsservimento))
        {
          sumAsservimentoParticella = conduzioneParticellaGaaDAO.getSumSupCondottaAsservimentoParticellaAzienda(
              idAzienda, vIdParticella.get(i));
          sumAsservimentoParticellaCalc = sumAsservimentoParticella;
          sumAsservimentoParticellaCalc = sumAsservimentoParticellaCalc.divide(supConfronto,4,BigDecimal.ROUND_HALF_UP);              
          sumAsservimentoParticellaCalc = sumAsservimentoParticellaCalc.multiply(new BigDecimal(100));            
          sumAsservimentoParticellaCalc = sumAsservimentoParticellaCalc.setScale(2, BigDecimal.ROUND_HALF_UP);
           
          if(percentualePossesso.compareTo(new BigDecimal(100)) < 0)
          {  
            if(sumAsservimentoParticellaCalc.compareTo(percentualePossesso) > 0)
            {
              return msgErrore = "La percentuale di possesso è incongruente con la superficie condotta e la superficie di riferimento della particella: "
                  +strParticella(storicoParticellaVO);
            }
          }
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
      throw new SolmrException(dae.getMessage());
    }
    catch (Exception rae)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(rae.getMessage());
    }
    
    return msgErrore;
  }
  
  private String strParticella(StoricoParticellaVO strVO) 
    throws SolmrException
  {
    
    String str = "";
    try
    {
      str += "Comune ";
      ComuneVO comuneVO = commonDAO.getComuneByISTAT(strVO.getIstatComune());
      str += comuneVO.getDescom()+" ("+comuneVO.getSiglaProv()+")";
      if(Validator.isNotEmpty(strVO.getSezione()))
      {
        str +=", Sez. "+strVO.getSezione();
      }
      if(Validator.isNotEmpty(strVO.getFoglio()))
      {
        str +=", Fgl. "+strVO.getFoglio();
      }
      if(Validator.isNotEmpty(strVO.getParticella()))
      {
        str +=", Par. "+strVO.getParticella();
      }
      if(Validator.isNotEmpty(strVO.getSubalterno()))
      {
        str +=", Sub. "+strVO.getSubalterno();
      }
    }
    catch(NotFoundException nex)
    {
      throw new SolmrException(nex.getMessage());
    }
    catch(DataAccessException dex)
    {
      throw new SolmrException(dex.getMessage());
    }
    
    return str;
  }
  
  public BigDecimal getSumSupCondottaAsservimentoParticellaAzienda(long idAzienda, long idParticella) 
      throws SolmrException
  {
    try
    {
      return conduzioneParticellaGaaDAO.getSumSupCondottaAsservimentoParticellaAzienda(idAzienda, idParticella); 
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
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
      UtilizzoParticellaVO[] elencoUtilizzi = getListUtilizzoParticellaVOByIdConduzioneParticella(idConduzioneParticella, null, false);
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
  
  
  private UtilizzoParticellaVO[] getListUtilizzoParticellaVOByIdConduzioneParticella(
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
   * 
   * usato nell'allineamento massivo della percentuale possesso nel territoriale
   * 
   * @param ruoloUtenza
   * @param idAzienda
   * @return
   * @throws Exception
   */
  public String cambiaPercentualePossessoSupUtilizzataMassivo(RuoloUtenza ruoloUtenza, Long idAzienda)
      throws SolmrException
  {
    String msgErrore = "";
    try
    {     
      Vector<Long> vIdParticella = pDAO.getListIdParticellaFromIdAzienda(idAzienda);      
      for (int i = 0; i < vIdParticella.size(); i++)
      {       
        
        StoricoParticellaVO storicoParticellaVO = storicoParticellaDAO
            .findCurrStoricoParticellaByIdParticella(vIdParticella.get(i));
        
        // Cerco i dati del foglio associati allo storico particella
        FoglioVO foglioVO = foglioDAO.findFoglioByParameters(storicoParticellaVO
            .getIstatComune(), storicoParticellaVO.getFoglio(),
            storicoParticellaVO.getSezione());
        
        //Se è presente una particella provvisoria non si può fare niente
        if(Validator.isEmpty(storicoParticellaVO.getParticella()))
        {
          return msgErrore = "Questa funzionalità non puo' avere luogo per le particelle provvisorie: "
              +strParticella(storicoParticellaVO);
        }
        
        if((Validator.validateDouble(storicoParticellaVO.getSupCatastale(), SolmrConstants.FORMAT_SUP_CATASTALE) == null)
            && (Validator.validateDouble(storicoParticellaVO.getSuperficieGrafica(), SolmrConstants.FORMAT_SUP_CATASTALE) == null))
        {
          return msgErrore = "La superficie catastale / grafica delle particelle deve essere maggiore di 0: "
              +strParticella(storicoParticellaVO);
        }
        
        Vector<Long> vIdConduzioni = conduzioneParticellaGaaDAO.getIdConduzioneFromIdAziendaIdParticella(
            idAzienda, vIdParticella.get(i));
        
        for(int j=0; j<vIdConduzioni.size(); j++)
        {
          
          BigDecimal percentualePossesso = new BigDecimal(0);
          Long idConduzioneParticella = vIdConduzioni.get(j);
          // Recupero i dati della conduzione particella
          ConduzioneParticellaVO oldConduzioneParticella = conduzioneParticellaDAO
              .findConduzioneParticellaByPrimaryKey(idConduzioneParticella);
          StoricoParticellaVO strVO = storicoParticellaDAO
              .findStoricoParticellaVOByIdConduzioneParticella(idConduzioneParticella);
          BigDecimal supConfronto = null;
          
          if(Validator.isNotEmpty(foglioVO)
              && Validator.isNotEmpty(foglioVO.getFlagStabilizzazione())
              && (foglioVO.getFlagStabilizzazione().compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0)
              && Validator.validateDouble(storicoParticellaVO.getSuperficieGrafica(), SolmrConstants.FORMAT_SUP_UTILIZZATA) != null 
              && Double.parseDouble(storicoParticellaVO.getSuperficieGrafica().replace(',', '.')) > 0)
          {
            supConfronto =  new BigDecimal(storicoParticellaVO.getSuperficieGrafica().replace(',','.'));
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
      } //for idParticella       
        
      
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(dae.getMessage());
    }
    catch (Exception rae)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(rae.getMessage());
    }
    
    
    return msgErrore;
  }
  
  /**
   * Setta i valori per effettuare il consolidamento delle uv!!!
   * 
   * 
   * @param idAzienda
   * @param ruoloUtenza
   * @throws SolmrException
   */
  public void avviaConsolidamento(long idAzienda, RuoloUtenza ruoloUtenza)
      throws SolmrException
  {
    try
    {
      //Mi ricavo le uv consolidate
      Vector<RiepilogoCompensazioneVO> vIdStoricoCompensazioneUV = storicoUnitaArboreaGaaDAO
          .getVIdStoricoUVPerCompensazione(idAzienda);
      Vector<Long> vIdUnitaArborea = new Vector<Long>();
      for(int i=0;i<vIdStoricoCompensazioneUV.size();i++)
      {
        vIdUnitaArborea.add(vIdStoricoCompensazioneUV.get(i).getIdUnitaArborea());
      }
      
      storicoUnitaArboreaGaaDAO.consolidaUnitaArboree(vIdUnitaArborea);
      storicoUnitaArboreaGaaDAO.updateCompensazioneAziendaConsolidamento(idAzienda, ruoloUtenza.getIdUtente().longValue());
      
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(dae.getMessage());
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(e.getMessage());
    }
    
    
  }
  
  
  
  public void modificaConduzioneEleggibileUV(HashMap<Long, ConduzioneEleggibilitaVO> hPartCondEleg)
      throws SolmrException
  {
    try
    {
      Iterator<Long> iterator = hPartCondEleg.keySet().iterator();
      while (iterator.hasNext()) 
      {
        Long key = iterator.next();
        ConduzioneEleggibilitaVO conduzioneEleggibilitaVO = hPartCondEleg.get(key);
        Vector<ConduzioneEleggibilitaVO> vConduzioneEleggibilitaVO = conduzioneEleggibilitaGaaDAO
            .getElencoCondElegAttivaByIdParticella(conduzioneEleggibilitaVO.getIdAzienda(), conduzioneEleggibilitaVO.getidParticella());
        ConduzioneEleggibilitaVO conduzioneEleggibilitaVOTmp = null;
        if(vConduzioneEleggibilitaVO != null)
        {
          conduzioneEleggibilitaVOTmp = vConduzioneEleggibilitaVO.get(0);
          conduzioneEleggibilitaGaaDAO.storicizzaConduzioneEleggibilita(
              conduzioneEleggibilitaVOTmp.getIdConduzioneEleggibilita(), conduzioneEleggibilitaVO.getIdUtenteAggiornamento());
        }
        
        conduzioneEleggibilitaGaaDAO.insertConduzioneEleggibilita(conduzioneEleggibilitaVO);
        
      }
      
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(dae.getMessage());
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(e.getMessage());
    }
    
    
  }
  
  /**
   * Alienao la percentuale eleggibile delle uv.
   * Se vIdParticella è valorizzato lavoro solo sulle particelle selezionate.
   * Se null su tutte le particelle su cui insistono uv dell'azienda
   * 
   * 
   * @param idAzienda
   * @param vIdParticella
   * @param idUtente
   * @throws SolmrException
   */
  public void allineaPercUsoElegg(long idAzienda, Vector<Long> vIdParticella, long idUtente)
      throws SolmrException
  {
    try
    {      
      //Tutta l'azienda
      if(vIdParticella == null)
      {
        //Mi ricavo tutti gli idParticella dell'azienda su cui insiste almeno una uv        
        vIdParticella = pDAO.getListIdParticellaFromIdAziendaISUV(idAzienda);
      }
      
      for (int i=0; i< vIdParticella.size();i++) 
      {
        BigDecimal supVitataGis = particellaCertificataDAO.getEleggibilitaVitataByIdParticella(vIdParticella.get(i).longValue());
        if(supVitataGis.compareTo(new BigDecimal(0)) > 0)
        {
          BigDecimal sommaAreaUV = storicoUnitaArboreaDAO.getSumAreaUVParticella(idAzienda, vIdParticella.get(i).longValue());
          
          BigDecimal percentualeUtilizzo = sommaAreaUV.divide(supVitataGis,4,BigDecimal.ROUND_HALF_UP);              
          percentualeUtilizzo = percentualeUtilizzo.multiply(new BigDecimal(100));            
          percentualeUtilizzo = percentualeUtilizzo.setScale(2, BigDecimal.ROUND_HALF_UP);
          if(percentualeUtilizzo.compareTo(new BigDecimal(100)) > 0)
          {
            percentualeUtilizzo = new BigDecimal(100);
          }
          
          ConduzioneEleggibilitaVO conduzioneEleggibilitaVO = conduzioneEleggibilitaGaaDAO
              .getCondElegAttivaByIdParticellaForUv(idAzienda, vIdParticella.get(i).longValue());
          ConduzioneEleggibilitaVO conduzioneEleggibilitaVOTmp = null;
          if(conduzioneEleggibilitaVO != null)
          {
            conduzioneEleggibilitaVOTmp = conduzioneEleggibilitaVO;
            conduzioneEleggibilitaGaaDAO.storicizzaConduzioneEleggibilita(
                conduzioneEleggibilitaVOTmp.getIdConduzioneEleggibilita(), idUtente);
          }
          else
          {
            conduzioneEleggibilitaVO = new ConduzioneEleggibilitaVO();
            conduzioneEleggibilitaVO.setIdAzienda(idAzienda);
            conduzioneEleggibilitaVO.setidParticella(vIdParticella.get(i).longValue());
            conduzioneEleggibilitaVO.setIdEleggibilitaFit(SolmrConstants.ELEGGIBILITA_FIT_VINO);
          }
          
          conduzioneEleggibilitaVO.setPercentualeUtilizzo(percentualeUtilizzo);
          conduzioneEleggibilitaVO.setIdUtenteAggiornamento(idUtente);
          conduzioneEleggibilitaGaaDAO.insertConduzioneEleggibilita(conduzioneEleggibilitaVO);
        }
        
      }
      
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(dae.getMessage());
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(e.getMessage());
    }
    
    
  }
  
  
  /*****************************************************/
  /*************TipoDestinazioneUsoDAO START **************/
  /*****************************************************/
  
  public Vector<TipoDettaglioUsoVO> getListDettaglioUsoByMatrice(
      long idUtilizzo, long idTipoDestinazione) 
      throws SolmrException
  {
    try
    {
      return tipoDestinazioneUsoDAO.getListDettaglioUsoByMatrice(
          idUtilizzo, idTipoDestinazione);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public TipoDettaglioUsoVO findDettaglioUsoByPrimaryKey(
      long idTipoDettaglioUso) throws SolmrException
  {
    try
    {
      return tipoDestinazioneUsoDAO.findDettaglioUsoByPrimaryKey(idTipoDettaglioUso);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  
  /*****************************************************/
  /*************TipoDestinazioneUsoDAO END **************/
  /*****************************************************/
  
      
  
  
  
  
}
