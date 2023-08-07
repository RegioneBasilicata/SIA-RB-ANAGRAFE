package it.csi.solmr.integration.anag;
	
import it.csi.solmr.dto.*;
import it.csi.solmr.dto.anag.terreni.*;
import it.csi.solmr.exception.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.util.*;
import java.sql.*;
import java.util.*;

public class EsitoControlloUnarDAO extends it.csi.solmr.integration.BaseDAO {


	public EsitoControlloUnarDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public EsitoControlloUnarDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo che mi restituisce l'elenco delle anomalie relative all'unità arborea selezionata
	 * 
	 * @param idStoricoUnitaArborea
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.EsitoControlloUnarVO[]
	 * @throws DataAccessException
	 */
	public EsitoControlloUnarVO[] getListEsitoControlloUnarByIdStoricoUnitaArborea(Long idStoricoUnitaArborea, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListEsitoControlloUnarByIdStoricoUnitaArborea method in EsitoControlloUnarDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<EsitoControlloUnarVO> elencoEsitoControllo = new Vector<EsitoControlloUnarVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListEsitoControlloUnarByIdStoricoUnitaArborea method in EsitoControlloUnarDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListEsitoControlloUnarByIdStoricoUnitaArborea method in EsitoControlloUnarDAO and it values: "+conn+"\n");

			String query = " SELECT ECU.ID_ESITO_CONTROLLO_UNAR, " +
				           "        ECU.ID_STORICO_UNITA_ARBOREA, " +
				           "        ECU.ID_CONTROLLO, " +
				           "        TC.DESCRIZIONE AS DESC_CONTROLLO, " +
				           "        ECU.BLOCCANTE, " +
				           "        ECU.DESCRIZIONE " +
				           " FROM   DB_ESITO_CONTROLLO_UNAR ECU, " +
				           "        DB_TIPO_CONTROLLO TC " +
				           " WHERE  ID_STORICO_UNITA_ARBOREA = ? " +
				           " AND    ECU.ID_CONTROLLO = TC.ID_CONTROLLO ";
			
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

			SolmrLogger.debug(this, "Value of parameter 1 [ID_STORICO_UNITA_ARBOREA] in getListEsitoControlloUnarByIdStoricoUnitaArborea method in EsitoControlloUnarDAO: "+idStoricoUnitaArborea+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ORDER_BY] in getListEsitoControlloUnarByIdStoricoUnitaArborea method in EsitoControlloUnarDAO: "+orderBy+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idStoricoUnitaArborea.longValue());

			SolmrLogger.debug(this, "Executing getListEsitoControlloUnarByIdStoricoUnitaArborea: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				EsitoControlloUnarVO esitoControlloUnarVO = new EsitoControlloUnarVO(); 
				esitoControlloUnarVO.setIdEsitoControlloUnar(new Long(rs.getLong("ID_ESITO_CONTROLLO_UNAR")));
				esitoControlloUnarVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
				esitoControlloUnarVO.setIdControllo(new Long(rs.getLong("ID_CONTROLLO")));
				CodeDescription controllo = new CodeDescription(Integer.decode(rs.getString("ID_CONTROLLO")), rs.getString("DESC_CONTROLLO"));
				esitoControlloUnarVO.setControllo(controllo);
				esitoControlloUnarVO.setBloccante(rs.getString("BLOCCANTE"));
				esitoControlloUnarVO.setDescrizione(rs.getString("DESCRIZIONE"));
				elencoEsitoControllo.add(esitoControlloUnarVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListEsitoControlloUnarByIdStoricoUnitaArborea in EsitoControlloUnarDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListEsitoControlloUnarByIdStoricoUnitaArborea in EsitoControlloUnarDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListEsitoControlloUnarByIdStoricoUnitaArborea in EsitoControlloUnarDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListEsitoControlloUnarByIdStoricoUnitaArborea in EsitoControlloUnarDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListEsitoControlloUnarByIdStoricoUnitaArborea method in EsitoControlloUnarDAO\n");
		if(elencoEsitoControllo.size() == 0) {
			return (EsitoControlloUnarVO[])elencoEsitoControllo.toArray(new EsitoControlloUnarVO[0]);
		}
		else {
			return (EsitoControlloUnarVO[])elencoEsitoControllo.toArray(new EsitoControlloUnarVO[elencoEsitoControllo.size()]);
		}
	}
}
