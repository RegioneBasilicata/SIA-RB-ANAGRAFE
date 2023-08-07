package it.csi.smranag.smrgaa.integration;


import it.csi.smranag.smrgaa.dto.fascicoli.InvioFascicoliVO;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class SchedulazioneFascicoliDAO extends it.csi.solmr.integration.BaseDAO {
  
  
  public SchedulazioneFascicoliDAO() throws ResourceAccessException{
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public SchedulazioneFascicoliDAO(String refName) throws ResourceAccessException 
  {
    super(refName);
  }
  
  
  /**
   * 
   * Ritorna l'ultimo stato schedulazione
   * della dichiarazione di consistenza
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public InvioFascicoliVO getLastSchedulazione(long idDichiarazioneConsistenza) throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    InvioFascicoliVO invioFascicoliVO = null;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[SchedulazioneFascicoliDAO::getLastSchedulazione] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
      		"SELECT IF.ID_INVIO_FASCICOLI, " +
      		"       IF.ID_DICHIARAZIONE_CONSISTENZA, " +
      		"       IF.DATA_RICHIESTA, " +
      		"       IF.DATA_INVIO, " +
      		"       IF.DATA_CONCLUSIONE, " +
      		"       IF.STATO_INVIO, " +
      		"       IF.FLAG_DATI_ANAGRAFICI, " +
      		"       IF.FLAG_TERRENI, " +
      		"       IF.FLAG_FABBRICATI, " +
      		"       IF.FLAG_CC, " +
      		"       IF.FLAG_UV, " + 
      		"       IF.NR_TENTATIVI_INVIO, " +
      		"       IF.ID_UTENTE_AGGIORNAMENTO, " +
      		"       IF.DATA_AGGIORNAMENTO " +
          "FROM   DB_INVIO_FASCICOLI IF " +
          "WHERE  IF.ID_DICHIARAZIONE_CONSISTENZA = ? " +
          "ORDER BY " +
          "       IF.DATA_AGGIORNAMENTO DESC");
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
                "[SchedulazioneFascicoliDAO::getLastSchedulazione] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idDichiarazioneConsistenza);
      
      rs = stmt.executeQuery();
      
      if(rs.next())
      {
        invioFascicoliVO = new InvioFascicoliVO();
        invioFascicoliVO.setIdInvioFascicoli(checkLong(rs.getString("ID_INVIO_FASCICOLI")));
        invioFascicoliVO.setIdDichiarazioneConsistenza(checkLong(rs.getString("ID_DICHIARAZIONE_CONSISTENZA")));
        invioFascicoliVO.setDataRichiesta(rs.getTimestamp("DATA_RICHIESTA"));
        invioFascicoliVO.setDataInvio(rs.getTimestamp("DATA_INVIO"));
        invioFascicoliVO.setDataConclusione(rs.getTimestamp("DATA_CONCLUSIONE"));
        invioFascicoliVO.setStatoInvio(rs.getString("STATO_INVIO"));
        invioFascicoliVO.setFlagDatiAnagrafici(rs.getString("FLAG_DATI_ANAGRAFICI"));
        invioFascicoliVO.setFlagTerreni(rs.getString("FLAG_TERRENI"));
        invioFascicoliVO.setFlagFabbricati(rs.getString("FLAG_FABBRICATI"));
        invioFascicoliVO.setFlagCc(rs.getString("FLAG_CC"));
        invioFascicoliVO.setFlagUv(rs.getString("FLAG_UV"));
        invioFascicoliVO.setNumTentativiInvio(rs.getInt("NR_TENTATIVI_INVIO"));
        invioFascicoliVO.setIdUtenteAggiornamento(rs.getLong("ID_UTENTE_AGGIORNAMENTO"));
        invioFascicoliVO.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO"));
        
      }
      
      
      return invioFascicoliVO;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("invioFascicoliVO", invioFascicoliVO), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[SchedulazioneFascicoliDAO::getLastSchedulazione] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[SchedulazioneFascicoliDAO::getLastSchedulazione] END.");
    }
  }
  
  /**
   * Mi controlla se per l'azienda corrente esiste oltra lal dichiarazione di consistenza 
   * selezionata una dichiarazione in stato schedulato o inviato a sian
   * 
   * 
   * 
   * 
   * @param idAzienda
   * @param idDichiarazioneConsistenza
   * @return
   * @throws DataAccessException
   */
  public boolean trovaSchedulazioneAttiva(long idAzienda, long idDichiarazioneConsistenza) 
      throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    boolean trovato = false;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[SchedulazioneFascicoliDAO::trovaSchedulazioneAttiva] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT DC.ID_DICHIARAZIONE_CONSISTENZA, " +
          "       IF.STATO_INVIO " +
          "FROM   DB_DICHIARAZIONE_CONSISTENZA DC, " +
          "       DB_INVIO_FASCICOLI IF " +
          "WHERE  DC.ID_AZIENDA = ? " +
          "AND    DC.ID_DICHIARAZIONE_CONSISTENZA = IF.ID_DICHIARAZIONE_CONSISTENZA " +
          "AND    IF.DATA_AGGIORNAMENTO = (SELECT MAX(IF2.DATA_AGGIORNAMENTO) " +
          "                                FROM DB_INVIO_FASCICOLI IF2 " +
          "                                WHERE IF2.ID_DICHIARAZIONE_CONSISTENZA = IF.ID_DICHIARAZIONE_CONSISTENZA) " +
          "AND    DC.ID_DICHIARAZIONE_CONSISTENZA <> ? ");
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
                "[SchedulazioneFascicoliDAO::trovaSchedulazioneAttiva] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idAzienda);
      stmt.setLong(++idx, idDichiarazioneConsistenza);
      
      rs = stmt.executeQuery();
      
      while(rs.next() && !trovato)
      {
        String statoInvio = rs.getString("STATO_INVIO");
        if(SolmrConstants.SCHED_STATO_INVIATO.equalsIgnoreCase(statoInvio)
           || SolmrConstants.SCHED_STATO_SCHEDULATO.equalsIgnoreCase(statoInvio))
        {
          trovato = true;
        }
        
      }      
      
      return trovato;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      {  new Parametro("idAzienda", idAzienda),
         new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("trovato", trovato), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[SchedulazioneFascicoliDAO::trovaSchedulazioneAttiva] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[SchedulazioneFascicoliDAO::trovaSchedulazioneAttiva] END.");
    }
  }
  
  
  
  public Long insertInvioFascicoli(InvioFascicoliVO invioFascicoliVO, long idUtente) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idInvioFascicoli = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[SchedulazioneFascicoliDAO::invioFascicoliVO] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idInvioFascicoli = getNextPrimaryKey(SolmrConstants.SEQ_DB_INVIO_FASCICOLI);
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
      		"INSERT INTO DB_INVIO_FASCICOLI " + 
          "     (ID_INVIO_FASCICOLI, " +
          "     ID_DICHIARAZIONE_CONSISTENZA, " +
          "     DATA_RICHIESTA, " +
          "     DATA_INVIO, " +
          "     DATA_CONCLUSIONE, " +
          "     STATO_INVIO, " +
          "     FLAG_DATI_ANAGRAFICI, " +
          "     FLAG_TERRENI, " +
          "     FLAG_FABBRICATI, " +
          "     FLAG_CC, " +
          "     FLAG_UV, " +
          "     NR_TENTATIVI_INVIO, " +
          "     ID_UTENTE_AGGIORNAMENTO, " +
          "     DATA_AGGIORNAMENTO )  " +
          "   VALUES(?,?,?,?,?,?,?,?,?,?," +
          "          ?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[SchedulazioneFascicoliDAO::invioFascicoliVO] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idInvioFascicoli.longValue());
      stmt.setLong(++indice, invioFascicoliVO.getIdDichiarazioneConsistenza().longValue());
      stmt.setTimestamp(++indice, convertDateToTimestamp(invioFascicoliVO.getDataRichiesta()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(invioFascicoliVO.getDataInvio()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(invioFascicoliVO.getDataConclusione()));
      stmt.setString(++indice, invioFascicoliVO.getStatoInvio());
      stmt.setString(++indice, invioFascicoliVO.getFlagDatiAnagrafici());
      stmt.setString(++indice, invioFascicoliVO.getFlagTerreni());
      stmt.setString(++indice, invioFascicoliVO.getFlagFabbricati());
      stmt.setString(++indice, invioFascicoliVO.getFlagCc());
      stmt.setString(++indice, invioFascicoliVO.getFlagUv());
      stmt.setInt(++indice, invioFascicoliVO.getNumTentativiInvio());
      stmt.setLong(++indice, idUtente);     
      stmt.setTimestamp(++indice, convertDateToTimestamp(invioFascicoliVO.getDataAggiornamento()));
  
      stmt.executeUpdate();
      
      return idInvioFascicoli;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idInvioFascicoli", idInvioFascicoli)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("invioFascicoliVO", invioFascicoliVO),
        new Parametro("idUtente", idUtente)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[SchedulazioneFascicoliDAO::invioFascicoliVO] ",
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
              "[SchedulazioneFascicoliDAO::invioFascicoliVO] END.");
    }
  }
  
  
  public void deleteInvioFascicoli(long idInvioFascicoli) 
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
              "[SchedulazioneFascicoliDAO::deleteInvioFascicoli] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   DELETE DB_INVIO_FASCICOLI WHERE ID_INVIO_FASCICOLI = ?  " );
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[SchedulazioneFascicoliDAO::deleteInvioFascicoli] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idInvioFascicoli);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idInvioFascicoli", idInvioFascicoli)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[SchedulazioneFascicoliDAO::deleteInvioFascicoli] ",
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
              "[SchedulazioneFascicoliDAO::deleteInvioFascicoli] END.");
    }
  }
  
  
  
  
}
