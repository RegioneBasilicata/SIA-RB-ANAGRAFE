package it.csi.solmr.integration.anag;
	
import it.csi.solmr.dto.*;
import it.csi.solmr.dto.anag.terreni.*;
import it.csi.solmr.exception.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.util.*;

import java.sql.*;
import java.util.*;

/**
 * Classe che si occupa di interrogare la tabella DB_TIPO_UTILIZZO
 * @author Mauro Vocale
 *
 */
public class TipoUtilizzoDAO extends it.csi.solmr.integration.BaseDAO {


	public TipoUtilizzoDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public TipoUtilizzoDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo che mi restituisce l'elenco di tutti gli uso del suolo utilizzati dall'azienda
	 * in esame
	 * @param idAzienda
	 * @param onlyActive
	 * @param orderBy
	 * @return java.util.Vector
	 * @throws DataAccessException
	 */
	/*public Vector<TipoUtilizzoVO> getListTipiUsoSuoloByIdAzienda(Long idAzienda, boolean onlyActive, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListTipiUsoSuoloByIdAzienda method in TipoUtilizzoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = new Vector<TipoUtilizzoVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListTipiUsoSuoloByIdAzienda method in TipoUtilizzoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListTipiUsoSuoloByIdAzienda method in TipoUtilizzoDAO and it values: "+conn+"\n");

			String query = " SELECT TU.ID_UTILIZZO, " +
			   			   "        TU.ID_INDIRIZZO_UTILIZZO AS IND_UTILIZZO, " +
			   			   "        TIU.DESCRIZIONE AS DESC_IND_UTI, " +
						   "        TU.CODICE, " +
						   "        TU.DESCRIZIONE AS DESC_TIPO_UTI, " +
						   "        TU.TIPO, " +
						   "        TU.FLAG_SAU, " +
						   "        TU.FLAG_ARBOREO, " +
						   "        TU.ANNO_INIZIO_VALIDITA, " +
						   "        TU.ANNO_FINE_VALIDITA, " +
						   "        TU.FLAG_SERRA, " + 
						   "        TU.FLAG_PASCOLO, " +
						   "        TU.FLAG_ALIMENTAZIONE_ANIMALE, " +
						   "        TU.FLAG_FORAGGERA, " +
						   "        TU.FLAG_UBA_SOSTENIBILE, " +
						   "        TU.FLAG_COLTURA_SECONDARIA, " +
						   "        TU.FLAG_NIDI, " +
						   "        TU.FLAG_PRATO_PASCOLO, " +
						   "        TU.CODICE_PSR, " +
						   "        TU.FLAG_FRUTTA_GUSCIO, " +
						   "        TU.FLAG_TIPO_AVVICENDAMENTO, " +
						   "        TU.FLAG_PRINCIPALE " +
						   " FROM   DB_UTE U, " +
						   "        DB_CONDUZIONE_PARTICELLA CP, " +
						   "        DB_UTILIZZO_PARTICELLA UP, " +
						   "        DB_TIPO_UTILIZZO TU, " +	
						   "        DB_TIPO_INDIRIZZO_UTILIZZO TIU " +
						   " WHERE  U.ID_AZIENDA = ? " +
						   " AND    U.ID_UTE = CP.ID_UTE  " +
						   " AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA " +
						   " AND    UP.ID_UTILIZZO = TU.ID_UTILIZZO " +
						   " AND    TU.ID_INDIRIZZO_UTILIZZO = TIU.ID_INDIRIZZO_UTILIZZO ";
			if(onlyActive) {
				query +=   " AND    TU.ANNO_FINE_VALIDITA IS NULL ";
			}
			query +=       " UNION " +  
			   			   " SELECT TU.ID_UTILIZZO, " +
			   			   "        TU.ID_INDIRIZZO_UTILIZZO AS IND_UTILIZZO, " +
						   "        TIU.DESCRIZIONE AS DESC_IND_UTI, " +
						   "        TU.CODICE, " +
						   "        TU.DESCRIZIONE AS DESC_TIPO_UTI, " +
						   "        TU.TIPO, " +
						   "        TU.FLAG_SAU, " +
						   "        TU.FLAG_ARBOREO, " +
						   "        TU.ANNO_INIZIO_VALIDITA, " +
						   "        TU.ANNO_FINE_VALIDITA, " +
						   "        TU.FLAG_SERRA, " +
						   "        TU.FLAG_PASCOLO, " +
						   "        TU.FLAG_ALIMENTAZIONE_ANIMALE, " +
						   "        TU.FLAG_FORAGGERA, " +
						   "        TU.FLAG_UBA_SOSTENIBILE, " +
						   "        TU.FLAG_COLTURA_SECONDARIA, " +
						   "        TU.FLAG_NIDI, " +
						   "        TU.FLAG_PRATO_PASCOLO, " +	
						   "        TU.CODICE_PSR, " +
						   "        TU.FLAG_FRUTTA_GUSCIO, " +
						   "        TU.FLAG_TIPO_AVVICENDAMENTO, " +
						   "        TU.FLAG_PRINCIPALE " +
						   " FROM   DB_UTE U, " +
						   "        DB_DICHIARAZIONE_CONSISTENZA DC, " +
						   "        DB_CONDUZIONE_DICHIARATA CD, " +
						   "        DB_UTILIZZO_DICHIARATO UD, " +
						   "        DB_TIPO_UTILIZZO TU, " +
						   "        DB_TIPO_INDIRIZZO_UTILIZZO TIU " +
						   " WHERE  U.ID_AZIENDA = ? " +
						   " AND    U.ID_AZIENDA = DC.ID_AZIENDA " +
						   " AND    U.ID_UTE = CD.ID_UTE " +
						   " AND    CD.CODICE_FOTOGRAFIA_TERRENI = DC.CODICE_FOTOGRAFIA_TERRENI " +
						   " AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA " +
						   " AND    UD.ID_UTILIZZO = TU.ID_UTILIZZO " +
						   " AND    TU.ID_INDIRIZZO_UTILIZZO = TIU.ID_INDIRIZZO_UTILIZZO ";
			if(onlyActive) {
				query +=   " AND    TU.ANNO_FINE_VALIDITA IS NULL ";
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

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListTipiUsoSuoloByIdAzienda method in TipoUtilizzoDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ONLY_ACTIVE] in getListTipiUsoSuoloByIdAzienda method in TipoUtilizzoDAO: "+onlyActive+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ORDER_BY] in getListTipiUsoSuoloByIdAzienda method in TipoUtilizzoDAO: "+orderBy+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());
			stmt.setLong(2, idAzienda.longValue());
			
			SolmrLogger.debug(this, "Executing getListTipiUsoSuoloByIdAzienda: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
				tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
				CodeDescription code = new CodeDescription(Integer.decode(rs.getString("IND_UTILIZZO")), rs.getString("DESC_IND_UTI"));
				tipoUtilizzoVO.setIndirizzoUtilizzo(code);
				tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
				tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTI"));
				tipoUtilizzoVO.setTipo(rs.getString("TIPO"));
				tipoUtilizzoVO.setFlagSau(rs.getString("FLAG_SAU"));
				tipoUtilizzoVO.setFlagArboreo(rs.getString("FLAG_ARBOREO"));
				tipoUtilizzoVO.setAnnoInizioValidita(rs.getString("ANNO_INIZIO_VALIDITA"));
				tipoUtilizzoVO.setAnnoFineValidita(rs.getString("ANNO_FINE_VALIDITA"));
				tipoUtilizzoVO.setFlagSerra(rs.getString("FLAG_SERRA"));
				tipoUtilizzoVO.setFlagPascolo(rs.getString("FLAG_PASCOLO"));
				tipoUtilizzoVO.setFlagAlimentazioneAnimale(rs.getString("FLAG_ALIMENTAZIONE_ANIMALE"));
				tipoUtilizzoVO.setFlagForaggera(rs.getString("FLAG_FORAGGERA"));
				tipoUtilizzoVO.setFlagUbaSostenibile(rs.getString("FLAG_UBA_SOSTENIBILE"));
				tipoUtilizzoVO.setFlagColturaSecondaria(rs.getString("FLAG_COLTURA_SECONDARIA"));
				tipoUtilizzoVO.setFlagNidi(rs.getString("FLAG_NIDI"));
				tipoUtilizzoVO.setFlagPratoPascolo(rs.getString("FLAG_PRATO_PASCOLO"));
				tipoUtilizzoVO.setCodicePsr(rs.getString("CODICE_PSR"));
				tipoUtilizzoVO.setFlagFruttaGuscio(rs.getString("FLAG_FRUTTA_GUSCIO"));
				tipoUtilizzoVO.setFlagTipoAvvicendamento(rs.getString("FLAG_TIPO_AVVICENDAMENTO"));
				tipoUtilizzoVO.setFlagPrincipale(rs.getString("FLAG_PRINCIPALE"));
				elencoTipiUsoSuolo.add(tipoUtilizzoVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListTipiUsoSuoloByIdAzienda in TipoUtilizzoDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListTipiUsoSuoloByIdAzienda in TipoUtilizzoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListTipiUsoSuoloByIdAzienda in TipoUtilizzoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListTipiUsoSuoloByIdAzienda in TipoUtilizzoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipiUsoSuoloByIdAzienda method in TipoUtilizzoDAO\n");
		return elencoTipiUsoSuolo;
	}*/
	
	/*public Vector<TipoUtilizzoVO> getListTipiDestinazProdPrimSecByIdAzienda(Long idAzienda) 
	    throws DataAccessException 
	{
    SolmrLogger.debug(this, "Invocating getListTipiDestinazProdPrimSecByIdAzienda method in TipoUtilizzoDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = new Vector<TipoUtilizzoVO>();

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getListTipiDestinazProdPrimSecByIdAzienda method in TipoUtilizzoDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListTipiDestinazProdPrimSecByIdAzienda method in TipoUtilizzoDAO and it values: "+conn+"\n");

      String query = 
        " SELECT TU.ID_UTILIZZO, " +
        "        TU.ID_INDIRIZZO_UTILIZZO AS IND_UTILIZZO, " +
        "        TIU.DESCRIZIONE AS DESC_IND_UTI, " +
        "        TU.CODICE, " +
        "        TU.DESCRIZIONE AS DESC_TIPO_UTI, " +
        "        TU.TIPO, " +
        "        TU.FLAG_SAU, " +
        "        TU.FLAG_ARBOREO, " +
        "        TU.ANNO_INIZIO_VALIDITA, " +
        "        TU.ANNO_FINE_VALIDITA, " +
        "        TU.FLAG_SERRA, " + 
        "        TU.FLAG_PASCOLO, " +
        "        TU.FLAG_ALIMENTAZIONE_ANIMALE, " +
        "        TU.FLAG_FORAGGERA, " +
        "        TU.FLAG_UBA_SOSTENIBILE, " +
        "        TU.FLAG_COLTURA_SECONDARIA, " +
        "        TU.FLAG_NIDI, " +
        "        TU.FLAG_PRATO_PASCOLO, " +
        "        TU.CODICE_PSR, " +
        "        TU.FLAG_FRUTTA_GUSCIO, " +
        "        TU.FLAG_TIPO_AVVICENDAMENTO, " +
        "        TU.FLAG_PRINCIPALE " +
        " FROM   DB_UTE U, " +
        "        DB_CONDUZIONE_PARTICELLA CP, " +
        "        DB_UTILIZZO_PARTICELLA UP, " +
        "        DB_TIPO_UTILIZZO TU, " +  
        "        DB_TIPO_INDIRIZZO_UTILIZZO TIU " +
        " WHERE  U.ID_AZIENDA = ? " +
        " AND    U.ID_UTE = CP.ID_UTE  " +
        " AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA " +
        " AND    (UP.ID_UTILIZZO = TU.ID_UTILIZZO " +
        "          OR UP.ID_UTILIZZO_SECONDARIO = TU.ID_UTILIZZO) " +
        " AND    TU.ID_INDIRIZZO_UTILIZZO = TIU.ID_INDIRIZZO_UTILIZZO " +
        " UNION " +  
        " SELECT TU.ID_UTILIZZO, " +
        "        TU.ID_INDIRIZZO_UTILIZZO AS IND_UTILIZZO, " +
        "        TIU.DESCRIZIONE AS DESC_IND_UTI, " +
        "        TU.CODICE, " +
        "        TU.DESCRIZIONE AS DESC_TIPO_UTI, " +
        "        TU.TIPO, " +
        "        TU.FLAG_SAU, " +
        "        TU.FLAG_ARBOREO, " +
        "        TU.ANNO_INIZIO_VALIDITA, " +
        "        TU.ANNO_FINE_VALIDITA, " +
        "        TU.FLAG_SERRA, " +
        "        TU.FLAG_PASCOLO, " +
        "        TU.FLAG_ALIMENTAZIONE_ANIMALE, " +
        "        TU.FLAG_FORAGGERA, " +
        "        TU.FLAG_UBA_SOSTENIBILE, " +
        "        TU.FLAG_COLTURA_SECONDARIA, " +
        "        TU.FLAG_NIDI, " +
        "        TU.FLAG_PRATO_PASCOLO, " +  
        "        TU.CODICE_PSR, " +
        "        TU.FLAG_FRUTTA_GUSCIO, " +
        "        TU.FLAG_TIPO_AVVICENDAMENTO, " +
        "        TU.FLAG_PRINCIPALE " +
        " FROM   DB_UTE U, " +
        "        DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "        DB_CONDUZIONE_DICHIARATA CD, " +
        "        DB_UTILIZZO_DICHIARATO UD, " +
        "        DB_TIPO_UTILIZZO TU, " +
        "        DB_TIPO_INDIRIZZO_UTILIZZO TIU " +
        " WHERE  U.ID_AZIENDA = ? " +
        " AND    U.ID_AZIENDA = DC.ID_AZIENDA " +
        " AND    U.ID_UTE = CD.ID_UTE " +
        " AND    CD.CODICE_FOTOGRAFIA_TERRENI = DC.CODICE_FOTOGRAFIA_TERRENI " +
        " AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA " +
        " AND    (UD.ID_UTILIZZO = TU.ID_UTILIZZO " +
        "          OR UD.ID_UTILIZZO_SECONDARIO = TU.ID_UTILIZZO) " +
        " AND    TU.ID_INDIRIZZO_UTILIZZO = TIU.ID_INDIRIZZO_UTILIZZO " +
        " ORDER BY DESC_TIPO_UTI ";
      

      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListTipiDestinazProdPrimSecByIdAzienda method in TipoUtilizzoDAO: "+idAzienda+"\n");
    
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda.longValue());
      stmt.setLong(2, idAzienda.longValue());
      
      SolmrLogger.debug(this, "Executing getListTipiDestinazProdPrimSecByIdAzienda: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) {
        TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
        tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
        CodeDescription code = new CodeDescription(Integer.decode(rs.getString("IND_UTILIZZO")), rs.getString("DESC_IND_UTI"));
        tipoUtilizzoVO.setIndirizzoUtilizzo(code);
        tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
        tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTI"));
        tipoUtilizzoVO.setTipo(rs.getString("TIPO"));
        tipoUtilizzoVO.setFlagSau(rs.getString("FLAG_SAU"));
        tipoUtilizzoVO.setFlagArboreo(rs.getString("FLAG_ARBOREO"));
        tipoUtilizzoVO.setAnnoInizioValidita(rs.getString("ANNO_INIZIO_VALIDITA"));
        tipoUtilizzoVO.setAnnoFineValidita(rs.getString("ANNO_FINE_VALIDITA"));
        tipoUtilizzoVO.setFlagSerra(rs.getString("FLAG_SERRA"));
        tipoUtilizzoVO.setFlagPascolo(rs.getString("FLAG_PASCOLO"));
        tipoUtilizzoVO.setFlagAlimentazioneAnimale(rs.getString("FLAG_ALIMENTAZIONE_ANIMALE"));
        tipoUtilizzoVO.setFlagForaggera(rs.getString("FLAG_FORAGGERA"));
        tipoUtilizzoVO.setFlagUbaSostenibile(rs.getString("FLAG_UBA_SOSTENIBILE"));
        tipoUtilizzoVO.setFlagColturaSecondaria(rs.getString("FLAG_COLTURA_SECONDARIA"));
        tipoUtilizzoVO.setFlagNidi(rs.getString("FLAG_NIDI"));
        tipoUtilizzoVO.setFlagPratoPascolo(rs.getString("FLAG_PRATO_PASCOLO"));
        tipoUtilizzoVO.setCodicePsr(rs.getString("CODICE_PSR"));
        tipoUtilizzoVO.setFlagFruttaGuscio(rs.getString("FLAG_FRUTTA_GUSCIO"));
        tipoUtilizzoVO.setFlagTipoAvvicendamento(rs.getString("FLAG_TIPO_AVVICENDAMENTO"));
        tipoUtilizzoVO.setFlagPrincipale(rs.getString("FLAG_PRINCIPALE"));
        elencoTipiUsoSuolo.add(tipoUtilizzoVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "getListTipiDestinazProdPrimSecByIdAzienda in TipoUtilizzoDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) 
    {
      SolmrLogger.error(this, "getListTipiDestinazProdPrimSecByIdAzienda in TipoUtilizzoDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) 
      {
        SolmrLogger.error(this, "getListTipiDestinazProdPrimSecByIdAzienda in TipoUtilizzoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) 
      {
        SolmrLogger.error(this, "getListTipiDestinazProdPrimSecByIdAzienda in TipoUtilizzoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListTipiDestinazProdPrimSecByIdAzienda method in TipoUtilizzoDAO\n");
    return elencoTipiUsoSuolo;
  }*/
	
	
	public Vector<TipoUtilizzoVO> getListUtilizziElencoTerrPianoLavorazione(Long idAzienda) 
      throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getListUtilizziElencoTerrPianoLavorazione method in TipoUtilizzoDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = new Vector<TipoUtilizzoVO>();

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getListUtilizziElencoTerrPianoLavorazione method in TipoUtilizzoDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListUtilizziElencoTerrPianoLavorazione method in TipoUtilizzoDAO and it values: "+conn+"\n");

      String query = 
        "WITH TABELLA AS (SELECT RCM.ID_UTILIZZO " +
        "                 FROM   DB_UTE U, " +
        "                        DB_CONDUZIONE_PARTICELLA CP, " +
        "                        DB_UTILIZZO_PARTICELLA UP, " +
        "                        DB_R_CATALOGO_MATRICE RCM " +
        "                 WHERE  U.ID_AZIENDA = ? " +
        "                 AND    U.ID_UTE = CP.ID_UTE " +
        "                 AND    U.DATA_FINE_ATTIVITA IS NULL " +
        "                 AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA " +
        "                 AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "                 AND    UP.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +
        "                 GROUP BY RCM.ID_UTILIZZO " +
        "                 UNION " +
        "                 SELECT RCM.ID_UTILIZZO " +
        "                 FROM   DB_UTE U, " +
        "                        DB_CONDUZIONE_PARTICELLA CP, " +
        "                        DB_UTILIZZO_PARTICELLA UP, " +
        "                        DB_R_CATALOGO_MATRICE RCM " +
        "                 WHERE  U.ID_AZIENDA = ? " +
        "                 AND    U.ID_UTE = CP.ID_UTE " +
        "                 AND    U.DATA_FINE_ATTIVITA IS NULL " +
        "                 AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA " +
        "                 AND    UP.ID_CATALOGO_MATRICE_SECONDARIO = RCM.ID_CATALOGO_MATRICE " +
        "                 AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "                 AND    UP.ID_CATALOGO_MATRICE_SECONDARIO IS NOT NULL " +
        "                 GROUP BY RCM.ID_UTILIZZO) " +
        "SELECT TU.ID_UTILIZZO, " +
        "       TU.CODICE, " +
        "       TU.DESCRIZIONE AS DESC_TIPO_UTI, " +
        "       TU.TIPO, " +
        "       TU.FLAG_SAU, " +
        "       TU.FLAG_ARBOREO, " +
        "       TU.ANNO_INIZIO_VALIDITA, " +
        "       TU.ANNO_FINE_VALIDITA, " +
        "       TU.FLAG_SERRA, " +
        "       TU.FLAG_PASCOLO, " +
        "       TU.FLAG_ALIMENTAZIONE_ANIMALE, " +
        "       TU.FLAG_FORAGGERA, " +
        "       TU.FLAG_UBA_SOSTENIBILE, " +
        "       TU.FLAG_COLTURA_SECONDARIA, " +
        "       TU.FLAG_NIDI, " +
        "       TU.FLAG_PRATO_PASCOLO, " +
        "       TU.CODICE_PSR, " +
        "       TU.FLAG_FRUTTA_GUSCIO, " +
        "       TU.FLAG_TIPO_AVVICENDAMENTO, " +
        "       TU.FLAG_PRINCIPALE " +
        "FROM   DB_TIPO_UTILIZZO TU, " +
        "       TABELLA UP " +
        "WHERE  TU.ID_UTILIZZO = UP.ID_UTILIZZO " +
        "ORDER BY TU.DESCRIZIONE ";
      

      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListUtilizziElencoTerrPianoLavorazione method in TipoUtilizzoDAO: "+idAzienda+"\n");
    
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda.longValue());
      stmt.setLong(2, idAzienda.longValue());
      
      SolmrLogger.debug(this, "Executing getListUtilizziElencoTerrPianoLavorazione: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) {
        TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
        tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
        tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
        tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTI"));
        tipoUtilizzoVO.setTipo(rs.getString("TIPO"));
        tipoUtilizzoVO.setFlagSau(rs.getString("FLAG_SAU"));
        tipoUtilizzoVO.setFlagArboreo(rs.getString("FLAG_ARBOREO"));
        tipoUtilizzoVO.setAnnoInizioValidita(rs.getString("ANNO_INIZIO_VALIDITA"));
        tipoUtilizzoVO.setAnnoFineValidita(rs.getString("ANNO_FINE_VALIDITA"));
        tipoUtilizzoVO.setFlagSerra(rs.getString("FLAG_SERRA"));
        tipoUtilizzoVO.setFlagPascolo(rs.getString("FLAG_PASCOLO"));
        tipoUtilizzoVO.setFlagAlimentazioneAnimale(rs.getString("FLAG_ALIMENTAZIONE_ANIMALE"));
        tipoUtilizzoVO.setFlagForaggera(rs.getString("FLAG_FORAGGERA"));
        tipoUtilizzoVO.setFlagUbaSostenibile(rs.getString("FLAG_UBA_SOSTENIBILE"));
        tipoUtilizzoVO.setFlagColturaSecondaria(rs.getString("FLAG_COLTURA_SECONDARIA"));
        tipoUtilizzoVO.setFlagNidi(rs.getString("FLAG_NIDI"));
        tipoUtilizzoVO.setFlagPratoPascolo(rs.getString("FLAG_PRATO_PASCOLO"));
        tipoUtilizzoVO.setCodicePsr(rs.getString("CODICE_PSR"));
        tipoUtilizzoVO.setFlagFruttaGuscio(rs.getString("FLAG_FRUTTA_GUSCIO"));
        tipoUtilizzoVO.setFlagTipoAvvicendamento(rs.getString("FLAG_TIPO_AVVICENDAMENTO"));
        tipoUtilizzoVO.setFlagPrincipale(rs.getString("FLAG_PRINCIPALE"));
        elencoTipiUsoSuolo.add(tipoUtilizzoVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "getListUtilizziElencoTerrPianoLavorazione in TipoUtilizzoDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) 
    {
      SolmrLogger.error(this, "getListUtilizziElencoTerrPianoLavorazione in TipoUtilizzoDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) 
      {
        SolmrLogger.error(this, "getListUtilizziElencoTerrPianoLavorazione in TipoUtilizzoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) 
      {
        SolmrLogger.error(this, "getListUtilizziElencoTerrPianoLavorazione in TipoUtilizzoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListUtilizziElencoTerrPianoLavorazione method in TipoUtilizzoDAO\n");
    return elencoTipiUsoSuolo;
  }
	
	
	public Vector<TipoUtilizzoVO> getListUtilizziElencoTerrPianoLavorazioneStor(Long idAzienda) 
      throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getListUtilizziElencoTerrPianoLavorazioneStor method in TipoUtilizzoDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = new Vector<TipoUtilizzoVO>();

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getListUtilizziElencoTerrPianoLavorazioneStor method in TipoUtilizzoDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListUtilizziElencoTerrPianoLavorazioneStor method in TipoUtilizzoDAO and it values: "+conn+"\n");

      String query = 
        " WITH TABELLA AS (SELECT RCM.ID_UTILIZZO " +
        "                  FROM   DB_UTE U, " +
        "                         DB_CONDUZIONE_PARTICELLA CP, " +
        "                         DB_UTILIZZO_PARTICELLA UP, " +
        "                         DB_R_CATALOGO_MATRICE RCM " +
        "                  WHERE  U.ID_AZIENDA = ? " +
        "                  AND    U.ID_UTE = CP.ID_UTE " +
        "                  AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA " +
        "                  AND    UP.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +
        "                  GROUP BY RCM.ID_UTILIZZO " +
        "                  UNION " +
        "                  SELECT RCM.ID_UTILIZZO " +
        "                  FROM   DB_UTE U, " +
        "                         DB_CONDUZIONE_PARTICELLA CP, " +
        "                         DB_UTILIZZO_PARTICELLA UP, " +
        "                         DB_R_CATALOGO_MATRICE RCM " +
        "                  WHERE  U.ID_AZIENDA = ? " +
        "                  AND    U.ID_UTE = CP.ID_UTE " +
        "                  AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA " +
        "                  AND    UP.ID_CATALOGO_MATRICE_SECONDARIO = RCM.ID_CATALOGO_MATRICE " +
        "                  AND    UP.ID_CATALOGO_MATRICE_SECONDARIO IS NOT NULL " +
        "                  GROUP BY RCM.ID_UTILIZZO) " +
        "SELECT TU.ID_UTILIZZO, " +
        "       TU.CODICE, " +
        "       TU.DESCRIZIONE AS DESC_TIPO_UTI, " +
        "       TU.TIPO, " +
        "       TU.FLAG_SAU, " +
        "       TU.FLAG_ARBOREO, " +
        "       TU.ANNO_INIZIO_VALIDITA, " +
        "       TU.ANNO_FINE_VALIDITA, " +
        "       TU.FLAG_SERRA, " +
        "       TU.FLAG_PASCOLO, " +
        "       TU.FLAG_ALIMENTAZIONE_ANIMALE, " +
        "       TU.FLAG_FORAGGERA, " +
        "       TU.FLAG_UBA_SOSTENIBILE, " +
        "       TU.FLAG_COLTURA_SECONDARIA, " +
        "       TU.FLAG_NIDI, " +
        "       TU.FLAG_PRATO_PASCOLO, " +
        "       TU.CODICE_PSR, " +
        "       TU.FLAG_FRUTTA_GUSCIO, " +
        "       TU.FLAG_TIPO_AVVICENDAMENTO, " +
        "       TU.FLAG_PRINCIPALE " +
        "FROM   DB_TIPO_UTILIZZO TU, " +
        "       TABELLA UP " +
        "WHERE  TU.ID_UTILIZZO = UP.ID_UTILIZZO " +
        "ORDER BY TU.DESCRIZIONE ";
      

      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListUtilizziElencoTerrPianoLavorazioneStor method in TipoUtilizzoDAO: "+idAzienda+"\n");
    
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda.longValue());
      stmt.setLong(2, idAzienda.longValue());
      
      SolmrLogger.debug(this, "Executing getListUtilizziElencoTerrPianoLavorazioneStor: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) {
        TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
        tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
        tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
        tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTI"));
        tipoUtilizzoVO.setTipo(rs.getString("TIPO"));
        tipoUtilizzoVO.setFlagSau(rs.getString("FLAG_SAU"));
        tipoUtilizzoVO.setFlagArboreo(rs.getString("FLAG_ARBOREO"));
        tipoUtilizzoVO.setAnnoInizioValidita(rs.getString("ANNO_INIZIO_VALIDITA"));
        tipoUtilizzoVO.setAnnoFineValidita(rs.getString("ANNO_FINE_VALIDITA"));
        tipoUtilizzoVO.setFlagSerra(rs.getString("FLAG_SERRA"));
        tipoUtilizzoVO.setFlagPascolo(rs.getString("FLAG_PASCOLO"));
        tipoUtilizzoVO.setFlagAlimentazioneAnimale(rs.getString("FLAG_ALIMENTAZIONE_ANIMALE"));
        tipoUtilizzoVO.setFlagForaggera(rs.getString("FLAG_FORAGGERA"));
        tipoUtilizzoVO.setFlagUbaSostenibile(rs.getString("FLAG_UBA_SOSTENIBILE"));
        tipoUtilizzoVO.setFlagColturaSecondaria(rs.getString("FLAG_COLTURA_SECONDARIA"));
        tipoUtilizzoVO.setFlagNidi(rs.getString("FLAG_NIDI"));
        tipoUtilizzoVO.setFlagPratoPascolo(rs.getString("FLAG_PRATO_PASCOLO"));
        tipoUtilizzoVO.setCodicePsr(rs.getString("CODICE_PSR"));
        tipoUtilizzoVO.setFlagFruttaGuscio(rs.getString("FLAG_FRUTTA_GUSCIO"));
        tipoUtilizzoVO.setFlagTipoAvvicendamento(rs.getString("FLAG_TIPO_AVVICENDAMENTO"));
        tipoUtilizzoVO.setFlagPrincipale(rs.getString("FLAG_PRINCIPALE"));
        elencoTipiUsoSuolo.add(tipoUtilizzoVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "getListUtilizziElencoTerrPianoLavorazioneStor in TipoUtilizzoDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) 
    {
      SolmrLogger.error(this, "getListUtilizziElencoTerrPianoLavorazioneStor in TipoUtilizzoDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) 
      {
        SolmrLogger.error(this, "getListUtilizziElencoTerrPianoLavorazioneStor in TipoUtilizzoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) 
      {
        SolmrLogger.error(this, "getListUtilizziElencoTerrPianoLavorazioneStor in TipoUtilizzoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListUtilizziElencoTerrPianoLavorazioneStor method in TipoUtilizzoDAO\n");
    return elencoTipiUsoSuolo;
  }
	
	public Vector<TipoUtilizzoVO> getListUtilizziElencoTerrValidazione(Long idDichiarazioneConsistenza) 
      throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getListUtilizziElencoTerrValidazione method in TipoUtilizzoDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = new Vector<TipoUtilizzoVO>();

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getListUtilizziElencoTerrValidazione method in TipoUtilizzoDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListUtilizziElencoTerrValidazione method in TipoUtilizzoDAO and it values: "+conn+"\n");

      String query = 
        "WITH TABELLA AS (SELECT RCM.ID_UTILIZZO " +
        "                 FROM   DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "                        DB_CONDUZIONE_DICHIARATA CP, " +
        "                        DB_UTILIZZO_DICHIARATO UP, " +
        "                        DB_R_CATALOGO_MATRICE RCM " +
        "                 WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "                 AND    DC.CODICE_FOTOGRAFIA_TERRENI = CP.CODICE_FOTOGRAFIA_TERRENI " +                    
        "                 AND    CP.ID_CONDUZIONE_DICHIARATA = UP.ID_CONDUZIONE_DICHIARATA " +                    
        "                 AND    UP.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +      
        "                 GROUP BY RCM.ID_UTILIZZO " +
        "                 UNION " +
        "                 SELECT RCM.ID_UTILIZZO " +                  
        "                 FROM   DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "                        DB_CONDUZIONE_DICHIARATA CP, " +
        "                        DB_UTILIZZO_DICHIARATO UP, " +
        "                        DB_R_CATALOGO_MATRICE RCM " +
        "                 WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "                 AND    DC.CODICE_FOTOGRAFIA_TERRENI = CP.CODICE_FOTOGRAFIA_TERRENI " +                     
        "                 AND    CP.ID_CONDUZIONE_DICHIARATA = UP.ID_CONDUZIONE_DICHIARATA " + 
        "                 AND    UP.ID_CATALOGO_MATRICE_SECONDARIO = RCM.ID_CATALOGO_MATRICE " +      
        "                 AND    UP.ID_CATALOGO_MATRICE_SECONDARIO IS NOT NULL " +
        "                 GROUP BY RCM.ID_UTILIZZO) " +
        "SELECT TU.ID_UTILIZZO, " +
        "       TU.CODICE, " +
        "       TU.DESCRIZIONE AS DESC_TIPO_UTI, " +
        "       TU.TIPO, " +
        "       TU.FLAG_SAU, " +
        "       TU.FLAG_ARBOREO, " +
        "       TU.ANNO_INIZIO_VALIDITA, " +
        "       TU.ANNO_FINE_VALIDITA, " +
        "       TU.FLAG_SERRA, " +
        "       TU.FLAG_PASCOLO, " +
        "       TU.FLAG_ALIMENTAZIONE_ANIMALE, " +
        "       TU.FLAG_FORAGGERA, " +
        "       TU.FLAG_UBA_SOSTENIBILE, " +
        "       TU.FLAG_COLTURA_SECONDARIA, " +
        "       TU.FLAG_NIDI, " +
        "       TU.FLAG_PRATO_PASCOLO, " +
        "       TU.CODICE_PSR, " +
        "       TU.FLAG_FRUTTA_GUSCIO, " +
        "       TU.FLAG_TIPO_AVVICENDAMENTO, " +
        "       TU.FLAG_PRINCIPALE " +
        "FROM   DB_TIPO_UTILIZZO TU, " +
        "       TABELLA UP " +
        "WHERE  TU.ID_UTILIZZO = UP.ID_UTILIZZO " +
        "ORDER BY TU.DESCRIZIONE ";
      

      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListUtilizziElencoTerrValidazione method in TipoUtilizzoDAO: "+idDichiarazioneConsistenza+"\n");
    
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idDichiarazioneConsistenza.longValue());
      stmt.setLong(2, idDichiarazioneConsistenza.longValue());
      
      SolmrLogger.debug(this, "Executing getListUtilizziElencoTerrValidazione: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
        tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
        tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
        tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTI"));
        tipoUtilizzoVO.setTipo(rs.getString("TIPO"));
        tipoUtilizzoVO.setFlagSau(rs.getString("FLAG_SAU"));
        tipoUtilizzoVO.setFlagArboreo(rs.getString("FLAG_ARBOREO"));
        tipoUtilizzoVO.setAnnoInizioValidita(rs.getString("ANNO_INIZIO_VALIDITA"));
        tipoUtilizzoVO.setAnnoFineValidita(rs.getString("ANNO_FINE_VALIDITA"));
        tipoUtilizzoVO.setFlagSerra(rs.getString("FLAG_SERRA"));
        tipoUtilizzoVO.setFlagPascolo(rs.getString("FLAG_PASCOLO"));
        tipoUtilizzoVO.setFlagAlimentazioneAnimale(rs.getString("FLAG_ALIMENTAZIONE_ANIMALE"));
        tipoUtilizzoVO.setFlagForaggera(rs.getString("FLAG_FORAGGERA"));
        tipoUtilizzoVO.setFlagUbaSostenibile(rs.getString("FLAG_UBA_SOSTENIBILE"));
        tipoUtilizzoVO.setFlagColturaSecondaria(rs.getString("FLAG_COLTURA_SECONDARIA"));
        tipoUtilizzoVO.setFlagNidi(rs.getString("FLAG_NIDI"));
        tipoUtilizzoVO.setFlagPratoPascolo(rs.getString("FLAG_PRATO_PASCOLO"));
        tipoUtilizzoVO.setCodicePsr(rs.getString("CODICE_PSR"));
        tipoUtilizzoVO.setFlagFruttaGuscio(rs.getString("FLAG_FRUTTA_GUSCIO"));
        tipoUtilizzoVO.setFlagTipoAvvicendamento(rs.getString("FLAG_TIPO_AVVICENDAMENTO"));
        tipoUtilizzoVO.setFlagPrincipale(rs.getString("FLAG_PRINCIPALE"));
        
        elencoTipiUsoSuolo.add(tipoUtilizzoVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "getListUtilizziElencoTerrValidazione in TipoUtilizzoDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) 
    {
      SolmrLogger.error(this, "getListUtilizziElencoTerrValidazione in TipoUtilizzoDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) 
      {
        SolmrLogger.error(this, "getListUtilizziElencoTerrValidazione in TipoUtilizzoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) 
      {
        SolmrLogger.error(this, "getListUtilizziElencoTerrValidazione in TipoUtilizzoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListUtilizziElencoTerrValidazione method in TipoUtilizzoDAO\n");
    return elencoTipiUsoSuolo;
  }
	
	public Vector<TipoUtilizzoVO> getListTipiUsoSuoloByIdDichCons(long idDichiarazioneConsistenza, String[] orderBy) throws DataAccessException 
	{
    SolmrLogger.debug(this, "Invocating getListTipiUsoSuoloByIdDichCons method in TipoUtilizzoDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = new Vector<TipoUtilizzoVO>();

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getListTipiUsoSuoloByIdDichCons method in TipoUtilizzoDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListTipiUsoSuoloByIdDichCons method in TipoUtilizzoDAO and it values: "+conn+"\n");

      String query = 
          "WITH TABELLA AS (SELECT UP.ID_UTILIZZO " +
          "                 FROM   DB_DICHIARAZIONE_CONSISTENZA DC, " +
          "                        DB_CONDUZIONE_DICHIARATA CP, " +
          "                        DB_UTILIZZO_DICHIARATO UP " +
          "                 WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
          "                 AND    DC.CODICE_FOTOGRAFIA_TERRENI = CP.CODICE_FOTOGRAFIA_TERRENI " +                    
          "                 AND    CP.ID_CONDUZIONE_DICHIARATA = UP.ID_CONDUZIONE_DICHIARATA " +                    
          "                 GROUP BY UP.ID_UTILIZZO) " +
          "SELECT TU.ID_UTILIZZO, " +
          "       TU.ID_INDIRIZZO_UTILIZZO AS IND_UTILIZZO, " +
          "       TIU.DESCRIZIONE AS DESC_IND_UTI, " +
          "       TU.CODICE, " +
          "       TU.DESCRIZIONE AS DESC_TIPO_UTI, " +
          "       TU.TIPO, " +
          "       TU.FLAG_SAU, " +
          "       TU.FLAG_ARBOREO, " +
          "       TU.ANNO_INIZIO_VALIDITA, " +
          "       TU.ANNO_FINE_VALIDITA, " +
          "       TU.FLAG_SERRA, " +
          "       TU.FLAG_PASCOLO, " +
          "       TU.FLAG_ALIMENTAZIONE_ANIMALE, " +
          "       TU.FLAG_FORAGGERA, " +
          "       TU.FLAG_UBA_SOSTENIBILE, " +
          "       TU.FLAG_COLTURA_SECONDARIA, " +
          "       TU.FLAG_NIDI, " +
          "       TU.FLAG_PRATO_PASCOLO, " +  
          "       TU.CODICE_PSR, " +
          "       TU.FLAG_FRUTTA_GUSCIO, " +
          "       TU.FLAG_TIPO_AVVICENDAMENTO, " +
          "       TU.FLAG_PRINCIPALE " +
          "FROM   TABELLA UP, " +
          "       DB_TIPO_UTILIZZO TU, " +
          "       DB_TIPO_INDIRIZZO_UTILIZZO TIU " +
          "WHERE  UP.ID_UTILIZZO = TU.ID_UTILIZZO " +
         " AND    TU.ID_INDIRIZZO_UTILIZZO = TIU.ID_INDIRIZZO_UTILIZZO ";
      
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

      SolmrLogger.debug(this, "Value of parameter 1 [ID_DICHIARAZIONE_CONSISTENZA] in getListTipiUsoSuoloByIdDichCons method in TipoUtilizzoDAO: "+idDichiarazioneConsistenza+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ORDER_BY] in getListTipiUsoSuoloByIdDichCons method in TipoUtilizzoDAO: "+orderBy+"\n");

      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idDichiarazioneConsistenza);
      
      SolmrLogger.debug(this, "Executing getListTipiUsoSuoloByIdDichCons: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) {
        TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
        tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
        CodeDescription code = new CodeDescription(Integer.decode(rs.getString("IND_UTILIZZO")), rs.getString("DESC_IND_UTI"));
        tipoUtilizzoVO.setIndirizzoUtilizzo(code);
        tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
        tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTI"));
        tipoUtilizzoVO.setTipo(rs.getString("TIPO"));
        tipoUtilizzoVO.setFlagSau(rs.getString("FLAG_SAU"));
        tipoUtilizzoVO.setFlagArboreo(rs.getString("FLAG_ARBOREO"));
        tipoUtilizzoVO.setAnnoInizioValidita(rs.getString("ANNO_INIZIO_VALIDITA"));
        tipoUtilizzoVO.setAnnoFineValidita(rs.getString("ANNO_FINE_VALIDITA"));
        tipoUtilizzoVO.setFlagSerra(rs.getString("FLAG_SERRA"));
        tipoUtilizzoVO.setFlagPascolo(rs.getString("FLAG_PASCOLO"));
        tipoUtilizzoVO.setFlagAlimentazioneAnimale(rs.getString("FLAG_ALIMENTAZIONE_ANIMALE"));
        tipoUtilizzoVO.setFlagForaggera(rs.getString("FLAG_FORAGGERA"));
        tipoUtilizzoVO.setFlagUbaSostenibile(rs.getString("FLAG_UBA_SOSTENIBILE"));
        tipoUtilizzoVO.setFlagColturaSecondaria(rs.getString("FLAG_COLTURA_SECONDARIA"));
        tipoUtilizzoVO.setFlagNidi(rs.getString("FLAG_NIDI"));
        tipoUtilizzoVO.setFlagPratoPascolo(rs.getString("FLAG_PRATO_PASCOLO"));
        tipoUtilizzoVO.setCodicePsr(rs.getString("CODICE_PSR"));
        tipoUtilizzoVO.setFlagFruttaGuscio(rs.getString("FLAG_FRUTTA_GUSCIO"));
        tipoUtilizzoVO.setFlagTipoAvvicendamento(rs.getString("FLAG_TIPO_AVVICENDAMENTO"));
        tipoUtilizzoVO.setFlagPrincipale(rs.getString("FLAG_PRINCIPALE"));
        elencoTipiUsoSuolo.add(tipoUtilizzoVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getListTipiUsoSuoloByIdDichCons in TipoUtilizzoDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getListTipiUsoSuoloByIdDichCons in TipoUtilizzoDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getListTipiUsoSuoloByIdDichCons in TipoUtilizzoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getListTipiUsoSuoloByIdDichCons in TipoUtilizzoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListTipiUsoSuoloByIdDichCons method in TipoUtilizzoDAO\n");
    return elencoTipiUsoSuolo;
  }
	
	/**
	 * Metodo che mi restituisce l'elenco di tutti gli uso del suolo in relazione all'id_indirizzo_utilizzo
	 * 
	 * @param idIndirizzoUtilizzo
	 * @param onlyActive
	 * @param orderBy
	 * @param colturaSecondaria
	 * @return it.csi.solmr.dto.anag.terreni.TipoUtilizzoVO[]
	 * @throws DataAccessException
	 */
	public TipoUtilizzoVO[] getListTipiUsoSuoloByIdIndirizzoUtilizzo(Long idIndirizzoUtilizzo, boolean onlyActive, 
	    String[] orderBy, String colturaSecondaria) throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating getListTipiUsoSuoloByIdIndirizzoUtilizzo method in TipoUtilizzoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = new Vector<TipoUtilizzoVO>();

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in getListTipiUsoSuoloByIdIndirizzoUtilizzo method in TipoUtilizzoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListTipiUsoSuoloByIdIndirizzoUtilizzo method in TipoUtilizzoDAO and it values: "+conn+"\n");

			String query = " SELECT DISTINCT TU.ID_UTILIZZO, " +
						   "                 TU.ID_INDIRIZZO_UTILIZZO AS IND_UTILIZZO, " +
						   "                 TIU.DESCRIZIONE AS DESC_IND_UTI, " +
						   "                 TU.CODICE, " +
						   "                 TU.DESCRIZIONE AS DESC_TIPO_UTI, " +
						   "                 TU.TIPO, " +
						   "                 TU.FLAG_SAU, " +
						   "                 TU.FLAG_ARBOREO, " +
						   "                 TU.ANNO_INIZIO_VALIDITA, " +
						   "                 TU.ANNO_FINE_VALIDITA, " +
						   "                 TU.FLAG_SERRA, " +
						   "                 TU.FLAG_PASCOLO, " +
						   "                 TU.FLAG_ALIMENTAZIONE_ANIMALE, " +
						   "                 TU.FLAG_FORAGGERA, " +
						   "                 TU.FLAG_UBA_SOSTENIBILE, " +
						   "                 TU.FLAG_COLTURA_SECONDARIA, " +
						   "                 TU.FLAG_NIDI, " +
						   "                 TU.FLAG_PRATO_PASCOLO, " +
						   "                 TU.CODICE_PSR, " +
						   "                 TU.FLAG_FRUTTA_GUSCIO, " +
						   "                 TU.FLAG_TIPO_AVVICENDAMENTO, " +
						   "                 TU.FLAG_PRINCIPALE, " +
               "                 TU.COEFFICIENTE_RIDUZIONE " +
						   " FROM            DB_TIPO_UTILIZZO TU, " +
						   "                 DB_TIPO_INDIRIZZO_UTILIZZO TIU " +
						   " WHERE           TU.ID_INDIRIZZO_UTILIZZO = ? " +
						   " AND             TU.ID_INDIRIZZO_UTILIZZO = TIU.ID_INDIRIZZO_UTILIZZO " +
						   " AND             NOT EXISTS (SELECT * " +
               "                             FROM   DB_TIPO_EFA_TIPO_VARIETA TEV, " +
               "                                    DB_TIPO_EFA TEF, " +
               "                                    DB_R_CATALOGO_MATRICE RCM " +               
               "                             WHERE  TEV.DATA_FINE_VALIDITA IS NULL " +
               "                             AND    RCM.ID_UTILIZZO = TU.ID_UTILIZZO " +
               "                             AND    RCM.DATA_FINE_VALIDITA IS NULL " +
               "                             AND    RCM.ID_CATALOGO_MATRICE = TEV.ID_CATALOGO_MATRICE " +
               "                             AND    TEV.ID_TIPO_EFA = TEF.ID_TIPO_EFA " +
               "                             AND    TEF.DICHIARABILE = 'S' ) ";
			if(Validator.isNotEmpty(colturaSecondaria) && colturaSecondaria.equalsIgnoreCase(SolmrConstants.FLAG_S)) {
				query +=   " AND             TU.FLAG_COLTURA_SECONDARIA = ? ";
			}
			if(onlyActive) {
				query +=   " AND             TU.ANNO_FINE_VALIDITA IS NULL ";
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

			SolmrLogger.debug(this, "Value of parameter 1 [ID_INDIRIZZO_UTILIZZO] in getListTipiUsoSuoloByIdIndirizzoUtilizzo method in TipoUtilizzoDAO: "+idIndirizzoUtilizzo+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ONLY_ACTIVE] in getListTipiUsoSuoloByIdIndirizzoUtilizzo method in TipoUtilizzoDAO: "+onlyActive+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ORDER_BY] in getListTipiUsoSuoloByIdIndirizzoUtilizzo method in TipoUtilizzoDAO: "+orderBy+"\n");
			SolmrLogger.debug(this, "Value of parameter 4 [COLTURA_SECONDARIA] in getListTipiUsoSuoloByIdIndirizzoUtilizzo method in TipoUtilizzoDAO: "+colturaSecondaria+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idIndirizzoUtilizzo.longValue());
			if(Validator.isNotEmpty(colturaSecondaria)) {
				stmt.setString(2, colturaSecondaria);
			}
			
			SolmrLogger.debug(this, "Executing getListTipiUsoSuoloByIdIndirizzoUtilizzo: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
				tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
				CodeDescription code = new CodeDescription(Integer.decode(rs.getString("IND_UTILIZZO")), rs.getString("DESC_IND_UTI"));
				tipoUtilizzoVO.setIndirizzoUtilizzo(code);
				tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
				tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTI"));
				tipoUtilizzoVO.setTipo(rs.getString("TIPO"));
				tipoUtilizzoVO.setFlagSau(rs.getString("FLAG_SAU"));
				tipoUtilizzoVO.setFlagArboreo(rs.getString("FLAG_ARBOREO"));
				tipoUtilizzoVO.setAnnoInizioValidita(rs.getString("ANNO_INIZIO_VALIDITA"));
				tipoUtilizzoVO.setAnnoFineValidita(rs.getString("ANNO_FINE_VALIDITA"));
				tipoUtilizzoVO.setFlagSerra(rs.getString("FLAG_SERRA"));
				tipoUtilizzoVO.setFlagPascolo(rs.getString("FLAG_PASCOLO"));
				tipoUtilizzoVO.setFlagAlimentazioneAnimale(rs.getString("FLAG_ALIMENTAZIONE_ANIMALE"));
				tipoUtilizzoVO.setFlagForaggera(rs.getString("FLAG_FORAGGERA"));
				tipoUtilizzoVO.setFlagUbaSostenibile(rs.getString("FLAG_UBA_SOSTENIBILE"));
				tipoUtilizzoVO.setFlagColturaSecondaria(rs.getString("FLAG_COLTURA_SECONDARIA"));
				tipoUtilizzoVO.setFlagNidi(rs.getString("FLAG_NIDI"));
				tipoUtilizzoVO.setFlagPratoPascolo(rs.getString("FLAG_PRATO_PASCOLO"));
				tipoUtilizzoVO.setCodicePsr(rs.getString("CODICE_PSR"));
				tipoUtilizzoVO.setFlagFruttaGuscio(rs.getString("FLAG_FRUTTA_GUSCIO"));
				tipoUtilizzoVO.setFlagTipoAvvicendamento(rs.getString("FLAG_TIPO_AVVICENDAMENTO"));
				tipoUtilizzoVO.setFlagPrincipale(rs.getString("FLAG_PRINCIPALE"));
				tipoUtilizzoVO.setCoefficienteRiduzione(rs.getBigDecimal("COEFFICIENTE_RIDUZIONE"));
				elencoTipiUsoSuolo.add(tipoUtilizzoVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListTipiUsoSuoloByIdIndirizzoUtilizzo in TipoUtilizzoDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListTipiUsoSuoloByIdIndirizzoUtilizzo in TipoUtilizzoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListTipiUsoSuoloByIdIndirizzoUtilizzo in TipoUtilizzoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListTipiUsoSuoloByIdIndirizzoUtilizzo in TipoUtilizzoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipiUsoSuoloByIdAzienda method in TipoUtilizzoDAO\n");
		if(elencoTipiUsoSuolo.size() == 0) {
			return (TipoUtilizzoVO[])elencoTipiUsoSuolo.toArray(new TipoUtilizzoVO[0]); 
		}
		else {
			return (TipoUtilizzoVO[])elencoTipiUsoSuolo.toArray(new TipoUtilizzoVO[elencoTipiUsoSuolo.size()]);
		}
	}

	
	/**
	 * Metodo che mi restituisce l'elenco di tutti gli uso del suolo in relazione al
	 * codice: se flag principale è uguale a "S" viene restituito un array con un solo
	 * elemento
	 * 
	 * @param codice
	 * @param onlyActive
	 * @param orderBy
	 * @param colturaSecondaria
	 * @param flagPrincipale
	 * @return it.csi.solmr.dto.anag.terreni.TipoUtilizzoVO[]
	 * @throws DataAccessException
	 */
	public TipoUtilizzoVO[] getListTipiUsoSuoloByCodice(
	    String codice, boolean onlyActive, String[] orderBy, String colturaSecondaria, 
	    String flagPrincipale) throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating getListTipiUsoSuoloByCodice method in TipoUtilizzoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = new Vector<TipoUtilizzoVO>();

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in getListTipiUsoSuoloByCodice method in TipoUtilizzoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListTipiUsoSuoloByCodice method in TipoUtilizzoDAO and it values: "+conn+"\n");

			String query = " SELECT DISTINCT TU.ID_UTILIZZO, " +
						   "        TU.ID_INDIRIZZO_UTILIZZO AS IND_UTILIZZO, " +
						   "        TIU.DESCRIZIONE AS DESC_IND_UTI, " +
						   "        TU.CODICE, " +
						   "        TU.DESCRIZIONE AS DESC_TIPO_UTI, " +
						   "        TU.TIPO, " +
						   "        TU.FLAG_SAU, " +
						   "        TU.FLAG_ARBOREO, " +
						   "        TU.ANNO_INIZIO_VALIDITA, " +
						   "        TU.ANNO_FINE_VALIDITA, " +
						   "        TU.FLAG_SERRA, " +
						   "        TU.FLAG_PASCOLO, " +
						   "        TU.FLAG_ALIMENTAZIONE_ANIMALE, " +
						   "        TU.FLAG_FORAGGERA, " +
						   "        TU.FLAG_UBA_SOSTENIBILE, " +
						   "        TU.FLAG_COLTURA_SECONDARIA, " +
						   "        TU.FLAG_NIDI, " +
						   "        TU.FLAG_PRATO_PASCOLO, " +
						   "        TU.CODICE_PSR, " +
						   "        TU.FLAG_FRUTTA_GUSCIO, " +
						   "        TU.FLAG_TIPO_AVVICENDAMENTO, " +
						   "        TU.FLAG_PRINCIPALE, " +
						   "        TU.COEFFICIENTE_RIDUZIONE " +
						   " FROM   DB_TIPO_UTILIZZO TU, " +
						   "        DB_TIPO_INDIRIZZO_UTILIZZO TIU, " +
						   "        DB_R_CATALOGO_MATRICE RCM " +
						   " WHERE  TU.CODICE = ? " +
						   " AND    TU.ID_INDIRIZZO_UTILIZZO = TIU.ID_INDIRIZZO_UTILIZZO " +
						   " AND    TU.ID_UTILIZZO = RCM.ID_UTILIZZO " +
						   " AND    RCM.DATA_FINE_VALIDITA IS NULL " +
          		 " AND    NOT EXISTS (SELECT * " +
               "                    FROM   DB_TIPO_EFA_TIPO_VARIETA TEV, " +
               "                           DB_TIPO_EFA TEF, " +
               "                           DB_R_CATALOGO_MATRICE RCM3 " +
               "                    WHERE  TEV.DATA_FINE_VALIDITA IS NULL " +
               "                    AND    TEV.ID_CATALOGO_MATRICE = RCM3.ID_CATALOGO_MATRICE " +
               "                    AND    RCM3.ID_UTILIZZO = RCM.ID_UTILIZZO " +
               "                    AND    TEV.ID_TIPO_EFA = TEF.ID_TIPO_EFA " +
               "                    AND    TEF.DICHIARABILE = 'S' ) ";
			if(Validator.isNotEmpty(colturaSecondaria) 
			     && colturaSecondaria.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
		  {
				query +=   " AND    TU.FLAG_COLTURA_SECONDARIA = ? ";
			}
			if(onlyActive) 
			{
				query +=   " AND    TU.ANNO_FINE_VALIDITA IS NULL ";
			}
			if(Validator.isNotEmpty(flagPrincipale) 
			    && flagPrincipale.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
			{
				query +=   " AND    TU.FLAG_PRINCIPALE = ? ";
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

			SolmrLogger.debug(this, "Value of parameter 1 [CODICE] in getListTipiUsoSuoloByCodice method in TipoUtilizzoDAO: "+codice+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ONLY_ACTIVE] in getListTipiUsoSuoloByCodice method in TipoUtilizzoDAO: "+onlyActive+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ORDER_BY] in getListTipiUsoSuoloByCodice method in TipoUtilizzoDAO: "+orderBy+"\n");
			SolmrLogger.debug(this, "Value of parameter 4 [COLTURA_SECONDARIA] in getListTipiUsoSuoloByCodice method in TipoUtilizzoDAO: "+colturaSecondaria+"\n");
			SolmrLogger.debug(this, "Value of parameter 5 [FLAG_PRINCIPALE] in getListTipiUsoSuoloByCodice method in TipoUtilizzoDAO: "+flagPrincipale+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setString(1, codice);
			if(Validator.isNotEmpty(colturaSecondaria)) {
				stmt.setString(2, colturaSecondaria);
			}
			if(Validator.isNotEmpty(flagPrincipale)) {
				stmt.setString(2, flagPrincipale);
			}
			
			SolmrLogger.debug(this, "Executing getListTipiUsoSuoloByCodice: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
				tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
				CodeDescription code = new CodeDescription(Integer.decode(rs.getString("IND_UTILIZZO")), rs.getString("DESC_IND_UTI"));
				tipoUtilizzoVO.setIndirizzoUtilizzo(code);
				tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
				tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTI"));
				tipoUtilizzoVO.setTipo(rs.getString("TIPO"));
				tipoUtilizzoVO.setFlagSau(rs.getString("FLAG_SAU"));
				tipoUtilizzoVO.setFlagArboreo(rs.getString("FLAG_ARBOREO"));
				tipoUtilizzoVO.setAnnoInizioValidita(rs.getString("ANNO_INIZIO_VALIDITA"));
				tipoUtilizzoVO.setAnnoFineValidita(rs.getString("ANNO_FINE_VALIDITA"));
				tipoUtilizzoVO.setFlagSerra(rs.getString("FLAG_SERRA"));
				tipoUtilizzoVO.setFlagPascolo(rs.getString("FLAG_PASCOLO"));
				tipoUtilizzoVO.setFlagAlimentazioneAnimale(rs.getString("FLAG_ALIMENTAZIONE_ANIMALE"));
				tipoUtilizzoVO.setFlagForaggera(rs.getString("FLAG_FORAGGERA"));
				tipoUtilizzoVO.setFlagUbaSostenibile(rs.getString("FLAG_UBA_SOSTENIBILE"));
				tipoUtilizzoVO.setFlagColturaSecondaria(rs.getString("FLAG_COLTURA_SECONDARIA"));
				tipoUtilizzoVO.setFlagNidi(rs.getString("FLAG_NIDI"));
				tipoUtilizzoVO.setFlagPratoPascolo(rs.getString("FLAG_PRATO_PASCOLO"));
				tipoUtilizzoVO.setCodicePsr(rs.getString("CODICE_PSR"));
				tipoUtilizzoVO.setFlagFruttaGuscio(rs.getString("FLAG_FRUTTA_GUSCIO"));
				tipoUtilizzoVO.setFlagTipoAvvicendamento(rs.getString("FLAG_TIPO_AVVICENDAMENTO"));
				tipoUtilizzoVO.setFlagPrincipale(rs.getString("FLAG_PRINCIPALE"));
				tipoUtilizzoVO.setCoefficienteRiduzione(rs.getBigDecimal("COEFFICIENTE_RIDUZIONE"));
				elencoTipiUsoSuolo.add(tipoUtilizzoVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListTipiUsoSuoloByCodice in TipoUtilizzoDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListTipiUsoSuoloByCodice in TipoUtilizzoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListTipiUsoSuoloByCodice in TipoUtilizzoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListTipiUsoSuoloByCodice in TipoUtilizzoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipiUsoSuoloByCodice method in TipoUtilizzoDAO\n");
		if(elencoTipiUsoSuolo.size() == 0) {
			return (TipoUtilizzoVO[])elencoTipiUsoSuolo.toArray(new TipoUtilizzoVO[0]); 
		}
		else {
			return (TipoUtilizzoVO[])elencoTipiUsoSuolo.toArray(new TipoUtilizzoVO[elencoTipiUsoSuolo.size()]);
		}
	}
	
	
	
	
	
	public Vector<TipoUtilizzoVO> findListTipiUsoSuoloByIdAzienda(Long idAzienda, String colturaSecondaria) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating findListTipiUsoSuoloByIdAzienda method in TipoUtilizzoDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = new Vector<TipoUtilizzoVO>();

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in findListTipiUsoSuoloByIdAzienda method in TipoUtilizzoDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in findListTipiUsoSuoloByIdAzienda method in TipoUtilizzoDAO and it values: "+conn+"\n");

      String query = "";
      if(Validator.isNotEmpty(colturaSecondaria) 
          && colturaSecondaria.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {
        query +=      
        "WITH TABELLA AS (SELECT RCM2.ID_UTILIZZO " +
        "                 FROM   DB_UTE U, " +
        "                        DB_CONDUZIONE_PARTICELLA CP, " +
        "                        DB_UTILIZZO_PARTICELLA UP, " +
        "                        DB_R_CATALOGO_MATRICE RCM, " +
        "                        DB_R_CATALOGO_MATRICE RCM2 " +
        "                 WHERE  U.ID_AZIENDA = ? " +
        "                 AND    U.ID_UTE = CP.ID_UTE " +
        "                 AND    U.DATA_FINE_ATTIVITA IS NULL " +
        "                 AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA " +
        "                 AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "                 AND    UP.ID_CATALOGO_MATRICE_SECONDARIO = RCM.ID_CATALOGO_MATRICE " +
        "                 AND    UP.ID_CATALOGO_MATRICE_SECONDARIO IS NOT NULL " +
        "                 AND    RCM.ID_UTILIZZO = RCM2.ID_UTILIZZO " +
        "                 AND    RCM2.DATA_FINE_VALIDITA IS NULL " +
        "                 AND NOT EXISTS (SELECT * " +
        "                                 FROM   DB_TIPO_EFA_TIPO_VARIETA TEV, " +
        "                                        DB_TIPO_EFA TEF, " +
        "                                        DB_R_CATALOGO_MATRICE RCM3 " +
        "                                WHERE   TEV.DATA_FINE_VALIDITA IS NULL " +
        "                                AND     TEV.ID_CATALOGO_MATRICE = RCM3.ID_CATALOGO_MATRICE " +
        "                                AND     RCM3.ID_UTILIZZO = RCM.ID_UTILIZZO " +
        "                                AND     TEV.ID_TIPO_EFA = TEF.ID_TIPO_EFA " +
        "                                AND     TEF.DICHIARABILE = 'S') " +
        "                 GROUP BY RCM2.ID_UTILIZZO ) ";
      }
      else
      {
        query +=      
        "WITH TABELLA AS (SELECT RCM2.ID_UTILIZZO " +
        "                 FROM   DB_UTE U, " +
        "                        DB_CONDUZIONE_PARTICELLA CP, " +
        "                        DB_UTILIZZO_PARTICELLA UP, " +
        "                        DB_R_CATALOGO_MATRICE RCM, " +
        "                        DB_R_CATALOGO_MATRICE RCM2 " +
        "                 WHERE  U.ID_AZIENDA = ? " +
        "                 AND    U.ID_UTE = CP.ID_UTE " +
        "                 AND    U.DATA_FINE_ATTIVITA IS NULL " +
        "                 AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA " +
        "                 AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "                 AND    UP.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +
        "                 AND    RCM.ID_UTILIZZO = RCM2.ID_UTILIZZO " +
        "                 AND    RCM2.DATA_FINE_VALIDITA IS NULL " +
        "                 AND NOT EXISTS (SELECT * " +
        "                                 FROM   DB_TIPO_EFA_TIPO_VARIETA TEV, " +
        "                                        DB_TIPO_EFA TEF, " +
        "                                        DB_R_CATALOGO_MATRICE RCM3 " +
        "                                WHERE   TEV.DATA_FINE_VALIDITA IS NULL " +
        "                                AND     TEV.ID_CATALOGO_MATRICE = RCM3.ID_CATALOGO_MATRICE " +
        "                                AND     RCM3.ID_UTILIZZO = RCM.ID_UTILIZZO " +
        "                                AND     TEV.ID_TIPO_EFA = TEF.ID_TIPO_EFA " +
        "                                AND     TEF.DICHIARABILE = 'S') " +
        "                 GROUP BY RCM2.ID_UTILIZZO) ";
      }
      
      query +=                   
        "SELECT TU.ID_UTILIZZO, " +
        "       TU.ID_INDIRIZZO_UTILIZZO AS IND_UTILIZZO, " +
        "       TIU.DESCRIZIONE AS DESC_IND_UTI, " +
        "       TU.CODICE, " +
        "       TU.DESCRIZIONE AS DESC_TIPO_UTI, " + 
        "       TU.TIPO, " +
        "       TU.FLAG_SAU, " + 
        "       TU.FLAG_ARBOREO, " + 
        "       TU.ANNO_INIZIO_VALIDITA, " + 
        "       TU.ANNO_FINE_VALIDITA, " +
        "       TU.FLAG_SERRA, " +
        "       TU.FLAG_PASCOLO, " +
        "       TU.FLAG_ALIMENTAZIONE_ANIMALE, " + 
        "       TU.FLAG_FORAGGERA, " +
        "       TU.FLAG_UBA_SOSTENIBILE, " + 
        "       TU.FLAG_COLTURA_SECONDARIA, " +
        "       TU.FLAG_NIDI, " +
        "       TU.FLAG_PRATO_PASCOLO, " + 
        "       TU.CODICE_PSR, " +
        "       TU.FLAG_FRUTTA_GUSCIO, " + 
        "       TU.FLAG_TIPO_AVVICENDAMENTO, " +
        "       TU.FLAG_PRINCIPALE, " +
        "       TU.COEFFICIENTE_RIDUZIONE " + 
        "FROM   DB_TIPO_UTILIZZO TU, " +
        "       TABELLA UP, " +
        "       DB_TIPO_INDIRIZZO_UTILIZZO TIU " + 
        "WHERE  TU.ID_UTILIZZO = UP.ID_UTILIZZO " +
        "AND    TU.ID_INDIRIZZO_UTILIZZO = TIU.ID_INDIRIZZO_UTILIZZO " +       
        "ORDER BY TU.DESCRIZIONE ";
      
      
      

      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in findListTipiUsoSuoloByIdAzienda method in TipoUtilizzoDAO: "+idAzienda+"\n");
      SolmrLogger.debug(this, "Value of parameter 4 [COLTURA_SECONDARIA] in findListTipiUsoSuoloByIdAzienda method in TipoUtilizzoDAO: "+colturaSecondaria+"\n");

      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda.longValue());
      
      SolmrLogger.debug(this, "Executing findListTipiUsoSuoloByIdAzienda: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
        tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
        CodeDescription code = new CodeDescription(Integer.decode(rs.getString("IND_UTILIZZO")), rs.getString("DESC_IND_UTI"));
        tipoUtilizzoVO.setIndirizzoUtilizzo(code);
        tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
        tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTI"));
        tipoUtilizzoVO.setTipo(rs.getString("TIPO"));
        tipoUtilizzoVO.setFlagSau(rs.getString("FLAG_SAU"));
        tipoUtilizzoVO.setFlagArboreo(rs.getString("FLAG_ARBOREO"));
        tipoUtilizzoVO.setAnnoInizioValidita(rs.getString("ANNO_INIZIO_VALIDITA"));
        tipoUtilizzoVO.setAnnoFineValidita(rs.getString("ANNO_FINE_VALIDITA"));
        tipoUtilizzoVO.setFlagSerra(rs.getString("FLAG_SERRA"));
        tipoUtilizzoVO.setFlagPascolo(rs.getString("FLAG_PASCOLO"));
        tipoUtilizzoVO.setFlagAlimentazioneAnimale(rs.getString("FLAG_ALIMENTAZIONE_ANIMALE"));
        tipoUtilizzoVO.setFlagForaggera(rs.getString("FLAG_FORAGGERA"));
        tipoUtilizzoVO.setFlagUbaSostenibile(rs.getString("FLAG_UBA_SOSTENIBILE"));
        tipoUtilizzoVO.setFlagColturaSecondaria(rs.getString("FLAG_COLTURA_SECONDARIA"));
        tipoUtilizzoVO.setFlagNidi(rs.getString("FLAG_NIDI"));
        tipoUtilizzoVO.setFlagPratoPascolo(rs.getString("FLAG_PRATO_PASCOLO"));
        tipoUtilizzoVO.setCodicePsr(rs.getString("CODICE_PSR"));
        tipoUtilizzoVO.setFlagFruttaGuscio(rs.getString("FLAG_FRUTTA_GUSCIO"));
        tipoUtilizzoVO.setFlagTipoAvvicendamento(rs.getString("FLAG_TIPO_AVVICENDAMENTO"));
        tipoUtilizzoVO.setFlagPrincipale(rs.getString("FLAG_PRINCIPALE"));
        tipoUtilizzoVO.setCoefficienteRiduzione(rs.getBigDecimal("COEFFICIENTE_RIDUZIONE"));
        elencoTipiUsoSuolo.add(tipoUtilizzoVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "findListTipiUsoSuoloByIdAzienda in TipoUtilizzoDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "findListTipiUsoSuoloByIdAzienda in TipoUtilizzoDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "findListTipiUsoSuoloByIdAzienda in TipoUtilizzoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "findListTipiUsoSuoloByIdAzienda in TipoUtilizzoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated findListTipiUsoSuoloByIdAzienda method in TipoUtilizzoDAO\n");
    return elencoTipiUsoSuolo;
  }
	
	
	
	
	/**
   * Metodo che mi restituisce l'elenco degli usi del suolo associati all'azienda:
   * al contrario del metodo getListTipiUsoSuoloByIdAzienda() questo mi restituisce
   * solo l'uso primario o secondario a seconda del valore del parametro colturaSecondaria
   * 
   * @param idAzienda
   * @param onlyActive
   * @param orderBy
   * @param colturaSecondaria
   * @return java.util.Vector
   * @throws DataAccessException
   */
  public Vector<TipoUtilizzoVO> findListTipiUsoSuoloByIdAziendaCess(Long idAzienda, String[] orderBy) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating findListTipiUsoSuoloByIdAziendaCess method in TipoUtilizzoDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = new Vector<TipoUtilizzoVO>();

    try {
      SolmrLogger.debug(this, "Creating db-connection in findListTipiUsoSuoloByIdAziendaCess method in TipoUtilizzoDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in findListTipiUsoSuoloByIdAziendaCess method in TipoUtilizzoDAO and it values: "+conn+"\n");

      String query = " SELECT DISTINCT TU.ID_UTILIZZO, " +
               "                 TU.ID_INDIRIZZO_UTILIZZO AS IND_UTILIZZO, " +
               "                 TIU.DESCRIZIONE AS DESC_IND_UTI, " +
               "                 TU.CODICE, " +
               "                 TU.DESCRIZIONE AS DESC_TIPO_UTI, " +
               "                 TU.TIPO, " +
               "                 TU.FLAG_SAU, " +
               "                 TU.FLAG_ARBOREO, " +
               "                 TU.ANNO_INIZIO_VALIDITA, " +
               "                 TU.ANNO_FINE_VALIDITA, " +
               "                 TU.FLAG_SERRA, " +
               "                 TU.FLAG_PASCOLO, " +
               "                 TU.FLAG_ALIMENTAZIONE_ANIMALE, " +
               "                 TU.FLAG_FORAGGERA, " +
               "                 TU.FLAG_UBA_SOSTENIBILE, " +
               "                 TU.FLAG_COLTURA_SECONDARIA, " +
               "                 TU.FLAG_NIDI, " +
               "                 TU.FLAG_PRATO_PASCOLO, " +
               "                 TU.CODICE_PSR, " +
               "                 TU.FLAG_FRUTTA_GUSCIO, " +
               "                 TU.FLAG_TIPO_AVVICENDAMENTO, " +
               "                 TU.FLAG_PRINCIPALE " +
               " FROM            DB_UTE U, " +
               "                 DB_CONDUZIONE_PARTICELLA CP, " +
               "                 DB_UTILIZZO_PARTICELLA UP, " +
               "                 DB_TIPO_UTILIZZO TU, " +
               "                 DB_TIPO_INDIRIZZO_UTILIZZO TIU " +
               " WHERE           U.ID_AZIENDA = ? " +
               " AND             U.ID_UTE = CP.ID_UTE " +
               " AND             CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA " +
               " AND             UP.ID_UTILIZZO = TU.ID_UTILIZZO "+
               " AND             TU.ANNO_FINE_VALIDITA IS NULL " +
               " AND             TU.ID_INDIRIZZO_UTILIZZO = TIU.ID_INDIRIZZO_UTILIZZO " +
               " UNION " +
               " SELECT DISTINCT TU.ID_UTILIZZO, " +
               "                 TU.ID_INDIRIZZO_UTILIZZO AS IND_UTILIZZO, " +
               "                 TIU.DESCRIZIONE AS DESC_IND_UTI, " +
               "                 TU.CODICE, " +
               "                 TU.DESCRIZIONE AS DESC_TIPO_UTI, " +
               "                 TU.TIPO, " +
               "                 TU.FLAG_SAU, " +
               "                 TU.FLAG_ARBOREO, " +
               "                 TU.ANNO_INIZIO_VALIDITA, " +
               "                 TU.ANNO_FINE_VALIDITA, " +
               "                 TU.FLAG_SERRA, " +
               "                 TU.FLAG_PASCOLO, " +
               "                 TU.FLAG_ALIMENTAZIONE_ANIMALE, " + 
               "                 TU.FLAG_FORAGGERA, " +
               "                 TU.FLAG_UBA_SOSTENIBILE, " + 
               "                 TU.FLAG_COLTURA_SECONDARIA, " +
               "                 TU.FLAG_NIDI, " +
               "                 TU.FLAG_PRATO_PASCOLO, " +
               "                 TU.CODICE_PSR, " +
               "                 TU.FLAG_FRUTTA_GUSCIO, " +
               "                 TU.FLAG_TIPO_AVVICENDAMENTO, " +
               "                 TU.FLAG_PRINCIPALE " +
               " FROM            DB_UTE U, " +
               "                 DB_DICHIARAZIONE_CONSISTENZA DC, " +
               "                 DB_CONDUZIONE_DICHIARATA CD, " +
               "                 DB_UTILIZZO_DICHIARATO UD, " +
               "                 DB_TIPO_UTILIZZO TU, " +
               "                 DB_TIPO_INDIRIZZO_UTILIZZO TIU " +
               " WHERE           U.ID_AZIENDA = ? " +
               " AND             U.ID_AZIENDA = DC.ID_AZIENDA " +
               " AND             U.ID_UTE = CD.ID_UTE " +
               " AND             CD.CODICE_FOTOGRAFIA_TERRENI = DC.CODICE_FOTOGRAFIA_TERRENI " +
               " AND             CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA "+
               " AND             UD.ID_UTILIZZO = TU.ID_UTILIZZO "+
               " AND             TU.ID_INDIRIZZO_UTILIZZO = TIU.ID_INDIRIZZO_UTILIZZO "+
               " AND             TU.ANNO_FINE_VALIDITA IS NULL "+
               " UNION " +
               " SELECT DISTINCT TU.ID_UTILIZZO,TU.ID_INDIRIZZO_UTILIZZO AS IND_UTILIZZO,TIU.DESCRIZIONE AS DESC_IND_UTI, "+
               " TU.CODICE,TU.DESCRIZIONE AS DESC_TIPO_UTI,TU.TIPO,TU.FLAG_SAU,TU.FLAG_ARBOREO,TU.ANNO_INIZIO_VALIDITA, "+
               " TU.ANNO_FINE_VALIDITA,TU.FLAG_SERRA,TU.FLAG_PASCOLO,TU.FLAG_ALIMENTAZIONE_ANIMALE,TU.FLAG_FORAGGERA, "+
               " TU.FLAG_UBA_SOSTENIBILE,TU.FLAG_COLTURA_SECONDARIA,TU.FLAG_NIDI,TU.FLAG_PRATO_PASCOLO,TU.CODICE_PSR, "+
               " TU.FLAG_FRUTTA_GUSCIO,TU.FLAG_TIPO_AVVICENDAMENTO,TU.FLAG_PRINCIPALE "+ 
               " FROM DB_TIPO_UTILIZZO TU,DB_TIPO_INDIRIZZO_UTILIZZO TIU, DB_DOCUMENTO_CORR_PARTICELLA CP, DB_DOCUMENTO D "+
               " WHERE           D.ID_AZIENDA = ? "+
               " AND CP.ID_DOCUMENTO= D.ID_DOCUMENTO "+
               " AND CP.ID_UTILIZZO = TU.ID_UTILIZZO "+  
               " AND TU.ID_INDIRIZZO_UTILIZZO = TIU.ID_INDIRIZZO_UTILIZZO "+  
               " AND TU.ANNO_FINE_VALIDITA IS NULL ";

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

      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in findListTipiUsoSuoloByIdAziendaCess method in TipoUtilizzoDAO: "+idAzienda+"\n");
      SolmrLogger.debug(this, "Value of parameter 3 [ORDER_BY] in findListTipiUsoSuoloByIdAziendaCess method in TipoUtilizzoDAO: "+orderBy+"\n");

      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda.longValue());
      stmt.setLong(2, idAzienda.longValue());
      stmt.setLong(3, idAzienda.longValue());
      
      SolmrLogger.debug(this, "Executing findListTipiUsoSuoloByIdAziendaCess: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) {
        TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
        tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
        CodeDescription code = new CodeDescription(Integer.decode(rs.getString("IND_UTILIZZO")), rs.getString("DESC_IND_UTI"));
        tipoUtilizzoVO.setIndirizzoUtilizzo(code);
        tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
        tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTI"));
        tipoUtilizzoVO.setTipo(rs.getString("TIPO"));
        tipoUtilizzoVO.setFlagSau(rs.getString("FLAG_SAU"));
        tipoUtilizzoVO.setFlagArboreo(rs.getString("FLAG_ARBOREO"));
        tipoUtilizzoVO.setAnnoInizioValidita(rs.getString("ANNO_INIZIO_VALIDITA"));
        tipoUtilizzoVO.setAnnoFineValidita(rs.getString("ANNO_FINE_VALIDITA"));
        tipoUtilizzoVO.setFlagSerra(rs.getString("FLAG_SERRA"));
        tipoUtilizzoVO.setFlagPascolo(rs.getString("FLAG_PASCOLO"));
        tipoUtilizzoVO.setFlagAlimentazioneAnimale(rs.getString("FLAG_ALIMENTAZIONE_ANIMALE"));
        tipoUtilizzoVO.setFlagForaggera(rs.getString("FLAG_FORAGGERA"));
        tipoUtilizzoVO.setFlagUbaSostenibile(rs.getString("FLAG_UBA_SOSTENIBILE"));
        tipoUtilizzoVO.setFlagColturaSecondaria(rs.getString("FLAG_COLTURA_SECONDARIA"));
        tipoUtilizzoVO.setFlagNidi(rs.getString("FLAG_NIDI"));
        tipoUtilizzoVO.setFlagPratoPascolo(rs.getString("FLAG_PRATO_PASCOLO"));
        tipoUtilizzoVO.setCodicePsr(rs.getString("CODICE_PSR"));
        tipoUtilizzoVO.setFlagFruttaGuscio(rs.getString("FLAG_FRUTTA_GUSCIO"));
        tipoUtilizzoVO.setFlagTipoAvvicendamento(rs.getString("FLAG_TIPO_AVVICENDAMENTO"));
        tipoUtilizzoVO.setFlagPrincipale(rs.getString("FLAG_PRINCIPALE"));
        elencoTipiUsoSuolo.add(tipoUtilizzoVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "findListTipiUsoSuoloByIdAziendaCess in TipoUtilizzoDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "findListTipiUsoSuoloByIdAziendaCess in TipoUtilizzoDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "findListTipiUsoSuoloByIdAziendaCess in TipoUtilizzoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "findListTipiUsoSuoloByIdAziendaCess in TipoUtilizzoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated findListTipiUsoSuoloByIdAziendaCess method in TipoUtilizzoDAO\n");
    return elencoTipiUsoSuolo;
  }
	
	
	
	/**
	 * Metodo che si occupa di reperire i dati dalla tabella DB_TIPO_INDIRIZZO_UTILIZZO
	 * 
	 * @param colturaSecondaria
	 * @param orderBy
	 * @param onlyActive
	 * @return it.csi.solmr.dto.CodeDescription[]
	 * @throws DataAccessException
	 */
	public it.csi.solmr.dto.CodeDescription[] getListTipoIndirizzoUtilizzo(String colturaSecondaria, String orderBy, boolean onlyActive) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListTipoIndirizzoUtilizzo method in TipoUtilizzoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<CodeDescription> elencoUtilizzi = new Vector<CodeDescription>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListTipoIndirizzoUtilizzo method in TipoUtilizzoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListTipoIndirizzoUtilizzo method in TipoUtilizzoDAO and it values: "+conn+"\n");

			String query = "";
			if(Validator.isNotEmpty(colturaSecondaria) && colturaSecondaria.equalsIgnoreCase(SolmrConstants.FLAG_S)) {
				query += " SELECT DISTINCT TIU.ID_INDIRIZZO_UTILIZZO, " +
						 "                 TIU.DESCRIZIONE " +
						 " FROM            DB_TIPO_INDIRIZZO_UTILIZZO TIU, " +
						 "                 DB_TIPO_UTILIZZO TU " +
						 " WHERE           TIU.ID_INDIRIZZO_UTILIZZO = TU.ID_INDIRIZZO_UTILIZZO " +
						 " AND             TU.FLAG_COLTURA_SECONDARIA = ? " +
						 " AND             TU.DATA_FINE_VALIDITA IS NULL " +
						 " AND             TIU.DATA_FINE_VALIDITA IS NULL ";
			}
			else {
				query += " SELECT ID_INDIRIZZO_UTILIZZO, " +
				 		 "        DESCRIZIONE " +
				 		 " FROM   DB_TIPO_INDIRIZZO_UTILIZZO ";
			}
			
			if(Validator.isNotEmpty(orderBy)) {
				query += "ORDER BY "+orderBy;
			}
			
			stmt = conn.prepareStatement(query);
			
			if(Validator.isNotEmpty(colturaSecondaria) && colturaSecondaria.equalsIgnoreCase(SolmrConstants.FLAG_S)) {
				SolmrLogger.debug(this, "Value of parameter 1 [FLAG_CLOTURA_SECONDARIA] in getListTipoIndirizzoUtilizzo method in TipoUtilizzoDAO: "+colturaSecondaria+"\n");
				stmt.setString(1, colturaSecondaria);
			}

			SolmrLogger.debug(this, "Executing getListTipoIndirizzoUtilizzo: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				CodeDescription code = new CodeDescription();
				code.setCode(new Integer(rs.getInt("ID_INDIRIZZO_UTILIZZO")));
				code.setDescription(rs.getString("DESCRIZIONE"));
				elencoUtilizzi.add(code);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListTipoIndirizzoUtilizzo in TipoUtilizzoDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListTipoIndirizzoUtilizzo in TipoUtilizzoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListTipoIndirizzoUtilizzo in TipoUtilizzoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListTipoIndirizzoUtilizzo in TipoUtilizzoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipoIndirizzoUtilizzo method in TipoUtilizzoDAO\n");
		if(elencoUtilizzi.size() == 0) { 
			return (CodeDescription[])elencoUtilizzi.toArray(new CodeDescription[0]);
		}
		else {
			return (CodeDescription[])elencoUtilizzi.toArray(new CodeDescription[elencoUtilizzi.size()]);
		}
	}
	
	/**
	 * Metodo che mi permette di recuperare l'elenco dei tipi utilizzo in funzione del tipo
	 * 
	 * @param tipo
	 * @param onlyActive
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.TipoUtilizzoVO[]
	 * @throws DataAccessException
	 */
	public TipoUtilizzoVO[] getListTipiUsoSuoloByTipo(String tipo, boolean onlyActive, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListTipiUsoSuoloByTipo method in TipoUtilizzoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = new Vector<TipoUtilizzoVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListTipiUsoSuoloByTipo method in TipoUtilizzoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListTipiUsoSuoloByTipo method in TipoUtilizzoDAO and it values: "+conn+"\n");

			String query = " SELECT DISTINCT TU.ID_UTILIZZO, " +
						   "                 TU.ID_INDIRIZZO_UTILIZZO AS IND_UTILIZZO, " +
						   "                 TIU.DESCRIZIONE AS DESC_IND_UTI, " +
						   "                 TU.CODICE, " +
						   "                 TU.DESCRIZIONE AS DESC_TIPO_UTI, " +
						   "                 TU.TIPO, " +
						   "                 TU.FLAG_SAU, " +
						   "                 TU.FLAG_ARBOREO, " +
						   "                 TU.ANNO_INIZIO_VALIDITA, " +
						   "                 TU.ANNO_FINE_VALIDITA, " +
						   "                 TU.FLAG_SERRA, " +
						   "                 TU.FLAG_PASCOLO, " +
						   "                 TU.FLAG_ALIMENTAZIONE_ANIMALE, " +
						   "                 TU.FLAG_FORAGGERA, " +
						   "                 TU.FLAG_UBA_SOSTENIBILE, " +
						   "                 TU.FLAG_COLTURA_SECONDARIA, " +
						   "                 TU.FLAG_NIDI, " +
						   "                 TU.FLAG_PRATO_PASCOLO, " +
						   "                 TU.CODICE_PSR, " +
						   "                 TU.FLAG_FRUTTA_GUSCIO, " +
						   "                 TU.FLAG_TIPO_AVVICENDAMENTO, " +
						   "                 TU.FLAG_ORTICOLO, " +
						   "                 TU.FLAG_PRINCIPALE, " +
						   "                 TU.FLAG_AUTUNNO_VERNINI, " +
						   "                 TU.FLAG_UNAR " +
						   " FROM            DB_TIPO_UTILIZZO TU, " +
						   "                 DB_TIPO_INDIRIZZO_UTILIZZO TIU " +
						   " WHERE           TU.TIPO = ? " +
						   " AND             TU.ID_INDIRIZZO_UTILIZZO = TIU.ID_INDIRIZZO_UTILIZZO ";
			if(onlyActive) 
			{
				query +=   " AND             TU.DATA_FINE_VALIDITA IS NULL ";
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

			SolmrLogger.debug(this, "Value of parameter 1 [TIPO] in getListTipiUsoSuoloByTipo method in TipoUtilizzoDAO: "+tipo+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ONLY_ACTIVE] in getListTipiUsoSuoloByTipo method in TipoUtilizzoDAO: "+onlyActive+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ORDER_BY] in getListTipiUsoSuoloByTipo method in TipoUtilizzoDAO: "+orderBy+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setString(1, tipo);
			
			SolmrLogger.debug(this, "Executing getListTipiUsoSuoloByTipo: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
				tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
				CodeDescription code = new CodeDescription(Integer.decode(rs.getString("IND_UTILIZZO")), rs.getString("DESC_IND_UTI"));
				tipoUtilizzoVO.setIndirizzoUtilizzo(code);
				tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
				tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTI"));
				tipoUtilizzoVO.setTipo(rs.getString("TIPO"));
				tipoUtilizzoVO.setFlagSau(rs.getString("FLAG_SAU"));
				tipoUtilizzoVO.setFlagArboreo(rs.getString("FLAG_ARBOREO"));
				tipoUtilizzoVO.setAnnoInizioValidita(rs.getString("ANNO_INIZIO_VALIDITA"));
				tipoUtilizzoVO.setAnnoFineValidita(rs.getString("ANNO_FINE_VALIDITA"));
				tipoUtilizzoVO.setFlagSerra(rs.getString("FLAG_SERRA"));
				tipoUtilizzoVO.setFlagPascolo(rs.getString("FLAG_PASCOLO"));
				tipoUtilizzoVO.setFlagAlimentazioneAnimale(rs.getString("FLAG_ALIMENTAZIONE_ANIMALE"));
				tipoUtilizzoVO.setFlagForaggera(rs.getString("FLAG_FORAGGERA"));
				tipoUtilizzoVO.setFlagUbaSostenibile(rs.getString("FLAG_UBA_SOSTENIBILE"));
				tipoUtilizzoVO.setFlagColturaSecondaria(rs.getString("FLAG_COLTURA_SECONDARIA"));
				tipoUtilizzoVO.setFlagNidi(rs.getString("FLAG_NIDI"));
				tipoUtilizzoVO.setFlagPratoPascolo(rs.getString("FLAG_PRATO_PASCOLO"));
				tipoUtilizzoVO.setCodicePsr(rs.getString("CODICE_PSR"));
				tipoUtilizzoVO.setFlagFruttaGuscio(rs.getString("FLAG_FRUTTA_GUSCIO"));
				tipoUtilizzoVO.setFlagTipoAvvicendamento(rs.getString("FLAG_TIPO_AVVICENDAMENTO"));
				tipoUtilizzoVO.setFlagOrticolo(rs.getString("FLAG_ORTICOLO"));
				tipoUtilizzoVO.setFlagPrincipale(rs.getString("FLAG_PRINCIPALE"));
				tipoUtilizzoVO.setFlagAutunnoVernini(rs.getString("FLAG_AUTUNNO_VERNINI"));
				tipoUtilizzoVO.setFlagUnar(rs.getString("FLAG_UNAR"));
				
				elencoTipiUsoSuolo.add(tipoUtilizzoVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListTipiUsoSuoloByTipo in TipoUtilizzoDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListTipiUsoSuoloByTipo in TipoUtilizzoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListTipiUsoSuoloByTipo in TipoUtilizzoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListTipiUsoSuoloByTipo in TipoUtilizzoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipiUsoSuoloByTipo method in TipoUtilizzoDAO\n");
		if(elencoTipiUsoSuolo.size() == 0) {
			return (TipoUtilizzoVO[])elencoTipiUsoSuolo.toArray(new TipoUtilizzoVO[0]); 
		}
		else {
			return (TipoUtilizzoVO[])elencoTipiUsoSuolo.toArray(new TipoUtilizzoVO[elencoTipiUsoSuolo.size()]);
		}
	}
	
	/**
	 * Metodo per recuperare il tipo utilizzo a partire dalla sua chiave primaria
	 * 
	 * @param idUtilizzo
	 * @return it.csi.solmr.dto.anag.terreni.TipoUtilizzoVO
	 * @throws DataAccessException
	 */
	public TipoUtilizzoVO findTipoUtilizzoByPrimaryKey(Long idUtilizzo) 
	    throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating findTipoUtilizzoByPrimaryKey method in TipoUtilizzoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		TipoUtilizzoVO tipoUtilizzoVO = null;

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in findTipoUtilizzoByPrimaryKey method in TipoUtilizzoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in findTipoUtilizzoByPrimaryKey method in TipoUtilizzoDAO and it values: "+conn+"\n");

			String query = " " +
				"SELECT TU.ID_UTILIZZO, " +
			  "       TU.ID_INDIRIZZO_UTILIZZO AS IND_UTILIZZO, " +
			  "       TIU.DESCRIZIONE AS DESC_IND_UTI, " +
			  "       TU.CODICE, " +
			  "       TU.DESCRIZIONE AS DESC_TIPO_UTI, " +
			  "       TU.TIPO, " +
			  "       TU.FLAG_SAU, " +
			  "       TU.FLAG_ARBOREO, " +
			  "       TU.ANNO_INIZIO_VALIDITA, " +
			  "       TU.ANNO_FINE_VALIDITA, " +
			  "       TU.FLAG_SERRA, " +
			  "       TU.FLAG_PASCOLO, " +
			  "       TU.FLAG_ALIMENTAZIONE_ANIMALE, " +
			  "       TU.FLAG_FORAGGERA, " +
			  "       TU.FLAG_UBA_SOSTENIBILE, " +
			  "       TU.FLAG_COLTURA_SECONDARIA, " +
			  "       TU.FLAG_NIDI, " +	
			  "       TU.FLAG_PRATO_PASCOLO, " +
			  "       TU.CODICE_PSR, " +
			  "       TU.FLAG_FRUTTA_GUSCIO, " +
			  "       TU.FLAG_TIPO_AVVICENDAMENTO, " +
			  "       TU.FLAG_ORTICOLO, " +
			  "       TU.FLAG_PRINCIPALE, " +
			  "       TU.FLAG_AUTUNNO_VERNINI, " +
			  "       TU.COEFFICIENTE_RIDUZIONE " +
			  "FROM   DB_TIPO_UTILIZZO TU, " +
			  "       DB_TIPO_INDIRIZZO_UTILIZZO TIU " +
			  "WHERE  TU.ID_UTILIZZO = ? " +
			  "AND    TU.ID_INDIRIZZO_UTILIZZO = TIU.ID_INDIRIZZO_UTILIZZO ";

			SolmrLogger.debug(this, "Value of parameter 1 [ID_UTILIZZO] in findTipoUtilizzoByPrimaryKey method in TipoUtilizzoDAO: "+idUtilizzo+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idUtilizzo.longValue());
			
			SolmrLogger.debug(this, "Executing findTipoUtilizzoByPrimaryKey: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) 
			{
				tipoUtilizzoVO = new TipoUtilizzoVO();
				tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
				CodeDescription code = new CodeDescription(Integer.decode(rs.getString("IND_UTILIZZO")), rs.getString("DESC_IND_UTI"));
				tipoUtilizzoVO.setIndirizzoUtilizzo(code);
				tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
				tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTI"));
				tipoUtilizzoVO.setTipo(rs.getString("TIPO"));
				tipoUtilizzoVO.setFlagSau(rs.getString("FLAG_SAU"));
				tipoUtilizzoVO.setFlagArboreo(rs.getString("FLAG_ARBOREO"));
				tipoUtilizzoVO.setAnnoInizioValidita(rs.getString("ANNO_INIZIO_VALIDITA"));
				tipoUtilizzoVO.setAnnoFineValidita(rs.getString("ANNO_FINE_VALIDITA"));
				tipoUtilizzoVO.setFlagSerra(rs.getString("FLAG_SERRA"));
				tipoUtilizzoVO.setFlagPascolo(rs.getString("FLAG_PASCOLO"));
				tipoUtilizzoVO.setFlagAlimentazioneAnimale(rs.getString("FLAG_ALIMENTAZIONE_ANIMALE"));
				tipoUtilizzoVO.setFlagForaggera(rs.getString("FLAG_FORAGGERA"));
				tipoUtilizzoVO.setFlagUbaSostenibile(rs.getString("FLAG_UBA_SOSTENIBILE"));
				tipoUtilizzoVO.setFlagColturaSecondaria(rs.getString("FLAG_COLTURA_SECONDARIA"));
				tipoUtilizzoVO.setFlagNidi(rs.getString("FLAG_NIDI"));
				tipoUtilizzoVO.setFlagPratoPascolo(rs.getString("FLAG_PRATO_PASCOLO"));
				tipoUtilizzoVO.setCodicePsr(rs.getString("CODICE_PSR"));
				tipoUtilizzoVO.setFlagFruttaGuscio(rs.getString("FLAG_FRUTTA_GUSCIO"));
				tipoUtilizzoVO.setFlagTipoAvvicendamento(rs.getString("FLAG_TIPO_AVVICENDAMENTO"));
				tipoUtilizzoVO.setFlagOrticolo(rs.getString("FLAG_ORTICOLO"));
				tipoUtilizzoVO.setFlagPrincipale(rs.getString("FLAG_PRINCIPALE"));
				tipoUtilizzoVO.setFlagAutunnoVernini(rs.getString("FLAG_AUTUNNO_VERNINI"));
				tipoUtilizzoVO.setCoefficienteRiduzione(rs.getBigDecimal("COEFFICIENTE_RIDUZIONE"));
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) 
		{
			SolmrLogger.error(this, "findTipoUtilizzoByPrimaryKey in TipoUtilizzoDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) 
		{
			SolmrLogger.error(this, "findTipoUtilizzoByPrimaryKey in TipoUtilizzoDAO - Generic Exception: "+ex+"\n");
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
				SolmrLogger.error(this, "findTipoUtilizzoByPrimaryKey in TipoUtilizzoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) 
			{
				SolmrLogger.error(this, "findTipoUtilizzoByPrimaryKey in TipoUtilizzoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated findTipoUtilizzoByPrimaryKey method in TipoUtilizzoDAO\n");
		return tipoUtilizzoVO;
	}
	
	
	/**
	 * Restituisce gli utilizzi relativi ad una azienda
	 * 
	 * 
	 * @param tipo
	 * @param idAzienda
	 * @return
	 * @throws DataAccessException
	 */
	public TipoUtilizzoVO[] getListTipoUtilizzoByIdAzienda(String tipo, long idAzienda) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getListTipoUtilizzoByIdAzienda method in TipoUtilizzoDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = new Vector<TipoUtilizzoVO>();
  
    try {
      SolmrLogger.debug(this, "Creating db-connection in getListTipoUtilizzoByIdAzienda method in TipoUtilizzoDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListTipoUtilizzoByIdAzienda method in TipoUtilizzoDAO and it values: "+conn+"\n");
  
      String query = 
          "SELECT DISTINCT " +
          "       TU.ID_UTILIZZO, " +
          "       TU.CODICE, " +
          "       TU.DESCRIZIONE " +
          "FROM " + 
          "       DB_STORICO_UNITA_ARBOREA SUA, " +
          "       DB_TIPO_UTILIZZO TU " +
          "WHERE " +
          "       SUA.ID_AZIENDA = ? " +
          "AND    TU.TIPO = ? " +
          "AND    SUA.ID_UTILIZZO = TU.ID_UTILIZZO " +
          "ORDER BY  TU.DESCRIZIONE ";
  
      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListTipoUtilizzoByIdAzienda method in TipoUtilizzoDAO: "+idAzienda+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [TIPO] in getListTipoUtilizzoByIdAzienda method in TipoUtilizzoDAO: "+tipo+"\n");
  
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda);
      stmt.setString(2, tipo);
      
      SolmrLogger.debug(this, "Executing getListTipoUtilizzoByIdAzienda: "+query+"\n");
  
      ResultSet rs = stmt.executeQuery();
  
      while(rs.next()) 
      {
        TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
        tipoUtilizzoVO.setIdUtilizzo(checkLongNull(rs.getString("ID_UTILIZZO")));
        tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
        tipoUtilizzoVO.setDescrizione(rs.getString("DESCRIZIONE"));
        
        elencoTipiUsoSuolo.add(tipoUtilizzoVO);
      }
      
      rs.close();
      stmt.close();
  
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getListTipoUtilizzoByIdAzienda in TipoUtilizzoDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getListTipoUtilizzoByIdAzienda in TipoUtilizzoDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getListTipoUtilizzoByIdAzienda in TipoUtilizzoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getListTipoUtilizzoByIdAzienda in TipoUtilizzoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListTipoUtilizzoByIdAzienda method in TipoUtilizzoDAO\n");
    if(elencoTipiUsoSuolo.size() == 0) {
      return (TipoUtilizzoVO[])elencoTipiUsoSuolo.toArray(new TipoUtilizzoVO[0]);
    }
    else {
      return (TipoUtilizzoVO[])elencoTipiUsoSuolo.toArray(new TipoUtilizzoVO[elencoTipiUsoSuolo.size()]);
    }
  }
	
	public TipoUtilizzoVO[] getListTipoUtilizzoByIdDichiarazioneConsistenza(String tipo, long idDichiarazioneConsistenza) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getListTipoUtilizzoByIdDichiarazioneConsistenza method in TipoUtilizzoDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = new Vector<TipoUtilizzoVO>();
  
    try {
      SolmrLogger.debug(this, "Creating db-connection in getListTipoUtilizzoByIdDichiarazioneConsistenza method in TipoUtilizzoDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListTipoUtilizzoByIdDichiarazioneConsistenza method in TipoUtilizzoDAO and it values: "+conn+"\n");
  
      String query = 
          "SELECT DISTINCT " +
          "       TU.ID_UTILIZZO, " +
          "       TU.CODICE, " +
          "       TU.DESCRIZIONE " +
          "FROM " + 
          "       DB_UNITA_ARBOREA_DICHIARATA UAD, " +
          "       DB_TIPO_UTILIZZO TU, " +
          "       DB_DICHIARAZIONE_CONSISTENZA DC " +
          "WHERE  TU.TIPO = ? " +
          "AND    UAD.ID_UTILIZZO = TU.ID_UTILIZZO " +
          "AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
          "AND    DC.CODICE_FOTOGRAFIA_TERRENI = UAD.CODICE_FOTOGRAFIA_TERRENI " +
          "ORDER BY  TU.DESCRIZIONE ";
  
      SolmrLogger.debug(this, "Value of parameter 1 [TIPO] in getListTipoUtilizzoByIdDichiarazioneConsistenza method in TipoUtilizzoDAO: "+tipo+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ID_DICHIARAZIONE_CONSISTENZA] in getListTipoUtilizzoByIdDichiarazioneConsistenza method in TipoUtilizzoDAO: "+idDichiarazioneConsistenza+"\n");
  
      stmt = conn.prepareStatement(query);
      
      stmt.setString(1, tipo);
      stmt.setLong(2, idDichiarazioneConsistenza);
      
      SolmrLogger.debug(this, "Executing getListTipoUtilizzoByIdDichiarazioneConsistenza: "+query+"\n");
  
      ResultSet rs = stmt.executeQuery();
  
      while(rs.next()) 
      {
        TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
        tipoUtilizzoVO.setIdUtilizzo(checkLongNull(rs.getString("ID_UTILIZZO")));
        tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
        tipoUtilizzoVO.setDescrizione(rs.getString("DESCRIZIONE"));
        
        elencoTipiUsoSuolo.add(tipoUtilizzoVO);
      }
      
      rs.close();
      stmt.close();
  
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getListTipoUtilizzoByIdDichiarazioneConsistenza in TipoUtilizzoDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getListTipoUtilizzoByIdDichiarazioneConsistenza in TipoUtilizzoDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getListTipoUtilizzoByIdDichiarazioneConsistenza in TipoUtilizzoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getListTipoUtilizzoByIdDichiarazioneConsistenza in TipoUtilizzoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListTipoUtilizzoByIdDichiarazioneConsistenza method in TipoUtilizzoDAO\n");
    if(elencoTipiUsoSuolo.size() == 0) {
      return (TipoUtilizzoVO[])elencoTipiUsoSuolo.toArray(new TipoUtilizzoVO[0]);
    }
    else {
      return (TipoUtilizzoVO[])elencoTipiUsoSuolo.toArray(new TipoUtilizzoVO[elencoTipiUsoSuolo.size()]);
    }
  }
	
	
	
}
