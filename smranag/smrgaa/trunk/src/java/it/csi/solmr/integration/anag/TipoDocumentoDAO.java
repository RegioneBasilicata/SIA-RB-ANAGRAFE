package it.csi.solmr.integration.anag;

import java.sql.*;
import java.util.*;

import it.csi.solmr.dto.anag.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.exception.*;
import it.csi.solmr.util.*;

/**
 * DAO che si occupa di effettuare le operazioni relative alla tabella DB_TIPO_DOCUMENTO
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
public class TipoDocumentoDAO extends it.csi.solmr.integration.BaseDAO {

	public TipoDocumentoDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public TipoDocumentoDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo per recuperare tutti i dati presenti nella tabella DB_TIPO_DOCUMENTO
	 * a partire dall'id_documento_categoria
	 * 
	 * @param idCategoriaDocumento
	 * @param orderBy[]
	 * @param onlyActive
	 * @return it.csi.solmr.dto.anag.TipoDocumentoVO[]
	 * @throws DataAccessException
	 */
	public TipoDocumentoVO[] getListTipoDocumentoByIdCategoriaDocumento(Long idCategoriaDocumento, String orderBy[], 
	    boolean onlyActive, Boolean cessata) 
	  throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating getListTipoDocumentoByIdCategoriaDocumento method in TipoDocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoDocumentoVO> elencoTipoDocumento = new Vector<TipoDocumentoVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListTipoDocumentoByIdCategoriaDocumento method in TipoDocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListTipoDocumentoByIdCategoriaDocumento method in TipoDocumentoDAO and it values: "+conn+"\n");

			String query = " SELECT TD.ID_DOCUMENTO, " +
						   "        TD.ID_TIPOLOGIA_DOCUMENTO, " +
						   "        TD.DESCRIZIONE, " +
						   "        TD.CODICE_DOCUMENTO, " +
						   "        TD.FLAG_ANAG_TERR, " +
						   "        TD.FLAG_OBBLIGO_FASCICOLO, " +
						   "        TD.FLAG_OBBLIGO_DATA_FINE, " +
						   "        TD.FLAG_OBBLIGO_PROTOCOLLO, " +
						   "        TD.DATA_INIZIO_VALIDITA, " +
						   "        TD.DATA_FINE_VALIDITA, " +
						   "        TD.FLAG_OBBLIGO_PARTICELLA, " +
						   "        TD.FLAG_OBBLIGO_PROPRIETARIO, " +
						   "        TD.FLAG_OBBLIGO_ENTE_NUMERO, " +
						   "        TD.FLAG_UNIVOCITA " +
						   " FROM   DB_TIPO_DOCUMENTO TD, " +
						   "        DB_TIPO_CATEGORIA_DOCUMENTO TCD, " +
						   "        DB_DOCUMENTO_CATEGORIA DC " +
						   " WHERE  TCD.ID_CATEGORIA_DOCUMENTO = ? " +
						   " AND    TCD.ID_CATEGORIA_DOCUMENTO = DC.ID_CATEGORIA_DOCUMENTO " +
						   " AND    DC.ID_DOCUMENTO = TD.ID_DOCUMENTO ";
			if(onlyActive) 
				query +=   " AND    DATA_FINE_VALIDITA IS NULL ";
		  //DB_TIPO_DOCUMENTO.FLAG_AZIENDA_CESSATA='C' se l'azienda è cessata può vedere tutti i documenti...
		  //altrimenti bisogna filtrare i documenti che solo l'azienda cessata può vedere
		  if(cessata==null || !cessata.booleanValue()) 
        query +=   " AND nvl(TD.FLAG_DOC_PER_AZIENDA_CESSATA,'T')<>'C' ";

			String ordinamento = "";
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
				ordinamento = "ORDER BY "+criterio;
			}
			if(!ordinamento.equals("")) {
				query += ordinamento;
			}

			SolmrLogger.debug(this, "Value of parameter 1 [ID_CATEGORIA_DOCUMENTO] in getListTipoDocumentoByIdCategoriaDocumento method in TipoDocumentoDAO: "+idCategoriaDocumento+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ORDER_BY] in getListTipoDocumentoByIdCategoriaDocumento method in TipoDocumentoDAO: "+ordinamento+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ONLY_ACTIVE] in getListTipoDocumentoByIdCategoriaDocumento method in TipoDocumentoDAO: "+onlyActive+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idCategoriaDocumento.longValue());

			SolmrLogger.debug(this, "Executing getListTipoDocumentoByIdTipologiaDocumento: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				TipoDocumentoVO tipoDocumentoVO = new TipoDocumentoVO();
				tipoDocumentoVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
				tipoDocumentoVO.setIdTipologiaDocumento(new Long(rs.getLong("ID_TIPOLOGIA_DOCUMENTO")));
				tipoDocumentoVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoDocumentoVO.setCodiceDocumento(rs.getString("CODICE_DOCUMENTO"));
				tipoDocumentoVO.setFlagAnagTerr(rs.getString("FLAG_ANAG_TERR"));
				tipoDocumentoVO.setFlagObbligoFascicolo(rs.getString("FLAG_OBBLIGO_FASCICOLO"));
				tipoDocumentoVO.setFlagObbligoDataFine(rs.getString("FLAG_OBBLIGO_DATA_FINE"));
				tipoDocumentoVO.setFlagObbligoProtocollo(rs.getString("FLAG_OBBLIGO_PROTOCOLLO"));
				tipoDocumentoVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				tipoDocumentoVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				tipoDocumentoVO.setFlagObbligoParticella(rs.getString("FLAG_OBBLIGO_PARTICELLA"));
				tipoDocumentoVO.setFlagObbligoProprietario(rs.getString("FLAG_OBBLIGO_PROPRIETARIO"));
				tipoDocumentoVO.setFlagObbligoEnteNumero(rs.getString("FLAG_OBBLIGO_ENTE_NUMERO"));
				tipoDocumentoVO.setFlagUnivocita(rs.getString("FLAG_UNIVOCITA"));
				elencoTipoDocumento.add(tipoDocumentoVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListTipoDocumentoByIdTipologiaDocumento in TipoDocumentoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListTipoDocumentoByIdTipologiaDocumento in TipoDocumentoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListTipoDocumentoByIdTipologiaDocumento in TipoDocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListTipoDocumentoByIdTipologiaDocumento in TipoDocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipoDocumentoByIdTipologiaDocumento method in TipoDocumentoDAO\n");
		if(elencoTipoDocumento.size() == 0) {
			return (TipoDocumentoVO[])elencoTipoDocumento.toArray(new TipoDocumentoVO[0]);
		}
		else {
			return (TipoDocumentoVO[])elencoTipoDocumento.toArray(new TipoDocumentoVO[elencoTipoDocumento.size()]);
		}
	}
	
	/**
	 * Metodo che si occupa di estrarre l'elenco dei tipi documento in funzione della tipologia documento indicata: recupera solo
	 * i records attivi se isActive == true
	 * 
	 * @param idTipologiaDocumento Long
	 * @param isActive boolean
	 * @return it.csi.solmr.dto.anag.TipoDocumentoVO[]
	 * @throws DataAccessException
	 */
	public TipoDocumentoVO[] getListTipoDocumentoByIdTipologiaDocumento(Long idTipologiaDocumento, boolean isActive) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListTipoDocumentoByIdTipologiaDocumento method in TipoDocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoDocumentoVO> elencoTipoDocumento = new Vector<TipoDocumentoVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListTipoDocumentoByIdTipologiaDocumento method in TipoDocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListTipoDocumentoByIdTipologiaDocumento method in TipoDocumentoDAO and it values: "+conn+"\n");

			String query = " SELECT TD.ID_DOCUMENTO, " +
						   "        TD.ID_TIPOLOGIA_DOCUMENTO, " +
						   "        TD.DESCRIZIONE, " +
						   "        TD.CODICE_DOCUMENTO, " +
						   "        TD.FLAG_ANAG_TERR, " +
						   "        TD.FLAG_OBBLIGO_FASCICOLO, " +
						   "        TD.FLAG_OBBLIGO_DATA_FINE, " +
						   "        TD.FLAG_OBBLIGO_PROTOCOLLO, " +
						   "        TD.DATA_INIZIO_VALIDITA, " +
						   "        TD.DATA_FINE_VALIDITA, " +
						   "        TD.FLAG_OBBLIGO_PARTICELLA, " +
						   "        TD.FLAG_OBBLIGO_PROPRIETARIO, " +
						   "        TD.FLAG_OBBLIGO_ENTE_NUMERO, " +
						   "        TD.FLAG_UNIVOCITA " +
    			           " FROM   DB_TIPO_DOCUMENTO TD " +
    			           " WHERE  ID_TIPOLOGIA_DOCUMENTO = ? ";

			if(isActive) {
				query += " AND DATA_FINE_VALIDITA IS NULL ";
			}

			query += " ORDER BY TD.DESCRIZIONE ";

			SolmrLogger.debug(this, "Value of parameter 1 [IS_ACTIVE] in getListTipoDocumentoByIdTipologiaDocumento method in TipoDocumentoDAO: "+isActive+"\n");

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Value of parameter 2 [ID_TIPOLOGIA_DOCUMENTO] in getListTipoDocumentoByIdTipologiaDocumento method in TipoDocumentoDAO: "+idTipologiaDocumento+"\n");
			stmt.setLong(1, idTipologiaDocumento.longValue());

			SolmrLogger.debug(this, "Executing getListTipoDocumentoByIdTipologiaDocumento: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				TipoDocumentoVO tipoDocumentoVO = new TipoDocumentoVO();
				tipoDocumentoVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
				tipoDocumentoVO.setIdTipologiaDocumento(new Long(rs.getLong("ID_TIPOLOGIA_DOCUMENTO")));
				tipoDocumentoVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoDocumentoVO.setCodiceDocumento(rs.getString("CODICE_DOCUMENTO"));
				tipoDocumentoVO.setFlagAnagTerr(rs.getString("FLAG_ANAG_TERR"));
				tipoDocumentoVO.setFlagObbligoFascicolo(rs.getString("FLAG_OBBLIGO_FASCICOLO"));
				tipoDocumentoVO.setFlagObbligoDataFine(rs.getString("FLAG_OBBLIGO_DATA_FINE"));
				tipoDocumentoVO.setFlagObbligoProtocollo(rs.getString("FLAG_OBBLIGO_PROTOCOLLO"));
				tipoDocumentoVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				tipoDocumentoVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				tipoDocumentoVO.setFlagObbligoParticella(rs.getString("FLAG_OBBLIGO_PARTICELLA"));
				tipoDocumentoVO.setFlagObbligoProprietario(rs.getString("FLAG_OBBLIGO_PROPRIETARIO"));
				tipoDocumentoVO.setFlagObbligoEnteNumero(rs.getString("FLAG_OBBLIGO_ENTE_NUMERO"));
				tipoDocumentoVO.setFlagUnivocita(rs.getString("FLAG_UNIVOCITA"));
				elencoTipoDocumento.add(tipoDocumentoVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListTipoDocumentoByIdTipologiaDocumento in TipoDocumentoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListTipoDocumentoByIdTipologiaDocumento in TipoDocumentoDAO - Generic Exception: "+ex+"\n");
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
				SolmrLogger.error(this, "getListTipoDocumentoByIdTipologiaDocumento in TipoDocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListTipoDocumentoByIdTipologiaDocumento in TipoDocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipoDocumentoByIdTipologiaDocumento method in TipoDocumentoDAO\n");
		if(elencoTipoDocumento.size() == 0) {
			return (TipoDocumentoVO[])elencoTipoDocumento.toArray(new TipoDocumentoVO[0]);
		}
		else {
			return (TipoDocumentoVO[])elencoTipoDocumento.toArray(new TipoDocumentoVO[elencoTipoDocumento.size()]);
		}
	}
	
	/**
	 * Metodo per recuperare il record tipo documento a partire dalla chiave primaria
	 * 
	 * @param idDocumento Long
	 * @return TipoDocumentoVO
	 * @throws DataAccessException
	 */
	public TipoDocumentoVO findTipoDocumentoVOByPrimaryKey(Long idDocumento) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating findTipoDocumentoVOByPrimaryKey method in TipoDocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		TipoDocumentoVO tipoDocumentoVO = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in findTipoDocumentoVOByPrimaryKey method in TipoDocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in findTipoDocumentoVOByPrimaryKey method in TipoDocumentoDAO and it values: "+conn+"\n");

			String query = 
			    " SELECT TD.ID_DOCUMENTO, " +
					"        TD.ID_TIPOLOGIA_DOCUMENTO, " +
					"        TD.DESCRIZIONE, " +
					"        TD.CODICE_DOCUMENTO, " +
					"        TD.FLAG_ANAG_TERR, " +
					"        TD.FLAG_OBBLIGO_FASCICOLO, " +
					"        TD.FLAG_OBBLIGO_DATA_FINE, " +
					"        TD.FLAG_OBBLIGO_PROTOCOLLO, " +
					"        TD.DATA_INIZIO_VALIDITA, " +
					"        TD.DATA_FINE_VALIDITA, " +
					"        TD.FLAG_OBBLIGO_PARTICELLA, " +
					"        TD.FLAG_OBBLIGO_PROPRIETARIO, " +
					"        TD.FLAG_OBBLIGO_ENTE_NUMERO, " +
					"        TD.FLAG_UNIVOCITA, " +
					"        TD.EXT_ID_TIPO_DOCUMENTO_INDEX, " +
					"        TD.FLAG_ANNULLABILE " +
				  " FROM   DB_TIPO_DOCUMENTO TD " +
				  " WHERE  ID_DOCUMENTO = ? ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Value of parameter 1 [ID_DOCUMENTO] in findTipoDocumentoVOByPrimaryKey method in TipoDocumentoDAO: "+idDocumento+"\n");
			stmt.setLong(1, idDocumento.longValue());

			SolmrLogger.debug(this, "Executing findTipoDocumentoVOByPrimaryKey: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) {
				tipoDocumentoVO = new TipoDocumentoVO();
				tipoDocumentoVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
				tipoDocumentoVO.setIdTipologiaDocumento(new Long(rs.getLong("ID_TIPOLOGIA_DOCUMENTO")));
				tipoDocumentoVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoDocumentoVO.setCodiceDocumento(rs.getString("CODICE_DOCUMENTO"));
				tipoDocumentoVO.setFlagAnagTerr(rs.getString("FLAG_ANAG_TERR"));
				tipoDocumentoVO.setFlagObbligoFascicolo(rs.getString("FLAG_OBBLIGO_FASCICOLO"));
				tipoDocumentoVO.setFlagObbligoDataFine(rs.getString("FLAG_OBBLIGO_DATA_FINE"));
				tipoDocumentoVO.setFlagObbligoProtocollo(rs.getString("FLAG_OBBLIGO_PROTOCOLLO"));
				tipoDocumentoVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				tipoDocumentoVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				tipoDocumentoVO.setFlagObbligoParticella(rs.getString("FLAG_OBBLIGO_PARTICELLA"));
				tipoDocumentoVO.setFlagObbligoProprietario(rs.getString("FLAG_OBBLIGO_PROPRIETARIO"));
				tipoDocumentoVO.setFlagObbligoEnteNumero(rs.getString("FLAG_OBBLIGO_ENTE_NUMERO"));
				tipoDocumentoVO.setFlagUnivocita(rs.getString("FLAG_UNIVOCITA"));
				tipoDocumentoVO.setExtIdTipoDocumentoIndex(checkLongNull(rs.getString("EXT_ID_TIPO_DOCUMENTO_INDEX")));
				tipoDocumentoVO.setFlagAnnullabile(rs.getString("FLAG_ANNULLABILE"));
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "findTipoDocumentoVOByPrimaryKey in TipoDocumentoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "findTipoDocumentoVOByPrimaryKey in TipoDocumentoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.error(this, "findTipoDocumentoVOByPrimaryKey in TipoDocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.error(this, "findTipoDocumentoVOByPrimaryKey in TipoDocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated findTipoDocumentoVOByPrimaryKey method in TipoDocumentoDAO\n");
		return tipoDocumentoVO;
	}
	
	/**
	 * Metodo per recuperare tutti i dati attivi presenti nella tabella DB_TIPO_DOCUMENTO
	 * a partire dall'id_documento_categoria più quelli cessati relativi all'azienda
	 * a cui sono collegati i documento
	 * 
	 * @param idCategoriaDocumento
	 * @param idAzienda
	 * @param orderBy[]
	 * @return it.csi.solmr.dto.anag.TipoDocumentoVO[]
	 * @throws DataAccessException
	 */
	public TipoDocumentoVO[] getListTipoDocumentoByIdCategoriaDocumentoAndIdAzienda(Long idCategoriaDocumento, Long idAzienda, String orderBy[]) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListTipoDocumentoByIdCategoriaDocumentoAndIdAzienda method in TipoDocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoDocumentoVO> elencoTipoDocumento = new Vector<TipoDocumentoVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListTipoDocumentoByIdCategoriaDocumentoAndIdAzienda method in TipoDocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListTipoDocumentoByIdCategoriaDocumentoAndIdAzienda method in TipoDocumentoDAO and it values: "+conn+"\n");

			String query = " SELECT TD.ID_DOCUMENTO, " +
					       "        TD.ID_TIPOLOGIA_DOCUMENTO, " +
					       "        TD.DESCRIZIONE, " +
					       "        TD.CODICE_DOCUMENTO, " +
					       "        TD.FLAG_ANAG_TERR, " +	
					       "        TD.FLAG_OBBLIGO_FASCICOLO, " +
					       "        TD.FLAG_OBBLIGO_DATA_FINE, " +
					       "        TD.FLAG_OBBLIGO_PROTOCOLLO, " +
					       "        TD.DATA_INIZIO_VALIDITA, " +
					       "        TD.DATA_FINE_VALIDITA, " +
					       "        TD.FLAG_OBBLIGO_PARTICELLA, " +
					       "        TD.FLAG_OBBLIGO_PROPRIETARIO, " +
					       "        TD.FLAG_OBBLIGO_ENTE_NUMERO, " +
					       "        TD.FLAG_UNIVOCITA " +
					       " FROM   DB_TIPO_DOCUMENTO TD, " +
					       "        DB_TIPO_CATEGORIA_DOCUMENTO TCD, " +
					       "        DB_DOCUMENTO_CATEGORIA DC " +
					       " WHERE  TCD.ID_CATEGORIA_DOCUMENTO = ? " +
					       " AND    TCD.ID_CATEGORIA_DOCUMENTO = DC.ID_CATEGORIA_DOCUMENTO " +
					       " AND    DC.ID_DOCUMENTO = TD.ID_DOCUMENTO " +
					       " AND    TD.DATA_FINE_VALIDITA IS NULL " +  
					       " UNION " +
					       " SELECT TD.ID_DOCUMENTO, " +
					       "        TD.ID_TIPOLOGIA_DOCUMENTO, " +
					       "        TD.DESCRIZIONE, " +
					       "        TD.CODICE_DOCUMENTO, " +
					       "        TD.FLAG_ANAG_TERR, " +
					       "        TD.FLAG_OBBLIGO_FASCICOLO, " +
					       "        TD.FLAG_OBBLIGO_DATA_FINE, " +
					       "        TD.FLAG_OBBLIGO_PROTOCOLLO, " +
					       "        TD.DATA_INIZIO_VALIDITA, " +
					       "        TD.DATA_FINE_VALIDITA, " +
					       "        TD.FLAG_OBBLIGO_PARTICELLA, " +
					       "        TD.FLAG_OBBLIGO_PROPRIETARIO, " +
					       "        TD.FLAG_OBBLIGO_ENTE_NUMERO, " +
					       "        TD.FLAG_UNIVOCITA " +
					       " FROM   DB_TIPO_DOCUMENTO TD, " +
					       "        DB_TIPO_CATEGORIA_DOCUMENTO TCD, " +
					       "        DB_DOCUMENTO_CATEGORIA DC, " +
					       "        DB_DOCUMENTO D, " +
					       "        DB_ANAGRAFICA_AZIENDA AA " +
					       " WHERE  TCD.ID_CATEGORIA_DOCUMENTO = ? " +
					       " AND    TCD.ID_CATEGORIA_DOCUMENTO = DC.ID_CATEGORIA_DOCUMENTO " +
					       " AND    DC.ID_DOCUMENTO = TD.ID_DOCUMENTO " +
					       " AND    TD.DATA_FINE_VALIDITA IS NOT NULL " +
					       " AND    AA.ID_AZIENDA = ? " +
					       " AND    AA.ID_AZIENDA = D.ID_AZIENDA " +
					       " AND    D.EXT_ID_DOCUMENTO = TD.ID_DOCUMENTO ";

			String ordinamento = "";
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
				ordinamento = "ORDER BY "+criterio;
			}
			if(!ordinamento.equals("")) {
				query += ordinamento;
			}

			SolmrLogger.debug(this, "Value of parameter 1 [ID_CATEGORIA_DOCUMENTO] in getListTipoDocumentoByIdCategoriaDocumentoAndIdAzienda method in TipoDocumentoDAO: "+idCategoriaDocumento+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ID_AZIENDA] in getListTipoDocumentoByIdCategoriaDocumentoAndIdAzienda method in TipoDocumentoDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ORDER_BY] in getListTipoDocumentoByIdCategoriaDocumentoAndIdAzienda method in TipoDocumentoDAO: "+ordinamento+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idCategoriaDocumento.longValue());
			stmt.setLong(2, idCategoriaDocumento.longValue());
			stmt.setLong(3, idAzienda.longValue());

			SolmrLogger.debug(this, "Executing getListTipoDocumentoByIdCategoriaDocumentoAndIdAzienda: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				TipoDocumentoVO tipoDocumentoVO = new TipoDocumentoVO();
				tipoDocumentoVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
				tipoDocumentoVO.setIdTipologiaDocumento(new Long(rs.getLong("ID_TIPOLOGIA_DOCUMENTO")));
				tipoDocumentoVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoDocumentoVO.setCodiceDocumento(rs.getString("CODICE_DOCUMENTO"));
				tipoDocumentoVO.setFlagAnagTerr(rs.getString("FLAG_ANAG_TERR"));
				tipoDocumentoVO.setFlagObbligoFascicolo(rs.getString("FLAG_OBBLIGO_FASCICOLO"));
				tipoDocumentoVO.setFlagObbligoDataFine(rs.getString("FLAG_OBBLIGO_DATA_FINE"));
				tipoDocumentoVO.setFlagObbligoProtocollo(rs.getString("FLAG_OBBLIGO_PROTOCOLLO"));
				tipoDocumentoVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				tipoDocumentoVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				tipoDocumentoVO.setFlagObbligoParticella(rs.getString("FLAG_OBBLIGO_PARTICELLA"));
				tipoDocumentoVO.setFlagObbligoProprietario(rs.getString("FLAG_OBBLIGO_PROPRIETARIO"));
				tipoDocumentoVO.setFlagObbligoEnteNumero(rs.getString("FLAG_OBBLIGO_ENTE_NUMERO"));
				tipoDocumentoVO.setFlagUnivocita(rs.getString("FLAG_UNIVOCITA"));
				elencoTipoDocumento.add(tipoDocumentoVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListTipoDocumentoByIdCategoriaDocumentoAndIdAzienda in TipoDocumentoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListTipoDocumentoByIdCategoriaDocumentoAndIdAzienda in TipoDocumentoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListTipoDocumentoByIdCategoriaDocumentoAndIdAzienda in TipoDocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListTipoDocumentoByIdCategoriaDocumentoAndIdAzienda in TipoDocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipoDocumentoByIdCategoriaDocumentoAndIdAzienda method in TipoDocumentoDAO\n");
		if(elencoTipoDocumento.size() == 0) {
			return (TipoDocumentoVO[])elencoTipoDocumento.toArray(new TipoDocumentoVO[0]);
		}
		else {
			return (TipoDocumentoVO[])elencoTipoDocumento.toArray(new TipoDocumentoVO[elencoTipoDocumento.size()]);
		}
	}
	
	/**
	 * Metodo che mi permette di recuperare l'elenco dei tipi documenti relativi
	 * ad un determinato id controllo
	 * 
	 * @param idControllo
	 * @param onlyActive
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.TipoDocumentoVO[]
	 * @throws DataAccessException
	 */
	public TipoDocumentoVO[] getListTipoDocumentoByIdControllo(Long idControllo, boolean onlyActive, String orderBy[]) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListTipoDocumentoByIdControllo method in TipoDocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoDocumentoVO> elencoTipoDocumento = new Vector<TipoDocumentoVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListTipoDocumentoByIdControllo method in TipoDocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListTipoDocumentoByIdControllo method in TipoDocumentoDAO and it values: "+conn+"\n");

			String query = " SELECT TD.ID_DOCUMENTO, " +
					       "        TD.ID_TIPOLOGIA_DOCUMENTO, " +
					       "        TD.DESCRIZIONE, " +
					       "        TD.CODICE_DOCUMENTO, " +
					       "        TD.FLAG_ANAG_TERR, " +	
					       "        TD.FLAG_OBBLIGO_FASCICOLO, " +
					       "        TD.FLAG_OBBLIGO_DATA_FINE, " +
					       "        TD.FLAG_OBBLIGO_PROTOCOLLO, " +
					       "        TD.DATA_INIZIO_VALIDITA, " +
					       "        TD.DATA_FINE_VALIDITA, " +
					       "        TD.FLAG_OBBLIGO_PARTICELLA, " +
					       "        TD.FLAG_OBBLIGO_PROPRIETARIO, " +
					       "        TD.FLAG_OBBLIGO_ENTE_NUMERO, " +
					       "        TD.FLAG_UNIVOCITA " +
					       " FROM   DB_TIPO_DOCUMENTO TD, " +
					       "        DB_DOCUMENTO_CONTROLLO DC " +
					       " WHERE  DC.ID_CONTROLLO = ? " +
					       " AND    DC.EXT_ID_DOCUMENTO = TD.ID_DOCUMENTO ";
			if(onlyActive) {
				query +=   " AND    TD.DATA_FINE_VALIDITA IS NULL ";
			}

			String ordinamento = "";
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
				ordinamento = "ORDER BY "+criterio;
			}
			if(!ordinamento.equals("")) {
				query += ordinamento;
			}

			SolmrLogger.debug(this, "Value of parameter 1 [ID_CONTROLLO] in getListTipoDocumentoByIdControllo method in TipoDocumentoDAO: "+idControllo+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ONLY_ACTIVE] in getListTipoDocumentoByIdControllo method in TipoDocumentoDAO: "+onlyActive+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idControllo.longValue());

			SolmrLogger.debug(this, "Executing getListTipoDocumentoByIdControllo: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				TipoDocumentoVO tipoDocumentoVO = new TipoDocumentoVO();
				tipoDocumentoVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
				tipoDocumentoVO.setIdTipologiaDocumento(new Long(rs.getLong("ID_TIPOLOGIA_DOCUMENTO")));
				tipoDocumentoVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoDocumentoVO.setCodiceDocumento(rs.getString("CODICE_DOCUMENTO"));
				tipoDocumentoVO.setFlagAnagTerr(rs.getString("FLAG_ANAG_TERR"));
				tipoDocumentoVO.setFlagObbligoFascicolo(rs.getString("FLAG_OBBLIGO_FASCICOLO"));
				tipoDocumentoVO.setFlagObbligoDataFine(rs.getString("FLAG_OBBLIGO_DATA_FINE"));
				tipoDocumentoVO.setFlagObbligoProtocollo(rs.getString("FLAG_OBBLIGO_PROTOCOLLO"));
				tipoDocumentoVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				tipoDocumentoVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				tipoDocumentoVO.setFlagObbligoParticella(rs.getString("FLAG_OBBLIGO_PARTICELLA"));
				tipoDocumentoVO.setFlagObbligoProprietario(rs.getString("FLAG_OBBLIGO_PROPRIETARIO"));
				tipoDocumentoVO.setFlagObbligoEnteNumero(rs.getString("FLAG_OBBLIGO_ENTE_NUMERO"));
				tipoDocumentoVO.setFlagUnivocita(rs.getString("FLAG_UNIVOCITA"));
				elencoTipoDocumento.add(tipoDocumentoVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListTipoDocumentoByIdControllo in TipoDocumentoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListTipoDocumentoByIdControllo in TipoDocumentoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListTipoDocumentoByIdControllo in TipoDocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListTipoDocumentoByIdControllo in TipoDocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipoDocumentoByIdControllo method in TipoDocumentoDAO\n");
		if(elencoTipoDocumento.size() == 0) {
			return (TipoDocumentoVO[])elencoTipoDocumento.toArray(new TipoDocumentoVO[0]);
		}
		else {
			return (TipoDocumentoVO[])elencoTipoDocumento.toArray(new TipoDocumentoVO[elencoTipoDocumento.size()]);
		}
	}
}
