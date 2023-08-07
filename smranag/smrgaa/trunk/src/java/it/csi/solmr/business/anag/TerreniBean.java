package it.csi.solmr.business.anag;

import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.ParticelleVO;
import it.csi.solmr.dto.anag.TerreniVO;
import it.csi.solmr.dto.anag.terreni.TipoSettoreAbacoVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.integration.anag.TerreniDAO;
import it.csi.solmr.util.SolmrLogger;

import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Stateless(name="comp/env/solmr/anag/Terreni",mappedName="comp/env/solmr/anag/Terreni")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(value=javax.ejb.TransactionAttributeType.REQUIRED)
public class TerreniBean implements TerreniLocal
{

  /**
   * 
   */
  private static final long serialVersionUID = 1963726040369197725L;
  private transient TerreniDAO tDAO = null;
  SessionContext sessionContext;

 

  private void initializeDAO() throws EJBException
  {
    try
    {
      tDAO = new TerreniDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
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

  public Vector<Long> getAnniRilevamento() throws Exception,
      SolmrException
  {
    Vector<Long> result = null;
    try
    {
      result = tDAO.getAnniRilevamento();
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return result;
  }

  public Vector<CodeDescription> getUnitaProduttive(Long idAzienda)
      throws Exception, SolmrException
  {
    Vector<CodeDescription> result = null;
    try
    {
      result = tDAO.getUnitaProduttive(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return result;
  }

  public TerreniVO getTerreni(Long idAzienda, Long idUte, Long anno,
      String criterio) throws Exception, SolmrException
  {
    TerreniVO terreniVO = null;
    try
    {
      terreniVO = tDAO.getTotaleSuperfici(idAzienda, idUte, anno);
      if (criterio.equals(SolmrConstants.RICERCA_TERRENI_PER_COMUNE))
        terreniVO.setElencoTerreni(tDAO.getTerreniGroupByComune(idAzienda,
            idUte, anno));
      else if (criterio
          .equals(SolmrConstants.RICERCA_TERRENI_PER_TIPO_CONDUZIONE))
        terreniVO.setElencoTerreni(tDAO.getTerreniGroupByConduzione(idAzienda,
            idUte, anno));
      else if (criterio.equals(SolmrConstants.RICERCA_TERRENI_PER_UTILIZZO))
        terreniVO.setElencoTerreni(tDAO.getTerreniGroupByUtilizzo(idAzienda,
            idUte, anno));
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return terreniVO;
  }

  public Vector<Vector<Long>> getIdParticelle(Long idAzienda, Long idUte,
      Long anno, String criterio, Long valore) throws Exception,
      SolmrException
  {
    Vector<Vector<Long>> result = null;
    try
    {
      result = tDAO.getIdParticelle(idAzienda, idUte, anno, criterio, valore);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return result;
  }

  public Vector<ParticelleVO> getParticelleByIdRange(
      Vector<Vector<Long>> idRange) throws Exception, SolmrException
  {
    Vector<ParticelleVO> result = null;
    try
    {
      result = tDAO.getParticelleByIdRange(idRange);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return result;
  }
  
  public Vector<TipoSettoreAbacoVO> getListSettoreAbaco() 
      throws Exception
  {
    Vector<TipoSettoreAbacoVO> result = null;
    try
    {
      result = tDAO.getListSettoreAbaco();
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return result;
  }
}
