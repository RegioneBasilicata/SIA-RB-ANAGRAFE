package it.csi.smranag.smrgaa.integration.terreni;

import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.dto.terreni.ConduzioneEleggibilitaVO;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Vector;

public class ConduzioneEleggibilitaGaaDAO extends it.csi.solmr.integration.BaseDAO
{

  public ConduzioneEleggibilitaGaaDAO() throws ResourceAccessException
  {
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public ConduzioneEleggibilitaGaaDAO(String refName) throws ResourceAccessException
  {
    super(refName);
  }

  
  /**
   * ritorna le'elncodelle conduzioni eleggibili relativi alal particella ed alla azienda.
   * Al momento esiste solo un record relativo all'eleggibilita' vino 
   * 
   * 
   * 
   * @param idAzienda
   * @param idParticella
   * @return
   * @throws DataAccessException
   */
  public Vector<ConduzioneEleggibilitaVO> getElencoCondElegAttivaByIdParticella(long idAzienda, 
      long idParticella)  throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<ConduzioneEleggibilitaVO> vConduzioneEleggibilitaVO = null;
    ConduzioneEleggibilitaVO conduzioneEleggibilitaVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[ConduzioneEleggibilitaGaaDAO::getElencoCondElegAttivaByIdParticella] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT CE.ID_CONDUZIONE_ELEGGIBILITA, " +
        "       CE.ID_PARTICELLA, " +
        "       CE.ID_AZIENDA, " +
        "       CE.ID_ELEGGIBILITA_FIT, " +
        "       CE.PERCENTUALE_UTILIZZO, " +
        "       CE.DATA_INIZIO_VALIDITA, " +
        "       CE.DATA_FINE_VALIDITA, " +
        "       CE.ID_UTENTE_AGGIORNAMENTO, " +
        "       CE.DATA_AGGIORNAMENTO " +
        "FROM   DB_CONDUZIONE_ELEGGIBILITA CE " +
        "WHERE  CE.ID_AZIENDA = ? " +
        "AND    CE.ID_PARTICELLA = ? " +
        "AND    CE.DATA_FINE_VALIDITA IS NULL ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ConduzioneEleggibilitaGaaDAO::getElencoCondElegAttivaByIdParticella] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int idx = 0;
      
      stmt.setLong(++idx, idAzienda);
      stmt.setLong(++idx, idParticella);
      
      
      
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {        
        if(vConduzioneEleggibilitaVO == null)
        {
          vConduzioneEleggibilitaVO = new Vector<ConduzioneEleggibilitaVO>();
        }
        
        conduzioneEleggibilitaVO = new ConduzioneEleggibilitaVO();
        conduzioneEleggibilitaVO.setIdConduzioneEleggibilita(rs.getLong("ID_CONDUZIONE_ELEGGIBILITA"));
        conduzioneEleggibilitaVO.setidParticella(rs.getLong("ID_PARTICELLA"));
        conduzioneEleggibilitaVO.setIdAzienda(rs.getLong("ID_AZIENDA"));
        conduzioneEleggibilitaVO.setIdEleggibilitaFit(rs.getInt("ID_ELEGGIBILITA_FIT"));
        conduzioneEleggibilitaVO.setPercentualeUtilizzo(rs.getBigDecimal("PERCENTUALE_UTILIZZO"));
        conduzioneEleggibilitaVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        conduzioneEleggibilitaVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        conduzioneEleggibilitaVO.setIdUtenteAggiornamento(rs.getLong("ID_UTENTE_AGGIORNAMENTO"));
        conduzioneEleggibilitaVO.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO"));
        
        vConduzioneEleggibilitaVO.add(conduzioneEleggibilitaVO);        
      }
      
      return vConduzioneEleggibilitaVO;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
        new Variabile("vConduzioneEleggibilitaVO", vConduzioneEleggibilitaVO),
        new Variabile("conduzioneEleggibilitaVO", conduzioneEleggibilitaVO)  };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {  new Parametro("idAzienda", idAzienda),
        new Parametro("idParticella", idParticella)   };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ConduzioneEleggibilitaGaaDAO::getElencoCondElegAttivaByIdParticella] ", t,
          query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
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
      SolmrLogger.debug(this,
          "[ConduzioneEleggibilitaGaaDAO::getElencoCondElegAttivaByIdParticella] END.");
    }
  }
  
  public Long insertConduzioneEleggibilita(ConduzioneEleggibilitaVO conduzioneEleggibilitaVO) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idConduzioneEleggibilita = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ConduzioneEleggibilitaGaaDAO::insertConduzioneEleggibilita] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idConduzioneEleggibilita = getNextPrimaryKey(SolmrConstants.SEQ_DB_CONDUZIONE_ELEGGIBILITA);
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("INSERT INTO DB_CONDUZIONE_ELEGGIBILITA   " + 
              "    (ID_CONDUZIONE_ELEGGIBILITA, " +
              "     ID_PARTICELLA, " +
              "     ID_AZIENDA, " +
              "     ID_ELEGGIBILITA_FIT, " +
              "     PERCENTUALE_UTILIZZO, " +
              "     DATA_INIZIO_VALIDITA, " +
              "     DATA_FINE_VALIDITA, " +
              "     ID_UTENTE_AGGIORNAMENTO, " +
              "     DATA_AGGIORNAMENTO) "
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
            "[ConduzioneEleggibilitaGaaDAO::insertConduzioneEleggibilita] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idConduzioneEleggibilita.longValue());
      stmt.setLong(++indice, conduzioneEleggibilitaVO.getidParticella());
      stmt.setLong(++indice, conduzioneEleggibilitaVO.getIdAzienda());
      stmt.setLong(++indice, conduzioneEleggibilitaVO.getIdEleggibilitaFit());
      stmt.setBigDecimal(++indice, conduzioneEleggibilitaVO.getPercentualeUtilizzo());
      stmt.setTimestamp(++indice, convertDateToTimestamp(new Date()));
      stmt.setTimestamp(++indice, null);
      stmt.setLong(++indice, conduzioneEleggibilitaVO.getIdUtenteAggiornamento());
      stmt.setTimestamp(++indice, convertDateToTimestamp(new Date()));
      
  
      stmt.executeUpdate();
      
      return idConduzioneEleggibilita;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idConduzioneEleggibilita", idConduzioneEleggibilita)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("conduzioneEleggibilitaVO", conduzioneEleggibilitaVO) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[IsolaParcellaGaaDAO::insertDBUnarParcella] ",
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
              "[ConduzioneEleggibilitaGaaDAO::insertConduzioneEleggibilita] END.");
    }
  }
  
  public void storicizzaConduzioneEleggibilita(long idConduzioneEleggibilita, long idUtente) 
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
              "[ConduzioneEleggibilitaGaaDAO::storicizzaConduzioneEleggibilita] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(""
         + "UPDATE DB_CONDUZIONE_ELEGGIBILITA   " 
         + "SET DATA_FINE_VALIDITA = ?, "
         + "    DATA_AGGIORNAMENTO = ?, "
         + "    ID_UTENTE_AGGIORNAMENTO = ? "
         + "WHERE ID_CONDUZIONE_ELEGGIBILITA = ? ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ConduzioneEleggibilitaGaaDAO::storicizzaConduzioneEleggibilita] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      stmt.setTimestamp(++indice, convertDateToTimestamp(new Date()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(new Date()));
      stmt.setLong(++indice, idUtente);
      stmt.setLong(++indice, idConduzioneEleggibilita);     
  
      stmt.executeUpdate();
      
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), 
        new Variabile("queryBuf", queryBuf)
        };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idConduzioneEleggibilita", idConduzioneEleggibilita),
        new Parametro("idUtente", idUtente) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[ConduzioneEleggibilitaGaaDAO::storicizzaConduzioneEleggibilita] ",
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
              "[ConduzioneEleggibilitaGaaDAO::storicizzaConduzioneEleggibilita] END.");
    }
  }
  
  public ConduzioneEleggibilitaVO getCondElegAttivaByIdParticellaForUv(long idAzienda, 
      long idParticella)  throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    ConduzioneEleggibilitaVO conduzioneEleggibilitaVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[ConduzioneEleggibilitaGaaDAO::getCondElegAttivaByIdParticellaForUv] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT CE.ID_CONDUZIONE_ELEGGIBILITA, " +
        "       CE.ID_PARTICELLA, " +
        "       CE.ID_AZIENDA, " +
        "       CE.ID_ELEGGIBILITA_FIT, " +
        "       CE.PERCENTUALE_UTILIZZO, " +
        "       CE.DATA_INIZIO_VALIDITA, " +
        "       CE.DATA_FINE_VALIDITA, " +
        "       CE.ID_UTENTE_AGGIORNAMENTO, " +
        "       CE.DATA_AGGIORNAMENTO " +
        "FROM   DB_CONDUZIONE_ELEGGIBILITA CE " +
        "WHERE  CE.ID_AZIENDA = ? " +
        "AND    CE.ID_PARTICELLA = ? " +
        "AND    CE.ID_ELEGGIBILITA_FIT = "+SolmrConstants.ELEGGIBILITA_FIT_VINO+" "+
        "AND    CE.DATA_FINE_VALIDITA IS NULL ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ConduzioneEleggibilitaGaaDAO::getCondElegAttivaByIdParticellaForUv] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int idx = 0;
      
      stmt.setLong(++idx, idAzienda);
      stmt.setLong(++idx, idParticella);
      
      
      
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      {                
        conduzioneEleggibilitaVO = new ConduzioneEleggibilitaVO();
        conduzioneEleggibilitaVO.setIdConduzioneEleggibilita(rs.getLong("ID_CONDUZIONE_ELEGGIBILITA"));
        conduzioneEleggibilitaVO.setidParticella(rs.getLong("ID_PARTICELLA"));
        conduzioneEleggibilitaVO.setIdAzienda(rs.getLong("ID_AZIENDA"));
        conduzioneEleggibilitaVO.setIdEleggibilitaFit(rs.getInt("ID_ELEGGIBILITA_FIT"));
        conduzioneEleggibilitaVO.setPercentualeUtilizzo(rs.getBigDecimal("PERCENTUALE_UTILIZZO"));
        conduzioneEleggibilitaVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        conduzioneEleggibilitaVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        conduzioneEleggibilitaVO.setIdUtenteAggiornamento(rs.getLong("ID_UTENTE_AGGIORNAMENTO"));
        conduzioneEleggibilitaVO.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO"));        
      }
      
      return conduzioneEleggibilitaVO;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
        new Variabile("conduzioneEleggibilitaVO", conduzioneEleggibilitaVO)  };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {  new Parametro("idAzienda", idAzienda),
        new Parametro("idParticella", idParticella)   };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ConduzioneEleggibilitaGaaDAO::getCondElegAttivaByIdParticellaForUv] ", t,
          query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
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
      SolmrLogger.debug(this,
          "[ConduzioneEleggibilitaGaaDAO::getCondElegAttivaByIdParticellaForUv] END.");
    }
  }

}
