package it.csi.solmr.integration.anag;

import java.sql.*;
import java.util.*;
import it.csi.solmr.dto.anag.consistenza.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.exception.*;
import it.csi.solmr.util.*;

/**
 * DAO che si occupa di effettuare le operazioni relative alla tabella DB_TIPO_CONTROLLO
 *
 * <p>Title: SMRGAA</p>
 *
 * <p>Description: Anagrafe delle Imprese Agricole e Agro-Alimentari</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: CSI - PIEMONTE</p>
 *
 * @author Mauro Vocale
 * @version 1.0
 */
public class TipoControlloDAO extends it.csi.solmr.integration.BaseDAO {

	public TipoControlloDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public TipoControlloDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo per recuperare tutti i controlli svolti in una determinata dichiarazione
	 * di consistenza e relativi ad un determinato gruppo controllo
	 * 
	 * @param idDichiarazioneConsistenza
	 * @param idGruppoControllo
	 * @param orderBy[]
	 * @return it.csi.solmr.dto.anag.consistenza.TipoControlloVO[]
	 * @throws DataAccessException
	 */
	public TipoControlloVO[] getListTipoControlloByIdDichiarazioneConsistenzaAndIdGruppoControllo(Long idDicharazioneConsistenza, Long idGruppoControllo, String orderBy[]) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListTipoControlloByIdDichiarazioneConsistenzaAndIdGruppoControllo method in TipoControlloDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoControlloVO> elencoTipoControllo = new Vector<TipoControlloVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListTipoControlloByIdDichiarazioneConsistenzaAndIdGruppoControllo method in TipoControlloDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListTipoControlloByIdDichiarazioneConsistenzaAndIdGruppoControllo method in TipoControlloDAO and it values: "+conn+"\n");

			String query = " SELECT DISTINCT TC.ID_CONTROLLO, " +
						   "                 TC.ID_GRUPPO_CONTROLLO, " +
       					   "                 TC.DESCRIZIONE, " +
       					   "                 TC.OBBLIGATORIO, " +
       					   "                 TC.NOTE, " +
       					   "                 TC.CODICE_CONTROLLO, " +
       					   "                 TC.ORDINAMENTO " +
       					   " FROM   	     DB_TIPO_CONTROLLO TC, " +
       					   "                 DB_DICHIARAZIONE_CONSISTENZA DC, " +
       					   "                 DB_TIPO_MOTIVO_DICHIARAZIONE TMD, " +
       					   "                 DB_TIPO_CONTROLLO_FASE TCF " +
       					   " WHERE  	     DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
       					   " AND    		 DC.ID_MOTIVO_DICHIARAZIONE = TMD.ID_MOTIVO_DICHIARAZIONE " +
       					   " AND    		 TMD.ID_FASE = TCF.ID_FASE " +
       					   " AND     		 TCF.ID_CONTROLLO = TC.ID_CONTROLLO " +
       					   " AND             TC.ID_GRUPPO_CONTROLLO = ? ";

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

			SolmrLogger.debug(this, "Value of parameter 1 [ID_DICHIARAZIONE_CONSISTENZA] in getListTipoControlloByIdDichiarazioneConsistenzaAndIdGruppoControllo method in TipoControlloDAO: "+idDicharazioneConsistenza+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ID_GRUPPO_CONTROLLO] in getListTipoControlloByIdDichiarazioneConsistenzaAndIdGruppoControllo method in TipoControlloDAO: "+idGruppoControllo+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ORDER_BY] in getListTipoControlloByIdDichiarazioneConsistenzaAndIdGruppoControllo method in TipoControlloDAO: "+orderBy+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idDicharazioneConsistenza.longValue());
			stmt.setLong(2, idGruppoControllo.longValue());
			
			SolmrLogger.debug(this, "Executing getListTipoControlloByIdDichiarazioneConsistenzaAndIdGruppoControllo: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				TipoControlloVO tipoControlloVO = new TipoControlloVO();
				tipoControlloVO.setIdControllo(new Long(rs.getLong("ID_CONTROLLO")));
				tipoControlloVO.setIdGruppoControllo(new Long(rs.getLong("ID_GRUPPO_CONTROLLO")));
				tipoControlloVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoControlloVO.setObbligatorio(rs.getString("OBBLIGATORIO"));
				tipoControlloVO.setNote(rs.getString("NOTE"));
				tipoControlloVO.setCodiceControllo(rs.getString("CODICE_CONTROLLO"));
				tipoControlloVO.setOrdinamento(rs.getString("ORDINAMENTO"));
				elencoTipoControllo.add(tipoControlloVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListTipoControlloByIdDichiarazioneConsistenzaAndIdGruppoControllo in TipoControlloDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListTipoControlloByIdDichiarazioneConsistenzaAndIdGruppoControllo in TipoControlloDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListTipoControlloByIdDichiarazioneConsistenzaAndIdGruppoControllo in TipoControlloDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListTipoControlloByIdDichiarazioneConsistenzaAndIdGruppoControllo in TipoControlloDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipoControlloByIdDichiarazioneConsistenzaAndIdGruppoControllo method in TipoControlloDAO\n");
		if(elencoTipoControllo.size() == 0) {
			return (TipoControlloVO[])elencoTipoControllo.toArray(new TipoControlloVO[0]);
		}
		else {
			return (TipoControlloVO[])elencoTipoControllo.toArray(new TipoControlloVO[elencoTipoControllo.size()]);
		}
	}
	
	/**
	 * Metodo per recuperare tutti i tipi controlli relativi ad un determinato gruppo controllo
	 *  
	 * @param idGruppoControllo
	 * @param orderBy
	 * @return
	 * @throws DataAccessException
	 */
	public TipoControlloVO[] getListTipoControlloByIdGruppoControllo(Long idGruppoControllo, String orderBy[]) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListTipoControlloByIdGruppoControllo method in TipoControlloDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoControlloVO> elencoTipoControllo = new Vector<TipoControlloVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListTipoControlloByIdGruppoControllo method in TipoControlloDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListTipoControlloByIdGruppoControllo method in TipoControlloDAO and it values: "+conn+"\n");

			String query = 
			  "SELECT DISTINCT TC.ID_CONTROLLO, " +
				"        TC.ID_GRUPPO_CONTROLLO, " +
       	"        TC.DESCRIZIONE, " +
       	"        TC.OBBLIGATORIO, " +
       	"        TC.NOTE, " +
       	"        TC.CODICE_CONTROLLO, " +
       	"        TC.ORDINAMENTO " +
       	" FROM   DB_TIPO_CONTROLLO TC " +
        " WHERE  TC.ID_GRUPPO_CONTROLLO = ? ";

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

			SolmrLogger.debug(this, "Value of parameter 1 [ID_GRUPPO_CONTROLLO] in getListTipoControlloByIdGruppoControllo method in TipoControlloDAO: "+idGruppoControllo+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ORDER_BY] in getListTipoControlloByIdGruppoControllo method in TipoControlloDAO: "+orderBy+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idGruppoControllo.longValue());
			
			SolmrLogger.debug(this, "Executing getListTipoControlloByIdGruppoControllo: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				TipoControlloVO tipoControlloVO = new TipoControlloVO();
				tipoControlloVO.setIdControllo(new Long(rs.getLong("ID_CONTROLLO")));
				tipoControlloVO.setIdGruppoControllo(new Long(rs.getLong("ID_GRUPPO_CONTROLLO")));
				tipoControlloVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoControlloVO.setObbligatorio(rs.getString("OBBLIGATORIO"));
				tipoControlloVO.setNote(rs.getString("NOTE"));
				tipoControlloVO.setCodiceControllo(rs.getString("CODICE_CONTROLLO"));
				tipoControlloVO.setOrdinamento(rs.getString("ORDINAMENTO"));
				elencoTipoControllo.add(tipoControlloVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListTipoControlloByIdGruppoControllo in TipoControlloDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListTipoControlloByIdGruppoControllo in TipoControlloDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListTipoControlloByIdGruppoControllo in TipoControlloDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListTipoControlloByIdGruppoControllo in TipoControlloDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipoControlloByIdGruppoControllo method in TipoControlloDAO\n");
		if(elencoTipoControllo.size() == 0) {
			return (TipoControlloVO[])elencoTipoControllo.toArray(new TipoControlloVO[0]);
		}
		else {
			return (TipoControlloVO[])elencoTipoControllo.toArray(new TipoControlloVO[elencoTipoControllo.size()]);
		}
	}
	
	/**
	 * 
	 * ritorna i controlli che sono configurati al piano in lavorazione
	 * 
	 * 
	 * @param idGruppoControllo
	 * @param orderBy
	 * @return
	 * @throws DataAccessException
	 */
	public TipoControlloVO[] getListTipoControlloByIdGruppoControlloAttivi(
	    Long idGruppoControllo, String orderBy[]) throws DataAccessException 
	{
    SolmrLogger.debug(this, "Invocating getListTipoControlloByIdGruppoControllo method in TipoControlloDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoControlloVO> elencoTipoControllo = new Vector<TipoControlloVO>();

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getListTipoControlloByIdGruppoControlloAttivi method in TipoControlloDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListTipoControlloByIdGruppoControlloAttivi method in TipoControlloDAO and it values: "+conn+"\n");

      String query = 
        "SELECT DISTINCT TC.ID_CONTROLLO, " +
        "        TC.ID_GRUPPO_CONTROLLO, " +
        "        TC.DESCRIZIONE, " +
        "        TC.OBBLIGATORIO, " +
        "        TC.NOTE, " +
        "        TC.CODICE_CONTROLLO, " +
        "        TC.ORDINAMENTO " +
        " FROM   DB_TIPO_CONTROLLO TC, " +
        "        DB_TIPO_CONTROLLO_FASE TCF " +
        " WHERE  TC.ID_GRUPPO_CONTROLLO = ? " +
        " AND    TC.ID_CONTROLLO = TCF.ID_CONTROLLO " +
        " AND    TC.OBBLIGATORIO = 'S' ";

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

      SolmrLogger.debug(this, "Value of parameter 1 [ID_GRUPPO_CONTROLLO] in getListTipoControlloByIdGruppoControlloAttivi method in TipoControlloDAO: "+idGruppoControllo+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ORDER_BY] in getListTipoControlloByIdGruppoControlloAttivi method in TipoControlloDAO: "+orderBy+"\n");

      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idGruppoControllo.longValue());
      
      SolmrLogger.debug(this, "Executing getListTipoControlloByIdGruppoControlloAttivi: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) {
        TipoControlloVO tipoControlloVO = new TipoControlloVO();
        tipoControlloVO.setIdControllo(new Long(rs.getLong("ID_CONTROLLO")));
        tipoControlloVO.setIdGruppoControllo(new Long(rs.getLong("ID_GRUPPO_CONTROLLO")));
        tipoControlloVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoControlloVO.setObbligatorio(rs.getString("OBBLIGATORIO"));
        tipoControlloVO.setNote(rs.getString("NOTE"));
        tipoControlloVO.setCodiceControllo(rs.getString("CODICE_CONTROLLO"));
        tipoControlloVO.setOrdinamento(rs.getString("ORDINAMENTO"));
        elencoTipoControllo.add(tipoControlloVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getListTipoControlloByIdGruppoControlloAttivi in TipoControlloDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getListTipoControlloByIdGruppoControlloAttivi in TipoControlloDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getListTipoControlloByIdGruppoControlloAttivi in TipoControlloDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getListTipoControlloByIdGruppoControlloAttivi in TipoControlloDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListTipoControlloByIdGruppoControlloAttivi method in TipoControlloDAO\n");
    if(elencoTipoControllo.size() == 0) {
      return (TipoControlloVO[])elencoTipoControllo.toArray(new TipoControlloVO[0]);
    }
    else {
      return (TipoControlloVO[])elencoTipoControllo.toArray(new TipoControlloVO[elencoTipoControllo.size()]);
    }
  }
	
	/**
	 * 
	 * Metodo per recuperare tutti i tipi controlli relativi ad un determinato gruppo controllo 
	 * e all'azienda
	 * 
	 * 
	 * 
	 * @param idGruppoControllo
	 * @param idAzienda
	 * @param orderBy
	 * @return
	 * @throws DataAccessException
	 */
	public TipoControlloVO[] getListTipoControlloForAziendaByIdGruppoControllo(Long idGruppoControllo, Long idAzienda, String orderBy[]) throws DataAccessException {
    SolmrLogger.debug(this, "Invocating getListTipoControlloForAziendaByIdGruppoControllo method in TipoControlloDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoControlloVO> elencoTipoControllo = new Vector<TipoControlloVO>();

    try {
      SolmrLogger.debug(this, "Creating db-connection in getListTipoControlloForAziendaByIdGruppoControllo method in TipoControlloDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListTipoControlloForAziendaByIdGruppoControllo method in TipoControlloDAO and it values: "+conn+"\n");

      String query = " SELECT DISTINCT TC.ID_CONTROLLO, " +
                   "        TC.ID_GRUPPO_CONTROLLO, " +
                   "        TC.DESCRIZIONE, " +
                   "        TC.OBBLIGATORIO, " +
                   "        TC.NOTE, " +
                   "        TC.CODICE_CONTROLLO, " +
                   "        TC.ORDINAMENTO " +
                   " FROM   DB_TIPO_CONTROLLO TC, " +
                   "        DB_DICHIARAZIONE_SEGNALAZIONE DS " +
                   " WHERE  TC.ID_GRUPPO_CONTROLLO = ? " +
                   " AND    TC.ID_CONTROLLO = DS.ID_CONTROLLO " +
                   " AND    DS.ID_AZIENDA = ? ";

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

      SolmrLogger.debug(this, "Value of parameter 1 [ID_GRUPPO_CONTROLLO] in getListTipoControlloForAziendaByIdGruppoControllo method in TipoControlloDAO: "+idGruppoControllo+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ID_GRUPPO_CONTROLLO] in getListTipoControlloForAziendaByIdGruppoControllo method in TipoControlloDAO: "+idAzienda+"\n");
      SolmrLogger.debug(this, "Value of parameter 3 [ORDER_BY] in getListTipoControlloForAziendaByIdGruppoControllo method in TipoControlloDAO: "+orderBy+"\n");

      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idGruppoControllo.longValue());
      stmt.setLong(2, idAzienda.longValue());
      
      SolmrLogger.debug(this, "Executing getListTipoControlloForAziendaByIdGruppoControllo: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) {
        TipoControlloVO tipoControlloVO = new TipoControlloVO();
        tipoControlloVO.setIdControllo(new Long(rs.getLong("ID_CONTROLLO")));
        tipoControlloVO.setIdGruppoControllo(new Long(rs.getLong("ID_GRUPPO_CONTROLLO")));
        tipoControlloVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoControlloVO.setObbligatorio(rs.getString("OBBLIGATORIO"));
        tipoControlloVO.setNote(rs.getString("NOTE"));
        tipoControlloVO.setCodiceControllo(rs.getString("CODICE_CONTROLLO"));
        tipoControlloVO.setOrdinamento(rs.getString("ORDINAMENTO"));
        elencoTipoControllo.add(tipoControlloVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getListTipoControlloForAziendaByIdGruppoControllo in TipoControlloDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getListTipoControlloForAziendaByIdGruppoControllo in TipoControlloDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getListTipoControlloForAziendaByIdGruppoControllo in TipoControlloDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getListTipoControlloForAziendaByIdGruppoControllo in TipoControlloDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListTipoControlloForAziendaByIdGruppoControllo method in TipoControlloDAO\n");
    if(elencoTipoControllo.size() == 0) {
      return (TipoControlloVO[])elencoTipoControllo.toArray(new TipoControlloVO[0]);
    }
    else {
      return (TipoControlloVO[])elencoTipoControllo.toArray(new TipoControlloVO[elencoTipoControllo.size()]);
    }
  }
	
	public TipoControlloVO[] getListTipoControlloForAziendaByIdGruppoControlloAndDichCons(Long idGruppoControllo, Long idDichiarazioneConsistenza, String orderBy[]) throws DataAccessException {
    SolmrLogger.debug(this, "Invocating getListTipoControlloForAziendaByIdGruppoControlloAndDichCons method in TipoControlloDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoControlloVO> elencoTipoControllo = new Vector<TipoControlloVO>();

    try {
      SolmrLogger.debug(this, "Creating db-connection in getListTipoControlloForAziendaByIdGruppoControlloAndDichCons method in TipoControlloDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListTipoControlloForAziendaByIdGruppoControlloAndDichCons method in TipoControlloDAO and it values: "+conn+"\n");

      String query = " SELECT DISTINCT TC.ID_CONTROLLO, " +
                   "        TC.ID_GRUPPO_CONTROLLO, " +
                   "        TC.DESCRIZIONE, " +
                   "        TC.OBBLIGATORIO, " +
                   "        TC.NOTE, " +
                   "        TC.CODICE_CONTROLLO, " +
                   "        TC.ORDINAMENTO " +
                   " FROM   DB_TIPO_CONTROLLO TC, " +
                   "        DB_DICHIARAZIONE_SEGNALAZIONE DS " +
                   " WHERE  TC.ID_GRUPPO_CONTROLLO = ? " +
                   " AND    TC.ID_CONTROLLO = DS.ID_CONTROLLO " +
                   " AND    DS.ID_DICHIARAZIONE_CONSISTENZA = ? ";

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

      SolmrLogger.debug(this, "Value of parameter 1 [ID_GRUPPO_CONTROLLO] in getListTipoControlloForAziendaByIdGruppoControlloAndDichCons method in TipoControlloDAO: "+idGruppoControllo+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ID_DICHIARAZIONE_CONSISTENZA] in getListTipoControlloForAziendaByIdGruppoControlloAndDichCons method in TipoControlloDAO: "+idDichiarazioneConsistenza+"\n");
      SolmrLogger.debug(this, "Value of parameter 3 [ORDER_BY] in getListTipoControlloForAziendaByIdGruppoControlloAndDichCons method in TipoControlloDAO: "+orderBy+"\n");

      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idGruppoControllo.longValue());
      stmt.setLong(2, idDichiarazioneConsistenza.longValue());
      
      SolmrLogger.debug(this, "Executing getListTipoControlloForAziendaByIdGruppoControlloAndDichCons: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) {
        TipoControlloVO tipoControlloVO = new TipoControlloVO();
        tipoControlloVO.setIdControllo(new Long(rs.getLong("ID_CONTROLLO")));
        tipoControlloVO.setIdGruppoControllo(new Long(rs.getLong("ID_GRUPPO_CONTROLLO")));
        tipoControlloVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoControlloVO.setObbligatorio(rs.getString("OBBLIGATORIO"));
        tipoControlloVO.setNote(rs.getString("NOTE"));
        tipoControlloVO.setCodiceControllo(rs.getString("CODICE_CONTROLLO"));
        tipoControlloVO.setOrdinamento(rs.getString("ORDINAMENTO"));
        elencoTipoControllo.add(tipoControlloVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getListTipoControlloForAziendaByIdGruppoControlloAndDichCons in TipoControlloDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getListTipoControlloForAziendaByIdGruppoControlloAndDichCons in TipoControlloDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getListTipoControlloForAziendaByIdGruppoControlloAndDichCons in TipoControlloDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getListTipoControlloForAziendaByIdGruppoControlloAndDichCons in TipoControlloDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListTipoControlloForAziendaByIdGruppoControlloAndDichCons method in TipoControlloDAO\n");
    if(elencoTipoControllo.size() == 0) {
      return (TipoControlloVO[])elencoTipoControllo.toArray(new TipoControlloVO[0]);
    }
    else {
      return (TipoControlloVO[])elencoTipoControllo.toArray(new TipoControlloVO[elencoTipoControllo.size()]);
    }
  }
}
