package it.csi.smranag.smrgaa.integration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import it.csi.smranag.smrgaa.dto.BaseCodeDescription;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;

/**
 * Dao per accedere alle tabelle di decodifica
 * Oct 24, 2008
 * @author TOBECONFIG
 *
 */
public class DecodificheDAO extends it.csi.solmr.integration.BaseDAO
{

  public DecodificheDAO() throws ResourceAccessException
  {
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public DecodificheDAO(String refName) throws ResourceAccessException
  {
    super(refName);
  }
/**
 * Decodifica una sequenza di id di una tabella in un array di BaseCodeDescription
 * con id/descrizione
 * @param tableName
 * @param ids
 * @param idOrder
 * @return
 * @throws DataAccessException
 */
  public BaseCodeDescription[] baseDecode(String tableName, long ids[],
      String itemName,Boolean idOrder) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<BaseCodeDescription> vDecodifica = null;
    BaseCodeDescription bcd = null;   ///
    try
    {
      SolmrLogger.debug(this, "[DecodificheDAO::baseDecode] BEGIN.");
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf = new StringBuffer();
      tableName = tableName.toUpperCase();
      String idName = "ID_" + tableName;
      queryBuf.append("SELECT ").append(idName);
      if (itemName!=null && !"".equals(itemName.trim()))
      {
        queryBuf.append(", ").append(itemName);
      }
      else
      {
        itemName=null; // lo reimposto nel caso sia == ""
      }
      queryBuf.append(", DESCRIZIONE FROM DB_TIPO_").append(tableName);
      queryBuf.append(" WHERE ").append(idName);
      queryBuf.append(" IN (").append(getIdListFromArrayForSQL(ids))
          .append(")");
      if (idOrder != null)
      {
        // Se valorizzato ==> Ordino
        if (idOrder.booleanValue())
        {
          // Se true ordino per id
          queryBuf.append(" ORDER BY ID_").append(tableName);
        }
        else
        {
          queryBuf.append(" ORDER BY DESCRIZIONE");
        }
      }
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      
      query=queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[DecodificheDAO::baseDecode] Query=" + query);
      }
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      vDecodifica = new Vector<BaseCodeDescription>();
      while (rs.next())
      {
        long id = rs.getLong(idName);
        String description = rs.getString("DESCRIZIONE");
        bcd = new BaseCodeDescription();
        bcd.setCode(id);
        if (itemName!=null)
        {
          bcd.setItem(rs.getString(itemName));
        }
        bcd.setDescription(description);
        vDecodifica.add(bcd);
      }
      int size = vDecodifica.size();
      if (size > 0)
      {
        return (BaseCodeDescription[]) vDecodifica
            .toArray(new BaseCodeDescription[size]);
      }
      else
      {
        return null;
      }
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("vDecodifica", vDecodifica), new Variabile("bcd", bcd) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("tableName", tableName), new Parametro("ids", ids),
          new Parametro("idOrder", idOrder) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[DecodificheDAO::baseDecode] ", t, query,
          variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
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

      // Fine metodo
      SolmrLogger.debug(this, "[DecodificheDAO::baseDecode] END.");
    }
  }
  
  

}
