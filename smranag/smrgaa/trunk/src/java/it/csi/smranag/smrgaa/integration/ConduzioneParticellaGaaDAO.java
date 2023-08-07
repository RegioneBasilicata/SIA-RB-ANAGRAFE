package it.csi.smranag.smrgaa.integration;

import it.csi.smranag.smrgaa.dto.BaseCodeDescription;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.dto.terreni.AvvicendamentoVO;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.anag.AnagParticellaExcelVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneDichiarataVO;
import it.csi.solmr.dto.anag.terreni.FiltriParticellareRicercaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.dto.anag.terreni.TipoUtilizzoVO;
import it.csi.solmr.dto.anag.terreni.TipoVarietaVO;
import it.csi.solmr.dto.anag.terreni.UtilizzoDichiaratoVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.HashMap;
import java.util.Vector;

public class ConduzioneParticellaGaaDAO extends
    it.csi.solmr.integration.BaseDAO
{

  public ConduzioneParticellaGaaDAO() throws ResourceAccessException
  {
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public ConduzioneParticellaGaaDAO(String refName)
      throws ResourceAccessException
  {
    super(refName);
  }

  /**
   * Restituisce una hashmap con chiave = Long(idDichiarazioneConsistenza) e
   * valore = java.util.Vector di BaseCodeDescription con code=idTitoloPossesso,
   * descrizione = desc titolo possesso e item = BigDecimal(superficieCondotta)
   * 
   * @param elencoIdDichiarazioneConsistenza
   * @return
   * @throws DataAccessException
   */
  public HashMap<Long,Vector<BaseCodeDescription>> getMapConduzioniPraticheParticellaByIdDichiarazioneConsistenza(
      long elencoIdDichiarazioneConsistenza[], long idParticella) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    HashMap<Long,Vector<BaseCodeDescription>> mapConduzioni = null;
    StringBuffer queryBuf = null;
    BaseCodeDescription bcd = null;
    Vector<BaseCodeDescription> vConduzioni = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[ConduzioneParticellaGaaDAO::getMapConduzioniPraticheParticellaByIdDichiarazioneConsistenza] BEGIN.");

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */

      queryBuf = new StringBuffer();
      queryBuf
          .append("   SELECT   "
              + "     DC.ID_DICHIARAZIONE_CONSISTENZA,   "
              + "     COND.ID_TITOLO_POSSESSO,   "
              + "     COND.SUPERFICIE_CONDOTTA,  "
              + "     TTP.DESCRIZIONE  "
              + "   FROM   "
              + "     DB_CONDUZIONE_DICHIARATA COND,   "
              + "     DB_DICHIARAZIONE_CONSISTENZA DC,   "
              + "     DB_TIPO_TITOLO_POSSESSO TTP  "
              + "   WHERE  "
              + "     COND.CODICE_FOTOGRAFIA_TERRENI = DC.CODICE_FOTOGRAFIA_TERRENI  "
              + "     AND COND.ID_PARTICELLA = ?"
              + "     AND DC.ID_DICHIARAZIONE_CONSISTENZA IN (");
      queryBuf
          .append(getIdListFromArrayForSQL(elencoIdDichiarazioneConsistenza));
      queryBuf
          .append(")     AND TTP.ID_TITOLO_POSSESSO = COND.ID_TITOLO_POSSESSO   "
              + "   ORDER BY   "
              + "     DC.ID_DICHIARAZIONE_CONSISTENZA, TTP.ID_TITOLO_POSSESSO ASC   ");
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ConduzioneParticellaGaaDAO::getMapConduzioniPraticheParticellaByIdDichiarazioneConsistenza] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      stmt.setLong(1,idParticella);

      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      mapConduzioni = new HashMap<Long,Vector<BaseCodeDescription>>();
      while (rs.next())
      {
        long idDichiarazioneConsistenza = rs
            .getLong("ID_DICHIARAZIONE_CONSISTENZA");
        Long idDichiarazioneLong = new Long(idDichiarazioneConsistenza);
        bcd = new BaseCodeDescription();
        bcd.setCode(rs.getLong("ID_TITOLO_POSSESSO"));
        bcd.setDescription(rs.getString("DESCRIZIONE"));
        bcd.setItem(rs.getBigDecimal("SUPERFICIE_CONDOTTA"));
        vConduzioni = (Vector<BaseCodeDescription>) mapConduzioni.get(idDichiarazioneLong);
        if (vConduzioni == null)
        {
          vConduzioni = new Vector<BaseCodeDescription>();
          mapConduzioni.put(idDichiarazioneLong, vConduzioni);
        }
        vConduzioni.add(bcd);
      }
      return mapConduzioni;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vConduzioni", vConduzioni), new Variabile("bcd", bcd),
          new Variabile("mapConduzioni", mapConduzioni) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("elencoIdDichiarazioneConsistenza",
          elencoIdDichiarazioneConsistenza) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[ConduzioneParticellaGaaDAO::getMapConduzioniPraticheParticellaByIdDichiarazioneConsistenza] ",
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
              "[ConduzioneParticellaGaaDAO::getMapConduzioniPraticheParticellaByIdDichiarazioneConsistenza] END.");
    }
  }
  
  /**
   * Return:     - 0 = non è stata richiesta alcuna istanza di riesame per la particella
   *             - 1 = E' stata richiesta un'istanza di riesame per la particella
   *             - 2 = l'isanza di riesame per la particella è gia stata lavorata dal GIS;  
   * 
   * 
   * @param idParticellaCertificata
   * @param idVarieta
   * @return
   * @throws DataAccessException
   */
  /*public Integer getValoreIstanzaRiesamePlSql(long idAzienda, long idParticella) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    CallableStatement cs = null;
    StringBuffer queryBuf = null;
    Integer result = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ConduzioneParticellaGaaDAO::getValoreIstanzaRiesamePlSql] BEGIN.");
      
       //FUNCTION CercaIstanzaRiesame( pIdAzienda       IN DB_AZIENDA.ID_AZIENDA%TYPE
       //                        , pIdParticella    IN DB_PARTICELLA.ID_PARTICELLA%TYPE
       //                        ) RETURN INTEGER IS
       
  
      // CONCATENAZIONE/CREAZIONE QUERY BEGIN. 
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{ ? = call PCK_SMRGAA_LIBRERIA.CercaIstanzaRiesame(?, ?)}");
      
      
      
      
      query = queryBuf.toString();
      // CONCATENAZIONE/CREAZIONE QUERY END. 
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ConduzioneParticellaGaaDAO::getValoreIstanzaRiesamePlSql] Query="
                + query);
      }
      
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.registerOutParameter(1,Types.INTEGER);
      cs.setLong(2, idAzienda);
      cs.setLong(3, idParticella);
      
      cs.executeUpdate();
      
      result = new Integer(cs.getInt(1));
      
      
      
      
      return result;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("result", result) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("idParticella", idParticella)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[ConduzioneParticellaGaaDAO::getValoreIstanzaRiesamePlSql] ",
              t, query, variabili, parametri);
      
      //Rimappo e rilancio l'eccezione come DataAccessException.
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      
       //Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       //ignora ogni eventuale eccezione)
       closePlSql(cs, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[ConduzioneParticellaGaaDAO::getValoreIstanzaRiesamePlSql] END.");
    }
  }*/
  
  /**
   * ritorna gli id conduzioni attivi di una particella
   * di una azienda
   * 
   * 
   * @param idAzienda
   * @param idParticella
   * @return
   * @throws DataAccessException
   */
  public Vector<Long> getIdConduzioneFromIdAziendaIdParticella(long idAzienda, long idParticella) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<Long> result = null;
    try
    {
      SolmrLogger.debug(this,
          "[ConduzioneParticellaGaaDAO::getIdConduzioneFromIdAziendaIdParticella] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT CP.ID_CONDUZIONE_PARTICELLA " +
        "FROM   DB_CONDUZIONE_PARTICELLA CP, " +
        "       DB_UTE U " +
        "WHERE  U.ID_AZIENDA = ? " +
        "AND    CP.ID_PARTICELLA = ? " +
        "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "AND    U.ID_UTE = CP.ID_UTE ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ConduzioneParticellaGaaDAO::getIdConduzioneFromIdAziendaIdParticella] Query="
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
        if(result == null)
        {
          result = new Vector<Long>();
        }
        
        result.add(checkLong(rs.getString("ID_CONDUZIONE_PARTICELLA")));        
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("result", result) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
          new Parametro("idParticella", idParticella)
      };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ConduzioneParticellaGaaDAO::getIdConduzioneFromIdAziendaIdParticella] ", t,
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
          "[ConduzioneParticellaGaaDAO::getIdConduzioneFromIdAziendaIdParticella] END.");
    }
  }
  
  
  /**
   * 
   * ritorna il numero di particella asservite per azienda/particella
   * 
   * 
   * @param idAzienda
   * @param idParticella
   * @return
   * @throws DataAccessException
   */
  public Vector<Long> getIdConduzioniAsserviteFromIdAziendaIdParticella(long idAzienda, long idParticella) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<Long> result = null;
    try
    {
      SolmrLogger.debug(this,
          "[ConduzioneParticellaGaaDAO::getIdConduzioniAsserviteFromIdAziendaIdParticella] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT CP.ID_CONDUZIONE_PARTICELLA " +
        "FROM   DB_CONDUZIONE_PARTICELLA CP, " +
        "       DB_UTE U " +
        "WHERE  U.ID_AZIENDA = ? " +
        "AND    CP.ID_PARTICELLA = ? " +
        "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "AND    U.ID_UTE = CP.ID_UTE " +
        "AND    CP.ID_TITOLO_POSSESSO = 5 ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ConduzioneParticellaGaaDAO::getIdConduzioniAsserviteFromIdAziendaIdParticella] Query="
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
        if(result == null)
        {
          result = new Vector<Long>();
        }
        
        result.add(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));    
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("result", result) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
          new Parametro("idParticella", idParticella)
      };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ConduzioneParticellaGaaDAO::getIdConduzioniAsserviteFromIdAziendaIdParticella] ", t,
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
          "[ConduzioneParticellaGaaDAO::getIdConduzioniAsserviteFromIdAziendaIdParticella] END.");
    }
  }
  
  /**
   * ritorna il totla della sup condotta di una particella di una azienda
   * 
   * 
   * 
   * @param idAzienda
   * @param idParticella
   * @return
   * @throws DataAccessException
   */
  public BigDecimal getSumSupCondottaAsservimentoParticellaAzienda(long idAzienda, long idParticella) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    BigDecimal result = null;
    try
    {
      SolmrLogger.debug(this,
          "[ConduzioneParticellaGaaDAO::getSumSupCondottaAsservimentoParticellaAzienda] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT SUM(CP.SUPERFICIE_CONDOTTA) AS TOT_SUP " +
        "FROM   DB_CONDUZIONE_PARTICELLA CP, " +
        "       DB_UTE U " +
        "WHERE  U.ID_AZIENDA = ? " +
        "AND    CP.ID_PARTICELLA = ? " +
        "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "AND    U.ID_UTE = CP.ID_UTE " +
        "AND    CP.ID_TITOLO_POSSESSO = 5 ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ConduzioneParticellaGaaDAO::getSumSupCondottaAsservimentoParticellaAzienda] Query="
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
        result = rs.getBigDecimal("TOT_SUP"); 
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("result", result) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("idParticella", idParticella) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ConduzioneParticellaGaaDAO::getSumSupCondottaAsservimentoParticellaAzienda] ", t,
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
          "[ConduzioneParticellaGaaDAO::getSumSupCondottaAsservimentoParticellaAzienda] END.");
    }
  }
  
  
  public Vector<Long> getIdConduzioneNoAsservimentoFromIdAziendaIdParticella(long idAzienda, long idParticella) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<Long> result = null;
    try
    {
      SolmrLogger.debug(this,
          "[ConduzioneParticellaGaaDAO::getIdConduzioneFromIdAziendaIdParticella] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT CP.ID_CONDUZIONE_PARTICELLA " +
        "FROM   DB_CONDUZIONE_PARTICELLA CP, " +
        "       DB_UTE U " +
        "WHERE  U.ID_AZIENDA = ? " +
        "AND    CP.ID_PARTICELLA = ? " +
        "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "AND    CP.ID_TITOLO_POSSESSO <> 5 " +
        "AND    U.ID_UTE = CP.ID_UTE ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ConduzioneParticellaGaaDAO::getIdConduzioneFromIdAziendaIdParticella] Query="
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
        if(result == null)
        {
          result = new Vector<Long>();
        }
        
        result.add(checkLong(rs.getString("ID_CONDUZIONE_PARTICELLA")));        
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("result", result) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
          new Parametro("idParticella", idParticella)
      };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ConduzioneParticellaGaaDAO::getIdConduzioneFromIdAziendaIdParticella] ", t,
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
          "[ConduzioneParticellaGaaDAO::getIdConduzioneFromIdAziendaIdParticella] END.");
    }
  }
  
  
  public Vector<AnagParticellaExcelVO> searchParticelleStabGisExcelByParameters(FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<AnagParticellaExcelVO> elencoParticelle = new Vector<AnagParticellaExcelVO>();
    StringBuffer queryBuf = null;
    String query = null;    
    try
    {
      SolmrLogger.debug(this,
          "[ConduzioneParticellaGaaDAO::searchParticelleStabGisExcelByParameters] BEGIN.");
      
      queryBuf = new StringBuffer();  
      // Prima parte della query: viene eseguita solo se l'utente non sceglie
      // di visualizzare solo le particella senza uso del suolo specificato
      queryBuf.append("" +
          "WITH PARTICELLE AS " +
          "  (SELECT DISTINCT PC.ID_PARTICELLA_CERTIFICATA," +
          "                   PC.ID_PARTICELLA " +
          "   FROM   DB_UTE U,  " +
          "          DB_CONDUZIONE_PARTICELLA CP, " +
          "          DB_PARTICELLA_CERTIFICATA PC " +
          "   WHERE  U.ID_AZIENDA = ?  " +
          "   AND    U.DATA_FINE_ATTIVITA IS NULL " + 
          "   AND    U.ID_UTE = CP.ID_UTE " +
          "   AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
          "   AND    CP.ID_PARTICELLA = PC.ID_PARTICELLA " +
          "   AND    PC.DATA_FINE_VALIDITA IS NULL " +
          "  ), " +
          "     PASCOLO_MAGRO AS " +
          "  (SELECT PM.ID_PARTICELLA_CERTIFICATA, PM.ID_FONTE, TF.DESCRIZIONE, SUM(PM.SUPERFICIE) AS SUPERFICIE " +
          "   FROM   DB_ESITO_PASCOLO_MAGRO PM, " +
          "          DB_TIPO_FONTE TF, " +
          "          PARTICELLE P " +
          "   WHERE  PM.ID_PARTICELLA_CERTIFICATA = P.ID_PARTICELLA_CERTIFICATA " +
          "   AND    PM.DATA_FINE_VALIDITA IS NULL " +
          "   AND    PM.ANNO_CAMPAGNA = (SELECT MAX(ANNO_CAMPAGNA) " +
          "                              FROM DB_ESITO_PASCOLO_MAGRO " +
          "                              WHERE ID_PARTICELLA_CERTIFICATA = PM.ID_PARTICELLA_CERTIFICATA) " +
          "   AND    PM.ID_FONTE = TF.ID_FONTE " +
          "   GROUP BY PM.ID_PARTICELLA_CERTIFICATA, PM.ID_FONTE, TF.DESCRIZIONE " + 
          "  ) ");     
      
      if (filtriParticellareRicercaVO.getIdUtilizzo().intValue() != 0)
      {
        queryBuf.append(
          " SELECT CP.ID_CONDUZIONE_PARTICELLA, " + 
          "        CP.ESITO_CONTROLLO, " +
          "        CP.ID_PARTICELLA, " + 
          "        SP.COMUNE, " +
          "        C.DESCOM, " +
          "        P.SIGLA_PROVINCIA, " +
          "        SP.SEZIONE, " +
          "        SP.FOGLIO, " +
          "        SP.PARTICELLA, " +
          "        SP.SUBALTERNO, " +
          "        SP.SUP_CATASTALE, " +
          "        SP.SUPERFICIE_GRAFICA, " +
          "        CP.ID_TITOLO_POSSESSO, " +
          "        TTP.DESCRIZIONE AS DESC_POSSESSO, " +
          "        CP.SUPERFICIE_CONDOTTA, " +
          "        CP.PERCENTUALE_POSSESSO, " +
          "        UP.ID_UTILIZZO_PARTICELLA, " +
          "        TU.CODICE AS COD_PRIMARIO, " +
          "        TU.DESCRIZIONE AS DESC_PRIMARIO, " +
          "        TV.DESCRIZIONE AS DESC_VARIETA, " +
          "        TV.CODICE_VARIETA AS COD_VARIETA, " +
          "        UP.SUPERFICIE_UTILIZZATA, " +
          "        CP.ID_UTE, " +
          "        U.COMUNE AS COM_UTE, " +
          "        C2.DESCOM AS DESC_COM_UTE, " +
          "        PC.SUP_USO_GRAFICA, " +
          "        PC.ID_PARTICELLA_CERTIFICATA, " +
          "        TV.ID_VARIETA, " +
          "        TMU.ALLINEA_A_ELEGGIBILE, " +
          "        PAS.SUPERFICIE AS SUP_REG_PASCOLI, " +
          "        PAS.DESCRIZIONE AS DESC_FONTE_REG_PASCOLI, " +
          "        NVL(PCK_SMRGAA_LIBRERIA.Sup_Eleggibile_Riproporzionata(?, CP.ID_PARTICELLA, PC.ID_PARTICELLA_CERTIFICATA, UP.ID_UTILIZZO_PARTICELLA),0) AS SUP_ELEGGIBILE_RIP, " +
          "        NVL(PCK_SMRGAA_LIBRERIA.Calcola_P26(?, CP.ID_PARTICELLA, PC.ID_PARTICELLA_CERTIFICATA),0) AS P26 " +
          " FROM   DB_UTE U, " +
          "        DB_STORICO_PARTICELLA SP, " +
          "        DB_CONDUZIONE_PARTICELLA CP, " +
          "        COMUNE C, " +
          "        DB_TIPO_TITOLO_POSSESSO TTP, " +
          "        DB_UTILIZZO_PARTICELLA UP, " +
          "        DB_TIPO_UTILIZZO TU, " +
          "        DB_TIPO_VARIETA TV, " +
          "        PROVINCIA P, " +
          "        COMUNE C2, " +
          "        DB_FOGLIO F, " +
          "        DB_PARTICELLA_CERTIFICATA PC, " +
          "        PASCOLO_MAGRO PAS, " +
          "        DB_TIPO_MACRO_USO TMU, " +
          "        DB_TIPO_MACRO_USO_VARIETA TMUV ");
        
        if (filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S)
            || filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S))
        {
          queryBuf.append("" +
              "    ,DB_DICHIARAZIONE_CONSISTENZA DICH_CONS, " +
              "     DB_CONDUZIONE_DICHIARATA CD ");
        }
        
        // Metto in join DB_DICHIARAZIONE_SEGNALAZIONE solo se l'utente
        // ha specificato un determinato controllo effettuato sulle particelle
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo()))
        {
          queryBuf.append("   ,DB_DICHIARAZIONE_SEGNALAZIONE DS ");
        }
        
        //Tabelle per la ricerca sui documenti
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()) 
            || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
        {
          queryBuf.append("" +
              "   ,DB_DOCUMENTO_CONDUZIONE DOCC, " +
              "    DB_DOCUMENTO DOC, " +
              "    DB_TIPO_DOCUMENTO TDOC," +
              "    DB_DOCUMENTO_CATEGORIA DOCCAT ");
        }
        
        queryBuf.append("" +
          " WHERE  U.ID_AZIENDA = ? " +
          //" AND    U.DATA_FINE_ATTIVITA IS NULL " +
          " AND    U.ID_UTE = CP.ID_UTE " +
          " AND    U.COMUNE = C2.ISTAT_COMUNE ");
        // Estraggo le conduzione attive solo se il piano di riferimento è
        // "in lavorazione(solo conduzioni attive)"
        if (filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() == -1)
        {
          queryBuf.append(" AND    U.DATA_FINE_ATTIVITA IS NULL " +
              "   AND CP.DATA_FINE_CONDUZIONE IS NULL ");
        }
        queryBuf.append(
          " AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
          " AND    SP.DATA_FINE_VALIDITA IS NULL " +
          " AND    SP.COMUNE = C.ISTAT_COMUNE " +
          " AND    SP.ID_PARTICELLA = PC.ID_PARTICELLA(+) " +
          " AND    PC.DATA_FINE_VALIDITA(+) IS NULL " +
          " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) " +
          " AND    CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO " +
          " AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA " +
          " AND    UP.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
          " AND    UP.ID_VARIETA = TV.ID_VARIETA(+) " +
          " AND    PC.ID_PARTICELLA_CERTIFICATA = PAS.ID_PARTICELLA_CERTIFICATA(+) " +
          " AND    UP.ID_VARIETA = TMUV.ID_VARIETA (+) " +
          " AND    NVL(UP.ID_TIPO_DETTAGLIO_USO,-1) = NVL(TMUV.ID_TIPO_DETTAGLIO_USO (+),-1) " +
          " AND    TMUV.DATA_FINE_VALIDITA (+) IS NULL " +
          " AND    TMUV.ID_MACRO_USO = TMU.ID_MACRO_USO (+) " +
          " AND    TMU.DATA_FINE_VALIDITA (+) IS NULL ");
        
        
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdMacroUso()))
        {
          queryBuf.append("" +
              " AND TMU.ID_MACRO_USO = ? ");
        }
        
        // Se l'utente ha indicato l'ute di riferimento
        if (filtriParticellareRicercaVO.getIdUte() != null)
        {
          queryBuf.append(" AND U.ID_UTE = ? ");
        }
        
        // Metto in join la tabella DB_FOGLIO con DB_STORICO_PARTICELLA
        // solo se l'utente ha indicato la Z.V.N. la ZVF o la PSN di riferimento
        // nei filtri di ricerca
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaE()) || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaF())
            || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaPSN())
            || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaEFasciaFluviale()))
        {
          queryBuf.append(
              " AND    SP.COMUNE = F.COMUNE " +
              " AND    NVL(SP.SEZIONE,'-') = NVL(F.SEZIONE,'-') " +
              " AND    SP.FOGLIO = F.FOGLIO ");
          if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaE()))
          {
            queryBuf.append(" AND    F.ID_AREA_E = ? ");
          }
          if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaF()))
          {
            queryBuf.append(" AND    F.ID_AREA_F = ? ");
          }
          if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaPSN()))
          {
            queryBuf.append(" AND    F.ID_AREA_PSN = ? ");
          }
          if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaEFasciaFluviale()))
          {
            // in ZVN comprensivo di fascia fluviale
            if (filtriParticellareRicercaVO.getIdAreaEFasciaFluviale().longValue() == 1)
            {
              queryBuf.append("" +
                  "AND    (SP.ID_FASCIA_FLUVIALE IS NOT NULL " +
                  "            OR NVL(F.ID_AREA_E,0) = 2) ");
            }
            // fuori ZVN comprensivo di fascia fluviale
            else
            {
              queryBuf.append(
                  " AND    SP.ID_FASCIA_FLUVIALE IS NULL " + 
                  "AND    NVL(F.ID_AREA_E,1) = 1 ");
            }
          }
        }
        else
        {
          queryBuf.append("" +
              " AND    SP.COMUNE = F.COMUNE(+) " +
              " AND    NVL( SP.SEZIONE,'-') = NVL( F.SEZIONE(+),'-') " +
              " AND    SP.FOGLIO = F.FOGLIO(+) ");
        }
        
        //Combo documenti
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()) 
            || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento())
            || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
        {
          queryBuf.append(" AND CP.ID_CONDUZIONE_PARTICELLA = DOCC.ID_CONDUZIONE_PARTICELLA " +
                   " AND DOCC.ID_DOCUMENTO = DOC.ID_DOCUMENTO " +
                   " AND DOC.EXT_ID_DOCUMENTO = TDOC.ID_DOCUMENTO " +
                   " AND TDOC.ID_DOCUMENTO = DOCCAT.ID_DOCUMENTO " +
                   " AND DOCCAT.ID_CATEGORIA_DOCUMENTO = ? " +
                   " AND DOC.ID_STATO_DOCUMENTO IS NULL " +
                   " AND NVL(DOC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > SYSDATE ");
          
          if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
          {
            queryBuf.append(" AND TDOC.ID_DOCUMENTO = ? ");
          }
          
          if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
          {
            queryBuf.append(" AND DOC.ID_DOCUMENTO = ? ");
          }
          
        }
        
        //Notifiche
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipologiaNotifica())
            || (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCategoriaNotifica())))
        {
          queryBuf.append(
                  " AND  EXISTS  (SELECT NE.ID_NOTIFICA_ENTITA " + 
                  "               FROM   DB_NOTIFICA_ENTITA NE, " +
                  "                      DB_NOTIFICA NO, " +
                  "                      DB_TIPO_ENTITA TE " +
                  "               WHERE  TE.CODICE_TIPO_ENTITA = 'P' " +
                  "               AND    TE.ID_TIPO_ENTITA = NE.ID_TIPO_ENTITA " +
                  "               AND    NE.IDENTIFICATIVO = SP.ID_PARTICELLA " +
                  "               AND    NE.ID_NOTIFICA = NO.ID_NOTIFICA " +
                  "               AND    NO.ID_AZIENDA = U.ID_AZIENDA ");
          if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipologiaNotifica()))
          {
            queryBuf.append(
                  "               AND    NO.ID_TIPOLOGIA_NOTIFICA = ? ");          
          }
          if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCategoriaNotifica()))
          {
            queryBuf.append(
                  "               AND    NO.ID_CATEGORIA_NOTIFICA = ? ");          
          }
          if(Validator.isNotEmpty(filtriParticellareRicercaVO.getFlagNotificheChiuse()))
          {
            queryBuf.append(
                  "AND    (NE.DATA_FINE_VALIDITA IS NULL " +
                  "        OR NE.DATA_FINE_VALIDITA = (SELECT MAX(NE1.DATA_FINE_VALIDITA) " +
                  "                                    FROM   DB_NOTIFICA_ENTITA NE1 " +
                  "                                    WHERE  NE1.IDENTIFICATIVO = NE.IDENTIFICATIVO " +
                  "                                    AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA )) )");
          }
          else
          {
            queryBuf.append(
                  " AND    NE.DATA_FINE_VALIDITA IS NULL )");
          }
          
        }
        
        // Se l'utente ha indicato il comune di riferimento
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIstatComune()))
        {
          queryBuf.append(" AND SP.COMUNE = ? ");
        }
        // Se l'utente ha indicato la sezione di riferimento
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSezione()))
        {
          queryBuf.append(" AND SP.SEZIONE = ? ");
        }
        // Se l'utente ha indicato il foglio di riferimento
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getFoglio()))
        {
          queryBuf.append(" AND SP.FOGLIO = ? ");
        }
        // Se l'utente ha indicato la particella di riferimento
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getParticella()))
        {
          queryBuf.append(" AND SP.PARTICELLA = ? ");
        }
        // Se l'utente ha indicato il subalterno di riferimento
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSubalterno()))
        {
          queryBuf.append(" AND SP.SUBALTERNO = ? ");
        }
        // Se l'utente ha indicato il tipo area A
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaA()))
        {
          queryBuf.append(" AND SP.ID_AREA_A = ? ");
        }
        // Se l'utente ha indicato il tipo area B
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaB()))
        {
          queryBuf.append(" AND SP.ID_AREA_B = ? ");
        }
        // Se l'utente ha indicato il tipo area C
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaC()))
        {
          queryBuf.append(" AND SP.ID_AREA_C = ? ");
        }
        // Se l'utente ha indicato il tipo area D
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaD()))
        {
          queryBuf.append(" AND SP.ID_AREA_D = ? ");
        }
        // Se l'utente ha indicato il tipo area D
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaM()))
        {
          queryBuf.append(" AND SP.ID_AREA_M = ? ");
        }
        // Se l'utente ha indicato il tipo fascia fluviale
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdFasciaFluviale()))
        {
          queryBuf.append(" AND SP.ID_FASCIA_FLUVIALE = ? ");
        }
        // Se l'utente ha indicato il tipo Araea G
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaG()))
        {
          queryBuf.append(" AND NVL(SP.ID_AREA_G,1) = ? ");
        }
        // Se l'utente ha indicato il tipo Araea H
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaH()))
        {
          queryBuf.append(" AND NVL(SP.ID_AREA_H,1) = ? ");
        }
        // Se l'utente ha indicato il tipo Zona Altimetrica
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdZonaAltimetrica()))
        {
          queryBuf.append(" AND SP.ID_ZONA_ALTIMETRICA = ? ");
        }
        // Se l'utente ha indicato il tipo Caso Particolare
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCasoParticolare()))
        {
          queryBuf.append(" AND SP.ID_CASO_PARTICOLARE = ? ");
        }
        // Se l'utente invece ha specificato un particolare uso del suolo
        if (filtriParticellareRicercaVO.getIdUtilizzo().longValue() > 0)
        {
          // Se non è stato indicato nei filtri di ricerca se l'utilizzo
          // selezionato
          // è primario o secondario, viene usata come condizione di default
          // quella
          // dell'utilizzo primario
          if (!Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario())
              && !Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario()))
          {
            queryBuf.append(" AND UP.ID_UTILIZZO = ? ");
          }
          else
          {
            // Uso Primario
            if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario()))
            {
              queryBuf.append(" AND UP.ID_UTILIZZO = ? ");
            }
            // Uso Secondario
            if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario()))
            {
              queryBuf.append(" AND UP.ID_UTILIZZO_SECONDARIO = ? ");
            }
          }
          
        }
        // Se l'utente ha specificato un particolare titolo di possesso
        if (filtriParticellareRicercaVO.getIdTitoloPossesso() != null)
        {
          queryBuf.append(" AND CP.ID_TITOLO_POSSESSO = ? ");
        }
        else
        {
          // Se l'utente ha scelto l'opzione escludi asservimento
          if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckEscludiAsservimento())
              && filtriParticellareRicercaVO.getCheckEscludiAsservimento().equalsIgnoreCase(SolmrConstants.FLAG_S))
          {
            queryBuf.append(" AND CP.ID_TITOLO_POSSESSO <> " + SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO);
          }
          // Se l'utente ha scelto l'opzione escludi conferimento
          if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckEscludiConferimento())
              && filtriParticellareRicercaVO.getCheckEscludiConferimento().equalsIgnoreCase(SolmrConstants.FLAG_S))
          {
            queryBuf.append(" AND CP.ID_TITOLO_POSSESSO <> " + SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO);
          }
        }
        // Se l'utente ha specificato la tipologia di anomalia bloccante
        boolean isFirst = true;
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneBloccante()))
        {
          queryBuf.append(" AND (CP.ESITO_CONTROLLO = ? ");
          isFirst = false;
        }
        // Se l'utente ha specificato la tipologia di anomalia warning
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneWarning()))
        {
          if (!isFirst)
          {
            queryBuf.append(" OR ");
          }
          else
          {
            queryBuf.append(" AND (");
          }
          queryBuf.append(" CP.ESITO_CONTROLLO = ? ");
          isFirst = false;
        }
        // Se l'utente ha specificato la tipologia di anomalia OK
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneOk()))
        {
          if (!isFirst)
          {
            queryBuf.append(" OR ");
          }
          else
          {
            queryBuf.append(" AND (");
          }
          queryBuf.append(" CP.ESITO_CONTROLLO = ? ");
          isFirst = false;
        }
        if (!isFirst)
        {
          queryBuf.append(")");
        }
        
        // Se l'utente ha specificato un determinato tipo di controllo
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo()))
        {
          queryBuf.append(" " +
              "AND DS.ID_AZIENDA = ? " +
              " AND DS.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
              " AND DS.ID_DICHIARAZIONE_CONSISTENZA IS NULL " +
              " AND DS.ID_CONTROLLO = ? ");
        }
        
        // Ricerco i dati delle particelle solo asservite
        if (filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S))
        {
          queryBuf.append("" +
              " AND DICH_CONS.ID_AZIENDA <> ? " +
              " AND DICH_CONS.DATA_INSERIMENTO_DICHIARAZIONE = " +
              "     (SELECT MAX(DATA_INSERIMENTO_DICHIARAZIONE) " +
              "      FROM   DB_DICHIARAZIONE_CONSISTENZA DC1 " +
              "      WHERE  DICH_CONS.ID_AZIENDA = DC1.ID_AZIENDA" + "     ) " +
              " AND DICH_CONS.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
              " AND CD.ID_TITOLO_POSSESSO = ? " +
              " AND CD.ID_PARTICELLA = SP.ID_PARTICELLA ");
        }
        // Ricerco i dati delle particelle solo conferite
        if (filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S))
        {
          queryBuf.append(" AND DICH_CONS.ID_AZIENDA <> ? "
              + " AND DICH_CONS.DATA_INSERIMENTO_DICHIARAZIONE = (SELECT MAX(DATA_INSERIMENTO_DICHIARAZIONE) FROM DB_DICHIARAZIONE_CONSISTENZA DC1 WHERE DICH_CONS.ID_AZIENDA = DC1.ID_AZIENDA) "
              + " AND DICH_CONS.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " + " AND CD.ID_TITOLO_POSSESSO = ? "
              + " AND CD.ID_PARTICELLA = SP.ID_PARTICELLA ");
        }
        
        
        queryBuf.append(" " +
            "GROUP BY CP.ID_CONDUZIONE_PARTICELLA, " +
            "         CP.ESITO_CONTROLLO, " +
            "         CP.ID_PARTICELLA, " + 
            "         SP.COMUNE, " +
            "         C.DESCOM, " +
            "         P.SIGLA_PROVINCIA, " +
            "         SP.SEZIONE, " +
            "         SP.FOGLIO, " +
            "         SP.PARTICELLA, " +
            "         SP.SUBALTERNO, " +
            "         SP.SUP_CATASTALE, " +
            "         SP.SUPERFICIE_GRAFICA, " +
            "         CP.ID_TITOLO_POSSESSO, " + 
            "         TTP.DESCRIZIONE, "  + 
            "         CP.SUPERFICIE_CONDOTTA, " +
            "         CP.PERCENTUALE_POSSESSO, " + 
            "         UP.ID_UTILIZZO_PARTICELLA, " +
            "         TU.CODICE, " +
            "         TU.DESCRIZIONE, " +
            "         TV.DESCRIZIONE, " +
            "         TV.CODICE_VARIETA, " +
            "         UP.SUPERFICIE_UTILIZZATA, " +
            "         CP.ID_UTE, " +
            "         U.COMUNE, " +
            "         C2.DESCOM, " +
            "         PC.SUP_USO_GRAFICA, " +
            "         PC.ID_PARTICELLA_CERTIFICATA, " +
            "         TV.ID_VARIETA, " +
            "         TMU.ALLINEA_A_ELEGGIBILE, " +
            "         PAS.SUPERFICIE, " +
            "         PAS.DESCRIZIONE, " +
            "         SP.ID_PARTICELLA ");
      }
      // Metto le query in UNION solo se l'utente sceglie di visualizzare
      // "qualunque uso del suolo"
      if ((filtriParticellareRicercaVO.getIdUtilizzo().intValue() < 0) && (filtriParticellareRicercaVO.getIdMacroUso() == null))
      {
        queryBuf.append(" UNION ");
      }
      // La seconda parte della query viene eseguita solo se l'utente non ha
      // scelto un particolare uso del suolo da visualizzare
      if ((filtriParticellareRicercaVO.getIdUtilizzo().intValue() <= 0) && (filtriParticellareRicercaVO.getIdMacroUso() == null))
      {
        queryBuf.append(" " +
            " SELECT  CP.ID_CONDUZIONE_PARTICELLA, " +
            "         CP.ESITO_CONTROLLO, " +
            "         CP.ID_PARTICELLA, " + 
            "         SP.COMUNE, " +
            "         C.DESCOM, " +
            "         P.SIGLA_PROVINCIA, " +
            "         SP.SEZIONE, " +
            "         SP.FOGLIO, " +
            "         SP.PARTICELLA, " +
            "         SP.SUBALTERNO, " +
            "         SP.SUP_CATASTALE, " +
            "         SP.SUPERFICIE_GRAFICA, " +
            "         CP.ID_TITOLO_POSSESSO, " +
            "         TTP.DESCRIZIONE AS DESC_POSSESSO, " +
            "         CP.SUPERFICIE_CONDOTTA, " +
            "         CP.PERCENTUALE_POSSESSO, " +
            "         -1 AS ID_UTILIZZO_PARTICELLA, " +
            "         NULL AS COD_PRIMARIO, " +
            "         NULL AS DESC_PRIMARIO, " +
            "         NULL AS DESC_VARIETA, " +
            "         NULL AS COD_VAR_SECONDARIA, " +
            "         DECODE(CP.ID_TITOLO_POSSESSO, 5 , CP.SUPERFICIE_CONDOTTA, " +
            "         TRUNC(LEAST(DECODE(SP.SUPERFICIE_GRAFICA,0,SP.SUP_CATASTALE,SP.SUPERFICIE_GRAFICA), " +
            "                     DECODE(SP.SUP_CATASTALE,0,SP.SUPERFICIE_GRAFICA,SP.SUP_CATASTALE)) * CP.PERCENTUALE_POSSESSO / 100,4) " +
            "                - TRUNC(SUM(NVL(UP.SUPERFICIE_UTILIZZATA,0)),4)) " +
            "         SUPERFICIE_UTILIZZATA, " +
            "         CP.ID_UTE, " +
            "         U.COMUNE AS COM_UTE, " +
            "         C2.DESCOM AS DESC_COM_UTE, " +
            "         PC.SUP_USO_GRAFICA, " +
            "         -1 AS ID_PARTICELLA_CERTIFICATA, " +
            "         -1 AS ID_VARIETA, " +
            "         NULL AS ALLINEA_A_ELEGGIBILE, " +
            "         PAS.SUPERFICIE AS SUP_REG_PASCOLI, " +
            "         PAS.DESCRIZIONE AS DESC_FONTE_REG_PASCOLI, " +
            "         0 AS SUP_ELEGGIBILE_RIP, " +
            "         0 AS P26 " +
            " FROM    DB_UTE U, " +
            "         DB_STORICO_PARTICELLA SP, " +
            "         DB_CONDUZIONE_PARTICELLA CP, " +
            "         COMUNE C, " +
            "         DB_TIPO_TITOLO_POSSESSO TTP, " +
            "         DB_UTILIZZO_PARTICELLA UP, " +
            "         PROVINCIA P, " +
            "         COMUNE C2, " +
            "         DB_FOGLIO F, " +
            "         DB_PARTICELLA_CERTIFICATA PC, " +
            "         PASCOLO_MAGRO PAS ");
            
        // Se è attivo almeno uno dei due filtri tra solo asservite e solo
        // conferite deve andare in join con la
        // DB_DICHIARAZIONE_CONSISTENZA
        if (filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S)
            || filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S))
        {
          queryBuf.append("" +
              "    ,DB_DICHIARAZIONE_CONSISTENZA DICH_CONS, " +
              "     DB_CONDUZIONE_DICHIARATA CD ");
        }
        
        // Metto in join DB_DICHIARAZIONE_SEGNALAZIONE solo se l'utente
        // ha specificato un determinato controllo effettuato sulle particelle
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo()))
        {
          queryBuf.append("" +
              "    ,DB_DICHIARAZIONE_SEGNALAZIONE DS ");
        }
        
        //Tabelle per la ricerca sui documenti
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()) 
            || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
        {
          queryBuf.append("" +
              "   ,DB_DOCUMENTO_CONDUZIONE DOCC, " +
              "    DB_DOCUMENTO DOC, DB_TIPO_DOCUMENTO TDOC, " +
              "    DB_DOCUMENTO_CATEGORIA DOCCAT ");
        }
        
        queryBuf.append("" +
            " WHERE   U.ID_AZIENDA = ? " +
            //" AND     U.DATA_FINE_ATTIVITA IS NULL " +
            " AND     U.ID_UTE = CP.ID_UTE ");
        // Estraggo le conduzione attive solo se il piano di riferimento è
        // "in lavorazione(solo conduzioni attive)"
        if (filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() == -1)
        {
          queryBuf.append( " AND     U.DATA_FINE_ATTIVITA IS NULL " +
            " AND CP.DATA_FINE_CONDUZIONE IS NULL ");
        }
        queryBuf.append("" +
            " AND    U.COMUNE = C2.ISTAT_COMUNE " +
            " AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
            " AND    SP.DATA_FINE_VALIDITA IS NULL " +
            " AND    SP.COMUNE = C.ISTAT_COMUNE " +
            " AND    SP.COMUNE = F.COMUNE(+) " +
            " AND    NVL( SP.SEZIONE,'-') = NVL( F.SEZIONE(+),'-') " +
            " AND    SP.FOGLIO = F.FOGLIO(+) " +
            " AND    SP.ID_PARTICELLA = PC.ID_PARTICELLA(+) " +
            " AND    PC.DATA_FINE_VALIDITA(+) IS NULL " +
            " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) " +
            " AND    CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO " +
            " AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA(+) " +
            " AND    PC.ID_PARTICELLA_CERTIFICATA = PAS.ID_PARTICELLA_CERTIFICATA(+) ");
        
        
        
        
        
        
        // Se l'utente ha indicato l'ute di riferimento
        if (filtriParticellareRicercaVO.getIdUte() != null)
        {
          queryBuf.append(" AND U.ID_UTE = ? ");
        }
        
        // Metto in join la tabella DB_FOGLIO con DB_STORICO_PARTICELLA
        // solo se l'utente ha indicato la Z.V.N. o la ZVF o la PSN di
        // riferimento nei filtri di ricerca
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaE()) || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaF())
            || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaPSN())
            || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaEFasciaFluviale()))
        {
          queryBuf.append(" " +
              " AND    SP.COMUNE = F.COMUNE " +
              " AND    NVL(SP.SEZIONE,'-') = NVL(F.SEZIONE,'-') " +
              " AND    SP.FOGLIO = F.FOGLIO ");
          if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaE()))
          {
            queryBuf.append(" AND    F.ID_AREA_E = ? ");
          }
          if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaF()))
          {
            queryBuf.append(" AND    F.ID_AREA_F = ? ");
          }
          if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaPSN()))
          {
            queryBuf.append(" AND    F.ID_AREA_PSN = ? ");
          }
          if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaEFasciaFluviale()))
          {
            // in ZVN comprensivo di fascia fluviale
            if (filtriParticellareRicercaVO.getIdAreaEFasciaFluviale().longValue() == 1)
            {
              queryBuf.append(" AND    (SP.ID_FASCIA_FLUVIALE IS NOT NULL " 
                  + "            OR NVL(F.ID_AREA_E,0) = 2) ");
            }
            // fuori ZVN comprensivo di fascia fluviale
            else
            {
              queryBuf.append(" AND    SP.ID_FASCIA_FLUVIALE IS NULL " + 
                              " AND    NVL(F.ID_AREA_E,1) = 1 ");
            }
          }
        }
        else
        {
          queryBuf.append("" +
              " AND    SP.COMUNE = F.COMUNE(+) " +
              " AND    NVL( SP.SEZIONE,'-') = NVL( F.SEZIONE(+),'-') " +
              " AND    SP.FOGLIO = F.FOGLIO(+) ");
        }
        
        //Combo documenti
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()) 
            || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento())
            || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
        {
          queryBuf.append(" AND CP.ID_CONDUZIONE_PARTICELLA = DOCC.ID_CONDUZIONE_PARTICELLA " +
                   " AND DOCC.ID_DOCUMENTO = DOC.ID_DOCUMENTO " +
                   " AND DOC.EXT_ID_DOCUMENTO = TDOC.ID_DOCUMENTO " +
                   " AND TDOC.ID_DOCUMENTO = DOCCAT.ID_DOCUMENTO " +
                   " AND DOCCAT.ID_CATEGORIA_DOCUMENTO = ? " +
                   " AND DOC.ID_STATO_DOCUMENTO IS NULL " +
                   " AND NVL(DOC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > SYSDATE ");
          
          if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
          {
            queryBuf.append(" AND TDOC.ID_DOCUMENTO = ? ");
          }
          
          if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
          {
            queryBuf.append(" AND DOC.ID_DOCUMENTO = ? ");
          }
          
        }
        
        //Notifiche
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipologiaNotifica())
            || (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCategoriaNotifica())))
        {
          queryBuf.append(
                     " AND  EXISTS  (SELECT NE.ID_NOTIFICA_ENTITA " + 
                     "               FROM   DB_NOTIFICA_ENTITA NE, " +
                     "                      DB_NOTIFICA NO, " +
                     "                      DB_TIPO_ENTITA TE " +
                     "               WHERE  TE.CODICE_TIPO_ENTITA = 'P' " +
                     "               AND    TE.ID_TIPO_ENTITA = NE.ID_TIPO_ENTITA " +
                     "               AND    NE.IDENTIFICATIVO = SP.ID_PARTICELLA " +
                     "               AND    NE.ID_NOTIFICA = NO.ID_NOTIFICA " +
                     "               AND    NO.ID_AZIENDA = U.ID_AZIENDA ");
          if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipologiaNotifica()))
          {
            queryBuf.append(
                     "               AND    NO.ID_TIPOLOGIA_NOTIFICA = ? ");          
          }
          if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCategoriaNotifica()))
          {
            queryBuf.append(
                     "               AND    NO.ID_CATEGORIA_NOTIFICA = ? ");          
          }
          if(Validator.isNotEmpty(filtriParticellareRicercaVO.getFlagNotificheChiuse()))
          {
            queryBuf.append(
                     "AND    (NE.DATA_FINE_VALIDITA IS NULL " +
                     "        OR NE.DATA_FINE_VALIDITA = (SELECT MAX(NE1.DATA_FINE_VALIDITA) " +
                     "                                    FROM   DB_NOTIFICA_ENTITA NE1 " +
                     "                                    WHERE  NE1.IDENTIFICATIVO = NE.IDENTIFICATIVO " +
                     "                                    AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA )) )");
          }
          else
          {
            queryBuf.append(
                     " AND    NE.DATA_FINE_VALIDITA IS NULL )");
          }
          
        }
        
        
        // Se l'utente ha indicato il comune di riferimento
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIstatComune()))
        {
          queryBuf.append(" AND SP.COMUNE = ? ");
        }
        // Se l'utente ha indicato la sezione di riferimento
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSezione()))
        {
          queryBuf.append(" AND SP.SEZIONE = ? ");
        }
        // Se l'utente ha indicato il foglio di riferimento
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getFoglio()))
        {
          queryBuf.append(" AND SP.FOGLIO = ? ");
        }
        // Se l'utente ha indicato la particella di riferimento
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getParticella()))
        {
          queryBuf.append(" AND SP.PARTICELLA = ? ");
        }
        // Se l'utente ha indicato il subalterno di riferimento
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSubalterno()))
        {
          queryBuf.append(" AND SP.SUBALTERNO = ? ");
        }
        // Se l'utente ha indicato il tipo area A
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaA()))
        {
          queryBuf.append(" AND SP.ID_AREA_A = ? ");
        }
        // Se l'utente ha indicato il tipo area B
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaB()))
        {
          queryBuf.append(" AND SP.ID_AREA_B = ? ");
        }
        // Se l'utente ha indicato il tipo area C
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaC()))
        {
          queryBuf.append(" AND SP.ID_AREA_C = ? ");
        }
        // Se l'utente ha indicato il tipo area D
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaD()))
        {
          queryBuf.append(" AND SP.ID_AREA_D = ? ");
        }
        // Se l'utente ha indicato il tipo area M
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaM()))
        {
          queryBuf.append(" AND SP.ID_AREA_M = ? ");
        }
        // Se l'utente ha indicato il tipo fascia fluviale
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdFasciaFluviale()))
        {
          queryBuf.append(" AND SP.ID_FASCIA_FLUVIALE = ? ");
        }
        // Se l'utente ha indicato il tipo Araea G
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaG()))
        {
          queryBuf.append(" AND NVL(SP.ID_AREA_G,1) = ? ");
        }
        // Se l'utente ha indicato il tipo Araea H
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaH()))
        {
          queryBuf.append(" AND NVL(SP.ID_AREA_H,1) = ? ");
        }
        // Se l'utente ha indicato il tipo Zona Altimetrica
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdZonaAltimetrica()))
        {
          queryBuf.append(" AND SP.ID_ZONA_ALTIMETRICA = ? ");
        }
        // Se l'utente ha indicato il tipo Caso Particolare
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCasoParticolare()))
        {
          queryBuf.append(" AND SP.ID_CASO_PARTICOLARE = ? ");
        }
        // Se l'utente ha specificato un particolare titolo di possesso
        if (filtriParticellareRicercaVO.getIdTitoloPossesso() != null)
        {
          queryBuf.append(" AND CP.ID_TITOLO_POSSESSO = ? ");
        }
        else
        {
          // Se l'utente ha scelto l'opzione escludi asservimento
          if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckEscludiAsservimento())
              && filtriParticellareRicercaVO.getCheckEscludiAsservimento().equalsIgnoreCase(SolmrConstants.FLAG_S))
          {
            queryBuf.append(" AND CP.ID_TITOLO_POSSESSO <> " + SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO);
          }
          // Se l'utente ha scelto l'opzione escludi conferimento
          if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckEscludiConferimento())
              && filtriParticellareRicercaVO.getCheckEscludiConferimento().equalsIgnoreCase(SolmrConstants.FLAG_S))
          {
            queryBuf.append(" AND CP.ID_TITOLO_POSSESSO <> " + SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO);
          }
        }
        // Se l'utente ha specificato la tipologia di anomalia bloccante
        boolean isFirst = true;
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneBloccante()))
        {
          queryBuf.append(" AND (CP.ESITO_CONTROLLO = ? ");
          isFirst = false;
        }
        // Se l'utente ha specificato la tipologia di anomalia warning
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneWarning()))
        {
          if (!isFirst)
          {
            queryBuf.append(" OR ");
          }
          else
          {
            queryBuf.append(" AND (");
          }
          queryBuf.append(" CP.ESITO_CONTROLLO = ? ");
          isFirst = false;
        }
        // Se l'utente ha specificato la tipologia di anomalia OK
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneOk()))
        {
          if (!isFirst)
          {
            queryBuf.append(" OR ");
          }
          else
          {
            queryBuf.append(" AND (");
          }
          queryBuf.append(" CP.ESITO_CONTROLLO = ? ");
          isFirst = false;
        }
        if (!isFirst)
        {
          query += ")";
        }
        
        // Se l'utente ha specificato un determinato tipo di controllo
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo()))
        {
          queryBuf.append(" " +
              " AND DS.ID_AZIENDA = ? " +
              " AND DS.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
              " AND DS.ID_DICHIARAZIONE_CONSISTENZA IS NULL " +
              " AND DS.ID_CONTROLLO = ? ");
        }
        
        // Ricerco i dati della particella certificata solo se vengono
        // richieste le particelle solo asservite
        if (filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S))
        {
          queryBuf.append(" AND DICH_CONS.ID_AZIENDA <> ? "
              + " AND DICH_CONS.DATA_INSERIMENTO_DICHIARAZIONE = (SELECT MAX(DATA_INSERIMENTO_DICHIARAZIONE) FROM DB_DICHIARAZIONE_CONSISTENZA DC1 WHERE DICH_CONS.ID_AZIENDA = DC1.ID_AZIENDA) "
              + " AND DICH_CONS.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " + " AND CD.ID_TITOLO_POSSESSO = ? "
              + " AND CD.ID_PARTICELLA = SP.ID_PARTICELLA ");
        }
        // Ricerco i dati della particella certificata solo se vengono
        // richieste le particelle solo conferite
        if (filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S))
        {
          queryBuf.append(" AND DICH_CONS.ID_AZIENDA <> ? "
              + " AND DICH_CONS.DATA_INSERIMENTO_DICHIARAZIONE = (SELECT MAX(DATA_INSERIMENTO_DICHIARAZIONE) FROM DB_DICHIARAZIONE_CONSISTENZA DC1 WHERE DICH_CONS.ID_AZIENDA = DC1.ID_AZIENDA) "
              + " AND DICH_CONS.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " + " AND CD.ID_TITOLO_POSSESSO = ? "
              + " AND CD.ID_PARTICELLA = SP.ID_PARTICELLA ");
        }
        
        queryBuf.append(" " +
            "GROUP BY CP.ID_CONDUZIONE_PARTICELLA, " +
            "         CP.ESITO_CONTROLLO, " +
            "         CP.ID_PARTICELLA, " + 
            "         SP.COMUNE, " +
            "         C.DESCOM, " +
            "         P.SIGLA_PROVINCIA, " +
            "         SP.SEZIONE, " +
            "         SP.FOGLIO, " +
            "         SP.PARTICELLA, " +
            "         SP.SUBALTERNO, " +
            "         SP.SUP_CATASTALE, " +
            "         SP.SUPERFICIE_GRAFICA, " +
            "         CP.ID_TITOLO_POSSESSO, " +
            "         TTP.DESCRIZIONE, " +
            "         CP.SUPERFICIE_CONDOTTA, " +
            "         CP.PERCENTUALE_POSSESSO, " +
            "         CP.ID_UTE, " +
            "         U.COMUNE, " +
            "         C2.DESCOM, " +
            "         PC.SUP_USO_GRAFICA, " +
            "         -1, " +
            "         -1, " +
            "         PAS.SUPERFICIE, " +
            "         PAS.DESCRIZIONE, " +
            "         SP.ID_PARTICELLA " +
            "         HAVING DECODE(CP.ID_TITOLO_POSSESSO, 5 , CP.SUPERFICIE_CONDOTTA, TRUNC(LEAST(DECODE(SP.SUPERFICIE_GRAFICA,0,SP.SUP_CATASTALE,SP.SUPERFICIE_GRAFICA), " +
            "                      DECODE(SP.SUP_CATASTALE,0,SP.SUPERFICIE_GRAFICA,SP.SUP_CATASTALE)) * CP.PERCENTUALE_POSSESSO / 100,4) " +
            "                - TRUNC(SUM(NVL(UP.SUPERFICIE_UTILIZZATA,0)),4)) > 0 ");
      }
      
      queryBuf.append(" ORDER BY DESCOM,SIGLA_PROVINCIA,SEZIONE,FOGLIO,PARTICELLA,SUBALTERNO," +
          "ID_TITOLO_POSSESSO,COD_PRIMARIO,DESC_PRIMARIO");
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ConduzioneParticellaGaaDAO::searchParticelleStabGisExcelByParameters] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Prima parte della query: viene eseguita solo se l'utente non sceglie
      // di visualizzare solo le particella senza uso del suolo specificato
      int indice = 0;
      //Istanza riesame
      stmt.setLong(++indice, idAzienda.longValue());
      
      
      if (filtriParticellareRicercaVO.getIdUtilizzo().intValue() != 0)
      {
        //P26
        stmt.setLong(++indice, idAzienda.longValue());
        
        //Plsql supeleggibileRiproporzionata
        stmt.setLong(++indice, idAzienda.longValue());
        
        stmt.setLong(++indice, idAzienda.longValue());
        
        // ID_MACRO_USO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdMacroUso()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdMacroUso().longValue());
        }
        // ID_UTE
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdUte()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUte().longValue());
        }
        // ID_AREA_E(Z.V.N.)
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaE()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaE().longValue());
        }
        // ID_AREA_F(Z.V.F.)
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaF()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaF().longValue());
        }
        // ID_AREA_PSN(Localizzazione)
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaPSN()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaPSN().longValue());
        }
        // TIPO DOCUMENTO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTipoDocumento().longValue());
        }        
        // DOCUMENTO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdDocumento().longValue());
        }
        // PROTOCOLLO DOCUMENTO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdProtocolloDocumento().longValue());
        }
        // TIPOLOGIA NOTIFICA
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipologiaNotifica())) 
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTipologiaNotifica().longValue());
        }
        // CATEGORIA_NOTIFICA
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCategoriaNotifica())) 
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdCategoriaNotifica().longValue());
        }
        // ISTAT_COMUNE
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIstatComune()))
        {
          stmt.setString(++indice, filtriParticellareRicercaVO.getIstatComune());
        }
        // SEZIONE
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSezione()))
        {
          stmt.setString(++indice, filtriParticellareRicercaVO.getSezione().toUpperCase());
        }
        // FOGLIO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getFoglio()))
        {
          if (Validator.isNumericInteger(filtriParticellareRicercaVO.getFoglio()))
          {
            stmt.setString(++indice, filtriParticellareRicercaVO.getFoglio());
          }
          else
          {
            stmt.setString(++indice, null);
          }
        }
        // PARTICELLA
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getParticella()))
        {
          if (Validator.isNumericInteger(filtriParticellareRicercaVO.getParticella()))
          {
            stmt.setString(++indice, filtriParticellareRicercaVO.getParticella());
          }
          else
          {
            stmt.setString(++indice, null);
          }
        }
        // SUBALTERNO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSubalterno()))
        {
          stmt.setString(++indice, filtriParticellareRicercaVO.getSubalterno().toUpperCase());
        }
        // ID_AREA_A
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaA()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaA().longValue());
        }
        // ID_AREA_B
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaB()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaB().longValue());
        }
        // ID_AREA_C
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaC()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaC().longValue());
        }
        // ID_AREA_D
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaD()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaD().longValue());
        }
        // ID_AREA_M
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaM()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaM().longValue());
        }
        // ID_FASCIA_FLUVIALE
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdFasciaFluviale()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdFasciaFluviale().longValue());
        }
        // ID_AREA_G
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaG()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaG().longValue());
        }
        // ID_AREA_H
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaH()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaH().longValue());
        }
        // ID_ZONA_ALTIMETRICA
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdZonaAltimetrica()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdZonaAltimetrica().longValue());
        }
        // ID_CASO_PARTICOLARE
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCasoParticolare()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdCasoParticolare().longValue());
        }
        if (filtriParticellareRicercaVO.getIdUtilizzo().longValue() > 0)
        {
          // Se non è stato indicato se l'uso è primario o secondario
          if (!Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario())
              && !Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario()))
          {
            // Se è stato indicato un particolare uso del suolo, seleziono di
            // default l'opzione
            // uso primario
            stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
          }
          else
          {
            // Uso primario
            if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario()))
            {
              stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
            }
            // Uso secondario
            if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario()))
            {
              stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
            }
          }
        }
        // ID_TIPO_TITOLO_POSSESSO
        if (filtriParticellareRicercaVO.getIdTitoloPossesso() != null)
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTitoloPossesso().longValue());
        }
        // SEGNALAZIONI
        // Se l'utente ha specificato la tipologia di anomalia bloccante
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneBloccante()))
        {
          stmt.setString(++indice, filtriParticellareRicercaVO.getTipoSegnalazioneBloccante());
        }
        // Se l'utente ha specificato la tipologia di anomalia warning
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneWarning()))
        {
          stmt.setString(++indice, filtriParticellareRicercaVO.getTipoSegnalazioneWarning());
        }
        // Se l'utente ha specificato la tipologia di anomalia OK
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneOk()))
        {
          stmt.setString(++indice, filtriParticellareRicercaVO.getTipoSegnalazioneOk());
        }
      }
      // Se l'utente ha specificato un determinato controllo
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo()))
      {
        stmt.setLong(++indice, idAzienda.longValue());
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdControllo().longValue());
      }
      // Ricerco i dati della particella certificata solo se vengono
      // richieste le particelle solo asservite
      if (filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S))
      {
        stmt.setLong(++indice, idAzienda.longValue());
        stmt.setLong(++indice, SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO.longValue());
      }
      // Ricerco i dati delle particelle solo conferite
      if (filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S))
      {
        stmt.setLong(++indice, idAzienda.longValue());
        stmt.setLong(++indice, SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO.longValue());
      }
      // Settaggio parametri seconda query
      // La seconda parte della query viene eseguita solo se l'utente non ha
      // scelto un particolare uso del suolo da visualizzare
      if ((filtriParticellareRicercaVO.getIdUtilizzo().intValue() <= 0) && (filtriParticellareRicercaVO.getIdMacroUso() == null))
      {
        // ID_AZIENDA
        stmt.setLong(++indice, idAzienda.longValue());
        // ID_UTE
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdUte()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUte().longValue());
        }
        // ID_AREA_E(Z.V.N.)
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaE()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaE().longValue());
        }
        // ID_AREA_F(Z.V.F.)
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaF()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaF().longValue());
        }
        // ID_AREA_PSN(Localizzazione)
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaPSN()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaPSN().longValue());
        }
        // TIPO DOCUMENTO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTipoDocumento().longValue());
        }
        
        // DOCUMENTO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdDocumento().longValue());
        }
        // PROTOCOLLO DOCUMENTO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdProtocolloDocumento().longValue());
        }
        // TIPOLOGIA NOTIFICA
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipologiaNotifica())) 
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTipologiaNotifica().longValue());
         }
        // CATEGORIA_NOTIFICA
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCategoriaNotifica())) 
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdCategoriaNotifica().longValue());
        }
        // ISTAT_COMUNE
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIstatComune()))
        {
          stmt.setString(++indice, filtriParticellareRicercaVO.getIstatComune());
        }
        // SEZIONE
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSezione()))
        {
          stmt.setString(++indice, filtriParticellareRicercaVO.getSezione().toUpperCase());
        }
        // FOGLIO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getFoglio()))
        {
          if (Validator.isNumericInteger(filtriParticellareRicercaVO.getFoglio()))
          {
            stmt.setString(++indice, filtriParticellareRicercaVO.getFoglio());
          }
          else
          {
            stmt.setString(++indice, null);
          }
        }
        // PARTICELLA
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getParticella()))
        {
          if (Validator.isNumericInteger(filtriParticellareRicercaVO.getParticella()))
          {
            stmt.setString(++indice, filtriParticellareRicercaVO.getParticella());
          }
          else
          {
            stmt.setString(++indice, null);
          }
        }
        // SUBALTERNO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSubalterno()))
        {
          stmt.setString(++indice, filtriParticellareRicercaVO.getSubalterno().toUpperCase());
        }
        // ID_AREA_A
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaA()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaA().longValue());
        }
        // ID_AREA_B
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaB()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaB().longValue());
        }
        // ID_AREA_C
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaC()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaC().longValue());
        }
        // ID_AREA_D
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaD()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaD().longValue());
        }
        // ID_AREA_M
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaM()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaM().longValue());
        }
        // ID_FASCIA_FLUVIALE
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdFasciaFluviale()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdFasciaFluviale().longValue());
        }
        // ID_AREA_G
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaG()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaG().longValue());
        }
        // ID_AREA_H
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaH()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaH().longValue());
        }
        // ID_ZONA_ALTIMETRICA
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdZonaAltimetrica()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdZonaAltimetrica().longValue());
        }
        // ID_CASO_PARTICOLARE
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCasoParticolare()))
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdCasoParticolare().longValue());
        }
        // ID_TIPO_TITOLO_POSSESSO
        if (filtriParticellareRicercaVO.getIdTitoloPossesso() != null)
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTitoloPossesso().longValue());
        }
        // Se l'utente ha specificato la tipologia di anomalia bloccante
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneBloccante()))
        {
          stmt.setString(++indice, filtriParticellareRicercaVO.getTipoSegnalazioneBloccante());
        }
        // Se l'utente ha specificato la tipologia di anomalia warning
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneWarning()))
        {
          stmt.setString(++indice, filtriParticellareRicercaVO.getTipoSegnalazioneWarning());
        }
        // Se l'utente ha specificato la tipologia di anomalia OK
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneOk()))
        {
          stmt.setString(++indice, filtriParticellareRicercaVO.getTipoSegnalazioneOk());
        }
      }
      // Se l'utente ha specificato un determinato controllo
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo()))
      {
        stmt.setLong(++indice, idAzienda.longValue());
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdControllo().longValue());
      }
      // Ricerco i dati della particella certificata solo se vengono
      // richieste le particelle solo asservite
      if (filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S))
      {
        stmt.setLong(++indice, idAzienda.longValue());
        stmt.setLong(++indice, SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO.longValue());
      }
      // Ricerco i dati delle particelle solo conferite
      if (filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S))
      {
        stmt.setLong(++indice, idAzienda.longValue());
        stmt.setLong(++indice, SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO.longValue());
      }
      
      ResultSet rs = stmt.executeQuery();     
      
      while (rs.next())
      {     
        AnagParticellaExcelVO anagParticellaExcelVO = new AnagParticellaExcelVO();
        anagParticellaExcelVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
        anagParticellaExcelVO.setLabelUte(rs.getString("COM_UTE") + " - " + rs.getString("DESC_COM_UTE"));
        anagParticellaExcelVO.setIstatComuneParticella(rs.getString("COMUNE"));
        anagParticellaExcelVO.setDescrizioneComuneParticella(rs.getString("DESCOM") +" ("+rs.getString("SIGLA_PROVINCIA")+")");
        anagParticellaExcelVO.setSezione(rs.getString("SEZIONE"));
        anagParticellaExcelVO.setFoglio(rs.getString("FOGLIO"));
        anagParticellaExcelVO.setParticella(rs.getString("PARTICELLA"));
        anagParticellaExcelVO.setSubalterno(rs.getString("SUBALTERNO"));
        anagParticellaExcelVO.setSuperficieCatastale(StringUtils.parseSuperficieField(rs.getString("SUP_CATASTALE")));
        //anagParticellaExcelVO.setSuperficieGrafica(StringUtils.parseSuperficieField(rs.getString("SUPERFICIE_GRAFICA")));
        
        anagParticellaExcelVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
        anagParticellaExcelVO.setSuperficieCondotta(StringUtils.parseSuperficieField(rs.getString("SUPERFICIE_CONDOTTA")));
        anagParticellaExcelVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
        if (Validator.isNotEmpty(rs.getString("ID_UTILIZZO_PARTICELLA")))
        {
          if (Long.decode(rs.getString("ID_UTILIZZO_PARTICELLA")).longValue() > -1)
          {
            anagParticellaExcelVO.setIdUtilizzoParticella(new Long(rs.getLong("ID_UTILIZZO_PARTICELLA")));
            String usoPrimario = "";
            if (Validator.isNotEmpty(rs.getString("COD_PRIMARIO")))
            {
              usoPrimario += "[" + rs.getString("COD_PRIMARIO") + "] ";
            }
            if (Validator.isNotEmpty(rs.getString("DESC_PRIMARIO")))
            {
              usoPrimario += rs.getString("DESC_PRIMARIO");
            }
            anagParticellaExcelVO.setUsoPrimario(usoPrimario);
            String varieta = "";
            if (Validator.isNotEmpty(rs.getString("COD_VARIETA")))
            {
              varieta += "[" + rs.getString("COD_VARIETA") + "] ";
            }
            if (Validator.isNotEmpty(rs.getString("DESC_VARIETA")))
            {
              varieta += rs.getString("DESC_VARIETA");
            }
            anagParticellaExcelVO.setVarieta(varieta);
          }
          anagParticellaExcelVO.setSuperficieUtilizzata(StringUtils.parseSuperficieField(rs.getString("SUPERFICIE_UTILIZZATA")));
        }
        if (Validator.isNotEmpty(rs.getString("SUP_USO_GRAFICA")))
        {
          anagParticellaExcelVO.setSupUsoGrafico(StringUtils.parseSuperficieField(rs.getString("SUP_USO_GRAFICA")));
        }
        else
        {
          anagParticellaExcelVO.setSupUsoGrafico(SolmrConstants.DEFAULT_SUPERFICIE);
        }        
        
        if("S".equalsIgnoreCase(rs.getString("ALLINEA_A_ELEGGIBILE")))
        {
          anagParticellaExcelVO.setSupEleggibileRiproporzionata(rs.getString("SUP_ELEGGIBILE_RIP"));
        }
        
        String p26 = rs.getString("P26");
        String note = "";
        if("0".equalsIgnoreCase(anagParticellaExcelVO.getSupEleggibileRiproporzionata()))
        {
          if (Validator.isNotEmpty(rs.getString("ID_UTILIZZO_PARTICELLA")))
          {
            anagParticellaExcelVO.setSupAssegnataAllineamento(
                StringUtils.parseSuperficieField(rs.getString("SUPERFICIE_UTILIZZATA")));
            note = "Non allineabile perchè con nuova superficie 0.";
          }
        }
        else if("0".equalsIgnoreCase(p26))
        {
          if (Validator.isNotEmpty(rs.getString("ID_UTILIZZO_PARTICELLA")))
          {
            anagParticellaExcelVO.setSupAssegnataAllineamento(
                StringUtils.parseSuperficieField(rs.getString("SUPERFICIE_UTILIZZATA")));
            note = "Non allineabile, particella senza anomalia P26.";
          }          
        }       
        else if("N".equalsIgnoreCase(rs.getString("ALLINEA_A_ELEGGIBILE")))
        {
          if (Validator.isNotEmpty(rs.getString("ID_UTILIZZO_PARTICELLA")))
          {
            anagParticellaExcelVO.setSupAssegnataAllineamento(
                StringUtils.parseSuperficieField(rs.getString("SUPERFICIE_UTILIZZATA")));
            note = "Utilizzo non contemplato nell'allineamento GIS.";
          }   
        }
        else if(Validator.isNotEmpty(rs.getString("SUP_REG_PASCOLI")))
        {          
          anagParticellaExcelVO.setSupAssegnataAllineamento(
              StringUtils.parseSuperficieField(rs.getString("SUPERFICIE_UTILIZZATA")));
          note = "Non allineabile, particella presente nel registro storico pascoli ";
        }
       
        
        anagParticellaExcelVO.setNote(note);
        
        elencoParticelle.add(anagParticellaExcelVO);
      }
      
      rs.close();
      stmt.close();
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("elencoParticelle", elencoParticelle) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
          new Parametro("filtriParticellareRicercaVO", filtriParticellareRicercaVO)
      };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ConduzioneParticellaGaaDAO::searchParticelleStabGisExcelByParameters] ", t,
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
          "[ConduzioneParticellaGaaDAO::searchParticelleStabGisExcelByParameters] END.");
    }
    
    return elencoParticelle;
  } 
  
  
  public Vector<AnagParticellaExcelVO> searchParticelleAvvicExcelByParameters(FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<AnagParticellaExcelVO> elencoParticelle = new Vector<AnagParticellaExcelVO>();
    StringBuffer queryBuf = null;
    String query = null;    
    try
    {
      SolmrLogger.debug(this,
          "[ConduzioneParticellaGaaDAO::searchParticelleAvvicExcelByParameters] BEGIN.");
      
      queryBuf = new StringBuffer();  
      // Prima parte della query: viene eseguita solo se l'utente non sceglie
      // di visualizzare solo le particella senza uso del suolo specificato
      queryBuf.append(
        " SELECT SP.COMUNE, " +
        "        C.DESCOM, " +
        "        P.SIGLA_PROVINCIA, " +
        "        SP.SEZIONE, " +
        "        SP.FOGLIO, " +
        "        SP.PARTICELLA, " +
        "        SP.SUBALTERNO, " +
        "        SP.SUP_CATASTALE, " +
        "        U.COMUNE AS COM_UTE, " +
        "        C2.DESCOM AS DESC_COM_UTE," +
        "        TZA.DESCRIZIONE AS DESC_ZONA_ALT, " +
        "        SP.ID_PARTICELLA " +
        " FROM   DB_UTE U, " +
        "        DB_STORICO_PARTICELLA SP, " +
        "        DB_CONDUZIONE_PARTICELLA CP, " +
        "        COMUNE C, " +
        "        DB_UTILIZZO_PARTICELLA UP, " +
        "        DB_TIPO_UTILIZZO TU, " +
        "        DB_TIPO_VARIETA TV, " +
        "        PROVINCIA P, " +
        "        COMUNE C2, " +
        "        DB_FOGLIO F, " +
        "        DB_TIPO_MACRO_USO TMU, " +
        "        DB_TIPO_MACRO_USO_VARIETA TMUV," +
        "        DB_TIPO_ZONA_ALTIMETRICA TZA ");
      
      if (filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S)
          || filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S))
      {
        queryBuf.append("" +
            "    ,DB_DICHIARAZIONE_CONSISTENZA DICH_CONS, " +
            "     DB_CONDUZIONE_DICHIARATA CD ");
      }
      
      // Metto in join DB_DICHIARAZIONE_SEGNALAZIONE solo se l'utente
      // ha specificato un determinato controllo effettuato sulle particelle
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo()))
      {
        queryBuf.append("   ,DB_DICHIARAZIONE_SEGNALAZIONE DS ");
      }
      
      //Tabelle per la ricerca sui documenti
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()) 
          || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
      {
        queryBuf.append("" +
            "   ,DB_DOCUMENTO_CONDUZIONE DOCC, " +
            "    DB_DOCUMENTO DOC, " +
            "    DB_TIPO_DOCUMENTO TDOC," +
            "    DB_DOCUMENTO_CATEGORIA DOCCAT ");
      }
      
      queryBuf.append("" +
        " WHERE  U.ID_AZIENDA = ? " +
        //" AND    U.DATA_FINE_ATTIVITA IS NULL " +
        " AND    U.ID_UTE = CP.ID_UTE " +
        " AND    U.COMUNE = C2.ISTAT_COMUNE ");
      // Estraggo le conduzione attive solo se il piano di riferimento è
      // "in lavorazione(solo conduzioni attive)"
      if (filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() == -1)
      {
        queryBuf.append( " AND    U.DATA_FINE_ATTIVITA IS NULL " +
            "   AND CP.DATA_FINE_CONDUZIONE IS NULL ");
      }
      queryBuf.append(
        " AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
        " AND    SP.DATA_FINE_VALIDITA IS NULL " +
        " AND    SP.COMUNE = C.ISTAT_COMUNE " +
        " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) " +
        " AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA (+) " +
        " AND    UP.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
        " AND    UP.ID_VARIETA = TV.ID_VARIETA(+) " +
        " AND    UP.ID_VARIETA = TMUV.ID_VARIETA (+)" +
        " AND    NVL(UP.ID_TIPO_DETTAGLIO_USO,-1) = NVL(TMUV.ID_TIPO_DETTAGLIO_USO (+),-1) " +
        " AND    TMUV.DATA_FINE_VALIDITA (+) IS NULL " +
        " AND    TMUV.ID_MACRO_USO = TMU.ID_MACRO_USO (+) " +
        " AND    TMU.DATA_FINE_VALIDITA (+) IS NULL " +
        " AND    SP.ID_ZONA_ALTIMETRICA = TZA.ID_ZONA_ALTIMETRICA (+) ");
      
      
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdMacroUso()))
      {
        queryBuf.append("" +
            " AND TMU.ID_MACRO_USO = ? ");
      }
      
      // Se l'utente ha indicato l'ute di riferimento
      if (filtriParticellareRicercaVO.getIdUte() != null)
      {
        queryBuf.append(" AND U.ID_UTE = ? ");
      }
      
      // Metto in join la tabella DB_FOGLIO con DB_STORICO_PARTICELLA
      // solo se l'utente ha indicato la Z.V.N. la ZVF o la PSN di riferimento
      // nei filtri di ricerca
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaE()) || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaF())
          || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaPSN())
          || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaEFasciaFluviale()))
      {
        queryBuf.append(
            " AND    SP.COMUNE = F.COMUNE " +
            " AND    NVL(SP.SEZIONE,'-') = NVL(F.SEZIONE,'-') " +
            " AND    SP.FOGLIO = F.FOGLIO ");
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaE()))
        {
          queryBuf.append(" AND    F.ID_AREA_E = ? ");
        }
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaF()))
        {
          queryBuf.append(" AND    F.ID_AREA_F = ? ");
        }
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaPSN()))
        {
          queryBuf.append(" AND    F.ID_AREA_PSN = ? ");
        }
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaEFasciaFluviale()))
        {
          // in ZVN comprensivo di fascia fluviale
          if (filtriParticellareRicercaVO.getIdAreaEFasciaFluviale().longValue() == 1)
          {
            queryBuf.append("" +
                "AND    (SP.ID_FASCIA_FLUVIALE IS NOT NULL " +
                "            OR NVL(F.ID_AREA_E,0) = 2) ");
          }
          // fuori ZVN comprensivo di fascia fluviale
          else
          {
            queryBuf.append(
                " AND    SP.ID_FASCIA_FLUVIALE IS NULL " + 
                "AND    NVL(F.ID_AREA_E,1) = 1 ");
          }
        }
      }
      else
      {
        queryBuf.append("" +
            " AND    SP.COMUNE = F.COMUNE(+) " +
            " AND    NVL( SP.SEZIONE,'-') = NVL( F.SEZIONE(+),'-') " +
            " AND    SP.FOGLIO = F.FOGLIO(+) ");
      }
      
      //Combo documenti
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()) 
          || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento())
          || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
      {
        queryBuf.append(" AND CP.ID_CONDUZIONE_PARTICELLA = DOCC.ID_CONDUZIONE_PARTICELLA " +
                 " AND DOCC.ID_DOCUMENTO = DOC.ID_DOCUMENTO " +
                 " AND DOC.EXT_ID_DOCUMENTO = TDOC.ID_DOCUMENTO " +
                 " AND TDOC.ID_DOCUMENTO = DOCCAT.ID_DOCUMENTO " +
                 " AND DOCCAT.ID_CATEGORIA_DOCUMENTO = ? " +
                 " AND DOC.ID_STATO_DOCUMENTO IS NULL " +
                 " AND NVL(DOC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > SYSDATE ");
        
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
        {
          queryBuf.append(" AND TDOC.ID_DOCUMENTO = ? ");
        }
        
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
        {
          queryBuf.append(" AND DOC.ID_DOCUMENTO = ? ");
        }
        
      }
      
      //Notifiche
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipologiaNotifica())
          || (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCategoriaNotifica())))
      {
        queryBuf.append(
                " AND  EXISTS  (SELECT NE.ID_NOTIFICA_ENTITA " + 
                "               FROM   DB_NOTIFICA_ENTITA NE, " +
                "                      DB_NOTIFICA NO, " +
                "                      DB_TIPO_ENTITA TE " +
                "               WHERE  TE.CODICE_TIPO_ENTITA = 'P' " +
                "               AND    TE.ID_TIPO_ENTITA = NE.ID_TIPO_ENTITA " +
                "               AND    NE.IDENTIFICATIVO = SP.ID_PARTICELLA " +
                "               AND    NE.ID_NOTIFICA = NO.ID_NOTIFICA " +
                "               AND    NO.ID_AZIENDA = U.ID_AZIENDA ");
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipologiaNotifica()))
        {
          queryBuf.append(
                "               AND    NO.ID_TIPOLOGIA_NOTIFICA = ? ");          
        }
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCategoriaNotifica()))
        {
          queryBuf.append(
                "               AND    NO.ID_CATEGORIA_NOTIFICA = ? ");          
        }
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getFlagNotificheChiuse()))
        {
          queryBuf.append(
                "AND    (NE.DATA_FINE_VALIDITA IS NULL " +
                "        OR NE.DATA_FINE_VALIDITA = (SELECT MAX(NE1.DATA_FINE_VALIDITA) " +
                "                                    FROM   DB_NOTIFICA_ENTITA NE1 " +
                "                                    WHERE  NE1.IDENTIFICATIVO = NE.IDENTIFICATIVO " +
                "                                    AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA )) )");
        }
        else
        {
          queryBuf.append(
                " AND    NE.DATA_FINE_VALIDITA IS NULL )");
        }
        
      }
      
      // Se l'utente ha indicato il comune di riferimento
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIstatComune()))
      {
        queryBuf.append(" AND SP.COMUNE = ? ");
      }
      // Se l'utente ha indicato la sezione di riferimento
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSezione()))
      {
        queryBuf.append(" AND SP.SEZIONE = ? ");
      }
      // Se l'utente ha indicato il foglio di riferimento
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getFoglio()))
      {
        queryBuf.append(" AND SP.FOGLIO = ? ");
      }
      // Se l'utente ha indicato la particella di riferimento
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getParticella()))
      {
        queryBuf.append(" AND SP.PARTICELLA = ? ");
      }
      // Se l'utente ha indicato il subalterno di riferimento
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSubalterno()))
      {
        queryBuf.append(" AND SP.SUBALTERNO = ? ");
      }
      // Se l'utente ha indicato il tipo area A
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaA()))
      {
        queryBuf.append(" AND SP.ID_AREA_A = ? ");
      }
      // Se l'utente ha indicato il tipo area B
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaB()))
      {
        queryBuf.append(" AND SP.ID_AREA_B = ? ");
      }
      // Se l'utente ha indicato il tipo area C
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaC()))
      {
        queryBuf.append(" AND SP.ID_AREA_C = ? ");
      }
      // Se l'utente ha indicato il tipo area D
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaD()))
      {
        queryBuf.append(" AND SP.ID_AREA_D = ? ");
      }
      // Se l'utente ha indicato il tipo area D
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaM()))
      {
        queryBuf.append(" AND SP.ID_AREA_M = ? ");
      }
      // Se l'utente ha indicato il tipo fascia fluviale
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdFasciaFluviale()))
      {
        queryBuf.append(" AND SP.ID_FASCIA_FLUVIALE = ? ");
      }
      // Se l'utente ha indicato il tipo Araea G
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaG()))
      {
        queryBuf.append(" AND NVL(SP.ID_AREA_G,1) = ? ");
      }
      // Se l'utente ha indicato il tipo Araea H
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaH()))
      {
        queryBuf.append(" AND NVL(SP.ID_AREA_H,1) = ? ");
      }
      // Se l'utente ha indicato il tipo Zona Altimetrica
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdZonaAltimetrica()))
      {
        queryBuf.append(" AND SP.ID_ZONA_ALTIMETRICA = ? ");
      }
      // Se l'utente ha indicato il tipo Caso Particolare
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCasoParticolare()))
      {
        queryBuf.append(" AND SP.ID_CASO_PARTICOLARE = ? ");
      }
      // Se l'utente invece ha specificato un particolare uso del suolo
      if (filtriParticellareRicercaVO.getIdUtilizzo().longValue() > 0)
      {
        // Se non è stato indicato nei filtri di ricerca se l'utilizzo
        // selezionato
        // è primario o secondario, viene usata come condizione di default
        // quella
        // dell'utilizzo primario
        if (!Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario())
            && !Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario()))
        {
          queryBuf.append(" AND UP.ID_UTILIZZO = ? ");
        }
        else
        {
          // Uso Primario
          if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario()))
          {
            queryBuf.append(" AND UP.ID_UTILIZZO = ? ");
          }
          // Uso Secondario
          if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario()))
          {
            queryBuf.append(" AND UP.ID_UTILIZZO_SECONDARIO = ? ");
          }
        }
        
      }
      // Se l'utente ha specificato un particolare titolo di possesso
      if (filtriParticellareRicercaVO.getIdTitoloPossesso() != null)
      {
        queryBuf.append(" AND CP.ID_TITOLO_POSSESSO = ? ");
      }
      else
      {
        // Se l'utente ha scelto l'opzione escludi asservimento
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckEscludiAsservimento())
            && filtriParticellareRicercaVO.getCheckEscludiAsservimento().equalsIgnoreCase(SolmrConstants.FLAG_S))
        {
          queryBuf.append(" AND CP.ID_TITOLO_POSSESSO <> " + SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO);
        }
        // Se l'utente ha scelto l'opzione escludi conferimento
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckEscludiConferimento())
            && filtriParticellareRicercaVO.getCheckEscludiConferimento().equalsIgnoreCase(SolmrConstants.FLAG_S))
        {
          queryBuf.append(" AND CP.ID_TITOLO_POSSESSO <> " + SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO);
        }
      }
      // Se l'utente ha specificato la tipologia di anomalia bloccante
      boolean isFirst = true;
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneBloccante()))
      {
        queryBuf.append(" AND (CP.ESITO_CONTROLLO = ? ");
        isFirst = false;
      }
      // Se l'utente ha specificato la tipologia di anomalia warning
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneWarning()))
      {
        if (!isFirst)
        {
          queryBuf.append(" OR ");
        }
        else
        {
          queryBuf.append(" AND (");
        }
        queryBuf.append(" CP.ESITO_CONTROLLO = ? ");
        isFirst = false;
      }
      // Se l'utente ha specificato la tipologia di anomalia OK
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneOk()))
      {
        if (!isFirst)
        {
          queryBuf.append(" OR ");
        }
        else
        {
          queryBuf.append(" AND (");
        }
        queryBuf.append(" CP.ESITO_CONTROLLO = ? ");
        isFirst = false;
      }
      if (!isFirst)
      {
        queryBuf.append(")");
      }
      
      // Se l'utente ha specificato un determinato tipo di controllo
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo()))
      {
        queryBuf.append(" " +
            "AND DS.ID_AZIENDA = ? " +
            " AND DS.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
            " AND DS.ID_DICHIARAZIONE_CONSISTENZA IS NULL " +
            " AND DS.ID_CONTROLLO = ? ");
      }
      
      // Ricerco i dati delle particelle solo asservite
      if (filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S))
      {
        queryBuf.append("" +
            " AND DICH_CONS.ID_AZIENDA <> ? " +
            " AND DICH_CONS.DATA_INSERIMENTO_DICHIARAZIONE = " +
            "     (SELECT MAX(DATA_INSERIMENTO_DICHIARAZIONE) " +
            "      FROM   DB_DICHIARAZIONE_CONSISTENZA DC1 " +
            "      WHERE  DICH_CONS.ID_AZIENDA = DC1.ID_AZIENDA" + "     ) " +
            " AND DICH_CONS.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
            " AND CD.ID_TITOLO_POSSESSO = ? " +
            " AND CD.ID_PARTICELLA = SP.ID_PARTICELLA ");
      }
      // Ricerco i dati delle particelle solo conferite
      if (filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S))
      {
        queryBuf.append(" AND DICH_CONS.ID_AZIENDA <> ? "
            + " AND DICH_CONS.DATA_INSERIMENTO_DICHIARAZIONE = (SELECT MAX(DATA_INSERIMENTO_DICHIARAZIONE) FROM DB_DICHIARAZIONE_CONSISTENZA DC1 WHERE DICH_CONS.ID_AZIENDA = DC1.ID_AZIENDA) "
            + " AND DICH_CONS.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " + " AND CD.ID_TITOLO_POSSESSO = ? "
            + " AND CD.ID_PARTICELLA = SP.ID_PARTICELLA ");
      }
      
      
      queryBuf.append(" " +
          "GROUP BY SP.COMUNE, " +
          "         C.DESCOM, " +
          "         P.SIGLA_PROVINCIA, " +
          "         SP.SEZIONE, " +
          "         SP.FOGLIO, " +
          "         SP.PARTICELLA, " +
          "         SP.SUBALTERNO, " +
          "         SP.SUP_CATASTALE, " +
          "         U.COMUNE, " +
          "         C2.DESCOM, " +
          "         TZA.DESCRIZIONE, " +
          "         SP.ID_PARTICELLA ");
    
    
      queryBuf.append(" ORDER BY DESCOM,SIGLA_PROVINCIA,SEZIONE,FOGLIO,PARTICELLA,SUBALTERNO ");
    
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ConduzioneParticellaGaaDAO::searchParticelleAvvicExcelByParameters] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Prima parte della query: viene eseguita solo se l'utente non sceglie
      // di visualizzare solo le particella senza uso del suolo specificato
      int indice = 0;
    
    
      
      stmt.setLong(++indice, idAzienda.longValue());
      
      // ID_MACRO_USO
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdMacroUso()))
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdMacroUso().longValue());
      }
      // ID_UTE
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdUte()))
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUte().longValue());
      }
      // ID_AREA_E(Z.V.N.)
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaE()))
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaE().longValue());
      }
      // ID_AREA_F(Z.V.F.)
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaF()))
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaF().longValue());
      }
      // ID_AREA_PSN(Localizzazione)
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaPSN()))
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaPSN().longValue());
      }
      // TIPO DOCUMENTO
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()))
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTipoDocumento().longValue());
      }
      
      // DOCUMENTO
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdDocumento().longValue());
      }
      // PROTOCOLLO DOCUMENTO
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdProtocolloDocumento().longValue());
      }
      // TIPOLOGIA NOTIFICA
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipologiaNotifica())) 
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTipologiaNotifica().longValue());
      }
      // CATEGORIA_NOTIFICA
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCategoriaNotifica())) 
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdCategoriaNotifica().longValue());
      }
      // ISTAT_COMUNE
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIstatComune()))
      {
        stmt.setString(++indice, filtriParticellareRicercaVO.getIstatComune());
      }
      // SEZIONE
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSezione()))
      {
        stmt.setString(++indice, filtriParticellareRicercaVO.getSezione().toUpperCase());
      }
      // FOGLIO
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getFoglio()))
      {
        if (Validator.isNumericInteger(filtriParticellareRicercaVO.getFoglio()))
        {
          stmt.setString(++indice, filtriParticellareRicercaVO.getFoglio());
        }
        else
        {
          stmt.setString(++indice, null);
        }
      }
      // PARTICELLA
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getParticella()))
      {
        if (Validator.isNumericInteger(filtriParticellareRicercaVO.getParticella()))
        {
          stmt.setString(++indice, filtriParticellareRicercaVO.getParticella());
        }
        else
        {
          stmt.setString(++indice, null);
        }
      }
      // SUBALTERNO
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSubalterno()))
      {
        stmt.setString(++indice, filtriParticellareRicercaVO.getSubalterno().toUpperCase());
      }
      // ID_AREA_A
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaA()))
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaA().longValue());
      }
      // ID_AREA_B
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaB()))
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaB().longValue());
      }
      // ID_AREA_C
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaC()))
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaC().longValue());
      }
      // ID_AREA_D
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaD()))
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaD().longValue());
      }
      // ID_AREA_M
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaM()))
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaM().longValue());
      }
      // ID_FASCIA_FLUVIALE
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdFasciaFluviale()))
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdFasciaFluviale().longValue());
      }
      // ID_AREA_G
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaG()))
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaG().longValue());
      }
      // ID_AREA_H
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaH()))
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaH().longValue());
      }
      // ID_ZONA_ALTIMETRICA
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdZonaAltimetrica()))
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdZonaAltimetrica().longValue());
      }
      // ID_CASO_PARTICOLARE
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCasoParticolare()))
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdCasoParticolare().longValue());
      }
      if (filtriParticellareRicercaVO.getIdUtilizzo().longValue() > 0)
      {
        // Se non è stato indicato se l'uso è primario o secondario
        if (!Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario())
            && !Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario()))
        {
          // Se è stato indicato un particolare uso del suolo, seleziono di
          // default l'opzione
          // uso primario
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
        }
        else
        {
          // Uso primario
          if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario()))
          {
            stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
          }
          // Uso secondario
          if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario()))
          {
            stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
          }
        }
      }
      // ID_TIPO_TITOLO_POSSESSO
      if (filtriParticellareRicercaVO.getIdTitoloPossesso() != null)
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTitoloPossesso().longValue());
      }
      // SEGNALAZIONI
      // Se l'utente ha specificato la tipologia di anomalia bloccante
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneBloccante()))
      {
        stmt.setString(++indice, filtriParticellareRicercaVO.getTipoSegnalazioneBloccante());
      }
      // Se l'utente ha specificato la tipologia di anomalia warning
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneWarning()))
      {
        stmt.setString(++indice, filtriParticellareRicercaVO.getTipoSegnalazioneWarning());
      }
      // Se l'utente ha specificato la tipologia di anomalia OK
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneOk()))
      {
        stmt.setString(++indice, filtriParticellareRicercaVO.getTipoSegnalazioneOk());
      }
    
      
      ResultSet rs = stmt.executeQuery();     
      
      while (rs.next())
      {     
        AnagParticellaExcelVO anagParticellaExcelVO = new AnagParticellaExcelVO();
        anagParticellaExcelVO.setLabelUte(rs.getString("COM_UTE") + " - " + rs.getString("DESC_COM_UTE"));
        anagParticellaExcelVO.setIstatComuneParticella(rs.getString("COMUNE"));
        anagParticellaExcelVO.setDescrizioneComuneParticella(rs.getString("DESCOM") +" ("+rs.getString("SIGLA_PROVINCIA")+")");
        anagParticellaExcelVO.setSezione(rs.getString("SEZIONE"));
        anagParticellaExcelVO.setFoglio(rs.getString("FOGLIO"));
        anagParticellaExcelVO.setParticella(rs.getString("PARTICELLA"));
        anagParticellaExcelVO.setSubalterno(rs.getString("SUBALTERNO"));
        anagParticellaExcelVO.setSuperficieCatastale(StringUtils.parseSuperficieField(rs.getString("SUP_CATASTALE")));
        anagParticellaExcelVO.setDescrizioneZonaAltimetrica(rs.getString("DESC_ZONA_ALT"));
        anagParticellaExcelVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
        
        elencoParticelle.add(anagParticellaExcelVO);
      }
      
      rs.close();
      stmt.close();
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("elencoParticelle", elencoParticelle) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
          new Parametro("filtriParticellareRicercaVO", filtriParticellareRicercaVO)
      };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ConduzioneParticellaGaaDAO::searchParticelleAvvicExcelByParameters] ", t,
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
          "[ConduzioneParticellaGaaDAO::searchParticelleAvvicExcelByParameters] END.");
    }
    
    return elencoParticelle;
  }
  
  
  
  
  public Vector<AnagParticellaExcelVO> searchParticelleDichiarateAvvicExcelByParameters(FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda) 
      throws DataAccessException 
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<AnagParticellaExcelVO> elencoParticelle = new Vector<AnagParticellaExcelVO>();
    StringBuffer queryBuf = null;
    String query = null;    
    try
    {
      SolmrLogger.debug(this,
          "[ConduzioneParticellaGaaDAO::searchParticelleDichiarateAvvicExcelByParameters] BEGIN.");
      
      queryBuf = new StringBuffer();      
      queryBuf.append(""+
        "SELECT SP.COMUNE, " +
        "       C.DESCOM, " +
        "       P.SIGLA_PROVINCIA, " +
        "       SP.SEZIONE, " +
        "       SP.FOGLIO, " +
        "       SP.PARTICELLA, " +
        "       SP.SUBALTERNO, " +
        "       SP.SUP_CATASTALE, " +
        "       TZA.DESCRIZIONE AS DESC_ZONA_ALT, " +
        "       U.COMUNE AS COM_UTE, " +
        "       C2.DESCOM AS DESC_COM_UTE, " +
        "       SP.ID_PARTICELLA " +     
        "FROM   DB_UTE U, " +
        "       DB_STORICO_PARTICELLA SP, " +    
        "       DB_CONDUZIONE_DICHIARATA CD, " +
        "       DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "       COMUNE C, " +
        "       DB_UTILIZZO_DICHIARATO UD, " +
        "       DB_TIPO_UTILIZZO TU, " +
        "       DB_TIPO_VARIETA TV, " +
        "       DB_TIPO_UTILIZZO TU2, " +  
        "       DB_TIPO_VARIETA TV2, " +
        "       PROVINCIA P, " +
        "       COMUNE C2, " +
        "       DB_FOGLIO F, " +
        "       DB_TIPO_ZONA_ALTIMETRICA TZA ");
      
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdMacroUso())) 
      {
        queryBuf.append(""+
            "       ,DB_TIPO_MACRO_USO TMU, " +
            "        DB_TIPO_MACRO_USO_VARIETA TMUV ");
      }
      
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo())) 
      {
        queryBuf.append(""+
            "       ,DB_DICHIARAZIONE_SEGNALAZIONE DS ");
      }
      
      //Se è attivo almeno uno dei due filtri tra solo asservite e solo conferite deve andare in join con la
      //DB_DICHIARAZIONE_CONSISTENZA
      if(filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S)
          || filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S))  
      {
        queryBuf.append(""+
            "      ,DB_DICHIARAZIONE_CONSISTENZA DICH_CONS, " +
            "       DB_CONDUZIONE_DICHIARATA CD2 ");
      }
      
      //Tabelle per la ricerca sui documenti
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()) 
          || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
      {
        queryBuf.append(""+
            "   ,DB_DOCUMENTO_CONDUZIONE DOCC, " +
            "    DB_DOCUMENTO DOC1, " +
            "    DB_TIPO_DOCUMENTO TDOC, " +
            "    DB_DOCUMENTO_CATEGORIA DOCCAT ");
      }
      
      queryBuf.append(""+
        " WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        " AND    U.ID_AZIENDA = ? " +
        " AND    TRUNC(U.DATA_INIZIO_ATTIVITA) <= TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
        " AND    TRUNC(NVL(U.DATA_FINE_ATTIVITA, TO_DATE('31/12/9999', 'DD/MM/YYYY'))) > TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
        " AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        " AND    CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +  
        " AND    SP.COMUNE = C.ISTAT_COMUNE " +
        " AND    SP.ID_ZONA_ALTIMETRICA = TZA.ID_ZONA_ALTIMETRICA(+) " +
        " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) " +
        " AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA(+) " +
        " AND    UD.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
        " AND    UD.ID_VARIETA = TV.ID_VARIETA(+) " +
        " AND    UD.ID_UTILIZZO_SECONDARIO = TU2.ID_UTILIZZO(+) " +
        " AND    UD.ID_VARIETA_SECONDARIA = TV2.ID_VARIETA(+) " +
        " AND    C2.ISTAT_COMUNE = U.COMUNE ");
        
      
      if(filtriParticellareRicercaVO.getIdUtilizzo().intValue() != 0) 
      {       
        queryBuf.append(""+
            " AND    U.ID_UTE = CD.ID_UTE ");
      }
      
      
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdMacroUso())) 
      {
        queryBuf.append(""+
             " AND UD.ID_VARIETA = TMUV.ID_VARIETA " +
             " AND NVL(UD.ID_TIPO_DETTAGLIO_USO,-1) = NVL(TMUV.ID_TIPO_DETTAGLIO_USO,-1) " +
             " AND TMUV.ID_MACRO_USO = TMU.ID_MACRO_USO " +
             " AND TMUV.DATA_INIZIO_VALIDITA <= ? " +
             " AND NVL(TMUV.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? " + 
             " AND TMU.ID_MACRO_USO = ? ");
      }
      
      
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo())) 
      {
        queryBuf.append(""+
             " AND DS.ID_AZIENDA = ? " +
             " AND DS.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
             " AND DS.ID_DICHIARAZIONE_CONSISTENZA = ? " +
             " AND DS.ID_CONTROLLO = ? ");
      }
      
      // Se l'utente ha indicato l'ute di riferimento
      if(filtriParticellareRicercaVO.getIdUte() != null) 
      {
        queryBuf.append(" AND U.ID_UTE = ? ");
      }
      
      
      
      // Metto in join la tabella DB_FOGLIO con DB_STORICO_PARTICELLA
      // solo se l'utente ha indicato la Z.V.N. o ZVF o PSN di riferimento nei filtri di ricerca
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaE()) 
            || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaF())
            || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaPSN())
            || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaEFasciaFluviale())) 
      {
        queryBuf.append(""+
          " AND    SP.COMUNE = F.COMUNE " +
          " AND    NVL(SP.SEZIONE,'-') = NVL(F.SEZIONE,'-') " +
          " AND    SP.FOGLIO = F.FOGLIO ")
          ;
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaE())) 
        {
          queryBuf.append("AND    F.ID_AREA_E = ? ");
        }
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaF())) 
        {
          queryBuf.append("AND    F.ID_AREA_F = ? ");
        }
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaPSN())) 
        {
          queryBuf.append("AND    F.ID_AREA_PSN = ? ");
        }
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaEFasciaFluviale())) 
        {
        //in ZVN comprensivo di fascia fluviale
          if(filtriParticellareRicercaVO.getIdAreaEFasciaFluviale().longValue() == 1)
          {
            queryBuf.append(""+
            " AND    (SP.ID_FASCIA_FLUVIALE IS NOT NULL " +
            "            OR NVL(F.ID_AREA_E,0) = 2) ");
          }
          //fuori ZVN comprensivo di fascia fluviale
          else
          {
            queryBuf.append(""+
            " AND    SP.ID_FASCIA_FLUVIALE IS NULL " +  
            "AND    NVL(F.ID_AREA_E,1) = 1 ");
          }
        }
      }
      else
      {
        queryBuf.append(""+
            " AND    SP.COMUNE = F.COMUNE(+) " +
            " AND    NVL( SP.SEZIONE,'-') = NVL( F.SEZIONE(+),'-') " +
            " AND    SP.FOGLIO = F.FOGLIO(+) ");
      }
      
      //Combo documenti
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()) 
          || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento())
          || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
      {
        queryBuf.append(""+
        " AND CD.ID_CONDUZIONE_PARTICELLA = DOCC.ID_CONDUZIONE_PARTICELLA " +
        " AND DOCC.ID_DOCUMENTO = DOC1.ID_DOCUMENTO " +
        " AND DOC1.EXT_ID_DOCUMENTO = TDOC.ID_DOCUMENTO " +
        " AND TDOC.ID_DOCUMENTO = DOCCAT.ID_DOCUMENTO " +
        " AND DOCCAT.ID_CATEGORIA_DOCUMENTO = ? " +
        " AND DOC1.DATA_INIZIO_VALIDITA <= DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        " AND NVL(DOC1.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) >= DC.DATA_INSERIMENTO_DICHIARAZIONE ");
        
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
        {
          queryBuf.append(" AND TDOC.ID_DOCUMENTO = ? ");
        }
        
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
        {
          queryBuf.append(" AND DOC1.ID_DOCUMENTO = ? ");
        }
        
      }
      
      //Notifiche
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipologiaNotifica())
          || (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCategoriaNotifica())))
      {
        queryBuf.append(
            " AND  EXISTS  (SELECT NE.ID_NOTIFICA_ENTITA " + 
            "               FROM   DB_NOTIFICA_ENTITA NE, " +
            "                      DB_NOTIFICA NO, " +
            "                      DB_TIPO_ENTITA TE " +
            "               WHERE  TE.CODICE_TIPO_ENTITA = 'P' " +
            "               AND    TE.ID_TIPO_ENTITA = NE.ID_TIPO_ENTITA " +
            "               AND    NE.IDENTIFICATIVO = SP.ID_PARTICELLA " +
            "               AND    NE.ID_NOTIFICA = NO.ID_NOTIFICA " +
            "               AND    NE.ID_DICHIARAZIONE_CONSISTENZA = DC.ID_DICHIARAZIONE_CONSISTENZA ");
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipologiaNotifica()))
        {
          queryBuf.append(
            "               AND    NO.ID_TIPOLOGIA_NOTIFICA = ? ");          
        }
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCategoriaNotifica()))
        {
          queryBuf.append(
            "               AND    NO.ID_CATEGORIA_NOTIFICA = ? ");          
        }
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getFlagNotificheChiuse()))
        {
          queryBuf.append(
             " AND    (NE.DATA_FINE_VALIDITA IS NULL " +
             "         OR NE.DATA_FINE_VALIDITA = (SELECT MAX(NE1.DATA_FINE_VALIDITA) " +
             "                                     FROM   DB_NOTIFICA_ENTITA NE1 " +
             "                                     WHERE  NE1.IDENTIFICATIVO = NE.IDENTIFICATIVO " +
             "                                     AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA " +
             "                                     AND    NE1.ID_DICHIARAZIONE_CONSISTENZA = NE.ID_DICHIARAZIONE_CONSISTENZA)) )");
        }
        else
        {
          queryBuf.append(
             " AND    NE.DATA_FINE_VALIDITA IS NULL) ");
        }
        
      }
      
      
      // Se l'utente ha indicato il comune di riferimento
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIstatComune())) {
        queryBuf.append("AND SP.COMUNE = ? ");
      }
      // Se l'utente ha indicato la sezione di riferimento
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getSezione())) {
        queryBuf.append("AND SP.SEZIONE = ? ");
      }
      // Se l'utente ha indicato il foglio di riferimento
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getFoglio())) {
        queryBuf.append("AND SP.FOGLIO = ? ");
      }
      // Se l'utente ha indicato la particella di riferimento
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getParticella())) {
        queryBuf.append("AND SP.PARTICELLA = ? ");
      }
      // Se l'utente ha indicato il subalterno di riferimento
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getSubalterno())) {
        queryBuf.append("AND SP.SUBALTERNO = ? ");
      }
      // Se l'utente ha indicato il tipo area A
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaA())) {
        queryBuf.append("AND SP.ID_AREA_A = ? ");
      }
      // Se l'utente ha indicato il tipo area B
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaB())) {
        queryBuf.append("AND SP.ID_AREA_B = ? ");
      }
      // Se l'utente ha indicato il tipo area C
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaC())) {
        queryBuf.append("AND SP.ID_AREA_C = ? ");
      }
      // Se l'utente ha indicato il tipo area D
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaD())) {
        queryBuf.append("AND SP.ID_AREA_D = ? ");
      }
      // Se l'utente ha indicato il tipo area M
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaM())) {
        queryBuf.append("AND SP.ID_AREA_M = ? ");
      }
      // Se l'utente ha indicato la fascia fluviale
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdFasciaFluviale())) 
      {
        queryBuf.append("AND SP.ID_FASCIA_FLUVIALE = ? ");
      }
      // Se l'utente ha indicato il tipo Area G
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaG())) 
      {
        queryBuf.append("AND NVL(SP.ID_AREA_G,1) = ? ");
      }
      // Se l'utente ha indicato il tipo Area H
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaH())) 
      {
        queryBuf.append("AND NVL(SP.ID_AREA_H,1) = ? ");
      }
      // Se l'utente ha indicato il tipo Zona Altimetrica
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdZonaAltimetrica())) 
      {
        queryBuf.append("AND SP.ID_ZONA_ALTIMETRICA = ? ");
      }
      // Se l'utente ha indicato il tipo Caso Particolare
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCasoParticolare())) 
      {
        queryBuf.append(" AND SP.ID_CASO_PARTICOLARE = ? ");
      }
      // Se l'utente invece ha specificato un particolare uso del suolo
      if(filtriParticellareRicercaVO.getIdUtilizzo().longValue() > 0) 
      {
        // Se non è stato indicato nei filtri di ricerca se l'utilizzo selezionato
        // è primario o secondario, viene usata come condizione di default quella
        // dell'utilizzo primario
        if(!Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario()) && !Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario())) {
          queryBuf.append(" AND UD.ID_UTILIZZO = ? ");
        }
        else 
        {
          // Uso Primario
          if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario())) 
          {
            queryBuf.append("AND UD.ID_UTILIZZO = ? ");
          }
          // Uso Secondario
          else if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario())) {
            queryBuf.append(" AND UD.ID_UTILIZZO_SECONDARIO = ? ");
          }
          // Se ha selezionato sia l'opzione uso primario che quella uso secondario
          else if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario()) && Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario())) {
            queryBuf.append("AND (UD.ID_UTILIZZO = ?  OR UD.ID_UTILIZZO_SECONDARIO = ?) ");
          }
        }
        
      }
      // Se l'utente ha specificato un particolare titolo di possesso
      if(filtriParticellareRicercaVO.getIdTitoloPossesso() != null) 
      {
        queryBuf.append(" AND CD.ID_TITOLO_POSSESSO = ? ");
      }
      else 
      {
        // Se l'utente ha scelto l'opzione escludi asservimento
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckEscludiAsservimento()) && filtriParticellareRicercaVO.getCheckEscludiAsservimento().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
        {
          queryBuf.append(" AND CD.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO);
        }
        // Se l'utente ha scelto l'opzione escludi conferimento
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckEscludiConferimento()) && filtriParticellareRicercaVO.getCheckEscludiConferimento().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
        {
          queryBuf.append("AND CD.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO);
        }
      }
      
      // Se l'utente ha specificato la tipologia di anomalia bloccante
      boolean isFirst = true;
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneBloccante())) 
      {
        queryBuf.append(" AND (CD.ESITO_CONTROLLO = ? ");
        isFirst = false;
      }
      // Se l'utente ha specificato la tipologia di anomalia warning
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneWarning())) {
        if(!isFirst) 
        {
          queryBuf.append(" OR ");
        }
        else {
          queryBuf.append(" AND (");
        }
        queryBuf.append(" CD.ESITO_CONTROLLO = ? ");
        isFirst = false;
      }
      // Se l'utente ha specificato la tipologia di anomalia OK
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneOk())) 
      {
        if(!isFirst) 
        {
          queryBuf.append(" OR ");
        }
        else {
          queryBuf.append(" AND (");
        }
        queryBuf.append(" CD.ESITO_CONTROLLO = ? ");
        isFirst = false;
      }
      if(!isFirst) {
        queryBuf.append(")");
      }
      
      // Ricerco i dati delle particelle solo asservite
      if(filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {
        queryBuf.append(""+
        " AND DICH_CONS.ID_AZIENDA <> ? " +
        " AND DICH_CONS.DATA_INSERIMENTO_DICHIARAZIONE = "+
        "(SELECT MAX(DATA_INSERIMENTO_DICHIARAZIONE) " +
        " FROM DB_DICHIARAZIONE_CONSISTENZA DC1 " +
        " WHERE DICH_CONS.ID_AZIENDA = DC1.ID_AZIENDA" +
        " AND DC1.DATA_INSERIMENTO_DICHIARAZIONE < DC.DATA_INSERIMENTO_DICHIARAZIONE ) " +
        " AND DICH_CONS.CODICE_FOTOGRAFIA_TERRENI = CD2.CODICE_FOTOGRAFIA_TERRENI " +
        " AND CD2.ID_TITOLO_POSSESSO = ? " +
        " AND CD2.ID_PARTICELLA = SP.ID_PARTICELLA ");
      }
      // Ricerco i dati delle particelle solo conferite
      if(filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {
        queryBuf.append(""+
        " AND DICH_CONS.ID_AZIENDA <> ? " +
        " AND DICH_CONS.DATA_INSERIMENTO_DICHIARAZIONE = "+
        "(SELECT MAX(DATA_INSERIMENTO_DICHIARAZIONE) " +
        " FROM DB_DICHIARAZIONE_CONSISTENZA DC1 " +
        " WHERE DICH_CONS.ID_AZIENDA = DC1.ID_AZIENDA" +
        " AND DC1.DATA_INSERIMENTO_DICHIARAZIONE < DC.DATA_INSERIMENTO_DICHIARAZIONE ) " +
        " AND DICH_CONS.CODICE_FOTOGRAFIA_TERRENI = CD2.CODICE_FOTOGRAFIA_TERRENI " +
        " AND CD2.ID_TITOLO_POSSESSO = ? " +
        " AND CD2.ID_PARTICELLA = SP.ID_PARTICELLA ");
      }
      
      
      //  Se l'utente vuole estrarre qualunque o un determinato uso del suolo
      if(filtriParticellareRicercaVO.getIdUtilizzo().intValue() != 0) 
      {
        queryBuf.append(""+
        " GROUP BY SP.COMUNE, " +
        "          C.DESCOM, " +
        "          P.SIGLA_PROVINCIA, " +
        "          SP.SEZIONE, " +
        "          SP.FOGLIO, " +
        "          SP.PARTICELLA, " +
        "          SP.SUBALTERNO, " +
        "          SP.SUP_CATASTALE, " +
        "          TZA.DESCRIZIONE, " +
        "          U.COMUNE, " +
        "          C2.DESCOM," +
        "          SP.ID_PARTICELLA ");
         
      }        
      queryBuf.append(" ORDER BY DESCOM,SIGLA_PROVINCIA,SEZIONE,FOGLIO,PARTICELLA,SUBALTERNO ");
       
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ConduzioneParticellaGaaDAO::searchParticelleDichiarateAvvicExcelByParameters] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri dello statement
      int indice = 0;
      
      stmt.setLong(++indice, filtriParticellareRicercaVO.getIdPianoRiferimento().longValue());
      stmt.setLong(++indice, idAzienda.longValue());
      // ID_MACRO_USO
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdMacroUso())) 
      { 
        stmt.setDate(++indice, new java.sql.Date(filtriParticellareRicercaVO.getDataInserimentoDichiarazione().getTime()));
        stmt.setDate(++indice, new java.sql.Date(filtriParticellareRicercaVO.getDataInserimentoDichiarazione().getTime()));
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdMacroUso().longValue());
      }
      // ID_CONTROLLO
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo())) 
      {          
        stmt.setLong(++indice, idAzienda.longValue());
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdPianoRiferimento().longValue());
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdControllo().longValue());
      }
      // ID_UTE
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdUte())) 
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUte().longValue());
      }
      // ID_AREA_E(Z.V.N.)
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaE())) 
      {          
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaE().longValue());
      }
      // ID_AREA_F(Z.V.F.)
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaF()))
      {          
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaF().longValue());
      }
      // ID_AREA_PSN(Localizzazione)
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaPSN())) 
      {          
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaPSN().longValue());
      }
      // TIPO DOCUMENTO
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()))
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTipoDocumento().longValue());
      }
      
      // DOCUMENTO
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdDocumento().longValue());
      }
      
      // PROTOCOLLO DOCUMENTO
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdProtocolloDocumento().longValue());
      }
      // TIPOLOGIA NOTIFICA
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipologiaNotifica())) 
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTipologiaNotifica().longValue());
      }
      // CATEGORIA_NOTIFICA
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCategoriaNotifica())) 
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdCategoriaNotifica().longValue());
      }
      // ISTAT_COMUNE
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIstatComune()))
      {
        stmt.setString(++indice, filtriParticellareRicercaVO.getIstatComune());
      }
      // SEZIONE
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getSezione())) 
      {
        stmt.setString(++indice, filtriParticellareRicercaVO.getSezione().toUpperCase());
      }
      // FOGLIO
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getFoglio())) 
      {
        stmt.setString(++indice, filtriParticellareRicercaVO.getFoglio());
      }
      // PARTICELLA
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getParticella())) 
      {
        stmt.setString(++indice, filtriParticellareRicercaVO.getParticella());
      }
      // SUBALTERNO
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getSubalterno())) 
      {
        stmt.setString(++indice, filtriParticellareRicercaVO.getSubalterno().toUpperCase());
      }
      // ID_AREA_A
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaA())) 
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaA().longValue());
      }
      // ID_AREA_B
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaB())) 
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaB().longValue());
      }
      // ID_AREA_C
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaC())) 
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaC().longValue());
      }
      // ID_AREA_D
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaD())) 
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaD().longValue());
      }
      // ID_AREA_M
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaM())) 
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaM().longValue());
      }
      // ID_FASCIA_FLUVIALE
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdFasciaFluviale())) 
      {          
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdFasciaFluviale().longValue());
      }
      // ID_AREA_G
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaG())) 
      {          
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaG().longValue());
      }
      // ID_AREA_H
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdAreaH())) 
      {          
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdAreaH().longValue());
      }
      // ID_ZONA_ALTIMETRICA
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdZonaAltimetrica())) 
      {          
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdZonaAltimetrica().longValue());
      }
      // ID_CASO_PARTICOLARE
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCasoParticolare())) 
      {          
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdCasoParticolare().longValue());
      }
      // Se non è stato indicato se l'uso è primario o secondario
      if(!Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario()) 
          && !Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario())) 
      {
        // Se è stato indicato un particolare uso del suolo, seleziono di default l'opzione
        // uso primario
        if(filtriParticellareRicercaVO.getIdUtilizzo().intValue() > 0) 
        {
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
        }
      }
      else 
      {
        // Se è stato indicato un particolare uso del suolo, seleziono di default l'opzione
        // uso primario
        if(filtriParticellareRicercaVO.getIdUtilizzo().intValue() > 0) 
        {
          // Uso primario
          if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario())) 
          {
            stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
          }
          // Uso secondario
          else if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario())) 
          {
            stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
          }
          // Altrimenti se ha selezionato sia l'opzione uso primario che quella uso secondario
          else if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario()) 
              && Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario())) 
          {
            stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
            stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
          }
        }
      }
      // ID_TIPO_TITOLO_POSSESSO
      if(filtriParticellareRicercaVO.getIdTitoloPossesso() != null) 
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTitoloPossesso().longValue());
      }
      // SEGNALAZIONI
      // Se l'utente ha specificato la tipologia di anomalia bloccante
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneBloccante())) 
      {
        stmt.setString(++indice, filtriParticellareRicercaVO.getTipoSegnalazioneBloccante());
      }
      // Se l'utente ha specificato la tipologia di anomalia warning
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneWarning())) 
      {
        stmt.setString(++indice, filtriParticellareRicercaVO.getTipoSegnalazioneWarning());
      }
      // Se l'utente ha specificato la tipologia di anomalia OK
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneOk()))
      {
        stmt.setString(++indice, filtriParticellareRicercaVO.getTipoSegnalazioneOk());
      }
      
      // Ricerco i dati della particella certificata solo se vengono
      // richieste le particelle solo asservite
      if(filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {         
        stmt.setLong(++indice, idAzienda.longValue());
        stmt.setLong(++indice, SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO.longValue());
      }
      // Ricerco i dati delle particelle solo conferite
      if(filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {         
        stmt.setLong(++indice, idAzienda.longValue());
        stmt.setLong(++indice, SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO.longValue());
      }
      
      ResultSet rs = stmt.executeQuery();
      
      
      while(rs.next()) 
      {
        AnagParticellaExcelVO anagParticellaExcelVO = new AnagParticellaExcelVO();
        anagParticellaExcelVO.setLabelUte(rs.getString("COM_UTE")+" - "+rs.getString("DESC_COM_UTE"));
        anagParticellaExcelVO.setIstatComuneParticella(rs.getString("COMUNE"));
        anagParticellaExcelVO.setDescrizioneComuneParticella(rs.getString("DESCOM"));
        anagParticellaExcelVO.setSezione(rs.getString("SEZIONE"));
        anagParticellaExcelVO.setFoglio(rs.getString("FOGLIO"));
        anagParticellaExcelVO.setParticella(rs.getString("PARTICELLA"));
        anagParticellaExcelVO.setSubalterno(rs.getString("SUBALTERNO"));
        anagParticellaExcelVO.setSuperficieCatastale(StringUtils.parseSuperficieField(rs.getString("SUP_CATASTALE")));
        anagParticellaExcelVO.setDescrizioneZonaAltimetrica(rs.getString("DESC_ZONA_ALT"));
        anagParticellaExcelVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
       
       
        elencoParticelle.add(anagParticellaExcelVO);
      }
    }
      
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("elencoParticelle", elencoParticelle) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
          new Parametro("filtriParticellareRicercaVO", filtriParticellareRicercaVO)
      };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ConduzioneParticellaGaaDAO::searchParticelleDichiarateAvvicExcelByParameters] ", t,
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
          "[ConduzioneParticellaGaaDAO::searchParticelleDichiarateAvvicExcelByParameters] END.");
    }
      
    return elencoParticelle;
  }
  
  
  
  /*public HashMap<Long,HashMap<Integer,AvvicendamentoVO>> getElencoAvvicendamento( 
    Vector<Long> vIdParticella, long idAzienda, Integer annoPartenza, int numeroAnni, 
    Long idDichiarazioneConsistenza) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    HashMap<Long,HashMap<Integer,AvvicendamentoVO>> result = null;
    HashMap<Integer,AvvicendamentoVO> hAvvicendamento = null;
    AvvicendamentoVO avvicendamentoVO = null;
    CallableStatement cs = null;
    
    try
    {
      SolmrLogger.debug(this,
          "[ConduzioneParticellaGaaDAO::getElencoAvvicendamento] BEGIN.");
      
      
      conn = getDatasource().getConnection();

      Long idJavaIns = getNextPrimaryKey(SolmrConstants.SEQ_SMRGAA_W_JAVA_INSERT);

      String insert="INSERT INTO SMRGAA_W_JAVA_INSERT (ID_JAVA_INSERT, ID_DETTAGLIO_INS) "+
                    "VALUES ( ?, ?) ";

      stmt = conn.prepareStatement(insert);
      for (int i=0;i<vIdParticella.size();i++)
      {
        stmt.setLong(1,idJavaIns.longValue());
        stmt.setLong(2, vIdParticella.get(i).longValue());
        stmt.addBatch();
      }
      stmt.executeBatch();

      SolmrLogger.debug(this,"[ConduzioneParticellaGaaDAO::getElencoAvvicendamento] insert executed");

      stmt.close();
      
      
      //Chiamata PlSql da fare!!!!!
      //???????????????????????????????????
      
      
       //   PROCEDURE Verifica_Avvicendamento (pIdAzienda   IN SMRGAA_W_AVVICENDAMENTO_COND.ID_AZIENDA%TYPE,
         //                              pIdRichAvv   IN SMRGAA_W_AVVICENDAMENTO_COND.ID_RICH_AVVICENDAMENTO%TYPE,
           //                            pAnnoFine    IN INTEGER,
       //                                pNumeroAnni  IN INTEGER,
         //                              pIdDichCons  IN DB_DICHIARAZIONE_CONSISTENZA.ID_DICHIARAZIONE_CONSISTENZA%TYPE,
           //                            pRisultato  OUT VARCHAR2,
          //                             pMessaggio  OUT VARCHAR2
         //                              );
      
  
      // CONCATENAZIONE/CREAZIONE QUERY BEGIN. 
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{call PCK_SMRGAA_LIBRERIA.Verifica_Avvicendamento(?,?,?,?,?,?,?)}");
      
      
      
      
      query = queryBuf.toString();
      // CONCATENAZIONE/CREAZIONE QUERY END. 
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ConduzioneParticellaGaaDAO::getElencoAvvicendamento::Verifica_Avvicendamento] Query="
                + query);
      }
      
      cs = conn.prepareCall(query);
      cs.setLong(1, idAzienda);
      cs.setLong(2, idJavaIns.longValue());
      cs.setInt(3, annoPartenza.intValue());
      cs.setInt(4, numeroAnni);
      if(Validator.isNotEmpty(idDichiarazioneConsistenza))
      {
        cs.setLong(5, idDichiarazioneConsistenza.longValue());
      }
      else
      {
        cs.setNull(5, Types.DECIMAL);
      }
      cs.registerOutParameter(6,Types.VARCHAR); //codice errore
      cs.registerOutParameter(7,Types.VARCHAR); //msg errore
      
      cs.executeUpdate();
      
      String risultato = cs.getString(6);
      String msgErrore = cs.getString(7);
      cs.close();
      
      if(!"0".equalsIgnoreCase(risultato))
        throw new DataAccessException("Errore nella chiamata plsql PCK_SMRGAA_LIBRERIA.Verifica_Avvicendamento: "
            +msgErrore);
      
      

      queryBuf = new StringBuffer();

      // CONCATENAZIONE/CREAZIONE QUERY BEGIN. 
      queryBuf.append(
        "SELECT WAC.ID_PARTICELLA, " +
        "       WAC.ID_AZIENDA, " +
        "       WAC.ANNO, " +
        "       WAC.CONDUZIONE_IN_AZIENDA, " +
        "       WAC.PRESENZA_DU_PSR, " +
        "       WAC.SUPERFICIE_UTILIZZATA, " +
        "       WAC.ID_UTILIZZO, " +
        "       TU.CODICE AS COD_UTILIZZO, " +
        "       TU.DESCRIZIONE AS DESC_UTILIZZO, " +
        "       TV.CODICE_VARIETA, " +
        "       TV.DESCRIZIONE AS DESC_VARIETA, " +
        "       WAC.ID_VARIETA, " +
        "       WAC.ESITO, " +
        "       WAC.MESSAGGIO " +
        "FROM   SMRGAA_W_AVVICENDAMENTO_COND  WAC, " +
        "       SMRGAA_W_JAVA_INSERT WIJ, " +
        "       DB_TIPO_UTILIZZO TU, " +
        "       DB_TIPO_VARIETA TV " +
        "WHERE  WIJ.ID_JAVA_INSERT = ? " +
        "AND    WIJ.ID_DETTAGLIO_INS = WAC.ID_PARTICELLA " +
        "AND    WIJ.ID_JAVA_INSERT = WAC.ID_RICH_AVVICENDAMENTO " + 
        //"AND    WAC.ID_AZIENDA = ? " +
        "AND    WAC.ID_UTILIZZO = TU.ID_UTILIZZO " +
        "AND    WAC.ID_VARIETA = TV.ID_VARIETA " +
        "ORDER BY WAC.ID_PARTICELLA, " +
        "         WAC.ANNO, " +
        "         TU.DESCRIZIONE");

      
      // CONCATENAZIONE/CREAZIONE QUERY END. 

      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ConduzioneParticellaGaaDAO::getElencoAvvicendamento] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int idx = 0;
      
      stmt.setLong(++idx, idJavaIns.longValue());
      //stmt.setLong(++idx, idAzienda);     
      
      rs = stmt.executeQuery();
      Long idParticellaTmp = new Long(0);
      Integer annoTmp = new Integer(0);
      boolean almenoUnaParticella = false;
      while (rs.next())
      {
        almenoUnaParticella = true;
        if(result == null)
        {
          result = new HashMap<Long,HashMap<Integer,AvvicendamentoVO>>();
        }
        
        Long idParticella = new Long(rs.getLong("ID_PARTICELLA"));
        Integer anno = new Integer(rs.getInt("ANNO"));
        if(idParticellaTmp.compareTo(idParticella) != 0)
        {
          //eseguo solo se diverso dal primo
          if(idParticellaTmp.compareTo(new Long(0)) != 0)
          {
            hAvvicendamento.put(annoTmp, avvicendamentoVO);
            result.put(idParticellaTmp, hAvvicendamento);
          }
          hAvvicendamento = new HashMap<Integer,AvvicendamentoVO>();
          
          avvicendamentoVO = new AvvicendamentoVO();
          avvicendamentoVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
          avvicendamentoVO.setIdParticella(idParticella);
          avvicendamentoVO.setEsito(rs.getString("ESITO"));
          avvicendamentoVO.setMessaggio(rs.getString("MESSAGGIO"));
        }
        else
        {
          if(annoTmp.compareTo(anno) != 0)
          {
            if(annoTmp.compareTo(new Integer(0)) != 0)
            {
              hAvvicendamento.put(annoTmp, avvicendamentoVO);
            }
            avvicendamentoVO = new AvvicendamentoVO();
            avvicendamentoVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
            avvicendamentoVO.setIdParticella(idParticella);
            avvicendamentoVO.setEsito(rs.getString("ESITO"));
            avvicendamentoVO.setMessaggio(rs.getString("MESSAGGIO"));
          }          
        }
        
        Vector<CodeDescription> vTipoUtilizzo = avvicendamentoVO.getvTipoUtilizzo();
        if(vTipoUtilizzo == null)
        {
          vTipoUtilizzo = new Vector<CodeDescription>();          
        }
        CodeDescription cdUtilizzo = new CodeDescription();
        cdUtilizzo.setCode(new Integer(rs.getInt("ID_UTILIZZO")));
        cdUtilizzo.setDescription(rs.getString("DESC_UTILIZZO"));
        cdUtilizzo.setSecondaryCode(rs.getString("COD_UTILIZZO"));
        vTipoUtilizzo.add(cdUtilizzo);
        avvicendamentoVO.setvTipoUtilizzo(vTipoUtilizzo);
        
        Vector<CodeDescription> vTipoVarieta = avvicendamentoVO.getvTipoVarieta();
        if(vTipoVarieta == null)
        {
          vTipoVarieta = new Vector<CodeDescription>();          
        }
        CodeDescription cdVarieta = new CodeDescription();
        cdVarieta.setCode(new Integer(rs.getInt("ID_VARIETA")));
        cdVarieta.setDescription(rs.getString("DESC_VARIETA"));
        cdVarieta.setSecondaryCode(rs.getString("CODICE_VARIETA"));
        vTipoVarieta.add(cdVarieta);
        avvicendamentoVO.setvTipoVarieta(vTipoVarieta);
        
        Vector<BigDecimal> vSuperficieUtilizzata = avvicendamentoVO.getvSuperficieUtilizzata();
        if(vSuperficieUtilizzata == null)
        {
          vSuperficieUtilizzata = new Vector<BigDecimal>();          
        }
        vSuperficieUtilizzata.add(rs.getBigDecimal("SUPERFICIE_UTILIZZATA"));
        avvicendamentoVO.setvSuperficieUtlizzata(vSuperficieUtilizzata);
        
        Vector<String> vConduzioneInAzienda = avvicendamentoVO.getvConduzioneInAzienda();
        if(vConduzioneInAzienda == null)
        {
          vConduzioneInAzienda = new Vector<String>();          
        }
        vConduzioneInAzienda.add(rs.getString("CONDUZIONE_IN_AZIENDA"));
        avvicendamentoVO.setvConduzioneInAzienda(vConduzioneInAzienda);
        
        Vector<String> vPresenzaDuPsr = avvicendamentoVO.getvPresenzaDuPsr();
        if(vPresenzaDuPsr == null)
        {
          vPresenzaDuPsr = new Vector<String>();          
        }
        vPresenzaDuPsr.add(rs.getString("PRESENZA_DU_PSR"));
        avvicendamentoVO.setvPresenzaDuPsr(vPresenzaDuPsr);
        
        
        annoTmp = anno; 
        idParticellaTmp = idParticella; 
      }
      
      if(almenoUnaParticella)
      {
        hAvvicendamento.put(annoTmp, avvicendamentoVO);
        result.put(idParticellaTmp, hAvvicendamento);
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("result", result),  new Variabile("hAvvicendamento", hAvvicendamento),
          new Variabile("avvicendamentoVO", avvicendamentoVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("vIdParticella", vIdParticella),
          new Parametro("idAzienda", idAzienda),
          new Parametro("annoPartenza", annoPartenza),
          new Parametro("numeroAnni", numeroAnni),
          new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza)
      };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ConduzioneParticellaGaaDAO::getElencoAvvicendamento] ", t,
          query, variabili, parametri);
      
       //Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       //informazione sui codici di errore utilizzati vedere il javadoc di
       // BaseDAO.convertIntoGaaservInternalException()
       
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      
       // Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       // ignora ogni eventuale eccezione)
       
      close(rs, stmt, conn);
      closePlSql(cs, conn);

      // Fine metodo
      SolmrLogger.debug(this,
          "[ConduzioneParticellaGaaDAO::getElencoAvvicendamento] END.");
    }
  }*/
  
  
  public Vector<StoricoParticellaVO> getElencoStoricoParticellaByNotifica(long idNotifica, 
      long idDichiarazioneConsistenza, boolean storico) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<StoricoParticellaVO>  vParticelle = null;
    StoricoParticellaVO storicoParticellaVO = null;
    Vector<ConduzioneDichiarataVO>  vConduzioni = null;
    ConduzioneDichiarataVO conduzioneDichiarataVO = null;
    Vector<UtilizzoDichiaratoVO>  vUtilizzi = null;
    UtilizzoDichiaratoVO utilizzoDichiaratoVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[ConduzioneParticellaGaaDAO::getElencoStoricoParticellaByNotifica] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
          "WITH ENTITA AS " +
          "      (SELECT NE.IDENTIFICATIVO " +
          "       FROM DB_NOTIFICA_ENTITA NE " +
          "       WHERE NE.ID_NOTIFICA = ? " +
          "       AND NE.DATA_FINE_VALIDITA IS NULL ");
        if(storico)
        {
          queryBuf.append(
          "       UNION " +
          "       SELECT NE.IDENTIFICATIVO " +
          "       FROM DB_NOTIFICA_ENTITA NE " +
          "       WHERE NE.ID_NOTIFICA = ? " +
          "       AND NE.DATA_FINE_VALIDITA = (SELECT MAX(NE1.DATA_FINE_VALIDITA) " +
          "                                    FROM   DB_NOTIFICA_ENTITA NE1 " +
          "                                    WHERE  NE1.IDENTIFICATIVO = NE.IDENTIFICATIVO " +
          "                                    AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA " +
          "                                    AND    NE1.DATA_FINE_VALIDITA IS NOT NULL) " +
          "       AND    NE.IDENTIFICATIVO NOT IN (SELECT NE1.IDENTIFICATIVO " +
          "                                        FROM   DB_NOTIFICA_ENTITA NE1 " +
          "                                        WHERE  NE1.IDENTIFICATIVO = NE.IDENTIFICATIVO " +
          "                                        AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA " +
          "                                        AND    NE1.DATA_FINE_VALIDITA IS NULL) ");
        }
        queryBuf.append(
          " ) "  +
          "SELECT SP.ID_STORICO_PARTICELLA, " +
          "       SP.ID_PARTICELLA, " +
          "       SP.COMUNE, " +
          "       C.DESCOM, " +
          "       P.SIGLA_PROVINCIA, " +
          "       SP.SEZIONE, " +
          "       SP.FOGLIO, " +
          "       SP.PARTICELLA, " +
          "       SP.SUBALTERNO, " +
          "       UD.ID_UTILIZZO_DICHIARATO, " +
          "       UD.ID_VARIETA, " +
          "       TVAR.DESCRIZIONE AS DESC_VARIETA, " +
          "       TVAR.CODICE_VARIETA AS COD_VARIETA, " +
          "       UD.ID_UTILIZZO, " +
          "       UD.SUPERFICIE_UTILIZZATA, " +
          "       TU.CODICE AS COD_UTILIZZO, " +
          "       TU.DESCRIZIONE AS DESC_UTILIZZO, " +
          "       CD.ID_CONDUZIONE_DICHIARATA, " +
          "       CD.ID_TITOLO_POSSESSO, " +
          "       CD.PERCENTUALE_POSSESSO " +
          "FROM   DB_STORICO_PARTICELLA SP, " +
          "       COMUNE C, " +
          "       PROVINCIA P, " +
          "       DB_TIPO_VARIETA TVAR, " +
          "       DB_TIPO_UTILIZZO TU, " +
          "       DB_DICHIARAZIONE_CONSISTENZA DC, " +
          "       DB_CONDUZIONE_DICHIARATA CD, " +
          "       DB_UTILIZZO_DICHIARATO UD, " +
          "       ENTITA EN " +
        "WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        "AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA (+) " +  
        "AND    EN.IDENTIFICATIVO = SP.ID_PARTICELLA " +
        "AND    CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
        "AND    SP.COMUNE = C.ISTAT_COMUNE " +  
        "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +        
        "AND    UD.ID_VARIETA = TVAR.ID_VARIETA(+) " +
        "AND    UD.ID_UTILIZZO = TVAR.ID_UTILIZZO(+) " + 
        "AND    UD.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
        "ORDER BY  C.DESCOM, " +
        "          SP.SEZIONE, " +
        "          SP.FOGLIO, " +
        "          SP.PARTICELLA, " +
        "          SP.SUBALTERNO, " +
        "          CD.ID_TITOLO_POSSESSO, " +
        "          CD.ID_CONDUZIONE_DICHIARATA, " +
        "          TU.DESCRIZIONE ASC, " +
        "          TVAR.DESCRIZIONE ASC ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ConduzioneParticellaGaaDAO::getElencoStoricoParticellaByNotifica] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int idx = 0;
      
      stmt.setLong(++idx, idNotifica);
      if(storico)
      {
        stmt.setLong(++idx, idNotifica);
      }
      stmt.setLong(++idx, idDichiarazioneConsistenza);
      
      
      ResultSet rs = stmt.executeQuery();
      long idParticellaTmp = 0;
      long idConduzioneTmp = 0;
      Long idUtilizzo = null;
      int i=0;
      while (rs.next())
      {
        if(vParticelle == null)
        {
          vParticelle = new Vector<StoricoParticellaVO>();
        }
        long idParticella = rs.getLong("ID_PARTICELLA");
        long idConduzione = rs.getLong("ID_CONDUZIONE_DICHIARATA");
        idUtilizzo = checkLongNull(rs.getString("ID_UTILIZZO_DICHIARATO"));
        if(idParticellaTmp != idParticella)
        {
          if(i != 0)
          {
            storicoParticellaVO.setvConduzioniDichiarate(vConduzioni);
            vParticelle.add(storicoParticellaVO);
          }          
          vConduzioni = new Vector<ConduzioneDichiarataVO>();
          
          storicoParticellaVO = new StoricoParticellaVO();
          storicoParticellaVO.setIdParticella(new Long(idParticella));
          storicoParticellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
          ComuneVO comuneParticellaVO = new ComuneVO();
          comuneParticellaVO.setDescom(rs.getString("DESCOM"));
          comuneParticellaVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
          storicoParticellaVO.setComuneParticellaVO(comuneParticellaVO);
          storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
          storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
          storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
          storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));          
          
        }
        
        if(idConduzioneTmp != idConduzione)
        {            
          if(i !=0)
          {
            conduzioneDichiarataVO.setvUtilizzi(vUtilizzi);
          }
          if(Validator.isNotEmpty(idUtilizzo))
          {
            vUtilizzi = new Vector<UtilizzoDichiaratoVO>();
          }
          
          conduzioneDichiarataVO = new ConduzioneDichiarataVO();
          conduzioneDichiarataVO.setIdConduzioneDichiarata(new Long(rs.getLong("ID_CONDUZIONE_DICHIARATA")));
          conduzioneDichiarataVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
          conduzioneDichiarataVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
          vConduzioni.add(conduzioneDichiarataVO);
          
        }
        
        if(Validator.isNotEmpty(idUtilizzo))
        {
          utilizzoDichiaratoVO = new UtilizzoDichiaratoVO();
          utilizzoDichiaratoVO.setIdUtilizzoDichiarato(idUtilizzo);
          TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
          tipoUtilizzoVO.setCodice(rs.getString("COD_UTILIZZO"));
          tipoUtilizzoVO.setDescrizione(rs.getString("DESC_UTILIZZO"));
          utilizzoDichiaratoVO.setTipoUtilizzoVO(tipoUtilizzoVO);
          TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
          tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VARIETA"));
          tipoVarietaVO.setDescrizione(rs.getString("DESC_VARIETA"));
          utilizzoDichiaratoVO.setTipoVarietaVO(tipoVarietaVO);
          utilizzoDichiaratoVO.setSupUtilizzataBg(rs.getBigDecimal("SUPERFICIE_UTILIZZATA"));
          
          if(conduzioneDichiarataVO.getvUtilizzi() != null)
          {  
            vUtilizzi = conduzioneDichiarataVO.getvUtilizzi(); 
          }
          vUtilizzi.add(utilizzoDichiaratoVO);
          conduzioneDichiarataVO.setvUtilizzi(vUtilizzi);
          
        }
       
        idParticellaTmp = idParticella;
        idConduzioneTmp = idConduzione;
        i++;
      }
      
      //Almeno un record
      if(i !=0)
      {
        storicoParticellaVO.setvConduzioniDichiarate(vConduzioni);
        vParticelle.add(storicoParticellaVO);
      }
      
      
      return vParticelle;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("vParticelle", vParticelle), new Variabile("storicoParticellaVO", storicoParticellaVO),
          new Variabile("vConduzioni", vConduzioni), new Variabile("conduzioneDichiarataVO", conduzioneDichiarataVO),
          new Variabile("vConduzioni", vUtilizzi), new Variabile("utilizzoDichiaratoVO", utilizzoDichiaratoVO)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idNotifica", idNotifica),
        new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza)};

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ConduzioneParticellaGaaDAO::getElencoStoricoParticellaByNotifica] ", t,
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
          "[ConduzioneParticellaGaaDAO::getElencoStoricoParticellaByNotifica] END.");
    }
  }
  
  
  
  public Vector<StoricoParticellaVO> getElencoStoricoParticellaByNotificaNoDich(long idNotifica, 
      boolean storico) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<StoricoParticellaVO>  vParticelle = null;
    StoricoParticellaVO storicoParticellaVO = null;
    Vector<ConduzioneDichiarataVO>  vConduzioni = null;
    ConduzioneDichiarataVO conduzioneDichiarataVO = null;
    Vector<UtilizzoDichiaratoVO>  vUtilizzi = null;
    UtilizzoDichiaratoVO utilizzoDichiaratoVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[ConduzioneParticellaGaaDAO::getElencoStoricoParticellaByNotificaNoDich] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
          "WITH ENTITA AS " +
          "      (SELECT NE.IDENTIFICATIVO " +
          "       FROM DB_NOTIFICA_ENTITA NE " +
          "       WHERE NE.ID_NOTIFICA = ? " +
          "       AND NE.DATA_FINE_VALIDITA IS NULL ");
        if(storico)
        {
          queryBuf.append(
          "       UNION " +
          "       SELECT NE.IDENTIFICATIVO " +
          "       FROM DB_NOTIFICA_ENTITA NE " +
          "       WHERE NE.ID_NOTIFICA = ? " +
          "       AND NE.DATA_FINE_VALIDITA = (SELECT MAX(NE1.DATA_FINE_VALIDITA) " +
          "                                    FROM   DB_NOTIFICA_ENTITA NE1 " +
          "                                    WHERE  NE1.IDENTIFICATIVO = NE.IDENTIFICATIVO " +
          "                                    AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA " +
          "                                    AND    NE1.DATA_FINE_VALIDITA IS NOT NULL) " +
          "       AND    NE.IDENTIFICATIVO NOT IN (SELECT NE1.IDENTIFICATIVO " +
          "                                        FROM   DB_NOTIFICA_ENTITA NE1 " +
          "                                        WHERE  NE1.IDENTIFICATIVO = NE.IDENTIFICATIVO " +
          "                                        AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA " +
          "                                        AND    NE1.DATA_FINE_VALIDITA IS NULL) ");
        }
        queryBuf.append(
          " ) "  +
          "SELECT SP.ID_STORICO_PARTICELLA, " +
          "       SP.ID_PARTICELLA, " +
          "       SP.COMUNE, " +
          "       C.DESCOM, " +
          "       P.SIGLA_PROVINCIA, " +
          "       SP.SEZIONE, " +
          "       SP.FOGLIO, " +
          "       SP.PARTICELLA, " +
          "       SP.SUBALTERNO, " +
          "       UP.ID_UTILIZZO_PARTICELLA, " +
          "       UP.ID_VARIETA, " +
          "       TVAR.DESCRIZIONE AS DESC_VARIETA, " +
          "       TVAR.CODICE_VARIETA AS COD_VARIETA, " +
          "       UP.ID_UTILIZZO, " +
          "       UP.SUPERFICIE_UTILIZZATA, " +
          "       TU.CODICE AS COD_UTILIZZO, " +
          "       TU.DESCRIZIONE AS DESC_UTILIZZO, " +
          "       CP.ID_CONDUZIONE_PARTICELLA, " +
          "       CP.ID_TITOLO_POSSESSO, " +
          "       CP.PERCENTUALE_POSSESSO " +
          "FROM   DB_STORICO_PARTICELLA SP, " +
          "       COMUNE C, " +
          "       PROVINCIA P, " +
          "       DB_TIPO_VARIETA TVAR, " +
          "       DB_TIPO_UTILIZZO TU, " +
          "       DB_CONDUZIONE_PARTICELLA CP, " +
          "       DB_UTILIZZO_PARTICELLA UP, " +
          "       ENTITA EN, " +
          "       DB_NOTIFICA NO, " +
          "       DB_UTE UT " +
          "WHERE  NO.ID_NOTIFICA = ? " +
          "AND    NO.ID_AZIENDA = UT.ID_AZIENDA " +
          "AND    UT.DATA_FINE_ATTIVITA IS NULL " +
          "AND    UT.ID_UTE = CP.ID_UTE " +
          "AND    EN.IDENTIFICATIVO = SP.ID_PARTICELLA "+
          "AND    SP.DATA_FINE_VALIDITA IS NULL " +
          "AND    SP.ID_PARTICELLA = CP.ID_PARTICELLA " +
          "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
          "AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA (+) " +  
          "AND    SP.COMUNE = C.ISTAT_COMUNE " +  
          "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +        
          "AND    UP.ID_VARIETA = TVAR.ID_VARIETA(+) " +
          "AND    UP.ID_UTILIZZO = TVAR.ID_UTILIZZO(+) " + 
          "AND    UP.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
          "ORDER BY  C.DESCOM, " +
          "          SP.SEZIONE, " +
          "          SP.FOGLIO, " +
          "          SP.PARTICELLA, " +
          "          SP.SUBALTERNO, " +
          "          CP.ID_TITOLO_POSSESSO, " +
          "          CP.ID_CONDUZIONE_PARTICELLA, " +
          "          TU.DESCRIZIONE ASC, " +
          "          TVAR.DESCRIZIONE ASC ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ConduzioneParticellaGaaDAO::getElencoStoricoParticellaByNotificaNoDich] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int idx = 0;
      
      stmt.setLong(++idx, idNotifica);
      if(storico)
      {
        stmt.setLong(++idx, idNotifica);
      }
      stmt.setLong(++idx, idNotifica);
      
      
      ResultSet rs = stmt.executeQuery();
      long idParticellaTmp = 0;
      long idConduzioneTmp = 0;
      Long idUtilizzo = null;
      int i=0;
      while (rs.next())
      {
        if(vParticelle == null)
        {
          vParticelle = new Vector<StoricoParticellaVO>();
        }
        long idParticella = rs.getLong("ID_PARTICELLA");
        long idConduzione = rs.getLong("ID_CONDUZIONE_PARTICELLA");
        idUtilizzo = checkLongNull(rs.getString("ID_UTILIZZO_PARTICELLA"));
        if(idParticellaTmp != idParticella)
        {
          if(i != 0)
          {
            storicoParticellaVO.setvConduzioniDichiarate(vConduzioni);
            vParticelle.add(storicoParticellaVO);
          }          
          vConduzioni = new Vector<ConduzioneDichiarataVO>();
          
          storicoParticellaVO = new StoricoParticellaVO();
          storicoParticellaVO.setIdParticella(new Long(idParticella));
          storicoParticellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
          ComuneVO comuneParticellaVO = new ComuneVO();
          comuneParticellaVO.setDescom(rs.getString("DESCOM"));
          comuneParticellaVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
          storicoParticellaVO.setComuneParticellaVO(comuneParticellaVO);
          storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
          storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
          storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
          storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));          
          
        }
        
        if(idConduzioneTmp != idConduzione)
        {            
          if(i !=0)
          {
            conduzioneDichiarataVO.setvUtilizzi(vUtilizzi);
          }
          if(Validator.isNotEmpty(idUtilizzo))
          {
            vUtilizzi = new Vector<UtilizzoDichiaratoVO>();
          }
          
          conduzioneDichiarataVO = new ConduzioneDichiarataVO();
          conduzioneDichiarataVO.setIdConduzioneDichiarata(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));
          conduzioneDichiarataVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
          conduzioneDichiarataVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
          vConduzioni.add(conduzioneDichiarataVO);
          
        }
        
        if(Validator.isNotEmpty(idUtilizzo))
        {
          utilizzoDichiaratoVO = new UtilizzoDichiaratoVO();
          utilizzoDichiaratoVO.setIdUtilizzoDichiarato(idUtilizzo);
          TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
          tipoUtilizzoVO.setCodice(rs.getString("COD_UTILIZZO"));
          tipoUtilizzoVO.setDescrizione(rs.getString("DESC_UTILIZZO"));
          utilizzoDichiaratoVO.setTipoUtilizzoVO(tipoUtilizzoVO);
          TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
          tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VARIETA"));
          tipoVarietaVO.setDescrizione(rs.getString("DESC_VARIETA"));
          utilizzoDichiaratoVO.setTipoVarietaVO(tipoVarietaVO);
          utilizzoDichiaratoVO.setSupUtilizzataBg(rs.getBigDecimal("SUPERFICIE_UTILIZZATA"));
          
          if(conduzioneDichiarataVO.getvUtilizzi() != null)
          {  
            vUtilizzi = conduzioneDichiarataVO.getvUtilizzi(); 
          }
          vUtilizzi.add(utilizzoDichiaratoVO);
          conduzioneDichiarataVO.setvUtilizzi(vUtilizzi);
          
        }
       
        idParticellaTmp = idParticella;
        idConduzioneTmp = idConduzione;
        i++;
      }
      
      //Almeno un record
      if(i !=0)
      {
        storicoParticellaVO.setvConduzioniDichiarate(vConduzioni);
        vParticelle.add(storicoParticellaVO);
      }
      
      
      return vParticelle;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("vParticelle", vParticelle), new Variabile("storicoParticellaVO", storicoParticellaVO),
          new Variabile("vConduzioni", vConduzioni), new Variabile("conduzioneDichiarataVO", conduzioneDichiarataVO),
          new Variabile("vConduzioni", vUtilizzi), new Variabile("utilizzoDichiaratoVO", utilizzoDichiaratoVO)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idNotifica", idNotifica)};

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ConduzioneParticellaGaaDAO::getElencoStoricoParticellaByNotificaNoDich] ", t,
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
          "[ConduzioneParticellaGaaDAO::getElencoStoricoParticellaByNotificaNoDich] END.");
    }
  }
  
  
  
}
