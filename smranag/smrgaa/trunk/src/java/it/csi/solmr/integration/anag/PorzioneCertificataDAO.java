package it.csi.solmr.integration.anag;
	
import it.csi.solmr.dto.anag.terreni.*;
import it.csi.solmr.exception.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.util.*;

import java.sql.*;
import java.util.*;

public class PorzioneCertificataDAO extends it.csi.solmr.integration.BaseDAO {


	public PorzioneCertificataDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public PorzioneCertificataDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo che mi restituisce l'elenco delle porzioni certificate a partire 
	 * dall'id_particella_certificata
	 * 
	 * @param idParticellaCertificata
	 * @return it.csi.solmr.dto.anag.terreni.PorzioneCertificataVO
	 * @throws DataAccessException
	 */
	public PorzioneCertificataVO[] getListPorzioneCertificataByIdParticellaCertificata(Long idParticellaCertificata,
	    java.util.Date dataDichiarazioneConsistenza) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListPorzioneCertificataByIdParticellaCertificata method in PorzioneCertificataDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<PorzioneCertificataVO> elencoPorzioni = new Vector<PorzioneCertificataVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListPorzioneCertificataByIdParticellaCertificata method in PorzioneCertificataDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListPorzioneCertificataByIdParticellaCertificata method in PorzioneCertificataDAO and it values: "+conn+"\n");

			String query = " SELECT ID_PORZIONE_CERTIFICATA, " +
					       "        ID_PARTICELLA_CERTIFICATA, " +
					       "        COMUNE, " +
					       "        STADIO, " +
					       "        SEZIONE, " +
					       "        ID_QUALITA, " +
					       "        SUP_CATASTALE, " +
					       "        IDENTIFICATIVO_PORZIONE " +
					       " FROM   DB_PORZIONE_CERTIFICATA " +
					       " WHERE  ID_PARTICELLA_CERTIFICATA = ? ";
			
			if(dataDichiarazioneConsistenza !=null)
			{
			  query +=
			    " AND  DATA_INIZIO_VALIDITA < ? " +
          " AND  NVL(DATA_FINE_VALIDITA, ?) > ? ";
			}
			else
			{
			  query +=
			    " AND DATA_FINE_VALIDITA IS NULL ";
			}

			SolmrLogger.debug(this, "Value of parameter 1 [ID_PARTICELLA_CERTIFICATA] in getListPorzioneCertificataByIdParticellaCertificata method in PorzioneCertificataDAO: "+idParticellaCertificata+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [DATA_DICHIARAZIONE_CONSISTENZA] in getListPorzioneCertificataByIdParticellaCertificata method in PorzioneCertificataDAO: "+dataDichiarazioneConsistenza+"\n");
			stmt = conn.prepareStatement(query);
			int idx=0;
			
			stmt.setLong(++idx, idParticellaCertificata.longValue());
			
			if(dataDichiarazioneConsistenza !=null)
      {
			  stmt.setTimestamp(++idx, convertDateToTimestamp(dataDichiarazioneConsistenza));
			  stmt.setDate(++idx, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
			  stmt.setTimestamp(++idx, convertDateToTimestamp(dataDichiarazioneConsistenza));
      }

			SolmrLogger.debug(this, "Executing getListPorzioneCertificataByIdParticellaCertificata: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				PorzioneCertificataVO porzioneCertificataVO = new PorzioneCertificataVO();
				porzioneCertificataVO.setIdPorzioneCertificata(new Long(rs.getLong("ID_PORZIONE_CERTIFICATA")));
				porzioneCertificataVO.setIdParticellaCertificata(new Long(rs.getLong("ID_PARTICELLA_CERTIFICATA")));
				porzioneCertificataVO.setComune(rs.getString("COMUNE"));
				porzioneCertificataVO.setStadio(rs.getString("STADIO"));
				porzioneCertificataVO.setSezione(rs.getString("SEZIONE"));
				if(Validator.isNotEmpty(rs.getString("ID_QUALITA"))) {
					porzioneCertificataVO.setIdQualita(new Long(rs.getLong("ID_QUALITA")));
				}
				porzioneCertificataVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
				porzioneCertificataVO.setIdentificativoPorzione(rs.getString("IDENTIFICATIVO_PORZIONE"));
				elencoPorzioni.add(porzioneCertificataVO);
			}

			stmt.close();
			rs.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListPorzioneCertificataByIdParticellaCertificata in PorzioneCertificataDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListPorzioneCertificataByIdParticellaCertificata in PorzioneCertificataDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListPorzioneCertificataByIdParticellaCertificata in PorzioneCertificataDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListPorzioneCertificataByIdParticellaCertificata in PorzioneCertificataDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListPorzioneCertificataByIdParticellaCertificata method in PorzioneCertificataDAO\n");
		return elencoPorzioni.size() == 0 ? null :(PorzioneCertificataVO[])elencoPorzioni.toArray(new PorzioneCertificataVO[0]);
	}
}
