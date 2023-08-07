package it.csi.smranag.smrgaa.integration.terreni;

import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.dto.terreni.RegistroPascoloVO;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.smrcomms.reportdin.util.Validator;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class RegistroPascoloGaaDAO extends it.csi.solmr.integration.BaseDAO
{

  public RegistroPascoloGaaDAO() throws ResourceAccessException
  {
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public RegistroPascoloGaaDAO(String refName) throws ResourceAccessException
  {
    super(refName);
  }

  /**
   * Dati del registro pascoli al piano in lavorazione
   * 
   * 
   * 
   * @param storicoParticellaVO
   * @return
   * @throws DataAccessException
   */
  public RegistroPascoloVO getRegistroPascoloPianoLavorazioneIdFonte(
      long idParticellaCertificata, long idFonte) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    RegistroPascoloVO registroPascoloVO = null;
    
    try
    {
      SolmrLogger.debug(this,
          "[RegistroPascoloGaaDAO::getRegistroPascoloPianoLavorazioneIdFonte] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
          "SELECT EPM.ANNO_CAMPAGNA, " +
          "       TF.DESCRIZIONE, " +
          "       EPM.ID_FONTE, " +
          "       EPM.SUPERFICIE " +
          "FROM   DB_ESITO_PASCOLO_MAGRO EPM, " +
          "       DB_TIPO_FONTE TF " +
          "WHERE  EPM.ID_PARTICELLA_CERTIFICATA = ? " +
          "AND    EPM.ID_FONTE = ? " +
          "AND    EPM.ID_FONTE = TF.ID_FONTE " +
          "AND    EPM.DATA_FINE_VALIDITA IS NULL " +
          "AND    EPM.ANNO_CAMPAGNA = (SELECT MAX(EPM1.ANNO_CAMPAGNA) " + 
          "                            FROM   DB_ESITO_PASCOLO_MAGRO EPM1 " +
          "                            WHERE  EPM1.ID_PARTICELLA_CERTIFICATA = ? " +
          "                            AND    EPM1.ID_FONTE = ? )");
                                    
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[RegistroPascoloGaaDAO::getRegistroPascoloPianoLavorazioneIdFonte] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idParticellaCertificata);
      stmt.setLong(++indice, idFonte);
      stmt.setLong(++indice, idParticellaCertificata);
      stmt.setLong(++indice, idFonte);
      
      
      
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        registroPascoloVO = new RegistroPascoloVO();
        registroPascoloVO.setAnnoCampagna(checkInt(rs.getString("ANNO_CAMPAGNA")));
        registroPascoloVO.setDescFonte(rs.getString("DESCRIZIONE"));
        registroPascoloVO.setIdFonte(checkLongNull(rs.getString("ID_FONTE")));
        registroPascoloVO.setSuperficie(rs.getBigDecimal("SUPERFICIE"));
       
        
      }
      
      return registroPascoloVO;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("registroPascoloVO", registroPascoloVO)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idParticellaCertificata", idParticellaCertificata) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[RegistroPascoloGaaDAO::getRegistroPascoloPianoLavorazioneIdFonte] ", t,
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
          "[RegistroPascoloGaaDAO::getRegistroPascoloPianoLavorazioneIdFonte] END.");
    }
  }
  
  
  public RegistroPascoloVO getRegistroPascoloPianoLavorazioneChiaveCatastaleIdFonte(
      String istatComune, String foglio, String particella, String sezione, String subalterno, long idFonte) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    RegistroPascoloVO registroPascoloVO = null;
    
    try
    {
      SolmrLogger.debug(this,
          "[RegistroPascoloGaaDAO::getRegistroPascoloPianoLavorazioneChiaveCatastaleIdFonte] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT EPM.ANNO_CAMPAGNA, " +
        "       TF.DESCRIZIONE, " +
        "       EPM.ID_FONTE, " +
        "       EPM.SUPERFICIE " +
        "FROM   DB_ESITO_PASCOLO_MAGRO EPM, " +
        "       DB_TIPO_FONTE TF " +
        "WHERE  EPM.COMUNE = ? " +
        "AND    EPM.FOGLIO = ? " +
        "AND    EPM.PARTICELLA = ? ");
      if(Validator.isNotEmpty(sezione))
      {
        queryBuf.append(
        "AND    EPM.SEZIONE = ? ");
      }
      if(Validator.isNotEmpty(subalterno))
      {
        queryBuf.append(
        "AND    EPM.SUBALTERNO = ? ");
      }
      queryBuf.append(
        "AND    EPM.ID_FONTE = ? " +
        "AND    EPM.ID_FONTE = TF.ID_FONTE " +
        "AND    EPM.DATA_FINE_VALIDITA IS NULL " +
        "AND    EPM.ANNO_CAMPAGNA = (SELECT MAX(EPM1.ANNO_CAMPAGNA) " + 
        "                            FROM   DB_ESITO_PASCOLO_MAGRO EPM1 " +
        "                            WHERE  EPM1.COMUNE = ? " +
        "                            AND    EPM1.FOGLIO = ? " +
        "                            AND    EPM1.PARTICELLA = ? ");
      if(Validator.isNotEmpty(sezione))
      {
        queryBuf.append(
        "                            AND    EPM1.SEZIONE = ? ");
      }
      if(Validator.isNotEmpty(subalterno))
      {
        queryBuf.append(
        "                            AND    EPM1.SUBALTERNO = ? ");
      }
      queryBuf.append(
        "                            AND    EPM1.ID_FONTE = ? )");
                                    
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[RegistroPascoloGaaDAO::getRegistroPascoloPianoLavorazioneChiaveCatastaleIdFonte] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;

      // Setto i parametri della query
      stmt.setString(++indice, istatComune);
      stmt.setString(++indice, foglio);
      stmt.setString(++indice, particella);
      if(Validator.isNotEmpty(sezione))
      {
        stmt.setString(++indice, sezione);
      }
      if(Validator.isNotEmpty(subalterno))
      {
        stmt.setString(++indice, subalterno);
      }
      stmt.setLong(++indice, idFonte);
      stmt.setString(++indice, istatComune);
      stmt.setString(++indice, foglio);
      stmt.setString(++indice, particella);
      if(Validator.isNotEmpty(sezione))
      {
        stmt.setString(++indice, sezione);
      }
      if(Validator.isNotEmpty(subalterno))
      {
        stmt.setString(++indice, subalterno);
      }
      stmt.setLong(++indice, idFonte);
      
      
      
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        registroPascoloVO = new RegistroPascoloVO();
        registroPascoloVO.setAnnoCampagna(checkInt(rs.getString("ANNO_CAMPAGNA")));
        registroPascoloVO.setDescFonte(rs.getString("DESCRIZIONE"));
        registroPascoloVO.setIdFonte(checkLongNull(rs.getString("ID_FONTE")));
        registroPascoloVO.setSuperficie(rs.getBigDecimal("SUPERFICIE"));
       
        
      }
      
      return registroPascoloVO;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("registroPascoloVO", registroPascoloVO)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("istatComune", istatComune),
          new Parametro("foglio", foglio),
          new Parametro("particella", particella),
          new Parametro("sezione", sezione),
          new Parametro("subalterno", subalterno),
          new Parametro("idFonte", idFonte)};

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[RegistroPascoloGaaDAO::getRegistroPascoloPianoLavorazioneChiaveCatastaleIdFonte] ", t,
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
          "[RegistroPascoloGaaDAO::getRegistroPascoloPianoLavorazioneChiaveCatastaleIdFonte] END.");
    }
  }
  
  
  
  public Vector<Long> getIdFonteRegistroPascoliPianoLavorazione(
      long idParticellaCertificata) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<Long> vIdFonte = null;
    
    try
    {
      SolmrLogger.debug(this,
          "[RegistroPascoloGaaDAO::getIdFonteRegistroPascoliPianoLavorazione] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
          "SELECT EPM.ID_FONTE " +
          "FROM   DB_ESITO_PASCOLO_MAGRO EPM " +
          "WHERE  EPM.ID_PARTICELLA_CERTIFICATA = ? " +
          "AND    EPM.DATA_FINE_VALIDITA IS NULL ");
                                    
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[RegistroPascoloGaaDAO::getIdFonteRegistroPascoliPianoLavorazione] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idParticellaCertificata);
      
      
      
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(vIdFonte == null)
        {
          vIdFonte = new Vector<Long>();
        }
        
        vIdFonte.add(new Long(rs.getLong("ID_FONTE")));
        
      }
      
      return vIdFonte;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("vIdFonte", vIdFonte)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idParticellaCertificata", idParticellaCertificata) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[RegistroPascoloGaaDAO::getIdFonteRegistroPascoliPianoLavorazione] ", t,
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
          "[RegistroPascoloGaaDAO::getIdFonteRegistroPascoliPianoLavorazione] END.");
    }
  }
  
  public Vector<Long> getIdFonteRegistroPascoliPianoLavorazioneChiaveCatastale(
      String istatComune, String foglio, String particella, String sezione, String subalterno) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<Long> vIdFonte = null;
    
    try
    {
      SolmrLogger.debug(this,
          "[RegistroPascoloGaaDAO::getIdFonteRegistroPascoliPianoLavorazioneChiaveCatastale] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT EPM.ID_FONTE " +
        "FROM   DB_ESITO_PASCOLO_MAGRO EPM " +
        "WHERE  EPM.COMUNE = ? " +
        "AND    EPM.FOGLIO = ? " +
        "AND    EPM.PARTICELLA = ? ");
      if(Validator.isNotEmpty(sezione))
      {
        queryBuf.append(
        "AND    EPM.SEZIONE = ? ");
      }
      if(Validator.isNotEmpty(subalterno))
      {
        queryBuf.append(
        "AND    EPM.SUBALTERNO = ? ");
      }
      queryBuf.append(
        "AND    EPM.DATA_FINE_VALIDITA IS NULL ");
                                    
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[RegistroPascoloGaaDAO::getIdFonteRegistroPascoliPianoLavorazioneChiaveCatastale] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;

      // Setto i parametri della query
      stmt.setString(++indice, istatComune);
      stmt.setString(++indice, foglio);
      stmt.setString(++indice, particella);
      if(Validator.isNotEmpty(sezione))
      {
        stmt.setString(++indice, sezione);
      }
      if(Validator.isNotEmpty(subalterno))
      {
        stmt.setString(++indice, subalterno);
      }
      
      
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(vIdFonte == null)
        {
          vIdFonte = new Vector<Long>();
        }
        
        vIdFonte.add(new Long(rs.getLong("ID_FONTE")));
        
      }
      
      return vIdFonte;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("vIdFonte", vIdFonte)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("istatComune", istatComune),
        new Parametro("foglio", foglio),
        new Parametro("particella", particella),
        new Parametro("sezione", sezione),
        new Parametro("subalterno", subalterno)};

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[RegistroPascoloGaaDAO::getIdFonteRegistroPascoliPianoLavorazioneChiaveCatastale] ", t,
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
          "[RegistroPascoloGaaDAO::getIdFonteRegistroPascoliPianoLavorazioneChiaveCatastale] END.");
    }
  }
  
  
  /**
   * Dati del registro pascoli alla dichiarazione
   * di consistenza
   * 
   * 
   * 
   * @param storicoParticellaVO
   * @param annoDichiarazione
   * @return
   * @throws DataAccessException
   */
  public Vector<RegistroPascoloVO> getRegistroPascoliDichCons(
      long idParticellaCertificata, int annoDichiarazione) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<RegistroPascoloVO> vRegistroPascoloVO = null;
    
    try
    {
      SolmrLogger.debug(this,
          "[RegistroPascoloGaaDAO::getRegistroPascoliDichCons] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
          "SELECT EPM.ANNO_CAMPAGNA, " +
          "       TF.DESCRIZIONE, " +
          "       EPM.ID_FONTE, " +
          "       EPM.SUPERFICIE " +
          "FROM   DB_ESITO_PASCOLO_MAGRO EPM, " +
          "       DB_TIPO_FONTE TF " +
          "WHERE  EPM.ID_PARTICELLA_CERTIFICATA = ? " +
          "AND    EPM.ID_FONTE = TF.ID_FONTE " +
          "AND    EPM.ANNO_CAMPAGNA = (SELECT MAX(EPM1.ANNO_CAMPAGNA) " +
          "                            FROM   DB_ESITO_PASCOLO_MAGRO EPM1 " +
          "                            WHERE  EPM1.ID_PARTICELLA_CERTIFICATA = ? " +
          "                            AND    EPM1.ANNO_CAMPAGNA <= ?) ");
                                    
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[RegistroPascoloGaaDAO::getRegistroPascoliDichCons] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idParticellaCertificata);
      stmt.setLong(++indice, idParticellaCertificata);
      stmt.setInt(++indice, annoDichiarazione);
      
      
      
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(vRegistroPascoloVO == null)
        {
          vRegistroPascoloVO = new Vector<RegistroPascoloVO>();
        }
        
        RegistroPascoloVO registroPascoloVO = new RegistroPascoloVO();
        registroPascoloVO.setAnnoCampagna(checkInt(rs.getString("ANNO_CAMPAGNA")));
        registroPascoloVO.setDescFonte(rs.getString("DESCRIZIONE"));
        registroPascoloVO.setIdFonte(checkLongNull(rs.getString("ID_FONTE")));
        registroPascoloVO.setSuperficie(rs.getBigDecimal("SUPERFICIE"));
        
        vRegistroPascoloVO.add(registroPascoloVO);
      }
      
      return vRegistroPascoloVO;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("vRegistroPascoloVO", vRegistroPascoloVO)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idParticellaCertificata", idParticellaCertificata),
        new Parametro("annoDichiarazione", annoDichiarazione) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[RegistroPascoloGaaDAO::getRegistroPascoliDichCons] ", t,
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
          "[RegistroPascoloGaaDAO::getRegistroPascoliDichCons] END.");
    }
  }
  
  public Vector<RegistroPascoloVO> getRegistroPascoliDichConsChiaveCatastale(
      String istatComune, String foglio, String particella, String sezione, String subalterno, 
      int annoDichiarazione) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<RegistroPascoloVO> vRegistroPascoloVO = null;
    
    try
    {
      SolmrLogger.debug(this,
          "[RegistroPascoloGaaDAO::getRegistroPascoliDichConsChiaveCatastale] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT EPM.ANNO_CAMPAGNA, " +
        "       TF.DESCRIZIONE, " +
        "       EPM.ID_FONTE, " +
        "       EPM.SUPERFICIE " +
        "FROM   DB_ESITO_PASCOLO_MAGRO EPM, " +
        "       DB_TIPO_FONTE TF " +
        "WHERE  EPM.COMUNE = ? " +
        "AND    EPM.FOGLIO = ? " +
        "AND    EPM.PARTICELLA = ? ");
      if(Validator.isNotEmpty(sezione))
      {
        queryBuf.append(
        "AND    EPM.SEZIONE = ? ");
      }
      if(Validator.isNotEmpty(subalterno))
      {
        queryBuf.append(
        "AND    EPM.SUBALTERNO = ? ");
      }
      queryBuf.append(
        "AND    EPM.ID_FONTE = TF.ID_FONTE " +
        "AND    EPM.ANNO_CAMPAGNA = (SELECT MAX(EPM1.ANNO_CAMPAGNA) " +
        "                            FROM   DB_ESITO_PASCOLO_MAGRO EPM1 " +
        "                            WHERE  EPM1.COMUNE = ? " +
        "                            AND    EPM1.FOGLIO = ? " +
        "                            AND    EPM1.PARTICELLA = ? ");
      if(Validator.isNotEmpty(sezione))
      {
        queryBuf.append(
        "                            AND    EPM1.SEZIONE = ? ");
      }
      if(Validator.isNotEmpty(subalterno))
      {
        queryBuf.append(
        "                            AND    EPM1.SUBALTERNO = ? ");
      }
      queryBuf.append(
        "                            AND    EPM1.ANNO_CAMPAGNA <= ?) ");
                                    
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[RegistroPascoloGaaDAO::getRegistroPascoliDichConsChiaveCatastale] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;

      // Setto i parametri della query
      stmt.setString(++indice, istatComune);
      stmt.setString(++indice, foglio);
      stmt.setString(++indice, particella);
      if(Validator.isNotEmpty(sezione))
      {
        stmt.setString(++indice, sezione);
      }
      if(Validator.isNotEmpty(subalterno))
      {
        stmt.setString(++indice, subalterno);
      }
      stmt.setString(++indice, istatComune);
      stmt.setString(++indice, foglio);
      stmt.setString(++indice, particella);
      if(Validator.isNotEmpty(sezione))
      {
        stmt.setString(++indice, sezione);
      }
      if(Validator.isNotEmpty(subalterno))
      {
        stmt.setString(++indice, subalterno);
      }
      stmt.setInt(++indice, annoDichiarazione);
      
      
      
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(vRegistroPascoloVO == null)
        {
          vRegistroPascoloVO = new Vector<RegistroPascoloVO>();
        }
        
        RegistroPascoloVO registroPascoloVO = new RegistroPascoloVO();
        registroPascoloVO.setAnnoCampagna(checkInt(rs.getString("ANNO_CAMPAGNA")));
        registroPascoloVO.setDescFonte(rs.getString("DESCRIZIONE"));
        registroPascoloVO.setIdFonte(checkLongNull(rs.getString("ID_FONTE")));
        registroPascoloVO.setSuperficie(rs.getBigDecimal("SUPERFICIE"));
        
        vRegistroPascoloVO.add(registroPascoloVO);
      }
      
      return vRegistroPascoloVO;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("vRegistroPascoloVO", vRegistroPascoloVO)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("istatComune", istatComune),
          new Parametro("foglio", foglio),
          new Parametro("particella", particella),
          new Parametro("sezione", sezione),
          new Parametro("subalterno", subalterno),
        new Parametro("annoDichiarazione", annoDichiarazione) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[RegistroPascoloGaaDAO::getRegistroPascoliDichConsChiaveCatastale] ", t,
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
          "[RegistroPascoloGaaDAO::getRegistroPascoliDichConsChiaveCatastale] END.");
    }
  }
  
  
  public boolean isRegistroPascoliPratoPolifita(
      long idParticellaCertificata) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean trovato = false;
    
    try
    {
      SolmrLogger.debug(this,
          "[RegistroPascoloGaaDAO::isRegistroPascoliPratoPolifita] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
          "SELECT EPM.ID_ESITO_PASCOLO_MAGRO " +
          "FROM   DB_ESITO_PASCOLO_MAGRO EPM " +
          "WHERE  EPM.ID_PARTICELLA_CERTIFICATA = ? " +
          "AND    EPM.ID_FONTE = 9 " +
          "AND    EPM.DATA_FINE_VALIDITA IS NULL ");
                                    
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[RegistroPascoloGaaDAO::isRegistroPascoliPratoPolifita] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idParticellaCertificata);
      
      
      
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        trovato = true;        
      }
      
      return trovato;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("trovato", trovato)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idParticellaCertificata", idParticellaCertificata) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[RegistroPascoloGaaDAO::isRegistroPascoliPratoPolifita] ", t,
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
          "[RegistroPascoloGaaDAO::isRegistroPascoliPratoPolifita] END.");
    }
  }
  
  
  public Long insertRegistroPascolo(RegistroPascoloVO registroPascoloVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idEsitoPascoloMagro = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[RegistroPascoloGaaDAO::insertRegistroPascolo] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idEsitoPascoloMagro = getNextPrimaryKey(SolmrConstants.SEQ_DB_ESITO_PASCOLO_MAGRO);
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_ESITO_PASCOLO_MAGRO   " 
              + "     (ID_ESITO_PASCOLO_MAGRO, "
              + "     ANNO_CAMPAGNA, " 
              + "     COMUNE, "
              + "     ID_FONTE, "
              + "     SEZIONE, "
              + "     ID_PARTICELLA_CERTIFICATA, "
              + "     FOGLIO, "
              + "     PARTICELLA, "
              + "     SUBALTERNO,"
              + "     SUPERFICIE, "
              + "     DATA_INIZIO_VALIDITA)  "
              + "   VALUES(?,?,?,?,?,?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[RegistroPascoloGaaDAO::insertRegistroPascolo] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idEsitoPascoloMagro.longValue());
      stmt.setInt(++indice,  registroPascoloVO.getAnnoCampagna().intValue());
      stmt.setString(++indice, registroPascoloVO.getComune());
      stmt.setLong(++indice,  registroPascoloVO.getIdFonte().longValue());
      stmt.setString(++indice, registroPascoloVO.getSezione());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(registroPascoloVO.getIdParticellaCertificata()));  
      stmt.setInt(++indice,  registroPascoloVO.getFoglio().intValue());
      stmt.setLong(++indice,  registroPascoloVO.getParticella().longValue());
      stmt.setString(++indice, registroPascoloVO.getSubalterno());
      stmt.setBigDecimal(++indice, registroPascoloVO.getSuperficie());
      stmt.setTimestamp(++indice, convertDateToTimestamp(registroPascoloVO.getDataInizioValidita()));
      
  
      stmt.executeUpdate();
      
      return idEsitoPascoloMagro;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idEsitoPascoloMagro", idEsitoPascoloMagro)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("registroPascoloVO", registroPascoloVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[RegistroPascoloGaaDAO::insertRegistroPascolo] ",
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
              "[RegistroPascoloGaaDAO::insertRegistroPascolo] END.");
    }
  }
  
  
  
  

  

}
