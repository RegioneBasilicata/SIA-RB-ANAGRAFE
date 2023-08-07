package it.csi.smranag.smrgaa.integration.terreni;

import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.dto.terreni.CatalogoMatriceSeminaVO;
import it.csi.smranag.smrgaa.dto.terreni.CatalogoMatriceVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoDestinazioneVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoEfaVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoFaseAllevamentoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoPeriodoSeminaVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoPraticaMantenimentoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoQualitaUsoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoSeminaVO;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.anag.terreni.TipoUtilizzoVO;
import it.csi.solmr.dto.anag.terreni.TipoVarietaVO;
import it.csi.solmr.dto.anag.terreni.UtilizzoParticellaVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Struct;
import java.sql.Types;
import java.sql.Array;
import java.util.Vector;

public class UtilizzoParticellaGaaDAO extends it.csi.solmr.integration.BaseDAO
{

  public UtilizzoParticellaGaaDAO() throws ResourceAccessException
  {
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public UtilizzoParticellaGaaDAO(String refName) throws ResourceAccessException
  {
    super(refName);
  }

  /**
   * 
   * Mi ritorna tutti gli utilizzi attivi relativi ad una conduzione
   * 
   * 
   * @param idConduzioneParticella
   * @return
   * @throws DataAccessException
   */
  public Vector<Long> getIdUtilizzoFromIdIdConduzione(long idConduzioneParticella) 
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
          "[UtilizzoParticellaGaaDAO::getIdUtilizzoFromIdIdConduzione] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT UP.ID_UTILIZZO_PARTICELLA " +
        "FROM   DB_UTILIZZO_PARTICELLA UP " +
        "WHERE  UP.ID_CONDUZIONE_PARTICELLA = ? ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[UtilizzoParticellaGaaDAO::getIdUtilizzoFromIdIdConduzione] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int idx = 0;
      
      stmt.setLong(++idx, idConduzioneParticella);
      
      
      
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {        
        if(result == null)
        {
          result = new Vector<Long>();
        }
        
        result.add(checkLong(rs.getString("ID_UTILIZZO_PARTICELLA")));        
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
      { new Parametro("idConduzioneParticella", idConduzioneParticella)   };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[UtilizzoParticellaGaaDAO::getIdUtilizzoFromIdIdConduzione] ", t,
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
          "[UtilizzoParticellaGaaDAO::getIdUtilizzoFromIdIdConduzione] END.");
    }
  }
  
  /**
   * 
   * ritorna la sup utilizzata di tutte le conduzioni della particella dell'azienda
   * 
   * 
   * @param idAzienda
   * @param idParticella
   * @return
   * @throws DataAccessException
   */
  public BigDecimal getSumSupUtilizzoParticellaAzienda(long idAzienda, long idParticella) 
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
          "[UtilizzoParticellaGaaDAO::getSumSupUtilizzoParticellaAzienda] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT SUM(UP.SUPERFICIE_UTILIZZATA) AS TOT_SUP " +
        "FROM   DB_UTILIZZO_PARTICELLA UP, " +
        "       DB_CONDUZIONE_PARTICELLA CP, " +
        "       DB_UTE U " +
        "WHERE  U.ID_AZIENDA = ? " +
        "AND    CP.ID_PARTICELLA = ? " +
        "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "AND    U.ID_UTE = CP.ID_UTE " +
        "AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[UtilizzoParticellaGaaDAO::getSumSupUtilizzoParticellaAzienda] Query="
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
          "[UtilizzoParticellaGaaDAO::getSumSupUtilizzoParticellaAzienda] ", t,
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
          "[UtilizzoParticellaGaaDAO::getSumSupUtilizzoParticellaAzienda] END.");
    }
  }
  
  /**
   * 
   * ritona la somma degli utilizzi della singola conduzione
   * 
   * 
   * @param idConduzioneParticella
   * @return
   * @throws DataAccessException
   */
  public BigDecimal getSumSupUtilizzoConduzione(long idConduzioneParticella) 
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
          "[UtilizzoParticellaGaaDAO::getSumSupUtilizzoConduzione] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT SUM(UP.SUPERFICIE_UTILIZZATA) AS TOT_SUP " +
        "FROM   DB_UTILIZZO_PARTICELLA UP " +
        "WHERE  UP.ID_CONDUZIONE_PARTICELLA = ? ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[UtilizzoParticellaGaaDAO::getSumSupUtilizzoConduzione] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int idx = 0;
      
      stmt.setLong(++idx, idConduzioneParticella);
      
      
      
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      {        
        result = rs.getBigDecimal("TOT_SUP"); 
      }
      
      if(result == null)
      {
        result = new BigDecimal(0);
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
      { new Parametro("idConduzioneParticella", idConduzioneParticella) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[UtilizzoParticellaGaaDAO::getSumSupUtilizzoConduzione] ", t,
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
          "[UtilizzoParticellaGaaDAO::getSumSupUtilizzoConduzione] END.");
    }
  }
  
  
  public Vector<UtilizzoParticellaVO> riepilogoTipoEfaPianoLavorazione(long idAzienda) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<UtilizzoParticellaVO> vUtilizzo = null;
    UtilizzoParticellaVO utilizzoParticellaVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[UtilizzoParticellaGaaDAO::riepilogoTipoEfaPianoLavorazione] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT ID_TIPO_EFA," +
        "       DESCRIZIONE_TIPO_EFA, " +
        "       ID_UNITA_MISURA, " +
        "       DESC_UNITA_MISURA, " +
        "       SUM(VALORE_ORIGINALE) AS VALORE_ORIGINALE, " +
        "       SUM(VALORE_DOPO_CONVERSIONE) AS VALORE_DOPO_CONVERSIONE, " +
        "       SUM(VALORE_DOPO_PONDERAZIONE) AS VALORE_DOPO_PONDERAZIONE, " +
        "       PADRE_EFA, " +
        "       ORDINAMENTO," +
        "       DICHIARABILE " +
        "FROM " +
        "  (SELECT TE.ID_TIPO_EFA," +
        "          TE.DESCRIZIONE_TIPO_EFA, " +
        "          TE.ID_UNITA_MISURA, " +
        "          UM.DESCRIZIONE AS DESC_UNITA_MISURA, " +
        "          AE.VALORE_ORIGINALE, " +
        "          AE.VALORE_DOPO_CONVERSIONE, " +
        "          AE.VALORE_DOPO_PONDERAZIONE, " +
        "          (CASE "+
        "               WHEN EXISTS (SELECT *  " +
        "                            FROM DB_TIPO_EFA TE1 " +
        "                            WHERE TE1.ID_TIPO_EFA_PADRE = TE.ID_TIPO_EFA " +
        "                            AND   TE1.DATA_FINE_VALIDITA IS NULL) " +
        "               THEN  'S' "+
        "            END) AS PADRE_EFA, " +
        "          TE.ORDINAMENTO, " +
        "          TE.DICHIARABILE "+
        "   FROM   DB_AZIENDA_EFA AE, " +
        "          DB_TIPO_EFA TE, " +
        "          DB_UNITA_MISURA UM " +
        "   WHERE  AE.ID_AZIENDA = ? " +
        "   AND    AE.ID_TIPO_EFA = TE.ID_TIPO_EFA  " +
        "   AND    AE.ID_DICHIARAZIONE_CONSISTENZA IS NULL  " +
        "   AND    AE.DATA_FINE_VALIDITA IS NULL " +
        "   AND    TE.ID_UNITA_MISURA = UM.ID_UNITA_MISURA (+) " +
        "   UNION " +
        "   SELECT TE.ID_TIPO_EFA," +
        "          TE.DESCRIZIONE_TIPO_EFA, " +
        "          TE.ID_UNITA_MISURA, " +
        "          UM.DESCRIZIONE AS DESC_UNITA_MISURA, " +
        "          0 AS VALORE_ORIGINALE, " +
        "          0 AS VALORE_DOPO_CONVERSIONE, " +
        "          0 AS VALORE_DOPO_PONDERAZIONE, " +
        "          (CASE "+
        "              WHEN EXISTS (SELECT *  " +
        "                           FROM DB_TIPO_EFA TE1 " +
        "                           WHERE TE1.ID_TIPO_EFA_PADRE = TE.ID_TIPO_EFA " +
        "                           AND   TE1.DATA_FINE_VALIDITA IS NULL) " +
        "                    THEN  'S' "+
        "           END) AS PADRE_EFA, " +
        "          TE.ORDINAMENTO, " +
        "          TE.DICHIARABILE "+
        "   FROM   DB_TIPO_EFA TE, " +
        "          DB_UNITA_MISURA UM " +
        "   WHERE  TE.DATA_FINE_VALIDITA IS NULL " +
        "   AND    TE.ID_UNITA_MISURA = UM.ID_UNITA_MISURA (+) ) " +
        "GROUP BY ID_TIPO_EFA, " +
        "         DESCRIZIONE_TIPO_EFA, " +
        "         ID_UNITA_MISURA, " +
        "         DESC_UNITA_MISURA, " +
        "         PADRE_EFA, " +
        "         ORDINAMENTO, " +
        "         DICHIARABILE " +
        "ORDER BY ORDINAMENTO ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[UtilizzoParticellaGaaDAO::riepilogoTipoEfaPianoLavorazione] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idAzienda);
      
      
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      { 
        if(vUtilizzo == null)
        {
          vUtilizzo = new Vector<UtilizzoParticellaVO>();
        }
        
        utilizzoParticellaVO = new UtilizzoParticellaVO();
        utilizzoParticellaVO.setIdTipoEfa(rs.getLong("ID_TIPO_EFA"));
        utilizzoParticellaVO.setDescTipoEfaEfa(rs.getString("DESCRIZIONE_TIPO_EFA"));        
        utilizzoParticellaVO.setDescUnitaMisuraEfa(rs.getString("DESC_UNITA_MISURA"));
        utilizzoParticellaVO.setValoreOriginale(rs.getBigDecimal("VALORE_ORIGINALE"));
        utilizzoParticellaVO.setValoreDopoConversione(rs.getBigDecimal("VALORE_DOPO_CONVERSIONE"));
        utilizzoParticellaVO.setValoreDopoPonderazione(rs.getBigDecimal("VALORE_DOPO_PONDERAZIONE"));
        utilizzoParticellaVO.setPadreEfa(rs.getString("PADRE_EFA"));
        utilizzoParticellaVO.setDichiarabileEfa(rs.getString("DICHIARABILE"));
        
        vUtilizzo.add(utilizzoParticellaVO);
      }
      
      return vUtilizzo;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("utilizzoParticellaVO", utilizzoParticellaVO), 
          new Variabile("vUtilizzo", vUtilizzo) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[UtilizzoParticellaGaaDAO::riepilogoTipoEfaPianoLavorazione] ", t,
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
          "[UtilizzoParticellaGaaDAO::riepilogoTipoEfaPianoLavorazione] END.");
    }
  }
  
  public Vector<UtilizzoParticellaVO> riepilogoTipoEfaDichiarazione(long idDichiarazioneConsistenza) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<UtilizzoParticellaVO> vUtilizzo = null;
    UtilizzoParticellaVO utilizzoParticellaVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[UtilizzoParticellaGaaDAO::riepilogoTipoEfaDichiarazione] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT ID_TIPO_EFA," +
        "       DESCRIZIONE_TIPO_EFA, " +
        "       ID_UNITA_MISURA, " +
        "       DESC_UNITA_MISURA, " +
        "       SUM(VALORE_ORIGINALE) AS VALORE_ORIGINALE, " +
        "       SUM(VALORE_DOPO_CONVERSIONE) AS VALORE_DOPO_CONVERSIONE, " +
        "       SUM(VALORE_DOPO_PONDERAZIONE) AS VALORE_DOPO_PONDERAZIONE, " +
        "       PADRE_EFA, " +
        "       ORDINAMENTO " +
        "FROM " +
        "   (SELECT TE.ID_TIPO_EFA," +
        "           TE.DESCRIZIONE_TIPO_EFA, " +
        "           TE.ID_UNITA_MISURA, " +
        "           UM.DESCRIZIONE AS DESC_UNITA_MISURA, " +
        "           AE.VALORE_ORIGINALE, " +
        "           AE.VALORE_DOPO_CONVERSIONE, " +
        "           AE.VALORE_DOPO_PONDERAZIONE, " +
        "           (CASE "+
        "               WHEN EXISTS (SELECT *  " +
        "                            FROM DB_TIPO_EFA TE1 " +
        "                            WHERE TE1.ID_TIPO_EFA_PADRE = TE.ID_TIPO_EFA" +
        "                            AND   TE1.DATA_INIZIO_VALIDITA <= DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "                            AND   NVL(TE1.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > DC.DATA_INSERIMENTO_DICHIARAZIONE ) " +
        "                     THEN  'S' "+
        "            END) AS PADRE_EFA, " +
        "           TE.ORDINAMENTO "+
        "    FROM   DB_AZIENDA_EFA AE, " +
        "           DB_TIPO_EFA TE, " +
        "           DB_UNITA_MISURA UM, " +
        "           DB_DICHIARAZIONE_CONSISTENZA DC " +
        "    WHERE  AE.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "    AND    AE.ID_DICHIARAZIONE_CONSISTENZA = DC.ID_DICHIARAZIONE_CONSISTENZA " +
        "    AND    AE.ID_TIPO_EFA = TE.ID_TIPO_EFA  " +
        "    AND    TE.ID_UNITA_MISURA = UM.ID_UNITA_MISURA (+) " +
        "    UNION " +
        "    SELECT TE.ID_TIPO_EFA," +
        "           TE.DESCRIZIONE_TIPO_EFA, " +
        "           TE.ID_UNITA_MISURA, " +
        "           UM.DESCRIZIONE AS DESC_UNITA_MISURA, " +
        "           0, " +
        "           0, " +
        "           0, " +
        "           (CASE "+
        "               WHEN EXISTS (SELECT *  " +
        "                            FROM DB_TIPO_EFA TE1 " +
        "                            WHERE TE1.ID_TIPO_EFA_PADRE = TE.ID_TIPO_EFA" +
        "                            AND   TE1.DATA_INIZIO_VALIDITA <= DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "                            AND   NVL(TE1.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > DC.DATA_INSERIMENTO_DICHIARAZIONE ) " +
        "                     THEN  'S' "+
        "            END) AS PADRE_EFA, "+
        "           TE.ORDINAMENTO "+
        "    FROM   DB_TIPO_EFA TE, " +
        "           DB_UNITA_MISURA UM, " +
        "           DB_DICHIARAZIONE_CONSISTENZA DC " +
        "    WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "    AND    TE.DATA_INIZIO_VALIDITA <= DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "    AND    NVL(TE.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "    AND    TE.ID_UNITA_MISURA = UM.ID_UNITA_MISURA (+) ) " +
        "GROUP BY ID_TIPO_EFA, " +
        "         DESCRIZIONE_TIPO_EFA, " +
        "         ID_UNITA_MISURA, " +
        "         DESC_UNITA_MISURA, " +
        "         PADRE_EFA, " +
        "         ORDINAMENTO " +
        "ORDER BY ORDINAMENTO ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[UtilizzoParticellaGaaDAO::riepilogoTipoEfaDichiarazione] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idDichiarazioneConsistenza);
      stmt.setLong(++indice, idDichiarazioneConsistenza);
      
      
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      { 
        if(vUtilizzo == null)
        {
          vUtilizzo = new Vector<UtilizzoParticellaVO>();
        }
        
        utilizzoParticellaVO = new UtilizzoParticellaVO();
        utilizzoParticellaVO.setIdTipoEfa(rs.getLong("ID_TIPO_EFA"));
        utilizzoParticellaVO.setDescTipoEfaEfa(rs.getString("DESCRIZIONE_TIPO_EFA"));        
        utilizzoParticellaVO.setDescUnitaMisuraEfa(rs.getString("DESC_UNITA_MISURA"));
        utilizzoParticellaVO.setValoreOriginale(rs.getBigDecimal("VALORE_ORIGINALE"));
        utilizzoParticellaVO.setValoreDopoConversione(rs.getBigDecimal("VALORE_DOPO_CONVERSIONE"));
        utilizzoParticellaVO.setValoreDopoPonderazione(rs.getBigDecimal("VALORE_DOPO_PONDERAZIONE"));
        utilizzoParticellaVO.setPadreEfa(rs.getString("PADRE_EFA"));
        
        vUtilizzo.add(utilizzoParticellaVO);
      }
      
      return vUtilizzo;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("utilizzoParticellaVO", utilizzoParticellaVO), 
          new Variabile("vUtilizzo", vUtilizzo) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[UtilizzoParticellaGaaDAO::riepilogoTipoEfaDichiarazione] ", t,
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
          "[UtilizzoParticellaGaaDAO::riepilogoTipoEfaDichiarazione] END.");
    }
  }
  
  
  public Vector<UtilizzoParticellaVO> riepilogoTipoGreeningPianoLavorazione(
      long idAzienda) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<UtilizzoParticellaVO> vUtilizzo = null;
    UtilizzoParticellaVO utilizzoParticellaVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[UtilizzoParticellaGaaDAO::riepilogoTipoGreeningPianoLavorazione] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT  RCM.CODICE_DIVERSIFICAZIONE, " +
        "        TS.DESCRIZIONE AS DESC_TIPO_SPECIE, " + 
        "        TF.DESCRIZIONE AS DESC_TIPO_FAMIGLIA, " +
        "        TG.DESCRIZIONE AS DESC_TIPO_GENERE, " +
        "        TG.TIPO_DIVERSIFICAZIONE, " +
        "        TPS.DESCRIZIONE AS DESC_PERIOD_SEMINA, " +     
        "        NVL(SUM(UP.SUPERFICIE_UTILIZZATA),0) AS SUP_UTILIZZATA " +   
        "FROM    DB_CONDUZIONE_PARTICELLA CP, " +
        "        DB_UTE U, " +
        "        DB_UTILIZZO_PARTICELLA UP, " +   
        "        DB_TIPO_SPECIE TS, " + 
        "        DB_TIPO_FAMIGLIA TF, " +
        "        DB_TIPO_GENERE TG, " +
        "        DB_TIPO_PERIODO_SEMINA TPS, " +
        "        DB_R_CATALOGO_MATRICE RCM " +
        "WHERE   CP.ID_UTE = U.ID_UTE " +
        "AND     U.ID_AZIENDA = ? " +
        "AND     UP.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA " +
        "AND     UP.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +
        "AND     UP.ID_TIPO_PERIODO_SEMINA = TPS.ID_TIPO_PERIODO_SEMINA " + 
        "AND     U.DATA_FINE_ATTIVITA IS NULL " +
        "AND     CP.ID_TITOLO_POSSESSO <> 5 " +
        "AND     CP.DATA_FINE_CONDUZIONE IS NULL " + 
        "AND     RCM.ID_TIPO_SPECIE = TS.ID_TIPO_SPECIE " + 
        "AND     RCM.SOGGETTO_A_DIVERSIFICAZIONE = 'S' " +
        "AND     TS.ID_TIPO_GENERE = TG.ID_TIPO_GENERE " +
        "AND     TG.ID_TIPO_FAMIGLIA = TF.ID_TIPO_FAMIGLIA " + 
        "GROUP BY RCM.CODICE_DIVERSIFICAZIONE, " +
        "         TS.DESCRIZIONE, " +
        "         TF.DESCRIZIONE, " +
        "         TG.DESCRIZIONE, " +
        "         TG.TIPO_DIVERSIFICAZIONE, " + 
        "         TPS.DESCRIZIONE " +
        "ORDER BY TF.DESCRIZIONE, " +
        "         TG.DESCRIZIONE, " +
        "         TS.DESCRIZIONE, " +
        "         TPS.DESCRIZIONE");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[UtilizzoParticellaGaaDAO::riepilogoTipoGreeningPianoLavorazione] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idAzienda);
      
      
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      { 
        if(vUtilizzo == null)
        {
          vUtilizzo = new Vector<UtilizzoParticellaVO>();
        }
        
        utilizzoParticellaVO = new UtilizzoParticellaVO();
        utilizzoParticellaVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
        TipoPeriodoSeminaVO tipoPeriodoSemina = new TipoPeriodoSeminaVO();
        tipoPeriodoSemina.setDescrizione(rs.getString("DESC_PERIOD_SEMINA"));
        utilizzoParticellaVO.setTipoPeriodoSemina(tipoPeriodoSemina);
        
        TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
        tipoUtilizzoVO.setCodiceDiversificazione(rs.getString("CODICE_DIVERSIFICAZIONE"));
        tipoUtilizzoVO.setDescrizioneFamiglia(rs.getString("DESC_TIPO_FAMIGLIA"));
        tipoUtilizzoVO.setDescrizioneGenere(rs.getString("DESC_TIPO_GENERE"));
        tipoUtilizzoVO.setDescrizioneSpecie(rs.getString("DESC_TIPO_SPECIE"));
        tipoUtilizzoVO.setTipoDiversificazione(rs.getString("TIPO_DIVERSIFICAZIONE"));
        
        utilizzoParticellaVO.setTipoUtilizzoVO(tipoUtilizzoVO);
        
        vUtilizzo.add(utilizzoParticellaVO);
      }
      
      return vUtilizzo;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("utilizzoParticellaVO", utilizzoParticellaVO), 
          new Variabile("vUtilizzo", vUtilizzo) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[UtilizzoParticellaGaaDAO::riepilogoTipoGreeningPianoLavorazione] ", t,
          query, variabili, parametri);
      
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
          "[UtilizzoParticellaGaaDAO::riepilogoTipoGreeningPianoLavorazione] END.");
    }
  }
  
  public Vector<UtilizzoParticellaVO> riepilogoTipoGreeningDichiarazione(long idDichiarazioneConsistenza) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<UtilizzoParticellaVO> vUtilizzo = null;
    UtilizzoParticellaVO utilizzoParticellaVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[UtilizzoParticellaGaaDAO::riepilogoTipoGreeningDichiarazione] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT  RCM.CODICE_DIVERSIFICAZIONE, " +
        "        TS.DESCRIZIONE AS DESC_TIPO_SPECIE, " +
        "        TF.DESCRIZIONE AS DESC_TIPO_FAMIGLIA, " +
        "        TG.DESCRIZIONE AS DESC_TIPO_GENERE, " +
        "        TG.TIPO_DIVERSIFICAZIONE, " +
        "        TPS.DESCRIZIONE AS DESC_PERIOD_SEMINA, " +  
        "        SUM(UD.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " + 
        "FROM    DB_R_CATALOGO_MATRICE RCM, " + 
        "        DB_UTILIZZO_DICHIARATO UD, " + 
        "        DB_DICHIARAZIONE_CONSISTENZA DC, " + 
        "        DB_CONDUZIONE_DICHIARATA CD, " +
        "        DB_TIPO_SPECIE TS, " +
        "        DB_TIPO_FAMIGLIA TF, " +
        "        DB_TIPO_GENERE TG, " +
        "        DB_TIPO_PERIODO_SEMINA TPS " + 
        "WHERE   DC.ID_DICHIARAZIONE_CONSISTENZA = ? " + 
        "AND     DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " + 
        "AND     CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA " + 
        "AND     UD.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +
        "AND     UD.ID_TIPO_PERIODO_SEMINA = TPS.ID_TIPO_PERIODO_SEMINA " +
        "AND     RCM.ID_TIPO_SPECIE = TS.ID_TIPO_SPECIE " +
        "AND     RCM.SOGGETTO_A_DIVERSIFICAZIONE = 'S' " +
        "AND     TS.ID_TIPO_GENERE = TG.ID_TIPO_GENERE " +
        "AND     TG.ID_TIPO_FAMIGLIA = TF.ID_TIPO_FAMIGLIA " +
        "GROUP BY RCM.CODICE_DIVERSIFICAZIONE, " +
        "         TS.DESCRIZIONE, " +
        "         TF.DESCRIZIONE, " +
        "         TG.DESCRIZIONE, " +
        "         TG.TIPO_DIVERSIFICAZIONE, " +
        "         TPS.DESCRIZIONE " +    
        "ORDER BY TF.DESCRIZIONE, " +
        "         TG.DESCRIZIONE, " +
        "         TS.DESCRIZIONE, " +
        "         TPS.DESCRIZIONE ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[UtilizzoParticellaGaaDAO::riepilogoTipoGreeningDichiarazione] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idDichiarazioneConsistenza);
      
      
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      { 
        if(vUtilizzo == null)
        {
          vUtilizzo = new Vector<UtilizzoParticellaVO>();
        }
        
        utilizzoParticellaVO = new UtilizzoParticellaVO();
        utilizzoParticellaVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
        
        TipoPeriodoSeminaVO tipoPeriodoSemina = new TipoPeriodoSeminaVO();
        tipoPeriodoSemina.setDescrizione(rs.getString("DESC_PERIOD_SEMINA"));
        utilizzoParticellaVO.setTipoPeriodoSemina(tipoPeriodoSemina);
        
        
        
        TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
        tipoUtilizzoVO.setCodiceDiversificazione(rs.getString("CODICE_DIVERSIFICAZIONE"));
        tipoUtilizzoVO.setDescrizioneFamiglia(rs.getString("DESC_TIPO_FAMIGLIA"));
        tipoUtilizzoVO.setDescrizioneGenere(rs.getString("DESC_TIPO_GENERE"));
        tipoUtilizzoVO.setDescrizioneSpecie(rs.getString("DESC_TIPO_SPECIE"));
        tipoUtilizzoVO.setTipoDiversificazione(rs.getString("TIPO_DIVERSIFICAZIONE"));
        
        utilizzoParticellaVO.setTipoUtilizzoVO(tipoUtilizzoVO);
        
        vUtilizzo.add(utilizzoParticellaVO);
      }
      
      return vUtilizzo;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("utilizzoParticellaVO", utilizzoParticellaVO), 
          new Variabile("vUtilizzo", vUtilizzo) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[UtilizzoParticellaGaaDAO::riepilogoTipoGreeningDichiarazione] ", t,
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
          "[UtilizzoParticellaGaaDAO::riepilogoTipoGreeningDichiarazione] END.");
    }
  }
  
  
  public Vector<TipoEfaVO> getElencoTipoEfaForAzienda(long idAzienda) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoEfaVO> vTipoEfaVO = null;
    TipoEfaVO tipoEfaVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[UtilizzoParticellaGaaDAO::getElencoTipoEfaForAzienda] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT DISTINCT " +
        "       TE.ID_TIPO_EFA," +
        "       TE.DESCRIZIONE_TIPO_EFA, " +
        "       TE.ORDINAMENTO " +
        "FROM   DB_AZIENDA_EFA AE, " +
        "       DB_TIPO_EFA TE " +
        "WHERE  AE.ID_AZIENDA = ? " +
        "AND    AE.ID_TIPO_EFA = TE.ID_TIPO_EFA  " +
        "AND    TE.ID_UNITA_MISURA IS NOT NULL " +
        "ORDER BY TE.ORDINAMENTO ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[UtilizzoParticellaGaaDAO::getElencoTipoEfaForAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idAzienda);
      
      
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      { 
        if(vTipoEfaVO == null)
        {
          vTipoEfaVO = new Vector<TipoEfaVO>();
        }
        
        tipoEfaVO = new TipoEfaVO();
        tipoEfaVO.setIdTipoEfa(rs.getLong("ID_TIPO_EFA"));
        tipoEfaVO.setDescrizioneTipoEfa(rs.getString("DESCRIZIONE_TIPO_EFA"));        
        
        
        vTipoEfaVO.add(tipoEfaVO);
      }
      
      return vTipoEfaVO;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("tipoEfaVO", tipoEfaVO), 
          new Variabile("vTipoEfaVO", vTipoEfaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[UtilizzoParticellaGaaDAO::getElencoTipoEfaForAzienda] ", t,
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
          "[UtilizzoParticellaGaaDAO::getElencoTipoEfaForAzienda] END.");
    }
  }
  
  
  public Vector<TipoDestinazioneVO> getElencoTipoDestinazioneByMatrice(long idUtilizzo) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoDestinazioneVO> vTipoDestinazioneVO = null;
    TipoDestinazioneVO tipoDestinazioneVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[UtilizzoParticellaGaaDAO::getElencoTipoDestinazioneByMatrice] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT DISTINCT " +
        "       TD.ID_TIPO_DESTINAZIONE," +
        "       TD.DESCRIZIONE_DESTINAZIONE, " +
        "       TD.CODICE_DESTINAZIONE," +
        "       TD.ORDINAMENTO " +
        "FROM   DB_R_CATALOGO_MATRICE RCM, " +
        "       DB_TIPO_DESTINAZIONE TD " +
        "WHERE  RCM.ID_UTILIZZO = ? " +
        "AND    RCM.ID_TIPO_DESTINAZIONE = TD.ID_TIPO_DESTINAZIONE  " +
        "AND    RCM.DATA_FINE_VALIDITA IS NULL " +
        "ORDER BY TD.ORDINAMENTO, TD.DESCRIZIONE_DESTINAZIONE ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[UtilizzoParticellaGaaDAO::getElencoTipoDestinazioneByMatrice] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idUtilizzo);
      
      
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      { 
        if(vTipoDestinazioneVO == null)
        {
          vTipoDestinazioneVO = new Vector<TipoDestinazioneVO>();
        }
        
        tipoDestinazioneVO = new TipoDestinazioneVO();
        tipoDestinazioneVO.setIdTipoDestinazione(rs.getLong("ID_TIPO_DESTINAZIONE"));
        tipoDestinazioneVO.setCodiceDestinazione(rs.getString("CODICE_DESTINAZIONE"));
        tipoDestinazioneVO.setDescrizioneDestinazione(rs.getString("DESCRIZIONE_DESTINAZIONE"));        
        
        
        vTipoDestinazioneVO.add(tipoDestinazioneVO);
      }
      
      return vTipoDestinazioneVO;
      
    }
    catch (Exception t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("tipoDestinazioneVO", tipoDestinazioneVO), 
          new Variabile("vTipoDestinazioneVO", vTipoDestinazioneVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUtilizzo", idUtilizzo)};

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[UtilizzoParticellaGaaDAO::getElencoTipoDestinazioneByMatrice] ", t,
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
          "[UtilizzoParticellaGaaDAO::getElencoTipoDestinazioneByMatrice] END.");
    }
  }
  
  
  
  public Vector<TipoVarietaVO> getElencoTipoVarietaByMatrice(
      long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso, long idTipoQualitaUso) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoVarietaVO> vTipoVarietaVO = null;
    TipoVarietaVO tipoVarietaVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[UtilizzoParticellaGaaDAO::getElencoTipoVarietaByMatrice] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT DISTINCT " +
        "       TV.ID_VARIETA," +
        "       TV.DESCRIZIONE, " +
        "       TV.CODICE_VARIETA, " +
        "       RCM.ID_UTILIZZO " +
        "FROM   DB_R_CATALOGO_MATRICE RCM, " +
        "       DB_TIPO_VARIETA TV " +
        "WHERE  RCM.ID_UTILIZZO = ? " +
        "AND    RCM.ID_TIPO_DESTINAZIONE = ? " +
        "AND    RCM.ID_TIPO_DETTAGLIO_USO = ? " +
        "AND    RCM.ID_TIPO_QUALITA_USO = ? " +
        "AND    RCM.ID_VARIETA = TV.ID_VARIETA  " +
        "AND    RCM.DATA_FINE_VALIDITA IS NULL " +
        "ORDER BY TV.DESCRIZIONE ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[UtilizzoParticellaGaaDAO::getElencoTipoVarietaByMatrice] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idUtilizzo);
      stmt.setLong(++indice, idTipoDestinazione);
      stmt.setLong(++indice, idTipoDettaglioUso);
      stmt.setLong(++indice, idTipoQualitaUso);
      
      
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      { 
        if(vTipoVarietaVO == null)
        {
          vTipoVarietaVO = new Vector<TipoVarietaVO>();
        }
        
        tipoVarietaVO = new TipoVarietaVO();
        tipoVarietaVO.setIdVarieta(rs.getLong("ID_VARIETA"));
        tipoVarietaVO.setCodiceVarieta(rs.getString("CODICE_VARIETA"));
        tipoVarietaVO.setDescrizione(rs.getString("DESCRIZIONE"));        
        tipoVarietaVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
        
        vTipoVarietaVO.add(tipoVarietaVO);
      }
      
      return vTipoVarietaVO;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("tipoVarietaVO", tipoVarietaVO), 
          new Variabile("vTipoVarietaVO", vTipoVarietaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUtilizzo", idUtilizzo),
          new Parametro("idTipoDestinazione", idTipoDestinazione),
          new Parametro("idTipoDettaglioUso", idTipoDettaglioUso),
          new Parametro("idTipoQualitaUso", idTipoQualitaUso)};

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[UtilizzoParticellaGaaDAO::getElencoTipoVarietaByMatrice] ", t,
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
          "[UtilizzoParticellaGaaDAO::getElencoTipoVarietaByMatrice] END.");
    }
  }
  
  
  public Vector<TipoQualitaUsoVO> getElencoTipoQualitaUsoByMatrice(
      long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoQualitaUsoVO> vTipoQualitaUsoVO = null;
    TipoQualitaUsoVO tipoQualitaUsoVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[UtilizzoParticellaGaaDAO::getElencoTipoQualitaUsoByMatrice] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT DISTINCT " +
        "       TQ.ID_TIPO_QUALITA_USO," +
        "       TQ.DESCRIZIONE_QUALITA_USO, " +
        "       TQ.CODICE_QUALITA_USO," +
        "       TQ.ORDINAMENTO " +
        "FROM   DB_R_CATALOGO_MATRICE RCM, " +
        "       DB_TIPO_QUALITA_USO TQ " +
        "WHERE  RCM.ID_UTILIZZO = ? " +
        "AND    RCM.ID_TIPO_DESTINAZIONE = ? " +
        "AND    RCM.ID_TIPO_DETTAGLIO_USO = ? " +
        "AND    RCM.ID_TIPO_QUALITA_USO = TQ.ID_TIPO_QUALITA_USO  " +
        "AND    RCM.DATA_FINE_VALIDITA IS NULL " +
        "ORDER BY TQ.ORDINAMENTO, TQ.DESCRIZIONE_QUALITA_USO ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[UtilizzoParticellaGaaDAO::getElencoTipoQualitaUsoByMatrice] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idUtilizzo);
      stmt.setLong(++indice, idTipoDestinazione);
      stmt.setLong(++indice, idTipoDettaglioUso);
      
      
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      { 
        if(vTipoQualitaUsoVO == null)
        {
          vTipoQualitaUsoVO = new Vector<TipoQualitaUsoVO>();
        }
        
        tipoQualitaUsoVO = new TipoQualitaUsoVO();
        tipoQualitaUsoVO.setIdTipoQualitaUso(rs.getLong("ID_TIPO_QUALITA_USO"));
        tipoQualitaUsoVO.setCodiceQualitaUso(rs.getString("CODICE_QUALITA_USO"));
        tipoQualitaUsoVO.setDescrizioneQualitaUso(rs.getString("DESCRIZIONE_QUALITA_USO"));        
        
        
        vTipoQualitaUsoVO.add(tipoQualitaUsoVO);
      }
      
      return vTipoQualitaUsoVO;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("tipoQualitaUsoVO", tipoQualitaUsoVO), 
          new Variabile("vTipoQualitaUsoVO", vTipoQualitaUsoVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUtilizzo", idUtilizzo),
          new Parametro("idTipoDestinazione", idTipoDestinazione),
          new Parametro("idTipoDettaglioUso", idTipoDettaglioUso) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[UtilizzoParticellaGaaDAO::getElencoTipoQualitaUsoByMatrice] ", t,
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
          "[UtilizzoParticellaGaaDAO::getElencoTipoQualitaUsoByMatrice] END.");
    }
  }
  
  
  public CatalogoMatriceVO getCatalogoMatriceFromMatrice(long idUtilizzo, long idVarieta,
      long idTipoDestinazione, long idTipoDettaglioUso, long idTipoQualitaUso) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    CatalogoMatriceVO catalogoMatriceVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[UtilizzoParticellaGaaDAO::getCatalogoMatriceFromMatrice] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT RCM.ID_CATALOGO_MATRICE, " +
        "       RCM.ID_VARIETA, " +
        "       RCM.ID_TIPO_DETTAGLIO_USO, " +
        "       RCM.FLAG_NON_AMMISSIBILE_EFA, " +
        "       RCM.FLAG_PRATO_PERMANENTE, " +
        "       RCM.FLAG_COLTURA_SOMMERSA, " +
        "       RCM.FLAG_RIPOSO, " +
        "       RCM.FLAG_PRATO_FORAGGERA, " +
        "       RCM.FLAG_LEGUMINOSA, " +
        "       RCM.FLAG_PROTEAGINOSA, " +
        "       RCM.FLAG_BIOLOGICO, " +
        "       RCM.FLAG_SEMINATIVO," +
        "       RCM.PERMANENTE, " +
        "       RCM.COEFFICIENTE_RIDUZIONE," +
        "       RCM.FLAG_UNAR_MODIFICABILE " + 
        "FROM   DB_R_CATALOGO_MATRICE RCM " +
        "WHERE  RCM.ID_UTILIZZO = ? " +
        "AND    RCM.ID_VARIETA = ? " +
        "AND    RCM.ID_TIPO_DESTINAZIONE = ? " +
        "AND    RCM.ID_TIPO_DETTAGLIO_USO = ? " +
        "AND    RCM.ID_TIPO_QUALITA_USO = ?  ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[UtilizzoParticellaGaaDAO::getCatalogoMatriceFromMatrice] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idUtilizzo);
      stmt.setLong(++indice, idVarieta);
      stmt.setLong(++indice, idTipoDestinazione);
      stmt.setLong(++indice, idTipoDettaglioUso);
      stmt.setLong(++indice, idTipoQualitaUso);
      
      
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      { 
        catalogoMatriceVO = new CatalogoMatriceVO();
        catalogoMatriceVO.setIdCatalogoMatrice(rs.getLong("ID_CATALOGO_MATRICE"));
        catalogoMatriceVO.setIdTipoDettaglioUso(rs.getLong("ID_TIPO_DETTAGLIO_USO"));
        catalogoMatriceVO.setIdVarieta(rs.getLong("ID_VARIETA"));
        catalogoMatriceVO.setFlagNonAmmissibileEfa(rs.getString("FLAG_NON_AMMISSIBILE_EFA"));
        catalogoMatriceVO.setFlagPratoPermanente(rs.getString("FLAG_PRATO_PERMANENTE"));
        catalogoMatriceVO.setFlagColturaSommersa(rs.getString("FLAG_COLTURA_SOMMERSA"));
        catalogoMatriceVO.setFlagRiposo(rs.getString("FLAG_RIPOSO"));
        catalogoMatriceVO.setFlagPratoForaggera(rs.getString("FLAG_PRATO_FORAGGERA"));
        catalogoMatriceVO.setFlagLeguminosa(rs.getString("FLAG_LEGUMINOSA"));
        catalogoMatriceVO.setFlagProteaginosa(rs.getString("FLAG_PROTEAGINOSA"));
        catalogoMatriceVO.setFlagBiologico(rs.getString("FLAG_BIOLOGICO"));
        catalogoMatriceVO.setFlagSeminativo(rs.getString("FLAG_SEMINATIVO"));
        catalogoMatriceVO.setPermanente(rs.getString("PERMANENTE"));
        catalogoMatriceVO.setCoefficienteRiduzione(rs.getBigDecimal("COEFFICIENTE_RIDUZIONE"));
        catalogoMatriceVO.setFlagUnarModificabile(rs.getString("FLAG_UNAR_MODIFICABILE"));
      }
      
      return catalogoMatriceVO;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("catalogoMatriceVO", catalogoMatriceVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUtilizzo", idUtilizzo),
          new Parametro("idVarieta", idVarieta),
          new Parametro("idTipoDestinazione", idTipoDestinazione),
          new Parametro("idTipoDettaglioUso", idTipoDettaglioUso),
          new Parametro("idTipoQualitaUso", idTipoQualitaUso) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[UtilizzoParticellaGaaDAO::getCatalogoMatriceFromMatrice] ", t,
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
          "[UtilizzoParticellaGaaDAO::getCatalogoMatriceFromMatrice] END.");
    }
  }
  
  public CatalogoMatriceVO getCatalogoMatriceFromPrimariKey(long idCatalogoMatrice) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    CatalogoMatriceVO catalogoMatriceVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[UtilizzoParticellaGaaDAO::getCatalogoMatriceFromPrimariKey] BEGIN.");

      queryBuf = new StringBuffer();

      // CONCATENAZIONE/CREAZIONE QUERY BEGIN. 
      queryBuf.append(
        "SELECT RCM.ID_CATALOGO_MATRICE, " +
        "       RCM.ID_UTILIZZO, " +
        "       RCM.ID_TIPO_DESTINAZIONE, " +
        "       RCM.ID_TIPO_DETTAGLIO_USO, " +
        "       RCM.ID_TIPO_QUALITA_USO, " +
        "       RCM.ID_VARIETA, " +       
        "       RCM.FLAG_NON_AMMISSIBILE_EFA, " +
        "       RCM.FLAG_PRATO_PERMANENTE, " +
        "       RCM.FLAG_COLTURA_SOMMERSA, " +
        "       RCM.FLAG_RIPOSO, " +
        "       RCM.FLAG_PRATO_FORAGGERA, " +
        "       RCM.FLAG_LEGUMINOSA, " +
        "       RCM.FLAG_PROTEAGINOSA, " +
        "       RCM.FLAG_BIOLOGICO, " +
        "       RCM.FLAG_SEMINATIVO," +
        "       RCM.PERMANENTE, " +
        "       RCM.COEFFICIENTE_RIDUZIONE, " +
        "       RCM.DATA_FINE_VALIDITA, " +
        "       RCM.FLAG_UNAR_MODIFICABILE " + 
        "FROM   DB_R_CATALOGO_MATRICE RCM " +
        "WHERE  RCM.ID_CATALOGO_MATRICE = ? ");

      
      // CONCATENAZIONE/CREAZIONE QUERY END. 

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[UtilizzoParticellaGaaDAO::getCatalogoMatriceFromPrimariKey] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idCatalogoMatrice);
      
      
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      { 
        catalogoMatriceVO = new CatalogoMatriceVO();
        catalogoMatriceVO.setIdCatalogoMatrice(rs.getLong("ID_CATALOGO_MATRICE"));
        catalogoMatriceVO.setIdUtilizzo(rs.getLong("ID_UTILIZZO"));
        catalogoMatriceVO.setIdTipoDestinazione(rs.getLong("ID_TIPO_DESTINAZIONE"));
        catalogoMatriceVO.setIdTipoDettaglioUso(rs.getLong("ID_TIPO_DETTAGLIO_USO"));
        catalogoMatriceVO.setIdTipoQualitaUso(rs.getLong("ID_TIPO_QUALITA_USO"));
        catalogoMatriceVO.setIdVarieta(rs.getLong("ID_VARIETA"));
        catalogoMatriceVO.setFlagNonAmmissibileEfa(rs.getString("FLAG_NON_AMMISSIBILE_EFA"));
        catalogoMatriceVO.setFlagPratoPermanente(rs.getString("FLAG_PRATO_PERMANENTE"));
        catalogoMatriceVO.setFlagColturaSommersa(rs.getString("FLAG_COLTURA_SOMMERSA"));
        catalogoMatriceVO.setFlagRiposo(rs.getString("FLAG_RIPOSO"));
        catalogoMatriceVO.setFlagPratoForaggera(rs.getString("FLAG_PRATO_FORAGGERA"));
        catalogoMatriceVO.setFlagLeguminosa(rs.getString("FLAG_LEGUMINOSA"));
        catalogoMatriceVO.setFlagProteaginosa(rs.getString("FLAG_PROTEAGINOSA"));
        catalogoMatriceVO.setFlagBiologico(rs.getString("FLAG_BIOLOGICO"));
        catalogoMatriceVO.setFlagSeminativo(rs.getString("FLAG_SEMINATIVO"));
        catalogoMatriceVO.setPermanente(rs.getString("PERMANENTE"));
        catalogoMatriceVO.setCoefficienteRiduzione(rs.getBigDecimal("COEFFICIENTE_RIDUZIONE"));
        catalogoMatriceVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        catalogoMatriceVO.setFlagUnarModificabile(rs.getString("FLAG_UNAR_MODIFICABILE"));
      }
      
      return catalogoMatriceVO;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("catalogoMatriceVO", catalogoMatriceVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idCatalogoMatrice", idCatalogoMatrice) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[UtilizzoParticellaGaaDAO::getCatalogoMatriceFromPrimariKey] ", t,
          query, variabili, parametri);
      
      //  Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       // informazione sui codici di errore utilizzati vedere il javadoc di
       // BaseDAO.convertIntoGaaservInternalException()
       
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      
      //  Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
      //  ignora ogni eventuale eccezione)
       
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this,
          "[UtilizzoParticellaGaaDAO::getCatalogoMatriceFromPrimariKey] END.");
    }
  }
  
  
  public CatalogoMatriceSeminaVO getCatalogoMatriceSeminaDefault(long idCatalogoMatrice) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    CatalogoMatriceSeminaVO catalogoMatriceSeminaVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[UtilizzoParticellaGaaDAO::getCatalogoMatriceSeminaDefault] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT RCMS.ID_TIPO_PERIODO_SEMINA, " +
        "       RCMS.ID_CATALOGO_MATRICE, " +
        "       RCMS.INIZIO_DESTINAZIONE_DEFAULT, " +
        "       RCMS.FINE_DESTINAZIONE_DEFAULT, " +
        "       RCMS.ANNO_DECODIFICA_PRE_DATA, " +
        "       RCMS.ANNO_DECODIFICA_POST_DATA " +
        "FROM   DB_R_CATALOGO_MATRICE_SEMINA RCMS " +
        "WHERE  RCMS.ID_CATALOGO_MATRICE = ? " +
        "AND    RCMS.FLAG_DEFAULT = 'S' " +
        "AND    RCMS.DATA_FINE_VALIDITA IS NULL ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[UtilizzoParticellaGaaDAO::getCatalogoMatriceSeminaDefault] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idCatalogoMatrice);
      
      
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      { 
        catalogoMatriceSeminaVO = new CatalogoMatriceSeminaVO();
        catalogoMatriceSeminaVO.setIdTipoPeriodoSemina(new Long(rs.getLong("ID_TIPO_PERIODO_SEMINA")));
        catalogoMatriceSeminaVO.setIdCatalogoMatrice(new Long(rs.getLong("ID_CATALOGO_MATRICE")));
        catalogoMatriceSeminaVO.setInizioDestinazioneDefault(rs.getString("INIZIO_DESTINAZIONE_DEFAULT"));
        catalogoMatriceSeminaVO.setFineDestinazioneDefault(rs.getString("FINE_DESTINAZIONE_DEFAULT"));
        catalogoMatriceSeminaVO.setAnnoDecodificaPostData(rs.getInt("ANNO_DECODIFICA_POST_DATA"));
        catalogoMatriceSeminaVO.setAnnoDecodificaPreData(rs.getInt("ANNO_DECODIFICA_PRE_DATA"));
      }
      
      return catalogoMatriceSeminaVO;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("catalogoMatriceSeminaVO", catalogoMatriceSeminaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idCatalogoMatrice", idCatalogoMatrice) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[UtilizzoParticellaGaaDAO::getCatalogoMatriceSeminaDefault] ", t,
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
          "[UtilizzoParticellaGaaDAO::getCatalogoMatriceSeminaDefault] END.");
    }
  }
  
  
  public CatalogoMatriceSeminaVO getCatalogoMatriceSeminaByIdTipoPeriodo(long idCatalogoMatrice, long idTipoPeriodoSemina) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    CatalogoMatriceSeminaVO catalogoMatriceSeminaVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[UtilizzoParticellaGaaDAO::getCatalogoMatriceSeminaByIdTipoPeriodo] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT RCMS.ID_TIPO_PERIODO_SEMINA, " +
        "       RCMS.ID_CATALOGO_MATRICE, " +
        "       RCMS.INIZIO_DESTINAZIONE_DEFAULT, " +
        "       RCMS.FINE_DESTINAZIONE_DEFAULT, " +
        "       RCMS.ANNO_DECODIFICA_PRE_DATA, " +
        "       RCMS.ANNO_DECODIFICA_POST_DATA " +
        "FROM   DB_R_CATALOGO_MATRICE_SEMINA RCMS " +
        "WHERE  RCMS.ID_CATALOGO_MATRICE = ? " +
        "AND    RCMS.ID_TIPO_PERIODO_SEMINA = ? " +
        "AND    RCMS.DATA_FINE_VALIDITA IS NULL ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[UtilizzoParticellaGaaDAO::getCatalogoMatriceSeminaByIdTipoPeriodo] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idCatalogoMatrice);
      stmt.setLong(++indice, idTipoPeriodoSemina);
      
      
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      { 
        catalogoMatriceSeminaVO = new CatalogoMatriceSeminaVO();
        catalogoMatriceSeminaVO.setIdTipoPeriodoSemina(new Long(rs.getLong("ID_TIPO_PERIODO_SEMINA")));
        catalogoMatriceSeminaVO.setIdCatalogoMatrice(new Long(rs.getLong("ID_CATALOGO_MATRICE")));
        catalogoMatriceSeminaVO.setInizioDestinazioneDefault(rs.getString("INIZIO_DESTINAZIONE_DEFAULT"));
        catalogoMatriceSeminaVO.setFineDestinazioneDefault(rs.getString("FINE_DESTINAZIONE_DEFAULT"));
        catalogoMatriceSeminaVO.setAnnoDecodificaPostData(rs.getInt("ANNO_DECODIFICA_POST_DATA"));
        catalogoMatriceSeminaVO.setAnnoDecodificaPreData(rs.getInt("ANNO_DECODIFICA_PRE_DATA"));
      }
      
      return catalogoMatriceSeminaVO;
      
    }
    catch (Exception t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("catalogoMatriceSeminaVO", catalogoMatriceSeminaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idCatalogoMatrice", idCatalogoMatrice),
        new Parametro("idTipoPeriodoSemina", idTipoPeriodoSemina)};

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[UtilizzoParticellaGaaDAO::getCatalogoMatriceSeminaByIdTipoPeriodo] ", t,
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
          "[UtilizzoParticellaGaaDAO::getCatalogoMatriceSeminaByIdTipoPeriodo] END.");
    }
  }
  
  
  public Vector<Long> getListIdPraticaMantenimentoPlSql(
      long idCatalogoMatrice, String flagDefault) throws DataAccessException
  {
    String query = null;
    Vector<Long> vIdPraticaMantenimento = null;
    Connection conn = null;
    CallableStatement cs = null;
    StringBuffer queryBuf = null;
    
    
    
    final String typeTableName = "SMRGAA.TYPVETRECPRATMANT";
    
    
    try
    {
      SolmrLogger
          .debug(this,
              "[UtilizzoParticellaGaaDAO::getListIdPraticaMantenimentoPlSql] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      /*
       * FUNCTION getListIdPraticaMantenimento(pIdCatalogoMatrice  DB_R_CATALOGO_MATRICE.ID_CATALOGO_MATRICE%TYPE,
                                      pFlagDefault        VARCHAR2 DEFAULT NULL) RETURN TYPVETRECPRATMANT IS
       */
      
      
      queryBuf.append("{ ? = call Pck_Smrgaa_Libreria.getListIdPraticaMantenimento(?,?) }");
        

      /* CONCATENAZIONE/CREAZIONE QUERY END. */
          
      // Get a description of your type (Oracle specific)

      conn = getDatasource().getConnection();
      
      
      query = queryBuf.toString();
      // Parametri
      StringBuffer parametri = new StringBuffer();
      parametri.append("idCatalogoMatrice=").append(idCatalogoMatrice)
        .append(" flagDefault=").append(flagDefault);

      // Query
        SolmrLogger.debug(this,
            "[UtilizzoParticellaGaaDAO::getListIdPraticaMantenimentoPlSql] Parametri="
                + parametri);
        SolmrLogger.debug(this,
            "[UtilizzoParticellaGaaDAO::getListIdPraticaMantenimentoPlSql] Query="
                + query);
      
      cs = conn.prepareCall(query);
      
      
      
      cs.registerOutParameter(1, Types.ARRAY, typeTableName);
      cs.setLong(2, idCatalogoMatrice);
      cs.setString(3, flagDefault);
      
      cs.executeUpdate();
      
      
      
      Object[] data = (Object[]) ((Array) cs.getObject(1)).getArray();
      for(Object tmp : data) 
      {
        if(vIdPraticaMantenimento == null)
          vIdPraticaMantenimento = new Vector<Long>();
        
        Struct row = (Struct) tmp;
        Object[] record = row.getAttributes();
                
        vIdPraticaMantenimento.add(new Long(((BigDecimal)record[0]).longValue()));        
        
      }     
      
      return vIdPraticaMantenimento;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), 
          new Variabile("vIdPraticaMantenimento", vIdPraticaMantenimento)
      };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {
        new Parametro("idCatalogoMatrice", idCatalogoMatrice),
        new Parametro("flagDefault", flagDefault)
      };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[UtilizzoParticellaGaaDAO::getListIdPraticaMantenimentoPlSql] ", t,
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
      closePlSql(cs, conn);

      // Fine metodo
      SolmrLogger.debug(this,
          "[UtilizzoParticellaGaaDAO::getListIdPraticaMantenimentoPlSql] END.");
    }
  }
  
  
  public Vector<TipoSeminaVO> getElencoTipoSemina() 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    TipoSeminaVO tipoSeminaVO = null;
    Vector<TipoSeminaVO> vTipoSemina = null;
    try
    {
      SolmrLogger.debug(this,
          "[UtilizzoParticellaGaaDAO::getElencoTipoSemina] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT TS.ID_SEMINA, " +
        "       TS.CODICE_SEMINA, " +
        "       TS.DESCRIZIONE_SEMINA, " +
        "       TS.FLAG_DEFAULT " +
        "FROM   DB_TIPO_SEMINA TS " +
        "WHERE  TS.DATA_FINE_VALIDITA IS NULL " +
        "ORDER BY TS.DESCRIZIONE_SEMINA ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[UtilizzoParticellaGaaDAO::getElencoTipoSemina] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      

      // Setto i parametri della query
           
      
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      { 
        if(vTipoSemina == null)
          vTipoSemina = new Vector<TipoSeminaVO>();
        
        tipoSeminaVO = new TipoSeminaVO();
        tipoSeminaVO.setIdTipoSemina(rs.getLong("ID_SEMINA"));
        tipoSeminaVO.setCodiceSemina(rs.getString("CODICE_SEMINA"));
        tipoSeminaVO.setDescrizioneSemina(rs.getString("DESCRIZIONE_SEMINA"));
        tipoSeminaVO.setFlagDefault(rs.getString("FLAG_DEFAULT"));
        
        vTipoSemina.add(tipoSeminaVO);
      }
      
      return vTipoSemina;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("vTipoSemina", vTipoSemina),
          new Variabile("tipoSeminaVO", tipoSeminaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[UtilizzoParticellaGaaDAO::getElencoTipoSemina] ", t,
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
          "[UtilizzoParticellaGaaDAO::getElencoTipoSemina] END.");
    }
  }
  
  
  public Vector<TipoPraticaMantenimentoVO> getElencoPraticaMantenimento(Vector<Long> vIdMantenimento) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    TipoPraticaMantenimentoVO tipoPraticaMantenimentoVO = null;
    Vector<TipoPraticaMantenimentoVO> vTipoPraticaMantenim = null;
    try
    {
      SolmrLogger.debug(this,
          "[UtilizzoParticellaGaaDAO::getElencoPraticaMantenimento] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT TPM.ID_PRATICA_MANTENIMENTO, " +
        "       TPM.CODICE_PRATICA_MANTENIMENTO, " +
        "       TPM.DESCRIZIONE_PRATICA_MANTENIMEN " +
        "FROM   DB_TIPO_PRATICA_MANTENIMENTO TPM " +
        "WHERE  TPM.ID_PRATICA_MANTENIMENTO IN ("+getIdListFromVectorLongForSQL(vIdMantenimento)+")"+
        "ORDER BY TPM.DESCRIZIONE_PRATICA_MANTENIMEN ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[UtilizzoParticellaGaaDAO::getElencoPraticaMantenimento] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      

      // Setto i parametri della query
           
      
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      { 
        if(vTipoPraticaMantenim == null)
          vTipoPraticaMantenim = new Vector<TipoPraticaMantenimentoVO>();
        
        tipoPraticaMantenimentoVO = new TipoPraticaMantenimentoVO();
        tipoPraticaMantenimentoVO.setIdPraticaMantenimento(rs.getLong("ID_PRATICA_MANTENIMENTO"));
        tipoPraticaMantenimentoVO.setCodicePraticaMantenimento(rs.getString("CODICE_PRATICA_MANTENIMENTO"));
        tipoPraticaMantenimentoVO.setDescrizionePraticaMantenim(rs.getString("DESCRIZIONE_PRATICA_MANTENIMEN"));
                
        vTipoPraticaMantenim.add(tipoPraticaMantenimentoVO);
      }
      
      return vTipoPraticaMantenim;
      
    }
    catch (Exception t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("vTipoPraticaMantenim", vTipoPraticaMantenim),
          new Variabile("tipoPraticaMantenimentoVO", tipoPraticaMantenimentoVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("vIdMantenimento", vIdMantenimento)};

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[UtilizzoParticellaGaaDAO::getElencoPraticaMantenimento] ", t,
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
          "[UtilizzoParticellaGaaDAO::getElencoPraticaMantenimento] END.");
    }
  }
  
  
  public Vector<TipoFaseAllevamentoVO> getElencoFaseAllevamento() 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    TipoFaseAllevamentoVO tipoFaseAllevamentoVO = null;
    Vector<TipoFaseAllevamentoVO> vTipoFaseAllev = null;
    try
    {
      SolmrLogger.debug(this,
          "[UtilizzoParticellaGaaDAO::getElencoFaseAllevamento] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT TFA.ID_FASE_ALLEVAMENTO, " +
        "       TFA.CODICE_FASE_ALLEVAMENTO, " +
        "       TFA.DESCRIZIONE_FASE_ALLEVAMENTO, " +
        "       TFA.FLAG_DEFAULT " +
        "FROM   DB_TIPO_FASE_ALLEVAMENTO TFA " +
        "WHERE  TFA.DATA_FINE_VALIDITA IS NULL " +
        "ORDER BY TFA.DESCRIZIONE_FASE_ALLEVAMENTO ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[UtilizzoParticellaGaaDAO::getElencoFaseAllevamento] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      

      // Setto i parametri della query
           
      
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      { 
        if(vTipoFaseAllev == null)
          vTipoFaseAllev = new Vector<TipoFaseAllevamentoVO>();
        
        tipoFaseAllevamentoVO = new TipoFaseAllevamentoVO();
        tipoFaseAllevamentoVO.setIdFaseAllevamento(rs.getLong("ID_FASE_ALLEVAMENTO"));
        tipoFaseAllevamentoVO.setCodiceFaseAllevamento(rs.getString("CODICE_FASE_ALLEVAMENTO"));
        tipoFaseAllevamentoVO.setDescrizioneFaseAllevamento(rs.getString("DESCRIZIONE_FASE_ALLEVAMENTO"));
        tipoFaseAllevamentoVO.setFlagDefault(rs.getString("FLAG_DEFAULT"));
        
        vTipoFaseAllev.add(tipoFaseAllevamentoVO);
      }
      
      return vTipoFaseAllev;
      
    }
    catch (Exception t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("vTipoFaseAllev", vTipoFaseAllev),
          new Variabile("tipoFaseAllevamentoVO", tipoFaseAllevamentoVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[UtilizzoParticellaGaaDAO::getElencoFaseAllevamento] ", t,
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
          "[UtilizzoParticellaGaaDAO::getElencoFaseAllevamento] END.");
    }
  }
  

}
