package it.csi.solmr.business.anag;

import it.csi.solmr.dto.anag.UteVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.integration.anag.UteDAO;
import it.csi.solmr.util.SolmrLogger;

import java.util.Date;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Stateless(name="comp/env/solmr/anag/UnitaProduttiva",mappedName="comp/env/solmr/anag/UnitaProduttiva")
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UteBean implements UteLocal
{

  /**
   * 
   */
  private static final long serialVersionUID = -8247603635682668088L;
  SessionContext sessionContext;
  private transient UteDAO uteDAO = null;

 
  @Resource
  public void setSessionContext(SessionContext sessionContext)
  {
    this.sessionContext = sessionContext;
    initializeDAO();
  }

  private void initializeDAO() throws EJBException
  {
    try
    {
      uteDAO = new UteDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
    }
    catch (ResourceAccessException ex)
    {
      SolmrLogger.fatal(this, ex.getMessage());
      throw new EJBException(ex);
    }
  }

  /**
   * Metodo che mi restituisce l'elenco delle ute relative all'azienda agricola
   * selezionata
   * 
   * @param idAzienda
   * @param onlyActive
   * @param orderBy
   * @return java.util.Vector
   * @throws Exception
   */
  public Vector<UteVO> getListUteByIdAzienda(Long idAzienda,
      boolean onlyActive, String[] orderBy) throws Exception
  {
    try
    {
      return uteDAO.getListUteByIdAzienda(idAzienda, onlyActive, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
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
      Long idAzienda, long idPianoRiferimento) throws Exception
  {
    try
    {
      return uteDAO.getListUteByIdAziendaAndIdPianoRiferimento(idAzienda,
          idPianoRiferimento);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per estrarre l'unità produttiva a partire dalla sua chiave primaria
   * 
   * @param idUte
   * @return it.csi.solmr.dto.anag.UteVO
   * @throws Exception
   */
  public UteVO findUteByPrimaryKey(Long idUte) throws Exception
  {
    try
    {
      return uteDAO.findUteByPrimaryKey(idUte);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che si occupa di estrarre la minima data inizio conduzione associata
   * ad un determinato idUte
   * 
   * @param idUte
   * @return java.util.Date
   * @throws Exception
   */
  public Date getMinDataInizioConduzione(Long idUte) throws Exception
  {
    try
    {
      return uteDAO.getMinDataInizioConduzione(idUte);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che si occupa di estrarre la minima data inizio allevamento
   * associata ad un determinato idUte
   * 
   * @param idUte
   * @return java.util.Date
   * @throws Exception
   */
  public Date getMinDataInizioAllevamento(Long idUte) throws Exception
  {
    try
    {
      return uteDAO.getMinDataInizioAllevamento(idUte);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che si occupa di estrarre la minima data inizio fabbricato associata
   * ad un determinato idUte
   * 
   * @param idUte
   * @return java.util.Date
   * @throws Exception
   */
  public Date getMinDataInizioFabbricati(Long idUte) throws Exception
  {
    try
    {
      return uteDAO.getMinDataInizioFabbricati(idUte);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

}
