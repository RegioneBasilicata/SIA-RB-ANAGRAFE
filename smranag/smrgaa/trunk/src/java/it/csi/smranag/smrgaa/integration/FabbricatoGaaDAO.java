package it.csi.smranag.smrgaa.integration;


import it.csi.smranag.smrgaa.dto.fabbricati.FabbricatoBioVO;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.anag.fabbricati.TipoFormaFabbricatoVO;
import it.csi.solmr.dto.anag.fabbricati.TipoTipologiaFabbricatoVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;


public class FabbricatoGaaDAO extends it.csi.solmr.integration.BaseDAO {
  
  
  public FabbricatoGaaDAO() throws ResourceAccessException{
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public FabbricatoGaaDAO(String refName) throws ResourceAccessException {
   super(refName);
  }

  /**
   * Restituisce il TipoFabbricatoVO con le caratteristiche del fabbricato
   * 
   * @param ids
   * @return
   * @throws DataAccessException
   */
  public TipoTipologiaFabbricatoVO getInfoTipologiaFabbricato(
      Long idTipologiaFabbricato) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    TipoTipologiaFabbricatoVO tipoTipologiaFabbricatoVO = null;
    
    try
    {
      SolmrLogger
          .debug(
              this,
              "[FabbricatoGaaDAO::getInfoTipologiaFabbricato] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT TTF.ID_TIPOLOGIA_FABBRICATO AS ID_TIPOLOGIA_FABBRICATO, " +
        "       DESCRIZIONE, " +
        "       UNITA_MISURA, " +
        "       FLAG_PER_STOCCAGGIO, " +
        "       FLAG_PALABILE, " +
        "       TIPO_FORMULA, " +
        "       TESTO_LAYER, " +
        "       OBBLIGO_PARTICELLA, " +
        "       OBBLIGO_COORDINATE "+
      	"FROM   DB_TIPO_TIPOLOGIA_FABBRICATO TTF, " +
      	"       DB_FABB_DIM_TIPOLOGIA FDT "+
        "WHERE  TTF.ID_TIPOLOGIA_FABBRICATO = ? "+
        "AND    TTF.ID_TIPOLOGIA_FABBRICATO = FDT.ID_TIPOLOGIA_FABBRICATO "+
        "AND    TTF.ANNO_FINE_VALIDITA IS NULL "+
        "AND    FDT.DATA_FINE_VALIDITA IS NULL "+
        "ORDER BY FDT.ID_FABBRICATO_DIMENSIONE ");
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
                "[FabbricatoGaaDAO::getInfoTipologiaFabbricato] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idTipologiaFabbricato.longValue());
      ResultSet rs = stmt.executeQuery();
          
      int count = 0;
      Vector<String> arLabel = null; 
      while (rs.next())
      {
        //Questi campi sono ripetuti
        if(count==0)
        {
          tipoTipologiaFabbricatoVO = new TipoTipologiaFabbricatoVO();
          arLabel = new Vector<String>();
          tipoTipologiaFabbricatoVO.setIdTipologiaFabbricato(new Long(rs.getLong("ID_TIPOLOGIA_FABBRICATO")));
          tipoTipologiaFabbricatoVO.setDescrizione(rs.getString("DESCRIZIONE"));
          tipoTipologiaFabbricatoVO.setUnitaMisura(rs.getString("UNITA_MISURA"));
          tipoTipologiaFabbricatoVO.setFlagStoccaggio(rs.getString("FLAG_PER_STOCCAGGIO"));
          tipoTipologiaFabbricatoVO.setFlagPalabile(rs.getString("FLAG_PALABILE"));
          tipoTipologiaFabbricatoVO.setTipoFormula(rs.getString("TIPO_FORMULA"));
          tipoTipologiaFabbricatoVO.setObbligoParticella(rs.getString("OBBLIGO_PARTICELLA"));
          tipoTipologiaFabbricatoVO.setObbligoCoordinate(rs.getString("OBBLIGO_COORDINATE"));
        }
        
        arLabel.add(rs.getString("TESTO_LAYER"));
        
        count++;
      }
      if(arLabel !=null)
      {
        tipoTipologiaFabbricatoVO.setVLabel(arLabel);
      }
      
      return tipoTipologiaFabbricatoVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idTipologiaFabbricato", idTipologiaFabbricato) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[FabbricatoGaaDAO::getInfoTipologiaFabbricato] ",
              t, query, variabili, parametri);
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
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger
          .debug(this,
              "[FabbricatoGaaDAO::getInfoTipologiaFabbricato] END.");
    }
  }
  
  /**
   * Restituisce il TipoFormaFabbricatoVO con le caratteristiche del fabbricato
   * 
   * @param ids
   * @return
   * @throws DataAccessException
   */
  public TipoFormaFabbricatoVO getTipoFormaFabbricato(
      Long idFormaFabbricato) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    TipoFormaFabbricatoVO tipoFormaFabbricatoVO = null;
    try
    {
      SolmrLogger
          .debug(
              this,
              "[FabbricatoGaaDAO::getTipoFormaFabbricato] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("SELECT ID_FORMA_FABBRICATO, DESCRIZIONE, " +
      		"FATTORE_CUBATURA, ID_TIPOLOGIA_FABBRICATO, COEFF_IMPILABILE " +
      		"FROM DB_TIPO_FORMA_FABBRICATO " +
          "WHERE ID_FORMA_FABBRICATO = ? ");
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
                "[FabbricatoGaaDAO::getTipoFormaFabbricato] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idFormaFabbricato.longValue());
      ResultSet rs = stmt.executeQuery();
          
      if(rs.next())
      {
        
        tipoFormaFabbricatoVO = new TipoFormaFabbricatoVO();
        tipoFormaFabbricatoVO.setIdFormaFabbricato(new Long(rs.getLong("ID_FORMA_FABBRICATO")));
        tipoFormaFabbricatoVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoFormaFabbricatoVO.setFattoreCubatura(new Double(rs.getDouble("FATTORE_CUBATURA")));
        tipoFormaFabbricatoVO.setIdTipologiaFabbricato(new Long(rs.getLong("ID_TIPOLOGIA_FABBRICATO")));
        tipoFormaFabbricatoVO.setCoeffImpilabile(new Double(rs.getDouble("COEFF_IMPILABILE")));
                  
      }
        
    
      
      return tipoFormaFabbricatoVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idFormaFabbricato", idFormaFabbricato) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[FabbricatoGaaDAO::getTipoFormaFabbricato] ",
              t, query, variabili, parametri);
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
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger
          .debug(this,
              "[FabbricatoGaaDAO::getTipoFormaFabbricato] END.");
    }
    
    
  }
  
  
  /**
   * Ritorna un vettore di TipoTipologiaFabbricatoVO validi (data_fine_validita == null)
   * adatti allo stoccaggio (flag_per_stoccaggio = 'S') 
   * 
   * @return
   * @throws DataAccessException
   */
  public Vector<TipoTipologiaFabbricatoVO> getListTipoFabbricatoStoccaggio() 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoTipologiaFabbricatoVO> vTipoFabbricato = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[FabbricatoGaaDAO::getListTipoFabbricatoStoccaggio] BEGIN.");

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   SELECT   " 
              + "     TTF.ID_TIPOLOGIA_FABBRICATO, " 
              + "     TTF.DESCRIZIONE "
              + "   FROM   "
              + "     DB_TIPO_TIPOLOGIA_FABBRICATO TTF   "
              + "   WHERE  "
              + "     TTF.ANNO_FINE_VALIDITA IS NULL  "
              + " AND TTF.FLAG_PER_STOCCAGGIO = 'S'  ");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[FabbricatoGaaDAO::getListTipoFabbricatoStoccaggio] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vTipoFabbricato == null)
        {
          vTipoFabbricato = new Vector<TipoTipologiaFabbricatoVO>();
        }
        TipoTipologiaFabbricatoVO tipoVO  = new TipoTipologiaFabbricatoVO();
        tipoVO.setIdTipologiaFabbricato(new Long(rs.getLong("ID_TIPOLOGIA_FABBRICATO")));
        tipoVO.setDescrizione(rs.getString("DESCRIZIONE"));
        vTipoFabbricato.add(tipoVO);
      }
      return vTipoFabbricato;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vTipoFabbricato", vTipoFabbricato) };
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[FabbricatoGaaDAO::getListTipoEffluente] ",
              t, query, variabili, null);
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
              "[FabbricatoGaaDAO::getListTipoFabbricatoStoccaggio] END.");
    }
  }
  
  
  
  /**
   * 
   * restituisce i dati della tabella DB_FABBRICATO_BIO
   * relativi alla particella e alla dichiarazione di consistenza
   * 
   * 
   * 
   * @param idParticella
   * @param idDichiarazioneConsistenza
   * @return
   * @throws DataAccessException
   */
  public FabbricatoBioVO getFabbricatoBio(long idFabbricato, long idAzienda, Date dataInserimentoDichiarazione)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    FabbricatoBioVO fabbricatoBioVO = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[FabbricatoGaaDAO::getFabbricatoBio] BEGIN.");
    
      query = " " +
          "SELECT FB.ID_FABBRICATO_BIO, " +
          "       FB.ID_AZIENDA, " +
          "       FB.ID_FABBRICATO, " +
          "       FB.DIMENSIONE, " +
          "       FB.DIMENSIONE_CONVENZIONALE, " +
          "       FB.DIMENSIONE_BIOLOGICO, " +
          "       FB.DATA_INIZIO_VALIDITA, " +
          "       FB.DATA_FINE_VALIDITA " +
          "FROM   DB_FABBRICATO_BIO FB " +
          "WHERE  FB.ID_FABBRICATO = ? " +
          "AND    FB.ID_AZIENDA = ? ";
      
      if(Validator.isNotEmpty(dataInserimentoDichiarazione))
      {
        query += "" +
          "AND   FB.DATA_INIZIO_VALIDITA < ? " +
          "AND   NVL(FB.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? ";       
      }
      else
      {
        query += "" +
          "AND   FB.DATA_FINE_VALIDITA IS NULL ";
      }
      
      
      
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[FabbricatoGaaDAO::getFabbricatoBio] Query="
                + query);
      }
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      int idx = 0;
      stmt.setLong(++idx, idFabbricato);
      stmt.setLong(++idx, idAzienda);
      
      
      if(Validator.isNotEmpty(dataInserimentoDichiarazione))
      {
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoDichiarazione));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoDichiarazione));
      }
      
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      if (rs.next())
      {
        fabbricatoBioVO = new FabbricatoBioVO();
        fabbricatoBioVO.setIdFabbricatoBio(rs.getLong("ID_FABBRICATO_BIO"));
        fabbricatoBioVO.setIdAzienda(rs.getLong("ID_AZIENDA"));
        fabbricatoBioVO.setIdFabbricato(rs.getLong("ID_FABBRICATO"));
        fabbricatoBioVO.setDimensione(rs.getBigDecimal("DIMENSIONE"));
        fabbricatoBioVO.setDimensioneConvenzionale(rs.getBigDecimal("DIMENSIONE_CONVENZIONALE"));
        fabbricatoBioVO.setDimensioneBiologico(rs.getBigDecimal("DIMENSIONE_BIOLOGICO"));
        fabbricatoBioVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        fabbricatoBioVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        
      }
      
      return fabbricatoBioVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("fabbricatoBioVO", fabbricatoBioVO) };
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idFabbricato", idFabbricato),
        new Parametro("idAzienda", idAzienda),
        new Parametro("dataInserimentoDichiarazione", dataInserimentoDichiarazione) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[FabbricatoGaaDAO::getFabbricatoBio] ", t,
          query, variabili, parametri);
    
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
      SolmrLogger.debug(this,
          "[FabbricatoGaaDAO::getFabbricatoBio] END.");
    }
  }
  
  
  
  /**
   * Restituisce un HashMap di coppie idConduzioneParticella, Descrizione fabbricato.
   * La coppia è inserita solo se esiste un fabbricato 
   * che insiste sulla particella della conduzione
   * 
   * 
   * @param elencoConduzioni
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public HashMap<Long,String> esisteFabbricatoAttivoByConduzioneAndAzienda(
      Vector<Long> elencoConduzioni, Long idAzienda)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    HashMap<Long,String> result = null;
    StringBuffer queryBuf = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[FabbricatoGaaDAO::esisteFabbricatoAttivoByConduzioneAndAzienda] BEGIN.");
      
      queryBuf = new StringBuffer();
      
      queryBuf.append(
          "SELECT CP.ID_CONDUZIONE_PARTICELLA, " +
          "       TTF.DESCRIZIONE " +
          "FROM   DB_UTE UT, " +
          "       DB_CONDUZIONE_PARTICELLA CP, " + 
          "       DB_FABBRICATO_PARTICELLA FP, " +
          "       DB_FABBRICATO FA, " +
          "       DB_TIPO_TIPOLOGIA_FABBRICATO TTF " +
          "WHERE  UT.ID_AZIENDA = ? " + 
          "AND    UT.ID_UTE = CP.ID_UTE " + 
          "AND    UT.DATA_FINE_ATTIVITA IS NULL " +
          "AND    UT.ID_UTE = FA.ID_UTE " +
          "AND    CP.ID_PARTICELLA = FP.ID_PARTICELLA " +
          "AND    FA.ID_FABBRICATO = FP.ID_FABBRICATO " +
          "AND    FA.ID_TIPOLOGIA_FABBRICATO = TTF.ID_TIPOLOGIA_FABBRICATO " +
          "AND    CP.ID_TITOLO_POSSESSO <> " +SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO+" "+
          "AND    CP.ID_TITOLO_POSSESSO <> " +SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO+" "+
          "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
          "AND    FP.DATA_FINE_VALIDITA IS NULL " +
          "AND    FA.DATA_FINE_VALIDITA IS NULL " +
          "AND    CP.ID_CONDUZIONE_PARTICELLA IN ( ")
          .append(this.getIdListFromVectorLongForSQL(elencoConduzioni)).append(")");
      
      
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
                "[FabbricatoGaaDAO::esisteFabbricatoAttivoByConduzioneAndAzienda] Query="
                    + query);
      }
      
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);      
    
      ResultSet rs = stmt.executeQuery();
      while(rs.next())
      {
        if(result == null)
        {
          result = new HashMap<Long,String>();
        }
        Long idConduzioneParticella = new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA"));
        if(result.get(idConduzioneParticella) !=null)
        {
          String str = result.get(idConduzioneParticella);
          str += "\n"+rs.getString("DESCRIZIONE");
          result.put(idConduzioneParticella, str);
        }
        else
        {
          result.put(idConduzioneParticella, rs.getString("DESCRIZIONE"));
        }
        
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query), 
        new Variabile("trovato", result)};
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("elencoConduzioni", elencoConduzioni),
          new Parametro("idAzienda", idAzienda) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[FabbricatoGaaDAO::esisteFabbricatoAttivoByConduzioneAndAzienda] ", t,
          query, variabili, parametri);
    
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
      SolmrLogger.debug(this,
          "[FabbricatoGaaDAO::esisteFabbricatoAttivoByConduzioneAndAzienda] END.");
    }
  }
  
  
  
  
  
  
}
