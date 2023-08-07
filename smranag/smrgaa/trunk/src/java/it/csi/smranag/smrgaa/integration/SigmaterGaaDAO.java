package it.csi.smranag.smrgaa.integration;


import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.dto.sigmater.ParticellaSigmaterVO;
import it.csi.smranag.smrgaa.dto.sigmater.RichiestaSigmaterVO;
import it.csi.smranag.smrgaa.dto.sigmater.SoggettoSigmaterVO;
import it.csi.smranag.smrgaa.dto.sigmater.TitolaritaParticellaSigVO;
import it.csi.smranag.smrgaa.dto.sigmater.TitolaritaSigmaterVO;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class SigmaterGaaDAO extends it.csi.solmr.integration.BaseDAO {
  
  
  public SigmaterGaaDAO() throws ResourceAccessException{
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public SigmaterGaaDAO(String refName) throws ResourceAccessException 
  {
    super(refName);
  }
  
  
  /**
   * 
   * verifica se è già presente un record con chiave 
   * id_azienda,cod_istat,sezione,nome_servizio.
   * Nel caso restituisco l'id_richiesta_sigmater.
   * Verifico se è già presente un log relativo servizio/azienda/sezione
   * 
   *
   * 
   * 
   * 
   * @param richSigVO
   * @return
   * @throws DataAccessException
   */
  public RichiestaSigmaterVO getRichiestaSigmater(RichiestaSigmaterVO richSigVO) throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    RichiestaSigmaterVO results = null;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[SigmaterDAO::getRichiestaSigmater] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
      		"SELECT RS.ID_RICHIESTA_SIGMATER, " +
      		"       RS.ID_AZIENDA, " +
      		"       RS.COD_ISTAT, " +
      		"       RS.SEZIONE, " +
      		"       RS.NOME_SERVIZIO," +
      		"       RS.DATA_ULTIMA_RICHIESTA " +
          "FROM " +
          "       DB_RICHIESTA_SIGMATER RS " +
          "WHERE " +
          "       RS.ID_AZIENDA = ? " +
          "AND    RS.NOME_SERVIZIO = UPPER(?) ");
      if(richSigVO.getCodIstat() != null)
      {
        queryBuf.append("" +
          "AND    RS.COD_ISTAT = ? ");
      }
      
      if(richSigVO.getSezione() != null)
      {
        queryBuf.append("" +
          "AND    RS.SEZIONE = ? ");
      }
         
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[SigmaterDAO::getIdRichiestaSigmater] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, richSigVO.getIdAzienda().longValue());
      stmt.setString(++idx, richSigVO.getNomeServizio());
      
      if(richSigVO.getCodIstat() != null)
      {
        stmt.setString(++idx, richSigVO.getCodIstat());
      }
      
      if(richSigVO.getSezione() != null)
      {
        stmt.setString(++idx, richSigVO.getSezione());
      }
      
      
      
      rs = stmt.executeQuery();
      
      if (rs.next())
      {
        results = new RichiestaSigmaterVO();
        results.setIdRichiestaSigmater(new Long(rs.getLong("ID_RICHIESTA_SIGMATER")));
        results.setIdAzienda(checkLongNull(rs.getString("ID_AZIENDA")));
        results.setCodIstat(rs.getString("COD_ISTAT"));
        results.setSezione(rs.getString("SEZIONE"));
        results.setNomeServizio(rs.getString("NOME_SERVIZIO"));
        results.setDataUltimaRichiesta(rs.getTimestamp("DATA_ULTIMA_RICHIESTA"));
      }
      
      
      return results;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("richSigVO", richSigVO) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("results", results), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[SigmaterDAO::getIdRichiestaSigmater] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[SigmaterDAO::getIdRichiestaSigmater] END.");
    }
  }
  
  
  /**
   * inserisce sulla tabella DB_RICHIESTA_SIGMATER
   * 
   * 
   * @param richSigVO
   * @return
   * @throws DataAccessException
   */
  public Long insertRichiestaSigmater(RichiestaSigmaterVO richSigVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idRichiestaSigmater = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[SigmaterDAO::insertRichiestaSigmater] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idRichiestaSigmater = getNextPrimaryKey(SolmrConstants.SEQ_DB_RICHIESTA_SIGMATER);
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_RICHIESTA_SIGMATER   " 
              + "     (ID_RICHIESTA_SIGMATER , "
              + "     ID_AZIENDA , " 
              + "     COD_ISTAT , "
              + "     SEZIONE ,   "
              + "     NOME_SERVIZIO , "
              + "     DATA_ULTIMA_RICHIESTA , "
              + "     DATA_AGGIORNAMENTO , "
              + "     CODICE_ESITO , "
              + "     DESCRIZIONE_ESITO) "
              + "   VALUES(?,?,?,?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[SigmaterDAO::insertRichiestaSigmater] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idRichiestaSigmater.longValue());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(richSigVO.getIdAzienda()));
      stmt.setString(++indice, richSigVO.getCodIstat());
      stmt.setString(++indice, richSigVO.getSezione());
      stmt.setString(++indice, richSigVO.getNomeServizio());
      stmt.setTimestamp(++indice, convertDateToTimestamp(richSigVO.getDataUltimaRichiesta()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(richSigVO.getDataAggiornamento()));
      stmt.setString(++indice, richSigVO.getCodiceEsito());
      stmt.setString(++indice, richSigVO.getDescrizioneEsito());
      
  
      stmt.executeUpdate();
      
      return idRichiestaSigmater;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idRichiestaSigmater", idRichiestaSigmater)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("richSigVO", richSigVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[SigmaterDAO::insertRichiestaSigmater] ",
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
              "[SigmaterDAO::insertRichiestaSigmater] END.");
    }
  }
  
  
  
  /**
   * 
   * update tabella DB_RICHIESTA_SIGMATER
   * 
   * 
   * @param richSigVO
   * @throws DataAccessException
   */
  public void updateRichiestaSigmater(RichiestaSigmaterVO richSigVO) 
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
              "[SigmaterDAO::updateRichiestaSigmater] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   UPDATE DB_RICHIESTA_SIGMATER   " 
              + "     SET ID_AZIENDA = ? , "
              + "     COD_ISTAT = ? ,   "
              + "     SEZIONE = ? , "
              + "     NOME_SERVIZIO = ?, "
              + "     DATA_ULTIMA_RICHIESTA = ? , "
              + "     DATA_AGGIORNAMENTO = ? , "
              + "     CODICE_ESITO = ? ,  "
              + "     DESCRIZIONE_ESITO = ? "
              + "   WHERE  "
              + "     ID_RICHIESTA_SIGMATER = ?  ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[SigmaterDAO::updateRichiestaSigmater] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(richSigVO.getIdAzienda()));
      stmt.setString(++indice, richSigVO.getCodIstat());
      stmt.setString(++indice, richSigVO.getSezione());
      stmt.setString(++indice, richSigVO.getNomeServizio());
      stmt.setTimestamp(++indice, convertDateToTimestamp(richSigVO.getDataUltimaRichiesta()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(richSigVO.getDataAggiornamento()));
      stmt.setString(++indice, richSigVO.getCodiceEsito());
      stmt.setString(++indice, richSigVO.getDescrizioneEsito());
      
      stmt.setLong(++indice, richSigVO.getIdRichiestaSigmater().longValue());
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("richSigVO", richSigVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[SigmaterDAO::updateRichiestaSigmater] ",
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
              "[SigmaterDAO::updateRichiestaSigmater] END.");
    }
  }
  
  /**
   * restituisce il soggetto dalla tabella DB_SOGGETTO_SIGMATER
   * 
   * 
   * @param idRichiestaSigmater
   * @throws DataAccessException
   */
  public SoggettoSigmaterVO getSoggettoSigmater(long idRichiestaSigmater) 
    throws DataAccessException
  {
    String query = null;
    int idx = 0;
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    SoggettoSigmaterVO soggSigVO = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[SigmaterGaaDAO::getSoggettoSigmater] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "SELECT " +
        "       SS.ID_SOGGETTO_SIGMATER, " +
        "       SS.ID_RICHIESTA_SIGMATER, " +
        "       SS.ID_SIGMATER, " +
        "       SS.TIPO_SOGGETTO, " +
        "       SS.COGNOME, " +
        "       SS.NOME, " +
        "       SS.SESSO, " +
        "       SS.DATA_NASCITA, " +
        "       SS.LUOGO_NASCITA, " +
        "       SS.CODICE_FISCALE, " +
        "       SS.DATA_AGGIORNAMENTO, " +
        "       SS.DENOMINAZIONE, " +
        "       SS.SEDE " +
        "FROM " +
        "       DB_SOGGETTO_SIGMATER SS " +
        "WHERE " +
        "       SS.ID_RICHIESTA_SIGMATER = ? ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[SigmaterGaaDAO::getSoggettoSigmater] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(++idx, idRichiestaSigmater);
      
      rs = stmt.executeQuery();
      
      if (rs.next())
      {
        soggSigVO = new SoggettoSigmaterVO();
        soggSigVO.setIdSoggettoSigmater(new Long(rs.getLong("ID_SOGGETTO_SIGMATER")));
        soggSigVO.setIdRichiestaSigmater(new Long(rs.getLong("ID_RICHIESTA_SIGMATER")));
        soggSigVO.setIdSigmater(new Long(rs.getLong("ID_SIGMATER")));
        soggSigVO.setTipoSoggetto(rs.getString("TIPO_SOGGETTO"));
        soggSigVO.setCognome(rs.getString("COGNOME"));
        soggSigVO.setNome(rs.getString("NOME"));
        soggSigVO.setSesso(rs.getString("SESSO"));
        soggSigVO.setDataNascita(rs.getTimestamp("DATA_NASCITA"));
        soggSigVO.setLuogoNascita(rs.getString("LUOGO_NASCITA"));
        soggSigVO.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
        soggSigVO.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO"));
        soggSigVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        soggSigVO.setSede(rs.getString("SEDE"));
      }
      
      
      return soggSigVO;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idRichiestaSigmater", idRichiestaSigmater) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("soggSigVO", soggSigVO), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[SigmaterDAO::getSoggettoSigmater] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[SigmaterDAO::getSoggettoSigmater] END.");
    }
  }
  
  
  
  
  /**
   * 
   * update tabella DB_SOGGETTO_SIGMATER
   * 
   * 
   * @param soggSigVO
   * @throws DataAccessException
   */
  public void updateSoggettoSigmater(SoggettoSigmaterVO soggSigVO) 
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
              "[SigmaterDAO::updateSoggettoSigmater] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   UPDATE DB_SOGGETTO_SIGMATER   " 
              + "     SET ID_SIGMATER = ?, "
              + "     TIPO_SOGGETTO = ?, "
              + "     COGNOME = ? , "
              + "     NOME = ? , "
              + "     SESSO = ? ,  "
              + "     DATA_NASCITA = ? , "
              + "     LUOGO_NASCITA = ? , "
              + "     CODICE_FISCALE = ? , "
              + "     DATA_AGGIORNAMENTO = ? , "
              + "     DENOMINAZIONE = ? , "
              + "     SEDE = ? "
              + "   WHERE  "
              + "     ID_RICHIESTA_SIGMATER = ?  ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[SigmaterDAO::updateSoggettoSigmater] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(soggSigVO.getIdSigmater()));
      stmt.setString(++indice, soggSigVO.getTipoSoggetto());
      stmt.setString(++indice, soggSigVO.getCognome());
      stmt.setString(++indice, soggSigVO.getNome());
      stmt.setString(++indice, soggSigVO.getSesso());
      stmt.setTimestamp(++indice, convertDateToTimestamp(soggSigVO.getDataNascita()));
      stmt.setString(++indice, soggSigVO.getLuogoNascita());
      stmt.setString(++indice, soggSigVO.getCodiceFiscale());
      stmt.setTimestamp(++indice, convertDateToTimestamp(soggSigVO.getDataAggiornamento()));
      stmt.setString(++indice, soggSigVO.getDenominazione());
      stmt.setString(++indice, soggSigVO.getSede());
      
      stmt.setLong(++indice, soggSigVO.getIdRichiestaSigmater().longValue());
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("soggSigVO", soggSigVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[SigmaterDAO::updateSoggettoSigmater] ",
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
              "[SigmaterDAO::updateSoggettoSigmater] END.");
    }
  }
  
  
  
  
  /**
   * 
   * inserimento sulla tabella DB_SOGGETTO_SIGMATER
   * 
   * 
   * @param soggSigVO
   * @return
   * @throws DataAccessException
   */
  public Long insertSoggettoSigmater(SoggettoSigmaterVO soggSigVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idSoggettoSigmater = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[SigmaterDAO::insertSoggettoSigmater] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idSoggettoSigmater = getNextPrimaryKey(SolmrConstants.SEQ_DB_SOGGETTO_SIGMATER);
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_SOGGETTO_SIGMATER   " 
              + "     (ID_SOGGETTO_SIGMATER , "
              + "     ID_RICHIESTA_SIGMATER , " 
              + "     ID_SIGMATER , "
              + "     TIPO_SOGGETTO ,   "
              + "     COGNOME , "
              + "     NOME , "
              + "     DENOMINAZIONE , "
              + "     SESSO , "
              + "     DATA_NASCITA , " 
              +	"	    LUOGO_NASCITA , "
              +	"     CODICE_FISCALE , " 
              + "     DATA_AGGIORNAMENTO , "
              +	"     SEDE) "
              + "   VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[SigmaterDAO::insertSoggettoSigmater] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idSoggettoSigmater.longValue());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(soggSigVO.getIdRichiestaSigmater()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(soggSigVO.getIdSigmater()));
      stmt.setString(++indice, soggSigVO.getTipoSoggetto());
      stmt.setString(++indice, soggSigVO.getCognome());
      stmt.setString(++indice, soggSigVO.getNome());
      stmt.setString(++indice, soggSigVO.getDenominazione());
      stmt.setString(++indice, soggSigVO.getSesso());
      stmt.setTimestamp(++indice, convertDateToTimestamp(soggSigVO.getDataNascita()));
      stmt.setString(++indice, soggSigVO.getLuogoNascita());
      stmt.setString(++indice, soggSigVO.getCodiceFiscale());
      stmt.setTimestamp(++indice, convertDateToTimestamp(soggSigVO.getDataAggiornamento()));
      stmt.setString(++indice, soggSigVO.getSede());
      
  
      stmt.executeUpdate();
      
      return idSoggettoSigmater;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idSoggettoSigmater", idSoggettoSigmater)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("soggSigVO", soggSigVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[SigmaterDAO::insertSoggettoSigmater] ",
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
              "[SigmaterDAO::insertSoggettoSigmater] END.");
    }
  }
  
  
  /**
   * delete dalla tabella DB_TITOLARITA_SIGMATER
   * 
   * 
   * @param idRichiestaSigmater
   * @throws DataAccessException
   */
  public void deleteTitolaritaSigmater(long idRichiestaSigmater) 
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
              "[SigmaterGaaDAO::deleteTitolaritaSigmater] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   DELETE DB_TITOLARITA_SIGMATER WHERE ID_RICHIESTA_SIGMATER = ?  " );
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[SigmaterGaaDAO::deleteTitolaritaSigmater] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idRichiestaSigmater);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idRichiestaSigmater", idRichiestaSigmater)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[SigmaterGaaDAO::deleteTitolaritaSigmater] ",
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
              "[SigmaterGaaDAO::deleteTitolaritaSigmater] END.");
    }
  }
  
  
  
  /**
   * 
   * inserimento sulla tabella DB_TITOLARITA_SIGMATER
   * 
   * 
   * @param titSigVO
   * @return
   * @throws DataAccessException
   */
  public Long insertTitolaritaSigmater(TitolaritaSigmaterVO titSigVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idTitolaritaSigmater = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[SigmaterDAO::insertTitolaritaSigmater] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idTitolaritaSigmater = getNextPrimaryKey(SolmrConstants.SEQ_DB_TITOLARITA_SIGMATER);
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_TITOLARITA_SIGMATER   " 
              + "     (ID_TITOLARITA_SIGMATER , "
              + "     ID_RICHIESTA_SIGMATER , " 
              + "     ID_SOGGETTO_SIGMATER , "
              + "     ID_DIRITTO_SIGMATER , "
              + "     QUOTA_NUMERATORE , "
              + "     QUOTA_DENOMINATORE , "
              + "     CODICE_REGIME , "
              + "     DESCRIZIONE_REGIME ) "
              + "   VALUES(?,?,?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[SigmaterDAO::insertTitolaritaSigmater] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idTitolaritaSigmater.longValue());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(titSigVO.getIdRichiestaSigmater()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(titSigVO.getIdSoggettoSigmater()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(titSigVO.getIdDirittoSigmater()));
      stmt.setString(++indice, titSigVO.getQuotaNumeratore());
      stmt.setString(++indice, titSigVO.getQuotaDenominatore());
      stmt.setString(++indice, titSigVO.getCodiceRegime());
      stmt.setString(++indice, titSigVO.getDescrizioneRegime());
  
      stmt.executeUpdate();
      
      return idTitolaritaSigmater;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idTitolaritaSigmater", idTitolaritaSigmater)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("titSigVO", titSigVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[SigmaterDAO::insertTitolaritaSigmater] ",
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
              "[SigmaterDAO::insertTitolaritaSigmater] END.");
    }
  }
  
  
  
  /**
   * restituisce la pk della tabella DB_TIPO_DIRITTO_SIGMATER
   * passando il campo codice
   * 
   * 
   * 
   * @param codice
   * @return
   * @throws DataAccessException
   */
  public Long getIdTipoDiritto(String codice) throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Long results = null;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[SigmaterDAO::getIdTipoDiritto] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT TD.ID_TIPO_DIRITTO " +
          "FROM " +
          "       DB_TIPO_DIRITTO TD " +
          "WHERE " +
          "       TD.CODICE = ? ");
         
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[SigmaterDAO::getIdTipoDiritto] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setString(++idx, codice);
      
      rs = stmt.executeQuery();
      
      if (rs.next())
      {
        results = new Long(rs.getLong("ID_TIPO_DIRITTO"));
      }
      
      
      return results;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("codice", codice) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("results", results), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[SigmaterDAO::getIdTipoDiritto] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[SigmaterDAO::getIdTipoDiritto] END.");
    }
  }
  
  
  
  /**
   * delete dalla tabella DB_TITOLARITA_PARTICELLA_SIG
   * partendo dal padre DB_TITOLARITA_SIGMATER
   * 
   * 
   * @param idTitolaritaSigmater
   * @throws DataAccessException
   */
  public void deleteTitolaritaParticellaSigFromPadre(long idRichiestaSigmater) 
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
              "[SigmaterGaaDAO::deleteTitolaritaParticellaSigFromPadre] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
         "DELETE " +
         "       DB_TITOLARITA_PARTICELLA_SIG " +
         "WHERE  ID_TITOLARITA_SIGMATER IN ( " +
         "                                  SELECT ID_TITOLARITA_SIGMATER " +
         "                                  FROM   DB_TITOLARITA_SIGMATER " +
         "                                  WHERE  ID_RICHIESTA_SIGMATER = ? )  " );
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[SigmaterGaaDAO::deleteTitolaritaParticellaSigFromPadre] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idRichiestaSigmater);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idRichiestaSigmater", idRichiestaSigmater)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[SigmaterGaaDAO::deleteTitolaritaParticellaSigFromPadre] ",
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
              "[SigmaterGaaDAO::deleteTitolaritaParticellaSigFromPadre] END.");
    }
  }
  
  
  /**
   * 
   * Restuisce un record da DB_PARTICELLA_SIGMATER
   * inserendo la chiave catastale
   * 
   * 
   * 
   * @param partSigVO
   * @return
   * @throws DataAccessException
   */
  public ParticellaSigmaterVO getParticellaSigmater(ParticellaSigmaterVO partSigVO) 
    throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    ParticellaSigmaterVO results = null;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[SigmaterDAO::getParticellaSigmater] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT PS.ID_PARTICELLA_SIGMATER, " +
          "       PS.COMUNE, " +
          "       PS.SEZIONE, " +
          "       PS.FOGLIO, " +
          "       PS.PARTICELLA, " +
          "       PS.SUBALTERNO, " +
          "       PS.DENOMINATORE, " +
          "       PS.EDIFICIALITA " +
          "FROM " +
          "       DB_PARTICELLA_SIGMATER PS " +
          "WHERE " +
          "       PS.FOGLIO = ? " +
          "AND    PS.PARTICELLA = ? ");
      if(partSigVO.getSezione() != null)
      {
        queryBuf.append("" +
          "AND    PS.SEZIONE = ? ");
      }
      
      if(partSigVO.getSubalterno() != null)
      {
        queryBuf.append("" +
          "AND    PS.SUBALTERNO = ? ");
      }
         
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[SigmaterDAO::getParticellaSigmater] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, partSigVO.getFoglio().longValue());
      stmt.setLong(++idx, partSigVO.getParticella().longValue());
      
      if(partSigVO.getSezione() != null)
      {
        stmt.setString(++idx, partSigVO.getSezione());
      }
      
      if(partSigVO.getSubalterno() != null)
      {
        stmt.setString(++idx, partSigVO.getSubalterno());
      }
      
      
      
      rs = stmt.executeQuery();
      
      if (rs.next())
      {
        results = new ParticellaSigmaterVO();
        results.setIdParticellaSigmater(new Long(rs.getLong("ID_PARTICELLA_SIGMATER")));
        results.setComune(rs.getString("COMUNE"));
        results.setSezione(rs.getString("SEZIONE"));
        results.setFoglio(checkLongNull(rs.getString("FOGLIO")));
        results.setParticella(checkLongNull(rs.getString("PARTICELLA")));
        results.setSubalterno(rs.getString("SUBALTERNO"));
        results.setDenominatore(rs.getString("DENOMINATORE"));
        results.setEdificialita(rs.getString("EDIFICIALITA"));
      }
      
      
      return results;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("partSigVO", partSigVO) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("results", results), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[SigmaterDAO::getParticellaSigmater] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[SigmaterDAO::getParticellaSigmater] END.");
    }
  }
  
  
  
  /**
   * 
   * inserisce un record su DB_TITOLARITA_PARTICELLA_SIG
   * 
   * 
   * @param titPartSigVO
   * @return
   * @throws DataAccessException
   */
  public Long insertTitolaritaParticellaSig(TitolaritaParticellaSigVO titPartSigVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idTitolaritaParticellaSig = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[SigmaterDAO::insertTitolaritaParticellaSig] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idTitolaritaParticellaSig = getNextPrimaryKey(SolmrConstants.SEQ_DB_TITOL_PARTICELLA_SIG);
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_TITOLARITA_PARTICELLA_SIG   " 
              + "     (ID_TITOLARITA_PARTICELLA_SIG , "
              + "     ID_TITOLARITA_SIGMATER , " 
              + "     ID_IMMOBILE_SIGMATER , "
              + "     ID_PARTICELLA_SIGMATER) "
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
            "[SigmaterDAO::insertTitolaritaParticellaSig] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idTitolaritaParticellaSig.longValue());
      stmt.setLong(++indice, titPartSigVO.getIdTitolaritaSigmater());
      stmt.setLong(++indice, titPartSigVO.getIdImmobileSigmater());
      stmt.setLong(++indice, titPartSigVO.getIdParticellaSigmater());
      
      stmt.executeUpdate();
      
      return idTitolaritaParticellaSig;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idTitolaritaParticellaSig", idTitolaritaParticellaSig)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("titPartSigVO", titPartSigVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[SigmaterDAO::insertTitolaritaParticellaSig] ",
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
              "[SigmaterDAO::insertTitolaritaParticellaSig] END.");
    }
  }
  
  /**
   * 
   * inserisce un record sulla tabella DB_PARTICELLA_SIGMATER 
   * 
   * 
   * @param partSigVO
   * @return
   * @throws DataAccessException
   */
  public Long insertParticellaSigmater(ParticellaSigmaterVO partSigVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idParticellaSigmater = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[SigmaterDAO::insertParticellaSigmater] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idParticellaSigmater = getNextPrimaryKey(SolmrConstants.SEQ_DB_PARTICELLA_SIGMATER);
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_PARTICELLA_SIGMATER   " 
              + "     (ID_PARTICELLA_SIGMATER , "
              + "     COMUNE , " 
              + "     SEZIONE , "
              + "     FOGLIO , "
              + "		  PARTICELLA , "
              +	"     SUBALTERNO , " 
              + "		  DENOMINATORE , "
              +	"     EDIFICIALITA ) "
              + "   VALUES(?,?,?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[SigmaterDAO::insertParticellaSigmater] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idParticellaSigmater.longValue());
      stmt.setString(++indice, partSigVO.getComune());
      stmt.setString(++indice, partSigVO.getSezione());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(partSigVO.getFoglio()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(partSigVO.getParticella()));
      stmt.setString(++indice, partSigVO.getSubalterno());
      stmt.setString(++indice, partSigVO.getDenominatore());
      stmt.setString(++indice, partSigVO.getEdificialita());
      
      
      stmt.executeUpdate();
      
      return idParticellaSigmater;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idParticellaSigmater", idParticellaSigmater)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("partSigVO", partSigVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[SigmaterDAO::insertParticellaSigmater] ",
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
              "[SigmaterDAO::insertParticellaSigmater] END.");
    }
  }
  
  
  /**
   * 
   * 
   * update della tabella DB_PARTICELLA_SIGMATER
   * 
   * 
   * @param partSigVO
   * @throws DataAccessException
   */
  public void updateParticellaSigmater(ParticellaSigmaterVO partSigVO) 
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
              "[SigmaterDAO::updateParticellaSigmater] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   UPDATE DB_PARTICELLA_SIGMATER   " 
              + "     SET COMUNE = ?, "
              + "     SEZIONE = ?, "
              + "     FOGLIO = ? , "
              + "     PARTICELLA = ? , "
              + "     SUBALTERNO = ? ,  "
              + "     DENOMINATORE = ? , "
              + "     EDIFICIALITA = ? "
              + "   WHERE  "
              + "     ID_PARTICELLA_SIGMATER = ?  ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[SigmaterDAO::updateParticellaSigmater] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setString(++indice, partSigVO.getComune());
      stmt.setString(++indice, partSigVO.getSezione());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(partSigVO.getFoglio()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(partSigVO.getParticella()));
      stmt.setString(++indice, partSigVO.getSubalterno());
      stmt.setString(++indice, partSigVO.getDenominatore());
      stmt.setString(++indice, partSigVO.getEdificialita());
      
      stmt.setLong(++indice, partSigVO.getIdParticellaSigmater());
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("partSigVO", partSigVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[SigmaterDAO::updateParticellaSigmater] ",
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
              "[SigmaterDAO::updateParticellaSigmater] END.");
    }
  }
  
  
  /**
   * Verifica se esiste almeno una titolarita du DB_TITOLARITA_SIMATER 
   * con idRichiestaSigmater
   * 
   * 
   * @param idRichiestaSigmater
   * @return
   * @throws DataAccessException
   */
  public boolean esisteTitolaritaSigmaterFromIdRichiesta(long idRichiestaSigmater) 
    throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    boolean results = false;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[SigmaterDAO::esisteTitolaritaSigmaterFromIdRichiesta] BEGIN.");
  
      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT TS.ID_TITOLARITA_SIGMATER " +
          "FROM " +
          "       DB_TITOLARITA_SIGMATER TS " +
          "WHERE " +
          "       TS.ID_RICHIESTA_SIGMATER = ? ");
         
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[SigmaterDAO::esisteTitolaritaSigmaterFromIdRichiesta] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idRichiestaSigmater);     
      
      rs = stmt.executeQuery();
      
      if (rs.next())
      {
        results = true;
      }
      
      
      return results;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idRichiestaSigmater", idRichiestaSigmater) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("results", results), new Variabile("idx", idx)
  
      };
      LoggerUtils.logDAOError(this,
          "[SigmaterDAO::esisteTitolaritaSigmaterFromIdRichiesta] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[SigmaterDAO::esisteTitolaritaSigmaterFromIdRichiesta] END.");
    }
  }
  
  
  /**
   * 
   * Controlla se esiste già un record con la chiave univoca 
   * (ID_TITOLARITA_SIGMATER,ID_IMMOBILE_SIGMATER,ID_PARTICELLA_SIGMATER).
   * Purtroppo sigmater restituisce valori per noi identici
   * 
   * 
   * @param titPartSigVO
   * @return
   * @throws DataAccessException
   */
  public boolean esisteTitolaritaParticellaSig(TitolaritaParticellaSigVO titPartSigVO) 
    throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    boolean results = false;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[SigmaterDAO::esisteTitolaritaParticellaSig] BEGIN.");
  
      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT TPS.ID_TITOLARITA_PARTICELLA_SIG " +
          "FROM " +
          "       DB_TITOLARITA_PARTICELLA_SIG TPS " +
          "WHERE " +
          "       TPS.ID_TITOLARITA_SIGMATER = ? " +
          "AND    TPS.ID_IMMOBILE_SIGMATER = ? " +
          "AND    TPS.ID_PARTICELLA_SIGMATER = ? ");
         
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[SigmaterDAO::esisteTitolaritaParticellaSig] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, titPartSigVO.getIdTitolaritaSigmater());
      stmt.setLong(++idx, titPartSigVO.getIdImmobileSigmater());
      stmt.setLong(++idx, titPartSigVO.getIdParticellaSigmater());     
      
      rs = stmt.executeQuery();
      
      if (rs.next())
      {
        results = true;
      }
      
      
      return results;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("titPartSigVO", titPartSigVO) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("results", results), new Variabile("idx", idx)
  
      };
      LoggerUtils.logDAOError(this,
          "[SigmaterDAO::getParticellaSigmater] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[SigmaterDAO::esisteTitolaritaParticellaSig] END.");
    }
  }
  
  
  
  
  
  
  
}
