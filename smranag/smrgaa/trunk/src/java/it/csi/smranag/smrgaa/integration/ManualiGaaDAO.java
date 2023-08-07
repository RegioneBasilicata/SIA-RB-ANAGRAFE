package it.csi.smranag.smrgaa.integration;


import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.dto.manuali.ManualeVO;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;


public class ManualiGaaDAO extends it.csi.solmr.integration.BaseDAO {
  
  
  public ManualiGaaDAO() throws ResourceAccessException{
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public ManualiGaaDAO(String refName) throws ResourceAccessException 
  {
    super(refName);
  }
  
  
 /**
  * restituisce l'elenco dei manuali visibili relativamente ad un ruolo
  * 
  * 
  * 
  * @param descRuolo
  * @return
  * @throws DataAccessException
  */
  public Vector<ManualeVO> getElencoManualiFromRuoli(String descRuolo) throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<ManualeVO> results = null;
    ManualeVO manualeVO = null;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[ManualiGaaDAO::getElencoManualiFromRuoli] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
      		"SELECT TM.ID_MANUALE, " +
      		"       TM.DATA_VERSIONE, " +
      		"       TM.VERSIONE, " +
      		"       TM.DESCRIZIONE AS DESC_MANUALE, " +
      		"       SM.ID_SEZIONE, " + 
      		"       SM.DESCRIZIONE AS DESC_SEZIONE " +
          "FROM   DB_R_RUOLO_IRIDE2_MANUALE RIM, " +
          "       DB_T_MANUALE TM," +
          "       DB_D_SEZIONE_MANUALE SM " +
          "WHERE  RIM.CODICE_RUOLO = ? " +
          "AND    RIM.ID_MANUALE = TM.ID_MANUALE " +
          "AND    TM.ID_SEZIONE = SM.ID_SEZIONE(+) " +
          "ORDER BY " +
          "       SM.DESCRIZIONE NULLS FIRST, " +
          "       TM.ORDINAMENTO, " +
          "       TM.DESCRIZIONE, " +
          "       TM.DATA_VERSIONE ");
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[ManualiGaaDAO::getElencoManualiFromRuoli] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setString(++idx, descRuolo);
      
      rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(results == null)
        {
          results = new Vector<ManualeVO>();
        }
        
        manualeVO = new ManualeVO();
        manualeVO.setIdManuale(rs.getLong("ID_MANUALE"));
        manualeVO.setDataVersione(rs.getTimestamp("DATA_VERSIONE"));
        manualeVO.setVersione(rs.getString("VERSIONE"));
        manualeVO.setDescManuale(rs.getString("DESC_MANUALE"));
        manualeVO.setIdSezione(checkLongNull(rs.getString("ID_SEZIONE")));
        manualeVO.setDescSezione(rs.getString("DESC_SEZIONE"));
        
        results.add(manualeVO);
      }
      
      
      return results;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("descRuolo", descRuolo) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("results", results), new Variabile("manualeVO", manualeVO)

      };
      LoggerUtils.logDAOError(this,
          "[ManualiGaaDAO::getElencoManualiFromRuoli] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[ManualiGaaDAO::getElencoManualiFromRuoli] END.");
    }
  }
  
  /**
   * 
   * ritorna il file binario del manuale.
   * 
   * 
   * @param idManuale
   * @return
   * @throws DataAccessException
   */
  public ManualeVO getManuale(long idManuale) throws DataAccessException 
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String query = null;
    StringBuffer queryBuf = null;
    ManualeVO manualeVO = null;
    
    int idx = 0;

    try
    {
      SolmrLogger.debug(this, "[ManualiGaaDAO::getManuale] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT TM.TESTO_MANUALE, " +
          "       TM.NOME_FILE " +
          "FROM   DB_T_MANUALE TM " +
          "WHERE  TM.ID_MANUALE = ? ");
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this, "[ManualiGaaDAO::getManuale] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idManuale);
      
      rs = stmt.executeQuery();
    
    

      if (rs.next()) 
      {
        manualeVO = new ManualeVO();
        manualeVO.setNomeFile(rs.getString("NOME_FILE"));
         
        Blob blob = rs.getBlob("TESTO_MANUALE");
        if(blob!=null && (blob.length() >0))
        {
          manualeVO.setTestoManuale(blob.getBytes(1, (int)blob.length()));
        }
      }
     
      return manualeVO;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idManuale", idManuale) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("manualeVO", manualeVO)     };
      LoggerUtils.logDAOError(this,
          "[ManualiGaaDAO::getManuale] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[ManualiGaaDAO::getManuale] END.");
    }
  }
  
  
  
  
}
