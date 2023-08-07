package it.csi.solmr.integration;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.smrcomms.reportdin.dto.ColonneRisultatoOutput;
import it.csi.smrcomms.reportdin.dto.VariabileReportOutput;
import it.csi.smrcomms.reportdin.util.ReportQueryUtil;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.ProvinciaVO;
import it.csi.solmr.dto.StringcodeDescription;
import it.csi.solmr.dto.UtenteIrideVO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.DelegaVO;
import it.csi.solmr.dto.anag.ElencoMandatiValidazioniFiltroExcelVO;
import it.csi.solmr.dto.anag.IntermediarioAnagVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.dto.anag.TipoCategoriaNotificaVO;
import it.csi.solmr.dto.anag.TipoCessazioneVO;
import it.csi.solmr.dto.anag.TipoTipologiaAziendaVO;
import it.csi.solmr.dto.anag.sian.SianBaseCodeDescription;
import it.csi.solmr.dto.anag.terreni.SezioneVO;
import it.csi.solmr.dto.anag.terreni.TipoImpiantoVO;
import it.csi.solmr.dto.anag.terreni.TipoIrrigazioneVO;
import it.csi.solmr.dto.anag.terreni.TipoMacroUsoVO;
import it.csi.solmr.dto.anag.terreni.TipoPiantaConsociataVO;
import it.csi.solmr.dto.anag.terreni.TipoPotenzialitaIrriguaVO;
import it.csi.solmr.dto.anag.terreni.TipoRotazioneColturaleVO;
import it.csi.solmr.dto.anag.terreni.TipoTerrazzamentoVO;
import it.csi.solmr.dto.comune.IntermediarioVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.DataControlException;
import it.csi.solmr.exception.NotFoundException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * <p>
 * Title: S.O.L.M.R.
 * </p>
 * <p>
 * Description: Servizi On-Line per il Mondo Rurale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: TOBECONFIG
 * </p>
 * 
 * @author Luca Romanello
 * @version 1.0
 */

public class CommonDAO extends BaseDAO
{

  public CommonDAO(String refName) throws ResourceAccessException
  {
    super(refName);
  }

  public Vector<CodeDescription> getCodeDescriptions(String tableName)
      throws DataAccessException, NotFoundException
  {
    return getCodeDescriptions(tableName, SolmrConstants.CD_DESCRIPTION);
  }

  public String getRegioneByProvincia(String siglaProvincia)
      throws DataAccessException
  {
    String istatRegione = "";
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT REGIONE.ID_REGIONE, REGIONE.DESCRIZIONE "
          + "  FROM PROVINCIA, REGIONE "
          + "  WHERE PROVINCIA.SIGLA_PROVINCIA = upper(?)"
          + "  AND REGIONE.ID_REGIONE=PROVINCIA.ID_REGIONE";
      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      stmt.setString(1, siglaProvincia);
      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        if (rs.next())
        {
          istatRegione = rs.getString("ID_REGIONE");
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return istatRegione;
  }

  public String getIstatProvinciaBySiglaProvincia(String siglaProvincia)
      throws DataAccessException
  {
    String istatProvincia = "";
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT provincia.ISTAT_PROVINCIA "
          + "  FROM PROVINCIA  "
          + "  WHERE PROVINCIA.SIGLA_PROVINCIA = upper(?)";
      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      stmt.setString(1, siglaProvincia);
      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        if (rs.next())
        {
          istatProvincia = rs.getString("ISTAT_PROVINCIA");
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return istatProvincia;
  }

  public String getProvinciaByIstat(String istatProvincia)
      throws DataAccessException
  {
    String descrizioneProvincia = "";
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT provincia.DESCRIZIONE " + "  FROM PROVINCIA  "
          + "  WHERE PROVINCIA.ISTAT_PROVINCIA = ?";
      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      stmt.setString(1, istatProvincia);
      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        if (rs.next())
        {
          descrizioneProvincia = rs.getString("DESCRIZIONE");
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return descrizioneProvincia;
  }

  /**
   * Restituisce un VO della Provincia in base al parametro passato. <br>
   * Se criterio è lungo due caratteri, la ricerca è fatta sulla sigla. <br>
   * Se criterio è lungo tre caratteri, la ricerca è fatta sul codice istat.
   * <br>
   * In tutti gli altri casi viene fatta una like sulla descrizione.
   * 
   * @param criterio
   *          String
   * @return it.csi.solmr.dto.ProvinciaVO
   * @throws DataAccessException
   */
  public ProvinciaVO getProvinciaByCriterio(String criterio)
      throws DataAccessException
  {
    ProvinciaVO pVO = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT DESCRIZIONE, " + "       ISTAT_PROVINCIA, "
          + "       ID_REGIONE, " + "       SIGLA_PROVINCIA "
          + "  FROM PROVINCIA " + "  WHERE ";
      if (criterio != null && !criterio.trim().equals(""))
      {
        switch (criterio.length())
        {
          case 2:
            search += " SIGLA_PROVINCIA = ? ";
            break;
          case 3:
            search += " ISTAT_PROVINCIA = ? ";
            break;
          default:
            search += " DESCRIZIONE LIKE ? ";
            criterio += "%";
            break;
        }
        stmt = conn.prepareStatement(search);

        SolmrLogger.debug(this, "Executing query: " + search);

        stmt.setString(1, criterio);
        ResultSet rs = stmt.executeQuery();

        if (rs != null)
        {
          if (rs.next())
          {
            pVO = new ProvinciaVO();
            pVO.setDescrizione(rs.getString("DESCRIZIONE"));
            pVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));
            pVO.setIdRegione(rs.getString("ID_REGIONE"));
          }
        }
        else
          throw new DataAccessException();

        rs.close();
        stmt.close();
      }

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return pVO;
  }

  public String getSiglaProvinciaByIstatProvincia(String istatProvincia)
      throws DataAccessException
  {

    String siglaProvincia = "";
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT PROVINCIA.SIGLA_PROVINCIA "
          + "  FROM PROVINCIA  " + "  WHERE provincia.ISTAT_PROVINCIA = ?";
      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      stmt.setString(1, istatProvincia);
      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        if (rs.next())
        {
          siglaProvincia = rs.getString("SIGLA_PROVINCIA");
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return siglaProvincia;
  }

  public Vector<ProvinciaVO> getProvinceByRegione(String idRegione)
      throws DataAccessException
  {
    Vector<ProvinciaVO> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT ISTAT_PROVINCIA, SIGLA_PROVINCIA, "
          + "       ID_REGIONE, DESCRIZIONE " + "  FROM PROVINCIA "
          + " WHERE ID_REGIONE = " + idRegione + " " + " ORDER BY DESCRIZIONE";
      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query getProvinceByRegione : " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        result = new Vector<ProvinciaVO>();
        while (rs.next())
        {
          ProvinciaVO pVO = new ProvinciaVO();
          pVO.setIstatProvincia(rs.getString("ISTAT_PROVINCIA"));
          pVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));
          pVO.setDescrizione(rs.getString("DESCRIZIONE"));
          pVO.setIdRegione(rs.getString("ID_REGIONE"));

          result.add(pVO);
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  
  public Vector<ProvinciaVO> getProvince()
      throws DataAccessException
  {
    Vector<ProvinciaVO> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = 
          "SELECT ISTAT_PROVINCIA, SIGLA_PROVINCIA, "
          + "       ID_REGIONE, DESCRIZIONE " 
          + "  FROM PROVINCIA "
          + " ORDER BY SIGLA_PROVINCIA";
      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        result = new Vector<ProvinciaVO>();
        while (rs.next())
        {
          ProvinciaVO pVO = new ProvinciaVO();
          pVO.setIstatProvincia(rs.getString("ISTAT_PROVINCIA"));
          pVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));
          pVO.setDescrizione(rs.getString("DESCRIZIONE"));
          pVO.setIdRegione(rs.getString("ID_REGIONE"));

          result.add(pVO);
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  public Vector<ComuneVO> getComuniLikeByRegione(String idRegione, String like)
      throws DataAccessException, NotFoundException
  {
    Vector<ComuneVO> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT ISTAT_COMUNE, DESCOM, "
          + "       C.ISTAT_PROVINCIA ALIAS_ISTAT_PROVINCIA, "
          + "       SIGLA_PROVINCIA, FLAG_ESTERO, CAP, CODFISC "
          + "  FROM PROVINCIA P, COMUNE C "
          + " WHERE C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
          + "   AND ID_REGIONE = " + idRegione + " "
          + "   AND UPPER(DESCOM) LIKE UPPER('" + like + "%')"
          + " ORDER BY DESCOM";
      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        result = new Vector<ComuneVO>();
        while (rs.next())
        {
          ComuneVO cVO = new ComuneVO();
          cVO.setIstatComune(rs.getString("ISTAT_COMUNE"));
          cVO.setDescom(rs.getString("DESCOM"));
          cVO.setIstatProvincia(rs.getString("ALIAS_ISTAT_PROVINCIA"));
          cVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
          cVO.setFlagEstero(rs.getString("FLAG_ESTERO"));
          cVO.setCap(rs.getString("CAP"));
          cVO.setCodfisc(rs.getString("CODFISC"));

          result.add(cVO);
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // Ricerca le tipologie di Forma Serra disponibili:
  // Codice: id_forma_serra + "&&" + fattore_cubatura
  // Descrizione: descrizione
  public Vector<StringcodeDescription> getTipiFormaSerra()
      throws DataAccessException
  {
    Vector<StringcodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    final String SEPARATORE = "_";

    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT db_tipo_forma_serra.id_forma_serra, "
          + "db_tipo_forma_serra.descrizione, "
          + "db_tipo_forma_serra.fattore_cubatura "
          + "FROM db_tipo_forma_serra "
          + "ORDER BY db_tipo_forma_serra.descrizione DESC";
      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        result = new Vector<StringcodeDescription>();
        while (rs.next())
        {
          StringcodeDescription strCodeDescVO = new StringcodeDescription();
          strCodeDescVO.setCode(rs.getString("id_forma_serra") + SEPARATORE
              + checkDouble(rs.getString("fattore_cubatura")));
          strCodeDescVO.setDescription(rs.getString("descrizione"));

          result.add(strCodeDescVO);
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // Ricerca le tipologie di coltura disponibili
  // Codice: id_coltura
  // Descrizione: descrizione
  public Vector<CodeDescription> getTipiColtura() throws DataAccessException
  {
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT db_tipo_coltura.id_coltura, "
          + "       db_tipo_coltura.descrizione " + "FROM db_tipo_coltura "
          + "ORDER BY db_tipo_coltura.descrizione DESC";

      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        result = new Vector<CodeDescription>();
        while (rs.next())
        {
          CodeDescription codeDescVO = new CodeDescription();
          codeDescVO.setCode(new Integer(rs.getString("id_coltura")));
          codeDescVO.setDescription(rs.getString("descrizione"));

          result.add(codeDescVO);
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // Ricerca le tipologie di coltura che vengono coltivate in serra
  // Codice: id_coltura
  // Descrizione: descrizione
  public Vector<CodeDescription> getTipiColturaInSerra()
      throws DataAccessException
  {
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT db_tipo_coltura.id_coltura, "
          + "       db_tipo_coltura.descrizione " + "FROM db_tipo_coltura "
          + "WHERE UPPER(db_Tipo_Coltura.coltura_in_serra) LIKE 'S' "
          + "ORDER BY db_tipo_coltura.descrizione DESC";

      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        result = new Vector<CodeDescription>();
        while (rs.next())
        {
          CodeDescription codeDescVO = new CodeDescription();
          codeDescVO.setCode(new Integer(rs.getString("id_coltura")));
          codeDescVO.setDescription(rs.getString("descrizione"));

          result.add(codeDescVO);
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // Ricerca le tipologie di coltura che non vengono coltivate in serra
  // Codice: id_coltura
  // Descrizione: descrizione
  public Vector<CodeDescription> getTipiColturaNonInSerra()
      throws DataAccessException
  {
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT db_tipo_coltura.id_coltura, "
          + "       db_tipo_coltura.descrizione " + "FROM db_tipo_coltura "
          + "WHERE db_Tipo_Coltura.coltura_in_serra is NULL "
          + "ORDER BY db_tipo_coltura.descrizione DESC";

      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        result = new Vector<CodeDescription>();
        while (rs.next())
        {
          CodeDescription codeDescVO = new CodeDescription();
          codeDescVO.setCode(new Integer(rs.getString("id_coltura")));
          codeDescVO.setDescription(rs.getString("descrizione"));

          result.add(codeDescVO);
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  public Vector<CodeDescription> getTipiGenereMacchina()
      throws DataAccessException, NotFoundException
  {
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT id_genere_macchina, " + "       descrizione, "
          + "       codifica_breve " + "  FROM DB_Tipo_genere_macchina "
          + " ORDER BY descrizione";
      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        result = new Vector<CodeDescription>();
        while (rs.next())
        {
          CodeDescription cd = new CodeDescription();

          cd.setCode(new Integer(rs.getInt(1)));
          cd.setDescription(rs.getString(2));
          cd.setSecondaryCode(rs.getString(3));

          result.add(cd);
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      if (result.size() == 0)
        throw new NotFoundException();

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (NotFoundException nfexc)
    {
      SolmrLogger.fatal(this, "NotFoundException: " + nfexc.getMessage());
      throw nfexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  public Vector<CodeDescription> getCodeDescriptions(String tableName,
      String orderBy) throws DataAccessException, NotFoundException
  {
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT id_" + tableName + ", " + "       descrizione "
          + "  FROM DB_Tipo_" + tableName + " ORDER BY ";

      if (Validator.isNotEmpty(orderBy))
      {
        search += orderBy;
      }
      else
      {
        search += (String) SolmrConstants.get("CD_DESCRIPTION");
      }

      stmt = conn.prepareStatement(search);
      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        result = new Vector<CodeDescription>();
        while (rs.next())
        {
          CodeDescription cd = new CodeDescription();

          cd.setCode(new Integer(rs.getInt(1)));
          cd.setDescription(rs.getString(2));

          result.add(cd);
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      if (result.size() == 0)
        throw new NotFoundException();

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (NotFoundException nfexc)
    {
      SolmrLogger.fatal(this, "NotFoundException: " + nfexc.getMessage());
      throw nfexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  public Vector<CodeDescription> getValidCodeDescriptions(String tableName,
      String orderBy) throws DataAccessException
  {
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT id_" + tableName + ", " + "       descrizione "
          + "  FROM DB_Tipo_" + tableName
          + "  WHERE DATA_FINE_VALIDITA IS NULL "
          + " ORDER BY ";

      if (Validator.isNotEmpty(orderBy))
      {
        search += orderBy;
      }
      else
      {
        search += (String) SolmrConstants.get("CD_DESCRIPTION");
      }

      stmt = conn.prepareStatement(search);
      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      result = new Vector<CodeDescription>();
      
      if (rs != null)
      {
        while (rs.next())
        {
          CodeDescription cd = new CodeDescription();

          cd.setCode(new Integer(rs.getInt(1)));
          cd.setDescription(rs.getString(2));

          result.add(cd);
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  public CodeDescription[] getCodeDescriptionsNew(String tableName,
      String filtro, String valFiltro, String orderBy)
      throws DataAccessException
  {
    Vector<CodeDescription> result = new Vector<CodeDescription>();
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT ID_" + tableName + " ,DESCRIZIONE " + " FROM DB_"
          + tableName + " WHERE DATA_FINE_VALIDITA IS NULL ";

      if (Validator.isNotEmpty(filtro))
        search += " AND " + filtro + " = ? ";

      search += " ORDER BY ";

      if (Validator.isNotEmpty(orderBy))
        search += orderBy;
      else
        search += "DESCRIZIONE";

      stmt = conn.prepareStatement(search);
      SolmrLogger.debug(this, "getCodeDescriptionsNew Executing query: "
          + search);

      if (Validator.isNotEmpty(valFiltro))
        stmt.setLong(1, Long.parseLong(valFiltro));

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        CodeDescription cd = new CodeDescription();

        cd.setCode(new Integer(rs.getInt(1)));
        cd.setDescription(rs.getString(2));

        result.add(cd);
      }

      SolmrLogger.debug(this, "getCodeDescriptionsNew Executed query - Found "
          + result.size() + " result(s).");
      return result.size() == 0 ? null : (CodeDescription[]) result
          .toArray(new CodeDescription[0]);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getCodeDescriptionsNew SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getCodeDescriptionsNew Generic Exception: "
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
            "getCodeDescriptionsNew SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getCodeDescriptionsNew Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  
  public Vector<CodeDescription> getListTipiTitoloPossesso(String orderBy)
	  throws DataAccessException
	{
		Vector<CodeDescription> result = new Vector<CodeDescription>();
		Connection conn = null;
		PreparedStatement stmt = null;
		try
		{
		  conn = getDatasource().getConnection();
		
		  String search = "SELECT ID_TITOLO_POSSESSO, DESCRIZIONE, FLAG_CESSAZIONE_CONDUZIONE "+
		  	 							"FROM DB_TIPO_TITOLO_POSSESSO "+
		  	 							"ORDER BY ";
		  
		  if (Validator.isNotEmpty(orderBy))
      {
        search += orderBy;
      }
      else
      {
        search += (String) SolmrConstants.get("CD_DESCRIPTION");
      }
		
		
		  stmt = conn.prepareStatement(search);
		  SolmrLogger.debug(this, "Executing query: " + search);
		
		  ResultSet rs = stmt.executeQuery();
		
	    
	    while (rs.next())
	    {
	      CodeDescription cd = new CodeDescription();
	      cd.setCode(new Integer(rs.getInt("ID_TITOLO_POSSESSO")));
	      cd.setDescription(rs.getString("DESCRIZIONE"));
	      cd.setCodeFlag(rs.getString("FLAG_CESSAZIONE_CONDUZIONE"));
	      result.add(cd);
	    }
		
		  rs.close();
		  stmt.close();
		
		  SolmrLogger.debug(this, "Executed query - Found " + result.size()
		      + " result(s).");
		}
		catch (SQLException exc)
		{
		  SolmrLogger.fatal(this, "getListTipiTitoloPossesso SQLException: " + exc.getMessage());
		  throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex)
		{
		  SolmrLogger.fatal(this, "getListTipiTitoloPossesso Generic Exception: " + ex.getMessage());
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
		        "getListTipiTitoloPossesso SQLException while closing Statement and Connection: "
		            + exc.getMessage());
		    throw new DataAccessException(exc.getMessage());
		  }
		  catch (Exception ex)
		  {
		    SolmrLogger.fatal(this,
		        "getListTipiTitoloPossesso Generic Exception while closing Statement and Connection: "
		            + ex.getMessage());
		    throw new DataAccessException(ex.getMessage());
		  }
		}
		return result;
	}
  
  
  /**
   * Caricamento: combo popolata con il campo descrizione dei record di
   * DB_TIPO_TIPOLOGIA_DOCUMENTO aventi ID_TIPOLOGIA_DOCUMENTO <> 4 se l’azienda
   * non è cessata di tutti i record di DB_TIPO_TIPOLOGIA_DOCUMENTO se l’azienda
   * è cessata Ordinamento: per descrizione in ordine alfabetico
   * 
   * @param cessata
   * @return
   * @throws DataAccessException
   */
  public Vector<CodeDescription> getTipiTipologiaDocumento(boolean cessata)
      throws DataAccessException
  {
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "select id_tipologia_documento, descrizione "
          + "from db_tipo_tipologia_documento ";
      if (!cessata)
        search += "where ID_TIPOLOGIA_DOCUMENTO <> 4 ";
      search += " order by descrizione ";

      stmt = conn.prepareStatement(search);
      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      result = new Vector<CodeDescription>();
      while (rs.next())
      {
        CodeDescription cd = new CodeDescription();
        cd.setCode(new Integer(rs.getInt(1)));
        cd.setDescription(rs.getString(2));
        result.add(cd);
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this,
          "getTipiTipologiaDocumento Executed query - Found " + result.size()
              + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTipiTipologiaDocumento SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getTipiTipologiaDocumento Generic Exception: "
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
                "getTipiTipologiaDocumento SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getTipiTipologiaDocumento Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  public Vector<CodeDescription> getTipologiaFabbricati()
      throws DataAccessException
  {
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = " SELECT ID_TIPOLOGIA_FABBRICATO,DESCRIZIONE,UNITA_MISURA"
          + " FROM DB_TIPO_TIPOLOGIA_FABBRICATO"
          + " WHERE ANNO_FINE_VALIDITA IS NULL" + " ORDER BY DESCRIZIONE";

      stmt = conn.prepareStatement(search);
      SolmrLogger.debug(this, "getTipologiaFabbricati Executing query: "
          + search);

      ResultSet rs = stmt.executeQuery();

      result = new Vector<CodeDescription>();
      while (rs.next())
      {
        CodeDescription cd = new CodeDescription();

        cd.setCode(new Integer(rs.getInt(1)));
        cd.setDescription(rs.getString(2));
        cd.setSecondaryCode(rs.getString(3));
        result.add(cd);
      }

      rs.close();

      SolmrLogger.debug(this, "getTipologiaFabbricati Executed query - Found "
          + result.size() + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTipologiaFabbricati SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getTipologiaFabbricati Generic Exception: "
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
            "getTipologiaFabbricati SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getTipologiaFabbricati Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  public Vector<CodeDescription> getTipiFormaFabbricato(
      Long idTipologiaFabbricato) throws DataAccessException
  {
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = " SELECT ID_FORMA_FABBRICATO,DESCRIZIONE,FATTORE_CUBATURA"
          + " FROM DB_TIPO_FORMA_FABBRICATO"
          + " WHERE ID_TIPOLOGIA_FABBRICATO=?" + " ORDER BY DESCRIZIONE";

      stmt = conn.prepareStatement(search);

      stmt.setLong(1, idTipologiaFabbricato.longValue());

      SolmrLogger.debug(this, "getTipiFormaFabbricato Executing query: "
          + search);

      ResultSet rs = stmt.executeQuery();

      result = new Vector<CodeDescription>();
      while (rs.next())
      {
        CodeDescription cd = new CodeDescription();

        cd.setCode(new Integer(rs.getInt(1)));
        cd.setDescription(rs.getString(2));
        cd.setSecondaryCode(rs.getString(3));
        result.add(cd);
      }

      rs.close();

      SolmrLogger.debug(this, "getTipiFormaFabbricato Executed query - Found "
          + result.size() + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTipiFormaFabbricato SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getTipiFormaFabbricato Generic Exception: "
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
            "getTipiFormaFabbricato SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getTipiFormaFabbricato Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  public Vector<CodeDescription> getTipiColturaSerra(Long idTipologiaFabbricato)
      throws DataAccessException
  {
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = " SELECT ID_COLTURA_SERRA,DESCRIZIONE,MESI_RISCALDAMENTO"
          + " FROM DB_TIPO_COLTURA_SERRA" + " WHERE ID_TIPOLOGIA_FABBRICATO=?"
          + " ORDER BY DESCRIZIONE";

      stmt = conn.prepareStatement(search);

      stmt.setLong(1, idTipologiaFabbricato.longValue());

      SolmrLogger.debug(this, "getTipiColturaSerra Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      result = new Vector<CodeDescription>();
      while (rs.next())
      {
        CodeDescription cd = new CodeDescription();

        cd.setCode(new Integer(rs.getInt(1)));
        cd.setDescription(rs.getString(2));
        cd.setSecondaryCode(rs.getString(3));
        result.add(cd);
      }

      rs.close();

      SolmrLogger.debug(this, "getTipiColturaSerra Executed query - Found "
          + result.size() + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTipiColturaSerra SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getTipiColturaSerra Generic Exception: "
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
            "getTipiColturaSerra SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getTipiColturaSerra Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  /**
   * Estrae le notifiche relative al ruolo
   * 
   * @param ruoloUtenza
   * @return
   * @throws DataAccessException
   */
  public Vector<TipoCategoriaNotificaVO> getTipiCategoriaNotificaFromRuolo(RuoloUtenza ruoloUtenza)
      throws DataAccessException
  {

    Vector<TipoCategoriaNotificaVO> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = " " +
      		"SELECT TCN.ID_CATEGORIA_NOTIFICA, " +
      		"       TCN.ID_TIPOLOGIA_NOTIFICA, " +
      		"       TCN.DESCRIZIONE, " +
      		"       TCN.ID_TIPO_ENTITA, " +
      		"       TCN.INVIA_EMAIL_AD_EVENTO_NOTIFICA " +
      		"FROM   DB_R_TIPO_NOTIFICA_RUOLO RT, " +
      		"       DB_D_TIPO_RUOLO DT, " +
      		"       DB_TIPO_CATEGORIA_NOTIFICA TCN " +
      		"WHERE  DT.RUOLO = ? " +
      		"AND    DT.ID_TIPO_RUOLO = RT.EXT_ID_TIPO_RUOLO " +
      		"AND    RT.DATA_FINE_VALIDITA IS NULL " +
      		"AND    RT.ID_CATEGORIA_NOTIFICA = TCN.ID_CATEGORIA_NOTIFICA ";
      if(ruoloUtenza.isReadWrite())
      {
        query += ""+ 
          "AND    (RT.ACCESSO_RW = 'S' OR RT.ACCESSO_RW = 'N') ";
      }
      else
      {
        query += ""+ 
          "AND    RT.ACCESSO_RW = 'N' ";
      }

      query +=
         "ORDER BY TCN.DESCRIZIONE";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, " -- Executing query getTipiCategoriaNotificaFromRuolo =" + query);
      SolmrLogger.debug(this, " -- codiceRuolo ="+ruoloUtenza.getCodiceRuolo().trim());
      
      
      stmt.setString(1, ruoloUtenza.getCodiceRuolo().trim());

      ResultSet rs = stmt.executeQuery();
      if(rs == null)
    	  SolmrLogger.debug(this, " -- 	rs == null");  

      while (rs.next())
      {
        if(result == null)
        {
          SolmrLogger.debug(this, " -- 	result == null");
          result = new Vector<TipoCategoriaNotificaVO>();
        }
        TipoCategoriaNotificaVO tipoCategoriaNotificaVO = new TipoCategoriaNotificaVO();
        tipoCategoriaNotificaVO.setIdCategoriaNotifica(new Integer(rs.getInt("ID_CATEGORIA_NOTIFICA")));
        tipoCategoriaNotificaVO.setIdTipologiaNotifica(new Integer(rs.getInt("ID_TIPOLOGIA_NOTIFICA")));
        tipoCategoriaNotificaVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoCategoriaNotificaVO.setIdTipoEntita(checkIntegerNull(rs.getString("ID_TIPO_ENTITA")));
        tipoCategoriaNotificaVO.setInviaEmail(rs.getString("INVIA_EMAIL_AD_EVENTO_NOTIFICA"));
        
        result.add(tipoCategoriaNotificaVO);
      }
      
      rs.close();
      stmt.close();

    /*  SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");*/
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTipiTipologiaNotificaFromRuolo SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  public Vector<TipoCategoriaNotificaVO> getTipiCategoriaNotificaFromEntita(String tipoEntita)
      throws DataAccessException
  {

    Vector<TipoCategoriaNotificaVO> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = " " +
          "SELECT TCN.ID_CATEGORIA_NOTIFICA, " +
          "       TCN.ID_TIPOLOGIA_NOTIFICA, " +
          "       TCN.DESCRIZIONE, " +
          "       TCN.ID_TIPO_ENTITA " +
          "FROM   DB_TIPO_CATEGORIA_NOTIFICA TCN, " +
          "       DB_TIPO_ENTITA TE " +
          "WHERE  TE.CODICE_TIPO_ENTITA = ? " +
          "AND    TE.ID_TIPO_ENTITA = TCN.ID_TIPO_ENTITA " +
          "AND    TCN.DATA_FINE_VALIDITA IS NULL " +
          "ORDER BY TCN.DESCRIZIONE";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);
      
      
      stmt.setString(1, tipoEntita);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        if(result == null)
        {
          result = new Vector<TipoCategoriaNotificaVO>();
        }
        TipoCategoriaNotificaVO tipoCategoriaNotificaVO = new TipoCategoriaNotificaVO();
        tipoCategoriaNotificaVO.setIdCategoriaNotifica(new Integer(rs.getInt("ID_CATEGORIA_NOTIFICA")));
        tipoCategoriaNotificaVO.setIdTipologiaNotifica(new Integer(rs.getInt("ID_TIPOLOGIA_NOTIFICA")));
        tipoCategoriaNotificaVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoCategoriaNotificaVO.setIdTipoEntita(checkIntegerNull(rs.getString("ID_TIPO_ENTITA")));
        
        result.add(tipoCategoriaNotificaVO);
      }
      
      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTipiCategoriaNotificaFromEntita SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  
  public Vector<CodeDescription> getCategoriaNotifica() throws DataAccessException
  {

    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = " " +
        "SELECT TCN.ID_CATEGORIA_NOTIFICA, " +
        "       TCN.ID_TIPOLOGIA_NOTIFICA, " +
        "       TCN.DESCRIZIONE " +
        "FROM   DB_TIPO_CATEGORIA_NOTIFICA TCN " +
        "ORDER BY TCN.DESCRIZIONE";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "getCategoriaNotifica - Executing query: " + query);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        if(result == null)
        {
          result = new Vector<CodeDescription>();
        }
        CodeDescription cd = new CodeDescription();
        cd.setCode(new Integer(rs.getInt("ID_CATEGORIA_NOTIFICA")));
        cd.setSecondaryCode(rs.getString("ID_TIPOLOGIA_NOTIFICA"));
        cd.setDescription(rs.getString("DESCRIZIONE"));
        result.add(cd);
      }
      
      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "getCategoriaNotifica - Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getCategoriaNotifica SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getCategoriaNotifica - Generic Exception: " + ex.getMessage());
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
            "getCategoriaNotifica - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "getCategoriaNotifica - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  
  public Vector<CodeDescription> getTipiTipologiaNotifica() throws DataAccessException
  {
  
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
    
      String search = 
        "SELECT ID_TIPOLOGIA_NOTIFICA, " + 
        "       DESCRIZIONE " + 
        "FROM   DB_TIPO_TIPOLOGIA_NOTIFICA " +
        "ORDER BY ID_TIPOLOGIA_NOTIFICA ";
    
      stmt = conn.prepareStatement(search);
    
      SolmrLogger.debug(this, "Executing query: " + search);
    
      ResultSet rs = stmt.executeQuery();
    
      while (rs.next())
      {
        if(result == null)
        {
          result = new Vector<CodeDescription>();
        }
        CodeDescription cd = new CodeDescription();
        cd.setCode(new Integer(rs.getInt(1)));
        cd.setDescription(rs.getString(2));
        result.add(cd);
      }
      
    
      rs.close();
      stmt.close();
    
    
      SolmrLogger.debug(this, "getTipiTipologiaNotifica: Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTipiTipologiaNotifica - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getTipiTipologiaNotifica - Generic Exception: " + ex.getMessage());
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
            "getTipiTipologiaNotifica - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "getTipiTipologiaNotifica - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  public Vector<CodeDescription> getTipologiaNotificaFromRuolo(RuoloUtenza ruoloUtenza)
      throws DataAccessException
  {

    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = " " +
          "SELECT DISTINCT TTN.ID_TIPOLOGIA_NOTIFICA, " +
          "       TTN.DESCRIZIONE " +
          "FROM   DB_R_TIPO_NOTIFICA_RUOLO RT, " +
          "       DB_D_TIPO_RUOLO DT, " +
          "       DB_TIPO_CATEGORIA_NOTIFICA TCN, " +
          "       DB_TIPO_TIPOLOGIA_NOTIFICA TTN " +
          "WHERE  DT.RUOLO = ? " +
          "AND    DT.ID_TIPO_RUOLO = RT.EXT_ID_TIPO_RUOLO " +
          "AND    RT.DATA_FINE_VALIDITA IS NULL " +
          "AND    RT.ID_CATEGORIA_NOTIFICA = TCN.ID_CATEGORIA_NOTIFICA " +
          "AND    TCN.ID_TIPOLOGIA_NOTIFICA = TTN.ID_TIPOLOGIA_NOTIFICA ";
      if(ruoloUtenza.isReadWrite())
      {
        query += ""+ 
          "AND    (RT.ACCESSO_RW = 'S' OR RT.ACCESSO_RW = 'N') ";
      }
      else
      {
        query += ""+ 
          "AND    RT.ACCESSO_RW = 'N' ";
      }

      query +=
         "ORDER BY TTN.DESCRIZIONE";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, " -- Executing query getTipologiaNotificaFromRuolo : " + query);
      
      SolmrLogger.debug(this, " -- codiceRuolo ="+ruoloUtenza.getCodiceRuolo().trim());
      stmt.setString(1, ruoloUtenza.getCodiceRuolo().trim());

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        if(result == null)
        {
          result = new Vector<CodeDescription>();
        }
        CodeDescription cd = new CodeDescription();
        cd.setCode(new Integer(rs.getInt("ID_TIPOLOGIA_NOTIFICA")));
        cd.setDescription(rs.getString("DESCRIZIONE"));
        result.add(cd);
      }
      
      rs.close();
      stmt.close();

    /*  SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");*/
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTipiTipologiaNotificaFromRuolo SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  public Vector<CodeDescription> getTipiTipologiaNotificaFromEntita(String tipoEntita) throws DataAccessException
  {
  
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
    
      String search = 
        "SELECT DISTINCT " +
        "       TTN.ID_TIPOLOGIA_NOTIFICA, " + 
        "       TTN.DESCRIZIONE " + 
        "FROM   DB_TIPO_TIPOLOGIA_NOTIFICA TTN, " +
        "       DB_TIPO_CATEGORIA_NOTIFICA TCN, " +
        "       DB_TIPO_ENTITA TE " +
        "WHERE  TE.CODICE_TIPO_ENTITA = ? " +
        "AND    TE.ID_TIPO_ENTITA = TCN.ID_TIPO_ENTITA " +
        "AND    TCN.ID_TIPOLOGIA_NOTIFICA = TTN.ID_TIPOLOGIA_NOTIFICA " +
        "AND    TCN.DATA_FINE_VALIDITA IS NULL " +
        "ORDER BY TTN.DESCRIZIONE ";
    
      stmt = conn.prepareStatement(search);
    
      SolmrLogger.debug(this, "Executing query: " + search);
      
      stmt.setString(1, tipoEntita);
    
      ResultSet rs = stmt.executeQuery();
    
      while (rs.next())
      {
        if(result == null)
        {
          result = new Vector<CodeDescription>();
        }
        CodeDescription cd = new CodeDescription();
        cd.setCode(new Integer(rs.getInt(1)));
        cd.setDescription(rs.getString(2));
        result.add(cd);
      }
      
    
      rs.close();
      stmt.close();
    
    
      SolmrLogger.debug(this, "getTipiTipologiaNotifica: Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTipiTipologiaNotifica - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getTipiTipologiaNotifica - Generic Exception: " + ex.getMessage());
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
            "getTipiTipologiaNotifica - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "getTipiTipologiaNotifica - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  public Vector<CodeDescription> getCodeDescriptionsFormaGiuridica(
      Long idTipologiaAzienda) throws DataAccessException, NotFoundException
  {
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT FG.ID_FORMA_GIURIDICA,FG.DESCRIZIONE "
          + "FROM DB_TIPO_FORMA_GIURIDICA FG,DB_TIPO_FG_TIPOLOGIA FGT "
          + "WHERE FGT.ID_FORMA_GIURIDICA=FG.ID_FORMA_GIURIDICA "
          + "AND FG.DATA_FINE_FORMA_GIURIDICA IS NULL "
          + "AND FGT.ID_TIPOLOGIA_AZIENDA = ? " + "ORDER BY FG.DESCRIZIONE";

      stmt = conn.prepareStatement(search);

      stmt.setLong(1, idTipologiaAzienda.longValue());

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      result = new Vector<CodeDescription>();
      while (rs.next())
      {
        CodeDescription cd = new CodeDescription();
        cd.setCode(new Integer(rs.getInt("ID_FORMA_GIURIDICA")));
        cd.setDescription(rs.getString("DESCRIZIONE"));
        result.add(cd);
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  
  public Vector<CodeDescription> getCodeDescriptionsFormaGiuridica() 
      throws DataAccessException, NotFoundException
  {
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = 
        "SELECT FG.ID_FORMA_GIURIDICA, " +
        "       FG.DESCRIZIONE," +
        "       FGT.ID_TIPOLOGIA_AZIENDA "
      + "FROM   DB_TIPO_FORMA_GIURIDICA FG," +
      "         DB_TIPO_FG_TIPOLOGIA FGT "
      + "WHERE  FG.ID_FORMA_GIURIDICA = FGT.ID_FORMA_GIURIDICA "
      + "AND    FG.DATA_FINE_FORMA_GIURIDICA IS NULL "
      + "ORDER BY FG.DESCRIZIONE";

      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      result = new Vector<CodeDescription>();
      while (rs.next())
      {
        CodeDescription cd = new CodeDescription();
        cd.setCode(new Integer(rs.getInt("ID_FORMA_GIURIDICA")));
        cd.setDescription(rs.getString("DESCRIZIONE"));
        cd.setSecondaryCode(rs.getString("ID_TIPOLOGIA_AZIENDA"));
        
        result.add(cd);
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  public TipoTipologiaAziendaVO getTipologiaAzienda(Long idTipologiaAzienda)
      throws DataAccessException, NotFoundException
  {
    TipoTipologiaAziendaVO tipoAzienda = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = 
        "SELECT FLAG_CONTROLLI_UNIVOCITA," +
        "       FLAG_AZIENDA_PROVVISORIA, " +
        "       FLAG_FORMA_ASSOCIATA " +
        "FROM   DB_TIPO_TIPOLOGIA_AZIENDA " +
        "WHERE  ID_TIPOLOGIA_AZIENDA = ? ";

      stmt = conn.prepareStatement(search);

      stmt.setLong(1, idTipologiaAzienda.longValue());

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        tipoAzienda = new TipoTipologiaAziendaVO();
        tipoAzienda.setFlagControlliUnivocita(rs
            .getString("FLAG_CONTROLLI_UNIVOCITA"));
        tipoAzienda.setFlagaziendaprovvisoria(rs
            .getString("FLAG_AZIENDA_PROVVISORIA"));
        tipoAzienda.setFlagFormaAssociata(rs
            .getString("FLAG_FORMA_ASSOCIATA"));
      }

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return tipoAzienda;
  }

  /**
   * Estrae Tutte le tipologie dell'azienda ordinate per descrizione
   * 
   * @param flagControlliUnivocita
   *          se è null non aggiunge nessun filtro, se vale true aggiunge il
   *          filtro FLAG_CONTROLLI_UNIVOCITA = S se vale false aggiunge il
   *          filtro FLAG_CONTROLLI_UNIVOCITA = N
   * @param flagAziendaProvvisoria
   *          se è null non aggiunge nessun filtro, se vale true aggiunge il
   *          filtro FLAG_AZIENDA_PROVVISORIA = S se vale false aggiunge il
   *          filtro FLAG_AZIENDA_PROVVISORIA = N
   * @return java.util.Vector
   * @throws DataAccessException
   * @throws NotFoundException
   */
  public Vector<TipoTipologiaAziendaVO> getTipiTipologiaAzienda(
      Boolean flagControlliUnivocita, Boolean flagAziendaProvvisoria)
      throws DataAccessException, NotFoundException
  {
    Vector<TipoTipologiaAziendaVO> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT ID_TIPOLOGIA_AZIENDA,DESCRIZIONE,"
          + "FLAG_CONTROLLI_UNIVOCITA,FLAG_AZIENDA_PROVVISORIA "
          + "FROM DB_TIPO_TIPOLOGIA_AZIENDA ";

      if (flagControlliUnivocita != null)
      {
        search += "WHERE FLAG_CONTROLLI_UNIVOCITA = "
            + (flagControlliUnivocita.booleanValue() ? "'S'" : "'N'") + " ";
        if (flagAziendaProvvisoria != null)
          search += "AND FLAG_AZIENDA_PROVVISORIA = "
              + (flagAziendaProvvisoria.booleanValue() ? "'S'" : "'N'") + " ";
      }
      else if (flagAziendaProvvisoria != null)
        search += "WHERE FLAG_AZIENDA_PROVVISORIA = "
            + (flagAziendaProvvisoria.booleanValue() ? "'S'" : "'N'") + " ";

      search += " ORDER BY DESCRIZIONE";

      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      result = new Vector<TipoTipologiaAziendaVO>();
      while (rs.next())
      {
        TipoTipologiaAziendaVO tipAzienda = new TipoTipologiaAziendaVO();
        tipAzienda.setIdTipologiaAzienda(rs.getString("ID_TIPOLOGIA_AZIENDA"));
        tipAzienda.setDescrizione(rs.getString("DESCRIZIONE"));
        tipAzienda.setFlagControlliUnivocita(rs
            .getString("FLAG_CONTROLLI_UNIVOCITA"));
        tipAzienda.setFlagaziendaprovvisoria(rs
            .getString("FLAG_AZIENDA_PROVVISORIA"));
        result.add(tipAzienda);
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  public Vector<CodeDescription> getCodeDescriptionsFormaGiuridica(
      String tableName, String orderBy) throws DataAccessException,
      NotFoundException
  {
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT id_"
          + tableName
          + ", "
          + "       descrizione "
          + "  FROM DB_Tipo_"
          + tableName
          + " where DATA_FINE_FORMA_GIURIDICA is null "
          + " ORDER BY "
          + (orderBy == null || orderBy.equals(SolmrConstants.CD_CODE) ? SolmrConstants.CD_CODE
              : SolmrConstants.CD_DESCRIPTION);
      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        result = new Vector<CodeDescription>();
        while (rs.next())
        {
          CodeDescription cd = new CodeDescription();

          cd.setCode(new Integer(rs.getInt(1)));
          cd.setDescription(rs.getString(2));

          result.add(cd);
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      if (result.size() == 0)
        throw new NotFoundException();

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (NotFoundException nfexc)
    {
      SolmrLogger.fatal(this, "NotFoundException: " + nfexc.getMessage());
      throw nfexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // Metodo per recuperare i codici e le descrizioni tranne dell'elemento non
  // desiderato
  public Vector<CodeDescription> getCodeDescriptionsExceptValue(
      String tableName, String orderBy, Integer valoreDaEscludere)
      throws DataAccessException, NotFoundException
  {

    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = " SELECT id_"
          + tableName
          + ", "
          + " descrizione "
          + " FROM DB_Tipo_"
          + tableName
          + " where id_"
          + tableName
          + " != "
          + valoreDaEscludere
          + " ORDER BY "
          + (orderBy == null || orderBy.equals(SolmrConstants.CD_CODE) ? SolmrConstants.CD_CODE
              : SolmrConstants.CD_DESCRIPTION);

      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        result = new Vector<CodeDescription>();
        while (rs.next())
        {
          CodeDescription cd = new CodeDescription();
          cd.setCode(new Integer(rs.getInt(1)));
          cd.setDescription(rs.getString(2));
          result.add(cd);
        }
      }
      else
      {
        throw new DataAccessException();
      }

      rs.close();
      stmt.close();

      if (result.size() == 0)
      {
        throw new NotFoundException();
      }

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger
          .fatal(this, "SQLException in getCodeDescriptionsExceptValue: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (NotFoundException nfexc)
    {
      SolmrLogger.fatal(this,
          "NotFoundException in getCodeDescriptionsExceptValue: "
              + nfexc.getMessage());
      throw nfexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in getCodeDescriptionsExceptValue: "
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
                "SQLException while closing Statement and Connection in getCodeDescriptionsExceptValue: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in getCodeDescriptionsExceptValue: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // Metodo per recuperare i codici e le descrizioni tranne dell'elemento non
  // desiderato
  public Vector<CodeDescription> getCodeDescriptionsExceptValue2(
      String tableName, String orderBy, Integer valoreDaEscludere,
      Integer valoreDaEscludere1) throws DataAccessException, NotFoundException
  {

    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = " SELECT id_"
          + tableName
          + ", "
          + " descrizione "
          + " FROM DB_Tipo_"
          + tableName
          + " where id_"
          + tableName
          + " != "
          + valoreDaEscludere
          + " and id_"
          + tableName
          + " != "
          + valoreDaEscludere1
          + " ORDER BY "
          + (orderBy == null || orderBy.equals(SolmrConstants.CD_CODE) ? SolmrConstants.CD_CODE
              : SolmrConstants.CD_DESCRIPTION);

      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        result = new Vector<CodeDescription>();
        while (rs.next())
        {
          CodeDescription cd = new CodeDescription();
          cd.setCode(new Integer(rs.getInt(1)));
          cd.setDescription(rs.getString(2));
          result.add(cd);
        }
      }
      else
      {
        throw new DataAccessException();
      }

      rs.close();
      stmt.close();

      if (result.size() == 0)
      {
        throw new NotFoundException();
      }

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger
          .fatal(this, "SQLException in getCodeDescriptionsExceptValue: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (NotFoundException nfexc)
    {
      SolmrLogger.fatal(this,
          "NotFoundException in getCodeDescriptionsExceptValue: "
              + nfexc.getMessage());
      throw nfexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in getCodeDescriptionsExceptValue: "
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
                "SQLException while closing Statement and Connection in getCodeDescriptionsExceptValue: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in getCodeDescriptionsExceptValue: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // Metodo per recuperare i codici e le descrizioni tranne dell'elemento non
  // desiderato
  // Utilizzato per la fomra fiuridica
  public Vector<CodeDescription> getCodeDescriptionsExceptValue3(
      String tableName, String orderBy, Integer valoreDaEscludere,
      Integer valoreDaEscludere1) throws DataAccessException, NotFoundException
  {

    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = " SELECT id_"
          + tableName
          + ", "
          + " descrizione "
          + " FROM DB_Tipo_"
          + tableName
          + " where id_"
          + tableName
          + " != "
          + valoreDaEscludere
          + " and id_"
          + tableName
          + " != "
          + valoreDaEscludere1
          + " and DATA_FINE_FORMA_GIURIDICA is null"
          + " ORDER BY "
          + (orderBy == null || orderBy.equals(SolmrConstants.CD_CODE) ? SolmrConstants.CD_CODE
              : SolmrConstants.CD_DESCRIPTION);

      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        result = new Vector<CodeDescription>();
        while (rs.next())
        {
          CodeDescription cd = new CodeDescription();
          cd.setCode(new Integer(rs.getInt(1)));
          cd.setDescription(rs.getString(2));
          result.add(cd);
        }
      }
      else
      {
        throw new DataAccessException();
      }

      rs.close();
      stmt.close();

      if (result.size() == 0)
      {
        throw new NotFoundException();
      }

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger
          .fatal(this, "SQLException in getCodeDescriptionsExceptValue: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (NotFoundException nfexc)
    {
      SolmrLogger.fatal(this,
          "NotFoundException in getCodeDescriptionsExceptValue: "
              + nfexc.getMessage());
      throw nfexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in getCodeDescriptionsExceptValue: "
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
                "SQLException while closing Statement and Connection in getCodeDescriptionsExceptValue: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in getCodeDescriptionsExceptValue: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  public String getDescriptionFromCode(String tableName, Integer code)
      throws DataAccessException
  {
    String description = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT descrizione " + "  FROM DB_Tipo_" + tableName
          + " " + " WHERE id_" + tableName + " = ? ";
      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      stmt.setInt(1, code.intValue());

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        while (rs.next())
        {
          description = rs.getString("descrizione");
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query.");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return description;
  }

  // Metodo per recuperare le decodifiche dei codici SIAN
  public String getDescriptionSIANFromCode(String tableName, String code)
      throws DataAccessException
  {

    SolmrLogger.debug(this, "Invocating method getDescriptionSIANFromCode");

    String description = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      SolmrLogger.debug(this,
          "Creating connection in method getDescriptionSIANFromCode");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this,
          "Created connection in method getDescriptionSIANFromCode with value: "
              + conn);

      String search = " SELECT DESCRIZIONE " + " FROM   DB_SIAN_TIPO_"
          + tableName + " WHERE  CODICE_" + tableName + " = ? ";

      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      SolmrLogger.debug(this, "Value of parameter 1: " + code);
      stmt.setString(1, code);

      ResultSet rs = stmt.executeQuery();
      SolmrLogger.debug(this, "Executed query");

      if (rs.next())
      {
        description = rs.getString(1);
      }

      SolmrLogger.debug(this,
          "Value of description in method getDescriptionSIANFromCode"
              + description);

      rs.close();
      SolmrLogger.debug(this,
          "Closed result set in method getDescriptionSIANFromCode");

      stmt.close();
      SolmrLogger.debug(this,
          "Closed statement in method getDescriptionSIANFromCode");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "SQLException in method getDescriptionSIANFromCode: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in method getDescriptionSIANFromCode: "
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
                "SQLException while closing Statement and Connection in method getDescriptionSIANFromCode: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in method getDescriptionSIANFromCode: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated method getDescriptionSIANFromCode");
    return description;
  }

  /**
   * Metodo che si occupa di estrarre tutti i gruppo controllo associati ad una
   * determinata dichiarazione di consistenza
   * 
   * @param idDichiarazioneConsistenza
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws DataAccessException
   */
  public CodeDescription[] getListTipoGruppoControlloByIdDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza, String[] orderBy)
      throws DataAccessException
  {
    SolmrLogger
        .debug(
            this,
            "Invocating getListTipoGruppoControlloByIdDichiarazioneConsistenza method in CommonDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<CodeDescription> elencoTipoGruppoControllo = new Vector<CodeDescription>();

    try
    {
      SolmrLogger
          .debug(
              this,
              "Creating db-connection in getListTipoGruppoControlloByIdDichiarazioneConsistenza method in CommonDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in getListTipoGruppoControlloByIdDichiarazioneConsistenza method in CommonDAO and it values: "
                  + conn + "\n");

      String query = " SELECT DISTINCT TGC.ID_GRUPPO_CONTROLLO, "
          + "                 TGC.DESCRIZIONE "
          + " FROM      DB_TIPO_GRUPPO_CONTROLLO TGC, "
          + "                 DB_DICHIARAZIONE_CONSISTENZA DC, "
          + "                 DB_TIPO_MOTIVO_DICHIARAZIONE TMD, "
          + "                 DB_TIPO_CONTROLLO_FASE TCF, "
          + "                 DB_TIPO_CONTROLLO TC "
          + " WHERE     DC.ID_DICHIARAZIONE_CONSISTENZA = ? "
          + " AND       DC.ID_MOTIVO_DICHIARAZIONE = TMD.ID_MOTIVO_DICHIARAZIONE "
          + " AND       TMD.ID_FASE = TCF.ID_FASE "
          + " AND       TCF.ID_CONTROLLO = TC.ID_CONTROLLO "
          + " AND       TC.ID_GRUPPO_CONTROLLO = TGC.ID_GRUPPO_CONTROLLO ";

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
              "Value of parameter 1 [ID_DICHIARAZIONE_CONSISTENZA] in getListTipoGruppoControlloByIdDichiarazioneConsistenza method in CommonDAO: "
                  + idDichiarazioneConsistenza + "\n");

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idDichiarazioneConsistenza.longValue());

      SolmrLogger.debug(this,
          "Executing getListTipoGruppoControlloByIdDichiarazioneConsistenza: "
              + query + "\n");

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        CodeDescription code = new CodeDescription();
        code.setCode(Integer.decode(rs.getString("ID_GRUPPO_CONTROLLO")));
        code.setDescription(rs.getString("DESCRIZIONE"));
        elencoTipoGruppoControllo.add(code);
      }

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger
          .error(
              this,
              "getListTipoGruppoControlloByIdDichiarazioneConsistenza in CommonDAO - SQLException: "
                  + exc + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .error(
              this,
              "getListTipoGruppoControlloByIdDichiarazioneConsistenza in CommonDAO - Generic Exception: "
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
                "getListTipoGruppoControlloByIdDichiarazioneConsistenza in CommonDAO - SQLException while closing Statement and Connection: "
                    + exc + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getListTipoGruppoControlloByIdDichiarazioneConsistenza in CommonDAO - Generic Exception while closing Statement and Connection: "
                    + ex + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger
        .debug(
            this,
            "Invocated getListTipoGruppoControlloByIdDichiarazioneConsistenza method in CommonDAO\n");
    if (elencoTipoGruppoControllo.size() == 0)
    {
      return (CodeDescription[]) elencoTipoGruppoControllo
          .toArray(new CodeDescription[0]);
    }
    else
    {
      return (CodeDescription[]) elencoTipoGruppoControllo
          .toArray(new CodeDescription[elencoTipoGruppoControllo.size()]);
    }
  }

  // Metodo per recuperare le decodifiche dei codici SIAN
  public CodeDescription getDescrizioneProduzioneFromCode(String codProd)
      throws DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating method getDescrizioneProduzioneFromCode");

    CodeDescription result = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      SolmrLogger.debug(this,
          "Creating connection in method getDescrizioneProduzioneFromCode");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this,
          "Created connection in method getDescrizioneProduzioneFromCode with value: "
              + conn);

      String search = " SELECT TTP.DESCRIZIONE,TTP.ID_TIPO_PRODUZIONE "
          + " FROM DB_TIPO_TIPO_PRODUZIONE TTP " + " WHERE TTP.CODICE = ? ";

      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      SolmrLogger.debug(this, "Value of parameter 1: " + codProd);

      stmt.setString(1, codProd);

      ResultSet rs = stmt.executeQuery();
      SolmrLogger.debug(this, "Executed query");

      if (rs.next())
      {
        result = new CodeDescription();
        result.setCode(new Integer(rs.getInt("ID_TIPO_PRODUZIONE")));
        result.setDescription(rs.getString("DESCRIZIONE"));
        SolmrLogger.debug(this,
            "Value of ID_TIPO_PRODUZIONE in method getDescrizioneProduzioneFromCode "
                + result.getCode());
        SolmrLogger.debug(this,
            "Value of DESCRIZIONE in method getDescrizioneProduzioneFromCode "
                + result.getDescription());
      }

      rs.close();
      SolmrLogger.debug(this,
          "Closed result set in method getDescrizioneProduzioneFromCode");

      stmt.close();
      SolmrLogger.debug(this,
          "Closed statement in method getDescrizioneProduzioneFromCode");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "SQLException in method getDescrizioneProduzioneFromCode: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in method getDescrizioneProduzioneFromCode: "
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
                "SQLException while closing Statement and Connection in method getDescrizioneProduzioneFromCode: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in method getDescrizioneProduzioneFromCode: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger
        .debug(this, "Invocated method getDescrizioneProduzioneFromCode");
    return result;
  }

  // Dato che ad una specie di teramo possono corrispondere più speci in
  // anagrafe, le memorizzo
  // in un array
  public SianBaseCodeDescription[] getSpeciAnagrafeFromTeramo(String codSpecie)
      throws DataAccessException
  {
    SolmrLogger.debug(this, "Invocating method getSpeciAnagrafeFromTeramo");

    Vector<SianBaseCodeDescription> result = new Vector<SianBaseCodeDescription>();
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      SolmrLogger.debug(this,
          "Creating connection in method getSpeciAnagrafeFromTeramo");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this,
          "Created connection in method getSpeciAnagrafeFromTeramo with value: "
              + conn);

      String search = " SELECT TSA.DESCRIZIONE,TSA.ID_SPECIE_ANIMALE "
          + " FROM  DB_TIPO_SPECIE_ANIMALE TSA,DB_R_SPECIE_AN_SIAN_SIAP SSS "
          + " WHERE SSS.FLAG_RELAZIONE_ATTIVA='S' "
          + " AND TSA.ID_SPECIE_ANIMALE=SSS.ID_SPECIE_ANIMALE "
          + " AND TSA.DATA_FINE_VALIDITA IS NULL "
          + " AND SSS.CODICE_SPECIE=? " + " ORDER BY TSA.DESCRIZIONE";

      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      SolmrLogger.debug(this, "Value of parameter 1: " + codSpecie);

      stmt.setString(1, codSpecie);

      ResultSet rs = stmt.executeQuery();
      SolmrLogger.debug(this, "Executed query");

      while (rs.next())
      {
        SianBaseCodeDescription base = new SianBaseCodeDescription();
        base.setCode(rs.getLong("ID_SPECIE_ANIMALE"));
        base.setDescription(rs.getString("DESCRIZIONE"));
        result.add(base);
      }

      rs.close();
      SolmrLogger.debug(this,
          "Closed result set in method getSpeciAnagrafeFromTeramo");

      stmt.close();
      SolmrLogger.debug(this,
          "Closed statement in method getSpeciAnagrafeFromTeramo");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "SQLException in method getSpeciAnagrafeFromTeramo: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in method getSpeciAnagrafeFromTeramo: "
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
                "SQLException while closing Statement and Connection in method getSpeciAnagrafeFromTeramo: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in method getSpeciAnagrafeFromTeramo: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated method getSpeciAnagrafeFromTeramo");
    return result.size() == 0 ? null : (SianBaseCodeDescription[]) result
        .toArray(new SianBaseCodeDescription[0]);
  }

  /*public Vector<StringcodeDescription> getTipiDitta()
      throws DataAccessException
  {
    Vector<StringcodeDescription> result = null;
    StringcodeDescription scd = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT DISTINCT TIPO_DITTA  " + "  FROM DB_DITTA_UMA ";
      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        result = new Vector<StringcodeDescription>();
        while (rs.next())
        {
          scd = new StringcodeDescription();
          String codice = rs.getString("TIPO_DITTA");
          scd.setCode(codice);
          if (codice.equals("U"))
            scd.setDescription("UMA");
          else if (codice.equals("L"))
            scd.setDescription("Locataria");
          else
            scd.setDescription("");
          result.addElement(scd);
        }
      }
      else
        throw new DataAccessException();

      if (result != null)
      {
        scd = new StringcodeDescription("U", "UMA");
        if (!result.contains(scd))
          result.addElement(scd);
        scd = new StringcodeDescription("L", "Locataria");
        if (!result.contains(scd))
          result.addElement(scd);
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query.");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }*/

  public CodeDescription getAttivitaFromCode(String tableName, Integer code)
      throws DataAccessException
  {
    CodeDescription cd = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT codice, descrizione " + "  FROM DB_Tipo_"
          + tableName + " " + " WHERE id_" + tableName + " = ? ";
      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      stmt.setInt(1, code.intValue());

      ResultSet rs = stmt.executeQuery();

      if (rs != null && rs.next())
      {
        cd = new CodeDescription();
        cd.setDescription(rs.getString("descrizione"));
        cd.setCode(code);
        cd.setSecondaryCode(rs.getString("codice"));
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query.");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return cd;
  }

  // Metodo per recuperare l'elenco dei comuni a partire dalla provincia(anche
  // parziale) e dalla descrizione
  // anche parziale del comune
  public Vector<ComuneVO> getComuniLikeByProvAndCom(String provincia,
      String comune) throws DataAccessException, NotFoundException,
      DataControlException
  {

    Vector<ComuneVO> elencoComuni = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    String queryProvincia = "";
    String queryComune = "";

    int indiceStatement = 0;

    if (provincia != null && !provincia.equals(""))
    {
      queryProvincia = " c.ISTAT_PROVINCIA in (select p.ISTAT_PROVINCIA from provincia where p.SIGLA_PROVINCIA like ?) ";
    }
    else
    {
      queryProvincia = " p.ISTAT_PROVINCIA = c.ISTAT_PROVINCIA ";
    }
    if (comune != null && !comune.equals(""))
    {
      queryComune = "and c.DESCOM  like ? ";
    }
    try
    {
      conn = getDatasource().getConnection();

      String query = "select p.descrizione, c.descom, c.ISTAT_COMUNE, c.cap, p.sigla_provincia, c.istat_provincia, c.zonaalt, c.codfisc from comune c, provincia p where "
          + queryProvincia
          + queryComune
          + "and flag_estero = ? order by c.descom ";

      stmt = conn.prepareStatement(query);

      if (provincia != null && !provincia.equals(""))
      {
        if (provincia.length() == 2)
        {
          stmt.setString(++indiceStatement, provincia.toUpperCase() + "%");
        }
        else
        {
          stmt.setString(++indiceStatement, provincia);
        }
      }
      if (comune != null && !comune.equals(""))
      {
        stmt.setString(++indiceStatement, comune.toUpperCase() + "%");
      }
      stmt.setString(++indiceStatement, SolmrConstants.FLAG_N);
      SolmrLogger.debug(this, "Executing query: " + query);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        elencoComuni = new Vector<ComuneVO>();
        while (rs.next())
        {
          ComuneVO comuneVO = new ComuneVO();
          comuneVO.setDescrProv(rs.getString(1));
          comuneVO.setDescom(rs.getString(2));
          comuneVO.setIstatComune(rs.getString(3));
          comuneVO.setCap(rs.getString(4));
          comuneVO.setSiglaProv(rs.getString(5));
          comuneVO.setIstatProvincia(rs.getString(6));
          comuneVO.setZonaAlt(new Long(rs.getLong(7)));
          comuneVO.setCodfisc(rs.getString(8));
          elencoComuni.add(comuneVO);
        }
      }
      if (elencoComuni.size() == 0)
      {
        throw new DataControlException(AnagErrors.RICERCACOMUNI);
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found " + elencoComuni.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataControlException dce)
    {
      SolmrLogger.fatal(this, "DataControlException: " + dce.getMessage());
      throw new DataControlException(dce.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoComuni;
  }

  // Metodo per recuperare l'elenco dei comuni non estinti a partire dalla
  // provincia(anche parziale) e dalla descrizione
  // anche parziale del comune
  public Vector<ComuneVO> getComuniNonEstintiLikeByProvAndCom(String provincia,
      String comune, String flagEstero) throws DataAccessException,
      NotFoundException, DataControlException
  {

    Vector<ComuneVO> elencoComuni = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    String queryProvincia = "";
    String queryComune = "";

    int indiceStatement = 0;

    if (provincia != null && !provincia.equals(""))
    {
      queryProvincia = " c.ISTAT_PROVINCIA in (select p.ISTAT_PROVINCIA from provincia where p.SIGLA_PROVINCIA like ?) ";
    }
    else
    {
      queryProvincia = " p.ISTAT_PROVINCIA = c.ISTAT_PROVINCIA ";
    }
    if (comune != null && !comune.equals(""))
    {
      queryComune += "and c.DESCOM  like ? ";
    }
    if (Validator.isNotEmpty(flagEstero))
    {
      queryComune += "and flag_estero = ?  ";
    }
    try
    {
      conn = getDatasource().getConnection();

      String query = "select p.descrizione, c.descom, c.ISTAT_COMUNE, c.cap, p.sigla_provincia, c.istat_provincia, c.zonaalt, c.codfisc from comune c, provincia p where "
          + queryProvincia
          + queryComune
          + "and flag_estinto = ? order by c.descom ";

      stmt = conn.prepareStatement(query);

      if (provincia != null && !provincia.equals(""))
      {
        if (provincia.length() == 2)
        {
          stmt.setString(++indiceStatement, provincia.toUpperCase() + "%");
        }
        else
        {
          stmt.setString(++indiceStatement, provincia);
        }
      }
      if (comune != null && !comune.equals(""))
      {
        stmt.setString(++indiceStatement, comune.toUpperCase() + "%");
      }
      if (Validator.isNotEmpty(flagEstero))
      {
        stmt.setString(++indiceStatement, flagEstero);
      }
      stmt.setString(++indiceStatement, SolmrConstants.FLAG_N);
      SolmrLogger.debug(this,
          "Executing query getComuniNonEstintiLikeByProvAndCom: " + query);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        elencoComuni = new Vector<ComuneVO>();
        while (rs.next())
        {
          ComuneVO comuneVO = new ComuneVO();
          comuneVO.setDescrProv(rs.getString(1));
          comuneVO.setDescom(rs.getString(2));
          comuneVO.setIstatComune(rs.getString(3));
          comuneVO.setCap(rs.getString(4));
          comuneVO.setSiglaProv(rs.getString(5));
          comuneVO.setIstatProvincia(rs.getString(6));
          comuneVO.setZonaAlt(new Long(rs.getLong(7)));
          comuneVO.setCodfisc(rs.getString(8));
          elencoComuni.add(comuneVO);
        }
      }
      if (elencoComuni.size() == 0)
      {
        throw new DataControlException(AnagErrors.RICERCACOMUNI);
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this,
          "Executed query getComuniNonEstintiLikeByProvAndCom - Found "
              + elencoComuni.size() + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "SQLException in getComuniNonEstintiLikeByProvAndCom: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataControlException dce)
    {
      SolmrLogger.fatal(this,
          "DataControlException in getComuniNonEstintiLikeByProvAndCom: "
              + dce.getMessage());
      throw new DataControlException(dce.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in getComuniNonEstintiLikeByProvAndCom: "
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
                "SQLException while closing Statement and Connection in getComuniNonEstintiLikeByProvAndCom: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in getComuniNonEstintiLikeByProvAndCom: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoComuni;
  }

  // Metodo per recuperare l'elenco dei comuni a partire dalla descrizione
  // del comune
  public Vector<ComuneVO> getComuniByDescCom(String comune)
      throws DataAccessException, NotFoundException, DataControlException
  {

    Vector<ComuneVO> elencoComuni = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    String queryProvincia = "";
    String queryComune = "";

    int indiceStatement = 0;
    queryProvincia = " p.ISTAT_PROVINCIA = c.ISTAT_PROVINCIA ";
    if (comune != null && !comune.equals(""))
    {
      queryComune = "and c.DESCOM  = ?";
    }
    try
    {
      conn = getDatasource().getConnection();

      String query = "select p.descrizione, c.descom, c.ISTAT_COMUNE, c.cap, p.sigla_provincia, c.istat_provincia, c.zonaalt, c.codfisc, c.flag_estinto from comune c, provincia p where "
          + queryProvincia
          + queryComune
          + "and flag_estero = ? order by c.descom ";

      stmt = conn.prepareStatement(query);

      if (comune != null && !comune.equals(""))
      {
        stmt.setString(++indiceStatement, comune.toUpperCase());
      }
      stmt.setString(++indiceStatement, SolmrConstants.FLAG_N);
      SolmrLogger.debug(this, "Executing query getComuniByDescCom: " + query);
      SolmrLogger.debug(this, "-- comune ="+comune.toUpperCase());

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        elencoComuni = new Vector<ComuneVO>();
        while (rs.next())
        {
          ComuneVO comuneVO = new ComuneVO();
          comuneVO.setDescrProv(rs.getString(1));
          comuneVO.setDescom(rs.getString(2));
          comuneVO.setIstatComune(rs.getString(3));
          comuneVO.setCap(rs.getString(4));
          comuneVO.setSiglaProv(rs.getString(5));
          comuneVO.setIstatProvincia(rs.getString(6));
          comuneVO.setZonaAlt(new Long(rs.getLong(7)));
          comuneVO.setCodfisc(rs.getString(8));
          comuneVO.setFlagEstinto(rs.getString(9));
          elencoComuni.add(comuneVO);
        }
      }
      if (elencoComuni.size() == 0)
      {
        throw new DataControlException(AnagErrors.RICERCACOMUNI);
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found " + elencoComuni.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataControlException dce)
    {
      SolmrLogger.fatal(this, "DataControlException: " + dce.getMessage());
      throw new DataControlException(dce.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoComuni;
  }

  /**
   * Metodo che mi restituisce l'elenco degli stati esteri in relazione ai
   * parametri
   * 
   * @param statoEstero
   * @param estinto
   * @param flagCatastoAttivo
   * @return java.util.Vector
   * @throws DataAccessException
   */
  public Vector<ComuneVO> ricercaStatoEstero(String statoEstero,
      String estinto, String flagCatastoAttivo) throws DataAccessException
  {
    Vector<ComuneVO> elencoStati = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    String query2 = "";

    if (statoEstero != null && !statoEstero.equals(""))
    {
      query2 = "and descom like ?";
    }

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT DESCOM, " + "        ISTAT_COMUNE "
          + " FROM   COMUNE " + " WHERE  FLAG_ESTERO = ? " + query2;
      if (Validator.isNotEmpty(estinto))
      {
        query += " AND      FLAG_ESTINTO = ? ";
      }
      if (Validator.isNotEmpty(flagCatastoAttivo))
      {
        query += " AND      FLAG_CATASTO_ATTIVO = ? ";
      }
      query += " ORDER BY DESCOM";

      stmt = conn.prepareStatement(query);

      stmt.setString(1, SolmrConstants.FLAG_S);
      int indice = 1;
      if (statoEstero != null && !statoEstero.equals(""))
      {
        stmt.setString(++indice, statoEstero.toUpperCase() + "%");
      }
      if (Validator.isNotEmpty(estinto))
      {
        if (estinto.equalsIgnoreCase(SolmrConstants.FLAG_S))
        {
          stmt.setString(++indice, SolmrConstants.FLAG_S);
        }
        else
        {
          stmt.setString(++indice, SolmrConstants.FLAG_N);
        }
      }
      if (Validator.isNotEmpty(flagCatastoAttivo))
      {
        stmt.setString(++indice, flagCatastoAttivo);
      }
      SolmrLogger.debug(this, "Executing query: " + query);
      ResultSet rs = stmt.executeQuery();
      elencoStati = new Vector<ComuneVO>();
      while (rs.next())
      {
        ComuneVO comuneVO = new ComuneVO();
        comuneVO.setDescom(rs.getString(1));
        comuneVO.setIstatComune(rs.getString(2));
        elencoStati.add(comuneVO);
      }
      rs.close();
      stmt.close();
      SolmrLogger.debug(this, "Executed query - Found " + elencoStati.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this, "Generic Exception: " + ex.getMessage());
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
        SolmrLogger.error(this,
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.error(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoStati;
  }

  // Metodo per recuperare il codice istat a partire dalla descrizione del
  // comune
  public String ricercaCodiceComune(String descrizioneComune,
      String siglaProvincia) throws DataAccessException, NotFoundException,
      DataControlException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String codiceIstat = null;

    try
    {
      conn = getDatasource().getConnection();
      String query2 = "";
      if (siglaProvincia != null && !siglaProvincia.equals(""))
      {
        query2 = " and p.sigla_provincia = ? and c.ISTAT_PROVINCIA = p.ISTAT_PROVINCIA";
      }
      String query = " select istat_comune from comune c, provincia p where c.descom = ? "
          + query2;

      stmt = conn.prepareStatement(query);
      stmt.setString(1, descrizioneComune.toUpperCase());
      if (siglaProvincia != null && !siglaProvincia.equals(""))
      {
        stmt.setString(2, siglaProvincia.toUpperCase());
      }
      SolmrLogger.debug(this, "Executing query: " + query);

      rs = stmt.executeQuery();

      if (!rs.next())
      {
        // Il messaggio che mando è quello relativo al fatto che non è stato
        // trovato un codice
        // istat relativo alla descrizione del comune quindi il comune inserito
        // dall'utente è inesistente
        throw new DataControlException(AnagErrors.CODICEISTATCOMUNE);
      }
      else
      {
        codiceIstat = rs.getString(1);
      }
      rs.close();
      stmt.close();
    }
    catch (DataControlException dce)
    {
      SolmrLogger.fatal(this, "DataControlException: " + dce.getMessage());
      throw new DataControlException(dce.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this,
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return codiceIstat;
  }

  // Metodo per recuperare il codice fiscale del comune a partire dalla
  // descrizione del comune
  public String ricercaCodiceFiscaleComune(String descrizioneComune,
      String siglaProvincia) throws DataAccessException, DataControlException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String codiceFiscaleComune = null;

    try
    {

      conn = getDatasource().getConnection();

      String query2 = "";

      if (siglaProvincia != null && !siglaProvincia.equals(""))
      {
        query2 = " and p.sigla_provincia = ? ";
      }

      String query = " select codfisc from comune c, provincia p where c.descom = ? "
          + query2;

      stmt = conn.prepareStatement(query);
      stmt.setString(1, descrizioneComune.toUpperCase());
      if (siglaProvincia != null && !siglaProvincia.equals(""))
      {
        stmt.setString(2, siglaProvincia.toUpperCase());
      }
      SolmrLogger.debug(this, "Executing query: " + query);

      rs = stmt.executeQuery();

      if (!rs.next())
      {
        // Il messaggio che mando è quello relativo al fatto che non è stato
        // trovato un codice
        // istat relativo alla descrizione del comune quindi il comune inserito
        // dall'utente è inesistente
        throw new DataControlException(AnagErrors.CODICEISTATCOMUNE);
      }
      else
      {
        codiceFiscaleComune = rs.getString(1);
      }
      rs.close();
      stmt.close();
    }
    catch (DataControlException dce)
    {
      SolmrLogger.fatal(this,
          "DataControlException in ricercaCodiceFiscaleComune : "
              + dce.getMessage());
      throw new DataControlException(dce.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(this, "Generic Exception in ricercaCodiceFiscaleComune: "
              + ex.getMessage());
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
            .fatal(
                this,
                "SQLException while closing Statement and Connection in ricercaCodiceFiscaleComune: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in ricercaCodiceFiscaleComune: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return codiceFiscaleComune;
  }

  public ComuneVO getComuneByISTAT(String istat) throws DataAccessException,
      NotFoundException
  {
    ComuneVO result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT c.istat_provincia, " + " c.descom, " + " c.cap, "
          + " c.flag_estero, " + " c.codfisc, " + " c.montana, " + " c.ussl, "
          + " c.zonaalt, " + " c.zonaalts1, " + " c.zonaalts2, "
          + " c.regagri, " + " c.prov_old, " + " c.comune_old, "
          + " c.usl_old, " + " c.prov_new, " + " c.prefisso, "
          + " p.SIGLA_PROVINCIA, " + " P.DESCRIZIONE as descProvincia "
          + " FROM comune c,provincia p" + " WHERE istat_comune = ? and "
          + "C.ISTAT_PROVINCIA=P.ISTAT_PROVINCIA(+)";
      stmt = conn.prepareStatement(search);
      stmt.setString(1, istat);

      SolmrLogger.debug(this, " -- istat_comune ="+istat);
      SolmrLogger.debug(this, "Executing query getComuneByISTAT =" + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        result = new ComuneVO();
        while (rs.next())
        {
          result.setCap(rs.getString("cap"));
          result.setComuneOld(rs.getString("comune_old"));
          result.setDescom(rs.getString("descom"));
          result.setFlagEstero(rs.getString("flag_estero"));
          result.setIstatComune(istat);
          result.setIstatProvincia(rs.getString("istat_provincia"));
          result.setPrefisso(rs.getString("prefisso"));
          result.setProvOld(rs.getString("prov_old"));
          result.setRegAgri(rs.getString("regagri"));
          result.setUslOld(rs.getString("usl_old"));
          result.setZonaAlt(new Long(rs.getLong("zonaalt")));
          result.setZonaAltS1(rs.getString("zonaalts1"));
          result.setZonaAltS2(rs.getString("zonaalts2"));
          result.setProvNew(rs.getString("prov_new"));
          result.setCodfisc(rs.getString("codfisc"));
          result.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
          result.setDescrProv(rs.getString("descProvincia"));
        }
      }
      
      rs.close();
      stmt.close();


      SolmrLogger.debug(this, "Executed query - Found " + result + ".");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  public Vector<CodeDescription> getAttivitaLike(String code,
      String description, String tableName) throws DataAccessException,
      NotFoundException
  {
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
      Vector<String> daParameters = new Vector<String>();

      String search = "SELECT id_" + tableName + ", " + " codice, "
          + " descrizione " + " FROM DB_Tipo_" + tableName;
      if (code != null && code.trim().length() > 0)
      {
        search += " WHERE codice LIKE ? ";
        daParameters.add(code.trim() + "%");
      }
      if (description != null && description.trim().length() > 0)
      {
        if (code != null && code.trim().length() > 0)
          search += " AND ";
        else
          search += " WHERE ";
        search += " UPPER(descrizione) LIKE ? ";
        daParameters.add("%" + description.trim().toUpperCase() + "%");
      }
      search += " ORDER BY 1";
      stmt = conn.prepareStatement(search);

      Iterator<String> iter = daParameters.iterator();
      int i = 0;
      while (iter.hasNext())
      {
        String par = (String) iter.next();
        stmt.setString(++i, par);
      }

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        result = new Vector<CodeDescription>();
        while (rs.next())
        {
          CodeDescription cd = new CodeDescription();
          cd.setCode(new Integer(rs.getInt(1)));
          cd.setSecondaryCode(rs.getString(2));
          cd.setDescription(rs.getString(3));
          result.add(cd);
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      if (result.size() == 0)
        throw new DataControlException(AnagErrors.SCELTA_ATTIVITA);

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  public ComuneVO getComuneByCUAA(String cuaa) throws DataAccessException,
      NotFoundException
  {
    SolmrLogger.debug(this, " Invocating method getComuneByCUAA in CommonDAO");
    ComuneVO result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      SolmrLogger.debug(this,
          " Creating db connection in method getComuneByCUAA in CommonDAO");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this,
          " Created db connection in method getComuneByCUAA in CommonDAO and it values: "
              + conn);

      String search = "SELECT COMUNE.ISTAT_PROVINCIA, COMUNE.DESCOM,  "
          + "COMUNE.CAP,  COMUNE.FLAG_ESTERO, COMUNE.CODFISC, "
          + "COMUNE.ZONAALT, COMUNE.ZONAALTS1, "
          + "COMUNE.ZONAALTS2, COMUNE.REGAGRI, "
          + "COMUNE.PROV_OLD, COMUNE.COMUNE_OLD, COMUNE.USL_OLD, "
          + "COMUNE.PROV_NEW, COMUNE.PREFISSO, "
          + "PROVINCIA.SIGLA_PROVINCIA,PROVINCIA.DESCRIZIONE, COMUNE.FLAG_ESTERO,COMUNE.ISTAT_COMUNE "
          + "FROM COMUNE,PROVINCIA " + "WHERE COMUNE.CODFISC=? AND "
          + "COMUNE.ISTAT_PROVINCIA=PROVINCIA.ISTAT_PROVINCIA";
      stmt = conn.prepareStatement(search);
      SolmrLogger.debug(this,
          " Value of parameter cuaa in method getComuneByCUAA in CommonDAO: "
              + cuaa);
      stmt.setString(1, cuaa);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        result = new ComuneVO();
        result.setIstatProvincia(rs.getString(1));
        result.setDescom(rs.getString(2));
        result.setCap(rs.getString(3));
        result.setFlagEstero(rs.getString(4));
        result.setCodfisc(rs.getString(5));
        result.setZonaAlt(new Long(rs.getLong(6)));
        result.setZonaAltS1(rs.getString(7));
        result.setZonaAltS2(rs.getString(8));
        result.setRegAgri(rs.getString(9));
        result.setProvOld(rs.getString(10));
        result.setComuneOld(rs.getString(11));
        result.setUslOld(rs.getString(12));
        result.setProvNew(rs.getString(13));
        result.setPrefisso(rs.getString(14));
        result.setSiglaProv(rs.getString(15));
        result.setDescrProv(rs.getString(16));
        result.setIstatComune(rs.getString("ISTAT_COMUNE"));
      }
      rs.close();
      stmt.close();

      if (result == null)
        throw new NotFoundException();

      SolmrLogger.debug(this,
          "Executed query in method getComuneByCUAA in CommonDAO - Found "
              + result + ".");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (NotFoundException nfexc)
    {
      SolmrLogger.fatal(this, "NotFoundException: " + nfexc.getMessage());
      throw nfexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated method getComuneByCUAA in CommonDAO");
    return result;
  }

  // Metodo per recuperare il comune di nascita a partire dal codice fiscale
  public String getComuneFromCF(String codiceFiscale)
      throws DataAccessException, NotFoundException
  {

    String comune = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {

      conn = getDatasource().getConnection();

      String query = "select descom from comune where codfisc = ? ";

      stmt = conn.prepareStatement(query);
      stmt.setString(1, codiceFiscale.substring(11, 15).toUpperCase());

      SolmrLogger.debug(this, "Executing query: " + query);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        if (rs.next())
        {
          comune = rs.getString(1);
        }
        else
        {
          throw new NotFoundException();
        }
      }
      else
      {
        throw new DataAccessException();
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found " + comune + ".");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in getComuneFromCF: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null in getComuneFromCF");
      throw daexc;
    }
    catch (NotFoundException nfexc)
    {
      SolmrLogger.fatal(this, "NotFoundException in getComuneFromCF: "
          + nfexc.getMessage());
      throw nfexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in getComuneFromCF: "
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
            "SQLException while closing Statement and Connection in getComuneFromCF: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return comune;
  }

  // Metodo per recuperare l'id dell'attività OTE partendo dal codice e dalla
  // descrizione, anche parziale
  public Long ricercaIdAttivitaOTE(String codice, String descrizione,
      boolean forPopup) throws DataAccessException, SolmrException
  {

    Long idAttivitaOTE = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {

      conn = getDatasource().getConnection();

      String query2 = "";

      if (descrizione != null && !descrizione.equals(""))
      {
        if (forPopup)
        {
          query2 = "and descrizione like ? ";
        }
        else
        {
          query2 = "and upper(descrizione) = ? ";
        }
      }

      String query = "select id_attivita_ote from db_tipo_attivita_ote where codice  = ? "
          + query2;

      stmt = conn.prepareStatement(query);
      stmt.setString(1, codice);
      if (descrizione != null && !descrizione.equals("") && forPopup)
      {
        stmt.setString(2, descrizione + "%");
      }
      else if (descrizione != null && !descrizione.equals("") && !forPopup)
      {
        stmt.setString(2, descrizione.toUpperCase());
      }
      SolmrLogger.debug(this, "Executing query: " + query);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        if (rs.next())
        {
          idAttivitaOTE = new Long(rs.getLong(1));
        }
        else
        {
          throw new SolmrException(AnagErrors.NESSUNA_OTE_TROVATA);
        }
      }
      else
      {
        throw new DataAccessException();
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in ricercaIdAttivitaOTE: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null in ricercaIdAttivitaOTE");
      throw daexc;
    }
    catch (SolmrException se)
    {
      SolmrLogger.fatal(this, "SolmrException in ricercaIdAttivitaOTE");
      throw se;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in ricercaIdAttivitaOTE: "
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
                "SQLException while closing Statement and Connection in ricercaIdAttivitaATECO: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return idAttivitaOTE;
  }

  // Metodo per recuperare l'id dell'attività OTE partendo dal codice e dalla
  // descrizione
  public Long ricercaIdAttivitaATECO(String codice, String descrizione,
      boolean forPopup) throws DataAccessException, SolmrException
  {

    Long idAttivitaATECO = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {

      conn = getDatasource().getConnection();

      String query2 = "";

      if (descrizione != null && !descrizione.equals(""))
      {
        if (forPopup)
        {
          query2 = "and descrizione like ? ";
        }
        else
        {
          query2 = "and upper(descrizione) = ? ";
        }
      }

      String query = "select id_attivita_ateco from db_tipo_attivita_ateco where codice  = ? "
          + query2 + " AND DATA_FINE_VALIDITA IS NULL ";

      stmt = conn.prepareStatement(query);
      stmt.setString(1, codice);
      if (descrizione != null && !descrizione.equals("") && forPopup)
      {
        stmt.setString(2, descrizione + "%");
      }
      else if (descrizione != null && !descrizione.equals("") && !forPopup)
      {
        stmt.setString(2, descrizione.toUpperCase());
      }

      SolmrLogger.debug(this, "Executing query ricercaIdAttivitaATECO: "
          + query);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        if (rs.next())
        {
          idAttivitaATECO = new Long(rs.getLong(1));
        }
        else
        {
          throw new SolmrException(AnagErrors.NESSUNA_ATECO_TROVATA);
        }
      }
      else
      {
        throw new DataAccessException();
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in ricercaIdAttivitaATECO: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null in ricercaIdAttivitaATECO");
      throw daexc;
    }
    catch (SolmrException se)
    {
      SolmrLogger.fatal(this, "SolmrException in ricercaIdAttivitaATECO");
      throw se;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in ricercaIdAttivitaATECO: "
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
                "SQLException while closing Statement and Connection in ricercaIdAttivitaATECO: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in ricercaIdAttivitaATECO: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return idAttivitaATECO;
  }

  // Metodo per recuperare l'id dell'attività Ateco e la descrizionme partendo
  // dal codice
  public CodeDescription getAttivitaATECObyCode(String codiceAteco)
      throws DataAccessException
  {

    CodeDescription attivitaATECO = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet resultSet = null;

    try
    {

      conn = getDatasource().getConnection();

      String query = "SELECT ID_ATTIVITA_ATECO,DESCRIZIONE FROM DB_TIPO_ATTIVITA_ATECO "
          + " WHERE CODICE=? AND DATA_FINE_VALIDITA IS NULL ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "getAttivitaATECObyCode Executing query: "
          + query);
      SolmrLogger.debug(this, "getAttivitaATECObyCode codiceAteco: "
          + codiceAteco);

      stmt.setString(1, codiceAteco);

      resultSet = stmt.executeQuery();

      if (resultSet.next())
      {
        attivitaATECO = new CodeDescription();
        attivitaATECO
            .setCode(new Integer(resultSet.getInt("ID_ATTIVITA_ATECO")));
        attivitaATECO.setDescription(resultSet.getString("DESCRIZIONE"));
      }
      resultSet.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in getAttivitaATECObyCode: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in getAttivitaATECObyCode: "
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
                "SQLException while closing Statement and Connection in getAttivitaATECObyCode: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in getAttivitaATECObyCode: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return attivitaATECO;
  }

  public CodeDescription[] getAttivitaATECOById(Long[] idAteco)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<CodeDescription> result = new Vector<CodeDescription>();

    try
    {
      SolmrLogger.debug(this, "[CommonDAO::getAttivitaATECOById] BEGIN.");

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */

      queryBuf = new StringBuffer();
      queryBuf
          .append(" SELECT ID_ATTIVITA_ATECO, CODICE, DESCRIZIONE "
              + " FROM  DB_TIPO_ATTIVITA_ATECO "
              + " WHERE ID_ATTIVITA_ATECO IN (?");

      int atecoDim = idAteco.length;
      int atecoCount = 1;
      for (int i = 1; i < atecoDim; i++)
        if (idAteco[i] != null)
        {
          queryBuf.append(",?");
          atecoCount++;
        }
      queryBuf.append(")");

      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[CommonDAO::getAttivitaATECOById] Query="
            + query);
      }
      stmt = conn.prepareStatement(query);

      int i = 0;
      for (int k = 0; k < atecoCount; k++)
        stmt.setLong(++i, idAteco[k]);

      ResultSet resultSet = stmt.executeQuery();

      while (resultSet.next())
      {
        CodeDescription cd = new CodeDescription();

        cd.setCode(new Integer(resultSet.getInt("ID_ATTIVITA_ATECO")));
        cd.setSecondaryCode(resultSet.getString("CODICE"));
        cd.setDescription(resultSet.getString("DESCRIZIONE"));

        result.add(cd);
      }
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[] { new Variabile("query", query),
          new Variabile("queryBuf", queryBuf), new Variabile("result", result) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[] { new Parametro("idAtecoOld",
          idAteco) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[CommonDAO::getAttivitaATECOById] ", t,
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
      SolmrLogger.debug(this, "[CommonDAO::getAttivitaATECOById] END.");
    }
    return result.size() == 0 ? null : (CodeDescription[]) result
        .toArray(new CodeDescription[0]);
  }

  /**
   * Metodo per recuperare l'id dell'attività Ateco e la descrizione partendo
   * dal codice e filtrando per db_parametro = 'CATE'
   * 
   * @param codiceAteco
   * @return
   * @throws DataAccessException
   */
  public CodeDescription getAttivitaATECObyCodeParametroCATE(String codiceAteco)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    CodeDescription attivitaATECO = null;
    String query = null;
    StringBuffer queryBuf = null;

    try
    {
      SolmrLogger.debug(this,
          "[CommonDAO::getAttivitaATECObyCodeParametroCATE] BEGIN.");

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */

      queryBuf = new StringBuffer();
      queryBuf
          .append("SELECT ID_ATTIVITA_ATECO,DESCRIZIONE,CODICE FROM DB_TIPO_ATTIVITA_ATECO "
              + " WHERE CODICE=? "
              + " AND ID_TIPO_CODIFICA_ATECO = "
              + "(SELECT VALORE FROM DB_PARAMETRO WHERE ID_PARAMETRO like '"
              + SolmrConstants.PARAMETRO_CATE + "')");

      query = queryBuf.toString();
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "getAttivitaATECObyCode Executing query: "
          + query);
      SolmrLogger.debug(this, "getAttivitaATECObyCode codiceAteco: "
          + codiceAteco.trim());

      stmt.setString(1, codiceAteco.trim());

      ResultSet resultSet = stmt.executeQuery();

      if (resultSet.next())
      {
        attivitaATECO = new CodeDescription();
        attivitaATECO.setCode(new Integer(resultSet.getInt("ID_ATTIVITA_ATECO")));
        attivitaATECO.setDescription(resultSet.getString("DESCRIZIONE"));
        attivitaATECO.setSecondaryCode(resultSet.getString("CODICE"));
        
        SolmrLogger.debug(this, " -- descrizione ateco ="+attivitaATECO.getDescription());
      }
      
      return attivitaATECO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[] { new Variabile("query", query),
          new Variabile("queryBuf", queryBuf),
          new Variabile("attivitaATECO", attivitaATECO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[] { new Parametro("codiceAteco",
          codiceAteco) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[CommonDAO::getAttivitaATECObyCodeParametroCATE] ", t, query,
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
          "[CommonDAO::getAttivitaATECObyCodeParametroCATE] END.");
    }
  }

  /**
   * 
   * Restituisce il VO UtenteIrideVO con le informazioni di un utente iride.
   * 
   * @param idUtente
   *          id dell'utente.
   * @return VO con i dati
   * 
   * @throws DataAccessException
   *           Errore di accesso ai dati
   * @throws NotFoundException
   *           id utente non presente nel database
   */
  public UtenteIrideVO getUtenteIrideById(Long idUtente)
      throws DataAccessException
  {
    UtenteIrideVO uiVO = new UtenteIrideVO();
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {

      conn = getDatasource().getConnection();

      String query =       
      "SELECT NVL(PVU.COGNOME_UTENTE, PVU.CODICE_FISCALE_UTENTE_LOGIN) AS CODICE_FISCALE, " +
      "       PVU.CUAA, " + 
      "       PVU.DATA_ULTIMO_ACCESSO, " +
      "       NVL(PVU.COGNOME_UTENTE, TRIM(UPPER(PVU.COGNOME_UTENTE_LOGIN)))||' '||NVL(PVU.NOME_UTENTE, TRIM(UPPER(PVU.NOME_UTENTE_LOGIN))) " +
      "       AS DENOMINAZIONE, " +
      "       PVU.DENOMINAZIONE AS DESCRIZIONE_ENTE_APPARTENENZA, " +
      "       PVU.EXT_ID_INTERMEDIARIO AS ID_INTERMEDIARIO, " + 
      //"       UTENTE_IRIDE.ID_PROFILO, "
      "       PVU.ID_UTENTE_LOGIN AS ID_UTENTE, " + 
      "       NVL(PVU.EMAIL_UTENTE, PVU.EMAIL_UTENTE_LOGIN) AS MAIL, " +
      "       PVU.NUMERO_ACCESSI, " + 
      "       PROV.SIGLA_PROVINCIA AS PROVINCIA " +
      "FROM   PAPUA_V_UTENTE_LOGIN PVU, " +
      "       COMUNE COM, " +
      "       PROVINCIA PROV " +
      "WHERE  PVU.ID_UTENTE_LOGIN = ? " +
      "AND    PVU.ISTAT_COMUNE_RESIDENZA = COM.ISTAT_COMUNE (+) " +
      "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA (+) ";

      SolmrLogger.debug(this, "getUtenteIrideById Executing query: " + query);
      SolmrLogger.debug(this, "getUtenteIrideById idUtente: " + idUtente);

      stmt = conn.prepareStatement(query);
      stmt.setLong(1, idUtente.longValue());
      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        uiVO.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
        uiVO.setCuaa(rs.getString("CUAA"));
        uiVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        uiVO.setDataUltimoAccesso(rs.getDate("DATA_ULTIMO_ACCESSO"));
        uiVO.setDescrizioneEnteAppartenenza(rs
            .getString("DESCRIZIONE_ENTE_APPARTENENZA"));
        uiVO.setIdIntermediario(checkLong(rs.getString("ID_INTERMEDIARIO")));
        //uiVO.setIdProfilo(checkLong(rs.getString("ID_PROFILO")));
        uiVO.setIdUtente(checkLong(rs.getString("ID_UTENTE")));
        uiVO.setMail(rs.getString("MAIL"));
        uiVO.setNumeroAccessi(checkLong(rs.getString("NUMERO_ACCESSI")));
        uiVO.setProvincia(rs.getString("PROVINCIA"));
      }
      
      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in getUtenteIrideById: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in getUtenteIrideById: "
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
                "SQLException while closing Statement and Connection in ricercaIdAttivitaATECO: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in ricercaIdAttivitaATECO: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return uiVO;
  }

  // Metodo per recuperare il codice istat del comune a partire dalla
  // descrizione del comune e provincia
  public String ricercaCodiceComuneNonEstinto(String descrizioneComune,
      String siglaProvincia) throws DataAccessException, DataControlException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String codiceIstat = null;

    try
    {
      conn = getDatasource().getConnection();
      String provaQuery = " AND PROVINCIA.SIGLA_PROVINCIA = ? ";
      String query = " SELECT COMUNE.ISTAT_COMUNE FROM COMUNE, PROVINCIA "
          + " WHERE COMUNE.DESCOM = ? " + " AND COMUNE.FLAG_ESTINTO = ? "
          + " AND COMUNE.ISTAT_PROVINCIA=PROVINCIA.ISTAT_PROVINCIA";
      boolean bSiglaProvincia = siglaProvincia != null
          && !"".equals(siglaProvincia);
      if (bSiglaProvincia)
      {
        query += provaQuery;
      }
      stmt = conn.prepareStatement(query);
      stmt.setString(1, descrizioneComune.toUpperCase());
      stmt.setString(2, SolmrConstants.FLAG_N);
      if (bSiglaProvincia)
      {
        stmt.setString(3, siglaProvincia.toUpperCase());
      }
      SolmrLogger.debug(this,
          "\n\n\n\n********************************************");
      SolmrLogger.debug(this, "ricercaComuneNonEstinto: Query = " + query);
      SolmrLogger.debug(this,
          "********************************************\n\n\n\n");

      rs = stmt.executeQuery();

      if (rs.next())
      {
        codiceIstat = rs.getString(1);
        if (rs.next())
        {
          throw new DataControlException((String) AnagErrors
              .get("COMUNE_DUPLICATO"));
        }
      }
      else
      {
        // Il messaggio che mando è quello relativo al fatto che non è stato
        // trovato un codice
        // istat relativo alla descrizione del comune quindi il comune inserito
        // dall'utente è inesistente
        throw new DataControlException(AnagErrors.CODICEISTATCOMUNE);
      }
      rs.close();
      stmt.close();
    }
    catch (DataControlException dce)
    {
      SolmrLogger.fatal(this,
          "DataControlException in ricercaCodiceComuneNonEstinto : "
              + dce.getMessage());
      throw new DataControlException(dce.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in ricercaCodiceComuneNonEstinto: "
              + ex.getMessage());
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
            .fatal(
                this,
                "SQLException while closing Statement and Connection in ricercaCodiceComuneNonEstinto: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in ricercaCodiceComuneNonEstinto: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return codiceIstat;
  }
  
  
  public String ricercaCodiceComuneFlagEstinto(String descrizioneComune,
      String siglaProvincia, String estinto) throws DataAccessException, DataControlException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String codiceIstat = null;

    try
    {
      conn = getDatasource().getConnection();
      String provaQuery = " AND PROVINCIA.SIGLA_PROVINCIA = ? ";
      String query = 
        "SELECT COMUNE.ISTAT_COMUNE " +
        "FROM   COMUNE, " +
        "       PROVINCIA " +
        "WHERE  COMUNE.DESCOM = ? ";
      if (Validator.isNotEmpty(estinto))
      {
        query += 
        "AND COMUNE.FLAG_ESTINTO = ? ";
      }
      query +=  
        "AND COMUNE.ISTAT_PROVINCIA = PROVINCIA.ISTAT_PROVINCIA ";
      boolean bSiglaProvincia = siglaProvincia != null
          && !"".equals(siglaProvincia);
      if (bSiglaProvincia)
      {
        query += provaQuery;
      }
      int idx = 0;
      stmt = conn.prepareStatement(query);
      stmt.setString(++idx, descrizioneComune.toUpperCase());
      if (Validator.isNotEmpty(estinto))
      {
        stmt.setString(++idx, estinto);
      }
      if (bSiglaProvincia)
      {
        stmt.setString(++idx, siglaProvincia.toUpperCase());
      }
      SolmrLogger.debug(this,
          "\n\n\n\n********************************************");
      SolmrLogger.debug(this, "ricercaCodiceComuneFlagEstinto: Query = " + query);
      SolmrLogger.debug(this,
          "********************************************\n\n\n\n");

      rs = stmt.executeQuery();

      if (rs.next())
      {
        codiceIstat = rs.getString(1);
        if (rs.next())
        {
          throw new DataControlException((String) AnagErrors
              .get("COMUNE_DUPLICATO"));
        }
      }
      else
      {
        // Il messaggio che mando è quello relativo al fatto che non è stato
        // trovato un codice
        // istat relativo alla descrizione del comune quindi il comune inserito
        // dall'utente è inesistente
        throw new DataControlException(AnagErrors.CODICEISTATCOMUNE);
      }
      rs.close();
      stmt.close();
    }
    catch (DataControlException dce)
    {
      SolmrLogger.fatal(this,
          "DataControlException in ricercaCodiceComuneFlagEstinto : "
              + dce.getMessage());
      throw new DataControlException(dce.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in ricercaCodiceComuneFlagEstinto: "
              + ex.getMessage());
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
            .fatal(
                this,
                "SQLException while closing Statement and Connection in ricercaCodiceComuneFlagEstinto: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in ricercaCodiceComuneFlagEstinto: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return codiceIstat;
  }
  
  

  public Vector<CodeDescription> getTipiIntermediario()
      throws DataAccessException
  {
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT ID_INTERMEDIARIO, " + "       DENOMINAZIONE "
          + "FROM DB_INTERMEDIARIO " + "ORDER BY DENOMINAZIONE";

      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        result = new Vector<CodeDescription>();
        while (rs.next())
        {
          CodeDescription codeDescVO = new CodeDescription();
          codeDescVO.setCode(new Integer(rs.getString("ID_INTERMEDIARIO")));
          codeDescVO.setDescription(rs.getString("DENOMINAZIONE"));

          result.add(codeDescVO);
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // Metodo per recupare il flag della partita iva relativa ad una forma
  // giuridica
  public String getFlagPartitaIva(Long idTipoFormaGiuridica)
      throws DataAccessException
  {

    String flagPartitaIva = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT FLAG_PARTITA_IVA "
          + "FROM DB_TIPO_FORMA_GIURIDICA " + "WHERE ID_FORMA_GIURIDICA = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);
      stmt.setLong(1, idTipoFormaGiuridica.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        if (rs.next())
        {
          flagPartitaIva = rs.getString(1);
        }
      }
      else
      {
        throw new DataAccessException();
      }

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in getFlagPartitaIva: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null in getFlagPartitaIva");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in getFlagPartitaIva: "
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
            "SQLException while closing Statement and Connection in getFlagPartitaIva: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in getFlagPartitaIva: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return flagPartitaIva;
  }
  
  
  
  public String getObbligoGfFromFormaGiuridica(Long idTipoFormaGiuridica)
      throws DataAccessException
  {

    String obbligoGF = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT OBBLIGO_GF "
          + "FROM DB_TIPO_FORMA_GIURIDICA " + "WHERE ID_FORMA_GIURIDICA = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);
      stmt.setLong(1, idTipoFormaGiuridica.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        if (rs.next())
        {
          obbligoGF = rs.getString(1);
        }
      }
      else
      {
        throw new DataAccessException();
      }

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in getObbligoGfFromFormaGiuridica: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null in getObbligoGfFromFormaGiuridica");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in getObbligoGfFromFormaGiuridica: "
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
            "SQLException while closing Statement and Connection in getObbligoGfFromFormaGiuridica: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in getObbligoGfFromFormaGiuridica: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return obbligoGF;
  }

  public Long getIdTipologiaAziendaByFormaGiuridica(Long idTipoFormaGiuridica,
      Boolean flagAziendaProvvisoria) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Long idFormaGiuridica = null;
    try
    {
      conn = getDatasource().getConnection();
      /**
       * Vado a leggere l'id della forma giuridica corrispondente alla
       * descrizione fornita da AAEP
       */
      String query = "SELECT TFG.ID_TIPOLOGIA_AZIENDA "
          + "FROM DB_TIPO_FG_TIPOLOGIA TFG,DB_TIPO_TIPOLOGIA_AZIENDA TA "
          + "WHERE TFG.ID_FORMA_GIURIDICA = ? "
          + "AND TFG.ID_TIPOLOGIA_AZIENDA=TA.ID_TIPOLOGIA_AZIENDA ";

      if (flagAziendaProvvisoria != null
          && flagAziendaProvvisoria.booleanValue())
        query += "AND TA.FLAG_AZIENDA_PROVVISORIA ='S' ";
      query += "ORDER BY TFG.ID_TIPOLOGIA_AZIENDA";

      stmt = conn.prepareStatement(query);
      stmt.setLong(1, idTipoFormaGiuridica.longValue());

      SolmrLogger.debug(this, "Executing query: " + query);

      ResultSet rs = stmt.executeQuery();
      if (rs.next())
        idFormaGiuridica = new Long(rs.getLong("ID_TIPOLOGIA_AZIENDA"));
      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "SQLException in getIdTipologiaAziendaByFormaGiuridica: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in getIdTipologiaAziendaByFormaGiuridica: "
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
                "SQLException while closing Statement and Connection in getIdTipologiaAziendaByFormaGiuridica: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in getIdTipologiaAziendaByFormaGiuridica: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return idFormaGiuridica;
  }

  // Metodo per recupare l'id della forma giuridica nostra dato il codice di
  // AAEP
  public Long getIdTipoFormaGiuridica(String idTipoFormaGiuridicaAAEP)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Long idFormaGiuridica = null;

    try
    {
      conn = getDatasource().getConnection();

      /**
       * Vado a leggere l'id della forma giuridica corrispondente alla
       * descrizione fornita da AAEP
       */
      String query = "SELECT ID_FORMA_GIURIDICA_GAA FROM DB_TIPO_FG_AAEP_GAA "
          + " WHERE CODICE_AAEP = ? ";

      stmt = conn.prepareStatement(query);

      stmt.setString(1, idTipoFormaGiuridicaAAEP.toUpperCase());

      SolmrLogger.debug(this, "Executing query: " + query);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        idFormaGiuridica = new Long(rs.getLong("ID_FORMA_GIURIDICA_GAA"));
      }

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in getIdTipoFormaGiuridica: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in getIdTipoFormaGiuridica: "
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
                "SQLException while closing Statement and Connection in getIdTipoFormaGiuridica: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in getIdTipoFormaGiuridica: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return idFormaGiuridica;
  }

  // Metodo per recupare la descrizione di una nostra forma giuridica data il
  // codice
  // della forma giuridica di AAEP
  public String getDescTipoFormaGiuridica(String idTipoFormaGiuridicaAAEP)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    String descFormaGiuridica = null;

    try
    {
      conn = getDatasource().getConnection();

      /**
       * Vado a leggere l'id della forma giuridica corrispondente alla
       * descrizione fornita da AAEP
       */
      String query = "SELECT DESCRIZIONE_GAA FROM DB_TIPO_FG_AAEP_GAA "
          + " WHERE CODICE_AAEP = ? ";

      stmt = conn.prepareStatement(query);

      stmt.setString(1, idTipoFormaGiuridicaAAEP.toUpperCase());

      SolmrLogger.debug(this, "Executing query: " + query);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        descFormaGiuridica = rs.getString("DESCRIZIONE_GAA");
      }

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in getIdTipoFormaGiuridica: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in getIdTipoFormaGiuridica: "
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
                "SQLException while closing Statement and Connection in getSescTipoFormaGiuridica: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in getSescTipoFormaGiuridica: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return descFormaGiuridica;
  }

  public boolean isProvinciaReaValida(String siglaProvincia)
      throws DataAccessException
  {

    boolean isValida = false;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {

      conn = getDatasource().getConnection();

      String query = "SELECT ISTAT_PROVINCIA FROM PROVINCIA WHERE SIGLA_PROVINCIA = ? ";

      stmt = conn.prepareStatement(query);
      stmt.setString(1, siglaProvincia.toUpperCase());

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        if (rs.next())
        {
          isValida = true;
        }
      }
      else
      {
        throw new DataAccessException();
      }
      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in isProvinciaReaValida: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null in isProvinciaReaValida");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in isProvinciaReaValida: "
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
            "SQLException while closing Statement and Connection in isProvinciaReaValida: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in isProvinciaReaValida: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return isValida;
  }

  // Ricerca le categorie di veicoli legati ad un genere macchina
  // Codice: id_categoria
  // Descrizione: descrizione
  public Vector<CodeDescription> getTipiCategoriaByGenereMacchina(
      Long idGenereMacchina) throws DataAccessException
  {
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = "select * from db_tipo_categoria "
          + "where id_genere_macchina = ? " + " order by descrizione";

      stmt = conn.prepareStatement(search);
      stmt.setLong(1, idGenereMacchina.longValue());

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        result = new Vector<CodeDescription>();
        while (rs.next())
        {
          CodeDescription codeDescVO = new CodeDescription();
          codeDescVO.setCode(new Integer(rs.getString("id_categoria")));
          codeDescVO.setDescription(rs.getString("descrizione"));

          result.add(codeDescVO);
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // Ricerca tutte le marche legate ad un genere macchina
  // Codice: id_genere_macchina
  // Descrizione: descrizione
  public Vector<CodeDescription> getTipiMarcaByGenereMacchina(
      Long idGenereMacchina) throws DataAccessException
  {
    SolmrLogger.debug(this, "getTipiMarcaByGenereMacchina - Begin");
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = "select distinct db_tipo_marca.id_marca, db_tipo_marca.DESCRIZIONE "
          + "from db_tipo_marca, db_matrice, db_tipo_genere_macchina "
          + "where db_tipo_genere_macchina.ID_GENERE_MACCHINA = db_matrice.ID_GENERE_MACCHINA "
          + "and db_matrice.ID_MARCA = db_tipo_marca.ID_MARCA "
          + "and db_tipo_genere_macchina.ID_GENERE_MACCHINA = ? ";

      stmt = conn.prepareStatement(search);
      stmt.setLong(1, idGenereMacchina.longValue());

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      SolmrLogger.debug(this, "Executed query");

      if (rs != null)
      {
        result = new Vector<CodeDescription>();
        SolmrLogger.debug(this, "result = new Vector()");
        while (rs.next())
        {
          CodeDescription codeDescVO = new CodeDescription();
          SolmrLogger.debug(this, "rs.getString(\"id_marca\")"
              + rs.getString("id_marca"));
          codeDescVO.setCode(new Integer(rs.getString("id_marca")));
          SolmrLogger.debug(this, "rs.getString(\"descrizione\")"
              + rs.getString("descrizione"));
          codeDescVO.setDescription(rs.getString("descrizione"));

          result.add(codeDescVO);
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // Restituisce un elenco di matrici che rispondono ai parametri passati.
  // genere: genereMacchina
  // categoria: categoria
  // marca: marca
  // tipo: tipoMacchina
  // numeroMatrice: numeroMatrice
  // numeroOmologazione: numeroOmologazione

  /*public Vector<MatriceVO> getElencoMatrici(String genereMacchina,
      String categoria, String marca, String tipoMacchina,
      String numeroMatrice, String numeroOmologazione)
      throws DataAccessException
  {
    Vector<MatriceVO> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = "select dbm.NUMERO_MATRICE as numeroMatrice, "
          + "  dbm.TIPO_MACCHINA as tipoMacchina, "
          + "  dbtc.DESCRIZIONE as tipoCategoria, "
          + "  dbm.NUMERO_OMOLOGAZIONE as numeroOmologazione, "
          + "  dbm.POTENZA_CV as potenzaCV, "
          + "  dbm.POTENZA_KW as potenzaKW, "
          + "  dbta.DESCRIZIONE as alimentazione" + "from db_matrice dbm,  "
          + " db_tipo_categoria dbtc,  " + " db_tipo_alimentazione dbta, "
          + " db_tipo_genere_macchina dbtgm, " + " db_tipo_marca dbtm"
          + "where dbm.ID_CATEGORIA = dbtc.ID_CATEGORIA "
          + "and dbta.ID_ALIMENTAZIONE = dbm.ID_ALIMENTAZIONE "
          + "and dbm.ID_GENERE_MACCHINA = dbtgm.ID_GENERE_MACCHINA "
          + "and dbm.ID_MARCA = dbtm.ID_MARCA";

      if (!"".equals(genereMacchina) && genereMacchina != null)
      {
        search += "and dbtgm.DESCRIZIONE LIKE '%" + genereMacchina + "%'";
      }
      if (!"".equals(categoria) && categoria != null)
      {
        search += "and dbtc.DESCRIZIONE LIKE '%" + categoria + "%'";
      }
      if (!"".equals(marca) && categoria != null)
      {
        search += "and dbtm.descrizione LIKE '%" + marca + "%'";
      }
      if (!"".equals(tipoMacchina) && tipoMacchina != null)
      {
        search += "and dbm.tipo_macchina LIKE '%" + tipoMacchina + "%'";
      }
      if (!"".equals(numeroMatrice) && numeroMatrice != null)
      {
        search += "and dbm.numero_matrice LIKE '%" + numeroMatrice + "%'";
      }
      if (!"".equals(numeroOmologazione) && numeroOmologazione != null)
      {
        search += "and dbm.numero_omologazione LIKE '%" + numeroOmologazione
            + "%'";
      }

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        result = new Vector<MatriceVO>();
        while (rs.next())
        {
          CodeDescription codeDescVO = new CodeDescription();
          codeDescVO.setCode(new Integer(rs.getString("id_marca")));
          codeDescVO.setDescription(rs.getString("descrizione"));

          MatriceVO maVO = new MatriceVO();

          maVO.setNumeroMatrice(rs.getString("numeroMatrice"));
          maVO.setTipoMacchina(rs.getString("tipoMacchina"));
          maVO.setDescCategoria(rs.getString("tipoCategoria"));
          maVO.setNumeroOmologazione(rs.getString("numeroOmologazione"));
          maVO.setPotenzaCV(rs.getString("potenzaCV"));
          maVO.setPotenzaKW("potenzaKW");
          maVO.setDescAlimentazione(rs.getString("alimentazione"));

          result.add(maVO);
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }*/

  // Ricerca tutte le marche legate ad un genere macchina
  // Partita Iva: partitaIVA
  // Ragione Sociale: ragioneSociale
  public Vector<AnagAziendaVO> getElencoDitteLeasing(String partitaIVA,
      String ragioneSociale) throws DataAccessException
  {
    Vector<AnagAziendaVO> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    SolmrLogger.debug(this, "Called CommonDAO.getElencoDitteLeasing("
        + partitaIVA + "," + ragioneSociale + ")");
    try
    {
      conn = getDatasource().getConnection();
      if (ragioneSociale == null)
      {
        ragioneSociale = "";
      }
      StringBuffer search = new StringBuffer(
          " select aa.id_anagrafica_azienda, " + " aa.id_azienda, "
              + " aa.denominazione, " + " aa.partita_iva, "
              + " aa.sedeleg_indirizzo, " + " pf.nome, " + " pf.cognome, "
              + " com.descom " + " from db_anagrafica_azienda aa, "
              + " db_azienda a, db_contitolare c, " + " db_persona_fisica pf, "
              + " comune com where "
              + " com.istat_comune=aa.sedeleg_comune and "
              + " c.id_ruolo = 1 and " + " c.data_fine_ruolo is null and "
              + " a.id_azienda = c.id_azienda and "
              + " c.id_soggetto = pf.id_soggetto and "
              + " aa.id_azienda=a.id_azienda and "
              + " a.DATA_INIZIO_VALIDITA=aa.DATA_INIZIO_VALIDITA and "
              + " aa.ID_TIPOLOGIA_AZIENDA=3 and "
              + " aa.DATA_FINE_VALIDITA IS NULL and "
              + " aa.DATA_CESSAZIONE IS NULL ");
      if (ragioneSociale != null && !"".equals(ragioneSociale))
      {
        search.append(" and aa.DENOMINAZIONE LIKE '%");
        search.append(ragioneSociale.toUpperCase());
        search.append("%'");
      }

      if (partitaIVA != null && !"".equals(partitaIVA))
      {
        search.append(" and aa.PARTITA_IVA = ");
        search.append(partitaIVA);
      }

      stmt = conn.prepareStatement(search.toString());

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        result = new Vector<AnagAziendaVO>();
        while (rs.next())
        {
          AnagAziendaVO aavo = new AnagAziendaVO();

          aavo.setIdAnagAzienda(checkLong(rs.getString(1)));
          aavo.setIdAzienda(checkLong(rs.getString(2)));
          aavo.setDenominazione(rs.getString(3));
          aavo.setPartitaIVA(rs.getString(4));
          aavo.setSedelegIndirizzo(rs.getString(5));
          aavo.setRappresentanteLegale(rs.getString(6) + " " + rs.getString(7));
          aavo.setDescComune(rs.getString(8));

          result.add(aavo);
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "return CommonDAO.getElencoDitteLeasing("
        + partitaIVA + "," + ragioneSociale + ") with value " + result);
    return result;
  }

  public AnagAziendaVO getDittaLeasing(String partitaIVA, String ragioneSociale)
      throws DataAccessException, DataControlException
  {
    AnagAziendaVO result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    SolmrLogger.debug(this, "Called CommonDAO.getDittaLeasing(" + partitaIVA
        + "," + ragioneSociale + ")");
    try
    {
      conn = getDatasource().getConnection();
      if (ragioneSociale == null)
      {
        ragioneSociale = "";
      }
      String search = " select aa.id_anagrafica_azienda, " + " aa.id_azienda, "
          + " aa.denominazione, " + " aa.partita_iva, "
          + " aa.sedeleg_indirizzo, " + " pf.nome, " + " pf.cognome, "
          + " com.descom " + " from db_anagrafica_azienda aa, "
          + " db_azienda a, db_contitolare c, " + " db_persona_fisica pf, "
          + " comune com where " + " com.istat_comune=aa.sedeleg_comune and "
          + " c.id_ruolo = 1 and " + " c.DATA_FINE_RUOLO is null and "
          + " a.id_azienda = c.id_azienda and "
          + " c.id_soggetto = pf.id_soggetto and "
          + " aa.id_azienda=a.id_azienda and "
          + " a.DATA_INIZIO_VALIDITA=aa.DATA_INIZIO_VALIDITA and "
          + " aa.ID_TIPOLOGIA_AZIENDA=3 and "
          + " aa.DATA_FINE_VALIDITA IS NULL and "
          + " aa.DATA_CESSAZIONE IS NULL and " + " aa.DENOMINAZIONE = '"
          + ragioneSociale.toUpperCase() + "' and " + " aa.PARTITA_IVA = '"
          + partitaIVA + "'" + " ORDER BY aa.DENOMINAZIONE";

      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        result = new AnagAziendaVO();

        result.setIdAnagAzienda(checkLong(rs.getString(1)));
        result.setIdAzienda(checkLong(rs.getString(2)));
        result.setDenominazione(rs.getString(3));
        result.setPartitaIVA(rs.getString(4));
        result.setSedelegIndirizzo(rs.getString(5));
        result.setRappresentanteLegale(rs.getString(6) + " " + rs.getString(7));
        result.setDescComune(rs.getString(8));
        if (rs.next())
        {
          throw new DataControlException(
              SolmrConstants.DITTA_LEASING_NON_UNIVOCA);
        }
      }

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataControlException exc)
    {
      SolmrLogger.fatal(this, "DataControlException: " + exc.getMessage());
      throw exc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "return CommonDAO.getDittaLeasing(" + partitaIVA
        + "," + ragioneSociale + ") with value " + result);
    return result;
  }

  public Vector<StringcodeDescription> getProvincePiemonte()
      throws DataAccessException
  {
    Vector<StringcodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT ISTAT_PROVINCIA, DESCRIZIONE "
          + "  FROM PROVINCIA " + " WHERE ID_REGIONE = "
          + SolmrConstants.ID_REG_PIEMONTE + " " + " ORDER BY DESCRIZIONE";
      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        result = new Vector<StringcodeDescription>();
        while (rs.next())
        {
          StringcodeDescription code = new StringcodeDescription();
          code.setCode(rs.getString("ISTAT_PROVINCIA"));
          code.setDescription(rs.getString("DESCRIZIONE"));

          result.add(code);
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // Metodo per recuperare l'elenco dei comuni a partire dall'istat provincia
  // Recupera solo istat_comune e descom.
  public Vector<ComuneVO> getAllComuniByProvincia(String istatProvincia)
      throws DataAccessException, DataControlException
  {

    Vector<ComuneVO> elencoComuni = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = "select comune.DESCOM, comune.ISTAT_COMUNE "
          + "from comune "
          + "where comune.istat_provincia=? and comune.flag_estero='N' "
          + "order by comune.descom";
      stmt = conn.prepareStatement(query);

      stmt.setString(1, istatProvincia);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        elencoComuni = new Vector<ComuneVO>();
        while (rs.next())
        {
          ComuneVO comuneVO = new ComuneVO();
          comuneVO.setDescom(rs.getString(1));
          comuneVO.setIstatComune(rs.getString(2));
          elencoComuni.add(comuneVO);
        }
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found " + elencoComuni.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoComuni;
  }

  /**
   * Controlla se sulla tabella data esistono record legati ad una ditta
   * 
   * @param idDittaUma
   *          Long
   * @param table
   *          String
   * @throws DataAccessException
   * @throws DataControlException
   */
  public void notExistRecordByIdDitta(Long idDittaUma, String table)
      throws DataAccessException, DataControlException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try
    {
      conn = getDatasource().getConnection();
      String query = "select id_ditta_uma " + "from " + table
          + " where id_ditta_uma = ?";
      SolmrLogger.debug(this, "Executing notExistRecordByIdDitta: " + query);
      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idDittaUma.longValue());
      rs = stmt.executeQuery();
      if (rs.next())
      {
        throw new DataControlException(
            "Esistono dei record legati alla ditta selezionata");
      }
    }
    catch (SQLException sqle)
    {
      SolmrLogger.fatal(this, "notExistRecordByIdDitta - SQLException: "
          + sqle.getMessage());
      throw new DataAccessException(sqle.getMessage());
    }
    catch (DataControlException dce)
    {
      throw dce;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "notExistRecordByIdDitta - Generic Exception: "
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
                "notExistRecordByIdDitta - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "notExistRecordByIdDitta - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  public boolean isComunePiemontese(String istatComune)
      throws DataAccessException, SQLException
  {

    boolean result = false;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = "select r.id_regione from comune c, provincia p, regione r where c.ISTAT_COMUNE = ? "
          + " and p.ISTAT_PROVINCIA = c.ISTAT_PROVINCIA "
          + " and r.ID_REGIONE = ? " + " and p.ID_REGIONE = r.ID_REGIONE";

      stmt = conn.prepareStatement(query);

      stmt.setString(1, istatComune);
      stmt.setString(2, SolmrConstants.ID_REG_PIEMONTE);

      SolmrLogger.debug(this, "Executing query: " + query);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        if (rs.next())
        {
          String idRegione = rs.getString(1);
          if (idRegione.equalsIgnoreCase(SolmrConstants.ID_REG_PIEMONTE))
          {
            result = true;
          }
        }
      }

      rs.close();
      stmt.close();
    }
    catch (SQLException sqle)
    {
      SolmrLogger.fatal(this, "SQLException: " + sqle.getMessage());
      throw new DataAccessException(sqle.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  public Vector<CodeDescription> getTipiMarcaByGenereMacchinaAndLikeMarca(
      Long idGenereMacchina, String marca) throws DataAccessException
  {
    SolmrLogger.debug(this, "getTipiMarcaByGenereMacchina - Begin");
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = "select db_tipo_marca.id_marca, db_tipo_marca.DESCRIZIONE, "
          + " db_tipo_marca.matrice from db_tipo_marca "
          + " where db_tipo_marca.DESCRIZIONE LIKE ? "
          + " and db_tipo_marca.MATRICE like ? "
          + " order by db_tipo_marca.DESCRIZIONE";

      stmt = conn.prepareStatement(search);
      SolmrLogger.debug(this, "idGenereMacchina: "
          + idGenereMacchina.longValue());
      stmt.setString(1, marca.toUpperCase() + "%");
      stmt.setString(2, String.valueOf(idGenereMacchina.longValue()) + "%");
      SolmrLogger.debug(this, "marca: " + marca);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      SolmrLogger.debug(this, "Executed query");

      if (rs != null)
      {
        result = new Vector<CodeDescription>();
        SolmrLogger.debug(this, "result = new Vector()");
        while (rs.next())
        {
          CodeDescription codeDescVO = new CodeDescription();
          SolmrLogger.debug(this, "rs.getString(\"id_marca\")"
              + rs.getString("id_marca"));
          codeDescVO.setCode(new Integer(rs.getString("id_marca")));
          SolmrLogger.debug(this, "rs.getString(\"descrizione\")"
              + rs.getString("descrizione"));
          codeDescVO.setDescription(rs.getString("descrizione"));
          codeDescVO.setSecondaryCode(rs.getString("matrice"));

          result.add(codeDescVO);
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  public Boolean isProvinciaValid(String siglaProvincia)
      throws DataAccessException
  {

    Boolean exist = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {

      conn = getDatasource().getConnection();

      String query = "select istat_provincia from provincia where sigla_provincia = ? ";

      stmt = conn.prepareStatement(query);
      stmt.setString(1, siglaProvincia.toUpperCase());

      ResultSet rs = stmt.executeQuery();
      exist = new Boolean(rs.next());
      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in isProvinciaReaValida: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in isProvinciaReaValida: "
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
            "SQLException while closing Statement and Connection in isProvinciaReaValida: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in isProvinciaReaValida: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return exist;
  }

  public Boolean isProvinciaPiemontese(String siglaProvincia)
      throws DataAccessException
  {

    Boolean exist = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {

      conn = getDatasource().getConnection();

      String query = "select istat_provincia from provincia where "
          + " sigla_provincia = ? and " + " id_regione = ? ";

      stmt = conn.prepareStatement(query);
      stmt.setString(1, siglaProvincia.toUpperCase());
      stmt.setString(2, (String) SolmrConstants.get("ID_REG_PIEMONTE"));

      ResultSet rs = stmt.executeQuery();
      exist = new Boolean(rs.next());
      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in isProvinciaReaValida: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in isProvinciaReaValida: "
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
            "SQLException while closing Statement and Connection in isProvinciaReaValida: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in isProvinciaReaValida: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return exist;
  }

  public Vector<StringcodeDescription> getSiglaProvincePiemontesi()
      throws SolmrException
  {

    Vector<StringcodeDescription> elencoProvince = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {

      conn = getDatasource().getConnection();

      String query = "SELECT ISTAT_PROVINCIA, SIGLA_PROVINCIA FROM PROVINCIA WHERE ID_REGIONE = ? "
          + " ORDER BY SIGLA_PROVINCIA ";

      stmt = conn.prepareStatement(query);

      stmt.setString(1, (String) SolmrConstants.get("ID_REG_PIEMONTE"));

      SolmrLogger.debug(this, "Executing query getSiglaProvincePiemontesi: "
          + query);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        elencoProvince = new Vector<StringcodeDescription>();
        while (rs.next())
        {
          StringcodeDescription code = new StringcodeDescription();
          code.setCode(rs.getString(1));
          code.setDescription(rs.getString(2));
          elencoProvince.add(code);
        }
      }

      if (elencoProvince.size() == 0)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_RICERCA_PROVINCE_TOBECONFIG"));
      }
      rs.close();
      stmt.close();

      SolmrLogger.debug(this,
          "Executed query getSiglaProvincePiemontesi - Found "
              + elencoProvince.size() + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in getSiglaProvincePiemontesi: "
          + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(this, "Generic Exception in getSiglaProvincePiemontesi: "
              + ex.getMessage());
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
                "SQLException while closing Statement and Connection in getSiglaProvincePiemontesi: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in getComuniNonEstintiLikeByProvAndCom: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoProvince;
  }

  public Vector<ComuneVO> getComuniPiemontesiNonEstintiLikeByProvAndCom(
      String provincia, String comune) throws DataAccessException,
      NotFoundException, DataControlException
  {

    Vector<ComuneVO> elencoComuni = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    String queryProvincia = "";
    String queryComune = "";

    int indiceStatement = 0;

    if (provincia != null && !provincia.equals(""))
    {
      queryProvincia = " c.ISTAT_PROVINCIA in (select p.ISTAT_PROVINCIA from provincia where p.SIGLA_PROVINCIA like ?) ";
    }
    else
    {
      queryProvincia = " p.ISTAT_PROVINCIA = c.ISTAT_PROVINCIA ";
    }
    if (comune != null && !comune.equals(""))
    {
      queryComune = "and c.DESCOM  like ? ";
    }
    try
    {
      conn = getDatasource().getConnection();

      String query = "select p.descrizione, c.descom, c.ISTAT_COMUNE, c.cap, p.sigla_provincia, c.istat_provincia, c.zonaalt, c.codfisc from comune c, provincia p where "
          + queryProvincia
          + queryComune
          + "and flag_estero = ? and flag_estinto = ? and id_regione = ? order by c.descom ";

      stmt = conn.prepareStatement(query);

      if (provincia != null && !provincia.equals(""))
      {
        if (provincia.length() == 2)
        {
          stmt.setString(++indiceStatement, provincia.toUpperCase() + "%");
        }
        else
        {
          stmt.setString(++indiceStatement, provincia);
        }
      }
      if (comune != null && !comune.equals(""))
      {
        stmt.setString(++indiceStatement, comune.toUpperCase() + "%");
      }
      stmt.setString(++indiceStatement, SolmrConstants.FLAG_N);
      stmt.setString(++indiceStatement, SolmrConstants.FLAG_N);
      stmt.setString(++indiceStatement, SolmrConstants.ID_REG_PIEMONTE);

      SolmrLogger.debug(this,
          "Executing query getComuniPiemontesiNonEstintiLikeByProvAndCom: "
              + query);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        elencoComuni = new Vector<ComuneVO>();
        while (rs.next())
        {
          ComuneVO comuneVO = new ComuneVO();
          comuneVO.setDescrProv(rs.getString(1));
          comuneVO.setDescom(rs.getString(2));
          comuneVO.setIstatComune(rs.getString(3));
          comuneVO.setCap(rs.getString(4));
          comuneVO.setSiglaProv(rs.getString(5));
          comuneVO.setIstatProvincia(rs.getString(6));
          comuneVO.setZonaAlt(new Long(rs.getLong(7)));
          comuneVO.setCodfisc(rs.getString(8));
          elencoComuni.add(comuneVO);
        }
      }
      if (elencoComuni.size() == 0)
      {
        throw new DataControlException(AnagErrors.RICERCACOMUNI);
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this,
          "Executed query getComuniPiemontesiNonEstintiLikeByProvAndCom - Found "
              + elencoComuni.size() + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "SQLException in getComuniPiemontesiNonEstintiLikeByProvAndCom: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataControlException dce)
    {
      SolmrLogger.fatal(this,
          "DataControlException in getComuniPiemontesiNonEstintiLikeByProvAndCom: "
              + dce.getMessage());
      throw new DataControlException(dce.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in getComuniPiemontesiNonEstintiLikeByProvAndCom: "
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
                "SQLException while closing Statement and Connection in getComuniPiemontesiNonEstintiLikeByProvAndCom: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in getComuniPiemontesiNonEstintiLikeByProvAndCom: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoComuni;
  }

  // Metodo per recuperare i codici e le descrizioni degli intermediari tranne
  // dell'elemento non desiderato
  public Vector<CodeDescription> getElencoIntermediariNoDelegati(
      Long idIntermediario) throws DataAccessException, NotFoundException
  {

    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = " SELECT id_intermediario, denominazione "
          + " FROM DB_INTERMEDIARIO" + " where id_intermediario != "
          + idIntermediario + " ORDER BY denominazione";

      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        result = new Vector<CodeDescription>();
        while (rs.next())
        {
          CodeDescription cd = new CodeDescription();
          cd.setCode(new Integer(rs.getInt(1)));
          cd.setDescription(rs.getString(2));
          result.add(cd);
        }
      }
      else
      {
        throw new DataAccessException();
      }

      rs.close();
      stmt.close();

      if (result.size() == 0)
      {
        throw new NotFoundException();
      }

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "SQLException in getElencoIntermediariNoDelegati: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (NotFoundException nfexc)
    {
      SolmrLogger.fatal(this,
          "NotFoundException in getElencoIntermediariNoDelegati: "
              + nfexc.getMessage());
      throw nfexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in getElencoIntermediariNoDelegati: "
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
                "SQLException while closing Statement and Connection in getElencoIntermediariNoDelegati: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in getElencoIntermediariNoDelegati: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // Metodo per recuperare il genere macchina in relazione alle matrici
  public Vector<CodeDescription> getGenereMacchinaForMatrici()
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<CodeDescription> result = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT ID_GENERE_MACCHINA, DESCRIZIONE "
          + " FROM DB_TIPO_GENERE_MACCHINA "
          + " WHERE RTRIM(CODIFICA_BREVE) <> (?)"
          + " AND RTRIM(CODIFICA_BREVE) <> (?)" + " ORDER BY DESCRIZIONE";

      stmt = conn.prepareStatement(query);

      stmt
          .setString(1, (String) SolmrConstants.get("TIPO_GENERE_MACCHINA_ASM"));
      stmt.setString(2, (String) SolmrConstants
          .get("TIPO_GENERE_MACCHINA_RIMORCHIO"));

      SolmrLogger.debug(this, "Executing query: " + query);

      ResultSet rs = stmt.executeQuery();

      result = new Vector<CodeDescription>();

      if (rs != null)
      {
        while (rs.next())
        {
          CodeDescription code = new CodeDescription();
          code.setCode(new Integer(rs.getString("ID_GENERE_MACCHINA")));
          code.setDescription(rs.getString("DESCRIZIONE"));
          ;
          result.add(code);
        }
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getGenereMacchina - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getGenereMacchina - Generic Exception: "
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
            "getGenereMacchina - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getGenereMacchina - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // Metodo per recuperare l'indirizzo di studio in relazione al titolo
  // selezionato
  public Vector<CodeDescription> getIndirizzoStudioByTitolo(Long idTitoloStudio)
      throws SolmrException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<CodeDescription> result = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT ID_INDIRIZZO_STUDIO, DESCRIZIONE "
          + " FROM DB_TIPO_INDIRIZZO_STUDIO " + " WHERE ID_TITOLO_STUDIO = ? "
          + " ORDER BY DESCRIZIONE";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idTitoloStudio.longValue());

      SolmrLogger.debug(this, "Executing query: " + query);

      ResultSet rs = stmt.executeQuery();

      result = new Vector<CodeDescription>();

      if (rs != null)
      {
        while (rs.next())
        {
          CodeDescription code = new CodeDescription();
          code.setCode(new Integer(rs.getString(1)));
          code.setDescription(rs.getString(2));
          ;
          result.add(code);
        }
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getIndirizzoStudioByTitolo - SQLException: "
          + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getIndirizzoStudioByTitolo - Generic Exception: " + ex.getMessage());
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
                "getIndirizzoStudioByTitolo - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getIndirizzoStudioByTitolo - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return result;
  }

  // Metodo per recuperare l'indirizzo di studio in relazione al titolo
  // selezionato
  public String getUnitaMisuraByTipoFabbricato(Long idTipologiaFabbricato)
      throws SolmrException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    String unitaMisura = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT UNITA_MISURA FROM DB_TIPO_TIPOLOGIA_FABBRICATO "
          + " WHERE ID_TIPOLOGIA_FABBRICATO = ? ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idTipologiaFabbricato.longValue());

      SolmrLogger.debug(this,
          "Executing query getUnitaMisuraByTipoFabbricato: " + query);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        while (rs.next())
        {
          unitaMisura = rs.getString(1);
        }
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getUnitaMisuraByTipoFabbricato - SQLException: "
          + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getUnitaMisuraByTipoFabbricato - Generic Exception: "
              + ex.getMessage());
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
                "getUnitaMisuraByTipoFabbricato - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getUnitaMisuraByTipoFabbricato - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return unitaMisura;
  }

  // Metodo per recuperare il fattore di cubatura in relazione alla forma del
  // fabbricato
  public double getFattoreCubaturaByFormaFabbricato(String idFormaFabbricato)
      throws SolmrException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    double fattoreCubatura = 0;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT FATTORE_CUBATURA FROM DB_TIPO_FORMA_FABBRICATO "
          + " WHERE ID_FORMA_FABBRICATO = ? ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, Long.parseLong(idFormaFabbricato));

      SolmrLogger.debug(this,
          "Executing query getFattoreCubaturaByFormaFabbricato: " + query);
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
        fattoreCubatura = rs.getDouble(1);
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getFattoreCubaturaByFormaFabbricato - SQLException: "
              + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getFattoreCubaturaByFormaFabbricato - Generic Exception: "
              + ex.getMessage());
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
                "getFattoreCubaturaByFormaFabbricato - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getFattoreCubaturaByFormaFabbricato - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return fattoreCubatura;
  }

  // Metodo per recuperare il fattore di cubatura in relazione alla forma del
  // fabbricato
  public int getMesiRiscaldamentoBySerra(String tipologiaColturaSerra)
      throws SolmrException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    int numeroMesi = 0;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT MESI_RISCALDAMENTO FROM DB_TIPO_COLTURA_SERRA "
          + " WHERE ID_COLTURA_SERRA = ? ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, Long.parseLong(tipologiaColturaSerra));

      SolmrLogger.debug(this, "Executing query getMesiRiscaldamentoBySerra: "
          + query);
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
        numeroMesi = rs.getInt(1);
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getMesiRiscaldamentoBySerra - SQLException: "
          + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(this, "getMesiRiscaldamentoBySerra - Generic Exception: "
              + ex.getMessage());
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
                "getMesiRiscaldamentoBySerra - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getMesiRiscaldamentoBySerra - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return numeroMesi;
  }

  // Metodo per recuperare il valore dalla tabella parametro: importante per la
  // gestione dei terreni
  public String getValoreFromParametroByIdCode(String codice)
      throws DataAccessException, SolmrException
  {

    String valore = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = " SELECT VALORE " + " FROM   DB_PARAMETRO "
          + " WHERE  ID_PARAMETRO = ? ";

      stmt = conn.prepareStatement(search);

      stmt.setString(1, codice);

      SolmrLogger.debug(this,
          "Executing query getValoreFromParametroByIdCode: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        valore = rs.getString(1);
      }

      rs.close();
      stmt.close();

      if (!Validator.isNotEmpty(valore))
      {
        throw new SolmrException((String) (AnagErrors
            .get("ERR_NO_VALORE_PARAMETRO")));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger
          .fatal(this, "SQLException in getValoreFromParametroByIdCode: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in getValoreFromParametroByIdCode: "
              + ex.getMessage());
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
            .fatal(
                this,
                "SQLException in getValoreFromParametroByIdCode while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception in getValoreFromParametroByIdCode while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return valore;
  }
  
  
  
  public String getValoreFromParametroByIdCodeNullValue(String codice)
    throws DataAccessException
  {
  
    String valore = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
    
      String search = " SELECT VALORE " + " FROM   DB_PARAMETRO "
          + " WHERE  ID_PARAMETRO = ? ";
    
      stmt = conn.prepareStatement(search);
    
      stmt.setString(1, codice);
    
      SolmrLogger.debug(this,
          "Executing query getValoreFromParametroByIdCodeNullValue: " + search);
    
      ResultSet rs = stmt.executeQuery();
    
      if (rs.next())
      {
        valore = rs.getString(1);
      }
    
      rs.close();
      stmt.close();
      
    }
    catch (SQLException exc)
    {
      SolmrLogger
          .fatal(this, "SQLException in getValoreFromParametroByIdCodeNullValue: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in getValoreFromParametroByIdCodeNullValue: "
              + ex.getMessage());
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
            .fatal(
                this,
                "SQLException in getValoreFromParametroByIdCodeNullValue while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception in getValoreFromParametroByIdCodeNullValue while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return valore;
  }
  
  
  

  /**
   * Tipo Forma Conduzione
   * 
   * @return Vector
   * @throws DataAccessException
   */
  public Vector<CodeDescription> getTipoFormaConduzione()
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;

    Vector<CodeDescription> vTipoFormaConduzione = null;
    CodeDescription codDesc = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = " SELECT ID_FORMA_CONDUZIONE, CODICE, FORMA, "
          + " DESCRIZIONE " + " FROM DB_TIPO_FORMA_CONDUZIONE "
          + " ORDER BY CODICE ";

      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        vTipoFormaConduzione = new Vector<CodeDescription>();
        while (rs.next())
        {
          codDesc = new CodeDescription();

          codDesc.setCode(new Integer(rs.getInt("ID_FORMA_CONDUZIONE")));
          codDesc.setSecondaryCode(rs.getString("CODICE"));
          codDesc.setDescription(rs.getString("FORMA") + " - "
              + rs.getString("DESCRIZIONE"));

          vTipoFormaConduzione.add(codDesc);
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "getTipoFormaConduzione - Executed query - Found "
          + vTipoFormaConduzione.size() + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTipoFormaConduzione - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getTipoFormaConduzione - Generic Exception: "
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
                "getTipoFormaConduzione - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getTipoFormaConduzione - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return vTipoFormaConduzione;
  }

  /**
   * Tipo Classi Manodopera
   * 
   * @return Vector
   * @throws DataAccessException
   */
  public Vector<CodeDescription> getTipoClassiManodopera()
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;

    Vector<CodeDescription> vTipoClassiManodopera = null;
    CodeDescription codDesc = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = " SELECT ID_CLASSE_MANODOPERA, CODICE, DESCRIZIONE "
          + " FROM DB_TIPO_CLASSI_MANODOPERA " + " ORDER BY CODICE ";

      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        vTipoClassiManodopera = new Vector<CodeDescription>();
        while (rs.next())
        {
          codDesc = new CodeDescription();

          codDesc.setCode(new Integer(rs.getInt("ID_CLASSE_MANODOPERA")));
          codDesc.setSecondaryCode(rs.getString("CODICE"));
          codDesc.setDescription(rs.getString("DESCRIZIONE"));

          vTipoClassiManodopera.add(codDesc);
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      SolmrLogger.debug(this,
          "getTipoClassiManodopera - Executed query - Found "
              + vTipoClassiManodopera.size() + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTipoClassiManodopera - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getTipoClassiManodopera - Generic Exception: "
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
                "getTipoClassiManodopera - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getTipoClassiManodopera - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return vTipoClassiManodopera;
  }

  public PersonaFisicaVO serviceGetStoricoResidenza(Long idSoggetto,
      java.util.Date date, PersonaFisicaVO pfVO) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    PersonaFisicaVO result = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT " + "  SR.DATA_INIZIO_RESIDENZA, "
          + "  SR.DATA_FINE_RESIDENZA, " + "  SR.INDIRIZZO, " + "  SR.COMUNE, "
          + "  C.DESCOM, " + "  P.SIGLA_PROVINCIA, " + "  P.DESCRIZIONE, "
          + "  SR.CAP, " + "  SR.CITTA_ESTERO " + "FROM  "
          + "  DB_STORICO_RESIDENZA SR, " + "  COMUNE C, " + "  PROVINCIA P "
          + "WHERE " + "  SR.COMUNE=C.ISTAT_COMUNE(+) "
          + "  AND C.ISTAT_PROVINCIA=P.ISTAT_PROVINCIA(+) "
          + "  AND SR.ID_PERSONA_FISICA = ? "
          + "  AND SR.DATA_INIZIO_RESIDENZA <= ? "
          + "  AND SR.DATA_FINE_RESIDENZA >= ?";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);
      java.sql.Date sqlDate = new java.sql.Date(date.getTime());
      stmt.setLong(1, idSoggetto.longValue());
      stmt.setDate(2, sqlDate);
      stmt.setDate(3, sqlDate);
      ResultSet rs = stmt.executeQuery();
      if (SolmrLogger.isDebugEnabled(this))
      {
        SolmrLogger.debug(this, "Query Executed");
      }
      if (rs.next())
      { // Se trovo qualcosa lo metto nel PersonaFisicaVO che viene passato e
        // lo ritorno
        result = pfVO;
        result.setDataInizioResidenza(rs.getDate("DATA_INIZIO_RESIDENZA"));
        result.setDescResProvincia(rs.getString("DESCRIZIONE"));
        result.setDescResComune(rs.getString("DESCOM"));
        result.setResComune(rs.getString("COMUNE"));
        result.setResCAP(rs.getString("CAP"));
        result.setResProvincia(rs.getString("SIGLA_PROVINCIA"));
        result.setResIndirizzo(rs.getString("INDIRIZZO"));
        result.setResCittaEstero(rs.getString("CITTA_ESTERO"));
      } // Altrimenti ritornerò result==null

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "SoggettiDAO.serviceGetStoricoResidenza - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "SoggettiDAO.serviceGetStoricoResidenza - Generic Exception: "
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
                "SoggettiDAO.serviceGetStoricoResidenza - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "SoggettiDAO.serviceGetStoricoResidenza - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  /**
   * Metodo che dato una tipologia di azienda mi dice se devo effettuare i
   * controlli sull'univocità della partita IVA / CUAA o no
   * 
   * @param idTipoAzienda
   *          Integer
   * @throws DataAccessException
   * @return boolean
   */
  public boolean isFlagUnivocitaAzienda(Integer idTipoAzienda)
      throws DataAccessException
  {

    boolean isUnivoca = false;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {

      conn = getDatasource().getConnection();

      String query = "SELECT FLAG_CONTROLLI_UNIVOCITA FROM DB_TIPO_TIPOLOGIA_AZIENDA WHERE ID_TIPOLOGIA_AZIENDA = ? ";

      stmt = conn.prepareStatement(query);
      stmt.setInt(1, idTipoAzienda.intValue());

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        String temp = rs.getString("FLAG_CONTROLLI_UNIVOCITA");
        if ("S".equalsIgnoreCase(temp))
          isUnivoca = true;
      }

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in isFlagUnivocitaAzienda: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in isFlagUnivocitaAzienda: "
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
                "SQLException while closing Statement and Connection in isFlagUnivocitaAzienda: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in isFlagUnivocitaAzienda: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return isUnivoca;
  }

  public DelegaVO getIntermediarioPerDelega(Long idIntermediario)
      throws DataAccessException
  {
    DelegaVO delegaVO = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT I.DENOMINAZIONE,I.INDIRIZZO,P.SIGLA_PROVINCIA,"
          + "C.DESCOM,I.COMUNE AS ISTAT_COMUNE, I.CAP "
          + "FROM DB_INTERMEDIARIO I, COMUNE C, PROVINCIA P "
          + "WHERE I.COMUNE=C.ISTAT_COMUNE(+) "
          + "  AND C.ISTAT_PROVINCIA=P.ISTAT_PROVINCIA(+) "
          + "  AND I.ID_INTERMEDIARIO = ?";

      stmt = conn.prepareStatement(search);

      stmt.setLong(1, idIntermediario.longValue());

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        delegaVO = new DelegaVO();
        delegaVO.setUfficioFascicolo(rs.getString("DENOMINAZIONE"));
        delegaVO.setIndirizzo(rs.getString("INDIRIZZO"));
        delegaVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));
        delegaVO.setDescComune(rs.getString("DESCOM"));
        delegaVO.setIstaComune(rs.getString("ISTAT_COMUNE"));
        delegaVO.setCap(rs.getString("CAP"));
      }

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return delegaVO;
  }

  /**
   * 
   * Restituisce l'intermediario senza utilizzare i servizi di Comune, dato che
   * la tabella è dell'anagrafe.
   * 
   * 
   * @param idIntermediario
   * @return
   * @throws DataAccessException
   */
  public IntermediarioAnagVO getIntermediarioAnagByIdIntermediario(
      long idIntermediario) throws DataAccessException
  {
    IntermediarioAnagVO intermediarioAnagVO = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "" + 
        "SELECT " +
        "       I.ID_INTERMEDIARIO, " + 
        "       I.DENOMINAZIONE," + 
        "       I.CODICE_FISCALE, " + 
        "       I.INDIRIZZO," + 
        "       I.CAP, " + 
        "       I.COMUNE, " +
        "       I.TIPO_INTERMEDIARIO, " +
        "       I.ID_INTERMEDIARIO_PADRE, " +
        "       I.LIVELLO, " +
        "       I.PARTITA_IVA, " +
        "       I.DATA_FINE_VALIDITA, " +
        "       I.RESPONSABILE, " +
        "       I.TELEFONO, " +
        "       I.FAX, " +
        "       I.EMAIL, " +
        "       I.PEC, " +
        "       P.SIGLA_PROVINCIA," + 
        "       C.DESCOM " + 
        "FROM   DB_INTERMEDIARIO I, " + 
        "       COMUNE C, " + 
        "       PROVINCIA P " + 
        "WHERE  I.COMUNE = C.ISTAT_COMUNE(+) " + 
        "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) " +
        "AND    I.ID_INTERMEDIARIO = ?";

      stmt = conn.prepareStatement(search);

      stmt.setLong(1, idIntermediario);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        intermediarioAnagVO = new IntermediarioAnagVO();
        intermediarioAnagVO.setIdIntermediario(rs.getLong("ID_INTERMEDIARIO"));
        intermediarioAnagVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        intermediarioAnagVO.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
        intermediarioAnagVO.setIndirizzo(rs.getString("INDIRIZZO"));
        intermediarioAnagVO.setCap(rs.getString("CAP"));
        intermediarioAnagVO.setComune(rs.getString("COMUNE"));
        intermediarioAnagVO.setTipoIntermediario(rs
            .getString("TIPO_INTERMEDIARIO"));
        intermediarioAnagVO.setIdIntermediarioPadre(checkLongNull(rs
            .getString("ID_INTERMEDIARIO_PADRE")));
        intermediarioAnagVO.setLivello(rs.getString("LIVELLO"));
        intermediarioAnagVO.setPartitaIva(rs.getString("PARTITA_IVA"));
        intermediarioAnagVO.setDataFineValidita(rs
            .getTimestamp("DATA_FINE_VALIDITA"));
        intermediarioAnagVO.setResponsabile(rs.getString("RESPONSABILE"));
        intermediarioAnagVO.setTelefono(rs.getString("TELEFONO"));
        intermediarioAnagVO.setFax(rs.getString("FAX"));
        intermediarioAnagVO.setEmail(rs.getString("EMAIL"));
        intermediarioAnagVO.setPec(rs.getString("PEC"));
        intermediarioAnagVO.setSglProv(rs.getString("SIGLA_PROVINCIA"));
        intermediarioAnagVO.setDesCom(rs.getString("DESCOM"));
      }

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return intermediarioAnagVO;
  }
  
  
  public IntermediarioAnagVO findIntermediarioVOByCodiceEnte(String codEnte) 
     throws DataAccessException
  {
    IntermediarioAnagVO intermediarioAnagVO = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "" + 
        "SELECT " +
        "       I.ID_INTERMEDIARIO, " + 
        "       I.DENOMINAZIONE," + 
        "       I.CODICE_FISCALE, " + 
        "       I.INDIRIZZO," + 
        "       I.CAP, " + 
        "       I.COMUNE, " +
        "       I.TIPO_INTERMEDIARIO, " +
        "       I.ID_INTERMEDIARIO_PADRE, " +
        "       I.LIVELLO, " +
        "       I.PARTITA_IVA, " +
        "       I.DATA_FINE_VALIDITA, " +
        "       I.RESPONSABILE, " +
        "       I.TELEFONO, " +
        "       I.FAX, " +
        "       I.EMAIL, " +
        "       I.PEC, " +
        "       P.SIGLA_PROVINCIA," + 
        "       C.DESCOM, " +
        "       I.EXT_ID_AZIENDA, " +
        "       I.EXT_CUAA " + 
        "FROM   DB_INTERMEDIARIO I, " + 
        "       COMUNE C, " + 
        "       PROVINCIA P " + 
        "WHERE  I.COMUNE = C.ISTAT_COMUNE(+) " + 
        "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) " +
        "AND    I.CODICE_FISCALE = ? " +
        "AND    I.DATA_FINE_VALIDITA IS NULL ";

      stmt = conn.prepareStatement(search);

      stmt.setString(1, codEnte);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        intermediarioAnagVO = new IntermediarioAnagVO();
        intermediarioAnagVO.setIdIntermediario(rs.getLong("ID_INTERMEDIARIO"));
        intermediarioAnagVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        intermediarioAnagVO.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
        intermediarioAnagVO.setIndirizzo(rs.getString("INDIRIZZO"));
        intermediarioAnagVO.setCap(rs.getString("CAP"));
        intermediarioAnagVO.setComune(rs.getString("COMUNE"));
        intermediarioAnagVO.setTipoIntermediario(rs
            .getString("TIPO_INTERMEDIARIO"));
        intermediarioAnagVO.setIdIntermediarioPadre(checkLongNull(rs
            .getString("ID_INTERMEDIARIO_PADRE")));
        intermediarioAnagVO.setLivello(rs.getString("LIVELLO"));
        intermediarioAnagVO.setPartitaIva(rs.getString("PARTITA_IVA"));
        intermediarioAnagVO.setDataFineValidita(rs
            .getTimestamp("DATA_FINE_VALIDITA"));
        intermediarioAnagVO.setResponsabile(rs.getString("RESPONSABILE"));
        intermediarioAnagVO.setTelefono(rs.getString("TELEFONO"));
        intermediarioAnagVO.setFax(rs.getString("FAX"));
        intermediarioAnagVO.setEmail(rs.getString("EMAIL"));
        intermediarioAnagVO.setPec(rs.getString("PEC"));
        intermediarioAnagVO.setSglProv(rs.getString("SIGLA_PROVINCIA"));
        intermediarioAnagVO.setDesCom(rs.getString("DESCOM"));
        intermediarioAnagVO.setExtIdAzienda(checkLongNull(rs.getString("EXT_ID_AZIENDA")));
        intermediarioAnagVO.setExtCuaa(rs.getString("EXT_CUAA"));
      }

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return intermediarioAnagVO;
  }
  
  
  public IntermediarioAnagVO findIntermediarioVOByIdAzienda(long idAzienda) 
      throws DataAccessException
  {
    IntermediarioAnagVO intermediarioAnagVO = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "" + 
        "SELECT " +
        "       I.ID_INTERMEDIARIO, " + 
        "       I.DENOMINAZIONE," + 
        "       I.CODICE_FISCALE, " + 
        "       I.INDIRIZZO," + 
        "       I.CAP, " + 
        "       I.COMUNE, " +
        "       I.TIPO_INTERMEDIARIO, " +
        "       I.ID_INTERMEDIARIO_PADRE, " +
        "       I.LIVELLO, " +
        "       I.PARTITA_IVA, " +
        "       I.DATA_FINE_VALIDITA, " +
        "       I.RESPONSABILE, " +
        "       I.TELEFONO, " +
        "       I.FAX, " +
        "       I.EMAIL, " +
        "       I.PEC, " +
        "       P.SIGLA_PROVINCIA," + 
        "       C.DESCOM, " +
        "       I.EXT_ID_AZIENDA, " +
        "       I.EXT_CUAA " + 
        "FROM   DB_INTERMEDIARIO I, " + 
        "       COMUNE C, " + 
        "       PROVINCIA P " + 
        "WHERE  I.COMUNE = C.ISTAT_COMUNE(+) " + 
        "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) " +
        "AND    I.EXT_ID_AZIENDA = ? " +
        "AND    I.DATA_FINE_VALIDITA IS NULL ";

      stmt = conn.prepareStatement(search);

      stmt.setLong(1, idAzienda);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        intermediarioAnagVO = new IntermediarioAnagVO();
        intermediarioAnagVO.setIdIntermediario(rs.getLong("ID_INTERMEDIARIO"));
        intermediarioAnagVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        intermediarioAnagVO.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
        intermediarioAnagVO.setIndirizzo(rs.getString("INDIRIZZO"));
        intermediarioAnagVO.setCap(rs.getString("CAP"));
        intermediarioAnagVO.setComune(rs.getString("COMUNE"));
        intermediarioAnagVO.setTipoIntermediario(rs
           .getString("TIPO_INTERMEDIARIO"));
        intermediarioAnagVO.setIdIntermediarioPadre(checkLongNull(rs
            .getString("ID_INTERMEDIARIO_PADRE")));
        intermediarioAnagVO.setLivello(rs.getString("LIVELLO"));
        intermediarioAnagVO.setPartitaIva(rs.getString("PARTITA_IVA"));
        intermediarioAnagVO.setDataFineValidita(rs
            .getTimestamp("DATA_FINE_VALIDITA"));
        intermediarioAnagVO.setResponsabile(rs.getString("RESPONSABILE"));
        intermediarioAnagVO.setTelefono(rs.getString("TELEFONO"));
        intermediarioAnagVO.setFax(rs.getString("FAX"));
        intermediarioAnagVO.setEmail(rs.getString("EMAIL"));
        intermediarioAnagVO.setPec(rs.getString("PEC"));
        intermediarioAnagVO.setSglProv(rs.getString("SIGLA_PROVINCIA"));
        intermediarioAnagVO.setDesCom(rs.getString("DESCOM"));
        intermediarioAnagVO.setExtIdAzienda(checkLongNull(rs.getString("EXT_ID_AZIENDA")));
        intermediarioAnagVO.setExtCuaa(rs.getString("EXT_CUAA"));
      }

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return intermediarioAnagVO;
  }
  
  
  public boolean isAziendaIntermediario(long idAzienda) 
      throws DataAccessException
  {
    boolean trovato = false;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "" + 
        "SELECT I.ID_INTERMEDIARIO " + 
        "FROM   DB_INTERMEDIARIO I " + 
        "WHERE  I.EXT_ID_AZIENDA = ? " ;

      stmt = conn.prepareStatement(search);

      stmt.setLong(1, idAzienda);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        trovato = true;
      }

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return trovato;
  }
  
  
  public Vector<IntermediarioAnagVO> findFigliIntermediarioVOById(long idIntemediario) 
     throws DataAccessException
  {
    IntermediarioAnagVO intermediarioAnagVO = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<IntermediarioAnagVO> vIntermediari = null; 
    try
    {
      conn = getDatasource().getConnection();

      String search = "" + 
        "SELECT I.ID_INTERMEDIARIO, " + 
        "       I.DENOMINAZIONE," + 
        "       I.CODICE_FISCALE, " + 
        "       I.INDIRIZZO," + 
        "       I.CAP, " + 
        "       I.COMUNE, " +
        "       I.TIPO_INTERMEDIARIO, " +
        "       I.ID_INTERMEDIARIO_PADRE, " +
        "       I.LIVELLO, " +
        "       I.PARTITA_IVA, " +
        "       I.DATA_FINE_VALIDITA, " +
        "       I.RESPONSABILE, " +
        "       I.TELEFONO, " +
        "       I.FAX, " +
        "       I.EMAIL, " +
        "       I.PEC, " +
        "       P.SIGLA_PROVINCIA," + 
        "       C.DESCOM, " +
        "       I.EXT_ID_AZIENDA, " +
        "       I.EXT_CUAA " + 
        "FROM   DB_INTERMEDIARIO I, " + 
        "       COMUNE C, " + 
        "       PROVINCIA P " + 
        "WHERE  I.COMUNE = C.ISTAT_COMUNE(+) " + 
        "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) " +
        "AND    I.ID_INTERMEDIARIO_PADRE = ? " +
        "AND    I.DATA_FINE_VALIDITA IS NULL " +
        "ORDER BY I.DENOMINAZIONE ";

      stmt = conn.prepareStatement(search);
      stmt.setLong(1, idIntemediario);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        if(vIntermediari == null)
        {
          vIntermediari = new Vector<IntermediarioAnagVO>();
        }
        
        intermediarioAnagVO = new IntermediarioAnagVO();
        intermediarioAnagVO.setIdIntermediario(rs.getLong("ID_INTERMEDIARIO"));
        intermediarioAnagVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        intermediarioAnagVO.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
        intermediarioAnagVO.setIndirizzo(rs.getString("INDIRIZZO"));
        intermediarioAnagVO.setCap(rs.getString("CAP"));
        intermediarioAnagVO.setComune(rs.getString("COMUNE"));
        intermediarioAnagVO.setTipoIntermediario(rs
            .getString("TIPO_INTERMEDIARIO"));
        intermediarioAnagVO.setIdIntermediarioPadre(checkLongNull(rs
            .getString("ID_INTERMEDIARIO_PADRE")));
        intermediarioAnagVO.setLivello(rs.getString("LIVELLO"));
        intermediarioAnagVO.setPartitaIva(rs.getString("PARTITA_IVA"));
        intermediarioAnagVO.setDataFineValidita(rs
            .getTimestamp("DATA_FINE_VALIDITA"));
        intermediarioAnagVO.setResponsabile(rs.getString("RESPONSABILE"));
        intermediarioAnagVO.setTelefono(rs.getString("TELEFONO"));
        intermediarioAnagVO.setFax(rs.getString("FAX"));
        intermediarioAnagVO.setEmail(rs.getString("EMAIL"));
        intermediarioAnagVO.setPec(rs.getString("PEC"));
        intermediarioAnagVO.setSglProv(rs.getString("SIGLA_PROVINCIA"));
        intermediarioAnagVO.setDesCom(rs.getString("DESCOM"));
        intermediarioAnagVO.setExtIdAzienda(checkLongNull(rs.getString("EXT_ID_AZIENDA")));
        intermediarioAnagVO.setExtCuaa(rs.getString("EXT_CUAA"));
        
        vIntermediari.add(intermediarioAnagVO);
      }

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return vIntermediari;
  }
  
  
  public Vector<IntermediarioAnagVO> findFigliIntermediarioAziendaVOById(long idIntemediario) 
      throws DataAccessException
  {
    IntermediarioAnagVO intermediarioAnagVO = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<IntermediarioAnagVO> vIntermediari = null; 
    try
    {
      conn = getDatasource().getConnection();

      String search = "" + 
        "SELECT I.ID_INTERMEDIARIO, " + 
        "       I.DENOMINAZIONE," + 
        "       I.CODICE_FISCALE, " + 
        "       I.INDIRIZZO," + 
        "       I.CAP, " + 
        "       I.COMUNE, " +
        "       I.TIPO_INTERMEDIARIO, " +
        "       I.ID_INTERMEDIARIO_PADRE, " +
        "       I.LIVELLO, " +
        "       I.PARTITA_IVA, " +
        "       I.DATA_FINE_VALIDITA, " +
        "       I.RESPONSABILE, " +
        "       I.TELEFONO, " +
        "       I.FAX, " +
        "       I.EMAIL, " +
        "       I.PEC, " +
        "       P.SIGLA_PROVINCIA," + 
        "       C.DESCOM, " +
        "       I.EXT_ID_AZIENDA, " +
        "       I.EXT_CUAA " + 
        "FROM   DB_INTERMEDIARIO I, " + 
        "       COMUNE C, " + 
        "       PROVINCIA P " + 
        "WHERE  I.COMUNE = C.ISTAT_COMUNE(+) " + 
        "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) " +
        "AND    I.ID_INTERMEDIARIO_PADRE = ? " +
        "AND    I.DATA_FINE_VALIDITA IS NULL " +
        "ANd    I.EXT_ID_AZIENDA IS NOT NULL " +
        "ORDER BY I.DENOMINAZIONE ";

      stmt = conn.prepareStatement(search);
      stmt.setLong(1, idIntemediario);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        if(vIntermediari == null)
        {
          vIntermediari = new Vector<IntermediarioAnagVO>();
        }
         
        intermediarioAnagVO = new IntermediarioAnagVO();
        intermediarioAnagVO.setIdIntermediario(rs.getLong("ID_INTERMEDIARIO"));
        intermediarioAnagVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        intermediarioAnagVO.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
        intermediarioAnagVO.setIndirizzo(rs.getString("INDIRIZZO"));
        intermediarioAnagVO.setCap(rs.getString("CAP"));
        intermediarioAnagVO.setComune(rs.getString("COMUNE"));
        intermediarioAnagVO.setTipoIntermediario(rs
           .getString("TIPO_INTERMEDIARIO"));
        intermediarioAnagVO.setIdIntermediarioPadre(checkLongNull(rs
            .getString("ID_INTERMEDIARIO_PADRE")));
        intermediarioAnagVO.setLivello(rs.getString("LIVELLO"));
        intermediarioAnagVO.setPartitaIva(rs.getString("PARTITA_IVA"));
        intermediarioAnagVO.setDataFineValidita(rs
            .getTimestamp("DATA_FINE_VALIDITA"));
        intermediarioAnagVO.setResponsabile(rs.getString("RESPONSABILE"));
        intermediarioAnagVO.setTelefono(rs.getString("TELEFONO"));
        intermediarioAnagVO.setFax(rs.getString("FAX"));
        intermediarioAnagVO.setEmail(rs.getString("EMAIL"));
        intermediarioAnagVO.setPec(rs.getString("PEC"));
        intermediarioAnagVO.setSglProv(rs.getString("SIGLA_PROVINCIA"));
        intermediarioAnagVO.setDesCom(rs.getString("DESCOM"));
        intermediarioAnagVO.setExtIdAzienda(checkLongNull(rs.getString("EXT_ID_AZIENDA")));
        intermediarioAnagVO.setExtCuaa(rs.getString("EXT_CUAA"));
        
        vIntermediari.add(intermediarioAnagVO);
      }

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return vIntermediari;
  }
  
  
  

  // Metodo per recuperare i tipi ufficio zona intermediario
  public Vector<CodeDescription> getElencoUfficiZonaIntermediarioByIdIntermediario(
      UtenteAbilitazioni utenteAbilitazioni) throws DataAccessException
  {
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = null;
      // Se l'intermediario connesso ha livello = "Z"
      if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
          (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_UFFICIO_ZONA")))
      {
        search = " SELECT   UZI.ID_UFFICIO_ZONA_INTERMEDIARIO, "
            + "          UZI.CODICE_AGEA, " + "          C.DESCOM, "
            + "          UZI.INDIRIZZO "
            + " FROM     DB_UFFICIO_ZONA_INTERMEDIARIO UZI, "
            + "          COMUNE C " + " WHERE    UZI.CODICE_AGEA = ? "
            + " AND      C.ISTAT_COMUNE = UZI.COMUNE "
            + " ORDER BY UZI.CODICE_AGEA ";
      }
      // Se l'intermediario connesso ha livello = "P"
      else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
          (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_PROVINCIALE")))
      {
        search = " SELECT   UZI.ID_UFFICIO_ZONA_INTERMEDIARIO, "
            + "          UZI.CODICE_AGEA, " + "          C.DESCOM, "
            + "          UZI.INDIRIZZO "
            + " FROM     DB_UFFICIO_ZONA_INTERMEDIARIO UZI, "
            + "          COMUNE C "
            + " WHERE    SUBSTR(UZI.CODICE_AGEA, 1, 6) = ? "
            + " AND      C.ISTAT_COMUNE = UZI.COMUNE "
            + " ORDER BY UZI.CODICE_AGEA ";
      }
      // Se l'intermediario connesso ha livello = "R"
      else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
          (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_REGIONALE")))
      {
        search = " SELECT   UZI.ID_UFFICIO_ZONA_INTERMEDIARIO, "
            + "          UZI.CODICE_AGEA, " + "          C.DESCOM, "
            + "          UZI.INDIRIZZO "
            + " FROM     DB_UFFICIO_ZONA_INTERMEDIARIO UZI, "
            + "          COMUNE C "
            + " WHERE    SUBSTR(UZI.CODICE_AGEA, 1, 3) = ? "
            + " AND      C.ISTAT_COMUNE = UZI.COMUNE "
            + " ORDER BY UZI.CODICE_AGEA ";
      }

      stmt = conn.prepareStatement(search);

      // Se l'intermediario connesso ha livello = "Z"
      if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
          (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_UFFICIO_ZONA")))
      {
        stmt.setString(1, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getCodiceEnte());
      }
      // Se l'intermediario connesso ha livello = "P"
      else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
          (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_PROVINCIALE")))
      {
        stmt.setString(1, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getCodiceEnte()
            .substring(0, 6));
      }
      // Se l'intermediario connesso ha livello = "R"
      else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
          (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_REGIONALE")))
      {
        stmt.setString(1, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getCodiceEnte()
            .substring(0, 3));
      }

      SolmrLogger.debug(this,
          "Executing query getElencoUfficiZonaIntermediarioByIdIntermediario: "
              + search);

      ResultSet rs = stmt.executeQuery();

      result = new Vector<CodeDescription>();
      while (rs.next())
      {
        CodeDescription cd = new CodeDescription();
        cd.setCode(new Integer(rs.getInt(1)));
        cd.setDescription(rs.getString(2) + " - " + rs.getString(3) + " - "
            + rs.getString(4));
        result.add(cd);
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this,
          "Executed query getElencoUfficiZonaIntermediarioByIdIntermediario - Found "
              + result.size() + " result(s).");

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "SQLException getElencoUfficiZonaIntermediarioByIdIntermediario : "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in getElencoUfficiZonaIntermediarioByIdIntermediario: "
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
                "SQLException while closing Statement and Connection in getElencoUfficiZonaIntermediarioByIdIntermediario: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in getElencoUfficiZonaIntermediarioByIdIntermediario: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  
  public Vector<CodeDescription> getElencoUfficiZonaIntermediarioAttiviByIdIntermediario(
      UtenteAbilitazioni utenteAbilitazioni) throws DataAccessException
  {
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = null;
      // Se l'intermediario connesso ha livello = "Z"
      if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
          (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_UFFICIO_ZONA")))
      {
        search = " SELECT   UZI.ID_UFFICIO_ZONA_INTERMEDIARIO, "
            + "          UZI.CODICE_AGEA, " 
            + "          C.DESCOM, "
            + "          UZI.INDIRIZZO "
            + " FROM     DB_UFFICIO_ZONA_INTERMEDIARIO UZI, "
            + "          DB_INTERMEDIARIO I, "
            + "          COMUNE C " 
            + " WHERE    UZI.CODICE_AGEA = ? "
            + " AND      C.ISTAT_COMUNE = UZI.COMUNE "
            + " AND      UZI.ID_INTERMEDIARIO = I.ID_INTERMEDIARIO " 
            + " AND      I.DATA_FINE_VALIDITA IS NULL "
            + " ORDER BY UZI.CODICE_AGEA ";
      }
      // Se l'intermediario connesso ha livello = "P"
      else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
          (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_PROVINCIALE")))
      {
        search = " SELECT   UZI.ID_UFFICIO_ZONA_INTERMEDIARIO, "
            + "          UZI.CODICE_AGEA, " 
            + "          C.DESCOM, "
            + "          UZI.INDIRIZZO "
            + " FROM     DB_UFFICIO_ZONA_INTERMEDIARIO UZI, "
            + "          DB_INTERMEDIARIO I, "
            + "          COMUNE C "
            + " WHERE    SUBSTR(UZI.CODICE_AGEA, 1, 6) = ? "
            + " AND      C.ISTAT_COMUNE = UZI.COMUNE "
            + " AND      UZI.ID_INTERMEDIARIO = I.ID_INTERMEDIARIO " 
            + " AND      I.DATA_FINE_VALIDITA IS NULL "
            + " ORDER BY UZI.CODICE_AGEA ";
      }
      // Se l'intermediario connesso ha livello = "R"
      else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
          (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_REGIONALE")))
      {
        search = " SELECT   UZI.ID_UFFICIO_ZONA_INTERMEDIARIO, "
            + "          UZI.CODICE_AGEA, " 
            + "          C.DESCOM, "
            + "          UZI.INDIRIZZO "
            + " FROM     DB_UFFICIO_ZONA_INTERMEDIARIO UZI, "
            + "          DB_INTERMEDIARIO I, "
            + "          COMUNE C "
            + " WHERE    SUBSTR(UZI.CODICE_AGEA, 1, 3) = ? "
            + " AND      C.ISTAT_COMUNE = UZI.COMUNE "
            + " AND      UZI.ID_INTERMEDIARIO = I.ID_INTERMEDIARIO " 
            + " AND      I.DATA_FINE_VALIDITA IS NULL "
            + " ORDER BY UZI.CODICE_AGEA ";
      }

      stmt = conn.prepareStatement(search);

      // Se l'intermediario connesso ha livello = "Z"
      if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
          (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_UFFICIO_ZONA")))
      {
        stmt.setString(1, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getCodiceEnte());
      }
      // Se l'intermediario connesso ha livello = "P"
      else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
          (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_PROVINCIALE")))
      {
        stmt.setString(1, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getCodiceEnte()
            .substring(0, 6));
      }
      // Se l'intermediario connesso ha livello = "R"
      else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
          (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_REGIONALE")))
      {
        stmt.setString(1, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getCodiceEnte()
            .substring(0, 3));
      }

      SolmrLogger.debug(this,
          "Executing query getElencoUfficiZonaIntermediarioAttiviByIdIntermediario: "
              + search);

      ResultSet rs = stmt.executeQuery();

      result = new Vector<CodeDescription>();
      while (rs.next())
      {
        CodeDescription cd = new CodeDescription();
        cd.setCode(new Integer(rs.getInt(1)));
        cd.setDescription(rs.getString(2) + " - " + rs.getString(3) + " - "
            + rs.getString(4));
        result.add(cd);
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this,
          "Executed query getElencoUfficiZonaIntermediarioAttiviByIdIntermediario - Found "
              + result.size() + " result(s).");

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "SQLException getElencoUfficiZonaIntermediarioAttiviByIdIntermediario : "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in getElencoUfficiZonaIntermediarioAttiviByIdIntermediario: "
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
                "SQLException while closing Statement and Connection in getElencoUfficiZonaIntermediarioAttiviByIdIntermediario: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in getElencoUfficiZonaIntermediarioAttiviByIdIntermediario: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  /**
   * Questo metodo, dato un dato CUAA, ricerca tutti le aziende che prentano
   * quel CUAA e la cui data di fine validità è null. Restituisce un vettore
   * contenente gli id dell'aziende trovate e l'eventuale data di cessazione. Se
   * non trova nessun record restituisce null
   * 
   * @param CUAA
   *          String
   * @throws DataAccessException
   * @return CodeDescription[]
   */
  public CodeDescription[] getIdAziendaCUAA(String CUAA)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<CodeDescription> idAzienda = new Vector<CodeDescription>();

    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT A.ID_AZIENDA,A.DATA_CESSAZIONE "
          + "FROM DB_ANAGRAFICA_AZIENDA A "
          + "WHERE A.DATA_FINE_VALIDITA IS NULL " + "AND A.CUAA = UPPER(?) "
          + "ORDER BY A.DATA_CESSAZIONE NULLS LAST";

      SolmrLogger.debug(this, "Executing query: " + search);

      stmt = conn.prepareStatement(search);

      stmt.setString(1, CUAA);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        CodeDescription codeDescription = new CodeDescription();
        codeDescription.setCode(new Integer(rs.getInt("ID_AZIENDA")));
        codeDescription.setDescription(rs.getString("DATA_CESSAZIONE"));
        idAzienda.add(codeDescription);
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getIdAziendaCUAA - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getIdAziendaCUAA - Generic Exception: " + ex);
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
            "getIdAziendaCUAA - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "getIdAziendaCUAA - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return idAzienda.size() == 0 ? null : (CodeDescription[]) idAzienda
        .toArray(new CodeDescription[0]);
  }

  /**
   * Questo metodo, dato un codice fiscale ed un idAzienda controlla che il
   * codiceFiscale passato abbia un ruolo attivo nell'azienda con id passato. Se
   * controllaLegameRLsuAnagrafe è true controlla che la persona sia
   * rappresentante legale di tali aziende in anagrafe (id_ruolo =1) oppure che
   * la persona abbia un ruolo attivo (diverso da titolare/rappr.legale) con
   * flag_accesso_forzato = ‘S’
   * 
   * @param codiceFiscale
   *          String
   * @param idAzienda
   *          long
   * @param controllaLegameRLsuAnagrafe
   *          boolean
   * @throws DataAccessException
   * @return boolean
   */
  public boolean isRuolosuAnagrafe(String codiceFiscale, long idAzienda,
      boolean controllaLegameRLsuAnagrafe) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    boolean result = false;

    try
    {
      conn = getDatasource().getConnection();

      String query = null;

      String search = "SELECT * "
          + "FROM DB_ANAGRAFICA_AZIENDA A,DB_CONTITOLARE C, DB_PERSONA_FISICA P "
          + "WHERE P.CODICE_FISCALE = UPPER(?) " + "  AND A.ID_AZIENDA = ?"
          + "  AND A.ID_AZIENDA = C.ID_AZIENDA "
          + "  AND C.ID_SOGGETTO = P.ID_SOGGETTO "
          + "  AND C.DATA_FINE_RUOLO IS NULL "
          + "  AND A.DATA_FINE_VALIDITA IS NULL ";
      if (controllaLegameRLsuAnagrafe)
        query = search + "  AND C.ID_RUOLO = ? ";
      else
        query = search;

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt = conn.prepareStatement(query);

      stmt.setString(1, codiceFiscale);
      stmt.setLong(2, idAzienda);
      if (controllaLegameRLsuAnagrafe)
        stmt.setLong(3, new Long(SolmrConstants.TIPORUOLO_TITOL_RAPPR_LEG)
            .longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        result = true;
      rs.close();

      if (controllaLegameRLsuAnagrafe && !result)
      {
        // Devo controllare che la persona abbia un ruolo attivo (diverso da
        // titolare/rappr.legale) con flag_accesso_forzato = ‘S’
        query = search + " AND C.FLAG_ACCESSO_FORZATO = 'S'";
        SolmrLogger.debug(this, "Executing query: " + query);

        stmt = conn.prepareStatement(query);

        stmt.setString(1, codiceFiscale);
        stmt.setLong(2, idAzienda);

        rs = stmt.executeQuery();

        if (rs.next())
          result = true;
        rs.close();
      }
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "isRuolosuAnagrafe - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "isRuolosuAnagrafe - Generic Exception: " + ex);
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
            "isRuolosuAnagrafe - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "isRuolosuAnagrafe - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  /**
   * Questo metodo dato un idAzienda controlla che sia presente per
   * quell'azienda un record su db_dichiarazione_consistenza con data >= del
   * valore del parametro DICH (su db_parametro)
   * 
   * @param idAzienda
   *          long
   * @throws DataAccessException
   * @return boolean
   */
  public boolean isPresenzaDichiarazione(long idAzienda)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    boolean result = false;

    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT * " + "FROM DB_DICHIARAZIONE_CONSISTENZA DC "
          + "WHERE DC.ID_AZIENDA = ? " + "AND DC.DATA >= " + "("
          + "  SELECT TO_DATE(P.VALORE,'dd/mm/yyyy') "
          + "  FROM DB_PARAMETRO P" + "  WHERE P.ID_PARAMETRO = ?" + ")";

      SolmrLogger.debug(this, "Executing query: " + search);

      stmt = conn.prepareStatement(search);

      stmt.setLong(1, idAzienda);
      stmt.setString(2, ((String) SolmrConstants.get("ID_PARAMETRO_DICH"))
          .toUpperCase());

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        result = true;

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "isPresenzaDichiarazione - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "isPresenzaDichiarazione - Generic Exception: "
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
                "isPresenzaDichiarazione - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "isPresenzaDichiarazione - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // Ricerca tutti i documenti associati ad un determianto controllo
  public Vector<CodeDescription> getDocumentiByIdControllo(Long idControllo)
      throws DataAccessException
  {
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT TD.ID_DOCUMENTO,TD.DESCRIZIONE "
          + "FROM DB_TIPO_DOCUMENTO TD,DB_DOCUMENTO_CONTROLLO DC "
          + "WHERE TD.ID_DOCUMENTO=DC.EXT_ID_DOCUMENTO "
          + "  AND DC.ID_CONTROLLO = ? " + "ORDER BY TD.DESCRIZIONE";

      stmt = conn.prepareStatement(search);

      stmt.setLong(1, idControllo.longValue());

      SolmrLogger.debug(this, "getDocumentiByIdControllo Executing query: "
          + search);

      ResultSet rs = stmt.executeQuery();

      result = new Vector<CodeDescription>();

      while (rs.next())
      {
        CodeDescription codeDescVO = new CodeDescription();
        codeDescVO.setCode(new Integer(rs.getInt("ID_DOCUMENTO")));
        codeDescVO.setDescription(rs.getString("DESCRIZIONE"));
        result.add(codeDescVO);
      }

      rs.close();

      SolmrLogger.debug(this,
          "getDocumentiByIdControllo Executed query - Found " + result.size()
              + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getDocumentiByIdControllo SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getDocumentiByIdControllo Generic Exception: "
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
                "getDocumentiByIdControllo SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getDocumentiByIdControllo Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  /**
   * Questo metodo, dato un codice fiscale ed un idAzienda controlla che il
   * codiceFiscale passato abbia un ruolo nell'azienda con id passato.
   * 
   * @param codiceFiscale
   *          String
   * @param idAzienda
   *          long
   * @throws DataAccessException
   * @return boolean
   */
  public boolean isRuolosuAnagrafe(String codiceFiscale, long idAzienda)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    boolean result = false;

    try
    {
      conn = getDatasource().getConnection();
      String query = "SELECT * "
          + "FROM DB_CONTITOLARE C, DB_PERSONA_FISICA P "
          + "WHERE P.CODICE_FISCALE = UPPER(?) " + "  AND C.ID_AZIENDA = ?"
          + "  AND C.ID_SOGGETTO = P.ID_SOGGETTO ";

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt = conn.prepareStatement(query);

      stmt.setString(1, codiceFiscale);
      stmt.setLong(2, idAzienda);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        result = true;
      rs.close();

      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "isRuolosuAnagrafe - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "isRuolosuAnagrafe - Generic Exception: " + ex);
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
            "isRuolosuAnagrafe - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "isRuolosuAnagrafe - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  /**
   * Per evidenziare eventuali differenze, confrontare i soggetti estratti dal
   * servizio AAEP con quelli di anagrafe con ruolo attivo per l’azienda in
   * esame (db_contitolare.data_fine_ruolo a null) a parità di codice fiscale.
   * Nel caso in cui in anagrafe il soggetto sia legato all’azienda con diversi
   * ruoli attivi, estendere il confronto a codice fiscale e ruolo. Se il
   * soggetto non è legato all’azienda con il ruolo di AAEP evidenziare la
   * differenza del ruolo. Sulla tabella db_tipo_ruolo è presente la colonna
   * codice_AAEP che contiene la colonna CaricaPersonaInfoc.codiceCarica per
   * decodificare la carica proveniente da AAEP con il ruolo di Anagrafe.
   * 
   * @param codiceFiscale
   *          String
   * @param idAzienda
   *          long
   * @param codRuoloAAEP
   *          String
   * @throws DataAccessException
   * @return PersonaFisicaVO
   */
  public PersonaFisicaVO getRuolosuAnagrafe(String codiceFiscale,
      long idAzienda, String codRuoloAAEP) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    PersonaFisicaVO personaFisicaVO = null;

    try
    {
      conn = getDatasource().getConnection();
      String query = "SELECT P.CODICE_FISCALE,TR.CODICE_AAEP,P.COGNOME,P.NOME,P.SESSO, "
          + "TO_CHAR(P.NASCITA_DATA,'dd/mm/yyyy')    AS NASCITA_DATA,P.NASCITA_COMUNE,"
          + "TO_CHAR(C.DATA_INIZIO_RUOLO,'dd/mm/yyyy')    AS DATA_INIZIO_RUOLO,"
          + "TO_CHAR(C.DATA_FINE_RUOLO,'dd/mm/yyyy')    AS DATA_FINE_RUOLO,"
          + "P.RES_INDIRIZZO,P.RES_COMUNE,P.RES_CAP,TR.DESCRIZIONE AS DESC_RUOLO, "
          + "NAS.DESCOM AS DESC_NAS_COMUNE,RES.DESCOM AS DESC_RES_COMUNE "
          + "FROM DB_CONTITOLARE C, DB_PERSONA_FISICA P, DB_TIPO_RUOLO TR, "
          + "COMUNE NAS, COMUNE RES "
          + "WHERE P.CODICE_FISCALE = UPPER(?) "
          + "  AND C.ID_AZIENDA = ?"
          + "  AND C.ID_SOGGETTO = P.ID_SOGGETTO "
          + "  AND C.DATA_FINE_RUOLO IS NULL "
          + "  AND C.ID_RUOLO=TR.ID_RUOLO "
          + "  AND P.NASCITA_COMUNE=NAS.ISTAT_COMUNE(+) "
          + "  AND P.RES_COMUNE=RES.ISTAT_COMUNE(+) ";

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt = conn.prepareStatement(query);

      stmt.setString(1, codiceFiscale);
      stmt.setLong(2, idAzienda);

      ResultSet rs = stmt.executeQuery();

      int numRuoliAttivi = 0;

      while (rs.next())
      {
        numRuoliAttivi++;
        personaFisicaVO = new PersonaFisicaVO();
        personaFisicaVO.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
        personaFisicaVO.setCodiceRuoloAAEP(rs.getString("CODICE_AAEP"));
        personaFisicaVO.setCognome(rs.getString("COGNOME"));
        personaFisicaVO.setNome(rs.getString("NOME"));
        personaFisicaVO.setSesso(rs.getString("SESSO"));
        personaFisicaVO.setStrNascitaData(rs.getString("NASCITA_DATA"));
        personaFisicaVO.setNascitaComune(rs.getString("NASCITA_COMUNE"));
        personaFisicaVO
            .setStrDataInizioRuolo(rs.getString("DATA_INIZIO_RUOLO"));
        personaFisicaVO.setStrDataFineRuolo(rs.getString("DATA_FINE_RUOLO"));
        personaFisicaVO.setResIndirizzo(rs.getString("RES_INDIRIZZO"));
        personaFisicaVO.setResComune(rs.getString("RES_COMUNE"));
        personaFisicaVO.setResCAP(rs.getString("RES_CAP"));
        personaFisicaVO.setRuolo(rs.getString("DESC_RUOLO"));
        personaFisicaVO.setDescNascitaComune(rs.getString("DESC_NAS_COMUNE"));
        personaFisicaVO.setDescResComune(rs.getString("DESC_RES_COMUNE"));
      }
      rs.close();
      stmt.close();

      if (numRuoliAttivi > 1)
      {
        // devo rieseguire la query aggiungendo la join del ruolo di AAEP
        query += " AND TR.CODICE_AAEP = ?";

        stmt = conn.prepareStatement(query);

        stmt.setString(1, codiceFiscale);
        stmt.setLong(2, idAzienda);
        stmt.setString(3, codRuoloAAEP);

        rs = stmt.executeQuery();

        if (rs.next())
        {
          personaFisicaVO.setCodiceRuoloAAEP(rs.getString("CODICE_AAEP"));
          personaFisicaVO.setStrDataInizioRuolo(rs
              .getString("DATA_INIZIO_RUOLO"));
          personaFisicaVO.setStrDataFineRuolo(rs.getString("DATA_FINE_RUOLO"));
          personaFisicaVO.setRuolo(rs.getString("DESC_RUOLO"));
        }
        else
        {
          personaFisicaVO.setCodiceRuoloAAEP(null);
          personaFisicaVO.setStrDataInizioRuolo(null);
          personaFisicaVO.setStrDataFineRuolo(null);
          personaFisicaVO.setRuolo(null);
        }

        rs.close();
        stmt.close();
      }

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getRuolosuAnagrafe - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getRuolosuAnagrafe - Generic Exception: " + ex);
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
            "getRuolosuAnagrafe - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getRuolosuAnagrafe - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return personaFisicaVO;
  }

  // Metodo per sapere se un soggetto esiste o meno in anagarfe
  public Long getSoggettoInAnagrafe(String codFiscale)
      throws DataAccessException
  {
    Long result = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT ID_SOGGETTO " + "FROM   DB_PERSONA_FISICA "
          + "WHERE  UPPER(CODICE_FISCALE) = ? ";

      stmt = conn.prepareStatement(query);

      stmt.setString(1, codFiscale.toUpperCase());

      SolmrLogger.debug(this, "Executing query: " + query);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        result = new Long(rs.getLong("ID_SOGGETTO"));
      }

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "isSoggettoInAnagrafe - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this, "isSoggettoInAnagrafe - Generic Exception: "
          + ex.getMessage());
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
        SolmrLogger.error(this,
            "isSoggettoInAnagrafe - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "isSoggettoInAnagrafe - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  /**
   * Va a cercare se un rappresentante legale restituito dal sian è presente su
   * anagrafe per una determinata azienda
   * 
   * @param codiceFiscale
   *          String
   * @param idAzienda
   *          long
   * @throws DataAccessException
   * @return boolean restituisce true se trova almeno un record, false se non
   *         trova niente
   */

  public boolean getRuolosuAnagrafe(String codiceFiscale, long idAzienda)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    boolean result = false;

    try
    {
      conn = getDatasource().getConnection();
      String query = "SELECT * "
          + "FROM DB_CONTITOLARE C, DB_PERSONA_FISICA P "
          + "WHERE P.CODICE_FISCALE = UPPER(?) " + "  AND C.ID_AZIENDA = ?"
          + "  AND C.ID_SOGGETTO = P.ID_SOGGETTO ";

      SolmrLogger.debug(this, "Executing query getRuolosuAnagrafe: " + query);

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [CODICE_FISCALE] in getRuolosuAnagrafe method in CommonDAO: "
                  + codiceFiscale + "\n");
      SolmrLogger.debug(this,
          "Value of parameter 2 [ID_AZIENDA] in getRuolosuAnagrafe method in CommonDAO: "
              + idAzienda + "\n");

      stmt = conn.prepareStatement(query);

      stmt.setString(1, codiceFiscale);
      stmt.setLong(2, idAzienda);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        result = true;

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getRuolosuAnagrafe - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getRuolosuAnagrafe - Generic Exception: " + ex);
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
            "getRuolosuAnagrafe - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getRuolosuAnagrafe - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // Metodo per recuperare l'istat di un comune data la sua descrizione
  public String getIstatByDescComune(String descComune)
      throws DataAccessException
  {

    String istat = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT C.ISTAT_COMUNE " + "FROM COMUNE C "
          + "WHERE UPPER(C.DESCOM) = ? ";

      stmt = conn.prepareStatement(query);

      stmt.setString(1, descComune.toUpperCase());

      SolmrLogger.debug(this, "Executing query: " + query);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        istat = (rs.getString("ISTAT_COMUNE"));

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getIstatByDescComune - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getIstatByDescComune - Generic Exception: "
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
            "getIstatByDescComune - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getIstatByDescComune - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return istat;
  }

  // Metodo per sapere se un soggetto esiste o meno in anagarfe
  public Long getDecodificaRuoloAAEP(String codRuoloAAEP)
      throws DataAccessException
  {

    Long result = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT TR.ID_RUOLO " + "FROM DB_TIPO_RUOLO TR "
          + "WHERE TR.CODICE_AAEP = ? ";

      stmt = conn.prepareStatement(query);

      stmt.setString(1, codRuoloAAEP.toUpperCase());

      SolmrLogger.debug(this, "Executing query: " + query);
      SolmrLogger.debug(this, "Executing query codRuoloAAEP: " + codRuoloAAEP);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        result = new Long(rs.getLong("ID_RUOLO"));

      SolmrLogger.debug(this, "Executing query result: " + result);

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getDecodificaRuoloAAEP - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getDecodificaRuoloAAEP - Generic Exception: "
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
                "getDecodificaRuoloAAEP - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getDecodificaRuoloAAEP - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  /**
   * Se trova in anagrafe un soggetto con lo stesso ruolo restituisce la data di
   * inizio ruolo... se non lo trova restituisce null
   * 
   * @param idRuolo
   *          Long
   * @param idSoggetto
   *          Long
   * @throws DataAccessException
   * @return String
   */
  public String confrontaRuoloAAEPconAnagrafe(Long idRuolo, Long idSoggetto)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    String result = null;

    try
    {
      conn = getDatasource().getConnection();
      String query = "SELECT TO_CHAR(DATA_INIZIO_RUOLO,'dd/mm/yyyy') AS DATA_INIZIO_RUOLO "
          + "FROM DB_CONTITOLARE "
          + "WHERE ID_SOGGETTO=? "
          + "AND ID_RUOLO = ?";

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idSoggetto.longValue());
      stmt.setLong(2, idRuolo.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        result = rs.getString("DATA_INIZIO_RUOLO");

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "confrontaRuoloAAEPconAnagrafe - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "confrontaRuoloAAEPconAnagrafe - Generic Exception: " + ex);
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
                "confrontaRuoloAAEPconAnagrafe - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "confrontaRuoloAAEPconAnagrafe - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  /**
   * Metodo per recuperare l'elenco dei mandati e delle validazioni, da inizio
   * anno al giorno corrente, a partire dall'utente (Modificato per unificazione
   * dei report 'Riepilogo mandati' e 'Riepilogo validazioni')
   * 
   */
  public Vector<DelegaVO> getMandatiByUtente(UtenteAbilitazioni utenteAbilitazioni,
      boolean forZona, java.util.Date dataDal, java.util.Date dataAl)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<DelegaVO> elencoMandati = null;
    String query = null;
    SimpleDateFormat sdf = new SimpleDateFormat(
        SolmrConstants.DATE_FORMAT_4REPORT);

    try
    {
      conn = getDatasource().getConnection();
      // CASI UTENTE INTERMEDIARIO
      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        // CASO INTERMEDIARIO CON LIVELLO "Z" CIOE' UFFICI SPECIFICI DI ZONA
        if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_UFFICIO_ZONA")))
        {
          query = " SELECT   DISTINCT   I.DENOMINAZIONE,  "
              + "             I.INDIRIZZO,  "
              + "             TO_CHAR(I.CODICE_FISCALE,'999G999G999')CODICE_FISCALE,    "
              + "       C.DESCOM,  "
              + "       P.SIGLA_PROVINCIA,   "
              + "        nvl(DELEGHE.num_deleghe,'0'),    "
              + "        nvl(DICHIARAZIONI_FATTE.num_dichiarazioni,'0')          "
              + "    FROM     DB_INTERMEDIARIO I,    "
              + "       COMUNE C,    "
              + "       PROVINCIA P,     "
              + "        (SELECT ID_INTERMEDIARIO, COUNT(distinct de.id_azienda) num_deleghe    "
              + "      FROM DB_DELEGA DE , DB_ANAGRAFICA_AZIENDA AA      "
              + "      WHERE de.id_procedimento = ?  "
              + "      and de.id_azienda = aa.id_azienda  "
              + "       and aa.data_fine_validita is null  "
              + "       AND NVL(TRUNC(AA.DATA_CESSAZIONE), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > ? "
              // Vecchio + "         AND TRUNC(DE.DATA_INIZIO) <= ? "
              + "         AND TRUNC(DE.DATA_INIZIO) >= ? "
              + "       AND NVL(TRUNC(DE.DATA_FINE), TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? "
              + "      GROUP BY ID_INTERMEDIARIO    "
              + "        ) DELEGHE,    "
              + "        (SELECT d.id_intermediario, COUNT(d.ID_AZIENDA) num_dichiarazioni    "
              + "            FROM  DB_DELEGA D,    "
              + "            DB_ANAGRAFICA_AZIENDA AA,    "
              + "       (SELECT ID_AZIENDA, MAX(DATA) AS DATA_ULTIMA_DICHIARAZIONE "
              + "            FROM DB_DICHIARAZIONE_CONSISTENZA "
              + "            WHERE TRUNC(DATA) >= ? "
              + "            AND TRUNC(DATA) <= ? "
              + "            GROUP BY ID_AZIENDA) DC "
              + "            WHERE  D.id_azienda = AA.id_azienda    "
              + "           AND AA.data_fine_validita IS NULL    "
              + "           and d.id_procedimento = ?        "
              + "           AND DC.id_azienda = D.id_azienda  "
              + "           AND NVL(D.DATA_FINE,SYSDATE)>=DC.DATA_ULTIMA_DICHIARAZIONE "
              + "           AND NVL(AA.DATA_CESSAZIONE,SYSDATE)>=DC.DATA_ULTIMA_DICHIARAZIONE "
              + "           AND TRUNC(DC.DATA_ULTIMA_DICHIARAZIONE) >= ?       "
              + "           AND TRUNC(DC.DATA_ULTIMA_DICHIARAZIONE) <= ?     "
              + "           GROUP BY D.id_intermediario    "
              + "      ) DICHIARAZIONI_FATTE    "
              + "   WHERE    I.ID_INTERMEDIARIO = ?      "
              + "           AND NVL(TRUNC(I.DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? "
              + "   and i.TIPO_INTERMEDIARIO = 'C'    "
              + "   and dichiarazioni_fatte.id_intermediario(+) = i.id_intermediario    "
              + "   and deleghe.id_intermediario = i.id_intermediario    "
              + "   AND      I.COMUNE = C.ISTAT_COMUNE     "
              + "   AND      C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA";

        }
        // CASO INTERMEDIARIO CON LIVELLO "P" CIOE' PROVINCIALE
        else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_PROVINCIALE")))
        {
          query = "  SELECT   DISTINCT      I.DENOMINAZIONE,  "
              + "  I.INDIRIZZO,      "
              + "  TO_CHAR(I.CODICE_FISCALE,'999G999G999')CODICE_FISCALE,    "
              + "  C.DESCOM,     "
              + "  P.SIGLA_PROVINCIA,       "
              + "  nvl(DELEGHE.num_deleghe,'0'),    "
              + "  nvl(DICHIARAZIONI_FATTE.num_dichiarazioni,'0')          "
              + "  FROM     DB_DELEGA D,         "
              + "  DB_INTERMEDIARIO I,    "
              + "  COMUNE C,    "
              + "  PROVINCIA P,    "
              + "  (SELECT ID_INTERMEDIARIO, COUNT(distinct de.id_azienda) num_deleghe      "
              + "            FROM DB_DELEGA DE   , DB_ANAGRAFICA_AZIENDA AA   "
              + "         WHERE de.id_procedimento = ?  "
              + "        and de.id_azienda = aa.id_azienda  "
              + "       and aa.data_fine_validita is null  "
              + "                           AND NVL(TRUNC(AA.DATA_CESSAZIONE), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > ? "
              // Vecchio + "         AND TRUNC(DE.DATA_INIZIO) <= ? "
              + "         AND TRUNC(DE.DATA_INIZIO) >= ? "
              + "                           AND NVL(TRUNC(DE.DATA_FINE), TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? "
              + "         GROUP BY ID_INTERMEDIARIO      "
              + "   ) DELEGHE,      "
              + "  (SELECT d.id_intermediario, COUNT(d.ID_AZIENDA) num_dichiarazioni      "
              + "            FROM  DB_DELEGA D,      "
              + "            DB_ANAGRAFICA_AZIENDA AA,      "
              + "       (SELECT ID_AZIENDA, MAX(DATA) AS DATA_ULTIMA_DICHIARAZIONE "
              + "            FROM DB_DICHIARAZIONE_CONSISTENZA "
              + "            WHERE TRUNC(DATA) >= ? "
              + "            AND TRUNC(DATA) <= ? "
              + "            GROUP BY ID_AZIENDA) DC "
              + "            WHERE  D.id_azienda = AA.id_azienda    "
              + "           AND AA.data_fine_validita IS NULL      "
              + "           AND NVL(D.DATA_FINE,SYSDATE)>=DC.DATA_ULTIMA_DICHIARAZIONE "
              + "           AND NVL(AA.DATA_CESSAZIONE,SYSDATE)>=DC.DATA_ULTIMA_DICHIARAZIONE "
              + "           and d.id_procedimento = ?         "
              + "           AND DC.id_azienda = D.id_azienda  "
              + "           AND TRUNC(DC.DATA_ULTIMA_DICHIARAZIONE) >= ?     "
              + "           AND TRUNC(DC.DATA_ULTIMA_DICHIARAZIONE) <= ?      "
              + "           GROUP BY D.id_intermediario      "
              + " ) DICHIARAZIONI_FATTE       "
              + " WHERE    D.ID_INTERMEDIARIO IN         "
              + "        (SELECT ID_INTERMEDIARIO         "
              + "         FROM   DB_INTERMEDIARIO         "
              + "         WHERE  SUBSTR(CODICE_FISCALE, 1, 6) = ? )    "
              + " AND      D.ID_INTERMEDIARIO = I.ID_INTERMEDIARIO        "
              + " AND NVL(TRUNC(I.DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? "
              + " AND      D.ID_PROCEDIMENTO = ?        "
              + " and i.TIPO_INTERMEDIARIO = 'C'      "
              + " and dichiarazioni_fatte.id_intermediario(+) = i.id_intermediario      "
              + " and deleghe.id_intermediario = i.id_intermediario    "
              + " AND      I.COMUNE = C.ISTAT_COMUNE     "
              + " AND      C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA  ";
        }
        // CASO INTERMEDIARIO CON LIVELLO "R" CIOE' REGIONALE
        else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_REGIONALE")))
        {
          // CASO NON PER UFFICIO DI ZONA
          if (!forZona)
          {
            query = " SELECT i.denominazione ,  "
                + "  I.INDIRIZZO,    "
                + "  C.DESCOM,    "
                + "  P.SIGLA_PROVINCIA,     "
                + "  TO_CHAR(I.CODICE_FISCALE,'999G999G999')CODICE_FISCALE ,   "
                + "  nvl(DELEGHE.num_deleghe,'0'),    "
                + "  nvl(DICHIARAZIONI_FATTE.num_dichiarazioni,'0')          "
                + "  FROM DB_INTERMEDIARIO I,    "
                + "    COMUNE C,    "
                + "    PROVINCIA P,    "
                + "    (SELECT substr(inte.codice_fiscale,0,6) intermediario, COUNT(distinct de.id_azienda) num_deleghe   "
                + "       FROM DB_DELEGA DE, db_intermediario inte,DB_ANAGRAFICA_AZIENDA AA    "
                + "    WHERE de.id_procedimento = ?     "
                + "    and de.id_intermediario = inte.id_intermediario   "
                + "    and de.id_azienda = aa.id_azienda  "
                + "    and aa.data_fine_validita is null  "
                + "         AND NVL(TRUNC(AA.DATA_CESSAZIONE), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > ? "
                // Vecchio + "         AND TRUNC(DE.DATA_INIZIO) <= ? "
                + "         AND TRUNC(DE.DATA_INIZIO) >= ? "
                + "         AND NVL(TRUNC(DE.DATA_FINE), TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? "
                + "    GROUP BY substr(inte.codice_fiscale,0,6)    "
                + "    ) DELEGHE,    "
                + "      (SELECT substr(I2.codice_fiscale,0,6) intermediario, COUNT(DISTINCT d.ID_AZIENDA || I2.ID_INTERMEDIARIO ) num_dichiarazioni  "
                + "       FROM  DB_DELEGA D,    "
                + "       DB_ANAGRAFICA_AZIENDA AA,    "
                + "       (SELECT ID_AZIENDA, MAX(DATA) AS DATA_ULTIMA_DICHIARAZIONE "
                + "            FROM DB_DICHIARAZIONE_CONSISTENZA "
                + "            WHERE TRUNC(DATA) >= ? "
                + "            AND TRUNC(DATA) <= ? "
                + "            GROUP BY ID_AZIENDA) DC, "
                + "    DB_INTERMEDIARIO I2    "
                + "      WHERE  D.id_azienda = AA.id_azienda    "
                + "      AND D.id_intermediario = I2.id_intermediario   "
                + "      AND AA.data_fine_validita IS NULL    "
                + "      AND NVL(D.DATA_FINE,SYSDATE)>=DC.DATA_ULTIMA_DICHIARAZIONE "
                + "      AND NVL(AA.DATA_CESSAZIONE,SYSDATE)>=DC.DATA_ULTIMA_DICHIARAZIONE "
                + "      and d.id_procedimento = ?        "
                + "      AND DC.id_azienda = D.id_azienda    "
                + "      AND TRUNC(DC.DATA_ULTIMA_DICHIARAZIONE) >= ?       "
                + "      AND TRUNC(DC.DATA_ULTIMA_DICHIARAZIONE) <= ?     "
                + "      GROUP BY substr(I2.codice_fiscale,0,6)    "
                + "      ) DICHIARAZIONI_FATTE      "
                + "  WHERE SUBSTR(I.CODICE_FISCALE, 4, 3) = P.ISTAT_PROVINCIA    "
                + "  and SUBSTR(I.CODICE_FISCALE, 1, 3) = ?    "
                + "  AND I.COMUNE = C.ISTAT_COMUNE    "
                + "  AND NVL(TRUNC(I.DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? "
                + "  and i.TIPO_INTERMEDIARIO = 'C'    "
                + "  and dichiarazioni_fatte.intermediario(+)||'000' = i.codice_fiscale    "
                + "  and deleghe.intermediario||'000' = i.codice_fiscale";
          }
          // CASO UFFICI DI ZONA SPECIFICATI
          else
          {
            query = " SELECT   DISTINCT       I.DENOMINAZIONE,   "
                + "  I.INDIRIZZO,     "
                + "  C.DESCOM,     "
                + "  P.SIGLA_PROVINCIA,    "
                + "  TO_CHAR(I.CODICE_FISCALE,'999G999G999')CODICE_FISCALE,     "
                + "  nvl(DELEGHE.num_deleghe,'0'),    "
                + "  nvl(DICHIARAZIONI_FATTE.num_dichiarazioni,'0')          "
                + " FROM     DB_DELEGA D,        "
                + "  DB_INTERMEDIARIO I,      "
                + "  COMUNE C,        "
                + "  PROVINCIA P,      "
                + "  (SELECT ID_INTERMEDIARIO, COUNT(distinct de.id_azienda) num_deleghe      "
                + "           FROM DB_DELEGA DE,DB_ANAGRAFICA_AZIENDA AA    "
                + "        WHERE de.id_procedimento = ? "
                + "        and de.id_azienda = aa.id_azienda "
                + "       and aa.data_fine_validita is null "
                + "                         AND NVL(TRUNC(AA.DATA_CESSAZIONE), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > ? "
                // Vecchio + "         AND TRUNC(DE.DATA_INIZIO) <= ? "
                + "         AND TRUNC(DE.DATA_INIZIO) >= ? "
                + "                         AND NVL(TRUNC(DE.DATA_FINE), TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? "
                + "        GROUP BY ID_INTERMEDIARIO       "
                + " ) DELEGHE,       "
                + " (SELECT d.id_intermediario, COUNT(DISTINCT d.ID_AZIENDA) num_dichiarazioni       "
                + "           FROM  DB_DELEGA D,       "
                + "           DB_ANAGRAFICA_AZIENDA AA,       "
                + "           (SELECT ID_AZIENDA, MAX(DATA) AS DATA_ULTIMA_DICHIARAZIONE "
                + "            FROM DB_DICHIARAZIONE_CONSISTENZA "
                + "            WHERE TRUNC(DATA) >= ? "
                + "            AND TRUNC(DATA) <= ? "
                + "            GROUP BY ID_AZIENDA) DC "
                + "           WHERE  D.id_azienda = AA.id_azienda     "
                + "          AND AA.data_fine_validita IS NULL   "
                + "          AND NVL(D.DATA_FINE,SYSDATE)>=DC.DATA_ULTIMA_DICHIARAZIONE "
                + "          AND NVL(AA.DATA_CESSAZIONE,SYSDATE)>=DC.DATA_ULTIMA_DICHIARAZIONE "
                + "          and d.id_procedimento = ?          "
                + "          AND DC.id_azienda = D.id_azienda     "
                + "          AND TRUNC(DC.DATA_ULTIMA_DICHIARAZIONE) >= ?    "
                + "          AND TRUNC(DC.DATA_ULTIMA_DICHIARAZIONE) <= ?    "
                + "          GROUP BY D.id_intermediario       "
                + "  ) DICHIARAZIONI_FATTE        "
                + " WHERE    D.ID_INTERMEDIARIO IN        "
                + "     (SELECT ID_INTERMEDIARIO        "
                + "       FROM   DB_INTERMEDIARIO        "
                + "       WHERE  SUBSTR(CODICE_FISCALE, 1, 3) = ?)   "
                + "   AND      D.ID_INTERMEDIARIO = I.ID_INTERMEDIARIO    "
                + "   AND NVL(TRUNC(I.DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? "
                + " AND      D.ID_PROCEDIMENTO = ?        "
                + " AND      I.COMUNE = C.ISTAT_COMUNE        "
                + " AND      C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA   "
                + " AND i.TIPO_INTERMEDIARIO = 'C'    "
                + " AND dichiarazioni_fatte.id_intermediario(+) = i.id_intermediario    "
                + " AND deleghe.id_intermediario = i.id_intermediario ";
          }
        }
      }
      // CASO DI UTENTE APPARTENENTE ALLA PA(PROVINCIALI E REGIONALI)
      else
      {
        // CASO RECORD NON DIVISI PER PROVINCIA
        if (!forZona)
        {
          query = " SELECT DISTINCT     i.denominazione,  "
              + " TO_CHAR(I.CODICE_FISCALE,'999G999G999')CODICE_FISCALE ,     "
              + "   nvl(deleghe.num_deleghe,'0'),   "
              + "   nvl(dichiarazioni_fatte.num_dichiarazioni,'0')   "
              + " FROM DB_INTERMEDIARIO I,    "
              + "   PROVINCIA P,    "
              + "   COMUNE C,    "
              + " (SELECT substr(inte.codice_fiscale,1,3) intermediario, COUNT(DISTINCT DE.ID_AZIENDA) num_deleghe   "
              + "         FROM DB_DELEGA DE, db_intermediario inte, db_anagrafica_azienda az "
              + "      WHERE de.id_procedimento = ?      "
              + "      and de.id_intermediario = inte.id_intermediario   "
              + "      AND NVL(TRUNC(INTE.DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? "
              //+ "      AND TRUNC(DE.DATA_INIZIO) <= ? "
              + "      AND TRUNC(DE.DATA_INIZIO) >= ? "
              + "      AND NVL(TRUNC(DE.DATA_FINE), TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? "
              + "      and az.id_azienda = de.id_azienda "
              + "      and az.data_fine_validita is null "
              + "      AND NVL(TRUNC(AZ.DATA_CESSAZIONE), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > ? "
              + "      GROUP BY substr(inte.codice_fiscale,1,3)   "
              + "   ) DELEGHE,    "
              + "     (SELECT substr(I2.codice_fiscale,1,3) intermediario, COUNT(d.ID_AZIENDA) num_dichiarazioni   "
              + "      FROM  DB_DELEGA D,    "
              + "      DB_ANAGRAFICA_AZIENDA AA,    "
              + "      (SELECT ID_AZIENDA, MAX(DATA) AS DATA_ULTIMA_DICHIARAZIONE "
              + "            FROM DB_DICHIARAZIONE_CONSISTENZA "
              + "            WHERE TRUNC(DATA) >= ? "
              + "            AND TRUNC(DATA) <= ? "
              + "            GROUP BY ID_AZIENDA) DC, "
              + "   DB_INTERMEDIARIO I2    "
              + "      WHERE  D.id_azienda = AA.id_azienda    "
              + "   AND D.id_intermediario = I2.id_intermediario    "
              + "     AND AA.data_fine_validita IS NULL    "
              + "     and d.id_procedimento = ?       "
              + "     AND DC.id_azienda = D.id_azienda    "
              + "     AND NVL(D.DATA_FINE,SYSDATE)>=DC.DATA_ULTIMA_DICHIARAZIONE "
              + "     AND NVL(AA.DATA_CESSAZIONE,SYSDATE)>=DC.DATA_ULTIMA_DICHIARAZIONE "
              + "     AND             TRUNC(DC.DATA_ULTIMA_DICHIARAZIONE) >= ?        "
              + "     AND             TRUNC(DC.DATA_ULTIMA_DICHIARAZIONE) <= ?     "
              + "     GROUP BY substr(I2.codice_fiscale,1,3)    "
              + "     ) DICHIARAZIONI_FATTE      "
              + " WHERE NVL(TRUNC(I.DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? "
              + " and i.TIPO_INTERMEDIARIO = 'C'    "
              + " AND I.COMUNE = C.ISTAT_COMUNE    "
              + " and dichiarazioni_fatte.intermediario(+)||'000'||'000' = i.codice_fiscale    "
              + " and deleghe.intermediario||'000'||'000' = i.codice_fiscale";
        }
        // CASO RECORD DIVISI PER PROVINCIA
        else
        {
          query = " SELECT DISTINCT i.denominazione, "
              + "       P.SIGLA_PROVINCIA, "
              + "  TO_CHAR(I.CODICE_FISCALE,'999G999G999')CODICE_FISCALE ,  "
              + "   nvl(deleghe.num_deleghe,'0'),   "
              + "   nvl(dichiarazioni_fatte.num_dichiarazioni,'0')   "
              + "  FROM DB_INTERMEDIARIO I, "
              + "    PROVINCIA P, "
              + "    COMUNE C, "
              + "    (SELECT substr(inte.codice_fiscale,1,6) intermediario, COUNT(DISTINCT DE.ID_AZIENDA) num_deleghe    "
              + "             FROM DB_DELEGA DE, db_intermediario inte, db_anagrafica_azienda az "
              + "          WHERE de.id_procedimento = ?      "
              + "          and de.id_intermediario = inte.id_intermediario   "
              + "          AND NVL(TRUNC(INTE.DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? "
              // Vecchio + "         AND TRUNC(DE.DATA_INIZIO) <= ? "
              + "         AND TRUNC(DE.DATA_INIZIO) >= ? "
              + "          AND NVL(TRUNC(DE.DATA_FINE), TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? "
              + "          and az.id_azienda = de.id_azienda "
              + "          and az.data_fine_validita is null "
              + "          AND NVL(TRUNC(AZ.DATA_CESSAZIONE), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > ? "
              + "          GROUP BY substr(inte.codice_fiscale,1,6)   "
              + "    ) DELEGHE, "
              + "      (SELECT substr(I2.codice_fiscale,0,6) intermediario, COUNT(d.ID_AZIENDA) num_dichiarazioni "
              + "       FROM  DB_DELEGA D, "
              + "       DB_ANAGRAFICA_AZIENDA AA, "
              + "      (SELECT ID_AZIENDA, MAX(DATA) AS DATA_ULTIMA_DICHIARAZIONE "
              + "            FROM DB_DICHIARAZIONE_CONSISTENZA "
              + "            WHERE TRUNC(DATA) >= ? "
              + "            AND TRUNC(DATA) <= ? "
              + "            GROUP BY ID_AZIENDA) DC, "
              + "    DB_INTERMEDIARIO I2 "
              + "       WHERE  D.id_azienda = AA.id_azienda "
              + "    AND D.id_intermediario = I2.id_intermediario "
              + "      AND AA.data_fine_validita IS NULL "
              + "      and d.id_procedimento = ?     "
              + "      AND DC.id_azienda = D.id_azienda "
              + "      AND NVL(D.DATA_FINE,SYSDATE)>=DC.DATA_ULTIMA_DICHIARAZIONE "
              + "      AND NVL(AA.DATA_CESSAZIONE,SYSDATE)>=DC.DATA_ULTIMA_DICHIARAZIONE "
              + "      AND             TRUNC(DC.DATA_ULTIMA_DICHIARAZIONE) >= ?     "
              + "      AND             TRUNC(DC.DATA_ULTIMA_DICHIARAZIONE) <= ?  "
              + "      GROUP BY substr(I2.codice_fiscale,0,6) "
              + "      ) DICHIARAZIONI_FATTE   "
              + "  WHERE SUBSTR(I.CODICE_FISCALE, 4, 3) = P.ISTAT_PROVINCIA "
              + "       AND NVL(TRUNC(I.DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? "
              + "  and i.TIPO_INTERMEDIARIO = 'C' "
              + "       and C.ISTAT_PROVINCIA(+) = P.ISTAT_PROVINCIA  "
              + "  and dichiarazioni_fatte.intermediario(+)||'000' = i.codice_fiscale "
              + "  and deleghe.intermediario||'000' = i.codice_fiscale";
        }
      }
      if (conn == null || conn.isClosed())
        conn = getDatasource().getConnection();

      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      // CASI PROFILO INTERMEDIARIO
      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        // CASO INTERMEDIARIO CON LIVELLO "Z" CIOE' UFFICI SPECIFICI DI ZONA
        if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_UFFICIO_ZONA")))
        {
          int indice = 0;
          stmt.setLong(++indice, Long.parseLong((String) SolmrConstants
              .get("ID_TIPO_PROCEDIMENTO_ANAG")));
          stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
          stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
          stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
          stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
          stmt.setDate(++indice, new java.sql.Date(dataAl.getTime()));
          stmt.setLong(++indice, Long.parseLong((String) SolmrConstants
              .get("ID_TIPO_PROCEDIMENTO_ANAG")));
          stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
          stmt.setDate(++indice, new java.sql.Date(dataAl.getTime()));
          stmt.setLong(++indice, utenteAbilitazioni.getEnteAppartenenza().getIntermediario()
              .getIdIntermediario());
          stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
        }
        // CASO INTERMEDIARIO CON LIVELLO "P" CIOE' PROVINCIALE
        else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_PROVINCIALE")))
        {
          int indice = 0;
          stmt.setLong(++indice, Long.parseLong((String) SolmrConstants
              .get("ID_TIPO_PROCEDIMENTO_ANAG")));
          stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
          stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
          stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
          stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
          stmt.setDate(++indice, new java.sql.Date(dataAl.getTime()));
          stmt.setLong(++indice, Long.parseLong((String) SolmrConstants
              .get("ID_TIPO_PROCEDIMENTO_ANAG")));
          stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
          stmt.setDate(++indice, new java.sql.Date(dataAl.getTime()));
          stmt.setString(++indice, utenteAbilitazioni.getEnteAppartenenza().getIntermediario()
              .getCodiceEnte().substring(0, 6));
          stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
          stmt.setLong(++indice, Long.parseLong((String) SolmrConstants
              .get("ID_TIPO_PROCEDIMENTO_ANAG")));
        }
        // CASO INTERMEDIARIO CON LIVELLO "R" CIOE' REGIONALE
        else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_REGIONALE")))
        {
          if (!forZona)
          {            
            int indice = 0;
            stmt.setLong(++indice, Long.parseLong((String) SolmrConstants
                .get("ID_TIPO_PROCEDIMENTO_ANAG")));
            SolmrLogger.debug(this, "ID_PROCEDIMENTO " + SolmrConstants
            .get("ID_TIPO_PROCEDIMENTO_ANAG"));
            stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
            SolmrLogger.debug(this, "DATA DAL " + dataDal);
            stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
            SolmrLogger.debug(this, "DATA DAL " + dataDal);
            stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
            SolmrLogger.debug(this, "DATA DAL " + dataDal);
            stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
            SolmrLogger.debug(this, "DATA DAL " + dataDal);
            stmt.setDate(++indice, new java.sql.Date(dataAl.getTime()));
            SolmrLogger.debug(this, "DATA AL " + dataAl);
            stmt.setLong(++indice, Long.parseLong((String) SolmrConstants
                .get("ID_TIPO_PROCEDIMENTO_ANAG")));
            SolmrLogger.debug(this, "ID_PROCEDIMENTO " + SolmrConstants
                .get("ID_TIPO_PROCEDIMENTO_ANAG"));
            stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
            SolmrLogger.debug(this, "DATA DAL " + dataDal);
            stmt.setDate(++indice, new java.sql.Date(dataAl.getTime()));
            SolmrLogger.debug(this, "DATA AL " + dataAl);
            stmt.setString(++indice, utenteAbilitazioni.getEnteAppartenenza().getIntermediario()
                .getCodiceEnte().substring(0, 3));
            SolmrLogger.debug(this, "INTERMEDIARIO " + utenteAbilitazioni.getEnteAppartenenza().getIntermediario()
                .getCodiceEnte().substring(0, 3));
            stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
            SolmrLogger.debug(this, "DATA DAL " + dataDal);
          }
          else
          {
            int indice = 0;
            stmt.setLong(++indice, Long.parseLong((String) SolmrConstants
                .get("ID_TIPO_PROCEDIMENTO_ANAG")));
            stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
            stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
            stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
            stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
            stmt.setDate(++indice, new java.sql.Date(dataAl.getTime()));
            stmt.setLong(++indice, Long.parseLong((String) SolmrConstants
                .get("ID_TIPO_PROCEDIMENTO_ANAG")));
            stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
            stmt.setDate(++indice, new java.sql.Date(dataAl.getTime()));
            stmt.setString(++indice, utenteAbilitazioni.getEnteAppartenenza().getIntermediario()
                .getCodiceEnte().substring(0, 3));
            stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
            stmt.setLong(++indice, Long.parseLong((String) SolmrConstants
                .get("ID_TIPO_PROCEDIMENTO_ANAG")));
          }
        }
      }
      // CASO DI UTENTE APPARTENENTE ALLA PA(PROVINCIALI E REGIONALI)
      else
      {
        if (!forZona)
        {
          int indice = 0;
          stmt.setLong(++indice, Long.parseLong((String) SolmrConstants
              .get("ID_TIPO_PROCEDIMENTO_ANAG")));
          stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
          stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
          stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
          stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
          stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
          stmt.setDate(++indice, new java.sql.Date(dataAl.getTime()));
          stmt.setLong(++indice, Long.parseLong((String) SolmrConstants
              .get("ID_TIPO_PROCEDIMENTO_ANAG")));
          stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
          stmt.setDate(++indice, new java.sql.Date(dataAl.getTime()));
          stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
        }
        else
        {
          int indice = 0;
          stmt.setLong(++indice, Long.parseLong((String) SolmrConstants
              .get("ID_TIPO_PROCEDIMENTO_ANAG")));
          stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
          stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
          stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
          stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
          stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
          stmt.setDate(++indice, new java.sql.Date(dataAl.getTime()));
          stmt.setLong(++indice, Long.parseLong((String) SolmrConstants
              .get("ID_TIPO_PROCEDIMENTO_ANAG")));
          stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
          stmt.setDate(++indice, new java.sql.Date(dataAl.getTime()));
          stmt.setDate(++indice, new java.sql.Date(dataDal.getTime()));
        }
      }

      SolmrLogger.debug(this, "Executing query getMandatiByUtente: " + query);

      ResultSet rs = stmt.executeQuery();
      elencoMandati = new Vector<DelegaVO>();
      while (rs.next())
      {
        DelegaVO delegaVO = new DelegaVO();
        ElencoMandatiValidazioniFiltroExcelVO elFiltro = new ElencoMandatiValidazioniFiltroExcelVO();
        // CASI PROFILO INTERMEDIARIO
        if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
        {
          // CASO INTERMEDIARIO CON LIVELLO "Z" CIOE' UFFICI SPECIFICI DI ZONA
          if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello()
              .equalsIgnoreCase(
                  (String) SolmrConstants
                      .get("LIVELLO_INTERMEDIARIO_UFFICIO_ZONA")))
          {
            SolmrLogger.debug(this,
                "INTERMEDIARIO CON LIVELLO 'Z' CIOE' UFFICI SPECIFICI DI ZONA");
            delegaVO.setDenomIntermediario(rs.getString(1));
            delegaVO.setDenominazioneExcel(rs.getString(1));
            delegaVO.setIndirizzo(rs.getString(2));
            delegaVO.setIndirizzoExcel(rs.getString(2));
            delegaVO.setCodiceFiscaleIntermediario(rs.getString(3));
            delegaVO.setCodiceAgeaExcel(rs.getString(3));
            delegaVO.setDescComune(rs.getString(4));
            delegaVO.setComuneExcel(rs.getString(4));
            delegaVO.setSiglaProvincia(rs.getString(5));
            delegaVO.setProvinciaExcel(rs.getString(5));
            delegaVO.setTotaleMandati(Integer.decode(rs.getString(6)));
            delegaVO.setTotaleMandatiExcel(Integer.decode(rs.getString(6)));
            delegaVO.setTotaleMandatiValidati(Integer.decode(rs.getString(7)));
            delegaVO.setTotaleMandatiValidatiExcel(Integer.decode(rs
                .getString(7)));
            elFiltro.setDataRiferimentoValidazione(sdf.format(dataDal), sdf
                .format(dataAl));
            delegaVO.setElencoMandatiValidazioniFiltroEvcelVO(elFiltro);
          }
          // CASO INTERMEDIARIO CON LIVELLO "P" CIOE' PROVINCIALE
          else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
              (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_PROVINCIALE")))
          {
            SolmrLogger.debug(this,
                "INTERMEDIARIO CON LIVELLO 'P' CIOE' PROVINCIALE");
            delegaVO.setDenomIntermediario(rs.getString(1));
            delegaVO.setDenominazioneExcel(rs.getString(1));
            delegaVO.setIndirizzo(rs.getString(2));
            delegaVO.setIndirizzoExcel(rs.getString(2));
            delegaVO.setCodiceFiscaleIntermediario(rs.getString(3));
            delegaVO.setCodiceAgeaExcel(rs.getString(3));
            delegaVO.setDescComune(rs.getString(4));
            delegaVO.setComuneExcel(rs.getString(4));
            delegaVO.setSiglaProvincia(rs.getString(5));
            delegaVO.setProvinciaExcel(rs.getString(5));
            delegaVO.setTotaleMandati(Integer.decode(rs.getString(6)));
            delegaVO.setTotaleMandatiExcel(Integer.decode(rs.getString(6)));
            delegaVO.setTotaleMandatiValidati(Integer.decode(rs.getString(7)));
            delegaVO.setTotaleMandatiValidatiExcel(Integer.decode(rs
                .getString(7)));
            elFiltro.setDataRiferimentoValidazione(sdf.format(dataDal), sdf
                .format(dataAl));
            delegaVO.setElencoMandatiValidazioniFiltroEvcelVO(elFiltro);
          }
          // CASO INTERMEDIARIO CON LIVELLO "R" CIOE' REGIONALE
          else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
              (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_REGIONALE")))
          {
            // CASO NON PER UFFICIO DI ZONA
            if (!forZona)
            {
              SolmrLogger
                  .debug(this,
                      "INTERMEDIARIO CON LIVELLO 'R' CIOE' REGIONALE NON PER UFFICIO DI ZONA");
              delegaVO.setDenomIntermediario(rs.getString(1));
              delegaVO.setDenominazioneExcel(rs.getString(1));
              delegaVO.setIndirizzo(rs.getString(2));
              delegaVO.setIndirizzoExcel(rs.getString(2));
              delegaVO.setDescComune(rs.getString(3));
              delegaVO.setComuneExcel(rs.getString(3));
              delegaVO.setSiglaProvincia(rs.getString(4));
              delegaVO.setProvinciaExcel(rs.getString(4));
              delegaVO.setCodiceFiscaleIntermediario(rs.getString(5));
              delegaVO.setCodiceAgeaExcel(rs.getString(5));
              delegaVO.setTotaleMandati(Integer.decode(rs.getString(6)));
              delegaVO.setTotaleMandatiExcel(Integer.decode(rs.getString(6)));
              delegaVO
                  .setTotaleMandatiValidati(Integer.decode(rs.getString(7)));
              delegaVO.setTotaleMandatiValidatiExcel(Integer.decode(rs
                  .getString(7)));
              elFiltro.setDataRiferimentoValidazione(sdf.format(dataDal), sdf
                  .format(dataAl));
              delegaVO.setElencoMandatiValidazioniFiltroEvcelVO(elFiltro);
            }
            else
            {
              SolmrLogger
                  .debug(this,
                      "INTERMEDIARIO CON LIVELLO 'R' CIOE' REGIONALE PER UFFICIO DI ZONA");
              delegaVO.setDenomIntermediario(rs.getString(1));
              delegaVO.setDenominazioneExcel(rs.getString(1));
              delegaVO.setIndirizzo(rs.getString(2));
              delegaVO.setIndirizzoExcel(rs.getString(2));
              delegaVO.setDescComune(rs.getString(3));
              delegaVO.setComuneExcel(rs.getString(3));
              delegaVO.setSiglaProvincia(rs.getString(4));
              delegaVO.setProvinciaExcel(rs.getString(4));
              delegaVO.setCodiceFiscaleIntermediario(rs.getString(5));
              delegaVO.setCodiceAgeaExcel(rs.getString(5));
              delegaVO.setTotaleMandati(Integer.decode(rs.getString(6)));
              delegaVO.setTotaleMandatiExcel(Integer.decode(rs.getString(6)));
              delegaVO
                  .setTotaleMandatiValidati(Integer.decode(rs.getString(7)));
              delegaVO.setTotaleMandatiValidatiExcel(Integer.decode(rs
                  .getString(7)));
              elFiltro.setDataRiferimentoValidazione(sdf.format(dataDal), sdf
                  .format(dataAl));
              delegaVO.setElencoMandatiValidazioniFiltroEvcelVO(elFiltro);
            }
          }
        }
        // CASO DI UTENTE APPARTENENTE ALLA PA(PROVINCIALI E REGIONALI)
        else
        {
          if (!forZona)
          {
            SolmrLogger
                .debug(
                    this,
                    "UTENTE APPARTENENTE ALLA PA(PROVINCIALI E REGIONALI) NON SUDDIVISI PER PROVINCIA");
            delegaVO.setDenomIntermediario(rs.getString(1));
            delegaVO.setDenominazioneExcel(rs.getString(1));
            delegaVO.setCodiceFiscaleIntermediario(rs.getString(2));
            delegaVO.setCodiceAgeaExcel(rs.getString(2));
            delegaVO.setTotaleMandati(Integer.decode(rs.getString(3)));
            delegaVO.setTotaleMandatiExcel(Integer.decode(rs.getString(3)));
            delegaVO.setTotaleMandatiValidati(Integer.decode(rs.getString(4)));
            delegaVO.setTotaleMandatiValidatiExcel(Integer.decode(rs
                .getString(4)));
            elFiltro.setDataRiferimentoValidazione(sdf.format(dataDal), sdf
                .format(dataAl));
            delegaVO.setElencoMandatiValidazioniFiltroEvcelVO(elFiltro);
          }
          else
          {
            SolmrLogger
                .debug(this,
                    "UTENTE APPARTENENTE ALLA PA(PROVINCIALI E REGIONALI) SUDDIVISI PER PROVINCIA");
            delegaVO.setDenomIntermediario(rs.getString(1));
            delegaVO.setDenominazioneExcel(rs.getString(1));
            delegaVO.setSiglaProvincia(rs.getString(2));
            delegaVO.setProvinciaExcel(rs.getString(2));
            delegaVO.setCodiceFiscaleIntermediario(rs.getString(3));
            delegaVO.setCodiceAgeaExcel(rs.getString(3));
            delegaVO.setTotaleMandati(Integer.decode(rs.getString(4)));
            delegaVO.setTotaleMandatiExcel(Integer.decode(rs.getString(4)));
            delegaVO.setTotaleMandatiValidati(Integer.decode(rs.getString(5)));
            delegaVO.setTotaleMandatiValidatiExcel(Integer.decode(rs
                .getString(5)));
            elFiltro.setDataRiferimentoValidazione(sdf.format(dataDal), sdf
                .format(dataAl));
            delegaVO.setElencoMandatiValidazioniFiltroEvcelVO(elFiltro);
          }
        }
        elencoMandati.add(delegaVO);
      }
      rs.close();
      stmt.close();
    }
    catch (SQLException sqe)
    {
      SolmrLogger.error(this, "getMandatiByUtente - SQLException: "
          + sqe.getMessage());
      throw new DataAccessException(sqe.getMessage());
    }
    catch (Exception e)
    {
      SolmrLogger.error(this, "getMandatiByUtente - Generic Exception: "
          + e.getMessage());
      throw new DataAccessException(e.getMessage());
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
      catch (SQLException sqe)
      {
        SolmrLogger.error(this,
            "getMandatiByUtente - SQLException while closing Statement and Connection: "
                + sqe.getMessage());
        throw new DataAccessException(sqe.getMessage());
      }
      catch (Exception e)
      {
        SolmrLogger
            .error(
                this,
                "getMandatiByUtente - Generic Exception while closing Statement and Connection: "
                    + e.getMessage());
        throw new DataAccessException(e.getMessage());
      }
    }
    return elencoMandati;
  }

  /**
   * Recupero l'elenco di CAA(espresso con un vector di CODE-DESCRIPTION perchè
   * mi serve per popolare una combo) in relazione all'utente che si è loggato
   * 
   */
  public Vector<CodeDescription> getElencoCAAByUtente(UtenteAbilitazioni utenteAbilitazioni)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<CodeDescription> elencoCAA = null;
    String query = null;
    try
    {
      conn = getDatasource().getConnection();
      // CASI UTENTE INTERMEDIARIO
      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        query = 
          "SELECT  ID_INTERMEDIARIO, " + 
          "        DENOMINAZIONE, " +
          "        CODICE_FISCALE, " +
          "        TO_CHAR(DATA_FINE_VALIDITA,'dd/mm/yyyy') AS DATA_FINE_VALIDITA " +
          "FROM     DB_INTERMEDIARIO ";
        // Se il livello intermediario è "R"
        if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_REGIONALE")))
        {
          query += "WHERE SUBSTR(CODICE_FISCALE, 1, 3) = ? ";
        }
        // Se il livello intermediario è "P"
        else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_PROVINCIALE")))
        {
          query += "WHERE SUBSTR(CODICE_FISCALE, 1, 6) = ? ";
        }
        // Se il livello intermediario è "Z"
        else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_UFFICIO_ZONA")))
        {
          query += "WHERE CODICE_FISCALE = ? ";
        }

        query += 
            " AND      TIPO_INTERMEDIARIO = ? " + " AND      LIVELLO = ? "
            + " ORDER BY DENOMINAZIONE ";
      }
      // In tutti gli altri casi
      else
      {
        query = 
        "SELECT ID_INTERMEDIARIO, " + 
        "       DENOMINAZIONE, " +
        "       CODICE_FISCALE, " +
        "       TO_CHAR(DATA_FINE_VALIDITA,'dd/mm/yyyy') AS DATA_FINE_VALIDITA " +
        "FROM   DB_INTERMEDIARIO " + 
        "WHERE  TIPO_INTERMEDIARIO = ? " +
        "AND    LIVELLO = ? "
 //           + " AND      DATA_FINE_VALIDITA IS NULL "
        + " ORDER BY DENOMINAZIONE ";
      }

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query getElencoCAAByUtente: " + query);

      // Setto i parametri

      // CASI INTERMEDIARIO
      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        // Se il livello intermediario è "R"
        if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_REGIONALE")))
        {
          stmt.setString(1, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getCodiceEnte()
              .substring(0, 3));
        }
        // Se il livello intermediario è "P"
        else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_PROVINCIALE")))
        {
          stmt.setString(1, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getCodiceEnte()
              .substring(0, 6));
        }
        // Se il livello intermediario è "Z"
        else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_UFFICIO_ZONA")))
        {
          stmt.setString(1, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getCodiceEnte());
        }
        stmt
            .setString(2, (String) SolmrConstants.get("TIPO_INTERMEDIARIO_CAA"));
        stmt.setString(3, (String) SolmrConstants
            .get("LIVELLO_INTERMEDIARIO_UFFICIO_ZONA"));
      }
      // In trtti gli altri casi
      else
      {
        stmt
            .setString(1, (String) SolmrConstants.get("TIPO_INTERMEDIARIO_CAA"));
        stmt.setString(2, (String) SolmrConstants
            .get("LIVELLO_INTERMEDIARIO_UFFICIO_ZONA"));
      }

      ResultSet rs = stmt.executeQuery();
      elencoCAA = new Vector<CodeDescription>();

      while (rs.next())
      {
        CodeDescription code = new CodeDescription();
        code.setCode(new Integer(rs.getString("ID_INTERMEDIARIO")));
        code.setDescription(rs.getString("DENOMINAZIONE"));
        code.setSecondaryCode(rs.getString("CODICE_FISCALE"));
        code.setCodeFlag(rs.getString("DATA_FINE_VALIDITA"));
        elencoCAA.add(code);
      }

      rs.close();
      stmt.close();

    }
    catch (SQLException sqe)
    {
      SolmrLogger.error(this, "getElencoCAAByUtente - SQLException: "
          + sqe.getMessage());
      throw new DataAccessException(sqe.getMessage());
    }
    catch (Exception e)
    {
      SolmrLogger.error(this, "getElencoCAAByUtente - Generic Exception: "
          + e.getMessage());
      throw new DataAccessException(e.getMessage());
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
      catch (SQLException sqe)
      {
        SolmrLogger.error(this,
            "getElencoCAAByUtente - SQLException while closing Statement and Connection: "
                + sqe.getMessage());
        throw new DataAccessException(sqe.getMessage());
      }
      catch (Exception e)
      {
        SolmrLogger
            .error(
                this,
                "getElencoCAAByUtente - Generic Exception while closing Statement and Connection: "
                    + e.getMessage());
        throw new DataAccessException(e.getMessage());
      }
    }
    return elencoCAA;
  }

  /**
   * Metodo per recuperare l'elenco dei mandati validati in un determinato
   * periodo di tempo
   */
  public Vector<DelegaVO> getMandatiValidatiByUtente(UtenteAbilitazioni utenteAbilitazioni,
      boolean forZona, java.util.Date dataDal, java.util.Date dataAl)
      throws DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating method getMandatiValidatiByUtente in CommonDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<DelegaVO> elencoMandati = null;
    String query = null;

    try
    {
      SolmrLogger
          .debug(this,
              "Creating db_connection in method getMandatiValidatiByUtente in CommonDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db_connection in method getMandatiValidatiByUtente in CommonDAO with value: "
                  + conn + "\n");
      // CASI UTENTE INTERMEDIARIO
      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        SolmrLogger
            .debug(
                this,
                "Creating query of case of profile intermediario in method getMandatiValidatiByUtente in CommonDAO\n");
        // CASO INTERMEDIARIO CON LIVELLO "Z" CIOE' UFFICI SPECIFICI DI ZONA
        if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_UFFICIO_ZONA")))
        {

          SolmrLogger
              .debug(
                  this,
                  "Creating query of case of profile intermediario with level Z in method getMandatiValidatiByUtente in CommonDAO\n");

          query = " SELECT  DENOMINAZIONE, "
              + "         INDIRIZZO , "
              + "         CODICE_FISCALE, "
              + "         COUNT(*) "
              + " FROM    (SELECT DISTINCT I.DENOMINAZIONE, "
              + "                          I.INDIRIZZO, "
              + "                          I.CODICE_FISCALE, "
              + "                          D.ID_AZIENDA "
              + "          FROM            DB_DELEGA D, "
              + "                          DB_INTERMEDIARIO I, "
              + "                          DB_ANAGRAFICA_AZIENDA AA, "
              + "                          DB_DICHIARAZIONE_CONSISTENZA DC "
              + "          WHERE           D.ID_INTERMEDIARIO = ? "
              + "          AND             D.ID_INTERMEDIARIO = I.ID_INTERMEDIARIO "
              + "          AND             D.DATA_FINE IS NULL "
              + "          AND             D.ID_PROCEDIMENTO = ? "
              + "          AND             D.ID_AZIENDA = AA.ID_AZIENDA "
              + "          AND             AA.DATA_FINE_VALIDITA IS NULL "
              + "          AND             AA.DATA_CESSAZIONE IS NULL "
              + "          AND             DC.ID_AZIENDA = D.ID_AZIENDA "
              + "          AND             TRUNC(DC.DATA) >= ? "
              + "          AND             TRUNC(DC.DATA) <= ? ) "
              + " GROUP BY DENOMINAZIONE, " + "          INDIRIZZO, "
              + "          CODICE_FISCALE ";

          SolmrLogger
              .debug(
                  this,
                  "Created query of case of profile intermediario with level Z in method getMandatiValidatiByUtente in CommonDAO\n");

        }
        // CASO INTERMEDIARIO CON LIVELLO "P" CIOE' PROVINCIALE
        else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_PROVINCIALE")))
        {

          SolmrLogger
              .debug(
                  this,
                  "Creating query of case of profile intermediario with level P in method getMandatiValidatiByUtente in CommonDAO\n");

          query = " SELECT  SIGLA_PROVINCIA, "
              + "         DESCOM, "
              + "         INDIRIZZO, "
              + "         CODICE_FISCALE, "
              + "         COUNT(*) "
              + " FROM    (SELECT DISTINCT P.SIGLA_PROVINCIA, "
              + "                          C.DESCOM, "
              + "                          I.INDIRIZZO, "
              + "                          I.CODICE_FISCALE, "
              + "                          D.ID_AZIENDA "
              + "          FROM            DB_DELEGA D, "
              + "                          DB_INTERMEDIARIO I, "
              + "                          COMUNE C, "
              + "                          PROVINCIA P, "
              + "                          DB_ANAGRAFICA_AZIENDA AA, "
              + "                          DB_DICHIARAZIONE_CONSISTENZA DC "
              + "          WHERE           D.ID_INTERMEDIARIO IN "
              + "                          (SELECT ID_INTERMEDIARIO "
              + "                           FROM   DB_INTERMEDIARIO "
              + "                           WHERE  SUBSTR(CODICE_FISCALE,1,6) = ? "
              + "                           AND    TIPO_INTERMEDIARIO = 'C' ) " 
              + "          AND             D.ID_INTERMEDIARIO = I.ID_INTERMEDIARIO "
              + "          AND             D.DATA_FINE IS NULL "
              + "          AND             D.ID_PROCEDIMENTO = ? "
              + "          AND             I.COMUNE = C.ISTAT_COMUNE "
              + "          AND             C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
              + "          AND             D.ID_AZIENDA = AA.ID_AZIENDA "
              + "          AND             AA.DATA_FINE_VALIDITA IS NULL "
              + "          AND             AA.DATA_CESSAZIONE IS NULL "
              + "          AND             DC.ID_AZIENDA = D.ID_AZIENDA "
              + "          AND             TRUNC(DC.DATA) >= ? "
              + "          AND             TRUNC(DC.DATA) <= ?) "
              + " GROUP BY SIGLA_PROVINCIA, " + "          DESCOM, "
              + "          INDIRIZZO, " + "          CODICE_FISCALE";

          SolmrLogger
              .debug(
                  this,
                  "Created query of case of profile intermediario with level P in method getMandatiValidatiByUtente in CommonDAO\n");

        }
        // CASO INTERMEDIARIO CON LIVELLO "R" CIOE' REGIONALE
        else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_REGIONALE")))
        {

          SolmrLogger
              .debug(
                  this,
                  "Creating query of case of profile intermediario with level R in method getMandatiValidatiByUtente in CommonDAO\n");

          // CASO NON PER UFFICIO DI ZONA
          if (!forZona)
          {

            SolmrLogger
                .debug(
                    this,
                    "Creating query of case of profile intermediario of option non per ufficio di zona in method getMandatiValidatiByUtente in CommonDAO\n");

            query = " SELECT  DESCRIZIONE, "
                + "         COUNT(*) "
                + " FROM    (SELECT DISTINCT P.DESCRIZIONE, "
                + "                          D.ID_AZIENDA "
                + "          FROM            DB_DELEGA D, "
                + "                          DB_INTERMEDIARIO I, "
                + "                          COMUNE C, "
                + "                          PROVINCIA P, "
                + "                          DB_DICHIARAZIONE_CONSISTENZA DC "
                + "          WHERE           D.ID_INTERMEDIARIO IN "
                + "                          (SELECT ID_INTERMEDIARIO "
                + "                           FROM   DB_INTERMEDIARIO "
                + "                           WHERE  SUBSTR(CODICE_FISCALE,1,3) = ?  "
                + "                           AND    TIPO_INTERMEDIARIO = 'C' ) " 
                + "          AND             D.ID_INTERMEDIARIO = I.ID_INTERMEDIARIO "
                + "          AND             D.DATA_FINE IS NULL "
                + "          AND             D.ID_PROCEDIMENTO = ? "
                + "          AND             I.COMUNE = C.ISTAT_COMUNE "
                + "          AND             C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
                + "          AND             DC.ID_AZIENDA = D.ID_AZIENDA "
                + "          AND             TRUNC(DC.DATA) >= ? "
                + "          AND             TRUNC(DC.DATA) <= ?) "
                + " GROUP BY DESCRIZIONE ";

            SolmrLogger
                .debug(
                    this,
                    "Created query of case of profile intermediario of option non per ufficio di zona in method getMandatiValidatiByUtente in CommonDAO\n");

          }
          // CASO UFFICI DI ZONA SPECIFICATI
          else
          {

            SolmrLogger
                .debug(
                    this,
                    "Creating query of case of profile intermediario of option per ufficio di zona in method getMandatiValidatiByUtente in CommonDAO\n");

            query = " SELECT  SIGLA_PROVINCIA, "
                + "         DESCOM, "
                + "         INDIRIZZO, "
                + "         CODICE_FISCALE, "
                + "         COUNT(*) "
                + " FROM    (SELECT DISTINCT P.SIGLA_PROVINCIA, "
                + "                          C.DESCOM, "
                + "                          I.INDIRIZZO, "
                + "                          I.CODICE_FISCALE, "
                + "                          D.ID_AZIENDA "
                + "          FROM            DB_DELEGA D, "
                + "                          DB_INTERMEDIARIO I, "
                + "                          COMUNE C, "
                + "                          PROVINCIA P, "
                + "                          DB_ANAGRAFICA_AZIENDA AA, "
                + "                          DB_DICHIARAZIONE_CONSISTENZA DC "
                + "          WHERE           D.ID_INTERMEDIARIO IN "
                + "                          (SELECT ID_INTERMEDIARIO "
                + "                           FROM   DB_INTERMEDIARIO "
                + "                           WHERE  SUBSTR(CODICE_FISCALE,1,3) = ? "
                + "                           AND    TIPO_INTERMEDIARIO = 'C' ) " 
                + "          AND             D.ID_INTERMEDIARIO = I.ID_INTERMEDIARIO "
                + "          AND             D.DATA_FINE IS NULL "
                + "          AND             D.ID_PROCEDIMENTO = ? "
                + "          AND             I.COMUNE = C.ISTAT_COMUNE "
                + "          AND             C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
                + "          AND             D.ID_AZIENDA = AA.ID_AZIENDA "
                + "          AND             AA.DATA_FINE_VALIDITA IS NULL "
                + "          AND             AA.DATA_CESSAZIONE IS NULL "
                + "          AND             DC.ID_AZIENDA = D.ID_AZIENDA "
                + "          AND             TRUNC(DC.DATA) >= ? "
                + "          AND             TRUNC(DC.DATA) <= ?) "
                + " GROUP BY SIGLA_PROVINCIA, " + "          DESCOM, "
                + "          INDIRIZZO, " + "          CODICE_FISCALE ";

            SolmrLogger
                .debug(
                    this,
                    "Created query of case of profile intermediario of option per ufficio di zona in method getMandatiValidatiByUtente in CommonDAO\n");

          }

          SolmrLogger
              .debug(
                  this,
                  "Created query of case of profile intermediario with level R in method getMandatiValidatiByUtente in CommonDAO\n");

        }

        SolmrLogger
            .debug(
                this,
                "Created query of case of profile intermediario in method getMandatiValidatiByUtente in CommonDAO\n");

      }
      // CASO DI UTENTE APPARTENENTE ALLA PA(PROVINCIALI E REGIONALI)
      else
      {

        SolmrLogger
            .debug(
                this,
                "Creating query of case of profile provinciale in method getMandatiValidatiByUtente in CommonDAO\n");

        // CASO RECORD NON DIVISI PER PROVINCIA
        if (!forZona)
        {

          SolmrLogger
              .debug(
                  this,
                  "Creating query of case of profile provinciale of option non per ufficio di zona in method getMandatiValidatiByUtente in CommonDAO\n");

          query = " SELECT  DENOMINAZIONE, "
              + "         COUNT(*) "
              + " FROM    (SELECT DISTINCT PADRE.DENOMINAZIONE, "
              + "                          D.ID_AZIENDA "
              + "          FROM            DB_DELEGA D, "
              + "                          DB_INTERMEDIARIO I, "
              + "                          DB_INTERMEDIARIO PADRE, "
              + "                          DB_ANAGRAFICA_AZIENDA AA, "
              + "                          DB_DICHIARAZIONE_CONSISTENZA DC "
              + "          WHERE           D.DATA_FINE IS NULL "
              + "          AND             D.ID_PROCEDIMENTO = ? "
              + "          AND             D.ID_INTERMEDIARIO = I.ID_INTERMEDIARIO "
              + "          AND             SUBSTR(I.CODICE_FISCALE,1,3) = SUBSTR(PADRE.CODICE_FISCALE,1,3) "
              + "          AND             I.TIPO_INTERMEDIARIO = 'C' " 
              + "          AND             PADRE.LIVELLO = ? "
              + "          AND             D.ID_AZIENDA = AA.ID_AZIENDA "
              + "          AND             AA.DATA_FINE_VALIDITA IS NULL "
              + "          AND             AA.DATA_CESSAZIONE IS NULL "
              + "          AND             DC.ID_AZIENDA = D.ID_AZIENDA "
              + "          AND             TRUNC(DC.DATA) >= ? "
              + "          AND             TRUNC(DC.DATA) <= ?) "
              + " GROUP BY DENOMINAZIONE ";

          SolmrLogger
              .debug(
                  this,
                  "Created query of case of profile provinciale of option non per ufficio di zona in method getMandatiValidatiByUtente in CommonDAO\n");

        }
        // CASO RECORD DIVISI PER PROVINCIA
        else
        {

          SolmrLogger
              .debug(
                  this,
                  "Creating query of case of profile provinciale of option per ufficio di zona in method getMandatiValidatiByUtente in CommonDAO\n");

          query = " SELECT  DENOMINAZIONE, "
              + "         DESCRIZIONE, "
              + "         COUNT(*) "
              + " FROM    (SELECT DISTINCT PADRE.DENOMINAZIONE, "
              + "                          P.DESCRIZIONE, "
              + "                          D.ID_AZIENDA "
              + "          FROM            DB_DELEGA D, "
              + "                          DB_INTERMEDIARIO I, "
              + "                          DB_INTERMEDIARIO PADRE, "
              + "                          PROVINCIA P, "
              + "                          DB_ANAGRAFICA_AZIENDA AA, "
              + "                          DB_DICHIARAZIONE_CONSISTENZA DC "
              + "          WHERE           D.DATA_FINE IS NULL "
              + "          AND             D.ID_PROCEDIMENTO = ? "
              + "          AND             D.ID_INTERMEDIARIO = I.ID_INTERMEDIARIO "
              + "          AND             SUBSTR(I.CODICE_FISCALE,1,3) = SUBSTR(PADRE.CODICE_FISCALE,1,3) "
              + "          AND             I.TIPO_INTERMEDIARIO = 'C' " 
              + "          AND             PADRE.LIVELLO = ? "
              + "          AND             SUBSTR(I.COMUNE,1,3) = P.ISTAT_PROVINCIA "
              + "          AND             D.ID_AZIENDA = AA.ID_AZIENDA "
              + "          AND             AA.DATA_FINE_VALIDITA IS NULL "
              + "          AND             AA.DATA_CESSAZIONE IS NULL "
              + "          AND             DC.ID_AZIENDA = D.ID_AZIENDA "
              + "          AND             TRUNC(DC.DATA) >= ? "
              + "          AND             TRUNC(DC.DATA) <= ?) "
              + " GROUP BY DENOMINAZIONE, " + "          DESCRIZIONE ";

          SolmrLogger
              .debug(
                  this,
                  "Created query of case of profile provinciale of option per ufficio di zona in method getMandatiValidatiByUtente in CommonDAO\n");

        }

        SolmrLogger
            .debug(
                this,
                "Creating query of case of profile provinciale in method getMandatiValidatiByUtente in CommonDAO\n");

      }

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query getMandatiValidatiByUtente: "
          + query);

      // Setto i parametri della query
      // CASI PROFILO INTERMEDIARIO
      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        SolmrLogger
            .debug(
                this,
                "Setting parameters of a case of profile intermediario in method getMandatiValidatiByUtente in CommonDAO\n");
        // CASO INTERMEDIARIO CON LIVELLO "Z" CIOE' UFFICI SPECIFICI DI ZONA
        if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_UFFICIO_ZONA")))
        {
          SolmrLogger
              .debug(
                  this,
                  "Setting parameters of a case of profile intermediario with level Z in method getMandatiValidatiByUtente in CommonDAO\n");
          SolmrLogger
              .debug(
                  this,
                  "Value of parameter 1 [ID_INTERMEDIARIO] in method getMandatiValidatiByUtente in CommonDAO: "
                      + utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getIdIntermediario()
                      + "\n");
          stmt.setLong(1, utenteAbilitazioni.getEnteAppartenenza().getIntermediario()
              .getIdIntermediario());
          SolmrLogger
              .debug(
                  this,
                  "Value of parameter 2 [ID_PROCEDIMENTO] in method getMandatiValidatiByUtente in CommonDAO: "
                      + SolmrConstants.get("ID_TIPO_PROCEDIMENTO_ANAG") + "\n");
          stmt.setLong(2, Long.parseLong((String) SolmrConstants
              .get("ID_TIPO_PROCEDIMENTO_ANAG")));
        }
        // CASO INTERMEDIARIO CON LIVELLO "P" CIOE' PROVINCIALE
        else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_PROVINCIALE")))
        {
          SolmrLogger
              .debug(
                  this,
                  "Setting parameters of a case of profile intermediario with level P in method getMandatiValidatiByUtente in CommonDAO\n");
          SolmrLogger
              .debug(
                  this,
                  "Value of parameter 1 [CODICE_FISCALE] in method getMandatiValidatiByUtente in CommonDAO: "
                      + utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getCodiceEnte()
                          .substring(0, 6) + "\n");
          stmt.setString(1, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getCodiceEnte()
              .substring(0, 6));
          SolmrLogger
              .debug(
                  this,
                  "Value of parameter 2 [ID_PROCEDIMENTO] in method getMandatiValidatiByUtente in CommonDAO: "
                      + SolmrConstants.get("ID_TIPO_PROCEDIMENTO_ANAG") + "\n");
          stmt.setLong(2, Long.parseLong((String) SolmrConstants
              .get("ID_TIPO_PROCEDIMENTO_ANAG")));
        }
        // CASO INTERMEDIARIO CON LIVELLO "R" CIOE' REGIONALE
        else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_REGIONALE")))
        {
          SolmrLogger
              .debug(
                  this,
                  "Setting parameters of a case of profile intermediario with level R in method getMandatiValidatiByUtente in CommonDAO\n");
          SolmrLogger
              .debug(
                  this,
                  "Value of parameter 1 [CODICE_FISCALE] in method getMandatiValidatiByUtente in CommonDAO: "
                      + utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getCodiceEnte()
                          .substring(0, 3) + "\n");
          stmt.setString(1, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getCodiceEnte()
              .substring(0, 3));
          SolmrLogger
              .debug(
                  this,
                  "Value of parameter 2 [ID_PROCEDIMENTO] in method getMandatiValidatiByUtente in CommonDAO: "
                      + SolmrConstants.get("ID_TIPO_PROCEDIMENTO_ANAG") + "\n");
          stmt.setLong(2, Long.parseLong((String) SolmrConstants
              .get("ID_TIPO_PROCEDIMENTO_ANAG")));
          SolmrLogger
              .debug(
                  this,
                  "Setted parameters of a case of profile intermediario with level R in method getMandatiValidatiByUtente in CommonDAO\n");
        }
      }
      // CASO DI UTENTE APPARTENENTE ALLA PA(PROVINCIALI E REGIONALI)
      else
      {
        SolmrLogger
            .debug(
                this,
                "Value of parameter 1 [ID_PROCEDIMENTO] in method getMandatiValidatiByUtente in CommonDAO: "
                    + SolmrConstants.get("ID_TIPO_PROCEDIMENTO_ANAG") + "\n");
        stmt.setLong(1, Long.parseLong((String) SolmrConstants
            .get("ID_TIPO_PROCEDIMENTO_ANAG")));
        SolmrLogger
            .debug(
                this,
                "Value of parameter 2 [LIVELLO] in method getMandatiValidatiByUtente in CommonDAO: "
                    + (String) SolmrConstants
                        .get("LIVELLO_INTERMEDIARIO_REGIONALE") + "\n");
        stmt.setString(2, (String) SolmrConstants
            .get("LIVELLO_INTERMEDIARIO_REGIONALE"));
      }

      SolmrLogger
          .debug(
              this,
              "Value of parameter 3 [DATA_DAL] in method getMandatiValidatiByUtente in CommonDAO: "
                  + new java.sql.Date(dataDal.getTime()) + "\n");
      stmt.setDate(3, new java.sql.Date(dataDal.getTime()));
      SolmrLogger
          .debug(
              this,
              "Value of parameter 4 [DATA_AL] in method getMandatiValidatiByUtente in CommonDAO: "
                  + new java.sql.Date(dataAl.getTime()) + "\n");
      stmt.setDate(4, new java.sql.Date(dataAl.getTime()));
      SolmrLogger
          .debug(
              this,
              "Setted parameters of a case of profile intermediario with level P in method getMandatiValidatiByUtente in CommonDAO\n");

      ResultSet rs = stmt.executeQuery();
      elencoMandati = new Vector<DelegaVO>();

      while (rs.next())
      {
        DelegaVO delegaVO = new DelegaVO();
        // CASI PROFILO INTERMEDIARIO
        if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
        {
          // CASO INTERMEDIARIO CON LIVELLO "Z" CIOE' UFFICI SPECIFICI DI ZONA
          if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello()
              .equalsIgnoreCase(
                  (String) SolmrConstants
                      .get("LIVELLO_INTERMEDIARIO_UFFICIO_ZONA")))
          {
            delegaVO.setDenomIntermediario(rs.getString(1));
            delegaVO.setIndirizzo(rs.getString(2));
            delegaVO.setCodiceFiscaleIntermediario(rs.getString(3));
            delegaVO.setTotaleMandati(Integer.decode(rs.getString(4)));
          }
          // CASO INTERMEDIARIO CON LIVELLO "P" CIOE' PROVINCIALE
          else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
              (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_PROVINCIALE")))
          {
            delegaVO.setSiglaProvincia(rs.getString(1));
            delegaVO.setDescComune(rs.getString(2));
            delegaVO.setIndirizzo(rs.getString(3));
            delegaVO.setCodiceFiscaleIntermediario(rs.getString(4));
            delegaVO.setTotaleMandati(Integer.decode(rs.getString(5)));
          }
          // CASO INTERMEDIARIO CON LIVELLO "R" CIOE' REGIONALE
          else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
              (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_REGIONALE")))
          {
            // CASO NON PER UFFICIO DI ZONA
            if (!forZona)
            {
              delegaVO.setDescrizioneProvincia(rs.getString(1));
              delegaVO.setTotaleMandati(Integer.decode(rs.getString(2)));
            }
            // CASO UFFICI DI ZONA SPECIFICATI
            else
            {
              delegaVO.setSiglaProvincia(rs.getString(1));
              delegaVO.setDescComune(rs.getString(2));
              delegaVO.setIndirizzo(rs.getString(3));
              delegaVO.setCodiceFiscaleIntermediario(rs.getString(4));
              delegaVO.setTotaleMandati(Integer.decode(rs.getString(5)));
            }
          }
        }
        // CASO DI UTENTE APPARTENENTE ALLA PA(PROVINCIALI E REGIONALI)
        else
        {
          // CASO NON PER PROVINCIA
          if (!forZona)
          {
            delegaVO.setDenomIntermediario(rs.getString(1));
            delegaVO.setTotaleMandati(Integer.decode(rs.getString(2)));
          }
          // CASO SUDDIVISI PER PROVINCIA
          else
          {
            delegaVO.setDenomIntermediario(rs.getString(1));
            delegaVO.setDescrizioneProvincia(rs.getString(2));
            delegaVO.setTotaleMandati(Integer.decode(rs.getString(3)));
          }
        }
        elencoMandati.add(delegaVO);
      }

      rs.close();
      stmt.close();

    }
    catch (SQLException sqe)
    {
      SolmrLogger.error(this, "getMandatiValidatiByUtente - SQLException: "
          + sqe.getMessage());
      throw new DataAccessException(sqe.getMessage());
    }
    catch (Exception e)
    {
      SolmrLogger.error(this,
          "getMandatiValidatiByUtente - Generic Exception: " + e.getMessage());
      throw new DataAccessException(e.getMessage());
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
      catch (SQLException sqe)
      {
        SolmrLogger
            .error(
                this,
                "getMandatiValidatiByUtente - SQLException while closing Statement and Connection: "
                    + sqe.getMessage());
        throw new DataAccessException(sqe.getMessage());
      }
      catch (Exception e)
      {
        SolmrLogger
            .error(
                this,
                "getMandatiValidatiByUtente - Generic Exception while closing Statement and Connection: "
                    + e.getMessage());
        throw new DataAccessException(e.getMessage());
      }
    }
    SolmrLogger.debug(this,
        "Invocated method getMandatiValidatiByUtente in CommonDAO\n");
    return elencoMandati;
  }

  /**
   * Metodo per reperire tutte le informazioni relative all'utente di tipo
   * intermediario associato ad uno specifico ufficio di zona
   * 
   * @param idUfficioZonaIntermediario
   *          Long
   * @return it.csi.solmr.dto.IntermediarioVO
   * @throws DataAccessException
   */
  public IntermediarioVO getIntermediarioVOByIdUfficioZonaIntermediario(
      Long idUfficioZonaIntermediario) throws DataAccessException
  {
    SolmrLogger
        .debug(
            this,
            "Invocating getIntermediarioVOByIdUfficioZonaIntermediario method in CommonDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    IntermediarioVO intermediarioVO = null;

    try
    {
      SolmrLogger
          .debug(
              this,
              "Creating db-connection in getIntermediarioVOByIdUfficioZonaIntermediario method in CommonDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in getIntermediarioVOByIdUfficioZonaIntermediario method in CommonDAO and it values: "
                  + conn + "\n");

      String query = " SELECT I.ID_INTERMEDIARIO, "
          + "        I.DENOMINAZIONE, " + "        I.CODICE_FISCALE, "
          + "        I.INDIRIZZO, " + "        I.CAP, " + "        I.COMUNE, "
          + "        I.TIPO_INTERMEDIARIO, "
          + "        I.ID_INTERMEDIARIO_PADRE, " + "        I.LIVELLO, "
          + "        I.PARTITA_IVA, " + "        I.DATA_FINE_VALIDITA "
          + " FROM   DB_INTERMEDIARIO I, "
          + "        DB_UFFICIO_ZONA_INTERMEDIARIO UZI "
          + " WHERE  UZI.ID_UFFICIO_ZONA_INTERMEDIARIO = ? "
          + " AND    UZI.ID_INTERMEDIARIO = I.ID_INTERMEDIARIO ";

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [ID_UFFICIO_ZONA_INTERMEDIARIO] in getIntermediarioVOByIdUfficioZonaIntermediario method in CommonDAO: "
                  + idUfficioZonaIntermediario + "\n");
      stmt.setLong(1, idUfficioZonaIntermediario.longValue());
      SolmrLogger.debug(this,
          "Executing getIntermediarioVOByIdUfficioZonaIntermediario: " + query
              + "\n");

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        intermediarioVO = new IntermediarioVO();
        intermediarioVO.setIdIntermediario(rs.getString(1));
        intermediarioVO.setDenominazione(rs.getString(2));
        intermediarioVO.setCodiceFiscale(rs.getString(3));
        intermediarioVO.setIndirizzo(rs.getString(4));
        intermediarioVO.setCap(rs.getString(5));
        intermediarioVO.setComune(rs.getString(6));
        intermediarioVO.setTipoIntermediario(rs.getString(7));
        intermediarioVO.setIdIntermediarioPadre(rs.getString(8));
        intermediarioVO.setLivello(rs.getString(9));
        intermediarioVO.setPartitaIva(rs.getString(10));
        if (Validator.isNotEmpty(rs.getString(11)))
        {
          intermediarioVO.setDataFineValidita(new java.util.Date(rs.getDate(11)
              .getTime()));
        }
      }

      stmt.close();
      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "getIntermediarioVOByIdUfficioZonaIntermediario in CommonDAO - SQLException: "
              + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .error(
              this,
              "getIntermediarioVOByIdUfficioZonaIntermediario in CommonDAO - Generic Exception: "
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
                "getIntermediarioVOByIdUfficioZonaIntermediario in CommonDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getIntermediarioVOByIdUfficioZonaIntermediario in CommonDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger
        .debug(
            this,
            "Invocated getIntermediarioVOByIdUfficioZonaIntermediario method in CommonDAO\n");
    return intermediarioVO;
  }

  /**
   * Metodo per recuperare l'intermediario a partire dalla chiave primaria
   * 
   * @param idIntermediario
   *          Long
   * @return it.csi.solmr.dto.IntermediarioVO
   * @throws DataAccessException
   */
  public IntermediarioVO findIntermediarioVOByPrimaryKey(Long idIntermediario)
      throws DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating findIntermediarioVOByPrimaryKey method in CommonDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    IntermediarioVO intermediarioVO = null;

    try
    {
      SolmrLogger
          .debug(
              this,
              "Creating db-connection in findIntermediarioVOByPrimaryKey method in CommonDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in findIntermediarioVOByPrimaryKey method in CommonDAO and it values: "
                  + conn + "\n");

      String query = " SELECT I.ID_INTERMEDIARIO, "
          + "        I.DENOMINAZIONE, " + "        I.CODICE_FISCALE, "
          + "        I.INDIRIZZO, " + "        I.CAP, " + "        I.COMUNE, "
          + "        I.TIPO_INTERMEDIARIO, "
          + "        I.ID_INTERMEDIARIO_PADRE, " + "        I.LIVELLO, "
          + "        I.PARTITA_IVA, " + "        I.DATA_FINE_VALIDITA "
          + " FROM   DB_INTERMEDIARIO I " + " WHERE  I.ID_INTERMEDIARIO = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [ID_INTERMEDIARIO] in findIntermediarioVOByPrimaryKey method in CommonDAO: "
                  + idIntermediario + "\n");
      stmt.setLong(1, idIntermediario.longValue());

      SolmrLogger.debug(this, "Executing findIntermediarioVOByPrimaryKey: "
          + query + "\n");

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        intermediarioVO = new IntermediarioVO();
        intermediarioVO.setIdIntermediario(rs.getString(1));
        intermediarioVO.setDenominazione(rs.getString(2));
        intermediarioVO.setCodiceFiscale(rs.getString(3));
        intermediarioVO.setIndirizzo(rs.getString(4));
        intermediarioVO.setCap(rs.getString(5));
        intermediarioVO.setComune(rs.getString(6));
        intermediarioVO.setTipoIntermediario(rs.getString(7));
        intermediarioVO.setIdIntermediarioPadre(rs.getString(8));
        intermediarioVO.setLivello(rs.getString(9));
        intermediarioVO.setPartitaIva(rs.getString(10));
        if (Validator.isNotEmpty(rs.getString(11)))
        {
          intermediarioVO.setDataFineValidita(new java.util.Date(rs.getDate(11)
              .getTime()));
        }
      }

      stmt.close();
      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "findIntermediarioVOByPrimaryKey in CommonDAO - SQLException: "
              + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "findIntermediarioVOByPrimaryKey in CommonDAO - Generic Exception: "
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
                "findIntermediarioVOByPrimaryKey in CommonDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "findIntermediarioVOByPrimaryKey in CommonDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this,
        "Invocated findIntermediarioVOByPrimaryKey method in CommonDAO\n");
    return intermediarioVO;
  }  

  /**
   * Metodo che si occupa di reperire i dati dalla tabella REGIONE in relazione
   * all'istat della provincia
   * 
   * @param istatProvincia
   *          String
   * @return CodeDescription
   * @throws DataAccessException
   */
  public CodeDescription findRegioneByIstatProvincia(String istatProvincia)
      throws DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating findRegioneByIstatProvincia method in CommonDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    CodeDescription code = null;

    try
    {
      SolmrLogger
          .debug(this,
              "Creating db-connection in findRegioneByIstatProvincia method in CommonDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in findRegioneByIstatProvincia method in CommonDAO and it values: "
                  + conn + "\n");

      String query = " SELECT R.ID_REGIONE, " + "        R.DESCRIZIONE "
          + " FROM   REGIONE R, " + "        PROVINCIA P "
          + " WHERE  P.ISTAT_PROVINCIA = ? "
          + " AND    P.ID_REGIONE = R.ID_REGIONE";

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [ISTAT_PROVINCIA] in findRegioneByIstatProvincia method in CommonDAO: "
                  + istatProvincia + "\n");
      stmt.setString(1, istatProvincia);

      SolmrLogger.debug(this, "Executing findRegioneByIstatProvincia: " + query
          + "\n");

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        code = new CodeDescription(new Integer(rs.getString(1)), rs
            .getString(2));
      }

      stmt.close();
      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "findRegioneByIstatProvincia in CommonDAO - SQLException: "
              + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "findRegioneByIstatProvincia in CommonDAO - Generic Exception: " + ex
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
                "findRegioneByIstatProvincia in CommonDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "findRegioneByIstatProvincia in CommonDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this,
        "Invocated findRegioneByIstatProvincia method in CommonDAO\n");
    return code;
  }

  /**
   * Metodo che si occupa di reperire i dati dalla tabella REGIONE relativi
   * all'intermediario loggato partendo dal codice_fiscale
   * 
   * @param codiceFiscaleIntermediario
   *          String
   * @return CodeDescription
   * @throws DataAccessException
   */
  public CodeDescription findRegioneByCodiceFiscaleIntermediario(
      String codiceFiscaleIntermediario) throws DataAccessException
  {
    SolmrLogger
        .debug(this,
            "Invocating findRegioneByCodiceFiscaleIntermediario method in CommonDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    CodeDescription code = null;

    try
    {
      SolmrLogger
          .debug(
              this,
              "Creating db-connection in findRegioneByCodiceFiscaleIntermediario method in CommonDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in findRegioneByCodiceFiscaleIntermediario method in CommonDAO and it values: "
                  + conn + "\n");

      String query = " SELECT R.DESCRIZIONE, " + "        R.ID_REGIONE "
          + " FROM   REGIONE R, " + "        PROVINCIA P, "
          + "        COMUNE C, " + "        DB_INTERMEDIARIO I "
          + " WHERE  I.CODICE_FISCALE = ? "
          + " AND    I.COMUNE = C.ISTAT_COMUNE "
          + " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
          + " AND    P.ID_REGIONE = R.ID_REGIONE ";

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [CODICE_FISCALE] in findRegioneByCodiceFiscaleIntermediario method in CommonDAO: "
                  + codiceFiscaleIntermediario + "\n");
      stmt.setString(1, codiceFiscaleIntermediario);

      SolmrLogger.debug(this,
          "Executing findRegioneByCodiceFiscaleIntermediario: " + query + "\n");

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        code = new CodeDescription(new Integer(rs.getString(2)), rs
            .getString(1));
      }

      stmt.close();
      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "findRegioneByCodiceFiscaleIntermediario in CommonDAO - SQLException: "
              + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "findRegioneByCodiceFiscaleIntermediario in CommonDAO - Generic Exception: "
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
                "findRegioneByCodiceFiscaleIntermediario in CommonDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "findRegioneByCodiceFiscaleIntermediario in CommonDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger
        .debug(this,
            "Invocated findRegioneByCodiceFiscaleIntermediario method in CommonDAO\n");
    return code;
  }

  /**
   * Metodo che si occupa di reperire i dati dalla tabella
   * DB_TIPO_PIANTE_CONSOCIATE
   * 
   * @param onlyActive
   * @return java.util.Vector
   * @throws DataAccessException
   */
  public Vector<TipoPiantaConsociataVO> getListPianteConsociate(
      boolean onlyActive) throws DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating getListPianteConsociateVO method in CommonDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoPiantaConsociataVO> elencoPiante = new Vector<TipoPiantaConsociataVO>();

    try
    {
      SolmrLogger
          .debug(this,
              "Creating db-connection in getListPianteConsociate method in CommonDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in getListPianteConsociate method in CommonDAO and it values: "
                  + conn + "\n");

      String query = " SELECT ID_PIANTE_CONSOCIATE, " + "        DESCRIZIONE, "
          + "        DATA_INIZIO_VALIDITA, " + "        DATA_FINE_VALIDITA "
          + " FROM   DB_TIPO_PIANTE_CONSOCIATE ";
      if (onlyActive)
      {
        query += " WHERE DATA_FINE_VALIDITA IS NULL ";
      }
      query += " ORDER BY ID_PIANTE_CONSOCIATE ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing getListPianteConsociate: " + query
          + "\n");

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        TipoPiantaConsociataVO tipoPiantaConsociataVO = new TipoPiantaConsociataVO();
        tipoPiantaConsociataVO.setIdPianteConsociate(new Long(rs
            .getLong("ID_PIANTE_CONSOCIATE")));
        tipoPiantaConsociataVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoPiantaConsociataVO.setDataInizioValidita(rs
            .getDate("DATA_INIZIO_VALIDITA"));
        tipoPiantaConsociataVO.setDataFineValidita(rs
            .getDate("DATA_FINE_VALIDITA"));
        elencoPiante.add(tipoPiantaConsociataVO);
      }

      stmt.close();
      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "getListPianteConsociate in CommonDAO - SQLException: "
              + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "getListPianteConsociate in CommonDAO - Generic Exception: " + ex
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
                "getListPianteConsociate in CommonDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getListPianteConsociate in CommonDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this,
        "Invocated getListPianteConsociate method in CommonDAO\n");
    return elencoPiante;
  }

  /**
   * Metodo che si occupa di reperire i dati dalla tabella DB_TIPO_IMPIANTO
   * 
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.TipoImpiantoVO[]
   * @throws DataAccessException
   */
  public TipoImpiantoVO[] getListTipoImpianto(boolean onlyActive, String orderBy)
      throws DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating getListTipoImpianto method in CommonDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoImpiantoVO> elencoImpianti = new Vector<TipoImpiantoVO>();

    try
    {
      SolmrLogger
          .debug(this,
              "Creating db-connection in getListTipoImpianto method in CommonDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in getListTipoImpianto method in CommonDAO and it values: "
                  + conn + "\n");

      String query = " SELECT ID_IMPIANTO, " + "        DESCRIZIONE, "
          + "        DATA_INIZIO_VALIDITA, " + "        DATA_FINE_VALIDITA, "
          + "        FLAG_PIANTE_CONSOCIATE " + " FROM   DB_TIPO_IMPIANTO ";
      if (onlyActive)
      {
        query += " WHERE DATA_FINE_VALIDITA IS NULL ";
      }
      if (Validator.isNotEmpty(orderBy))
      {
        query += " ORDER BY " + orderBy;
      }
      else
      {
        query += " ORDER BY " + SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION;
      }

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing getListTipoImpianto: " + query + "\n");

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        TipoImpiantoVO tipoImpiantoVO = new TipoImpiantoVO();
        tipoImpiantoVO.setIdImpianto(new Long(rs.getLong("ID_IMPIANTO")));
        tipoImpiantoVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoImpiantoVO
            .setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        tipoImpiantoVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        tipoImpiantoVO.setFlagPianteConsociate(rs
            .getString("FLAG_PIANTE_CONSOCIATE"));
        elencoImpianti.add(tipoImpiantoVO);
      }

      stmt.close();
      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "getListTipoImpianto in CommonDAO - SQLException: "
              + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "getListTipoImpianto in CommonDAO - Generic Exception: " + ex + "\n");
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
                "getListTipoImpianto in CommonDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getListTipoImpianto in CommonDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this,
        "Invocated getListTipoImpianto method in CommonDAO\n");
    if (elencoImpianti.size() == 0)
    {
      return (TipoImpiantoVO[]) elencoImpianti.toArray(new TipoImpiantoVO[0]);
    }
    else
    {
      return (TipoImpiantoVO[]) elencoImpianti
          .toArray(new TipoImpiantoVO[elencoImpianti.size()]);
    }
  }

  /**
   * Metodo che si occupa di reperire i dati dalla tabella DB_TIPO_IRRIGAZIONE
   * 
   * @param orderBy
   * @param onlyActive
   * @return it.csi.solmr.dto.anag.terreni.TipoIrrigazioneVO[]
   * @throws DataAccessException
   */
  public TipoIrrigazioneVO[] getListTipoIrrigazione(String orderBy,
      boolean onlyActive) throws DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating getListTipoIrrigazione method in CommonDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoIrrigazioneVO> elencoTipiIrrigazione = new Vector<TipoIrrigazioneVO>();

    try
    {
      SolmrLogger
          .debug(this,
              "Creating db-connection in getListTipoIrrigazione method in CommonDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in getListTipoIrrigazione method in CommonDAO and it values: "
                  + conn + "\n");

      String query = " SELECT ID_IRRIGAZIONE, " + "        DESCRIZIONE, "
          + "        DATA_INIZIO_VALIDITA, " + "        DATA_FINE_VALIDITA "
          + " FROM   DB_TIPO_IRRIGAZIONE ";

      if (onlyActive)
      {
        query += " WHERE DATA_FINE_VALIDITA IS NULL ";
      }
      if (Validator.isNotEmpty(orderBy))
      {
        query += " ORDER BY " + orderBy;
      }
      else
      {
        query += " ORDER BY " + SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION;
      }

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing getListTipoIrrigazione: " + query
          + "\n");

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        TipoIrrigazioneVO tipoIrrigazioneVO = new TipoIrrigazioneVO();
        tipoIrrigazioneVO.setIdIrrigazione(new Long(rs
            .getLong("ID_IRRIGAZIONE")));
        tipoIrrigazioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoIrrigazioneVO.setDataInizioValidita(rs
            .getDate("DATA_INIZIO_VALIDITA"));
        tipoIrrigazioneVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        elencoTipiIrrigazione.add(tipoIrrigazioneVO);
      }

      stmt.close();
      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "getListTipoIrrigazione in CommonDAO - SQLException: "
              + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "getListTipoIrrigazione in CommonDAO - Generic Exception: " + ex
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
                "getListTipoIrrigazione in CommonDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getListTipoIrrigazione in CommonDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this,
        "Invocated getListTipoIrrigazione method in CommonDAO\n");
    if (elencoTipiIrrigazione.size() == 0)
    {
      return (TipoIrrigazioneVO[]) elencoTipiIrrigazione
          .toArray(new TipoIrrigazioneVO[0]);
    }
    else
    {
      return (TipoIrrigazioneVO[]) elencoTipiIrrigazione
          .toArray(new TipoIrrigazioneVO[elencoTipiIrrigazione.size()]);
    }
  }

  /**
   * Metodo che si occupa di estrarre l'impianto a partire dalla sua chiave
   * primaria
   * 
   * @param idImpianto
   * @return it.csi.solmr.dto.anag.terreni.TipoImpiantoVO
   * @throws DataAccessException
   */
  public TipoImpiantoVO findTipoImpiantoByPrimaryKey(Long idImpianto)
      throws DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating findTipoImpiantoByPrimaryKey method in CommonDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    TipoImpiantoVO tipoImpiantoVO = null;

    try
    {
      SolmrLogger
          .debug(this,
              "Creating db-connection in findTipoImpiantoByPrimaryKey method in CommonDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in findTipoImpiantoByPrimaryKey method in CommonDAO and it values: "
                  + conn + "\n");

      String query = " SELECT ID_IMPIANTO, " + "        DESCRIZIONE, "
          + "        DATA_INIZIO_VALIDITA, " + "        DATA_FINE_VALIDITA, "
          + "        FLAG_PIANTE_CONSOCIATE " + " FROM   DB_TIPO_IMPIANTO "
          + " WHERE  ID_IMPIANTO = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [ID_IMPIANTO] in findTipoImpiantoByPrimaryKey method in CommonDAO: "
                  + idImpianto + "\n");
      stmt.setLong(1, idImpianto.longValue());

      SolmrLogger.debug(this, "Executing findTipoImpiantoByPrimaryKey: "
          + query + "\n");

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        tipoImpiantoVO = new TipoImpiantoVO();
        tipoImpiantoVO.setIdImpianto(new Long(rs.getLong("ID_IMPIANTO")));
        tipoImpiantoVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoImpiantoVO
            .setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        tipoImpiantoVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        tipoImpiantoVO.setFlagPianteConsociate(rs
            .getString("FLAG_PIANTE_CONSOCIATE"));
      }

      stmt.close();
      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "findTipoImpiantoByPrimaryKey in CommonDAO - SQLException: "
              + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "findTipoImpiantoByPrimaryKey in CommonDAO - Generic Exception: "
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
                "findTipoImpiantoByPrimaryKey in CommonDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "findTipoImpiantoByPrimaryKey in CommonDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this,
        "Invocated findTipoImpiantoByPrimaryKey method in CommonDAO\n");
    return tipoImpiantoVO;
  }

  /**
   * Metodo che mi restituisce il Comune in funzione dei parametri, del suo
   * stato di vita e dello stato del catasto.
   * 
   * @param descComune
   * @param siglaProvincia
   * @param flagEstinto
   * @param flagCatastoAttivo
   * @param flagEstero
   * @return it.csi.solmr.dto.ComuneVO
   * @throws DataAccessException
   */
  public ComuneVO getComuneByParameters(String descComune,
      String siglaProvincia, String flagEstinto, String flagCatastoAttivo,
      String flagEstero) throws DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating getComuneByParameters method in CommonDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    ComuneVO comuneVO = null;

    try
    {
      SolmrLogger
          .debug(this,
              "Creating db-connection in getComuneByParameters method in CommonDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in getComuneByParameters method in CommonDAO and it values: "
                  + conn + "\n");

      String query = 
        "SELECT C.ISTAT_COMUNE, " +
        "       C.ISTAT_PROVINCIA, " +
        "       P.SIGLA_PROVINCIA, " +
        "       C.DESCOM, " +
        "       C.CAP, " +
        "       C.CODFISC, " +
        "       C.MONTANA, " +
        "       C.ZONAALT, " +
        "       C.USSL, " +
        "       C.ZONAALTS1, " +
        "       C.ZONAALTS2, " +
        "       C.REGAGRI, " +
        "       C.PROV_OLD, " +
        "       C.COMUNE_OLD, " +
        "       C.USL_OLD, " +
        "       C.PROV_NEW, " +
        "       C.FLAG_ESTERO, " +
        "       C.FLAG_ESTINTO, " +
        "       C.SIGLA_ESTERO, " +
        "       C.FLAG_CATASTO_ATTIVO, " +
        "       C.GESTIONE_SEZIONE " +
        "FROM   COMUNE C, " +
        "       PROVINCIA P " +
        "WHERE  C.DESCOM = ? " +
        "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA ";
      if (Validator.isNotEmpty(siglaProvincia))
      {
        query += " AND    P.SIGLA_PROVINCIA = ? ";
      }
      if (Validator.isNotEmpty(flagEstinto))
      {
        query += " AND    C.FLAG_ESTINTO = ? ";
      }
      if (Validator.isNotEmpty(flagCatastoAttivo))
      {
        query += " AND    C.FLAG_CATASTO_ATTIVO = ? ";
      }
      if (Validator.isNotEmpty(flagEstero))
      {
        query += " AND    C.FLAG_ESTERO = ? ";
      }

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [DESC_COMUNE] in getComuneByParameters method in CommonDAO: "
                  + descComune + "\n");
      SolmrLogger
          .debug(
              this,
              "Value of parameter 2 [SIGLA_PROVINCIA] in getComuneByParameters method in CommonDAO: "
                  + siglaProvincia + "\n");
      SolmrLogger
          .debug(
              this,
              "Value of parameter 3 [FLAG_ESTINTO] in getComuneByParameters method in CommonDAO: "
                  + flagEstinto + "\n");
      SolmrLogger
          .debug(
              this,
              "Value of parameter 4 [FLAG_CATASTO_ATTIVO] in getComuneByParameters method in CommonDAO: "
                  + flagCatastoAttivo + "\n");
      SolmrLogger
          .debug(
              this,
              "Value of parameter 5 [FLAG_ESTERO] in getComuneByParameters method in CommonDAO: "
                  + flagEstero + "\n");

      stmt = conn.prepareStatement(query);

      stmt.setString(1, descComune.toUpperCase());
      int indice = 1;
      if (Validator.isNotEmpty(siglaProvincia))
      {
        stmt.setString(++indice, siglaProvincia.toUpperCase());
      }
      if (Validator.isNotEmpty(flagEstinto))
      {
        stmt.setString(++indice, flagEstinto);
      }
      if (Validator.isNotEmpty(flagCatastoAttivo))
      {
        stmt.setString(++indice, flagCatastoAttivo);
      }
      if (Validator.isNotEmpty(flagEstero))
      {
        stmt.setString(++indice, flagEstero);
      }

      SolmrLogger.debug(this, "Executing getComuneByParameters: " + query
          + "\n");

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        comuneVO = new ComuneVO();
        comuneVO.setIstatComune(rs.getString("ISTAT_COMUNE"));
        comuneVO.setIstatProvincia(rs.getString("ISTAT_PROVINCIA"));
        comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
        comuneVO.setDescom(rs.getString("DESCOM"));
        comuneVO.setCap(rs.getString("CAP"));
        comuneVO.setCodfisc(rs.getString("CODFISC"));
        comuneVO.setMontana(rs.getString("MONTANA"));
        comuneVO.setZonaAlt(new Long(rs.getLong("ZONAALT")));
        comuneVO.setUssl(rs.getString("USSL"));
        comuneVO.setZonaAltS1(rs.getString("ZONAALTS1"));
        comuneVO.setZonaAltS2(rs.getString("ZONAALTS2"));
        comuneVO.setRegAgri(rs.getString("REGAGRI"));
        comuneVO.setProvOld(rs.getString("PROV_OLD"));
        comuneVO.setComuneOld(rs.getString("COMUNE_OLD"));
        comuneVO.setUslOld(rs.getString("USL_OLD"));
        comuneVO.setProvNew(rs.getString("PROV_NEW"));
        comuneVO.setFlagEstero(rs.getString("FLAG_ESTERO"));
        comuneVO.setFlagEstinto(rs.getString("FLAG_ESTINTO"));
        comuneVO.setSiglaEstero(rs.getString("SIGLA_ESTERO"));
        comuneVO.setFlagCatastoAttivo(rs.getString("FLAG_CATASTO_ATTIVO"));
        comuneVO.setGestioneSezione(rs.getString("GESTIONE_SEZIONE"));
      }

      stmt.close();
      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "getComuneByParameters in CommonDAO - SQLException: "
              + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "getComuneByParameters in CommonDAO - Generic Exception: " + ex
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
                "getComuneByParameters in CommonDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getComuneByParameters in CommonDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this,
        "Invocated getComuneByParameters method in CommonDAO\n");
    return comuneVO;
  }

  /**
   * Metodo che mi consente di reperire l'elenco dei comuni in funzione dei
   * parametri di ricerca associati
   * 
   * @param descComune
   * @param siglaProvincia
   * @param flagEstinto
   * @param flagCatastoAttivo
   * @param flagEstero
   * @param orderBy
   * @return java.util.Vector
   * @throws DataAccessException
   * @throws SolmrException
   */
  public Vector<ComuneVO> getComuniByParameters(String descComune,
      String siglaProvincia, String flagEstinto, String flagCatastoAttivo,
      String flagEstero, String[] orderBy) throws DataAccessException,
      SolmrException
  {
    SolmrLogger.debug(this, "Invocating getComuniByParameters method in CommonDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ComuneVO> elencoComuni = new Vector<ComuneVO>();

    try
    {
      SolmrLogger.debug(this, "Creating db-connection in getComuniByParameters method in CommonDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getComuniByParameters method in CommonDAO and it values: " + conn + "\n");

      String query = 
        "SELECT C.ISTAT_COMUNE, " + 
        "       C.ISTAT_PROVINCIA, " +
        "       P.SIGLA_PROVINCIA, " + 
        "       C.DESCOM, " +
        "       C.CAP, " + 
        "       C.CODFISC, " +
        "       C.MONTANA, " +
        "       C.ZONAALT, " +
        "       C.USSL, " +
        "       C.ZONAALTS1, " +
        "       C.ZONAALTS2, " +
        "       C.REGAGRI, " +
        "       C.PROV_OLD, " +
        "       C.COMUNE_OLD, " +
        "       C.USL_OLD, " +
        "       C.PROV_NEW, " +
        "       C.FLAG_ESTERO, " +
        "       C.FLAG_ESTINTO, " +
        "       C.SIGLA_ESTERO, " +
        "       C.FLAG_CATASTO_ATTIVO " +
        "FROM   COMUNE C, " +
        "       PROVINCIA P " +
        "WHERE  C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA ";
      if (Validator.isNotEmpty(descComune))
      {
        query += 
        "AND    C.DESCOM LIKE ? ";
      }
      if (Validator.isNotEmpty(siglaProvincia))
      {
        query += 
        "AND    P.SIGLA_PROVINCIA LIKE ? ";
      }
      if (Validator.isNotEmpty(flagEstinto))
      {
        query += 
        "AND    C.FLAG_ESTINTO = ? ";
      }
      if (Validator.isNotEmpty(flagCatastoAttivo))
      {
        query +=
        "AND    C.FLAG_CATASTO_ATTIVO = ? ";
      }
      if (Validator.isNotEmpty(flagEstero))
      {
        query +=
        "AND    C.FLAG_ESTERO = ? ";
      }
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
        query += "ORDER BY " + criterio;
      }

      SolmrLogger.debug(this, "Value of parameter 1 [DESC_COMUNE] in getComuniByParameters method in CommonDAO: " + descComune + "\n");
      SolmrLogger.debug(this, "Value of parameter 2 [SIGLA_PROVINCIA] in getComuniByParameters method in CommonDAO: " + siglaProvincia + "\n");
      SolmrLogger.debug(this, "Value of parameter 3 [FLAG_ESTINTO] in getComuniByParameters method in CommonDAO: " + flagEstinto + "\n");
      SolmrLogger.debug(this, "Value of parameter 4 [FLAG_CATASTO_ATTIVO] in getComuniByParameters method in CommonDAO: " + flagCatastoAttivo + "\n");
      SolmrLogger.debug(this, "Value of parameter 5 [FLAG_ESTERO] in getComuniByParameters method in CommonDAO: " + flagEstero + "\n");
      SolmrLogger.debug(this, "Value of parameter 6 [ORDER_BY] in getComuniByParameters method in CommonDAO: " + orderBy + "\n");

      stmt = conn.prepareStatement(query);
      stmt.setQueryTimeout(SolmrConstants.MIN_TIME_WAIT);

      int indice = 0;
      if (Validator.isNotEmpty(descComune))
      {
        stmt.setString(++indice, "%" + descComune.toUpperCase() + "%");
      }
      if (Validator.isNotEmpty(siglaProvincia))
      {
        stmt.setString(++indice, siglaProvincia.toUpperCase());
      }
      if (Validator.isNotEmpty(flagEstinto))
      {
        stmt.setString(++indice, flagEstinto);
      }
      if (Validator.isNotEmpty(flagCatastoAttivo))
      {
        stmt.setString(++indice, flagCatastoAttivo);
      }
      if (Validator.isNotEmpty(flagEstero))
      {
        stmt.setString(++indice, flagEstero);
      }

      SolmrLogger.debug(this, "Executing getComuniByParameters: " + query + "\n");

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        ComuneVO comuneVO = new ComuneVO();
        comuneVO.setIstatComune(rs.getString("ISTAT_COMUNE"));
        comuneVO.setIstatProvincia(rs.getString("ISTAT_PROVINCIA"));
        comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
        comuneVO.setDescrProv(rs.getString("SIGLA_PROVINCIA"));
        comuneVO.setDescom(rs.getString("DESCOM"));
        comuneVO.setCap(rs.getString("CAP"));
        comuneVO.setCodfisc(rs.getString("CODFISC"));
        comuneVO.setMontana(rs.getString("MONTANA"));
        comuneVO.setZonaAlt(new Long(rs.getLong("ZONAALT")));
        comuneVO.setUssl(rs.getString("USSL"));
        comuneVO.setZonaAltS1(rs.getString("ZONAALTS1"));
        comuneVO.setZonaAltS2(rs.getString("ZONAALTS2"));
        comuneVO.setRegAgri(rs.getString("REGAGRI"));
        comuneVO.setProvOld(rs.getString("PROV_OLD"));
        comuneVO.setComuneOld(rs.getString("COMUNE_OLD"));
        comuneVO.setUslOld(rs.getString("USL_OLD"));
        comuneVO.setProvNew(rs.getString("PROV_NEW"));
        comuneVO.setFlagEstero(rs.getString("FLAG_ESTERO"));
        comuneVO.setFlagEstinto(rs.getString("FLAG_ESTINTO"));
        comuneVO.setSiglaEstero(rs.getString("SIGLA_ESTERO"));
        comuneVO.setFlagCatastoAttivo(rs.getString("FLAG_CATASTO_ATTIVO"));
        elencoComuni.add(comuneVO);
      }

      stmt.close();
      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "getComuniByParameters in CommonDAO - SQLException: " + exc + "\n");
      if (exc.getErrorCode() == SolmrConstants.ORACLE_PREPARE_STATEMENT_TIME_OUT)
      {
        throw new SolmrException(AnagErrors.ERRORE_FILTRI_GENERICI);
      }
      else
      {
        throw new DataAccessException(exc.getMessage());
      }
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this, "getComuniByParameters in CommonDAO - Generic Exception: " + ex + "\n");
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
        SolmrLogger.error(this, "getComuniByParameters in CommonDAO - SQLException while closing Statement and Connection: " + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.error(this, "getComuniByParameters in CommonDAO - Generic Exception while closing Statement and Connection: " + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getComuniByParameters method in CommonDAO\n");
    return elencoComuni;
  }
  
  public Vector<ComuneVO> getComuniAttiviByIstatProvincia(String istatProvincia) 
    throws DataAccessException, SolmrException
  {
    SolmrLogger.debug(this, "Invocating getComuniAttiviByIstatProvincia method in CommonDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ComuneVO> elencoComuni = new Vector<ComuneVO>();

    try
    {
      SolmrLogger.debug(this, "Creating db-connection in getComuniAttiviByIstatProvincia method in CommonDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getComuniAttiviByIstatProvincia method in CommonDAO and it values: " + conn + "\n");

      String query = 
        "SELECT C.ISTAT_COMUNE, " + 
        "       C.ISTAT_PROVINCIA, " +
        "       P.SIGLA_PROVINCIA, " + 
        "       C.DESCOM, " +
        "       C.CAP, " + 
        "       C.CODFISC, " +
        "       C.MONTANA, " +
        "       C.ZONAALT, " +
        "       C.USSL, " +
        "       C.ZONAALTS1, " +
        "       C.ZONAALTS2, " +
        "       C.REGAGRI, " +
        "       C.PROV_OLD, " +
        "       C.COMUNE_OLD, " +
        "       C.USL_OLD, " +
        "       C.PROV_NEW, " +
        "       C.FLAG_ESTERO, " +
        "       C.FLAG_ESTINTO, " +
        "       C.SIGLA_ESTERO, " +
        "       C.FLAG_CATASTO_ATTIVO " +
        "FROM   COMUNE C, " +
        "       PROVINCIA P " +
        "WHERE  C.ISTAT_PROVINCIA = ? " +
        "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
        "AND    C.FLAG_ESTINTO = 'N' " +
        "AND    C.FLAG_CATASTO_ATTIVO = 'S' " +
        "AND    C.FLAG_ESTERO = 'N' " +
        "ORDER BY C.DESCOM ";

      stmt = conn.prepareStatement(query);
      stmt.setQueryTimeout(SolmrConstants.MIN_TIME_WAIT);

      int indice = 0;
      stmt.setString(++indice, istatProvincia);

      SolmrLogger.debug(this, "Executing getComuniAttiviByIstatProvincia: " + query + "\n");

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        ComuneVO comuneVO = new ComuneVO();
        comuneVO.setIstatComune(rs.getString("ISTAT_COMUNE"));
        comuneVO.setIstatProvincia(rs.getString("ISTAT_PROVINCIA"));
        comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
        comuneVO.setDescrProv(rs.getString("SIGLA_PROVINCIA"));
        comuneVO.setDescom(rs.getString("DESCOM"));
        comuneVO.setCap(rs.getString("CAP"));
        comuneVO.setCodfisc(rs.getString("CODFISC"));
        comuneVO.setMontana(rs.getString("MONTANA"));
        comuneVO.setZonaAlt(new Long(rs.getLong("ZONAALT")));
        comuneVO.setUssl(rs.getString("USSL"));
        comuneVO.setZonaAltS1(rs.getString("ZONAALTS1"));
        comuneVO.setZonaAltS2(rs.getString("ZONAALTS2"));
        comuneVO.setRegAgri(rs.getString("REGAGRI"));
        comuneVO.setProvOld(rs.getString("PROV_OLD"));
        comuneVO.setComuneOld(rs.getString("COMUNE_OLD"));
        comuneVO.setUslOld(rs.getString("USL_OLD"));
        comuneVO.setProvNew(rs.getString("PROV_NEW"));
        comuneVO.setFlagEstero(rs.getString("FLAG_ESTERO"));
        comuneVO.setFlagEstinto(rs.getString("FLAG_ESTINTO"));
        comuneVO.setSiglaEstero(rs.getString("SIGLA_ESTERO"));
        comuneVO.setFlagCatastoAttivo(rs.getString("FLAG_CATASTO_ATTIVO"));
        elencoComuni.add(comuneVO);
      }

      stmt.close();
      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "getComuniAttiviByIstatProvincia in CommonDAO - SQLException: " + exc + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,"getSezioneByParameters in CommonDAO - Generic Exception: " + ex+ "\n");
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
        SolmrLogger.error(this, "getComuniAttiviByIstatProvincia in CommonDAO - SQLException while closing Statement and Connection: " + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.error(this, "getComuniAttiviByIstatProvincia in CommonDAO - Generic Exception while closing Statement and Connection: " + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getComuniAttiviByIstatProvincia method in CommonDAO\n");
    
    return elencoComuni;
  }

  /**
   * Metodo che mi permette di recuperare la sezione a partire dai parametri
   * 
   * @param istatComune
   * @param sezione
   * @return it.csi.solmr.dto.anag.terreni.SezioneVO
   * @throws DataAccessException
   */
  public SezioneVO getSezioneByParameters(String istatComune, String sezione)
      throws DataAccessException
  {
    SolmrLogger.debug(this, "Invocating getSezioneByParameters method in CommonDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    SezioneVO sezioneVO = null;

    try
    {
      SolmrLogger.debug(this,"Creating db-connection in getSezioneByParameters method in CommonDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getSezioneByParameters method in CommonDAO and it values: "+ conn + "\n");

      String query = 
        "SELECT ISTAT_COMUNE, " +
        "       SEZIONE, " +
        "       DESCRIZIONE " +
        "FROM   DB_SEZIONE " +
        "WHERE  ISTAT_COMUNE = ? " +
        "AND    SEZIONE = ? ";

      SolmrLogger.debug(this,"Value of parameter 1 [ISTAT_COMUNE] in getSezioneByParameters method in CommonDAO: "+ istatComune + "\n");
      SolmrLogger.debug(this,"Value of parameter 2 [SEZIONE] in getSezioneByParameters method in CommonDAO: "+ sezione + "\n");

      stmt = conn.prepareStatement(query);

      stmt.setString(1, istatComune);
      stmt.setString(2, sezione.toUpperCase());

      SolmrLogger.debug(this, "Executing query: " + query);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        sezioneVO = new SezioneVO();
        sezioneVO.setIstatComune(rs.getString("ISTAT_COMUNE"));
        sezioneVO.setSezione(rs.getString("SEZIONE"));
        sezioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
      }

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,"getSezioneByParameters in CommonDAO - SQLException: "+ exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,"getSezioneByParameters in CommonDAO - Generic Exception: " + ex+ "\n");
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
        SolmrLogger.error(this,"getSezioneByParameters in CommonDAO - SQLException while closing Statement and Connection: "+ exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.error(this,"getSezioneByParameters in CommonDAO - Generic Exception while closing Statement and Connection: "+ ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this,"Invocated getSezioneByParameters method in CommonDAO\n");
    return sezioneVO;
  }

  /**
   * Metodo che si occupa di reperire i dati dalla tabella DB_TIPO_CESSAZIONE
   * 
   * @param orderBy
   * @param onlyActive
   * @return it.csi.solmr.dto.anag.TipoCessazioneVO[]
   * @throws DataAccessException
   */
  public TipoCessazioneVO[] getListTipoCessazione(String orderBy,
      boolean onlyActive) throws DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating getListTipoCessazione method in CommonDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoCessazioneVO> elencoTipiCessazione = new Vector<TipoCessazioneVO>();

    try
    {
      SolmrLogger
          .debug(this,
              "Creating db-connection in getListTipoCessazione method in CommonDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in getListTipoCessazione method in CommonDAO and it values: "
                  + conn + "\n");

      String query = " SELECT ID_CESSAZIONE, " + "        DESCRIZIONE, "
          + "        DATA_INIZIO_VALIDITA, " + "        DATA_FINE_VALIDITA "
          + " FROM   DB_TIPO_CESSAZIONE ";

      if (onlyActive)
      {
        query += " WHERE DATA_FINE_VALIDITA IS NULL ";
      }
      if (Validator.isNotEmpty(orderBy))
      {
        query += " ORDER BY " + orderBy;
      }
      else
      {
        query += " ORDER BY " + SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION;
      }

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing getListTipoCessazione: " + query
          + "\n");

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        TipoCessazioneVO tipoCessazioneVO = new TipoCessazioneVO();
        tipoCessazioneVO.setIdCessazione(new Long(rs.getLong("ID_CESSAZIONE")));
        tipoCessazioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoCessazioneVO.setDataInizioValidita(rs
            .getDate("DATA_INIZIO_VALIDITA"));
        tipoCessazioneVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        elencoTipiCessazione.add(tipoCessazioneVO);
      }

      stmt.close();
      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "getListTipoCessazione in CommonDAO - SQLException: "
              + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "getListTipoCessazione in CommonDAO - Generic Exception: " + ex
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
                "getListTipoCessazione in CommonDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getListTipoCessazione in CommonDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this,
        "Invocated getListTipoCessazione method in CommonDAO\n");
    if (elencoTipiCessazione.size() == 0)
    {
      return (TipoCessazioneVO[]) elencoTipiCessazione
          .toArray(new TipoCessazioneVO[0]);
    }
    else
    {
      return (TipoCessazioneVO[]) elencoTipiCessazione
          .toArray(new TipoCessazioneVO[elencoTipiCessazione.size()]);
    }
  }

  /**
   * Metodo per estrarre i tipi macro uso associati ad un'azienda agricola
   * 
   * @param idAzienda
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.TipoMacroUsoVO[]
   * @throws DataAccessException
   */
  public TipoMacroUsoVO[] getListTipoMacroUsoByIdAzienda(Long idAzienda,
      boolean onlyActive, String[] orderBy) throws DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating getListTipoMacroUsoByIdAzienda method in CommonDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoMacroUsoVO> elencoMacroUso = new Vector<TipoMacroUsoVO>();

    try
    {
      SolmrLogger
          .debug(
              this,
              "Creating db-connection in getListTipoMacroUsoByIdAzienda method in CommonDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in getListTipoMacroUsoByIdAzienda method in CommonDAO and it values: "
                  + conn + "\n");

      String query = " SELECT   TMU.ID_MACRO_USO, "
          + "          TMU.CODICE, "
          + "          TMU.DESCRIZIONE "
          + " FROM     DB_CONDUZIONE_PARTICELLA CP, "
          + "          DB_UTILIZZO_PARTICELLA UP, "
          + "          DB_R_CATALOGO_MATRICE RCM, "
          + "          DB_UTE U, "
          + "          DB_STORICO_PARTICELLA SP, "
          + "          DB_TIPO_MACRO_USO TMU, "
          + "          DB_TIPO_MACRO_USO_VARIETA TMUV "
          + " WHERE    U.ID_AZIENDA = ? "
          + " AND      U.ID_UTE = CP.ID_UTE "
          + " AND      CP.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND      CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA "
          + " AND      UP.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE "
          + " AND      RCM.ID_CATALOGO_MATRICE = TMUV.ID_CATALOGO_MATRICE "
          + " AND      TMUV.ID_MACRO_USO = TMU.ID_MACRO_USO ";

      if (onlyActive)
      {
        query += " AND U.DATA_FINE_ATTIVITA IS NULL "
            + " AND CP.DATA_FINE_CONDUZIONE IS NULL "
            + " AND SP.DATA_FINE_VALIDITA IS NULL "
            + " AND TMUV.DATA_FINE_VALIDITA IS NULL ";
      }
      query += " GROUP BY TMU.ID_MACRO_USO, " + "          TMU.CODICE, "
          + "          TMU.DESCRIZIONE ";

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

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idAzienda.longValue());

      SolmrLogger.debug(this, "Executing getListTipoMacroUsoByIdAzienda: "
          + query + "\n");

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        TipoMacroUsoVO tipoMacroUsoVO = new TipoMacroUsoVO();
        tipoMacroUsoVO.setIdMacroUso(new Long(rs.getLong("ID_MACRO_USO")));
        tipoMacroUsoVO.setCodice(rs.getString("CODICE"));
        tipoMacroUsoVO.setDescrizione(rs.getString("DESCRIZIONE"));
        elencoMacroUso.add(tipoMacroUsoVO);
      }

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "getListTipoMacroUsoByIdAzienda in CommonDAO - SQLException: " + exc
              + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "getListTipoMacroUsoByIdAzienda in CommonDAO - Generic Exception: "
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
                "getListTipoMacroUsoByIdAzienda in CommonDAO - SQLException while closing Statement and Connection: "
                    + exc + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getListTipoMacroUsoByIdAzienda in CommonDAO - Generic Exception while closing Statement and Connection: "
                    + ex + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this,
        "Invocated getListTipoMacroUsoByIdAzienda method in CommonDAO\n");
    if (elencoMacroUso.size() == 0)
    {
      return (TipoMacroUsoVO[]) elencoMacroUso.toArray(new TipoMacroUsoVO[0]);
    }
    else
    {
      return (TipoMacroUsoVO[]) elencoMacroUso
          .toArray(new TipoMacroUsoVO[elencoMacroUso.size()]);
    }
  }

  // Metodo per recuperare le decodifiche dei codici SIAN
  public CodeDescription getDescrizioneOrientamentoFromCode(String codSpecie,
      String codOrient, String codProd) throws DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating method getDescrizioneOrientamentoFromCode");

    CodeDescription result = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      SolmrLogger.debug(this,
          "Creating connection in method getDescrizioneOrientamentoFromCode");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this,
          "Created connection in method getDescrizioneOrientamentoFromCode with value: "
              + conn);

      String search = " SELECT TOP.DESCRIZIONE,TOP.ID_ORIENTAMENTO_PRODUTTIVO "
          + " FROM DB_SIAN_SPECIE_ORIENTAM_PROD SSOP, DB_TIPO_ORIENTAMENTO_PRODUT TOP ,"
          + " DB_TIPO_TIPO_PRODUZIONE TTP "
          + " WHERE SSOP.CODICE_SPECIE  = ? "
          + " AND SSOP.ID_ORIENTAMENTO_PRODUTTIVO = TOP.ID_ORIENTAMENTO_PRODUTTIVO "
          + " AND SSOP.ID_TIPO_PRODUZIONE=TTP.ID_TIPO_PRODUZIONE "
          + " AND TTP.CODICE = ? " + " AND TOP.CODICE = ? ";

      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "Executing query: " + search);

      SolmrLogger.debug(this, "Value of parameter 1: " + codSpecie);
      SolmrLogger.debug(this, "Value of parameter 2: " + codProd);
      SolmrLogger.debug(this, "Value of parameter 3: " + codOrient);

      stmt.setString(1, codSpecie);
      stmt.setString(2, codProd);
      stmt.setString(3, codOrient);

      ResultSet rs = stmt.executeQuery();
      SolmrLogger.debug(this, "Executed query");

      if (rs.next())
      {
        result = new CodeDescription();
        result.setCode(new Integer(rs.getInt("ID_ORIENTAMENTO_PRODUTTIVO")));
        result.setDescription(rs.getString("DESCRIZIONE"));
        SolmrLogger
            .debug(
                this,
                "Value of ID_ORIENTAMENTO_PRODUTTIVO in method getDescrizioneOrientamentoFromCode "
                    + result.getCode());
        SolmrLogger.debug(this,
            "Value of DESCRIZIONE in method getDescrizioneOrientamentoFromCode "
                + result.getDescription());
      }

      rs.close();
      SolmrLogger.debug(this,
          "Closed result set in method getDescrizioneOrientamentoFromCode");

      stmt.close();
      SolmrLogger.debug(this,
          "Closed statement in method getDescrizioneOrientamentoFromCode");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "SQLException in method getDescrizioneOrientamentoFromCode: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in method getDescrizioneOrientamentoFromCode: "
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
                "SQLException while closing Statement and Connection in method getDescrizioneOrientamentoFromCode: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in method getDescrizioneOrientamentoFromCode: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this,
        "Invocated method getDescrizioneOrientamentoFromCode");
    return result;
  }

  public Vector<CodeDescription> getMotiviDichiarazione()
      throws DataAccessException
  {
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT ID_MOTIVO_DICHIARAZIONE,DESCRIZIONE "
          + "FROM DB_TIPO_MOTIVO_DICHIARAZIONE "
          + "WHERE DATA_FINE_VALIDITA IS NULL " + "ORDER BY DESCRIZIONE";

      stmt = conn.prepareStatement(search);
      SolmrLogger.debug(this, "Executing query getMotiviDichiarazione: "
          + search);

      ResultSet rs = stmt.executeQuery();

      result = new Vector<CodeDescription>();
      while (rs.next())
      {
        CodeDescription cd = new CodeDescription();

        cd.setCode(new Integer(rs.getInt("ID_MOTIVO_DICHIARAZIONE")));
        cd.setDescription(rs.getString("DESCRIZIONE"));

        result.add(cd);
      }

      rs.close();

      SolmrLogger.debug(this, "Executed query getMotiviDichiarazione - Found "
          + result.size() + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getMotiviDichiarazione SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getMotiviDichiarazione Generic Exception: "
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
            "getMotiviDichiarazione SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getMotiviDichiarazione Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  /**
   * Recupero dei prefissi possibili per un cellulare
   * 
   * @return
   * @throws DataAccessException
   * @throws NotFoundException
   */
  public Vector<CodeDescription> getPrefissiCellulare()
      throws DataAccessException, NotFoundException
  {
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT ID_PREFISSO_CELLULARE,PREFISSO_CELLULARE "
          + "  FROM DB_TIPO_PREFISSO_CELLULARE "
          + "  WHERE DATA_FINE_VALIDITA IS NULL" + "  ORDER BY 1";

      stmt = conn.prepareStatement(search);
      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        result = new Vector<CodeDescription>();
        while (rs.next())
        {
          CodeDescription cd = new CodeDescription();

          cd.setCode(new Integer(rs.getInt(1)));
          cd.setDescription(rs.getString(2));

          result.add(cd);
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      if (result.size() == 0)
        throw new NotFoundException();

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    }
    catch (NotFoundException nfexc)
    {
      SolmrLogger.fatal(this, "NotFoundException: " + nfexc.getMessage());
      throw nfexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  /*****************************************************************************
   * /* I METODI SEGUENTI SERVONO AI REPORT DINAMICI
   */
  /** ********************************************* */
 /* public Htmpl executeQueryReportDin(String queryWithClause,
      HashMap<?, ?> variabili, HashMap<?, ?> colonneRisultato, Htmpl layout)
      throws SolmrException
  {
    final String THIS_METHOD = "executeQueryReportDin";
    Connection conn = null;

    Htmpl result = null;

    try
    {
      SolmrLogger.debug(this, "[CommonDAO::" + THIS_METHOD + "] BEGIN.");

      conn = getDatasource().getConnection();

      result = ReportQueryUtil.executeQueryReportDin(conn, queryWithClause,
          variabili, colonneRisultato, layout);

    }
    catch (SQLException exc)
    {
      if (exc.getErrorCode() == SolmrConstants.ORACLE_PREPARE_STATEMENT_TIME_OUT)
      {
        SolmrLogger.fatal(this, "Problemi di timeout SQLException: "
            + exc.getMessage());
        throw new SolmrException(SolmrConstants.TIME_OUT_QUERY_REPORTDIN);
      }
      else
      {
        SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
      throw new SolmrException(ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return result;
  }*/
  
  public Htmpl executeQueryReportDin(String queryWithClause,
		  VariabileReportOutput[] variabili, ColonneRisultatoOutput[] colonneRisultato, Htmpl layout)
	      throws SolmrException
	  {
	    final String THIS_METHOD = "executeQueryReportDin";
	    Connection conn = null;

	    Htmpl result = null;

	    try
	    {
	      SolmrLogger.debug(this, "[CommonDAO::" + THIS_METHOD + "] BEGIN.");

	      conn = getDatasource().getConnection();

	      result = ReportQueryUtil.executeQueryReportDin(conn, queryWithClause,
	          variabili, colonneRisultato, layout);

	    }
	    catch (SQLException exc)
	    {
	      if (exc.getErrorCode() == SolmrConstants.ORACLE_PREPARE_STATEMENT_TIME_OUT)
	      {
	        SolmrLogger.fatal(this, "Problemi di timeout SQLException: "
	            + exc.getMessage());
	        throw new SolmrException(SolmrConstants.TIME_OUT_QUERY_REPORTDIN);
	      }
	      else
	      {
	        SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
	        throw new SolmrException(exc.getMessage());
	      }
	    }
	    catch (Exception ex)
	    {
	      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
	      throw new SolmrException(ex.getMessage());
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
	            "SQLException while closing Statement and Connection: "
	                + exc.getMessage());
	        throw new SolmrException(exc.getMessage());
	      }
	      catch (Exception ex)
	      {
	        SolmrLogger.fatal(this,
	            "Generic Exception while closing Statement and Connection: "
	                + ex.getMessage());
	        throw new SolmrException(ex.getMessage());
	      }
	    }
	    return result;
	  }

 /* public HSSFWorkbook executeQueryReportDin(String queryWithClause,
      HashMap<?, ?> variabili, HashMap<?, ?> colonneRisultato,
      HSSFWorkbook workBook, String nomeFoglio) throws SolmrException
  {
    final String THIS_METHOD = "executeQueryReportDin";
    Connection conn = null;

    HSSFWorkbook result = null;

    try
    {
      SolmrLogger.debug(this, "[CommonDAO::" + THIS_METHOD + "] BEGIN.");

      conn = getDatasource().getConnection();

      result = ReportQueryUtil.executeQueryReportDin(conn, queryWithClause,
          variabili, colonneRisultato, workBook, nomeFoglio);

    }
    catch (SQLException exc)
    {
      if (exc.getErrorCode() == SolmrConstants.ORACLE_PREPARE_STATEMENT_TIME_OUT)
      {
        SolmrLogger.fatal(this, "Problemi di timeout SQLException: "
            + exc.getMessage());
        throw new SolmrException(SolmrConstants.TIME_OUT_QUERY_REPORTDIN);
      }
      else
      {
        SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
      throw new SolmrException(ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return result;
  }*/
  
  public HSSFWorkbook executeQueryReportDin(String queryWithClause,
	      VariabileReportOutput[] variabili, ColonneRisultatoOutput[] colonneRisultato,
	      HSSFWorkbook workBook, String nomeFoglio) throws SolmrException
	  {
	    final String THIS_METHOD = "executeQueryReportDin";
	    Connection conn = null;

	    HSSFWorkbook result = null;

	    try
	    {
	      SolmrLogger.debug(this, "[CommonDAO::" + THIS_METHOD + "] BEGIN.");

	      conn = getDatasource().getConnection();

	      result = ReportQueryUtil.executeQueryReportDin(conn, queryWithClause,
	          variabili, colonneRisultato, workBook, nomeFoglio);

	    }
	    catch (SQLException exc)
	    {
	      if (exc.getErrorCode() == SolmrConstants.ORACLE_PREPARE_STATEMENT_TIME_OUT)
	      {
	        SolmrLogger.fatal(this, "Problemi di timeout SQLException: "
	            + exc.getMessage());
	        throw new SolmrException(SolmrConstants.TIME_OUT_QUERY_REPORTDIN);
	      }
	      else
	      {
	        SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
	        throw new SolmrException(exc.getMessage());
	      }
	    }
	    catch (Exception ex)
	    {
	      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
	      throw new SolmrException(ex.getMessage());
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
	            "SQLException while closing Statement and Connection: "
	                + exc.getMessage());
	        throw new SolmrException(exc.getMessage());
	      }
	      catch (Exception ex)
	      {
	        SolmrLogger.fatal(this,
	            "Generic Exception while closing Statement and Connection: "
	                + ex.getMessage());
	        throw new SolmrException(ex.getMessage());
	      }
	    }
	    return result;
	  }

  /**
   * Carica le righe di valori legate alla query di popolamento
   * 
   * @param queryPopolamento
   * @return
   * @throws DataAccessException
   */
  public HashMap<?, ?> getQueryPopolamento(String queryPopolamento,
      String idRptVariabileReport) throws SolmrException
  {
    final String THIS_METHOD = "getQueryPopolamento";
    Connection conn = null;

    HashMap<?, ?> result = null;

    try
    {
      SolmrLogger.debug(this, "[CommonDAO::" + THIS_METHOD + "] BEGIN.");

      conn = getDatasource().getConnection();

      result = ReportQueryUtil.getQueryPopolamento(conn, queryPopolamento,
          idRptVariabileReport);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
      throw new SolmrException(ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return result;
  }

  

  public it.csi.solmr.dto.profile.CodeDescription getGruppoRuolo(String ruolo)
      throws DataAccessException
  {
    it.csi.solmr.dto.profile.CodeDescription result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT TGR.ID_TIPO_GRUPPO_RUOLO, TGR.DESCRIZIONE "
          + "FROM DB_D_TIPO_RUOLO TR, DB_D_TIPO_GRUPPO_RUOLO TGR "
          + "WHERE TGR.ID_TIPO_GRUPPO_RUOLO=TR.ID_TIPO_GRUPPO_RUOLO "
          + "AND TR.RUOLO=? ";

      stmt = conn.prepareStatement(search);
      SolmrLogger.debug(this, "getGruppoRuolo Executing query: " + search);
      SolmrLogger.debug(this, "getGruppoRuolo param ruolo: " + ruolo);

      stmt.setString(1, ruolo);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        result = new it.csi.solmr.dto.profile.CodeDescription();

        result.setCode(new Integer(rs.getInt(1)));
        result.setDescription(rs.getString(2));
      }
      rs.close();

      SolmrLogger.debug(this, "getGruppoRuolo Executed query - Found ");
      return result;
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getGruppoRuolo SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getGruppoRuolo Generic Exception: "
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
            "getGruppoRuolo SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "getGruppoRuolo Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  public String testDB()
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    String result = null;
    try
    {
      conn = getDatasource().getConnection();

      String select = "SELECT SYSDATE FROM DUAL";

      SolmrLogger.debug(this, "testDB " + select);

      stmt = conn.prepareStatement(select);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        rs.getDate(1);

      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "testDB SQLException: " + exc.getMessage());
      result = exc.getMessage();
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "testDB Generic Exception: " + ex.getMessage());
      result = ex.getMessage();
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
            "testDB SQLException while closing Statement and Connection: "
                + exc.getMessage());
        result = exc.getMessage();
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "testDB Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        result = ex.getMessage();
      }
    }
    return result;
  }

  public Vector<CodeDescription> getTipiAttivitaATECO(String code,
      String description) throws DataAccessException
  {
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
      Vector<String> daParameters = new Vector<String>();

      String search = "" + "SELECT " + "       ID_ATTIVITA_ATECO, "
          + "       CODICE," + "       DESCRIZIONE "
          + "FROM   DB_TIPO_ATTIVITA_ATECO ";
      if (code != null && code.trim().length() > 0)
      {
        search += " WHERE CODICE LIKE ? ";
        daParameters.add(code.trim() + "%");
      }
      if (description != null && description.trim().length() > 0)
      {
        if (code != null && code.trim().length() > 0)
          search += " AND ";
        else
          search += " WHERE ";
        search += " UPPER(DESCRIZIONE) LIKE ? ";
        daParameters.add("%" + description.trim().toUpperCase() + "%");
      }

      if (Validator.isEmpty(code) && Validator.isEmpty(description))
      {
        search += " WHERE DATA_FINE_VALIDITA IS NULL ";
      }
      else
      {
        search += " AND DATA_FINE_VALIDITA IS NULL ";
      }

      search += " ORDER BY 1";
      stmt = conn.prepareStatement(search);

      Iterator<String> iter = daParameters.iterator();
      int indice = 0;
      while (iter.hasNext())
      {
        String par = (String) iter.next();
        stmt.setString(++indice, par);
      }

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        if (result == null)
        {
          result = new Vector<CodeDescription>();
        }
        CodeDescription cd = new CodeDescription();
        cd.setCode(new Integer(rs.getInt(1)));
        cd.setSecondaryCode(rs.getString(2));
        cd.setDescription(rs.getString(3));
        result.add(cd);

      }

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  public String[] getUserPwdServizio(String nomeServizio)
      throws DataAccessException
  {
    String result[] = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT SAS.UTENTE, "
          + "PCK_UTL_OBFUSCATION.FNC_DECRYPT(PWD,SAS.ID_ANAGRAFICA_SERVIZI || '_' || "
          + "SAS.NOME_SERVIZIO || '_' || "
          + "SAS.ID_ANAGRAFICA_SERVIZI) AS PASSWORD "
          + "FROM SMRGAA_D_ANAGRAFICA_SERVIZI SAS " + "WHERE NOME_SERVIZIO=? ";

      stmt = conn.prepareStatement(search);
      SolmrLogger.debug(this, "getUserPwdServizio Executing query: " + search);
      SolmrLogger.debug(this, "getUserPwdServizio param nomeServizio: "
          + nomeServizio);

      stmt.setString(1, nomeServizio);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        result = new String[2];
        result[0] = rs.getString("UTENTE");
        result[1] = rs.getString("PASSWORD");
      }
      rs.close();

      SolmrLogger.debug(this, "getUserPwdServizio Executed query - Found ");
      return result;
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getUserPwdServizio SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getUserPwdServizio Generic Exception: "
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
            "getUserPwdServizio SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "getUserPwdServizio Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }
  
  /**
   * restituisce il codice fiscale usato dal Sina
   * in base al procedimento!
   * 
   * 
   * @param idProcedimento
   * @return
   * @throws DataAccessException
   */
  public String getCFFromProcedimento(long idProcedimento) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    String codiceFiscale = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[CommonDAO::getCFFromProcedimento] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("" +
              "SELECT " +
              "       CF_RESPONSABILE_AT " +
              "FROM   DB_TIPO_PROCEDIMENTO " +
              "WHERE  ID_PROCEDIMENTO = ? ");
          
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[CommonDAO::getCFFromProcedimento] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      int indice = 0;
      stmt.setLong(++indice,idProcedimento);
      
      
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        codiceFiscale = rs.getString("CF_RESPONSABILE_AT");
      }
      
      return codiceFiscale;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("codiceFiscale", codiceFiscale) };
      
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idProcedimento", idProcedimento) };
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[CommonDAO::getCFFromProcedimento] ",
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
              "[CommonDAO::getCFFromProcedimento] END.");
    }
  }
  
  /**
   * Mi ricavo l'istatComune attraverso il codice
   * fiscale del comune
   * 
   * 
   * 
   * @param codFisc
   * @return
   * @throws DataAccessException
   */
  public String getIstatComuneNonEstinfoFromCodFisc(String codFisc) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    String istatComune = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[CommonDAO::getIstatComuneNonEstinfoFromCodFisc] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("" +
              "SELECT " +
              "       C.ISTAT_COMUNE " +
              "FROM   COMUNE C " +
              "WHERE  C.CODFISC = ? " +
              "AND    C.FLAG_ESTINTO = 'N' ");     
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[CommonDAO::getIstatComuneNonEstinfoFromCodFisc] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      int indice = 0;
      stmt.setString(++indice,codFisc);
      
      
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        istatComune = rs.getString("ISTAT_COMUNE");
      }
      
      return istatComune;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("istatComune", istatComune) };
      
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("codFisc", codFisc) };
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[CommonDAO::getIstatComuneNonEstinfoFromCodFisc] ",
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
              "[CommonDAO::getIstatComuneNonEstinfoFromCodFisc] END.");
    }
  }
  
  /**
   * Metodo che si occupa di reperire i dati dalla tabella DB_TIPO_POTENZIALITA_IRRIGUA
   * 
   * @param orderBy
   * @param onlyActive
   * @return it.csi.solmr.dto.anag.terreni.TipoPotenzialitaIrriguaVO[]
   * @throws DataAccessException
   */
  public TipoPotenzialitaIrriguaVO[] getListTipoPotenzialitaIrrigua(String orderBy,
      Date dataRiferimento) throws DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating getListTipoPotenzialitaIrrigua method in CommonDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoPotenzialitaIrriguaVO> elencoTipiPotenzialitaIrrigua = new Vector<TipoPotenzialitaIrriguaVO>();

    try
    {
      SolmrLogger
          .debug(this,
              "Creating db-connection in getListTipoPotenzialitaIrrigua method in CommonDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in getListTipoPotenzialitaIrrigua method in CommonDAO and it values: "
                  + conn + "\n");

      String query = " " +
      		"SELECT ID_POTENZIALITA_IRRIGUA, " +
      		"        CODICE, " +
          "        DESCRIZIONE, " + 
          "        DATA_INIZIO_VALIDITA, " + 
          "        DATA_FINE_VALIDITA " +
          "FROM    DB_TIPO_POTENZIALITA_IRRIGUA ";

      if (Validator.isNotEmpty(dataRiferimento))
      {
        query += " " +
          "WHERE DATA_INIZIO_VALIDITA <= ? " +
          "AND NVL(DATA_FINE_VALIDITA,TO_DATE('31/12/9999','dd/mm/yyyy')) >= ? ";
      }
      else
      {
        query += " WHERE DATA_FINE_VALIDITA IS NULL ";
      }
      
      
      if (Validator.isNotEmpty(orderBy))
      {
        query += " ORDER BY " + orderBy;
      }
      else
      {
        query += " ORDER BY " + SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION;
      }

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing getListTipoPotenzialitaIrrigua: " + query
          + "\n");
      
      int idx = 0;
      
      if (Validator.isNotEmpty(dataRiferimento))
      {
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataRiferimento));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataRiferimento));
      }
      
      
      

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        TipoPotenzialitaIrriguaVO tipoPotenzialitaIrriguaVO = new TipoPotenzialitaIrriguaVO();
        tipoPotenzialitaIrriguaVO.setIdPotenzialitaIrrigua(new Long(rs
            .getLong("ID_POTENZIALITA_IRRIGUA")));
        tipoPotenzialitaIrriguaVO.setCodice(rs.getString("CODICE"));
        tipoPotenzialitaIrriguaVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoPotenzialitaIrriguaVO.setDataInizioValidita(rs
            .getDate("DATA_INIZIO_VALIDITA"));
        tipoPotenzialitaIrriguaVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        elencoTipiPotenzialitaIrrigua.add(tipoPotenzialitaIrriguaVO);
      }

      stmt.close();
      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "getListTipoPotenzialitaIrrigua in CommonDAO - SQLException: "
              + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "getListTipoPotenzialitaIrrigua in CommonDAO - Generic Exception: " + ex
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
                "getListTipoPotenzialitaIrrigua in CommonDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getListTipoPotenzialitaIrrigua in CommonDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this,
        "Invocated getListTipoPotenzialitaIrrigua method in CommonDAO\n");
    if (elencoTipiPotenzialitaIrrigua.size() == 0)
    {
      return (TipoPotenzialitaIrriguaVO[]) elencoTipiPotenzialitaIrrigua
          .toArray(new TipoPotenzialitaIrriguaVO[0]);
    }
    else
    {
      return (TipoPotenzialitaIrriguaVO[]) elencoTipiPotenzialitaIrrigua
          .toArray(new TipoPotenzialitaIrriguaVO[elencoTipiPotenzialitaIrrigua.size()]);
    }
  }
  
  
  
  /**
   * Metodo che si occupa di reperire i dati dalla tabella DB_TIPO_TERRAZZAMENTO
   * 
   * @param orderBy
   * @param onlyActive
   * @return it.csi.solmr.dto.anag.terreni.TipoTerrazzamentoVO[]
   * @throws DataAccessException
   */
  public TipoTerrazzamentoVO[] getListTipoTerrazzamento(String orderBy,
      Date dataRiferimento) throws DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating getListTipoTerrazzamento method in CommonDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoTerrazzamentoVO> elencoTipiTerrazzamento = new Vector<TipoTerrazzamentoVO>();

    try
    {
      SolmrLogger
          .debug(this,
              "Creating db-connection in getListTipoTerrazzamento method in CommonDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in getListTipoTerrazzamento method in CommonDAO and it values: "
                  + conn + "\n");

      String query = " " +
      		"SELECT ID_TERRAZZAMENTO, " +
      		"       CODICE, " +
      		"       DESCRIZIONE, " +
      		"       DATA_INIZIO_VALIDITA, " +
      		"       DATA_FINE_VALIDITA " +
      		"FROM   DB_TIPO_TERRAZZAMENTO ";

      
      if (Validator.isNotEmpty(dataRiferimento))
      {
        query += " " +
          "WHERE DATA_INIZIO_VALIDITA <= ? " +
          "AND NVL(DATA_FINE_VALIDITA,TO_DATE('31/12/9999','dd/mm/yyyy')) >= ? ";
      }
      else
      {
        query += " WHERE DATA_FINE_VALIDITA IS NULL ";
      }
      
      
      if (Validator.isNotEmpty(orderBy))
      {
        query += " ORDER BY " + orderBy;
      }
      else
      {
        query += " ORDER BY " + SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION;
      }

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing getListTipoTerrazzamento: " + query
          + "\n");
      
      int idx = 0;
      
      if (Validator.isNotEmpty(dataRiferimento))
      {
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataRiferimento));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataRiferimento));
      }
      
      

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        TipoTerrazzamentoVO tipoTerrazzamentoVO = new TipoTerrazzamentoVO();
        tipoTerrazzamentoVO.setIdTerrazzamento(new Long(rs
            .getLong("ID_TERRAZZAMENTO")));
        tipoTerrazzamentoVO.setCodice(rs.getString("CODICE"));
        tipoTerrazzamentoVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoTerrazzamentoVO.setDataInizioValidita(rs
            .getDate("DATA_INIZIO_VALIDITA"));
        tipoTerrazzamentoVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        elencoTipiTerrazzamento.add(tipoTerrazzamentoVO);
      }

      stmt.close();
      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "getListTipoTerrazzamento in CommonDAO - SQLException: "
              + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "getListTipoTerrazzamento in CommonDAO - Generic Exception: " + ex
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
                "getListTipoTerrazzamento in CommonDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getListTipoTerrazzamento in CommonDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this,
        "Invocated getListTipoTerrazzamento method in CommonDAO\n");
    if (elencoTipiTerrazzamento.size() == 0)
    {
      return (TipoTerrazzamentoVO[]) elencoTipiTerrazzamento
          .toArray(new TipoTerrazzamentoVO[0]);
    }
    else
    {
      return (TipoTerrazzamentoVO[]) elencoTipiTerrazzamento
          .toArray(new TipoTerrazzamentoVO[elencoTipiTerrazzamento.size()]);
    }
  }
  
  
  
  /**
   * Metodo che si occupa di reperire i dati dalla tabella DB_TIPO_ROTAZIONE_COLTURALE
   * 
   * @param orderBy
   * @param onlyActive
   * @return it.csi.solmr.dto.anag.terreni.TipoRotazioneColturaleVO[]
   * @throws DataAccessException
   */
  public TipoRotazioneColturaleVO[] getListTipoRotazioneColturale(String orderBy,
      Date dataRiferimento) throws DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating getListTipoRotazioneColturale method in CommonDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoRotazioneColturaleVO> elencoTipiRotazioneColturale = new Vector<TipoRotazioneColturaleVO>();

    try
    {
      SolmrLogger
          .debug(this,
              "Creating db-connection in getListTipoRotazioneColturale method in CommonDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in getListTipoRotazioneColturale method in CommonDAO and it values: "
                  + conn + "\n");

      String query = " " +
      		"SELECT ID_ROTAZIONE_COLTURALE, " +
      		"       CODICE, " +
      		"       DESCRIZIONE, " +
      		"       DATA_INIZIO_VALIDITA, " +
      		"       DATA_FINE_VALIDITA " +
      		"FROM   DB_TIPO_ROTAZIONE_COLTURALE ";

      if (Validator.isNotEmpty(dataRiferimento))
      {
        query += " " +
        	"WHERE DATA_INIZIO_VALIDITA <= ? " +
        	"AND NVL(DATA_FINE_VALIDITA,TO_DATE('31/12/9999','dd/mm/yyyy')) >= ? ";
      }
      else
      {
        query += " WHERE DATA_FINE_VALIDITA IS NULL ";
      }
      
      if (Validator.isNotEmpty(orderBy))
      {
        query += " ORDER BY " + orderBy;
      }
      else
      {
        query += " ORDER BY " + SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION;
      }

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing getListTipoRotazioneColturale: " + query
          + "\n");
      
      int idx = 0;
      
      if (Validator.isNotEmpty(dataRiferimento))
      {
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataRiferimento));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataRiferimento));
      }
      
      

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        TipoRotazioneColturaleVO tipoRotazioneColturaleVO = new TipoRotazioneColturaleVO();
        tipoRotazioneColturaleVO.setIdRotazioneColturale(new Long(rs
            .getLong("ID_ROTAZIONE_COLTURALE")));
        tipoRotazioneColturaleVO.setCodice(rs.getString("CODICE"));
        tipoRotazioneColturaleVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoRotazioneColturaleVO.setDataInizioValidita(rs
            .getDate("DATA_INIZIO_VALIDITA"));
        tipoRotazioneColturaleVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        elencoTipiRotazioneColturale.add(tipoRotazioneColturaleVO);
      }

      stmt.close();
      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "getListTipoRotazioneColturale in CommonDAO - SQLException: "
              + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "getListTipoRotazioneColturale in CommonDAO - Generic Exception: " + ex
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
                "getListTipoRotazioneColturale in CommonDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getListTipoRotazioneColturale in CommonDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this,
        "Invocated getListTipoRotazioneColturale method in CommonDAO\n");
    if (elencoTipiRotazioneColturale.size() == 0)
    {
      return (TipoRotazioneColturaleVO[]) elencoTipiRotazioneColturale
          .toArray(new TipoRotazioneColturaleVO[0]);
    }
    else
    {
      return (TipoRotazioneColturaleVO[]) elencoTipiRotazioneColturale
          .toArray(new TipoRotazioneColturaleVO[elencoTipiRotazioneColturale.size()]);
    }
  }
  
  
  /**
   * 
   * restituisce il valore del parametro di DB_ALTRI_DATI.
   * Se tipo = 'S' ritona una stringa
   * tipo = 'N' ritorna un BigDecimal
   * tipo = 'D' ritorna un java.util.Date
   * 
   * 
   * @param codiceParametro
   * @return
   * @throws DataAccessException
   */
  public Object getValoreParametroAltriDati(String codiceParametro) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Object  valore = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[CommonDAO::getValoreParametroAltriDati] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
         "SELECT   " + 
         "       AD.TIPO, " +
         "       AD.VALORE_STRINGA, " +
         "       AD.VALORE_DATA, " +
         "       AD.VALORE_NUMERICO " +
         "FROM   " +
         "       DB_ALTRI_DATI AD   " +
         "WHERE  CODICE = ? " +
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
            "[CommonDAO::getValoreParametroAltriDati] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setString(++indice, codiceParametro);
     
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {
        String tipo = rs.getString("TIPO");
        if("S".equalsIgnoreCase(tipo))
        {
          valore = rs.getString("VALORE_STRINGA");
        }
        
        if("N".equalsIgnoreCase(tipo))
        {
          valore = rs.getBigDecimal("VALORE_NUMERICO");
        }
        
        if("D".equalsIgnoreCase(tipo))
        {
          Date dataTmp = rs.getTimestamp("VALORE_DATA");
          valore = dataTmp;
        }
      }
      
      return valore;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("valore", valore) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("codiceParametro", codiceParametro) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[CommonDAO::getValoreParametroAltriDati] ",
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
              "[CommonDAO::getValoreParametroAltriDati] END.");
    }
  }
  
  
  
  
  
  
  
  
  

}
