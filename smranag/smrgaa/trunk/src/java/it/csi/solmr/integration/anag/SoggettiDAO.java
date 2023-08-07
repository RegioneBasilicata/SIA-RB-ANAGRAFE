package it.csi.solmr.integration.anag;

import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.dto.anag.TesserinoFitoSanitarioVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.SolmrErrors;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.NotFoundException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

public class SoggettiDAO extends it.csi.solmr.integration.BaseDAO
{

  public SoggettiDAO() throws ResourceAccessException
  {
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public SoggettiDAO(String refName) throws ResourceAccessException {
    super(refName);
  }

  // Recupera i soggetti collegati ad una
  // azienda partendo dall'idAzienda e dalla "data fotografia"
  public Vector<PersonaFisicaVO> getSoggetti(Long idAzienda, java.util.Date data)
      throws DataAccessException, NotFoundException
  {
    Vector<PersonaFisicaVO> result = null;
    PersonaFisicaVO personaVO = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try 
    {
      conn = getDatasource().getConnection();
      String query = 
        "SELECT pf.id_persona_fisica, " +
        "       pf.id_soggetto, " +
        "       tr.descrizione, " +
        "       pf.nome, "+
        "       pf.cognome, " +
        "       pf.codice_fiscale, " +
        "       c.data_inizio_ruolo, " +
        "       c.data_fine_ruolo, "+
        "       c.data_inizio_ruolo_mod, " +
        "       c.data_fine_ruolo_mod," +
        "       C.ID_RUOLO "+
        "FROM   db_contitolare c, " +
        "       db_soggetto s, "+
        "       db_persona_fisica pf, " +
        "       db_tipo_ruolo tr "+
        "WHERE  c.id_soggetto = s.id_soggetto "+
        "AND    s.id_soggetto = pf.id_soggetto "+
        "AND    tr.id_ruolo = c.id_ruolo "+
        "AND    c.id_azienda = ? "+
        "AND    c.data_inizio_ruolo <= ? "+
        "AND    (c.data_fine_ruolo is null or c.data_fine_ruolo >= ?) " +
        "ORDER BY C.ID_RUOLO,COGNOME, DATA_INIZIO_RUOLO ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: "+query);

      stmt.setLong(1, idAzienda.longValue());
      String fine = "31/12/"+DateUtils.extractYearFromDate(data);
      String inizio = "01/01/"+DateUtils.extractYearFromDate(data);
      stmt.setDate(2, new java.sql.Date(DateUtils.parseDate(fine).getTime()));
      stmt.setDate(3, new java.sql.Date(DateUtils.parseDate(inizio).getTime()));


      ResultSet rs = stmt.executeQuery();

      result = new Vector<PersonaFisicaVO>();
      while (rs.next())
      {
        personaVO = new PersonaFisicaVO();
        personaVO.setIdPersonaFisica(new Long(rs.getLong(1)));
        personaVO.setIdSoggetto(new Long(rs.getLong(2)));
        personaVO.setRuolo(rs.getString(3));
        personaVO.setNome(rs.getString(4));
        personaVO.setCognome(rs.getString(5));
        personaVO.setCodiceFiscale(rs.getString(6));
        personaVO.setDataInizioRuolo(rs.getDate(7));
        personaVO.setDataFineRuolo(rs.getDate(8));
        personaVO.setDataInizioRuoloMod(rs.getDate(9));
        personaVO.setDataFineRuoloMod(rs.getDate(10));
        personaVO.setIdRuolo(new Long(rs.getLong(11)));
        result.add(personaVO);
      }

      if(result.size() == 0) {
        throw new NotFoundException((String)AnagErrors.get("SOGGETTO_NON_PRESENTE"));
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "getSoggetti - Found "+result.size()+" record(s)");
    }
    catch(SQLException exc) {
      SolmrLogger.fatal(this, "getSoggetti - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (NotFoundException nex) {
      SolmrLogger.fatal(this, "getSoggetti - NotFoundException");
      throw nex;
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this, "getSoggetti - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this, "getSoggetti - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this, "getSoggetti - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // Recupera i soggetti collegati ad una
  // azienda partendo dall'idAzienda
  public Vector<PersonaFisicaVO> getSoggetti(Long idAzienda, Boolean storico) throws DataAccessException, NotFoundException {
    Vector<PersonaFisicaVO> result = null;
    PersonaFisicaVO personaVO = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try {
      conn = getDatasource().getConnection();

      String query = 
          " SELECT PF.ID_PERSONA_FISICA, " +
          "        PF.ID_SOGGETTO, " +
          "        TR.DESCRIZIONE, " +
          "        TR.ID_RUOLO, " +
          "        PF.NOME, " +
          "        PF.COGNOME, " +
          "        PF.CODICE_FISCALE, " +
          "		     NVL(TO_CHAR(PF.PREFISSO_INTER_CELLULARE,'0000'),'') AS PREFISSO_INTER_CELLULARE, " + 
			 		"		     NVL(PF.ID_PREFISSO_CELLULARE,'') AS ID_PREFISSO_CELLULARE,  " + 
			 		"		     NVL(PF.NUMERO_CELLULARE,'')AS NUMERO_CELLULARE, " +
          "        C.DATA_INIZIO_RUOLO, " +
          "        C.DATA_FINE_RUOLO, " +
          "        C.ID_CONTITOLARE, " +
          "        C.DATA_INIZIO_RUOLO_MOD, " +
          "        C.DATA_FINE_RUOLO_MOD "+
          " FROM   DB_CONTITOLARE C, " +
          "        DB_SOGGETTO S, " +
          "        DB_PERSONA_FISICA PF, " +
          "        DB_TIPO_RUOLO TR " +
          " WHERE  C.ID_SOGGETTO = S.ID_SOGGETTO " +
          " AND    S.ID_SOGGETTO = PF.ID_SOGGETTO " +
          " AND    TR.ID_RUOLO = C.ID_RUOLO "+
          " AND    C.ID_AZIENDA = ? ";

      if (storico !=null && storico.booleanValue()) 
      {
        query += " ORDER BY C.ID_RUOLO, COGNOME, DATA_INIZIO_RUOLO ";
      }
      else 
      {
        // Visualizzo solo le ute con data fine ruolo a null
        query += " AND C.DATA_FINE_RUOLO IS NULL ";
        query += " ORDER BY C.ID_RUOLO, COGNOME, DATA_INIZIO_RUOLO ";
      }


      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query getSoggetti in soggettiDAO: "+query);

      stmt.setLong(1, idAzienda.longValue());


      ResultSet rs = stmt.executeQuery();

      result = new Vector<PersonaFisicaVO>();
      while(rs.next()) 
      {
        personaVO = new PersonaFisicaVO();
        personaVO.setIdPersonaFisica(new Long(rs.getLong("ID_PERSONA_FISICA")));
        personaVO.setIdSoggetto(new Long(rs.getLong("ID_SOGGETTO")));
        personaVO.setRuolo(rs.getString("DESCRIZIONE"));
        personaVO.setIdRuolo(new Long(rs.getLong("ID_RUOLO")));
        personaVO.setNome(rs.getString("NOME"));
        personaVO.setCognome(rs.getString("COGNOME"));
        personaVO.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
        personaVO.setdesPrefissoCellulareInt(rs.getString("PREFISSO_INTER_CELLULARE"));
        personaVO.setIdPrefissoCellulareNaz(new Long(rs.getLong("ID_PREFISSO_CELLULARE")));
        personaVO.setdesNumeroCellulare(rs.getString("NUMERO_CELLULARE"));       
        personaVO.setDataInizioRuolo(rs.getTimestamp("DATA_INIZIO_RUOLO"));
        personaVO.setDataFineRuolo(rs.getTimestamp("DATA_FINE_RUOLO"));
        if(Validator.isNotEmpty(rs.getString("ID_CONTITOLARE"))) {
          personaVO.setIdContitolare(new Long(rs.getLong("ID_CONTITOLARE")));
        }
        else {
          personaVO.setIdContitolare(null);
        }
        personaVO.setDataInizioRuoloMod(rs.getDate("DATA_INIZIO_RUOLO_MOD"));
        personaVO.setDataFineRuoloMod(rs.getDate("DATA_FINE_RUOLO_MOD"));
        result.add(personaVO);
      }

      if(result.size() == 0) {
        throw new NotFoundException((String)AnagErrors.get("SOGGETTO_NON_PRESENTE"));
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "getSoggetti in soggettiDAO - Found "+result.size()+" record(s)");
    }
    catch(SQLException exc) {
      SolmrLogger.fatal(this, "getSoggetti in soggettiDAO - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (NotFoundException nex) {
      SolmrLogger.fatal(this, "getSoggetti in soggettiDAO - NotFoundException");
      throw nex;
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this, "getSoggetti in soggettiDAO - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this, "getSoggetti in soggettiDAO - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this, "getSoggetti in soggettiDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  public int getSoggettiForDelete(Long idSoggetto, Long idAzienda)throws DataAccessException, SolmrException{
    int count = 0;
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = getDatasource().getConnection();
      String query = "SELECT count(*) "+
                     "FROM db_contitolare c, db_soggetto s, "+
                     "db_persona_fisica pf, db_tipo_ruolo tr "+
                     "WHERE c.id_soggetto = s.id_soggetto "+
                     "AND s.id_soggetto = pf.id_soggetto "+
                     "AND tr.id_ruolo = c.id_ruolo "+
                     "AND c.id_soggetto = ? "+
                     "AND c.id_azienda <> ? ";
      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query: "+query);

      stmt.setLong(1, idSoggetto.longValue());
      stmt.setLong(2, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs!=null){
        if(rs.next()){
          count = rs.getInt(1);
        }
      }
      else
        throw new DataAccessException();

      SolmrLogger.debug(this, "getSoggettiForDelete - Found "+count+" record(s)");
    }
    catch(SQLException exc) {
      SolmrLogger.fatal(this, "getSoggettiForDelete - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc) {
      SolmrLogger.fatal(this, "getSoggettiForDelete - ResultSet null");
      throw daexc;
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this, "getSoggettiForDelete - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this, "getSoggettiForDelete - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this, "getSoggettiForDelete - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return count;
  }

  // Una volta selezionato un soggetto, il metodo ne recupera i dati per la visualizzazione
  // del dettaglio
  public PersonaFisicaVO getDettaglioSoggetti(Long idSoggetto, Long idAzienda, java.util.Date data)
      throws DataAccessException,NotFoundException
  {
    PersonaFisicaVO pfVO = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = getDatasource().getConnection();
      String query = "SELECT c.id_contitolare, id_persona_fisica, c.id_ruolo, tr.descrizione, data_inizio_ruolo, "+
                     "data_fine_ruolo, cognome, nome, pf.codice_fiscale, sesso, "+
                     "nascita_data, cn.istat_comune, cn.flag_estero alias_f_e, cn.descom comune_nascita, "+
                     "nascita_citta_estero, pn.sigla_provincia sigla_prov, "+
                     "res_indirizzo, cr.flag_estero alias_f_e2, res_comune, "+
                     "pf.res_citta_estero, cr.descom comune_residenza, res_cap, "+
                     "pr.sigla_provincia prov_res, res_telefono,res_fax, res_mail, note, "+
                     "data_aggiornamento, " +
                     "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
                     "          || ' ' " + 
                     "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
                     "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
                     "         WHERE pf.id_utente_aggiornamento = PVU.ID_UTENTE_LOGIN) " + 
                     "  AS denominazione, " +
                     "dom_indirizzo, "+
                     "dom_comune, dom_citta_estero, dom_cap, "+
                     " TTS.ID_TITOLO_STUDIO,  TTS.DESCRIZIONE, TIS.ID_INDIRIZZO_STUDIO, " +
                     " TIS.DESCRIZIONE,  CD.FLAG_ESTERO, CD.DESCOM, PD.SIGLA_PROVINCIA, " +
                     " TRUNC(pf.DATA_INIZIO_RESIDENZA) AS DATA_INIZIO_RESIDENZA, "+
                     " PF.FLAG_CF_OK " +
                     "FROM db_contitolare c, db_persona_fisica pf, comune cn, "+
                     "comune cr, provincia pn, provincia pr, db_tipo_ruolo tr, "+
                     "db_soggetto s, " +
                     //"PAPUA_V_UTENTE_LOGIN PVU, " +
                     "COMUNE CD, PROVINCIA PD, "+
                     " DB_TIPO_TITOLO_STUDIO TTS, DB_TIPO_INDIRIZZO_STUDIO TIS " +
                     "WHERE pf.nascita_comune = cn.istat_comune "+
                     "AND cn.istat_provincia = pn.istat_provincia(+) "+
                     "AND cr.istat_provincia = pr.istat_provincia(+) "+
                     "AND CD.ISTAT_PROVINCIA = PD.ISTAT_PROVINCIA(+) "+
                     "AND c.id_azienda = ? "+
                     " AND c.id_soggetto = s.id_soggetto "+
                     "AND pf.id_soggetto = s.id_soggetto "+
                     "AND pf.res_comune = cr.istat_comune "+
                     "AND tr.id_ruolo = c.id_ruolo "+
                     //"AND PVU.ID_UTENTE_LOGIN = pf.id_utente_aggiornamento "+
                     "AND pf.id_soggetto = ? "+
                     " AND PF.ID_TITOLO_STUDIO = TTS.ID_TITOLO_STUDIO(+) " +
                     " AND PF.ID_INDIRIZZO_STUDIO = TIS.ID_INDIRIZZO_STUDIO(+) " +
                     " AND PF.DOM_COMUNE = CD.ISTAT_COMUNE(+) ";

      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query: "+query);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setLong(2, idSoggetto.longValue());

      ResultSet rs = stmt.executeQuery();

      if(rs!=null && rs.next()){
        pfVO = new PersonaFisicaVO();
        pfVO.setIdContitolare(new Long(rs.getLong(1)));
        pfVO.setIdPersonaFisica(new Long(rs.getLong(2)));
        pfVO.setIdRuolo(new Long(rs.getLong(3))); // ID_RUOLO su DB_CONTITOLARE
        pfVO.setTipiRuoloNonTitolare(new Long(rs.getLong(3)));
        pfVO.setTipiRuoloNonTitolareAndNonSpecificato(new Long(rs.getLong(3)));// Valore settato solo per l'applicativo non serve per i servizi
        pfVO.setRuolo(rs.getString(4));
        pfVO.setDataInizioRuolo(rs.getDate(5));
        if(rs.getDate(6)!=null)
          pfVO.setDataFineRuolo(rs.getDate(6));
        pfVO.setCognome(rs.getString(7));
        pfVO.setNome(rs.getString(8));
        pfVO.setCodiceFiscale(rs.getString(9));
        pfVO.setSesso(rs.getString(10));
        pfVO.setNascitaData(rs.getDate(11));
        pfVO.setNascitaComune(rs.getString(12));
        if(rs.getString(13)!=null&&rs.getString(13).equals(SolmrConstants.FLAG_N)){
          pfVO.setDescNascitaComune(rs.getString(14));
          pfVO.setNascitaProv(rs.getString(16));
        }
        else{
          pfVO.setNascitaStatoEstero(rs.getString(14));
          pfVO.setNascitaCittaEstero(rs.getString(15));
        }
        pfVO.setResIndirizzo(rs.getString(17));
        if(rs.getString(18)!=null&&rs.getString(18).equals(SolmrConstants.FLAG_N)){
          pfVO.setResComune(rs.getString(19));
          pfVO.setDescResComune(rs.getString(21));
          pfVO.setResCAP(rs.getString(22));
          pfVO.setDescResProvincia(rs.getString(23));
        }
        else{
          pfVO.setResCittaEstero(rs.getString(20));
          pfVO.setDescStatoEsteroResidenza(rs.getString(21));
        }
        pfVO.setResTelefono(rs.getString(24));
        pfVO.setResFax(rs.getString(25));
        pfVO.setResMail(rs.getString(26));
        pfVO.setNote(rs.getString(27));
        pfVO.setDataAggiornamento(rs.getDate(28));
        pfVO.setDescUtenteAggiornamento(rs.getString(29));
        pfVO.setDomIndirizzo(rs.getString(30));
        pfVO.setIstatComuneDomicilio(rs.getString(31));
        pfVO.setDescCittaEsteroDomicilio(rs.getString(32));
        pfVO.setDomCAP(rs.getString(33));
        if(rs.getString(34) != null) {
          pfVO.setIdTitoloStudio(new Long(rs.getLong(34)));
          pfVO.setDescrizioneTitoloStudio(rs.getString(35));
        }
        if(rs.getString(36) != null) {
          pfVO.setIdIndirizzoStudio(new Long(rs.getLong(36)));
          pfVO.setDescrizioneIndirizzoStudio(rs.getString(37));
        }
        if((SolmrConstants.FLAG_N).equalsIgnoreCase(rs.getString(38))) {
          pfVO.setDomComune(rs.getString(39));
          pfVO.setDomProvincia(rs.getString(40));
        }
        else {
          pfVO.setDomicilioStatoEstero(rs.getString(39));
          pfVO.setDomProvincia(null);
        }
        pfVO.setDataInizioResidenza(rs.getDate("DATA_INIZIO_RESIDENZA"));
        pfVO.setFlagCfOk("FLAG_CF_OK");
      }
      else
        throw new NotFoundException();

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "getDettaglioSoggetti - Found record with primary key: "+idSoggetto);
    }
    catch (NotFoundException nexc) {
      SolmrLogger.fatal(this, "getDettaglioSoggetti - ResultSet null");
      throw nexc;
    }
    catch(SQLException exc) {
      SolmrLogger.fatal(this, "getDettaglioSoggetti - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this, "getDettaglioSoggetti - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this, "getDettaglioSoggetto - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this, "getDettaglioSoggetto - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return pfVO;
  }

  public void checkSoggettoAziendaIns(PersonaFisicaVO pfVO) throws DataAccessException, SolmrException {
    Connection conn = null;
    PreparedStatement stmt = null;
    PersonaFisicaVO result = null;
    try {
      conn = getDatasource().getConnection();
      String query = "SELECT c.id_contitolare, c.data_inizio_ruolo, c.data_fine_ruolo "+
                     "FROM db_contitolare c, "+
                     "db_persona_fisica pf, "+
                     "db_soggetto s "+
                     "WHERE c.id_azienda = ? and "+
                     "pf.id_persona_fisica = ? and "+
                     "pf.id_soggetto = s.id_soggetto and "+
                     "c.id_soggetto = s.id_soggetto "+
                     "order by c.data_inizio_ruolo desc";
      stmt = conn.prepareStatement(query);
      stmt.setLong(1, pfVO.getIdAzienda().longValue());
      stmt.setLong(2, pfVO.getIdPersonaFisica().longValue());
      SolmrLogger.debug(this, "Executing query: "+query);
      ResultSet rs = stmt.executeQuery();
      if (rs != null) {
        while (rs.next()) {
          if(rs.isFirst()){
            result = new PersonaFisicaVO();
            result.setIdContitolare(new Long(rs.getLong(1)));
            result.setDataInizioRuolo(rs.getDate(2));
            result.setDataFineRuolo(rs.getDate(3));
          }
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      if(result!=null){
        if(result.getDataFineRuolo()==null){
          throw new SolmrException(""+AnagErrors.get("INSERISCI_SOGGETTO"));
        }
        else if(result.getDataFineRuolo().before(pfVO.getDataInizioRuolo())){
          throw new SolmrException(""+AnagErrors.get("ERR_DATA_INIZIO_RUOLO"));
        }
      }
      SolmrLogger.debug(this, "Executed query - Found "+result+" result(s).");
    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (DataAccessException daexc) {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    } catch (SolmrException sexc) {
      SolmrLogger.fatal(this, "SolmrException: "+sexc.getMessage());
      throw sexc;
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    } finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }
  public void checkSoggettoAziendaMod(PersonaFisicaVO pfVO) throws DataAccessException, SolmrException {
    Connection conn = null;
    PreparedStatement stmt = null;
    PersonaFisicaVO result = null;
    try {
      conn = getDatasource().getConnection();
      String query = "SELECT c.id_contitolare, c.data_fine_ruolo "+
                     "FROM db_contitolare c, "+
                     "db_persona_fisica pf, "+
                     "db_soggetto s "+
                     "WHERE c.id_azienda = ? and "+
                     "pf.id_persona_fisica = ? and "+
                     "pf.id_soggetto = s.id_soggetto and "+
                     "c.id_soggetto = s.id_soggetto and "+
                     "c.id_contitolare <> ? "+
                     "order by c.data_inizio_ruolo desc";
      stmt = conn.prepareStatement(query);
      stmt.setLong(1, pfVO.getIdAzienda().longValue());
      stmt.setLong(2, pfVO.getIdPersonaFisica().longValue());
      stmt.setLong(3, pfVO.getIdContitolare().longValue());
      SolmrLogger.debug(this, "Executing query: "+query);
      ResultSet rs = stmt.executeQuery();
      if (rs != null) {
        while (rs.next()) {
          if(rs.isFirst()){
            result = new PersonaFisicaVO();
            result.setIdContitolare(new Long(rs.getLong(1)));
            result.setDataFineRuolo(rs.getDate(2));
          }
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();


      SolmrLogger.debug(this, "Executed query - Found "+result+" result(s).");
    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (DataAccessException daexc) {
      SolmrLogger.fatal(this, "ResultSet null");
      throw daexc;
    } /*catch (SolmrException sexc) {
      SolmrLogger.fatal(this, "SolmrException: "+sexc.getMessage());
      throw sexc;
    }*/ catch (Exception ex) {
      SolmrLogger.fatal(this, "Generic Exception: "+ex.getMessage());
      throw new SolmrException(ex.getMessage());
    } finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new SolmrException(ex.getMessage());
      }
    }
  }
  public void deleteContitolare(Long idSoggetto) throws DataAccessException, SolmrException{
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = getDatasource().getConnection();

      String delete = "DELETE from DB_CONTITOLARE "+
                      " WHERE ID_CONTITOLARE = ? ";

      stmt = conn.prepareStatement(delete);

      SolmrLogger.debug(this, "Executing delete deleteContitolare: "+delete);

      stmt.setLong(1, idSoggetto.longValue());
      stmt.executeUpdate();

      SolmrLogger.debug(this, "Executed delete deleteContitolare.");
    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "deleteContitolare - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "deleteContitolare - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    } finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "deleteContitolare - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "deleteContitolare - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  public void deletePersonaFisica(Long idSoggetto) throws DataAccessException, SolmrException{
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = getDatasource().getConnection();

      String delete = "DELETE from DB_PERSONA_FISICA "+
                      " WHERE ID_SOGGETTO = ? ";

      stmt = conn.prepareStatement(delete);

      SolmrLogger.debug(this, "Executing delete: "+delete);

      stmt.setLong(1, idSoggetto.longValue());

      stmt.executeUpdate();

      SolmrLogger.debug(this, "Executed delete.");
    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "deletePersonaFisica - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "deletePersonaFisica - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    } finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "deletePersonaFisica - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "deletePersonaFisica - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  public void deletePersonaGiuridica(Long idSoggetto) throws DataAccessException, SolmrException{
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = getDatasource().getConnection();

      String delete = "DELETE from DB_PERSONA_GIURIDICA "+
                      " WHERE ID_SOGGETTO = ? ";

      stmt = conn.prepareStatement(delete);

      SolmrLogger.debug(this, "Executing delete: "+delete);

      stmt.setLong(1, idSoggetto.longValue());

      stmt.executeUpdate();

      SolmrLogger.debug(this, "Executed delete.");
    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "deletePersonaGiuridica - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "deletePersonaGiuridica - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    } finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "deletePersonaGiuridica - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "deletePersonaGiuridica - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  public void deleteSoggetto(Long idSoggetto) throws DataAccessException, SolmrException{
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = getDatasource().getConnection();

      String delete = "DELETE from DB_SOGGETTO "+
                      " WHERE ID_SOGGETTO = ? ";

      stmt = conn.prepareStatement(delete);

      SolmrLogger.debug(this, "Executing delete: "+delete);

      stmt.setLong(1, idSoggetto.longValue());

      stmt.executeUpdate();

      SolmrLogger.debug(this, "Executed delete.");
    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "deleteSoggetto - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "deleteSoggetto - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    } finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "deleteSoggetto - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "deleteSoggetto - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  public Long insertContitolareRuolo(Long idAzienda, Long idSoggetto, Long idRuolo, java.util.Date data) throws DataAccessException {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_CONTITOLARE);
      conn = getDatasource().getConnection();

      String insert = "INSERT INTO Db_Contitolare "+
                      " (id_contitolare, id_soggetto, id_ruolo, "+
                      "  id_azienda, data_inizio_ruolo,data_inizio_ruolo_mod) "+
                      "     VALUES (?, ?, ?, ?, TRUNC(SYSDATE),?) ";
      stmt = conn.prepareStatement(insert);

      stmt.setLong(1, primaryKey.longValue());
      stmt.setLong(2, idSoggetto.longValue());
      stmt.setLong(3, idRuolo.longValue());
      stmt.setLong(4, idAzienda.longValue());
      stmt.setDate(5, new java.sql.Date(data.getTime()));

      SolmrLogger.debug(this, "Executing insert: "+insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insert.");
    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    } finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }

  public Vector<Long> getIdPersoneFisiche(String codFiscale, String cognome, String nome,
                                    String dataNascita, String istatNascita,
                                    String istatResidenza, boolean personaAttiva)
      throws DataAccessException, SolmrException {

    Vector<Long> result;
    Connection conn = null;
    PreparedStatement stmt = null;
    String condizioni = "";
    Vector<Object> daParam = new Vector<Object>();

    try {

      conn = getDatasource().getConnection();

      String query = "SELECT DISTINCT PF.ID_PERSONA_FISICA, PF.COGNOME, PF.NOME "+
                     "FROM DB_PERSONA_FISICA PF, DB_CONTITOLARE C,DB_AZIENDA A,"+
                     "DB_ANAGRAFICA_AZIENDA AA "+
                     "WHERE ";

      if(!codFiscale.equals("")){
        condizioni += "PF.CODICE_FISCALE = UPPER(?) ";
        daParam.add(codFiscale);
      }
      if(!cognome.equals("")) {
        if(condizioni.length() > 0)
          condizioni += " AND ";
        condizioni += " PF.COGNOME LIKE ? ";
        daParam.add((cognome + "%").toUpperCase());
        if(!nome.equals("")){
          condizioni += " AND PF.NOME LIKE ? ";
          daParam.add((nome + "%").toUpperCase());
        }
      }
      if(dataNascita != null && !dataNascita.equals("")) {
        condizioni += " AND PF.NASCITA_DATA = TO_DATE(?, 'DD/MM/YYYY') ";
        daParam.add(dataNascita);
      }
      if(istatNascita != null && !istatNascita.equals("")) {
        condizioni += " AND PF.NASCITA_COMUNE = ? ";
        daParam.add(istatNascita);
      }
      if(istatResidenza != null && !istatResidenza.equals("")) {
        condizioni += " AND PF.RES_COMUNE = ? ";
        daParam.add(istatResidenza);
      }
      if (personaAttiva)
      {
        condizioni += " AND C.DATA_FINE_RUOLO IS NULL ";
        condizioni += " AND AA.DATA_CESSAZIONE IS NULL ";
      }

      condizioni += " AND PF.ID_SOGGETTO = C.ID_SOGGETTO ";
      condizioni += " AND A.ID_AZIENDA = C.ID_AZIENDA ";
      condizioni += " AND A.ID_AZIENDA = AA.ID_AZIENDA ";
      condizioni += " ORDER BY PF.COGNOME, PF.NOME";

      stmt = conn.prepareStatement(query+condizioni);
      SolmrLogger.debug(this, "Executing query: "+query+condizioni);

      this.fillStatementWithParameters(stmt, daParam);
      ResultSet rs = stmt.executeQuery();

      if (rs != null) {
        result = new Vector<Long>();
        while (rs.next()) {
          result.add(new Long(rs.getLong("ID_PERSONA_FISICA")));

          if(rs.getRow()>new Integer(""+SolmrConstants.get("NUM_MAX_ROWS_RESULT")).intValue())
            throw new SolmrException(SolmrErrors.EXC_TROPPI_RECORD_SELEZIONATI);
        }
        if(result.size()==0)
          throw new SolmrException(""+AnagErrors.get("NESSUNA_PERSONA_TROVATA"));
      } else
        throw new DataAccessException();

    }
    catch(SQLException exc) {
      SolmrLogger.fatal(this, "getIdPersoneFisiche - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (SolmrException sex) {
      SolmrLogger.fatal(this, "getIdPersoneFisiche - SolmrException");
      throw sex;
    }
     catch (Exception ex) {
      SolmrLogger.fatal(this, "getIdPersoneFisiche - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this, "getIdPersoneFisiche - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this, "getIdPersoneFisiche - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  public Vector<PersonaFisicaVO> getListPersoneFisicheByIdRange(Vector<Long> collIdPF) 
      throws DataAccessException, SolmrException 
  {
    Vector<PersonaFisicaVO> result = null;
    Connection conn = null;
    PersonaFisicaVO pfVO;
    PreparedStatement stmt = null;
    try {
      conn = getDatasource().getConnection();

      Iterator<Long> iterator = collIdPF.iterator();

      String inCondition = ((Long)iterator.next()).toString();
      while(iterator.hasNext()){
        inCondition +=", "+((Long)iterator.next()).toString();
      }
      String query = " SELECT ID_PERSONA_FISICA, ID_SOGGETTO, COGNOME, NOME, CODICE_FISCALE, "+
                     " C.DESCOM ALIAS_DESCRIZIONE_COMUNE, RES_INDIRIZZO, "+
                     " P.SIGLA_PROVINCIA ALIAS_RES_PROV, "+
                     " NASCITA_DATA, C2.DESCOM ALIAS_NASCITA_COMUNE, P2.SIGLA_PROVINCIA ALIAS_NASCITA_PROV, "+
                     " NASCITA_CITTA_ESTERO, RES_CITTA_ESTERO " +
                     " FROM DB_PERSONA_FISICA PF, COMUNE C, PROVINCIA P, COMUNE C2, PROVINCIA P2 "+
                     " WHERE RES_COMUNE = C.ISTAT_COMUNE(+)"+
                     " AND C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+)"+
                     " AND NASCITA_COMUNE= C2.ISTAT_COMUNE(+) "+
                     " AND C2.ISTAT_PROVINCIA = P2.ISTAT_PROVINCIA(+) "+
                     " AND ID_PERSONA_FISICA IN ("+inCondition+") "+
                     " ORDER BY COGNOME, NOME";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: "+query);

      ResultSet rs = stmt.executeQuery();

      if (rs != null) {
        result = new Vector<PersonaFisicaVO>();
        while (rs.next()) {
          pfVO = new PersonaFisicaVO();
          pfVO.setIdPersonaFisica(new Long(rs.getLong("ID_PERSONA_FISICA")));
          pfVO.setIdSoggetto(new Long(rs.getLong("ID_SOGGETTO")));
          pfVO.setCognome(rs.getString("COGNOME"));
          pfVO.setNome(rs.getString("NOME"));
          pfVO.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
          pfVO.setDescResComune(rs.getString("ALIAS_DESCRIZIONE_COMUNE"));
          pfVO.setResIndirizzo(rs.getString("RES_INDIRIZZO"));
          // Aggiunti da Michele il 6/12/2003
          pfVO.setResProvincia(rs.getString("ALIAS_RES_PROV"));
          pfVO.setNascitaData(rs.getDate("NASCITA_DATA"));
          pfVO.setDescNascitaComune(rs.getString("ALIAS_NASCITA_COMUNE"));
          pfVO.setNascitaProv(rs.getString("ALIAS_NASCITA_PROV"));
          pfVO.setNascitaCittaEstero(rs.getString("NASCITA_CITTA_ESTERO"));
          pfVO.setResCittaEstero(rs.getString("RES_CITTA_ESTERO"));

          result.add(pfVO);
        }
      } 
      else  throw new SolmrException(""+AnagErrors.get("NESSUNA_PERSONA_TROVATA"));

      
    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "getListPersoneFisicheByIdRange - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }catch (SolmrException exc) {
      SolmrLogger.fatal(this, "getListPersoneFisicheByIdRange - SolmrException: "+exc.getMessage());
      throw exc;
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "getListPersoneFisicheByIdRange - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    } finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "getListPersoneFisicheByIdRange - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "getListPersoneFisicheByIdRange - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }


  public PersonaFisicaVO findByPrimaryKey(Long idPersonaFisica) throws DataAccessException, SolmrException {

    PersonaFisicaVO pf = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<PersonaFisicaVO> result = new Vector<PersonaFisicaVO>();

    try {
      conn = getDatasource().getConnection();

      String query = "SELECT id_persona_fisica, "+
                     "db_persona_fisica.id_soggetto, "+
                     "codice_fiscale, "+
                     "cognome, "+
                     "nascita_comune, "+
                     "nome, "+
                     "sesso, "+
                     "res_comune , "+
                     "nascita_data, "+
                     "res_indirizzo, "+
                     "res_cap, "+
                     "res_telefono, "+
                     "res_fax, "+
                     "res_mail, "+
                     "data_aggiornamento, "+
                     "note, "+
                     "id_utente_aggiornamento, "+
                     "dom_indirizzo, "+
                     "dom_comune, "+
                     " DOM_CITTA_ESTERO, "+
                     "dom_cap, "+
                     "res_citta_estero, "+
                     "nascita_citta_estero, "+
                     "C1.DESCOM, C2.DESCOM, tr.DESCRIZIONE, "+
                     "db_contitolare.DATA_INIZIO_RUOLO, db_contitolare.DATA_FINE_RUOLO, "+
                     " TTS.ID_TITOLO_STUDIO,  TTS.DESCRIZIONE, TIS.ID_INDIRIZZO_STUDIO, " +
                     " TIS.DESCRIZIONE, C3.FLAG_ESTERO, C3.DESCOM, P3.SIGLA_PROVINCIA " +
                     "FROM DB_PERSONA_FISICA , db_contitolare, db_tipo_ruolo tr, "+
                     "COMUNE C1, COMUNE C2, COMUNE C3, PROVINCIA P1, PROVINCIA P2, PROVINCIA P3, " +
                     " DB_TIPO_TITOLO_STUDIO TTS, DB_TIPO_INDIRIZZO_STUDIO TIS " +
                     "WHERE id_persona_fisica = ? "+
                     " and DB_PERSONA_FISICA.RES_COMUNE=C1.ISTAT_COMUNE(+) "+
                     " and DB_PERSONA_FISICA.NASCITA_COMUNE=C2.ISTAT_COMUNE(+) "+
                     " AND DB_PERSONA_FISICA.DOM_COMUNE = C3.ISTAT_COMUNE(+) "+
                     " and C1.ISTAT_PROVINCIA=P1.ISTAT_PROVINCIA(+) "+
                     " and C2.ISTAT_PROVINCIA=P2.ISTAT_PROVINCIA(+)"+
                     " AND C3.ISTAT_PROVINCIA = P3.ISTAT_PROVINCIA(+)"+
                     " and db_contitolare.ID_SOGGETTO = DB_PERSONA_FISICA.ID_SOGGETTO "+
                     " and db_contitolare.ID_RUOLO = tr.ID_RUOLO " +
                     " AND DB_PERSONA_FISICA.ID_TITOLO_STUDIO = TTS.ID_TITOLO_STUDIO(+) " +
                     " AND DB_PERSONA_FISICA.ID_INDIRIZZO_STUDIO = TIS.ID_INDIRIZZO_STUDIO(+) ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: "+query);

      stmt.setLong(1, idPersonaFisica.longValue());
      ResultSet rs = stmt.executeQuery();

      if (rs != null) {
        while (rs.next()) {
          pf = new PersonaFisicaVO();

          pf.setIdPersonaFisica(new Long(rs.getLong("ID_PERSONA_FISICA")));
          pf.setIdSoggetto(new Long(rs.getLong("ID_SOGGETTO")));
          pf.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
          pf.setCognome(rs.getString("COGNOME"));
          pf.setNascitaComune(rs.getString("NASCITA_COMUNE"));
          pf.setNome(rs.getString("NOME"));
          pf.setSesso(rs.getString("SESSO"));
          pf.setResComune(rs.getString("RES_COMUNE"));
          pf.setNascitaData(rs.getDate("NASCITA_DATA"));
          pf.setResIndirizzo(rs.getString("RES_INDIRIZZO"));
          pf.setResCAP(rs.getString("RES_CAP"));
          pf.setResTelefono(rs.getString("RES_TELEFONO"));
          pf.setResFax(rs.getString("RES_FAX"));
          pf.setResMail(rs.getString("RES_MAIL"));
          pf.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
          pf.setNote(rs.getString("NOTE"));
          pf.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
          pf.setDomIndirizzo(rs.getString("DOM_INDIRIZZO"));
          pf.setDomComune(rs.getString("DOM_COMUNE"));
          pf.setDescCittaEsteroDomicilio(rs.getString("DOM_CITTA_ESTERO"));
          pf.setDomCAP(rs.getString("DOM_CAP"));
          pf.setResCittaEstero(rs.getString("RES_CITTA_ESTERO"));
          pf.setNascitaCittaEstero(rs.getString("NASCITA_CITTA_ESTERO"));
          pf.setDescResComune(rs.getString(24));
          pf.setDescNascitaComune(rs.getString(25));
          pf.setRuolo(rs.getString(26));//descrizione ruolo
          pf.setDataInizioRuolo(rs.getDate(27));//data inizio ruolo
          pf.setDataFineRuolo(rs.getDate(28));//data fine ruolo
          if(rs.getString(29) != null) {
            pf.setIdTitoloStudio(new Long(rs.getLong(29)));
            pf.setDescrizioneTitoloStudio(rs.getString(30));
          }
          if(rs.getString(31) != null) {
            pf.setIdIndirizzoStudio(new Long(rs.getLong(31)));
            pf.setDescrizioneIndirizzoStudio(rs.getString(32));
          }
          if((SolmrConstants.FLAG_N).equalsIgnoreCase(rs.getString(33))) {
            pf.setDomComune(rs.getString(34));
            pf.setDomProvincia(rs.getString(35));
          }
          else {
            pf.setDomicilioStatoEstero(rs.getString(34));
            pf.setDomProvincia(null);
          }

          result.add(pf);
        }
        if (result.size()==0)
          throw new SolmrException(""+SolmrErrors.get("EXC_NOT_FOUND_PK"));
      } else
        throw new DataAccessException();


      SolmrLogger.debug(this, "Executed query - Found record with primary key: "+idPersonaFisica);
    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "findByPrimaryKey - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (DataAccessException daexc) {
      SolmrLogger.fatal(this, "findByPrimaryKey - ResultSet null");
      throw daexc;
    } catch (SolmrException daexc) {
      SolmrLogger.fatal(this, "findByPrimaryKey - SolmrException");
      throw daexc;
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "findByPrimaryKey - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    } finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "findByPrimaryKey - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "findByPrimaryKey - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return pf;
  }

  public Vector<Long> getIdAziendeBySoggetto(Long idSoggetto) throws DataAccessException, SolmrException{

    Vector<Long> result = new Vector<Long>();
    Connection conn = null;
    PreparedStatement stmt = null;

    try {

      conn = getDatasource().getConnection();

      String query = "SELECT DISTINCT ID_AZIENDA "+
                     " from db_contitolare "+
                     " where ID_SOGGETTO = ?";

      stmt = conn.prepareStatement(query);
      stmt.setLong(1, idSoggetto.longValue());
      SolmrLogger.debug(this, "Executing query: "+query);

      ResultSet rs = stmt.executeQuery();

      if (rs != null) {
        //result = new Vector();
        while (rs.next()) {
          result.add(new Long(rs.getLong("ID_AZIENDA")));
        }
        if(result.size()==0)
          throw new SolmrException(""+AnagErrors.get("NESSUNA_AZIENDA_SOGGETTO"));
      }
      else throw new DataAccessException();
    }
    catch(SQLException exc) {
      SolmrLogger.fatal(this, "getIdAziendeBySoggetto - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (SolmrException sex) {
      SolmrLogger.fatal(this, "getIdAziendeBySoggetto - SolmrException");
      throw sex;
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this, "getIdAziendeBySoggetto - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this, "getIdAziendeBySoggetto - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this, "getIdAziendeBySoggetto - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    //SolmrLogger.debug(this, "SoggettiDAO getIdAziendeBySoggetto, result: "+result);
    return result;
  }


  public PersonaFisicaVO findPersonaFisica(Long idPersonaFisica) 
      throws DataAccessException, SolmrException 
  {

    PersonaFisicaVO pf = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try 
    {
      conn = getDatasource().getConnection();

      String query = 
        "SELECT PF.ID_PERSONA_FISICA, "+
        "       PF.ID_SOGGETTO, "+
        "       PF.CODICE_FISCALE, "+
       "        PF.COGNOME, "+
       "        PF.NASCITA_COMUNE, "+
       "        PF.NOME, "+
       "        PF.SESSO, "+
       "        PF.RES_COMUNE , "+
       "        PF.NASCITA_DATA, "+
       "        PF.RES_INDIRIZZO, "+
       "        PF.RES_CAP, "+
       "        PF.RES_TELEFONO, "+
       "        PF.RES_FAX, "+
       "        PF.RES_MAIL, " +
       "        PF.NUMERO_CELLULARE, "+
       "        PF.DATA_AGGIORNAMENTO, "+
       "        PF.NOTE, "+
       "        PF.ID_UTENTE_AGGIORNAMENTO, "+
       "        PF.DOM_INDIRIZZO, "+
       "        PF.DOM_COMUNE, "+
       "        PF.DOM_CITTA_ESTERO, "+
       "        PF.DOM_CAP, "+
       "        PF.RES_CITTA_ESTERO, "+
       "        PF.NASCITA_CITTA_ESTERO, "+
       "        C1.DESCOM AS RES_DESCOM, " +
       "        C2.DESCOM AS NASC_DESCOM, " +
       "        C3.FLAG_ESTERO AS DOM_FLAG_ESTERO, " +
       "        C3.DESCOM AS DOM_DESCOM, " +
       "        P3.SIGLA_PROVINCIA AS DOM_SIGLA_PROVINCIA, "+
       "        PF.DATA_INIZIO_RESIDENZA, " +
       "        P1.SIGLA_PROVINCIA AS RES_SIGLA_PROVINCIA " +
       "FROM    DB_PERSONA_FISICA PF, "+
       "        COMUNE C1, " +
       "        COMUNE C2, " +
       "        COMUNE C3, " +
       "        PROVINCIA P1, " +
       "        PROVINCIA P2, " +
       "        PROVINCIA P3 "+
       "WHERE   PF.ID_PERSONA_FISICA = ? "+
       "AND     PF.RES_COMUNE = C1.ISTAT_COMUNE(+) "+
       "AND     PF.NASCITA_COMUNE = C2.ISTAT_COMUNE(+) "+
       "AND     PF.DOM_COMUNE = C3.ISTAT_COMUNE(+) "+
       "AND     C1.ISTAT_PROVINCIA = P1.ISTAT_PROVINCIA(+) "+
       "AND     C2.ISTAT_PROVINCIA = P2.ISTAT_PROVINCIA(+) " +
       "AND     C3.ISTAT_PROVINCIA = P3.ISTAT_PROVINCIA(+) ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: "+query);

      stmt.setLong(1, idPersonaFisica.longValue());
      ResultSet rs = stmt.executeQuery();

      if (rs != null) 
      {
        while (rs.next()) 
        {
          pf = new PersonaFisicaVO();

          pf.setIdPersonaFisica(new Long(rs.getLong("ID_PERSONA_FISICA")));
          pf.setIdSoggetto(new Long(rs.getLong("ID_SOGGETTO")));
          pf.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
          pf.setCognome(rs.getString("COGNOME"));
          pf.setNascitaComune(rs.getString("NASCITA_COMUNE"));
          pf.setNome(rs.getString("NOME"));
          pf.setSesso(rs.getString("SESSO"));
          pf.setResComune(rs.getString("RES_COMUNE"));
          pf.setNascitaData(rs.getDate("NASCITA_DATA"));
          pf.setResIndirizzo(rs.getString("RES_INDIRIZZO"));
          pf.setResCAP(rs.getString("RES_CAP"));
          pf.setResTelefono(rs.getString("RES_TELEFONO"));
          pf.setResFax(rs.getString("RES_FAX"));
          pf.setResMail(rs.getString("RES_MAIL"));
          pf.setNumeroCellulare(rs.getString("NUMERO_CELLULARE"));
          pf.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
          pf.setNote(rs.getString("NOTE"));
          pf.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
          pf.setDomIndirizzo(rs.getString("DOM_INDIRIZZO"));
          pf.setIstatComuneDomicilio(rs.getString("DOM_COMUNE"));
          pf.setDescCittaEsteroDomicilio(rs.getString("DOM_CITTA_ESTERO"));
          pf.setDomCAP(rs.getString("DOM_CAP"));
          pf.setResCittaEstero(rs.getString("RES_CITTA_ESTERO"));
          pf.setNascitaCittaEstero(rs.getString("NASCITA_CITTA_ESTERO"));
          pf.setDescResComune(rs.getString("RES_DESCOM"));
          pf.setDescNascitaComune(rs.getString("NASC_DESCOM"));
          if((SolmrConstants.FLAG_N).equalsIgnoreCase(rs.getString("DOM_FLAG_ESTERO"))) 
          {
            pf.setDomComune(rs.getString("DOM_DESCOM"));
            pf.setDomProvincia(rs.getString("DOM_SIGLA_PROVINCIA"));
          }
          else 
          {
            pf.setDomicilioStatoEstero(rs.getString("DOM_DESCOM"));
            pf.setDomProvincia(null);
          }
          pf.setDataInizioResidenza(rs.getDate("DATA_INIZIO_RESIDENZA"));
          pf.setResProvincia(rs.getString("RES_SIGLA_PROVINCIA"));
        }
      } else
        throw new DataAccessException();

      if (pf == null)
        throw new NotFoundException(SolmrErrors.EXC_NOT_FOUND_PK);

      SolmrLogger.debug(this, "Executed query - Found record with primary key: "+idPersonaFisica);
    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "findPersonaFisica - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (DataAccessException daexc) {
      SolmrLogger.fatal(this, "findPersonaFisica - ResultSet null");
      throw daexc;
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "findPersonaFisica - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    } finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "findPersonaFisica - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "findPersonaFisica - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return pf;
  }
  /* Restituisce le informazioni sui soggetti da utilizzare nelle attestazioni di proprietà*/
  public PersonaFisicaVO getDatiSoggettoPerMacchina(Long idPersonaFisica)throws DataAccessException, NotFoundException{
    PersonaFisicaVO pfVO = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = getDatasource().getConnection();
      String query = "SELECT "+
                     "pf.codice_fiscale, "+
                     "pf.cognome, "+
                     "pf.nome, "+
                     "c.flag_estero, "+
                     "c.descom, "+
                     "pf.res_cap, "+
                     "p.sigla_provincia, "+
                     "pf.res_citta_estero, "+
                     "pf.res_indirizzo "+
                     "FROM  "+
                     "db_persona_fisica pf, "+
                     "comune c, "+
                     "provincia p "+
                     "WHERE  "+
                     "c.istat_comune(+) = pf.res_comune and "+
                     "c.istat_provincia = p.istat_provincia(+) and "+
                     "pf.id_persona_fisica =?";
      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query: "+query);
      stmt.setLong(1, idPersonaFisica.longValue());
      ResultSet rs = stmt.executeQuery();
      if (rs != null) {
        while (rs.next()) {
          pfVO = new PersonaFisicaVO();
          pfVO.setCodiceFiscale(rs.getString(1));
          pfVO.setCognome(rs.getString(2));
          pfVO.setNome(rs.getString(3));
          if(rs.getString(4).equals(SolmrConstants.FLAG_N)){
            pfVO.setResComune(rs.getString(5));
            pfVO.setResCAP(rs.getString(6));
            pfVO.setResProvincia(rs.getString(7));
          }
          else{
            pfVO.setStatoEsteroRes(rs.getString(5));
            pfVO.setResCittaEstero(rs.getString(8));
          }
          pfVO.setResIndirizzo(rs.getString(9));
        }
      } else
        throw new DataAccessException();

      if (pfVO == null)
        throw new NotFoundException(SolmrErrors.EXC_NOT_FOUND_PK);

      SolmrLogger.debug(this, "Executed query - Found record with primary key: "+idPersonaFisica);
    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "getDatiSoggettoPerMacchina - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (DataAccessException daexc) {
      SolmrLogger.fatal(this, "getDatiSoggettoPerMacchina - ResultSet null");
      throw daexc;
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "getDatiSoggettoPerMacchina - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    } finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "getDatiSoggettoPerMacchina - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "getDatiSoggettoPerMacchina - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return pfVO;
  }

  public Vector<PersonaFisicaVO> findPersonaFisicaByIdSoggettoAndIdAzienda(Long idSoggetto, Long idAzienda) throws DataAccessException, SolmrException {

    PersonaFisicaVO pf = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<PersonaFisicaVO> result = new Vector<PersonaFisicaVO>();

    try {
      conn = getDatasource().getConnection();

      String query = "SELECT distinct id_persona_fisica, "+
                     "db_persona_fisica.id_soggetto, "+
                     "codice_fiscale, "+
                     "cognome, "+
                     "nascita_comune, "+
                     "nome, "+
                     "sesso, "+
                     "res_comune , "+
                     "nascita_data, "+
                     "res_indirizzo, "+
                     "res_cap, "+
                     "res_telefono, "+
                     "res_fax, "+
                     "res_mail, "+
                     "data_aggiornamento, "+
                     "note, "+
                     "id_utente_aggiornamento, "+
                     "dom_indirizzo, "+
                     "dom_comune, "+
                     " DOM_CITTA_ESTERO, "+
                     "dom_cap, "+
                     "res_citta_estero, "+
                     "nascita_citta_estero, "+
                     "C1.DESCOM, C2.DESCOM, tr.DESCRIZIONE, "+
                     "db_contitolare.DATA_INIZIO_RUOLO, db_contitolare.DATA_FINE_RUOLO, "+
                     " C3.FLAG_ESTERO, C3.DESCOM, P3.SIGLA_PROVINCIA " +
                     "FROM DB_PERSONA_FISICA, db_contitolare, db_tipo_ruolo tr, "+
                     "COMUNE C1, COMUNE C2, COMUNE C3, PROVINCIA P1, PROVINCIA P2, PROVINCIA P3 "+
                     "WHERE db_contitolare.id_soggetto = ? "+
                     " and db_contitolare.ID_AZIENDA = ? "+
                     " and db_contitolare.id_soggetto = DB_PERSONA_FISICA.id_soggetto "+
                     " and DB_PERSONA_FISICA.RES_COMUNE=C1.ISTAT_COMUNE(+) "+
                     " and DB_PERSONA_FISICA.NASCITA_COMUNE=C2.ISTAT_COMUNE(+) "+
                     " AND DB_PERSONA_FISICA.DOM_COMUNE = C3.ISTAT_COMUNE(+) "+
                     " and C1.ISTAT_PROVINCIA=P1.ISTAT_PROVINCIA(+) "+
                     " and C2.ISTAT_PROVINCIA=P2.ISTAT_PROVINCIA(+) "+
                     " and C3.ISTAT_PROVINCIA = P3.ISTAT_PROVINCIA(+) "+
                     " and db_contitolare.ID_SOGGETTO = DB_PERSONA_FISICA.ID_SOGGETTO "+
                     " and db_contitolare.ID_RUOLO = tr.ID_RUOLO ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: "+query);

      stmt.setLong(1, idSoggetto.longValue());
      stmt.setLong(2, idAzienda.longValue());
      ResultSet rs = stmt.executeQuery();

      if (rs != null) {
        while (rs.next()) {
          pf = new PersonaFisicaVO();

          pf.setIdPersonaFisica(new Long(rs.getLong("ID_PERSONA_FISICA")));
          pf.setIdSoggetto(new Long(rs.getLong("ID_SOGGETTO")));
          pf.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
          pf.setCognome(rs.getString("COGNOME"));
          pf.setNascitaComune(rs.getString("NASCITA_COMUNE"));
          pf.setNome(rs.getString("NOME"));
          pf.setSesso(rs.getString("SESSO"));
          pf.setResComune(rs.getString("RES_COMUNE"));
          pf.setNascitaData(rs.getDate("NASCITA_DATA"));
          pf.setResIndirizzo(rs.getString("RES_INDIRIZZO"));
          pf.setResCAP(rs.getString("RES_CAP"));
          pf.setResTelefono(rs.getString("RES_TELEFONO"));
          pf.setResFax(rs.getString("RES_FAX"));
          pf.setResMail(rs.getString("RES_MAIL"));
          pf.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
          pf.setNote(rs.getString("NOTE"));
          pf.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
          pf.setDomIndirizzo(rs.getString("DOM_INDIRIZZO"));
          pf.setIstatComuneDomicilio(rs.getString("DOM_COMUNE"));
          pf.setDescCittaEsteroDomicilio(rs.getString("DOM_CITTA_ESTERO"));
          pf.setDomCAP(rs.getString("DOM_CAP"));
          pf.setResCittaEstero(rs.getString("RES_CITTA_ESTERO"));
          pf.setNascitaCittaEstero(rs.getString("NASCITA_CITTA_ESTERO"));
          pf.setDescResComune(rs.getString(24));
          pf.setDescNascitaComune(rs.getString(25));
          pf.setRuolo(rs.getString(26));//descrizione ruolo
          pf.setDataInizioRuolo(rs.getDate(27));//data inizio ruolo
          pf.setDataFineRuolo(rs.getDate(28));//data fine ruolo
          if((SolmrConstants.FLAG_N).equalsIgnoreCase(rs.getString(29))) {
            pf.setDomComune(rs.getString(30));
            pf.setDomProvincia(rs.getString(31));
          }
          else {
            pf.setDomicilioStatoEstero(rs.getString(30));
            pf.setDomProvincia(null);
          }
          pf.setIdAzienda(idAzienda);
          result.add(pf);
        }
        if (result.size()==0)
          throw new SolmrException(""+SolmrErrors.get("EXC_NOT_FOUND_PK"));
      } else
        throw new DataAccessException();

    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "findPersonaFisicaByIdSoggettoAndIdAzienda - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (DataAccessException daexc) {
      SolmrLogger.fatal(this, "findPersonaFisicaByIdSoggettoAndIdAzienda - ResultSet null");
      throw daexc;
    } catch (SolmrException daexc) {
      SolmrLogger.fatal(this, "findPersonaFisicaByIdSoggettoAndIdAzienda - SolmrException");
      throw daexc;
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "findPersonaFisicaByIdSoggettoAndIdAzienda - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    } finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "findPersonaFisicaByIdSoggettoAndIdAzienda - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "findPersonaFisicaByIdSoggettoAndIdAzienda - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // Metodo per recuperare gli elementi del dettaglio del soggetto collegato a partire dall'id_contitolare
  public PersonaFisicaVO getDettaglioSoggettoByIdContitolare(Long idContitolare) throws DataAccessException, SolmrException {

    PersonaFisicaVO pfVO = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try {
      conn = getDatasource().getConnection();
      String query = "SELECT C.ID_SOGGETTO, " +
                     "       PF.ID_PERSONA_FISICA, " +
                     "       C.ID_RUOLO, " +
                     "       TR.DESCRIZIONE, " +
                     "       C.DATA_INIZIO_RUOLO, " +
                     "       C.DATA_FINE_RUOLO, " +
                     "       PF.COGNOME, " +
                     "       PF.NOME, " +
                     "       PF.CODICE_FISCALE, " +
                     "       PF.SESSO, " +
                     "       PF.NASCITA_DATA, " +
                     "       CN.ISTAT_COMUNE, " +
                     "       CN.FLAG_ESTERO ALIAS_F_E, " +
                     "       CN.DESCOM comune_nascita, " +
                     "       PF.NASCITA_CITTA_ESTERO, " +
                     "       PN.SIGLA_PROVINCIA SIGLA_PROV, " +
                     "       PF.RES_INDIRIZZO, " +
                     "       CR.FLAG_ESTERO ALIAS_F_E2, " +
                     "       PF.RES_COMUNE, " +
                     "       PF.RES_CITTA_ESTERO, " +
                     "       CR.DESCOM COMUNE_RESIDENZA, " +
                     "       PF.RES_CAP, " +
                     "       PR.SIGLA_PROVINCIA PROV_RES, " +
                     "       PF.RES_TELEFONO, " +
                     "       PF.RES_FAX, " +
                     "       PF.RES_MAIL, " +
                     "       PF.NOTE, " +
                     "       PF.DATA_AGGIORNAMENTO, " +
                     "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
                     "          || ' ' " + 
                     "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
                     "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
                     "         WHERE PF.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
                     "       AS DENOMINAZIONE, " +
                     "       PF.DOM_INDIRIZZO, " +
                     "       PF.DOM_COMUNE, " +
                     "       PF.DOM_CITTA_ESTERO, " +
                     "       PF.DOM_CAP, " +
                     "       TTS.ID_TITOLO_STUDIO, " +
                     "       TTS.DESCRIZIONE, " +
                     "       TIS.ID_INDIRIZZO_STUDIO, " +
                     "       TIS.DESCRIZIONE, " +
                     "       CD.FLAG_ESTERO, " +
                     "       CD.DESCOM, " +
                     "       PD.SIGLA_PROVINCIA, " +
                     "       TRUNC(PF.DATA_INIZIO_RESIDENZA) AS DATA_INIZIO_RESIDENZA, " +
                     "       PF.FLAG_CF_OK, " +
                     "       PF.NUMERO_CELLULARE, " +
                     "       C.DATA_INIZIO_RUOLO_MOD, " +
                     "       C.DATA_FINE_RUOLO_MOD " +
                     " FROM  DB_CONTITOLARE C, " +
                     "       DB_PERSONA_FISICA PF, " +
                     //"       PAPUA_V_UTENTE_LOGIN PVU, " +
                     "       COMUNE CN, " +
                     "       COMUNE CR, " +
                     "       PROVINCIA PN, " +
                     "       PROVINCIA PR, " +
                     "       DB_TIPO_RUOLO TR, " +
                     "       COMUNE CD, " +
                     "       PROVINCIA PD, " +
                     "       DB_TIPO_TITOLO_STUDIO TTS, " +
                     "       DB_TIPO_INDIRIZZO_STUDIO TIS, " +
                     "		 DB_TIPO_PREFISSO_CELLULARE TPC " +
                     " WHERE C.ID_CONTITOLARE = ? " +
                     " AND   PF.ID_SOGGETTO = C.ID_SOGGETTO " +
                     " AND   PF.NASCITA_COMUNE = CN.ISTAT_COMUNE(+) " +
                     " AND   PF.RES_COMUNE = CR.ISTAT_COMUNE(+) " +
                     " AND   PF.ID_INDIRIZZO_STUDIO = TIS.ID_INDIRIZZO_STUDIO(+) " +
                     " AND   PF.ID_TITOLO_STUDIO = TTS.ID_TITOLO_STUDIO(+) " +
                     " AND   PF.DOM_COMUNE = CD.ISTAT_COMUNE(+) " +
                     //" AND   PVU.ID_UTENTE_LOGIN = PF.ID_UTENTE_AGGIORNAMENTO " +
                     " AND   CN.ISTAT_PROVINCIA = PN.ISTAT_PROVINCIA(+) " +
                     " AND   CR.ISTAT_PROVINCIA = PR.ISTAT_PROVINCIA(+) " +
                     " AND   CD.ISTAT_PROVINCIA = PD.ISTAT_PROVINCIA(+) " +
                     " AND   TR.ID_RUOLO = C.ID_RUOLO " +
                     " AND 	PF.ID_PREFISSO_CELLULARE = TPC.ID_PREFISSO_CELLULARE(+)";

      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query getDettaglioSoggettoByIdContitolare in SoggettiDAO: "+query);

      stmt.setLong(1, idContitolare.longValue());

      ResultSet rs = stmt.executeQuery();

      if(rs!=null && rs.next()){
        pfVO = new PersonaFisicaVO();
        pfVO.setIdSoggetto(new Long(rs.getLong(1)));
        pfVO.setIdPersonaFisica(new Long(rs.getLong(2)));
        pfVO.setIdRuolo(new Long(rs.getLong(3))); // ID_RUOLO su DB_CONTITOLARE
        pfVO.setTipiRuoloNonTitolare(new Long(rs.getLong(3)));
        pfVO.setTipiRuoloNonTitolareAndNonSpecificato(new Long(rs.getLong(3)));// Valore settato solo per l'applicativo non serve per i servizi
        pfVO.setRuolo(rs.getString(4));
        pfVO.setDataInizioRuolo(rs.getDate(5));
        if(rs.getDate(6)!= null) {
          pfVO.setDataFineRuolo(rs.getDate(6));
        }
        pfVO.setCognome(rs.getString(7));
        pfVO.setNome(rs.getString(8));
        pfVO.setCodiceFiscale(rs.getString(9));
        pfVO.setSesso(rs.getString(10));
        pfVO.setNascitaData(rs.getDate(11));
        // La data di nascita può non essere valorizzata a causa di records errati inseriti
        // attraverso porting
        if(Validator.isNotEmpty(rs.getString(11))) {
          pfVO.setStrNascitaData(DateUtils.formatDate(rs.getDate(11)));
        }
        pfVO.setNascitaComune(rs.getString(12));
        if(rs.getString(13)!=null&&rs.getString(13).equals(SolmrConstants.FLAG_N)){
          pfVO.setDescNascitaComune(rs.getString(14));
          pfVO.setNascitaProv(rs.getString(16));
        }
        else{
          pfVO.setNascitaStatoEstero(rs.getString(14));
          pfVO.setNascitaCittaEstero(rs.getString(15));
          pfVO.setCittaNascita(rs.getString(15));
        }
        pfVO.setResIndirizzo(rs.getString(17));
        pfVO.setResComune(rs.getString(19));
        if(rs.getString(18)!=null&&rs.getString(18).equals(SolmrConstants.FLAG_N)){
          pfVO.setDescResComune(rs.getString(21));
          pfVO.setResCAP(rs.getString(22));
          pfVO.setDescResProvincia(rs.getString(23));
          pfVO.setResProvincia(rs.getString(23));
        }
        else{
          pfVO.setResCittaEstero(rs.getString(20));
          pfVO.setDescStatoEsteroResidenza(rs.getString(21));
          pfVO.setResProvincia(null);
        }
        pfVO.setResTelefono(rs.getString(24));
        pfVO.setResFax(rs.getString(25));
        pfVO.setResMail(rs.getString(26));
        pfVO.setNote(rs.getString(27));
        pfVO.setDataAggiornamento(rs.getDate(28));
        pfVO.setDescUtenteAggiornamento(rs.getString(29));
        pfVO.setDomIndirizzo(rs.getString(30));
        pfVO.setIstatComuneDomicilio(rs.getString(31));
        pfVO.setDescCittaEsteroDomicilio(rs.getString(32));
        pfVO.setDomCAP(rs.getString(33));
        if(rs.getString(34) != null) {
          pfVO.setIdTitoloStudio(new Long(rs.getLong(34)));
          pfVO.setDescrizioneTitoloStudio(rs.getString(35));
        }
        if(rs.getString(36) != null) {
          pfVO.setIdIndirizzoStudio(new Long(rs.getLong(36)));
          pfVO.setDescrizioneIndirizzoStudio(rs.getString(37));
        }
        if((SolmrConstants.FLAG_N).equalsIgnoreCase(rs.getString(38))) {
          pfVO.setDomComune(rs.getString(39));
          pfVO.setDomProvincia(rs.getString(40));
        }
        else {
          pfVO.setDomicilioStatoEstero(rs.getString(39));
          pfVO.setDomProvincia(null);
        }
        pfVO.setDataInizioResidenza(rs.getDate("DATA_INIZIO_RESIDENZA"));
        pfVO.setFlagCfOk("FLAG_CF_OK");
        //Settaggio del cellulare
        pfVO.setdesNumeroCellulare(rs.getString("NUMERO_CELLULARE"));
        
        if(rs.getDate("DATA_INIZIO_RUOLO_MOD")!= null)
          pfVO.setDataInizioRuoloMod(rs.getDate("DATA_INIZIO_RUOLO_MOD"));
        
        if(rs.getDate("DATA_FINE_RUOLO_MOD")!= null)
          pfVO.setDataFineRuoloMod(rs.getDate("DATA_FINE_RUOLO_MOD"));
      }
      else {
        throw new SolmrException((String)AnagErrors.get("ERR_NESSUN_SOGGETTO_TROVATO"));
      }
      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "getDettaglioSoggettoByIdContitolare in SoggettiDAO - Found record with primary key: "+idContitolare);
    }
    catch (SolmrException se) {
      SolmrLogger.fatal(this, "getDettaglioSoggettoByIdContitolare in SoggettiDAO - ResultSet null");
      throw new SolmrException(se.getMessage());
    }
    catch(SQLException exc) {
      SolmrLogger.fatal(this, "getDettaglioSoggettoByIdContitolare in SoggettiDAO - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this, "getDettaglioSoggettoByIdContitolare in SoggettiDAO - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this, "getDettaglioSoggettoByIdContitolare in SoggettiDAO - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this, "getDettaglioSoggettoByIdContitolare in SoggettiDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return pfVO;
  }

  public PersonaFisicaVO serviceGetSoggetto(Long idSoggetto) throws
    DataAccessException
  {
    PersonaFisicaVO pf = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try {
      conn = getDatasource().getConnection();

      String query = "SELECT "+
                     "  id_persona_fisica, "+
                     "  db_persona_fisica.id_soggetto, "+
                     "  codice_fiscale, "+
                     "  cognome, "+
                     "  nascita_comune, "+
                     "  nome, "+
                     "  sesso, "+
                     "  res_comune , "+
                     "  nascita_data, "+
                     "  res_indirizzo, "+
                     "  res_cap, "+
                     "  res_telefono, "+
                     "  res_fax, "+
                     "  res_mail, "+
                     "  data_aggiornamento, "+
                     "  note, "+
                     "  id_utente_aggiornamento, "+
                     "  dom_indirizzo, "+
                     "  dom_comune, "+
                     "  DOM_CITTA_ESTERO, "+
                     "  dom_cap, "+
                     "  res_citta_estero, "+
                     "  nascita_citta_estero, "+

                     " C3.ISTAT_COMUNE AS ISTAT_DOMICILIO, "+

                     " C1.DESCOM AS DESCOM_RESIDENZA, "+
                     " C2.DESCOM AS DESCOM_NASCITA, "+
                     " C3.DESCOM AS DESCOM_DOMICILIO,"+
                     " C1.FLAG_ESTERO AS FLAG_ESTERO_RESIDENZA,"+
                     " C2.FLAG_ESTERO AS FLAG_ESTERO_NASCITA,"+
                     " C3.FLAG_ESTERO AS FLAG_ESTERO_DOMICILIO, "+

                     "  TTS.ID_TITOLO_STUDIO, "+
                     "  TTS.DESCRIZIONE AS DESCRIZIONE_TITOLO_STUDIO, "+
                     "  TIS.ID_INDIRIZZO_STUDIO, "+
                     "  TIS.DESCRIZIONE AS DESCRIZIONE_INDIRIZZO_STUDIO, "+
                     "  P3.SIGLA_PROVINCIA AS DOM_PROVINCIA, "+
                     "  P1.SIGLA_PROVINCIA AS RES_PROVINCIA, "+
                     "  P2.SIGLA_PROVINCIA AS NASCITA_PROVINCIA, "+
                     "  TRUNC(DATA_INIZIO_RESIDENZA) AS DATA_INIZIO_RESIDENZA "+
                     "FROM "+
                     "  DB_PERSONA_FISICA, "+
                     "  COMUNE C1, "+
                     "  COMUNE C2, "+
                     "  COMUNE C3, "+
                     "  PROVINCIA P1, "+
                     "  PROVINCIA P2, "+
                     "  PROVINCIA P3, "+
                     "  DB_TIPO_TITOLO_STUDIO TTS, "+
                     "  DB_TIPO_INDIRIZZO_STUDIO TIS "+
                     "WHERE "+
                     "  id_soggetto = ? and "+
                     "  DB_PERSONA_FISICA.RES_COMUNE=C1.ISTAT_COMUNE(+) and "+
                     "  DB_PERSONA_FISICA.NASCITA_COMUNE=C2.ISTAT_COMUNE(+) AND "+
                     "  DB_PERSONA_FISICA.DOM_COMUNE = C3.ISTAT_COMUNE(+) and "+
                     "  C1.ISTAT_PROVINCIA=P1.ISTAT_PROVINCIA(+) and "+
                     "  C2.ISTAT_PROVINCIA=P2.ISTAT_PROVINCIA(+) AND "+
                     "  C3.ISTAT_PROVINCIA = P3.ISTAT_PROVINCIA(+) AND "+
                     "  DB_PERSONA_FISICA.ID_TITOLO_STUDIO = TTS.ID_TITOLO_STUDIO(+) AND "+
                     "  DB_PERSONA_FISICA.ID_INDIRIZZO_STUDIO = TIS.ID_INDIRIZZO_STUDIO(+) ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: "+query);

      stmt.setLong(1, idSoggetto.longValue());
      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        pf = new PersonaFisicaVO();
        pf.setIdPersonaFisica(new Long(rs.getLong("ID_PERSONA_FISICA")));
        pf.setIdSoggetto(new Long(rs.getLong("ID_SOGGETTO")));
        pf.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
        pf.setCognome(rs.getString("COGNOME"));
        pf.setNascitaComune(rs.getString("NASCITA_COMUNE"));
        pf.setNome(rs.getString("NOME"));
        pf.setSesso(rs.getString("SESSO"));
        pf.setResComune(rs.getString("RES_COMUNE"));
        pf.setNascitaData(rs.getDate("NASCITA_DATA"));
        pf.setResIndirizzo(rs.getString("RES_INDIRIZZO"));
        pf.setResCAP(rs.getString("RES_CAP"));
        pf.setResTelefono(rs.getString("RES_TELEFONO"));
        pf.setResFax(rs.getString("RES_FAX"));
        pf.setResMail(rs.getString("RES_MAIL"));
        pf.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
        pf.setNote(rs.getString("NOTE"));
        pf.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        pf.setDomIndirizzo(rs.getString("DOM_INDIRIZZO"));
        pf.setIstatComuneDomicilio(rs.getString("ISTAT_DOMICILIO"));
        pf.setDescCittaEsteroDomicilio(rs.getString("DOM_CITTA_ESTERO"));
        pf.setDomCAP(rs.getString("DOM_CAP"));
        pf.setResCittaEstero(rs.getString("RES_CITTA_ESTERO"));
        pf.setNascitaCittaEstero(rs.getString("NASCITA_CITTA_ESTERO"));
        String idTitoloStudio=rs.getString("ID_TITOLO_STUDIO");
        if(idTitoloStudio != null)
        {
          pf.setIdTitoloStudio(new Long(idTitoloStudio));
          pf.setDescrizioneTitoloStudio(rs.getString("DESCRIZIONE_TITOLO_STUDIO"));
        }
        String idIndirizzoTitoloStudio=rs.getString("ID_INDIRIZZO_STUDIO");
        if(idIndirizzoTitoloStudio != null)
        {
          pf.setIdIndirizzoStudio(new Long(idIndirizzoTitoloStudio));
          pf.setDescrizioneIndirizzoStudio(rs.getString("DESCRIZIONE_INDIRIZZO_STUDIO"));
        }

        String flagEsteroDomNO=rs.getString("FLAG_ESTERO_DOMICILIO");
        if((SolmrConstants.FLAG_N).equalsIgnoreCase(flagEsteroDomNO))
        {
          pf.setDomComune(rs.getString("DESCOM_DOMICILIO"));
          pf.setDomProvincia(rs.getString("DOM_PROVINCIA"));
        }
        else {
          pf.setDomicilioStatoEstero(rs.getString("DESCOM_DOMICILIO"));
          pf.setDomProvincia(null);
        }

        String flagEsteroResNO=rs.getString("FLAG_ESTERO_RESIDENZA");
        if((SolmrConstants.FLAG_N).equalsIgnoreCase(flagEsteroResNO))
        {
          pf.setDescResComune(rs.getString("DESCOM_RESIDENZA"));
          pf.setResProvincia(rs.getString("RES_PROVINCIA"));
        }
        else {
          pf.setDescStatoEsteroResidenza(rs.getString("DESCOM_RESIDENZA"));
          pf.setResProvincia(null);
        }

        String flagEsteroNasNO=rs.getString("FLAG_ESTERO_NASCITA");
        if((SolmrConstants.FLAG_N).equalsIgnoreCase(flagEsteroNasNO))
        {
          pf.setDescNascitaComune(rs.getString("DESCOM_NASCITA"));
          pf.setNascitaProv(rs.getString("NASCITA_PROVINCIA"));
        }
        else {
          pf.setNascitaStatoEstero(rs.getString("DESCOM_NASCITA"));
          pf.setNascitaProv(null);
        }
        pf.setDataInizioResidenza(rs.getDate("DATA_INIZIO_RESIDENZA"));
        pf.setNewPersonaFisica(false);
      }
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SoggettiDAO.serviceGetSoggetto - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "SoggettiDAO.serviceGetSoggetto - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    } finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "SoggettiDAO.serviceGetSoggetto - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "SoggettiDAO.serviceGetSoggetto - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return pf;
  }


  public void updateDataInizRuoloDbContitolare(Long idRuolo, Long idSoggetto,java.util.Date dataInizioRuolo)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = getDatasource().getConnection();

      String update = "UPDATE DB_CONTITOLARE "+
                      "SET DATA_INIZIO_RUOLO = ? "+
                      "WHERE ID_SOGGETTO= ? "+
                      "AND ID_RUOLO = ? ";



      stmt = conn.prepareStatement(update);

      SolmrLogger.debug(this, "Executing UPDATE: "+update);

      stmt.setDate(1, new java.sql.Date(dataInizioRuolo.getTime()));
      stmt.setLong(2, idSoggetto.longValue());
      stmt.setLong(3, idRuolo.longValue());

      stmt.executeUpdate();

      SolmrLogger.debug(this, "Executed UPDATE.");
    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "updateDataInizRuoloDbContitolare - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "updateDataInizRuoloDbContitolare - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    } finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "updateDataInizRuoloDbContitolare - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "updateDataInizRuoloDbContitolare - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }


  /**
   * Restituisce true se la storicizzazione può essere fatta (non sono presenti
   * altre storicizzazione per la stessa persona all'interno della stessa
   * giornata)
   */
  public boolean controllaStoricizzazioneResidenza(long idPersonaFisica)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    boolean result=true;

    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT * FROM DB_STORICO_RESIDENZA "+
                     "WHERE TRUNC(DATA_FINE_RESIDENZA) = TRUNC(SYSDATE) "+
                     "AND ID_PERSONA_FISICA=? ";

      SolmrLogger.debug(this,"controllaStoricizzazioneResidenza Query "+query);

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idPersonaFisica);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        result=false;
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SoggettiDAO.controllaStoricizzazioneResidenza - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "SoggettiDAO.controllaStoricizzazioneResidenza - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    } finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "SoggettiDAO.controllaStoricizzazioneResidenza - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "SoggettiDAO.controllaStoricizzazioneResidenza - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  
  
  public Long getIdSoggettoFromPersonaFisica(String codiceFiscale)
    throws DataAccessException, SolmrException 
  {
    Long idSoggetto = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try 
    {
      conn = getDatasource().getConnection();

      String query = 
        "SELECT PF.ID_SOGGETTO "+
        "FROM    DB_PERSONA_FISICA PF "+
        "WHERE   PF.CODICE_FISCALE = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: "+query);

      stmt.setString(1, codiceFiscale);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) 
      {
        idSoggetto = new Long(rs.getLong("ID_SOGGETTO"));
      }
    } 
    catch (SQLException exc) 
    {
      SolmrLogger.fatal(this, "getIdSoggettoFromPersonaFisica - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) 
    {
      SolmrLogger.fatal(this, "getIdSoggettoFromPersonaFisica - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    } 
    finally 
    {
      try 
      {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) 
      {
        SolmrLogger.fatal(this, "getIdSoggettoFromPersonaFisica - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) 
      {
        SolmrLogger.fatal(this, "getIdSoggettoFromPersonaFisica - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return idSoggetto;
  }
  
  
  public Long getIdSoggettoFromPersonaGiuridica(String cuaa)
    throws DataAccessException, SolmrException 
  {
    Long idSoggetto = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try 
    {
      conn = getDatasource().getConnection();

      String query = 
        "SELECT PG.ID_SOGGETTO "+
        "FROM    DB_PERSONA_GIURIDICA PG "+
        "WHERE   PG.CODICE_FISCALE = ? " +
        "OR      PG.PARTITA_IVA = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: "+query);

      stmt.setString(1, cuaa);
      stmt.setString(2, cuaa);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) 
      {
        idSoggetto = new Long(rs.getLong("ID_SOGGETTO"));
      }
    } 
    catch (SQLException exc) 
    {
      SolmrLogger.fatal(this, "getIdSoggettoFromPersonaGiuridica - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) 
    {
      SolmrLogger.fatal(this, "getIdSoggettoFromPersonaGiuridica - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    } 
    finally 
    {
      try 
      {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) 
      {
        SolmrLogger.fatal(this, "getIdSoggettoFromPersonaGiuridica - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) 
      {
        SolmrLogger.fatal(this, "getIdSoggettoFromPersonaGiuridica - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return idSoggetto;
  }
  
  
  public  TesserinoFitoSanitarioVO getTesserinoFitoSanitario(String codiceFiscale) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    TesserinoFitoSanitarioVO tesserinoVO = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[SoggettiDAO::getTesserinoFitoSanitario] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
      "SELECT VP.FLAG_TESSERINO_SCADUTO, " +
      "       VP.DESC_TIPO_CERTIFICATO, " +
      "       VP.DATA_SCADENZA, " +
      "       VP.DATA_RILASCIO " + 
      "FROM   PATE_V_PERSONA_ULTIMO_CERTIF VP " +
      "WHERE  VP.CODICE_FISCALE = ? ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[SoggettiDAO::getTesserinoFitoSanitario] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setString(++indice, codiceFiscale);     
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {
        tesserinoVO = new TesserinoFitoSanitarioVO();
        
        tesserinoVO.setDescTipoCertificato(rs.getString("DESC_TIPO_CERTIFICATO"));
        tesserinoVO.setFlagTesserinoScaduto(rs.getString("FLAG_TESSERINO_SCADUTO"));
        tesserinoVO.setDataRilascio(rs.getTimestamp("DATA_RILASCIO"));
        tesserinoVO.setDataScadenza(rs.getTimestamp("DATA_SCADENZA"));
        
       
      }
      
      return tesserinoVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("tesserinoVO", tesserinoVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("codiceFiscale", codiceFiscale)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[SoggettiDAO::getTesserinoFitoSanitario] ",
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
              "[SoggettiDAO::getTesserinoFitoSanitario] END.");
    }
  }
  
  
  
  

}
