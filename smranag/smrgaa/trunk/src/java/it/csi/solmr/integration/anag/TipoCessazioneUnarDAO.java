package it.csi.solmr.integration.anag;
	
import it.csi.solmr.dto.anag.terreni.*;
import it.csi.solmr.exception.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.util.*;
import java.sql.*;
import java.util.*;

public class TipoCessazioneUnarDAO extends it.csi.solmr.integration.BaseDAO {


	public TipoCessazioneUnarDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public TipoCessazioneUnarDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo che mi restituisce l'elenco delle cessazioni unar
	 * 
	 * @param onlyActive
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.TipoCessazioneUnarVO[]
	 * @throws DataAccessException
	 */
	public TipoCessazioneUnarVO[] getListTipoCessazioneUnar(boolean onlyActive, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListTipoCessazioneUnar method in TipoCessazioneUnarDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoCessazioneUnarVO> elencoCessazioniUnar = new Vector<TipoCessazioneUnarVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListTipoCessazioneUnar method in TipoCessazioneUnarDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListTipoCessazioneUnar method in TipoCessazioneUnarDAO and it values: "+conn+"\n");

			String query = " SELECT ID_CESSAZIONE_UNAR, " +
						   "        DESCRIZIONE, " +
						   "        DATA_INIZIO_VALIDITA, " +
						   "        DATA_FINE_VALIDITA " +
						   " FROM   DB_TIPO_CESSAZIONE_UNAR ";
			
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

			SolmrLogger.debug(this, "Value of parameter 1 [ONLY_ACTIVE] in getListTipoCessazioneUnar method in TipoCessazioneUnarDAO: "+onlyActive+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ORDER_BY] in getListTipoCessazioneUnar method in TipoCessazioneUnarDAO: "+orderBy+"\n");

			stmt = conn.prepareStatement(query);
			
			SolmrLogger.debug(this, "Executing getListTipoCessazioneUnar: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				TipoCessazioneUnarVO tipoCessazioneUnarVO = new TipoCessazioneUnarVO();
				tipoCessazioneUnarVO.setIdCessazioneUnar(new Long(rs.getLong("ID_CESSAZIONE_UNAR")));
				tipoCessazioneUnarVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoCessazioneUnarVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				tipoCessazioneUnarVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				elencoCessazioniUnar.add(tipoCessazioneUnarVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListTipoCessazioneUnar in TipoCessazioneUnarDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListTipoCessazioneUnar in TipoCessazioneUnarDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListTipoCessazioneUnar in TipoCessazioneUnarDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListTipoCessazioneUnar in TipoCessazioneUnarDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipoCessazioneUnar method in TipoCessazioneUnarDAO\n");
		if(elencoCessazioniUnar.size() == 0) {
			return (TipoCessazioneUnarVO[])elencoCessazioniUnar.toArray(new TipoCessazioneUnarVO[0]);
		}
		else {
			return (TipoCessazioneUnarVO[])elencoCessazioniUnar.toArray(new TipoCessazioneUnarVO[elencoCessazioniUnar.size()]);
		}
	}
}
