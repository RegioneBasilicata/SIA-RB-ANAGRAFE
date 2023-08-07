package it.csi.solmr.integration;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Luca Romanello
 * @version 1.0
 */

import it.csi.solmr.etc.SolmrErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;

public abstract class BaseDAO
{
  public static final int PASSO = 800;
  private DataSource datasource = null;

  /*
   * public BaseDAO() throws ResourceAccessException {
   * this(SolmrConstants.JNDI_RESOURCE_REFERENCE); }
   */

  public BaseDAO(String refName) throws ResourceAccessException
  {
    try
    {
      Context ctx = new InitialContext();
      // String _refName = (refName == null) ?
      // SolmrConstants.JNDI_RESOURCE_REFERENCE : refName;
      datasource = (DataSource) PortableRemoteObject.narrow(
          ctx.lookup(refName), DataSource.class);
      ctx.close();
      if (SolmrLogger.isDebugEnabled(this))
        SolmrLogger.debug(this, "Obtained Datasource " + refName);
    }
    catch (Exception e)
    {
      SolmrLogger.fatal(this, "Cannot get Datasource - Root cause: " + e);
      new ResourceAccessException(e.getMessage());
    }
  }

  public DataSource getDatasource() throws ResourceAccessException
  {
    if (datasource == null)
    {
      SolmrLogger.fatal(this, SolmrErrors.EXC_RESOURCE_ACCESS);
      throw new ResourceAccessException();
    }
    return datasource;
  }

  public Long getNextPrimaryKey(String sequenceName) throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT " + sequenceName + ".NEXTVAL " + "  FROM DUAL ";
      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Searching new Primary Key: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        while (rs.next())
        {
          primaryKey = new Long(rs.getLong(1));
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Found new Primary Key: " + primaryKey);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this,
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }

  // Verifica che la stringa contenga un Long - Restituisce 0 non null
  protected Long checkLong(String val)
  {
    if (val == null)
    {
      return new Long(0);
    }
    return new Long(val);
  }

  // Verifica che la stringa contenga un Long - Restituisce null
  protected Long checkLongNull(String val)
  {
    if (val == null)
    {
      return null;
    }
    return new Long(val);
  }
  
  protected Integer checkIntegerNull(String val)
  {
    if (val == null)
    {
      return null;
    }
    return new Integer(val);
  }

  // Effettua la conversione da java.util.Date a java.sql.Date
  protected java.sql.Date checkDate(java.util.Date val)
  {
    if (val == null)
    {
      return null;
    }
    return new java.sql.Date(val.getTime());
  }

  // Verifica che la stringa contenga un Double
  protected Double checkDouble(String val)
  {
    if (val == null)
    {
      return new Double(0);
    }
    return new Double(val);
  }

  // Verifica che la stringa contenga un Integer
  protected Integer checkInt(String val)
  {
    try
    {
      return new Integer(val);
    }
    catch (NumberFormatException ex)
    {
      return null;
    }
  }

  /**
   * Converte una oggetto java.util.Date in un oggetto di tipo java.sql.Date.
   * 
   * @param aDate
   *          Data da convertire
   * @return l'oggetto java.util.Date rappresentante la data passata o null se
   *         aDate è null
   */
  protected java.sql.Date convertSqlDate(java.util.Date aDate)
  {
    if (aDate == null)
    {
      return null;
    }
    return new java.sql.Date(aDate.getTime());
  }

  /**
   * Converte una java.util.Date in un java.util.Time
   * 
   * @param java.util.Date
   *          da convertire
   * @return l'oggetto java.sql.Time rappresentante la data convertita o null se
   *         la data è null
   */
  protected java.util.Date checkTime(java.sql.Time val)
  {
    if (val == null)
    {
      return null;
    }
    return val;
  }

  /*
   * protected java.sql.Time checkTime(java.util.Date val){ if (val==null) {
   * return null; } return new java.sql.Time(val.getTime()); }
   */
  protected java.util.Date checkTimestamp(java.sql.Timestamp val)
  {
    if (val == null)
      return null;
    return val;
  }

  protected java.sql.Timestamp convertDateToTimestamp(java.util.Date val)
  {
    if (val == null)
      return null;
    return new Timestamp(val.getTime());
  }

  /**
   * Converte un Long in un BigDecimal
   * 
   * @param Long
   *          da convertire
   * @return l'oggetto java.util.BigDecimal rappresentante il numero convertito
   *         o null se Long è null
   */
  protected BigDecimal convertLongToBigDecimal(Long valueLong)
  {
    if (valueLong == null)
    {
      return null;
    }
    return (new BigDecimal(valueLong.longValue()));
  }

  /**
   * Converte un Double in un BigDecimal
   * 
   * @param Double
   *          da convertire
   * @return l'oggetto java.util.BigDecimal rappresentante il numero convertito
   *         o null se Double è null
   */
  protected BigDecimal convertDoubleToBigDecimal(Double valueDouble)
  {
    if (valueDouble == null)
    {
      return null;
    }
    return (new BigDecimal(valueDouble.doubleValue()));
  }

  /**
   * Converte una java.util.Date in un java.sql.time
   * 
   * @param util.Date
   *          da convertire
   * @return l'oggetto java.util.Date rappresentante la data convertita o null
   *         se java.util.Date è null
   */
  protected java.sql.Time convertUtilDateToSqlTime(java.util.Date valueDate)
  {
    if (valueDate == null)
    {
      return null;
    }
    return (new java.sql.Time(valueDate.getTime()));
  }

  /**
   * Converte un Integer in un BigDecimal
   * 
   * @param Integer
   *          da convertire
   * @return l'oggetto java.util.BigDecimal rappresentante il numero convertito
   *         oppure null se Integer è null
   */
  protected BigDecimal convertIntegerToBigDecimal(Integer valueInteger)
  {
    if (valueInteger == null)
    {
      return null;
    }
    return (new BigDecimal(valueInteger.intValue()));
  }
  
  protected OutputStream convertBlobToOutputStream(java.sql.Blob blob) 
      throws SQLException 
  {
    OutputStream os =null;
    if(blob instanceof oracle.sql.BLOB){
      //os = ((weblogic.jdbc.vendor.oracle.OracleThinBlob) blob).getBinaryOutputStream();
      // JBOSS
      oracle.sql.BLOB oraBlob = (oracle.sql.BLOB) blob;		
      os = oraBlob.setBinaryStream(0);
    }
    else
    {
      os = null;
    }
    return os;
  }
  
  

  protected int fillStatementWithParameters(PreparedStatement stmt,
      Vector<Object> daParams, int counter) throws SQLException
  {
    Iterator<Object> iter = daParams.iterator();
    while (iter.hasNext())
    {
      Object daPar = iter.next();
      if (daPar != null)
      {
        if (daPar instanceof String)
        {
          stmt.setString(++counter, (String) daPar);
        }
        else if (daPar instanceof Long)
        {
          stmt.setLong(++counter, ((Long) daPar).longValue());
        }
        else if (daPar instanceof Integer)
        {
          stmt.setInt(++counter, ((Integer) daPar).intValue());
        }
        else if (daPar instanceof java.util.Date)
        {
          stmt.setDate(++counter, new java.sql.Date(((java.util.Date) daPar)
              .getTime()));
        }
        else if (daPar instanceof java.sql.Date)
        {
          stmt.setDate(++counter, (java.sql.Date) daPar);
        }
        else if (daPar instanceof java.math.BigDecimal)
        {
          stmt.setBigDecimal(++counter, (java.math.BigDecimal) daPar);
        }
        else if (daPar instanceof Double)
        {
          stmt.setDouble(++counter, ((Double) daPar).doubleValue());
        }
        else if (daPar instanceof Float)
        {
          stmt.setFloat(++counter, ((Float) daPar).floatValue());
        }
        else if (daPar instanceof Byte)
        {
          stmt.setByte(++counter, ((Byte) daPar).byteValue());
        }
        else if (daPar instanceof Boolean)
        {
          stmt.setBoolean(++counter, ((Boolean) daPar).booleanValue());
        }
        else
        {
          stmt.setObject(++counter, daPar);
        }
      }
      else
      {
        stmt.setObject(++counter, null);
      }
    }
    return counter;
  }

  protected int fillStatementWithParameters(PreparedStatement stmt,
      Vector<Object> daParams) throws SQLException
  {
    return fillStatementWithParameters(stmt, daParams, 0);
  }

  protected int fillStatementWithParameters(PreparedStatement stmt,
      Object[] daParams, int counter) throws SQLException
  {
    Vector<Object> vDaParams = new Vector<Object>();
    for (int i = 0; i < daParams.length; i++)
      vDaParams.add(daParams[i]);
    return fillStatementWithParameters(stmt, vDaParams, counter);
  }

  protected int fillStatementWithParameters(PreparedStatement stmt,
      Object[] daParams) throws SQLException
  {
    return fillStatementWithParameters(stmt, daParams, 0);
  }

  /**
   * Converte una stringa in maiuscolo se non è null
   * 
   * @param String
   *          da convertire
   * @return l'oggetto String rappresentante la stringa convertita o null se
   *         String è null
   */
  protected String convertUpperString(String valueString)
  {
    if (valueString == null)
    {
      return null;
    }
    valueString = valueString.trim();
    if ("".equals(valueString))
    {
      return null;
    }
    return valueString.toUpperCase();
  }

  public Long selectForUpdate(String chosenField) throws DataAccessException
  {
    Long currentNumber = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String selUpd = "SELECT numero " + "  FROM Persistenza "
          + " WHERE nome = UPPER(?) " + "  FOR UPDATE ";
      stmt = conn.prepareStatement(selUpd);

      stmt.setString(1, chosenField);

      SolmrLogger.debug(this, "Locking " + chosenField + ": " + selUpd);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        if (rs.next())
        {
          currentNumber = new Long(rs.getLong(1));
        }
        else
        {
          insertNewNome(chosenField, new Long(1));
          currentNumber = selectForUpdate(chosenField);
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Found : " + currentNumber);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this,
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return currentNumber;
  }

  public void insertNewNome(String chosenField, Long currentNumber)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String selUpd = "INSERT INTO Persistenza (nome, numero) "
          + "                 VALUES (?, ?) ";
      stmt = conn.prepareStatement(selUpd);

      stmt.setString(1, chosenField);
      stmt.setLong(2, currentNumber.longValue());

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Inserted: " + chosenField + "/" + currentNumber);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this,
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  public void updatePersistenza(String chosenField, Long currentNumber)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String selUpd = "UPDATE Persistenza " + "   SET numero = ? "
          + " WHERE nome = UPPER(?) ";
      stmt = conn.prepareStatement(selUpd);

      stmt.setLong(1, currentNumber.longValue());
      stmt.setString(2, chosenField);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Updated: " + chosenField + "/" + currentNumber);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this,
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  protected void close(ResultSet rs, PreparedStatement ps, Connection con)
  {
    try
    {
      if (rs != null)
        rs.close();
      if (ps != null)
        ps.close();
      if (con != null)
        con.close();
      rs = null;
      ps = null;
      con = null;
    }
    catch (SQLException ex)
    {
      SolmrLogger.error(this, ex);
      ex.printStackTrace();
    }
  }
  
  protected void closePlSql(CallableStatement cs , Connection con)
  {
    try
    {
      if (cs != null)
        cs.close();
      if (con != null)
        con.close();
      cs = null;
      con = null;
    }
    catch (SQLException ex)
    {
      SolmrLogger.error(this, ex);
      ex.printStackTrace();
    }
  }

  /**
   * Verifica che tutti gli id passati siano presenti sulla tabella indicata,
   * NB: Il metodo vuole che il vettore di id non abbia duplicati
   * 
   * @param ids
   * @param nomeTabella
   * @param nomeCampo
   * @return true==tutti gli id esisto sulla tabella indicata, false altrimenti
   * @throws DataAccessException
   */
  public boolean checkIfAllIdExist(Long ids[], String nomeTabella,
      String nomeCampo) throws DataAccessException
  {
    try
    {
      SolmrLogger.debug(this, "DocumentoDAO::checkIfAllIdExist BEGIN.");
      Connection conn = null;
      PreparedStatement stmt = null;
      boolean result = false;
      try
      {
        conn = getDatasource().getConnection();
        StringBuffer queryBuf = new StringBuffer();
        int length = ids == null ? 0 : ids.length;
        queryBuf.append("SELECT " + " COUNT(*) " + " FROM (");
        for (int i = 0; i < length; i++)
        {
          if (i % PASSO == 0)
          {
            if (i != 0)
            {
              queryBuf.append(") UNION ALL");
            }
            queryBuf.append(" SELECT 1 " + " FROM ");
            queryBuf.append(nomeTabella);
            queryBuf.append(" WHERE ");
            queryBuf.append(nomeCampo);
            queryBuf.append(" IN (");
          }
          else
          {
            queryBuf.append(',');
          }
          queryBuf.append(ids[i]);
        }
        queryBuf.append("))");
        String query = queryBuf.toString();
        SolmrLogger.debug(this, "Executing checkIfAllIdExist: " + query + "\n");
        stmt = conn.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();

        rs.next();
        int count = rs.getInt(1);
        result = count == length;

        rs.close();
        stmt.close();

      }
      catch (SQLException exc)
      {
        SolmrLogger.error(this,
            "checkIfAllIdExist in DocumentoDAO - SQLException: "
                + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.error(this,
            "checkIfAllIdExist in DocumentoDAO - Generic Exception: " + ex
                + "\n");
        throw new DataAccessException(ex.getMessage());
      }
      finally
      {
        try
        {
          if (stmt != null)
            stmt.close();
          if (conn != null)
            conn.close();
        }
        catch (SQLException exc)
        {
          SolmrLogger
              .error(
                  this,
                  "checkIfAllIdExist in DocumentoDAO - SQLException while closing Statement and Connection: "
                      + exc.getMessage() + "\n");
          throw new DataAccessException(exc.getMessage());
        }
        catch (Exception ex)
        {
          SolmrLogger
              .error(
                  this,
                  "checkIfAllIdExist in DocumentoDAO - Generic Exception while closing Statement and Connection: "
                      + ex.getMessage() + "\n");
          throw new DataAccessException(ex.getMessage());
        }
      }
      return result;
    }
    finally
    {
      SolmrLogger.debug(this, "DocumentoDAO::checkIfAllIdExist END.");
    }
  }

  /**
   * Concatena una sequenza di id per utilizzo all'interno di una clausola sql
   * IN
   * 
   * @param ids
   *          elenco di id da concatenare
   * @return id concatenati Es "1,2,3,4,10,20,50,5,12"
   */
  public String getIdListFromArrayForSQL(long ids[])
  {
    StringBuffer sb = new StringBuffer();
    int lenght = ids == null ? 0 : ids.length;
    if (lenght == 0)
    {
      SolmrLogger.warn(this,
          "[BaseDAO::getIdListFromArrayForSQL] Nessun ID da convertire!");
      return "";
    }
    for (int i = 0; i < lenght; i++)
    {
      if (i != 0)
      {
        sb.append(",");
      }
      sb.append(ids[i]);
    }
    return sb.toString();
  }
  
  
  public String getIdListFromVectorStringForSQL(Vector<String> id)
  {
    StringBuffer sb = new StringBuffer();
    int lenght = id == null ? 0 : id.size();
    if (lenght == 0)
    {
      SolmrLogger.warn(this,
          "[BaseDAO::getIdListFromVectorStringForSQL] Nessun ID da convertire!");
      return "";
    }
    for (int i = 0; i < lenght; i++)
    {
      if (i != 0)
      {
        sb.append(",");
      }
      sb.append(id.get(i));
    }
    return sb.toString();
  }
  
  public String getIdListFromVectorLongForSQL(Vector<Long> id)
  {
    StringBuffer sb = new StringBuffer();
    int lenght = id == null ? 0 : id.size();
    if (lenght == 0)
    {
      SolmrLogger.warn(this,
          "[BaseDAO::getIdListFromVectorLongForSQL] Nessun ID da convertire!");
      return "";
    }
    for (int i = 0; i < lenght; i++)
    {
      if (i != 0)
      {
        sb.append(",");
      }
      sb.append(id.get(i));
    }
    return sb.toString();
  }


  public void addOrdinamento(StringBuffer queryBuf, String campo, boolean asc)
  {
    queryBuf.append(campo);
    if (asc)
    {
      queryBuf.append(" ASC ");
    }
    else
    {
      queryBuf.append(" DESC ");
    }
  }
  
  /*
   * Restituisce il numero di punti interrogfativi perla bind in base alla paginazione.
   * 
   */
  public String getPuntiInterrogativiByPaginazione(int numeroRighe)
  {
    String parametri = "(";    
    for(int i=0;i<numeroRighe;i++)
    {
      parametri+="?";
      if (i<numeroRighe-1)
        parametri+=",";
    }
    parametri+=") ";
    
    return parametri;
  }
  
  
  /**
   * 
   * mette a null i valori che mancano per arrivare al max paginazione,
   * per poter usare correttamente le bind!!!
   * 
   * 
   * @param stmt
   * @param vIdLong
   * @param indice
   * @param numeroRighe
   * @return
   * @throws SQLException
   */
  public int setLongStatementPaginazione(
      PreparedStatement stmt, Vector<Long> vIdLong, int indice, int numeroRighe)
      throws SQLException
  { 
    
    int i=0;
    StringBuffer strBuf = new StringBuffer();
    for(;i<vIdLong.size();i++)
    {
      if(i==0)
      {
        strBuf.append("(");
      }
      strBuf.append(vIdLong.get(i).toString());
      if(i != (numeroRighe-1) )
      {
        strBuf.append(",");
      }
      
      stmt.setString(++indice, vIdLong.get(i).toString());
    }
    
    
    for(;i<numeroRighe;i++)
    {
      
      strBuf.append("null");
      if(i != (numeroRighe-1) )
      {
        strBuf.append(",");
      }
      
      stmt.setString(++indice, null);
    }   
    
    strBuf.append(")");
    
    SolmrLogger.debug(this, "BaseDAO::setLongStatementPaginazione: "+strBuf.toString());
    
    return indice;
    
  }
  
  
}
