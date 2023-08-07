package it.csi.solmr.integration.anag;

import java.sql.*;
import it.csi.solmr.dto.anag.terreni.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.exception.*;
import it.csi.solmr.util.*;

public class UnitaArboreaDAO extends it.csi.solmr.integration.BaseDAO {


	public UnitaArboreaDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public UnitaArboreaDAO(String refName) throws ResourceAccessException {
		super(refName);
	}
	
	/**
	 * Metodo utilizzato per inserire un record su DB_UNITA_ARBOREA
	 * 
	 * @param unitaArboreaVO
	 * @throws DataAccessException
	 */
	public Long insertUnitaArborea(UnitaArboreaVO unitaArboreaVO) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating insertUnitaArborea method in UnitaArboreaDAO\n");
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    Long idUnitaArborea = null;
	
	    try {
	    	idUnitaArborea = getNextPrimaryKey(SolmrConstants.SEQ_UNITA_ARBOREA);
	    	SolmrLogger.debug(this, "Creating db-connection in insertUnitaArborea method in UnitaArboreaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in insertUnitaArborea method in UnitaArboreaDAO and it values: "+conn+"\n");
			
			String query = " INSERT INTO DB_UNITA_ARBOREA " +
			   			   "        (ID_UNITA_ARBOREA, " +
			   			   "         DATA_INIZIO_VALIDITA) " +
			   			   " VALUES  (?, ?) ";
			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing insertUnitaArborea: "+query);
			
			stmt.setLong(1, idUnitaArborea.longValue());
			SolmrLogger.debug(this, "Value of parameter 1 [ID_UNITA_ARBOREA] in method insertUnitaArborea in UnitaArboreaDAO: "+idUnitaArborea.longValue()+"\n");
			stmt.setTimestamp(2, new Timestamp(unitaArboreaVO.getDataInizioValidita().getTime()));
			SolmrLogger.debug(this, "Value of parameter 2 [DATA_FINE_VALIDITA] in method insertUnitaArborea in UnitaArboreaDAO: "+new Timestamp(unitaArboreaVO.getDataInizioValidita().getTime())+"\n");			
			
			stmt.executeUpdate();

			stmt.close();
	    }
	    catch(SQLException exc) {
	    	SolmrLogger.error(this, "insertUnitaArborea in UnitaArboreaDAO - SQLException: "+exc);
	    	throw new DataAccessException(exc.getMessage());
	    }
	    catch (Exception ex) {
	    	SolmrLogger.error(this, "insertUnitaArborea in UnitaArboreaDAO - Generic Exception: "+ex);
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
	    		SolmrLogger.error(this, "insertUnitaArborea in UnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc);
	    		throw new DataAccessException(exc.getMessage());
	    	}
	    	catch(Exception ex) {
	    		SolmrLogger.error(this, "insertUnitaArborea in UnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex);
	    		throw new DataAccessException(ex.getMessage());
	    	}
	    }
	    SolmrLogger.debug(this, "Invocated insertUnitaArborea method in UnitaArboreaDAO\n");
	    return idUnitaArborea;
	}
}
