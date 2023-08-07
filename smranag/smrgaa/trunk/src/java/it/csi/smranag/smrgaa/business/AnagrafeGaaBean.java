package it.csi.smranag.smrgaa.business;

import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni;
import it.csi.smranag.smrgaa.dto.PlSqlCodeDescription;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaAtecoSecVO;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaColVarExcelVO;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaSezioniVO;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaUmaExcelVO;
import it.csi.smranag.smrgaa.dto.anagrafe.GruppoGreeningVO;
import it.csi.smranag.smrgaa.dto.anagrafe.PlSqlCalcoloOteVO;
import it.csi.smranag.smrgaa.dto.anagrafe.TipoAttivitaOteVO;
import it.csi.smranag.smrgaa.dto.anagrafe.TipoDimensioneAziendaVO;
import it.csi.smranag.smrgaa.dto.anagrafe.TipoInfoAggiuntivaVO;
import it.csi.smranag.smrgaa.dto.anagrafe.TipoUdeVO;
import it.csi.smranag.smrgaa.dto.search.FiltriRicercaAziendeCollegateVO;
import it.csi.smranag.smrgaa.dto.search.FiltriRicercaVariazioniAziendaliVO;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaAziendeCollegateVO;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaVariazioniAziendaliVO;
import it.csi.smranag.smrgaa.integration.AnagrafeGaaDAO;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.ConsistenzaVO;
import it.csi.solmr.dto.anag.DelegaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.integration.anag.ConsistenzaDAO;
import it.csi.solmr.util.SolmrLogger;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
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

@Stateless(name=AnagrafeGaaBean.jndiName,mappedName=AnagrafeGaaBean.jndiName)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AnagrafeGaaBean implements AnagrafeGaaLocal
{

  

  /**
   * 
   */
  private static final long serialVersionUID = 6132353026925646587L;
  public final static String jndiName="comp/env/solmr/gaa/AnagrafeGaa";
  

  SessionContext                   sessionContext;

  private transient AnagrafeGaaDAO aDAO;
  private transient ConsistenzaDAO consistenzaDAO;

  private void initializeDAO() throws EJBException
  {
    try
    {
      aDAO = new AnagrafeGaaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      consistenzaDAO = new ConsistenzaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
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
  
  public PlSqlCalcoloOteVO calcolaIneaPlSql(long idAzienda) 
    throws SolmrException
  {
    
    PlSqlCalcoloOteVO plSqlObl = null;
    
    try
    {
      plSqlObl = aDAO.calcolaIneaPlSql(idAzienda, null);
      if(plSqlObl.getIdUde() != null)
      {
        TipoUdeVO tipoUdeVO = aDAO.getTipoUdeByPrimaryKey(plSqlObl.getIdUde().longValue());
        plSqlObl.setClasseUde(new Long(tipoUdeVO.getClasseUde()));
      }
      if(plSqlObl.getIdAttivitaOte() != null)
      {
        TipoAttivitaOteVO tipoAttivitaOteVO = aDAO.getTipoAttivitaOteByPrimaryKey(
            plSqlObl.getIdAttivitaOte().longValue());
        plSqlObl.setDescOte(tipoAttivitaOteVO.getDescrizione());
        plSqlObl.setCodiceOte(tipoAttivitaOteVO.getCodice());
      }
      
      return plSqlObl;
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
    
  }
  
  
  public PlSqlCalcoloOteVO calcolaUluPlSql(long idAzienda, 
      Long idUde) 
    throws SolmrException
  {    
    try
    {
      return aDAO.calcolaUluPlSql(idAzienda, idUde);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
    
  }
  
  public Vector<AziendaAtecoSecVO> getListActiveAziendaAtecoSecByIdAzienda(long idAzienda)
    throws SolmrException
  {
    try
    {
      return aDAO.getListActiveAziendaAtecoSecByIdAzienda(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<AziendaAtecoSecVO> getListActiveAziendaAtecoSecByIdAziendaAndValid(long idAzienda,
      long idDichiarazioneConsistenza) throws SolmrException
  {
    try
    {
      return aDAO.getListActiveAziendaAtecoSecByIdAziendaAndValid(idAzienda, idDichiarazioneConsistenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public CodeDescription getAttivitaAtecoAllaValid(long idAzienda,
      long idDichiarazioneConsistenza) throws SolmrException
  {
    try
    {
      return aDAO.getAttivitaAtecoAllaValid(idAzienda, idDichiarazioneConsistenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<TipoDimensioneAziendaVO> getListActiveTipoDimensioneAzienda() 
    throws SolmrException
  {
    try
    {
      return aDAO.getListActiveTipoDimensioneAzienda();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public TipoAttivitaOteVO getTipoAttivitaOteByPrimaryKey(long idAttivitaOte)
    throws SolmrException
  {
    try
    {
      return aDAO.getTipoAttivitaOteByPrimaryKey(idAttivitaOte);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public long[] ricercaIdAziendeCollegate(
      FiltriRicercaAziendeCollegateVO filtriRicercaAziendeCollegateVO) throws SolmrException
  {
    try
    {
      return aDAO.ricercaIdAziendeCollegate(filtriRicercaAziendeCollegateVO);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public RigaRicercaAziendeCollegateVO[] getRigheRicercaAziendeCollegateByIdAziendaCollegata(
      long ids[]) throws SolmrException
  {
    try
    {
      return aDAO.getRigheRicercaAziendeCollegateByIdAziendaCollegata(ids);
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
  }
  
  public int ricercaNumVariazioni(FiltriRicercaVariazioniAziendaliVO filtriRicercaVariazioniAziendaliVO,
                                  UtenteAbilitazioni utenteAbilitazioni, RuoloUtenza ruoloUtenza) 
    throws SolmrException
  {
    try
    {
      return aDAO.ricercaNumVariazioni(filtriRicercaVariazioniAziendaliVO, utenteAbilitazioni, ruoloUtenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }  
  
  public void insertVisioneVariazione(Vector<Long> elencoIdPresaVisione, RuoloUtenza ruoloUtenza) 
    throws SolmrException
  {
    try
    {
      aDAO.insertVisioneVariazione(elencoIdPresaVisione, ruoloUtenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }  
  
  public RigaRicercaVariazioniAziendaliVO[] getRigheRicercaVariazioni(FiltriRicercaVariazioniAziendaliVO filtriRicercaVariazioniAziendaliVO,
                                                                      UtenteAbilitazioni utenteAbilitazioni, RuoloUtenza ruoloUtenza, boolean excel) 
    throws SolmrException
  {
    try
    {
      return aDAO.getRigheRicercaVariazioni(filtriRicercaVariazioniAziendaliVO, utenteAbilitazioni, ruoloUtenza, excel);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public RigaRicercaVariazioniAziendaliVO[] getRigheVariazioniVisione(Vector<Long> elencoIdPresaVisione,FiltriRicercaVariazioniAziendaliVO filtriRicercaVariazioniAziendaliVO) 
    throws SolmrException
  {
    try
    {
      return aDAO.getRigheVariazioniVisione(elencoIdPresaVisione,filtriRicercaVariazioniAziendaliVO) ;
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public boolean isPresaVisione(Vector<Long> elencoIdPresaVisione, RuoloUtenza ruoloUtenza)
    throws SolmrException
  {
    try
    {
      return aDAO.isPresaVisione(elencoIdPresaVisione, ruoloUtenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public BigDecimal[] getTOTSupCondottaAndSAU(long idAzienda) throws SolmrException
  {
    try
    {
      return aDAO.getTOTSupCondottaAndSAU(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public HashMap<Long,DelegaVO> getDelegaAndIntermediario(long ids[]) throws SolmrException
  {
    try
    {
      return aDAO.getDelegaAndIntermediario(ids);
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
  }
  
  public boolean isAziendeCollegataFiglia(
      long idAzienda, long idAziendaSearch) throws SolmrException
  {
    try
    {
      return aDAO.isAziendeCollegataFiglia(
          idAzienda, idAziendaSearch);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public boolean isAziendeCollegataMenu(long idAzienda) throws SolmrException
  {
    try
    {
      return aDAO.isAziendeCollegataMenu(
          idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public Vector<AziendaUmaExcelVO> getScaricoExcelElencoSociUma(
      long idAzienda) throws SolmrException
  {
    try
    {
      return aDAO.getScaricoExcelElencoSociUma(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public Vector<AziendaColVarExcelVO> getScaricoExcelElencoSociColturaVarieta(
      long idAzienda) throws SolmrException
  {
    try
    {
      return aDAO.getScaricoExcelElencoSociColturaVarieta(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public Vector<AziendaColVarExcelVO> getScaricoExcelElencoSociFruttaGuscio(
      long idAzienda) throws SolmrException
  {
    try
    {
      return aDAO.getScaricoExcelElencoSociFruttaGuscio(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public boolean isInAziendaProvenienza(long idAzienda) 
      throws SolmrException
  {
    try
    {
      return aDAO.isInAziendaProvenienza(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public Vector<TipoInfoAggiuntivaVO> getTipoInfoAggiuntive(long idAzienda)
    throws SolmrException
  {
    try
    {
      return aDAO.getTipoInfoAggiuntive(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public Vector<GruppoGreeningVO> getListGruppiGreening(Long idDichConsistenza, 
  		Long idAzienda) throws SolmrException {
  	Vector<GruppoGreeningVO> result = null;
  	
  	try {
  		ConsistenzaVO consistenza = null;
  		
  		if (idDichConsistenza!=null) {
  			consistenza = consistenzaDAO.findDichiarazioneConsistenzaByPrimaryKey(idDichConsistenza);
  		}
  		
  		if (consistenza!=null) {
  			result = aDAO.getListGruppiGreening(idDichConsistenza, consistenza.getDataInserimentoDichiarazione(), idAzienda);
  		}else {
  			result = aDAO.getListGruppiGreening(null, null, idAzienda);
  		}
      
    }catch (DataAccessException ex) {
      throw new SolmrException(ex.getMessage());
    }
  	
  	return result;
  }
  
  public PlSqlCodeDescription calcolaGreeningPlSql(long idAzienda, long IdUtente, 
  		Long idDichiarazioneConsistenza) throws SolmrException 
  {
  	try 
  	{
      return aDAO.calcolaGreeningPlSql(idAzienda, IdUtente, idDichiarazioneConsistenza);
    }
  	catch (DataAccessException ex) 
  	{
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public PlSqlCodeDescription calcolaEfaPlSql(long idAzienda, Long idDichiarazioneConsistenza,
      long IdUtente) throws SolmrException 
  {
    try 
    {
      return aDAO.calcolaEfaPlSql(idAzienda, idDichiarazioneConsistenza, IdUtente);
    }
    catch (DataAccessException ex) 
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public Vector<AziendaSezioniVO> getListActiveAziendaSezioniByIdAzienda(long idAzienda)
    throws SolmrException 
  {
    try 
    {
      return aDAO.getListActiveAziendaSezioniByIdAzienda(idAzienda);
    }
    catch (DataAccessException ex) 
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
}
