package it.csi.smranag.smrgaa.business;

import it.csi.smranag.smrgaa.dto.BaseCodeDescription;
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
import it.csi.smranag.smrgaa.integration.AllevamentoDAO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.SolmrLogger;

import java.util.Date;
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

@Stateless(name=AllevamentoBean.jndiName,mappedName=AllevamentoBean.jndiName)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)

public class AllevamentoBean implements AllevamentoLocal
{
  /**
   * Serial version UID
   */
  private static final long        serialVersionUID = -4871081274087776052L;

  SessionContext                   sessionContext;
  public final static String jndiName="comp/env/solmr/gaa/Allevamento";

  private transient AllevamentoDAO aDAO;

  private void initializeDAO() throws EJBException
  {
    try
    {
      aDAO = new AllevamentoDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
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

  public TipoSottoCategoriaAnimale[] getTipiSottoCategoriaAnimale(long idCategoriaAnimale) throws SolmrException
  {
    try
    {
      return aDAO.getTipiSottoCategoriaAnimale(idCategoriaAnimale);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }

  public TipoSottoCategoriaAnimale getTipoSottoCategoriaAnimale(long idSottoCategoriaAnimale) throws SolmrException
  {
    try
    {
      return aDAO.getTipoSottoCategoriaAnimale(idSottoCategoriaAnimale);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }

  public TipoMungitura[] getTipiMungitura() throws SolmrException
  {
    try
    {
      return aDAO.getTipiMungitura();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<SottoCategoriaAllevamento> getTipiSottoCategoriaAllevamento(long idAllevamento) throws SolmrException
  {
    try
    {
      return aDAO.getTipiSottoCategoriaAllevamento(idAllevamento);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<StabulazioneTrattamento> getStabulazioni(long idAllevamento,boolean modifica) throws SolmrException
  {
    try
    {
      return aDAO.getStabulazioni(idAllevamento,modifica);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public BaseCodeDescription[] getTipiStabulazione(long idSottoCategoriaAnimale) throws SolmrException
  {
    try
    {
      return aDAO.getTipiStabulazione(idSottoCategoriaAnimale);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<TipoTrattamento> getTipiTrattamento() throws SolmrException
  {
    try
    {
      return aDAO.getTipiTrattamento();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }

  public TipoTrattamento getTipoTrattamento(long idTrattamento) throws SolmrException
  {
    try
    {
      return aDAO.getTipoTrattamento(idTrattamento);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public SottoCategoriaAnimStab getSottoCategoriaAnimStab(long idSottoCategoriaAnimale,boolean palabile, long idStabulazione) throws SolmrException
  {
    try
    {
      return aDAO.getSottoCategoriaAnimStab(idSottoCategoriaAnimale,palabile, idStabulazione);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  
  public Vector<AllevamentoVO> getElencoAllevamentiExcel(long idAzienda, Date dataInserimentiDich, Long idUte)  
    throws SolmrException
  {
    try
    {
      Vector<AllevamentoVO> vAllevamenti = null;
      vAllevamenti = aDAO.getElencoAllevamenti(idAzienda, dataInserimentiDich, idUte);
      if(vAllevamenti != null)
      {
        for(int i=0;i<vAllevamenti.size();i++)
        {
          AllevamentoVO allVO = (AllevamentoVO)vAllevamenti.get(i);
          Vector<StabulazioneTrattamento> vStabulazioni = aDAO.getElencoStabulazioni(allVO
              .getSottoCategoriaAllevamentoVO().getIdSottoCategoriaAllevamento());
          /*if(vStabulazioni != null)
          {
            for(int j=0;j<vStabulazioni.size();j++)
            {
              StabulazioneTrattamento staTrattVO = (StabulazioneTrattamento)vStabulazioni.get(j);
              Vector<EffluenteProdotto> vEffluenti = aDAO.getElencoEffluenteProdotto(staTrattVO
                  .getIdStabulazioneTrattamento(), true);
              staTrattVO.setVEffluenteProdotto(vEffluenti);
            }
          }*/
          allVO.getSottoCategoriaAllevamentoVO().setVStabulazioneTrattamentoVO(vStabulazioni);          
        }
      }
      
      return vAllevamenti;
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<AllevamentoBioVO> getAllevamentiBio(Date dataInserimentoDichiarazione, long idAllevamento) 
    throws SolmrException
  {
    try
    {
      return aDAO.getAllevamentiBio(dataInserimentoDichiarazione, idAllevamento);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<TipoDestinoAcquaLavaggio> getElencoDestAcquaLavaggio()  
    throws SolmrException
  {
    try
    {
      return aDAO.getElencoDestAcquaLavaggio();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<AllevamentoAcquaLavaggio> getElencoAllevamentoAcquaLavaggio(long idAllevamento)
    throws SolmrException
  {
    try
    {
      return aDAO.getElencoAllevamentoAcquaLavaggio(idAllevamento);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<Long> getElencoSpecieAzienda(long idAzienda) 
    throws SolmrException
  {
    try
    {
      return aDAO.getElencoSpecieAzienda(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  /*public HashMap<Long,BigDecimal> getAllevamentoTotAcquaLavaggio(long idAzienda)  
    throws SolmrException
  {
    try
    {
      return aDAO.getAllevamentoTotAcquaLavaggio(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }*/
  
  public Vector<AllevamentoVO> getElencoAllevamentiSian(String cuaa, 
      long idTipoSpecie, String codAziendaZoo)  
    throws SolmrException
  {
    try
    {
      return aDAO.getElencoAllevamentiSian(cuaa, idTipoSpecie, codAziendaZoo);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<EsitoControlloAllevamento> getElencoEsitoControlloAllevamento(long idAllevamento)
    throws SolmrException
  {
    try
    {
      return aDAO.getElencoEsitoControlloAllevamento(idAllevamento);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public HashMap<Long, ControlloAllevamenti> getEsitoControlliAllevamentiAzienda(long idAzienda)
    throws SolmrException
  {
    try
    {
      return aDAO.getEsitoControlliAllevamentiAzienda(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public HashMap<Long, ControlloAllevamenti> getSegnalazioniControlliAllevamentiAzienda(long idAzienda)
    throws SolmrException
  {
    try
    {
      return aDAO.getSegnalazioniControlliAllevamentiAzienda(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
}
