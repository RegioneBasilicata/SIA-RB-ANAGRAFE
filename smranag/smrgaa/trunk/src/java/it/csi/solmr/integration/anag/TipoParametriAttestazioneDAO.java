package it.csi.solmr.integration.anag;

import it.csi.solmr.dto.anag.attestazioni.*;
import it.csi.solmr.exception.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.util.*;
import java.sql.*;

public class TipoParametriAttestazioneDAO extends it.csi.solmr.integration.BaseDAO {


	public TipoParametriAttestazioneDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public TipoParametriAttestazioneDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo utilizzato per estrarre i tipi parametri previsti per una determinata attestazione
	 *
	 * @param idAttestazione
	 * @return it.csi.solmr.dto.anag.attestazioni.TipoParametriAttestazioneVO
	 * @throws DataAccessException
	 */
	public TipoParametriAttestazioneVO findTipoParametriAttestazioneByIdAttestazione(Long idAttestazione) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating findTipoParametriAttestazioneByIdAttestazione method in TipoParametriAttestazioneDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		TipoParametriAttestazioneVO tipoParametriAttestazioneVO = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in findTipoParametriAttestazioneByIdAttestazione method in TipoParametriAttestazioneDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in findTipoParametriAttestazioneByIdAttestazione method in TipoParametriAttestazioneDAO and it values: "+conn+"\n");

			String query = " SELECT ID_PARAMETRI_ATTESTAZIONE, " +
						   "        ID_ATTESTAZIONE, " + 
						   "        PARAMETRO_1, " + 
						   "        PARAMETRO_2, " + 
						   "        PARAMETRO_3, " + 
						   "        PARAMETRO_4, " + 
						   "        PARAMETRO_5, " + 
						   "        OBBLIGATORIO_1, " + 
						   "        OBBLIGATORIO_2, " + 
						   "        OBBLIGATORIO_3, " + 
						   "        OBBLIGATORIO_4, " + 
						   "        OBBLIGATORIO_5 " +
						   " FROM   DB_TIPO_PARAMETRI_ATTESTAZIONE " +
						   " WHERE	ID_ATTESTAZIONE = ? ";

			SolmrLogger.debug(this, "Value of parameter 1 [ID_ATTESTAZIONE] in findTipoParametriAttestazioneByIdAttestazione method in TipoParametriAttestazioneDAO: "+idAttestazione+"\n");


			stmt = conn.prepareStatement(query);

			stmt.setLong(1, idAttestazione.longValue());

			SolmrLogger.debug(this, "Executing findTipoParametriAttestazioneByIdAttestazione: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) {
				tipoParametriAttestazioneVO = new TipoParametriAttestazioneVO();
				tipoParametriAttestazioneVO.setIdParametriAttestazione(new Long(rs.getLong("ID_PARAMETRI_ATTESTAZIONE")));
				tipoParametriAttestazioneVO.setIdAttestazione(new Long(rs.getLong("ID_ATTESTAZIONE")));
				tipoParametriAttestazioneVO.setParametro1(rs.getString("PARAMETRO_1"));
				tipoParametriAttestazioneVO.setParametro2(rs.getString("PARAMETRO_2"));
				tipoParametriAttestazioneVO.setParametro3(rs.getString("PARAMETRO_3"));
				tipoParametriAttestazioneVO.setParametro4(rs.getString("PARAMETRO_4"));
				tipoParametriAttestazioneVO.setParametro5(rs.getString("PARAMETRO_5"));
				tipoParametriAttestazioneVO.setObbligatorio1(rs.getString("OBBLIGATORIO_1"));
				tipoParametriAttestazioneVO.setObbligatorio2(rs.getString("OBBLIGATORIO_2"));
				tipoParametriAttestazioneVO.setObbligatorio3(rs.getString("OBBLIGATORIO_3"));
				tipoParametriAttestazioneVO.setObbligatorio4(rs.getString("OBBLIGATORIO_4"));
				tipoParametriAttestazioneVO.setObbligatorio5(rs.getString("OBBLIGATORIO_5"));
			}

			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "findTipoParametriAttestazioneByIdAttestazione in TipoParametriAttestazioneDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "findTipoParametriAttestazioneByIdAttestazione in TipoParametriAttestazioneDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "findTipoParametriAttestazioneByIdAttestazione in TipoParametriAttestazioneDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "findTipoParametriAttestazioneByIdAttestazione in TipoParametriAttestazioneDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated findTipoParametriAttestazioneByIdAttestazione method in TipoParametriAttestazioneDAO\n");
		return tipoParametriAttestazioneVO;
	}
}
