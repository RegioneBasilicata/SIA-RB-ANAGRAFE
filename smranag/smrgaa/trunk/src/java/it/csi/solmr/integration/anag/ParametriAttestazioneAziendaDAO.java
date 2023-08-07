package it.csi.solmr.integration.anag;

import it.csi.solmr.dto.anag.attestazioni.ParametriAttAziendaVO;
import it.csi.solmr.exception.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.util.*;

import java.sql.*;

public class ParametriAttestazioneAziendaDAO extends it.csi.solmr.integration.BaseDAO {


	public ParametriAttestazioneAziendaDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public ParametriAttestazioneAziendaDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo che mi consente di eliminare fisicamente tutte le occorrenze presenti su
	 * DB_PARAMETRI_ATTESTAZIONE_AZIENDA
	 *
	 * @param idAzienda
	 * @param voceMenu
	 * @throws DataAccessException
	 */
	public void deleteParametriAttestazioneAziendaByIdAzienda(Long idAzienda, String voceMenu) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating deleteParametriAttestazioneAziendaByIdAzienda method in ParametriAttestazioneAziendaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in deleteParametriAttestazioneAziendaByIdAzienda method in ParametriAttestazioneAziendaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in deleteParametriAttestazioneAziendaByIdAzienda method in ParametriAttestazioneAziendaDAO and it values: "+conn+"\n");

			String query  = " DELETE FROM  DB_PARAMETRI_ATT_AZIENDA "+
                    		"        WHERE ID_ATTESTAZIONE_AZIENDA IN(SELECT AA.ID_ATTESTAZIONE_AZIENDA " +
                    		"                                         FROM   DB_ATTESTAZIONE_AZIENDA AA, " +
                    		"                                                DB_TIPO_ATTESTAZIONE TA" +
                    		"                                         WHERE  AA.ID_AZIENDA = ? " +
                    		"                                         AND    AA.ID_ATTESTAZIONE = TA.ID_ATTESTAZIONE " +
                    		"                                         AND    TA.VOCE_MENU = ?) ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing deleteParametriAttestazioneAziendaByIdAzienda: "+query);

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in method deleteParametriAttestazioneAziendaByIdAzienda in ParametriAttestazioneAziendaDAO: "+idAzienda.longValue()+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [VOCE_MENU] in method deleteParametriAttestazioneAziendaByIdAzienda in ParametriAttestazioneAziendaDAO: "+voceMenu+"\n");
			
			stmt.setLong(1, idAzienda.longValue());
			stmt.setString(2, voceMenu);

			stmt.executeUpdate();

			stmt.close();
		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "SQLException in deleteParametriAttestazioneAziendaByIdAzienda: "+exc);
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "Generic Exception in deleteParametriAttestazioneAziendaByIdAzienda: "+ex);
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
				SolmrLogger.error(this, "SQLException while closing Statement and Connection in deleteParametriAttestazioneAziendaByIdAzienda: "+exc);
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in deleteParametriAttestazioneAziendaByIdAzienda: "+ex);
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated deleteParametriAttestazioneAziendaByIdAzienda method in ParametriAttestazioneAziendaDAO\n");
	}
	
	/**
	 * Metodo per effettuare l'inserimento massivo di tutti i parametri inseriti dall'utente
	 * legati alle attestazioni di un'azienda
	 *  
	 * @param elencoParametri
	 * @throws DataAccessException
	 */
	public void insertAllParametriAttAzienda(ParametriAttAziendaVO[] elencoParametri) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating insertAllParametriAttAzienda method in ParametriAttestazioneAziendaDAO\n");
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    Long[] idParametriAttAzienda = new Long[elencoParametri.length];
	
	    try {
	    	for(int i = 0; i < elencoParametri.length; i++) {
	    		idParametriAttAzienda[i] = getNextPrimaryKey(SolmrConstants.SEQ_PARAMETRI_ATT_AZIENDA);
	    	}
	    	SolmrLogger.debug(this, "Creating db-connection in insertAllParametriAttAzienda method in ParametriAttestazioneAziendaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in insertAllParametriAttAzienda method in ParametriAttestazioneAziendaDAO and it values: "+conn+"\n");
			
			String query = " INSERT INTO DB_PARAMETRI_ATT_AZIENDA " +
			   			   "        (ID_PARAMETRI_ATT_AZIENDA, " +
			   			   "         ID_ATTESTAZIONE_AZIENDA, " +
			   			   "         PARAMETRO_1, " +
			   			   "         PARAMETRO_2, " +
			   			   "         PARAMETRO_3, " +
			   			   "         PARAMETRO_4, " +
			   			   "         PARAMETRO_5) " +
			   			   " VALUES  (?, ?, ?, ?, ?, ?, ?) ";
			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing insertAllParametriAttAzienda: "+query);
			
			for(int i = 0; i < elencoParametri.length; i++) {
				ParametriAttAziendaVO parametriAttAziendaVO = elencoParametri[i];
				stmt.setLong(1, idParametriAttAzienda[i].longValue());
				SolmrLogger.debug(this, "Value of parameter 1 [ID_PARAMETRI_ATT_AZIENDA] in method insertAllParametriAttAzienda in ParametriAttestazioneAziendaDAO: "+idParametriAttAzienda[i].longValue()+"\n");
				stmt.setLong(2, parametriAttAziendaVO.getIdAttestazioneAzienda().longValue());
				SolmrLogger.debug(this, "Value of parameter 2 [ID_ATTESTAZIONE_AZIENDA] in method insertAllParametriAttAzienda in ParametriAttestazioneAziendaDAO: "+parametriAttAziendaVO.getIdAttestazioneAzienda().longValue()+"\n");
				stmt.setString(3, parametriAttAziendaVO.getParametro1());
				SolmrLogger.debug(this, "Value of parameter 3 [PARAMETRO_1] in method insertAllParametriAttAzienda in ParametriAttestazioneAziendaDAO: "+parametriAttAziendaVO.getParametro1()+"\n");
				stmt.setString(4, parametriAttAziendaVO.getParametro2());
				SolmrLogger.debug(this, "Value of parameter 4 [PARAMETRO_2] in method insertAllParametriAttAzienda in ParametriAttestazioneAziendaDAO: "+parametriAttAziendaVO.getParametro2()+"\n");
				stmt.setString(5, parametriAttAziendaVO.getParametro3());
				SolmrLogger.debug(this, "Value of parameter 5 [PARAMETRO_3] in method insertAllParametriAttAzienda in ParametriAttestazioneAziendaDAO: "+parametriAttAziendaVO.getParametro3()+"\n");
				stmt.setString(6, parametriAttAziendaVO.getParametro4());
				SolmrLogger.debug(this, "Value of parameter 6 [PARAMETRO_4] in method insertAllParametriAttAzienda in ParametriAttestazioneAziendaDAO: "+parametriAttAziendaVO.getParametro4()+"\n");
				stmt.setString(7, parametriAttAziendaVO.getParametro5());
				SolmrLogger.debug(this, "Value of parameter 7 [PARAMETRO_5] in method insertAllParametriAttAzienda in ParametriAttestazioneAziendaDAO: "+parametriAttAziendaVO.getParametro5()+"\n");
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
	    	SolmrLogger.error(this, "insertAllParametriAttAzienda in ParametriAttestazioneAziendaDAO - SQLException: "+exc);
	    	throw new DataAccessException(exc.getMessage());
	    }
	    catch (Exception ex) {
	    	SolmrLogger.error(this, "insertAllParametriAttAzienda in ParametriAttestazioneAziendaDAO - Generic Exception: "+ex);
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
	    		SolmrLogger.error(this, "insertAllParametriAttAzienda in ParametriAttestazioneAziendaDAO - SQLException while closing Statement and Connection: "+exc);
	    		throw new DataAccessException(exc.getMessage());
	    	}
	    	catch(Exception ex) {
	    		SolmrLogger.error(this, "insertAllParametriAttAzienda in ParametriAttestazioneAziendaDAO - Generic Exception while closing Statement and Connection: "+ex);
	    		throw new DataAccessException(ex.getMessage());
	    	}
	    }
	    SolmrLogger.debug(this, "Invocated insertAllParametriAttAzienda method in ParametriAttestazioneAziendaDAO\n");
	}
}
