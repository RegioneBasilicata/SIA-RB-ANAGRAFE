package it.csi.solmr.integration.anag;
	
import it.csi.solmr.dto.anag.terreni.*;
import it.csi.solmr.exception.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.util.*;

import java.sql.*;
import java.util.*;

public class AltroVitignoDAO extends it.csi.solmr.integration.BaseDAO {


	public AltroVitignoDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public AltroVitignoDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo che mi restituisce l'elenco degli altri vitigni associati
	 * all'unità arborea
	 * 
	 * @param idStoricoUnitaArborea
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.AltroVitignoVO[]
	 * @throws DataAccessException
	 */
	public AltroVitignoVO[] getListAltroVitignoByIdStoricoUnitaArborea(Long idStoricoUnitaArborea, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListAltroVitignoByIdStoricoUnitaArborea method in AltroVitignoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<AltroVitignoVO> elencoAltriVitigni = new Vector<AltroVitignoVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListAltroVitignoByIdStoricoUnitaArborea method in AltroVitignoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListAltroVitignoByIdStoricoUnitaArborea method in AltroVitignoDAO and it values: "+conn+"\n");

			String query = " SELECT AV.ID_ALTRO_VITIGNO, " +
						   "        AV.ID_STORICO_UNITA_ARBOREA, " +
						   "        AV.PERCENTUALE_VITIGNO, " +
						   "        AV.ID_VARIETA, " +
						   "        TV.CODICE_VARIETA, " +
						   "        TV.DESCRIZIONE, " +
						   "        TV.ID_UTILIZZO " +
						   " FROM   DB_ALTRO_VITIGNO AV, " +
						   "        DB_TIPO_VARIETA TV " +
						   " WHERE  AV.ID_STORICO_UNITA_ARBOREA = ? " +
						   " AND    AV.ID_VARIETA = TV.ID_VARIETA ";
			
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

			SolmrLogger.debug(this, "Value of parameter 1 [ID_STORICO_UNITA_ARBOREA] in getListAltroVitignoByIdStoricoUnitaArborea method in AltroVitignoDAO: "+idStoricoUnitaArborea+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ORDER_BY] in getListAltroVitignoByIdStoricoUnitaArborea method in AltroVitignoDAO: "+orderBy+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idStoricoUnitaArborea.longValue());

			SolmrLogger.debug(this, "Executing getListAltroVitignoByIdStoricoUnitaArborea: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				AltroVitignoVO altroVitignoVO = new AltroVitignoVO(); 
				altroVitignoVO.setIdAltroVitigno(new Long(rs.getLong("ID_ALTRO_VITIGNO")));
				if(Validator.isNotEmpty(rs.getString("ID_STORICO_UNITA_ARBOREA"))) {
					altroVitignoVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
				}
				altroVitignoVO.setPercentualeVitigno(rs.getString("PERCENTUALE_VITIGNO"));
				altroVitignoVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
				TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
				tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
				tipoVarietaVO.setCodiceVarieta(rs.getString("CODICE_VARIETA"));
				tipoVarietaVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoVarietaVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
				altroVitignoVO.setTipoVarietaVO(tipoVarietaVO);
				elencoAltriVitigni.add(altroVitignoVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListAltroVitignoByIdStoricoUnitaArborea in AltroVitignoDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListAltroVitignoByIdStoricoUnitaArborea in AltroVitignoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListAltroVitignoByIdStoricoUnitaArborea in AltroVitignoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListAltroVitignoByIdStoricoUnitaArborea in AltroVitignoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListAltroVitignoByIdStoricoUnitaArborea method in AltroVitignoDAO\n");
		if(elencoAltriVitigni.size() == 0) {
			return (AltroVitignoVO[])elencoAltriVitigni.toArray(new AltroVitignoVO[0]);
		}
		else {
			return (AltroVitignoVO[])elencoAltriVitigni.toArray(new AltroVitignoVO[elencoAltriVitigni.size()]);
		}
	}
	
	/**
	 * Metodo utilizzato per inserire un record su DB_ALTRO_VITIGNO
	 * 
	 * @param altroVitignoVO
	 * @return java.lang.Long
	 * @throws DataAccessException
	 */
	public Long insertAltroVitigno(AltroVitignoVO altroVitignoVO) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating insertAltroVitigno method in AltroVitignoDAO\n");
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    Long idAltroVitigno = null;
	
	    try {
	    	idAltroVitigno = getNextPrimaryKey(SolmrConstants.SEQ_ALTRO_VITIGNO);
	    	SolmrLogger.debug(this, "Creating db-connection in insertAltroVitigno method in AltroVitignoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in insertAltroVitigno method in AltroVitignoDAO and it values: "+conn+"\n");
			
			String query = " INSERT INTO DB_ALTRO_VITIGNO " +
			   			   "        (ID_ALTRO_VITIGNO, " +
			   			   "         ID_STORICO_UNITA_ARBOREA, " +
			   			   "         PERCENTUALE_VITIGNO, " +
			   			   "         ID_VARIETA) " +
			   			   " VALUES  (?, ?, ?, ?) ";
			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing insertAltroVitigno: "+query);
			
			stmt.setLong(1, idAltroVitigno.longValue());
			SolmrLogger.debug(this, "Value of parameter 1 [ID_ALTRO_VITIGNO] in method insertAltroVitigno in AltroVitignoDAO: "+idAltroVitigno.longValue()+"\n");
			stmt.setLong(2, altroVitignoVO.getIdStoricoUnitaArborea().longValue());
			SolmrLogger.debug(this, "Value of parameter 2 [ID_STORICO_UNITA_ARBOREA] in method insertAltroVitigno in AltroVitignoDAO: "+altroVitignoVO.getIdStoricoUnitaArborea().longValue()+"\n");			
			stmt.setString(3, altroVitignoVO.getPercentualeVitigno());
			SolmrLogger.debug(this, "Value of parameter 3 [PERCENTUALE_VITIGNO] in method insertAltroVitigno in AltroVitignoDAO: "+altroVitignoVO.getPercentualeVitigno()+"\n");
			stmt.setLong(4, altroVitignoVO.getIdVarieta().longValue());
			SolmrLogger.debug(this, "Value of parameter 4 [ID_VARIETA] in method insertAltroVitigno in AltroVitignoDAO: "+altroVitignoVO.getIdVarieta().longValue()+"\n");
			
			stmt.executeUpdate();

			stmt.close();
	    }
	    catch(SQLException exc) {
	    	SolmrLogger.error(this, "insertAltroVitigno in AltroVitignoDAO - SQLException: "+exc);
	    	throw new DataAccessException(exc.getMessage());
	    }
	    catch (Exception ex) {
	    	SolmrLogger.error(this, "insertAltroVitigno in AltroVitignoDAO - Generic Exception: "+ex);
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
	    		SolmrLogger.error(this, "insertAltroVitigno in AltroVitignoDAO - SQLException while closing Statement and Connection: "+exc);
	    		throw new DataAccessException(exc.getMessage());
	    	}
	    	catch(Exception ex) {
	    		SolmrLogger.error(this, "insertAltroVitigno in AltroVitignoDAO - Generic Exception while closing Statement and Connection: "+ex);
	    		throw new DataAccessException(ex.getMessage());
	    	}
	    }
	    SolmrLogger.debug(this, "Invocated insertAltroVitigno method in AltroVitignoDAO\n");
	    return idAltroVitigno;
	}
	
	/**
	 * Metodo utilizzato per eliminare tutti gli altri vitigni collegati
	 * ad un'unità arborea
	 * 
	 * @param idStoricoUnitaArborea
	 * @throws DataAccessException
	 */
	public void deleteAltroVitignoByIdStoricoUnitaArborea(Long idStoricoUnitaArborea) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating deleteAltroVitignoByIdStoricoUnitaArborea method in AltroVitignoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in deleteAltroVitignoByIdStoricoUnitaArborea method in AltroVitignoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in deleteAltroVitignoByIdStoricoUnitaArborea method in AltroVitignoDAO and it values: "+conn+"\n");

			String query  = " DELETE FROM  DB_ALTRO_VITIGNO "+
                    		"        WHERE ID_STORICO_UNITA_ARBOREA = ? ";

			stmt = conn.prepareStatement(query);
			
			SolmrLogger.debug(this, "Executing deleteAltroVitignoByIdStoricoUnitaArborea: "+query);
			
			stmt.setLong(1, idStoricoUnitaArborea.longValue());
			SolmrLogger.debug(this, "Value of parameter 1 [ID_STORICO_UNITA_ARBOREA] in method deleteAltroVitignoByIdStoricoUnitaArborea in AltroVitignoDAO: "+idStoricoUnitaArborea.longValue()+"\n");

			stmt.executeUpdate();
    
			stmt.close();
		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "SQLException in deleteAltroVitignoByIdStoricoUnitaArborea: "+exc);
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "Generic Exception in deleteAltroVitignoByIdStoricoUnitaArborea: "+ex);
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
				SolmrLogger.error(this, "SQLException while closing Statement and Connection in deleteAltroVitignoByIdStoricoUnitaArborea: "+exc);
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in deleteAltroVitignoByIdStoricoUnitaArborea: "+ex);
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated deleteAltroVitignoByIdStoricoUnitaArborea method in AltroVitignoDAO\n");
	}
}
