package it.csi.solmr.integration.anag;

import java.sql.*;

import it.csi.solmr.dto.anag.consistenza.*;
import it.csi.solmr.dto.profile.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.exception.*;
import it.csi.solmr.util.*;

/**
 * DAO che si occupa di mappare la tabella DB_NUMERO_PROTOCOLLO_GTFO
 *
 * <p>Title: SMRGAA</p>
 *
 * <p>Description: Anagrafe delle Imprese Agricole e Agro-Alimentari</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: CSI - PIEMONTE</p>
 *
 * @author Mauro Vocale
 * @version 1.0
 */
public class NumeroProtocolloGtfoDAO extends it.csi.solmr.integration.BaseDAO {
	
	public NumeroProtocolloGtfoDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public NumeroProtocolloGtfoDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo per recuperare il numero protocollo(utilizzato nel caso di fermo orologio per RPU)
	 * 
	 * @param ruoloUtenza
	 * @param anno String
	 * @return it.csi.solmr.dto.anag.consistenza.NumeroProtocolloGtfoVO
	 * @throws DataAccessException
	 */
	public NumeroProtocolloGtfoVO getNumeroProtocolloGtfo(RuoloUtenza ruoloUtenza, String anno) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getNumeroProtocolloGtfo method in NumeroProtocolloGtfoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		NumeroProtocolloGtfoVO numeroProtocolloGtfoVO = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in getNumeroProtocolloGtfo method in NumeroProtocolloGtfoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getNumeroProtocolloGtfo method in NumeroProtocolloGtfoDAO and it values: "+conn+"\n");

			String query = "";
			// Se l'utente loggato è un intermediario o un OPR GESTORE
			if(ruoloUtenza.isUtenteIntermediario() || ruoloUtenza.isUtenteOPRGestore()) {
				query = " SELECT P.ID_NUMERO_PROTOCOLLO_GTFO, " +
						"        SUBSTR(CODICE_ENTE,1,3) " +
				        "        || '.' ||               " +
				        "        SUBSTR(CODICE_ENTE,4,3) " +
				        "        || '.' ||               " +
				        "        SUBSTR(CODICE_ENTE,7,3) " +
				        "        || '.' ||               " +
				        "        P.ANNO                  " +
				        "        || '.' ||               " +
				        "        P.PROGRESSIVO AS NUMERO_PROTOCOLLO, " +
				        "        P.DATA_PROTOCOLLO " +
				        " FROM   DB_NUMERO_PROTOCOLLO_GTFO P " +
				        " WHERE  P.CODICE_ENTE = ? " +
				        " AND    P.ANNO = ? " +
				        " AND    P.PROGRESSIVO <> P.PROGR_FINE " +
				        " AND    ROWNUM = 1 " +
				        " ORDER BY P.DATA_PROTOCOLLO ASC " +
				        " FOR UPDATE ";
			}
			// Se l'utente loggato è un PA
			else {
				if(Validator.isNumericInteger(ruoloUtenza.getCodiceEnte()) && ruoloUtenza.getCodiceEnte().length() > 3) {
					query = " SELECT P.ID_NUMERO_PROTOCOLLO_GTFO, " +
							"        SUBSTR(CODICE_ENTE,1,3) " +
					        "        || '.' ||               " +
					        "        SUBSTR(CODICE_ENTE,4)   " +
					        "        || '.' ||               " +
					        "        P.ANNO                  " +
					        "        || '.' ||               " +
					        "        P.PROGRESSIVO AS NUMERO_PROTOCOLLO, " +
					        "        P.DATA_PROTOCOLLO " +
					        " FROM   DB_NUMERO_PROTOCOLLO_GTFO P " +
					        " WHERE  P.CODICE_ENTE = ? " +
					        " AND    P.ANNO = ? " +
					        " AND    P.PROGRESSIVO <> P.PROGR_FINE " +
					        " AND    ROWNUM = 1 " +
					        " ORDER BY P.DATA_PROTOCOLLO ASC " +
					        " FOR UPDATE ";

				}
				else {
					query = " SELECT P.ID_NUMERO_PROTOCOLLO_GTFO, " +
							"        SUBSTR(CODICE_ENTE,1)  " +
					        "        || '.' ||              " +
					        "        P.ANNO                 " +
					        "        || '.' ||              " +
					        "        P.PROGRESSIVO AS NUMERO_PROTOCOLLO, " +
					        "        P.DATA_PROTOCOLLO " +
					        " FROM   DB_NUMERO_PROTOCOLLO_GTFO P " +
					        " WHERE  P.CODICE_ENTE = ? " +
					        " AND    P.ANNO = ? " +
					        " AND    P.PROGRESSIVO <> P.PROGR_FINE " +
					        " AND    ROWNUM = 1 " +
					        " ORDER BY P.DATA_PROTOCOLLO ASC " +
					        " FOR UPDATE ";
				}
			}

			stmt = conn.prepareStatement(query);

			if(ruoloUtenza.isUtenteIntermediario() || ruoloUtenza.isUtenteOPRGestore()) {
				SolmrLogger.debug(this, "Value of parameter 1 [CODICE_FISCALE_INTERMEDIARIO] in getNumeroProtocolloGtfo method in NumeroProtocolloGtfoDAO: "+ruoloUtenza.getCodiceEnte()+"\n");
				stmt.setString(1, ruoloUtenza.getCodiceEnte());
			}
			else {
				SolmrLogger.debug(this, "Value of parameter 1 [SIGLA_AMMINISTRAZIONE_PA] in getNumeroProtocolloGtfo method in NumeroProtocolloGtfoDAO: "+ruoloUtenza.getSiglaAmministrazione()+"\n");
				stmt.setString(1, ruoloUtenza.getSiglaAmministrazione());
			}
			SolmrLogger.debug(this, "Value of parameter 2 [ANNO] in getNumeroProtocolloGtfo method in NumeroProtocolloGtfoDAO: "+anno+"\n");
			stmt.setString(2, anno);

			SolmrLogger.debug(this, "Executing getNumeroProtocolloGtfo: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) {
				numeroProtocolloGtfoVO = new NumeroProtocolloGtfoVO();
				numeroProtocolloGtfoVO.setIdNumeroProtocolloGtfo(new Long(rs.getLong("ID_NUMERO_PROTOCOLLO_GTFO")));
				numeroProtocolloGtfoVO.setNumeroProtocollo(rs.getString("NUMERO_PROTOCOLLO"));
				numeroProtocolloGtfoVO.setDataProtocollo(rs.getTimestamp("DATA_PROTOCOLLO"));
			}

			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getNumeroProtocolloGtfo in NumeroProtocolloGtfoDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getNumeroProtocolloGtfo in NumeroProtocolloGtfoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.error(this, "getNumeroProtocolloGtfo in NumeroProtocolloGtfoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.error(this, "getNumeroProtocolloGtfo in NumeroProtocolloGtfoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getNumeroProtocolloGtfo method in NumeroProtocolloGtfoDAO\n");
		return numeroProtocolloGtfoVO;
	}
	
	/**
	 * Metodo utilizzato per effettuare l'aggiornamento del progressivo del numero
	 * di protocollo utilizzato nel caso di fermo orologi di RPU
	 * 
	 * @param idNumeroProtocolloGtfo
	 * @throws DataAccessException
	 */
	public void aggiornaProgressivoNumeroProtocolloGtfo(Long idNumeroProtocolloGtfo) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating aggiornaProgressivoNumeroProtocolloGtfo method in NumeroProtocolloGtfoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in aggiornaProgressivoNumeroProtocolloGtfo method in NumeroProtocolloGtfoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in aggiornaProgressivoNumeroProtocolloGtfo method in NumeroProtocolloGtfoDAO and it values: "+conn+"\n");


			String query = " UPDATE DB_NUMERO_PROTOCOLLO_GTFO "+
			               " SET    PROGRESSIVO = PROGRESSIVO + 1 " +
			               " WHERE  ID_NUMERO_PROTOCOLLO_GTFO = ? ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing aggiornaProgressivoNumeroProtocolloGtfo: "+query+"\n");

			SolmrLogger.debug(this,"Value of parameter 1 [ID_NUMERO_PROTOCOLLO_GTFO] in aggiornaProgressivoNumeroProtocolloGtfo method in NumeroProtocolloGtfoDAO: "+idNumeroProtocolloGtfo+"\n");
			stmt.setLong(1, idNumeroProtocolloGtfo.longValue());

			stmt.executeUpdate();

			stmt.close();

		}
		catch (SQLException exc) {
			SolmrLogger.error(this, "aggiornaProgressivoNumeroProtocolloGtfo in NumeroProtocolloGtfoDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex) {
			SolmrLogger.error(this, "aggiornaProgressivoNumeroProtocolloGtfo in NumeroProtocolloGtfoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.error(this, "aggiornaProgressivoNumeroProtocolloGtfo in NumeroProtocolloGtfoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.error(this, "aggiornaProgressivoNumeroProtocolloGtfo in NumeroProtocolloGtfoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Executed aggiornaProgressivoNumeroProtocolloGtfo in NumeroProtocolloGtfoDAO\n");
	}
}
