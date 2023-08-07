package it.csi.solmr.integration.anag;
	
import it.csi.solmr.dto.anag.*;
import it.csi.solmr.exception.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.util.*;

import java.math.BigDecimal;
import java.sql.*;
import oracle.sql.ARRAY;

public class ParticellaDAO extends it.csi.solmr.integration.BaseDAO {


	public ParticellaDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public ParticellaDAO(String refName) throws ResourceAccessException {
		super(refName);
	}
	
	/**
	 * Metodo utilizzato per effettuare l'inserimento del record relativo alla tabella
	 * DB_PARTICELLA
	 * 
	 * @param particellaVO
	 * return java.lang.Long idParticella
	 * @throws DataAccessException
	 */
	public Long insertParticella(ParticellaVO particellaVO) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating insertParticella method in ParticellaDAO\n");
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    Long idParticella = null;

	    try {
	    	idParticella = getNextPrimaryKey((String)SolmrConstants.get("SEQ_PARTICELLA"));
	    	SolmrLogger.debug(this, "Creating db-connection in insertParticella method in ParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in insertParticella method in ParticellaDAO and it values: "+conn+"\n");

			String query = " INSERT INTO DB_PARTICELLA "+
	                       "     		 (ID_PARTICELLA, " +
	                       "              DATA_CREAZIONE, " +
	                       "              DATA_CESSAZIONE, " +
	                       "              BIOLOGICO, " +
	                       "              DATA_INIZIO_VALIDITA, " +
	                       "              FLAG_SCHEDARIO) " +
	                       " VALUES       (?, ?, ?, ?, ?, ?) ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing insertParticella: "+query);
			
			stmt.setLong(1, idParticella.longValue());
			SolmrLogger.debug(this, "Value of parameter 1 [ID_PARTICELLA] in method insertParticella in ParticellaDAO: "+idParticella.longValue()+"\n");
			stmt.setTimestamp(2, new Timestamp(particellaVO.getDataCreazione().getTime()));
			SolmrLogger.debug(this, "Value of parameter 2 [DATA_CREAZIONE] in method insertParticella in ParticellaDAO: "+new Timestamp(particellaVO.getDataCreazione().getTime())+"\n");
			if(particellaVO.getDataCessazione() != null) {
				stmt.setTimestamp(3, new Timestamp(particellaVO.getDataCessazione().getTime()));
				SolmrLogger.debug(this, "Value of parameter 3 [DATA_CESSAZIONE] in method insertParticella in ParticellaDAO: "+new Timestamp(particellaVO.getDataCessazione().getTime())+"\n");
			}
			else {
				stmt.setString(3, null);
				SolmrLogger.debug(this, "Value of parameter 3 [DATA_CESSAZIONE] in method insertParticella in ParticellaDAO: "+null+"\n");
			}
			stmt.setString(4, particellaVO.getBiologico());
			SolmrLogger.debug(this, "Value of parameter 4 [BIOLOGICO] in method insertParticella in ParticellaDAO: "+particellaVO.getBiologico()+"\n");
			stmt.setDate(5, new java.sql.Date(particellaVO.getDataInizioValidita().getTime()));
			SolmrLogger.debug(this, "Value of parameter 5 [DATA_INIZIO_VALIDITA] in method insertParticella in ParticellaDAO: "+new java.sql.Date(particellaVO.getDataInizioValidita().getTime())+"\n");
			if(Validator.isNotEmpty(particellaVO.getFlagSchedario())) {
				stmt.setString(6, particellaVO.getFlagSchedario());
				SolmrLogger.debug(this, "Value of parameter 6 [FLAG_SCHEDARIO] in method insertParticella in ParticellaDAO: "+particellaVO.getFlagSchedario()+"\n");
			}
			else {
				stmt.setString(6, SolmrConstants.FLAG_N);
				SolmrLogger.debug(this, "Value of parameter 6 [FLAG_SCHEDARIO] in method insertParticella in ParticellaDAO: "+SolmrConstants.FLAG_N+"\n");
			}
			
			stmt.executeUpdate();

			stmt.close();
	    }
	    catch(SQLException exc) {
	    	SolmrLogger.error(this, "insertParticella in ParticellaDAO - SQLException: "+exc);
	    	throw new DataAccessException(exc.getMessage());
	    }
	    catch (Exception ex) {
	    	SolmrLogger.error(this, "insertParticella in ParticellaDAO - Generic Exception: "+ex);
	    	throw new DataAccessException(ex.getMessage());
	    }
	    finally {
	    	try {
	    		if(stmt != null) {
	    			stmt.close();
	    		}
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	}
	    	catch(SQLException exc) {
	    		SolmrLogger.error(this, "insertParticella in ParticellaDAO - SQLException while closing Statement and Connection: "+exc);
	    		throw new DataAccessException(exc.getMessage());
	    	}
	    	catch(Exception ex) {
	    		SolmrLogger.error(this, "insertParticella in ParticellaDAO - Generic Exception while closing Statement and Connection: "+ex);
	    		throw new DataAccessException(ex.getMessage());
	    	}
	    }
	    SolmrLogger.debug(this, "Invocated insertParticella method in ParticellaDAO\n");
	    return idParticella;
	}
	
	/**
	 * Metodo che mi consente di effettuare la cessazione del record su DB_PARTICELLA
	 * 
	 * @param idParticella
	 * @throws DataAccessException
	 */
	public void cessaParticella(Long idParticella) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating cessaParticella method in ParticellaDAO\n");
	    Connection conn = null;
	    PreparedStatement stmt = null;

	    try {
	    	SolmrLogger.debug(this, "Creating db-connection in cessaParticella method in ParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in cessaParticella method in ParticellaDAO and it values: "+conn+"\n");

			String query = " UPDATE DB_PARTICELLA "+
	                       " SET    DATA_CESSAZIONE = SYSDATE " +
	                       " WHERE  ID_PARTICELLA = ? ";

			stmt = conn.prepareStatement(query);
			
			SolmrLogger.debug(this, "Value of parameter 1 [ID_PARTICELLA] in method cessaParticella in ParticellaDAO: "+idParticella.longValue()+"\n");
			stmt.setLong(1, idParticella.longValue());

			SolmrLogger.debug(this, "Executing cessaParticella: "+query);

			stmt.executeUpdate();

			stmt.close();
	    }
	    catch(SQLException exc) {
	    	SolmrLogger.error(this, "cessaParticella in ParticellaDAO - SQLException: "+exc.getMessage());
	    	throw new DataAccessException(exc.getMessage());
	    }
	    catch (Exception ex) {
	    	SolmrLogger.error(this, "cessaParticella in ParticellaDAO - Generic Exception: "+ex);
	    	throw new DataAccessException(ex.getMessage());
	    }
	    finally {
	    	try {
	    		if(stmt != null) {
	    			stmt.close();
	    		}
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	}
	    	catch(SQLException exc) {
	    		SolmrLogger.error(this, "cessaParticella in ParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage());
	    		throw new DataAccessException(exc.getMessage());
	    	}
	    	catch(Exception ex) {
	    		SolmrLogger.error(this, "cessaParticella in ParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage());
	    		throw new DataAccessException(ex.getMessage());
	    	}
	    }
	    SolmrLogger.debug(this, "Invocated cessaParticella method in ParticellaDAO\n");
	}
	
	public void attivaParticella(Long idParticella) throws DataAccessException 
	{
    SolmrLogger.debug(this, "Invocating attivaParticella method in ParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in attivaParticella method in ParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in attivaParticella method in ParticellaDAO and it values: "+conn+"\n");

      String query = " UPDATE DB_PARTICELLA "+
                         " SET    DATA_CESSAZIONE = NULL " +
                         " WHERE  ID_PARTICELLA = ? ";

      stmt = conn.prepareStatement(query);
      
      SolmrLogger.debug(this, "Value of parameter 1 [ID_PARTICELLA] in method attivaParticella in ParticellaDAO: "+idParticella.longValue()+"\n");
      stmt.setLong(1, idParticella.longValue());

      SolmrLogger.debug(this, "Executing attivaParticella: "+query);

      stmt.executeUpdate();

      stmt.close();
    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "attivaParticella in ParticellaDAO - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) 
    {
      SolmrLogger.error(this, "attivaParticella in ParticellaDAO - Generic Exception: "+ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) 
        {
          stmt.close();
        }
        if(conn != null) 
        {
          conn.close();
        }
      }
      catch(SQLException exc) 
      {
        SolmrLogger.error(this, "attivaParticella in ParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) 
      {
        SolmrLogger.error(this, "attivaParticella in ParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated attivaParticella method in ParticellaDAO\n");
  }
	
	/**
	 * Metodo che mi consente di effettuare la modifica del record su DB_PARTICELLA
	 * 
	 * @param particellaVO
	 * @throws DataAccessException
	 */
	public void updateParticella(ParticellaVO particellaVO) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating updateParticella method in ParticellaDAO\n");
	    Connection conn = null;
	    PreparedStatement stmt = null;

	    try {
	    	SolmrLogger.debug(this, "Creating db-connection in updateParticella method in ParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in updateParticella method in ParticellaDAO and it values: "+conn+"\n");

			String query = " UPDATE DB_PARTICELLA "+
	                       " SET    DATA_INIZIO_VALIDITA = ? ";
			if(Validator.isNotEmpty(particellaVO.getFlagSchedario())) {
				query +=   ", FLAG_SCHEDARIO = ? ";
			}
	        query +=       " WHERE  ID_PARTICELLA = ? ";

			stmt = conn.prepareStatement(query);
			
			SolmrLogger.debug(this, "Value of parameter 1 [DATA_INIZIO_VALIDITA] in method updateParticella in ParticellaDAO: "+particellaVO.getDataInizioValidita()+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [FLAG_SCHEDARIO] in method updateParticella in ParticellaDAO: "+particellaVO.getFlagSchedario()+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ID_PARTICELLA] in method updateParticella in ParticellaDAO: "+particellaVO.getIdParticella()+"\n");
			
			stmt.setTimestamp(1, new Timestamp(particellaVO.getDataInizioValidita().getTime()));
			int indice = 1;
			if(Validator.isNotEmpty(particellaVO.getFlagSchedario())) {
				stmt.setString(++indice, particellaVO.getFlagSchedario());
			}
			stmt.setLong(++indice, particellaVO.getIdParticella().longValue());

			SolmrLogger.debug(this, "Executing updateParticella: "+query);

			stmt.executeUpdate();

			stmt.close();
	    }
	    catch(SQLException exc) {
	    	SolmrLogger.error(this, "updateParticella in ParticellaDAO - SQLException: "+exc);
	    	throw new DataAccessException(exc.getMessage());
	    }
	    catch (Exception ex) {
	    	SolmrLogger.error(this, "updateParticella in ParticellaDAO - Generic Exception: "+ex);
	    	throw new DataAccessException(ex.getMessage());
	    }
	    finally {
	    	try {
	    		if(stmt != null) {
	    			stmt.close();
	    		}
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	}
	    	catch(SQLException exc) {
	    		SolmrLogger.error(this, "updateParticella in ParticellaDAO - SQLException while closing Statement and Connection: "+exc);
	    		throw new DataAccessException(exc.getMessage());
	    	}
	    	catch(Exception ex) {
	    		SolmrLogger.error(this, "updateParticella in ParticellaDAO - Generic Exception while closing Statement and Connection: "+ex);
	    		throw new DataAccessException(ex.getMessage());
	    	}
	    }
	    SolmrLogger.debug(this, "Invocated updateParticella method in ParticellaDAO\n");
	}
	
	/**
	 * Metodo che mi consente di recuperare il record dalla tabella DB_PARTICELLA a partire dalla sua
	 * chiave primaria
	 * 
	 * @param idParticella
	 * @return it.csi.solmr.dto.anag.ParticellaVO
	 * @throws DataAccessException
	 */
	public ParticellaVO findParticellaByPrimaryKey(Long idParticella) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating findParticellaByPrimaryKey method in ParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		ParticellaVO particellaVO = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in findParticellaByPrimaryKey method in ParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in findParticellaByPrimaryKey method in ParticellaDAO and it values: "+conn+"\n");

			String query = " SELECT ID_PARTICELLA, " +
						   "        DATA_CREAZIONE, " +
						   "        DATA_CESSAZIONE, " +
						   "        BIOLOGICO, " +
						   "        DATA_INIZIO_VALIDITA, " +
						   "        FLAG_SCHEDARIO " +
						   " FROM   DB_PARTICELLA " +
						   " WHERE  ID_PARTICELLA = ? ";

			SolmrLogger.debug(this, "Value of parameter 1 [ID_PARTICELLA] in findParticellaByPrimaryKey method in ParticellaDAO: "+idParticella+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idParticella.longValue());
			
			SolmrLogger.debug(this, "Executing findParticellaByPrimaryKey: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) {
				particellaVO = new ParticellaVO();
				particellaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
				particellaVO.setDataCreazione(rs.getTimestamp("DATA_CREAZIONE"));
				particellaVO.setDataCessazione(rs.getTimestamp("DATA_CESSAZIONE"));
				particellaVO.setBiologico(rs.getString("BIOLOGICO"));
				particellaVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				particellaVO.setFlagSchedario(rs.getString("FLAG_SCHEDARIO"));
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "findParticellaByPrimaryKey in ParticellaDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "findParticellaByPrimaryKey in ParticellaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "findParticellaByPrimaryKey in ParticellaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "findParticellaByPrimaryKey in ParticellaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated findParticellaByPrimaryKey method in ParticellaDAO\n");
		return particellaVO;
	}
	
	/**
	 * Metodo che richiama una procedura PL-SQL per l'aggiornamento massivo delle particelle
	 * in relazione alla loro posizione in schedario
	 * 
	 * @param idParticella
	 * @throws SolmrException
	 * @throws DataAccessException
	 */
	public void aggiornaSchedarioParticellaPlSql(BigDecimal[] idParticella) 
		throws SolmrException, DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating aggiornaSchedarioParticellaPlSql method in ParticellaDAO\n");
		
    Connection conn = null;
    CallableStatement cs = null;
    String command = "{call PACK_AGGIORNA_UV.AGGIORNA_STATO_UV(?,?,?)}";
    try {
    	SolmrLogger.debug(this, "Creating db-connection in aggiornaSchedarioParticellaPlSql method in ParticellaDAO\n");
		conn = getDatasource().getConnection();
		SolmrLogger.debug(this, "Created db-connection in aggiornaSchedarioParticellaPlSql method in ParticellaDAO and it values: "+conn+"\n");
		
		SolmrLogger.debug(this, "Value of parameter 1 [ID_PARTICELLA[]] in aggiornaSchedarioParticellaPlSql method in ParticellaDAO: "+idParticella+"\n");
		
		cs = conn.prepareCall(command);
		// JBOSS
		org.jboss.jca.adapters.jdbc.WrappedConnection vendorConnection = (org.jboss.jca.adapters.jdbc.WrappedConnection) conn;
		//org.jboss.resource.adapter.jdbc.WrappedConnection vendorConnection = (org.jboss.resource.adapter.jdbc.WrappedConnection) conn;
		oracle.sql.ArrayDescriptor arrayDescriptor = oracle.sql.ArrayDescriptor.createDescriptor("NUM_VARRAY", vendorConnection.getUnderlyingConnection());
		oracle.sql.ARRAY array = new oracle.sql.ARRAY(arrayDescriptor, vendorConnection.getUnderlyingConnection(),idParticella);//vendorConnection, idParticella);
		
		cs.setObject(1, array);
		cs.registerOutParameter(2,Types.VARCHAR);
		cs.registerOutParameter(3,Types.VARCHAR);
		
		cs.executeUpdate();
		String msgErr = cs.getString(2);
		String errorCode = cs.getString(3);

		if(Validator.isNotEmpty(errorCode)) {
			throw new SolmrException((String)AnagErrors.get("ERR_PLSQL")+errorCode + " - " + msgErr);
		}
		cs.close();
		conn.close();
    }
    catch(SolmrException se) {
    	SolmrLogger.error(this, "SolmrException in aggiornaSchedarioParticellaPlSql in ParticellaDAO: "+se);
    	throw new SolmrException(se.getMessage());
    }
    catch(SQLException exc) {
    	char a = '"';
    	String messaggioErrore = StringUtils.replace(exc.getMessage(), "'", "''");
    	messaggioErrore = StringUtils.replace(exc.getMessage(), System.getProperty("line.separator"), "\\n");
    	messaggioErrore = StringUtils.replace(exc.getMessage(), String.valueOf(a), " ");
    	SolmrLogger.error(this, "SQLException in aggiornaSchedarioParticellaPlSql in ParticellaDAO: "+messaggioErrore);
    	throw new SolmrException((String)AnagErrors.get("ERR_PLSQL")+" "+ StringUtils.replace(messaggioErrore, System.getProperty("line.separator"), "\\n"));
    }
    catch(Exception ex) {
    	SolmrLogger.error(this, "Generic Exception in aggiornaSchedarioParticellaPlSql in ParticellaDAO: "+ex);
    	throw new DataAccessException(ex.getMessage());
    }
    catch(Throwable ex) {
    	SolmrLogger.error(this, "Throwable in aggiornaSchedarioParticellaPlSql in ParticellaDAO: "+ex);
    	throw new DataAccessException(ex.getMessage());
    }
    finally {
    	try {
    		if(cs != null) {
    			cs.close();
    		}
    		if(conn != null) {
    			conn.close();
    		}
    	}
    	catch(SQLException exc) {
    		SolmrLogger.error(this, "SQLException while closing Statement and Connection in aggiornaSchedarioParticellaPlSql in ParticellaDAO: "+exc);
    		throw new DataAccessException(exc.getMessage());
    	}
    	catch(Exception ex) {
    		SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in aggiornaSchedarioParticellaPlSql in ParticellaDAO: "+ex);
    		throw new DataAccessException(ex.getMessage());
    	}
    }
    SolmrLogger.debug(this, "Invocated aggiornaSchedarioParticellaPlSql method in ParticellaDAO\n");
	}
}
