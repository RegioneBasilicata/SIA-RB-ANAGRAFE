package it.csi.solmr.integration.anag.stampe;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Claudio Zamboni
 * @version 1.0
 */

import it.csi.smranag.smrgaa.dto.BaseCodeDescription;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.dto.stampe.ConsistenzaZootecnicaStampa;
import it.csi.smranag.smrgaa.dto.stampe.QuadroDTerreni;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.DocumentoConduzioneVO;
import it.csi.solmr.dto.anag.DocumentoProprietarioVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.FabbricatoVO;
import it.csi.solmr.dto.anag.ParticellaVO;
import it.csi.solmr.dto.anag.TipoDocumentoVO;
import it.csi.solmr.dto.anag.TipoFormaConduzioneVO;
import it.csi.solmr.dto.anag.UteVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.SolmrErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.NotFoundException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.integration.BaseDAO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.ImageIcon;

public class StampeDAO extends BaseDAO
{

  public StampeDAO() throws ResourceAccessException
  {
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public StampeDAO(String refName) throws ResourceAccessException
  {
    super(refName);
  }

  // ===========================================================================
  // Recupera il logo della provincia partendo dal codice istat
  // Restituisce un oggetto Blob.
  // ===========================================================================
  public ImageIcon getLogoProvincia(String idIstat) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    ImageIcon result = null;
    Blob bTemp = null;
    try
    {
      conn = getDatasource().getConnection();
      String query = "SELECT provincia.logo " + "FROM   provincia "
          + "WHERE  provincia.istat_provincia = ? ";

      // SolmrLogger.debug(this, "getLogoProvincia - Executing query: "+query);
      stmt = conn.prepareStatement(query);
      stmt.setString(1, idIstat);

      rs = stmt.executeQuery();

      if (rs.next())
      {
        bTemp = rs.getBlob(1);
        if (bTemp != null && bTemp.length() > 0)
          result = new ImageIcon(bTemp.getBytes(1, (int) bTemp.length()));
      }
    }
    catch (SQLException sqle)
    {
      SolmrLogger.fatal(this, "StampeDAO.getLogoProvincia - SQLException: "
          + sqle.getMessage());
      throw new DataAccessException(sqle.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "StampeDAO.getLogoProvincia - Generic Exception: " + ex.getMessage());
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
      catch (SQLException sqle)
      {
        SolmrLogger
            .fatal(
                this,
                "StampeDAO.getLogoProvincia - SQLException while closing Statement and Connection: "
                    + sqle.getMessage());
        throw new DataAccessException(sqle.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "StampeDAO.getLogoProvincia - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // ===========================================================================
  // Recupera il logo della Regione
  // Restituisce un oggetto Blob.
  // ===========================================================================
  public ImageIcon getLogoRegione(String idIstat) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    ImageIcon result = null;
    Blob bTemp = null;
    try
    {
      conn = getDatasource().getConnection();
      String query = "SELECT logo " + "FROM   regione "
          + "WHERE  id_regione = ? ";

      // SolmrLogger.debug(this, "StampeDAO.getLogoRegionePiemonte - Executing
      // query: "+query);
      stmt = conn.prepareStatement(query);
      stmt.setString(1, idIstat);

      rs = stmt.executeQuery();

      if (rs.next())
      {
        bTemp = rs.getBlob(1);
        if (bTemp != null && bTemp.length() > 0)
          result = new ImageIcon(bTemp.getBytes(1, (int) bTemp.length()));
      }
    }
    catch (SQLException sqle)
    {
      SolmrLogger.fatal(this, "StampeDAO.getLogoRegione - SQLException: "
          + sqle.getMessage());
      throw new DataAccessException(sqle.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "StampeDAO.getLogoRegione - Generic Exception: "
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
      catch (SQLException sqle)
      {
        SolmrLogger
            .fatal(
                this,
                "StampeDAO.getLogoRegione - SQLException while closing Statement and Connection: "
                    + sqle.getMessage());
        throw new DataAccessException(sqle.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "StampeDAO.getLogoRegione - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // ===================================================================
  // Quadro A - Sez I
  //
  // Recupero azienda attiva con idAzienda e idDichiarazioneConsistenza
  // ===================================================================
  
  /**
   * 
   * Usare il metodo omonimo della classe StampaGaaDAO
   * @deprecated
   */
  public AnagAziendaVO getAnagraficaAzienda(Long idAzienda,
      java.util.Date dataRiferimento) throws DataAccessException,
      NotFoundException
  {
    AnagAziendaVO result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
      String query = "SELECT AA.ID_ANAGRAFICA_AZIENDA, AA.ID_AZIENDA, "
          + "       AA.DATA_INIZIO_VALIDITA, AA.DATA_FINE_VALIDITA, "
          + "       AA.CUAA, PARTITA_IVA, AA.DENOMINAZIONE,  "
          + "       AA.ID_FORMA_GIURIDICA, "
          + "       TFG.DESCRIZIONE DESC_FORMA_GIURIDICA, "
          + "       AA.ID_ATTIVITA_ATECO, "
          + "       TAT.DESCRIZIONE DESC_ATTIVITA_ATECO, "
          + "       AA.ID_ATTIVITA_OTE, "
          + "       TOT.DESCRIZIONE DESC_ATTIVITA_OTE, "
          + "       AA.CCIAA_PROVINCIA_REA, "
          + "       AA.CCIAA_NUMERO_REA, "
          + "       AA.CCIAA_NUMERO_REGISTRO_IMPRESE, "
          + "       AA.CCIAA_ANNO_ISCRIZIONE, "
          + "       AA.PROVINCIA_COMPETENZA, "
          + "       P1.SIGLA_PROVINCIA SEDELEG_PROV,  "
          + "       COMUNE.DESCOM, "
          + "       AA.SEDELEG_INDIRIZZO, "
          + "       AA.SEDELEG_CAP, "
          + "       AA.SEDELEG_CITTA_ESTERO, "
          + "       AA.MAIL, "
          + "       P2.DESCRIZIONE AS DESPROV "
          + "  FROM DB_ANAGRAFICA_AZIENDA AA, "
          + "       DB_TIPO_FORMA_GIURIDICA TFG, "
          + "       DB_TIPO_ATTIVITA_ATECO TAT, "
          + "       DB_TIPO_ATTIVITA_OTE TOT, "
          + "       COMUNE, PROVINCIA P1, PROVINCIA P2 "
          + " WHERE AA.SEDELEG_COMUNE = COMUNE.ISTAT_COMUNE(+) "
          + "   AND COMUNE.ISTAT_PROVINCIA = P1.ISTAT_PROVINCIA(+) "
          + "   AND AA.ID_FORMA_GIURIDICA = TFG.ID_FORMA_GIURIDICA(+) "
          + "   AND AA.ID_ATTIVITA_ATECO = TAT.ID_ATTIVITA_ATECO(+) "
          + "   AND AA.ID_ATTIVITA_OTE = TOT.ID_ATTIVITA_OTE(+) "
          + "   AND AA.PROVINCIA_COMPETENZA = P2.ISTAT_PROVINCIA(+) "
          + "   AND AA.ID_AZIENDA = ? "
          + "   AND (  ( AA.DATA_FINE_VALIDITA IS NOT NULL AND ( ? BETWEEN AA.DATA_INIZIO_VALIDITA AND AA.DATA_FINE_VALIDITA ) ) "
          + "       OR ( AA.DATA_FINE_VALIDITA IS NULL     AND AA.DATA_INIZIO_VALIDITA <= ? ) ) ";

      // SolmrLogger.debug(this, "StampeDAO.getAnagraficaAzienda - Executing
      // query: "+query);
      // SolmrLogger.debug(this, "StampeDAO.getAnagraficaAzienda -
      // dataRiferimento: "+dataRiferimento);
      stmt = conn.prepareStatement(query);
      java.sql.Date sqlDate = new java.sql.Date(dataRiferimento.getTime());
      stmt.setLong(1, idAzienda.longValue());
      stmt.setDate(2, sqlDate);
      stmt.setDate(3, sqlDate);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        result = new AnagAziendaVO();
        result.setIdAnagAzienda(new Long(rs.getLong("ID_ANAGRAFICA_AZIENDA")));
        result.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        result.setDataInizioVal(rs.getDate("DATA_INIZIO_VALIDITA"));
        result.setDataFineVal(rs.getDate("DATA_FINE_VALIDITA"));
        result.setCUAA(rs.getString("CUAA"));
        result.setPartitaIVA(rs.getString("PARTITA_IVA"));
        result.setDenominazione(rs.getString("DENOMINAZIONE"));
        result.setTipoFormaGiuridica(new CodeDescription(new Integer(rs
            .getInt("ID_FORMA_GIURIDICA")), rs
            .getString("DESC_FORMA_GIURIDICA")));
        result
            .setTipoAttivitaATECO(new CodeDescription(new Integer(rs
                .getInt("ID_ATTIVITA_ATECO")), rs
                .getString("DESC_ATTIVITA_ATECO")));
        result.setTipoAttivitaOTE(new CodeDescription(new Integer(rs
            .getInt("ID_ATTIVITA_OTE")), rs.getString("DESC_ATTIVITA_OTE")));
        result.setCCIAAprovREA(rs.getString("CCIAA_PROVINCIA_REA"));

        String numTemp = rs.getString("CCIAA_NUMERO_REA");
        if (numTemp != null)
          result.setCCIAAnumeroREA(new Long(rs.getLong("CCIAA_NUMERO_REA")));

        result.setCCIAAnumRegImprese(rs
            .getString("CCIAA_NUMERO_REGISTRO_IMPRESE"));
        result.setCCIAAannoIscrizione(rs.getString("CCIAA_ANNO_ISCRIZIONE"));
        result.setProvCompetenza(rs.getString("PROVINCIA_COMPETENZA"));
        result.setSedelegProv(rs.getString("SEDELEG_PROV"));
        result.setDescComune(rs.getString("DESCOM"));
        result.setSedelegIndirizzo(rs.getString("SEDELEG_INDIRIZZO"));
        result.setSedelegCAP(rs.getString("SEDELEG_CAP"));
        result.setSedelegCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
        result.setMail(rs.getString("MAIL"));
        result.setDescrizioneProvCompetenza(rs.getString("DESPROV"));
      }

      if (result == null)
      {
        throw new NotFoundException(SolmrErrors.DATI_AZIENDA_NON_REPERIBILI
            + " " + DateUtils.formatDate(dataRiferimento));
      }

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "StampeDAO.getAnagraficaAzienda - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (NotFoundException exc)
    {
      SolmrLogger.fatal(this,
          "StampeDAO.getAnagraficaAzienda - NotFoundException: "
              + exc.getMessage());
      throw new NotFoundException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "StampeDAO.getAnagraficaAzienda - Generic Exception: "
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
                "StampeDAO.getAnagraficaAzienda - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "StampeDAO.getAnagraficaAzienda - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // ===================================================================
  // Quadro B - unità tecnico economiche
  //
  // prendere i dati da DB_UTE
  // ===================================================================
  public Vector<UteVO> getUteQuadroB(Long idAzienda, java.util.Date dataRiferimento)
      throws DataAccessException
  {
    Vector<UteVO> result = new Vector<UteVO>();
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = 
        "SELECT U.INDIRIZZO, " +
        "       C.DESCOM, " +
        "       P.SIGLA_PROVINCIA, " + 
        "       U.CAP, " +
        "       ZA.DESCRIZIONE AS ZONA_ALTIMETRICA, " + 
        "       TO_CHAR(U.DATA_INIZIO_ATTIVITA,'dd/mm/yyyy') AS DATA_INIZIO_ATTIVITA, " +
        "       U.DENOMINAZIONE, " +
        "       U.TELEFONO, " +
        "       TAT.DESCRIZIONE AS DESC_ATECO, " +
        "       TAT.CODICE AS COD_ATECO " + 
        "FROM   DB_UTE U, " +
        "       COMUNE C, " +
        "       PROVINCIA P, " +
        "       DB_TIPO_ZONA_ALTIMETRICA ZA, " +
        "       DB_TIPO_ATTIVITA_ATECO TAT " + 
        "WHERE  U.COMUNE = C.ISTAT_COMUNE " + 
        "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " + 
        "AND    U.ID_ZONA_ALTIMETRICA = ZA.ID_ZONA_ALTIMETRICA " + 
        "AND    U.ID_AZIENDA = ? " +
        "AND    U.DATA_INIZIO_ATTIVITA <= ? " + 
        "AND    NVL(U.DATA_FINE_ATTIVITA, TO_DATE('31/12/9999','dd/mm/yyyy')) > ? " +
        "AND    U.ID_ATTIVITA_ATECO = TAT.ID_ATTIVITA_ATECO (+)" +
        "ORDER BY C.DESCOM ";

      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setTimestamp(2, this.convertDateToTimestamp(dataRiferimento));
      stmt.setTimestamp(3, this.convertDateToTimestamp(dataRiferimento));

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        UteVO uteVO = new UteVO();
        uteVO.setIndirizzo(rs.getString("INDIRIZZO"));
        uteVO.setComune(rs.getString("DESCOM"));
        uteVO.setProvincia(rs.getString("SIGLA_PROVINCIA"));
        uteVO.setCap(rs.getString("CAP"));
        uteVO.setZonaAltimetrica(rs.getString("ZONA_ALTIMETRICA"));
        uteVO.setDataInizioAttivitaStr(rs.getString("DATA_INIZIO_ATTIVITA"));
        uteVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        uteVO.setTelefono(rs.getString("TELEFONO"));
        uteVO.setDescAteco(rs.getString("DESC_ATECO"));
        uteVO.setCodeAteco(rs.getString("COD_ATECO"));
        
        result.add(uteVO);
      }

      rs.close();

      SolmrLogger.debug(this, "getUTEQuadroB - Found " + result.size()
          + " record(s)");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getUTEQuadroB - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getUTEQuadroB - Generic Exception: "
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
        SolmrLogger.fatal(this,
            "getUTEQuadroB - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "getUTEQuadroB - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // ===================================================================
  // QUADRO D - forma di conduzione
  //
  //
  // ===================================================================
  public Vector<TipoFormaConduzioneVO> getFormeConduzioneQuadroD() throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;

    Vector<TipoFormaConduzioneVO> vTipoFormaConduzione = new Vector<TipoFormaConduzioneVO>();
    

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT ID_FORMA_CONDUZIONE, CODICE, FORMA, "
          + " DESCRIZIONE " + " FROM DB_TIPO_FORMA_CONDUZIONE "
          + " ORDER BY FORMA ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        TipoFormaConduzioneVO formaConduzione = new TipoFormaConduzioneVO();
        formaConduzione.setIdFormaConduzione(rs
            .getString("ID_FORMA_CONDUZIONE"));
        formaConduzione.setCodice(rs.getString("CODICE"));
        formaConduzione.setForma(rs.getString("FORMA"));
        formaConduzione.setDescrizione(rs.getString("DESCRIZIONE"));
        vTipoFormaConduzione.add(formaConduzione);
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this,
          "getFormeConduzioneQuadroD - Executed query - Found "
              + vTipoFormaConduzione.size() + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getFormeConduzioneQuadroD - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getFormeConduzioneQuadroD - Generic Exception: "
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
                "getFormeConduzioneQuadroD - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getFormeConduzioneQuadroD - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return vTipoFormaConduzione;
  }

  // ===================================================================
  // QUADRO D - forma di conduzione
  //
  //
  // ===================================================================
  public Long getFormaConduzioneQuadroD(Long idAzienda,
      java.util.Date dataRiferimento) throws DataAccessException
  {
    Long idFormaConduzione = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT ID_FORMA_CONDUZIONE "
          + "FROM DB_MANODOPERA "
          + "WHERE ID_AZIENDA= ? "
          + "AND TRUNC(DATA_INIZIO_VALIDITA) <= ? "
          + "AND NVL(TRUNC(DATA_FINE_VALIDITA), TO_DATE('31/12/9999','dd/mm/yyyy')) > ? ";

      /*
       * "and ((data_fine_validita is null and data_inizio_validita <= ?) "+ "or ?
       * between data_inizio_validita and data_fine_validita) ";
       */

      /*
       * "AND (DATA_FINE_VALIDITA IS NULL "+ "OR ? BETWEEN DATA_INIZIO_VALIDITA
       * AND DATA_FINE_VALIDITA) ";
       */

      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setDate(2, this.checkDate(dataRiferimento));
      stmt.setDate(3, this.checkDate(dataRiferimento));

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        idFormaConduzione = new Long(rs.getInt("ID_FORMA_CONDUZIONE"));
      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getFormaConduzioneQuadroD - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getFormaConduzioneQuadroD - Generic Exception: "
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
                "getFormaConduzioneQuadroD - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getFormaConduzioneQuadroD - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return idFormaConduzione;
  }

  // ===================================================================
  // QUADRO E - attività complementari
  //
  //
  // ===================================================================
  public Vector<CodeDescription> getAttivitaComplementariQuadroE(Long idAzienda,
      java.util.Date dataRiferimento) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;

    Vector<CodeDescription> vAttivitaComplementari = new Vector<CodeDescription>();
    Vector<Long> idAttivita = new Vector<Long>();

    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT A.ID_ATTIVITA_COMPLEMENTARI "
          + "FROM DB_DETTAGLIO_ATTIVITA A, DB_MANODOPERA M "
          + "WHERE A.ID_MANODOPERA=M.ID_MANODOPERA AND M.ID_AZIENDA= ? "
          + "AND TRUNC(DATA_INIZIO_VALIDITA) <= ? "
          + "AND NVL(TRUNC(DATA_FINE_VALIDITA), TO_DATE('31/12/9999','dd/mm/yyyy')) > ? ";

      /*
       * "and ((data_fine_validita is null and data_inizio_validita <= ?) "+ "or ?
       * between data_inizio_validita and data_fine_validita) ";
       */

      /*
       * "AND ("+ "DATA_FINE_VALIDITA IS NULL "+ "OR "+ "? BETWEEN
       * DATA_INIZIO_VALIDITA AND DATA_FINE_VALIDITA) ";
       */

      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setDate(2, this.checkDate(dataRiferimento));
      stmt.setDate(3, this.checkDate(dataRiferimento));

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        Long id = new Long(rs.getLong("ID_ATTIVITA_COMPLEMENTARI"));
        idAttivita.add(id);
      }

      rs.close();

      query = " SELECT C.ID_ATTIVITA_COMPLEMENTARI,C.DESCRIZIONE "
          + " FROM DB_TIPO_ATTIVITA_COMPLEMENTARI C "
          + " ORDER BY C.DESCRIZIONE ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      rs = stmt.executeQuery();

      boolean trovato = false;

      while (rs.next())
      {
        CodeDescription code = new CodeDescription();
        code.setSecondaryCode(rs.getString("ID_ATTIVITA_COMPLEMENTARI"));
        code.setDescription(rs.getString("DESCRIZIONE"));
        if (confrontaidAttivita(idAttivita, code.getSecondaryCode()))
        {
          code.setCode(new Integer(1));
          trovato = true;
        }
        else
          code.setCode(null);
        vAttivitaComplementari.add(code);
      }

      if (!trovato)
        vAttivitaComplementari = new Vector<CodeDescription>();

      rs.close();
      stmt.close();

      SolmrLogger.debug(this,
          "getAttivitaComplementariQuadroE - Executed query - Found "
              + vAttivitaComplementari.size() + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger
          .fatal(this, "getAttivitaComplementariQuadroE - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getAttivitaComplementariQuadroE - Generic Exception: "
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
                "getAttivitaComplementariQuadroE - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getAttivitaComplementariQuadroE - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return vAttivitaComplementari;
  }

  private boolean confrontaidAttivita(Vector<?> attivita, String idAttivita)
  {
    boolean trovato = false;
    int size = attivita.size();
    for (int i = 0; i < size; i++)
    {
      Long num = (Long) attivita.get(i);
      try
      {
        if (num.longValue() == Long.parseLong(idAttivita))
          return true;
      }
      catch (Exception e)
      {
      }
    }
    return trovato;
  }

  // ===================================================================
  // QUADRO F - manodopera
  //
  //
  // ===================================================================
  public Long getIdManodoperaQuadroF(Long idAzienda,
      java.util.Date dataRiferimento) throws DataAccessException
  {
    Long idManodopera = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT ID_MANODOPERA "
          + "FROM DB_MANODOPERA "
          + "WHERE ID_AZIENDA= ? "
          + "AND TRUNC(DATA_INIZIO_VALIDITA) <= ? "
          + "AND NVL(TRUNC(DATA_FINE_VALIDITA), TO_DATE('31/12/9999','dd/mm/yyyy')) > ? ";

      /*
       * "and ((data_fine_validita is null and data_inizio_validita <= ?) "+ "or ?
       * between data_inizio_validita and data_fine_validita) ";
       */

      /*
       * "AND ( DATA_FINE_VALIDITA IS NULL OR "+ " ? BETWEEN
       * DATA_INIZIO_VALIDITA "+ "AND DATA_FINE_VALIDITA) ";
       */

      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setDate(2, this.checkDate(dataRiferimento));
      stmt.setDate(3, this.checkDate(dataRiferimento));

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        idManodopera = new Long(rs.getInt("ID_MANODOPERA"));
      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getIdManodoperaQuadroF - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getIdManodoperaQuadroF - Generic Exception: "
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
                "getIdManodoperaQuadroF - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getIdManodoperaQuadroF - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return idManodopera;
  }

  // ===================================================================
  // QUADRO G - Consistenza zootecnica
  //
  // prendere i dati da DB_ALLEVAMENTO
  // ===================================================================
  public Vector<Long> getAllevamentiQuadroG(Long idAzienda,
      java.util.Date dataRiferimento) throws DataAccessException
  {
    Vector<Long> result = new Vector<Long>();
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT A.ID_ALLEVAMENTO "
          + "FROM DB_ALLEVAMENTO A, DB_UTE UTE,"
          + "COMUNE C, DB_TIPO_SPECIE_ANIMALE S "
          + "WHERE A.ID_UTE = UTE.ID_UTE AND UTE.ID_AZIENDA = ? "
          + "AND A.DATA_INIZIO <= ? "
          + "AND NVL(A.DATA_FINE, TO_DATE('31/12/9999','dd/mm/yyyy')) > ? "
          + "AND A.ISTAT_COMUNE = C.ISTAT_COMUNE "
          + "AND A.ID_SPECIE_ANIMALE = S.ID_SPECIE_ANIMALE "
          + "ORDER BY C.DESCOM,S.DESCRIZIONE";

      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query: " + query);
      SolmrLogger.debug(this, "Param idAzienda: " + idAzienda);
      SolmrLogger.debug(this, "Param dataRiferimento: " + dataRiferimento);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setTimestamp(2, convertDateToTimestamp(dataRiferimento));
      stmt.setTimestamp(3, convertDateToTimestamp(dataRiferimento));

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        Long idAllevamento;
        idAllevamento = new Long(rs.getLong("ID_ALLEVAMENTO"));
        result.add(idAllevamento);
      }
      rs.close();

      SolmrLogger.debug(this, "getAllevamentiQuadroG - Found " + result.size()
          + " record(s)");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getAllevamentiQuadroG - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getAllevamentiQuadroG - Generic Exception: "
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
        SolmrLogger.fatal(this,
            "getAllevamentiQuadroG - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getAllevamentiQuadroG - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }


  // ===================================================================
  // QuadroI1
  //
  // Estraggo tutte le particelle
  // Se codFotografia valorizzato significa id_dichiarazione_consistenza
  // not null
  // ===================================================================
  public Vector<ParticellaVO> getElencoParticelleQuadroI1(Long idAzienda,Long codFotografia) 
    throws DataAccessException
  {
    Vector<ParticellaVO> result = new Vector<ParticellaVO>();
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      //query per la situazione attuale
      String querySitAtt = 
        "SELECT CP.ID_CONDUZIONE_PARTICELLA, C.DESCOM || ' (' || P.SIGLA_PROVINCIA || ')' AS DESC_COMUNE, " +
        "       SP.SEZIONE, " +
        "       SP.FOGLIO, " +
        "       SP.PARTICELLA, " +
        "       SP.SUBALTERNO, "+
        "       TU.CODICE AS COD_USO, " +
        "       TU.DESCRIZIONE AS DESC_USO, " +
        "       TTD.CODICE_DESTINAZIONE AS COD_DEST_PRIM, " +
        "       TTD.DESCRIZIONE_DESTINAZIONE AS DESC_DEST_PRIM, "+
        "       TDU.CODICE_DETTAGLIO_USO AS COD_DETT_USO, " +
        "       TDU.DESCRIZIONE_DETTAGLIO_USO AS DESC_DETT_USO, "+
        "       TQU.CODICE_QUALITA_USO AS COD_QUAL_USO, " +
        "       TQU.DESCRIZIONE_QUALITA_USO AS DESC_QUAL_USO, "+
        "       TV.CODICE_VARIETA AS COD_VARIETA, " +
        "       TV.DESCRIZIONE AS DESC_VARIETA, "+
        "       SP.SUP_CATASTALE, " +
        "       SP.SUPERFICIE_GRAFICA, " +
        "       CP.ID_TITOLO_POSSESSO, " +
        "       CP.SUPERFICIE_CONDOTTA, " +
        "       CP.PERCENTUALE_POSSESSO, " +
        "       UP.SUPERFICIE_UTILIZZATA, " +
        "       CP.SUPERFICIE_AGRONOMICA, "+
        "       SP.ID_CASO_PARTICOLARE, " +
        "       SP.ID_AREA_A, " +
        "       SP.ID_AREA_B, " +
        "       SP.ID_AREA_C, " +
        "       SP.ID_AREA_D, "+
        "       DECODE(F.ID_AREA_E,2,'X') AS ZVN, " +
        "       SP.ID_ZONA_ALTIMETRICA, " +
        "       SP.FLAG_IRRIGABILE, " +
        "       SP.FLAG_CAPTAZIONE_POZZI, " +
        "       SP.ID_POTENZIALITA_IRRIGUA, " +
        "       SP.ID_ROTAZIONE_COLTURALE, " +
        "       SP.ID_TERRAZZAMENTO, " +
        "       SP.ID_AREA_M, " +
        "       TU_SEC.CODICE AS COD_USO_SEC, " +
        "       TU_SEC.DESCRIZIONE AS DESC_USO_SEC, " +
        "       TTD_SEC.CODICE_DESTINAZIONE AS COD_DEST_SEC, " +
        "       TTD_SEC.DESCRIZIONE_DESTINAZIONE AS DESC_DEST_SEC, "+
        "       TDU_SEC.CODICE_DETTAGLIO_USO AS COD_DETT_USO_SEC, " +
        "       TDU_SEC.DESCRIZIONE_DETTAGLIO_USO AS DESC_DETT_USO_SEC, "+
        "       TQU_SEC.CODICE_QUALITA_USO AS COD_QUAL_USO_SEC, " +
        "       TQU_SEC.DESCRIZIONE_QUALITA_USO AS DESC_QUAL_USO_SEC, "+
        "       TV_SEC.CODICE_VARIETA AS COD_VARIETA_SEC, " +
        "       TV_SEC.DESCRIZIONE AS DESC_VARIETA_SEC, "+        
        "       UP.SUP_UTILIZZATA_SECONDARIA, " +
        "       SP.ID_PARTICELLA " +
        "FROM   DB_CONDUZIONE_PARTICELLA CP, " +
        "       DB_UTE U, " +
        "       DB_UTILIZZO_PARTICELLA UP, " +
        "       DB_R_CATALOGO_MATRICE RCM, " +
        "       DB_R_CATALOGO_MATRICE RCM_SEC, " +
        "       DB_STORICO_PARTICELLA SP, " +
        "       COMUNE C, "+
        "       PROVINCIA P, " +
        "       DB_TIPO_UTILIZZO TU, " +
        "       DB_TIPO_DESTINAZIONE TTD, " +
        "       DB_TIPO_DETTAGLIO_USO TDU," +
        "       DB_TIPO_QUALITA_USO TQU," +
        "       DB_TIPO_VARIETA TV, " +
        "       DB_TIPO_UTILIZZO TU_SEC, " +
        "       DB_TIPO_DESTINAZIONE TTD_SEC, " +
        "       DB_TIPO_DETTAGLIO_USO TDU_SEC," +
        "       DB_TIPO_QUALITA_USO TQU_SEC," +
        "       DB_TIPO_VARIETA TV_SEC, " +
        "       DB_FOGLIO F "+
        "WHERE  U.ID_AZIENDA = ? "+
        "AND    CP.ID_UTE = U.ID_UTE "+
        "AND    U.DATA_FINE_ATTIVITA IS NULL "+
        "AND    CP.DATA_FINE_CONDUZIONE IS NULL "+
        "AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA(+) "+
        "AND    SP.ID_PARTICELLA = CP.ID_PARTICELLA "+
        "AND    SP.DATA_FINE_VALIDITA IS NULL "+
        "AND    SP.COMUNE = C.ISTAT_COMUNE "+
        "AND    P.ISTAT_PROVINCIA = C.ISTAT_PROVINCIA "+
        "AND    UP.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE(+) "+
        "AND    RCM.ID_UTILIZZO = TU.ID_UTILIZZO(+) "+
        "AND    RCM.ID_TIPO_DESTINAZIONE = TTD.ID_TIPO_DESTINAZIONE (+) "+
        "AND    RCM.ID_TIPO_DETTAGLIO_USO = TDU.ID_TIPO_DETTAGLIO_USO (+) "+
        "AND    RCM.ID_TIPO_QUALITA_USO = TQU.ID_TIPO_QUALITA_USO (+) "+
        "AND    RCM.ID_VARIETA = TV.ID_VARIETA(+) " +
        "AND    UP.ID_CATALOGO_MATRICE_SECONDARIO = RCM_SEC.ID_CATALOGO_MATRICE(+) "+
        "AND    RCM_SEC.ID_UTILIZZO = TU_SEC.ID_UTILIZZO(+) "+
        "AND    RCM_SEC.ID_TIPO_DESTINAZIONE = TTD_SEC.ID_TIPO_DESTINAZIONE (+) "+
        "AND    RCM_SEC.ID_TIPO_DETTAGLIO_USO = TDU_SEC.ID_TIPO_DETTAGLIO_USO (+) "+
        "AND    RCM_SEC.ID_TIPO_QUALITA_USO = TQU_SEC.ID_TIPO_QUALITA_USO (+) "+
        "AND    RCM_SEC.ID_VARIETA = TV_SEC.ID_VARIETA(+) " +
        "AND    SP.COMUNE = F.COMUNE(+) "+
        "AND    NVL(SP.SEZIONE,-1) = NVL(F.SEZIONE(+),-1) "+
        "AND    SP.FOGLIO = F.FOGLIO(+) "+
        "ORDER BY DESC_COMUNE, " +
        "         SP.SEZIONE, " +
        "         SP.FOGLIO, " +
        "         SP.PARTICELLA," +
        "         SP.SUBALTERNO ";
      
      //query per la dichiarazione di consistenza
      String queryDic = 
        "SELECT CD.ID_CONDUZIONE_DICHIARATA, " +
        "       DC.DATA_INSERIMENTO_DICHIARAZIONE, " +
        "       C.DESCOM || ' (' || P.SIGLA_PROVINCIA || ')' AS DESC_COMUNE," +
        "       SP.SEZIONE, " +
        "       SP.FOGLIO, " +
        "       SP.PARTICELLA, " +
        "       SP.SUBALTERNO, "+
        "       TU.CODICE AS COD_USO, " +
        "       TU.DESCRIZIONE AS DESC_USO, " +
        "       TTD.CODICE_DESTINAZIONE AS COD_DEST_PRIM, " +
        "       TTD.DESCRIZIONE_DESTINAZIONE AS DESC_DEST_PRIM, "+
        "       TDU.CODICE_DETTAGLIO_USO AS COD_DETT_USO, " +
        "       TDU.DESCRIZIONE_DETTAGLIO_USO AS DESC_DETT_USO, "+
        "       TQU.CODICE_QUALITA_USO AS COD_QUAL_USO, " +
        "       TQU.DESCRIZIONE_QUALITA_USO AS DESC_QUAL_USO, "+
        "       TV.CODICE_VARIETA AS COD_VARIETA, " +
        "       TV.DESCRIZIONE AS DESC_VARIETA, " +
        "       SP.SUP_CATASTALE, " +
        "       SP.SUPERFICIE_GRAFICA, "+
        "       CD.ID_TITOLO_POSSESSO, " +
        "       CD.SUPERFICIE_CONDOTTA, " +
        "       CD.PERCENTUALE_POSSESSO, " +
        "       UD.SUPERFICIE_UTILIZZATA, " +
        "       CD.SUPERFICIE_AGRONOMICA, " +
        "       SP.ID_CASO_PARTICOLARE, "+
        "       SP.ID_AREA_A, " +
        "       SP.ID_AREA_B, " +
        "       SP.ID_AREA_C, " +
        "       SP.ID_AREA_D, " +
        "       DECODE(F.ID_AREA_E,2,'X') AS ZVN, " +
        "       SP.ID_ZONA_ALTIMETRICA, "+
        "       SP.FLAG_IRRIGABILE, " +
        "       SP.FLAG_CAPTAZIONE_POZZI, "+
        "       SP.ID_POTENZIALITA_IRRIGUA, " +
        "       SP.ID_ROTAZIONE_COLTURALE, " +
        "       SP.ID_TERRAZZAMENTO, " +
        "       SP.ID_AREA_M, "+
        "       TU_SEC.CODICE AS COD_USO_SEC, " +
        "       TU_SEC.DESCRIZIONE AS DESC_USO_SEC, " +
        "       TTD_SEC.CODICE_DESTINAZIONE AS COD_DEST_SEC, " +
        "       TTD_SEC.DESCRIZIONE_DESTINAZIONE AS DESC_DEST_SEC, "+
        "       TDU_SEC.CODICE_DETTAGLIO_USO AS COD_DETT_USO_SEC, " +
        "       TDU_SEC.DESCRIZIONE_DETTAGLIO_USO AS DESC_DETT_USO_SEC, "+
        "       TQU_SEC.CODICE_QUALITA_USO AS COD_QUAL_USO_SEC, " +
        "       TQU_SEC.DESCRIZIONE_QUALITA_USO AS DESC_QUAL_USO_SEC, "+
        "       TV_SEC.CODICE_VARIETA AS COD_VARIETA_SEC, " +
        "       TV_SEC.DESCRIZIONE AS DESC_VARIETA_SEC, "+
        "       UD.SUP_UTILIZZATA_SECONDARIA, " +
        "       SP.ID_PARTICELLA " +
        "FROM   DB_CONDUZIONE_DICHIARATA CD, " +
        "       DB_UTILIZZO_DICHIARATO UD, " +
        "       DB_R_CATALOGO_MATRICE RCM, " +
        "       DB_R_CATALOGO_MATRICE RCM_SEC, " +
        "       DB_STORICO_PARTICELLA SP, " +
        "       COMUNE C, " +
        "       PROVINCIA P," +
        "       DB_TIPO_UTILIZZO TU, " +
        "       DB_TIPO_DESTINAZIONE TTD, " +
        "       DB_TIPO_DETTAGLIO_USO TDU," +
        "       DB_TIPO_QUALITA_USO TQU," +
        "       DB_TIPO_VARIETA TV, " +
        "       DB_TIPO_UTILIZZO TU_SEC, " +
        "       DB_TIPO_DESTINAZIONE TTD_SEC, " +
        "       DB_TIPO_DETTAGLIO_USO TDU_SEC," +
        "       DB_TIPO_QUALITA_USO TQU_SEC," +
        "       DB_TIPO_VARIETA TV_SEC, " +
        "       DB_FOGLIO F, " +
        "       DB_DICHIARAZIONE_CONSISTENZA DC " +
        "WHERE  CD.CODICE_FOTOGRAFIA_TERRENI = ? " +
        "AND    CD.CODICE_FOTOGRAFIA_TERRENI = DC.CODICE_FOTOGRAFIA_TERRENI " +
        "AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA(+) " +
        "AND    SP.ID_STORICO_PARTICELLA = CD.ID_STORICO_PARTICELLA " +
        "AND    SP.COMUNE = C.ISTAT_COMUNE " +
        "AND    P.ISTAT_PROVINCIA = C.ISTAT_PROVINCIA " +
        "AND    UD.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE(+) "+
        "AND    RCM.ID_UTILIZZO = TU.ID_UTILIZZO(+) "+
        "AND    RCM.ID_TIPO_DESTINAZIONE = TTD.ID_TIPO_DESTINAZIONE (+) "+
        "AND    RCM.ID_TIPO_DETTAGLIO_USO = TDU.ID_TIPO_DETTAGLIO_USO (+) "+
        "AND    RCM.ID_TIPO_QUALITA_USO = TQU.ID_TIPO_QUALITA_USO (+) "+
        "AND    RCM.ID_VARIETA = TV.ID_VARIETA(+) " +
        "AND    UD.ID_CATALOGO_MATRICE_SECONDARIO = RCM_SEC.ID_CATALOGO_MATRICE(+) "+
        "AND    RCM_SEC.ID_UTILIZZO = TU_SEC.ID_UTILIZZO(+) "+
        "AND    RCM_SEC.ID_TIPO_DESTINAZIONE = TTD_SEC.ID_TIPO_DESTINAZIONE (+) "+
        "AND    RCM_SEC.ID_TIPO_DETTAGLIO_USO = TDU_SEC.ID_TIPO_DETTAGLIO_USO (+) "+
        "AND    RCM_SEC.ID_TIPO_QUALITA_USO = TQU_SEC.ID_TIPO_QUALITA_USO (+) "+
        "AND    RCM_SEC.ID_VARIETA = TV_SEC.ID_VARIETA(+) " +
        "AND    SP.COMUNE = F.COMUNE(+) " +
        "AND    NVL(SP.SEZIONE,-1) = NVL(F.SEZIONE(+),-1) " +
        "AND    SP.FOGLIO = F.FOGLIO(+) " +
        "ORDER BY DESC_COMUNE, " +
        "         SP.SEZIONE, " +
        "         SP.FOGLIO, " +
        "         SP.PARTICELLA, " +
        "         SP.SUBALTERNO ";
      

      String query=null;
      
      if (codFotografia==null)
      {
        query=querySitAtt; //sono alla situazione attuale
      }
      else
      {
        query=queryDic; //sono alla dichiarazione di consistenza
      }
      
      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query getElencoParticelleQuadroI1: " + query);
      SolmrLogger.debug(this, "Param idAzienda: " + idAzienda);
      SolmrLogger.debug(this, "Param codFotografia: " + codFotografia);

      if (codFotografia==null) //sono alla situazione attuale
        stmt.setLong(1, idAzienda.longValue());
      else
      {      //sono alla dichiarazione di consistenza
        stmt.setLong(1, codFotografia.longValue());
      }

      ResultSet rs = stmt.executeQuery();
      
      
      while (rs.next())
      {
        ParticellaVO particella=new ParticellaVO();
        particella.setDescComuneParticella(rs.getString("DESC_COMUNE"));
        particella.setSezione(rs.getString("SEZIONE"));
        try
        {
          particella.setFoglio(new Long(rs.getString("FOGLIO")));
        }
        catch(Exception e){}
        try
        {
          particella.setParticella(new Long(rs.getString("PARTICELLA")));
        }
        catch(Exception e){}
        particella.setSubalterno(rs.getString("SUBALTERNO"));
        particella.setSupCatastale(rs.getString("SUP_CATASTALE"));
        particella.setSuperficieGrafica(rs.getString("SUPERFICIE_GRAFICA"));
        particella.setIdTitoloPossesso(new Long(rs.getString("ID_TITOLO_POSSESSO")));
        particella.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
        particella.setSuperficieCondotta(rs.getString("SUPERFICIE_CONDOTTA"));
        particella.setSupAgronomico(rs.getString("SUPERFICIE_AGRONOMICA"));
        particella.setSuperficieUtilizzata(rs.getString("SUPERFICIE_UTILIZZATA"));
        
        String uso=rs.getString("COD_USO");
        if (uso!=null) uso="["+uso+"] "+rs.getString("DESC_USO");
        particella.setDescUtilizzoParticella(uso);
        
        String destinazione=rs.getString("COD_DEST_PRIM");
        if (destinazione!=null) destinazione="["+destinazione+"] "+rs.getString("DESC_DEST_PRIM");
        particella.setDescDestinazione(destinazione);
        
        String dettUso=rs.getString("COD_DETT_USO");
        if (dettUso!=null) dettUso="["+dettUso+"] "+rs.getString("DESC_DETT_USO");
        particella.setDescDettaglioUso(dettUso);
        
        String qualUso=rs.getString("COD_QUAL_USO");
        if (qualUso!=null) qualUso="["+qualUso+"] "+rs.getString("DESC_QUAL_USO");
        particella.setDescQualitaUso(qualUso);
        
        String var=rs.getString("COD_VARIETA");
        if (var!=null) var="["+var+"] "+rs.getString("DESC_VARIETA");
        particella.setDescVarieta(var);
        
        String usoSec=rs.getString("COD_USO_SEC");
        if (usoSec!=null) usoSec="["+usoSec+"] "+rs.getString("DESC_USO_SEC");
        particella.setDescUtilizzoParticellaSecondaria(usoSec);
        
        String destinazioneSec=rs.getString("COD_DEST_SEC");
        if (destinazioneSec!=null) destinazioneSec="["+destinazioneSec+"] "+rs.getString("DESC_DEST_SEC");
        particella.setDescDestinazioneSecondaria(destinazioneSec);
        
        String dettUsoSec=rs.getString("COD_DETT_USO_SEC");
        if (dettUsoSec!=null) dettUsoSec="["+dettUsoSec+"] "+rs.getString("DESC_DETT_USO_SEC");
        particella.setDescDettaglioUsoSecondario(dettUsoSec);
        
        String qualUsoSec=rs.getString("COD_QUAL_USO_SEC");
        if (qualUsoSec!=null) qualUsoSec="["+qualUsoSec+"] "+rs.getString("DESC_QUAL_USO_SEC");
        particella.setDescQualitaUsoSecondaria(qualUsoSec);
        
        
        String varSec=rs.getString("COD_VARIETA_SEC");
        if (varSec!=null) varSec="["+varSec+"] "+rs.getString("DESC_VARIETA_SEC");
        particella.setDescVarietaSecondaria(varSec);
        
        
        
        particella.setSuperficieUtilizzataSecondaria(rs.getBigDecimal("SUP_UTILIZZATA_SECONDARIA"));
        
        
        particella.setIdAreaA(checkLongNull(rs.getString("ID_AREA_A")));
        particella.setIdAreaB(checkLongNull(rs.getString("ID_AREA_B")));
        particella.setIdAreaC(checkLongNull(rs.getString("ID_AREA_C")));
        particella.setIdAreaD(checkLongNull(rs.getString("ID_AREA_D")));
        particella.setIdCasoParticolare(checkLongNull(rs.getString("ID_CASO_PARTICOLARE")));
        particella.setZonaVulnerabileNitrati(rs.getString("ZVN"));
        particella.setIdZonaAltimetrica(checkLongNull(rs.getString("ID_ZONA_ALTIMETRICA")));
        particella.setIdPotenzaIrrigua(checkLongNull(rs.getString("ID_POTENZIALITA_IRRIGUA")));
        particella.setIdRotazioneColturale(checkLongNull(rs.getString("ID_ROTAZIONE_COLTURALE")));
        particella.setIdTerrazzamento(checkLongNull(rs.getString("ID_TERRAZZAMENTO")));
        
      
        
        if (SolmrConstants.FLAG_S.equalsIgnoreCase(rs.getString("FLAG_IRRIGABILE")))
          particella.setFlagIrrigabile(true);
        
        if (SolmrConstants.FLAG_S.equalsIgnoreCase(rs.getString("FLAG_CAPTAZIONE_POZZI")))
          particella.setFlagCaptazionePozzi(true);
        
        
        
        String documento = null;
        if (codFotografia==null)
        {
          long idConduzioneParticella = rs.getLong("ID_CONDUZIONE_PARTICELLA");
          try
          {
            documento = getDocumentiFromIdConduzioneParticella(idConduzioneParticella, idAzienda);
          }
          catch(Exception e){}
        }
        else
        {
          long idConduzioneDichiarata = rs.getLong("ID_CONDUZIONE_DICHIARATA");
          java.util.Date dataInserimentoDichiarazione = rs.getDate("DATA_INSERIMENTO_DICHIARAZIONE");   
          try
          {
            documento = getDocumentiFromIdConduzioneDichiarata(
                idConduzioneDichiarata, idAzienda, dataInserimentoDichiarazione);
          }
          catch(Exception e){}
        }
        
        particella.setDocumento(documento);
        
        particella.setIdAreaM(checkLongNull(rs.getString("ID_AREA_M")));      
        particella.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
        
        
        result.add(particella);        
        
      }
      rs.close();

      SolmrLogger.debug(this, "getElencoParticelleQuadroI1 - Found " + result.size()
          + " record(s)");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getElencoParticelleQuadroI1 - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getElencoParticelleQuadroI1 - Generic Exception: "
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
        SolmrLogger.fatal(this,
            "getElencoParticelleQuadroI1 - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getElencoParticelleQuadroI1 - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  /**
   * 
   * @param idAzienda
   * @param codFotografia
   * @return restituisce un array di due elementi:
   * - il primo contiene la somma delle SUPERFICIE_CONDOTTA
   * - il secondo contiene la somma delle SUPERFICIE_AGRONOMICA
   * @throws DataAccessException
   */
  public BigDecimal[] getTotSupQuadroI1CondottaAndAgronomica(Long idAzienda,Long codFotografia) 
    throws DataAccessException
  {
    BigDecimal result[] = new BigDecimal[2];
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
  
      //query per la situazione attuale
      String querySitAtt = "" +
      	 "SELECT SUM (CP.SUPERFICIE_CONDOTTA) AS SUPERFICIE_CONDOTTA, " +
         "       SUM (DECODE(CP.ID_TITOLO_POSSESSO,5,CP.SUPERFICIE_CONDOTTA,CP.SUPERFICIE_AGRONOMICA)) AS SUPERFICIE_AGRONOMICA " +
         "FROM   DB_CONDUZIONE_PARTICELLA CP, " +
         "       DB_UTE U, " +
         "       DB_STORICO_PARTICELLA SP " +
         "WHERE  U.ID_AZIENDA = ? " +
         "AND    CP.ID_UTE = U.ID_UTE " +
         "AND    U.DATA_FINE_ATTIVITA IS NULL " +
         "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
         "AND    SP.ID_PARTICELLA = CP.ID_PARTICELLA " +
         "AND    SP.DATA_FINE_VALIDITA IS NULL";
      
      //query per la dichiarazione di consistenza
      String queryDic = "" +
      	 "SELECT SUM (CD.SUPERFICIE_CONDOTTA) AS SUPERFICIE_CONDOTTA, " +
      	 "       SUM (DECODE(CD.ID_TITOLO_POSSESSO,5,CD.SUPERFICIE_CONDOTTA,CD.SUPERFICIE_AGRONOMICA)) AS SUPERFICIE_AGRONOMICA " +
      	 "FROM   DB_CONDUZIONE_DICHIARATA CD, " +
      	 "       DB_STORICO_PARTICELLA SP " +
      	 "WHERE  CD.CODICE_FOTOGRAFIA_TERRENI = ? " +
      	 "AND    SP.ID_STORICO_PARTICELLA = CD.ID_STORICO_PARTICELLA";
      
  
      String query=null;
      
      if (codFotografia==null) query=querySitAtt; //sono alla situazione attuale
      else query=queryDic; //sono alla dichiarazione di consistenza
      
      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query getTotSupQuadroI1CondottaAndAgronomica: " + query);
      SolmrLogger.debug(this, "Param idAzienda: " + idAzienda);
      SolmrLogger.debug(this, "Param codFotografia: " + codFotografia);
  
      if (codFotografia==null) //sono alla situazione attuale
        stmt.setLong(1, idAzienda.longValue());
      else
      {      //sono alla dichiarazione di consistenza
        stmt.setLong(1, codFotografia.longValue());
      }
  
      ResultSet rs = stmt.executeQuery();
      
      
      while (rs.next())
      {
        
        result[0]=rs.getBigDecimal("SUPERFICIE_CONDOTTA");
        if (result[0]==null) result[0]=new BigDecimal(0);
        
        result[1]=rs.getBigDecimal("SUPERFICIE_AGRONOMICA");
        if (result[1]==null) result[1]=new BigDecimal(0);
      }
      rs.close();
  
      SolmrLogger.debug(this, "getTotSupQuadroI1CondottaAndAgronomica - Executed ");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTotSupQuadroI1CondottaAndAgronomica - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getTotSupQuadroI1CondottaAndAgronomica - Generic Exception: "
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
        SolmrLogger.fatal(this,
            "getTotSupQuadroI1CondottaAndAgronomica - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getTotSupQuadroI1CondottaAndAgronomica - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  /**
   * 
   * @param idAzienda
   * @param codFotografia
   * @return restituisce BigDecimal
   * contenente la somma delle SUP_CATASTALE
   * @throws DataAccessException
   */
  public BigDecimal[] getTotSupQuadroI1CatastaleAndGrafica(Long idAzienda, Long codFotografia) 
    throws DataAccessException
  {
    BigDecimal[] result = new BigDecimal[2];
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
  
      //query per la situazione attuale
      String querySitAtt = "" +
      	 "SELECT SUM (SP.SUP_CATASTALE) AS SUP_CATASTALE," +
      	 "       SUM (SP.SUPERFICIE_GRAFICA) AS SUPERFICIE_GRAFICA " +
         "FROM DB_STORICO_PARTICELLA SP " +
         "WHERE SP.ID_PARTICELLA IN ( SELECT CP.ID_PARTICELLA" +
         "                            FROM   DB_CONDUZIONE_PARTICELLA CP, " +
         "                                   DB_UTE U" +
         "                            WHERE  U.ID_AZIENDA = ? " +
         "                            AND    CP.ID_UTE = U.ID_UTE " +
         "                            AND    U.DATA_FINE_ATTIVITA IS NULL " +
         "                            AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
         "                           ) " +
         "AND   SP.DATA_FINE_VALIDITA IS NULL";
      
      //query per la dichiarazione di consistenza
      String queryDic = "" +
      		"SELECT SUM (SP.SUP_CATASTALE) AS SUP_CATASTALE," +
      		"        SUM (SP.SUPERFICIE_GRAFICA) AS SUPERFICIE_GRAFICA " +
          "FROM   DB_STORICO_PARTICELLA SP " +
          "WHERE  SP.ID_STORICO_PARTICELLA IN ( SELECT CD.ID_STORICO_PARTICELLA" +
          "                                     FROM   DB_CONDUZIONE_DICHIARATA CD " +
          "                                     WHERE CD.CODICE_FOTOGRAFIA_TERRENI = ? " +
          "                                   ) ";
      
  
      String query=null;
      
      if (codFotografia==null) query=querySitAtt; //sono alla situazione attuale
      else query=queryDic; //sono alla dichiarazione di consistenza
      
      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query getTotSupQuadroI1CatastaleAndGrafica: " + query);
      SolmrLogger.debug(this, "Param idAzienda: " + idAzienda);
      SolmrLogger.debug(this, "Param codFotografia: " + codFotografia);
  
      if (codFotografia==null) //sono alla situazione attuale
        stmt.setLong(1, idAzienda.longValue());
      else
      {      //sono alla dichiarazione di consistenza
        stmt.setLong(1, codFotografia.longValue());
      }
  
      ResultSet rs = stmt.executeQuery();
      
      
      if (rs.next())
      {
        result[0] = rs.getBigDecimal("SUP_CATASTALE");
        if (result[0]==null)
        {
          result[0] = new BigDecimal(0);
        }
        result[1] = rs.getBigDecimal("SUPERFICIE_GRAFICA");
        if (result[1]==null)
        {
          result[1] = new BigDecimal(0);
        }
        
      }
      rs.close();
  
      SolmrLogger.debug(this, "getTotSupQuadroI1CatastaleAndGrafica - Executed ");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTotSupQuadroI1CatastaleAndGrafica - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getTotSupQuadroI1CatastaleAndGrafica - Generic Exception: "
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
        SolmrLogger.fatal(this,
            "getTotSupQuadroI1CatastaleAndGrafica - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getTotSupQuadroI1CatastaleAndGrafica - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  

  // ===================================================================
  // QuadroI1
  //
  // Estraggo il codice fotografia della dichiarazione di consistenza
  // ===================================================================
  public Long getCodFotTerreniQuadroI1(Long idDichiarazioneConsistenza)
      throws DataAccessException
  {
    Long codFotografiaTerreni = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
      String query = "SELECT CODICE_FOTOGRAFIA_TERRENI "
          + "FROM DB_DICHIARAZIONE_CONSISTENZA "
          + "WHERE ID_DICHIARAZIONE_CONSISTENZA=?";

      SolmrLogger.debug(this,
          "StampeDAO.getCodFotTerreniQuadroI1 - Executing query: " + query);
      SolmrLogger.debug(this,
          "StampeDAO.getCodFotTerreniQuadroI1 - idDichiarazioneConsistenza: "
              + idDichiarazioneConsistenza);
      stmt = conn.prepareStatement(query);
      stmt.setLong(1, idDichiarazioneConsistenza.longValue());

      ResultSet rs = stmt.executeQuery();
      if (rs.next())
        codFotografiaTerreni = new Long((rs
            .getLong("CODICE_FOTOGRAFIA_TERRENI")));
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "StampeDAO.getCodFotTerreniQuadroI1 - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "StampeDAO.getCodFotTerreniQuadroI1 - Generic Exception: "
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
                "StampeDAO.getCodFotTerreniQuadroI1 - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "StampeDAO.getCodFotTerreniQuadroI1 - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return codFotografiaTerreni;
  }



  // ===================================================================
  // Quadro N
  //
  // Anomalie
  // ===================================================================
  public Vector<String[]> getAnomalie(Long idAzienda, Long idDichiarazioneConsistenza)
      throws DataAccessException
  {
    Vector<String[]> result = new Vector<String[]>();
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "" +
      		"SELECT TGC.DESCRIZIONE DESCR_TIPO_CONTROLLO, " +
          "       TC.DESCRIZIONE DESCR_CONTROLLO, " +
          "       DS.BLOCCANTE, " +
          "       DS.DESCRIZIONE DESCR_SEGNALAZIONE, " +
          "       TO_CHAR(DS.DATA_CONTROLLO, 'dd/mm/yyyy') DATA_ULTIMO_CONTROLLO, " +
          "       DC.ID_DICHIARAZIONE_CORREZIONE, " +
          "       DC.ID_DOCUMENTO_PROTOCOLLATO, " +
          "       DC.RIFERIMENTO_DOCUMENTO," +
          "       D.NUMERO_PROTOCOLLO, " +
          "       TO_CHAR(D.DATA_PROTOCOLLO, 'dd/mm/yyyy') DATA_PROTOCOLLO, " +
          "       DTC.DESCRIZIONE AS DEC_DOCUMENTO, " +
          "       DTC2.DESCRIZIONE AS DEC_DOCUMENTO_PROT " +
          "FROM   DB_DICHIARAZIONE_SEGNALAZIONE DS, " +
          "       DB_TIPO_CONTROLLO TC, " +
          "       DB_TIPO_GRUPPO_CONTROLLO TGC, " +
          "       DB_DICHIARAZIONE_CORREZIONE DC, " +
          "       DB_TIPO_DOCUMENTO DTC, " +
          "       DB_TIPO_DOCUMENTO DTC2, " +
          "       DB_DOCUMENTO D " +
          // "     DB_STORICO_PARTICELLA SP,COMUNE C "
          "WHERE  DS.ID_AZIENDA = ? ";
      if (idDichiarazioneConsistenza != null)
      {
        query = query + "" +
        	"AND    DS.ID_DICHIARAZIONE_CONSISTENZA = ? ";
      }
      else
      {
        query = query + "" +
          "AND    DS.ID_DICHIARAZIONE_CONSISTENZA IS NULL ";
      }
      query = query + "" +
      		"AND    DS.ID_CONTROLLO = TC.ID_CONTROLLO " +
          "AND    TC.ID_GRUPPO_CONTROLLO = TGC.ID_GRUPPO_CONTROLLO " +
          "AND    DS.ID_AZIENDA = DC.ID_AZIENDA(+) " +
          "AND    NVL(DS.ID_DICHIARAZIONE_CONSISTENZA,-1) = NVL(DC.ID_DICHIARAZIONE_CONSISTENZA(+),-1) " +
          "AND    DS.ID_CONTROLLO = DC.ID_CONTROLLO(+) " +
          "AND    NVL(DS.ID_STORICO_PARTICELLA,-1) = NVL(DC.ID_STORICO_PARTICELLA(+),-1) " +
          "AND    DC.EXT_ID_DOCUMENTO = DTC.ID_DOCUMENTO(+) " +
          "AND    D.EXT_ID_DOCUMENTO = DTC2.ID_DOCUMENTO(+) " +
          "AND    DC.ID_DOCUMENTO_PROTOCOLLATO = D.ID_DOCUMENTO(+) " +
          //"AND nvl(ds.ID_STORICO_PARTICELLA,-1)=nvl(SP.ID_STORICO_PARTICELLA(+),-1) "
          //"AND SP.COMUNE=C.ISTAT_COMUNE(+) "
          "ORDER BY " +
          "       TGC.ID_GRUPPO_CONTROLLO," +
          "       TC.ORDINAMENTO," +
          "       DS.DESCRIZIONE";
          //"C.DESCOM,SP.SEZIONE,SP.FOGLIO,SP.PARTICELLA,SP.SUBALTERNO ";

      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setLong(1, idAzienda.longValue());
      if (idDichiarazioneConsistenza != null)
        stmt.setLong(2, idDichiarazioneConsistenza.longValue());

      ResultSet rs = stmt.executeQuery();

      String[] anomalia;
      String bloccante;
      while (rs.next())
      {
        anomalia = new String[6];
        anomalia[0] = rs.getString("DESCR_TIPO_CONTROLLO");
        anomalia[1] = rs.getString("DESCR_CONTROLLO");
        bloccante = rs.getString("BLOCCANTE");
        anomalia[2] = ("N".equals(bloccante) ? "NO" : "SI");
        anomalia[3] = rs.getString("DESCR_SEGNALAZIONE");
        anomalia[4] = rs.getString("DATA_ULTIMO_CONTROLLO");
        
        
        if (rs.getString("ID_DICHIARAZIONE_CORREZIONE")!=null)
        {
          //Se esiste un record su db_dichiarazione_correzione
          if (rs.getString("ID_DOCUMENTO_PROTOCOLLATO")!=null)
          {
            anomalia[5] = rs.getString("DEC_DOCUMENTO_PROT");
            //se db_dichiarazione_correzione.id_documento_protocollato è valorizzato visualizzare: 
            //decodifica di db_documento.ext_id_documento + ““Repertorio N.” + db_documento.numero_protocollo 
            //+ “del “ + db_documento.data_protocollo (fmt dd/mm/yyyy)
            anomalia[5] +=" Repertorio N.:"+rs.getString("NUMERO_PROTOCOLLO");
            anomalia[5] +="   del "+rs.getString("DATA_PROTOCOLLO");
          }
          else
          {
            anomalia[5] = rs.getString("DEC_DOCUMENTO");
            //se db_dichiarazione_correzione.id_documento_protocollato è null visualizzare:             
            //decodifica db_dichiarazione_correzione.ext_id_documento +            “Rif.:”
            //+ db_dichiarazione_correzione.riferimento_documento.  
            anomalia[5] +=" Rif.:"+rs.getString("RIFERIMENTO_DOCUMENTO");
          }
          
        }
        
        
        result.add(anomalia);
      }

      rs.close();

      SolmrLogger.debug(this, "getAnomalie - Found " + result.size()
          + " record(s)");
    }
    catch (SQLException exc)
    {
      SolmrLogger
          .fatal(this, "getAnomalie - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getAnomalie - Generic Exception: "
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
        SolmrLogger.fatal(this,
            "getAnomalie - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "getAnomalie - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // ===================================================================
  // Quadro L - fabbricati
  //
  // Recupero fabbricati con idAzienda e dataRiferimento
  // ===================================================================
  public Vector<FabbricatoVO> getFabbricati(Long idAzienda, java.util.Date dataRiferimento,boolean comunicazione10R)
      throws DataAccessException
  {
    Vector<FabbricatoVO> result = new Vector<FabbricatoVO>();
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT fabb.ID_FABBRICATO, ";
        if (comunicazione10R)
          query+= "       c.DESCOM || ' (' || p.SIGLA_PROVINCIA || ') - ' || ute.INDIRIZZO as UBICAZIONE, ";
        else
          query+= "       c.DESCOM || ' (' || p.SIGLA_PROVINCIA || ') - Istat: ' || c.ISTAT_COMUNE as UBICAZIONE, ";  
  query+= "       tipo.DESCRIZIONE as TIPO,  "
          + "     fabb.SUPERFICIE,  "
          + "     tipo.UNITA_MISURA,  "
          + "     fabb.DIMENSIONE,  "
          + "     fabb.ANNO_COSTRUZIONE,  "
          + "     fabb.MESI_RISCALDAMENTO_SERRA, "
          + "     fabb.ORE_RISCALDAMENTO_SERRA, "
          + "     TCS.DESCRIZIONE AS DESCRIZIONE_SERRA, "
          + "     TFF.DESCRIZIONE AS DESC_TIPO_FORMA_FABBRICATO, "
          + "     fabb.ID_COLTURA_SERRA "
          + "FROM DB_FABBRICATO fabb,  "
          + "     DB_UTE ute,  "
          + "     DB_TIPO_TIPOLOGIA_FABBRICATO tipo, "
          + "     COMUNE c, "
          + "     PROVINCIA p, "
          + "     DB_TIPO_COLTURA_SERRA TCS, "
          + "     DB_TIPO_FORMA_FABBRICATO TFF "
          + "WHERE ute.ID_AZIENDA=?  "
          + "  AND fabb.ID_UTE= UTE.ID_UTE  "
          + "  AND fabb.id_tipologia_fabbricato=tipo.id_tipologia_fabbricato "
          + "  AND ute.COMUNE = c.ISTAT_COMUNE "
          + "  AND c.ISTAT_PROVINCIA = p.ISTAT_PROVINCIA  "
          + "  AND fabb.ID_COLTURA_SERRA = TCS.ID_COLTURA_SERRA(+) "
          + "  AND fabb.ID_FORMA_FABBRICATO = TFF.ID_FORMA_FABBRICATO(+) "
          + "  AND fabb.DATA_INIZIO_VALIDITA <= ? "
          + "  AND NVL(fabb.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) > ? "
          +

          /*
           * " AND ((fabb.DATA_FINE_VALIDITA IS NULL and
           * fabb.data_inizio_validita <= ?) "+ " or ? between
           * fabb.data_inizio_validita and fabb.data_fine_validita) "+
           */

          /*
           * " AND ( fabb.DATA_FINE_VALIDITA IS NULL "+ " OR ? BETWEEN
           * fabb.DATA_INIZIO_VALIDITA AND fabb.DATA_FINE_VALIDITA) "+
           */
          "ORDER BY UBICAZIONE, TIPO, SUPERFICIE";

      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query getFabbricati: " + query);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setTimestamp(2, convertDateToTimestamp(dataRiferimento));
      stmt.setTimestamp(3, convertDateToTimestamp(dataRiferimento));

      ResultSet rs = stmt.executeQuery();
      String idFabbricato;
      while (rs.next())
      {
        FabbricatoVO fVO = new FabbricatoVO();
        idFabbricato = rs.getString("ID_FABBRICATO");
        fVO.setIdFabbricato(idFabbricato == null ? null : new Long(rs
            .getLong("ID_FABBRICATO")));
        fVO.setDescComuneUte(rs.getString("UBICAZIONE"));
        fVO.setDescrizioneTipoFabbricato(rs.getString("TIPO"));
        fVO.setSuperficieFabbricato(rs.getString("SUPERFICIE"));
        fVO.setUnitaMisura(rs.getString("UNITA_MISURA"));
        fVO.setDimensioneFabbricato(rs.getString("DIMENSIONE"));
        fVO.setAnnoCostruzioneFabbricato(rs.getString("ANNO_COSTRUZIONE"));
        fVO.setTipologiaColturaSerra(rs.getString("ID_COLTURA_SERRA"));
        fVO.setDescrizioneTipoFormaFabbricato(rs
            .getString("DESC_TIPO_FORMA_FABBRICATO"));
        fVO.setMesiRiscSerra(rs.getString("MESI_RISCALDAMENTO_SERRA"));
        fVO.setOreRisc(rs.getString("ORE_RISCALDAMENTO_SERRA"));
        fVO.setDescrizioneTipologiaColturaSerra(rs
            .getString("DESCRIZIONE_SERRA"));

        result.add(fVO);
      }

      rs.close();

      SolmrLogger.debug(this, "getFabbricati - Found " + result.size()
          + " record(s)");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getFabbricati - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getFabbricati - Generic Exception: "
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
        SolmrLogger.fatal(this,
            "getFabbricati - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "getFabbricati - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // ===================================================================
  // Quadro L - fabbricati
  //
  // Recupero le particelle associate ad un fabbricato fabbricati con
  // idFabbricato
  // e dataRiferimento
  // ===================================================================
  public Vector<ParticellaVO> getFabbricatiParticelle(Long idFabbricato,
      java.util.Date dataRiferimento) throws DataAccessException
  {
    Vector<ParticellaVO> result = new Vector<ParticellaVO>();
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT P.SIGLA_PROVINCIA,C.DESCOM,"
          + "       PART.SEZIONE,PART.FOGLIO,PART.PARTICELLA,"
          + "       PART.SUBALTERNO,PART.SUP_CATASTALE "
          + "FROM DB_FABBRICATO_PARTICELLA FP,"
          + "     DB_STORICO_PARTICELLA PART,"
          + "     COMUNE C,PROVINCIA P "
          + "WHERE FP.ID_FABBRICATO=?"
          + "  AND FP.ID_PARTICELLA=PART.ID_PARTICELLA"
          + "  AND PART.DATA_FINE_VALIDITA IS NULL "
          + "  AND TRUNC(FP.DATA_INIZIO_VALIDITA) <= ? "
          + "  AND NVL(TRUNC(FP.DATA_FINE_VALIDITA), TO_DATE('31/12/9999','dd/mm/yyyy')) > ? "
          + "  AND PART.COMUNE=C.ISTAT_COMUNE(+)"
          + "  AND C.ISTAT_PROVINCIA=P.ISTAT_PROVINCIA(+)"
          + "  ORDER BY P.SIGLA_PROVINCIA,C.DESCOM";

      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setLong(1, idFabbricato.longValue());
      stmt.setDate(2, this.checkDate(dataRiferimento));
      stmt.setDate(3, this.checkDate(dataRiferimento));

      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {
        ParticellaVO pVO = new ParticellaVO();
        pVO.setSiglaProvinciaParticella(rs.getString("SIGLA_PROVINCIA"));
        pVO.setDescComuneParticella(rs.getString("DESCOM"));
        pVO.setSezione(rs.getString("SEZIONE"));
        pVO.setStrFoglio(rs.getString("FOGLIO"));
        pVO.setStrParticella(rs.getString("PARTICELLA"));
        pVO.setSubalterno(rs.getString("SUBALTERNO"));
        pVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
        result.add(pVO);
      }

      rs.close();

      SolmrLogger.debug(this, "getFabbricatiParticelle - Found " + result.size()
          + " record(s)");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getFabbricatiParticelle - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getFabbricatiParticelle - Generic Exception: "
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
                "getFabbricatiParticelle - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getFabbricatiParticelle - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // ===================================================================
  // Quadro O - Documenti
  //
  // Stampa alla situazione attiva
  //
  // Recupero i documenti associati all'azienda
  // ===================================================================
  public Vector<DocumentoVO> getDocumentiStampa(Long idAzienda, String cuaa)
      throws DataAccessException
  {
    SolmrLogger.debug(this, "Invocating getDocumentiStampa method in StampeDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<DocumentoVO> elencoDocumenti = new Vector<DocumentoVO>();

    try
    {
      SolmrLogger.debug(this,
          "Creating db-connection in getDocumentiStampa method in StampeDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this,
          "Created db-connection in getDocumentiStampa method in StampeDAO and it values: "
              + conn + "\n");

      String query = ""
      		+ "SELECT DISTINCT "
      		+ "       D.ID_DOCUMENTO, "
          + "       TTD.DESCRIZIONE AS TIPO_DOCUMENTO, "
          + "       TD.DESCRIZIONE AS DESC_DOCUMENTO, "
          + "       D.DATA_INIZIO_VALIDITA, "
          + "       D.DATA_FINE_VALIDITA, "
          + "       D.NUMERO_PROTOCOLLO, "
          + "       D.DATA_PROTOCOLLO, "
          + "       D.ID_STATO_DOCUMENTO, "
          + "       TTD.ID_TIPOLOGIA_DOCUMENTO, "
          + "       TD.ID_DOCUMENTO "
          + "FROM   DB_DOCUMENTO D, "
          + "       DB_TIPO_TIPOLOGIA_DOCUMENTO TTD, "
          + "       DB_TIPO_DOCUMENTO TD, "
          + "       DB_TIPO_CATEGORIA_DOCUMENTO TCD, "
          + "       DB_DOCUMENTO_CATEGORIA DC "
          + "WHERE  D.CUAA = ? "
          + "AND    D.ID_AZIENDA = ? "
          + "AND    ("
          + "        D.ID_STATO_DOCUMENTO IS NULL "
          + "        OR "
          + "        (D.ID_STATO_DOCUMENTO <> 1 AND      D.ID_STATO_DOCUMENTO <> 2) "
          + "       ) "
          + "AND    (D.DATA_FINE_VALIDITA IS NULL OR D.DATA_FINE_VALIDITA > SYSDATE) "
          + "AND    TTD.ID_TIPOLOGIA_DOCUMENTO = TD.ID_TIPOLOGIA_DOCUMENTO "
          + "AND    TD.ID_DOCUMENTO = D.EXT_ID_DOCUMENTO "
          + "AND    TD.ID_DOCUMENTO = DC.ID_DOCUMENTO "
          + "AND    DC.ID_CATEGORIA_DOCUMENTO = TCD.ID_CATEGORIA_DOCUMENTO "
          + "AND    TCD.TIPO_IDENTIFICATIVO <> 'TP' "
          + "ORDER BY "
          + "       D.ID_STATO_DOCUMENTO DESC, "
          + "       TTD.ID_TIPOLOGIA_DOCUMENTO, "
          + "       TD.ID_DOCUMENTO";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Value of parameter 1 [CUAA] in getDocumentiStampa method in StampeDAO: "
              + cuaa + "\n");
      stmt.setString(1, cuaa);
      SolmrLogger.debug(this,
          "Value of parameter 2 [ID_AZIENDA] in getDocumentiStampa method in StampeDAO: "
              + idAzienda + "\n");
      stmt.setLong(2, idAzienda.longValue());

      SolmrLogger.debug(this, "Executing getDocumentiStampa: " + query + "\n");

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        DocumentoVO documentoEsitoVO = new DocumentoVO();
        TipoDocumentoVO tipoDocumentoVO = new TipoDocumentoVO();
        documentoEsitoVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
        tipoDocumentoVO.setDescrizione(rs.getString("DESC_DOCUMENTO"));
        CodeDescription tipoTipologiaDocumento = new CodeDescription(null, rs
            .getString("TIPO_DOCUMENTO"));
        tipoDocumentoVO.setTipoTipologiaDocumento(tipoTipologiaDocumento);
        documentoEsitoVO.setDataInizioValidita(new java.util.Date(rs.getDate(
            "DATA_INIZIO_VALIDITA").getTime()));
        try
        {
          documentoEsitoVO.setDataFineValidita(new java.util.Date(rs.getDate(
              "DATA_FINE_VALIDITA").getTime()));
        }
        catch (Exception e)
        {
        }
        documentoEsitoVO.setNumeroProtocollo(rs.getString("NUMERO_PROTOCOLLO"));
        try
        {
          documentoEsitoVO.setDataProtocollo(rs.getDate("DATA_PROTOCOLLO"));
        }
        catch (Exception e)
        {
        }
        documentoEsitoVO.setTipoDocumentoVO(tipoDocumentoVO);
        elencoDocumenti.add(documentoEsitoVO);
      }

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "getDocumentiStampa in StampeDAO - SQLException: "
          + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this, "getDocumentiStampa in StampeDAO - Generic Exception: "
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
            .fatal(
                this,
                "getDocumentiStampa in StampeDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getDocumentiStampa in StampeDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getDocumentiStampa method in StampeDAO\n");
    return elencoDocumenti;
  }

  // ===================================================================
  // Quadro O - Documenti
  //
  // Stampa alla data della consistenza
  //
  // Recupero i documenti associati all'azienda
  // ===================================================================
  public Vector<DocumentoVO> getDocumentiStampaDichCons(Long idAzienda, java.util.Date dataConsistenza)
      throws DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating getDocumentiStampaDichCons method in StampeDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<DocumentoVO> elencoDocumenti = new Vector<DocumentoVO>();

    try
    {
      SolmrLogger.debug(this,
          "Creating db-connection in getDocumentiStampaDichCons method in StampeDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this,
          "Created db-connection in getDocumentiStampaDichCons method in StampeDAO and it values: "
              + conn + "\n");

      String query = ""
      		+ "SELECT DISTINCT "
      		+ "       D.ID_DOCUMENTO, "
          + "       TTD.DESCRIZIONE AS TIPO_DOCUMENTO, "
          + "       TD.DESCRIZIONE AS DESC_DOCUMENTO, "
          + "       D.DATA_INIZIO_VALIDITA, " 
          + "       D.DATA_FINE_VALIDITA, "
          + "       D.NUMERO_PROTOCOLLO, "  
          + "       D.DATA_PROTOCOLLO, "
          + "       D.ID_STATO_DOCUMENTO, "
          + "       TTD.ID_TIPOLOGIA_DOCUMENTO, "
          + "       TD.ID_DOCUMENTO "
          + "FROM   DB_DOCUMENTO D, "
          + "       DB_TIPO_TIPOLOGIA_DOCUMENTO TTD, "
          + "       DB_TIPO_DOCUMENTO TD, "
          + "       DB_TIPO_CATEGORIA_DOCUMENTO TCD, "
          + "       DB_DOCUMENTO_CATEGORIA DC "
          + "WHERE  D.ID_AZIENDA = ? "
          + "AND    D.DATA_INSERIMENTO <= ? "
          + "AND    ( D.DATA_VARIAZIONE_STATO IS NULL OR "
          + "         D.DATA_VARIAZIONE_STATO > ? ) "
          + "AND    TTD.ID_TIPOLOGIA_DOCUMENTO = TD.ID_TIPOLOGIA_DOCUMENTO "
          + "AND    TD.ID_DOCUMENTO = D.EXT_ID_DOCUMENTO "
          + "AND    TD.ID_DOCUMENTO = DC.ID_DOCUMENTO "
          + "AND    DC.ID_CATEGORIA_DOCUMENTO = TCD.ID_CATEGORIA_DOCUMENTO "
          + "AND    TCD.TIPO_IDENTIFICATIVO <> 'TP' "
          + "AND    D.CUAA = " 
          + "                ( "
          + "                  SELECT CUAA FROM DB_ANAGRAFICA_AZIENDA "
          + "                  WHERE ID_AZIENDA = ? " 
          + "                  AND " 
          + "                      ( "
          + "                       (DATA_INIZIO_VALIDITA <= ? AND DATA_FINE_VALIDITA IS NULL) "
          + "                        OR  "
          + "                       (DATA_INIZIO_VALIDITA <= ? AND DATA_FINE_VALIDITA >= ?) " 
          + "                      ) "
          + "                ) "
          +	"ORDER BY "
          +	"      D.ID_STATO_DOCUMENTO DESC, "
          +	"      TTD.ID_TIPOLOGIA_DOCUMENTO, "
          + "      TD.ID_DOCUMENTO";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Value of parameter 2 [ID_AZIENDA] in getDocumentiStampaDichCons method in StampeDAO: "
              + idAzienda + "\n");
      stmt.setLong(1, idAzienda.longValue());

      SolmrLogger.debug(this,
          "Value of parameter 1 [dataConsistenza] in getDocumentiStampaDichCons method in StampeDAO: "
              + dataConsistenza + "\n");
      stmt.setTimestamp(2, this.convertDateToTimestamp(dataConsistenza));
      stmt.setTimestamp(3, this.convertDateToTimestamp(dataConsistenza));
      stmt.setLong(4, idAzienda.longValue());
      stmt.setTimestamp(5, this.convertDateToTimestamp(dataConsistenza));
      stmt.setTimestamp(6, this.convertDateToTimestamp(dataConsistenza));
      stmt.setTimestamp(7, this.convertDateToTimestamp(dataConsistenza));

      SolmrLogger
          .debug(this, "Executing getDocumentiStampaDichCons: " + query + "\n");

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        DocumentoVO documentoEsitoVO = new DocumentoVO();
        TipoDocumentoVO tipoDocumentoVO = new TipoDocumentoVO();
        documentoEsitoVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
        tipoDocumentoVO.setDescrizione(rs.getString("DESC_DOCUMENTO"));
        CodeDescription tipoTipologiaDocumento = new CodeDescription(null, rs
            .getString("TIPO_DOCUMENTO"));
        tipoDocumentoVO.setTipoTipologiaDocumento(tipoTipologiaDocumento);
        documentoEsitoVO.setDataInizioValidita(new java.util.Date(rs.getDate(
            "DATA_INIZIO_VALIDITA").getTime()));
        try
        {
          documentoEsitoVO.setDataFineValidita(new java.util.Date(rs.getDate(
              "DATA_FINE_VALIDITA").getTime()));
        }
        catch (Exception e)
        {
        }
        documentoEsitoVO.setNumeroProtocollo(rs.getString("NUMERO_PROTOCOLLO"));
        try
        {
          documentoEsitoVO.setDataProtocollo(rs.getDate("DATA_PROTOCOLLO"));
        }
        catch (Exception e)
        {
        }
        documentoEsitoVO.setTipoDocumentoVO(tipoDocumentoVO);
        elencoDocumenti.add(documentoEsitoVO);
      }

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "getDocumentiStampaDichCons in StampeDAO - SQLException: "
          + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this, "getDocumentiStampaDichCons in StampeDAO - Generic Exception: "
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
            .fatal(
                this,
                "getDocumentiStampaDichCons in StampeDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getDocumentiStampaDichCons in StampeDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getDocumentiStampaDichCons method in StampeDAO\n");
    return elencoDocumenti;
  }

  /**
   * Metodo che si occupa di reperire i proprietari del documento relativamente
   * ad una data di dichiarazione della consistenza
   *
   * @param idDocumento
   *          Long
   * @return Vector
   * @throws DataAccessException
   * @throws SolmrException
   */
  public Vector<DocumentoProprietarioVO> getListProprietariDocumento(Long idDocumento,
      java.util.Date dataConsistenza) throws DataAccessException,
      SolmrException
  {
    SolmrLogger.debug(this,
        "Invocating getListProprietariDocumento method in StampeDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<DocumentoProprietarioVO> elencoProprietari = new Vector<DocumentoProprietarioVO>();

    try
    {
      SolmrLogger
          .debug(this,
              "Creating db-connection in getListProprietariDocumento method in StampeDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in getListProprietariDocumento method in StampeDAO and it values: "
                  + conn + "\n");

      String query = " SELECT   ID_DOCUMENTO_PROPRIETARIO, "
          + "          ID_DOCUMENTO, " + "          CUAA, "
          + "          DENOMINAZIONE, "
          + "          DATA_ULTIMO_AGGIORNAMENTO, "
          + "          UTENTE_ULTIMO_AGGIORNAMENTO, "
          + "          DATA_INSERIMENTO "
          + " FROM     DB_DOCUMENTO_PROPRIETARIO "
          + " WHERE    ID_DOCUMENTO = ? " + "   AND DATA_INSERIMENTO <=? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [ID_DOCUMENTO] in getListProprietariDocumento method in StampeDAO: "
                  + idDocumento + "\n");
      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [DATA_INSERIMENTO] in getListProprietariDocumento method in StampeDAO: "
                  + dataConsistenza + "\n");
      stmt.setLong(1, idDocumento.longValue());
      stmt.setTimestamp(2, this.convertDateToTimestamp(dataConsistenza));

      SolmrLogger.debug(this, "Executing getListProprietariDocumento: " + query
          + "\n");

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        DocumentoProprietarioVO documentoProprietarioVO = new DocumentoProprietarioVO();
        documentoProprietarioVO.setIdDocumentoProprietario(new Long(rs
            .getLong(1)));
        documentoProprietarioVO.setIdDocumento(new Long(rs.getLong(2)));
        documentoProprietarioVO.setCuaa(rs.getString(3));
        documentoProprietarioVO.setDenominazione(rs.getString(4));
        documentoProprietarioVO.setDataUltimoAggiornamento(new java.util.Date(
            rs.getDate(5).getTime()));
        documentoProprietarioVO.setUtenteUltimoAggiornamento(new Long(rs
            .getLong(6)));
        documentoProprietarioVO.setDataInserimento(new java.util.Date(rs
            .getDate(7).getTime()));
        elencoProprietari.add(documentoProprietarioVO);
      }
      stmt.close();
      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "getListProprietariDocumento in StampeDAO - SQLException: "
              + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "getListProprietariDocumento in StampeDAO - Generic Exception: " + ex
              + "\n");
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
                "getListProprietariDocumento in StampeDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getListProprietariDocumento in StampeDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger
        .debug(this,
            "Invocated getListTipoDocumentoByIdTipologiaDocumento method in StampeDAO\n");
    return elencoProprietari;
  }

  /**
   * Metodo che si occupa di reperire tutti le conduzioni legate al documento
   * relativamente ad una data di dichiarazione della consistenza
   *
   * @param idDocumento
   *          Long
   * @return Vector
   * @throws DataAccessException
   * @throws SolmrException
   */
  public Vector<DocumentoConduzioneVO> getListDocumentoConduzioni(Long idDocumento,
      java.util.Date dataConsistenza) throws DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating getListDocumentoConduzioni method in StampeDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<DocumentoConduzioneVO> elencoConduzioni = new Vector<DocumentoConduzioneVO>();

    try
    {
      SolmrLogger
          .debug(this,
              "Creating db-connection in getListDocumentoConduzioni method in StampeDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in getListDocumentoConduzioni method in StampeDAO and it values: "
                  + conn + "\n");

      String query = " SELECT   ID_DOCUMENTO_CONDUZIONE, "
          + "          ID_CONDUZIONE_PARTICELLA, " + "          ID_DOCUMENTO, "
          + "          DATA_INSERIMENTO "
          + " FROM     DB_DOCUMENTO_CONDUZIONE "
          + " WHERE    ID_DOCUMENTO = ? " + "   AND DATA_INSERIMENTO <=? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [ID_DOCUMENTO] in getListDocumentoConduzioni method in StampeDAO: "
                  + idDocumento + "\n");
      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [DATA_INSERIMENTO] in getListProprietariDocumento method in StampeDAO: "
                  + dataConsistenza + "\n");
      stmt.setLong(1, idDocumento.longValue());
      stmt.setTimestamp(2, this.convertDateToTimestamp(dataConsistenza));

      SolmrLogger.debug(this, "Executing getListDocumentoConduzioni: " + query
          + "\n");

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        DocumentoConduzioneVO documentoConduzioneVO = new DocumentoConduzioneVO();
        documentoConduzioneVO.setIdDocumentoConduzione(new Long(rs.getLong(1)));
        documentoConduzioneVO
            .setIdConduzioneParticella(new Long(rs.getLong(2)));
        documentoConduzioneVO.setIdDocumento(new Long(rs.getLong(3)));
        documentoConduzioneVO.setDataInserimento(new java.util.Date(rs.getDate(
            4).getTime()));
        elencoConduzioni.add(documentoConduzioneVO);
      }

      stmt.close();
      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "getListDocumentoConduzioni in StampeDAO - SQLException: "
              + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "getListDocumentoConduzioni in StampeDAO - Generic Exception: " + ex
              + "\n");
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
                "getListDocumentoConduzioni in StampeDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getListDocumentoConduzioni in StampeDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this,
        "Invocated getListDocumentoConduzioni method in StampeDAO\n");
    return elencoConduzioni;
  }

  // Metodo per recuperare i dati della particella a partire dall'
  // id_conduzione_particella
  // e relative ad una dichiarazione di consistenza
  public ParticellaVO getParticellaVOByIdConduzioneParticella(
      Long idConduzioneParticella, Long idDichiarazioneConsistenza)
      throws SolmrException, DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    ParticellaVO particellaVO = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT SP.ID_PARTICELLA, "
          + "        SP.ID_STORICO_PARTICELLA, "
          + "        SP.SEZIONE, "
          + "        SP.COMUNE, "
          + "        SP.FOGLIO, "
          + "        SP.DATA_INIZIO_VALIDITA, "
          + "        SP.DATA_FINE_VALIDITA, "
          + "        SP.PARTICELLA, "
          + "        SP.SUP_CATASTALE, "
          + "        SP.SUBALTERNO, "
          + "        SP.ID_UTENTE_AGGIORNAMENTO, "
          + "        C.DESCOM, "
          + "        CD.SUPERFICIE_CONDOTTA, "
          + "        C1.DESCOM, "
          + "        P.SIGLA_PROVINCIA, "
          + "        CD.ID_CONDUZIONE_PARTICELLA, "
          + "        TTP.DESCRIZIONE, "
          + "        CD.DATA_FINE_CONDUZIONE, "
          + "        CD.ID_UTE, "
          + "        CD.ID_TITOLO_POSSESSO, "
          + "        CD.NOTE, "
          + "        CD.DATA_INIZIO_CONDUZIONE "
          + " FROM   DB_STORICO_PARTICELLA SP, "
          + "        DB_PARTICELLA P, "
          + "        COMUNE C, "
          + "        DB_CONDUZIONE_DICHIARATA CD, "
          + "        DB_UTE U, "
          + "        COMUNE C1, "
          + "        PROVINCIA P, "
          + "        DB_TIPO_TITOLO_POSSESSO TTP, "
          + "        DB_DICHIARAZIONE_CONSISTENZA DC"
          + "        WHERE  CD.ID_CONDUZIONE_PARTICELLA = ? "
          + "        AND    CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA "
          + "        AND    P.ID_PARTICELLA = SP.ID_PARTICELLA "
          + "        AND    U.ID_UTE = CD.ID_UTE "
          + "        AND    U.COMUNE = C.ISTAT_COMUNE "
          + "        AND    SP.COMUNE = C1.ISTAT_COMUNE "
          + "        AND    C1.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
          + "        AND    CD.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO "
          + "        AND    CD.CODICE_FOTOGRAFIA_TERRENI = DC.CODICE_FOTOGRAFIA_TERRENI "
          + "        AND    DC.ID_DICHIARAZIONE_CONSISTENZA=? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "StampeDAO.getParticellaVOByIdConduzioneParticella - idConduzioneParticella: "
              + idConduzioneParticella);
      SolmrLogger
          .debug(
              this,
              "StampeDAO.getParticellaVOByIdConduzioneParticella - idDichiarazioneConsistenza: "
                  + idDichiarazioneConsistenza);

      stmt.setLong(1, idConduzioneParticella.longValue());
      stmt.setLong(2, idDichiarazioneConsistenza.longValue());

      SolmrLogger.debug(this,
          "Executing query getParticellaVOByIdConduzioneParticella: " + query);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        particellaVO = new ParticellaVO();
        particellaVO.setIdParticella(new Long(rs.getLong(1)));
        particellaVO.setIdStoricoParticella(new Long(rs.getLong(2)));
        particellaVO.setSezione(rs.getString(3));
        particellaVO.setIstatComuneParticella(rs.getString(4));
        particellaVO.setFoglio(new Long(rs.getLong(5)));
        particellaVO.setDataInizioValidita(rs.getDate(6));
        particellaVO.setDataFineValidita(rs.getDate(7));
        if (Validator.isNotEmpty(rs.getString(8)))
        {
          particellaVO.setParticella(new Long(rs.getLong(8)));
        }
        particellaVO.setSupCatastale(StringUtils.parseSuperficieField(rs
            .getString(9)));
        particellaVO.setSubalterno(rs.getString(10));
        particellaVO.setIdUtenteAggiornamento(new Long(rs.getLong(11)));
        particellaVO.setDescrizioneUnitaProduttiva(rs.getString(12));
        particellaVO.setSuperficieCondotta(StringUtils.parseSuperficieField(rs
            .getString(13)));
        particellaVO.setDescComuneParticella(rs.getString(14));
        particellaVO.setSiglaProvinciaParticella(rs.getString(15));
        particellaVO.setIdConduzioneParticella(new Long(rs.getLong(16)));
        particellaVO.setDescrizioneTitoloPossesso(rs.getString(17));
        if (Validator.isNotEmpty(rs.getString(18)))
        {
          particellaVO.setDataFineConduzione(rs.getDate(18));
        }
        particellaVO.setIdUnitaProduttiva(new Long(rs.getLong(19)));
        particellaVO.setIdTitoloPossesso(new Long(rs.getLong(20)));
        particellaVO.setNote(rs.getString(21));
        particellaVO.setDataInizioConduzione(rs.getDate(22));
      }

      stmt.close();
      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getParticellaVOByIdConduzioneParticella - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getParticellaVOByIdConduzioneParticella - Generic Exception: " + ex);
      throw new SolmrException(ex.getMessage());
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
                "getParticellaVOByIdConduzioneParticella - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getParticellaVOByIdConduzioneParticella - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return particellaVO;
  }

  public Vector<DocumentoVO> getDocumenti(String idDocumenti[]) throws DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating getDocumentiDichCons method in StampeDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<DocumentoVO> elencoDocumenti = new Vector<DocumentoVO>();
    StringBuffer stringBuf = new StringBuffer("");

    try
    {
      SolmrLogger
          .debug(this,
              "Creating db-connection in getDocumentiDichCons method in StampeDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in getDocumentiDichCons method in StampeDAO and it values: "
                  + conn + "\n");

      String query = " SELECT D.ID_DOCUMENTO,"
          + "        TTD.DESCRIZIONE AS TIPO_DOCUMENTO,"
          + "        TD.DESCRIZIONE AS DESC_DOCUMENTO,"
          + "        D.DATA_INIZIO_VALIDITA," 
          + "        D.DATA_FINE_VALIDITA,"
          + "        D.NUMERO_PROTOCOLLO," 
          + "        D.DATA_PROTOCOLLO, "
          + "        D.DATA_ULTIMO_AGGIORNAMENTO "
          + " FROM   DB_DOCUMENTO D, "
          + "        DB_TIPO_TIPOLOGIA_DOCUMENTO TTD, "
          + "        DB_TIPO_DOCUMENTO TD "
          + " WHERE  TTD.ID_TIPOLOGIA_DOCUMENTO = TD.ID_TIPOLOGIA_DOCUMENTO "
          + "    AND TD.ID_DOCUMENTO = D.EXT_ID_DOCUMENTO " + "    AND (";

      for (int i = 0; i < idDocumenti.length; i++)
      {
        SolmrLogger.debug(this,
            "Value of parameter [idDocumenti[i]] in getDocumenti method in StampeDAO: "
                + idDocumenti[i] + "\n");
        stringBuf.append("D.ID_DOCUMENTO = ").append(idDocumenti[i]);
        if (i < idDocumenti.length - 1)
          stringBuf.append(" OR ");
      }

      query += stringBuf.toString() + ")";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing getDocumenti: " + query + "\n");

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        DocumentoVO documentoEsitoVO = new DocumentoVO();
        TipoDocumentoVO tipoDocumentoVO = new TipoDocumentoVO();
        documentoEsitoVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
        tipoDocumentoVO.setDescrizione(rs.getString("DESC_DOCUMENTO"));
        CodeDescription tipoTipologiaDocumento = new CodeDescription(null, rs
            .getString("TIPO_DOCUMENTO"));
        tipoDocumentoVO.setTipoTipologiaDocumento(tipoTipologiaDocumento);
        documentoEsitoVO.setDataInizioValidita(new java.util.Date(rs.getDate(
            "DATA_INIZIO_VALIDITA").getTime()));
        try
        {
          documentoEsitoVO.setDataFineValidita(new java.util.Date(rs.getDate(
              "DATA_FINE_VALIDITA").getTime()));
        }
        catch (Exception e)
        {
        }
        documentoEsitoVO.setNumeroProtocollo(rs.getString("NUMERO_PROTOCOLLO"));
        try
        {
          documentoEsitoVO.setDataProtocollo(rs.getDate("DATA_PROTOCOLLO"));
        }
        catch (Exception e)
        {
        }
        
        documentoEsitoVO.setDataUltimoAggiornamento(rs.getTimestamp("DATA_ULTIMO_AGGIORNAMENTO"));
        
        documentoEsitoVO.setTipoDocumentoVO(tipoDocumentoVO);
        elencoDocumenti.add(documentoEsitoVO);
      }

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "getDocumenti in StampeDAO - SQLException: "
          + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this, "getDocumenti in StampeDAO - Generic Exception: "
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
            .fatal(
                this,
                "getDocumenti in StampeDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getDocumenti in StampeDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getDocumenti method in StampeDAO\n");
    return elencoDocumenti;
  }

  // ===================================================================
  // QuadroU
  //
  // Se idAzienda è valorizzato significa id_dichiarazione_consistenza=null
  // Se codFotografia valorizzato significa id_dichiarazione_consistenza not
  // null
  // ===================================================================
  public double getTotSupCond(Long idAzienda, Long codFotografia)
      throws DataAccessException
  {
    double result = 0;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
      String queryA = "SELECT SUM(COND.SUPERFICIE_CONDOTTA) AS TOT_SUP_COND "
          + "FROM DB_CONDUZIONE_PARTICELLA COND,DB_UTE UTE "
          + "WHERE UTE.ID_AZIENDA=? " + "AND UTE.ID_UTE=COND.ID_UTE "
          + "AND COND.DATA_FINE_CONDUZIONE IS NULL ";
      String queryB = "SELECT SUM(COND.SUPERFICIE_CONDOTTA) AS TOT_SUP_COND "
          + "FROM DB_CONDUZIONE_DICHIARATA COND "
          + "WHERE COND.CODICE_FOTOGRAFIA_TERRENI = ?";

      SolmrLogger.debug(this, "StampeDAO.getTotSupCond - idAzienda: "
          + idAzienda);
      SolmrLogger.debug(this, "StampeDAO.getTotSupCond - codFotografia: "
          + codFotografia);

      if (codFotografia == null)
      {
        SolmrLogger.debug(this, "StampeDAO.getTotSupCond - Executing query: "
            + queryA);
        stmt = conn.prepareStatement(queryA);
        stmt.setLong(1, idAzienda.longValue());
      }
      else
      {
        SolmrLogger.debug(this, "StampeDAO.getTotSupCond - Executing query: "
            + queryB);
        stmt = conn.prepareStatement(queryB);
        stmt.setLong(1, codFotografia.longValue());
      }

      ResultSet rs = stmt.executeQuery();
      if (rs.next())
        result = rs.getDouble("TOT_SUP_COND");
      SolmrLogger.debug(this, "StampeDAO.getTotSupCond - result: " + result);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "StampeDAO.getTotSupCond - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "StampeDAO.getTotSupCond - Generic Exception: "
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
                "StampeDAO.getTotSupCond - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "StampeDAO.getTotSupCond - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // ===================================================================
  // QuadroU
  //
  // Se idAzienda è valorizzato significa id_dichiarazione_consistenza=null
  // Se codFotografia valorizzato significa id_dichiarazione_consistenza not
  // null
  // ===================================================================
  public double getSIC(Long idAzienda, Long codFotografia)
      throws DataAccessException
  {
    double result = 0;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
      String queryA = "SELECT NVL(SUM(COND.SUPERFICIE_CONDOTTA),0) AS SUP_COND "
          + "FROM DB_CONDUZIONE_PARTICELLA COND,DB_STORICO_PARTICELLA PART, DB_UTE UTE "
          + "WHERE COND.ID_PARTICELLA=PART.ID_PARTICELLA "
          + "AND COND.DATA_FINE_CONDUZIONE IS NULL "
          + "AND PART.DATA_FINE_VALIDITA IS NULL "
          + "AND UTE.ID_AZIENDA=? "
          + "AND UTE.ID_UTE=COND.ID_UTE "
          + "AND (PART.ID_AREA_C = 3 OR PART.ID_AREA_C = 4) ";
      String queryB = "SELECT NVL(SUM(COND.SUPERFICIE_CONDOTTA),0) AS SUP_COND "
          + "FROM DB_CONDUZIONE_DICHIARATA COND,DB_STORICO_PARTICELLA PART "
          + "WHERE COND.ID_STORICO_PARTICELLA =PART.ID_STORICO_PARTICELLA "
          + "AND COND.CODICE_FOTOGRAFIA_TERRENI = ? "
          + "AND (PART.ID_AREA_C = 3 OR PART.ID_AREA_C = 4) ";

      SolmrLogger.debug(this, "StampeDAO.getSIC - idAzienda: " + idAzienda);
      SolmrLogger.debug(this, "StampeDAO.getSIC - codFotografia: "
          + codFotografia);

      if (codFotografia == null)
      {
        SolmrLogger
            .debug(this, "StampeDAO.getSIC - Executing query: " + queryA);
        stmt = conn.prepareStatement(queryA);
        stmt.setLong(1, idAzienda.longValue());
      }
      else
      {
        SolmrLogger
            .debug(this, "StampeDAO.getSIC - Executing query: " + queryB);
        stmt = conn.prepareStatement(queryB);
        stmt.setLong(1, codFotografia.longValue());
      }

      ResultSet rs = stmt.executeQuery();
      if (rs.next())
        result = rs.getDouble("SUP_COND");
      SolmrLogger.debug(this, "StampeDAO.getSIC - result: " + result);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "StampeDAO.getSIC - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "StampeDAO.getSIC - Generic Exception: "
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
        SolmrLogger.fatal(this,
            "StampeDAO.getSIC - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "StampeDAO.getSIC - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // ===================================================================
  // QuadroU
  //
  // Se idAzienda è valorizzato significa id_dichiarazione_consistenza=null
  // Se codFotografia valorizzato significa id_dichiarazione_consistenza not
  // null
  // ===================================================================
  public double getZPS(Long idAzienda, Long codFotografia)
      throws DataAccessException
  {
    double result = 0;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
      String queryA = "SELECT NVL(SUM(COND.SUPERFICIE_CONDOTTA),0) AS SUP_COND "
          + "FROM DB_CONDUZIONE_PARTICELLA COND,DB_STORICO_PARTICELLA PART, DB_UTE UTE "
          + "WHERE COND.ID_PARTICELLA=PART.ID_PARTICELLA "
          + "AND COND.DATA_FINE_CONDUZIONE IS NULL "
          + "AND PART.DATA_FINE_VALIDITA IS NULL "
          + "AND UTE.ID_AZIENDA=? "
          + "AND UTE.ID_UTE=COND.ID_UTE "
          + "AND (PART.ID_AREA_C = 2 OR PART.ID_AREA_C = 4)";
      String queryB = "SELECT NVL(SUM(COND.SUPERFICIE_CONDOTTA),0) AS SUP_COND "
          + "FROM DB_CONDUZIONE_DICHIARATA COND,DB_STORICO_PARTICELLA PART "
          + "WHERE COND.ID_STORICO_PARTICELLA =PART.ID_STORICO_PARTICELLA "
          + "AND COND.CODICE_FOTOGRAFIA_TERRENI = ? "
          + "AND (PART.ID_AREA_C = 2 OR PART.ID_AREA_C = 4) ";

      SolmrLogger.debug(this, "StampeDAO.getZPS - idAzienda: " + idAzienda);
      SolmrLogger.debug(this, "StampeDAO.getZPS - codFotografia: "
          + codFotografia);

      if (codFotografia == null)
      {
        SolmrLogger
            .debug(this, "StampeDAO.getZPS - Executing query: " + queryA);
        stmt = conn.prepareStatement(queryA);
        stmt.setLong(1, idAzienda.longValue());
      }
      else
      {
        SolmrLogger
            .debug(this, "StampeDAO.getZPS - Executing query: " + queryB);
        stmt = conn.prepareStatement(queryB);
        stmt.setLong(1, codFotografia.longValue());
      }

      ResultSet rs = stmt.executeQuery();
      if (rs.next())
        result = rs.getDouble("SUP_COND");
      SolmrLogger.debug(this, "StampeDAO.getZPS - result: " + result);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "StampeDAO.getZPS - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "StampeDAO.getZPS - Generic Exception: "
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
        SolmrLogger.fatal(this,
            "StampeDAO.getZPS - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "StampeDAO.getZPS - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // ===================================================================
  // QuadroU
  //
  // Se idAzienda è valorizzato significa id_dichiarazione_consistenza=null
  // Se codFotografia valorizzato significa id_dichiarazione_consistenza not
  // null
  // ===================================================================
  public double getZVN(Long idAzienda, Long codFotografia)
      throws DataAccessException
  {
    double result = 0;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
      String queryA = "SELECT NVL(SUM(COND.SUPERFICIE_CONDOTTA),0) AS SUP_COND "
          + "FROM DB_CONDUZIONE_PARTICELLA COND,DB_STORICO_PARTICELLA PART, DB_UTE UTE, DB_FOGLIO F "
          + "WHERE COND.ID_PARTICELLA=PART.ID_PARTICELLA "
          + "AND COND.DATA_FINE_CONDUZIONE IS NULL "
          + "AND PART.DATA_FINE_VALIDITA IS NULL "
          + "AND UTE.ID_AZIENDA=? "
          + "AND UTE.ID_UTE=COND.ID_UTE "
          + "AND PART.FOGLIO=F.FOGLIO "
          + "AND PART.COMUNE = F.COMUNE "
          + "AND NVL(PART.SEZIONE,0) = NVL(F.SEZIONE,0) "
          + "AND F.ID_AREA_E = 2";
      String queryB = "SELECT NVL(SUM(COND.SUPERFICIE_CONDOTTA),0) AS SUP_COND "
          + "FROM DB_CONDUZIONE_DICHIARATA  COND,DB_STORICO_PARTICELLA PART, DB_FOGLIO F "
          + "WHERE COND.ID_STORICO_PARTICELLA =PART.ID_STORICO_PARTICELLA "
          + "AND COND.CODICE_FOTOGRAFIA_TERRENI = ? "
          + "AND PART.FOGLIO=F.FOGLIO "
          + "AND PART.COMUNE = F.COMUNE "
          + "AND NVL(PART.SEZIONE,0) = NVL(F.SEZIONE,0) "
          + "AND F.ID_AREA_E = 2 ";

      SolmrLogger.debug(this, "StampeDAO.getZVN - idAzienda: " + idAzienda);
      SolmrLogger.debug(this, "StampeDAO.getZVN - codFotografia: "
          + codFotografia);

      if (codFotografia == null)
      {
        SolmrLogger
            .debug(this, "StampeDAO.getZVN - Executing query: " + queryA);
        stmt = conn.prepareStatement(queryA);
        stmt.setLong(1, idAzienda.longValue());
      }
      else
      {
        SolmrLogger
            .debug(this, "StampeDAO.getZVN - Executing query: " + queryB);
        stmt = conn.prepareStatement(queryB);
        stmt.setLong(1, codFotografia.longValue());
      }

      ResultSet rs = stmt.executeQuery();
      if (rs.next())
        result = rs.getDouble("SUP_COND");
      SolmrLogger.debug(this, "StampeDAO.getZVN - result: " + result);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "StampeDAO.getZVN - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "StampeDAO.getZVN - Generic Exception: "
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
        SolmrLogger.fatal(this,
            "StampeDAO.getZVN - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "StampeDAO.getZVN - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  
//===================================================================
  // QuadroU
  //
  // Se idAzienda è valorizzato significa id_dichiarazione_consistenza=null
  // Se codFotografia valorizzato significa id_dichiarazione_consistenza not
  // null
  // ===================================================================
  public double getFasceFluviali(Long idAzienda, Long codFotografia)
      throws DataAccessException
  {
    double result = 0;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
      String queryA = "SELECT NVL(SUM(COND.SUPERFICIE_CONDOTTA),0) AS SUP_COND "
        + "FROM DB_CONDUZIONE_PARTICELLA COND,DB_STORICO_PARTICELLA PART, DB_UTE UTE "
        + "WHERE COND.ID_PARTICELLA=PART.ID_PARTICELLA "
        + "AND COND.DATA_FINE_CONDUZIONE IS NULL "
        + "AND PART.DATA_FINE_VALIDITA IS NULL "
        + "AND UTE.ID_AZIENDA=? "
        + "AND UTE.ID_UTE=COND.ID_UTE "
        + "AND (PART.ID_FASCIA_FLUVIALE = 1 OR PART.ID_FASCIA_FLUVIALE = 2)";
      String queryB = "SELECT NVL(SUM(COND.SUPERFICIE_CONDOTTA),0) AS SUP_COND "
        + "FROM DB_CONDUZIONE_DICHIARATA COND,DB_STORICO_PARTICELLA PART "
        + "WHERE COND.ID_STORICO_PARTICELLA =PART.ID_STORICO_PARTICELLA "
        + "AND COND.CODICE_FOTOGRAFIA_TERRENI = ? "
        + "AND (PART.ID_FASCIA_FLUVIALE = 1 OR PART.ID_FASCIA_FLUVIALE = 2)";

      SolmrLogger.debug(this, "StampeDAO.getFasceFluviali - idAzienda: " + idAzienda);
      SolmrLogger.debug(this, "StampeDAO.getFasceFluviali - codFotografia: "
          + codFotografia);

      if (codFotografia == null)
      {
        SolmrLogger
            .debug(this, "StampeDAO.getFasceFluviali - Executing query: " + queryA);
        stmt = conn.prepareStatement(queryA);
        stmt.setLong(1, idAzienda.longValue());
      }
      else
      {
        SolmrLogger
            .debug(this, "StampeDAO.getFasceFluviali - Executing query: " + queryB);
        stmt = conn.prepareStatement(queryB);
        stmt.setLong(1, codFotografia.longValue());
      }

      ResultSet rs = stmt.executeQuery();
      if (rs.next())
        result = rs.getDouble("SUP_COND");
      SolmrLogger.debug(this, "StampeDAO.getFasceFluviali - result: " + result);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "StampeDAO.getFasceFluviali - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "StampeDAO.getFasceFluviali - Generic Exception: "
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
        SolmrLogger.fatal(this,
            "StampeDAO.getFasceFluviali - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "StampeDAO.getFasceFluviali - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  
  public Vector<BaseCodeDescription> getTerreniQuadroI4(Long idAzienda, Long codFotografia) throws DataAccessException
  {
    Vector<BaseCodeDescription> result = new Vector<BaseCodeDescription>();
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      //query per la situazione attuale
      String querySitAtt = "SELECT TTP.DESCRIZIONE AS DESC_CONDUZIONE, " +
      		                 "       COUNT(*) AS NUM_PART, "+
                           "       SUM (SP.SUP_CATASTALE) AS TOT_SUP_CATASTALE, "+
                           "       SUM (CP.SUPERFICIE_CONDOTTA) AS TOT_SUPERFICIE_CONDOTTA, "+
                           "       SUM(COP.SUPERFICIE_UTILIZZATA) TOT_SUPERFICIE_UTILIZZATA "+
                           "FROM   DB_UTE U, " +
                           "       DB_STORICO_PARTICELLA SP, " +
                           "       DB_TIPO_TITOLO_POSSESSO TTP, " +
                           "       DB_CONDUZIONE_PARTICELLA CP, "+
                           "       (SELECT CP.ID_CONDUZIONE_PARTICELLA, " +
                           "               SUM (UP.SUPERFICIE_UTILIZZATA) AS SUPERFICIE_UTILIZZATA "+
                           "        FROM   DB_UTE U, " +
                           "               DB_CONDUZIONE_PARTICELLA CP, " +
                           "               DB_STORICO_PARTICELLA SP, "+
                           "               DB_UTILIZZO_PARTICELLA UP "+
                           "        WHERE  U.ID_AZIENDA = ? "+
                           "        AND    U.DATA_FINE_ATTIVITA IS NULL "+ 
                           "        AND    CP.ID_UTE = U.ID_UTE  "+
                           "        AND    CP.DATA_FINE_CONDUZIONE IS NULL  "+
                           "        AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA  "+
                           "        AND    SP.DATA_FINE_VALIDITA IS NULL "+
                           "        AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA "+
                           "        GROUP BY CP.ID_CONDUZIONE_PARTICELLA) COP "+
                           "WHERE  U.ID_AZIENDA = ? "+
                           "AND    U.DATA_FINE_ATTIVITA IS NULL "+ 
                           "AND    CP.ID_UTE = U.ID_UTE  "+
                           "AND    CP.DATA_FINE_CONDUZIONE IS NULL "+ 
                           "AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA "+
                           "AND    SP.DATA_FINE_VALIDITA IS NULL "+
                           "AND    CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO "+
                           "AND    CP.ID_CONDUZIONE_PARTICELLA = COP.ID_CONDUZIONE_PARTICELLA(+) "+
                           "GROUP BY TTP.DESCRIZIONE ";
      
      //query per la dichiarazione di consistenza
      String queryDic = "SELECT TTP.DESCRIZIONE AS DESC_CONDUZIONE, COUNT(*) AS NUM_PART, " + 
                        "SUM (SP.SUP_CATASTALE) AS TOT_SUP_CATASTALE, " +
                        "SUM (CD.SUPERFICIE_CONDOTTA) AS TOT_SUPERFICIE_CONDOTTA, " +
                        "SUM(COD.SUPERFICIE_UTILIZZATA) AS TOT_SUPERFICIE_UTILIZZATA " +
                        "FROM DB_CONDUZIONE_DICHIARATA CD, " +
                        "     DB_STORICO_PARTICELLA SP, " +
                        "     DB_TIPO_TITOLO_POSSESSO TTP, " +
                        "(SELECT CD.ID_CONDUZIONE_DICHIARATA,  " +
                        "SUM (UD.SUPERFICIE_UTILIZZATA) AS SUPERFICIE_UTILIZZATA " +
                        "FROM DB_CONDUZIONE_DICHIARATA CD, " +
                        "     DB_STORICO_PARTICELLA SP, " +
                        "     DB_UTILIZZO_DICHIARATO UD " +
                        "WHERE CD.CODICE_FOTOGRAFIA_TERRENI = ? " +
                        "AND   CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " + 
                        "AND   CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA " + 
                        "GROUP BY CD.ID_CONDUZIONE_DICHIARATA ) COD " +
                        "WHERE CD.CODICE_FOTOGRAFIA_TERRENI = ? " + 
                        "AND CD.ID_STORICO_PARTICELLA=SP.ID_STORICO_PARTICELLA " + 
                        "AND CD.ID_TITOLO_POSSESSO=TTP.ID_TITOLO_POSSESSO " +
                        "AND CD.ID_CONDUZIONE_DICHIARATA = COD.ID_CONDUZIONE_DICHIARATA " +
                        "GROUP BY TTP.DESCRIZIONE";
      

      String query=null;
      
      if (codFotografia==null) query=querySitAtt; //sono alla situazione attuale
      else query=queryDic; //sono alla dichiarazione di consistenza
      
      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query: " + query);
      SolmrLogger.debug(this, "Param idAzienda: " + idAzienda);
      SolmrLogger.debug(this, "Param codFotografia: " + codFotografia);

      if (codFotografia==null) //sono alla situazione attuale
      {
        stmt.setLong(1, idAzienda.longValue());
        stmt.setLong(2, idAzienda.longValue());
      }
      else
      {      //sono alla dichiarazione di consistenza
        stmt.setLong(1, codFotografia.longValue());
        stmt.setLong(2, codFotografia.longValue());
      }

      ResultSet rs = stmt.executeQuery();
      
      
      while (rs.next())
      {
        BaseCodeDescription code=new BaseCodeDescription();
        code.setDescription(rs.getString("DESC_CONDUZIONE"));
        code.setCode(rs.getLong("NUM_PART"));
        BigDecimal num[]=new BigDecimal[3];
        num[0]=rs.getBigDecimal("TOT_SUP_CATASTALE");
        num[1]=rs.getBigDecimal("TOT_SUPERFICIE_CONDOTTA");
        num[2]=rs.getBigDecimal("TOT_SUPERFICIE_UTILIZZATA");
        code.setItem(num);
        result.add(code);
      }
      rs.close();

      SolmrLogger.debug(this, "getTerreniQuadroI4 - Found " + result.size()
          + " record(s)");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTerreniQuadroI4 - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getTerreniQuadroI4 - Generic Exception: "
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
        SolmrLogger.fatal(this,
            "getTerreniQuadroI4 - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getTerreniQuadroI4 - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  
  public Vector<BaseCodeDescription> getTerreniQuadroI5(Long idAzienda,java.util.Date dataRiferimento, Long codFotografia) throws DataAccessException
  {
    Vector<BaseCodeDescription> result = new Vector<BaseCodeDescription>();
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      //query per la situazione attuale
      String querySitAtt = 
          "SELECT '[' || MU.CODICE || '] ' || MU.DESCRIZIONE AS DESC_MACRO_USO, " +
          "       SUM(UP.SUPERFICIE_UTILIZZATA) AS SUP, " +
          "       MU.ID_MACRO_USO "+
          "FROM   DB_UTE U, " +
          "       DB_CONDUZIONE_PARTICELLA CP, " +
          "       DB_UTILIZZO_PARTICELLA UP, " +
          "       DB_TIPO_MACRO_USO_VARIETA MV, " +
          "       DB_TIPO_MACRO_USO MU "+
          "WHERE  U.ID_AZIENDA = ? "+
          "AND    U.DATA_FINE_ATTIVITA IS NULL "+
          "AND    CP.ID_UTE = U.ID_UTE "+
          "AND    CP.DATA_FINE_CONDUZIONE IS NULL "+
          "AND    UP.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA "+
          "AND    UP.ID_CATALOGO_MATRICE = MV.ID_CATALOGO_MATRICE "+
          "AND    MV.DATA_FINE_VALIDITA IS NULL "+
          "AND    MU.ID_MACRO_USO = MV.ID_MACRO_USO "+
          "AND    MU.DATA_FINE_VALIDITA IS NULL "+
          "GROUP BY MU.CODICE, " +
          "         MU.DESCRIZIONE, " +
          "         MU.ID_MACRO_USO "+
          "ORDER BY MU.DESCRIZIONE";
      
      //query per la dichiarazione di consistenza
      String queryDic = 
        "SELECT '[' || MU.CODICE || '] ' || MU.DESCRIZIONE AS DESC_MACRO_USO, " +
        "       SUM(UD.SUPERFICIE_UTILIZZATA) AS SUP, " +
        "       MU.ID_MACRO_USO "+
        "FROM   DB_CONDUZIONE_DICHIARATA CD, " +
        "       DB_UTILIZZO_DICHIARATO UD, " +
        "       DB_TIPO_MACRO_USO_VARIETA MV, " +
        "       DB_TIPO_MACRO_USO MU "+
        "WHERE  CD.CODICE_FOTOGRAFIA_TERRENI = ? "+
        "AND    UD.ID_CONDUZIONE_DICHIARATA = CD.ID_CONDUZIONE_DICHIARATA "+
        "AND    UD.ID_CATALOGO_MATRICE = MV.ID_CATALOGO_MATRICE "+
        "AND    MV.DATA_INIZIO_VALIDITA <= ? "+
        "AND    NVL (MV.DATA_FINE_VALIDITA,SYSDATE) > ? "+
        "AND    MU.ID_MACRO_USO=MV.ID_MACRO_USO "+
        "AND    MU.DATA_INIZIO_VALIDITA <= ? " +
        "AND    NVL (MU.DATA_FINE_VALIDITA,SYSDATE) > ? "+
        "GROUP BY MU.CODICE, " +
        "         MU.DESCRIZIONE, " +
        "         MU.ID_MACRO_USO "+
        "ORDER BY MU.DESCRIZIONE";
      
      String queryCountSitAtt = 
        "SELECT COUNT(DISTINCT (CP.ID_PARTICELLA)) AS NUM_PART "+
        "FROM   DB_UTE U, " +
        "       DB_CONDUZIONE_PARTICELLA CP, " +
        "       DB_UTILIZZO_PARTICELLA UP, " +
        "       DB_TIPO_MACRO_USO_VARIETA MV "+
        "WHERE  U.ID_AZIENDA= ? " +
        "AND    U.DATA_FINE_ATTIVITA IS NULL " +
        "AND    CP.ID_UTE = U.ID_UTE " +
        "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "AND    UP.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA " +
        "AND    UP.ID_CATALOGO_MATRICE = MV.ID_CATALOGO_MATRICE " +
        "AND    MV.DATA_FINE_VALIDITA IS NULL " +
        "AND    MV.ID_MACRO_USO = ? ";
      
      String queryCountDic = 
        "SELECT COUNT(DISTINCT (CD.ID_PARTICELLA)) AS NUM_PART " +
        "FROM   DB_CONDUZIONE_DICHIARATA CD, " +
        "       DB_UTILIZZO_DICHIARATO UD, " +
        "       DB_TIPO_MACRO_USO_VARIETA MV " +
        "WHERE  CD.CODICE_FOTOGRAFIA_TERRENI = ? " +
        "AND    UD.ID_CONDUZIONE_DICHIARATA = CD.ID_CONDUZIONE_DICHIARATA " +
        "AND    UD.ID_CATALOGO_MATRICE = MV.ID_CATALOGO_MATRICE " +
        "AND    MV.DATA_INIZIO_VALIDITA <= ? " +
        "AND    NVL (MV.DATA_FINE_VALIDITA,SYSDATE) > ? " +
        "AND    MV.ID_MACRO_USO = ? ";
                                
      

      String query=null,queryCount=null;
      
      if (codFotografia==null) 
      {
        //sono alla situazione attuale
        query=querySitAtt; 
        queryCount=queryCountSitAtt;
      }
      else 
      {
        //sono alla dichiarazione di consistenza
        query=queryDic; 
        queryCount=queryCountDic;
      }
      
      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query: " + query);
      SolmrLogger.debug(this, "Param idAzienda: " + idAzienda);
      SolmrLogger.debug(this, "Param dataRiferimento: " + dataRiferimento);
      SolmrLogger.debug(this, "Param codFotografia: " + codFotografia);

      if (codFotografia==null) //sono alla situazione attuale
        stmt.setLong(1, idAzienda.longValue());
      else
      {      //sono alla dichiarazione di consistenza
        stmt.setLong(1, codFotografia.longValue());
        stmt.setTimestamp(2, this.convertDateToTimestamp(dataRiferimento));
        stmt.setTimestamp(3, this.convertDateToTimestamp(dataRiferimento));
        stmt.setTimestamp(4, this.convertDateToTimestamp(dataRiferimento));
        stmt.setTimestamp(5, this.convertDateToTimestamp(dataRiferimento));
      }

      ResultSet rs = stmt.executeQuery();
      
      
      while (rs.next())
      {
        BaseCodeDescription code=new BaseCodeDescription();
        code.setDescription(rs.getString("DESC_MACRO_USO"));
        code.setCode(getNumParticelle( idAzienda,dataRiferimento, codFotografia,rs.getLong("ID_MACRO_USO"),queryCount));
        code.setItem(rs.getBigDecimal("SUP"));
        result.add(code);
      }
      rs.close();

      SolmrLogger.debug(this, "getTerreniQuadroI5 - Found " + result.size()
          + " record(s)");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTerreniQuadroI5 - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getTerreniQuadroI5 - Generic Exception: "
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
        SolmrLogger.fatal(this,
            "getTerreniQuadroI5 - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getTerreniQuadroI5 - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  public long getNumParticelle(Long idAzienda,java.util.Date dataRiferimento, Long codFotografia,long idMacroUso,String query)
    throws DataAccessException
  {
    long result = 0;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
     
      SolmrLogger.debug(this, "StampeDAO.getNumParticelle - idAzienda: " + idAzienda);
      SolmrLogger.debug(this, "StampeDAO.getNumParticelle - dataRiferimento: " + dataRiferimento);
      SolmrLogger.debug(this, "StampeDAO.getNumParticelle - codFotografia: "+ codFotografia);
      SolmrLogger.debug(this, "StampeDAO.getNumParticelle - idMacroUso: " + idMacroUso);
      SolmrLogger.debug(this, "StampeDAO.getNumParticelle - query: " + query);
    
      stmt = conn.prepareStatement(query);
      
      if (codFotografia==null) 
      {  //sono alla situazione attuale
        stmt.setLong(1, idAzienda.longValue());
        stmt.setLong(2, idMacroUso);
      }
      else
      {      //sono alla dichiarazione di consistenza
        stmt.setLong(1, codFotografia.longValue());
        stmt.setTimestamp(2, this.convertDateToTimestamp(dataRiferimento));
        stmt.setTimestamp(3, this.convertDateToTimestamp(dataRiferimento));
        stmt.setLong(4, idMacroUso);
      }
      
    
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
        result = rs.getLong("NUM_PART");
      SolmrLogger.debug(this, "StampeDAO.getNumParticelle - result: " + result);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "StampeDAO.getNumParticelle - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "StampeDAO.getNumParticelle - Generic Exception: "
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
        SolmrLogger.fatal(this,
            "StampeDAO.getNumParticelle - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "StampeDAO.getNumParticelle - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  
  
  /***************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * 
   * 
   * inizio metodi usati dalla stampa della comunicazione 10R
   * 
   * 
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   */
  
  //===================================================================
  // QUADRO C - Consistenza zootecnica
  //
  // prendere i dati da DB_ALLEVAMENTO
  // ===================================================================
  public Vector<ConsistenzaZootecnicaStampa> getAllevamentiQuadroC10R(Long idAzienda,
      java.util.Date dataRiferimento) throws DataAccessException
  {
    Vector<ConsistenzaZootecnicaStampa> result = new Vector<ConsistenzaZootecnicaStampa>();
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT C.DESCOM ,P.SIGLA_PROVINCIA,A.CODICE_AZIENDA_ZOOTECNICA,S.DESCRIZIONE,SUM(CA.QUANTITA) AS QUANTITA "
          + "FROM DB_ALLEVAMENTO A,DB_UTE U,DB_CATEGORIE_ALLEVAMENTO CA,DB_TIPO_SPECIE_ANIMALE S,COMUNE C, PROVINCIA P "
          + "WHERE A.ID_UTE=U.ID_UTE AND U.ID_AZIENDA = ? "
          + "AND A.DATA_INIZIO <= ? "
          + "AND NVL(A.DATA_FINE, TO_DATE('31/12/9999','dd/mm/yyyy')) > ? "
          + "AND CA.ID_ALLEVAMENTO=A.ID_ALLEVAMENTO "
          + "AND A.ID_SPECIE_ANIMALE=S.ID_SPECIE_ANIMALE "
          + "AND U.COMUNE=C.ISTAT_COMUNE "
          + "AND C.ISTAT_PROVINCIA=P.ISTAT_PROVINCIA "
          + "GROUP BY C.DESCOM ,P.SIGLA_PROVINCIA,A.CODICE_AZIENDA_ZOOTECNICA,S.DESCRIZIONE";
      

      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query: " + query);
      SolmrLogger.debug(this, "Param idAzienda: " + idAzienda);
      SolmrLogger.debug(this, "Param dataRiferimento: " + dataRiferimento);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setTimestamp(2, convertDateToTimestamp(dataRiferimento));
      stmt.setTimestamp(3, convertDateToTimestamp(dataRiferimento));

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        ConsistenzaZootecnicaStampa cons=new ConsistenzaZootecnicaStampa();
        cons.setComuneProvUTE(rs.getString("DESCOM")+" ("+rs.getString("SIGLA_PROVINCIA")+")");
        cons.setCodiceAziendaZootecnica(rs.getString("CODICE_AZIENDA_ZOOTECNICA"));
        cons.setDescrizioneSpecie(rs.getString("DESCRIZIONE"));
        cons.setTotaleCapi(rs.getLong("QUANTITA"));
        result.add(cons);
      }
      rs.close();

      SolmrLogger.debug(this, "getAllevamentiQuadroC10R - Found " + result.size()
          + " record(s)");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getAllevamentiQuadroC10R - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getAllevamentiQuadroC10R - Generic Exception: "
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
        SolmrLogger.fatal(this,
            "getAllevamentiQuadroC10R - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getAllevamentiQuadroC10R - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  
  public Vector<QuadroDTerreni> getTerreniQuadroD10R(Long idAzienda) throws DataAccessException
  {
    Vector<QuadroDTerreni> result = new Vector<QuadroDTerreni>();
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = 
        "SELECT SP.COMUNE || SP.FOGLIO AS CODICE, " +
        "       C.DESCOM, " +
        "       P.SIGLA_PROVINCIA, " +
        "       SP.FOGLIO, " +
        "       F.ID_AREA_E, "+
        "       SP.ID_FASCIA_FLUVIALE, " +
        "       CP.ID_TITOLO_POSSESSO, " +
        "       CP.SUPERFICIE_CONDOTTA, " +
        "       CP.SUPERFICIE_AGRONOMICA, " +
        "       U.INDIRIZZO, "+
        "       CUTE.DESCOM AS UT_DESCOM, " +
        "       PUTE.SIGLA_PROVINCIA AS UT_SIGLA_PROVINCIA " +
        "FROM   DB_CONDUZIONE_PARTICELLA CP, " +
        "       DB_UTE U, " +
        "       DB_STORICO_PARTICELLA SP, " +
        "       DB_FOGLIO F, " +
        "       COMUNE C, " +
        "       PROVINCIA P, "+
        "       COMUNE CUTE, " +
        "       PROVINCIA PUTE "+
        "WHERE  U.ID_UTE = CP.ID_UTE "+
        "AND    SP.ID_PARTICELLA = CP.ID_PARTICELLA "+
        "AND    CP.DATA_FINE_CONDUZIONE IS NULL "+
        "AND    U.DATA_FINE_ATTIVITA IS NULL "+
        "AND    SP.DATA_FINE_VALIDITA IS NULL "+
        "AND    U.ID_AZIENDA = ? "+
        "AND    SP.COMUNE = F.COMUNE(+) "+
        "AND    NVL(SP.SEZIONE,-1)=NVL(F.SEZIONE(+),-1) "+
        "AND    SP.FOGLIO = F.FOGLIO(+) "+
        "AND    SP.COMUNE = C.ISTAT_COMUNE "+
        "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "+
        "AND    U.COMUNE = CUTE.ISTAT_COMUNE "+
        "AND    CUTE.ISTAT_PROVINCIA = PUTE.ISTAT_PROVINCIA "+
        "ORDER BY " +
        "         CUTE.DESCOM, " +
        "         PUTE.SIGLA_PROVINCIA, " +
        "         C.DESCOM, " +
        "         P.SIGLA_PROVINCIA, " +
        "         SP.FOGLIO ";
      

      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query: " + query);
      SolmrLogger.debug(this, "Param idAzienda: " + idAzienda);

      stmt.setLong(1, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();
      
      String codiceOld="-1";
      QuadroDTerreni terreni=null;
      
      boolean trovato=false;
      
      while (rs.next())
      {
        trovato=true;
        String codice=rs.getString("CODICE");
        if(!codice.equals(codiceOld)) 
        {
          codiceOld=new String(codice.toString());
          if (terreni!=null) result.add(terreni);
          terreni=new QuadroDTerreni();
          //Reiposto le superfici a 0
          terreni.setSupAssVul(new BigDecimal(0));
          terreni.setSupAssNonVul(new BigDecimal(0));
          terreni.setSupCondVul(new BigDecimal(0));
          terreni.setSupCondNonVul(new BigDecimal(0));
        }
        terreni.setComune(rs.getString("DESCOM"));
        terreni.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
        terreni.setFoglio(rs.getString("FOGLIO"));
        //Vado a leggere i dati epr potermi ricavare le superfici
        String idAreaE=rs.getString("ID_AREA_E");
        String idFasciaFluviale=rs.getString("ID_FASCIA_FLUVIALE");
        String idTitoloPossesso=rs.getString("ID_TITOLO_POSSESSO");
        BigDecimal supCondotta=rs.getBigDecimal("SUPERFICIE_CONDOTTA");
        BigDecimal supAgronomica=rs.getBigDecimal("SUPERFICIE_AGRONOMICA");
        if ("5".equals(idTitoloPossesso)) //Asservimento
        {
          if (idFasciaFluviale!=null || "2".equals(idAreaE))
          {
            //Zona vulnerabile 
            if (supCondotta!=null)
              terreni.setSupAssVul(terreni.getSupAssVul().add(supCondotta));
          }
          if (idFasciaFluviale==null && ("1".equals(idAreaE) || idAreaE==null ))
          {
            //Zona non vulnerabile 
            if (supCondotta!=null)
              terreni.setSupAssNonVul(terreni.getSupAssNonVul().add(supCondotta));
          }
        }
        else //Conduzione
        {
          if (idFasciaFluviale!=null || "2".equals(idAreaE))
          {
            //Zona vulnerabile 
            if (supAgronomica!=null)
              terreni.setSupCondVul(terreni.getSupCondVul().add(supAgronomica));
          }
          if (idFasciaFluviale==null && ("1".equals(idAreaE) || idAreaE==null ))
          {
            //Zona non vulnerabile 
            if (supAgronomica!=null)
              terreni.setSupCondNonVul(terreni.getSupCondNonVul().add(supAgronomica));
          } 
        }
        
        terreni.setIndirizzoUte(rs.getString("INDIRIZZO"));
        terreni.setComuneUte(rs.getString("UT_DESCOM"));
        terreni.setSiglaProvUte(rs.getString("UT_SIGLA_PROVINCIA"));
        
      }
      if (trovato) result.add(terreni);
      rs.close();

      SolmrLogger.debug(this, "getTerreniQuadroD10R - Found " + result.size()
          + " record(s)");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTerreniQuadroD10R - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getTerreniQuadroD10R - Generic Exception: "
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
        SolmrLogger.fatal(this,
            "getTerreniQuadroD10R - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getTerreniQuadroD10R - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  
  public Vector<QuadroDTerreni> getTerreniQuadroD10R(java.util.Date dataRiferimento, Long codFotografia) throws DataAccessException
  {
    Vector<QuadroDTerreni> result = new Vector<QuadroDTerreni>();
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = 
        "SELECT SP.COMUNE || SP.FOGLIO AS CODICE, " +
        "       C.DESCOM, " +
        "       P.SIGLA_PROVINCIA, " +
        "       SP.FOGLIO, " +
        "       F.ID_AREA_E, "+
        "       SP.ID_FASCIA_FLUVIALE, " +
        "       CD.ID_TITOLO_POSSESSO, " +
        "       CD.SUPERFICIE_CONDOTTA, " +
        "       CD.SUPERFICIE_AGRONOMICA, "+
        "       UT.INDIRIZZO, "+
        "       CUTE.DESCOM AS UT_DESCOM, " +
        "       PUTE.SIGLA_PROVINCIA AS UT_SIGLA_PROVINCIA " +
        "FROM   DB_CONDUZIONE_DICHIARATA CD, " +
        "       DB_STORICO_PARTICELLA SP, " +
        "       DB_FOGLIO F, " +
        "       COMUNE C, " +
        "       PROVINCIA P, " +
        "       DB_UTE UT, " +
        "       COMUNE CUTE, " +
        "       PROVINCIA PUTE " + 
        "WHERE  CD.CODICE_FOTOGRAFIA_TERRENI = ? "+      
        "AND    SP.ID_STORICO_PARTICELLA = CD.ID_STORICO_PARTICELLA "+
        "AND    SP.COMUNE = F.COMUNE(+) "+
        "AND    NVL(SP.SEZIONE,-1) = NVL(F.SEZIONE(+),-1) "+
        "AND    SP.FOGLIO = F.FOGLIO(+) "+
        "AND    SP.COMUNE = C.ISTAT_COMUNE "+
        "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "+
        "AND    CD.ID_UTE = UT.ID_UTE " +
        "AND    UT.COMUNE = CUTE.ISTAT_COMUNE "+
        "AND    CUTE.ISTAT_PROVINCIA = PUTE.ISTAT_PROVINCIA "+
        "ORDER BY " +
        "        CUTE.DESCOM, " +
        "        PUTE.SIGLA_PROVINCIA, " +
        "        C.DESCOM," +
        "        P.SIGLA_PROVINCIA," +
        "        SP.FOGLIO  ";
      
      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query: " + query);
      SolmrLogger.debug(this, "Param codFotografia: " + codFotografia);
      SolmrLogger.debug(this, "Param dataRiferimento: " + dataRiferimento);

      stmt.setLong(1, codFotografia.longValue());
      /*stmt.setTimestamp(2, this.convertDateToTimestamp(dataRiferimento));
      stmt.setTimestamp(3, this.convertDateToTimestamp(dataRiferimento));*/

      ResultSet rs = stmt.executeQuery();
      
      String codiceOld="-1";
      QuadroDTerreni terreni=null;
      
      boolean trovato=false;
      
      while (rs.next())
      {
        trovato=true;
        String codice=rs.getString("CODICE");
        if(!codice.equals(codiceOld)) 
        {
          codiceOld=new String(codice.toString());
          if (terreni!=null) result.add(terreni);
          terreni=new QuadroDTerreni();
          //Reiposto le superfici a 0
          terreni.setSupAssVul(new BigDecimal(0));
          terreni.setSupAssNonVul(new BigDecimal(0));
          terreni.setSupCondVul(new BigDecimal(0));
          terreni.setSupCondNonVul(new BigDecimal(0));
        }
        terreni.setComune(rs.getString("DESCOM"));
        terreni.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
        terreni.setFoglio(rs.getString("FOGLIO"));
        //Vado a leggere i dati epr potermi ricavare le superfici
        String idAreaE=rs.getString("ID_AREA_E");
        String idFasciaFluviale=rs.getString("ID_FASCIA_FLUVIALE");
        String idTitoloPossesso=rs.getString("ID_TITOLO_POSSESSO");
        BigDecimal supCondotta=rs.getBigDecimal("SUPERFICIE_CONDOTTA");
        BigDecimal supAgronomica=rs.getBigDecimal("SUPERFICIE_AGRONOMICA");
        if ("5".equals(idTitoloPossesso)) //Asservimento
        {
          if (idFasciaFluviale!=null || ("2".equals(idAreaE) || idAreaE==null ))
          {
            //Zona vulnerabile 
            if (supCondotta!=null)
              terreni.setSupAssVul(terreni.getSupAssVul().add(supCondotta));
          }
          if (idFasciaFluviale==null && ("1".equals(idAreaE) || idAreaE==null ))
          {
            //Zona non vulnerabile 
            if (supCondotta!=null)
              terreni.setSupAssNonVul(terreni.getSupAssNonVul().add(supCondotta));
          }
        }
        else //Condizione
        {
          if (idFasciaFluviale!=null || ("2".equals(idAreaE) || idAreaE==null ))
          {
            //Zona vulnerabile 
            if (supAgronomica!=null)
              terreni.setSupCondVul(terreni.getSupCondVul().add(supAgronomica));
          }
          if (idFasciaFluviale==null && ("1".equals(idAreaE) || idAreaE==null ))
          {
            //Zona non vulnerabile 
            if (supAgronomica!=null)
              terreni.setSupCondNonVul(terreni.getSupCondNonVul().add(supAgronomica));
          } 
        }
        
        terreni.setIndirizzoUte(rs.getString("INDIRIZZO"));
        terreni.setComuneUte(rs.getString("UT_DESCOM"));
        terreni.setSiglaProvUte(rs.getString("UT_SIGLA_PROVINCIA"));
        
      }
      if (trovato) result.add(terreni);
      rs.close();

      SolmrLogger.debug(this, "getTerreniQuadroD10R - Found " + result.size()
          + " record(s)");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTerreniQuadroD10R - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getTerreniQuadroD10R - Generic Exception: "
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
        SolmrLogger.fatal(this,
            "getTerreniQuadroD10R - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getTerreniQuadroD10R - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  /**
   * 
   * 
   * 
   * Estrae la descrizione dei documenti legati alla conduzione e alal dichiarazione di consistenza
   * per il brogliaccio
   * 
   * 
   * @param idConduzioneDichiarata
   * @param idAzienda
   * @param idDichiarazioneConsistenza
   * @return
   * @throws DataAccessException
   */
  private String getDocumentiFromIdConduzioneDichiarata(long idConduzioneDichiarata,
      long idAzienda, Date dataInserimentoDichiarazione) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    String documento = null;
    try
    {
      SolmrLogger.debug(this,
          "[ConduzioneDichiarataDAO::getDocumentiFromIdConduzioneParticella] BEGIN.");      
       
      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT  "+
        "        TD.DESCRIZIONE, " +       
        "        DOC.NUMERO_PROTOCOLLO, " +
        "        DOC.DATA_PROTOCOLLO " + 
        "FROM    DB_DOCUMENTO_CONDUZIONE DOCC," +
        "        DB_DOCUMENTO DOC, " +
        "        DB_TIPO_DOCUMENTO TD, " +
        "        DB_DOCUMENTO_CATEGORIA DCAT, " +
        "        DB_TIPO_CATEGORIA_DOCUMENTO TCD," +
        "        DB_CONDUZIONE_DICHIARATA CD "+
        "WHERE   CD.ID_CONDUZIONE_DICHIARATA = ? " +
        "AND     CD.ID_CONDUZIONE_PARTICELLA = DOCC.ID_CONDUZIONE_PARTICELLA " +
        "AND     DOCC.ID_DOCUMENTO = DOC.ID_DOCUMENTO " +
        "AND     DOC.EXT_ID_DOCUMENTO = TD.ID_DOCUMENTO " +
        "AND     TD.ID_DOCUMENTO = DCAT.ID_DOCUMENTO " +
        "AND     DCAT.ID_CATEGORIA_DOCUMENTO = TCD.ID_CATEGORIA_DOCUMENTO " +
        "AND     DOC.ID_AZIENDA = ? " +    
        "AND     TD.ID_TIPOLOGIA_DOCUMENTO = 2 " +
        "AND     TCD.TIPO_IDENTIFICATIVO = 'TP' " +
        "AND     DOCC.ID_CONDUZIONE_PARTICELLA = CD.ID_CONDUZIONE_PARTICELLA " +
        "AND     TCD.IDENTIFICATIVO = CD.ID_TITOLO_POSSESSO " +
        "AND     DOCC.DATA_INIZIO_VALIDITA < ? " +
        "AND     NVL(DOCC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? ");
      
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ConduzioneDichiarataDAO::getDocumentiFromIdConduzioneParticella] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1,idConduzioneDichiarata);
      stmt.setLong(2,idAzienda);
      stmt.setTimestamp(3,convertDateToTimestamp(dataInserimentoDichiarazione));
      stmt.setTimestamp(4,convertDateToTimestamp(dataInserimentoDichiarazione));
      
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(Validator.isNotEmpty(documento))
        {
          documento += " - ";
        }
        else
        {
          documento = "";
        }
        
        String documentoDesc = rs.getString("DESCRIZIONE");      
        
        if(Validator.isNotEmpty(documentoDesc))
        {
          documento +=  documentoDesc;
        }              
      }
      
      return documento;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("documento", documento) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idConduzioneDichiarata", idConduzioneDichiarata),
        new Parametro("idAzienda", idAzienda),
        new Parametro("dataInserimentoDichiarazione", dataInserimentoDichiarazione) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ConduzioneDichiarataDAO::getDocumentiFromIdConduzioneParticella] ", t,
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
          "[ConduzioneDichiarataDAO::getDocumentiFromIdConduzioneParticella] END.");
    }
  }
  
  
  /**
   * 
   * Estrae la descrizione dei documenti legati alla conduzione per il brogliaccio
   * 
   * 
   * @param idConduzioneParticella
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  private String getDocumentiFromIdConduzioneParticella(long idConduzioneParticella,
      long idAzienda) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    String documento = null;
    try
    {
      SolmrLogger.debug(this,
          "[StampeDAO::getDocumentiFromIdConduzioneParticella] BEGIN.");      
       
      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT  "+
        "        TD.DESCRIZIONE, " +       
        "        DOC.NUMERO_PROTOCOLLO, " +
        "        DOC.DATA_PROTOCOLLO " + 
        "FROM    DB_DOCUMENTO_CONDUZIONE DOCC," +
        "        DB_DOCUMENTO DOC, " +
        "        DB_TIPO_DOCUMENTO TD, " +
        "        DB_DOCUMENTO_CATEGORIA DCAT, " +
        "        DB_TIPO_CATEGORIA_DOCUMENTO TCD," +
        "        DB_CONDUZIONE_PARTICELLA CP "+
        "WHERE   DOCC.ID_CONDUZIONE_PARTICELLA = ? " +
        "AND     DOCC.ID_DOCUMENTO = DOC.ID_DOCUMENTO " +
        "AND     DOC.EXT_ID_DOCUMENTO = TD.ID_DOCUMENTO " +
        "AND     TD.ID_DOCUMENTO = DCAT.ID_DOCUMENTO " +
        "AND     DCAT.ID_CATEGORIA_DOCUMENTO = TCD.ID_CATEGORIA_DOCUMENTO " +
        "AND     DOC.ID_AZIENDA = ? " +
        "AND     DOC.ID_STATO_DOCUMENTO IS NULL " +
        "AND     NVL(DOC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > SYSDATE " +    
        "AND     TD.ID_TIPOLOGIA_DOCUMENTO = 2 " +
        "AND     TCD.TIPO_IDENTIFICATIVO = 'TP' " +
        "AND     DOCC.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA " +
        "AND     TCD.IDENTIFICATIVO = CP.ID_TITOLO_POSSESSO ");
      
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[StampeDAO::getDocumentiFromIdConduzioneParticella] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1,idConduzioneParticella);
      stmt.setLong(2,idAzienda);
      
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(Validator.isNotEmpty(documento))
        {
          documento += " - ";
        }
        else
        {
          documento = "";
        }
        
        String documentoDesc = rs.getString("DESCRIZIONE");        
        
        if(Validator.isNotEmpty(documentoDesc))
        {
          documento +=  documentoDesc;
        }               
        
      }
      
      return documento;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("documento", documento) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idConduzioneParticella", idConduzioneParticella),
        new Parametro("idAzienda", idAzienda) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StampeDAO::getDocumentiFromIdConduzioneParticella] ", t,
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
          "[StampeDAO::getDocumentiFromIdConduzioneParticella] END.");
    }
  }
  
  
  public HashMap<Long, Vector<Long>> getFontiParticellaByAzienda(long idAzienda) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    HashMap<Long, Vector<Long>> hMapPartFonte = new HashMap<Long, Vector<Long>>();
    Vector<Long> vIdFonti = null;
    try
    {
      SolmrLogger.debug(this,
          "[StampeDAO::getFontiParticellaByAzienda] BEGIN.");      
       
      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT DISTINCT " +
        "       TF.ID_FONTE, " +
        "       SP.ID_PARTICELLA " + 
        "FROM   DB_CONDUZIONE_PARTICELLA CP, " +
        "       DB_UTE U, " +
        "       DB_STORICO_PARTICELLA SP, " +
        "       DB_PARTICELLA_CERTIFICATA PC, " + 
        "       DB_ESITO_PASCOLO_MAGRO EPM, " +
        "       DB_TIPO_FONTE TF " +
        "WHERE  U.ID_AZIENDA = ? " + 
        "AND    CP.ID_UTE = U.ID_UTE " +
        "AND    U.DATA_FINE_ATTIVITA IS NULL " + 
        "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "AND    SP.ID_PARTICELLA = CP.ID_PARTICELLA " + 
        "AND    SP.DATA_FINE_VALIDITA IS NULL " +
        "AND    SP.ID_PARTICELLA  = PC.ID_PARTICELLA " + 
        "AND    PC.ID_PARTICELLA_CERTIFICATA = EPM.ID_PARTICELLA_CERTIFICATA " + 
        "AND    EPM.DATA_FINE_VALIDITA IS NULL " +
        "AND    EPM.ID_FONTE = TF.ID_FONTE " +
        "AND    TF.FLAG_VINCOLO = 'S' ");
      
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[StampeDAO::getFontiParticellaByAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1,idAzienda);
      
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        Long idParticella = new Long(rs.getLong("ID_PARTICELLA"));
        if(hMapPartFonte.get(idParticella) != null)
        {
          vIdFonti = hMapPartFonte.get(idParticella);
          vIdFonti.add(new Long(rs.getLong("ID_FONTE")));
          hMapPartFonte.put(idParticella, vIdFonti);
        }
        else
        {
          vIdFonti = new Vector<Long>();
          vIdFonti.add(new Long(rs.getLong("ID_FONTE")));
          hMapPartFonte.put(idParticella, vIdFonti);
        }        
      }
      
      return hMapPartFonte;
      
    }
    catch (Exception t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("hMapPartFonte", hMapPartFonte), 
          new Variabile("vIdFonti", vIdFonti) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StampeDAO::getFontiParticellaByAzienda] ", t,
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
          "[StampeDAO::getFontiParticellaByAzienda] END.");
    }
  }
  
  public HashMap<Long, Vector<Long>> getFontiParticellaByValidazione(long codFotografiaTerreni) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    HashMap<Long, Vector<Long>> hMapPartFonte = new HashMap<Long, Vector<Long>>();
    Vector<Long> vIdFonti = null;
    try
    {
      SolmrLogger.debug(this,
          "[StampeDAO::getFontiParticellaByValidazione] BEGIN.");      
       
      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT TF.ID_FONTE, " +
        "       SP.ID_PARTICELLA " + 
        "FROM   DB_CONDUZIONE_DICHIARATA CD, " +
        "       DB_STORICO_PARTICELLA SP, " +
        "       DB_PARTICELLA_CERTIFICATA PC, " + 
        "       DB_ESITO_PASCOLO_MAGRO EPM, " +
        "       DB_TIPO_FONTE TF, " +
        "       DB_DICHIARAZIONE_CONSISTENZA DC " + 
        "WHERE  CD.CODICE_FOTOGRAFIA_TERRENI = ? " +
        "AND    CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
        "AND    CD.CODICE_FOTOGRAFIA_TERRENI = DC.CODICE_FOTOGRAFIA_TERRENI " +
        "AND    SP.ID_PARTICELLA  = PC.ID_PARTICELLA " +
        "AND    PC.ID_PARTICELLA_CERTIFICATA = EPM.ID_PARTICELLA_CERTIFICATA " + 
        "AND    NVL(EPM.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) > DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "AND    EPM.DATA_INIZIO_VALIDITA < DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "AND    EPM.ID_FONTE = TF.ID_FONTE " +
        "AND    TF.FLAG_VINCOLO = 'S' ");
      
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[StampeDAO::getFontiParticellaByValidazione] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, codFotografiaTerreni);
      
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        Long idParticella = new Long(rs.getLong("ID_PARTICELLA"));
        if(hMapPartFonte.get(idParticella) != null)
        {
          vIdFonti = hMapPartFonte.get(idParticella);
          vIdFonti.add(new Long(rs.getLong("ID_FONTE")));
          hMapPartFonte.put(idParticella, vIdFonti);
        }
        else
        {
          vIdFonti = new Vector<Long>();
          vIdFonti.add(new Long(rs.getLong("ID_FONTE")));
          hMapPartFonte.put(idParticella, vIdFonti);
        }        
      }
      
      return hMapPartFonte;
      
    }
    catch (Exception t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("hMapPartFonte", hMapPartFonte), 
          new Variabile("vIdFonti", vIdFonti) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("codFotografiaTerreni", codFotografiaTerreni) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StampeDAO::getFontiParticellaByValidazione] ", t,
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
          "[StampeDAO::getFontiParticellaByValidazione] END.");
    }
  }
  
  

}
