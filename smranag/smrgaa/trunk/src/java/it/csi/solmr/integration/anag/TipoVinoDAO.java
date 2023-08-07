package it.csi.solmr.integration.anag;
	
import it.csi.solmr.dto.anag.terreni.*;
import it.csi.solmr.exception.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.util.*;
import java.sql.*;
import java.util.*;

public class TipoVinoDAO extends it.csi.solmr.integration.BaseDAO {


	public TipoVinoDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public TipoVinoDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo che mi restituisce l'elenco dei tipi vino
	 * 
	 * @param onlyActive
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.TipoVinoVO[]
	 * @throws DataAccessException
	 */
	public TipoVinoVO[] getListTipoVino(boolean onlyActive, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListTipoVino method in TipoVinoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoVinoVO> elencoTipoVino = new Vector<TipoVinoVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListTipoVino method in TipoVinoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListTipoVino method in TipoVinoDAO and it values: "+conn+"\n");

			String query = " SELECT ID_VINO, " +
					       "        DESCRIZIONE, " +
					       "        ALTRI_VITIGNI, " +
					       "        RICADUTA, " +
					       "        RESA, " +
					       "        VARIETA_PARTICOLARE, " +
					       "        RESA_DI_RICADUTA, " +
					       "        FLAG_CONSIDERA_VITIGNO, " +
					       "        DATA_INIZIO_VALIDITA, " +
					       "        DATA_FINE_VALIDITA, " +
					       "        CODICE " +
					       " FROM   DB_TIPO_VINO ";
			if(onlyActive) {
				query +=   " WHERE  DATA_FINE_VALIDITA IS NULL ";
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

			SolmrLogger.debug(this, "Value of parameter 1 [ONLY_ACTIVE] in getListTipoVino method in TipoVinoDAO: "+onlyActive+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ORDER_BY] in getListTipoVino method in TipoVinoDAO: "+orderBy+"\n");

			stmt = conn.prepareStatement(query);
			

			SolmrLogger.debug(this, "Executing getListTipoVino: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				TipoVinoVO tipoVinoVO = new TipoVinoVO();
				tipoVinoVO.setIdVino(new Long(rs.getLong("ID_VINO")));
				tipoVinoVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoVinoVO.setAltriVitigni(rs.getString("ALTRI_VITIGNI"));
				tipoVinoVO.setRicaduta(rs.getString("RICADUTA"));
				tipoVinoVO.setResa(rs.getString("RESA"));
				tipoVinoVO.setVarietaParticolare(rs.getString("VARIETA_PARTICOLARE"));
				tipoVinoVO.setResaDiRicaduta(rs.getString("RESA_DI_RICADUTA"));
				tipoVinoVO.setFlagConsideraVitigno(rs.getString("FLAG_CONSIDERA_VITIGNO"));
				tipoVinoVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				tipoVinoVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				tipoVinoVO.setCodice(rs.getString("CODICE"));
				elencoTipoVino.add(tipoVinoVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListTipoVino in TipoVinoDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListTipoVino in TipoVinoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListTipoVino in TipoVinoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListTipoVino in TipoVinoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipoVino method in TipoVinoDAO\n");
		if(elencoTipoVino.size() == 0) {
			return (TipoVinoVO[])elencoTipoVino.toArray(new TipoVinoVO[0]);
		}
		else {
			return (TipoVinoVO[])elencoTipoVino.toArray(new TipoVinoVO[elencoTipoVino.size()]);
		}
	}
}
