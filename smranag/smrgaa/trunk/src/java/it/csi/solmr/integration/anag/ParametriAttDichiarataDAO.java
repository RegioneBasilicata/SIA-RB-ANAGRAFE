package it.csi.solmr.integration.anag;

import it.csi.solmr.dto.anag.attestazioni.*;
import it.csi.solmr.exception.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.util.*;

import java.sql.*;

public class ParametriAttDichiarataDAO extends it.csi.solmr.integration.BaseDAO {


	public ParametriAttDichiarataDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public ParametriAttDichiarataDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo che mi consente di eliminare fisicamente tutte le occorrenze presenti su
	 * DB_PARAMETRI_ATT_DICHIARATA
	 *
	 * @param codiceFotografiaTerreni
	 * @param voceMenu
	 * @throws DataAccessException
	 */
	public void deleteParametriAttDichiarataByCodiceFotografiaTerreni(String codiceFotografiaTerreni, String voceMenu) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating deleteParametriAttDichiarataByCodiceFotografiaTerreni method in ParametriAttDichiarataDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in deleteParametriAttDichiarataByCodiceFotografiaTerreni method in ParametriAttDichiarataDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in deleteParametriAttDichiarataByCodiceFotografiaTerreni method in ParametriAttDichiarataDAO and it values: "+conn+"\n");
			
			// Se la voce di menù è valorizzata, gestisco la delete per la logica
			// delle attestazioni e degli allegati
			String query = "";
			if(Validator.isNotEmpty(voceMenu)) {
				query  = " DELETE FROM  DB_PARAMETRI_ATT_DICHIARATA "+
        		         "        WHERE ID_ATTESTAZIONE_DICHIARATA IN(SELECT AD.ID_ATTESTAZIONE_DICHIARATA " +
        		         "                                            FROM   DB_ATTESTAZIONE_DICHIARATA AD, " +
        		         "                                                   DB_TIPO_ATTESTAZIONE TA " +
        		         "                                            WHERE  AD.CODICE_FOTOGRAFIA_TERRENI = ? " +
        		         "                                            AND    AD.ID_ATTESTAZIONE = TA.ID_ATTESTAZIONE " +
        		         "                                            AND    TA.VOCE_MENU = ?) ";
			}
			else {
				query  = " DELETE FROM  DB_PARAMETRI_ATT_DICHIARATA "+
					     "        WHERE ID_ATTESTAZIONE_DICHIARATA IN(SELECT ID_ATTESTAZIONE_DICHIARATA " +
					     "                                            FROM   DB_ATTESTAZIONE_DICHIARATA " +
					     "                                            WHERE  CODICE_FOTOGRAFIA_TERRENI = ?) ";
			}

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing deleteParametriAttDichiarataByCodiceFotografiaTerreni: "+query);

			SolmrLogger.debug(this, "Value of parameter 1 [CODICE_FOTOGRAFIA_TERRENI] in method deleteParametriAttDichiarataByCodiceFotografiaTerreni in ParametriAttDichiarataDAO: "+codiceFotografiaTerreni+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [VOCE_MENU] in method deleteParametriAttDichiarataByCodiceFotografiaTerreni in ParametriAttDichiarataDAO: "+voceMenu+"\n");
			
			stmt.setString(1, codiceFotografiaTerreni);
			if(Validator.isNotEmpty(voceMenu)) {
				stmt.setString(2, voceMenu);
			}

			stmt.executeUpdate();

			stmt.close();
		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "SQLException in deleteParametriAttDichiarataByCodiceFotografiaTerreni: "+exc);
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "Generic Exception in deleteParametriAttDichiarataByCodiceFotografiaTerreni: "+ex);
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
				SolmrLogger.error(this, "SQLException while closing Statement and Connection in deleteParametriAttDichiarataByCodiceFotografiaTerreni: "+exc);
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in deleteParametriAttDichiarataByCodiceFotografiaTerreni: "+ex);
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated deleteParametriAttDichiarataByCodiceFotografiaTerreni method in ParametriAttDichiarataDAO\n");
	}
	
	/**
	 * Metodo per effettuare l'inserimento massivo di tutti i parametri inseriti dall'utente
	 * legati agli allegati di una dichiarazione di consistenza
	 *  
	 * @param elencoParametri
	 * @throws DataAccessException
	 */
	public void insertAllParametriAttDichiarata(ParametriAttDichiarataVO[] elencoParametri) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating insertAllParametriAttDichiarata method in ParametriAttDichiarataDAO\n");
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    Long[] idParametriAttDichiarata = new Long[elencoParametri.length];
	
	    try {
	    	for(int i = 0; i < elencoParametri.length; i++) {
	    		idParametriAttDichiarata[i] = getNextPrimaryKey(SolmrConstants.SEQ_PARAMETRI_ATT_DICHIARATA);
	    	}
	    	SolmrLogger.debug(this, "Creating db-connection in insertAllParametriAttDichiarata method in ParametriAttDichiarataDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in insertAllParametriAttDichiarata method in ParametriAttDichiarataDAO and it values: "+conn+"\n");
			
			String query = " INSERT INTO DB_PARAMETRI_ATT_DICHIARATA " +
			   			   "        (ID_PARAMETRI_ATT_DICHIARATA, " +
			   			   "         ID_ATTESTAZIONE_DICHIARATA, " +
			   			   "         PARAMETRO_1, " +
			   			   "         PARAMETRO_2, " +
			   			   "         PARAMETRO_3, " +
			   			   "         PARAMETRO_4, " +
			   			   "         PARAMETRO_5) " +
			   			   " VALUES  (?, ?, ?, ?, ?, ?, ?) ";
			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing insertAllParametriAttDichiarata: "+query);
			
			for(int i = 0; i < elencoParametri.length; i++) {
				ParametriAttDichiarataVO parametriAttDichiarataVO = elencoParametri[i];
				stmt.setLong(1, idParametriAttDichiarata[i].longValue());
				SolmrLogger.debug(this, "Value of parameter 1 [ID_PARAMETRI_ATT_DICHIARATA] in method insertAllParametriAttDichiarata in ParametriAttDichiarataDAO: "+idParametriAttDichiarata[i].longValue()+"\n");
				stmt.setLong(2, parametriAttDichiarataVO.getIdAttestazioneDichiarata().longValue());
				SolmrLogger.debug(this, "Value of parameter 2 [ID_ATTESTAZIONE_DICHIARATA] in method insertAllParametriAttDichiarata in ParametriAttDichiarataDAO: "+parametriAttDichiarataVO.getIdAttestazioneDichiarata().longValue()+"\n");
				stmt.setString(3, parametriAttDichiarataVO.getParametro1());
				SolmrLogger.debug(this, "Value of parameter 3 [PARAMETRO_1] in method insertAllParametriAttDichiarata in ParametriAttDichiarataDAO: "+parametriAttDichiarataVO.getParametro1()+"\n");
				stmt.setString(4, parametriAttDichiarataVO.getParametro2());
				SolmrLogger.debug(this, "Value of parameter 4 [PARAMETRO_2] in method insertAllParametriAttDichiarata in ParametriAttDichiarataDAO: "+parametriAttDichiarataVO.getParametro2()+"\n");
				stmt.setString(5, parametriAttDichiarataVO.getParametro3());
				SolmrLogger.debug(this, "Value of parameter 5 [PARAMETRO_3] in method insertAllParametriAttDichiarata in ParametriAttDichiarataDAO: "+parametriAttDichiarataVO.getParametro3()+"\n");
				stmt.setString(6, parametriAttDichiarataVO.getParametro4());
				SolmrLogger.debug(this, "Value of parameter 6 [PARAMETRO_4] in method insertAllParametriAttDichiarata in ParametriAttDichiarataDAO: "+parametriAttDichiarataVO.getParametro4()+"\n");
				stmt.setString(7, parametriAttDichiarataVO.getParametro5());
				SolmrLogger.debug(this, "Value of parameter 7 [PARAMETRO_5] in method insertAllParametriAttDichiarata in ParametriAttDichiarataDAO: "+parametriAttDichiarataVO.getParametro5()+"\n");
				stmt.addBatch();
			}

			int[] risultati = stmt.executeBatch();
			for(int i = 0; i < risultati.length; i++) {
				int risultato = risultati[i];
				if(risultato < 0 && risultato != -2) {
					throw new DataAccessException();
				}
			}

			stmt.close();
	    }
	    catch(SQLException exc) {
	    	SolmrLogger.error(this, "insertAllParametriAttDichiarata in ParametriAttDichiarataDAO - SQLException: "+exc);
	    	throw new DataAccessException(exc.getMessage());
	    }
	    catch (Exception ex) {
	    	SolmrLogger.error(this, "insertAllParametriAttDichiarata in ParametriAttDichiarataDAO - Generic Exception: "+ex);
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
	    		SolmrLogger.error(this, "insertAllParametriAttDichiarata in ParametriAttDichiarataDAO - SQLException while closing Statement and Connection: "+exc);
	    		throw new DataAccessException(exc.getMessage());
	    	}
	    	catch(Exception ex) {
	    		SolmrLogger.error(this, "insertAllParametriAttDichiarata in ParametriAttDichiarataDAO - Generic Exception while closing Statement and Connection: "+ex);
	    		throw new DataAccessException(ex.getMessage());
	    	}
	    }
	    SolmrLogger.debug(this, "Invocated insertAllParametriAttDichiarata method in ParametriAttDichiarataDAO\n");
	}
}
