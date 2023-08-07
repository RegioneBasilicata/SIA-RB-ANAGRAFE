package it.csi.solmr.integration.anag;

import java.sql.*;
import java.util.*;

import it.csi.solmr.dto.anag.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.exception.*;
import it.csi.solmr.util.*;

/**
 * DAO che si occupa di effettuare le operazioni relative alla tabella DB_TIPO_CATEGORIA_DOCUMENTO
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
public class TipoCategoriaDocumentoDAO extends it.csi.solmr.integration.BaseDAO {

	public TipoCategoriaDocumentoDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public TipoCategoriaDocumentoDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo per recuperare tutti i dati presenti nella tabella DB_CATEGORIA_DOCUMENTO
	 * 
	 * @param idTipologiaDocumento
	 * @param orderBy[]
	 * @return it.csi.solmr.dto.anag.TipoCategoriaDocumentoVO[]
	 * @throws DataAccessException
	 */
	public TipoCategoriaDocumentoVO[] getListTipoCategoriaDocumentoByIdTipologiaDocumento(Long idTipologiaDocumento, String orderBy[]) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListTipoCategoriaDocumentoByIdTipologiaDocumento method in TipoCategoriaDocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoCategoriaDocumentoVO> elencoTipoCategoriaDocumento = new Vector<TipoCategoriaDocumentoVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListTipoCategoriaDocumentoByIdTipologiaDocumento method in TipoCategoriaDocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListTipoCategoriaDocumentoByIdTipologiaDocumento method in TipoCategoriaDocumentoDAO and it values: "+conn+"\n");

			String query = " SELECT ID_CATEGORIA_DOCUMENTO, " +
						   "        ID_TIPOLOGIA_DOCUMENTO, " +
						   "        DESCRIZIONE, " +
						   "        IDENTIFICATIVO, " +
						   "        TIPO_IDENTIFICATIVO " +
						   " FROM   DB_TIPO_CATEGORIA_DOCUMENTO " +
						   " WHERE  ID_TIPOLOGIA_DOCUMENTO = ? ";

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

			SolmrLogger.debug(this, "Value of parameter 1 [ID_TIPOLOGIA_DOCUMENTO] in getListTipoCategoriaDocumentoByIdTipologiaDocumento method in TipoCategoriaDocumentoDAO: "+idTipologiaDocumento+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ORDER_BY] in getListTipoCategoriaDocumentoByIdTipologiaDocumento method in TipoCategoriaDocumentoDAO: "+ordinamento+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idTipologiaDocumento.longValue());
			
			SolmrLogger.debug(this, "Executing getListTipoCategoriaDocumentoByIdTipologiaDocumento: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				TipoCategoriaDocumentoVO tipoCategoriaDocumentoVO = new TipoCategoriaDocumentoVO();
				tipoCategoriaDocumentoVO.setIdCategoriaDocumento(new Long(rs.getLong("ID_CATEGORIA_DOCUMENTO")));
				tipoCategoriaDocumentoVO.setIdTipologiaDocumento(new Long(rs.getLong("ID_TIPOLOGIA_DOCUMENTO")));
				tipoCategoriaDocumentoVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoCategoriaDocumentoVO.setIdentificativo(rs.getString("IDENTIFICATIVO"));
				tipoCategoriaDocumentoVO.setTipoIdentificativo(rs.getString("TIPO_IDENTIFICATIVO"));
				elencoTipoCategoriaDocumento.add(tipoCategoriaDocumentoVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListTipoCategoriaDocumentoByIdTipologiaDocumento in TipoCategoriaDocumentoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListTipoCategoriaDocumentoByIdTipologiaDocumento in TipoCategoriaDocumentoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListTipoCategoriaDocumentoByIdTipologiaDocumento in TipoCategoriaDocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListTipoCategoriaDocumentoByIdTipologiaDocumento in TipoCategoriaDocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipoCategoriaDocumentoByIdTipologiaDocumento method in TipoCategoriaDocumentoDAO\n");
		if(elencoTipoCategoriaDocumento.size() == 0) {
			return (TipoCategoriaDocumentoVO[])elencoTipoCategoriaDocumento.toArray(new TipoCategoriaDocumentoVO[0]);
		}
		else {
			return (TipoCategoriaDocumentoVO[])elencoTipoCategoriaDocumento.toArray(new TipoCategoriaDocumentoVO[elencoTipoCategoriaDocumento.size()]);
		}
	}
	
	/**
	 * Metodo per recuperare il record su DB_CATEGORIA_DOCUMENTO a partire dalla chiave
	 * primaria
	 * 
	 * @param idCategoriaDocumento
	 * @return it.csi.solmr.dto.anag.TipoCategoriaDocumentoVO
	 * @throws DataAccessException
	 */
	public TipoCategoriaDocumentoVO findTipoCategoriaDocumentoByPrimaryKey(Long idCategoriaDocumento) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating findTipoCategoriaDocumentoByPrimaryKey method in TipoCategoriaDocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		TipoCategoriaDocumentoVO tipoCategoriaDocumentoVO = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in findTipoCategoriaDocumentoByPrimaryKey method in TipoCategoriaDocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in findTipoCategoriaDocumentoByPrimaryKey method in TipoCategoriaDocumentoDAO and it values: "+conn+"\n");

			String query = " SELECT ID_CATEGORIA_DOCUMENTO, " +
						   "        ID_TIPOLOGIA_DOCUMENTO, " +
						   "        DESCRIZIONE, " +
						   "        IDENTIFICATIVO, " +
						   "        TIPO_IDENTIFICATIVO " +
						   " FROM   DB_TIPO_CATEGORIA_DOCUMENTO " +
						   " WHERE  ID_CATEGORIA_DOCUMENTO = ? ";
			
			SolmrLogger.debug(this, "Value of parameter 1 [ID_CATEGORIA_DOCUMENTO] in findTipoCategoriaDocumentoByPrimaryKey method in TipoCategoriaDocumentoDAO: "+idCategoriaDocumento+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idCategoriaDocumento.longValue());
			
			SolmrLogger.debug(this, "Executing findTipoCategoriaDocumentoByPrimaryKey: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) {
				tipoCategoriaDocumentoVO = new TipoCategoriaDocumentoVO();
				tipoCategoriaDocumentoVO.setIdCategoriaDocumento(new Long(rs.getLong("ID_CATEGORIA_DOCUMENTO")));
				tipoCategoriaDocumentoVO.setIdTipologiaDocumento(new Long(rs.getLong("ID_TIPOLOGIA_DOCUMENTO")));
				tipoCategoriaDocumentoVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoCategoriaDocumentoVO.setIdentificativo(rs.getString("IDENTIFICATIVO"));
				tipoCategoriaDocumentoVO.setTipoIdentificativo(rs.getString("TIPO_IDENTIFICATIVO"));
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "findTipoCategoriaDocumentoByPrimaryKey in TipoCategoriaDocumentoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "findTipoCategoriaDocumentoByPrimaryKey in TipoCategoriaDocumentoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "findTipoCategoriaDocumentoByPrimaryKey in TipoCategoriaDocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "findTipoCategoriaDocumentoByPrimaryKey in TipoCategoriaDocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated findTipoCategoriaDocumentoByPrimaryKey method in TipoCategoriaDocumentoDAO\n");
		return tipoCategoriaDocumentoVO;
	}
}
