package it.csi.solmr.integration.anag;

import it.csi.solmr.dto.anag.*;
import it.csi.solmr.dto.profile.*;
import it.csi.solmr.exception.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.util.*;

import java.sql.*;
import java.util.*;
import it.csi.util.performance.StopWatch;

public class DelegaDAO extends it.csi.solmr.integration.BaseDAO {


	public DelegaDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public DelegaDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo che mi restituisce la data max di fine mandato
	 *
	 * @param idAzienda
	 * @return java.util.Date maxDataFineMandato
	 * @throws DataAccessException
	 */
	public java.util.Date getDataMaxFineMandato(Long idAzienda) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getDataMaxFineMandato method in DelegaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		java.util.Date maxDataFineMandato = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in getDataMaxFineMandato method in DelegaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getDataMaxFineMandato method in DelegaDAO and it values: "+conn+"\n");

			String query = " SELECT MAX(DATA_FINE_MANDATO) AS MAX_DATA " +
						   " FROM   DB_DELEGA " +
						   " WHERE  ID_AZIENDA = ? ";

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getDataMaxFineMandato method in DelegaDAO: "+idAzienda+"\n");

			stmt = conn.prepareStatement(query);

			stmt.setLong(1, idAzienda.longValue());

			SolmrLogger.debug(this, "Executing getDataMaxFineMandato: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) {
				if(Validator.isNotEmpty(rs.getString("MAX_DATA"))) {
					maxDataFineMandato = new java.util.Date(rs.getDate("MAX_DATA").getTime());
				}
			}

			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getDataMaxFineMandato in DelegaDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getDataMaxFineMandato in DelegaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getDataMaxFineMandato in DelegaDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getDataMaxFineMandato in DelegaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getDataMaxFineMandato method in DelegaDAO\n");
		return maxDataFineMandato;
	}

	/**
	 * Metodo utilizzato per inserire un record nella tabella DB_DELEGA
	 *
	 * @param DelegaVO delegaVO
	 * @throws DataAccessException
	 */
	public Long insertDelega(DelegaVO delegaVO) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating insertDelega method in DelegaDAO\n");
		Long idDelega = null;
		Connection conn = null;
		PreparedStatement stmt = null;

		try {

			idDelega = getNextPrimaryKey((String)SolmrConstants.get("SEQ_DELEGA"));
			SolmrLogger.debug(this, "Creating db-connection in insertDelega method in DelegaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in insertDelega method in DelegaDAO and it values: "+conn+"\n");

			String query  = " INSERT INTO DB_DELEGA "+
                    		"            (ID_DELEGA, " +
                    		"             ID_INTERMEDIARIO, " +
                    		"             ID_PROCEDIMENTO, "+
                    		"             ID_AZIENDA, " +
                    		"             DATA_INIZIO, " +
                    		"             DATA_FINE, " +
                    		"             ID_UTENTE_INSERIMENTO_DELEGA, " +
                    		"             ID_UFFICIO_ZONA_INTERMEDIARIO, " +
                    		"             UFFICIO_FASCICOLO, " +
                    		"             ID_UTENTE_FINE_DELEGA, " +
                    		"             INDIRIZZO, " +
                    		"             COMUNE, " +
                    		"             CAP, " +
                    		"             CODICE_AMMINISTRAZIONE, " +
                    		"             RECAPITO, " +
                    		"             DATA_INIZIO_MANDATO, " +
                    		"             DATA_FINE_MANDATO) " +
                    		" VALUES     (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing insertDelega: "+query);

			stmt.setLong(1, idDelega.longValue());
			SolmrLogger.debug(this, "Value of parameter 1 [ID_DELEGA] in method insertDelega in DelegaDAO: "+idDelega.longValue()+"\n");
			stmt.setLong(2, delegaVO.getIdIntermediario().longValue());
			SolmrLogger.debug(this, "Value of parameter 2 [ID_INTERMEDIARIO] in method insertDelega in DelegaDAO: "+delegaVO.getIdIntermediario().longValue()+"\n");
			stmt.setLong(3, delegaVO.getIdProcedimento().longValue());
			SolmrLogger.debug(this, "Value of parameter 3 [ID_PROCEDIMENTO] in method insertDelega in DelegaDAO: "+delegaVO.getIdProcedimento().longValue()+"\n");
			stmt.setLong(4, delegaVO.getIdAzienda().longValue());
			SolmrLogger.debug(this, "Value of parameter 4 [ID_AZIENDA] in method insertDelega in DelegaDAO: "+delegaVO.getIdAzienda().longValue()+"\n");
			stmt.setTimestamp(5, new Timestamp(delegaVO.getDataInizio().getTime()));
			SolmrLogger.debug(this, "Value of parameter 5 [DATA_INIZIO] in method insertDelega in DelegaDAO: "+delegaVO.getDataInizio()+"\n");
			if(delegaVO.getDataFine() != null) {
				stmt.setTimestamp(6, new Timestamp(delegaVO.getDataFine().getTime()));
				SolmrLogger.debug(this, "Value of parameter 6 [DATA_FINE] in method insertDelega in DelegaDAO: "+delegaVO.getDataFine()+"\n");
			}
			else {
				stmt.setString(6, null);
				SolmrLogger.debug(this, "Value of parameter 6 [DATA_FINE] in method insertDelega in DelegaDAO: "+null+"\n");
			}
			stmt.setLong(7, delegaVO.getIdUtenteInsDelega().longValue());
			SolmrLogger.debug(this, "Value of parameter 7 [ID_UTENTE_INSERIMENTO_DELEGA] in method insertDelega in DelegaDAO: "+delegaVO.getIdUtenteInsDelega().longValue()+"\n");
			stmt.setLong(8, delegaVO.getIdUfficioZonaIntermediario().longValue());
			SolmrLogger.debug(this, "Value of parameter 8 [ID_UFFICIO_ZONA_INTERMEDIARIO] in method insertDelega in DelegaDAO: "+delegaVO.getIdUfficioZonaIntermediario().longValue()+"\n");
			stmt.setString(9, delegaVO.getUfficioFascicolo());
			SolmrLogger.debug(this, "Value of parameter 9 [UFFICIO_FASCICOLO] in method insertDelega in DelegaDAO: "+delegaVO.getUfficioFascicolo()+"\n");
			if(delegaVO.getIdUtenteFineDelega() != null) {
				stmt.setLong(10, delegaVO.getIdUtenteFineDelega().longValue());
				SolmrLogger.debug(this, "Value of parameter 10 [ID_UTENTE_FINE_DELEGA] in method insertDelega in DelegaDAO: "+delegaVO.getIdUtenteFineDelega().longValue()+"\n");
			}
			else {
				stmt.setString(10, null);
				SolmrLogger.debug(this, "Value of parameter 10 [ID_UTENTE_FINE_DELEGA] in method insertDelega in DelegaDAO: "+null+"\n");
			}
			stmt.setString(11, delegaVO.getIndirizzo());
			SolmrLogger.debug(this, "Value of parameter 11 [INDIRIZZO] in method insertDelega in DelegaDAO: "+delegaVO.getIndirizzo()+"\n");
			stmt.setString(12, delegaVO.getIstaComune());
			SolmrLogger.debug(this, "Value of parameter 12 [COMUNE] in method insertDelega in DelegaDAO: "+delegaVO.getIstaComune()+"\n");
			stmt.setString(13, delegaVO.getCap());
			SolmrLogger.debug(this, "Value of parameter 13 [CAP] in method insertDelega in DelegaDAO: "+delegaVO.getCap()+"\n");
			stmt.setString(14, delegaVO.getCodiceAmministrazione());
			SolmrLogger.debug(this, "Value of parameter 14 [CODICE_AMMINISTRAZIONE] in method insertDelega in DelegaDAO: "+delegaVO.getCodiceAmministrazione()+"\n");
			stmt.setString(15, delegaVO.getRecapito());
			SolmrLogger.debug(this, "Value of parameter 15 [RECAPITO] in method insertDelega in DelegaDAO: "+delegaVO.getRecapito()+"\n");
			stmt.setDate(16, new java.sql.Date(delegaVO.getDataInizioMandato().getTime()));
			SolmrLogger.debug(this, "Value of parameter 16 [DATA_INIZIO_MANDATO] in method insertDelega in DelegaDAO: "+delegaVO.getDataInizioMandato()+"\n");
			if(delegaVO.getDataFineMandato() != null) {
				stmt.setDate(17, new java.sql.Date(delegaVO.getDataFineMandato().getTime()));
				SolmrLogger.debug(this, "Value of parameter 17 [DATA_FINE_MANDATO] in method insertDelega in DelegaDAO: "+delegaVO.getDataFineMandato()+"\n");
			}
			else {
				stmt.setString(17, null);
				SolmrLogger.debug(this, "Value of parameter 17 [DATA_FINE_MANDATO] in method insertDelega in DelegaDAO: "+null+"\n");
			}

			stmt.executeUpdate();

			stmt.close();
		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "SQLException in insertDelega: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "Generic Exception in insertDelega: "+ex.getMessage());
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
				SolmrLogger.error(this, "SQLException while closing Statement and Connection in insertDelega: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in insertDelega: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated insertDelega method in DelegaDAO\n");
		return idDelega;
	}

	/**
	 * Metodo che mi restituisce la delega di un dato procedimento relativo ad
	 * una determinata azienda agricola
	 *
	 * @param idAzienda
	 * @param idProcedimento
	 * @return it.csi.solmr.dto.anag.DelegaVO
	 * @throws DataAccessException
	 */
	public DelegaVO getDelegaByAziendaAndIdProcedimento(Long idAzienda, Long idProcedimento) 
	    throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating getDelegaByAziendaAndIdProcedimento method in DelegaDAO\n");
	  Connection conn = null;
	  PreparedStatement stmt = null;
	  DelegaVO delegaVO = null;

	  try 
	  {
	  	SolmrLogger.debug(this, "Creating db-connection in getDelegaByAziendaAndIdProcedimento method in DelegaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getDelegaByAziendaAndIdProcedimento method in DelegaDAO and it values: "+conn+"\n");

			String query =  
			  "SELECT D.ID_DELEGA, " +
			  "       D.ID_INTERMEDIARIO AS INTERM, " +
		    "       I.DENOMINAZIONE ALIAS_DESC_INTERM, " +
		    "       I.CODICE_FISCALE, " +
		    "       D.ID_PROCEDIMENTO, " +
		    "       D.ID_AZIENDA, " +
		    "       D.DATA_INIZIO, " +
		    "       D.DATA_FINE, " +
		    "       D.ID_UTENTE_INSERIMENTO_DELEGA, " +
		    "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
	        "          || ' ' " + 
	        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
	        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
	        "         WHERE D.ID_UTENTE_INSERIMENTO_DELEGA = PVU.ID_UTENTE_LOGIN) " +
			"       AS DENOMINAZIONE_UTENTE, "+
			"       (SELECT PVU.DENOMINAZIONE " +
		    "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
		    "        WHERE D.ID_UTENTE_INSERIMENTO_DELEGA = PVU.ID_UTENTE_LOGIN) " +
			"       AS DESCRIZIONE_ENTE_APPARTENENZA, " +
		    "       D.ID_UFFICIO_ZONA_INTERMEDIARIO, " +
		    "       D.UFFICIO_FASCICOLO, " +
        "       D.ID_UTENTE_FINE_DELEGA, " +
        "       D.INDIRIZZO, " +
        "       D.COMUNE, " +
        "       D.CAP, " +
        "       D.CODICE_AMMINISTRAZIONE, " +
        "       D.RECAPITO, " +
        "       D.DATA_INIZIO_MANDATO, " +
        "       D.DATA_FINE_MANDATO, " +
        "       I.EMAIL AS MAIL_INT, " +
        "       I.PEC AS PEC_INT, " +
        "       UZI.MAIL AS MAIL_UFFZONA " +
        "FROM   DB_DELEGA D, " +
        "       DB_INTERMEDIARIO I, " +
        //"       PAPUA_V_UTENTE_LOGIN PVU, " +
        "       DB_UFFICIO_ZONA_INTERMEDIARIO UZI "+
        "WHERE  D.ID_INTERMEDIARIO = I.ID_INTERMEDIARIO "+
        "AND    D.ID_AZIENDA = ? "+
        "AND    D.DATA_FINE IS NULL " +
        "AND    D.ID_PROCEDIMENTO = ? "+
        //"AND    D.ID_UTENTE_INSERIMENTO_DELEGA = PVU.ID_UTENTE_LOGIN " +
        "AND    D.ID_UFFICIO_ZONA_INTERMEDIARIO = UZI.ID_UFFICIO_ZONA_INTERMEDIARIO (+) ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getDelegaByAziendaAndIdProcedimento method in DelegaDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ID_PROCEDIMENTO] in getDelegaByAziendaAndIdProcedimento method in DelegaDAO: "+idProcedimento+"\n");
			stmt.setLong(1, idAzienda.longValue());
			stmt.setLong(2, idProcedimento.longValue());

			SolmrLogger.debug(this, "Executing query: "+query);
			ResultSet rs = stmt.executeQuery();

			if(rs.next()) 
			{
        delegaVO = new DelegaVO();
        delegaVO.setIdDelega(new Long(rs.getLong("ID_DELEGA")));
        delegaVO.setIdIntermediario(new Long(rs.getLong("INTERM")));
        delegaVO.setDenomIntermediario(rs.getString("ALIAS_DESC_INTERM"));
        delegaVO.setCodiceFiscaleIntermediario(rs.getString("CODICE_FISCALE"));
        delegaVO.setIdProcedimento(new Long(rs.getLong("ID_PROCEDIMENTO")));
        delegaVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        delegaVO.setDataInizio(rs.getDate("DATA_INIZIO"));
        delegaVO.setDataFine(rs.getDate("DATA_FINE"));
        delegaVO.setIdUtenteInsDelega(new Long(rs.getLong("ID_UTENTE_INSERIMENTO_DELEGA")));
        delegaVO.setNomeUtenIrideIndDelega(rs.getString("DENOMINAZIONE_UTENTE"));
        delegaVO.setEnteUtenIrideIndDelega(rs.getString("DESCRIZIONE_ENTE_APPARTENENZA"));
        delegaVO.setIdUfficioZonaIntermediario(new Long(rs.getLong("ID_UFFICIO_ZONA_INTERMEDIARIO")));
        delegaVO.setUfficioFascicolo(rs.getString("UFFICIO_FASCICOLO"));
        delegaVO.setIdUtenteFineDelega(new Long(rs.getLong("ID_UTENTE_FINE_DELEGA")));
        delegaVO.setIndirizzo(rs.getString("INDIRIZZO"));
        delegaVO.setIstaComune(rs.getString("COMUNE"));
        delegaVO.setCap(rs.getString("CAP"));
        delegaVO.setCodiceAmministrazione(rs.getString("CODICE_AMMINISTRAZIONE"));
        delegaVO.setRecapito(rs.getString("RECAPITO"));
        delegaVO.setDataInizioMandato(rs.getDate("DATA_INIZIO_MANDATO"));
        delegaVO.setDataFineMandato(rs.getDate("DATA_FINE_MANDATO"));
        delegaVO.setEmailIntermediario(rs.getString("MAIL_INT"));
        delegaVO.setPecIntermediario(rs.getString("PEC_INT"));
        delegaVO.setMailUfficioZona(rs.getString("MAIL_UFFZONA"));
			}

			rs.close();
			stmt.close();
	  }
	  catch(SQLException exc) 
	  {
		  SolmrLogger.error(this, "getDelegaByAziendaAndIdProcedimento in DelegaDAO - SQLException: "+exc.getMessage()+"\n");
		  throw new DataAccessException(exc.getMessage());
	  }
		catch(Exception ex) 
		{
			SolmrLogger.error(this, "getDelegaByAziendaAndIdProcedimento in DelegaDAO - Generic Exception: "+ex+"\n");
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
				SolmrLogger.error(this, "getDelegaByAziendaAndIdProcedimento in DelegaDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) 
			{
				SolmrLogger.error(this, "getDelegaByAziendaAndIdProcedimento in DelegaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getDelegaByAziendaAndIdProcedimento method in DelegaDAO\n");
		return delegaVO;
	}

	/**
	 * Metodo che mi restituisce la data max di inizio mandato
	 *
	 * @param idAzienda
	 * @return java.util.Date maxDataFineMandato
	 * @throws DataAccessException
	 */
	public java.util.Date getDataMaxInizioMandato(Long idAzienda) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getDataMaxInizioMandato method in DelegaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		java.util.Date maxDataInizioMandato = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in getDataMaxInizioMandato method in DelegaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getDataMaxInizioMandato method in DelegaDAO and it values: "+conn+"\n");

			String query = " SELECT MAX(DATA_INIZIO_MANDATO) AS MAX_DATA " +
						   " FROM   DB_DELEGA " +
						   " WHERE  ID_AZIENDA = ? ";

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getDataMaxInizioMandato method in DelegaDAO: "+idAzienda+"\n");

			stmt = conn.prepareStatement(query);

			stmt.setLong(1, idAzienda.longValue());

			SolmrLogger.debug(this, "Executing getDataMaxInizioMandato: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) {
				if(Validator.isNotEmpty(rs.getString("MAX_DATA"))) {
					maxDataInizioMandato = new java.util.Date(rs.getDate("MAX_DATA").getTime());
				}
			}

			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getDataMaxInizioMandato in DelegaDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getDataMaxInizioMandato in DelegaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getDataMaxInizioMandato in DelegaDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getDataMaxInizioMandato in DelegaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getDataMaxInizioMandato method in DelegaDAO\n");
		return maxDataInizioMandato;
	}

	/**
	 * Metodo che mi permette di storicizzare un record sulla tabella DB_DELEGA
	 *
	 * @param delegaVO
	 * @param ruoloUtenza
	 * @throws DataAccessException
	 */
	public void storicizzaDelega(DelegaVO delegaVO, RuoloUtenza ruoloUtenza) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating storicizzaDelega method in DelegaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;

		try {

			SolmrLogger.debug(this, "Creating db-connection in storicizzaDelega method in DelegaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in storicizzaDelega method in DelegaDAO and it values: "+conn+"\n");

			String query  = " UPDATE DB_DELEGA " +
							" SET    DATA_FINE = SYSDATE, " +
							"        ID_UTENTE_FINE_DELEGA = ?, " +
							"        DATA_FINE_MANDATO = ? " +
							" WHERE  ID_DELEGA = ? ";

			SolmrLogger.debug(this, "Value of parameter 1 [ID_UTENTE_FINE_DELEGA] in storicizzaDelega method in DelegaDAO: "+ruoloUtenza.getIdUtente()+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [DATA_FINE_MANDATO] in storicizzaDelega method in DelegaDAO: "+delegaVO.getDataFineMandato()+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ID_DELEGA] in storicizzaDelega method in DelegaDAO: "+delegaVO.getIdDelega()+"\n");

			stmt = conn.prepareStatement(query);

			stmt.setLong(1, ruoloUtenza.getIdUtente().longValue());
			stmt.setDate(2, new java.sql.Date(delegaVO.getDataFineMandato().getTime()));
			stmt.setLong(3, delegaVO.getIdDelega().longValue());

			SolmrLogger.debug(this, "Executing storicizzaDelega: "+query);

			stmt.executeUpdate();

			stmt.close();
		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "SQLException in storicizzaDelega: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "Generic Exception in storicizzaDelega: "+ex.getMessage());
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
				SolmrLogger.error(this, "SQLException while closing Statement and Connection in storicizzaDelega: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in storicizzaDelega: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated storicizzaDelega method in DelegaDAO\n");
	}
	
	
	/**
	 * Setta la storicicizzazine delle delega in modo da poter poi essere conclusa dal batch
	 * che girerà una volta al giorno
	 * 
	 * 
	 * @param delegaVO
	 * @param ruoloUtenza
	 * @throws DataAccessException
	 */
	public void storicizzaDelegaTemporanea(DelegaVO delegaVO, RuoloUtenza ruoloUtenza) 
	  throws DataAccessException 
	{
    SolmrLogger.debug(this, "Invocating storicizzaDelegaTemporanea method in DelegaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;

    try 
    {

      SolmrLogger.debug(this, "Creating db-connection in storicizzaDelegaTemporanea method in DelegaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in storicizzaDelegaTemporanea method in DelegaDAO and it values: "+conn+"\n");

      String query  = " UPDATE DB_DELEGA " +
              " SET    DATA_RICEVUTA_RITORNO_REVOCA = ?, " +
              "        ID_UTENTE_REVOCA = ?, " +
              "        DATA_FINE_MANDATO = ? " +
              " WHERE  ID_DELEGA = ? ";

      SolmrLogger.debug(this, "Value of parameter [DATA_RICEVUTA_RITORNO_REVOCA] in storicizzaDelegaTemporanea method in DelegaDAO: "+delegaVO.getDataRicevutaRitornoDelega()+"\n");
      SolmrLogger.debug(this, "Value of parameter [ID_UTENTE_FINE_DELEGA] in storicizzaDelegaTemporanea method in DelegaDAO: "+ruoloUtenza.getIdUtente()+"\n");
      SolmrLogger.debug(this, "Value of parameter [DATA_FINE_MANDATO] in storicizzaDelegaTemporanea method in DelegaDAO: "+delegaVO.getDataFineMandato()+"\n");
      SolmrLogger.debug(this, "Value of parameter [ID_DELEGA] in storicizzaDelegaTemporanea method in DelegaDAO: "+delegaVO.getIdDelega()+"\n");

      stmt = conn.prepareStatement(query);

      int indice = 0;
      
      stmt.setDate(++indice, new java.sql.Date(delegaVO.getDataRicevutaRitornoDelega().getTime()));
      stmt.setLong(++indice, ruoloUtenza.getIdUtente().longValue());
      stmt.setDate(++indice, new java.sql.Date(delegaVO.getDataFineMandato().getTime()));
      stmt.setLong(++indice, delegaVO.getIdDelega().longValue());

      SolmrLogger.debug(this, "Executing storicizzaDelegaTemporanea: "+query);

      stmt.executeUpdate();

      stmt.close();
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "SQLException in storicizzaDelegaTemporanea: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "Generic Exception in storicizzaDelegaTemporanea: "+ex.getMessage());
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
        SolmrLogger.error(this, "SQLException while closing Statement and Connection in storicizzaDelegaTemporanea: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in storicizzaDelegaTemporanea: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated storicizzaDelegaTemporanea method in DelegaDAO\n");
  }

	/**
	 * Metodo che mi restituisce lo storico delle deleghe di un dato procedimento relativo ad
	 * una determinata azienda agricola
	 *
	 * @param idAzienda
	 * @param idProcedimento
	 * @param orderBy[]
	 * @return it.csi.solmr.dto.anag.DelegaVO[]
	 * @throws DataAccessException
	 */
	public DelegaVO[] getStoricoDelegheByAziendaAndIdProcedimento(Long idAzienda, Long idProcedimento, String[] orderBy) 
	    throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating getStoricoDelegheByAziendaAndIdProcedimento method in DelegaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<DelegaVO> elencoDeleghe = new Vector<DelegaVO>();

    try 
    {
	   	SolmrLogger.debug(this, "Creating db-connection in getStoricoDelegheByAziendaAndIdProcedimento method in DelegaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getStoricoDelegheByAziendaAndIdProcedimento method in DelegaDAO and it values: "+conn+"\n");

			String query =  
			  "SELECT D.ID_DELEGA, " +
				"       D.ID_INTERMEDIARIO AS INTERM, " +
    	  "       I.DENOMINAZIONE ALIAS_DESC_INTERM, " +
    		"       I.CODICE_FISCALE, " +
    		"       D.ID_PROCEDIMENTO, " +
    		"       D.ID_AZIENDA, " +
        "       D.DATA_INIZIO, " +
        "       D.DATA_FINE, " +
        "       D.ID_UTENTE_INSERIMENTO_DELEGA, " +
        "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
        "          || ' ' " + 
        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
        "         WHERE D.ID_UTENTE_INSERIMENTO_DELEGA = PVU.ID_UTENTE_LOGIN) " + 
        "       AS DENOMINAZIONE_UTENTE, "+
        "       (SELECT PVU.DENOMINAZIONE " +
        "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
        "        WHERE D.ID_UTENTE_INSERIMENTO_DELEGA = PVU.ID_UTENTE_LOGIN) " +
        "       AS DESCRIZIONE_ENTE_APPARTENENZA, " +
        "       D.ID_UFFICIO_ZONA_INTERMEDIARIO, " +
        "       D.UFFICIO_FASCICOLO, " +
        "       D.ID_UTENTE_FINE_DELEGA, " +
        "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
        "          || ' ' " + 
        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
        "         WHERE D.ID_UTENTE_FINE_DELEGA = PVU.ID_UTENTE_LOGIN) " + 
        "       AS DENOMINAZIONE2_UTENTE, "+
        "       D.INDIRIZZO, " +
        "       D.COMUNE, " +
        "       D.CAP, " +
        "       D.CODICE_AMMINISTRAZIONE, " +
        "       D.RECAPITO, " +
        "       D.DATA_INIZIO_MANDATO, " +
        "       D.DATA_FINE_MANDATO, " +
        "       D.DATA_RICEVUTA_RITORNO_REVOCA, " +
        " (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
                "          || ' ' " + 
                "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
                "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
                "         WHERE D.ID_UTENTE_REVOCA = PVU.ID_UTENTE_LOGIN) " + 
                "       AS DENOM_UTENTE_RIC_REVOCA, "+
                "       (SELECT PVU.DENOMINAZIONE " +
                "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
                "        WHERE D.ID_UTENTE_REVOCA = PVU.ID_UTENTE_LOGIN) " +
                "       AS DESC_ENTE_UTENTE_RIC_REVOCA " +
        "FROM   DB_DELEGA D, " +
        "       DB_INTERMEDIARIO I " +
        /*"       PAPUA_V_UTENTE_LOGIN PVU, "+
        "       PAPUA_V_UTENTE_LOGIN PVU2, " +
        "       PAPUA_V_UTENTE_LOGIN PVU3 " +*/
        "WHERE  D.ID_INTERMEDIARIO = I.ID_INTERMEDIARIO "+
        "AND    D.ID_AZIENDA = ? "+
        "AND    (D.DATA_FINE IS NOT NULL OR D.DATA_RICEVUTA_RITORNO_REVOCA IS NOT NULL) " +
        "AND    D.ID_PROCEDIMENTO = ? ";
       /* "AND    D.ID_UTENTE_INSERIMENTO_DELEGA = PVU.ID_UTENTE_LOGIN " +
        "AND    D.ID_UTENTE_FINE_DELEGA = PVU2.ID_UTENTE_LOGIN(+) " +
        "AND    D.ID_UTENTE_REVOCA = PVU3.ID_UTENTE_LOGIN(+) ";*/

			String ordinamento = "";
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
				ordinamento = "ORDER BY "+criterio;
			}
			if(!ordinamento.equals("")) 
			{
				query += ordinamento;
			}

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getStoricoDelegheByAziendaAndIdProcedimento method in DelegaDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ID_PROCEDIMENTO] in getStoricoDelegheByAziendaAndIdProcedimento method in DelegaDAO: "+idProcedimento+"\n");
			stmt.setLong(1, idAzienda.longValue());
			stmt.setLong(2, idProcedimento.longValue());

			SolmrLogger.debug(this, "Executing query: "+query);
			ResultSet rs = stmt.executeQuery();

			while(rs.next()) 
			{
        DelegaVO delegaVO = new DelegaVO();
        delegaVO.setIdDelega(new Long(rs.getLong("ID_DELEGA")));
        delegaVO.setIdIntermediario(new Long(rs.getLong("INTERM")));
        delegaVO.setDenomIntermediario(rs.getString("ALIAS_DESC_INTERM"));
        delegaVO.setCodiceFiscaleIntermediario(rs.getString("CODICE_FISCALE"));
        delegaVO.setIdProcedimento(new Long(rs.getLong("ID_PROCEDIMENTO")));
        delegaVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        delegaVO.setDataInizio(rs.getDate("DATA_INIZIO"));
        delegaVO.setDataFine(rs.getDate("DATA_FINE"));
        delegaVO.setIdUtenteInsDelega(new Long(rs.getLong("ID_UTENTE_INSERIMENTO_DELEGA")));
        delegaVO.setNomeUtenIrideIndDelega(rs.getString("DENOMINAZIONE_UTENTE"));
        delegaVO.setEnteUtenIrideIndDelega(rs.getString("DESCRIZIONE_ENTE_APPARTENENZA"));
        delegaVO.setIdUfficioZonaIntermediario(new Long(rs.getLong("ID_UFFICIO_ZONA_INTERMEDIARIO")));
        delegaVO.setUfficioFascicolo(rs.getString("UFFICIO_FASCICOLO"));
        delegaVO.setIdUtenteFineDelega(new Long(rs.getLong("ID_UTENTE_FINE_DELEGA")));
        delegaVO.setNomeUtenIrideFineDelega(rs.getString("DENOMINAZIONE2_UTENTE"));
        delegaVO.setIndirizzo(rs.getString("INDIRIZZO"));
        delegaVO.setIstaComune(rs.getString("COMUNE"));
        delegaVO.setCap(rs.getString("CAP"));
        delegaVO.setCodiceAmministrazione(rs.getString("CODICE_AMMINISTRAZIONE"));
        delegaVO.setRecapito(rs.getString("RECAPITO"));
        delegaVO.setDataInizioMandato(rs.getDate("DATA_INIZIO_MANDATO"));
        delegaVO.setDataFineMandato(rs.getDate("DATA_FINE_MANDATO"));
        delegaVO.setDataRicevutaRitornoDelega(rs.getDate("DATA_RICEVUTA_RITORNO_REVOCA"));
        delegaVO.setNomeUtenIrideRicRevoca(rs.getString("DENOM_UTENTE_RIC_REVOCA"));
        delegaVO.setEnteUtenIrideRicRevoca(rs.getString("DESC_ENTE_UTENTE_RIC_REVOCA"));
        elencoDeleghe.add(delegaVO);
			}

			rs.close();
			stmt.close();
    }
    catch(SQLException exc) 
    {
			SolmrLogger.error(this, "getStoricoDelegheByAziendaAndIdProcedimento in DelegaDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) 
		{
			SolmrLogger.error(this, "getStoricoDelegheByAziendaAndIdProcedimento in DelegaDAO - Generic Exception: "+ex+"\n");
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
				SolmrLogger.error(this, "getStoricoDelegheByAziendaAndIdProcedimento in DelegaDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) 
			{
				SolmrLogger.error(this, "getStoricoDelegheByAziendaAndIdProcedimento in DelegaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getStoricoDelegheByAziendaAndIdProcedimento method in DelegaDAO\n");
		if(elencoDeleghe.size() == 0) 
		{
			return (DelegaVO[])elencoDeleghe.toArray(new DelegaVO[0]);
		}
		else 
		{
			return (DelegaVO[])elencoDeleghe.toArray(new DelegaVO[elencoDeleghe.size()]);
		}
	}

  //Restituisce un array di oggetti DelegaAnagrafePadriVO contenente per ciascun
  //CUAA presente in ElencoCUAA la delega e le eventuali deleghe padri.
  public HashMap<String,DelegaAnagrafePadriVO> serviceGetDelegaConPadriByRange(String cuaa[],java.util.Date data)
      throws DataAccessException
  {
    Connection  conn    = null;
    PreparedStatement   stmt    = null;
    ResultSet   rs      = null;
    HashMap<String,DelegaAnagrafePadriVO>     result  = new HashMap<String,DelegaAnagrafePadriVO>();
    StringBuffer queryBuff = new StringBuffer("");

    try
    {
      StopWatch watcher = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
      watcher.start();

      conn = getDatasource().getConnection();

      Long idRichiesta = getNextPrimaryKey(SolmrConstants.SEQ_SMRGAA_WRK_DELEGA_CON_PADR);

      String insert="INSERT INTO SMRGAA_WRK_DELEGA_CON_PADRI (CUAA, ID_RICHIESTA) "+
                    "VALUES ( ?, ?) ";

      stmt = conn.prepareStatement(insert);

      // Setto il timeout possibile della query
      stmt.setQueryTimeout(SolmrConstants.TIME_WAIT_QUERY_RANGE_INSERT);

      for (int i=0;i<cuaa.length;i++)
      {
        stmt.setString(1,cuaa[i]);
        stmt.setLong(2,idRichiesta.longValue());
        stmt.addBatch();
      }
      stmt.executeBatch();

      watcher.dumpElapsed("DelegaDAO", "serviceGetDelegaConPadriByRange", "Esecuzione query inser", null);

      SolmrLogger.debug(this,"serviceGetDelegaConPadriByRange insert executed");

      stmt.close();

      String query = new String("SELECT FIGLIO.CODICE_FISCALE AS CODICE_FIGLIO, "+
                                "FIGLIO.DENOMINAZIONE AS DESCRIZIONE_FIGLIO, "+
                                "FIGLIO.LIVELLO AS TIPO_UFFICIO_FIGLIO, "+
                                "PADRE.CODICE_FISCALE AS CODICE_PADRE, "+
                                "PADRE.DENOMINAZIONE AS DESCRIZIONE_PADRE, "+
                                "NONNO.CODICE_FISCALE AS CODICE_NONNO, "+
                                "NONNO.DENOMINAZIONE AS DESCRIZIONE_NONNO, "+
                                "AA.CUAA, AA.DATA_INIZIO_VALIDITA AS DATA_INIZIO_VALIDITA "+
                                "FROM DB_ANAGRAFICA_AZIENDA AA, DB_DELEGA D,  DB_INTERMEDIARIO PADRE,  DB_INTERMEDIARIO FIGLIO, "+
                                "DB_INTERMEDIARIO  NONNO, SMRGAA_WRK_DELEGA_CON_PADRI DC "+
                                "WHERE AA.DATA_FINE_VALIDITA IS NULL "+
                                "  AND D.ID_AZIENDA=AA.ID_AZIENDA "+
                                "  AND D.ID_PROCEDIMENTO = "+SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE+
                                "  AND D.ID_INTERMEDIARIO = FIGLIO.ID_INTERMEDIARIO "+
                                "  AND FIGLIO.TIPO_INTERMEDIARIO = 'C' "+
                                "  AND FIGLIO.ID_INTERMEDIARIO_PADRE = PADRE.ID_INTERMEDIARIO(+) "+
                                "  AND PADRE.ID_INTERMEDIARIO_PADRE = NONNO.ID_INTERMEDIARIO(+) ");
      if (data==null) query+="  AND D.DATA_FINE IS NULL ";
      else query+="  AND D.DATA_INIZIO<=? AND (D.DATA_FINE > ? OR D.DATA_FINE IS NULL) ";
      query+="  AND AA.CUAA=DC.CUAA "+
             " and DC.ID_RICHIESTA = ? "+
             " ORDER BY DATA_INIZIO_VALIDITA";

      SolmrLogger.debug(this,"serviceGetDelegaConPadriByRange select executing "+query);

      stmt = conn.prepareStatement(query);

      // Setto il timeout possibile della query
      stmt.setQueryTimeout(SolmrConstants.TIME_WAIT_QUERY_RANGE_SELECT);

      int indice=0;

      if (data!=null)
      {
        stmt.setTimestamp(++indice, this.convertDateToTimestamp(data));
        stmt.setTimestamp(++indice, this.convertDateToTimestamp(data));
      }
      stmt.setLong(++indice,idRichiesta.longValue());



      rs = stmt.executeQuery();

      watcher.dumpElapsed("DelegaDAO", "serviceGetDelegaConPadriByRange", "Esecuzione query select", null);

      SolmrLogger.debug(this,"serviceGetDelegaConPadriByRange select executed");

      while (rs.next())
      {
        DelegaAnagrafePadriVO delega=new DelegaAnagrafePadriVO();
        String codiceFiglio=rs.getString("CODICE_FIGLIO");
        String codicePadre=rs.getString("CODICE_PADRE");
        String codiceNonno=rs.getString("CODICE_NONNO");
        String descrizioneFiglio=rs.getString("DESCRIZIONE_FIGLIO");
        String descrizionePadre=rs.getString("DESCRIZIONE_PADRE");
        String descrizioneNonno=rs.getString("DESCRIZIONE_NONNO");
        String tipoUfficioFiglio=rs.getString("TIPO_UFFICIO_FIGLIO");

        if (SolmrConstants.UFFICIO_ZONA.equals(tipoUfficioFiglio))
        {
          delega.setCodDelegaZona(codiceFiglio);
          delega.setDescDelegaZona(descrizioneFiglio);
          delega.setCodDelegaProvincia(codicePadre);
          delega.setDescDelegaProvincia(descrizionePadre);
          delega.setCodDelegaRegione(codiceNonno);
          delega.setDescDelegaRegione(descrizioneNonno);
        }
        else
        {
          if (SolmrConstants.UFFICIO_PROVINCIALE.equals(tipoUfficioFiglio))
          {
            delega.setCodDelegaProvincia(codiceFiglio);
            delega.setDescDelegaProvincia(descrizioneFiglio);
            delega.setCodDelegaRegione(codicePadre);
            delega.setDescDelegaRegione(descrizionePadre);
          }
          else
          {
            delega.setCodDelegaRegione(codiceFiglio);
            delega.setDescDelegaRegione(descrizioneFiglio);
          }
        }
        delega.setCuaa(rs.getString("CUAA"));
        result.put(delega.getCuaa(),delega);
      }
    }
    catch(SQLException ex) {
      SolmrLogger.fatal(this,"\n[DelegaDAO.serviceGetDelegaConPadriByRange()] \nSQLException> " + ex.getMessage() + "QueryName: " + queryBuff.toString() +"\n");
      throw new DataAccessException(ex.getMessage());
    }
    catch (Exception ex) {
     SolmrLogger.fatal(this,"\n[DelegaDAO.serviceGetDelegaConPadriByRange()] \nEccezione generica > " + ex.getMessage());
     throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException ex) {
        SolmrLogger.fatal(this,"\n[DelegaDAO.serviceGetDelegaConPadriByRange()] \nSQLException durante la chiusura dello Statement e della Connection > " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this,"\n[DelegaDAO.serviceGetDelegaConPadriByRange()] \nEccezione generica durante la chiusura dello Statement e della Connection > " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
    
  /***
   * ritorna true se esiste già una richiesta di revoca!!!
   *   
   *   
   *   
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public boolean existsRichiestaRevoca(long idAzienda) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating existsRichiestaRevoca method in DelegaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    boolean result = false;

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in existsRichiestaRevoca method in DelegaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in existsRichiestaRevoca method in DelegaDAO and it values: "+conn+"\n");

      String query = 
        "SELECT * " +
        "FROM  DB_DELEGA DE " +
        "WHERE DE.ID_AZIENDA = ? " +
        "AND   DE.DATA_RICEVUTA_RITORNO_REVOCA IS NOT NULL " +
        "AND   DE.DATA_FINE IS NULL";

      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in existsRichiestaRevoca method in DelegaDAO: "+idAzienda+"\n");

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idAzienda);

      SolmrLogger.debug(this, "Executing existsRichiestaRevoca: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      if(rs.next()) 
      {
        result = true;
      }

      rs.close();
      stmt.close();

    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "existsRichiestaRevoca in DelegaDAO - SQLException: "+exc.getMessage()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) 
    {
      SolmrLogger.error(this, "existsRichiestaRevoca in DelegaDAO - Generic Exception: "+ex+"\n");
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
        SolmrLogger.error(this, "existsRichiestaRevoca in DelegaDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "existsRichiestaRevoca in DelegaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated existsRichiestaRevoca method in DelegaDAO\n");
    
    return result;
  }


}
