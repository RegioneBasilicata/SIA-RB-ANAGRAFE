package it.csi.solmr.integration.anag;
	
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.anag.terreni.UtilizzoConsociatoDichiaratoVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class UtilizzoConsociatoDichDAO extends it.csi.solmr.integration.BaseDAO {


	public UtilizzoConsociatoDichDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public UtilizzoConsociatoDichDAO(String refName) throws ResourceAccessException {
		super(refName);
	}
	
	/**
	 * Metodo che mi restituisce l'elenco degli utilizzi consociati a partire dall'id_utilizzo_particella
	 * 
	 * @param idUtilizzoParticella
	 * @param orderBy
	 * @return java.util.Vector
	 * @throws DataAccessException
	 */
	public Vector<UtilizzoConsociatoDichiaratoVO> getListUtilizziConsociatiDichByIdUtilizzoDichiarato(Long idUtilizzoDichiarato) throws DataAccessException 
	{
	  String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<UtilizzoConsociatoDichiaratoVO> result = null;
    UtilizzoConsociatoDichiaratoVO utilizzoConsociatoDichiaratoVO = null;
    
    try
    {
      SolmrLogger.debug(this,
          "[UtilizzoConsociatoDichDAO::getListUtilizziConsociatiDichByIdUtilizzoDichiarato] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT UCD.ID_UTILIZZO_CONSOCIATO_DICH, " +
        "       UCD.ID_UTILIZZO_DICHIARATO, " +
        "       UCD.NUMERO_PIANTE, " +
        "       UCD.ID_PIANTE_CONSOCIATE " +
        "FROM   DB_UTILIZZO_CONSOCIATO_DICH UCD " +
        "WHERE  UCD.ID_UTILIZZO_DICHIARATO = ? ");
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[UtilizzoConsociatoDichDAO::getListUtilizziConsociatiDichByIdUtilizzoDichiarato] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int idx = 0;
      
      stmt.setLong(++idx, idUtilizzoDichiarato);
      
      
      
      ResultSet rs = stmt.executeQuery();
      while(rs.next()) 
      {
        if(result == null)
        {
          result = new Vector<UtilizzoConsociatoDichiaratoVO>();
        }
        
        utilizzoConsociatoDichiaratoVO = new UtilizzoConsociatoDichiaratoVO();
        utilizzoConsociatoDichiaratoVO.setIdUtilizzoConsociatoDich(new Long(rs.getLong("ID_UTILIZZO_CONSOCIATO_DICH")));
        utilizzoConsociatoDichiaratoVO.setIdUtilizzoDichiarato(new Long(rs.getLong("ID_UTILIZZO_DICHIARATO")));
        utilizzoConsociatoDichiaratoVO.setIdPianteConsociate(checkLongNull(rs.getString("ID_PIANTE_CONSOCIATE")));
        utilizzoConsociatoDichiaratoVO.setNumeroPiante(rs.getString("NUMERO_PIANTE"));
        
        result.add(utilizzoConsociatoDichiaratoVO);
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("result", result),
          new Variabile("utilizzoConsociatoDichiaratoVO", utilizzoConsociatoDichiaratoVO)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUtilizzoDichiarato", idUtilizzoDichiarato)
      };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[UtilizzoConsociatoDichDAO::getListUtilizziConsociatiDichByIdUtilizzoDichiarato] ", t,
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

      // Fine metodo
      SolmrLogger.debug(this,
          "[UtilizzoConsociatoDichDAO::getListUtilizziConsociatiDichByIdUtilizzoDichiarato] END.");
    }
  }
	
	
	
}
