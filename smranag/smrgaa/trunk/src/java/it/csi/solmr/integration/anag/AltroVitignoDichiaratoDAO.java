package it.csi.solmr.integration.anag;
	
import it.csi.solmr.dto.anag.terreni.*;
import it.csi.solmr.exception.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.util.*;
import java.sql.*;
import java.util.*;

public class AltroVitignoDichiaratoDAO extends it.csi.solmr.integration.BaseDAO {


	public AltroVitignoDichiaratoDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public AltroVitignoDichiaratoDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo che mi restituisce l'elenco degli altri vitigni dichiarati associati
	 * all'unità arborea
	 * 
	 * @param idUnitaArboreaDichiarata
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.AltroVitignoDichiaratoVO[]
	 * @throws DataAccessException
	 */
	public AltroVitignoDichiaratoVO[] getListAltroVitignoDichiaratoByIdUnitaArboreaDichiarata(Long idUnitaArboreaDichiarata, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListAltroVitignoDichiaratoByIdUnitaArboreaDichiarata method in AltroVitignoDichiatoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<AltroVitignoDichiaratoVO> elencoAltriVitigniDichiarati = new Vector<AltroVitignoDichiaratoVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListAltroVitignoDichiaratoByIdUnitaArboreaDichiarata method in AltroVitignoDichiatoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListAltroVitignoDichiaratoByIdUnitaArboreaDichiarata method in AltroVitignoDichiatoDAO and it values: "+conn+"\n");

			String query = " SELECT AVD.ID_ALTRO_VITIGNO_DICHIARATO, " +
						   "        AVD.ID_UNITA_ARBOREA_DICHIARATA, " +
						   "        AVD.PERCENTUALE_VITIGNO, " +
						   "        AVD.ID_VARIETA, " +
						   "        TV.CODICE_VARIETA, " +
						   "        TV.DESCRIZIONE " +
						   " FROM   DB_ALTRO_VITIGNO_DICHIARATO AVD, " +
						   "        DB_TIPO_VARIETA TV " +
						   " WHERE  AVD.ID_UNITA_ARBOREA_DICHIARATA = ? " +
						   " AND    AVD.ID_VARIETA = TV.ID_VARIETA ";
			
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

			SolmrLogger.debug(this, "Value of parameter 1 [ID_UNITA_ARBOREA_DICHIARATA] in getListAltroVitignoDichiaratoByIdUnitaArboreaDichiarata method in AltroVitignoDichiaratoDAO: "+idUnitaArboreaDichiarata+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ORDER_BY] in getListAltroVitignoDichiaratoByIdUnitaArboreaDichiarata method in AltroVitignoDichiaratoDAO: "+orderBy+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idUnitaArboreaDichiarata.longValue());

			SolmrLogger.debug(this, "Executing getListAltroVitignoDichiaratoByIdUnitaArboreaDichiarata: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				AltroVitignoDichiaratoVO altroVitignoDichiaratoVO = new AltroVitignoDichiaratoVO(); 
				altroVitignoDichiaratoVO.setIdAltroVitignoDichiarato(new Long(rs.getLong("ID_ALTRO_VITIGNO_DICHIARATO")));
				altroVitignoDichiaratoVO.setIdUnitaArboreaDichiarata(new Long(rs.getLong("ID_UNITA_ARBOREA_DICHIARATA")));
				altroVitignoDichiaratoVO.setPercentualeVitigno(rs.getString("PERCENTUALE_VITIGNO"));
				TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
				tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
				tipoVarietaVO.setCodiceVarieta(rs.getString("CODICE_VARIETA"));
				tipoVarietaVO.setDescrizione(rs.getString("DESCRIZIONE"));
				altroVitignoDichiaratoVO.setTipoVarietaVO(tipoVarietaVO);
				elencoAltriVitigniDichiarati.add(altroVitignoDichiaratoVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListAltroVitignoDichiaratoByIdUnitaArboreaDichiarata in AltroVitignoDichiaratoDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListAltroVitignoDichiaratoByIdUnitaArboreaDichiarata in AltroVitignoDichiaratoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListAltroVitignoDichiaratoByIdUnitaArboreaDichiarata in AltroVitignoDichiaratoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListAltroVitignoDichiaratoByIdUnitaArboreaDichiarata in AltroVitignoDichiaratoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListAltroVitignoDichiaratoByIdUnitaArboreaDichiarata method in AltroVitignoDichiaratoDAO\n");
		if(elencoAltriVitigniDichiarati.size() == 0) {
			return (AltroVitignoDichiaratoVO[])elencoAltriVitigniDichiarati.toArray(new AltroVitignoDichiaratoVO[0]);
		}
		else {
			return (AltroVitignoDichiaratoVO[])elencoAltriVitigniDichiarati.toArray(new AltroVitignoDichiaratoVO[elencoAltriVitigniDichiarati.size()]);
		}
	}
}
