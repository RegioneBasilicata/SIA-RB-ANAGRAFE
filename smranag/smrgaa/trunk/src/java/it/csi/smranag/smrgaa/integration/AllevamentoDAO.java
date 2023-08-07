package it.csi.smranag.smrgaa.integration;

import it.csi.smranag.smrgaa.dto.BaseCodeDescription;
import it.csi.smranag.smrgaa.dto.allevamenti.AllevamentoAcquaLavaggio;
import it.csi.smranag.smrgaa.dto.allevamenti.AllevamentoBioVO;
import it.csi.smranag.smrgaa.dto.allevamenti.AllevamentoVO;
import it.csi.smranag.smrgaa.dto.allevamenti.ControlloAllevamenti;
import it.csi.smranag.smrgaa.dto.allevamenti.EffluenteProdotto;
import it.csi.smranag.smrgaa.dto.allevamenti.EsitoControlloAllevamento;
import it.csi.smranag.smrgaa.dto.allevamenti.SottoCategoriaAllevamento;
import it.csi.smranag.smrgaa.dto.allevamenti.SottoCategoriaAnimStab;
import it.csi.smranag.smrgaa.dto.allevamenti.StabulazioneTrattamento;
import it.csi.smranag.smrgaa.dto.allevamenti.TipoCategoriaAnimale;
import it.csi.smranag.smrgaa.dto.allevamenti.TipoDestinoAcquaLavaggio;
import it.csi.smranag.smrgaa.dto.allevamenti.TipoMungitura;
import it.csi.smranag.smrgaa.dto.allevamenti.TipoSottoCategoriaAnimale;
import it.csi.smranag.smrgaa.dto.allevamenti.TipoSpecieAnimale;
import it.csi.smranag.smrgaa.dto.allevamenti.TipoTrattamento;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.anag.AllevamentoAnagVO;
import it.csi.solmr.dto.anag.TipoSpecieAnimaleAnagVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

/**
 * <p>
 * Title: S.O.L.M.R.
 * </p>
 * <p>
 * Description: Servizi On-Line per il Mondo Rurale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: TOBECONFIG
 * </p>
 * 
 * @author TOBECONFIG
 * @version 1.0
 */

public class AllevamentoDAO extends it.csi.solmr.integration.BaseDAO
{

  public AllevamentoDAO(String refName) throws ResourceAccessException
  {
    super(refName);
  }

  /**
   * Restituisce l'elenco dei tipi delle sotto categorie animali
   * 
   * @return
   * @throws DataAccessException
   */
  public TipoSottoCategoriaAnimale[] getTipiSottoCategoriaAnimale(long idCategoriaAnimale) throws DataAccessException
  {
    TipoSottoCategoriaAnimale tipoSottoCategoriaAnimale = null;
    Vector<TipoSottoCategoriaAnimale> results = new Vector<TipoSottoCategoriaAnimale>();

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      SolmrLogger.debug(this, "[AllevamentoDAO::getTipiSottoCategoriaAnimale] BEGIN.");

      conn = getDatasource().getConnection();

      String query = "SELECT D.ID_SOTTOCATEGORIA_ANIMALE, D.ID_CATEGORIA_ANIMALE, " + "D.DESCRIZIONE,D.PESO_VIVO_MEDIO, D.PESO_VIVO_MIN, "
          + "D.PESO_VIVO_MAX,D.PESO_VIVO_AZOTO, D.DATA_INIZIO_VALIDITA, " + "D.DATA_FINE_VALIDITA, D.FLAG_SOTTOCAT_FITTIZIA, " + "D.GIORNI_VUOTO_SANITARIO, D.GIORNI_DURATA_CICLO "
          + "FROM DB_TIPO_SOTTOCATEGORIA_ANIMALE D " + "WHERE D.DATA_FINE_VALIDITA IS NULL " + "AND D.ID_CATEGORIA_ANIMALE = ? " + "ORDER BY D.DESCRIZIONE";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "[AllevamentoDAO::getTipiSottoCategoriaAnimale] param idCategoriaAnimale: " + idCategoriaAnimale);

      stmt.setLong(1, idCategoriaAnimale);

      SolmrLogger.debug(this, "Executing query getTipiSottoCategoriaAnimale: " + query);

      ResultSet rs = stmt.executeQuery();

      SolmrLogger.debug(this, "Executed query getTipiSottoCategoriaAnimale");

      while (rs.next())
      {
        tipoSottoCategoriaAnimale = new TipoSottoCategoriaAnimale();
        tipoSottoCategoriaAnimale.setIdSottocategoriaAnimale(rs.getLong("ID_SOTTOCATEGORIA_ANIMALE"));
        tipoSottoCategoriaAnimale.setIdCategoriaAnimale(rs.getLong("ID_CATEGORIA_ANIMALE"));
        tipoSottoCategoriaAnimale.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoSottoCategoriaAnimale.setPesoVivoMedio(rs.getDouble("PESO_VIVO_MEDIO"));
        tipoSottoCategoriaAnimale.setPesoVivoMin(rs.getDouble("PESO_VIVO_MIN"));
        tipoSottoCategoriaAnimale.setPesoVivoMax(rs.getDouble("PESO_VIVO_MAX"));
        tipoSottoCategoriaAnimale.setPesoVivoAzoto(rs.getDouble("PESO_VIVO_AZOTO"));
        tipoSottoCategoriaAnimale.setGgVuotoSanitario(rs.getInt("GIORNI_VUOTO_SANITARIO"));
        tipoSottoCategoriaAnimale.setGgDurataCiclo(rs.getInt("GIORNI_DURATA_CICLO"));

        tipoSottoCategoriaAnimale.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        tipoSottoCategoriaAnimale.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));

        if (SolmrConstants.FLAG_S.equals(rs.getString("FLAG_SOTTOCAT_FITTIZIA")))
          tipoSottoCategoriaAnimale.setFlagSottocatFittizia(true);
        else
          tipoSottoCategoriaAnimale.setFlagSottocatFittizia(false);

        results.add(tipoSottoCategoriaAnimale);

      }

      SolmrLogger.debug(this, "Executed query - Found " + results.size() + " record");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "[AllevamentoDAO::getTipiSottoCategoriaAnimale] - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "[AllevamentoDAO::getTipiSottoCategoriaAnimale] - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "[AllevamentoDAO::getTipiSottoCategoriaAnimale] - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "[AllevamentoDAO::getTipiSottoCategoriaAnimale] - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return results.size() == 0 ? null : (TipoSottoCategoriaAnimale[]) results.toArray(new TipoSottoCategoriaAnimale[0]);
  }

  /**
   * Restituisce i dati relativi ad una sotto categorie animale
   * 
   * @return
   * @throws DataAccessException
   */
  public TipoSottoCategoriaAnimale getTipoSottoCategoriaAnimale(long idSottoCategoriaAnimale) throws DataAccessException
  {
    TipoSottoCategoriaAnimale tipoSottoCategoriaAnimale = null;

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      SolmrLogger.debug(this, "[AllevamentoDAO::getTipoSottoCategoriaAnimale] BEGIN.");

      conn = getDatasource().getConnection();

      String query = 
        "SELECT D.ID_SOTTOCATEGORIA_ANIMALE, " +
        "       D.ID_CATEGORIA_ANIMALE, " + 
        "       D.DESCRIZIONE, " +
        "       D.PESO_VIVO_MEDIO, " +
        "       D.PESO_VIVO_MIN, " +
        "       D.PESO_VIVO_MAX, " +
        "       D.PESO_VIVO_AZOTO, " +
        "       D.DATA_INIZIO_VALIDITA, " + 
        "       D.DATA_FINE_VALIDITA, " +
        "       D.FLAG_SOTTOCAT_FITTIZIA, " + 
        "       D.GIORNI_VUOTO_SANITARIO, " +
        "       D.GIORNI_DURATA_CICLO " +
        "FROM   DB_TIPO_SOTTOCATEGORIA_ANIMALE D " + 
        "WHERE  D.ID_SOTTOCATEGORIA_ANIMALE = ?";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "[AllevamentoDAO::getTipoSottoCategoriaAnimale] param idSottoCategoriaAnimale: " + idSottoCategoriaAnimale);

      stmt.setLong(1, idSottoCategoriaAnimale);

      SolmrLogger.debug(this, "Executing query getTipoSottoCategoriaAnimale: " + query);

      ResultSet rs = stmt.executeQuery();

      SolmrLogger.debug(this, "Executed query getTipoSottoCategoriaAnimale");

      if (rs.next())
      {
        tipoSottoCategoriaAnimale = new TipoSottoCategoriaAnimale();
        tipoSottoCategoriaAnimale.setIdSottocategoriaAnimale(rs.getLong("ID_SOTTOCATEGORIA_ANIMALE"));
        tipoSottoCategoriaAnimale.setIdCategoriaAnimale(rs.getLong("ID_CATEGORIA_ANIMALE"));
        tipoSottoCategoriaAnimale.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoSottoCategoriaAnimale.setPesoVivoMedio(rs.getDouble("PESO_VIVO_MEDIO"));
        tipoSottoCategoriaAnimale.setPesoVivoMin(rs.getDouble("PESO_VIVO_MIN"));
        tipoSottoCategoriaAnimale.setPesoVivoMax(rs.getDouble("PESO_VIVO_MAX"));
        tipoSottoCategoriaAnimale.setPesoVivoAzoto(rs.getDouble("PESO_VIVO_AZOTO"));
        tipoSottoCategoriaAnimale.setGgVuotoSanitario(rs.getInt("GIORNI_VUOTO_SANITARIO"));
        tipoSottoCategoriaAnimale.setGgDurataCiclo(rs.getInt("GIORNI_DURATA_CICLO"));

        tipoSottoCategoriaAnimale.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        tipoSottoCategoriaAnimale.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));

        if (SolmrConstants.FLAG_S.equals(rs.getString("FLAG_SOTTOCAT_FITTIZIA")))
          tipoSottoCategoriaAnimale.setFlagSottocatFittizia(true);
        else
          tipoSottoCategoriaAnimale.setFlagSottocatFittizia(false);

      }

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "[AllevamentoDAO::getTipoSottoCategoriaAnimale] - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "[AllevamentoDAO::getTipoSottoCategoriaAnimale] - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "[AllevamentoDAO::getTipoSottoCategoriaAnimale] - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "[AllevamentoDAO::getTipoSottoCategoriaAnimale] - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return tipoSottoCategoriaAnimale;
  }

  /**
   * Restituisce l'elenco dei tipi di mungitura
   * 
   * @return
   * @throws DataAccessException
   */
  public TipoMungitura[] getTipiMungitura() throws DataAccessException
  {
    TipoMungitura tipoMungitura = null;
    Vector<TipoMungitura> results = new Vector<TipoMungitura>();

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      SolmrLogger.debug(this, "[AllevamentoDAO::getTipiMungitura] BEGIN.");

      conn = getDatasource().getConnection();

      String query = "SELECT D.ID_MUNGITURA, D.DESCRIZIONE, D.COEFF_VOLUME_LAVAGGIO, " + "D.DATA_INIZIO_VALIDITA, D.DATA_FINE_VALIDITA " + "FROM DB_TIPO_MUNGITURA D "
          + "WHERE D.DATA_FINE_VALIDITA IS NULL " + "ORDER BY D.DESCRIZIONE";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query getTipiMungitura: " + query);

      ResultSet rs = stmt.executeQuery();

      SolmrLogger.debug(this, "Executed query getTipiMungitura");

      while (rs.next())
      {
        tipoMungitura = new TipoMungitura();
        tipoMungitura.setIdMungitura(rs.getLong("ID_MUNGITURA"));
        tipoMungitura.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoMungitura.setCoeffVolumeLavaggio(rs.getDouble("COEFF_VOLUME_LAVAGGIO"));
        tipoMungitura.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        tipoMungitura.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));

        results.add(tipoMungitura);

      }

      SolmrLogger.debug(this, "Executed query - Found " + results.size() + " record");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "[AllevamentoDAO::getTipiMungitura] - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "[AllevamentoDAO::getTipiMungitura] - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "[AllevamentoDAO::getTipiMungitura] - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "[AllevamentoDAO::getTipiMungitura] - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return results.size() == 0 ? null : (TipoMungitura[]) results.toArray(new TipoMungitura[0]);
  }

  /**
   * Restituisce l'elenco dei tipi delle sotto categorie degli allevamenti
   * 
   * @return
   * @throws DataAccessException
   */
  public Vector<SottoCategoriaAllevamento> getTipiSottoCategoriaAllevamento(long idAllevamento) throws DataAccessException
  {
    SottoCategoriaAllevamento sottoCategoriaAllevamento = null;
    Vector<SottoCategoriaAllevamento> results = new Vector<SottoCategoriaAllevamento>();

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      SolmrLogger.debug(this, "[AllevamentoDAO::getTipiSottoCategoriaAllevamento] BEGIN.");

      conn = getDatasource().getConnection();

      String query = 
        "SELECT SA.ID_SOTTOCATEGORIA_ALLEVAMENTO, " +
        "       NVL(SA.ORE_PASCOLO_INVERNO, 0) AS ORE_PASCOLO_INVERNO, " +
        "       SA.ID_SOTTOCATEGORIA_ANIMALE, " +
        "       SA.ID_CATEGORIE_ALLEVAMENTO, " +
        "       SA.QUANTITA, " +
        "       SA.QUANTITA_PROPRIETA, " +
        "       SA.PESO_VIVO, " +
        "       NVL(SA.GIORNI_VUOTO_SANITARIO, 0) AS GIORNI_VUOTO_SANITARIO, " +
        "       NVL(SA.GIORNI_PASCOLO_ESTATE, 0) AS GIORNI_PASCOLO_ESTATE , " +
        "       NVL(SA.ORE_PASCOLO_ESTATE, 0) AS ORE_PASCOLO_ESTATE, " +
        "       NVL(SA.GIORNI_PASCOLO_INVERNO, 0) AS GIORNI_PASCOLO_INVERNO, " +
        "       NVL(SA.CICLI, 0) AS CICLI, " +
        "       CA.ID_CATEGORIA_ANIMALE, " +
        "       TSA.DESCRIZIONE AS DESC_SOTTO_CATEGORIA, " +
        "       TCA.DESCRIZIONE AS DESC_CATEGORIA, " +
        "       TP.FLAG_STALLA_PASCOLO, " +
        "       TSA.PESO_VIVO_MIN, " +
        "       TSA.PESO_VIVO_MAX, " +
        "       TSA.GIORNI_DURATA_CICLO, " +
        "       TSA.GIORNI_VUOTO_SANITARIO AS GG_VUOTO_SANITARIO, " +
        "       TCA.COEFFICIENTE_UBA, " +
        "       SA.NUMERO_CICLI_ANNUALI, " +
        "       TSA.PESO_VIVO_AZOTO, " +
        "       TCA.LATTAZIONE " +
        "FROM   DB_SOTTOCATEGORIA_ALLEVAMENTO SA, " +
        "       DB_CATEGORIE_ALLEVAMENTO CA, " +
        "       DB_TIPO_SOTTOCATEGORIA_ANIMALE TSA, " + 
        "       DB_TIPO_CATEGORIA_ANIMALE TCA, " +
        "       DB_TIPO_SPECIE_ANIMALE TP " +
        "WHERE  SA.ID_CATEGORIE_ALLEVAMENTO = CA.ID_CATEGORIE_ALLEVAMENTO " +
        "AND    SA.ID_SOTTOCATEGORIA_ANIMALE = TSA.ID_SOTTOCATEGORIA_ANIMALE " +
        "AND    TCA.ID_CATEGORIA_ANIMALE = CA.ID_CATEGORIA_ANIMALE " +
        "AND    TCA.ID_SPECIE_ANIMALE = TP.ID_SPECIE_ANIMALE " +
        "AND    CA.ID_ALLEVAMENTO = ? " +
        "ORDER BY DESC_CATEGORIA, " +
        "      DESC_SOTTO_CATEGORIA";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "[AllevamentoDAO::getTipoSottoCategoriaAnimale] param idAllevamento: " + idAllevamento);

      stmt.setLong(1, idAllevamento);

      SolmrLogger.debug(this, "Executing query getTipiSottoCategoriaAllevamento: " + query);

      ResultSet rs = stmt.executeQuery();

      SolmrLogger.debug(this, "Executed query getTipiSottoCategoriaAllevamento");

      while (rs.next())
      {
        sottoCategoriaAllevamento = new SottoCategoriaAllevamento();
        sottoCategoriaAllevamento.setIdSottoCategoriaAllevamento(rs.getLong("ID_SOTTOCATEGORIA_ALLEVAMENTO"));
        sottoCategoriaAllevamento.setIdSottoCategoriaAnimale(rs.getLong("ID_SOTTOCATEGORIA_ANIMALE"));
        sottoCategoriaAllevamento.setIdCategoriaAnimale(rs.getLong("ID_CATEGORIA_ANIMALE"));
        sottoCategoriaAllevamento.setDescCategoriaAnimale(rs.getString("DESC_CATEGORIA"));
        sottoCategoriaAllevamento.setDescSottoCategoriaAnimale(rs.getString("DESC_SOTTO_CATEGORIA"));
        sottoCategoriaAllevamento.setPesoVivo(StringUtils.parsePesoCapiMod(rs.getString("PESO_VIVO")));
        sottoCategoriaAllevamento.setPesoVivoAzoto(rs.getDouble("PESO_VIVO_AZOTO"));
        sottoCategoriaAllevamento.setQuantita(rs.getString("QUANTITA"));
        sottoCategoriaAllevamento.setQuantitaProprieta(rs.getString("QUANTITA_PROPRIETA"));
        sottoCategoriaAllevamento.setGiorniVuotoSanitario(rs.getString("GIORNI_VUOTO_SANITARIO"));
        sottoCategoriaAllevamento.setCicli(rs.getString("CICLI"));
        sottoCategoriaAllevamento.setGiorniPascoloEstate(rs.getString("GIORNI_PASCOLO_ESTATE"));
        sottoCategoriaAllevamento.setOrePascoloEstate(rs.getString("ORE_PASCOLO_ESTATE"));
        sottoCategoriaAllevamento.setGiorniPascoloInverno(rs.getString("GIORNI_PASCOLO_INVERNO"));
        sottoCategoriaAllevamento.setOrePascoloInverno(rs.getString("ORE_PASCOLO_INVERNO"));

        if ("S".equals(rs.getString("FLAG_STALLA_PASCOLO")))
          sottoCategoriaAllevamento.setFlagStallaPascolo(true);
        else
          sottoCategoriaAllevamento.setFlagStallaPascolo(false);

        sottoCategoriaAllevamento.setPesoVivoMin(rs.getDouble("PESO_VIVO_MIN"));
        sottoCategoriaAllevamento.setPesoVivoMax(rs.getDouble("PESO_VIVO_MAX"));
        //sottoCategoriaAllevamento.setGgVuotoSanitario(rs.getInt("GG_VUOTO_SANITARIO"));
        sottoCategoriaAllevamento.setGgDurataCiclo(rs.getInt("GIORNI_DURATA_CICLO"));
        sottoCategoriaAllevamento.setCoefficienteUba(rs.getDouble("COEFFICIENTE_UBA"));
        sottoCategoriaAllevamento.setNumeroCicliAnnuali(rs.getString("NUMERO_CICLI_ANNUALI"));
        sottoCategoriaAllevamento.setLattazione(rs.getString("LATTAZIONE"));
        
        results.add(sottoCategoriaAllevamento);

      }

      SolmrLogger.debug(this, "Executed query - Found " + results.size() + " record");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "[AllevamentoDAO::getTipiSottoCategoriaAllevamento] - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "[AllevamentoDAO::getTipiSottoCategoriaAllevamento] - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "[AllevamentoDAO::getTipiSottoCategoriaAllevamento] - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "[AllevamentoDAO::getTipiSottoCategoriaAllevamento] - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return results;
  }

  /**
   * Restituisce l'elenco dei tipi delle stabulazioni
   * 
   * @return
   * @throws DataAccessException
   */
  public BaseCodeDescription[] getTipiStabulazione(long idSottoCategoriaAnimale) throws DataAccessException
  {
    BaseCodeDescription tipoStabulazione = null;
    Vector<BaseCodeDescription> results = new Vector<BaseCodeDescription>();

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      SolmrLogger.debug(this, "[AllevamentoDAO::getTipiStabulazione] BEGIN.");

      conn = getDatasource().getConnection();

      String query = 
        "SELECT DISTINCT " +
        "       TS.ID_STABULAZIONE, " +
        "       TS.DESCRIZIONE," +
        "       TS.LETTIERA_PERMANENTE " +
        "FROM   DB_TIPO_STABULAZIONE TS, " +
        "       DB_SOTTOCATEGORIA_ANIM_STAB SAS " + 
        "WHERE  TS.DATA_FINE_VALIDITA IS NULL " +
        "AND    TS.ID_STABULAZIONE=SAS.ID_STABULAZIONE " + 
        "AND    SAS.DATA_FINE_VALIDITA IS NULL " +
        "AND    SAS.ID_SOTTOCATEGORIA_ANIMALE = ? " +
        "ORDER BY TS.DESCRIZIONE";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "[AllevamentoDAO::getTipiStabulazione] param idSottoCategoriaAnimale: " + idSottoCategoriaAnimale);

      stmt.setLong(1, idSottoCategoriaAnimale);

      SolmrLogger.debug(this, "Executing query getTipiStabulazione: " + query);

      ResultSet rs = stmt.executeQuery();

      SolmrLogger.debug(this, "Executed query getTipiStabulazione");

      while (rs.next())
      {
        tipoStabulazione = new BaseCodeDescription();
        tipoStabulazione.setCode(rs.getLong("ID_STABULAZIONE"));
        tipoStabulazione.setDescription(rs.getString("DESCRIZIONE"));
        tipoStabulazione.setItem(rs.getString("LETTIERA_PERMANENTE"));
        
        results.add(tipoStabulazione);

      }

      SolmrLogger.debug(this, "Executed query - Found " + results.size() + " record");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "[AllevamentoDAO::getTipiStabulazione] - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "[AllevamentoDAO::getTipiStabulazione] - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "[AllevamentoDAO::getTipiStabulazione] - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "[AllevamentoDAO::getTipiStabulazione] - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return results.size() == 0 ? null : (BaseCodeDescription[]) results.toArray(new BaseCodeDescription[0]);
  }

  /**
   * Restituisce l'elenco dei trattamenti
   * 
   * @return
   * @throws DataAccessException
   */
  public Vector<TipoTrattamento> getTipiTrattamento() throws DataAccessException
  {
    TipoTrattamento tipoTrattamento = null;
    Vector<TipoTrattamento> results = new Vector<TipoTrattamento>();

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      SolmrLogger.debug(this, "[AllevamentoDAO::getTipiTrattamento] BEGIN.");

      conn = getDatasource().getConnection();

      String query = 
        "SELECT D.ID_TRATTAMENTO, " +
        "       D.DESCRIZIONE, " +
        "       D.PERC_VOLUME_SOLIDO, " +
        "       D.PERC_AZOTO_SOLIDO," + 
        "       D.NOTE, " +
        "       D.CODICE_SIAP, " +
        "       D.PERC_AZOTO_VOLATILE, " +
        "       D.FLAG_CALCOLO " + 
        "FROM   DB_TIPO_TRATTAMENTO D " +
        "WHERE  D.DATA_FINE_VALIDITA IS NULL " +
        "ORDER BY D.CODICE_SIAP";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query getTipiTrattamento: " + query);

      ResultSet rs = stmt.executeQuery();

      SolmrLogger.debug(this, "Executed query getTipiTrattamento");

      while (rs.next())
      {
        tipoTrattamento = new TipoTrattamento();
        tipoTrattamento.setIdTrattamento(rs.getLong("ID_TRATTAMENTO"));
        tipoTrattamento.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoTrattamento.setNote(rs.getString("NOTE"));
        tipoTrattamento.setCodiceSiap(rs.getString("CODICE_SIAP"));
        
        if (SolmrConstants.FLAG_S.equals(rs.getString("FLAG_CALCOLO")))
          tipoTrattamento.setFlagCalcolo(true);
        else
          tipoTrattamento.setFlagCalcolo(false);
        
        tipoTrattamento.setPercVolumeSolido(rs.getDouble("PERC_VOLUME_SOLIDO"));
        tipoTrattamento.setPercAzotoSolido(rs.getDouble("PERC_AZOTO_SOLIDO"));
        tipoTrattamento.setPercAzotoVolatile(rs.getDouble("PERC_AZOTO_VOLATILE"));

        results.add(tipoTrattamento);
      }

      SolmrLogger.debug(this, "Executed query - Found " + results.size() + " record");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "[AllevamentoDAO::getTipiTrattamento] - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "[AllevamentoDAO::getTipiTrattamento] - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "[AllevamentoDAO::getTipiTrattamento] - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "[AllevamentoDAO::getTipiTrattamento] - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return results;
  }
  
  
  /**
   * Restituisce il trattamento richeisto
   * 
   * @return
   * @throws DataAccessException
   */
  public TipoTrattamento getTipoTrattamento(long idTrattamento) throws DataAccessException
  {
    TipoTrattamento tipoTrattamento = null;

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      SolmrLogger.debug(this, "[AllevamentoDAO::getTipoTrattamento] BEGIN.");

      conn = getDatasource().getConnection();

      String query = "SELECT D.ID_TRATTAMENTO, D.DESCRIZIONE, D.PERC_VOLUME_SOLIDO, D.PERC_AZOTO_SOLIDO," + "D.NOTE, D.CODICE_SIAP, D.PERC_AZOTO_VOLATILE, D.FLAG_CALCOLO "
          + "FROM DB_TIPO_TRATTAMENTO D WHERE D.ID_TRATTAMENTO = ? ";

      stmt = conn.prepareStatement(query);
      
      SolmrLogger.debug(this, "[AllevamentoDAO::getTipoTrattamento] param idTrattamento: " + idTrattamento);

      stmt.setLong(1, idTrattamento);


      SolmrLogger.debug(this, "Executing query getTipoTrattamento: " + query);

      ResultSet rs = stmt.executeQuery();

      SolmrLogger.debug(this, "Executed query getTipoTrattamento");

      if (rs.next())
      {
        tipoTrattamento = new TipoTrattamento();
        tipoTrattamento.setIdTrattamento(rs.getLong("ID_TRATTAMENTO"));
        tipoTrattamento.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoTrattamento.setNote(rs.getString("NOTE"));
        tipoTrattamento.setCodiceSiap(rs.getString("CODICE_SIAP"));
        
        if (SolmrConstants.FLAG_S.equals(rs.getString("FLAG_CALCOLO")))
          tipoTrattamento.setFlagCalcolo(true);
        else
          tipoTrattamento.setFlagCalcolo(false);
        
        tipoTrattamento.setPercVolumeSolido(rs.getDouble("PERC_VOLUME_SOLIDO"));
        tipoTrattamento.setPercAzotoSolido(rs.getDouble("PERC_AZOTO_SOLIDO"));
        tipoTrattamento.setPercAzotoVolatile(rs.getDouble("PERC_AZOTO_VOLATILE"));

      }

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "[AllevamentoDAO::getTipoTrattamento] - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "[AllevamentoDAO::getTipoTrattamento] - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "[AllevamentoDAO::getTipoTrattamento] - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "[AllevamentoDAO::getTipoTrattamento] - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return tipoTrattamento;
  }

  
  /**
   * Restituisce i dati relativi all'effluente
   * 
   * @return
   * @throws DataAccessException
   */
  public SottoCategoriaAnimStab getSottoCategoriaAnimStab(long idSottoCategoriaAnimale,boolean palabile, long idStabulazione) throws DataAccessException
  {
    SottoCategoriaAnimStab animStab = null;

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      SolmrLogger.debug(this, "[AllevamentoDAO::getSottoCategoriaAnimStab] BEGIN.");

      conn = getDatasource().getConnection();

      String query =  "SELECT SAS.COEFF_VOLUME_MAX, SAS.COEFF_PESO_MAX, SAS.COEFF_AZOTO_MAX, SAS.COEFF_VOLUME_MIN, SAS.COEFF_PESO_MIN, "+
                      "  SAS.COEFF_AZOTO_MIN, TE.ID_EFFLUENTE, TE.COEFF_M3_T_PUA "+
                      "FROM DB_SOTTOCATEGORIA_ANIM_STAB SAS, DB_TIPO_EFFLUENTE TE "+
                      "WHERE SAS.ID_EFFLUENTE=TE.ID_EFFLUENTE "+
                      "  AND SAS.DATA_FINE_VALIDITA IS NULL "+
                      "  AND TE.DATA_FINE_VALIDITA IS NULL "+
                      "  AND SAS.ID_SOTTOCATEGORIA_ANIMALE = ? "+
                      "  AND TE.FLAG_PALABILE = ? "+
                      "  AND SAS.ID_STABULAZIONE = ? ";

      stmt = conn.prepareStatement(query);
      
      SolmrLogger.debug(this, "[AllevamentoDAO::getSottoCategoriaAnimStab] param idSottoCategoriaAnimale: " + idSottoCategoriaAnimale);
      SolmrLogger.debug(this, "[AllevamentoDAO::getSottoCategoriaAnimStab] param palabile: " + palabile);
      SolmrLogger.debug(this, "[AllevamentoDAO::getSottoCategoriaAnimStab] param idStabulazione: " + idStabulazione);

      stmt.setLong(1, idSottoCategoriaAnimale);
      if (palabile)
        stmt.setString(2, SolmrConstants.FLAG_S);
      else
        stmt.setString(2, SolmrConstants.FLAG_N);
      stmt.setLong(3, idStabulazione);


      SolmrLogger.debug(this, "Executing query getSottoCategoriaAnimStab: " + query);

      ResultSet rs = stmt.executeQuery();

      SolmrLogger.debug(this, "Executed query getSottoCategoriaAnimStab");
      
      int count=0;

      while (rs.next())
      {
        count++;
        animStab = new SottoCategoriaAnimStab();
        animStab.setCoeffVolumeMax(rs.getDouble("COEFF_VOLUME_MAX"));
        animStab.setCoeffVolumeMin(rs.getDouble("COEFF_VOLUME_MIN"));
        animStab.setCoeffPesoMax(rs.getDouble("COEFF_PESO_MAX"));
        animStab.setCoeffPesoMin(rs.getDouble("COEFF_PESO_MIN"));
        animStab.setCoeffAzotoMax(rs.getDouble("COEFF_AZOTO_MAX"));
        animStab.setCoeffAzotoMin(rs.getDouble("COEFF_AZOTO_MIN"));
        animStab.setIdEffluente(rs.getLong("ID_EFFLUENTE"));
        animStab.setCoeffM3TPua(rs.getDouble("COEFF_M3_T_PUA"));
      }
      //Se ho trovato più record devo gestirlo come se non avessi trovato record
      if (count>1) animStab=null;
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "[AllevamentoDAO::getSottoCategoriaAnimStab] - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "[AllevamentoDAO::getSottoCategoriaAnimStab] - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "[AllevamentoDAO::getSottoCategoriaAnimStab] - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "[AllevamentoDAO::getSottoCategoriaAnimStab] - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return animStab;
  }

  
  
  
  
  /**
   * Restituisce l'elenco delle stabulazioni inserite dall'utente
   * 
   * @return
   * @throws DataAccessException
   */
  public Vector<StabulazioneTrattamento> getStabulazioni(long idAllevamento,boolean modifica) throws DataAccessException
  {
    StabulazioneTrattamento stabulazione = null;
    Vector<StabulazioneTrattamento> results = new Vector<StabulazioneTrattamento>();

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      SolmrLogger.debug(this, "[AllevamentoDAO::getStabulazioni] BEGIN.");

      conn = getDatasource().getConnection();

      String query = "SELECT ST.ID_STABULAZIONE_TRATTAMENTO,ST.ID_SOTTOCATEGORIA_ALLEVAMENTO,ST.ID_STABULAZIONE,ST.QUANTITA_STABULATI,"+
      "ST.ID_TRATTAMENTO,TS.DESCRIZIONE AS DESC_STABULAZIONE,SA.ID_SOTTOCATEGORIA_ANIMALE,TR.FLAG_CALCOLO,"+
      "TR.DESCRIZIONE AS DESC_TRATTAMENTO,TCA.DESCRIZIONE AS DESC_CATEGORIA,TSA.DESCRIZIONE AS  DESC_SOTTO_CATEGORIA "+
      "FROM DB_STABULAZIONE_TRATTAMENTO ST,DB_SOTTOCATEGORIA_ALLEVAMENTO SA,DB_CATEGORIE_ALLEVAMENTO CA, "+
      "DB_TIPO_STABULAZIONE TS,DB_TIPO_SOTTOCATEGORIA_ANIMALE TSA, DB_TIPO_CATEGORIA_ANIMALE TCA,DB_TIPO_TRATTAMENTO TR "+
      "WHERE CA.ID_ALLEVAMENTO=? "+
      "AND CA.ID_CATEGORIE_ALLEVAMENTO=SA.ID_CATEGORIE_ALLEVAMENTO "+
      "AND SA.ID_SOTTOCATEGORIA_ALLEVAMENTO=ST.ID_SOTTOCATEGORIA_ALLEVAMENTO "+
      "AND TS.ID_STABULAZIONE=ST.ID_STABULAZIONE "+
      "AND TSA.ID_SOTTOCATEGORIA_ANIMALE=SA.ID_SOTTOCATEGORIA_ANIMALE "+
      "AND TCA.ID_CATEGORIA_ANIMALE=CA.ID_CATEGORIA_ANIMALE "+
      "AND ST.ID_TRATTAMENTO = TR.ID_TRATTAMENTO(+) "+
      "ORDER BY TCA.DESCRIZIONE,TSA.DESCRIZIONE,TS.DESCRIZIONE";

      stmt = conn.prepareStatement(query);
      
      SolmrLogger.debug(this, "[AllevamentoDAO::getTipoSottoCategoriaAnimale] param idAllevamento: " + idAllevamento);

      stmt.setLong(1, idAllevamento);

      SolmrLogger.debug(this, "Executing query getStabulazioni: " + query);

      ResultSet rs = stmt.executeQuery();

      SolmrLogger.debug(this, "Executed query getStabulazioni");

      while (rs.next())
      {
        stabulazione = new StabulazioneTrattamento();
        stabulazione.setIdStabulazioneTrattamento(rs.getLong("ID_STABULAZIONE_TRATTAMENTO"));
        stabulazione.setIdSottoCategoriaAllevamento(rs.getString("ID_SOTTOCATEGORIA_ALLEVAMENTO"));
        stabulazione.setIdStabulazione(rs.getString("ID_STABULAZIONE"));
        stabulazione.setDescStabulazione(rs.getString("DESC_STABULAZIONE"));
        stabulazione.setIdTrattamento(rs.getString("ID_TRATTAMENTO"));
        stabulazione.setDescTrattamento(rs.getString("DESC_TRATTAMENTO"));
        stabulazione.setDescCategoria(rs.getString("DESC_CATEGORIA"));
        stabulazione.setDescSottoCategoria(rs.getString("DESC_SOTTO_CATEGORIA"));
        stabulazione.setIdSottoCategoriaAnimale(rs.getString("ID_SOTTOCATEGORIA_ANIMALE"));
        stabulazione.setQuantita(rs.getString("QUANTITA_STABULATI"));       
        if (SolmrConstants.FLAG_S.equals(rs.getString("FLAG_CALCOLO")))
          stabulazione.setFlagCalcolo(true);
        else
          stabulazione.setFlagCalcolo(false);
        
        //Se sono in lettura devo andare a memorizzare l'azoto prodotto
        //Se sono in modifica devo memorizzare l'azoto prodotto solo nel caso di altri trattamenti      
        stabulazione.setTotaleAzoto("0");
        if (!modifica || (modifica && !stabulazione.isFlagCalcolo()))
          stabulazione.setTotaleAzoto(getAzotoAziendaleProdotto(stabulazione.getIdStabulazioneTrattamento())); 
        
        String[] volumePalabile = getDbEffluenteProdottoNew(stabulazione.getIdStabulazioneTrattamento(),true,false);
        stabulazione.setPalabile(volumePalabile[0]); //palabile, preTrattamento
        stabulazione.setAzotoPalabile(volumePalabile[1]); //palabile, preTrattamento
        stabulazione.setPalabileTAnno(volumePalabile[2]); //palabile, preTrattamento
        stabulazione.setPalabileTrat(getDbEffluenteProdotto(stabulazione.getIdStabulazioneTrattamento(),true,true)); //palabile, postTrattamento
        String[] volumeNonPalabile = getDbEffluenteProdottoNew(stabulazione.getIdStabulazioneTrattamento(),false,false);
        stabulazione.setNonPalabile(volumeNonPalabile[0]); //non palabile, preTrattamento
        stabulazione.setAzotoNonPalabile(volumeNonPalabile[1]); //non palabile, preTrattamento
        stabulazione.setNonPalabileTrat(getDbEffluenteProdotto(stabulazione.getIdStabulazioneTrattamento(),false,true)); //non palabile, postTrattamento
        
        results.add(stabulazione);
      }

      SolmrLogger.debug(this, "Executed query - Found " + results.size() + " record");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "[AllevamentoDAO::getStabulazioni] - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "[AllevamentoDAO::getStabulazioni] - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "[AllevamentoDAO::getStabulazioni] - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "[AllevamentoDAO::getStabulazioni] - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return results;
  }
  
  
  public String getDbEffluenteProdotto(long idStabulazioneTrattamento,boolean palabile, boolean trattamento) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    String volume = null;
    try
    {
      SolmrLogger.debug(this, "[AllevamentoDAO::getDbEffluenteProdotto] BEGIN.");

      conn = getDatasource().getConnection();

      String query =  "SELECT EP.VOLUME_PRODOTTO FROM DB_EFFLUENTE_PRODOTTO EP,DB_TIPO_EFFLUENTE TE "+
      "WHERE EP.ID_STABULAZIONE_TRATTAMENTO = ? "+
      "AND EP.ID_EFFLUENTE= TE.ID_EFFLUENTE "+
      "AND EP.FLAG_TRATTAMENTO = ? "+
      "AND TE.FLAG_PALABILE = ? ";

      stmt = conn.prepareStatement(query);
      
      SolmrLogger.debug(this, "[AllevamentoDAO::getDbEffluenteProdotto] param idStabulazioneTrattamento: " + idStabulazioneTrattamento);
      SolmrLogger.debug(this, "[AllevamentoDAO::getDbEffluenteProdotto] param palabile: " + palabile);
      SolmrLogger.debug(this, "[AllevamentoDAO::getDbEffluenteProdotto] param trattamento: " + trattamento);

      stmt.setLong(1, idStabulazioneTrattamento);
      if (trattamento)
        stmt.setString(2, SolmrConstants.FLAG_S);
      else
        stmt.setString(2, SolmrConstants.FLAG_N);
      if (palabile)
        stmt.setString(3, SolmrConstants.FLAG_S);
      else
        stmt.setString(3, SolmrConstants.FLAG_N);



      SolmrLogger.debug(this, "Executing query getDbEffluenteProdotto: " + query);

      ResultSet rs = stmt.executeQuery();

      SolmrLogger.debug(this, "Executed query getDbEffluenteProdotto");
      
      volume="0";
      
      double volumeD=0;

      while (rs.next())
        volumeD+=rs.getDouble("VOLUME_PRODOTTO");
      
      volume=StringUtils.parsePesoCapiMod(""+volumeD);
      
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "[AllevamentoDAO::getDbEffluenteProdotto] - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "[AllevamentoDAO::getDbEffluenteProdotto] - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "[AllevamentoDAO::getDbEffluenteProdotto] - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "[AllevamentoDAO::getDbEffluenteProdotto] - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return volume;
  }
  
  
  
  /**
   * Restituisce i dati relativi all'effluente
   * 
   * @return
   * @throws DataAccessException
   */
  public String[] getDbEffluenteProdottoNew(long idStabulazioneTrattamento,boolean palabile, boolean trattamento) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    String[] volume = new String[3];
    try
    {
      SolmrLogger.debug(this, "[AllevamentoDAO::getDbEffluenteProdottoNew] BEGIN.");

      conn = getDatasource().getConnection();

      String query =  
        "SELECT EP.VOLUME_PRODOTTO, " +
        "       EP.AZOTO_PRODOTTO, " +
        "       TE.COEFF_M3_T_PUA " +
        "FROM   DB_EFFLUENTE_PRODOTTO EP, " +
        "       DB_TIPO_EFFLUENTE TE "+
        "WHERE  EP.ID_STABULAZIONE_TRATTAMENTO = ? "+
        "AND    EP.ID_EFFLUENTE = TE.ID_EFFLUENTE "+
        "AND    EP.FLAG_TRATTAMENTO = ? "+
        "AND    TE.FLAG_PALABILE = ? ";

      stmt = conn.prepareStatement(query);
      
      SolmrLogger.debug(this, "[AllevamentoDAO::getDbEffluenteProdottoNew] param idStabulazioneTrattamento: " + idStabulazioneTrattamento);
      SolmrLogger.debug(this, "[AllevamentoDAO::getDbEffluenteProdottoNew] param palabile: " + palabile);
      SolmrLogger.debug(this, "[AllevamentoDAO::getDbEffluenteProdottoNew] param trattamento: " + trattamento);

      stmt.setLong(1, idStabulazioneTrattamento);
      if (trattamento)
        stmt.setString(2, SolmrConstants.FLAG_S);
      else
        stmt.setString(2, SolmrConstants.FLAG_N);
      if (palabile)
        stmt.setString(3, SolmrConstants.FLAG_S);
      else
        stmt.setString(3, SolmrConstants.FLAG_N);



      SolmrLogger.debug(this, "Executing query getDbEffluenteProdottoNew: " + query);

      ResultSet rs = stmt.executeQuery();

      SolmrLogger.debug(this, "Executed query getDbEffluenteProdottoNew");
      
      volume[0] = "0";
      volume[1] = "0";
      volume[2] = "0";
      
      double volumeD=0;
      double azotoD=0;
      double coeffM3=0;

      if (rs.next())
      {
        volumeD = rs.getDouble("VOLUME_PRODOTTO");
        azotoD = rs.getDouble("AZOTO_PRODOTTO");
        coeffM3 = rs.getDouble("COEFF_M3_T_PUA");
      }
      
      coeffM3 = coeffM3 * volumeD / 10;
      
      volume[0]=StringUtils.parsePesoCapiMod(""+volumeD);
      volume[1]=StringUtils.parsePesoCapiMod(""+azotoD);
      volume[2]=StringUtils.parsePesoCapiMod(""+coeffM3);
      
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "[AllevamentoDAO::getDbEffluenteProdottoNew] - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "[AllevamentoDAO::getDbEffluenteProdottoNew] - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "[AllevamentoDAO::getDbEffluenteProdottoNew] - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "[AllevamentoDAO::getDbEffluenteProdottoNew] - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return volume;
  }
  
  
  /**
   * Restituisce i dati relativi all'azoto aziendale prodotto post trattamento:+
   * è dato dalla somam di quello palabile con quello non palabile
   * 
   * @return
   * @throws DataAccessException
   */
  public String getAzotoAziendaleProdotto(long idStabulazioneTrattamento) 
    throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    double azoto = 0;
    String result="";
    try
    {
      SolmrLogger.debug(this, "[AllevamentoDAO::getAzotoAziendaleProdotto] BEGIN.");

      conn = getDatasource().getConnection();

      String query =  "SELECT EP.AZOTO_PRODOTTO_AZIENDALE FROM DB_EFFLUENTE_PRODOTTO EP,DB_TIPO_EFFLUENTE TE "+
      "WHERE EP.ID_STABULAZIONE_TRATTAMENTO = ? "+
      "AND EP.ID_EFFLUENTE= TE.ID_EFFLUENTE "+
      "AND EP.FLAG_TRATTAMENTO = 'S' ";

      stmt = conn.prepareStatement(query);
      
      SolmrLogger.debug(this, "[AllevamentoDAO::getAzotoAziendaleProdotto] param idStabulazioneTrattamento: " + idStabulazioneTrattamento);

      stmt.setLong(1, idStabulazioneTrattamento);
      

      SolmrLogger.debug(this, "Executing query getAzotoAziendaleProdotto: " + query);

      ResultSet rs = stmt.executeQuery();
      

      SolmrLogger.debug(this, "Executed query getAzotoAziendaleProdotto");
      

      while (rs.next())
        azoto+=rs.getDouble("AZOTO_PRODOTTO_AZIENDALE");
      
      result=StringUtils.parsePesoCapiMod(""+azoto);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "[AllevamentoDAO::getAzotoAziendaleProdotto] - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "[AllevamentoDAO::getAzotoAziendaleProdotto] - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "[AllevamentoDAO::getAzotoAziendaleProdotto] - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "[AllevamentoDAO::getAzotoAziendaleProdotto] - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  /**
   * Restituisce un Vector di AllevamentoVO
   * 
   * 
   * @param idAzienda
   * @param dataInserimentiDich
   * @return
   * @throws DataAccessException
   */
  public Vector<AllevamentoVO> getElencoAllevamenti(long idAzienda, Date dataInserimentiDich, Long idUte) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<AllevamentoVO> result = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[AllevamentoDAO::getElencoAllevamentiExcel] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("" +
          		"SELECT  UT.INDIRIZZO AS INDIRIZZO_UTE, " +
              "        COM.DESCOM AS DESCOM_UTE, " +
              "        PRO.SIGLA_PROVINCIA AS SGLPROV_UTE, " +
              "        DA.CODICE_AZIENDA_ZOOTECNICA, " +
              "        DA.INDIRIZZO AS INDIRIZZO_ALL, " +
              "        DA.CAP AS CAP_ALL, " +
              "        COM_ALL.DESCOM AS DESCOM_ALL, " +
              "        PRO_ALL.SIGLA_PROVINCIA AS SGLPROV_ALL, " +
              "        TSA.DESCRIZIONE AS DESCRIZIONE_SPECIE, " +
              "        TCA.DESCRIZIONE AS DESCRIZIONE_CATEGORIA, " +
              "        TSTA.DESCRIZIONE AS DESCRIZIONE_SOTTOCATEGORIA, " +
              "        SA.QUANTITA, " +
              "        SA.QUANTITA_PROPRIETA, " +
              "        SA.ID_SOTTOCATEGORIA_ALLEVAMENTO, " +
              "        DA.CODICE_FISCALE_DETENTORE, " +
              "        DA.DENOMINAZIONE_DETENTORE, " +
              "        DA.CODICE_FISCALE_PROPRIETARIO, " +
              "        DA.DENOMINAZIONE_PROPRIETARIO, " +
              "        DA.FLAG_SOCCIDA " +
              "FROM    DB_UTE UT, " +
              "        COMUNE COM, " +
              "        PROVINCIA PRO, " +
              "        DB_ALLEVAMENTO DA, " +
              "        COMUNE COM_ALL, " +
              "        PROVINCIA PRO_ALL, " +
              "        DB_TIPO_SPECIE_ANIMALE TSA, " +
              "        DB_CATEGORIE_ALLEVAMENTO CA, " +
              "        DB_TIPO_CATEGORIA_ANIMALE TCA, " +
              "        DB_SOTTOCATEGORIA_ALLEVAMENTO SA, " +
              "        DB_TIPO_SOTTOCATEGORIA_ANIMALE TSTA " +
              "WHERE   UT.ID_AZIENDA = ? " +
              "AND     UT.COMUNE = COM.ISTAT_COMUNE " +
              "AND     COM.ISTAT_PROVINCIA = PRO.ISTAT_PROVINCIA " +
              "AND     UT.ID_UTE = DA.ID_UTE ");
      if(Validator.isNotEmpty(idUte))
      {
        queryBuf.append("" +
            "AND       DA.ID_UTE = ? ");
      }
      
      if(dataInserimentiDich !=null)
      {
        queryBuf
          .append("" +
              "AND       DA.DATA_INIZIO < ? " +
              "AND       NVL(DA.DATA_FINE, ?) > ? ");
      }
      else
      {
        queryBuf
          .append("" +
        		  "AND     DA.DATA_FINE IS NULL ");
      }
      
      queryBuf
          .append("" +
              "AND     DA.ISTAT_COMUNE = COM_ALL.ISTAT_COMUNE " +
              "AND     COM_ALL.ISTAT_PROVINCIA = PRO_ALL.ISTAT_PROVINCIA " +
              "AND     DA.ID_SPECIE_ANIMALE = TSA.ID_SPECIE_ANIMALE " +
              "AND     DA.ID_ALLEVAMENTO = CA.ID_ALLEVAMENTO " +
              "AND     CA.ID_CATEGORIA_ANIMALE = TCA.ID_CATEGORIA_ANIMALE " +
              "AND     CA.ID_CATEGORIE_ALLEVAMENTO = SA.ID_CATEGORIE_ALLEVAMENTO " +
              "AND     SA.ID_SOTTOCATEGORIA_ANIMALE = TSTA.ID_SOTTOCATEGORIA_ANIMALE ");
              
      
      queryBuf.append("ORDER BY DESCOM_UTE, INDIRIZZO_UTE, DESCRIZIONE_SPECIE");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AllevamentoDAO::getElencoAllevamentiExcel] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice,idAzienda);
      
      if(Validator.isNotEmpty(idUte))
      {
        stmt.setLong(++indice,idUte.longValue());
      }
      
      if(dataInserimentiDich !=null) 
      {
        stmt.setTimestamp(++indice, convertDateToTimestamp(dataInserimentiDich));
        stmt.setTimestamp(++indice, convertDateToTimestamp(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE)));
        stmt.setTimestamp(++indice, convertDateToTimestamp(dataInserimentiDich));
      }  
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(result == null)
        {
          result = new Vector<AllevamentoVO>();
        }
        
        AllevamentoVO allVO = new AllevamentoVO();
        allVO.setUteIndirizzo(rs.getString("INDIRIZZO_UTE"));
        allVO.setUteDescom(rs.getString("DESCOM_UTE"));
        allVO.setUteSglProv(rs.getString("SGLPROV_UTE"));
        allVO.setCodiceAziendaZootecnica(rs.getString("CODICE_AZIENDA_ZOOTECNICA"));
        allVO.setAllevamentoIndirizzo(rs.getString("INDIRIZZO_ALL"));
        allVO.setAllevamentoCap(rs.getString("CAP_ALL"));
        allVO.setAllevamentoDescom(rs.getString("DESCOM_ALL"));
        allVO.setAllevamentoSglProv(rs.getString("SGLPROV_ALL"));
        TipoSpecieAnimale tipoSpecieVO = new TipoSpecieAnimale();
        tipoSpecieVO.setDescrizione(rs.getString("DESCRIZIONE_SPECIE"));
        allVO.setTipoSpecieAnimaleVO(tipoSpecieVO);
        TipoCategoriaAnimale tipoCategoriaVO = new TipoCategoriaAnimale();
        tipoCategoriaVO.setDescrizione(rs.getString("DESCRIZIONE_CATEGORIA"));
        allVO.setTipoCategoriaAnimaleVO(tipoCategoriaVO);
        SottoCategoriaAllevamento sottoCategoriaVO = new SottoCategoriaAllevamento();
        sottoCategoriaVO.setQuantita(rs.getString("QUANTITA"));
        sottoCategoriaVO.setQuantitaProprieta(rs.getString("QUANTITA_PROPRIETA"));
        sottoCategoriaVO.setIdSottoCategoriaAllevamento(rs.getLong("ID_SOTTOCATEGORIA_ALLEVAMENTO"));
        allVO.setSottoCategoriaAllevamentoVO(sottoCategoriaVO);
        TipoSottoCategoriaAnimale tipoSottoCategoriaVO = new TipoSottoCategoriaAnimale();
        tipoSottoCategoriaVO.setDescrizione(rs.getString("DESCRIZIONE_SOTTOCATEGORIA"));
        allVO.setTipoSottoCategoriaAnimaleVO(tipoSottoCategoriaVO);        
        allVO.setCodFiscDetentore(rs.getString("CODICE_FISCALE_DETENTORE"));
        allVO.setDenDetentore(rs.getString("DENOMINAZIONE_DETENTORE"));
        allVO.setCodFiscProprietario(rs.getString("CODICE_FISCALE_PROPRIETARIO"));
        allVO.setDenProprietario(rs.getString("DENOMINAZIONE_PROPRIETARIO"));
        if("S".equalsIgnoreCase(rs.getString("FLAG_SOCCIDA")))
        {
          allVO.setSoccida("Si");
        }
        else
        {
          allVO.setSoccida("No");
        }
        
        
        result.add(allVO);
      }
      
      return result;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("allevamentiVO", result) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),  
        new Parametro("idPianoRiferimento", dataInserimentiDich) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AllevamentoDAO::getElencoAllevamentiExcel] ",
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
              "[AllevamentoDAO::getElencoAllevamentiExcel] END.");
    }
  }
  
  /**
   * restituisce un vettore di StabulazioneTrattamento
   * 
   * @param idSottoCategoriaAllevamento
   * @return
   * @throws DataAccessException
   */
  public Vector<StabulazioneTrattamento> getElencoStabulazioni(long idSottoCategoriaAllevamento) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<StabulazioneTrattamento> result = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[AllevamentoDAO::getElencoStabulazioni] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("" +
              "SELECT " +
              "       ST.QUANTITA_STABULATI, " +
              "       ST.ID_STABULAZIONE_TRATTAMENTO, " +
              "       ST.ID_SOTTOCATEGORIA_ALLEVAMENTO, " +
              "       TS.DESCRIZIONE, " +
              "       TT.CODICE_SIAP " +
              "FROM " +
              "       DB_STABULAZIONE_TRATTAMENTO ST, " +
              "       DB_TIPO_STABULAZIONE TS, " +
              "       DB_TIPO_TRATTAMENTO TT " +
              "WHERE " +
              "       ST.ID_SOTTOCATEGORIA_ALLEVAMENTO = ? " +
              "AND    ST.ID_STABULAZIONE = TS.ID_STABULAZIONE " +
              "AND    ST.ID_TRATTAMENTO = TT.ID_TRATTAMENTO (+)");
      
      
              
      
      //queryBuf.append("ORDER BY U.ID_UTE,C.DESCOM, U.INDIRIZZO ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AllevamentoDAO::getElencoStabulazioni] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice,idSottoCategoriaAllevamento);
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(result == null)
        {
          result = new Vector<StabulazioneTrattamento>();
        }
        
        StabulazioneTrattamento staTrattVO = new StabulazioneTrattamento();
        staTrattVO.setQuantita(rs.getString("QUANTITA_STABULATI"));
        staTrattVO.setIdStabulazioneTrattamento(rs.getLong("ID_STABULAZIONE_TRATTAMENTO"));
        staTrattVO.setDescStabulazione(rs.getString("DESCRIZIONE"));
        TipoTrattamento tipoTrattamento = new TipoTrattamento();
        tipoTrattamento.setCodiceSiap(rs.getString("CODICE_SIAP"));        
        staTrattVO.setTipoTrattamento(tipoTrattamento);
        result.add(staTrattVO);
      }
      
      return result;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("staTrattVO", result) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idSottoCategoriaAllevamento", idSottoCategoriaAllevamento) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AllevamentoDAO::getElencoStabulazioni] ",
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
              "[AllevamentoDAO::getElencoStabulazioni] END.");
    }
  }
  
  /**
   * Restituisce un Vectori di EffluenteProdotto.
   * Se trattamento == true (restituisce gli effluenti post trattamento)
   * Se trattamento == false (restituisce gli effluenti pre trattamento)
   * 
   * @param idStabulazioneTrattamento
   * @param trattamento
   * @return
   * @throws DataAccessException
   */
  public Vector<EffluenteProdotto> getElencoEffluenteProdotto(long idStabulazioneTrattamento, boolean trattamento) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<EffluenteProdotto> result = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[AllevamentoDAO::getElencoEffluenteProdotto] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("" +
              "SELECT " +
              "       EP.ID_EFFLUENTE_PRODOTTO, " +
              "       EP.ID_STABULAZIONE_TRATTAMENTO, " +
              "       EP.VOLUME_PRODOTTO, " +
              "       EP.AZOTO_PRODOTTO, " +
              "       TE.FLAG_PALABILE " +
              "FROM " +
              "       DB_EFFLUENTE_PRODOTTO EP, " +
              "       DB_TIPO_EFFLUENTE TE " +
              "WHERE " +
              "       EP.ID_STABULAZIONE_TRATTAMENTO = ? " +
              "AND    EP.ID_EFFLUENTE = TE.ID_EFFLUENTE " +
              "AND    EP.FLAG_TRATTAMENTO = ? ");             
      
      queryBuf.append("ORDER BY TE.FLAG_PALABILE DESC ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AllevamentoDAO::getElencoEffluenteProdotto] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice,idStabulazioneTrattamento);
      
      if(trattamento)
      {
        stmt.setString(++indice,"S");
      }
      else
      {
        stmt.setString(++indice,"N");
      }
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(result == null)
        {
          result = new Vector<EffluenteProdotto>();
        }
        
        EffluenteProdotto effProdVO = new EffluenteProdotto();
        effProdVO.setIdEffluenteProdotto(rs.getLong("ID_EFFLUENTE_PRODOTTO"));
        effProdVO.setIdStabulazioneTrattamento(rs.getLong("ID_STABULAZIONE_TRATTAMENTO"));
        effProdVO.setVolumeProdotto(rs.getBigDecimal("VOLUME_PRODOTTO"));
        effProdVO.setAzotoProdotto(rs.getBigDecimal("AZOTO_PRODOTTO"));
        effProdVO.setFlagPalabile(rs.getString("FLAG_PALABILE"));
        
        result.add(effProdVO);
      }
      
      return result;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("effProdVO", result) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idStabulazioneTrattamento", idStabulazioneTrattamento),  
        new Parametro("trattamento", trattamento)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AllevamentoDAO::getElencoEffluenteProdotto] ",
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
              "[AllevamentoDAO::getElencoEffluenteProdotto] END.");
    }
  }
  
  
  /**
   * 
   * restituisce un vettore di AllevamentoBioVO.
   * usato nel dettaglio allevamento 
   * 
   * 
   * 
   * @param idDichiarazioneConsistenza
   * @param idAllevamento
   * @return
   * @throws DataAccessException
   */
  public Vector<AllevamentoBioVO> getAllevamentiBio(Date dataInserimentoDichiarazione, long idAllevamento)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<AllevamentoBioVO> vAllevamentoBioVO = null;
    AllevamentoBioVO allevamentoBioVO = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[AllevamentoDAO::getAllevamentiBio] BEGIN.");
    
      query = " " +
          "SELECT AB.ID_ALLEVAMENTO_BIO, " +
          "       AB.ID_AZIENDA, " +
          "       AB.ID_UTE, " +
          "       AB.CODICE_AZIENDA_ZOOTECNICA, " +
          "       AB.ID_CATEGORIA_ANIMALE, " +
          "       AB.ID_SPECIE_ANIMALE, " +
          "       AB.QUANTITA, " +
          "       AB.QUANTITA_CONVENZIONALE, " +
          "       AB.QUANTITA_BIOLOGICO, " +
          "       AB.DATA_INIZIO_VALIDITA, " +
          "       AB.DATA_FINE_VALIDITA, " +
          "       TCA.DESCRIZIONE AS DESCRIZIONE_CATEGORIA," +
          "       TSA.UNITA_MISURA " +
          "FROM   DB_ALLEVAMENTO_BIO AB," +
          "       DB_ALLEVAMENTO AA, " +
          "       DB_TIPO_CATEGORIA_ANIMALE TCA, " +
          "       DB_TIPO_SPECIE_ANIMALE TSA " +
          "WHERE  AA.ID_ALLEVAMENTO = ? " +
          "AND    AB.ID_UTE = AA.ID_UTE ";
      
      if(Validator.isNotEmpty(dataInserimentoDichiarazione))
      {
        query += "" +
          "AND   AB.DATA_INIZIO_VALIDITA < ? " +
          "AND   NVL(AB.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? ";       
      }
      else
      {
        query += "" +
          "AND   AB.DATA_FINE_VALIDITA IS NULL ";
      }
      
      query += "" +
          "AND    NVL(AB.CODICE_AZIENDA_ZOOTECNICA, 'A') = NVL(AA.CODICE_AZIENDA_ZOOTECNICA,'A') " +
          "AND    AB.ID_SPECIE_ANIMALE = AA.ID_SPECIE_ANIMALE " +
          "AND    AB.ID_CATEGORIA_ANIMALE IN (" +
          "                                   SELECT CA.ID_CATEGORIA_ANIMALE " +
          "                                   FROM   DB_CATEGORIE_ALLEVAMENTO CA " +
          "                                   WHERE  CA.ID_ALLEVAMENTO = AA.ID_ALLEVAMENTO " +
          "                                  ) " +
          "AND   AB.ID_CATEGORIA_ANIMALE = TCA.ID_CATEGORIA_ANIMALE " +
          "AND   TCA.ID_SPECIE_ANIMALE = TSA.ID_SPECIE_ANIMALE " +
          "ORDER BY " +
          "      TCA.DESCRIZIONE ASC";
      
      
      
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AllevamentoDAO::getAllevamentiBio] Query="
                + query);
      }
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      int idx = 0;
      
      stmt.setLong(++idx, idAllevamento);
      
      
      if(Validator.isNotEmpty(dataInserimentoDichiarazione))
      {
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoDichiarazione));
        stmt.setTimestamp(++idx, convertDateToTimestamp(dataInserimentoDichiarazione));
      }
      
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      while (rs.next())
      {
        if(vAllevamentoBioVO == null)
        {
          vAllevamentoBioVO = new Vector<AllevamentoBioVO>();
        }
        
        allevamentoBioVO = new AllevamentoBioVO();
        allevamentoBioVO.setIdAllevamentoBio(rs.getLong("ID_ALLEVAMENTO_BIO"));
        allevamentoBioVO.setIdAzienda(rs.getLong("ID_AZIENDA"));
        allevamentoBioVO.setIdUte(rs.getLong("ID_UTE"));
        allevamentoBioVO.setCodiceAziendaZootecnica(rs.getString("CODICE_AZIENDA_ZOOTECNICA"));
        allevamentoBioVO.setIdCategoriaAnimale(rs.getLong("ID_CATEGORIA_ANIMALE"));
        allevamentoBioVO.setIdSpecieAnimale(rs.getLong("ID_SPECIE_ANIMALE"));
        allevamentoBioVO.setQuantita(rs.getBigDecimal("QUANTITA"));
        allevamentoBioVO.setQuantitaConvenzionale(rs.getBigDecimal("QUANTITA_CONVENZIONALE"));
        allevamentoBioVO.setQuantitaBiologico(rs.getBigDecimal("QUANTITA_BIOLOGICO"));
        allevamentoBioVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        allevamentoBioVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        allevamentoBioVO.setDescrizioneCategoria(rs.getString("DESCRIZIONE_CATEGORIA"));
        allevamentoBioVO.setUnitaMisura(rs.getString("UNITA_MISURA"));
        
        vAllevamentoBioVO.add(allevamentoBioVO);
        
      }
      
      return vAllevamentoBioVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
        new Variabile("allevamentoBioVO", allevamentoBioVO),
        new Variabile("vAllevamentoBioVO", vAllevamentoBioVO) };
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("dataInserimentoDichiarazione", dataInserimentoDichiarazione),
        new Parametro("idAllevamento", idAllevamento) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[AllevamentoDAO::getAllevamentiBio] ", t,
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
          "[AllevamentoDAO::getAllevamentiBio] END.");
    }
  }
  
  public Vector<TipoDestinoAcquaLavaggio> getElencoDestAcquaLavaggio() 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoDestinoAcquaLavaggio> result = null;
    TipoDestinoAcquaLavaggio tipoDestinoAcquaLavaggio = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[AllevamentoDAO::getElencoDestAcquaLavaggio] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("" +
              "SELECT DAL.ID_DESTINO_ACQUA_LAVAGGIO, " +
              "       DAL.DESCRIZIONE " +
              "FROM   DB_TIPO_DESTINO_ACQUA_LAVAGGIO DAL " +
              "WHERE  DAL.DATA_FINE_VALIDITA IS NULL ");             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AllevamentoDAO::getElencoDestAcquaLavaggio] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(result == null)
        {
          result = new Vector<TipoDestinoAcquaLavaggio>();
        }
        
        tipoDestinoAcquaLavaggio = new TipoDestinoAcquaLavaggio();
        tipoDestinoAcquaLavaggio.setIdDestinoAcquaLavaggio(rs.getLong("ID_DESTINO_ACQUA_LAVAGGIO"));
        tipoDestinoAcquaLavaggio.setDescrizione(rs.getString("DESCRIZIONE"));
        
        result.add(tipoDestinoAcquaLavaggio);
      }
      
      return result;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("result", result),
          new Variabile("tipoDestinoAcquaLavaggio", tipoDestinoAcquaLavaggio)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AllevamentoDAO::getElencoDestAcquaLavaggio] ",
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
              "[AllevamentoDAO::getElencoDestAcquaLavaggio] END.");
    }
  }
  
  public Long insertAllevamentoAcquaLavaggio(AllevamentoAcquaLavaggio allevamentoAcquaLavaggio) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idAllevamentoAcquaLavaggio = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[AllevamentoDAO::insertAllevamentoAcquaLavaggio] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idAllevamentoAcquaLavaggio = getNextPrimaryKey(SolmrConstants.SEQ_DB_ALLEVAMENTO_ACQUA_LAVAG);
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_ALLEVAMENTO_ACQUA_LAVAGGIO   " 
              + "     (ID_ALLEVAMENTO_ACQUA_LAVAGGIO, "
              + "     ID_ALLEVAMENTO, " 
              + "     ID_DESTINO_ACQUA_LAVAGGIO, "
              + "     QUANTITA_ACQUA_LAVAGGIO )  "
              + "   VALUES(?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AllevamentoDAO::insertAllevamentoAcquaLavaggio] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAllevamentoAcquaLavaggio.longValue());
      stmt.setLong(++indice,allevamentoAcquaLavaggio.getIdAllevamento());
      stmt.setLong(++indice, allevamentoAcquaLavaggio.getIdDestinoAcquaLavaggio());
      stmt.setBigDecimal(++indice, allevamentoAcquaLavaggio.getQuantitaAcquaLavaggio());     
      
  
      stmt.executeUpdate();
      
      return idAllevamentoAcquaLavaggio;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idAllevamentoAcquaLavaggio", idAllevamentoAcquaLavaggio)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("allevamentoAcquaLavaggio", allevamentoAcquaLavaggio)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AllevamentoDAO::insertAllevamentoAcquaLavaggio] ",
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
              "[AllevamentoDAO::insertAllevamentoAcquaLavaggio] END.");
    }
  }
  
  public void deleteAcquaLavaggio(long idAllevamento) 
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
              "[Comunicazione10RGaaDAO::deleteAcquaLavaggio] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   DELETE DB_ALLEVAMENTO_ACQUA_LAVAGGIO WHERE ID_ALLEVAMENTO = ?  " );
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::deleteAcquaLavaggio] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAllevamento);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAllevamento", idAllevamento)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::deleteAcquaLavaggio] ",
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
              "[Comunicazione10RGaaDAO::deleteAcquaLavaggio] END.");
    }
  }
  
  public Vector<AllevamentoAcquaLavaggio> getElencoAllevamentoAcquaLavaggio(long idAllevamento) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<AllevamentoAcquaLavaggio> result = null;
    AllevamentoAcquaLavaggio allevamentoAcquaLavaggio = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[AllevamentoDAO::getElencoAllevamentoAcquaLavaggio] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("" +
              "SELECT DAL.ID_ALLEVAMENTO_ACQUA_LAVAGGIO, " +
              "       DAL.ID_ALLEVAMENTO, " +
              "       DAL.ID_DESTINO_ACQUA_LAVAGGIO, " +
              "       DAL.QUANTITA_ACQUA_LAVAGGIO, " +
              "       TDAL.DESCRIZIONE " +
              "FROM   DB_ALLEVAMENTO_ACQUA_LAVAGGIO DAL, " +
              "       DB_TIPO_DESTINO_ACQUA_LAVAGGIO TDAL " +
              "WHERE  DAL.ID_ALLEVAMENTO = ? " +
              "AND    DAL.ID_DESTINO_ACQUA_LAVAGGIO = TDAL.ID_DESTINO_ACQUA_LAVAGGIO " +
              "ORDER BY DAL.ID_DESTINO_ACQUA_LAVAGGIO ASC ");             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AllevamentoDAO::getElencoAllevamentoAcquaLavaggio] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int idx = 0;
      
      stmt.setLong(++idx, idAllevamento);
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(result == null)
        {
          result = new Vector<AllevamentoAcquaLavaggio>();
        }
        
        allevamentoAcquaLavaggio = new AllevamentoAcquaLavaggio();
        allevamentoAcquaLavaggio.setIdAllevamentoAcquaLavaggio(rs.getLong("ID_ALLEVAMENTO_ACQUA_LAVAGGIO"));
        allevamentoAcquaLavaggio.setIdAllevamento(rs.getLong("ID_ALLEVAMENTO"));
        allevamentoAcquaLavaggio.setIdDestinoAcquaLavaggio(rs.getLong("ID_DESTINO_ACQUA_LAVAGGIO"));
        allevamentoAcquaLavaggio.setQuantitaAcquaLavaggio(rs.getBigDecimal("QUANTITA_ACQUA_LAVAGGIO"));
        allevamentoAcquaLavaggio.setQuantitaAcquaLavaggioStr(Formatter.formatDouble1(rs.getBigDecimal("QUANTITA_ACQUA_LAVAGGIO")));
        allevamentoAcquaLavaggio.setDescDestinoAcquaLavaggio(rs.getString("DESCRIZIONE"));
        
        
        result.add(allevamentoAcquaLavaggio);
      }
      
      return result;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("result", result),
          new Variabile("allevamentoAcquaLavaggio", allevamentoAcquaLavaggio)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AllevamentoDAO::getElencoAllevamentoAcquaLavaggio] ",
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
              "[AllevamentoDAO::getElencoAllevamentoAcquaLavaggio] END.");
    }
  }
  
  public AllevamentoAcquaLavaggio getAllevamentoAcquaLavaggio(long idAllevamento, long idDestinoAcquaLavaggio) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    AllevamentoAcquaLavaggio allevamentoAcquaLavaggio = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[AllevamentoDAO::getAllevamentoAcquaLavaggio] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("" +
              "SELECT DAL.ID_ALLEVAMENTO_ACQUA_LAVAGGIO, " +
              "       DAL.ID_ALLEVAMENTO, " +
              "       DAL.ID_DESTINO_ACQUA_LAVAGGIO, " +
              "       DAL.QUANTITA_ACQUA_LAVAGGIO " +
              "FROM   DB_ALLEVAMENTO_ACQUA_LAVAGGIO DAL " +
              "WHERE  DAL.ID_ALLEVAMENTO = ? " +
              "AND    DAL.ID_DESTINO_ACQUA_LAVAGGIO = ? ");             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AllevamentoDAO::getAllevamentoAcquaLavaggio] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int idx = 0;
      
      stmt.setLong(++idx, idAllevamento);
      stmt.setLong(++idx, idDestinoAcquaLavaggio);
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {        
        allevamentoAcquaLavaggio = new AllevamentoAcquaLavaggio();
        allevamentoAcquaLavaggio.setIdAllevamentoAcquaLavaggio(rs.getLong("ID_ALLEVAMENTO_ACQUA_LAVAGGIO"));
        allevamentoAcquaLavaggio.setIdAllevamento(rs.getLong("ID_ALLEVAMENTO"));
        allevamentoAcquaLavaggio.setIdDestinoAcquaLavaggio(rs.getLong("ID_DESTINO_ACQUA_LAVAGGIO"));
        allevamentoAcquaLavaggio.setQuantitaAcquaLavaggio(rs.getBigDecimal("QUANTITA_ACQUA_LAVAGGIO"));
        allevamentoAcquaLavaggio.setQuantitaAcquaLavaggioStr(Formatter.formatDouble1(rs.getBigDecimal("QUANTITA_ACQUA_LAVAGGIO")));
        
      }
      
      return allevamentoAcquaLavaggio;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("allevamentoAcquaLavaggio", allevamentoAcquaLavaggio)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AllevamentoDAO::getAllevamentoAcquaLavaggio] ",
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
              "[AllevamentoDAO::getAllevamentoAcquaLavaggio] END.");
    }
  }
  
  
  public void updateAllevamentoAcquaLavaggio(AllevamentoAcquaLavaggio allevamentoAcquaLavaggio) 
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
              "[AllevamentoDAO::updateAllevamentoAcquaLavaggio] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   UPDATE DB_ALLEVAMENTO_ACQUA_LAVAGGIO   " 
              + "     SET QUANTITA_ACQUA_LAVAGGIO = ? "
              + "   WHERE  "
              + "     ID_ALLEVAMENTO_ACQUA_LAVAGGIO = ?  ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AllevamentoDAO::updateAllevamentoAcquaLavaggio] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setBigDecimal(++indice, allevamentoAcquaLavaggio.getQuantitaAcquaLavaggio());
      stmt.setLong(++indice, allevamentoAcquaLavaggio.getIdAllevamentoAcquaLavaggio());
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("allevamentoAcquaLavaggio", allevamentoAcquaLavaggio)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AllevamentoDAO::updateAllevamentoAcquaLavaggio] ",
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
              "[AllevamentoDAO::updateAllevamentoAcquaLavaggio] END.");
    }
  }
  
  
  public Vector<Long> getElencoSpecieAzienda(long idAzienda) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<Long> result = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[AllevamentoDAO::getElencoSpecieAzienda] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("" +
              "SELECT  DISTINCT " +
              "        DA.ID_SPECIE_ANIMALE " +
              "FROM    DB_UTE UT, " +
              "        DB_ALLEVAMENTO DA " +
              "WHERE   UT.ID_AZIENDA = ? " +
              "AND     UT.ID_UTE = DA.ID_UTE " +
              "AND     DA.DATA_FINE IS NULL ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AllevamentoDAO::getElencoSpecieAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice,idAzienda);
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(result == null)
        {
          result = new Vector<Long>();
        }
        
        
        
        result.add(new Long(rs.getLong("ID_SPECIE_ANIMALE")));
      }
      
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
      { new Parametro("idAzienda", idAzienda) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AllevamentoDAO::getElencoSpecieAzienda] ",
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
              "[AllevamentoDAO::getElencoSpecieAzienda] END.");
    }
  }
  
  
  /**
   * 
   * Ritorna un record per ogni ute con la somma delle acque lavaggio
   * 0 altrimenti.
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  /*public HashMap<Long,BigDecimal> getAllevamentoTotAcquaLavaggio(long idAzienda) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    HashMap<Long,BigDecimal> hUteAcquaLav = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[AllevamentoDAO::getAllevamentoTotAcquaLavaggio] BEGIN.");
  
      //CONCATENAZIONE/CREAZIONE QUERY BEGIN. 
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("" +
              "SELECT NVL(SUM(AL.QUANTITA_ACQUA_LAVAGGIO),0) AS TOT, " +
              "       AL.ID_UTE " +
              "FROM   DB_ALLEVAMENTO AL, " +
              "       DB_UTE UT " +
              "WHERE  UT.ID_AZIENDA = ? " +
              "AND    UT.ID_UTE = AL.ID_UTE " +
              "AND    UT.DATA_FINE_ATTIVITA IS NULL " +
              "AND    AL.DATA_FINE IS NULL " +
              "GROUP BY AL.ID_UTE ");             
      
      query = queryBuf.toString();
      // CONCATENAZIONE/CREAZIONE QUERY END. 
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AllevamentoDAO::getAllevamentoTotAcquaLavaggio] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int idx = 0;
      
      stmt.setLong(++idx, idAzienda);
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(hUteAcquaLav == null)
        {
          hUteAcquaLav = new HashMap<Long,BigDecimal>();
        }
        
        hUteAcquaLav.put(new Long(rs.getLong("ID_UTE")), rs.getBigDecimal("TOT"));
      }
      
      return hUteAcquaLav;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("hUteAcquaLav", hUteAcquaLav) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AllevamentoDAO::getAllevamentoTotAcquaLavaggio] ",
              t, query, variabili, parametri);
      
      //Rimappo e rilancio l'eccezione come DataAccessException.
      
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      
       //Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       //ignora ogni eventuale eccezione)
       
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[AllevamentoDAO::getAllevamentoTotAcquaLavaggio] END.");
    }
  }*/
  
  
  public Vector<AllevamentoVO> getElencoAllevamentiSian(String cuaa, 
      long idTipoSpecie, String codAziendaZoo) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<AllevamentoVO> vAllevamentiSian = null;
    AllevamentoVO allevamentoVO = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[AllevamentoDAO::getElencoAllevamentiSian] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "SELECT ALS.CODICE_FISCALE_PROPRIETARIO, " +
        "       ALS.CODICE_FISCALE_DETENTORE " +
        "FROM   DB_ALLEVAMENTI_SIAN ALS, " +
        "       DB_R_SPECIE_AN_SIAN_SIAP SASS " +
        "WHERE  ALS.CUAA = ? " +
        "AND    ALS.FLAG_PRESENZA_SIAN = 'S' " +      
        "AND    SASS.ID_SPECIE_ANIMALE = ? " +
        "AND    ALS.CODICE_SPECIE = SASS.CODICE_SPECIE " +
        "AND    SASS.FLAG_RELAZIONE_ATTIVA = 'S' " +
        "AND    ALS.CODICE_AZIENDA = ? ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AllevamentoDAO::getElencoAllevamentiSian] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setString(++indice, cuaa);
      stmt.setLong(++indice, idTipoSpecie);
      stmt.setString(++indice, codAziendaZoo);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vAllevamentiSian == null)
        {
          vAllevamentiSian = new Vector<AllevamentoVO>();
        }
        allevamentoVO = new AllevamentoVO(); 
        allevamentoVO.setCodFiscProprietario(rs.getString("CODICE_FISCALE_PROPRIETARIO"));
        allevamentoVO.setCodFiscDetentore(rs.getString("CODICE_FISCALE_DETENTORE"));
        
        
        vAllevamentiSian.add(allevamentoVO);
      }
      
      return vAllevamentiSian;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vAllevamentiSian", vAllevamentiSian),
          new Variabile("allevamentoVO", allevamentoVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("cuaa", cuaa),
        new Parametro("idTipoSpecie", idTipoSpecie),
        new Parametro("codAziendaZoo", codAziendaZoo) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AllevamentoDAO::getElencoAllevamentiSian] ",
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
              "[AllevamentoDAO::getElencoAllevamentiSian] END.");
    }
  }
  
  
  public Vector<EsitoControlloAllevamento> getElencoEsitoControlloAllevamento(long idAllevamento) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<EsitoControlloAllevamento> vEsitoCtrl = null;
    EsitoControlloAllevamento esitoCtrl = null;
    
    try
    {
      SolmrLogger.debug(this, "[AllevamentoDAO::getElencoEsitoControlloAllevamento] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "SELECT  ECA.ID_ESITO_CONTROLLO_ALLEVAMENTO, " +
        "        ECA.ID_ALLEVAMENTO,  " +
        "        ECA.ID_CONTROLLO, " +
        "        ECA.BLOCCANTE, " +
        "        ECA.DESCRIZIONE AS DESC_ESITO, " +
        "        TC.DESCRIZIONE AS DESC_CONTROLLO " +
        "FROM    DB_ESITO_CONTROLLO_ALLEVAMENTO ECA, " +
        "        DB_TIPO_CONTROLLO TC " +
        "WHERE   ECA.ID_ALLEVAMENTO = ? " +
        "AND     ECA.ID_CONTROLLO = TC.ID_CONTROLLO ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AllevamentoDAO::getElencoEsitoControlloAllevamento] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice,idAllevamento);
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vEsitoCtrl == null)
        {
          vEsitoCtrl = new Vector<EsitoControlloAllevamento>();
        }
        esitoCtrl = new EsitoControlloAllevamento();
        esitoCtrl.setIdEsitoControlloAllevamento(rs.getLong("ID_ESITO_CONTROLLO_ALLEVAMENTO"));
        esitoCtrl.setIdAllevamento(rs.getLong("ID_ALLEVAMENTO"));
        esitoCtrl.setIdControllo(rs.getLong("ID_CONTROLLO"));
        esitoCtrl.setDescrizione(rs.getString("DESC_ESITO"));
        esitoCtrl.setDescControllo(rs.getString("DESC_CONTROLLO"));
        esitoCtrl.setBloccante(rs.getString("BLOCCANTE"));
        
        vEsitoCtrl.add(esitoCtrl);
      }
      
      return vEsitoCtrl;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vEsitoCtrl", vEsitoCtrl),  
          new Variabile("esitoCtrl", esitoCtrl) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAllevamento", idAllevamento) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AllevamentoDAO::getElencoEsitoControlloAllevamento] ",
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
              "[AllevamentoDAO::getElencoEsitoControlloAllevamento] END.");
    }
  }
  
  
  public HashMap<Long, ControlloAllevamenti> getEsitoControlliAllevamentiAzienda(long idAzienda) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    HashMap<Long, ControlloAllevamenti> hEsitoCtrl = null;
    ControlloAllevamenti esitoCtrl = null;
    Vector<AllevamentoAnagVO> vAllevamenti = null;
    AllevamentoAnagVO allevamentoAnagVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[AllevamentoDAO::getEsitoControlliAllevamentiAzienda] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "SELECT TSA.DESCRIZIONE AS DESC_SPECIE, " +
        "       AL.CODICE_AZIENDA_ZOOTECNICA, " +
        "       AL.CODICE_FISCALE_DETENTORE, " +
        "       AL.DENOMINAZIONE_DETENTORE, " +
        "       AL.CODICE_FISCALE_PROPRIETARIO, " +
        "       AL.DENOMINAZIONE_PROPRIETARIO, " +
        "       ECA.DESCRIZIONE AS DESC_ESITO, " +
        "       ECA.ID_CONTROLLO, " +
        "       ECA.BLOCCANTE " +
        "FROM   DB_UTE UT, " +
        "       DB_ALLEVAMENTO AL, " +
        "       DB_TIPO_SPECIE_ANIMALE TSA, " +
        "       DB_ESITO_CONTROLLO_ALLEVAMENTO ECA " +
        "WHERE  UT.ID_AZIENDA = ? " +
        "AND    UT.DATA_FINE_ATTIVITA IS NULL " +
        "AND    UT.ID_UTE = AL.ID_UTE " +
        "AND    AL.DATA_FINE IS NULL " +
        "AND    AL.ID_SPECIE_ANIMALE = TSA.ID_SPECIE_ANIMALE " +
        "AND    AL.ID_ALLEVAMENTO = ECA.ID_ALLEVAMENTO " +
        "ORDER BY ECA.ID_CONTROLLO, TSA.DESCRIZIONE");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AllevamentoDAO::getEsitoControlliAllevamentiAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAzienda);
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      long idControlloTmp = 0;
      while (rs.next())
      {
        if(hEsitoCtrl == null)
        {
          hEsitoCtrl = new HashMap<Long, ControlloAllevamenti>();
        }
        
        long idControllo = rs.getLong("ID_CONTROLLO");
        if(idControlloTmp != idControllo)
        {
          if(idControlloTmp != 0)
          {
            esitoCtrl.setvAllevamenti(vAllevamenti);
            hEsitoCtrl.put(new Long(idControlloTmp), esitoCtrl);
          }
          esitoCtrl = new ControlloAllevamenti();
          vAllevamenti = new Vector<AllevamentoAnagVO>();
          esitoCtrl.setIdControllo(idControllo);
          esitoCtrl.setDescrizione(rs.getString("DESC_ESITO"));          
          esitoCtrl.setBloccante(rs.getString("BLOCCANTE"));
        }
        
        allevamentoAnagVO = new AllevamentoAnagVO();
        TipoSpecieAnimaleAnagVO tipoSpecieAnimaleAnagVO = new TipoSpecieAnimaleAnagVO();
        tipoSpecieAnimaleAnagVO.setDescrizione(rs.getString("DESC_SPECIE"));
        allevamentoAnagVO.setTipoSpecieAnimaleAnagVO(tipoSpecieAnimaleAnagVO);
        allevamentoAnagVO.setCodiceAziendaZootecnica(rs.getString("CODICE_AZIENDA_ZOOTECNICA"));
        allevamentoAnagVO.setCodiceFiscaleDetentore(rs.getString("CODICE_FISCALE_DETENTORE"));
        allevamentoAnagVO.setDenominazioneDetentore(rs.getString("DENOMINAZIONE_DETENTORE"));
        allevamentoAnagVO.setCodiceFiscaleProprietario(rs.getString("CODICE_FISCALE_PROPRIETARIO"));
        allevamentoAnagVO.setDenominazioneProprietario(rs.getString("DENOMINAZIONE_PROPRIETARIO"));
        
        idControlloTmp = idControllo;
        vAllevamenti.add(allevamentoAnagVO);
        
      }
      
      if(esitoCtrl != null)
      {
        esitoCtrl.setvAllevamenti(vAllevamenti);
        hEsitoCtrl.put(new Long(idControlloTmp), esitoCtrl);
      }
      
      return hEsitoCtrl;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("hEsitoCtrl", hEsitoCtrl),  
          new Variabile("esitoCtrl", esitoCtrl),
          new Variabile("vAllevamenti", vAllevamenti),  
          new Variabile("allevamentoAnagVO", allevamentoAnagVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AllevamentoDAO::getEsitoControlliAllevamentiAzienda] ",
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
              "[AllevamentoDAO::getEsitoControlliAllevamentiAzienda] END.");
    }
  }
  
  public HashMap<Long, ControlloAllevamenti> getSegnalazioniControlliAllevamentiAzienda(long idAzienda) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    HashMap<Long, ControlloAllevamenti> hEsitoCtrl = null;
    ControlloAllevamenti esitoCtrl = null;
    
    try
    {
      SolmrLogger.debug(this, "[AllevamentoDAO::getSegnalazioniControlliAllevamentiAzienda] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
          "SELECT DISTINCT " +
          "       TC.ID_CONTROLLO, " +
          "       DS.BLOCCANTE, " +
          "       DS.DESCRIZIONE AS DESC_SEGNALAZIONE " +
          "FROM   DB_TIPO_CONTROLLO TC, " +
          "       DB_TIPO_CONTROLLO_FASE TCF, " +
          "       DB_DICHIARAZIONE_SEGNALAZIONE DS " +
          "WHERE  TC.ID_GRUPPO_CONTROLLO = 6 " +
          "AND    TC.ID_CONTROLLO = TCF.ID_CONTROLLO " +
          "AND    TC.OBBLIGATORIO = 'S' " +
          "AND    DS.ID_AZIENDA = ? " +
          "AND    TC.ID_CONTROLLO = DS.ID_CONTROLLO " +
          "AND    DS.ID_DICHIARAZIONE_CONSISTENZA IS NULL " +
          "ORDER BY TC.ID_CONTROLLO ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AllevamentoDAO::getSegnalazioniControlliAllevamentiAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAzienda);
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(hEsitoCtrl == null)
        {
          hEsitoCtrl = new HashMap<Long, ControlloAllevamenti>();
        }
        
        esitoCtrl = new ControlloAllevamenti();
        esitoCtrl.setIdControllo(rs.getLong("ID_CONTROLLO"));
        esitoCtrl.setDescrizione(rs.getString("DESC_SEGNALAZIONE"));          
        esitoCtrl.setBloccante(rs.getString("BLOCCANTE"));        
        
        hEsitoCtrl.put(new Long(esitoCtrl.getIdControllo()), esitoCtrl);
      }
      
      
      return hEsitoCtrl;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("hEsitoCtrl", hEsitoCtrl),  
          new Variabile("esitoCtrl", esitoCtrl) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AllevamentoDAO::getSegnalazioniControlliAllevamentiAzienda] ",
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
              "[AllevamentoDAO::getSegnalazioniControlliAllevamentiAzienda] END.");
    }
  }

}
