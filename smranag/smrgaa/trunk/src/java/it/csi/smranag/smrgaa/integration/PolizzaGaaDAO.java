package it.csi.smranag.smrgaa.integration;


import it.csi.smranag.smrgaa.dto.BaseCodeDescription;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.dto.polizze.PolizzaDettaglioVO;
import it.csi.smranag.smrgaa.dto.polizze.PolizzaVO;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.TreeMap;
import java.util.Vector;


public class PolizzaGaaDAO extends it.csi.solmr.integration.BaseDAO {
  
  
  public PolizzaGaaDAO() throws ResourceAccessException{
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public PolizzaGaaDAO(String refName) throws ResourceAccessException 
  {
    super(refName);
  }
  
  
  /**
   * 
   * Restituisce gli anni campagna relativi alla azienda
   * della tabella DB_POLIZZA_ASSICURATIVA
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<Integer> getElencoAnnoCampagnaByIdAzienda(long idAzienda) throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<Integer> results = null;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[PolizzaGaaDAO::getElencoAnnoCampagnaByIdAzienda] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
      		"SELECT DISTINCT " + 
      		"        ANNO_CAMPAGNA " +
          "FROM " +
          "        DB_POLIZZA_ASSICURATIVA PA " +
          "WHERE " +
          "        ID_AZIENDA = ? " +
          "ORDER BY " +
          "        ANNO_CAMPAGNA DESC");
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
                "[PolizzaGaaDAO::getElencoAnnoCampagnaByIdAzienda] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idAzienda);
      
      rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(results == null)
        {
          results = new Vector<Integer>();
        }
        
        Integer annoCampagna = new Integer(rs.getInt("ANNO_CAMPAGNA"));
        
        results.add(annoCampagna);
      }
      
      
      return results;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("results", results), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[PolizzaGaaDAO::getElencoAnnoCampagnaByIdAzienda] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[PolizzaGaaDAO::getElencoAnnoCampagnaByIdAzienda] END.");
    }
  }
  
  
  /**
   * 
   * restituisce gli interventi relativi ad una azienda
   * sulla tabella DB_POLIZZA_ASSICURATIVA
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<BaseCodeDescription> getElencoInterventoByIdAzienda(long idAzienda) 
    throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<BaseCodeDescription> results = null;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[PolizzaGaaDAO::getElencoInterventoByIdAzienda] BEGIN.");

      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT DISTINCT" +
          "       PA.ID_TIPO_INTERVENTO, " + 
          "       TI.DESCRIZIONE " +
          "FROM " +
          "       DB_POLIZZA_ASSICURATIVA PA," +
          "       DB_TIPO_INTERVENTO TI " +
          "WHERE " +
          "       PA.ID_AZIENDA = ? " +
          "AND    PA.ID_TIPO_INTERVENTO = TI.ID_TIPO_INTERVENTO " +
          "ORDER BY " +
          "       TI.DESCRIZIONE ASC");
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
                "[PolizzaGaaDAO::getElencoInterventoByIdAzienda] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idAzienda);
      
      rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(results == null)
        {
          results = new Vector<BaseCodeDescription>();
        }
        
        BaseCodeDescription bcd = new BaseCodeDescription();
        bcd.setCode(rs.getLong("ID_TIPO_INTERVENTO"));
        bcd.setDescription(rs.getString("DESCRIZIONE"));
        
        
        results.add(bcd);
      }
      
      
      return results;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("results", results), new Variabile("idx", idx)

      };
      LoggerUtils.logDAOError(this,
          "[PolizzaGaaDAO::getElencoInterventoByIdAzienda] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[PolizzaGaaDAO::getElencoInterventoByIdAzienda] END.");
    }
  }
  
  
  /**
   * 
   * restiuisce un vettore che ha nel 
   * primo campo la somma VALORE_ASSICURATO
   * secondo campo la somma IMPORTO_PREMIO
   * terzo campo la somma VALORE_RISARCITO
   * 
   * di tutti i dettagli della polizza
   * 
   * 
   * 
   * @param idPolizzaAssicurativa
   * @return
   * @throws DataAccessException
   */
  private Vector<BigDecimal> getSumValAss_ImpPre_ValRis(long idPolizzaAssicurativa) 
    throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<BigDecimal> results = null;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[PolizzaGaaDAO::getSumValAss_ImpPre_ValRis] BEGIN.");
  
      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT NVL(SUM(VALORE_ASSICURATO),0) AS VALORE_ASSICURATO, " +
          "       NVL(SUM(IMPORTO_PREMIO),0) AS IMPORTO_PREMIO, " +
          "       NVL(SUM(VALORE_RISARCITO),0) AS VALORE_RISARCITO " +
          "FROM ( " +
          "      SELECT NVL(SUM(DPC.VALORE_ASSICURATO),0) AS VALORE_ASSICURATO, " +
          "             NVL(SUM(DPC.IMPORTO_PREMIO),0) AS IMPORTO_PREMIO, " +
          "             NVL(SUM(DPC.VALORE_RISARCITO),0) AS VALORE_RISARCITO " +
          "      FROM   DB_POLIZZA_ASSICURATIVA PA, " +
          "             DB_DETTAGLIO_POLIZZA DP, " +
          "             DB_DETTAGLIO_POLIZZA_COLTURA DPC " +
          "      WHERE  PA.ID_POLIZZA_ASSICURATIVA = ? " + 
          "      AND    PA.ID_POLIZZA_ASSICURATIVA = DP.ID_POLIZZA_ASSICURATIVA " +
          "      AND    DP.ID_DETTAGLIO_POLIZZA = DPC.ID_DETTAGLIO_POLIZZA " +
          "      UNION  ALL " +
          "      SELECT NVL(SUM(DPS.VALORE_ASSICURATO),0) AS VALORE_ASSICURATO, " +
          "             NVL(SUM(DPS.IMPORTO_PREMIO),0) AS IMPORTO_PREMIO, " +
          "             NVL(SUM(DPS.VALORE_RISARCITO),0) AS VALORE_RISARCITO " +
          "      FROM   DB_POLIZZA_ASSICURATIVA PA, " +
          "             DB_DETTAGLIO_POLIZZA DP, " +
          "             DB_DETTAGLIO_POLIZZA_STRUTTURA DPS " +
          "      WHERE  PA.ID_POLIZZA_ASSICURATIVA = ? " +
          "      AND    PA.ID_POLIZZA_ASSICURATIVA = DP.ID_POLIZZA_ASSICURATIVA " +
          "      AND    DP.ID_DETTAGLIO_POLIZZA = DPS.ID_DETTAGLIO_POLIZZA " +
          "      UNION  ALL " +
          "      SELECT NVL(SUM(DPZ.VALORE_ASSICURATO),0) AS VALORE_ASSICURATO, " +
          "             NVL(SUM(DPZ.IMPORTO_PREMIO),0) AS IMPORTO_PREMIO, " +
          "             NVL(SUM(DPZ.VALORE_RISARCITO),0) AS VALORE_RISARCITO " +
          "      FROM   DB_POLIZZA_ASSICURATIVA PA, " +
          "             DB_DETTAGLIO_POLIZZA DP, " +
          "             DB_DETTAGLIO_POLIZZA_ZOOTECNIA DPZ " +
          "      WHERE  PA.ID_POLIZZA_ASSICURATIVA = ? " +
          "      AND    PA.ID_POLIZZA_ASSICURATIVA = DP.ID_POLIZZA_ASSICURATIVA " +
          "      AND    DP.ID_DETTAGLIO_POLIZZA = DPZ.ID_DETTAGLIO_POLIZZA " +
          "      )");
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
                "[PolizzaGaaDAO::getSumValAss_ImpPre_ValRis] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idPolizzaAssicurativa);
      stmt.setLong(++idx, idPolizzaAssicurativa);
      stmt.setLong(++idx, idPolizzaAssicurativa);
      
      rs = stmt.executeQuery();
      
      if(rs.next())
      {
        if(results == null)
        {
          results = new Vector<BigDecimal>();
        }
        
        results.add(rs.getBigDecimal("VALORE_ASSICURATO"));
        results.add(rs.getBigDecimal("IMPORTO_PREMIO"));
        results.add(rs.getBigDecimal("VALORE_RISARCITO"));
      }
      
      
      return results;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idPolizzaAssicurativa", idPolizzaAssicurativa) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("results", results), new Variabile("idx", idx)
  
      };
      LoggerUtils.logDAOError(this,
          "[PolizzaGaaDAO::getSumValAss_ImpPre_ValRis] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[PolizzaGaaDAO::getSumValAss_ImpPre_ValRis] END.");
    }
  }
  
  
  private Vector<String> getMacroUso(long idPolizzaAssicurativa) 
    throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<String> results = null;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[PolizzaGaaDAO::getMacroUso] BEGIN.");
  
      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT DISTINCT " +
          "       TMU.CODICE, " +
          "       TMU.DESCRIZIONE " +
          "FROM   DB_POLIZZA_ASSICURATIVA PA, " +
          "       DB_DETTAGLIO_POLIZZA DP, " +
          "       DB_DETTAGLIO_POLIZZA_COLTURA DPC, " +
          "       DB_TIPO_MACRO_USO TMU " +
          "WHERE  PA.ID_POLIZZA_ASSICURATIVA = ? " + 
          "AND    PA.ID_POLIZZA_ASSICURATIVA = DP.ID_POLIZZA_ASSICURATIVA " +
          "AND    DP.ID_DETTAGLIO_POLIZZA = DPC.ID_DETTAGLIO_POLIZZA " +
          "AND    DPC.ID_MACRO_USO = TMU.ID_MACRO_USO " +
          "ORDER BY TMU.CODICE ");
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
                "[PolizzaGaaDAO::getMacroUso] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idPolizzaAssicurativa);
      
      rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(results == null)
        {
          results = new Vector<String>();
        }
        
        String descMacroUso = "["+rs.getString("CODICE")+"] "+rs.getString("DESCRIZIONE");
        results.add(descMacroUso);
      }
      
      
      return results;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idPolizzaAssicurativa", idPolizzaAssicurativa) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("results", results), new Variabile("idx", idx)
  
      };
      LoggerUtils.logDAOError(this,
          "[PolizzaGaaDAO::getMacroUso] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[PolizzaGaaDAO::getMacroUso] END.");
    }
  }
  
  
  /**
   * 
   * restituisce la somma del IMPORTO_PAGATO
   * di tutti i dettagli della polizza
   * 
   * 
   * @param idPolizzaAssicurativa
   * @return
   * @throws DataAccessException
   */
  private BigDecimal getImportoPagato(long idPolizzaAssicurativa) 
    throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    BigDecimal results = null;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[PolizzaGaaDAO::getImportoPagato] BEGIN.");
  
      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT NVL(SUM(DP.IMPORTO_PAGATO),0) AS IMPORTO_PAGATO " +
          "FROM   DB_POLIZZA_ASSICURATIVA PA, " +
          "       DB_DETTAGLIO_POLIZZA DP " +
          "WHERE  PA.ID_POLIZZA_ASSICURATIVA = ? " +  
          "AND    PA.ID_POLIZZA_ASSICURATIVA = DP.ID_POLIZZA_ASSICURATIVA");
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
                "[PolizzaGaaDAO::getImportoPagato] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idPolizzaAssicurativa);
      
      rs = stmt.executeQuery();
      
      if(rs.next())
      {
        results = rs.getBigDecimal("IMPORTO_PAGATO");
      }
      
      
      return results;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idPolizzaAssicurativa", idPolizzaAssicurativa) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("results", results), new Variabile("idx", idx) };
      LoggerUtils.logDAOError(this,
          "[PolizzaGaaDAO::getImportoPagato] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[PolizzaGaaDAO::getImportoPagato] END.");
    }
  }
  
  
  /**
   * 
   * restituisce l'elenco delle polizze relative ad una azienda
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<PolizzaVO> getElencoPolizze(long idAzienda, Integer annoCampagna, Long idIntervento)
    throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<PolizzaVO> results = null;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[PolizzaGaaDAO::getElencoPolizze] BEGIN.");
  
      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT PA.ID_POLIZZA_ASSICURATIVA, " +
          "       PA.NUMERO_POLIZZA, " +
          "       PA.ANNO_CAMPAGNA, " +
          "       TI.CODICE_INTERVENTO, " +
          "       TI.DESCRIZIONE AS DESC_INTERVENTO, " +
          "       CA.CODICE_COMPAGNIA, " +
          "       CA.DENOMINAZIONE AS DEN_COMPAGNIA_ASSICURATRICE, " +
          "       CD.CODICE_CONSORZIO, " +
          "       CD.DESCRIZIONE AS DESCRIZIONE_CONSORZIO, " +
          "       PA.DATA_POLIZZA, " +
          "       PA.DATA_QUIETANZA, " +
          "       TP.DESCRIZIONE AS DESC_PERIODO, " +
          "       PA.POLIZZA_INTEGRATIVA, " +
          "       PA.AGGIUNTIVA " +
          "FROM   DB_POLIZZA_ASSICURATIVA PA, " +
          "       DB_TIPO_INTERVENTO TI, " +
          "       DB_COMPAGNIA_ASSICURATRICE CA, " +
          "       DB_CONSORZIO_DIFESA CD, " +
          "       DB_TIPO_PERIODO TP " +
          "WHERE  PA.ID_AZIENDA = ? " +
          "AND    PA.ID_TIPO_INTERVENTO = TI.ID_TIPO_INTERVENTO " +
          "AND    PA.ID_COMPAGNIA_ASSICURATRICE = CA.ID_COMPAGNIA_ASSICURATRICE " +
          "AND    PA.ID_TIPO_PERIODO = TP.ID_TIPO_PERIODO " +
          "AND    PA.ID_CONSORZIO_DIFESA = CD.ID_CONSORZIO_DIFESA (+)");
      
      if(Validator.isNotEmpty(annoCampagna))
      {
        queryBuf.append("" +
          "AND    PA.ANNO_CAMPAGNA = ? ");
      }
      
      if(Validator.isNotEmpty(idIntervento))
      {
        queryBuf.append("" +
          "AND    PA.ID_TIPO_INTERVENTO = ? ");
      }
      
      queryBuf.append("" +
          "ORDER BY " +
          "       PA.ANNO_CAMPAGNA DESC," +
          "       PA.NUMERO_POLIZZA ASC," +
          "       PA.ID_POLIZZA_ASSICURATIVA ASC ");
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
                "[PolizzaGaaDAO::getElencoPolizze] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idAzienda);
      
      if(Validator.isNotEmpty(annoCampagna))
      {
        stmt.setInt(++idx, annoCampagna.intValue());
      }
      
      if(Validator.isNotEmpty(idIntervento))
      {
        stmt.setLong(++idx, idIntervento.longValue());
      }
      
      
      rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(results == null)
        {
          results = new Vector<PolizzaVO>();
        }
        long idPolizzaAssicurativa = rs.getLong("ID_POLIZZA_ASSICURATIVA");
        
        PolizzaVO polizzaVO = new PolizzaVO();
        polizzaVO.setIdPolizzaAssicurativa(idPolizzaAssicurativa);
        polizzaVO.setNumeroPolizza(rs.getString("NUMERO_POLIZZA"));
        polizzaVO.setAnnoCampagna(rs.getInt("ANNO_CAMPAGNA"));
        polizzaVO.setCodiceIntervento(rs.getString("CODICE_INTERVENTO"));
        polizzaVO.setDescrizioneIntervento(rs.getString("DESC_INTERVENTO"));
        polizzaVO.setCodiceCompagnia(rs.getString("CODICE_COMPAGNIA"));
        polizzaVO.setDenominazioneCompagnia(rs.getString("DEN_COMPAGNIA_ASSICURATRICE"));
        polizzaVO.setCodiceConsorzio(rs.getString("CODICE_CONSORZIO"));
        polizzaVO.setDescrizioneConsorzio(rs.getString("DESCRIZIONE_CONSORZIO"));
        Vector<BigDecimal> vSum = getSumValAss_ImpPre_ValRis(idPolizzaAssicurativa);
        polizzaVO.setValoreAssicurato(vSum.get(0));
        polizzaVO.setImportoPremio(vSum.get(1));
        polizzaVO.setValoreRisarcito(vSum.get(2));
        polizzaVO.setImportoPagato(getImportoPagato(idPolizzaAssicurativa));
        polizzaVO.setDataPolizza(rs.getTimestamp("DATA_POLIZZA"));
        polizzaVO.setDataQuietanza(rs.getTimestamp("DATA_QUIETANZA"));
        polizzaVO.setDescrizionePeriodo(rs.getString("DESC_PERIODO"));
        polizzaVO.setvMacroUso(getMacroUso(idPolizzaAssicurativa));
        polizzaVO.setPolizzaIntegrativa(rs.getString("POLIZZA_INTEGRATIVA"));
        polizzaVO.setAggiuntiva(rs.getString("AGGIUNTIVA"));
        
        results.add(polizzaVO);
      }
      
      
      return results;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("results", results), new Variabile("idx", idx) };
      LoggerUtils.logDAOError(this,
          "[PolizzaGaaDAO::getElencoPolizze] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[PolizzaGaaDAO::getElencoPolizze] END.");
    }
  }
  
  
  
  /**
   * 
   * restituisce il dettaglio della polizza.
   * Essendo  in join con db_Dettaglio_polizza
   * potrebbero uscire più occorrenze, ma dato che pe ri campi restituiti
   * sono identiche prendo solo la prima!!!
   * 
   * 
   * @param idPolizzaAssicurativa
   * @return
   * @throws DataAccessException
   */
  public PolizzaVO getDettaglioPolizza(long idPolizzaAssicurativa)
    throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    PolizzaVO results = null;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[PolizzaGaaDAO::getDettaglioPolizza] BEGIN.");
  
      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT PA.ID_POLIZZA_ASSICURATIVA, " +
          "       PA.NUMERO_POLIZZA, " +
          "       PA.ANNO_CAMPAGNA, " +
          "       DP.TIPO_DETTAGLIO, " +
          "       TI.CODICE_INTERVENTO, " +
          "       TI.DESCRIZIONE AS DESC_INTERVENTO, " +
          "       CA.CODICE_COMPAGNIA, " +
          "       CA.DENOMINAZIONE AS DEN_COMPAGNIA_ASSICURATRICE, " +
          "       CD.CODICE_CONSORZIO, " +
          "       CD.DESCRIZIONE AS DESCRIZIONE_CONSORZIO, " +
          "       PA.CONTRIBUTO_CONSORZIO, " +
          "       PA.DATA_POLIZZA, " +
          "       PA.DATA_QUIETANZA, " +
          "       TP.DESCRIZIONE AS DESC_PERIODO, " +
          "       PA.DATA_INFORMATIZZAZIONE, " +
          "       PA.GIORNI_FUORI_TERMINE, " +
          "       PA.POLIZZA_INTEGRATIVA, " +
          "       PA.AGGIUNTIVA " +
          "FROM   DB_POLIZZA_ASSICURATIVA PA, " +
          "       DB_DETTAGLIO_POLIZZA DP, " +
          "       DB_TIPO_INTERVENTO TI, " +
          "       DB_COMPAGNIA_ASSICURATRICE CA, " +
          "       DB_CONSORZIO_DIFESA CD, " +
          "       DB_TIPO_PERIODO TP " +
          "WHERE  PA.ID_POLIZZA_ASSICURATIVA = ? " +
          "AND    PA.ID_POLIZZA_ASSICURATIVA = DP.ID_POLIZZA_ASSICURATIVA " +
          "AND    PA.ID_TIPO_INTERVENTO = TI.ID_TIPO_INTERVENTO " +
          "AND    PA.ID_COMPAGNIA_ASSICURATRICE = CA.ID_COMPAGNIA_ASSICURATRICE " +
          "AND    PA.ID_TIPO_PERIODO = TP.ID_TIPO_PERIODO " +
          "AND    PA.ID_CONSORZIO_DIFESA = CD.ID_CONSORZIO_DIFESA (+)" +
          "AND    ROWNUM = 1"); //Prendo solo un record poichei campi restituiti in questa query
                                //sono tutti uguali
      
     
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
                "[PolizzaGaaDAO::getDettaglioPolizza] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idPolizzaAssicurativa);
      
      
      
      rs = stmt.executeQuery();
      
      if(rs.next())
      {
        results = new PolizzaVO();
        results.setIdPolizzaAssicurativa(rs.getLong("ID_POLIZZA_ASSICURATIVA"));
        results.setNumeroPolizza(rs.getString("NUMERO_POLIZZA"));
        results.setAnnoCampagna(rs.getInt("ANNO_CAMPAGNA"));
        results.setTipoPolizza(rs.getString("TIPO_DETTAGLIO"));
        results.setCodiceIntervento(rs.getString("CODICE_INTERVENTO"));
        results.setDescrizioneIntervento(rs.getString("DESC_INTERVENTO"));
        results.setCodiceCompagnia(rs.getString("CODICE_COMPAGNIA"));
        results.setDenominazioneCompagnia(rs.getString("DEN_COMPAGNIA_ASSICURATRICE"));
        results.setCodiceConsorzio(rs.getString("CODICE_CONSORZIO"));
        results.setDescrizioneConsorzio(rs.getString("DESCRIZIONE_CONSORZIO"));
        results.setContributoConsorzio(rs.getString("CONTRIBUTO_CONSORZIO"));
        results.setDataPolizza(rs.getTimestamp("DATA_POLIZZA"));
        results.setDataQuietanza(rs.getTimestamp("DATA_QUIETANZA"));
        results.setDescrizionePeriodo(rs.getString("DESC_PERIODO"));
        results.setDataInformatizzazione(rs.getTimestamp("DATA_INFORMATIZZAZIONE"));
        results.setGiorniFuoriTermine(rs.getBigDecimal("GIORNI_FUORI_TERMINE"));
        results.setPolizzaIntegrativa(rs.getString("POLIZZA_INTEGRATIVA"));
        results.setAggiuntiva(rs.getString("AGGIUNTIVA"));
        
      }
      
      
      return results;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idPolizzaAssicurativa", idPolizzaAssicurativa) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("results", results), new Variabile("idx", idx) };
      LoggerUtils.logDAOError(this,
          "[PolizzaGaaDAO::getDettaglioPolizza] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[PolizzaGaaDAO::getDettaglioPolizza] END.");
    }
  }
  
  /**
   * 
   * Restituisce i dati del dettaglio per la polizza di tipo coltura.
   * Dati presi da DB_DETTAGLIO_POLIZZA_COLTURA e DB_DETTAGLIO_POLIZZA
   * 
   * 
   * 
   * @param idPolizzaAssicurativa
   * @return
   * @throws DataAccessException
   */
  public TreeMap<Long,Vector<PolizzaDettaglioVO>> getDettaglioPolizzaColtura(long idPolizzaAssicurativa)
    throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    TreeMap<Long,Vector<PolizzaDettaglioVO>> results = null;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[PolizzaGaaDAO::getDettaglioPolizzaColtura] BEGIN.");
  
      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT DP.ID_DETTAGLIO_POLIZZA, " +
          "       COM.DESCOM, " +
          "       PROV.SIGLA_PROVINCIA, " +
          "       TMU.CODICE AS CODICE_MACROUSO, " +
          "       TMU.DESCRIZIONE AS DESC_MACROUSO, " +
          "       TP.CODICE_PRODOTTO, " +
          "       TP.DESCRIZIONE AS DESC_PRODOTTO, " +
          "       DPC.PERCENTUALE_FRANCHIGIA, " +
          "       DP.PARAMETRO_MIPAAF, " +
          "       DP.SPESA_PARAMETRATA, " +
          "       DP.SPESA_AMMESSA, " +
          "       DP.SUPERFICIE_UTILIZZATA, " +
          "       DPC.SUPERFICIE_ASSICURATA, " +
          "       DPC.QUANTITA_ASSICURATA, " +
          "       DPC.UNITA_MISURA, " +
          "       DPC.VALORE_ASSICURATO, " +
          "       DPC.TASSO_APPLICATO, " +
          "       DPC.IMPORTO_PREMIO, " +
          "       DP.IMPORTO_PROPOSTO, " +
          "       DP.IMPORTO_PAGATO, " +
          "       DPC.ID_DETTAGLIO_POLIZZA_COLTURA, " +
          "       DPC.SUPERFICIE_DANNEGGIATA, " +
          "       DPC.QUANTITA_DANNEGGIATA, " +
          "       DPC.SUPERFICIE_RISARCITA, " +
          "       DPC.QUANTITA_RISARCITA, " +
          "       DPC.VALORE_RISARCITO, " +
          "       DPC.DATA_INIZIO_COPERTURA," +
          "       DPC.DATA_FINE_COPERTURA, " +
          "       DPC.PROGR_RACCOLTO, " +
          "       DPC.EXTRA_RESA " +
          //"       DP.ANOMALIA " +
          "FROM   DB_DETTAGLIO_POLIZZA DP, " +
          "       DB_DETTAGLIO_POLIZZA_COLTURA DPC, " +
          "       COMUNE COM, " +
          "       PROVINCIA PROV, " +
          "       DB_TIPO_MACRO_USO TMU, " +
          "       DB_TIPO_PRODOTTO TP " +
          "WHERE  DP.ID_POLIZZA_ASSICURATIVA = ? " +
          "AND    DP.ID_DETTAGLIO_POLIZZA = DPC.ID_DETTAGLIO_POLIZZA " +
          "AND    DPC.ISTAT_COMUNE = COM.ISTAT_COMUNE " +
          "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +
          "AND    DPC.ID_MACRO_USO = TMU.ID_MACRO_USO " +
          "AND    DPC.ID_TIPO_PRODOTTO = TP.ID_TIPO_PRODOTTO " +
          "ORDER BY " +
          "       DP.ID_DETTAGLIO_POLIZZA " );
          
      
     
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
                "[PolizzaGaaDAO::getDettaglioPolizzaColtura] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idPolizzaAssicurativa);
      
      
      
      rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(results == null)
        {
          results = new TreeMap<Long, Vector<PolizzaDettaglioVO>>();
        }
        
        PolizzaDettaglioVO polVO = new PolizzaDettaglioVO();
        polVO.setIdDettaglioPolizza(rs.getLong("ID_DETTAGLIO_POLIZZA"));
        Long idDettaglioPolizza = new Long(polVO.getIdDettaglioPolizza());
        polVO.setDescComune(rs.getString("DESCOM"));
        polVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));
        polVO.setCodMacroUso(rs.getString("CODICE_MACROUSO"));
        polVO.setDescMacroUso(rs.getString("DESC_MACROUSO"));
        polVO.setCodProdotto(rs.getString("CODICE_PRODOTTO"));
        polVO.setDescProdotto(rs.getString("DESC_PRODOTTO"));
        polVO.setPercentualeFranchigia(rs.getInt("PERCENTUALE_FRANCHIGIA"));
        polVO.setParametroMipaaf(rs.getBigDecimal("PARAMETRO_MIPAAF"));
        polVO.setSpesaParametrata(rs.getBigDecimal("SPESA_PARAMETRATA"));
        polVO.setSpesaAmmessa(rs.getBigDecimal("SPESA_AMMESSA"));
        polVO.setSuperficieUtilizzata(rs.getBigDecimal("SUPERFICIE_UTILIZZATA"));
        polVO.setSuperficieAssicurata(rs.getBigDecimal("SUPERFICIE_ASSICURATA"));
        polVO.setQuantitaAssicurata(rs.getBigDecimal("QUANTITA_ASSICURATA"));
        polVO.setUnitaMisura(rs.getString("UNITA_MISURA"));
        polVO.setValoreAssicurato(rs.getBigDecimal("VALORE_ASSICURATO"));
        polVO.setTassoApplicato(rs.getBigDecimal("TASSO_APPLICATO"));
        polVO.setImportoPremio(rs.getBigDecimal("IMPORTO_PREMIO"));
        polVO.setImportoProposto(rs.getBigDecimal("IMPORTO_PROPOSTO"));
        polVO.setImportoPagato(rs.getBigDecimal("IMPORTO_PAGATO"));
        
        long idDetttaglioPolizzaColtura = rs.getLong("ID_DETTAGLIO_POLIZZA_COLTURA");
        polVO.setVDescGaranzia(getDescGaranziaColtura(idDetttaglioPolizzaColtura));
        
        polVO.setSuperficieDanneggiata(rs.getBigDecimal("SUPERFICIE_DANNEGGIATA"));
        polVO.setQuantitaDanneggiata(rs.getBigDecimal("QUANTITA_DANNEGGIATA"));
        polVO.setSuperficieRisarcita(rs.getBigDecimal("SUPERFICIE_RISARCITA"));
        polVO.setQuantitaRisarcita(rs.getBigDecimal("QUANTITA_RISARCITA"));
        polVO.setValoreRisarcito(rs.getBigDecimal("VALORE_RISARCITO"));
        polVO.setDataInizioCopertura(rs.getTimestamp("DATA_INIZIO_COPERTURA"));
        polVO.setDataFineCopertura(rs.getTimestamp("DATA_FINE_COPERTURA"));
        //polVO.setAnomalia(rs.getString("ANOMALIA"));
        polVO.setvAnomalia(getDescAnomalia(idDettaglioPolizza));
        polVO.setProgrRaccolto(rs.getBigDecimal("PROGR_RACCOLTO"));
        polVO.setExtraResa(rs.getString("EXTRA_RESA"));
        
        
        Vector<PolizzaDettaglioVO> vPolizzaDettaglio = null;
        if(results.get(idDettaglioPolizza) != null)
        {
          vPolizzaDettaglio = results.get(idDettaglioPolizza);
          vPolizzaDettaglio.add(polVO);
        }
        else
        {
          vPolizzaDettaglio = new Vector<PolizzaDettaglioVO>();
          vPolizzaDettaglio.add(polVO);
        }
        
        
        
        
        
        
        results.put(idDettaglioPolizza, vPolizzaDettaglio);
        
      }
      
      
      return results;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idPolizzaAssicurativa", idPolizzaAssicurativa) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("results", results), new Variabile("idx", idx) };
      LoggerUtils.logDAOError(this,
          "[PolizzaGaaDAO::getDettaglioPolizzaColtura] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[PolizzaGaaDAO::getDettaglioPolizzaColtura] END.");
    }
  }
  
  
  /**
   * 
   * Recupera su DB_POLIZZA_COLTURA_GARANZIA la descrizione della garanzia
   * per le polizze colture
   * 
   * @param idDettaglioPolizzaColtura
   * @return
   * @throws DataAccessException
   */
  private Vector<String> getDescGaranziaColtura(long idDettaglioPolizzaColtura) 
    throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<String> results = null;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[PolizzaGaaDAO::getDescGaranziaColtura] BEGIN.");
  
      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT TG.DESCRIZIONE " +
          "FROM   DB_POLIZZA_COLTURA_GARANZIA PCG," +
          "       DB_TIPO_GARANZIA TG " +
          "WHERE  PCG.ID_DETTAGLIO_POLIZZA_COLTURA = ? " +
          "AND    PCG.ID_TIPO_GARANZIA = TG.ID_TIPO_GARANZIA " +
          "ORDER BY" +
          "       TG.DESCRIZIONE ");
         
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
                "[PolizzaGaaDAO::getDescGaranziaColtura] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idDettaglioPolizzaColtura);
      
      rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(results == null)
        {
          results = new Vector<String>();
        }
        
        results.add(rs.getString("DESCRIZIONE"));
      }
      
      
      return results;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDettaglioPolizzaColtura", idDettaglioPolizzaColtura) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("results", results), new Variabile("idx", idx)
  
      };
      LoggerUtils.logDAOError(this,
          "[PolizzaGaaDAO::getDescGaranziaColtura] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[PolizzaGaaDAO::getDescGaranziaColtura] END.");
    }
  }
  
  
  /**
   * 
   * Restituisce i dati del dettaglio per la polizza di tipo struttura.
   * Dati presi da DB_DETTAGLIO_POLIZZA_STRUTTURA e DB_DETTAGLIO_POLIZZA
   * 
   * 
   * @param idPolizzaAssicurativa
   * @return
   * @throws DataAccessException
   */
  public TreeMap<Long,Vector<PolizzaDettaglioVO>> getDettaglioPolizzaStruttura(long idPolizzaAssicurativa)
    throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    TreeMap<Long,Vector<PolizzaDettaglioVO>> results = null;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[PolizzaGaaDAO::getDettaglioPolizzaStruttura] BEGIN.");
  
      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT DP.ID_DETTAGLIO_POLIZZA, " +
          "       COM.DESCOM, " +
          "       PROV.SIGLA_PROVINCIA, " +
          "       TP.CODICE_PRODOTTO, " +
          "       TP.DESCRIZIONE AS DESC_PRODOTTO, " +
          "       DPS.PERCENTUALE_FRANCHIGIA, " +
          "       DP.PARAMETRO_MIPAAF, " +
          "       DP.SPESA_PARAMETRATA, " +
          "       DP.SPESA_AMMESSA, " +
          "       DP.SUPERFICIE_UTILIZZATA, " +
          "       DPS.SUPERFICIE_ASSICURATA, " +
          "       DPS.VALORE_ASSICURATO, " +
          "       DPS.TASSO_APPLICATO, " +
          "       DPS.IMPORTO_PREMIO, " +
          "       DP.IMPORTO_PROPOSTO, " +
          "       DP.IMPORTO_PAGATO, " +
          "       DPS.ID_DETTAGLIO_POLIZZA_STRUTTURA, " +
          "       DPS.SUPERFICIE_DANNEGGIATA, " +
          "       DPS.SUPERFICIE_RISARCITA, " +
          "       DPS.VALORE_RISARCITO, " +
          "       DPS.DATA_INIZIO_COPERTURA," +
          "       DPS.DATA_FINE_COPERTURA  " +
          //"       DP.ANOMALIA " +
          "FROM   DB_DETTAGLIO_POLIZZA DP, " +
          "       DB_DETTAGLIO_POLIZZA_STRUTTURA DPS, " +
          "       COMUNE COM, " +
          "       PROVINCIA PROV, " +
          "       DB_TIPO_PRODOTTO TP " +
          "WHERE  DP.ID_POLIZZA_ASSICURATIVA = ? " +
          "AND    DP.ID_DETTAGLIO_POLIZZA = DPS.ID_DETTAGLIO_POLIZZA " +
          "AND    DPS.ISTAT_COMUNE = COM.ISTAT_COMUNE " +
          "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +
          "AND    DPS.ID_TIPO_PRODOTTO = TP.ID_TIPO_PRODOTTO " +
          "ORDER BY " +
          "       DP.ID_DETTAGLIO_POLIZZA " );
          
      
     
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
                "[PolizzaGaaDAO::getDettaglioPolizzaStruttura] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idPolizzaAssicurativa);
      
      
      
      rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(results == null)
        {
          results = new TreeMap<Long, Vector<PolizzaDettaglioVO>>();
        }
        
        PolizzaDettaglioVO polVO = new PolizzaDettaglioVO();
        polVO.setIdDettaglioPolizza(rs.getLong("ID_DETTAGLIO_POLIZZA"));
        Long idDettaglioPolizza = new Long(polVO.getIdDettaglioPolizza());
        polVO.setDescComune(rs.getString("DESCOM"));
        polVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));
        polVO.setCodProdotto(rs.getString("CODICE_PRODOTTO"));
        polVO.setDescProdotto(rs.getString("DESC_PRODOTTO"));
        polVO.setPercentualeFranchigia(rs.getInt("PERCENTUALE_FRANCHIGIA"));
        polVO.setParametroMipaaf(rs.getBigDecimal("PARAMETRO_MIPAAF"));
        polVO.setSpesaParametrata(rs.getBigDecimal("SPESA_PARAMETRATA"));
        polVO.setSpesaAmmessa(rs.getBigDecimal("SPESA_AMMESSA"));
        polVO.setSuperficieUtilizzata(rs.getBigDecimal("SUPERFICIE_UTILIZZATA"));
        polVO.setSuperficieAssicurata(rs.getBigDecimal("SUPERFICIE_ASSICURATA"));
        polVO.setValoreAssicurato(rs.getBigDecimal("VALORE_ASSICURATO"));
        polVO.setTassoApplicato(rs.getBigDecimal("TASSO_APPLICATO"));
        polVO.setImportoPremio(rs.getBigDecimal("IMPORTO_PREMIO"));
        polVO.setImportoProposto(rs.getBigDecimal("IMPORTO_PROPOSTO"));
        polVO.setImportoPagato(rs.getBigDecimal("IMPORTO_PAGATO"));
        
        long idDetttaglioPolizzaStruttura = rs.getLong("ID_DETTAGLIO_POLIZZA_STRUTTURA");
        polVO.setVDescGaranzia(getDescGaranziaStruttura(idDetttaglioPolizzaStruttura));
        
        polVO.setSuperficieDanneggiata(rs.getBigDecimal("SUPERFICIE_DANNEGGIATA"));
        polVO.setSuperficieRisarcita(rs.getBigDecimal("SUPERFICIE_RISARCITA"));
        polVO.setValoreRisarcito(rs.getBigDecimal("VALORE_RISARCITO"));
        polVO.setDataInizioCopertura(rs.getTimestamp("DATA_INIZIO_COPERTURA"));
        polVO.setDataFineCopertura(rs.getTimestamp("DATA_FINE_COPERTURA"));
        //polVO.setAnomalia(rs.getString("ANOMALIA"));
        polVO.setVDescGaranzia(getDescAnomalia(idDettaglioPolizza));
        
        
        Vector<PolizzaDettaglioVO> vPolizzaDettaglio = null;
        if(results.get(idDettaglioPolizza) != null)
        {
          vPolizzaDettaglio = results.get(idDettaglioPolizza);
          vPolizzaDettaglio.add(polVO);
        }
        else
        {
          vPolizzaDettaglio = new Vector<PolizzaDettaglioVO>();
          vPolizzaDettaglio.add(polVO);
        }
        
        
        
        
        
        
        results.put(idDettaglioPolizza, vPolizzaDettaglio);
        
      }
      
      
      return results;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idPolizzaAssicurativa", idPolizzaAssicurativa) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("results", results), new Variabile("idx", idx) };
      LoggerUtils.logDAOError(this,
          "[PolizzaGaaDAO::getDettaglioPolizzaStruttura] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[PolizzaGaaDAO::getDettaglioPolizzaStruttura] END.");
    }
  }
  
  
  /**
   * 
   * Recupera su DB_POLIZZA_STRUTTURA_GARANZIA la descrizione della garanzia
   * per le polizze strutture
   * 
   * 
   * 
   * @param idDettaglioPolizzaStruttura
   * @return
   * @throws DataAccessException
   */
  private Vector<String> getDescGaranziaStruttura(long idDettaglioPolizzaStruttura) 
    throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<String> results = null;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[PolizzaGaaDAO::getDescGaranziaStruttura] BEGIN.");
  
      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT TG.DESCRIZIONE " +
          "FROM   DB_POLIZZA_STRUTTURA_GARANZIA PSG," +
          "       DB_TIPO_GARANZIA TG " +
          "WHERE  PSG.ID_DETTAGLIO_POLIZZA_STRUTTURA = ? " +
          "AND    PSG.ID_TIPO_GARANZIA = TG.ID_TIPO_GARANZIA " +
          "ORDER BY" +
          "       TG.DESCRIZIONE ");
         
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
                "[PolizzaGaaDAO::getDescGaranziaStruttura] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idDettaglioPolizzaStruttura);
      
      rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(results == null)
        {
          results = new Vector<String>();
        }
        
        results.add(rs.getString("DESCRIZIONE"));
      }
      
      
      return results;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDettaglioPolizzaStruttura", idDettaglioPolizzaStruttura) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("results", results), new Variabile("idx", idx)
  
      };
      LoggerUtils.logDAOError(this,
          "[PolizzaGaaDAO::getDescGaranziaStruttura] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[PolizzaGaaDAO::getDescGaranziaStruttura] END.");
    }
  }
  
  
  
  /**
   * 
   * Restituisce i dati del dettaglio per la polizza di tipo zootecnia.
   * Dati presi da DB_DETTAGLIO_POLIZZA_ZOOTECNIA e DB_DETTAGLIO_POLIZZA
   * 
   * @param idPolizzaAssicurativa
   * @return
   * @throws DataAccessException
   */
  public TreeMap<Long,Vector<PolizzaDettaglioVO>> getDettaglioPolizzaZootecnia(long idPolizzaAssicurativa)
    throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    TreeMap<Long,Vector<PolizzaDettaglioVO>> results = null;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[PolizzaGaaDAO::getDettaglioPolizzaZootecnia] BEGIN.");
  
      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT DP.ID_DETTAGLIO_POLIZZA, " +
          "       COM.DESCOM, " +
          "       PROV.SIGLA_PROVINCIA, " +
          "       DPZ.INDIRIZZO_ALLEVAMENTO, " +
          "       TP.CODICE_PRODOTTO, " +
          "       TP.DESCRIZIONE AS DESC_PRODOTTO, " +
          "       DPZ.CODICE_ASL, " +
          "       DP.PARAMETRO_MIPAAF, " +
          "       DP.SPESA_PARAMETRATA, " +
          "       DP.SPESA_AMMESSA, " +
          "       DPZ.PERCENTUALE_FRANCHIGIA, " +
          "       DPZ.QUANTITA_COPERTA_FRANCHIGIA, " +
          "       DPZ.QUANTITA_ASSICURATA, " +
          "       DPZ.VALORE_ASSICURATO, " +
          "       DPZ.TASSO_APPLICATO, " +
          "       DPZ.IMPORTO_PREMIO, " +
          "       DP.IMPORTO_PROPOSTO, " +
          "       DP.IMPORTO_PAGATO, " +
          "       DPZ.ID_DETTAGLIO_POLIZZA_ZOOTECNIA, " +
          "       DPZ.QUANTITA_RITIRATA, " +
          "       DPZ.QUANTITA_RISARCITA, " +
          "       DPZ.VALORE_RISARCITO, " +
          "       DPZ.DATA_INIZIO_COPERTURA," +
          "       DPZ.DATA_FINE_COPERTURA  " +
          //"       DP.ANOMALIA " +
          "FROM   DB_DETTAGLIO_POLIZZA DP, " +
          "       DB_DETTAGLIO_POLIZZA_ZOOTECNIA DPZ, " +
          "       COMUNE COM, " +
          "       PROVINCIA PROV, " +
          "       DB_TIPO_PRODOTTO TP " +
          "WHERE  DP.ID_POLIZZA_ASSICURATIVA = ? " +
          "AND    DP.ID_DETTAGLIO_POLIZZA = DPZ.ID_DETTAGLIO_POLIZZA " +
          "AND    DPZ.ISTAT_COMUNE = COM.ISTAT_COMUNE " +
          "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +
          "AND    DPZ.ID_TIPO_PRODOTTO = TP.ID_TIPO_PRODOTTO " +
          "ORDER BY " +
          "       DP.ID_DETTAGLIO_POLIZZA " );
          
      
     
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
                "[PolizzaGaaDAO::getDettaglioPolizzaZootecnia] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idPolizzaAssicurativa);
      
      
      
      rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(results == null)
        {
          results = new TreeMap<Long, Vector<PolizzaDettaglioVO>>();
        }
        
        PolizzaDettaglioVO polVO = new PolizzaDettaglioVO();
        polVO.setIdDettaglioPolizza(rs.getLong("ID_DETTAGLIO_POLIZZA"));
        Long idDettaglioPolizza = new Long(polVO.getIdDettaglioPolizza());
        polVO.setDescComune(rs.getString("DESCOM"));
        polVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));
        polVO.setIndirizzo(rs.getString("INDIRIZZO_ALLEVAMENTO"));
        polVO.setCodProdotto(rs.getString("CODICE_PRODOTTO"));
        polVO.setDescProdotto(rs.getString("DESC_PRODOTTO"));
        polVO.setCodiceAsl(rs.getString("CODICE_ASL"));
        polVO.setParametroMipaaf(rs.getBigDecimal("PARAMETRO_MIPAAF"));
        polVO.setSpesaParametrata(rs.getBigDecimal("SPESA_PARAMETRATA"));
        polVO.setSpesaAmmessa(rs.getBigDecimal("SPESA_AMMESSA"));
        polVO.setPercentualeFranchigia(rs.getInt("PERCENTUALE_FRANCHIGIA"));
        polVO.setQuantitaCopertaFranchigia(rs.getBigDecimal("QUANTITA_COPERTA_FRANCHIGIA"));
        polVO.setQuantitaAssicurata(rs.getBigDecimal("QUANTITA_ASSICURATA"));
        polVO.setValoreAssicurato(rs.getBigDecimal("VALORE_ASSICURATO"));
        polVO.setTassoApplicato(rs.getBigDecimal("TASSO_APPLICATO"));
        polVO.setImportoPremio(rs.getBigDecimal("IMPORTO_PREMIO"));
        polVO.setImportoProposto(rs.getBigDecimal("IMPORTO_PROPOSTO"));
        polVO.setImportoPagato(rs.getBigDecimal("IMPORTO_PAGATO"));
        
        long idDetttaglioPolizzaZootecnia = rs.getLong("ID_DETTAGLIO_POLIZZA_ZOOTECNIA");
        polVO.setVDescGaranzia(getDescGaranziaZootecnia(idDetttaglioPolizzaZootecnia));
        polVO.setVDescEpizoozia(getDescEpizooziaZootecnia(idDetttaglioPolizzaZootecnia));
        
        polVO.setQuantitaRitirata(rs.getBigDecimal("QUANTITA_RITIRATA"));
        polVO.setQuantitaRisarcita(rs.getBigDecimal("QUANTITA_RISARCITA"));
        polVO.setValoreRisarcito(rs.getBigDecimal("VALORE_RISARCITO"));
        polVO.setDataInizioCopertura(rs.getTimestamp("DATA_INIZIO_COPERTURA"));
        polVO.setDataFineCopertura(rs.getTimestamp("DATA_FINE_COPERTURA"));
        //polVO.setAnomalia(rs.getString("ANOMALIA"));
        polVO.setvAnomalia(getDescAnomalia(idDettaglioPolizza));
        
        
        Vector<PolizzaDettaglioVO> vPolizzaDettaglio = null;
        if(results.get(idDettaglioPolizza) != null)
        {
          vPolizzaDettaglio = results.get(idDettaglioPolizza);
          vPolizzaDettaglio.add(polVO);
        }
        else
        {
          vPolizzaDettaglio = new Vector<PolizzaDettaglioVO>();
          vPolizzaDettaglio.add(polVO);
        }
        
        
        
        
        
        
        results.put(idDettaglioPolizza, vPolizzaDettaglio);
        
      }
      
      
      return results;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idPolizzaAssicurativa", idPolizzaAssicurativa) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("results", results), new Variabile("idx", idx) };
      LoggerUtils.logDAOError(this,
          "[PolizzaGaaDAO::getDettaglioPolizzaZootecnia] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[PolizzaGaaDAO::getDettaglioPolizzaZootecnia] END.");
    }
  }
  
  
  
  /**
   * 
   * Recupera su DB_POLIZZA_ZOOTECNIA_GARANZIA la descrizione della garanzia
   * per le polizze zootecnia
   * 
   * 
   * 
   * @param idDettaglioPolizzaZootecnia
   * @return
   * @throws DataAccessException
   */
  private Vector<String> getDescGaranziaZootecnia(long idDettaglioPolizzaZootecnia) 
    throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<String> results = null;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[PolizzaGaaDAO::getDescGaranziaZootecnia] BEGIN.");
  
      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT TG.DESCRIZIONE " +
          "FROM   DB_POLIZZA_ZOOTECNIA_GARANZIA PZG," +
          "       DB_TIPO_GARANZIA TG " +
          "WHERE  PZG.ID_DETTAGLIO_POLIZZA_ZOOTECNIA = ? " +
          "AND    PZG.ID_TIPO_GARANZIA = TG.ID_TIPO_GARANZIA " +
          "ORDER BY" +
          "       TG.DESCRIZIONE ");
         
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
                "[PolizzaGaaDAO::getDescGaranziaZootecnia] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idDettaglioPolizzaZootecnia);
      
      rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(results == null)
        {
          results = new Vector<String>();
        }
        
        results.add(rs.getString("DESCRIZIONE"));
      }
      
      
      return results;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDettaglioPolizzaZootecnia", idDettaglioPolizzaZootecnia) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("results", results), new Variabile("idx", idx)
  
      };
      LoggerUtils.logDAOError(this,
          "[PolizzaGaaDAO::getDescGaranziaZootecnia] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[PolizzaGaaDAO::getDescGaranziaZootecnia] END.");
    }
  }
  
  
  
  /**
   * 
   * Recupera su DB_POLIZZA_ZOOTECNIA_EPIZOOZIA la descrizione della epizoozia
   * per le polizze zootecnia
   * 
   * 
   * 
   * @param idDettaglioPolizzaZootecnia
   * @return
   * @throws DataAccessException
   */
  private Vector<String> getDescEpizooziaZootecnia(long idDettaglioPolizzaZootecnia) 
    throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<String> results = null;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[PolizzaGaaDAO::getDescEpizooziaZootecnia] BEGIN.");
  
      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT TE.DESCRIZIONE " +
          "FROM   DB_POLIZZA_ZOOTECNIA_EPIZOOZIA PZE," +
          "       DB_TIPO_EPIZOOZIA TE " +
          "WHERE  PZE.ID_DETTAGLIO_POLIZZA_ZOOTECNIA = ? " +
          "AND    PZE.ID_TIPO_EPIZOOZIA = TE.ID_TIPO_EPIZOOZIA " +
          "ORDER BY" +
          "       TE.DESCRIZIONE ");
         
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
                "[PolizzaGaaDAO::getDescEpizooziaZootecnia] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idDettaglioPolizzaZootecnia);
      
      rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(results == null)
        {
          results = new Vector<String>();
        }
        
        results.add(rs.getString("DESCRIZIONE"));
      }
      
      
      return results;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDettaglioPolizzaZootecnia", idDettaglioPolizzaZootecnia) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("results", results), new Variabile("idx", idx)
  
      };
      LoggerUtils.logDAOError(this,
          "[PolizzaGaaDAO::getDescEpizooziaZootecnia] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[PolizzaGaaDAO::getDescEpizooziaZootecnia] END.");
    }
  }
  
  
  private Vector<String> getDescAnomalia(long idDettaglioPolizza) 
    throws DataAccessException
  {
    Connection conn = null;
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<String> results = null;
    int idx = 0;
    try
    {
      SolmrLogger
      .debug(
          this,
          "[PolizzaGaaDAO::getDescAnomalia] BEGIN.");
  
      queryBuf = new StringBuffer();
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
          "SELECT DISTINCT AP.DESCRIZIONE " +
          "FROM   DB_ANOMALIA_POLIZZA AP " +
          "WHERE  AP.ID_DETTAGLIO_POLIZZA = ? " +
          "ORDER BY" +
          "       AP.DESCRIZIONE ");
         
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
                "[PolizzaGaaDAO::getDescAnomalia] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(++idx, idDettaglioPolizza);
      
      rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(results == null)
        {
          results = new Vector<String>();
        }
        
        results.add(rs.getString("DESCRIZIONE"));
      }
      
      
      return results;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDettaglioPolizza", idDettaglioPolizza) };
      Variabile variabili[] = new Variabile[]
      { new Variabile("results", results), new Variabile("idx", idx)
  
      };
      LoggerUtils.logDAOError(this,
          "[PolizzaGaaDAO::getDescAnomalia] ", t, query,
          variabili, parametri);
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      close(rs, stmt, conn);
      SolmrLogger
          .debug(this, "[PolizzaGaaDAO::getDescAnomalia] END.");
    }
  }
  
  
  
  
  
  
  
  
  
  
}
