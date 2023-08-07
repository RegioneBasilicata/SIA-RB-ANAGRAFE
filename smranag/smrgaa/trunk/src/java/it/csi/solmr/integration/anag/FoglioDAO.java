package it.csi.solmr.integration.anag;
	
import it.csi.solmr.dto.anag.*;
import it.csi.solmr.exception.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.util.*;

import java.sql.*;
import java.util.Vector;

public class FoglioDAO extends it.csi.solmr.integration.BaseDAO {


	public FoglioDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public FoglioDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo che mi restituisce il foglio a partire dai parametri che rappresentano
	 * la sua chiave logica
	 * 
	 * @param istatComune
	 * @param foglio
	 * @param sezione
	 * @return it.csi.solmr.dto.anag.FoglioVO
	 * @throws DataAccessException
	 */
	public FoglioVO findFoglioByParameters(String istatComune, String foglio, String sezione) 
	  throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating findFoglioByParameters method in FoglioDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		FoglioVO foglioVO = null;

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in findFoglioByParameters method in FoglioDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in findFoglioByParameters method in FoglioDAO and it values: "+conn+"\n");

			String query = " SELECT F.ID_FOGLIO, " +
						   "        F.ID_CASO_PARTICOLARE, " +
						   "        F.SEZIONE, " +
						   "        F.FOGLIO, " +
						   "        F.COMUNE, " +
						   "        F.ID_ZONA_ALTIMETRICA, " +
						   "        F.ID_AREA_A, " +
						   "        F.ID_AREA_B, " +
						   "        F.ID_AREA_C, " +
						   "        F.ID_AREA_D, " +
						   "        F.ID_AREA_E AS F_ID_AREA_E, " +
						   "        F.ID_AREA_G AS F_ID_AREA_G, " +
						   "        TAG.DESCRIZIONE AS DESC_AREA_G, " +
						   "        F.FLAG_CAPTAZIONE_POZZI, " +
						   "        TAE.DESCRIZIONE AS DESC_AREA_E, " +
						   "        F.SUPERFICIE_FOGLIO, " +
						   "        F.FLAG_PROVVISORIO, " +
						   "        F.ID_AREA_F, " +
						   "        TAF.DESCRIZIONE AS DESC_AREA_F, " +
						   "        F.ID_AREA_PSN, "+
						   "        TAP.DESCRIZIONE AS DESC_AREA_PSN, "+
						   "        F.ID_AREA_I AS F_ID_AREA_I, " +
               "        TAI.DESCRIZIONE AS DESC_AREA_I, " +
               "        F.ID_AREA_L AS F_ID_AREA_L, " +
               "        TAL.DESCRIZIONE AS DESC_AREA_L, " +
               "        F.FLAG_STABILIZZAZIONE " +
						   " FROM   DB_FOGLIO F, " +
						   "        DB_TIPO_AREA_E TAE, " +
						   "        DB_TIPO_AREA_F TAF, " +
						   "        DB_TIPO_AREA_PSN TAP, " +
						   "        DB_TIPO_AREA_G TAG, " +
				       "        DB_TIPO_AREA_I TAI, " +
				       "        DB_TIPO_AREA_L TAL " +
						   " WHERE  F.COMUNE = ? " +
						   " AND    F.FOGLIO = ? " +
						   " AND    F.ID_AREA_E = TAE.ID_AREA_E(+) " +
						   " AND    F.ID_AREA_F = TAF.ID_AREA_F "+
						   " AND    F.ID_AREA_PSN = TAP.ID_AREA_PSN(+) " +
			         " AND    F.ID_AREA_G = TAG.ID_AREA_G(+) " +
			         " AND    F.ID_AREA_I = TAI.ID_AREA_I(+) " +
			         " AND    F.ID_AREA_L = TAL.ID_AREA_L(+) ";


			if(sezione != null && !sezione.equals("")) {
				query += " AND F.SEZIONE = ? ";
			}
			else
			{
			  query += " AND F.SEZIONE IS NULL ";
			}

			SolmrLogger.debug(this, "Value of parameter 1 [ISTAT_COMUNE] in findFoglioByParameters method in FoglioDAO: "+istatComune+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [FOGLIO] in findFoglioByParameters method in FoglioDAO: "+foglio+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setString(1, istatComune);
			stmt.setString(2, foglio);
			if(Validator.isNotEmpty(sezione)) {
				SolmrLogger.debug(this, "Value of parameter 3 [SEZIONE] in findFoglioByParameters method in FoglioDAO: "+sezione+"\n");
				stmt.setString(3, sezione);
			}

			SolmrLogger.debug(this, "Executing findFoglioByParameters: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) 
			{
				foglioVO = new FoglioVO();
        foglioVO.setIdFoglio(new Long(rs.getLong("ID_FOGLIO")));
        if(rs.getString("ID_CASO_PARTICOLARE") != null && !rs.getString("ID_CASO_PARTICOLARE").equals("")) {
        	foglioVO.setIdCasoParticolare(new Long(rs.getLong("ID_CASO_PARTICOLARE")));
        }
        foglioVO.setSezione(rs.getString("SEZIONE"));
        if(rs.getString("FOGLIO") != null && !rs.getString("FOGLIO").equals("")) {
        	foglioVO.setFoglio(new Long(rs.getLong("FOGLIO")));
        }
        foglioVO.setIstatComune(rs.getString("COMUNE"));
        if(rs.getString("ID_ZONA_ALTIMETRICA") != null && !rs.getString("ID_ZONA_ALTIMETRICA").equals("")) {
        	foglioVO.setIdZonaAltimetrica(new Long(rs.getLong("ID_ZONA_ALTIMETRICA")));
        }
        if(rs.getString("ID_AREA_A") != null && !rs.getString("ID_AREA_A").equals("")) {
        	foglioVO.setIdAreaA(new Long(rs.getLong("ID_AREA_A")));
        }
        if(rs.getString("ID_AREA_B") != null && !rs.getString("ID_AREA_B").equals("")) {
        	foglioVO.setIdAreaB(new Long(rs.getLong("ID_AREA_B")));
        }
        if(rs.getString("ID_AREA_C") != null && !rs.getString("ID_AREA_C").equals("")) {
        	foglioVO.setIdAreaC(new Long(rs.getLong("ID_AREA_C")));
        }
        if(rs.getString("ID_AREA_D") != null && !rs.getString("ID_AREA_D").equals("")) {
        	foglioVO.setIdAreaD(new Long(rs.getLong("ID_AREA_D")));
        }
        if(rs.getString("F_ID_AREA_E") != null && !rs.getString("F_ID_AREA_E").equals("")) {
            foglioVO.setIdAreaE(new Long(rs.getLong("F_ID_AREA_E")));
        }
        if(rs.getString("ID_AREA_PSN") != null && !rs.getString("ID_AREA_PSN").equals("")) 
        {
          foglioVO.setIdAreaPSN(new Long(rs.getLong("ID_AREA_PSN")));
          foglioVO.setDescrizioneAreaPSN(rs.getString("DESC_AREA_PSN"));
        }
        if(rs.getString("FLAG_CAPTAZIONE_POZZI") != null) {
            foglioVO.setFlagCaptazionePozzi(rs.getString("FLAG_CAPTAZIONE_POZZI"));
        }
        if(rs.getString("F_ID_AREA_G") != null && !rs.getString("F_ID_AREA_G").equals("")) {
          foglioVO.setIdAreaG(new Long(rs.getLong("F_ID_AREA_G")));
        }
        foglioVO.setDescrizioneAreaE(rs.getString("DESC_AREA_E"));
        foglioVO.setSuperficieFoglio(rs.getString("SUPERFICIE_FOGLIO"));
        foglioVO.setFlagProvvisorio(rs.getString("FLAG_PROVVISORIO"));
        foglioVO.setIdAreaF(new Long(rs.getLong("ID_AREA_F")));
        foglioVO.setDescrizioneAreaF(rs.getString("DESC_AREA_F"));
        foglioVO.setDescrizioneAreaG(rs.getString("DESC_AREA_G"));
        foglioVO.setIdAreaI(checkLongNull(rs.getString("F_ID_AREA_I")));
        foglioVO.setDescrizioneAreaI(rs.getString("DESC_AREA_I"));
        foglioVO.setIdAreaL(checkLongNull(rs.getString("F_ID_AREA_L")));
        foglioVO.setDescrizioneAreaL(rs.getString("DESC_AREA_L"));
        foglioVO.setFlagStabilizzazione(checkIntegerNull(rs.getString("FLAG_STABILIZZAZIONE")));
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "findFoglioByParameters in FoglioDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "findFoglioByParameters in FoglioDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally 
		{
			try 
			{
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) 
			{
				SolmrLogger.error(this, "findFoglioByParameters in FoglioDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) 
			{
				SolmrLogger.error(this, "findFoglioByParameters in FoglioDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated findFoglioByParameters method in FoglioDAO\n");
		return foglioVO;
	}
	
	/**
	 * Metodo che mi permette di recuperare l'elenco dei fogli a partire dai parametri
	 * 
	 * @param istatComune
	 * @param sezione
	 * @param foglio
	 * @return it.csi.solmr.dto.anag.FoglioVO[]
	 * @throws DataAccessException
	 */
	public FoglioVO[] getFogliByParameters(String istatComune, String sezione, String foglio) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getFogliByParameters method in FoglioDAO\n");
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    Vector<FoglioVO> elencoFogli = new Vector<FoglioVO>();

	    try {
	    	SolmrLogger.debug(this, "Creating db-connection in getFogliByParameters method in FoglioDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getFogliByParameters method in FoglioDAO and it values: "+conn+"\n");

			String query = " SELECT F.ID_FOGLIO, " +
						   "        F.ID_CASO_PARTICOLARE, " +
	                       "        CP.DESCRIZIONE, " +
	                       "        F.SEZIONE, " +
	                       "        S.DESCRIZIONE, " +
	                       "        F.ID_AREA_A, " +
	                       "        F.ID_AREA_B, " +
	                       "        F.ID_AREA_C, " +
	                       "        F.ID_AREA_D, " +
	                       "        F.ID_AREA_E, " +
	                       "        F.FLAG_CAPTAZIONE_POZZI, " +
	                       "        TAA.DESCRIZIONE, " +
	                       "        TAB.DESCRIZIONE, " +
	                       "        TAC.DESCRIZIONE, " +
	                       "        TAD.DESCRIZIONE, " +
	                       "        TAE.DESCRIZIONE, " +
	                       "        F.FOGLIO, " +
	                       "        F.COMUNE, " +
	                       "        C.DESCOM, " +
	                       "        F.ID_ZONA_ALTIMETRICA, " +
	                       "        TZA.DESCRIZIONE, " +
						   "        F.ID_AREA_F, " +
						   "        TAF.DESCRIZIONE AS DESC_AREA_F " +
	                       " FROM   DB_FOGLIO F, " +
	                       "        DB_TIPO_CASO_PARTICOLARE CP, " +
	                       "        DB_TIPO_AREA_A TAA, " +
	                       "        DB_TIPO_AREA_B TAB, " +
	                       "        DB_TIPO_AREA_C TAC, " +
	                       "        DB_TIPO_AREA_D TAD, " +
	                       "        DB_TIPO_AREA_E TAE, " +
	                       "        COMUNE C, " +
	                       "        DB_TIPO_ZONA_ALTIMETRICA TZA, " +
	                       "        DB_SEZIONE S, " +
	                       "        DB_TIPO_AREA_F TAF " +
	                       " WHERE  COMUNE = ? " +
	                       " AND    F.ID_CASO_PARTICOLARE = CP.ID_CASO_PARTICOLARE(+) " +
	                       " AND    F.ID_AREA_A = TAA.ID_AREA_A(+) " +
	                       " AND    F.ID_AREA_B = TAB.ID_AREA_B(+) " +
	                       " AND    F.ID_AREA_C = TAC.ID_AREA_C(+) " +
	                       " AND    F.ID_AREA_D = TAD.ID_AREA_D(+) " +
	                       " AND    F.ID_AREA_E = TAE.ID_AREA_E(+) " +
	                       " AND    F.COMUNE = C.ISTAT_COMUNE(+) " +
	                       " AND    F.ID_ZONA_ALTIMETRICA = TZA.ID_ZONA_ALTIMETRICA(+) " +
	                       " AND    S.SEZIONE(+) = F.SEZIONE " +
	                       " AND    F.COMUNE = S.ISTAT_COMUNE(+) " +
	                       " AND    F.ID_AREA_f = TAF.ID_AREA_F ";

			if(Validator.isNotEmpty(sezione)) {
				query += "AND F.SEZIONE = ? ";
			}
			if(Validator.isNotEmpty(foglio)) {
				query += "AND FOGLIO = ? ";
			}

			query += " ORDER BY F.COMUNE, F.SEZIONE, F.FOGLIO ";
			
			SolmrLogger.debug(this, "Value of parameter 1 [ISTAT_COMUNE] in getFogliByParameters method in FoglioDAO: "+istatComune+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [SEZIONE] in getFogliByParameters method in FoglioDAO: "+sezione+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [FOGLIO] in getFogliByParameters method in FoglioDAO: "+foglio+"\n");
			
			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing query: "+query);

			int indiceStatement = 0;

			stmt.setString(++indiceStatement, istatComune);

			if(Validator.isNotEmpty(sezione)) {
				stmt.setString(++indiceStatement, sezione.toUpperCase());
			}
			if(Validator.isNotEmpty(foglio)) {
				stmt.setString(++indiceStatement, foglio.toString());
			}

			ResultSet rs = stmt.executeQuery();

	        while(rs.next()) {
	        	FoglioVO foglioVO = new FoglioVO();
	        	foglioVO.setIdFoglio(new Long(rs.getLong(1)));
	        	if(rs.getString(2) != null && !rs.getString(2).equals("")) {
	        		foglioVO.setIdCasoParticolare(new Long(rs.getLong(2)));
	        	}
	        	foglioVO.setDescrizioneCasoParticolare(rs.getString(3));
	        	foglioVO.setSezione(rs.getString(4));
	        	foglioVO.setDescrizioneSezione(rs.getString(5));
	        	if(rs.getString(6) != null && !rs.getString(6).equals("")) {
	        		foglioVO.setIdAreaA(new Long(rs.getLong(6)));
	        	}
	        	if(rs.getString(7) != null && !rs.getString(7).equals("")) {
	        		foglioVO.setIdAreaB(new Long(rs.getLong(7)));
	        	}
	        	if(rs.getString(8) != null && !rs.getString(8).equals("")) {
	        		foglioVO.setIdAreaC(new Long(rs.getLong(8)));
	        	}
	        	if(rs.getString(9) != null && !rs.getString(9).equals("")) {
	        		foglioVO.setIdAreaD(new Long(rs.getLong(9)));
	        	}
	        	if(rs.getString(10) != null && !rs.getString(10).equals("")) {
	        		foglioVO.setIdAreaE(new Long(rs.getLong(10)));
	        	}
	        	foglioVO.setFlagCaptazionePozzi(rs.getString(11));
	        	foglioVO.setDescrizioneAreaA(rs.getString(12));
	        	foglioVO.setDescrizioneAreaB(rs.getString(13));
	        	foglioVO.setDescrizioneAreaC(rs.getString(14));
	        	foglioVO.setDescrizioneAreaD(rs.getString(15));
	        	foglioVO.setDescrizioneAreaE(rs.getString(16));
	        	if(rs.getString(17) != null && !rs.getString(17).equals("")) {
	        		foglioVO.setFoglio(new Long(rs.getLong(17)));
	        	}
	        	foglioVO.setIstatComune(rs.getString(18));
	        	foglioVO.setDescrizioneComune(rs.getString(19));
	        	if(rs.getString(20) != null && !rs.getString(20).equals("")) {
	        		foglioVO.setIdZonaAltimetrica(new Long(rs.getLong(20)));
	        	}
	        	foglioVO.setDescrizioneZonaAltimetrica(rs.getString(21));
	        	foglioVO.setIdAreaF(new Long(rs.getLong("ID_AREA_F")));
		        foglioVO.setDescrizioneAreaF(rs.getString("DESC_AREA_F"));
	        	elencoFogli.add(foglioVO);
	        }
	        
	        rs.close();
	        stmt.close();
	    }
	    catch(SQLException exc) {
			SolmrLogger.error(this, "getFogliByParameters in FoglioDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getFogliByParameters in FoglioDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getFogliByParameters in FoglioDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getFogliByParameters in FoglioDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getFogliByParameters method in FoglioDAO\n");
		if(elencoFogli.size() == 0) {
			return (FoglioVO[])elencoFogli.toArray(new FoglioVO[0]);
		}
		else {
			return (FoglioVO[])elencoFogli.toArray(new FoglioVO[elencoFogli.size()]);
		}
	}
}
