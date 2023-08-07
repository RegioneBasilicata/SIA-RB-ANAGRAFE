package it.csi.smranag.smrgaa.integration.terreni;

import it.csi.smranag.smrgaa.dto.BaseCodeDescription;
import it.csi.smranag.smrgaa.dto.ParticellaDettaglioValidazioniVO;
import it.csi.smranag.smrgaa.dto.PlSqlCodeDescription;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.dto.search.FiltriRicercaTerrenoImportaAsservimentoVO;
import it.csi.smranag.smrgaa.dto.search.FiltriRicercaTerrenoVO;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaTerreniImportaAsservimentoVO;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaTerreniVO;
import it.csi.smranag.smrgaa.dto.terreni.CasoParticolareVO;
import it.csi.smranag.smrgaa.dto.terreni.IstanzaRiesameVO;
import it.csi.smranag.smrgaa.dto.terreni.ParticellaBioVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoAreaVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoDestinazioneVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoDettaglioUsoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoEfaVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoEsportazioneDatiVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoFonteVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoMetodoIrriguoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoPeriodoSeminaVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoQualitaUsoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoRiepilogoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoValoreAreaVO;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.ProvinciaVO;
import it.csi.solmr.dto.UtenteIrideVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneDichiarataVO;
import it.csi.solmr.dto.anag.terreni.FiltriParticellareRicercaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.dto.anag.terreni.StoricoUnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.TipoIrrigazioneVO;
import it.csi.solmr.dto.anag.terreni.TipoPotenzialitaIrriguaVO;
import it.csi.solmr.dto.anag.terreni.TipoRotazioneColturaleVO;
import it.csi.solmr.dto.anag.terreni.TipoTerrazzamentoVO;
import it.csi.solmr.dto.anag.terreni.TipoUtilizzoVO;
import it.csi.solmr.dto.anag.terreni.TipoVarietaVO;
import it.csi.solmr.dto.anag.terreni.UtilizzoDichiaratoVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

public class ParticellaGaaDAO extends it.csi.solmr.integration.BaseDAO
{

  public ParticellaGaaDAO() throws ResourceAccessException
  {
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public ParticellaGaaDAO(String refName) throws ResourceAccessException
  {
    super(refName);
  }

  /**
   * Restituisce i dati della ricerca terreni di cui si conoscono gli id ( dati
   * ricercaIdStoricoParticella)
   * 
   * @param ids
   * @return
   * @throws DataAccessException
   */
  public RigaRicercaTerreniVO[] getRigheRicercaTerreniByIdParticellaRange(
      long ids[]) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    RigaRicercaTerreniVO rigaRicercaTerreniVO = null;
    RigaRicercaTerreniVO righe[] = null;
    HashMap<Long,RigaRicercaTerreniVO> hmRighe = null;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::getRigheRicercaTerreniByIdParticellaRange] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
                "SELECT   " 
              + "     SP.ID_PARTICELLA,  "
              + "     P.SIGLA_PROVINCIA,   " 
              + "     C.DESCOM,  "
              + "     C.ISTAT_COMUNE,  " 
              + "     SP.SEZIONE,  "
              + "     SP.FOGLIO,   " 
              + "     SP.PARTICELLA,   "
              + "     SP.SUBALTERNO,   " 
              + "     SP.SUP_CATASTALE,  "
              + "     SP.SUPERFICIE_GRAFICA,  "
              + "     PA.DATA_CREAZIONE,   " 
              + "     PA.DATA_CESSAZIONE,   "
              + "     C.FLAG_ESTERO " 
              + "     FROM   "
              + "     DB_STORICO_PARTICELLA SP,    "
              + "     DB_PARTICELLA PA,  " + "     COMUNE C,    "
              + "     PROVINCIA P  " + "   WHERE  "
              + "     SP.ID_PARTICELLA = PA.ID_PARTICELLA  "
              + "     AND C.ISTAT_COMUNE = SP.COMUNE   "
              + "     AND SP.DATA_FINE_VALIDITA IS NULL "
              + "     AND C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA  "
              + "     AND SP.ID_PARTICELLA IN ( ").append(
          this.getIdListFromArrayForSQL(ids)).append(")");
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getRigheRicercaTerreniByIdParticellaRange] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      ResultSet rs = stmt.executeQuery();
      hmRighe = new HashMap<Long,RigaRicercaTerreniVO>();
      while (rs.next())
      {
        rigaRicercaTerreniVO = new RigaRicercaTerreniVO();
        rigaRicercaTerreniVO.setDataCessazione(rs.getDate("DATA_CESSAZIONE"));
        rigaRicercaTerreniVO.setDataInizioDecorrenza(rs
            .getDate("DATA_CREAZIONE"));
        rigaRicercaTerreniVO.setDescrizioneComune(rs.getString("DESCOM"));
        rigaRicercaTerreniVO.setFoglio(rs.getLong("FOGLIO"));
        rigaRicercaTerreniVO.setIdParticella(rs.getLong("ID_PARTICELLA"));
        rigaRicercaTerreniVO.setIstatComune(rs.getString("ISTAT_COMUNE"));
        rigaRicercaTerreniVO
            .setParticella(checkLong(rs.getString("PARTICELLA")));
        rigaRicercaTerreniVO.setSezione(rs.getString("SEZIONE"));
        rigaRicercaTerreniVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));
        rigaRicercaTerreniVO.setSubalterno(rs.getString("SUBALTERNO"));
        rigaRicercaTerreniVO.setSupCatastale(rs.getBigDecimal("SUP_CATASTALE"));
        rigaRicercaTerreniVO.setSuperficieGrafica(rs.getBigDecimal("SUPERFICIE_GRAFICA"));
        rigaRicercaTerreniVO.setFlagEstero(rs.getString("FLAG_ESTERO"));
        hmRighe.put(new Long(rigaRicercaTerreniVO.getIdParticella()),
            rigaRicercaTerreniVO);
      }
      int size = hmRighe.size();
      if (size == 0)
      {
        return null;
      }
      else
      {
        righe = new RigaRicercaTerreniVO[size];
        for (int i = 0; i < size; ++i)
        {
          righe[i] = (RigaRicercaTerreniVO) hmRighe.get(new Long(ids[i]));
        }
        return righe;
      }
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("hmRighe", hmRighe),
          new Variabile("rigaRicercaTerreniVO", rigaRicercaTerreniVO),
          new Variabile("righe", righe) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("ids", ids) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getRigheRicercaTerreniByIdParticellaRange] ", t,
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
          "[ParticellaGaaDAO::getRigheRicercaTerreniByIdParticellaRange] END.");
    }
  }
  
  
  
  

  /**
   * Restituisce l'elenco degli id_storico_particella che soddisfano i filtri
   * della ricerca terreni
   * 
   * @param ids
   * @return
   * @throws DataAccessException
   */
  public long[] ricercaIdParticelleTerreni(
      FiltriRicercaTerrenoVO filtriRicercaTerrenoVO) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<Long> vIds = null;
    long ids[] = null;
    try
    {
      SolmrLogger.debug(this, "[ParticellaGaaDAO::ricercaTerreni] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf = new StringBuffer(" SELECT SP.ID_PARTICELLA "
          + " FROM   DB_PARTICELLA PARTICELLA, "
          + "        DB_STORICO_PARTICELLA SP, " + "        COMUNE C, "
          + "        PROVINCIA P " + " WHERE  SP.COMUNE = ? "
          + " AND    SP.COMUNE = C.ISTAT_COMUNE "
          + " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
          + " AND    SP.FOGLIO = ? "
          + " AND    SP.ID_PARTICELLA = PARTICELLA.ID_PARTICELLA "
          + " AND    SP.DATA_FINE_VALIDITA IS NULL ");

      // Se la sezione è valorizzata, ricerco anche per sezione
      if (Validator.isNotEmpty(filtriRicercaTerrenoVO.getSezione()))
      {
        queryBuf.append(" AND SP.SEZIONE = UPPER(?) ");
      }

      // Se l'utente ha selezionato il flag "particella provvisoria" allora
      // ricerco per particella
      // e subalterno uguali a null
      if (filtriRicercaTerrenoVO.isParticellaProvvisoria())
      {
        queryBuf.append(" AND SP.PARTICELLA IS NULL "
            + " AND SP.SUBALTERNO IS NULL ");
      }
      // Altrimenti
      else
      {
        // Se l'utente ha valorizzato la particella
        if (Validator.isNotEmpty(filtriRicercaTerrenoVO.getParticella()))
        {
          queryBuf.append(" AND SP.PARTICELLA = ? ");
        }
        // Se l'utente ha valorizzato il subalterno
        if (Validator.isNotEmpty(filtriRicercaTerrenoVO.getSubalterno()))
        {
          queryBuf.append(" AND UPPER(SP.SUBALTERNO) = ? ");
        }
      }

      // Se l'utente ha selezionato il flag "particella attiva"
      if (filtriRicercaTerrenoVO.isParticellaAttiva())
      {
        queryBuf.append(" AND PARTICELLA.DATA_CESSAZIONE IS NULL ");
      }

      queryBuf
          .append(" ORDER BY P.SIGLA_PROVINCIA, C.DESCOM, SP.SEZIONE, SP.FOGLIO, SP.PARTICELLA, "
              + "       SP.SUBALTERNO ");

      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[ParticellaGaaDAO::ricercaTerreni] Query="
            + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setString(++indice, filtriRicercaTerrenoVO
          .getIstatComuneParticella());
      stmt.setLong(++indice, new Long(filtriRicercaTerrenoVO.getFoglio())
          .longValue());
      if (Validator.isNotEmpty(filtriRicercaTerrenoVO.getSezione()))
      {
        stmt.setString(++indice, filtriRicercaTerrenoVO.getSezione()
            .toUpperCase());
      }
      if (Validator.isNotEmpty(filtriRicercaTerrenoVO.getParticella()))
      {
        stmt.setLong(++indice, new Long(filtriRicercaTerrenoVO.getParticella())
            .longValue());
      }
      if (Validator.isNotEmpty(filtriRicercaTerrenoVO.getSubalterno()))
      {
        stmt.setString(++indice, filtriRicercaTerrenoVO.getSubalterno()
            .toUpperCase());
      }

      ResultSet rs = stmt.executeQuery();
      vIds = new Vector<Long>();
      while (rs.next())
      {
        vIds.add(new Long(rs.getLong("ID_PARTICELLA")));
      }
      int size = vIds.size();
      if (size == 0)
      {
        return null;
      }
      else
      {
        ids = new long[size];
        for (int i = 0; i < size; ++i)
        {
          ids[i] = ((Long) vIds.get(i)).longValue();
        }
        return ids;
      }
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("vIds", vIds), new Variabile("ids", ids) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("filtriRicercaTerrenoVO", filtriRicercaTerrenoVO) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[ParticellaGaaDAO::ricercaTerreni] ", t,
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
      SolmrLogger.debug(this, "[ParticellaGaaDAO::ricercaTerreni] END.");
    }
  }

  /**
   * Restituisce l'elenco degli id_storico_particella legati ad una particella
   * 
   * @param idParticella
   * @return
   * @throws DataAccessException
   */
  public long[] getElencoIdStoricoParticellaByIdParticella(long idParticella)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<Long> vIds = null;
    long ids[] = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::getElencoIdStoricoParticellaByIdParticella] BEGIN.");

      query = " SELECT " + "   ID_STORICO_PARTICELLA " + " FROM "
          + "   DB_STORICO_PARTICELLA " + " WHERE ID_PARTICELLA = ?";
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      stmt.setLong(1, idParticella);

      ResultSet rs = stmt.executeQuery();
      vIds = new Vector<Long>();
      while (rs.next())
      {
        vIds.add(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
      }
      int size = vIds.size();
      if (size == 0)
      {
        return null;
      }
      else
      {
        ids = new long[size];
        for (int i = 0; i < size; ++i)
        {
          ids[i] = ((Long) vIds.get(i)).longValue();
        }
        return ids;
      }
    }
    catch (Exception t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("vIds", vIds),
          new Variabile("ids", ids) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idParticella", idParticella) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getElencoIdStoricoParticellaByIdParticella] ", t,
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
          "[ParticellaGaaDAO::getElencoIdStoricoParticellaByIdParticella] END.");
    }
  }
  
  
  /**
   * Restituisce l'elenco degli id_storico_particella legati ad array di particelle
   * 
   * @param idParticella
   * @return
   * @throws DataAccessException
   */
  public HashMap<Long,Long> getHashIdStoricoParticellaIdParticellaByIdParticella(long[] idParticella)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    HashMap<Long,Long> hCoppie = null;
    StringBuffer queryBuf = null;
    long ids[] = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::getHashIdStoricoParticellaIdParticellaByIdParticella] BEGIN.");
      
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT " +
        "       ID_STORICO_PARTICELLA, " +
        "       ID_PARTICELLA " + 
        "FROM " + 
        "       DB_STORICO_PARTICELLA " +
        "WHERE ID_PARTICELLA IN ( ")
      .append(this.getIdListFromArrayForSQL(idParticella)).append(")");
        
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
                "[ParticellaGaaDAO::getHashIdStoricoParticellaIdParticellaByIdParticella] Query="
                    + query);
      }
      
      stmt = conn.prepareStatement(query);
    

      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {
        if(hCoppie == null)
        {
          hCoppie = new HashMap<Long,Long>();
        }
        hCoppie.put(new Long(rs.getLong("ID_STORICO_PARTICELLA")),
            new Long(rs.getLong("ID_PARTICELLA")));
      }
      
      return hCoppie;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("hCoppie", hCoppie),
          new Variabile("ids", ids) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idParticella", idParticella) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getHashIdStoricoParticellaIdParticellaByIdParticella] ", t,
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
          "[ParticellaGaaDAO::getHashIdStoricoParticellaIdParticellaByIdParticella] END.");
    }
  }

  /**
   * Restituisce i dati per il dettaglio della particella (ricerca terreni), tab
   * validazioni
   * 
   * @param idParticella
   *          idParticella che identifica la particella
   * @param anno
   *          anno di cui si cerca la validazione (anno della dichiarazione di
   *          consistenza) facoltativo
   * @param ordineAscendente
   *          array di booleani per indicare se l'ordinamento di campi
   *          ordinabili dall'utente è ascendente o discendente, true =
   *          ascendente, false = discendente: il primo elemento corrisponde
   *          all'ordinamento del cuaa, il secondo della data dichiarazione
   * @return array di ParticellaDettaglioValidazioniVO con i dati richiesti o
   *         null se non trovati
   * @throws DataAccessException
   */
  public ParticellaDettaglioValidazioniVO[] getParticellaDettaglioValidazioni(
      long idParticella, Long anno, int tipoOrdinamento,
      boolean ordineAscendente[]) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<ParticellaDettaglioValidazioniVO> vValidazioni = null;
    ParticellaDettaglioValidazioniVO validazioni[] = null;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::getParticellaDettaglioValidazioni] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf = new StringBuffer(
          "   SELECT  "
              + "     CD.ID_PARTICELLA, "
              + "     DC.ID_DICHIARAZIONE_CONSISTENZA,  "
              + "     AA.CUAA,  "
              + "     AA.DENOMINAZIONE, "
              + "     TO_CHAR(DC.DATA_INSERIMENTO_DICHIARAZIONE,'DD/MM/YYYY') ||  "
              + "       DECODE(DC.NUMERO_PROTOCOLLO, NULL, '', ' Repertorio n. ' || DC.NUMERO_PROTOCOLLO) || "
              + "       DECODE(DC.DATA_PROTOCOLLO, NULL, '', ' del ' || TO_CHAR(DC.DATA_PROTOCOLLO,'DD/MM/YYYY')) AS VALIDAZIONE, "
              + "     CD.ID_TITOLO_POSSESSO,  "
              + "     TMU.CODICE AS CODICE_MACRO_USO, "
              + "     TMU.DESCRIZIONE AS DESC_MACRO_USO,  "
              + "     UD.SUPERFICIE_UTILIZZATA, "
              + "     TU.CODICE AS CODICE_UTILIZZO, "
              + "     TU.DESCRIZIONE AS DESC_UTILIZZO,  "
              + "     TTP.DESCRIZIONE AS DESC_TITOLO_POSSESSO,  "
              + "     CD.SUPERFICIE_CONDOTTA, "
              + "     CD.PERCENTUALE_POSSESSO, "
              + "     CD.ID_CONDUZIONE_DICHIARATA, "
              + "     AA.ID_AZIENDA, "
              + "     (SELECT NVL(MAX(DECODE(BLOCCANTE, "
              + "         'S', 2, 'N', 1,  0)),0) AS ESITO_CONTROLLO "
              + "     FROM  "
              + "       DB_DICHIARAZIONE_SEGNALAZIONE DS "
              + "     WHERE "
              + "       DS.ID_DICHIARAZIONE_CONSISTENZA = DC.ID_DICHIARAZIONE_CONSISTENZA "
              + "       AND DS.ID_STORICO_PARTICELLA = CD.ID_STORICO_PARTICELLA "
              + "      ) AS ESITO_CONTROLLO, "
              + "     SP.SUP_CATASTALE "
              + "   FROM  "
              + "     DB_CONDUZIONE_DICHIARATA CD,  "
              + "     DB_DICHIARAZIONE_CONSISTENZA DC,  "
              + "     DB_UTILIZZO_DICHIARATO UD,  "
              + "     DB_TIPO_UTILIZZO TU,  "
              + "     DB_TIPO_MACRO_USO_VARIETA TMUV, "
              + "     DB_TIPO_MACRO_USO TMU,  "
              + "     DB_ANAGRAFICA_AZIENDA AA, "
              + "     DB_TIPO_TITOLO_POSSESSO TTP, "
              + "     DB_STORICO_PARTICELLA SP "
              + "   WHERE "
              + "     CD.ID_PARTICELLA = ?  "
              + "     AND SP.ID_STORICO_PARTICELLA = CD.ID_STORICO_PARTICELLA "
              + "     AND CD.CODICE_FOTOGRAFIA_TERRENI = DC.CODICE_FOTOGRAFIA_TERRENI "
              + "     AND CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA(+)  "
              + "     AND UD.ID_UTILIZZO = TU.ID_UTILIZZO (+)  "
              + "     AND UD.ID_VARIETA = TMUV.ID_VARIETA (+) "
              + "     AND NVL(UD.ID_TIPO_DETTAGLIO_USO,-1) = NVL(TMUV.ID_TIPO_DETTAGLIO_USO (+) ,-1)  "
              + "     AND TMUV.ID_MACRO_USO = TMU.ID_MACRO_USO (+) "
              + "     AND DC.ID_AZIENDA = AA.ID_AZIENDA "
              + "     AND AA.DATA_INIZIO_VALIDITA <= DC.DATA_INSERIMENTO_DICHIARAZIONE  "
              + "     AND NVL(AA.DATA_FINE_VALIDITA,TO_DATE('31/12/9999','DD/MM/YYYY')) > DC.DATA_INSERIMENTO_DICHIARAZIONE "
              + "     AND CD.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO  ");
      if (anno != null)
      {
        queryBuf
            .append("     AND TO_CHAR(DC.DATA_INSERIMENTO_DICHIARAZIONE,'YYYY') = ?  ");
      }
      queryBuf.append("   ORDER BY  ");
      if (tipoOrdinamento == SolmrConstants.ORDINAMENTO_DETTAGLIO_TERRENI_VALIDAZIONI_PER_CUAA) // Ordinamento
      // per
      // CUAA
      {
        addOrdinamento(queryBuf, "AA.CUAA", ordineAscendente[0]);
        queryBuf.append(", ");
        addOrdinamento(queryBuf, "DC.DATA_INSERIMENTO_DICHIARAZIONE",
            ordineAscendente[1]);
      }
      else
      {
        if (tipoOrdinamento == SolmrConstants.ORDINAMENTO_DETTAGLIO_TERRENI_VALIDAZIONI_PER_VALIDAZIONI)// Ordinamento
        // per
        // Validazioni
        {
          addOrdinamento(queryBuf, "DC.DATA_INSERIMENTO_DICHIARAZIONE",
              ordineAscendente[1]);
          queryBuf.append(", ");
          addOrdinamento(queryBuf, "AA.CUAA", ordineAscendente[0]);
        }
        else
        {
          queryBuf
              .append("AA.CUAA ASC, DC.DATA_INSERIMENTO_DICHIARAZIONE DESC");
        }
      }
      queryBuf
          .append(", CD.ID_TITOLO_POSSESSO DESC, TU.CODICE DESC, TU.DESCRIZIONE DESC");

      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getParticellaDettaglioValidazioni] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idParticella);
      if (anno != null)
      {
        stmt.setLong(++indice, anno.longValue());
      }
      ResultSet rs = stmt.executeQuery();
      vValidazioni = new Vector<ParticellaDettaglioValidazioniVO>();
      while (rs.next())
      {
        ParticellaDettaglioValidazioniVO validazioneVO = new ParticellaDettaglioValidazioniVO();
        validazioneVO.setCodiceMacroUso(rs.getString("CODICE_MACRO_USO"));
        validazioneVO.setCodiceUtilizzo(rs.getString("CODICE_UTILIZZO"));
        validazioneVO.setCuaa(rs.getString("CUAA"));
        validazioneVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        validazioneVO.setDescrizioneMacroUso(rs.getString("DESC_MACRO_USO"));
        validazioneVO.setDescrizioneTitoloPossesso(rs
            .getString("DESC_TITOLO_POSSESSO"));
        validazioneVO.setDescrizioneUtilizzo(rs.getString("DESC_UTILIZZO"));
        int esitoControllo = rs.getInt("ESITO_CONTROLLO");
        /**
         * I valori possibili sono:
         * <ul>
         * <li> 0 Controllo Positivo</li>
         * <li> 1: Warning
         * <li> 2: Bloccante
         * </ul>
         * La query per il caso di controllo positivo può ritornare come valore
         * 0 o null ma La rs.getInt() ritorna 0 quando null quindi unifico i 2
         * casi.
         */
        switch (esitoControllo)
        {
          case 2:
            validazioneVO
                .setEsitoControllo(SolmrConstants.ESITO_CONTROLLO_BLOCCANTE);
            break;
          case 1:
            validazioneVO
                .setEsitoControllo(SolmrConstants.ESITO_CONTROLLO_WARNING);
            break;
          default:
            validazioneVO
                .setEsitoControllo(SolmrConstants.ESITO_CONTROLLO_POSITIVO);
        }
        validazioneVO.setIdDichiarazioneConsistenza(rs
            .getLong("ID_DICHIARAZIONE_CONSISTENZA"));
        validazioneVO.setIdParticella(rs.getLong("ID_PARTICELLA"));
        validazioneVO.setIdTitoloPossesso(rs.getLong("ID_TITOLO_POSSESSO"));
        validazioneVO.setSuperficieCondotta(rs
            .getBigDecimal("SUPERFICIE_CONDOTTA"));
        validazioneVO.setPercentualePossesso(rs
            .getBigDecimal("PERCENTUALE_POSSESSO"));
        validazioneVO.setSuperficieUtilizzata(rs
            .getBigDecimal("SUPERFICIE_UTILIZZATA"));
        validazioneVO.setValidazione(rs.getString("VALIDAZIONE"));
        validazioneVO.setIdConduzioneDichiarata(rs
            .getLong("ID_CONDUZIONE_DICHIARATA"));
        validazioneVO.setIdAzienda(rs.getLong("ID_AZIENDA"));
        validazioneVO.setSupCatastale(rs.getBigDecimal("SUP_CATASTALE"));
        vValidazioni.add(validazioneVO);
      }
      int size = vValidazioni.size();
      if (size == 0)
      {
        return null;
      }
      else
      {
        validazioni = (ParticellaDettaglioValidazioniVO[]) vValidazioni
            .toArray(new ParticellaDettaglioValidazioniVO[size]);
        return validazioni;
      }
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("vValidazioni", vValidazioni),
          new Variabile("validazioni", validazioni) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idParticella", idParticella),
          new Parametro("anno", anno),
          new Parametro("tipoOrdinamento", tipoOrdinamento),
          new Parametro("ordineAscendente", ordineAscendente) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getParticellaDettaglioValidazioni] ", t, query,
          variabili, parametri);
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
          "[ParticellaGaaDAO::getParticellaDettaglioValidazioni] END.");
    }
  }

  public boolean areParticelleNonCessateByIdParticelle(long idParticelle[])
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean retValue = false;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::areParticelleNonCessateByIdParticelle] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf = new StringBuffer(" SELECT " + "  P.DATA_CESSAZIONE "
          + "FROM DB_PARTICELLA P, " + "  DB_STORICO_PARTICELLA SP "
          + "WHERE P.ID_PARTICELLA =SP.ID_PARTICELLA "
          + "  AND P.DATA_CESSAZIONE IS NOT NULL "
          + "  AND SP.ID_PARTICELLA IN ( ").append(
          this.getIdListFromArrayForSQL(idParticelle)).append(")");
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::areParticelleNonCessateByIdParticelle] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      retValue = rs.next();
      return retValue;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("retValue", retValue) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idParticelle", idParticelle) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::areParticelleNonCessateByIdParticelle] ", t, query,
          variabili, parametri);
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
          "[ParticellaGaaDAO::areParticelleNonCessateByIdParticelle] END.");
    }
  }

  /**
   * Metodo che mi restituisce il record di DB_STORICO_PARTICELLA a partire
   * dall'id_particella
   * 
   * @param idParticella
   * @return it.csi.solmr.dto.StoricoParticellaVO
   * @throws DataAccessException
   */
  public StoricoParticellaVO findStoricoParticellaVOByIdParticella(
      long idParticella) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StoricoParticellaVO storicoParticellaVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::findStoricoParticellaVOByIdParticella] BEGIN.");

      query = " SELECT "
      		+ "      SP.ID_STORICO_PARTICELLA, "
          + "      SP.ID_PARTICELLA, "
          + "      SP.DATA_INIZIO_VALIDITA, "
          + "      SP.DATA_FINE_VALIDITA, "
          + "      SP.COMUNE, "
          + "      C.DESCOM, "
          + "      P.SIGLA_PROVINCIA, "
          + "      SP.SEZIONE, "
          + "      SP.FOGLIO, "
          + "      SP.PARTICELLA, "
          + "      SP.SUBALTERNO, "
          + "      SP.SUP_CATASTALE, "
          + "      SP.SUPERFICIE_GRAFICA, "
          + "      SP.ID_ZONA_ALTIMETRICA, "
          + "      TZA.DESCRIZIONE AS DESC_ALTIMETRICA, "
          + "      SP.ID_AREA_A, "
          + "      TAA.DESCRIZIONE AS DESC_AREA_A, "
          + "      SP.ID_AREA_B, "
          + "      TAB.DESCRIZIONE AS DESC_AREA_B, "
          + "      SP.ID_AREA_C, "
          + "      TAC.DESCRIZIONE AS DESC_AREA_C, "
          + "      SP.ID_AREA_D, "
          + "      TAD.DESCRIZIONE AS DESC_AREA_D, "
          + "      SP.ID_AREA_G, "
          + "      TAG.DESCRIZIONE AS DESC_AREA_G, "
          + "      SP.ID_AREA_H, "
          + "      TAH.DESCRIZIONE AS DESC_AREA_H, "
          + "      SP.ID_AREA_I, "
          + "      TAI.DESCRIZIONE AS DESC_AREA_I, "
          + "      SP.ID_AREA_L, "
          + "      TAL.DESCRIZIONE AS DESC_AREA_L, "
          + "      SP.ID_AREA_M, "
          + "      TAM.DESCRIZIONE AS DESC_AREA_M, "
          + "      SP.FLAG_CAPTAZIONE_POZZI, "
          + "      SP.FLAG_IRRIGABILE, "
          + "      SP.ID_CASO_PARTICOLARE, "
          + "      TCP.DESCRIZIONE AS DESC_CASO_PART, "
          + "      SP.MOTIVO_MODIFICA, "
          + "      SP.DATA_AGGIORNAMENTO, "
          + "      SP.ID_UTENTE_AGGIORNAMENTO, "
         // + "      NVL(PVU.COGNOME_UTENTE, TRIM(UPPER(PVU.COGNOME_UTENTE_LOGIN)))||' '||NVL(PVU.NOME_UTENTE, TRIM(UPPER(PVU.NOME_UTENTE_LOGIN)))  AS DENOMINAZIONE, " 
         + "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " 
         + "          || ' ' "
         + "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " 
         + "         FROM PAPUA_V_UTENTE_LOGIN PVU " 
         + "         WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " 
         + "      AS DENOMINAZIONE, " 
        // + "      PVU.DENOMINAZIONE "
         + "      (SELECT PVU.DENOMINAZIONE " 
         + "       FROM PAPUA_V_UTENTE_LOGIN PVU " 
         + "       WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " 
         + "        AS DESCRIZIONE_ENTE_APPARTENENZA, "
          + "      SP.ID_CAUSALE_MOD_PARTICELLA, "
          + "      TCMP.DESCRIZIONE AS DESC_CAUSALE_MOD, "
          + "      SP.SUP_NON_ELEGGIBILE, "
          + "      SP.SUP_NE_BOSCO_ACQUE_FABBRICATO, "
          + "      SP.SUP_NE_FORAGGIERE, "
          + "      SP.SUP_EL_FRUTTA_GUSCIO, "
          + "      SP.SUP_EL_PRATO_PASCOLO, "
          + "      SP.SUP_EL_COLTURE_MISTE, "
          + "      SP.SUP_COLTIVAZ_ARBOREA_CONS, "
          + "      SP.SUP_COLTIVAZ_ARBOREA_SPEC, "
          + "      SP.DATA_FOTO, "
          + "      SP.TIPO_FOTO, "
          + "      SP.ID_FONTE, "
          + "      TF.DESCRIZIONE AS DESC_FONTE, "
          + "      SP.ID_DOCUMENTO, "
          + "      PART.DATA_CESSAZIONE, "
          + "      SP.ID_DOCUMENTO_PROTOCOLLATO, "
          + "      SP.ID_IRRIGAZIONE, "
          + "      SP.ID_FASCIA_FLUVIALE, "
          + "      TFF.DESCRIZIONE AS DESC_FLUVIALE, "
          + "      TI.DESCRIZIONE AS DESC_IRRIGAZIONE, "
          + "      TI.DATA_INIZIO_VALIDITA AS DATA_INI_IRRIGAZ, "
          + "      TI.DATA_FINE_VALIDITA AS DATA_FINE_IRRIGAZ, "
          + "      SP.ID_POTENZIALITA_IRRIGUA, "
          + "      TPI.DESCRIZIONE AS DESC_POTENZIALITA_IRRIGUA, "
          + "      TPI.CODICE AS COD_POTENZIALITA_IRRIGUA, "
          + "      SP.ID_TERRAZZAMENTO, "
          + "      TTR.DESCRIZIONE AS DESC_TERRAZZAMENTO, "
          + "      TTR.CODICE AS COD_TERRAZZAMENTO, "
          + "      SP.ID_ROTAZIONE_COLTURALE, "
          + "      TRC.DESCRIZIONE AS DESC_ROTAZIONE_COLTURALE, "
          + "      TRC.CODICE AS COD_ROTAZIONE_COLTURALE, " +
          "        SP.PERCENTUALE_PENDENZA_MEDIA, " +
          "        SP.GRADI_PENDENZA_MEDIA, " +
          "        SP.GRADI_ESPOSIZIONE_MEDIA, " +
          "        SP.METRI_ALTITUDINE_MEDIA, " +
          "        SP.ID_METODO_IRRIGUO, " +
          "        TMI.DESCRIZIONE_METODO_IRRIGUO, " +
          "        TMI.CODICE_METODO_IRRIGUO " 
          + " FROM DB_STORICO_PARTICELLA SP, "
          + "   COMUNE C, "
          + "   PROVINCIA P, "
          + "   DB_TIPO_ZONA_ALTIMETRICA TZA, "
          + "   DB_TIPO_AREA_A TAA, "
          + "   DB_TIPO_AREA_B TAB, "
          + "   DB_TIPO_AREA_C TAC, "
          + "   DB_TIPO_AREA_D TAD, "
          + "   DB_TIPO_AREA_G TAG, "
          + "   DB_TIPO_AREA_H TAH, "
          + "   DB_TIPO_AREA_I TAI, "
          + "   DB_TIPO_AREA_L TAL, "
          + "   DB_TIPO_AREA_M TAM, "
          + "   DB_TIPO_CASO_PARTICOLARE TCP, "
          //+ "   PAPUA_V_UTENTE_LOGIN PVU, "
          + "   DB_TIPO_CAUSALE_MOD_PARTICELLA TCMP, "
          + "   DB_TIPO_FONTE TF, "
          + "   DB_TIPO_DOCUMENTO TD, "
          + "   DB_PARTICELLA PART, "
          + "   DB_TIPO_IRRIGAZIONE TI, "
          + "   DB_TIPO_FASCIA_FLUVIALE TFF, "
          + "   DB_TIPO_POTENZIALITA_IRRIGUA TPI, "
          + "   DB_TIPO_TERRAZZAMENTO TTR, "
          + "   DB_TIPO_ROTAZIONE_COLTURALE TRC, " +
          "     DB_TIPO_METODO_IRRIGUO TMI "
          + " WHERE  SP.ID_PARTICELLA = ? "
          + " AND    SP.DATA_FINE_VALIDITA IS NULL "
          + " AND    SP.COMUNE = C.ISTAT_COMUNE "
          + " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) "
          + " AND    SP.ID_ZONA_ALTIMETRICA = TZA.ID_ZONA_ALTIMETRICA(+) "
          + " AND    SP.ID_AREA_A = TAA.ID_AREA_A(+) "
          + " AND    SP.ID_AREA_B = TAB.ID_AREA_B(+) "
          + " AND    SP.ID_AREA_C = TAC.ID_AREA_C(+) "
          + " AND    SP.ID_AREA_D = TAD.ID_AREA_D(+) "
          + " AND    SP.ID_AREA_G = TAG.ID_AREA_G(+) "
          + " AND    SP.ID_AREA_H = TAH.ID_AREA_H(+) "
          + " AND    SP.ID_AREA_I = TAI.ID_AREA_I(+) "
          + " AND    SP.ID_AREA_L = TAL.ID_AREA_L(+) "
          + " AND    SP.ID_AREA_M = TAM.ID_AREA_M(+) "
          + " AND    SP.ID_CASO_PARTICOLARE = TCP.ID_CASO_PARTICOLARE(+) "
         // + " AND    SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN "
          + " AND    SP.ID_CAUSALE_MOD_PARTICELLA = TCMP.ID_CAUSALE_MOD_PARTICELLA(+) "
          + " AND    SP.ID_FONTE = TF.ID_FONTE(+) "
          + " AND    SP.ID_DOCUMENTO = TD.ID_DOCUMENTO(+) "
          + " AND    SP.ID_PARTICELLA = PART.ID_PARTICELLA "
          + " AND    SP.ID_IRRIGAZIONE = TI.ID_IRRIGAZIONE(+) "
          + " AND    SP.ID_FASCIA_FLUVIALE = TFF.ID_FASCIA_FLUVIALE(+) "
          + " AND    SP.ID_POTENZIALITA_IRRIGUA = TPI.ID_POTENZIALITA_IRRIGUA(+) "
          + " AND    SP.ID_TERRAZZAMENTO = TTR.ID_TERRAZZAMENTO(+) "
          + " AND    SP.ID_ROTAZIONE_COLTURALE = TRC.ID_ROTAZIONE_COLTURALE(+) " +
          "   AND    SP.ID_METODO_IRRIGUO = TMI.ID_METODO_IRRIGUO (+) ";

      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::findStoricoParticellaVOByIdParticella] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      stmt.setLong(1, idParticella);
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      {
        storicoParticellaVO = new StoricoParticellaVO();
        storicoParticellaVO.setIdStoricoParticella(new Long(rs
            .getLong("ID_STORICO_PARTICELLA")));
        storicoParticellaVO.setIdParticella(new Long(rs
            .getLong("ID_PARTICELLA")));
        storicoParticellaVO.setDataInizioValidita(rs
            .getDate("DATA_INIZIO_VALIDITA"));
        storicoParticellaVO.setDataFineValidita(rs
            .getDate("DATA_FINE_VALIDITA"));
        storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
        ComuneVO comuneVO = new ComuneVO();
        comuneVO.setIstatComune(rs.getString("COMUNE"));
        comuneVO.setDescom(rs.getString("DESCOM"));
        if (Validator.isNotEmpty(rs.getString("SIGLA_PROVINCIA")))
        {
          comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
          ProvinciaVO provinciaVO = new ProvinciaVO();
          provinciaVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));
          comuneVO.setProvinciaVO(provinciaVO);
        }
        storicoParticellaVO.setComuneParticellaVO(comuneVO);
        storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
        storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
        storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
        storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
        storicoParticellaVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
        storicoParticellaVO.setSuperficieGrafica(rs.getString("SUPERFICIE_GRAFICA"));
        if (Validator.isNotEmpty(rs.getString("ID_ZONA_ALTIMETRICA")))
        {
          CodeDescription code = new CodeDescription(Integer.decode(rs
              .getString("ID_ZONA_ALTIMETRICA")), rs
              .getString("DESC_ALTIMETRICA"));
          storicoParticellaVO.setIdZonaAltimetrica(Long.decode(rs
              .getString("ID_ZONA_ALTIMETRICA")));
          storicoParticellaVO.setZonaAltimetrica(code);
        }
        if (Validator.isNotEmpty(rs.getString("ID_AREA_A")))
        {
          CodeDescription code = new CodeDescription(Integer.decode(rs
              .getString("ID_AREA_A")), rs.getString("DESC_AREA_A"));
          storicoParticellaVO.setIdAreaA(new Long(rs.getString("ID_AREA_A")));
          storicoParticellaVO.setAreaA(code);
        }
        if (Validator.isNotEmpty(rs.getString("ID_AREA_B")))
        {
          CodeDescription code = new CodeDescription(Integer.decode(rs
              .getString("ID_AREA_B")), rs.getString("DESC_AREA_B"));
          storicoParticellaVO.setIdAreaB(new Long(rs.getString("ID_AREA_B")));
          storicoParticellaVO.setAreaB(code);
        }
        if (Validator.isNotEmpty(rs.getString("ID_AREA_C")))
        {
          CodeDescription code = new CodeDescription(Integer.decode(rs
              .getString("ID_AREA_C")), rs.getString("DESC_AREA_C"));
          storicoParticellaVO.setIdAreaC(new Long(rs.getString("ID_AREA_C")));
          storicoParticellaVO.setAreaC(code);
        }
        if (Validator.isNotEmpty(rs.getString("ID_AREA_D")))
        {
          CodeDescription code = new CodeDescription(Integer.decode(rs
              .getString("ID_AREA_D")), rs.getString("DESC_AREA_D"));
          storicoParticellaVO.setIdAreaD(new Long(rs.getString("ID_AREA_D")));
          storicoParticellaVO.setAreaD(code);
        }
        if (Validator.isNotEmpty(rs.getString("ID_AREA_G")))
        {
          CodeDescription code = new CodeDescription(Integer.decode(rs
              .getString("ID_AREA_G")), rs.getString("DESC_AREA_G"));
          storicoParticellaVO.setIdAreaG(new Long(rs.getString("ID_AREA_G")));
          storicoParticellaVO.setAreaG(code);
        }
        if(Validator.isNotEmpty(rs.getString("ID_AREA_H"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_H")), rs.getString("DESC_AREA_H"));
          storicoParticellaVO.setIdAreaH(new Long(rs.getString("ID_AREA_H")));
          storicoParticellaVO.setAreaH(code);
        }
        if(Validator.isNotEmpty(rs.getString("ID_AREA_I"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_I")), rs.getString("DESC_AREA_I"));
          storicoParticellaVO.setIdAreaI(new Long(rs.getString("ID_AREA_I")));
          storicoParticellaVO.setAreaI(code);
        }
        if(Validator.isNotEmpty(rs.getString("ID_AREA_L"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_L")), rs.getString("DESC_AREA_L"));
          storicoParticellaVO.setIdAreaL(new Long(rs.getString("ID_AREA_L")));
          storicoParticellaVO.setAreaL(code);
        }
        if(Validator.isNotEmpty(rs.getString("ID_AREA_M"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_M")), rs.getString("DESC_AREA_M"));
          storicoParticellaVO.setIdAreaM(new Long(rs.getString("ID_AREA_M")));
          storicoParticellaVO.setAreaM(code);
        }
        storicoParticellaVO.setFlagCaptazionePozzi(rs
            .getString("FLAG_CAPTAZIONE_POZZI"));
        storicoParticellaVO.setFlagIrrigabile(rs.getString("FLAG_IRRIGABILE"));
        if (Validator.isNotEmpty(rs.getString("ID_CASO_PARTICOLARE")))
        {
          CodeDescription code = new CodeDescription(Integer.decode(rs
              .getString("ID_CASO_PARTICOLARE")), rs
              .getString("DESC_CASO_PART"));
          storicoParticellaVO.setIdCasoParticolare(new Long(rs
              .getString("ID_CASO_PARTICOLARE")));
          storicoParticellaVO.setCasoParticolare(code);
        }
        storicoParticellaVO.setMotivoModifica(rs.getString("MOTIVO_MODIFICA"));
        storicoParticellaVO.setDataAggiornamento(rs
            .getDate("DATA_AGGIORNAMENTO"));
        storicoParticellaVO.setIdUtenteAggiornamento(new Long(rs
            .getLong("ID_UTENTE_AGGIORNAMENTO")));
        UtenteIrideVO utenteIrideVO = new UtenteIrideVO();
        utenteIrideVO.setIdUtente(new Long(rs
            .getLong("ID_UTENTE_AGGIORNAMENTO")));
        utenteIrideVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        utenteIrideVO.setDescrizioneEnteAppartenenza(rs.getString("DESCRIZIONE_ENTE_APPARTENENZA"));
        storicoParticellaVO.setUtenteAggiornamento(utenteIrideVO);
        if (Validator.isNotEmpty(rs.getString("ID_CAUSALE_MOD_PARTICELLA")))
        {
          CodeDescription code = new CodeDescription(Integer.decode(rs
              .getString("ID_CAUSALE_MOD_PARTICELLA")), rs
              .getString("DESC_CAUSALE_MOD"));
          storicoParticellaVO.setIdCausaleModParticella(new Long(rs
              .getString("ID_CAUSALE_MOD_PARTICELLA")));
          storicoParticellaVO.setCausaleModParticella(code);
        }
        storicoParticellaVO.setSupNonEleggibile(rs
            .getString("SUP_NON_ELEGGIBILE"));
        storicoParticellaVO.setSupNeBoscoAcqueFabbricato(rs
            .getString("SUP_NE_BOSCO_ACQUE_FABBRICATO"));
        storicoParticellaVO
            .setSupNeForaggere(rs.getString("SUP_NE_FORAGGIERE"));
        storicoParticellaVO.setSupElFruttaGuscio(rs
            .getString("SUP_EL_FRUTTA_GUSCIO"));
        storicoParticellaVO.setSupElPratoPascolo(rs
            .getString("SUP_EL_PRATO_PASCOLO"));
        storicoParticellaVO.setSupElColtureMiste(rs
            .getString("SUP_EL_COLTURE_MISTE"));
        storicoParticellaVO.setSupColtivazArboreaCons(rs
            .getString("SUP_COLTIVAZ_ARBOREA_CONS"));
        storicoParticellaVO.setSupColtivazArboreaSpec(rs
            .getString("SUP_COLTIVAZ_ARBOREA_SPEC"));
        storicoParticellaVO.setDataFoto(rs.getDate("DATA_FOTO"));
        storicoParticellaVO.setTipoFoto(rs.getString("TIPO_FOTO"));
        if (Validator.isNotEmpty(rs.getString("ID_FONTE")))
        {
          CodeDescription code = new CodeDescription(Integer.decode(rs
              .getString("ID_FONTE")), rs.getString("DESC_FONTE"));
          storicoParticellaVO.setIdFonte(new Long(rs.getString("ID_FONTE")));
          storicoParticellaVO.setFonte(code);
        }
        if (Validator.isNotEmpty(rs.getString("ID_DOCUMENTO")))
        {
          storicoParticellaVO.setIdDocumento(new Long(rs
              .getLong("ID_DOCUMENTO")));
        }
        storicoParticellaVO.setDataCessazione(rs.getDate("DATA_CESSAZIONE"));
        if (Validator.isNotEmpty(rs.getString("ID_DOCUMENTO_PROTOCOLLATO")))
        {
          storicoParticellaVO.setIdDocumentoProtocollato(new Long(rs
              .getString("ID_DOCUMENTO_PROTOCOLLATO")));
        }
        if (Validator.isNotEmpty(rs.getString("ID_IRRIGAZIONE")))
        {
          storicoParticellaVO.setIdIrrigazione(new Long(rs
              .getLong("ID_IRRIGAZIONE")));
          TipoIrrigazioneVO tipoIrrigazioneVO = new TipoIrrigazioneVO();
          tipoIrrigazioneVO.setIdIrrigazione(new Long(rs
              .getLong("ID_IRRIGAZIONE")));
          tipoIrrigazioneVO.setDescrizione(rs.getString("DESC_IRRIGAZIONE"));
          tipoIrrigazioneVO.setDataInizioValidita(rs
              .getDate("DATA_INI_IRRIGAZ"));
          tipoIrrigazioneVO
              .setDataFineValidita(rs.getDate("DATA_FINE_IRRIGAZ"));
          storicoParticellaVO.setTipoIrrigazioneVO(tipoIrrigazioneVO);
        }
        if (Validator.isNotEmpty(rs.getString("ID_FASCIA_FLUVIALE")))
        {
          storicoParticellaVO.setIdFasciaFluviale(new Long(rs
              .getLong("ID_FASCIA_FLUVIALE")));
          CodeDescription fasciaFluviale = new CodeDescription(Integer
              .decode(rs.getString("ID_FASCIA_FLUVIALE")), rs
              .getString("DESC_FLUVIALE"));
          storicoParticellaVO.setFasciaFluviale(fasciaFluviale);
        }
        if(Validator.isNotEmpty(rs.getString("ID_POTENZIALITA_IRRIGUA"))) {
          storicoParticellaVO.setIdPotenzialitaIrrigua(new Long(rs.getLong("ID_POTENZIALITA_IRRIGUA")));
          TipoPotenzialitaIrriguaVO tipoPotenzialitaIrriguaVO = new TipoPotenzialitaIrriguaVO();
          tipoPotenzialitaIrriguaVO.setIdPotenzialitaIrrigua(new Long(rs.getLong("ID_POTENZIALITA_IRRIGUA")));
          tipoPotenzialitaIrriguaVO.setDescrizione(rs.getString("DESC_POTENZIALITA_IRRIGUA"));
          tipoPotenzialitaIrriguaVO.setCodice(rs.getString("COD_POTENZIALITA_IRRIGUA"));
          storicoParticellaVO.setTipoPotenzialitaIrriguaVO(tipoPotenzialitaIrriguaVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_TERRAZZAMENTO"))) {
          storicoParticellaVO.setIdTerrazzamento(new Long(rs.getLong("ID_TERRAZZAMENTO")));
          TipoTerrazzamentoVO tipoTerrazzamentoVO = new TipoTerrazzamentoVO();
          tipoTerrazzamentoVO.setIdTerrazzamento(new Long(rs.getLong("ID_TERRAZZAMENTO")));
          tipoTerrazzamentoVO.setDescrizione(rs.getString("DESC_TERRAZZAMENTO"));
          tipoTerrazzamentoVO.setCodice(rs.getString("COD_TERRAZZAMENTO"));
          storicoParticellaVO.setTipoTerrazzamentoVO(tipoTerrazzamentoVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_ROTAZIONE_COLTURALE"))) {
          storicoParticellaVO.setIdRotazioneColturale(new Long(rs.getLong("ID_ROTAZIONE_COLTURALE")));
          TipoRotazioneColturaleVO tipoRotazioneColturaleVO = new TipoRotazioneColturaleVO();
          tipoRotazioneColturaleVO.setIdRotazioneColturale(new Long(rs.getLong("ID_ROTAZIONE_COLTURALE")));
          tipoRotazioneColturaleVO.setDescrizione(rs.getString("DESC_ROTAZIONE_COLTURALE"));
          tipoRotazioneColturaleVO.setCodice(rs.getString("COD_ROTAZIONE_COLTURALE"));
          storicoParticellaVO.setTipoRotazioneColturaleVO(tipoRotazioneColturaleVO);
        }
        
        storicoParticellaVO.setPercentualePendenzaMedia(rs.getBigDecimal("PERCENTUALE_PENDENZA_MEDIA"));
        storicoParticellaVO.setGradiPendenzaMedia(rs.getBigDecimal("GRADI_PENDENZA_MEDIA"));
        storicoParticellaVO.setGradiEsposizioneMedia(rs.getBigDecimal("GRADI_ESPOSIZIONE_MEDIA"));
        storicoParticellaVO.setMetriAltitudineMedia(rs.getBigDecimal("METRI_ALTITUDINE_MEDIA"));
        
        
        if(Validator.isNotEmpty(rs.getString("ID_METODO_IRRIGUO"))) {
          storicoParticellaVO.setIdMetodoIrriguo(new Long(rs.getLong("ID_METODO_IRRIGUO")));
          TipoMetodoIrriguoVO tipoMetodoIrriguo = new TipoMetodoIrriguoVO();
          tipoMetodoIrriguo.setCodiceMetodoIrriguo(rs.getString("CODICE_METODO_IRRIGUO"));
          tipoMetodoIrriguo.setDescrizioneMetodoIrriguo(rs.getString("DESCRIZIONE_METODO_IRRIGUO"));
          
          storicoParticellaVO.setTipoMetodoIrriguo(tipoMetodoIrriguo);
        }
        
        
      }
      return storicoParticellaVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("storicoParticellaVO", storicoParticellaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idParticella", idParticella), };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::findStoricoParticellaVOByIdParticella] ", t, query,
          variabili, parametri);
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
          "[ParticellaGaaDAO::findStoricoParticellaVOByIdParticella] END.");
    }
  }

  /**
   * Metodo che mi restituisce una hashmap con chiave = ID_STORICO_PARTICELLA e
   * valore= SUP_CATASTALE di BD_STORICO_PARTICELLA per tutti gli
   * ID_STORICO_PARTICELLA di una particella (ossia quelli con ID_PARTICELLA = ?)
   * 
   * @param idParticella
   * @return HashMap
   * @throws DataAccessException
   */
  public HashMap<Long,BigDecimal> getMapStoricoSupCatastaleByIdParticella(long idParticella)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    HashMap<Long,BigDecimal> mapStoricoSuperficieCatastale = null;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::getMapStoricoSupCatastaleByIdParticella] BEGIN.");

      query = " SELECT " + "   SP.ID_STORICO_PARTICELLA, SP.SUP_CATASTALE "
          + " FROM " + "   DB_STORICO_PARTICELLA SP " + " WHERE "
          + "   ID_PARTICELLA = ?";

      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getMapStoricoSupCatastaleByIdParticella] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      stmt.setLong(1, idParticella);
      ResultSet rs = stmt.executeQuery();
      mapStoricoSuperficieCatastale = new HashMap<Long,BigDecimal>();
      while (rs.next())
      {
        mapStoricoSuperficieCatastale.put(new Long(rs
            .getLong("ID_STORICO_PARTICELLA")), rs
            .getBigDecimal("SUP_CATASTALE"));
      }
      return mapStoricoSuperficieCatastale;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      {
          new Variabile("query", query),
          new Variabile("mapStoricoSuperficieCatastale",
              mapStoricoSuperficieCatastale) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idParticella", idParticella), };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getMapStoricoSupCatastaleByIdParticella] ", t,
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
          "[ParticellaGaaDAO::getMapStoricoSupCatastaleByIdParticella] END.");
    }
  }
  
  
  /**
   * Restituisce l'elenco degli id_particella che soddisfano i filtri
   * della ricerca terreni
   * 
   * @param ids
   * @return
   * @throws DataAccessException
   */
  public long[] ricercaIdConduzioneTerreniImportaAsservimento(
      FiltriRicercaTerrenoImportaAsservimentoVO filtriRicercaTerrenoVO) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<Long> vIds = null;
    long ids[] = null;
    try
    {
      SolmrLogger.debug(this, "[ParticellaGaaDAO::ricercaIdConduzioneTerreniImportaAsservimento] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf = new StringBuffer(" " +
      		"SELECT " +
      		"        CD.ID_CONDUZIONE_DICHIARATA, "+
          "        SP.ID_PARTICELLA " +
          "FROM    DB_CONDUZIONE_DICHIARATA CD, "+
          "        DB_STORICO_PARTICELLA SP, COMUNE C "+
          "WHERE   SP.COMUNE = C.ISTAT_COMUNE " +
          "AND     CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA "+
          "AND     CD.ID_TITOLO_POSSESSO <> '5' "+
          "AND     CD.ID_TITOLO_POSSESSO <> '6' "+
          "AND     CD.CODICE_FOTOGRAFIA_TERRENI = "+ 
          "(SELECT MAX(CODICE_FOTOGRAFIA_TERRENI) "+
          "FROM    DB_DICHIARAZIONE_CONSISTENZA DC "+ 
          "WHERE   DC.ID_AZIENDA = ? "+ 
          "AND     DC.DATA_INSERIMENTO_DICHIARAZIONE = "+ 
          "(SELECT MAX(DC1.DATA_INSERIMENTO_DICHIARAZIONE) "+ 
          "FROM   DB_DICHIARAZIONE_CONSISTENZA DC1, DB_TIPO_MOTIVO_DICHIARAZIONE D "+ 
          "WHERE  DC1.ID_AZIENDA = ? "+ 
          "AND D.ID_MOTIVO_DICHIARAZIONE=DC1.ID_MOTIVO_DICHIARAZIONE "+ 
          "AND D.ID_MOTIVO_DICHIARAZIONE<>7 "+ 
          "AND D.TIPO_DICHIARAZIONE<>'C' )) "+ 
          
          
          "ORDER BY C.DESCOM, SP.SEZIONE, SP.FOGLIO, SP.PARTICELLA, SP.SUBALTERNO");

      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[ParticellaGaaDAO::ricercaIdConduzioneTerreniImportaAsservimento] Query="
            + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, filtriRicercaTerrenoVO
          .getIdAziendaSearch());
      stmt.setLong(++indice, filtriRicercaTerrenoVO
          .getIdAziendaSearch());

      ResultSet rs = stmt.executeQuery();
      vIds = new Vector<Long>();
      while (rs.next())
      {
        vIds.add(new Long(rs.getLong("ID_CONDUZIONE_DICHIARATA")));
      }
      int size = vIds.size();
      if (size == 0)
      {
        return null;
      }
      else
      {
        ids = new long[size];
        for (int i = 0; i < size; ++i)
        {
          ids[i] = ((Long) vIds.get(i)).longValue();
        }
        return ids;
      }
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("vIds", vIds), new Variabile("ids", ids) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("filtriRicercaTerrenoVO", filtriRicercaTerrenoVO) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[ParticellaGaaDAO::ricercaIdConduzioneTerreniImportaAsservimento] ", t,
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
      SolmrLogger.debug(this, "[ParticellaGaaDAO::ricercaIdConduzioneTerreniImportaAsservimento] END.");
    }
  }
  
  
  /**
   * Restituisce i dati della ricerca terreni importa asservimento di cui si conoscono gli id ( dati
   * ricercaIdConduzioneImportaAsservimento)
   * 
   * @param ids
   * @return
   * @throws DataAccessException
   */
  public RigaRicercaTerreniImportaAsservimentoVO[] getRigheRicercaTerreniImportaAsservimentoByIdConduzioneRange(
      long ids[]) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    RigaRicercaTerreniImportaAsservimentoVO rigaRicercaTerreniImportaAsservimentoVO = null;
    RigaRicercaTerreniImportaAsservimentoVO righe[] = null;
    HashMap<Long,RigaRicercaTerreniImportaAsservimentoVO> hmRighe = null;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::getRigheRicercaTerreniImportaAsservimentoByIdConduzioneRange] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
                "SELECT   " 
              + "        CD.ID_CONDUZIONE_DICHIARATA, "
              + "        SP.COMUNE, C.DESCOM, SP.SEZIONE, " 
              + "		     SP.FOGLIO, SP.PARTICELLA, SP.SUPERFICIE_GRAFICA, " 
              + "        SP.SUBALTERNO, SP.SUP_CATASTALE, SP.ID_PARTICELLA, " 
              + "        CD.ID_TITOLO_POSSESSO, CD.PERCENTUALE_POSSESSO, CD.SUPERFICIE_CONDOTTA, "
              + "        P.SIGLA_PROVINCIA "
              + "FROM    DB_CONDUZIONE_DICHIARATA CD, "
              + "        DB_STORICO_PARTICELLA SP, COMUNE C,PROVINCIA P "
              + "WHERE  "
              + "        SP.COMUNE = C.ISTAT_COMUNE "
              + "        AND C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA   "
              + "AND     CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA "
              + "     AND CD.ID_CONDUZIONE_DICHIARATA IN ( ").append(
          this.getIdListFromArrayForSQL(ids)).append(")");
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getRigheRicercaTerreniImportaAsservimentoByIdConduzioneRange] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      ResultSet rs = stmt.executeQuery();
      hmRighe = new HashMap<Long,RigaRicercaTerreniImportaAsservimentoVO>();
      while (rs.next())
      {
        rigaRicercaTerreniImportaAsservimentoVO = new RigaRicercaTerreniImportaAsservimentoVO();
        rigaRicercaTerreniImportaAsservimentoVO.setTipoRicerca(SolmrConstants.RICERCA_IMPORTA_ASSERVIMENO_CUAA);
        rigaRicercaTerreniImportaAsservimentoVO.setIdConduzioneDichiarata(
              checkLong(rs.getString("ID_CONDUZIONE_DICHIARATA")));
        rigaRicercaTerreniImportaAsservimentoVO.setDescrizioneComune(rs.getString("DESCOM"));
        rigaRicercaTerreniImportaAsservimentoVO.setFoglio(rs.getLong("FOGLIO"));
        rigaRicercaTerreniImportaAsservimentoVO.setIdParticella(rs.getLong("ID_PARTICELLA"));
        rigaRicercaTerreniImportaAsservimentoVO.setIstatComune(rs.getString("COMUNE"));
        rigaRicercaTerreniImportaAsservimentoVO.setSiglaProvinciaParticella(rs.getString("SIGLA_PROVINCIA"));
        rigaRicercaTerreniImportaAsservimentoVO
            .setParticella(checkLong(rs.getString("PARTICELLA")));
        rigaRicercaTerreniImportaAsservimentoVO.setSezione(rs.getString("SEZIONE"));
        rigaRicercaTerreniImportaAsservimentoVO.setSubalterno(rs.getString("SUBALTERNO"));
        rigaRicercaTerreniImportaAsservimentoVO.setSupCatastale(rs.getBigDecimal("SUP_CATASTALE"));
        rigaRicercaTerreniImportaAsservimentoVO.setSuperficieGrafica(rs.getBigDecimal("SUPERFICIE_GRAFICA"));
        rigaRicercaTerreniImportaAsservimentoVO.setSupCondotta(rs.getBigDecimal("SUPERFICIE_CONDOTTA"));
        rigaRicercaTerreniImportaAsservimentoVO.setIdTitoloPosesso(rs.getLong("ID_TITOLO_POSSESSO"));
        rigaRicercaTerreniImportaAsservimentoVO.setPercentualePossesso(rs.getLong("PERCENTUALE_POSSESSO"));
        hmRighe.put(rigaRicercaTerreniImportaAsservimentoVO.getIdConduzioneDichiarata(),
            rigaRicercaTerreniImportaAsservimentoVO);
      }
      int size = hmRighe.size();
      if (size == 0)
      {
        return null;
      }
      else
      {
        righe = new RigaRicercaTerreniImportaAsservimentoVO[size];
        for (int i = 0; i < size; ++i)
        {
          righe[i] = (RigaRicercaTerreniImportaAsservimentoVO) hmRighe.get(new Long(ids[i]));
        }
        return righe;
      }
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("hmRighe", hmRighe),
          new Variabile("rigaRicercaTerreniImportaAsservimentoVO", rigaRicercaTerreniImportaAsservimentoVO),
          new Variabile("righe", righe) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("ids", ids) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getRigheRicercaTerreniImportaAsservimentoByIdConduzioneRange] ", t,
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
          "[ParticellaGaaDAO::getRigheRicercaTerreniImportaAsservimentoByIdConduzioneRange] END.");
    }
  }
  
  /**
   * Restituisce i dati della ricerca terreni di cui si conoscono gli id ( dati
   * ricercaIdStoricoParticella)
   * 
   * @param ids
   * @return
   * @throws DataAccessException
   */
  public RigaRicercaTerreniImportaAsservimentoVO[] getRigheRicercaTerreniImportaAsservimentoByIdParticellaRange(
      long ids[]) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    RigaRicercaTerreniImportaAsservimentoVO rigaRicercaTerreniImportaAsservimentoVO = null;
    RigaRicercaTerreniImportaAsservimentoVO righe[] = null;
    HashMap<Long,RigaRicercaTerreniImportaAsservimentoVO> hmRighe = null;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::getRigheRicercaTerreniImportaAsservimentoByIdParticellaRange] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
          "   SELECT   " 
              + "     SP.ID_PARTICELLA, "
              + "     C.DESCOM,  "
              + "     C.ISTAT_COMUNE,  " 
              + "     SP.SEZIONE,  "
              + "     SP.FOGLIO,   " 
              + "     SP.PARTICELLA,   "
              + "     SP.SUBALTERNO,   " 
              + "     SP.SUP_CATASTALE,  "
              + "     SP.SUPERFICIE_GRAFICA,  "
              + "     P.SIGLA_PROVINCIA "
              + "FROM   "
              + "     DB_STORICO_PARTICELLA SP,    "
              + "     COMUNE C,    "
              + "     PROVINCIA P "
              + "WHERE  "
              + "     C.ISTAT_COMUNE = SP.COMUNE   "
              + "AND  C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA   "
              + "AND  SP.DATA_FINE_VALIDITA IS NULL "
              + "AND  SP.ID_PARTICELLA IN ( ").append(
          this.getIdListFromArrayForSQL(ids)).append(")");
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getRigheRicercaTerreniImportaAsservimentoByIdParticellaRange] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      ResultSet rs = stmt.executeQuery();
      hmRighe = new HashMap<Long,RigaRicercaTerreniImportaAsservimentoVO>();
      while (rs.next())
      {
        rigaRicercaTerreniImportaAsservimentoVO = new RigaRicercaTerreniImportaAsservimentoVO();
        rigaRicercaTerreniImportaAsservimentoVO.setTipoRicerca(SolmrConstants.RICERCA_IMPORTA_ASSERVIMENO_CHIAVE_CATASTALE);
        rigaRicercaTerreniImportaAsservimentoVO.setDescrizioneComune(rs.getString("DESCOM"));
        rigaRicercaTerreniImportaAsservimentoVO.setSiglaProvinciaParticella(rs.getString("SIGLA_PROVINCIA"));
        rigaRicercaTerreniImportaAsservimentoVO.setFoglio(rs.getLong("FOGLIO"));
        rigaRicercaTerreniImportaAsservimentoVO.setIdParticella(rs.getLong("ID_PARTICELLA"));
        rigaRicercaTerreniImportaAsservimentoVO.setIstatComune(rs.getString("ISTAT_COMUNE"));
        rigaRicercaTerreniImportaAsservimentoVO.setParticella(checkLong(rs.getString("PARTICELLA")));
        rigaRicercaTerreniImportaAsservimentoVO.setSezione(rs.getString("SEZIONE"));
        rigaRicercaTerreniImportaAsservimentoVO.setSubalterno(rs.getString("SUBALTERNO"));
        rigaRicercaTerreniImportaAsservimentoVO.setSupCatastale(rs.getBigDecimal("SUP_CATASTALE"));
        rigaRicercaTerreniImportaAsservimentoVO.setSuperficieGrafica(rs.getBigDecimal("SUPERFICIE_GRAFICA"));
        hmRighe.put(new Long(rigaRicercaTerreniImportaAsservimentoVO.getIdParticella()),
            rigaRicercaTerreniImportaAsservimentoVO);
      }
      int size = hmRighe.size();
      if (size == 0)
      {
        return null;
      }
      else
      {
        righe = new RigaRicercaTerreniImportaAsservimentoVO[size];
        for (int i = 0; i < size; ++i)
        {
          righe[i] = (RigaRicercaTerreniImportaAsservimentoVO) hmRighe.get(new Long(ids[i]));
        }
        return righe;
      }
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("hmRighe", hmRighe),
          new Variabile("rigaRicercaTerreniImportaAsservimentoVO", rigaRicercaTerreniImportaAsservimentoVO),
          new Variabile("righe", righe) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("ids", ids) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getRigheRicercaTerreniImportaAsservimentoByIdParticellaRange] ", t,
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
          "[ParticellaGaaDAO::getRigheRicercaTerreniImportaAsservimentoByIdParticellaRange] END.");
    }
  }
  
  
  public PlSqlCodeDescription allineaSupEleggibilePlSql(long idAzienda, 
      long idUtenteAggiornamento) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    CallableStatement cs = null;
    StringBuffer queryBuf = null;
    PlSqlCodeDescription plqObj = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::allineaSupEleggibilePlSql] BEGIN.");
      /***
       *  PROCEDURE Allinea_Sup_Utilizzata (pIdAzienda    IN DB_AZIENDA.ID_AZIENDA%TYPE,
                                      pIdUtenteAgg  IN DB_UTILIZZO_PARTICELLA.ID_UTENTE_AGGIORNAMENTO%TYPE,
                                      pResult      OUT VARCHAR2,
                                      pMsg         OUT VARCHAR2);
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{call PCK_SMRGAA_LIBRERIA.Allinea_Sup_Utilizzata(?,?,?,?)}");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::allineaSupEleggibilePlSql] Query="
                + query);
      }
      
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.setLong(1, idAzienda);
      cs.setLong(2, idUtenteAggiornamento);
      cs.registerOutParameter(3,Types.VARCHAR); //codice errore
      cs.registerOutParameter(4,Types.VARCHAR); //msg errore
      
      cs.executeUpdate();
      
      plqObj = new PlSqlCodeDescription();  
      plqObj.setDescription(cs.getString(3));
      plqObj.setOtherdescription(cs.getString(4));
      
      return plqObj;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("plqObj", plqObj) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("idUtenteAggiornamento", idUtenteAggiornamento) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[ParticellaGaaDAO::allineaSupEleggibilePlSql] ",
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
      closePlSql(cs, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::allineaSupEleggibilePlSql] END.");
    }
  }
  
  
  /**
   * 
   * Metodo usato in eliminaParticelle per trovare se sulla conduzione
   * esitono unita arboree attive
   * 
   * 
   * @param idConduzioneParticella
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<StoricoUnitaArboreaVO> getUnitaArboreeAttiveForAziendaAndConduzione(
      long idConduzioneParticella, long idAzienda)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoUnitaArboreaVO> vUnitaArborea = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::getUnitaArboreeAttiveForAziendaAndConduzione] BEGIN.");

      query = " " +
      		"SELECT SUA.ID_STORICO_UNITA_ARBOREA, " +
      		"       SUA.ID_PARTICELLA " + 
          "FROM   DB_CONDUZIONE_PARTICELLA CP, " +
          "       DB_STORICO_UNITA_ARBOREA SUA " +  
          "WHERE  CP.ID_CONDUZIONE_PARTICELLA = ? " +
          "AND    CP.ID_PARTICELLA = SUA.ID_PARTICELLA " +
          "AND    SUA.ID_AZIENDA = ? " +
          "AND    SUA.DATA_FINE_VALIDITA IS NULL ";
      
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      stmt.setLong(1, idConduzioneParticella);
      stmt.setLong(2, idAzienda);
      
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      while (rs.next())
      {
        if(vUnitaArborea == null)
        {
          vUnitaArborea = new Vector<StoricoUnitaArboreaVO>();
        }
        StoricoUnitaArboreaVO storicoUnitaArboreaVO = new StoricoUnitaArboreaVO();
        
        storicoUnitaArboreaVO.setIdStoricoUnitaArborea(rs.getLong("ID_STORICO_UNITA_ARBOREA"));
        storicoUnitaArboreaVO.setIdParticella(rs.getLong("ID_PARTICELLA"));
        
        vUnitaArborea.add(storicoUnitaArboreaVO);
      }
      
      return vUnitaArborea;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("vUnitaArborea", vUnitaArborea) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idConduzioneParticella", idConduzioneParticella),
        new Parametro("idAzienda", idAzienda)  };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getUnitaArboreeAttiveForAziendaAndConduzione] ", t,
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
          "[ParticellaGaaDAO::getUnitaArboreeAttiveForAziendaAndConduzione] END.");
    }
  }
  
  /**
   * 
   * Metodo usato in eliminaParticelle per trovare se esistono altre conduzioni
   * dell'azienda relative alla particella
   * 
   * @param idParticella
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public boolean esistonoAltreConduzioni(long idParticella, long idAzienda)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    boolean flagTrovato = false;
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::esistonoAltreConduzioni] BEGIN.");

      query = " " +
          "SELECT * " +
          "FROM   DB_CONDUZIONE_PARTICELLA CP " +
          "WHERE  CP.ID_PARTICELLA = ? " +
          "AND    CP.ID_UTE IN (SELECT ID_UTE " +
          "                     FROM   DB_UTE " +
          "                     WHERE  ID_AZIENDA = ? " +
          "                     AND    DATA_FINE_ATTIVITA IS NULL ) " +
          "AND    CP.DATA_FINE_CONDUZIONE IS NULL ";
      
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      stmt.setLong(1, idParticella);
      stmt.setLong(2, idAzienda);
      
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      if (rs.next())
      {
        flagTrovato = true;
      }
      
      return flagTrovato;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("flagTrovato", flagTrovato) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idParticella", idParticella),
        new Parametro("idAzienda", idAzienda)  };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::esistonoAltreConduzioni] ", t,
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
          "[ParticellaGaaDAO::esistonoAltreConduzioni] END.");
    }
  }
  
  /**
   * 
   * restituisce i dati della tabella DB_PARTICELLA_BIO
   * relativi alla particella e alla dichiarazione di consistenza
   * 
   * 
   * 
   * @param idParticella
   * @param idDichiarazioneConsistenza
   * @return
   * @throws DataAccessException
   */
  public ParticellaBioVO getParticellaBio(long idParticella, long idAzienda, Date dataInserimentoDichiarazione)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    ParticellaBioVO particellaBioVO = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::getParticellaBio] BEGIN.");
    
      query = " " +
          "SELECT PB.ID_PARTICELLA_BIO, " +
          "       PB.ID_AZIENDA, " +
          "       PB.ID_PARTICELLA, " +
          "       PB.SUP_BIOLOGICO, " +
          "       PB.SUP_CONVENZIONALE, " +
          "       PB.SUP_IN_CONVERSIONE, " +
          "       PB.DATA_INIZIO_CONVERSIONE, " +
          "       PB.DATA_FINE_CONVERSIONE, " +
          "       PB.DATA_INIZIO_VALIDITA, " +
          "       PB.DATA_FINE_VALIDITA " +
          "FROM   DB_PARTICELLA_BIO PB " +
          "WHERE  PB.ID_PARTICELLA = ? " +
          "AND    PB.ID_AZIENDA = ? ";
      
      if(Validator.isNotEmpty(dataInserimentoDichiarazione))
      {
        query += "" +
        	"AND   PB.DATA_INIZIO_VALIDITA < ? " +
          "AND   NVL(PB.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? ";       
      }
      else
      {
        query += "" +
          "AND   PB.DATA_FINE_VALIDITA IS NULL ";
      }
      
      
      
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getParticellaBio] Query="
                + query);
      }
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      int idx = 0;
      stmt.setLong(++idx, idParticella);
      stmt.setLong(++idx, idAzienda);
      
      
      if(Validator.isNotEmpty(dataInserimentoDichiarazione))
      {
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoDichiarazione));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoDichiarazione));
      }
      
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      if (rs.next())
      {
        particellaBioVO = new ParticellaBioVO();
        particellaBioVO.setIdParticellaBio(rs.getLong("ID_PARTICELLA_BIO"));
        particellaBioVO.setIdAzienda(rs.getLong("ID_AZIENDA"));
        particellaBioVO.setIdParticella(rs.getLong("ID_PARTICELLA"));
        particellaBioVO.setSupBiologico(rs.getBigDecimal("SUP_BIOLOGICO"));
        particellaBioVO.setSupConvenzionale(rs.getBigDecimal("SUP_CONVENZIONALE"));
        particellaBioVO.setSupInConversione(rs.getBigDecimal("SUP_IN_CONVERSIONE"));
        particellaBioVO.setDataInzioConversione(rs.getTimestamp("DATA_INIZIO_CONVERSIONE"));
        particellaBioVO.setDataFineConversione(rs.getTimestamp("DATA_FINE_CONVERSIONE"));
        particellaBioVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        particellaBioVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        
      }
      
      return particellaBioVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("particellaBioVO", particellaBioVO) };
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idParticella", idParticella),
        new Parametro("idAzienda", idAzienda),
        new Parametro("dataInserimentoDichiarazione", dataInserimentoDichiarazione) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getParticellaBio] ", t,
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
          "[ParticellaGaaDAO::getParticellaBio] END.");
    }
  }
  
  
  /**
   * Restituisce un vettore di coppie comune/sezione
   * della tabella db_storico_unita_arborea relative
   * alla azienda per poter essere usata nel servizio sigmater
   * description = comune
   * item = sezione
   * 
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<BaseCodeDescription> getComuneAndSezioneForSigmater(long idAzienda)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<BaseCodeDescription> vComSez = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::getComuneAndSezioneForSigmater] BEGIN.");
    
      query = " " +
          "SELECT SP.COMUNE, SP.SEZIONE " +
          "FROM   DB_STORICO_PARTICELLA SP,  " +
          "       DB_CONDUZIONE_PARTICELLA CP, " +
          "       DB_UTE UT " +
          "WHERE  UT.ID_AZIENDA = ? " +
          "AND    SP.DATA_FINE_VALIDITA IS NULL " +
          "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
          "AND    UT.DATA_FINE_ATTIVITA IS NULL " +
          "AND    UT.ID_UTE = CP.ID_UTE " +
          "AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
          "GROUP BY SP.COMUNE, " +
          "         SP.SEZIONE";
      
      
      
      
      
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getComuneAndSezioneForSigmater] Query="
                + query);
      }
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      int idx = 0;
      stmt.setLong(++idx, idAzienda);      
      
      ResultSet rs = stmt.executeQuery();   
      
      while(rs.next())
      {
        if(vComSez == null)
        {
          vComSez = new Vector<BaseCodeDescription>();
        }
        
        BaseCodeDescription bcd = new BaseCodeDescription();
        bcd.setDescription(rs.getString("COMUNE"));
        bcd.setItem(rs.getString("SEZIONE"));
        
        
        vComSez.add(bcd);
      }
      
      return vComSez;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("vComSez", vComSez) };
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getComuneAndSezioneForSigmater] ", t,
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
          "[ParticellaGaaDAO::getComuneAndSezioneForSigmater] END.");
    }
  }
  
  
  /**
   * 
   * Restituisce la somma della superificie utilizzata relativa all'azienda e alla particella
   * menu gli utilizzi indicati.
   * 
   * 
   * @param idAzienda
   * @param idParticellalong
   * @param vIdUtilizzo
   * @return
   * @throws DataAccessException
   */
  public BigDecimal getSumUtilizziPrimariNoIndicati(
      long idAzienda, long idParticella, Vector<String> vIdUtilizzo) 
  throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    BigDecimal somma = null;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::getSumUtilizziPrimariNoIndicati] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
          "SELECT NVL(SUM(UP.SUPERFICIE_UTILIZZATA),0) AS SOMMA " +
          "FROM   DB_UTE UT, " +
          "       DB_CONDUZIONE_PARTICELLA CP, " +
          "       DB_UTILIZZO_PARTICELLA UP " +
          "WHERE  CP.ID_PARTICELLA = ? "+
          "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
          "AND    UT.ID_AZIENDA = ? " +
          "AND    UT.ID_UTE = CP.ID_UTE " +
          "AND    UT.DATA_FINE_ATTIVITA IS NULL " +
          "AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA " +
          "AND    CP.ID_TITOLO_POSSESSO <> 5 ");
      if(vIdUtilizzo != null)
      {
        queryBuf.append(     
          "AND    UP.ID_UTILIZZO_PARTICELLA NOT IN (").append(
              this.getIdListFromVectorStringForSQL(vIdUtilizzo)).append(")");
      }
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getSumUtilizziPrimariNoIndicati] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int idx = 0;
      stmt.setLong(++idx, idParticella);
      stmt.setLong(++idx, idAzienda);
      
      
      
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      {
        somma = rs.getBigDecimal("SOMMA");
      }
      
      return somma;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("somma", somma)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("idParticella", idParticella),
        new Parametro("vIdUtilizzo", vIdUtilizzo)};

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getSumUtilizziPrimariNoIndicati] ", t,
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
          "[ParticellaGaaDAO::getSumUtilizziPrimariNoIndicati] END.");
    }
  }
  
  
  /**
   * 
   * Restituisce la somma della superificie utilizzata relativa all'azienda e alla particella
   * menu gli utilizzi indicati.
   * 
   * 
   * @param idAzienda
   * @param idParticellalong
   * @param vIdUtilizzo
   * @return
   * @throws DataAccessException
   */
  public BigDecimal getSumUtilizziPrimariConConduzioneNoIndicati(long idConduzioneParticella,
      Vector<String> vIdUtilizzo) 
  throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    BigDecimal somma = null;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::getSumUtilizziPrimariConConduzioneNoIndicati] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
          "SELECT NVL(SUM(UP.SUPERFICIE_UTILIZZATA),0) AS SOMMA " +
          "FROM   DB_CONDUZIONE_PARTICELLA CP, " +
          "       DB_UTILIZZO_PARTICELLA UP " +
          "WHERE  CP.ID_CONDUZIONE_PARTICELLA = ? " +
          "AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA " +
          "AND    CP.ID_TITOLO_POSSESSO <> 5 ");
      if(vIdUtilizzo != null)
      {
        queryBuf.append(     
          "AND    UP.ID_UTILIZZO_PARTICELLA NOT IN (").append(
              this.getIdListFromVectorStringForSQL(vIdUtilizzo)).append(")");
      }
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getSumUtilizziPrimariConConduzioneNoIndicati] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int idx = 0;
      stmt.setLong(++idx, idConduzioneParticella);
      
      
      
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      {
        somma = rs.getBigDecimal("SOMMA");
      }
      
      return somma;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("somma", somma)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("vIdUtilizzo", vIdUtilizzo),
        new Parametro("idConduzioneParticella", idConduzioneParticella)};

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getSumUtilizziPrimariConConduzioneNoIndicati] ", t,
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
          "[ParticellaGaaDAO::getSumUtilizziPrimariConConduzioneNoIndicati] END.");
    }
  }
  
  
  public BigDecimal getSumUtilizziPrimariParticellaAltreConduzioni(long idAzienda, long idParticella,
      Vector<Long> vIdConduzioneParticella) 
  throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    BigDecimal somma = null;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::getSumUtilizziPrimariParticellaAltreConduzioni] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
          "SELECT NVL(SUM(UP.SUPERFICIE_UTILIZZATA),0) AS SOMMA " +
          "FROM   DB_UTE UT, " +
          "       DB_CONDUZIONE_PARTICELLA CP, " +
          "       DB_UTILIZZO_PARTICELLA UP " +
          "WHERE  UT.ID_AZIENDA = ? " +
          "AND    CP.ID_PARTICELLA = ? " +
          "AND    UT.ID_UTE = CP.ID_UTE " +
          "AND    UT.DATA_FINE_ATTIVITA IS NULL " +
          "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
          "AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA " +
          "AND    CP.ID_TITOLO_POSSESSO <> 5 ");
      
      if(vIdConduzioneParticella != null)
      {
        queryBuf.append(     
          "AND    CP.ID_CONDUZIONE_PARTICELLA NOT IN (").append(
              this.getIdListFromVectorLongForSQL(vIdConduzioneParticella)).append(")");
      }
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getSumUtilizziPrimariParticellaAltreConduzioni] Query="
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
        somma = rs.getBigDecimal("SOMMA");
      }
      
      return somma;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("somma", somma)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("idParticella", idParticella),
        new Parametro("vIdConduzioneParticella", vIdConduzioneParticella)
      };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getSumUtilizziPrimariParticellaAltreConduzioni] ", t,
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
          "[ParticellaGaaDAO::getSumUtilizziPrimariParticellaAltreConduzioni] END.");
    }
  }
  
  /**
   * 
   * ritorna gli id_particella dall'idConduzioneParticella
   * 
   * @param vIdConduzioneParticella
   * @return
   * @throws DataAccessException
   */
  public Vector<Long> getIdParticellaByIdConduzione(Vector<Long> vIdConduzioneParticella) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<Long> vIdParticella = null;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::getIdParticellaByIdConduzione] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT " +
        "       DISTINCT CP.ID_PARTICELLA " +
        "FROM   DB_CONDUZIONE_PARTICELLA CP " +
        "WHERE  CP.ID_CONDUZIONE_PARTICELLA IN (").append(
        this.getIdListFromVectorLongForSQL(vIdConduzioneParticella)).append(")");
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getIdParticellaByIdConduzione] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);   
      
      
      ResultSet rs = stmt.executeQuery();
      while(rs.next())
      {
        if(vIdParticella == null)
        {
          vIdParticella = new Vector<Long>();
        }
        vIdParticella.add(new Long(rs.getLong("ID_PARTICELLA")));
      }
      
      return vIdParticella;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("vIdParticella", vIdParticella)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("vIdConduzioneParticella", vIdConduzioneParticella)
      };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getIdParticellaByIdConduzione] ", t,
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
          "[ParticellaGaaDAO::getIdParticellaByIdConduzione] END.");
    }
  }
  
  
  
  /**
   * Restituisce l'elenco delle provincie relative alla conduzioni delle
   * particelle
   * 
   * 
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<ProvinciaVO> getListProvincieParticelleByIdAzienda(Long idAzienda)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ProvinciaVO> result = null;
    StringBuffer queryBuf = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::getListProvincieParticelleByIdAzienda] BEGIN.");
      
      queryBuf = new StringBuffer();
      
      queryBuf.append(
        "SELECT DISTINCT P.ISTAT_PROVINCIA, " +
        "                P.SIGLA_PROVINCIA, " +
        "                P.DESCRIZIONE " +
        "FROM            DB_STORICO_PARTICELLA SP, " +  
        "                DB_CONDUZIONE_PARTICELLA CP, " +
        "                DB_UTE U, " +
        "                COMUNE C, " +
        "                PROVINCIA P " +
        "WHERE           U.ID_AZIENDA = ? " +
        "AND             U.ID_UTE = CP.ID_UTE " +
        "AND             CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
        "AND             SP.COMUNE = C.ISTAT_COMUNE " + 
        "AND             C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
        "ORDER BY P.DESCRIZIONE ASC ");
       
      
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
                "[ParticellaGaaDAO::getListProvincieParticelleByIdAzienda] Query="
                    + query);
      }
      
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);      
    
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {
        if(result == null)
        {
          result = new Vector<ProvinciaVO>();
        }
        
        
        ProvinciaVO provinciaVO = new ProvinciaVO(); 
        provinciaVO.setDescrizione(rs.getString("DESCRIZIONE"));
        provinciaVO.setIstatProvincia(rs.getString("ISTAT_PROVINCIA"));
        provinciaVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));    
        
        result.add(provinciaVO);
        
        
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query), 
        new Variabile("result", result)};
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getListProvincieParticelleByIdAzienda] ", t,
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
          "[ParticellaGaaDAO::getListProvincieParticelleByIdAzienda] END.");
    }
  }
  
  
  public Vector<ProvinciaVO> getListProvincieParticelleByIdDichiarazioneConsistenza(Long idDichiarazioneConsistenza)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ProvinciaVO> result = null;
    StringBuffer queryBuf = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::getListProvincieParticelleByIdDichiarazioneConsistenza] BEGIN.");
      
      queryBuf = new StringBuffer();
      
      queryBuf.append(
        "SELECT DISTINCT P.ISTAT_PROVINCIA, " +
        "                P.SIGLA_PROVINCIA, " +
        "                P.DESCRIZIONE " +
        "FROM            DB_STORICO_PARTICELLA SP, " +  
        "                DB_CONDUZIONE_DICHIARATA CD, " +
        "                DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "                COMUNE C, " +
        "                PROVINCIA P " +
        "WHERE           DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "AND             DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        "AND             CD.ID_PARTICELLA = SP.ID_PARTICELLA " +
        "AND             SP.COMUNE = C.ISTAT_COMUNE " + 
        "AND             C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
        "ORDER BY P.DESCRIZIONE ASC ");
       
      
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
                "[ParticellaGaaDAO::getListProvincieParticelleByIdDichiarazioneConsistenza] Query="
                    + query);
      }
      
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idDichiarazioneConsistenza);      
    
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {
        if(result == null)
        {
          result = new Vector<ProvinciaVO>();
        }
        
        
        ProvinciaVO provinciaVO = new ProvinciaVO(); 
        provinciaVO.setDescrizione(rs.getString("DESCRIZIONE"));
        provinciaVO.setIstatProvincia(rs.getString("ISTAT_PROVINCIA"));
        provinciaVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));    
        
        result.add(provinciaVO);
        
        
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query), 
        new Variabile("result", result)};
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getListProvincieParticelleByIdDichiarazioneConsistenza] ", t,
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
          "[ParticellaGaaDAO::getListProvincieParticelleByIdDichiarazioneConsistenza] END.");
    }
  }
  
  /**
   * ritona la somma della percentuale possesso relativa alle conduzioni
   * sulla stessa particella della stessa azienda meno le conduzioni indicate
   * 
   * 
   * 
   * @param idAzienda
   * @param idParticella
   * @param vIdConduzioneParticella
   * @return
   * @throws DataAccessException
   */
  public BigDecimal getSumPercentualPossessoAltreConduzioni(long idAzienda, long idParticella,
      Vector<Long> vIdConduzioneParticella) 
  throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    BigDecimal somma = null;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::getSumPercentualPossessoAltreConduzioni] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
          "SELECT NVL(SUM(CP.PERCENTUALE_POSSESSO),0) AS SOMMA " +
          "FROM   DB_UTE UT, " +
          "       DB_CONDUZIONE_PARTICELLA CP " +
          "WHERE  UT.ID_AZIENDA = ? " +
          "AND    CP.ID_PARTICELLA = ? " +
          "AND    UT.ID_UTE = CP.ID_UTE " +
          "AND    UT.DATA_FINE_ATTIVITA IS NULL " +
          "AND    CP.DATA_FINE_CONDUZIONE IS NULL ");
      
      if(vIdConduzioneParticella != null)
      {
        queryBuf.append(     
          "AND    CP.ID_CONDUZIONE_PARTICELLA NOT IN (").append(
              this.getIdListFromVectorLongForSQL(vIdConduzioneParticella)).append(")");
      }
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getSumPercentualPossessoAltreConduzioni] Query="
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
        somma = rs.getBigDecimal("SOMMA");
      }
      
      return somma;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("somma", somma)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("idParticella", idParticella),
        new Parametro("vIdConduzioneParticella", vIdConduzioneParticella)
      };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getSumPercentualPossessoAltreConduzioni] ", t,
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
          "[ParticellaGaaDAO::getSumPercentualPossessoAltreConduzioni] END.");
    }
  }
  
  
  /**
   * 
   * ritona la somma della superficie condotta relativa alle conduzioni
   * sulla stessa particella della stessa azienda meno le conduzioni indicate
   * 
   * 
   * 
   * @param idAzienda
   * @param idParticella
   * @param vIdConduzioneParticella
   * @return
   * @throws DataAccessException
   */
  public BigDecimal getSumSupCondottaAltreConduzioni(long idAzienda, long idParticella,
      Vector<Long> vIdConduzioneParticella) 
  throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    BigDecimal somma = null;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::getSumSupCondottaAltreConduzioni] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
          "SELECT NVL(SUM(CP.SUPERFICIE_CONDOTTA),0) AS SOMMA " +
          "FROM   DB_UTE UT, " +
          "       DB_CONDUZIONE_PARTICELLA CP " +
          "WHERE  UT.ID_AZIENDA = ? " +
          "AND    CP.ID_PARTICELLA = ? " +
          "AND    UT.ID_UTE = CP.ID_UTE " +
          "AND    UT.DATA_FINE_ATTIVITA IS NULL " +
          "AND    CP.DATA_FINE_CONDUZIONE IS NULL ");
      
      if(vIdConduzioneParticella != null)
      {
        queryBuf.append(     
          "AND    CP.ID_CONDUZIONE_PARTICELLA NOT IN (").append(
              this.getIdListFromVectorLongForSQL(vIdConduzioneParticella)).append(")");
      }
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getSumSupCondottaAltreConduzioni] Query="
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
        somma = rs.getBigDecimal("SOMMA");
      }
      
      return somma;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("somma", somma)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("idParticella", idParticella),
        new Parametro("vIdConduzioneParticella", vIdConduzioneParticella)
      };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getSumSupCondottaAltreConduzioni] ", t,
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
          "[ParticellaGaaDAO::getSumSupCondottaAltreConduzioni] END.");
    }
  }
  
  /**
   * 
   * EStrae i dati sia al piano n lavorazione che alla dichiarazione,
   * relativamente all'istanza di riesame ed alle sue fasi.
   * 
   * @param idAzienda
   * @param idParticella
   * @param dataInserimentoDichiarazione
   * @return
   * @throws DataAccessException
   */
  public Vector<IstanzaRiesameVO> getFasiIstanzaRiesame(long idAzienda, long idParticella,
      Date dataInserimentoDichiarazione) 
  throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<IstanzaRiesameVO> result = null;
    IstanzaRiesameVO istanzaRiesameVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::getFasiIstanzaRiesame] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
          "SELECT IR.ID_ISTANZA_RIESAME," +
          "       IR.ID_FASE_ISTANZA_RIESAME, " +
          "       FIR.DESCRIZIONE, " +
          "       IR.DATA_RICHIESTA, " +
          "       IR.DATA_ANNULLAMENTO, " +
          "       IR.DATA_EVASIONE, " +
          "       IR.DATA_INVIO_GIS, " +
          "       IR.DATA_SITICONVOCA, " +
          "       IR.DATA_SOSPENSIONE_SCADUTA, " +
          "       IR.DATA_AGGIORNAMENTO, " +
          "       IR.NOTE, " +
          "       IR.ANNO, " +
          "       IR.PROTOCOLLO_ISTANZA "  +
          "FROM   DB_ISTANZA_RIESAME IR, " +
          "       DB_FASE_ISTANZA_RIESAME FIR " +
          "WHERE  IR.ID_AZIENDA = ? " +
          "AND    IR.ID_PARTICELLA = ? " +
          "AND    IR.ID_FASE_ISTANZA_RIESAME = FIR.ID_FASE_ISTANZA_RIESAME ");
      if(Validator.isNotEmpty(dataInserimentoDichiarazione))
      {
        queryBuf.append(
          "AND    IR.DATA_RICHIESTA <= ? ");
      }
          

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getFasiIstanzaRiesame] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int idx = 0;
      
      stmt.setLong(++idx, idAzienda);
      stmt.setLong(++idx, idParticella);
      
      if(Validator.isNotEmpty(dataInserimentoDichiarazione))
      {
        
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoDichiarazione));
      }
      
      
      
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {
        if(result == null)
        {
          result = new Vector<IstanzaRiesameVO>();
        }
        
        istanzaRiesameVO = new IstanzaRiesameVO();
        istanzaRiesameVO.setIdIstanzaRiesame(checkLong(rs.getString("ID_ISTANZA_RIESAME")));
        istanzaRiesameVO.setIdFaseIstanzaRiesame(checkLong(rs.getString("ID_FASE_ISTANZA_RIESAME")));
        istanzaRiesameVO.setDescFaseIstanzaRiesame(rs.getString("DESCRIZIONE"));
        istanzaRiesameVO.setDataRichiesta(rs.getTimestamp("DATA_RICHIESTA"));
        istanzaRiesameVO.setDataAnnullamento(rs.getTimestamp("DATA_ANNULLAMENTO"));
        istanzaRiesameVO.setDataEvasione(rs.getTimestamp("DATA_EVASIONE"));
        istanzaRiesameVO.setDataInvioGis(rs.getTimestamp("DATA_INVIO_GIS"));
        istanzaRiesameVO.setDataSiticonvoca(rs.getTimestamp("DATA_SITICONVOCA"));
        istanzaRiesameVO.setDataSospensioneScaduta(rs.getTimestamp("DATA_SOSPENSIONE_SCADUTA"));
        istanzaRiesameVO.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO"));
        istanzaRiesameVO.setNote(rs.getString("NOTE"));
        istanzaRiesameVO.setAnno(checkIntegerNull(rs.getString("ANNO")));
        istanzaRiesameVO.setProtocolloIstanza(rs.getString("PROTOCOLLO_ISTANZA"));
        
        result.add(istanzaRiesameVO);
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("result", result), new Variabile("istanzaRiesameVO", istanzaRiesameVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("idParticella", idParticella),
        new Parametro("dataInserimentoDichiarazione", dataInserimentoDichiarazione)
      };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getFasiIstanzaRiesame] ", t,
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
          "[ParticellaGaaDAO::getFasiIstanzaRiesame] END.");
    }
  }
  
  /**
   * 
   * ritorna i dati dell'istanza di riesame quando la tabella
   * DB_ISTANZA_RIESAME non ritorna nessun record!!! 
   * 
   * 
   * @param idAzienda
   * @param idParticella
   * @return
   * @throws DataAccessException
   */
  public Vector<IstanzaRiesameVO> getFasiIstanzaRiesameNoRecord(long idAzienda, long idParticella) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<IstanzaRiesameVO> result = null;
    IstanzaRiesameVO istanzaRiesameVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::getFasiIstanzaRiesameNoRecord] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT FIR.DESCRIZIONE, " +
        "       FIR.ID_FASE_ISTANZA_RIESAME, " +
        "       (SELECT MAX (DC.DATA_INSERIMENTO) AS DATA_RICHIESTA " +
        "        FROM   DB_DOCUMENTO D, " +
        "               DB_DOCUMENTO_CONDUZIONE DC, " +
        "               DB_CONDUZIONE_PARTICELLA CP, " +
        "               DB_TIPO_CATEGORIA_DOCUMENTO CD, " +
        "               DB_DOCUMENTO_CATEGORIA DCA " +
        "        WHERE  CD.TIPO_IDENTIFICATIVO = 'TC' " +
        "        AND    CD.IDENTIFICATIVO = 518 " +
        "        AND    CD.ID_CATEGORIA_DOCUMENTO = DCA.ID_CATEGORIA_DOCUMENTO " +
        "        AND    D.EXT_ID_DOCUMENTO = DCA.ID_DOCUMENTO " +
        "        AND    NVL (D.DATA_FINE_VALIDITA, TO_DATE ('31/12/9999', 'DD/MM/YYYY')) >  SYSDATE " +
        "        AND    D.ID_STATO_DOCUMENTO IS NULL " +
        "        AND    DC.ID_DOCUMENTO = D.ID_DOCUMENTO " +
        "        AND    NVL (DC.DATA_FINE_VALIDITA, TO_DATE ('31/12/9999', 'DD/MM/YYYY')) > SYSDATE " +
        "        AND    CP.ID_CONDUZIONE_PARTICELLA = DC.ID_CONDUZIONE_PARTICELLA " +
        "        AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "        AND    D.ID_AZIENDA = ? " +
        "        AND    CP.ID_PARTICELLA = ? )  AS DATA_RICHIESTA " +      
        "FROM    DB_FASE_ISTANZA_RIESAME FIR " +
        "WHERE   FIR.ID_FASE_ISTANZA_RIESAME = 1");
          

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getFasiIstanzaRiesameNoRecord] Query="
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
        if(rs.getTimestamp("DATA_RICHIESTA") != null)
        {
          if(result == null)
          {
            result = new Vector<IstanzaRiesameVO>();
          }
          
          istanzaRiesameVO = new IstanzaRiesameVO();
          istanzaRiesameVO.setIdFaseIstanzaRiesame(checkLong(rs.getString("ID_FASE_ISTANZA_RIESAME")));
          istanzaRiesameVO.setDescFaseIstanzaRiesame(rs.getString("DESCRIZIONE"));
          istanzaRiesameVO.setDataRichiesta(rs.getTimestamp("DATA_RICHIESTA"));
          
          result.add(istanzaRiesameVO);
        }
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("result", result), new Variabile("istanzaRiesameVO", istanzaRiesameVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("idParticella", idParticella)
      };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getFasiIstanzaRiesameNoRecord] ", t,
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
          "[ParticellaGaaDAO::getFasiIstanzaRiesameNoRecord] END.");
    }
  }
  
  /**
   * ritorna tutti i tipi dei casi particolari.
   * Se valorizzato particellaObbligatoria ritorna solo quelli 
   * col valore passato.
   * 
   * 
   * 
   * @param particellaObbligatoria
   * @return
   * @throws DataAccessException
   */
  public Vector<CasoParticolareVO> getCasiParticolari(String particellaObbligatoria) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<CasoParticolareVO> result = null;
    CasoParticolareVO casoParticolareVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::getCasiParticolari] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT TCP.ID_CASO_PARTICOLARE, " +
        "       TCP.DESCRIZIONE, " +
        "       TCP.PARTICELLA_OBBLIGATORIA " +
        "FROM   DB_TIPO_CASO_PARTICOLARE TCP " +
        "WHERE  1 = 1");
      
      if(Validator.isNotEmpty(particellaObbligatoria))
      {
        queryBuf.append(
        "AND    TCP.PARTICELLA_OBBLIGATORIA = ? ");
      }
          
      queryBuf.append(
        "ORDER BY TCP.ID_CASO_PARTICOLARE");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getCasiParticolari] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int idx = 0;
      
      if(Validator.isNotEmpty(particellaObbligatoria))
      {
        stmt.setString(++idx, particellaObbligatoria);
      }
      
      
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {        
        if(result == null)
        {
          result = new Vector<CasoParticolareVO>();
        }
        
        casoParticolareVO = new CasoParticolareVO();
        casoParticolareVO.setIdCasoParticolare(rs.getLong("ID_CASO_PARTICOLARE"));
        casoParticolareVO.setDescrizione(rs.getString("DESCRIZIONE"));
        casoParticolareVO.setParticellaObbligatoria(rs.getString("PARTICELLA_OBBLIGATORIA"));
        
        result.add(casoParticolareVO);
        
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("result", result), new Variabile("casoParticolareVO", casoParticolareVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("particellaObbligatoria", particellaObbligatoria)
      };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getCasiParticolari] ", t,
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
          "[ParticellaGaaDAO::getCasiParticolari] END.");
    }
  }
  
  /**
   * 
   * Restituisce solo gli idParticella di tutte le particella in conduzione
   * attive dell'azienda
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<Long> getListIdParticellaFromIdAzienda(long idAzienda) 
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
          "[ParticellaGaaDAO::getListIdParticellaFromIdAzienda] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT CP.ID_PARTICELLA " +
        "FROM   DB_CONDUZIONE_PARTICELLA CP, " + 
        "       DB_UTE U " +
        "WHERE  U.ID_AZIENDA = ? " +
        "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "AND    U.ID_UTE = CP.ID_UTE " +
        "GROUP BY CP.ID_PARTICELLA");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getListIdParticellaFromIdAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int idx = 0;
      
      stmt.setLong(++idx, idAzienda);     
      
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {        
        if(result == null)
        {
          result = new Vector<Long>();
        }
        
        result.add(checkLong(rs.getString("ID_PARTICELLA")));        
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
      { new Parametro("idAzienda", idAzienda)
      };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getListIdParticellaFromIdAzienda] ", t,
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
          "[ConduzioneParticellaGaaDAO::getListIdParticellaFromIdAzienda] END.");
    }
  }
  
  
  /**
   * 
   * ritorna tutti gli idConduzione delle particelle dell'azienda
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<Long> getListIdConduzioneFromIdAzienda(long idAzienda) 
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
          "[ParticellaGaaDAO::getListIdConduzioneFromIdAzienda] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT CP.ID_CONDZIONE_PARTICELLA " +
        "FROM   DB_CONDUZIONE_PARTICELLA CP, " + 
        "       DB_UTE U " +
        "WHERE  U.ID_AZIENDA = ? " +
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
            "[ParticellaGaaDAO::getListIdConduzioneFromIdAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int idx = 0;
      
      stmt.setLong(++idx, idAzienda);     
      
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
      { new Parametro("idAzienda", idAzienda)
      };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getListIdConduzioneFromIdAzienda] ", t,
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
          "[ConduzioneParticellaGaaDAO::getListIdConduzioneFromIdAzienda] END.");
    }
  }
  
  
  /**
   * 
   * Aggiorna il flag schedario delle particelle 
   * a S a seconda delle uv di tutta la particella
   * 
   * 
   * 
   * @param vIdParticella
   * @throws DataAccessException
   */
  public void updateParticelleSchedarioS(Vector<Long> vIdParticella) 
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
              "[ParticellaGaaDAO::updateParticelleSchedarioS] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
          "UPDATE DB_PARTICELLA PAR SET PAR.FLAG_SCHEDARIO = 'S' " +
          "WHERE  (SELECT COUNT(0) " +
          "        FROM   DB_STORICO_UNITA_ARBOREA SUA " +
          "        WHERE  SUA.DATA_FINE_VALIDITA IS NULL "+
          "        AND    SUA.ID_PARTICELLA = PAR.ID_PARTICELLA " +
          "        AND    NVL(SUA.STATO_UNITA_ARBOREA,'P') = 'P') = 0 " +
          "AND     PAR.ID_PARTICELLA IN ( ").append(
           this.getIdListFromVectorLongForSQL(vIdParticella)).append(")");
      
      
      
          
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::updateParticelleSchedarioS] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("vIdParticella", vIdParticella)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[ParticellaGaaDAO::updateParticelleSchedarioS] ",
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
              "[ParticellaGaaDAO::updateParticelleSchedarioS] END.");
    }
  }
  
  /**
   * 
   *  Aggiorna il flag schedario delle particelle 
   *  a M a seconda delle uv di tutta la particella
   * 
   * 
   * @param vIdParticella
   * @throws DataAccessException
   */
  public void updateParticelleSchedarioM(Vector<Long> vIdParticella) 
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
              "[ParticellaGaaDAO::updateParticelleSchedarioM] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
          "UPDATE DB_PARTICELLA PAR SET PAR.FLAG_SCHEDARIO = 'M' " + 
          "WHERE  (SELECT COUNT(0) " +
          "        FROM    DB_STORICO_UNITA_ARBOREA SUA " +
          "        WHERE   SUA.DATA_FINE_VALIDITA IS NULL " +
          "        AND     SUA.ID_PARTICELLA = PAR.ID_PARTICELLA " +
          "        AND     NVL(SUA.STATO_UNITA_ARBOREA,'P') = 'P') > 0 " +
          "AND     PAR.ID_PARTICELLA IN ( ").append(
           this.getIdListFromVectorLongForSQL(vIdParticella)).append(")");
      
      
      
          
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::updateParticelleSchedarioM] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("vIdParticella", vIdParticella)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[ParticellaGaaDAO::updateParticelleSchedarioM] ",
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
              "[ParticellaGaaDAO::updateParticelleSchedarioM] END.");
    }
  }
  
  
  /**
   * Richiama il plsql calcolaP30
   * 1 - in P30
   * 0 - altrimenti
   * 
   * 
   * 
   * @param idStoricoParticella
   * @param dataInizioValidita
   * @return
   * @throws DataAccessException
   */
  public int calcolaP30PlSql(long idStoricoParticella, Date dataInizioValidita) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    CallableStatement cs = null;
    StringBuffer queryBuf = null;
    int result = 0;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::calcolaP30PlSql] BEGIN.");
      /***
       *  FUNCTION Calcola_P30 (pIdStoricoParticella IN DB_STORICO_PARTICELLA.ID_STORICO_PARTICELLA%TYPE,
       *                  pDataConfronto       IN DB_PARTICELLA_CERTIFICATA.DATA_INIZIO_VALIDITA%TYPE) 
       *                  RETURN NUMBER IS
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{ ? = call PCK_SMRGAA_LIBRERIA.Calcola_P30(?,?)}");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::calcolaP30PlSql] Query="
                + query);
      }
      
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.registerOutParameter(1,Types.INTEGER);
      cs.setLong(2, idStoricoParticella);
      cs.setTimestamp(3, convertDateToTimestamp(dataInizioValidita));
      
      cs.executeUpdate();
      
      result = cs.getInt(1);    
      
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
      { new Parametro("idStoricoParticella", idStoricoParticella),
        new Parametro("dataInizioValidita", dataInizioValidita)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[ParticellaGaaDAO::calcolaP30PlSql] ",
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
      closePlSql(cs, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::calcolaP30PlSql] END.");
    }
  }
  
  
  /**
   * 
   * Richiama il plsql calcolaP30
   * 1 - in P25
   * 0 - altrimenti
   * 
   * 
   * @param idStoricoParticella
   * @param dataInizioValidita
   * @return
   * @throws DataAccessException
   */
  public int calcolaP25PlSql(long idStoricoParticella, Date dataInizioValidita) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    CallableStatement cs = null;
    StringBuffer queryBuf = null;
    int result = 0;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::calcolaP25PlSql] BEGIN.");
      /***
       *  FUNCTION Calcola_P25 (pIdStoricoParticella IN DB_STORICO_PARTICELLA.ID_STORICO_PARTICELLA%TYPE,
       *                  pDataConfronto       IN DB_PARTICELLA_CERTIFICATA.DATA_INIZIO_VALIDITA%TYPE) 
       *                  RETURN NUMBER IS
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{ ? = call PCK_SMRGAA_LIBRERIA.Calcola_P25(?,?)}");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::calcolaP25PlSql] Query="
                + query);
      }
      
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.registerOutParameter(1,Types.INTEGER);
      cs.setLong(2, idStoricoParticella);
      cs.setTimestamp(3, convertDateToTimestamp(dataInizioValidita));
      
      cs.executeUpdate();
      
      result = cs.getInt(1);    
      
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
      { new Parametro("idStoricoParticella", idStoricoParticella),
        new Parametro("dataInizioValidita", dataInizioValidita)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[ParticellaGaaDAO::calcolaP25PlSql] ",
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
      closePlSql(cs, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::calcolaP25PlSql] END.");
    }
  }
  
  
  /**
   * Richiama il plsql calcolaP26
   * 1 - in P26
   * 0 - altrimenti
   * 
   * 
   * 
   * @param idAzienda
   * @param idParticella
   * @param idParticellaCertificata
   * @return
   * @throws DataAccessException
   */
  public int calcolaP26PlSql(long idAzienda, long idParticella, Long idParticellaCertificata) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    CallableStatement cs = null;
    StringBuffer queryBuf = null;
    int result = 0;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::calcolaP26PlSql] BEGIN.");
      /***
       *  FUNCTION Calcola_P26(pIdAzienda      IN DB_ANAGRAFICA_AZIENDA.ID_AZIENDA%TYPE,
       *                                pIdParticella   IN DB_PARTICELLA.ID_PARTICELLA%TYPE,
       *                                pIdPartCertif   IN DB_PARTICELLA_CERTIFICATA.ID_PARTICELLA_CERTIFICATA%TYPE)  RETURN NUMBER IS
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{ ? = call PCK_SMRGAA_LIBRERIA.Calcola_P26(?,?,?)}");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::calcolaP26PlSql] Query="
                + query);
      }
      
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.registerOutParameter(1,Types.INTEGER);
      cs.setLong(2, idAzienda);
      cs.setLong(3, idParticella);
      if(Validator.isNotEmpty(idParticellaCertificata))
      {
        cs.setLong(4, idParticellaCertificata.longValue());
      }
      else
      {
        cs.setNull(4, Types.DECIMAL);
      }
      
      cs.executeUpdate();
      
      result = cs.getInt(1);    
      
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
        new Parametro("idParticella", idParticella),
        new Parametro("idParticellaCertificata", idParticellaCertificata)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[ParticellaGaaDAO::calcolaP26PlSql] ",
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
      closePlSql(cs, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::calcolaP26PlSql] END.");
    }
  }
  
  
  
  /**
   * è utilizzate nelle modifche dei documenti
   * per allineare i dati dell'istanza di riesame
   * 
   * 
   * @param idAzienda
   * @param anno
   * @param fase
   * @return
   * @throws DataAccessException
   */
  public PlSqlCodeDescription annullaIstanzaPlSql(long idAzienda, 
      int anno, int fase, long idUtente) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    CallableStatement cs = null;
    StringBuffer queryBuf = null;
    PlSqlCodeDescription plqObj = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::annullaIstanzaPlSql] BEGIN.");
      /***
       *  PROCEDURE AnnullaIstanza (pIdAzienda        IN DB_ISTANZA_RIESAME.ID_AZIENDA%TYPE,
                    pAnnoDocumento    IN DB_ISTANZA_RIESAME.ANNO%TYPE,
                    pIdFaseIstRiesame IN DB_ISTANZA_RIESAME.ID_FASE_ISTANZA_RIESAME%TYPE,
                    pIdUtente         IN DB_ISTANZA_RIESAME.ID_UTENTE_RICHIEDENTE%TYPE,
                    pCodErr          OUT VARCHAR2,
                    pDesErr          OUT VARCHAR2)
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{call Pck_Gestione_Istanza_Riesame.AnnullaIstanza(?,?,?,?,?,?)}");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::annullaIstanzaPlSql] Query="
                + query);
      }
      
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.setLong(1, idAzienda);
      cs.setInt(2, anno);
      cs.setInt(3, fase);
      cs.setLong(4, idUtente);
      cs.registerOutParameter(5,Types.VARCHAR); //codice errore
      cs.registerOutParameter(6,Types.VARCHAR); //msg errore
      
      cs.executeUpdate();
      
      plqObj = new PlSqlCodeDescription();  
      plqObj.setDescription(cs.getString(5));
      plqObj.setOtherdescription(cs.getString(6));
      
      return plqObj;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("plqObj", plqObj) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("anno", anno),
        new Parametro("fase", fase),
        new Parametro("idUtente", idUtente) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[ParticellaGaaDAO::annullaIstanzaPlSql] ",
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
      closePlSql(cs, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::annullaIstanzaPlSql] END.");
    }
  }
  
  
  /**
   * 
   * metodoutilizzato per inserire i dati per la creazione
   * di una istanza di riesame
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public PlSqlCodeDescription inserisciIstanzaPlSql(long idAzienda, int anno) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    CallableStatement cs = null;
    StringBuffer queryBuf = null;
    PlSqlCodeDescription plqObj = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::inserisciIstanzaPlSql] BEGIN.");
      /***
       *  PROCEDURE InserisciIstanza (pIdAzienda        IN DB_ISTANZA_RIESAME.ID_AZIENDA%TYPE,
       *                        pAnnoDocumento    IN DB_ISTANZA_RIESAME.ANNO%TYPE,
       *                        pCodErr          OUT VARCHAR2,
       *                        pDesErr          OUT VARCHAR2)
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{call Pck_Gestione_Istanza_Riesame.InserisciIstanza(?,?,?,?)}");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::inserisciIstanzaPlSql] Query="
                + query);
      }
      
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.setLong(1, idAzienda);
      cs.setLong(2, anno);
      cs.registerOutParameter(3,Types.VARCHAR); //codice errore
      cs.registerOutParameter(4,Types.VARCHAR); //msg errore
      
      cs.executeUpdate();
      
      plqObj = new PlSqlCodeDescription();  
      plqObj.setDescription(cs.getString(3));
      plqObj.setOtherdescription(cs.getString(4));
      
      return plqObj;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("plqObj", plqObj) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("anno", anno)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[ParticellaGaaDAO::inserisciIstanzaPlSql] ",
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
      closePlSql(cs, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::inserisciIstanzaPlSql] END.");
    }
  }
  
  /**
   * 
   * Restituisce solo gli idParticella di tutte le particella in cui insitono
   * dele uv attive dell'azienda
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<Long> getListIdParticellaFromIdAziendaISUV(long idAzienda) 
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
          "[ParticellaGaaDAO::getListIdParticellaFromIdAziendaISUV] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT DISTINCT SUA.ID_PARTICELLA " +
        "FROM DB_STORICO_UNITA_ARBOREA SUA " +
        "WHERE SUA.DATA_FINE_VALIDITA IS NULL " +
        "AND   SUA.ID_AZIENDA = ? ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getListIdParticellaFromIdAziendaISUV] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int idx = 0;
      
      stmt.setLong(++idx, idAzienda);     
      
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {        
        if(result == null)
        {
          result = new Vector<Long>();
        }
        
        result.add(checkLong(rs.getString("ID_PARTICELLA")));        
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
      { new Parametro("idAzienda", idAzienda)
      };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getListIdParticellaFromIdAziendaISUV] ", t,
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
          "[ConduzioneParticellaGaaDAO::getListIdParticellaFromIdAziendaISUV] END.");
    }
  }
  
  
  /**
   * ritorna i riepilogo del tipo corrispondente a funzionalità.
   * Se sulla tabella di relazione DB_R_RUOLO_IRIDE2_RIEPILOGO trova
   * la corrispondenza con codice_ruolo ritorna solo quelli,
   * altrimenti tutti
   * 
   * 
   * 
   * 
   * @param funzionalita
   * @param codiceRuolo
   * @return
   * @throws DataAccessException
   */
  public Vector<TipoRiepilogoVO> getTipoRiepilogo(String funzionalita, String codiceRuolo) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoRiepilogoVO> result = null;
    TipoRiepilogoVO tipoRiepilogoVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::getTipoRiepilogo] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT TR.ID_TIPO_RIEPILOGO," +
        "       TR.NOME, " +
        "       TR.DESCRIZIONE, " +
        "       TR.FLAG_DEFAULT, " +
        "       TR.FLAG_ESCLUDI_ASSERVIMENTO " +
        "FROM   DB_TIPO_RIEPILOGO TR " +
        "WHERE  TR.FLAG_FUNZIONALITA = ? " +
        "AND    TR.DATA_FINE_VALIDITA IS NULL " +
        "AND    (TR.ID_TIPO_RIEPILOGO IN (SELECT RIR.ID_TIPO_RIEPILOGO " +
        "                                 FROM   DB_R_RUOLO_IRIDE2_RIEPILOGO RIR, " +
        "                                        DB_TIPO_RIEPILOGO TR2 " +
        "                                 WHERE  RIR.DATA_FINE_VALIDITA IS NULL " +
        "                                 AND    RIR.CODICE_RUOLO = ? " +
        "                                 AND    TR2.FLAG_FUNZIONALITA = TR.FLAG_FUNZIONALITA " +
        "                                 AND    RIR.ID_TIPO_RIEPILOGO = TR2.ID_TIPO_RIEPILOGO) " +
        "        OR NOT EXISTS (SELECT RIR.ID_TIPO_RIEPILOGO " +
        "                       FROM   DB_R_RUOLO_IRIDE2_RIEPILOGO RIR," +
        "                              DB_TIPO_RIEPILOGO TR2 " +
        "                       WHERE  RIR.DATA_FINE_VALIDITA IS NULL " +
        "                       AND    RIR.CODICE_RUOLO = ? " +
        "                       AND    TR2.FLAG_FUNZIONALITA = TR.FLAG_FUNZIONALITA " +
        "                       AND    RIR.ID_TIPO_RIEPILOGO = TR2.ID_TIPO_RIEPILOGO)) " +
        "ORDER BY TR.ORDINE ASC ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getTipoRiepilogo] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int idx = 0;
      
      stmt.setString(++idx, funzionalita);
      stmt.setString(++idx, codiceRuolo);
      stmt.setString(++idx, codiceRuolo);
      
      
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {        
        if(result == null)
        {
          result = new Vector<TipoRiepilogoVO>();
        }
        
        tipoRiepilogoVO = new TipoRiepilogoVO();
        tipoRiepilogoVO.setIdTipoRiepilogo(rs.getLong("ID_TIPO_RIEPILOGO"));
        tipoRiepilogoVO.setNome(rs.getString("NOME"));
        tipoRiepilogoVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoRiepilogoVO.setFlagDefault(rs.getString("FLAG_DEFAULT"));
        tipoRiepilogoVO.setFlagEscludiAsservimento(rs.getString("FLAG_ESCLUDI_ASSERVIMENTO"));
        
        result.add(tipoRiepilogoVO);
        
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("result", result), new Variabile("tipoRiepilogoVO", tipoRiepilogoVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("funzionalita", funzionalita),
          new Parametro("codiceRuolo", codiceRuolo)
      };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getTipoRiepilogo] ", t,
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
          "[ParticellaGaaDAO::getTipoRiepilogo] END.");
    }
  }
  
  
  /**
   * 
   * Controlla che sulla particella nn vi siano altre unita arboree
   * attive nn associate a nessuna azienda
   * 
   * 
   * @param idParticella
   * @return
   * @throws DataAccessException
   */
  public boolean isAltreUvDaSchedario(long idParticella) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean esisteUVInSchedario = false;
    
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::isAltreUvDaSchedario] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT SUA.ID_STORICO_UNITA_ARBOREA " +
        "FROM   DB_STORICO_UNITA_ARBOREA SUA " +
        "WHERE  SUA.ID_PARTICELLA = ? " +
        "AND    SUA.DATA_CESSAZIONE IS NULL " +
        "AND    SUA.DATA_FINE_VALIDITA IS NULL " +
        "AND    SUA.ID_AZIENDA IS NULL ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::isAltreUvDaSchedario] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int idx = 0;
      
      stmt.setLong(++idx, idParticella);
      
      
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      {        
        esisteUVInSchedario = true;        
      }
      
      return esisteUVInSchedario;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("esisteUVInSchedario", esisteUVInSchedario) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idParticella", idParticella) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::isAltreUvDaSchedario] ", t,
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
          "[ParticellaGaaDAO::isAltreUvDaSchedario] END.");
    }
  }
  
  
  /**
   * ritorna i riepilogo del tipo corrispondente a funzionalità.
   * Se sulla tabella di relazione DB_R_RUOLO_IRIDE2_RIEPILOGO trova
   * la corrispondenza con codice_ruolo ritorna solo quelli,
   * altrimenti tutti
   * 
   * 
   * 
   * 
   * @param funzionalita
   * @param codiceRuolo
   * @return
   * @throws DataAccessException
   */
  public Vector<TipoEsportazioneDatiVO> getTipoEsportazioneDati(String codMenu, String codiceRuolo) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoEsportazioneDatiVO> result = null;
    TipoEsportazioneDatiVO tipoEsportazioneDatiVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::getTipoEsportazioneDati] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT TED.ID_ESPORTAZIONE_DATI, " +
        "       TED.NOME, " +
        "       TED.DESCRIZIONE, " +
        "       TED.CODICE_ESPORTAZIONE, " +
        "       TED.ESPORTAZIONE_DICHIARAZIONE, " +
        "       TED.ESPORTAZIONE_IN_LAVORAZIONE " +
        "FROM   DB_R_MENU_ESPORTAZIONE RME, " +
        "       DB_TIPO_ESPORTAZIONE_DATI TED, " +
        "       DB_TIPO_MENU TM " +
        "WHERE  TM.CODICE_MENU = ? " +
        "AND    TM.ID_TIPO_MENU = RME.ID_TIPO_MENU " +
        "AND    RME.DATA_FINE_VALIDITA IS NULL " +
        "AND    TED.DATA_FINE_VALIDITA IS NULL " +
        "AND    RME.ID_ESPORTAZIONE_DATI = TED.ID_ESPORTAZIONE_DATI " +    
        "AND    (TED.ID_ESPORTAZIONE_DATI IN (SELECT RRE.ID_ESPORTAZIONE_DATI " +
        "                                     FROM   DB_R_RUOLO_ESPORTAZIONE RRE " +
        "                                     WHERE  RRE.DATA_FINE_VALIDITA IS NULL " +
        "                                     AND    RRE.CODICE_RUOLO = ? ) " + 
        "        OR NOT EXISTS (SELECT RRE.ID_ESPORTAZIONE_DATI " +
        "                       FROM   DB_R_RUOLO_ESPORTAZIONE RRE " +
        "                       WHERE  RRE.DATA_FINE_VALIDITA IS NULL " +
        "                       AND    RRE.CODICE_RUOLO = ? )) " +
        "ORDER BY TED.ORDINE ASC ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getTipoEsportazioneDati] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int idx = 0;
      
      stmt.setString(++idx, codMenu);
      stmt.setString(++idx, codiceRuolo);
      stmt.setString(++idx, codiceRuolo);
      
      
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {        
        if(result == null)
        {
          result = new Vector<TipoEsportazioneDatiVO>();
        }
        
        tipoEsportazioneDatiVO = new TipoEsportazioneDatiVO();
        tipoEsportazioneDatiVO.setIdTipoEsportazioneDati(rs.getLong("ID_ESPORTAZIONE_DATI"));
        tipoEsportazioneDatiVO.setNome(rs.getString("NOME"));
        tipoEsportazioneDatiVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoEsportazioneDatiVO.setCodiceEsportazione(rs.getString("CODICE_ESPORTAZIONE"));
        tipoEsportazioneDatiVO.setEsportazioneDichiarazione(rs.getString("ESPORTAZIONE_DICHIARAZIONE"));
        tipoEsportazioneDatiVO.setEsportazioneInLavorazione(rs.getString("ESPORTAZIONE_IN_LAVORAZIONE"));
        
        result.add(tipoEsportazioneDatiVO);
        
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("result", result), new Variabile("tipoEsportazioneDatiVO", tipoEsportazioneDatiVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("codMenu", codMenu),
          new Parametro("codiceRuolo", codiceRuolo)
      };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getTipoEsportazioneDati] ", t,
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
          "[ParticellaGaaDAO::getTipoEsportazioneDati] END.");
    }
  }
  
  
  public boolean isParticellAttivaStoricoParticella(String istatComune, String sezione,
      String foglio, String particella, String subalterno) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean trovata = false;
    try
    {
      SolmrLogger.debug(this, "[ParticellaGaaDAO::isParticellAttivaStoricoParticella] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf = new StringBuffer(
        "SELECT SP.ID_PARTICELLA " +
        "FROM   DB_STORICO_PARTICELLA SP " +
        "WHERE  SP.COMUNE = ? ");
      if(Validator.isNotEmpty(sezione))
      {
        queryBuf.append(
        "AND    SP.SEZIONE = ? ");
      }
      else
      {
        queryBuf.append(
        "AND    SP.SEZIONE IS NULL ");
      }
      queryBuf.append(
        "AND    SP.FOGLIO = ? " +
        "AND    SP.ID_PARTICELLA = ? ");
      if(Validator.isNotEmpty(subalterno))
      {
        queryBuf.append(
        "AND    SP.SUBALTERNO = UPPER(?) ");
      }
      else
      {
        queryBuf.append(
        "AND    SP.SUBALTERNO IS NULL ");
      }
      queryBuf.append(
        "AND    SP.DATA_FINE_VALIDITA IS NULL ");

      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[ParticellaGaaDAO::isParticellAttivaStoricoParticella] Query="
            + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setString(++indice, istatComune);
      if (Validator.isNotEmpty(sezione))
      {
        stmt.setString(++indice, sezione.toUpperCase());
      }
      stmt.setLong(++indice, new Long(foglio).longValue());
      stmt.setLong(++indice, new Long(particella).longValue());
      if (Validator.isNotEmpty(subalterno))
      {
        stmt.setString(++indice, subalterno.toUpperCase());
      }

      ResultSet rs = stmt.executeQuery();
     
      if(rs.next())
      {
        trovata = true;
      }
      
      return trovata;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("trovata", trovata) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("istatComune", istatComune),
        new Parametro("sezione", sezione),
        new Parametro("foglio", foglio),
        new Parametro("particella", particella),
        new Parametro("subalterno", subalterno) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[ParticellaGaaDAO::isParticellAttivaStoricoParticella] ", t,
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
      SolmrLogger.debug(this, "[ParticellaGaaDAO::isParticellAttivaStoricoParticella] END.");
    }
  }
  
  
  /**
   * 
   * Restituisce l'elenco delle particelle selzionabili per le notifiche
   * 
   * 
   * @param filtriRicercaTerrenoVO
   * @return
   * @throws DataAccessException
   */
  public Vector<StoricoParticellaVO> getElencoParticelleForPopNotifica(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO) throws DataAccessException
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
      SolmrLogger.debug(this, "[ParticellaGaaDAO::getElencoParticelleForPopNotifica] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf = new StringBuffer(
        "SELECT SP.ID_STORICO_PARTICELLA, " +
        "       SP.COMUNE, " +
        "       C.DESCOM, " +
        "       P.SIGLA_PROVINCIA, " +
        "       SP.SEZIONE, " +
        "       SP.FOGLIO, " +
        "       SP.PARTICELLA, " +
        "       SP.SUBALTERNO, " +
        "       SP.ID_PARTICELLA," +
        "       CD.ID_CONDUZIONE_DICHIARATA, " +
        "       CD.ID_TITOLO_POSSESSO, " +
        "       CD.PERCENTUALE_POSSESSO, " +
        "       UD.ID_UTILIZZO_DICHIARATO, " +
        "       UD.SUPERFICIE_UTILIZZATA, " +
        "       TU.CODICE AS COD_UTILIZZO, " +
        "       TU.DESCRIZIONE AS DESC_TIPO_UTILIZZO, " +
        "       TV.DESCRIZIONE AS DESC_VARIETA, " +
        "       TV.CODICE_VARIETA AS COD_VAR " +
        "FROM   DB_STORICO_PARTICELLA SP, " + 
        "       COMUNE C, " +
        "       PROVINCIA P, " +
        "       DB_TIPO_VARIETA TV, " +
        "       DB_TIPO_UTILIZZO TU, " +
        "       DB_CONDUZIONE_DICHIARATA CD, " +
        "       DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "       DB_UTILIZZO_DICHIARATO UD ");
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo()))
      {
        queryBuf.append(
        ",DB_DICHIARAZIONE_SEGNALAZIONE DS ");
      }
      
      queryBuf.append(
        "WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        "AND    CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
        "AND    SP.COMUNE = C.ISTAT_COMUNE " +
        "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA ");
      
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdUtilizzo())
        && (filtriParticellareRicercaVO.getIdUtilizzo().intValue() !=0)
        && (filtriParticellareRicercaVO.getIdUtilizzo().intValue() !=-1))
      {
        queryBuf.append(
        "AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA " +
        "AND    UD.ID_UTILIZZO = ? " +
        "AND    UD.ID_UTILIZZO = TU.ID_UTILIZZO " +
        "AND    UD.ID_VARIETA  = TV.ID_VARIETA ");
      }
      else
      {
        queryBuf.append(
        "AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA(+) " +
        "AND    UD.ID_UTILIZZO = TU.ID_UTILIZZO (+) " +
        "AND    UD.ID_VARIETA  = TV.ID_VARIETA (+) ");
      }
      
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIstatComune()))
      {
        queryBuf.append(
        "AND    SP.COMUNE = ? ");
      }
      
      // Se la sezione è valorizzata, ricerco anche per sezione
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSezione()))
      {
        queryBuf.append(
        " AND SP.SEZIONE = UPPER(?) ");
      }
      
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getFoglio()))
      {
        queryBuf.append(
        "AND    SP.FOGLIO = ? ");
      }
      
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getParticella()))
      {
        queryBuf.append(
        "AND    SP.PARTICELLA = ? ");
      }
      
      // Se l'utente ha valorizzato il subalterno
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSubalterno()))
      {
        queryBuf.append(
        "AND UPPER(SP.SUBALTERNO) = UPPER(?) ");
      }
      
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo()))
      {
        queryBuf.append(
        "AND  SP.ID_STORICO_PARTICELLA = DS.ID_STORICO_PARTICELLA " +
        "AND  DS.ID_DICHIARAZIONE_CONSISTENZA = DC.ID_DICHIARAZIONE_CONSISTENZA " +
        "AND  DS.ID_CONTROLLO = ? ");
      }  

      queryBuf.append(
        "ORDER BY C.DESCOM, " +
        "         SP.SEZIONE, " +
        "         SP.FOGLIO, " +
        "         SP.PARTICELLA, " +
        "         SP.SUBALTERNO, " +
        "         CD.ID_TITOLO_POSSESSO, " +
        "         CD.ID_CONDUZIONE_DICHIARATA, " +
        "         TU.DESCRIZIONE, " +
        "         TV.DESCRIZIONE ");

      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[ParticellaGaaDAO::getElencoParticelleForPopNotifica] Query="
            + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, filtriParticellareRicercaVO.getIdPianoRiferimento().longValue());
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdUtilizzo())
          && (filtriParticellareRicercaVO.getIdUtilizzo().intValue() !=0)
          && (filtriParticellareRicercaVO.getIdUtilizzo().intValue() !=-1))
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
      }
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIstatComune()))
      {
        stmt.setString(++indice, filtriParticellareRicercaVO.getIstatComune());
      }      
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSezione()))
      {
        stmt.setString(++indice, filtriParticellareRicercaVO.getSezione());
      }      
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getFoglio()))
      {
        stmt.setString(++indice, filtriParticellareRicercaVO.getFoglio());
      }      
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getParticella()))
      {
        stmt.setString(++indice, filtriParticellareRicercaVO.getParticella());
      }      
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSubalterno()))
      {
        stmt.setString(++indice, filtriParticellareRicercaVO.getSubalterno());
      }
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo()))
      {
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdControllo().longValue());
      }
     

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
          idConduzioneTmp = 0;
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
          if(idConduzioneTmp !=0)
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
          tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTILIZZO"));
          utilizzoDichiaratoVO.setTipoUtilizzoVO(tipoUtilizzoVO);
          TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
          tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VAR"));
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
      { new Parametro("filtriParticellareRicercaVO", filtriParticellareRicercaVO) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[ParticellaGaaDAO::getElencoParticelleForPopNotifica] ", t,
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
      SolmrLogger.debug(this, "[ParticellaGaaDAO::getElencoParticelleForPopNotifica] END.");
    }
  }
  
  
  
  public StoricoParticellaVO findStoricoParticellaDichCompleto(long idStoricoParticella, long idDichiarazioneConsistenza) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    StoricoParticellaVO storicoParticellaVO = null;
    Vector<ConduzioneDichiarataVO>  vConduzioni = null;
    ConduzioneDichiarataVO conduzioneDichiarataVO = null;
    Vector<UtilizzoDichiaratoVO>  vUtilizzi = null;
    UtilizzoDichiaratoVO utilizzoDichiaratoVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[ParticellaGaaDAO::findStoricoParticellaDichCompleto] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf = new StringBuffer(
        "SELECT SP.ID_STORICO_PARTICELLA, " +
        "       SP.COMUNE, " +
        "       C.DESCOM, " +
        "       P.SIGLA_PROVINCIA, " +
        "       SP.SEZIONE, " +
        "       SP.FOGLIO, " +
        "       SP.PARTICELLA, " +
        "       SP.SUBALTERNO, " +
        "       SP.ID_PARTICELLA," +
        "       CD.ID_CONDUZIONE_DICHIARATA, " +
        "       CD.ID_TITOLO_POSSESSO, " +
        "       CD.PERCENTUALE_POSSESSO, " +
        "       UD.ID_UTILIZZO_DICHIARATO, " +
        "       UD.SUPERFICIE_UTILIZZATA, " +
        "       TU.CODICE AS COD_UTILIZZO, " +
        "       TU.DESCRIZIONE AS DESC_TIPO_UTILIZZO, " +
        "       TV.DESCRIZIONE AS DESC_VARIETA, " +
        "       TV.CODICE_VARIETA AS COD_VAR " +
        "FROM   DB_STORICO_PARTICELLA SP, " + 
        "       COMUNE C, " +
        "       PROVINCIA P, " +
        "       DB_TIPO_VARIETA TV, " +
        "       DB_TIPO_UTILIZZO TU, " +
        "       DB_CONDUZIONE_DICHIARATA CD, " +
        "       DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "       DB_UTILIZZO_DICHIARATO UD " +
        "WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "AND    SP.ID_STORICO_PARTICELLA = ? " +
        "AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        "AND    CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
        "AND    SP.COMUNE = C.ISTAT_COMUNE " +
        "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
        "AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA(+) " +
        "AND    UD.ID_UTILIZZO = TU.ID_UTILIZZO (+) " +
        "AND    UD.ID_VARIETA  = TV.ID_VARIETA (+) " +
        "ORDER BY C.DESCOM, " +
        "         SP.SEZIONE, " +
        "         SP.FOGLIO, " +
        "         SP.PARTICELLA, " +
        "         SP.SUBALTERNO, " +
        "         CD.ID_TITOLO_POSSESSO, " +
        "         CD.ID_CONDUZIONE_DICHIARATA, " +
        "         TU.DESCRIZIONE, " +
        "         TV.DESCRIZIONE ");

      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[ParticellaGaaDAO::findStoricoParticellaDichCompleto] Query="
            + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idDichiarazioneConsistenza);
      stmt.setLong(++indice, idStoricoParticella);
      
     

      ResultSet rs = stmt.executeQuery();
      long idConduzioneTmp = 0;
      Long idUtilizzo = null;
      int i=0;
      while (rs.next())
      {
        if(storicoParticellaVO == null)
        {
          storicoParticellaVO = new StoricoParticellaVO();
        }
        long idParticella = rs.getLong("ID_PARTICELLA");
        long idConduzione = rs.getLong("ID_CONDUZIONE_DICHIARATA");
        idUtilizzo = checkLongNull(rs.getString("ID_UTILIZZO_DICHIARATO"));
        if(i==0)
        {
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
          tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTILIZZO"));
          utilizzoDichiaratoVO.setTipoUtilizzoVO(tipoUtilizzoVO);
          TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
          tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VAR"));
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
       
        idConduzioneTmp = idConduzione;
        i++;
      }
      
      //Almeno un record
      if(i !=0)
      {
        storicoParticellaVO.setvConduzioniDichiarate(vConduzioni);
      }
      
      
      return storicoParticellaVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("storicoParticellaVO", storicoParticellaVO),
          new Variabile("vConduzioni", vConduzioni), new Variabile("conduzioneDichiarataVO", conduzioneDichiarataVO),
          new Variabile("vConduzioni", vUtilizzi), new Variabile("utilizzoDichiaratoVO", utilizzoDichiaratoVO)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idStoricoParticella", idStoricoParticella),
        new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[ParticellaGaaDAO::findStoricoParticellaDichCompleto] ", t,
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
      SolmrLogger.debug(this, "[ParticellaGaaDAO::findStoricoParticellaDichCompleto] END.");
    }
  }
  
  public Vector<TipoEfaVO> getListTipoEfa() 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoEfaVO> vTipoEfa = null;
    TipoEfaVO tipoEfaVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::getListTipoEfa] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT TE.ID_TIPO_EFA," +
        "       TE.CODICE_TIPO_EFA, " +
        "       TE.DESCRIZIONE_TIPO_EFA, " +
        "       TE.DESCRIZIONE_ESTESA_TIPO_EFA, " +
        "       TE.ID_UNITA_MISURA, " +
        "       TE.FATTORE_DI_CONVERSIONE, " +
        "       TE.FATTORE_DI_PONDERAZIONE, " +
        "       TE.ID_TIPO_EFA_PADRE, " +
        "       UM.DESCRIZIONE AS DESC_UNITA_MISURA " +
        "FROM   DB_TIPO_EFA TE, " +
        "       DB_UNITA_MISURA UM " +
        "WHERE  TE.DATA_FINE_VALIDITA IS NULL " +
        "AND    TE.DICHIARABILE = 'S' " +
        "AND    TE.ID_UNITA_MISURA = UM.ID_UNITA_MISURA (+) " +
        "ORDER BY ORDINAMENTO ASC ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getListTipoEfa] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {        
        if(vTipoEfa == null)
        {
          vTipoEfa = new Vector<TipoEfaVO>();
        }
        
        tipoEfaVO = new TipoEfaVO();
        tipoEfaVO.setIdTipoEfa(rs.getLong("ID_TIPO_EFA"));
        tipoEfaVO.setCodiceTipoEfa(rs.getString("CODICE_TIPO_EFA"));
        tipoEfaVO.setDescrizioneTipoEfa(rs.getString("DESCRIZIONE_TIPO_EFA"));
        tipoEfaVO.setDescrizioneEstesaTipoEfa(rs.getString("DESCRIZIONE_ESTESA_TIPO_EFA"));
        tipoEfaVO.setIdUnitaMisura(checkIntegerNull(rs.getString("ID_UNITA_MISURA")));
        tipoEfaVO.setFattoreDiConversione(rs.getBigDecimal("FATTORE_DI_CONVERSIONE"));
        tipoEfaVO.setFattoreDiPonderazione(rs.getBigDecimal("FATTORE_DI_PONDERAZIONE"));
        tipoEfaVO.setIdTipoEfaPadre(checkLongNull(rs.getString("ID_TIPO_EFA_PADRE")));
        tipoEfaVO.setDescUnitaMisura(rs.getString("DESC_UNITA_MISURA"));
        
        vTipoEfa.add(tipoEfaVO);
        
      }
      
      return vTipoEfa;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("vTipoEfa", vTipoEfa), new Variabile("tipoEfaVO", tipoEfaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getListTipoEfa] ", t,
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
          "[ParticellaGaaDAO::getListTipoEfa] END.");
    }
  }
  
  
  public Vector<TipoEfaVO> getListLegendaTipoEfa() 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoEfaVO> vTipoEfa = null;
    TipoEfaVO tipoEfaVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::getListLegendaTipoEfa] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT TE.ID_TIPO_EFA, " +
        "       TE.CODICE_TIPO_EFA, " + 
        "       TE.DESCRIZIONE_TIPO_EFA, " + 
        "       TE.DESCRIZIONE_ESTESA_TIPO_EFA, " + 
        "       TE.ID_UNITA_MISURA, " +
        "       TE.FATTORE_DI_CONVERSIONE, " + 
        "       TE.FATTORE_DI_PONDERAZIONE, " +
        "       TE.ID_TIPO_EFA_PADRE, " +
        "       TE.ORDINAMENTO, " +
        "       UM.DESCRIZIONE AS DESC_UNITA_MISURA, " +
        "       'N' AS PADRE " + 
        "FROM   DB_TIPO_EFA TE, " +
        "       DB_UNITA_MISURA UM " +
        "WHERE  TE.DATA_FINE_VALIDITA IS NULL " + 
        "AND    TE.DICHIARABILE = 'S' " +
        "AND    TE.ID_UNITA_MISURA = UM.ID_UNITA_MISURA (+) " +
        "UNION " +
        "SELECT DISTINCT " +
        "       TE.ID_TIPO_EFA, " +
        "       TE.CODICE_TIPO_EFA, " +
        "       TE.DESCRIZIONE_TIPO_EFA, " + 
        "       TE.DESCRIZIONE_ESTESA_TIPO_EFA, " + 
        "       TE.ID_UNITA_MISURA, " +
        "       TE.FATTORE_DI_CONVERSIONE, " + 
        "       TE.FATTORE_DI_PONDERAZIONE, " +
        "       TE.ID_TIPO_EFA_PADRE, " +
        "       TE.ORDINAMENTO, " +
        "       NULL AS DESC_UNITA_MISURA, " +
        "       'S' AS PADRE " +
        "FROM   DB_TIPO_EFA TE " +
        "WHERE  TE.DATA_FINE_VALIDITA IS NULL " +
        "AND    TE.ID_TIPO_EFA IN (SELECT  TE1.ID_TIPO_EFA_PADRE " +
        "                          FROM   DB_TIPO_EFA TE1 " +
        "                          WHERE  TE1.DATA_FINE_VALIDITA IS NULL " +
        "                          AND    TE1.DICHIARABILE = 'S' ) " +
        "ORDER BY ORDINAMENTO ASC ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getListLegendaTipoEfa] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {        
        if(vTipoEfa == null)
        {
          vTipoEfa = new Vector<TipoEfaVO>();
        }
        
        tipoEfaVO = new TipoEfaVO();
        tipoEfaVO.setIdTipoEfa(rs.getLong("ID_TIPO_EFA"));
        tipoEfaVO.setCodiceTipoEfa(rs.getString("CODICE_TIPO_EFA"));
        tipoEfaVO.setDescrizioneTipoEfa(rs.getString("DESCRIZIONE_TIPO_EFA"));
        tipoEfaVO.setDescrizioneEstesaTipoEfa(rs.getString("DESCRIZIONE_ESTESA_TIPO_EFA"));
        tipoEfaVO.setIdUnitaMisura(checkIntegerNull(rs.getString("ID_UNITA_MISURA")));
        tipoEfaVO.setFattoreDiConversione(rs.getBigDecimal("FATTORE_DI_CONVERSIONE"));
        tipoEfaVO.setFattoreDiPonderazione(rs.getBigDecimal("FATTORE_DI_PONDERAZIONE"));
        tipoEfaVO.setIdTipoEfaPadre(checkLongNull(rs.getString("ID_TIPO_EFA_PADRE")));
        tipoEfaVO.setDescUnitaMisura(rs.getString("DESC_UNITA_MISURA"));
        String padre = rs.getString("PADRE");
        if("S".equalsIgnoreCase(padre))
          tipoEfaVO.setPadre(true);
        
        vTipoEfa.add(tipoEfaVO);
        
      }
      
      return vTipoEfa;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("vTipoEfa", vTipoEfa), new Variabile("tipoEfaVO", tipoEfaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getListLegendaTipoEfa] ", t,
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
          "[ParticellaGaaDAO::getListLegendaTipoEfa] END.");
    }
  }
  
  
  
  
  public TipoEfaVO getTipoEfaFromIdCatalogoMatrice(long idCatalogoMatrice) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    TipoEfaVO tipoEfaVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::getTipoEfaFromIdCatalogoMatrice] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT TE.ID_TIPO_EFA," +
        "       TE.CODICE_TIPO_EFA, " +
        "       TE.DESCRIZIONE_TIPO_EFA, " +
        "       TE.DESCRIZIONE_ESTESA_TIPO_EFA, " +
        "       TE.ID_UNITA_MISURA, " +
        "       TE.FATTORE_DI_CONVERSIONE, " +
        "       TE.FATTORE_DI_PONDERAZIONE, " +
        "       TE.ID_TIPO_EFA_PADRE, " +
        "       UM.DESCRIZIONE AS DESC_UNITA_MISURA " +
        "FROM   DB_TIPO_EFA TE, " +
        "       DB_UNITA_MISURA UM, " +
        "       DB_TIPO_EFA_TIPO_VARIETA TETV " +
        "WHERE  TETV.ID_CATALOGO_MATRICE = ?  " +
        "AND    TETV.ID_TIPO_EFA = TE.ID_TIPO_EFA " +
        "AND    TE.ID_UNITA_MISURA = UM.ID_UNITA_MISURA (+) " +
        "AND    TE.DATA_FINE_VALIDITA IS NULL " +
        "AND    TETV.DATA_FINE_VALIDITA IS NULL ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getTipoEfaFromIdCatalogoMatrice] Query="
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
        tipoEfaVO = new TipoEfaVO();
        tipoEfaVO.setIdTipoEfa(rs.getLong("ID_TIPO_EFA"));
        tipoEfaVO.setCodiceTipoEfa(rs.getString("CODICE_TIPO_EFA"));
        tipoEfaVO.setDescrizioneTipoEfa(rs.getString("DESCRIZIONE_TIPO_EFA"));
        tipoEfaVO.setDescrizioneEstesaTipoEfa(rs.getString("DESCRIZIONE_ESTESA_TIPO_EFA"));
        tipoEfaVO.setIdUnitaMisura(checkIntegerNull(rs.getString("ID_UNITA_MISURA")));
        tipoEfaVO.setFattoreDiConversione(rs.getBigDecimal("FATTORE_DI_CONVERSIONE"));
        tipoEfaVO.setFattoreDiPonderazione(rs.getBigDecimal("FATTORE_DI_PONDERAZIONE"));
        tipoEfaVO.setIdTipoEfaPadre(checkLongNull(rs.getString("ID_TIPO_EFA_PADRE")));
        tipoEfaVO.setDescUnitaMisura(rs.getString("DESC_UNITA_MISURA"));        
      }
      
      return tipoEfaVO;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("tipoEfaVO", tipoEfaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idCatalogoMatrice", idCatalogoMatrice)};

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getTipoEfaFromIdCatalogoMatrice] ", t,
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
          "[ParticellaGaaDAO::getTipoEfaFromIdCatalogoMatrice] END.");
    }
  }
  
  public Vector<TipoPeriodoSeminaVO> getListTipoPeriodoSemina() 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoPeriodoSeminaVO> vTipoPeriodoSemina = null;
    TipoPeriodoSeminaVO tipoPeriodoSeminaVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::getListTipoPeriodoSemina] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT TPS.ID_TIPO_PERIODO_SEMINA, " +
        "       TPS.CODICE, " + 
        "       TPS.DESCRIZIONE " + 
        "FROM   DB_TIPO_PERIODO_SEMINA TPS " +
        "WHERE  TPS.DATA_FINE_VALIDITA IS NULL " + 
        "ORDER BY TPS.DESCRIZIONE ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getListTipoPeriodoSemina] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {        
        if(vTipoPeriodoSemina == null)
        {
          vTipoPeriodoSemina = new Vector<TipoPeriodoSeminaVO>();
        }
        
        tipoPeriodoSeminaVO = new TipoPeriodoSeminaVO();
        tipoPeriodoSeminaVO.setIdTipoPeriodoSemina(rs.getLong("ID_TIPO_PERIODO_SEMINA"));
        tipoPeriodoSeminaVO.setCodice(rs.getString("CODICE"));
        tipoPeriodoSeminaVO.setDescrizione(rs.getString("DESCRIZIONE"));
       
        
        vTipoPeriodoSemina.add(tipoPeriodoSeminaVO);
        
      }
      
      return vTipoPeriodoSemina;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("vTipoPeriodoSemina", vTipoPeriodoSemina), 
          new Variabile("tipoPeriodoSeminaVO", tipoPeriodoSeminaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getListTipoPeriodoSemina] ", t,
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
          "[ParticellaGaaDAO::getListTipoPeriodoSemina] END.");
    }
  }
  
  
  public Vector<TipoUtilizzoVO> getListTipoUtilizzoEfa(long idTipoEfa) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoUtilizzoVO> vTipoUtilizzo = null;
    TipoUtilizzoVO tipoUtilizzoVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::getListTipoUtilizzoEfa] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        " SELECT DISTINCT TU.ID_UTILIZZO, " +
        "        TU.ID_INDIRIZZO_UTILIZZO AS IND_UTILIZZO, " +
        "        TIU.DESCRIZIONE AS DESC_IND_UTI, " +
        "        TU.CODICE, " +
        "        TU.DESCRIZIONE AS DESC_TIPO_UTI, " +
        "        TU.TIPO, " +
        "        TU.FLAG_SAU, " +
        "        TU.FLAG_ARBOREO, " +
        "        TU.ANNO_INIZIO_VALIDITA, " +
        "        TU.ANNO_FINE_VALIDITA, " +
        "        TU.FLAG_SERRA, " +
        "        TU.FLAG_PASCOLO, " +
        "        TU.FLAG_ALIMENTAZIONE_ANIMALE, " +
        "        TU.FLAG_FORAGGERA, " +
        "        TU.FLAG_UBA_SOSTENIBILE, " +
        "        TU.FLAG_COLTURA_SECONDARIA, " +
        "        TU.FLAG_NIDI, " +
        "        TU.FLAG_PRATO_PASCOLO, " +  
        "        TU.CODICE_PSR, " +
        "        TU.FLAG_FRUTTA_GUSCIO, " +
        "        TU.FLAG_TIPO_AVVICENDAMENTO, " +
        "        TU.FLAG_PRINCIPALE " +
        " FROM   DB_TIPO_EFA_TIPO_VARIETA TETV, " +
        "        DB_R_CATALOGO_MATRICE RCM, " +
        "        DB_TIPO_UTILIZZO TU, " +
        "        DB_TIPO_INDIRIZZO_UTILIZZO TIU " +
        " WHERE  TETV.ID_TIPO_EFA = ? " +
        " AND    TETV.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +
        " AND    TETV.DATA_FINE_VALIDITA IS NULL " +
        " AND    RCM.ID_UTILIZZO = TU.ID_UTILIZZO " +
        " AND    RCM.DATA_FINE_VALIDITA IS NULL " +
        " AND    TU.ID_INDIRIZZO_UTILIZZO = TIU.ID_INDIRIZZO_UTILIZZO " +
        " AND    TU.DATA_FINE_VALIDITA IS NULL " +
        " ORDER BY TU.DESCRIZIONE ASC ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getListTipoUtilizzoEfa] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      stmt.setLong(1, idTipoEfa);
      
      ResultSet rs = stmt.executeQuery();
      while(rs.next()) 
      {
        if(vTipoUtilizzo == null)
        {
          vTipoUtilizzo = new Vector<TipoUtilizzoVO>();
        }
        
        tipoUtilizzoVO = new TipoUtilizzoVO();
        tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
        CodeDescription code = new CodeDescription(Integer.decode(rs.getString("IND_UTILIZZO")), rs.getString("DESC_IND_UTI"));
        tipoUtilizzoVO.setIndirizzoUtilizzo(code);
        tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
        tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTI"));
        tipoUtilizzoVO.setTipo(rs.getString("TIPO"));
        tipoUtilizzoVO.setFlagSau(rs.getString("FLAG_SAU"));
        tipoUtilizzoVO.setFlagArboreo(rs.getString("FLAG_ARBOREO"));
        tipoUtilizzoVO.setAnnoInizioValidita(rs.getString("ANNO_INIZIO_VALIDITA"));
        tipoUtilizzoVO.setAnnoFineValidita(rs.getString("ANNO_FINE_VALIDITA"));
        tipoUtilizzoVO.setFlagSerra(rs.getString("FLAG_SERRA"));
        tipoUtilizzoVO.setFlagPascolo(rs.getString("FLAG_PASCOLO"));
        tipoUtilizzoVO.setFlagAlimentazioneAnimale(rs.getString("FLAG_ALIMENTAZIONE_ANIMALE"));
        tipoUtilizzoVO.setFlagForaggera(rs.getString("FLAG_FORAGGERA"));
        tipoUtilizzoVO.setFlagUbaSostenibile(rs.getString("FLAG_UBA_SOSTENIBILE"));
        tipoUtilizzoVO.setFlagColturaSecondaria(rs.getString("FLAG_COLTURA_SECONDARIA"));
        tipoUtilizzoVO.setFlagNidi(rs.getString("FLAG_NIDI"));
        tipoUtilizzoVO.setFlagPratoPascolo(rs.getString("FLAG_PRATO_PASCOLO"));
        tipoUtilizzoVO.setCodicePsr(rs.getString("CODICE_PSR"));
        tipoUtilizzoVO.setFlagFruttaGuscio(rs.getString("FLAG_FRUTTA_GUSCIO"));
        tipoUtilizzoVO.setFlagTipoAvvicendamento(rs.getString("FLAG_TIPO_AVVICENDAMENTO"));
        tipoUtilizzoVO.setFlagPrincipale(rs.getString("FLAG_PRINCIPALE"));
        
        
        vTipoUtilizzo.add(tipoUtilizzoVO);
      }
      
      return vTipoUtilizzo;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("vTipoUtilizzo", vTipoUtilizzo), 
          new Variabile("tipoUtilizzoVO", tipoUtilizzoVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idTipoEfa", idTipoEfa)};

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getListTipoUtilizzoEfa] ", t,
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
          "[ParticellaGaaDAO::getListTipoUtilizzoEfa] END.");
    }
  }
  
  
  
  public Vector<TipoDestinazioneVO> getListTipoDestinazioneEfa(long idTipoEfa, long idUtilizzo) 
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
          "[ParticellaGaaDAO::getListTipoDestinazioneEfa] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        " SELECT DISTINCT " +
        "       TD.ID_TIPO_DESTINAZIONE," +
        "       TD.DESCRIZIONE_DESTINAZIONE, " +
        "       TD.CODICE_DESTINAZIONE, " +
        "       TD.ORDINAMENTO " +  
        "FROM   DB_TIPO_EFA_TIPO_VARIETA TETV, " +
        "       DB_R_CATALOGO_MATRICE RCM, " +
        "       DB_TIPO_DESTINAZIONE TD " +
        "WHERE  TETV.ID_TIPO_EFA = ? " +
        "AND    RCM.DATA_FINE_VALIDITA IS NULL " +
        "AND    RCM.ID_UTILIZZO = ? " +
        "AND    RCM.ID_TIPO_DESTINAZIONE = TD.ID_TIPO_DESTINAZIONE " +
        "AND    TETV.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +
        "AND    TETV.DATA_FINE_VALIDITA IS NULL " +
        "ORDER BY TD.ORDINAMENTO, TD.DESCRIZIONE_DESTINAZIONE ASC ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getListTipoDestinazioneEfa] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int idx = 0;
      
      stmt.setLong(++idx, idTipoEfa);
      stmt.setLong(++idx, idUtilizzo);
      
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
          new Variabile("vTipoDestinazioneVO", vTipoDestinazioneVO), 
          new Variabile("tipoDestinazioneVO", tipoDestinazioneVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUtilizzo", idUtilizzo),
        new Parametro("idTipoEfa", idTipoEfa)};

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getListTipoDestinazioneEfa] ", t,
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
          "[ParticellaGaaDAO::getListTipoDestinazioneEfa] END.");
    }
  }
  
  
  public Vector<TipoVarietaVO> getListTipoVarietaEfaByMatrice(long idTipoEfa, 
      long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso, long idTipoQualitaUso) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoVarietaVO> vTipoVarieta = null;
    TipoVarietaVO tipoVarietaVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::getListTipoVarietaEfaByMatrice] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT DISTINCT " +
        "       TV.ID_VARIETA, " +
        "       TV.ID_UTILIZZO, " +
        "       TV.CODICE_VARIETA, " +
        "       TV.DESCRIZIONE, " +
        "       TV.ANNO_INIZIO_VALIDITA, " +
        "       TV.ANNO_FINE_VALIDITA, " +
        "       TV.RICHIESTA_RISERVA, " +
        "       TV.PERMANENTE, " +
        "       TV.FLAG_SIAP, " +
        "       TV.FLAG_FORAGGERA_PERMANENTE, " + 
        "       TV.FLAG_AVVICENDAMENTO, " +
        "       TV.ID_TIPO_PERIODO_SEMINA " +
        "FROM   DB_R_CATALOGO_MATRICE RCM, " +
        "       DB_TIPO_VARIETA TV, " +
        "       DB_TIPO_EFA_TIPO_VARIETA TETV " +
        "WHERE  TETV.ID_TIPO_EFA = ? " +
        "AND    RCM.ID_UTILIZZO = ? " +
        "AND    RCM.ID_TIPO_DESTINAZIONE = ? " +
        "AND    RCM.ID_TIPO_DETTAGLIO_USO = ? " +
        "AND    RCM.ID_TIPO_QUALITA_USO = ? " +
        "AND    RCM.DATA_FINE_VALIDITA IS NULL " +
        "AND    RCM.ID_CATALOGO_MATRICE = TETV.ID_CATALOGO_MATRICE " +
        "AND    TETV.DATA_FINE_VALIDITA IS NULL " +
        "AND    TV.DATA_FINE_VALIDITA IS NULL " +
        "AND    RCM.ID_VARIETA = TV.ID_VARIETA " +
        
        "ORDER BY TV.DESCRIZIONE ASC ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getListTipoVarietaEfaByMatrice] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int idx = 0;
      
      stmt.setLong(++idx, idTipoEfa);
      stmt.setLong(++idx, idUtilizzo);
      stmt.setLong(++idx, idTipoDestinazione);
      stmt.setLong(++idx, idTipoDettaglioUso);
      stmt.setLong(++idx, idTipoQualitaUso);
      
      
      ResultSet rs = stmt.executeQuery();
      while(rs.next()) 
      {
        if(vTipoVarieta == null)
        {
          vTipoVarieta = new Vector<TipoVarietaVO>();
        }
        
        tipoVarietaVO = new TipoVarietaVO();
        tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
        tipoVarietaVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
        tipoVarietaVO.setCodiceVarieta(rs.getString("CODICE_VARIETA"));
        tipoVarietaVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoVarietaVO.setAnnoInizioValidita(rs.getString("ANNO_INIZIO_VALIDITA"));
        tipoVarietaVO.setAnnoFineValidita(rs.getString("ANNO_FINE_VALIDITA"));
        tipoVarietaVO.setRichiestaRiserva(rs.getString("RICHIESTA_RISERVA"));
        tipoVarietaVO.setPermanente(rs.getString("PERMANENTE"));
        tipoVarietaVO.setFlagSiap(rs.getString("FLAG_SIAP"));
        tipoVarietaVO.setFlagForaggeraPermanente(rs.getString("FLAG_FORAGGERA_PERMANENTE"));
        tipoVarietaVO.setFlagAvvicendamento(rs.getString("FLAG_AVVICENDAMENTO"));
        tipoVarietaVO.setIdTipoPeriodoSemina(checkLongNull(rs.getString("ID_TIPO_PERIODO_SEMINA")));
        /*Integer abbattimentoPonderazione = checkIntegerNull(rs.getString("ABBATTIMENTO_PONDERAZIONE"));
        if(Validator.isNotEmpty(abbattimentoPonderazione))
          tipoVarietaVO.setAbbattimentoPonderazione(abbattimentoPonderazione);
        else
          tipoVarietaVO.setAbbattimentoPonderazione(new Integer(1));*/
        
        
        vTipoVarieta.add(tipoVarietaVO);
      }
      
      return vTipoVarieta;
      
    }
    catch (Exception t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("vTipoVarieta", vTipoVarieta), 
          new Variabile("tipoVarietaVO", tipoVarietaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idTipoEfa", idTipoEfa),
        new Parametro("idUtilizzo", idUtilizzo),
        new Parametro("idTipoDestinazione", idTipoDestinazione),
        new Parametro("idTipoDettaglioUso", idTipoDettaglioUso),
        new Parametro("idTipoQualitaUso", idTipoQualitaUso) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getListTipoVarietaEfaByMatrice] ", t,
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
          "[ParticellaGaaDAO::getListTipoVarietaEfaByMatrice] END.");
    }
  }
  
  public Vector<TipoDettaglioUsoVO> getListDettaglioUsoEfaByMatrice(long idTipoEfa,
      long idUtilizzo, long idTipoDestinazione)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoDettaglioUsoVO> vTipoDettaglioUso = null;
    TipoDettaglioUsoVO tipoDettaglioUsoVO = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::getListDettaglioUsoEfaByMatrice] BEGIN.");

      query = " " +
          "SELECT DISTINCT " +
          "       TDU.ID_TIPO_DETTAGLIO_USO, " +
          "       TDU.CODICE_DETTAGLIO_USO, " +
          "       TDU.DESCRIZIONE_DETTAGLIO_USO, " +
          "       TDU.ORDINAMENTO " +
          "FROM   DB_TIPO_DETTAGLIO_USO TDU, " +
          "       DB_R_CATALOGO_MATRICE RCM, " +
          "       DB_TIPO_EFA_TIPO_VARIETA TETV " +
          "WHERE  TETV.ID_TIPO_EFA = ? " +
          "AND    TETV.DATA_FINE_VALIDITA IS NULL " +
          "AND    RCM.ID_UTILIZZO = ? " +
          "AND    RCM.ID_TIPO_DESTINAZIONE = ? " +
          "AND    TETV.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +
          "AND    RCM.DATA_FINE_VALIDITA IS NULL " +
          "AND    RCM.ID_TIPO_DETTAGLIO_USO = TDU.ID_TIPO_DETTAGLIO_USO " +
          "AND    TDU.DATA_FINE_VALIDITA IS NULL " +
          "ORDER BY TDU.ORDINAMENTO, TDU.DESCRIZIONE_DETTAGLIO_USO ";
      
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      int idx = 0;
      stmt.setLong(++idx, idTipoEfa);
      stmt.setLong(++idx, idUtilizzo);
      stmt.setLong(++idx, idTipoDestinazione);
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      while (rs.next())
      {
        if(vTipoDettaglioUso == null)
        {
          vTipoDettaglioUso = new Vector<TipoDettaglioUsoVO>();
        }

        tipoDettaglioUsoVO = new TipoDettaglioUsoVO();
        
        tipoDettaglioUsoVO.setIdTipoDettaglioUso(rs.getLong("ID_TIPO_DETTAGLIO_USO"));
        tipoDettaglioUsoVO.setCodiceDettaglioUso(rs.getString("CODICE_DETTAGLIO_USO"));
        tipoDettaglioUsoVO.setDescrizione(rs.getString("DESCRIZIONE_DETTAGLIO_USO"));
        
        vTipoDettaglioUso.add(tipoDettaglioUsoVO);
      }
      
      return vTipoDettaglioUso;
    }
    catch (Exception t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("tipoDettaglioUsoVO", tipoDettaglioUsoVO),
          new Variabile("vTipoDettaglioUso", vTipoDettaglioUso) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idTipoEfa", idTipoEfa),
          new Parametro("idUtilizzo", idUtilizzo),
          new Parametro("idTipoDestinazione", idTipoDestinazione) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getListDettaglioUsoEfaByMatrice] ", t,
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
          "[ParticellaGaaDAO::getListDettaglioUsoEfaByMatrice] END.");
    }
  }
  
  
  public Vector<TipoQualitaUsoVO> getListQualitaUsoEfaByMatrice(long idTipoEfa,
      long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoQualitaUsoVO> vTipoQualitaUso = null;
    TipoQualitaUsoVO tipoQualitaUsoVO = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::getListQualitaUsoEfaByMatrice] BEGIN.");

      query = " " +
          "SELECT DISTINCT " +
          "       TQU.ID_TIPO_QUALITA_USO, " +
          "       TQU.CODICE_QUALITA_USO, " +
          "       TQU.DESCRIZIONE_QUALITA_USO, " +
          "       TQU.ORDINAMENTO " +
          "FROM   DB_TIPO_QUALITA_USO TQU, " +
          "       DB_R_CATALOGO_MATRICE RCM, " +
          "       DB_TIPO_EFA_TIPO_VARIETA TETV " +
          "WHERE  TETV.ID_TIPO_EFA = ? " +
          "AND    TETV.DATA_FINE_VALIDITA IS NULL " +
          "AND    RCM.ID_UTILIZZO = ? " +
          "AND    RCM.ID_TIPO_DESTINAZIONE = ? " +
          "AND    RCM.ID_TIPO_DETTAGLIO_USO = ? " +
          "AND    TETV.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +
          "AND    RCM.DATA_FINE_VALIDITA IS NULL " +
          "AND    RCM.ID_TIPO_QUALITA_USO = TQU.ID_TIPO_QUALITA_USO " +
          "AND    TQU.DATA_FINE_VALIDITA IS NULL " +
          "ORDER BY TQU.ORDINAMENTO, TQU.DESCRIZIONE_QUALITA_USO ";
      
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      int idx = 0;
      stmt.setLong(++idx, idTipoEfa);
      stmt.setLong(++idx, idUtilizzo);
      stmt.setLong(++idx, idTipoDestinazione);
      stmt.setLong(++idx, idTipoDettaglioUso);
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      while (rs.next())
      {
        if(vTipoQualitaUso == null)
        {
          vTipoQualitaUso = new Vector<TipoQualitaUsoVO>();
        }

        tipoQualitaUsoVO = new TipoQualitaUsoVO();
        
        tipoQualitaUsoVO.setIdTipoQualitaUso(rs.getLong("ID_TIPO_QUALITA_USO"));
        tipoQualitaUsoVO.setCodiceQualitaUso(rs.getString("CODICE_QUALITA_USO"));
        tipoQualitaUsoVO.setDescrizioneQualitaUso(rs.getString("DESCRIZIONE_QUALITA_USO"));
        
        vTipoQualitaUso.add(tipoQualitaUsoVO);
      }
      
      return vTipoQualitaUso;
    }
    catch (Exception t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("tipoQualitaUsoVO", tipoQualitaUsoVO),
          new Variabile("vTipoQualitaUso", vTipoQualitaUso) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idTipoEfa", idTipoEfa),
          new Parametro("idUtilizzo", idUtilizzo),
          new Parametro("idTipoDestinazione", idTipoDestinazione),
          new Parametro("idTipoDettaglioUso", idTipoDettaglioUso)};

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getListQualitaUsoEfaByMatrice] ", t,
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
          "[ParticellaGaaDAO::getListQualitaUsoEfaByMatrice] END.");
    }
  }
  
  
  public boolean isDettaglioUsoObbligatorio(long idTipoEfa,
      long idVarieta)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    boolean flagObbligatorio = false;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::isDettaglioUsoObbligatorio] BEGIN.");

      query = " " +
          "SELECT TETV.ID_TIPO_DETTAGLIO_USO, " +
          "       TETV.ID_VARIETA " +
          "FROM   DB_TIPO_EFA_TIPO_VARIETA TETV " +
          "WHERE  TETV.ID_TIPO_EFA = ? " +
          "AND    TETV.ID_VARIETA = ? " +
          "AND    TETV.DATA_FINE_VALIDITA IS NULL ";
      
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      stmt.setLong(1, idTipoEfa);
      stmt.setLong(2, idVarieta);
      
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      while (rs.next())
      {
        String idTipoDettaglioUso = rs.getString("ID_TIPO_DETTAGLIO_USO"); 
        if(!flagObbligatorio && Validator.isNotEmpty(idTipoDettaglioUso))
        {
          flagObbligatorio = true;
        }
      }
      
      return flagObbligatorio;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("flagObbligatorio", flagObbligatorio) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idTipoEfa", idTipoEfa),
          new Parametro("idVarieta", idVarieta)  };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::isDettaglioUsoObbligatorio] ", t,
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
          "[ParticellaGaaDAO::isDettaglioUsoObbligatorio] END.");
    }
  }
  
  
  public TipoEfaVO getTipoEfaFromPrimaryKey(long idTipoEfa) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    TipoEfaVO tipoEfaVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::getTipoEfaFromPrimaryKey] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT TE.ID_TIPO_EFA," +
        "       TE.CODICE_TIPO_EFA, " +
        "       TE.DESCRIZIONE_TIPO_EFA, " +
        "       TE.DESCRIZIONE_ESTESA_TIPO_EFA, " +
        "       TE.ID_UNITA_MISURA, " +
        "       TE.FATTORE_DI_CONVERSIONE, " +
        "       TE.FATTORE_DI_PONDERAZIONE, " +
        "       TE.ID_TIPO_EFA_PADRE, " +
        "       UM.DESCRIZIONE AS DESC_UNITA_MISURA " +
        "FROM   DB_TIPO_EFA TE, " +
        "       DB_UNITA_MISURA UM " +
        "WHERE  TE.ID_TIPO_EFA = ?  " +
        "AND    TE.ID_UNITA_MISURA = UM.ID_UNITA_MISURA ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getTipoEfaFromPrimaryKey] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idTipoEfa);
      
      
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      {                
        tipoEfaVO = new TipoEfaVO();
        tipoEfaVO.setIdTipoEfa(rs.getLong("ID_TIPO_EFA"));
        tipoEfaVO.setCodiceTipoEfa(rs.getString("CODICE_TIPO_EFA"));
        tipoEfaVO.setDescrizioneTipoEfa(rs.getString("DESCRIZIONE_TIPO_EFA"));
        tipoEfaVO.setDescrizioneEstesaTipoEfa(rs.getString("DESCRIZIONE_ESTESA_TIPO_EFA"));
        tipoEfaVO.setIdUnitaMisura(checkIntegerNull(rs.getString("ID_UNITA_MISURA")));
        tipoEfaVO.setFattoreDiConversione(rs.getBigDecimal("FATTORE_DI_CONVERSIONE"));
        tipoEfaVO.setFattoreDiPonderazione(rs.getBigDecimal("FATTORE_DI_PONDERAZIONE"));
        tipoEfaVO.setIdTipoEfaPadre(checkLongNull(rs.getString("ID_TIPO_EFA_PADRE")));
        tipoEfaVO.setDescUnitaMisura(rs.getString("DESC_UNITA_MISURA"));        
      }
      
      return tipoEfaVO;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("tipoEfaVO", tipoEfaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idTipoEfa", idTipoEfa) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getTipoEfaFromPrimaryKey] ", t,
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
          "[ParticellaGaaDAO::getTipoEfaFromPrimaryKey] END.");
    }
  }
  
  
  
  public Integer getAbbPonderazioneByMatrice(
      long idTipoEfa, long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso,
      long idTipoQualitaUso, long idVarieta)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Integer abbPonderazione = new Integer(1);
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::getAbbPonderazioneByMatrice] BEGIN.");

      query = " " +
          "SELECT TETV.ABBATTIMENTO_PONDERAZIONE " +
          "FROM   DB_TIPO_EFA_TIPO_VARIETA TETV, " +
          "       DB_R_CATALOGO_MATRICE RCM " +
          "WHERE  TETV.ID_TIPO_EFA = ? " +
          "AND    TETV.DATA_FINE_VALIDITA IS NULL " +
          "AND    TETV.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +
          "AND    RCM.ID_UTILIZZO = ? " +
          "AND    RCM.ID_TIPO_DESTINAZIONE = ? " +
          "AND    RCM.ID_TIPO_DETTAGLIO_USO = ? " +
          "AND    RCM.ID_TIPO_QUALITA_USO = ? " +
          "AND    RCM.ID_VARIETA = ? " +
          "AND    RCM.DATA_FINE_VALIDITA IS NULL ";
      
          
      
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      int idx = 0;
      
      stmt.setLong(++idx, idTipoEfa);
      stmt.setLong(++idx, idUtilizzo);
      stmt.setLong(++idx, idTipoDestinazione);
      stmt.setLong(++idx, idTipoDettaglioUso);
      stmt.setLong(++idx, idTipoQualitaUso);
      stmt.setLong(++idx, idVarieta);
      
           
      ResultSet rs = stmt.executeQuery();
      
      
      
      if (rs.next())
      {
        abbPonderazione = new Integer(rs.getInt("ABBATTIMENTO_PONDERAZIONE"));
      }
      
      return abbPonderazione;
    }
    catch (Exception t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("abbPonderazione", abbPonderazione) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idTipoEfa", idTipoEfa), 
          new Parametro("idUtilizzo", idUtilizzo), 
          new Parametro("idTipoDestinazione", idTipoDestinazione), 
          new Parametro("idTipoDettaglioUso", idTipoDettaglioUso), 
          new Parametro("idTipoQualitaUso", idTipoQualitaUso), 
          new Parametro("idVarieta", idVarieta) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getAbbPonderazioneByMatrice] ", t,
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
          "[ParticellaGaaDAO::getAbbPonderazioneByMatrice] END.");
    }
  }
  
  
  public String getValoreAttivoTipoAreaFromParticellaAndId(
      long idParticella, long idTipoArea)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    String valore = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::getValoreAttivoTipoAreaFromParticellaAndId] BEGIN.");

      query = " " +
          "SELECT TVA.VALORE " +
          "FROM   DB_R_PARTICELLA_AREA RPA," +
          "       DB_TIPO_VALORE_AREA TVA " +
          "WHERE  RPA.ID_PARTICELLA = ? " +
          "AND    RPA.DATA_FINE_VALIDITA IS NULL " +
          "AND    RPA.ID_TIPO_VALORE_AREA = TVA.ID_TIPO_VALORE_AREA " +
          "AND    TVA.DATA_FINE_VALIDITA IS NULL " +
          "AND    TVA.ID_TIPO_AREA = ? ";
      
          
      
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      stmt.setLong(1, idParticella);
      stmt.setLong(2, idTipoArea);
      
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      if (rs.next())
      {
        valore = rs.getString("VALORE");
      }
      
      return valore;
    }
    catch (Exception t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("valore", valore) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idParticella", idParticella), 
        new Parametro("idTipoArea", idTipoArea) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getValoreAttivoTipoAreaFromParticellaAndId] ", t,
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
          "[ParticellaGaaDAO::getValoreAttivoTipoAreaFromParticellaAndId] END.");
    }
  }
  
  
  public Vector<TipoAreaVO> getValoriTipoAreaParticella(long idParticella, Date dataInserimentoValidazione)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoAreaVO> vTipoArea = null;
    TipoAreaVO tipoAreaVO = null;
    Vector<TipoValoreAreaVO> vTipoValoreArea = null;
    TipoValoreAreaVO tipoValoreAreaVO = null;
    
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::getValoriTipoAreaParticella] BEGIN.");

      query = "" +
        "WITH PARTICELLA AS " +
        "   (SELECT TVA.ID_TIPO_VALORE_AREA, " +
        "           TVA.ID_TIPO_AREA, " +
        "           TVA.DESCRIZIONE AS DESC_VALORE, " +
        "           TVA.VALORE, " +
        "           TA.DESCRIZIONE AS DESC_TIPO, " +
        "           TA.FLAG_ESCLUSIVO_FOGLIO " +
        "    FROM   DB_R_PARTICELLA_AREA RA, " +
        "           DB_TIPO_VALORE_AREA TVA, " +
        "           DB_TIPO_AREA TA " +
        "    WHERE  RA.ID_PARTICELLA = ? ";
      if(Validator.isEmpty(dataInserimentoValidazione))
      {
        query += 
        "    AND    RA.DATA_FINE_VALIDITA IS NULL " +
        "    AND    TVA.DATA_FINE_VALIDITA IS NULL "+
        "    AND    TA.DATA_FINE_VALIDITA IS NULL ";
      }
      else
      {
        query +=
        "    AND    RA.DATA_INIZIO_VALIDITA <= ?  " +
        "    AND    NVL(RA.DATA_FINE_VALIDITA,TO_DATE('31/12/9999','DD/MM/YYYY')) > ? " +
        "    AND    TVA.DATA_INIZIO_VALIDITA <= ?  " +
        "    AND    NVL(TVA.DATA_FINE_VALIDITA,TO_DATE('31/12/9999','DD/MM/YYYY')) > ? " +
        "    AND    TA.DATA_INIZIO_VALIDITA <= ?  " +
        "    AND    NVL(TA.DATA_FINE_VALIDITA,TO_DATE('31/12/9999','DD/MM/YYYY')) > ? ";
      }
      query +=
        "    AND    RA.ID_TIPO_VALORE_AREA = TVA.ID_TIPO_VALORE_AREA " +       
        "    AND    TVA.ID_TIPO_AREA = TA.ID_TIPO_AREA " +
        "    AND    TA.FLAG_ESCLUSIVO_FOGLIO = 'N'), " +
        "FOGLIO AS " +
        "  (SELECT TVA.ID_TIPO_VALORE_AREA, " +
        "          TVA.ID_TIPO_AREA, " +
        "          TVA.DESCRIZIONE AS DESC_VALORE, " +
        "          TVA.VALORE, " +
        "          TA.DESCRIZIONE AS DESC_TIPO, " +
        "          TA.FLAG_ESCLUSIVO_FOGLIO " +
        "   FROM   DB_R_FOGLIO_AREA RFA, " +
        "          DB_TIPO_VALORE_AREA TVA, " +
        "          DB_STORICO_PARTICELLA SP, " +
        "          DB_FOGLIO FF, " +
        "          DB_TIPO_AREA TA " +
        "   WHERE  SP.ID_PARTICELLA = ? ";
      if(Validator.isEmpty(dataInserimentoValidazione))
      {
        query += 
        "   AND    SP.DATA_FINE_VALIDITA IS NULL " +
        "   AND    RFA.DATA_FINE_VALIDITA IS NULL " +
        "   AND    TVA.DATA_FINE_VALIDITA IS NULL " +
        "   AND    TA.DATA_FINE_VALIDITA IS NULL ";
      }
      else
      {
        query +=
        "   AND    SP.DATA_INIZIO_VALIDITA <= ?  " +
        "   AND    NVL(SP.DATA_FINE_VALIDITA,TO_DATE('31/12/9999','DD/MM/YYYY')) > ? " +
        "   AND    RFA.DATA_INIZIO_VALIDITA <= ?  " +
        "   AND    NVL(RFA.DATA_FINE_VALIDITA,TO_DATE('31/12/9999','DD/MM/YYYY')) > ? " +
        "   AND    TVA.DATA_INIZIO_VALIDITA <= ?  " +
        "   AND    NVL(TVA.DATA_FINE_VALIDITA,TO_DATE('31/12/9999','DD/MM/YYYY')) > ? " +
        "   AND    TA.DATA_INIZIO_VALIDITA <= ?  " +
        "   AND    NVL(TA.DATA_FINE_VALIDITA,TO_DATE('31/12/9999','DD/MM/YYYY')) > ? ";
      }
      query +=
        "   AND    SP.COMUNE = FF.COMUNE " +
        "   AND    NVL(SP.SEZIONE,'-') = NVL(FF.SEZIONE,'-') " +
        "   AND    SP.FOGLIO = FF.FOGLIO " +
        "   AND    FF.ID_FOGLIO = RFA.ID_FOGLIO " +
        "   AND    RFA.ID_TIPO_VALORE_AREA = TVA.ID_TIPO_VALORE_AREA " +
        "   AND    TA.FLAG_ESCLUSIVO_FOGLIO = 'S'  " +
        "   AND    TVA.ID_TIPO_AREA = TA.ID_TIPO_AREA) " +
        "SELECT TA.ID_TIPO_AREA, " +
        "       TA.DESCRIZIONE, " +
        "       DECODE(TA.FLAG_ESCLUSIVO_FOGLIO,'S',FO.DESC_VALORE,PA.DESC_VALORE) AS DESC_VALORE, " +
        "       DECODE(TA.FLAG_ESCLUSIVO_FOGLIO,'S',FO.ID_TIPO_VALORE_AREA,PA.ID_TIPO_VALORE_AREA) AS ID_TIPO_VALORE_AREA, " +
        "       DECODE(TA.FLAG_ESCLUSIVO_FOGLIO,'S',FO.VALORE,PA.VALORE) AS VALORE, " +
        "       TA.FLAG_AREA_MODIFICABILE, " +
        "       TA.FLAG_ESCLUSIVO_FOGLIO " +
        "FROM   DB_TIPO_AREA TA, " +
        "       PARTICELLA PA, " +
        "       FOGLIO FO " +
        "WHERE  TA.ID_TIPO_AREA = PA.ID_TIPO_AREA(+) " +
        "AND    TA.ID_TIPO_AREA = FO.ID_TIPO_AREA(+) ";
        if(Validator.isEmpty(dataInserimentoValidazione))
        {
          query += 
          "    AND TA.DATA_FINE_VALIDITA IS NULL ";
        }
        else
        {
          query +=
          "    AND TA.DATA_INIZIO_VALIDITA <= ?  " +
          "    AND NVL(TA.DATA_FINE_VALIDITA,TO_DATE('31/12/9999','DD/MM/YYYY')) > ? ";
        }
        query +=
          "ORDER BY TA.DESCRIZIONE ";

          
      
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      int idx = 0;
      stmt.setLong(++idx, idParticella);
      if(Validator.isNotEmpty(dataInserimentoValidazione))
      {
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoValidazione));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoValidazione));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoValidazione));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoValidazione));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoValidazione));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoValidazione));
      }
      stmt.setLong(++idx, idParticella);
      if(Validator.isNotEmpty(dataInserimentoValidazione))
      {
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoValidazione));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoValidazione));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoValidazione));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoValidazione));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoValidazione));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoValidazione));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoValidazione));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoValidazione));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoValidazione));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoValidazione));
      }
      
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      while(rs.next())
      {
        if(vTipoArea == null)
          vTipoArea = new Vector<TipoAreaVO>();
        
        tipoAreaVO = new TipoAreaVO();
        tipoAreaVO.setIdTipoArea(rs.getLong("ID_TIPO_AREA"));
        tipoAreaVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoAreaVO.setFlagAreaModificabile(rs.getString("FLAG_AREA_MODIFICABILE"));
        tipoAreaVO.setFlagEsclusivoFoglio(rs.getString("FLAG_ESCLUSIVO_FOGLIO"));
        
        vTipoValoreArea = new Vector<TipoValoreAreaVO>();
        tipoValoreAreaVO = new TipoValoreAreaVO();
        tipoValoreAreaVO.setIdTipoValoreArea(rs.getLong("ID_TIPO_VALORE_AREA"));
        tipoValoreAreaVO.setDescrizione(rs.getString("DESC_VALORE"));
        tipoValoreAreaVO.setValore(rs.getString("VALORE"));
        vTipoValoreArea.add(tipoValoreAreaVO);
        
        tipoAreaVO.setvTipoValoreArea(vTipoValoreArea);
        
        vTipoArea.add(tipoAreaVO);
      }
      
      return vTipoArea;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("vTipoArea", vTipoArea),
          new Variabile("tipoAreaVO", tipoAreaVO),
          new Variabile("vTipoValoreArea", vTipoValoreArea),
          new Variabile("tipoValoreAreaVO", tipoValoreAreaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idParticella", idParticella), 
        new Parametro("dataInserimentoValidazione", dataInserimentoValidazione) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getValoriTipoAreaParticella] ", t,
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
          "[ParticellaGaaDAO::getValoriTipoAreaParticella] END.");
    }
  }
  
  
  
  public Vector<TipoAreaVO> getAllValoriTipoArea()
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoAreaVO> vTipoArea = null;
    TipoAreaVO tipoAreaVO = null;
    Vector<TipoValoreAreaVO> vTipoValoreArea = null;
    TipoValoreAreaVO tipoValoreAreaVO = null;
    
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::getAllValoriTipoArea] BEGIN.");

      query = "" +
        "SELECT TA.ID_TIPO_AREA, " +
        "       TA.DESCRIZIONE AS DESC_AREA, " + 
        "       TA.FLAG_AREA_MODIFICABILE, " +
        "       TA.FLAG_ESCLUSIVO_FOGLIO, " +
        "       TVA.ID_TIPO_VALORE_AREA, " +
        "       TVA.DESCRIZIONE AS DESC_VALORE, " +
        "       TVA.VALORE " +
        "FROM   DB_TIPO_AREA TA, " +
        "       DB_TIPO_VALORE_AREA TVA " +
        "WHERE  TA.DATA_FINE_VALIDITA IS NULL " +
        "AND    TA.ID_TIPO_AREA = TVA.ID_TIPO_AREA " +
        "AND    TVA.DATA_FINE_VALIDITA IS NULL " +
        "ORDER BY TA.DESCRIZIONE, " +
        "         TVA.DESCRIZIONE";

          
      
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      
      
      ResultSet rs = stmt.executeQuery();
      
      
      Long idTipoAreaTmp = new Long(-1);
      boolean first = true;
      while(rs.next())
      {
        if(vTipoArea == null)
          vTipoArea = new Vector<TipoAreaVO>();
        
        Long idTipoArea = new Long(rs.getLong("ID_TIPO_AREA"));
        if(idTipoAreaTmp.compareTo(idTipoArea) !=0)
        {          
          if(first)
          {
            first = false;
          }
          else
          {
            tipoAreaVO.setvTipoValoreArea(vTipoValoreArea);
            vTipoArea.add(tipoAreaVO);
          }
          
          tipoAreaVO = new TipoAreaVO();
          tipoAreaVO.setIdTipoArea(idTipoArea);
          tipoAreaVO.setDescrizione(rs.getString("DESC_AREA"));
          tipoAreaVO.setFlagEsclusivoFoglio(rs.getString("FLAG_ESCLUSIVO_FOGLIO"));
          tipoAreaVO.setFlagAreaModificabile(rs.getString("FLAG_AREA_MODIFICABILE"));
          vTipoValoreArea = new Vector<TipoValoreAreaVO>();          
        }
        
        
        
        
        tipoValoreAreaVO = new TipoValoreAreaVO();
        tipoValoreAreaVO.setDescrizione(rs.getString("DESC_VALORE"));
        tipoValoreAreaVO.setValore(rs.getString("VALORE"));
        tipoValoreAreaVO.setIdTipoValoreArea(rs.getLong("ID_TIPO_VALORE_AREA"));
        vTipoValoreArea.add(tipoValoreAreaVO);
        
        idTipoAreaTmp = idTipoArea;
      }
      
      if(vTipoArea != null)
      {
        tipoAreaVO.setvTipoValoreArea(vTipoValoreArea);
        vTipoArea.add(tipoAreaVO);
      }
      
      return vTipoArea;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("vTipoArea", vTipoArea),
          new Variabile("tipoAreaVO", tipoAreaVO),
          new Variabile("vTipoValoreArea", vTipoValoreArea),
          new Variabile("tipoValoreAreaVO", tipoValoreAreaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {};

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getAllValoriTipoArea] ", t,
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
          "[ParticellaGaaDAO::getAllValoriTipoArea] END.");
    }
  }
  
  
  public Vector<TipoAreaVO> getValoriTipoAreaFoglio(String comune, String foglio, String sezione)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoAreaVO> vTipoArea = null;
    TipoAreaVO tipoAreaVO = null;
    Vector<TipoValoreAreaVO> vTipoValoreArea = null;
    TipoValoreAreaVO tipoValoreAreaVO = null;
    
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::getValoriTipoAreaFoglio] BEGIN.");

      query = "" +
        "WITH  FOGLIO AS " +
        "  (SELECT TVA.ID_TIPO_VALORE_AREA, " +
        "          TVA.ID_TIPO_AREA, " +
        "          TVA.DESCRIZIONE AS DESC_VALORE, " +
        "          TVA.VALORE, " +
        "          TA.DESCRIZIONE AS DESC_TIPO, " +
        "          TA.FLAG_ESCLUSIVO_FOGLIO " +
        "   FROM   DB_R_FOGLIO_AREA RFA, " +
        "          DB_TIPO_VALORE_AREA TVA, " +
        "          DB_FOGLIO FF, " +
        "          DB_TIPO_AREA TA " +
        "   WHERE  FF.COMUNE = ? " +
        "   AND    FF.FOGLIO = ? ";
      if(Validator.isNotEmpty(sezione)) 
      {
        query += 
        "   AND    FF.SEZIONE = ? ";
      }
      else
      { 
        query += 
        "   AND    FF.SEZIONE IS NULL ";
      }
      query +=
        "   AND    RFA.DATA_FINE_VALIDITA IS NULL " +
        "   AND    TVA.DATA_FINE_VALIDITA IS NULL " +
        "   AND    TA.DATA_FINE_VALIDITA IS NULL " +
        "   AND    FF.ID_FOGLIO = RFA.ID_FOGLIO " +
        "   AND    RFA.ID_TIPO_VALORE_AREA = TVA.ID_TIPO_VALORE_AREA " +
        "   AND    TVA.ID_TIPO_AREA = TA.ID_TIPO_AREA) " +
        "SELECT TA.ID_TIPO_AREA, " +
        "       TA.DESCRIZIONE, " +
        "       TA.FLAG_AREA_MODIFICABILE, " +
        "       TA.FLAG_ESCLUSIVO_FOGLIO, " +
        "       FO.DESC_VALORE, " +
        "       FO.ID_TIPO_VALORE_AREA, " +
        "       FO.VALORE " +
        "FROM   DB_TIPO_AREA TA, " +
        "       FOGLIO FO " +
        "WHERE  TA.ID_TIPO_AREA = FO.ID_TIPO_AREA(+) " +
        "AND    TA.DATA_FINE_VALIDITA IS NULL " +
        "ORDER BY TA.DESCRIZIONE ";

          
      
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      int idx = 0;
      stmt.setString(++idx, comune);
      stmt.setString(++idx, foglio);
      if(Validator.isNotEmpty(sezione))
      {
        stmt.setString(++idx, sezione);
      }
      
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      while(rs.next())
      {
        if(vTipoArea == null)
          vTipoArea = new Vector<TipoAreaVO>();
        
        tipoAreaVO = new TipoAreaVO();
        tipoAreaVO.setIdTipoArea(rs.getLong("ID_TIPO_AREA"));
        tipoAreaVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoAreaVO.setFlagAreaModificabile(rs.getString("FLAG_AREA_MODIFICABILE"));
        tipoAreaVO.setFlagEsclusivoFoglio(rs.getString("FLAG_ESCLUSIVO_FOGLIO"));
        
        vTipoValoreArea = new Vector<TipoValoreAreaVO>();
        tipoValoreAreaVO = new TipoValoreAreaVO();
        tipoValoreAreaVO.setIdTipoValoreArea(rs.getLong("ID_TIPO_VALORE_AREA"));
        tipoValoreAreaVO.setDescrizione(rs.getString("DESC_VALORE"));
        tipoValoreAreaVO.setValore(rs.getString("VALORE"));
        vTipoValoreArea.add(tipoValoreAreaVO);
        
        tipoAreaVO.setvTipoValoreArea(vTipoValoreArea);
        
        vTipoArea.add(tipoAreaVO);
      }
      
      return vTipoArea;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("vTipoArea", vTipoArea),
          new Variabile("tipoAreaVO", tipoAreaVO),
          new Variabile("vTipoValoreArea", vTipoValoreArea),
          new Variabile("tipoValoreAreaVO", tipoValoreAreaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("comune", comune), 
        new Parametro("foglio", foglio),
        new Parametro("sezione", sezione) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getValoriTipoAreaFoglio] ", t,
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
          "[ParticellaGaaDAO::getValoriTipoAreaFoglio] END.");
    }
  }
  
  
  public Vector<TipoMetodoIrriguoVO> getElencoMetodoIrriguo()
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoMetodoIrriguoVO> vMetodoIrriguo = null;
    TipoMetodoIrriguoVO tipoMetodoIrriguoVO = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::getElencoMetodoIrriguo] BEGIN.");

      query = " " +
          "SELECT TMI.ID_METODO_IRRIGUO, " +
          "       TMI.CODICE_METODO_IRRIGUO, " +
          "       TMI.DESCRIZIONE_METODO_IRRIGUO " +
          "FROM   DB_TIPO_METODO_IRRIGUO TMI " +
          "WHERE  TMI.DATA_FINE_VALIDITA IS NULL " +
          "ORDER BY TMI.DESCRIZIONE_METODO_IRRIGUO ";
      
          
      
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);

      
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      while (rs.next())
      {
        if(vMetodoIrriguo == null)
          vMetodoIrriguo = new Vector<TipoMetodoIrriguoVO>();
        
        tipoMetodoIrriguoVO = new TipoMetodoIrriguoVO();
        tipoMetodoIrriguoVO.setIdMetodoIrriguo(rs.getLong("ID_METODO_IRRIGUO"));
        tipoMetodoIrriguoVO.setCodiceMetodoIrriguo(rs.getString("CODICE_METODO_IRRIGUO"));
        tipoMetodoIrriguoVO.setDescrizioneMetodoIrriguo(rs.getString("DESCRIZIONE_METODO_IRRIGUO"));
        
        vMetodoIrriguo.add(tipoMetodoIrriguoVO);
      }
      
      return vMetodoIrriguo;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("tipoMetodoIrriguoVO", tipoMetodoIrriguoVO),
          new Variabile("vMetodoIrriguo", vMetodoIrriguo) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getElencoMetodoIrriguo] ", t,
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
          "[ParticellaGaaDAO::getElencoMetodoIrriguo] END.");
    }
  }
  
  
  
  public Vector<TipoPeriodoSeminaVO> getListTipoPeriodoSeminaByCatalogo(long idUtilizzo,
      long idDestinazione, long idDettUso, long idQualiUso, long idVarieta) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoPeriodoSeminaVO> vTipoPeriodoSemina = null;
    TipoPeriodoSeminaVO tipoPeriodoSeminaVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::getListTipoPeriodoSeminaByCatalogo] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT TPS.ID_TIPO_PERIODO_SEMINA, " +
        "       TPS.CODICE, " + 
        "       TPS.DESCRIZIONE, " +
        "       RCMS.FLAG_DEFAULT, " +
        "       RCM.ID_CATALOGO_MATRICE " +
        "FROM   DB_R_CATALOGO_MATRICE RCM, " +
        "       DB_R_CATALOGO_MATRICE_SEMINA RCMS, " +
        "       DB_TIPO_PERIODO_SEMINA TPS " +
        "WHERE  RCM.ID_UTILIZZO = ? " +
        "AND    RCM.ID_TIPO_DESTINAZIONE = ? " +
        "AND    RCM.ID_TIPO_DETTAGLIO_USO = ?  " +
        "AND    RCM.ID_TIPO_QUALITA_USO = ? " +
        "AND    RCM.ID_VARIETA = ? " +
        "AND    RCM.ID_CATALOGO_MATRICE = RCMS.ID_CATALOGO_MATRICE " +
        "AND    RCMS.DATA_FINE_VALIDITA IS NULL " +
        "AND    RCMS.ID_TIPO_PERIODO_SEMINA = TPS.ID_TIPO_PERIODO_SEMINA " + 
        "ORDER BY TPS.DESCRIZIONE ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::getListTipoPeriodoSeminaByCatalogo] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int idx = 0;
      stmt.setLong(++idx, idUtilizzo);
      stmt.setLong(++idx, idDestinazione);
      stmt.setLong(++idx, idDettUso);
      stmt.setLong(++idx, idQualiUso);
      stmt.setLong(++idx, idVarieta);
      
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {        
        if(vTipoPeriodoSemina == null)
        {
          vTipoPeriodoSemina = new Vector<TipoPeriodoSeminaVO>();
        }
        
        tipoPeriodoSeminaVO = new TipoPeriodoSeminaVO();
        //per tutti uguale
        tipoPeriodoSeminaVO.setIdCatalogoMatrice(new Long(rs.getLong("ID_CATALOGO_MATRICE")));
        
        tipoPeriodoSeminaVO.setIdTipoPeriodoSemina(rs.getLong("ID_TIPO_PERIODO_SEMINA"));
        tipoPeriodoSeminaVO.setCodice(rs.getString("CODICE"));
        tipoPeriodoSeminaVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoPeriodoSeminaVO.setFlagDefault(rs.getString("FLAG_DEFAULT"));
       
        
        vTipoPeriodoSemina.add(tipoPeriodoSeminaVO);
        
      }
      
      return vTipoPeriodoSemina;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("vTipoPeriodoSemina", vTipoPeriodoSemina), 
          new Variabile("tipoPeriodoSeminaVO", tipoPeriodoSeminaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUtilizzo", idUtilizzo),
        new Parametro("idDestinazione", idDestinazione),
        new Parametro("idDettUso", idDettUso),
        new Parametro("idQualiUso", idQualiUso),
        new Parametro("idVarieta", idVarieta)};

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getListTipoPeriodoSeminaByCatalogo] ", t,
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
          "[ParticellaGaaDAO::getListTipoPeriodoSeminaByCatalogo] END.");
    }
  }
  
  
  public Long insertRParticellaArea(long idParticella, long idTipoValoreArea) throws DataAccessException
  {     
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idParticellaArea = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::insertRParticellaArea] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idParticellaArea = getNextPrimaryKey(SolmrConstants.SEQ_DB_R_PARTICELLA_AREA);
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_R_PARTICELLA_AREA   " 
              + "     (ID_PARTICELLA_AREA, "
              + "     ID_PARTICELLA, " 
              + "     ID_TIPO_VALORE_AREA, "
              + "     DATA_INIZIO_VALIDITA)  "
              + "   VALUES(?,?,?,SYSDATE) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::insertRParticellaArea] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idParticellaArea.longValue());
      stmt.setLong(++indice, idParticella);
      stmt.setLong(++indice, idTipoValoreArea);     
      
  
      stmt.executeUpdate();
      
      return idParticellaArea;
    }
    catch (Exception t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idParticellaArea", idParticellaArea)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idParticella", idParticella),
        new Parametro("idTipoValoreArea", idTipoValoreArea) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[ParticellaGaaDAO::insertRParticellaArea] ",
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
              "[ParticellaGaaDAO::insertRParticellaArea] END.");
    }
  }
  
  
  
  public Long getIdTipoValoreArea(long idTipoArea, String valore)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Long idTipoValoreArea = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::getIdTipoValoreArea] BEGIN.");

      query = " " +
          "SELECT TVA.ID_TIPO_VALORE_AREA " +
          "FROM   DB_TIPO_VALORE_AREA TVA " +
          "WHERE  TVA.ID_TIPO_AREA = ? " +
          "AND    TVA.VALORE = ? " +
          "AND    TVA.DATA_FINE_VALIDITA IS NULL ";
      
          
      
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      stmt.setLong(1, idTipoArea);
      stmt.setString(2, valore);
      
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      if (rs.next())
      {
        idTipoValoreArea = rs.getLong("ID_TIPO_VALORE_AREA");
      }
      
      return idTipoValoreArea;
    }
    catch (Exception t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("valore", valore) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("valore", valore), 
        new Parametro("idTipoArea", idTipoArea) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getIdTipoValoreArea] ", t,
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
          "[ParticellaGaaDAO::getIdTipoValoreArea] END.");
    }
  }
  
  
  public void storicizzaRParticellaArea(long idParticella, long idTipoValoreArea) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger.debug(this,
              "[ParticellaGaaDAO::storicizzaRParticellaArea] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "UPDATE DB_R_PARTICELLA_AREA  " +
        "SET    DATA_FINE_VALIDITA = ? " +
        "WHERE  ID_PARTICELLA = ?  " +
        "AND    ID_TIPO_VALORE_AREA = ? " +
        "AND    DATA_FINE_VALIDITA IS NULL ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::storicizzaRParticellaArea] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setTimestamp(++indice, convertDateToTimestamp(new Date()));
      stmt.setLong(++indice, idParticella);
      stmt.setLong(++indice, idTipoValoreArea);
      
  
      stmt.executeUpdate();
    }
    catch (Exception t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idParticella", idParticella),
        new Parametro("idTipoValoreArea", idTipoValoreArea) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[ParticellaGaaDAO::storicizzaRParticellaArea] ",
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
              "[ParticellaGaaDAO::storicizzaRParticellaArea] END.");
    }
  }
  
  
  public Vector<TipoFonteVO> getElencoAllTipoFonte()
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoFonteVO> vTipoFonte = null;
    TipoFonteVO tipoFonteVO = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::getElencoAllTipoFonte] BEGIN.");

      query = 
        "SELECT TF.ID_FONTE, " +
        "       TF.DESCRIZIONE " + 
        "FROM   DB_TIPO_FONTE TF " +
        "ORDER BY TF.DESCRIZIONE ";
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query

      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vTipoFonte == null)
          vTipoFonte = new Vector<TipoFonteVO>();
        
        tipoFonteVO = new TipoFonteVO();
        tipoFonteVO.setIdFonte(rs.getLong("ID_FONTE"));
        tipoFonteVO.setDescrizione(rs.getString("DESCRIZIONE"));
        
        vTipoFonte.add(tipoFonteVO);
      }
      
      
      return vTipoFonte;
      
    }
    catch (Exception t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("vTipoFonte", vTipoFonte),
          new Variabile("tipoFonteVO", tipoFonteVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getElencoAllTipoFonte] ", t,
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
          "[ParticellaGaaDAO::getElencoAllTipoFonte] END.");
    }
  }
  
  
  
  public boolean isUtilizzoAttivoSuMatrice(long idUtilizzo) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean attivo = false;
    try
    {
      SolmrLogger.debug(this,
          "[ParticellaGaaDAO::isUtilizzoAttivoSuMatrice] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT RCM.ID_CATALOGO_MATRICE " +
        "FROM   DB_R_CATALOGO_MATRICE RCM " +
        "WHERE  RCM.ID_UTILIZZO = ? " +
        "AND    RCM.DATA_FINE_VALIDITA IS NULL ");

      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::isUtilizzoAttivoSuMatrice] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int idx = 0;
      stmt.setLong(++idx, idUtilizzo);
      
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      {        
        attivo = true;        
      }
      
      return attivo;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("attivo", attivo) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUtilizzo", idUtilizzo) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::isUtilizzoAttivoSuMatrice] ", t,
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
          "[ParticellaGaaDAO::isUtilizzoAttivoSuMatrice] END.");
    }
  }
  
  
  
  public Vector<TipoAreaVO> riepilogoTipoArea(long idAzienda)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoAreaVO> vTipoArea = null;
    TipoAreaVO tipoAreaVO = null;
    Vector<TipoValoreAreaVO> vTipoValoreArea = null;
    TipoValoreAreaVO tipoValoreAreaVO = null;
    
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::riepilogoTipoArea] BEGIN.");

      query = "" +
        "SELECT TA.DESCRIZIONE AS DESC_TIPO_AREA, " +
        "       TA.ID_TIPO_AREA, " +
        "       TVA.DESCRIZIONE AS DESC_TIPO_VALORE_AREA, " +
        "       TVA.ID_TIPO_VALORE_AREA, " +
        "       TA.FLAG_ESCLUSIVO_FOGLIO, " +
        "       SUM(UP.SUPERFICIE_UTILIZZATA) AS SUM_SUP_UTIL " +
        "FROM   DB_CONDUZIONE_PARTICELLA CP, " +
        "       DB_UTE UT,  " +
        "       DB_TIPO_VALORE_AREA TVA, " +
        "       DB_TIPO_AREA TA, " +
        "       DB_R_PARTICELLA_AREA RPA, " +
        "       DB_UTILIZZO_PARTICELLA UP " +
        "WHERE  UT.ID_AZIENDA = ? " +
        "AND    UT.DATA_FINE_ATTIVITA IS NULL " +
        "AND    UT.ID_UTE = CP.ID_UTE " +
        "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA " +
        "AND    CP.ID_PARTICELLA = RPA.ID_PARTICELLA " +
        "AND    RPA.DATA_FINE_VALIDITA IS NULL " +
        "AND    RPA.ID_TIPO_VALORE_AREA = TVA.ID_TIPO_VALORE_AREA " +
        "AND    TVA.ID_TIPO_AREA = TA.ID_TIPO_AREA " +
        "AND    TVA.DATA_FINE_VALIDITA IS NULL " +
        "AND    TA.FLAG_VISIBILE_RIEPILOGO = 'S' " +
        "AND    TA.FLAG_ESCLUSIVO_FOGLIO = 'N' " +
        "AND    TA.DATA_FINE_VALIDITA IS NULL " +
        "GROUP BY TA.DESCRIZIONE," +
        "         TA.ID_TIPO_AREA, " +
        "         TVA.DESCRIZIONE, " +
        "         TVA.ID_TIPO_VALORE_AREA, " +
        "         TA.FLAG_ESCLUSIVO_FOGLIO " +
        "UNION " +
        "SELECT TA.DESCRIZIONE AS DESC_TIPO_AREA, " +
        "       TA.ID_TIPO_AREA, " +
        "       TVA.DESCRIZIONE AS DESC_TIPO_VALORE_AREA, " +
        "       TVA.ID_TIPO_VALORE_AREA, " +
        "       TA.FLAG_ESCLUSIVO_FOGLIO, " +
        "       SUM(UP.SUPERFICIE_UTILIZZATA) AS SUM_SUP_UTIL " +
        "FROM   DB_CONDUZIONE_PARTICELLA CP, " +
        "       DB_STORICO_PARTICELLA SP, " +
        "       DB_UTE UT, " +
        "       DB_TIPO_VALORE_AREA TVA, " +
        "       DB_TIPO_AREA TA, " +
        "       DB_R_FOGLIO_AREA RFA, " +
        "       DB_UTILIZZO_PARTICELLA UP, " +
        "       DB_FOGLIO FF " +
        "WHERE  UT.ID_AZIENDA = ? " +
        "AND    UT.DATA_FINE_ATTIVITA IS NULL " +
        "AND    UT.ID_UTE = CP.ID_UTE " +
        "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA " +
        "AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
        "AND    SP.DATA_FINE_VALIDITA IS NULL " +
        "AND    SP.COMUNE = FF.COMUNE " +
        "AND    NVL(SP.SEZIONE,'-') = NVL(FF.SEZIONE,'-') " +
        "AND    SP.FOGLIO = FF.FOGLIO " +
        "AND    FF.ID_FOGLIO = RFA.ID_FOGLIO " +
        "AND    RFA.DATA_FINE_VALIDITA IS NULL " +
        "AND    RFA.ID_TIPO_VALORE_AREA = TVA.ID_TIPO_VALORE_AREA " +
        "AND    TVA.ID_TIPO_AREA = TA.ID_TIPO_AREA " +
        "AND    TVA.DATA_FINE_VALIDITA IS NULL " +
        "AND    TA.FLAG_VISIBILE_RIEPILOGO = 'S' " +
        "AND    TA.FLAG_ESCLUSIVO_FOGLIO = 'S' " +
        "AND    TA.DATA_FINE_VALIDITA IS NULL " +
        "GROUP BY TA.DESCRIZIONE, " +
        "         TA.ID_TIPO_AREA, " +
        "         TVA.DESCRIZIONE, " +
        "         TVA.ID_TIPO_VALORE_AREA," +
        "         TA.FLAG_ESCLUSIVO_FOGLIO " +
        "ORDER BY DESC_TIPO_AREA," +
        "         DESC_TIPO_VALORE_AREA";

          
      
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      int idx = 0;
      stmt.setLong(++idx, idAzienda);
      stmt.setLong(++idx, idAzienda);
      
      
      
      ResultSet rs = stmt.executeQuery();
      
      
      long idTipoAreaTmp = 0;
      while(rs.next())
      {
        if(vTipoArea == null)
          vTipoArea = new Vector<TipoAreaVO>();
        
        long idTipoArea = rs.getLong("ID_TIPO_AREA");
        if(idTipoAreaTmp != idTipoArea)
        {
          if(idTipoAreaTmp != 0)
          {
            tipoAreaVO.setvTipoValoreArea(vTipoValoreArea);
            vTipoArea.add(tipoAreaVO);
          }
          
          tipoAreaVO = new TipoAreaVO();
          tipoAreaVO.setIdTipoArea(rs.getLong("ID_TIPO_AREA"));
          tipoAreaVO.setDescrizione(rs.getString("DESC_TIPO_AREA"));
          tipoAreaVO.setFlagEsclusivoFoglio(rs.getString("FLAG_ESCLUSIVO_FOGLIO"));
          vTipoValoreArea = new Vector<TipoValoreAreaVO>();         
        }
        
        tipoValoreAreaVO = new TipoValoreAreaVO();
        tipoValoreAreaVO.setIdTipoValoreArea(rs.getLong("ID_TIPO_VALORE_AREA"));
        tipoValoreAreaVO.setDescrizione(rs.getString("DESC_TIPO_VALORE_AREA"));
        tipoValoreAreaVO.setSupUtilizzata(rs.getBigDecimal("SUM_SUP_UTIL"));
        vTipoValoreArea.add(tipoValoreAreaVO);
        
        
        
        idTipoAreaTmp = idTipoArea;
      }
      
      if(vTipoArea != null)
      {
        tipoAreaVO.setvTipoValoreArea(vTipoValoreArea);
        vTipoArea.add(tipoAreaVO);
      }
      
      return vTipoArea;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("vTipoArea", vTipoArea),
          new Variabile("tipoAreaVO", tipoAreaVO),
          new Variabile("vTipoValoreArea", vTipoValoreArea),
          new Variabile("tipoValoreAreaVO", tipoValoreAreaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::riepilogoTipoArea] ", t,
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
          "[ParticellaGaaDAO::riepilogoTipoArea] END.");
    }
  }
  
  
  public Vector<TipoAreaVO> riepilogoTipoAreaDichiarato(long idDichiarazioneConsistenza)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoAreaVO> vTipoArea = null;
    TipoAreaVO tipoAreaVO = null;
    Vector<TipoValoreAreaVO> vTipoValoreArea = null;
    TipoValoreAreaVO tipoValoreAreaVO = null;
    
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::riepilogoTipoAreaDichiarato] BEGIN.");

      query = "" +
        "SELECT TA.DESCRIZIONE AS DESC_TIPO_AREA, " +
        "       TA.ID_TIPO_AREA, " +
        "       TVA.DESCRIZIONE AS DESC_TIPO_VALORE_AREA, " +
        "       TVA.ID_TIPO_VALORE_AREA, " +
        "       TA.FLAG_ESCLUSIVO_FOGLIO, " +
        "       SUM(UD.SUPERFICIE_UTILIZZATA) AS SUM_SUP_UTIL " +
        "FROM   DB_CONDUZIONE_DICHIARATA CD, " +
        "       DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "       DB_TIPO_VALORE_AREA TVA, " +
        "       DB_TIPO_AREA TA, " +
        "       DB_R_PARTICELLA_AREA RPA, " +
        "       DB_UTILIZZO_DICHIARATO UD " +
        "WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        "AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA " +
        "AND    CD.ID_PARTICELLA = RPA.ID_PARTICELLA " +
        "AND    RPA.DATA_INIZIO_VALIDITA < DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "AND    NVL(RPA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) > DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "AND    RPA.ID_TIPO_VALORE_AREA = TVA.ID_TIPO_VALORE_AREA " +
        "AND    TVA.ID_TIPO_AREA = TA.ID_TIPO_AREA " +
        "AND    TVA.DATA_INIZIO_VALIDITA < DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "AND    NVL(TVA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) > DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "AND    TA.FLAG_VISIBILE_RIEPILOGO = 'S' " +
        "AND    TA.FLAG_ESCLUSIVO_FOGLIO = 'N' " +
        "AND    TA.DATA_INIZIO_VALIDITA < DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "AND    NVL(TA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) > DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "GROUP BY TA.DESCRIZIONE, " +
        "         TA.ID_TIPO_AREA, " +
        "         TVA.DESCRIZIONE, " +
        "         TVA.ID_TIPO_VALORE_AREA, " +
        "         TA.FLAG_ESCLUSIVO_FOGLIO " +
        "UNION " +
        "SELECT TA.DESCRIZIONE AS DESC_TIPO_AREA, " +
        "       TA.ID_TIPO_AREA, " +
        "       TVA.DESCRIZIONE AS DESC_TIPO_VALORE_AREA, " +
        "       TVA.ID_TIPO_VALORE_AREA, " +
        "       TA.FLAG_ESCLUSIVO_FOGLIO, " +
        "       SUM(UD.SUPERFICIE_UTILIZZATA) AS SUM_SUP_UTIL " +
        "FROM   DB_CONDUZIONE_DICHIARATA CD, " +
        "       DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "       DB_STORICO_PARTICELLA SP, " +
        "       DB_TIPO_VALORE_AREA TVA, " +
        "       DB_TIPO_AREA TA, " +
        "       DB_R_FOGLIO_AREA RFA, " +
        "       DB_UTILIZZO_DICHIARATO UD, " +
        "       DB_FOGLIO FF " +
        "WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        "AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA " +
        "AND    CD.ID_PARTICELLA = SP.ID_PARTICELLA " +
        "AND    SP.DATA_INIZIO_VALIDITA < DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "AND    NVL(SP.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) > DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "AND    SP.COMUNE = FF.COMUNE " +
        "AND    NVL(SP.SEZIONE,'-') = NVL(FF.SEZIONE,'-') " +
        "AND    SP.FOGLIO = FF.FOGLIO " +
        "AND    FF.ID_FOGLIO = RFA.ID_FOGLIO " +
        "AND    RFA.DATA_INIZIO_VALIDITA < DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "AND    NVL(RFA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) > DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "AND    RFA.ID_TIPO_VALORE_AREA = TVA.ID_TIPO_VALORE_AREA " +
        "AND    TVA.ID_TIPO_AREA = TA.ID_TIPO_AREA " +
        "AND    TVA.DATA_INIZIO_VALIDITA < DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "AND    NVL(TVA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) > DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "AND    TA.FLAG_VISIBILE_RIEPILOGO = 'S' " +
        "AND    TA.FLAG_ESCLUSIVO_FOGLIO = 'S' " +
        "AND    TA.DATA_INIZIO_VALIDITA < DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "AND    NVL(TA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) > DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "GROUP BY TA.DESCRIZIONE, " +
        "         TA.ID_TIPO_AREA, " +
        "         TVA.DESCRIZIONE, " +
        "         TVA.ID_TIPO_VALORE_AREA, " +
        "         TA.FLAG_ESCLUSIVO_FOGLIO " +
        "ORDER BY DESC_TIPO_AREA," +
        "         DESC_TIPO_VALORE_AREA";

          
      
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      int idx = 0;
      stmt.setLong(++idx, idDichiarazioneConsistenza);
      stmt.setLong(++idx, idDichiarazioneConsistenza);
      
      
      
      ResultSet rs = stmt.executeQuery();
      
      
      long idTipoAreaTmp = 0;
      while(rs.next())
      {
        if(vTipoArea == null)
          vTipoArea = new Vector<TipoAreaVO>();
        
        long idTipoArea = rs.getLong("ID_TIPO_AREA");
        if(idTipoAreaTmp != idTipoArea)
        {
          if(idTipoAreaTmp != 0)
          {
            tipoAreaVO.setvTipoValoreArea(vTipoValoreArea);
            vTipoArea.add(tipoAreaVO);
          }
          
          tipoAreaVO = new TipoAreaVO();
          tipoAreaVO.setIdTipoArea(rs.getLong("ID_TIPO_AREA"));
          tipoAreaVO.setDescrizione(rs.getString("DESC_TIPO_AREA"));
          tipoAreaVO.setFlagEsclusivoFoglio(rs.getString("FLAG_ESCLUSIVO_FOGLIO"));
          vTipoValoreArea = new Vector<TipoValoreAreaVO>();         
        }
        
        tipoValoreAreaVO = new TipoValoreAreaVO();
        tipoValoreAreaVO.setIdTipoValoreArea(rs.getLong("ID_TIPO_VALORE_AREA"));
        tipoValoreAreaVO.setDescrizione(rs.getString("DESC_TIPO_VALORE_AREA"));
        tipoValoreAreaVO.setSupUtilizzata(rs.getBigDecimal("SUM_SUP_UTIL"));
        vTipoValoreArea.add(tipoValoreAreaVO);
        
        
        
        idTipoAreaTmp = idTipoArea;
      }
      
      if(vTipoArea != null)
      {
        tipoAreaVO.setvTipoValoreArea(vTipoValoreArea);
        vTipoArea.add(tipoAreaVO);
      }
      
      return vTipoArea;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("vTipoArea", vTipoArea),
          new Variabile("tipoAreaVO", tipoAreaVO),
          new Variabile("vTipoValoreArea", vTipoValoreArea),
          new Variabile("tipoValoreAreaVO", tipoValoreAreaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::riepilogoTipoAreaDichiarato] ", t,
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
          "[ParticellaGaaDAO::riepilogoTipoAreaDichiarato] END.");
    }
  }
  
  public Vector<TipoAreaVO> getValoriTipoAreaFiltroElenco(Long idDichiarazioneConsistenza)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoAreaVO> vTipoArea = null;
    TipoAreaVO tipoAreaVO = null;
    Vector<TipoValoreAreaVO> vTipoValoreArea = null;
    TipoValoreAreaVO tipoValoreAreaVO = null;
    
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::getValoriTipoAreaFiltroElenco] BEGIN.");

      query = "" +
        "SELECT TA.ID_TIPO_AREA, " +
        "       TA.DESCRIZIONE AS DESC_AREA, " + 
        "       TA.FLAG_AREA_MODIFICABILE, " +
        "       TA.FLAG_ESCLUSIVO_FOGLIO, " +
        "       TVA.ID_TIPO_VALORE_AREA, " +
        "       TVA.DESCRIZIONE AS DESC_VALORE, " +
        "       TVA.VALORE " +
        "FROM   DB_TIPO_AREA TA, " +
        "       DB_TIPO_VALORE_AREA TVA ";
      if(Validator.isNotEmpty(idDichiarazioneConsistenza))
      {
        query += 
        "       ,DB_DICHIARAZIONE_CONSISTENZA DC ";
      }
      query +=
        "WHERE  TA.ID_TIPO_AREA = TVA.ID_TIPO_AREA " +
        "AND    TA.FLAG_VISIBILE_RIEPILOGO = 'S' ";
      if(Validator.isNotEmpty(idDichiarazioneConsistenza))
      {
        query +=
        "AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "AND    TA.DATA_INIZIO_VALIDITA < DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "AND    NVL(TA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) > DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "AND    TVA.DATA_INIZIO_VALIDITA < DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "AND    NVL(TVA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) > DC.DATA_INSERIMENTO_DICHIARAZIONE ";    
      }
      else
      {
        query +=
        "AND    TA.DATA_FINE_VALIDITA IS NULL " +
        "AND    TVA.DATA_FINE_VALIDITA IS NULL ";      
      }
      query += 
        "ORDER BY TA.DESCRIZIONE, " +
        "         TVA.DESCRIZIONE";

          
      
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      int idx = 0;
      if(Validator.isNotEmpty(idDichiarazioneConsistenza))
        stmt.setLong(++idx, idDichiarazioneConsistenza);
      
      
      
      ResultSet rs = stmt.executeQuery();
      
      
      Long idTipoAreaTmp = new Long(-1);
      boolean first = true;
      while(rs.next())
      {
        if(vTipoArea == null)
          vTipoArea = new Vector<TipoAreaVO>();
        
        Long idTipoArea = new Long(rs.getLong("ID_TIPO_AREA"));
        if(idTipoAreaTmp.compareTo(idTipoArea) !=0)
        {          
          if(first)
          {
            first = false;
          }
          else
          {
            tipoAreaVO.setvTipoValoreArea(vTipoValoreArea);
            vTipoArea.add(tipoAreaVO);
          }
          
          tipoAreaVO = new TipoAreaVO();
          tipoAreaVO.setIdTipoArea(idTipoArea);
          tipoAreaVO.setDescrizione(rs.getString("DESC_AREA"));
          tipoAreaVO.setFlagEsclusivoFoglio(rs.getString("FLAG_ESCLUSIVO_FOGLIO"));
          tipoAreaVO.setFlagAreaModificabile(rs.getString("FLAG_AREA_MODIFICABILE"));
          vTipoValoreArea = new Vector<TipoValoreAreaVO>();          
        }
        
        
        
        
        tipoValoreAreaVO = new TipoValoreAreaVO();
        tipoValoreAreaVO.setDescrizione(rs.getString("DESC_VALORE"));
        tipoValoreAreaVO.setValore(rs.getString("VALORE"));
        tipoValoreAreaVO.setIdTipoValoreArea(rs.getLong("ID_TIPO_VALORE_AREA"));
        vTipoValoreArea.add(tipoValoreAreaVO);
        
        idTipoAreaTmp = idTipoArea;
      }
      
      if(vTipoArea != null)
      {
        tipoAreaVO.setvTipoValoreArea(vTipoValoreArea);
        vTipoArea.add(tipoAreaVO);
      }
      
      return vTipoArea;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("vTipoArea", vTipoArea),
          new Variabile("tipoAreaVO", tipoAreaVO),
          new Variabile("vTipoValoreArea", vTipoValoreArea),
          new Variabile("tipoValoreAreaVO", tipoValoreAreaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {};

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getAllValoriTipoArea] ", t,
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
          "[ParticellaGaaDAO::getAllValoriTipoArea] END.");
    }
  }
  
  
  public Vector<TipoAreaVO> getDescTipoAreaBrogliaccio(Long idDichiarazioneConsistenza)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoAreaVO> vTipoArea = null;
    TipoAreaVO tipoAreaVO = null;    
    
    try
    {
      SolmrLogger.debug(this, "[ParticellaGaaDAO::getDescTipoAreaBrogliaccio] BEGIN.");

      query = "" +
        "SELECT TA.ID_TIPO_AREA, " +
        "       TA.DESCRIZIONE_ESTESA, " +
        "       TA.FLAG_ESCLUSIVO_FOGLIO " + 
        "FROM   DB_TIPO_AREA TA ";
      if(Validator.isNotEmpty(idDichiarazioneConsistenza))
      {
        query += 
        "       ,DB_DICHIARAZIONE_CONSISTENZA DC ";
      }
      query +=
        "WHERE  TA.FLAG_VISIBILE_BROGLIACCIO = 'S' ";
      if(Validator.isNotEmpty(idDichiarazioneConsistenza))
      {
        query +=
        "AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "AND    TA.DATA_INIZIO_VALIDITA < DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "AND    NVL(TA.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) > DC.DATA_INSERIMENTO_DICHIARAZIONE ";
      }
      else
      {
        query +=
        "AND    TA.DATA_FINE_VALIDITA IS NULL ";
      }
      query += 
        "ORDER BY TA.DESCRIZIONE_ESTESA ";

          
      
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      int idx = 0;
      if(Validator.isNotEmpty(idDichiarazioneConsistenza))
        stmt.setLong(++idx, idDichiarazioneConsistenza);
      
      
      
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(vTipoArea == null)
          vTipoArea = new Vector<TipoAreaVO>();
        
        tipoAreaVO = new TipoAreaVO();
        tipoAreaVO.setIdTipoArea(new Long(rs.getLong("ID_TIPO_AREA")));
        tipoAreaVO.setDescrizioneEstesa(rs.getString("DESCRIZIONE_ESTESA"));    
        tipoAreaVO.setFlagEsclusivoFoglio(rs.getString("FLAG_ESCLUSIVO_FOGLIO"));
        
        vTipoArea.add(tipoAreaVO);
      }     
      
      return vTipoArea;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("vTipoArea", vTipoArea),
          new Variabile("tipoAreaVO", tipoAreaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {};

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaGaaDAO::getDescTipoAreaBrogliaccio] ", t,
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
          "[ParticellaGaaDAO::getDescTipoAreaBrogliaccio] END.");
    }
  }
    
  
  
  
  
  
  
  

}
