package it.csi.solmr.integration.anag;

import it.csi.smranag.smrgaa.dto.PlSqlCodeDescription;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.anag.attestazioni.AttestazioneAziendaVO;
import it.csi.solmr.dto.anag.attestazioni.AttestazioneDichiarataVO;
import it.csi.solmr.dto.anag.attestazioni.ParametriAttAziendaVO;
import it.csi.solmr.dto.anag.attestazioni.ParametriAttDichiarataVO;
import it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO;
import it.csi.solmr.dto.anag.attestazioni.TipoParametriAttestazioneVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Vector;

public class TipoAttestazioneDAO extends it.csi.solmr.integration.BaseDAO {


	public TipoAttestazioneDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public TipoAttestazioneDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo utilizzato per estrarre le attestazioni relative al piano di riferimento attuale
	 *
	 * @param idAzienda
	 * @param flagVideo
	 * @param flagStampa
	 * @param orderBy
	 * @param codiceAttestazione
	 * @param voceMenu
	 * @return it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO[]
	 * @throws DataAccessException
	 */
	public TipoAttestazioneVO[] getListTipoAttestazioneAlPianoAttuale(Long idAzienda, 
	    String[] orderBy, String voceMenu) throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating getListTipoAttestazioneAlPianoAttuale method in TipoAttestazioneDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoAttestazioneVO> elencoTipoAttestazione = new Vector<TipoAttestazioneVO>();

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in getListTipoAttestazioneAlPianoAttuale method in TipoAttestazioneDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListTipoAttestazioneAlPianoAttuale method in TipoAttestazioneDAO and it values: "+conn+"\n");

			String query = 
			  "SELECT TA.ID_ATTESTAZIONE, " +
		    "       TA.CODICE_ATTESTAZIONE, " +
		    "       TA.DESCRIZIONE, " +
		    "       TA.DATA_INIZIO_VALIDITA, " +
		    "       TA.DATA_FINE_VALIDITA, " +
		    "       TA.FLAG_VIDEO, " +
		    "       TA.FLAG_STAMPA, " +
		    "       TA.TIPO_RIGA, " +
		    "       TA.NUMERO_COLONNE_RIGA, " +
		    "       TA.ID_ATTESTAZIONE_PADRE, " +
		    "       TA.TIPO_CARATTERE, " +
		    "       TA.GRUPPO, " +
		    "       TA.ORDINAMENTO, " +
		    "       TA.OBBLIGATORIO, " +
		    "       TA.DISABILITATO, " +
		    "       TA.SELEZIONA_VIDEO, " +
		    "       TA.ATTESTAZIONI_CORRELATE, " +
		    "       TA.VOCE_MENU, " +
		    "       AA.ID_ATTESTAZIONE_AZIENDA, " +
		    "       PAA.ID_PARAMETRI_ATT_AZIENDA, " +
		    "       PAA.PARAMETRO_1, " +
		    "       PAA.PARAMETRO_2, " +
		    "       PAA.PARAMETRO_3, " +
		    "       PAA.PARAMETRO_4, " +
		    "       PAA.PARAMETRO_5, " +
		    "       TPA.ID_PARAMETRI_ATTESTAZIONE, " +
		    "       TPA.PARAMETRO_1 AS TIPO_PAR_1, " +
		    "       TPA.PARAMETRO_2 AS TIPO_PAR_2, " +
		    "       TPA.PARAMETRO_3 AS TIPO_PAR_3, " +
		    "       TPA.PARAMETRO_4 AS TIPO_PAR_4, " +
		    "       TPA.PARAMETRO_5 AS TIPO_PAR_5, " +
		    "       TPA.OBBLIGATORIO_1, " +
		    "       TPA.OBBLIGATORIO_2, " +
		    "       TPA.OBBLIGATORIO_3, " +
		    "       TPA.OBBLIGATORIO_4, " +
		    "       TPA.OBBLIGATORIO_5 " +
		    "FROM   DB_TIPO_ATTESTAZIONE TA, " +
		    "       DB_ATTESTAZIONE_AZIENDA AA, " +
		    "       DB_PARAMETRI_ATT_AZIENDA PAA, " +
		    "       DB_TIPO_PARAMETRI_ATTESTAZIONE TPA " +
		    "WHERE  TA.DATA_INIZIO_VALIDITA < SYSDATE " +
		    "AND    NVL(TA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) > SYSDATE " +
		    "AND    TA.ID_ATTESTAZIONE = AA.ID_ATTESTAZIONE(+) " +
		    "AND    AA.ID_AZIENDA (+) = ? " +
		    "AND    AA.ID_ATTESTAZIONE_AZIENDA = PAA.ID_ATTESTAZIONE_AZIENDA(+) " +
		    "AND    TA.ID_ATTESTAZIONE = TPA.ID_ATTESTAZIONE(+) " +
        "AND    TA.FLAG_VIDEO = 'S' " +
        "AND    TA.VOCE_MENU = ? ";
      

			if(orderBy != null && orderBy.length > 0) 
			{
				String criterio = "";
				for(int i = 0; i < orderBy.length; i++) 
				{
					if(i == 0) 
					{
						criterio = (String)orderBy[i];
					}
					else 
					{
						criterio += ", "+(String)orderBy[i];
					}
				}
				query+= "ORDER BY "+criterio;
			}

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListTipoAttestazioneAlPianoAttuale method in TipoAttestazioneDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 4 [ORDER_BY] in getListTipoAttestazioneAlPianoAttuale method in TipoAttestazioneDAO: "+orderBy+"\n");
      SolmrLogger.debug(this, "Value of parameter 6 [VOCE_MENU] in getListTipoAttestazioneAlPianoAttuale method in TipoAttestazioneDAO: "+voceMenu+"\n");


			stmt = conn.prepareStatement(query);

			int indice = 0;
			stmt.setLong(++indice, idAzienda.longValue());
			stmt.setString(++indice, voceMenu);

			SolmrLogger.debug(this, "Executing getListTipoAttestazioneAlPianoAttuale: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			Long idAttestazioneConfronto = null;
			while(rs.next()) {
				TipoAttestazioneVO tipoAttestazioneVO = new TipoAttestazioneVO();
				tipoAttestazioneVO.setIdAttestazione(new Long(rs.getLong("ID_ATTESTAZIONE")));
				tipoAttestazioneVO.setCodiceAttestazione(rs.getString("CODICE_ATTESTAZIONE"));
				tipoAttestazioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoAttestazioneVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				tipoAttestazioneVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				tipoAttestazioneVO.setFlagVideo(rs.getString("FLAG_VIDEO"));
				tipoAttestazioneVO.setFlagStampa(rs.getString("FLAG_STAMPA"));
				tipoAttestazioneVO.setTipoRiga(rs.getString("TIPO_RIGA"));
				tipoAttestazioneVO.setNumeroColonneRiga(rs.getString("NUMERO_COLONNE_RIGA"));
				tipoAttestazioneVO.setIdAttestazionePadre(new Long(rs.getLong("ID_ATTESTAZIONE_PADRE")));
				tipoAttestazioneVO.setTipoCarattere(rs.getString("TIPO_CARATTERE"));
				tipoAttestazioneVO.setGruppo(rs.getString("GRUPPO"));
				tipoAttestazioneVO.setOrdinamento(rs.getString("ORDINAMENTO"));
				tipoAttestazioneVO.setObbligatorio(rs.getString("OBBLIGATORIO"));
				tipoAttestazioneVO.setDisabilitato(rs.getString("DISABILITATO"));
				tipoAttestazioneVO.setSelezionaVideo(rs.getString("SELEZIONA_VIDEO"));
				tipoAttestazioneVO.setAttestazioniCorrelate(rs.getString("ATTESTAZIONI_CORRELATE"));
				tipoAttestazioneVO.setVoceMenu(rs.getString("VOCE_MENU"));
				if(Validator.isNotEmpty(rs.getString("ID_ATTESTAZIONE_AZIENDA"))) {
					if(tipoAttestazioneVO.getAttestazioniCorrelate().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
						idAttestazioneConfronto = tipoAttestazioneVO.getIdAttestazione();
					}
					tipoAttestazioneVO.setAttestazioneAzienda(true);
				}
				else {
					if(idAttestazioneConfronto != null && idAttestazioneConfronto.compareTo(tipoAttestazioneVO.getIdAttestazionePadre()) == 0) {
						tipoAttestazioneVO.setAttestazioneAzienda(true);
					}
				}
				if(Validator.isNotEmpty(rs.getString("ID_PARAMETRI_ATT_AZIENDA"))) {
					ParametriAttAziendaVO parametriAttAziendaVO = new ParametriAttAziendaVO();
					parametriAttAziendaVO.setIdParametriAttAzienda(new Long(rs.getLong("ID_PARAMETRI_ATT_AZIENDA")));
					parametriAttAziendaVO.setParametro1(rs.getString("PARAMETRO_1"));
					parametriAttAziendaVO.setParametro2(rs.getString("PARAMETRO_2"));
					parametriAttAziendaVO.setParametro3(rs.getString("PARAMETRO_3"));
					parametriAttAziendaVO.setParametro4(rs.getString("PARAMETRO_4"));
					parametriAttAziendaVO.setParametro5(rs.getString("PARAMETRO_5"));
					tipoAttestazioneVO.setParametriAttAziendaVO(parametriAttAziendaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_PARAMETRI_ATTESTAZIONE"))) {
					TipoParametriAttestazioneVO tipoParametriAttestazioneVO = new TipoParametriAttestazioneVO();
					tipoParametriAttestazioneVO.setIdParametriAttestazione(new Long(rs.getLong("ID_PARAMETRI_ATTESTAZIONE")));
					tipoParametriAttestazioneVO.setIdAttestazione(new Long(rs.getLong("ID_ATTESTAZIONE")));
					tipoParametriAttestazioneVO.setParametro1(rs.getString("TIPO_PAR_1"));
					tipoParametriAttestazioneVO.setParametro2(rs.getString("TIPO_PAR_2"));
					tipoParametriAttestazioneVO.setParametro3(rs.getString("TIPO_PAR_3"));
					tipoParametriAttestazioneVO.setParametro4(rs.getString("TIPO_PAR_4"));
					tipoParametriAttestazioneVO.setParametro5(rs.getString("TIPO_PAR_5"));
					tipoParametriAttestazioneVO.setObbligatorio1(rs.getString("OBBLIGATORIO_1"));
					tipoParametriAttestazioneVO.setObbligatorio2(rs.getString("OBBLIGATORIO_2"));
					tipoParametriAttestazioneVO.setObbligatorio3(rs.getString("OBBLIGATORIO_3"));
					tipoParametriAttestazioneVO.setObbligatorio4(rs.getString("OBBLIGATORIO_4"));
					tipoParametriAttestazioneVO.setObbligatorio5(rs.getString("OBBLIGATORIO_5"));
					tipoAttestazioneVO.setTipoParametriAttestazioneVO(tipoParametriAttestazioneVO);
				}
				elencoTipoAttestazione.add(tipoAttestazioneVO);
			}

			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListTipoAttestazioneAlPianoAttuale in TipoAttestazioneDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListTipoAttestazioneAlPianoAttuale in TipoAttestazioneDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListTipoAttestazioneAlPianoAttuale in TipoAttestazioneDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListTipoAttestazioneAlPianoAttuale in TipoAttestazioneDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipoAttestazioneAlPianoAttuale method in TipoAttestazioneDAO\n");
		if(elencoTipoAttestazione.size() == 0) {
			return (TipoAttestazioneVO[])elencoTipoAttestazione.toArray(new TipoAttestazioneVO[0]);
		}
		else {
			return (TipoAttestazioneVO[])elencoTipoAttestazione.toArray(new TipoAttestazioneVO[elencoTipoAttestazione.size()]);
		}
	}



	/**
	 * Metodo utilizzato per estrarre l'elenco delle attestazioni/allegati validi
	 * al piano di lavoro corrente
	 *
	 * @param flagVideo
	 * @param flagStampa
	 * @param orderBy
	 * @param codiceAttestazione
	 * @param voceMenu
	 * @return it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO[]
	 * @throws DataAccessException
	 */
	public TipoAttestazioneVO[] getElencoTipoAttestazioneAlPianoAttuale(boolean flagVideo, boolean flagStampa, String[] orderBy, String codiceAttestazione, String voceMenu) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getElencoTipoAttestazioneAlPianoAttuale method in TipoAttestazioneDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoAttestazioneVO> elencoTipoAttestazione = new Vector<TipoAttestazioneVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getElencoTipoAttestazioneAlPianoAttuale method in TipoAttestazioneDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getElencoTipoAttestazioneAlPianoAttuale method in TipoAttestazioneDAO and it values: "+conn+"\n");

			String query = " SELECT TA.ID_ATTESTAZIONE, " +
						   "        TA.CODICE_ATTESTAZIONE, " +
						   "        TA.DESCRIZIONE, " +
						   "        TA.DATA_INIZIO_VALIDITA, " +
						   "        TA.DATA_FINE_VALIDITA, " +
						   "        TA.FLAG_VIDEO, " +
						   "        TA.FLAG_STAMPA, " +
						   "        TA.TIPO_RIGA, " +
						   "        TA.NUMERO_COLONNE_RIGA, " +
						   "        TA.ID_ATTESTAZIONE_PADRE, " +
						   "        TA.TIPO_CARATTERE, " +
						   "        TA.GRUPPO, " +
						   "        TA.ORDINAMENTO, " +
						   "        TA.OBBLIGATORIO, " +
						   "        TA.DISABILITATO, " +
						   "        TA.SELEZIONA_VIDEO, " +
						   "        TA.ATTESTAZIONI_CORRELATE, " +
						   "        TA.VOCE_MENU, " +
						   "        TPA.ID_PARAMETRI_ATTESTAZIONE, " +
						   "        TPA.PARAMETRO_1 AS TIPO_PAR_1, " +
						   "        TPA.PARAMETRO_2 AS TIPO_PAR_2, " +
						   "        TPA.PARAMETRO_3 AS TIPO_PAR_3, " +
						   "        TPA.PARAMETRO_4 AS TIPO_PAR_4, " +
						   "        TPA.PARAMETRO_5 AS TIPO_PAR_5, " +
						   "        TPA.OBBLIGATORIO_1, " +
						   "        TPA.OBBLIGATORIO_2, " +
						   "        TPA.OBBLIGATORIO_3, " +
						   "        TPA.OBBLIGATORIO_4, " +
						   "        TPA.OBBLIGATORIO_5 " +
						   " FROM   DB_TIPO_ATTESTAZIONE TA, " +
						   "        DB_TIPO_PARAMETRI_ATTESTAZIONE TPA " +
						   " WHERE  TA.DATA_FINE_VALIDITA IS NULL " +
						   " AND    TA.ID_ATTESTAZIONE = TPA.ID_ATTESTAZIONE(+) ";
			if(flagVideo) {
				query +=   " AND    TA.FLAG_VIDEO = ? ";
			}
			if(flagStampa) {
				query +=   " AND    TA.FLAG_STAMPA = ? ";
			}
            if(codiceAttestazione != null) {
            	query += "   AND    TA.CODICE_ATTESTAZIONE = ? ";
            }
            if(Validator.isNotEmpty(voceMenu)) {
            	query += "   AND    TA.VOCE_MENU = ? ";
            }

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
				query+= "ORDER BY "+criterio;
			}

			SolmrLogger.debug(this, "Value of parameter 1 [FLAG_VIDEO] in getElencoTipoAttestazioneAlPianoAttuale method in TipoAttestazioneDAO: "+flagVideo+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [FLAG_STAMPA] in getElencoTipoAttestazioneAlPianoAttuale method in TipoAttestazioneDAO: "+flagStampa+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ORDER_BY] in getElencoTipoAttestazioneAlPianoAttuale method in TipoAttestazioneDAO: "+orderBy+"\n");
            SolmrLogger.debug(this, "Value of parameter 4 [CODICE_ATTESTAZIONE] in getElencoTipoAttestazioneAlPianoAttuale method in TipoAttestazioneDAO: "+codiceAttestazione+"\n");
            SolmrLogger.debug(this, "Value of parameter 5 [VOCE_MENU] in getElencoTipoAttestazioneAlPianoAttuale method in TipoAttestazioneDAO: "+voceMenu+"\n");


			stmt = conn.prepareStatement(query);

			int indice = 1;
			if(flagVideo) {
				stmt.setString(indice, SolmrConstants.FLAG_S);
				indice++;
			}
			if(flagStampa) {
				stmt.setString(indice, SolmrConstants.FLAG_S);
				indice++;
			}
            if(codiceAttestazione != null) {
            	stmt.setString(indice, codiceAttestazione);
                indice++;
            }
            if(Validator.isNotEmpty(voceMenu)) {
            	stmt.setString(indice, voceMenu);
                indice++;
            }


			SolmrLogger.debug(this, "Executing getElencoTipoAttestazioneAlPianoAttuale: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				TipoAttestazioneVO tipoAttestazioneVO = new TipoAttestazioneVO();
				tipoAttestazioneVO.setIdAttestazione(new Long(rs.getLong("ID_ATTESTAZIONE")));
				tipoAttestazioneVO.setCodiceAttestazione(rs.getString("CODICE_ATTESTAZIONE"));
				tipoAttestazioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoAttestazioneVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				tipoAttestazioneVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				tipoAttestazioneVO.setFlagVideo(rs.getString("FLAG_VIDEO"));
				tipoAttestazioneVO.setFlagStampa(rs.getString("FLAG_STAMPA"));
				tipoAttestazioneVO.setTipoRiga(rs.getString("TIPO_RIGA"));
				tipoAttestazioneVO.setNumeroColonneRiga(rs.getString("NUMERO_COLONNE_RIGA"));
				tipoAttestazioneVO.setIdAttestazionePadre(new Long(rs.getLong("ID_ATTESTAZIONE_PADRE")));
				tipoAttestazioneVO.setTipoCarattere(rs.getString("TIPO_CARATTERE"));
				tipoAttestazioneVO.setGruppo(rs.getString("GRUPPO"));
				tipoAttestazioneVO.setOrdinamento(rs.getString("ORDINAMENTO"));
				tipoAttestazioneVO.setObbligatorio(rs.getString("OBBLIGATORIO"));
				tipoAttestazioneVO.setDisabilitato(rs.getString("DISABILITATO"));
				tipoAttestazioneVO.setSelezionaVideo(rs.getString("SELEZIONA_VIDEO"));
				tipoAttestazioneVO.setAttestazioniCorrelate(rs.getString("ATTESTAZIONI_CORRELATE"));
				tipoAttestazioneVO.setVoceMenu(rs.getString("VOCE_MENU"));
				tipoAttestazioneVO.setAttestazioneAzienda(false);
				if(Validator.isNotEmpty(rs.getString("ID_PARAMETRI_ATTESTAZIONE"))) {
					TipoParametriAttestazioneVO tipoParametriAttestazioneVO = new TipoParametriAttestazioneVO();
					tipoParametriAttestazioneVO.setIdParametriAttestazione(new Long(rs.getLong("ID_PARAMETRI_ATTESTAZIONE")));
					tipoParametriAttestazioneVO.setIdAttestazione(new Long(rs.getLong("ID_ATTESTAZIONE")));
					tipoParametriAttestazioneVO.setParametro1(rs.getString("TIPO_PAR_1"));
					tipoParametriAttestazioneVO.setParametro2(rs.getString("TIPO_PAR_2"));
					tipoParametriAttestazioneVO.setParametro3(rs.getString("TIPO_PAR_3"));
					tipoParametriAttestazioneVO.setParametro4(rs.getString("TIPO_PAR_4"));
					tipoParametriAttestazioneVO.setParametro5(rs.getString("TIPO_PAR_5"));
					tipoParametriAttestazioneVO.setObbligatorio1(rs.getString("OBBLIGATORIO_1"));
					tipoParametriAttestazioneVO.setObbligatorio2(rs.getString("OBBLIGATORIO_2"));
					tipoParametriAttestazioneVO.setObbligatorio3(rs.getString("OBBLIGATORIO_3"));
					tipoParametriAttestazioneVO.setObbligatorio4(rs.getString("OBBLIGATORIO_4"));
					tipoParametriAttestazioneVO.setObbligatorio5(rs.getString("OBBLIGATORIO_5"));
					tipoAttestazioneVO.setTipoParametriAttestazioneVO(tipoParametriAttestazioneVO);
				}
				elencoTipoAttestazione.add(tipoAttestazioneVO);
			}

			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getElencoTipoAttestazioneAlPianoAttuale in TipoAttestazioneDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getElencoTipoAttestazioneAlPianoAttuale in TipoAttestazioneDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getElencoTipoAttestazioneAlPianoAttuale in TipoAttestazioneDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getElencoTipoAttestazioneAlPianoAttuale in TipoAttestazioneDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getElencoTipoAttestazioneAlPianoAttuale method in TipoAttestazioneDAO\n");
		if(elencoTipoAttestazione.size() == 0) {
			return (TipoAttestazioneVO[])elencoTipoAttestazione.toArray(new TipoAttestazioneVO[0]);
		}
		else {
			return (TipoAttestazioneVO[])elencoTipoAttestazione.toArray(new TipoAttestazioneVO[elencoTipoAttestazione.size()]);
		}
	}

	/**
	 * Metodo utilizzato per estrarre le attestazioni relative ad una determinata dichiarazione di consistenza
	 *
	 * @param codiceFotografiaTerreni
	 * @param dataInserimentoDichiarazione
	 * @param flagVideo
	 * @param flagStampa
	 * @param orderBy
	 * @param codiceAttestazione
	 * @param voceMenu
	 * @return it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO[]
	 * @throws DataAccessException
	 */
	public TipoAttestazioneVO[] getListTipoAttestazioneDichiarazioniAllaDichiarazione(String codiceFotografiaTerreni, 
	    java.util.Date dataAnnoCampagna, String[] orderBy) throws DataAccessException 
  {
		SolmrLogger.debug(this, "Invocating getListTipoAttestazioneAllaDichiarazione method in TipoAttestazioneDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoAttestazioneVO> elencoTipoAttestazione = new Vector<TipoAttestazioneVO>();
		//int countDichiarate = 0;

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in getListTipoAttestazioneDichiarazioniAllaDichiarazione method in TipoAttestazioneDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListTipoAttestazioneDichiarazioniAllaDichiarazione method in TipoAttestazioneDAO and it values: "+conn+"\n");

			String query = 
        "WITH DATI_DICHIARATI AS	" +
        " 	(SELECT AD.*,	 " +
        " 	        PAD.ID_PARAMETRI_ATT_DICHIARATA, " +
        " 	        PAD.PARAMETRO_1, " +
        " 	        PAD.PARAMETRO_2, " +
        " 	        PAD.PARAMETRO_3, " +
        " 	        PAD.PARAMETRO_4, " +
        " 	        PAD.PARAMETRO_5	 " +
        " 	 FROM   DB_ATTESTAZIONE_DICHIARATA AD, " +
        " 	        DB_PARAMETRI_ATT_DICHIARATA PAD " +
        " 	 WHERE  AD.CODICE_FOTOGRAFIA_TERRENI = ? " +
        "    AND    AD.DATA_FINE_VALIDITA IS NULL " + 
        " 	 AND    AD.ID_ATTESTAZIONE_DICHIARATA = PAD.ID_ATTESTAZIONE_DICHIARATA(+) )	" +
        " 	SELECT 	 " +
        " 	   TA.ID_ATTESTAZIONE, " +
        " 	   TA.CODICE_ATTESTAZIONE, " +
        " 	   TA.DESCRIZIONE, " +
        " 	   TA.DATA_INIZIO_VALIDITA, " +
        " 	   TA.DATA_FINE_VALIDITA, " +
        " 	   TA.FLAG_VIDEO, " +
        " 	   TA.FLAG_STAMPA, " +
        " 	   TA.TIPO_RIGA, " +
        " 	   TA.NUMERO_COLONNE_RIGA, " +
        " 	   TA.ID_ATTESTAZIONE_PADRE, " +
        " 	   TA.TIPO_CARATTERE, " +
        " 	   TA.GRUPPO, " +
        " 	   TA.ORDINAMENTO, " +
        " 	   TA.OBBLIGATORIO, " +
        " 	   TA.DISABILITATO, " +
        " 	   TA.SELEZIONA_VIDEO, " +
        " 	   TA.ATTESTAZIONI_CORRELATE, " +
        " 	   TA.VOCE_MENU, " +
        " 	   DATI_DICHIARATI.ID_ATTESTAZIONE_DICHIARATA, " +
        " 	   DATI_DICHIARATI.ID_PARAMETRI_ATT_DICHIARATA, " +
        " 	   DATI_DICHIARATI.PARAMETRO_1, " +
        " 	   DATI_DICHIARATI.PARAMETRO_2, " +
        " 	   DATI_DICHIARATI.PARAMETRO_3, " +
        " 	   DATI_DICHIARATI.PARAMETRO_4, " +
        " 	   DATI_DICHIARATI.PARAMETRO_5, " +
        " 	   TPA.ID_PARAMETRI_ATTESTAZIONE, " +
        " 	   TPA.PARAMETRO_1 AS TIPO_PAR_1, " +
        " 	   TPA.PARAMETRO_2 AS TIPO_PAR_2, " +
        " 	   TPA.PARAMETRO_3 AS TIPO_PAR_3, " +
        " 	   TPA.PARAMETRO_4 AS TIPO_PAR_4, " +
        " 	   TPA.PARAMETRO_5 AS TIPO_PAR_5, " +
        " 	   TPA.OBBLIGATORIO_1, " +
        " 	   TPA.OBBLIGATORIO_2, " +
        " 	   TPA.OBBLIGATORIO_3, " +
        " 	   TPA.OBBLIGATORIO_4, " +
        " 	   TPA.OBBLIGATORIO_5 " +
        " 	FROM   DB_TIPO_ATTESTAZIONE TA, " +
        " 	   DATI_DICHIARATI, " +
        " 	   DB_TIPO_PARAMETRI_ATTESTAZIONE TPA " +
        " 	WHERE  TA.ID_ATTESTAZIONE = DATI_DICHIARATI.ID_ATTESTAZIONE(+) " +
        " 	AND    TRUNC(TA.DATA_INIZIO_VALIDITA) <= TRUNC(?) " +
        " 	AND    NVL(TRUNC(TA.DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY')) >=  TRUNC(?)	 " +
        " 	AND    TA.ID_ATTESTAZIONE = TPA.ID_ATTESTAZIONE(+) " +
			  "   AND    TA.FLAG_VIDEO = 'S' " +
      	"   AND    TA.VOCE_MENU = '"+SolmrConstants.VOCE_MENU_ATTESTAZIONI_DICHIARAZIONI+"' ";

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
				query+= "ORDER BY "+criterio;
			}

	
			stmt = conn.prepareStatement(query);

			int indice = 0;
			stmt.setString(++indice, codiceFotografiaTerreni);
			stmt.setDate(++indice, new java.sql.Date(dataAnnoCampagna.getTime()));
			stmt.setDate(++indice, new java.sql.Date(dataAnnoCampagna.getTime()));
			
			SolmrLogger.debug(this, "Executing getListTipoAttestazioneDichiarazioniAllaDichiarazione: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			Long idAttestazioneConfronto = null;
			while(rs.next()) {
				TipoAttestazioneVO tipoAttestazioneVO = new TipoAttestazioneVO();
				tipoAttestazioneVO.setIdAttestazione(new Long(rs.getLong("ID_ATTESTAZIONE")));
				tipoAttestazioneVO.setCodiceAttestazione(rs.getString("CODICE_ATTESTAZIONE"));
				tipoAttestazioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoAttestazioneVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				tipoAttestazioneVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				tipoAttestazioneVO.setFlagVideo(rs.getString("FLAG_VIDEO"));
				tipoAttestazioneVO.setFlagStampa(rs.getString("FLAG_STAMPA"));
				tipoAttestazioneVO.setTipoRiga(rs.getString("TIPO_RIGA"));
				tipoAttestazioneVO.setNumeroColonneRiga(rs.getString("NUMERO_COLONNE_RIGA"));
				tipoAttestazioneVO.setIdAttestazionePadre(new Long(rs.getLong("ID_ATTESTAZIONE_PADRE")));
				tipoAttestazioneVO.setTipoCarattere(rs.getString("TIPO_CARATTERE"));
				tipoAttestazioneVO.setGruppo(rs.getString("GRUPPO"));
				tipoAttestazioneVO.setOrdinamento(rs.getString("ORDINAMENTO"));
				tipoAttestazioneVO.setObbligatorio(rs.getString("OBBLIGATORIO"));
				tipoAttestazioneVO.setDisabilitato(rs.getString("DISABILITATO"));
				tipoAttestazioneVO.setSelezionaVideo(rs.getString("SELEZIONA_VIDEO"));
				tipoAttestazioneVO.setAttestazioniCorrelate(rs.getString("ATTESTAZIONI_CORRELATE"));
				tipoAttestazioneVO.setVoceMenu(rs.getString("VOCE_MENU"));
				if(Validator.isNotEmpty(rs.getString("ID_ATTESTAZIONE_DICHIARATA"))) {
					//countDichiarate++;
					if(tipoAttestazioneVO.getAttestazioniCorrelate().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
						idAttestazioneConfronto = tipoAttestazioneVO.getIdAttestazione();
					}
					tipoAttestazioneVO.setAttestazioneAzienda(true);
				}
				else {
					if(idAttestazioneConfronto != null && idAttestazioneConfronto.compareTo(tipoAttestazioneVO.getIdAttestazionePadre()) == 0) {
						tipoAttestazioneVO.setAttestazioneAzienda(true);
					}
				}
				if(Validator.isNotEmpty(rs.getString("ID_PARAMETRI_ATT_DICHIARATA"))) {
					ParametriAttDichiarataVO parametriAttDichiarataVO = new ParametriAttDichiarataVO();
					parametriAttDichiarataVO.setIdParametriAttDichiarata(new Long(rs.getLong("ID_PARAMETRI_ATT_DICHIARATA")));
					parametriAttDichiarataVO.setParametro1(rs.getString("PARAMETRO_1"));
					parametriAttDichiarataVO.setParametro2(rs.getString("PARAMETRO_2"));
					parametriAttDichiarataVO.setParametro3(rs.getString("PARAMETRO_3"));
					parametriAttDichiarataVO.setParametro4(rs.getString("PARAMETRO_4"));
					parametriAttDichiarataVO.setParametro5(rs.getString("PARAMETRO_5"));
					tipoAttestazioneVO.setParametriAttDichiarataVO(parametriAttDichiarataVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_PARAMETRI_ATTESTAZIONE"))) {
					TipoParametriAttestazioneVO tipoParametriAttestazioneVO = new TipoParametriAttestazioneVO();
					tipoParametriAttestazioneVO.setIdParametriAttestazione(new Long(rs.getLong("ID_PARAMETRI_ATTESTAZIONE")));
					tipoParametriAttestazioneVO.setIdAttestazione(new Long(rs.getLong("ID_ATTESTAZIONE")));
					tipoParametriAttestazioneVO.setParametro1(rs.getString("TIPO_PAR_1"));
					tipoParametriAttestazioneVO.setParametro2(rs.getString("TIPO_PAR_2"));
					tipoParametriAttestazioneVO.setParametro3(rs.getString("TIPO_PAR_3"));
					tipoParametriAttestazioneVO.setParametro4(rs.getString("TIPO_PAR_4"));
					tipoParametriAttestazioneVO.setParametro5(rs.getString("TIPO_PAR_5"));
					tipoParametriAttestazioneVO.setObbligatorio1(rs.getString("OBBLIGATORIO_1"));
					tipoParametriAttestazioneVO.setObbligatorio2(rs.getString("OBBLIGATORIO_2"));
					tipoParametriAttestazioneVO.setObbligatorio3(rs.getString("OBBLIGATORIO_3"));
					tipoParametriAttestazioneVO.setObbligatorio4(rs.getString("OBBLIGATORIO_4"));
					tipoParametriAttestazioneVO.setObbligatorio5(rs.getString("OBBLIGATORIO_5"));
					tipoAttestazioneVO.setTipoParametriAttestazioneVO(tipoParametriAttestazioneVO);
				}
				elencoTipoAttestazione.add(tipoAttestazioneVO);
			}

			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListTipoAttestazioneDichiarazioniAllaDichiarazione in TipoAttestazioneDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListTipoAttestazioneDichiarazioniAllaDichiarazione in TipoAttestazioneDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListTipoAttestazioneDichiarazioniAllaDichiarazione in TipoAttestazioneDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListTipoAttestazioneDichiarazioniAllaDichiarazione in TipoAttestazioneDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipoAttestazioneDichiarazioniAllaDichiarazione method in TipoAttestazioneDAO\n");
		if(elencoTipoAttestazione.size() == 0) {
			return (TipoAttestazioneVO[])elencoTipoAttestazione.toArray(new TipoAttestazioneVO[0]);
		}
		else {
			return (TipoAttestazioneVO[])elencoTipoAttestazione.toArray(new TipoAttestazioneVO[elencoTipoAttestazione.size()]);
		}
	}
	
	
	
	
	/**
	 * Metodo utilizzato per estrarre gli allegati validi 
	 * ad una determinata dichiarazione di consistenza e ad una vaziriazione
	 *
	 * @param dataInserimentoDichiarazione
	 * @param flagVideo
	 * @param flagStampa
	 * @param orderBy
	 * @param codiceAttestazione
	 * @param voceMenu
	 * @return it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO[]
	 * @throws DataAccessException
	 */
	public TipoAttestazioneVO[] getTipoAttestazioneAllegatiAllaDichiarazione(
	    String codiceFotografiaTerreni, java.util.Date dataAnnoCampagna, 
	    java.util.Date dataVariazione, String[] orderBy) 
	  throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating getTipoAttestazioneAllegatiAllaDichiarazione method in TipoAttestazioneDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoAttestazioneVO> elencoTipoAttestazione = new Vector<TipoAttestazioneVO>();

		try 
		{
		  SolmrLogger.debug(this, "Creating db-connection in getTipoAttestazioneAllegatiAllaDichiarazione method in TipoAttestazioneDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getTipoAttestazioneAllegatiAllaDichiarazione method in TipoAttestazioneDAO and it values: "+conn+"\n");

      String query = 
        "WITH DATI_DICHIARATI AS  " +
        "   (SELECT AD.*,  " +
        "           PAD.ID_PARAMETRI_ATT_DICHIARATA, " +
        "           PAD.PARAMETRO_1, " +
        "           PAD.PARAMETRO_2, " +
        "           PAD.PARAMETRO_3, " +
        "           PAD.PARAMETRO_4, " +
        "           PAD.PARAMETRO_5  " +
        "      FROM DB_ATTESTAZIONE_DICHIARATA AD, " +
        "           DB_PARAMETRI_ATT_DICHIARATA PAD " +
        "     WHERE AD.CODICE_FOTOGRAFIA_TERRENI = ? " +
        "     AND TRUNC(AD.DATA_INIZIO_VALIDITA) = TRUNC(?) " +
        "     AND NVL(TRUNC(AD.DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'dd/mm/yyyy')) > TRUNC(?) " +
        "       AND AD.ID_ATTESTAZIONE_DICHIARATA = PAD.ID_ATTESTAZIONE_DICHIARATA(+) ) " +
        "   SELECT   " +
        "      TA.ID_ATTESTAZIONE, " +
        "      TA.CODICE_ATTESTAZIONE, " +
        "      TA.DESCRIZIONE, " +
        "      TA.DATA_INIZIO_VALIDITA, " +
        "      TA.DATA_FINE_VALIDITA, " +
        "      TA.FLAG_VIDEO, " +
        "      TA.FLAG_STAMPA, " +
        "      TA.TIPO_RIGA, " +
        "      TA.NUMERO_COLONNE_RIGA, " +
        "      TA.ID_ATTESTAZIONE_PADRE, " +
        "      TA.TIPO_CARATTERE, " +
        "      TA.GRUPPO, " +
        "      TA.ORDINAMENTO, " +
        "      TA.OBBLIGATORIO, " +
        "      TA.DISABILITATO, " +
        "      TA.SELEZIONA_VIDEO, " +
        "      TA.ATTESTAZIONI_CORRELATE, " +
        "      TA.VOCE_MENU, " +
        "      DATI_DICHIARATI.ID_ATTESTAZIONE_DICHIARATA, " +
        "      DATI_DICHIARATI.ID_PARAMETRI_ATT_DICHIARATA, " +
        "      DATI_DICHIARATI.PARAMETRO_1, " +
        "      DATI_DICHIARATI.PARAMETRO_2, " +
        "      DATI_DICHIARATI.PARAMETRO_3, " +
        "      DATI_DICHIARATI.PARAMETRO_4, " +
        "      DATI_DICHIARATI.PARAMETRO_5, " +
        "      TPA.ID_PARAMETRI_ATTESTAZIONE, " +
        "      TPA.PARAMETRO_1 AS TIPO_PAR_1, " +
        "      TPA.PARAMETRO_2 AS TIPO_PAR_2, " +
        "      TPA.PARAMETRO_3 AS TIPO_PAR_3, " +
        "      TPA.PARAMETRO_4 AS TIPO_PAR_4, " +
        "      TPA.PARAMETRO_5 AS TIPO_PAR_5, " +
        "      TPA.OBBLIGATORIO_1, " +
        "      TPA.OBBLIGATORIO_2, " +
        "      TPA.OBBLIGATORIO_3, " +
        "      TPA.OBBLIGATORIO_4, " +
        "      TPA.OBBLIGATORIO_5 " +
        "   FROM   DB_TIPO_ATTESTAZIONE TA, " +
        "      DATI_DICHIARATI, " +
        "      DB_TIPO_PARAMETRI_ATTESTAZIONE TPA " +
        "   WHERE  TA.ID_ATTESTAZIONE = DATI_DICHIARATI.ID_ATTESTAZIONE(+) " +
        "   AND    TRUNC(TA.DATA_INIZIO_VALIDITA) <= TRUNC(?) " +
        "   AND    NVL(TRUNC(TA.DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY')) >=  TRUNC(?)   " +
        "   AND    TA.ID_ATTESTAZIONE = TPA.ID_ATTESTAZIONE(+)   " +
        "   AND    TA.FLAG_VIDEO = 'S' " +
        "   AND    TA.VOCE_MENU = '"+SolmrConstants.VOCE_MENU_ATTESTAZIONI_ALLEGATI+"' ";

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
        query+= "ORDER BY "+criterio;
      }

  
      stmt = conn.prepareStatement(query);

      int indice = 0;
      stmt.setString(++indice, codiceFotografiaTerreni);
      stmt.setDate(++indice, new java.sql.Date(dataVariazione.getTime()));
      stmt.setDate(++indice, new java.sql.Date(dataVariazione.getTime()));
      stmt.setDate(++indice, new java.sql.Date(dataAnnoCampagna.getTime()));
      stmt.setDate(++indice, new java.sql.Date(dataAnnoCampagna.getTime()));
      

      SolmrLogger.debug(this, "Executing getListTipoAttestazioneAllaDichiarazione: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      Long idAttestazioneConfronto = null;
      while(rs.next()) {
        TipoAttestazioneVO tipoAttestazioneVO = new TipoAttestazioneVO();
        tipoAttestazioneVO.setIdAttestazione(new Long(rs.getLong("ID_ATTESTAZIONE")));
        tipoAttestazioneVO.setCodiceAttestazione(rs.getString("CODICE_ATTESTAZIONE"));
        tipoAttestazioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoAttestazioneVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        tipoAttestazioneVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        tipoAttestazioneVO.setFlagVideo(rs.getString("FLAG_VIDEO"));
        tipoAttestazioneVO.setFlagStampa(rs.getString("FLAG_STAMPA"));
        tipoAttestazioneVO.setTipoRiga(rs.getString("TIPO_RIGA"));
        tipoAttestazioneVO.setNumeroColonneRiga(rs.getString("NUMERO_COLONNE_RIGA"));
        tipoAttestazioneVO.setIdAttestazionePadre(new Long(rs.getLong("ID_ATTESTAZIONE_PADRE")));
        tipoAttestazioneVO.setTipoCarattere(rs.getString("TIPO_CARATTERE"));
        tipoAttestazioneVO.setGruppo(rs.getString("GRUPPO"));
        tipoAttestazioneVO.setOrdinamento(rs.getString("ORDINAMENTO"));
        tipoAttestazioneVO.setObbligatorio(rs.getString("OBBLIGATORIO"));
        tipoAttestazioneVO.setDisabilitato(rs.getString("DISABILITATO"));
        tipoAttestazioneVO.setSelezionaVideo(rs.getString("SELEZIONA_VIDEO"));
        tipoAttestazioneVO.setAttestazioniCorrelate(rs.getString("ATTESTAZIONI_CORRELATE"));
        tipoAttestazioneVO.setVoceMenu(rs.getString("VOCE_MENU"));
        if(Validator.isNotEmpty(rs.getString("ID_ATTESTAZIONE_DICHIARATA"))) {
          //countDichiarate++;
          if(tipoAttestazioneVO.getAttestazioniCorrelate().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
            idAttestazioneConfronto = tipoAttestazioneVO.getIdAttestazione();
          }
          tipoAttestazioneVO.setAttestazioneAzienda(true);
        }
        else {
          if(idAttestazioneConfronto != null && idAttestazioneConfronto.compareTo(tipoAttestazioneVO.getIdAttestazionePadre()) == 0) {
            tipoAttestazioneVO.setAttestazioneAzienda(true);
          }
        }
        if(Validator.isNotEmpty(rs.getString("ID_PARAMETRI_ATT_DICHIARATA"))) {
          ParametriAttDichiarataVO parametriAttDichiarataVO = new ParametriAttDichiarataVO();
          parametriAttDichiarataVO.setIdParametriAttDichiarata(new Long(rs.getLong("ID_PARAMETRI_ATT_DICHIARATA")));
          parametriAttDichiarataVO.setParametro1(rs.getString("PARAMETRO_1"));
          parametriAttDichiarataVO.setParametro2(rs.getString("PARAMETRO_2"));
          parametriAttDichiarataVO.setParametro3(rs.getString("PARAMETRO_3"));
          parametriAttDichiarataVO.setParametro4(rs.getString("PARAMETRO_4"));
          parametriAttDichiarataVO.setParametro5(rs.getString("PARAMETRO_5"));
          tipoAttestazioneVO.setParametriAttDichiarataVO(parametriAttDichiarataVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_PARAMETRI_ATTESTAZIONE"))) {
          TipoParametriAttestazioneVO tipoParametriAttestazioneVO = new TipoParametriAttestazioneVO();
          tipoParametriAttestazioneVO.setIdParametriAttestazione(new Long(rs.getLong("ID_PARAMETRI_ATTESTAZIONE")));
          tipoParametriAttestazioneVO.setIdAttestazione(new Long(rs.getLong("ID_ATTESTAZIONE")));
          tipoParametriAttestazioneVO.setParametro1(rs.getString("TIPO_PAR_1"));
          tipoParametriAttestazioneVO.setParametro2(rs.getString("TIPO_PAR_2"));
          tipoParametriAttestazioneVO.setParametro3(rs.getString("TIPO_PAR_3"));
          tipoParametriAttestazioneVO.setParametro4(rs.getString("TIPO_PAR_4"));
          tipoParametriAttestazioneVO.setParametro5(rs.getString("TIPO_PAR_5"));
          tipoParametriAttestazioneVO.setObbligatorio1(rs.getString("OBBLIGATORIO_1"));
          tipoParametriAttestazioneVO.setObbligatorio2(rs.getString("OBBLIGATORIO_2"));
          tipoParametriAttestazioneVO.setObbligatorio3(rs.getString("OBBLIGATORIO_3"));
          tipoParametriAttestazioneVO.setObbligatorio4(rs.getString("OBBLIGATORIO_4"));
          tipoParametriAttestazioneVO.setObbligatorio5(rs.getString("OBBLIGATORIO_5"));
          tipoAttestazioneVO.setTipoParametriAttestazioneVO(tipoParametriAttestazioneVO);
        }
        elencoTipoAttestazione.add(tipoAttestazioneVO);
      }

      

      rs.close();
      stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getTipoAttestazioneAllegatiAllaDichiarazione in TipoAttestazioneDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getTipoAttestazioneAllegatiAllaDichiarazione in TipoAttestazioneDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getTipoAttestazioneAllegatiAllaDichiarazione in TipoAttestazioneDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getTipoAttestazioneAllegatiAllaDichiarazione in TipoAttestazioneDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getTipoAttestazioneAllegatiAllaDichiarazione method in TipoAttestazioneDAO\n");
		if(elencoTipoAttestazione.size() == 0) {
			return (TipoAttestazioneVO[])elencoTipoAttestazione.toArray(new TipoAttestazioneVO[0]);
		}
		else {
			return (TipoAttestazioneVO[])elencoTipoAttestazione.toArray(new TipoAttestazioneVO[elencoTipoAttestazione.size()]);
		}
	}
	
	
	
	public Vector<TipoAttestazioneVO> getElencoAttestazioniAllaDichiarazione(
      String codiceFotografiaTerreni, java.util.Date dataAnnoCampagna, 
      String codiceAttestazione) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getElencoAttestazioniAllaDichiarazione method in TipoAttestazioneDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoAttestazioneVO> elencoTipoAttestazione = new Vector<TipoAttestazioneVO>();

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getElencoAttestazioniAllaDichiarazione method in TipoAttestazioneDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getElencoAttestazioniAllaDichiarazione method in TipoAttestazioneDAO and it values: "+conn+"\n");

      String query = 
        "WITH DATI_DICHIARATI AS  " +
        "   (SELECT AD.*,  " +
        "           PAD.ID_PARAMETRI_ATT_DICHIARATA, " +
        "           PAD.PARAMETRO_1, " +
        "           PAD.PARAMETRO_2, " +
        "           PAD.PARAMETRO_3, " +
        "           PAD.PARAMETRO_4, " +
        "           PAD.PARAMETRO_5  " +
        "    FROM   DB_ATTESTAZIONE_DICHIARATA AD, " +
        "           DB_PARAMETRI_ATT_DICHIARATA PAD " +
        "    WHERE  AD.CODICE_FOTOGRAFIA_TERRENI = ? " +
        "    AND    TRUNC(AD.DATA_INIZIO_VALIDITA) = TRUNC(?) " +
        "    AND    NVL(TRUNC(AD.DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'dd/mm/yyyy')) > TRUNC(?) " +
        "    AND    AD.ID_ATTESTAZIONE_DICHIARATA = PAD.ID_ATTESTAZIONE_DICHIARATA(+) ) " +
        "SELECT TA.ID_ATTESTAZIONE, " +
        "       TA.CODICE_ATTESTAZIONE, " +
        "       TA.DESCRIZIONE, " +
        "       TA.DATA_INIZIO_VALIDITA, " +
        "       TA.DATA_FINE_VALIDITA, " +
        "       TA.FLAG_VIDEO, " +
        "       TA.FLAG_STAMPA, " +
        "       TA.TIPO_RIGA, " +
        "       TA.NUMERO_COLONNE_RIGA, " +
        "       TA.ID_ATTESTAZIONE_PADRE, " +
        "       TA.TIPO_CARATTERE, " +
        "       TA.GRUPPO, " +
        "       TA.ORDINAMENTO, " +
        "       TA.OBBLIGATORIO, " +
        "       TA.DISABILITATO, " +
        "       TA.SELEZIONA_VIDEO, " +
        "       TA.ATTESTAZIONI_CORRELATE, " +
        "       TA.VOCE_MENU, " +
        "       DATI_DICHIARATI.ID_ATTESTAZIONE_DICHIARATA, " +
        "       DATI_DICHIARATI.ID_PARAMETRI_ATT_DICHIARATA, " +
        "       DATI_DICHIARATI.PARAMETRO_1, " +
        "       DATI_DICHIARATI.PARAMETRO_2, " +
        "       DATI_DICHIARATI.PARAMETRO_3, " +
        "       DATI_DICHIARATI.PARAMETRO_4, " +
        "       DATI_DICHIARATI.PARAMETRO_5, " +
        "       TPA.ID_PARAMETRI_ATTESTAZIONE, " +
        "       TPA.PARAMETRO_1 AS TIPO_PAR_1, " +
        "       TPA.PARAMETRO_2 AS TIPO_PAR_2, " +
        "       TPA.PARAMETRO_3 AS TIPO_PAR_3, " +
        "       TPA.PARAMETRO_4 AS TIPO_PAR_4, " +
        "       TPA.PARAMETRO_5 AS TIPO_PAR_5, " +
        "       TPA.OBBLIGATORIO_1, " +
        "       TPA.OBBLIGATORIO_2, " +
        "       TPA.OBBLIGATORIO_3, " +
        "       TPA.OBBLIGATORIO_4, " +
        "       TPA.OBBLIGATORIO_5 " +
        "FROM   DB_TIPO_ATTESTAZIONE TA, " +
        "       DATI_DICHIARATI, " +
        "       DB_TIPO_PARAMETRI_ATTESTAZIONE TPA " +
        "WHERE  TA.ID_ATTESTAZIONE = DATI_DICHIARATI.ID_ATTESTAZIONE(+) " +
        "AND    TRUNC(TA.DATA_INIZIO_VALIDITA) <= TRUNC(?) " +
        "AND    NVL(TRUNC(TA.DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY')) >=  TRUNC(?) " +
        "AND    TA.ID_ATTESTAZIONE = TPA.ID_ATTESTAZIONE(+)   " +
        "AND    TA.FLAG_VIDEO = 'S' " +
        "AND    TA.CODICE_ATTESTAZIONE = ? " +
        "ORDER BY TA.CODICE_ATTESTAZIONE ASC, " +
        "         TA.GRUPPO ASC, " +
        "         TA.ORDINAMENTO ASC ";

      

  
      stmt = conn.prepareStatement(query);

      int indice = 0;
      stmt.setString(++indice, codiceFotografiaTerreni);
      stmt.setTimestamp(++indice, convertDateToTimestamp(dataAnnoCampagna));
      stmt.setTimestamp(++indice, convertDateToTimestamp(dataAnnoCampagna));
      stmt.setTimestamp(++indice, convertDateToTimestamp(dataAnnoCampagna));
      stmt.setTimestamp(++indice, convertDateToTimestamp(dataAnnoCampagna));
      stmt.setString(++indice, codiceAttestazione);
      

      SolmrLogger.debug(this, "Executing getElencoAttestazioniAllaDichiarazione: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      Long idAttestazioneConfronto = null;
      while(rs.next()) 
      {
        TipoAttestazioneVO tipoAttestazioneVO = new TipoAttestazioneVO();
        tipoAttestazioneVO.setIdAttestazione(new Long(rs.getLong("ID_ATTESTAZIONE")));
        tipoAttestazioneVO.setCodiceAttestazione(rs.getString("CODICE_ATTESTAZIONE"));
        tipoAttestazioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoAttestazioneVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        tipoAttestazioneVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        tipoAttestazioneVO.setFlagVideo(rs.getString("FLAG_VIDEO"));
        tipoAttestazioneVO.setFlagStampa(rs.getString("FLAG_STAMPA"));
        tipoAttestazioneVO.setTipoRiga(rs.getString("TIPO_RIGA"));
        tipoAttestazioneVO.setNumeroColonneRiga(rs.getString("NUMERO_COLONNE_RIGA"));
        tipoAttestazioneVO.setIdAttestazionePadre(new Long(rs.getLong("ID_ATTESTAZIONE_PADRE")));
        tipoAttestazioneVO.setTipoCarattere(rs.getString("TIPO_CARATTERE"));
        tipoAttestazioneVO.setGruppo(rs.getString("GRUPPO"));
        tipoAttestazioneVO.setOrdinamento(rs.getString("ORDINAMENTO"));
        tipoAttestazioneVO.setObbligatorio(rs.getString("OBBLIGATORIO"));
        tipoAttestazioneVO.setDisabilitato(rs.getString("DISABILITATO"));
        tipoAttestazioneVO.setSelezionaVideo(rs.getString("SELEZIONA_VIDEO"));
        tipoAttestazioneVO.setAttestazioniCorrelate(rs.getString("ATTESTAZIONI_CORRELATE"));
        tipoAttestazioneVO.setVoceMenu(rs.getString("VOCE_MENU"));
        if(Validator.isNotEmpty(rs.getString("ID_ATTESTAZIONE_DICHIARATA"))) 
        {
          //countDichiarate++;
          if(tipoAttestazioneVO.getAttestazioniCorrelate().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
          {
            idAttestazioneConfronto = tipoAttestazioneVO.getIdAttestazione();
          }
          tipoAttestazioneVO.setAttestazioneAzienda(true);
        }
        else 
        {
          if(idAttestazioneConfronto != null && idAttestazioneConfronto.compareTo(tipoAttestazioneVO.getIdAttestazionePadre()) == 0) {
            tipoAttestazioneVO.setAttestazioneAzienda(true);
          }
        }
        if(Validator.isNotEmpty(rs.getString("ID_PARAMETRI_ATT_DICHIARATA"))) 
        {
          ParametriAttDichiarataVO parametriAttDichiarataVO = new ParametriAttDichiarataVO();
          parametriAttDichiarataVO.setIdParametriAttDichiarata(new Long(rs.getLong("ID_PARAMETRI_ATT_DICHIARATA")));
          parametriAttDichiarataVO.setParametro1(rs.getString("PARAMETRO_1"));
          parametriAttDichiarataVO.setParametro2(rs.getString("PARAMETRO_2"));
          parametriAttDichiarataVO.setParametro3(rs.getString("PARAMETRO_3"));
          parametriAttDichiarataVO.setParametro4(rs.getString("PARAMETRO_4"));
          parametriAttDichiarataVO.setParametro5(rs.getString("PARAMETRO_5"));
          tipoAttestazioneVO.setParametriAttDichiarataVO(parametriAttDichiarataVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_PARAMETRI_ATTESTAZIONE"))) 
        {
          TipoParametriAttestazioneVO tipoParametriAttestazioneVO = new TipoParametriAttestazioneVO();
          tipoParametriAttestazioneVO.setIdParametriAttestazione(new Long(rs.getLong("ID_PARAMETRI_ATTESTAZIONE")));
          tipoParametriAttestazioneVO.setIdAttestazione(new Long(rs.getLong("ID_ATTESTAZIONE")));
          tipoParametriAttestazioneVO.setParametro1(rs.getString("TIPO_PAR_1"));
          tipoParametriAttestazioneVO.setParametro2(rs.getString("TIPO_PAR_2"));
          tipoParametriAttestazioneVO.setParametro3(rs.getString("TIPO_PAR_3"));
          tipoParametriAttestazioneVO.setParametro4(rs.getString("TIPO_PAR_4"));
          tipoParametriAttestazioneVO.setParametro5(rs.getString("TIPO_PAR_5"));
          tipoParametriAttestazioneVO.setObbligatorio1(rs.getString("OBBLIGATORIO_1"));
          tipoParametriAttestazioneVO.setObbligatorio2(rs.getString("OBBLIGATORIO_2"));
          tipoParametriAttestazioneVO.setObbligatorio3(rs.getString("OBBLIGATORIO_3"));
          tipoParametriAttestazioneVO.setObbligatorio4(rs.getString("OBBLIGATORIO_4"));
          tipoParametriAttestazioneVO.setObbligatorio5(rs.getString("OBBLIGATORIO_5"));
          tipoAttestazioneVO.setTipoParametriAttestazioneVO(tipoParametriAttestazioneVO);
        }
        elencoTipoAttestazione.add(tipoAttestazioneVO);
      }

      

      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getElencoAttestazioniAllaDichiarazione in TipoAttestazioneDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getElencoAttestazioniAllaDichiarazione in TipoAttestazioneDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getElencoAttestazioniAllaDichiarazione in TipoAttestazioneDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getElencoAttestazioniAllaDichiarazione in TipoAttestazioneDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getElencoAttestazioniAllaDichiarazione method in TipoAttestazioneDAO\n");
    
    return elencoTipoAttestazione;
    
  }



  /**
   * Metodo utilizzato per confrontare la dataInserimentoDichiarazione con
   * MIN(TA.DATA_INIZIO_VALIDITA) e MAX(TA.DATA_FINE_VALIDITA) della tabella
   * DB_TIPO_ATTESTAZIONE
   *
   * @param codiceFotografiaTerreni
   * @param dataInserimentoDichiarazione
   * @param flagVideo
   * @param flagStampa
   * @param codiceAttestazione
   * @param voceMenu
   * @return Integer possibili valori restituiti dal metodo
   *  null: non è stato trovato alcun record sul DB
   *  -1: la data dataInserimentoDichiarazione è minore della data MIN(TA.DATA_INIZIO_VALIDITA)
   *   0: la data dataInserimentoDichiarazione è compresa fra la data MIN(TA.DATA_INIZIO_VALIDITA) e la data MAX(TA.DATA_FINE_VALIDITA)
   *  +1: la data dataInserimentoDichiarazione è maggiore della data MAX(TA.DATA_FINE_VALIDITA)
   * @throws DataAccessException
   */
  public boolean getDataAttestazioneAllaDichiarazione(String codiceFotografiaTerreni, java.util.Date dataInserimentoDichiarazione, boolean flagVideo, boolean flagStampa, String codiceAttestazione, String voceMenu)
    throws DataAccessException
  {
    SolmrLogger.debug(this, "Invocating getDataAttestazioneAllaDichiarazione method in TipoAttestazioneDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    boolean result=false;

    try
    {
      SolmrLogger.debug(this, "Creating db-connection in getDataAttestazioneAllaDichiarazione method in TipoAttestazioneDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getDataAttestazioneAllaDichiarazione method in TipoAttestazioneDAO and it values: "+conn+"\n");

      String query = "" +
      		"SELECT * "+
          "FROM   DB_TIPO_ATTESTAZIONE TA ";
      if (codiceFotografiaTerreni!=null)
      {
        query +="" +
        	"      ,DB_ATTESTAZIONE_DICHIARATA AD ";
      }

      query +="" +
      		"WHERE  1=1 ";

      if (codiceFotografiaTerreni!=null)
      {
        query +=   "" +
        	"AND    AD.CODICE_FOTOGRAFIA_TERRENI = ? " +
        	"AND    TA.ID_ATTESTAZIONE = AD.ID_ATTESTAZIONE ";
      }
      if(flagVideo)
      {
        query +=   "" +
        	"AND    TA.FLAG_VIDEO = ? ";
      }
      if(flagStampa)
      {
        query +=   "" +
        	"AND    TA.FLAG_STAMPA = ? ";
      }
      if(codiceAttestazione != null)
      {
        query +=   "" +
          "AND    TA.CODICE_ATTESTAZIONE = ? ";
      }
      if(Validator.isNotEmpty(voceMenu))
      {
        query +=   "" +
        	"AND    TA.VOCE_MENU = ? ";
      }
        



      SolmrLogger.debug(this, "Value of parameter 1 [CODICE_FOTOGRAFIA_TERRENI] in getDataAttestazioneAllaDichiarazione method in TipoAttestazioneDAO: "+codiceFotografiaTerreni+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [DATA_INSERIMENTO_DICHIARAZIONE] in getDataAttestazioneAllaDichiarazione method in TipoAttestazioneDAO: "+dataInserimentoDichiarazione+"\n");
      SolmrLogger.debug(this, "Value of parameter 3 [FLAG_VIDEO] in getDataAttestazioneAllaDichiarazione method in TipoAttestazioneDAO: "+flagVideo+"\n");
      SolmrLogger.debug(this, "Value of parameter 4 [FLAG_STAMPA] in getDataAttestazioneAllaDichiarazione method in TipoAttestazioneDAO: "+flagStampa+"\n");
      SolmrLogger.debug(this, "Value of parameter 5 [CODICE_ATTESTAZIONE] in getDataAttestazioneAllaDichiarazione method in TipoAttestazioneDAO: "+codiceAttestazione+"\n");
      SolmrLogger.debug(this, "Value of parameter 6 [VOCE_MENU] in getDataAttestazioneAllaDichiarazione method in TipoAttestazioneDAO: "+voceMenu+"\n");

      stmt = conn.prepareStatement(query);

      int indice = 1;
      if (codiceFotografiaTerreni!=null)
      {
        stmt.setString(indice++, codiceFotografiaTerreni);
      }
      if(flagVideo)
      {
        stmt.setString(indice++, SolmrConstants.FLAG_S);
      }
      if(flagStampa)
      {
        stmt.setString(indice++, SolmrConstants.FLAG_S);
      }
      if(codiceAttestazione != null)
      {
        stmt.setString(indice++, codiceAttestazione);
      }
      if(Validator.isNotEmpty(voceMenu))
      {
        stmt.setString(indice++, voceMenu);
      }

      SolmrLogger.debug(this, "Executing getDataAttestazioneAllaDichiarazione: "+query+"\n");

      ResultSet rs = stmt.executeQuery();


      if(rs.next())
      	result=true;
    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "getDataAttestazioneAllaDichiarazione in TipoAttestazioneDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) 
    {
      SolmrLogger.error(this, "getDataAttestazioneAllaDichiarazione in TipoAttestazioneDAO - Generic Exception: "+ex+"\n");
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
        SolmrLogger.error(this, "getDataAttestazioneAllaDichiarazione in TipoAttestazioneDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) 
      {
        SolmrLogger.error(this, "getDataAttestazioneAllaDichiarazione in TipoAttestazioneDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getDataAttestazioneAllaDichiarazione method in TipoAttestazioneDAO\n");
    return result;
  }



	/**
	 * Metodo per verificare la presenza di attestazioni dichiarate
	 *
	 * @param codiceFotografiaTerreni
	 * @return boolean
	 * @throws DataAccessException
	 */
	public boolean isAttestazioneDichiarata(String codiceFotografiaTerreni) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating isAttestazioneDichiarata method in TipoAttestazioneDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean isAttestazioneDichiarata = false;

		try {
			SolmrLogger.debug(this, "Creating db-connection in isAttestazioneDichiarata method in TipoAttestazioneDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in isAttestazioneDichiarata method in TipoAttestazioneDAO and it values: "+conn+"\n");

			String query = " " +
					"SELECT " +
					"       ID_ATTESTAZIONE_DICHIARATA " +
					"FROM   DB_ATTESTAZIONE_DICHIARATA AD," +
					"       DB_TIPO_ATTESTAZIONE TA " +
					"WHERE  CODICE_FOTOGRAFIA_TERRENI = ? " +
					"AND    AD.ID_ATTESTAZIONE = TA.ID_ATTESTAZIONE " +
					"AND    TA.CODICE_ATTESTAZIONE = 'COND' " +
					"AND    TA.VOCE_MENU = 'DICHIARAZIONI' ";

			SolmrLogger.debug(this, "Value of parameter 1 [CODICE_FOTOGRAFIA_TERRENI] in isAttestazioneDichiarata method in TipoAttestazioneDAO: "+codiceFotografiaTerreni+"\n");

			stmt = conn.prepareStatement(query);

			stmt.setString(1, codiceFotografiaTerreni);

			SolmrLogger.debug(this, "Executing isAttestazioneDichiarata: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) {
				isAttestazioneDichiarata = true;
			}

			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "isAttestazioneDichiarata in TipoAttestazioneDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "isAttestazioneDichiarata in TipoAttestazioneDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "isAttestazioneDichiarata in TipoAttestazioneDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "isAttestazioneDichiarata in TipoAttestazioneDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated isAttestazioneDichiarata method in TipoAttestazioneDAO\n");
		return isAttestazioneDichiarata;
	}
	
	/**
	 * Metodo per verificare la presenza di attestazioni azienda
	 * 
	 * @param idAzienda
	 * @return
	 * @throws DataAccessException
	 */
	public boolean isAttestazioneAzienda(long idAzienda) throws DataAccessException {
    SolmrLogger.debug(this, "Invocating isAttestazioneAzienda method in TipoAttestazioneDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    boolean isAttestazioneAzienda = false;

    try {
      SolmrLogger.debug(this, "Creating db-connection in isAttestazioneAzienda method in TipoAttestazioneDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in isAttestazioneAzienda method in TipoAttestazioneDAO and it values: "+conn+"\n");

      String query = " " +
      		"SELECT ID_ATTESTAZIONE_AZIENDA " +
          "FROM   DB_ATTESTAZIONE_AZIENDA AA, " +
          "       DB_TIPO_ATTESTAZIONE TA " +
          "WHERE  ID_AZIENDA = ? " +
          "AND    AA.ID_ATTESTAZIONE = TA.ID_ATTESTAZIONE " +
          "AND    TA.CODICE_ATTESTAZIONE = 'COND' " +
          "AND    TA.VOCE_MENU = 'DICHIARAZIONI' ";

      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in isAttestazioneAzienda method in TipoAttestazioneDAO: "+idAzienda+"\n");

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idAzienda);

      SolmrLogger.debug(this, "Executing isAttestazioneAzienda: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      if(rs.next()) {
        isAttestazioneAzienda = true;
      }

      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "isAttestazioneAzienda in TipoAttestazioneDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "isAttestazioneAzienda in TipoAttestazioneDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "isAttestazioneAzienda in TipoAttestazioneDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "isAttestazioneAzienda in TipoAttestazioneDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated isAttestazioneAzienda method in TipoAttestazioneDAO\n");
    return isAttestazioneAzienda;
  }

	/**
	 * Metodo che si occupa di richiamare una procedura PL-SQL per calcolare le dichiarazioni che possono già essere selezionate dal sistema
	 *
	 * @param idAzienda
	 * @param idUtente
	 * @param voceMenu
	 * @throws SolmrException
	 * @throws DataAccessException
	 */
	public void aggiornaAttestazioniPlSql(Long idAzienda, Long idUtente, Long idDichiarazioneConsistenza, String codAttestazione) throws SolmrException, DataAccessException 
	{
	  /**
	   *  CARICA_ATTESTAZIONI(P_ID_AZIENDA IN     DB_AZIENDA.ID_AZIENDA%TYPE,
     *                      P_ID_UTENTE_AGGIORNAMENTO IN DB_ATTESTAZIONE_AZIENDA.ID_UTENTE_AGGIORNAMENTO%TYPE,
     *                      P_COD_ATTESTAZIONE                IN DB_TIPO_ATTESTAZIONE.CODICE_ATTESTAZIONE%TYPE,
     *                      P_ID_DICHIARAZIONE_CONSISTENZA IN DB_DICHIARAZIONE_CONSISTENZA.ID_DICHIARAZIONE_CONSISTENZA%TYPE,
     *                      P_CODERR     IN OUT VARCHAR2,
     *                      P_MSGERR    IN OUT VARCHAR2);
     */         
	  
		SolmrLogger.debug(this, "Invocating aggiornaAttestazioniPlSql method in TipoAttestazioneDAO\n");
    Connection conn = null;
    CallableStatement cs = null;
    String command = "{call PACK_AGGIORNA_ATTESTAZIONI.CARICA_ATTESTAZIONI(?,?,?,?,?,?)}";
    try 
    {
    	SolmrLogger.debug(this, "Creating db-connection in aggiornaAttestazioniPlSql method in TipoAttestazioneDAO\n");
		  conn = getDatasource().getConnection();
		  SolmrLogger.debug(this, "Created db-connection in aggiornaAttestazioniPlSql method in TipoAttestazioneDAO and it values: "+conn+"\n");

  		SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in aggiornaAttestazioniPlSql method in TipoAttestazioneDAO: "+idAzienda+"\n");
  		SolmrLogger.debug(this, "Value of parameter 2 [ID_UTENTE] in aggiornaAttestazioniPlSql method in TipoAttestazioneDAO: "+idUtente+"\n");
  		SolmrLogger.debug(this, "Value of parameter 3 [ID_DICHIARAZIONE_CONSISTENZA] in aggiornaAttestazioniPlSql method in TipoAttestazioneDAO: "+idDichiarazioneConsistenza+"\n");
  		SolmrLogger.debug(this, "Value of parameter 4 [CODICE_ATTESTAZIONE] in aggiornaAttestazioniPlSql method in TipoAttestazioneDAO: "+codAttestazione+"\n");
  
  		cs = conn.prepareCall(command);
  		cs.setLong(1, idAzienda.longValue());
  		cs.setLong(2, idUtente.longValue());
  		cs.setString(3, codAttestazione);
  		if(Validator.isNotEmpty(idDichiarazioneConsistenza))
      {
        cs.setLong(4, idDichiarazioneConsistenza.longValue());
      }
      else
      {
        cs.setString(4, null);
      }
  		cs.registerOutParameter(5,Types.VARCHAR);
  		cs.registerOutParameter(6,Types.VARCHAR);
  
  		cs.executeUpdate();
  		String msgErr = cs.getString(5);
  		String errorCode = cs.getString(6);

  		if(Validator.isNotEmpty(errorCode)) {
  			throw new SolmrException((String)AnagErrors.get("ERR_PLSQL")+errorCode + " - " + msgErr);
  		}
  		cs.close();
  		conn.close();
    }
    catch(SolmrException se) 
    {
    	SolmrLogger.error(this, "SolmrException in aggiornaAttestazioniPlSql in TipoAttestazioneDAO: "+se);
    	throw new SolmrException(se.getMessage());
    }
    catch(SQLException exc) 
    {
    	char a = '"';
    	String messaggioErrore = StringUtils.replace(exc.getMessage(), "'", "''");
    	messaggioErrore = StringUtils.replace(exc.getMessage(), System.getProperty("line.separator"), "\\n");
    	messaggioErrore = StringUtils.replace(exc.getMessage(), String.valueOf(a), " ");
    	SolmrLogger.error(this, "SQLException in aggiornaAttestazioniPlSql in TipoAttestazioneDAO: "+messaggioErrore);
    	throw new SolmrException((String)AnagErrors.get("ERR_PLSQL")+" "+ StringUtils.replace(messaggioErrore, System.getProperty("line.separator"), "\\n"));
    }
    catch(Exception ex) 
    {
    	SolmrLogger.error(this, "Generic Exception in aggiornaAttestazioniPlSql in TipoAttestazioneDAO: "+ex);
    	throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
    	try 
    	{
    		if(cs != null) {
    			cs.close();
    		}
    		if(conn != null) {
    			conn.close();
    		}
    	}
    	catch(SQLException exc) {
    		SolmrLogger.error(this, "SQLException while closing Statement and Connection in aggiornaAttestazioniPlSql in TipoAttestazioneDAO: "+exc);
    		throw new DataAccessException(exc.getMessage());
    	}
    	catch(Exception ex) {
    		SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in aggiornaAttestazioniPlSql in TipoAttestazioneDAO: "+ex);
    		throw new DataAccessException(ex.getMessage());
    	}
    }
    SolmrLogger.debug(this, "Invocated aggiornaAttestazioniPlSql method in TipoAttestazioneDAO\n");
	}

	/**
	 * Metodo che mi consente di eliminare fisicamente il legame tra le attestazioni e l'azienda
	 *
	 * @param idAzienda
	 * @param voceMenu
	 * @throws DataAccessException
	 */
	public void deleteAttestazioneAziendaByIdAzienda(Long idAzienda, String voceMenu) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating deleteAttestazioneAziendaByIdAzienda method in TipoAttestazioneDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in deleteAttestazioneAziendaByIdAzienda method in TipoAttestazioneDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in deleteAttestazioneAziendaByIdAzienda method in TipoAttestazioneDAO and it values: "+conn+"\n");

			String query  = " DELETE FROM  DB_ATTESTAZIONE_AZIENDA "+
                    		"        WHERE ID_AZIENDA = ? " +
                    		"        AND   ID_ATTESTAZIONE IN(SELECT ID_ATTESTAZIONE " +
                    		"                                 FROM   DB_TIPO_ATTESTAZIONE " +
                    		"                                 WHERE  VOCE_MENU = ?) ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing deleteAttestazioneAziendaByIdAzienda: "+query);

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in method deleteAttestazioneAziendaByIdAzienda in TipoAttestazioneDAO: "+idAzienda.longValue()+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [VOCE_MENU] in method deleteAttestazioneAziendaByIdAzienda in TipoAttestazioneDAO: "+voceMenu+"\n");
			stmt.setLong(1, idAzienda.longValue());
			stmt.setString(2, voceMenu);

			stmt.executeUpdate();

			stmt.close();
		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "SQLException in deleteAttestazioneAziendaByIdAzienda: "+exc);
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "Generic Exception in deleteAttestazioneAziendaByIdAzienda: "+ex);
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
				SolmrLogger.error(this, "SQLException while closing Statement and Connection in deleteAttestazioneAziendaByIdAzienda: "+exc);
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in deleteAttestazioneAziendaByIdAzienda: "+ex);
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated deleteAttestazioneAziendaByIdAzienda method in TipoAttestazioneDAO\n");
	}
	
	/**
	 * Lock su tabella DB_ATTESTAZIONE_AZIENDA
	 * 
	 * @param idAzienda
	 * @return
	 * @throws DataAccessException
	 */
  public boolean lockTableAttestazioneAzienda(long idAzienda) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean flagLock = false;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[TipoAttestazioneDAO::lockTableAttestazioneAzienda] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append(" SELECT * " +
              "FROM DB_ATTESTAZIONE_AZIENDA " +
              "WHERE ID_AZIENDA  = ? " +
              "FOR UPDATE ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[TipoAttestazioneDAO::lockTableAttestazioneAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice,idAzienda);
      
      
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        flagLock = true;
      }
      
      return flagLock;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("flagLock", flagLock) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[TipoAttestazioneDAO::lockTableAttestazioneAzienda] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[TipoAttestazioneDAO::lockTableAttestazioneAzienda] END.");
    }
  }

	/**
	 * Metodo che mi permette di inserire un record nella tabella DB_ATTESTAZIONE_AZIENDA
	 *
	 * @param attestazioneAziendaVO
	 * @return java.lang.Long
	 * @throws DataAccessException
	 */
	public Long insertAttestazioneAzienda(AttestazioneAziendaVO attestazioneAziendaVO) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating insertAttestazioneAzienda method in TipoAttestazioneDAO\n");
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    Long idAttestazioneAzienda = null;

	    try {
	    	idAttestazioneAzienda = getNextPrimaryKey(SolmrConstants.SEQ_ATTESTAZIONE_AZIENDA);
	    	SolmrLogger.debug(this, "Creating db-connection in insertAttestazioneAzienda method in TipoAttestazioneDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in insertAttestazioneAzienda method in TipoAttestazioneDAO and it values: "+conn+"\n");

			String query = " INSERT INTO DB_ATTESTAZIONE_AZIENDA " +
			   			   "        (ID_ATTESTAZIONE_AZIENDA, " +
			   			   "         ID_AZIENDA, " +
			   			   "         ID_ATTESTAZIONE, " +
			   			   "         DATA_ULTIMO_AGGIORNAMENTO, " +
			   			   "         ID_UTENTE_AGGIORNAMENTO) " +
			   			   " VALUES  (?, ?, ?, ?, ?) ";
			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing insertAttestazioneAzienda: "+query);

			SolmrLogger.debug(this, "Value of parameter 1 [ID_ATTESTAZIONE_AZIENDA] in method insertAttestazioneAzienda in TipoAttestazioneDAO: "+idAttestazioneAzienda.longValue()+"\n");
			stmt.setLong(1, idAttestazioneAzienda.longValue());
			SolmrLogger.debug(this, "Value of parameter 2 [ID_AZIENDA] in method insertAttestazioneAzienda in TipoAttestazioneDAO: "+attestazioneAziendaVO.getIdAzienda().longValue()+"\n");
			stmt.setLong(2, attestazioneAziendaVO.getIdAzienda().longValue());
			SolmrLogger.debug(this, "Value of parameter 3 [ID_ATTESTAZIONE] in method insertAttestazioneAzienda in TipoAttestazioneDAO: "+attestazioneAziendaVO.getIdAttestazione().longValue()+"\n");
			stmt.setLong(3, attestazioneAziendaVO.getIdAttestazione().longValue());
			SolmrLogger.debug(this, "Value of parameter 4 [DATA_ULTIMO_AGGIORNAMENTO] in method insertAttestazioneAzienda in TipoAttestazioneDAO: "+attestazioneAziendaVO.getDataUltimoAggiornamento()+"\n");
			stmt.setTimestamp(4, new Timestamp(attestazioneAziendaVO.getDataUltimoAggiornamento().getTime()));
			SolmrLogger.debug(this, "Value of parameter 5 [ID_UTENTE_AGGIORNAMENTO] in method insertAttestazioneAzienda in TipoAttestazioneDAO: "+attestazioneAziendaVO.getIdUtenteAggiornamento().longValue()+"\n");
			stmt.setLong(5, attestazioneAziendaVO.getIdUtenteAggiornamento().longValue());

			stmt.executeUpdate();

			stmt.close();
	    }
	    catch(SQLException exc) {
	    	SolmrLogger.error(this, "insertAttestazioneAzienda in TipoAttestazioneDAO - SQLException: "+exc);
	    	throw new DataAccessException(exc.getMessage());
	    }
	    catch (Exception ex) {
	    	SolmrLogger.error(this, "insertAttestazioneAzienda in TipoAttestazioneDAO - Generic Exception: "+ex);
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
	    		SolmrLogger.error(this, "insertAttestazioneAzienda in TipoAttestazioneDAO - SQLException while closing Statement and Connection: "+exc);
	    		throw new DataAccessException(exc.getMessage());
	    	}
	    	catch(Exception ex) {
	    		SolmrLogger.error(this, "insertAttestazioneAzienda in TipoAttestazioneDAO - Generic Exception while closing Statement and Connection: "+ex);
	    		throw new DataAccessException(ex.getMessage());
	    	}
	    }
	    SolmrLogger.debug(this, "Invocated insertAttestazioneAzienda method in TipoAttestazioneDAO\n");
	    return idAttestazioneAzienda;
	}

	/**
	 * Metodo utilizzato per eliminare le attestazioni legate ad una dichiarazione di consistenza
	 *
	 * @param codiceFotografiaTerreni
	 * @param voceMenu
	 * @throws DataAccessException
	 */
	public void deleteAttestazioneDichiarataByCodiceFotografiaTerreni(String codiceFotografiaTerreni, String voceMenu) throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating deleteAttestazioneDichiarataByCodiceFotografiaTerreni method in TipoAttestazioneDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in deleteAttestazioneDichiarataByCodiceFotografiaTerreni method in TipoAttestazioneDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in deleteAttestazioneDichiarataByCodiceFotografiaTerreni method in TipoAttestazioneDAO and it values: "+conn+"\n");

			String query = "";
			if(Validator.isNotEmpty(voceMenu)) 
			{
				query  = " DELETE FROM  DB_ATTESTAZIONE_DICHIARATA "+
        						"        WHERE CODICE_FOTOGRAFIA_TERRENI = ? " +
        						"        AND   ID_ATTESTAZIONE IN(SELECT ID_ATTESTAZIONE " +
        						"                                 FROM   DB_TIPO_ATTESTAZIONE " +
        						"                                 WHERE  VOCE_MENU = ?) ";
			}
			else 
			{
				query  = " DELETE FROM  DB_ATTESTAZIONE_DICHIARATA "+
						"        WHERE CODICE_FOTOGRAFIA_TERRENI = ? ";
			}


			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing deleteAttestazioneDichiarataByCodiceFotografiaTerreni: "+query);

			SolmrLogger.debug(this, "Value of parameter 1 [CODICE_FOTOGRAFIA_TERRENI] in method deleteAttestazioneDichiarataByCodiceFotografiaTerreni in TipoAttestazioneDAO: "+codiceFotografiaTerreni+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [VOCE_MENU] in method deleteAttestazioneDichiarataByCodiceFotografiaTerreni in TipoAttestazioneDAO: "+voceMenu+"\n");

			stmt.setString(1, codiceFotografiaTerreni);
			if(Validator.isNotEmpty(voceMenu)) {
				stmt.setString(2, voceMenu);
			}

			stmt.executeUpdate();

			stmt.close();
		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "SQLException in deleteAttestazioneDichiarataByCodiceFotografiaTerreni: "+exc);
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "Generic Exception in deleteAttestazioneDichiarataByCodiceFotografiaTerreni: "+ex);
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
				SolmrLogger.error(this, "SQLException while closing Statement and Connection in deleteAttestazioneDichiarataByCodiceFotografiaTerreni: "+exc);
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in deleteAttestazioneDichiarataByCodiceFotografiaTerreni: "+ex);
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated deleteAttestazioneDichiarataByCodiceFotografiaTerreni method in TipoAttestazioneDAO\n");
	}
	
	
	
	
	public void storicizzaAttDichiarata(Long codiceFotografiaTerreni, String voceMenu) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[TipoAttestazioneDAO::storicizzaAttDichiarata] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
      		"UPDATE DB_ATTESTAZIONE_DICHIARATA " +
      		"SET  DATA_FINE_VALIDITA = SYSDATE " +
          "WHERE  CODICE_FOTOGRAFIA_TERRENI = ? " +
      		"AND  DATA_FINE_VALIDITA IS NULL " +
          "AND    ID_ATTESTAZIONE IN(SELECT ID_ATTESTAZIONE " +
          "                          FROM   DB_TIPO_ATTESTAZIONE " +
          "                          WHERE  VOCE_MENU = ? ) ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[TipoAttestazioneDAO::storicizzaAttDichiarata] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      stmt.setLong(++indice, codiceFotografiaTerreni.longValue());
      stmt.setString(++indice, voceMenu);      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("codiceFotografiaTerreni", codiceFotografiaTerreni), 
        new Parametro("voceMenu", voceMenu) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[TipoAttestazioneDAO::storicizzaAttDichiarata] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[TipoAttestazioneDAO::storicizzaAttDichiarata] END.");
    }
  }
	
	public void storicizzaAttDichiarataCodAtt(Long codiceFotografiaTerreni, String codiceAttestazione) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger.debug(this, "[TipoAttestazioneDAO::storicizzaAttDichiarataCodAtt] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
          "UPDATE DB_ATTESTAZIONE_DICHIARATA " +
          "SET  DATA_FINE_VALIDITA = SYSDATE " +
          "WHERE  CODICE_FOTOGRAFIA_TERRENI = ? " +
          "AND  DATA_FINE_VALIDITA IS NULL " +
          "AND    ID_ATTESTAZIONE IN(SELECT ID_ATTESTAZIONE " +
          "                          FROM   DB_TIPO_ATTESTAZIONE " +
          "                          WHERE  CODICE_ATTESTAZIONE = ? ) ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[TipoAttestazioneDAO::storicizzaAttDichiarataCodAtt] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      stmt.setLong(++indice, codiceFotografiaTerreni.longValue());
      stmt.setString(++indice, codiceAttestazione);      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("codiceFotografiaTerreni", codiceFotografiaTerreni), 
        new Parametro("codiceAttestazione", codiceAttestazione) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
              "[TipoAttestazioneDAO::storicizzaAttDichiarataCodAtt] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[TipoAttestazioneDAO::storicizzaAttDichiarataCodAtt] END.");
    }
  }
	
	/**
   * Lock su tabella DB_ATTESTAZIONE_DICHIARATA
   * 
   * @param idCodiceFotografiaTerreni
   * @return
   * @throws DataAccessException
   */
  public boolean lockTableAttestazioneDichiarata(String codiceFotografiaTerreni) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean flagLock = false;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[TipoAttestazioneDAO::lockTableAttestazioneDichiarata] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append(" SELECT * " +
              "FROM DB_ATTESTAZIONE_DICHIARATA " +
              "WHERE CODICE_FOTOGRAFIA_TERRENI  = ? " +
              "FOR UPDATE ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[TipoAttestazioneDAO::lockTableAttestazioneDichiarata] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setString(++indice, codiceFotografiaTerreni);
      
      
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        flagLock = true;
      }
      
      return flagLock;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("flagLock", flagLock) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("codiceFotografiaTerreni", codiceFotografiaTerreni)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[TipoAttestazioneDAO::lockTableAttestazioneDichiarata] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[TipoAttestazioneDAO::lockTableAttestazioneDichiarata] END.");
    }
  }

	/**
	 * Metodo che mi permette di inserire un record nella tabella DB_ATTESTAZIONE_DICHIARATA
	 *
	 * @param attestazioneDichiarataVO
	 * @return java.lang.Long
	 * @throws DataAccessException
	 */
	public Long insertAttestazioneDichiarata(AttestazioneDichiarataVO attestazioneDichiarataVO) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating insertAttestazioneDichiarata method in TipoAttestazioneDAO\n");
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    Long idAttestazioneDichiarata = null;

	    try {
	    	idAttestazioneDichiarata = getNextPrimaryKey(SolmrConstants.SEQ_ATTESTAZIONE_DICHIARATA);
	    	SolmrLogger.debug(this, "Creating db-connection in insertAttestazioneDichiarata in TipoAttestazioneDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in insertAttestazioneDichiarata method in TipoAttestazioneDAO and it values: "+conn+"\n");

			String query = " INSERT INTO DB_ATTESTAZIONE_DICHIARATA " +
			   			   "        (ID_ATTESTAZIONE_DICHIARATA, " +
			   			   "         CODICE_FOTOGRAFIA_TERRENI, " +
			   			   "         ID_ATTESTAZIONE, " +
			   			   "         DATA_AGGIORNAMENTO, " +
			   			   "         ID_UTENTE_AGGIORNAMENTO, " +
			   			   "         DATA_INIZIO_VALIDITA) " +
			   			   " VALUES  (?, ?, ?, ?, ?, ?) ";
			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing insertAttestazioneDichiarata: "+query);

			SolmrLogger.debug(this, "Value of parameter 1 [ID_ATTESTAZIONE_DICHIARATA] in method insertAttestazioneDichiarata in TipoAttestazioneDAO: "+idAttestazioneDichiarata.longValue()+"\n");
			stmt.setLong(1, idAttestazioneDichiarata.longValue());
			SolmrLogger.debug(this, "Value of parameter 2 [CODICE_FOTOGRAFIA_TERRENI] in method insertAttestazioneDichiarata in TipoAttestazioneDAO: "+attestazioneDichiarataVO.getCodiceFotografiaTerreni()+"\n");
			stmt.setString(2, attestazioneDichiarataVO.getCodiceFotografiaTerreni());
			SolmrLogger.debug(this, "Value of parameter 3 [ID_ATTESTAZIONE] in method insertAttestazioneDichiarata in TipoAttestazioneDAO: "+attestazioneDichiarataVO.getIdAttestazione().longValue()+"\n");
			stmt.setLong(3, attestazioneDichiarataVO.getIdAttestazione().longValue());
			SolmrLogger.debug(this, "Value of parameter 4 [DATA_AGGIORNAMENTO] in method insertAttestazioneDichiarata in TipoAttestazioneDAO: "+attestazioneDichiarataVO.getDataAggiornamento()+"\n");
			if(attestazioneDichiarataVO.getDataAggiornamento() != null) {
				stmt.setTimestamp(4, new Timestamp(attestazioneDichiarataVO.getDataAggiornamento().getTime()));
			}
			else {
				stmt.setString(4, null);
			}
			SolmrLogger.debug(this, "Value of parameter 5 [ID_UTENTE_AGGIORNAMENTO] in method insertAttestazioneDichiarata in TipoAttestazioneDAO: "+attestazioneDichiarataVO.getIdUtenteAggiornamento()+"\n");
			if(attestazioneDichiarataVO.getIdUtenteAggiornamento() != null) {
				stmt.setLong(5, attestazioneDichiarataVO.getIdUtenteAggiornamento().longValue());
			}
			else {
				stmt.setString(5, null);
			}
			stmt.setTimestamp(6, new Timestamp(attestazioneDichiarataVO.getDataInizioValidita().getTime()));
      

			stmt.executeUpdate();

			stmt.close();
	    }
	    catch(SQLException exc) {
	    	SolmrLogger.error(this, "insertAttestazioneDichiarata in TipoAttestazioneDAO - SQLException: "+exc);
	    	throw new DataAccessException(exc.getMessage());
	    }
	    catch (Exception ex) {
	    	SolmrLogger.error(this, "insertAttestazioneDichiarata in TipoAttestazioneDAO - Generic Exception: "+ex);
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
	    		SolmrLogger.error(this, "insertAttestazioneDichiarata in TipoAttestazioneDAO - SQLException while closing Statement and Connection: "+exc);
	    		throw new DataAccessException(exc.getMessage());
	    	}
	    	catch(Exception ex) {
	    		SolmrLogger.error(this, "insertAttestazioneDichiarata in TipoAttestazioneDAO - Generic Exception while closing Statement and Connection: "+ex);
	    		throw new DataAccessException(ex.getMessage());
	    	}
	    }
	    SolmrLogger.debug(this, "Invocated insertAttestazioneDichiarata method in TipoAttestazioneDAO\n");
	    return idAttestazioneDichiarata;
	}
	
	
	/**
	 * restituisce la prima attestazione relativa all'azienda
	 * per recuperare i dati dell'aggiornamento identici per tutte!!!
	 * 
	 * 
	 * @param idAzienda
	 * @return
	 * @throws DataAccessException
	 */
	public AttestazioneAziendaVO getFirstAttestazioneAzienda(long idAzienda) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    AttestazioneAziendaVO aaVO = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[TipoAttestazioneDAO::getFirstAttestazioneAzienda] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append(" " +
          		"SELECT AA.ID_UTENTE_AGGIORNAMENTO, " +
          		"       AA.DATA_ULTIMO_AGGIORNAMENTO, " +
          		"         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
                "          || ' ' " + 
                "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
                "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
                "         WHERE AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
            		"       AS UTENTE_DENOMINAZIONE, " +
            		"       (SELECT PVU.DENOMINAZIONE " +
                "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
                "        WHERE AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
                "       AS ENTE_DENOMINAZIONE " +
                "FROM   DB_ATTESTAZIONE_AZIENDA AA " +
           //     "       PAPUA_V_UTENTE_LOGIN PVU " +
                "WHERE  AA.ID_AZIENDA  = ? ");
              //  "AND    AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[TipoAttestazioneDAO::getFirstAttestazioneAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice,idAzienda);
      
      
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        aaVO = new AttestazioneAziendaVO();
        aaVO.setIdUtenteAggiornamento(checkLongNull(rs.getString("ID_UTENTE_AGGIORNAMENTO")));
        aaVO.setDataUltimoAggiornamento(rs.getTimestamp("DATA_ULTIMO_AGGIORNAMENTO"));
        aaVO.setUtenteUltimaModifica(rs.getString("UTENTE_DENOMINAZIONE"));
        aaVO.setEnteUltimaModifica(rs.getString("ENTE_DENOMINAZIONE"));
        
      }
      
      return aaVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("aaVO", aaVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[TipoAttestazioneDAO::getFirstAttestazioneAzienda] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[TipoAttestazioneDAO::getFirstAttestazioneAzienda] END.");
    }
  }
	
	
	/**
   * restituisce la prima attestazione relativa alla dichiarazione
   * per recuperare i dati dell'aggiornamento identici per tutte!!!
   * 
   * 
   * @param codiceFotografia
   * @return
   * @throws DataAccessException
   */
  public AttestazioneDichiarataVO getFirstAttestazioneDichiarata(long codiceFotografia) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    AttestazioneDichiarataVO aaVO = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[TipoAttestazioneDAO::getFirstAttestazioneDichiarata] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append(" " +
              "SELECT AD.ID_UTENTE_AGGIORNAMENTO, " +
              "       AD.DATA_AGGIORNAMENTO, " +
              "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
              "          || ' ' " + 
              "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
              "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
              "         WHERE AD.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
              "       AS UTENTE_DENOMINAZIONE, " +
              "       (SELECT PVU.DENOMINAZIONE " +
              "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
              "        WHERE AD.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
              "       AS ENTE_DENOMINAZIONE " +
              "FROM   DB_ATTESTAZIONE_DICHIARATA AD " +
           //   "       PAPUA_V_UTENTE_LOGIN PVU " +
              "WHERE  AD.CODICE_FOTOGRAFIA_TERRENI  = ? ");
          //    "AND    AD.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[TipoAttestazioneDAO::getFirstAttestazioneDichiarata] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice,codiceFotografia);
      
      
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        aaVO = new AttestazioneDichiarataVO();
        aaVO.setIdUtenteAggiornamento(checkLongNull(rs.getString("ID_UTENTE_AGGIORNAMENTO")));
        aaVO.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO"));
        aaVO.setUtenteUltimaModifica(rs.getString("UTENTE_DENOMINAZIONE"));
        aaVO.setEnteUltimaModifica(rs.getString("ENTE_DENOMINAZIONE"));
        
      }
      
      return aaVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("aaVO", aaVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("codiceFotografia", codiceFotografia)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[TipoAttestazioneDAO::getFirstAttestazioneDichiarata] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[TipoAttestazioneDAO::getFirstAttestazioneDichiarata] END.");
    }
  }
  
  
  public PlSqlCodeDescription raggruppaAttestazioniPlSql(long idDichiarazioneConsistenza) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    CallableStatement cs = null;
    StringBuffer queryBuf = null;
    PlSqlCodeDescription plqObj = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[TipoAttestazioniDAO::raggruppaAttestazioniPlSql] BEGIN.");
      /***
       * PROCEDURE RAGGRUPPA_ATTESTAZIONI (P_ID_DICHIARAZIONE_CONSISTENZA IN DB_DICHIARAZIONE_CONSISTENZA.ID_DICHIARAZIONE_CONSISTENZA%TYPE,
       *                                   P_COD_ERR                  IN OUT VARCHAR2,
       *                                   P_MSG_ERR                  IN OUT VARCHAR2)
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{call PACK_AGGIORNA_ATTESTAZIONI.RAGGRUPPA_ATTESTAZIONI(?,?,?)}");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[TipoAttestazioniDAO::raggruppaAttestazioniPlSql] Query="
                + query);
      }
      
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.setLong(1, idDichiarazioneConsistenza);      
      cs.registerOutParameter(2,Types.VARCHAR); //codice errore
      cs.registerOutParameter(3,Types.VARCHAR); //msg errore
      
      cs.executeUpdate();
      
      plqObj = new PlSqlCodeDescription();  
      plqObj.setDescription(cs.getString(2)); //codice errore
      plqObj.setOtherdescription(cs.getString(3)); //msg errore
      
      
      return plqObj;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("plqObj", plqObj) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[TipoAttestazioniDAO::raggruppaAttestazioniPlSql] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      closePlSql(cs, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[TipoAttestazioniDAO::raggruppaAttestazioniPlSql] END.");
    }
  }
  
  /**
   * ritorna le date delel variazioni al piano della dichiarazione degli allegati
   * 
   * 
   * 
   * @param codiceFotografia
   * @return
   * @throws DataAccessException
   */
  public Vector<java.util.Date> getDateVariazioniAllegati(long codiceFotografia) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<java.util.Date> vDateVariazioni = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[TipoAttestazioneDAO::getDateVariazioniAllegati] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append(" " +
              "SELECT DISTINCT TRUNC(AD.DATA_INIZIO_VALIDITA) AS DATA_INIZIO_VALIDITA " +
              "FROM   DB_ATTESTAZIONE_DICHIARATA AD " +
              "WHERE  AD.CODICE_FOTOGRAFIA_TERRENI = ? " +
              "ORDER BY TRUNC(AD.DATA_INIZIO_VALIDITA) DESC ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[TipoAttestazioneDAO::getDateVariazioniAllegati] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice,codiceFotografia);
      
      
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(vDateVariazioni == null)
        {
          vDateVariazioni = new Vector<java.util.Date>();
        }
       
        vDateVariazioni.add(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
      }
      
      return vDateVariazioni;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vDateVariazioni", vDateVariazioni) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("codiceFotografia", codiceFotografia)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[TipoAttestazioneDAO::getDateVariazioniAllegati] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[TipoAttestazioneDAO::getDateVariazioniAllegati] END.");
    }
  }

	
}
