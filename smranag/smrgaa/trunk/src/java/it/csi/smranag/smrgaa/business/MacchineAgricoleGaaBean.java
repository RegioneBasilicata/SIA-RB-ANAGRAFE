package it.csi.smranag.smrgaa.business;

import it.csi.smranag.smrgaa.dto.uma.TipoGenereMacchinaGaaVO;
import it.csi.smranag.smrgaa.dto.search.FiltriRicercaMacchineAgricoleVO;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaMacchineAgricoleVO;
import it.csi.smranag.smrgaa.dto.uma.MacchinaGaaVO;
import it.csi.smranag.smrgaa.dto.uma.PossessoMacchinaVO;
import it.csi.smranag.smrgaa.dto.uma.TipoCategoriaGaaVO;
import it.csi.smranag.smrgaa.dto.uma.TipoFormaPossessoGaaVO;
import it.csi.smranag.smrgaa.dto.uma.TipoMacchinaGaaVO;
import it.csi.smranag.smrgaa.integration.AnagrafeGaaDAO;
import it.csi.smranag.smrgaa.integration.MacchineAgricoleGaaDAO;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.UteVO;
import it.csi.solmr.dto.uma.MacchinaVO;
import it.csi.solmr.dto.uma.PossessoVO;
import it.csi.solmr.dto.uma.UtilizzoVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.integration.anag.FascicoloDAO;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.math.BigDecimal;
import java.util.Date;
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
import javax.naming.InitialContext;

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
@Stateless(name= MacchineAgricoleGaaBean.jndiName,mappedName=  MacchineAgricoleGaaBean.jndiName)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED )
public class MacchineAgricoleGaaBean implements MacchineAgricoleGaaLocal
{
  

  /**
   * 
   */
  private static final long serialVersionUID = 554417831084945113L;
  public final static String jndiName="comp/env/solmr/gaa/MacchineAgricole";

  SessionContext                   sessionContext;

  private transient MacchineAgricoleGaaDAO macchineAgricoleDAO;
  private transient FascicoloDAO fascicoloDAO;
  private transient AnagrafeGaaDAO anagrafeGaaDAO;

  private void initializeDAO() throws EJBException
  {
    try
    {
      macchineAgricoleDAO = new MacchineAgricoleGaaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      fascicoloDAO = new FascicoloDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      anagrafeGaaDAO = new AnagrafeGaaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
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

  public long[] ricercaIdPossessoMacchina(
      FiltriRicercaMacchineAgricoleVO filtriRicercaMacchineAgricoleVO) throws SolmrException
  {
    try
    {
      return macchineAgricoleDAO.ricercaIdPossessoMacchina(filtriRicercaMacchineAgricoleVO);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public long[] ricercaIdPossessoMacchinaImport(
      FiltriRicercaMacchineAgricoleVO filtriRicercaMacchineAgricoleVO) throws SolmrException
  {
    try
    {
      return macchineAgricoleDAO.ricercaIdPossessoMacchinaImport(filtriRicercaMacchineAgricoleVO);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public RigaRicercaMacchineAgricoleVO[] getRigheRicercaMacchineAgricoleById(
      long ids[]) throws SolmrException
  {
    try
    {
      return macchineAgricoleDAO.getRigheRicercaMacchineAgricoleById(ids);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public Vector<PossessoMacchinaVO> getElencoMacchineAgricoleForStampa(
      long idAzienda, Date dataInserimentoValidazione) throws SolmrException
  {
    try
    {
      return macchineAgricoleDAO.getElencoMacchineAgricoleForStampa(idAzienda, dataInserimentoValidazione);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public void popolaTabelleMacchineAgricoleConServizio(
      long idAzienda) throws SolmrException
  {
    try
    {
      
      anagrafeGaaDAO.updateDataAggiornamentoUma(idAzienda);
      
      InitialContext ctx = new InitialContext();
      UmaServGaaLocal umaServGaaLocal = ((UmaServGaaLocal) ctx.lookup("java:app/smrgaa/"+UmaServGaaBean.jndiName));
      //mi estraggo tutto da UMA
      Vector<MacchinaVO> vMacchine = umaServGaaLocal.serviceGetElencoMacchineByIdAzienda(
          idAzienda, new Boolean(true), null);
      //cancello sulle nostre tabelle tutto
      Vector<Long> vIdMacchine = macchineAgricoleDAO.getListUniqueIdMacchinaForDelete(idAzienda);
      if(vIdMacchine != null)
      {
        macchineAgricoleDAO.deleteNumeroTarga(vIdMacchine);
        macchineAgricoleDAO.deletePosessoMacchina(vIdMacchine);
        macchineAgricoleDAO.deleteMacchina(vIdMacchine);
      }
      //inserisco      
      if(vMacchine != null)
      {
        for(int i=0;i<vMacchine.size();i++)
        {
          MacchinaVO macchinaVO = vMacchine.get(i);          
          
          //S id_genere sono null nn inserisco
          Long idGenereMacchina = null;
          if(macchinaVO.getMatriceVO() != null
              && macchinaVO.getMatriceVO().getIdGenereMacchina() != null)
          {
            idGenereMacchina =  macchinaVO.getMatriceVO().getIdGenereMacchinaLong();
          }
          else
          {
            if(macchinaVO.getDatiMacchinaVO() != null
                && macchinaVO.getDatiMacchinaVO().getIdGenereMacchinaLong() != null)
            {
              idGenereMacchina = macchinaVO.getDatiMacchinaVO().getIdGenereMacchinaLong();
            }
          }
          Long idCategoria = null;
          if(macchinaVO.getMatriceVO() != null
            && macchinaVO.getMatriceVO().getIdCategoria() != null)
          {
              idCategoria = macchinaVO.getMatriceVO().getIdCategoriaLong();
          }
          else
          {
            if(macchinaVO.getDatiMacchinaVO() != null
              && macchinaVO.getDatiMacchinaVO().getIdCategoriaLong() != null)
            {
              idCategoria = macchinaVO.getDatiMacchinaVO().getIdCategoriaLong();
            }
          }
          
          if(idGenereMacchina != null)
          { 
            TipoGenereMacchinaGaaVO tipoGenereMacchinaGaaVO = macchineAgricoleDAO.getTipoGenereMacchinaByPrimaryKey(idGenereMacchina);
            if("S".equalsIgnoreCase(tipoGenereMacchinaGaaVO.getFlagImportabile()))
            {
              //Controllo se la marca è presente sul db!!!
              if(macchinaVO.getMatriceVO() != null
                  && macchinaVO.getMatriceVO().getIdMarca() != null)
              {
                //Se nn esiste inserisco tra le decodifiche
                if(!macchineAgricoleDAO.existMarcaById(macchinaVO.getMatriceVO().getIdMarcaLong()))
                {
                  macchineAgricoleDAO.insertTipoMarcaFromUma(macchinaVO.getMatriceVO());
                }
              }
            
              Long idMacchina = macchineAgricoleDAO.insertMacchina(macchinaVO, idGenereMacchina, idCategoria);
              if(Validator.isNotEmpty(macchinaVO.getTargaCorrente()))
                macchineAgricoleDAO.insertNumeroTarga(macchinaVO.getTargaCorrente(), idMacchina);
              Vector<Long> vIdAzienda = umaServGaaLocal.serviceGetElencoAziendeUtilizzatrici(macchinaVO.getIdMacchinaLong());
              for(int j=0;j<vIdAzienda.size();j++)
              {
                Long idAziendaUma = vIdAzienda.get(j);
                UtilizzoVO utilizzoVO = umaServGaaLocal.serviceGetUtilizzoByIdMacchinaAndIdAzienda(macchinaVO.getIdMacchinaLong(), idAziendaUma);
                Long idUte = null;
                //se nn trova ute va in eccezione...e lo fà uscire...
                //Quindi messo catch...
                Vector<UteVO> vUte = null;
                try
                {
                  vUte = fascicoloDAO.getUTE(idAziendaUma, null);
                }
                catch(Exception ex)
                {}
                //prendo il primo (a caso)
                if(Validator.isNotEmpty(vUte))
                {
                  idUte = vUte.get(0).getIdUte();
                  if(Validator.isNotEmpty(utilizzoVO))
                  {
                    for(int k=0;k<utilizzoVO.getPossesso().length;k++)
                    {
                      PossessoVO possessoVO = (PossessoVO)utilizzoVO.getPossesso()[k];
                      macchineAgricoleDAO.insertPossessoMacchina(utilizzoVO, possessoVO, idMacchina, idUte);
                    }
                  }
                }
              }
              
            }
          }
          
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
  }
  
  public Vector<TipoGenereMacchinaGaaVO> getElencoTipoGenereMacchina() throws SolmrException
  {
    try
    {
      return macchineAgricoleDAO.getElencoTipoGenereMacchina();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public TipoGenereMacchinaGaaVO getTipoGenereMacchinaByPrimaryKey(long idGenereMacchina) throws SolmrException
  {
    try
    {
      return macchineAgricoleDAO.getTipoGenereMacchinaByPrimaryKey(idGenereMacchina);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public PossessoMacchinaVO getPosessoMacchinaFromId(long idPossessoMacchina)
      throws SolmrException
  {
    try
    {
      return macchineAgricoleDAO.getPosessoMacchinaFromId(idPossessoMacchina);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public Vector<PossessoMacchinaVO> getElencoDitteUtilizzatrici(
      long idMacchina, Date dataScarico) throws SolmrException
  {
    try
    {
      return macchineAgricoleDAO.getElencoDitteUtilizzatrici(idMacchina, dataScarico);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public Vector<PossessoMacchinaVO> getElencoPossessoDitteUtilizzatrici(
      long idMacchina, long idAzienda) throws SolmrException
  {
    try
    {
      return macchineAgricoleDAO.getElencoPossessoDitteUtilizzatrici(idMacchina, idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public Vector<TipoMacchinaGaaVO> getElencoTipoMacchina(String flaIrroratrice) 
    throws SolmrException
  {
    try
    {
      return macchineAgricoleDAO.getElencoTipoMacchina(flaIrroratrice);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public Vector<TipoGenereMacchinaGaaVO> getElencoTipoGenereMacchinaFromRuolo(
      long idTipoMacchina, String codiceRuolo) throws SolmrException
  {
    try
    {
      return macchineAgricoleDAO.getElencoTipoGenereMacchinaFromRuolo(idTipoMacchina, codiceRuolo);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public Vector<TipoCategoriaGaaVO> getElencoTipoCategoria(long idGenereMacchina) 
    throws SolmrException
  {
    try
    {
      return macchineAgricoleDAO.getElencoTipoCategoria(idGenereMacchina);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public Vector<CodeDescription> getElencoTipoMarca()
    throws SolmrException
  {
    try
    {
      return macchineAgricoleDAO.getElencoTipoMarca();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public Vector<CodeDescription> getElencoTipoMarcaByIdGenere(long idGenereMacchina) 
    throws SolmrException
  {
    try
    {
      return macchineAgricoleDAO.getElencoTipoMarcaByIdGenere(idGenereMacchina);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public Vector<TipoFormaPossessoGaaVO> getElencoTipoFormaPossesso()
    throws SolmrException
  {
    try
    {
      return macchineAgricoleDAO.getElencoTipoFormaPossesso();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public boolean existsMotoreAgricolo(Long idMarca, String modello,
      Integer annoCostruzione, String matricolaTelaio) throws SolmrException
  {
    try
    {
      return macchineAgricoleDAO.existsMotoreAgricolo(
          idMarca, modello, annoCostruzione, matricolaTelaio);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public void inserisciMacchinaAgricola(PossessoMacchinaVO possessoMacchinaVO) 
      throws SolmrException
  {
    try
    {
      MacchinaGaaVO macchinaVO = possessoMacchinaVO.getMacchinaVO();
      Long idMacchina = macchineAgricoleDAO.insertMacchinaGaa(macchinaVO);
      macchineAgricoleDAO.insertPossessoMacchinaGaa(possessoMacchinaVO, idMacchina);
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  
  public void modificaMacchinaAgricola(long idPossessoMacchina, PossessoMacchinaVO possessoMacchinaVO, boolean flagModUte) 
      throws SolmrException
  {
    try
    {
      PossessoMacchinaVO possessoMacchinaVOOld = macchineAgricoleDAO.getPosessoMacchinaFromId(idPossessoMacchina);
      //cambiati i dati macchina aggiorno db_macchina_gaa
      if(!possessoMacchinaVO.getMacchinaVO().equalsDatiMacchinaConNote(possessoMacchinaVOOld.getMacchinaVO()))
      {
        possessoMacchinaVO.getMacchinaVO().setIdMacchina(possessoMacchinaVOOld.getMacchinaVO().getIdMacchina());
        macchineAgricoleDAO.updateMacchinaGaa(possessoMacchinaVO.getMacchinaVO());
      }
      
      if(flagModUte)
      {        
        macchineAgricoleDAO.storicizzaPossessoMacchinaPerScarico(idPossessoMacchina, 
            possessoMacchinaVO.getDataCarico(), new Long(100).longValue(), 
            possessoMacchinaVO.getExtIdUtenteAggiornamento());
        macchineAgricoleDAO.insertPossessoMacchinaGaa(possessoMacchinaVO, possessoMacchinaVOOld.getMacchinaVO().getIdMacchina());
      }
      else
      {
        if(Validator.isNotEmpty(possessoMacchinaVO.getDataScarico()))
        {
          macchineAgricoleDAO.storicizzaPossessoMacchinaPerScarico(idPossessoMacchina, 
              possessoMacchinaVO.getDataScarico(), possessoMacchinaVO.getIdScarico().longValue(), 
              possessoMacchinaVO.getExtIdUtenteAggiornamento());
        }
        else if(!possessoMacchinaVO.equalsDatiPossesso(possessoMacchinaVOOld))
        {
          macchineAgricoleDAO.storicizzaPossessoMacchina(idPossessoMacchina);
          possessoMacchinaVO.setDataCarico(possessoMacchinaVOOld.getDataCarico());
          macchineAgricoleDAO.insertPossessoMacchinaGaa(possessoMacchinaVO, possessoMacchinaVOOld.getMacchinaVO().getIdMacchina());
        }
        
      }
      
      
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  
  public void importaMacchinaAgricola(long idPossessoMacchina, PossessoMacchinaVO possessoMacchinaVO) 
      throws SolmrException
  {
    try
    {
      PossessoMacchinaVO possessoMacchinaVOOld = macchineAgricoleDAO.getPosessoMacchinaFromId(idPossessoMacchina);
      //cambiati i dati macchina aggiorno db_macchina_gaa
      if(!possessoMacchinaVO.getMacchinaVO().equalsDatiMacchinaConNote(possessoMacchinaVOOld.getMacchinaVO()))
      {
        possessoMacchinaVO.getMacchinaVO().setIdMacchina(possessoMacchinaVOOld.getMacchinaVO().getIdMacchina());
        macchineAgricoleDAO.updateMacchinaGaa(possessoMacchinaVO.getMacchinaVO());
      }
      
      if(possessoMacchinaVO.getIdUte().compareTo(possessoMacchinaVOOld.getIdUte()) == 0)
      {
        macchineAgricoleDAO.storicizzaPossessoMacchina(possessoMacchinaVOOld.getIdPossessoMacchina());
      }
      macchineAgricoleDAO.insertPossessoMacchinaGaa(possessoMacchinaVO, possessoMacchinaVOOld.getMacchinaVO().getIdMacchina());   
      
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public boolean isMacchinaModificabile(long idPossessoMacchina, String codiceRuolo)
      throws SolmrException
  {
    try
    {
      return macchineAgricoleDAO.isMacchinaModificabile(idPossessoMacchina, codiceRuolo);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public boolean isMacchinaPossMultiplo(long idMacchina)
      throws SolmrException
  {
    try
    {
      return macchineAgricoleDAO.isMacchinaPossMultiplo(idMacchina);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public Vector<CodeDescription> getElencoTipoScarico()
    throws SolmrException
  {
    try
    {
      return macchineAgricoleDAO.getElencoTipoScarico();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public boolean isMacchinaGiaPossesso(long idMacchina, long idAzienda)
      throws SolmrException
  {
    try
    {
      return macchineAgricoleDAO.isMacchinaGiaPossesso(idMacchina, idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public BigDecimal percMacchinaGiaInPossesso(long idMacchina)
      throws SolmrException
  {
    try
    {
      return macchineAgricoleDAO.percMacchinaGiaInPossesso(idMacchina);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
  public TipoCategoriaGaaVO getTipoCategoriaFromPK(long idCategoria) 
      throws SolmrException
  {
    try
    {
      return macchineAgricoleDAO.getTipoCategoriaFromPK(idCategoria);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
  }
  
 
  
}
