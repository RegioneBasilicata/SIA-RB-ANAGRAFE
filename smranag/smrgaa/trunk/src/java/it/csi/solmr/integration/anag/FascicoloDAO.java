package it.csi.solmr.integration.anag;

import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.ProvinciaVO;
import it.csi.solmr.dto.StringcodeDescription;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.ElencoAziendeParticellaVO;
import it.csi.solmr.dto.anag.EsitoControlloParticellaVO;
import it.csi.solmr.dto.anag.FabbricatoVO;
import it.csi.solmr.dto.anag.FoglioVO;
import it.csi.solmr.dto.anag.ParticellaAziendaVO;
import it.csi.solmr.dto.anag.ParticellaUtilizzoVO;
import it.csi.solmr.dto.anag.ParticellaVO;
import it.csi.solmr.dto.anag.UteVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.SolmrErrors;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.NotFoundException;
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
import java.sql.Types;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

public class FascicoloDAO extends it.csi.solmr.integration.BaseDAO
{

  public UteVO uteVO;

  public FascicoloDAO() throws ResourceAccessException
  {
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public FascicoloDAO(String refName) throws ResourceAccessException
  {
    super(refName);
  }

  public Vector<UteVO> getUTE(Long idAzienda, Boolean storico)
      throws DataAccessException, SolmrException
  {
    Vector<UteVO> collIdUte = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
      String query = "SELECT DB_UTE.ID_UTE, PROVINCIA.SIGLA_PROVINCIA, "
          + "DB_UTE.INDIRIZZO, DB_UTE.DENOMINAZIONE, "
          + "DB_UTE.DATA_INIZIO_ATTIVITA, DB_UTE.DATA_FINE_ATTIVITA, "
          + "COMUNE.FLAG_ESTERO, COMUNE.DESCOM, COMUNE.ISTAT_COMUNE "
          + "FROM DB_UTE, COMUNE, PROVINCIA "
          + "WHERE DB_UTE.COMUNE = COMUNE.ISTAT_COMUNE AND "
          + "COMUNE.ISTAT_PROVINCIA = PROVINCIA.ISTAT_PROVINCIA(+) AND "
          + "DB_UTE.ID_AZIENDA = ? ";

      if (storico == null || !storico.booleanValue())
      {
        // Visualizzo solo le ute con data fine validita a null
        query += " AND DB_UTE.DATA_FINE_ATTIVITA IS NULL ";
      }

      query += " ORDER BY DB_UTE.DATA_INIZIO_ATTIVITA ";

      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query getUTE: " + query);

      stmt.setLong(1, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      collIdUte = new Vector<UteVO>();

      while (rs.next())
      {
        uteVO = new UteVO();
        uteVO.setIdAzienda(idAzienda);
        uteVO.setIdUte(new Long(rs.getLong(1)));
        uteVO.setProvincia(rs.getString(2));
        uteVO.setIndirizzo(rs.getString(3));
        uteVO.setDenominazione(rs.getString(4));
        uteVO.setDataInizioAttivita(rs.getDate(5));
        uteVO.setDataFineAttivita(rs.getDate(6));
        // Se il flag_estero == S, valorizzo lo stato estero con la
        // descrizione del comune
        if (rs.getString(7).equals("S"))
        {
          uteVO.setComune("");
          uteVO.setIstat("");
          uteVO.setStatoEstero(rs.getString(8));
        }
        // Altrimenti valorizzo il comune con la descrizione e
        // l'ISTAT con il campo COMUNE
        else if (rs.getString(7).equals("N"))
        {
          uteVO.setComune(rs.getString(8));
          uteVO.setIstat(rs.getString(9));
          uteVO.setStatoEstero("");
        }

        collIdUte.add(uteVO);
      }

      rs.close();
      stmt.close();

      if (collIdUte.size() == 0)
        throw new SolmrException(AnagErrors.RICERCA_TERRENI_UTE);

      SolmrLogger.debug(this, "getUTE - Found " + collIdUte.size() + " records");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getUTE - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (SolmrException sex)
    {
      SolmrLogger.error(this, "getUTE - No data found");
      throw sex;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getUTE - Generic Exception: " + ex.getMessage());
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
            "getUTE - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "getUTE - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return collIdUte;
  }

  public UteVO getUteById(Long idUte) throws DataAccessException,
      NotFoundException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "" +
      		"SELECT DB_UTE.ID_UTE, " +
          "       PROVINCIA.SIGLA_PROVINCIA, " + 
      		"       COMUNE.DESCOM, " + 
      	  "       COMUNE.ISTAT_COMUNE, " +
          "       DB_UTE.CAP, " +
          "       DB_UTE.INDIRIZZO, " +
          "       DB_UTE.DENOMINAZIONE AS UTE_DENOMINAZIONE, " +
          "       DB_TIPO_ATTIVITA_OTE.DESCRIZIONE AS OTE_DESCRIZIONE, " +
          "       DB_TIPO_ATTIVITA_ATECO.DESCRIZIONE AS ATECO_DESCRIZIONE, " +
          "       DB_TIPO_ZONA_ALTIMETRICA.ID_ZONA_ALTIMETRICA AS TIPO_ID_ZONA_ALTIMETRICA, " +
          "       DB_TIPO_ZONA_ALTIMETRICA.DESCRIZIONE AS DESC_ZONA_ALTIMETRICA, " +
          "       DB_UTE.TELEFONO, " +
          "       DB_UTE.FAX, " +
          "       DB_UTE.DATA_INIZIO_ATTIVITA, " +
          "       DB_UTE.DATA_FINE_ATTIVITA, " +
          "       DB_UTE.CAUSALE_CESSAZIONE, " +
          "       DB_UTE.DATA_AGGIORNAMENTO, " +
          "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
          "          || ' ' " + 
          "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
          "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
          "         WHERE DB_UTE.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
          "       AS IRIDE_DENOMINAZIONE, " +
          "       (SELECT PVU.DENOMINAZIONE " +
          "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
          "        WHERE DB_UTE.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
          "       AS ENTE_DENOMINAZIONE, " +
          "       DB_UTE.MOTIVO_MODIFICA, " +
          "       DB_UTE.NOTE, " +
          "       DB_TIPO_ATTIVITA_OTE.CODICE AS OTE_CODICE, " +
          "       DB_TIPO_ATTIVITA_ATECO.CODICE AS ATECO_CODICE, " +
          "       DB_UTE.ID_ZONA_ALTIMETRICA AS UTE_ID_ZONA_ALTIMETRICA, " +
          "       DB_UTE.ID_AZIENDA " +
          "FROM   DB_UTE, " +
          "       COMUNE, " +
          "       DB_TIPO_ZONA_ALTIMETRICA, " +
          "       DB_TIPO_ATTIVITA_OTE, " +
          "       DB_TIPO_ATTIVITA_ATECO, " +
          //"       PAPUA_V_UTENTE_LOGIN PVU, " +
          "       PROVINCIA " +
          "WHERE  DB_UTE.COMUNE = COMUNE.ISTAT_COMUNE " +
          "AND    COMUNE.ISTAT_PROVINCIA = PROVINCIA.ISTAT_PROVINCIA " +
          "AND    DB_UTE.ID_ZONA_ALTIMETRICA = DB_TIPO_ZONA_ALTIMETRICA.ID_ZONA_ALTIMETRICA (+) " +
          "AND    DB_UTE.ID_ATTIVITA_OTE = DB_TIPO_ATTIVITA_OTE.ID_ATTIVITA_OTE (+) " +
          "AND    DB_UTE.ID_ATTIVITA_ATECO = DB_TIPO_ATTIVITA_ATECO.ID_ATTIVITA_ATECO(+) " +
          //"AND    DB_UTE.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
          "AND DB_UTE.ID_UTE = ? ";
      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);
      stmt.setLong(1, idUte.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        uteVO = new UteVO();
        uteVO.setIdUte(new Long(rs.getLong("ID_UTE")));
        uteVO.setProvincia(rs.getString("SIGLA_PROVINCIA"));
        // In data astrale 30/06 mi è stato detto che le UTE
        // non necsssitano del flag_estero, perchè obbligatoriamente
        // tutte italiane
        uteVO.setComune(rs.getString("DESCOM"));
        uteVO.setIstat(rs.getString("ISTAT_COMUNE"));
        uteVO.setCap(rs.getString("CAP"));
        uteVO.setIndirizzo(rs.getString("INDIRIZZO"));
        uteVO.setDenominazione(rs.getString("UTE_DENOMINAZIONE"));
        uteVO.setOte(rs.getString("OTE_DESCRIZIONE"));
        uteVO.setDescOte(rs.getString("OTE_DESCRIZIONE"));
        uteVO.setAteco(rs.getString("ATECO_DESCRIZIONE"));
        uteVO.setDescAteco(rs.getString("ATECO_DESCRIZIONE"));
        CodeDescription cd = new CodeDescription();
        cd.setCode(new Integer(rs.getInt("TIPO_ID_ZONA_ALTIMETRICA")));
        cd.setDescription(rs.getString("DESC_ZONA_ALTIMETRICA"));
        uteVO.setTipoZonaAltimetrica(cd);
        uteVO.setZonaAltimetrica(rs.getString("DESC_ZONA_ALTIMETRICA"));
        uteVO.setTelefono(rs.getString("TELEFONO"));
        uteVO.setFax(rs.getString("FAX"));
        uteVO.setDataInizioAttivita(rs.getDate("DATA_INIZIO_ATTIVITA"));
        uteVO.setDataFineAttivita(rs.getDate("DATA_FINE_ATTIVITA"));
        if (rs.getString("CAUSALE_CESSAZIONE") != null)
          uteVO.setCausaleCessazione(rs.getString("CAUSALE_CESSAZIONE"));
        else
          uteVO.setCausaleCessazione("");

        /*
         * if (rs.getDate("DATA_AGGIORNAMENTO")!=null){ ultimaModifica =
         * DateUtils.formatDate(rs.getDate("DATA_AGGIORNAMENTO")); if
         * (rs.getString("IRIDE_DENOMINAZIONE")!= null){ ultimaModifica+= "
         * ("+rs.getString("IRIDE_DENOMINAZIONE")+")";
         * if(rs.getString("MOTIVO_MODIFICA") != null){ ultimaModifica+= " -
         * "+rs.getString("MOTIVO_MODIFICA"); } } }
         * uteVO.setUltimaModifica(ultimaModifica);
         */

        // Nuovo Blocco ultima modifica
        String ultimaModifica = "";
        java.util.Date dataAgg = rs.getDate("DATA_AGGIORNAMENTO");
        if (dataAgg != null)
        {
          ultimaModifica = DateUtils.formatDate(dataAgg);
        }

        String tmp = "";
        String utDenominazione = rs.getString("IRIDE_DENOMINAZIONE");
        if (utDenominazione != null && !utDenominazione.equals(""))
        {
          if (tmp.equals(""))
            tmp += " (";
          tmp += utDenominazione;
        }
        String utEnteAppart = rs.getString("ENTE_DENOMINAZIONE");
        if (utEnteAppart != null && !utEnteAppart.equals(""))
        {
          if (tmp.equals(""))
            tmp += " (";
          else
            tmp += " - ";
          tmp += utEnteAppart;
        }
        String motivoModifica = rs.getString("MOTIVO_MODIFICA");
        if (motivoModifica != null && !motivoModifica.equals(""))
        {
          if (tmp.equals(""))
            tmp += " (";
          else
            tmp += " - ";
          tmp += motivoModifica;
        }
        if (!tmp.equals(""))
          tmp += ")";
        ultimaModifica += tmp;

        uteVO.setUltimaModifica(ultimaModifica);

        // Ultima Modifica Spezzata
        uteVO.setDataAggiornamento(dataAgg);
        uteVO.setUtenteUltimaModifica(utDenominazione);
        uteVO.setEnteUltimaModifica(utEnteAppart);
        uteVO.setMotivoModifica(motivoModifica);

        uteVO.setNote(rs.getString("NOTE"));
        uteVO.setCodeOte(rs.getString("OTE_CODICE"));
        uteVO.setCodeAteco(rs.getString("ATECO_CODICE"));
        if (Validator.isNotEmpty(rs.getString("UTE_ID_ZONA_ALTIMETRICA")))
        {
          uteVO.setIdZonaAltimetrica(new Long(rs
              .getLong("UTE_ID_ZONA_ALTIMETRICA")));
        }

        uteVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));

      }
      else
        throw new NotFoundException(SolmrErrors.EXC_NOT_FOUND_PK);

      rs.close();
      stmt.close();

      if (uteVO == null)
        throw new NotFoundException(SolmrErrors.EXC_NOT_FOUND_PK);

      SolmrLogger.debug(this, "getUTEbyID - Found record with primary key: "
          + idUte);
    }
    catch (NotFoundException exc)
    {
      SolmrLogger.fatal(this, "getUTEbyID - NotFoundException: "
          + exc.getMessage());
      throw new NotFoundException(exc.getMessage());
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getUTEbyID - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getUTEbyID - Generic Exception: "
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
            "getUTEbyID - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "getUTEbyID - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return uteVO;
  }

  public UteVO[] getUteByIdRange(Long idUte[]) throws DataAccessException,
      NotFoundException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    UteVO[] uteVOArray = new UteVO[idUte.length];
    try
    {
      conn = getDatasource().getConnection();

      String idUteRangeQuery = "";
      if (idUte != null && idUte.length > 0)
      {
        idUteRangeQuery += "  AND DB_UTE.ID_UTE IN (";
        for (int i = 0; i < idUte.length; i++)
        {
          if (i > 0)
            idUteRangeQuery += ",";
          idUteRangeQuery += "?";
        }
        idUteRangeQuery += ") ";
      }

      String query = "SELECT "
          + "DB_UTE.ID_UTE, "
          + "PROVINCIA.SIGLA_PROVINCIA, "
          + "COMUNE.DESCOM, "
          + "COMUNE.ISTAT_COMUNE, "
          + "DB_UTE.CAP, "
          + "DB_UTE.INDIRIZZO, "
          + "DB_UTE.DENOMINAZIONE AS UTE_DENOMINAZIONE, "
          + "DB_TIPO_ATTIVITA_OTE.DESCRIZIONE AS OTE_DESCRIZIONE,"
          + "DB_TIPO_ATTIVITA_ATECO.DESCRIZIONE AS ATECO_DESCRIZIONE, "
          + "DB_TIPO_ZONA_ALTIMETRICA.ID_ZONA_ALTIMETRICA AS TIPO_ID_ZONA_ALTIMETRICA, "
          + "DB_TIPO_ZONA_ALTIMETRICA.DESCRIZIONE AS ALTIMETRICA_DESCRIZIONE, "
          + "DB_UTE.TELEFONO, "
          + "DB_UTE.FAX, "
          + "DB_UTE.DATA_INIZIO_ATTIVITA, "
          + "DB_UTE.DATA_FINE_ATTIVITA, "
          + "DB_UTE.CAUSALE_CESSAZIONE, "
          + "DB_UTE.DATA_AGGIORNAMENTO, " +
          "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
          "          || ' ' " + 
          "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
          "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
          "         WHERE DB_UTE.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
          "  AS IRIDE_DENOMINAZIONE, " +
          "       (SELECT PVU.DENOMINAZIONE " +
          "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
          "        WHERE DB_UTE.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
          "  AS ENTE_DENOMINAZIONE, "
          + "DB_UTE.MOTIVO_MODIFICA, "
          + "DB_UTE.NOTE, "
          + "DB_TIPO_ATTIVITA_OTE.CODICE AS OTE_CODICE, "
          + "DB_TIPO_ATTIVITA_ATECO.CODICE AS ATECO_CODICE, "
          + "DB_UTE.ID_ZONA_ALTIMETRICA AS UTE_ID_ZONA_ALTIMETRICA,  "
          + "DB_UTE.ID_AZIENDA "
          + "FROM DB_UTE, " +
          "       COMUNE, " +
          "       DB_TIPO_ZONA_ALTIMETRICA, " +
          "       DB_TIPO_ATTIVITA_OTE, " +
          "       DB_TIPO_ATTIVITA_ATECO, " +
          //"       PAPUA_V_UTENTE_LOGIN PVU, " +
          "       PROVINCIA "
          + "WHERE DB_UTE.COMUNE = COMUNE.ISTAT_COMUNE " +
            "AND   COMUNE.ISTAT_PROVINCIA = PROVINCIA.ISTAT_PROVINCIA " +
            "AND   DB_UTE.ID_ZONA_ALTIMETRICA = DB_TIPO_ZONA_ALTIMETRICA.ID_ZONA_ALTIMETRICA " +
            "AND   DB_UTE.ID_ATTIVITA_OTE = DB_TIPO_ATTIVITA_OTE.ID_ATTIVITA_OTE(+) " +
            "AND   DB_UTE.ID_ATTIVITA_ATECO = DB_TIPO_ATTIVITA_ATECO.ID_ATTIVITA_ATECO(+) "
            //"AND   DB_UTE.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN "
          + idUteRangeQuery;
      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);
      String idUteRange = "";
      if (idUte != null && idUte.length > 0)
      {
        for (int i = 0; i < idUte.length; i++)
        {
          idUteRange += ((i == 0 ? "" : ",") + idUte[i]);
          stmt.setLong(i + 1, idUte[i].longValue());
        }
        SolmrLogger.debug(this, "\tParametri idUteRange: " + idUteRange + "\n");
      }

      ResultSet rs = stmt.executeQuery();

      int contaUte = 0;
      while (rs.next())
      {
        uteVO = new UteVO();
        uteVO.setIdUte(new Long(rs.getLong("ID_UTE")));
        uteVO.setProvincia(rs.getString("SIGLA_PROVINCIA"));
        // In data astrale 30/06 mi è stato detto che le UTE
        // non necsssitano del flag_estero, perchè obbligatoriamente
        // tutte italiane
        uteVO.setComune(rs.getString("DESCOM"));
        uteVO.setIstat(rs.getString("ISTAT_COMUNE"));
        uteVO.setCap(rs.getString("CAP"));
        uteVO.setIndirizzo(rs.getString("INDIRIZZO"));
        uteVO.setDenominazione(rs.getString("UTE_DENOMINAZIONE"));
        uteVO.setOte(rs.getString("OTE_DESCRIZIONE"));
        uteVO.setDescOte(rs.getString("OTE_DESCRIZIONE"));
        uteVO.setAteco(rs.getString("ATECO_DESCRIZIONE"));
        uteVO.setDescAteco(rs.getString("ATECO_DESCRIZIONE"));
        CodeDescription cd = new CodeDescription();
        cd.setCode(new Integer(rs.getInt("TIPO_ID_ZONA_ALTIMETRICA")));
        cd.setDescription(rs.getString("ALTIMETRICA_DESCRIZIONE"));
        uteVO.setTipoZonaAltimetrica(cd);
        uteVO.setZonaAltimetrica(rs.getString("ALTIMETRICA_DESCRIZIONE"));
        uteVO.setTelefono(rs.getString("TELEFONO"));
        uteVO.setFax(rs.getString("FAX"));
        uteVO.setDataInizioAttivita(rs.getDate("DATA_INIZIO_ATTIVITA"));
        uteVO.setDataFineAttivita(rs.getDate("DATA_FINE_ATTIVITA"));
        if (rs.getString("CAUSALE_CESSAZIONE") != null)
          uteVO.setCausaleCessazione(rs.getString("CAUSALE_CESSAZIONE"));
        else
          uteVO.setCausaleCessazione("");

        /*
         * if (rs.getDate(17)!=null){ ultimaModifica =
         * DateUtils.formatDate(rs.getDate(17)); if (rs.getString(18)!= null){
         * ultimaModifica+= " ("+rs.getString(18)+")"; if(rs.getString(19) !=
         * null){ ultimaModifica+= " - "+rs.getString(19); } } }
         * uteVO.setUltimaModifica(ultimaModifica);
         */

        // Nuovo Blocco ultima modifica
        String ultimaModifica = "";
        java.util.Date dataAgg = rs.getDate("DATA_AGGIORNAMENTO");
        if (dataAgg != null)
        {
          ultimaModifica = DateUtils.formatDate(dataAgg);
        }

        String tmp = "";
        String utDenominazione = rs.getString("IRIDE_DENOMINAZIONE");
        if (utDenominazione != null && !utDenominazione.equals(""))
        {
          if (tmp.equals(""))
            tmp += " (";
          tmp += utDenominazione;
        }
        String utEnteAppart = rs.getString("ENTE_DENOMINAZIONE");
        if (utEnteAppart != null && !utEnteAppart.equals(""))
        {
          if (tmp.equals(""))
            tmp += " (";
          else
            tmp += " - ";
          tmp += utEnteAppart;
        }
        String motivoModifica = rs.getString("MOTIVO_MODIFICA");
        if (motivoModifica != null && !motivoModifica.equals(""))
        {
          if (tmp.equals(""))
            tmp += " (";
          else
            tmp += " - ";
          tmp += motivoModifica;
        }
        if (!tmp.equals(""))
          tmp += ")";
        ultimaModifica += tmp;

        uteVO.setUltimaModifica(ultimaModifica);

        // Ultima Modifica Spezzata
        uteVO.setDataAggiornamento(dataAgg);
        uteVO.setUtenteUltimaModifica(utDenominazione);
        uteVO.setEnteUltimaModifica(utEnteAppart);
        uteVO.setEnteUltimaModifica(motivoModifica);

        uteVO.setNote(rs.getString("NOTE"));
        uteVO.setCodeOte(rs.getString("OTE_CODICE"));
        uteVO.setCodeAteco(rs.getString("ATECO_CODICE"));
        if (Validator.isNotEmpty(rs.getString("UTE_ID_ZONA_ALTIMETRICA")))
        {
          uteVO.setIdZonaAltimetrica(new Long(rs
              .getLong("UTE_ID_ZONA_ALTIMETRICA")));
        }
        try
        {
          uteVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        }
        catch (Exception e)
        {
        }
        uteVOArray[contaUte++] = uteVO;
      }
      // else
      // throw new NotFoundException(SolmrErrors.EXC_NOT_FOUND_PK);

      rs.close();
      stmt.close();

      if (uteVOArray[0] == null)
        throw new NotFoundException(SolmrErrors.EXC_NOT_FOUND_PK);

      SolmrLogger.debug(this,
          "getUteByIdRange - Found records with primary keys: " + idUteRange);
    }
    catch (NotFoundException exc)
    {
      SolmrLogger.fatal(this, "getUteByIdRange - NotFoundException: "
          + exc.getMessage());
      throw new NotFoundException(exc.getMessage());
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getUteByIdRange - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getUteByIdRange - Generic Exception: "
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
            "getUteByIdRange - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "getUteByIdRange - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return uteVOArray;
  }

  public void deleteUTE(Long idUte) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "DELETE from DB_UTE " + " WHERE ID_UTE = ? ";
      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing delete: " + query);

      stmt.setLong(1, idUte.longValue());
      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed delete.");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "deleteUTE - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "deleteUTE - Generic Exception: "
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
            "deleteUTE - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "deleteUTE - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }
  
  public void deleteUTEAtecoSec(Long idUte) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "DELETE from DB_UTE_ATECO_SECONDARI " + " WHERE ID_UTE = ? ";
      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing delete: " + query);

      stmt.setLong(1, idUte.longValue());
      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed delete.");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "deleteUTEAtecoSec - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "deleteUTEAtecoSec - Generic Exception: "
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
            "deleteUTEAtecoSec - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "deleteUTEAtecoSec - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  public void mayIDeleteUTE(Long idUte) throws DataAccessException,
      SolmrException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      /*
       * Se è presente un record in DB_CONDUZIONE_PARTICELLA non posso
       * cancellare la UTE e devo generare l'errrore relativo alla particella
       */
      String query = "SELECT P.ID_CONDUZIONE_PARTICELLA "
          + "FROM DB_CONDUZIONE_PARTICELLA P " + "WHERE P.ID_UTE = ? ";
      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing delete: " + query);

      stmt.setLong(1, idUte.longValue());
      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        throw new SolmrException(AnagErrors.DELETE_UTE_PARTICELLE);
      else
      {
        rs.close();
        stmt.close();
      }

      /*
       * Se è presente un record in DB_CONDUZIONE_DICHIARATA non posso
       * cancellare la UTE e devo generare l'errrore relativo alla particella
       */
      query = "SELECT C.ID_CONDUZIONE_DICHIARATA "
          + "FROM DB_CONDUZIONE_DICHIARATA  C " + "WHERE C.ID_UTE = ? ";
      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing delete: " + query);

      stmt.setLong(1, idUte.longValue());
      rs = stmt.executeQuery();

      if (rs.next())
        throw new SolmrException(AnagErrors.DELETE_UTE_PARTICELLE);
      else
      {
        rs.close();
        stmt.close();
      }

      /*
       * Se è presente un record in DB_ALLEVAMENTO non posso cancellare la UTE e
       * devo generare l'errrore relativo all'allevamento
       */
      query = "SELECT A.ID_ALLEVAMENTO " + "FROM DB_ALLEVAMENTO A "
          + "WHERE A.ID_UTE = ? ";
      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing delete: " + query);

      stmt.setLong(1, idUte.longValue());
      rs = stmt.executeQuery();

      if (rs.next())
        throw new SolmrException(AnagErrors.DELETE_UTE_ALLEVAMENTI);
      else
      {
        rs.close();
        stmt.close();
      }

      /*
       * Se è presente un record in DB_FABBRICATO non posso cancellare la UTE e
       * devo generare l'errrore relativo al fabbricato
       */
      query = "SELECT F.ID_FABBRICATO " + "FROM DB_FABBRICATO F "
          + "WHERE F.ID_UTE = ? ";
      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing delete: " + query);

      stmt.setLong(1, idUte.longValue());
      rs = stmt.executeQuery();

      if (rs.next())
        throw new SolmrException(AnagErrors.DELETE_UTE_FABBRICATI);
      else
      {
        rs.close();
        stmt.close();
      }
      
      
      /*
       * Se è presente un record in DB_COMUNICAZIONE_10R non posso cancellare la UTE e
       * devo generare l'errrore relativo alla comunicazione 10r
       */
      query = "SELECT COM.ID_COMUNICAZIONE_10R " + "FROM DB_COMUNICAZIONE_10R COM "
          + "WHERE COM.ID_UTE = ? ";
      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing delete: " + query);

      stmt.setLong(1, idUte.longValue());
      rs = stmt.executeQuery();

      if (rs.next())
        throw new SolmrException(AnagErrors.DELETE_UTE_COMUNICAZIONE_10R);
      else
      {
        rs.close();
        stmt.close();
      }
      
      /*
       * Se è presente un record in DB_POSSESSO_MACCHINA non posso cancellare la UTE e
       * devo generare l'errrore relativo alle macchine agricole
       */
      query = "SELECT PM.ID_POSSESSO_MACCHINA " + "FROM DB_POSSESSO_MACCHINA PM "
          + "WHERE PM.ID_UTE = ? ";
      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing delete: " + query);

      stmt.setLong(1, idUte.longValue());
      rs = stmt.executeQuery();

      if (rs.next())
        throw new SolmrException(AnagErrors.DELETE_UTE_MACCHINE_AGRICOLE);
      else
      {
        rs.close();
        stmt.close();
      }
      

      SolmrLogger.debug(this, "Executed delete.");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "deleteUTE - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (SolmrException exc)
    {
      SolmrLogger
          .fatal(this, "deleteUTE - SolmrException: " + exc.getMessage());
      throw exc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "deleteUTE - Generic Exception: "
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
            "deleteUTE - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "deleteUTE - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  public void mayICessazioneUTE(Long idUte) throws DataAccessException,
      SolmrException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      /*
       * Se è presente un record in DB_CONDUZIONE_PARTICELLA non posso
       * cancellare la UTE e devo generare l'errrore relativo alla particella
       */
      String query = "SELECT P.ID_CONDUZIONE_PARTICELLA "
          + "FROM DB_CONDUZIONE_PARTICELLA P " + "WHERE P.ID_UTE = ? "
          + "AND P.DATA_FINE_CONDUZIONE IS NULL ";
      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing delete: " + query);

      stmt.setLong(1, idUte.longValue());
      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        throw new SolmrException(AnagErrors.CESSAZIONE_UTE_PARTICELLE);
      else
      {
        rs.close();
        stmt.close();
      }

      /*
       * Se è presente un record in DB_CONDUZIONE_DICHIARATA non posso
       * cancellare la UTE e devo generare l'errrore relativo alla particella
       */
      /*
       * query = "SELECT C.ID_CONDUZIONE_DICHIARATA "+ "FROM
       * DB_CONDUZIONE_DICHIARATA C "+ "WHERE C.ID_UTE = ? "; stmt =
       * conn.prepareStatement(query);
       * 
       * SolmrLogger.debug(this, "Executing delete: "+query);
       * 
       * stmt.setLong(1, idUte.longValue()); rs = stmt.executeQuery();
       * 
       * if(rs.next()) throw new
       * SolmrException(AnagErrors.CESSAZIONE_UTE_PARTICELLE); else {
       * rs.close(); stmt.close(); }
       */

      /*
       * Se è presente un record attivo in DB_ALLEVAMENTO non posso cancellare
       * la UTE e devo generare l'errrore relativo all'allevamento
       */
      query = "SELECT A.ID_ALLEVAMENTO " + "FROM DB_ALLEVAMENTO A "
          + "WHERE A.ID_UTE = ? " + "AND A.DATA_FINE IS NULL";
      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing delete: " + query);

      stmt.setLong(1, idUte.longValue());
      rs = stmt.executeQuery();

      if (rs.next())
        throw new SolmrException(AnagErrors.CESSAZIONE_UTE_ALLEVAMENTI);
      else
      {
        rs.close();
        stmt.close();
      }

      /*
       * Se è presente un record attivo in DB_FABBRICATO non posso cancellare la
       * UTE e devo generare l'errrore relativo al fabbricato
       */
      query = "SELECT F.ID_FABBRICATO " + "FROM DB_FABBRICATO F "
          + "WHERE F.ID_UTE = ? " + "AND F.DATA_FINE_VALIDITA IS NULL";
      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing delete: " + query);

      stmt.setLong(1, idUte.longValue());
      rs = stmt.executeQuery();

      if (rs.next())
        throw new SolmrException(AnagErrors.CESSAZIONE_UTE_FABBRICATI);
      else
      {
        rs.close();
        stmt.close();
      }
      
      
      /*
       * Se è presente un record attivo in DB_POSSESSO_MACCHINA non posso cancellare la
       * UTE e devo generare l'errrore relativo alla macchina
       */
      query = "SELECT PM.ID_POSSESSO_MACCHINA " + "FROM DB_POSSESSO_MACCHINA PM "
          + "WHERE PM.ID_UTE = ? " + " AND PM.DATA_FINE_VALIDITA IS NULL " + " AND PM.DATA_SCARICO IS NULL ";
      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing delete: " + query);

      stmt.setLong(1, idUte.longValue());
      rs = stmt.executeQuery();

      if (rs.next())
        throw new SolmrException(AnagErrors.CESSAZIONE_UTE_MACCHINE_AGRICOLE);
      else
      {
        rs.close();
        stmt.close();
      }

      SolmrLogger.debug(this, "Executed delete.");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "deleteUTE - SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (SolmrException exc)
    {
      SolmrLogger
          .fatal(this, "deleteUTE - SolmrException: " + exc.getMessage());
      throw exc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "deleteUTE - Generic Exception: "
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
            "deleteUTE - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "deleteUTE - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  public void updateUTE(UteVO uteVO) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = " UPDATE DB_UTE " + "    SET DENOMINAZIONE = ?, "
          + "        INDIRIZZO = ?, " + "        COMUNE = ?, "
          + "        CAP = ?, " + "        TELEFONO = ?, "
          + "        FAX = ?, " + "        ID_ZONA_ALTIMETRICA = ?, "
          + "        NOTE = ?, " + "        MOTIVO_MODIFICA = ?, "
          + "        DATA_INIZIO_ATTIVITA = ?, "
          + "        ID_ATTIVITA_ATECO = ?, " + "        ID_ATTIVITA_OTE = ?, "
          + "        ID_UTENTE_AGGIORNAMENTO = ?, "
          + "        DATA_AGGIORNAMENTO = SYSDATE " + " WHERE  ID_UTE = ? ";
      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing updateUTE: " + query);

      stmt.setString(1, uteVO.getDenominazione());
      stmt.setString(2, uteVO.getIndirizzo());
      stmt.setString(3, uteVO.getIdComune());
      stmt.setString(4, uteVO.getCap());
      stmt.setString(5, uteVO.getTelefono());
      stmt.setString(6, uteVO.getFax());
      stmt.setLong(7, uteVO.getIdZonaAltimetrica().longValue());
      stmt.setString(8, uteVO.getNote());
      stmt.setString(9, uteVO.getMotivoModifica());
      stmt.setDate(10, new java.sql.Date(uteVO.getDataInizioAttivita()
          .getTime()));
      if (uteVO.getIdAteco() == null
          || uteVO.getIdAteco().toString().equals(""))
        stmt.setBigDecimal(11, null);
      else
        stmt.setInt(11, new Integer(uteVO.getIdAteco()).intValue());
      if (uteVO.getIdOte() == null || uteVO.getIdOte().toString().equals(""))
        stmt.setBigDecimal(12, null);
      else
        stmt.setInt(12, new Integer(uteVO.getIdOte()).intValue());
      stmt.setLong(13, uteVO.getUtenteAggiornamento().longValue());
      stmt.setLong(14, uteVO.getIdUte().longValue());

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed update.");
    }
    catch (SQLException exc)
    {
      if (exc.getErrorCode() == ((Long) SolmrConstants
          .get("CODE_DUP_KEY_ORACLE")).intValue())
      {
        SolmrLogger
            .fatal(this, "updateUTE - SQLException: CODE_DUP_KEY_ORACLE");
        throw new DataAccessException(AnagErrors.ERRORE_UTE_DUP_KEY);
      }
      else
      {
        SolmrLogger
            .fatal(this, "updateUTE - SQLException: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "updateUTE - Generic Exception: " + ex);
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
            "updateUTE - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "updateUTE - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per recuperare l'elenco delle unità produttive valide associate ad
  // un'azienda agricola
  public Vector<UteVO> getElencoUteAttiveForAzienda(Long idAzienda)
      throws DataAccessException, NotFoundException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<UteVO> elencoUteAttive = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT U.ID_UTE, U.ID_AZIENDA, U.DENOMINAZIONE, U.INDIRIZZO, "
          + " U.COMUNE, C.DESCOM, U.ID_ZONA_ALTIMETRICA, ZA.DESCRIZIONE, "
          + " U.CAP, U.ID_ATTIVITA_ATECO, AA.DESCRIZIONE, U.NOTE, U.TELEFONO, "
          + " U.FAX, U.ID_UTENTE_AGGIORNAMENTO, U.DATA_INIZIO_ATTIVITA, "
          + " U.DATA_FINE_ATTIVITA, U.CAUSALE_CESSAZIONE, U.DATA_AGGIORNAMENTO, "
          + " U.MOTIVO_MODIFICA, U.ID_ATTIVITA_OTE, AO.DESCRIZIONE, "
          + " P.SIGLA_PROVINCIA "
          + " FROM DB_UTE U, COMUNE C, DB_TIPO_ZONA_ALTIMETRICA ZA, "
          + " DB_TIPO_ATTIVITA_ATECO AA, DB_TIPO_ATTIVITA_OTE AO, "
          + " PROVINCIA P "
          + " WHERE ID_AZIENDA = ? "
          + " AND DATA_FINE_ATTIVITA IS NULL AND U.COMUNE = C.ISTAT_COMUNE(+) "
          + " AND C.ISTAT_PROVINCIA=P.ISTAT_PROVINCIA(+) "
          + " AND U.ID_ZONA_ALTIMETRICA = ZA.ID_ZONA_ALTIMETRICA(+) "
          + " AND U.ID_ATTIVITA_ATECO = AA.ID_ATTIVITA_ATECO(+) "
          + " AND U.ID_ATTIVITA_OTE = AO.ID_ATTIVITA_OTE(+) ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing delete: " + query);

      stmt.setLong(1, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        elencoUteAttive = new Vector<UteVO>();
        while (rs.next())
        {
          UteVO uteVO = new UteVO();
          uteVO.setIdUte(new Long(rs.getLong(1)));
          uteVO.setIdAzienda(new Long(rs.getLong(2)));
          uteVO.setDenominazione(rs.getString(3));
          uteVO.setIndirizzo(rs.getString(4));
          uteVO.setIdComune(rs.getString(5));
          uteVO.setComune(rs.getString(6));
          if (Validator.isNotEmpty(rs.getString(7)))
          {
            uteVO.setIdZonaAltimetrica(new Long(rs.getLong(7)));
          }
          uteVO.setZonaAltimetrica(rs.getString(8));
          uteVO.setCap(rs.getString(9));
          uteVO.setCodeAteco(rs.getString(10));
          uteVO.setAteco(rs.getString(11));
          uteVO.setNote(rs.getString(12));
          uteVO.setTelefono(rs.getString(13));
          uteVO.setFax(rs.getString(14));
          uteVO.setUtenteAggiornamento(new Long(rs.getLong(15)));
          uteVO.setDataInizioAttivita(rs.getDate(16));
          if (rs.getDate(17) != null)
          {
            uteVO.setDataFineAttivita(rs.getDate(17));
          }
          uteVO.setCausaleCessazione(rs.getString(18));
          uteVO.setMotivoModifica(rs.getString(19));
          uteVO.setCodeOte(rs.getString(20));
          uteVO.setOte(rs.getString(21));
          uteVO.setProvincia(rs.getString("SIGLA_PROVINCIA"));
          elencoUteAttive.add(uteVO);
        }
      }

      stmt.close();
      rs.close();

      if (elencoUteAttive == null || elencoUteAttive.size() == 0)
      {
        throw new NotFoundException((String) AnagErrors.get("ERR_UTE_ATTIVE"));
      }
    }
    catch (NotFoundException exc)
    {
      SolmrLogger.fatal(this,
          "getElencoUteAttiveForAzienda - NotFoundException: "
              + exc.getMessage());
      throw new NotFoundException(exc.getMessage());
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getElencoUteAttiveForAzienda - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getElencoUteAttiveForAzienda - Generic Exception: " + ex);
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
                "getElencoUteAttiveForAzienda - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getElencoUteAttiveForAzienda - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoUteAttive;
  }

  // Metodo per recuperare l'elenco delle sezioni relative ad uno specifico
  // comune
  public Vector<CodeDescription> getSezioniByComune(String istatComune)
      throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<CodeDescription> elencoSezioni = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT SEZIONE, DESCRIZIONE FROM DB_SEZIONE WHERE ISTAT_COMUNE = ?";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setString(1, istatComune);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        elencoSezioni = new Vector<CodeDescription>();
        while (rs.next())
        {
          CodeDescription code = new CodeDescription();
          String sezione = rs.getString(1);
          String descrizione = rs.getString(2);
          code.setSecondaryCode(sezione);
          code.setDescription(descrizione);
          elencoSezioni.add(code);
        }
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getSezioniByComune - SQLException: "
          + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getSezioniByComune - Generic Exception: " + ex);
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
        SolmrLogger.fatal(this,
            "getSezioniByComune - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getSezioniByComune - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoSezioni;
  }

  // Metodo per recuperare l'elenco dei fogli in relazione al comune o stato
  // estero
  // ed eventualmente la sezione
  public Vector<FoglioVO> getFogliByComuneAndSezione(String istatComune,
      String sezione, Long foglio) throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<FoglioVO> elencoFogli = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT F.ID_FOGLIO, "
          + "        F.ID_CASO_PARTICOLARE, " + "        CP.DESCRIZIONE, "
          + "        F.SEZIONE, " + "        S.DESCRIZIONE, "
          + "        F.ID_AREA_A, " + "        F.ID_AREA_B, "
          + "        F.ID_AREA_C, " + "        F.ID_AREA_D, "
          + "        F.ID_AREA_E, " + "        F.FLAG_CAPTAZIONE_POZZI, "
          + "        TAA.DESCRIZIONE, " + "        TAB.DESCRIZIONE, "
          + "        TAC.DESCRIZIONE, " + "        TAD.DESCRIZIONE, "
          + "        TAE.DESCRIZIONE, " + "        F.FOGLIO, "
          + "        F.COMUNE, " + "        C.DESCOM, "
          + "        F.ID_ZONA_ALTIMETRICA, " + "        TZA.DESCRIZIONE "
          + " FROM   DB_FOGLIO F, " + "        DB_TIPO_CASO_PARTICOLARE CP, "
          + "        DB_TIPO_AREA_A TAA, " + "        DB_TIPO_AREA_B TAB, "
          + "        DB_TIPO_AREA_C TAC, " + "        DB_TIPO_AREA_D TAD, "
          + "        DB_TIPO_AREA_E TAE, " + "        COMUNE C, "
          + "        DB_TIPO_ZONA_ALTIMETRICA TZA, " + "        DB_SEZIONE S "
          + " WHERE  COMUNE = ? "
          + " AND    F.ID_CASO_PARTICOLARE = CP.ID_CASO_PARTICOLARE(+) "
          + " AND    F.ID_AREA_A = TAA.ID_AREA_A(+) "
          + " AND    F.ID_AREA_B = TAB.ID_AREA_B(+) "
          + " AND    F.ID_AREA_C = TAC.ID_AREA_C(+) "
          + " AND    F.ID_AREA_D = TAD.ID_AREA_D(+) "
          + " AND    F.ID_AREA_E = TAE.ID_AREA_E(+) "
          + " AND    F.COMUNE = C.ISTAT_COMUNE(+) "
          + " AND    F.ID_ZONA_ALTIMETRICA = TZA.ID_ZONA_ALTIMETRICA(+) "
          + " AND    S.SEZIONE(+) = F.SEZIONE "
          + " AND    F.COMUNE = S.ISTAT_COMUNE(+) ";

      if (sezione != null && !sezione.equals(""))
      {
        query += "AND F.SEZIONE = ? ";
      }
      if (foglio != null)
      {
        query += "AND FOGLIO = ? ";
      }

      query += " ORDER BY F.COMUNE, F.SEZIONE, F.FOGLIO ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      int indiceStatement = 0;

      stmt.setString(++indiceStatement, istatComune);

      if (sezione != null && !sezione.equals(""))
      {
        stmt.setString(++indiceStatement, sezione.toUpperCase());
      }
      if (foglio != null)
      {
        stmt.setString(++indiceStatement, foglio.toString());
      }

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        elencoFogli = new Vector<FoglioVO>();
        while (rs.next())
        {
          FoglioVO foglioVO = new FoglioVO();
          foglioVO.setIdFoglio(new Long(rs.getLong(1)));
          if (rs.getString(2) != null && !rs.getString(2).equals(""))
          {
            foglioVO.setIdCasoParticolare(new Long(rs.getLong(2)));
          }
          foglioVO.setDescrizioneCasoParticolare(rs.getString(3));
          foglioVO.setSezione(rs.getString(4));
          foglioVO.setDescrizioneSezione(rs.getString(5));
          if (rs.getString(6) != null && !rs.getString(6).equals(""))
          {
            foglioVO.setIdAreaA(new Long(rs.getLong(6)));
          }
          if (rs.getString(7) != null && !rs.getString(7).equals(""))
          {
            foglioVO.setIdAreaB(new Long(rs.getLong(7)));
          }
          if (rs.getString(8) != null && !rs.getString(8).equals(""))
          {
            foglioVO.setIdAreaC(new Long(rs.getLong(8)));
          }
          if (rs.getString(9) != null && !rs.getString(9).equals(""))
          {
            foglioVO.setIdAreaD(new Long(rs.getLong(9)));
          }
          if (rs.getString(10) != null && !rs.getString(10).equals(""))
          {
            foglioVO.setIdAreaE(new Long(rs.getLong(10)));
          }
          foglioVO.setFlagCaptazionePozzi(rs.getString(11));
          foglioVO.setDescrizioneAreaA(rs.getString(12));
          foglioVO.setDescrizioneAreaB(rs.getString(13));
          foglioVO.setDescrizioneAreaC(rs.getString(14));
          foglioVO.setDescrizioneAreaD(rs.getString(15));
          foglioVO.setDescrizioneAreaE(rs.getString(16));
          if (rs.getString(17) != null && !rs.getString(17).equals(""))
          {
            foglioVO.setFoglio(new Long(rs.getLong(17)));
          }
          foglioVO.setIstatComune(rs.getString(18));
          foglioVO.setDescrizioneComune(rs.getString(19));
          if (rs.getString(20) != null && !rs.getString(20).equals(""))
          {
            foglioVO.setIdZonaAltimetrica(new Long(rs.getLong(20)));
          }
          foglioVO.setDescrizioneZonaAltimetrica(rs.getString(21));
          elencoFogli.add(foglioVO);
        }
      }

      stmt.close();
      rs.close();

      if (elencoFogli == null || elencoFogli.size() == 0)
      {
        throw new SolmrException((String) AnagErrors.get("ERR_NO_FOGLI"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getFogliByComuneAndSezione - SQLException: "
          + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getFogliByComuneAndSezione - Generic Exception: " + ex);
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
                "getFogliByComuneAndSezione - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getFogliByComuneAndSezione - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoFogli;
  }

  // Metodo per recuperare l'elenco delle particelle in relazione al comune o
  // stato estero
  // e il foglio
  public Vector<ParticellaVO> getParticelleByParametri(
      String descrizioneComune, Long foglio, String sezione, Long particella,
      String flagEstinto) throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaVO> elencoParticelle = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT SP.ID_PARTICELLA, "
          + "        SP.ID_STORICO_PARTICELLA, "
          + "        SP.SEZIONE, "
          + "        SP.COMUNE, "
          + "        C.DESCOM, "
          + "        P.DATA_CREAZIONE, "
          + "        SP.FOGLIO, "
          + "        SP.DATA_FINE_VALIDITA, "
          + "        SP.PARTICELLA, "
          + "        SP.SUP_CATASTALE, "
          + "        SP.ID_AREA_A, "
          + "        TAA.DESCRIZIONE, "
          + "        SP.ID_AREA_B, "
          + "        TAB.DESCRIZIONE, "
          + "        SP.ID_AREA_C, "
          + "        TAC.DESCRIZIONE, "
          + "        SP.ID_AREA_D, "
          + "        TAD.DESCRIZIONE, "
          + "        SP.SUBALTERNO, "
          + "        SP.ID_ZONA_ALTIMETRICA, "
          + "        TZA.DESCRIZIONE, "
          + "        SP.FLAG_IRRIGABILE, "
          + "        SP.ID_CASO_PARTICOLARE, "
          + "        TCP.DESCRIZIONE, "
          + "        SP.DATA_AGGIORNAMENTO, "
          + "        SP.MOTIVO_MODIFICA, "
          + "        C.FLAG_ESTERO, "
          + "        SP.FLAG_CAPTAZIONE_POZZI, "
          + "        S.DESCRIZIONE "
          + " FROM   DB_STORICO_PARTICELLA SP, "
          + "        COMUNE C, "
          + "        DB_TIPO_AREA_A TAA, "
          + "        DB_TIPO_AREA_B TAB, "
          + "        DB_TIPO_AREA_C TAC, "
          + "        DB_TIPO_AREA_D TAD, "
          + "        DB_TIPO_ZONA_ALTIMETRICA TZA, "
          + "        DB_TIPO_CASO_PARTICOLARE TCP, "
          + "        DB_PARTICELLA P, "
          + "        DB_SEZIONE S "
          + " WHERE  SP.FOGLIO = ? "
          + " AND    SP.COMUNE = (SELECT C.ISTAT_COMUNE FROM  COMUNE C WHERE C.DESCOM = ? "
          + " AND    C.FLAG_CATASTO_ATTIVO = ? )"
          + " AND    SP.DATA_FINE_VALIDITA IS NULL "
          + " AND    SP.COMUNE = C.ISTAT_COMUNE "
          + " AND    SP.ID_AREA_A = TAA.ID_AREA_A(+) "
          + " AND    SP.ID_AREA_B = TAB.ID_AREA_B(+) "
          + " AND    SP.ID_AREA_C = TAC.ID_AREA_C(+) "
          + " AND    SP.ID_AREA_D = TAD.ID_AREA_D(+) "
          + " AND    SP.ID_ZONA_ALTIMETRICA = TZA.ID_ZONA_ALTIMETRICA(+) "
          + " AND    SP.ID_CASO_PARTICOLARE = TCP.ID_CASO_PARTICOLARE(+) "
          + " AND    P.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND    S.SEZIONE(+) = SP.SEZIONE "
          + " AND    S.ISTAT_COMUNE(+) = SP.COMUNE ";

      if (particella != null && !particella.equals(""))
      {
        query += "AND SP.PARTICELLA LIKE ? ";
      }

      if (sezione != null && !sezione.equals(""))
      {
        query += " AND SP.SEZIONE = ? ";
      }

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      int indiceStatement = 0;

      stmt.setLong(++indiceStatement, foglio.longValue());
      stmt.setString(++indiceStatement, descrizioneComune.toUpperCase());
      if (!Validator.isNotEmpty(flagEstinto)
          || (SolmrConstants.FLAG_N).equalsIgnoreCase(flagEstinto))
      {
        stmt.setString(++indiceStatement, SolmrConstants.FLAG_N);
      }
      else
      {
        stmt.setString(++indiceStatement, SolmrConstants.FLAG_S);
      }
      if (particella != null && !particella.equals(""))
      {
        stmt.setString(++indiceStatement, particella + "%");
      }
      if (sezione != null && !sezione.equals(""))
      {
        stmt.setString(++indiceStatement, sezione.toUpperCase());
      }

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        elencoParticelle = new Vector<ParticellaVO>();
        while (rs.next())
        {
          ParticellaVO particellaVO = new ParticellaVO();
          particellaVO.setIdParticella(new Long(rs.getLong(1)));
          particellaVO.setIdStoricoParticella(new Long(rs.getLong(2)));
          particellaVO.setSezione(rs.getString(3));
          particellaVO.setIstatComuneParticella(rs.getString(4));
          if (rs.getString(29) != null)
          {
            if (rs.getString(29).equalsIgnoreCase(SolmrConstants.FLAG_S))
            {
              particellaVO.setDescStatoEsteroParticella(rs.getString(5));
            }
            else
            {
              particellaVO.setDescComuneParticella(rs.getString(5));
            }
          }
          particellaVO.setDataCreazione(rs.getDate(6));
          particellaVO.setFoglio(new Long(rs.getLong(7)));
          particellaVO.setDataFineValidita(rs.getDate(8));
          if (rs.getString(9) != null && !rs.getString(9).equals(""))
          {
            particellaVO.setParticella(new Long(rs.getLong(9)));
          }
          particellaVO.setSupCatastale(rs.getString(10));
          if (rs.getString(11) != null && !rs.getString(11).equals(""))
          {
            particellaVO.setIdAreaA(new Long(rs.getString(11)));
          }
          particellaVO.setDescrizioneAreaA(rs.getString(12));
          if (rs.getString(13) != null && !rs.getString(13).equals(""))
          {
            particellaVO.setIdAreaB(new Long(rs.getString(13)));
          }
          particellaVO.setDescrizioneAreaB(rs.getString(14));
          if (rs.getString(15) != null && !rs.getString(15).equals(""))
          {
            particellaVO.setIdAreaC(new Long(rs.getString(15)));
          }
          particellaVO.setDescrizioneAreaC(rs.getString(16));
          if (rs.getString(17) != null && !rs.getString(17).equals(""))
          {
            particellaVO.setIdAreaD(new Long(rs.getString(17)));
          }
          particellaVO.setDescrizioneAreaD(rs.getString(18));
          particellaVO.setSubalterno(rs.getString(19));
          if (rs.getString(20) != null && !rs.getString(20).equals(""))
          {
            particellaVO.setIdZonaAltimetrica(new Long(rs.getLong(20)));
          }
          particellaVO.setDescrizioneZonaAltimetrica(rs.getString(21));
          if (rs.getString(22) != null)
          {
            if (rs.getString(22).equalsIgnoreCase(SolmrConstants.FLAG_S))
            {
              particellaVO.setFlagIrrigabile(true);
            }
            else
            {
              particellaVO.setFlagIrrigabile(false);
            }
          }
          if (rs.getString(23) != null && !rs.getString(23).equals(""))
          {
            particellaVO.setIdCasoParticolare(new Long(rs.getLong(23)));
          }
          particellaVO.setDescrizioneCasoParticolare(rs.getString(24));
          particellaVO.setDataAggiornamento(rs.getDate(25));
          particellaVO.setMotivoModifica(rs.getString(26));
          if (rs.getString(28) != null)
          {
            if (rs.getString(28).equalsIgnoreCase(SolmrConstants.FLAG_S))
            {
              particellaVO.setFlagCaptazionePozzi(true);
            }
            else
            {
              particellaVO.setFlagCaptazionePozzi(false);
            }
          }
          particellaVO.setDescrizioneSezione(rs.getString(29));
          elencoParticelle.add(particellaVO);
        }
      }

      stmt.close();
      rs.close();

      if (elencoParticelle == null || elencoParticelle.size() == 0)
      {
        throw new SolmrException((String) AnagErrors.get("ERR_NO_PARTICELLE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getParticelleByParametri - SQLException: "
          + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getParticelleByParametri - Generic Exception: "
          + ex);
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
                "getParticelleByParametri - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getParticelleByParametri - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoParticelle;
  }

  // Metodo per recuperare la sezione a partire dall'istat del comune e dalla
  // sezione stessa
  public String ricercaSezione(String istatComune, String sezione)
      throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    String sezioneParticella = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT SEZIONE FROM DB_SEZIONE WHERE ISTAT_COMUNE = ? AND SEZIONE = ?";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setString(1, istatComune);
      stmt.setString(2, sezione.toUpperCase());

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        while (rs.next())
        {
          sezioneParticella = rs.getString(1);
        }
      }

      stmt.close();
      rs.close();

      if (sezioneParticella == null)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_SEZIONE_INESISTENTE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "ricercaSezione - SQLException: "
          + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "ricercaSezione - Generic Exception: " + ex);
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
        SolmrLogger.fatal(this,
            "ricercaSezione - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "ricercaSezione - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return sezioneParticella;
  }

  // Metodo per recuperare il foglio in relazione al comune o stato estero, il
  // foglio
  // stesso ed eventualmente la sezione
  public FoglioVO ricercaFoglio(String istatComune, String sezione, Long foglio)
      throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    FoglioVO foglioVO = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT F.ID_FOGLIO, F.ID_CASO_PARTICOLARE, F.SEZIONE, "
          + " F.FOGLIO, F.COMUNE, F.ID_ZONA_ALTIMETRICA, F.ID_AREA_A, "
          + " F.ID_AREA_B, F.ID_AREA_C, F.ID_AREA_D, F.ID_AREA_E, "
          + " F.FLAG_CAPTAZIONE_POZZI, TAE.DESCRIZIONE "
          + " FROM DB_FOGLIO F, DB_TIPO_AREA_E TAE " + " WHERE F.COMUNE = ? "
          + " AND F.FOGLIO = ? " + " AND F.ID_AREA_E = TAE.ID_AREA_E(+)";

      if (sezione != null && !sezione.equals(""))
      {
        query += " AND F.SEZIONE = ? ";
      }

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setString(1, istatComune);
      stmt.setLong(2, foglio.longValue());
      if (sezione != null && !sezione.equals(""))
      {
        stmt.setString(3, sezione.toUpperCase());
      }

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        while (rs.next())
        {
          foglioVO = new FoglioVO();
          foglioVO.setIdFoglio(new Long(rs.getLong(1)));
          if (rs.getString(2) != null && !rs.getString(2).equals(""))
          {
            foglioVO.setIdCasoParticolare(new Long(rs.getLong(2)));
          }
          else
          {
            foglioVO.setIdCasoParticolare(null);
          }
          foglioVO.setSezione(rs.getString(3));
          if (rs.getString(4) != null && !rs.getString(4).equals(""))
          {
            foglioVO.setFoglio(new Long(rs.getLong(4)));
          }
          else
          {
            foglioVO.setFoglio(null);
          }
          foglioVO.setIstatComune(rs.getString(5));
          if (rs.getString(6) != null && !rs.getString(6).equals(""))
          {
            foglioVO.setIdZonaAltimetrica(new Long(rs.getLong(6)));
          }
          else
          {
            foglioVO.setIdZonaAltimetrica(null);
          }
          if (rs.getString(7) != null && !rs.getString(7).equals(""))
          {
            foglioVO.setIdAreaA(new Long(rs.getLong(7)));
          }
          else
          {
            foglioVO.setIdAreaA(null);
          }
          if (rs.getString(8) != null && !rs.getString(8).equals(""))
          {
            foglioVO.setIdAreaB(new Long(rs.getLong(8)));
          }
          else
          {
            foglioVO.setIdAreaB(null);
          }
          if (rs.getString(9) != null && !rs.getString(9).equals(""))
          {
            foglioVO.setIdAreaC(new Long(rs.getLong(9)));
          }
          else
          {
            foglioVO.setIdAreaC(null);
          }
          if (rs.getString(10) != null && !rs.getString(10).equals(""))
          {
            foglioVO.setIdAreaD(new Long(rs.getLong(10)));
          }
          else
          {
            foglioVO.setIdAreaD(null);
          }
          if (rs.getString(11) != null && !rs.getString(11).equals(""))
          {
            foglioVO.setIdAreaE(new Long(rs.getLong(11)));
          }
          else
          {
            foglioVO.setIdAreaE(null);
          }
          if (rs.getString(12) != null)
          {
            foglioVO.setFlagCaptazionePozzi(rs.getString(12));
          }
          else
          {
            foglioVO.setFlagCaptazionePozzi(SolmrConstants.FLAG_N);
          }
          foglioVO.setDescrizioneAreaE(rs.getString(13));
        }
      }

      stmt.close();
      rs.close();

      if (foglioVO == null)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_FOGLIO_INESISTENTE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "ricercaFoglio - SQLException: "
          + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "ricercaFoglio - Generic Exception: " + ex);
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
        SolmrLogger.fatal(this,
            "ricercaFoglio - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "ricercaFoglio - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return foglioVO;
  }

  // Metodo per recuperare la particella in relazione al comune o stato estero
  // e il foglio
  public ParticellaVO ricercaParticellaAttiva(String istatComune,
      String sezione, Long foglio, Long particella, String subalterno)
      throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    ParticellaVO particellaVO = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = 
        " SELECT SP.ID_PARTICELLA, " +
        "        SP.ID_STORICO_PARTICELLA, " + 
        "        SP.SEZIONE, " +
        "        SP.COMUNE, " + 
        "        C.DESCOM, " +
        "        P.DATA_CREAZIONE, " + 
        "        SP.FOGLIO, " + 
        "        SP.DATA_FINE_VALIDITA, " + 
        "        SP.PARTICELLA, " + 
        "        SP.SUP_CATASTALE, " + 
        "        SP.ID_AREA_A, " + 
        "        TAA.DESCRIZIONE, " + 
        "        SP.ID_AREA_B, " + 
        "        TAB.DESCRIZIONE, " + 
        "        SP.ID_AREA_C, " + 
        "        TAC.DESCRIZIONE, " + 
        "        SP.ID_AREA_D, " + 
        "        TAD.DESCRIZIONE, " + 
        "        SP.FLAG_CAPTAZIONE_POZZI, " + 
        "        SP.SUBALTERNO, " + 
        "        SP.ID_ZONA_ALTIMETRICA, " + 
        "        TZA.DESCRIZIONE, " + 
        "        SP.FLAG_IRRIGABILE, " + 
        "        SP.ID_CASO_PARTICOLARE, " + 
        "        TCP.DESCRIZIONE, " + 
        "        SP.DATA_AGGIORNAMENTO, " + 
        "        SP.ID_UTENTE_AGGIORNAMENTO, " + 
        "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
        "          || ' ' " + 
        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
        "         WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
        "        AS DENOMINAZIONE, " +  
        "        SP.MOTIVO_MODIFICA, " + 
        "        C.FLAG_ESTERO " + 
        " FROM   DB_STORICO_PARTICELLA SP, " + 
        "        COMUNE C, " + 
        "        DB_TIPO_AREA_A TAA, " + 
        "        DB_TIPO_AREA_B TAB, " + 
        "        DB_TIPO_AREA_C TAC, " + 
        "        DB_TIPO_AREA_D TAD, " + 
        "        DB_TIPO_ZONA_ALTIMETRICA TZA, " + 
        "        DB_TIPO_CASO_PARTICOLARE TCP, " + 
      //  "        PAPUA_V_UTENTE_LOGIN PVU, " + 
        "        DB_PARTICELLA P " + 
        " WHERE  SP.COMUNE = ? " + 
        " AND    SP.FOGLIO = ? " + 
        " AND    SP.PARTICELLA = ? " + 
        " AND    SP.DATA_FINE_VALIDITA IS NULL " +
        " AND    SP.COMUNE = C.ISTAT_COMUNE " +
        " AND    SP.ID_AREA_A = TAA.ID_AREA_A(+) " +
        " AND    SP.ID_AREA_B = TAB.ID_AREA_B(+) " +
        " AND    SP.ID_AREA_C = TAC.ID_AREA_C(+) " +
        " AND    SP.ID_AREA_D = TAD.ID_AREA_D(+) " +
        " AND    SP.ID_ZONA_ALTIMETRICA = TZA.ID_ZONA_ALTIMETRICA(+) " +
        " AND    SP.ID_CASO_PARTICOLARE = TCP.ID_CASO_PARTICOLARE(+) " +
       // " AND    SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
        " AND    P.ID_PARTICELLA = SP.ID_PARTICELLA ";

      if (sezione != null && !sezione.equals(""))
      {
        query += " AND SP.SEZIONE = ? ";
      }
      else
      {
        query += " AND SP.SEZIONE IS NULL ";
      }
      if (subalterno != null && !subalterno.equals(""))
      {
        query += " AND UPPER(SP.SUBALTERNO) = ? ";
      }
      else
      {
        query += " AND SP.SUBALTERNO IS NULL ";
      }

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setString(1, istatComune);
      stmt.setLong(2, foglio.longValue());
      stmt.setLong(3, particella.longValue());

      int indice = 4;

      if (sezione != null && !sezione.equals(""))
      {
        stmt.setString(indice, sezione.toUpperCase());
        indice++;
      }
      if (subalterno != null && !subalterno.equals(""))
      {
        stmt.setString(indice, subalterno.toUpperCase());
      }

      ResultSet rs = stmt.executeQuery();

      int i = 0;

      if (rs != null)
      {
        while (rs.next())
        {
          particellaVO = new ParticellaVO();
          particellaVO.setIdParticella(new Long(rs.getLong(1)));
          particellaVO.setIdStoricoParticella(new Long(rs.getLong(2)));
          particellaVO.setSezione(rs.getString(3));
          particellaVO.setIstatComuneParticella(rs.getString(4));
          if (rs.getString(29) != null)
          {
            if (rs.getString(29).equalsIgnoreCase(SolmrConstants.FLAG_S))
            {
              particellaVO.setDescStatoEsteroParticella(rs.getString(5));
            }
            else
            {
              particellaVO.setDescComuneParticella(rs.getString(5));
            }
          }
          particellaVO.setDataCreazione(rs.getDate(6));
          particellaVO.setFoglio(new Long(rs.getLong(7)));
          particellaVO.setDataFineValidita(rs.getDate(8));
          if (rs.getString(9) != null && !rs.getString(9).equals(""))
          {
            particellaVO.setParticella(new Long(rs.getLong(9)));
          }
          particellaVO.setSupCatastale(StringUtils.parseSuperficieField(rs
              .getString(10)));
          if (rs.getString(11) != null && !rs.getString(11).equals(""))
          {
            particellaVO.setIdAreaA(new Long(rs.getString(11)));
          }
          particellaVO.setDescrizioneAreaA(rs.getString(12));
          if (rs.getString(13) != null && !rs.getString(13).equals(""))
          {
            particellaVO.setIdAreaB(new Long(rs.getString(13)));
          }
          particellaVO.setDescrizioneAreaB(rs.getString(14));
          if (rs.getString(15) != null && !rs.getString(15).equals(""))
          {
            particellaVO.setIdAreaC(new Long(rs.getString(15)));
          }
          particellaVO.setDescrizioneAreaC(rs.getString(16));
          if (rs.getString(17) != null && !rs.getString(17).equals(""))
          {
            particellaVO.setIdAreaD(new Long(rs.getString(17)));
          }
          particellaVO.setDescrizioneAreaD(rs.getString(18));
          if (rs.getString(19) != null)
          {
            if (rs.getString(19).equalsIgnoreCase(SolmrConstants.FLAG_S))
            {
              particellaVO.setFlagCaptazionePozzi(true);
            }
            else
            {
              particellaVO.setFlagCaptazionePozzi(false);
            }
          }
          particellaVO.setSubalterno(rs.getString(20));
          if (rs.getString(21) != null && !rs.getString(21).equals(""))
          {
            particellaVO.setIdZonaAltimetrica(new Long(rs.getLong(21)));
          }
          particellaVO.setDescrizioneZonaAltimetrica(rs.getString(22));
          if (rs.getString(23) != null)
          {
            if (rs.getString(23).equalsIgnoreCase(SolmrConstants.FLAG_S))
            {
              particellaVO.setFlagIrrigabile(true);
            }
            else
            {
              particellaVO.setFlagIrrigabile(false);
            }
          }
          if (rs.getString(24) != null && !rs.getString(24).equals(""))
          {
            particellaVO.setIdCasoParticolare(new Long(rs.getLong(24)));
          }
          particellaVO.setDescrizioneCasoParticolare(rs.getString(25));
          particellaVO.setDataAggiornamento(rs.getDate(26));
          particellaVO.setIdUtenteAggiornamento(new Long(rs.getLong(27)));
          particellaVO.setDenominazioneUtenteAggiornamento(rs.getString(28));
          particellaVO.setMotivoModifica(rs.getString(29));
          i++;
        }
      }

      stmt.close();
      rs.close();

      if (particellaVO == null)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_PARTICELLA_INESISTENTE"));
      }
      if (particellaVO.getDataFineValidita() != null)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_PARTICELLA_CESSATA"));
      }
      if (i > 1)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_PARTICELLA_NON_UNIVOCA"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "ricercaParticellaAttiva - SQLException: "
          + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "ricercaParticellaAttiva - Generic Exception: "
          + ex);
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
                "ricercaParticellaAttiva - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "ricercaParticellaAttiva - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return particellaVO;
  }

  // Metodo per controllare che una particella non sia già attribuita ad una
  // azienda
  public void checkParticellaByAzienda(Long idParticella, Long idAzienda)
      throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    String idConduzioneParticella = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT ID_CONDUZIONE_PARTICELLA FROM DB_CONDUZIONE_PARTICELLA CP, "
          + " DB_UTE U "
          + " WHERE CP.ID_UTE = U.ID_UTE "
          + " AND CP.ID_PARTICELLA = ? "
          + " AND U.ID_AZIENDA = ? "
          + " AND DATA_FINE_CONDUZIONE IS NULL";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setLong(1, idParticella.longValue());
      stmt.setLong(2, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        while (rs.next())
        {
          idConduzioneParticella = rs.getString(1);
        }
      }

      stmt.close();
      rs.close();

      if (idConduzioneParticella != null)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_PARTICELLA_GIA_ATTRIBUITA"));
      }

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "checkParticellaByAzienda - SQLException: "
          + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "checkParticellaByAzienda - Generic Exception: "
          + ex);
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
                "checkParticellaByAzienda - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "checkParticellaByAzienda - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
  }

  // Metodo per recuperare l'elenco delle aziende che hanno in conduzione la
  // particella selezionata
  public Vector<ElencoAziendeParticellaVO> elencoAziendeByParticellaAndConduzione(
      Long idParticella, Long idAzienda) throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ElencoAziendeParticellaVO> elencoAziende = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT AA.DENOMINAZIONE, AA.CUAA, CP.ID_TITOLO_POSSESSO, "
          + "        TTP.DESCRIZIONE, CP.SUPERFICIE_CONDOTTA "
          + " FROM   DB_ANAGRAFICA_AZIENDA AA, DB_TIPO_TITOLO_POSSESSO TTP, "
          + "        DB_CONDUZIONE_PARTICELLA CP, DB_UTE U "
          + " WHERE  CP.ID_PARTICELLA = ? "
          + " AND    AA.ID_AZIENDA <> ? "
          + " AND    U.ID_UTE = CP.ID_UTE "
          + " AND    U.ID_AZIENDA = AA.ID_AZIENDA "
          + " AND    AA.DATA_FINE_VALIDITA IS NULL "
          + " AND    CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO "
          + " AND    CP.DATA_FINE_CONDUZIONE IS NULL ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setLong(1, idParticella.longValue());
      stmt.setLong(2, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        elencoAziende = new Vector<ElencoAziendeParticellaVO>();
        while (rs.next())
        {
          ElencoAziendeParticellaVO elencoAziendeParticellaVO = new ElencoAziendeParticellaVO();
          elencoAziendeParticellaVO.setDenominazione(rs.getString(1));
          elencoAziendeParticellaVO.setCuaa(rs.getString(2));
          elencoAziendeParticellaVO
              .setIdTitoloPossesso(new Long(rs.getLong(3)));
          elencoAziendeParticellaVO.setDescrizioneTitoloPossesso(rs
              .getString(4));
          elencoAziendeParticellaVO.setSuperficieCondotta(StringUtils
              .parseSuperficieField(rs.getString(5)));
          elencoAziende.add(elencoAziendeParticellaVO);
        }
      }

      stmt.close();
      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "elencoAziendeByParticellaAndConduzione - SQLException: "
              + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "elencoAziendeByParticellaAndConduzione - Generic Exception: " + ex);
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
                "elencoAziendeByParticellaAndConduzione - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "elencoAziendeByParticellaAndConduzione - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoAziende;
  }

  // Metodo per recuperare il valore dell'ultima data fine conduzione delle
  // particelle
  // associate ad un azienda agricola
  public java.util.Date getMaxDataFineConduzione(Long idParticella,
      Long idAzienda) throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    java.util.Date dataFineConduzione = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT MAX(CP.DATA_FINE_CONDUZIONE) FROM DB_CONDUZIONE_PARTICELLA CP, "
          + " DB_UTE U "
          + " WHERE CP.ID_UTE = U.ID_UTE "
          + " AND CP.ID_PARTICELLA = ? " + " AND U.ID_AZIENDA = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setLong(1, idParticella.longValue());
      stmt.setLong(2, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        while (rs.next())
        {
          dataFineConduzione = rs.getDate(1);
        }
      }

      stmt.close();
      rs.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getMaxDataFineConduzione - SQLException: "
          + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getMaxDataFineConduzione - Generic Exception: "
          + ex);
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
                "getMaxDataFineConduzione - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getMaxDataFineConduzione - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return dataFineConduzione;
  }

  // Metodo per recuperare i tipi utilizzo attivi
  public Vector<CodeDescription> getTipiUtilizzoAttivi() throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<CodeDescription> elencoTipiUtilizzo = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT ID_UTILIZZO, DESCRIZIONE, CODICE "
          + " FROM DB_TIPO_UTILIZZO " + " WHERE ANNO_FINE_VALIDITA IS NULL "
          +		"ORDER BY DESCRIZIONE ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        elencoTipiUtilizzo = new Vector<CodeDescription>();
        while (rs.next())
        {
          CodeDescription code = new CodeDescription();
          code.setCode(new Integer(rs.getInt("ID_UTILIZZO")));
          code.setDescription(rs.getString("DESCRIZIONE"));
          code.setSecondaryCode(rs.getString("CODICE"));
          elencoTipiUtilizzo.add(code);
        }
      }

      stmt.close();
      rs.close();

      if (elencoTipiUtilizzo == null || elencoTipiUtilizzo.size() == 0)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_NO_UTILIZZI_ATTIVI"));
      }

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTipiUtilizzoAttivi - SQLException: "
          + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getTipiUtilizzoAttivi - Generic Exception: "
          + ex);
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
        SolmrLogger.fatal(this,
            "getTipiUtilizzoAttivi - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getTipiUtilizzoAttivi - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoTipiUtilizzo;
  }

  // Metodo per recuperare i tipi utilizzo attivi dato un indirizzo
  public Vector<CodeDescription> getTipiUtilizzoAttivi(int idIndirizzo)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<CodeDescription> elencoTipiUtilizzo = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT ID_UTILIZZO, DESCRIZIONE, CODICE "
          + " FROM DB_TIPO_UTILIZZO " + " WHERE ANNO_FINE_VALIDITA IS NULL "
          + " AND ID_INDIRIZZO_UTILIZZO = ? " + " ORDER BY DESCRIZIONE ";

      stmt = conn.prepareStatement(query);

      stmt.setInt(1, idIndirizzo);

      SolmrLogger.debug(this, "Executing query: " + query);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        elencoTipiUtilizzo = new Vector<CodeDescription>();
        while (rs.next())
        {
          CodeDescription code = new CodeDescription();
          code.setCode(new Integer(rs.getInt(1)));
          code.setDescription(rs.getString(2));
          code.setSecondaryCode(rs.getString(3));
          elencoTipiUtilizzo.add(code);
        }
      }

      stmt.close();
      rs.close();

      if (elencoTipiUtilizzo == null || elencoTipiUtilizzo.size() == 0)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_NO_UTILIZZI_ATTIVI"));
      }

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTipiUtilizzoAttivi - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getTipiUtilizzoAttivi - Generic Exception: "
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
        SolmrLogger.fatal(this,
            "getTipiUtilizzoAttivi - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getTipiUtilizzoAttivi - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoTipiUtilizzo;
  }

  // Metodo per inserire un record in DB_PARTICELLA
  public Long insertParticella() throws SolmrException
  {

    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {

      primaryKey = getNextPrimaryKey((String) SolmrConstants
          .get("SEQ_PARTICELLA"));
      conn = getDatasource().getConnection();

      String insert = " INSERT INTO DB_PARTICELLA "
          + " (ID_PARTICELLA, DATA_CREAZIONE, DATA_CESSAZIONE, BIOLOGICO, "
          + " DATA_INIZIO_VALIDITA, FLAG_SCHEDARIO) "
          + " VALUES (?, SYSDATE, ?, ?, TRUNC(SYSDATE), 'N') ";
      stmt = conn.prepareStatement(insert);

      stmt.setLong(1, primaryKey.longValue());
      stmt.setString(2, null);
      stmt.setString(3, null);

      SolmrLogger.debug(this, "Executing insert: " + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insert.");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in insertParticella: "
          + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in insertParticella: "
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
        SolmrLogger.fatal(this,
            "SQLException while closing Statement and Connection in insertParticella: "
                + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in insertParticella: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return primaryKey;
  }



  // Metodo per recuperare la particella provvisoria in relazione al comune o
  // stato estero
  // e il foglio
  public ParticellaVO ricercaParticellaProvvisoriaAttiva(String istatComune,
      String sezione, Long foglio) throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    ParticellaVO particellaVO = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = 
        " SELECT SP.ID_PARTICELLA, " +
        "        SP.ID_STORICO_PARTICELLA, " +
        "        SP.SEZIONE, " +
        "        SP.COMUNE, " +
        "        C.DESCOM, " +
        "        P.DATA_CREAZIONE, " +
        "        SP.FOGLIO, " + 
        "        SP.DATA_FINE_VALIDITA, " +
        "        SP.PARTICELLA, " +
        "        SP.SUP_CATASTALE, " + 
        "        SP.ID_AREA_A, " +
        "        SP.ID_AREA_B, " +
        "        SP.ID_AREA_C, " +
        "        SP.ID_AREA_D, " + 
        "        SP.FLAG_CAPTAZIONE_POZZI, " +
        "        TAA.DESCRIZIONE, " +
        "        TAB.DESCRIZIONE, " + 
        "        TAC.DESCRIZIONE, " +
        "        TAD.DESCRIZIONE, " +
        "        SP.SUBALTERNO, " +
        "        SP.ID_ZONA_ALTIMETRICA, " + 
        "        TZA.DESCRIZIONE, " +
        "        SP.FLAG_IRRIGABILE, " +
        "        SP.ID_CASO_PARTICOLARE, " + 
        "        TCP.DESCRIZIONE, " +
        "        SP.DATA_AGGIORNAMENTO, " +
        "        SP.ID_UTENTE_AGGIORNAMENTO, " + 
        "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
        "          || ' ' " + 
        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
        "         WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
        "        AS DENOMINAZIONE, " +
        "        SP.MOTIVO_MODIFICA, " +
        "        C.FLAG_ESTERO " + 
        "FROM    DB_STORICO_PARTICELLA SP, " +
        "        COMUNE C, " +
        "        DB_TIPO_AREA_A TAA, " + 
        "        DB_TIPO_AREA_B TAB, " +
        "        DB_TIPO_AREA_C TAC, " +
        "        DB_TIPO_AREA_D TAD, " + 
        "        DB_TIPO_ZONA_ALTIMETRICA TZA, " +
        "        DB_TIPO_CASO_PARTICOLARE TCP, " + 
        //"        PAPUA_V_UTENTE_LOGIN PVU, " +
        "        DB_PARTICELLA P "  + 
        "WHERE   SP.COMUNE = ? " + 
        "AND     SP.FOGLIO = ? " + 
        "AND     SP.DATA_FINE_VALIDITA IS NULL " + 
        "AND     SP.PARTICELLA IS NULL " + 
        "AND     SP.COMUNE = C.ISTAT_COMUNE " + 
        "AND     SP.ID_AREA_A = TAA.ID_AREA_A(+) " + 
        "AND     SP.ID_AREA_B = TAB.ID_AREA_B(+) " + 
        "AND     SP.ID_AREA_C = TAC.ID_AREA_C(+) " + 
        "AND     SP.ID_AREA_D = TAD.ID_AREA_D(+) " + 
        "AND     SP.ID_ZONA_ALTIMETRICA = TZA.ID_ZONA_ALTIMETRICA(+) " + 
        "AND     SP.ID_CASO_PARTICOLARE = TCP.ID_CASO_PARTICOLARE(+) " + 
        //"AND     SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " + 
        "AND     P.ID_PARTICELLA = SP.ID_PARTICELLA ";

      if (sezione != null && !sezione.equals(""))
      {
        query += " AND SP.SEZIONE = ? ";
      }
      else
      {
        query += " AND SP.SEZIONE IS NULL ";
      }

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setString(1, istatComune);
      stmt.setLong(2, foglio.longValue());

      if (sezione != null && !sezione.equals(""))
      {
        stmt.setString(3, sezione.toUpperCase());
      }

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        while (rs.next())
        {
          particellaVO = new ParticellaVO();
          particellaVO.setIdParticella(new Long(rs.getLong(1)));
          particellaVO.setIdStoricoParticella(new Long(rs.getLong(2)));
          particellaVO.setSezione(rs.getString(3));
          particellaVO.setIstatComuneParticella(rs.getString(4));
          if (rs.getString(30) != null)
          {
            if (rs.getString(30).equalsIgnoreCase(SolmrConstants.FLAG_S))
            {
              particellaVO.setDescStatoEsteroParticella(rs.getString(5));
            }
            else
            {
              particellaVO.setDescComuneParticella(rs.getString(5));
            }
          }
          particellaVO.setDataCreazione(rs.getDate(6));
          particellaVO.setFoglio(new Long(rs.getLong(7)));
          particellaVO.setDataFineValidita(rs.getDate(8));
          if (rs.getString(9) != null && !rs.getString(9).equals(""))
          {
            particellaVO.setParticella(new Long(rs.getLong(9)));
          }
          particellaVO.setSupCatastale(rs.getString(10));
          if (rs.getString(11) != null && !rs.getString(11).equals(""))
          {
            particellaVO.setIdAreaA(new Long(rs.getString(11)));
          }
          if (rs.getString(12) != null && !rs.getString(12).equals(""))
          {
            particellaVO.setIdAreaB(new Long(rs.getString(12)));
          }
          if (rs.getString(13) != null && !rs.getString(13).equals(""))
          {
            particellaVO.setIdAreaC(new Long(rs.getString(13)));
          }
          if (rs.getString(14) != null && !rs.getString(14).equals(""))
          {
            particellaVO.setIdAreaD(new Long(rs.getString(14)));
          }
          if (rs.getString(15) == null
              || rs.getString(15).equalsIgnoreCase(SolmrConstants.FLAG_N))
          {
            particellaVO.setFlagCaptazionePozzi(false);
          }
          else
          {
            particellaVO.setFlagCaptazionePozzi(true);
          }
          particellaVO.setDescrizioneAreaA(rs.getString(16));
          particellaVO.setDescrizioneAreaB(rs.getString(17));
          particellaVO.setDescrizioneAreaC(rs.getString(18));
          particellaVO.setDescrizioneAreaD(rs.getString(19));
          particellaVO.setSubalterno(rs.getString(20));
          if (rs.getString(21) != null && !rs.getString(21).equals(""))
          {
            particellaVO.setIdZonaAltimetrica(new Long(rs.getLong(21)));
          }
          particellaVO.setDescrizioneZonaAltimetrica(rs.getString(22));
          if (rs.getString(23) != null)
          {
            if (rs.getString(23).equalsIgnoreCase(SolmrConstants.FLAG_S))
            {
              particellaVO.setFlagIrrigabile(true);
            }
            else
            {
              particellaVO.setFlagIrrigabile(false);
            }
          }
          if (rs.getString(24) != null && !rs.getString(24).equals(""))
          {
            particellaVO.setIdCasoParticolare(new Long(rs.getLong(24)));
          }
          particellaVO.setDescrizioneCasoParticolare(rs.getString(25));
          particellaVO.setDataAggiornamento(rs.getDate(26));
          particellaVO.setIdUtenteAggiornamento(new Long(rs.getLong(27)));
          particellaVO.setDenominazioneUtenteAggiornamento(rs.getString(28));
          particellaVO.setMotivoModifica(rs.getString(29));
        }
      }

      stmt.close();
      rs.close();

      if (particellaVO == null)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_PARTICELLA_INESISTENTE"));
      }
      if (particellaVO.getDataFineValidita() != null)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_PARTICELLA_CESSATA"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "ricercaParticellaProvvisoriaAttiva - SQLException: "
              + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "ricercaParticellaProvvisoriaAttiva - Generic Exception: " + ex);
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
                "ricercaParticellaProvvisoriaAttiva - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "ricercaParticellaProvvisoriaAttiva - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return particellaVO;
  }

  // Metodo per recuperare la particella in relazione al comune o stato estero
  // la sezione e il foglio indipendentemente dal fatto che sia attiva o meno
  public ParticellaVO ricercaParticella(String istatComune, String sezione,
      Long foglio, Long particella, String subalterno) throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    ParticellaVO particellaVO = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = 
        "SELECT SP.ID_PARTICELLA, " +
        "       SP.ID_STORICO_PARTICELLA, " +
        "       SP.SEZIONE, " + 
        "       SP.COMUNE, " +
        "       C.DESCOM, " +
        "       P.DATA_CREAZIONE, " +
        "       SP.FOGLIO, " + 
        "       SP.DATA_FINE_VALIDITA, " +
        "       SP.PARTICELLA, " +
        "       SP.SUP_CATASTALE, " + 
        "       SP.ID_AREA, " +
        "       TA.DESCRIZIONE, " +
        "       SP.SUBALTERNO, " +
        "       SP.ID_ZONA_ALTIMETRICA, " + 
        "       TZA.DESCRIZIONE, " +
        "       SP.FLAG_IRRIGABILE, " +
        "       SP.ID_CASO_PARTICOLARE, " + 
        "       TCP.DESCRIZIONE, " +
        "       SP.DATA_AGGIORNAMENTO, " +
        "       SP.ID_UTENTE_AGGIORNAMENTO, " + 
        "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
        "          || ' ' " + 
        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
        "         WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
        "       AS DENOMINAZIONE, " +
        "       SP.MOTIVO_MODIFICA, " +
        "       C.FLAG_ESTERO " + 
        "FROM   DB_STORICO_PARTICELLA SP, " +
        "       COMUNE C, " +
        "       DB_TIPO_AREA TA, " + 
        "       DB_TIPO_ZONA_ALTIMETRICA TZA, " +
        "       DB_TIPO_CASO_PARTICOLARE TCP, "  +
        //"       PAPUA_V_UTENTE_LOGIN PVU, " +
        "       DB_PARTICELLA P " +
        "WHERE  SP.COMUNE = ? " + 
        "AND    SP.FOGLIO = ? " + 
        "AND    SP.PARTICELLA = ? " + 
        "AND    SP.COMUNE = C.ISTAT_COMUNE " + 
        "AND    SP.ID_AREA = TA.ID_AREA(+) " + 
        "AND    SP.ID_ZONA_ALTIMETRICA = TZA.ID_ZONA_ALTIMETRICA(+) " + 
        "AND    SP.ID_CASO_PARTICOLARE = TCP.ID_CASO_PARTICOLARE(+) " + 
        //"AND    SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " + 
        "AND    SP.DATA_FINE_VALIDITA IS NULL " + 
        "AND    P.ID_PARTICELLA = SP.ID_PARTICELLA";

      if (sezione != null && !sezione.equals(""))
      {
        query += " AND SP.SEZIONE = ? ";
      }
      if (subalterno != null && !subalterno.equals(""))
      {
        query += " AND SP.SUBALTERNO = ? ";
      }
      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setString(1, istatComune);
      stmt.setLong(2, foglio.longValue());
      stmt.setLong(3, particella.longValue());

      int indice = 4;

      if (sezione != null && !sezione.equals(""))
      {
        stmt.setString(indice, sezione.toUpperCase());
        indice++;
      }
      if (subalterno != null && !subalterno.equals(""))
      {
        stmt.setString(indice, subalterno.toUpperCase());
      }

      ResultSet rs = stmt.executeQuery();

      int i = 0;

      if (rs != null)
      {
        while (rs.next())
        {
          particellaVO = new ParticellaVO();
          particellaVO.setIdParticella(new Long(rs.getLong(1)));
          particellaVO.setIdStoricoParticella(new Long(rs.getLong(2)));
          particellaVO.setSezione(rs.getString(3));
          particellaVO.setIstatComuneParticella(rs.getString(4));
          if (rs.getString(23) != null)
          {
            if (rs.getString(23).equalsIgnoreCase(SolmrConstants.FLAG_S))
            {
              particellaVO.setDescStatoEsteroParticella(rs.getString(5));
            }
            else
            {
              particellaVO.setDescComuneParticella(rs.getString(5));
            }
          }
          particellaVO.setDataCreazione(rs.getDate(6));
          particellaVO.setFoglio(new Long(rs.getLong(7)));
          particellaVO.setStrFoglio(rs.getString(7));
          particellaVO.setDataFineValidita(rs.getDate(8));
          if (rs.getString(9) != null && !rs.getString(9).equals(""))
          {
            particellaVO.setParticella(new Long(rs.getLong(9)));
          }
          particellaVO.setStrParticella(rs.getString(9));
          particellaVO.setSupCatastale(rs.getString(10));
          if (rs.getString(11) != null && !rs.getString(11).equals(""))
          {
            particellaVO.setIdAreaA(new Long(rs.getString(11)));
          }
          particellaVO.setDescrizioneAreaA(rs.getString(12));
          particellaVO.setSubalterno(rs.getString(13));
          if (rs.getString(14) != null && !rs.getString(14).equals(""))
          {
            particellaVO.setIdZonaAltimetrica(new Long(rs.getLong(14)));
          }
          particellaVO.setDescrizioneZonaAltimetrica(rs.getString(15));
          if (rs.getString(16) != null)
          {
            if (rs.getString(16).equalsIgnoreCase(SolmrConstants.FLAG_S))
            {
              particellaVO.setFlagIrrigabile(true);
            }
            else
            {
              particellaVO.setFlagIrrigabile(false);
            }
          }
          if (rs.getString(17) != null && !rs.getString(17).equals(""))
          {
            particellaVO.setIdCasoParticolare(new Long(rs.getLong(17)));
          }
          particellaVO.setDescrizioneCasoParticolare(rs.getString(18));
          particellaVO.setDataAggiornamento(rs.getDate(19));
          particellaVO.setIdUtenteAggiornamento(new Long(rs.getLong(20)));
          particellaVO.setDenominazioneUtenteAggiornamento(rs.getString(21));
          particellaVO.setMotivoModifica(rs.getString(22));
          i++;
        }
      }

      stmt.close();
      rs.close();

      if (particellaVO == null)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_PARTICELLA_INESISTENTE"));
      }
      if (particellaVO.getDataFineValidita() != null)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_PARTICELLA_CESSATA"));
      }
      if (i > 1)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_PARTICELLA_NON_UNIVOCA"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "ricercaParticella - SQLException: "
          + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "ricercaParticella - Generic Exception: " + ex);
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
        SolmrLogger.fatal(this,
            "ricercaParticella - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "ricercaParticella - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return particellaVO;
  }

  // Metodo per cessare un range di particelle in DB_PARTICELLA tramite
  // id_storico_particella
  public void cessaParticelleByIdParticellaRange(long idParticella[])
      throws DataAccessException
  {
    SolmrLogger.debug(this,
        "[FascicoloDAO::cessaParticelleByIdParticellaRange] BEGIN.");
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " UPDATE DB_PARTICELLA P "
          + " SET    DATA_CESSAZIONE = SYSDATE "
          + " WHERE  P.ID_PARTICELLA IN ("
          + getIdListFromArrayForSQL(idParticella) + ")";

      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this,
          "[FascicoloDAO::cessaParticelleByIdParticellaRange]  Executing query: "
              + query);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger
          .debug(this,
              "[FascicoloDAO::cessaParticelleByIdParticellaRange]  query executed.");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "[FascicoloDAO::cessaParticelleByIdParticellaRange] "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in cessaParticelleByIdParticellaRange: "
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
                "SQLException while closing Statement and Connection in cessaParticelleByIdParticellaRange: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in cessaParticelleByIdParticellaRange: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
      SolmrLogger.debug(this,
          "[FascicoloDAO::cessaParticelleByIdParticellaRange] END.");
    }
  }

  // Metodo verificare se la particella selezionata possiede delle conduzioni
  // attive
  public int countParticelleConConduzioniAttive(long idParticella[])
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    int hasConduzioniAttive = 0;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT COUNT(DISTINCT CP.ID_PARTICELLA) AS COUNT_PARTICELLE "
          + " FROM   DB_CONDUZIONE_PARTICELLA CP "
          + " WHERE  "
          + " CP.DATA_FINE_CONDUZIONE IS NULL "
          + " AND    CP.ID_PARTICELLA IN ("
          + getIdListFromArrayForSQL(idParticella) + ")";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing hasParticellaConduzioniAttive: "
          + query);

      ResultSet rs = stmt.executeQuery();

      // Recupero i records estratti dalla query
      if (rs.next())
      {
        hasConduzioniAttive = rs.getInt("COUNT_PARTICELLE");
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "countParticelleConConduzioniAttive - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "countParticelleConConduzioniAttive - Generic Exception: " + ex);
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
                "countParticelleConConduzioniAttive - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "countParticelleConConduzioniAttive - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return hasConduzioniAttive;
  }

  // Metodo per recuperare il valore massimo inseribile per una particella non
  // presente in archivio
  // nata dal frazionamento di una particella già esistente in archivio che
  // presenta già una
  // particella nata dal suo frazionamento.....(Per chi lo legge in futuro so
  // che è complesso ma io l'ho
  // capito....) :)
  public double getMaxSupCatastaleInseribile(Long idParticella)
      throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    double maxSupCatastale = 0;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT SP2.SUP_CATASTALE - SUM(SP.SUP_CATASTALE) "
          + " FROM DB_STORICO_PARTICELLA SP, DB_STORICO_PARTICELLA SP2, "
          + " DB_EVENTO_PARTICELLA EP "
          + " WHERE EP.ID_PARTICELLA_EVENTO = SP.ID_PARTICELLA "
          + " AND EP.ID_PARTICELLA_LEGAME = SP2.ID_PARTICELLA "
          + " AND EP.ID_PARTICELLA_LEGAME = ? "
          + " GROUP BY SP2.SUP_CATASTALE ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing getMaxSupCatastaleInseribile: "
          + query);

      stmt.setLong(1, idParticella.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        if (rs.next())
        {
          String res = rs.getString(1);
          if (res != null)
          {
            maxSupCatastale = rs.getDouble(1);
          }
        }
        else
        {
          throw new SolmrException((String) AnagErrors
              .get("ERR_PARTICELLE_FRAZIONATE_NO_ESISTENTI"));
        }
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getMaxSupCatastaleInseribile - SQLException: "
          + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (SolmrException se)
    {
      SolmrLogger.fatal(this, "getMaxSupCatastaleInseribile - SolmrException: "
          + se.getMessage());
      throw new SolmrException(se.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getMaxSupCatastaleInseribile - Generic Exception: " + ex);
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
                "getMaxSupCatastaleInseribile - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getMaxSupCatastaleInseribile - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return maxSupCatastale;
  }

  // Metodo per recuperare la particella a partire dalla sua chiave primaria
  public ParticellaVO findParticellaByPrimaryKey(Long idStoricoParticella)
      throws SolmrException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ParticellaVO particellaVO = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT SP.ID_PARTICELLA, "
          + "        SP.ID_STORICO_PARTICELLA, " + "        SP.SEZIONE, "
          + "        SP.COMUNE, " + "        SP.FOGLIO, "
          + "        SP.DATA_INIZIO_VALIDITA, "
          + "        SP.DATA_FINE_VALIDITA, " + "        SP.PARTICELLA, "
          + "        SP.SUP_CATASTALE, " + "        SP.ID_AREA_A, "
          + "        SP.ID_AREA_B, " + "        SP.ID_AREA_C, "
          + "        SP.ID_AREA_D, " + "        TAA.DESCRIZIONE, "
          + "        TAB.DESCRIZIONE, " + "        TAC.DESCRIZIONE, "
          + "        TAD.DESCRIZIONE, " + "        SP.SUBALTERNO, "
          + "        SP.ID_ZONA_ALTIMETRICA, " + "        SP.FLAG_IRRIGABILE, "
          + "        SP.ID_CASO_PARTICOLARE, "
          + "        SP.DATA_AGGIORNAMENTO, "
          + "        SP.ID_UTENTE_AGGIORNAMENTO, "
          + "        SP.MOTIVO_MODIFICA, " + "        C.DESCOM, "
          + "        CP.SUPERFICIE_CONDOTTA, " + "        C1.DESCOM, "
          + "        P.SIGLA_PROVINCIA, " + "        TTP.DESCRIZIONE "
          + " FROM   DB_STORICO_PARTICELLA SP, " + "        DB_PARTICELLA P, "
          + "        DB_TIPO_AREA_A TAA, " + "        DB_TIPO_AREA_B TAB, "
          + "        DB_TIPO_AREA_C TAC, " + "        DB_TIPO_AREA_D TAD, "
          + "        COMUNE C, " + "        DB_CONDUZIONE_PARTICELLA CP, "
          + "        DB_UTE U, " + "        COMUNE C1, "
          + "        PROVINCIA P, " + "        DB_TIPO_TITOLO_POSSESSO TTP "
          + " WHERE  SP.ID_STORICO_PARTICELLA = ? "
          + " AND    P.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND    U.ID_UTE = CP.ID_UTE "
          + " AND    U.COMUNE = C.ISTAT_COMUNE "
          + " AND    SP.COMUNE = C1.ISTAT_COMUNE "
          + " AND    C1.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
          + " AND    SP.ID_AREA_A = TAA.ID_AREA_A(+) "
          + " AND    SP.ID_AREA_B = TAB.ID_AREA_B(+) "
          + " AND    SP.ID_AREA_C = TAC.ID_AREA_C(+) "
          + " AND    SP.ID_AREA_D = TAD.ID_AREA_D(+) "
          + " AND    CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query findParticellaByPrimaryKey: "
          + query);

      stmt.setLong(1, idStoricoParticella.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        while (rs.next())
        {
          particellaVO = new ParticellaVO();
          particellaVO.setIdParticella(new Long(rs.getLong(1)));
          particellaVO.setIdStoricoParticella(new Long(rs.getLong(2)));
          particellaVO.setSezione(rs.getString(3));
          particellaVO.setIstatComuneParticella(rs.getString(4));
          particellaVO.setFoglio(new Long(rs.getLong(5)));
          particellaVO.setStrFoglio(rs.getString(5));
          particellaVO.setDataInizioValidita(rs.getDate(6));
          particellaVO.setDataFineValidita(rs.getDate(7));
          if (rs.getString(8) != null && !rs.getString(8).equals(""))
          {
            particellaVO.setParticella(new Long(rs.getLong(8)));
          }
          particellaVO.setStrParticella(rs.getString(8));
          particellaVO.setSupCatastale(StringUtils.parseSuperficieField(rs
              .getString(9)));
          if (rs.getString(10) != null && !rs.getString(10).equals(""))
          {
            particellaVO.setIdAreaA(new Long(rs.getString(10)));
          }
          if (rs.getString(11) != null && !rs.getString(11).equals(""))
          {
            particellaVO.setIdAreaB(new Long(rs.getString(11)));
          }
          if (rs.getString(12) != null && !rs.getString(12).equals(""))
          {
            particellaVO.setIdAreaC(new Long(rs.getString(12)));
          }
          if (rs.getString(13) != null && !rs.getString(13).equals(""))
          {
            particellaVO.setIdAreaD(new Long(rs.getString(13)));
          }
          particellaVO.setDescrizioneAreaA(rs.getString(14));
          particellaVO.setDescrizioneAreaB(rs.getString(15));
          particellaVO.setDescrizioneAreaC(rs.getString(16));
          particellaVO.setDescrizioneAreaD(rs.getString(17));
          particellaVO.setSubalterno(rs.getString(18));
          if (rs.getString(19) != null && !rs.getString(19).equals(""))
          {
            particellaVO.setIdZonaAltimetrica(new Long(rs.getLong(19)));
          }
          if (rs.getString(20) != null)
          {
            if (rs.getString(20).equalsIgnoreCase(SolmrConstants.FLAG_S))
            {
              particellaVO.setFlagIrrigabile(true);
            }
            else
            {
              particellaVO.setFlagIrrigabile(false);
            }
          }
          if (rs.getString(21) != null && !rs.getString(21).equals(""))
          {
            particellaVO.setIdCasoParticolare(new Long(rs.getLong(21)));
          }
          particellaVO.setDataAggiornamento(rs.getDate(22));
          particellaVO.setIdUtenteAggiornamento(new Long(rs.getLong(23)));
          particellaVO.setMotivoModifica(rs.getString(24));
          particellaVO.setDescrizioneUnitaProduttiva(rs.getString(25));
          particellaVO.setSuperficieCondotta(StringUtils
              .parseSuperficieField(rs.getString(26)));
          particellaVO.setDescComuneParticella(rs.getString(27));
          particellaVO.setSiglaProvinciaParticella(rs.getString(28));
          particellaVO.setDescrizioneTitoloPossesso(rs.getString(29));
        }

        stmt.close();
        rs.close();

        if (particellaVO == null)
        {
          throw new SolmrException((String) AnagErrors
              .get("ERR_PARTICELLA_INESISTENTE"));
        }
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "findParticellaByPrimaryKey - SQLException: "
          + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "findParticellaByPrimaryKey - Generic Exception: " + ex);
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
                "findParticellaByPrimaryKey - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "findParticellaByPrimaryKey - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return particellaVO;
  }

  // Metodo per recuperare l'elenco delle particelle ad uso fabbricato associate
  // ad una
  // unita produttiva
  public Vector<ParticellaVO> getElencoParticelleFabbricatoByUte(Long idUte,
      boolean serra) throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaVO> elencoParticelleFabbricato = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT DISTINCT SP.ID_PARTICELLA, SP.ID_STORICO_PARTICELLA, SP.COMUNE, "
          + " C.DESCOM, SP.SEZIONE, SP.FOGLIO, SP.PARTICELLA, SP.SUBALTERNO, "
          + " SP.SUP_CATASTALE, P.SIGLA_PROVINCIA "
          + " FROM DB_STORICO_PARTICELLA SP, COMUNE C, "
          + " DB_CONDUZIONE_PARTICELLA CP, DB_TIPO_TITOLO_POSSESSO TTP, "
          + " DB_UTILIZZO_PARTICELLA UP,DB_TIPO_UTILIZZO TU, PROVINCIA P "
          + " WHERE CP.ID_UTE = ? "
          + " AND CP.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND SP.DATA_FINE_VALIDITA IS NULL "
          + " AND UP.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA "
          + " AND SP.COMUNE = C.ISTAT_COMUNE "
          + " AND CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO "
          + " AND CP.DATA_FINE_CONDUZIONE IS NULL "
          + " AND UP.ID_UTILIZZO = TU.ID_UTILIZZO "
          + " AND C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
      	  + " AND CP.ID_TITOLO_POSSESSO <> 5 "
          + " AND CP.ID_TITOLO_POSSESSO <> 6 ";

     // " AND (UP.ANNO <= ? OR UP.ANNO IS NULL) ";
      if (serra)
      {
        query += " AND ( TU.TIPO = ? OR TU.FLAG_SERRA = ?) ";
      }
      else
      {
        query += " AND TU.TIPO = ? ";
      }

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing query getElencoParticelleFabbricatoByUte: " + query);

      stmt.setLong(1, idUte.longValue());
      // stmt.setInt(2, DateUtils.getCurrentYear().intValue());
      stmt.setString(2, ((String) SolmrConstants
          .get("TIPO_UTILIZZO_FABBRICATO")));
      if (serra)
        stmt.setString(3, ((String) SolmrConstants
            .get("TIPO_UTILIZZO_FABBRICATO_SERRA")));

      ResultSet rs = stmt.executeQuery();

      elencoParticelleFabbricato = new Vector<ParticellaVO>();
      while (rs.next())
      {
        ParticellaVO particellaVO = new ParticellaVO();
        particellaVO.setIdParticella(new Long(rs.getLong(1)));
        particellaVO.setIdStoricoParticella(new Long(rs.getLong(2)));
        particellaVO.setIstatComuneParticella(rs.getString(3));
        particellaVO.setDescComuneParticella(rs.getString(4));
        particellaVO.setSezione(rs.getString(5));
        particellaVO.setFoglio(new Long(rs.getLong(6)));
        particellaVO.setStrFoglio(rs.getString(6));
        if (rs.getString(7) != null && !rs.getString(7).equals(""))
        {
          particellaVO.setParticella(new Long(rs.getLong(7)));
        }
        particellaVO.setStrParticella(rs.getString(7));
        particellaVO.setSubalterno(rs.getString(8));
        particellaVO.setSupCatastale(rs.getString(9));
        particellaVO.setSiglaProvinciaParticella(rs.getString(10));
        elencoParticelleFabbricato.add(particellaVO);
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getElencoParticelleFabbricatoByUte - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getElencoParticelleFabbricatoByUte - Generic Exception: " + ex);
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
                "getElencoParticelleFabbricatoByUte - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getElencoParticelleFabbricatoByUte - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoParticelleFabbricato;
  }

  // Metodo per recuperare la somma delle superfici dei fabbricati che insistono
  // esclusivamente
  // sulla particella selezionata
  public String getSuperficiFabbricatiByParticella(Long idUte, Long idParticella)
      throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    String sommaSuperfici = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT SUM(SUPERFICIE) "
          + " FROM DB_FABBRICATO "
          + " WHERE ID_FABBRICATO IN "
          + " (SELECT PF.ID_FABBRICATO "
          + " FROM DB_CONDUZIONE_PARTICELLA CP, DB_UTILIZZO_PARTICELLA UP, "
          + " DB_FABBRICATO_PARTICELLA PF "
          + " WHERE CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA "
          + " AND UP.ID_UTILIZZO_PARTICELLA = PF.ID_UTILIZZO_PARTICELLA "
          + " AND CP.DATA_FINE_CONDUZIONE IS NULL "
          + " AND CP.ID_PARTICELLA = ? "
          + " AND CP.ID_UTE = ? "
          + " AND PF.DATA_FINE_VALIDITA IS NULL "
          + " MINUS SELECT C.ID_FABBRICATO "
          + " FROM DB_CONDUZIONE_PARTICELLA A, DB_UTILIZZO_PARTICELLA B, DB_FABBRICATO_PARTICELLA C "
          + " WHERE A.ID_CONDUZIONE_PARTICELLA = B.ID_CONDUZIONE_PARTICELLA "
          + " AND B.ID_UTILIZZO_PARTICELLA = C.ID_UTILIZZO_PARTICELLA "
          + " AND A.DATA_FINE_CONDUZIONE IS NULL "
          + " AND A.ID_PARTICELLA <> ? " + " AND C.DATA_FINE_VALIDITA IS NULL)";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing query getSuperficiFabbricatiByParticella: " + query);

      stmt.setLong(1, idUte.longValue());
      stmt.setLong(2, idParticella.longValue());
      stmt.setLong(3, idParticella.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        while (rs.next())
        {
          sommaSuperfici = rs.getString(1);
        }
        stmt.close();
        rs.close();
      }
      if (sommaSuperfici == null || sommaSuperfici.equals(""))
      {
        sommaSuperfici = "0";
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getSuperficiFabbricatiByParticella - SQLException: "
              + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getSuperficiFabbricatiByParticella - Generic Exception: " + ex);
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
                "getSuperficiFabbricatiByParticella - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getSuperficiFabbricatiByParticella - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return sommaSuperfici;
  }

  // Metodo per inserire un record su DB_FABBRICATO
  public Long insertFabbricato(FabbricatoVO fabbricatoVO, long idUtenteAggiornamento)
      throws DataAccessException
  {

    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {

      primaryKey = getNextPrimaryKey((String) SolmrConstants
          .get("SEQ_FABBRICATO"));
      conn = getDatasource().getConnection();

      String insert = "INSERT INTO DB_FABBRICATO "
          + "(ID_FABBRICATO, ID_UTE, ID_TIPOLOGIA_FABBRICATO, ID_FORMA_FABBRICATO, "
          + "DENOMINAZIONE, SUPERFICIE, ANNO_COSTRUZIONE, DIMENSIONE, "
          + "LUNGHEZZA, LARGHEZZA, DATA_AGGIORNAMENTO, ALTEZZA, "
          + "UTM_X, UTM_Y, NOTE, "
          + "ID_UTENTE_AGGIORNAMENTO, DATA_INIZIO_VALIDITA, "
          + "ID_COLTURA_SERRA,MESI_RISCALDAMENTO_SERRA, ORE_RISCALDAMENTO_SERRA, "
          + "VOLUME_UTILE_PRESUNTO, SUPERFICIE_COPERTA, SUPERFICIE_SCOPERTA, SUPERFICIE_SCOPERTA_EXTRA)"
          + "  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?, ?, ?, ?, ?, SYSDATE,?,?,?,?,?,?,?) ";

      stmt = conn.prepareStatement(insert);

      stmt.setLong(1, primaryKey.longValue());
      stmt
          .setLong(2, fabbricatoVO.getIdUnitaProduttivaFabbricato().longValue());
      stmt.setLong(3, fabbricatoVO.getIdTipologiaFabbricato().longValue());
      stmt.setString(4, fabbricatoVO.getTipiFormaFabbricato());
      if (fabbricatoVO.getDenominazioneFabbricato() != null)
      {
        stmt.setString(5, fabbricatoVO.getDenominazioneFabbricato()
            .toUpperCase());
      }
      else
      {
        stmt.setString(5, null);
      }
      stmt.setDouble(6, Double.parseDouble(Validator.validateDouble(
          fabbricatoVO.getSuperficieFabbricato(), 9999999999.9999)));
      stmt.setString(7, fabbricatoVO.getAnnoCostruzioneFabbricato());
      stmt.setDouble(8, Double.parseDouble(Validator.validateDouble(
          fabbricatoVO.getDimensioneFabbricato(), 9999999999.9999)));
      if (fabbricatoVO.getLunghezzaFabbricato() != null
          && !fabbricatoVO.getLunghezzaFabbricato().equals(""))
      {
        stmt.setDouble(9, Double.parseDouble(Validator.validateDouble(
            fabbricatoVO.getLunghezzaFabbricato(), 99999.99)));
      }
      else
      {
        stmt.setString(9, null);
      }

      if (fabbricatoVO.getLarghezzaFabbricato() != null
          && !fabbricatoVO.getLarghezzaFabbricato().equals(""))
      {
        stmt.setDouble(10, Double.parseDouble(Validator.validateDouble(
            fabbricatoVO.getLarghezzaFabbricato(), 99999.99)));
      }
      else
      {
        stmt.setString(10, null);
      }
      if (fabbricatoVO.getAltezzaFabbricato() != null
          && !fabbricatoVO.getAltezzaFabbricato().equals(""))
      {
        stmt.setDouble(11, Double.parseDouble(Validator
            .validateDoubleIncludeZero(fabbricatoVO.getAltezzaFabbricato(),
                99999.99)));
      }
      else
      {
        stmt.setString(11, null);
      }
      stmt.setString(12, fabbricatoVO.getUtmx());
      stmt.setString(13, fabbricatoVO.getUtmy());
      stmt.setString(14, fabbricatoVO.getNoteFabbricato());

      stmt.setLong(15, idUtenteAggiornamento);

      if (fabbricatoVO.getTipologiaColturaSerra() != null
          && !"".equals(fabbricatoVO.getTipologiaColturaSerra()))
      {
        stmt.setLong(16, Long
            .parseLong(fabbricatoVO.getTipologiaColturaSerra()));// ID_COLTURA_SERRA
      }
      else
      {
        stmt.setString(16, null);
      }
      if (fabbricatoVO.getMesiRiscSerra() != null
          && !"".equals(fabbricatoVO.getMesiRiscSerra()))
      {
        stmt.setLong(17, Long.parseLong(fabbricatoVO.getMesiRiscSerra()));// MESI_RISCALDAMENTO_SERRA
      }
      else
      {
        stmt.setString(17, null);
      }
      if (fabbricatoVO.getOreRisc() != null
          && !"".equals(fabbricatoVO.getOreRisc()))
      {
        stmt.setLong(18, Long.parseLong(fabbricatoVO.getOreRisc()));// ORE_RISCALDAMENTO_SERRA
      }
      else
      {
        stmt.setString(18, null);
      }
      if ((fabbricatoVO.getVolumeUtilePresuntoFabbricato() != null)
          && !"".equals(fabbricatoVO.getVolumeUtilePresuntoFabbricato()))
      {
        stmt.setDouble(19, Double.parseDouble(Validator.validateDouble(
            fabbricatoVO.getVolumeUtilePresuntoFabbricato(), 9999999999.9999))); // VOLUME_UTILE_PRESUNTO
      }
      else
      {
        stmt.setString(19, null);
      }
      if ((fabbricatoVO.getSuperficieCopertaFabbricato() != null)
          && !"".equals(fabbricatoVO.getSuperficieCopertaFabbricato()))
      {
        stmt.setDouble(20, Double.parseDouble(Validator.validateDouble(
            fabbricatoVO.getSuperficieCopertaFabbricato(), 9999999999.9999))); // SUPERFICIE_COPERTA
      }
      else
      {
        stmt.setString(20, null);
      }
      if ((fabbricatoVO.getSuperficieScopertaFabbricato() != null)
          && !"".equals(fabbricatoVO.getSuperficieScopertaFabbricato()))
      {
        stmt.setDouble(21, Double.parseDouble(Validator.validateDouble(
            fabbricatoVO.getSuperficieScopertaFabbricato(), 9999999999.9999))); // SUPERFICIE_SCOPERTA
      }
      else
      {
        stmt.setString(21, null);
      }

      if ((fabbricatoVO.getSuperficieScopertaExtraFabbricato() != null)
          && !"".equals(fabbricatoVO.getSuperficieScopertaExtraFabbricato()))
      {
        stmt.setDouble(22, Double.parseDouble(Validator.validateDouble(
            fabbricatoVO.getSuperficieScopertaExtraFabbricato(), 999999.9999))); // SUPERFICIE_SCOPERTA_EXTRA
      }
      else
      {
        stmt.setString(22, null);
      }

      SolmrLogger.debug(this, "Executing insertFabbricato: " + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insertFabbricato");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in insertFabbricato: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in insertFabbricato: "
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
            "SQLException while closing Statement and Connection in insertFabbricato: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in insertFabbricato: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }

  // Metodo per inserire un record su DB_FABBRICATO_PARTICELLA
  public Long insertParticellaFabbricato(FabbricatoVO fabbricatoVO,
      Long idParticella) throws DataAccessException
  {

    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {

      primaryKey = getNextPrimaryKey((String) SolmrConstants
          .get("SEQ_PARTICELLA_FABBRICATO"));
      conn = getDatasource().getConnection();

      String insert = " INSERT INTO DB_FABBRICATO_PARTICELLA "
          + " (ID_FABBRICATO_PARTICELLA, ID_PARTICELLA, "
          + "  ID_FABBRICATO, DATA_INIZIO_VALIDITA, DATA_FINE_VALIDITA) "
          + "  VALUES (?, ?, ?, SYSDATE, ?) ";

      stmt = conn.prepareStatement(insert);

      stmt.setLong(1, primaryKey.longValue());
      stmt.setLong(2, idParticella.longValue());
      stmt.setLong(3, fabbricatoVO.getIdFabbricato().longValue());
      if (fabbricatoVO.getDataFineValiditaFabbricato() != null)
      {
        stmt.setDate(4, new java.sql.Date(fabbricatoVO
            .getDataFineValiditaFabbricato().getTime()));
      }
      else
      {
        stmt.setString(4, null);
      }

      SolmrLogger.debug(this, "Executing insertFabbricato: " + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insertParticellaFabbricato");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in insertParticellaFabbricato: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(this, "Generic Exception in insertParticellaFabbricato: "
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
                "SQLException while closing Statement and Connection in insertParticellaFabbricato: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in insertParticellaFabbricato: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }

  // Metodo per effettuare la ricerca dei fabbricati relativi all'azienda
  // agricola selezionata
  public Vector<FabbricatoVO> ricercaFabbricatiByAzienda(Long idAzienda,
      String anno) throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<FabbricatoVO> elencoFabbricati = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT   DISTINCT F.ID_FABBRICATO, C.DESCOM, P.SIGLA_PROVINCIA, TTF.DESCRIZIONE, "
          + "          F.DIMENSIONE, TTF.UNITA_MISURA, F.SUPERFICIE, "
          + "          F.DATA_INIZIO_VALIDITA, F.DATA_FINE_VALIDITA "
          + " FROM     DB_UTE U, COMUNE C, PROVINCIA P, DB_FABBRICATO F, "
          + "          DB_TIPO_TIPOLOGIA_FABBRICATO TTF "
          + " WHERE    U.ID_AZIENDA = ? AND U.ID_UTE = F.ID_UTE "
          + " AND      U.COMUNE = C.ISTAT_COMUNE "
          + " AND      C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
          + " AND      F.ID_TIPOLOGIA_FABBRICATO = TTF.ID_TIPOLOGIA_FABBRICATO "
          + " AND      F.DATA_INIZIO_VALIDITA  <= ? "
          + " AND      (F.DATA_FINE_VALIDITA IS NULL "
          + " OR       F.DATA_FINE_VALIDITA >= ?) "
          + " ORDER BY TTF.DESCRIZIONE, F.DATA_INIZIO_VALIDITA";

      java.util.Date dataInizioAnno = null;
      java.util.Date dataFineAnno = null;
      String dataInizioAnnoStr = "31/12/" + anno;
      String dataFineAnnoStr = "01/01/" + anno;

      try
      {
        dataInizioAnno = Validator.parseDate(dataInizioAnnoStr, "dd/MM/yyyy");
        dataFineAnno = Validator.parseDate(dataFineAnnoStr, "dd/MM/yyyy");
      }
      catch (ParseException ex)
      {
        SolmrLogger.debug(this, "Date Inizio/Fine non parsificabili");
      }

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query ricercaFabbricatiByAzienda: "
          + query);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setDate(2, this.convertSqlDate(dataInizioAnno));
      stmt.setDate(3, this.convertSqlDate(dataFineAnno));

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        elencoFabbricati = new Vector<FabbricatoVO>();
        while (rs.next())
        {
          FabbricatoVO fabbricatoVO = new FabbricatoVO();
          fabbricatoVO.setIdFabbricato(new Long(rs.getLong(1)));
          fabbricatoVO.setDescComuneUte(rs.getString(2));
          fabbricatoVO.setSiglaProvUte(rs.getString(3));
          fabbricatoVO.setDescrizioneTipoFabbricato(rs.getString(4));
          fabbricatoVO.setDimensioneFabbricato(StringUtils.parseDoubleField(rs
              .getString(5)));
          fabbricatoVO.setUnitaMisura(rs.getString(6));
          fabbricatoVO.setSuperficieFabbricato(StringUtils.parseDoubleField(rs
              .getString(7)));
          fabbricatoVO.setDataInizioValiditaFabbricato(rs.getTimestamp(8));
          if (rs.getDate(9) != null)
          {
            fabbricatoVO.setDataFineValiditaFabbricato(rs.getTimestamp(9));
          }
          elencoFabbricati.add(fabbricatoVO);
        }
        stmt.close();
        rs.close();
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "ricercaFabbricatiByAzienda - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "ricercaFabbricatiByAzienda - Generic Exception: " + ex);
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
                "ricercaFabbricatiByAzienda - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "ricercaFabbricatiByAzienda - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoFabbricati;
  }

  // Metodo per recuperare il fabbricato a partire dalla sua chiave primaria
  public FabbricatoVO findFabbricatoByPrimaryKey(Long idFabbricato)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    FabbricatoVO fabbricatoVO = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = 
        "SELECT F.ID_FABBRICATO, " + 
        "       F.ID_TIPOLOGIA_FABBRICATO, " + 
        "       F.ID_FORMA_FABBRICATO, " + 
        "       C.DESCOM, " + 
        "       TTF.DESCRIZIONE AS DESC_TIPO_FABBRICATO, " + 
        "       TFF.DESCRIZIONE AS DESC_FORMA_FABBRICATO, " + 
        "       F.DENOMINAZIONE AS FABBRICATO_DENOMINAZIONE, " + 
        "       F.LARGHEZZA, " + 
        "       F.LUNGHEZZA, " + 
        "       F.ALTEZZA, " + 
        "       F.SUPERFICIE, " + 
        "       F.DIMENSIONE, " + 
        "       TTF.UNITA_MISURA, " + 
        "       F.ANNO_COSTRUZIONE, " +
        "       F.UTM_X, " + 
        "       F.UTM_Y, " + 
        "       F.DATA_INIZIO_VALIDITA, " + 
        "       F.DATA_FINE_VALIDITA, " + 
        "       F.DATA_AGGIORNAMENTO, " + 
        "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
        "          || ' ' " + 
        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
        "         WHERE F.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
        "       AS IRIDE_DENOMINAZIONE, " + 
        "       (SELECT PVU.DENOMINAZIONE " +
        "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
        "        WHERE F.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
        "       AS ENTE_DENOMINAZIONE, " +
        "       U.MOTIVO_MODIFICA, " + 
        "       F.NOTE, " + 
        "       F.ID_UTE, " + 
        "       F.ID_COLTURA_SERRA, " + 
        "       F.MESI_RISCALDAMENTO_SERRA, " + 
        "       F.ORE_RISCALDAMENTO_SERRA, " + 
        "       F.VOLUME_UTILE_PRESUNTO, " + 
        "       F.SUPERFICIE_COPERTA, " + 
        "       F.SUPERFICIE_SCOPERTA, " + 
        "       F.SUPERFICIE_SCOPERTA_EXTRA, " + 
        "       TCS.DESCRIZIONE AS DESCRIZIONE_SERRA " + 
        "       FROM DB_UTE U, " +
        "       COMUNE C, " +
        "       PROVINCIA P, " +
        "       DB_FABBRICATO F, " + 
        "       DB_TIPO_TIPOLOGIA_FABBRICATO TTF, " + 
        "       DB_TIPO_FORMA_FABBRICATO TFF, " +
     //   "       PAPUA_V_UTENTE_LOGIN PVU, " + 
        "       DB_TIPO_COLTURA_SERRA TCS " + 
        "WHERE  F.ID_FABBRICATO = ? " + 
        "AND    U.ID_UTE = F.ID_UTE " + 
        "AND    U.COMUNE = C.ISTAT_COMUNE " + 
        "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " + 
        "AND    F.ID_TIPOLOGIA_FABBRICATO = TTF.ID_TIPOLOGIA_FABBRICATO " + 
        "AND    F.ID_FORMA_FABBRICATO = TFF.ID_FORMA_FABBRICATO(+) " + 
        "AND    F.ID_COLTURA_SERRA = TCS.ID_COLTURA_SERRA(+) "; 
        //"AND    PVU.ID_UTENTE_LOGIN = F.ID_UTENTE_AGGIORNAMENTO ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query findFabbricatoByPrimaryKey: "
          + query);

      stmt.setLong(1, idFabbricato.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        while (rs.next())
        {
          fabbricatoVO = new FabbricatoVO();
          fabbricatoVO.setIdFabbricato(new Long(rs.getLong("ID_FABBRICATO")));
          fabbricatoVO.setIdTipologiaFabbricato(new Long(rs
              .getLong("ID_TIPOLOGIA_FABBRICATO")));
          fabbricatoVO.setTipiFormaFabbricato(rs
              .getString("ID_FORMA_FABBRICATO"));
          fabbricatoVO.setDescComuneUte(rs.getString("DESCOM"));
          fabbricatoVO.setDescrizioneTipoFabbricato(rs
              .getString("DESC_TIPO_FABBRICATO"));
          fabbricatoVO.setDescrizioneTipoFormaFabbricato(rs
              .getString("DESC_FORMA_FABBRICATO"));
          fabbricatoVO.setDenominazioneFabbricato(rs
              .getString("FABBRICATO_DENOMINAZIONE"));
          fabbricatoVO.setLarghezzaFabbricato(rs.getString("LARGHEZZA"));
          fabbricatoVO.setLunghezzaFabbricato(rs.getString("LUNGHEZZA"));
          fabbricatoVO.setAltezzaFabbricato(rs.getString("ALTEZZA"));
          fabbricatoVO.setSuperficieFabbricato(rs.getString("SUPERFICIE"));
          fabbricatoVO.setDimensioneFabbricato(rs.getString("DIMENSIONE"));
          fabbricatoVO.setUnitaMisura(rs.getString("UNITA_MISURA"));
          fabbricatoVO.setAnnoCostruzioneFabbricato(rs
              .getString("ANNO_COSTRUZIONE"));
          fabbricatoVO.setOldAnnoCostruzioneFabbricato(rs
              .getString("ANNO_COSTRUZIONE"));
          fabbricatoVO.setUtmx(rs.getString("UTM_X"));
          fabbricatoVO.setUtmy(rs.getString("UTM_Y"));
          fabbricatoVO.setDataInizioValiditaFabbricato(rs
              .getTimestamp("DATA_INIZIO_VALIDITA"));
          fabbricatoVO.setOldDataInizioValiditaFabbricato(rs
              .getTimestamp("DATA_INIZIO_VALIDITA"));
          fabbricatoVO.setStrDataInizioValiditaFabbricato(DateUtils
              .formatDate(rs.getDate("DATA_INIZIO_VALIDITA")));
          if (rs.getDate("DATA_FINE_VALIDITA") != null)
          {
            fabbricatoVO.setDataFineValiditaFabbricato(rs
                .getTimestamp("DATA_FINE_VALIDITA"));
          }

          // Nuovo Blocco ultima modifica
          String ultimaModifica = "";
          java.util.Date dataAgg = rs.getDate("DATA_AGGIORNAMENTO");
          if (dataAgg != null)
          {
            ultimaModifica = DateUtils.formatDate(dataAgg);
          }

          String tmp = "";
          String utDenominazione = rs.getString("IRIDE_DENOMINAZIONE");
          if (utDenominazione != null && !utDenominazione.equals(""))
          {
            if (tmp.equals(""))
              tmp += " (";
            tmp += utDenominazione;
          }
          String utEnteAppart = rs.getString("ENTE_DENOMINAZIONE");
          if (utEnteAppart != null && !utEnteAppart.equals(""))
          {
            if (tmp.equals(""))
              tmp += " (";
            else
              tmp += " - ";
            tmp += utEnteAppart;
          }
          String motivoModifica = rs.getString("MOTIVO_MODIFICA");
          if (motivoModifica != null && !motivoModifica.equals(""))
          {
            if (tmp.equals(""))
              tmp += " (";
            else
              tmp += " - ";
            tmp += motivoModifica;
          }
          if (!tmp.equals(""))
            tmp += ")";
          ultimaModifica += tmp;

          fabbricatoVO.setUltimaModificaFabbricato(ultimaModifica);

          // Ultima Modifica Spezzata
          fabbricatoVO.setDataAggiornamento(dataAgg);
          fabbricatoVO.setUtenteUltimaModifica(utDenominazione);
          fabbricatoVO.setEnteUltimaModifica(utEnteAppart);
          fabbricatoVO.setMotivoModifica(motivoModifica);

          fabbricatoVO.setNoteFabbricato(rs.getString("NOTE"));
          if (rs.getString("ID_UTE") != null
              && !rs.getString("ID_UTE").equals(""))
          {
            fabbricatoVO.setIdUnitaProduttivaFabbricato(new Long(rs
                .getLong("ID_UTE")));
          }
          fabbricatoVO.setTipologiaColturaSerra(rs
              .getString("ID_COLTURA_SERRA"));
          fabbricatoVO.setMesiRiscSerra(rs
              .getString("MESI_RISCALDAMENTO_SERRA"));
          fabbricatoVO.setOreRisc(rs.getString("ORE_RISCALDAMENTO_SERRA"));
          fabbricatoVO.setDescrizioneTipologiaColturaSerra(rs
              .getString("DESCRIZIONE_SERRA"));
          fabbricatoVO.setVolumeUtilePresuntoFabbricato(rs
              .getString("VOLUME_UTILE_PRESUNTO"));
          fabbricatoVO.setSuperficieCopertaFabbricato(rs
              .getString("SUPERFICIE_COPERTA"));
          fabbricatoVO.setSuperficieScopertaFabbricato(rs
              .getString("SUPERFICIE_SCOPERTA"));
          fabbricatoVO.setSuperficieScopertaExtraFabbricato(rs
              .getString("SUPERFICIE_SCOPERTA_EXTRA"));
        }

        stmt.close();
        rs.close();
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "findFabbricatoByPrimaryKey - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "findFabbricatoByPrimaryKey - Generic Exception: " + ex);
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
                "findFabbricatoByPrimaryKey - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "findFabbricatoByPrimaryKey - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return fabbricatoVO;
  }

  /**
   * Metodo per recuperare l'elenco delle particelle su cui insiste il
   * fabbricato selezionato Se modifica è uguale a true significa che sto
   * costruendo l'elenco delle particelle da usare nella modifica, quindi non
   * devo far vedere quelle particelle che hanno la DataFineValidità impostata
   * 
   * @param fabbricatoVO
   *          FabbricatoVO
   * @param modifica
   *          boolean
   * @return Vector
   * @throws DataAccessException
   */
  public Vector<ParticellaVO> getElencoParticelleByFabbricato(
      FabbricatoVO fabbricatoVO, boolean modifica) throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaVO> elencoParticelleFabbricato = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT DISTINCT C.DESCOM, SP.SEZIONE, SP.FOGLIO, SP.PARTICELLA, "
          + "                 SP.SUBALTERNO, SP.SUP_CATASTALE, "
          + "                 SP.ID_STORICO_PARTICELLA,  "
          + "                 PF.ID_PARTICELLA, CP.ID_TITOLO_POSSESSO, TTP.DESCRIZIONE, "
          + "                 PF.DATA_INIZIO_VALIDITA, PF.DATA_FINE_VALIDITA,  "
          + "                 CP.DATA_FINE_CONDUZIONE, "
          + "                 P.SIGLA_PROVINCIA "
          + " FROM            DB_STORICO_PARTICELLA SP, COMUNE C, "
          + "                 DB_CONDUZIONE_PARTICELLA CP, DB_TIPO_TITOLO_POSSESSO TTP, "
          + "                 DB_FABBRICATO_PARTICELLA PF, "
          + "                 PROVINCIA P "
          + " WHERE           PF.ID_FABBRICATO = ? "
          + " AND             PF.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND             SP.COMUNE = C.ISTAT_COMUNE  "
          + " AND             SP.DATA_FINE_VALIDITA IS NULL  "
          + " AND             CP.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND             CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO "
          + " AND             CP.ID_UTE = ? "
          + " AND             CP.ID_CONDUZIONE_PARTICELLA = (SELECT MAX(ID_CONDUZIONE_PARTICELLA) FROM DB_CONDUZIONE_PARTICELLA WHERE ID_PARTICELLA = SP.ID_PARTICELLA) "
          + " AND             C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA ";
      if (modifica)
        query += "AND    PF.DATA_FINE_VALIDITA IS NULL  ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing query getElencoParticelleByFabbricato: " + query);

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [ID_FABBRICATO] in getElencoParticelleByFabbricato method in FascicoloDAO: "
                  + fabbricatoVO.getIdFabbricato() + "\n");
      stmt.setLong(1, fabbricatoVO.getIdFabbricato().longValue());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 2 [ID_UTE] in getElencoParticelleByFabbricato method in FascicoloDAO: "
                  + fabbricatoVO.getIdUnitaProduttivaFabbricato() + "\n");
      stmt
          .setLong(2, fabbricatoVO.getIdUnitaProduttivaFabbricato().longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        elencoParticelleFabbricato = new Vector<ParticellaVO>();
        while (rs.next())
        {
          ParticellaVO particellaVO = new ParticellaVO();
          particellaVO.setDescComuneParticella(rs.getString(1));
          particellaVO.setSezione(rs.getString(2));
          particellaVO.setFoglio(new Long(rs.getLong(3)));
          if (rs.getString(4) != null && !rs.getString(4).equals(""))
          {
            particellaVO.setParticella(new Long(rs.getLong(4)));
          }
          particellaVO.setSubalterno(rs.getString(5));
          particellaVO.setSupCatastale(rs.getString(6));
          particellaVO.setIdStoricoParticella(new Long(rs.getLong(7)));
          particellaVO.setIdParticella(new Long(rs.getLong(8)));
          particellaVO.setIdTitoloPossesso(new Long(rs.getLong(9)));
          particellaVO.setDescrizioneTitoloPossesso(rs.getString(10));
          particellaVO.setDataInizioValidita(rs.getDate(11));
          particellaVO.setDataFineValidita(rs.getDate(12));
          particellaVO
              .setDataFineConduzione(rs.getDate("DATA_FINE_CONDUZIONE"));
          particellaVO.setSiglaProvinciaParticella(rs
              .getString("SIGLA_PROVINCIA"));
          elencoParticelleFabbricato.add(particellaVO);
        }

        stmt.close();
        rs.close();
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger
          .fatal(this, "getElencoParticelleByFabbricato - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getElencoParticelleByFabbricato - Generic Exception: " + ex);
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
                "getElencoParticelleByFabbricato - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getElencoParticelleByFabbricato - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoParticelleFabbricato;
  }

  // Metodo per recuperare l'elenco delle particelle ad uso fabbricato associate
  // ad una
  // unita produttiva associabili ad un fabbricato
  public Vector<ParticellaVO> getElencoParticelleFabbricatoByUteAssociabili(
      Long idUte, Vector<Long> elencoParticelle, boolean serra)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaVO> elencoParticelleFabbricato = null;
    String inCondition;

    try
    {
      if (elencoParticelle != null && !elencoParticelle.isEmpty())
      {
        Iterator<Long> iterator = elencoParticelle.iterator();
        inCondition = ((Long) iterator.next()).toString();
        while (iterator.hasNext())
        {
          inCondition += ", " + ((Long) iterator.next()).toString();
        }
      }
      else
      {
        inCondition = "-1";
      }

      conn = getDatasource().getConnection();

      String query = " SELECT DISTINCT SP.ID_PARTICELLA, SP.ID_STORICO_PARTICELLA, SP.COMUNE, "
          + " C.DESCOM, SP.SEZIONE, SP.FOGLIO, SP.PARTICELLA, SP.SUBALTERNO, "
          + " SP.SUP_CATASTALE, "
          + " P.SIGLA_PROVINCIA "
          + " FROM DB_STORICO_PARTICELLA SP, COMUNE C, "
          + " DB_CONDUZIONE_PARTICELLA CP, DB_TIPO_TITOLO_POSSESSO TTP, "
          + " DB_UTILIZZO_PARTICELLA UP,DB_TIPO_UTILIZZO TU, "
          + " PROVINCIA P "
          + " WHERE CP.ID_UTE = ? "
          + " AND CP.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND SP.DATA_FINE_VALIDITA IS NULL "
          + " AND UP.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA "
          + " AND SP.COMUNE = C.ISTAT_COMUNE "
          + " AND CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO "
          + " AND CP.ID_TITOLO_POSSESSO <> 5 "
          + " AND CP.ID_TITOLO_POSSESSO <> 6 "
          + " AND CP.DATA_FINE_CONDUZIONE IS NULL "
          + " AND UP.ID_UTILIZZO = TU.ID_UTILIZZO "
          +
          // " AND (UP.ANNO <= ? OR UP.ANNO IS NULL) "+
          " AND SP.ID_PARTICELLA NOT IN ("
          + inCondition
          + ") "
          + " AND C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA ";
      if (serra)
      {
        query += " AND ( TU.TIPO = ? OR TU.FLAG_SERRA = ?) ";
      }
      else
      {
        query += " AND TU.TIPO = ? ";
      }

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing query getElencoParticelleFabbricatoByUteAssociabili: "
              + query);

      stmt.setLong(1, idUte.longValue());
      // stmt.setInt(2, DateUtils.getCurrentYear().intValue());
      stmt.setString(2, ((String) SolmrConstants
          .get("TIPO_UTILIZZO_FABBRICATO")));
      if (serra)
        stmt.setString(3, ((String) SolmrConstants
            .get("TIPO_UTILIZZO_FABBRICATO_SERRA")));

      ResultSet rs = stmt.executeQuery();

      elencoParticelleFabbricato = new Vector<ParticellaVO>();
      while (rs.next())
      {
        ParticellaVO particellaVO = new ParticellaVO();
        particellaVO.setIdParticella(new Long(rs.getLong(1)));
        particellaVO.setIdStoricoParticella(new Long(rs.getLong(2)));
        particellaVO.setIstatComuneParticella(rs.getString(3));
        particellaVO.setDescComuneParticella(rs.getString(4));
        particellaVO.setSezione(rs.getString(5));
        particellaVO.setFoglio(new Long(rs.getLong(6)));
        particellaVO.setStrFoglio(rs.getString(6));
        if (rs.getString(7) != null && !rs.getString(7).equals(""))
        {
          particellaVO.setParticella(new Long(rs.getLong(7)));
        }
        particellaVO.setStrParticella(rs.getString(7));
        particellaVO.setSubalterno(rs.getString(8));
        particellaVO.setSupCatastale(rs.getString(9));
        particellaVO.setSiglaProvinciaParticella(rs
            .getString("SIGLA_PROVINCIA"));
        elencoParticelleFabbricato.add(particellaVO);
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getElencoParticelleFabbricatoByUteAssociabili - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getElencoParticelleFabbricatoByUteAssociabili - Generic Exception: "
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
                "getElencoParticelleFabbricatoByUteAssociabili - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getElencoParticelleFabbricatoByUteAssociabili - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoParticelleFabbricato;
  }

  // Metodo per cessare l'utilizzo di una particella a fabbricato
  public void cessaUtilizzoParticellaFabbricato(FabbricatoVO fabbricatoVO)
      throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " UPDATE DB_FABBRICATO_PARTICELLA "
          + " SET DATA_FINE_VALIDITA = SYSDATE " + " WHERE ID_FABBRICATO = ? "
          + " AND DATA_FINE_VALIDITA IS NULL";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing cessaUtilizzoParticellaFabbricato in fascicoloDAO: "
              + query);

      stmt.setLong(1, fabbricatoVO.getIdFabbricato().longValue());

      stmt.executeUpdate();

      query = " UPDATE DB_FABBRICATO " + " SET DATA_FINE_VALIDITA = SYSDATE "
          + " WHERE ID_FABBRICATO = ? " + " AND DATA_FINE_VALIDITA IS NULL";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing cessaUtilizzoParticellaFabbricato in fascicoloDAO: "
              + query);

      stmt.setLong(1, fabbricatoVO.getIdFabbricato().longValue());

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this,
          "Executed cessaUtilizzoParticellaFabbricato in fascicoloDAO");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "cessaUtilizzoParticellaFabbricato in fascicoloDAO - SQLException: "
              + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "cessaUtilizzoParticellaFabbricato in fascicoloDAO - Generic Exception: "
              + ex);
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
                "cessaUtilizzoParticellaFabbricato in fascicoloDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "cessaUtilizzoParticellaFabbricato in fascicoloDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
  }

  // Metodo per effettuare la modifica di un record su DB_FABBRICATO
  public void updateFabbricato(FabbricatoVO fabbricatoVO, long idUtenteAggiornamento)
      throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " UPDATE DB_FABBRICATO "
          + " SET    ID_TIPOLOGIA_FABBRICATO = ?, "
          + "        ID_FORMA_FABBRICATO = ?, " + "        DENOMINAZIONE = ?, "
          + "        SUPERFICIE = ?, " + "        DIMENSIONE = ?, "
          + "        LUNGHEZZA = ?, " + "        LARGHEZZA = ?, "
          + "        ALTEZZA = ?, " + "        ANNO_COSTRUZIONE = ?, "
          + "        UTM_X = ?, " + "        UTM_Y = ?, "
          + "        NOTE = ?, " + "        DATA_AGGIORNAMENTO = SYSDATE, "
          + "        ID_UTENTE_AGGIORNAMENTO = ?, "
          + "        ID_COLTURA_SERRA = ?, "
          + "        MESI_RISCALDAMENTO_SERRA = ?, "
          + "        ORE_RISCALDAMENTO_SERRA = ? , "
          + "        SUPERFICIE_COPERTA = ? , "
          + "        SUPERFICIE_SCOPERTA = ?, "
          + "        SUPERFICIE_SCOPERTA_EXTRA = ?, "
          + "        VOLUME_UTILE_PRESUNTO = ? " + " WHERE  ID_FABBRICATO = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing updateFabbricato: " + query);

      stmt.setLong(1, fabbricatoVO.getIdTipologiaFabbricato().longValue());
      stmt.setString(2, fabbricatoVO.getTipiFormaFabbricato());
      if (fabbricatoVO.getDenominazioneFabbricato() != null)
      {
        stmt.setString(3, fabbricatoVO.getDenominazioneFabbricato()
            .toUpperCase());
      }
      else
      {
        stmt.setString(3, null);
      }
      stmt.setDouble(4, Double.parseDouble(Validator.validateDouble(
          fabbricatoVO.getSuperficieFabbricato(), 9999999999.9999)));
      stmt.setDouble(5, Double.parseDouble(Validator.validateDouble(
          fabbricatoVO.getDimensioneFabbricato(), 9999999999.9999)));
      if (fabbricatoVO.getLunghezzaFabbricato() != null
          && !fabbricatoVO.getLunghezzaFabbricato().equals(""))
      {
        stmt.setDouble(6, Double.parseDouble(Validator.validateDouble(
            fabbricatoVO.getLunghezzaFabbricato(), 99999.99)));
      }
      else
      {
        stmt.setString(6, null);
      }
      if (fabbricatoVO.getLarghezzaFabbricato() != null
          && !fabbricatoVO.getLarghezzaFabbricato().equals(""))
      {
        stmt.setDouble(7, Double.parseDouble(Validator.validateDouble(
            fabbricatoVO.getLarghezzaFabbricato(), 99999.99)));
      }
      else
      {
        stmt.setString(7, null);
      }
      if (fabbricatoVO.getAltezzaFabbricato() != null
          && !fabbricatoVO.getAltezzaFabbricato().equals(""))
      {
        stmt.setDouble(8, Double.parseDouble(Validator
            .validateDoubleIncludeZero(fabbricatoVO.getAltezzaFabbricato(),
                99999.99)));
      }
      else
      {
        stmt.setString(8, null);
      }
      stmt.setString(9, fabbricatoVO.getAnnoCostruzioneFabbricato());
      stmt.setString(10, fabbricatoVO.getUtmx());
      stmt.setString(11, fabbricatoVO.getUtmy());
      stmt.setString(12, fabbricatoVO.getNoteFabbricato());
      stmt.setLong(13, idUtenteAggiornamento);

      if (fabbricatoVO.getTipologiaColturaSerra() != null
          && !"".equals(fabbricatoVO.getTipologiaColturaSerra()))
        stmt.setLong(14, Long
            .parseLong(fabbricatoVO.getTipologiaColturaSerra()));// ID_COLTURA_SERRA
      else
        stmt.setString(14, null);
      if (fabbricatoVO.getMesiRiscSerra() != null
          && !"".equals(fabbricatoVO.getMesiRiscSerra()))
        stmt.setLong(15, Long.parseLong(fabbricatoVO.getMesiRiscSerra()));// MESI_RISCALDAMENTO_SERRA
      else
        stmt.setString(15, null);
      if (fabbricatoVO.getOreRisc() != null
          && !"".equals(fabbricatoVO.getOreRisc()))
        stmt.setLong(16, Long.parseLong(fabbricatoVO.getOreRisc()));// ORE_RISCALDAMENTO_SERRA
      else
        stmt.setString(16, null);

      if (fabbricatoVO.getSuperficieCopertaFabbricato() != null
          && !fabbricatoVO.getSuperficieCopertaFabbricato().equals(""))
      {
        stmt.setDouble(17, Double.parseDouble(Validator.validateDouble(
            fabbricatoVO.getSuperficieCopertaFabbricato(), 99999.99)));
      }
      else
      {
        stmt.setString(17, null);
      }

      if (fabbricatoVO.getSuperficieScopertaFabbricato() != null
          && !fabbricatoVO.getSuperficieScopertaFabbricato().equals(""))
      {
        stmt.setDouble(18, Double.parseDouble(Validator.validateDouble(
            fabbricatoVO.getSuperficieScopertaFabbricato(), 99999.99)));
      }
      else
      {
        stmt.setString(18, null);
      }

      if (fabbricatoVO.getSuperficieScopertaExtraFabbricato() != null
          && !fabbricatoVO.getSuperficieScopertaExtraFabbricato().equals(""))
      {
        stmt.setDouble(19, Double.parseDouble(Validator.validateDouble(
            fabbricatoVO.getSuperficieScopertaExtraFabbricato(), 999999.9999)));
      }
      else
      {
        stmt.setString(19, null);
      }

      if (fabbricatoVO.getVolumeUtilePresuntoFabbricato() != null)
        stmt.setDouble(20, Double.parseDouble(Validator.validateDouble(
            fabbricatoVO.getVolumeUtilePresuntoFabbricato(), 9999999999.9999)));
      else
        stmt.setString(20, null);

      stmt.setLong(21, fabbricatoVO.getIdFabbricato().longValue());

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed updateFabbricato in fascicoloDAO");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "updateFabbricato in fascicoloDAO - SQLException: "
              + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "updateFabbricato in fascicoloDAO - Generic Exception: " + ex);
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
                "updateFabbricato in fascicoloDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "updateFabbricato in fascicoloDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
  }

  // Metodo per eliminare dei record da DB_FABBRICATO_PARTICELLA
  public void deleteParticellaFabbricato(ParticellaVO particellaFabbricatoVO)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " DELETE FROM DB_FABBRICATO_PARTICELLA "
          + " WHERE  ID_PARTICELLA = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing deleteParticellaFabbricato: " + query);

      stmt.setLong(1, particellaFabbricatoVO.getIdParticella().longValue());

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this,
          "Executed deleteParticellaFabbricato in fascicoloDAO");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "deleteParticellaFabbricato in fascicoloDAO - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "deleteParticellaFabbricato in fascicoloDAO - Generic Exception: "
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
                "deleteParticellaFabbricato in fascicoloDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "deleteParticellaFabbricato in fascicoloDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per eliminare tutte le particelle legate ad un fabbricato
  public void deleteParticellaFabbricato(Long idFabbricato)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " DELETE FROM DB_FABBRICATO_PARTICELLA "
          + " WHERE  ID_FABBRICATO = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing deleteParticellaFabbricato: " + query);

      stmt.setLong(1, idFabbricato.longValue());

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this,
          "Executed deleteParticellaFabbricato in fascicoloDAO");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "deleteParticellaFabbricato in fascicoloDAO - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "deleteParticellaFabbricato in fascicoloDAO - Generic Exception: "
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
                "deleteParticellaFabbricato in fascicoloDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "deleteParticellaFabbricato in fascicoloDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per modificare dei record da DB_FABBRICATO_PARTICELLA
  public void updateParticellaFabbricato(ParticellaVO particellaFabbricatoVO)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " UPDATE DB_FABBRICATO_PARTICELLA "
          + " SET DATA_FINE_VALIDITA = SYSDATE " + " WHERE  ID_PARTICELLA = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing deleteParticellaFabbricato: " + query);

      stmt.setLong(1, particellaFabbricatoVO.getIdParticella().longValue());

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this,
          "Executed deleteParticellaFabbricato in fascicoloDAO");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "updateParticellaFabbricato in fascicoloDAO - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "updateParticellaFabbricato in fascicoloDAO - Generic Exception: "
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
                "updateParticellaFabbricato in fascicoloDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "updateParticellaFabbricato in fascicoloDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per eliminare dei record da DB_FABBRICATO_PARTICELLA a partire dal
  // fabbricato
  public void deleteParticellaFabbricatoByIdFabbricato(FabbricatoVO fabbricatoVO)
      throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " DELETE FROM DB_FABBRICATO_PARTICELLA "
          + " WHERE  ID_FABBRICATO = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing deleteParticellaFabbricatoByIdFabbricato: " + query);

      stmt.setLong(1, fabbricatoVO.getIdFabbricato().longValue());

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this,
          "Executed deleteParticellaFabbricatoByIdFabbricato in fascicoloDAO");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "deleteParticellaFabbricatoByIdFabbricato in fascicoloDAO - SQLException: "
              + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(
              this,
              "deleteParticellaFabbricatoByIdFabbricato in fascicoloDAO - Generic Exception: "
                  + ex);
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
                "deleteParticellaFabbricatoByIdFabbricato in fascicoloDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "deleteParticellaFabbricatoByIdFabbricato in fascicoloDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
  }

  // Metodo per recuperare il numero di record preseenti su
  // DB_FABBRICATO_PARTICELLA
  // a partire dall'id_fabbricato
  public int getNumRecordParticellaFabbricato(Long idFabbricato)
      throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    int numFabbricato = 0;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT COUNT(*) FROM DB_FABBRICATO_PARTICELLA WHERE ID_FABBRICATO = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing query getNumRecordParticellaFabbricato: " + query);

      stmt.setLong(1, idFabbricato.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        while (rs.next())
        {
          numFabbricato = rs.getInt(1);
        }
        stmt.close();
        rs.close();
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getNumRecordParticellaFabbricato - SQLException: "
              + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getNumRecordParticellaFabbricato - Generic Exception: " + ex);
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
                "getNumRecordParticellaFabbricato - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getNumRecordParticellaFabbricato - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return numFabbricato;
  }

  // Metodo per eliminare un record da DB_FABBRICATO a partire dal fabbricato
  public void deleteFabbricato(FabbricatoVO fabbricatoVO) throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " DELETE FROM DB_FABBRICATO "
          + " WHERE  ID_FABBRICATO = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing deleteFabbricato: " + query);

      stmt.setLong(1, fabbricatoVO.getIdFabbricato().longValue());

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed deleteFabbricato in fascicoloDAO");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "deleteFabbricato in fascicoloDAO - SQLException: "
              + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "deleteFabbricato in fascicoloDAO - Generic Exception: " + ex);
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
                "deleteFabbricato in fascicoloDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "deleteFabbricato in fascicoloDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
  }

  // Metodo per recuperare l'elenco delle unità produttive valide ad una certa
  // data associate
  // ad un'azienda agricola
  public Vector<UteVO> getElencoUteAttiveForDateAndAzienda(Long idAzienda,
      String data) throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<UteVO> elencoUteAttive = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT U.ID_UTE, U.ID_AZIENDA, U.DENOMINAZIONE, U.INDIRIZZO, "
          + " U.COMUNE, C.DESCOM, U.ID_ZONA_ALTIMETRICA, ZA.DESCRIZIONE, "
          + " U.CAP, U.ID_ATTIVITA_ATECO, AA.DESCRIZIONE, U.NOTE, U.TELEFONO, "
          + " U.FAX, U.ID_UTENTE_AGGIORNAMENTO, U.DATA_INIZIO_ATTIVITA, "
          + " U.DATA_FINE_ATTIVITA, U.CAUSALE_CESSAZIONE, U.DATA_AGGIORNAMENTO, "
          + " U.MOTIVO_MODIFICA, U.ID_ATTIVITA_OTE, AO.DESCRIZIONE "
          + " FROM DB_UTE U, COMUNE C, DB_TIPO_ZONA_ALTIMETRICA ZA, "
          + " DB_TIPO_ATTIVITA_ATECO AA, DB_TIPO_ATTIVITA_OTE AO "
          + " WHERE ID_AZIENDA = ? "
          + " AND (DATA_FINE_ATTIVITA IS NULL OR DATA_FINE_ATTIVITA >= to_date(?, 'DD/MM/RRRR')) "
          + " AND U.COMUNE = C.ISTAT_COMUNE(+) "
          + " AND U.ID_ZONA_ALTIMETRICA = ZA.ID_ZONA_ALTIMETRICA(+) "
          + " AND U.ID_ATTIVITA_ATECO = AA.ID_ATTIVITA_ATECO(+) "
          + " AND U.ID_ATTIVITA_OTE = AO.ID_ATTIVITA_OTE(+) ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing getElencoUteAttiveForDateAndAzienda: "
          + query);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setString(2, data);

      ResultSet rs = stmt.executeQuery();

      elencoUteAttive = new Vector<UteVO>();
      while (rs.next())
      {
        UteVO uteVO = new UteVO();
        uteVO.setIdUte(new Long(rs.getLong(1)));
        uteVO.setIdAzienda(new Long(rs.getLong(2)));
        uteVO.setDenominazione(rs.getString(3));
        uteVO.setIndirizzo(rs.getString(4));
        uteVO.setIdComune(rs.getString(5));
        uteVO.setComune(rs.getString(6));
        if (Validator.isNotEmpty(rs.getString(7)))
        {
          uteVO.setIdZonaAltimetrica(new Long(rs.getLong(7)));
        }
        uteVO.setZonaAltimetrica(rs.getString(8));
        uteVO.setCap(rs.getString(9));
        uteVO.setCodeAteco(rs.getString(10));
        uteVO.setAteco(rs.getString(11));
        uteVO.setNote(rs.getString(12));
        uteVO.setTelefono(rs.getString(13));
        uteVO.setFax(rs.getString(14));
        uteVO.setUtenteAggiornamento(new Long(rs.getLong(15)));
        uteVO.setDataInizioAttivita(rs.getDate(16));
        if (rs.getDate(17) != null)
        {
          uteVO.setDataFineAttivita(rs.getDate(17));
        }
        uteVO.setCausaleCessazione(rs.getString(18));
        uteVO.setMotivoModifica(rs.getString(19));
        uteVO.setCodeOte(rs.getString(20));
        uteVO.setOte(rs.getString(21));
        elencoUteAttive.add(uteVO);
      }

      stmt.close();
      rs.close();

      if (elencoUteAttive == null || elencoUteAttive.size() == 0)
      {
        throw new SolmrException((String) AnagErrors.get("ERR_UTE_ATTIVE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getElencoUteAttiveForDateAndAzienda - SQLException: "
              + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getElencoUteAttiveForDateAndAzienda - Generic Exception: " + ex);
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
                "getElencoUteAttiveForDateAndAzienda - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getElencoUteAttiveForDateAndAzienda - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoUteAttive;
  }

  // Metodo per recuperare l'elenco delle particelle relative ad un'azienda
  // agricola
  public Vector<ParticellaVO> getElencoParticelleForUteAndAzienda(Long idAzienda)
      throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaVO> elencoParticelle = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT   A.ID_TITOLO_POSSESSO, "
          + "          TTP.DESCRIZIONE, "
          + "          SUM(A.SUPERFICIE_CONDOTTA) "
          + " FROM     DB_CONDUZIONE_PARTICELLA A, " + "          DB_UTE B, "
          + "          DB_TIPO_TITOLO_POSSESSO TTP "
          + " WHERE    A.ID_UTE = B.ID_UTE "
          + " AND      A.DATA_FINE_CONDUZIONE IS NULL "
          + " AND      B.ID_AZIENDA = ? "
          + " AND      TTP.ID_TITOLO_POSSESSO = A.ID_TITOLO_POSSESSO "
          + " GROUP BY A.ID_TITOLO_POSSESSO, TTP.DESCRIZIONE "
          + " ORDER BY TTP.DESCRIZIONE ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing getElencoParticelleForUteAndAzienda: "
          + query);

      stmt.setLong(1, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      elencoParticelle = new Vector<ParticellaVO>();

      while (rs.next())
      {
        ParticellaVO particellaVO = new ParticellaVO();
        particellaVO.setIdTitoloPossesso(new Long(rs.getLong(1)));
        particellaVO.setDescrizioneTitoloPossesso(rs.getString(2));
        particellaVO.setSuperficieCondotta(StringUtils.parseSuperficieField(rs
            .getString(3)));
        elencoParticelle.add(particellaVO);
      }

      stmt.close();
      rs.close();

      if (elencoParticelle == null || elencoParticelle.size() == 0)
      {
        throw new SolmrException((String) AnagErrors.get("ERR_NO_PARTICELLE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getElencoParticelleForUteAndAzienda - SQLException: "
              + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getElencoParticelleForUteAndAzienda - Generic Exception: " + ex);
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
                "getElencoParticelleForUteAndAzienda - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getElencoParticelleForUteAndAzienda - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoParticelle;
  }

  // Metodo per recuperare l'elenco delle particelle relative ad un'azienda
  // agricola,
  // ad un'unità produttiva e per riepilogo titolo possesso/comune
  public Vector<ParticellaVO> getElencoParticelleForUteAndAziendaPossAndComune(
      Long idAzienda) throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaVO> elencoParticelle = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT   A.ID_TITOLO_POSSESSO, "
          + "          TTP.DESCRIZIONE, " + "          SP.COMUNE, "
          + "          C.DESCOM, " + "          P.SIGLA_PROVINCIA, "
          + "          SUM(A.SUPERFICIE_CONDOTTA) "
          + " FROM     DB_CONDUZIONE_PARTICELLA A, " + "          DB_UTE B, "
          + "          DB_TIPO_TITOLO_POSSESSO TTP, "
          + "          DB_STORICO_PARTICELLA SP, " + "          COMUNE C, "
          + "          PROVINCIA P " + " WHERE    A.ID_UTE = B.ID_UTE "
          + " AND      A.DATA_FINE_CONDUZIONE IS NULL "
          + " AND      B.ID_AZIENDA = ? "
          + " AND      TTP.ID_TITOLO_POSSESSO = A.ID_TITOLO_POSSESSO "
          + " AND      A.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND      SP.DATA_FINE_VALIDITA IS NULL "
          + " AND      C.ISTAT_COMUNE = SP.COMUNE "
          + " AND      P.ISTAT_PROVINCIA = C.ISTAT_PROVINCIA "
          + " GROUP BY A.ID_TITOLO_POSSESSO, " + "          TTP.DESCRIZIONE, "
          + "          SP.COMUNE, " + "          C.DESCOM, "
          + "          P.SIGLA_PROVINCIA "
          + " ORDER BY TTP.DESCRIZIONE, C.DESCOM ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing getElencoParticelleForUteAndAziendaPossAndComune: "
              + query);

      stmt.setLong(1, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      elencoParticelle = new Vector<ParticellaVO>();

      while (rs.next())
      {
        ParticellaVO particellaVO = new ParticellaVO();
        particellaVO.setIdTitoloPossesso(new Long(rs.getLong(1)));
        particellaVO.setDescrizioneTitoloPossesso(rs.getString(2));
        particellaVO.setIstatComuneParticella(rs.getString(3));
        particellaVO.setDescComuneParticella(rs.getString(4));
        particellaVO.setSiglaProvinciaParticella(rs.getString(5));
        particellaVO.setSuperficieCondotta(StringUtils.parseSuperficieField(rs
            .getString(6)));
        elencoParticelle.add(particellaVO);
      }

      stmt.close();
      rs.close();

      if (elencoParticelle == null || elencoParticelle.size() == 0)
      {
        throw new SolmrException((String) AnagErrors.get("ERR_NO_PARTICELLE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getElencoParticelleForUteAndAziendaPossAndComune - SQLException: "
              + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getElencoParticelleForUteAndAziendaPossAndComune - Generic Exception: "
              + ex);
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
                "getElencoParticelleForUteAndAziendaPossAndComune - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getElencoParticelleForUteAndAziendaPossAndComune - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoParticelle;
  }

  // Metodo per recuperare l'elenco delle particelle relative ad un'azienda
  // agricola,
  // ad un'unità produttiva e per riepilogo comune
  public Vector<ParticellaVO> getElencoParticelleForUteAndAziendaComune(
      Long idAzienda) throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaVO> elencoParticelle = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT   SP.COMUNE, " + "          C.DESCOM, "
          + "          P.SIGLA_PROVINCIA, "
          + "          SUM(A.SUPERFICIE_CONDOTTA) "
          + " FROM     DB_CONDUZIONE_PARTICELLA A, " + "          DB_UTE B, "
          + "          DB_STORICO_PARTICELLA SP, " + "          COMUNE C, "
          + "          PROVINCIA P " + " WHERE    A.ID_UTE = B.ID_UTE "
          + " AND      A.DATA_FINE_CONDUZIONE IS NULL "
          + " AND      B.ID_AZIENDA = ? "
          + " AND      A.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND      SP.DATA_FINE_VALIDITA IS NULL "
          + " AND      C.ISTAT_COMUNE = SP.COMUNE "
          + " AND      P.ISTAT_PROVINCIA = C.ISTAT_PROVINCIA "
          + " GROUP BY SP.COMUNE, " + "          C.DESCOM, "
          + "          P.SIGLA_PROVINCIA ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing getElencoParticelleForUteAndAziendaComune: " + query);

      stmt.setLong(1, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      elencoParticelle = new Vector<ParticellaVO>();

      while (rs.next())
      {
        ParticellaVO particellaVO = new ParticellaVO();
        particellaVO.setIstatComuneParticella(rs.getString(1));
        particellaVO.setDescComuneParticella(rs.getString(2));
        particellaVO.setSiglaProvinciaParticella(rs.getString(3));
        particellaVO.setSuperficieCondotta(StringUtils.parseSuperficieField(rs
            .getString(4)));
        elencoParticelle.add(particellaVO);
      }

      stmt.close();
      rs.close();

      if (elencoParticelle == null || elencoParticelle.size() == 0)
      {
        throw new SolmrException((String) AnagErrors.get("ERR_NO_PARTICELLE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getElencoParticelleForUteAndAziendaComune - SQLException: "
              + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getElencoParticelleForUteAndAziendaComune - Generic Exception: "
              + ex);
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
                "getElencoParticelleForUteAndAziendaComune - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getElencoParticelleForUteAndAziendaComune - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoParticelle;
  }

  // Metodo per effettuare la ricerca delle particelle attive in relazione ai
  // parametri di
  // ricerca
  public Vector<ParticellaVO> ricercaParticelleAttiveByParametri(
      ParticellaVO particellaVO, String data, Long idAzienda)
      throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaVO> elencoParticelle = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT SP.ID_STORICO_PARTICELLA, "
          + "        P.SIGLA_PROVINCIA, " + "        C.DESCOM, "
          + "        C.FLAG_ESTERO, " + "        SP.SEZIONE, "
          + "        SP.FOGLIO, " + "        SP.PARTICELLA, "
          + "        SP.SUBALTERNO, " + "        SP.SUP_CATASTALE, "
          + "        CP.SUPERFICIE_CONDOTTA, " + "        TTP.DESCRIZIONE, "
          + "        CP.DATA_INIZIO_CONDUZIONE, "
          + "        CP.ID_CONDUZIONE_PARTICELLA, "
          + "        CP.DATA_FINE_CONDUZIONE, "
          + "        CP.ESITO_CONTROLLO, " + "        CP.ID_TITOLO_POSSESSO, "
          + "        CP.ID_CONDUZIONE_PARTICELLA " + " FROM   PROVINCIA P, "
          + "        COMUNE C, " + "        DB_STORICO_PARTICELLA SP, "
          + "        DB_CONDUZIONE_PARTICELLA CP, "
          + "        DB_TIPO_TITOLO_POSSESSO TTP, " + "        DB_UTE U "
          + " WHERE  SP.COMUNE = C.ISTAT_COMUNE "
          + " AND    SP.DATA_FINE_VALIDITA IS NULL "
          + " AND    U.ID_AZIENDA = ? " + " AND    U.ID_UTE = CP.ID_UTE "
          + " AND    CP.DATA_INIZIO_CONDUZIONE < ? ";
      if (!particellaVO.getFlagStorico())
      {
        query += " AND CP.DATA_FINE_CONDUZIONE IS NULL ";
      }
      query += " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
          + " AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND    TTP.ID_TITOLO_POSSESSO = CP.ID_TITOLO_POSSESSO ";

      if (Validator.isNotEmpty(particellaVO.getIstatComuneParticella()))
      {
        query += " AND SP.COMUNE = ? ";
      }
      if (Validator.isNotEmpty(particellaVO.getSezione()))
      {
        query += " AND SP.SEZIONE = ? ";
      }
      if (Validator.isNotEmpty(particellaVO.getFoglio()))
      {
        query += " AND SP.FOGLIO = ? ";
      }
      if (Validator.isNotEmpty(particellaVO.getParticella()))
      {
        query += " AND SP.PARTICELLA = ? ";
      }
      if (particellaVO.getFlagNoCatastale())
      {
        query += " AND SP.PARTICELLA IS NULL";
      }
      if (Validator.isNotEmpty(particellaVO.getSubalterno()))
      {
        query += " AND SP.SUBALTERNO = ? ";
      }
      if (Validator.isNotEmpty(particellaVO.getIdTitoloPossesso()))
      {
        query += " AND CP.ID_TITOLO_POSSESSO = ? ";
      }

      query += " ORDER BY P.SIGLA_PROVINCIA, C.DESCOM, SP.SEZIONE, SP.FOGLIO, SP.PARTICELLA, "
          + "       SP.SUBALTERNO ";

      stmt = conn.prepareStatement(query);

      int indice = 0;

      stmt.setLong(++indice, idAzienda.longValue());
      stmt.setDate(++indice, new java.sql.Date(DateUtils.parseDate(
          "31/12/" + data).getTime()));

      if (Validator.isNotEmpty(particellaVO.getIstatComuneParticella()))
      {
        stmt.setString(++indice, particellaVO.getIstatComuneParticella());
      }
      else
      {
        if (particellaVO.getFlagEstero())
        {
          stmt.setString(++indice, SolmrConstants.FLAG_S);
        }
      }
      if (Validator.isNotEmpty(particellaVO.getSezione()))
      {
        stmt.setString(++indice, particellaVO.getSezione().toUpperCase());
      }
      if (Validator.isNotEmpty(particellaVO.getFoglio()))
      {
        stmt.setLong(++indice, particellaVO.getFoglio().longValue());
      }
      if (Validator.isNotEmpty(particellaVO.getParticella()))
      {
        stmt.setLong(++indice, particellaVO.getParticella().longValue());
      }
      if (Validator.isNotEmpty(particellaVO.getSubalterno()))
      {
        stmt.setString(++indice, particellaVO.getSubalterno());
      }
      if (Validator.isNotEmpty(particellaVO.getIdTitoloPossesso()))
      {
        stmt.setLong(++indice, particellaVO.getIdTitoloPossesso().longValue());
      }

      SolmrLogger.debug(this, "Executing ricercaParticelleAttiveByParametri: "
          + query);

      ResultSet rs = stmt.executeQuery();

      elencoParticelle = new Vector<ParticellaVO>();

      while (rs.next())
      {
        ParticellaVO particellaElencoVO = new ParticellaVO();
        particellaElencoVO.setIdStoricoParticella(new Long(rs.getLong(1)));
        if (rs.getString(4).equalsIgnoreCase(SolmrConstants.FLAG_S))
        {
          particellaElencoVO.setSiglaProvinciaParticella(null);
          particellaElencoVO.setDescStatoEsteroParticella(rs.getString(3));
        }
        else
        {
          particellaElencoVO.setSiglaProvinciaParticella(rs.getString(2));
          particellaElencoVO.setDescComuneParticella(rs.getString(3));
        }
        if (rs.getString(5) != null)
        {
          particellaElencoVO.setSezione(rs.getString(5).toUpperCase());
        }
        particellaElencoVO.setFoglio(new Long(rs.getLong(6)));
        if (Validator.isNotEmpty(rs.getString(7)))
        {
          particellaElencoVO.setParticella(new Long(rs.getLong(7)));
        }
        particellaElencoVO.setSubalterno(rs.getString(8));
        particellaElencoVO.setSupCatastale(StringUtils.parseSuperficieField(rs
            .getString(9)));
        particellaElencoVO.setSuperficieCondotta(StringUtils
            .parseSuperficieField(rs.getString(10)));
        particellaElencoVO.setDescrizioneTitoloPossesso(rs.getString(11));
        particellaElencoVO.setDataInizioConduzione(rs.getDate(12));
        particellaElencoVO.setIdConduzioneParticella(new Long(rs.getLong(13)));
        if (Validator.isNotEmpty(rs.getString(14)))
        {
          particellaElencoVO.setDataFineConduzione(rs.getDate(14));
        }
        particellaElencoVO.setEsitoControllo(rs.getString(15));
        particellaElencoVO.setIdTitoloPossesso(new Long(rs.getLong(16)));
        particellaElencoVO.setIdConduzioneParticella(new Long(rs.getLong(17)));
        elencoParticelle.add(particellaElencoVO);
      }

      stmt.close();
      rs.close();

      if (elencoParticelle == null || elencoParticelle.size() == 0)
      {
        throw new SolmrException((String) AnagErrors.get("ERR_NO_PARTICELLE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "ricercaParticelleAttiveByParametri - SQLException: "
              + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "ricercaParticelleAttiveByParametri - Generic Exception: " + ex);
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
                "ricercaParticelleAttiveByParametri - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "ricercaParticelleAttiveByParametri - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoParticelle;
  }

  // Metodo per controllare che non esistano conduzioni attive di particelle
  // associate ad
  // una azienda agricola
  public void checkCessaAziendaByConduzioneParticella(Long idUte)
      throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<Long> elencoIdConduzioniParticelle = new Vector<Long>();

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT ID_CONDUZIONE_PARTICELLA "
          + " FROM   DB_CONDUZIONE_PARTICELLA " + " WHERE  ID_UTE = ? "
          + " AND    DATA_FINE_CONDUZIONE IS NULL ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing query getElencoIdConduzioneParticelleAttiveByUte: "
              + query);

      stmt.setLong(1, idUte.longValue());

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        Long idConuduzioneParticella = new Long(rs.getLong(1));
        elencoIdConduzioniParticelle.add(idConuduzioneParticella);
      }

      stmt.close();
      rs.close();

      if (elencoIdConduzioniParticelle.size() > 0)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_UNITA_PRODUTTIVA_NO_CESSABILE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getElencoIdConduzioneParticelleAttiveByUte - SQLException: "
              + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getElencoIdConduzioneParticelleAttiveByUte - Generic Exception: "
              + ex);
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
                "getElencoIdConduzioneParticelleAttiveByUte - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getElencoIdConduzioneParticelleAttiveByUte - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
  }

  /**
   * Elenco delle provincie su cui insistono le particelle attive dell'azienda
   * 
   * @param idAzienda
   *          Long
   * @return Vector
   * @throws DataAccessException
   */
  public Vector<ProvinciaVO> getProvincieParticelleAzienda(Long idAzienda)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;

    Vector<ProvinciaVO> vProvincieTerreni = null;
    ProvinciaVO provinciaVO = null;

    SolmrLogger.debug(this, "idAzienda: " + idAzienda);

    try
    {
      conn = getDatasource().getConnection();

      String search = "select pro.istat_provincia, "
          + "       pro.id_regione, " + "       pro.sigla_provincia, "
          + "       pro.descrizione " + "  from provincia pro, "
          + "       comune co, " + "       db_storico_particella stp, "
          + "       db_conduzione_particella cdp, " + "       db_ute ute "
          + " where pro.ISTAT_PROVINCIA = co.ISTAT_PROVINCIA "
          + "   and co.ISTAT_COMUNE = stp.COMUNE "
          + "   and stp.DATA_FINE_VALIDITA is null "
          + "   and stp.ID_PARTICELLA = cdp.ID_PARTICELLA "
          + "   and cdp.DATA_FINE_CONDUZIONE is null "
          + "   and cdp.ID_UTE = ute.ID_UTE "
          + "group by pro.istat_provincia, " + "         pro.id_regione, "
          + "         pro.sigla_provincia, " + "         pro.descrizione ";

      stmt = conn.prepareStatement(search);

      stmt.setBigDecimal(1, convertLongToBigDecimal(idAzienda));

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      vProvincieTerreni = new Vector<ProvinciaVO>();

      while (rs.next())
      {
        provinciaVO = new ProvinciaVO();

        provinciaVO.setIstatProvincia(rs.getString("istat_provincia"));
        provinciaVO.setIdRegione(rs.getString("id_regione"));
        provinciaVO.setSiglaProvincia(rs.getString("sigla_provincia"));
        provinciaVO.setDescrizione(rs.getString("descrizione"));

        vProvincieTerreni.add(provinciaVO);
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this,
          "getProvincieParticelleAzienda - Executed query - Found "
              + vProvincieTerreni.size() + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getProvincieParticelleAzienda - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getProvincieParticelleAzienda - Generic Exception: "
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
                "getProvincieParticelleAzienda - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getProvincieParticelleAzienda - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return vProvincieTerreni;
  }

  /**
   * controlla che la data passata sia sia minore della data dell'ultima
   * dichiarazione di consistenza dell'azienda
   * 
   * @param idAzienda
   *          long
   * @param dataInizio
   *          Date
   * @return boolean
   * @throws DataAccessException
   */
  public boolean isDataInizioValida(long idAzienda, Date dataInizio)
      throws DataAccessException
  {
    boolean result = false;

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      Date dataDichiarazione = null;

      String query = null;

      if (dataInizio == null)
      {
        query = "SELECT SYSDATE AS DATAODIERNA FROM DUAL";
        stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        if (rs.next())
          dataInizio = rs.getTimestamp("DATAODIERNA");
        rs.close();
        stmt.close();
      }

      query = "SELECT MAX(C.DATA_INSERIMENTO_DICHIARAZIONE) AS DATA_DICHIARAZIONE "
          + "FROM DB_DICHIARAZIONE_CONSISTENZA C " + "WHERE C.ID_AZIENDA = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);
      SolmrLogger.debug(this, "idAzienda = " + idAzienda);
      SolmrLogger.debug(this, "dataInizio = " + dataInizio);

      stmt.setLong(1, idAzienda);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        dataDichiarazione = rs.getTimestamp("DATA_DICHIARAZIONE");

      if (dataDichiarazione == null)
        result = false;
      else
      {
        if (dataInizio.before(dataDichiarazione))
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
      SolmrLogger.fatal(this, "isDataInizioValida - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "isDataInizioValida - Generic Exception: "
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
            "isDataInizioValida - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "isDataInizioValida - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  /**
   * Restituisce la data di inizio validità di un fabbricato
   * 
   * @param idFabbricato
   *          long
   * @return boolean
   * @throws DataAccessException
   */
  public Date getDataInizioValidataFabbricato(long idFabbricato)
      throws DataAccessException
  {
    Connection conn = null;
    Date dataInizioValidita = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT DF.DATA_INIZIO_VALIDITA "
          + "FROM DB_FABBRICATO DF " + "WHERE DF.ID_FABBRICATO = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);
      SolmrLogger.debug(this, "idFabbricato = " + idFabbricato);

      stmt.setLong(1, idFabbricato);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        dataInizioValidita = rs.getTimestamp("DATA_INIZIO_VALIDITA");

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger
          .fatal(this, "getDataInizioValidataFabbricato - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getDataInizioValidataFabbricato - Generic Exception: "
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
                "getDataInizioValidataFabbricato - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getDataInizioValidataFabbricato - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return dataInizioValidita;
  }

  // Metodo che mi restituisce un vettore contenente le date delle "fotografie"
  // effettuate
  // su una specifica azienda individuata attraverso l'id azienda
  public Vector<CodeDescription> getListaDateConsistenza(Long idAzienda)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<CodeDescription> elencoDate = new Vector<CodeDescription>();

    try
    {

      conn = getDatasource().getConnection();

      String query = " SELECT   ID_DICHIARAZIONE_CONSISTENZA, ANNO, DATA "
          + " FROM     DB_DICHIARAZIONE_CONSISTENZA "
          + " WHERE    ID_AZIENDA = ? " + " ORDER BY ANNO DESC, DATA DESC ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setLong(1, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        CodeDescription code = new CodeDescription();
        code.setCode(new Integer(rs.getString(1)));
        String data = rs.getString(2)
            + " dichiarazione del "
            + StringUtils.parseDateFieldToEuropeStandard(
                SolmrConstants.FULL_DATE_ORACLE_FORMAT,
                SolmrConstants.FULL_DATE_EUROPE_FORMAT, rs.getString("DATA"));
        code.setDescription(data);
        elencoDate.add(code);
      }

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getListaDateConsistenza - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getListaDateConsistenza - Generic Exception: "
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
                "getListaDateConsistenza - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getListaDateConsistenza - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoDate;
  }

  // Metodo per recuperare l'elenco delle fotografie relative alle particelle
  // attraverso l'id azienda e il codice fotografia terreni
  public Vector<ParticellaVO> getElencoConsistenzaParticelleForAzienda(
      Long idAzienda, Long idDichiarazioneConsistenza) throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaVO> elencoParticelle = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT   ID_DICHIARAZIONE_CONSISTENZA, "
          + "          TTP.DESCRIZIONE, "
          + "          CD.ID_TITOLO_POSSESSO, "
          + "          SUM(CD.SUPERFICIE_CONDOTTA) "
          + " FROM     DB_TIPO_TITOLO_POSSESSO TTP, "
          + "          DB_CONDUZIONE_DICHIARATA CD, "
          + "          DB_DICHIARAZIONE_CONSISTENZA DC "
          + " WHERE    CD.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO "
          + " AND      DC.ID_AZIENDA = ? "
          + " AND      DC.ID_DICHIARAZIONE_CONSISTENZA = ? "
          + " AND      DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI "
          + " GROUP BY ID_DICHIARAZIONE_CONSISTENZA, CD.ID_TITOLO_POSSESSO, TTP.DESCRIZIONE "
          + " ORDER BY TTP.DESCRIZIONE ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing getElencoConsistenzaParticelleForAzienda: " + query);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setLong(2, idDichiarazioneConsistenza.longValue());

      ResultSet rs = stmt.executeQuery();

      elencoParticelle = new Vector<ParticellaVO>();

      while (rs.next())
      {
        ParticellaVO particellaVO = new ParticellaVO();
        particellaVO.setIdDichiarazioneConsistenza(new Long(rs.getLong(1)));
        particellaVO.setDescrizioneTitoloPossesso(rs.getString(2));
        particellaVO.setIdTitoloPossesso(new Long(rs.getLong(3)));
        particellaVO.setSuperficieCondotta(StringUtils.parseSuperficieField(rs
            .getString(4)));
        elencoParticelle.add(particellaVO);
      }

      stmt.close();
      rs.close();

      if (elencoParticelle == null || elencoParticelle.size() == 0)
      {
        throw new SolmrException((String) AnagErrors.get("ERR_NO_PARTICELLE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getElencoConsistenzaParticelleForAzienda - SQLException: "
              + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(this,
              "getElencoConsistenzaParticelleForAzienda - Generic Exception: "
                  + ex);
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
                "getElencoConsistenzaParticelleForAzienda - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getElencoConsistenzaParticelleForAzienda - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoParticelle;
  }

  // Metodo per recuperare la data dell'ultimo aggiornamento relativo alla
  // consistenza
  public Date getDataUltimoAggiornamentoConsistenza(Long idAzienda)
      throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Date dataUltimoAggiornamentoConsistenza = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT MAX(DATA_AGGIORNAMENTO) "
          + " FROM   V_AGGIORNAMENTO_CONSISTENZA " + " WHERE  ID_AZIENDA = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing getDataUltimoAggiornamentoConsistenza: " + query);

      stmt.setLong(1, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        dataUltimoAggiornamentoConsistenza = rs.getTimestamp(1);
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getDataUltimoAggiornamentoConsistenza - SQLException: "
              + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getDataUltimoAggiornamentoConsistenza - Generic Exception: " + ex);
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
                "getDataUltimoAggiornamentoConsistenza - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getDataUltimoAggiornamentoConsistenza - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return dataUltimoAggiornamentoConsistenza;
  }

  // Metodo per recuperare la data dell'ultima dichiarazione di consistenza
  public Date getMaxDataDichiarazioneConsistenza(Long idAzienda)
      throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Date dataMaxDichiarazioneConsistenza = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT MAX(DATA_INSERIMENTO_DICHIARAZIONE) "
          + " FROM   DB_DICHIARAZIONE_CONSISTENZA " + " WHERE  ID_AZIENDA = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing getMaxDataDichiarazioneConsistenza: "
          + query);

      stmt.setLong(1, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        if (Validator.isNotEmpty(rs.getString(1)))
        {
          dataMaxDichiarazioneConsistenza = rs.getTimestamp(1);
        }
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getMaxDataDichiarazioneConsistenza - SQLException: "
              + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getMaxDataDichiarazioneConsistenza - Generic Exception: " + ex);
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
                "getMaxDataDichiarazioneConsistenza - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getMaxDataDichiarazioneConsistenza - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return dataMaxDichiarazioneConsistenza;
  }

  // Metodo per recuperare l'elenco delle fotografie relative alle particelle
  // attraverso l'id azienda e il codice fotografia terreni
  public Vector<ParticellaVO> getElencoConsistenzaParticelleForAziendaAndPossessoAndComune(
      Long idAzienda, Long idDichiarazioneConsistenza) throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaVO> elencoParticelle = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT   CD.ID_TITOLO_POSSESSO, "
          + "          TTP.DESCRIZIONE, "
          + "          SP.COMUNE, "
          + "          C.DESCOM, "
          + "          P.SIGLA_PROVINCIA, "
          + "          SUM(CD.SUPERFICIE_CONDOTTA) "
          + " FROM     DB_TIPO_TITOLO_POSSESSO TTP, "
          + "          DB_CONDUZIONE_DICHIARATA CD, "
          + "          DB_DICHIARAZIONE_CONSISTENZA DC, "
          + "          DB_STORICO_PARTICELLA SP, "
          + "          COMUNE C, "
          + "          PROVINCIA P "
          + " WHERE    CD.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO "
          + " AND      CD.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND      SP.DATA_FINE_VALIDITA IS NULL "
          + " AND      DC.ID_AZIENDA = ? "
          + " AND      DC.ID_DICHIARAZIONE_CONSISTENZA = ? "
          + " AND      C.ISTAT_COMUNE = SP.COMUNE "
          + " AND      P.ISTAT_PROVINCIA = C.ISTAT_PROVINCIA "
          + " AND      DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI "
          + " GROUP BY CD.ID_TITOLO_POSSESSO, TTP.DESCRIZIONE, SP.COMUNE, C.DESCOM, "
          + "          P.SIGLA_PROVINCIA "
          + " ORDER BY TTP.DESCRIZIONE, C.DESCOM ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing getElencoConsistenzaParticelleForAziendaAndPossessoAndComune: "
              + query);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setLong(2, idDichiarazioneConsistenza.longValue());

      ResultSet rs = stmt.executeQuery();

      elencoParticelle = new Vector<ParticellaVO>();

      while (rs.next())
      {
        ParticellaVO particellaVO = new ParticellaVO();
        particellaVO.setIdTitoloPossesso(new Long(rs.getLong(1)));
        particellaVO.setDescrizioneTitoloPossesso(rs.getString(2));
        particellaVO.setIstatComuneParticella(rs.getString(3));
        particellaVO.setDescComuneParticella(rs.getString(4));
        particellaVO.setSiglaProvinciaParticella(rs.getString(5));
        particellaVO.setSuperficieCondotta(StringUtils.parseSuperficieField(rs
            .getString(6)));
        elencoParticelle.add(particellaVO);
      }

      stmt.close();
      rs.close();

      if (elencoParticelle == null || elencoParticelle.size() == 0)
      {
        throw new SolmrException((String) AnagErrors.get("ERR_NO_PARTICELLE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getElencoConsistenzaParticelleForAziendaAndPossessoAndComune - SQLException: "
              + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(
              this,
              "getElencoConsistenzaParticelleForAziendaAndPossessoAndComune - Generic Exception: "
                  + ex);
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
                "getElencoConsistenzaParticelleForAziendaAndPossessoAndComune - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getElencoConsistenzaParticelleForAziendaAndPossessoAndComune - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoParticelle;
  }

  // Metodo per recuperare l'elenco delle fotografie relative alle particelle
  // attraverso l'id azienda e il codice fotografia terreni
  public Vector<ParticellaVO> getElencoConsistenzaParticelleForAziendaAndComune(
      Long idAzienda, Long idDichiarazioneConsistenza) throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaVO> elencoParticelle = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT   SP.COMUNE, "
          + "          C.DESCOM, "
          + "          P.SIGLA_PROVINCIA, "
          + "          SUM(CD.SUPERFICIE_CONDOTTA) "
          + " FROM     DB_CONDUZIONE_DICHIARATA CD, "
          + "          DB_DICHIARAZIONE_CONSISTENZA DC, "
          + "          DB_STORICO_PARTICELLA SP, "
          + "          COMUNE C, "
          + "          PROVINCIA P "
          + " WHERE    CD.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND      SP.DATA_FINE_VALIDITA IS NULL "
          + " AND      DC.ID_AZIENDA = ? "
          + " AND      DC.ID_DICHIARAZIONE_CONSISTENZA = ? "
          + " AND      C.ISTAT_COMUNE = SP.COMUNE "
          + " AND      P.ISTAT_PROVINCIA = C.ISTAT_PROVINCIA "
          + " AND      DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI "
          + " GROUP BY SP.COMUNE, C.DESCOM, P.SIGLA_PROVINCIA "
          + " ORDER BY C.DESCOM ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing getElencoConsistenzaParticelleForAziendaAndComune: "
              + query);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setLong(2, idDichiarazioneConsistenza.longValue());

      ResultSet rs = stmt.executeQuery();

      elencoParticelle = new Vector<ParticellaVO>();

      while (rs.next())
      {
        ParticellaVO particellaVO = new ParticellaVO();
        particellaVO.setIstatComuneParticella(rs.getString(1));
        particellaVO.setDescComuneParticella(rs.getString(2));
        particellaVO.setSiglaProvinciaParticella(rs.getString(3));
        particellaVO.setSuperficieCondotta(StringUtils.parseSuperficieField(rs
            .getString(4)));
        elencoParticelle.add(particellaVO);
      }

      stmt.close();
      rs.close();

      if (elencoParticelle == null || elencoParticelle.size() == 0)
      {
        throw new SolmrException((String) AnagErrors.get("ERR_NO_PARTICELLE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getElencoConsistenzaParticelleForAziendaAndComune - SQLException: "
              + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getElencoConsistenzaParticelleForAziendaAndComune - Generic Exception: "
              + ex);
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
                "getElencoConsistenzaParticelleForAziendaAndComune - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getElencoConsistenzaParticelleForAziendaAndComune - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoParticelle;
  }

  // Metodo per recuperare l'anno successivo rispetto a quello di sistema nella
  // tabella degli utilizzi
  // inserito come previsione
  public String getAnnoPrevisioneUtilizzi(Long idAzienda)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    String annoPrevisione = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT DISTINCT UP.ANNO "
          + " FROM   DB_UTILIZZO_PARTICELLA UP, "
          + "        DB_CONDUZIONE_PARTICELLA CP, "
          + "        DB_UTE U "
          + " WHERE  U.ID_AZIENDA = ? "
          + " AND    U.DATA_FINE_ATTIVITA IS NULL "
          + " AND    U.ID_UTE = CP.ID_UTE "
          + " AND    CP.DATA_FINE_CONDUZIONE IS NULL "
          + " AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA "
          + " AND    UP.ANNO > ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing getAnnoPrevisioneUtilizzi: " + query);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setString(2, DateUtils.getCurrentYear().toString());

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        annoPrevisione = rs.getString(1);
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getAnnoPrevisioneUtilizzi - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getAnnoPrevisioneUtilizzi - Generic Exception: "
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
                "getAnnoPrevisioneUtilizzi - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getAnnoPrevisioneUtilizzi - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return annoPrevisione;
  }

  // Metodo per recuperare l'elenco delle particelle in relazione ad un anno
  // specificato
  // e agli utilizzi
  public Vector<ParticellaVO> getElencoParticelleForAziendaAndUtilizzo(
      Long idAzienda, String anno) throws SolmrException, DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaVO> elencoParticelle = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT   UP.ID_UTILIZZO, "
          + "          TU.DESCRIZIONE, "
          + "          TU.FLAG_SAU, "
          + "          TIU.ID_INDIRIZZO_UTILIZZO, "
          + "          SUM(UP.SUPERFICIE_UTILIZZATA) "
          + " FROM     DB_CONDUZIONE_PARTICELLA A, "
          + "          DB_UTE B, "
          + "          DB_UTILIZZO_PARTICELLA UP, "
          + "          DB_TIPO_UTILIZZO TU, "
          + "          DB_TIPO_INDIRIZZO_UTILIZZO TIU "
          + " WHERE    A.ID_UTE = B.ID_UTE "
          + " AND      A.DATA_FINE_CONDUZIONE IS NULL "
          + " AND      B.ID_AZIENDA = ? "
          + " AND      UP.ID_CONDUZIONE_PARTICELLA = A.ID_CONDUZIONE_PARTICELLA "
          + " AND      UP.ID_UTILIZZO = TU.ID_UTILIZZO "
          + " AND      UP.ANNO = ? "
          + " AND      TU.ID_INDIRIZZO_UTILIZZO = TIU.ID_INDIRIZZO_UTILIZZO "
          + " GROUP BY UP.ID_UTILIZZO, TU.DESCRIZIONE, TU.FLAG_SAU, TIU.ID_INDIRIZZO_UTILIZZO "
          + " ORDER BY TU.FLAG_SAU DESC, TU.DESCRIZIONE ASC ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing getElencoParticelleForAziendaAndUtilizzo: " + query);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setString(2, anno);

      ResultSet rs = stmt.executeQuery();

      elencoParticelle = new Vector<ParticellaVO>();

      while (rs.next())
      {
        ParticellaVO particellaVO = new ParticellaVO();
        ParticellaUtilizzoVO particellaUtilizzoVO = new ParticellaUtilizzoVO();
        particellaVO.setIdUtilizzoParticella(new Long(rs.getLong(1)));
        particellaVO.setDescUtilizzoParticella(rs.getString(2));
        particellaVO.setFlagSAU(rs.getString(3));
        particellaUtilizzoVO
            .setIdTipoIndirizzoUtilizzo(new Long(rs.getLong(4)));
        particellaVO.setSuperficieUtilizzata(StringUtils
            .parseSuperficieField(rs.getString(5)));
        particellaVO.setParticellaUtilizzoVO(particellaUtilizzoVO);
        elencoParticelle.add(particellaVO);
      }

      stmt.close();
      rs.close();

      if (elencoParticelle == null || elencoParticelle.size() == 0)
      {
        throw new SolmrException((String) AnagErrors.get("ERR_NO_PARTICELLE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getElencoParticelleForAziendaAndUtilizzo - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(this,
              "getElencoParticelleForAziendaAndUtilizzo - Generic Exception: "
                  + ex);
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
                "getElencoParticelleForAziendaAndUtilizzo - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getElencoParticelleForAziendaAndUtilizzo - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoParticelle;
  }

  // Metodo per recuperare l'elenco delle particelle in relazione ad un anno
  // specificato
  // e agli usi secondari
  public Vector<ParticellaUtilizzoVO> getElencoParticelleForAziendaAndUsoSecondario(
      Long idAzienda, String anno) throws SolmrException, DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaUtilizzoVO> elencoParticelle = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT   UP.ID_UTILIZZO_SECONDARIO, "
          + "          TU.DESCRIZIONE, "
          + "          TU.FLAG_SAU, "
          + "          SUM(UP.SUP_UTILIZZATA_SECONDARIA) "
          + " FROM     DB_CONDUZIONE_PARTICELLA A, "
          + "          DB_UTE B, "
          + "          DB_UTILIZZO_PARTICELLA UP, "
          + "          DB_TIPO_UTILIZZO TU "
          + " WHERE    A.ID_UTE = B.ID_UTE "
          + " AND      A.DATA_FINE_CONDUZIONE IS NULL "
          + " AND      B.ID_AZIENDA = ? "
          + " AND      UP.ID_CONDUZIONE_PARTICELLA = A.ID_CONDUZIONE_PARTICELLA "
          + " AND      UP.ID_UTILIZZO_SECONDARIO = TU.ID_UTILIZZO "
          + " AND      UP.ANNO = ? " + " GROUP BY ID_UTILIZZO_SECONDARIO, "
          + "          TU.DESCRIZIONE, " + "          TU.FLAG_SAU "
          + " ORDER BY TU.FLAG_SAU DESC, " + "          TU.DESCRIZIONE ASC ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing getElencoParticelleForAziendaAndUsoSecondario: " + query);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setString(2, anno);

      ResultSet rs = stmt.executeQuery();

      elencoParticelle = new Vector<ParticellaUtilizzoVO>();

      while (rs.next())
      {
        ParticellaUtilizzoVO particellaUtilizzoVO = new ParticellaUtilizzoVO();
        particellaUtilizzoVO.setIdUtilizzoSecondario(new Long(rs.getLong(1)));
        particellaUtilizzoVO.setDescTipoUtilizzoSecondario(rs.getString(2));
        particellaUtilizzoVO.setFlagSAU(rs.getString(3));
        particellaUtilizzoVO.setSuperficieUtilizzataSecondaria(StringUtils
            .parseSuperficieField(rs.getString(4)));
        elencoParticelle.add(particellaUtilizzoVO);
      }

      stmt.close();
      rs.close();

      if (elencoParticelle == null || elencoParticelle.size() == 0)
      {
        throw new SolmrException((String) AnagErrors.get("ERR_NO_PARTICELLE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getElencoParticelleForAziendaAndUsoSecondario - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getElencoParticelleForAziendaAndUsoSecondario - Generic Exception: "
              + ex);
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
                "getElencoParticelleForAziendaAndUsoSecondario - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getElencoParticelleForAziendaAndUsoSecondario - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoParticelle;
  }

  // Metodo per recuperare il valore di tutte le superfici condotte da
  // un'azienda agricola
  public String getTotaleSupCondotteByAzienda(Long idAzienda, String data)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    String totSuperficiCondotte = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT SUM(CP.SUPERFICIE_CONDOTTA) "
          + " FROM   DB_CONDUZIONE_PARTICELLA CP, " + "        DB_UTE U "
          + " WHERE  U.ID_AZIENDA = ? " + " AND    CP.ID_UTE = U.ID_UTE "
          + " AND    TRUNC(CP.DATA_INIZIO_CONDUZIONE) <= ? "
          + " AND    CP.DATA_FINE_CONDUZIONE IS NULL ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing getTotaleSupCondotteByAzienda: "
          + query);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setDate(2, new java.sql.Date(DateUtils.parse(data, "dd/MM/yyyy")
          .getTime()));

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        totSuperficiCondotte = StringUtils.parseSuperficieField((rs
            .getString(1)));
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getTotaleSupCondotteByAzienda - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getTotaleSupCondotteByAzienda - Generic Exception: " + ex);
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
                "getTotaleSupCondotteByAzienda - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getTotaleSupCondotteByAzienda - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return totSuperficiCondotte;
  }

  // Metodo per recuperare il valore di tutte le superfici condotte relative ad
  // una dichiarazione di
  // consistenza di un'azienda agricola
  public String getTotaleSupCondotteDichiarateByAzienda(Long idAzienda,
      Long idDichiarazioneConsistenza) throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    String totSuperficiCondotte = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT SUM(CD.SUPERFICIE_CONDOTTA) "
          + " FROM   DB_CONDUZIONE_DICHIARATA CD, "
          + "        DB_UTE U, "
          + "        DB_DICHIARAZIONE_CONSISTENZA DC"
          + " WHERE  U.ID_AZIENDA = ? "
          + " AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? "
          + " AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI "
          + " AND    CD.ID_UTE = U.ID_UTE ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing getTotaleSupCondotteDichiarateByAzienda: " + query);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setLong(2, idDichiarazioneConsistenza.longValue());

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        totSuperficiCondotte = StringUtils.parseSuperficieField((rs
            .getString(1)));
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getTotaleSupCondotteDichiarateByAzienda - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getTotaleSupCondotteDichiarateByAzienda - Generic Exception: " + ex);
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
                "getTotaleSupCondotteDichiarateByAzienda - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getTotaleSupCondotteDichiarateByAzienda - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return totSuperficiCondotte;
  }

  // Metodo per recuperare l'elenco delle fotografie relative alle particelle
  // attraverso l'id azienda e la destinazione d'uso
  public Vector<ParticellaVO> getElencoConsistenzaParticelleForAziendaAndDestinazioneUso(
      Long idAzienda, Long idDichiarazioneConsistenza,
      Long idConduzioneParticella) throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaVO> elencoParticelle = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT   UD.ID_UTILIZZO, "
          + "          TU.DESCRIZIONE, "
          + "          TU.FLAG_SAU, "
          + "          TIU.ID_INDIRIZZO_UTILIZZO, "
          + "          SUM(UD.SUPERFICIE_UTILIZZATA) "
          + " FROM     DB_TIPO_UTILIZZO TU, "
          + "          DB_UTILIZZO_DICHIARATO UD, "
          + "          DB_DICHIARAZIONE_CONSISTENZA DC, "
          + "          DB_CONDUZIONE_DICHIARATA CD, "
          + "          DB_TIPO_INDIRIZZO_UTILIZZO TIU "
          + " WHERE    DC.ID_AZIENDA = ? "
          + " AND      DC.ID_DICHIARAZIONE_CONSISTENZA = ? "
          + " AND      DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI "
          + " AND      CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA "
          + " AND      UD.ID_UTILIZZO = TU.ID_UTILIZZO "
          + " AND      TU.ID_INDIRIZZO_UTILIZZO = TIU.ID_INDIRIZZO_UTILIZZO ";

      if (idConduzioneParticella != null)
      {
        query += " AND CD.ID_CONDUZIONE_PARTICELLA = ? ";
      }

      query += " GROUP BY UD.ID_UTILIZZO, TU.DESCRIZIONE, TU.FLAG_SAU, TIU.ID_INDIRIZZO_UTILIZZO "
          + " ORDER BY TU.FLAG_SAU ASC, TU.DESCRIZIONE ASC ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing getElencoConsistenzaParticelleForAziendaAndDestinazioneUso: "
              + query);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setLong(2, idDichiarazioneConsistenza.longValue());
      if (idConduzioneParticella != null)
      {
        stmt.setLong(3, idConduzioneParticella.longValue());
      }

      ResultSet rs = stmt.executeQuery();

      elencoParticelle = new Vector<ParticellaVO>();

      while (rs.next())
      {
        ParticellaVO particellaVO = new ParticellaVO();
        ParticellaUtilizzoVO particellaUtilizzoVO = new ParticellaUtilizzoVO();
        particellaVO.setIdUtilizzoParticella(new Long(rs.getLong(1)));
        particellaVO.setDescUtilizzoParticella(rs.getString(2));
        particellaVO.setFlagSAU(rs.getString(3));
        particellaUtilizzoVO
            .setIdTipoIndirizzoUtilizzo(new Long(rs.getLong(4)));
        particellaVO.setSuperficieUtilizzata(StringUtils
            .parseSuperficieField(rs.getString(5)));
        particellaVO.setParticellaUtilizzoVO(particellaUtilizzoVO);
        elencoParticelle.add(particellaVO);
      }

      stmt.close();
      rs.close();

      if (elencoParticelle == null || elencoParticelle.size() == 0)
      {
        throw new SolmrException((String) AnagErrors.get("ERR_NO_PARTICELLE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getElencoConsistenzaParticelleForAziendaAndDestinazioneUso - SQLException: "
              + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(
              this,
              "getElencoConsistenzaParticelleForAziendaAndDestinazioneUso - Generic Exception: "
                  + ex);
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
                "getElencoConsistenzaParticelleForAziendaAndDestinazioneUso - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getElencoConsistenzaParticelleForAziendaAndDestinazioneUso - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoParticelle;
  }

  // Metodo per recuperare l'elenco delle fotografie relative alle particelle
  // attraverso l'id azienda e l'uso secondario
  public Vector<ParticellaUtilizzoVO> getElencoConsistenzaParticelleForAziendaAndUsoSecondario(
      Long idAzienda, Long idDichiarazioneConsistenza) throws SolmrException,
      DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaUtilizzoVO> elencoParticelle = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT   UD.ID_UTILIZZO_SECONDARIO, "
          + "          TU.DESCRIZIONE, "
          + "          TU.FLAG_SAU, "
          + "          SUM(UD.SUP_UTILIZZATA_SECONDARIA) "
          + " FROM     DB_TIPO_UTILIZZO TU, "
          + "          DB_UTILIZZO_DICHIARATO UD, "
          + "          DB_DICHIARAZIONE_CONSISTENZA DC, "
          + "          DB_CONDUZIONE_DICHIARATA CD "
          + " WHERE    DC.ID_AZIENDA = ? "
          + " AND      DC.ID_DICHIARAZIONE_CONSISTENZA = ? "
          + " AND      DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI "
          + " AND      CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA "
          + " AND      UD.ID_UTILIZZO_SECONDARIO(+) = TU.ID_UTILIZZO "
          + " GROUP BY UD.ID_UTILIZZO_SECONDARIO, "
          + "          TU.DESCRIZIONE, " + "          TU.FLAG_SAU "
          + " ORDER BY TU.FLAG_SAU ASC, " + "          TU.DESCRIZIONE ASC ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing getElencoConsistenzaParticelleForAziendaAndUsoSecondario: "
              + query);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setLong(2, idDichiarazioneConsistenza.longValue());

      ResultSet rs = stmt.executeQuery();

      elencoParticelle = new Vector<ParticellaUtilizzoVO>();

      while (rs.next())
      {
        if (Validator.isNotEmpty(rs.getString(1)))
        {
          ParticellaUtilizzoVO particellaUtilizzoVO = new ParticellaUtilizzoVO();
          particellaUtilizzoVO.setIdUtilizzoSecondario(new Long(rs.getLong(1)));
          particellaUtilizzoVO.setDescTipoUtilizzoSecondario(rs.getString(2));
          particellaUtilizzoVO.setFlagSAU(rs.getString(3));
          particellaUtilizzoVO.setSuperficieUtilizzataSecondaria(StringUtils
              .parseSuperficieField(rs.getString(4)));
          elencoParticelle.add(particellaUtilizzoVO);
        }
      }

      stmt.close();
      rs.close();

      if (elencoParticelle == null || elencoParticelle.size() == 0)
      {
        throw new SolmrException((String) AnagErrors.get("ERR_NO_PARTICELLE"));
      }
    }
    catch (SolmrException se)
    {
      SolmrLogger.fatal(this,
          "getElencoConsistenzaParticelleForAziendaAndUsoSecondario - SolmrException: "
              + se.getMessage());
      throw new SolmrException(se.getMessage());
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getElencoConsistenzaParticelleForAziendaAndUsoSecondario - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(
              this,
              "getElencoConsistenzaParticelleForAziendaAndUsoSecondario - Generic Exception: "
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
                "getElencoConsistenzaParticelleForAziendaAndUsoSecondario - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getElencoConsistenzaParticelleForAziendaAndUsoSecondario - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoParticelle;
  }

  // Metodo per effettuare la ricerca delle particelle storicizzate in relazione
  // ai parametri
  // di ricerca
  public Vector<ParticellaVO> ricercaParticelleStoricizzateByParametri(
      ParticellaVO particellaVO, Long idDichiarazioneConsistenza)
      throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaVO> elencoParticelle = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT CD.ID_CONDUZIONE_DICHIARATA, "
          + "        P.SIGLA_PROVINCIA, "
          + "        C.DESCOM, "
          + "        C.FLAG_ESTERO, "
          + "        SP.SEZIONE, "
          + "        SP.FOGLIO, "
          + "        SP.PARTICELLA, "
          + "        SP.SUBALTERNO, "
          + "        SP.SUP_CATASTALE, "
          + "        CD.SUPERFICIE_CONDOTTA, "
          + "        TTP.DESCRIZIONE, "
          + "        CD.DATA_INIZIO_CONDUZIONE, "
          + "        CD.DATA_FINE_CONDUZIONE "
          + " FROM   PROVINCIA P, "
          + "        COMUNE C, "
          + "        DB_STORICO_PARTICELLA SP, "
          + "        DB_TIPO_TITOLO_POSSESSO TTP, "
          + "        DB_DICHIARAZIONE_CONSISTENZA DC, "
          + "        DB_CONDUZIONE_DICHIARATA CD "
          + " WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? "
          + " AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI "
          + " AND    CD.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND    CD.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO "
          + " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
          + " AND    SP.COMUNE = C.ISTAT_COMUNE "
          + " AND    SP.DATA_FINE_VALIDITA IS NULL ";

      if (Validator.isNotEmpty(particellaVO.getIstatComuneParticella()))
      {
        query += " AND SP.COMUNE = ? ";
      }
      if (Validator.isNotEmpty(particellaVO.getSezione()))
      {
        query += " AND SP.SEZIONE = ? ";
      }
      if (Validator.isNotEmpty(particellaVO.getFoglio()))
      {
        query += " AND SP.FOGLIO = ? ";
      }
      if (Validator.isNotEmpty(particellaVO.getParticella()))
      {
        query += " AND SP.PARTICELLA = ? ";
      }
      if (particellaVO.getFlagNoCatastale())
      {
        query += " AND SP.PARTICELLA IS NULL";
      }
      if (Validator.isNotEmpty(particellaVO.getSubalterno()))
      {
        query += " AND SP.SUBALTERNO = ? ";
      }
      if (Validator.isNotEmpty(particellaVO.getIdTitoloPossesso()))
      {
        query += " AND CD.ID_TITOLO_POSSESSO = ? ";
      }

      query += " ORDER BY P.SIGLA_PROVINCIA, C.DESCOM, SP.SEZIONE, SP.FOGLIO, SP.PARTICELLA, "
          + "       SP.SUBALTERNO ";

      stmt = conn.prepareStatement(query);

      int indice = 0;

      stmt.setLong(++indice, idDichiarazioneConsistenza.longValue());

      if (Validator.isNotEmpty(particellaVO.getIstatComuneParticella()))
      {
        stmt.setString(++indice, particellaVO.getIstatComuneParticella());
      }
      else
      {
        if (particellaVO.getFlagEstero())
        {
          stmt.setString(++indice, SolmrConstants.FLAG_S);
        }
      }
      if (Validator.isNotEmpty(particellaVO.getSezione()))
      {
        stmt.setString(++indice, particellaVO.getSezione().toUpperCase());
      }
      if (Validator.isNotEmpty(particellaVO.getFoglio()))
      {
        stmt.setLong(++indice, particellaVO.getFoglio().longValue());
      }
      if (Validator.isNotEmpty(particellaVO.getParticella()))
      {
        stmt.setLong(++indice, particellaVO.getParticella().longValue());
      }
      if (Validator.isNotEmpty(particellaVO.getSubalterno()))
      {
        stmt.setString(++indice, particellaVO.getSubalterno());
      }
      if (Validator.isNotEmpty(particellaVO.getIdTitoloPossesso()))
      {
        stmt.setLong(++indice, particellaVO.getIdTitoloPossesso().longValue());
      }

      SolmrLogger.debug(this,
          "Executing ricercaParticelleStoricizzateByParametri: " + query);

      ResultSet rs = stmt.executeQuery();

      elencoParticelle = new Vector<ParticellaVO>();

      while (rs.next())
      {
        ParticellaVO particellaElencoVO = new ParticellaVO();
        particellaElencoVO.setIdConduzioneDichiarata(new Long(rs.getLong(1)));
        if (rs.getString(4).equalsIgnoreCase(SolmrConstants.FLAG_S))
        {
          particellaElencoVO.setSiglaProvinciaParticella(null);
          particellaElencoVO.setDescStatoEsteroParticella(rs.getString(3));
        }
        else
        {
          particellaElencoVO.setSiglaProvinciaParticella(rs.getString(2));
          particellaElencoVO.setDescComuneParticella(rs.getString(3));
        }
        if (rs.getString(5) != null)
        {
          particellaElencoVO.setSezione(rs.getString(5).toUpperCase());
        }
        particellaElencoVO.setFoglio(new Long(rs.getLong(6)));
        if (Validator.isNotEmpty(rs.getString(7)))
        {
          particellaElencoVO.setParticella(new Long(rs.getLong(7)));
        }
        particellaElencoVO.setSubalterno(rs.getString(8));
        particellaElencoVO.setSupCatastale(StringUtils.parseSuperficieField(rs
            .getString(9)));
        particellaElencoVO.setSuperficieCondotta(StringUtils
            .parseSuperficieField(rs.getString(10)));
        particellaElencoVO.setDescrizioneTitoloPossesso(rs.getString(11));
        particellaElencoVO.setDataInizioConduzione(rs.getDate(12));
        if (rs.getDate(13) != null)
        {
          particellaElencoVO.setDataFineConduzione(rs.getDate(13));
        }
        elencoParticelle.add(particellaElencoVO);
      }

      stmt.close();
      rs.close();

      if (elencoParticelle == null || elencoParticelle.size() == 0)
      {
        throw new SolmrException((String) AnagErrors.get("ERR_NO_PARTICELLE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "ricercaParticelleStoricizzateByParametri - SQLException: "
              + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(this,
              "ricercaParticelleStoricizzateByParametri - Generic Exception: "
                  + ex);
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
                "ricercaParticelleStoricizzateByParametri - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "ricercaParticelleStoricizzateByParametri - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoParticelle;
  }

  // Metodo per recuperare i dati territoriali della particella a partire
  // dall'id storico particella
  public ParticellaVO getDettaglioParticellaDatiTerritoriali(
      Long idStoricoParticella) throws SolmrException, DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    ParticellaVO particellaVO = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT /*rule*/ "
          + "        C.DESCOM, "
          + "        P.SIGLA_PROVINCIA, "
          + "        C1.DESCOM, "
          + "        SP.SEZIONE, "
          + "        SP.FOGLIO, "
          + "        SP.PARTICELLA, "
          + "        SP.SUBALTERNO, "
          + "        SP.SUP_CATASTALE, "
          + "        TZA.DESCRIZIONE, "
          + "        TAA.DESCRIZIONE, "
          + "        TAB.DESCRIZIONE, "
          + "        TAC.DESCRIZIONE, "
          + "        TAD.DESCRIZIONE, "
          + "        TAE.DESCRIZIONE, "
          + "        TCP.DESCRIZIONE, "
          + "        SP.FLAG_CAPTAZIONE_POZZI, "
          + "        SP.FLAG_IRRIGABILE, "
          + "        PARTICELLA.DATA_CREAZIONE, "
          + "        PARTICELLA.DATA_CESSAZIONE, "
          + "        SP.DATA_AGGIORNAMENTO, "
          + "        CP.SUPERFICIE_CONDOTTA, "
          + "        CP.ID_CONDUZIONE_PARTICELLA, "
          + "        S.DESCRIZIONE, "
          + "        SP.ID_AREA_A, "
          + "        SP.ID_AREA_B, "
          + "        SP.ID_AREA_C, "
          + "        SP.ID_AREA_D, "
          + "        SP.ID_ZONA_ALTIMETRICA, "
          + "        PARTICELLA.ID_PARTICELLA, "
          + "        SP.DATA_INIZIO_VALIDITA, "
          + "        SP.ID_STORICO_PARTICELLA, "
          + "        C1.ISTAT_COMUNE, "
          + "        SP.ID_CASO_PARTICOLARE, "
          + "        SP.MOTIVO_MODIFICA, "
          + "        SP.ID_FONTE, "
          + "        TF.DESCRIZIONE, "
          + "        TCMP.ID_CAUSALE_MOD_PARTICELLA, "
          + "        TCMP.DESCRIZIONE, "
          + "        SP.ID_DOCUMENTO, "
          + "        TD.DESCRIZIONE, "
          + "        SP.ID_UTENTE_AGGIORNAMENTO "
          + " FROM   DB_UTE U, "
          + "        COMUNE C, "
          + "        DB_CONDUZIONE_PARTICELLA CP, "
          + "        PROVINCIA P, "
          + "        DB_STORICO_PARTICELLA SP, "
          + "        COMUNE C1, "
          + "        DB_TIPO_ZONA_ALTIMETRICA TZA, "
          + "        DB_TIPO_AREA_A TAA, "
          + "        DB_TIPO_AREA_B TAB, "
          + "        DB_TIPO_AREA_C TAC, "
          + "        DB_TIPO_AREA_D TAD, "
          + "        DB_TIPO_AREA_E TAE, "
          + "        DB_FOGLIO F, "
          + "        DB_TIPO_CASO_PARTICOLARE TCP, "
          + "        DB_PARTICELLA PARTICELLA, "
          + "        DB_SEZIONE S, "
          + "        DB_TIPO_FONTE TF, "
          + "        DB_TIPO_CAUSALE_MOD_PARTICELLA TCMP, "
          + "        DB_TIPO_DOCUMENTO TD "
          + " WHERE  SP.ID_STORICO_PARTICELLA = ? "
          + " AND    PARTICELLA.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND    SP.DATA_FINE_VALIDITA IS NULL "
          + " AND    CP.ID_PARTICELLA(+) = SP.ID_PARTICELLA "
          + " AND    CP.DATA_FINE_CONDUZIONE IS NULL "
          + " AND    CP.ID_UTE = U.ID_UTE(+) "
          + " AND    U.COMUNE = C.ISTAT_COMUNE(+) "
          + " AND    SP.COMUNE = C1.ISTAT_COMUNE(+) "
          + " AND    C1.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
          + " AND    SP.ID_ZONA_ALTIMETRICA = TZA.ID_ZONA_ALTIMETRICA(+) "
          + " AND    SP.ID_AREA_A = TAA.ID_AREA_A(+) "
          + " AND    SP.ID_AREA_B = TAB.ID_AREA_B(+) "
          + " AND    SP.ID_AREA_C = TAC.ID_AREA_C(+) "
          + " AND    SP.ID_AREA_D = TAD.ID_AREA_D(+) "
          + " AND    nvl(SP.SEZIONE,'-')=nvl(F.SEZIONE(+),'-') "
          + " AND    F.COMUNE(+) = SP.COMUNE "
          + " AND    TAE.ID_AREA_E(+) = F.ID_AREA_E "
          + " AND    SP.FOGLIO = F.FOGLIO "
          + " AND    SP.ID_CASO_PARTICOLARE = TCP.ID_CASO_PARTICOLARE(+) "
          + " AND    SP.SEZIONE = S.SEZIONE(+) "
          + " AND    SP.COMUNE = S.ISTAT_COMUNE(+) "
          + " AND    TF.ID_FONTE(+) = SP.ID_FONTE "
          + " AND    TCMP.ID_CAUSALE_MOD_PARTICELLA(+) = SP.ID_CAUSALE_MOD_PARTICELLA "
          + " AND    TD.ID_DOCUMENTO(+) = SP.ID_DOCUMENTO ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idStoricoParticella.longValue());

      SolmrLogger.debug(this,
          "Executing getDettaglioParticellaDatiTerritoriali: " + query);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        particellaVO = new ParticellaVO();
        particellaVO.setIdStoricoParticella(idStoricoParticella);
        particellaVO.setDescrizioneUnitaProduttiva(rs.getString(1));
        particellaVO.setSiglaProvinciaParticella(rs.getString(2));
        particellaVO.setDescComuneParticella(rs.getString(3));
        particellaVO.setSezione(rs.getString(4));
        particellaVO.setFoglio(new Long(rs.getLong(5)));
        if (Validator.isNotEmpty(rs.getString(6)))
        {
          particellaVO.setParticella(new Long(rs.getLong(6)));
        }
        else
        {
          particellaVO.setParticellaProvvisoria(true);
        }
        particellaVO.setSubalterno(rs.getString(7));
        particellaVO.setSupCatastale(StringUtils.parseSuperficieField(rs
            .getString(8)));
        particellaVO.setDescrizioneZonaAltimetrica(rs.getString(9));
        particellaVO.setDescrizioneAreaA(rs.getString(10));
        particellaVO.setDescrizioneAreaB(rs.getString(11));
        particellaVO.setDescrizioneAreaC(rs.getString(12));
        particellaVO.setDescrizioneAreaD(rs.getString(13));
        particellaVO.setDescrizioneAreaE(rs.getString(14));
        particellaVO.setDescrizioneCasoParticolare(rs.getString(15));
        if (Validator.isNotEmpty(rs.getString(16)))
        {
          if (rs.getString(16).equalsIgnoreCase(SolmrConstants.FLAG_S))
          {
            particellaVO.setFlagCaptazionePozzi(true);
          }
          else
          {
            particellaVO.setFlagCaptazionePozzi(false);
          }
        }
        else
        {
          particellaVO.setFlagCaptazionePozzi(false);
        }
        if (Validator.isNotEmpty(rs.getString(17)))
        {
          if (rs.getString(17).equalsIgnoreCase(SolmrConstants.FLAG_S))
          {
            particellaVO.setFlagIrrigabile(true);
          }
          else
          {
            particellaVO.setFlagIrrigabile(false);
          }
        }
        else
        {
          particellaVO.setFlagIrrigabile(false);
        }
        particellaVO.setDataCreazione(rs.getDate(18));
        if (Validator.isNotEmpty(rs.getString(19)))
        {
          particellaVO.setDataCessazione(rs.getDate(19));
        }
        particellaVO.setDataAggiornamento(rs.getDate(20));
        particellaVO.setSuperficieCondotta(StringUtils.parseSuperficieField(rs
            .getString(21)));
        particellaVO.setIdConduzioneParticella(new Long(rs.getLong(22)));
        particellaVO.setDescrizioneSezione(rs.getString(23));
        if (Validator.isNotEmpty(rs.getString(24)))
        {
          particellaVO.setIdAreaA(new Long(rs.getLong(24)));
          particellaVO.setTipiAreaA(rs.getString(24));
        }
        if (Validator.isNotEmpty(rs.getString(25)))
        {
          particellaVO.setIdAreaB(new Long(rs.getLong(25)));
          particellaVO.setTipiAreaB(rs.getString(25));
        }
        if (Validator.isNotEmpty(rs.getString(26)))
        {
          particellaVO.setIdAreaC(new Long(rs.getLong(26)));
          particellaVO.setTipiAreaC(rs.getString(26));
        }
        if (Validator.isNotEmpty(rs.getString(27)))
        {
          particellaVO.setIdAreaD(new Long(rs.getLong(27)));
          particellaVO.setTipiAreaD(rs.getString(27));
        }
        if (Validator.isNotEmpty(rs.getString(28)))
        {
          particellaVO.setIdZonaAltimetrica(new Long(rs.getLong(28)));
          particellaVO.setTipiZonaAltimetrica(rs.getString(28));
        }
        particellaVO.setIdParticella(new Long(rs.getLong(29)));
        particellaVO.setDataInizioValidita(rs.getDate(30));
        particellaVO.setIdStoricoParticella(new Long(rs.getLong(31)));
        particellaVO.setIstatComuneParticella(rs.getString(32));
        if (Validator.isNotEmpty(rs.getString(33)))
        {
          particellaVO.setIdCasoParticolare(new Long(rs.getLong(33)));
          particellaVO.setTipiCasoParticolare(rs.getString(33));
        }
        particellaVO.setMotivoModifica(rs.getString(34));
        CodeDescription fonteDato = null;
        if (Validator.isNotEmpty(rs.getString(35)))
        {
          fonteDato = new CodeDescription(Integer.decode(rs.getString(35)), rs
              .getString(36));
        }
        particellaVO.setFonteDato(fonteDato);
        CodeDescription causaleModParticella = null;
        if (Validator.isNotEmpty(rs.getString(37)))
        {
          causaleModParticella = new CodeDescription(Integer.decode(rs
              .getString(37)), rs.getString(38));
        }
        particellaVO.setCausaleModParticella(causaleModParticella);
        CodeDescription tipoDocumento = null;
        if (Validator.isNotEmpty(rs.getString(39)))
        {
          tipoDocumento = new CodeDescription(Integer.decode(rs.getString(39)),
              rs.getString(40));
        }
        particellaVO.setTipoDocumento(tipoDocumento);
        particellaVO.setIdUtenteAggiornamento(new Long(rs.getLong(41)));
      }

      stmt.close();
      rs.close();

      if (particellaVO == null)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_PARTICELLA_INESISTENTE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getDettaglioParticellaDatiTerritoriali - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getDettaglioParticellaDatiTerritoriali - Generic Exception: " + ex);
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
                "getDettaglioParticellaDatiTerritoriali - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getDettaglioParticellaDatiTerritoriali - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return particellaVO;
  }

  // Metodo per recuperare i dati dell'uso del suolo e dei contratti della
  // particella a partire dall'id storico particella
  public Vector<ParticellaUtilizzoVO> getElencoParticellaUtilizzoVO(
      Long idConduzioneParticella, String anno) throws SolmrException,
      DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaUtilizzoVO> elencoParticellaUtilizzoVO = null;
    ParticellaUtilizzoVO particellaUtilizzoVO = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT   TU.DESCRIZIONE, "
          + "          SUM(UP.SUPERFICIE_UTILIZZATA) "
          + " FROM     DB_CONDUZIONE_PARTICELLA CP, "
          + "          (SELECT  ID_CONDUZIONE_PARTICELLA, "
          + "                   SUPERFICIE_UTILIZZATA, "
          + "                   ID_UTILIZZO "
          + "           FROM    DB_UTILIZZO_PARTICELLA "
          + "           WHERE   ANNO = ?) UP, "
          + "          DB_TIPO_UTILIZZO TU  "
          + " WHERE    CP.ID_CONDUZIONE_PARTICELLA = ? "
          + " AND      CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA(+) "
          + " AND      UP.ID_UTILIZZO = TU.ID_UTILIZZO(+) "
          + " GROUP BY TU.DESCRIZIONE ";

      stmt = conn.prepareStatement(query);

      stmt.setString(1, anno);
      stmt.setLong(2, idConduzioneParticella.longValue());

      SolmrLogger.debug(this, "Executing getElencoParticellaUtilizzoVO: "
          + query);

      ResultSet rs = stmt.executeQuery();

      elencoParticellaUtilizzoVO = new Vector<ParticellaUtilizzoVO>();
      while (rs.next())
      {
        if (Validator.isNotEmpty(rs.getString(1)))
        {
          particellaUtilizzoVO = new ParticellaUtilizzoVO();
          particellaUtilizzoVO.setIndirizziDescTipiUtilizzoAttivi(rs
              .getString(1));
          particellaUtilizzoVO.setSuperficieUtilizzata(StringUtils
              .parseSuperficieField(rs.getString(2)));
          elencoParticellaUtilizzoVO.add(particellaUtilizzoVO);
        }
      }
      if (elencoParticellaUtilizzoVO.size() == 0)
      {
        throw new SolmrException((String) AnagErrors.get("ERR_NO_PARTICELLE"));
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "elencoParticellaUtilizzoVO - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "elencoParticellaUtilizzoVO - Generic Exception: " + ex);
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
                "elencoParticellaUtilizzoVO - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "elencoParticellaUtilizzoVO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoParticellaUtilizzoVO;
  }

  // Metodo per recuperare l'elenco delle dichiarazioni di consistenza
  // attraverso l'id azienda
  public Vector<ParticellaVO> getElencoDichiarazioniConsistenzaByIdAzienda(
      Long idAzienda) throws SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaVO> elencoParticelle = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT   ID_DICHIARAZIONE_CONSISTENZA, "
          + "          TTP.DESCRIZIONE, "
          + "          CD.ID_TITOLO_POSSESSO, "
          + "          SUM(CD.SUPERFICIE_CONDOTTA) "
          + " FROM     DB_TIPO_TITOLO_POSSESSO TTP, "
          + "          DB_CONDUZIONE_DICHIARATA CD, "
          + "          DB_DICHIARAZIONE_CONSISTENZA DC "
          + " WHERE    CD.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO "
          + " AND      DC.ID_AZIENDA = ? "
          + " AND      DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI "
          + " GROUP BY ID_DICHIARAZIONE_CONSISTENZA, CD.ID_TITOLO_POSSESSO, TTP.DESCRIZIONE "
          + " ORDER BY TTP.DESCRIZIONE ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing getElencoDichiarazioniConsistenzaByIdAzienda: " + query);

      stmt.setLong(1, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      elencoParticelle = new Vector<ParticellaVO>();

      while (rs.next())
      {
        ParticellaVO particellaVO = new ParticellaVO();
        particellaVO.setIdDichiarazioneConsistenza(new Long(rs.getLong(1)));
        particellaVO.setDescrizioneTitoloPossesso(rs.getString(2));
        particellaVO.setIdTitoloPossesso(new Long(rs.getLong(3)));
        particellaVO.setSuperficieCondotta(StringUtils.parseSuperficieField(rs
            .getString(4)));
        elencoParticelle.add(particellaVO);
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getElencoDichiarazioniConsistenzaByIdAzienda - SQLException: "
              + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getElencoDichiarazioniConsistenzaByIdAzienda - Generic Exception: "
              + ex);
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
                "getElencoDichiarazioniConsistenzaByIdAzienda - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getElencoDichiarazioniConsistenzaByIdAzienda - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoParticelle;
  }

  // Metodo per recuperare il valore di tutte le superfici utilizzate da
  // un'azienda agricola
  public double getTotaleSupUtilizzateByIdConduzioneParticella(
      Long idConduzioneParticella, String anno) throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    double totSuperficiUtilizzate = 0;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT SUM(UP.SUPERFICIE_UTILIZZATA) "
          + " FROM   (SELECT ID_CONDUZIONE_PARTICELLA, "
          + "                SUPERFICIE_UTILIZZATA "
          + "         FROM   DB_UTILIZZO_PARTICELLA "
          + "         WHERE  ANNO = ?) UP, "
          + "        DB_CONDUZIONE_PARTICELLA CP "
          + " WHERE  CP.ID_CONDUZIONE_PARTICELLA = ? "
          + " AND    UP.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing getTotaleSupUtilizzateByIdConduzioneParticella: " + query);

      stmt.setString(1, anno);
      stmt.setLong(2, idConduzioneParticella.longValue());

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        totSuperficiUtilizzate = rs.getDouble(1);
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getTotaleSupUtilizzateByIdConduzioneParticella - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getTotaleSupUtilizzateByIdConduzioneParticella - Generic Exception: "
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
                "getTotaleSupUtilizzateByIdConduzioneParticella - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getTotaleSupUtilizzateByIdConduzioneParticella - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return totSuperficiUtilizzate;
  }

  // Metodo per recuperare i dati territoriali della particella a partire
  // dall'id dichiarazione
  // consistenza quindi si tratta di dati storicizzati
  public ParticellaVO getDettaglioParticellaStoricizzataDatiTerritoriali(
      Long idConduzioneDichiarata) throws SolmrException, DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    ParticellaVO particellaVO = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT C.DESCOM, "
          + "        P.SIGLA_PROVINCIA, "
          + "        C1.DESCOM, "
          + "        SP.SEZIONE, "
          + "        SP.FOGLIO, "
          + "        SP.PARTICELLA, "
          + "        SP.SUBALTERNO, "
          + "        SP.SUP_CATASTALE, "
          + "        TZA.DESCRIZIONE, "
          + "        TAA.DESCRIZIONE, "
          + "        TAB.DESCRIZIONE, "
          + "        TAC.DESCRIZIONE, "
          + "        TAD.DESCRIZIONE, "
          + "        TAE.DESCRIZIONE, "
          + "        TCP.DESCRIZIONE, "
          + "        SP.FLAG_CAPTAZIONE_POZZI, "
          + "        SP.FLAG_IRRIGABILE, "
          + "        P.DATA_CREAZIONE, "
          + "        P.DATA_CESSAZIONE, "
          + "        SP.DATA_AGGIORNAMENTO, " +
          "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
          "          || ' ' " + 
          "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
          "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
          "         WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
          "          AS DENOMINAZIONE, " +
          "       (SELECT PVU.DENOMINAZIONE " +
          "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
          "        WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
          "          AS DESCRIZIONE_ENTE_APPARTENENZA, "
          + "        CD.SUPERFICIE_CONDOTTA, "
          + "        CD.ID_CONDUZIONE_PARTICELLA, "
          + "        S.DESCRIZIONE, "
          + "        SP.ID_FONTE, "
          + "        TF.DESCRIZIONE, "
          + "        SP.ID_CAUSALE_MOD_PARTICELLA, "
          + "        TCMP.DESCRIZIONE, "
          + "        SP.ID_DOCUMENTO, "
          + "        TD.DESCRIZIONE, "
          + "        SP.MOTIVO_MODIFICA, "
          + "        SP.COMUNE "
          + " FROM   DB_UTE U, "
          + "        COMUNE C, "
          + "        DB_CONDUZIONE_DICHIARATA CD, "
          + "        PROVINCIA P, "
          + "        DB_STORICO_PARTICELLA SP, "
          + "        COMUNE C1, "
          + "        DB_TIPO_ZONA_ALTIMETRICA TZA, "
          + "        DB_TIPO_AREA_A TAA, "
          + "        DB_TIPO_AREA_B TAB, "
          + "        DB_TIPO_AREA_C TAC, "
          + "        DB_TIPO_AREA_D TAD, "
          + "        DB_TIPO_AREA_E TAE, "
          + "        DB_FOGLIO F, "
          + "        DB_TIPO_CASO_PARTICOLARE TCP, "
          + "        DB_PARTICELLA P, "
          //+ "        PAPUA_V_UTENTE_LOGIN PVU, "
          + "        DB_SEZIONE S, "
          + "        DB_TIPO_FONTE TF, "
          + "        DB_TIPO_CAUSALE_MOD_PARTICELLA TCMP, "
          + "        DB_TIPO_DOCUMENTO TD "
          + " WHERE  CD.ID_CONDUZIONE_DICHIARATA = ? "
          + " AND    CD.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND    P.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND    SP.DATA_FINE_VALIDITA IS NULL "
          + " AND    CD.ID_UTE = U.ID_UTE "
          + " AND    U.COMUNE = C.ISTAT_COMUNE "
          + " AND    SP.COMUNE = C1.ISTAT_COMUNE "
          + " AND    C1.ISTAT_COMUNE = SP.COMUNE "
          + " AND    C1.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
          + " AND    SP.ID_ZONA_ALTIMETRICA = TZA.ID_ZONA_ALTIMETRICA(+) "
          + " AND    SP.ID_AREA_A = TAA.ID_AREA_A(+) "
          + " AND    SP.ID_AREA_B = TAB.ID_AREA_B(+) "
          + " AND    SP.ID_AREA_C = TAC.ID_AREA_C(+) "
          + " AND    SP.ID_AREA_D = TAD.ID_AREA_D(+) "
          + " AND    F.FOGLIO(+) = SP.FOGLIO "
          + " AND    F.COMUNE(+) = SP.COMUNE "
          + " AND    TAE.ID_AREA_E(+) = F.ID_AREA_E "
          + " AND    SP.ID_CASO_PARTICOLARE = TCP.ID_CASO_PARTICOLARE(+) "
          //+ " AND    SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN "
          + " AND    SP.SEZIONE = S.SEZIONE(+) "
          + " AND    SP.COMUNE = S.ISTAT_COMUNE(+) "
          + " AND    TF.ID_FONTE(+) = SP.ID_FONTE "
          + " AND    TCMP.ID_CAUSALE_MOD_PARTICELLA(+) = SP.ID_CAUSALE_MOD_PARTICELLA"
          + " AND    TD.ID_DOCUMENTO(+) = SP.ID_DOCUMENTO ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idConduzioneDichiarata.longValue());

      SolmrLogger.debug(this,
          "Executing getDettaglioParticellaStoricizzataDatiTerritoriali: "
              + query);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        particellaVO = new ParticellaVO();
        particellaVO.setIdConduzioneDichiarata(idConduzioneDichiarata);
        particellaVO.setDescrizioneUnitaProduttiva(rs.getString(1));
        particellaVO.setSiglaProvinciaParticella(rs.getString(2));
        particellaVO.setDescComuneParticella(rs.getString(3));
        particellaVO.setSezione(rs.getString(4));
        particellaVO.setFoglio(new Long(rs.getLong(5)));
        if (Validator.isNotEmpty(rs.getString(6)))
        {
          particellaVO.setParticella(new Long(rs.getLong(6)));
        }
        particellaVO.setSubalterno(rs.getString(7));
        particellaVO.setSupCatastale(StringUtils.parseSuperficieField(rs
            .getString(8)));
        particellaVO.setDescrizioneZonaAltimetrica(rs.getString(9));
        particellaVO.setDescrizioneAreaA(rs.getString(10));
        particellaVO.setDescrizioneAreaB(rs.getString(11));
        particellaVO.setDescrizioneAreaC(rs.getString(12));
        particellaVO.setDescrizioneAreaD(rs.getString(13));
        particellaVO.setDescrizioneAreaE(rs.getString(14));
        particellaVO.setDescrizioneCasoParticolare(rs.getString(15));
        if (Validator.isNotEmpty(rs.getString(16)))
        {
          particellaVO.setFlagCaptazionePozzi(true);
        }
        else
        {
          particellaVO.setFlagCaptazionePozzi(false);
        }
        if (Validator.isNotEmpty(rs.getString(17)))
        {
          if (rs.getString(17).equalsIgnoreCase(SolmrConstants.FLAG_S))
          {
            particellaVO.setFlagIrrigabile(true);
          }
          else
          {
            particellaVO.setFlagIrrigabile(false);
          }
        }
        else
        {
          particellaVO.setFlagIrrigabile(false);
        }
        particellaVO.setDataCreazione(rs.getDate(18));
        if (Validator.isNotEmpty(rs.getString(19)))
        {
          particellaVO.setDataCessazione(rs.getDate(19));
        }
        particellaVO.setDataAggiornamento(rs.getDate(20));
        String denominazioneUtente = rs.getString(21);
        if (Validator.isNotEmpty(rs.getString(22)))
        {
          denominazioneUtente += " - " + rs.getString(22);
        }
        particellaVO.setDenominazioneUtenteAggiornamento(denominazioneUtente);
        particellaVO.setSuperficieCondotta(StringUtils.parseSuperficieField(rs
            .getString(23)));
        particellaVO.setIdConduzioneParticella(new Long(rs.getLong(24)));
        particellaVO.setDescrizioneSezione(rs.getString(25));
        CodeDescription fonteDato = null;
        if (Validator.isNotEmpty(rs.getString(26)))
        {
          fonteDato = new CodeDescription(Integer.decode(rs.getString(26)), rs
              .getString(27));
        }
        particellaVO.setFonteDato(fonteDato);
        CodeDescription causaleModParticella = null;
        if (Validator.isNotEmpty(rs.getString(28)))
        {
          causaleModParticella = new CodeDescription(Integer.decode(rs
              .getString(28)), rs.getString(29));
        }
        particellaVO.setCausaleModParticella(causaleModParticella);
        CodeDescription tipoDocumento = null;
        if (Validator.isNotEmpty(rs.getString(30)))
        {
          tipoDocumento = new CodeDescription(Integer.decode(rs.getString(30)),
              rs.getString(31));
        }
        particellaVO.setTipoDocumento(tipoDocumento);
        particellaVO.setMotivoModifica(rs.getString(32));
        particellaVO.setIstatComuneParticella(rs.getString(33));
      }

      stmt.close();
      rs.close();

      if (particellaVO == null)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_PARTICELLA_INESISTENTE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getDettaglioParticellaStoricizzataDatiTerritoriali - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getDettaglioParticellaStoricizzataDatiTerritoriali - Generic Exception: "
              + ex);
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
                "getDettaglioParticellaStoricizzataDatiTerritoriali - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getDettaglioParticellaStoricizzataDatiTerritoriali - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return particellaVO;
  }

  // Metodo per recuperare i dati della conduzione e dei contratti della
  // particella a partire dall'id conduzione
  // dichiarata e quindi si tratta di dati storicizzati
  public ParticellaVO getDettaglioParticellaStoricizzataConduzione(
      Long idConduzioneDichiarata) throws SolmrException, DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    ParticellaVO particellaVO = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = 
        "SELECT C3.DESCOM, " + 
        "       TTP.DESCRIZIONE, " + 
        "       CD.SUPERFICIE_CONDOTTA, " + 
        "       CD.DATA_INIZIO_CONDUZIONE, " + 
        "       CD.DATA_FINE_CONDUZIONE, " + 
        "       CD.NOTE, " + 
        "       CD.DATA_AGGIORNAMENTO, " + 
        "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
        "          || ' ' " + 
        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
        "         WHERE CD.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
        "       AS DENOMINAZIONE, " + 
        "       (SELECT PVU.DENOMINAZIONE " +
        "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
        "        WHERE CD.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
        "       AS DESCRIZIONE_ENTE_APPARTENENZA, " + 
        "       C2.DESCOM, " + 
        "       P.SIGLA_PROVINCIA, " + 
        "       C1.DESCOM, " + 
        "       SP.SEZIONE, " + 
        "       SP.FOGLIO, " + 
        "       SP.PARTICELLA, " + 
        "       SP.SUBALTERNO, " + 
        "       SP.SUP_CATASTALE, " + 
        "       CD.ID_CONDUZIONE_PARTICELLA, " + 
        "       S.SEZIONE " + 
        "FROM   DB_TIPO_TITOLO_POSSESSO TTP, " + 
        "       DB_CONDUZIONE_DICHIARATA CD, " + 
        "       DB_STORICO_PARTICELLA SP, " + 
        //"       PAPUA_V_UTENTE_LOGIN PVU, " + 
        "       COMUNE C2, " + 
        "       PROVINCIA P, " + 
        "       COMUNE C1, " + 
        "       DB_UTE U, "  + 
        "       DB_PARTICELLA P2, " + 
        "       DB_SEZIONE S, " + 
        "       COMUNE C3 " + 
        "WHERE  CD.ID_CONDUZIONE_DICHIARATA = ? " + 
        "AND    CD.ID_PARTICELLA = SP.ID_PARTICELLA " + 
        "AND    CD.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO " + 
       // "AND    PVU.ID_UTENTE_LOGIN = CD.ID_UTENTE_AGGIORNAMENTO " + 
        "AND    P2.ID_PARTICELLA = SP.ID_PARTICELLA " + 
        "AND    SP.DATA_FINE_VALIDITA IS NULL " + 
        "AND    CD.ID_UTE = U.ID_UTE " + 
        "AND    U.COMUNE = C2.ISTAT_COMUNE " + 
        "AND    SP.COMUNE = C1.ISTAT_COMUNE " + 
        "AND    C1.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " + 
        "AND    SP.SEZIONE = S.SEZIONE(+) " + 
        "AND    SP.COMUNE = S.ISTAT_COMUNE(+) " + 
        "AND    C3.ISTAT_COMUNE = U.COMUNE ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idConduzioneDichiarata.longValue());

      SolmrLogger.debug(this,
          "Executing getDettaglioParticellaStoricizzataConduzione: " + query);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        particellaVO = new ParticellaVO();
        particellaVO.setIdConduzioneDichiarata(idConduzioneDichiarata);
        particellaVO.setDescrizioneUnitaProduttiva(rs.getString(1));
        particellaVO.setDescrizioneTitoloPossesso(rs.getString(2));
        particellaVO.setSuperficieCondotta(StringUtils.parseSuperficieField(rs
            .getString(3)));
        particellaVO.setDataInizioConduzione(rs.getDate(4));
        if (rs.getDate(5) != null)
        {
          particellaVO.setDataFineConduzione(rs.getDate(5));
        }
        particellaVO.setNote(rs.getString(6));
        particellaVO.setDataAggiornamento(rs.getDate(7));
        particellaVO.setDenominazioneUtenteAggiornamento(rs.getString(8)
            + " - " + rs.getString(9));
        particellaVO.setDescrizioneUnitaProduttiva(rs.getString(10));
        particellaVO.setSiglaProvinciaParticella(rs.getString(11));
        particellaVO.setDescComuneParticella(rs.getString(12));
        particellaVO.setSezione(rs.getString(13));
        if (Validator.isNotEmpty(rs.getString(14)))
        {
          particellaVO.setFoglio(new Long(rs.getLong(14)));
        }
        if (Validator.isNotEmpty(rs.getString(15)))
        {
          particellaVO.setParticella(new Long(rs.getLong(15)));
        }
        particellaVO.setSubalterno(rs.getString(16));
        particellaVO.setSupCatastale(StringUtils.parseSuperficieField(rs
            .getString(17)));
        particellaVO.setIdConduzioneParticella(new Long(rs.getLong(18)));
        particellaVO.setDescrizioneSezione(rs.getString(19));
      }

      stmt.close();
      rs.close();

      if (particellaVO == null)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_PARTICELLA_INESISTENTE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getDettaglioParticellaStoricizzataConduzione - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getDettaglioParticellaStoricizzataConduzione - Generic Exception: "
              + ex);
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
                "getDettaglioParticellaStoricizzataConduzione - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getDettaglioParticellaStoricizzataConduzione - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return particellaVO;
  }

  // Metodo per recuperare i dati dell'uso del suolo della particella a partire
  // dall'id conduzione dichiarata
  // quindi sto prelevando l'elenco degli utilizzi storicizzati della particella
  public Vector<ParticellaUtilizzoVO> getElencoStoricoParticellaUtilizzoVO(
      Long idConduzioneDichiarata, Long idDichiarazioneConsistenza)
      throws SolmrException, DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaUtilizzoVO> elencoParticellaUtilizzoVO = null;
    ParticellaUtilizzoVO particellaUtilizzoVO = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT   TU.DESCRIZIONE, "
          + "          SUM(UD.SUPERFICIE_UTILIZZATA) "
          + " FROM     DB_CONDUZIONE_DICHIARATA CD, "
          + "          DB_UTILIZZO_DICHIARATO UD, "
          + "          DB_TIPO_UTILIZZO TU, "
          + "          DB_DICHIARAZIONE_CONSISTENZA DC "
          + " WHERE    CD.ID_CONDUZIONE_DICHIARATA = ? "
          + " AND      DC.ID_DICHIARAZIONE_CONSISTENZA = ? "
          + " AND      DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI "
          + " AND      CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA(+) "
          + " AND      UD.ID_UTILIZZO = TU.ID_UTILIZZO(+) "
          + " GROUP BY TU.DESCRIZIONE ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idConduzioneDichiarata.longValue());
      stmt.setLong(2, idDichiarazioneConsistenza.longValue());

      SolmrLogger.debug(this,
          "Executing getElencoStoricoParticellaUtilizzoVO: " + query);

      ResultSet rs = stmt.executeQuery();

      elencoParticellaUtilizzoVO = new Vector<ParticellaUtilizzoVO>();

      while (rs.next())
      {
        if (Validator.isNotEmpty(rs.getString(1)))
        {
          particellaUtilizzoVO = new ParticellaUtilizzoVO();
          particellaUtilizzoVO.setIndirizziDescTipiUtilizzoAttivi(rs
              .getString(1));
          particellaUtilizzoVO.setSuperficieUtilizzata(StringUtils
              .parseSuperficieField(rs.getString(2)));
          elencoParticellaUtilizzoVO.add(particellaUtilizzoVO);
        }
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getElencoStoricoParticellaUtilizzoVO - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getElencoStoricoParticellaUtilizzoVO - Generic Exception: " + ex);
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
                "getElencoStoricoParticellaUtilizzoVO - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getElencoStoricoParticellaUtilizzoVO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoParticellaUtilizzoVO;
  }

  // Metodo per recuperare i tipi utilizzo dato un indirizzo tipo utilizzo
  public Vector<CodeDescription> getTipiUtilizzoForIdIndirizzoTipoUtilizzo(
      Long idTipoIndirizzoUtilizzo) throws DataAccessException, SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<CodeDescription> elencoTipiUtilizzo = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT   ID_UTILIZZO, DESCRIZIONE "
          + " FROM     DB_TIPO_UTILIZZO "
          + " WHERE    ID_INDIRIZZO_UTILIZZO = ? "
          + " AND      ANNO_FINE_VALIDITA IS NULL " + " ORDER BY DESCRIZIONE ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idTipoIndirizzoUtilizzo.longValue());

      SolmrLogger.debug(this, "Executing query: " + query);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        elencoTipiUtilizzo = new Vector<CodeDescription>();
        while (rs.next())
        {
          CodeDescription code = new CodeDescription();
          code.setCode(new Integer(rs.getInt(1)));
          code.setDescription(rs.getString(2));
          elencoTipiUtilizzo.add(code);
        }
      }

      stmt.close();
      rs.close();

      if (elencoTipiUtilizzo == null || elencoTipiUtilizzo.size() == 0)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_NO_UTILIZZI_ATTIVI"));
      }

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getTipiUtilizzoForIdIndirizzoTipoUtilizzo - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getTipiUtilizzoForIdIndirizzoTipoUtilizzo - Generic Exception: "
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
                "getTipiUtilizzoForIdIndirizzoTipoUtilizzo - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getTipiUtilizzoForIdIndirizzoTipoUtilizzo - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoTipiUtilizzo;
  }

  // Metodo per recuperare l'elenco delle particelle in relazione a dei filtri
  // di ricerca e agli utilizzi
  public Vector<ParticellaVO> ricercaParticelleByParametriAndUtilizzi(
      ParticellaVO particellaRicercaVO, Long idAzienda) throws SolmrException,
      DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaVO> elencoParticelle = null;

    SolmrLogger.debug(this,
        "\n[FascicoloDAO.ricercaParticelleByParametriAndUtilizzi() BEGIN]\n");

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT SP.ID_STORICO_PARTICELLA, "
          + "        CP.ID_CONDUZIONE_PARTICELLA, "
          + "        P.SIGLA_PROVINCIA, "
          + "        C.DESCOM, "
          + "        C.FLAG_ESTERO, "
          + "        SP.SEZIONE, "
          + "        SP.FOGLIO, "
          + "        SP.PARTICELLA, "
          + "        SP.SUBALTERNO, "
          + "        SP.SUP_CATASTALE, "
          + "        CP.SUPERFICIE_CONDOTTA, "
          + "        TTP.DESCRIZIONE, "
          + "        TU.DESCRIZIONE, "
          + "        UP.SUPERFICIE_UTILIZZATA, "
          + "        TU2.DESCRIZIONE, "
          + "        UP.SUP_UTILIZZATA_SECONDARIA, "
          + "        UP.NOTE, "
          + "        UP.ID_UTILIZZO_PARTICELLA, "
          + "        CP.ESITO_CONTROLLO, "
          + "        CP.RECORD_MODIFICATO, "
          + "        CP.ID_TITOLO_POSSESSO "
          + " FROM   PROVINCIA P, "
          + "        COMUNE C, "
          + "        DB_STORICO_PARTICELLA SP, "
          + "        DB_CONDUZIONE_PARTICELLA CP, "
          + "        DB_UTE U, "
          + "        DB_TIPO_UTILIZZO TU, "
          + "        DB_TIPO_UTILIZZO TU2, "
          + "        DB_TIPO_INDIRIZZO_UTILIZZO TUI, "
          + "        (SELECT  ID_CONDUZIONE_PARTICELLA, "
          + "                 SUPERFICIE_UTILIZZATA, "
          + "                 ID_UTILIZZO, "
          + "                 ID_UTILIZZO_PARTICELLA, "
          + "                 ID_UTILIZZO_SECONDARIO, "
          + "                 SUP_UTILIZZATA_SECONDARIA, "
          + "                 NOTE "
          + "         FROM    DB_UTILIZZO_PARTICELLA "
          + "         WHERE   ANNO = ?) UP, "
          + "         DB_TIPO_TITOLO_POSSESSO TTP "
          + " WHERE  U.ID_UTE = CP.ID_UTE "
          + " AND    SP.COMUNE = C.ISTAT_COMUNE "
          + " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
          + " AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND    SP.DATA_FINE_VALIDITA IS NULL "
          + " AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA(+) "
          + " AND    UP.ID_UTILIZZO = TU.ID_UTILIZZO(+) "
          + " AND    U.ID_AZIENDA = ? "
          + " AND    CP.DATA_FINE_CONDUZIONE IS NULL "
          + " AND    TUI.ID_INDIRIZZO_UTILIZZO =  TU.ID_INDIRIZZO_UTILIZZO "
          + " AND    TTP.ID_TITOLO_POSSESSO = CP.ID_TITOLO_POSSESSO "
          + " AND    TU2.ID_UTILIZZO(+) = UP.ID_UTILIZZO_SECONDARIO ";

      if (Validator.isNotEmpty(particellaRicercaVO.getIdSegnalazione())
          && particellaRicercaVO.getIdSegnalazione().compareTo(new Long(0)) != 0)
      {
        if (particellaRicercaVO.getIdSegnalazione().compareTo(new Long(1)) == 0)
        {
          query += " AND (CP.ESITO_CONTROLLO = ? OR CP.ESITO_CONTROLLO = ?) ";
        }
        else
        {
          query += " AND CP.ESITO_CONTROLLO = ? ";
        }
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getIstatComuneParticella()))
      {
        query += " AND SP.COMUNE = ? ";
      }
      else
      {
        if (particellaRicercaVO.getFlagEstero())
        {
          query += " AND C.FLAG_ESTERO = ? ";
        }
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSezione()))
      {
        query += " AND SP.SEZIONE = ? ";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getFoglio()))
      {
        query += " AND SP.FOGLIO = ? ";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticella()))
      {
        query += " AND SP.PARTICELLA = ? ";
      }
      if (particellaRicercaVO.getFlagNoCatastale())
      {
        query += " AND SP.PARTICELLA IS NULL";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSubalterno()))
      {
        query += " AND SP.SUBALTERNO = ? ";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticellaUtilizzoVO()))
      {
        if (Validator.isNotEmpty(particellaRicercaVO.getParticellaUtilizzoVO()
            .getIdTipoIndirizzoUtilizzo()))
        {
          query += " AND TUI.ID_INDIRIZZO_UTILIZZO = ? ";
        }
        if (Validator.isNotEmpty(particellaRicercaVO.getParticellaUtilizzoVO()
            .getIdTipoUtilizzo()))
        {
          query += " AND TU.ID_UTILIZZO = ? ";
        }
      }

      query += " UNION SELECT SP.ID_STORICO_PARTICELLA, "
          + "              CP.ID_CONDUZIONE_PARTICELLA, "
          + "              P.SIGLA_PROVINCIA, "
          + "              C.DESCOM, "
          + "              C.FLAG_ESTERO, "
          + "              SP.SEZIONE, "
          + "              SP.FOGLIO, "
          + "              SP.PARTICELLA, "
          + "              SP.SUBALTERNO, "
          + "              SP.SUP_CATASTALE, "
          + "              CP.SUPERFICIE_CONDOTTA, "
          + "              TTP.DESCRIZIONE, "
          + "              '', "
          + "              (CP.SUPERFICIE_CONDOTTA - SUM(NVL(UP.SUPERFICIE_UTILIZZATA,0))) SUPERFICIE_UTILIZZATA, '' ,"
          + "              0, "
          + "              '', "
          + "              -1, "
          + "              CP.ESITO_CONTROLLO, "
          + "              CP.RECORD_MODIFICATO, "
          + "              CP.ID_TITOLO_POSSESSO "
          + "              FROM PROVINCIA P, "
          + "              COMUNE C, "
          + "              DB_STORICO_PARTICELLA SP, "
          + "              DB_CONDUZIONE_PARTICELLA CP, "
          + "              DB_UTE U, "
          + "              DB_TIPO_UTILIZZO TU, "
          + "              DB_TIPO_UTILIZZO TU2, "
          + "              DB_TIPO_INDIRIZZO_UTILIZZO TIU, "
          + "              (SELECT  ID_CONDUZIONE_PARTICELLA, "
          + "                       SUPERFICIE_UTILIZZATA, "
          + "                       ID_UTILIZZO, "
          + "                       ID_UTILIZZO_PARTICELLA, "
          + "                       ID_UTILIZZO_SECONDARIO, "
          + "                       SUP_UTILIZZATA_SECONDARIA, "
          + "                       NOTE "
          + "               FROM    DB_UTILIZZO_PARTICELLA "
          + "               WHERE   ANNO = ?) UP, "
          + "              DB_TIPO_TITOLO_POSSESSO TTP "
          + "              WHERE U.ID_UTE = CP.ID_UTE "
          + "              AND SP.COMUNE = C.ISTAT_COMUNE "
          + "              AND C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
          + "              AND CP.ID_PARTICELLA = SP.ID_PARTICELLA "
          + "              AND SP.DATA_FINE_VALIDITA IS NULL "
          + "              AND CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA(+) "
          + "              AND TIU.ID_INDIRIZZO_UTILIZZO(+) = TU.ID_INDIRIZZO_UTILIZZO "
          + "              AND UP.ID_UTILIZZO = TU.ID_UTILIZZO(+) "
          + "              AND U.ID_AZIENDA = ? "
          + "              AND CP.DATA_FINE_CONDUZIONE IS NULL "
          + "              AND TTP.ID_TITOLO_POSSESSO = CP.ID_TITOLO_POSSESSO "
          + "              AND TU2.ID_UTILIZZO(+) = UP.ID_UTILIZZO_SECONDARIO ";

      if (Validator.isNotEmpty(particellaRicercaVO.getIdSegnalazione())
          && particellaRicercaVO.getIdSegnalazione().compareTo(new Long(0)) != 0)
      {
        if (particellaRicercaVO.getIdSegnalazione().compareTo(new Long(1)) == 0)
        {
          query += " AND (CP.ESITO_CONTROLLO = ? OR CP.ESITO_CONTROLLO = ?) ";
        }
        else
        {
          query += " AND CP.ESITO_CONTROLLO = ? ";
        }
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getIstatComuneParticella()))
      {
        query += " AND SP.COMUNE = ? ";
      }
      else
      {
        if (particellaRicercaVO.getFlagEstero())
        {
          query += " AND C.FLAG_ESTERO = ? ";
        }
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSezione()))
      {
        query += " AND SP.SEZIONE = ? ";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getFoglio()))
      {
        query += " AND SP.FOGLIO = ? ";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticella()))
      {
        query += " AND SP.PARTICELLA = ? ";
      }
      if (particellaRicercaVO.getFlagNoCatastale())
      {
        query += " AND SP.PARTICELLA IS NULL";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSubalterno()))
      {
        query += " AND SP.SUBALTERNO = ? ";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticellaUtilizzoVO()))
      {
        if (Validator.isNotEmpty(particellaRicercaVO.getParticellaUtilizzoVO()
            .getIdTipoIndirizzoUtilizzo()))
        {
          query += " AND TIU.ID_INDIRIZZO_UTILIZZO = ? ";
        }
        if (Validator.isNotEmpty(particellaRicercaVO.getParticellaUtilizzoVO()
            .getIdTipoUtilizzo()))
        {
          query += " AND TU.ID_UTILIZZO = ? ";
        }
      }

      query += " GROUP BY SP.ID_STORICO_PARTICELLA, "
          + "          CP.ID_CONDUZIONE_PARTICELLA, "
          + "          P.SIGLA_PROVINCIA, "
          + "          C.DESCOM, "
          + "          C.FLAG_ESTERO, "
          + "          SP.SEZIONE, "
          + "          SP.FOGLIO, "
          + "          SP.PARTICELLA, "
          + "          SP.SUBALTERNO, "
          + "          SP.SUP_CATASTALE, "
          + "          CP.SUPERFICIE_CONDOTTA, "
          + "          TTP.DESCRIZIONE, "
          + "          CP.ID_TITOLO_POSSESSO, "
          + "          CP.ESITO_CONTROLLO, "
          + "          CP.RECORD_MODIFICATO "
          + "          HAVING SUM(NVL(UP.SUPERFICIE_UTILIZZATA,0)) < CP.SUPERFICIE_CONDOTTA "
          + "          ORDER BY 3, 4, 6, 7, 8, 9, 12 ";

      stmt = conn.prepareStatement(query);

      int indice = 0;

      stmt.setString(++indice, particellaRicercaVO.getParticellaUtilizzoVO()
          .getAnnoUtilizzo());
      stmt.setLong(++indice, idAzienda.longValue());

      if (Validator.isNotEmpty(particellaRicercaVO.getIdSegnalazione())
          && particellaRicercaVO.getIdSegnalazione().compareTo(new Long(0)) != 0)
      {
        if (particellaRicercaVO.getIdSegnalazione().compareTo(new Long(1)) == 0)
        {
          stmt.setString(++indice, (String) SolmrConstants
              .get("ESITO_CONTROLLO_BLOCCANTE"));
          stmt.setString(++indice, (String) SolmrConstants
              .get("ESITO_CONTROLLO_WARNING"));
        }
        else
        {
          if (particellaRicercaVO.getIdSegnalazione().compareTo(new Long(2)) == 0)
          {
            stmt.setString(++indice, (String) SolmrConstants
                .get("ESITO_CONTROLLO_BLOCCANTE"));
          }
          else
          {
            stmt.setString(++indice, (String) SolmrConstants
                .get("ESITO_CONTROLLO_WARNING"));
          }
        }
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getIstatComuneParticella()))
      {
        stmt
            .setString(++indice, particellaRicercaVO.getIstatComuneParticella());
      }
      else
      {
        if (particellaRicercaVO.getFlagEstero())
        {
          stmt.setString(++indice, SolmrConstants.FLAG_S);
        }
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSezione()))
      {
        stmt
            .setString(++indice, particellaRicercaVO.getSezione().toUpperCase());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getFoglio()))
      {
        stmt.setLong(++indice, particellaRicercaVO.getFoglio().longValue());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticella()))
      {
        stmt.setLong(++indice, particellaRicercaVO.getParticella().longValue());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSubalterno()))
      {
        stmt.setString(++indice, particellaRicercaVO.getSubalterno());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticellaUtilizzoVO()
          .getIdTipoIndirizzoUtilizzo()))
      {
        stmt.setLong(++indice, particellaRicercaVO.getParticellaUtilizzoVO()
            .getIdTipoIndirizzoUtilizzo().longValue());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticellaUtilizzoVO()
          .getIdTipoUtilizzo()))
      {
        stmt.setLong(++indice, particellaRicercaVO.getParticellaUtilizzoVO()
            .getIdTipoUtilizzo().longValue());
      }

      // INIZIO SETTAGGIO PARAMETRI RELATIVI ALLA UNION
      stmt.setString(++indice, particellaRicercaVO.getParticellaUtilizzoVO()
          .getAnnoUtilizzo());
      stmt.setLong(++indice, idAzienda.longValue());

      if (Validator.isNotEmpty(particellaRicercaVO.getIdSegnalazione())
          && particellaRicercaVO.getIdSegnalazione().compareTo(new Long(0)) != 0)
      {
        if (particellaRicercaVO.getIdSegnalazione().compareTo(new Long(1)) == 0)
        {
          stmt.setString(++indice, (String) SolmrConstants
              .get("ESITO_CONTROLLO_BLOCCANTE"));
          stmt.setString(++indice, (String) SolmrConstants
              .get("ESITO_CONTROLLO_WARNING"));
        }
        else
        {
          if (particellaRicercaVO.getIdSegnalazione().compareTo(new Long(2)) == 0)
          {
            stmt.setString(++indice, (String) SolmrConstants
                .get("ESITO_CONTROLLO_BLOCCANTE"));
          }
          else
          {
            stmt.setString(++indice, (String) SolmrConstants
                .get("ESITO_CONTROLLO_WARNING"));
          }
        }
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getIstatComuneParticella()))
      {
        stmt
            .setString(++indice, particellaRicercaVO.getIstatComuneParticella());
      }
      else
      {
        if (particellaRicercaVO.getFlagEstero())
        {
          stmt.setString(++indice, SolmrConstants.FLAG_S);
        }
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSezione()))
      {
        stmt
            .setString(++indice, particellaRicercaVO.getSezione().toUpperCase());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getFoglio()))
      {
        stmt.setLong(++indice, particellaRicercaVO.getFoglio().longValue());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticella()))
      {
        stmt.setLong(++indice, particellaRicercaVO.getParticella().longValue());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSubalterno()))
      {
        stmt.setString(++indice, particellaRicercaVO.getSubalterno());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticellaUtilizzoVO()
          .getIdTipoIndirizzoUtilizzo()))
      {
        stmt.setLong(++indice, particellaRicercaVO.getParticellaUtilizzoVO()
            .getIdTipoIndirizzoUtilizzo().longValue());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticellaUtilizzoVO()
          .getIdTipoUtilizzo()))
      {
        stmt.setLong(++indice, particellaRicercaVO.getParticellaUtilizzoVO()
            .getIdTipoUtilizzo().longValue());
      }

      SolmrLogger.debug(this,
          "Executing query ricercaParticelleByParametriAndUtilizzi: " + query);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        elencoParticelle = new Vector<ParticellaVO>();
        while (rs.next())
        {
          ParticellaVO particellaVO = new ParticellaVO();
          ParticellaUtilizzoVO particellaUtilizzoVO = new ParticellaUtilizzoVO();
          particellaVO.setIdStoricoParticella(new Long(rs.getLong(1)));
          particellaVO.setIdConduzioneParticella(new Long(rs.getLong(2)));
          if (rs.getString(5) != null)
          {
            if (rs.getString(5).equalsIgnoreCase(SolmrConstants.FLAG_S))
            {
              particellaVO.setDescStatoEsteroParticella(rs.getString(4));
              particellaVO.setDescComuneParticella(null);
              particellaVO.setSiglaProvinciaParticella(null);
            }
            else
            {
              particellaVO.setSiglaProvinciaParticella(rs.getString(3));
              particellaVO.setDescComuneParticella(rs.getString(4));
            }
          }
          particellaVO.setSezione(rs.getString(6));
          particellaVO.setFoglio(new Long(rs.getLong(7)));
          if (rs.getString(8) != null && !rs.getString(8).equals(""))
          {
            particellaVO.setParticella(new Long(rs.getLong(8)));
          }
          particellaVO.setSubalterno(rs.getString(9));
          particellaVO.setSupCatastale(StringUtils.parseSuperficieField(rs
              .getString(10)));
          particellaVO.setSuperficieCondotta(StringUtils
              .parseSuperficieField(rs.getString(11)));
          particellaVO.setDescrizioneTitoloPossesso(rs.getString(12));
          if (Validator.isNotEmpty(rs.getString(13)))
          {
            particellaUtilizzoVO.setDescTipoUtilizzo(rs.getString(13));
          }
          else
          {
            particellaUtilizzoVO.setDescTipoUtilizzo("-");
          }
          if (Validator.isNotEmpty(rs.getString(14)))
          {
            particellaUtilizzoVO.setSuperficieUtilizzata(StringUtils
                .parseSuperficieField(rs.getString(14)));
          }
          // Setto il valore dell'eventuale utilizzo secondario
          if (Validator.isNotEmpty(rs.getString(15)))
          {
            particellaUtilizzoVO
                .setDescTipoUtilizzoSecondario(rs.getString(15));
          }
          else
          {
            particellaUtilizzoVO.setDescTipoUtilizzoSecondario(" - ");
          }
          // Setto il valore eventuale della superficie secondaria
          if (Validator.isNotEmpty(rs.getString(16))
              && !rs.getString(16).equalsIgnoreCase("0"))
          {
            particellaUtilizzoVO.setSuperficieUtilizzataSecondaria(StringUtils
                .parseSuperficieField(rs.getString(16)));
          }
          else
          {
            particellaUtilizzoVO.setSuperficieUtilizzataSecondaria(" - ");
          }
          if (Validator.isNotEmpty(rs.getString(17)))
          {
            if (rs.getString(17).length() > 20)
            {
              particellaUtilizzoVO.setNote(rs.getString(17).substring(0, 20)
                  + "...");
            }
            else
            {
              particellaUtilizzoVO.setNote(rs.getString(17));
            }
          }
          if (!rs.getString(18).equals("-1"))
          {
            particellaUtilizzoVO.setIdUtilizzoParticella(new Long(rs
                .getLong(18)));
          }
          particellaVO.setParticellaUtilizzoVO(particellaUtilizzoVO);
          particellaVO.setEsitoControllo(rs.getString(19));
          particellaVO.setIdTitoloPossesso(checkLongNull(rs
              .getString("ID_TITOLO_POSSESSO")));
          String temp = rs.getString("RECORD_MODIFICATO");
          if (temp != null)
            if (temp.equalsIgnoreCase(SolmrConstants.FLAG_S))
              particellaVO.setRecordModificato(true);

          elencoParticelle.add(particellaVO);
        }
      }

      stmt.close();
      rs.close();

      if (elencoParticelle == null || elencoParticelle.size() == 0)
      {
        throw new SolmrException((String) AnagErrors.get("ERR_NO_PARTICELLE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "ricercaParticelleByParametriAndUtilizzi - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "ricercaParticelleByParametriAndUtilizzi - Generic Exception: " + ex);
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
                "ricercaParticelleByParametriAndUtilizzi - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "ricercaParticelleByParametriAndUtilizzi - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoParticelle;
  }

  // Metodo per effettuare la ricerca delle particelle a partire dai parametri
  // relativi alla particella
  // e ad uno speficifico utilizzo
  public Vector<ParticellaVO> ricercaParticelleByParametriAndUtilizzoSpecificato(
      Long idAzienda, ParticellaVO particellaRicercaVO)
      throws DataAccessException, SolmrException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaVO> elencoParticelle = null;

    SolmrLogger
        .debug(this,
            "\n[FascicoloDAO.ricercaParticelleByParametriAndUtilizzoSpecificato() BEGIN]\n");

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT SP.ID_STORICO_PARTICELLA, "
          + "        CP.ID_CONDUZIONE_PARTICELLA, "
          + "        P.SIGLA_PROVINCIA, "
          + "        C.DESCOM, "
          + "        C.FLAG_ESTERO, "
          + "        SP.SEZIONE, "
          + "        SP.FOGLIO, "
          + "        SP.PARTICELLA, "
          + "        SP.SUBALTERNO, "
          + "        SP.SUP_CATASTALE, "
          + "        CP.SUPERFICIE_CONDOTTA, "
          + "        TTP.DESCRIZIONE, "
          + "        TU.DESCRIZIONE, "
          + "        UP.SUPERFICIE_UTILIZZATA, "
          + "        TU2.DESCRIZIONE, "
          + "        UP.SUP_UTILIZZATA_SECONDARIA, "
          + "        CP.ESITO_CONTROLLO, "
          + "        UP.NOTE, "
          + "        UP.ID_UTILIZZO_PARTICELLA, "
          + "        CP.RECORD_MODIFICATO, "
          + "        CP.ID_TITOLO_POSSESSO "
          + " FROM   PROVINCIA P, "
          + "        COMUNE C, "
          + "        DB_STORICO_PARTICELLA SP, "
          + "        DB_CONDUZIONE_PARTICELLA CP, "
          + "        DB_UTE U, "
          + "        DB_TIPO_UTILIZZO TU, "
          + "        DB_TIPO_UTILIZZO TU2, "
          + "        DB_TIPO_INDIRIZZO_UTILIZZO TUI, "
          + "        DB_UTILIZZO_PARTICELLA UP, "
          + "        DB_TIPO_TITOLO_POSSESSO TTP "
          + " WHERE  U.ID_UTE = CP.ID_UTE "
          + " AND    SP.COMUNE = C.ISTAT_COMUNE "
          + " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
          + " AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND    SP.DATA_FINE_VALIDITA IS NULL "
          + " AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA "
          + " AND    UP.ID_UTILIZZO = TU.ID_UTILIZZO "
          + " AND    U.ID_AZIENDA = ? "
          + " AND    CP.DATA_FINE_CONDUZIONE IS NULL " + " AND    UP.ANNO = ? "
          + " AND    TUI.ID_INDIRIZZO_UTILIZZO = ? "
          + " AND    TU.ID_INDIRIZZO_UTILIZZO = TUI.ID_INDIRIZZO_UTILIZZO "
          + " AND    CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO "
          + " AND    TU2.ID_UTILIZZO(+) = UP.ID_UTILIZZO_SECONDARIO ";

      if (Validator.isNotEmpty(particellaRicercaVO.getIdSegnalazione())
          && particellaRicercaVO.getIdSegnalazione().compareTo(new Long(0)) != 0)
      {
        if (particellaRicercaVO.getIdSegnalazione().compareTo(new Long(1)) == 0)
        {
          query += " AND (CP.ESITO_CONTROLLO = ? OR CP.ESITO_CONTROLLO = ?) ";
        }
        else
        {
          query += " AND CP.ESITO_CONTROLLO = ? ";
        }
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getIstatComuneParticella()))
      {
        query += " AND SP.COMUNE = ? ";
      }
      else
      {
        if (particellaRicercaVO.getFlagEstero())
        {
          query += " AND C.FLAG_ESTERO = ? ";
        }
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSezione()))
      {
        query += " AND SP.SEZIONE = ? ";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getFoglio()))
      {
        query += " AND SP.FOGLIO = ? ";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticella()))
      {
        query += " AND SP.PARTICELLA = ? ";
      }
      if (particellaRicercaVO.getFlagNoCatastale())
      {
        query += " AND SP.PARTICELLA IS NULL";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSubalterno()))
      {
        query += " AND SP.SUBALTERNO = ? ";
      }

      if (Validator.isNotEmpty(particellaRicercaVO.getParticellaUtilizzoVO()
          .getIdTipoUtilizzo()))
      {
        query += " AND TU.ID_UTILIZZO = ? ";
      }

      query += " ORDER BY P.SIGLA_PROVINCIA, " + "          C.DESCOM, "
          + "          SP.SEZIONE, " + "          SP.FOGLIO, "
          + "          SP.PARTICELLA, " + "          SP.SUBALTERNO, "
          + "          TU.DESCRIZIONE ";

      stmt = conn.prepareStatement(query);

      int indice = 0;

      stmt.setLong(++indice, idAzienda.longValue());
      stmt.setString(++indice, particellaRicercaVO.getParticellaUtilizzoVO()
          .getAnnoUtilizzo());
      stmt.setLong(++indice, particellaRicercaVO.getParticellaUtilizzoVO()
          .getIdTipoIndirizzoUtilizzo().longValue());
      if (Validator.isNotEmpty(particellaRicercaVO.getIdSegnalazione())
          && particellaRicercaVO.getIdSegnalazione().compareTo(new Long(0)) != 0)
      {
        if (particellaRicercaVO.getIdSegnalazione().compareTo(new Long(1)) == 0)
        {
          stmt.setString(++indice, (String) SolmrConstants
              .get("ESITO_CONTROLLO_BLOCCANTE"));
          stmt.setString(++indice, (String) SolmrConstants
              .get("ESITO_CONTROLLO_WARNING"));
        }
        else
        {
          if (particellaRicercaVO.getIdSegnalazione().compareTo(new Long(2)) == 0)
          {
            stmt.setString(++indice, (String) SolmrConstants
                .get("ESITO_CONTROLLO_BLOCCANTE"));
          }
          else
          {
            stmt.setString(++indice, (String) SolmrConstants
                .get("ESITO_CONTROLLO_WARNING"));
          }
        }
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getIstatComuneParticella()))
      {
        stmt
            .setString(++indice, particellaRicercaVO.getIstatComuneParticella());
      }
      else
      {
        if (particellaRicercaVO.getFlagEstero())
        {
          stmt.setString(++indice, SolmrConstants.FLAG_S);
        }
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSezione()))
      {
        stmt
            .setString(++indice, particellaRicercaVO.getSezione().toUpperCase());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getFoglio()))
      {
        stmt.setLong(++indice, particellaRicercaVO.getFoglio().longValue());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticella()))
      {
        stmt.setLong(++indice, particellaRicercaVO.getParticella().longValue());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSubalterno()))
      {
        stmt.setString(++indice, particellaRicercaVO.getSubalterno());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticellaUtilizzoVO()
          .getIdTipoUtilizzo()))
      {
        stmt.setLong(++indice, particellaRicercaVO.getParticellaUtilizzoVO()
            .getIdTipoUtilizzo().longValue());
      }

      SolmrLogger.debug(this,
          "Executing query ricercaParticelleByParametriAndUtilizzoSpecificato: "
              + query);

      ResultSet rs = stmt.executeQuery();

      elencoParticelle = new Vector<ParticellaVO>();
      while (rs.next())
      {
        ParticellaVO particellaVO = new ParticellaVO();
        ParticellaUtilizzoVO particellaUtilizzoVO = new ParticellaUtilizzoVO();
        particellaVO.setIdStoricoParticella(new Long(rs.getLong(1)));
        particellaVO.setIdConduzioneParticella(new Long(rs.getLong(2)));
        if (rs.getString(5) != null)
        {
          if (rs.getString(5).equalsIgnoreCase(SolmrConstants.FLAG_S))
          {
            particellaVO.setDescStatoEsteroParticella(rs.getString(4));
            particellaVO.setDescComuneParticella(null);
            particellaVO.setSiglaProvinciaParticella(null);
          }
          else
          {
            particellaVO.setSiglaProvinciaParticella(rs.getString(3));
            particellaVO.setDescComuneParticella(rs.getString(4));
          }
        }
        particellaVO.setSezione(rs.getString(6));
        particellaVO.setFoglio(new Long(rs.getLong(7)));
        if (rs.getString(8) != null && !rs.getString(8).equals(""))
        {
          particellaVO.setParticella(new Long(rs.getLong(8)));
        }
        particellaVO.setSubalterno(rs.getString(9));
        particellaVO.setSupCatastale(StringUtils.parseSuperficieField(rs
            .getString(10)));
        particellaVO.setSuperficieCondotta(StringUtils.parseSuperficieField(rs
            .getString(11)));
        particellaVO.setDescrizioneTitoloPossesso(rs.getString(12));
        particellaUtilizzoVO.setDescTipoUtilizzo(rs.getString(13));
        if (Validator.isNotEmpty(rs.getString(14)))
        {
          particellaUtilizzoVO.setSuperficieUtilizzata(StringUtils
              .parseSuperficieField(rs.getString(14)));
        }
        // Setto l'eventuale valore dell'utilizzo secondario
        if (Validator.isNotEmpty(rs.getString(15)))
        {
          particellaUtilizzoVO.setDescTipoUtilizzoSecondario(rs.getString(15));
        }
        else
        {
          particellaUtilizzoVO.setDescTipoUtilizzoSecondario(" - ");
        }
        // Setto l'evetuale valore della superficie secondaria utilizzata
        if (Validator.isNotEmpty(rs.getString(16)))
        {
          particellaUtilizzoVO.setSuperficieUtilizzataSecondaria(StringUtils
              .parseSuperficieField(rs.getString(16)));
        }
        else
        {
          particellaUtilizzoVO.setSuperficieUtilizzataSecondaria(" - ");
        }
        // Setto il valore di esito controllo
        particellaVO.setEsitoControllo(rs.getString(17));
        if (Validator.isNotEmpty(rs.getString(18)))
        {
          if (rs.getString(18).length() > 20)
          {
            particellaUtilizzoVO.setNote(rs.getString(18).substring(0, 20)
                + "...");
          }
          else
          {
            particellaUtilizzoVO.setNote(rs.getString(18));
          }
        }
        if (Validator.isNotEmpty(rs.getString(19)))
        {
          particellaUtilizzoVO
              .setIdUtilizzoParticella(new Long(rs.getLong(19)));
        }
        particellaVO.setParticellaUtilizzoVO(particellaUtilizzoVO);
        particellaVO.setIdTitoloPossesso(checkLongNull(rs
            .getString("ID_TITOLO_POSSESSO")));
        String temp = rs.getString("RECORD_MODIFICATO");
        if (temp != null)
          if (temp.equalsIgnoreCase(SolmrConstants.FLAG_S))
            particellaVO.setRecordModificato(true);
        elencoParticelle.add(particellaVO);
      }
      stmt.close();
      rs.close();

      if (elencoParticelle == null || elencoParticelle.size() == 0)
      {
        throw new SolmrException((String) AnagErrors.get("ERR_NO_PARTICELLE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "ricercaParticelleByParametriAndUtilizzoSpecificato - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "ricercaParticelleByParametriAndUtilizzoSpecificato - Generic Exception: "
              + ex);
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
                "ricercaParticelleByParametriAndUtilizzoSpecificato - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "ricercaParticelleByParametriAndUtilizzoSpecificato - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoParticelle;
  }

  // Metodo per recuperare l'elenco delle particelle in relazione a dei filtri
  // di ricerca e all'assenza
  // di utilizzi o al fatto che la somma delle superfici utilizzate relative ad
  // una conduzione non
  // sia uguale alla superficie condotta
  public Vector<ParticellaVO> ricercaParticelleByParametriSenzaUsoSuolo(
      ParticellaVO particellaRicercaVO, Long idAzienda) throws SolmrException,
      DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaVO> elencoParticelle = null;

    SolmrLogger.debug(this,
        "\n[FascicoloDAO.ricercaParticelleByParametriSenzaUsoSuolo() BEGIN]\n");

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT SP.ID_STORICO_PARTICELLA, "
          + "        CP.ID_CONDUZIONE_PARTICELLA, "
          + "        P.SIGLA_PROVINCIA, "
          + "        C.DESCOM, "
          + "        C.FLAG_ESTERO, "
          + "        SP.SEZIONE, "
          + "        SP.FOGLIO, "
          + "        SP.PARTICELLA, "
          + "        SP.SUBALTERNO, "
          + "        SP.SUP_CATASTALE, "
          + "        CP.SUPERFICIE_CONDOTTA, "
          + "        TTP.DESCRIZIONE, "
          + "        '-', "
          + "        (CP.SUPERFICIE_CONDOTTA - SUM(NVL(UP.SUPERFICIE_UTILIZZATA, 0))) SUPERFICIE_UTILIZZATA, "
          + "        CP.ESITO_CONTROLLO, "
          + "        CP.RECORD_MODIFICATO, "
          + "        CP.ID_TITOLO_POSSESSO "
          + " FROM   PROVINCIA P, "
          + "        COMUNE C, "
          + "        DB_STORICO_PARTICELLA SP, "
          + "        DB_CONDUZIONE_PARTICELLA CP, "
          + "        DB_UTE U, "
          + "        DB_TIPO_TITOLO_POSSESSO TTP, "
          + "        (SELECT  ID_CONDUZIONE_PARTICELLA, SUPERFICIE_UTILIZZATA "
          + "         FROM    DB_UTILIZZO_PARTICELLA "
          + "         WHERE   ANNO = ?) UP "
          + " WHERE  U.ID_UTE = CP.ID_UTE  AND "
          + "        SP.COMUNE = C.ISTAT_COMUNE  AND "
          + "        C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA  AND "
          + "        CP.ID_PARTICELLA = SP.ID_PARTICELLA  AND "
          + "        SP.DATA_FINE_VALIDITA IS NULL AND"
          + "        CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA(+)  AND "
          + "        U.ID_AZIENDA = ? AND "
          + "        CP.DATA_FINE_CONDUZIONE IS NULL AND "
          + "        TTP.ID_TITOLO_POSSESSO = CP.ID_TITOLO_POSSESSO ";

      if (Validator.isNotEmpty(particellaRicercaVO.getIdSegnalazione())
          && particellaRicercaVO.getIdSegnalazione().compareTo(new Long(0)) != 0)
      {
        if (particellaRicercaVO.getIdSegnalazione().compareTo(new Long(1)) == 0)
        {
          query += " AND (CP.ESITO_CONTROLLO = ? OR CP.ESITO_CONTROLLO = ?) ";
        }
        else
        {
          query += " AND CP.ESITO_CONTROLLO = ? ";
        }
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getIstatComuneParticella()))
      {
        query += " AND SP.COMUNE = ? ";
      }
      else
      {
        if (particellaRicercaVO.getFlagEstero())
        {
          query += " AND C.FLAG_ESTERO = ? ";
        }
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSezione()))
      {
        query += " AND SP.SEZIONE = ? ";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getFoglio()))
      {
        query += " AND SP.FOGLIO = ? ";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticella()))
      {
        query += " AND SP.PARTICELLA = ? ";
      }
      if (particellaRicercaVO.getFlagNoCatastale())
      {
        query += " AND SP.PARTICELLA IS NULL";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSubalterno()))
      {
        query += " AND SP.SUBALTERNO = ? ";
      }

      query += " GROUP BY SP.ID_STORICO_PARTICELLA, "
          + "          CP.ID_CONDUZIONE_PARTICELLA, "
          + "          P.SIGLA_PROVINCIA, "
          + "          C.DESCOM, "
          + "          C.FLAG_ESTERO, "
          + "          SP.SEZIONE, "
          + "          SP.FOGLIO, "
          + "          SP.PARTICELLA, "
          + "          SP.SUBALTERNO, "
          + "          SP.SUP_CATASTALE, "
          + "          CP.SUPERFICIE_CONDOTTA, "
          + "          TTP.DESCRIZIONE, "
          + "          CP.ID_TITOLO_POSSESSO, "
          + "          CP.ESITO_CONTROLLO, "
          + "          CP.RECORD_MODIFICATO "
          + " HAVING   SUM(NVL(UP.SUPERFICIE_UTILIZZATA,0)) < CP.SUPERFICIE_CONDOTTA "
          + " ORDER BY 3, 4, 6, 7, 8, 9 ";

      stmt = conn.prepareStatement(query);

      int indice = 0;

      stmt.setString(++indice, particellaRicercaVO.getParticellaUtilizzoVO()
          .getAnnoUtilizzo());
      stmt.setLong(++indice, idAzienda.longValue());
      if (Validator.isNotEmpty(particellaRicercaVO.getIdSegnalazione())
          && particellaRicercaVO.getIdSegnalazione().compareTo(new Long(0)) != 0)
      {
        if (particellaRicercaVO.getIdSegnalazione().compareTo(new Long(1)) == 0)
        {
          stmt.setString(++indice, (String) SolmrConstants
              .get("ESITO_CONTROLLO_BLOCCANTE"));
          stmt.setString(++indice, (String) SolmrConstants
              .get("ESITO_CONTROLLO_WARNING"));
        }
        else
        {
          if (particellaRicercaVO.getIdSegnalazione().compareTo(new Long(2)) == 0)
          {
            stmt.setString(++indice, (String) SolmrConstants
                .get("ESITO_CONTROLLO_BLOCCANTE"));
          }
          else
          {
            stmt.setString(++indice, (String) SolmrConstants
                .get("ESITO_CONTROLLO_WARNING"));
          }
        }
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getIstatComuneParticella()))
      {
        stmt
            .setString(++indice, particellaRicercaVO.getIstatComuneParticella());
      }
      else
      {
        if (particellaRicercaVO.getFlagEstero())
        {
          stmt.setString(++indice, SolmrConstants.FLAG_S);
        }
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSezione()))
      {
        stmt
            .setString(++indice, particellaRicercaVO.getSezione().toUpperCase());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getFoglio()))
      {
        stmt.setLong(++indice, particellaRicercaVO.getFoglio().longValue());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticella()))
      {
        stmt.setLong(++indice, particellaRicercaVO.getParticella().longValue());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSubalterno()))
      {
        stmt.setString(++indice, particellaRicercaVO.getSubalterno());
      }

      SolmrLogger
          .debug(this,
              "Executing query ricercaParticelleByParametriSenzaUsoSuolo: "
                  + query);

      ResultSet rs = stmt.executeQuery();

      elencoParticelle = new Vector<ParticellaVO>();
      while (rs.next())
      {
        ParticellaVO particellaVO = new ParticellaVO();
        ParticellaUtilizzoVO particellaUtilizzoVO = new ParticellaUtilizzoVO();
        particellaVO.setIdStoricoParticella(new Long(rs.getLong(1)));
        particellaVO.setIdConduzioneParticella(new Long(rs.getLong(2)));
        if (rs.getString(5) != null)
        {
          if (rs.getString(5).equalsIgnoreCase(SolmrConstants.FLAG_S))
          {
            particellaVO.setDescStatoEsteroParticella(rs.getString(4));
            particellaVO.setDescComuneParticella(null);
            particellaVO.setSiglaProvinciaParticella(null);
          }
          else
          {
            particellaVO.setSiglaProvinciaParticella(rs.getString(3));
            particellaVO.setDescComuneParticella(rs.getString(4));
          }
        }
        particellaVO.setSezione(rs.getString(6));
        particellaVO.setFoglio(new Long(rs.getLong(7)));
        if (rs.getString(8) != null && !rs.getString(8).equals(""))
        {
          particellaVO.setParticella(new Long(rs.getLong(8)));
        }
        particellaVO.setSubalterno(rs.getString(9));
        particellaVO.setSupCatastale(StringUtils.parseSuperficieField(rs
            .getString(10)));
        particellaVO.setSuperficieCondotta(StringUtils.parseSuperficieField(rs
            .getString(11)));
        particellaVO.setDescrizioneTitoloPossesso(rs.getString(12));
        if (Validator.isNotEmpty(rs.getString(13)))
        {
          particellaUtilizzoVO.setDescTipoUtilizzo(rs.getString(13));
        }
        else
        {
          particellaUtilizzoVO.setDescTipoUtilizzo("-");
        }
        if (Validator.isNotEmpty(rs.getString(14)))
        {
          particellaUtilizzoVO.setSuperficieUtilizzata(StringUtils
              .parseSuperficieField(rs.getString(14)));
        }
        // Dal momento che la ricerca è ristretta alle particelle senza uso del
        // suolo "primario"
        // setto automaticamente i valori relativi all'uso secondario poichè
        // sicuramente non
        // saranno valorizzati.
        particellaUtilizzoVO.setDescTipoUtilizzoSecondario(" - ");
        particellaUtilizzoVO.setSuperficieUtilizzataSecondaria(" - ");
        particellaVO.setParticellaUtilizzoVO(particellaUtilizzoVO);
        particellaVO.setEsitoControllo(rs.getString(15));
        particellaVO.setIdTitoloPossesso(checkLongNull(rs
            .getString("ID_TITOLO_POSSESSO")));
        String temp = rs.getString("RECORD_MODIFICATO");
        if (temp != null)
          if (temp.equalsIgnoreCase(SolmrConstants.FLAG_S))
            particellaVO.setRecordModificato(true);

        elencoParticelle.add(particellaVO);
      }

      stmt.close();
      rs.close();

      if (elencoParticelle == null || elencoParticelle.size() == 0)
      {
        throw new SolmrException((String) AnagErrors.get("ERR_NO_PARTICELLE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "ricercaParticelleByParametriSenzaUsoSuolo - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "ricercaParticelleByParametriSenzaUsoSuolo - Generic Exception: "
              + ex);
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
                "ricercaParticelleByParametriSenzaUsoSuolo - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "ricercaParticelleByParametriSenzaUsoSuolo - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoParticelle;
  }

  // Metodo per recuperare l'elenco delle fotografie relative alle particelle
  // attraverso i parametri relativi alla particella e l'id dichiarazione di
  // consistenza
  public Vector<ParticellaVO> ricercaParticelleStoricizzateByParametriAndUtilizzo(
      ParticellaVO particellaRicercaVO) throws SolmrException,
      DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaVO> elencoParticelle = null;

    SolmrLogger
        .debug(
            this,
            "\n[FascicoloDAO.ricercaParticelleStoricizzateByParametriAndUtilizzo() BEGIN]\n");

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT SP.ID_STORICO_PARTICELLA, "
          + "        CD.ID_CONDUZIONE_DICHIARATA, "
          + "        P.SIGLA_PROVINCIA, "
          + "        C.DESCOM, "
          + "        C.FLAG_ESTERO, "
          + "        SP.SEZIONE, "
          + "        SP.FOGLIO, "
          + "        SP.PARTICELLA, "
          + "        SP.SUBALTERNO, "
          + "        SP.SUP_CATASTALE, "
          + "        CD.SUPERFICIE_CONDOTTA, "
          + "        TTP.DESCRIZIONE, "
          + "        TU.DESCRIZIONE, "
          + "        UD.SUPERFICIE_UTILIZZATA, "
          + "        TU2.DESCRIZIONE, "
          + "        UD.SUP_UTILIZZATA_SECONDARIA, "
          + "        UD.NOTE, "
          + "        UD.ID_UTILIZZO_DICHIARATO, "
          + "        CD.ID_TITOLO_POSSESSO "
          + " FROM   PROVINCIA P, "
          + "        COMUNE C, "
          + "        DB_STORICO_PARTICELLA SP, "
          + "        DB_CONDUZIONE_DICHIARATA CD, "
          + "        DB_TIPO_UTILIZZO TU, "
          + "        DB_TIPO_UTILIZZO TU2, "
          + "        DB_TIPO_INDIRIZZO_UTILIZZO TUI, "
          + "        DB_UTILIZZO_DICHIARATO UD, "
          + "        DB_DICHIARAZIONE_CONSISTENZA DC, "
          + "        DB_TIPO_TITOLO_POSSESSO TTP "
          + " WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? "
          + " AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI "
          + " AND    SP.COMUNE = C.ISTAT_COMUNE "
          + " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
          + " AND    CD.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA(+) "
          + " AND    UD.ID_UTILIZZO = TU.ID_UTILIZZO(+) "
          + " AND    TUI.ID_INDIRIZZO_UTILIZZO =  TU.ID_INDIRIZZO_UTILIZZO "
          + " AND    SP.DATA_FINE_VALIDITA IS NULL "
          + " AND    TTP.ID_TITOLO_POSSESSO = CD.ID_TITOLO_POSSESSO "
          + " AND    TU2.ID_UTILIZZO(+) = UD.ID_UTILIZZO_SECONDARIO ";

      if (Validator.isNotEmpty(particellaRicercaVO.getIstatComuneParticella()))
      {
        query += " AND SP.COMUNE = ? ";
      }
      else
      {
        if (particellaRicercaVO.getFlagEstero())
        {
          query += " AND C.FLAG_ESTERO = ? ";
        }
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSezione()))
      {
        query += " AND SP.SEZIONE = ? ";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getFoglio()))
      {
        query += " AND SP.FOGLIO = ? ";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticella()))
      {
        query += " AND SP.PARTICELLA = ? ";
      }
      if (particellaRicercaVO.getFlagNoCatastale())
      {
        query += " AND SP.PARTICELLA IS NULL";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSubalterno()))
      {
        query += " AND SP.SUBALTERNO = ? ";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticellaUtilizzoVO()))
      {
        if (Validator.isNotEmpty(particellaRicercaVO.getParticellaUtilizzoVO()
            .getIdTipoIndirizzoUtilizzo()))
        {
          query += " AND TUI.ID_INDIRIZZO_UTILIZZO = ? ";
        }
        if (Validator.isNotEmpty(particellaRicercaVO.getParticellaUtilizzoVO()
            .getIdTipoUtilizzo()))
        {
          query += " AND TU.ID_UTILIZZO = ? ";
        }
      }
      query += " UNION SELECT SP.ID_STORICO_PARTICELLA, "
          + "              CD.ID_CONDUZIONE_DICHIARATA, "
          + "              P.SIGLA_PROVINCIA, "
          + "              C.DESCOM, "
          + "              C.FLAG_ESTERO, "
          + "              SP.SEZIONE, "
          + "              SP.FOGLIO, "
          + "              SP.PARTICELLA, "
          + "              SP.SUBALTERNO, "
          + "              SP.SUP_CATASTALE, "
          + "              CD.SUPERFICIE_CONDOTTA, "
          + "              TTP.DESCRIZIONE, "
          + "              '-', "
          + "              (CD.SUPERFICIE_CONDOTTA - SUM(NVL(UD.SUPERFICIE_UTILIZZATA,0))) SUPERFICIE_UTILIZZATA, '', "
          + "              0, "
          + "              '', "
          + "              0, "
          + "              CD.ID_TITOLO_POSSESSO "
          + " FROM         PROVINCIA P, "
          + "              COMUNE C, "
          + "              DB_STORICO_PARTICELLA SP, "
          + "              DB_CONDUZIONE_DICHIARATA CD, "
          + "              DB_UTILIZZO_DICHIARATO UD, "
          + "              DB_DICHIARAZIONE_CONSISTENZA DC, "
          + "              DB_TIPO_UTILIZZO TU, "
          + "              DB_TIPO_UTILIZZO TU2, "
          + "              DB_TIPO_INDIRIZZO_UTILIZZO TIU, "
          + "              DB_TIPO_TITOLO_POSSESSO TTP "
          + " WHERE        DC.ID_DICHIARAZIONE_CONSISTENZA = ? "
          + "              AND DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI "
          + "              AND SP.COMUNE = C.ISTAT_COMUNE "
          + "              AND C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
          + "              AND CD.ID_PARTICELLA = SP.ID_PARTICELLA "
          + "              AND CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA(+) "
          + "              AND TTP.ID_TITOLO_POSSESSO = CD.ID_TITOLO_POSSESSO "
          + "              AND TU2.ID_UTILIZZO(+) = UD.ID_UTILIZZO_SECONDARIO "
          + "              AND TU.ID_UTILIZZO(+) = UD.ID_UTILIZZO "
          + "              AND TIU.ID_INDIRIZZO_UTILIZZO = TU.ID_INDIRIZZO_UTILIZZO "
          + "              AND    SP.DATA_FINE_VALIDITA IS NULL ";

      if (Validator.isNotEmpty(particellaRicercaVO.getIstatComuneParticella()))
      {
        query += " AND SP.COMUNE = ? ";
      }
      else
      {
        if (particellaRicercaVO.getFlagEstero())
        {
          query += " AND C.FLAG_ESTERO = ? ";
        }
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSezione()))
      {
        query += " AND SP.SEZIONE = ? ";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getFoglio()))
      {
        query += " AND SP.FOGLIO = ? ";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticella()))
      {
        query += " AND SP.PARTICELLA = ? ";
      }
      if (particellaRicercaVO.getFlagNoCatastale())
      {
        query += " AND SP.PARTICELLA IS NULL";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSubalterno()))
      {
        query += " AND SP.SUBALTERNO = ? ";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticellaUtilizzoVO()))
      {
        if (Validator.isNotEmpty(particellaRicercaVO.getParticellaUtilizzoVO()
            .getIdTipoIndirizzoUtilizzo()))
        {
          query += " AND TIU.ID_INDIRIZZO_UTILIZZO = ? ";
        }
        if (Validator.isNotEmpty(particellaRicercaVO.getParticellaUtilizzoVO()
            .getIdTipoUtilizzo()))
        {
          query += " AND TU.ID_UTILIZZO = ? ";
        }
      }
      query += " GROUP BY SP.ID_STORICO_PARTICELLA, "
          + "          CD.ID_CONDUZIONE_DICHIARATA, "
          + "          P.SIGLA_PROVINCIA, "
          + "          C.DESCOM, "
          + "          C.FLAG_ESTERO, "
          + "          SP.SEZIONE, "
          + "          SP.FOGLIO, "
          + "          SP.PARTICELLA, "
          + "          SP.SUBALTERNO, "
          + "          SP.SUP_CATASTALE, "
          + "          CD.SUPERFICIE_CONDOTTA, "
          + "          TTP.DESCRIZIONE, "
          + "          CD.ID_TITOLO_POSSESSO "
          + "          HAVING SUM(NVL(UD.SUPERFICIE_UTILIZZATA,0)) < CD.SUPERFICIE_CONDOTTA "
          + "          ORDER BY 3, 4, 6, 7, 8, 9 ";

      stmt = conn.prepareStatement(query);

      int indice = 0;

      stmt.setLong(++indice, particellaRicercaVO
          .getIdDichiarazioneConsistenza().longValue());

      if (Validator.isNotEmpty(particellaRicercaVO.getIstatComuneParticella()))
      {
        stmt
            .setString(++indice, particellaRicercaVO.getIstatComuneParticella());
      }
      else
      {
        if (particellaRicercaVO.getFlagEstero())
        {
          stmt.setString(++indice, SolmrConstants.FLAG_S);
        }
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSezione()))
      {
        stmt
            .setString(++indice, particellaRicercaVO.getSezione().toUpperCase());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getFoglio()))
      {
        stmt.setLong(++indice, particellaRicercaVO.getFoglio().longValue());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticella()))
      {
        stmt.setLong(++indice, particellaRicercaVO.getParticella().longValue());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSubalterno()))
      {
        stmt.setString(++indice, particellaRicercaVO.getSubalterno());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticellaUtilizzoVO()
          .getIdTipoIndirizzoUtilizzo()))
      {
        stmt.setLong(++indice, particellaRicercaVO.getParticellaUtilizzoVO()
            .getIdTipoIndirizzoUtilizzo().longValue());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticellaUtilizzoVO()
          .getIdTipoUtilizzo()))
      {
        stmt.setLong(++indice, particellaRicercaVO.getParticellaUtilizzoVO()
            .getIdTipoUtilizzo().longValue());
      }

      stmt.setLong(++indice, particellaRicercaVO
          .getIdDichiarazioneConsistenza().longValue());

      if (Validator.isNotEmpty(particellaRicercaVO.getIstatComuneParticella()))
      {
        stmt
            .setString(++indice, particellaRicercaVO.getIstatComuneParticella());
      }
      else
      {
        if (particellaRicercaVO.getFlagEstero())
        {
          stmt.setString(++indice, SolmrConstants.FLAG_S);
        }
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSezione()))
      {
        stmt
            .setString(++indice, particellaRicercaVO.getSezione().toUpperCase());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getFoglio()))
      {
        stmt.setLong(++indice, particellaRicercaVO.getFoglio().longValue());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticella()))
      {
        stmt.setLong(++indice, particellaRicercaVO.getParticella().longValue());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSubalterno()))
      {
        stmt.setString(++indice, particellaRicercaVO.getSubalterno());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticellaUtilizzoVO()
          .getIdTipoIndirizzoUtilizzo()))
      {
        stmt.setLong(++indice, particellaRicercaVO.getParticellaUtilizzoVO()
            .getIdTipoIndirizzoUtilizzo().longValue());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticellaUtilizzoVO()
          .getIdTipoUtilizzo()))
      {
        stmt.setLong(++indice, particellaRicercaVO.getParticellaUtilizzoVO()
            .getIdTipoUtilizzo().longValue());
      }

      SolmrLogger.debug(this,
          "Executing query ricercaParticelleStoricizzateByParametriAndUtilizzo: "
              + query);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        elencoParticelle = new Vector<ParticellaVO>();
        while (rs.next())
        {
          ParticellaVO particellaVO = new ParticellaVO();
          ParticellaUtilizzoVO particellaUtilizzoVO = new ParticellaUtilizzoVO();
          particellaVO.setIdStoricoParticella(new Long(rs.getLong(1)));
          particellaVO.setIdConduzioneDichiarata(new Long(rs.getLong(2)));
          if (rs.getString(5) != null)
          {
            if (rs.getString(5).equalsIgnoreCase(SolmrConstants.FLAG_S))
            {
              particellaVO.setDescStatoEsteroParticella(rs.getString(4));
              particellaVO.setDescComuneParticella(null);
              particellaVO.setSiglaProvinciaParticella(null);
            }
            else
            {
              particellaVO.setSiglaProvinciaParticella(rs.getString(3));
              particellaVO.setDescComuneParticella(rs.getString(4));
            }
          }
          particellaVO.setSezione(rs.getString(6));
          particellaVO.setFoglio(new Long(rs.getLong(7)));
          if (rs.getString(8) != null && !rs.getString(8).equals(""))
          {
            particellaVO.setParticella(new Long(rs.getLong(8)));
          }
          particellaVO.setSubalterno(rs.getString(9));
          particellaVO.setSupCatastale(StringUtils.parseSuperficieField(rs
              .getString(10)));
          particellaVO.setSuperficieCondotta(StringUtils
              .parseSuperficieField(rs.getString(11)));
          particellaVO.setDescrizioneTitoloPossesso(rs.getString(12));
          if (Validator.isNotEmpty(rs.getString(13)))
          {
            particellaUtilizzoVO.setDescTipoUtilizzo(rs.getString(13));
          }
          else
          {
            particellaUtilizzoVO.setDescTipoUtilizzo("-");
          }
          if (Validator.isNotEmpty(rs.getString(14)))
          {
            particellaUtilizzoVO.setSuperficieUtilizzata(StringUtils
                .parseSuperficieField(rs.getString(14)));
          }
          // Setto il valore dell'eventuale utilizzo secondario
          if (Validator.isNotEmpty(rs.getString(15)))
          {
            particellaUtilizzoVO
                .setDescTipoUtilizzoSecondario(rs.getString(15));
          }
          else
          {
            particellaUtilizzoVO.setDescTipoUtilizzoSecondario(" - ");
          }
          // Setto il valore eventuale della superficie secondaria
          if (Validator.isNotEmpty(rs.getString(16)))
          {
            particellaUtilizzoVO.setSuperficieUtilizzataSecondaria(StringUtils
                .parseSuperficieField(rs.getString(16)));
          }
          else
          {
            particellaUtilizzoVO.setSuperficieUtilizzataSecondaria(" - ");
          }

          if (Validator.isNotEmpty(rs.getString(17)))
          {
            if (rs.getString(17).length() > 20)
            {
              particellaUtilizzoVO.setNote(rs.getString(17).substring(0, 20)
                  + "...");
            }
          }
          if (Validator.isNotEmpty(rs.getString(18)))
          {
            particellaUtilizzoVO.setIdUtilizzoDichiarato(new Long(rs
                .getLong(18)));
          }
          particellaVO.setParticellaUtilizzoVO(particellaUtilizzoVO);
          particellaVO.setIdTitoloPossesso(checkLongNull(rs
              .getString("ID_TITOLO_POSSESSO")));
          elencoParticelle.add(particellaVO);
        }
      }

      stmt.close();
      rs.close();

      if (elencoParticelle == null || elencoParticelle.size() == 0)
      {
        throw new SolmrException((String) AnagErrors.get("ERR_NO_PARTICELLE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "ricercaParticelleStoricizzateByParametriAndUtilizzo - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "ricercaParticelleStoricizzateByParametriAndUtilizzo - Generic Exception: "
              + ex);
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
                "ricercaParticelleStoricizzateByParametriAndUtilizzo - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "ricercaParticelleStoricizzateByParametriAndUtilizzo - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoParticelle;
  }

  // Metodo per effettuare la ricerca delle particelle storicizzate a partire
  // dai parametri relativi alla
  // particella e ad uno speficifico utilizzo
  public Vector<ParticellaVO> ricercaParticelleStoricizzateByParametriAndUtilizzoSpecificato(
      ParticellaVO particellaRicercaVO) throws DataAccessException,
      SolmrException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaVO> elencoParticelle = null;

    SolmrLogger
        .debug(
            this,
            "\n[FascicoloDAO.ricercaParticelleStoricizzateByParametriAndUtilizzoSpecificato() BEGIN]\n");

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT SP.ID_STORICO_PARTICELLA, "
          + "        CD.ID_CONDUZIONE_DICHIARATA, "
          + "        P.SIGLA_PROVINCIA, "
          + "        C.DESCOM, "
          + "        C.FLAG_ESTERO, "
          + "        SP.SEZIONE, "
          + "        SP.FOGLIO, "
          + "        SP.PARTICELLA, "
          + "        SP.SUBALTERNO, "
          + "        SP.SUP_CATASTALE, "
          + "        CD.SUPERFICIE_CONDOTTA, "
          + "        TTP.DESCRIZIONE, "
          + "        TU.DESCRIZIONE, "
          + "        UD.SUPERFICIE_UTILIZZATA, "
          + "        TU2.DESCRIZIONE, "
          + "        UD.SUP_UTILIZZATA_SECONDARIA, "
          + "        UD.NOTE, "
          + "        UD.ID_UTILIZZO_DICHIARATO, "
          + "        CD.ID_TITOLO_POSSESSO "
          + " FROM   PROVINCIA P, "
          + "        COMUNE C, "
          + "        DB_STORICO_PARTICELLA SP, "
          + "        DB_CONDUZIONE_DICHIARATA CD, "
          + "        DB_TIPO_UTILIZZO TU, "
          + "        DB_TIPO_UTILIZZO TU2, "
          + "        DB_TIPO_INDIRIZZO_UTILIZZO TUI, "
          + "        DB_UTILIZZO_DICHIARATO UD, "
          + "        DB_DICHIARAZIONE_CONSISTENZA DC, "
          + "        DB_TIPO_TITOLO_POSSESSO TTP "
          + " WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? "
          + " AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI "
          + " AND    DC.CODICE_FOTOGRAFIA_TERRENI = UD.CODICE_FOTOGRAFIA_TERRENI "
          + " AND    SP.COMUNE = C.ISTAT_COMUNE "
          + " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
          + " AND    CD.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND    SP.DATA_FINE_VALIDITA IS NULL "
          + " AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA "
          + " AND    UD.ID_UTILIZZO = TU.ID_UTILIZZO "
          + " AND    TUI.ID_INDIRIZZO_UTILIZZO = ? "
          + " AND    TU.ID_INDIRIZZO_UTILIZZO = TUI.ID_INDIRIZZO_UTILIZZO "
          + " AND    TTP.ID_TITOLO_POSSESSO = CD.ID_TITOLO_POSSESSO "
          + " AND    TU2.ID_UTILIZZO(+) = UD.ID_UTILIZZO_SECONDARIO ";

      if (Validator.isNotEmpty(particellaRicercaVO.getIstatComuneParticella()))
      {
        query += " AND SP.COMUNE = ? ";
      }
      else
      {
        if (particellaRicercaVO.getFlagEstero())
        {
          query += " AND C.FLAG_ESTERO = ? ";
        }
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSezione()))
      {
        query += " AND SP.SEZIONE = ? ";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getFoglio()))
      {
        query += " AND SP.FOGLIO = ? ";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticella()))
      {
        query += " AND SP.PARTICELLA = ? ";
      }
      if (particellaRicercaVO.getFlagNoCatastale())
      {
        query += " AND SP.PARTICELLA IS NULL";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSubalterno()))
      {
        query += " AND SP.SUBALTERNO = ? ";
      }
      if (particellaRicercaVO.getParticellaUtilizzoVO() != null)
      {
        if (particellaRicercaVO.getParticellaUtilizzoVO().getIdTipoUtilizzo() != null)
        {
          query += " AND    TU.ID_UTILIZZO = ? ";
        }
      }

      query += " ORDER BY P.SIGLA_PROVINCIA, " + "          C.DESCOM, "
          + "          SP.SEZIONE, " + "          SP.FOGLIO, "
          + "          SP.PARTICELLA, " + "          SP.SUBALTERNO, "
          + "          TU.DESCRIZIONE ";

      stmt = conn.prepareStatement(query);

      int indice = 0;

      stmt.setLong(++indice, particellaRicercaVO
          .getIdDichiarazioneConsistenza().longValue());
      if (particellaRicercaVO.getParticellaUtilizzoVO() != null)
      {
        if (particellaRicercaVO.getParticellaUtilizzoVO()
            .getIdTipoIndirizzoUtilizzo() != null)
        {
          stmt.setLong(++indice, particellaRicercaVO.getParticellaUtilizzoVO()
              .getIdTipoIndirizzoUtilizzo().longValue());
        }
      }
      if (particellaRicercaVO.getParticellaUtilizzoVO() != null)
      {
        if (particellaRicercaVO.getParticellaUtilizzoVO().getIdTipoUtilizzo() != null)
        {
          stmt.setLong(++indice, particellaRicercaVO.getParticellaUtilizzoVO()
              .getIdTipoUtilizzo().longValue());
        }
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getIstatComuneParticella()))
      {
        stmt
            .setString(++indice, particellaRicercaVO.getIstatComuneParticella());
      }
      else
      {
        if (particellaRicercaVO.getFlagEstero())
        {
          stmt.setString(++indice, SolmrConstants.FLAG_S);
        }
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSezione()))
      {
        stmt
            .setString(++indice, particellaRicercaVO.getSezione().toUpperCase());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getFoglio()))
      {
        stmt.setLong(++indice, particellaRicercaVO.getFoglio().longValue());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticella()))
      {
        stmt.setLong(++indice, particellaRicercaVO.getParticella().longValue());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSubalterno()))
      {
        stmt.setString(++indice, particellaRicercaVO.getSubalterno());
      }

      SolmrLogger
          .debug(
              this,
              "Executing query ricercaParticelleStoricizzateByParametriAndUtilizzoSpecificato: "
                  + query);

      ResultSet rs = stmt.executeQuery();

      elencoParticelle = new Vector<ParticellaVO>();
      while (rs.next())
      {
        ParticellaVO particellaVO = new ParticellaVO();
        ParticellaUtilizzoVO particellaUtilizzoVO = new ParticellaUtilizzoVO();
        particellaVO.setIdStoricoParticella(new Long(rs.getLong(1)));
        particellaVO.setIdConduzioneDichiarata(new Long(rs.getLong(2)));
        if (rs.getString(5) != null)
        {
          if (rs.getString(5).equalsIgnoreCase(SolmrConstants.FLAG_S))
          {
            particellaVO.setDescStatoEsteroParticella(rs.getString(4));
            particellaVO.setDescComuneParticella(null);
            particellaVO.setSiglaProvinciaParticella(null);
          }
          else
          {
            particellaVO.setSiglaProvinciaParticella(rs.getString(3));
            particellaVO.setDescComuneParticella(rs.getString(4));
          }
        }
        particellaVO.setSezione(rs.getString(6));
        particellaVO.setFoglio(new Long(rs.getLong(7)));
        if (rs.getString(8) != null && !rs.getString(8).equals(""))
        {
          particellaVO.setParticella(new Long(rs.getLong(8)));
        }
        particellaVO.setSubalterno(rs.getString(9));
        particellaVO.setSupCatastale(StringUtils.parseSuperficieField(rs
            .getString(10)));
        particellaVO.setSuperficieCondotta(StringUtils.parseSuperficieField(rs
            .getString(11)));
        particellaVO.setDescrizioneTitoloPossesso(rs.getString(12));
        particellaUtilizzoVO.setDescTipoUtilizzo(rs.getString(13));
        if (Validator.isNotEmpty(rs.getString(14)))
        {
          particellaUtilizzoVO.setSuperficieUtilizzata(StringUtils
              .parseSuperficieField(rs.getString(14)));
        }
        // Setto l'eventuale valore dell'utilizzo secondario
        if (Validator.isNotEmpty(rs.getString(15)))
        {
          particellaUtilizzoVO.setDescTipoUtilizzoSecondario(rs.getString(15));
        }
        else
        {
          particellaUtilizzoVO.setDescTipoUtilizzoSecondario(" - ");
        }
        // Setto l'evetuale valore della superficie secondaria utilizzata
        if (Validator.isNotEmpty(rs.getString(16)))
        {
          particellaUtilizzoVO.setSuperficieUtilizzataSecondaria(StringUtils
              .parseSuperficieField(rs.getString(16)));
        }
        else
        {
          particellaUtilizzoVO.setSuperficieUtilizzataSecondaria(" - ");
        }
        if (Validator.isNotEmpty(rs.getString(17)))
        {
          if (rs.getString(17).length() > 20)
          {
            particellaUtilizzoVO.setNote(rs.getString(17).substring(0, 20)
                + "...");
          }
        }
        if (Validator.isNotEmpty(rs.getString(18)))
        {
          particellaUtilizzoVO
              .setIdUtilizzoDichiarato(new Long(rs.getLong(18)));
        }
        particellaVO.setParticellaUtilizzoVO(particellaUtilizzoVO);
        particellaVO.setIdTitoloPossesso(checkLongNull(rs
            .getString("ID_TITOLO_POSSESSO")));
        elencoParticelle.add(particellaVO);
      }
      stmt.close();
      rs.close();

      if (elencoParticelle == null || elencoParticelle.size() == 0)
      {
        throw new SolmrException((String) AnagErrors.get("ERR_NO_PARTICELLE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger
          .fatal(
              this,
              "ricercaParticelleStoricizzateByParametriAndUtilizzoSpecificato - SQLException: "
                  + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(
              this,
              "ricercaParticelleStoricizzateByParametriAndUtilizzoSpecificato - Generic Exception: "
                  + ex);
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
                "ricercaParticelleStoricizzateByParametriAndUtilizzoSpecificato - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "ricercaParticelleStoricizzateByParametriAndUtilizzoSpecificato - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoParticelle;
  }

  // Metodo per recuperare l'elenco delle particelle storicizzate in relazione a
  // dei filtri di ricerca e all'assenza
  // di utilizzi o al fatto che la somma delle superfici utilizzate relative ad
  // una conduzione non
  // sia uguale alla superficie condotta
  public Vector<ParticellaVO> ricercaParticelleStoricizzateByParametriSenzaUsoSuolo(
      ParticellaVO particellaRicercaVO) throws SolmrException,
      DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaVO> elencoParticelle = null;

    SolmrLogger
        .debug(
            this,
            "\n[FascicoloDAO.ricercaParticelleStoricizzateByParametriSenzaUsoSuolo() BEGIN]\n");

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT SP.ID_STORICO_PARTICELLA, "
          + "        CD.ID_CONDUZIONE_DICHIARATA, "
          + "        P.SIGLA_PROVINCIA, "
          + "        C.DESCOM, "
          + "        C.FLAG_ESTERO, "
          + "        SP.SEZIONE, "
          + "        SP.FOGLIO, "
          + "        SP.PARTICELLA, "
          + "        SP.SUBALTERNO, "
          + "        SP.SUP_CATASTALE, "
          + "        CD.SUPERFICIE_CONDOTTA, "
          + "        TTP.DESCRIZIONE, "
          + "        '-', "
          + "        (CD.SUPERFICIE_CONDOTTA - SUM(NVL(UD.SUPERFICIE_UTILIZZATA, 0))) SUPERFICIE_UTILIZZATA, "
          + "        '', "
          + "        CD.ID_TITOLO_POSSESSO "
          + " FROM   PROVINCIA P, "
          + "        COMUNE C, "
          + "        DB_STORICO_PARTICELLA SP, "
          + "        DB_CONDUZIONE_DICHIARATA CD, "
          + "        DB_DICHIARAZIONE_CONSISTENZA DC, "
          + "        DB_UTILIZZO_DICHIARATO UD, "
          + "        DB_TIPO_TITOLO_POSSESSO TTP "
          + " WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? "
          + " AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI "
          + " AND    SP.COMUNE = C.ISTAT_COMUNE "
          + " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
          + " AND    CD.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA(+) "
          + " AND    SP.DATA_FINE_VALIDITA IS NULL "
          + " AND    TTP.ID_TITOLO_POSSESSO = CD.ID_TITOLO_POSSESSO ";

      if (Validator.isNotEmpty(particellaRicercaVO.getIstatComuneParticella()))
      {
        query += " AND SP.COMUNE = ? ";
      }
      else
      {
        if (particellaRicercaVO.getFlagEstero())
        {
          query += " AND C.FLAG_ESTERO = ? ";
        }
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSezione()))
      {
        query += " AND SP.SEZIONE = ? ";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getFoglio()))
      {
        query += " AND SP.FOGLIO = ? ";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticella()))
      {
        query += " AND SP.PARTICELLA = ? ";
      }
      if (particellaRicercaVO.getFlagNoCatastale())
      {
        query += " AND SP.PARTICELLA IS NULL";
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSubalterno()))
      {
        query += " AND SP.SUBALTERNO = ? ";
      }

      query += " GROUP BY SP.ID_STORICO_PARTICELLA, "
          + "          CD.ID_CONDUZIONE_DICHIARATA, "
          + "          P.SIGLA_PROVINCIA, "
          + "          C.DESCOM, "
          + "          C.FLAG_ESTERO, "
          + "          SP.SEZIONE, "
          + "          SP.FOGLIO, "
          + "          SP.PARTICELLA, "
          + "          SP.SUBALTERNO, "
          + "          SP.SUP_CATASTALE, "
          + "          CD.SUPERFICIE_CONDOTTA, "
          + "          TTP.DESCRIZIONE, "
          + "          CD.ID_TITOLO_POSSESSO "
          + " HAVING   SUM(NVL(UD.SUPERFICIE_UTILIZZATA,0)) < CD.SUPERFICIE_CONDOTTA "
          + " ORDER BY 3, 4, 6, 7, 8, 9 ";

      stmt = conn.prepareStatement(query);

      int indice = 0;

      stmt.setLong(++indice, particellaRicercaVO
          .getIdDichiarazioneConsistenza().longValue());

      if (Validator.isNotEmpty(particellaRicercaVO.getIstatComuneParticella()))
      {
        stmt
            .setString(++indice, particellaRicercaVO.getIstatComuneParticella());
      }
      else
      {
        if (particellaRicercaVO.getFlagEstero())
        {
          stmt.setString(++indice, SolmrConstants.FLAG_S);
        }
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSezione()))
      {
        stmt
            .setString(++indice, particellaRicercaVO.getSezione().toUpperCase());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getFoglio()))
      {
        stmt.setLong(++indice, particellaRicercaVO.getFoglio().longValue());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getParticella()))
      {
        stmt.setLong(++indice, particellaRicercaVO.getParticella().longValue());
      }
      if (Validator.isNotEmpty(particellaRicercaVO.getSubalterno()))
      {
        stmt.setString(++indice, particellaRicercaVO.getSubalterno());
      }

      SolmrLogger.debug(this,
          "Executing query ricercaParticelleStoricizzateByParametriSenzaUsoSuolo: "
              + query);

      ResultSet rs = stmt.executeQuery();

      elencoParticelle = new Vector<ParticellaVO>();
      while (rs.next())
      {
        ParticellaVO particellaVO = new ParticellaVO();
        ParticellaUtilizzoVO particellaUtilizzoVO = new ParticellaUtilizzoVO();
        particellaVO.setIdStoricoParticella(new Long(rs.getLong(1)));
        particellaVO.setIdConduzioneDichiarata(new Long(rs.getLong(2)));
        if (rs.getString(5) != null)
        {
          if (rs.getString(5).equalsIgnoreCase(SolmrConstants.FLAG_S))
          {
            particellaVO.setDescStatoEsteroParticella(rs.getString(4));
            particellaVO.setDescComuneParticella(null);
            particellaVO.setSiglaProvinciaParticella(null);
          }
          else
          {
            particellaVO.setSiglaProvinciaParticella(rs.getString(3));
            particellaVO.setDescComuneParticella(rs.getString(4));
          }
        }
        particellaVO.setSezione(rs.getString(6));
        particellaVO.setFoglio(new Long(rs.getLong(7)));
        if (rs.getString(8) != null && !rs.getString(8).equals(""))
        {
          particellaVO.setParticella(new Long(rs.getLong(8)));
        }
        particellaVO.setSubalterno(rs.getString(9));
        particellaVO.setSupCatastale(StringUtils.parseSuperficieField(rs
            .getString(10)));
        particellaVO.setSuperficieCondotta(StringUtils.parseSuperficieField(rs
            .getString(11)));
        particellaVO.setDescrizioneTitoloPossesso(rs.getString(12));
        if (Validator.isNotEmpty(rs.getString(13)))
        {
          particellaUtilizzoVO.setDescTipoUtilizzo(rs.getString(13));
        }
        else
        {
          particellaUtilizzoVO.setDescTipoUtilizzo("-");
        }
        if (Validator.isNotEmpty(rs.getString(14)))
        {
          particellaUtilizzoVO.setSuperficieUtilizzata(StringUtils
              .parseSuperficieField(rs.getString(14)));
        }
        if (Validator.isNotEmpty(rs.getString(15)))
        {
          if (rs.getString(15).length() > 20)
          {
            particellaUtilizzoVO.setNote(rs.getString(15).substring(0, 20)
                + "...");
          }
        }
        // Dal momento che la ricerca è ristretta alle particelle senza uso del
        // suolo "primario"
        // setto automaticamente i valori relativi all'uso secondario poichè
        // sicuramente non
        // saranno valorizzati.
        particellaUtilizzoVO.setDescTipoUtilizzoSecondario(" - ");
        particellaUtilizzoVO.setSuperficieUtilizzataSecondaria(" - ");
        particellaVO.setParticellaUtilizzoVO(particellaUtilizzoVO);
        particellaVO.setIdTitoloPossesso(checkLongNull(rs
            .getString("ID_TITOLO_POSSESSO")));
        elencoParticelle.add(particellaVO);
      }

      stmt.close();
      rs.close();

      if (elencoParticelle == null || elencoParticelle.size() == 0)
      {
        throw new SolmrException((String) AnagErrors.get("ERR_NO_PARTICELLE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "ricercaParticelleStoricizzateByParametriSenzaUsoSuolo - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "ricercaParticelleStoricizzateByParametriSenzaUsoSuolo - Generic Exception: "
              + ex);
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
                "ricercaParticelleStoricizzateByParametriSenzaUsoSuolo - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "ricercaParticelleStoricizzateByParametriSenzaUsoSuolo - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoParticelle;
  }

  // Metodo per recuperare la data oltre la quale non sarà più possibile
  // modificare gli usi del suolo
  public Date getDataMaxForUpdateUsoDelSuolo() throws SolmrException,
      DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    java.util.Date dataMassima = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT VALORE " + " FROM   DB_PARAMETRO "
          + " WHERE  UPPER(ID_PARAMETRO) = ? ";

      stmt = conn.prepareStatement(query);

      stmt.setString(1, ((String) SolmrConstants.get("ID_PARAMETRO_DAUS"))
          .toUpperCase());

      SolmrLogger.debug(this,
          "Executing query getDataMaxForUpdateUsoDelSuolo: " + query);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        String dataMax = rs.getString(1);
        String giorno = dataMax.substring(0, 2);
        String mese = dataMax.substring(2, 4);
        dataMassima = DateUtils.parseDate(giorno, mese, DateUtils
            .getCurrentYear().toString());
      }

      stmt.close();
      rs.close();

      if (dataMassima == null)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_DATA_MAX_FOR_UPDATE_UTILIZZO_NO_IMPOSTATA"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getDataMaxForUpdateUsoDelSuolo - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getDataMaxForUpdateUsoDelSuolo - Generic Exception: " + ex);
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
                "getDataMaxForUpdateUsoDelSuolo - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getDataMaxForUpdateUsoDelSuolo - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return dataMassima;
  }

  // Metodo per recuperare i dati della particella e dell'utilizzo ad essa
  // relativa a partire
  // dall' id utilizzi particella
  public ParticellaVO getParticellaVOByIdUtilizzoParticella(
      Long idUtilizzoParticella) throws SolmrException, DataAccessException
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
          + "        SP.FLAG_IRRIGABILE, "
          + "        SP.ID_CASO_PARTICOLARE, "
          + "        SP.DATA_AGGIORNAMENTO, "
          + "        SP.ID_UTENTE_AGGIORNAMENTO, "
          + "        SP.MOTIVO_MODIFICA, "
          + "        CP.ID_TITOLO_POSSESSO, "
          + "        C.DESCOM, "
          + "        CP.SUPERFICIE_CONDOTTA, "
          + "        C1.DESCOM, "
          + "        P.SIGLA_PROVINCIA, "
          + "        UP.ID_UTILIZZO_PARTICELLA, "
          + "        UP.ID_UTILIZZO, "
          + "        TU.ID_INDIRIZZO_UTILIZZO, "
          + "        UP.NOTE, "
          + "        UP.SUPERFICIE_UTILIZZATA, "
          + "        TTP.DESCRIZIONE, "
          + "        CP.ID_CONDUZIONE_PARTICELLA, "
          + "        TU.DESCRIZIONE, "
          + "        UP.ID_UTILIZZO_SECONDARIO, "
          + "        TU2.DESCRIZIONE, "
          + "        UP.SUP_UTILIZZATA_SECONDARIA "
          + " FROM   DB_STORICO_PARTICELLA SP, "
          + "        DB_PARTICELLA P, "
          + "        COMUNE C, "
          + "        DB_CONDUZIONE_PARTICELLA CP, "
          + "        DB_UTE U, "
          + "        COMUNE C1, "
          + "        PROVINCIA P, "
          + "        DB_TIPO_TITOLO_POSSESSO TTP, "
          + "        DB_UTILIZZO_PARTICELLA UP, "
          + "        DB_TIPO_INDIRIZZO_UTILIZZO TIU, "
          + "        DB_TIPO_UTILIZZO TU, "
          + "        DB_TIPO_UTILIZZO TU2 "
          + " WHERE  UP.ID_UTILIZZO_PARTICELLA = ? "
          + " AND    UP.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA "
          + " AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND    P.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND    SP.DATA_FINE_VALIDITA IS NULL "
          + " AND    U.ID_UTE = CP.ID_UTE "
          + " AND    U.COMUNE = C.ISTAT_COMUNE "
          + " AND    SP.COMUNE = C1.ISTAT_COMUNE "
          + " AND    C1.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
          + " AND    CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO "
          + " AND    UP.ID_UTILIZZO = TU.ID_UTILIZZO "
          + " AND    TU.ID_INDIRIZZO_UTILIZZO = TIU.ID_INDIRIZZO_UTILIZZO "
          + " AND    UP.ID_UTILIZZO_SECONDARIO = TU2.ID_UTILIZZO(+) ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idUtilizzoParticella.longValue());

      SolmrLogger.debug(this,
          "Executing query getParticellaVOByIdUtilizzoParticella: " + query);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        particellaVO = new ParticellaVO();
        ParticellaUtilizzoVO particellaUtilizzoVO = new ParticellaUtilizzoVO();
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
        if ((!Validator.isNotEmpty(rs.getString(11)) || rs.getString(11)
            .equalsIgnoreCase(SolmrConstants.FLAG_N)))
        {
          particellaVO.setFlagIrrigabile(false);
        }
        else
        {
          particellaVO.setFlagIrrigabile(true);
        }
        if (Validator.isNotEmpty(rs.getString(12)))
        {
          particellaVO.setIdCasoParticolare(new Long(rs.getLong(12)));
        }
        particellaVO.setDataAggiornamento(rs.getDate(13));
        particellaVO.setIdUtenteAggiornamento(new Long(rs.getLong(14)));
        particellaVO.setMotivoModifica(rs.getString(15));
        particellaVO.setIdTitoloPossesso(new Long(rs.getLong(16)));
        particellaVO.setDescrizioneUnitaProduttiva(rs.getString(17));
        particellaVO.setSuperficieCondotta(StringUtils.parseSuperficieField(rs
            .getString(18)));
        particellaVO.setDescComuneParticella(rs.getString(19));
        particellaVO.setSiglaProvinciaParticella(rs.getString(20));
        particellaUtilizzoVO.setIdUtilizzoParticella(new Long(rs.getLong(21)));
        if (Validator.isNotEmpty(rs.getString(22)))
        {
          particellaUtilizzoVO.setIdTipoUtilizzo(new Long(rs.getLong(22)));
        }
        if (Validator.isNotEmpty(rs.getString(23)))
        {
          particellaUtilizzoVO.setIdTipoIndirizzoUtilizzo(new Long(rs
              .getLong(23)));
        }
        particellaUtilizzoVO.setNote(rs.getString(24));
        particellaUtilizzoVO.setSuperficieUtilizzata(StringUtils
            .parseSuperficieField(rs.getString(25)));
        particellaVO.setSuperficieUtilizzata(StringUtils
            .parseSuperficieField(rs.getString(25)));
        particellaVO.setDescrizioneTitoloPossesso(rs.getString(26));
        particellaVO.setIdConduzioneParticella(new Long(rs.getLong(27)));
        particellaUtilizzoVO.setDescTipoUtilizzo(rs.getString(28));
        if (Validator.isNotEmpty(rs.getString(29)))
        {
          particellaUtilizzoVO
              .setIdUtilizzoSecondario(new Long(rs.getLong(29)));
          particellaUtilizzoVO.setDescTipoUtilizzoSecondario(rs.getString(30));
          particellaUtilizzoVO.setSuperficieUtilizzataSecondaria(StringUtils
              .parseSuperficieField(rs.getString(31)));
        }
        else
        {
          particellaUtilizzoVO.setDescTipoUtilizzoSecondario(" - ");
        }

        particellaVO.setParticellaUtilizzoVO(particellaUtilizzoVO);
      }

      stmt.close();
      rs.close();

      if (particellaVO == null)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_NO_UTILIZZI_ATTIVI"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getParticellaVOByIdUtilizzoParticella - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getParticellaVOByIdUtilizzoParticella - Generic Exception: " + ex);
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
                "getParticellaVOByIdUtilizzoParticella - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getParticellaVOByIdUtilizzoParticella - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return particellaVO;
  }

  // Metodo per recuperare i dati della particella a partire dall'
  // id_conduzione_particella
  public ParticellaVO getParticellaVOByIdConduzioneParticella(
      Long idConduzioneParticella) throws SolmrException, DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    ParticellaVO particellaVO = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = "" +
      		"SELECT SP.ID_PARTICELLA, " +
          "       SP.ID_STORICO_PARTICELLA, " +
          "       SP.SEZIONE, " +
          "       SP.COMUNE, " +
          "       SP.FOGLIO, " +
          "       SP.DATA_INIZIO_VALIDITA, " +
          "       SP.DATA_FINE_VALIDITA, " +
          "       SP.PARTICELLA, " +
          "       SP.SUP_CATASTALE, " +
          "       SP.SUBALTERNO, " +
          "       SP.ID_UTENTE_AGGIORNAMENTO, " +
          "       C.DESCOM AS DESC_UTE, " +
          "       CP.SUPERFICIE_CONDOTTA, " +
          "       CP.PERCENTUALE_POSSESSO, " +
          "       C1.DESCOM AS DESC_COM_PART, " +
          "       P.SIGLA_PROVINCIA, " +
          "       CP.ID_CONDUZIONE_PARTICELLA, " +
          "       TTP.DESCRIZIONE AS DESC_POSSESSO, " +
          "       CP.DATA_FINE_CONDUZIONE, " +
          "       CP.ID_UTE, " +
          "       CP.ID_TITOLO_POSSESSO, " +
          "       CP.NOTE, " +
          "       CP.DATA_INIZIO_CONDUZIONE " +
          "FROM   DB_STORICO_PARTICELLA SP, " +
          "       DB_PARTICELLA P, " +
          "       COMUNE C, " +
          "       DB_CONDUZIONE_PARTICELLA CP, " +
          "       DB_UTE U, " + 
          "       COMUNE C1, " +
          "       PROVINCIA P, " +
          "       DB_TIPO_TITOLO_POSSESSO TTP " +
          "WHERE  CP.ID_CONDUZIONE_PARTICELLA = ? " +
          "AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
          "AND    P.ID_PARTICELLA = SP.ID_PARTICELLA " +
          "AND    SP.DATA_FINE_VALIDITA IS NULL " +
          "AND    U.ID_UTE = CP.ID_UTE " +
          "AND    U.COMUNE = C.ISTAT_COMUNE " +
          "AND    SP.COMUNE = C1.ISTAT_COMUNE " +
          "AND    C1.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
          "AND    CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idConduzioneParticella.longValue());

      SolmrLogger.debug(this,
          "Executing query getParticellaVOByIdConduzioneParticella: " + query);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        particellaVO = new ParticellaVO();
        particellaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
        particellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
        particellaVO.setSezione(rs.getString("SEZIONE"));
        particellaVO.setIstatComuneParticella(rs.getString("COMUNE"));
        particellaVO.setFoglio(new Long(rs.getLong("FOGLIO")));
        particellaVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        particellaVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        particellaVO.setParticella(checkLongNull(rs.getString("PARTICELLA")));
        particellaVO.setSupCatastale(StringUtils.parseSuperficieField(rs
            .getString("SUP_CATASTALE")));
        particellaVO.setSubalterno(rs.getString("SUBALTERNO"));
        particellaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        particellaVO.setDescrizioneUnitaProduttiva(rs.getString("DESC_UTE"));
        particellaVO.setSuperficieCondotta(StringUtils.parseSuperficieField(rs
            .getString("SUPERFICIE_CONDOTTA")));
        particellaVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
        particellaVO.setDescComuneParticella(rs.getString("DESC_COM_PART"));
        particellaVO.setSiglaProvinciaParticella(rs.getString("SIGLA_PROVINCIA"));
        particellaVO.setIdConduzioneParticella(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));
        particellaVO.setDescrizioneTitoloPossesso(rs.getString("DESC_POSSESSO"));
        particellaVO.setDataFineConduzione(rs.getTimestamp("DATA_FINE_CONDUZIONE"));        
        particellaVO.setIdUnitaProduttiva(new Long(rs.getLong("ID_UTE")));
        particellaVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
        particellaVO.setNote(rs.getString("NOTE"));
        particellaVO.setDataInizioConduzione(rs.getTimestamp("DATA_INIZIO_CONDUZIONE"));
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

  // Metodo per recuperare il valore di tutte le superfici utilizzate da
  // un'azienda agricola esclusa
  // quella selezionata
  public double getTotSupUtilizzateByIdCondParticellaNotIncludeIdUtilizzo(
      Long idConduzioneParticella, Long idUtilizzoParticella, String anno)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    double totSuperficiUtilizzate = 0;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT SUM(UP.SUPERFICIE_UTILIZZATA) "
          + " FROM   DB_UTILIZZO_PARTICELLA UP, "
          + "        DB_CONDUZIONE_PARTICELLA CP "
          + " WHERE  CP.ID_CONDUZIONE_PARTICELLA = ? "
          + " AND    UP.ID_UTILIZZO_PARTICELLA != ? "
          + " AND    UP.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA "
          + " AND    UP.ANNO = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing getTotSupUtilizzateByIdCondParticellaNotIncludeIdUtilizzo: "
              + query);

      stmt.setLong(1, idConduzioneParticella.longValue());
      stmt.setLong(2, idUtilizzoParticella.longValue());
      stmt.setString(3, anno);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        totSuperficiUtilizzate = rs.getDouble(1);
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getTotSupUtilizzateByIdCondParticellaNotIncludeIdUtilizzo - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(
              this,
              "getTotSupUtilizzateByIdCondParticellaNotIncludeIdUtilizzo - Generic Exception: "
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
                "getTotSupUtilizzateByIdCondParticellaNotIncludeIdUtilizzo - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getTotSupUtilizzateByIdCondParticellaNotIncludeIdUtilizzo - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return totSuperficiUtilizzate;
  }

  // Metodo per recuperare il valore di tutte le superfici condotte attive di
  // un'azienda agricola esclusa
  // quella della particella su cui si sta lavorando
  public String getTotaleSupCondotteByIdParticellaNotIncludeIdConduzioneParticella(
      Long idConduzioneParticella, Long idParticella)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    String totSuperficiCondotte = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT SUM(CP.SUPERFICIE_CONDOTTA) "
          + " FROM   DB_CONDUZIONE_PARTICELLA CP "
          + " WHERE  CP.ID_CONDUZIONE_PARTICELLA != ? "
          + " AND    CP.ID_PARTICELLA = ? "
          + " AND    CP.DATA_FINE_CONDUZIONE IS NULL";

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(
              this,
              "Executing getTotaleSupCondotteByIdParticellaNotIncludeIdConduzioneParticella: "
                  + query);

      stmt.setLong(1, idConduzioneParticella.longValue());
      stmt.setLong(2, idParticella.longValue());

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        totSuperficiCondotte = StringUtils.parseSuperficieField((rs
            .getString(1)));
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger
          .fatal(
              this,
              "getTotaleSupCondotteByIdParticellaNotIncludeIdConduzioneParticella - SQLException: "
                  + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(
              this,
              "getTotaleSupCondotteByIdParticellaNotIncludeIdConduzioneParticella - Generic Exception: "
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
                "getTotaleSupCondotteByIdParticellaNotIncludeIdConduzioneParticella - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getTotaleSupCondotteByIdParticellaNotIncludeIdConduzioneParticella - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return totSuperficiCondotte;
  }

  // Metodo per impostare una conduzione su inità vitate
  public void impostaConduzioneUnitaVitate(ParticellaVO particellaVO,
      Long newIdConduzioneParticella) throws SolmrException,
      DataAccessException
  {

    Connection conn = null;
    CallableStatement cs = null;
    String command = "{call PACK_GESTIONE_UV.IMPOSTA_CONDUZIONE_UV(?,?,?,?)}";

    try
    {

      conn = getDatasource().getConnection();

      cs = conn.prepareCall(command);
      cs.setLong(1, particellaVO.getIdParticella().longValue());
      cs.setLong(2, newIdConduzioneParticella.longValue());
      cs.registerOutParameter(3, Types.VARCHAR);
      cs.registerOutParameter(4, Types.VARCHAR);

      cs.executeUpdate();
      String msgErr = cs.getString(3);
      String errorCode = cs.getString(4);

      if (!(errorCode == null || "".equals(errorCode)))
      {
        throw new SolmrException((String) AnagErrors.get("ERR_PLSQL")
            + errorCode + " - " + msgErr);
      }

      cs.close();
      conn.close();

      SolmrLogger.debug(this, "Executed impostaConduzioneUnitaVitate");
    }
    catch (SolmrException se)
    {
      SolmrLogger.fatal(this,
          "SolmrException in impostaConduzioneUnitaVitate: " + se.getMessage());
      throw new SolmrException(se.getMessage());
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in impostaConduzioneUnitaVitate: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in impostaConduzioneUnitaVitate: "
              + ex.getMessage());
      throw new SolmrException(ex.getMessage());
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
                "SQLException while closing Statement and Connection in impostaConduzioneUnitaVitate: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in impostaConduzioneUnitaVitate: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
  }

  // Metodo per storicizzare su inità vitate
  public void storicizzaUnitaVitate(ParticellaVO particellaVO)
      throws SolmrException, DataAccessException
  {

    Connection conn = null;
    CallableStatement cs = null;
    String command = "{call PACK_GESTIONE_UV.CESSA_CONDUZIONE_UV(?,?,?,?)}";

    try
    {

      conn = getDatasource().getConnection();

      cs = conn.prepareCall(command);
      cs.setLong(1, particellaVO.getIdConduzioneParticella().longValue());
      cs.setDate(2, new java.sql.Date(particellaVO.getDataFineConduzione()
          .getTime()));
      cs.registerOutParameter(3, Types.VARCHAR);
      cs.registerOutParameter(4, Types.VARCHAR);

      cs.executeUpdate();
      String msgErr = cs.getString(3);
      String errorCode = cs.getString(4);

      if (!(errorCode == null || "".equals(errorCode)))
      {
        throw new SolmrException((String) AnagErrors.get("ERR_PLSQL")
            + errorCode + " - " + msgErr);
      }

      cs.close();
      conn.close();

      SolmrLogger.debug(this, "Executed storicizzaUnitaVitate");
    }
    catch (SolmrException se)
    {
      SolmrLogger.fatal(this, "SolmrException in storicizzaUnitaVitate: "
          + se.getMessage());
      throw new SolmrException(se.getMessage());
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in storicizzaUnitaVitate: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in storicizzaUnitaVitate: "
          + ex.getMessage());
      throw new SolmrException(ex.getMessage());
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
                "SQLException while closing Statement and Connection in storicizzaUnitaVitate: "
                    + exc.getMessage());
        throw new SolmrException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in storicizzaUnitaVitate: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
  }

  // Metodo per verificare la presenza di utilizzi a vigneto tra le particelle
  // selezionate
  public int countUtilizziVignetoByElencoIdUtilizzoParticella(
      Vector<String> elencoIdUtilizziParticella) throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    int numParticelle = 0;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT ";

      for (int i = 0; i < elencoIdUtilizziParticella.size(); i++)
      {
        if (i >= 1)
        {
          query += " + ";
        }

        query += " (SELECT DECODE(TU.TIPO, 'V', 1, 0) "
            + " FROM    DB_UTILIZZO_PARTICELLA UP, "
            + "         DB_TIPO_UTILIZZO TU "
            + " WHERE   UP.ID_UTILIZZO_PARTICELLA = "
            + elencoIdUtilizziParticella.elementAt(i)
            + " AND     UP.ID_UTILIZZO = TU.ID_UTILIZZO) ";
      }

      query += " FROM DUAL ";

      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this,
          "Executing countUtilizziVignetoByElencoIdUtilizzoParticella: "
              + query);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        numParticelle = rs.getInt(1);
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "countUtilizziVignetoByElencoIdUtilizzoParticella - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "countUtilizziVignetoByElencoIdUtilizzoParticella - Generic Exception: "
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
                "countUtilizziVignetoByElencoIdUtilizzoParticella - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "countUtilizziVignetoByElencoIdUtilizzoParticella - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return numParticelle;
  }

  // Metodo per effettuare la cancellazione degli utilizzi dalla tabella
  // DB_UTILIZZO_PARTICELLA
  public void deleteUtilizzoParticella(Long idUtilizzoParticella)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " DELETE FROM DB_UTILIZZO_PARTICELLA "
          + " WHERE  ID_UTILIZZO_PARTICELLA = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing deleteUtilizzoParticella: " + query);

      stmt.setLong(1, idUtilizzoParticella.longValue());

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this,
          "Executed deleteUtilizzoParticella in fascicoloDAO");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "deleteUtilizzoParticella in fascicoloDAO - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(this,
              "deleteUtilizzoParticella in fascicoloDAO - Generic Exception: "
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
                "deleteUtilizzoParticella in fascicoloDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "deleteUtilizzoParticella in fascicoloDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per controllare le particelle selezionate sono già state inserite
  // all'interno di una
  // dichiarazione di consistenza
  public void checkParticellaLegataDichiarazioneConsistenza(
      Vector<Long> elencoParticelle) throws DataAccessException, SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT DISTINCT SP.COMUNE, "
          + "        C.DESCOM, "
          + "        SP.FOGLIO, "
          + "        SP.PARTICELLA "
          + " FROM   DB_STORICO_PARTICELLA SP, "
          + "        DB_CONDUZIONE_PARTICELLA CP, "
          + "        DB_CONDUZIONE_DICHIARATA CD, "
          + "        COMUNE C "
          + " WHERE  CP.ID_CONDUZIONE_PARTICELLA = CD.ID_CONDUZIONE_PARTICELLA "
          + " AND    CD.ID_PARTICELLA = CP.ID_PARTICELLA "
          + " AND    SP.ID_PARTICELLA = CP.ID_PARTICELLA "
          + " AND    SP.COMUNE = C.ISTAT_COMUNE "
          + " AND    SP.DATA_FINE_VALIDITA IS NULL " + " AND    ( ";

      for (int i = 0; i < elencoParticelle.size(); i++)
      {

        if (i >= 1)
        {
          query += " OR ";
        }

        query += " CD.ID_CONDUZIONE_PARTICELLA =  "
            + elencoParticelle.elementAt(i);
      }

      query += " ) ";

      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this,
          "Executing checkParticellaLegataDichiarazioneConsistenza: " + query);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        String messaggio = "Non è possibile eliminare la particella comune "
            + rs.getString(2) + " foglio " + rs.getLong(3);
        if (Validator.isNotEmpty(rs.getString(4)))
        {
          messaggio += " part. " + rs.getLong(4);
        }
        messaggio += " perchè presente nelle dichiarazioni di consistenza";
        throw new SolmrException(messaggio);
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "checkParticellaLegataDichiarazioneConsistenza - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (SolmrException se)
    {
      SolmrLogger.fatal(this,
          "checkParticellaLegataDichiarazioneConsistenza - SolmrException: "
              + se.getMessage());
      throw new SolmrException(se.getMessage());
    }

    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "checkParticellaLegataDichiarazioneConsistenza - Generic Exception: "
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
                "checkParticellaLegataDichiarazioneConsistenza - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "checkParticellaLegataDichiarazioneConsistenza - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per effettuare la cancellazione degli utilizzi dalla tabella
  // DB_UTILIZZO_PARTICELLA
  // partendo dall'id conduzione particella
  public void deleteUtilizzoParticellaByIdConduzioneParticella(
      Long idConduzioneParticella) throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " DELETE FROM DB_UTILIZZO_PARTICELLA "
          + " WHERE  ID_CONDUZIONE_PARTICELLA = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing deleteUtilizzoParticellaByIdConduzioneParticella: "
              + query);

      stmt.setLong(1, idConduzioneParticella.longValue());

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger
          .debug(this,
              "Executed deleteUtilizzoParticellaByIdConduzioneParticella in fascicoloDAO");
    }
    catch (SQLException exc)
    {
      SolmrLogger
          .fatal(
              this,
              "deleteUtilizzoParticellaByIdConduzioneParticella in fascicoloDAO - SQLException: "
                  + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(
              this,
              "deleteUtilizzoParticellaByIdConduzioneParticella in fascicoloDAO - Generic Exception: "
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
                "deleteUtilizzoParticellaByIdConduzioneParticella in fascicoloDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "deleteUtilizzoParticellaByIdConduzioneParticella in fascicoloDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per effettuare la cancellazione dei record dalla tabella
  // DB_ESITO_CONTROLLO_PARTICELLA
  public void deleteEsitoControlloParticellaByIdConduzioneParticella(
      Long idConduzioneParticella) throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " DELETE FROM DB_ESITO_CONTROLLO_PARTICELLA "
          + " WHERE  ID_CONDUZIONE_PARTICELLA = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing deleteEsitoControlloParticellaByIdConduzioneParticella: "
              + query);

      stmt.setLong(1, idConduzioneParticella.longValue());

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger
          .debug(
              this,
              "Executed deleteEsitoControlloParticellaByIdConduzioneParticella in fascicoloDAO");
    }
    catch (SQLException exc)
    {
      SolmrLogger
          .fatal(
              this,
              "deleteEsitoControlloParticellaByIdConduzioneParticella in fascicoloDAO - SQLException: "
                  + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(
              this,
              "deleteEsitoControlloParticellaByIdConduzioneParticella in fascicoloDAO - Generic Exception: "
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
                "deleteEsitoControlloParticellaByIdConduzioneParticella in fascicoloDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "deleteEsitoControlloParticellaByIdConduzioneParticella in fascicoloDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per effettuare la cancellazione delle conduzioni dalla tabella
  // DB_CONDUZIONE_PARTICELLA
  public void deleteConduzioneParticella(Long idConduzioneParticella)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " DELETE FROM DB_CONDUZIONE_PARTICELLA "
          + " WHERE  ID_CONDUZIONE_PARTICELLA = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing deleteConduzioneParticella: " + query);

      stmt.setLong(1, idConduzioneParticella.longValue());

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this,
          "Executed deleteConduzioneParticella in fascicoloDAO");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "deleteConduzioneParticella in fascicoloDAO - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "deleteConduzioneParticella in fascicoloDAO - Generic Exception: "
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
                "deleteConduzioneParticella in fascicoloDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "deleteConduzioneParticella in fascicoloDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }


  // Metodo per effettuare la modifica di un record su DB_FABBRICATO
  public void storicizzaFabbricato(Long idFabbricato, long idUtenteAggiornamento)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " UPDATE DB_FABBRICATO "
          + " SET    DATA_FINE_VALIDITA = SYSDATE, "
          + "        DATA_AGGIORNAMENTO = SYSDATE, "
          + "        ID_UTENTE_AGGIORNAMENTO = ? "
          + " WHERE  ID_FABBRICATO = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing storicizzaFabbricato: " + query);

      stmt.setLong(1, idUtenteAggiornamento);
      stmt.setLong(2, idFabbricato.longValue());

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed storicizzaFabbricato in fascicoloDAO");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "storicizzaFabbricato in fascicoloDAO - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "storicizzaFabbricato in fascicoloDAO - Generic Exception: " + ex);
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
                "storicizzaFabbricato in fascicoloDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "storicizzaFabbricato in fascicoloDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per effettuare la modifica di un record su DB_FABBRICATO
  public void storicizzaFabbricatoParticella(Long idFabbricato)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " UPDATE DB_FABBRICATO_PARTICELLA "
          + " SET    DATA_FINE_VALIDITA = SYSDATE "
          + " WHERE  ID_FABBRICATO = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing storicizzaFabbricatoParticella: "
          + query);

      stmt.setLong(1, idFabbricato.longValue());

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this,
          "Executed storicizzaFabbricatoParticella in fascicoloDAO");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "storicizzaFabbricatoParticella in fascicoloDAO - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "storicizzaFabbricatoParticella in fascicoloDAO - Generic Exception: "
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
                "storicizzaFabbricatoParticella in fascicoloDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "storicizzaFabbricatoParticella in fascicoloDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per effettuare la cessazione dell'unità produttiva selezionata
  public void cessazioneUTE(UteVO uteVO, long idUtenteAggiornamento)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = " UPDATE DB_UTE "
          + "    SET DATA_FINE_ATTIVITA = SYSDATE, "
          + "        CAUSALE_CESSAZIONE = ?, "
          + "        ID_UTENTE_AGGIORNAMENTO = ?, "
          + "        DATA_AGGIORNAMENTO = SYSDATE " + " WHERE  ID_UTE = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing cessazioneUTE: " + query);

      stmt.setString(1, uteVO.getCausaleCessazione());
      stmt.setLong(2, idUtenteAggiornamento);
      stmt.setLong(3, uteVO.getIdUte().longValue());

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed cessazioneUte");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "cessazioneUTE - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "cessazioneUTE - Generic Exception: " + ex);
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
            "cessazioneUTE - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "cessazioneUTE - Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per recuperare i dati della particella a partire dall'
  // id_conduzione_particella e la
  // superficie libera cioè senza uso del suolo specificato
  public ParticellaVO getParticellaVOByIdConduzioneParticellaSenzaUsoSuolo(
      Long idConduzioneParticella, String anno) throws SolmrException,
      DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    ParticellaVO particellaVO = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT   SP.ID_PARTICELLA, "
          + "          SP.ID_STORICO_PARTICELLA, "
          + "          SP.SEZIONE, "
          + "          SP.COMUNE, "
          + "          SP.FOGLIO, "
          + "          SP.DATA_INIZIO_VALIDITA, "
          + "          SP.DATA_FINE_VALIDITA, "
          + "          SP.PARTICELLA, "
          + "          SP.SUP_CATASTALE, "
          + "          SP.SUBALTERNO, "
          + "          SP.ID_UTENTE_AGGIORNAMENTO, "
          + "          C.DESCOM, "
          + "          CP.SUPERFICIE_CONDOTTA, "
          + "          C1.DESCOM, "
          + "          P.SIGLA_PROVINCIA, "
          + "          CP.ID_CONDUZIONE_PARTICELLA, "
          + "          TTP.DESCRIZIONE, "
          + "          CP.DATA_FINE_CONDUZIONE, "
          + "          CP.ID_UTE, "
          + "          CP.ID_TITOLO_POSSESSO, "
          + "          CP.NOTE, "
          + "          CP.DATA_INIZIO_CONDUZIONE, "
          + "          '-', "
          + "          (CP.SUPERFICIE_CONDOTTA - SUM(NVL(UP.SUPERFICIE_UTILIZZATA, 0))) SUPERFICIE_UTILIZZATA "
          + " FROM     DB_STORICO_PARTICELLA SP, "
          + "          DB_PARTICELLA P, "
          + "          COMUNE C, "
          + "          DB_CONDUZIONE_PARTICELLA CP, "
          + "          DB_UTE U, "
          + "          COMUNE C1, "
          + "          PROVINCIA P, "
          + "          DB_TIPO_TITOLO_POSSESSO TTP, "
          + "          (SELECT ID_CONDUZIONE_PARTICELLA, SUPERFICIE_UTILIZZATA "
          + "          FROM   DB_UTILIZZO_PARTICELLA "
          + "          WHERE  ANNO = ?) UP "
          + " WHERE    CP.ID_CONDUZIONE_PARTICELLA = ? "
          + " AND      CP.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND      P.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND      SP.DATA_FINE_VALIDITA IS NULL "
          + " AND      U.ID_UTE = CP.ID_UTE "
          + " AND      U.COMUNE = C.ISTAT_COMUNE "
          + " AND      SP.COMUNE = C1.ISTAT_COMUNE "
          + " AND      C1.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
          + " AND      CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO "
          + " AND      UP.ID_CONDUZIONE_PARTICELLA(+) = CP.ID_CONDUZIONE_PARTICELLA "
          + " GROUP BY SP.ID_PARTICELLA, "
          + "          SP.ID_STORICO_PARTICELLA, "
          + "          SP.SEZIONE, "
          + "          SP.COMUNE, "
          + "          SP.FOGLIO, "
          + "          SP.DATA_INIZIO_VALIDITA, "
          + "          SP.DATA_FINE_VALIDITA, "
          + "          SP.PARTICELLA, "
          + "          SP.SUP_CATASTALE, "
          + "          SP.SUBALTERNO, "
          + "          SP.ID_UTENTE_AGGIORNAMENTO, "
          + "          C.DESCOM, "
          + "          CP.SUPERFICIE_CONDOTTA, "
          + "          C1.DESCOM, "
          + "          P.SIGLA_PROVINCIA, "
          + "          CP.ID_CONDUZIONE_PARTICELLA, "
          + "          TTP.DESCRIZIONE, "
          + "          CP.DATA_FINE_CONDUZIONE, "
          + "          CP.ID_UTE, "
          + "          CP.ID_TITOLO_POSSESSO, "
          + "          CP.NOTE, "
          + "          CP.DATA_INIZIO_CONDUZIONE "
          + " HAVING   SUM(NVL(UP.SUPERFICIE_UTILIZZATA,0)) < CP.SUPERFICIE_CONDOTTA ";

      stmt = conn.prepareStatement(query);

      stmt.setString(1, anno);
      stmt.setLong(2, idConduzioneParticella.longValue());

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
        ParticellaUtilizzoVO particellaUtilizzoVO = new ParticellaUtilizzoVO();
        particellaUtilizzoVO.setDescTipoUtilizzo(rs.getString(23));
        particellaUtilizzoVO.setSuperficieUtilizzata(StringUtils
            .parseSuperficieField(rs.getString(24)));
        particellaVO.setSuperficieUtilizzata(StringUtils
            .parseSuperficieField(rs.getString(24)));
        particellaUtilizzoVO.setIdUtilizzoParticella(null);
        particellaVO.setParticellaUtilizzoVO(particellaUtilizzoVO);
      }

      stmt.close();
      rs.close();

      if (particellaVO == null)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_PARTICELLA_INESISTENTE"));
      }
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

  // Metodo per recuperare i dati territoriali della particella a partire
  // dall'id conduzione particella
  public ParticellaVO getDettaglioParticellaDatiTerritorialiByIdConduzioneParticella(
      Long idConduzioneParticella) throws SolmrException, DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    ParticellaVO particellaVO = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT DISTINCT C.DESCOM, "
          + "                 P.SIGLA_PROVINCIA, "
          + "                 C1.DESCOM, "
          + "                 SP.SEZIONE, "
          + "                 SP.FOGLIO, "
          + "                 SP.PARTICELLA, "
          + "                 SP.SUBALTERNO, "
          + "                 SP.SUP_CATASTALE, "
          + "                 TZA.DESCRIZIONE, "
          + "                 TAA.DESCRIZIONE, "
          + "                 TAB.DESCRIZIONE, "
          + "                 TAC.DESCRIZIONE, "
          + "                 TAD.DESCRIZIONE, "
          + "                 TAE.DESCRIZIONE, "
          + "                 TCP.DESCRIZIONE, "
          + "                 SP.FLAG_CAPTAZIONE_POZZI, "
          + "                 SP.FLAG_IRRIGABILE, "
          + "                 P.DATA_CREAZIONE, "
          + "                 P.DATA_CESSAZIONE, "
          + "                 SP.DATA_AGGIORNAMENTO, "
          + "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
          "          || ' ' " + 
          "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
          "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
          "         WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
          "                   AS DENOMINAZIONE, " +
          "       (SELECT PVU.DENOMINAZIONE " +
          "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
          "        WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
          "                   AS DESCRIZIONE_ENTE_APPARTENENZA, "
          + "                 CP.SUPERFICIE_CONDOTTA, "
          + "                 CP.ID_CONDUZIONE_PARTICELLA, "
          + "                 S.DESCRIZIONE, "
          + "                 SP.ID_STORICO_PARTICELLA, "
          + "                 SP.ID_CAUSALE_MOD_PARTICELLA, "
          + "                 SP.ID_AREA_A, "
          + "                 SP.ID_AREA_B, "
          + "                 SP.ID_AREA_C, "
          + "                 SP.ID_AREA_D, "
          + "                 SP.ID_CASO_PARTICOLARE, "
          + "                 SP.ID_ZONA_ALTIMETRICA, "
          + "                 SP.COMUNE, "
          + "                 SP.DATA_INIZIO_VALIDITA, "
          + "                 SP.DATA_FINE_VALIDITA, "
          + "                 SP.ID_PARTICELLA, "
          + "                 SP.ID_FONTE, "
          + "                 TF.DESCRIZIONE, "
          + "                 TCMP.DESCRIZIONE, "
          + "                 SP.ID_DOCUMENTO, "
          + "                 TD.DESCRIZIONE, "
          + "                 SP.MOTIVO_MODIFICA "
          + " FROM            DB_UTE U, "
          + "                 COMUNE C, "
          + "                 DB_CONDUZIONE_PARTICELLA CP, "
          + "                 PROVINCIA P, "
          + "                 DB_STORICO_PARTICELLA SP, "
          + "                 COMUNE C1, "
          + "                 DB_TIPO_ZONA_ALTIMETRICA TZA, "
          + "                 DB_TIPO_AREA_A TAA, "
          + "                 DB_TIPO_AREA_B TAB, "
          + "                 DB_TIPO_AREA_C TAC, "
          + "                 DB_TIPO_AREA_D TAD, "
          + "                 DB_TIPO_AREA_E TAE, "
          + "                 DB_FOGLIO F, "
          + "                 DB_TIPO_CASO_PARTICOLARE TCP, "
          + "                 DB_PARTICELLA P, "
          //+ "                 PAPUA_V_UTENTE_LOGIN PVU, "
          + "                 DB_SEZIONE S, "
          + "                 DB_TIPO_FONTE TF, "
          + "                 DB_TIPO_CAUSALE_MOD_PARTICELLA TCMP, "
          + "                 DB_TIPO_DOCUMENTO TD "
          + " WHERE           CP.ID_CONDUZIONE_PARTICELLA = ? "
          + " AND             CP.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND             P.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND             SP.DATA_FINE_VALIDITA IS NULL "
          + " AND             CP.ID_UTE = U.ID_UTE "
          + " AND             U.COMUNE = C.ISTAT_COMUNE "
          + " AND             SP.COMUNE = C1.ISTAT_COMUNE "
          + " AND             C1.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
          + " AND             SP.ID_ZONA_ALTIMETRICA = TZA.ID_ZONA_ALTIMETRICA(+) "
          + " AND             SP.ID_AREA_A = TAA.ID_AREA_A(+) "
          + " AND             SP.ID_AREA_B = TAB.ID_AREA_B(+) "
          + " AND             SP.ID_AREA_C = TAC.ID_AREA_C(+) "
          + " AND             SP.ID_AREA_D = TAD.ID_AREA_D(+) "
          + " AND             F.FOGLIO(+) = SP.FOGLIO "
          + " AND             nvl(SP.SEZIONE,'-')=nvl(F.SEZIONE(+),'-') "
          + " AND             F.COMUNE(+) = SP.COMUNE "
          + " AND             TAE.ID_AREA_E(+) = F.ID_AREA_E "
          + " AND             SP.ID_CASO_PARTICOLARE = TCP.ID_CASO_PARTICOLARE(+) "
          //+ " AND             SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN "
          + " AND             SP.SEZIONE = S.SEZIONE(+) "
          + " AND             SP.COMUNE = S.ISTAT_COMUNE(+) "
          + " AND             TF.ID_FONTE(+) = SP.ID_FONTE "
          + " AND             TCMP.ID_CAUSALE_MOD_PARTICELLA(+) = SP.ID_CAUSALE_MOD_PARTICELLA "
          + " AND             TD.ID_DOCUMENTO(+) = SP.ID_DOCUMENTO";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idConduzioneParticella.longValue());

      SolmrLogger.debug(this,
          "Executing getDettaglioParticellaDatiTerritorialiByIdConduzioneParticella: "
              + query);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        particellaVO = new ParticellaVO();
        particellaVO.setDescrizioneUnitaProduttiva(rs.getString(1));
        particellaVO.setSiglaProvinciaParticella(rs.getString(2));
        particellaVO.setDescComuneParticella(rs.getString(3));
        particellaVO.setSezione(rs.getString(4));
        particellaVO.setFoglio(new Long(rs.getLong(5)));
        if (Validator.isNotEmpty(rs.getString(6)))
        {
          particellaVO.setParticella(new Long(rs.getLong(6)));
          particellaVO.setStrParticella(rs.getString(6));
        }
        else
        {
          particellaVO.setParticellaProvvisoria(true);
        }
        particellaVO.setSubalterno(rs.getString(7));
        particellaVO.setSupCatastale(StringUtils.parseSuperficieField(rs
            .getString(8)));
        particellaVO.setDescrizioneZonaAltimetrica(rs.getString(9));
        particellaVO.setDescrizioneAreaA(rs.getString(10));
        particellaVO.setDescrizioneAreaB(rs.getString(11));
        particellaVO.setDescrizioneAreaC(rs.getString(12));
        particellaVO.setDescrizioneAreaD(rs.getString(13));
        particellaVO.setDescrizioneAreaE(rs.getString(14));
        particellaVO.setDescrizioneCasoParticolare(rs.getString(15));
        if (Validator.isNotEmpty(rs.getString(16)))
        {
          if (rs.getString(16).equalsIgnoreCase(SolmrConstants.FLAG_S))
          {
            particellaVO.setFlagCaptazionePozzi(true);
          }
          else
          {
            particellaVO.setFlagCaptazionePozzi(false);
          }
        }
        else
        {
          particellaVO.setFlagCaptazionePozzi(false);
        }
        if (Validator.isNotEmpty(rs.getString(17)))
        {
          if (rs.getString(17).equalsIgnoreCase(SolmrConstants.FLAG_S))
          {
            particellaVO.setFlagIrrigabile(true);
          }
          else
          {
            particellaVO.setFlagIrrigabile(false);
          }
        }
        else
        {
          particellaVO.setFlagIrrigabile(false);
        }
        particellaVO.setDataCreazione(rs.getDate(18));
        if (Validator.isNotEmpty(rs.getString(19)))
        {
          particellaVO.setDataCessazione(rs.getDate(19));
        }
        particellaVO.setDataAggiornamento(rs.getDate(20));
        particellaVO.setDenominazioneUtenteAggiornamento(rs.getString(21)
            + " - " + rs.getString(22));
        particellaVO.setSuperficieCondotta(StringUtils.parseSuperficieField(rs
            .getString(23)));
        particellaVO.setIdConduzioneParticella(new Long(rs.getLong(24)));
        particellaVO.setDescrizioneSezione(rs.getString(25));
        particellaVO.setIdStoricoParticella(new Long(rs.getString(26)));
        CodeDescription causaleModParticella = null;
        if (Validator.isNotEmpty(rs.getString(27)))
        {
          particellaVO.setIdCausaleModParticella(new Long(rs.getString(27)));
          particellaVO.setTipiCausaleModParticella(rs.getString(27));
          causaleModParticella = new CodeDescription(Integer.decode(rs
              .getString(27)), rs.getString(40));
        }
        particellaVO.setCausaleModParticella(causaleModParticella);
        particellaVO.setTipiAreaA(rs.getString(28)); // Settaggio per le combo
                                                      // cotruite con HtmplUtil
        particellaVO.setTipiAreaB(rs.getString(29)); // Settaggio per le combo
                                                      // cotruite con HtmplUtil
        particellaVO.setTipiAreaC(rs.getString(30)); // Settaggio per le combo
                                                      // cotruite con HtmplUtil
        particellaVO.setTipiAreaD(rs.getString(31)); // Settaggio per le combo
                                                      // cotruite con HtmplUtil
        particellaVO.setTipiCasoParticolare(rs.getString(32)); // Settaggio per
                                                                // le combo
                                                                // cotruite con
                                                                // HtmplUtil
        particellaVO.setTipiZonaAltimetrica(rs.getString(33)); // Settaggio per
                                                                // le combo
                                                                // cotruite con
                                                                // HtmplUtil
        particellaVO.setIstatComuneParticella(rs.getString(34)); // Valore
                                                                  // COMUNE
                                                                  // sulla
                                                                  // tabella
                                                                  // DB_STORICO_PARTICELLA
        particellaVO.setDataInizioValidita(rs.getDate(35)); // Valore
                                                            // DATA_INIZIO_VALIDITA
                                                            // sulla tabella
                                                            // DB_STORICO_PARTICELLA
        if (Validator.isNotEmpty(rs.getString(36)))
        {
          particellaVO.setDataFineValidita(rs.getDate(36)); // Valore
                                                            // DATA_FINE_VALIDITA
                                                            // sulla tabella
                                                            // DB_STORICO_PARTICELLA
        }
        particellaVO.setIdParticella(new Long(rs.getLong(37))); // Valore
                                                                // ID_PARTICELLA
                                                                // sulla tabella
                                                                // DB_STORICO_PARTICELLA
        CodeDescription fonteDato = null;
        if (Validator.isNotEmpty(rs.getString(38)))
        {
          fonteDato = new CodeDescription(Integer.decode(rs.getString(38)), rs
              .getString(39));
        }
        particellaVO.setFonteDato(fonteDato);
        CodeDescription tipoDocumento = null;
        if (Validator.isNotEmpty(rs.getString(41)))
        {
          tipoDocumento = new CodeDescription(Integer.decode(rs.getString(41)),
              rs.getString(42));
        }
        particellaVO.setTipoDocumento(tipoDocumento);
        particellaVO.setMotivoModifica(rs.getString(43));
      }

      stmt.close();
      rs.close();

      if (particellaVO == null)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_PARTICELLA_INESISTENTE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger
          .fatal(
              this,
              "getDettaglioParticellaDatiTerritorialiByIdConduzioneParticella - SQLException: "
                  + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(
              this,
              "getDettaglioParticellaDatiTerritorialiByIdConduzioneParticella - Generic Exception: "
                  + ex);
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
                "getDettaglioParticellaDatiTerritorialiByIdConduzioneParticella - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getDettaglioParticellaDatiTerritorialiByIdConduzioneParticella - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return particellaVO;
  }

  // Metodo per ribaltare gli utilizzi da una conduzione particella ad un'altra
  public void allegaUtilizziToNewConduzioneParticella(
      Long newIdConduzioneParticella, Long oldIdConduzioneParticella)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " UPDATE DB_UTILIZZO_PARTICELLA "
          + " SET    ID_CONDUZIONE_PARTICELLA = ? "
          + " WHERE  ID_CONDUZIONE_PARTICELLA = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing allegaUtilizziToNewConduzioneParticella: " + query);

      stmt.setLong(1, newIdConduzioneParticella.longValue());
      stmt.setLong(2, oldIdConduzioneParticella.longValue());

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this,
          "Executed allegaUtilizziToNewConduzioneParticella in fascicoloDAO");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "allegaUtilizziToNewConduzioneParticella in fascicoloDAO - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "allegaUtilizziToNewConduzioneParticella in fascicoloDAO - Generic Exception: "
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
                "allegaUtilizziToNewConduzioneParticella in fascicoloDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "allegaUtilizziToNewConduzioneParticella in fascicoloDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per effettuare la ricerca del terreno in relazione ai parametri di
  // ricerca
  public Vector<ParticellaVO> ricercaTerreniByParametri(
      ParticellaVO particellaRicercaTerrenoVO) throws SolmrException,
      DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaVO> elencoTerreni = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT SP.ID_STORICO_PARTICELLA, "
          + "        P.SIGLA_PROVINCIA, " + "        C.DESCOM, "
          + "        SP.SEZIONE, " + "        SP.FOGLIO, "
          + "        SP.PARTICELLA, " + "        SP.SUBALTERNO, "
          + "        SP.SUP_CATASTALE, "
          + "        PARTICELLA.DATA_CREAZIONE, "
          + "        PARTICELLA.DATA_CESSAZIONE "
          + " FROM   DB_PARTICELLA PARTICELLA, "
          + "        DB_STORICO_PARTICELLA SP, " + "        COMUNE C, "
          + "        PROVINCIA P " + " WHERE  SP.COMUNE = ? "
          + " AND    SP.COMUNE = C.ISTAT_COMUNE "
          + " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
          + " AND    SP.FOGLIO = ? "
          + " AND    SP.ID_PARTICELLA = PARTICELLA.ID_PARTICELLA "
          + " AND    SP.DATA_FINE_VALIDITA IS NULL ";

      // Se la sezione è valorizzata, ricerco anche per sezione
      if (Validator.isNotEmpty(particellaRicercaTerrenoVO.getSezione()))
      {
        query += " AND SP.SEZIONE = ? ";
      }

      // Se l'utente ha selezionato il flag "particella provvisoria" allora
      // ricerco per particella
      // e subalterno uguali a null
      if (particellaRicercaTerrenoVO.getParticellaProvvisoria())
      {
        query += " AND SP.PARTICELLA IS NULL " + " AND SP.SUBALTERNO IS NULL ";
      }
      // Altrimenti
      else
      {
        // Se l'utente ha valorizzato la particella
        if (Validator.isNotEmpty(particellaRicercaTerrenoVO.getParticella()))
        {
          query += " AND SP.PARTICELLA = ? ";
        }
        // Se l'utente ha valorizzato il subalterno
        if (Validator.isNotEmpty(particellaRicercaTerrenoVO.getSubalterno()))
        {
          query += " AND UPPER(SP.SUBALTERNO) = ? ";
        }
      }

      // Se l'utente ha selezionato il flag "particella attiva"
      if (particellaRicercaTerrenoVO.isParticellaAttiva())
      {
        query += " AND PARTICELLA.DATA_CESSAZIONE IS NULL ";
      }

      query += " ORDER BY P.SIGLA_PROVINCIA, C.DESCOM, SP.SEZIONE, SP.FOGLIO, SP.PARTICELLA, "
          + "       SP.SUBALTERNO ";

      stmt = conn.prepareStatement(query);

      int indice = 0;

      // Setto i parametri della query
      stmt.setString(++indice, particellaRicercaTerrenoVO
          .getIstatComuneParticella());
      stmt
          .setLong(++indice, particellaRicercaTerrenoVO.getFoglio().longValue());
      if (Validator.isNotEmpty(particellaRicercaTerrenoVO.getSezione()))
      {
        stmt.setString(++indice, particellaRicercaTerrenoVO.getSezione()
            .toUpperCase());
      }
      if (Validator.isNotEmpty(particellaRicercaTerrenoVO.getParticella()))
      {
        stmt.setLong(++indice, particellaRicercaTerrenoVO.getParticella()
            .longValue());
      }
      if (Validator.isNotEmpty(particellaRicercaTerrenoVO.getSubalterno()))
      {
        stmt.setString(++indice, particellaRicercaTerrenoVO.getSubalterno()
            .toUpperCase());
      }

      SolmrLogger.debug(this, "Executing ricercaTerreniByParametri: " + query);

      ResultSet rs = stmt.executeQuery();

      elencoTerreni = new Vector<ParticellaVO>();

      // Recupero i records estratti dalla query
      while (rs.next())
      {
        ParticellaVO particellaTerrenoVO = new ParticellaVO();
        particellaTerrenoVO.setIdStoricoParticella(new Long(rs.getLong(1)));
        if (!rs.getString(2).equalsIgnoreCase(
            (String) SolmrConstants.get("SIGLA_PROVINCIA_ESTERO")))
        {
          particellaTerrenoVO.setSiglaProvinciaParticella(rs.getString(2));
          particellaTerrenoVO.setDescComuneParticella(rs.getString(3));
        }
        else
        {
          particellaTerrenoVO.setDescStatoEsteroParticella(rs.getString(3));
        }
        particellaTerrenoVO.setSezione(rs.getString(4));
        particellaTerrenoVO.setFoglio(new Long(rs.getLong(5)));
        if (Validator.isNotEmpty(rs.getString(6)))
        {
          particellaTerrenoVO.setParticella(new Long(rs.getLong(6)));
        }
        particellaTerrenoVO.setSubalterno(rs.getString(7));
        particellaTerrenoVO.setSupCatastale(StringUtils.parseSuperficieField(rs
            .getString(8)));
        particellaTerrenoVO.setDataCreazione(rs.getDate(9));
        if (Validator.isNotEmpty(rs.getString(10)))
        {
          particellaTerrenoVO.setDataCessazione(rs.getDate(10));
        }
        elencoTerreni.add(particellaTerrenoVO);
      }

      stmt.close();
      rs.close();

      if (elencoTerreni == null || elencoTerreni.size() == 0)
      {
        throw new SolmrException((String) AnagErrors.get("ERR_NO_PARTICELLE"));
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "ricercaTerreniByParametri - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (SolmrException se)
    {
      SolmrLogger.fatal(this, "ricercaTerreniByParametri - SolmrException: "
          + se);
      throw new SolmrException(se.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "ricercaTerreniByParametri - Generic Exception: "
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
                "ricercaTerreniByParametri - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "ricercaTerreniByParametri - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoTerreni;
  }

  // Metodo verificare se esiste un contenzioso sulla particella selezionata,
  // cioè se sono presenti
  // delle conduzioni aperte a fronte di particelle selezionate su aziende
  // attive
  public boolean isParticellaContenziosoOnAzienda(Long idStoricoParticella)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    boolean isContenzioso = false;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT ID_ANAGRAFICA_AZIENDA "
          + " FROM   DB_ANAGRAFICA_AZIENDA AA, " + "        DB_UTE U, "
          + "        DB_PARTICELLA P, " + "        DB_STORICO_PARTICELLA SP, "
          + "        DB_CONDUZIONE_PARTICELLA CP "
          + " WHERE  SP.ID_STORICO_PARTICELLA = ? "
          + " AND    SP.ID_PARTICELLA = P.ID_PARTICELLA "
          + " AND    CP.DATA_FINE_CONDUZIONE IS NULL "
          + " AND    P.DATA_CESSAZIONE IS NOT NULL "
          + " AND    CP.ID_PARTICELLA = P.ID_PARTICELLA "
          + " AND    CP.ID_UTE = U.ID_UTE "
          + " AND    U.ID_AZIENDA = AA.ID_AZIENDA "
          + " AND    AA.DATA_FINE_VALIDITA IS NULL "
          + " AND    AA.DATA_CESSAZIONE IS NULL ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idStoricoParticella.longValue());

      SolmrLogger.debug(this, "Executing isParticellaContenziosoOnAzienda: "
          + query);

      ResultSet rs = stmt.executeQuery();

      // Recupero i records estratti dalla query
      if (rs.next())
      {
        isContenzioso = true;
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "isParticellaContenziosoOnAzienda - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "isParticellaContenziosoOnAzienda - Generic Exception: " + ex);
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
                "isParticellaContenziosoOnAzienda - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "isParticellaContenziosoOnAzienda - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return isContenzioso;
  }

  // Metodo per recuperare la somma delle superfici condotte relativa ad una
  // particella
  public String getTotSupCondotteByIdStoricoParticella(Long idStoricoParticella)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    String totSupCondotte = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT SUM(CP.SUPERFICIE_CONDOTTA) "
          + " FROM   DB_PARTICELLA P, " + "        DB_STORICO_PARTICELLA SP, "
          + "        DB_CONDUZIONE_PARTICELLA CP "
          + " WHERE  SP.ID_STORICO_PARTICELLA = ? "
          + " AND    SP.ID_PARTICELLA = P.ID_PARTICELLA "
          + " AND    CP.ID_PARTICELLA = P.ID_PARTICELLA "
          + " AND    CP.DATA_FINE_CONDUZIONE IS NULL ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idStoricoParticella.longValue());

      SolmrLogger.debug(this,
          "Executing getTotSupCondotteByIdStoricoParticella: " + query);

      ResultSet rs = stmt.executeQuery();

      // Recupero i records estratti dalla query
      if (rs.next())
      {
        totSupCondotte = StringUtils.parseSuperficieField(rs.getString(1));
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getTotSupCondotteByIdStoricoParticella - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getTotSupCondotteByIdStoricoParticella - Generic Exception: " + ex);
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
                "getTotSupCondotteByIdStoricoParticella - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getTotSupCondotteByIdStoricoParticella - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return totSupCondotte;
  }

  // Metodo per recuperare l'elenco delle azienda e delle conduzioni a partire
  // dall 'id storico
  // particella
  public Vector<ParticellaAziendaVO> getElencoAziendeAndConduzioniByIdStoricoParticella(
      Long idStoricoParticella, boolean attive) throws DataAccessException,
      SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaAziendaVO> elencoAziendeConduzioni = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT AA.ID_ANAGRAFICA_AZIENDA, "
          + "        CP.DATA_INIZIO_CONDUZIONE, "
          + "        CP.DATA_FINE_CONDUZIONE, " + "        TTP.DESCRIZIONE, "
          + "        CP.SUPERFICIE_CONDOTTA, " + "        AA.DENOMINAZIONE, "
          + "        AA.CUAA, " + "        C.DESCOM, "
          + "        P.SIGLA_PROVINCIA, "
          + "        CP.ID_CONDUZIONE_PARTICELLA "
          + " FROM   DB_ANAGRAFICA_AZIENDA AA, "
          + "        DB_CONDUZIONE_PARTICELLA CP, " + "        DB_UTE U, "
          + "        DB_STORICO_PARTICELLA SP, "
          + "        DB_TIPO_TITOLO_POSSESSO TTP, " + "        COMUNE C, "
          + "        PROVINCIA P " + " WHERE  SP.ID_STORICO_PARTICELLA = ? "
          + " AND    SP.ID_PARTICELLA = CP.ID_PARTICELLA "
          + " AND    CP.ID_UTE = U.ID_UTE "
          + " AND    U.ID_AZIENDA = AA.ID_AZIENDA "
          + " AND    CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO "
          + " AND    C.ISTAT_COMUNE = AA.SEDELEG_COMUNE "
          + " AND    P.ISTAT_PROVINCIA = C.ISTAT_PROVINCIA "
          + " AND    AA.DATA_FINE_VALIDITA IS NULL ";

      if (attive)
      {
        query += " AND CP.DATA_FINE_CONDUZIONE IS NULL "
            + " AND AA.DATA_CESSAZIONE IS NULL ";
      }

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idStoricoParticella.longValue());

      SolmrLogger.debug(this,
          "Executing getElencoAziendeAndConduzioniByIdStoricoParticella: "
              + query);

      ResultSet rs = stmt.executeQuery();

      elencoAziendeConduzioni = new Vector<ParticellaAziendaVO>();

      // Recupero i records estratti dalla query
      while (rs.next())
      {
        ParticellaAziendaVO particellaAziendaVO = new ParticellaAziendaVO();
        particellaAziendaVO.setIdAnagraficaAzienda(new Long(rs.getLong(1)));
        particellaAziendaVO.setDataInizioConduzione(new Date(rs.getDate(2)
            .getTime()));
        if (Validator.isNotEmpty(rs.getString(3)))
        {
          particellaAziendaVO.setDataFineConduzione(new Date(rs.getDate(3)
              .getTime()));
        }
        particellaAziendaVO.setDescTitoloPossesso(rs.getString(4));
        particellaAziendaVO.setSuperficieCondotta(StringUtils
            .parseSuperficieField(rs.getString(5)));
        particellaAziendaVO.setDenominazione(rs.getString(6).toUpperCase());
        particellaAziendaVO.setCuaa(rs.getString(7).toUpperCase());
        if (rs.getString(9).equalsIgnoreCase(
            (String) SolmrConstants.get("SIGLA_PROVINCIA_ESTERO")))
        {
          particellaAziendaVO.setDescStatoEsteroSedeLegale(rs.getString(8));
        }
        else
        {
          particellaAziendaVO.setDescComuneSedeLegale(rs.getString(8));
          particellaAziendaVO.setSiglaProvinciaSedeLegale(rs.getString(9));
        }
        particellaAziendaVO.setIdConduzioneParticella(new Long(rs.getLong(10)));
        elencoAziendeConduzioni.add(particellaAziendaVO);
      }

      if (elencoAziendeConduzioni.size() == 0 && attive)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_NO_AZIENDE_CONDUZIONI_ATTIVE"));
      }
      else if (elencoAziendeConduzioni.size() == 0 && !attive)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_NO_AZIENDE_CONDUZIONI"));
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getElencoAziendeAndConduzioniByIdStoricoParticella - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (SolmrException se)
    {
      SolmrLogger.fatal(this,
          "getElencoAziendeAndConduzioniByIdStoricoParticella - SQLException: "
              + se.getMessage());
      throw new SolmrException(se.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getElencoAziendeAndConduzioniByIdStoricoParticella - Generic Exception: "
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
                "getElencoAziendeAndConduzioniByIdStoricoParticella - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getElencoAziendeAndConduzioniByIdStoricoParticella - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoAziendeConduzioni;
  }

  // Metodo per recuperare l'elenco degli utilizzi relativi a conduzioni attive
  // in un anno
  // specificato
  public Vector<ParticellaUtilizzoVO> getElencoUtilizziAttiviByAnnoAndIdStoricoParticella(
      Long idStoricoParticella, String anno) throws DataAccessException,
      SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaUtilizzoVO> elencoUtilizziAttivi = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT TU.DESCRIZIONE, "
          + "        UP.SUPERFICIE_UTILIZZATA "
          + " FROM   DB_PARTICELLA P, "
          + "        DB_STORICO_PARTICELLA SP, "
          + "        DB_CONDUZIONE_PARTICELLA CP, "
          + "        DB_UTILIZZO_PARTICELLA UP, "
          + "        DB_TIPO_UTILIZZO TU "
          + " WHERE  SP.ID_STORICO_PARTICELLA = ? "
          + " AND    P.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND    SP.ID_PARTICELLA = CP.ID_PARTICELLA "
          + " AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA "
          + " AND    CP.DATA_FINE_CONDUZIONE IS NULL " + " AND    UP.ANNO = ? "
          + " AND    UP.ID_UTILIZZO = TU.ID_UTILIZZO ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idStoricoParticella.longValue());
      stmt.setString(2, anno);

      SolmrLogger.debug(this,
          "Executing getElencoUtilizziAttiviByAnnoAndIdStoricoParticella: "
              + query);

      ResultSet rs = stmt.executeQuery();

      elencoUtilizziAttivi = new Vector<ParticellaUtilizzoVO>();

      // Recupero i records estratti dalla query
      while (rs.next())
      {
        ParticellaUtilizzoVO particellaUtilizzoVO = new ParticellaUtilizzoVO();
        particellaUtilizzoVO.setDescTipoUtilizzo(rs.getString(1));
        particellaUtilizzoVO.setSuperficieUtilizzata(StringUtils
            .parseSuperficieField(rs.getString(2)));
        elencoUtilizziAttivi.add(particellaUtilizzoVO);
      }

      if (elencoUtilizziAttivi.size() == 0)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_NO_UTILIZZI_ATTIVI"));
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getElencoUtilizziAttiviByAnnoAndIdStoricoParticella - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (SolmrException se)
    {
      SolmrLogger.fatal(this,
          "getElencoUtilizziAttiviByAnnoAndIdStoricoParticella - SQLException: "
              + se.getMessage());
      throw new SolmrException(se.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getElencoUtilizziAttiviByAnnoAndIdStoricoParticella - Generic Exception: "
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
                "getElencoUtilizziAttiviByAnnoAndIdStoricoParticella - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getElencoUtilizziAttiviByAnnoAndIdStoricoParticella - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoUtilizziAttivi;
  }

  // Metodo per aggiornare un record su DB_PARTICELLA(in questo momento viene
  // utilizzato solo per
  // aggiornare il campo DATA_INIZIO_VALIDITA ma lo implemento in modo che in
  // futuro, su richiesta
  // del dominio, si possano modificare anche altri campi)
  public void aggiornaParticella(ParticellaVO particellaVO)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " UPDATE DB_PARTICELLA "
          + " SET    DATA_INIZIO_VALIDITA = TRUNC(SYSDATE) "
          + " WHERE  ID_PARTICELLA = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing aggiornaParticella: " + query);

      stmt.setLong(1, particellaVO.getIdParticella().longValue());

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed aggiornaParticella in fascicoloDAO");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "aggiornaParticella in fascicoloDAO - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "aggiornaParticella in fascicoloDAO - Generic Exception: " + ex);
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
                "aggiornaParticella in fascicoloDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "aggiornaParticella in fascicoloDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }


  // Metodo per recuperare l'elenco delle variazioni storiche di una determinata
  // particella a
  // partire dall'id storico particella
  public Vector<ParticellaVO> getElencoStoricoParticella(Long idParticella)
      throws SolmrException, DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaVO> elencoStoricoParticella = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " "
      		+ "SELECT   "
      		+ "       SP.SUP_CATASTALE, "
      		+ "       SP.SUPERFICIE_GRAFICA, "
          + "       SP.DATA_INIZIO_VALIDITA, "
          + "       SP.DATA_FINE_VALIDITA, "
          + "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
          "          || ' ' " + 
          "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
          "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
          "         WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
          "         AS DENOMINAZIONE, " +
          "       (SELECT PVU.DENOMINAZIONE " +
          "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
          "        WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
          "         AS DESCRIZIONE_ENTE_APPARTENENZA, "
          + "       SP.ID_FONTE, "
          + "       TF.DESCRIZIONE AS DESC_FONTE, "
          + "       SP.ID_CAUSALE_MOD_PARTICELLA, "
          + "       TCMP.DESCRIZIONE AS DESC_CAUSALE_MOD_PART, "
          + "       SP.ID_DOCUMENTO, "
          + "       TD.DESCRIZIONE AS DESC_DOCUMENTO "
          + " FROM  DB_STORICO_PARTICELLA SP, "
          //+ "       PAPUA_V_UTENTE_LOGIN PVU, "
          + "       DB_TIPO_FONTE TF, "
          + "       DB_TIPO_CAUSALE_MOD_PARTICELLA TCMP, "
          + "       DB_TIPO_DOCUMENTO TD "
          + " WHERE SP.ID_PARTICELLA = ? "
          //+ " AND   PVU.ID_UTENTE_LOGIN = SP.ID_UTENTE_AGGIORNAMENTO "
          + " AND   TF.ID_FONTE(+) = SP.ID_FONTE "
          + " AND   TCMP.ID_CAUSALE_MOD_PARTICELLA(+) = SP.ID_CAUSALE_MOD_PARTICELLA "
          + " AND   TD.ID_DOCUMENTO(+) = SP.ID_DOCUMENTO "
          + "  ORDER BY SP.ID_STORICO_PARTICELLA DESC ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idParticella.longValue());

      SolmrLogger.debug(this, "Executing getElencoStoricoParticella: " + query);

      ResultSet rs = stmt.executeQuery();

      elencoStoricoParticella = new Vector<ParticellaVO>();

      while (rs.next())
      {
        ParticellaVO particellaVO = new ParticellaVO();
        particellaVO.setSupCatastale(StringUtils.parseSuperficieField(rs
            .getString("SUP_CATASTALE")));
        
        particellaVO.setSuperficieGrafica(StringUtils.parseSuperficieField(rs
            .getString("SUPERFICIE_GRAFICA")));
        
        particellaVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        if (Validator.isNotEmpty(rs.getString("DATA_FINE_VALIDITA")))
        {
          particellaVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        }
        String utenteAggiornamento = "";
        if (Validator.isNotEmpty(rs.getString("DENOMINAZIONE")))
        {
          utenteAggiornamento += rs.getString("DENOMINAZIONE") + " - ";
        }
        if (Validator.isNotEmpty(rs.getString("DESCRIZIONE_ENTE_APPARTENENZA")))
        {
          utenteAggiornamento += rs.getString("DESCRIZIONE_ENTE_APPARTENENZA");
        }
        particellaVO.setDenominazioneUtenteAggiornamento(utenteAggiornamento);
        if (Validator.isNotEmpty(rs.getString("ID_FONTE")))
        {
          CodeDescription fonteDato = new CodeDescription(Integer.decode(rs
              .getString("ID_FONTE")), rs.getString("DESC_FONTE"));
          particellaVO.setFonteDato(fonteDato);
        }
        if (Validator.isNotEmpty(rs.getString("ID_CAUSALE_MOD_PARTICELLA")))
        {
          CodeDescription causaleModParticella = new CodeDescription(Integer
              .decode(rs.getString("ID_CAUSALE_MOD_PARTICELLA")), rs.getString("DESC_CAUSALE_MOD_PART"));
          particellaVO.setCausaleModParticella(causaleModParticella);
        }
        if (Validator.isNotEmpty(rs.getString("ID_DOCUMENTO")))
        {
          CodeDescription tipoDocumento = new CodeDescription(Integer.decode(rs
              .getString("ID_DOCUMENTO")), rs.getString("DESC_DOCUMENTO"));
          particellaVO.setTipoDocumento(tipoDocumento);
        }
        elencoStoricoParticella.add(particellaVO);
      }

      // Controllo se è stato trovato qualcosa
      if (elencoStoricoParticella.size() == 0)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_NO_VARIAZIONI_CENSITE"));
      }

      SolmrLogger.debug(this, "Executed getElencoStoricoParticella!");

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getElencoStoricoParticella - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getElencoStoricoParticella - Generic Exception: " + ex);
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
                "getElencoStoricoParticella - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getElencoStoricoParticella - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    return elencoStoricoParticella;
  }

  // Metodo per certificare la modifica di una particella in una qualsiasi della
  // sue parti
  // conduzione/utilizzo
  public void certificateUpdateParticella(ParticellaVO particellaVO,
      long idUtenteAggiornamento) throws DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating certificateUpdateParticella method in FascicoloDAO");
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      SolmrLogger
          .debug(this,
              "Creating db-connection in certificateUpdateParticella method in FascicoloDAO");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in certificateUpdateParticella method in FascicoloDAO and it values: "
                  + conn);

      String query = " UPDATE DB_CONDUZIONE_PARTICELLA "
          + " SET    RECORD_MODIFICATO = ?, "
          + "        DATA_AGGIORNAMENTO = SYSDATE, "
          + "        ID_UTENTE_AGGIORNAMENTO = ? "
          + " WHERE  ID_CONDUZIONE_PARTICELLA = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(this, "Executing certificateUpdateParticella: " + query);

      if (particellaVO.isRecordModificato())
      {
        stmt.setString(1, SolmrConstants.FLAG_S);
        SolmrLogger
            .debug(
                this,
                "Parameter 1 [FLAG_MODIFICATO] in certificateUpdateParticella in FascicoloDAO: "
                    + SolmrConstants.FLAG_S);
      }
      else
      {
        stmt.setString(1, SolmrConstants.FLAG_N);
        SolmrLogger
            .debug(
                this,
                "Parameter 1 [FLAG_MODIFICATO] in certificateUpdateParticella in FascicoloDAO: "
                    + SolmrConstants.FLAG_S);
      }
      stmt.setLong(2, idUtenteAggiornamento);
      SolmrLogger.debug(this,
          "Parameter 2 [ID_UTENTE] in certificateUpdateParticella in FascicoloDAO: "
              + idUtenteAggiornamento);
      stmt.setLong(3, particellaVO.getIdConduzioneParticella().longValue());
      SolmrLogger
          .debug(
              this,
              "Parameter 3 [ID_CONDUZIONE_PARTICELLA] in certificateUpdateParticella in FascicoloDAO: "
                  + particellaVO.getIdConduzioneParticella().longValue());

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this,
          "Executed certificateUpdateParticella in fascicoloDAO");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "certificateUpdateParticella in fascicoloDAO - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "certificateUpdateParticella in fascicoloDAO - Generic Exception: "
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
                "certificateUpdateParticella in fascicoloDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "certificateUpdateParticella in fascicoloDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this,
        "Invocated certificateUpdateParticella method in FascicoloDAO");
  }

  /**
   * Metodo per recuperare l'elenco delle particelle in relazione ad un'azienda
   * e al suo stato
   * 
   */
  public Vector<ParticellaVO> getElencoParticelleForImportByAzienda(
      AnagAziendaVO searchAnagAziendaVO, AnagAziendaVO anagAziendaVO,
      RuoloUtenza ruoloUtenza) throws DataAccessException, SolmrException
  {
    SolmrLogger
        .debug(this,
            "Invocating method getElencoParticelleForImportByAzienda in FascicoloDAO");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaVO> elencoParticelle = null;

    try
    {
      SolmrLogger
          .debug(
              this,
              "Creating connection in method getElencoParticelleForImportByAzienda in FascicoloDAO");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created connection in method getElencoParticelleForImportByAzienda in FascicoloDAO with value: "
                  + conn);

      SolmrLogger
          .debug(
              this,
              "Creating query in method getElencoParticelleForImportByAzienda in FascicoloDAO");
      String query = "";
      // Se l'azienda cedente è cessata reperisco solo le conduzioni cessate
      // alla stessa data in cui
      // è stata cessata l'azienda
      if (Validator.isNotEmpty(searchAnagAziendaVO.getDataCessazione()))
      {
        query += " SELECT CP.ID_CONDUZIONE_PARTICELLA, "
            + "        SP.COMUNE, " 
            + "        C.DESCOM, "
            + "        SP.SEZIONE, " 
            + "        SP.FOGLIO, "
            + "        SP.PARTICELLA, " 
            + "        SP.SUBALTERNO, "
            + "        SP.SUP_CATASTALE, "
            + "        SP.SUPERFICIE_GRAFICA, "
            + "        CP.ID_TITOLO_POSSESSO, "
            + "        CP.SUPERFICIE_CONDOTTA, "
            + "        CP.PERCENTUALE_POSSESSO, "
            + "        P.SIGLA_PROVINCIA "
            + " FROM   DB_UTE U, "
            + "        DB_CONDUZIONE_PARTICELLA CP, "
            + "        DB_STORICO_PARTICELLA SP, " 
            + "        COMUNE C, "
            + "        PROVINCIA P "
            + " WHERE  U.ID_AZIENDA = ? " + " AND    U.ID_UTE = CP.ID_UTE "
            + " AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA "
            + " AND    SP.COMUNE = C.ISTAT_COMUNE "
            + " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
            + " AND    SP.DATA_FINE_VALIDITA IS NULL "
            + " AND    CP.ID_TITOLO_POSSESSO <> 5 "
            + " AND    CP.ID_TITOLO_POSSESSO <> 6 "            
            + " AND    CP.DATA_FINE_CONDUZIONE = ? ";
      }
      // Altrimenti...
      else
      {
        // Se l'utente è un intermediario...
        if (ruoloUtenza.isUtenteIntermediario()
            || ruoloUtenza.isUtenteOPRGestore())
        {
          String codFiscaleIntermediarioDelegaRicevente = null;
          if (anagAziendaVO.getDelegaVO() != null)
          {
            codFiscaleIntermediarioDelegaRicevente = anagAziendaVO
                .getDelegaVO().getCodiceFiscaleIntermediario().substring(0, 3);
          }
          if (searchAnagAziendaVO.getDelegaVO()!=null && searchAnagAziendaVO.getDelegaVO().getCodiceFiscaleIntermediario()!=null) 
	          SolmrLogger
	              .debug(
	                  this,
	                  "Value of parameter [CODICE_FISCALE_INTERMEDIARIO_CEDENTE].substring(0,3) in method getElencoParticelleForImportByAzienda in FascicoloDAO: "
	                      + searchAnagAziendaVO.getDelegaVO()
	                          .getCodiceFiscaleIntermediario().substring(0, 3)
	                      + "\n");
          SolmrLogger
              .debug(
                  this,
                  "Value of parameter [CODICE_FISCALE_INTERMEDIARIO_RICEVENTE].substring(0,3) in method getElencoParticelleForImportByAzienda in FascicoloDAO: "
                      + codFiscaleIntermediarioDelegaRicevente + "\n");
          // Se l'azienda cedente non ha conferito mandato oppure ha dato
          // mandato allo stesso CAA
          // dell'azienda ricevente reperisco solo le conduzioni attive più
          // tutte le conduzioni
          // cessate presenti nell'ultima dichiarazione di consistenza
          if (!searchAnagAziendaVO.isPossiedeDelegaAttiva()
              || searchAnagAziendaVO.getDelegaVO()
                  .getCodiceFiscaleIntermediario().substring(0, 3)
                  .equalsIgnoreCase(codFiscaleIntermediarioDelegaRicevente))
          {
            query += " SELECT CP.ID_CONDUZIONE_PARTICELLA, "
                + "        SP.COMUNE, "
                + "        C.DESCOM, "
                + "        SP.SEZIONE, "
                + "        SP.FOGLIO, "
                + "        SP.PARTICELLA, "
                + "        SP.SUBALTERNO, "
                + "        SP.SUP_CATASTALE, "
                + "        SP.SUPERFICIE_GRAFICA, "
                + "        CP.ID_TITOLO_POSSESSO, "
                + "        CP.SUPERFICIE_CONDOTTA, "
                + "        CP.PERCENTUALE_POSSESSO, "
                + "        P.SIGLA_PROVINCIA "
                + " FROM   DB_UTE U, "
                + "        DB_CONDUZIONE_PARTICELLA CP, "
                + "        DB_STORICO_PARTICELLA SP, "
                + "        COMUNE C, "
                + "        PROVINCIA P "
                + " WHERE  U.ID_AZIENDA = ? "
                + " AND    U.ID_UTE = CP.ID_UTE "
                + " AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA "
                + " AND    SP.COMUNE = C.ISTAT_COMUNE "
                + " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
                + " AND    SP.DATA_FINE_VALIDITA IS NULL "
                + " AND    CP.DATA_FINE_CONDUZIONE IS NULL "
                + " AND    CP.ID_TITOLO_POSSESSO <> 5 "
                + " AND    CP.ID_TITOLO_POSSESSO <> 6 "    
                + " UNION "
                + " SELECT CD.ID_CONDUZIONE_PARTICELLA, "
                + "        SP.COMUNE, "
                + "        C.DESCOM, "
                + "        SP.SEZIONE, "
                + "        SP.FOGLIO, "
                + "        SP.PARTICELLA, "
                + "        SP.SUBALTERNO, "
                + "        SP.SUP_CATASTALE, "
                + "        SP.SUPERFICIE_GRAFICA, "
                + "        CD.ID_TITOLO_POSSESSO, "
                + "        CD.SUPERFICIE_CONDOTTA, "
                + "        CD.PERCENTUALE_POSSESSO, "
                + "        P.SIGLA_PROVINCIA "
                + " FROM   DB_CONDUZIONE_DICHIARATA CD, "
                + "        DB_CONDUZIONE_PARTICELLA CP, "
                + "        DB_UTE U, "
                + "        DB_STORICO_PARTICELLA SP, "
                + "        COMUNE C, "
                + "        PROVINCIA P "
                + " WHERE  U.ID_AZIENDA = ? "
                + " AND    CD.ID_UTE = U.ID_UTE "
                + " AND    CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA "
                + " AND    SP.COMUNE = C.ISTAT_COMUNE "
                + " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
                + " AND    SP.DATA_FINE_VALIDITA IS NULL "
                + " AND    CP.DATA_FINE_CONDUZIONE IS NOT NULL "
                + " AND    CD.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA "
                + " AND    CP.ID_TITOLO_POSSESSO <> 5 "
                + " AND    CP.ID_TITOLO_POSSESSO <> 6 "    
                + " AND    CD.ID_TITOLO_POSSESSO <> 5 "
                + " AND    CD.ID_TITOLO_POSSESSO <> 6 "    
                + " AND    CD.CODICE_FOTOGRAFIA_TERRENI = "
                + "        (SELECT MAX(CODICE_FOTOGRAFIA_TERRENI) "
                + "         FROM   DB_DICHIARAZIONE_CONSISTENZA DC "
                + "         WHERE  DC.ID_AZIENDA = ? "
                + "         AND    DC.DATA_INSERIMENTO_DICHIARAZIONE = (SELECT MAX(DC1.DATA_INSERIMENTO_DICHIARAZIONE) "
                + "                                                     FROM   DB_DICHIARAZIONE_CONSISTENZA DC1, DB_TIPO_MOTIVO_DICHIARAZIONE D "
                + "                                                     WHERE  DC1.ID_AZIENDA = ? "
                + "                                                     AND D.ID_MOTIVO_DICHIARAZIONE=DC1.ID_MOTIVO_DICHIARAZIONE "
                + "                                                     AND D.ID_MOTIVO_DICHIARAZIONE<>7 "
                + "                                                     AND D.TIPO_DICHIARAZIONE<>'C' )) ";
          }
          // Altrimenti reperisco solo le conduzioni cessate presenti
          // sull'ultima dichiarazione
          // di consistenza dell'azienda.
          else
          {
            query += " SELECT CD.ID_CONDUZIONE_PARTICELLA, "
                + "        SP.COMUNE, "
                + "        C.DESCOM, "
                + "        SP.SEZIONE, "
                + "        SP.FOGLIO, "
                + "        SP.PARTICELLA, "
                + "        SP.SUBALTERNO, "
                + "        SP.SUP_CATASTALE, "
                + "        SP.SUPERFICIE_GRAFICA, "
                + "        CD.ID_TITOLO_POSSESSO, "
                + "        CD.SUPERFICIE_CONDOTTA, "
                + "        CD.PERCENTUALE_POSSESSO, "
                + "        P.SIGLA_PROVINCIA "
                + " FROM   DB_CONDUZIONE_DICHIARATA CD, "
                + "        DB_CONDUZIONE_PARTICELLA CP, "
                + "        DB_UTE U, "
                + "        DB_STORICO_PARTICELLA SP, "
                + "        COMUNE C, "
                + "        PROVINCIA P "
                + " WHERE  U.ID_AZIENDA = ? "
                + " AND    CD.ID_UTE = U.ID_UTE "
                + " AND    CD.ID_PARTICELLA = SP.ID_PARTICELLA "
                + " AND    SP.COMUNE = C.ISTAT_COMUNE "
                + " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
                + " AND    SP.DATA_FINE_VALIDITA IS NULL "
                + " AND    CP.DATA_FINE_CONDUZIONE IS NOT NULL "
                + " AND    CD.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA "
                + " AND    CP.ID_TITOLO_POSSESSO <> 5 "
                + " AND    CP.ID_TITOLO_POSSESSO <> 6 "
                + " AND    CD.ID_TITOLO_POSSESSO <> 5 "
                + " AND    CD.ID_TITOLO_POSSESSO <> 6 "
                + " AND    CD.CODICE_FOTOGRAFIA_TERRENI = "
                + "        (SELECT MAX(CODICE_FOTOGRAFIA_TERRENI) "
                + "         FROM   DB_DICHIARAZIONE_CONSISTENZA DC "
                + "         WHERE  DC.ID_AZIENDA = ? "
                + "         AND    DC.DATA_INSERIMENTO_DICHIARAZIONE = (SELECT MAX(DC1.DATA_INSERIMENTO_DICHIARAZIONE) "
                + "                                                     FROM   DB_DICHIARAZIONE_CONSISTENZA DC1, DB_TIPO_MOTIVO_DICHIARAZIONE D "
                + "                                                     WHERE  DC1.ID_AZIENDA = ? "
                + "                                                     AND D.ID_MOTIVO_DICHIARAZIONE=DC1.ID_MOTIVO_DICHIARAZIONE "
                + "                                                     AND D.ID_MOTIVO_DICHIARAZIONE<>7 "
                + "                                                     AND D.TIPO_DICHIARAZIONE<>'C' )) ";
          }
        }
        // Se l'utente è un funzionario PA
        else
        {
          // Se l'azienda cedente non ha conferito mandato a nessun CAA
          // reperisco solo le conduzioni
          // attive  più
          // tutte le conduzioni
          // cessate presenti nell'ultima dichiarazione di consistenza
          if (!searchAnagAziendaVO.isPossiedeDelegaAttiva())
          {
            query += " SELECT CP.ID_CONDUZIONE_PARTICELLA, "
                + "        SP.COMUNE, "
                + "        C.DESCOM, "
                + "        SP.SEZIONE, "
                + "        SP.FOGLIO, "
                + "        SP.PARTICELLA, "
                + "        SP.SUBALTERNO, "
                + "        SP.SUP_CATASTALE, "
                + "        SP.SUPERFICIE_GRAFICA, "
                + "        CP.ID_TITOLO_POSSESSO, "
                + "        CP.SUPERFICIE_CONDOTTA, "
                + "        CP.PERCENTUALE_POSSESSO, "
                + "        P.SIGLA_PROVINCIA "
                + " FROM   DB_UTE U, "
                + "        DB_CONDUZIONE_PARTICELLA CP, "
                + "        DB_STORICO_PARTICELLA SP, "
                + "        COMUNE C, "
                + "        PROVINCIA P "
                + " WHERE  U.ID_AZIENDA = ? "
                + " AND    U.ID_UTE = CP.ID_UTE "
                + " AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA "
                + " AND    SP.COMUNE = C.ISTAT_COMUNE "
                + " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
                + " AND    SP.DATA_FINE_VALIDITA IS NULL "
                + " AND    CP.DATA_FINE_CONDUZIONE IS NULL "
                + " AND    CP.ID_TITOLO_POSSESSO <> 5 "
                + " AND    CP.ID_TITOLO_POSSESSO <> 6 "
                + " UNION "
                + " SELECT CD.ID_CONDUZIONE_PARTICELLA, "
                + "        SP.COMUNE, "
                + "        C.DESCOM, "
                + "        SP.SEZIONE, "
                + "        SP.FOGLIO, "
                + "        SP.PARTICELLA, "
                + "        SP.SUBALTERNO, "
                + "        SP.SUP_CATASTALE, "
                + "        SP.SUPERFICIE_GRAFICA, "
                + "        CD.ID_TITOLO_POSSESSO, "
                + "        CD.SUPERFICIE_CONDOTTA, "
                + "        CD.PERCENTUALE_POSSESSO, "
                + "        P.SIGLA_PROVINCIA "
                + " FROM   DB_CONDUZIONE_DICHIARATA CD, "
                + "        DB_CONDUZIONE_PARTICELLA CP, "
                + "        DB_UTE U, "
                + "        DB_STORICO_PARTICELLA SP, "
                + "        COMUNE C, "
                + "        PROVINCIA P "
                + " WHERE  U.ID_AZIENDA = ? "
                + " AND    CD.ID_UTE = U.ID_UTE "
                + " AND    CD.ID_PARTICELLA = SP.ID_PARTICELLA "
                + " AND    SP.COMUNE = C.ISTAT_COMUNE "
                + " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
                + " AND    SP.DATA_FINE_VALIDITA IS NULL "
                + " AND    CP.DATA_FINE_CONDUZIONE IS NOT NULL "
                + " AND    CD.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA "
                + " AND    CP.ID_TITOLO_POSSESSO <> 5 "
                + " AND    CP.ID_TITOLO_POSSESSO <> 6 "
                + " AND    CD.ID_TITOLO_POSSESSO <> 5 "
                + " AND    CD.ID_TITOLO_POSSESSO <> 6 "
                + " AND    CD.CODICE_FOTOGRAFIA_TERRENI = "
                + "        (SELECT MAX(CODICE_FOTOGRAFIA_TERRENI) "
                + "         FROM   DB_DICHIARAZIONE_CONSISTENZA DC "
                + "         WHERE  DC.ID_AZIENDA = ? "
                + "         AND    DC.DATA_INSERIMENTO_DICHIARAZIONE = (SELECT MAX(DC1.DATA_INSERIMENTO_DICHIARAZIONE) "
                + "                                                     FROM   DB_DICHIARAZIONE_CONSISTENZA DC1, DB_TIPO_MOTIVO_DICHIARAZIONE D "
                + "                                                     WHERE  DC1.ID_AZIENDA = ? "
                + "                                                     AND D.ID_MOTIVO_DICHIARAZIONE=DC1.ID_MOTIVO_DICHIARAZIONE "
                + "                                                     AND D.ID_MOTIVO_DICHIARAZIONE<>7 "
                + "                                                     AND D.TIPO_DICHIARAZIONE<>'C' )) ";
          }
          // Se invece l'azienda cedente ha conferito mandato ad un CAA
          // reperisco solo le conduzioni cessate sull'ultima dichiarazione di consistenza,
          // nel caso la stessa conduzione sia stata cessata più volte, prendo
          // la più recente
          else
          {
            query += " SELECT CD.ID_CONDUZIONE_PARTICELLA, "
                + "        SP.COMUNE, "
                + "        C.DESCOM, "
                + "        SP.SEZIONE, "
                + "        SP.FOGLIO, "
                + "        SP.PARTICELLA, "
                + "        SP.SUBALTERNO, "
                + "        SP.SUP_CATASTALE, "
                + "        SP.SUPERFICIE_GRAFICA, "
                + "        CD.ID_TITOLO_POSSESSO, "
                + "        CD.SUPERFICIE_CONDOTTA, "
                + "        CD.PERCENTUALE_POSSESSO, "
                + "        P.SIGLA_PROVINCIA "
                + " FROM   DB_CONDUZIONE_DICHIARATA CD, "
                + "        DB_CONDUZIONE_PARTICELLA CP, "
                + "        DB_UTE U, "
                + "        DB_STORICO_PARTICELLA SP, "
                + "        COMUNE C, "
                + "        PROVINCIA P "
                + " WHERE  U.ID_AZIENDA = ? "
                + " AND    CD.ID_UTE = U.ID_UTE "
                + " AND    CD.ID_PARTICELLA = SP.ID_PARTICELLA "
                + " AND    SP.COMUNE = C.ISTAT_COMUNE "
                + " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
                + " AND    SP.DATA_FINE_VALIDITA IS NULL "
                + " AND    CP.DATA_FINE_CONDUZIONE IS NOT NULL "
                + " AND    CD.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA "
                + " AND    CP.ID_TITOLO_POSSESSO <> 5 "
                + " AND    CP.ID_TITOLO_POSSESSO <> 6 "
                + " AND    CD.ID_TITOLO_POSSESSO <> 5 "
                + " AND    CD.ID_TITOLO_POSSESSO <> 6 "
                + " AND    CD.CODICE_FOTOGRAFIA_TERRENI = "
                + "        (SELECT MAX(CODICE_FOTOGRAFIA_TERRENI) "
                + "         FROM   DB_DICHIARAZIONE_CONSISTENZA DC "
                + "         WHERE  DC.ID_AZIENDA = ? "
                + "         AND    DC.DATA_INSERIMENTO_DICHIARAZIONE = (SELECT MAX(DC1.DATA_INSERIMENTO_DICHIARAZIONE) "                
                + "                                                     FROM   DB_DICHIARAZIONE_CONSISTENZA DC1, DB_TIPO_MOTIVO_DICHIARAZIONE D "
                + "                                                     WHERE  DC1.ID_AZIENDA = ? "
                + "                                                     AND D.ID_MOTIVO_DICHIARAZIONE=DC1.ID_MOTIVO_DICHIARAZIONE "
                + "                                                     AND D.ID_MOTIVO_DICHIARAZIONE<>7 "
                + "                                                     AND D.TIPO_DICHIARAZIONE<>'C' )) ";
          }
        }
      }
      query += " ORDER BY 3, 4, 5, 6, 7 ";
      SolmrLogger
          .debug(this,
              "Created query in method getElencoParticelleForImportByAzienda in FascicoloDAO");

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(
              this,
              "Setting parameters in method getElencoParticelleForImportByAzienda in FascicoloDAO");
      // CASO AZIENDA CESSATA
      if (Validator.isNotEmpty(searchAnagAziendaVO.getDataCessazione()))
      {
        stmt.setLong(1, searchAnagAziendaVO.getIdAzienda().longValue());
        SolmrLogger
            .debug(
                this,
                "Parameter 1 [ID_AZIENDA] in method getElencoParticelleForImportByAzienda in FascicoloDAO: "
                    + searchAnagAziendaVO.getIdAzienda());
        stmt.setDate(2, new java.sql.Date(searchAnagAziendaVO
            .getDataCessazione().getTime()));
        SolmrLogger
            .debug(
                this,
                "Parameter 2 [DATA_CESSAZIONE] in method getElencoParticelleForImportByAzienda in FascicoloDAO: "
                    + searchAnagAziendaVO.getDataCessazione());
      }
      // CASI AZIENDA ATTIVA
      else
      {
        // CASI UTENTE INTERMEDIARIO
        if (ruoloUtenza.isUtenteIntermediario()
            || ruoloUtenza.isUtenteOPRGestore())
        {
          String codFiscaleIntermediarioDelegaRicevente = null;
          if (anagAziendaVO.getDelegaVO() != null)
          {
            codFiscaleIntermediarioDelegaRicevente = anagAziendaVO
                .getDelegaVO().getCodiceFiscaleIntermediario().substring(0, 3);
          }
          // L'azienda cedente non ha conferito mandato oppure ha dato mandato
          // allo stesso CAA
          // dell'azienda ricevente
          if (!searchAnagAziendaVO.isPossiedeDelegaAttiva()
              || searchAnagAziendaVO.getDelegaVO()
                  .getCodiceFiscaleIntermediario().substring(0, 3)
                  .equalsIgnoreCase(codFiscaleIntermediarioDelegaRicevente))
          {
            stmt.setLong(1, searchAnagAziendaVO.getIdAzienda().longValue());
            SolmrLogger
                .debug(
                    this,
                    "Parameter 1 [ID_AZIENDA] in method getElencoParticelleForImportByAzienda in FascicoloDAO: "
                        + searchAnagAziendaVO.getIdAzienda());
            stmt.setLong(2, searchAnagAziendaVO.getIdAzienda().longValue());
            SolmrLogger
                .debug(
                    this,
                    "Parameter 2 [ID_AZIENDA] in method getElencoParticelleForImportByAzienda in FascicoloDAO: "
                        + searchAnagAziendaVO.getIdAzienda());
            stmt.setLong(3, searchAnagAziendaVO.getIdAzienda().longValue());
            SolmrLogger
                .debug(
                    this,
                    "Parameter 3 [ID_AZIENDA] in method getElencoParticelleForImportByAzienda in FascicoloDAO: "
                        + searchAnagAziendaVO.getIdAzienda());
            stmt.setLong(4, searchAnagAziendaVO.getIdAzienda().longValue());
            SolmrLogger
                .debug(
                    this,
                    "Parameter 4 [ID_AZIENDA] in method getElencoParticelleForImportByAzienda in FascicoloDAO: "
                        + searchAnagAziendaVO.getIdAzienda());
          }
          else
          {
            stmt.setLong(1, searchAnagAziendaVO.getIdAzienda().longValue());
            SolmrLogger
                .debug(
                    this,
                    "Parameter 1 [ID_AZIENDA] in method getElencoParticelleForImportByAzienda in FascicoloDAO: "
                        + searchAnagAziendaVO.getIdAzienda());
            stmt.setLong(2, searchAnagAziendaVO.getIdAzienda().longValue());
            SolmrLogger
                .debug(
                    this,
                    "Parameter 2 [ID_AZIENDA] in method getElencoParticelleForImportByAzienda in FascicoloDAO: "
                        + searchAnagAziendaVO.getIdAzienda());
            stmt.setLong(3, searchAnagAziendaVO.getIdAzienda().longValue());
            SolmrLogger
                .debug(
                    this,
                    "Parameter 3 [ID_AZIENDA] in method getElencoParticelleForImportByAzienda in FascicoloDAO: "
                        + searchAnagAziendaVO.getIdAzienda());
          }
        }
        // CASI UTENTE PA
        else
        {
          // Se l'azienda cedente non ha conferito mandato a nessun CAA
          if (!searchAnagAziendaVO.isPossiedeDelegaAttiva())
          {
            stmt.setLong(1, searchAnagAziendaVO.getIdAzienda().longValue());
            SolmrLogger
                .debug(
                    this,
                    "Parameter 1 [ID_AZIENDA] in method getElencoParticelleForImportByAzienda in FascicoloDAO: "
                        + searchAnagAziendaVO.getIdAzienda());
            stmt.setLong(2, searchAnagAziendaVO.getIdAzienda().longValue());
            SolmrLogger
                .debug(
                    this,
                    "Parameter 2 [ID_AZIENDA] in method getElencoParticelleForImportByAzienda in FascicoloDAO: "
                        + searchAnagAziendaVO.getIdAzienda());
            stmt.setLong(3, searchAnagAziendaVO.getIdAzienda().longValue());
            SolmrLogger
                .debug(
                    this,
                    "Parameter 3 [ID_AZIENDA] in method getElencoParticelleForImportByAzienda in FascicoloDAO: "
                        + searchAnagAziendaVO.getIdAzienda());
            stmt.setLong(4, searchAnagAziendaVO.getIdAzienda().longValue());
            SolmrLogger
                .debug(
                    this,
                    "Parameter 4 [ID_AZIENDA] in method getElencoParticelleForImportByAzienda in FascicoloDAO: "
                        + searchAnagAziendaVO.getIdAzienda());
          }
          else
          {
            stmt.setLong(1, searchAnagAziendaVO.getIdAzienda().longValue());
            SolmrLogger
                .debug(
                    this,
                    "Parameter 1 [ID_AZIENDA] in method getElencoParticelleForImportByAzienda in FascicoloDAO: "
                        + searchAnagAziendaVO.getIdAzienda());
            stmt.setLong(2, searchAnagAziendaVO.getIdAzienda().longValue());
            SolmrLogger
                .debug(
                    this,
                    "Parameter 2 [ID_AZIENDA] in method getElencoParticelleForImportByAzienda in FascicoloDAO: "
                        + searchAnagAziendaVO.getIdAzienda());
            stmt.setLong(3, searchAnagAziendaVO.getIdAzienda().longValue());
            SolmrLogger
                .debug(
                    this,
                    "Parameter 3 [ID_AZIENDA] in method getElencoParticelleForImportByAzienda in FascicoloDAO: "
                        + searchAnagAziendaVO.getIdAzienda());
          }
        }
      }
      SolmrLogger.debug(this,
          "Executing getElencoParticelleForImportByAzienda: " + query);

      ResultSet rs = stmt.executeQuery();

      elencoParticelle = new Vector<ParticellaVO>();

      while (rs.next())
      {
        ParticellaVO particellaVO = new ParticellaVO();
        particellaVO.setIdConduzioneParticella(new Long(rs.getLong(1)));
        particellaVO.setIstatComuneParticella(rs.getString("COMUNE"));
        particellaVO.setDescComuneParticella(rs.getString("DESCOM"));
        particellaVO.setSiglaProvinciaParticella(rs.getString("SIGLA_PROVINCIA"));
        particellaVO.setSezione(rs.getString("SEZIONE"));
        particellaVO.setFoglio(new Long(rs.getLong("FOGLIO")));
        if (Validator.isNotEmpty(rs.getString("PARTICELLA")))
        {
          particellaVO.setParticella(new Long(rs.getLong("PARTICELLA")));
        }
        particellaVO.setSubalterno(rs.getString("SUBALTERNO"));
        particellaVO.setSupCatastale(StringUtils.parseSuperficieField(rs
            .getString("SUP_CATASTALE")));
        particellaVO.setSuperficieGrafica(StringUtils.parseSuperficieField(rs
            .getString("SUPERFICIE_GRAFICA")));
        particellaVO.setIdTitoloPossesso(new Long(rs
            .getLong("ID_TITOLO_POSSESSO")));
        particellaVO.setSuperficieCondotta(StringUtils.parseSuperficieField(rs
            .getString("SUPERFICIE_CONDOTTA")));
        particellaVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
        
        
        elencoParticelle.add(particellaVO);
      }

      // Controllo se è stato trovato qualcosa
      if (elencoParticelle.size() == 0)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_NO_PARTICELLE_FOR_IMPORT"));
      }

      SolmrLogger
          .debug(this, "Executed getElencoParticelleForImportByAzienda!");

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "getElencoParticelleForImportByAzienda - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "getElencoParticelleForImportByAzienda - Generic Exception: " + ex);
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
            .error(
                this,
                "getElencoParticelleForImportByAzienda - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getElencoParticelleForImportByAzienda - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
    SolmrLogger
        .debug(this,
            "Invocated method getElencoParticelleForImportByAzienda in FascicoloDAO");
    return elencoParticelle;
  }


  // Metodo per recuperare l'elenco delle unità produttive attive ad una certa
  // data associate ad un'azienda agricola
  public Vector<UteVO> getElencoUteAzienda(Long idAzienda, Date dataValidita)
      throws DataAccessException, NotFoundException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<UteVO> elencoUteAttive = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT U.ID_UTE, U.ID_AZIENDA, U.DENOMINAZIONE, U.INDIRIZZO, "
          + " U.COMUNE, C.DESCOM, U.ID_ZONA_ALTIMETRICA, ZA.DESCRIZIONE, "
          + " U.CAP, U.ID_ATTIVITA_ATECO, AA.DESCRIZIONE, U.NOTE, U.TELEFONO, "
          + " U.FAX, U.ID_UTENTE_AGGIORNAMENTO, U.DATA_INIZIO_ATTIVITA, "
          + " U.DATA_FINE_ATTIVITA, U.CAUSALE_CESSAZIONE, U.DATA_AGGIORNAMENTO, "
          + " U.MOTIVO_MODIFICA, U.ID_ATTIVITA_OTE, AO.DESCRIZIONE, "
          + " P.SIGLA_PROVINCIA "
          + " FROM DB_UTE U, COMUNE C, DB_TIPO_ZONA_ALTIMETRICA ZA, "
          + " DB_TIPO_ATTIVITA_ATECO AA, DB_TIPO_ATTIVITA_OTE AO, "
          + " PROVINCIA P "
          + " WHERE U.ID_AZIENDA = ? "
          + " AND (U.DATA_FINE_ATTIVITA IS NULL OR TRUNC(U.DATA_FINE_ATTIVITA) >=?)"
          + " AND TRUNC(U.DATA_INIZIO_ATTIVITA)<=? "
          + " AND U.COMUNE = C.ISTAT_COMUNE(+) "
          + " AND C.ISTAT_PROVINCIA=P.ISTAT_PROVINCIA(+) "
          + " AND U.ID_ZONA_ALTIMETRICA = ZA.ID_ZONA_ALTIMETRICA(+) "
          + " AND U.ID_ATTIVITA_ATECO = AA.ID_ATTIVITA_ATECO(+) "
          + " AND U.ID_ATTIVITA_OTE = AO.ID_ATTIVITA_OTE(+) ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing delete: " + query);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setTimestamp(2, this.convertDateToTimestamp(dataValidita));
      stmt.setTimestamp(3, this.convertDateToTimestamp(dataValidita));

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        elencoUteAttive = new Vector<UteVO>();
        while (rs.next())
        {
          UteVO uteVO = new UteVO();
          uteVO.setIdUte(new Long(rs.getLong(1)));
          uteVO.setIdAzienda(new Long(rs.getLong(2)));
          uteVO.setDenominazione(rs.getString(3));
          uteVO.setIndirizzo(rs.getString(4));
          uteVO.setIdComune(rs.getString(5));
          uteVO.setComune(rs.getString(6));
          if (Validator.isNotEmpty(rs.getString(7)))
          {
            uteVO.setIdZonaAltimetrica(new Long(rs.getLong(7)));
          }
          uteVO.setZonaAltimetrica(rs.getString(8));
          uteVO.setCap(rs.getString(9));
          uteVO.setCodeAteco(rs.getString(10));
          uteVO.setAteco(rs.getString(11));
          uteVO.setNote(rs.getString(12));
          uteVO.setTelefono(rs.getString(13));
          uteVO.setFax(rs.getString(14));
          uteVO.setUtenteAggiornamento(new Long(rs.getLong(15)));
          uteVO.setDataInizioAttivita(rs.getDate(16));
          if (rs.getDate(17) != null)
          {
            uteVO.setDataFineAttivita(rs.getDate(17));
          }
          uteVO.setCausaleCessazione(rs.getString(18));
          uteVO.setMotivoModifica(rs.getString(19));
          uteVO.setCodeOte(rs.getString(20));
          uteVO.setOte(rs.getString(21));
          uteVO.setProvincia(rs.getString("SIGLA_PROVINCIA"));
          elencoUteAttive.add(uteVO);
        }
      }

      stmt.close();
      rs.close();

      if (elencoUteAttive == null || elencoUteAttive.size() == 0)
      {
        throw new NotFoundException((String) AnagErrors.get("ERR_UTE_ATTIVE"));
      }
    }
    catch (NotFoundException exc)
    {
      SolmrLogger.fatal(this, "getElencoUteAzienda - NotFoundException: "
          + exc.getMessage());
      throw new NotFoundException(exc.getMessage());
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getElencoUteAzienda - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getElencoUteAzienda - Generic Exception: " + ex);
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
            "getElencoUteAzienda - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getElencoUteAzienda - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoUteAttive;
  }


  // Metodo per recuperare l'elenco dei terreni su cui l'azienda ha o ha avuto
  // dei terreni
  public Vector<StringcodeDescription> getListaComuniTerreniByIdAzienda(
      Long idAzienda) throws DataAccessException, SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StringcodeDescription> elencoComuniTerreni = null;
    Vector<String> elencoIstat = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT DISTINCT SP.COMUNE "
          + " FROM            DB_STORICO_PARTICELLA SP, "
          + "                 DB_CONDUZIONE_PARTICELLA CP, "
          + "                 DB_UTE U "
          + " WHERE           SP.DATA_FINE_VALIDITA IS NULL "
          + " AND             SP.ID_PARTICELLA = CP.ID_PARTICELLA "
          + " AND             CP.ID_UTE = U.ID_UTE "
          + " AND             U.ID_AZIENDA = ? "
          + " ORDER BY        SP.COMUNE ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idAzienda.longValue());

      SolmrLogger.debug(this, "Executing getListaComuniTerreniByIdAzienda: "
          + query);

      ResultSet rs = stmt.executeQuery();

      elencoComuniTerreni = new Vector<StringcodeDescription>();
      elencoIstat = new Vector<String>();

      // Recupero i records estratti dalla query
      while (rs.next())
      {
        // StringcodeDescription code = new StringcodeDescription();
        String istatComune = rs.getString(1);
        /*
         * code.setCode(rs.getString(1)); String descrizione = rs.getString(2);
         * if(rs.getString(3).equalsIgnoreCase(SolmrConstants.FLAG_N)) {
         * descrizione += " (" + rs.getString(4) + ")"; }
         * code.setDescription(descrizione); elencoComuniTerreni.add(code);
         */
        elencoIstat.add(istatComune);
      }

      stmt.close();
      rs.close();

      if (elencoIstat.size() == 0)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_NO_COMUNI_TERRENI_AZIENDA"));
      }
      else
      {
        Iterator<String> iteraIstat = elencoIstat.iterator();
        while (iteraIstat.hasNext())
        {
          String query2 = " SELECT DISTINCT C.ISTAT_COMUNE, "
              + "                 C.DESCOM, "
              + "                 C.FLAG_ESTERO, "
              + "                 P.SIGLA_PROVINCIA "
              + " FROM            COMUNE C, " + "                 PROVINCIA P "
              + " WHERE           C.ISTAT_COMUNE = ? "
              + " AND             C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
              + " ORDER BY        C.DESCOM ";

          stmt = conn.prepareStatement(query2);

          stmt.setString(1, (String) iteraIstat.next());

          rs = stmt.executeQuery();

          // Recupero i records estratti dalla query
          while (rs.next())
          {
            StringcodeDescription code = new StringcodeDescription();
            code.setCode(rs.getString(1));
            String descrizione = rs.getString(2);
            if (rs.getString(3).equalsIgnoreCase(SolmrConstants.FLAG_N))
            {
              descrizione += " (" + rs.getString(4) + ")";
            }
            code.setDescription(descrizione);
            elencoComuniTerreni.add(code);
          }

          stmt.close();
          rs.close();
        }
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getListaComuniTerreniByIdAzienda - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (SolmrException se)
    {
      SolmrLogger
          .fatal(this, "getListaComuniTerreniByIdAzienda - SQLException: "
              + se.getMessage());
      throw new SolmrException(se.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getListaComuniTerreniByIdAzienda - Generic Exception: " + ex);
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
                "getListaComuniTerreniByIdAzienda - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getListaComuniTerreniByIdAzienda - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoComuniTerreni;
  }

  // Metodo per recuperare il totale delle superfici condotte attive associate
  // alla particella
  public String getTotaleSupCondotteAttiveByIdParticella(Long idParticella,
      Long idAzienda) throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    String totSuperficiCondotte = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT SUM(CP.SUPERFICIE_CONDOTTA) "
          + " FROM   DB_CONDUZIONE_PARTICELLA CP, " + "        DB_UTE U "
          + " WHERE  CP.ID_PARTICELLA = ? "
          + " AND    CP.DATA_FINE_CONDUZIONE IS NULL "
          + " AND    U.ID_AZIENDA = ? " + " AND    CP.ID_UTE = U.ID_UTE ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,
          "Executing getTotaleSupCondotteAttiveByIdParticella: " + query);

      stmt.setLong(1, idParticella.longValue());
      stmt.setLong(2, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        totSuperficiCondotte = StringUtils.parseSuperficieField((rs
            .getString(1)));
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getTotaleSupCondotteAttiveByIdParticella - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(this,
              "getTotaleSupCondotteAttiveByIdParticella - Generic Exception: "
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
                "getTotaleSupCondotteAttiveByIdParticella - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getTotaleSupCondotteAttiveByIdParticella - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return totSuperficiCondotte;
  }

  // Metodo per recuperare le anomalie relative ad una particella
  public Vector<EsitoControlloParticellaVO> getElencoEsitoControlloParticella(
      Long idConduzioneParticella) throws DataAccessException, SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<EsitoControlloParticellaVO> elencoEsitoControlloParticella = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT   ECP.ID_ESITO_CONTROLLO_PARTICELLA, "
          + "          ECP.ID_CONDUZIONE_PARTICELLA, "
          + "          ECP.ID_CONTROLLO, " + "          ECP.BLOCCANTE, "
          + "          ECP.DESCRIZIONE, " + "          TC.DESCRIZIONE "
          + " FROM     DB_ESITO_CONTROLLO_PARTICELLA ECP, "
          + "          DB_TIPO_CONTROLLO TC "
          + " WHERE    ID_CONDUZIONE_PARTICELLA = ? "
          + " AND      ECP.ID_CONTROLLO = TC.ID_CONTROLLO "
          + " ORDER BY ECP.BLOCCANTE DESC ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing getElencoEsitoControlloParticella: "
          + query);

      stmt.setLong(1, idConduzioneParticella.longValue());

      ResultSet rs = stmt.executeQuery();

      elencoEsitoControlloParticella = new Vector<EsitoControlloParticellaVO>();

      while (rs.next())
      {
        EsitoControlloParticellaVO esitoControlloParticellaVO = new EsitoControlloParticellaVO();
        esitoControlloParticellaVO.setIdEsitoControlloParticella(new Long(rs
            .getLong(1)));
        esitoControlloParticellaVO.setIdConduzioneParticella(new Long(rs
            .getLong(2)));
        esitoControlloParticellaVO.setIdControllo(new Long(rs.getLong(3)));
        esitoControlloParticellaVO.setBloccante(rs.getString(4));
        esitoControlloParticellaVO.setDescrizione(rs.getString(5));
        esitoControlloParticellaVO.setDescrizioneControllo(rs.getString(6));
        elencoEsitoControlloParticella.add(esitoControlloParticellaVO);
      }

      stmt.close();
      rs.close();

      if (elencoEsitoControlloParticella == null
          || elencoEsitoControlloParticella.size() == 0)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_NESSUN_CONTROLLO_PARTICELLA_TROVATO"));
      }

    }
    catch (SolmrException se)
    {
      SolmrLogger.fatal(this,
          "getElencoEsitoControlloParticella - SolmrException: "
              + se.getMessage());
      throw new SolmrException(se.getMessage());
    }

    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getElencoEsitoControlloParticella - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getElencoEsitoControlloParticella - Generic Exception: " + ex);
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
                "getElencoEsitoControlloParticella - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getElencoEsitoControlloParticella - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoEsitoControlloParticella;
  }

  // Metodo che restituisce la data di esecuzione controlli di una determinata
  // azienda agricola
  // in formato dd/mm/yyyy hh/mm/ss
  public String getDataUltimaEsecuzioneControlli(Long idAzienda)
      throws DataAccessException
  {

    String dataEsecuzioneControlli = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT MAX(CP.DATA_ESECUZIONE) "
          + " FROM   DB_CONDUZIONE_PARTICELLA CP, " + "        DB_UTE U "
          + " WHERE  U.ID_AZIENDA = ? " + " AND    CP.ID_UTE = U.ID_UTE ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing getDataUltimaEsecuzioneControlli: "
          + query);

      stmt.setLong(1, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        if (Validator.isNotEmpty(rs.getString(1)))
        {
          dataEsecuzioneControlli = StringUtils.parseDateFieldToEuropeStandard(
              (String) SolmrConstants.get("FULL_DATE_ORACLE_FORMAT"),
              (String) SolmrConstants.get("FULL_DATE_EUROPE_FORMAT"), rs
                  .getString(1));
        }
      }
      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getDataUltimaEsecuzioneControlli - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getDataUltimaEsecuzioneControlli - Generic Exception: " + ex);
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
                "getDataUltimaEsecuzioneControlli - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getDataUltimaEsecuzioneControlli - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return dataEsecuzioneControlli;
  }

  /**
   * Metodo per recuperare l'elenco dei records presenti su
   * DB_UTILIZZO_PARTICELLA partendo dall'id_conduzione_particella e dall'anno
   * utilizzo
   * 
   * @param idConduzioneParticella
   *          Long
   * @param anno
   *          String
   * @return Vector
   * @throws SolmrException
   * @throws DataAccessException
   */
  public Vector<ParticellaUtilizzoVO> findElencoParticellaUtilizzoVO(
      Long idConduzioneParticella, String anno) throws SolmrException,
      DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating method findElencoParticellaUtilizzoVO in FascicoloDAO");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaUtilizzoVO> elencoParticellaUtilizzoVO = null;
    ParticellaUtilizzoVO particellaUtilizzoVO = null;

    try
    {
      SolmrLogger
          .debug(
              this,
              "Creating db_connection in method findElencoParticellaUtilizzoVO in FascicoloDAO");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db_connection in method findElencoParticellaUtilizzoVO in FascicoloDAO with value: "
                  + conn);

      SolmrLogger
          .debug(this,
              "Creating query in method findElencoParticellaUtilizzoVO in FascicoloDAO");
      String query = " SELECT UP.ID_UTILIZZO_PARTICELLA, "
          + "        UP.ID_UTILIZZO, " + "        TU.DESCRIZIONE, "
          + "        UP.ID_CONDUZIONE_PARTICELLA, "
          + "        UP.SUPERFICIE_UTILIZZATA, "
          + "        UP.DATA_AGGIORNAMENTO, "
          + "        UP.ID_UTENTE_AGGIORNAMENTO, " + "        UP.ANNO, "
          + "        UP.NOTE, " + "        UP.ID_UTILIZZO_SECONDARIO, "
          + "        TU2.DESCRIZIONE, "
          + "        UP.SUP_UTILIZZATA_SECONDARIA "
          + " FROM   DB_UTILIZZO_PARTICELLA UP, "
          + "        DB_TIPO_UTILIZZO TU, " + "        DB_TIPO_UTILIZZO TU2 "
          + " WHERE  ANNO = ? " + " AND    UP.ID_CONDUZIONE_PARTICELLA = ? "
          + " AND    UP.ID_UTILIZZO = TU.ID_UTILIZZO "
          + " AND    UP.ID_UTILIZZO_SECONDARIO = TU2.ID_UTILIZZO(+) ";
      SolmrLogger
          .debug(this,
              "Created query in method findElencoParticellaUtilizzoVO in FascicoloDAO");

      stmt = conn.prepareStatement(query);

      stmt.setString(1, anno);
      stmt.setLong(2, idConduzioneParticella.longValue());

      SolmrLogger.debug(this, "Executing findElencoParticellaUtilizzoVO: "
          + query);

      ResultSet rs = stmt.executeQuery();

      elencoParticellaUtilizzoVO = new Vector<ParticellaUtilizzoVO>();
      while (rs.next())
      {
        particellaUtilizzoVO = new ParticellaUtilizzoVO();
        particellaUtilizzoVO.setIdUtilizzoParticella(new Long(rs.getLong(1)));
        particellaUtilizzoVO.setIdTipoUtilizzo(new Long(rs.getLong(2)));
        particellaUtilizzoVO.setTipiUtilizzoAttivi(rs.getString(2));
        particellaUtilizzoVO.setDescTipoUtilizzo(rs.getString(3));
        particellaUtilizzoVO.setIdConduzioneParticella(new Long(rs.getLong(4)));
        particellaUtilizzoVO.setSuperficieUtilizzata(StringUtils
            .parseSuperficieField(rs.getString(5)));
        particellaUtilizzoVO.setDataAggiornamento(rs.getDate(6));
        particellaUtilizzoVO.setIdUtenteAggiornamento(new Long(rs.getLong(7)));
        particellaUtilizzoVO.setAnnoUtilizzo(rs.getString(8));
        particellaUtilizzoVO.setNote(rs.getString(9));
        if (Validator.isNotEmpty(rs.getString(10)))
        {
          particellaUtilizzoVO
              .setIdUtilizzoSecondario(new Long(rs.getLong(10)));
          particellaUtilizzoVO.setDescTipoUtilizzoSecondario(rs.getString(11));
          particellaUtilizzoVO.setSuperficieUtilizzataSecondaria(StringUtils
              .parseSuperficieField(rs.getString(12)));
        }
        elencoParticellaUtilizzoVO.add(particellaUtilizzoVO);
      }

      if (elencoParticellaUtilizzoVO.size() == 0)
      {
        throw new SolmrException((String) AnagErrors.get("ERR_NO_PARTICELLE"));
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "findElencoParticellaUtilizzoVO - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (SolmrException se)
    {
      SolmrLogger.error(this,
          "findElencoParticellaUtilizzoVO - SolmrException: " + se);
      throw new SolmrException(se.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "findElencoParticellaUtilizzoVO - Generic Exception: " + ex);
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
                "findElencoParticellaUtilizzoVO - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "findElencoParticellaUtilizzoVO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this,
        "Invocated method findElencoParticellaUtilizzoVO in FascicoloDAO");
    return elencoParticellaUtilizzoVO;
  }

  /**
   * Metodo per recuperare l'elenco delle ute relative ad un comune
   * 
   * @param istatComune
   *          String
   * @param idAzienda
   *          Long
   * @param isActive
   *          boolean
   * @return Vector
   * @throws DataAccessException
   * @throws SolmrException
   */
  public Vector<Long> getListIdUteByIstatComuneAndIdAzienda(String istatComune,
      Long idAzienda, boolean isActive) throws DataAccessException,
      SolmrException
  {
    SolmrLogger
        .debug(this,
            "Invocating method getListIdUteByIstatComuneAndIdAzienda in FascicoloDAO");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<Long> elencoIdUte = null;

    try
    {
      SolmrLogger
          .debug(
              this,
              "Creating db_connection in method getListIdUteByIstatComuneAndIdAzienda in FascicoloDAO");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db_connection in method getListIdUteByIstatComuneAndIdAzienda in FascicoloDAO with value: "
                  + conn);

      SolmrLogger
          .debug(
              this,
              "Creating query in method getListIdUteByIstatComuneAndIdAzienda in FascicoloDAO");

      String query = " SELECT U.ID_UTE " + " FROM   DB_UTE U "
          + " WHERE  U.COMUNE = ? " + " AND    U.ID_AZIENDA = ? ";

      if (isActive)
      {
        query += " AND    U.DATA_FINE_ATTIVITA IS NULL ";
      }
      SolmrLogger.debug(this,
          "Created query in method getListUteByIstatComune in FascicoloDAO");

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [ISTAT_COMUNE] in method getListIdUteByIstatComuneAndIdAzienda in FascicoloDAO: "
                  + istatComune);
      stmt.setString(1, istatComune);
      SolmrLogger
          .debug(
              this,
              "Value of parameter 2 [ID_AZIENDA] in method getListIdUteByIstatComuneAndIdAzienda in FascicoloDAO: "
                  + idAzienda);
      stmt.setLong(2, idAzienda.longValue());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 3 [IS_ACTIVE] in method getListIdUteByIstatComuneAndIdAzienda in FascicoloDAO: "
                  + isActive);

      SolmrLogger.debug(this,
          "Executing getListIdUteByIstatComuneAndIdAzienda: " + query);

      ResultSet rs = stmt.executeQuery();

      elencoIdUte = new Vector<Long>();
      while (rs.next())
      {
        Long idUte = new Long(rs.getLong(1));
        elencoIdUte.add(idUte);
      }

      if (elencoIdUte.size() == 0)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_NO_UTE_ATTIVE_FOR_COMUNE"));
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "getListIdUteByIstatComuneAndIdAzienda - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (SolmrException se)
    {
      SolmrLogger.error(this,
          "getListIdUteByIstatComuneAndIdAzienda - SolmrException: " + se);
      throw new SolmrException(se.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "getListIdUteByIstatComuneAndIdAzienda - Generic Exception: " + ex);
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
                "getListIdUteByIstatComuneAndIdAzienda - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getListIdUteByIstatComuneAndIdAzienda - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger
        .debug(this,
            "Invocated method getListIdUteByIstatComuneAndIdAzienda in FascicoloDAO");
    return elencoIdUte;
  }

  /**
   * Metodo per recuperare il record dalla tabella DB_STORICO_PARTICELLA a
   * partire dalla chiave primaria
   * 
   * @param idStoricoParticella
   *          Long
   * @return ParticellaVO
   * @throws DataAccessException
   */
  public ParticellaVO getStoricoParticella(Long idStoricoParticella)
      throws DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating getStoricoParticella method in FascicoloDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    ParticellaVO particellaVO = null;

    try
    {
      SolmrLogger
          .debug(this,
              "Creating db-connection in getStoricoParticella method in FascicoloDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in getStoricoParticella method in FascicoloDAO and it values: "
                  + conn + "\n");

      String query = " SELECT /*rule*/ "
          + "        P.SIGLA_PROVINCIA, "
          + "        C1.DESCOM, "
          + "        SP.SEZIONE, "
          + "        SP.FOGLIO, "
          + "        SP.PARTICELLA, "
          + "        SP.SUBALTERNO, "
          + "        SP.SUP_CATASTALE, "
          + "        TZA.DESCRIZIONE, "
          + "        TAA.DESCRIZIONE, "
          + "        TAB.DESCRIZIONE, "
          + "        TAC.DESCRIZIONE, "
          + "        TAD.DESCRIZIONE, "
          + "        TAE.DESCRIZIONE, "
          + "        TCP.DESCRIZIONE, "
          + "        SP.FLAG_CAPTAZIONE_POZZI, "
          + "        SP.FLAG_IRRIGABILE, "
          + "        PARTICELLA.DATA_CREAZIONE, "
          + "        PARTICELLA.DATA_CESSAZIONE, "
          + "        SP.DATA_AGGIORNAMENTO, "
          + "        S.DESCRIZIONE, "
          + "        SP.ID_AREA_A, "
          + "        SP.ID_AREA_B, "
          + "        SP.ID_AREA_C, "
          + "        SP.ID_AREA_D, "
          + "        SP.ID_ZONA_ALTIMETRICA, "
          + "        PARTICELLA.ID_PARTICELLA, "
          + "        SP.DATA_INIZIO_VALIDITA, "
          + "        SP.ID_STORICO_PARTICELLA, "
          + "        C1.ISTAT_COMUNE, "
          + "        SP.ID_CASO_PARTICOLARE, "
          + "        SP.MOTIVO_MODIFICA, "
          + "        SP.ID_FONTE, "
          + "        TF.DESCRIZIONE, "
          + "        TCMP.ID_CAUSALE_MOD_PARTICELLA, "
          + "        TCMP.DESCRIZIONE, "
          + "        SP.ID_DOCUMENTO, "
          + "        TD.DESCRIZIONE, "
          + "        SP.ID_UTENTE_AGGIORNAMENTO, "
          + "        SP.ID_UTENTE_AGGIORNAMENTO, " +
          "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
          "          || ' ' " + 
          "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
          "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
          "         WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
          "          AS DENOMINAZIONE, " +
          "       (SELECT PVU.DENOMINAZIONE " +
          "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
          "        WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
          "          AS DESCRIZIONE_ENTE_APPARTENENZA "
          + " FROM   PROVINCIA P, "
          + "        DB_STORICO_PARTICELLA SP, "
          + "        COMUNE C1, "
          + "        DB_TIPO_ZONA_ALTIMETRICA TZA, "
          + "        DB_TIPO_AREA_A TAA, "
          + "        DB_TIPO_AREA_B TAB, "
          + "        DB_TIPO_AREA_C TAC, "
          + "        DB_TIPO_AREA_D TAD, "
          + "        DB_TIPO_AREA_E TAE, "
          + "        DB_FOGLIO F, "
          + "        DB_TIPO_CASO_PARTICOLARE TCP, "
          + "        DB_PARTICELLA PARTICELLA, "
          + "        DB_SEZIONE S, "
          + "        DB_TIPO_FONTE TF, "
          + "        DB_TIPO_CAUSALE_MOD_PARTICELLA TCMP, "
          + "        DB_TIPO_DOCUMENTO TD "
          //+ "        PAPUA_V_UTENTE_LOGIN PVU "
          + " WHERE  SP.ID_STORICO_PARTICELLA = ? "
          + " AND    PARTICELLA.ID_PARTICELLA = SP.ID_PARTICELLA "
          + " AND    SP.DATA_FINE_VALIDITA IS NULL "
          + " AND    SP.COMUNE = C1.ISTAT_COMUNE(+) "
          + " AND    C1.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "
          + " AND    SP.ID_ZONA_ALTIMETRICA = TZA.ID_ZONA_ALTIMETRICA(+) "
          + " AND    SP.ID_AREA_A = TAA.ID_AREA_A(+) "
          + " AND    SP.ID_AREA_B = TAB.ID_AREA_B(+) "
          + " AND    SP.ID_AREA_C = TAC.ID_AREA_C(+) "
          + " AND    SP.ID_AREA_D = TAD.ID_AREA_D(+) "
          + " AND    nvl(SP.SEZIONE,'-')=nvl(F.SEZIONE(+),'-') "
          + " AND    F.COMUNE(+) = SP.COMUNE "
          + " AND    TAE.ID_AREA_E(+) = F.ID_AREA_E "
          + " AND    SP.FOGLIO = F.FOGLIO(+) "
          + " AND    SP.ID_CASO_PARTICOLARE = TCP.ID_CASO_PARTICOLARE(+) "
          + " AND    SP.SEZIONE = S.SEZIONE(+) "
          + " AND    SP.COMUNE = S.ISTAT_COMUNE(+) "
          + " AND    TF.ID_FONTE(+) = SP.ID_FONTE "
          + " AND    TCMP.ID_CAUSALE_MOD_PARTICELLA(+) = SP.ID_CAUSALE_MOD_PARTICELLA "
          + " AND    TD.ID_DOCUMENTO(+) = SP.ID_DOCUMENTO ";
          //+ " AND    PVU.ID_UTENTE_LOGIN = SP.ID_UTENTE_AGGIORNAMENTO ";

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [ID_STORICO_PARTICELLA] in getStoricoParticella method in FascicoloDAO: "
                  + idStoricoParticella + "\n");
      stmt.setLong(1, idStoricoParticella.longValue());

      SolmrLogger.debug(this, "Executing getStoricoParticella: " + query);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        particellaVO = new ParticellaVO();
        particellaVO.setIdStoricoParticella(idStoricoParticella);
        particellaVO.setSiglaProvinciaParticella(rs.getString(1));
        particellaVO.setDescComuneParticella(rs.getString(2));
        particellaVO.setSezione(rs.getString(3));
        particellaVO.setFoglio(new Long(rs.getLong(4)));
        if (Validator.isNotEmpty(rs.getString(5)))
        {
          particellaVO.setParticella(new Long(rs.getLong(5)));
        }
        else
        {
          particellaVO.setParticellaProvvisoria(true);
        }
        particellaVO.setSubalterno(rs.getString(6));
        particellaVO.setSupCatastale(StringUtils.parseSuperficieField(rs
            .getString(7)));
        particellaVO.setDescrizioneZonaAltimetrica(rs.getString(8));
        particellaVO.setDescrizioneAreaA(rs.getString(9));
        particellaVO.setDescrizioneAreaB(rs.getString(10));
        particellaVO.setDescrizioneAreaC(rs.getString(11));
        particellaVO.setDescrizioneAreaD(rs.getString(12));
        particellaVO.setDescrizioneAreaE(rs.getString(13));
        particellaVO.setDescrizioneCasoParticolare(rs.getString(14));
        if (Validator.isNotEmpty(rs.getString(15)))
        {
          if (rs.getString(15).equalsIgnoreCase(SolmrConstants.FLAG_S))
          {
            particellaVO.setFlagCaptazionePozzi(true);
          }
          else
          {
            particellaVO.setFlagCaptazionePozzi(false);
          }
        }
        else
        {
          particellaVO.setFlagCaptazionePozzi(false);
        }
        if (Validator.isNotEmpty(rs.getString(16)))
        {
          if (rs.getString(16).equalsIgnoreCase(SolmrConstants.FLAG_S))
          {
            particellaVO.setFlagIrrigabile(true);
          }
          else
          {
            particellaVO.setFlagIrrigabile(false);
          }
        }
        else
        {
          particellaVO.setFlagIrrigabile(false);
        }
        particellaVO.setDataCreazione(rs.getDate(17));
        if (Validator.isNotEmpty(rs.getString(18)))
        {
          particellaVO.setDataCessazione(rs.getDate(18));
        }
        particellaVO.setDataAggiornamento(rs.getDate(19));
        particellaVO.setDescrizioneSezione(rs.getString(20));
        if (Validator.isNotEmpty(rs.getString(21)))
        {
          particellaVO.setIdAreaA(new Long(rs.getLong(21)));
          particellaVO.setTipiAreaA(rs.getString(21));
        }
        if (Validator.isNotEmpty(rs.getString(22)))
        {
          particellaVO.setIdAreaB(new Long(rs.getLong(22)));
          particellaVO.setTipiAreaB(rs.getString(22));
        }
        if (Validator.isNotEmpty(rs.getString(23)))
        {
          particellaVO.setIdAreaC(new Long(rs.getLong(23)));
          particellaVO.setTipiAreaC(rs.getString(23));
        }
        if (Validator.isNotEmpty(rs.getString(24)))
        {
          particellaVO.setIdAreaD(new Long(rs.getLong(24)));
          particellaVO.setTipiAreaD(rs.getString(24));
        }
        if (Validator.isNotEmpty(rs.getString(25)))
        {
          particellaVO.setIdZonaAltimetrica(new Long(rs.getLong(25)));
          particellaVO.setTipiZonaAltimetrica(rs.getString(25));
        }
        particellaVO.setIdParticella(new Long(rs.getLong(26)));
        particellaVO.setDataInizioValidita(rs.getDate(27));
        particellaVO.setIdStoricoParticella(new Long(rs.getLong(28)));
        particellaVO.setIstatComuneParticella(rs.getString(29));
        if (Validator.isNotEmpty(rs.getString(30)))
        {
          particellaVO.setIdCasoParticolare(new Long(rs.getLong(30)));
          particellaVO.setTipiCasoParticolare(rs.getString(30));
        }
        particellaVO.setMotivoModifica(rs.getString(31));
        CodeDescription fonteDato = null;
        if (Validator.isNotEmpty(rs.getString(32)))
        {
          fonteDato = new CodeDescription(Integer.decode(rs.getString(32)), rs
              .getString(33));
        }
        particellaVO.setFonteDato(fonteDato);
        CodeDescription causaleModParticella = null;
        if (Validator.isNotEmpty(rs.getString(34)))
        {
          causaleModParticella = new CodeDescription(Integer.decode(rs
              .getString(34)), rs.getString(35));
        }
        particellaVO.setCausaleModParticella(causaleModParticella);
        CodeDescription tipoDocumento = null;
        if (Validator.isNotEmpty(rs.getString(36)))
        {
          tipoDocumento = new CodeDescription(Integer.decode(rs.getString(36)),
              rs.getString(37));
        }
        particellaVO.setTipoDocumento(tipoDocumento);
        particellaVO.setIdUtenteAggiornamento(new Long(rs.getLong(38)));
        particellaVO.setDenominazioneUtenteAggiornamento(rs.getString(39)
            + " - " + rs.getString(40));
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "getStoricoParticella - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .error(this, "getStoricoParticella - Generic Exception: " + ex);
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
            "getStoricoParticella - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getStoricoParticella - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this,
        "Invocated getStoricoParticella method in FascicoloDAO\n");
    return particellaVO;
  }

  /**
   * Metodo che si occupa di cessare tutte le conduzioni attive di un'azienda
   * agricola
   * 
   * @param dataFineConduzione
   * @param idParticella
   * @param idUtente
   * @param idAzienda
   * @throws DataAccessException
   */
  public void cessaConduzioniOfAziendaByParticella(
      java.util.Date dataFineConduzione, Long idParticella, Long idUtente,
      Long idAzienda) throws DataAccessException
  {
    SolmrLogger
        .debug(this,
            "Invocating cessaConduzioniOfAziendaByParticella method in FascicoloDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      SolmrLogger
          .debug(
              this,
              "Creating db-connection in cessaConduzioniOfAziendaByParticella method in FascicoloDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in cessaConduzioniOfAziendaByParticella method in FascicoloDAO and it values: "
                  + conn + "\n");

      String query = " UPDATE DB_CONDUZIONE_PARTICELLA "
          + " SET    DATA_FINE_CONDUZIONE = ?, "
          + "        DATA_AGGIORNAMENTO = SYSDATE, "
          + "        ID_UTENTE_AGGIORNAMENTO = ? "
          + "        WHERE  ID_PARTICELLA = ? "
          + " AND    DATA_FINE_CONDUZIONE IS NULL "
          + " AND    ID_UTE IN (SELECT ID_UTE "
          + "                   FROM   DB_UTE "
          + "                   WHERE  ID_AZIENDA = ? "
          + "                   AND    DATA_FINE_ATTIVITA IS NULL) ";

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [DATA_FINE_CONDUZIONE] in cessaConduzioniOfAziendaByParticella method in FascicoloDAO: "
                  + dataFineConduzione + "\n");
      stmt.setDate(1, new java.sql.Date(dataFineConduzione.getTime()));
      SolmrLogger
          .debug(
              this,
              "Value of parameter 2 [ID_UTENTE_AGGIORNAMENTO] in cessaConduzioniOfAziendaByParticella method in FascicoloDAO: "
                  + idUtente + "\n");
      stmt.setLong(2, idUtente.longValue());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 3 [ID_PARTICELLA] in cessaConduzioniOfAziendaByParticella method in FascicoloDAO: "
                  + idParticella + "\n");
      stmt.setLong(3, idParticella.longValue());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 4 [ID_AZIENDA] in cessaConduzioniOfAziendaByParticella method in FascicoloDAO: "
                  + idAzienda + "\n");
      stmt.setLong(4, idAzienda.longValue());

      SolmrLogger.debug(this,
          "Executing cessaConduzioniOfAziendaByParticella: " + query);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this,
          "Executed cessaConduzioniOfAziendaByParticella in fascicoloDAO");
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "cessaConduzioniOfAziendaByParticella in fascicoloDAO - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "cessaConduzioniOfAziendaByParticella in fascicoloDAO - Generic Exception: "
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
            .error(
                this,
                "cessaConduzioniOfAziendaByParticella in fascicoloDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "cessaConduzioniOfAziendaByParticella in fascicoloDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger
        .debug(this,
            "Invocated cessaConduzioniOfAziendaByParticella method in FascicoloDAO\n");
  }

  /**
   * DA CANCELLARE QUANDO ENTRA TUTTO IL TERRITORIALE NUOVO DA CANCELLARE QUANDO
   * ENTRA TUTTO IL TERRITORIALE NUOVO DA CANCELLARE QUANDO ENTRA TUTTO IL
   * TERRITORIALE NUOVO
   */
  public void cessaConduzioniOfAzienda(java.util.Date dataFineConduzione,
      Long idUtente, Long idAzienda) throws DataAccessException
  {
    SolmrLogger
        .debug(this,
            "Invocating cessaConduzioniOfAziendaByParticella method in FascicoloDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      SolmrLogger
          .debug(
              this,
              "Creating db-connection in cessaConduzioniOfAziendaByParticella method in FascicoloDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in cessaConduzioniOfAziendaByParticella method in FascicoloDAO and it values: "
                  + conn + "\n");

      String query = " UPDATE DB_CONDUZIONE_PARTICELLA "
          + " SET    DATA_FINE_CONDUZIONE = ?, "
          + "        DATA_AGGIORNAMENTO = SYSDATE, "
          + "        ID_UTENTE_AGGIORNAMENTO = ? "
          + " WHERE  DATA_FINE_CONDUZIONE IS NULL "
          + " AND    ID_UTE IN (SELECT ID_UTE "
          + "                   FROM   DB_UTE "
          + "                   WHERE  ID_AZIENDA = ? "
          + "                   AND    DATA_FINE_ATTIVITA IS NULL) ";

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [DATA_FINE_CONDUZIONE] in cessaConduzioniOfAziendaByParticella method in FascicoloDAO: "
                  + dataFineConduzione + "\n");
      stmt.setDate(1, new java.sql.Date(dataFineConduzione.getTime()));
      SolmrLogger
          .debug(
              this,
              "Value of parameter 2 [ID_UTENTE_AGGIORNAMENTO] in cessaConduzioniOfAziendaByParticella method in FascicoloDAO: "
                  + idUtente + "\n");
      stmt.setLong(2, idUtente.longValue());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 4 [ID_AZIENDA] in cessaConduzioniOfAziendaByParticella method in FascicoloDAO: "
                  + idAzienda + "\n");
      stmt.setLong(3, idAzienda.longValue());

      SolmrLogger.debug(this,
          "Executing cessaConduzioniOfAziendaByParticella: " + query);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this,
          "Executed cessaConduzioniOfAziendaByParticella in fascicoloDAO");
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "cessaConduzioniOfAziendaByParticella in fascicoloDAO - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "cessaConduzioniOfAziendaByParticella in fascicoloDAO - Generic Exception: "
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
            .error(
                this,
                "cessaConduzioniOfAziendaByParticella in fascicoloDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "cessaConduzioniOfAziendaByParticella in fascicoloDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger
        .debug(this,
            "Invocated cessaConduzioniOfAziendaByParticella method in FascicoloDAO\n");
  }

  /**
   * Metodo che si occupa di cessare tutte le conduzioni dichiarate di
   * un'azienda agricola
   * 
   * @param dataFineConduzione
   * @param idParticella
   * @param idUtente
   * @param idAzienda
   * @throws DataAccessException
   */
  public void cessaConduzioniDichiarateOfAziendaByParticella(
      java.util.Date dataFineConduzione, Long idParticella, Long idUtente,
      Long idAzienda) throws DataAccessException
  {
    SolmrLogger
        .debug(
            this,
            "Invocating cessaConduzioniDichiarateOfAziendaByParticella method in FascicoloDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      SolmrLogger
          .debug(
              this,
              "Creating db-connection in cessaConduzioniDichiarateOfAziendaByParticella method in FascicoloDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in cessaConduzioniDichiarateOfAziendaByParticella method in FascicoloDAO and it values: "
                  + conn + "\n");

      String query = " UPDATE DB_CONDUZIONE_DICHIARATA "
          + " SET    DATA_FINE_CONDUZIONE = ?, "
          + "        DATA_AGGIORNAMENTO = SYSDATE, "
          + "        ID_UTENTE_AGGIORNAMENTO = ? "
          + " WHERE  ID_PARTICELLA = ? "
          + " AND    DATA_FINE_CONDUZIONE IS NULL "
          + " AND    CODICE_FOTOGRAFIA_TERRENI IN (SELECT CODICE_FOTOGRAFIA_TERRENI "
          + "            	                        FROM   DB_DICHIARAZIONE_CONSISTENZA "
          + "                                      WHERE  ID_AZIENDA = ?) ";

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [DATA_FINE_CONDUZIONE] in cessaConduzioniDichiarateOfAziendaByParticella method in FascicoloDAO: "
                  + dataFineConduzione + "\n");
      stmt.setDate(1, new java.sql.Date(dataFineConduzione.getTime()));
      SolmrLogger
          .debug(
              this,
              "Value of parameter 2 [ID_UTENTE_AGGIORNAMENTO] in cessaConduzioniDichiarateOfAziendaByParticella method in FascicoloDAO: "
                  + idUtente + "\n");
      stmt.setLong(2, idUtente.longValue());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 3 [ID_PARTICELLA] in cessaConduzioniDichiarateOfAziendaByParticella method in FascicoloDAO: "
                  + idParticella + "\n");
      stmt.setLong(3, idParticella.longValue());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 4 [ID_AZIENDA] in cessaConduzioniDichiarateOfAziendaByParticella method in FascicoloDAO: "
                  + idAzienda + "\n");
      stmt.setLong(4, idAzienda.longValue());

      SolmrLogger.debug(this,
          "Executing cessaConduzioniDichiarateOfAziendaByParticella: " + query);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger
          .debug(this,
              "Executed cessaConduzioniDichiarateOfAziendaByParticella in fascicoloDAO");
    }
    catch (SQLException exc)
    {
      SolmrLogger
          .error(
              this,
              "cessaConduzioniDichiarateOfAziendaByParticella in fascicoloDAO - SQLException: "
                  + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .error(
              this,
              "cessaConduzioniDichiarateOfAziendaByParticella in fascicoloDAO - Generic Exception: "
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
            .error(
                this,
                "cessaConduzioniDichiarateOfAziendaByParticella in fascicoloDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "cessaConduzioniDichiarateOfAziendaByParticella in fascicoloDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger
        .debug(
            this,
            "Invocated cessaConduzioniDichiarateOfAziendaByParticella method in FascicoloDAO\n");
  }

  // Metodo per recuperare gli indirizzi deli utilizzi
  public Vector<CodeDescription> getIndirizziTipiUtilizzoAttivi()
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<CodeDescription> elencoIndirizziTipiUtilizzo = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT ID_INDIRIZZO_UTILIZZO, DESCRIZIONE "
          + " FROM DB_TIPO_INDIRIZZO_UTILIZZO " + " ORDER BY DESCRIZIONE ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      ResultSet rs = stmt.executeQuery();

      elencoIndirizziTipiUtilizzo = new Vector<CodeDescription>();
      while (rs.next())
      {
        CodeDescription code = new CodeDescription();
        code.setCode(new Integer(rs.getInt(1)));
        code.setDescription(rs.getString(2));
        elencoIndirizziTipiUtilizzo.add(code);
      }

      stmt.close();
      rs.close();

      if (elencoIndirizziTipiUtilizzo.size() == 0)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_NO_INDIRIZZI_ATTIVI"));
      }

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getIndirizziTipiUtilizzoAttivi - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getIndirizziTipiUtilizzoAttivi - Generic Exception: " + ex);
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
                "getIndirizziTipiUtilizzoAttivi - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getIndirizziTipiUtilizzoAttivi - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoIndirizziTipiUtilizzo;
  }

  


}
