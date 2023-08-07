package it.csi.solmr.integration.anag;
	
import it.csi.solmr.dto.*;
import it.csi.solmr.dto.anag.*;
import it.csi.solmr.exception.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.util.*;
import java.sql.*;
import java.util.*;

public class EsitoControlloDocumentoDAO extends it.csi.solmr.integration.BaseDAO {


	public EsitoControlloDocumentoDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public EsitoControlloDocumentoDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo che mi restituisce l'elenco delle anomalie relative al documento selezionato
	 * 
	 * @param idDocumento
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.EsitoControlloDocumentoVO[]
	 * @throws DataAccessException
	 */
	public EsitoControlloDocumentoVO[] getListEsitoControlloDocumentoByIdDocumento(Long idDocumento, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListEsitoControlloDocumentoByIdDocumento method in EsitoControlloDocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<EsitoControlloDocumentoVO> elencoEsitoControllo = new Vector<EsitoControlloDocumentoVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListEsitoControlloDocumentoByIdDocumento method in EsitoControlloDocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListEsitoControlloDocumentoByIdDocumento method in EsitoControlloDocumentoDAO and it values: "+conn+"\n");

			String query = " SELECT ECD.ID_ESITO_CONTROLLO_DOCUMENTO, " +
						   "        ECD.ID_DOCUMENTO, " +
						   "        ECD.ID_CONTROLLO, " +
						   "        TC.DESCRIZIONE AS DESC_CONTROLLO, " +
						   "        ECD.BLOCCANTE, " +
						   "        ECD.DESCRIZIONE " +
						   " FROM   DB_ESITO_CONTROLLO_DOCUMENTO ECD, " +
						   "        DB_TIPO_CONTROLLO TC " +
						   " WHERE  ID_DOCUMENTO = ? " +
						   " AND    ECD.ID_CONTROLLO = TC.ID_CONTROLLO ";
			
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

			SolmrLogger.debug(this, "Value of parameter 1 [ID_DOCUMENTO] in getListEsitoControlloDocumentoByIdDocumento method in EsitoControlloDocumentoDAO: "+idDocumento+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ORDER_BY] in getListEsitoControlloDocumentoByIdDocumento method in EsitoControlloDocumentoDAO: "+orderBy+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idDocumento.longValue());

			SolmrLogger.debug(this, "Executing getListEsitoControlloDocumentoByIdDocumento: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				EsitoControlloDocumentoVO esitoControlloDocumentoVO = new EsitoControlloDocumentoVO(); 
				esitoControlloDocumentoVO.setIdEsitoControlloDocumento(new Long(rs.getLong("ID_ESITO_CONTROLLO_DOCUMENTO")));
				esitoControlloDocumentoVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
				esitoControlloDocumentoVO.setIdControllo(new Long(rs.getLong("ID_CONTROLLO")));
				CodeDescription controllo = new CodeDescription(Integer.decode(rs.getString("ID_CONTROLLO")), rs.getString("DESC_CONTROLLO"));
				esitoControlloDocumentoVO.setControllo(controllo);
				esitoControlloDocumentoVO.setBloccante(rs.getString("BLOCCANTE"));
				esitoControlloDocumentoVO.setDescrizione(rs.getString("DESCRIZIONE"));
				elencoEsitoControllo.add(esitoControlloDocumentoVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListEsitoControlloDocumentoByIdDocumento in EsitoControlloDocumentoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListEsitoControlloDocumentoByIdDocumento in EsitoControlloDocumentoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListEsitoControlloDocumentoByIdDocumento in EsitoControlloDocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListEsitoControlloDocumentoByIdDocumento in EsitoControlloDocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListEsitoControlloDocumentoByIdDocumento method in EsitoControlloDocumentoDAO\n");
		if(elencoEsitoControllo.size() == 0) {
			return (EsitoControlloDocumentoVO[])elencoEsitoControllo.toArray(new EsitoControlloDocumentoVO[0]);
		}
		else {
			return (EsitoControlloDocumentoVO[])elencoEsitoControllo.toArray(new EsitoControlloDocumentoVO[elencoEsitoControllo.size()]);
		}
	}
}
