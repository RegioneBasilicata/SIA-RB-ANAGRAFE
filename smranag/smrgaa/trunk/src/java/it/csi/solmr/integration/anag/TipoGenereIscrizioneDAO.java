package it.csi.solmr.integration.anag;
	
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.anag.terreni.TipoGenereIscrizioneVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class TipoGenereIscrizioneDAO extends it.csi.solmr.integration.BaseDAO 
{


	public TipoGenereIscrizioneDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public TipoGenereIscrizioneDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	
	/**
   * 
   * Estrae l'id_genere_iscrizione con flag_definitiva = 'S'
   * 
   * 
   * @return
   * @throws GaaservInternalException
   */
  public Long getDefaultIdGenereIscrizione() throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    String query = null;
    Long idGenereIscrizione = null;
    try
    {
      SolmrLogger.debug(this,
          "[TipoGenereIscrizioneDAO::getDefaultIdGenereIscrizione] BEGIN.");
  
      conn = getDatasource().getConnection();
      queryBuf = new StringBuffer();
      queryBuf.append("" +
          "SELECT ID_GENERE_ISCRIZIONE " +
          "FROM   DB_TIPO_GENERE_ISCRIZIONE " +
          "WHERE  FLAG_DEFINITIVA = 'S' ");
      
  
      query = queryBuf.toString();
      stmt = conn.prepareStatement(query);
  
      rs = stmt.executeQuery();
  
      if (rs.next())
      {
        idGenereIscrizione = checkLongNull(rs.getString("ID_GENERE_ISCRIZIONE"));
      }
  
      return idGenereIscrizione;
    }
    catch (Throwable t)
    {
      Parametro parametri[] =
      {};
      Variabile variabili[] =
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("idGenereIscrizione", idGenereIscrizione)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[TipoGenereIscrizioneDAO::getDefaultIdGenereIscrizione] ", t,
          query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      SolmrLogger.debug(this,
          "[TipoGenereIscrizioneDAO::getDefaultIdGenereIscrizione] END.");
    }
  }
  
  
  /**
   * 
   * restituisce la listadi tutti irecord su DB_GENERE_ISCRIZIONE
   * 
   * 
   * @return
   * @throws GaaservInternalException
   */
  public Vector<TipoGenereIscrizioneVO> getListTipoGenereIscrizione() throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    String query = null;
    Vector<TipoGenereIscrizioneVO> vTipiGenere = null;
    try
    {
      SolmrLogger.debug(this,
          "[TipoGenereIscrizioneDAO::getListTipoGenereIscrizione] BEGIN.");
  
      conn = getDatasource().getConnection();
      queryBuf = new StringBuffer();
      queryBuf.append("" +
          "SELECT ID_GENERE_ISCRIZIONE, DESCRIZIONE " +
          "FROM   DB_TIPO_GENERE_ISCRIZIONE ");
      
  
      query = queryBuf.toString();
      stmt = conn.prepareStatement(query);
  
      rs = stmt.executeQuery();
  
      while (rs.next())
      {
        
        if(vTipiGenere == null)
        {
          vTipiGenere = new Vector<TipoGenereIscrizioneVO>();
        }
        
        TipoGenereIscrizioneVO tipoGenereIscrizioneVO = new TipoGenereIscrizioneVO();
        tipoGenereIscrizioneVO.setIdGenereIscrizione(checkLong(rs.getString("ID_GENERE_ISCRIZIONE")));
        tipoGenereIscrizioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
        
        
        vTipiGenere.add(tipoGenereIscrizioneVO);
      }
  
      return vTipiGenere;
    }
    catch (Throwable t)
    {
      Parametro parametri[] =
      {};
      Variabile variabili[] =
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("vTipiGenere", vTipiGenere)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[TipoGenereIscrizioneDAO::getListTipoGenereIscrizione] ", t,
          query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      SolmrLogger.debug(this,
          "[TipoGenereIscrizioneDAO::getListTipoGenereIscrizione] END.");
    }
  }
  
  
  /**
   * 
   * restituisce l'id_genere iscrizione in base al flag
   * passato come argomento
   * 
   * 
   * @param flag
   * @return
   * @throws DataAccessException
   */
  public Long getIdGenereIscrizioneByFlag(String flag) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    String query = null;
    Long idGenereIscrizione = null;
    try
    {
      SolmrLogger.debug(this,
          "[TipoGenereIscrizioneDAO::getDefaultIdGenereIscrizione] BEGIN.");
  
      conn = getDatasource().getConnection();
      queryBuf = new StringBuffer();
      queryBuf.append("" +
          "SELECT ID_GENERE_ISCRIZIONE " +
          "FROM   DB_TIPO_GENERE_ISCRIZIONE " +
          "WHERE  FLAG_DEFINITIVA = ? ");
      
  
      query = queryBuf.toString();
      stmt = conn.prepareStatement(query);
      
      
      stmt.setString(1, flag);      
  
      rs = stmt.executeQuery();
  
      if (rs.next())
      {
        idGenereIscrizione = checkLongNull(rs.getString("ID_GENERE_ISCRIZIONE"));
      }
  
      return idGenereIscrizione;
    }
    catch (Throwable t)
    {
      Parametro parametri[] =
      {};
      Variabile variabili[] =
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("idGenereIscrizione", idGenereIscrizione)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[TipoGenereIscrizioneDAO::getDefaultIdGenereIscrizione] ", t,
          query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      SolmrLogger.debug(this,
          "[TipoGenereIscrizioneDAO::getDefaultIdGenereIscrizione] END.");
    }
  }
  
  
  
  
  
}
