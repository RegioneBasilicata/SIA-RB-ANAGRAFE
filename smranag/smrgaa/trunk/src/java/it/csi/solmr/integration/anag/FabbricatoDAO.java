package it.csi.solmr.integration.anag;
	
import it.csi.solmr.dto.anag.*;
import it.csi.solmr.dto.anag.fabbricati.TipoColturaSerraVO;
import it.csi.solmr.dto.anag.fabbricati.TipoFormaFabbricatoVO;
import it.csi.solmr.dto.anag.fabbricati.TipoTipologiaFabbricatoVO;
import it.csi.solmr.dto.anag.services.*;
import it.csi.solmr.exception.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.util.*;

import java.sql.*;
import java.util.*;

public class FabbricatoDAO extends it.csi.solmr.integration.BaseDAO {


	public FabbricatoDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public FabbricatoDAO(String refName) throws ResourceAccessException {
		super(refName);
	}
	
	/**
	 * Metodo utilizzato per estrarre i fabbricati di un'azienda relativi ad una
	 * specifica particella
	 * 
	 * @param idConduzioneParticella
	 * @param orderBy
	 * @param onlyActive
	 * @return
	 * @throws DataAccessException
	 */
	public FabbricatoParticellaVO[] getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella(Long idConduzioneParticella, Long idAzienda, String[] orderBy, boolean onlyActive) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella method in FabbricatoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<FabbricatoParticellaVO> elencoFabbricatoParticella = new Vector<FabbricatoParticellaVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella method in FabbricatoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella method in FabbricatoDAO and it values: "+conn+"\n");

			String query = " SELECT FP.ID_FABBRICATO_PARTICELLA, " +
						   "        FP.ID_FABBRICATO, " +
						   "        FP.ID_PARTICELLA, " +
						   "        FP.DATA_INIZIO_VALIDITA, " +
						   "        FP.DATA_FINE_VALIDITA " +
						   " FROM   DB_FABBRICATO_PARTICELLA FP, " +
						   "        DB_UTE U, " +
						   "        DB_FABBRICATO F, " +
						   "        DB_CONDUZIONE_PARTICELLA CP " +
						   " WHERE  U.ID_AZIENDA = ? " +
						   " AND    F.ID_UTE = U.ID_UTE " +
						   " AND    F.ID_FABBRICATO = FP.ID_FABBRICATO " +
						   " AND    CP.ID_CONDUZIONE_PARTICELLA = ? " +
						   " AND    CP.ID_PARTICELLA = FP.ID_PARTICELLA ";
			if(onlyActive) {
				query +=   " AND    F.DATA_FINE_VALIDITA IS NULL " +
				           " AND    FP.DATA_FINE_VALIDITA IS NULL ";
			}
		
			/*if(orderBy != null && orderBy.length > 0) {
				String criterio = "";
				for(int i = 0; i < orderBy.length; i++) {
					if(i == 0) {
						criterio = (String)orderBy[i];
					}
					else {
						criterio += ", "+(String)orderBy[i];
					}
				}
			}*/
			
			SolmrLogger.debug(this, "Value of parameter 1 [ID_CONDUZIONE_PARTICELLA] in getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella method in UtilizzoParticellaDAO: "+idConduzioneParticella+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ID_AZIENDA] in getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella method in FabbricatoDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ORDER_BY] in getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella method in FabbricatoDAO: "+orderBy+"\n");
			SolmrLogger.debug(this, "Value of parameter 4 [ONLY_ACTIVE] in getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella method in FabbricatoDAO: "+onlyActive+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());
			stmt.setLong(2, idConduzioneParticella.longValue());

			SolmrLogger.debug(this, "Executing getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				FabbricatoParticellaVO fabbricatoParticellaVO = new FabbricatoParticellaVO();
				fabbricatoParticellaVO.setIdFabbricatoParticella(new Long(rs.getLong("ID_FABBRICATO_PARTICELLA")));
				fabbricatoParticellaVO.setIdFabbricato(new Long(rs.getLong("ID_FABBRICATO")));
				fabbricatoParticellaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
				fabbricatoParticellaVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
				fabbricatoParticellaVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
				elencoFabbricatoParticella.add(fabbricatoParticellaVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella in FabbricatoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella in FabbricatoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella in FabbricatoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella in FabbricatoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella method in FabbricatoDAO\n");
		if(elencoFabbricatoParticella.size() == 0) {
			return (FabbricatoParticellaVO[])elencoFabbricatoParticella.toArray(new FabbricatoParticellaVO[0]);
		}
		else {
			return (FabbricatoParticellaVO[])elencoFabbricatoParticella.toArray(new FabbricatoParticellaVO[elencoFabbricatoParticella.size()]);
		}
	}
	
	/**
	 * Metodo utilizzato per effettuare la modifica del record relativo alla tabella
	 * DB_FABBRICATO_PARTICELLA
	 * 
	 * @param fabbricatoParticellaVO
	 * @throws DataAccessException
	 */
	public void updateFabbricatoParticella(FabbricatoParticellaVO fabbricatoParticellaVO) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating updateFabbricatoParticella method in FabbricatoDAO\n");
	    Connection conn = null;
	    PreparedStatement stmt = null;

	    try {
	    	SolmrLogger.debug(this, "Creating db-connection in updateFabbricatoParticella method in FabbricatoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in updateFabbricatoParticella method in FabbricatoDAO and it values: "+conn+"\n");

			String query = " UPDATE DB_FABBRICATO_PARTICELLA "+
	                       " SET    ID_FABBRICATO = ?, " +
	                       "        ID_PARTICELLA = ?, " +
	                       "        DATA_INIZIO_VALIDITA = ?, " +
	                       "        DATA_FINE_VALIDITA = ? " +
	                       " WHERE  ID_FABBRICATO_PARTICELLA = ? ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing updateFabbricatoParticella: "+query);

			stmt.setLong(1, fabbricatoParticellaVO.getIdFabbricato().longValue());
			SolmrLogger.debug(this, "Value of parameter 1 [ID_FABBRICATO] in method updateFabbricatoParticella in FabbricatoDAO: "+fabbricatoParticellaVO.getIdFabbricato().longValue()+"\n");
			stmt.setLong(2, fabbricatoParticellaVO.getIdParticella().longValue());
			SolmrLogger.debug(this, "Value of parameter 2 [ID_PARTICELLA] in method updateFabbricatoParticella in FabbricatoDAO: "+fabbricatoParticellaVO.getIdParticella().longValue()+"\n");
			stmt.setTimestamp(3, new Timestamp(fabbricatoParticellaVO.getDataInizioValidita().getTime()));
			SolmrLogger.debug(this, "Value of parameter 3 [DATA_INIZIO_VALIDITA] in method updateFabbricatoParticella in FabbricatoDAO: "+new Timestamp(fabbricatoParticellaVO.getDataInizioValidita().getTime())+"\n");
			if(fabbricatoParticellaVO.getDataFineValidita() != null) {
				stmt.setTimestamp(4, new Timestamp(fabbricatoParticellaVO.getDataFineValidita().getTime()));
				SolmrLogger.debug(this, "Value of parameter 4 [DATA_FINE_VALIDITA] in method updateFabbricatoParticella in FabbricatoDAO: "+new Timestamp(fabbricatoParticellaVO.getDataFineValidita().getTime())+"\n");
			}
			else {
				stmt.setString(4, null);
				SolmrLogger.debug(this, "Value of parameter 4 [DATA_FINE_VALIDITA] in method updateFabbricatoParticella in FabbricatoDAO: "+null+"\n");
			}
			stmt.setLong(5, fabbricatoParticellaVO.getIdFabbricatoParticella().longValue());
			SolmrLogger.debug(this, "Value of parameter 5 [ID_FABBRICATO_PARTICELLA] in method updateFabbricatoParticella in FabbricatoDAO: "+fabbricatoParticellaVO.getIdFabbricatoParticella().longValue()+"\n");

			stmt.executeUpdate();

			stmt.close();
	    }
	    catch(SQLException exc) {
	    	SolmrLogger.error(this, "updateFabbricatoParticella in FabbricatoDAO - SQLException: "+exc.getMessage());
	    	throw new DataAccessException(exc.getMessage());
	    }
	    catch (Exception ex) {
	    	SolmrLogger.error(this, "updateFabbricatoParticella in FabbricatoDAO - Generic Exception: "+ex);
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
	    		SolmrLogger.error(this, "updateFabbricatoParticella in FabbricatoDAO - SQLException while closing Statement and Connection: "+exc.getMessage());
	    		throw new DataAccessException(exc.getMessage());
	    	}
	    	catch(Exception ex) {
	    		SolmrLogger.error(this, "updateFabbricatoParticella in FabbricatoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage());
	    		throw new DataAccessException(ex.getMessage());
	    	}
	    }
	    SolmrLogger.debug(this, "Invocated updateFabbricatoParticella method in FabbricatoDAO\n");
	}
	
	/**
	 * Metodo che mi permette di recuperare l'elenco dei fabbricati di un'azienda
	 * relativi ad un determinato piano di riferimento
	 * 
	 * @param idAzienda
	 * @param idPianoRiferimento
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.FabbricatoParticellaVO[]
	 * @throws DataAccessException
	 */
	public FabbricatoVO[] getListFabbricatiAziendaByPianoRifererimento(Long idAzienda, Long idPianoRiferimento, Long idUte, String[] orderBy) throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating getListFabbricatiAziendaByPianoRifererimento method in FabbricatoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<FabbricatoVO> elencoFabbricati = new Vector<FabbricatoVO>();

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in getListFabbricatiAziendaByPianoRifererimento method in FabbricatoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListFabbricatiAziendaByPianoRifererimento method in FabbricatoDAO and it values: "+conn+"\n");

			String query = " " +
				"SELECT F.ID_FABBRICATO, " +
			  "       F.ID_TIPOLOGIA_FABBRICATO, " +
			  "       TTF.DESCRIZIONE AS DESC_TIPO_FABBRICATO, " +
			  "       TTF.UNITA_MISURA, " +
			  "       F.ID_FORMA_FABBRICATO, " +
			  "       TFF.DESCRIZIONE AS DESC_FORMA_FABBRICATO, " +
			  "       F.DENOMINAZIONE, " +
			  "       F.SUPERFICIE, " +
			  "       F.ANNO_COSTRUZIONE, " +
			  "       F.DIMENSIONE, " +
			  "       F.LUNGHEZZA, " +
			  "       F.LARGHEZZA, " + 
			  "       F.DATA_AGGIORNAMENTO, " +
			  "       F.ALTEZZA, " +
			  "       F.UTM_X, " +
			  "       F.UTM_Y, " +
			  "       F.NOTE, " +
			  "       F.ID_UTENTE_AGGIORNAMENTO, " +
			  "       F.ID_UTE, " +
			  "       F.DATA_INIZIO_VALIDITA, " +
			  "       F.DATA_FINE_VALIDITA, " +
			  "       F.ID_COLTURA_SERRA, ";
						   
	    if (idPianoRiferimento.intValue() > 0)
      {
        query += "" +
        "       (SELECT 'BIOLOGICO' " +
        "        FROM  DB_FABBRICATO_BIO FB " +
        "        WHERE FB.ID_AZIENDA = ? " +
        "        AND   FB.DATA_INIZIO_VALIDITA < DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "        AND   NVL(FB.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "        AND   FB.ID_FABBRICATO = F.ID_FABBRICATO " +
        "       ) AS BIOLOGICO, ";
      }
      else
      {
        query += "" +
        "       (SELECT 'BIOLOGICO' " +
        "        FROM  DB_FABBRICATO_BIO FB " +
        "        WHERE FB.ID_AZIENDA = ? " +
        "        AND   FB.DATA_FINE_VALIDITA IS NULL " +
        "        AND   FB.ID_FABBRICATO = F.ID_FABBRICATO " +
        "       ) AS BIOLOGICO, ";
      }
	   
			query += "" +
	      "       TCS.DESCRIZIONE AS DESC_COLTURA_SERRA, " +
			  "       F.MESI_RISCALDAMENTO_SERRA, " +
				"       F.ORE_RISCALDAMENTO_SERRA, " +
				"       C.DESCOM, " +
				"       P.SIGLA_PROVINCIA " +
				"FROM   DB_FABBRICATO F, " +
				"       DB_TIPO_TIPOLOGIA_FABBRICATO TTF, " +
				"       DB_TIPO_FORMA_FABBRICATO TFF, " +
				"       DB_TIPO_COLTURA_SERRA TCS, " +
				"       DB_UTE U, " +
				"       COMUNE C, " +
				"       PROVINCIA P ";
			if(idPianoRiferimento.intValue() > 0) {
				query +=   "        ,DB_DICHIARAZIONE_CONSISTENZA DC ";
			}
			query+=  
			  " WHERE  U.ID_AZIENDA = ? " +
				" AND    F.ID_TIPOLOGIA_FABBRICATO = TTF.ID_TIPOLOGIA_FABBRICATO " +
				" AND    F.ID_FORMA_FABBRICATO = TFF.ID_FORMA_FABBRICATO(+) " +
				" AND    F.ID_COLTURA_SERRA = TCS.ID_COLTURA_SERRA(+) " +
				" AND    F.ID_UTE = U.ID_UTE " +
				" AND    U.COMUNE = C.ISTAT_COMUNE " +
				" AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA ";
			
			if(Validator.isNotEmpty(idUte))
			{
			  query+=  
		    " AND    F.ID_UTE = ? ";
			}

			if(idPianoRiferimento.intValue() > 0) 
			{
				query +=   
				" AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
				" AND    F.DATA_INIZIO_VALIDITA < DC.DATA_INSERIMENTO_DICHIARAZIONE " +
				" AND    NVL(F.DATA_FINE_VALIDITA, ?) > DC.DATA_INSERIMENTO_DICHIARAZIONE ";
			}
			else {
				query +=   
				" AND    F.DATA_FINE_VALIDITA IS NULL ";
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
			
			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListFabbricatiAziendaByPianoRifererimento method in FabbricatoDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ID_PIANO_RIFERIMENTO] in getListFabbricatiAziendaByPianoRifererimento method in FabbricatoDAO: "+idPianoRiferimento+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ID_UTE] in getListFabbricatiAziendaByPianoRifererimento method in FabbricatoDAO: "+idUte+"\n");

			stmt = conn.prepareStatement(query);
			int idx = 0;
			
			stmt.setLong(++idx, idAzienda.longValue());
			stmt.setLong(++idx, idAzienda.longValue());
			if(Validator.isNotEmpty(idUte))
      {
			  stmt.setLong(++idx, idUte.longValue());
      }
			if(idPianoRiferimento.intValue() > 0) {
				stmt.setLong(++idx, idPianoRiferimento.longValue());
				stmt.setDate(++idx, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
			}

			SolmrLogger.debug(this, "Executing getListFabbricatiAziendaByPianoRifererimento: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) 
			{
				FabbricatoVO fabbricatoVO = new FabbricatoVO();
				fabbricatoVO.setIdFabbricato(new Long(rs.getLong("ID_FABBRICATO")));
				fabbricatoVO.setIdTipologiaFabbricato(new Long(rs.getLong("ID_TIPOLOGIA_FABBRICATO")));
				TipoTipologiaFabbricatoVO tipoTipologiaFabbricatoVO = new TipoTipologiaFabbricatoVO();
				tipoTipologiaFabbricatoVO.setIdTipologiaFabbricato(new Long(rs.getLong("ID_TIPOLOGIA_FABBRICATO")));
				tipoTipologiaFabbricatoVO.setDescrizione(rs.getString("DESC_TIPO_FABBRICATO"));
				tipoTipologiaFabbricatoVO.setUnitaMisura(rs.getString("UNITA_MISURA"));
				fabbricatoVO.setTipoTipologiaFabbricatoVO(tipoTipologiaFabbricatoVO);
				if(Validator.isNotEmpty(rs.getString("ID_FORMA_FABBRICATO"))) {
					fabbricatoVO.setIdFormaFabbricato(new Long(rs.getLong("ID_FORMA_FABBRICATO")));
					TipoFormaFabbricatoVO tipoFormaFabbricatoVO = new TipoFormaFabbricatoVO();
					tipoFormaFabbricatoVO.setIdFormaFabbricato(new Long(rs.getLong("ID_FORMA_FABBRICATO")));
					tipoFormaFabbricatoVO.setDescrizione(rs.getString("DESC_FORMA_FABBRICATO"));
					fabbricatoVO.setTipoFormaFabbricatoVO(tipoFormaFabbricatoVO);
				}
				fabbricatoVO.setDenominazioneFabbricato(rs.getString("DENOMINAZIONE"));
				fabbricatoVO.setSuperficieFabbricato(rs.getString("SUPERFICIE"));
				fabbricatoVO.setAnnoCostruzioneFabbricato(rs.getString("ANNO_COSTRUZIONE"));
				fabbricatoVO.setDimensioneFabbricato(rs.getString("DIMENSIONE"));
				fabbricatoVO.setLunghezzaFabbricato(rs.getString("LUNGHEZZA"));
				fabbricatoVO.setLarghezzaFabbricato(rs.getString("LARGHEZZA"));
				fabbricatoVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
				fabbricatoVO.setAltezzaFabbricato(rs.getString("ALTEZZA"));
				fabbricatoVO.setUtmx(rs.getString("UTM_X"));
				fabbricatoVO.setUtmy(rs.getString("UTM_Y"));
				fabbricatoVO.setNoteFabbricato(rs.getString("NOTE"));
				fabbricatoVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				fabbricatoVO.setIdUnitaProduttivaFabbricato(new Long(rs.getLong("ID_UTE")));
				fabbricatoVO.setDataInizioValiditaFabbricato(rs.getDate("DATA_INIZIO_VALIDITA"));
				fabbricatoVO.setDataFineValiditaFabbricato(rs.getDate("DATA_FINE_VALIDITA"));
				if(Validator.isNotEmpty(rs.getString("ID_COLTURA_SERRA"))) {
					fabbricatoVO.setIdColturaSerra(new Long(rs.getLong("ID_COLTURA_SERRA")));
					TipoColturaSerraVO tipoColturaSerraVO = new TipoColturaSerraVO();
					tipoColturaSerraVO.setIdColturaSerra(new Long(rs.getLong("ID_COLTURA_SERRA")));
					tipoColturaSerraVO.setDescrizione(rs.getString("DESC_COLTURA_SERRA"));
					fabbricatoVO.setTipoColturaSerraVO(tipoColturaSerraVO);
				}
				fabbricatoVO.setBiologico(rs.getString("BIOLOGICO"));
				fabbricatoVO.setMesiRiscSerra(rs.getString("MESI_RISCALDAMENTO_SERRA"));
				fabbricatoVO.setOreRisc(rs.getString("ORE_RISCALDAMENTO_SERRA"));
				fabbricatoVO.setDescComuneUte(rs.getString("DESCOM"));
				fabbricatoVO.setSiglaProvUte(rs.getString("SIGLA_PROVINCIA"));
				elencoFabbricati.add(fabbricatoVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListFabbricatiAziendaByPianoRifererimento in FabbricatoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListFabbricatiAziendaByPianoRifererimento in FabbricatoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListFabbricatiAziendaByPianoRifererimento in FabbricatoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListFabbricatiAziendaByPianoRifererimento in FabbricatoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListFabbricatiAziendaByPianoRifererimento method in FabbricatoDAO\n");
		if(elencoFabbricati.size() == 0) {
			return (FabbricatoVO[])elencoFabbricati.toArray(new FabbricatoVO[0]);
		}
		else {
			return (FabbricatoVO[])elencoFabbricati.toArray(new FabbricatoVO[elencoFabbricati.size()]);
		}
	}
	
	/**
	 * Metodo che verifica se esiste uno o più fabbricati ripristinati da una precedente
	 * dichiarazione di consistenza
	 * 
	 * @param idAzienda
	 * @return boolean
	 * @throws DataAccessException
	 */
	public boolean isFabbricatoRipristinato(Long idAzienda) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating isFabbricatoRipristinato method in FabbricatoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean isRipristinato = false;

		try {
			SolmrLogger.debug(this, "Creating db-connection in isFabbricatoRipristinato method in FabbricatoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in isFabbricatoRipristinato method in FabbricatoDAO and it values: "+conn+"\n");

			String query = " SELECT F.ID_FABBRICATO " +
						   " FROM   DB_FABBRICATO F, " +
						   "        DB_UTE U " +			
						   " WHERE  U.ID_AZIENDA = ? " +
						   " AND    F.ID_UTE = U.ID_UTE " +
						   " AND    F.DATA_FINE_VALIDITA IS NULL " +
						   " AND    F.DICHIARAZIONE_RIPRISTINATA = ? ";
			
			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in isFabbricatoRipristinato method in FabbricatoDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [DICHIARAZIONE_RIPRISTINATA] in isFabbricatoRipristinato method in FabbricatoDAO: "+SolmrConstants.FLAG_S+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());
			stmt.setString(2, SolmrConstants.FLAG_S);

			SolmrLogger.debug(this, "Executing isFabbricatoRipristinato: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				if(Validator.isNotEmpty(rs.getString("ID_FABBRICATO"))) {
					isRipristinato = true;
					break;
				}
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "isFabbricatoRipristinato in FabbricatoDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "isFabbricatoRipristinato in FabbricatoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "isFabbricatoRipristinato in FabbricatoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "isFabbricatoRipristinato in FabbricatoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated isFabbricatoRipristinato method in FabbricatoDAO\n");
		return isRipristinato;
	}
	
	
  //Metodo per effettuare la modifica di un record su DB_FABBRICATO
  public void storicizzaFabbricatoParticellaByPrimaryKey(Long idFabbricatoParticella)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " UPDATE DB_FABBRICATO_PARTICELLA "
          + " SET    DATA_FINE_VALIDITA = SYSDATE "
          + " WHERE  ID_FABBRICATO_PARTICELLA = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing storicizzaFabbricatoParticellaByPrimaryKey: "
          + query);

      stmt.setLong(1, idFabbricatoParticella.longValue());

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this,
          "Executed storicizzaFabbricatoParticellaByPrimaryKey in FabbricatoDAO");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "storicizzaFabbricatoParticellaByPrimaryKey in FabbricatoDAO - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "storicizzaFabbricatoParticellaByPrimaryKey in FabbricatoDAO - Generic Exception: "
              + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "storicizzaFabbricatoParticellaByPrimaryKey in FabbricatoDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "storicizzaFabbricatoParticellaByPrimaryKey in FabbricatoDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }
	
	
}
