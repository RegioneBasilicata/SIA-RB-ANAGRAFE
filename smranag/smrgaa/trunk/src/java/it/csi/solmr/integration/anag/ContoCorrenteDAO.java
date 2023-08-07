package it.csi.solmr.integration.anag;

import it.csi.solmr.dto.anag.BancaSportelloVO;
import it.csi.solmr.dto.anag.ContoCorrenteVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.DataControlException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.exception.services.ErrorTypes;
import it.csi.solmr.integration.BaseDAO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class ContoCorrenteDAO extends BaseDAO
{

  public ContoCorrenteDAO() throws ResourceAccessException
  {
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public ContoCorrenteDAO(String refName) throws ResourceAccessException
  {
    super(refName);
  }

  // Restituisce il conto corrente richiesto
  public ContoCorrenteVO getContoCorrente(Long idContoCorrente)
      throws DataAccessException
  {
    ContoCorrenteVO result=new ContoCorrenteVO();
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query =
          "SELECT "+
          "       CC.ID_CONTO_CORRENTE, "+
          "       CC.ID_SPORTELLO, "+
          "       CC.ID_AZIENDA, "+
          "       CC.NUMERO_CONTO_CORRENTE, "+
          "	      CC.CIFRA_CONTROLLO,	" +
          "       CC.CIN, "+
          "       CC.BBAN, "+
          "       CC.IBAN, " +
          "       CC.INTESTAZIONE, "+
          "       CC.DATA_INIZIO_VALIDITA, "+
          "       CC.DATA_FINE_VALIDITA, "+
          "       CC.DATA_ESTINZIONE, "+
          "       CC.DATA_AGGIORNAMENTO, "+
          "       CC.ID_UTENTE_AGGIORNAMENTO, "+
          "       CC.FLAG_VALIDATO," +
          "       CC.MOTIVO_RIVALIDAZIONE," +
          "       CC.NOTE," +
          "       CC.FLAG_CONTO_GF, " +
          "       CC.FLAG_CONTO_VINCOLATO, "+
          "       TB.ABI, "+
          "       TB.DENOMINAZIONE AS DENOMINAZIONE_BANCA, "+
          "       TB.DATA_INIZIO_VALIDITA AS DATA_INIZIO_VALIDITA_BANCA, "+
          "       TB.DATA_FINE_VALIDITA AS DATA_FINE_VALIDITA_BANCA, "+
          "       TS.ID_SPORTELLO, "+
          "       TS.ID_BANCA, "+
          "       TS.CODICE_PAESE, " +
          "       TS.CAB, "+
          "       TS.DENOMINAZIONE AS DENOMINAZIONE_SPORTELLO, "+
          "       TS.INDIRIZZO AS INDIRIZZO_SPORTELLO, "+
          "       TS.CAP AS CAP_SPORTELLO, "+
          "       TS.COMUNE AS COMUNE_SPORTELLO, "+
          "       TS.DATA_INIZIO_VALIDITA AS DATA_INZIO_VALIDITA_SPORTELLO, "+
          "       TS.DATA_FINE_VALIDITA AS DATA_FINE_VALIDITA_SPORTELLO, "+
          "       TS.FLAG_SPORTELLO_GF, " +
          "       C.DESCOM || ' (' || P.SIGLA_PROVINCIA || ')' AS DESC_COMUNE_SPORTELLO, "+
          "       P.DESCRIZIONE AS DESC_PROVINCIA_SPORTELLO, "+
          "       P.SIGLA_PROVINCIA, "+
          "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
          "          || ' ' " + 
          "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
          "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
          "         WHERE CC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
          "       AS DENOMINAZIONE_UTENTE, "+
          "       (SELECT PVU.DENOMINAZIONE " +
          "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
          "        WHERE CC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
          "       AS DESCRIZIONE_ENTE_APPARTENENZA, "+
          "       TB.BIC, "+
          "       CC.ID_TIPO_CAUSA_INVALIDAZIONE_CC, " +
          "       TCIC.DESCRIZIONE AS DESCRIZIONE_INVALID " +
          "FROM "+
          "       DB_CONTO_CORRENTE CC, "+
          "       DB_TIPO_SPORTELLO TS, "+
          "       DB_TIPO_BANCA TB, "+
          "       DB_TIPO_CAUSA_INVALIDAZIONE_CC TCIC, " +
          "       COMUNE C, "+
          "       PROVINCIA P "+
          //"       PAPUA_V_UTENTE_LOGIN PVU "+
          "WHERE "+
          "       CC.ID_SPORTELLO = TS.ID_SPORTELLO "+
          "AND    TB.ID_BANCA = TS.ID_BANCA "+
          "AND    CC.ID_TIPO_CAUSA_INVALIDAZIONE_CC = TCIC.ID_TIPO_CAUSA_INVALIDAZIONE_CC (+) "+
          "AND    TS.COMUNE = C.ISTAT_COMUNE(+) "+
          "AND    P.ISTAT_PROVINCIA(+) = C.ISTAT_PROVINCIA "+
          //"AND    CC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN "+
          "AND    CC.ID_CONTO_CORRENTE = ? ";

      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query: "+query);

      stmt.setLong(1, idContoCorrente.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        // Contocorrente
        result.setIdContoCorrente(new Long(rs.getLong("ID_CONTO_CORRENTE")));
        result.setIdSportello(new Long(rs.getLong("ID_SPORTELLO")));
        result.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        result.setNumeroContoCorrente(rs.getString("NUMERO_CONTO_CORRENTE"));
        result.setCifraCtrl(rs.getString("CIFRA_CONTROLLO"));
        result.setflagValidato(rs.getString("FLAG_VALIDATO"));
        result.setCodPaese(rs.getString("CODICE_PAESE"));
        result.setCin(rs.getString("CIN"));
        result.setBban(rs.getString("BBAN"));
        result.setIban(rs.getString("IBAN"));
        result.setIntestazione(rs.getString("INTESTAZIONE"));
        result.setDataInizioValiditaContoCorrente(rs.getDate("DATA_INIZIO_VALIDITA"));
        result.setDataFineValiditaContoCorrente(rs.getDate("DATA_FINE_VALIDITA"));
        result.setDataEstinzione(rs.getDate("DATA_ESTINZIONE"));
        result.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
        result.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        String denominazioneUtente=rs.getString("DENOMINAZIONE_UTENTE");
        // Utente aggiornamento del Contocorrente
        String enteAppartenenza=rs.getString("DESCRIZIONE_ENTE_APPARTENENZA");
        String separator=" - ";
        if (denominazioneUtente==null || "".equals(denominazioneUtente))
        {
          denominazioneUtente="";
          separator="";
        }
        if (enteAppartenenza==null || "".equals(enteAppartenenza))
        {
          enteAppartenenza="";
          separator="";
        }
        result.setDescUtenteAggiornamento(denominazioneUtente+separator+enteAppartenenza);
        result.setflagValidato(rs.getString("FLAG_VALIDATO"));
        // BANCA
        result.setAbi(rs.getString("ABI"));
        result.setDenominazioneBanca(rs.getString("DENOMINAZIONE_BANCA"));
        result.setDataInizioValiditaBanca(rs.getDate("DATA_INIZIO_VALIDITA_BANCA"));
        result.setDataFineValiditaBanca(rs.getDate("DATA_FINE_VALIDITA_BANCA"));
        result.setIdBanca(new Long(rs.getLong("ID_BANCA")));
        result.setBic(rs.getString("BIC"));
        // SPORTELLO
        result.setIdSportello(new Long(rs.getLong("ID_SPORTELLO")));
        result.setCab(rs.getString("CAB"));
        result.setDenominazioneSportello(rs.getString("DENOMINAZIONE_SPORTELLO"));
        result.setIndirizzoSportello(rs.getString("INDIRIZZO_SPORTELLO"));
        result.setCapSportello(rs.getString("CAP_SPORTELLO"));
        result.setIstatComuneSportello(rs.getString("COMUNE_SPORTELLO"));
        result.setDataInizioValiditaSportello(rs.getDate("DATA_INZIO_VALIDITA_SPORTELLO"));
        result.setDataFineValiditaSportello(rs.getDate("DATA_FINE_VALIDITA_SPORTELLO"));
        result.setDescrizioneComuneSportello(rs.getString("DESC_COMUNE_SPORTELLO"));
        result.setProvinciaSportello(rs.getString("DESC_PROVINCIA_SPORTELLO"));
        result.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));
        result.setFlagSportelloGf(rs.getString("FLAG_SPORTELLO_GF"));
        //Invalidazione
        result.setNote(rs.getString("NOTE"));
        result.setMotivoRivalidazione(rs.getString("MOTIVO_RIVALIDAZIONE"));
        result.setIdTipoCausaInvalidazione(checkLongNull(rs.getString("ID_TIPO_CAUSA_INVALIDAZIONE_CC")));
        result.setDescrizioneCausaInvalidazione(rs.getString("DESCRIZIONE_INVALID"));
        result.setFlagContoGf(rs.getString("FLAG_CONTO_GF"));
        result.setFlagContoVincolato(rs.getString("FLAG_CONTO_VINCOLATO"));
        
      }

      rs.close();

    }
    catch(SQLException exc)
    {
      SolmrLogger.fatal(this, "getContoCorrente - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getContoCorrente - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this, "getContoCorrente - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "getContoCorrente - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }


  // Restituisce l'elenco (ContoCorrenteVO) dei conti correnti legati ad un'azienda
  public Vector<ContoCorrenteVO> getContiCorrenti(Long idAzienda,java.util.Date dataSituazioneAl, boolean soloAnno)
      throws DataAccessException
  {
    Vector<ContoCorrenteVO> result = new Vector<ContoCorrenteVO>();
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();


      String query =
          "SELECT CC.ID_CONTO_CORRENTE,TB.DENOMINAZIONE AS BANCA,"+
          "TS.INDIRIZZO || ' ' || TS.CAP || ' ' || C.DESCOM || ' (' || P.SIGLA_PROVINCIA || ')' AS FILIALE,"+
          "CC.CIN,TB.ABI , TS.CAB , CC.NUMERO_CONTO_CORRENTE,"+
          "CC.INTESTAZIONE AS INTESTATARIO,"+
          "CC.DATA_INIZIO_VALIDITA AS DATA_DAL,"+
          "CC.DATA_ESTINZIONE AS DATA_AL, "+
          "CC.DATA_AGGIORNAMENTO AS DATA_AGGIORNAMENTO, "+
          "CC.ID_UTENTE_AGGIORNAMENTO, "+
          "TS.INDIRIZZO AS INDIRIZZO_SPORTELLO, "+
          "TS.CODICE_PAESE, " +
          "CC.CIFRA_CONTROLLO,	" +
          "CC.FLAG_VALIDATO, " +
          "CC.BBAN " +
          "CC.FLAG_CONTO_GF, " +
          "CC.FLAG_CONTO_VINCOLATO "+
          "FROM DB_CONTO_CORRENTE CC,DB_TIPO_BANCA TB,"+
          "DB_TIPO_SPORTELLO TS,COMUNE C,PROVINCIA P "+
          "WHERE CC.ID_SPORTELLO=TS.ID_SPORTELLO"+
          " AND TS.ID_BANCA=TB.ID_BANCA"+
          " AND TS.COMUNE=C.ISTAT_COMUNE(+)"+
          " AND C.ISTAT_PROVINCIA=P.ISTAT_PROVINCIA(+)"+
          " AND CC.ID_AZIENDA = ?"+
          " AND CC.DATA_INIZIO_VALIDITA < TO_DATE(?,'DD/MM/YYYY')"+
          " AND (CC.DATA_ESTINZIONE IS NULL OR CC.DATA_ESTINZIONE >= TO_DATE(?,'DD/MM/YYYY'))"+
          " AND CC.DATA_FINE_VALIDITA IS NULL "+
          " ORDER BY BANCA";

      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query: "+query);

      stmt.setLong(1, idAzienda.longValue());
      if (soloAnno)
      {
        int anno=DateUtils.extractYearFromDate(dataSituazioneAl);
        stmt.setString(2,"01/01/"+(anno+1));
        stmt.setString(3,"01/01/"+anno);
      }
      else
      {
        stmt.setString(2,DateUtils.formatDate(dataSituazioneAl));
        stmt.setString(3,DateUtils.formatDate(dataSituazioneAl));
      }

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        ContoCorrenteVO ccVO=new ContoCorrenteVO();
        ccVO.setIdContoCorrente(new Long(rs.getString("ID_CONTO_CORRENTE")));
        ccVO.setDenominazioneBanca(rs.getString("BANCA"));
        ccVO.setDenominazioneSportello(rs.getString("FILIALE"));
        ccVO.setIndirizzoSportello(rs.getString("INDIRIZZO_SPORTELLO"));
        ccVO.setCin(rs.getString("CIN"));
        ccVO.setAbi(rs.getString("ABI"));
        ccVO.setCab(rs.getString("CAB"));
        ccVO.setNumeroContoCorrente(rs.getString("NUMERO_CONTO_CORRENTE"));
        ccVO.setIntestazione(rs.getString("INTESTATARIO"));
        ccVO.setDataInizioValiditaContoCorrente(rs.getDate("DATA_DAL"));
        ccVO.setDataEstinzione(rs.getDate("DATA_AL"));
        ccVO.setCifraCtrl(rs.getString("CIFRA_CONTROLLO"));
        ccVO.setflagValidato(rs.getString("FLAG_VALIDATO"));
        ccVO.setCodPaese(rs.getString("CODICE_PAESE"));
        ccVO.setBban(rs.getString("BBAN"));
        ccVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
        ccVO.setIdUtenteAggiornamento(new Long(rs.getString("ID_UTENTE_AGGIORNAMENTO")));
        ccVO.setFlagContoGf(rs.getString("FLAG_CONTO_GF"));
        ccVO.setFlagContoVincolato(rs.getString("FLAG_CONTO_VINCOLATO"));
        result.add(ccVO);
      }

      rs.close();

      SolmrLogger.debug(this, "contiCorrenti - Found "+result.size()+" record(s)");
    }
    catch(SQLException exc)
    {
      SolmrLogger.fatal(this, "contiCorrenti - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "contiCorrenti - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this, "contiCorrenti - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "contiCorrenti - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }


  // Restituisce l'elenco (ContoCorrenteVO) dei conti correnti attivi legati ad un'azienda
  public Vector<ContoCorrenteVO> getContiCorrentiAttivi(Long idAzienda)
      throws DataAccessException
  {
    Vector<ContoCorrenteVO> result = new Vector<ContoCorrenteVO>();
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();


      String query =
          "SELECT CC.ID_CONTO_CORRENTE,TB.DENOMINAZIONE AS BANCA,"+
          "TS.INDIRIZZO || ' ' || TS.CAP || ' ' || C.DESCOM || ' (' || P.SIGLA_PROVINCIA || ')' AS FILIALE,"+
          "CC.CIN,TB.ABI , TS.CAB , CC.NUMERO_CONTO_CORRENTE,"+
          "CC.INTESTAZIONE AS INTESTATARIO,"+
          "CC.DATA_INIZIO_VALIDITA AS DATA_DAL,"+
          "CC.DATA_AGGIORNAMENTO AS DATA_AGGIORNAMENTO, "+
          "CC.ID_UTENTE_AGGIORNAMENTO, "+
          "TS.INDIRIZZO AS INDIRIZZO_SPORTELLO, "+
          "TS.CODICE_PAESE, " +
          "CC.CIFRA_CONTROLLO,	" +
          "CC.FLAG_VALIDATO, " +
          "CC.FLAG_CONTO_GF, " +
          "CC.FLAG_CONTO_VINCOLATO, "+
          "CC.BBAN " +
          "FROM DB_CONTO_CORRENTE CC,DB_TIPO_BANCA TB,"+
          "DB_TIPO_SPORTELLO TS,COMUNE C,PROVINCIA P "+
          "WHERE CC.ID_SPORTELLO=TS.ID_SPORTELLO"+
          " AND TS.ID_BANCA=TB.ID_BANCA"+
          " AND TS.COMUNE=C.ISTAT_COMUNE(+)"+
          " AND C.ISTAT_PROVINCIA=P.ISTAT_PROVINCIA(+)"+
          " AND CC.ID_AZIENDA = ?"+
          " AND CC.DATA_ESTINZIONE IS NULL "+
          " AND CC.DATA_FINE_VALIDITA IS NULL "+
          " ORDER BY BANCA";

      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "getContiCorrentiAttivi Executing query: "+query);

      stmt.setLong(1, idAzienda.longValue());


      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        ContoCorrenteVO ccVO=new ContoCorrenteVO();
        ccVO.setIdContoCorrente(new Long(rs.getString("ID_CONTO_CORRENTE")));
        ccVO.setDenominazioneBanca(rs.getString("BANCA"));
        ccVO.setDenominazioneSportello(rs.getString("FILIALE"));
        ccVO.setIndirizzoSportello(rs.getString("INDIRIZZO_SPORTELLO"));
        ccVO.setCin(rs.getString("CIN"));
        ccVO.setAbi(rs.getString("ABI"));
        ccVO.setCab(rs.getString("CAB"));
        ccVO.setNumeroContoCorrente(rs.getString("NUMERO_CONTO_CORRENTE"));
        ccVO.setIntestazione(rs.getString("INTESTATARIO"));
        ccVO.setDataInizioValiditaContoCorrente(rs.getDate("DATA_DAL"));
        ccVO.setCifraCtrl(rs.getString("CIFRA_CONTROLLO"));
        ccVO.setflagValidato(rs.getString("FLAG_VALIDATO"));
        ccVO.setFlagContoGf(rs.getString("FLAG_CONTO_GF"));
        ccVO.setFlagContoVincolato(rs.getString("FLAG_CONTO_VINCOLATO"));
        ccVO.setCodPaese(rs.getString("CODICE_PAESE"));
        ccVO.setBban(rs.getString("BBAN"));
        ccVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
        ccVO.setIdUtenteAggiornamento(new Long(rs.getString("ID_UTENTE_AGGIORNAMENTO")));
        result.add(ccVO);
      }

      rs.close();

      SolmrLogger.debug(this, "getContiCorrentiAttivi - Found "+result.size()+" record(s)");
    }
    catch(SQLException exc)
    {
      SolmrLogger.fatal(this, "getContiCorrentiAttivi - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getContiCorrentiAttivi - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this, "getContiCorrentiAttivi - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "getContiCorrentiAttivi - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // Restituisce l'elenco (ContoCorrenteVO)di tutti i conti correnti legati all’azienda
  //ordinati dal più recente al più vecchio.
  public Vector<ContoCorrenteVO> getContiCorrentiStorico(Long idAzienda)
      throws DataAccessException
  {
    Vector<ContoCorrenteVO> result = new Vector<ContoCorrenteVO>();
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();


      String query =
          "SELECT CC.ID_CONTO_CORRENTE,TB.DENOMINAZIONE AS BANCA,"+
          "TS.INDIRIZZO || ' ' || TS.CAP || ' ' || C.DESCOM || ' (' || P.SIGLA_PROVINCIA || ')' AS FILIALE,"+
          "CC.CIN,TB.ABI , TS.CAB , CC.NUMERO_CONTO_CORRENTE,"+
          "CC.INTESTAZIONE AS INTESTATARIO,"+
          "CC.DATA_INIZIO_VALIDITA AS DATA_DAL,"+
          "CC.DATA_FINE_VALIDITA, "+
          "CC.DATA_ESTINZIONE, "+
          "CC.DATA_AGGIORNAMENTO AS DATA_AGGIORNAMENTO, "+
          "CC.ID_UTENTE_AGGIORNAMENTO, "+
          "TS.INDIRIZZO AS INDIRIZZO_SPORTELLO, "+
          "TS.CODICE_PAESE, " +
          "CC.CIFRA_CONTROLLO,	" +
          "CC.FLAG_VALIDATO, " +
          "CC.FLAG_CONTO_GF, " +
          "CC.FLAG_CONTO_VINCOLATO, " +
          "CC.BBAN " +
          "FROM DB_CONTO_CORRENTE CC,DB_TIPO_BANCA TB,"+
          "DB_TIPO_SPORTELLO TS,COMUNE C,PROVINCIA P "+
          "WHERE CC.ID_SPORTELLO=TS.ID_SPORTELLO"+
          " AND TS.ID_BANCA=TB.ID_BANCA"+
          " AND TS.COMUNE=C.ISTAT_COMUNE(+)"+
          " AND C.ISTAT_PROVINCIA=P.ISTAT_PROVINCIA(+)"+
          " AND CC.ID_AZIENDA = ?"+
          " ORDER BY DATA_DAL,BANCA";

      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "getContiCorrentiStorico: Executing query: "+query);

      stmt.setLong(1, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        ContoCorrenteVO ccVO=new ContoCorrenteVO();
        ccVO.setIdContoCorrente(new Long(rs.getString("ID_CONTO_CORRENTE")));
        ccVO.setDenominazioneBanca(rs.getString("BANCA"));
        ccVO.setDenominazioneSportello(rs.getString("FILIALE"));
        ccVO.setIndirizzoSportello(rs.getString("INDIRIZZO_SPORTELLO"));
        ccVO.setCin(rs.getString("CIN"));
        ccVO.setAbi(rs.getString("ABI"));
        ccVO.setCab(rs.getString("CAB"));
        ccVO.setNumeroContoCorrente(rs.getString("NUMERO_CONTO_CORRENTE"));
        ccVO.setIntestazione(rs.getString("INTESTATARIO"));
        ccVO.setDataInizioValiditaContoCorrente(rs.getDate("DATA_DAL"));
        ccVO.setDataEstinzione(rs.getDate("DATA_ESTINZIONE"));
        //In questa visualizzazione compare anche una data al. Per i conti estinti,
        //questa data sarà costituita dalla data di estinzione.
        if (ccVO.getDataEstinzione()==null)
          ccVO.setDataEstinzione(rs.getDate("DATA_FINE_VALIDITA"));
        ccVO.setCifraCtrl(rs.getString("CIFRA_CONTROLLO"));
        ccVO.setflagValidato(rs.getString("FLAG_VALIDATO"));
        ccVO.setFlagContoGf(rs.getString("FLAG_CONTO_GF"));
        ccVO.setFlagContoVincolato(rs.getString("FLAG_CONTO_VINCOLATO"));
        ccVO.setCodPaese(rs.getString("CODICE_PAESE"));
        ccVO.setBban(rs.getString("BBAN"));
        ccVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
        ccVO.setIdUtenteAggiornamento(new Long(rs.getString("ID_UTENTE_AGGIORNAMENTO")));
        result.add(ccVO);
      }

      rs.close();

      SolmrLogger.debug(this, "getContiCorrentiStorico - Found "+result.size()+" record(s)");
    }
    catch(SQLException exc)
    {
      SolmrLogger.fatal(this, "getContiCorrentiStorico - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "getContiCorrentiStorico - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this, "getContiCorrentiStorico - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "getContiCorrentiStorico - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }




  // Metodo per effettuare una cancellazione logica da un conto corrente
  public void deleteContoCorrente(Long idConto,Long idUtente)
    throws DataAccessException, DataControlException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
      String query = "UPDATE DB_CONTO_CORRENTE "+
                     "SET DATA_FINE_VALIDITA = SYSDATE, "+
                     "DATA_AGGIORNAMENTO = SYSDATE, "+
                     "ID_UTENTE_AGGIORNAMENTO = ? "+
                     "WHERE ID_CONTO_CORRENTE = ? "+
                     "AND DATA_ESTINZIONE IS NULL";
      stmt = conn.prepareStatement(query);
      stmt.setLong(1, idUtente.longValue());
      stmt.setLong(2, idConto.longValue());
      SolmrLogger.debug(this, "Executing delete contoCorrente"+query);

      if (stmt.executeUpdate()==0)
      {
        stmt.close();
        //Se non è stato modificato nessun record vuol dire che quel conto
        //è estinto oppure non esiste (DB inconsistente)
        query = "SELECT * FROM DB_CONTO_CORRENTE "+
                "WHERE ID_CONTO_CORRENTE = ?";
        stmt = conn.prepareStatement(query);
        stmt.setLong(1, idConto.longValue());
        SolmrLogger.debug(this, "Executing delete contoCorrente"+query);
        if ( (stmt.executeQuery()).next() )
          throw new DataControlException((String)AnagErrors.get("ERR_CANC_CONTO_ESTINTO"));
        else
          throw new DataControlException((String)AnagErrors.get("ERR_CANC_CONTO_INESISTENTE"));
      }

      SolmrLogger.debug(this, "Executed delete contoCorrente");
    }
    catch(DataControlException d)
    {
      throw d;
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in delete delega: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in delete contoCorrente: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this, "SQLException while closing Statement and Connection in delete contoCorrente: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "Generic Exception while closing Statement and Connection in delete contoCorrente: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  /**
   * Metodo per effettuare la modifica di un record della tabella DB_CONTO_CORRENTE
   * @param conto
   * @param idUtente
   * @throws DataAccessException
   */
  public void updateContoCorrente(ContoCorrenteVO conto) throws DataAccessException 
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try 
    {
      conn = getDatasource().getConnection();
      StringBuffer queryBuf= new StringBuffer("" +
      		"UPDATE DB_CONTO_CORRENTE " +
          "SET    CIN = ?, " +
          "       INTESTAZIONE = UPPER(?), " +
          "       DATA_ESTINZIONE = ?, " +
          "       DATA_AGGIORNAMENTO = ?," +
          "       DATA_INIZIO_VALIDITA = ?," +
          "       DATA_FINE_VALIDITA = ?," +
          "       ID_UTENTE_AGGIORNAMENTO = ?, " +
          "       CIFRA_CONTROLLO = UPPER(?), " +
          "       IBAN = UPPER(?), " +
          "       FLAG_VALIDATO = ?, " +
          "       ID_TIPO_CAUSA_INVALIDAZIONE_CC = ?, " +
          "       NOTE = ?, " +
          "       MOTIVO_RIVALIDAZIONE = ?, " +
          "       FLAG_CONTO_GF = ? " +
          "WHERE ID_CONTO_CORRENTE = ? ");
      
      String query = queryBuf.toString();

      SolmrLogger.debug(this,"query="+query);

      stmt = conn.prepareStatement(query);
      
      int idx = 0;
      
      stmt.setString(++idx, conto.getCin());
      stmt.setString(++idx, conto.getIntestazione());
      stmt.setTimestamp(++idx, convertDateToTimestamp(conto.getDataEstinzione()));
      stmt.setTimestamp(++idx, convertDateToTimestamp(conto.getDataAggiornamento()));
      stmt.setTimestamp(++idx, convertDateToTimestamp(conto.getDataInizioValiditaContoCorrente()));
      stmt.setTimestamp(++idx, convertDateToTimestamp(conto.getDataFineValiditaContoCorrente()));
      if(Validator.isNotEmpty(conto.getIdUtenteAggiornamento())) 
      {
        stmt.setLong(++idx, conto.getIdUtenteAggiornamento().longValue());
      }
      else
      {
        stmt.setString(++idx, null);
      }
      stmt.setString(++idx, conto.getCifraCtrl());
      stmt.setString(++idx, conto.getIban());
      stmt.setString(++idx, conto.getflagValidato());
      if(Validator.isNotEmpty(conto.getIdTipoCausaInvalidazione())) 
      {
        stmt.setLong(++idx, conto.getIdTipoCausaInvalidazione().longValue());
      }
      else
      {
        stmt.setString(++idx, null);
      }
      stmt.setString(++idx, conto.getNote());
      stmt.setString(++idx, conto.getMotivoRivalidazione());
      String flagContoGf = "N";
      if(Validator.isNotEmpty(conto.getFlagContoGf()))
      {        
        flagContoGf = conto.getFlagContoGf();
      }
      stmt.setString(++idx, flagContoGf);
      


      stmt.setLong(++idx, conto.getIdContoCorrente().longValue());



      SolmrLogger.debug(this, "Executing updateContoCorrente contoCorrente"+query);
      stmt.executeUpdate();
      SolmrLogger.debug(this, "Executed updateContoCorrente contoCorrente");
    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "SQLException in updateContoCorrente: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) 
    {
      SolmrLogger.error(this, "Generic Exception in updateContoCorrente contoCorrente: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) 
        {
          stmt.close();
        }
        if(conn != null) 
        {
          conn.close();
        }
      }
      catch(SQLException exc) 
      {
        SolmrLogger.error(this, "SQLException while closing Statement and Connection in updateContoCorrente contoCorrente: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) 
      {
        SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in updateContoCorrente contoCorrente: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  /**
   * Metodo che inserisce un record sulla tabella DB_CONTO_CORRENTE
   *
   * @param conto
   * @param idUtente
   * @throws DataAccessException
   * @throws DataControlException
   */
  public void insertContoCorrente(ContoCorrenteVO conto, Long idUtente) 
    throws DataAccessException, SolmrException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    try 
    {
      conn = getDatasource().getConnection();
      String query = " " +
      		"INSERT INTO DB_CONTO_CORRENTE "+
          "      (ID_CONTO_CORRENTE, " +
          "       ID_SPORTELLO, " +
          "       ID_AZIENDA, " +
          "       NUMERO_CONTO_CORRENTE, " +
          "       CIN, " +
          "       INTESTAZIONE, " +
          "       DATA_INIZIO_VALIDITA, " +
          "       DATA_AGGIORNAMENTO, " +
          "       ID_UTENTE_AGGIORNAMENTO," +
          "				CIFRA_CONTROLLO," +
          "				IBAN," +
          "       FLAG_VALIDATO," +
          "       ID_TIPO_CAUSA_INVALIDAZIONE_CC," +
          "       NOTE," +
          "       MOTIVO_RIVALIDAZIONE," +
          "       DATA_ESTINZIONE, " +
          "       FLAG_CONTO_GF) "+
          " VALUES "+
          "      (SEQ_CONTO_CORRENTE.NEXTVAL, ?, ?, ?, UPPER(?), UPPER(?), ?, ?, ?,UPPER(?),UPPER(?), ?, ?, ?, ?, ?, ?) ";

      stmt = conn.prepareStatement(query);
      
      int idx=0;

      stmt.setLong(++idx, conto.getIdSportello().longValue());
      stmt.setLong(++idx, conto.getIdAzienda().longValue());
      stmt.setString(++idx, conto.getNumeroContoCorrente());
      stmt.setString(++idx, conto.getCin());
      stmt.setString(++idx, conto.getIntestazione());
      stmt.setTimestamp(++idx, convertDateToTimestamp(conto.getDataInizioValiditaContoCorrente()));
      stmt.setTimestamp(++idx, convertDateToTimestamp(conto.getDataAggiornamento()));
      stmt.setLong(++idx, idUtente.longValue());
      stmt.setString(++idx, conto.getCifraCtrl());
      stmt.setString(++idx, conto.getIban());
      stmt.setString(++idx, conto.getflagValidato());
      if(Validator.isNotEmpty(conto.getIdTipoCausaInvalidazione())) 
      {
        stmt.setLong(++idx, conto.getIdTipoCausaInvalidazione().longValue());
      }
      else
      {
        stmt.setString(++idx, null);
      }
      stmt.setString(++idx, conto.getNote());
      stmt.setString(++idx, conto.getMotivoRivalidazione());
      
      stmt.setTimestamp(++idx, convertDateToTimestamp(conto.getDataEstinzione()));
      
      String flagContoGf = "N";
      if(Validator.isNotEmpty(conto.getFlagContoGf()))
      {        
        flagContoGf = conto.getFlagContoGf();
      }
      stmt.setString(++idx, flagContoGf);
      
      SolmrLogger.debug(this, "Executing insertContoCorrente contoCorrente"+query);

      stmt.executeUpdate();
      
      SolmrLogger.debug(this, "Executed insertContoCorrente contoCorrente");
    }
    catch(SQLException exc) 
    {
      if (exc.getErrorCode() == ((Long) SolmrConstants
          .get("CODE_DUP_KEY_ORACLE")).intValue())
      {
        SolmrLogger
            .fatal(this, "SQLException in insertContoCorrente: CODE_DUP_KEY_ORACLE");
        throw new SolmrException(AnagErrors.ERRORE_CC_DUP_KEY);
      }
      else
      {
        SolmrLogger
            .fatal(this, "SQLException in insertContoCorrente: " + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
    }
    catch(Exception ex) 
    {
      SolmrLogger.error(this, "Generic Exception in insertContoCorrente contoCorrente: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) 
        {
          stmt.close();
        }
        if(conn != null) 
        {
          conn.close();
        }
      }
      catch(SQLException exc) 
      {
        SolmrLogger.error(this, "SQLException while closing Statement and Connection in insertContoCorrente contoCorrente: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) 
      {
        SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in insertContoCorrente contoCorrente: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }


  /**
   * Restituisce l'elenco (ContoCorrenteVO) delle banche associate ad un ABI / Denominazione
   * @param abi parametro di ricerca
   * @param denominazione parametro di ricerca
   * @return restituisce un array di BancaSportelloVO con l'elenco delle banche.
   * Se non ci sono record restituisce null
   * @throws DataAccessException
   */
  public BancaSportelloVO[] searchBanca(String abi,String denominazione)
      throws DataAccessException, DataControlException
  {
    Vector<BancaSportelloVO> result = new Vector<BancaSportelloVO>();
    Connection conn = null;
    PreparedStatement stmt = null;
    String query=null;
    try
    {
      conn = getDatasource().getConnection();


      query =
          " SELECT "+
          "   TB.ID_BANCA, "+
          "   TB.ABI, "+
          "	  TB.BIC,	" +
          "   TB.DENOMINAZIONE, "+
          "   TB.DATA_INIZIO_VALIDITA, "+
          "   TB.DATA_FINE_VALIDITA "+
          " FROM "+
          "   DB_TIPO_BANCA TB "+
          " WHERE ";
      String andStr="";
      if (!"".equals(abi) && (abi!=null))
      {
        query+=andStr+" TB.ABI = ? AND ";
      }
      if (!"".equals(denominazione) && (denominazione!=null))
      {
        query+=andStr+" UPPER(TB.DENOMINAZIONE) LIKE UPPER(?) AND ";
      }
      query+=" DATA_FINE_VALIDITA IS NULL "+
             " ORDER BY DENOMINAZIONE";

      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query: "+query);

      int indice=1;

      if (!"".equals(abi) && (abi!=null))
      {
        stmt.setString(indice++,abi);
      }
      if (!"".equals(denominazione) && (denominazione!=null))
      {
        stmt.setString(indice++,"%"+denominazione.toUpperCase()+"%");
      }

      ResultSet rs = stmt.executeQuery();
      int count=0;
      while (rs.next())
      {
        count++;
        if (count>ErrorTypes.NUM_MAX_RECORD)
        {
          throw new DataControlException(ErrorTypes.STR_MAX_RECORD);
        }
        BancaSportelloVO bsVO=new BancaSportelloVO();
        bsVO.setIdBanca(new Long(rs.getString("ID_BANCA")));
        bsVO.setBic(rs.getString("BIC"));
        bsVO.setAbi(rs.getString("ABI"));
        bsVO.setDenominazioneBanca(rs.getString("DENOMINAZIONE"));
        bsVO.setDataInizioValiditaBanca(rs.getDate("DATA_INIZIO_VALIDITA"));
        bsVO.setDataFineValiditaBanca(rs.getDate("DATA_FINE_VALIDITA"));
        result.add(bsVO);
      }

      rs.close();

      SolmrLogger.debug(this, "searchBanca - Found "+result.size()+" record(s)");
    }
    catch(SQLException exc)
    {
      SolmrLogger.fatal(this, "searchBanca - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch(DataControlException exc)
    {
      SolmrLogger.fatal(this, "searchBanca - DataControlException: "+exc.getMessage());
      throw exc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "searchBanca - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this, "searchBanca - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "searchBanca - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result.size()==0?null:(BancaSportelloVO[]) result.toArray(new BancaSportelloVO[0]);
  }

  public BancaSportelloVO[] searchSportello(String abi,String cab,String comune)
      throws DataAccessException, DataControlException
  {
    Vector<BancaSportelloVO> result = new Vector<BancaSportelloVO>();
    Connection conn = null;
    PreparedStatement stmt = null;
    String query=null;
    try
    {
      conn = getDatasource().getConnection();

      query =
          " SELECT "+
          "   TB.ID_BANCA AS ID_BANCA, "+
          "   TB.ABI AS ABI, "+
          "   TB.DENOMINAZIONE AS DENOMINAZIONE_BANCA,"+
          "   TB.DATA_INIZIO_VALIDITA AS DATA_INIZIO_VALIDITA_BANCA, "+
          "   TB.DATA_FINE_VALIDITA AS DATA_FINE_VALIDITA_BANCA, "+
          "   TS.ID_SPORTELLO, "+
          "   TS.CAB, "+
          "   TS.DENOMINAZIONE AS DENOMINAZIONE_SPORTELLO, "+
          "   TS.INDIRIZZO AS INDIRIZZO_SPORTELLO, "+
          "   TS.CAP AS CAP_SPORTELLO, "+
          "   TS.COMUNE AS COMUNE_SPORTELLO, "+
          "   TS.DATA_INIZIO_VALIDITA AS DATA_INIZIO_VALIDITA_SPORTELLO, "+
          "   TS.DATA_FINE_VALIDITA AS DATA_FINE_VALIDITA_SPORTELLO, "+
          "   TS.CODICE_PAESE," +
          "   TS.FLAG_SPORTELLO_GF, " +
          "   C.DESCOM AS DESC_COMUNE_SPORTELLO, "+
          "   P.DESCRIZIONE AS DESC_PROVINCIA_SPORTELLO, "+
          "   P.SIGLA_PROVINCIA  AS SIGLA_PROVINCIA_SPORTELLO "+
          " FROM DB_TIPO_BANCA TB, "+
          "   COMUNE C, "+
          "   PROVINCIA P, "+
          "   DB_TIPO_SPORTELLO TS "+
          " WHERE "+
          "   TS.ID_BANCA=TB.ID_BANCA "+
          "   AND TS.COMUNE=C.ISTAT_COMUNE(+) "+
          "   AND TB.ABI = ? "+
          "   AND P.ISTAT_PROVINCIA(+)=C.ISTAT_PROVINCIA " +
          "   AND TS.DATA_FINE_VALIDITA IS NULL ";

      if (!"".equals(cab) && (cab!=null))
      {
        query+="AND TS.CAB = ? ";
      }
      if (!"".equals(comune) && (comune!=null))
      {
        query+="AND UPPER(C.DESCOM) LIKE ? ";
      }
      query+="ORDER BY DESCOM ";

      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query: "+query);

      int indice=1;

      stmt.setString(indice++,abi);

      if (!"".equals(cab) && (cab!=null))
      {
        stmt.setString(indice++,cab);
      }
      if (!"".equals(comune) && (comune!=null))
      {
        stmt.setString(indice++,"%"+comune.toUpperCase()+"%");
      }

      ResultSet rs = stmt.executeQuery();

      int count=0;

      while (rs.next())
      {
        count++;
        if (count>ErrorTypes.NUM_MAX_RECORD)
        {
          throw new DataControlException(ErrorTypes.STR_MAX_RECORD);
        }
        BancaSportelloVO bsVO=new BancaSportelloVO();
        bsVO.setIdBanca(new Long(rs.getString("ID_BANCA")));
        bsVO.setAbi(rs.getString("ABI"));
        bsVO.setDenominazioneBanca(rs.getString("DENOMINAZIONE_BANCA"));
        bsVO.setDataInizioValiditaBanca(rs.getDate("DATA_INIZIO_VALIDITA_BANCA"));
        bsVO.setDataFineValiditaBanca(rs.getDate("DATA_FINE_VALIDITA_BANCA"));
        bsVO.setIdSportello(new Long(rs.getString("ID_SPORTELLO")));
        bsVO.setCab(rs.getString("CAB"));
        bsVO.setDenominazioneSportello(rs.getString("DENOMINAZIONE_SPORTELLO"));
        bsVO.setIndirizzoSportello(rs.getString("INDIRIZZO_SPORTELLO"));
        bsVO.setCapSportello(rs.getString("CAP_SPORTELLO"));
        bsVO.setIstatComuneSportello(rs.getString("COMUNE_SPORTELLO"));
        bsVO.setDataInizioValiditaSportello(rs.getDate("DATA_INIZIO_VALIDITA_SPORTELLO"));
        bsVO.setDataFineValiditaSportello(rs.getDate("DATA_FINE_VALIDITA_SPORTELLO"));
        bsVO.setDescrizioneComuneSportello(rs.getString("DESC_COMUNE_SPORTELLO"));
        bsVO.setProvinciaSportello(rs.getString("DESC_PROVINCIA_SPORTELLO"));
        bsVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA_SPORTELLO"));
        bsVO.setCodPaeseSportello(rs.getString("CODICE_PAESE"));
        bsVO.setFlagSportelloGf(rs.getString("FLAG_SPORTELLO_GF"));
        
        result.add(bsVO);
      }

      rs.close();

      SolmrLogger.debug(this, "searchSportello - Found "+result.size()+" record(s)");
    }
    catch(SQLException exc)
    {
      SolmrLogger.fatal(this, "searchSportello - Query: "+query);
      SolmrLogger.fatal(this, "searchSportello - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch(DataControlException exc)
    {
      SolmrLogger.fatal(this, "searchSportello - DataControlException: "+exc.getMessage());
      throw exc;
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "searchSportello - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this, "searchSportello - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "searchSportello - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result.size()==0?null:(BancaSportelloVO[])result.toArray(new BancaSportelloVO[0]);
  }

  // Metodo per effettuare l'estinzione dei conti correnti collegati ad un'azienda agricola
  public void desistsAccountCorrent(Long idAzienda, Long idUtente) throws DataAccessException {

    Connection conn = null;
    PreparedStatement stmt = null;

    try {
      conn = getDatasource().getConnection();

      String query = " UPDATE DB_CONTO_CORRENTE "+
                     " SET    DATA_ESTINZIONE = SYSDATE, "+
                     "        DATA_AGGIORNAMENTO = SYSDATE, "+
                     "        ID_UTENTE_AGGIORNAMENTO = ? "+
                     " WHERE  ID_AZIENDA = ? "+
                     " AND    DATA_ESTINZIONE IS NULL " +
                     " AND    DATA_FINE_VALIDITA IS NULL";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idUtente.longValue());
      stmt.setLong(2, idAzienda.longValue());

      SolmrLogger.debug(this, "Executing desistsAccountCorrent in ContoCorrenteDAO"+query);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed desistsAccountCorrent in ContoCorrenteDAO");
    }
    catch (SQLException exc) {
      SolmrLogger.fatal(this, "SQLException in desistsAccountCorrent in ContoCorrenteDAO: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this, "Generic Exception in desistsAccountCorrent in ContoCorrenteDAO: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this, "SQLException while closing Statement and Connection in desistsAccountCorrent in ContoCorrenteDAO: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this, "Generic Exception while closing Statement and Connection in desistsAccountCorrent in ContoCorrenteDAO: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }



}
