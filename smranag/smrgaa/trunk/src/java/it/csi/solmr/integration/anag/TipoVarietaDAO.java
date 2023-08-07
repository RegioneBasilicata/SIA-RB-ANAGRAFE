package it.csi.solmr.integration.anag;
	
import it.csi.solmr.dto.anag.terreni.*;
import it.csi.solmr.exception.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.util.*;
import java.sql.*;
import java.util.*;

public class TipoVarietaDAO extends it.csi.solmr.integration.BaseDAO {


	public TipoVarietaDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public TipoVarietaDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo che mi restituisce l'elenco delle varieta a partire dall'id_utilizzo
	 * 
	 * @param idUtilizzo
	 * @param onlyActive
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.TipoVarietaVO
	 * @throws DataAccessException
	 */
	public TipoVarietaVO[] getListTipoVarietaByIdUtilizzo(Long idUtilizzo, boolean onlyActive) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListTipoVarietaByIdUtilizzo method in TipoVarietaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoVarietaVO> elencoTipoVarieta = new Vector<TipoVarietaVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListTipoVarietaByIdUtilizzo method in TipoVarietaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListTipoVarietaByIdUtilizzo method in TipoVarietaDAO and it values: "+conn+"\n");

			String query = " SELECT DISTINCT TV.ID_VARIETA, " +
						   "        TV.ID_UTILIZZO, " +
						   "        TV.CODICE_VARIETA, " +
						   "        TV.DESCRIZIONE, " +
						   "        TV.ANNO_INIZIO_VALIDITA, " +
						   "        TV.ANNO_FINE_VALIDITA, " +
						   "        TV.RICHIESTA_RISERVA, " +
						   "        TV.PERMANENTE, " +
						   "        TV.FLAG_SIAP, " +
						   "        TV.FLAG_FORAGGERA_PERMANENTE, " + 
						   "        TV.FLAG_AVVICENDAMENTO, " +
						   "        TV.ID_TIPO_PERIODO_SEMINA " +	
						   " FROM   DB_TIPO_VARIETA TV, " +
						   "        DB_R_CATALOGO_MATRICE RCM " +
						   " WHERE  RCM.ID_UTILIZZO = ? " +
						   " AND    RCM.ID_VARIETA = TV.ID_VARIETA ";
			
			if(onlyActive) {
				query +=   " AND    RCM.DATA_FINE_VALIDITA IS NULL ";
			}
			query += "ORDER BY TV.DESCRIZIONE";
			

			SolmrLogger.debug(this, "Value of parameter 1 [ID_UTILIZZO] in getListTipoVarietaByIdUtilizzo method in TipoVarietaDAO: "+idUtilizzo+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ONLY_ACTIVE] in getListTipoVarietaByIdUtilizzo method in TipoVarietaDAO: "+onlyActive+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idUtilizzo.longValue());

			SolmrLogger.debug(this, "Executing getListTipoVarietaByIdUtilizzo: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
				tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
				tipoVarietaVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
				tipoVarietaVO.setCodiceVarieta(rs.getString("CODICE_VARIETA"));
				tipoVarietaVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoVarietaVO.setAnnoInizioValidita(rs.getString("ANNO_INIZIO_VALIDITA"));
				tipoVarietaVO.setAnnoFineValidita(rs.getString("ANNO_FINE_VALIDITA"));
				tipoVarietaVO.setRichiestaRiserva(rs.getString("RICHIESTA_RISERVA"));
				tipoVarietaVO.setPermanente(rs.getString("PERMANENTE"));
				tipoVarietaVO.setFlagSiap(rs.getString("FLAG_SIAP"));
				tipoVarietaVO.setFlagForaggeraPermanente(rs.getString("FLAG_FORAGGERA_PERMANENTE"));
				tipoVarietaVO.setFlagAvvicendamento(rs.getString("FLAG_AVVICENDAMENTO"));
				tipoVarietaVO.setIdTipoPeriodoSemina(checkLongNull(rs.getString("ID_TIPO_PERIODO_SEMINA")));
				
				elencoTipoVarieta.add(tipoVarietaVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListTipoVarietaByIdUtilizzo in TipoVarietaDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListTipoVarietaByIdUtilizzo in TipoVarietaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListTipoVarietaByIdUtilizzo in TipoVarietaDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListTipoVarietaByIdUtilizzo in TipoVarietaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipoVarietaByIdUtilizzo method in TipoVarietaDAO\n");
		if(elencoTipoVarieta.size() == 0) {
			return (TipoVarietaVO[])elencoTipoVarieta.toArray(new TipoVarietaVO[0]);
		}
		else {
			return (TipoVarietaVO[])elencoTipoVarieta.toArray(new TipoVarietaVO[elencoTipoVarieta.size()]);
		}
	}
	
	
	public Vector<TipoVarietaVO> getListTipoVarietaVitignoByMatriceAndComune(long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso,
	    long idTipoQualitaUso, String istatComune) throws DataAccessException 
	{
    SolmrLogger.debug(this, "Invocating getListTipoVarietaVitignoByMatriceAndComune method in TipoVarietaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoVarietaVO> elencoTipoVarieta = null;

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getListTipoVarietaVitignoByMatriceAndComune method in TipoVarietaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListTipoVarietaVitignoByMatriceAndComune method in TipoVarietaDAO and it values: "+conn+"\n");

      String query = 
        "SELECT DISTINCT " +
        "       TV.ID_VARIETA, " +
        "       TV.CODICE_VARIETA, " +
        "       TV.DESCRIZIONE, " +
        "       TV.RICHIESTA_RISERVA, " +
        "       TV.PERMANENTE, " +
        "       TV.FLAG_SIAP, " +
        "       TV.FLAG_FORAGGERA_PERMANENTE, " + 
        "       TV.FLAG_AVVICENDAMENTO," +
        "       RCM.ID_UTILIZZO " +
        "FROM   DB_R_CATALOGO_MATRICE RCM, " +
        "       DB_TIPO_VARIETA TV, " +
        "       DB_TIPO_VINO_VARIETA TVV, " +
        "       DB_TIPO_TIPOLOGIA_VINO TTV, " +
        "       DB_TIPO_VINIDOC_COMUNI TVC " +
        "WHERE  RCM.ID_UTILIZZO = ? " +
        "AND    RCM.ID_TIPO_DESTINAZIONE = ? " +
        "AND    RCM.ID_TIPO_DETTAGLIO_USO = ? " +
        "AND    RCM.ID_TIPO_QUALITA_USO = ? " +
        "AND    RCM.ID_VARIETA = TV.ID_VARIETA " +
        "AND    RCM.DATA_FINE_VALIDITA IS NULL " +
        "AND    TV.ID_VARIETA = TVV.ID_VARIETA " +
        "AND    TVV.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO " +
        "AND    TTV.ID_TIPOLOGIA_VINO = TVC.ID_TIPOLOGIA_VINO " +
        "AND    TVC.ISTAT_COMUNE = ? " +
        "AND    TTV.DATA_FINE_VALIDITA IS NULL " +
        "ORDER BY TV.DESCRIZIONE";
      

      SolmrLogger.debug(this, "Value of parameter 1 [ID_UTILIZZO] in getListTipoVarietaVitignoByMatriceAndComune method in TipoVarietaDAO: "+idUtilizzo+"\n");
      SolmrLogger.debug(this, "Value of parameter 1 [ID_TIPO_DESTINAZIONE] in getListTipoVarietaVitignoByMatriceAndComune method in TipoVarietaDAO: "+idTipoDestinazione+"\n");
      SolmrLogger.debug(this, "Value of parameter 1 [ID_TIPO_DETTAGLIO_USO] in getListTipoVarietaVitignoByMatriceAndComune method in TipoVarietaDAO: "+idTipoDettaglioUso+"\n");
      SolmrLogger.debug(this, "Value of parameter 1 [ID_TIPO_QUALITA_USO] in getListTipoVarietaVitignoByMatriceAndComune method in TipoVarietaDAO: "+idTipoQualitaUso+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ISTAT_COMUNE] in getListTipoVarietaVitignoByMatriceAndComune method in TipoVarietaDAO: "+istatComune+"\n");

      stmt = conn.prepareStatement(query);
      
      int idx = 0;
      
      stmt.setLong(++idx, idUtilizzo);
      stmt.setLong(++idx, idTipoDestinazione);
      stmt.setLong(++idx, idTipoDettaglioUso);
      stmt.setLong(++idx, idTipoQualitaUso);
      stmt.setString(++idx, istatComune);

      SolmrLogger.debug(this, "Executing getListTipoVarietaVitignoByMatriceAndComune: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        if(elencoTipoVarieta == null)
          elencoTipoVarieta = new Vector<TipoVarietaVO>();
        
        TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
        tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
        tipoVarietaVO.setCodiceVarieta(rs.getString("CODICE_VARIETA"));
        tipoVarietaVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoVarietaVO.setRichiestaRiserva(rs.getString("RICHIESTA_RISERVA"));
        tipoVarietaVO.setPermanente(rs.getString("PERMANENTE"));
        tipoVarietaVO.setFlagSiap(rs.getString("FLAG_SIAP"));
        tipoVarietaVO.setFlagForaggeraPermanente(rs.getString("FLAG_FORAGGERA_PERMANENTE"));
        tipoVarietaVO.setFlagAvvicendamento(rs.getString("FLAG_AVVICENDAMENTO"));
        tipoVarietaVO.setIdUtilizzo(rs.getLong("ID_UTILIZZO"));
        
        elencoTipoVarieta.add(tipoVarietaVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getListTipoVarietaVitignoByMatriceAndComune in TipoVarietaDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getListTipoVarietaVitignoByMatriceAndComune in TipoVarietaDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getListTipoVarietaVitignoByMatriceAndComune in TipoVarietaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getListTipoVarietaVitignoByMatriceAndComune in TipoVarietaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListTipoVarietaVitignoByMatriceAndComune method in TipoVarietaDAO\n");
    
    return elencoTipoVarieta;
    
  }
	
	/**
	 * Metodo che mi restituisce l'elenco delle varieta a partire dall'id_utilizzo
	 * e dal codice varietà: se parametro onlyActive è true, restituisce solo i records
	 * attivi
	 * 
	 * @param idUtilizzo
	 * @param codiceVarieta
	 * @param onlyActive
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.TipoVarietaVO
	 * @throws DataAccessException
	 */
	public TipoVarietaVO[] getListTipoVarietaByIdUtilizzoAndCodice(Long idUtilizzo, String codiceVarieta, boolean onlyActive, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListTipoVarietaByIdUtilizzoAndCodice method in TipoVarietaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoVarietaVO> elencoTipoVarieta = new Vector<TipoVarietaVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListTipoVarietaByIdUtilizzoAndCodice method in TipoVarietaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListTipoVarietaByIdUtilizzoAndCodice method in TipoVarietaDAO and it values: "+conn+"\n");

			String query = " SELECT ID_VARIETA, " +
						   "        ID_UTILIZZO, " +
						   "        CODICE_VARIETA, " +
						   "        DESCRIZIONE, " +
						   "        ANNO_INIZIO_VALIDITA, " +
						   "        ANNO_FINE_VALIDITA, " +
						   "        RICHIESTA_RISERVA, " +
						   "        PERMANENTE " +	
						   " FROM   DB_TIPO_VARIETA " +
						   " WHERE  ID_UTILIZZO = ? " +
						   " AND    CODICE_VARIETA = ? ";
			
			if(onlyActive) {
				query +=   " AND    ANNO_FINE_VALIDITA IS NULL ";
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

			SolmrLogger.debug(this, "Value of parameter 1 [ID_UTILIZZO] in getListTipoVarietaByIdUtilizzoAndCodice method in TipoVarietaDAO: "+idUtilizzo+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [CODICE_VARIETA] in getListTipoVarietaByIdUtilizzoAndCodice method in TipoVarietaDAO: "+codiceVarieta+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ONLY_ACTIVE] in getListTipoVarietaByIdUtilizzoAndCodice method in TipoVarietaDAO: "+onlyActive+"\n");
			SolmrLogger.debug(this, "Value of parameter 4 [ORDER_BY] in getListTipoVarietaByIdUtilizzoAndCodice method in TipoVarietaDAO: "+orderBy+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idUtilizzo.longValue());
			stmt.setString(2, codiceVarieta);

			SolmrLogger.debug(this, "Executing getListTipoVarietaByIdUtilizzoAndCodice: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
				tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
				tipoVarietaVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
				tipoVarietaVO.setCodiceVarieta(rs.getString("CODICE_VARIETA"));
				tipoVarietaVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoVarietaVO.setAnnoInizioValidita(rs.getString("ANNO_INIZIO_VALIDITA"));
				tipoVarietaVO.setAnnoFineValidita(rs.getString("ANNO_FINE_VALIDITA"));
				tipoVarietaVO.setRichiestaRiserva(rs.getString("RICHIESTA_RISERVA"));
				tipoVarietaVO.setPermanente(rs.getString("PERMANENTE"));
				elencoTipoVarieta.add(tipoVarietaVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListTipoVarietaByIdUtilizzoAndCodice in TipoVarietaDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListTipoVarietaByIdUtilizzoAndCodice in TipoVarietaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListTipoVarietaByIdUtilizzoAndCodice in TipoVarietaDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListTipoVarietaByIdUtilizzoAndCodice in TipoVarietaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipoVarietaByIdUtilizzoAndCodice method in TipoVarietaDAO\n");
		if(elencoTipoVarieta.size() == 0) {
			return (TipoVarietaVO[])elencoTipoVarieta.toArray(new TipoVarietaVO[0]);
		}
		else {
			return (TipoVarietaVO[])elencoTipoVarieta.toArray(new TipoVarietaVO[elencoTipoVarieta.size()]);
		}
	}
	
	/**
	 * Metodo che mi permette di recuperare il tipo varietà a partire
	 * dalla sua chiave primaria
	 * 
	 * @param idVarieta
	 * @return it.csi.solmr.dto.anag.terreni.TipoVarietaVO
	 * @throws DataAccessException
	 */
	public TipoVarietaVO findTipoVarietaByPrimaryKey(Long idVarieta) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating findTipoVarietaByPrimaryKey method in TipoVarietaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		TipoVarietaVO tipoVarietaVO = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in findTipoVarietaByPrimaryKey method in TipoVarietaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in findTipoVarietaByPrimaryKey method in TipoVarietaDAO and it values: "+conn+"\n");

			String query = " SELECT ID_VARIETA, " +
						   "        ID_UTILIZZO, " +
						   "        CODICE_VARIETA, " +
						   "        DESCRIZIONE, " +
						   "        ANNO_INIZIO_VALIDITA, " +
						   "        ANNO_FINE_VALIDITA, " +
						   "        RICHIESTA_RISERVA, " +
						   "        PERMANENTE, " +
						   "        FLAG_SIAP, " +
						   "        FLAG_FORAGGERA_PERMANENTE, " +
						   "        FLAG_AVVICENDAMENTO, " +
						   "        ID_TIPO_PERIODO_SEMINA " +
						   " FROM   DB_TIPO_VARIETA " +
						   " WHERE  ID_VARIETA = ? ";

			SolmrLogger.debug(this, "Value of parameter 1 [ID_VARIETA] in findTipoVarietaByPrimaryKey method in TipoVarietaDAO: "+idVarieta+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idVarieta.longValue());

			SolmrLogger.debug(this, "Executing findTipoVarietaByPrimaryKey: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) {
				tipoVarietaVO = new TipoVarietaVO();
				tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
				tipoVarietaVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
				tipoVarietaVO.setCodiceVarieta(rs.getString("CODICE_VARIETA"));
				tipoVarietaVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoVarietaVO.setAnnoInizioValidita(rs.getString("ANNO_INIZIO_VALIDITA"));
				tipoVarietaVO.setAnnoFineValidita(rs.getString("ANNO_FINE_VALIDITA"));
				tipoVarietaVO.setRichiestaRiserva(rs.getString("RICHIESTA_RISERVA"));
				tipoVarietaVO.setPermanente(rs.getString("PERMANENTE"));
				tipoVarietaVO.setFlagSiap(rs.getString("FLAG_SIAP"));
				tipoVarietaVO.setFlagForaggeraPermanente(rs.getString("FLAG_FORAGGERA_PERMANENTE"));
				tipoVarietaVO.setFlagAvvicendamento(rs.getString("FLAG_AVVICENDAMENTO"));
				tipoVarietaVO.setIdTipoPeriodoSemina(checkLongNull(rs.getString("ID_TIPO_PERIODO_SEMINA")));
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "findTipoVarietaByPrimaryKey in TipoVarietaDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "findTipoVarietaByPrimaryKey in TipoVarietaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "findTipoVarietaByPrimaryKey in TipoVarietaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "findTipoVarietaByPrimaryKey in TipoVarietaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated findTipoVarietaByPrimaryKey method in TipoVarietaDAO\n");
		return tipoVarietaVO;
	}
	
	
	/**
	 * 
	 * restituisce le varieta relativamente all'azienda
	 * 
	 * 
	 * @param idUtilizzo
	 * @param idAzienda
	 * @return
	 * @throws DataAccessException
	 */
	public TipoVarietaVO[] getListTipoVarietaByIdAzienda(long idAzienda) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getListTipoVarietaByIdUtilizzoAndAzienda method in TipoVarietaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoVarietaVO> elencoTipoVarieta = new Vector<TipoVarietaVO>();
  
    try {
      SolmrLogger.debug(this, "Creating db-connection in getListTipoVarietaByIdUtilizzoAndAzienda method in TipoVarietaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListTipoVarietaByIdUtilizzoAndAzienda method in TipoVarietaDAO and it values: "+conn+"\n");
  
      String query = 
          "SELECT DISTINCT " +
          "       TV.ID_VARIETA, " +
          "       TV.ID_UTILIZZO, " +
          "       TV.CODICE_VARIETA, " +
          "       TV.DESCRIZIONE " +
          "FROM " + 
          "       DB_STORICO_UNITA_ARBOREA SUA, " +
          "       DB_TIPO_VARIETA TV " +
          "WHERE " +
          "       SUA.ID_AZIENDA = ? " +
          "AND    SUA.ID_VARIETA = TV.ID_VARIETA " +
          "ORDER BY  TV.DESCRIZIONE ";
  
      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListTipoVarietaByIdUtilizzoAndAzienda method in TipoVarietaDAO: "+idAzienda+"\n");
        
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda);
      
      SolmrLogger.debug(this, "Executing getListTipoVarietaByIdUtilizzoAndAzienda: "+query+"\n");
  
      ResultSet rs = stmt.executeQuery();
  
      while(rs.next()) 
      {
        TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
        tipoVarietaVO.setIdVarieta(checkLongNull(rs.getString("ID_VARIETA")));
        tipoVarietaVO.setIdUtilizzo(checkLongNull(rs.getString("ID_UTILIZZO")));
        tipoVarietaVO.setCodiceVarieta(rs.getString("CODICE_VARIETA"));
        tipoVarietaVO.setDescrizione(rs.getString("DESCRIZIONE"));
        
        elencoTipoVarieta.add(tipoVarietaVO);
      }
      
      rs.close();
      stmt.close();
  
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getListTipoVarietaByIdUtilizzoAndAzienda in TipoVarietaDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getListTipoVarietaByIdUtilizzoAndAzienda in TipoVarietaDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getListTipoVarietaByIdUtilizzoAndAzienda in TipoVarietaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getListTipoVarietaByIdUtilizzoAndAzienda in TipoVarietaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListTipoVarietaByIdUtilizzoAndAzienda method in TipoVarietaDAO\n");
    if(elencoTipoVarieta.size() == 0) {
      return (TipoVarietaVO[])elencoTipoVarieta.toArray(new TipoVarietaVO[0]);
    }
    else {
      return (TipoVarietaVO[])elencoTipoVarieta.toArray(new TipoVarietaVO[elencoTipoVarieta.size()]);
    }
  }
	
	
	public TipoVarietaVO[] getListTipoVarietaByIdDichiarazioneConsistenza(long idDichiarazioneConsistenza) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getListTipoVarietaByIdDichiarazioneConsistenza method in TipoVarietaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoVarietaVO> elencoTipoVarieta = new Vector<TipoVarietaVO>();
  
    try {
      SolmrLogger.debug(this, "Creating db-connection in getListTipoVarietaByIdDichiarazioneConsistenza method in TipoVarietaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListTipoVarietaByIdDichiarazioneConsistenza method in TipoVarietaDAO and it values: "+conn+"\n");
  
      String query = 
          "SELECT DISTINCT " +
          "       TV.ID_VARIETA, " +
          "       TV.ID_UTILIZZO, " +
          "       TV.CODICE_VARIETA, " +
          "       TV.DESCRIZIONE " +
          "FROM " + 
          "       DB_UNITA_ARBOREA_DICHIARATA UAD, " +
          "       DB_TIPO_VARIETA TV," +
          "       DB_DICHIARAZIONE_CONSISTENZA DC " +
          "WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
          "AND    DC.CODICE_FOTOGRAFIA_TERRENI = UAD.CODICE_FOTOGRAFIA_TERRENI " +
          "AND    UAD.ID_VARIETA = TV.ID_VARIETA " +
          "ORDER BY  TV.DESCRIZIONE ";
  
      SolmrLogger.debug(this, "Value of parameter 1 [ID_DICHIARAZIONE_CONSISTENZA] in getListTipoVarietaByIdDichiarazioneConsistenza method in TipoVarietaDAO: "+idDichiarazioneConsistenza+"\n");
        
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idDichiarazioneConsistenza);
      
      SolmrLogger.debug(this, "Executing getListTipoVarietaByIdDichiarazioneConsistenza: "+query+"\n");
  
      ResultSet rs = stmt.executeQuery();
  
      while(rs.next()) 
      {
        TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
        tipoVarietaVO.setIdVarieta(checkLongNull(rs.getString("ID_VARIETA")));
        tipoVarietaVO.setIdUtilizzo(checkLongNull(rs.getString("ID_UTILIZZO")));
        tipoVarietaVO.setCodiceVarieta(rs.getString("CODICE_VARIETA"));
        tipoVarietaVO.setDescrizione(rs.getString("DESCRIZIONE"));
        
        elencoTipoVarieta.add(tipoVarietaVO);
      }
      
      rs.close();
      stmt.close();
  
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getListTipoVarietaByIdDichiarazioneConsistenza in TipoVarietaDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getListTipoVarietaByIdDichiarazioneConsistenza in TipoVarietaDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getListTipoVarietaByIdDichiarazioneConsistenza in TipoVarietaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getListTipoVarietaByIdDichiarazioneConsistenza in TipoVarietaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListTipoVarietaByIdDichiarazioneConsistenza method in TipoVarietaDAO\n");
    if(elencoTipoVarieta.size() == 0) {
      return (TipoVarietaVO[])elencoTipoVarieta.toArray(new TipoVarietaVO[0]);
    }
    else {
      return (TipoVarietaVO[])elencoTipoVarieta.toArray(new TipoVarietaVO[elencoTipoVarieta.size()]);
    }
  }
	
	
	
	
}
