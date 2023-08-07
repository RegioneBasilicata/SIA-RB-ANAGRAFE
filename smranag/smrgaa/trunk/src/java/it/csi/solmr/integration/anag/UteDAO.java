package it.csi.solmr.integration.anag;

import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.ProvinciaVO;
import it.csi.solmr.dto.UtenteIrideVO;
import it.csi.solmr.dto.anag.UteAtecoSecondariVO;
import it.csi.solmr.dto.anag.UteVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

public class UteDAO extends it.csi.solmr.integration.BaseDAO {


	public UteDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public UteDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo che mi restituisce l'elenco delle ute relative all'azienda agricola selezionata
	 * @param idAzienda
	 * @param onlyActive
	 * @param orderBy
	 * @return java.util.Vector
	 * @throws DataAccessException
	 */
	public Vector<UteVO> getListUteByIdAzienda(Long idAzienda, boolean onlyActive, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListUteByIdAzienda method in UteDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<UteVO> elencoUte = new Vector<UteVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListUteByIdAzienda method in UteDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListUteByIdAzienda method in UteDAO and it values: "+conn+"\n");

			String query = " SELECT U.ID_UTE, " +
			               "        U.ID_AZIENDA, " +
			               "        U.DENOMINAZIONE, " +
			               "        U.INDIRIZZO, " +
			               "        U.COMUNE, " +
			               "        C.DESCOM, " +
			               "        C.FLAG_ESTERO, " +
			               "        P.SIGLA_PROVINCIA, " +
			               "        U.CAP, " +
			               "        U.ID_ZONA_ALTIMETRICA, " +	
			               "        TZA.DESCRIZIONE AS DESC_ALTIMETRICA, " +
			               "        U.ID_ATTIVITA_ATECO, " +
			               "        TAA.DESCRIZIONE AS DESC_ATECO, " +
			               "        U.TELEFONO, " +
			               "        U.FAX, " +
			               "        U.NOTE, " +
			               "        U.DATA_INIZIO_ATTIVITA, " + 
			               "        U.DATA_FINE_ATTIVITA, " +
			               "        U.CAUSALE_CESSAZIONE, " +
			               "        U.DATA_AGGIORNAMENTO, " +
			               "        U.ID_UTENTE_AGGIORNAMENTO, " +
			               "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
			               "          || ' ' " + 
			               "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
			               "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
			               "         WHERE U.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
			               "        AS DENOM_UTENTE, " +
			               "       (SELECT PVU.DENOMINAZIONE " +
			               "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
			               "        WHERE U.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
			               "        AS DESCRIZIONE_ENTE_APPARTENENZA, " +
			               "        U.MOTIVO_MODIFICA, " +	
			               "        U.ID_ATTIVITA_OTE, " +
			               "        TAO.DESCRIZIONE AS DESC_OTE, " +
			               "        U.TIPO_SEDE " +
			               " FROM   DB_UTE U, " +
			               "        COMUNE C, " +
			               "        PROVINCIA P, " +
			               "        DB_TIPO_ZONA_ALTIMETRICA TZA, " +
			               "        DB_TIPO_ATTIVITA_ATECO TAA, " +
			               "        DB_TIPO_ATTIVITA_OTE TAO " +
			              // "        PAPUA_V_UTENTE_LOGIN PVU " +
			               " WHERE  U.ID_AZIENDA = ? " +
			               " AND    U.COMUNE = C.ISTAT_COMUNE " +
			               " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
			               " AND    U.ID_ZONA_ALTIMETRICA = TZA.ID_ZONA_ALTIMETRICA(+) " +
			               " AND    U.ID_ATTIVITA_ATECO = TAA.ID_ATTIVITA_ATECO(+) " +
			               " AND    U.ID_ATTIVITA_OTE = TAO.ID_ATTIVITA_OTE(+) ";
			              // " AND    U.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN ";
			
			if(onlyActive) {
				query += "   AND    U.DATA_FINE_ATTIVITA IS NULL ";
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

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListUteByIdAzienda method in UteDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ONLY_ACTIVE] in getListUteByIdAzienda method in UteDAO: "+onlyActive+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ORDER_BY] in getListUteByIdAzienda method in UteDAO: "+orderBy+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());

			SolmrLogger.debug(this, "Executing getListUteByIdAzienda: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				UteVO uteVO = new UteVO();
				uteVO.setIdUte(new Long(rs.getLong("ID_UTE")));
				uteVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
				uteVO.setDenominazione(rs.getString("DENOMINAZIONE"));
				uteVO.setIndirizzo(rs.getString("INDIRIZZO"));
				ComuneVO comuneUte = new ComuneVO();
				comuneUte.setIstatComune(rs.getString("COMUNE"));
				comuneUte.setDescom(rs.getString("DESCOM"));
				comuneUte.setFlagEstero(rs.getString("FLAG_ESTERO"));
				ProvinciaVO provinciaVO = new ProvinciaVO();
				provinciaVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));
				comuneUte.setProvinciaVO(provinciaVO);
				uteVO.setComuneUte(comuneUte);
				uteVO.setCap(rs.getString("CAP"));
				if(Validator.isNotEmpty(rs.getString("ID_ZONA_ALTIMETRICA"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_ZONA_ALTIMETRICA")), rs.getString("DESC_ALTIMETRICA"));
					uteVO.setTipoZonaAltimetrica(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_ATTIVITA_ATECO"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_ATTIVITA_ATECO")), rs.getString("DESC_ATECO"));
					uteVO.setTipoAttivitaATECO(code);
				}
				uteVO.setTelefono(rs.getString("TELEFONO"));
				uteVO.setFax(rs.getString("FAX"));
				uteVO.setNote(rs.getString("NOTE"));
				uteVO.setDataInizioAttivita(rs.getDate("DATA_INIZIO_ATTIVITA"));
				uteVO.setDataFineAttivita(rs.getDate("DATA_FINE_ATTIVITA"));
				uteVO.setCausaleCessazione(rs.getString("CAUSALE_CESSAZIONE"));
				uteVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
				UtenteIrideVO utenteIrideVO = new UtenteIrideVO();
				utenteIrideVO.setIdUtente(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				String utenteModifica = rs.getString("DENOM_UTENTE");
				utenteIrideVO.setDenominazione(utenteModifica);
				uteVO.setUtenteUltimaModifica(utenteModifica);
				String enteModifica = rs.getString("DESCRIZIONE_ENTE_APPARTENENZA");
				utenteIrideVO.setDescrizioneEnteAppartenenza(enteModifica);
				uteVO.setEnteUltimaModifica(enteModifica);
				uteVO.setDatiUtenteAggiornamento(utenteIrideVO);
				uteVO.setMotivoModifica(rs.getString("MOTIVO_MODIFICA"));
				if(Validator.isNotEmpty(rs.getString("ID_ATTIVITA_OTE"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_ATTIVITA_OTE")), rs.getString("DESC_OTE"));
					uteVO.setTipoAttivitaOTE(code);
				}
				uteVO.setTipoSede(rs.getString("TIPO_SEDE"));
				elencoUte.add(uteVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListUteByIdAzienda in UteDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListUteByIdAzienda in UteDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListUteByIdAzienda in UteDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListUteByIdAzienda in UteDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListUteByIdAzienda method in UteDAO\n");
		return elencoUte;
	}
	
	/**
	 * Metodo per estrarre l'unità produttiva a partire dalla sua chiave primaria
	 * 
	 * @param idUte
	 * @return it.csi.solmr.dto.anag.UteVO
	 * @throws DataAccessException
	 */
	public UteVO findUteByPrimaryKey(Long idUte) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating findUteByPrimaryKey method in UteDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		UteVO uteVO = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in findUteByPrimaryKey method in UteDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in findUteByPrimaryKey method in UteDAO and it values: "+conn+"\n");

			String query = " SELECT U.ID_UTE, " +
			               "        U.ID_AZIENDA, " +
			               "        U.DENOMINAZIONE, " +
			               "        U.INDIRIZZO, " +
			               "        U.COMUNE, " +
			               "        C.DESCOM, " +
			               "        C.FLAG_ESTERO, " +
			               "        P.SIGLA_PROVINCIA, " +
			               "        U.CAP, " +
			               "        U.ID_ZONA_ALTIMETRICA, " +	
			               "        TZA.DESCRIZIONE AS DESC_ALTIMETRICA, " +
			               "        U.ID_ATTIVITA_ATECO, " +
			               "        TAA.DESCRIZIONE AS DESC_ATECO, " +
			               "        U.TELEFONO, " +
			               "        U.FAX, " +
			               "        U.NOTE, " +
			               "        U.DATA_INIZIO_ATTIVITA, " + 
			               "        U.DATA_FINE_ATTIVITA, " +
			               "        U.CAUSALE_CESSAZIONE, " +
			               "        U.DATA_AGGIORNAMENTO, " +
			               "        U.ID_UTENTE_AGGIORNAMENTO, " +
			               "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
			               "          || ' ' " + 
			               "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
			               "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
			               "         WHERE U.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
			               "        AS DENOM_UTENTE, " +
			               "       (SELECT PVU.DENOMINAZIONE " +
			               "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
			               "        WHERE U.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
			               "        AS DESCRIZIONE_ENTE_APPARTENENZA, " + 
			               "        U.MOTIVO_MODIFICA, " +	
			               "        U.ID_ATTIVITA_OTE, " +
			               "        TAO.DESCRIZIONE AS DESC_OTE, " +
			               "        U.TIPO_SEDE " +
			               " FROM   DB_UTE U, " +
			               "        COMUNE C, " +
			               "        DB_TIPO_ZONA_ALTIMETRICA TZA, " +
			               "        DB_TIPO_ATTIVITA_ATECO TAA, " +
			               "        DB_TIPO_ATTIVITA_OTE TAO, " +
			               //"        PAPUA_V_UTENTE_LOGIN PVU, " +
			               "        PROVINCIA P " +
			               " WHERE  U.ID_UTE = ? " +
			               " AND    U.COMUNE = C.ISTAT_COMUNE " +
			               " AND    U.ID_ZONA_ALTIMETRICA = TZA.ID_ZONA_ALTIMETRICA(+) " +
			               " AND    U.ID_ATTIVITA_ATECO = TAA.ID_ATTIVITA_ATECO(+) " +
			               " AND    U.ID_ATTIVITA_OTE = TAO.ID_ATTIVITA_OTE(+) " +
			               //" AND    U.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
			               " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA ";

			SolmrLogger.debug(this, "Value of parameter 1 [ID_UTE] in findUteByPrimaryKey method in UteDAO: "+idUte+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idUte.longValue());

			SolmrLogger.debug(this, "Executing findUteByPrimaryKey: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) {
				uteVO = new UteVO();
				uteVO.setIdUte(new Long(rs.getLong("ID_UTE")));
				uteVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
				uteVO.setDenominazione(rs.getString("DENOMINAZIONE"));
				uteVO.setIndirizzo(rs.getString("INDIRIZZO"));
				uteVO.setComune(rs.getString("COMUNE"));
				ComuneVO comuneUte = new ComuneVO();
				comuneUte.setIstatComune(rs.getString("COMUNE"));
				comuneUte.setDescom(rs.getString("DESCOM"));
				comuneUte.setFlagEstero(rs.getString("FLAG_ESTERO"));
				if(Validator.isNotEmpty(rs.getString("SIGLA_PROVINCIA"))) {
					ProvinciaVO provinciaVO = new ProvinciaVO();
					provinciaVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));
					comuneUte.setProvinciaVO(provinciaVO);
				}
				uteVO.setComuneUte(comuneUte);
				uteVO.setCap(rs.getString("CAP"));
				if(Validator.isNotEmpty(rs.getString("ID_ZONA_ALTIMETRICA"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_ZONA_ALTIMETRICA")), rs.getString("DESC_ALTIMETRICA"));
					uteVO.setTipoZonaAltimetrica(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_ATTIVITA_ATECO"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_ATTIVITA_ATECO")), rs.getString("DESC_ATECO"));
					uteVO.setTipoAttivitaATECO(code);
				}
				uteVO.setTelefono(rs.getString("TELEFONO"));
				uteVO.setFax(rs.getString("FAX"));
				uteVO.setNote(rs.getString("NOTE"));
				uteVO.setDataInizioAttivita(rs.getDate("DATA_INIZIO_ATTIVITA"));
				uteVO.setDataFineAttivita(rs.getDate("DATA_FINE_ATTIVITA"));
				uteVO.setCausaleCessazione(rs.getString("CAUSALE_CESSAZIONE"));
				uteVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
				UtenteIrideVO utenteIrideVO = new UtenteIrideVO();
				utenteIrideVO.setIdUtente(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				utenteIrideVO.setDenominazione(rs.getString("DENOM_UTENTE"));
				utenteIrideVO.setDescrizioneEnteAppartenenza(rs.getString("DESCRIZIONE_ENTE_APPARTENENZA"));
				uteVO.setDatiUtenteAggiornamento(utenteIrideVO);
				uteVO.setMotivoModifica(rs.getString("MOTIVO_MODIFICA"));
				if(Validator.isNotEmpty(rs.getString("ID_ATTIVITA_OTE"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_ATTIVITA_OTE")), rs.getString("DESC_OTE"));
					uteVO.setTipoAttivitaOTE(code);
				}
				uteVO.setTipoSede(rs.getString("TIPO_SEDE"));
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "findUteByPrimaryKey in UteDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "findUteByPrimaryKey in UteDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "findUteByPrimaryKey in UteDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "findUteByPrimaryKey in UteDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated findUteByPrimaryKey method in UteDAO\n");
		return uteVO;
	}
	
	
	/**
   * Metodo che mi restituisce l'elenco delle ute attive relative all'azienda agricola selezionata
   * e al piano di riferimento
   * @param idAzienda
   * @param idPianoRiferimento
   * @return java.util.Vector
   * @throws DataAccessException
   */
  public Vector<UteVO> getListUteByIdAziendaAndIdPianoRiferimento(Long idAzienda, long idPianoRiferimento) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getListUteByIdAziendaAndIdPianoRiferimento method in UteDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<UteVO> elencoUte = null;

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getListUteByIdAziendaAndIdPianoRiferimento method in UteDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListUteByIdAziendaAndIdPianoRiferimento method in UteDAO and it values: "+conn+"\n");

      String query = 
          " SELECT U.ID_UTE, " +
          "        U.ID_AZIENDA, " +
          "        U.DENOMINAZIONE, " +
          "        U.INDIRIZZO, " +
          "        U.COMUNE, " +
          "        C.DESCOM, " +
          "        C.FLAG_ESTERO, " +
          "        P.SIGLA_PROVINCIA, " +
          "        U.CAP, " +
          "        U.ID_ZONA_ALTIMETRICA, " +  
          "        TZA.DESCRIZIONE AS DESC_ALTIMETRICA, " +
          "        U.ID_ATTIVITA_ATECO, " +
          "        TAA.DESCRIZIONE AS DESC_ATECO, " +
          "        U.TELEFONO, " +
          "        U.FAX, " +
          "        U.NOTE, " +
          "        U.DATA_INIZIO_ATTIVITA, " + 
          "        U.DATA_FINE_ATTIVITA, " +
          "        U.CAUSALE_CESSAZIONE, " +
          "        U.DATA_AGGIORNAMENTO, " +
          "        U.ID_UTENTE_AGGIORNAMENTO, " +
          " (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
                  "          || ' ' " + 
                  "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
                  "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
                  "         WHERE U.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
                  "        AS DENOM_UTENTE, " +
                  "       (SELECT PVU.DENOMINAZIONE " +
                  "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
                  "        WHERE U.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
                  "        AS DESCRIZIONE_ENTE_APPARTENENZA, " + 
          "        U.MOTIVO_MODIFICA, " +  
          "        U.ID_ATTIVITA_OTE, " +
          "        TAO.DESCRIZIONE AS DESC_OTE, " +
          "        U.TIPO_SEDE " +
          " FROM   DB_UTE U, " +
          "        COMUNE C, " +
          "        PROVINCIA P, " +
          "        DB_TIPO_ZONA_ALTIMETRICA TZA, " +
          "        DB_TIPO_ATTIVITA_ATECO TAA, " +
          "        DB_TIPO_ATTIVITA_OTE TAO, ";
          //"        PAPUA_V_UTENTE_LOGIN PVU ";
      
      if(idPianoRiferimento > 0) 
      {
        query += "    ,DB_DICHIARAZIONE_CONSISTENZA DC ";
      }
      
      query += 
          " WHERE  U.ID_AZIENDA = ? " +
          " AND    U.COMUNE = C.ISTAT_COMUNE " +
          " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
          " AND    U.ID_ZONA_ALTIMETRICA = TZA.ID_ZONA_ALTIMETRICA(+) " +
          " AND    U.ID_ATTIVITA_ATECO = TAA.ID_ATTIVITA_ATECO(+) " +
          " AND    U.ID_ATTIVITA_OTE = TAO.ID_ATTIVITA_OTE(+) ";
          //" AND    U.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN ";
      
      
      if(idPianoRiferimento > 0) {
        query +=
          " AND  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
          " AND  U.DATA_INIZIO_ATTIVITA < DC.DATA_INSERIMENTO_DICHIARAZIONE " +
          " AND  NVL(U.DATA_FINE_ATTIVITA, ?) > DC.DATA_INSERIMENTO_DICHIARAZIONE ";
      }
      else 
      {
        query +=
          " AND  U.DATA_FINE_ATTIVITA IS NULL ";
      }
      
      query +=
          " ORDER BY C.DESCOM, U.INDIRIZZO ";

      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListUteByIdAziendaAndIdPianoRiferimento method in UteDAO: "+idAzienda+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ID_PIANO_RIFERIMENO] in getListUteByIdAziendaAndIdPianoRiferimento method in UteDAO: "+idPianoRiferimento+"\n");
      

      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAzienda.longValue());
      
      if(idPianoRiferimento > 0) {
        stmt.setLong(++indice, idPianoRiferimento);
        stmt.setDate(++indice, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
      }  

      SolmrLogger.debug(this, "Executing getListUteByIdAziendaAndIdPianoRiferimento: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        if(elencoUte == null)
        {
          elencoUte = new Vector<UteVO>();
        }
        UteVO uteVO = new UteVO();
        uteVO.setIdUte(new Long(rs.getLong("ID_UTE")));
        uteVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        uteVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        uteVO.setIndirizzo(rs.getString("INDIRIZZO"));
        ComuneVO comuneUte = new ComuneVO();
        comuneUte.setIstatComune(rs.getString("COMUNE"));
        comuneUte.setDescom(rs.getString("DESCOM"));
        comuneUte.setFlagEstero(rs.getString("FLAG_ESTERO"));
        ProvinciaVO provinciaVO = new ProvinciaVO();
        provinciaVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));
        comuneUte.setProvinciaVO(provinciaVO);
        uteVO.setComuneUte(comuneUte);
        uteVO.setCap(rs.getString("CAP"));
        if(Validator.isNotEmpty(rs.getString("ID_ZONA_ALTIMETRICA"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_ZONA_ALTIMETRICA")), rs.getString("DESC_ALTIMETRICA"));
          uteVO.setTipoZonaAltimetrica(code);
        }
        if(Validator.isNotEmpty(rs.getString("ID_ATTIVITA_ATECO"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_ATTIVITA_ATECO")), rs.getString("DESC_ATECO"));
          uteVO.setTipoAttivitaATECO(code);
        }
        uteVO.setTelefono(rs.getString("TELEFONO"));
        uteVO.setFax(rs.getString("FAX"));
        uteVO.setNote(rs.getString("NOTE"));
        uteVO.setDataInizioAttivita(rs.getDate("DATA_INIZIO_ATTIVITA"));
        uteVO.setDataFineAttivita(rs.getDate("DATA_FINE_ATTIVITA"));
        uteVO.setCausaleCessazione(rs.getString("CAUSALE_CESSAZIONE"));
        uteVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
        UtenteIrideVO utenteIrideVO = new UtenteIrideVO();
        utenteIrideVO.setIdUtente(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        String utenteModifica = rs.getString("DENOM_UTENTE");
        utenteIrideVO.setDenominazione(utenteModifica);
        uteVO.setUtenteUltimaModifica(utenteModifica);
        String enteModifica = rs.getString("DESCRIZIONE_ENTE_APPARTENENZA");
        utenteIrideVO.setDescrizioneEnteAppartenenza(enteModifica);
        uteVO.setEnteUltimaModifica(enteModifica);
        uteVO.setDatiUtenteAggiornamento(utenteIrideVO);
        uteVO.setMotivoModifica(rs.getString("MOTIVO_MODIFICA"));
        if(Validator.isNotEmpty(rs.getString("ID_ATTIVITA_OTE"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_ATTIVITA_OTE")), rs.getString("DESC_OTE"));
          uteVO.setTipoAttivitaOTE(code);
        }
        uteVO.setTipoSede(rs.getString("TIPO_SEDE"));
        elencoUte.add(uteVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getListUteByIdAziendaAndIdPianoRiferimento in UteDAO - SQLException: "+exc.getMessage()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getListUteByIdAziendaAndIdPianoRiferimento in UteDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getListUteByIdAziendaAndIdPianoRiferimento in UteDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getListUteByIdAziendaAndIdPianoRiferimento in UteDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListUteByIdAziendaAndIdPianoRiferimento method in UteDAO\n");
    
    return elencoUte;
  }
  
  /**
   * Restituisce la minima data inizio conduzione 
   * associata all'id_ute
   * 
   * @param idUte
   * @return
   * @throws DataAccessException
   */
  public Date getMinDataInizioConduzione(Long idUte) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Date minDataInizio = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[UteDAO::getMinDataInizioConduzione] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append(" SELECT MIN(DATA_INIZIO_CONDUZIONE) AS MIN_DATA_INIZIO_COND " + 
          		" FROM  DB_CONDUZIONE_PARTICELLA " + 
          		" WHERE ID_UTE = ? ");// +
          		//" AND DATA_FINE_CONDUZIONE IS NULL ");
              
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[UteDAO::getMinDataInizioConduzione] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int idx = 0;
      stmt.setLong(++idx,idUte);
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {
    	  minDataInizio = rs.getDate("MIN_DATA_INIZIO_COND");
      }
      
      return minDataInizio;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("minDataInizio", minDataInizio) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUte", idUte) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[UteDAO::getMinDataInizioConduzione] ",
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
              "[UteDAO::getMinDataInizioConduzione] END.");
    }
  }
  
  /**
   * Restituisce la minima data inizio  
   * associata all'id_ute
   * 
   * @param idUte
   * @return
   * @throws DataAccessException
   */
  public Date getMinDataInizioAllevamento(Long idUte) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Date minDataInizio = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[UteDAO::getMinDataInizioAllevamento] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append(" SELECT MIN(DATA_INIZIO) AS MIN_DATA_INIZIO_ALLEVAMENTO " + 
          		" FROM  DB_ALLEVAMENTO " + 
          		" WHERE ID_UTE = ? ");// +
          		//" AND DATA_FINE IS NULL ");
              
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[UteDAO::getMinDataInizioAllevamento] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int idx = 0;
      stmt.setLong(++idx,idUte);
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {
    	  minDataInizio = rs.getDate("MIN_DATA_INIZIO_ALLEVAMENTO");
      }
      
      return minDataInizio;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("minDataInizio", minDataInizio) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUte", idUte) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[UteDAO::getMinDataInizioAllevamento] ",
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
              "[UteDAO::getMinDataInizioAllevamento] END.");
    }
  }

  /**
   * Restituisce la minima data inizio validita fabbricati
   * associata all'id_ute
   * 
   * @param idUte
   * @return
   * @throws DataAccessException
   */
  public Date getMinDataInizioFabbricati(Long idUte) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Date minDataInizio = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[UteDAO::getMinDataInizioFabbricati] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append(" SELECT MIN(DATA_INIZIO_VALIDITA) AS MIN_DATA_INIZIO_VAL_FABBRICATI " + 
          		" FROM  DB_FABBRICATO " + 
          		" WHERE ID_UTE = ? ");// +
          		//" AND DATA_FINE_VALIDITA IS NULL ");
              
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[UteDAO::getMinDataInizioFabbricati] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int idx = 0;
      stmt.setLong(++idx,idUte);
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {
    	  minDataInizio = rs.getDate("MIN_DATA_INIZIO_VAL_FABBRICATI");
      }
      
      return minDataInizio;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("minDataInizio", minDataInizio) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUte", idUte) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[UteDAO::getMinDataInizioFabbricati] ",
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
              "[UteDAO::getMinDataInizioFabbricati] END.");
    }
  }
  
  
  public Vector<UteAtecoSecondariVO> getElencoAtecoSecUte(Long idUte) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<UteAtecoSecondariVO> vUteAtecoSec = null;
    UteAtecoSecondariVO uteAtecoSecondariVO = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[UteDAO::getElencoAtecoSecUte] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "SELECT UAS.ID_UTE_ATECO_SECONDARI, " +
        "       UAS.ID_UTE, " +
        "       UAS.ID_ATTIVITA_ATECO, " +
        "       UAS.DATA_INIZIO_VALIDITA, " +
        "       TAA.DESCRIZIONE, " +
        "       TAA.CODICE   " + 
        "FROM   DB_UTE_ATECO_SECONDARI UAS," +
        "       DB_TIPO_ATTIVITA_ATECO TAA " + 
        "WHERE  UAS.ID_UTE = ? " +
        "AND    UAS.ID_ATTIVITA_ATECO = TAA.ID_ATTIVITA_ATECO " +
        "AND    UAS.DATA_FINE_VALIDITA IS NULL " +
        "ORDER BY TAA.DESCRIZIONE ");// +
              //" AND DATA_FINE_VALIDITA IS NULL ");
              
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[UteDAO::getElencoAtecoSecUte] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int idx = 0;
      stmt.setLong(++idx,idUte);
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vUteAtecoSec == null)
          vUteAtecoSec = new Vector<UteAtecoSecondariVO>();
        
        uteAtecoSecondariVO = new UteAtecoSecondariVO();
        uteAtecoSecondariVO.setIdUteAtecoSecondari(rs.getLong("ID_UTE_ATECO_SECONDARI"));
        uteAtecoSecondariVO.setIdUte(rs.getLong("ID_UTE"));
        uteAtecoSecondariVO.setIdAttivitaAteco(rs.getLong("ID_ATTIVITA_ATECO"));
        uteAtecoSecondariVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        uteAtecoSecondariVO.setDescAttivitaAteco(rs.getString("DESCRIZIONE"));
        uteAtecoSecondariVO.setCodiceAteco(rs.getString("CODICE"));
        
        vUteAtecoSec.add(uteAtecoSecondariVO);
      }
      
      return vUteAtecoSec;
    }
    catch (Exception t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vUteAtecoSec", vUteAtecoSec),
          new Variabile("uteAtecoSecondariVO", uteAtecoSecondariVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUte", idUte) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[UteDAO::getElencoAtecoSecUte] ",
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
              "[UteDAO::getElencoAtecoSecUte] END.");
    }
  }
  
  public void storicizzaAtecoSecUte(long idUte) 
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
              "[UteDAO::storicizzaAtecoSecUte] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   UPDATE DB_UTE_ATECO_SECONDARI   " 
              + "     SET  DATA_FINE_VALIDITA = ? "
              + "   WHERE  "
              + "     ID_UTE = ?  ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[UteDAO::storicizzaAtecoSecUte] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setTimestamp(++indice, convertDateToTimestamp(new Date()));
      stmt.setLong(++indice, idUte);
      
  
      stmt.executeUpdate();
    }
    catch (Exception t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUte", idUte)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[UteDAO::storicizzaAtecoSecUte] ",
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
              "[UteDAO::storicizzaAtecoSecUte] END.");
    }
  }
  
  public Long insertUteAtecoSecondari(UteAtecoSecondariVO uteAtecoSecondariVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idUteAtecoSecondari = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[UteDAO::insertUteAtecoSecondari] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idUteAtecoSecondari = getNextPrimaryKey(SolmrConstants.SEQ_DB_UTE_ATECO_SECONDARI);
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_UTE_ATECO_SECONDARI   " 
              + "     (ID_UTE_ATECO_SECONDARI, "
              + "     ID_UTE , " 
              + "     ID_ATTIVITA_ATECO , "
              + "     DATA_INIZIO_VALIDITA )  "
              + "   VALUES(?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[UteDAO::insertUteAtecoSecondari] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idUteAtecoSecondari.longValue());
      stmt.setLong(++indice,uteAtecoSecondariVO.getIdUte());
      stmt.setLong(++indice, uteAtecoSecondariVO.getIdAttivitaAteco());
      stmt.setTimestamp(++indice, convertDateToTimestamp(uteAtecoSecondariVO.getDataInizioValidita()));
      
      
      
  
      stmt.executeUpdate();
      
      return idUteAtecoSecondari;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idUteAtecoSecondari", idUteAtecoSecondari)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("uteAtecoSecondariVO", uteAtecoSecondariVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[UteDAO::insertUteAtecoSecondari] ",
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
              "[UteDAO::insertUteAtecoSecondari] END.");
    }
  }

}
