package it.csi.solmr.integration.anag;

import it.csi.smranag.smrgaa.dto.PlSqlCodeDescription;
import it.csi.smranag.smrgaa.dto.fascicoli.InvioFascicoliVO;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.anag.ConsistenzaVO;
import it.csi.solmr.dto.anag.ErrAnomaliaDicConsistenzaVO;
import it.csi.solmr.dto.anag.EsitoPianoGraficoVO;
import it.csi.solmr.dto.anag.TemporaneaPraticaAziendaVO;
import it.csi.solmr.dto.anag.consistenza.FascicoloNazionaleVO;
import it.csi.solmr.dto.anag.consistenza.TipoMotivoDichiarazioneVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneParticellaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

public class ConsistenzaDAO extends it.csi.solmr.integration.BaseDAO
{

  public ConsistenzaDAO() throws ResourceAccessException
  {
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public ConsistenzaDAO(String refName) throws ResourceAccessException
  {
    super(refName);
  }

  // Metodo per controllare se l'azienda idAzienda ha fatto una previsione del
  // piano
  // colturale per l'anno sucessivo. Restituisce true se è stata fatta la
  // previsione,
  // false se non è stata fatta
  public boolean previsioneAnnoSucessivo(Long idAzienda)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    boolean ris = false;

    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT U.ID_UTE "
          + "FROM DB_UTILIZZO_PARTICELLA UP,DB_CONDUZIONE_PARTICELLA CP,DB_UTE U "
          + "WHERE U.ID_AZIENDA = ? " + "AND CP.ID_UTE = U.ID_UTE  "
          + "AND UP.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA  "
          + "AND CP.DATA_FINE_CONDUZIONE IS NULL  " + "AND UP.ANNO = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing previsioneAnnoSucessivo: " + query);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setInt(2, DateUtils.getCurrentYear().intValue() + 1);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        ris = true;
      }
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "previsioneAnnoSucessivo - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "previsioneAnnoSucessivo - Generic Exception: "
          + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "previsioneAnnoSucessivo - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "previsioneAnnoSucessivo - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return ris;
  }

  // Metodo per controllare se sono state fatte delle modifiche a seguito
  // dell'ultima
  // dichiarazione. Restituisce true se sono state fatte modifiche, false
  // altrimenti
  public boolean controlloUltimeModifiche(Long idAzienda, Integer anno)
      throws DataAccessException
  {
    return true;
    /*
     * Connection conn = null; PreparedStatement stmt = null; boolean ris =
     * false; Date date1=null; Date date2=null;
     * 
     * try {
     * 
     * conn = getDatasource().getConnection();
     * 
     * String query = "SELECT MAX (AC.DATA_AGGIORNAMENTO) AS DATA " + "FROM
     * V_AGGIORNAMENTO_CONSISTENZA AC " + "WHERE AC.ID_AZIENDA = ? " ;
     * 
     * stmt = conn.prepareStatement(query);
     * 
     * SolmrLogger.debug(this, "Executing controlloUltimeModifiche: "+query);
     * 
     * stmt.setLong(1, idAzienda.longValue());
     * 
     * ResultSet rs = stmt.executeQuery();
     * 
     * if (rs.next()) { date1=rs.getTimestamp("DATA"); } rs.close();
     * stmt.close();
     * 
     * query = "SELECT MAX(DC.DATA) AS DATA " + "FROM
     * DB_DICHIARAZIONE_CONSISTENZA DC " + "WHERE DC.ID_AZIENDA = ? " + "AND
     * DC.ANNO = ? " ;
     * 
     * stmt = conn.prepareStatement(query);
     * 
     * SolmrLogger.debug(this, "Executing controlloUltimeModifiche: "+query);
     * 
     * stmt.setLong(1, idAzienda.longValue()); stmt.setInt(2, anno.intValue());
     * 
     * rs = stmt.executeQuery();
     * 
     * if (rs.next()) { date2=rs.getTimestamp("DATA"); } rs.close();
     * stmt.close();
     * 
     * if (date2==null) ris=true; else { if (date1!=null) if
     * (date1.after(date2)) ris=true; } } catch (SQLException exc) {
     * SolmrLogger.fatal(this, "controlloUltimeModifiche - SQLException:
     * "+exc.getMessage()); throw new DataAccessException(exc.getMessage()); }
     * catch (Exception ex) { SolmrLogger.fatal(this, "controlloUltimeModifiche -
     * Generic Exception: "+ex); throw new DataAccessException(ex.getMessage()); }
     * finally { try { if (stmt != null) stmt.close(); if (conn != null)
     * conn.close(); } catch (SQLException exc) { SolmrLogger.fatal(this,
     * "controlloUltimeModifiche - SQLException while closing Statement and
     * Connection: "+exc.getMessage()); throw new
     * DataAccessException(exc.getMessage()); } catch (Exception ex) {
     * SolmrLogger.fatal(this, "controlloUltimeModifiche - Generic Exception
     * while closing Statement and Connection: "+ex.getMessage()); throw new
     * DataAccessException(ex.getMessage()); } } return ris;
     */
  }

  /**
   * Metodo per richiamare la procedura plsql per i controlli sulla consistenza
   * Il parametro restituito può assumere tre valori: - N: richiedere la
   * motivazione della dichiarazione e permettere il salvataggio della
   * dichiarazione di consistenza - E: visualizzare gli errori in ordine di
   * argomento e non permettere il proseguimento - A: visualizzare le anomalie,
   * richiedere la motivazione della dichiarazione e permettere il salvataggio
   */
  public String controlliDichiarazionePLSQL(Long idAzienda, Integer anno,
      Long idMotivoDichiarazione, Long idUtente) throws DataAccessException, SolmrException
  {
    Connection conn = null;
    CallableStatement cs = null;
    try
    {
      /*************************************************************************
       * PROCEDURE CONTROLLI_DICHIARAZIONE(P_ID_AZIENDA              IN DB_AZIENDA.ID_AZIENDA%TYPE,
       * P_ANNO_RIF                IN DB_UTILIZZO_PARTICELLA.ANNO%TYPE,
       * P_ID_MOTIVO_DICHIARAZIONE IN DB_TIPO_MOTIVO_DICHIARAZIONE.ID_MOTIVO_DICHIARAZIONE%TYPE,
       * P_ID_UTENTE_LOGIN         IN UTENTE_LOGIN.ID_UTENTE%TYPE,
       * P_ESITO_CONTR            OUT VARCHAR2,
       * P_MSGERR              IN OUT VARCHAR2,
       * P_CODERR              IN OUT VARCHAR2) IS
       */

      SolmrLogger.debug(this, "controlliDichiarazionePLSQL - idAzienda: "
          + idAzienda);
      SolmrLogger.debug(this, "controlliDichiarazionePLSQL - anno: " + anno);
      conn = getDatasource().getConnection();
      String sql = "{call PACK_DICHIARAZIONE_CONSISTENZA.CONTROLLI_DICHIARAZIONE(?,?,?,?,?,?,?)}";
      cs = conn.prepareCall(sql);
      cs.setLong(1, idAzienda.longValue());
      cs.setInt(2, anno.intValue());
      cs.setLong(3, idMotivoDichiarazione.longValue());
      cs.setLong(4, idUtente.longValue());
      cs.registerOutParameter(5, Types.VARCHAR);
      cs.registerOutParameter(6, Types.VARCHAR);
      cs.registerOutParameter(7, Types.VARCHAR);

      cs.executeUpdate();
      String msgErr = cs.getString(6);
      String errorCode = cs.getString(7);

      if (!(errorCode == null || "".equals(errorCode)))
        throw new SolmrException((String) AnagErrors.get("ERR_PLSQL")
            + errorCode + " - " + msgErr);
      return cs.getString(5);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    catch (SQLException sqle)
    {
      SolmrLogger.fatal(this, "controlliDichiarazionePLSQL - SQLException: "
          + sqle.getMessage());
      throw new DataAccessException(sqle.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(this, "controlliDichiarazionePLSQL - Generic Exception: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (cs != null)
          cs.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "controlliDichiarazionePLSQL - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "controlliDichiarazionePLSQL - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  /**
   * Metodo per richiamare la procedura plsql per i controlli sulla particelle
   */
  public void controlliParticellarePLSQL(Long idAzienda, Integer anno, Long idUtente)
      throws DataAccessException, SolmrException
  {
    Connection conn = null;
    CallableStatement cs = null;
    try
    {
      /*************************************************************************
       * PROCEDURE ESEGUI_CONTROLLI(P_ID_AZIENDA         IN DB_AZIENDA.ID_AZIENDA%TYPE,
       *                            P_ANNO_RIF         IN DB_UTILIZZO_PARTICELLA.ANNO%TYPE,
       *                            P_ID_UTENTE_LOGIN    IN ?.ID_UTENTE%TYPE,
       *                            P_MSGERR           IN OUT VARCHAR2,
       *                            P_CODERR         IN OUT VARCHAR2) IS 
       */

      conn = getDatasource().getConnection();
      String sql = "{call PACK_CONTROLLI_PARTICELLARE.ESEGUI_CONTROLLI(?,?,?,?,?)}";
      cs = conn.prepareCall(sql);
      cs.setLong(1, idAzienda.longValue());
      cs.setInt(2, anno.intValue());
      cs.setLong(3, idUtente.longValue());
      cs.registerOutParameter(4, Types.VARCHAR);
      cs.registerOutParameter(5, Types.VARCHAR);

      cs.executeUpdate();
      String msgErr = cs.getString(4);
      String errorCode = cs.getString(5);

      if (!(errorCode == null || "".equals(errorCode)))
        throw new SolmrException((String) AnagErrors.get("ERR_PLSQL")
            + errorCode + " - " + msgErr);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    catch (SQLException sqle)
    {
      SolmrLogger.fatal(this, "controlliParticellarePLSQL - SQLException: "
          + sqle.getMessage());
      throw new DataAccessException(sqle.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "controlliParticellarePLSQL - Generic Exception: " + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (cs != null)
          cs.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "controlliParticellarePLSQL - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "controlliParticellarePLSQL - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  /**
   * Metodo per richiamare la procedura plsql per la verifica dei terreni (
   * idGruppoControllo = 1) o della consistenza (idGruppoControllo = null) Il
   * parametro restituito può assumere due valori: - N: I dati relativi alla
   * consistenza risultano corretti - S: visualizzare le anomalie
   */
  public String controlliVerificaPLSQL(Long idAzienda, Integer anno,
      Integer idGruppoControllo, Long idUtente) throws DataAccessException, SolmrException
  {
    Connection conn = null;
    CallableStatement cs = null;
    try
    {
      /*************************************************************************
       * PROCEDURE CONTROLLI_VERIFICA(P_ID_AZIENDA          IN DB_AZIENDA.ID_AZIENDA%TYPE,
       *                              P_ANNO_RIF            IN DB_UTILIZZO_PARTICELLA.ANNO%TYPE,
       *                              P_ID_GRUPPO_CONTROLLO IN DB_TIPO_GRUPPO_CONTROLLO.ID_GRUPPO_CONTROLLO%TYPE,
       *                              P_ID_UTENTE_LOGIN     IN ?.ID_UTENTE%TYPE,
       *                              P_PRESENZA_ANOM      OUT VARCHAR2,
       *                              P_MSGERR          IN OUT VARCHAR2,
       *                              P_CODERR          IN OUT VARCHAR2) IS
       */

      conn = getDatasource().getConnection();
      String sql = "{call PACK_DICHIARAZIONE_CONSISTENZA.CONTROLLI_VERIFICA(?,?,?,?,?,?,?)}";
      cs = conn.prepareCall(sql);
      cs.setLong(1, idAzienda.longValue());
      cs.setInt(2, anno.intValue());
      if (idGruppoControllo == null)
        cs.setBigDecimal(3, null);
      else
        cs.setInt(3, idGruppoControllo.intValue());
      cs.setLong(4, idUtente.longValue());
      cs.registerOutParameter(5, Types.VARCHAR);
      cs.registerOutParameter(6, Types.VARCHAR);
      cs.registerOutParameter(7, Types.VARCHAR);

      cs.executeUpdate();
      String msgErr = cs.getString(6);
      String errorCode = cs.getString(7);

      if (Validator.isNotEmpty(errorCode))
      {
        throw new SolmrException((String) AnagErrors.get("ERR_PLSQL2")
            + errorCode + " - " + msgErr);
      }

      return cs.getString(5);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    catch (SQLException exc)
    {
      char a = '"';
      String messaggioErrore = StringUtils.replace(exc.getMessage(), "'", "''");
      messaggioErrore = StringUtils.replace(exc.getMessage(), System
          .getProperty("line.separator"), "\\n");
      messaggioErrore = StringUtils.replace(exc.getMessage(),
          String.valueOf(a), " ");
      messaggioErrore = StringUtils.replace(messaggioErrore, "\n", "\\n");
      SolmrLogger.fatal(this, "SQLException in controlliVerificaPLSQL: "
          + messaggioErrore);
      throw new SolmrException((String) AnagErrors.get("ERR_PLSQL2")
          + " "
          + StringUtils.replace(messaggioErrore, System
              .getProperty("line.separator"), "\\n"));
    }

    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "controlliVerificaPLSQL - Generic Exception: "
          + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (cs != null)
          cs.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "controlliVerificaPLSQL - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "controlliVerificaPLSQL - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  /**
   * Metodo per inserire i dati su DB_UTILIZZO_DICHIARATO e su
   * DB_CONDUZIONE_DICHIARATA i dati dei terreni
   */
  public void inserimentoTerreniPLQSL(Long idAzienda, Integer anno,
      Long codiceFotografiaTerreni, String idUtente)
      throws DataAccessException, SolmrException
  {
    Connection conn = null;
    CallableStatement cs = null;
    try
    {
      SolmrLogger.debug(this, "inserimentoTerreniPLQSL start");
      /*************************************************************************
       * PROCEDURE SALVA_DICHIARAZIONE (P_ID_AZIENDA IN
       * DB_AZIENDA.ID_AZIENDA%TYPE, P_ANNO_RIF IN
       * DB_UTILIZZO_PARTICELLA.ANNO%TYPE, P_CODICE_FOTO IN
       * DB_DICHIARAZIONE_CONSISTENZA.CODICE_FOTOGRAFIA_TERRENI%TYPE,
       * P_ID_UTENTE_AGGIORNAMENTO IN
       * DB_CONDUZIONE_DICHIARATA.ID_UTENTE_AGGIORNAMENTO%TYPE, P_MSGERR IN OUT
       * VARCHAR2, P_CODERR IN OUT VARCHAR2);
       */
      SolmrLogger
          .debug(this, "inserimentoTerreniPLQSL idAzienda: " + idAzienda);
      SolmrLogger.debug(this, "inserimentoTerreniPLQSL anno: " + anno);
      SolmrLogger.debug(this,
          "inserimentoTerreniPLQSL codiceFotografiaTerreni: "
              + codiceFotografiaTerreni);
      SolmrLogger.debug(this, "inserimentoTerreniPLQSL idUtente: " + idUtente);

      conn = getDatasource().getConnection();
      String sql = "{call PACK_DICHIARAZIONE_CONSISTENZA.SALVA_DICHIARAZIONE(?,?,?,?,?,?)}";
      cs = conn.prepareCall(sql);
      cs.setLong(1, idAzienda.longValue());
      cs.setInt(2, anno.intValue());
      cs.setLong(3, codiceFotografiaTerreni.longValue());
      cs.setLong(4, Long.parseLong(idUtente));
      cs.registerOutParameter(5, Types.VARCHAR);
      cs.registerOutParameter(6, Types.VARCHAR);

      cs.executeUpdate();
      String msgErr = cs.getString(5);
      String errorCode = cs.getString(6);

      if (!(errorCode == null || "".equals(errorCode)))
      {
        char a = '"';
        String messaggioErrore = StringUtils.replace((String) AnagErrors
            .get("ERR_PLSQL")
            + errorCode + " - " + msgErr, "'", "''");
        messaggioErrore = StringUtils.replace(messaggioErrore, System
            .getProperty("line.separator"), "\\n");
        messaggioErrore = StringUtils.replace(messaggioErrore, String
            .valueOf(a), " ");
        SolmrLogger.error(this,
            "SQLException in inserimentoTerreniPLQSL in ConsistenzaDAO: "
                + messaggioErrore);
        throw new SolmrException(StringUtils.replace(messaggioErrore, System
            .getProperty("line.separator"), "\\n"));
      }
      SolmrLogger.debug(this, "inserimentoTerreniPLQSL end");
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    catch (SQLException sqle)
    {
      SolmrLogger.fatal(this, "inserimentoTerreniPLQSL - SQLException: "
          + sqle.getMessage());
      throw new DataAccessException(sqle.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "inserimentoTerreniPLQSL - Generic Exception: "
          + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (cs != null)
          cs.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "inserimentoTerreniPLQSL - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "inserimentoTerreniPLQSL - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per recuperare l'elenco degli errori o delle anomalie dovute ad
  // una dichiarazione di consistenza
  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichiarazioneConsistenza(Long idAzienda,Long fase, Long idMotivoDichiarazione)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ErrAnomaliaDicConsistenzaVO> elencoErrori = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT TGC.ID_GRUPPO_CONTROLLO,TGC.DESCRIZIONE AS DESC_GRUPPO,"
          + "TC.DESCRIZIONE AS TIPO_ANOMALIA_ERRORE,"
          + "DS.DESCRIZIONE AS DESC_ANOMALIA_ERRORE,DS.BLOCCANTE, "
          + "DCC.ID_DICHIARAZIONE_CORREZIONE,DS.ID_DICHIARAZIONE_SEGNALAZIONE, "
          + "DTC.DESCRIZIONE || ' ' || 'Rif:' || DCC.RIFERIMENTO_DOCUMENTO AS RISOLUZIONE, "
          + "TCF.FLAG_DOCUMENTO_GIUSTIFICATIVO, "
          + " DCC.ID_DOCUMENTO_PROTOCOLLATO "
          + "FROM DB_DICHIARAZIONE_SEGNALAZIONE DS, DB_TIPO_CONTROLLO TC,"
          + "DB_TIPO_GRUPPO_CONTROLLO TGC,DB_DICHIARAZIONE_CORREZIONE DCC,  "
          + "DB_TIPO_DOCUMENTO DTC,DB_TIPO_CONTROLLO_FASE TCF, "
          + "DB_STORICO_PARTICELLA SP,COMUNE C ";
      //Filtrare la visualizzazione per la id_fase legata al motivo dichiarazione selezionato 
      if (idMotivoDichiarazione!=null) query += ",DB_TIPO_MOTIVO_DICHIARAZIONE MD ";
          
      query += "WHERE DS.ID_AZIENDA = ? "
          + "AND NVL(DS.ID_DICHIARAZIONE_CONSISTENZA,-1)=NVL(DCC.ID_DICHIARAZIONE_CONSISTENZA(+),-1) "
          + "AND DS.ID_DICHIARAZIONE_CONSISTENZA IS NULL "
          + "AND DS.ID_CONTROLLO = TC.ID_CONTROLLO "
          + "AND TC.ID_GRUPPO_CONTROLLO = TGC.ID_GRUPPO_CONTROLLO "
          + "AND DS.ID_AZIENDA=DCC.ID_AZIENDA(+) "
          + "AND DS.ID_CONTROLLO=DCC.ID_CONTROLLO(+) "
          + "AND NVL(DS.ID_STORICO_PARTICELLA,-1)=NVL(DCC.ID_STORICO_PARTICELLA(+),-1) "
          + "AND DCC.EXT_ID_DOCUMENTO=DTC.ID_DOCUMENTO(+) "
          + "AND TC.ID_CONTROLLO=TCF.ID_CONTROLLO ";
          
          if (idMotivoDichiarazione!=null) query += " AND TCF.ID_FASE=MD.ID_FASE AND MD.ID_MOTIVO_DICHIARAZIONE=? ";
          else query +=" AND TCF.FASE= ? ";
      query += " AND DS.ID_STORICO_PARTICELLA=SP.ID_STORICO_PARTICELLA(+) "
          + "AND SP.COMUNE=C.ISTAT_COMUNE(+) "
          + "ORDER BY TGC.ID_GRUPPO_CONTROLLO,TC.ORDINAMENTO,C.DESCOM,SP.SEZIONE,SP.FOGLIO,SP.PARTICELLA,SP.SUBALTERNO ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing getErroriAnomalieDichiarazioneConsistenza: " + query);

      stmt.setLong(1, idAzienda.longValue());
      if (idMotivoDichiarazione!=null)
        stmt.setLong(2, idMotivoDichiarazione.longValue());
      else stmt.setLong(2, fase.longValue());

      ResultSet rs = stmt.executeQuery();

      elencoErrori = new Vector<ErrAnomaliaDicConsistenzaVO>();

      while (rs.next())
      {
        ErrAnomaliaDicConsistenzaVO err = new ErrAnomaliaDicConsistenzaVO();

        err.setIdDichiarazioneSegnalazione(rs
            .getString("ID_DICHIARAZIONE_SEGNALAZIONE"));
        err.setDescAnomaliaErrore(rs.getString("DESC_ANOMALIA_ERRORE"));
        err.setDescGruppoControllo(rs.getString("DESC_GRUPPO"));
        err.setIdGruppoControllo(rs.getString("ID_GRUPPO_CONTROLLO"));
        err.setTipoAnomaliaErrore(rs.getString("TIPO_ANOMALIA_ERRORE"));
        err.setBloccanteStr(rs.getString("BLOCCANTE"));

        // Controllo se è un errore bloccante o se è solo un'anomalia
        if ("S".equals(err.getBloccanteStr()))
        {
          err.setBloccante(true);
          err.setFlagDocumentoGiustificativo(rs
              .getString("FLAG_DOCUMENTO_GIUSTIFICATIVO"));
        }
        else
        {
          err.setBloccante(false);
        }

        err.setIdDichiarazioneCorrezione(rs
            .getString("ID_DICHIARAZIONE_CORREZIONE"));
        err.setRisoluzione(rs.getString("RISOLUZIONE"));
        if (Validator.isNotEmpty(rs.getString("ID_DOCUMENTO_PROTOCOLLATO")))
        {
          err.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO_PROTOCOLLATO")));
        }
        elencoErrori.add(err);
      }

      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getErroriAnomalieDichiarazioneConsistenza - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getErroriAnomalieDichiarazioneConsistenza - Generic Exception: "
              + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "getErroriAnomalieDichiarazioneConsistenza - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getErroriAnomalieDichiarazioneConsistenza - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoErrori;
  }
  
  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichConsPG(long idDichiarazioneConsistenza, long fase)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ErrAnomaliaDicConsistenzaVO> elencoErrori = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = 
        "WITH SEGNALAZIONE " +
        "  AS (SELECT DDS.ID_CONTROLLO, " +
        "             DDS.ID_DICHIARAZIONE_SEGNALAZIONE, " +
        "             C.DESCOM, " +
        "             SP.SEZIONE, " +
        "             SP.FOGLIO, " +
        "             SP.PARTICELLA, " +
        "             SP.SUBALTERNO, " +
        "             DDS.DESCRIZIONE AS DESC_ANOMALIA_ERRORE " +
        "      FROM   DB_DICHIARAZIONE_SEGNALAZIONE DDS, " +
        "             DB_STORICO_PARTICELLA SP, " +
        "             COMUNE C " +
        "      WHERE  DDS.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "      AND    DDS.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA(+) " +
        "      AND    SP.COMUNE = C.ISTAT_COMUNE(+)) " +
        "SELECT TGC.ID_GRUPPO_CONTROLLO, " +
        "       TC.ID_CONTROLLO, " +
        "       TGC.DESCRIZIONE AS DESC_GRUPPO, " +
        "       TC.DESCRIZIONE AS TIPO_ANOMALIA_ERRORE, " +
        "       TCF.BLOCCANTE, " +
        "       SE.DESC_ANOMALIA_ERRORE, " +
        "       SE.ID_DICHIARAZIONE_SEGNALAZIONE " +
        "FROM   DB_TIPO_CONTROLLO_FASE TCF, " +
        "       DB_TIPO_CONTROLLO TC, " +
        "       DB_TIPO_GRUPPO_CONTROLLO TGC, " +
        "       SEGNALAZIONE SE " +
        "WHERE  TCF.ID_FASE = ? " +
        "AND    TCF.ID_CONTROLLO = TC.ID_CONTROLLO " +
        "AND    TC.ID_GRUPPO_CONTROLLO = TGC.ID_GRUPPO_CONTROLLO " +
        "AND    TC.ID_CONTROLLO = SE.ID_CONTROLLO(+) " +
        "ORDER BY TGC.ID_GRUPPO_CONTROLLO, " +
        "         TC.ORDINAMENTO, " +
        "         SE.DESCOM, " +
        "         SE.SEZIONE, " +
        "         SE.FOGLIO, " +
        "         SE.PARTICELLA, " +
        "         SE.SUBALTERNO";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing getErroriAnomalieDichConsPG: " + query);

      stmt.setLong(1, idDichiarazioneConsistenza);
      stmt.setLong(2, fase);

      ResultSet rs = stmt.executeQuery();

      elencoErrori = new Vector<ErrAnomaliaDicConsistenzaVO>();

      while (rs.next())
      {
        ErrAnomaliaDicConsistenzaVO err = new ErrAnomaliaDicConsistenzaVO();

        err.setIdDichiarazioneSegnalazione(rs
            .getString("ID_DICHIARAZIONE_SEGNALAZIONE"));
        err.setDescAnomaliaErrore(rs.getString("DESC_ANOMALIA_ERRORE"));
        err.setDescGruppoControllo(rs.getString("DESC_GRUPPO"));
        err.setIdGruppoControllo(rs.getString("ID_GRUPPO_CONTROLLO"));
        err.setTipoAnomaliaErrore(rs.getString("TIPO_ANOMALIA_ERRORE"));
        err.setBloccanteStr(rs.getString("BLOCCANTE"));

        // Controllo se è un errore bloccante o se è solo un'anomalia
        if ("S".equals(err.getBloccanteStr())
          && Validator.isNotEmpty(err.getIdDichiarazioneSegnalazione()))
        {
          err.setBloccante(true);
        }
        else
        {
          err.setBloccante(false);
        }

        elencoErrori.add(err);
      }

      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getErroriAnomalieDichConsPG - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getErroriAnomalieDichConsPG - Generic Exception: "
              + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "getErroriAnomalieDichConsPG - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getErroriAnomalieDichConsPG - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoErrori;
  }


  // Metodo per recuperare l'elenco degli errori o delle anomalie dovute ad
  // una dichiarazione di consistenza relativamente ai terreni
  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichConsistenzaTerreni(Long idAzienda)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ErrAnomaliaDicConsistenzaVO> elencoErrori = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT TC.DESCRIZIONE AS TIPO_ANOMALIA_ERRORE,"
          + "DS.DESCRIZIONE AS DESC_ANOMALIA_ERRORE,DS.BLOCCANTE "
          + "FROM DB_DICHIARAZIONE_SEGNALAZIONE DS, DB_TIPO_CONTROLLO TC "
          + "WHERE DS.ID_AZIENDA = ? "
          + "AND DS.ID_DICHIARAZIONE_CONSISTENZA IS NULL "
          + "AND DS.ID_CONTROLLO = TC.ID_CONTROLLO "
          + "ORDER BY TC.ORDINAMENTO";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing getErroriAnomalieDichConsistenzaTerreni: " + query);

      stmt.setLong(1, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      elencoErrori = new Vector<ErrAnomaliaDicConsistenzaVO>();

      while (rs.next())
      {
        ErrAnomaliaDicConsistenzaVO err = new ErrAnomaliaDicConsistenzaVO();
        err.setDescAnomaliaErrore(rs.getString("DESC_ANOMALIA_ERRORE"));
        err.setTipoAnomaliaErrore(rs.getString("TIPO_ANOMALIA_ERRORE"));
        // Controllo se è un errore blocante o se è solo un'anomalia
        if ("S".equals(rs.getString("BLOCCANTE")))
          err.setBloccante(true);
        else
          err.setBloccante(false);
        elencoErrori.add(err);
      }

      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getErroriAnomalieDichConsistenzaTerreni - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getErroriAnomalieDichConsistenzaTerreni - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "getErroriAnomalieDichConsistenzaTerreni - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getErroriAnomalieDichConsistenzaTerreni - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoErrori;
  }

  // Metodo per verificare sui terreni se la data ultima modifica è maggiore
  // della data dell'ultima consistenza (Restituisce true se è maggiore,
  // false se è minore o uguale)
  public boolean verificaTerreni(Long idAzienda) throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    boolean ris = false;
    Date date1 = null;
    Date date2 = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT MAX(AC.DATA_AGGIORNAMENTO) AS DATA "
          + "FROM V_AGGIORNAMENTO_CONSISTENZA AC "
          + "WHERE AC.TIPO_AGGIORNAMENTO='T' " + "AND AC.ID_AZIENDA = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing controlloUltimeModifiche: " + query);
      if (idAzienda != null)
        SolmrLogger.debug(this, "idAzienda: " + idAzienda.longValue());

      stmt.setLong(1, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        date1 = rs.getTimestamp("DATA");
      }
      rs.close();
      stmt.close();

      query = "SELECT MAX(DC.DATA) AS DATA "
          + "FROM DB_DICHIARAZIONE_CONSISTENZA DC "
          + "WHERE DC.ID_AZIENDA = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing controlloUltimeModifiche: " + query);

      stmt.setLong(1, idAzienda.longValue());

      rs = stmt.executeQuery();

      if (rs.next())
      {
        date2 = rs.getTimestamp("DATA");
      }
      rs.close();
      stmt.close();
      if (date2 == null)
        ris = true;
      else
      {
        if (date1 != null)
          if (date1.after(date2))
            ris = true;
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "verificaTerreni - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "verificaTerreni - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this,
            "verificaTerreni - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "verificaTerreni - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return ris;
  }

  // Metodo per verificare se ci sono dichiarazioni di
  // consistenza fatte lo stesso giorno. Se il parametro restituito
  // è null significa che non ci sono dichiarazioni. Se è diverso da null
  // contiene l'id della dichiarazione di consistenza già fatta nel giorno
  public Long[] verificaConsistenzaStessoGiorno(Long idAzienda)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Long[] result = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT DC.ID_DICHIARAZIONE_CONSISTENZA,DC.CODICE_FOTOGRAFIA_TERRENI "
          + "FROM DB_DICHIARAZIONE_CONSISTENZA DC "
          + "WHERE ID_AZIENDA = ? "
          + "AND TRUNC(DC.DATA) = TRUNC(SYSDATE) "
          + " ORDER BY DC.DATA_INSERIMENTO_DICHIARAZIONE DESC";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing controlloUltimeModifiche: " + query);
      if (idAzienda != null)
        SolmrLogger.debug(this, "idAzienda: " + idAzienda.longValue());

      stmt.setLong(1, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        result = new Long[2];
        result[0] = new Long(rs.getLong("ID_DICHIARAZIONE_CONSISTENZA"));
        result[1] = new Long(rs.getLong("CODICE_FOTOGRAFIA_TERRENI"));
      }

      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger
          .fatal(this, "verificaConsistenzaStessoGiorno - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "verificaConsistenzaStessoGiorno - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "verificaConsistenzaStessoGiorno - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "verificaConsistenzaStessoGiorno - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  
  public void selectForUpdateDichiarazione(Long idAzienda)
    throws DataAccessException
  {
  
    Connection conn = null;
    PreparedStatement stmt = null;
    
    try
    {
      conn = getDatasource().getConnection();
    
      String query = " SELECT ID_DICHIARAZIONE_CONSISTENZA "
          + "FROM DB_DICHIARAZIONE_CONSISTENZA "
          + "WHERE ID_AZIENDA = ? "
          + "FOR UPDATE ";
    
      stmt = conn.prepareStatement(query);
    
      SolmrLogger.debug(this, "Executing selectForUpdatehiarazione: " + query);
      if (idAzienda != null)
        SolmrLogger.debug(this, "idAzienda: " + idAzienda.longValue());
    
      stmt.setLong(1, idAzienda.longValue());
    
      ResultSet rs = stmt.executeQuery();
    
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger
          .fatal(this, "selectForUpdatehiarazione - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "selectForUpdatehiarazione - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "selectForUpdatehiarazione - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "selectForUpdatehiarazione - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }
  

  // Questo metodo è identico a verificaConsistenzaStessoGiorno
  // L'unica differenza che non viene fatta una SELECT ... FOR UPDATE
  // Metodo per verificare se ci sono dichiarazioni di
  // consistenza fatte lo stesso giorno. Se il parametro restituito
  // è null significa che non ci sono dichiarazioni. Se è diverso da null
  // contiene l'id della dichiarazione di consistenza già fatta nel giorno
  public Long verificaConsistenzaStessoGiornoNoUpdate(Long idAzienda)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Long idDichiarazioneConsistenza = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT DC.ID_DICHIARAZIONE_CONSISTENZA "
          + "FROM DB_DICHIARAZIONE_CONSISTENZA DC " + "WHERE ID_AZIENDA = ? "
          + "AND TRUNC(DC.DATA) = TRUNC(SYSDATE) ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing controlloUltimeModifiche: " + query);

      stmt.setLong(1, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        idDichiarazioneConsistenza = new Long(rs
            .getLong("ID_DICHIARAZIONE_CONSISTENZA"));
      }
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "verificaConsistenzaStessoGiornoNoUpdate - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "verificaConsistenzaStessoGiornoNoUpdate - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "verificaConsistenzaStessoGiornoNoUpdate - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "verificaConsistenzaStessoGiornoNoUpdate - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return idDichiarazioneConsistenza;
  }

  // Metodo per avere un nuovo codice fotografia
  public Long getSeqCodiceFotografiaTerreni() throws DataAccessException
  {
    return getNextPrimaryKey((String) SolmrConstants
        .get("SEQ_CODICE_FOTOGRAFIA_TERRENI"));
  }

  // Metodo per recuperare il codice fotografia dell'ultima dichiarazione di
  // consistenza fatta dall'azienda
  public Long getLastCodiceFotografiaTerreni(Long idAzienda)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Long codiceFotografiaTerreni = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT DC.CODICE_FOTOGRAFIA_TERRENI "
          + "FROM DB_DICHIARAZIONE_CONSISTENZA DC "
          + "WHERE DC.ID_AZIENDA = ? " + "ORDER BY DC.DATA DESC ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing controlloUltimeModifiche: " + query);
      if (idAzienda != null)
        SolmrLogger.debug(this, "idAzienda: " + idAzienda.longValue());

      stmt.setLong(1, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        codiceFotografiaTerreni = new Long(rs
            .getLong("CODICE_FOTOGRAFIA_TERRENI"));
      }
      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getLastCodiceFotografiaTerreni - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getLastCodiceFotografiaTerreni - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "getLastCodiceFotografiaTerreni - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getLastCodiceFotografiaTerreni - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return codiceFotografiaTerreni;
  }

  // Metodo per effettuare l'inserimento di una dichiarazione di consistenza
  public Long insertDichiarazioneConsistenza(ConsistenzaVO consistenzaVO,
      Long idAzienda, Date dataCorrezioneDate) throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      primaryKey = getNextPrimaryKey((String) SolmrConstants
          .get("SEQ_DICHIARAZIONE_CONSISTENZA"));

      conn = getDatasource().getConnection();
      String query = "INSERT INTO DB_DICHIARAZIONE_CONSISTENZA "
          + "(ID_DICHIARAZIONE_CONSISTENZA, ANNO,"
          + "CODICE_FOTOGRAFIA_TERRENI,"
          + "ID_AZIENDA, ID_PROCEDIMENTO, ID_UTENTE,"
          + "ID_MOTIVO_DICHIARAZIONE, NOTE, TIPO_CONVALIDA, RESPONSABILE, DATA_RICHIESTA_STAMPA, "
          + "DATA, DATA_INSERIMENTO_DICHIARAZIONE, ANNO_CAMPAGNA, "
          + "DATA_PROTOCOLLO, NUMERO_PROTOCOLLO, FLAG_INVIO_MAIL) " + "VALUES"
          + "(?,?,?,?,?,?,?,?,'D',?, SYSDATE,";

      if (dataCorrezioneDate == null)
      {
        if (consistenzaVO.getNumeroProtocollo() == null)
          query += "SYSDATE,SYSDATE,?,?,?,?)";
        else
          query += "SYSDATE,SYSDATE,?,?,?,?)";
      }
      else
      {
        if (consistenzaVO.getNumeroProtocollo() == null)
          query += "?,SYSDATE,?,?,?,?)";
        else
          query += "?,SYSDATE,?,?,?,?)";
      }

      stmt = conn.prepareStatement(query);

      int idx = 0;
      
      stmt.setLong(++idx, primaryKey.longValue());
      stmt.setLong(++idx, Long.parseLong(consistenzaVO.getAnno()));
      stmt.setLong(++idx, consistenzaVO.getCodiceFotografiaTerreni().longValue());
      stmt.setLong(++idx, idAzienda.longValue());
      if (consistenzaVO.getIdProcedimento() == null)
        stmt.setBigDecimal(++idx, null);
      else
        stmt.setLong(++idx, Integer.parseInt(consistenzaVO.getIdProcedimento()));
      stmt.setLong(++idx, Long.parseLong(consistenzaVO.getIdUtente()));
      if (consistenzaVO.getIdMotivo() == null)
        stmt.setBigDecimal(++idx, null);
      else
        stmt.setLong(++idx, Integer.parseInt(consistenzaVO.getIdMotivo()));
      stmt.setString(++idx, consistenzaVO.getNote());
      stmt.setString(++idx, consistenzaVO.getResponsabile());
      
      if (dataCorrezioneDate != null)
      {
        stmt.setTimestamp(++idx, new Timestamp(dataCorrezioneDate.getTime()));
      }
      if (Validator.isNotEmpty(consistenzaVO.getAnnoCampagna()))
      {
        stmt.setString(++idx, consistenzaVO.getAnnoCampagna());
      }
      else
      {
        stmt.setString(++idx, null);
      }
      if (consistenzaVO.getDataProtocollo() != null)
      {
        stmt.setTimestamp(++idx, new java.sql.Timestamp(consistenzaVO
            .getDataProtocollo().getTime()));
      }
      else
      {
        stmt.setString(++idx, null);
      }
      stmt.setString(++idx, consistenzaVO.getNumeroProtocollo());
      stmt.setString(++idx, consistenzaVO.getFlagInvioMail());

      SolmrLogger.debug(this, "Executing insertDichiarazioneConsistenza"
          + query);

      stmt.executeUpdate();
      SolmrLogger.debug(this, "Executed insertDichiarazioneConsistenza");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in insertDichiarazioneConsistenza "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in insertDichiarazioneConsistenza: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in insertDichiarazioneConsistenza "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in insertDichiarazioneConsistenza "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }

  // Metodo per effettuare la modifica di una dichiarazione di consistenza
  public void updateDichiarazioneConsistenza(ConsistenzaVO consistenzaVO,
      Long idAzienda, Long idDichiarazioneConsistenza, boolean sysdate)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
      String query = 
        "UPDATE DB_DICHIARAZIONE_CONSISTENZA " +
        "SET ANNO = ?," +
        "    CODICE_FOTOGRAFIA_TERRENI = ?, " +
        "    ID_AZIENDA = ?, " +
        "    ID_UTENTE = ?, " +
        "    ID_MOTIVO_DICHIARAZIONE = ?, " +
        "    NOTE =?, " +
        "    TIPO_CONVALIDA = 'D', " +
        "    RESPONSABILE = ?, " +
        "    DATA_INSERIMENTO_DICHIARAZIONE = SYSDATE, " +
        "    NUMERO_PROTOCOLLO = ?, " +
        "    DATA_PROTOCOLLO = ?, " +
        "    FLAG_INVIO_MAIL = ? ";
      if (Validator.isNotEmpty(consistenzaVO.getAnnoCampagna()))
      {
        query += ",ANNO_CAMPAGNA = ? ";
      }

      if (sysdate)
        query += ",DATA = SYSDATE ";

      query += "WHERE ID_DICHIARAZIONE_CONSISTENZA = ?";

      stmt = conn.prepareStatement(query);

      int idx = 0;
      stmt.setLong(++idx, Long.parseLong(consistenzaVO.getAnno()));
      stmt.setLong(++idx, consistenzaVO.getCodiceFotografiaTerreni().longValue());
      stmt.setLong(++idx, idAzienda.longValue());
      stmt.setLong(++idx, Long.parseLong(consistenzaVO.getIdUtente()));
      if (consistenzaVO.getIdMotivo() == null)
        stmt.setBigDecimal(++idx, null);
      else
        stmt.setLong(++idx, Integer.parseInt(consistenzaVO.getIdMotivo()));
      stmt.setString(++idx, consistenzaVO.getNote());
      stmt.setString(++idx, consistenzaVO.getResponsabile());
      if (Validator.isNotEmpty(consistenzaVO.getNumeroProtocollo()))
      {
        stmt.setString(++idx, consistenzaVO.getNumeroProtocollo());
      }
      else
      {
        stmt.setString(++idx, null);
      }
      if (consistenzaVO.getDataProtocollo() != null)
      {
        stmt.setTimestamp(++idx, new Timestamp(consistenzaVO.getDataProtocollo()
            .getTime()));
      }
      else
      {
        stmt.setString(++idx, null);
      }
      
      stmt.setString(++idx, consistenzaVO.getFlagInvioMail());
      
      if (Validator.isNotEmpty(consistenzaVO.getAnnoCampagna()))
      {
        stmt.setString(++idx, consistenzaVO.getAnnoCampagna());
      }
      stmt.setLong(++idx, idDichiarazioneConsistenza.longValue());

      SolmrLogger.debug(this, "Executing updateDichiarazioneConsistenza"
          + query);

      stmt.executeUpdate();
      SolmrLogger.debug(this, "Executed updateDichiarazioneConsistenza");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in updateDichiarazioneConsistenza "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in updateDichiarazioneConsistenza: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in updateDichiarazioneConsistenza "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in updateDichiarazioneConsistenza "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per effettuare la modifica di una dichiarazione di consistenza
  public void updateDichiarazioneSegnalazione(Long idAzienda,
      Long idDichiarazioneConsistenza) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
      String query = "UPDATE DB_DICHIARAZIONE_SEGNALAZIONE "
          + "SET ID_DICHIARAZIONE_CONSISTENZA = ? " + "WHERE ID_AZIENDA = ? "
          + "AND ID_DICHIARAZIONE_CONSISTENZA IS NULL";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idDichiarazioneConsistenza.longValue());
      stmt.setLong(2, idAzienda.longValue());

      SolmrLogger.debug(this, "Executing updateDichiarazioneSegnalazione"
          + query);

      stmt.executeUpdate();
      SolmrLogger.debug(this, "Executed updateDichiarazioneSegnalazione");
    }
    catch (SQLException exc)
    {
      SolmrLogger
          .fatal(this, "SQLException in updateDichiarazioneSegnalazione "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in updateDichiarazioneSegnalazione: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in updateDichiarazioneSegnalazione "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in updateDichiarazioneSegnalazione "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  /**
   * Metodo per richiamare la procedura plsql che permette il salvataggio dei
   * dati di una dichiarazione:
   * 
   * 
   * ATTUALMENTE NON VIENE USATO
   * 
   * 
   */
  public String salvataggioDichiarazionePLQSL(ConsistenzaVO consistenzaVO,
      Long idAzienda, Integer anno) throws DataAccessException, SolmrException
  {
    Connection conn = null;
    CallableStatement cs = null;
    try
    {
      /*************************************************************************
       * PROCEDURE INSERIMENTO_TERRENI(P_ID_AZIENDA IN
       * DB_AZIENDA.ID_AZIENDA%TYPE, P_ANNO_RIF IN
       * DB_UTILIZZO_PARTICELLA.ANNO%TYPE, P_MOTIVO IN
       * DB_DICHIARAZIONE_CONSISTENZA.MOTIVO%TYPE, P_NOTE IN
       * DB_DICHIARAZIONE_CONSISTENZA.NOTE%TYPE, P_TIPO_CONVAL IN
       * DB_DICHIARAZIONE_CONSISTENZA.TIPO_CONVALIDA%TYPE, P_ID_PROC IN
       * DB_DICHIARAZIONE_CONSISTENZA.ID_PROCEDIMENTO%TYPE, P_ID_UTENTE IN
       * DB_DICHIARAZIONE_CONSISTENZA.ID_UTENTE%TYPE, P_MSGERR IN OUT VARCHAR2,
       * P_CODERR IN OUT VARCHAR2)
       * 
       */

      conn = getDatasource().getConnection();
      String sql = "{call PACK_DICHIARAZIONE_CONSISTENZA.INSERIMENTO_TERRENI(?,?,?,?,?,?,?,?,?)}";
      cs = conn.prepareCall(sql);
      cs.setLong(1, idAzienda.longValue()); // P_ID_AZIENDA
      cs.setInt(2, anno.intValue()); // P_ANNO_RIF
      cs.setString(3, consistenzaVO.getMotivo()); // P_MOTIVO
      cs.setString(4, consistenzaVO.getNote()); // P_NOTE
      cs.setString(5, ((String) SolmrConstants
          .get("TIPO_CONVALIDA_DICHIARAZIONE_CONSISTENZA"))); // P_TIPO_CONVAL
      cs.setInt(6, ((Integer) SolmrConstants.get("ID_PROCEDIMENTO_ANAG"))
          .intValue()); // P_ID_PROC
      cs.setInt(7, Integer.parseInt(consistenzaVO.getIdUtente())); // P_ID_UTENTE
      cs.registerOutParameter(8, Types.VARCHAR); // P_MSGERR
      cs.registerOutParameter(9, Types.VARCHAR); // P_CODERR

      cs.executeUpdate();
      String msgErr = cs.getString(8);
      String errorCode = cs.getString(9);

      if (!(errorCode == null || "".equals(errorCode)))
        throw new SolmrException((String) AnagErrors.get("ERR_PLSQL")
            + errorCode + " - " + msgErr);

      return cs.getString(3);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    catch (SQLException sqle)
    {
      SolmrLogger.fatal(this, "salvataggioDichiarazionePLQSL - SQLException: "
          + sqle.getMessage());
      throw new DataAccessException(sqle.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "salvataggioDichiarazionePLQSL - Generic Exception: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (cs != null)
          cs.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "salvataggioDichiarazionePLQSL - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "salvataggioDichiarazionePLQSL - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  /**
   * Metodo che si occupa di recuperare l'elenco delle validazioni effettuate su
   * una azienda
   * 
   * @param idAzienda
   * @return java.util.Vector
   * @throws DataAccessException
   */
  public Vector<ConsistenzaVO> getDichiarazioniConsistenza(Long idAzienda)
      throws DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating getDichiarazioniConsistenza method in ConsistenzaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ConsistenzaVO> elencoDichiarazioni = new Vector<ConsistenzaVO>();

    try
    {
      SolmrLogger.debug(this,
              "Creating db-connection in getDichiarazioniConsistenza method in ConsistenzaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this,
              "Created db-connection in getDichiarazioniConsistenza method in ConsistenzaDAO and it values: "
                  + conn + "\n");

      String query =
        "WITH INVIO AS " +
        "  (SELECT DC.ID_DICHIARAZIONE_CONSISTENZA, " + 
        "          IF.STATO_INVIO, " +
        "          IF.FLAG_DATI_ANAGRAFICI, " +
        "          IF.FLAG_TERRENI, " +
        "          IF.FLAG_FABBRICATI, " +
        "          IF.FLAG_CC, " +
        "          IF.FLAG_UV " +
        "   FROM   DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "          DB_INVIO_FASCICOLI IF " +
        "   WHERE  DC.ID_AZIENDA = ? " +
        "   AND    DC.ID_DICHIARAZIONE_CONSISTENZA = IF.ID_DICHIARAZIONE_CONSISTENZA " +
        "   AND    IF.DATA_AGGIORNAMENTO = (SELECT MAX(IF2.DATA_AGGIORNAMENTO) " +
        "                                   FROM DB_INVIO_FASCICOLI IF2 " +
        "                                   WHERE IF2.ID_DICHIARAZIONE_CONSISTENZA = IF.ID_DICHIARAZIONE_CONSISTENZA) " +
        "  ), " +
        "  ALLEGATO AS " +
        "  (SELECT AL.ID_ALLEGATO, " +
        "          AL.ID_TIPO_FIRMA, " +
        "          AL.EXT_ID_DOCUMENTO_INDEX, " +
        "          DC.ID_DICHIARAZIONE_CONSISTENZA, " +
        "          TF.STILE_FIRMA, " +
        "          TF.DESCRIZIONE_TIPO_FIRMA " +
        "   FROM   DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "          DB_ALLEGATO_DICHIARAZIONE AD, " +
        "          DB_ALLEGATO AL, " +
        "          DB_TIPO_FIRMA TF " +
        "   WHERE  DC.ID_AZIENDA = ? " +
        "   AND    DC.ID_DICHIARAZIONE_CONSISTENZA = AD.ID_DICHIARAZIONE_CONSISTENZA " +
        "   AND    AL.ID_TIPO_FIRMA = TF.ID_TIPO_FIRMA (+) " +
        "   AND    AD.DATA_FINE_VALIDITA IS NULL " +
        "   AND    AD.ID_ALLEGATO = AL.ID_ALLEGATO " +
        "   AND    AL.ID_TIPO_ALLEGATO = 10 ), " +
        "  PIANO_GRAFICO AS " +
        "  (SELECT APG.ID_DICHIARAZIONE_CONSISTENZA, " +
        "          APG.ID_ACCESSO_PIANO_GRAFICO " +
        "   FROM   DB_ACCESSO_PIANO_GRAFICO APG " +
        "   WHERE  APG.ID_AZIENDA = ? " +
        "   AND    APG.ID_ESITO_GRAFICO = 10 " +
        "   AND    APG.DATA_FINE_VALIDITA IS NULL) " +
        "SELECT DISTINCT " +
        "       DC.ID_DICHIARAZIONE_CONSISTENZA, " +
        "       DC.ANNO, " +
        "       TO_CHAR(DC.DATA,'DD/MM/YYYY HH24:MI:SS') AS DATA, " +
        "       DC.ID_MOTIVO_DICHIARAZIONE, " +
        "       DS.ID_DICHIARAZIONE_CONSISTENZA AS ANOMALIA, " +
        "       DC.DATA AS DATA_ORD, " +
        "       MD.DESCRIZIONE AS MOTIVO, " +
        "       TO_CHAR(DC.DATA_PROTOCOLLO,'DD/MM/YYYY HH24:MI:SS') AS DATA_PROTOCOLLO, " +
        "       DC.NUMERO_PROTOCOLLO, " +
        "       DC.DATA_AGGIORNAMENTO_FASCICOLO, " +
        "       DC.DATA_INSERIMENTO_DICHIARAZIONE, " +
        "       DC.FLAG_ANOMALIA, " +
        "       DC.DATA_AGGIORNAMENTO_UV, " +
        "       DC.FLAG_ANOMALIA_UV, " +
        "       DC.DATA_AGGIORNAMENTO_COLTURA, " +
        "       DC.FLAG_ANOMALIA_COLTURA, " +
        "       DC.DATA_AGGIORNAMENTO_FABBRICATI, " +
        "       DC.FLAG_ANOMALIA_FABBRICATI, " +
        "       DC.DATA_AGGIORNAMENTO_CC, " +
        "       DC.FLAG_ANOMALIA_CC, " +
        "       DC.DATA_RICHIESTA_STAMPA, " +
        "       IV.STATO_INVIO, " +
        "       IV.FLAG_DATI_ANAGRAFICI, " +
        "       IV.FLAG_TERRENI, " +
        "       IV.FLAG_FABBRICATI, " +
        "       IV.FLAG_CC, " +
        "       IV.FLAG_UV, " +
        "       AL1.ID_ALLEGATO, " +
        "       AL1.EXT_ID_DOCUMENTO_INDEX, " +
        "       AL1.ID_TIPO_FIRMA,  " +
        "       AL1.STILE_FIRMA, " +
        "       AL1.DESCRIZIONE_TIPO_FIRMA," +
        "       PG.ID_ACCESSO_PIANO_GRAFICO " +
        "FROM   DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "       DB_DICHIARAZIONE_SEGNALAZIONE DS, " +
        "       DB_TIPO_MOTIVO_DICHIARAZIONE MD, " +
        "       INVIO IV, " +
        "       ALLEGATO AL1, " +
        "       PIANO_GRAFICO PG " +
        "WHERE  DC.ID_AZIENDA = ? " +
        "AND    DC.ID_MOTIVO_DICHIARAZIONE = MD.ID_MOTIVO_DICHIARAZIONE(+) " +
        "AND    DC.ID_DICHIARAZIONE_CONSISTENZA = DS.ID_DICHIARAZIONE_CONSISTENZA(+) " +
        "AND    DC.ID_DICHIARAZIONE_CONSISTENZA = IV.ID_DICHIARAZIONE_CONSISTENZA (+) " +
        "AND    DC.ID_DICHIARAZIONE_CONSISTENZA = AL1.ID_DICHIARAZIONE_CONSISTENZA (+) " +
        "AND    DC.ID_DICHIARAZIONE_CONSISTENZA = PG.ID_DICHIARAZIONE_CONSISTENZA (+) " +
        "ORDER BY DC.ANNO DESC, " +
        "         DATA_ORD DESC ";

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [ID_AZIENDA] in getDichiarazioniConsistenza method in ConsistenzaDAO: "
                  + idAzienda + "\n");

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(this, "Executing getDichiarazioniConsistenza: " + query);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setLong(2, idAzienda.longValue());
      stmt.setLong(3, idAzienda.longValue());
      stmt.setLong(4, idAzienda.longValue());
      
      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        ConsistenzaVO cons = new ConsistenzaVO();
        cons.setIdDichiarazioneConsistenza(rs
            .getString("ID_DICHIARAZIONE_CONSISTENZA"));
        cons.setAnno(rs.getString("ANNO"));
        cons.setData(rs.getString("DATA"));
        cons.setIdMotivo(rs.getString("ID_MOTIVO_DICHIARAZIONE"));
        cons.setMotivo(rs.getString("MOTIVO"));
        cons.setDataInserimentoDichiarazione(rs
            .getTimestamp("DATA_INSERIMENTO_DICHIARAZIONE"));
        // Controllo se sono presenti anomalie
        if (rs.getString("ANOMALIA") == null)
        {
          cons.setAnomalie(false);
        }
        else
        {
          cons.setAnomalie(true);
        }
        cons.setDataProtocolloStr(rs.getString("DATA_PROTOCOLLO"));
        cons.setNumeroProtocollo(rs.getString("NUMERO_PROTOCOLLO"));
        cons.setDataAggiornamentoFascicolo(rs
            .getTimestamp("DATA_AGGIORNAMENTO_FASCICOLO"));
        cons.setFlagAnomalia(rs.getString("FLAG_ANOMALIA"));
        cons.setDataAggiornamentoUV(rs
            .getTimestamp("DATA_AGGIORNAMENTO_UV"));
        cons.setFlagAnomaliaUV(rs.getString("FLAG_ANOMALIA_UV"));
        cons.setDataAggiornamentoColtura(rs
            .getTimestamp("DATA_AGGIORNAMENTO_COLTURA"));
        cons.setFlagAnomaliaColtura(rs.getString("FLAG_ANOMALIA_COLTURA"));
        cons.setDataAggiornamentoFabbricati(rs
            .getTimestamp("DATA_AGGIORNAMENTO_FABBRICATI"));
        cons.setFlagAnomaliaFabbricati(rs.getString("FLAG_ANOMALIA_FABBRICATI"));
        cons.setDataAggiornamentoCC(rs
            .getTimestamp("DATA_AGGIORNAMENTO_CC"));
        cons.setFlagAnomaliaCC(rs.getString("FLAG_ANOMALIA_CC"));
        cons.setDataRichiestaStampa(rs.getTimestamp("DATA_RICHIESTA_STAMPA"));
        String statoInvio = rs.getString("STATO_INVIO");
        if(Validator.isNotEmpty(statoInvio))
        {
          InvioFascicoliVO invioFascicoliVO = new InvioFascicoliVO();
          invioFascicoliVO.setStatoInvio(statoInvio);
          invioFascicoliVO.setFlagDatiAnagrafici(rs.getString("FLAG_DATI_ANAGRAFICI"));
          invioFascicoliVO.setFlagTerreni(rs.getString("FLAG_TERRENI"));
          invioFascicoliVO.setFlagFabbricati(rs.getString("FLAG_FABBRICATI"));
          invioFascicoliVO.setFlagCc(rs.getString("FLAG_CC"));
          invioFascicoliVO.setFlagUv(rs.getString("FLAG_UV"));
          cons.setInvioFascicoliVO(invioFascicoliVO);
        }
        cons.setIdAllegato(checkLongNull(rs.getString("ID_ALLEGATO")));
        cons.setIdTipoFirma(checkLongNull(rs.getString("ID_TIPO_FIRMA")));
        cons.setExtIdDocumentoIndex(checkLongNull(rs.getString("EXT_ID_DOCUMENTO_INDEX")));
        cons.setStileFirma(rs.getString("STILE_FIRMA"));
        cons.setDescrizioneTipoFirma(rs.getString("DESCRIZIONE_TIPO_FIRMA"));
        
        //piano grafico effettuato con successo!!!
        cons.setIdAccessoPianoGrafico(checkLongNull(rs.getString("ID_ACCESSO_PIANO_GRAFICO")));
        
        elencoDichiarazioni.add(cons);
      }

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "getDichiarazioniConsistenza - SQLException: "
          + exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "getDichiarazioniConsistenza - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .error(
                this,
                "getDichiarazioniConsistenza - SQLException while closing Statement and Connection: "
                    + exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getDichiarazioniConsistenza - Generic Exception while closing Statement and Connection: "
                    + ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this,
        "Invocated getDichiarazioniConsistenza method in ConsistenzaDAO\n");
    return elencoDichiarazioni;
  }

  // Metodo per recuperare l'elenco minimo delle dichiarazione di consistenza di
  // un azienda
  public Vector<ConsistenzaVO> getDichiarazioniConsistenzaMinimo(Long idAzienda)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ConsistenzaVO> elencoDichiarazioni = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT DISTINCT DC.ID_DICHIARAZIONE_CONSISTENZA, DC.ANNO, "
          + "TO_CHAR(DC.DATA,'DD/MM/YYYY HH24:MI:SS') AS CHAR_DATA, "
          + "DC.DATA "
          + "FROM DB_DICHIARAZIONE_CONSISTENZA DC "
          + "WHERE DC.ID_AZIENDA = ? " + "ORDER BY DC.ANNO DESC, DC.DATA DESC ";

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(this, "Executing getDichiarazioniConsistenza: " + query);

      stmt.setLong(1, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      elencoDichiarazioni = new Vector<ConsistenzaVO>();

      while (rs.next())
      {
        ConsistenzaVO cons = new ConsistenzaVO();
        cons.setIdDichiarazioneConsistenza(rs
            .getString("ID_DICHIARAZIONE_CONSISTENZA"));
        cons.setAnno(rs.getString("ANNO"));
        cons.setData(rs.getString("CHAR_DATA"));
        elencoDichiarazioni.add(cons);
      }

      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getDichiarazioniConsistenza - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getDichiarazioniConsistenza - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "getDichiarazioniConsistenza - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getDichiarazioniConsistenza - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoDichiarazioni;
  }

  // Metodo per recuperare il dettaglio di una dichiarazione di consistenza dato
  // un idDichiarazioneConsistenza
  public ConsistenzaVO getDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza) throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    ConsistenzaVO cons = new ConsistenzaVO();

    try
    {
      conn = getDatasource().getConnection();

      String query = 
        "SELECT DC.ID_DICHIARAZIONE_CONSISTENZA, " +
        "       DC.ANNO, " +
        "       TO_CHAR(DC.DATA,'DD/MM/YYYY HH24:MI:SS') AS DATA, " +
        "       DC.ID_MOTIVO_DICHIARAZIONE, " +
        "       DC.NOTE, " + 
        "       (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
        "          || ' ' " + 
        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
        "         WHERE DC.ID_UTENTE = PVU.ID_UTENTE_LOGIN) " +  
        "         || ' ( ' " +
        "         || (SELECT PVU.DENOMINAZIONE " +
        "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
        "        WHERE DC.ID_UTENTE = PVU.ID_UTENTE_LOGIN) || ' ) ' " +
        "       AS UTENTE_AGGIORNAMENTO, " +
        "       MD.DESCRIZIONE AS MOTIVO, " +
        "       DC.RESPONSABILE, DC.ID_UTENTE AS ID_UTENTE, " +
        "       TO_CHAR(DC.DATA_PROTOCOLLO,'DD/MM/YYYY HH24:MI:SS') AS DATA_PROTOCOLLO, " +
        "       DC.NUMERO_PROTOCOLLO, " +
        "       DC.DATA_INSERIMENTO_DICHIARAZIONE, " +
        "       DC.CODICE_FOTOGRAFIA_TERRENI, " +
        "       DC.ANNO_CAMPAGNA " +
        "FROM   DB_DICHIARAZIONE_CONSISTENZA DC, " +
        //"       PAPUA_V_UTENTE_LOGIN PVU, " + 
        "       DB_TIPO_MOTIVO_DICHIARAZIONE MD " +
        "WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "AND DC.ID_MOTIVO_DICHIARAZIONE = MD.ID_MOTIVO_DICHIARAZIONE(+) ";
        //"AND DC.ID_UTENTE=PVU.ID_UTENTE_LOGIN ";

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(this, "Executing getDichiarazioneConsistenza: " + query);

      stmt.setLong(1, idDichiarazioneConsistenza.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        cons.setIdDichiarazioneConsistenza(rs
            .getString("ID_DICHIARAZIONE_CONSISTENZA"));
        cons.setAnno(rs.getString("ANNO"));
        cons.setData(rs.getString("DATA"));
        cons.setIdMotivo(rs.getString("ID_MOTIVO_DICHIARAZIONE"));
        cons.setMotivo(rs.getString("MOTIVO"));
        cons.setNote(rs.getString("NOTE"));
        cons.setUtenteAggiornamento(rs.getString("UTENTE_AGGIORNAMENTO"));
        cons.setResponsabile(rs.getString("RESPONSABILE"));
        cons.setIdUtente(rs.getString("ID_UTENTE"));
        cons.setDataProtocolloStr(rs.getString("DATA_PROTOCOLLO"));
        cons.setNumeroProtocollo(rs.getString("NUMERO_PROTOCOLLO"));
        cons.setDataInserimentoDichiarazione(rs.getTimestamp(
            "DATA_INSERIMENTO_DICHIARAZIONE"));
        cons.setCodiceFotografiaTerreni(checkLong(rs.getString("CODICE_FOTOGRAFIA_TERRENI")));
        cons.setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
      }

      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getDichiarazioneConsistenza - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getDichiarazioneConsistenza - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "getDichiarazioneConsistenza - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getDichiarazioneConsistenza - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return cons;
  }

  public ConsistenzaVO getUltimaDichiarazioneConsistenza(Long idAzienda)
      throws DataAccessException
  {
    return getUltimaDichiarazioneConsistenza(idAzienda, null);
  }

  public ConsistenzaVO getUltimaDichiarazioneConsistenza(Long idAzienda,
      java.util.Date dataAl) throws DataAccessException
  {
    return getUltimaDichiarazioneConsistenza(idAzienda, dataAl, null, false,
        false);
  }

  public ConsistenzaVO getUltimaDichiarazioneConsistenza(Long idAzienda,
      java.util.Date dataAl, Long idProcedimento,
      boolean escludiDichParticolari, boolean dichiarazioniAttive)

  throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    ConsistenzaVO cons = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT "
          + "   DC.ID_DICHIARAZIONE_CONSISTENZA, "
          + "   DC.ID_AZIENDA, "
          + "   DC.ANNO, "
          + "   TO_CHAR(DC.DATA,'dd/MM/yyyy HH24.mi.ss') AS DATA, "
          + "   MD.DESCRIZIONE AS MOTIVO, "
          + "   DC.ID_MOTIVO_DICHIARAZIONE, "
          + "   DC.NOTE, "
          + "   DC.TIPO_CONVALIDA, "
          + "   DC.CODICE_FOTOGRAFIA_TERRENI, "
          + "   DC.ID_PROCEDIMENTO, "
          + "   DC.ID_UTENTE, "
          + "   (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
          "          || ' ' " + 
          "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
          "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
          "         WHERE DC.ID_UTENTE = PVU.ID_UTENTE_LOGIN) " +  
          "         || ' ( ' || " +
          "       (SELECT PVU.DENOMINAZIONE " +
          "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
          "        WHERE DC.ID_UTENTE = PVU.ID_UTENTE_LOGIN) || ' ) ' AS UTENTE_AGGIORNAMENTO, "
          + "   TP.DESCRIZIONE AS DESCRIZIONE_TIPO_PROCEDIMENTO, "
          + "   DC.RESPONSABILE, "
          + "   DC.DATA_AGGIORNAMENTO_FASCICOLO, "
          + "   DC.CUAA_VALIDATO, "
          + "   DC.FLAG_ANOMALIA, "
          + "   DC.DATA_INSERIMENTO_DICHIARAZIONE, "
          + "   DC.FLAG_INVIA_USO, "
          + "   DC.ANNO_CAMPAGNA, "
          + "   DC.NUMERO_PROTOCOLLO, "
          + "   DC.DATA_PROTOCOLLO, "
          + "   MD.ID_MOTIVO_DICHIARAZIONE, "
          + "   MD.DESCRIZIONE, "
          + "   MD.DATA_INIZIO_VALIDITA, "
          + "   MD.DATA_FINE_VALIDITA, "
          + "   MD.ANNO_CAMPAGNA, "
          + "   MD.ID_FASE, "
          + "   MD.GTFO, "
          + "   MD.TIPO_DICHIARAZIONE "
          + " FROM "
          + "   DB_DICHIARAZIONE_CONSISTENZA DC, "
          //+ "   PAPUA_V_UTENTE_LOGIN PVU, "
          + "   DB_TIPO_PROCEDIMENTO TP, "
          + "   DB_TIPO_MOTIVO_DICHIARAZIONE MD "
          + " WHERE TP.ID_PROCEDIMENTO(+)=DC.ID_PROCEDIMENTO "
          + "   AND DC.ID_MOTIVO_DICHIARAZIONE = MD.ID_MOTIVO_DICHIARAZIONE(+) "
          + "   AND DC.ID_AZIENDA = ? ";
      if (dataAl != null)
      {
        query += "   AND DC.DATA <= ? ";
      }

      if (escludiDichParticolari)
      {
        query += "   AND NVL(DC.ID_MOTIVO_DICHIARAZIONE,-1) NOT IN " + "   ( "
            + "     SELECT ID_MOTIVO_DICHIARAZIONE "
            + "     FROM DB_MOTIVO_ESCLUSO_PROCEDIMENTO "
            + "     WHERE ID_PROCEDIMENTO = ? " + "   ) ";
      }

      if (dichiarazioniAttive)
        query += "   AND MD.DATA_FINE_VALIDITA IS NULL ";

      query += " ORDER BY DC.DATA DESC";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing getUltimaDichiarazioneConsistenza: "
          + query);

      SolmrLogger.debug(this, "Param idAzienda=" + idAzienda);
      SolmrLogger.debug(this, "Param dataAl=" + dataAl);
      SolmrLogger.debug(this, "Param idProcedimento=" + idProcedimento);
      SolmrLogger.debug(this, "Param escludiDichParticolari="
          + escludiDichParticolari);
      SolmrLogger.debug(this, "Param dichiarazioniAttive="
          + dichiarazioniAttive);

      int indice = 1;
      stmt.setLong(indice++, idAzienda.longValue());
      if (dataAl != null)
      {
        stmt.setTimestamp(indice++, convertDateToTimestamp(dataAl));
      }
      if (escludiDichParticolari)
        stmt.setLong(indice++, idProcedimento.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        cons = new ConsistenzaVO();
        cons.setIdDichiarazioneConsistenza(rs
            .getString("ID_DICHIARAZIONE_CONSISTENZA"));
        cons.setIdAzienda(new Long(rs.getString("ID_AZIENDA")));
        cons.setAnno(rs.getString("ANNO"));
        cons.setData(rs.getString("DATA"));
        cons.setMotivo(rs.getString("MOTIVO"));
        cons.setIdMotivo(rs.getString("ID_MOTIVO_DICHIARAZIONE"));
        cons.setNote(rs.getString("NOTE"));
        cons.setCodiceFotografiaTerreni(new Long(rs
            .getLong("CODICE_FOTOGRAFIA_TERRENI")));
        cons.setIdProcedimento(rs.getString("ID_PROCEDIMENTO"));
        cons.setIdUtente(rs.getString("ID_UTENTE"));
        cons.setTipoConvalida(rs.getString("TIPO_CONVALIDA"));
        cons.setUtenteAggiornamento(rs.getString("UTENTE_AGGIORNAMENTO"));
        cons.setResponsabile(rs.getString("RESPONSABILE"));
        String descUtenteAggiornamento = cons.getUtenteAggiornamento();
        if (descUtenteAggiornamento != null
            && descUtenteAggiornamento.trim().equals("(  )"))
        {
          cons.setUtenteAggiornamento(null);
        }
        cons.setDescProcedimento(rs.getString("DESCRIZIONE_TIPO_PROCEDIMENTO"));

        // Aggiunti su richiesta di Ernesto SMRANAGS-39
        // serviceGetUltimaDichiarazioneConsistenza
        cons.setDataAggiornamentoFascicolo(rs
            .getDate("DATA_AGGIORNAMENTO_FASCICOLO"));
        cons.setCuaaValidato(rs.getString("CUAA_VALIDATO"));
        cons.setFlagAnomalia(rs.getString("FLAG_ANOMALIA"));
        cons.setDataInserimentoDichiarazione(rs
            .getDate("DATA_INSERIMENTO_DICHIARAZIONE"));
        cons.setFlagInviaUso(rs.getString("FLAG_INVIA_USO"));
        cons.setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
        cons.setNumeroProtocollo(rs.getString("NUMERO_PROTOCOLLO"));
        cons.setDataProtocollo(rs.getTimestamp("DATA_PROTOCOLLO"));

        /*
         * TipoMotivoDichiarazioneVO tipoMotivoDichiarazioneVO = new
         * TipoMotivoDichiarazioneVO();
         * tipoMotivoDichiarazioneVO.setIdMotivoDichiarazione(new
         * Long(rs.getLong("ID_MOTIVO_DICHIARAZIONE")));
         * tipoMotivoDichiarazioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
         * tipoMotivoDichiarazioneVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
         * tipoMotivoDichiarazioneVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
         * tipoMotivoDichiarazioneVO.setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
         * tipoMotivoDichiarazioneVO.setIdFase(new Long(rs.getLong("ID_FASE")));
         * tipoMotivoDichiarazioneVO.setGtfo(rs.getString("GTFO"));
         * tipoMotivoDichiarazioneVO.setTipoDichiarazione(rs.getString("TIPO_DICHIARAZIONE"));
         * cons.setTipoMotivoDichiarazioneVO(tipoMotivoDichiarazioneVO);
         */

      }

      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getUltimaDichiarazioneConsistenza - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getUltimaDichiarazioneConsistenza - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "getUltimaDichiarazioneConsistenza - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getUltimaDichiarazioneConsistenza - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return cons;
  }


  /**
   * Non posso usare il servizio getDichiarazione di consistenza in quanto
   * utilizza un formato di data diverso da quello richiesto per i servizi.
   * 
   * @param idDichiarazioneConsistenza
   * @return
   * @throws DataAccessException
   */
  public ConsistenzaVO serviceGetDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza) throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    ConsistenzaVO cons = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT "
          + "   DC.ID_DICHIARAZIONE_CONSISTENZA, "
          + "   DC.ID_AZIENDA, "
          + "   DC.ANNO, "
          + "   TO_CHAR(DC.DATA,'dd/MM/yyyy HH24.mi.ss') AS DATA, "
          + "   DC.ID_MOTIVO_DICHIARAZIONE, "
          + "   MD.DESCRIZIONE AS MOTIVO, "
          + "   DC.NOTE, "
          + "   DC.TIPO_CONVALIDA, "
          + "   DC.CODICE_FOTOGRAFIA_TERRENI, "
          + "   DC.ID_PROCEDIMENTO, "
          + "   DC.ID_UTENTE, "
          + "   (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
          "          || ' ' " + 
          "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
          "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
          "         WHERE DC.ID_UTENTE = PVU.ID_UTENTE_LOGIN) " + 
          "     || ' ( ' " +
          "     || (SELECT PVU.DENOMINAZIONE " +
          "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
          "        WHERE DC.ID_UTENTE = PVU.ID_UTENTE_LOGIN) || ' ) ' AS UTENTE_AGGIORNAMENTO, "
          + "   TP.DESCRIZIONE AS DESCRIZIONE_TIPO_PROCEDIMENTO, "
          + "   DC.RESPONSABILE, "
          + "   DC.DATA_AGGIORNAMENTO_FASCICOLO, "
          + "   DC.CUAA_VALIDATO, "
          + "   DC.FLAG_ANOMALIA, "
          + "   DC.DATA_INSERIMENTO_DICHIARAZIONE, "
          + "   DC.FLAG_INVIA_USO, "
          + "   DC.ANNO_CAMPAGNA, "
          + "   DC.NUMERO_PROTOCOLLO, "
          + "   DC.DATA_PROTOCOLLO, "
          + "   MD.ID_MOTIVO_DICHIARAZIONE, "
          + "   MD.DESCRIZIONE, "
          + "   MD.DATA_INIZIO_VALIDITA, "
          + "   MD.DATA_FINE_VALIDITA, "
          + "   MD.ANNO_CAMPAGNA, "
          + "   MD.ID_FASE, "
          + "   MD.GTFO, "
          + "   MD.TIPO_DICHIARAZIONE "
          + " FROM "
          + "   DB_DICHIARAZIONE_CONSISTENZA DC, "
          //+ "   PAPUA_V_UTENTE_LOGIN PVU, "
          + "   DB_TIPO_PROCEDIMENTO TP, "
          + "   DB_TIPO_MOTIVO_DICHIARAZIONE MD "
          + " WHERE "
          + "   DC.ID_DICHIARAZIONE_CONSISTENZA = ? "
          + "   AND DC.ID_MOTIVO_DICHIARAZIONE = MD.ID_MOTIVO_DICHIARAZIONE(+) "
          //+ "   AND DC.ID_UTENTE = PVU.ID_UTENTE_LOGIN "
          + "   AND TP.ID_PROCEDIMENTO(+) = DC.ID_PROCEDIMENTO ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing serviceGetDichiarazioneConsistenza: "
          + query);
      SolmrLogger.debug(this, "idDichiarazioneConsistenza: "
          + idDichiarazioneConsistenza);
      stmt.setLong(1, idDichiarazioneConsistenza.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        cons = new ConsistenzaVO();
        cons.setIdDichiarazioneConsistenza(rs
            .getString("ID_DICHIARAZIONE_CONSISTENZA"));
        cons.setIdAzienda(new Long(rs.getString("ID_AZIENDA")));
        cons.setAnno(rs.getString("ANNO"));
        cons.setData(rs.getString("DATA"));
        cons.setMotivo(rs.getString("MOTIVO"));
        cons.setIdMotivo(rs.getString("ID_MOTIVO_DICHIARAZIONE"));
        cons.setNote(rs.getString("NOTE"));
        cons.setCodiceFotografiaTerreni(new Long(rs
            .getLong("CODICE_FOTOGRAFIA_TERRENI")));
        cons.setIdProcedimento(rs.getString("ID_PROCEDIMENTO"));
        cons.setIdUtente(rs.getString("ID_UTENTE"));
        cons.setTipoConvalida(rs.getString("TIPO_CONVALIDA"));
        cons.setUtenteAggiornamento(rs.getString("UTENTE_AGGIORNAMENTO"));
        cons.setResponsabile(rs.getString("RESPONSABILE"));
        String descUtenteAggiornamento = cons.getUtenteAggiornamento();
        if (descUtenteAggiornamento != null
            && descUtenteAggiornamento.trim().equals("(  )"))
        {
          cons.setUtenteAggiornamento(null);
        }
        cons.setDescProcedimento(rs.getString("DESCRIZIONE_TIPO_PROCEDIMENTO"));

        // Aggiunti su richiesta di Ernesto SMRANAGS-37
        // serviceGetDichiarazioneConsistenza
        cons.setDataAggiornamentoFascicolo(rs
            .getDate("DATA_AGGIORNAMENTO_FASCICOLO"));
        cons.setCuaaValidato(rs.getString("CUAA_VALIDATO"));
        cons.setFlagAnomalia(rs.getString("FLAG_ANOMALIA"));
        cons.setDataInserimentoDichiarazione(rs
            .getDate("DATA_INSERIMENTO_DICHIARAZIONE"));
        cons.setFlagInviaUso(rs.getString("FLAG_INVIA_USO"));
        cons.setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
        cons.setNumeroProtocollo(rs.getString("NUMERO_PROTOCOLLO"));
        cons.setDataProtocollo(rs.getTimestamp("DATA_PROTOCOLLO"));

        /*
         * TipoMotivoDichiarazioneVO tipoMotivoDichiarazioneVO = new
         * TipoMotivoDichiarazioneVO();
         * tipoMotivoDichiarazioneVO.setIdMotivoDichiarazione(new
         * Long(rs.getLong("ID_MOTIVO_DICHIARAZIONE")));
         * tipoMotivoDichiarazioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
         * tipoMotivoDichiarazioneVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
         * tipoMotivoDichiarazioneVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
         * tipoMotivoDichiarazioneVO.setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
         * tipoMotivoDichiarazioneVO.setIdFase(new Long(rs.getLong("ID_FASE")));
         * tipoMotivoDichiarazioneVO.setGtfo(rs.getString("GTFO"));
         * tipoMotivoDichiarazioneVO.setTipoDichiarazione(rs.getString("TIPO_DICHIARAZIONE"));
         * cons.setTipoMotivoDichiarazioneVO(tipoMotivoDichiarazioneVO);
         */

      }

      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "serviceGetDichiarazioneConsistenza - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "serviceGetDichiarazioneConsistenza - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "serviceGetDichiarazioneConsistenza - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "serviceGetDichiarazioneConsistenza - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return cons;
  }

  /**
   * Metodo per richiamare la procedura plsql per i controlli delle pratiche che
   * hanno utilizzato quella dichiarazione di consistenza
   * 
   * @param idAzienda:
   *          idAzienda selezionata
   * @param idUtente:
   *          Idutente connesso
   * @param idDichiarazioneConsistenza:
   *          idDichiarazioneConsistenza
   * @return l'elenco delle pratiche che hanno utilizzato la dichiarazione di
   *         consistenza
   * @throws DataAccessException
   * @throws SolmrException
   */
  public Vector<TemporaneaPraticaAziendaVO> aggiornaPraticaAziendaPLQSL(Long idAzienda, Long idUtente,
      Long idDichiarazioneConsistenza) throws DataAccessException,
      SolmrException
  {
    Connection conn = null;
    CallableStatement cs = null;
    PreparedStatement stmt = null;
    Vector<TemporaneaPraticaAziendaVO> result = null;
    try
    {
      /*************************************************************************
       * PROCEDURE AGGIORNA_PRATICA_AZIENDA(P_ID_AZIENDA IN
       * DB_AZIENDA.ID_AZIENDA%TYPE, P_MSGERR IN OUT VARCHAR2, P_CODERR IN OUT
       * VARCHAR2);
       */
      conn = getDatasource().getConnection();
      String sql = "{call PACK_PRATICA_AZIENDA.AGGIORNA_PRATICA_AZIENDA(?,?,?)}";
      cs = conn.prepareCall(sql);

      SolmrLogger.debug(this, "Executing aggiornaPraticaAziendaPLQSL: PLSQL");
      SolmrLogger.debug(this, "idAzienda: " + idAzienda);
      SolmrLogger.debug(this, "idUtente: " + idUtente);

      cs.setLong(1, idAzienda.longValue());
      cs.registerOutParameter(2, Types.VARCHAR);
      cs.registerOutParameter(3, Types.VARCHAR);
      cs.executeUpdate();
      String msgErr = cs.getString(2);
      if (!(msgErr == null || "".equals(msgErr)))
        throw new SolmrException((String) AnagErrors
            .get("ERR_PLSQL_PRATICA_AZIENDA")
            + " " + msgErr);

      // se non si sono verificati errori fare la select su
      // DB_PROCEDIMENTO_AZIENDA
      // per id_dichiarazione_consistenza in questione, id_utente connesso,
      // id_azienda selezionata
      String query = "SELECT TPA.ID_PROCEDIMENTO_AZIENDA, "
          + "       TPA.ID_AZIENDA, TPA.ID_PROCEDIMENTO, TPA.NUMERO_PRATICA,"
          + "       TPA.DESCRIZIONE, TPA.STATO, TPA.DESCRIZIONE_STATO,"
          + "       TO_CHAR(TPA.DATA_VALIDITA_STATO,'dd/mm/yyyy') AS DATA_VALIDITA_STATO, "
          + "       TPA.ID_DICHIARAZIONE_CONSISTENZA,"
          + "       TPA.FLAG_CESSAZIONE_AZ_AMMESSA,"
          + "       TP.DESCRIZIONE_ESTESA AS DESC_PROCEDIMENTO, "
          + "       TPA.EXT_ID_AMM_COMPETENZA "
          + "FROM DB_PROCEDIMENTO_AZIENDA TPA,DB_TIPO_PROCEDIMENTO TP "
          + "WHERE TP.ID_PROCEDIMENTO=TPA.ID_PROCEDIMENTO "
          + "  AND TPA.ID_DICHIARAZIONE_CONSISTENZA = ?"
          + "  AND TPA.ID_AZIENDA = ?"
          + "ORDER BY DESC_PROCEDIMENTO,TPA.DATA_VALIDITA_STATO DESC";

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(this, "Executing aggiornaPraticaAziendaPLQSL: " + query);
      SolmrLogger.debug(this, "idDichiarazioneConsistenza: "
          + idDichiarazioneConsistenza);
      stmt.setLong(1, idDichiarazioneConsistenza.longValue());
      stmt.setLong(2, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      result = new Vector<TemporaneaPraticaAziendaVO>();

      while (rs.next())
      {
        TemporaneaPraticaAziendaVO temp = new TemporaneaPraticaAziendaVO();
        temp.setDescProcedimento(rs.getString("DESC_PROCEDIMENTO"));
        temp.setIdProcedimento(rs.getString("ID_PROCEDIMENTO"));
        temp.setDescrizione(rs.getString("DESCRIZIONE"));
        temp.setNumeroPratica(rs.getString("NUMERO_PRATICA"));
        temp.setDescrizioneStato(rs.getString("DESCRIZIONE_STATO"));
        temp.setDataValiditaStato(rs.getString("DATA_VALIDITA_STATO"));
        if (Validator.isNotEmpty(rs.getString("EXT_ID_AMM_COMPETENZA")))
        {
          temp.setExtIdAmmCompetenza(new Long(rs
              .getLong("EXT_ID_AMM_COMPETENZA")));
        }
        result.add(temp);
      }
      rs.close();
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    catch (SQLException sqle)
    {
      SolmrLogger.fatal(this, "aggiornaPraticaAziendaPLQSL - SQLException: "
          + sqle.getMessage());
      throw new DataAccessException(sqle.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(this, "aggiornaPraticaAziendaPLQSL - Generic Exception: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (cs != null)
          cs.close();
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "aggiornaPraticaAziendaPLQSL - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "aggiornaPraticaAziendaPLQSL - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  /**
   * Metodo per richiamare la procedura plsql che controlla se posso proseguire
   * con una dihiarazione di consistenza o no: viene richiamato quando si
   * riscontra che ho già effettuato una dichiarazione nel giorno
   * 
   * @param idAzienda:
   *          idAzienda selezionata
   * @param idUtente:
   *          Idutente connesso
   * @param idDichiarazioneConsistenza:
   *          idDichiarazioneConsistenza
   * @param operazione:
   *          tipo di operazione per cui viene chiamato il PLSql: - "U": update -
   *          "D": delete
   * @return true se posso proseguire e false altrimenti
   * @throws DataAccessException
   * @throws SolmrException
   */
  public boolean operazioneAmmessaPLQSL(Long idAzienda, Long idUtente,
      Long idDichiarazioneConsistenza, String operazione)
      throws DataAccessException, SolmrException
  {
    Connection conn = null;
    CallableStatement cs = null;
    boolean result = false;
    try
    {
      /*************************************************************************
       * PROCEDURE OPERAZIONE_AMMESSA(P_ID_AZIENDA
       * P_ID_DICHIARAZIONE_CONSISTENZA P_OPERAZIONE P_ESITO P_MSGERR P_CODERR
       */
      conn = getDatasource().getConnection();
      String sql = "{call PACK_DICHIARAZIONE_CONSISTENZA.OPERAZIONE_AMMESSA(?,?,?,?,?,?)}";
      cs = conn.prepareCall(sql);

      SolmrLogger.debug(this, "Executing operazioneAmmessaPLQSL: PLSQL " + sql);
      SolmrLogger.debug(this, "idAzienda: " + idAzienda);
      SolmrLogger.debug(this, "idUtente: " + idUtente);
      SolmrLogger.debug(this, "idDichiarazioneConsistenza: "
          + idDichiarazioneConsistenza);
      SolmrLogger.debug(this, "operazione: " + operazione);

      cs.setLong(1, idAzienda.longValue()); // P_ID_AZIENDA
      cs.setLong(2, idDichiarazioneConsistenza.longValue()); // P_ID_DICHIARAZIONE_CONSISTENZA
      cs.setString(3, operazione); // P_OPERAZIONE
      cs.registerOutParameter(4, Types.VARCHAR); // P_ESITO
      cs.registerOutParameter(5, Types.VARCHAR); // P_MSGERR
      cs.registerOutParameter(6, Types.VARCHAR); // P_CODERR
      cs.executeUpdate();
      String esito = cs.getString(4);
      String msgErr = cs.getString(5);
      String errorCode = cs.getString(6);

      if (!(errorCode == null || "".equals(errorCode)))
        throw new SolmrException((String) AnagErrors.get("ERR_PLSQL")
            + errorCode + " - " + msgErr);

      if ("S".equals(esito))
        result = true;

    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    catch (SQLException sqle)
    {
      SolmrLogger.fatal(this, "operazioneAmmessaPLQSL - SQLException: "
          + sqle.getMessage());
      throw new DataAccessException(sqle.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "operazioneAmmessaPLQSL - Generic Exception: "
          + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (cs != null)
          cs.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "operazioneAmmessaPLQSL - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "operazioneAmmessaPLQSL - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // Metodo per cancellare una dichiarazione di consistenza
  public void deleteDichiarazioneConsistenza(Long idDichiarazioneConsistenza)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    long codiceFotografiaTerreni = -1;
    long idDichiarazioneConsistenzaTemp = -1;
    boolean cancellaAll = true;
    try
    {
      conn = getDatasource().getConnection();

      // delete da db_segnalazione_dichiarazione di tutti i record con quell'
      // id_dichiarazione_consistenza
      String query = "DELETE DB_DICHIARAZIONE_SEGNALAZIONE WHERE ID_DICHIARAZIONE_CONSISTENZA=? ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idDichiarazioneConsistenza.longValue());

      SolmrLogger.debug(this, "Executing deleteDbDichiarazioneSegnalazione"
          + query);

      stmt.executeUpdate();
      
      // delete da db_accesso_piano_grafico di tutti i record con quell'
      // id_dichiarazione_consistenza
      query = "DELETE DB_ACCESSO_PIANO_GRAFICO WHERE ID_DICHIARAZIONE_CONSISTENZA=? ";
      
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idDichiarazioneConsistenza.longValue());
      
      SolmrLogger.debug(this, "Executing deleteDbAccessoPianoGrafico"
          + query);
      
      stmt.executeUpdate();

      // delete da db_dichiarazione_correzione di tutti i record con quell’
      // id_dichiarazione_consistenza
      query = "DELETE DB_DICHIARAZIONE_CORREZIONE WHERE ID_DICHIARAZIONE_CONSISTENZA = ? ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idDichiarazioneConsistenza.longValue());

      SolmrLogger
          .debug(this, "Executing deleteDichiarazioneCorrezione" + query);

      stmt.executeUpdate();
      
      // delete figli da azienda greening db_azienda_greening_esonero
      // id_dichiarazione_consistenza
      query = "DELETE DB_AZIENDA_GREENING_ESONERO WHERE ID_AZIENDA_GREENING  " +
      		" IN (SELECT ID_AZIENDA_GREENING FROM DB_AZIENDA_GREENING " +
      		"    WHERE ID_DICHIARAZIONE_CONSISTENZA = ? ) ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idDichiarazioneConsistenza.longValue());

      SolmrLogger
          .debug(this, "Executing deleteDbAziendaGreeningEsonero" + query);

      stmt.executeUpdate();
      
      // delete azienda greening  id_dichiarazione_consistenza
      query = "DELETE DB_AZIENDA_GREENING WHERE ID_DICHIARAZIONE_CONSISTENZA = ? ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idDichiarazioneConsistenza.longValue());

      SolmrLogger
          .debug(this, "Executing deleteDbAziendaGreening" + query);

      stmt.executeUpdate();
      
      
      // delete azienda efa  id_dichiarazione_consistenza
      query = "DELETE DB_AZIENDA_EFA WHERE ID_DICHIARAZIONE_CONSISTENZA = ? ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idDichiarazioneConsistenza.longValue());

      SolmrLogger
          .debug(this, "Executing deleteDbAziendaEfa" + query);

      stmt.executeUpdate();

      // dato l'id_dichiarazione_consistenza recupero il
      // codice_fotografia_terreni
      query = "SELECT DC.CODICE_FOTOGRAFIA_TERRENI "
          + "FROM DB_DICHIARAZIONE_CONSISTENZA DC "
          + "WHERE DC.ID_DICHIARAZIONE_CONSISTENZA = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing controlloUltimeModifiche: " + query);

      stmt.setLong(1, idDichiarazioneConsistenza.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        codiceFotografiaTerreni = rs.getLong("CODICE_FOTOGRAFIA_TERRENI");
      }
      rs.close();

      SolmrLogger.debug(this, "\n\n codiceFotografiaTerreni="
          + codiceFotografiaTerreni + "\n\n");

      // delete da db_dichiarazione-consistenza del record con
      // l'id_dichiarazione_consistenza
      query = "DELETE DB_DICHIARAZIONE_CONSISTENZA WHERE ID_DICHIARAZIONE_CONSISTENZA=? ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idDichiarazioneConsistenza.longValue());

      SolmrLogger.debug(this, "Executing deleteDichiarazioneConsistenza"
          + query);

      stmt.executeUpdate();

      // se il codice_fotografia_terreni era presente solo esclusivamente
      // per il record di quell'id_dichiarazione_consistenza
      // allora fare la delete da db_conduzione_dichiarata,
      // db_utilizzo_dichiarato di tutti i record con il
      // codice_fotografia_terreni
      if (codiceFotografiaTerreni != -1)
      {
        query = "SELECT DC.ID_DICHIARAZIONE_CONSISTENZA "
            + "FROM DB_DICHIARAZIONE_CONSISTENZA DC "
            + "WHERE DC.CODICE_FOTOGRAFIA_TERRENI = ? ";

        stmt = conn.prepareStatement(query);

        SolmrLogger.debug(this, "Executing controlloUltimeModifiche: " + query);

        stmt.setLong(1, codiceFotografiaTerreni);

        rs = stmt.executeQuery();

        while (rs.next())
        {
          idDichiarazioneConsistenzaTemp = rs
              .getLong("ID_DICHIARAZIONE_CONSISTENZA");
          SolmrLogger.debug(this, "\n\n idDichiarazioneConsistenzaTemp="
              + idDichiarazioneConsistenzaTemp + "\n\n");
          if (idDichiarazioneConsistenzaTemp != idDichiarazioneConsistenza
              .longValue())
            cancellaAll = false;
        }
        rs.close();
        if (cancellaAll)
        {

          // Va poi in cascade sull'utilizzo dichiarato
          query = "DELETE DB_CONDUZIONE_DICHIARATA WHERE CODICE_FOTOGRAFIA_TERRENI=? ";
          stmt = conn.prepareStatement(query);
          stmt.setLong(1, codiceFotografiaTerreni);
          SolmrLogger.debug(this, "Executing deleteDichiarazioneConsistenza"
              + query);
          stmt.executeUpdate();

          // Va poi in cascade su altro vitigno dichiarato
          query = "DELETE DB_UNITA_ARBOREA_DICHIARATA WHERE CODICE_FOTOGRAFIA_TERRENI=? ";
          stmt = conn.prepareStatement(query);
          stmt.setLong(1, codiceFotografiaTerreni);
          SolmrLogger.debug(this, "Executing deleteDichiarazioneConsistenza"
              + query);
          stmt.executeUpdate();

          // Effettuo delete su DB_ATTESTAZIONE_DICHIARATA
          query = "DELETE DB_ATTESTAZIONE_DICHIARATA WHERE CODICE_FOTOGRAFIA_TERRENI = ? ";
          stmt = conn.prepareStatement(query);
          stmt.setLong(1, codiceFotografiaTerreni);
          SolmrLogger.debug(this, "Executing delete dichiarazione dichiarata"
              + query);
          stmt.executeUpdate();
          
          // Effettuo delete su DB_ELEGGIBILITA_DICHIARATA
          query = "DELETE DB_ELEGGIBILITA_DICHIARATA WHERE CODICE_FOTOGRAFIA_TERRENI = ? ";
          stmt = conn.prepareStatement(query);
          stmt.setLong(1, codiceFotografiaTerreni);
          SolmrLogger.debug(this, "Executing delete DB_ELEGGIBILITA_DICHIARATA"
              + query);
          stmt.executeUpdate();
        }
      }

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in deleteDichiarazioneConsistenza "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in deleteDichiarazioneConsistenza: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in deleteDichiarazioneConsistenza "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in deleteDichiarazioneConsistenza "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo nuova validazione eliminazione su db_utilizzo_dichiarato e
  // db_conduzione_dichiarata qualora si stia facendo l'update di
  // db_dichiarazione_consistenza ma si usa un nuovo codice_fotografia_terreni
  public void deleteUtilizziAndConduzioni(Long codiceFotografiaTerreniOLD)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      /*
       * String query = "DELETE DB_UTILIZZO_DICHIARATO WHERE
       * CODICE_FOTOGRAFIA_TERRENI=? "; stmt = conn.prepareStatement(query);
       * stmt.setLong(1,codiceFotografiaTerreniOLD.longValue());
       * SolmrLogger.debug(this, "Executing
       * deleteDichiarazioneConsistenza"+query); stmt.executeUpdate();
       */

      // La prima non serve perchè andiamo in delete on cascade
      String query = "DELETE DB_CONDUZIONE_DICHIARATA WHERE CODICE_FOTOGRAFIA_TERRENI=? ";
      stmt = conn.prepareStatement(query);
      stmt.setLong(1, codiceFotografiaTerreniOLD.longValue());
      SolmrLogger.debug(this, "Executing deleteUtilizziAndConduzioni" + query);
      stmt.executeUpdate();

      query = "DELETE DB_UNITA_ARBOREA_DICHIARATA WHERE CODICE_FOTOGRAFIA_TERRENI=? ";
      stmt = conn.prepareStatement(query);
      stmt.setLong(1, codiceFotografiaTerreniOLD.longValue());
      SolmrLogger.debug(this, "Executing deleteUtilizziAndConduzioni" + query);
      stmt.executeUpdate();
      
      query = "DELETE DB_ELEGGIBILITA_DICHIARATA WHERE CODICE_FOTOGRAFIA_TERRENI=? ";
      stmt = conn.prepareStatement(query);
      stmt.setLong(1, codiceFotografiaTerreniOLD.longValue());
      SolmrLogger.debug(this, "Executing deleteUtilizziAndConduzioni" + query);
      stmt.executeUpdate();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in deleteUtilizziAndConduzioni "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in deleteUtilizziAndConduzioni: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in deleteUtilizziAndConduzioni "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in deleteUtilizziAndConduzioni "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  public void deleteDichiarazioneSegnalazione(Long idDichiarazioneConsistenza)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      // delete da db_segnalazione_dichiarazione di tutti i record con quell'
      // id_dichiarazione_consistenza
      String query = "DELETE DB_DICHIARAZIONE_SEGNALAZIONE WHERE ID_DICHIARAZIONE_CONSISTENZA=? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing deleteDichiarazioneSegnalazione"
          + query);
      if (idDichiarazioneConsistenza != null)
        SolmrLogger.debug(this, "idDichiarazioneConsistenza: "
            + idDichiarazioneConsistenza.longValue());

      stmt.setLong(1, idDichiarazioneConsistenza.longValue());

      stmt.executeUpdate();
      SolmrLogger.debug(this, "Executed deleteDichiarazioneSegnalazione");
    }
    catch (SQLException exc)
    {
      SolmrLogger
          .fatal(this, "SQLException in deleteDichiarazioneSegnalazione "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in deleteDichiarazioneSegnalazione: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in deleteDichiarazioneSegnalazione "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in deleteDichiarazioneSegnalazione "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  /**
   * Metodo per richiamare la procedura plsql per i controlli sull'insediamento
   * giovani Il parametro restituito può assumere tre valori: - uguale ad E:
   * chiedere conferma per il proseguimento - diverso da E: non permettere di
   * proseguire à E necessario risolvere le anomalie bloccanti
   */
  public String controlliInsediamentoPLSQL(Long idAzienda, Long idUtente)
      throws DataAccessException, SolmrException
  {
    Connection conn = null;
    CallableStatement cs = null;
    try
    {
      /*************************************************************************
       * PROCEDURE CONTROLLI_INSEDIAMENTO(P_ID_AZIENDA        IN DB_AZIENDA.ID_AZIENDA%TYPE,
       *                                  P_ID_UTENTE_LOGIN   IN ?.ID_UTENTE%TYPE, 
       *                                  P_ESITO_CONTR     OUT VARCHAR2,
       *                                  P_MSGERR       IN OUT VARCHAR2,
       *                                  P_CODERR       IN OUT VARCHAR2) IS
       */

      conn = getDatasource().getConnection();
      String sql = "{call PACK_DICHIARAZIONE_CONSISTENZA.CONTROLLI_INSEDIAMENTO(?,?,?,?,?)}";
      cs = conn.prepareCall(sql);
      cs.setLong(1, idAzienda.longValue());
      cs.setLong(2, idUtente.longValue());
      cs.registerOutParameter(3, Types.VARCHAR);
      cs.registerOutParameter(4, Types.VARCHAR);
      cs.registerOutParameter(5, Types.VARCHAR);

      cs.executeUpdate();
      String msgErr = cs.getString(4);
      String errorCode = cs.getString(5);

      if (!(errorCode == null || "".equals(errorCode)))
        throw new SolmrException((String) AnagErrors.get("ERR_PLSQL2")
            + errorCode + " - " + msgErr);
      return cs.getString(3);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    catch (SQLException sqle)
    {
      SolmrLogger.fatal(this, "controlliInsediamentoPLSQL - SQLException: "
          + sqle.getMessage());
      throw new DataAccessException(sqle.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "controlliInsediamentoPLSQL - Generic Exception: " + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (cs != null)
          cs.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "controlliInsediamentoPLSQL - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "controlliInsediamentoPLSQL - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  /**
   * Cancello gli eventuali documenti giustificativi associati ai controlli
   * 
   * @param idDichiarazioneConsistenza
   *          Long
   * @throws DataAccessException
   */
  public void deleteDichiarazioneCorrezione(Long idDichiarazioneConsistenza)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      // delete da db_segnalazione_dichiarazione di tutti i record con quell'
      // id_dichiarazione_consistenza
      String query = "DELETE DB_DICHIARAZIONE_CORREZIONE WHERE ID_DICHIARAZIONE_CONSISTENZA=? ";

      if (idDichiarazioneConsistenza != null)
        SolmrLogger.debug(this, "idDichiarazioneConsistenza: "
            + idDichiarazioneConsistenza.longValue());

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idDichiarazioneConsistenza.longValue());

      SolmrLogger.debug(this, "Executing deleteDichiarazioneSegnalazione"
          + query);

      stmt.executeUpdate();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in deleteDichiarazioneCorrezione "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in deleteDichiarazioneCorrezione: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in deleteDichiarazioneCorrezione "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in deleteDichiarazioneCorrezione "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per effettuare la modifiche delle dichiarazioni correzioni
  public void updateDichiarazioneCorrezione(Long idAzienda,
      Long idDichiarazioneConsistenza) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
      String query = "UPDATE DB_DICHIARAZIONE_CORREZIONE "
          + "SET ID_DICHIARAZIONE_CONSISTENZA = ? " + "WHERE ID_AZIENDA = ? "
          + "AND ID_DICHIARAZIONE_CONSISTENZA IS NULL";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idDichiarazioneConsistenza.longValue());
      stmt.setLong(2, idAzienda.longValue());

      SolmrLogger
          .debug(this, "Executing updateDichiarazioneCorrezione" + query);

      stmt.executeUpdate();
      SolmrLogger.debug(this, "Executed updateDichiarazioneCorrezione");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in updateDichiarazioneCorrezione "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in updateDichiarazioneCorrezione: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in updateDichiarazioneCorrezione "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in updateDichiarazioneCorrezione "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per effettuare la modifiche delle conduzioni particelle
  // settando a null il campo record_modificato
  public void updateConduzioneParticella(Long idAzienda)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
      String query = " UPDATE DB_CONDUZIONE_PARTICELLA "
          + " SET    RECORD_MODIFICATO = NULL, "
          + "        DICHIARAZIONE_RIPRISTINATA = NULL, "
          + "        ID_DICHIARAZIONE_CONSISTENZA = NULL "
          + "WHERE DATA_FINE_CONDUZIONE IS NULL " + "AND ID_UTE IN " + "( "
          + "SELECT ID_UTE FROM DB_UTE " + "WHERE ID_AZIENDA = ? "
          + "  AND DATA_FINE_ATTIVITA IS NULL " + ")";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idAzienda.longValue());

      SolmrLogger.debug(this, "Executing updateConduzioneParticella" + query);

      stmt.executeUpdate();
      SolmrLogger.debug(this, "Executed updateConduzioneParticella");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in updateConduzioneParticella "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(this, "Generic Exception in updateConduzioneParticella: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in updateConduzioneParticella "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in updateConduzioneParticella "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per effettuare la modifiche delle unità arboree sullo storico
  // settando a null il campo record_modificato
  public void updateStoricoUnitaArborea(Long idAzienda)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
      String query = "UPDATE DB_STORICO_UNITA_ARBOREA "
          + "SET RECORD_MODIFICATO = NULL "
          + "WHERE DATA_FINE_VALIDITA IS NULL " + "AND ID_AZIENDA = ? ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idAzienda.longValue());

      SolmrLogger.debug(this, "Executing updateStoricoUnitaArborea" + query);

      stmt.executeUpdate();
      SolmrLogger.debug(this, "Executed updateStoricoUnitaArborea");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in updateStoricoUnitaArborea "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in updateStoricoUnitaArborea: " + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in updateStoricoUnitaArborea "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in updateStoricoUnitaArborea "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per recuperare il dettaglio di un'anomalia
  public ErrAnomaliaDicConsistenzaVO getAnomaliaDichiarazioneConsistenza(
      Long idDichiarazioneSegnalazione) throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    ErrAnomaliaDicConsistenzaVO err = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT DS.ID_DICHIARAZIONE_SEGNALAZIONE, "
          + "       TC.CODICE_CONTROLLO, "
          + "       TC.DESCRIZIONE AS TIPO_ANOMALIA_ERRORE, "
          + "       DS.DESCRIZIONE AS DESC_ANOMALIA_ERRORE, "
          + "       TO_CHAR(DS.DATA_CONTROLLO,'DD/MM/YYYY') AS DATA_ESECUZIONE, "
          + "       DS.BLOCCANTE,DCC.ID_DICHIARAZIONE_CORREZIONE, "
          + "       DTC.DESCRIZIONE AS TIPO_DOCUMENTO,DCC.RIFERIMENTO_DOCUMENTO, "
          +
          // " TO_CHAR(DCC.DATA_AGGIORNAMENTO,'DD/MM/YYYY') || ' (' ||
          // UI.DENOMINAZIONE || '-' || UI.DESCRIZIONE_ENTE_APPARTENENZA || ')'
          // AS ULTIMA_MODIFICA, " +
          " DCC.DATA_AGGIORNAMENTO AS DATA_AGG, " +
          "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
          "          || ' ' " + 
          "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
          "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
          "         WHERE  DCC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
          "    AS UTENTE_AGG, " +
          "       (SELECT PVU.DENOMINAZIONE " +
          "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
          "        WHERE DCC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
          "    AS ENTE_AGG,  "
          + "       DCC.ID_DOCUMENTO_PROTOCOLLATO "
          + "FROM DB_DICHIARAZIONE_SEGNALAZIONE DS,DB_TIPO_CONTROLLO TC, "
          + "     DB_DICHIARAZIONE_CORREZIONE DCC," +
          "       DB_TIPO_DOCUMENTO DTC "
          //"       PAPUA_V_UTENTE_LOGIN PVU "
          + "WHERE DS.ID_DICHIARAZIONE_SEGNALAZIONE = ? "
          + "AND DS.ID_CONTROLLO = TC.ID_CONTROLLO  "
          + "AND NVL(DS.ID_DICHIARAZIONE_CONSISTENZA,-1)=NVL(DCC.ID_DICHIARAZIONE_CONSISTENZA(+),-1) "
          + "AND DS.ID_DICHIARAZIONE_CONSISTENZA IS NULL "
          + "AND DS.ID_AZIENDA=DCC.ID_AZIENDA(+) "
          + "AND DS.ID_CONTROLLO=DCC.ID_CONTROLLO(+) "
          + "AND NVL(DS.ID_STORICO_PARTICELLA,-1)=NVL(DCC.ID_STORICO_PARTICELLA(+),-1) "
          + "AND DCC.EXT_ID_DOCUMENTO=DTC.ID_DOCUMENTO(+) ";
          //+ "AND DCC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN (+) ";

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(this, "Executing ErrAnomaliaDicConsistenzaVO: " + query);

      stmt.setLong(1, idDichiarazioneSegnalazione.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        err = new ErrAnomaliaDicConsistenzaVO();
        err.setIdDichiarazioneSegnalazione(rs
            .getString("ID_DICHIARAZIONE_SEGNALAZIONE"));
        err.setCodiceControllo(rs.getString("CODICE_CONTROLLO"));
        err.setTipoAnomaliaErrore(rs.getString("TIPO_ANOMALIA_ERRORE"));
        err.setDescAnomaliaErrore(rs.getString("DESC_ANOMALIA_ERRORE"));
        err.setDataEsecuzione(rs.getString("DATA_ESECUZIONE"));
        err.setBloccanteStr(rs.getString("BLOCCANTE"));
        // Controllo se è un errore bloccante o se è solo un'anomalia
        if ("S".equals(err.getBloccanteStr()))
          err.setBloccante(true);
        else
          err.setBloccante(false);

        err.setIdDichiarazioneCorrezione(rs
            .getString("ID_DICHIARAZIONE_CORREZIONE"));
        err.setTipoDocumento(rs.getString("TIPO_DOCUMENTO"));
        err.setRiferimentoDocumento(rs.getString("RIFERIMENTO_DOCUMENTO"));

        // Nuovo Blocco ultima modifica
        String ultimaModifica = "";
        java.util.Date dataAgg = rs.getDate("DATA_AGG");
        if (dataAgg != null)
        {
          ultimaModifica = DateUtils.formatDate(dataAgg);
        }

        String tmp = "";
        String utDenominazione = rs.getString("UTENTE_AGG");
        if (utDenominazione != null && !utDenominazione.equals(""))
        {
          if (tmp.equals(""))
            tmp += " (";
          tmp += utDenominazione;
        }
        String utEnteAppart = rs.getString("ENTE_AGG");
        if (utEnteAppart != null && !utEnteAppart.equals(""))
        {
          if (tmp.equals(""))
            tmp += " (";
          else
            tmp += " - ";
          tmp += utEnteAppart;
        }
        if (!tmp.equals(""))
          tmp += ")";
        ultimaModifica += tmp;

        err.setUltimaModifica(ultimaModifica);

        // Ultima Modifica Spezzata
        err.setDataUltimaModifica(dataAgg);
        err.setUtenteUltimaModifica(utDenominazione);
        err.setEnteUltimaModifica(utEnteAppart);

        // err.setUltimaModifica(rs.getString("ULTIMA_MODIFICA"));
        if (Validator.isNotEmpty(rs.getString("ID_DOCUMENTO_PROTOCOLLATO")))
        {
          err.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO_PROTOCOLLATO")));
        }
      }
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "ErrAnomaliaDicConsistenzaVO - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "ErrAnomaliaDicConsistenzaVO - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "ErrAnomaliaDicConsistenzaVO - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "ErrAnomaliaDicConsistenzaVO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return err;
  }

  // Metodo per recuperare l'elenco degli errori o delle anomalie associati
  // all'elenco di IdDichiarazioneSegnalazione e vedere se possono essere
  // corrette.
  public Vector<ErrAnomaliaDicConsistenzaVO> getAnomaliePerCorrezione(Long elencoIdDichiarazioneSegnalazione[],long idMotivoDichiarazione) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ErrAnomaliaDicConsistenzaVO> elencoErrori = null;

    try
    {
      conn = getDatasource().getConnection();

      StringBuffer elencoIdDichiarazioneSegnalazioneStr = new StringBuffer();

      for (int i = 0; i < elencoIdDichiarazioneSegnalazione.length; i++)
      {
        elencoIdDichiarazioneSegnalazioneStr
            .append(elencoIdDichiarazioneSegnalazione[i]);
        if (i < elencoIdDichiarazioneSegnalazione.length - 1)
          elencoIdDichiarazioneSegnalazioneStr.append(',');
      }

      SolmrLogger.debug(this, "elencoIdDichiarazioneSegnalazioneStr "
          + elencoIdDichiarazioneSegnalazioneStr);

      String query = "SELECT DS.ID_DICHIARAZIONE_SEGNALAZIONE, TC.ID_CONTROLLO,"
          + "       TC.DESCRIZIONE || '-' || DS.DESCRIZIONE AS TIPO_ANOMALIA_ERRORE,"
          + "       TCF.FLAG_DOCUMENTO_GIUSTIFICATIVO, "
          + "       DS.ID_STORICO_PARTICELLA "
          + "FROM DB_DICHIARAZIONE_SEGNALAZIONE DS,"
          + "     DB_TIPO_CONTROLLO TC,DB_TIPO_CONTROLLO_FASE TCF, "
          + "     DB_TIPO_GRUPPO_CONTROLLO TGC,DB_STORICO_PARTICELLA SP,COMUNE C, DB_TIPO_MOTIVO_DICHIARAZIONE MD ";
      query += "WHERE DS.ID_DICHIARAZIONE_SEGNALAZIONE IN ("
          + elencoIdDichiarazioneSegnalazioneStr
          + ")"
          + "  AND DS.ID_CONTROLLO = TC.ID_CONTROLLO "
          + "  AND TC.ID_CONTROLLO=TCF.ID_CONTROLLO "
          + "  AND TC.ID_GRUPPO_CONTROLLO = TGC.ID_GRUPPO_CONTROLLO "
          + "  AND TCF.ID_FASE=MD.ID_FASE "
          + "  AND MD.ID_MOTIVO_DICHIARAZIONE=?"
          + "  AND DS.ID_STORICO_PARTICELLA=SP.ID_STORICO_PARTICELLA(+) "
          + "  AND SP.COMUNE=C.ISTAT_COMUNE(+) "
          + "ORDER BY TGC.ID_GRUPPO_CONTROLLO,TC.ORDINAMENTO,C.DESCOM,SP.SEZIONE,SP.FOGLIO,SP.PARTICELLA,SP.SUBALTERNO,DS.ID_DICHIARAZIONE_SEGNALAZIONE";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing getAnomaliePerCorrezione: " + query);
      
      SolmrLogger.debug(this,"Executing idMotivoDichiarazione: " + idMotivoDichiarazione);
      
      stmt.setLong(1, idMotivoDichiarazione);
      

      ResultSet rs = stmt.executeQuery();

      elencoErrori = new Vector<ErrAnomaliaDicConsistenzaVO>();

      int flagNull = 0;

      while (rs.next())
      {
        ErrAnomaliaDicConsistenzaVO err = new ErrAnomaliaDicConsistenzaVO();
        err.setIdControllo(rs.getString("ID_CONTROLLO"));
        err.setIdStoricoParticella(rs.getString("ID_STORICO_PARTICELLA"));
        err.setIdDichiarazioneSegnalazione(rs
            .getString("ID_DICHIARAZIONE_SEGNALAZIONE"));
        err.setDescAnomaliaErrore(rs.getString("TIPO_ANOMALIA_ERRORE"));
        err.setFlagDocumentoGiustificativo(rs
            .getString("FLAG_DOCUMENTO_GIUSTIFICATIVO"));
        if (err.getFlagDocumentoGiustificativo() == null)
          flagNull++;
        elencoErrori.add(err);
      }

      rs.close();

      if (flagNull == elencoErrori.size())
        elencoErrori = null;

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getAnomaliePerCorrezione - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getAnomaliePerCorrezione - Generic Exception: "
          + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "getAnomaliePerCorrezione - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getAnomaliePerCorrezione - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoErrori;
  }

  // Metodo per effettuare l'inserimento di una dichiarazione di correzione
  public void deleteInsertDichiarazioneCorrezione(
      String elencoIdDichiarazioneSegnalazione[], Vector<ErrAnomaliaDicConsistenzaVO> corrErr, long idUtente)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {

      conn = getDatasource().getConnection();

      String queryDelete1 = "DELETE DB_DICHIARAZIONE_CORREZIONE "
          + "WHERE ID_AZIENDA = ? "
          + "  AND ID_DICHIARAZIONE_CONSISTENZA IS NULL "
          + "  AND ID_CONTROLLO = ? " + "  AND ID_STORICO_PARTICELLA = ?";
      String queryDelete2 = "DELETE DB_DICHIARAZIONE_CORREZIONE "
          + "WHERE ID_AZIENDA = ? "
          + "  AND ID_DICHIARAZIONE_CONSISTENZA IS NULL "
          + "  AND ID_CONTROLLO = ? " + "  AND ID_STORICO_PARTICELLA IS NULL";

      String queryInsert = "INSERT INTO DB_DICHIARAZIONE_CORREZIONE "
          + "(ID_DICHIARAZIONE_CORREZIONE, ID_AZIENDA, ID_DICHIARAZIONE_CONSISTENZA,"
          + "ID_CONTROLLO, ID_STORICO_PARTICELLA, RIFERIMENTO_DOCUMENTO, "
          + "DATA_AGGIORNAMENTO, ID_UTENTE_AGGIORNAMENTO, ID_DOCUMENTO_PROTOCOLLATO) "
          + "VALUES" + "(?,?,null,?,?,?,SYSDATE,?,?)";
      int size = corrErr.size();
      for (int i = 0; i < size; i++)
      {
        ErrAnomaliaDicConsistenzaVO err = (ErrAnomaliaDicConsistenzaVO) corrErr
            .get(i);

        if (err.getFlagDocumentoGiustificativo() != null
            && !"".equals(err.getFlagDocumentoGiustificativo()))
        {
          // Prima cancello il record e poi inserisco

          if (err.getIdStoricoParticella() != null
              && !"".equals(err.getIdStoricoParticella()))
            stmt = conn.prepareStatement(queryDelete1);
          else
            stmt = conn.prepareStatement(queryDelete2);

          stmt.setLong(1, err.getIdAzienda().longValue()); // ID_AZIENDA
          stmt.setLong(2, Long.parseLong(err.getIdControllo())); // ID_CONTROLLO

          if (err.getIdStoricoParticella() != null
              && !"".equals(err.getIdStoricoParticella()))
            stmt.setLong(3, Long.parseLong(err.getIdStoricoParticella())); // ID_STORICO_PARTICELLA

          if (err.getIdStoricoParticella() != null
              && !"".equals(err.getIdStoricoParticella()))
            SolmrLogger.debug(this,
                "Executing deleteInsertDichiarazioneCorrezione" + queryDelete1);

          else
            SolmrLogger.debug(this,
                "Executing deleteInsertDichiarazioneCorrezione" + queryDelete2);

          SolmrLogger.debug(this,
              "Executing deleteInsertDichiarazioneCorrezione" + queryDelete1);

          stmt.executeUpdate();
          stmt.close();
          SolmrLogger
              .debug(this, "Executed deleteInsertDichiarazioneCorrezione");

          // Ora passo ad inserire
          stmt = conn.prepareStatement(queryInsert);

          Long primaryKey = getNextPrimaryKey((String) SolmrConstants
              .get("SEQ_DICHIARAZIONE_CORREZIONE"));

          stmt.setLong(1, primaryKey.longValue()); // ID_DICHIARAZIONE_CORREZIONE
          stmt.setLong(2, err.getIdAzienda().longValue()); // ID_AZIENDA
          stmt.setLong(3, Long.parseLong(err.getIdControllo())); // ID_CONTROLLO

          if (err.getIdStoricoParticella() == null
              || "".equals(err.getIdStoricoParticella()))
            stmt.setBigDecimal(4, null); // ID_STORICO_PARTICELLA
          else
            stmt.setLong(4, Long.parseLong(err.getIdStoricoParticella())); // ID_STORICO_PARTICELLA

          stmt.setString(5, err.getRiferimentoDocumento()); // RIFERIMENTO_DOCUMENTO
          stmt.setLong(6, idUtente); // ID_UTENTE_AGGIORNAMENTO
          if (err.getIdDocumento() != null)
          {
            stmt.setLong(7, err.getIdDocumento().longValue()); // ID_DOCUMENTO_PROTOCOLLATO
          }
          else
          {
            stmt.setString(7, null); // ID_DOCUMENTO_PROTOCOLLATO
          }

          SolmrLogger.debug(this,
              "Executing deleteInsertDichiarazioneCorrezione" + queryInsert);

          stmt.executeUpdate();
          stmt.close();
          SolmrLogger
              .debug(this, "Executed deleteInsertDichiarazioneCorrezione");
        }
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "SQLException in deleteInsertDichiarazioneCorrezione "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in deleteInsertDichiarazioneCorrezione: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in deleteInsertDichiarazioneCorrezione "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in deleteInsertDichiarazioneCorrezione "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per effettuare la cancellazione di una dichiarazione di correzione
  public void deleteDichiarazioneCorrezione(
      String elencoIdDichiarazioneSegnalazione[]) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      StringBuffer elencoIdDichiarazioneSegnalazioneStr = new StringBuffer();

      for (int i = 0; i < elencoIdDichiarazioneSegnalazione.length; i++)
      {
        elencoIdDichiarazioneSegnalazioneStr
            .append(elencoIdDichiarazioneSegnalazione[i]);
        if (i < elencoIdDichiarazioneSegnalazione.length - 1)
          elencoIdDichiarazioneSegnalazioneStr.append(',');
      }

      SolmrLogger.debug(this, "elencoIdDichiarazioneSegnalazioneStr "
          + elencoIdDichiarazioneSegnalazioneStr);

      conn = getDatasource().getConnection();

      String queryDelete = "DELETE DB_DICHIARAZIONE_CORREZIONE "
          + "WHERE ID_DICHIARAZIONE_CORREZIONE IN "
          + "( "
          + "SELECT DC.ID_DICHIARAZIONE_CORREZIONE "
          + "FROM DB_DICHIARAZIONE_SEGNALAZIONE DS,DB_DICHIARAZIONE_CORREZIONE DC "
          + "WHERE DS.ID_DICHIARAZIONE_SEGNALAZIONE IN ("
          + elencoIdDichiarazioneSegnalazioneStr
          + ") "
          + "  AND DC.ID_DICHIARAZIONE_CONSISTENZA IS NULL "
          + "  AND DS.ID_AZIENDA=DC.ID_AZIENDA "
          + "  AND DS.ID_CONTROLLO=DC.ID_CONTROLLO "
          + "  AND NVL(DS.ID_STORICO_PARTICELLA,-1)=NVL(DC.ID_STORICO_PARTICELLA,-1) "
          + ")";

      stmt = conn.prepareStatement(queryDelete);

      SolmrLogger.debug(this, "Executing deleteDichiarazioneCorrezione: "
          + queryDelete);
      stmt.executeUpdate();
      SolmrLogger.debug(this, "Executed deleteDichiarazioneCorrezione");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in deleteDichiarazioneCorrezione "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in deleteDichiarazioneCorrezione: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in deleteDichiarazioneCorrezione "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in deleteDichiarazioneCorrezione "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per recuperare l'elenco degli errori o delle anomalie dovute ad
  // una dichiarazione di consistenza
  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieDichiarazioneConsistenza(
      Long elencoIdDichiarazioneSegnalazione[], long idMotivoDichiarazione) throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ErrAnomaliaDicConsistenzaVO> elencoErrori = null;

    StringBuffer elencoIdDichiarazioneSegnalazioneStr = new StringBuffer();

    for (int i = 0; i < elencoIdDichiarazioneSegnalazione.length; i++)
    {
      elencoIdDichiarazioneSegnalazioneStr
          .append(elencoIdDichiarazioneSegnalazione[i]);
      if (i < elencoIdDichiarazioneSegnalazione.length - 1)
        elencoIdDichiarazioneSegnalazioneStr.append(',');
    }

    SolmrLogger.debug(this, "elencoIdDichiarazioneSegnalazioneStr "
        + elencoIdDichiarazioneSegnalazioneStr);

    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT TGC.ID_GRUPPO_CONTROLLO,TGC.DESCRIZIONE AS DESC_GRUPPO,"
          + "TC.DESCRIZIONE AS TIPO_ANOMALIA_ERRORE,"
          + "DS.DESCRIZIONE AS DESC_ANOMALIA_ERRORE,DS.BLOCCANTE, "
          + "DCC.ID_DICHIARAZIONE_CORREZIONE,DS.ID_DICHIARAZIONE_SEGNALAZIONE, "
          + "DCC.RIFERIMENTO_DOCUMENTO, DCC.EXT_ID_DOCUMENTO, "
          + "TCF.FLAG_DOCUMENTO_GIUSTIFICATIVO, "
          + " DCC.ID_DOCUMENTO_PROTOCOLLATO "
          + "FROM DB_DICHIARAZIONE_SEGNALAZIONE DS, DB_TIPO_CONTROLLO TC,"
          + "DB_TIPO_GRUPPO_CONTROLLO TGC,DB_DICHIARAZIONE_CORREZIONE DCC,  "
          + "DB_TIPO_DOCUMENTO DTC,DB_TIPO_CONTROLLO_FASE TCF, "
          + "DB_STORICO_PARTICELLA SP,COMUNE C, "
          + " DB_DOCUMENTO D,DB_TIPO_MOTIVO_DICHIARAZIONE MD  "
          + "WHERE DS.ID_DICHIARAZIONE_SEGNALAZIONE IN ("
          + elencoIdDichiarazioneSegnalazioneStr
          + ")"
          + "AND NVL(DS.ID_DICHIARAZIONE_CONSISTENZA,-1)=NVL(DCC.ID_DICHIARAZIONE_CONSISTENZA(+),-1) "
          + "AND DS.ID_DICHIARAZIONE_CONSISTENZA IS NULL "
          + "AND DS.ID_CONTROLLO = TC.ID_CONTROLLO "
          + "AND TC.ID_GRUPPO_CONTROLLO = TGC.ID_GRUPPO_CONTROLLO "
          + "AND DS.ID_AZIENDA=DCC.ID_AZIENDA(+) "
          + "AND DS.ID_CONTROLLO=DCC.ID_CONTROLLO(+) "
          + "AND NVL(DS.ID_STORICO_PARTICELLA,-1)=NVL(DCC.ID_STORICO_PARTICELLA(+),-1) "
          + "AND DCC.EXT_ID_DOCUMENTO=DTC.ID_DOCUMENTO(+) "
          + "AND TC.ID_CONTROLLO=TCF.ID_CONTROLLO "
          +" AND TCF.ID_FASE=MD.ID_FASE AND "
          +" MD.ID_MOTIVO_DICHIARAZIONE=? "
          + " AND DS.ID_STORICO_PARTICELLA=SP.ID_STORICO_PARTICELLA(+) "
          + "AND SP.COMUNE=C.ISTAT_COMUNE(+) "
          + " AND D.ID_DOCUMENTO(+) = DCC.ID_DOCUMENTO_PROTOCOLLATO "
          + "ORDER BY TGC.ID_GRUPPO_CONTROLLO,TC.ORDINAMENTO,C.DESCOM,SP.SEZIONE,SP.FOGLIO,SP.PARTICELLA,SP.SUBALTERNO,DS.ID_DICHIARAZIONE_SEGNALAZIONE ";

      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idMotivoDichiarazione);
      
      SolmrLogger.debug(this,
          "Executing idMotivoDichiarazione: " + idMotivoDichiarazione);

      SolmrLogger.debug(this,
          "Executing getErroriAnomalieDichiarazioneConsistenza: " + query);
      

      ResultSet rs = stmt.executeQuery();

      elencoErrori = new Vector<ErrAnomaliaDicConsistenzaVO>();

      while (rs.next())
      {
        ErrAnomaliaDicConsistenzaVO err = new ErrAnomaliaDicConsistenzaVO();

        err.setIdDichiarazioneSegnalazione(rs
            .getString("ID_DICHIARAZIONE_SEGNALAZIONE"));
        err.setDescAnomaliaErrore(rs.getString("DESC_ANOMALIA_ERRORE"));
        err.setDescGruppoControllo(rs.getString("DESC_GRUPPO"));
        err.setIdGruppoControllo(rs.getString("ID_GRUPPO_CONTROLLO"));
        err.setTipoAnomaliaErrore(rs.getString("TIPO_ANOMALIA_ERRORE"));
        err.setBloccanteStr(rs.getString("BLOCCANTE"));
        err.setIdDichiarazioneCorrezione(rs
            .getString("ID_DICHIARAZIONE_CORREZIONE"));
        err.setRiferimentoDocumento(rs.getString("RIFERIMENTO_DOCUMENTO"));
        err.setExtIdTipoDocumento(rs.getString("EXT_ID_DOCUMENTO"));
        err.setFlagDocumentoGiustificativo(rs
            .getString("FLAG_DOCUMENTO_GIUSTIFICATIVO"));
        if (Validator.isNotEmpty(rs.getString("ID_DOCUMENTO_PROTOCOLLATO")))
        {
          err.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO_PROTOCOLLATO")));
        }
        err.setNoError(true);

        elencoErrori.add(err);
      }

      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getErroriAnomalieDichiarazioneConsistenza - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getErroriAnomalieDichiarazioneConsistenza - Generic Exception: "
              + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "getErroriAnomalieDichiarazioneConsistenza - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getErroriAnomalieDichiarazioneConsistenza - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoErrori;
  }

  // Cerco l’ultima dichiarazione di consistenza in anagrafe con data
  // validazione (priva di ora, minuti secondi)
  // compresa tra le date contenute nei parametri “VRPU” e “SRPU” presenti sulla
  // tabella DB_PARAMETRO
  // dell’utente SRMCOMUNE
  public ConsistenzaVO getLastDichiarazionebetweenVRPUeSRPU(Long idAzienda,
      String dataVRPU, String dataSRPU) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    //Long result[] = null;
    ConsistenzaVO consistenzaVO = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "" +
      		"SELECT DC.ID_DICHIARAZIONE_CONSISTENZA, " +
      		"       DC.CODICE_FOTOGRAFIA_TERRENI, " +
      		"       DC.NUMERO_PROTOCOLLO, " +
      		"       DC.DATA " +
          "FROM   DB_DICHIARAZIONE_CONSISTENZA DC " +
          "WHERE  DC.ID_AZIENDA = ? " +
          "AND    TRUNC(DC.DATA) BETWEEN " +
          "TO_DATE(?,'dd/mm/yyyy') AND TO_DATE(?,'dd/mm/yyyy') " +
          "ORDER BY DC.DATA DESC ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing getLastDichiarazionebetweenVRPUeSRPU: " + query);
      if (idAzienda != null)
        SolmrLogger.debug(this, "idAzienda: " + idAzienda.longValue());
      SolmrLogger.debug(this, "dataVRPU: " + dataVRPU);
      SolmrLogger.debug(this, "dataSRPU: " + dataSRPU);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setString(2, dataVRPU);
      stmt.setString(3, dataSRPU);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        /*result = new Long[3];
        result[0] = new Long(rs.getLong("ID_DICHIARAZIONE_CONSISTENZA"));
        result[1] = new Long(rs.getLong("CODICE_FOTOGRAFIA_TERRENI"));
        result[2] = new Long(rs.getLong("PROCOLLATA"));
        SolmrLogger.debug(this, "result ID_DICHIARAZIONE_CONSISTENZA: "
            + result[0]);
        SolmrLogger.debug(this, "result CODICE_FOTOGRAFIA_TERRENI: "
            + result[1]);
        SolmrLogger.debug(this, "result PROCOLLATA: "
            + result[2]);*/
        
        consistenzaVO = new ConsistenzaVO();
        consistenzaVO.setIdDichiarazioneConsistenza(rs.getString("ID_DICHIARAZIONE_CONSISTENZA"));
        consistenzaVO.setCodiceFotografiaTerreni(new Long(rs.getLong("CODICE_FOTOGRAFIA_TERRENI")));
        consistenzaVO.setNumeroProtocollo(rs.getString("NUMERO_PROTOCOLLO"));
        consistenzaVO.setDataDichiarazione(rs.getTimestamp("DATA"));
      }
      rs.close();
      stmt.close();
      SolmrLogger.debug(this, "Executed getLastDichiarazionebetweenVRPUeSRPU");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getLastDichiarazionebetweenVRPUeSRPU - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getLastDichiarazionebetweenVRPUeSRPU - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "getLastDichiarazionebetweenVRPUeSRPU - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getLastDichiarazionebetweenVRPUeSRPU - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return consistenzaVO;
  }

  /**
   * Metodo per richiamare la procedura plsql per i controlli delle pratiche che
   * hanno utilizzato quella dichiarazione di consistenza
   * 
   * @param idAzienda:
   *          idAzienda selezionata
   * @return void
   * @throws DataAccessException
   * @throws SolmrException
   */
  public void aggiornaPraticaAziendaPLQSL(Long idAzienda)
      throws DataAccessException, SolmrException
  {
    Connection conn = null;
    CallableStatement cs = null;
    try
    {
      /*************************************************************************
       * PROCEDURE AGGIORNA_PRATICA_AZIENDA(P_ID_AZIENDA IN
       * DB_AZIENDA.ID_AZIENDA%TYPE, P_MSGERR IN OUT VARCHAR2, P_CODERR IN OUT
       * VARCHAR2);
       */
      conn = getDatasource().getConnection();
      String sql = "{call PACK_PRATICA_AZIENDA.AGGIORNA_PRATICA_AZIENDA(?,?,?)}";
      cs = conn.prepareCall(sql);

      SolmrLogger.debug(this, "Executing aggiornaPraticaAziendaPLQSL: PLSQL");
      SolmrLogger.debug(this, "idAzienda: " + idAzienda);

      cs.setLong(1, idAzienda.longValue());
      cs.registerOutParameter(2, Types.VARCHAR);
      cs.registerOutParameter(3, Types.VARCHAR);
      cs.executeUpdate();
      String msgErr = cs.getString(2);
      if (!(msgErr == null || "".equals(msgErr)))
        throw new SolmrException((String) AnagErrors
            .get("ERR_PLSQL_PRATICA_AZIENDA")
            + " " + msgErr);

      SolmrLogger.debug(this, "Executing aggiornaPraticaAziendaPLQSL");
    }
    catch (SolmrException se)
    {
      SolmrLogger.debug(this, "aggiornaPraticaAziendaPLQSL - SolmrException: "
          + se.getMessage());
      throw new SolmrException(se.getMessage());
    }
    catch (SQLException sqle)
    {
      SolmrLogger.fatal(this, "aggiornaPraticaAziendaPLQSL - SQLException: "
          + sqle.getMessage());
      throw new DataAccessException(sqle.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(this, "aggiornaPraticaAziendaPLQSL - Generic Exception: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (cs != null)
          cs.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "aggiornaPraticaAziendaPLQSL - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "aggiornaPraticaAziendaPLQSL - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  /**
   * Metodo per verificare se esistono praticche associate all'azienda ed alla
   * dichiarazione
   * 
   * @param idAzienda
   *          Long :identificativo dell’azienda in esame
   * @param idDichiarazioneConsistenza
   *          Long : identificativo della consistenza in esame.
   * @param idProcedimento
   *          Long null oppure 12 (RPU) oppure 2 (PSR)
   * @param dataSRPU
   *          String null o data di cui bisogna prendere solo l'anno
   * @param bozza
   *          boolean
   * @throws DataAccessException
   * @return long
   */
  public long isEsistePratica(Long idAzienda, Long idDichiarazioneConsistenza,
      Long idProcedimento, String dataDGTF, boolean bozza)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    long result = 0;
    try
    {
      conn = getDatasource().getConnection();

      if (idAzienda != null)
        SolmrLogger.debug(this, "idAzienda: " + idAzienda.longValue());
      if (idDichiarazioneConsistenza != null)
        SolmrLogger.debug(this, "idDichiarazioneConsistenza: "
            + idDichiarazioneConsistenza.longValue());
      if (idProcedimento != null)
        SolmrLogger
            .debug(this, "idProcedimento: " + idProcedimento.longValue());
      SolmrLogger.debug(this, "dataDGTF: " + dataDGTF);
      SolmrLogger.debug(this, "bozza: " + bozza);

      String query = "SELECT COUNT(*) AS NUM "
          + "FROM DB_PROCEDIMENTO_AZIENDA PA " + "WHERE PA.ID_AZIENDA = ? "
          + "AND PA.ID_DICHIARAZIONE_CONSISTENZA = ? ";

      if (idProcedimento != null)
        query += " AND PA.ID_PROCEDIMENTO = " + idProcedimento.longValue();
      if (bozza)
        query += " AND TO_UPPER(PA.DESCRIZIONE_STATO)=TO_UPPER('"
            + SolmrConstants.PRATICA_IN_BOZZA + "') ";
      if (dataDGTF != null)
        query += " AND PA.ANNO_CAMPAGNA = "
            + DateUtils.getCurrentYear().toString();

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing isEsistePratica: " + query);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setLong(2, idDichiarazioneConsistenza.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        result = rs.getLong("NUM");

      SolmrLogger.debug(this, "Executed isEsistePratica: " + result);

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "isEsistePratica - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "isEsistePratica - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this,
            "isEsistePratica - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "isEsistePratica - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    //******************
    return result;
  }

  // Se il parametro CRPU presente sulla tabella DB_PARAMETRO dell’utente
  // SRMCOMUNE = ‘N’
  // creare una nuova dichiarazione di consistenza con data uguale alla data
  // dell’ultima dichiarazione di consistenza trovata + n minuti
  // (il numero di minuti è salvato nel parametro MDIC presente sulla tabella
  // DB_PARAMETRO dell’utente SRMCOMUNE)
  public Date getDataLastDichiarazione(Long idDichiarazioneConsistenza,
      String minuti) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Date result = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT DC.DATA +(1/1440)* " + minuti + " AS NUOVA_DATA "
          + "FROM DB_DICHIARAZIONE_CONSISTENZA DC "
          + "WHERE DC.ID_DICHIARAZIONE_CONSISTENZA = ?";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing getDataLastDichiarazione: " + query);
      if (idDichiarazioneConsistenza != null)
        SolmrLogger.debug(this, "idDichiarazioneConsistenza: "
            + idDichiarazioneConsistenza.longValue());
      SolmrLogger.debug(this, "minuti: " + minuti);

      stmt.setLong(1, idDichiarazioneConsistenza.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        result = rs.getTimestamp("NUOVA_DATA");

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getDataLastDichiarazione - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getDataLastDichiarazione - Generic Exception: "
          + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "getDataLastDichiarazione - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getDataLastDichiarazione - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  /**
   * Metodo che mi permette di recuperare l'elenco dei controlli effettuati e le
   * rispettive segnalazioni\correzioni associate
   * 
   * @param idDichiarazioneConsistenza
   * @param idFase
   * @param errAnomaliaDicConsistenzaRicercaVO
   * @param orderBy
   * @return it.csi.solmr.dto.anag.ErrAnomaliaDicConsistenzaVO[]
   * @throws DataAccessException
   */
  public ErrAnomaliaDicConsistenzaVO[] getListAnomalieByIdDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza, Long idFase,
      ErrAnomaliaDicConsistenzaVO errAnomaliaDicConsistenzaRicercaVO,
      String[] orderBy) throws DataAccessException
  {
    SolmrLogger
        .debug(
            this,
            "Invocating getListAnomalieByIdDichiarazioneConsistenza method in ConsistenzaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ErrAnomaliaDicConsistenzaVO> elencoAnomalie = new Vector<ErrAnomaliaDicConsistenzaVO>();

    try
    {
      SolmrLogger
          .debug(
              this,
              "Creating db-connection in getListAnomalieByIdDichiarazioneConsistenza method in ConsistenzaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in getListAnomalieByIdDichiarazioneConsistenza method in ConsistenzaDAO and it values: "
                  + conn + "\n");

      String query = "";
      int count = 0;

      if (Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
          .getTipoAnomaliaBloccante())
          || Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
              .getTipoAnomaliaWarning())
          || (!Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
              .getTipoAnomaliaBloccante())
              && !Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
                  .getTipoAnomaliaWarning()) && !Validator
              .isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
                  .getTipoAnomaliaOk())))
      {
        query += " SELECT TGC.ID_GRUPPO_CONTROLLO, "
            + "        TGC.DESCRIZIONE AS DESC_GRUPPO, "
            + "        TC.ORDINAMENTO, "
            + "        TC.DESCRIZIONE AS TIPO_ANOMALIA_ERRORE, "
            + "        TC.ID_CONTROLLO, "
            + "        DS.DESCRIZIONE AS DESC_ANOMALIA_ERRORE, "
            + "        DS.BLOCCANTE, "
            + "        DCC.ID_DICHIARAZIONE_CORREZIONE, "
            + "        DS.ID_DICHIARAZIONE_SEGNALAZIONE, "
            + "        DCC.EXT_ID_DOCUMENTO, "
            + "        TD.DESCRIZIONE || ' ' || 'Rif:' || DCC.RIFERIMENTO_DOCUMENTO AS RISOLUZIONE, "
            + "        DCC.ID_DOCUMENTO_PROTOCOLLATO, "
            + "        C.DESCOM, "
            + "        SP.SEZIONE, "
            + "        SP.FOGLIO, "
            + "        SP.PARTICELLA, "
            + "        SP.SUBALTERNO "
            + " FROM   DB_DICHIARAZIONE_SEGNALAZIONE DS, "
            + "        DB_DICHIARAZIONE_CORREZIONE DCC, "
            + "        DB_TIPO_CONTROLLO TC, "
            + "        DB_TIPO_GRUPPO_CONTROLLO TGC, "
            + "        DB_TIPO_CONTROLLO_FASE TCF, "
            + "        DB_TIPO_DOCUMENTO TD, "
            + "        DB_STORICO_PARTICELLA SP, "
            + "        COMUNE C, "
            + "        DB_TIPO_MOTIVO_DICHIARAZIONE TMD, "
            + "        DB_DICHIARAZIONE_CONSISTENZA DC "
            + " WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? "
            + " AND    DC.ID_DICHIARAZIONE_CONSISTENZA = DS.ID_DICHIARAZIONE_CONSISTENZA "
            + " AND    DC.ID_MOTIVO_DICHIARAZIONE = TMD.ID_MOTIVO_DICHIARAZIONE "
            + " AND    DS.ID_AZIENDA = DCC.ID_AZIENDA(+) "
            + " AND    DS.ID_CONTROLLO = DCC.ID_CONTROLLO(+) "
            + " AND    NVL(DS.ID_STORICO_PARTICELLA,-1) = NVL(DCC.ID_STORICO_PARTICELLA(+),-1) "
            + " AND    DS.ID_DICHIARAZIONE_CONSISTENZA = DCC.ID_DICHIARAZIONE_CONSISTENZA(+) "
            + " AND    TC.ID_CONTROLLO = DS.ID_CONTROLLO "
            + " AND    TGC.ID_GRUPPO_CONTROLLO = TC.ID_GRUPPO_CONTROLLO "
            + " AND    TCF.FASE = TMD.ID_FASE "
            + " AND    TC.ID_CONTROLLO = TCF.ID_CONTROLLO "
            + " AND    DCC.EXT_ID_DOCUMENTO = TD.ID_DOCUMENTO(+) "
            + " AND    DS.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA(+) "
            + " AND    SP.COMUNE = C.ISTAT_COMUNE(+) ";
        if (Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
            .getIdGruppoControllo()))
        {
          query += " AND TGC.ID_GRUPPO_CONTROLLO = ? ";
        }
        if (Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
            .getIdControllo()))
        {
          query += " AND TC.ID_CONTROLLO = ? ";
        }
        // Se l'utente ha specificato la tipologia di anomalia bloccante
        boolean isFirst = true;
        if (Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
            .getTipoAnomaliaBloccante()))
        {
          count++;
          query += " AND (DS.BLOCCANTE = ? ";
          isFirst = false;
        }
        // Se l'utente ha specificato la tipologia di anomalia warning
        if (Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
            .getTipoAnomaliaWarning()))
        {
          count++;
          if (!isFirst)
          {
            query += " OR ";
          }
          else
          {
            query += " AND (";
          }
          query += " DS.BLOCCANTE = ? ";
          isFirst = false;
        }
        if (!isFirst)
        {
          query += ")";
        }
      }
      if (Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
          .getTipoAnomaliaOk())
          || (!Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
              .getTipoAnomaliaBloccante())
              && !Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
                  .getTipoAnomaliaWarning()) && !Validator
              .isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
                  .getTipoAnomaliaOk())))
      {
        if (count > 0
            || (!Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
                .getTipoAnomaliaBloccante())
                && !Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
                    .getTipoAnomaliaWarning()) && !Validator
                .isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
                    .getTipoAnomaliaOk())))
        {
          query += " UNION ";
        }
        query += " SELECT TGC.ID_GRUPPO_CONTROLLO, "
            + "        TGC.DESCRIZIONE AS DESC_GRUPPO, "
            + "        TC.ORDINAMENTO, "
            + "        TC.DESCRIZIONE AS TIPO_ANOMALIA_ERRORE, "
            + "        TC.ID_CONTROLLO, "
            + "        NULL AS DESC_ANOMALIA_ERRORE, "
            + "        'N' AS BLOCCANTE, "
            + "        0 AS ID_DICHIARAZIONE_CORREZIONE, "
            + "        0 AS ID_DICHIARAZIONE_SEGNALAZIONE, "
            + "        0 AS EXT_ID_DOCUMENTO, "
            + "        NULL AS RISOLUZIONE, "
            + "        0 AS ID_DOCUMENTO_PROTOCOLLATO, "
            + "        NULL, "
            + "        NULL, "
            + "        0, "
            + "        0, "
            + "        NULL "
            + " FROM   DB_TIPO_CONTROLLO TC, "
            + "        DB_TIPO_CONTROLLO_FASE TCF, "
            + "        DB_TIPO_GRUPPO_CONTROLLO TGC, "
            + "        DB_TIPO_MOTIVO_DICHIARAZIONE TMD, "
            + "        DB_DICHIARAZIONE_CONSISTENZA DC "
            + " WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? "
            + " AND    DC.ID_MOTIVO_DICHIARAZIONE = TMD.ID_MOTIVO_DICHIARAZIONE "
            + " AND    TMD.ID_FASE = TCF.FASE "
            + " AND    TCF.ID_CONTROLLO = TC.ID_CONTROLLO "
            + " AND    TC.ID_GRUPPO_CONTROLLO = TGC.ID_GRUPPO_CONTROLLO "
            + " AND    TC.ID_CONTROLLO NOT IN "
            + "        (SELECT ID_CONTROLLO "
            + "         FROM   DB_DICHIARAZIONE_SEGNALAZIONE "
            + "         WHERE  ID_DICHIARAZIONE_CONSISTENZA = ?) ";
        if (Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
            .getIdGruppoControllo()))
        {
          query += " AND TGC.ID_GRUPPO_CONTROLLO = ? ";
        }
        if (Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
            .getIdControllo()))
        {
          query += " AND TC.ID_CONTROLLO = ? ";
        }
      }

      String ordinamento = "";
      if (orderBy != null && orderBy.length > 0)
      {
        String criterio = "";
        for (int i = 0; i < orderBy.length; i++)
        {
          if (i == 0)
          {
            criterio = (String) orderBy[i];
          }
          else
          {
            criterio += ", " + (String) orderBy[i];
          }
        }
        ordinamento = " ORDER BY " + criterio;
      }
      if (!ordinamento.equals(""))
      {
        query += ordinamento;
      }

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [ID_DICHIARAZIONE_CONSISTENZA] in getListAnomalieByIdDichiarazioneConsistenza method in ConsistenzaDAO: "
                  + idDichiarazioneConsistenza + "\n");

      stmt = conn.prepareStatement(query);

      int indice = 0;
      if (Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
          .getTipoAnomaliaBloccante())
          || Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
              .getTipoAnomaliaWarning())
          || (!Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
              .getTipoAnomaliaBloccante())
              && !Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
                  .getTipoAnomaliaWarning()) && !Validator
              .isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
                  .getTipoAnomaliaOk())))
      {
        stmt.setLong(++indice, idDichiarazioneConsistenza.longValue());
        if (Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
            .getIdGruppoControllo()))
        {
          stmt.setString(++indice, errAnomaliaDicConsistenzaRicercaVO
              .getIdGruppoControllo());
          SolmrLogger
              .debug(
                  this,
                  "Value of parameter "
                      + indice
                      + " [ID_GRUPPO_CONTROLLO] in getListAnomalieByIdDichiarazioneConsistenza method in ConsistenzaDAO: "
                      + errAnomaliaDicConsistenzaRicercaVO
                          .getIdGruppoControllo() + "\n");
        }
        if (Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
            .getIdControllo()))
        {
          stmt.setString(++indice, errAnomaliaDicConsistenzaRicercaVO
              .getIdControllo());
          SolmrLogger
              .debug(
                  this,
                  "Value of parameter "
                      + indice
                      + " [ID_CONTROLLO] in getListAnomalieByIdDichiarazioneConsistenza method in ConsistenzaDAO: "
                      + errAnomaliaDicConsistenzaRicercaVO.getIdControllo()
                      + "\n");
        }
        if (Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
            .getTipoAnomaliaBloccante()))
        {
          stmt.setString(++indice, errAnomaliaDicConsistenzaRicercaVO
              .getTipoAnomaliaBloccante());
          SolmrLogger
              .debug(
                  this,
                  "Value of parameter "
                      + indice
                      + " [TIPO_ANOMALIA_BLOCCANTE] in getListAnomalieByIdDichiarazioneConsistenza method in ConsistenzaDAO: "
                      + errAnomaliaDicConsistenzaRicercaVO
                          .getTipoAnomaliaBloccante() + "\n");
        }
        if (Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
            .getTipoAnomaliaWarning()))
        {
          stmt.setString(++indice, errAnomaliaDicConsistenzaRicercaVO
              .getTipoAnomaliaWarning());
          SolmrLogger
              .debug(
                  this,
                  "Value of parameter "
                      + indice
                      + " [TIPO_ANOMALIA_BLOCCANTE] in getListAnomalieByIdDichiarazioneConsistenza method in ConsistenzaDAO: "
                      + errAnomaliaDicConsistenzaRicercaVO
                          .getTipoAnomaliaWarning() + "\n");
        }
      }
      if (Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
          .getTipoAnomaliaOk())
          || (!Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
              .getTipoAnomaliaBloccante())
              && !Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
                  .getTipoAnomaliaWarning()) && !Validator
              .isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
                  .getTipoAnomaliaOk())))
      {
        stmt.setLong(++indice, idDichiarazioneConsistenza.longValue());
        stmt.setLong(++indice, idDichiarazioneConsistenza.longValue());
        if (Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
            .getIdGruppoControllo()))
        {
          stmt.setString(++indice, errAnomaliaDicConsistenzaRicercaVO
              .getIdGruppoControllo());
          SolmrLogger
              .debug(
                  this,
                  "Value of parameter "
                      + indice
                      + " [ID_GRUPPO_CONTROLLO] in getListAnomalieByIdDichiarazioneConsistenza method in ConsistenzaDAO: "
                      + errAnomaliaDicConsistenzaRicercaVO
                          .getIdGruppoControllo() + "\n");
        }
        if (Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO
            .getIdControllo()))
        {
          stmt.setString(++indice, errAnomaliaDicConsistenzaRicercaVO
              .getIdControllo());
          SolmrLogger
              .debug(
                  this,
                  "Value of parameter "
                      + indice
                      + " [ID_CONTROLLO] in getListAnomalieByIdDichiarazioneConsistenza method in ConsistenzaDAO: "
                      + errAnomaliaDicConsistenzaRicercaVO.getIdControllo()
                      + "\n");
        }
      }

      SolmrLogger.debug(this,
          "Executing getListAnomalieByIdDichiarazioneConsistenza: " + query
              + "\n");

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        ErrAnomaliaDicConsistenzaVO errAnomaliaDicConsistenzaVO = new ErrAnomaliaDicConsistenzaVO();
        errAnomaliaDicConsistenzaVO.setIdGruppoControllo(rs
            .getString("ID_GRUPPO_CONTROLLO"));
        errAnomaliaDicConsistenzaVO.setDescGruppoControllo(rs
            .getString("DESC_GRUPPO"));
        errAnomaliaDicConsistenzaVO.setTipoAnomaliaErrore(rs
            .getString("TIPO_ANOMALIA_ERRORE"));
        errAnomaliaDicConsistenzaVO
            .setIdControllo(rs.getString("ID_CONTROLLO"));
        errAnomaliaDicConsistenzaVO.setDescAnomaliaErrore(rs
            .getString("DESC_ANOMALIA_ERRORE"));
        if (Validator.isNotEmpty(rs.getString("BLOCCANTE"))
            && rs.getString("BLOCCANTE")
                .equalsIgnoreCase(SolmrConstants.FLAG_S))
        {
          errAnomaliaDicConsistenzaVO.setBloccante(true);
        }
        else
        {
          errAnomaliaDicConsistenzaVO.setBloccante(false);
        }
        if (Validator.isNotEmpty(rs.getString("ID_DICHIARAZIONE_CORREZIONE"))
            && !rs.getString("ID_DICHIARAZIONE_CORREZIONE").equalsIgnoreCase(
                "0"))
        {
          errAnomaliaDicConsistenzaVO.setIdDichiarazioneCorrezione(rs
              .getString("ID_DICHIARAZIONE_CORREZIONE"));
        }
        if (Validator.isNotEmpty(rs.getString("ID_DICHIARAZIONE_SEGNALAZIONE"))
            && !rs.getString("ID_DICHIARAZIONE_SEGNALAZIONE").equalsIgnoreCase(
                "0"))
        {
          errAnomaliaDicConsistenzaVO.setIdDichiarazioneSegnalazione(rs
              .getString("ID_DICHIARAZIONE_SEGNALAZIONE"));
        }
        if (Validator.isNotEmpty(rs.getString("EXT_ID_DOCUMENTO"))
            && !rs.getString("EXT_ID_DOCUMENTO").equalsIgnoreCase("0"))
        {
          errAnomaliaDicConsistenzaVO.setExtIdTipoDocumento(rs
              .getString("EXT_ID_DOCUMENTO"));
          errAnomaliaDicConsistenzaVO.setRisoluzione(rs
              .getString("RISOLUZIONE"));
        }
        if (Validator.isNotEmpty(rs.getString("ID_DOCUMENTO_PROTOCOLLATO"))
            && !rs.getString("ID_DOCUMENTO_PROTOCOLLATO").equalsIgnoreCase("0"))
        {
          errAnomaliaDicConsistenzaVO.setIdDocumento(new Long(rs
              .getLong("ID_DOCUMENTO_PROTOCOLLATO")));
        }
        elencoAnomalie.add(errAnomaliaDicConsistenzaVO);
      }

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger
          .error(
              this,
              "getListAnomalieByIdDichiarazioneConsistenza in ConsistenzaDAO - SQLException: "
                  + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .error(
              this,
              "getListAnomalieByIdDichiarazioneConsistenza in ConsistenzaDAO - Generic Exception: "
                  + ex + "\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .error(
                this,
                "getListAnomalieByIdDichiarazioneConsistenza in ConsistenzaDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getListAnomalieByIdDichiarazioneConsistenza in ConsistenzaDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger
        .debug(
            this,
            "Invocated getListAnomalieByIdDichiarazioneConsistenza method in ConsistenzaDAO\n");
    if (elencoAnomalie.size() == 0)
    {
      return (ErrAnomaliaDicConsistenzaVO[]) elencoAnomalie
          .toArray(new ErrAnomaliaDicConsistenzaVO[0]);
    }
    else
    {
      return (ErrAnomaliaDicConsistenzaVO[]) elencoAnomalie
          .toArray(new ErrAnomaliaDicConsistenzaVO[elencoAnomalie.size()]);
    }
  }

  /**
   * Metodo che mi restituisce l'elenco delle dichiarazioni di consistenza
   * relative ad un'azienda agricola con le informazioni relative al motivo
   * della dichiarazione
   * 
   * @param idAzienda
   * @param orderBy
   * @return it.csi.solmr.dto.anag.ConsistenzaVO[]
   * @throws DataAccessException
   */
  public ConsistenzaVO[] getListDichiarazioniConsistenzaByIdAzienda(
      Long idAzienda, String[] orderBy) throws DataAccessException
  {
    SolmrLogger
        .debug(
            this,
            "Invocating getListDichiarazioniConsistenzaByIdAzienda method in ConsistenzaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ConsistenzaVO> elencoDichiarazioni = new Vector<ConsistenzaVO>();

    try
    {
      SolmrLogger
          .debug(
              this,
              "Creating db-connection in getListDichiarazioniConsistenzaByIdAzienda method in ConsistenzaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in getListDichiarazioniConsistenzaByIdAzienda method in ConsistenzaDAO and it values: "
                  + conn + "\n");

      String query = " SELECT DC.ID_DICHIARAZIONE_CONSISTENZA, "
          + "        DC.ID_AZIENDA, " + "        DC.ANNO, "
          + "        DC.DATA, " + "        DC.TIPO_CONVALIDA, "
          + "        DC.CODICE_FOTOGRAFIA_TERRENI, "
          + "        DC.ID_PROCEDIMENTO, " + "        DC.ID_UTENTE, "
          + "        DC.NOTE, " + "        DC.ID_MOTIVO_DICHIARAZIONE, "
          + "        DC.RESPONSABILE, "
          + "        DC.DATA_AGGIORNAMENTO_FASCICOLO, "
          + "        DC.CUAA_VALIDATO, " + "        DC.FLAG_ANOMALIA, "
          + "        DC.DATA_INSERIMENTO_DICHIARAZIONE, "
          + "        DC.FLAG_INVIA_USO, " + "        DC.ANNO_CAMPAGNA, "
          + "        TMD.ID_MOTIVO_DICHIARAZIONE, "
          + "        TMD.DESCRIZIONE, " + "        TMD.DATA_INIZIO_VALIDITA, "
          + "        TMD.DATA_FINE_VALIDITA, " + "        TMD.ANNO_CAMPAGNA, "
          + "        TMD.ID_FASE, " + "        TMD.GTFO, "
          + "        TMD.TIPO_DICHIARAZIONE "
          + " FROM   DB_DICHIARAZIONE_CONSISTENZA DC, "
          + "        DB_TIPO_MOTIVO_DICHIARAZIONE TMD "
          + " WHERE  DC.ID_AZIENDA = ? "
          + " AND    DC.ID_MOTIVO_DICHIARAZIONE = TMD.ID_MOTIVO_DICHIARAZIONE ";

      String ordinamento = "";
      if (orderBy != null && orderBy.length > 0)
      {
        String criterio = "";
        for (int i = 0; i < orderBy.length; i++)
        {
          if (i == 0)
          {
            criterio = (String) orderBy[i];
          }
          else
          {
            criterio += ", " + (String) orderBy[i];
          }
        }
        ordinamento = "ORDER BY " + criterio;
      }
      if (!ordinamento.equals(""))
      {
        query += ordinamento;
      }

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [ID_AZIENDA] in getListDichiarazioniConsistenzaByIdAzienda method in ConsistenzaDAO: "
                  + idAzienda + "\n");
      SolmrLogger
          .debug(
              this,
              "Value of parameter 2 [ORDER_BY] in getListDichiarazioniConsistenzaByIdAzienda method in ConsistenzaDAO: "
                  + orderBy + "\n");

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idAzienda.longValue());

      SolmrLogger.debug(this,
          "Executing getListDichiarazioniConsistenzaByIdAzienda: " + query
              + "\n");

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        ConsistenzaVO consistenzaVO = new ConsistenzaVO();
        consistenzaVO.setIdDichiarazioneConsistenza(rs
            .getString("ID_DICHIARAZIONE_CONSISTENZA"));
        consistenzaVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        consistenzaVO.setAnno(rs.getString("ANNO"));
        consistenzaVO.setData(rs.getString("DATA"));
        consistenzaVO.setTipoConvalida(rs.getString("TIPO_CONVALIDA"));
        consistenzaVO.setCodiceFotografiaTerreni(new Long(rs
            .getLong("CODICE_FOTOGRAFIA_TERRENI")));
        consistenzaVO.setIdProcedimento(rs.getString("ID_PROCEDIMENTO"));
        consistenzaVO.setIdUtente(rs.getString("ID_UTENTE"));
        consistenzaVO.setNote(rs.getString("NOTE"));
        consistenzaVO.setIdMotivo(rs.getString("ID_MOTIVO_DICHIARAZIONE"));
        consistenzaVO.setResponsabile(rs.getString("RESPONSABILE"));
        consistenzaVO.setDataAggiornamentoFascicolo(rs
            .getDate("DATA_AGGIORNAMENTO_FASCICOLO"));
        consistenzaVO.setCuaaValidato(rs.getString("CUAA_VALIDATO"));
        consistenzaVO.setFlagAnomalia(rs.getString("FLAG_ANOMALIA"));
        consistenzaVO.setDataInserimentoDichiarazione(rs
            .getTimestamp("DATA_INSERIMENTO_DICHIARAZIONE"));
        consistenzaVO.setFlagInviaUso(rs.getString("FLAG_INVIA_USO"));
        consistenzaVO.setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
        TipoMotivoDichiarazioneVO tipoMotivoDichiarazioneVO = new TipoMotivoDichiarazioneVO();
        tipoMotivoDichiarazioneVO.setIdMotivoDichiarazione(new Long(rs
            .getLong("ID_MOTIVO_DICHIARAZIONE")));
        tipoMotivoDichiarazioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoMotivoDichiarazioneVO.setDataInizioValidita(rs
            .getDate("DATA_INIZIO_VALIDITA"));
        tipoMotivoDichiarazioneVO.setDataFineValidita(rs
            .getDate("DATA_FINE_VALIDITA"));
        tipoMotivoDichiarazioneVO
            .setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
        tipoMotivoDichiarazioneVO.setIdFase(new Long(rs.getLong("ID_FASE")));
        tipoMotivoDichiarazioneVO.setGtfo(rs.getString("GTFO"));
        tipoMotivoDichiarazioneVO.setTipoDichiarazione(rs
            .getString("TIPO_DICHIARAZIONE"));
        consistenzaVO.setTipoMotivoDichiarazioneVO(tipoMotivoDichiarazioneVO);
        elencoDichiarazioni.add(consistenzaVO);
      }

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "getListDichiarazioniConsistenzaByIdAzienda in ConsistenzaDAO - SQLException: "
              + exc + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .error(
              this,
              "getListDichiarazioniConsistenzaByIdAzienda in ConsistenzaDAO - Generic Exception: "
                  + ex + "\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .error(
                this,
                "getListDichiarazioniConsistenzaByIdAzienda in ConsistenzaDAO - SQLException while closing Statement and Connection: "
                    + exc + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getListDichiarazioniConsistenzaByIdAzienda in ConsistenzaDAO - Generic Exception while closing Statement and Connection: "
                    + ex + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger
        .debug(
            this,
            "Invocated getListDichiarazioniConsistenzaByIdAzienda method in ConsistenzaDAO\n");
    if (elencoDichiarazioni.size() == 0)
    {
      return (ConsistenzaVO[]) elencoDichiarazioni
          .toArray(new ConsistenzaVO[0]);
    }
    else
    {
      return (ConsistenzaVO[]) elencoDichiarazioni
          .toArray(new ConsistenzaVO[elencoDichiarazioni.size()]);
    }
  }
  
  
  
  public ConsistenzaVO[] getListDichiarazioniConsistenzaByIdAziendaVarCat(
      Long idAzienda, String[] orderBy) throws DataAccessException
  {
    SolmrLogger
        .debug(
            this,
            "Invocating getListDichiarazioniConsistenzaByIdAziendaVarCat method in ConsistenzaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ConsistenzaVO> elencoDichiarazioni = new Vector<ConsistenzaVO>();

    try
    {
      SolmrLogger
          .debug(
              this,
              "Creating db-connection in getListDichiarazioniConsistenzaByIdAziendaVarCat method in ConsistenzaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in getListDichiarazioniConsistenzaByIdAziendaVarCat method in ConsistenzaDAO and it values: "
                  + conn + "\n");

      String query = 
          "SELECT DISTINCT DC.ID_DICHIARAZIONE_CONSISTENZA, " +
          "       DC.ID_AZIENDA, " + 
          "       DC.ANNO, " +
          "       DC.DATA, " +
          "       DC.TIPO_CONVALIDA, " +
          "       DC.CODICE_FOTOGRAFIA_TERRENI, " +
          "       DC.ID_PROCEDIMENTO, " +
          "       DC.ID_UTENTE, " +
          "       DC.NOTE, " +
          "       DC.ID_MOTIVO_DICHIARAZIONE, " +
          "       DC.RESPONSABILE, " +
          "       DC.DATA_AGGIORNAMENTO_FASCICOLO, " +
          "       DC.CUAA_VALIDATO, " +
          "       DC.FLAG_ANOMALIA, " +
          "       DC.DATA_INSERIMENTO_DICHIARAZIONE, " +
          "       DC.FLAG_INVIA_USO, " +
          "       DC.ANNO_CAMPAGNA, " +
          "       TMD.ID_MOTIVO_DICHIARAZIONE, " +
          "       TMD.DESCRIZIONE, " +
          "       TMD.DATA_INIZIO_VALIDITA, " +
          "       TMD.DATA_FINE_VALIDITA, " +
          "       TMD.ANNO_CAMPAGNA, " +
          "       TMD.ID_FASE, " +
          "       TMD.GTFO, " +
          "       TMD.TIPO_DICHIARAZIONE " +
          "FROM   DB_DICHIARAZIONE_CONSISTENZA DC, " +
          "       DB_TIPO_MOTIVO_DICHIARAZIONE TMD, " +
          "       DB_NOTIFICA_ENTITA NE, " +
          "       DB_NOTIFICA NO " +
          "WHERE  DC.ID_AZIENDA = ? " +
          "AND    DC.ID_MOTIVO_DICHIARAZIONE = TMD.ID_MOTIVO_DICHIARAZIONE " +
          "AND    DC.ID_DICHIARAZIONE_CONSISTENZA = NE.ID_DICHIARAZIONE_CONSISTENZA " +
          "AND    NE.ID_NOTIFICA = NO.ID_NOTIFICA " +
          "AND    DC.ID_AZIENDA = NO.ID_AZIENDA " +
          "AND    NO.ID_TIPOLOGIA_NOTIFICA = " + SolmrConstants.ID_TIPO_TIPOLOGIA_VARIAZIONECATASTALE;

      String ordinamento = "";
      if (orderBy != null && orderBy.length > 0)
      {
        String criterio = "";
        for (int i = 0; i < orderBy.length; i++)
        {
          if (i == 0)
          {
            criterio = (String) orderBy[i];
          }
          else
          {
            criterio += ", " + (String) orderBy[i];
          }
        }
        ordinamento = "ORDER BY " + criterio;
      }
      if (!ordinamento.equals(""))
      {
        query += ordinamento;
      }

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [ID_AZIENDA] in getListDichiarazioniConsistenzaByIdAziendaVarCat method in ConsistenzaDAO: "
                  + idAzienda + "\n");
      SolmrLogger
          .debug(
              this,
              "Value of parameter 2 [ORDER_BY] in getListDichiarazioniConsistenzaByIdAziendaVarCat method in ConsistenzaDAO: "
                  + orderBy + "\n");

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idAzienda.longValue());

      SolmrLogger.debug(this,
          "Executing getListDichiarazioniConsistenzaByIdAziendaVarCat: " + query
              + "\n");

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        ConsistenzaVO consistenzaVO = new ConsistenzaVO();
        consistenzaVO.setIdDichiarazioneConsistenza(rs
            .getString("ID_DICHIARAZIONE_CONSISTENZA"));
        consistenzaVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        consistenzaVO.setAnno(rs.getString("ANNO"));
        consistenzaVO.setData(rs.getString("DATA"));
        consistenzaVO.setTipoConvalida(rs.getString("TIPO_CONVALIDA"));
        consistenzaVO.setCodiceFotografiaTerreni(new Long(rs
            .getLong("CODICE_FOTOGRAFIA_TERRENI")));
        consistenzaVO.setIdProcedimento(rs.getString("ID_PROCEDIMENTO"));
        consistenzaVO.setIdUtente(rs.getString("ID_UTENTE"));
        consistenzaVO.setNote(rs.getString("NOTE"));
        consistenzaVO.setIdMotivo(rs.getString("ID_MOTIVO_DICHIARAZIONE"));
        consistenzaVO.setResponsabile(rs.getString("RESPONSABILE"));
        consistenzaVO.setDataAggiornamentoFascicolo(rs
            .getDate("DATA_AGGIORNAMENTO_FASCICOLO"));
        consistenzaVO.setCuaaValidato(rs.getString("CUAA_VALIDATO"));
        consistenzaVO.setFlagAnomalia(rs.getString("FLAG_ANOMALIA"));
        consistenzaVO.setDataInserimentoDichiarazione(rs
            .getTimestamp("DATA_INSERIMENTO_DICHIARAZIONE"));
        consistenzaVO.setFlagInviaUso(rs.getString("FLAG_INVIA_USO"));
        consistenzaVO.setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
        TipoMotivoDichiarazioneVO tipoMotivoDichiarazioneVO = new TipoMotivoDichiarazioneVO();
        tipoMotivoDichiarazioneVO.setIdMotivoDichiarazione(new Long(rs
            .getLong("ID_MOTIVO_DICHIARAZIONE")));
        tipoMotivoDichiarazioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoMotivoDichiarazioneVO.setDataInizioValidita(rs
            .getDate("DATA_INIZIO_VALIDITA"));
        tipoMotivoDichiarazioneVO.setDataFineValidita(rs
            .getDate("DATA_FINE_VALIDITA"));
        tipoMotivoDichiarazioneVO
            .setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
        tipoMotivoDichiarazioneVO.setIdFase(new Long(rs.getLong("ID_FASE")));
        tipoMotivoDichiarazioneVO.setGtfo(rs.getString("GTFO"));
        tipoMotivoDichiarazioneVO.setTipoDichiarazione(rs
            .getString("TIPO_DICHIARAZIONE"));
        consistenzaVO.setTipoMotivoDichiarazioneVO(tipoMotivoDichiarazioneVO);
        elencoDichiarazioni.add(consistenzaVO);
      }

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "getListDichiarazioniConsistenzaByIdAziendaVarCat in ConsistenzaDAO - SQLException: "
              + exc + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .error(
              this,
              "getListDichiarazioniConsistenzaByIdAziendaVarCat in ConsistenzaDAO - Generic Exception: "
                  + ex + "\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .error(
                this,
                "getListDichiarazioniConsistenzaByIdAziendaVarCat in ConsistenzaDAO - SQLException while closing Statement and Connection: "
                    + exc + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getListDichiarazioniConsistenzaByIdAziendaVarCat in ConsistenzaDAO - Generic Exception while closing Statement and Connection: "
                    + ex + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger
        .debug(
            this,
            "Invocated getListDichiarazioniConsistenzaByIdAziendaVarCat method in ConsistenzaDAO\n");
    if (elencoDichiarazioni.size() == 0)
    {
      return (ConsistenzaVO[]) elencoDichiarazioni
          .toArray(new ConsistenzaVO[0]);
    }
    else
    {
      return (ConsistenzaVO[]) elencoDichiarazioni
          .toArray(new ConsistenzaVO[elencoDichiarazioni.size()]);
    }
  }

  /**
   * Metodo utilizzato per ripristinare il piano di riferimento con la
   * dichiarazione di consistenza selezionata
   * 
   * @param idDichiarazioneConsistenza
   * @param idUtentte
   * 
   * @throws SolmrException
   * @throws DataAccessException
   */
  public void ripristinaPianoRiferimento(Long idDichiarazioneConsistenza,
      Long idUtente) throws SolmrException, DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating ripristinaPianoRiferimento method in ConsistenzaDAO\n");
    Connection conn = null;
    CallableStatement cs = null;
    String msgErr = null;
    String errorCode = null;
    String command = "{call PACK_RIPRISTINA_DICHIARAZIONE.RIPRISTINA_DICHIARAZIONE(?,?,?,?)}";
    try
    {
      SolmrLogger
          .debug(
              this,
              "Creating db-connection in ripristinaPianoRiferimento method in ConsistenzaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in ripristinaPianoRiferimento method in ConsistenzaDAO and it values: "
                  + conn + "\n");

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [ID_DICHIARAZIONE_CONSISTENZA] in ripristinaPianoRiferimento method in ConsistenzaDAO: "
                  + idDichiarazioneConsistenza + "\n");
      SolmrLogger
          .debug(
              this,
              "Value of parameter 2 [ID_UTENTE] in ripristinaPianoRiferimento method in ConsistenzaDAO: "
                  + idUtente + "\n");

      cs = conn.prepareCall(command);
      cs.setLong(1, idDichiarazioneConsistenza.longValue());
      cs.setLong(2, idUtente.longValue());
      cs.registerOutParameter(3, Types.VARCHAR);
      cs.registerOutParameter(4, Types.VARCHAR);

      // Imposto il timeout
      cs
          .setQueryTimeout(SolmrConstants.MAX_TIME_WAIT_PACK_RIPRISTINA_DICHIARAZIONE);

      cs.executeUpdate();
      msgErr = cs.getString(3);
      errorCode = cs.getString(4);

      if (Validator.isNotEmpty(errorCode))
      {
        throw new SolmrException(msgErr);
      }

      cs.close();
      conn.close();
    }
    catch (SolmrException se)
    {
      SolmrLogger.error(this,
          "ripristinaPianoRiferimento in ConsistenzaDAO - SolmrException: "
              + se + "\n");
      throw new SolmrException(
          AnagErrors.ERR_KO_PLSQL_PACK_RIPRISTINA_DICHIARAZIONE + errorCode
              + ": " + msgErr);
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "ripristinaPianoRiferimento in ConsistenzaDAO - SQLException: " + exc
              + "\n");
      if (exc.getErrorCode() == SolmrConstants.ORACLE_PREPARE_STATEMENT_TIME_OUT)
      {
        throw new SolmrException(
            AnagErrors.ERR_GENERIC_KO_PLSQL_PACK_RIPRISTINA_DICHIARAZIONE);

      }
      else
      {
        SolmrLogger.error(this,
            "ripristinaPianoRiferimento in ConsistenzaDAO - SQLException: "
                + exc + "\n");
        throw new SolmrException(
            AnagErrors.ERR_KO_PLSQL_PACK_RIPRISTINA_DICHIARAZIONE
                + exc.getMessage());
      }
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "ripristinaPianoRiferimento in ConsistenzaDAO - Generic Exception: "
              + ex + "\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (cs != null)
        {
          cs.close();
        }
        if (conn != null)
        {
          conn.close();
        }
      }
      catch (SQLException exc)
      {
        SolmrLogger.error(this,
            "ripristinaPianoRiferimento in ConsistenzaDAO - SQLException: "
                + exc + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.error(this,
            "ripristinaPianoRiferimento in ConsistenzaDAO - Generic Exception: "
                + ex + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  /**
   * Metodo per effettuare la modifica di una dichiarazione di consistenza
   * 
   * @param consistenzaVO
   * @throws DataAccessException
   */
  public void modificaDichiarazioneConsistenza(ConsistenzaVO consistenzaVO)
      throws DataAccessException
  {
    SolmrLogger
        .debug(this,
            "Invocating modificaDichiarazioneConsistenza method in ConsistenzaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      SolmrLogger
          .debug(
              this,
              "Creating db-connection in modificaDichiarazioneConsistenza method in ConsistenzaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in modificaDichiarazioneConsistenza method in ConsistenzaDAO and it values: "
                  + conn + "\n");

      String query = " UPDATE DB_DICHIARAZIONE_CONSISTENZA "
          + " SET    ID_AZIENDA = ?, " + "        ANNO = ?, "
          + "        DATA = ?, " + "        TIPO_CONVALIDA = ?, "
          + "        CODICE_FOTOGRAFIA_TERRENI = ?, "
          + "        ID_PROCEDIMENTO = ?, " + "        ID_UTENTE = ?, "
          + "        NOTE = ?, " + "        ID_MOTIVO_DICHIARAZIONE = ?, "
          + "        RESPONSABILE = ?, "
          + "        DATA_AGGIORNAMENTO_FASCICOLO = ?, "
          + "        CUAA_VALIDATO = ?, " + "        FLAG_ANOMALIA = ?, "
          + "        DATA_INSERIMENTO_DICHIARAZIONE = ?, "
          + "        FLAG_INVIA_USO = ?, " + "        ANNO_CAMPAGNA = ?, "
          + "        DATA_PROTOCOLLO = ?, " + "        NUMERO_PROTOCOLLO = ?, "
          +	"        DATA_RICHIESTA_STAMPA = SYSDATE " 
          + " WHERE  ID_DICHIARAZIONE_CONSISTENZA = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing modificaDichiarazioneConsistenza: "
          + query);

      stmt.setLong(1, consistenzaVO.getIdAzienda().longValue());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [ID_AZIENDA] in method modificaDichiarazioneConsistenza in ConsistenzaDAO: "
                  + consistenzaVO.getIdAzienda().longValue() + "\n");
      stmt.setString(2, consistenzaVO.getAnno());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 2 [ANNO] in method modificaDichiarazioneConsistenza in ConsistenzaDAO: "
                  + consistenzaVO.getAnno() + "\n");
      stmt.setTimestamp(3, new Timestamp(consistenzaVO.getDataDichiarazione()
          .getTime()));
      SolmrLogger
          .debug(
              this,
              "Value of parameter 3 [DATA] in method modificaDichiarazioneConsistenza in ConsistenzaDAO: "
                  + new Timestamp(consistenzaVO.getDataDichiarazione()
                      .getTime()) + "\n");
      stmt.setString(4, consistenzaVO.getTipoConvalida());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 4 [TIPO_CONVALIDA] in method modificaDichiarazioneConsistenza in ConsistenzaDAO: "
                  + consistenzaVO.getTipoConvalida() + "\n");
      stmt.setString(5, String.valueOf(consistenzaVO
          .getCodiceFotografiaTerreni().longValue()));
      SolmrLogger
          .debug(
              this,
              "Value of parameter 5 [CODICE_FOTOGRAFIA_TERRENI] in method modificaDichiarazioneConsistenza in ConsistenzaDAO: "
                  + consistenzaVO.getCodiceFotografiaTerreni() + "\n");
      stmt.setString(6, consistenzaVO.getIdProcedimento());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 6 [ID_PROCEDIMENTO] in method modificaDichiarazioneConsistenza in ConsistenzaDAO: "
                  + consistenzaVO.getIdProcedimento() + "\n");
      stmt.setString(7, consistenzaVO.getIdUtente());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 7 [ID_UTENTE] in method modificaDichiarazioneConsistenza in ConsistenzaDAO: "
                  + consistenzaVO.getIdUtente() + "\n");
      stmt.setString(8, consistenzaVO.getNote());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 8 [NOTE] in method modificaDichiarazioneConsistenza in ConsistenzaDAO: "
                  + consistenzaVO.getNote() + "\n");
      stmt.setString(9, consistenzaVO.getIdMotivo());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 9 [ID_MOTIVO_DICHIARAZIONE] in method modificaDichiarazioneConsistenza in ConsistenzaDAO: "
                  + consistenzaVO.getIdMotivo() + "\n");
      stmt.setString(10, consistenzaVO.getResponsabile());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 10 [RESPONSABILE] in method modificaDichiarazioneConsistenza in ConsistenzaDAO: "
                  + consistenzaVO.getResponsabile() + "\n");
      if (consistenzaVO.getDataAggiornamentoFascicolo() != null)
      {
        stmt.setTimestamp(11, new Timestamp(consistenzaVO
            .getDataAggiornamentoFascicolo().getTime()));
        SolmrLogger
            .debug(
                this,
                "Value of parameter 11 [DATA_AGGIORNAMENTO_FASCICOLO] in method modificaDichiarazioneConsistenza in ConsistenzaDAO: "
                    + new Timestamp(consistenzaVO
                        .getDataAggiornamentoFascicolo().getTime()) + "\n");
      }
      else
      {
        stmt.setString(11, null);
        SolmrLogger
            .debug(
                this,
                "Value of parameter 11 [DATA_AGGIORNAMENTO_FASCICOLO] in method modificaDichiarazioneConsistenza in ConsistenzaDAO: "
                    + null + "\n");
      }
      stmt.setString(12, consistenzaVO.getCuaaValidato());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 12 [CUAA_VALIDATO] in method modificaDichiarazioneConsistenza in ConsistenzaDAO: "
                  + consistenzaVO.getCuaaValidato() + "\n");
      stmt.setString(13, consistenzaVO.getFlagAnomalia());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 13 [FLAG_ANOMALIA] in method modificaDichiarazioneConsistenza in ConsistenzaDAO: "
                  + consistenzaVO.getFlagAnomalia() + "\n");
      stmt.setTimestamp(14, new Timestamp(consistenzaVO
          .getDataInserimentoDichiarazione().getTime()));
      SolmrLogger
          .debug(
              this,
              "Value of parameter 14 [DATA_INSERIMENTO_DICHIARAZIONE] in method modificaDichiarazioneConsistenza in ConsistenzaDAO: "
                  + new Timestamp(consistenzaVO
                      .getDataInserimentoDichiarazione().getTime()) + "\n");
      stmt.setString(15, consistenzaVO.getFlagInviaUso());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 15 [FLAG_INVIA_USO] in method modificaDichiarazioneConsistenza in ConsistenzaDAO: "
                  + consistenzaVO.getFlagInviaUso() + "\n");
      stmt.setString(16, consistenzaVO.getAnnoCampagna());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 16 [ANNO_CAMPAGNA] in method modificaDichiarazioneConsistenza in ConsistenzaDAO: "
                  + consistenzaVO.getAnnoCampagna() + "\n");
      if (consistenzaVO.getDataProtocollo() != null)
      {
        stmt.setTimestamp(17, new Timestamp(consistenzaVO.getDataProtocollo()
            .getTime()));
        SolmrLogger
            .debug(
                this,
                "Value of parameter 17 [DATA_PROTOCOLLO] in method modificaDichiarazioneConsistenza in ConsistenzaDAO: "
                    + new Timestamp(consistenzaVO.getDataProtocollo().getTime())
                    + "\n");
      }
      else
      {
        stmt.setString(17, null);
        SolmrLogger
            .debug(
                this,
                "Value of parameter 17 [DATA_PROTOCOLLO] in method modificaDichiarazioneConsistenza in ConsistenzaDAO: "
                    + null + "\n");
      }
      stmt.setString(18, consistenzaVO.getNumeroProtocollo());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 18 [NUMERO_PROTOCOLLO] in method modificaDichiarazioneConsistenza in ConsistenzaDAO: "
                  + consistenzaVO.getNumeroProtocollo() + "\n");
      stmt.setLong(19, Long.decode(
          consistenzaVO.getIdDichiarazioneConsistenza()).longValue());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 19 [ID_DICHIARAZIONE_CONSISTENZA] in method modificaDichiarazioneConsistenza in ConsistenzaDAO: "
                  + consistenzaVO.getIdDichiarazioneConsistenza() + "\n");

      stmt.executeUpdate();

      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "modificaDichiarazioneConsistenza in ConsistenzaDAO - SQLException: "
              + exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "modificaDichiarazioneConsistenza in ConsistenzaDAO - Generic Exception: "
              + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
        {
          stmt.close();
        }
        if (conn != null)
        {
          conn.close();
        }
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .error(
                this,
                "modificaDichiarazioneConsistenza in ConsistenzaDAO - SQLException while closing Statement and Connection: "
                    + exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "modificaDichiarazioneConsistenza in ConsistenzaDAO - Generic Exception while closing Statement and Connection: "
                    + ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger
        .debug(this,
            "Invocated modificaDichiarazioneConsistenza method in ConsistenzaDAO\n");
  }

  /**
   * Metodo per recuperare la dichiarazione di consistenza a partire dalla sua
   * chiave primaria
   * 
   * @param idDichiarazioneConsistenza
   * @return it.csi.solmr.dto.anag.ConsistenzaVO
   * @throws DataAccessException
   */
  public ConsistenzaVO findDichiarazioneConsistenzaByPrimaryKey(
      Long idDichiarazioneConsistenza) throws DataAccessException
  {
    SolmrLogger
        .debug(
            this,
            "Invocating findDichiarazioneConsistenzaByPrimaryKey method in ConsistenzaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    ConsistenzaVO consistenzaVO = null;

    try
    {
      SolmrLogger
          .debug(
              this,
              "Creating db-connection in findDichiarazioneConsistenzaByPrimaryKey method in ConsistenzaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in findDichiarazioneConsistenzaByPrimaryKey method in ConsistenzaDAO and it values: "
                  + conn + "\n");

      String query = 
          "WITH ALLEGATO AS " +
          "  (SELECT AL.ID_ALLEGATO, " +
          "          AL.ID_TIPO_FIRMA, " +
          "          DC.ID_DICHIARAZIONE_CONSISTENZA, " +
          "          AD.ID_ALLEGATO_DICHIARAZIONE " +
          "   FROM   DB_DICHIARAZIONE_CONSISTENZA DC, " +
          "          DB_ALLEGATO_DICHIARAZIONE AD, " +
          "          DB_ALLEGATO AL " +
          "   WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
          "   AND    DC.ID_DICHIARAZIONE_CONSISTENZA = AD.ID_DICHIARAZIONE_CONSISTENZA " +
          "   AND    AD.DATA_FINE_VALIDITA IS NULL " +
          "   AND    AD.ID_ALLEGATO = AL.ID_ALLEGATO " +
          "   AND    AL.ID_TIPO_ALLEGATO = 10 ) " +          
          "SELECT DC.ID_DICHIARAZIONE_CONSISTENZA, " +
          "       DC.ID_AZIENDA, " +
          "       DC.ANNO, " +
          "       DC.DATA, " +
          "       DC.TIPO_CONVALIDA, " +
          "       DC.CODICE_FOTOGRAFIA_TERRENI, " +
          "       DC.ID_PROCEDIMENTO, " +
          "       DC.ID_UTENTE, " +
          "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
          "          || ' ' " + 
          "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
          "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
          "         WHERE DC.ID_UTENTE = PVU.ID_UTENTE_LOGIN) " + 
          "       || ' ( ' || " +
          "       (SELECT PVU.DENOMINAZIONE " +
          "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
          "        WHERE DC.ID_UTENTE = PVU.ID_UTENTE_LOGIN) || ' ) ' AS UTENTE_AGGIORNAMENTO, " +
          "       DC.NOTE, " +
          "       DC.ID_MOTIVO_DICHIARAZIONE, " +
          "       TMD.DESCRIZIONE AS MOTIVO_DESC, " +
          "       TMD.SCHEDULA_FASCICOLI, " +
          "       TMD.TIPO_DICHIARAZIONE, " +
          "       TMD.EXT_ID_TIPO_DOCUMENTO_INDEX, " +
          "       DC.RESPONSABILE, " +
          "       DC.DATA_AGGIORNAMENTO_FASCICOLO, " +
          "       DC.CUAA_VALIDATO, " +
          "       DC.FLAG_ANOMALIA, " +
          "       DC.DATA_AGGIORNAMENTO_UV, " +
          "       DC.FLAG_ANOMALIA_UV, " +
          "       DC.DATA_AGGIORNAMENTO_COLTURA, " + 
          "       DC.FLAG_ANOMALIA_COLTURA, " +
          "       DC.DATA_AGGIORNAMENTO_FABBRICATI, " +
          "       DC.FLAG_ANOMALIA_FABBRICATI, " +
          "       DC.DATA_AGGIORNAMENTO_CC, " +
          "       DC.FLAG_ANOMALIA_CC, " +
          "       DC.DATA_INSERIMENTO_DICHIARAZIONE, " +
          "       DC.FLAG_INVIA_USO, " +
          "       DC.ANNO_CAMPAGNA, " +
          "       DC.DATA_PROTOCOLLO, " + 
          "       DC.NUMERO_PROTOCOLLO, " +
          "       DC.FLAG_INVIO_MAIL, " +
          "       AL.ID_ALLEGATO, " +
          "       AL.ID_TIPO_FIRMA, " +
          "       AL.ID_ALLEGATO_DICHIARAZIONE " +
          "FROM   DB_DICHIARAZIONE_CONSISTENZA DC, " +
          "       DB_TIPO_MOTIVO_DICHIARAZIONE TMD, " +
          //"       PAPUA_V_UTENTE_LOGIN PVU, " +
          "       ALLEGATO AL " +
          "WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
          "AND    DC.ID_MOTIVO_DICHIARAZIONE = TMD.ID_MOTIVO_DICHIARAZIONE " +
          //"AND    DC.ID_UTENTE = PVU.ID_UTENTE_LOGIN " +
          "AND    DC.ID_DICHIARAZIONE_CONSISTENZA = AL.ID_DICHIARAZIONE_CONSISTENZA (+) ";

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [ID_DICHIARAZIONE_CONSISTENZA] in findDichiarazioneConsistenzaByPrimaryKey method in ConsistenzaDAO: "
                  + idDichiarazioneConsistenza + "\n");

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idDichiarazioneConsistenza.longValue());
      stmt.setLong(2, idDichiarazioneConsistenza.longValue());

      SolmrLogger
          .debug(this, "Executing findDichiarazioneConsistenzaByPrimaryKey: "
              + query + "\n");

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        consistenzaVO = new ConsistenzaVO();
        consistenzaVO.setIdDichiarazioneConsistenza(rs
            .getString("ID_DICHIARAZIONE_CONSISTENZA"));
        consistenzaVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        consistenzaVO.setAnno(rs.getString("ANNO"));
        consistenzaVO.setData(DateUtils.formatDateTimeNotNull(rs.getTimestamp("DATA")));
        consistenzaVO.setDataDichiarazione(new java.util.Date(rs.getTimestamp(
            "DATA").getTime()));
        consistenzaVO.setTipoConvalida(rs.getString("TIPO_CONVALIDA"));
        consistenzaVO.setCodiceFotografiaTerreni(new Long(rs
            .getLong("CODICE_FOTOGRAFIA_TERRENI")));
        consistenzaVO.setIdProcedimento(rs.getString("ID_PROCEDIMENTO"));
        consistenzaVO.setIdUtente(rs.getString("ID_UTENTE"));
        consistenzaVO.setUtenteAggiornamento(rs
            .getString("UTENTE_AGGIORNAMENTO"));
        consistenzaVO.setNote(rs.getString("NOTE"));
        consistenzaVO.setIdMotivo(rs.getString("ID_MOTIVO_DICHIARAZIONE"));
        consistenzaVO.setMotivo(rs.getString("MOTIVO_DESC"));
        TipoMotivoDichiarazioneVO tipoMotivoDichiarazioneVO = new TipoMotivoDichiarazioneVO();
        tipoMotivoDichiarazioneVO.setTipoDichiarazione(rs.getString("TIPO_DICHIARAZIONE"));
        consistenzaVO.setTipoMotivoDichiarazioneVO(tipoMotivoDichiarazioneVO);
        consistenzaVO.setResponsabile(rs.getString("RESPONSABILE"));
        consistenzaVO.setDataAggiornamentoFascicolo(rs
            .getTimestamp("DATA_AGGIORNAMENTO_FASCICOLO"));
        consistenzaVO.setCuaaValidato(rs.getString("CUAA_VALIDATO"));
        consistenzaVO.setFlagAnomalia(rs.getString("FLAG_ANOMALIA"));
        consistenzaVO.setDataAggiornamentoUV(rs
            .getTimestamp("DATA_AGGIORNAMENTO_UV"));
        consistenzaVO.setFlagAnomaliaUV(rs.getString("FLAG_ANOMALIA_UV"));
        consistenzaVO.setDataAggiornamentoColtura(rs
            .getTimestamp("DATA_AGGIORNAMENTO_COLTURA"));
        consistenzaVO.setFlagAnomaliaColtura(rs.getString("FLAG_ANOMALIA_COLTURA"));
        consistenzaVO.setDataAggiornamentoFabbricati(rs
            .getTimestamp("DATA_AGGIORNAMENTO_FABBRICATI"));
        consistenzaVO.setFlagAnomaliaFabbricati(rs.getString("FLAG_ANOMALIA_FABBRICATI"));
        consistenzaVO.setDataAggiornamentoCC(rs
            .getTimestamp("DATA_AGGIORNAMENTO_CC"));
        consistenzaVO.setFlagAnomaliaCC(rs.getString("FLAG_ANOMALIA_CC"));
        consistenzaVO.setDataInserimentoDichiarazione(rs
            .getTimestamp("DATA_INSERIMENTO_DICHIARAZIONE"));
        consistenzaVO.setFlagInviaUso(rs.getString("FLAG_INVIA_USO"));
        consistenzaVO.setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
        consistenzaVO.setDataProtocollo(rs.getTimestamp("DATA_PROTOCOLLO"));
        consistenzaVO.setNumeroProtocollo(rs.getString("NUMERO_PROTOCOLLO"));
        consistenzaVO.setSchedulaFascicoli(rs.getString("SCHEDULA_FASCICOLI"));
        consistenzaVO.setFlagInvioMail(rs.getString("FLAG_INVIO_MAIL"));
        consistenzaVO.setExtIdTipoDocumentoIndex(checkLongNull(rs.getString("EXT_ID_TIPO_DOCUMENTO_INDEX")));
        consistenzaVO.setIdAllegato(checkLongNull(rs.getString("ID_ALLEGATO")));
        consistenzaVO.setIdTipoFirma(checkLongNull(rs.getString("ID_TIPO_FIRMA")));
        consistenzaVO.setIdAllegatoDichiarazione(checkLongNull(rs.getString("ID_ALLEGATO_DICHIARAZIONE")));
        
      }

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "findDichiarazioneConsistenzaByPrimaryKey in ConsistenzaDAO - SQLException: "
              + exc + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .error(
              this,
              "findDichiarazioneConsistenzaByPrimaryKey in ConsistenzaDAO - Generic Exception: "
                  + ex + "\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .error(
                this,
                "findDichiarazioneConsistenzaByPrimaryKey in ConsistenzaDAO - SQLException while closing Statement and Connection: "
                    + exc + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "findDichiarazioneConsistenzaByPrimaryKey in ConsistenzaDAO - Generic Exception while closing Statement and Connection: "
                    + ex + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger
        .debug(this,
            "Invocated findDichiarazioneConsistenzaByPrimaryKey method in ConsistenzaDAO\n");
    return consistenzaVO;
  }

  /**
   * Metodo che mi consente di recuperare la dichiarazione di consistenza
   * relativa alla conduzione dichiarata selezionata
   * 
   * @param idConduzioneDichiarata
   * @return it.csi.solmr.dto.anag.ConsistenzaVO
   * @throws DataAccessException
   */
  public ConsistenzaVO findDichiarazioneConsistenzaByIdConduzioneDichiarata(
      Long idConduzioneDichiarata) throws DataAccessException
  {
    SolmrLogger
        .debug(
            this,
            "Invocating findDichiarazioneConsistenzaByIdConduzioneDichiarata method in ConsistenzaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    ConsistenzaVO consistenzaVO = null;

    try
    {
      SolmrLogger
          .debug(
              this,
              "Creating db-connection in findDichiarazioneConsistenzaByIdConduzioneDichiarata method in ConsistenzaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in findDichiarazioneConsistenzaByIdConduzioneDichiarata method in ConsistenzaDAO and it values: "
                  + conn + "\n");

      String query = " SELECT DC.ID_DICHIARAZIONE_CONSISTENZA, "
          + "        DC.ID_AZIENDA, "
          + "        DC.ANNO, "
          + "        DC.DATA, "
          + "        DC.TIPO_CONVALIDA, "
          + "        DC.CODICE_FOTOGRAFIA_TERRENI, "
          + "        DC.ID_PROCEDIMENTO, "
          + "        DC.ID_UTENTE, "+
          "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
          "          || ' ' " + 
          "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
          "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
          "         WHERE DC.ID_UTENTE = PVU.ID_UTENTE_LOGIN) " + 
          "          || ' ( ' || " +
          "       (SELECT PVU.DENOMINAZIONE " +
          "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
          "        WHERE DC.ID_UTENTE = PVU.ID_UTENTE_LOGIN) " +
          "          || ' ) ' AS UTENTE_AGGIORNAMENTO, "
          + "        DC.NOTE, "
          + "        DC.ID_MOTIVO_DICHIARAZIONE, "
          + "        TMD.DESCRIZIONE AS MOTIVO_DESC, "
          + "        DC.RESPONSABILE, "
          + "        DC.DATA_AGGIORNAMENTO_FASCICOLO, "
          + "        DC.CUAA_VALIDATO, "
          + "        DC.FLAG_ANOMALIA, "
          + "        DC.DATA_INSERIMENTO_DICHIARAZIONE, "
          + "        DC.FLAG_INVIA_USO, "
          + "        DC.ANNO_CAMPAGNA, "
          + "        DC.DATA_PROTOCOLLO, "
          + "        DC.NUMERO_PROTOCOLLO "
          + " FROM   DB_DICHIARAZIONE_CONSISTENZA DC, "
          + "        DB_CONDUZIONE_DICHIARATA CD, "
          + "        DB_TIPO_MOTIVO_DICHIARAZIONE TMD "
          //+ "        PAPUA_V_UTENTE_LOGIN PVU "
          + " WHERE  CD.ID_CONDUZIONE_DICHIARATA = ? "
          + " AND    CD.CODICE_FOTOGRAFIA_TERRENI = DC.CODICE_FOTOGRAFIA_TERRENI "
          + " AND    DC.ID_MOTIVO_DICHIARAZIONE = TMD.ID_MOTIVO_DICHIARAZIONE ";
          //+ " AND    DC.ID_UTENTE = PVU.ID_UTENTE_LOGIN ";

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [ID_CONDUZIONE_DICHIARATA] in findDichiarazioneConsistenzaByIdConduzioneDichiarata method in ConsistenzaDAO: "
                  + idConduzioneDichiarata + "\n");

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idConduzioneDichiarata.longValue());

      SolmrLogger.debug(this,
          "Executing findDichiarazioneConsistenzaByIdConduzioneDichiarata: "
              + query + "\n");

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        consistenzaVO = new ConsistenzaVO();
        consistenzaVO.setIdDichiarazioneConsistenza(rs
            .getString("ID_DICHIARAZIONE_CONSISTENZA"));
        consistenzaVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        consistenzaVO.setAnno(rs.getString("ANNO"));
        consistenzaVO.setDataDichiarazione(new java.util.Date(rs.getTimestamp(
            "DATA").getTime()));
        consistenzaVO.setTipoConvalida(rs.getString("TIPO_CONVALIDA"));
        consistenzaVO.setCodiceFotografiaTerreni(new Long(rs
            .getLong("CODICE_FOTOGRAFIA_TERRENI")));
        consistenzaVO.setIdProcedimento(rs.getString("ID_PROCEDIMENTO"));
        consistenzaVO.setIdUtente(rs.getString("ID_UTENTE"));
        consistenzaVO.setUtenteAggiornamento(rs
            .getString("UTENTE_AGGIORNAMENTO"));
        consistenzaVO.setNote(rs.getString("NOTE"));
        consistenzaVO.setIdMotivo(rs.getString("ID_MOTIVO_DICHIARAZIONE"));
        consistenzaVO.setMotivo(rs.getString("MOTIVO_DESC"));
        consistenzaVO.setResponsabile(rs.getString("RESPONSABILE"));
        consistenzaVO.setDataAggiornamentoFascicolo(rs
            .getTimestamp("DATA_AGGIORNAMENTO_FASCICOLO"));
        consistenzaVO.setCuaaValidato(rs.getString("CUAA_VALIDATO"));
        consistenzaVO.setFlagAnomalia(rs.getString("FLAG_ANOMALIA"));
        consistenzaVO.setDataInserimentoDichiarazione(rs
            .getTimestamp("DATA_INSERIMENTO_DICHIARAZIONE"));
        consistenzaVO.setFlagInviaUso(rs.getString("FLAG_INVIA_USO"));
        consistenzaVO.setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
        consistenzaVO.setDataProtocollo(rs.getTimestamp("DATA_PROTOCOLLO"));
        consistenzaVO.setNumeroProtocollo(rs.getString("NUMERO_PROTOCOLLO"));
      }

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger
          .error(
              this,
              "findDichiarazioneConsistenzaByIdConduzioneDichiarata in ConsistenzaDAO - SQLException: "
                  + exc + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .error(
              this,
              "findDichiarazioneConsistenzaByIdConduzioneDichiarata in ConsistenzaDAO - Generic Exception: "
                  + ex + "\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .error(
                this,
                "findDichiarazioneConsistenzaByIdConduzioneDichiarata in ConsistenzaDAO - SQLException while closing Statement and Connection: "
                    + exc + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "findDichiarazioneConsistenzaByIdConduzioneDichiarata in ConsistenzaDAO - Generic Exception while closing Statement and Connection: "
                    + ex + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger
        .debug(
            this,
            "Invocated findDichiarazioneConsistenzaByIdConduzioneDichiarata method in ConsistenzaDAO\n");
    return consistenzaVO;
  }

  /**
   * La dichiarazione di consistenza non sia antecedente al 2007 (deve essere
   * DATA_INSERIMENTO_DICHIARAZIONE>=2007). Se la dichiarazione verifica la
   * condizione ritorno l'anno della dichiarazione, in caso contrario ritorno
   * NULL
   */
  public Long getAnnoDichiarazione(Long idDichiarazioneConsistenza)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Long annoDichiarazione = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT DC.ANNO "
          + "FROM DB_DICHIARAZIONE_CONSISTENZA DC "
          + "WHERE DC.ID_DICHIARAZIONE_CONSISTENZA = ? "
          + "AND TO_CHAR(DC.DATA_INSERIMENTO_DICHIARAZIONE,'yyyy') >= 2007 ";

      SolmrLogger.debug(this, "Executing getAnnoDichiarazione: " + query);
      
      stmt = conn.prepareStatement(query);

      if (idDichiarazioneConsistenza != null)
        SolmrLogger.debug(this, "idDichiarazioneConsistenza: "
            + idDichiarazioneConsistenza.longValue());

      stmt.setLong(1, idDichiarazioneConsistenza.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        annoDichiarazione = new Long(rs.getLong("ANNO"));
      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getAnnoDichiarazione - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(this, "getAnnoDichiarazione - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this,
            "getAnnoDichiarazione - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getAnnoDichiarazione - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return annoDichiarazione;
  }

  /**
   * Questo metodo va a lavorare sulle pratiche legate alla dichiarazione di
   * consistenza. Il suo scopo è quello di verificare se ci sono pratiche legate
   * alla dichiarazione o meno. E se ci sono se sono legate al PSR o RPU.
   * 
   * @param idAzienda
   * @param idDichiarazioneConsistenza
   * @return restituisce null se non vengono trovate pratiche
   * @throws DataAccessException
   */
  public Long getProcedimento(Long idAzienda, Long idDichiarazioneConsistenza)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Long idProcedimento = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT PA.ID_PROCEDIMENTO "
          + "FROM DB_PROCEDIMENTO_AZIENDA PA "
          + "WHERE PA.ID_DICHIARAZIONE_CONSISTENZA = ? "
          + "AND PA.ID_AZIENDA = ? ";


      SolmrLogger.debug(this, "Executing getProcedimento: " + query);
      
      stmt = conn.prepareStatement(query);

      if (idAzienda != null)
        SolmrLogger.debug(this, "idAzienda: " + idAzienda.longValue());
      if (idDichiarazioneConsistenza != null)
        SolmrLogger.debug(this, "idDichiarazioneConsistenza: "
            + idDichiarazioneConsistenza.longValue());

      stmt.setLong(1, idDichiarazioneConsistenza.longValue());
      stmt.setLong(2, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      boolean trovato = false;
      while (rs.next() && !trovato)
      {
        idProcedimento = new Long(rs.getLong("ID_PROCEDIMENTO"));
        if (idProcedimento.longValue() == SolmrConstants.ID_PROCEDIMENTO_PSR
            || idProcedimento.longValue() == SolmrConstants.ID_PROCEDIMENTO_RPU)
          trovato = true;
      }
      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getProcedimento - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getProcedimento - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this,
            "getProcedimento - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "getProcedimento - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return idProcedimento;
  }

  /**
   * Questo metodo restituisce l'ultima dichiarazione di consistenza
   * legata ad un'azienda. Se viene valorizzato anche l'anno restituisce 
   * l'ultima dichiarazione di consistenza fatta in quell'anno.
   * @param idAzienda
   * @param anno
   * @return
   * @throws DataAccessException
   */
  public long getLastIdDichiazioneConsistenza(Long idAzienda, Long anno)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    long idDichiazioneConsistenza = 0;
    try
    {
      conn = getDatasource().getConnection();
      
      String query="";
      
      if (anno==null)
      {
        query += "SELECT MAX (DC.ID_DICHIARAZIONE_CONSISTENZA) AS MAX_ID "+
                 "FROM DB_DICHIARAZIONE_CONSISTENZA DC "+
                 "WHERE DC.ID_AZIENDA = ? ";
      }
      else
      {
        //Vado a prendere solo quelle dell'anno e che sono legate a PSR o RPU
        query+= "SELECT MAX (DC.ID_DICHIARAZIONE_CONSISTENZA) AS MAX_ID "+
        		    "FROM DB_DICHIARAZIONE_CONSISTENZA DC,DB_PROCEDIMENTO_AZIENDA PA "+      
        		    "WHERE DC.ID_AZIENDA = ? "+
        		    "AND DC.ANNO = ? "+
        		    "AND PA.ID_AZIENDA=DC.ID_AZIENDA "+
        		    "AND (PA.ID_PROCEDIMENTO = ? OR PA.ID_PROCEDIMENTO = ?) "+
        		    "AND DC.ID_DICHIARAZIONE_CONSISTENZA = PA.ID_DICHIARAZIONE_CONSISTENZA ";
      }
      
      SolmrLogger.debug(this, "Executing getLastIdDichiazioneConsistenza: " + query);
      stmt = conn.prepareStatement(query);

      if (idAzienda != null)
        SolmrLogger.debug(this, "idAzienda: " + idAzienda.longValue());
      if (anno != null)
        SolmrLogger.debug(this, "anno: "
            + anno.longValue());


      stmt.setLong(1, idAzienda.longValue());
      if (anno!=null) 
      {
        stmt.setLong(2, anno.longValue());
        stmt.setLong(3, SolmrConstants.ID_PROCEDIMENTO_RPU);
        stmt.setLong(4, SolmrConstants.ID_PROCEDIMENTO_PSR);
      }


      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        idDichiazioneConsistenza = rs.getLong("MAX_ID");
        
      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getLastIdDichiazioneConsistenza - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getLastIdDichiazioneConsistenza - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this,
            "getLastIdDichiazioneConsistenza - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "getLastIdDichiazioneConsistenza - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return idDichiazioneConsistenza;
  }


  /**
   * Restituisce l'elenco degli id_storico_particella che soddisfano i filtri
   * della ricerca terreni
   * 
   * @param ids
   * @return
   * @throws DataAccessException
   */
  public long[] getElencoAnniDichiarazioniConsistenzaByIdParticella(long idParticella)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<Long> vIds = null;
    long ids[] = null;
    try
    {
      SolmrLogger.debug(this, "[ConsistenzaDAO::getElencoAnniDichiarazioniConsistenzaByIdParticella] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf = new StringBuffer("   SELECT " +
          "     DISTINCT DC.ANNO " +
          "   FROM " +
          "     DB_CONDUZIONE_DICHIARATA CD, " +
          "     DB_DICHIARAZIONE_CONSISTENZA DC " +
          "   WHERE  " +
          "     CD.ID_PARTICELLA = ? " +
          "     AND CD.CODICE_FOTOGRAFIA_TERRENI = DC.CODICE_FOTOGRAFIA_TERRENI " +
          "   ORDER BY ANNO DESC");


      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[ConsistenzaDAO::getElencoAnniDichiarazioniConsistenzaByIdParticella] Query="
            + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idParticella);
      ResultSet rs=stmt.executeQuery();
      vIds = new Vector<Long>();
      while (rs.next())
      {
        vIds.add(new Long(rs.getLong("ANNO")));
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
      { new Parametro("idStoricoParticella", idParticella) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[ConsistenzaDAO::getElencoAnniDichiarazioniConsistenzaByIdParticella] ",
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
      SolmrLogger.debug(this, "[ConsistenzaDAO::getElencoAnniDichiarazioniConsistenzaByIdParticella] END.");
    }
  }
  
  
  /**
   * Recupera le anomalie sui controlli dei terreni.
   * 
   * 
   * @param idAzienda
   * @param idControllo
   * @param vTipoErrori se valorizzato specifica se gli errori devono essere bloccanti o meno
   * @param flagOK se devono anche essere indicati i controlli con esito positivo
   * @return
   * @throws DataAccessException
   */
  public Vector<ErrAnomaliaDicConsistenzaVO> getErroriAnomalieForControlliTerreni(Long idAzienda, Long idControllo, 
      Vector<String> vTipoErrori, boolean flagOK, String ordinamento)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    StringBuffer tipoErrori = null;
    String query = null;
    Vector<ErrAnomaliaDicConsistenzaVO> elencoErrori = null;
    
    try
    {
      SolmrLogger.debug(this, "[ConsistenzaDAO::getErroriAnomalieForControlliTerreni] BEGIN.");
      queryBuf = new StringBuffer();
      
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      if(vTipoErrori !=null)
      {
        tipoErrori = new StringBuffer();
        for (int i = 0; i < vTipoErrori.size(); i++)
        {
          tipoErrori.append("?");
          if (i < (vTipoErrori.size() - 1))
          {
            tipoErrori.append(',');
          }
        }
        
        queryBuf.append(""
            + "SELECT TGC.ID_GRUPPO_CONTROLLO AS ID_GRUPPO_CONTROLLO, "
            + "       TC.ID_CONTROLLO AS ID_CONTROLLO, "
            + "       TC.ORDINAMENTO AS ORDINAMENTO, " 
            + "       TGC.DESCRIZIONE AS DESC_GRUPPO, "
            + "       TC.DESCRIZIONE AS TIPO_ANOMALIA_ERRORE, "
            + "       ECP.DESCRIZIONE AS DESC_ANOMALIA_ERRORE, "
            + "       ECP.BLOCCANTE AS BLOCCANTE, "
            + "       SP.ID_STORICO_PARTICELLA AS ID_STORICO_PARTICELLA, "
            + "       C.DESCOM AS DESCOM, "
            + "       SP.SEZIONE AS SEZIONE, "
            + "       SP.FOGLIO AS FOGLIO, "
            + "       SP.PARTICELLA AS PARTICELLA, "
            + "       SP.SUBALTERNO AS SUBALTERNO, "
            + "       SP.SUP_CATASTALE AS SUP_CATASTALE, " 
            + "       CP.SUPERFICIE_CONDOTTA AS SUPERFICIE_CONDOTTA " 
            + "FROM   DB_ESITO_CONTROLLO_PARTICELLA ECP, "
            + "       DB_UTE UT, "
            + "       DB_CONDUZIONE_PARTICELLA CP, " 
            + "       DB_TIPO_CONTROLLO TC, "
            + "       DB_TIPO_GRUPPO_CONTROLLO TGC, "
            + "       DB_STORICO_PARTICELLA SP, "
            + "       COMUNE C "
            + "WHERE  UT.ID_AZIENDA = ? "
            + "AND    UT.DATA_FINE_ATTIVITA IS NULL "
            + "AND    UT.ID_UTE = CP.ID_UTE "
            + "AND    CP.DATA_FINE_CONDUZIONE IS NULL "
            + "AND    CP.ID_CONDUZIONE_PARTICELLA = ECP.ID_CONDUZIONE_PARTICELLA "  
            + "AND    ECP.ID_CONTROLLO = TC.ID_CONTROLLO "
            + "AND    TC.ID_GRUPPO_CONTROLLO = TGC.ID_GRUPPO_CONTROLLO "  
            + "AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA "
            + "AND    SP.DATA_FINE_VALIDITA IS NULL " 
            + "AND    SP.COMUNE = C.ISTAT_COMUNE ");
          
        if(Validator.isNotEmpty(idControllo))
        {
          queryBuf.append(" AND ECP.ID_CONTROLLO = ? ");
        }
        
        if(Validator.isNotEmpty(vTipoErrori))
        {
          queryBuf.append(" AND ECP.BLOCCANTE IN ( "
                +tipoErrori.toString()+ ") ");
        }
        
      }
      
      if((vTipoErrori !=null) && flagOK)
      {
        queryBuf.append("UNION ALL ");        
      }      
      
      
      if(flagOK)
      {
        
        queryBuf.append("" 
        	+	"SELECT DISTINCT " 
        	+	"        TGCA.ID_GRUPPO_CONTROLLO AS ID_GRUPPO_CONTROLLO, "
        	+ "        TCA.ID_CONTROLLO AS ID_CONTROLLO, "
        	+ "        TCA.ORDINAMENTO AS ORDINAMENTO, "
        	+ "        TGCA.DESCRIZIONE AS DESC_GRUPPO, "
        	+ "        TCA.DESCRIZIONE AS TIPO_ANOMALIA_ERRORE, "
        	+ "        NULL AS DESC_ANOMALIA_ERRORE, "
        	+ "        'P' AS BLOCCANTE, "
        	+ "        NULL AS ID_STORICO_PARTICELLA, "
        	+ "        NULL AS DESCOM, "
        	+ "        NULL AS SEZIONE, "
        	+ "        NULL AS FOGLIO, "
        	+ "        NULL AS PARTICELLA, "
        	+ "        NULL AS SUBALTERNO, "
        	+ "        NULL AS SUP_CATASTALE, "
        	+ "        NULL AS SUPERFICIE_CONDOTTA " 
        	+ "FROM    DB_TIPO_CONTROLLO TCA, DB_TIPO_CONTROLLO_FASE TCFA, " 
        	+ "        DB_TIPO_GRUPPO_CONTROLLO TGCA "
        	+ "WHERE   TCA.ID_CONTROLLO = TCFA.ID_CONTROLLO " 
        	+ "AND     TCA.ID_GRUPPO_CONTROLLO = 4 "
        	+ "AND     TCA.ID_GRUPPO_CONTROLLO = TGCA.ID_GRUPPO_CONTROLLO "
        	+ "AND     TCA.OBBLIGATORIO = 'S' "
        	+ "AND     NOT EXISTS "
        	+ "(SELECT TC.ID_CONTROLLO "
        	+ "FROM    DB_ESITO_CONTROLLO_PARTICELLA ECP, "
        	+ "        DB_UTE UT, "
        	+ "        DB_CONDUZIONE_PARTICELLA CP, " 
        	+ "        DB_TIPO_CONTROLLO TC, "
        	+ "        DB_TIPO_GRUPPO_CONTROLLO TGC, "
        	+ "        DB_STORICO_PARTICELLA SP, "
        	+ "        COMUNE C "
        	+ "WHERE   UT.ID_AZIENDA = ? "
        	+ "AND     TC.ID_CONTROLLO = TCA.ID_CONTROLLO "
        	+ "AND     UT.DATA_FINE_ATTIVITA IS NULL "
        	+ "AND     UT.ID_UTE = CP.ID_UTE "
        	+ "AND     CP.DATA_FINE_CONDUZIONE IS NULL "
        	+ "AND     CP.ID_CONDUZIONE_PARTICELLA = ECP.ID_CONDUZIONE_PARTICELLA " 
        	+ "AND     ECP.ID_CONTROLLO = TC.ID_CONTROLLO "
        	+ "AND     TC.ID_GRUPPO_CONTROLLO = TGC.ID_GRUPPO_CONTROLLO "  
        	+ "AND     CP.ID_PARTICELLA= SP.ID_PARTICELLA "
        	+ "AND     SP.DATA_FINE_VALIDITA IS NULL " 
        	+ "AND     SP.COMUNE=C.ISTAT_COMUNE "
        	+ "AND     ECP.BLOCCANTE IN('N','S') ) ");
        
        if(Validator.isNotEmpty(idControllo))
        {
          queryBuf.append(" AND TCA.ID_CONTROLLO = ? ");
        }

      }
      	
      if(!Validator.isNotEmpty(ordinamento)) 
      {
        queryBuf.append(" ORDER BY ORDINAMENTO, DESCOM, SEZIONE, " 
          + "FOGLIO, PARTICELLA, SUBALTERNO ");
      }
      else 
      {
        
        queryBuf.append("ORDER BY "+ordinamento+", SEZIONE, " 
          + "FOGLIO, PARTICELLA, SUBALTERNO ");
      }
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[ConsistenzaDAO::getErroriAnomalieForControlliTerreni] Query="
            + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      if(vTipoErrori !=null)
      {
        // Setto i parametri della query
        stmt.setLong(++indice, idAzienda.longValue());
       
        
        if(Validator.isNotEmpty(idControllo))
        {
          stmt.setLong(++indice, idControllo.longValue());
        }
        
        if(Validator.isNotEmpty(vTipoErrori))
        {
          for (int i = 0; i < vTipoErrori.size(); i++)
          {
            stmt.setString(++indice, (String)vTipoErrori.get(i));
          }
        }
      }
      
      if(flagOK)
      {
        // Setto i parametri della query
        stmt.setLong(++indice, idAzienda.longValue());
       
        
        if(Validator.isNotEmpty(idControllo))
        {
          stmt.setLong(++indice, idControllo.longValue());
        }
      }
      
      ResultSet rs=stmt.executeQuery();
      
      while (rs.next())
      {        
        if(elencoErrori == null)
        {
          elencoErrori = new Vector<ErrAnomaliaDicConsistenzaVO>();
        }
      
        ErrAnomaliaDicConsistenzaVO err = new ErrAnomaliaDicConsistenzaVO();

        err.setIdGruppoControllo(rs.getString("ID_GRUPPO_CONTROLLO"));
        err.setIdControllo(rs.getString("ID_CONTROLLO"));
        err.setDescGruppoControllo(rs.getString("DESC_GRUPPO"));
        err.setTipoAnomaliaErrore(rs.getString("TIPO_ANOMALIA_ERRORE"));
        err.setDescAnomaliaErrore(rs.getString("DESC_ANOMALIA_ERRORE"));
        String bloccante = rs.getString("BLOCCANTE");
        err.setBloccanteStr(bloccante);
        
        //esite una particella collegata
        if(!(Validator.isNotEmpty(bloccante) && bloccante.equalsIgnoreCase("P")))
        {
          StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
          storicoParticellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
          ComuneVO comuneVO = new ComuneVO();
          comuneVO.setDescom(rs.getString("DESCOM"));
          storicoParticellaVO.setComuneParticellaVO(comuneVO);
          
          storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
          storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
          storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
          storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
          storicoParticellaVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
          err.setStoricoParticellaVO(storicoParticellaVO);
          ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
          conduzioneParticellaVO.setSuperficieCondotta(rs.getString("SUPERFICIE_CONDOTTA"));
          err.setConduzioneParticellaVO(conduzioneParticellaVO);
          
        }

        // Controllo se è un errore bloccante o se è solo un'anomalia
        if ("S".equals(err.getBloccanteStr()))
          err.setBloccante(true);
        else
          err.setBloccante(false);

       
        elencoErrori.add(err);
      }
      
      return elencoErrori;
    }      
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("elencoErrori", elencoErrori) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda), new Parametro("idControllo", idControllo),
        new Parametro("idAzienda", idAzienda) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[ConsistenzaDAO::getErroriAnomalieForControlliTerreni] ",
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
      SolmrLogger.debug(this, "[ConsistenzaDAO::getErroriAnomalieForControlliTerreni] END.");
    }
      

      
  }
  
  
  /**
   * Restituisce l'anno campagna associato 
   * all'ultima dichiarazione di consisitenza
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public String getLastAnnoCampagnaFromDichCons(long idAzienda) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    String anno_campagna = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ConsistenzaDAO::getLastAnnoCampagnaFromDichCons] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("" +
          		"SELECT " + 
          		"        TMD.ANNO_CAMPAGNA " + 
          		"FROM " + 
          		"        DB_DICHIARAZIONE_CONSISTENZA DAC, " +
          		"        DB_TIPO_MOTIVO_DICHIARAZIONE TMD " +
          		"WHERE " + 
          		"        DAC.ID_MOTIVO_DICHIARAZIONE = TMD.ID_MOTIVO_DICHIARAZIONE " +
          		"AND     DAC.ID_AZIENDA = ? " +
          		"AND     DATA_INSERIMENTO_DICHIARAZIONE = " +
          		"            ( SELECT MAX(DATA_INSERIMENTO_DICHIARAZIONE)  " + 
          		"              FROM   DB_DICHIARAZIONE_CONSISTENZA " +  
          		"              WHERE  ID_AZIENDA = ? )");
              
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ConsistenzaDAO::getLastAnnoCampagnaFromDichCons] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int idx = 0;
      stmt.setLong(++idx,idAzienda);
      stmt.setLong(++idx,idAzienda);
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {
        anno_campagna = rs.getString("ANNO_CAMPAGNA");
      }
      
      return anno_campagna;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("anno_campagna", anno_campagna) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[ConsistenzaDAO::getLastAnnoCampagnaFromDichCons] ",
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
              "[ConsistenzaDAO::getLastAnnoCampagnaFromDichCons] END.");
    }
  }
  
  
  /**
   * Restituisce l'anno dell'ultima dichiarazione di 
   * consistenza non correttiva
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public String getLastDichConsNoCorrettiva(long idAzienda) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    String anno = null;
    java.util.Date dataInserimentoDichiarazione = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ConsistenzaDAO::getLastDichConsNoCorrettiva] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("" +
              "SELECT " +
              "       MAX(DIC.DATA_INSERIMENTO_DICHIARAZIONE) AS DATA " +
              "FROM   DB_DICHIARAZIONE_CONSISTENZA DIC, " +
              "       DB_TIPO_MOTIVO_DICHIARAZIONE TMC " +
              "WHERE  DIC.ID_AZIENDA = ? " +
              "AND    DIC.ID_MOTIVO_DICHIARAZIONE = TMC.ID_MOTIVO_DICHIARAZIONE " +
              "AND    DIC.ID_MOTIVO_DICHIARAZIONE <> 7 " + 
              "AND    TMC.TIPO_DICHIARAZIONE <> 'C' ");
              
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ConsistenzaDAO::getLastDichConsNoCorrettiva] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int idx = 0;
      stmt.setLong(++idx,idAzienda);
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {
        dataInserimentoDichiarazione = rs.getDate("DATA");
      }
      
      if(Validator.isNotEmpty(dataInserimentoDichiarazione))
      {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dataInserimentoDichiarazione);
        anno = new Integer(cal.get(Calendar.YEAR)).toString();
      }
      
      return anno;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("anno", anno) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[ConsistenzaDAO::getLastDichConsNoCorrettiva] ",
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
              "[ConsistenzaDAO::getLastDichConsNoCorrettiva] END.");
    }
  }
  
  public ConsistenzaVO getUltimaDichConsNoCorrettiva(long idAzienda) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    ConsistenzaVO consistenzaVO = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ConsistenzaDAO::getUltimaDichConsNoCorrettiva] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("" +
              "SELECT DC.ID_DICHIARAZIONE_CONSISTENZA, " +
              "       DC.ID_AZIENDA, " + 
              "       DC.ANNO, " +
              "       DC.DATA " +
              "FROM   DB_DICHIARAZIONE_CONSISTENZA DC " +
              "WHERE  DC.ID_AZIENDA = ? " +
              "AND    DC.DATA_INSERIMENTO_DICHIARAZIONE = " +
              "(SELECT " +
              "       MAX(DIC.DATA_INSERIMENTO_DICHIARAZIONE) AS DATA " +
              "FROM   DB_DICHIARAZIONE_CONSISTENZA DIC, " +
              "       DB_TIPO_MOTIVO_DICHIARAZIONE TMC " +
              "WHERE  DIC.ID_AZIENDA = ? " +
              "AND    DIC.ID_MOTIVO_DICHIARAZIONE = TMC.ID_MOTIVO_DICHIARAZIONE " +
              "AND    DIC.ID_MOTIVO_DICHIARAZIONE <> 7 " + 
              "AND    TMC.TIPO_DICHIARAZIONE <> 'C' )");
              
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ConsistenzaDAO::getUltimaDichConsNoCorrettiva] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int idx = 0;
      stmt.setLong(++idx,idAzienda);
      stmt.setLong(++idx,idAzienda);
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {
        consistenzaVO = new ConsistenzaVO();
        consistenzaVO.setIdDichiarazioneConsistenza(rs.getString("ID_DICHIARAZIONE_CONSISTENZA"));
        consistenzaVO.setAnno(rs.getString("ANNO"));
        consistenzaVO.setData(rs.getString("DATA"));
      }
      
      
      
      return consistenzaVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("consistenzaVO", consistenzaVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[ConsistenzaDAO::getUltimaDichConsNoCorrettiva] ",
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
              "[ConsistenzaDAO::getUltimaDichConsNoCorrettiva] END.");
    }
  }
  
  /**
   * 
   * restituisce il risultato dell'invio fascicoli al sian!!!!
   * 
   * 
   * @param idDichiarazioneConsistenza
   * @return
   * @throws DataAccessException
   */
  public FascicoloNazionaleVO getInfoRisultatiSianDichiarazioneConsistenza(
      long idDichiarazioneConsistenza) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    FascicoloNazionaleVO fascicoloNazionaleVO = null;
    
    try
    {
      SolmrLogger
          .debug(
              this,
              "[ConsistenzaDAO::getInfoRisultatiSianDichiarazioneConsistenza] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
      		"SELECT FN.COD_ERRORE_FASCICOLO, " +
          "       FN.MSG_ERRORE_FASCICOLO, " +
          "       FN.COD_ERRORE_CONSISTENZA, " +
          "       FN.MSG_AGG_CONSISTENZA, " +
          "       FN.COD_ERRORE_UV, " +
          "       FN.MSG_ERRORE_UV, " +
          "       FN.COD_ERRORE_FABBRICATI, " +
          "       FN.MSG_ERRORE_FABBRICATI, " +
          "       FN.COD_ERRORE_CC, " +
          "       FN.MSG_ERRORE_CC " +
          "FROM   DB_FASCICOLI_NAZIONALE_BACKUP FN "+
          "WHERE  FN.ID_FASCICOLI_NAZIONALE_BACKUP =  "+
          "                                       (SELECT MAX(FN2.ID_FASCICOLI_NAZIONALE_BACKUP) " +
          "                                        FROM   DB_FASCICOLI_NAZIONALE_BACKUP FN2 " +
          "                                        WHERE  FN2.ID_DICHIARAZIONE_CONSISTENZA = ? )");
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
                "[ConsistenzaDAO::getInfoRisultatiSianDichiarazioneConsistenza] Query="
                    + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idDichiarazioneConsistenza);
      ResultSet rs = stmt.executeQuery();
          
      if (rs.next())
      {
        fascicoloNazionaleVO = new FascicoloNazionaleVO();        
        fascicoloNazionaleVO.setCodErroreFascicolo(rs.getString("COD_ERRORE_FASCICOLO"));
        fascicoloNazionaleVO.setMsgErroreFascicolo(rs.getString("MSG_ERRORE_FASCICOLO"));
        fascicoloNazionaleVO.setCodErroreConsistenza(rs.getString("COD_ERRORE_CONSISTENZA"));
        fascicoloNazionaleVO.setMsgErroreConsistenza(rs.getString("MSG_AGG_CONSISTENZA"));
        fascicoloNazionaleVO.setCodErroreUV(rs.getString("COD_ERRORE_UV"));
        fascicoloNazionaleVO.setMsgErroreUV(rs.getString("MSG_ERRORE_UV"));
        fascicoloNazionaleVO.setCodErroreFabbricati(rs.getString("COD_ERRORE_FABBRICATI"));
        fascicoloNazionaleVO.setMsgErroreFabbricati(rs.getString("MSG_ERRORE_FABBRICATI"));
        fascicoloNazionaleVO.setCodErroreCC(rs.getString("COD_ERRORE_CC"));
        fascicoloNazionaleVO.setMsgErroreCC(rs.getString("MSG_ERRORE_CC"));    
      }
      
      
      return fascicoloNazionaleVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[ConsistenzaDAO::getInfoRisultatiSianDichiarazioneConsistenza] ",
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
              "[ConsistenzaDAO::getInfoRisultatiSianDichiarazioneConsistenza] END.");
    }
  }
  
  
  
  public Date getMaxValiditaStato(Long idAzienda, Long idDichiarazioneConsistenza) 
    throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Date dataMax = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT MAX(DATA_VALIDITA_STATO) AS DATA_MAX "
          + "FROM  DB_PROCEDIMENTO_AZIENDA PA "
          + "WHERE PA.ID_AZIENDA = ? "
          + "AND   PA.ID_DICHIARAZIONE_CONSISTENZA = ? " ;

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing getMaxValiditaStato: " + query);
      
      SolmrLogger.debug(this, "idAzienda: " + idAzienda.longValue());
      SolmrLogger.debug(this, "idDichiarazioneConsistenza: " + idDichiarazioneConsistenza.longValue());
      
      stmt.setLong(1, idAzienda.longValue());
      stmt.setLong(2, idDichiarazioneConsistenza.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        dataMax = rs.getTimestamp("DATA_MAX");
        SolmrLogger.debug(this, "result DATA_MAX: "
            + dataMax);
      }
      rs.close();
      stmt.close();
      SolmrLogger.debug(this, "Executed getMaxValiditaStato");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getMaxValiditaStato - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getMaxValiditaStato - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "getMaxValiditaStato - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getMaxValiditaStato - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return dataMax;
  }
  
  
  /**
   * ritorna lka data dell'ultima dichiarazione di consistenza
   * non correttiva dell'azienda
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Date getLastDateDichConsNoCorrettiva(long idAzienda)  throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    //String anno = null;
    java.util.Date dataInserimentoDichiarazione = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ConsistenzaDAO::getLastDateDichConsNoCorrettiva] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("" +
              "SELECT " +
              "       MAX(DIC.DATA_INSERIMENTO_DICHIARAZIONE) AS DATA " +
              "FROM   DB_DICHIARAZIONE_CONSISTENZA DIC, " +
              "       DB_TIPO_MOTIVO_DICHIARAZIONE TMC " +
              "WHERE  DIC.ID_AZIENDA = ? " +
              "AND    DIC.ID_MOTIVO_DICHIARAZIONE = TMC.ID_MOTIVO_DICHIARAZIONE " +
              "AND    DIC.ID_MOTIVO_DICHIARAZIONE <> 7 " + 
              "AND    TMC.TIPO_DICHIARAZIONE <> 'C' ");
              
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ConsistenzaDAO::getLastDateDichConsNoCorrettiva] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int idx = 0;
      stmt.setLong(++idx,idAzienda);
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {
        dataInserimentoDichiarazione = rs.getTimestamp("DATA");
      }
      
      return dataInserimentoDichiarazione;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("dataInserimentoDichiarazione", dataInserimentoDichiarazione) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[ConsistenzaDAO::getLastDateDichConsNoCorrettiva] ",
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
              "[ConsistenzaDAO::getLastDateDichConsNoCorrettiva] END.");
    }
  }
  
  /**
   * 
   * ritorna l'id dell'utlima dichiarazione di consistenza
   * protocollata.
   * 
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Long getLastIdDichConsProtocollata(long idAzienda)  
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    //String anno = null;
    Long idDichiarazioneConsistenza = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ConsistenzaDAO::getLastIdDichConsProtocollata] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("" +
              "SELECT MAX(DIC.ID_DICHIARAZIONE_CONSISTENZA) AS MAX_ID " +
              "FROM   DB_DICHIARAZIONE_CONSISTENZA DIC " +
              "WHERE  DIC.ID_AZIENDA = ? " +
              "AND    DIC.NUMERO_PROTOCOLLO IS NOT NULL ");
              
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ConsistenzaDAO::getLastIdDichConsProtocollata] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int idx = 0;
      stmt.setLong(++idx,idAzienda);
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {
        idDichiarazioneConsistenza = new Long(rs.getLong("MAX_ID"));
      }
      
      return idDichiarazioneConsistenza;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("idDichiarazioneConsistenza", idDichiarazioneConsistenza) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[ConsistenzaDAO::getLastIdDichConsProtocollata] ",
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
              "[ConsistenzaDAO::getLastIdDichConsProtocollata] END.");
    }
  }
  
  public void updateNoteDichiarazioneConsistenza(String note,
      long idDichiarazioneConsistenza)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
      String query = 
        "UPDATE DB_DICHIARAZIONE_CONSISTENZA " +
        "SET NOTE =? " +
        "WHERE ID_DICHIARAZIONE_CONSISTENZA = ? ";

      stmt = conn.prepareStatement(query);

      int idx = 0;
      stmt.setString(++idx, note);
      stmt.setLong(++idx, idDichiarazioneConsistenza);

      SolmrLogger.debug(this, "Executing updateNoteDichiarazioneConsistenza"
          + query);

      stmt.executeUpdate();
      SolmrLogger.debug(this, "Executed updateNoteDichiarazioneConsistenza");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in updateNoteDichiarazioneConsistenza "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in updateNoteDichiarazioneConsistenza: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in updateNoteDichiarazioneConsistenza "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in updateNoteDichiarazioneConsistenza "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }
  
  
  public void updateDichiarazioneConsistenzaRichiestaStampa(Long idDichiarazioneConsistenza)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
      String query = 
        "UPDATE DB_DICHIARAZIONE_CONSISTENZA " +
        "SET DATA_RICHIESTA_STAMPA = SYSDATE " +
        "WHERE ID_DICHIARAZIONE_CONSISTENZA = ? ";

      stmt = conn.prepareStatement(query);

      int idx = 0;
      
      stmt.setLong(++idx, idDichiarazioneConsistenza.longValue());

      SolmrLogger.debug(this, "Executing updateDichiarazioneConsistenzaRichiestaStampa"
          + query);

      stmt.executeUpdate();
      SolmrLogger.debug(this, "Executed updateDichiarazioneConsistenzaRichiestaStampa");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in updateDichiarazioneConsistenzaRichiestaStampa "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in updateDichiarazioneConsistenzaRichiestaStampa: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in updateDichiarazioneConsistenzaRichiestaStampa "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in updateDichiarazioneConsistenzaRichiestaStampa "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }
  
  
  public Vector<ConsistenzaVO> getListDichiarazioniPianoGrafico(Long idAzienda) 
      throws DataAccessException
  {
    SolmrLogger.debug(this, "Invocating getListDichiarazioniPianoGrafico method in ConsistenzaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ConsistenzaVO> elencoDichiarazioni = null;

    try
    {
      SolmrLogger.debug(this, "Creating db-connection in getListDichiarazioniPianoGrafico method in ConsistenzaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListDichiarazioniPianoGrafico method in ConsistenzaDAO and it values: "
         + conn + "\n");

      String query = 
        "SELECT DC.ID_DICHIARAZIONE_CONSISTENZA, " +
        "       DC.ID_AZIENDA, " +
        "       DC.ANNO, " +
        "       DC.DATA, " +
        "       DC.CODICE_FOTOGRAFIA_TERRENI, " + 
        "       DC.ID_PROCEDIMENTO, " +
        "       DC.ID_UTENTE, " +
        "       DC.NOTE, " +
        "       DC.ID_MOTIVO_DICHIARAZIONE, " + 
        "       DC.DATA_INSERIMENTO_DICHIARAZIONE, " + 
        "       DC.ANNO_CAMPAGNA AS ANNO_CAMPAGNA_DICH, " +
        "       TMD.ID_MOTIVO_DICHIARAZIONE, " + 
        "       TMD.DESCRIZIONE, " +
        "       TMD.ANNO_CAMPAGNA AS ANNO_CAMPAGNA_TIPO, " +
        "       TMD.TIPO_DICHIARAZIONE, " +
        "       APG.CODICE_UTILITA," +
        "       APG.ID_ACCESSO_PIANO_GRAFICO " + 
        "FROM   DB_ACCESSO_PIANO_GRAFICO APG, " +
        "       DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "       DB_TIPO_MOTIVO_DICHIARAZIONE TMD " +
        "WHERE  APG.ID_AZIENDA = ? " +
        "AND    APG.ID_GRAFICO = 1 " +
        "AND    APG.DATA_FINE_VALIDITA IS NULL " +
        "AND    APG.ID_DICHIARAZIONE_CONSISTENZA = DC.ID_DICHIARAZIONE_CONSISTENZA " +
        "AND    DC.ID_MOTIVO_DICHIARAZIONE = TMD.ID_MOTIVO_DICHIARAZIONE " +
        "ORDER BY DC.DATA_INSERIMENTO_DICHIARAZIONE DESC";

      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListDichiarazioniPianoGrafico method in ConsistenzaDAO: "
        + idAzienda + "\n");

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idAzienda.longValue());

      SolmrLogger.debug(this,
          "Executing getListDichiarazioniPianoGrafico: " + query
              + "\n");

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        if(elencoDichiarazioni == null)
          elencoDichiarazioni = new Vector<ConsistenzaVO>();
        
        ConsistenzaVO consistenzaVO = new ConsistenzaVO();
        consistenzaVO.setIdDichiarazioneConsistenza(rs
            .getString("ID_DICHIARAZIONE_CONSISTENZA"));
        consistenzaVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        consistenzaVO.setAnno(rs.getString("ANNO"));
        consistenzaVO.setData(rs.getString("DATA"));
        consistenzaVO.setCodiceFotografiaTerreni(new Long(rs
            .getLong("CODICE_FOTOGRAFIA_TERRENI")));
        consistenzaVO.setIdProcedimento(rs.getString("ID_PROCEDIMENTO"));
        consistenzaVO.setIdUtente(rs.getString("ID_UTENTE"));
        consistenzaVO.setNote(rs.getString("NOTE"));
        consistenzaVO.setIdMotivo(rs.getString("ID_MOTIVO_DICHIARAZIONE"));
        consistenzaVO.setDataInserimentoDichiarazione(rs
            .getTimestamp("DATA_INSERIMENTO_DICHIARAZIONE"));
        consistenzaVO.setAnnoCampagna(rs.getString("ANNO_CAMPAGNA_DICH"));
        consistenzaVO.setCodiceUtilita(rs.getString("CODICE_UTILITA"));
        TipoMotivoDichiarazioneVO tipoMotivoDichiarazioneVO = new TipoMotivoDichiarazioneVO();
        tipoMotivoDichiarazioneVO.setIdMotivoDichiarazione(new Long(rs
            .getLong("ID_MOTIVO_DICHIARAZIONE")));
        tipoMotivoDichiarazioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoMotivoDichiarazioneVO
            .setAnnoCampagna(rs.getString("ANNO_CAMPAGNA_TIPO"));
        tipoMotivoDichiarazioneVO.setTipoDichiarazione(rs
            .getString("TIPO_DICHIARAZIONE"));
        consistenzaVO.setTipoMotivoDichiarazioneVO(tipoMotivoDichiarazioneVO);
        consistenzaVO.setIdAccessoPianoGrafico(checkLongNull(rs.getString("ID_ACCESSO_PIANO_GRAFICO")));
        elencoDichiarazioni.add(consistenzaVO);
      }

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "getListDichiarazioniPianoGrafico in ConsistenzaDAO - SQLException: "
        + exc + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this, "getListDichiarazioniPianoGrafico in ConsistenzaDAO - Generic Exception: "
        + ex + "\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.error(this, "getListDichiarazioniPianoGrafico in ConsistenzaDAO - SQLException while closing Statement and Connection: "
          + exc + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.error(this, "getListDichiarazioniPianoGrafico in ConsistenzaDAO - Generic Exception while closing Statement and Connection: "
                    + ex + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListDichiarazioniPianoGrafico method in ConsistenzaDAO\n");
    
    return elencoDichiarazioni;
  }
  
  public EsitoPianoGraficoVO getEsitoPianoGraficoFromAccesso(long idAccessoPianoGrafico) 
      throws DataAccessException
  {
    SolmrLogger.debug(this, "Invocating getEsitoPianoGraficoFromAccesso method in ConsistenzaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    EsitoPianoGraficoVO esitoPianoGraficoVO = null;

    try
    {
      SolmrLogger.debug(this, "Creating db-connection in getEsitoPianoGraficoFromAccesso method in ConsistenzaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getEsitoPianoGraficoFromAccesso method in ConsistenzaDAO and it values: "
         + conn + "\n");

      String query = 
        "SELECT APG.MESSAGGIO_ERRORE, " +
        "       EG.MESSAGGIO, " +
        "       EG.LINK, " +
        "       EG.ETICHETTA_PULSANTE, " +
        "       EG.ID_ESITO_GRAFICO, " +
        "       EG.ESEGUI_CONTROLLI " +
        "FROM   DB_ACCESSO_PIANO_GRAFICO APG, " +
        "       DB_TIPO_ESITO_GRAFICO EG "+
        "WHERE  APG.ID_ACCESSO_PIANO_GRAFICO = ? " +
        "AND    APG.ID_ESITO_GRAFICO = EG.ID_ESITO_GRAFICO ";

      SolmrLogger.debug(this, "Value of parameter 1 [ID_ACCESSO_PIANO_GRAFICO] in getEsitoPianoGraficoFromAccesso method in ConsistenzaDAO: "
        + idAccessoPianoGrafico + "\n");

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idAccessoPianoGrafico);

      SolmrLogger.debug(this,
          "Executing getEsitoPianoGraficoFromAccesso: " + query
              + "\n");

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        esitoPianoGraficoVO = new EsitoPianoGraficoVO();
        
        esitoPianoGraficoVO.setMessaggioErrore(rs.getString("MESSAGGIO_ERRORE"));
        esitoPianoGraficoVO.setMessaggio(rs.getString("MESSAGGIO"));
        esitoPianoGraficoVO.setLink(rs.getString("LINK"));
        esitoPianoGraficoVO.setEtichettaPulsante(rs.getString("ETICHETTA_PULSANTE"));
        esitoPianoGraficoVO.setIdEsitoGrafico(new Long(rs.getLong("ID_ESITO_GRAFICO")));
        esitoPianoGraficoVO.setFlagEseguiControlli(rs.getString("ESEGUI_CONTROLLI"));
      }

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "getEsitoPianoGraficoFromAccesso in ConsistenzaDAO - SQLException: "
        + exc + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this, "getEsitoPianoGraficoFromAccesso in ConsistenzaDAO - Generic Exception: "
        + ex + "\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.error(this, "getEsitoPianoGraficoFromAccesso in ConsistenzaDAO - SQLException while closing Statement and Connection: "
          + exc + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.error(this, "getEsitoPianoGraficoFromAccesso in ConsistenzaDAO - Generic Exception while closing Statement and Connection: "
                    + ex + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getEsitoPianoGraficoFromAccesso method in ConsistenzaDAO\n");
    
    return esitoPianoGraficoVO;
  }
  
  
  public int preCaricamentoPianoGrafico(long idDichiarazioneConsistenza) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    CallableStatement cs = null;
    StringBuffer queryBuf = null;
    int result = 0;
    
    try
    {
      SolmrLogger.debug(this, "[ConsistenzaDAO::preCaricamentoPianoGrafico] BEGIN.");
      /***
       *  FUNCTION preCaricamento(pIdDichiarazioneConsistenza  DB_DICHIARAZIONE_CONSISTENZA.ID_DICHIARAZIONE_CONSISTENZA%TYPE) 
       *    RETURN NUMBER;
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("{ ? = call CABAAD778_PS_WP.preCaricamento(?)}");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ConsistenzaDAO::preCaricamentoPianoGrafico] Query="
                + query);
      }
      
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.registerOutParameter(1,Types.INTEGER);
      cs.setLong(2, idDichiarazioneConsistenza);
      
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
      { new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[ConsistenzaDAO::preCaricamentoPianoGrafico] ",
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
              "[ConsistenzaDAO::preCaricamentoPianoGrafico] END.");
    }
  }
  
  public EsitoPianoGraficoVO getEsitoPianoGraficoFromPK(long idEsitoGrafico) 
      throws DataAccessException
  {
    SolmrLogger.debug(this, "Invocating getEsitoPianoGraficoFromPK method in ConsistenzaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    EsitoPianoGraficoVO esitoPianoGraficoVO = null;

    try
    {
      SolmrLogger.debug(this, "Creating db-connection in getEsitoPianoGraficoFromPK method in ConsistenzaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getEsitoPianoGraficoFromPK method in ConsistenzaDAO and it values: "
         + conn + "\n");

      String query = 
        "SELECT EG.MESSAGGIO, " +
        "       EG.LINK, " +
        "       EG.ETICHETTA_PULSANTE " +
        "FROM   DB_TIPO_ESITO_GRAFICO EG "+
        "WHERE  EG.ID_ESITO_GRAFICO = ? ";

      SolmrLogger.debug(this, "Value of parameter 1 [ID_ESITO_GRAFICO] in getEsitoPianoGraficoFromPK method in ConsistenzaDAO: "
        + idEsitoGrafico + "\n");

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idEsitoGrafico);

      SolmrLogger.debug(this,
          "Executing getEsitoPianoGraficoFromPK: " + query
              + "\n");

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        esitoPianoGraficoVO = new EsitoPianoGraficoVO();
        
        esitoPianoGraficoVO.setMessaggio(rs.getString("MESSAGGIO"));
        esitoPianoGraficoVO.setLink(rs.getString("LINK"));
        esitoPianoGraficoVO.setEtichettaPulsante(rs.getString("ETICHETTA_PULSANTE"));
      }

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "getEsitoPianoGraficoFromPK in ConsistenzaDAO - SQLException: "
        + exc + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this, "getEsitoPianoGraficoFromPK in ConsistenzaDAO - Generic Exception: "
        + ex + "\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.error(this, "getEsitoPianoGraficoFromPK in ConsistenzaDAO - SQLException while closing Statement and Connection: "
          + exc + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.error(this, "getEsitoPianoGraficoFromPK in ConsistenzaDAO - Generic Exception while closing Statement and Connection: "
                    + ex + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getEsitoPianoGraficoFromPK method in ConsistenzaDAO\n");
    
    return esitoPianoGraficoVO;
  }
  
  
  public void storicizzaAccessoPianoGrafico(Long idAccessoPianoGrafico)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
      String query = 
        "UPDATE DB_ACCESSO_PIANO_GRAFICO " +
        "SET DATA_FINE_VALIDITA = SYSDATE " +
        "WHERE ID_ACCESSO_PIANO_GRAFICO = ? ";

      stmt = conn.prepareStatement(query);

      int idx = 0;
      stmt.setLong(++idx, idAccessoPianoGrafico);
      

      SolmrLogger.debug(this, "Executing storicizzaAccessoPianoGrafico"
          + query);

      stmt.executeUpdate();
      SolmrLogger.debug(this, "Executed storicizzaAccessoPianoGrafico");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in storicizzaAccessoPianoGrafico "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in storicizzaAccessoPianoGrafico: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this, "SQLException while closing Statement and Connection in storicizzaAccessoPianoGrafico "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "Generic Exception while closing Statement and Connection in storicizzaAccessoPianoGrafico "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }
  
  public Long insertAccessoPianoGraficoStatoInCorso(long idAccessoPianoGrafico, long idUtente) 
      throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_DB_ACCESSO_PIANO_GRAFICO);

      conn = getDatasource().getConnection();
      String query = 
        "INSERT INTO DB_ACCESSO_PIANO_GRAFICO " +
        "  (ID_ACCESSO_PIANO_GRAFICO, " +
        "   ID_AZIENDA, " +
        "   DATA_ACCESSO, " +
        "   DATA_FINE_VALIDITA, " + 
        "   CODICE_UTILITA, " +
        "   ID_UTENTE, " +
        "   ID_GRAFICO, " +
        "   ID_DICHIARAZIONE_CONSISTENZA, " +
        "   MESSAGGIO_ERRORE, " +    
        "   ID_ESITO_GRAFICO) " +
        "   (SELECT ?, " +
        "           ID_AZIENDA, " +
        "           SYSDATE, " +
        "           NULL, " +
        "           CODICE_UTILITA, " +
        "           ?, " +
        "           ID_GRAFICO, " +  
        "           ID_DICHIARAZIONE_CONSISTENZA, " + 
        "           NULL, " +
        "           4 " +
        "    FROM   DB_ACCESSO_PIANO_GRAFICO " +
        "    WHERE  ID_ACCESSO_PIANO_GRAFICO = ? ) ";

      stmt = conn.prepareStatement(query);

      int idx = 0;
      
      stmt.setLong(++idx, primaryKey.longValue());
      stmt.setLong(++idx, idUtente);
      stmt.setLong(++idx, idAccessoPianoGrafico);
     

      SolmrLogger.debug(this, "Executing insertAccessoPianoGraficoStatoInCorso"
          + query);

      stmt.executeUpdate();
      SolmrLogger.debug(this, "Executed insertAccessoPianoGraficoStatoInCorso");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in insertAccessoPianoGraficoStatoInCorso "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in insertAccessoPianoGraficoStatoInCorso: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this, "SQLException while closing Statement and Connection in insertAccessoPianoGraficoStatoInCorso "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "Generic Exception while closing Statement and Connection in insertAccessoPianoGraficoStatoInCorso "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }
  
  
  public PlSqlCodeDescription controlliValidazionePlSql(long idAzienda, int idFase, 
      long idUtente, long idDichiarazioneConsistenza) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    CallableStatement cs = null;
    StringBuffer queryBuf = null;
    PlSqlCodeDescription plqObj = null;
    
    try
    {
      SolmrLogger.debug(this, "[ConsistenzaDAO::controlliValidazionePlSql] BEGIN.");
      /***
       *  PROCEDURE CONTROLLI_VALIDAZIONE(P_ID_AZIENDA               DB_AZIENDA.ID_AZIENDA%TYPE,
                                P_ID_FASE                  DB_TIPO_FASE.ID_FASE%TYPE,
                                P_ID_UTENTE_LOGIN          ?.ID_UTENTE%TYPE,
                                P_ID_DICHIARAZIONE         DB_DICHIARAZIONE_CONSISTENZA.ID_DICHIARAZIONE_CONSISTENZA%TYPE,
                                P_PRESENZA_ANOM        OUT VARCHAR2,
                                P_MSGERR            IN OUT VARCHAR2,
                                P_CODERR            IN OUT VARCHAR2) IS
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{call PACK_DICHIARAZIONE_CONSISTENZA.CONTROLLI_VALIDAZIONE(?,?,?,?,?,?,?)}");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this, "[ConsistenzaDAO::controlliValidazionePlSql] Query="
                + query);
      }
      
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.setLong(1, idAzienda);
      cs.setInt(2, idFase);
      cs.setLong(3, idUtente);
      cs.setLong(4, idDichiarazioneConsistenza);
      cs.registerOutParameter(5,Types.VARCHAR); //pPresenzaAnom      
      cs.registerOutParameter(6,Types.VARCHAR); //codice errore
      cs.registerOutParameter(7,Types.VARCHAR); //msg errore
      
      cs.executeUpdate();
      
      plqObj = new PlSqlCodeDescription();  
      plqObj.setDescription(cs.getString(6));
      plqObj.setOtherdescription(cs.getString(7));
      
      return plqObj;
    }
    catch (Exception t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("plqObj", plqObj) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("idFase", idFase), 
        new Parametro("idUtente", idUtente),
        new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[ConsistenzaDAO::controlliValidazionePlSql] ",
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
      SolmrLogger.debug(this, "[ConsistenzaDAO::controlliValidazionePlSql] END.");
    }
  }

  
  
  
}
