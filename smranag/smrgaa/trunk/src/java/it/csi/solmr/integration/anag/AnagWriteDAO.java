package it.csi.solmr.integration.anag;

import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.UfficioZonaIntermediarioVO;
import it.csi.solmr.dto.anag.AnagAAEPAziendaVO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.DelegaVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.dto.anag.PersonaGiuridicaVO;
import it.csi.solmr.dto.anag.ProcedimentoAziendaVO;
import it.csi.solmr.dto.anag.TipoProcedimentoVO;
import it.csi.solmr.dto.anag.UteVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.DataControlException;
import it.csi.solmr.exception.NotFoundException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.exception.services.ErrorTypes;
import it.csi.solmr.integration.BaseDAO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;
import it.csi.solmr.ws.infoc.Sede;
import it.csi.util.performance.StopWatch;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

// <p>Title: S.O.L.M.R.</p>
// <p>Description: Servizi On-Line per il Mondo Rurale</p>
// <p>Copyright: Copyright (c) 2003</p>
// <p>Company: TOBECONFIG</p>
// @author
// @version 1.0

public class AnagWriteDAO extends BaseDAO
{

  public AnagWriteDAO() throws ResourceAccessException
  {
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public AnagWriteDAO(String refName) throws ResourceAccessException
  {
    super(refName);
  }

  public Long insertAzienda(AnagAziendaVO aaVO, Long idOpr, Date dataAperFasc,
      Date dataChiusFasc) throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_AZIENDA);
      conn = getDatasource().getConnection();

      String insert = "INSERT INTO Db_Azienda " + "            (id_azienda, "
          + " FLAG_OBBLIGO_FASCICOLO, " + " FLAG_AZIENDA_PROVVISORIA, "
          + "ID_AZIENDA_PROVENIENZA," + "data_inizio_validita, " + "ID_OPR, "
          + "DATA_APERTURA_FASCICOLO, " + "DATA_CHIUSURA_FASCICOLO) "
          + "     VALUES (?,'S',?,?,trunc(SYSDATE),?,?,?) ";
      stmt = conn.prepareStatement(insert);

      stmt.setLong(1, primaryKey.longValue());

      /**
       * Le due righe seguenti sono state aggiunte per evitare incongruenze sul
       * DB, in quanto non ha senso inserire l'azienda di subentro e poi mettere
       * a N FLAG_AZIENDA_PROVVISORIA.
       */
      if (aaVO.getIdAziendaSubentro() != null)
        aaVO.setFlagAziendaProvvisoria(true);

      if (aaVO.isFlagAziendaProvvisoria())
      {
        stmt.setString(2, "S");
      }
      else
      {
        stmt.setString(2, "N");
      }
      /*
       * try { stmt.setLong(3, aaVO.getIdAziendaSubentro().longValue()); }
       * catch(Exception e) { stmt.setBigDecimal(3,null); }
       */
      if (aaVO.getIdAziendaProvenienza() != null)
      {
        stmt.setLong(3, aaVO.getIdAziendaProvenienza().longValue());
      }
      else
      {
        if (aaVO.getIdAziendaSubentro() != null)
          stmt.setLong(3, aaVO.getIdAziendaSubentro().longValue());
        else
          stmt.setString(3, null);
      }

      if (idOpr != null)
        stmt.setLong(4, idOpr.longValue()); // ID_OPR
      else
        stmt.setString(4, null); // ID_OPR

      stmt.setTimestamp(5, convertDateToTimestamp(dataAperFasc)); // DATA_APERTURA_FASCICOLO
      stmt.setTimestamp(6, convertDateToTimestamp(dataChiusFasc)); // DATA_CHIUSURA_FASCICOLO

      SolmrLogger.debug(this, "Executing insert: " + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insert.");
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
    return primaryKey;
  }

  public Long insertAnagraficaAzienda(AnagAziendaVO aaVO, long idUtenteAggiornamento)
      throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_ANAG_AZIENDA);
      conn = getDatasource().getConnection();

      String insert = "INSERT INTO Db_Anagrafica_Azienda " +
          " (id_anagrafica_azienda, id_azienda, data_inizio_validita, " +
          "  cuaa, partita_iva, denominazione, " +
          "  id_forma_giuridica, id_attivita_OTE, provincia_competenza, " +
          "  cciaa_provincia_rea, cciaa_numero_rea, sedeleg_comune, " +
          "  sedeleg_indirizzo, sedeleg_cap, mail, " +
          "  sitoweb, note, data_aggiornamento, " +
          "  id_utente_aggiornamento, id_attivita_ATECO, motivo_modifica, " +
          "  cciaa_numero_registro_imprese, cciaa_anno_iscrizione, sedeleg_citta_estero, " +
          "  id_tipologia_azienda, id_ude, id_dimensione_azienda, " +
          "  rls, ulu, telefono, " +
          "  fax, pec, codice_agriturismo, " +
          "  ESONERO_PAGAMENTO_GF," +
          "  INTESTAZIONE_PARTITA_IVA," +
          "  DATA_AGGIORNAMENTO_UMA," +
          "  FLAG_IAP," +
          "  DATA_ISCRIZIONE_REA," +
          "  DATA_CESSAZIONE_REA," +
          "  DATA_ISCRIZIONE_RI, " +
          "  DATA_CESSAZIONE_RI, " +
          "  DATA_INIZIO_ATECO) " +
          "  VALUES (?, ?, trunc(SYSDATE), " + 
          "  ?, ?, ?, " + 
          "  ?, ?, ?, " + 
          "  ?, ?, ?, " + 
          "  ?, ?, ?, " + 
          "  ?, ?, SYSDATE, " +
          "  ?, ?, ?, " +
          "  ?, ?, ?, " +
          "  ?, ?, ?, " +
          "  ?, ?, ?, " +
          "  ?, ?, ?, " +
          "  ?, ?, ?, " +
          "  ?, ?, ?, " +
          "  ?, ?, ?) ";
      
      SolmrLogger.debug(this, "-- insertAnagraficaAzienda ="+insert);
      stmt = conn.prepareStatement(insert);
      
      SolmrLogger.debug(this, "-- id_anagrafica_azienda ="+primaryKey);
      stmt.setLong(1, primaryKey.longValue());
      SolmrLogger.debug(this, "-- id_azienda ="+aaVO.getIdAzienda());
      stmt.setLong(2, aaVO.getIdAzienda().longValue());
      if (aaVO.getCUAA() != null && !aaVO.getCUAA().equals(""))
        stmt.setString(3, aaVO.getCUAA().toUpperCase());
      else
        stmt.setString(3, aaVO.getCUAA());
      stmt.setString(4, aaVO.getPartitaIVA());
      stmt.setString(5, aaVO.getDenominazione().toUpperCase().trim());
      stmt.setLong(6, aaVO.getTipoFormaGiuridica().getCode().longValue());
      if (aaVO.getTipoAttivitaOTE() != null
          && aaVO.getTipoAttivitaOTE().getCode() != null)
        stmt.setLong(7, aaVO.getTipoAttivitaOTE().getCode().longValue());
      else
        stmt.setBigDecimal(7, null);
      stmt.setString(8, aaVO.getProvCompetenza());
      if (aaVO.getCCIAAprovREA() != null && !aaVO.getCCIAAprovREA().equals(""))
      {
        stmt.setString(9, aaVO.getCCIAAprovREA().toUpperCase());
      }
      else
      {
        stmt.setString(9, null);
      }
      if (aaVO.getCCIAAnumeroREA() != null)
        stmt.setLong(10, aaVO.getCCIAAnumeroREA().longValue());
      else
        stmt.setBigDecimal(10, null);
      if (aaVO.getSedelegComune() != null
          && !aaVO.getSedelegComune().equals(""))
      {
        stmt.setString(11, aaVO.getSedelegComune());
      }
      else
      {
        stmt.setString(11, aaVO.getSedelegEstero());
      }
      if (aaVO.getSedelegIndirizzo() != null)
      {
        stmt.setString(12, aaVO.getSedelegIndirizzo().toUpperCase());
      }
      else
      {
        stmt.setString(12, null);
      }
      stmt.setString(13, aaVO.getSedelegCAP());
      stmt.setString(14, aaVO.getMail());
      stmt.setString(15, aaVO.getSitoWEB());
      stmt.setString(16, aaVO.getNote());
      stmt.setLong(17, idUtenteAggiornamento);
      if (aaVO.getTipoAttivitaATECO() != null
          && aaVO.getTipoAttivitaATECO().getCode() != null)
        stmt.setLong(18, aaVO.getTipoAttivitaATECO().getCode().longValue());
      else
        stmt.setBigDecimal(18, null);
      stmt.setString(19, aaVO.getMotivoModifica());
      if (aaVO.getCCIAAnumRegImprese() != null)
      {
        stmt.setString(20, aaVO.getCCIAAnumRegImprese().toUpperCase());
      }
      else
      {
        stmt.setString(20, null);
      }
      stmt.setString(21, aaVO.getCCIAAannoIscrizione());
      // Se la sede legale è all'estero e la città è stata valorizzata, la
      // inserisco convertendola
      // in maiuscolo come richiesto dal dominio
      if (Validator.isNotEmpty(aaVO.getSedelegCittaEstero()))
      {
        stmt.setString(22, aaVO.getSedelegCittaEstero().toUpperCase());
      }
      else
      {
        stmt.setString(22, null);
      }
      if (aaVO.getTipoTipologiaAzienda() != null
          && aaVO.getTipoTipologiaAzienda().getCode() != null)
        stmt.setLong(23, aaVO.getTipoTipologiaAzienda().getCode().longValue());
      else
        stmt.setBigDecimal(23, null);
      if (aaVO.getIdUde() != null)
        stmt.setLong(24, aaVO.getIdUde().longValue());
      else
        stmt.setBigDecimal(24, null);
      if (aaVO.getIdDimensioneAzienda() != null)
        stmt.setLong(25, aaVO.getIdDimensioneAzienda().longValue());
      else
        stmt.setBigDecimal(25, null);
      
      stmt.setBigDecimal(26, aaVO.getRls());
      
      if (aaVO.getUlu()!=null)
        stmt.setBigDecimal(27, aaVO.getUlu());
      else
        stmt.setBigDecimal(27, null);
      
      stmt.setString(28, aaVO.getTelefono());
      stmt.setString(29, aaVO.getFax());
      stmt.setString(30, aaVO.getPec());
      stmt.setString(31, aaVO.getCodiceAgriturismo());
      stmt.setString(32, aaVO.getEsoneroPagamentoGF());
      stmt.setString(33, aaVO.getIntestazionePartitaIva());
      stmt.setTimestamp(34, convertDateToTimestamp(aaVO.getDataAggiornamentoUma()));
      String flagIap = "N";
      if(Validator.isNotEmpty(aaVO.getFlagIap()))
        flagIap = aaVO.getFlagIap();
      stmt.setString(35, flagIap);
      stmt.setTimestamp(36, convertDateToTimestamp(aaVO.getDataIscrizioneRea()));
      stmt.setTimestamp(37, convertDateToTimestamp(aaVO.getDataCessazioneRea()));
      stmt.setTimestamp(38, convertDateToTimestamp(aaVO.getDataIscrizioneRi()));
      stmt.setTimestamp(39, convertDateToTimestamp(aaVO.getDataCessazioneRi()));
      stmt.setTimestamp(40, convertDateToTimestamp(aaVO.getDataInizioAteco()));
      
      SolmrLogger.debug(this, "Executing insert: " + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insert.");
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
    return primaryKey;
  }

  public Long insertSoggetto(String flagFisico) throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_SOGGETTO);
      conn = getDatasource().getConnection();

      String insert = "INSERT INTO Db_Soggetto "
          + "            (id_soggetto, flag_fisico) " + "     VALUES (?,?) ";

      stmt = conn.prepareStatement(insert);
      stmt.setLong(1, primaryKey.longValue());
      stmt.setString(2, flagFisico);

      SolmrLogger.debug(this, "Executing insert: " + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insert.");
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
    return primaryKey;
  }

  public Long insertPersonaFisica(PersonaFisicaVO pfVO, long idUtenteAggiornamento)
      throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_PERSONA_FISICA);
      conn = getDatasource().getConnection();

      String insert = " INSERT INTO DB_Persona_Fisica "
          + "             (id_persona_fisica, id_soggetto, codice_fiscale, "
          + "              cognome, nome, sesso, nascita_comune, nascita_data, "
          + "              res_indirizzo, res_comune, res_cap, res_telefono, "
          + "              res_fax, res_mail, note, data_aggiornamento, "
          + "              id_utente_aggiornamento, dom_indirizzo, "
          + "              dom_comune, dom_cap, nascita_citta_estero, "
          + "              res_citta_estero, ID_TITOLO_STUDIO, "
          + "              ID_INDIRIZZO_STUDIO, DOM_CITTA_ESTERO,DATA_INIZIO_RESIDENZA, "
          + "              FLAG_CF_OK,NUMERO_CELLULARE) "
          + "  VALUES      (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
          + "               SYSDATE, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?,?) ";

      stmt = conn.prepareStatement(insert);
      stmt.setLong(1, primaryKey.longValue());
      if (pfVO.getIdSoggetto() != null)
      {
        stmt.setLong(2, pfVO.getIdSoggetto().longValue());
      }
      else
      {
        stmt.setBigDecimal(2, null);
      }
      stmt.setString(3, pfVO.getCodiceFiscale().toUpperCase());
      stmt.setString(4, pfVO.getCognome().toUpperCase().trim());
      stmt.setString(5, pfVO.getNome().toUpperCase().trim());
      stmt.setString(6, pfVO.getSesso());
      if (pfVO.getNascitaComune() != null
          && !pfVO.getNascitaComune().equals(""))
      {
        stmt.setString(7, pfVO.getNascitaComune());
      }
      else
      {
        stmt.setString(7, pfVO.getNascitaStatoEstero());
      }
      stmt.setDate(8, new java.sql.Date(pfVO.getNascitaData().getTime()));
      if (pfVO.getResIndirizzo() != null)
      {
        stmt.setString(9, pfVO.getResIndirizzo().toUpperCase());
      }
      else
      {
        stmt.setString(9, null);
      }
      if (pfVO.getResComune() != null && !pfVO.getResComune().equals(""))
      {
        stmt.setString(10, pfVO.getResComune());
      }
      else
      {
        stmt.setString(10, pfVO.getStatoEsteroRes());
      }
      stmt.setString(11, pfVO.getResCAP());
      stmt.setString(12, pfVO.getResTelefono());
      stmt.setString(13, pfVO.getResFax());
      stmt.setString(14, pfVO.getResMail());
      stmt.setString(15, pfVO.getNote());
      stmt.setLong(16, idUtenteAggiornamento);
      if (pfVO.getDomIndirizzo() != null)
      {
        stmt.setString(17, pfVO.getDomIndirizzo().toUpperCase());
      }
      else
      {
        stmt.setString(17, null);
      }
      stmt.setString(18, pfVO.getIstatComuneDomicilio());
      stmt.setString(19, pfVO.getDomCAP());
      // Se la citta estera di nascita è valorizzata, la inserisco
      // trasformandola in maiuscolo
      // come richiesto dal dominio
      if (Validator.isNotEmpty(pfVO.getCittaNascita()))
      {
        stmt.setString(20, pfVO.getCittaNascita().toUpperCase());
      }
      else
      {
        stmt.setString(20, null);
      }
      if (Validator.isNotEmpty(pfVO.getCittaResidenza()))
      {
        stmt.setString(21, pfVO.getCittaResidenza().toUpperCase());
      }
      else
      {
        stmt.setString(21, null);
      }
      if (pfVO.getIdTitoloStudio() != null)
      {
        stmt.setLong(22, pfVO.getIdTitoloStudio().longValue());
      }
      else
      {
        stmt.setString(22, null);
      }
      if (pfVO.getIdIndirizzoStudio() != null)
      {
        stmt.setLong(23, pfVO.getIdIndirizzoStudio().longValue());
      }
      else
      {
        stmt.setString(23, null);
      }
      if (Validator.isNotEmpty(pfVO.getDescCittaEsteroDomicilio()))
      {
        stmt.setString(24, pfVO.getDescCittaEsteroDomicilio().toUpperCase());
      }
      else
      {
        stmt.setString(24, null);
      }
      stmt.setString(25, SolmrConstants.FLAG_S);
      // Gestione cellulare
      if (pfVO.getdesNumeroCellulare() != null)
        stmt.setString(26, pfVO.getdesNumeroCellulare());
      else
        stmt.setString(26, null);


      SolmrLogger.debug(this, "Executing insert: " + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insert.");
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
    return primaryKey;
  }

  public Long insertPersonaFisicaForAAEP(PersonaFisicaVO pfVO,
      Long idUtenteAggiornamento) throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_PERSONA_FISICA);
      conn = getDatasource().getConnection();

      String insert = " INSERT INTO DB_PERSONA_FISICA "
          + "      (ID_PERSONA_FISICA, ID_SOGGETTO, CODICE_FISCALE, "
          + "       COGNOME, NOME, SESSO, NASCITA_COMUNE, NASCITA_DATA, "
          + "       DATA_INIZIO_RESIDENZA, RES_INDIRIZZO, RES_COMUNE, "
          + "       NASCITA_CITTA_ESTERO, RES_CITTA_ESTERO, "
          + "       RES_CAP, NOTE, DATA_AGGIORNAMENTO, "
          + "       ID_UTENTE_AGGIORNAMENTO) "
          + "  VALUES      (?, "
          + // ID_PERSONA_FISICA 1
          "?, "
          + // ID_SOGGETTO 2
          "?, "
          + // CODICE_FISCALE 3
          "?, "
          + // COGNOME 4
          "?, "
          + // NOME 5
          "?, "
          + // SESSO 6
          "?, "
          + // NASCITA_COMUNE 7
          "?, "
          + // NASCITA_DATA 8
          "?, "
          + // DATA_INIZIO_RESIDENZA 9
          "?, "
          + // RES_INDIRIZZO 10
          "?, "
          + // RES_COMUNE 11
          "?, "
          + // NASCITA_CITTA_ESTERO 12
          "?, "
          + // RES_CITTA_ESTERO 13
          "?, "
          + // RES_CAP 14
          "'Importazione soggetto da Infocamere - Fonte infocamere del ' || TO_CHAR(SYSDATE,'DD/MM/YYYY'), "
          + // NOTE
          "SYSDATE, " + // DATA_AGGIORNAMENTO
          "?)"; // ID_UTENTE_AGGIORNAMENTO 15

      stmt = conn.prepareStatement(insert);
      stmt.setLong(1, primaryKey.longValue()); // ID_PERSONA_FISICA
      if (pfVO.getIdSoggetto() != null)
      {
        stmt.setLong(2, pfVO.getIdSoggetto().longValue()); // ID_SOGGETTO
      }
      else
      {
        stmt.setBigDecimal(2, null); // ID_SOGGETTO
      }

      if (pfVO.getCodiceFiscale() != null)
        stmt.setString(3, pfVO.getCodiceFiscale().toUpperCase().trim()); // CODICE_FISCALE
      else
        stmt.setString(3, null); // CODICE_FISCALE

      if (pfVO.getCognome() != null)
        stmt.setString(4, pfVO.getCognome().toUpperCase().trim()); // COGNOME
      else
        stmt.setString(4, null); // COGNOME

      if (pfVO.getNome() != null)
        stmt.setString(5, pfVO.getNome().toUpperCase().trim()); // NOME
      else
        stmt.setString(5, null); // NOME
      stmt.setString(6, pfVO.getSesso()); // SESSO
      if (pfVO.getNascitaComune() != null)
        stmt.setString(7, pfVO.getNascitaComune()); // NASCITA_COMUNE
      else
        stmt.setString(7, null); // NASCITA_COMUNE

      if (pfVO.getNascitaData() != null)
        stmt.setDate(8, new java.sql.Date(pfVO.getNascitaData().getTime())); // NASCITA_DATA
      else
        stmt.setDate(8, null); // NASCITA_DATA

      if (pfVO.getDataInizioResidenza() != null)
        stmt.setDate(9, new java.sql.Date(pfVO.getDataInizioResidenza()
            .getTime())); // DATA_INIZIO_RESIDENZA
      else
        stmt.setDate(9, null); // DATA_INIZIO_RESIDENZA

      if (pfVO.getResIndirizzo() != null)
      {
        stmt.setString(10, pfVO.getResIndirizzo().toUpperCase()); // RES_INDIRIZZO
      }
      else
      {
        stmt.setString(10, null); // RES_INDIRIZZO
      }
      if (pfVO.getResComune() != null)
        stmt.setString(11, pfVO.getResComune()); // RES_COMUNE
      else
        stmt.setString(11, null); // RES_COMUNE

      if (pfVO.getNascitaComune() == null
          && pfVO.getNascitaCittaEstero() != null
          && !pfVO.getNascitaCittaEstero().equals(""))
        stmt.setString(12, pfVO.getNascitaCittaEstero()); // NASCITA_CITTA_ESTERO
      else
        stmt.setString(12, null); // NASCITA_CITTA_ESTERO
      if (pfVO.getResComune() == null && pfVO.getResCittaEstero() != null
          && !pfVO.getResCittaEstero().equals(""))
        stmt.setString(13, pfVO.getResCittaEstero()); // RES_CITTA_ESTERO
      else
        stmt.setString(13, null); // RES_CITTA_ESTERO

      stmt.setString(14, pfVO.getResCAP()); // RES_CAP
      stmt.setLong(15, idUtenteAggiornamento.longValue()); // ID_UTENTE_AGGIORNAMENTO

      SolmrLogger.debug(this, "insertPersonaFisicaForAAEP Executing insert: "
          + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "insertPersonaFisicaForAAEP Executed insert.");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "insertPersonaFisicaForAAEP SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "insertPersonaFisicaForAAEP Generic Exception: "
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
                "insertPersonaFisicaForAAEP SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "insertPersonaFisicaForAAEP Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }

  public void updatePersonaFisicaForAAEP(PersonaFisicaVO pfVO,
      Long idUtenteAggiornamento) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String update = "UPDATE DB_PERSONA_FISICA "
          + "   SET COGNOME = ?,"
          + // 1
          "       NOME = ?,"
          + // 2
          "       SESSO = ?,"
          + // 3
          "       NASCITA_COMUNE = ?,"
          + // 4
          "       NASCITA_DATA = ?,"
          + // 5
          "       DATA_INIZIO_RESIDENZA = SYSDATE, "
          + "       RES_INDIRIZZO = ?,"
          + // 6
          "       RES_COMUNE = ?,"
          + // 7
          "       RES_CAP = ?,"
          + // 8
          "       NOTE = 'Importazione soggetto da Infocamere - Fonte infocamere del ' || TO_CHAR(SYSDATE,'DD/MM/YYYY'),"
          + "       DATA_AGGIORNAMENTO = SYSDATE,"
          + "       ID_UTENTE_AGGIORNAMENTO = ?," + // 9
          "       NASCITA_CITTA_ESTERO = ?," + // 10
          "       RES_CITTA_ESTERO = ?  " + // 11
          " WHERE ID_PERSONA_FISICA = ? "; // 12

      stmt = conn.prepareStatement(update);

      if (pfVO.getCognome() != null)
        stmt.setString(1, pfVO.getCognome().toUpperCase().trim()); // COGNOME
      else
        stmt.setString(1, null); // COGNOME

      if (pfVO.getNome() != null)
        stmt.setString(2, pfVO.getNome().toUpperCase().trim()); // NOME
      else
        stmt.setString(2, null); // NOME

      stmt.setString(3, pfVO.getSesso()); // SESSO
      if (pfVO.getNascitaComune() != null)
        stmt.setString(4, pfVO.getNascitaComune()); // NASCITA_COMUNE
      else
        stmt.setString(4, null); // NASCITA_COMUNE

      if (pfVO.getNascitaData() != null)
        stmt.setDate(5, new java.sql.Date(pfVO.getNascitaData().getTime())); // NASCITA_DATA
      else
        stmt.setDate(5, null); // NASCITA_DATA

      if (pfVO.getResIndirizzo() != null)
      {
        stmt.setString(6, pfVO.getResIndirizzo().toUpperCase()); // RES_INDIRIZZO
      }
      else
      {
        stmt.setString(6, null); // RES_INDIRIZZO
      }
      if (pfVO.getResComune() != null){
    	SolmrLogger.debug(this, "--- resComune ="+pfVO.getResComune());  
        stmt.setString(7, pfVO.getResComune()); // RES_COMUNE
      }  
      else
        stmt.setString(7, null); // RES_COMUNE

      stmt.setString(8, pfVO.getResCAP()); // RES_CAP
      stmt.setLong(9, idUtenteAggiornamento.longValue()); // ID_UTENTE_AGGIORNAMENTO

      if (pfVO.getNascitaComune() == null
          && pfVO.getNascitaCittaEstero() != null
          && !pfVO.getNascitaCittaEstero().equals(""))
        stmt.setString(10, pfVO.getNascitaCittaEstero()); // NASCITA_CITTA_ESTERO
      else
        stmt.setString(10, null); // NASCITA_CITTA_ESTERO
      if (pfVO.getResComune() == null && pfVO.getResCittaEstero() != null
          && !pfVO.getResCittaEstero().equals(""))
        stmt.setString(11, pfVO.getResCittaEstero()); // RES_CITTA_ESTERO
      else
        stmt.setString(11, null); // RES_CITTA_ESTERO
      stmt.setLong(12, pfVO.getIdPersonaFisica().longValue()); // ID_PERSONA_FISICA

      SolmrLogger.debug(this, "updatePersonaFisicaForAAEP Executing update: "
          + update);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "updatePersonaFisicaForAAEP Executed update.");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "updatePersonaFisicaForAAEP SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "updatePersonaFisicaForAAEP Generic Exception: "
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
                "updatePersonaFisicaForAAEP SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "updatePersonaFisicaForAAEP Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }
  
  public Long insertPersonaFisicaForAT(PersonaFisicaVO pfVO,
      Long idUtenteAggiornamento) throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_PERSONA_FISICA);
      conn = getDatasource().getConnection();

      String insert = " INSERT INTO DB_PERSONA_FISICA "
          + "      (ID_PERSONA_FISICA, ID_SOGGETTO, CODICE_FISCALE, "
          + "       COGNOME, NOME, SESSO, NASCITA_COMUNE, NASCITA_DATA, "
          + "       DATA_INIZIO_RESIDENZA, RES_INDIRIZZO, RES_COMUNE, "
          + "       RES_CAP, NOTE, DATA_AGGIORNAMENTO, "
          + "       ID_UTENTE_AGGIORNAMENTO, DOM_COMUNE, DOM_INDIRIZZO, DOM_CAP) "
          + "  VALUES      (?, "
          + // ID_PERSONA_FISICA 1
          "?, "
          + // ID_SOGGETTO 2
          "?, "
          + // CODICE_FISCALE 3
          "?, "
          + // COGNOME 4
          "?, "
          + // NOME 5
          "?, "
          + // SESSO 6
          "?, "
          + // NASCITA_COMUNE 7
          "?, "
          + // NASCITA_DATA 8
          "SYSDATE, "
          + // DATA_INIZIO_RESIDENZA
          "?, "
          + // RES_INDIRIZZO 9
          "?, "
          + // RES_COMUNE 10
          "?, "
          + // RES_CAP 11
          "'Importazione soggetto da A.T. - Anagrafe tributaria del ' || TO_CHAR(SYSDATE,'DD/MM/YYYY'), "
          + // NOTE
          "SYSDATE, " 
          + // DATA_AGGIORNAMENTO 
          "?, " +
          // ID_UTENTE_AGGIORNAMENTO 12
          "?, " +
          // DOM_COMUNE 13
          "?, " +
          // DOM_INDIRIZZO 14
          "?)"; // DOM_CAP 15

      stmt = conn.prepareStatement(insert);
      stmt.setLong(1, primaryKey.longValue()); // ID_PERSONA_FISICA
      if (pfVO.getIdSoggetto() != null)
      {
        stmt.setLong(2, pfVO.getIdSoggetto().longValue()); // ID_SOGGETTO
      }
      else
      {
        stmt.setBigDecimal(2, null); // ID_SOGGETTO
      }

      if (pfVO.getCodiceFiscale() != null)
        stmt.setString(3, pfVO.getCodiceFiscale().toUpperCase().trim()); // CODICE_FISCALE
      else
        stmt.setString(3, null); // CODICE_FISCALE

      if (pfVO.getCognome() != null)
        stmt.setString(4, pfVO.getCognome().toUpperCase().trim()); // COGNOME
      else
        stmt.setString(4, null); // COGNOME

      if (pfVO.getNome() != null)
        stmt.setString(5, pfVO.getNome().toUpperCase().trim()); // NOME
      else
        stmt.setString(5, null); // NOME
      
      stmt.setString(6, pfVO.getSesso().trim()); // SESSO
      
      if (pfVO.getNascitaComune() != null)
        stmt.setString(7, pfVO.getNascitaComune()); // NASCITA_COMUNE
      else
        stmt.setString(7, null); // NASCITA_COMUNE

      if (pfVO.getNascitaData() != null)
        stmt.setDate(8, new java.sql.Date(pfVO.getNascitaData().getTime())); // NASCITA_DATA
      else
        stmt.setDate(8, null); // NASCITA_DATA

      if (pfVO.getResIndirizzo() != null)
      {
        stmt.setString(9, pfVO.getResIndirizzo().toUpperCase()); // RES_INDIRIZZO
      }
      else
      {
        stmt.setString(9, null); // RES_INDIRIZZO
      }
      if (pfVO.getResComune() != null)
        stmt.setString(10, pfVO.getResComune()); // RES_COMUNE
      else
        stmt.setString(10, null); // RES_COMUNE

      stmt.setString(11, pfVO.getResCAP()); // RES_CAP
      stmt.setLong(12, idUtenteAggiornamento.longValue()); // ID_UTENTE_AGGIORNAMENTO
      if (pfVO.getDomComune() != null)
        stmt.setString(13, pfVO.getDomComune()); // DOM_COMUNE
      else
        stmt.setString(13, null); // DOM_COMUNE
      if (pfVO.getDomIndirizzo() != null)
      {
        stmt.setString(14, pfVO.getDomIndirizzo().toUpperCase()); // DOM_INDIRIZZO
      }
      else
      {
        stmt.setString(14, null); // DOM_INDIRIZZO
      }
      

      stmt.setString(15, pfVO.getDomCAP()); // DOM_CAP

      SolmrLogger.debug(this, "insertPersonaFisicaForAT Executing insert: "
          + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "insertPersonaFisicaForAT Executed insert.");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "insertPersonaFisicaForAT SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "insertPersonaFisicaForAT Generic Exception: "
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
                "insertPersonaFisicaForAT SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "insertPersonaFisicaForAT Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }

  public Long insertStoricoResidenza(PersonaFisicaVO pfVO,
      long idUtenteAggiornamento) throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      primaryKey = getNextPrimaryKey((String) SolmrConstants
          .get("SEQ_STORICO_RESIDENZA"));
      conn = getDatasource().getConnection();

      String insert = " INSERT INTO DB_STORICO_RESIDENZA "
          + "(ID_STORICO_RESIDENZA, ID_PERSONA_FISICA, DATA_INIZIO_RESIDENZA,"
          + "DATA_FINE_RESIDENZA, INDIRIZZO, COMUNE,"
          + "CAP, CITTA_ESTERO, DATA_STORICIZZAZIONE, ID_UTENTE_STORICIZZAZIONE)"
          + "VALUES(?,?,?,SYSDATE,?,?,?,?,SYSDATE,?)";

      stmt = conn.prepareStatement(insert);
      stmt.setLong(1, primaryKey.longValue()); // ID_STORICO_RESIDENZA
      stmt.setLong(2, pfVO.getIdPersonaFisica().longValue()); // ID_PERSONA_FISICA
      stmt.setDate(3,
          new java.sql.Date(pfVO.getDataInizioResidenza().getTime())); // DATA_INIZIO_RESIDENZA
      if (pfVO.getResIndirizzo() != null)
      {
        stmt.setString(4, pfVO.getResIndirizzo().toUpperCase()); // INDIRIZZO
      }
      else
      {
        stmt.setString(4, null); // INDIRIZZO
      }
      stmt.setString(5, pfVO.getResComune()); // COMUNE
      stmt.setString(6, pfVO.getResCAP()); // CAP
      if (Validator.isNotEmpty(pfVO.getResCittaEstero()))
      {
        stmt.setString(7, pfVO.getResCittaEstero().toUpperCase()); // CITTA_ESTERO
      }
      else
      {
        stmt.setString(7, null);
      }
      stmt.setLong(8, idUtenteAggiornamento); // ID_UTENTE_STORICIZZAZIONE

      SolmrLogger.debug(this, "Executing insertStoricoResidenza: " + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insertStoricoResidenza.");
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
    return primaryKey;
  }

  public void updateRappLegale(PersonaFisicaVO pfVO, long idUtenteAggiornamento)
      throws DataAccessException
  {
    updateRappLegale(pfVO, idUtenteAggiornamento, false);
  }

  /**
   * Metodo che si occupa di effettuare la modifica del rappresentante legale
   * 
   * @param pfVO
   * @param profVO
   * @param storicizza
   * @throws DataAccessException
   */
  public void updateRappLegale(PersonaFisicaVO pfVO, long idUtenteAggiornamento,
      boolean storicizza) throws DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating updateRappLegale method in AnagWriteDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      SolmrLogger
          .debug(this,
              "Creating db-connection in updateRappLegale method in AnagWriteDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in updateRappLegale method in AnagWriteDAO and it values: "
                  + conn + "\n");

      String update = " UPDATE DB_PERSONA_FISICA "
          + " SET    CODICE_FISCALE = ?, " + "        COGNOME = ?, "
          + "        NOME = ?, " + "        SESSO = ?, "
          + "        NASCITA_COMUNE = ?, " + "        NASCITA_DATA = ?, "
          + "        RES_INDIRIZZO = ?, " + "        RES_COMUNE = ?, "
          + "        RES_CAP = ?, " + "        RES_TELEFONO = ?, "
          + "        RES_FAX = ?, " + "        RES_MAIL = ?, "
          + "        NOTE = ?, " + "        DATA_AGGIORNAMENTO = SYSDATE, "
          + "        ID_UTENTE_AGGIORNAMENTO = ?, "
          + "        DOM_INDIRIZZO = ?, " + "        DOM_COMUNE = ?, "
          + "        DOM_CITTA_ESTERO = ?, " + "        DOM_CAP = ?, "
          + "        NASCITA_CITTA_ESTERO = ?, "
          + "        RES_CITTA_ESTERO = ?, " + "        ID_TITOLO_STUDIO = ?, "
          + "        ID_INDIRIZZO_STUDIO = ? , " + "        FLAG_CF_OK = ?, "
          + "        NUMERO_CELLULARE = ? ";
      if (storicizza)
      {
        update += ", DATA_INIZIO_RESIDENZA = SYSDATE ";
      }

      update += " WHERE ID_PERSONA_FISICA = ? ";

      stmt = conn.prepareStatement(update);

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [CODICE_FISCALE] in updateRappLegale method in AnagWriteDAO: "
                  + pfVO.getCodiceFiscale().toUpperCase() + "\n");
      stmt.setString(1, pfVO.getCodiceFiscale().toUpperCase());
      SolmrLogger.debug(this,
          "Value of parameter 2 [COGNOME] in updateRappLegale method in AnagWriteDAO: "
              + pfVO.getCognome().toUpperCase().trim() + "\n");
      stmt.setString(2, pfVO.getCognome().toUpperCase().trim());
      SolmrLogger.debug(this,
          "Value of parameter 3 [NOME] in updateRappLegale method in AnagWriteDAO: "
              + pfVO.getNome().toUpperCase().trim() + "\n");
      stmt.setString(3, pfVO.getNome().toUpperCase().trim());
      SolmrLogger.debug(this,
          "Value of parameter 4 [SESSO] in updateRappLegale method in AnagWriteDAO: "
              + pfVO.getSesso() + "\n");
      stmt.setString(4, pfVO.getSesso());
      if (pfVO.getNascitaComune() != null
          && !pfVO.getNascitaComune().equals(""))
      {
        SolmrLogger
            .debug(
                this,
                "Value of parameter 5 [ISTAT_COMUNE_NASCITA] in updateRappLegale method in AnagWriteDAO: "
                    + pfVO.getNascitaComune() + "\n");
        stmt.setString(5, pfVO.getNascitaComune());
      }
      else
      {
        SolmrLogger
            .debug(
                this,
                "Value of parameter 5 [ISTAT_COMUNE_NASCITA] in updateRappLegale method in AnagWriteDAO: "
                    + pfVO.getNascitaStatoEstero() + "\n");
        stmt.setString(5, pfVO.getNascitaStatoEstero());
      }
      SolmrLogger
          .debug(
              this,
              "Value of parameter 6 [NASCITA_DATA] in updateRappLegale method in AnagWriteDAO: "
                  + new java.sql.Date(pfVO.getNascitaData().getTime()) + "\n");
      stmt.setDate(6, new java.sql.Date(pfVO.getNascitaData().getTime()));
      if (pfVO.getResIndirizzo() != null)
      {
        SolmrLogger
            .debug(
                this,
                "Value of parameter 7 [RES_INDIRIZZO] in updateRappLegale method in AnagWriteDAO: "
                    + pfVO.getResIndirizzo().toUpperCase() + "\n");
        stmt.setString(7, pfVO.getResIndirizzo().toUpperCase());
      }
      else
      {
        SolmrLogger
            .debug(
                this,
                "Value of parameter 7 [RES_INDIRIZZO] in updateRappLegale method in AnagWriteDAO: "
                    + null + "\n");
        stmt.setString(7, null);
      }
      if (pfVO.getResComune() != null && !pfVO.getResComune().equals(""))
      {
        SolmrLogger
            .debug(
                this,
                "Value of parameter 8 [RES_COMUNE] in updateRappLegale method in AnagWriteDAO: "
                    + pfVO.getResComune() + "\n");
        stmt.setString(8, pfVO.getResComune());
      }
      else
      {
        SolmrLogger
            .debug(
                this,
                "Value of parameter 8 [RES_COMUNE] in updateRappLegale method in AnagWriteDAO: "
                    + pfVO.getStatoEsteroRes() + "\n");
        stmt.setString(8, pfVO.getStatoEsteroRes());
      }
      SolmrLogger
          .debug(
              this,
              "Value of parameter 9 [RES_COMUNE] in updateRappLegale method in AnagWriteDAO: "
                  + pfVO.getResCAP() + "\n");
      stmt.setString(9, pfVO.getResCAP());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 10 [RES_TELEFONO] in updateRappLegale method in AnagWriteDAO: "
                  + pfVO.getResTelefono() + "\n");
      stmt.setString(10, pfVO.getResTelefono());
      SolmrLogger.debug(this,
          "Value of parameter 11 [RES_FAX] in updateRappLegale method in AnagWriteDAO: "
              + pfVO.getResFax() + "\n");
      stmt.setString(11, pfVO.getResFax());
      SolmrLogger.debug(this,
          "Value of parameter 12 [RES_MAIL] in updateRappLegale method in AnagWriteDAO: "
              + pfVO.getResMail() + "\n");
      stmt.setString(12, pfVO.getResMail());
      SolmrLogger.debug(this,
          "Value of parameter 13 [NOTE] in updateRappLegale method in AnagWriteDAO: "
              + pfVO.getNote() + "\n");
      stmt.setString(13, pfVO.getNote());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 14 [ID_UTENTE] in updateRappLegale method in AnagWriteDAO: "
                  + idUtenteAggiornamento + "\n");
      stmt.setLong(14, idUtenteAggiornamento);
      if (pfVO.getDomIndirizzo() != null)
      {
        SolmrLogger
            .debug(
                this,
                "Value of parameter 15 [DOM_INDIRIZZO] in updateRappLegale method in AnagWriteDAO: "
                    + pfVO.getDomIndirizzo().toUpperCase() + "\n");
        stmt.setString(15, pfVO.getDomIndirizzo().toUpperCase());
      }
      else
      {
        SolmrLogger
            .debug(
                this,
                "Value of parameter 15 [DOM_INDIRIZZO] in updateRappLegale method in AnagWriteDAO: "
                    + null + "\n");
        stmt.setString(15, null);
      }
      SolmrLogger
          .debug(
              this,
              "Value of parameter 16 [ISTAT_COMUNE_DOMICILIO] in updateRappLegale method in AnagWriteDAO: "
                  + pfVO.getIstatComuneDomicilio() + "\n");
      stmt.setString(16, pfVO.getIstatComuneDomicilio());
      if (Validator.isNotEmpty(pfVO.getDescCittaEsteroDomicilio()))
      {
        SolmrLogger
            .debug(
                this,
                "Value of parameter 17 [DESC_CITTA_ESTERO_DOMICILIO] in updateRappLegale method in AnagWriteDAO: "
                    + pfVO.getDescCittaEsteroDomicilio().toUpperCase() + "\n");
        stmt.setString(17, pfVO.getDescCittaEsteroDomicilio().toUpperCase());
      }
      else
      {
        SolmrLogger
            .debug(
                this,
                "Value of parameter 17 [DESC_CITTA_ESTERO_DOMICILIO] in updateRappLegale method in AnagWriteDAO: "
                    + null + "\n");
        stmt.setString(17, null);
      }
      SolmrLogger.debug(this,
          "Value of parameter 18 [DOM_CAP] in updateRappLegale method in AnagWriteDAO: "
              + pfVO.getDomCAP() + "\n");
      stmt.setString(18, pfVO.getDomCAP());
      // Se la citta estera di nascita è stata valorizzata, la inserisco
      // convertendola in maiuscolo
      // come richiesto dal dominio
      if (Validator.isNotEmpty(pfVO.getCittaNascita()))
      {
        SolmrLogger
            .debug(
                this,
                "Value of parameter 19 [CITTA_NASCITA] in updateRappLegale method in AnagWriteDAO: "
                    + pfVO.getCittaNascita().toUpperCase() + "\n");
        stmt.setString(19, pfVO.getCittaNascita().toUpperCase());
      }
      else
      {
        SolmrLogger
            .debug(
                this,
                "Value of parameter 19 [CITTA_NASCITA] in updateRappLegale method in AnagWriteDAO: "
                    + null + "\n");
        stmt.setString(19, null);
      }
      if (Validator.isNotEmpty(pfVO.getCittaResidenza()))
      {
        SolmrLogger
            .debug(
                this,
                "Value of parameter 20 [CITTA_RESIDENZA] in updateRappLegale method in AnagWriteDAO: "
                    + pfVO.getCittaResidenza().toUpperCase() + "\n");
        stmt.setString(20, pfVO.getCittaResidenza().toUpperCase());
      }
      else
      {
        SolmrLogger
            .debug(
                this,
                "Value of parameter 20 [CITTA_RESIDENZA] in updateRappLegale method in AnagWriteDAO: "
                    + null + "\n");
        stmt.setString(20, null);
      }
      if (pfVO.getIdTitoloStudio() != null)
      {
        SolmrLogger
            .debug(
                this,
                "Value of parameter 21 [ID_TITOLO_STUDIO] in updateRappLegale method in AnagWriteDAO: "
                    + pfVO.getIdTitoloStudio().longValue() + "\n");
        stmt.setLong(21, pfVO.getIdTitoloStudio().longValue());
      }
      else
      {
        SolmrLogger
            .debug(
                this,
                "Value of parameter 21 [ID_TITOLO_STUDIO] in updateRappLegale method in AnagWriteDAO: "
                    + null + "\n");
        stmt.setString(21, null);
      }
      if (pfVO.getIdIndirizzoStudio() != null)
      {
        SolmrLogger
            .debug(
                this,
                "Value of parameter 22 [ID_INDIRIZZO_STUDIO] in updateRappLegale method in AnagWriteDAO: "
                    + pfVO.getIdIndirizzoStudio().longValue() + "\n");
        stmt.setLong(22, pfVO.getIdIndirizzoStudio().longValue());
      }
      else
      {
        SolmrLogger
            .debug(
                this,
                "Value of parameter 22 [ID_INDIRIZZO_STUDIO] in updateRappLegale method in AnagWriteDAO: "
                    + null + "\n");
        stmt.setString(22, null);
      }
      SolmrLogger
          .debug(
              this,
              "Value of parameter 23 [FLAG_CF_OK] in updateRappLegale method in AnagWriteDAO: "
                  + SolmrConstants.FLAG_S + "\n");
      stmt.setString(23, SolmrConstants.FLAG_S);
      SolmrLogger
      .debug(
          this,
          "Value of parameter 24 [NUMERO_CELLULARE] in updateRappLegale method in AnagWriteDAO: "
              + pfVO.getNumeroCellulare() + "\n");
      stmt.setString(24, pfVO.getNumeroCellulare());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 25 [ID_PERSONA_FISICA] in updateRappLegale method in AnagWriteDAO: "
                  + pfVO.getIdPersonaFisica().longValue() + "\n");
      stmt.setLong(25, pfVO.getIdPersonaFisica().longValue());

      SolmrLogger.debug(this, "Executing updateRappLegale: " + update);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed updateRappLegale.");
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "updateRappLegale in AnagWriteDAO - SQLException: " + exc + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "updateRappLegale in AnagWriteDAO - Generic Exception: " + ex + "\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
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
                "updateRappLegale in AnagWriteDAO - SQLException while closing Statement and Connection: "
                    + exc + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "updateRappLegale in AnagWriteDAO - Generic Exception while closing Statement and Connection: "
                    + ex + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this,
        "Invocated updateRappLegale method in AnagWriteDAO\n");
  }

  public void updatePersonaFisica(Long pfPK, PersonaFisicaVO pfVO,
      long idUtenteAggiornamento) throws DataAccessException
  {
    updatePersonaFisica(pfPK, pfVO, idUtenteAggiornamento, false);
  }

  public void updatePersonaFisica(Long pfPK, PersonaFisicaVO pfVO,
      long idUtenteAggiornamento, boolean storicizza) throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "UPDATE Db_Persona_Fisica " + " SET id_soggetto = ?, "
          + "codice_fiscale = ?, " + "cognome = ?, " + "nome = ?, "
          + "sesso = ?, " + "nascita_comune = ?, " + "nascita_data = ?, "
          + "nascita_citta_estero = ?, " + "res_indirizzo = ?, "
          + "res_comune = ?, " + "res_cap = ?, " + "res_telefono = ?, "
          + "res_fax = ?, " + "res_mail = ?, " + "res_citta_estero = ?, "
          + "note = ?, " + "data_aggiornamento = SYSDATE, "
          + "id_utente_aggiornamento = ?, " + "dom_indirizzo = ?, "
          + "dom_comune = ?, " + "dom_cap = ?, " + " ID_TITOLO_STUDIO = ?, "
          + " ID_INDIRIZZO_STUDIO = ?, " + " DOM_CITTA_ESTERO = ? , "
          + " FLAG_CF_OK = ? , " + " NUMERO_CELLULARE = ? ";

      /**
       * Se chiamo l'update per storicizzare devo impostare la data inizio
       * residenza a SYSDATE
       */
      if (storicizza)
        query += ", DATA_INIZIO_RESIDENZA = SYSDATE ";

      query += "WHERE id_persona_fisica = ? ";

      stmt = conn.prepareStatement(query);
      
      int idx=0;

      if (pfVO.getIdSoggetto() != null)
      {
        stmt.setLong(++idx, pfVO.getIdSoggetto().longValue());
      }
      else
      {
        stmt.setBigDecimal(++idx, null);
      }
      if (pfVO.getCodiceFiscale() != null)
      {
        stmt.setString(++idx, pfVO.getCodiceFiscale().toUpperCase());
      }
      else
      {
        stmt.setString(++idx, null);
      }
      stmt.setString(++idx, pfVO.getCognome().toUpperCase().trim());
      stmt.setString(++idx, pfVO.getNome().toUpperCase().trim());
      stmt.setString(++idx, pfVO.getSesso());
      if (pfVO.getNascitaComune() != null)
      {
        stmt.setString(++idx, pfVO.getNascitaComune());
      }
      else
      {
        stmt.setString(++idx, pfVO.getNascitaStatoEstero());
      }
      stmt.setDate(++idx, new java.sql.Date(pfVO.getNascitaData().getTime()));
      // Se la città estera di nascita è stata valorizzata, la inserisco
      // trasformandola in maiuscolo
      // come richiesto dal dominio
      if (Validator.isNotEmpty(pfVO.getNascitaCittaEstero()))
      {
        stmt.setString(++idx, pfVO.getNascitaCittaEstero().toUpperCase());
      }
      else
      {
        stmt.setString(++idx, null);
      }
      if (pfVO.getResIndirizzo() != null)
      {
        stmt.setString(++idx, pfVO.getResIndirizzo().toUpperCase());
      }
      else
      {
        stmt.setString(++idx, null);
      }
      if (pfVO.getResComune() != null)
      {
        stmt.setString(++idx, pfVO.getResComune());
      }
      else
      {
        stmt.setString(++idx, pfVO.getStatoEsteroRes());
      }
      stmt.setString(++idx, pfVO.getResCAP());
      stmt.setString(++idx, pfVO.getResTelefono());
      stmt.setString(++idx, pfVO.getResFax());
      stmt.setString(++idx, pfVO.getResMail());
      if (Validator.isNotEmpty(pfVO.getResCittaEstero()))
      {
        stmt.setString(++idx, pfVO.getResCittaEstero().toUpperCase());
      }
      else
      {
        stmt.setString(++idx, null);
      }
      stmt.setString(++idx, pfVO.getNote());
      stmt.setLong(++idx, idUtenteAggiornamento);
      if (pfVO.getDomIndirizzo() != null)
      {
        stmt.setString(++idx, pfVO.getDomIndirizzo().toUpperCase());
      }
      else
      {
        stmt.setString(++idx, null);
      }
      stmt.setString(++idx, pfVO.getIstatComuneDomicilio());
      stmt.setString(++idx, pfVO.getDomCAP());
      if (pfVO.getIdTitoloStudio() != null)
      {
        stmt.setLong(++idx, pfVO.getIdTitoloStudio().longValue());
      }
      else
      {
        stmt.setString(++idx, null);
      }
      if (pfVO.getIdIndirizzoStudio() != null)
      {
        stmt.setLong(++idx, pfVO.getIdIndirizzoStudio().longValue());
      }
      else
      {
        stmt.setString(++idx, null);
      }
      if (Validator.isNotEmpty(pfVO.getDescCittaEsteroDomicilio()))
      {
        stmt.setString(++idx, pfVO.getDescCittaEsteroDomicilio().toUpperCase());
      }
      else
      {
        stmt.setString(++idx, null);
      }
      stmt.setString(++idx, SolmrConstants.FLAG_S);
      // Setto il cellulare se presente
      if (pfVO.getdesNumeroCellulare() != null)
        stmt.setString(++idx, pfVO.getdesNumeroCellulare());
      else
        stmt.setString(++idx, null);



      stmt.setLong(++idx, pfPK.longValue());

      SolmrLogger.debug(this, "Executing updatePersonaFisica: " + query);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed. updatePersonaFisica");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in updatePersonaFisica: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in updatePersonaFisica: "
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
            "SQLException while closing Statement and Connection in updatePersonaFisica: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in updatePersonaFisica: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  public Long insertUTE(UteVO ute, long idUtenteAggiornamento)
      throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_UTE);
      conn = getDatasource().getConnection();

      String insert = "INSERT INTO Db_UTE "
          + " (id_ute, id_azienda, denominazione, indirizzo, comune, "
          + "  cap, id_attivita_ateco, id_attivita_ote, id_zona_altimetrica, telefono, "
          + "  fax, data_inizio_attivita, data_fine_attivita, causale_cessazione, motivo_modifica, note,"
          + "  id_utente_aggiornamento, data_aggiornamento) "
          + " VALUES (?, ?, ?, ?, ?, " + "  ?, ?, ?, ?, ?, "
          + "  ?, ?, ?, ?, null, ?," + "  ?, SYSDATE) ";
      stmt = conn.prepareStatement(insert);

      stmt.setLong(1, primaryKey.longValue());
      stmt.setLong(2, ute.getIdAzienda().longValue());
      stmt.setString(3, ute.getDenominazione());
      if (ute.getIndirizzo() != null)
      {
        stmt.setString(4, ute.getIndirizzo().toUpperCase());
      }
      else
      {
        stmt.setString(4, null);
      }
      stmt.setString(5, ute.getIstat());
      stmt.setString(6, ute.getCap());
      if (ute.getTipoAttivitaATECO() != null
          && ute.getTipoAttivitaATECO().getCode() != null)
        stmt.setLong(7, ute.getTipoAttivitaATECO().getCode().longValue());
      else
        stmt.setBigDecimal(7, null);
      if (ute.getTipoAttivitaOTE() != null
          && ute.getTipoAttivitaOTE().getCode() != null)
        stmt.setLong(8, ute.getTipoAttivitaOTE().getCode().longValue());
      else
        stmt.setBigDecimal(8, null);

      if (ute.getTipoZonaAltimetrica().getCode() != null)
      {
        stmt.setLong(9, ute.getTipoZonaAltimetrica().getCode().longValue());
      }
      else
      {
        stmt.setString(9, null);
      }
      stmt.setString(10, ute.getTelefono());
      stmt.setString(11, ute.getFax());
      stmt
          .setDate(12, new java.sql.Date(ute.getDataInizioAttivita().getTime()));
      if (ute.getDataFineAttivita() != null)
        stmt
            .setDate(13, new java.sql.Date(ute.getDataFineAttivita().getTime()));
      else
        stmt.setDate(13, null);
      stmt.setString(14, ute.getCausaleCessazione());
      stmt.setString(15, ute.getNote());
      stmt.setLong(16, idUtenteAggiornamento);

      SolmrLogger.debug(this, "Executing insert: " + insert);

      stmt.executeUpdate();

      SolmrLogger.debug(this, "Executed insert.");
    }
    catch (SQLException exc)
    {
      if (exc.getErrorCode() == ((Long) SolmrConstants
          .get("CODE_DUP_KEY_ORACLE")).intValue())
      {
        SolmrLogger
            .fatal(this, "insertUTE - SQLException: CODE_DUP_KEY_ORACLE");
        throw new DataAccessException(AnagErrors.ERRORE_UTE_DUP_KEY);
      }
      else
      {
        SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
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
    return primaryKey;
  }

  public Long insertUTEforAAEP(AnagAziendaVO anagAziendaVO,
      Sede sedeInfocamere, Long idUtenteAggiornamento, Integer idAteco,
      ComuneVO comune) throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_UTE);
      conn = getDatasource().getConnection();

      String insert = "INSERT INTO DB_UTE " + " (ID_UTE, " + " ID_AZIENDA, "
          + " DENOMINAZIONE, " + " INDIRIZZO, " + " COMUNE, " + " CAP, "
          + " ID_ZONA_ALTIMETRICA, " + " ID_ATTIVITA_ATECO, " + " TELEFONO, "
          + " FAX, " + " NOTE, " + " DATA_INIZIO_ATTIVITA, "
          + " DATA_AGGIORNAMENTO, " + " ID_UTENTE_AGGIORNAMENTO, "
          + " TIPO_SEDE) "
          + " VALUES (?, "
          + // ID_UTE 1
          "         ?, "
          + // ID_AZIENDA 2
          "         ?, "
          + // DENOMINAZIONE 3
          "         ?, "
          + // INDIRIZZO 4
          "         ?, "
          + // COMUNE 5
          "         ?, "
          + // CAP 6
          "         ?, "
          + // ID_ZONA_ALTIMETRICA 7
          "         ?, "
          + // ID_ATTIVITA_ATECO 8
          "         ?, "
          + // TELEFONO 9
          "         ?, "
          + // FAX 10
          "'Importazione unità locale da Infocamere - Fonte infocamere del ' || TO_CHAR(SYSDATE,'DD/MM/YYYY'), ";// NOTE,
      if (sedeInfocamere.getDataInizioAttivita().getValue() != null)
        insert += "?, "; // DATA_INIZIO_ATTIVITA 11
      else
        insert += "SYSDATE, "; // DATA_INIZIO_ATTIVITA
      insert += "         SYSDATE, " + // DATA_AGGIORNAMENTO
          "         ?, " + // ID_UTENTE_AGGIORNAMENTO 12
          "         ? " + // TIPO_SEDE 13
          " ) ";
      stmt = conn.prepareStatement(insert);

      String indirizzo = "";

      if (sedeInfocamere.getToponimo().getValue() != null)
        indirizzo += sedeInfocamere.getToponimo().getValue() + " ";
      if (sedeInfocamere.getIndirizzo().getValue() != null)
        indirizzo += sedeInfocamere.getIndirizzo().getValue() + " ";
      if (sedeInfocamere.getNumeroCivico().getValue() != null)
        indirizzo += sedeInfocamere.getNumeroCivico().getValue() + " ";
      if (sedeInfocamere.getFrazioneUL() != null)
        indirizzo += sedeInfocamere.getFrazioneUL();

      SolmrLogger.debug(this, "Executing insert: " + insert);
      SolmrLogger.debug(this, "Parameter 1 primaryKey.longValue(): "+ primaryKey.longValue());
      SolmrLogger.debug(this, "Parameter 2 anagAziendaVO.getIdAzienda(): "+ anagAziendaVO.getIdAzienda());
      SolmrLogger.debug(this, "Parameter 3 sedeInfoc.getDenominazioneUL(): "+ sedeInfocamere.getDenominazioneSede().getValue());
      SolmrLogger.debug(this, "Parameter 4 indirizzo: " + indirizzo);
      SolmrLogger.debug(this, "Parameter 5 : ");
      SolmrLogger.debug(this, "Parameter 6 sedeInfoc.getCapUL(): "+ sedeInfocamere.getCap());
      SolmrLogger.debug(this, "Parameter 7 ");
      SolmrLogger.debug(this, "Parameter 8 idAteco: " + idAteco);
      SolmrLogger.debug(this, "Parameter 9 sedeInfoc.getNumTelef(): "+ sedeInfocamere.getTelefono().getValue());     
      SolmrLogger.debug(this, "Parameter 11 sedeInfoc.getDataAperturaUL(): "+ sedeInfocamere.getDataInizioAttivita().getValue());
      SolmrLogger.debug(this, "Parameter 12 idUtenteAggiornamento: "+ idUtenteAggiornamento);
      SolmrLogger.debug(this,"Parameter 13 sedeInfoc.getCodTipoLocalizzazione(): "+ sedeInfocamere.getCodTipoLocalizzazione());

      stmt.setLong(1, primaryKey.longValue()); // ID_UTE
      stmt.setLong(2, anagAziendaVO.getIdAzienda().longValue()); // ID_AZIENDA
      stmt.setString(3, sedeInfocamere.getDenominazioneSede().getValue()); // DENOMINAZIONE
      stmt.setString(4, indirizzo.trim()); // INDIRIZZO

      if (sedeInfocamere.getCodComune().getValue() != null)
        stmt.setString(5, sedeInfocamere.getCodComune().getValue());// COMUNE
      else
        stmt.setString(5, null);// COMUNE

      stmt.setString(6, sedeInfocamere.getCap().getValue()); // CAP

      if (comune != null && comune.getZonaAlt() != null)
        stmt.setLong(7, comune.getZonaAlt().longValue());// ID_ZONA_ALTIMETRICA
      else
        stmt.setString(7, null);// ID_ZONA_ALTIMETRICA

      if (idAteco != null)
        stmt.setInt(8, idAteco.intValue()); // ID_ATTIVITA_ATECO
      else
        stmt.setString(8, null);
      stmt.setString(9, sedeInfocamere.getTelefono().getValue()); // TELEFONO
      stmt.setString(10, sedeInfocamere.getFax().getValue()); // FAX
      int passo = 0;
      if (sedeInfocamere.getDataInizioAttivita().getValue() != null)
      {
        passo++;
        stmt.setTimestamp(11, convertDateToTimestamp(DateUtils.convert(sedeInfocamere.getDataInizioAttivita().getValue())));
      }
      stmt.setLong(11 + passo, idUtenteAggiornamento.longValue()); // ID_UTENTE_AGGIORNAMENTO

      if ("SE".equals(sedeInfocamere.getCodTipoLocalizzazione()))
        stmt.setString(12 + passo, "1"); // TIPO_SEDE
      if ("SS".equals(sedeInfocamere.getCodTipoLocalizzazione()))
        stmt.setString(12 + passo, "2"); // TIPO_SEDE
      if ("UL".equals(sedeInfocamere.getCodTipoLocalizzazione()))
        stmt.setString(12 + passo, "3"); // TIPO_SEDE

      stmt.executeUpdate();

      SolmrLogger.debug(this, "Executed insert.");
    }
    catch (SQLException exc)
    {
      if (exc.getErrorCode() != ((Long) SolmrConstants
          .get("CODE_DUP_KEY_ORACLE")).intValue())
      {
        SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
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
    
    return primaryKey;
  }

  public Long insertContitolareDate(Long idAzienda, Long idSoggetto,
      Date inizioRuolo) throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_CONTITOLARE);
      conn = getDatasource().getConnection();

      String insert = "INSERT INTO Db_Contitolare "
          + " (id_contitolare, id_soggetto, id_ruolo, "
          + "  id_azienda, data_inizio_ruolo,data_inizio_ruolo_mod) "
          + "     VALUES (?, ?, ?, ?, TRUNC(SYSDATE),?) ";
      stmt = conn.prepareStatement(insert);

      stmt.setLong(1, primaryKey.longValue());
      stmt.setLong(2, idSoggetto.longValue());
      stmt.setLong(3, new Long(SolmrConstants.TIPORUOLO_TITOL_RAPPR_LEG)
          .longValue());
      stmt.setLong(4, idAzienda.longValue());
      stmt.setDate(5, checkDate(inizioRuolo));

      SolmrLogger.debug(this, "Executing insert: " + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insert.");
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
    return primaryKey;
  }

  public Long insertContitolare(Long idAzienda, Long idSoggetto)
      throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_CONTITOLARE);
      conn = getDatasource().getConnection();

      String insert = "INSERT INTO Db_Contitolare "
          + " (id_contitolare, id_soggetto, id_ruolo, "
          + "  id_azienda, data_inizio_ruolo, data_inizio_ruolo_mod) "
          + "     VALUES (?, ?, ?, ?, trunc(SYSDATE), trunc(SYSDATE)) ";
      stmt = conn.prepareStatement(insert);

      stmt.setLong(1, primaryKey.longValue());
      stmt.setLong(2, idSoggetto.longValue());
      stmt.setLong(3, new Long(SolmrConstants.TIPORUOLO_TITOL_RAPPR_LEG)
          .longValue());
      stmt.setLong(4, idAzienda.longValue());

      SolmrLogger.debug(this, "Executing insert: " + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insert.");
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
    return primaryKey;
  }

  public Long insertContitolareRuolo(Long idAzienda, Long idSoggetto,
      Long idRuolo) throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_CONTITOLARE);
      conn = getDatasource().getConnection();

      String insert = "INSERT INTO Db_Contitolare "
          + " (id_contitolare, id_soggetto, id_ruolo, "
          + "  id_azienda, data_inizio_ruolo, data_inizio_ruolo_mod) "
          + "     VALUES (?, ?, ?, ?, trunc(SYSDATE), trunc(SYSDATE)) ";
      stmt = conn.prepareStatement(insert);

      stmt.setLong(1, primaryKey.longValue());
      stmt.setLong(2, idSoggetto.longValue());
      stmt.setLong(3, idRuolo.longValue());
      stmt.setLong(4, idAzienda.longValue());

      SolmrLogger.debug(this, "Executing insert: " + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insert.");
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
    return primaryKey;
  }

  public Long checkIsCUAAAlreadyPresent(String cuaa)
      throws DataAccessException, SolmrException
  {
    Connection conn = null;
    Long idAnagraficaAzienda = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT id_anagrafica_azienda "
          + " FROM db_anagrafica_azienda " + " WHERE cuaa = ? "
          + " and data_fine_validita is null "
          + " and data_cessazione is null "
          + " order by DATA_AGGIORNAMENTO desc";
      stmt = conn.prepareStatement(search);

      stmt.setString(1, cuaa);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      // Anche se non trovo più di un record mi interessa solo il primo
      if (rs.next())
        idAnagraficaAzienda = (new Long(rs.getLong("id_anagrafica_azienda")));

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found " + idAnagraficaAzienda
          + " idAnagraficaAzienda");
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
    return idAnagraficaAzienda;
  }

  public boolean isCUAAAlreadyPresentInsediate(String cuaa)
      throws DataAccessException
  {
    Connection conn = null;
    boolean trovato = false;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT AA.ID_ANAGRAFICA_AZIENDA "
          + "FROM DB_ANAGRAFICA_AZIENDA AA,DB_AZIENDA A "
          + "WHERE AA.CUAA = ? " + "  AND AA.DATA_FINE_VALIDITA IS NULL "
          + "  AND AA.DATA_CESSAZIONE IS NULL "
          + "  AND AA.ID_AZIENDA=A.ID_AZIENDA "
          + "  AND A.FLAG_AZIENDA_PROVVISORIA = 'S'";

      stmt = conn.prepareStatement(search);

      stmt.setString(1, cuaa);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        trovato = true;

      rs.close();
      stmt.close();

      SolmrLogger.debug(this,
          "isCUAAAlreadyPresentInsediate Executed query - trovato is "
              + trovato);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "isCUAAAlreadyPresentInsediate SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(this, "isCUAAAlreadyPresentInsediate Generic Exception: "
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
                "isCUAAAlreadyPresentInsediate SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "isCUAAAlreadyPresentInsediate Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return trovato;
  }

  // Metodo per controllare se esiste già un'azienda agricola attiva con il CUAA
  // indicato dall'utente
  public void checkIsCUAAPresent(String cuaa, Long idAzienda)
      throws DataAccessException, SolmrException
  {
    Collection<String> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT id_anagrafica_azienda "
          + " FROM db_anagrafica_azienda " + " WHERE cuaa = ? "
          + " and id_azienda <> ? " + " and data_fine_validita is null "
          + " and data_cessazione is null ";
      stmt = conn.prepareStatement(search);

      stmt.setString(1, cuaa.toUpperCase());
      stmt.setLong(2, idAzienda.longValue());

      SolmrLogger.debug(this, "Executing query checkIsCUAAPresent: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        result = new Vector<String>();
        while (rs.next())
        {
          result.add(rs.getString(1));
        }
      }
      else
      {
        throw new DataAccessException();
      }

      rs.close();
      stmt.close();

      if (result.size() != 0)
      {
        throw new SolmrException(AnagErrors.CUAA_GIA_ESISTENTE);
      }

      SolmrLogger.debug(this, "Executed query checkIsCUAAPresent - Found "
          + result.size() + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in checkIsCUAAPresent: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this, "ResultSet null in checkIsCUAAPresent");
      throw daexc;
    }
    catch (SolmrException sexc)
    {
      SolmrLogger.fatal(this, "SolmrException in checkIsCUAAPresent: "
          + sexc.getMessage());
      throw sexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in checkIsCUAAPresent: "
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
            "SQLException while closing Statement and Connection in checkIsCUAAPresent: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in checkIsCUAAPresent: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  public Long checkIsPivaAlreadyPresent(String piva)
      throws DataAccessException, SolmrException
  {
    Long idAnagraficaAzienda = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT id_anagrafica_azienda "
          + "  FROM db_anagrafica_azienda " + " WHERE partita_iva = ? "
          + " and data_fine_validita is null "
          + " and data_cessazione is null "
          + " order by DATA_AGGIORNAMENTO desc";
      stmt = conn.prepareStatement(search);

      stmt.setString(1, piva);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      // Anche se non trovo più di un record mi interessa solo il primo
      if (rs.next())
        idAnagraficaAzienda = (new Long(rs.getLong("id_anagrafica_azienda")));

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found " + idAnagraficaAzienda
          + " idAnagraficaAzienda.");
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
    return idAnagraficaAzienda;
  }

  // Metodo per controllare se esiste già una partita IVA in una azienda attiva
  // diversa da quella corrente
  public Long checkIsPivaPresent(String piva, Long idAzienda)
      throws DataAccessException, SolmrException
  {
    Long idAnagraficaAzienda = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT id_anagrafica_azienda "
          + "  FROM db_anagrafica_azienda " + " WHERE partita_iva = ? "
          + " and id_azienda <> ? " + " and data_fine_validita is null "
          + " and data_cessazione is null "
          + " order by DATA_AGGIORNAMENTO desc";
      stmt = conn.prepareStatement(search);

      SolmrLogger.debug(this, "-- piva ="+piva);
      stmt.setString(1, piva);
      SolmrLogger.debug(this, "-- idAzienda ="+idAzienda);
      stmt.setLong(2, idAzienda.longValue());

      SolmrLogger.debug(this, "Executing query checkIsPivaPresent =" + search);

      ResultSet rs = stmt.executeQuery();

      // Anche se non trovo più di un record mi interessa solo il primo
      if (rs.next())
        idAnagraficaAzienda = (new Long(rs.getLong("id_anagrafica_azienda")));

      SolmrLogger.debug(this, "-- idAnagraficaAzienda ="+idAnagraficaAzienda);
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
    return idAnagraficaAzienda;
  }

  /**
   * Questo metodo restituisce l'elenco degli idAzienda legati ad un persona
   * 
   * @param codiceFiscale
   *          String
   * @param idRuolo
   *          Long
   * @param ruoloAttivo
   *          boolean
   * @throws DataAccessException
   * @throws DataControlException
   * @return Vector
   */
  public Vector<Long> serviceGetAziendeByCfPersona(String codiceFiscale,
      Long idRuolo, boolean ruoloAttivo) throws DataAccessException,
      DataControlException
  {

    Vector<Long> result = new Vector<Long>();
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      
       // Per prima cosa controllo se trovo una persona fisica con il codice
       // fiscale passato. Se non la trovo rilancio un'eccezione
       

      String search = "SELECT PF.ID_SOGGETTO " + "FROM DB_PERSONA_FISICA PF "
          + "WHERE PF.CODICE_FISCALE = ?";
      stmt = conn.prepareStatement(search);

      stmt.setString(1, codiceFiscale);

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      // Se non trovo nessun record lancio un'eccezione
      if (!rs.next())
        throw new DataControlException(ErrorTypes.STR_NO_RECORD);

      rs.close();

      search = "SELECT  DISTINCT(AA.ID_AZIENDA) "
          + "FROM DB_PERSONA_FISICA PF, DB_CONTITOLARE C, DB_ANAGRAFICA_AZIENDA AA "
          + "WHERE PF.CODICE_FISCALE = ? "
          + "AND PF.ID_SOGGETTO=C.ID_SOGGETTO "
          + "AND C.ID_AZIENDA=AA.ID_AZIENDA "
          + "AND AA.DATA_FINE_VALIDITA IS NULL "
          + "AND AA.DATA_CESSAZIONE IS NULL ";
      // Se idRuolo è valorizzato aggiungo la condizione id_ruolo = ?
      if (idRuolo != null)
        search += "AND C.ID_RUOLO = ? ";
      if (ruoloAttivo)
        search += "AND C.DATA_FINE_RUOLO IS NULL";

      stmt = conn.prepareStatement(search);

      stmt.setString(1, codiceFiscale);
      if (idRuolo != null)
        stmt.setLong(2, idRuolo.longValue());

      SolmrLogger.debug(this, "Executing query: " + search);

      rs = stmt.executeQuery();

      while (rs.next())
        result.add(new Long(rs.getLong(1)));

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "serviceGetAziendeByCfPersona - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataControlException exc)
    {
      SolmrLogger.debug(this,
          "serviceGetAziendeByCfPersona - DataControlException: "
              + exc.getMessage());
      throw exc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "serviceGetAziendeByCfPersona - Generic Exception: " + ex
              + " with message: " + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
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
                "serviceGetAziendeByCfPersona - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "serviceGetAziendeByCfPersona - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  /*
   * Modifica effettuata da TOBECONFIG il 28/07/2004. Aggiunto il
   * caricamento delle descrizioni sul titolo di studio e sull'indirizzo di
   * studio. Modificata la query e aggiunto il set dei valori nel VO
   */
  public PersonaFisicaVO getPersonaFisicaFromCUAA(String cuaa)
      throws DataAccessException, SolmrException
  {

    PersonaFisicaVO result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
      String search = 
        "SELECT PF.ID_PERSONA_FISICA, " +
        "       PF.ID_SOGGETTO, " +
        "       PF.CODICE_FISCALE, " +
        "       PF.COGNOME, " +
        "       PF.NOME, " +
        "       PF.SESSO, " +
        "       PF.NASCITA_COMUNE, " +
        "       PF.NASCITA_CITTA_ESTERO, " +
        "       PF.NASCITA_DATA, " +
        "       PF.DOM_INDIRIZZO, " +
        "       PF.DOM_CAP, " +
        "       PF.RES_INDIRIZZO, " +
        "       PF.RES_COMUNE, " +
        "       PF.RES_CITTA_ESTERO, " +
        "       PF.RES_CAP, " +
        "       PF.RES_TELEFONO, " +
        "       PF.RES_FAX, " +
        "       PF.RES_MAIL, " +
        "       PF.NUMERO_CELLULARE, " +
        "       PF.NOTE, " +
        "       PF.DATA_AGGIORNAMENTO, " +
        "       PF.ID_UTENTE_AGGIORNAMENTO, " +
        "       C1.DESCOM AS RES_DESCOM, " +
        "       C1.FLAG_ESTERO AS RES_FLAG_ESTERO, " +
        "       P1.SIGLA_PROVINCIA AS RES_SIGLA_PROVINCIA, " +
        "       C2.DESCOM AS NASC_DESCOM, " +
        "       C2.FLAG_ESTERO AS NASC_FLAG_ESTERO, " +
        "       P2.SIGLA_PROVINCIA AS NASC_SIGLA_PROVINCIA, " +
        "       PF.ID_TITOLO_STUDIO, " +
        "       PF.ID_INDIRIZZO_STUDIO, " +
        "       PF.DOM_COMUNE, " +
        "       PF.DOM_CITTA_ESTERO, " +
        "       C3.DESCOM AS DOM_DESCOM, " +
        "       C3.FLAG_ESTERO AS DOM_FLAG_ESTERO, " +
        "       P3.SIGLA_PROVINCIA AS DOM_SIGLA_PROVINCIA, " +
        "       TTS.DESCRIZIONE AS DESC_TITOLO_STUDIO, " +
        "       TIS.DESCRIZIONE AS DESC_INDIRIZZO_STUDIO, " +
        "       TRUNC(DATA_INIZIO_RESIDENZA) AS DATA_INIZIO_RESIDENZA " +
        "FROM   DB_PERSONA_FISICA PF, " +
        "       COMUNE C1, " +
        "       COMUNE C2, " +
        "       COMUNE C3, " +
        "       PROVINCIA P1, " +
        "       PROVINCIA P2, " +
        "       PROVINCIA P3, " +
        "       DB_TIPO_TITOLO_STUDIO TTS, " +
        "       DB_TIPO_INDIRIZZO_STUDIO TIS " +
        "WHERE PF.CODICE_FISCALE = ? " +
        "AND   PF.RES_COMUNE = C1.ISTAT_COMUNE(+) " +
        "AND   PF.NASCITA_COMUNE = C2.ISTAT_COMUNE(+) " +
        "AND   PF.DOM_COMUNE = C3.ISTAT_COMUNE(+) " +
        "AND   C1.ISTAT_PROVINCIA = P1.ISTAT_PROVINCIA(+) " +
        "AND   C2.ISTAT_PROVINCIA = P2.ISTAT_PROVINCIA(+) " +
        "AND   C3.ISTAT_PROVINCIA = P3.ISTAT_PROVINCIA(+) " +
        "AND   TTS.ID_TITOLO_STUDIO(+) = PF.ID_TITOLO_STUDIO " +
        "AND   TIS.ID_INDIRIZZO_STUDIO(+) = PF.ID_INDIRIZZO_STUDIO ";
      stmt = conn.prepareStatement(search);

      stmt.setString(1, cuaa.toUpperCase());

      SolmrLogger.debug(this, "Executing query getPersonaFisicaFromCUAA: "
          + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null && rs.next())
      {
        result = new PersonaFisicaVO();
        result.setCodiceFiscale(cuaa.toUpperCase());
        result.setCognome(rs.getString("COGNOME"));
        result.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
        result.setNascitaComune(rs.getString("NASCITA_COMUNE"));
        result.setIdPersonaFisica(new Long(rs.getLong("ID_PERSONA_FISICA")));
        result.setIdSoggetto(new Long(rs.getLong("ID_SOGGETTO")));
        result.setIdUtenteAggiornamento(new Long(rs
            .getLong("ID_UTENTE_AGGIORNAMENTO")));
        result.setNascitaData(rs.getDate("NASCITA_DATA"));
        if (rs.getDate("NASCITA_DATA") != null)
        {
          result.setStrNascitaData(DateUtils.formatDate(rs
              .getDate("NASCITA_DATA")));
        }
        result.setNome(rs.getString("NOME"));
        result.setNote(rs.getString("NOTE"));
        result.setResCAP(rs.getString("RES_CAP"));
        result.setResComune(rs.getString("RES_COMUNE"));
        result.setResFax(rs.getString("RES_FAX"));
        result.setResIndirizzo(rs.getString("RES_INDIRIZZO"));
        result.setResMail(rs.getString("RES_MAIL"));
        result.setResTelefono(rs.getString("RES_TELEFONO"));
        result.setNumeroCellulare(rs.getString("NUMERO_CELLULARE"));
        result.setSesso(rs.getString("SESSO"));
        result.setDomCAP(rs.getString("DOM_CAP"));
        result.setDomComune(rs.getString("DOM_COMUNE"));
        result.setDomIndirizzo(rs.getString("DOM_INDIRIZZO"));
        result.setDescResComune(rs.getString("RES_DESCOM"));
        result.setFlagEstero(rs.getString("RES_FLAG_ESTERO"));
        result.setResProvincia(rs.getString("RES_SIGLA_PROVINCIA"));
        result.setDescNascitaComune(rs.getString("NASC_DESCOM"));
        result.setNascitaFlagEstero(rs.getString("NASC_FLAG_ESTERO"));
        result.setNascitaProv(rs.getString("NASC_SIGLA_PROVINCIA"));
        result.setNewPersonaFisica(false);
        /* Aggiunta Einaudi 28/07/2004 */
        // Recupero le descrizioni relative a titolo/indirizzo di studio
        result.setDescrizioneIndirizzoStudio(rs
            .getString("DESC_INDIRIZZO_STUDIO"));
        result.setDescrizioneTitoloStudio(rs.getString("DESC_TITOLO_STUDIO"));
        /* Fine aggiunta Einaudi 28/07/2004 */
        if (Validator.isNotEmpty(rs.getDate("DATA_INIZIO_RESIDENZA")))
        {
          result.setDataInizioResidenza(rs.getDate("DATA_INIZIO_RESIDENZA"));
          result.setStrDataInizioResidenza(DateUtils.formatDate(rs
              .getDate("DATA_INIZIO_RESIDENZA")));
        }

        if ("S".equalsIgnoreCase(result.getFlagEstero()))
        {
          // In questo caso il comune contiene le informazioni sullo stato
          // estero
          result.setStatoEsteroRes(result.getDescResComune());
          result.setDescStatoEsteroResidenza(result.getDescResComune());
          result.setResCittaEstero(rs.getString("RES_CITTA_ESTERO"));
          result.setResProvincia(null);
          result.setDescResComune(null);
        }
        if ("S".equalsIgnoreCase(result.getNascitaFlagEstero()))
        {
          // In questo caso il comune contiene le informazioni sullo stato
          // estero
          result.setNascitaStatoEstero(result.getDescNascitaComune());
          result.setNascitaCittaEstero(rs.getString("NASCITA_CITTA_ESTERO"));
          result.setDescNascitaComune(null);
          result.setNascitaProv(null);
        }
        result.setIdTitoloStudio(checkLongNull(rs.getString("ID_TITOLO_STUDIO")));        
        result.setIdIndirizzoStudio(checkLongNull(rs.getString("ID_INDIRIZZO_STUDIO")));
        
        result.setIstatComuneDomicilio(rs.getString("DOM_COMUNE"));
        result.setDescCittaEsteroDomicilio(rs.getString("DOM_CITTA_ESTERO"));
        result.setDomComune(rs.getString("DOM_DESCOM"));
        result.setDomicilioFlagEstero(rs.getString("DOM_FLAG_ESTERO"));
        result.setDomProvincia(rs.getString("DOM_SIGLA_PROVINCIA"));
        if ("S".equalsIgnoreCase(result.getDomicilioFlagEstero()))
        {
          // In questo caso il comune contiene le informazioni sullo stato
          // estero
          result.setDomicilioStatoEstero(result.getDomComune());
          result.setDomComune(null);
          result.setDomProvincia(null);
        }
      }

      rs.close();
      stmt.close();

      if (result == null)
        throw new SolmrException((String) AnagErrors.get("CUAA_INESISTENTE"));

      SolmrLogger.debug(this, "Executed query getPersonaFisicaFromCUAA - Found "
          + result + ".");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException getPersonaFisicaFromCUAA: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (SolmrException nfexc)
    {
      SolmrLogger.fatal(this, "SolmrException in getPersonaFisicaFromCUAA: "
          + nfexc.getMessage());
      throw nfexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in getPersonaFisicaFromCUAA: "
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
                "SQLException while closing Statement and Connection in getPersonaFisicaFromCUAA: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in getPersonaFisicaFromCUAA: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // Metodo per reperire il rappresentante legale di una società a partire
  // dall'id_anagrafica_azienda
  /**
   * @deprected
   * 
   * 
   */
  public PersonaFisicaVO getRappresentanteLegaleFromIdAnagAzienda(
      Long idAnagAzienda) throws DataAccessException, NotFoundException
  {
    PersonaFisicaVO result = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();
      String query = " SELECT ID_PERSONA_FISICA, DB_PERSONA_FISICA.ID_SOGGETTO, CODICE_FISCALE, "
          + "        COGNOME, NOME, SESSO, NASCITA_COMUNE, NASCITA_DATA, "
          + "        DOM_INDIRIZZO, DOM_COMUNE, DOM_CAP, DOM_CITTA_ESTERO, "
          + "        RES_INDIRIZZO, RES_COMUNE, RES_CAP, RES_TELEFONO, "
          + "        RES_FAX, RES_MAIL, RES_CITTA_ESTERO, DB_PERSONA_FISICA.NOTE, "
          + "        DB_PERSONA_FISICA.DATA_AGGIORNAMENTO, "
          + "        DB_PERSONA_FISICA.ID_UTENTE_AGGIORNAMENTO, "
          + "        C1.DESCOM, C1.FLAG_ESTERO, "
          + "        P1.SIGLA_PROVINCIA, C2.DESCOM, C2.FLAG_ESTERO, P2.SIGLA_PROVINCIA, NASCITA_CITTA_ESTERO, "
          + "        C3.DESCOM, C3.FLAG_ESTERO, P3.SIGLA_PROVINCIA, "
          + "        C.DATA_INIZIO_RUOLO, DATA_INIZIO_RESIDENZA, "
          + "        FLAG_CF_OK, "
          + "        DB_TIPO_INDIRIZZO_STUDIO.DESCRIZIONE AS DESC_INDIRIZZO_STUDIO,"
          + "        DB_TIPO_TITOLO_STUDIO.DESCRIZIONE DESC_TITOLO_STUDIO,"
          + "        DB_PERSONA_FISICA.ID_TITOLO_STUDIO,"
          + "        DB_PERSONA_FISICA.ID_INDIRIZZO_STUDIO, "
          + "		 NUMERO_CELLULARE "
          + " FROM   DB_PERSONA_FISICA , "
          + "        DB_ANAGRAFICA_AZIENDA A,DB_CONTITOLARE C, "
          + "        COMUNE C1, COMUNE C2, COMUNE C3, "
          + "        PROVINCIA P1, PROVINCIA P2, PROVINCIA P3, "
          + "        DB_TIPO_INDIRIZZO_STUDIO, DB_TIPO_TITOLO_STUDIO "
          + " WHERE  A.ID_ANAGRAFICA_AZIENDA = ? AND A.ID_AZIENDA = C.ID_AZIENDA AND"
          + "        C.ID_RUOLO = ? AND C.DATA_FINE_RUOLO IS NULL AND "
          + "        C.ID_SOGGETTO = DB_PERSONA_FISICA.ID_SOGGETTO AND"
          + "        DB_PERSONA_FISICA.RES_COMUNE=C1.ISTAT_COMUNE(+) AND "
          + "        DB_PERSONA_FISICA.NASCITA_COMUNE=C2.ISTAT_COMUNE(+) AND  "
          + "        DB_PERSONA_FISICA.DOM_COMUNE = C3.ISTAT_COMUNE(+) AND  "
          + "        C1.ISTAT_PROVINCIA=P1.ISTAT_PROVINCIA(+) AND "
          + "        C2.ISTAT_PROVINCIA=P2.ISTAT_PROVINCIA(+) AND "
          + "        C3.ISTAT_PROVINCIA=P3.ISTAT_PROVINCIA(+) AND "
          + "        DB_PERSONA_FISICA.ID_TITOLO_STUDIO=DB_TIPO_TITOLO_STUDIO.ID_TITOLO_STUDIO(+) AND "
          + "        DB_PERSONA_FISICA.ID_INDIRIZZO_STUDIO=DB_TIPO_INDIRIZZO_STUDIO.ID_INDIRIZZO_STUDIO(+) ";

      stmt = conn.prepareStatement(query);
      stmt.setLong(1, idAnagAzienda.longValue());
      stmt.setString(2, SolmrConstants.TIPORUOLO_TITOL_RAPPR_LEG);

      ResultSet rs = stmt.executeQuery();

      if (rs != null && rs.next())
      {
        result = new PersonaFisicaVO();
        result.setNumeroCellulare(rs.getString("NUMERO_CELLULARE"));
        result.setIdPersonaFisica(new Long(rs.getLong(1)));
        result.setIdSoggetto(new Long(rs.getLong(2)));
        result.setCodiceFiscale(rs.getString(3));
        result.setCognome(rs.getString(4));
        result.setNome(rs.getString(5));
        result.setSesso(rs.getString(6));
        result.setNascitaComune(rs.getString(7));
        result.setNascitaData(rs.getDate(8));
        result.setDomIndirizzo(rs.getString(9));
        result.setIstatComuneDomicilio(rs.getString(10));
        result.setDomCAP(rs.getString(11));
        result.setDescCittaEsteroDomicilio(rs.getString(12));
        result.setResIndirizzo(rs.getString(13));
        result.setResComune(rs.getString(14));
        result.setResCAP(rs.getString(15));
        result.setResTelefono(rs.getString(16));
        result.setResFax(rs.getString(17));
        result.setResMail(rs.getString(18));
        result.setCittaResidenza(rs.getString(19));
        result.setResCittaEstero(rs.getString(19));
        result.setNote(rs.getString(20));
        result.setDataAggiornamento(rs.getDate(21));
        result.setIdUtenteAggiornamento(new Long(rs.getLong(22)));
        result.setDescResComune(rs.getString(23));
        result.setFlagEstero(rs.getString(24));
        result.setResProvincia(rs.getString(25));
        result.setDescNascitaComune(rs.getString(26));
        result.setNascitaFlagEstero(rs.getString(27));
        result.setNascitaProv(rs.getString(28));
        result.setNewPersonaFisica(false);
        if (Validator.isNotEmpty(rs.getString("ID_TITOLO_STUDIO")))
        {
          result.setIdTitoloStudio(new Long(rs.getLong("ID_TITOLO_STUDIO")));
          result.setDescrizioneTitoloStudio(rs.getString("DESC_TITOLO_STUDIO"));
        }
        if (Validator.isNotEmpty(rs.getString("ID_INDIRIZZO_STUDIO")))
        {
          result.setIdIndirizzoStudio(new Long(rs
              .getLong("ID_INDIRIZZO_STUDIO")));
          result.setDescrizioneIndirizzoStudio(rs
              .getString("DESC_INDIRIZZO_STUDIO"));
        }

        if ("S".equalsIgnoreCase(result.getFlagEstero()))
        {
          // In questo caso il comune contiene le informazioni sullo stato
          // estero
          result.setDescStatoEsteroResidenza(result.getDescResComune());
          result.setResProvincia(null);
          result.setDescResComune(null);
        }
        if ("S".equalsIgnoreCase(result.getNascitaFlagEstero()))
        {
          // In questo caso il comune contiene le informazioni sullo stato
          // estero
          result.setNascitaStatoEstero(result.getDescNascitaComune());
          result.setNascitaCittaEstero(rs.getString("NASCITA_CITTA_ESTERO"));
          result.setDescNascitaComune(null);
          result.setNascitaProv(null);
        }
        result.setCittaNascita(rs.getString(29));
        result.setDomicilioFlagEstero(rs.getString(31));
        if ((SolmrConstants.FLAG_S).equalsIgnoreCase(result
            .getDomicilioFlagEstero()))
        {
          result.setDomicilioStatoEstero(rs.getString(30));
          result.setDomProvincia(null);
        }
        else
        {
          result.setDomComune(rs.getString(30));
          result.setDomProvincia(rs.getString(32));
        }
        result.setDataInizioRuolo(rs.getDate(33));
        if (Validator.isNotEmpty(rs.getString(34)))
        {
          result.setDataInizioResidenza(rs.getDate(34));
        }
        result.setFlagCfOk(rs.getString(35));
      }
      else
      {
        if (rs == null)
        {
          throw new DataAccessException();
        }
      }

      if (result == null)
      {
        throw new NotFoundException(AnagErrors.NESSUN_RAPPRESENTANTE_LEGALE);
      }

      result = fillAnagWithProvAndCom(result);

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "SQLException in getRappresentanteLegaleFromIdAnagAzienda: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc)
    {
      SolmrLogger.fatal(this,
          "ResultSet null in getRappresentanteLegaleFromIdAnagAzienda");
      throw daexc;
    }
    catch (NotFoundException nfexc)
    {
      SolmrLogger.fatal(this,
          "NotFoundException in getRappresentanteLegaleFromIdAnagAzienda: "
              + nfexc.getMessage());
      throw nfexc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in getRappresentanteLegaleFromIdAnagAzienda: "
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
  
  public PersonaFisicaVO getRappresentanteLegaleFromIdAnagAziendaAndDichCons(
      long idAnagAzienda, Date dataDichiarazione) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    PersonaFisicaVO personaFisicaVO = null;
    
    try
    {
      SolmrLogger.debug(this,
          "[AnagWriteDAO::getRappresentanteLegaleFromIdAnagAziendaAndDichCons] BEGIN.");
  
      queryBuf = new StringBuffer();
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
            "SELECT PF.ID_PERSONA_FISICA, " +
            "       PF.ID_SOGGETTO, " +
            "       PF.CODICE_FISCALE, " +
            "       PF.COGNOME, " +
            "       PF.NOME, " +
            "       PF.SESSO, " +
            "       PF.NASCITA_COMUNE, " +
            "       PF.NASCITA_DATA, " +
            "       PF.DOM_INDIRIZZO, " +
            "       PF.DOM_COMUNE, " +
            "       PF.DOM_CAP, " +
            "       PF.DOM_CITTA_ESTERO, " +
            "       PF.RES_INDIRIZZO, " +
            "       PF.RES_COMUNE, " +
            "       PF.RES_CAP, " +
            "       PF.RES_TELEFONO, " +
            "       PF.RES_FAX, " +
            "       PF.RES_MAIL, " +
            "       PF.RES_CITTA_ESTERO, " +
            "       PF.NOTE, " +
            "       PF.DATA_AGGIORNAMENTO, " +
            "       PF.ID_UTENTE_AGGIORNAMENTO, " +
            "       C1.DESCOM AS DESCOM_RES, " +
            "       C1.FLAG_ESTERO AS FLAG_ESTERO_RES, " +
            "       P1.SIGLA_PROVINCIA AS SIGLA_PROVINCIA_RES, " +
            "       C2.DESCOM AS DESCOM_NAS, " +
            "       C2.FLAG_ESTERO AS FLAG_ESTERO_NAS, " +
            "       P2.SIGLA_PROVINCIA AS SIGLA_PROVINCIA_NAS, " +
            "       PF.NASCITA_CITTA_ESTERO, " +
            "       C3.DESCOM AS DESCOM_DOM, " +
            "       C3.FLAG_ESTERO AS FLAG_ESTERO_DOM, " +
            "       P3.SIGLA_PROVINCIA AS SIGLA_PROVINCIA_DOM, " +
            "       C.DATA_INIZIO_RUOLO, " +
            "       PF.DATA_INIZIO_RESIDENZA, " +
            "       PF.FLAG_CF_OK, " +
            "       TIS.DESCRIZIONE AS DESC_INDIRIZZO_STUDIO," +
            "       TTS.DESCRIZIONE DESC_TITOLO_STUDIO," +
            "       PF.ID_TITOLO_STUDIO," +
            "       PF.ID_INDIRIZZO_STUDIO, " +
            "       PF.NUMERO_CELLULARE " +
            "FROM   DB_PERSONA_FISICA PF, " +
            "       DB_ANAGRAFICA_AZIENDA A," +
            "       DB_CONTITOLARE C, " +
            "       COMUNE C1, " +
            "       COMUNE C2, " +
            "       COMUNE C3, " +
            "       PROVINCIA P1, " +
            "       PROVINCIA P2, " +
            "       PROVINCIA P3, " +
            "       DB_TIPO_INDIRIZZO_STUDIO TIS, " +
            "       DB_TIPO_TITOLO_STUDIO TTS " +
            "WHERE  A.ID_ANAGRAFICA_AZIENDA = ? " +
            "AND    A.ID_AZIENDA = C.ID_AZIENDA " +
            "AND    C.ID_RUOLO = ? ");
      if(dataDichiarazione !=null)
      {
        queryBuf.append("" +
            "  AND  C.DATA_INIZIO_RUOLO <= ? " +
            "  AND  NVL(C.DATA_FINE_RUOLO, ?) >= ? ");
      }
      else
      {
        queryBuf.append("" +
        		"AND    C.DATA_FINE_RUOLO IS NULL ");
      }
      
      queryBuf.append("" +    
            "AND    C.ID_SOGGETTO = PF.ID_SOGGETTO " +
            "AND    PF.RES_COMUNE = C1.ISTAT_COMUNE(+) " +
            "AND    PF.NASCITA_COMUNE = C2.ISTAT_COMUNE(+) " +
            "AND    PF.DOM_COMUNE = C3.ISTAT_COMUNE(+) " +
            "AND    C1.ISTAT_PROVINCIA = P1.ISTAT_PROVINCIA(+) " +
            "AND    C2.ISTAT_PROVINCIA = P2.ISTAT_PROVINCIA(+) " +
            "AND    C3.ISTAT_PROVINCIA = P3.ISTAT_PROVINCIA(+) " +
            "AND    PF.ID_TITOLO_STUDIO = TTS.ID_TITOLO_STUDIO(+) " +
            "AND    PF.ID_INDIRIZZO_STUDIO = TIS.ID_INDIRIZZO_STUDIO(+) ");
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AnagWriteDAO::getRappresentanteLegaleFromIdAnagAziendaAndDichCons] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAnagAzienda);
      stmt.setString(++indice, SolmrConstants.TIPORUOLO_TITOL_RAPPR_LEG);
      if(dataDichiarazione != null)
      {
        stmt.setTimestamp(++indice, convertDateToTimestamp(dataDichiarazione));
        stmt.setDate(++indice, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
        stmt.setTimestamp(++indice, convertDateToTimestamp(dataDichiarazione));
      }
      
      ResultSet rs = stmt.executeQuery();
      if (rs != null && rs.next())
      {
        personaFisicaVO = new PersonaFisicaVO();
        personaFisicaVO.setNumeroCellulare(rs.getString("NUMERO_CELLULARE"));
        personaFisicaVO.setIdPersonaFisica(new Long(rs.getLong("ID_PERSONA_FISICA")));
        personaFisicaVO.setIdSoggetto(new Long(rs.getLong("ID_SOGGETTO")));
        personaFisicaVO.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
        personaFisicaVO.setCognome(rs.getString("COGNOME"));
        personaFisicaVO.setNome(rs.getString("NOME"));
        personaFisicaVO.setSesso(rs.getString("SESSO"));
        personaFisicaVO.setNascitaComune(rs.getString("NASCITA_COMUNE"));
        personaFisicaVO.setNascitaData(rs.getDate("NASCITA_DATA"));
        personaFisicaVO.setDomIndirizzo(rs.getString("DOM_INDIRIZZO"));
        personaFisicaVO.setIstatComuneDomicilio(rs.getString("DOM_COMUNE"));
        personaFisicaVO.setDomCAP(rs.getString("DOM_CAP"));
        personaFisicaVO.setDescCittaEsteroDomicilio(rs.getString("DOM_CITTA_ESTERO"));
        personaFisicaVO.setResIndirizzo(rs.getString("RES_INDIRIZZO"));
        personaFisicaVO.setResComune(rs.getString("RES_COMUNE"));
        personaFisicaVO.setResCAP(rs.getString("RES_CAP"));
        personaFisicaVO.setResTelefono(rs.getString("RES_TELEFONO"));
        personaFisicaVO.setResFax(rs.getString("RES_FAX"));
        personaFisicaVO.setResMail(rs.getString("RES_MAIL"));
        personaFisicaVO.setCittaResidenza(rs.getString("RES_CITTA_ESTERO"));
        personaFisicaVO.setResCittaEstero(rs.getString("RES_CITTA_ESTERO"));
        personaFisicaVO.setNote(rs.getString("NOTE"));
        personaFisicaVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
        personaFisicaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        personaFisicaVO.setDescResComune(rs.getString("DESCOM_RES"));
        personaFisicaVO.setFlagEstero(rs.getString("FLAG_ESTERO_RES"));
        personaFisicaVO.setResProvincia(rs.getString("SIGLA_PROVINCIA_RES"));
        personaFisicaVO.setDescNascitaComune(rs.getString("DESCOM_NAS"));
        personaFisicaVO.setNascitaFlagEstero(rs.getString("FLAG_ESTERO_NAS"));
        personaFisicaVO.setNascitaProv(rs.getString("SIGLA_PROVINCIA_NAS"));
        personaFisicaVO.setNewPersonaFisica(false);
        if (Validator.isNotEmpty(rs.getString("ID_TITOLO_STUDIO")))
        {
          personaFisicaVO.setIdTitoloStudio(new Long(rs.getLong("ID_TITOLO_STUDIO")));
          personaFisicaVO.setDescrizioneTitoloStudio(rs.getString("DESC_TITOLO_STUDIO"));
        }
        if (Validator.isNotEmpty(rs.getString("ID_INDIRIZZO_STUDIO")))
        {
          personaFisicaVO.setIdIndirizzoStudio(new Long(rs
              .getLong("ID_INDIRIZZO_STUDIO")));
          personaFisicaVO.setDescrizioneIndirizzoStudio(rs
              .getString("DESC_INDIRIZZO_STUDIO"));
        }

        if ("S".equalsIgnoreCase(personaFisicaVO.getFlagEstero()))
        {
          // In questo caso il comune contiene le informazioni sullo stato
          // estero
          personaFisicaVO.setDescStatoEsteroResidenza(personaFisicaVO.getDescResComune());
          personaFisicaVO.setResProvincia(null);
          personaFisicaVO.setDescResComune(null);
        }
        if ("S".equalsIgnoreCase(personaFisicaVO.getNascitaFlagEstero()))
        {
          // In questo caso il comune contiene le informazioni sullo stato
          // estero
          personaFisicaVO.setNascitaStatoEstero(personaFisicaVO.getDescNascitaComune());
          personaFisicaVO.setNascitaCittaEstero(rs.getString("NASCITA_CITTA_ESTERO"));
          personaFisicaVO.setDescNascitaComune(null);
          personaFisicaVO.setNascitaProv(null);
        }
        personaFisicaVO.setCittaNascita(rs.getString("NASCITA_CITTA_ESTERO"));
        personaFisicaVO.setDomicilioFlagEstero(rs.getString("FLAG_ESTERO_DOM"));
        if ((SolmrConstants.FLAG_S).equalsIgnoreCase(personaFisicaVO
            .getDomicilioFlagEstero()))
        {
          personaFisicaVO.setDomicilioStatoEstero(rs.getString("DESCOM_DOM"));
          personaFisicaVO.setDomProvincia(null);
        }
        else
        {
          personaFisicaVO.setDomComune(rs.getString("DESCOM_DOM"));
          personaFisicaVO.setDomProvincia(rs.getString("SIGLA_PROVINCIA_DOM"));
        }
        personaFisicaVO.setDataInizioRuolo(rs.getDate("DATA_INIZIO_RUOLO"));
        if (Validator.isNotEmpty(rs.getString("DATA_INIZIO_RESIDENZA")))
        {
          personaFisicaVO.setDataInizioResidenza(rs.getDate("DATA_INIZIO_RESIDENZA"));
        }
        personaFisicaVO.setFlagCfOk(rs.getString("FLAG_CF_OK"));
      }
      
      return personaFisicaVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("personaFisicaVO", personaFisicaVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAnagAzienda", idAnagAzienda),
        new Parametro("dataDichiarazione", dataDichiarazione) };
  
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[AnagWriteDAO::getRappresentanteLegaleFromIdAnagAziendaAndDichCons] ", t,
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
          "[AnagWriteDAO::getRappresentanteLegaleFromIdAnagAziendaAndDichCons] END.");
    }
  }
  
  /**
   * ritorna tutti i soggetti dell'azienda senza il rappresentante legale...
   * 
   * 
   * 
   * @param idAnagAzienda
   * @param dataDichiarazione
   * @return
   * @throws DataAccessException
   */
  public Vector<PersonaFisicaVO> getVAltriSoggettiFromIdAnagAziendaAndDichCons(
      long idAnagAzienda, Date dataDichiarazione) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<PersonaFisicaVO> vPersonaFisicaVO = null;
    PersonaFisicaVO personaFisicaVO = null;
    
    try
    {
      SolmrLogger.debug(this,
          "[AnagWriteDAO::getVAltriSoggettiFromIdAnagAziendaAndDichCons] BEGIN.");
  
      queryBuf = new StringBuffer();
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
            "SELECT PF.ID_PERSONA_FISICA, " +
            "       PF.ID_SOGGETTO, " +
            "       PF.CODICE_FISCALE, " +
            "       PF.COGNOME, " +
            "       PF.NOME, " +
            "       PF.SESSO, " +
            "       PF.NASCITA_COMUNE, " +
            "       PF.NASCITA_DATA, " +
            "       PF.DOM_INDIRIZZO, " +
            "       PF.DOM_COMUNE, " +
            "       PF.DOM_CAP, " +
            "       PF.DOM_CITTA_ESTERO, " +
            "       PF.RES_INDIRIZZO, " +
            "       PF.RES_COMUNE, " +
            "       PF.RES_CAP, " +
            "       PF.RES_TELEFONO, " +
            "       PF.RES_FAX, " +
            "       PF.RES_MAIL, " +
            "       PF.RES_CITTA_ESTERO, " +
            "       PF.NOTE, " +
            "       PF.DATA_AGGIORNAMENTO, " +
            "       PF.ID_UTENTE_AGGIORNAMENTO, " +
            "       C1.DESCOM AS DESCOM_RES, " +
            "       C1.FLAG_ESTERO AS FLAG_ESTERO_RES, " +
            "       P1.SIGLA_PROVINCIA AS SIGLA_PROVINCIA_RES, " +
            "       C2.DESCOM AS DESCOM_NAS, " +
            "       C2.FLAG_ESTERO AS FLAG_ESTERO_NAS, " +
            "       P2.SIGLA_PROVINCIA AS SIGLA_PROVINCIA_NAS, " +
            "       PF.NASCITA_CITTA_ESTERO, " +
            "       C3.DESCOM AS DESCOM_DOM, " +
            "       C3.FLAG_ESTERO AS FLAG_ESTERO_DOM, " +
            "       P3.SIGLA_PROVINCIA AS SIGLA_PROVINCIA_DOM, " +
            "       C.DATA_INIZIO_RUOLO, " +
            "       PF.DATA_INIZIO_RESIDENZA, " +
            "       PF.FLAG_CF_OK, " +
            "       TIS.DESCRIZIONE AS DESC_INDIRIZZO_STUDIO," +
            "       TTS.DESCRIZIONE DESC_TITOLO_STUDIO," +
            "       PF.ID_TITOLO_STUDIO," +
            "       PF.ID_INDIRIZZO_STUDIO, " +
            "       PF.NUMERO_CELLULARE, " +
            "       TR.DESCRIZIONE AS DESC_RUOLO " +
            "FROM   DB_PERSONA_FISICA PF, " +
            "       DB_ANAGRAFICA_AZIENDA A," +
            "       DB_CONTITOLARE C, " +
            "       COMUNE C1, " +
            "       COMUNE C2, " +
            "       COMUNE C3, " +
            "       PROVINCIA P1, " +
            "       PROVINCIA P2, " +
            "       PROVINCIA P3, " +
            "       DB_TIPO_INDIRIZZO_STUDIO TIS, " +
            "       DB_TIPO_TITOLO_STUDIO TTS, " +
            "       DB_TIPO_RUOLO TR " +
            "WHERE  A.ID_ANAGRAFICA_AZIENDA = ? " +
            "AND    A.ID_AZIENDA = C.ID_AZIENDA " +
            "AND    C.ID_RUOLO <> ? ");
      if(dataDichiarazione !=null)
      {
        queryBuf.append("" +
            "  AND  C.DATA_INIZIO_RUOLO <= ? " +
            "  AND  NVL(C.DATA_FINE_RUOLO, ?) >= ? ");
      }
      else
      {
        queryBuf.append("" +
            "AND    C.DATA_FINE_RUOLO IS NULL ");
      }
      
      queryBuf.append("" +    
            "AND    C.ID_SOGGETTO = PF.ID_SOGGETTO " +
            "AND    C.ID_RUOLO = TR.ID_RUOLO " +
            "AND    PF.RES_COMUNE = C1.ISTAT_COMUNE(+) " +
            "AND    PF.NASCITA_COMUNE = C2.ISTAT_COMUNE(+) " +
            "AND    PF.DOM_COMUNE = C3.ISTAT_COMUNE(+) " +
            "AND    C1.ISTAT_PROVINCIA = P1.ISTAT_PROVINCIA(+) " +
            "AND    C2.ISTAT_PROVINCIA = P2.ISTAT_PROVINCIA(+) " +
            "AND    C3.ISTAT_PROVINCIA = P3.ISTAT_PROVINCIA(+) " +
            "AND    PF.ID_TITOLO_STUDIO = TTS.ID_TITOLO_STUDIO(+) " +
            "AND    PF.ID_INDIRIZZO_STUDIO = TIS.ID_INDIRIZZO_STUDIO(+) " +
            "ORDER BY TR.DESCRIZIONE ");
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AnagWriteDAO::getVAltriSoggettiFromIdAnagAziendaAndDichCons] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAnagAzienda);
      stmt.setString(++indice, SolmrConstants.TIPORUOLO_TITOL_RAPPR_LEG);
      if(dataDichiarazione != null)
      {
        stmt.setTimestamp(++indice, convertDateToTimestamp(dataDichiarazione));
        stmt.setDate(++indice, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
        stmt.setTimestamp(++indice, convertDateToTimestamp(dataDichiarazione));
      }
      
      ResultSet rs = stmt.executeQuery();
      while(rs.next())
      {
        if(vPersonaFisicaVO == null)
        {
          vPersonaFisicaVO = new Vector<PersonaFisicaVO>();
        }
        
        personaFisicaVO = new PersonaFisicaVO();
        personaFisicaVO.setNumeroCellulare(rs.getString("NUMERO_CELLULARE"));
        personaFisicaVO.setIdPersonaFisica(new Long(rs.getLong("ID_PERSONA_FISICA")));
        personaFisicaVO.setIdSoggetto(new Long(rs.getLong("ID_SOGGETTO")));
        personaFisicaVO.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
        personaFisicaVO.setCognome(rs.getString("COGNOME"));
        personaFisicaVO.setNome(rs.getString("NOME"));
        personaFisicaVO.setSesso(rs.getString("SESSO"));
        personaFisicaVO.setNascitaComune(rs.getString("NASCITA_COMUNE"));
        personaFisicaVO.setNascitaData(rs.getDate("NASCITA_DATA"));
        personaFisicaVO.setDomIndirizzo(rs.getString("DOM_INDIRIZZO"));
        personaFisicaVO.setIstatComuneDomicilio(rs.getString("DOM_COMUNE"));
        personaFisicaVO.setDomCAP(rs.getString("DOM_CAP"));
        personaFisicaVO.setDescCittaEsteroDomicilio(rs.getString("DOM_CITTA_ESTERO"));
        personaFisicaVO.setResIndirizzo(rs.getString("RES_INDIRIZZO"));
        personaFisicaVO.setResComune(rs.getString("RES_COMUNE"));
        personaFisicaVO.setResCAP(rs.getString("RES_CAP"));
        personaFisicaVO.setResTelefono(rs.getString("RES_TELEFONO"));
        personaFisicaVO.setResFax(rs.getString("RES_FAX"));
        personaFisicaVO.setResMail(rs.getString("RES_MAIL"));
        personaFisicaVO.setCittaResidenza(rs.getString("RES_CITTA_ESTERO"));
        personaFisicaVO.setResCittaEstero(rs.getString("RES_CITTA_ESTERO"));
        personaFisicaVO.setNote(rs.getString("NOTE"));
        personaFisicaVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
        personaFisicaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        personaFisicaVO.setDescResComune(rs.getString("DESCOM_RES"));
        personaFisicaVO.setFlagEstero(rs.getString("FLAG_ESTERO_RES"));
        personaFisicaVO.setResProvincia(rs.getString("SIGLA_PROVINCIA_RES"));
        personaFisicaVO.setDescNascitaComune(rs.getString("DESCOM_NAS"));
        personaFisicaVO.setNascitaFlagEstero(rs.getString("FLAG_ESTERO_NAS"));
        personaFisicaVO.setNascitaProv(rs.getString("SIGLA_PROVINCIA_NAS"));
        personaFisicaVO.setNewPersonaFisica(false);
        if (Validator.isNotEmpty(rs.getString("ID_TITOLO_STUDIO")))
        {
          personaFisicaVO.setIdTitoloStudio(new Long(rs.getLong("ID_TITOLO_STUDIO")));
          personaFisicaVO.setDescrizioneTitoloStudio(rs.getString("DESC_TITOLO_STUDIO"));
        }
        if (Validator.isNotEmpty(rs.getString("ID_INDIRIZZO_STUDIO")))
        {
          personaFisicaVO.setIdIndirizzoStudio(new Long(rs
              .getLong("ID_INDIRIZZO_STUDIO")));
          personaFisicaVO.setDescrizioneIndirizzoStudio(rs
              .getString("DESC_INDIRIZZO_STUDIO"));
        }

        if ("S".equalsIgnoreCase(personaFisicaVO.getFlagEstero()))
        {
          // In questo caso il comune contiene le informazioni sullo stato
          // estero
          personaFisicaVO.setDescStatoEsteroResidenza(personaFisicaVO.getDescResComune());
          personaFisicaVO.setResProvincia(null);
          personaFisicaVO.setDescResComune(null);
        }
        if ("S".equalsIgnoreCase(personaFisicaVO.getNascitaFlagEstero()))
        {
          // In questo caso il comune contiene le informazioni sullo stato
          // estero
          personaFisicaVO.setNascitaStatoEstero(personaFisicaVO.getDescNascitaComune());
          personaFisicaVO.setNascitaCittaEstero(rs.getString("NASCITA_CITTA_ESTERO"));
          personaFisicaVO.setDescNascitaComune(null);
          personaFisicaVO.setNascitaProv(null);
        }
        personaFisicaVO.setCittaNascita(rs.getString("NASCITA_CITTA_ESTERO"));
        personaFisicaVO.setDomicilioFlagEstero(rs.getString("FLAG_ESTERO_DOM"));
        if ((SolmrConstants.FLAG_S).equalsIgnoreCase(personaFisicaVO
            .getDomicilioFlagEstero()))
        {
          personaFisicaVO.setDomicilioStatoEstero(rs.getString("DESCOM_DOM"));
          personaFisicaVO.setDomProvincia(null);
        }
        else
        {
          personaFisicaVO.setDomComune(rs.getString("DESCOM_DOM"));
          personaFisicaVO.setDomProvincia(rs.getString("SIGLA_PROVINCIA_DOM"));
        }
        personaFisicaVO.setDataInizioRuolo(rs.getDate("DATA_INIZIO_RUOLO"));
        if (Validator.isNotEmpty(rs.getString("DATA_INIZIO_RESIDENZA")))
        {
          personaFisicaVO.setDataInizioResidenza(rs.getDate("DATA_INIZIO_RESIDENZA"));
        }
        personaFisicaVO.setFlagCfOk(rs.getString("FLAG_CF_OK"));
        personaFisicaVO.setRuolo(rs.getString("DESC_RUOLO"));
        
        vPersonaFisicaVO.add(personaFisicaVO);
      }
      
      return vPersonaFisicaVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("personaFisicaVO", personaFisicaVO),
      new Variabile("vPersonaFisicaVO", vPersonaFisicaVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAnagAzienda", idAnagAzienda),
        new Parametro("dataDichiarazione", dataDichiarazione) };
  
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[AnagWriteDAO::getVAltriSoggettiFromIdAnagAziendaAndDichCons] ", t,
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
          "[AnagWriteDAO::getVAltriSoggettiFromIdAnagAziendaAndDichCons] END.");
    }
  }

  // Metodo che ricerca le aziende attive (data_fine_validità=null and
  // data_cessazione=null)
  // per cui il soggetto è legato con un ruolo attivo all'azienda.
  public CodeDescription[] getRappresentanteLegaleFromIdAnagAzienda(
      String codiceFiscale) throws DataAccessException
  {
    return getRappresentanteLegaleFromIdAnagAzienda(codiceFiscale, true);
  }

  // Metodo che ricerca le aziende attive (data_fine_validità=null and
  // data_cessazione=null)
  // per cui il soggetto è legato con un ruolo attivo all'azienda.
  public CodeDescription[] getRappresentanteLegaleFromIdAnagAzienda(
      String codiceFiscale, boolean aziendeAttive) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<CodeDescription> idAzienda = new Vector<CodeDescription>();

    try
    {
      conn = getDatasource().getConnection();
      String query = "SELECT A.ID_AZIENDA,C.ID_RUOLO,A.CUAA, TR.CARICA_RL "
          + "FROM DB_PERSONA_FISICA P, DB_ANAGRAFICA_AZIENDA A, DB_CONTITOLARE C, DB_TIPO_RUOLO TR "
          + "WHERE P.CODICE_FISCALE = UPPER(?) "
          + "  AND C.ID_SOGGETTO = P.ID_SOGGETTO"
          + "  AND A.ID_AZIENDA = C.ID_AZIENDA"
          + "  AND C.DATA_FINE_RUOLO IS NULL"
          + "  AND A.DATA_FINE_VALIDITA IS NULL" 
          + " AND C.ID_RUOLO = TR.ID_RUOLO ";
      if (aziendeAttive)
        query += "  AND A.DATA_CESSAZIONE IS NULL";

      stmt = conn.prepareStatement(query);
      stmt.setString(1, codiceFiscale);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        CodeDescription codeDescription = new CodeDescription();
        codeDescription.setCode(new Integer(rs.getInt("ID_AZIENDA")));
        codeDescription.setSecondaryCode(rs.getString("ID_RUOLO"));
        codeDescription.setDescription(rs.getString("CUAA"));
        codeDescription.setCodeFlag(rs.getString("CARICA_RL"));
        idAzienda.add(codeDescription);
      }

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "SQLException in getRappresentanteLegaleFromIdAnagAzienda: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in getRappresentanteLegaleFromIdAnagAzienda: "
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
    return idAzienda.size() == 0 ? null : (CodeDescription[]) idAzienda
        .toArray(new CodeDescription[0]);
  }

  // Il metodo,come espressamente richiesto dal dominio,i legami
  // con l'azienda selezionata e il rappresentante legale.
  public void cessaRappresentanteLegaleDate(Long aziendaPK, Date dataInizioRuolo)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String update = "UPDATE DB_CONTITOLARE " + 
                      "SET DATA_FINE_RUOLO = TRUNC(SYSDATE), " + 
                      " DATA_FINE_RUOLO_MOD = ? " + 
                      "WHERE ID_AZIENDA=? " + 
                      "AND ID_RUOLO= " + 
                      SolmrConstants.get("RUOLO_RAPPRESENTANTE_LEGALE") + 
                      " AND DATA_FINE_RUOLO IS NULL";

      stmt = conn.prepareStatement(update);

      stmt.setDate(1, checkDate(dataInizioRuolo));
      stmt.setLong(2, aziendaPK.longValue());

      SolmrLogger.debug(this, "Executing query: " + update);

      stmt.executeUpdate();
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
  }

  // Il metodo,come espressamente richiesto dal dominio,i legami
  // con l'azienda selezionata e il rappresentante legale.
  public void cessaRappresentanteLegale(Long aziendaPK)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String update = "UPDATE DB_CONTITOLARE "
          + "SET DATA_FINE_RUOLO = trunc(SYSDATE) " + "WHERE ID_AZIENDA=? "
          + "AND ID_RUOLO= "
          + SolmrConstants.get("RUOLO_RAPPRESENTANTE_LEGALE")
          + " AND DATA_FINE_RUOLO IS NULL";

      stmt = conn.prepareStatement(update);

      stmt.setLong(1, aziendaPK.longValue());

      SolmrLogger.debug(this, "Executing query: " + update);

      stmt.executeUpdate();
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
  }

  // Il metodo,come espressamente richiesto dal dominio,si occupa di far cessare
  // tutti i legami
  // con l'azienda selezionata non solo quello del rappresentante legale.
  public void cessaRappLegAndAltriSoggetti(Long aziendaPK)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String update = "UPDATE DB_CONTITOLARE "
          + "SET DATA_FINE_RUOLO = trunc(SYSDATE) " + "WHERE ID_AZIENDA=? "
          + "AND DATA_FINE_RUOLO IS NULL";

      stmt = conn.prepareStatement(update);

      stmt.setLong(1, aziendaPK.longValue());

      SolmrLogger.debug(this, "Executing query: " + update);

      stmt.executeUpdate();
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
  }

  // il sistema cessa tutti i ruoli attivi che il nuovo titolare aveva
  // eventualmente
  // in azienda E' necessario cioè fare l'update su db_contitolare valorizzando
  // a sysdate data_fine_ruolo su tutti i record con l'id_soggetto=nuovo
  // rappresentante legale e l'id_azienda= quella selezionata

  public void cessaRuoliAttiviRappresentanteLegale(Long idAzienda,
      Long idSoggetto) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String update = "UPDATE DB_CONTITOLARE "
          + "SET DATA_FINE_RUOLO = trunc(SYSDATE), "
          + "DATA_FINE_RUOLO_MOD = trunc(SYSDATE) "          
          + "WHERE ID_AZIENDA=? "
          + "AND ID_SOGGETTO= ? " + "AND DATA_FINE_RUOLO IS NULL";

      stmt = conn.prepareStatement(update);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setLong(2, idSoggetto.longValue());

      SolmrLogger.debug(this, "Executing query: " + update);

      stmt.executeUpdate();
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
  }

  // Il metodo si occupa di far cessare il legame del rappresentante legale
  // con l'azienda selezionata .
  public void cessaTitolare(AnagAziendaVO anagAziendaVO,
      PersonaFisicaVO personaFisicaVO) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String update = "UPDATE DB_CONTITOLARE "
          + "SET DATA_FINE_RUOLO = trunc(SYSDATE) "
          + "WHERE ID_AZIENDA= ? AND " + "ID_SOGGETTO = ? AND "
          + "ID_RUOLO = ? AND " + "DATA_FINE_RUOLO IS NULL";

      stmt = conn.prepareStatement(update);

      stmt.setLong(1, anagAziendaVO.getIdAzienda().longValue());
      stmt.setLong(2, personaFisicaVO.getIdSoggetto().longValue());
      stmt.setString(3, SolmrConstants.TIPORUOLO_TITOL_RAPPR_LEG);

      SolmrLogger.debug(this, "Executing query in cessaTitolare: " + update);

      stmt.executeUpdate();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in cessaTitolare: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in cessaTitolare: "
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
            "SQLException while closing Statement and Connection in cessaTitolare: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection in cessaTitolare: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per verificare che la forma giuridica abbia il flag CCIAA = S
  public String getFormaGiuridicaFlagCCIAA(Long idFormaGiuridica)
      throws DataAccessException
  {

    String flagCCIAA = null;

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try
    {

      conn = getDatasource().getConnection();

      String query = "select flag_cciaa from db_tipo_forma_giuridica where id_forma_giuridica = ? ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idFormaGiuridica.longValue());

      SolmrLogger.debug(this, "Executing query hasFormaGiuridicaFlagCCIAA: "
          + query);

      rs = stmt.executeQuery();

      if (rs.next())
      {
        flagCCIAA = rs.getString(1);
      }

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in hasFormaGiuridicaFlagCCIAA: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(this, "Generic Exception in hasFormaGiuridicaFlagCCIAA: "
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
    return flagCCIAA;
  }

  public void storicizzaAzienda(Long idAnagAzienda) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "UPDATE DB_ANAGRAFICA_AZIENDA "
          + " SET DATA_FINE_VALIDITA = TRUNC(SYSDATE -1) "
          + " WHERE ID_ANAGRAFICA_AZIENDA = ? ";
      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idAnagAzienda.longValue());

      SolmrLogger.debug(this, "Executing storicizzaAzienda =" + query);
      SolmrLogger.debug(this, "-- id_anagrafica_azienda ="+idAnagAzienda);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed.");
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
  }

  public void updateAzienda(AnagAziendaVO anagVO) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = 
      "UPDATE DB_ANAGRAFICA_AZIENDA " + 
      "SET    DENOMINAZIONE = ?, " +
      "       PARTITA_IVA = ?, " +
      "       ID_UTENTE_AGGIORNAMENTO = ?, " + 
      "       ID_FORMA_GIURIDICA = ?, " +
      "       CCIAA_PROVINCIA_REA= ?, " +
      "       CCIAA_NUMERO_REA = ?, " + 
      "       ID_ATTIVITA_OTE = ?, " +
      "       ID_ATTIVITA_ATECO = ?, " +
      "       NOTE = ?, " +
      "       MOTIVO_MODIFICA = ?, " + 
      "       DATA_AGGIORNAMENTO = SYSDATE, " +
      "       CUAA = ?, " +
      "       SEDELEG_COMUNE = ?, " +
      "       SEDELEG_INDIRIZZO = ?, " + 
      "       MAIL = ?, " +
      "       SITOWEB = ?, " +
      "       SEDELEG_CAP = ? , " +
      "       SEDELEG_CITTA_ESTERO = ? , " + 
      "       ID_TIPOLOGIA_AZIENDA = ?, " + 
      "       CCIAA_NUMERO_REGISTRO_IMPRESE = ? , " +
      "       CCIAA_ANNO_ISCRIZIONE = ? , " +
      "       PROVINCIA_COMPETENZA = ?, " +	
      "       ID_UDE = ? , " +
      "       ID_DIMENSIONE_AZIENDA = ?, " +
      "       RLS = ?, " +
      "       ULU = ?, " +
      "       TELEFONO = ?, " +	
      "       FAX = ?, " +
      "       PEC = ?, " +
      "       CODICE_AGRITURISMO = ?, " + 
      "       ESONERO_PAGAMENTO_GF = ?, " +
      "       INTESTAZIONE_PARTITA_IVA = ?, " +
      "       DATA_AGGIORNAMENTO_UMA = ?, " +
      "       FLAG_IAP = ?, " +
      "       DATA_ISCRIZIONE_REA = ?, " +
      "       DATA_CESSAZIONE_REA = ?, " +
      "       DATA_ISCRIZIONE_RI = ?, " +
      "       DATA_CESSAZIONE_RI = ?, " +
      "       DATA_INIZIO_ATECO = ? " +
      "WHERE  ID_ANAGRAFICA_AZIENDA = ? ";
      stmt = conn.prepareStatement(query);

      stmt.setString(1, anagVO.getDenominazione().toUpperCase().trim());
      stmt.setString(2, anagVO.getPartitaIVA());
      stmt.setLong(3, anagVO.getIdUtenteAggiornamento().longValue());
      if (anagVO.getTipoFormaGiuridica() != null
          && anagVO.getTipoFormaGiuridica().getCode() != null)
        stmt.setLong(4, anagVO.getTipoFormaGiuridica().getCode().longValue());
      else
        stmt.setBigDecimal(4, null);
      stmt.setString(5, anagVO.getCCIAAprovREA());
      if (anagVO.getCCIAAnumeroREA() != null)
        stmt.setLong(6, anagVO.getCCIAAnumeroREA().longValue());
      else
        stmt.setBigDecimal(6, null);
      if (anagVO.getTipoAttivitaOTE() != null
          && anagVO.getTipoAttivitaOTE().getCode() != null)
        stmt.setLong(7, anagVO.getTipoAttivitaOTE().getCode().longValue());
      else
        stmt.setBigDecimal(7, null);

      if (anagVO.getTipoAttivitaATECO() != null
          && anagVO.getTipoAttivitaATECO().getCode() != null)
        stmt.setLong(8, anagVO.getTipoAttivitaATECO().getCode().longValue());
      else
        stmt.setBigDecimal(8, null);
      stmt.setString(9, anagVO.getNote());
      stmt.setString(10, anagVO.getMotivoModifica());
      stmt.setString(11, anagVO.getCUAA().toUpperCase());
      if (anagVO.getSedelegComune() != null)
      {
        stmt.setString(12, anagVO.getSedelegComune());
      }
      else
      {
        stmt.setString(12, anagVO.getSedelegEstero());
      }
      if (anagVO.getSedelegIndirizzo() != null)
      {
        stmt.setString(13, anagVO.getSedelegIndirizzo().toUpperCase());
      }
      else
      {
        stmt.setString(13, null);
      }
      stmt.setString(14, anagVO.getMail());
      stmt.setString(15, anagVO.getSitoWEB());
      stmt.setString(16, anagVO.getSedelegCAP());
      // Se la città estera della sede legale è stata valorizzata, la inserisco
      // convertendola in maiuscolo
      // come richiesto dal dominio
      if (Validator.isNotEmpty(anagVO.getSedelegCittaEstero()))
      {
        stmt.setString(17, anagVO.getSedelegCittaEstero().toUpperCase());
      }
      else
      {
        stmt.setString(17, null);
      }
      stmt.setLong(18, anagVO.getTipoTipologiaAzienda().getCode().longValue());
      if (anagVO.getCCIAAnumRegImprese() != null)
      {
        stmt.setString(19, anagVO.getCCIAAnumRegImprese().toUpperCase());
      }
      else
      {
        stmt.setString(19, null);
      }
      stmt.setString(20, anagVO.getCCIAAannoIscrizione());
      stmt.setString(21, anagVO.getProvCompetenza());
      if (anagVO.getIdUde() != null)
        stmt.setLong(22, anagVO.getIdUde().longValue());
      else
        stmt.setBigDecimal(22, null);
      if (anagVO.getIdDimensioneAzienda() != null)
        stmt.setLong(23, anagVO.getIdDimensioneAzienda().longValue());
      else
        stmt.setBigDecimal(23, null);
      
      stmt.setBigDecimal(24, anagVO.getRls());
      stmt.setBigDecimal(25, anagVO.getUlu());
      stmt.setString(26, anagVO.getTelefono());
      stmt.setString(27, anagVO.getFax());
      stmt.setString(28, anagVO.getPec());
      stmt.setString(29, anagVO.getCodiceAgriturismo());
      stmt.setString(30, anagVO.getEsoneroPagamentoGF());
      
      if(Validator.isNotEmpty(anagVO.getIntestazionePartitaIva()))
        stmt.setString(31, anagVO.getIntestazionePartitaIva().toUpperCase());
      else
        stmt.setString(31, null);
      
      stmt.setTimestamp(32,convertDateToTimestamp(anagVO.getDataAggiornamentoUma()));
      stmt.setString(33, anagVO.getFlagIap());
      stmt.setTimestamp(34,convertDateToTimestamp(anagVO.getDataIscrizioneRea()));
      stmt.setTimestamp(35,convertDateToTimestamp(anagVO.getDataCessazioneRea()));
      stmt.setTimestamp(36,convertDateToTimestamp(anagVO.getDataIscrizioneRi()));
      stmt.setTimestamp(37,convertDateToTimestamp(anagVO.getDataCessazioneRi()));
      stmt.setTimestamp(38,convertDateToTimestamp(anagVO.getDataInizioAteco()));
      
      stmt.setLong(39, anagVO.getIdAnagAzienda().longValue());
     

      SolmrLogger.debug(this, "Executing: " + query);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed.");
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
  }
  
  public void updateAziendaMotivoModifica(AnagAziendaVO anagVO) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "UPDATE DB_ANAGRAFICA_AZIENDA "
          + " SET MOTIVO_MODIFICA = ? "
          + " WHERE ID_ANAGRAFICA_AZIENDA = ? ";
      
      stmt = conn.prepareStatement(query);

      
      stmt.setString(1, anagVO.getMotivoModifica());
      stmt.setLong(2, anagVO.getIdAnagAzienda().longValue());
     

      SolmrLogger.debug(this, "Executing: " + query);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed.");
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
  }

  public Long updateDatiAnagSedeLegAAEP(AnagAAEPAziendaVO anagAAEPAziendaVO,
      AnagAziendaVO anagVO, long idUtenteAggiornamento, boolean denominazione,
      boolean partitaIVA, boolean descrizioneAteco, boolean provinciaREA,
      boolean numeroREA, boolean annoIscrizione, boolean numeroRegistroImprese,
      boolean pec, boolean sedeLegale, boolean formaGiuridica, boolean dataIscrizioneREA,
      boolean dataCancellazioneREA, boolean dataIscrizioneRI) throws DataAccessException
  {
    Long idAnagAzienda = anagVO.getIdAnagAzienda();
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      /**
       * Per prima cosa controllo se esiste già una storicizzazione per oggi
       * oppure no
       */
      SolmrLogger.debug(this,"-- controllo se esiste già una storicizzazione per oggi");
      boolean storicizzazionePresente = false;

      String query = "SELECT ID_AZIENDA " + "FROM DB_AZIENDA A "
          + "WHERE ID_AZIENDA = ? "
          + "AND TRUNC(A.DATA_INIZIO_VALIDITA) = TRUNC(SYSDATE) ";

      stmt = conn.prepareStatement(query);
      
      SolmrLogger.debug(this, "Executing updateDatiAnagSedeLegAAEP: " + query);

      stmt.setLong(1, anagVO.getIdAzienda().longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        storicizzazionePresente = true;
      }
      rs.close();

      SolmrLogger.debug(this, "-- storicizzazionePresente ="+storicizzazionePresente);
      if (!storicizzazionePresente)
      {
        /***********************************************************************
         * Storicizzare il record attivo
         * (db_anagrafica_azienda.data_fine_validità a null) dell'azienda in
         * esame aggiornando le seguenti colonne della tabella
         * Db_Anagrafica_Azienda:
         */
        query = "UPDATE DB_ANAGRAFICA_AZIENDA "
            + "SET DATA_FINE_VALIDITA = TRUNC(SYSDATE -1), "
            + "MOTIVO_MODIFICA = '"
            + (String) SolmrConstants.get("MOTIVO_MODIFICA_DATI_AAEP") + "',"
            + "ID_UTENTE_AGGIORNAMENTO = ? "
            + "WHERE ID_ANAGRAFICA_AZIENDA = ? ";
        stmt = conn.prepareStatement(query);
        stmt.setLong(1, anagVO.getIdUtenteAggiornamento().longValue());
        stmt.setLong(2, anagVO.getIdAnagAzienda().longValue());

        SolmrLogger.debug(this, "Executing: " + query);

        stmt.executeUpdate();
        stmt.close();

        /***********************************************************************
         * Aggiornare la data inizio validità dell'azienda aggiornando le
         * seguenti colonne della tabella Db_Azienda
         */
        query = "UPDATE DB_AZIENDA "
            + "SET DATA_INIZIO_VALIDITA = trunc(SYSDATE) "
            + "WHERE ID_AZIENDA = ? ";
        stmt = conn.prepareStatement(query);
        stmt.setLong(1, anagVO.getIdAzienda().longValue());

        SolmrLogger.debug(this, "Executing: " + query);

        stmt.executeUpdate();
        stmt.close();
      }

      java.sql.Date dataInizioValiditaAzienda = null;

      /**
       * Vado a leggere la dataInizioValiditaAzienda
       */
      query = "SELECT DATA_INIZIO_VALIDITA FROM DB_AZIENDA "
          + "WHERE ID_AZIENDA = ? ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, anagVO.getIdAzienda().longValue());

      SolmrLogger.debug(this, "Executing query: " + query);

      rs = stmt.executeQuery();

      if (rs.next())
      {
        dataInizioValiditaAzienda = rs.getDate("DATA_INIZIO_VALIDITA");
      }
      rs.close();
      stmt.close();

      Long idFormaGiuridica = null;
      
      if (anagAAEPAziendaVO.getFormaGiuridicaAAEP()!=null)
      {

	      /**
	       * Vado a leggere l'id della forma giuridica corrispondente alla
	       * descrizione fornita da AAEP
	       */
	      query = "SELECT ID_FORMA_GIURIDICA FROM DB_TIPO_FORMA_GIURIDICA "
	          + " WHERE DESCRIZIONE = UPPER(?) ";
	
	      stmt = conn.prepareStatement(query);
	
	      stmt
	          .setString(1, anagAAEPAziendaVO.getFormaGiuridicaAAEP().toUpperCase());
	
	      SolmrLogger.debug(this, "Executing query: " + query);
	
	      rs = stmt.executeQuery();
	
	      if (rs.next())
	      {
	        idFormaGiuridica = new Long(rs.getLong("ID_FORMA_GIURIDICA"));
	      }
	      rs.close();
	      stmt.close();
	      
	    }
      
      //Aggiunta GAA06-01 Visura AAEP - fonte infoCamere - v2.doc
      //**************************************************
      Vector<Long> listIdTipoAz = null;
      //Se true significa che è stato trovato nella tabella 
      //DB_TIPO_FG_TIPOLOGIA la relazione padre/figlio tra id_tipologia_azienda corrente 
      //e ID_FORMA_GIURIDICA di AAEP
      boolean flagTipoAziendaFormaGiuridica = false;
      if(idFormaGiuridica !=null)
      {
        query = "SELECT ID_TIPOLOGIA_AZIENDA "
          + "FROM DB_TIPO_FG_TIPOLOGIA " 
          + "WHERE ID_FORMA_GIURIDICA = ? "
          + "ORDER BY ID_TIPOLOGIA_AZIENDA ";
        
        stmt = conn.prepareStatement(query);
        stmt.setLong(1, idFormaGiuridica.longValue());
        
        SolmrLogger.debug(this, "Executing query: " + query);

        rs = stmt.executeQuery();

        while (rs.next())
        {
          if(listIdTipoAz == null)
          {
            listIdTipoAz = new Vector<Long>();
          }
          listIdTipoAz.add(new Long(rs.getLong("ID_TIPOLOGIA_AZIENDA")));
        }
        
        if(listIdTipoAz !=null)
        {
          for(int k=0;k<listIdTipoAz.size();k++)
          {
            Long idTipoAz = (Long)listIdTipoAz.get(k);
            if(idTipoAz.toString().equalsIgnoreCase(anagVO.getTipiAzienda()))
            {
              flagTipoAziendaFormaGiuridica = true;
              break;
            }
          }
        }
        rs.close();
        stmt.close();
        
        
      }

      Long idAttivitaAteco = null;

      /**
       * Vado a leggere l'id del codice attività ateco
       */
      query = "SELECT ID_ATTIVITA_ATECO FROM DB_TIPO_ATTIVITA_ATECO "
          + " WHERE CODICE=? AND DATA_FINE_VALIDITA IS NULL ";// AND ID_TIPO_CODIFICA_ATECO = " 
          //+ "(SELECT VALORE FROM DB_PARAMETRO WHERE ID_PARAMETRO like '"+SolmrConstants.PARAMETRO_CATE+"')";

      stmt = conn.prepareStatement(query);

      stmt.setString(1, anagAAEPAziendaVO.getIdAtecoAAEP());

      SolmrLogger.debug(this, "Executing query: " + query);

      rs = stmt.executeQuery();

      if (rs.next())
      {
        idAttivitaAteco = new Long(rs.getLong("ID_ATTIVITA_ATECO"));
      }
      rs.close();
      stmt.close();

      if (!storicizzazionePresente)
      {
        /***********************************************************************
         * Inserire un nuovo record sulla tabella db_anagrafica_azienda con gli
         * stessi valori del record storicizzato aggiornando con i nuovi valori
         * solo le colonne che l'utente vuole importare da AAEP (selezionate a
         * video=
         */
        Long primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_ANAG_AZIENDA);

        idAnagAzienda = primaryKey;

        String insert = "INSERT INTO DB_ANAGRAFICA_AZIENDA "
            + " (ID_ANAGRAFICA_AZIENDA, " + "  ID_AZIENDA, "
            + "  ID_TIPOLOGIA_AZIENDA," + "  DATA_INIZIO_VALIDITA, "
            + "  DATA_FINE_VALIDITA, " + "  CUAA, " + "  PARTITA_IVA, "
            + "  DENOMINAZIONE, " + "  ID_FORMA_GIURIDICA, "
            + "  ID_ATTIVITA_ATECO, " + "  PROVINCIA_COMPETENZA, "
            + "  CCIAA_PROVINCIA_REA, " + "  CCIAA_NUMERO_REA, " + "  MAIL, "
            + "  SEDELEG_COMUNE, " + "  SEDELEG_INDIRIZZO, " + "  SITOWEB, "
            + "  SEDELEG_CITTA_ESTERO, " + "  SEDELEG_CAP, "
            + "  DATA_CESSAZIONE, " + "  CAUSALE_CESSAZIONE, " + "  NOTE, "
            + "  DATA_AGGIORNAMENTO, " + "  ID_UTENTE_AGGIORNAMENTO, "
            + "  ID_ATTIVITA_OTE, " + "  MOTIVO_MODIFICA, "
            + "  CCIAA_NUMERO_REGISTRO_IMPRESE, " + "  CCIAA_ANNO_ISCRIZIONE, "
            + "  MODIFICA_INTERMEDIARIO, " + "  NUMERO_AGEA,"
            +	"  ID_UDE, ID_DIMENSIONE_AZIENDA, RLS, ULU, TELEFONO, FAX, "
            + "  PEC, CODICE_AGRITURISMO, ESONERO_PAGAMENTO_GF, INTESTAZIONE_PARTITA_IVA, "
            + "  DATA_AGGIORNAMENTO_UMA, FLAG_IAP, DATA_ISCRIZIONE_REA, " +
            "    DATA_CESSAZIONE_REA, DATA_ISCRIZIONE_RI, DATA_CESSAZIONE_RI, " +
            "    DATA_INIZIO_ATECO ) " +
            " VALUES (?, " + // ID_ANAGRAFICA_AZIENDA 1
            "  ?, " + // ID_AZIENDA 2
            "  ?, " + // ID_TIPOLOGIA_AZIENDA 3
            "  ?, " + // DATA_INIZIO_VALIDITA 4
            "  null, " + // DATA_FINE_VALIDITA
            "  ?, " + // CUAA 5
            "  ?, " + // PARTITA_IVA 6
            "  ?, " + // DENOMINAZIONE 7
            "  ?, " + // ID_FORMA_GIURIDICA 8
            "  ?, " + // ID_ATTIVITA_ATECO 9
            "  ?, " + // PROVINCIA_COMPETENZA 10
            "  ?, " + // CCIAA_PROVINCIA_REA 11
            "  ?, " + // CCIAA_NUMERO_REA 12
            "  ?, " + // MAIL 13
            "  ?, " + // SEDELEG_COMUNE 14
            "  ?, " + // SEDELEG_INDIRIZZO 15
            "  ?, " + // SITOWEB 16
            "  null, " + // SEDELEG_CITTA_ESTERO
            "  ?, " + // SEDELEG_CAP 17
            "  ?, " + // DATA_CESSAZIONE 18
            "  ?, " + // CAUSALE_CESSAZIONE 19
            "  null, " + // NOTE
            "  SYSDATE, " + // DATA_AGGIORNAMENTO
            "  ?, " + // ID_UTENTE_AGGIORNAMENTO 20
            "  ?, " + // ID_ATTIVITA_OTE 21
            "  null, " + // MOTIVO_MODIFICA
            "  ?, " + // CCIAA_NUMERO_REGISTRO_IMPRESE 22
            "  ?, " + // CCIAA_ANNO_ISCRIZIONE 23
            "  null, " + // MODIFICA_INTERMEDIARIO
            "  ?," + // NUMERO_AGEA 24
            "  ?," + // ID_UDE 25
            "  ?," + // ID_DIMENSIONE_AZIENDA 26
            "  ?," + //  RLS 27
            "  ?," + //  ULU 28
            "  ?," + //  TELEFONO 29
            "  ?," + //  FAX 30
            "  ?," + //  PEC 31
        		"  ?," + // CODICE_AGRITURISMO 32
            "  ?," + //ESONERO_PAGAMENTO_GF 33
            "  ?," + //INTESTAZIONE_PARTITA_IVA 34
            "  ?, " + //DATA_AGGIORNAMENTO_UMA 35
            "  ?, " + //FLAG_IAP
            "  ?, " + //DATA_ISCRIZIONE_REA
            "  ?, " + //DATA_CESSAZIONE_REA
            "  ?, " + //DATA_ISCRIZIONE_RI
            "  ?, " + //DATA_CESSAZIONE_RI
            "  ? )"; //DATA_INIZIO_ATECO
           
        SolmrLogger.debug(this, "-- insert DB_ANAGRAFICA_AZIENDA ="+insert);
        stmt = conn.prepareStatement(insert);

        SolmrLogger.debug(this, "-- ID_ANAGRAFICA_AZIENDA ="+primaryKey);
        stmt.setLong(1, primaryKey.longValue()); // ID_ANAGRAFICA_AZIENDA
        SolmrLogger.debug(this, "-- ID_AZIENDA ="+anagVO.getIdAzienda());
        stmt.setLong(2, anagVO.getIdAzienda().longValue()); // ID_AZIENDA

        //Aggiunta GAA06-01 Visura AAEP - fonte infoCamere - v2.doc
        //**************************************************
        if(formaGiuridica)
        {
          if(flagTipoAziendaFormaGiuridica)
          {
            if (anagVO.getTipoTipologiaAzienda() != null
                && anagVO.getTipoTipologiaAzienda().getCode() != null)
              stmt.setLong(3, anagVO.getTipoTipologiaAzienda().getCode()
                  .longValue()); // ID_TIPOLOGIA_AZIENDA
            else
              stmt.setBigDecimal(3, null); // ID_TIPOLOGIA_AZIENDA
          }
          else //Non è stata trovata relazione tra id_tipologia_azienda corrente con id_forma_giuridica AAEP
          {
            if(listIdTipoAz != null)
            {
              stmt.setLong(3, ((Long)listIdTipoAz.get(0)).longValue() ); // ID_TIPOLOGIA_AZIENDA
            }
            else
            {
              if (anagVO.getTipoTipologiaAzienda() != null
                  && anagVO.getTipoTipologiaAzienda().getCode() != null)
                stmt.setLong(3, anagVO.getTipoTipologiaAzienda().getCode()
                    .longValue()); // ID_TIPOLOGIA_AZIENDA
              else
                stmt.setBigDecimal(3, null); // ID_TIPOLOGIA_AZIENDA
            }
          }
        }
        else
        {
          if (anagVO.getTipoTipologiaAzienda() != null
              && anagVO.getTipoTipologiaAzienda().getCode() != null)
            stmt.setLong(3, anagVO.getTipoTipologiaAzienda().getCode()
                .longValue()); // ID_TIPOLOGIA_AZIENDA
          else
            stmt.setBigDecimal(3, null); // ID_TIPOLOGIA_AZIENDA
        }
        //**********************************************

        stmt.setDate(4, dataInizioValiditaAzienda); // DATA_INIZIO_VALIDITA

        if (anagVO.getCUAA() != null && !anagVO.getCUAA().equals("")) // CUAA
          stmt.setString(5, anagVO.getCUAA().toUpperCase());
        else
          stmt.setString(5, anagVO.getCUAA());

        if (partitaIVA) // PARTITA_IVA
          stmt.setString(6, anagAAEPAziendaVO.getPartitaIVAAAEP());
        else
          stmt.setString(6, anagVO.getPartitaIVA());

        if (denominazione) // DENOMINAZIONE
        {
          String temp = AnagAAEPAziendaVO.eliminaSpazi(
              anagAAEPAziendaVO.getDenominazioneAAEP()).toUpperCase();
          // Recupero i primi 1000 caratteri e non più 120
          if (temp.length() > 1000)
            temp = temp.substring(0, 1000);
          stmt.setString(7, temp);
        }
        else
          stmt.setString(7, anagVO.getDenominazione().toUpperCase().trim());

        try
        {
          if (formaGiuridica)
          {
            if (idFormaGiuridica != null)
              stmt.setLong(8, idFormaGiuridica.longValue()); // ID_FORMA_GIURIDICA
            else
              stmt.setBigDecimal(8, null);
          }
          else
            stmt.setLong(8, anagVO.getTipoFormaGiuridica().getCode()
                .longValue()); // ID_FORMA_GIURIDICA
        }
        catch (Exception e)
        {
          stmt.setBigDecimal(8, null); // ID_FORMA_GIURIDICA
        }

        if (descrizioneAteco) // ID_ATTIVITà_ATECO
        {
          if (idAttivitaAteco != null)
            stmt.setLong(9, idAttivitaAteco.longValue());
          else
            stmt.setBigDecimal(9, null);
        }
        else
        {
          if (anagVO.getTipoAttivitaATECO() != null
              && anagVO.getTipoAttivitaATECO().getCode() != null)
            stmt
                .setLong(9, anagVO.getTipoAttivitaATECO().getCode().longValue());
          else
            stmt.setBigDecimal(9, null);
        }

        stmt.setString(10, anagVO.getProvCompetenza()); // PROVINCIA_COMPETENZA

        if (provinciaREA) // CCIAA_PROVINCIA_REA
        {
          if (anagAAEPAziendaVO.getProvinciaREAAAEP() != null
              && !anagAAEPAziendaVO.getProvinciaREAAAEP().equals(""))
            stmt.setString(11, anagAAEPAziendaVO.getProvinciaREAAAEP()
                .toUpperCase());
          else
            stmt.setString(11, null);
        }
        else
        {
          if (anagVO.getCCIAAprovREA() != null
              && !anagVO.getCCIAAprovREA().equals(""))
            stmt.setString(11, anagVO.getCCIAAprovREA().toUpperCase());
          else
            stmt.setString(11, null);
        }
        if (numeroREA) // CCIAA_NUMERO_REA
        {
          if (anagAAEPAziendaVO.getNumeroREAAAEP() != null)
            stmt.setLong(12, Long.parseLong(anagAAEPAziendaVO
                .getNumeroREAAAEP()));
          else
            stmt.setBigDecimal(12, null);
        }
        else
        {
          if (anagVO.getCCIAAnumeroREA() != null)
            stmt.setLong(12, anagVO.getCCIAAnumeroREA().longValue());
          else
            stmt.setBigDecimal(12, null);
        }

        stmt.setString(16, anagVO.getSitoWEB()); // SITOWEB

        if (sedeLegale)
        {
          stmt.setString(13, anagAAEPAziendaVO.getSedeLegaleEmailAAEP()); // MAIL
          stmt.setString(14, anagAAEPAziendaVO.getSedeLegaleIstatAAEP()); // SEDELEG_COMUNE
          if (anagAAEPAziendaVO.getSedeLegaleIndirizzoAAEP().length() > 100)
          {
            stmt.setString(15, anagAAEPAziendaVO.getSedeLegaleIndirizzoAAEP()
                .substring(0, 100).trim()); // SEDELEG_INDIRIZZO
          }
          else
          {
            stmt.setString(15, anagAAEPAziendaVO.getSedeLegaleIndirizzoAAEP()); // SEDELEG_INDIRIZZO
          }
          stmt.setString(17, anagAAEPAziendaVO.getSedeLegaleCAPAAEP()); // SEDELEG_CAP
        }
        else
        {
          stmt.setString(13, anagVO.getMail()); // MAIL

          if (anagVO.getSedelegComune() != null
              && !anagVO.getSedelegComune().equals(""))
            stmt.setString(14, anagVO.getSedelegComune()); // SEDELEG_COMUNE
          else
            stmt.setString(14, anagVO.getSedelegEstero()); // SEDELEG_COMUNE

          stmt.setString(15, anagVO.getSedelegIndirizzo().toUpperCase()); // SEDELEG_INDIRIZZO
          stmt.setString(17, anagVO.getSedelegCAP()); // SEDELEG_CAP
        }

        if (anagVO.getDataCessazione() != null)
          stmt.setDate(18, new java.sql.Date(anagVO.getDataCessazione()
              .getTime())); // DATA_CESSAZIONE
        else
          stmt.setDate(18, null);
        stmt.setString(19, anagVO.getCausaleCessazione());// CAUSALE_CESSAZIONE

        stmt.setLong(20, idUtenteAggiornamento); // ID_UTENTE_AGGIORNAMENTO

        if (anagVO.getTipoAttivitaOTE() != null
            && anagVO.getTipoAttivitaOTE().getCode() != null)
          stmt.setLong(21, anagVO.getTipoAttivitaOTE().getCode().longValue()); // ID_ATTIVITA_OTE
        else
          stmt.setBigDecimal(21, null); // ID_ATTIVITA_OTE

        if (numeroRegistroImprese)
        {
          stmt.setString(22, anagAAEPAziendaVO.getNumeroRegistroImpreseAAEP()); // CCIAA_NUMERO_REGISTRO_IMPRESE
        }
        else
        {
          if (anagVO.getCCIAAnumRegImprese() != null) // CCIAA_NUMERO_REGISTRO_IMPRESE
            stmt.setString(22, anagVO.getCCIAAnumRegImprese().toUpperCase());
          else
            stmt.setString(22, null);
        }

        if (annoIscrizione)
          stmt.setString(23, anagAAEPAziendaVO.getAnnoIscrizioneAAEP()); // CCIAA_ANNO_ISCRIZIONE
        else
          stmt.setString(23, anagVO.getCCIAAannoIscrizione()); // CCIAA_ANNO_ISCRIZIONE

        stmt.setString(24, anagVO.getPosizioneSchedario()); // NUMERO_AGEA
        
        if (anagVO.getIdUde() != null)
          stmt.setLong(25, anagVO.getIdUde().longValue()); // ID_UDE
        else
          stmt.setBigDecimal(25, null);
        
        if (anagVO.getIdDimensioneAzienda() != null)
          stmt.setLong(26, anagVO.getIdDimensioneAzienda().longValue()); // ID_DIMENSIONE_AZIENDA
        else
          stmt.setBigDecimal(26, null);
        
        if (anagVO.getRls() != null)
          stmt.setBigDecimal(27, anagVO.getRls()); //RLS
        else
          stmt.setBigDecimal(27, null); // RLS
        /**
         * TO da modifcare quando viene aggiunto il campo
         */
        stmt.setBigDecimal(28, anagVO.getUlu()); // ULU
        stmt.setString(29, anagVO.getTelefono()); // TELEFONO
        stmt.setString(30, anagVO.getFax()); // FAX
        if(pec)
        {
          stmt.setString(31, anagAAEPAziendaVO.getPecAAEP()); // PEC
        }
        else
        {
          stmt.setString(31, anagVO.getPec()); // PEC
        }
        stmt.setString(32, anagVO.getCodiceAgriturismo()); // CODICE_AGRITURISMO
        
        stmt.setString(33, anagVO.getEsoneroPagamentoGF()); //ESONERO_PAGAMENTO_GF
        stmt.setString(34, anagVO.getIntestazionePartitaIva()); //INTESTAZIONE_PARTITA_IVA
        stmt.setTimestamp(35, convertDateToTimestamp(anagVO.getDataAggiornamentoUma())); //DATA_AGGIORNAMENTO_UMA
        stmt.setString(36, anagVO.getFlagIap()); //FLAG_IAP
        if(dataIscrizioneREA)
        {
          stmt.setTimestamp(37, convertDateToTimestamp(DateUtils.parseDateNull(anagAAEPAziendaVO.getDataIscrizioneREAAAEP()))); //DATA_ISCRIZIONE_REA
        }
        else
        {
          stmt.setTimestamp(37, convertDateToTimestamp(anagVO.getDataIscrizioneRea())); //DATA_ISCRIZIONE_REA
        }
        if(dataCancellazioneREA)
        {
          stmt.setTimestamp(38, convertDateToTimestamp(DateUtils.parseDateNull(anagAAEPAziendaVO.getDataCancellazioneREAAAEP()))); //DATA_CESSAZIONE_REA
        }
        else
        {
          stmt.setTimestamp(38, convertDateToTimestamp(anagVO.getDataCessazioneRea())); //DATA_CESSAZIONE_REA
        }
        if(dataIscrizioneRI)
        {
          stmt.setTimestamp(39, convertDateToTimestamp(DateUtils.parseDateNull(anagAAEPAziendaVO.getDataIscrizioneRIAAEP()))); //DATA_ISCRIZIONE_RI
        }
        else
        {
          stmt.setTimestamp(39, convertDateToTimestamp(anagVO.getDataIscrizioneRi())); //DATA_ISCRIZIONE_RI
        }
        
        stmt.setTimestamp(40, convertDateToTimestamp(anagVO.getDataCessazioneRi())); //DATA_CESSAZIONE_RI
        stmt.setTimestamp(41, convertDateToTimestamp(anagVO.getDataInizioAteco())); //DATA_INIZIO_ATECO

        SolmrLogger.debug(this, "Executing insert: " + insert);

        stmt.executeUpdate();

        stmt.close();
      }
      else
      {
        /***********************************************************************
         * Modifico il record sulla tabella db_anagrafica_azienda con gli stessi
         * valori del record storicizzato aggiornando con i nuovi valori solo le
         * colonne che l'utente vuole importare da AAEP (selezionate a video=
         */
        String update = "UPDATE DB_ANAGRAFICA_AZIENDA SET"
            + "  PARTITA_IVA = ? ,"
            + // 1
            "  DENOMINAZIONE = ? ,"
            + // 2
            "  ID_ATTIVITA_ATECO = ? ,"
            + // 3
            "  CCIAA_PROVINCIA_REA = ? ,"
            + // 4
            "  CCIAA_NUMERO_REA = ? ,"
            + // 5
            "  MAIL = ? ,"
            + // 6
            "  SEDELEG_COMUNE = ? ,"
            + // 7
            "  SEDELEG_INDIRIZZO = ? ,"
            + // 8
            "  SEDELEG_CAP = ? ,"
            + // 9
            "  DATA_AGGIORNAMENTO = SYSDATE ,"
            + "  ID_UTENTE_AGGIORNAMENTO = ? ," + // 10
            "  CCIAA_NUMERO_REGISTRO_IMPRESE = ? ," + // 11
            "  CCIAA_ANNO_ISCRIZIONE = ? ," + // 12
            "  ID_FORMA_GIURIDICA = ? , " + // 13
            "  ID_TIPOLOGIA_AZIENDA = ? , " + // 14
            "  PEC = ?, " + // 15
            "  DATA_ISCRIZIONE_REA = ?, " + //16
            "  DATA_CESSAZIONE_REA = ?, " + //17
            "  DATA_ISCRIZIONE_RI = ? " +  //18
            " WHERE ID_ANAGRAFICA_AZIENDA = ?"; // 19

        stmt = conn.prepareStatement(update);

        if (partitaIVA) // PARTITA_IVA
          stmt.setString(1, anagAAEPAziendaVO.getPartitaIVAAAEP());
        else
          stmt.setString(1, anagVO.getPartitaIVA());

        if (denominazione) // DENOMINAZIONE
        {
          String temp = AnagAAEPAziendaVO.eliminaSpazi(
              anagAAEPAziendaVO.getDenominazioneAAEP()).toUpperCase();
          if (temp.length() > 1000)
          {
            temp = temp.substring(0, 1000);
          }
          stmt.setString(2, temp);
        }
        else
        {
          stmt.setString(2, anagVO.getDenominazione().toUpperCase());
        }
        if (descrizioneAteco) // ID_ATTIVITà_ATECO
          if (idAttivitaAteco != null)
            stmt.setLong(3, idAttivitaAteco.longValue());
          else
            stmt.setBigDecimal(3, null);
        else
        {
          if (anagVO.getTipoAttivitaATECO() != null
              && anagVO.getTipoAttivitaATECO().getCode() != null)
            stmt
                .setLong(3, anagVO.getTipoAttivitaATECO().getCode().longValue());
          else
            stmt.setBigDecimal(3, null);
        }

        if (provinciaREA) // CCIAA_PROVINCIA_REA
        {
          if (anagAAEPAziendaVO.getProvinciaREAAAEP() != null
              && !anagAAEPAziendaVO.getProvinciaREAAAEP().equals(""))
            stmt.setString(4, anagAAEPAziendaVO.getProvinciaREAAAEP()
                .toUpperCase());
          else
            stmt.setString(4, null);
        }
        else
        {
          if (anagVO.getCCIAAprovREA() != null
              && !anagVO.getCCIAAprovREA().equals(""))
            stmt.setString(4, anagVO.getCCIAAprovREA().toUpperCase());
          else
            stmt.setString(4, null);
        }
        if (numeroREA) // CCIAA_NUMERO_REA
        {
          if (anagAAEPAziendaVO.getNumeroREAAAEP() != null)
            stmt.setLong(5, Long
                .parseLong(anagAAEPAziendaVO.getNumeroREAAAEP()));
          else
            stmt.setBigDecimal(5, null);
        }
        else
        {
          if (anagVO.getCCIAAnumeroREA() != null)
            stmt.setLong(5, anagVO.getCCIAAnumeroREA().longValue());
          else
            stmt.setBigDecimal(5, null);
        }

        if (sedeLegale)
        {
          stmt.setString(6, anagAAEPAziendaVO.getSedeLegaleEmailAAEP()); // MAIL
          stmt.setString(7, anagAAEPAziendaVO.getSedeLegaleIstatAAEP()); // SEDELEG_COMUNE
          stmt.setString(8, anagAAEPAziendaVO.getSedeLegaleIndirizzoAAEP()); // SEDELEG_INDIRIZZO
          stmt.setString(9, anagAAEPAziendaVO.getSedeLegaleCAPAAEP()); // SEDELEG_CAP
        }
        else
        {
          stmt.setString(6, anagVO.getMail()); // MAIL

          if (anagVO.getSedelegComune() != null
              && !anagVO.getSedelegComune().equals(""))
            stmt.setString(7, anagVO.getSedelegComune()); // SEDELEG_COMUNE
          else
            stmt.setString(7, anagVO.getSedelegEstero()); // SEDELEG_COMUNE

          stmt.setString(8, anagVO.getSedelegIndirizzo().toUpperCase()); // SEDELEG_INDIRIZZO
          stmt.setString(9, anagVO.getSedelegCAP()); // SEDELEG_CAP
        }

        stmt.setLong(10, idUtenteAggiornamento); // ID_UTENTE_AGGIORNAMENTO

        if (numeroRegistroImprese)
        {
          stmt.setString(11, anagAAEPAziendaVO.getNumeroRegistroImpreseAAEP()); // CCIAA_NUMERO_REGISTRO_IMPRESE
        }
        else
        {
          if (anagVO.getCCIAAnumRegImprese() != null) // CCIAA_NUMERO_REGISTRO_IMPRESE
            stmt.setString(11, anagVO.getCCIAAnumRegImprese().toUpperCase());
          else
            stmt.setString(11, null);
        }

        if (annoIscrizione)
          stmt.setString(12, anagAAEPAziendaVO.getAnnoIscrizioneAAEP()); // CCIAA_ANNO_ISCRIZIONE
        else
          stmt.setString(12, anagVO.getCCIAAannoIscrizione()); // CCIAA_ANNO_ISCRIZIONE

        try
        {
          if (formaGiuridica)
            stmt.setLong(13, idFormaGiuridica.longValue()); // ID_FORMA_GIURIDICA
          else
            stmt.setLong(13, anagVO.getTipoFormaGiuridica().getCode()
                .longValue()); // ID_FORMA_GIURIDICA
        }
        catch (Exception e)
        {
          stmt.setBigDecimal(13, null); // ID_FORMA_GIURIDICA
        }
        
        //Aggiunta GAA06-01 Visura AAEP - fonte infoCamere - v2.doc
        //**************************************************
        if(formaGiuridica)
        {
          if(flagTipoAziendaFormaGiuridica)
          {
            if (anagVO.getTipoTipologiaAzienda() != null
                && anagVO.getTipoTipologiaAzienda().getCode() != null)
              stmt.setLong(14, anagVO.getTipoTipologiaAzienda().getCode()
                  .longValue()); // ID_TIPOLOGIA_AZIENDA
            else
              stmt.setBigDecimal(14, null); // ID_TIPOLOGIA_AZIENDA
          }
          else //Non è stata trovata relazione tra id_tipologia_azienda corrente con id_forma_giuridica AAEP
          {
            if(listIdTipoAz != null)
            {
              stmt.setLong(14, ((Long)listIdTipoAz.get(0)).longValue() ); // ID_TIPOLOGIA_AZIENDA
            }
            else
            {
              if (anagVO.getTipoTipologiaAzienda() != null
                  && anagVO.getTipoTipologiaAzienda().getCode() != null)
                stmt.setLong(14, anagVO.getTipoTipologiaAzienda().getCode()
                    .longValue()); // ID_TIPOLOGIA_AZIENDA
              else
                stmt.setBigDecimal(14, null); // ID_TIPOLOGIA_AZIENDA
            }
          }
        }
        else
        {
          if (anagVO.getTipoTipologiaAzienda() != null
              && anagVO.getTipoTipologiaAzienda().getCode() != null)
            stmt.setLong(14, anagVO.getTipoTipologiaAzienda().getCode()
                .longValue()); // ID_TIPOLOGIA_AZIENDA
          else
            stmt.setBigDecimal(14, null); // ID_TIPOLOGIA_AZIENDA
        }
        
        if (pec) // PEC
          stmt.setString(15, anagAAEPAziendaVO.getPecAAEP());
        else
          stmt.setString(15, anagVO.getPec());
        
        if(dataIscrizioneREA)
        {
          stmt.setTimestamp(16, convertDateToTimestamp(DateUtils.parseDateNull(anagAAEPAziendaVO.getDataIscrizioneREAAAEP()))); //DATA_ISCRIZIONE_REA
        }
        else
        {
          stmt.setTimestamp(16, convertDateToTimestamp(anagVO.getDataIscrizioneRea())); //DATA_ISCRIZIONE_REA
        }
        if(dataCancellazioneREA)
        {
          stmt.setTimestamp(17, convertDateToTimestamp(DateUtils.parseDateNull(anagAAEPAziendaVO.getDataCancellazioneREAAAEP()))); //DATA_CESSAZIONE_REA
        }
        else
        {
          stmt.setTimestamp(17, convertDateToTimestamp(anagVO.getDataCessazioneRea())); //DATA_CESSAZIONE_REA
        }
        if(dataIscrizioneRI)
        {
          stmt.setTimestamp(18, convertDateToTimestamp(DateUtils.parseDateNull(anagAAEPAziendaVO.getDataIscrizioneRIAAEP()))); //DATA_ISCRIZIONE_RI
        }
        else
        {
          stmt.setTimestamp(18, convertDateToTimestamp(anagVO.getDataIscrizioneRi())); //DATA_ISCRIZIONE_RI
        }
        
        //**********************************************

        stmt.setLong(19, anagVO.getIdAnagAzienda().longValue()); // ID_ANAGRAFICA_AZIENDA

        SolmrLogger.debug(this, "Executing update: " + update);

        stmt.executeUpdate();

        stmt.close();
      }

      SolmrLogger.debug(this, "Executed.");
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
    return idAnagAzienda;
  }

  public void updateDataAzienda(Long primaryKey, java.util.Date data)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String update = "UPDATE DB_AZIENDA " + "   SET DATA_INIZIO_VALIDITA = ? "
          + " WHERE ID_AZIENDA = ? ";
      stmt = conn.prepareStatement(update);
      stmt.setDate(1, new java.sql.Date(data.getTime()));
      stmt.setLong(2, primaryKey.longValue());

      SolmrLogger.debug(this, "Executing updateDataAzienda: " + update);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed updateDataAzienda.");
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
  }

  // Metodo per effettuare la modifica del record su DB_CONTITOLARE. Utilizzato
  // in modifica
  // soggetto collegato per modificare il ruolo.Da non toccare in nessun caso
  // senza prima aver
  // chiesto a ME(MAURO VOCALE).Se lo toccate vi ammazzo.
  // Viene usato id_soggetto come parametro per poter utilizzare il metodo
  // durante la bonifica
  // delle persone fisiche.
  public void updateContitolareRuolo(PersonaFisicaVO pfVO)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {

      conn = getDatasource().getConnection();

      String update = " UPDATE DB_CONTITOLARE " 
                    + " SET    ID_RUOLO = ?, "          
                    + "        DATA_INIZIO_RUOLO_MOD = ?, " 
                    + "        DATA_FINE_RUOLO_MOD = ?, " ;         
      if (pfVO.getDataFineRuoloMod() != null)
        update+= "        DATA_FINE_RUOLO = TRUNC(SYSDATE), ";          
          update+= "        ID_SOGGETTO = ? " 
                    + " WHERE  ID_CONTITOLARE = ? ";

      stmt = conn.prepareStatement(update);
      stmt.setLong(1, pfVO.getIdRuolo().longValue());
      stmt.setDate(2, new java.sql.Date(pfVO.getDataInizioRuoloMod().getTime()));
      if (pfVO.getDataFineRuoloMod() != null)
        stmt.setDate(3, new java.sql.Date(pfVO.getDataFineRuoloMod().getTime()));
      else
        stmt.setDate(3, null);
      stmt.setLong(4, pfVO.getIdSoggetto().longValue());
      stmt.setLong(5, pfVO.getIdContitolare().longValue());

      SolmrLogger.debug(this, "Executing query updateContitolareRuolo: "
          + update);

      stmt.executeUpdate();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in updateContitolareRuolo: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in updateContitolareRuolo: "
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
                "SQLException while closing Statement and Connection in updateContitolareRuolo: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in updateContitolareRuolo: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  

  /**
   * Metodo per richiamare la procedura plsql per i controlli sulla consistenza
   * Il parametro restituito può assumere tre valori: - N: richiedere la
   * motivazione della dichiarazione e permettere il salvataggio della
   * dichiarazione di consistenza - E: visualizzare gli errori in ordine di
   * argomento e non permettere il proseguimento - A: visualizzare le anomalie,
   * richiedere la motivazione della dichiarazione e permettere il salvataggio
   * 
   * @param aaVO
   *          AnagAziendaVO
   * @throws DataAccessException
   * @throws SolmrException
   */
  public void ribaltamentoPLQSL(AnagAziendaVO aaVO) throws DataAccessException,
      SolmrException
  {
    Connection conn = null;
    CallableStatement cs = null;
    try
    {
      /*************************************************************************
       * PROCEDURE RIBALTAMENTO_CONSISTENZA(P_ID_AZIENDA_NEW IN
       * DB_AZIENDA.ID_AZIENDA%TYPE, P_ID_AZIENDA_PROV IN
       * DB_AZIENDA.ID_AZIENDA%TYPE, P_MSGERR IN OUT VARCHAR2, P_CODERR IN OUT
       * VARCHAR2)IS
       */

      conn = getDatasource().getConnection();
      String sql = "{call PACK_RIBALTAMENTO_CONSISTENZA.RIBALTAMENTO_CONSISTENZA(?,?,?,?)}";
      cs = conn.prepareCall(sql);
      cs.setLong(1, aaVO.getIdAzienda().longValue());
      if (aaVO.getIdAziendaSubentro() != null)
      {
        cs.setLong(2, aaVO.getIdAziendaSubentro().longValue());
      }
      else if (aaVO.getIdAziendaProvenienza() != null)
      {
        cs.setLong(2, aaVO.getIdAziendaProvenienza().longValue());
      }
      cs.registerOutParameter(3, Types.VARCHAR);
      cs.registerOutParameter(4, Types.VARCHAR);

      cs.executeUpdate();
      String msgErr = cs.getString(3);
      String errorCode = cs.getString(4);

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
      SolmrLogger.fatal(this, "ribaltamentoPLQSL - SQLException: "
          + sqle.getMessage());
      throw new DataAccessException(sqle.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "ribaltamentoPLQSL - Generic Exception: "
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
        SolmrLogger.fatal(this,
            "ribaltamentoPLQSL - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "ribaltamentoPLQSL - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  /**
   * Dato un ID_AZIENDA inserisco tutti gli utilizzi e le condizioni reperite
   * tramite il CUAA su TMP_MIGRAZIONE_H_P2 nelle tavole
   * DB_CONDUZIONE_PARTICELLA e DB_UTILIZZO_PARTICELLA
   * 
   * @param idAzienda
   *          long
   * @throws DataAccessException
   * @throws SolmrException
   */
  public void ribaltamentoConsistenzaPLQSL(long idAzienda)
      throws DataAccessException, SolmrException
  {
    Connection conn = null;
    CallableStatement cs = null;
    try
    {
      /**
       * PROCEDURE MAIN (pIdAzienda IN DB_ANAGRAFICA_AZIENDA.ID_AZIENDA%TYPE,
       * pCodErr IN OUT VARCHAR2, pMsgErr IN OUT VARCHAR2);
       * 
       */
      conn = getDatasource().getConnection();
      String sql = "{call PCK_CARICA_MISURA_H.MAIN(?,?,?)}";
      cs = conn.prepareCall(sql);
      cs.setLong(1, idAzienda);
      cs.registerOutParameter(2, Types.VARCHAR);
      cs.registerOutParameter(3, Types.VARCHAR);

      cs.executeUpdate();
      String msgErr = cs.getString(2);
      String errorCode = cs.getString(3);

      if (!(errorCode == null || "".equals(errorCode)))
        throw new SolmrException(AnagErrors.ERR_PLSQL_PCK_CARICA_MISURA_H
            + errorCode + " - " + msgErr);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    catch (SQLException sqle)
    {
      SolmrLogger.fatal(this, "ribaltamentoConsistenzaPLQSL - SQLException: "
          + sqle.getMessage());
      throw new DataAccessException(sqle.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "ribaltamentoConsistenzaPLQSL - Generic Exception: "
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
                "ribaltamentoConsistenzaPLQSL - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "ribaltamentoConsistenzaPLQSL - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per effettuare la storicizzazione della delega
  /*public void storicizzaDelega(DelegaVO dVO, long idUtenteAggiornamento)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String storicizza = "UPDATE DB_DELEGA "
          + "   SET DATA_FINE = TRUNC(SYSDATE), ID_UTENTE_FINE_DELEGA = ? "
          + " WHERE ID_DELEGA = ? ";

      stmt = conn.prepareStatement(storicizza);

      stmt.setLong(1, idUtenteAggiornamento);
      stmt.setLong(2, dVO.getIdDelega().longValue());

      SolmrLogger.debug(this, "Executing storicizza delega: " + storicizza);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed storicizza delega.");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in storicizza delega: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in storicizza delega: "
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
            "SQLException while closing Statement and Connection in inserisci delega: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in inserisci delega: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }*/


  // Metodo per effettuare la "bonifica" su DB_AZIENDA
  public void bonificaDBAzienda(Long idAzienda) throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {

      conn = getDatasource().getConnection();
      String update = "update db_azienda "
          + " set data_inizio_validita = trunc(sysdate), "
          + " flag_bonifica_dati = ? " + " where id_azienda = ? ";

      stmt = conn.prepareStatement(update);
      stmt.setLong(1, 0);
      stmt.setLong(2, idAzienda.longValue());

      SolmrLogger
          .debug(this, "Executing query in bonificaDBAzienda: " + update);

      stmt.executeUpdate();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in bonificaDBAzienda: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in bonificaDBAzienda: "
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
            "SQLException while closing Statement and Connection in bonificaDBAzienda: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in bonificaDBAzienda: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per controllare se una persona è legata con un ruolo diverso da
  // titolare/rappresentante legale
  // ad una azienda agricola
  public boolean hasRuoloInAzienda(Long idAzienda, Long idSoggetto)
      throws DataAccessException
  {

    boolean result = false;
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try
    {

      conn = getDatasource().getConnection();
      String update = " SELECT ID_CONTITOLARE FROM DB_CONTITOLARE WHERE ID_SOGGETTO = ? "
          + " AND ID_RUOLO != ? AND ID_AZIENDA = ? ";

      stmt = conn.prepareStatement(update);
      stmt.setLong(1, idSoggetto.longValue());
      stmt.setString(2, SolmrConstants.TIPORUOLO_TITOL_RAPPR_LEG);
      stmt.setLong(3, idAzienda.longValue());

      SolmrLogger
          .debug(this, "Executing query in hasRuoloInAzienda: " + update);

      rs = stmt.executeQuery();

      String idContitolare = null;
      if (rs.next())
      {
        idContitolare = rs.getString(1);
      }
      if (idContitolare != null)
      {
        result = true;
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in hasRuoloInAzienda: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in hasRuoloInAzienda: "
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
            "SQLException while closing Statement and Connection in bonificaDBAzienda: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in bonificaDBAzienda: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // Il metodo si occupa di far cessare tutti i legami diversi da
  // titolare/rappresentante legale
  // con l'azienda selezionata
  public void cessaRuoloNoTitolare(AnagAziendaVO anagAziendaVO,
      PersonaFisicaVO personaFisicaVO) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String update = "UPDATE DB_CONTITOLARE "
          + "SET DATA_FINE_RUOLO = trunc(SYSDATE) "
          + "WHERE ID_AZIENDA= ? AND " + "ID_SOGGETTO = ? AND "
          + "ID_RUOLO != ? AND " + "DATA_FINE_RUOLO IS NULL";

      stmt = conn.prepareStatement(update);

      stmt.setLong(1, anagAziendaVO.getIdAzienda().longValue());
      stmt.setLong(2, personaFisicaVO.getIdSoggetto().longValue());
      stmt.setString(3, SolmrConstants.TIPORUOLO_TITOL_RAPPR_LEG);

      SolmrLogger.debug(this, "Executing query in cessaRuoloNoTitolare: "
          + update);

      stmt.executeUpdate();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in cessaRuoloNoTitolare: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in cessaRuoloNoTitolare: "
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
            "SQLException while closing Statement and Connection in cessaRuoloNoTitolare: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in cessaRuoloNoTitolare: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  public PersonaFisicaVO fillAnagWithProvAndCom(PersonaFisicaVO result)
      throws DataAccessException, NotFoundException, SolmrException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT c.descom descom, p.descrizione descrizione, "
          + "       p.sigla_provincia sigla, c.flag_estero flag "
          + "  FROM Comune c, Provincia p "
          + " WHERE c.istat_provincia = p.istat_provincia "
          + "   AND c.istat_comune = ? ";
      if (result.getNascitaComune() != null
          && !result.getNascitaComune().equals(""))
      {
        stmt = conn.prepareStatement(search);

        SolmrLogger.debug(this, "Executing query: " + search);

        stmt.setString(1, result.getNascitaComune());

        ResultSet rs = stmt.executeQuery();
        String flagEstero1 = "";
        String aliasDescom1 = "";
        String siglaProv1 = "";
        if (rs != null)
        {
          while (rs.next())
          {
            aliasDescom1 = rs.getString("descom");
            result.setDescNascitaComune(aliasDescom1);
            siglaProv1 = rs.getString("sigla");
            flagEstero1 = rs.getString("flag");

            result.setNascitaProv(siglaProv1);
            if (flagEstero1.equals(SolmrConstants.FLAG_S))
            {
              result.setLuogoNascita(aliasDescom1);
            }
            else
            {
              result.setLuogoNascita(aliasDescom1 + " (" + siglaProv1 + ")");
              result.setNascitaCittaEstero("");
            }
          }
        }
        else
          throw new DataAccessException();

        rs.close();

      }
      if (result.getResComune() != null && !result.getResComune().equals(""))
      {
        stmt = conn.prepareStatement(search);

        SolmrLogger.debug(this, "Executing query: " + search);

        stmt.setString(1, result.getResComune());

        ResultSet rs = stmt.executeQuery();
        String flagEstero2 = "";
        String aliasDescom2 = "";
        String siglaProv2 = "";
        String descProv2 = "";

        if (rs != null)
        {
          while (rs.next())
          {
            aliasDescom2 = rs.getString("descom");
            result.setDescResComune(aliasDescom2);
            siglaProv2 = rs.getString("sigla");
            flagEstero2 = rs.getString("flag");
            descProv2 = rs.getString("descrizione");

            if (flagEstero2.equals(SolmrConstants.FLAG_S))
            {
              result.setDescResProvincia("");
              result.setDescResComune("");
              result.setResCAP("");
              result.setResProvincia("");
              result.setStatoEsteroRes(aliasDescom2);
            }
            else
            {
              result.setDescResProvincia(descProv2);
              result.setDescResComune(aliasDescom2);
              result.setResProvincia(siglaProv2);
              result.setStatoEsteroRes("");
              result.setResCittaEstero("");
            }
          }
        }
        else
          throw new DataAccessException();

        rs.close();
        stmt.close();
      }

      SolmrLogger.debug(this, "Executed fill.");
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

  /*private String checkIsIntermediarioCorrect(String cf)
      throws DataAccessException, SolmrException
  {
    String result = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = "SELECT denominazione " + "  FROM Db_Intermediario "
          + " WHERE codice_fiscale = ? ";
      stmt = conn.prepareStatement(search);

      stmt.setString(1, cf);

      if (SolmrLogger.isDebugEnabled(this))
        SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs != null)
      {
        while (rs.next())
          result = rs.getString(1);
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      if (result == null)
        throw new SolmrException(SolmrErrors.INTERMEDIARIO_SCONOSCIUTO + " ("
            + cf + ")");

      SolmrLogger.debug(this, "Executed checkIsIntermediarioCorrect.");
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
    catch (SolmrException nfexc)
    {
      SolmrLogger.fatal(this, "SolmrException: " + nfexc.getMessage());
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
  }*/

  // Metodo per controllare se esiste una delega in relazione ad un
  // intermediario
  public Long esisteDelegaAziendaByIntermediario(Long idAzienda)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Long result = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT ID_DELEGA " + "  FROM DB_DELEGA D "
          + " WHERE ID_AZIENDA = ? " + "   AND ID_PROCEDIMENTO = "
          + SolmrConstants.ID_TIPO_PROCEDIMENTO_UMA
          + "   AND DATA_FINE IS NULL FOR UPDATE ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setLong(1, idAzienda.longValue());
      ResultSet rs = stmt.executeQuery();
      long i = 0;
      if (rs != null)
      {
        while (rs.next())
        {
          // result = new Long(rs.getInt(1));
          i++;
        }
        result = new Long(i);
      }
    }

    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "esisteDelegaAziendaIntermediario - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "esisteDelegaAziendaIntermediario - Generic Exception: " + ex);
      // ex.printStackTrace(System.out);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
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
                "esisteDelegaAziendaIntermediario - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "esisteDelegaAziendaIntermediario - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // Metodo per controllare se esiste una delega in relazione ad un
  // intermediario
  public void lockDelegaAzienda(Long idAzienda) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT ID_DELEGA " + "  FROM DB_DELEGA D "
          + " WHERE ID_AZIENDA = ? " + "   AND ID_PROCEDIMENTO = "
          + SolmrConstants.ID_TIPO_PROCEDIMENTO_UMA + "   FOR UPDATE ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt.setLong(1, idAzienda.longValue());
    }

    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "esisteDelegaAziendaIntermediario - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "esisteDelegaAziendaIntermediario - Generic Exception: " + ex);
      // ex.printStackTrace(System.out);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
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
                "esisteDelegaAziendaIntermediario - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "esisteDelegaAziendaIntermediario - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per cancellare una delega
  public void deleteDelega(Long idDelega) throws DataAccessException,
      SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String delete = "DELETE DB_DELEGA " + " WHERE ID_DELEGA = ? ";

      stmt = conn.prepareStatement(delete);
      stmt.setLong(1, idDelega.longValue());
      SolmrLogger.debug(this, "Executing insert delega: " + delete);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insert delega.");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in delete delega: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in delete delega: "
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
            "SQLException while closing Statement and Connection in delete delega: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection in delete delega: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  public Vector<AnagAziendaVO> getListaStoricoAzienda(Long idAzienda)
      throws DataAccessException, SolmrException
  {

    Vector<AnagAziendaVO> result = new Vector<AnagAziendaVO>();
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = 
        "SELECT AA.ID_ANAGRAFICA_AZIENDA, " +
        "       AA.DATA_INIZIO_VALIDITA, "
          + "   AA.DATA_FINE_VALIDITA, "
          + "   AA.MOTIVO_MODIFICA, " +
          "     AA.ID_UTENTE_AGGIORNAMENTO, " +
          "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
          "          || ' ' " + 
          "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
          "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
          "         WHERE AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
          "     AS DENOMINAZIONE, " +
          " (SELECT PVU.DENOMINAZIONE " +
          " FROM PAPUA_V_UTENTE_LOGIN PVU " +
          " WHERE AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
          "     AS DESCRIZIONE_ENTE_APPARTENENZA," +
          "     AA.CUAA " +
          " FROM DB_ANAGRAFICA_AZIENDA AA " +
        //  "   PAPUA_V_UTENTE_LOGIN PVU "
          " WHERE AA.ID_AZIENDA = ? ";
         // + " AND AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idAzienda.longValue());

      SolmrLogger.debug(this, "Executing query: " + query);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        AnagAziendaVO anagAziendaVO = new AnagAziendaVO();
        anagAziendaVO.setIdAnagAzienda(Long.decode(rs.getString(1)));
        anagAziendaVO.setDataInizioVal(rs.getDate(2));
        anagAziendaVO.setDataFineVal(rs.getDate(3));
        anagAziendaVO.setMotivoModifica(rs.getString(4));
        anagAziendaVO.setIdUtenteAggiornamento(Long.decode(rs.getString(5)));
        anagAziendaVO.setDescrizioneUtenteModifica(rs.getString(6));
        anagAziendaVO.setDescrizioneEnteUtenteModifica(rs.getString(7));
        anagAziendaVO.setCUAA(rs.getString(8));
        result.add(anagAziendaVO);
      }

      if (result.size() == 0)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_NO_STORICO_AZIENDA"));
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found " + result.size()
          + " result(s).");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getListaStoricoAzienda - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (SolmrException exc)
    {
      SolmrLogger.fatal(this, "getListaStoricoAzienda - SolmrException: "
          + exc.getMessage());
      throw exc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getListaStoricoAzienda - Generic Exception: "
          + ex + " with message: " + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
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
                "getListaStoricoAzienda - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getListaStoricoAzienda - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // Metodo per effettuare la sostituzione del vecchio soggetto con il nuovo in
  // tutti i legami tra aziende
  // e persona fisiche
  public void changeLegameBetweenPersoneAndAziende(Long newIdSoggetto,
      Long oldIdSoggetto, Long idAzienda) throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " UPDATE DB_CONTITOLARE SET ID_SOGGETTO = ? "
          + " WHERE  ID_SOGGETTO = ? ";
      
      if (idAzienda!=null)
    	  query += " AND    ID_AZIENDA = ? ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, newIdSoggetto.longValue());
      stmt.setLong(2, oldIdSoggetto.longValue());
      if (idAzienda!=null)
    	  stmt.setLong(3, idAzienda.longValue());

      SolmrLogger.debug(this,
          "Executing query changeLegameBetweenPersoneAndAziende: " + query);

      ResultSet rs = stmt.executeQuery();

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "changeLegameBetweenPersoneAndAziende - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "changeLegameBetweenPersoneAndAziende - Generic Exception: " + ex
              + " with message: " + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
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
                "changeLegameBetweenPersoneAndAziende - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "changeLegameBetweenPersoneAndAziende - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per cessare il legame tra una persona e l'azienda
  public void cessaLegameBetweenPersonaAndAzienda(Long idSoggetto,
      Long idAzienda) throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " UPDATE DB_CONTITOLARE SET DATA_FINE_RUOLO = TRUNC(SYSDATE) "
          + " WHERE  ID_SOGGETTO = ? " + " AND    ID_AZIENDA = ? ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idSoggetto.longValue());
      stmt.setLong(2, idAzienda.longValue());

      SolmrLogger.debug(this,
          "Executing query cessaLegameBetweenPersonaAndAzienda: " + query);

      ResultSet rs = stmt.executeQuery();

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "cessaLegameBetweenPersonaAndAzienda - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "cessaLegameBetweenPersonaAndAzienda - Generic Exception: " + ex
              + " with message: " + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
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
                "cessaLegameBetweenPersonaAndAzienda - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "cessaLegameBetweenPersonaAndAzienda - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per recuperare il flag che indica se è possibile effettuare la
  // modifica degli usi del suolo
  // relativa all'azienda agricola selezionata
  public String getFlagVariazioneUtilizziAmmessa(Long idAzienda)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    String flagVariazioneUtilizziAmmessa = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT VARIAZIONE_UTILIZZI_AMMESSA "
          + " FROM   DB_AZIENDA " + " WHERE  ID_AZIENDA = ? ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idAzienda.longValue());

      SolmrLogger.debug(this,
          "Executing query getFlagVariazioneUtilizziAmmessa: " + query);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        flagVariazioneUtilizziAmmessa = rs.getString(1);
        if (!Validator.isNotEmpty(flagVariazioneUtilizziAmmessa))
        {
          flagVariazioneUtilizziAmmessa = SolmrConstants.FLAG_N;
        }
      }

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getFlagVariazioneUtilizziAmmessa - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getFlagVariazioneUtilizziAmmessa - Generic Exception: " + ex
              + " with message: " + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
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
                "getFlagVariazioneUtilizziAmmessa - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getFlagVariazioneUtilizziAmmessa - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return flagVariazioneUtilizziAmmessa;
  }

  // il metodo seguente viene usato per far passare un'azienda da provvisoria
  // a non più provvisoria
  public void updateInsediamentoGiovani(Long idAzienda)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String update = "UPDATE DB_AZIENDA "
          + "SET FLAG_AZIENDA_PROVVISORIA = 'N', "
          + "DATA_INSEDIAMENTO = SYSDATE " + "WHERE ID_AZIENDA = ? ";

      stmt = conn.prepareStatement(update);

      stmt.setLong(1, idAzienda.longValue());

      SolmrLogger.debug(this, "Executing query: " + update);

      stmt.executeUpdate();
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
  }

  // Metodo per recuperare recuperare l'ufficio zona intermediario partendo
  // dalla chiave primaria
  public UfficioZonaIntermediarioVO findUfficioZonaIntermediarioVOByPrimaryKey(
      Long idUfficioZonaIntermediario) throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    UfficioZonaIntermediarioVO ufficioZonaIntermediarioVO = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT UZI.ID_UFFICIO_ZONA_INTERMEDIARIO, "
          + "        UZI.ID_INTERMEDIARIO, " + "        UZI.DENOMINAZIONE, "
          + "        UZI.CODICE_AGEA, " + "        UZI.INDIRIZZO, "
          + "        UZI.CAP, " + "        UZI.COMUNE, " + "        C.DESCOM, "
          + "        UZI.RECAPITO, " + "        P.SIGLA_PROVINCIA "
          + " FROM   DB_UFFICIO_ZONA_INTERMEDIARIO UZI, "
          + "        COMUNE C, " + "        PROVINCIA P "
          + " WHERE  UZI.ID_UFFICIO_ZONA_INTERMEDIARIO = ? "
          + " AND    UZI.COMUNE = C.ISTAT_COMUNE "
          + " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idUfficioZonaIntermediario.longValue());

      SolmrLogger.debug(this,
          "Executing query findUfficioZonaIntermediarioVOByPrimaryKey: "
              + query);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        ufficioZonaIntermediarioVO = new UfficioZonaIntermediarioVO();
        ufficioZonaIntermediarioVO.setIdUfficioZonaIntermediario(new Long(rs
            .getLong(1)));
        ufficioZonaIntermediarioVO.setIdIntermediario(new Long(rs.getLong(2)));
        ufficioZonaIntermediarioVO.setDenominazione(rs.getString(3));
        ufficioZonaIntermediarioVO.setCodiceAgea(rs.getString(4));
        ufficioZonaIntermediarioVO.setIndirizzo(rs.getString(5));
        ufficioZonaIntermediarioVO.setCap(rs.getString(6));
        ufficioZonaIntermediarioVO.setIstatComune(rs.getString(7));
        ufficioZonaIntermediarioVO.setDescrizioneComune(rs.getString(8));
        ufficioZonaIntermediarioVO.setRecapito(rs.getString(9));
        ufficioZonaIntermediarioVO.setSiglaProvincia(rs.getString(10));
      }

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "findUfficioZonaIntermediarioVOByPrimaryKey - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "findUfficioZonaIntermediarioVOByPrimaryKey - Generic Exception: "
              + ex + " with message: " + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
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
                "findUfficioZonaIntermediarioVOByPrimaryKey - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "findUfficioZonaIntermediarioVOByPrimaryKey - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return ufficioZonaIntermediarioVO;
  }

  /**
   * Metodo per recuperare l'elenco delle aziende in relazione all'intermediario
   * delegato e allo stato del fascicolo
   * 
   * @param delegaVO
   *          DelegaVO
   * @return Vector
   * @throws DataAccessException
   * @throws SolmrException
   */
  public Vector<AnagAziendaVO> getElencoAziendeByCAA(DelegaVO delegaVO)
      throws DataAccessException, SolmrException
  {
    SolmrLogger.debug(this,
        "Invocating method getElencoAziendeByCAA in AnagWriteDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<AnagAziendaVO> elencoAziende = null;

    try
    {
      SolmrLogger
          .debug(this,
              "Creating connection in method getElencoAziendeByCAA in AnagWriteDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created connection in method getElencoAziendeByCAA in AnagWriteDAO with value: "
                  + conn + "\n");

      String query = "";

      SolmrLogger
          .debug(
              this,
              "Value of parameter [STATO_RICERCA_FASCICOLO] in method getElencoAziendeByCAA in AnagWriteDAO: "
                  + delegaVO.getStatoRicercaFascicolo() + "\n");

      // Caso di ricerca per tutti i fascicoli
      if (!Validator.isNotEmpty(delegaVO.getStatoRicercaFascicolo())
          || delegaVO.getStatoRicercaFascicolo().equalsIgnoreCase(
              (String) SolmrConstants.get("STATO_FASCICOLO_TUTTI")))
      {

        SolmrLogger
            .debug(
                this,
                "Creating query for a case of tutti i fascicoli in method getElencoAziendeByCAA in AnagWriteDAO\n");

        query = " SELECT   AA.ID_AZIENDA, " + "          AA.DENOMINAZIONE, "
            + "          AA.CUAA, " + "          AA.PARTITA_IVA, "
            + "          C.DESCOM, " + "          AA.SEDELEG_CITTA_ESTERO, "
            + "          AA.SEDELEG_INDIRIZZO, " + "          AA.SEDELEG_CAP, "
            + "          P.SIGLA_PROVINCIA, " + "          AA.SEDELEG_COMUNE "
            + " FROM     DB_ANAGRAFICA_AZIENDA AA, "
            + "          DB_DELEGA D, " + "          COMUNE C, "
            + "          PROVINCIA P "
            + " WHERE    AA.DATA_FINE_VALIDITA IS NULL "
            + " AND AA.DATA_CESSAZIONE IS NULL "
            + " AND      AA.ID_AZIENDA = D.ID_AZIENDA "
            + " AND      D.DATA_FINE IS NULL "
            + " AND      D.ID_PROCEDIMENTO = ? "
            + " AND      D.ID_INTERMEDIARIO = ? "
            + " AND      AA.SEDELEG_COMUNE = C.ISTAT_COMUNE "
            + " AND      P.ISTAT_PROVINCIA = C.ISTAT_PROVINCIA "
            + " ORDER BY AA.DENOMINAZIONE, " + "          AA.CUAA, "
            + "          AA.PARTITA_IVA ";

        SolmrLogger
            .debug(
                this,
                "Created query for a case of tutti i fascicoli in method getElencoAziendeByCAA in AnagWriteDAO\n");

      }
      // Caso di ricerca per piano colturale incompleto
      else if (delegaVO.getStatoRicercaFascicolo().equalsIgnoreCase(
          (String) SolmrConstants
              .get("STATO_FASCICOLO_PIANO_COLTURALE_INCOMPLETO")))
      {

        SolmrLogger
            .debug(
                this,
                "Creating query for a case of piano colturale incompleto in method getElencoAziendeByCAA in AnagWriteDAO\n");

        query = " SELECT  AA.ID_AZIENDA, "
            + "         AA.DENOMINAZIONE, "
            + "         AA.CUAA, "
            + "         AA.PARTITA_IVA, "
            + "         C.DESCOM, "
            + "         AA.SEDELEG_CITTA_ESTERO, "
            + "         AA.SEDELEG_INDIRIZZO, "
            + "         AA.SEDELEG_CAP, "
            + "         P.SIGLA_PROVINCIA, "
            + "         AA.SEDELEG_COMUNE "
            + " FROM    DB_ANAGRAFICA_AZIENDA AA, "
            + "         DB_DELEGA D, "
            + "         COMUNE C, "
            + "         PROVINCIA P "
            + " WHERE   AA.DATA_FINE_VALIDITA IS NULL "
            + " AND     AA.ID_AZIENDA = D.ID_AZIENDA "
            + " AND     D.DATA_FINE IS NULL "
            + " AND     D.ID_PROCEDIMENTO = ? "
            + " AND     D.ID_INTERMEDIARIO = ? "
            + " AND     AA.SEDELEG_COMUNE = C.ISTAT_COMUNE "
            + " AND     P.ISTAT_PROVINCIA = C.ISTAT_PROVINCIA "
            + " AND     AA.DATA_CESSAZIONE IS NULL "
            + " AND     (SELECT SUM(CP.SUPERFICIE_CONDOTTA) "
            + "          FROM   DB_CONDUZIONE_PARTICELLA CP, "
            + "                 DB_UTE U "
            + "          WHERE  CP.DATA_FINE_CONDUZIONE IS NULL "
            + "          AND    CP.ID_UTE = U.ID_UTE "
            + "          AND    U.ID_AZIENDA = AA.ID_AZIENDA) "
            + "          > "
            + "          (SELECT SUM(UP.SUPERFICIE_UTILIZZATA) "
            + "           FROM   DB_UTILIZZO_PARTICELLA UP, "
            + "                  DB_CONDUZIONE_PARTICELLA CP, "
            + "                  DB_UTE U "
            + "           WHERE  UP.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA "
            + "           AND    CP.DATA_FINE_CONDUZIONE IS NULL "
            + "           AND    CP.ID_UTE = U.ID_UTE "
            + "           AND U.ID_AZIENDA = AA.ID_AZIENDA) "
            + " ORDER BY  AA.DENOMINAZIONE, " + "           AA.CUAA, "
            + "           AA.PARTITA_IVA";

        SolmrLogger
            .debug(
                this,
                "Created query for a case of piano colturale incompleto in method getElencoAziendeByCAA in AnagWriteDAO\n");

      }
      // Caso di ricerca per fascicolo da validare
      else if (delegaVO.getStatoRicercaFascicolo().equalsIgnoreCase(
          (String) SolmrConstants.get("STATO_FASCICOLO_DA_VALIDARE")))
      {

        SolmrLogger
            .debug(
                this,
                "Creating query for a case of fascicoli da validare in method getElencoAziendeByCAA in AnagWriteDAO\n");

        query = " SELECT  AA.ID_AZIENDA, " + "         AA.DENOMINAZIONE, "
            + "         AA.CUAA, " + "         AA.PARTITA_IVA, "
            + "         C.DESCOM, " + "         AA.SEDELEG_CITTA_ESTERO, "
            + "         AA.SEDELEG_INDIRIZZO, " + "         AA.SEDELEG_CAP, "
            + "         P.SIGLA_PROVINCIA, " + "         AA.SEDELEG_COMUNE "
            + " FROM    DB_ANAGRAFICA_AZIENDA AA, " + "         DB_DELEGA D, "
            + "         COMUNE C, " + "         PROVINCIA P, "
            + "         (SELECT   ID_AZIENDA, "
            + "                   MAX(DATA) AS DATA_ULTIMA_DICHIARAZIONE "
            + "          FROM     DB_DICHIARAZIONE_CONSISTENZA "
            + "          GROUP BY ID_AZIENDA) DC "
            + " WHERE    AA.DATA_FINE_VALIDITA IS NULL "
            + " AND      AA.ID_AZIENDA = D.ID_AZIENDA "
            + " AND      D.DATA_FINE IS NULL "
            + " AND      D.ID_PROCEDIMENTO = ? "
            + " AND      D.ID_INTERMEDIARIO= ? "
            + " AND      AA.SEDELEG_COMUNE = C.ISTAT_COMUNE "
            + " AND      P.ISTAT_PROVINCIA = C.ISTAT_PROVINCIA "
            + " AND      AA.ID_AZIENDA = DC.ID_AZIENDA "
            + " AND      AA.DATA_CESSAZIONE IS NULL "
            + " AND      DC.DATA_ULTIMA_DICHIARAZIONE " + "          < "
            + "          (SELECT MAX(DATA_AGGIORNAMENTO) "
            + "          FROM   V_AGGIORNAMENTO_CONSISTENZA V "
            + "          WHERE   V.ID_AZIENDA = AA.ID_AZIENDA) " + " UNION "
            + " SELECT   AA.ID_AZIENDA, " + "          AA.DENOMINAZIONE, "
            + "          AA.CUAA, " + "          AA.PARTITA_IVA, "
            + "          C.DESCOM, " + "          AA.SEDELEG_CITTA_ESTERO, "
            + "          AA.SEDELEG_INDIRIZZO, " + "          AA.SEDELEG_CAP, "
            + "          P.SIGLA_PROVINCIA, " + "          AA.SEDELEG_COMUNE "
            + " FROM     DB_ANAGRAFICA_AZIENDA AA, "
            + "          DB_DELEGA D, " + "          COMUNE C, "
            + "          PROVINCIA P, "
            + "          DB_DICHIARAZIONE_CONSISTENZA DC "
            + " WHERE    AA.DATA_FINE_VALIDITA IS NULL "
            + " AND      AA.ID_AZIENDA = D.ID_AZIENDA "
            + " AND      D.DATA_FINE IS NULL "
            + " AND      D.ID_PROCEDIMENTO = ? "
            + " AND      D.ID_INTERMEDIARIO= ? "
            + " AND      AA.SEDELEG_COMUNE = C.ISTAT_COMUNE "
            + " AND      P.ISTAT_PROVINCIA = C.ISTAT_PROVINCIA "
            + " AND      AA.ID_AZIENDA = DC.ID_AZIENDA(+) "
            + " AND      DC.ID_AZIENDA IS NULL "
            + " AND      AA.DATA_CESSAZIONE IS NULL " + " ORDER BY 2,3,4 ";

        SolmrLogger
            .debug(
                this,
                "Created query for a case of fascicoli da validare in method getElencoAziendeByCAA in AnagWriteDAO\n");

      }
      // Caso di ricerca per fascicoli validati dal/al
      else
      {

        SolmrLogger
            .debug(
                this,
                "Creating query for a case of fascicoli validati dal/al in method getElencoAziendeByCAA in AnagWriteDAO\n");

        query = " SELECT AA.ID_AZIENDA, AA.DENOMINAZIONE, "
            + "        AA.CUAA, AA.PARTITA_IVA, "
            + "        C.DESCOM, AA.SEDELEG_CITTA_ESTERO, "
            + "        AA.SEDELEG_INDIRIZZO, AA.SEDELEG_CAP, "
            + "        P.SIGLA_PROVINCIA, AA.SEDELEG_COMUNE "
            + " FROM   DB_ANAGRAFICA_AZIENDA AA, DB_DELEGA D, "
            + "        COMUNE C, " + "        PROVINCIA P, "
            + "        (SELECT   ID_AZIENDA, MAX(DATA) AS DATA_ULTIMA_DICHIARAZIONE "
            + "         FROM     DB_DICHIARAZIONE_CONSISTENZA "
            + "         WHERE TRUNC(DATA) >= ? ";
        if (Validator.isNotEmpty(delegaVO.getDataFine()))
          query += " AND TRUNC(DATA) <= ? ";
        query +="    GROUP BY ID_AZIENDA) DC "
            + " WHERE  AA.DATA_FINE_VALIDITA IS NULL "
            + " AND    AA.ID_AZIENDA = D.ID_AZIENDA "
            + " AND 	 NVL(D.DATA_FINE,SYSDATE)>=DC.DATA_ULTIMA_DICHIARAZIONE "
            + " AND    D.DATA_INIZIO <= DC.DATA_ULTIMA_DICHIARAZIONE "
            + " AND    D.ID_PROCEDIMENTO = ? "
            + " AND    D.ID_INTERMEDIARIO = ? "
            + " AND    AA.SEDELEG_COMUNE = C.ISTAT_COMUNE "
            + " AND    P.ISTAT_PROVINCIA=C.ISTAT_PROVINCIA "
            + " AND    AA.ID_AZIENDA = DC.ID_AZIENDA "
            + " AND 	 NVL(AA.DATA_CESSAZIONE,SYSDATE)>=DC.DATA_ULTIMA_DICHIARAZIONE "
            + " AND    TRUNC(DC.DATA_ULTIMA_DICHIARAZIONE) >= ? ";

        if (Validator.isNotEmpty(delegaVO.getDataFine()))
        {
          query += " AND TRUNC(DC.DATA_ULTIMA_DICHIARAZIONE) <= ? ";
        }

        query += " ORDER BY 2,3,4 ";

        SolmrLogger
            .debug(
                this,
                "Created query for a case of fascicoli validati dal/al in method getElencoAziendeByCAA in AnagWriteDAO\n");

      }

      stmt = conn.prepareStatement(query);
      
      if (delegaVO.getStatoRicercaFascicolo().equalsIgnoreCase((String) SolmrConstants.get("STATO_FASCICOLO_VALIDATI_DAL_AL")))
      {
      	// Caso di ricerca per fascicoli validati dal/al
      	int indice=0;
        SolmrLogger.debug(this,"Setting parameters of a case of fascicoli validati dal/al in method getElencoAziendeByCAA in AnagWriteDAO\n");
        SolmrLogger.debug(this,"Value of parameter [DATA_DAL] in method getElencoAziendeByCAA in AnagWriteDAO: "+ delegaVO.getDataInizio() + "\n");
        stmt.setDate(++indice, new java.sql.Date(delegaVO.getDataInizio().getTime()));
        if (Validator.isNotEmpty(delegaVO.getDataFine()))
        {
          SolmrLogger.debug(this,"Value of parameter [DATA_AL] in method getElencoAziendeByCAA in AnagWriteDAO: "+ delegaVO.getDataFine() + "\n");
          stmt.setDate(++indice, new java.sql.Date(delegaVO.getDataFine().getTime()));
        }
        SolmrLogger.debug(this,"Value of parameter [ID_PROCEDIMENTO] in method getElencoAziendeByCAA in AnagWriteDAO: "+ (String) SolmrConstants.get("ID_TIPO_PROCEDIMENTO_ANAG")+ "\n");
	      stmt.setLong(++indice, Long.parseLong((String) SolmrConstants.get("ID_TIPO_PROCEDIMENTO_ANAG")));
	      SolmrLogger.debug(this,"Value of parameter [ID_INTERMEDIARIO] in method getElencoAziendeByCAA in AnagWriteDAO: "+ delegaVO.getIdIntermediario() + "\n");
	      stmt.setLong(++indice, delegaVO.getIdIntermediario().longValue());
        
        stmt.setDate(++indice, new java.sql.Date(delegaVO.getDataInizio().getTime()));
        if (Validator.isNotEmpty(delegaVO.getDataFine()))
          stmt.setDate(++indice, new java.sql.Date(delegaVO.getDataFine().getTime()));
        SolmrLogger.debug(this,"Setted parameters of a case of fascicoli validati dal/al in method getElencoAziendeByCAA in AnagWriteDAO\n");

      }
      else
      {
	      SolmrLogger.debug(this,"Value of parameter 1 [ID_PROCEDIMENTO] in method getElencoAziendeByCAA in AnagWriteDAO: "+ (String) SolmrConstants.get("ID_TIPO_PROCEDIMENTO_ANAG")+ "\n");
	      stmt.setLong(1, Long.parseLong((String) SolmrConstants.get("ID_TIPO_PROCEDIMENTO_ANAG")));
	      SolmrLogger.debug(this,"Value of parameter 2 [ID_INTERMEDIARIO] in method getElencoAziendeByCAA in AnagWriteDAO: "+ delegaVO.getIdIntermediario() + "\n");
	      stmt.setLong(2, delegaVO.getIdIntermediario().longValue());
	      // Caso di ricerca fascicolo da validare
	      if (delegaVO.getStatoRicercaFascicolo().equalsIgnoreCase(
	          (String) SolmrConstants.get("STATO_FASCICOLO_DA_VALIDARE")))
	      {
	        SolmrLogger.debug(this,"Setting parameters of a case of fascicoli da validare in method getElencoAziendeByCAA in AnagWriteDAO\n");
	        SolmrLogger.debug(this,"Value of parameter 3 [ID_PROCEDIMENTO] in method getElencoAziendeByCAA in AnagWriteDAO: "+ (String) SolmrConstants.get("ID_TIPO_PROCEDIMENTO_ANAG")+ "\n");
	        stmt.setLong(3, Long.parseLong((String) SolmrConstants
	            .get("ID_TIPO_PROCEDIMENTO_ANAG")));
	        SolmrLogger.debug(this,"Value of parameter 4 [ID_INTERMEDIARIO] in method getElencoAziendeByCAA in AnagWriteDAO: "+ delegaVO.getIdIntermediario() + "\n");
	        stmt.setLong(4, delegaVO.getIdIntermediario().longValue());
	        SolmrLogger.debug(this,"Setted parameters of a case of fascicoli da validare in method getElencoAziendeByCAA in AnagWriteDAO\n");
	      }
      } 
      	
      SolmrLogger.debug(this, "Executing query getElencoAziendeByCAA: " + query);

      ResultSet rs = stmt.executeQuery();

      elencoAziende = new Vector<AnagAziendaVO>();

      while (rs.next())
      {
        AnagAziendaVO anagAziendaVO = new AnagAziendaVO();
        anagAziendaVO.setIdAzienda(new Long(rs.getLong(1)));
        anagAziendaVO.setDenominazione(rs.getString(2));
        anagAziendaVO.setCUAA(rs.getString(3));
        anagAziendaVO.setPartitaIVA(rs.getString(4));
        anagAziendaVO.setDescComune(rs.getString(5));
        anagAziendaVO.setSedelegCittaEstero(rs.getString(6));
        anagAziendaVO.setSedelegIndirizzo(rs.getString(7));
        anagAziendaVO.setSedelegCAP(rs.getString(8));
        anagAziendaVO.setSedelegProv(rs.getString(9));
        anagAziendaVO.setSedelegComune(rs.getString(10));
        elencoAziende.add(anagAziendaVO);
      }

      rs.close();
      stmt.close();

      if (elencoAziende.size() == 0)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_NO_AZIENDE_FOR_CAA"));
      }

    }
    catch (SolmrException exc)
    {
      SolmrLogger.error(this, "getElencoAziendeByCAA - SolmrException: "
          + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "getElencoAziendeByCAA - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this, "getElencoAziendeByCAA - Generic Exception: "
          + ex + " with message: " + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
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
            "getElencoAziendeByCAA - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getElencoAziendeByCAA - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this,
        "Invocated method getElencoAziendeByCAA in AnagWriteDAO\n");
    return elencoAziende;
  }

  // Metodo che richiama una procedura PL-SQL per effettuare l'aggiornamento dei
  // dati
  // della tabella DB_PROCEDIMENTO_AZIENDA
  public void updateProcedimentoAzienda(Long idAzienda)
      throws SolmrException, DataAccessException
  {
    Connection conn = null;
    CallableStatement cs = null;
    String command = "{call PACK_PRATICA_AZIENDA.AGGIORNA_PRATICA_AZIENDA(?,?,?)}";

    try
    {

      conn = getDatasource().getConnection();

      cs = conn.prepareCall(command);
      cs.setLong(1, idAzienda.longValue());
      cs.registerOutParameter(2, Types.VARCHAR);
      cs.registerOutParameter(3, Types.VARCHAR);

      cs.executeUpdate();
      String msgErr = cs.getString(2);
      String errorCode = cs.getString(3);

      if (!(errorCode == null || "".equals(errorCode)))
      {
        throw new SolmrException((String) AnagErrors.get("ERR_PLSQL2")
            + errorCode + " - " + msgErr);
      }

      cs.close();
      conn.close();

      SolmrLogger.debug(this, "Executed updateProcedimentoAzienda");
    }
    catch (SolmrException se)
    {
      SolmrLogger.fatal(this, "SolmrException in updateProcedimentoAzienda: "
          + se.getMessage());
      throw new SolmrException(se.getMessage());
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in updateProcedimentoAzienda: "
          + exc.getMessage());
      throw new SolmrException((String) AnagErrors.get("ERR_PLSQL2") + " "
          + exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in updateProcedimentoAzienda: " + ex.getMessage());
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
                "SQLException while closing Statement and Connection in updateProcedimentoAzienda: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in updateProcedimentoAzienda: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  /**
   * Metodo per recuperare l'elenco dei procedimenti legati ad un'azienda
   * agricola. Restituisce un vettore di ProcedimentoAziendaVO.
   * 
   * @param idAzienda
   * @param annoProc
   * @param idProcedimento
   * @return
   * @throws DataAccessException
   * @throws SolmrException
   */
  public Vector<ProcedimentoAziendaVO> getElencoProcedimentiByIdAzienda(Long idAzienda, Long annoProc,
      Long idProcedimento, Long idAziendaSelezionata) throws DataAccessException, SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ProcedimentoAziendaVO> elencoProcedimenti = null;
    int idx = 0;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT   TP.DESCRIZIONE_ESTESA, "
          + "          TP.ID_PROCEDIMENTO, " 
          + "          PA.NUMERO_PRATICA, "
          + "          PA.DESCRIZIONE, " 
          + "          PA.DESCRIZIONE_STATO, "
          + "          PA.DATA_VALIDITA_STATO, " 
          + "          PA.NOTE, "
          + "          PA.EXT_ID_AMM_COMPETENZA, "
          + "          PA.ANNO_CAMPAGNA, "
          + "          PA.ID_DICHIARAZIONE_CONSISTENZA "
          + " FROM     DB_PROCEDIMENTO_AZIENDA PA, "
          + "          DB_TIPO_PROCEDIMENTO TP "
          + " WHERE    PA.ID_AZIENDA = ? ";

      if (Validator.isNotEmpty(annoProc))
      {
        query += "AND PA.ANNO_CAMPAGNA = ? ";
      }

      if (Validator.isNotEmpty(idProcedimento))
      {
        query += "AND PA.ID_PROCEDIMENTO = ? ";
      }
      
      query += " AND      PA.ID_PROCEDIMENTO = TP.ID_PROCEDIMENTO "
          + " AND      PA.STATO <> ? " + " ORDER BY PA.ID_PROCEDIMENTO, "
          + "          PA.DATA_VALIDITA_STATO DESC ";

      stmt = conn.prepareStatement(query);

      if (Validator.isNotEmpty(idAziendaSelezionata))
      {
    	  stmt.setLong(++idx, idAziendaSelezionata.longValue());
      }
      else stmt.setLong(++idx, idAzienda.longValue());

      if (Validator.isNotEmpty(annoProc))
      {
        stmt.setLong(++idx, annoProc.longValue());
      }

      if (Validator.isNotEmpty(idProcedimento))
      {
        stmt.setLong(++idx, idProcedimento.longValue());
      }

      stmt.setString(++idx, (String) SolmrConstants
          .get("STATO_PRATICA_IN_BOZZA"));

      SolmrLogger.debug(this,
          "Executing query getElencoProcedimentiByIdAzienda: " + query);

      ResultSet rs = stmt.executeQuery();

      elencoProcedimenti = new Vector<ProcedimentoAziendaVO>();

      while (rs.next())
      {
        ProcedimentoAziendaVO procedimentoAziendaVO = new ProcedimentoAziendaVO();
        TipoProcedimentoVO tipoProcedimentoVO = new TipoProcedimentoVO();
        tipoProcedimentoVO.setDescrizioneEstesa(rs
            .getString("DESCRIZIONE_ESTESA"));
        tipoProcedimentoVO.setIdProcedimento(new Long(rs
            .getLong("ID_PROCEDIMENTO")));
        procedimentoAziendaVO.setIdDichiarazioneConsistenza(checkLongNull(rs.getString("ID_DICHIARAZIONE_CONSISTENZA")));
        procedimentoAziendaVO.setNumeroPratica(rs.getString("NUMERO_PRATICA"));
        procedimentoAziendaVO.setDescrizione(rs.getString("DESCRIZIONE"));
        procedimentoAziendaVO.setDescrizioneStato(rs
            .getString("DESCRIZIONE_STATO"));
        procedimentoAziendaVO.setDataValiditaStato(rs
            .getDate("DATA_VALIDITA_STATO"));
        procedimentoAziendaVO.setNote(rs.getString("NOTE"));
        procedimentoAziendaVO.setExIdAmmCompetenza(new Long(rs
            .getLong("EXT_ID_AMM_COMPETENZA")));
        procedimentoAziendaVO.setAnnoCampagna(new Long(rs.getLong("ANNO_CAMPAGNA")));
        procedimentoAziendaVO.setTipoProcedimentoVO(tipoProcedimentoVO);
        elencoProcedimenti.add(procedimentoAziendaVO);
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this,
          "Executed query getElencoProcedimentiByIdAzienda and found: "
              + elencoProcedimenti.size() + " records");

      if (elencoProcedimenti.size() == 0)
      {
        throw new SolmrException((String) AnagErrors.get("ERR_NO_PRATICHE"));
      }

    }
    catch (SolmrException exc)
    {
      SolmrLogger.debug(this,
          "getElencoProcedimentiByIdAzienda - SolmrException: "
              + exc.getMessage());
      throw new SolmrException(exc.getMessage());
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "getElencoProcedimentiByIdAzienda - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getElencoProcedimentiByIdAzienda - Generic Exception: " + ex
              + " with message: " + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
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
                "getElencoProcedimentiByIdAzienda - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getElencoProcedimentiByIdAzienda - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoProcedimenti;
  }

  /**
   * Metodo per recuperare l'elenco dei CUAA relativi ad un'id_azienda
   * 
   * @param idAzienda
   *          Long
   * @return Vector
   * @throws DataAccessException
   */
  public Vector<String> getListCUAAByIdAzienda(Long idAzienda)
      throws DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating method getListCUAAByIdAzienda in AnagWriteDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<String> elencoCUAA = new Vector<String>();

    try
    {
      SolmrLogger
          .debug(this,
              "Creating db_connection in method getListCUAAByIdAzienda in AnagWriteDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db_connection in method getListCUAAByIdAzienda in AnagWriteDAO with value: "
                  + conn + "\n");

      SolmrLogger.debug(this,
          "Creating query in method getListCUAAByIdAzienda in AnagWriteDAO\n");

      String query = " SELECT   AA.CUAA, "
          + "          MAX(AA.DATA_INIZIO_VALIDITA) "
          + " FROM     DB_ANAGRAFICA_AZIENDA AA "
          + " WHERE    AA.ID_AZIENDA = ? " + " GROUP BY AA.CUAA "
          + " ORDER BY 2 DESC ";

      SolmrLogger.debug(this,
          "Created query in method getListCUAAByIdAzienda in AnagWriteDAO\n");

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [ID_AZIENDA] in method getListCUAAByIdAzienda in AnagWriteDAO: "
                  + idAzienda + "\n");
      stmt.setLong(1, idAzienda.longValue());

      SolmrLogger.debug(this, "Executing getListCUAAByIdAzienda: " + query
          + "\n");

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        if (Validator.isNotEmpty(rs.getString(1)))
        {
          String cuaa = rs.getString(1);
          elencoCUAA.add(cuaa);
        }
      }
      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "getListCUAAByIdAzienda - SQLException: "
          + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this, "getListCUAAByIdAzienda - Generic Exception: "
          + ex + "\n");
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
                "getListCUAAByIdAzienda - SQLException while closing Statement and Connection: "
                    + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getListCUAAByIdAzienda - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this,
        "Invocated method getListCUAAByIdAzienda in AnagWriteDAO");
    return elencoCUAA;
  }

  /**
   * Metodo che si occupa di estrarre la denominazione più recente di un'azienda
   * partendo dal CUAA e dall'id_azienda
   * 
   * @param idAzienda
   *          Long
   * @param cuaa
   *          String
   * @return String
   * @throws DataAccessException
   */
  public String getDenominazioneAziendaByCuaaAndIdAzienda(Long idAzienda,
      String cuaa) throws DataAccessException
  {
    SolmrLogger
        .debug(this,
            "Invocating method getDenominazioneAziendaByCuaaAndIdAzienda in AnagWriteDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    String denominazione = null;

    try
    {
      SolmrLogger
          .debug(
              this,
              "Creating db_connection in method getDenominazioneAziendaByCuaaAndIdAzienda in AnagWriteDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db_connection in method getDenominazioneAziendaByCuaaAndIdAzienda in AnagWriteDAO with value: "
                  + conn + "\n");

      SolmrLogger
          .debug(
              this,
              "Creating query in method getDenominazioneAziendaByCuaaAndIdAzienda in AnagWriteDAO\n");

      String query = " SELECT   AA.DENOMINAZIONE, "
          + "          MAX(AA.DATA_INIZIO_VALIDITA) "
          + " FROM     DB_ANAGRAFICA_AZIENDA AA "
          + " WHERE    AA.ID_AZIENDA = ? " + " AND      AA.CUAA = ? "
          + " GROUP BY AA.DENOMINAZIONE " + " ORDER BY 2 DESC ";

      SolmrLogger
          .debug(
              this,
              "Created query in method getDenominazioneAziendaByCuaaAndIdAzienda in AnagWriteDAO\n");

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [ID_AZIENDA] in method getDenominazioneAziendaByCuaaAndIdAzienda in AnagWriteDAO: "
                  + idAzienda + "\n");
      stmt.setLong(1, idAzienda.longValue());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 2 [CUAA] in method getDenominazioneAziendaByCuaaAndIdAzienda in AnagWriteDAO: "
                  + cuaa.toUpperCase() + "\n");
      stmt.setString(2, cuaa.toUpperCase());

      SolmrLogger.debug(this,
          "Executing getDenominazioneAziendaByCuaaAndIdAzienda: " + query
              + "\n");

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        if (Validator.isNotEmpty(rs.getString(1)))
        {
          denominazione = rs.getString(1);
        }
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "getDenominazioneAziendaByCuaaAndIdAzienda - SQLException: "
              + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "getDenominazioneAziendaByCuaaAndIdAzienda - Generic Exception: "
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
                "getDenominazioneAziendaByCuaaAndIdAzienda - SQLException while closing Statement and Connection: "
                    + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getDenominazioneAziendaByCuaaAndIdAzienda - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger
        .debug(this,
            "Invocated method getDenominazioneAziendaByCuaaAndIdAzienda in AnagWriteDAO");
    return denominazione;
  }

  

  /**
   * Servizio utilizzato per estrarre cuaa e id delle aziende di provenienza e 
   * di destinazione rispetto a quello dato
   * 
   * @param idAzienda
   * @return CodeDescription (idAzienda e cuaa)
   * @throws DataAccessException
   */
  public CodeDescription[] getListCuaaAttiviProvDestByIdAzienda(Long idAzienda) throws DataAccessException
  {
	  String query = null;
	  Connection conn = null;
	  PreparedStatement stmt = null;
	  StringBuffer queryBuf = null;
	  Vector<CodeDescription> idAziendaCuaa = new Vector<CodeDescription>();

	  try
	  {
		  SolmrLogger
		  .debug(this,
				  "[UteDAO::getListCuaaAttiviProvDestByIdAzienda] BEGIN.");

		  /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */

		  queryBuf = new StringBuffer();
		  queryBuf
		  .append(" SELECT DISTINCT ID_AZIENDA, CUAA " + 
				  " FROM  DB_ANAGRAFICA_AZIENDA " + 
				  " WHERE CUAA IS NOT NULL " +
				  " AND DATA_FINE_VALIDITA IS NULL" +
				  " AND ID_AZIENDA IN (" +
				  "		SELECT ID_AZIENDA  ID_AZI " +
				  "		FROM DB_AZIENDA_DESTINAZIONE " +
				  "		CONNECT BY PRIOR ID_AZIENDA = ID_AZIENDA_DI_DESTINAZIONE  " +
				  "		START WITH ID_AZIENDA_DI_DESTINAZIONE = ? " +
				  "		UNION " +
				  "		SELECT ID_AZIENDA_DI_DESTINAZIONE ID_AZI " +
				  "		FROM DB_AZIENDA_DESTINAZIONE " +
				  "		CONNECT BY PRIOR ID_AZIENDA_DI_DESTINAZIONE=ID_AZIENDA    " +
				  "		START WITH ID_AZIENDA = ? " +
				  "		UNION " +
				  "		SELECT ID_AZIENDA  ID_AZI " +
				  "		FROM DB_ANAGRAFICA_AZIENDA " +
		  		  "		WHERE ID_AZIENDA = ?) ");

		  query = queryBuf.toString();
		  /* CONCATENAZIONE/CREAZIONE QUERY END. */

		  conn = getDatasource().getConnection();
		  if (SolmrLogger.isDebugEnabled(this))
		  {
			  // Dato che la query costruita dinamicamente è un dato importante la
			  // registro sul file di log se il
			  // debug è abilitato

			  SolmrLogger.debug(this, "[UteDAO::getListCuaaAttiviProvDestByIdAzienda] Query=" + query);
		  }
		  stmt = conn.prepareStatement(query);

		  stmt.setLong(1,idAzienda);
		  stmt.setLong(2,idAzienda);
		  stmt.setLong(3,idAzienda);


		  // Setto i parametri della query
		  ResultSet rs = stmt.executeQuery();

		  while (rs.next())
		  {
			  CodeDescription codeDescription = new CodeDescription();
			  codeDescription.setCode(new Integer(rs.getInt("ID_AZIENDA")));
			  codeDescription.setDescription(rs.getString("CUAA"));
			  idAziendaCuaa.add(codeDescription);
		  }

	  }
	  catch (Throwable t)
	  {
		  // Vettore di variabili interne del metodo
		  Variabile variabili[] = new Variabile[]
		                                        { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
				  new Variabile("idAziendaCuaa", idAziendaCuaa) };

		  // Vettore di parametri passati al metodo
		  Parametro parametri[] = new Parametro[]
		                                        { new Parametro("idAzienda", idAzienda) };
		  // Logging dell'eccezione, query, variabili e parametri del metodo
		  LoggerUtils.logDAOError(this, "[UteDAO::getListCuaaAttiviProvDestByIdAzienda] ", t, query, variabili, parametri);
		  /*
		   * Rimappo e rilancio l'eccezione come DataAccessException.
		   */
		  throw new DataAccessException(t.getMessage());
	  }
	  finally
	  {
		  close(null, stmt, conn);

		  // Fine metodo
		  SolmrLogger.debug(this,"[UteDAO::getListCuaaAttiviProvDestByIdAzienda] END.");
	  }
	  return idAziendaCuaa.size() == 0 ? null : (CodeDescription[]) idAziendaCuaa.toArray(new CodeDescription[0]);
  }
  
  /**
   * Vengono restituiti dal servizio 3 booleani:· bVariazioneAnagrafica
   * bVariazioneTitolare bVariazioneResidenzaTitolare
   * 
   * I parametri di output vanno così valorizzati: bVariazioneAnagraficatrue se
   * esiste su db_anagrafica_azienda un record con data_inziio_validita > data
   * (parametro)false altrimenti bVariazioneTitolaretrue se esiste su
   * db_contitolare un record con data_inizio_ruolo > data (parametro) and
   * id_ruolo = 1false altrimenti bVariazioneResidenzaTitolaretrue se esiste su
   * db_persona_fisica un record con data_inizio_residenza > data (parametro)
   * and id_soggetto = identificativo del soggetto del titolare attivo.false
   * altrimenti
   */
  public boolean[] serviceGetVariazioneDatiAnagrafici(Long idAzienda, Date data)
      throws DataAccessException
  {
    SolmrLogger
        .debug(this,
            "Invocating serviceGetVariazioneDatiAnagrafici method in AnagWriteDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    boolean result[] = new boolean[3];
    result[0] = result[1] = result[2] = false;

    // Creo l'oggetto Stop Watch per monitorare le operazioni eseguite
    // all'interno del metodo
    StopWatch watcher = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);

    try
    {

      // START
      watcher.start();

      SolmrLogger
          .debug(
              this,
              "Creating db-connection in serviceGetVariazioneDatiAnagrafici method in AnagWriteDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in serviceGetVariazioneDatiAnagrafici method in AnagWriteDAO and it values: "
                  + conn + "\n");

      String query = "SELECT * " + "FROM DB_ANAGRAFICA_AZIENDA AA "
          + "WHERE AA.ID_AZIENDA=? " + "AND AA.DATA_INIZIO_VALIDITA>? ";

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [idAzienda] in serviceGetVariazioneDatiAnagrafici method in AnagWriteDAO: "
                  + idAzienda + "\n");
      SolmrLogger
          .debug(
              this,
              "Value of parameter 2 [data] in serviceGetVariazioneDatiAnagrafici method in AnagWriteDAO: "
                  + data + "\n");

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setTimestamp(2, convertDateToTimestamp(data));

      SolmrLogger.debug(this,
          "Executing serviceGetVariazioneDatiAnagrafici query param 1: "
              + query + "\n");

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        result[0] = true;

      SolmrLogger.debug(this,
          "Executed serviceGetVariazioneDatiAnagrafici query param 1");

      rs.close();
      stmt.close();

      query = "SELECT * " + "FROM DB_CONTITOLARE C " + "WHERE C.ID_AZIENDA=? "
          + "AND C.DATA_INIZIO_RUOLO>? " + "AND C.ID_RUOLO=? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(
              this,
              "Value of parameter 3 [ID_RUOLO] in serviceGetVariazioneDatiAnagrafici method in AnagWriteDAO: "
                  + SolmrConstants.TIPO_RUOLO_TITOLARE_RAPPRESENTANTE_LEGALE
                  + "\n");

      stmt.setLong(1, idAzienda.longValue());
      stmt.setTimestamp(2, convertDateToTimestamp(data));
      stmt.setString(3,
          SolmrConstants.TIPO_RUOLO_TITOLARE_RAPPRESENTANTE_LEGALE);

      SolmrLogger.debug(this,
          "Executing serviceGetVariazioneDatiAnagrafici query param 2: "
              + query + "\n");

      rs = stmt.executeQuery();

      if (rs.next())
        result[1] = true;

      SolmrLogger.debug(this,
          "Executed serviceGetVariazioneDatiAnagrafici query param 2");

      rs.close();
      stmt.close();

      query = "SELECT * " + "FROM DB_PERSONA_FISICA PF,DB_CONTITOLARE C "
          + "WHERE PF.ID_SOGGETTO=C.ID_SOGGETTO " + "AND C.ID_AZIENDA=? "
          + "AND PF.DATA_INIZIO_RESIDENZA>? " + "AND C.ID_RUOLO=? "
          + "AND C.DATA_FINE_RUOLO IS NULL ";

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(
              this,
              "Value of parameter 3 [ID_RUOLO] in serviceGetVariazioneDatiAnagrafici method in AnagWriteDAO: "
                  + SolmrConstants.TIPO_RUOLO_TITOLARE_RAPPRESENTANTE_LEGALE
                  + "\n");

      stmt.setLong(1, idAzienda.longValue());
      stmt.setTimestamp(2, convertDateToTimestamp(data));
      stmt.setString(3,
          SolmrConstants.TIPO_RUOLO_TITOLARE_RAPPRESENTANTE_LEGALE);

      SolmrLogger.debug(this,
          "Executing serviceGetVariazioneDatiAnagrafici query param 3: "
              + query + "\n");

      rs = stmt.executeQuery();

      if (rs.next())
        result[2] = true;

      SolmrLogger.debug(this,
          "Executed serviceGetVariazioneDatiAnagrafici query param 3");

      rs.close();
      stmt.close();

    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "serviceGetVariazioneDatiAnagrafici in AnagWriteDAO - SQLException: "
              + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "serviceGetVariazioneDatiAnagrafici in AnagWriteDAO - Generic Exception: "
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
                "serviceGetVariazioneDatiAnagrafici in AnagWriteDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "serviceGetVariazioneDatiAnagrafici in AnagWriteDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }

    // Monitoraggio tempistica
    watcher
        .dumpElapsed(
            "AnagWriteDAO",
            "serviceGetVariazioneDatiAnagrafici",
            "In serviceGetVariazioneDatiAnagrafici method from the beginning to the end",
            "List of parameters: idAzienda: " + idAzienda + ",  data: " + data);

    // STOP
    watcher.stop();

    SolmrLogger
        .debug(this,
            "Invocated serviceGetVariazioneDatiAnagrafici method in AnagWriteDAO\n");
    return result;
  }

  /**
   * Metodo che mi restituisce la MIN relativa alla data inizio validita di
   * un'azienda
   * 
   * @param idAzienda
   * @return java.util.Date
   * @throws DataAccessException
   */
  public Date getMinDataInizioValiditaAnagraficaAzienda(Long idAzienda)
      throws DataAccessException
  {
    SolmrLogger
        .debug(this,
            "Invocating getMinDataInizioValiditaAnagraficaAzienda method in AnagWriteDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Date minDataInizioValidita = null;

    try
    {

      SolmrLogger
          .debug(
              this,
              "Creating db-connection in getMinDataInizioValiditaAnagraficaAzienda method in AnagWriteDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in getMinDataInizioValiditaAnagraficaAzienda method in AnagWriteDAO and it values: "
                  + conn + "\n");

      String query = " SELECT MIN(DATA_INIZIO_VALIDITA) AS MIN_DATA_INIZIO "
          + " FROM   DB_ANAGRAFICA_AZIENDA " + " WHERE  ID_AZIENDA = ?";

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [ID_AZIENDA] in getMinDataInizioValiditaAnagraficaAzienda method in AnagWriteDAO: "
                  + idAzienda + "\n");
      stmt.setLong(1, idAzienda.longValue());

      SolmrLogger.debug(this,
          "Executing getMinDataInizioValiditaAnagraficaAzienda: " + query
              + "\n");
      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        minDataInizioValidita = rs.getDate("MIN_DATA_INIZIO");
      }

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "getMinDataInizioValiditaAnagraficaAzienda - SQLException: "
              + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "getMinDataInizioValiditaAnagraficaAzienda - Generic Exception: "
              + ex + "\n");
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
                "getMinDataInizioValiditaAnagraficaAzienda - SQLException while closing Statement and Connection: "
                    + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getMinDataInizioValiditaAnagraficaAzienda - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger
        .debug(this,
            "Invocated getMinDataInizioValiditaAnagraficaAzienda method in AnagWriteDAO\n");
    return minDataInizioValidita;
  }

  /**
   * Metodo utilizzato per effettuare la modifica del record su DB_AZIENDA in
   * funzione dell'OPR delegato al fascicolo
   * 
   * @param anagAziendaVO
   * @throws DataAccessException
   */
  public void modificaGestoreFascicolo(AnagAziendaVO anagAziendaVO)
      throws DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating modificaGestoreFascicolo method in AnagWriteDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      SolmrLogger
          .debug(this,
              "Creating db-connection in modificaGestoreFascicolo method in AnagWriteDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in modificaGestoreFascicolo method in AnagWriteDAO and it values: "
                  + conn + "\n");

      String query = " UPDATE DB_AZIENDA " + " SET    ID_OPR = ?, "
          + "        DATA_APERTURA_FASCICOLO = ?, "
          + "        DATA_CHIUSURA_FASCICOLO = ?, "
          + "        DATA_AGGIORNAMENTO_OPR = ?, "
          + "        ID_UTENTE_AGGIORNAMENTO_OPR = ? "
          + " WHERE  ID_AZIENDA = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing modificaGestoreFascicolo: " + query);

      if (anagAziendaVO.getIdOpr() != null)
      {
        stmt.setLong(1, anagAziendaVO.getIdOpr().longValue());
      }
      else
      {
        stmt.setString(1, null);
      }
      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [ID_OPR] in method modificaGestoreFascicolo in AnagWriteDAO: "
                  + anagAziendaVO.getIdOpr() + "\n");
      if (anagAziendaVO.getDataAperturaFascicolo() != null)
      {
        stmt.setDate(2, new java.sql.Date(anagAziendaVO
            .getDataAperturaFascicolo().getTime()));
      }
      else
      {
        stmt.setString(2, null);
      }
      SolmrLogger
          .debug(
              this,
              "Value of parameter 2 [DATA_APERTURA_FASCICOLO] in method modificaGestoreFascicolo in AnagWriteDAO: "
                  + anagAziendaVO.getDataAperturaFascicolo() + "\n");
      if (anagAziendaVO.getDataChiusuraFascicolo() != null)
      {
        stmt.setDate(3, new java.sql.Date(anagAziendaVO
            .getDataChiusuraFascicolo().getTime()));
        SolmrLogger
            .debug(
                this,
                "Value of parameter 3 [DATA_CHIUSURA_FASCICOLO] in method modificaGestoreFascicolo in AnagWriteDAO: "
                    + anagAziendaVO.getDataChiusuraFascicolo() + "\n");
      }
      else
      {
        stmt.setString(3, null);
        SolmrLogger
            .debug(
                this,
                "Value of parameter 3 [DATA_CHIUSURA_FASCICOLO] in method modificaGestoreFascicolo in AnagWriteDAO: "
                    + null + "\n");
      }
      stmt.setTimestamp(4, new Timestamp(anagAziendaVO
          .getDataAggiornamentoOpr().getTime()));
      SolmrLogger
          .debug(
              this,
              "Value of parameter 4 [DATA_AGGIORNAMENTO_OPR] in method modificaGestoreFascicolo in AnagWriteDAO: "
                  + new Timestamp(anagAziendaVO.getDataAggiornamentoOpr()
                      .getTime()) + "\n");
      stmt.setLong(5, anagAziendaVO.getIdUtenteAggiornamentoOpr().longValue());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 5 [ID_UTENTE_AGGIORNAMENTO_OPR] in method modificaGestoreFascicolo in AnagWriteDAO: "
                  + anagAziendaVO.getIdUtenteAggiornamentoOpr().longValue()
                  + "\n");
      stmt.setLong(6, anagAziendaVO.getIdAzienda().longValue());
      SolmrLogger
          .debug(
              this,
              "Value of parameter 6 [ID_AZIENDA] in method modificaGestoreFascicolo in AnagWriteDAO: "
                  + anagAziendaVO.getIdAzienda().longValue() + "\n");

      stmt.executeUpdate();

      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "modificaGestoreFascicolo in AnagWriteDAO - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .error(this,
              "modificaGestoreFascicolo in AnagWriteDAO - Generic Exception: "
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
                "modificaGestoreFascicolo in AnagWriteDAO - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "modificaGestoreFascicolo in AnagWriteDAO - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this,
        "Invocated modificaGestoreFascicolo method in AnagWriteDAO\n");
  }

  /**
   * Metodo che mi restituisce la MAX relativa alla data inizio validita di
   * un'azienda
   * 
   * @param idAzienda
   * @return java.util.Date
   * @throws DataAccessException
   */
  public Date getMaxDataInizioValiditaAnagraficaAzienda(Long idAzienda)
      throws DataAccessException
  {
    SolmrLogger
        .debug(this,
            "Invocating getMaxDataInizioValiditaAnagraficaAzienda method in AnagWriteDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Date maxDataInizioValidita = null;

    try
    {

      SolmrLogger
          .debug(
              this,
              "Creating db-connection in getMaxDataInizioValiditaAnagraficaAzienda method in AnagWriteDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db-connection in getMaxDataInizioValiditaAnagraficaAzienda method in AnagWriteDAO and it values: "
                  + conn + "\n");

      String query = " SELECT MAX(DATA_INIZIO_VALIDITA) AS MAX_DATA_INIZIO "
          + " FROM   DB_ANAGRAFICA_AZIENDA " + " WHERE  ID_AZIENDA = ?";

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [ID_AZIENDA] in getMaxDataInizioValiditaAnagraficaAzienda method in AnagWriteDAO: "
                  + idAzienda + "\n");
      stmt.setLong(1, idAzienda.longValue());

      SolmrLogger.debug(this,
          "Executing getMaxDataInizioValiditaAnagraficaAzienda: " + query
              + "\n");
      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        maxDataInizioValidita = rs.getDate("MAX_DATA_INIZIO");
      }

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "getMaxDataInizioValiditaAnagraficaAzienda - SQLException: "
              + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "getMaxDataInizioValiditaAnagraficaAzienda - Generic Exception: "
              + ex + "\n");
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
                "getMaxDataInizioValiditaAnagraficaAzienda - SQLException while closing Statement and Connection: "
                    + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getMaxDataInizioValiditaAnagraficaAzienda - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger
        .debug(this,
            "Invocated getMaxDataInizioValiditaAnagraficaAzienda method in AnagWriteDAO\n");
    return maxDataInizioValidita;
  }

  // Metodo per recuperare recuperare l'ufficio zona intermediario partendo
  // dall' IdIntemerdiario
  public UfficioZonaIntermediarioVO findUfficioZonaIntermediarioVOByIdIntemediario(
      Long idIntermediario) throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    UfficioZonaIntermediarioVO ufficioZonaIntermediarioVO = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = " SELECT UZI.ID_UFFICIO_ZONA_INTERMEDIARIO, "
          + "        UZI.ID_INTERMEDIARIO, " + "        UZI.DENOMINAZIONE, "
          + "        UZI.CODICE_AGEA, " + "        UZI.INDIRIZZO, "
          + "        UZI.CAP, " + "        UZI.COMUNE, " + "        C.DESCOM, "
          + "        UZI.RECAPITO, " + "        P.SIGLA_PROVINCIA "
          + " FROM   DB_UFFICIO_ZONA_INTERMEDIARIO UZI, "
          + "        COMUNE C, " + "        PROVINCIA P "
          + " WHERE  UZI.ID_INTERMEDIARIO = ? "
          + " AND    UZI.COMUNE = C.ISTAT_COMUNE "
          + " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idIntermediario.longValue());

      SolmrLogger.debug(this,
          "Executing query findUfficioZonaIntermediarioVOByIdIntemediario: "
              + query);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        ufficioZonaIntermediarioVO = new UfficioZonaIntermediarioVO();
        ufficioZonaIntermediarioVO.setIdUfficioZonaIntermediario(new Long(rs
            .getLong(1)));
        ufficioZonaIntermediarioVO.setIdIntermediario(new Long(rs.getLong(2)));
        ufficioZonaIntermediarioVO.setDenominazione(rs.getString(3));
        ufficioZonaIntermediarioVO.setCodiceAgea(rs.getString(4));
        ufficioZonaIntermediarioVO.setIndirizzo(rs.getString(5));
        ufficioZonaIntermediarioVO.setCap(rs.getString(6));
        ufficioZonaIntermediarioVO.setIstatComune(rs.getString(7));
        ufficioZonaIntermediarioVO.setDescrizioneComune(rs.getString(8));
        ufficioZonaIntermediarioVO.setRecapito(rs.getString(9));
        ufficioZonaIntermediarioVO.setSiglaProvincia(rs.getString(10));
      }

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "findUfficioZonaIntermediarioVOByIdIntemediario - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "findUfficioZonaIntermediarioVOByIdIntemediario - Generic Exception: "
              + ex + " with message: " + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
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
                "findUfficioZonaIntermediarioVOByIdIntemediario - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "findUfficioZonaIntermediarioVOByIdIntemediario - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return ufficioZonaIntermediarioVO;
  }
  
  
  public void updateContitolareAT(long idAzienda)
    throws DataAccessException
  {
  
    Connection conn = null;
    PreparedStatement stmt = null;
  
    try
    {
      conn = getDatasource().getConnection();
  
      String update = 
               "UPDATE DB_CONTITOLARE " +
               "SET    DATA_FINE_RUOLO = TRUNC(SYSDATE), " +          
               "       DATA_FINE_RUOLO_MOD = TRUNC(SYSDATE) " + 
               "WHERE  ID_AZIENDA = ? " +
               "AND    ID_RUOLO = 1 " +
               "AND    DATA_FINE_RUOLO IS NULL ";
  
      stmt = conn.prepareStatement(update);
      stmt.setLong(1, idAzienda);
  
      SolmrLogger.debug(this, "Executing query updateContitolareAT: "
       + update);
  
      stmt.executeUpdate();
      stmt.close();  
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in updateContitolareAT: "
        + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in updateContitolareAT: "
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
                "SQLException while closing Statement and Connection in updateContitolareAT: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in updateContitolareAT: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }
  
  
  public Long insertPersonaFisicaNew(PersonaFisicaVO pfVO, long idUtente)
      throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_PERSONA_FISICA);
      conn = getDatasource().getConnection();

      String insert = 
        "INSERT INTO DB_PERSONA_FISICA "+
        "  (ID_PERSONA_FISICA, ID_SOGGETTO, CODICE_FISCALE, " +
        "   COGNOME, NOME, SESSO, " +
        "   NASCITA_COMUNE, NASCITA_DATA, DATA_AGGIORNAMENTO, " +
        "   ID_UTENTE_AGGIORNAMENTO, RES_INDIRIZZO, DATA_INIZIO_RESIDENZA) " +
        " VALUES  (?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?, ?, SYSDATE) ";

      stmt = conn.prepareStatement(insert);
      int indice = 0;
      stmt.setLong(++indice, primaryKey.longValue());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(pfVO.getIdSoggetto()));
      String codiceFiscale = null;
      if(Validator.isNotEmpty(pfVO.getCodiceFiscale()))
      {
        codiceFiscale = pfVO.getCodiceFiscale().toUpperCase();
      }
      stmt.setString(++indice, codiceFiscale);
      String cognome = null;
      if(Validator.isNotEmpty(pfVO.getCognome()))
      {
        cognome = pfVO.getCognome().toUpperCase().trim();
      }
      stmt.setString(++indice, cognome);
      String nome = null;
      if(Validator.isNotEmpty(pfVO.getNome()))
      {
        nome = pfVO.getNome().toUpperCase().trim();
      }
      stmt.setString(++indice, nome);
      stmt.setString(++indice, pfVO.getSesso());
      stmt.setString(++indice, pfVO.getNascitaComune());
      stmt.setTimestamp(++indice, convertDateToTimestamp(pfVO.getNascitaData()));      
      stmt.setLong(++indice, idUtente);
      stmt.setString(++indice, pfVO.getResIndirizzo());

      SolmrLogger.debug(this, "Executing insert: " + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insert.");
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
    return primaryKey;
  }
  
  public Long insertPersonaGiuridica(PersonaGiuridicaVO pgVO, long idUtente)
      throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_PERSONA_GIURIDICA);
      conn = getDatasource().getConnection();

      String insert = 
        "INSERT INTO DB_PERSONA_GIURIDICA "+
        "  (ID_PERSONA_GIURIDICA, ID_SOGGETTO, CODICE_FISCALE, " +
        "   DENOMINAZIONE, PARTITA_IVA, COMUNE, " +
        "   DATA_AGGIORNAMENTO, ID_UTENTE_AGGIORNAMENTO) " +
        " VALUES  (?, ?, ?, ?, ?, ?, SYSDATE, ?) ";

      stmt = conn.prepareStatement(insert);
      int indice = 0;
      stmt.setLong(++indice, primaryKey.longValue());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(pgVO.getIdSoggetto()));
      String codiceFiscale = null;
      if(Validator.isNotEmpty(pgVO.getCodiceFiscale()))
      {
        codiceFiscale = pgVO.getCodiceFiscale().toUpperCase();
      }
      stmt.setString(++indice, codiceFiscale);
      String denominazione = null;
      if(Validator.isNotEmpty(pgVO.getDenominazione()))
      {
        denominazione = pgVO.getDenominazione().toUpperCase().trim();
      }
      stmt.setString(++indice, denominazione);
      stmt.setString(++indice, pgVO.getPartitaIva());
      stmt.setString(++indice, pgVO.getIstatComune());      
      stmt.setLong(++indice, idUtente);

      SolmrLogger.debug(this, "Executing insert: " + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insert.");
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
    return primaryKey;
  }
  
  
}
