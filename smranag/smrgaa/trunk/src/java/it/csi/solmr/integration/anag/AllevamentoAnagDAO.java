package it.csi.solmr.integration.anag;

import it.csi.smranag.smrgaa.dto.allevamenti.SottoCategoriaAllevamento;
import it.csi.smranag.smrgaa.dto.allevamenti.StabulazioneTrattamento;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.ProvinciaVO;
import it.csi.solmr.dto.anag.AllevamentoAnagVO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.CategorieAllevamentoAnagVO;
import it.csi.solmr.dto.anag.TipoASLAnagVO;
import it.csi.solmr.dto.anag.TipoCategoriaAnimaleAnagVO;
import it.csi.solmr.dto.anag.TipoSpecieAnimaleAnagVO;
import it.csi.solmr.dto.anag.sian.SianAllevamentiVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.SolmrErrors;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.NotFoundException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.integration.BaseDAO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

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
 * @author TOBECONFIG
 * @version 1.0
 */

public class AllevamentoAnagDAO extends BaseDAO
{

  public AllevamentoAnagDAO(String refName) throws ResourceAccessException
  {
    super(refName);
  }

  /**
   * Restituisce il tipoSpecieAnimale corrispondente all'idSpecieAnimale
   * 
   * @param idSpecieAnimale
   *          Long
   * @return TipoSpecieAnimaleAnagVO
   * @throws DataAccessException
   * @throws NotFoundException
   */
  public TipoSpecieAnimaleAnagVO getTipoSpecieAnimale(Long idSpecieAnimale) throws DataAccessException, NotFoundException
  {
    TipoSpecieAnimaleAnagVO tipoSpecieAnimaleAnagVO = null;

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = 
        "SELECT ID_SPECIE_ANIMALE, " + 
        "       DESCRIZIONE, " + 
        "       UNITA_MISURA, " + 
        "       FLAG_OBBLIGO_ASL, " +
        "       FLAG_MODIFICA_COD_AZ_ZOO, " + 
        "       TO_CHAR(DATA_INIZIO_VALIDITA,'DD/MM/YYYY') AS DATA_INIZIO_VALIDITA, " + 
        "       TO_CHAR(DATA_FINE_VALIDITA,'DD/MM/YYYY') AS DATA_FINE_VALIDITA, " +
        "       FLAG_STALLA_PASCOLO, " +
        "       ALT_LETTIERA_PERMANENTE_MIN, " +
        "       ALT_LETTIERA_PERMANENTE_MAX, " +
        "       FLAG_CONTROLLO_COMUNE " + 
        "FROM   DB_TIPO_SPECIE_ANIMALE " + 
        "WHERE  ID_SPECIE_ANIMALE = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setLong(1, idSpecieAnimale.longValue());
      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        tipoSpecieAnimaleAnagVO = new TipoSpecieAnimaleAnagVO();
        tipoSpecieAnimaleAnagVO.setIdSpecieAnimaleLong(new Long(rs.getLong(1)));
        tipoSpecieAnimaleAnagVO.setDescrizione(rs.getString(2));
        tipoSpecieAnimaleAnagVO.setUnitaDiMisura(rs.getString(3));
        tipoSpecieAnimaleAnagVO.setFlagObbligoAsl(rs.getString(4));
        if (SolmrConstants.FLAG_S.equals(rs.getString("FLAG_MODIFICA_COD_AZ_ZOO")))
          tipoSpecieAnimaleAnagVO.setFlagMofCodAzZoot(true);
        else
          tipoSpecieAnimaleAnagVO.setFlagMofCodAzZoot(false);
        tipoSpecieAnimaleAnagVO.setDataInizioValidita(rs.getString("DATA_INIZIO_VALIDITA"));
        tipoSpecieAnimaleAnagVO.setDataFineValidita(rs.getString("DATA_FINE_VALIDITA"));

        if ("S".equals(rs.getString("FLAG_STALLA_PASCOLO")))
          tipoSpecieAnimaleAnagVO.setFlagStallaPascolo(true);
        else
          tipoSpecieAnimaleAnagVO.setFlagStallaPascolo(false);
        
        tipoSpecieAnimaleAnagVO.setAltLettieraPermMin(rs.getDouble("ALT_LETTIERA_PERMANENTE_MIN"));
        tipoSpecieAnimaleAnagVO.setAltLettieraPermMax(rs.getDouble("ALT_LETTIERA_PERMANENTE_MAX"));
        tipoSpecieAnimaleAnagVO.setFlagControlloComune(rs.getString("FLAG_CONTROLLO_COMUNE"));
        
      }
      
      rs.close();

      if (tipoSpecieAnimaleAnagVO == null)
        throw new NotFoundException(SolmrErrors.EXC_NOT_FOUND_PK);

      SolmrLogger.debug(this, "Executed query - Found record with primary key: " + idSpecieAnimale);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTipoSpecieAnimale - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (NotFoundException nfexc)
    {
      SolmrLogger.fatal(this, "getTipoSpecieAnimale - NotFoundException: " + nfexc.getMessage());
      throw nfexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getTipoSpecieAnimale - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "getTipoSpecieAnimale - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "getTipoSpecieAnimale - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return tipoSpecieAnimaleAnagVO;
  }

  /**
   * Restituisce l'elenco dei tipi specie animale
   * 
   * @return Vector
   * @throws DataAccessException
   * @throws NotFoundException
   */
  public Vector<TipoSpecieAnimaleAnagVO> getTipiSpecieAnimale() throws DataAccessException, NotFoundException
  {
    TipoSpecieAnimaleAnagVO tipoSpecieAnimaleAnagVO = null;
    Vector<TipoSpecieAnimaleAnagVO> results = new Vector<TipoSpecieAnimaleAnagVO>();

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "select id_specie_animale, " + "descrizione, " + "unita_misura, " + "FLAG_MODIFICA_COD_AZ_ZOO, " + "FLAG_STALLA_PASCOLO, "
          + "TO_CHAR(DATA_INIZIO_VALIDITA,'DD/MM/YYYY') AS DATA_INIZIO_VALIDITA," + "TO_CHAR(DATA_FINE_VALIDITA,'DD/MM/YYYY') AS DATA_FINE_VALIDITA, " 
          + "ALT_LETTIERA_PERMANENTE_MIN, ALT_LETTIERA_PERMANENTE_MAX "
          + "from db_tipo_specie_animale "
          + "where FLAG_MODIFICA_COD_AZ_ZOO = 'S' " + "AND DATA_FINE_VALIDITA IS NULL " + "order by descrizione";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        tipoSpecieAnimaleAnagVO = new TipoSpecieAnimaleAnagVO();
        tipoSpecieAnimaleAnagVO.setIdSpecieAnimaleLong(new Long(rs.getLong(1)));
        tipoSpecieAnimaleAnagVO.setDescrizione(rs.getString(2));
        tipoSpecieAnimaleAnagVO.setUnitaDiMisura(rs.getString(3));
        if (SolmrConstants.FLAG_S.equals(rs.getString("FLAG_MODIFICA_COD_AZ_ZOO")))
          tipoSpecieAnimaleAnagVO.setFlagMofCodAzZoot(true);
        else
          tipoSpecieAnimaleAnagVO.setFlagMofCodAzZoot(false);

        if (SolmrConstants.FLAG_S.equals(rs.getString("FLAG_STALLA_PASCOLO")))
          tipoSpecieAnimaleAnagVO.setFlagStallaPascolo(true);
        else
          tipoSpecieAnimaleAnagVO.setFlagStallaPascolo(false);

        tipoSpecieAnimaleAnagVO.setDataInizioValidita(rs.getString("DATA_INIZIO_VALIDITA"));
        tipoSpecieAnimaleAnagVO.setDataFineValidita(rs.getString("DATA_FINE_VALIDITA"));
        
        tipoSpecieAnimaleAnagVO.setAltLettieraPermMin(rs.getDouble("ALT_LETTIERA_PERMANENTE_MIN"));
        tipoSpecieAnimaleAnagVO.setAltLettieraPermMax(rs.getDouble("ALT_LETTIERA_PERMANENTE_MAX"));

        results.add(tipoSpecieAnimaleAnagVO);

      }

      rs.close();
      
      if (tipoSpecieAnimaleAnagVO == null)
        throw new NotFoundException(SolmrErrors.EXC_NOT_FOUND_PK);

      SolmrLogger.debug(this, "Executed query - Found " + results.size() + " record");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTipiSpecieAnimale - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (NotFoundException nfexc)
    {
      SolmrLogger.fatal(this, "getTipiSpecieAnimale - NotFoundException: " + nfexc.getMessage());
      throw nfexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getTipiSpecieAnimale - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "getTipiSpecieAnimale - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "getTipiSpecieAnimale - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return results;
  }
  
  
  /**
   * Restituisce l'elenco dei tipi specie animale per le aziende provvisorie
   * Non filtra per FLAG_MODIFICA_COD_AZ_ZOO = 'S'
   * 
   * @return Vector
   * @throws DataAccessException
   * @throws NotFoundException
   */
  public Vector<TipoSpecieAnimaleAnagVO> getTipiSpecieAnimaleAzProv() throws DataAccessException, NotFoundException
  {
    TipoSpecieAnimaleAnagVO tipoSpecieAnimaleAnagVO = null;
    Vector<TipoSpecieAnimaleAnagVO> results = new Vector<TipoSpecieAnimaleAnagVO>();

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = 
          "select id_specie_animale, " + 
          "       descrizione, " +
          "       unita_misura, " + 
          "       FLAG_MODIFICA_COD_AZ_ZOO, " +
          "       FLAG_STALLA_PASCOLO, " +
          "       FLAG_OBBLIGO_ASL, " + 
          "       TO_CHAR(DATA_INIZIO_VALIDITA,'DD/MM/YYYY') AS DATA_INIZIO_VALIDITA," +
          "       TO_CHAR(DATA_FINE_VALIDITA,'DD/MM/YYYY') AS DATA_FINE_VALIDITA, " + 
          "       ALT_LETTIERA_PERMANENTE_MIN, " +
          "       ALT_LETTIERA_PERMANENTE_MAX " + 
          "from   db_tipo_specie_animale " + 
          "       where DATA_FINE_VALIDITA IS NULL " +
          "       order by descrizione";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        tipoSpecieAnimaleAnagVO = new TipoSpecieAnimaleAnagVO();
        tipoSpecieAnimaleAnagVO.setIdSpecieAnimaleLong(new Long(rs.getLong(1)));
        tipoSpecieAnimaleAnagVO.setDescrizione(rs.getString(2));
        tipoSpecieAnimaleAnagVO.setUnitaDiMisura(rs.getString(3));
        if (SolmrConstants.FLAG_S.equals(rs.getString("FLAG_MODIFICA_COD_AZ_ZOO")))
          tipoSpecieAnimaleAnagVO.setFlagMofCodAzZoot(true);
        else
          tipoSpecieAnimaleAnagVO.setFlagMofCodAzZoot(false);

        if (SolmrConstants.FLAG_S.equals(rs.getString("FLAG_STALLA_PASCOLO")))
          tipoSpecieAnimaleAnagVO.setFlagStallaPascolo(true);
        else
          tipoSpecieAnimaleAnagVO.setFlagStallaPascolo(false);
        
        tipoSpecieAnimaleAnagVO.setFlagObbligoAsl(rs.getString("FLAG_OBBLIGO_ASL"));

        tipoSpecieAnimaleAnagVO.setDataInizioValidita(rs.getString("DATA_INIZIO_VALIDITA"));
        tipoSpecieAnimaleAnagVO.setDataFineValidita(rs.getString("DATA_FINE_VALIDITA"));
        
        tipoSpecieAnimaleAnagVO.setAltLettieraPermMin(rs.getDouble("ALT_LETTIERA_PERMANENTE_MIN"));
        tipoSpecieAnimaleAnagVO.setAltLettieraPermMax(rs.getDouble("ALT_LETTIERA_PERMANENTE_MAX"));

        results.add(tipoSpecieAnimaleAnagVO);

      }
      
      rs.close();

      if (tipoSpecieAnimaleAnagVO == null)
        throw new NotFoundException(SolmrErrors.EXC_NOT_FOUND_PK);

      SolmrLogger.debug(this, "Executed query - Found " + results.size() + " record");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTipiSpecieAnimale - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (NotFoundException nfexc)
    {
      SolmrLogger.fatal(this, "getTipiSpecieAnimale - NotFoundException: " + nfexc.getMessage());
      throw nfexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getTipiSpecieAnimale - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "getTipiSpecieAnimale - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "getTipiSpecieAnimale - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return results;
  }

  /**
   * Restituisce il tipoCategoriaAnimale corrispondente all'idCategoriaAnimale
   * 
   * @param idCategoriaAnimale
   *          Long
   * @return TipoCategoriaAnimaleAnagVO
   * @throws DataAccessException
   * @throws NotFoundException
   */
  public TipoCategoriaAnimaleAnagVO getTipoCategoriaAnimale(Long idCategoriaAnimale) 
      throws DataAccessException
  {
    TipoCategoriaAnimaleAnagVO tipoCategoriaAnimaleAnagVO = null;

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = 
        "SELECT ID_CATEGORIA_ANIMALE, " + 
        "       ID_SPECIE_ANIMALE, " + 
        "       DESCRIZIONE, " + 
        "       CONSUMO_ANNUO_UF, " + 
        "       PESO_VIVO_MEDIO, " + 
        "       COEFFICIENTE_UBA, " +
        "       LATTAZIONE " +
        "FROM   DB_TIPO_CATEGORIA_ANIMALE " +
        "WHERE  ID_CATEGORIA_ANIMALE = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setLong(1, idCategoriaAnimale.longValue());
      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        tipoCategoriaAnimaleAnagVO = new TipoCategoriaAnimaleAnagVO();
        tipoCategoriaAnimaleAnagVO.setIdCategoriaAnimaleLong(new Long(rs.getLong(1)));
        tipoCategoriaAnimaleAnagVO.setIdSpecieAnimaleLong(new Long(rs.getLong(2)));
        tipoCategoriaAnimaleAnagVO.setDescrizioneCategoriaAnimale(rs.getString(3));
        tipoCategoriaAnimaleAnagVO.setConsumoAnnuoUFLong(new Long(rs.getLong(4)));
        tipoCategoriaAnimaleAnagVO.setPesoVivoMedioDouble(new Double(rs.getDouble(5)));
        tipoCategoriaAnimaleAnagVO.setCoefficienteUBADouble(new Double(rs.getDouble(6)));
        tipoCategoriaAnimaleAnagVO.setLattazione(rs.getString(7));
      }
      
      rs.close();

      SolmrLogger.debug(this, "Executed query - Found record with primary key: " + idCategoriaAnimale);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTipoCategoriaAnimale - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getTipoCategoriaAnimale - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "getTipoCategoriaAnimale - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "getTipoCategoriaAnimale - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return tipoCategoriaAnimaleAnagVO;
  }

  /**
   * Restituisce un elenco di categorie corrispondenti alla specie specificata
   * 
   * @return TipoCategoriaAnimaleAnagVO
   * @throws DataAccessException
   * @throws NotFoundException
   * @param idSpecie
   *          Long
   */
  public Vector<TipoCategoriaAnimaleAnagVO> getTipiCategoriaAnimaleBySpecie(Long idSpecie) 
    throws DataAccessException, NotFoundException
  {
    TipoCategoriaAnimaleAnagVO tipoCategoriaAnimaleAnagVO = null;
    Vector<TipoCategoriaAnimaleAnagVO> results = new Vector<TipoCategoriaAnimaleAnagVO>();

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT ID_CATEGORIA_ANIMALE, " + "        ID_SPECIE_ANIMALE, " + "        DESCRIZIONE, " + "        CONSUMO_ANNUO_UF, " + "        PESO_VIVO_MEDIO, "
          + "        COEFFICIENTE_UBA, " + "        TO_CHAR(DATA_INIZIO_VALIDITA,'DD/MM/YYYY') AS DATA_INIZIO_VALIDITA," + "        TO_CHAR(DATA_FINE_VALIDITA,'DD/MM/YYYY') AS DATA_FINE_VALIDITA, "
          + "        PESO_VIVO_MIN, " + "        PESO_VIVO_MAX " + " FROM   DB_TIPO_CATEGORIA_ANIMALE " + " WHERE  ID_SPECIE_ANIMALE = ? " + " AND    DATA_FINE_VALIDITA IS NULL "
          + " ORDER BY DESCRIZIONE ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setLong(1, idSpecie.longValue());
      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        while (rs.next())
        {
          tipoCategoriaAnimaleAnagVO = new TipoCategoriaAnimaleAnagVO();
          tipoCategoriaAnimaleAnagVO.setIdCategoriaAnimaleLong(new Long(rs.getLong(1)));
          tipoCategoriaAnimaleAnagVO.setIdSpecieAnimaleLong(new Long(rs.getLong(2)));
          tipoCategoriaAnimaleAnagVO.setDescrizioneCategoriaAnimale(rs.getString(3));
          tipoCategoriaAnimaleAnagVO.setConsumoAnnuoUFLong(new Long(rs.getLong(4)));
          tipoCategoriaAnimaleAnagVO.setPesoVivoMedioDouble(new Double(rs.getDouble(5)));
          tipoCategoriaAnimaleAnagVO.setCoefficienteUBADouble(new Double(rs.getDouble(6)));
          tipoCategoriaAnimaleAnagVO.setDataInizioValidita(rs.getString("DATA_INIZIO_VALIDITA"));
          tipoCategoriaAnimaleAnagVO.setDataFineValidita(rs.getString("DATA_FINE_VALIDITA"));
          tipoCategoriaAnimaleAnagVO.setPesoVivoMin(rs.getString("PESO_VIVO_MIN"));
          tipoCategoriaAnimaleAnagVO.setPesoVivoMax(rs.getString("PESO_VIVO_MAX"));
          results.add(tipoCategoriaAnimaleAnagVO);

          // SolmrLogger.debug(this, tipoCategoriaAnimaleAnagVO.getDump());
        }
        rs.close();
      }
      else
        throw new DataAccessException();

      if (tipoCategoriaAnimaleAnagVO == null)
        throw new NotFoundException(SolmrErrors.EXC_NOT_FOUND_PK);

      SolmrLogger.debug(this, "Executed query - Found " + results.size() + " records");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTipiCategoriaAnimaleBySpecie - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "getTipiCategoriaAnimaleBySpecie - ResultSet null");
      throw daexc;
    }
    catch (NotFoundException nfexc)
    {
      SolmrLogger.fatal(this, "getTipiCategoriaAnimaleBySpecie - NotFoundException: " + nfexc.getMessage());
      throw nfexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getTipiCategoriaAnimaleBySpecie - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "getTipiCategoriaAnimaleBySpecie - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "getTipiCategoriaAnimaleBySpecie - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return results;
  }

  /**
   * Restituisce un vector contenente le CategorieAllevamento legate ad un dato
   * allevamento, indentificato da un idAllevamento
   * 
   * @param idAllevamento
   *          Long
   * @return Vector
   * @throws DataAccessException
   * @throws NotFoundException
   */
  public Vector<CategorieAllevamentoAnagVO> getCategorieAllevamento(Long idAllevamento) 
      throws DataAccessException
  {
    Vector<CategorieAllevamentoAnagVO> categorieAllevamento = new Vector<CategorieAllevamentoAnagVO>();
    CategorieAllevamentoAnagVO categorieAllevamentoVO = null;

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = 
        "SELECT ID_CATEGORIE_ALLEVAMENTO, " + 
        "       ID_CATEGORIA_ANIMALE, " +
        "       ID_ALLEVAMENTO, " + 
        "       QUANTITA, " +
        "       QUANTITA_PROPRIETA, " +
        "       PESO_VIVO_UNITARIO " + 
        "FROM   DB_CATEGORIE_ALLEVAMENTO " +
        "WHERE  ID_ALLEVAMENTO = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setLong(1, idAllevamento.longValue());
      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        while (rs.next())
        {
          categorieAllevamentoVO = new CategorieAllevamentoAnagVO();

          categorieAllevamentoVO.setIdCategorieAllevamentoLong(new Long(rs.getLong(1)));
          categorieAllevamentoVO.setIdCategoriaAnimaleLong(new Long(rs.getLong(2)));
          categorieAllevamentoVO.setIdAllevamentoLong(new Long(rs.getLong(3)));
          categorieAllevamentoVO.setQuantitaLong(new Long(rs.getLong(4)));
          categorieAllevamentoVO.setQuantitaProprietaLong(new Long(rs.getLong(5)));
          categorieAllevamentoVO.setPesoVivoUnitario(rs.getString(6));

          categorieAllevamentoVO.setTipoCategoriaAnimaleAnagVO(this.getTipoCategoriaAnimale(categorieAllevamentoVO.getIdCategoriaAnimaleLong()));

          // SolmrLogger.debug(this, categorieAllevamentoVO.getDump());
          categorieAllevamento.add(categorieAllevamentoVO);
        }
        rs.close();
      }
      else
        throw new DataAccessException();

      //if (categorieAllevamento == null)
        //throw new NotFoundException(SolmrErrors.EXC_NOT_FOUND_PK);

      SolmrLogger.debug(this, "Executed query - Found record with primary key: " + idAllevamento);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getCategorieAllevamento - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "getCategorieAllevamento - ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getCategorieAllevamento - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "getCategorieAllevamento - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "getCategorieAllevamento - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return categorieAllevamento;
  }

  /**
   * Restituisce il tipoASL corrispondente all'idASL
   * 
   * @param idASL
   *          Long
   * @return it.csi.solmr.dto.anag.TipoASLAnagVO
   * @throws DataAccessException
   * @throws NotFoundException
   */
  public TipoASLAnagVO getTipoASL(Long idASL) throws DataAccessException, NotFoundException
  {
    TipoASLAnagVO tipoASLVO = null;

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "select id_asl, " + " descrizione, " + " istat_comune, " + "EXT_ID_AMM_COMPETENZA " + " from db_tipo_asl " + " where id_asl = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setLong(1, idASL.longValue());
      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        while (rs.next())
        {
          tipoASLVO = new TipoASLAnagVO();
          tipoASLVO.setIdASLLong(new Long(rs.getLong(1)));
          tipoASLVO.setDescrizione(rs.getString(2));
          tipoASLVO.setIstatComune(rs.getString(3));

          try
          {
            tipoASLVO.setExtIdAmmCompetenza(new Long(rs.getString("EXT_ID_AMM_COMPETENZA")));
          }
          catch (Exception e)
          {
          }

          // SolmrLogger.debug(this, tipoASLVO.getDump());
        }
        rs.close();
      }
      else
        throw new DataAccessException();

      if (tipoASLVO == null)
        throw new NotFoundException(SolmrErrors.EXC_NOT_FOUND_PK);

      SolmrLogger.debug(this, "Executed query - Found record with primary key: " + idASL);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTipoASL - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "getTipoASL - ResultSet null");
      throw daexc;
    }
    catch (NotFoundException nfexc)
    {
      SolmrLogger.fatal(this, "getTipoASL - NotFoundException: " + nfexc.getMessage());
      throw nfexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getTipoASL - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "getTipoASL - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "getTipoASL - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return tipoASLVO;
  }

  /**
   * Restituisce un vettore con tutti i tipi ASL
   * 
   * @return Vector
   * @throws DataAccessException
   * @throws NotFoundException
   */
  public Vector<TipoASLAnagVO> getTipiASL() throws DataAccessException, NotFoundException
  {
    TipoASLAnagVO tipoASLVO = null;
    Vector<TipoASLAnagVO> results = new Vector<TipoASLAnagVO>();

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT ID_ASL," +
      // "DESCRIZIONE,"+
          "ISTAT_COMUNE, " + "EXT_ID_AMM_COMPETENZA " + "FROM DB_TIPO_ASL " + "WHERE DATA_FINE_VALIDITA IS NULL ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        tipoASLVO = new TipoASLAnagVO();
        tipoASLVO.setIdASLLong(new Long(rs.getLong("ID_ASL")));
        // tipoASLVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoASLVO.setIstatComune(rs.getString("ISTAT_COMUNE"));
        try
        {
          tipoASLVO.setExtIdAmmCompetenza(new Long(rs.getString("EXT_ID_AMM_COMPETENZA")));
        }
        catch (Exception e)
        {
        }

        results.add(tipoASLVO);
      }
      rs.close();

      if (tipoASLVO == null)
        throw new NotFoundException(SolmrErrors.EXC_NOT_FOUND_PK);

      SolmrLogger.debug(this, "Executed query - Found " + results.size() + " records");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTipiASL - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (NotFoundException nfexc)
    {
      SolmrLogger.fatal(this, "getTipiASL - NotFoundException: " + nfexc.getMessage());
      throw nfexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getTipiASL - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "getTipiASL - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "getTipiASL - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return results;
  }

  /**
   * restituisce un allevamentoVO contenente un vettore con tutte le
   * categorieAllevamento legate all'allevamento, il tipoSpecieVO di
   * quell'allevamento, e il vo dell'asl al quale l'allevamento fa riferimento.
   * 
   * @param idAllevamento
   *          Long
   * @return AllevamentoAnagVO
   * @throws DataAccessException
   * @throws NotFoundException
   */
  public AllevamentoAnagVO getAllevamento(Long idAllevamento) throws DataAccessException, NotFoundException
  {
    AllevamentoAnagVO allevamentoVO = null;

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = 
        "SELECT A.ID_ALLEVAMENTO, " +
        "       A.ID_UTE, " +
        "       A.ID_ASL, " +
        "       A.ISTAT_COMUNE, " +
        "       A.ID_SPECIE_ANIMALE, " +
        "       A.CODICE_AZIENDA_ZOOTECNICA, " +
        "       A.DATA_INIZIO, " +
        "       A.DATA_FINE, " +
        "       A.NOTE, " +
        "       A.DATA_AGGIORNAMENTO, " +
        "       A.ID_UTENTE_AGGIORNAMENTO, " +
        "       A.INDIRIZZO, " +
        "       A.CAP, " +
        "       A.TELEFONO, " +
        "       A.CODICE_FISCALE_PROPRIETARIO, " +
        "       TM.DESCRIZIONE AS DESC_MUNGITURA, " +
        "       A.DENOMINAZIONE_PROPRIETARIO, " +
        "       A.CODICE_FISCALE_DETENTORE, " +
        "       A.DENOMINAZIONE_DETENTORE, " +
        "       TO_CHAR(A.DATA_INIZIO_DETENZIONE,'DD/MM/YYYY') AS DATA_INIZIO_DETENZIONE, " +
        "       TO_CHAR(A.DATA_FINE_DETENZIONE,'DD/MM/YYYY') AS DATA_FINE_DETENZIONE, " +
        "       A.FLAG_SOCCIDA, " +
        "       A.ID_TIPO_PRODUZIONE, " +
        "       A.ID_ORIENTAMENTO_PRODUTTIVO, " +
        "       TOP.DESCRIZIONE AS DESC_ORIENTAMENTO_PRODUT, " +
        "       TTP.DESCRIZIONE AS DESC_TIPO_PRODUZIONE," +
        "       A.MEDIA_CAPI_LATTAZIONE, " +
        "       A.QUANTITA_ACQUA_LAVAGGIO, " +
        "       A.FLAG_ACQUE_EFFLUENTI, " +
        "       A.ID_MUNGITURA, " +
        "       A.DESCRIZIONE_ALTRI_TRATTAM, " +
        "       A.FLAG_DEIEZIONE_AVICOLI, " +
        "       A.SUPERFICIE_LETTIERA_PERMANENTE, " +
        "       A.ALTEZZA_LETTIERA_PERMANENTE, " + 
        "       TSA.ALT_LETTIERA_PERMANENTE_MIN, " +
        "       TSA.ALT_LETTIERA_PERMANENTE_MAX, " +
        "       A.DENOMINAZIONE_ALLEVAMENTO, " +
        "       A.LATITUDINE, " +
        "       A.LONGITUDINE, " +
        "       TO_CHAR(A.DATA_APERTURA_ALLEVAMENTO,'DD/MM/YYYY') AS DATA_APERTURA_ALLEVAMENTO, " +
        "       A.MOTIVO_SOCCIDA, " +
        "       A.ID_TIPO_PRODUZIONE_COSMAN, " +
        "       TPC.DESCRIZIONE AS DESC_TIPO_PROD_COSMAN, " +
        "       A.FLAG_ASSICURATO_COSMAN " +
        "FROM   DB_ALLEVAMENTO A," +
        "       DB_TIPO_ORIENTAMENTO_PRODUT TOP," +
        "       DB_TIPO_TIPO_PRODUZIONE TTP," +
        "       DB_TIPO_MUNGITURA TM," +
        "       DB_TIPO_SPECIE_ANIMALE TSA," +
        "       DB_TIPO_PRODUZIONE_COSMAN TPC " +
        "WHERE  A.ID_ALLEVAMENTO = ? " +
        "AND    A.ID_MUNGITURA = TM.ID_MUNGITURA(+) " +
        "AND    A.ID_ORIENTAMENTO_PRODUTTIVO = TOP.ID_ORIENTAMENTO_PRODUTTIVO(+) " +
        "AND    A.ID_TIPO_PRODUZIONE = TTP.ID_TIPO_PRODUZIONE (+) " +
        "AND    A.ID_SPECIE_ANIMALE = TSA.ID_SPECIE_ANIMALE " +
        "AND    A.ID_TIPO_PRODUZIONE_COSMAN = TPC.ID_TIPO_PRODUZIONE_COSMAN (+) ";

      
      
      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setLong(1, idAllevamento.longValue());
      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        while (rs.next())
        {
          allevamentoVO = new AllevamentoAnagVO();

          allevamentoVO.setIdAllevamentoLong(new Long(rs.getLong(1)));
          allevamentoVO.setIdUTELong(new Long(rs.getLong(2)));

          String temp = rs.getString(3);
          try
          {
            if (temp != null)
              allevamentoVO.setIdASLLong(new Long(temp));
          }
          catch (Exception e)
          {
          }

          allevamentoVO.setIstatComuneAllevamento(rs.getString(4));
          allevamentoVO.setIdSpecieAnimaleLong(new Long(rs.getLong(5)));
          allevamentoVO.setCodiceAziendaZootecnica(rs.getString(6));
          allevamentoVO.setDataInizioDate(rs.getDate(7));
          allevamentoVO.setDataFineDate(rs.getDate(8));
          allevamentoVO.setNote(rs.getString(9));
          allevamentoVO.setDataAggiornamentoDate(rs.getDate(10));
          allevamentoVO.setIdUtenteAggiornamentoLong(new Long(rs.getLong(11)));
          allevamentoVO.setIndirizzo(rs.getString(12));
          allevamentoVO.setCap(rs.getString(13));
          allevamentoVO.setTelefono(rs.getString(14));
          allevamentoVO.setCodiceFiscaleProprietario(rs.getString("CODICE_FISCALE_PROPRIETARIO"));
          allevamentoVO.setDenominazioneProprietario(rs.getString("DENOMINAZIONE_PROPRIETARIO"));
          allevamentoVO.setCodiceFiscaleDetentore(rs.getString("CODICE_FISCALE_DETENTORE"));
          allevamentoVO.setDenominazioneDetentore(rs.getString("DENOMINAZIONE_DETENTORE"));
          allevamentoVO.setDataInizioDetenzione(rs.getString("DATA_INIZIO_DETENZIONE"));
          allevamentoVO.setDataFineDetenzione(rs.getString("DATA_FINE_DETENZIONE"));
          if (SolmrConstants.FLAG_S.equals(rs.getString("FLAG_SOCCIDA")))
            allevamentoVO.setFlagSoccida(true);
          else
            allevamentoVO.setFlagSoccida(false);
          allevamentoVO.setIdTipoProduzione(rs.getString("ID_TIPO_PRODUZIONE"));
          allevamentoVO.setIdOrientamentoProduttivo(rs.getString("ID_ORIENTAMENTO_PRODUTTIVO"));
          allevamentoVO.setDescOrientamentoProduttivo(rs.getString("DESC_ORIENTAMENTO_PRODUT"));
          allevamentoVO.setDescTipoProduzione(rs.getString("DESC_TIPO_PRODUZIONE"));
          allevamentoVO.setMediaCapiLattazione(rs.getString("MEDIA_CAPI_LATTAZIONE"));
          allevamentoVO.setFlagAcqueEffluenti(rs.getString("FLAG_ACQUE_EFFLUENTI"));
          allevamentoVO.setIdMungitura(rs.getString("ID_MUNGITURA"));
          allevamentoVO.setDescMungitura(rs.getString("DESC_MUNGITURA"));
          allevamentoVO.setDescrizioneAltriTrattam(rs.getString("DESCRIZIONE_ALTRI_TRATTAM"));
          allevamentoVO.setQuantitaAcquaLavaggio(rs.getString("QUANTITA_ACQUA_LAVAGGIO"));
          allevamentoVO.setFlagDeiezioneAvicoli(rs.getString("FLAG_DEIEZIONE_AVICOLI"));
          allevamentoVO.setSuperficieLettieraPermanente(rs.getString("SUPERFICIE_LETTIERA_PERMANENTE"));
          allevamentoVO.setAltezzaLettieraPermanente(rs.getString("ALTEZZA_LETTIERA_PERMANENTE"));
          allevamentoVO.setAltLettieraPermMin(rs.getDouble("ALT_LETTIERA_PERMANENTE_MIN"));
          allevamentoVO.setAltLettieraPermMax(rs.getDouble("ALT_LETTIERA_PERMANENTE_MAX"));
          allevamentoVO.setDenominazione(rs.getString("DENOMINAZIONE_ALLEVAMENTO"));
          allevamentoVO.setDataInizioAttivita(rs.getString("DATA_APERTURA_ALLEVAMENTO"));
          allevamentoVO.setLatitudine(rs.getBigDecimal("LATITUDINE"));
          allevamentoVO.setLongitudine(rs.getBigDecimal("LONGITUDINE"));
          allevamentoVO.setMotivoSoccida(rs.getString("MOTIVO_SOCCIDA"));
          allevamentoVO.setIdTipoProduzioneCosman(rs.getString("ID_TIPO_PRODUZIONE_COSMAN"));
          allevamentoVO.setDescTipoProduzioneCosman(rs.getString("DESC_TIPO_PROD_COSMAN"));
          allevamentoVO.setFlagAssicuratoCosman(rs.getString("FLAG_ASSICURATO_COSMAN"));
          
          //SolmrLogger.debug(this, allevamentoVO.getDump());
        }
        rs.close();
      }
      else
        throw new DataAccessException();

      if (allevamentoVO == null)
        throw new NotFoundException(SolmrErrors.EXC_NOT_FOUND_PK);

      SolmrLogger.debug(this, "Executed query - Found record with primary key: " + idAllevamento);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getAllevamento - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "getAllevamento - ResultSet null");
      throw daexc;
    }
    catch (NotFoundException nfexc)
    {
      SolmrLogger.fatal(this, "getAllevamento - NotFoundException: " + nfexc.getMessage());
      throw nfexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getAllevamento - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "getAllevamento - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "getAllevamento - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return allevamentoVO;
  }

  /**
   * restituisce un vettore contenente tutti gli anni che si trovano su
   * DB_DICHIARAZIONE_CONSISTENZA per una determinata azienda (eccetto l'anno
   * corrente). Se non trova niente restituisce null
   * 
   * @param idAzienda
   *          Long
   * @return java.lang.Integer[]
   * @throws DataAccessException
   */
  public Integer[] getAnniByIdAzienda(Long idAzienda) throws DataAccessException
  {
    SolmrLogger.debug(this, "Entering AllevamentoAnagDAO : getAnniByIdAzienda : idAzienda = " + idAzienda);
    Vector<Integer> anni = null;

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT DISTINCT ANNO " + " FROM DB_DICHIARAZIONE_CONSISTENZA " + " WHERE ID_AZIENDA = ? " + " AND ANNO < ? " + " ORDER BY ANNO DESC";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setInt(2, DateUtils.getCurrentYear().intValue());
      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        anni = new Vector<Integer>();
        anni.add(new Integer((rs.getInt("ANNO"))));
        while (rs.next())
        {
          anni.add(new Integer((rs.getInt("ANNO"))));
        }
      }
      rs.close();

      SolmrLogger.debug(this, "Executed query - Found record with primary key: " + idAzienda);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getAnniByIdAzienda - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getAnniByIdAzienda - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "getAnniByIdAzienda - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "getAnniByIdAzienda - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    if (anni == null || anni.size() == 0)
    {
      return null;
    }
    else
    {
      return (Integer[]) anni.toArray(new Integer[1]);
    }
  }

  /**
   * restituisce un vettore contenente tutti gli allevamenti legati ad un
   * determinato UTE
   * 
   * @param idUTE
   *          Long
   * @param anno
   *          int
   * @return Vector
   * @throws DataAccessException
   * @throws NotFoundException
   */
  public Vector<AllevamentoAnagVO> getAllevamentiByIdUTE(Long idUTE, int anno) throws DataAccessException, NotFoundException
  {
    SolmrLogger.debug(this, "Entering AllevamentoAnagDAO : getAllevamentiByIdUTE : idUTE = " + idUTE);
    Vector<AllevamentoAnagVO> allevamenti = new Vector<AllevamentoAnagVO>();
    AllevamentoAnagVO allevamentoVO = null;

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "select A.id_allevamento, " + "       A.id_ute, " + "       A.id_asl, " + "       A.istat_comune, " + "       A.id_specie_animale, " + "       A.codice_azienda_zootecnica, "
          + "       A.data_inizio, " + "       A.data_fine, " + "       A.note, " + "       A.data_aggiornamento, " + "       A.id_utente_aggiornamento, " + "       C.DESCOM, "
          + "       TSA.DESCRIZIONE " + " from  db_allevamento A, " + "       COMUNE C, " + "       DB_TIPO_SPECIE_ANIMALE TSA " + " where A.id_ute = ? "
          + " and   A.data_inizio <= to_date(?,'dd/mm/yyyy') " + " and   (A.data_fine is null or A.data_fine >= to_date(?,'dd/mm/yyyy')) " + " AND   A.ISTAT_COMUNE = C.ISTAT_COMUNE "
          + " AND   TSA.ID_SPECIE_ANIMALE = A.ID_SPECIE_ANIMALE " + " order by C.DESCOM ASC, TSA.DESCRIZIONE ASC, A.data_inizio desc";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setLong(1, idUTE.longValue());
      stmt.setString(2, "31/12/" + (anno));
      stmt.setString(3, "01/01/" + anno);
      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        allevamentoVO = new AllevamentoAnagVO();

        allevamentoVO.setIdAllevamentoLong(new Long(rs.getLong(1)));
        allevamentoVO.setIdUTELong(new Long(rs.getLong(2)));
        String temp = rs.getString(3);
        try
        {
          if (temp != null)
            allevamentoVO.setIdASLLong(new Long(temp));
        }
        catch (Exception e)
        {
        }
        allevamentoVO.setIstatComuneAllevamento(rs.getString(4));
        allevamentoVO.setIdSpecieAnimaleLong(new Long(rs.getLong(5)));
        allevamentoVO.setCodiceAziendaZootecnica(rs.getString(6));
        allevamentoVO.setDataInizioDate(rs.getDate(7));
        allevamentoVO.setDataFineDate(rs.getDate(8));
        allevamentoVO.setNote(rs.getString(9));
        allevamentoVO.setDataAggiornamentoDate(rs.getDate(10));
        allevamentoVO.setIdUtenteAggiornamentoLong(new Long(rs.getLong(11)));

        //SolmrLogger.debug(this, allevamentoVO.getDump());
        allevamenti.add(allevamentoVO);
      }
      rs.close();


      SolmrLogger.debug(this, "Executed query - Found record with primary key: " + idUTE);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getAllevamentiByIdUTE - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getAllevamentiByIdUTE - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "getAllevamentiByIdUTE - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "getAllevamentiByIdUTE - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return allevamenti;
  }

  public AllevamentoAnagVO[] getAllevamentiByIdUTE(Long idUTE, Date dataAl) throws DataAccessException
  {
    SolmrLogger.debug(this, "Entering AllevamentoAnagDAO : getAllevamentiByIdUTE : idUTE = " + idUTE);
    SolmrLogger.debug(this, "Entering AllevamentoAnagDAO : getAllevamentiByIdUTE : dataAl = " + dataAl);
    Vector<AllevamentoAnagVO> allevamenti = new Vector<AllevamentoAnagVO>();
    AllevamentoAnagVO allevamentoVO = null;

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "select A.id_allevamento, " + "       A.id_ute, " + "       A.id_asl, " + "       A.istat_comune, " + "       A.id_specie_animale, " + "       A.codice_azienda_zootecnica, "
          + "       A.data_inizio, " + "       A.data_fine, " + "       A.note, " + "       A.data_aggiornamento, " + "       A.id_utente_aggiornamento " + " from  db_allevamento A "
          + " where A.id_ute = ? ";
      if (dataAl != null)
        query += " and   A.data_inizio <= ? and nvl(data_fine,to_date('31/12/9999','dd/mm/yyyy')) > ? ";
      else
        query += " and   A.data_inizio <= sysdate and nvl(data_fine,to_date('31/12/9999','dd/mm/yyyy')) > sysdate ";
      query += " order by A.data_inizio desc";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setLong(1, idUTE.longValue());
      if (dataAl != null)
      {
        stmt.setTimestamp(2, this.convertDateToTimestamp(dataAl));
        stmt.setTimestamp(3, this.convertDateToTimestamp(dataAl));
      }
      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        allevamentoVO = new AllevamentoAnagVO();

        allevamentoVO.setIdAllevamentoLong(new Long(rs.getLong(1)));
        allevamentoVO.setIdUTELong(new Long(rs.getLong(2)));
        String temp = rs.getString(3);
        try
        {
          if (temp != null)
            allevamentoVO.setIdASLLong(new Long(temp));
        }
        catch (Exception e)
        {
        }
        allevamentoVO.setIstatComuneAllevamento(rs.getString(4));
        allevamentoVO.setIdSpecieAnimaleLong(new Long(rs.getLong(5)));
        allevamentoVO.setCodiceAziendaZootecnica(rs.getString(6));
        allevamentoVO.setDataInizioDate(rs.getDate(7));
        allevamentoVO.setDataFineDate(rs.getDate(8));
        allevamentoVO.setNote(rs.getString(9));
        allevamentoVO.setDataAggiornamentoDate(rs.getDate(10));
        allevamentoVO.setIdUtenteAggiornamentoLong(new Long(rs.getLong(11)));

        //SolmrLogger.debug(this, allevamentoVO.getDump());
        allevamenti.add(allevamentoVO);
      }
      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getAllevamentiByIdUTE - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getAllevamentiByIdUTE - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "getAllevamentiByIdUTE - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "getAllevamentiByIdUTE - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return allevamenti.size() == 0 ? null : (AllevamentoAnagVO[]) allevamenti.toArray(new AllevamentoAnagVO[0]);
  }

  /**
   * Restituisce un vector contenente gli id di tutti gli UTE legati
   * all'idAzienda
   * 
   * @param idAzienda
   *          Long
   * @return Vector elencoIdUTE
   * @throws DataAccessException
   * @throws NotFoundException
   */
  public Vector<Long> getElencoIdUTEByIdAzienda(Long idAzienda) throws DataAccessException, NotFoundException
  {
    SolmrLogger.debug(this, "inside AllevamentoAnagDAO : getElencoIdUTEByIdAzienda : idAzienda = " + idAzienda);
    Vector<Long> elencoIdUTE = new Vector<Long>();

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "select id_UTE " + " from db_ute " + " where id_azienda = ? " + " and data_fine_attivita is null  ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setLong(1, idAzienda.longValue());
      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        while (rs.next())
        {
          Long idUTE = new Long(rs.getLong(1));
          elencoIdUTE.add(idUTE);
        }
        rs.close();
      }
      else
        throw new DataAccessException();

      SolmrLogger.debug(this, "Executed query - Found record with primary key: " + idAzienda);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getElencoIdUTEByIdAzienda - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "getElencoIdUTEByIdAzienda - ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getElencoIdUTEByIdAzienda - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "getElencoIdUTEByIdAzienda - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "getElencoIdUTEByIdAzienda - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoIdUTE;
  }

  /**
   * Inserisce un allevamento sul DB.
   * 
   */
  public Long insertAllevamento(AllevamentoAnagVO allevamentoVO, long idUtenteAggiornamento) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Long primaryKey = null;
    try
    {
      conn = getDatasource().getConnection();
      primaryKey = getNextPrimaryKey(((String) SolmrConstants.get("SEQ_ALLEVAMENTO")).trim());
      String insert = "insert into db_allevamento (id_allevamento, id_ute, id_asl, istat_comune, id_specie_animale, codice_azienda_zootecnica, "
          + "data_inizio, data_fine, note, data_aggiornamento, id_utente_aggiornamento, INDIRIZZO, CAP, TELEFONO, CODICE_FISCALE_PROPRIETARIO, DENOMINAZIONE_PROPRIETARIO, "
          + "CODICE_FISCALE_DETENTORE, DENOMINAZIONE_DETENTORE, DATA_INIZIO_DETENZIONE, DATA_FINE_DETENZIONE, FLAG_SOCCIDA, ID_TIPO_PRODUZIONE, ID_ORIENTAMENTO_PRODUTTIVO, "
          + "DESCRIZIONE_ALTRI_TRATTAM,FLAG_DEIEZIONE_AVICOLI, MEDIA_CAPI_LATTAZIONE, QUANTITA_ACQUA_LAVAGGIO,  FLAG_ACQUE_EFFLUENTI,  ID_MUNGITURA, SUPERFICIE_LETTIERA_PERMANENTE, "
          + "ALTEZZA_LETTIERA_PERMANENTE, DENOMINAZIONE_ALLEVAMENTO, LATITUDINE, LONGITUDINE, DATA_APERTURA_ALLEVAMENTO, MOTIVO_SOCCIDA, ID_TIPO_PRODUZIONE_COSMAN, "
          + "FLAG_ASSICURATO_COSMAN) "
          + "values (?, ?, ?, ?, ?, ?, sysdate, ?, ?, sysdate, ?, ?, ?, ?, ?, ? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

      SolmrLogger.debug(this, "\ninsertAllevamento=" + insert);

      // SolmrLogger.debug(this, "\n"+allevamentoVO.getDump());

      stmt = conn.prepareStatement(insert);
      int i = 0;
      SolmrLogger.debug(this, "" + i++);
      stmt.setLong(1, primaryKey.longValue());
      SolmrLogger.debug(this, "Value of parameter 2 [ID_UTE] in method insertAllevamento in AllevamentoAnagDAO: " + allevamentoVO.getIdUTELong() + "\n");
      stmt.setLong(2, allevamentoVO.getIdUTELong().longValue());
      if (Validator.isNotEmpty(allevamentoVO.getIdASLLong()))
      {
        stmt.setLong(3, allevamentoVO.getIdASLLong().longValue());
      }
      else
      {
        stmt.setString(3, null);
      }
      SolmrLogger.debug(this, "Value of parameter 4 [ISTAT_COMUNE] in method insertAllevamento in AllevamentoAnagDAO: " + allevamentoVO.getIstatComuneAllevamento() + "\n");
      stmt.setString(4, allevamentoVO.getIstatComuneAllevamento());
      SolmrLogger.debug(this, "Value of parameter 5 [ID_SPECIE_ANIMALE] in method insertAllevamento in AllevamentoAnagDAO: " + allevamentoVO.getIdSpecieAnimaleLong() + "\n");
      stmt.setLong(5, allevamentoVO.getIdSpecieAnimaleLong().longValue());
      stmt.setString(6, allevamentoVO.getCodiceAziendaZootecnica());
      /*
       * if(Validator.isNotEmpty(allevamentoVO.getDataInizioDate())) {
       * SolmrLogger.debug(this, "Value of parameter 7 [DATA_INIZIO] in method
       * insertAllevamento in AllevamentoAnagDAO:
       * "+convertDate(allevamentoVO.getDataInizioDate())+"\n"); stmt.setDate(7,
       * convertDate(allevamentoVO.getDataInizioDate())); } else {
       * SolmrLogger.debug(this, "Value of parameter 7 [DATA_INIZIO] in method
       * insertAllevamento in AllevamentoAnagDAO:
       * "+DateUtils.parseDate(DateUtils.getCurrent())+"\n");
       * stmt.setDate(7,convertDate(DateUtils.parseDate(DateUtils.getCurrent()))); }
       */
      if (Validator.isNotEmpty(allevamentoVO.getDataFineDate()))
      {
        SolmrLogger.debug(this, "Value of parameter 7 [DATA_FINE] in method insertAllevamento in AllevamentoAnagDAO: " + allevamentoVO.getDataFineDate() + "\n");
        stmt.setDate(7, convertDate(allevamentoVO.getDataFineDate()));
      }
      else
      {
        SolmrLogger.debug(this, "Value of parameter 7 [DATA_FINE] in method insertAllevamento in AllevamentoAnagDAO: null \n");
        stmt.setString(7, null);
      }
      stmt.setString(8, allevamentoVO.getNote());
      SolmrLogger.debug(this, "Value of parameter 9 [ID_UTENTE_AGGIORNAMENTO] in method insertAllevamento in AllevamentoAnagDAO: " + idUtenteAggiornamento + "\n");
      stmt.setLong(9, idUtenteAggiornamento);
      if (allevamentoVO.getIndirizzo() != null)
        allevamentoVO.setIndirizzo(allevamentoVO.getIndirizzo().toUpperCase());
      stmt.setString(10, allevamentoVO.getIndirizzo());
      stmt.setString(11, allevamentoVO.getCap());
      stmt.setString(12, allevamentoVO.getTelefono());
      if (allevamentoVO.getCodiceFiscaleProprietario() != null)
        allevamentoVO.setCodiceFiscaleProprietario(allevamentoVO.getCodiceFiscaleProprietario().toUpperCase());
      if (allevamentoVO.getDenominazioneProprietario() != null)
        allevamentoVO.setDenominazioneProprietario(allevamentoVO.getDenominazioneProprietario().toUpperCase());
      if (allevamentoVO.getCodiceFiscaleDetentore() != null)
        allevamentoVO.setCodiceFiscaleDetentore(allevamentoVO.getCodiceFiscaleDetentore().toUpperCase());
      if (allevamentoVO.getDenominazioneDetentore() != null)
        allevamentoVO.setDenominazioneDetentore(allevamentoVO.getDenominazioneDetentore().toUpperCase());
      stmt.setString(13, allevamentoVO.getCodiceFiscaleProprietario());
      stmt.setString(14, allevamentoVO.getDenominazioneProprietario());
      stmt.setString(15, allevamentoVO.getCodiceFiscaleDetentore());
      stmt.setString(16, allevamentoVO.getDenominazioneDetentore());
      if (Validator.isNotEmpty(allevamentoVO.getDataInizioDetenzione()))
        stmt.setDate(17, convertDate(DateUtils.parseDate(allevamentoVO.getDataInizioDetenzione())));
      else
        stmt.setString(17, null);
      if (Validator.isNotEmpty(allevamentoVO.getDataFineDetenzione()))
        stmt.setDate(18, convertDate(DateUtils.parseDate(allevamentoVO.getDataFineDetenzione())));
      else
        stmt.setString(18, null);
      if (allevamentoVO.isFlagSoccida())
        stmt.setString(19, SolmrConstants.FLAG_S);
      else
        stmt.setString(19, SolmrConstants.FLAG_N);

      SolmrLogger.debug(this, "Value of parameter 20 [ID_TIPO_PRODUZIONE] in method insertAllevamento in AllevamentoAnagDAO: " + allevamentoVO.getIdTipoProduzione() + "\n");

      try
      {
        stmt.setLong(20, Long.parseLong(allevamentoVO.getIdTipoProduzione()));
      }
      catch (Exception e)
      {
        stmt.setString(20, null);
      }

      SolmrLogger.debug(this, "Value of parameter 21 [ID_ORIENTAMENTO_PRODUTTIVO] in method insertAllevamento in AllevamentoAnagDAO: " + allevamentoVO.getIdOrientamentoProduttivo() + "\n");

      try
      {
        stmt.setLong(21, Long.parseLong(allevamentoVO.getIdOrientamentoProduttivo()));
      }
      catch (Exception e)
      {
        stmt.setString(21, null);
      }

      stmt.setString(22, allevamentoVO.getDescrizioneAltriTrattam()); // DESCRIZIONE_ALTRI_TRATTAM

      if (allevamentoVO.getFlagDeiezioneAvicoli() == null)
        stmt.setString(23, null); // FLAG_DEIEZIONE_AVICOLI
      else
        stmt.setString(23, allevamentoVO.getFlagDeiezioneAvicoli()); // FLAG_DEIEZIONE_AVICOLI

      if (allevamentoVO.isStrutturaMungitura())
      {
        // E' stata inserita una struttura di mungitura
        try
        {
          stmt.setLong(24, Long.parseLong(allevamentoVO.getMediaCapiLattazione())); // //MEDIA_CAPI_LATTAZIONE
        }
        catch (Exception e)
        {
          stmt.setString(24, null); // QUANTITA_ACQUA_LAVAGGIO
        }
        try
        {
          stmt.setDouble(25, Double.parseDouble(allevamentoVO.getQuantitaAcquaLavaggio().replace(',', '.'))); // QUANTITA_ACQUA_LAVAGGIO
        }
        catch (Exception e)
        {
          stmt.setString(25, null); // QUANTITA_ACQUA_LAVAGGIO
        }
        if (allevamentoVO.getFlagAcqueEffluenti() == null)
          stmt.setString(26, null); // FLAG_ACQUE_EFFLUENTI
        else
          stmt.setString(26, allevamentoVO.getFlagAcqueEffluenti()); // FLAG_ACQUE_EFFLUENTI
        try
        {
          stmt.setLong(27, Long.parseLong(allevamentoVO.getIdMungitura())); // ID_MUNGITURA
        }
        catch (Exception e)
        {
          stmt.setString(27, null); // ID_MUNGITURA
        }
      }
      else
      {
        stmt.setString(24, null); // MEDIA_CAPI_LATTAZIONE
        stmt.setString(25, null); // QUANTITA_ACQUA_LAVAGGIO
        stmt.setString(26, null); // FLAG_ACQUE_EFFLUENTI
        stmt.setString(27, null); // ID_MUNGITURA
      }
      if(Validator.isNotEmpty(allevamentoVO.getSuperficieLettieraPermanente()))
      {
        stmt.setDouble(28, Double.parseDouble(allevamentoVO.getSuperficieLettieraPermanente().replace(',', '.'))); // SUPERFICIE_LETTIERA_PERMANENTE
      }
      else
      {
        stmt.setString(28, null); // // SUPERFICIE_LETTIERA_PERMANENTE
      }
      if(Validator.isNotEmpty(allevamentoVO.getAltezzaLettieraPermanente()))
      {
        stmt.setDouble(29, Double.parseDouble(allevamentoVO.getAltezzaLettieraPermanente().replace(',', '.'))); // ALTEZZA_LETTIERA_PERMANENTE
      }
      else
      {
        stmt.setString(29, null); // // ALTEZZA_LETTIERA_PERMANENTE
      }
      
      stmt.setString(30, allevamentoVO.getDenominazione());
      stmt.setBigDecimal(31, allevamentoVO.getLatitudine());
      stmt.setBigDecimal(32, allevamentoVO.getLongitudine());
      
      if (Validator.isNotEmpty(allevamentoVO.getDataInizioAttivita()))
        stmt.setDate(33, convertDate(DateUtils.parseDate(allevamentoVO.getDataInizioAttivita())));
      else
        stmt.setString(33, null);
      
      stmt.setString(34, allevamentoVO.getMotivoSoccida());
      
      
      if(Validator.isNotEmpty(allevamentoVO.getIdTipoProduzioneCosman()))
      {
        stmt.setLong(35, Long.parseLong(allevamentoVO.getIdTipoProduzioneCosman()));
      }
      else
      {
        stmt.setString(35, null);
      }
      
      stmt.setString(36, allevamentoVO.getFlagAssicuratoCosman());

      SolmrLogger.debug(this, "Executing query: " + insert);

      stmt.executeUpdate();

      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "insertAllevamento - SQLException: " + exc.getMessage() + "\n");
      exc.printStackTrace();
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this, "insertAllevamento - Generic Exception: " + ex.getMessage() + "\n");
      ex.printStackTrace();
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
        SolmrLogger.error(this, "insertAllevamento - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.error(this, "insertAllevamento - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }

  public Long insertCategorieAllevamento(CategorieAllevamentoAnagVO categorieAllevamentoVO, Long idAllevamento) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Long primaryKey = null;
    try
    {
      conn = getDatasource().getConnection();
      primaryKey = getNextPrimaryKey(((String) SolmrConstants.get("SEQ_CATEGORIE_ALLEVAMENTO")).trim());
      String insert = 
        " INSERT INTO DB_CATEGORIE_ALLEVAMENTO " + 
        "             (ID_CATEGORIE_ALLEVAMENTO, " + 
        "              ID_CATEGORIA_ANIMALE, " + 
        "              ID_ALLEVAMENTO, " + 
        "              QUANTITA, " + 
        "              QUANTITA_PROPRIETA, " + 
        "              PESO_VIVO_UNITARIO) " + 
        " VALUES      (?, ?, ?, ?, ?, ?)";

      SolmrLogger.debug(this, "\n insertCategorieAllevamento=" + insert);

      stmt = conn.prepareStatement(insert);
      stmt.setLong(1, primaryKey.longValue());
      stmt.setLong(2, categorieAllevamentoVO.getIdCategoriaAnimaleLong().longValue());
      stmt.setLong(3, idAllevamento.longValue());
      stmt.setLong(4, categorieAllevamentoVO.getQuantitaLong().longValue());
      stmt.setLong(5, categorieAllevamentoVO.getQuantitaProprietaLong().longValue());
      try
      {
        stmt.setDouble(6, Double.parseDouble(categorieAllevamentoVO.getPesoVivoUnitario()));
      }
      catch (Exception e)
      {
        stmt.setString(6, null);
      }

      SolmrLogger.debug(this, "Executing query: " + insert);

      stmt.executeUpdate();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "insertCategorieAllevamento - SQLException: " + exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this, "insertCategorieAllevamento - Generic Exception: " + ex);
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
        SolmrLogger.error(this, "insertCategorieAllevamento - SQLException while closing Statement and Connection: " + exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.error(this, "insertCategorieAllevamento - Generic Exception while closing Statement and Connection: " + ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }

  public Long insertSottoCategorieAllevamento(SottoCategoriaAllevamento sottoCategorieAllevamentoVO, Long idCategorieAll) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Long primaryKey = null;
    try
    {
      conn = getDatasource().getConnection();
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_SOTTOCATEGORIA_ALLEVAMENTO);
      String insert = 
        " INSERT INTO DB_SOTTOCATEGORIA_ALLEVAMENTO ( " + 
        "ID_SOTTOCATEGORIA_ALLEVAMENTO, ORE_PASCOLO_INVERNO, ID_SOTTOCATEGORIA_ANIMALE, " +
        "ID_CATEGORIE_ALLEVAMENTO, QUANTITA, PESO_VIVO, " + 
        "GIORNI_VUOTO_SANITARIO, GIORNI_PASCOLO_ESTATE, ORE_PASCOLO_ESTATE, " +
        "GIORNI_PASCOLO_INVERNO, CICLI, NUMERO_CICLI_ANNUALI, QUANTITA_PROPRIETA) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

      SolmrLogger.debug(this, "\n insertCategorieAllevamento=" + insert);

      stmt = conn.prepareStatement(insert);
      int indice = 0;
      stmt.setLong(++indice, primaryKey.longValue()); // ID_SOTTOCATEGORIA_ALLEVAMENTO
      ++indice;
      try
      {
        stmt.setLong(indice, Long.parseLong(sottoCategorieAllevamentoVO.getOrePascoloInverno())); // ORE_PASCOLO_INVERNO
      }
      catch (Exception e)
      {
        stmt.setString(indice, null); // ORE_PASCOLO_INVERNO
      }
      stmt.setLong(++indice, sottoCategorieAllevamentoVO.getIdSottoCategoriaAnimale()); // ID_SOTTOCATEGORIA_ANIMALE
      stmt.setLong(++indice, idCategorieAll.longValue()); // ID_CATEGORIE_ALLEVAMENTO

      ++indice;
      try
      {
        stmt.setLong(indice, Long.parseLong(sottoCategorieAllevamentoVO.getQuantita())); // QUANTITA
      }
      catch (Exception e)
      {
        stmt.setString(indice, null); // QUANTITA
      }

      ++indice;
      try
      {
        stmt.setDouble(indice, Double.parseDouble(sottoCategorieAllevamentoVO.getPesoVivo().replace(',', '.'))); // PESO_VIVO
      }
      catch (Exception e)
      {
        stmt.setString(indice, null); // PESO_VIVO
      }

      stmt.setString(++indice, null); // GIORNI_VUOTO_SANITARIO

      ++indice;
      try
      {
        stmt.setLong(indice, Long.parseLong(sottoCategorieAllevamentoVO.getGiorniPascoloEstate())); // GIORNI_PASCOLO_ESTATE
      }
      catch (Exception e)
      {
        stmt.setString(indice, null); // GIORNI_PASCOLO_ESTATE
      }

      ++indice;
      try
      {
        stmt.setLong(indice, Long.parseLong(sottoCategorieAllevamentoVO.getOrePascoloEstate())); // ORE_PASCOLO_ESTATE
      }
      catch (Exception e)
      {
        stmt.setString(indice, null); // ORE_PASCOLO_ESTATE
      }

      ++indice;
      try
      {
        stmt.setLong(indice, Long.parseLong(sottoCategorieAllevamentoVO.getGiorniPascoloInverno())); // GIORNI_PASCOLO_INVERNO
      }
      catch (Exception e)
      {
        stmt.setString(indice, null); // GIORNI_PASCOLO_INVERNO
      }

      
      ++indice;
      try
      {
        stmt.setLong(indice, Long.parseLong(sottoCategorieAllevamentoVO.getCicli())); // CICLI
      }
      catch (Exception e)
      {
        stmt.setString(indice, null); // CICLI
      }
      
      ++indice;
      try
      {
        stmt.setLong(indice, Long.parseLong(sottoCategorieAllevamentoVO.getNumeroCicliAnnuali())); // NUMERO_CICLI_ANNUALI
      }
      catch (Exception e)
      {
        stmt.setString(indice, null); // NUMERO_CICLI_ANNUALI
      }
      
      ++indice;
      try
      {
        stmt.setLong(indice, Long.parseLong(sottoCategorieAllevamentoVO.getQuantitaProprieta())); // QUANTITA_PROPRIETA
      }
      catch (Exception e)
      {
        stmt.setString(indice, null); // QUANTITA_PROPRIETA
      }

      SolmrLogger.debug(this, "Executing query: " + insert);

      stmt.executeUpdate();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "insertSottoCategorieAllevamento - SQLException: " + exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this, "insertSottoCategorieAllevamento - Generic Exception: " + ex);
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
        SolmrLogger.error(this, "insertSottoCategorieAllevamento - SQLException while closing Statement and Connection: " + exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.error(this, "insertSottoCategorieAllevamento - Generic Exception while closing Statement and Connection: " + ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }
  
  
  //Inserisce una stabulazione trattamento
  public Long insertStabulazioneTrattamento(StabulazioneTrattamento stab,Long idSottocategoriaAllevamento) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Long primaryKey = null;
    try
    {
      conn = getDatasource().getConnection();
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_STABULAZIONE_TRATTAMENTO);
      String insert = " INSERT INTO DB_STABULAZIONE_TRATTAMENTO (ID_STABULAZIONE_TRATTAMENTO, ID_SOTTOCATEGORIA_ALLEVAMENTO, "+
      "ID_STABULAZIONE, QUANTITA_STABULATI, ID_TRATTAMENTO) VALUES ( ?, ?, ?, ?, ?)";

      SolmrLogger.debug(this, "Executing query insertStabulazioneTrattamento: " + insert);

      stmt = conn.prepareStatement(insert);
      int indice = 0;
      stmt.setLong(++indice, primaryKey.longValue()); // ID_STABULAZIONE_TRATTAMENTO
      stmt.setLong(++indice, idSottocategoriaAllevamento.longValue()); // ID_SOTTOCATEGORIA_ALLEVAMENTO
      stmt.setLong(++indice, Long.parseLong(stab.getIdStabulazione())); // ID_STABULAZIONE
      stmt.setLong(++indice, Long.parseLong(stab.getQuantita())); // QUANTITA_STABULATI
      try
      {
        stmt.setLong(++indice, Long.parseLong(stab.getIdTrattamento())); // ID_TRATTAMENTO
      }
      catch (Exception e)
      {
        stmt.setString(indice, null); // ID_TRATTAMENTO
      }

      SolmrLogger.debug(this, "Executed query insertStabulazioneTrattamento");

      stmt.executeUpdate();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "insertStabulazioneTrattamento - SQLException: " + exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this, "insertStabulazioneTrattamento - Generic Exception: " + ex);
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
        SolmrLogger.error(this, "insertStabulazioneTrattamento - SQLException while closing Statement and Connection: " + exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.error(this, "insertStabulazioneTrattamento - Generic Exception while closing Statement and Connection: " + ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }
  
  
  //Inserisce un effluente
  public void insertEffluenteProdotto(StabulazioneTrattamento stab,Long idStabulazioneTrattamento, boolean palabile, String flagTrattamento,
      double volumeProdotto, double azotoProdotto, double volumeProdottoAziendale, double azotoProdottoAziendale, boolean trattamento) 
    throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Long primaryKey = null;
    try
    {     
      //inserire un record sulla tavola db_effluente_prodotto se almeno uno dei relativi campi calcolati  
      //volume_prodotto,
      //volume_prodotto_aziendale,  
      //azoto_prodotto,
      //azoto_prodotto_aziendale, 
      //è diverso da 0 allora      
      if (volumeProdotto==0 && azotoProdotto==0 && volumeProdottoAziendale==0 && azotoProdottoAziendale==0) 
        return;
      
      conn = getDatasource().getConnection();
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_EFFLUENTE_PRODOTTO);
      String insert = " INSERT INTO DB_EFFLUENTE_PRODOTTO ( ID_EFFLUENTE_PRODOTTO, ID_STABULAZIONE_TRATTAMENTO, ID_EFFLUENTE, "+
      "FLAG_TRATTAMENTO, VOLUME_PRODOTTO, AZOTO_PRODOTTO,    VOLUME_PRODOTTO_AZIENDALE, AZOTO_PRODOTTO_AZIENDALE) "+
      "VALUES ( ?, ?,";
      
      //id_effluente  Se non è selezionato il trattamento:lo stesso precedente 
      //Se è stato indicato il trattamento:Accedere alla tavola db_legame_effluente per chiave id_effluente precedente 
      //(Vedi Tab. A) quindi considerare il valore del campo id_effluente_trattato.
      if (!trattamento)
        insert +=" ?, ?, ?, ?, ?, ?)";
      else insert += " (SELECT ID_EFFLUENTE_TRATTATO FROM DB_LEGAME_EFFLUENTE WHERE ID_EFFLUENTE=?), ?, ?, ?, ?, ?)";
      
      SolmrLogger.debug(this, "Executing query insertEffluenteProdotto: " + insert);

      stmt = conn.prepareStatement(insert);
      int indice = 0;
      stmt.setLong(++indice, primaryKey.longValue()); // ID_EFFLUENTE_PRODOTTO
      stmt.setLong(++indice, idStabulazioneTrattamento.longValue()); // ID_STABULAZIONE_TRATTAMENTO
      
      if (palabile)
      {
        stmt.setLong(++indice, stab.getStabPal().getIdEffluente()); // ID_STABULAZIONE_TRATTAMENTO
      }
      else
      {
        stmt.setLong(++indice, stab.getStabNonPal().getIdEffluente()); // ID_EFFLUENTE
      }
      
      stmt.setString(++indice, flagTrattamento); // FLAG_TRATTAMENTO
      
      stmt.setDouble(++indice, volumeProdotto); // VOLUME_PRODOTTO
      stmt.setDouble(++indice, azotoProdotto); // AZOTO_PRODOTTO
      stmt.setDouble(++indice, volumeProdottoAziendale); // VOLUME_PRODOTTO_AZIENDALE
      stmt.setDouble(++indice, azotoProdottoAziendale); // AZOTO_PRODOTTO_AZIENDALE
          
      SolmrLogger.debug(this, "Executed query insertEffluenteProdotto");

      stmt.executeUpdate();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "insertEffluenteProdotto - SQLException: " + exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this, "insertEffluenteProdotto - Generic Exception: " + ex);
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
        SolmrLogger.error(this, "insertEffluenteProdotto - SQLException while closing Statement and Connection: " + exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.error(this, "insertEffluenteProdotto - Generic Exception while closing Statement and Connection: " + ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  private java.sql.Date convertDate(java.util.Date aDate)
  {
    if (aDate != null)
    {
      return new java.sql.Date(aDate.getTime());
    }
    return null;
  }

  /**
   * Elimina un allevamento
   * 
   * @param idAllevamento
   *          Long
   * @throws DataAccessException
   */
  public void deleteAllevamento(Long idAllevamento) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;

    SolmrLogger.debug(this, "idAllevamento: " + idAllevamento);

    try
    {
      conn = getDatasource().getConnection();

      String delete = " delete " + " from db_allevamento " + " where id_allevamento = ? ";

      stmt = conn.prepareStatement(delete);
      stmt.setBigDecimal(1, convertLongToBigDecimal(idAllevamento));

      SolmrLogger.debug(this, "Executing delete: " + delete);

      stmt.executeUpdate();

      SolmrLogger.debug(this, "Executed delete.");

      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "deleteAllevamento - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "deleteAllevamento - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "deleteAllevamento - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "deleteAllevamento - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  /**
   * Elimina tutte le categorieAllevamento legate ad un allevamento
   * 
   * @param idAllevamento
   *          Long
   * @throws DataAccessException
   */
  public void deleteCategorieAllevamento(Long idAllevamento) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;

    SolmrLogger.debug(this, "idAllevamento: " + idAllevamento);

    try
    {
      conn = getDatasource().getConnection();

      String delete = " delete " + " from db_categorie_allevamento " + " where id_allevamento = ? ";

      stmt = conn.prepareStatement(delete);
      stmt.setBigDecimal(1, convertLongToBigDecimal(idAllevamento));

      SolmrLogger.debug(this, "Executing delete: " + delete);

      stmt.executeUpdate();

      SolmrLogger.debug(this, "Executed delete.");

      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "deleteCategorieAllevamento - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "deleteCategorieAllevamento - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "deleteCategorieAllevamento - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "deleteCategorieAllevamento - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  /**
   * Elimina tutte le sottoCategorieAllevamento legate ad un allevamento
   * 
   * @param idAllevamento
   *          Long
   * @throws DataAccessException
   */
  public void deleteSottoCategorieAllevamento(Long idAllevamento) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;

    SolmrLogger.debug(this, "idAllevamento: " + idAllevamento);

    try
    {
      conn = getDatasource().getConnection();

      String delete = " DELETE DB_SOTTOCATEGORIA_ALLEVAMENTO SA WHERE SA.ID_CATEGORIE_ALLEVAMENTO IN " + "(SELECT ID_CATEGORIE_ALLEVAMENTO FROM DB_CATEGORIE_ALLEVAMENTO WHERE ID_ALLEVAMENTO = ?)";

      stmt = conn.prepareStatement(delete);
      stmt.setBigDecimal(1, convertLongToBigDecimal(idAllevamento));

      SolmrLogger.debug(this, "Executing delete: " + delete);

      stmt.executeUpdate();

      SolmrLogger.debug(this, "Executed delete.");

      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "deleteSottoCategorieAllevamento - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "deleteSottoCategorieAllevamento - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "deleteSottoCategorieAllevamento - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "deleteSottoCategorieAllevamento - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  /**
   * Modifica un allevamento
   * 
   */
  public void updateAllevamento(AllevamentoAnagVO all, long idUtenteAggiornamento) 
    throws DataAccessException
  {
    
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
      String debug = 
        "UPDATE DB_ALLEVAMENTO SET " + 
        "     ID_UTE = ?, " + 
        "     CODICE_AZIENDA_ZOOTECNICA = ?, " + 
        "     ID_ASL = ?, " +
        "     ISTAT_COMUNE = ?, " +
        "     NOTE = ?, " +
        "     DATA_AGGIORNAMENTO = SYSDATE, " + 
        "     ID_UTENTE_AGGIORNAMENTO = ?, " +
        "     INDIRIZZO = ?, " +
        "     CAP = ?, " +
        "     TELEFONO = ?, " +
        "     CODICE_FISCALE_PROPRIETARIO = ?, " +
        "     DENOMINAZIONE_PROPRIETARIO = ?, " + 
        "     CODICE_FISCALE_DETENTORE = ?, " +
        "     DENOMINAZIONE_DETENTORE = ?, " +
        "     DATA_INIZIO_DETENZIONE = ?, " +
        "     DATA_FINE_DETENZIONE = ?, " +
        "     FLAG_SOCCIDA = ?, " +
        "     ID_TIPO_PRODUZIONE = ?, " +
        "     ID_ORIENTAMENTO_PRODUTTIVO = ?, " +
        "     DESCRIZIONE_ALTRI_TRATTAM = ?, " +
        "     FLAG_DEIEZIONE_AVICOLI = ?, " +
        "     MEDIA_CAPI_LATTAZIONE = ?, " + 
        "     QUANTITA_ACQUA_LAVAGGIO = ?, " +
        "     FLAG_ACQUE_EFFLUENTI = ?, " +
        "     ID_MUNGITURA = ?, " +
        "     SUPERFICIE_LETTIERA_PERMANENTE = ?, " +
        "     ALTEZZA_LETTIERA_PERMANENTE = ?, " +
        "     DENOMINAZIONE_ALLEVAMENTO = ?, " +
        "     LATITUDINE = ?, " +
        "     LONGITUDINE = ?, " +
        "     DATA_APERTURA_ALLEVAMENTO = ?, " +
        "     MOTIVO_SOCCIDA = ? ," +
        "     ID_TIPO_PRODUZIONE_COSMAN = ?, " +
        "     FLAG_ASSICURATO_COSMAN = ? " +
        "WHERE ID_ALLEVAMENTO = ? ";

      SolmrLogger.debug(this, "updateAllevamento=" + debug);
      stmt = conn.prepareStatement(debug);
      stmt.setLong(1, all.getIdUTELong().longValue());
      stmt.setString(2, all.getCodiceAziendaZootecnica());
      if (Validator.isNotEmpty(all.getIdASLLong()))
      {
        stmt.setLong(3, all.getIdASLLong().longValue());
      }
      else
      {
        stmt.setString(3, null);
      }
      stmt.setString(4, all.getIstatComuneAllevamento());
      stmt.setString(5, all.getNote());
      stmt.setLong(6, idUtenteAggiornamento);
      if (all.getIndirizzo() != null)
        all.setIndirizzo(all.getIndirizzo().toUpperCase());
      stmt.setString(7, all.getIndirizzo());
      stmt.setString(8, all.getCap());
      stmt.setString(9, all.getTelefono());

      if (all.getCodiceFiscaleProprietario() != null)
        all.setCodiceFiscaleProprietario(all.getCodiceFiscaleProprietario().toUpperCase());
      if (all.getDenominazioneProprietario() != null)
        all.setDenominazioneProprietario(all.getDenominazioneProprietario().toUpperCase());
      if (all.getCodiceFiscaleDetentore() != null)
        all.setCodiceFiscaleDetentore(all.getCodiceFiscaleDetentore().toUpperCase());
      if (all.getDenominazioneDetentore() != null)
        all.setDenominazioneDetentore(all.getDenominazioneDetentore().toUpperCase());

      stmt.setString(10, all.getCodiceFiscaleProprietario());
      stmt.setString(11, all.getDenominazioneProprietario());
      stmt.setString(12, all.getCodiceFiscaleDetentore());
      stmt.setString(13, all.getDenominazioneDetentore());
      if (Validator.isNotEmpty(all.getDataInizioDetenzione()))
        stmt.setDate(14, convertDate(DateUtils.parseDate(all.getDataInizioDetenzione())));
      else
        stmt.setString(14, null);
      if (Validator.isNotEmpty(all.getDataFineDetenzione()))
        stmt.setDate(15, convertDate(DateUtils.parseDate(all.getDataFineDetenzione())));
      else
        stmt.setString(15, null);
      if (all.isFlagSoccida())
        stmt.setString(16, SolmrConstants.FLAG_S);
      else
        stmt.setString(16, SolmrConstants.FLAG_N);

      SolmrLogger.debug(this, "Value of parameter 17 [ID_TIPO_PRODUZIONE] in method insertAllevamento in AllevamentoAnagDAO: " + all.getIdTipoProduzione() + "\n");

      try
      {
        stmt.setLong(17, Long.parseLong(all.getIdTipoProduzione()));
      }
      catch (Exception e)
      {
        stmt.setString(17, null);
      }

      SolmrLogger.debug(this, "Value of parameter 18 [ID_ORIENTAMENTO_PRODUTTIVO] in method insertAllevamento in AllevamentoAnagDAO: " + all.getIdOrientamentoProduttivo() + "\n");

      try
      {
        stmt.setLong(18, Long.parseLong(all.getIdOrientamentoProduttivo()));
      }
      catch (Exception e)
      {
        stmt.setString(18, null);
      }
      
      stmt.setString(19, all.getDescrizioneAltriTrattam()); // DESCRIZIONE_ALTRI_TRATTAM

      if (all.getFlagDeiezioneAvicoli() == null)
        stmt.setString(20, null); // FLAG_DEIEZIONE_AVICOLI
      else
        stmt.setString(20, all.getFlagDeiezioneAvicoli()); // FLAG_DEIEZIONE_AVICOLI

      if (all.isStrutturaMungitura())
      {
        // E' stata inserita una struttura di mungitura
        try
        {
          stmt.setLong(21, Long.parseLong(all.getMediaCapiLattazione())); //MEDIA_CAPI_LATTAZIONE
        }
        catch (Exception e)
        {
          stmt.setString(21, null); //MEDIA_CAPI_LATTAZIONE
        }
        try
        {
          stmt.setDouble(22, Double.parseDouble(all.getQuantitaAcquaLavaggio().replace(',', '.'))); // QUANTITA_ACQUA_LAVAGGIO
        }
        catch (Exception e)
        {
          stmt.setString(22, null); // QUANTITA_ACQUA_LAVAGGIO
        }
        if (all.getFlagAcqueEffluenti() == null)
          stmt.setString(23, null); // FLAG_ACQUE_EFFLUENTI
        else
          stmt.setString(23, all.getFlagAcqueEffluenti()); // FLAG_ACQUE_EFFLUENTI
        try
        {
          stmt.setLong(24, Long.parseLong(all.getIdMungitura())); // ID_MUNGITURA
        }
        catch (Exception e)
        {
          stmt.setString(24, null); // ID_MUNGITURA
        }
      }
      else
      {
        stmt.setString(21, null); // MEDIA_CAPI_LATTAZIONE
        stmt.setString(22, null); // QUANTITA_ACQUA_LAVAGGIO
        stmt.setString(23, null); // FLAG_ACQUE_EFFLUENTI
        stmt.setString(24, null); // ID_MUNGITURA
      }
      try
      {
        stmt.setDouble(25, Double.parseDouble(all.getSuperficieLettieraPermanente().replace(',', '.'))); // SUPERFICIE_LETTIERA_PERMANENTE
      }
      catch (Exception e)
      {
        stmt.setString(25, null); // // SUPERFICIE_LETTIERA_PERMANENTE
      }
      
      try
      {
        stmt.setDouble(26, Double.parseDouble(all.getAltezzaLettieraPermanente().replace(',', '.'))); // ALTEZZA_LETTIERA_PERMANENTE
      }
      catch (Exception e)
      {
        stmt.setString(26, null); // // ALTEZZA_LETTIERA_PERMANENTE
      }
      
      stmt.setString(27, all.getDenominazione());
      stmt.setBigDecimal(28, all.getLatitudine());
      stmt.setBigDecimal(29, all.getLongitudine());
      if(Validator.isNotEmpty(all.getDataInizioAttivita()))
      {
        stmt.setTimestamp(30, convertDateToTimestamp(DateUtils.parseDate(all.getDataInizioAttivita())));
      }
      else
      {
        stmt.setTimestamp(30, null);
      }
      
      stmt.setString(31, all.getMotivoSoccida());
      
      if(Validator.isNotEmpty(all.getIdTipoProduzioneCosman()))
      {
        stmt.setLong(32, Long.parseLong(all.getIdTipoProduzioneCosman()));
      }
      else
      {
        stmt.setString(32, null);
      }
      
      stmt.setString(33, all.getFlagAssicuratoCosman());
      stmt.setLong(34, all.getIdAllevamentoLong().longValue());

      SolmrLogger.debug(this, "Executing query: " + debug);

      stmt.executeUpdate();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "updateAllevamento - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "updateAllevamento - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "updateAllevamento - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "updateAllevamento - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  /**
   * controlla se l'allevamento indicato è storicizzato (data_fine valorizzata)
   * 
   * @param idAllevamento
   *          Long
   * @return boolean
   * @throws DataAccessException
   * @throws NotFoundException
   */
  public boolean isAllevamentoStoricizzato(Long idAllevamento) throws DataAccessException, NotFoundException
  {
    boolean result = false;

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "select data_fine " + " from db_allevamento " + " where id_allevamento = ?";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);
      SolmrLogger.debug(this, "idAllevamento = " + idAllevamento);

      stmt.setLong(1, idAllevamento.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        while (rs.next())
        {

          if (rs.getDate(1) != null)
            result = true;
        }
        rs.close();
      }
      else
        throw new DataAccessException();

      SolmrLogger.debug(this, "Executed query - Found records");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "isAllevamentoStoricizzato - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "isAllevamentoStoricizzato - ResultSet null");
      throw daexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "isAllevamentoStoricizzato - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "getTipiASL - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "getTipiASL - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  /**
   * controlla che la data passata sia sia minore della data dell'ultima
   * dichiarazione di consistenza dell'azienda
   * 
   * @param idAllevamento
   *          Long
   * @param dataInizio
   *          Date
   * @return boolean
   * @throws DataAccessException
   */
  public boolean isAllevamentoDichiarato(Long idAllevamento, Date dataInizio) throws DataAccessException
  {
    boolean result = false;

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT A.DATA_INIZIO,U.ID_AZIENDA " + "FROM DB_ALLEVAMENTO A,DB_UTE U " + "WHERE A.ID_ALLEVAMENTO = ? " + "AND A.ID_UTE=U.ID_UTE ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);
      SolmrLogger.debug(this, "idAllevamento = " + idAllevamento);

      stmt.setLong(1, idAllevamento.longValue());

      ResultSet rs = stmt.executeQuery();

      Date dataInizioAll = null, dataDichiarazione = null;
      long idAzienda = 0;

      if (rs.next())
      {
        dataInizioAll = rs.getTimestamp("DATA_INIZIO");
        idAzienda = rs.getLong("ID_AZIENDA");
      }

      /**
       * In teoria non dovrebbe mai passare di qua perchè un record deve
       * trovarlo. Se non lo trova non ho un idazienda valido
       */
      else
        return false;

      rs.close();
      stmt.close();

      if (dataInizio != null)
        dataInizioAll = dataInizio;

      query = "SELECT MAX(C.DATA_INSERIMENTO_DICHIARAZIONE) AS DATA_DICHIARAZIONE " + "FROM DB_DICHIARAZIONE_CONSISTENZA C " + "WHERE C.ID_AZIENDA = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);
      SolmrLogger.debug(this, "idAllevamento = " + idAllevamento);
      SolmrLogger.debug(this, "idAzienda = " + idAzienda);
      SolmrLogger.debug(this, "dataInizioAll = " + dataInizioAll);

      stmt.setLong(1, idAzienda);

      rs = stmt.executeQuery();

      if (rs.next())
        dataDichiarazione = rs.getTimestamp("DATA_DICHIARAZIONE");

      if (dataDichiarazione == null)
        result = false;
      else
      {
        if (dataInizioAll.before(dataDichiarazione))
          result = true;
        else
          result = false;
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found records");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "isAllevamentoDichiarato - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "isAllevamentoDichiarato - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "getTipiASL - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "getTipiASL - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  /**
   * controlla che db_allevamento.data_inizio sia minore della data dell'ultima
   * dichiarazione di consistenza dell'azienda
   * 
   * @param idAllevamento
   *          Long
   * @return boolean
   * @throws DataAccessException
   */
  public boolean isAllevamentoDichiarato(Long idAllevamento) throws DataAccessException
  {
    return isAllevamentoDichiarato(idAllevamento, null);
  }

  /**
   * Inserisce un nuovo allevamento e imposta a SYSDATE la data fine validità
   * dell'allevamento corrente
   * 
   */
  public void storicizzaAllevamento(AllevamentoAnagVO all, long idUtenteAggiornamento) throws DataAccessException
  {
    // SolmrLogger.debug(this, "\n\n\n\n\n\n\n\n\n"+all.getDump()+"\n\n\n\n\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
      String debug = "update db_allevamento set " + "  data_fine = sysdate , " + "  data_aggiornamento = sysdate, " + "  id_utente_aggiornamento = ? " + "  where id_allevamento = ? ";

      SolmrLogger.debug(this, "\nstoricizzaAllevamento=" + debug);
      stmt = conn.prepareStatement(debug);
      stmt.setLong(1, idUtenteAggiornamento);
      stmt.setLong(2, all.getIdAllevamentoLong().longValue());

      SolmrLogger.debug(this, "Executing query: " + debug);

      stmt.executeUpdate();

      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "storicizzaAllevamento - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "storicizzaAllevamento - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "storicizzaAllevamento - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "storicizzaAllevamento - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo che mi restituisce un elenco di allevamenti secondo un ordine
  // stabilito
  // (Metodo introdotto per correggere baco di ordinamento non risolvibile con
  // la struttura creata
  // in partenza) Mauro Vocale 24/01/2005
  public Vector<AllevamentoAnagVO> getAllevamentiByIdAziendaOrdinati(Long idAzienda, int anno) throws DataAccessException, NotFoundException
  {

    Vector<AllevamentoAnagVO> allevamenti = new Vector<AllevamentoAnagVO>();
    AllevamentoAnagVO allevamentoVO = null;

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "select    A.id_allevamento, " + "          A.id_ute, " + "          A.id_asl, " + "          A.istat_comune, " + "          A.id_specie_animale, "
          + "          A.codice_azienda_zootecnica, " + "          A.data_inizio, " + "          A.data_fine, " + "          A.note, " + "          A.data_aggiornamento, "
          + "          A.id_utente_aggiornamento, " + "          C.DESCOM, " + "          TSA.DESCRIZIONE " + " from     db_allevamento A, " + "          COMUNE C, "
          + "          DB_TIPO_SPECIE_ANIMALE TSA, " + "          DB_UTE U " + " where    A.data_inizio <= to_date(?,'dd/mm/yyyy') "
          + " and      (A.data_fine is null or A.data_fine >= to_date(?,'dd/mm/yyyy')) " + " AND      A.ISTAT_COMUNE = C.ISTAT_COMUNE " + " AND      TSA.ID_SPECIE_ANIMALE = A.ID_SPECIE_ANIMALE "
          + " AND      U.ID_UTE = A.ID_UTE " + " AND      U.ID_AZIENDA = ? " + " ORDER BY C.DESCOM ASC, TSA.DESCRIZIONE ASC, A.data_inizio desc";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query getAllevamentiByIdUTEOrdinati: " + query);

      stmt.setString(1, "31/12/" + (anno));
      stmt.setString(2, "01/01/" + anno);
      stmt.setLong(3, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        allevamentoVO = new AllevamentoAnagVO();
        allevamentoVO.setIdAllevamentoLong(new Long(rs.getLong(1)));
        allevamentoVO.setIdUTELong(new Long(rs.getLong(2)));
        String temp = rs.getString(3);
        try
        {
          if (temp != null)
          {
            allevamentoVO.setIdASLLong(new Long(temp));
          }
        }
        catch (Exception e)
        {
        }
        allevamentoVO.setIstatComuneAllevamento(rs.getString(4));
        allevamentoVO.setIdSpecieAnimaleLong(new Long(rs.getLong(5)));
        allevamentoVO.setCodiceAziendaZootecnica(rs.getString(6));
        allevamentoVO.setDataInizioDate(rs.getDate(7));
        allevamentoVO.setDataFineDate(rs.getDate(8));
        allevamentoVO.setNote(rs.getString(9));
        allevamentoVO.setDataAggiornamentoDate(rs.getDate(10));
        allevamentoVO.setIdUtenteAggiornamentoLong(new Long(rs.getLong(11)));

        //SolmrLogger.debug(this, allevamentoVO.getDump());
        allevamenti.add(allevamentoVO);
      }
      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getAllevamentiByIdUTEOrdinati - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getAllevamentiByIdUTEOrdinati - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "getAllevamentiByIdUTEOrdinati - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "getAllevamentiByIdUTEOrdinati - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return allevamenti;
  }

  public AllevamentoAnagVO[] getAllevamentiByIdAziendaOrdinati(Long idAzienda, Date dataAl) throws DataAccessException
  {

    Vector<AllevamentoAnagVO> allevamenti = new Vector<AllevamentoAnagVO>();
    AllevamentoAnagVO allevamentoVO = null;

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "select    A.id_allevamento, " + "          A.id_ute, " + "          A.id_asl, " + "          A.istat_comune, " + "          A.id_specie_animale, "
          + "          A.codice_azienda_zootecnica, " + "          A.data_inizio, " + "          A.data_fine, " + "          A.note, " + "          A.data_aggiornamento, "
          + "          A.id_utente_aggiornamento " + " from     db_allevamento A, " + "          DB_UTE U " + " where    U.ID_AZIENDA = ? " + " AND      U.ID_UTE = A.ID_UTE ";

      if (dataAl != null)
        query += " and   A.data_inizio <= ? and   nvl(data_fine,to_date('31/12/9999','dd/mm/yyyy')) > ? ";
      else
        query += " and   A.data_inizio <= sysdate and nvl(data_fine,to_date('31/12/9999','dd/mm/yyyy')) > sysdate ";
      query += " order by A.data_inizio desc";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query getAllevamentiByIdUTEOrdinati: " + query);

      stmt.setLong(1, idAzienda.longValue());

      if (dataAl != null)
      {
        stmt.setTimestamp(2, this.convertDateToTimestamp(dataAl));
        stmt.setTimestamp(3, this.convertDateToTimestamp(dataAl));
      }

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        allevamentoVO = new AllevamentoAnagVO();
        allevamentoVO.setIdAllevamentoLong(new Long(rs.getLong(1)));
        allevamentoVO.setIdUTELong(new Long(rs.getLong(2)));
        String temp = rs.getString(3);
        try
        {
          if (temp != null)
          {
            allevamentoVO.setIdASLLong(new Long(temp));
          }
        }
        catch (Exception e)
        {
        }
        allevamentoVO.setIstatComuneAllevamento(rs.getString(4));
        allevamentoVO.setIdSpecieAnimaleLong(new Long(rs.getLong(5)));
        allevamentoVO.setCodiceAziendaZootecnica(rs.getString(6));
        allevamentoVO.setDataInizioDate(rs.getDate(7));
        allevamentoVO.setDataFineDate(rs.getDate(8));
        allevamentoVO.setNote(rs.getString(9));
        allevamentoVO.setDataAggiornamentoDate(rs.getDate(10));
        allevamentoVO.setIdUtenteAggiornamentoLong(new Long(rs.getLong(11)));

        //SolmrLogger.debug(this, allevamentoVO.getDump());
        allevamenti.add(allevamentoVO);
      }
      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getAllevamentiByIdUTEOrdinati - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getAllevamentiByIdUTEOrdinati - Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "getAllevamentiByIdUTEOrdinati - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "getAllevamentiByIdUTEOrdinati - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return allevamenti.size() == 0 ? null : (AllevamentoAnagVO[]) allevamenti.toArray(new AllevamentoAnagVO[0]);
  }

  /**
   * Metodo per verificare se il record allevamento che restituisce il SIAN è
   * già stato censito in anagrafe
   * 
   * @param anagAziendaVO
   *          AnagAziendaVO
   * @param sianAllevamentiVO
   *          SianAllevamentiVO
   * @return boolean
   * @throws DataAccessException
   */
  public boolean isRecordSianInAnagrafe(AnagAziendaVO anagAziendaVO, SianAllevamentiVO sianAllevamentiVO) throws DataAccessException
  {
    SolmrLogger.debug(this, "Invocating method isRecordSianInAnagrafe in AllevamentoAnagDAO");
    Connection conn = null;
    PreparedStatement stmt = null;
    boolean isRecordSianInAnagrafe = false;

    try
    {
      SolmrLogger.debug(this, "Creating db_connection in method isRecordSianInAnagrafe in AllevamentoAnagDAO");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db_connection in method isRecordSianInAnagrafe in AllevamentoAnagDAO with value: " + conn);

      SolmrLogger.debug(this, "Creating query in method isRecordSianInAnagrafe in AllevamentoAnagDAO");

      String query = " SELECT A.ID_ALLEVAMENTO " + " FROM   DB_ALLEVAMENTO A, " + "        DB_UTE U, " + "        (SELECT STS.ID_TIPO_SPECIE_ANIMALE TIPO_SPECIE "
          + "         FROM   DB_SIAN_TIPO_SPECIE STS " + "         WHERE  STS.CODICE_SPECIE = ?)  TIPO_SPECIE_ANIMALE " + " WHERE  U.ID_AZIENDA = ? " + " AND    U.ID_UTE = A.ID_UTE "
          + " AND    A.CODICE_AZIENDA_ZOOTECNICA = ? " + " AND    A.ID_SPECIE_ANIMALE =  TIPO_SPECIE_ANIMALE.TIPO_SPECIE " + " AND    A.DATA_FINE IS NULL ";

      SolmrLogger.debug(this, "Created query in method isRecordSianInAnagrafe in AllevamentoAnagDAO");

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Value of parameter 1 [SPE_CODICE] in method isRecordSianInAnagrafe in AllevamentoAnagDAO: " + sianAllevamentiVO.getSpeCodice());
      stmt.setString(1, sianAllevamentiVO.getSpeCodice());
      SolmrLogger.debug(this, "Value of parameter 2 [ID_AZIENDA] in method isRecordSianInAnagrafe in AllevamentoAnagDAO: " + anagAziendaVO.getIdAzienda().longValue());
      stmt.setLong(2, anagAziendaVO.getIdAzienda().longValue());
      SolmrLogger.debug(this, "Value of parameter 3 [AZIENDA_CODICE] in method isRecordSianInAnagrafe in AllevamentoAnagDAO: " + sianAllevamentiVO.getAziendaCodice());
      stmt.setString(3, sianAllevamentiVO.getAziendaCodice());

      SolmrLogger.debug(this, "Executing isRecordSianInAnagrafe: " + query);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        isRecordSianInAnagrafe = true;
      }

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "isRecordSianInAnagrafe - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this, "isRecordSianInAnagrafe - Generic Exception: " + ex);
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
        SolmrLogger.error(this, "isRecordSianInAnagrafe - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.error(this, "isRecordSianInAnagrafe - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated method isRecordSianInAnagrafe in FascicoloDAO");
    return isRecordSianInAnagrafe;
  }

  /**
   * Metodo per recuperare il tipo asl a partire dall'id_amm_competenza
   * 
   * @param idAmmCompetenza
   *          Long
   * @param isActive
   *          boolean
   * @return TipoASLAnagVO
   * @throws DataAccessException
   * @throws SolmrException
   */
  public TipoASLAnagVO getTipoASLAnagVOByExtIdAmmCompetenza(Long idAmmCompetenza, boolean isActive) throws DataAccessException, SolmrException
  {
    SolmrLogger.debug(this, "Invocating method getTipoASLAnagVOByExtIdAmmCompetenza in AllevamentoAnagDAO");
    Connection conn = null;
    PreparedStatement stmt = null;
    TipoASLAnagVO tipoASLAnagVO = null;

    try
    {
      SolmrLogger.debug(this, "Creating db_connection in method getTipoASLAnagVOByExtIdAmmCompetenza in AllevamentoAnagDAO");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db_connection in method getTipoASLAnagVOByExtIdAmmCompetenza in AllevamentoAnagDAO with value: " + conn);

      SolmrLogger.debug(this, "Creating query in method getTipoASLAnagVOByExtIdAmmCompetenza in AllevamentoAnagDAO");

      String query = " SELECT TA.ID_ASL, " + "        TA.EXT_ID_AMM_COMPETENZA, " + "        TA.DATA_INIZIO_VALIDITA, " + "        TA.DATA_FINE_VALIDITA, " + "        TA.DESCRIZIONE, "
          + "        TA.ISTAT_COMUNE " + " FROM   DB_TIPO_ASL TA " + " WHERE  TA.EXT_ID_AMM_COMPETENZA = ? ";
      if (isActive)
      {
        query += " AND TA.DATA_FINE_VALIDITA IS NULL ";
      }

      SolmrLogger.debug(this, "Created query in method getTipoASLAnagVOByExtIdAmmCompetenza in AllevamentoAnagDAO");

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Value of parameter 1 [ID_AMM_COMPETENZA] in method getTipoASLAnagVOByExtIdAmmCompetenza in AllevamentoAnagDAO: " + idAmmCompetenza);
      stmt.setLong(1, idAmmCompetenza.longValue());
      SolmrLogger.debug(this, "Value of parameter 2 [IS_ACTIVE] in method getTipoASLAnagVOByExtIdAmmCompetenza in AllevamentoAnagDAO: " + isActive);

      SolmrLogger.debug(this, "Executing getTipoASLAnagVOByExtIdAmmCompetenza: " + query);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        tipoASLAnagVO = new TipoASLAnagVO();
        tipoASLAnagVO.setIdASL(rs.getString(1));
        tipoASLAnagVO.setIdASLLong(new Long(rs.getLong(1)));
        if (Validator.isNotEmpty(rs.getString(2)))
        {
          tipoASLAnagVO.setExtIdAmmCompetenza(new Long(rs.getLong(2)));
        }
        if (Validator.isNotEmpty(rs.getString(3)))
        {
          tipoASLAnagVO.setDataInizioValidita(rs.getDate(3));
        }
        if (Validator.isNotEmpty(rs.getString(4)))
        {
          tipoASLAnagVO.setDataFineValidita(rs.getDate(4));
        }
        tipoASLAnagVO.setDescrizione(rs.getString(5));
        tipoASLAnagVO.setIstatComune(rs.getString(6));
      }

      if (tipoASLAnagVO == null)
      {
        throw new SolmrException((String) AnagErrors.get("ERR_NO_ASL_FOUND_FOR_ID_AMM_COMPETENZA"));
      }

      rs.close();
      stmt.close();
      
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "getTipoASLAnagVOByExtIdAmmCompetenza - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (SolmrException se)
    {
      SolmrLogger.error(this, "getTipoASLAnagVOByExtIdAmmCompetenza - Generic Exception: " + se);
      throw new SolmrException(se.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this, "getTipoASLAnagVOByExtIdAmmCompetenza - Generic Exception: " + ex);
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
        SolmrLogger.error(this, "getTipoASLAnagVOByExtIdAmmCompetenza - SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.error(this, "getTipoASLAnagVOByExtIdAmmCompetenza - Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated method getTipoASLAnagVOByExtIdAmmCompetenza in FascicoloDAO");
    return tipoASLAnagVO;
  }

  /**
   * Metodo che mi permette di recuperare l'elenco degli allevamenti di
   * un'azienda agricola relativi ad un determinato piano di riferimento
   * 
   * @param idAzienda
   * @param idPianoRiferimento
   * @param orderBy
   * @return it.csi.solmr.dto.anag.AllevamentoAnagVO[]
   * @throws DataAccessException
   */
  public AllevamentoAnagVO[] getListAllevamentiAziendaByPianoRifererimento(Long idAzienda, Long idPianoRiferimento, Long idUte, String[] orderBy) throws DataAccessException
  {
    SolmrLogger.debug(this, "Invocating getListAllevamentiAziendaByPianoRifererimento method in AllevamentoAnagDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<AllevamentoAnagVO> elencoAllevamenti = new Vector<AllevamentoAnagVO>();

    try
    {
      SolmrLogger.debug(this, "Creating db-connection in getListAllevamentiAziendaByPianoRifererimento method in AllevamentoAnagDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListAllevamentiAziendaByPianoRifererimento method in AllevamentoAnagDAO and it values: " + conn + "\n");

      String query = "" +
        "SELECT A.ID_ALLEVAMENTO, " + 
      	"       A.ID_UTE, " + 
      	"       A.ID_ASL, " +
      	"       A.ISTAT_COMUNE AS ISTAT_COMUNE_ALL, ";
      
      if (idPianoRiferimento.intValue() > 0)
      {
        query += "" +
        "       (SELECT 'BIOLOGICO' " +
        "        FROM  DB_ALLEVAMENTO_BIO AB " +
        "        WHERE AB.ID_UTE = A.ID_UTE " +
        "        AND   AB.DATA_INIZIO_VALIDITA < DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "        AND   NVL(AB.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "        AND   NVL(AB.CODICE_AZIENDA_ZOOTECNICA,'A') = NVL(A.CODICE_AZIENDA_ZOOTECNICA, 'A') " +
        "        AND   AB.ID_SPECIE_ANIMALE = A.ID_SPECIE_ANIMALE " +
        "        AND   AB.ID_CATEGORIA_ANIMALE IN (" +
        "                                           SELECT CA.ID_CATEGORIA_ANIMALE " +
        "                                           FROM   DB_CATEGORIE_ALLEVAMENTO CA " +
        "                                           WHERE  CA.ID_ALLEVAMENTO = A.ID_ALLEVAMENTO " +
        "                                           ) " +
        "        AND ROWNUM = 1" +
        "       ) AS BIOLOGICO, ";
      }
      else
      {
        query += "" +
        "       (SELECT 'BIOLOGICO' " +
        "        FROM  DB_ALLEVAMENTO_BIO AB " +
        "        WHERE AB.ID_UTE = A.ID_UTE " +
        "        AND   AB.DATA_FINE_VALIDITA IS NULL " +
        "        AND   NVL(AB.CODICE_AZIENDA_ZOOTECNICA,'A') = NVL(A.CODICE_AZIENDA_ZOOTECNICA, 'A') " +
        "        AND   AB.ID_SPECIE_ANIMALE = A.ID_SPECIE_ANIMALE " +
        "        AND   AB.ID_CATEGORIA_ANIMALE IN (" +
        "                                           SELECT CA.ID_CATEGORIA_ANIMALE " +
        "                                           FROM   DB_CATEGORIE_ALLEVAMENTO CA " +
        "                                           WHERE  CA.ID_ALLEVAMENTO = A.ID_ALLEVAMENTO " +
        "                                           ) " +
        "        AND ROWNUM = 1" +
        "       ) AS BIOLOGICO, ";
      }
      		
      query += "" +
        "       C.DESCOM AS DESCOM_ALL, " + 
      	"       P.SIGLA_PROVINCIA AS SIGLA_PROVINCIA_ALL, " + 
      	"       A.ID_SPECIE_ANIMALE, " +
      	"       TSA.DESCRIZIONE AS DESC_SPECIE_ANIMALE, " +
      	"       TSA.UNITA_MISURA, " +
      	"       A.CODICE_AZIENDA_ZOOTECNICA, " +
      	"       A.DATA_INIZIO, " +
      	"       A.DATA_FINE, " +
      	"       A.NOTE, " +
      	"       A.DATA_AGGIORNAMENTO, " +
      	"       A.ID_UTENTE_AGGIORNAMENTO, " +
      	"       A.INDIRIZZO AS INDIRIZZO_ALL, " +
      	"       A.CAP, " +
      	"       A.TELEFONO, " +
      	"       A.DENOMINAZIONE_ALLEVAMENTO, " +
      	"       U.COMUNE AS ISTAT_COMUNE_UTE," +
      	"       U.INDIRIZZO AS INDIRIZZO_UTE," +
      	"       C_UTE.DESCOM AS DESCOM_UTE, " + 
        "       P_UTE.SIGLA_PROVINCIA AS SIGLA_PROVINCIA_UTE, " +
        "       A.FLAG_SOCCIDA, " +
        "       A.CODICE_FISCALE_PROPRIETARIO, " +
        "       A.DENOMINAZIONE_PROPRIETARIO, " +
        "       A.CODICE_FISCALE_DETENTORE, " +
        "       A.DENOMINAZIONE_DETENTORE," +
        "       (CASE " +
        "          WHEN EXISTS (SELECT ECA.ID_ESITO_CONTROLLO_ALLEVAMENTO " + 
        "                     FROM   DB_ESITO_CONTROLLO_ALLEVAMENTO ECA " +
        "                     WHERE  ECA.ID_ALLEVAMENTO = A.ID_ALLEVAMENTO  " +
        "                     AND    ECA.BLOCCANTE = 'S' ) " +
        "          THEN   'BLOCCANTE' " +
        "        END ) AS BLOCCANTE, " +
        "       (CASE " +
        "          WHEN EXISTS (SELECT ECA.ID_ESITO_CONTROLLO_ALLEVAMENTO " + 
        "                     FROM   DB_ESITO_CONTROLLO_ALLEVAMENTO ECA " +
        "                     WHERE  ECA.ID_ALLEVAMENTO = A.ID_ALLEVAMENTO  " +
        "                     AND    ECA.BLOCCANTE = 'N' ) " +
        "          THEN   'WARNING' " +
        "        END ) AS WARNING " +
      	"FROM   DB_ALLEVAMENTO A, " +
      	"       COMUNE C, " +
      	"       PROVINCIA P, " +
      	"       DB_TIPO_SPECIE_ANIMALE TSA, " +
      	"       DB_UTE U, " +
      	"       COMUNE C_UTE, " +
      	"       PROVINCIA P_UTE ";
      if (idPianoRiferimento.intValue() > 0)
      {
        query += ",DB_DICHIARAZIONE_CONSISTENZA DC ";
      }
      query += " " +
      	"WHERE  U.ID_AZIENDA = ? " +
      	"AND    U.ID_UTE = A.ID_UTE " +
      	"AND    A.ISTAT_COMUNE = C.ISTAT_COMUNE " +
      	"AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
      	"AND    U.COMUNE = C_UTE.ISTAT_COMUNE " +
        "AND    C_UTE.ISTAT_PROVINCIA = P_UTE.ISTAT_PROVINCIA " +
      	"AND    A.ID_SPECIE_ANIMALE = TSA.ID_SPECIE_ANIMALE ";
      if(Validator.isNotEmpty(idUte))
      {
        query += 
        "AND    A.ID_UTE = ? ";
      }

      if (idPianoRiferimento.intValue() > 0)
      {
        query += "" +
        "AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "AND    A.DATA_INIZIO < DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "AND    NVL(A.DATA_FINE, ?) > DC.DATA_INSERIMENTO_DICHIARAZIONE ";
      }
      else
      {
        query += " AND     A.DATA_FINE IS NULL ";
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
        ordinamento = "ORDER BY " + criterio;
      }
      if (!ordinamento.equals(""))
      {
        query += ordinamento;
      }

      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListAllevamentiAziendaByPianoRifererimento method in AllevamentoAnagDAO: " + idAzienda + "\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ID_PIANO_RIFERIMENTO] in getListAllevamentiAziendaByPianoRifererimento method in AllevamentoAnagDAO: " + idPianoRiferimento + "\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ID_UTE] in getListAllevamentiAziendaByPianoRifererimento method in AllevamentoAnagDAO: " + idUte + "\n");

      stmt = conn.prepareStatement(query);
      
      int indice = 0;      
      stmt.setLong(++indice, idAzienda.longValue());
      if(Validator.isNotEmpty(idUte))
      {
        stmt.setLong(++indice, idUte.longValue());
      }
      if (idPianoRiferimento.intValue() > 0)
      {
        stmt.setLong(++indice, idPianoRiferimento.longValue());
        stmt.setDate(++indice, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
      }

      SolmrLogger.debug(this, "Executing getListAllevamentiAziendaByPianoRifererimento: " + query + "\n");

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        AllevamentoAnagVO allevamentoAnagVO = new AllevamentoAnagVO();
        allevamentoAnagVO.setIdAllevamento(rs.getString("ID_ALLEVAMENTO"));
        allevamentoAnagVO.setIdAllevamentoLong(new Long(rs.getLong("ID_ALLEVAMENTO")));
        allevamentoAnagVO.setIdUTE(rs.getString("ID_UTE"));
        allevamentoAnagVO.setIdASL(rs.getString("ID_ASL"));
        allevamentoAnagVO.setIstatComuneAllevamento(rs.getString("ISTAT_COMUNE_ALL"));
        allevamentoAnagVO.setBiologico(rs.getString("BIOLOGICO"));
        ComuneVO comuneVO = new ComuneVO();
        ProvinciaVO provinciaVO = new ProvinciaVO();
        provinciaVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA_ALL"));
        comuneVO.setProvinciaVO(provinciaVO);
        comuneVO.setDescom(rs.getString("DESCOM_ALL"));
        comuneVO.setIstatComune(rs.getString("ISTAT_COMUNE_ALL"));
        allevamentoAnagVO.setComuneVO(comuneVO);
        allevamentoAnagVO.setIdSpecieAnimale(rs.getString("ID_SPECIE_ANIMALE"));
        TipoSpecieAnimaleAnagVO tipoSpecieAnimaleAnagVO = new TipoSpecieAnimaleAnagVO();
        tipoSpecieAnimaleAnagVO.setIdSpecieAnimale(rs.getString("ID_SPECIE_ANIMALE"));
        tipoSpecieAnimaleAnagVO.setDescrizione(rs.getString("DESC_SPECIE_ANIMALE"));
        tipoSpecieAnimaleAnagVO.setUnitaDiMisura(rs.getString("UNITA_MISURA"));
        allevamentoAnagVO.setTipoSpecieAnimaleAnagVO(tipoSpecieAnimaleAnagVO);
        allevamentoAnagVO.setCodiceAziendaZootecnica(rs.getString("CODICE_AZIENDA_ZOOTECNICA"));
        allevamentoAnagVO.setDataInizioDate(rs.getDate("DATA_INIZIO"));
        allevamentoAnagVO.setDataFineDate(rs.getDate("DATA_FINE"));
        allevamentoAnagVO.setNote(rs.getString("NOTE"));
        allevamentoAnagVO.setDataAggiornamento(rs.getString("DATA_AGGIORNAMENTO"));
        allevamentoAnagVO.setIdUtenteAggiornamento(rs.getString("ID_UTENTE_AGGIORNAMENTO"));
        allevamentoAnagVO.setIndirizzo(rs.getString("INDIRIZZO_ALL"));
        allevamentoAnagVO.setCap("CAP");
        allevamentoAnagVO.setTelefono("TELEFONO");
        allevamentoAnagVO.setDenominazione(rs.getString("DENOMINAZIONE_ALLEVAMENTO"));
        ComuneVO comuneUteVO = new ComuneVO();
        ProvinciaVO provinciaUteVO = new ProvinciaVO();
        provinciaUteVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA_UTE"));
        comuneUteVO.setProvinciaVO(provinciaUteVO);
        comuneUteVO.setDescom(rs.getString("DESCOM_UTE"));
        comuneUteVO.setIstatComune(rs.getString("ISTAT_COMUNE_UTE"));        
        allevamentoAnagVO.setComuneUteVO(comuneUteVO);
        allevamentoAnagVO.setIndirizzoUte(rs.getString("INDIRIZZO_UTE"));
        if (SolmrConstants.FLAG_S.equals(rs.getString("FLAG_SOCCIDA")))
          allevamentoAnagVO.setFlagSoccida(true);
        else
          allevamentoAnagVO.setFlagSoccida(false);
        allevamentoAnagVO.setCodiceFiscaleProprietario(rs.getString("CODICE_FISCALE_PROPRIETARIO"));
        allevamentoAnagVO.setDenominazioneProprietario(rs.getString("DENOMINAZIONE_PROPRIETARIO"));
        allevamentoAnagVO.setCodiceFiscaleDetentore(rs.getString("CODICE_FISCALE_DETENTORE"));
        allevamentoAnagVO.setDenominazioneDetentore(rs.getString("DENOMINAZIONE_DETENTORE"));
        
        if(rs.getString("BLOCCANTE") != null)
        {
          allevamentoAnagVO.setEsitoControllo("B");
        }
        else if(rs.getString("WARNING") != null) 
        {
          allevamentoAnagVO.setEsitoControllo("W");
        }
        else
        {
          allevamentoAnagVO.setEsitoControllo("P");
        }
        
        elencoAllevamenti.add(allevamentoAnagVO);
      }

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "getListAllevamentiAziendaByPianoRifererimento in AllevamentoAnagDAO - SQLException: " + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this, "getListAllevamentiAziendaByPianoRifererimento in AllevamentoAnagDAO - Generic Exception: " + ex + "\n");
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
        SolmrLogger.error(this, "getListAllevamentiAziendaByPianoRifererimento in AllevamentoAnagDAO - SQLException while closing Statement and Connection: " + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.error(this, "getListAllevamentiAziendaByPianoRifererimento in AllevamentoAnagDAO - Generic Exception while closing Statement and Connection: " + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListAllevamentiAziendaByPianoRifererimento method in AllevamentoAnagDAO\n");
    if (elencoAllevamenti.size() == 0)
    {
      return (AllevamentoAnagVO[]) elencoAllevamenti.toArray(new AllevamentoAnagVO[0]);
    }
    else
    {
      return (AllevamentoAnagVO[]) elencoAllevamenti.toArray(new AllevamentoAnagVO[elencoAllevamenti.size()]);
    }
  }

  /**
   * Metodo che verifica se esiste uno o più allevamenti ripristinati da una
   * precedente dichiarazione di consistenza
   * 
   * @param idAzienda
   * @return boolean
   * @throws DataAccessException
   */
  public boolean isAllevamentoRipristinato(Long idAzienda) throws DataAccessException
  {
    SolmrLogger.debug(this, "Invocating isAllevamentoRipristinato method in AllevamentoAnagDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    boolean isRipristinato = false;

    try
    {
      SolmrLogger.debug(this, "Creating db-connection in isAllevamentoRipristinato method in AllevamentoAnagDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in isAllevamentoRipristinato method in AllevamentoAnagDAO and it values: " + conn + "\n");

      String query = " SELECT A.ID_ALLEVAMENTO " + " FROM   DB_ALLEVAMENTO A, " + "        DB_UTE U " + " WHERE  U.ID_AZIENDA = ? " + " AND    U.ID_UTE = A.ID_UTE " + " AND    A.DATA_FINE IS NULL "
          + " AND    A.DICHIARAZIONE_RIPRISTINATA = ? ";

      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in isAllevamentoRipristinato method in AllevamentoAnagDAO: " + idAzienda + "\n");
      SolmrLogger.debug(this, "Value of parameter 2 [DICHIARAZIONE_RIPRISTINATA] in isAllevamentoRipristinato method in AllevamentoAnagDAO: " + SolmrConstants.FLAG_S + "\n");

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setString(2, SolmrConstants.FLAG_S);

      SolmrLogger.debug(this, "Executing isAllevamentoRipristinato: " + query + "\n");

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        if (Validator.isNotEmpty(rs.getString("ID_ALLEVAMENTO")))
        {
          isRipristinato = true;
          break;
        }
      }

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "isAllevamentoRipristinato in AllevamentoAnagDAO - SQLException: " + exc + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this, "isAllevamentoRipristinato in AllevamentoAnagDAO - Generic Exception: " + ex + "\n");
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
        SolmrLogger.error(this, "isAllevamentoRipristinato in AllevamentoAnagDAO - SQLException while closing Statement and Connection: " + exc + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.error(this, "isAllevamentoRipristinato in AllevamentoAnagDAO - Generic Exception while closing Statement and Connection: " + ex + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated isAllevamentoRipristinato method in AllevamentoAnagDAO\n");
    return isRipristinato;
  }

  public Vector<CodeDescription> getTipoTipoProduzione(long idSpecie) throws DataAccessException
  {
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = 
        "SELECT DISTINCT TTP.ID_TIPO_PRODUZIONE, " +
        "       TTP.CODICE, " +
        "       TTP.DESCRIZIONE " + 
        "FROM DB_TIPO_TIPO_PRODUZIONE TTP, " +
        "     DB_TIPO_SPECIE_ORIENTAM_PROD TSOP " + 
        "WHERE TSOP.ID_SPECIE_ANIMALE = ? " +
        "AND   TSOP.ID_TIPO_PRODUZIONE = TTP.ID_TIPO_PRODUZIONE " +
        "AND   TTP.DATA_FINE_VALIDITA IS NULL " + 
        "ORDER BY TTP.DESCRIZIONE ";

      stmt = conn.prepareStatement(search);
      stmt.setLong(1, idSpecie);
      SolmrLogger.debug(this, "getTipoTipoProduzione Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      result = new Vector<CodeDescription>();
      while (rs.next())
      {
        CodeDescription cd = new CodeDescription();

        cd.setCode(new Integer(rs.getInt("ID_TIPO_PRODUZIONE")));
        cd.setDescription(rs.getString("DESCRIZIONE"));
        cd.setSecondaryCode(rs.getString("CODICE"));
        result.add(cd);
      }

      rs.close();

      SolmrLogger.debug(this, "getTipoTipoProduzione Executed query - Found " + result.size() + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTipoTipoProduzione SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getTipoTipoProduzione Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "getTipoTipoProduzione SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "getTipoTipoProduzione Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  public Vector<CodeDescription> getOrientamentoProduttivo(long idSpecie, long idTipoProduzione) throws DataAccessException
  {
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = 
          " SELECT TOP.ID_ORIENTAMENTO_PRODUTTIVO," +
          "        TOP.CODICE,TOP.DESCRIZIONE " + 
          "FROM    DB_TIPO_ORIENTAMENTO_PRODUT TOP, " +
          "        DB_TIPO_SPECIE_ORIENTAM_PROD TSOP " + 
          "WHERE   TOP.ID_ORIENTAMENTO_PRODUTTIVO = TSOP.ID_ORIENTAMENTO_PRODUTTIVO " +
          "AND     DATA_FINE_VALIDITA IS NULL " +
          "AND     TSOP.ID_SPECIE_ANIMALE = ? " +
          "AND     TSOP.ID_TIPO_PRODUZIONE = ? ";

      stmt = conn.prepareStatement(search);
      SolmrLogger.debug(this, "getOrientamentoProduttivo Executing query: " + search);
      SolmrLogger.debug(this, "getOrientamentoProduttivo Parameter idSpecie: " + idSpecie);
      SolmrLogger.debug(this, "getOrientamentoProduttivo Parameter idTipoProduzione: " + idTipoProduzione);

      stmt.setLong(1, idSpecie);
      stmt.setLong(2, idTipoProduzione);

      ResultSet rs = stmt.executeQuery();

      result = new Vector<CodeDescription>();
      while (rs.next())
      {
        CodeDescription cd = new CodeDescription();

        cd.setCode(new Integer(rs.getInt("ID_ORIENTAMENTO_PRODUTTIVO")));
        cd.setDescription(rs.getString("DESCRIZIONE"));
        cd.setSecondaryCode(rs.getString("CODICE"));
        result.add(cd);
      }

      rs.close();

      SolmrLogger.debug(this, "getOrientamentoProduttivo Executed query - Found " + result.size() + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getOrientamentoProduttivo SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getOrientamentoProduttivo Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "getOrientamentoProduttivo SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "getOrientamentoProduttivo Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  
  public Vector<CodeDescription> getTipoProduzioneCosman(long idSpecie, 
      long idTipoProduzione, long idOrientamentoProduttivo) throws DataAccessException
  {
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = 
        "SELECT TPC.ID_TIPO_PRODUZIONE_COSMAN, " +
        "       TPC.CODICE_COSMAN, " +
        "       TPC.DESCRIZIONE " +
        "FROM   DB_TIPO_SPECIE_ORIENTAM_PROD SOP, " +
        "       DB_ORIENTAMENTO_PROD_COSMAN OPC, " +
        "       DB_TIPO_PRODUZIONE_COSMAN TPC " +
        "WHERE  SOP.ID_SPECIE_ANIMALE = ? " +
        "AND    SOP.ID_TIPO_PRODUZIONE = ? " +
        "AND    SOP.ID_ORIENTAMENTO_PRODUTTIVO = ? " +
        "AND    SOP.ID_SPECIE_ORIENTAM_PROD = OPC.ID_SPECIE_ORIENTAM_PROD " +
        "AND    OPC.DATA_FINE_VALIDITA IS NULL " +
        "AND    OPC.ID_TIPO_PRODUZIONE_COSMAN = TPC.ID_TIPO_PRODUZIONE_COSMAN " +
        "ORDER BY TPC.DESCRIZIONE ";

      stmt = conn.prepareStatement(search);
      SolmrLogger.debug(this, "getTipoProduzioneCosman Executing query: " + search);
      SolmrLogger.debug(this, "getTipoProduzioneCosman Parameter idSpecie: " + idSpecie);
      SolmrLogger.debug(this, "getTipoProduzioneCosman Parameter idTipoProduzione: " + idTipoProduzione);
      SolmrLogger.debug(this, "getTipoProduzioneCosman Parameter idOrientamentoProduttivo: " + idOrientamentoProduttivo);

      stmt.setLong(1, idSpecie);
      stmt.setLong(2, idTipoProduzione);
      stmt.setLong(3, idOrientamentoProduttivo);

      ResultSet rs = stmt.executeQuery();

      result = new Vector<CodeDescription>();
      while (rs.next())
      {
        CodeDescription cd = new CodeDescription();

        cd.setCode(new Integer(rs.getInt("ID_TIPO_PRODUZIONE_COSMAN")));
        cd.setDescription(rs.getString("DESCRIZIONE"));
        cd.setSecondaryCode(rs.getString("CODICE_COSMAN"));
        result.add(cd);
      }

      rs.close();

      SolmrLogger.debug(this, "getTipoProduzioneCosman Executed query - Found " + result.size() + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTipoProduzioneCosman SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getTipoProduzioneCosman Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "getTipoProduzioneCosman SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "getTipoProduzioneCosman Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  
  /**
   * 
   * Mi estare le sottocaterie cosman accetatte se flag a S
   * e non accetate se flag a N
   * 
   * 
   * 
   * @param idSpecie
   * @param idTipoProduzione
   * @param idOrientamentoProduttivo
   * @param flagEsiste
   * @return
   * @throws DataAccessException
   */
  public Vector<CodeDescription> getSottocategorieCosman(long idSpecie, 
      long idTipoProduzione, long idOrientamentoProduttivo, long idTipoProduzioneCosman, 
      String flagEsiste) throws DataAccessException
  {
    Vector<CodeDescription> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = 
        "SELECT OCA.ID_SOTTOCATEGORIA_ANIMALE, " +
        "       TSA.DESCRIZIONE " +
        "FROM   DB_TIPO_SPECIE_ORIENTAM_PROD SOP, " +
        "       DB_ORIENTAMENTO_PROD_COSMAN OPC, " +
        "       DB_ORIENTAMENTO_COSMAN_AMMESSO OCA, " +
        "       DB_TIPO_SOTTOCATEGORIA_ANIMALE TSA " +
        "WHERE  SOP.ID_SPECIE_ANIMALE = ? " +
        "AND    SOP.ID_TIPO_PRODUZIONE = ? " +
        "AND    SOP.ID_ORIENTAMENTO_PRODUTTIVO = ? " +
        "AND    SOP.ID_SPECIE_ORIENTAM_PROD = OPC.ID_SPECIE_ORIENTAM_PROD " +
        "AND    OPC.DATA_FINE_VALIDITA IS NULL " +
        "AND    OPC.ID_ORIENTAMENTO_PROD_COSMAN = OCA.ID_ORIENTAMENTO_PROD_COSMAN " +
        "AND    OPC.ID_TIPO_PRODUZIONE_COSMAN = ? " +
        "AND    OCA.ESISTE_SOTTOCATEGORIA = ? " +
        "AND    OCA.DATA_FINE_VALIDITA IS NULL " +
        "AND    OCA.ID_SOTTOCATEGORIA_ANIMALE = TSA.ID_SOTTOCATEGORIA_ANIMALE " +
        "ORDER BY TSA.DESCRIZIONE ASC ";

      stmt = conn.prepareStatement(search);
      SolmrLogger.debug(this, "getSottocategorieCosman Executing query: " + search);
      SolmrLogger.debug(this, "getSottocategorieCosman Parameter idSpecie: " + idSpecie);
      SolmrLogger.debug(this, "getSottocategorieCosman Parameter idTipoProduzione: " + idTipoProduzione);
      SolmrLogger.debug(this, "getSottocategorieCosman Parameter idOrientamentoProduttivo: " + idOrientamentoProduttivo);
      SolmrLogger.debug(this, "getSottocategorieCosman Parameter idTipoProduzioneCosman: " + idTipoProduzioneCosman);
      SolmrLogger.debug(this, "getSottocategorieCosman Parameter flagEsiste: " + flagEsiste);

      stmt.setLong(1, idSpecie);
      stmt.setLong(2, idTipoProduzione);
      stmt.setLong(3, idOrientamentoProduttivo);
      stmt.setLong(4, idTipoProduzioneCosman);
      stmt.setString(5, flagEsiste);

      ResultSet rs = stmt.executeQuery();

      
      while (rs.next())
      {
        if(result == null)
        {
          result = new Vector<CodeDescription>();
        }

        CodeDescription code = new CodeDescription();
        code.setCode(new Integer(rs.getInt("ID_SOTTOCATEGORIA_ANIMALE")));
        code.setDescription(rs.getString("DESCRIZIONE"));
        
        result.add(code);
      }

      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getSottocategorieCosman SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getSottocategorieCosman Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this, "getSottocategorieCosman SQLException while closing Statement and Connection: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "getSottocategorieCosman Generic Exception while closing Statement and Connection: " + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

}
