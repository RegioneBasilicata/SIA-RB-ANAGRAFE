package it.csi.solmr.integration.anag;

import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.anag.consistenza.*;
import it.csi.solmr.exception.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.util.*;

import java.sql.*;
import java.util.*;

public class TipoMotivoDichiarazioneDAO extends it.csi.solmr.integration.BaseDAO {


	public TipoMotivoDichiarazioneDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public TipoMotivoDichiarazioneDAO(String refName) throws ResourceAccessException {
		super(refName);
	}
	
	/**
	 * Metodo utilizzato per estrarre i tipi motivo dichiarazione 
	 * 
	 * @param onlyActive
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.TipoMotivoDichiarazioneVO[]
	 * @throws DataAccessException
	 */
	/*public TipoMotivoDichiarazioneVO[] getListTipoMotivoDichiarazione(boolean onlyActive, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListTipoMotivoDichiarazione method in TipoMotivoDichiarazioneDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoMotivoDichiarazioneVO> elencoTipoMotivoDichiarazione = new Vector<TipoMotivoDichiarazioneVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListTipoMotivoDichiarazione method in TipoMotivoDichiarazioneDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListTipoMotivoDichiarazione method in TipoMotivoDichiarazioneDAO and it values: "+conn+"\n");

			String query = " SELECT ID_MOTIVO_DICHIARAZIONE, " +
						   "        DESCRIZIONE, " +
						   "        DATA_INIZIO_VALIDITA, " +
						   "        DATA_FINE_VALIDITA, " +
						   "        ANNO_CAMPAGNA, " +
						   "        ID_FASE, " +
						   "        GTFO, " +
						   "        TIPO_DICHIARAZIONE " +
						   " FROM   DB_TIPO_MOTIVO_DICHIARAZIONE ";
			if(onlyActive) {
				query +=   " WHERE  DATA_FINE_VALIDITA IS NULL ";
			}
		
			if(orderBy != null && orderBy.length > 0) {
				String criterio = "";
				for(int i = 0; i < orderBy.length; i++) {
					if(i == 0) {
						criterio = (String)orderBy[i];
					}
					else {
						criterio += ", "+(String)orderBy[i];
					}
				}
				query+= "ORDER BY "+criterio;
			}
			
			SolmrLogger.debug(this, "Value of parameter 1 [ONLY_ACTIVE] in getListTipoMotivoDichiarazione method in TipoMotivoDichiarazioneDAO: "+onlyActive+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ORDER_BY] in getListTipoMotivoDichiarazione method in TipoMotivoDichiarazioneDAO: "+orderBy+"\n");

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing getListTipoMotivoDichiarazione: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				TipoMotivoDichiarazioneVO tipoMotivoDichiarazioneVO = new TipoMotivoDichiarazioneVO();
				tipoMotivoDichiarazioneVO.setIdMotivoDichiarazione(new Long(rs.getLong("ID_MOTIVO_DICHIARAZIONE")));
				tipoMotivoDichiarazioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoMotivoDichiarazioneVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				tipoMotivoDichiarazioneVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				tipoMotivoDichiarazioneVO.setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
				tipoMotivoDichiarazioneVO.setIdFase(new Long(rs.getLong("ID_FASE")));
				tipoMotivoDichiarazioneVO.setGtfo(rs.getString("GTFO"));
				tipoMotivoDichiarazioneVO.setTipoDichiarazione(rs.getString("TIPO_DICHIARAZIONE"));
				elencoTipoMotivoDichiarazione.add(tipoMotivoDichiarazioneVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListTipoMotivoDichiarazione in TipoMotivoDichiarazioneDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListTipoMotivoDichiarazione in TipoMotivoDichiarazioneDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListTipoMotivoDichiarazione in TipoMotivoDichiarazioneDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListTipoMotivoDichiarazione in TipoMotivoDichiarazioneDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipoMotivoDichiarazione method in TipoMotivoDichiarazioneDAO\n");
		if(elencoTipoMotivoDichiarazione.size() == 0) {
			return (TipoMotivoDichiarazioneVO[])elencoTipoMotivoDichiarazione.toArray(new TipoMotivoDichiarazioneVO[0]);
		}
		else {
			return (TipoMotivoDichiarazioneVO[])elencoTipoMotivoDichiarazione.toArray(new TipoMotivoDichiarazioneVO[elencoTipoMotivoDichiarazione.size()]);
		}
	}*/
	
	/**
	 * Metodo per estrarre il tipo motivo dichiarazione a partire dalla sua chiave primaria
	 * 
	 * @param idMotivoDichiarazione
	 * @return it.csi.solmr.dto.consistenza.TipoMotivoDichiarazioneVO
	 * @throws DataAccessException
	 */
	public TipoMotivoDichiarazioneVO findTipoMotivoDichiarazioneByPrimaryKey(Long idMotivoDichiarazione) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating findTipoMotivoDichiarazioneByPrimaryKey method in TipoMotivoDichiarazioneDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		TipoMotivoDichiarazioneVO tipoMotivoDichiarazioneVO = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in findTipoMotivoDichiarazioneByPrimaryKey method in TipoMotivoDichiarazioneDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in findTipoMotivoDichiarazioneByPrimaryKey method in TipoMotivoDichiarazioneDAO and it values: "+conn+"\n");

			String query = " SELECT ID_MOTIVO_DICHIARAZIONE, " +
						   "        DESCRIZIONE, " +
						   "        DATA_INIZIO_VALIDITA, " +
						   "        DATA_FINE_VALIDITA, " +
						   "        ANNO_CAMPAGNA, " +
						   "        ID_FASE, " +
						   "        GTFO, " +
						   "        TIPO_DICHIARAZIONE " +
						   " FROM   DB_TIPO_MOTIVO_DICHIARAZIONE " +
						   " WHERE  ID_MOTIVO_DICHIARAZIONE = ? ";
			
			SolmrLogger.debug(this, "Value of parameter 1 [ID_MOTIVO_DICHIARAZIONE] in findTipoMotivoDichiarazioneByPrimaryKey method in TipoMotivoDichiarazioneDAO: "+idMotivoDichiarazione+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idMotivoDichiarazione.longValue());

			SolmrLogger.debug(this, "Executing findTipoMotivoDichiarazioneByPrimaryKey: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) {
				tipoMotivoDichiarazioneVO = new TipoMotivoDichiarazioneVO();
				tipoMotivoDichiarazioneVO.setIdMotivoDichiarazione(new Long(rs.getLong("ID_MOTIVO_DICHIARAZIONE")));
				tipoMotivoDichiarazioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoMotivoDichiarazioneVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				tipoMotivoDichiarazioneVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				tipoMotivoDichiarazioneVO.setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
				tipoMotivoDichiarazioneVO.setIdFase(new Long(rs.getLong("ID_FASE")));
				tipoMotivoDichiarazioneVO.setGtfo(rs.getString("GTFO"));
				tipoMotivoDichiarazioneVO.setTipoDichiarazione(rs.getString("TIPO_DICHIARAZIONE"));
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "findTipoMotivoDichiarazioneByPrimaryKey in TipoMotivoDichiarazioneDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "findTipoMotivoDichiarazioneByPrimaryKey in TipoMotivoDichiarazioneDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "findTipoMotivoDichiarazioneByPrimaryKey in TipoMotivoDichiarazioneDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "findTipoMotivoDichiarazioneByPrimaryKey in TipoMotivoDichiarazioneDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated findTipoMotivoDichiarazioneByPrimaryKey method in TipoMotivoDichiarazioneDAO\n");
		return tipoMotivoDichiarazioneVO;
	}
	
	
	
	/**
	 * ritorna i tipi di dichiarazioni possibili per ogni azienda.
	 * TUtte quelle attive non correttive +
	 * quelle ative correttive ma legate all'azienda
	 * 
	 * 
	 * @param idAzienda
	 * @return
	 * @throws DataAccessException
	 */
	public Vector<TipoMotivoDichiarazioneVO> getListTipoMotivoDichiarazione(long idAzienda) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoMotivoDichiarazioneVO> vTipoMotivoDichiarazione = null;
    try
    {
      SolmrLogger.debug(this,
          "[TipoMotivoDichiarazioneDAO::getListTipoMotivoDichiarazione] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT TMD.ID_MOTIVO_DICHIARAZIONE, " +
        "       TMD.DESCRIZIONE, " +
        "       TMD.DATA_INIZIO_VALIDITA, " +
        "       TMD.DATA_FINE_VALIDITA, " +
        "       TMD.ANNO_CAMPAGNA, " +
        "       TMD.ID_FASE, " +
        "       TMD.GTFO, " +
        "       TMD.TIPO_DICHIARAZIONE " +
        "FROM   DB_TIPO_MOTIVO_DICHIARAZIONE TMD " +
        "WHERE  TMD.DATA_INIZIO_VALIDITA < SYSDATE " +
        "AND    TMD.GTFO <> 'S' " +
        "AND    NVL(TMD.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > SYSDATE " +          
        "UNION " +        
        "SELECT TMD.ID_MOTIVO_DICHIARAZIONE, " +
        "       TMD.DESCRIZIONE, " +
        "       TMD.DATA_INIZIO_VALIDITA, " +
        "       TMD.DATA_FINE_VALIDITA, " +
        "       TMD.ANNO_CAMPAGNA, " +
        "       TMD.ID_FASE, " +
        "       TMD.GTFO, " +
        "       TMD.TIPO_DICHIARAZIONE " +
        "FROM   DB_TIPO_MOTIVO_DICHIARAZIONE TMD, " +
        "       DB_AZIENDA_MOT_DICHIARAZIONE AMD " +
        "WHERE  AMD.ID_AZIENDA = ? " +
        "AND    AMD.DATA_INIZIO_VALIDITA < SYSDATE " +
        "AND    NVL(AMD.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > SYSDATE " +
        "AND    AMD.ID_MOTIVO_DICHIARAZIONE = TMD.ID_MOTIVO_DICHIARAZIONE " +
        "AND    TMD.DATA_INIZIO_VALIDITA < SYSDATE " +
        "AND    NVL(TMD.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > SYSDATE " +
        "ORDER BY DESCRIZIONE ASC ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[TipoMotivoDichiarazioneDAO::getListTipoMotivoDichiarazione] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int idx = 0;
      
      stmt.setLong(++idx, idAzienda);
      
      
      
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {        
        if(vTipoMotivoDichiarazione == null)
        {
          vTipoMotivoDichiarazione = new Vector<TipoMotivoDichiarazioneVO>();
        }
        
        TipoMotivoDichiarazioneVO tipoMotivoDichiarazioneVO = new TipoMotivoDichiarazioneVO();
        tipoMotivoDichiarazioneVO.setIdMotivoDichiarazione(new Long(rs.getLong("ID_MOTIVO_DICHIARAZIONE")));
        tipoMotivoDichiarazioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoMotivoDichiarazioneVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        tipoMotivoDichiarazioneVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        tipoMotivoDichiarazioneVO.setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
        tipoMotivoDichiarazioneVO.setIdFase(new Long(rs.getLong("ID_FASE")));
        tipoMotivoDichiarazioneVO.setGtfo(rs.getString("GTFO"));
        tipoMotivoDichiarazioneVO.setTipoDichiarazione(rs.getString("TIPO_DICHIARAZIONE"));
        vTipoMotivoDichiarazione.add(tipoMotivoDichiarazioneVO);
               
      }
      
      return vTipoMotivoDichiarazione;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("vTipoMotivoDichiarazione", vTipoMotivoDichiarazione) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda)
      };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[TipoMotivoDichiarazioneDAO::getListTipoMotivoDichiarazione] ", t,
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
          "[TipoMotivoDichiarazioneDAO::getListTipoMotivoDichiarazione] END.");
    }
  }
	
	
	
	
	
}
