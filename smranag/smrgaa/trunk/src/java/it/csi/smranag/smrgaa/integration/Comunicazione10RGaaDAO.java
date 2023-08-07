package it.csi.smranag.smrgaa.integration;


import it.csi.smranag.smrgaa.dto.PlSqlCodeDescription;
import it.csi.smranag.smrgaa.dto.comunicazione10R.AcquaExtraVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.Comunicazione10RVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.EffluenteCesAcqVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.EffluenteStocExtVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.EffluenteVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.RefluoEffluenteVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.TipoAcquaAgronomicaVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.TipoCausaleEffluenteVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.TipoEffluenteVO;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Date;
import java.util.Vector;


public class Comunicazione10RGaaDAO extends it.csi.solmr.integration.BaseDAO {
  
  
  public Comunicazione10RGaaDAO() throws ResourceAccessException{
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public Comunicazione10RGaaDAO(String refName) throws ResourceAccessException {
   super(refName);
  }
  
  
  public Comunicazione10RVO getComunicazione10RByIdUteAndPianoRifererimento(long idUte, long idPianoRiferimento) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Comunicazione10RVO com10RVO = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[Comunicazione10RGaaDAO::getComunicazione10RByIdUteAndPianoRifererimento] BEGIN.");

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */

      queryBuf = new StringBuffer();
      queryBuf
          .append("   SELECT   " 
              + "     COM.ID_COMUNICAZIONE_10R, "
              + "     COM.ID_UTENTE_AGGIORNAMENTO, "
              + "     COM.DATA_INIZIO_VALIDITA,   "
              + "     COM.DATA_FINE_VALIDITA,   "
              + "     COM.DATA_AGGIORNAMENTO,   "
              + "     COM.DATA_RICALCOLO,   "
              + "     COM.VOLUME_SOTTOGRIGLIATO,   "
              + "     COM.VOLUME_REFLUO_AZIENDA,  "
              + "     COM.SUPERFICIE_CONDUZIONE_ZVN ,  "
              + "     COM.SUPERFICIE_ASSERVIMENTO_ZVN , "
              + "     COM.SUPERFICIE_CONDUZIONE_NO_ZVN ,  "
              + "     COM.SUPERFICIE_ASSERVIMENTO_NO_ZVN , "
              + "     COM.AZOTO_CONDUZIONE_ZVN , "
              + "     COM.AZOTO_ASSERVIMENTO_ZVN , "
              + "     COM.AZOTO_CONDUZIONE_NO_ZVN , "
              + "     COM.AZOTO_ASSERVIMENTO_NO_ZVN , "
              + "     COM.TOTALE_AZOTO_AZIENDALE , "
              + "     COM.STOC_NETTO_PALABILE , "
              + "     COM.STOC_NETTO_NONPALABILE , "
              + "     COM.STOC_DISP_PALABILE_VOL , "
              + "     COM.STOC_DISP_NONPALABILE_VOL , "
              + "     COM.VOLUME_PIOGGE, "
              + "     COM.ACQUE_LAVAGGIO,  "
              + "     COM.STOC_ACQUE_NETTO_CESSIONE,  "
              + "     COM.STOC_ACQUE_NEC_GG,  "
              + "     COM.STOC_ACQUE_NEC_VOL,  "
              + "     COM.NOTE, "
              + "     COM.ID_UTE, "
              + "     COM.TOTALE_AZOTO, "
              + "     COM.SUPERFICIE_SAU_PIEMONTE, "
              + "     COM.SUPERFICIE_SAU_PIEMONTE_ZVN, "
              + "     COM.ADESIONE_DEROGA, "
              + "     COM.AZOTO_ESCRETO_PASCOLO, "
              + "     NVL(PVU.COGNOME_UTENTE, TRIM(UPPER(PVU.COGNOME_UTENTE_LOGIN)))||' '||NVL(PVU.NOME_UTENTE, TRIM(UPPER(PVU.NOME_UTENTE_LOGIN))) AS UT_DENOMINAZIONE, "
              + "     PVU.DENOMINAZIONE AS UT_ENTE_APPART "
              + "   FROM   "
              + "     DB_COMUNICAZIONE_10R COM,   "
              + "     PAPUA_V_UTENTE_LOGIN PVU, "
              + "     DB_UTE U, "
              +	"     COMUNE C ");
              
      if(idPianoRiferimento > 0) 
      {
        queryBuf
          .append(""
              + "    ,DB_DICHIARAZIONE_CONSISTENZA DC ");
      }
      
      queryBuf
      .append(""
            + "  WHERE  "
            + "     COM.ID_UTE = ?  "
            + " AND COM.ID_UTE = U.ID_UTE  "
            + " AND U.COMUNE = C.ISTAT_COMUNE "
            + " AND COM.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN ");
      
      
      if(idPianoRiferimento > 0) {
        queryBuf
        .append(""
              + " AND  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
               "  AND  COM.DATA_INIZIO_VALIDITA < DC.DATA_INSERIMENTO_DICHIARAZIONE " +
               "  AND  NVL(COM.DATA_FINE_VALIDITA, ?) > DC.DATA_INSERIMENTO_DICHIARAZIONE ");
      }
      else 
      {
        queryBuf
          .append(""
              + " AND  COM.DATA_FINE_VALIDITA IS NULL ");
      }
      
      queryBuf.append("ORDER BY C.DESCOM, U.INDIRIZZO ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::getComunicazione10RByIdUteAndPianoRifererimento] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice,idUte);
      
      if(idPianoRiferimento > 0) {
        stmt.setLong(++indice, idPianoRiferimento);
        stmt.setDate(++indice, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
      }     

      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {
        com10RVO = new Comunicazione10RVO();
        com10RVO.setIdComunicazione10R(rs.getLong("ID_COMUNICAZIONE_10R"));
        com10RVO.setIdUtenteAggiornamento(rs.getLong("ID_UTENTE_AGGIORNAMENTO"));
        com10RVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        com10RVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        com10RVO.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO"));
        com10RVO.setDataRicalcolo(rs.getTimestamp("DATA_RICALCOLO"));
        com10RVO.setVolumeSottogrigliato(rs.getBigDecimal("VOLUME_SOTTOGRIGLIATO"));
        com10RVO.setVolumeRefluoAzienda(rs.getBigDecimal("VOLUME_REFLUO_AZIENDA"));
        com10RVO.setSuperficieConduzioneZVN(rs.getBigDecimal("SUPERFICIE_CONDUZIONE_ZVN"));
        com10RVO.setSuperficieAsservimentoZVN(rs.getBigDecimal("SUPERFICIE_ASSERVIMENTO_ZVN"));
        com10RVO.setSuperficieConduzioneNoZVN(rs.getBigDecimal("SUPERFICIE_CONDUZIONE_NO_ZVN"));
        com10RVO.setSuperficieAsservimentoNoZVN(rs.getBigDecimal("SUPERFICIE_ASSERVIMENTO_NO_ZVN"));
        com10RVO.setAzotoConduzioneZVN(rs.getBigDecimal("AZOTO_CONDUZIONE_ZVN"));
        com10RVO.setAzotoAsservimentoZVN(rs.getBigDecimal("AZOTO_ASSERVIMENTO_ZVN"));
        com10RVO.setAzotoConduzioneNoZVN(rs.getBigDecimal("AZOTO_CONDUZIONE_NO_ZVN"));
        com10RVO.setAzotoAsservimentoNoZVN(rs.getBigDecimal("AZOTO_ASSERVIMENTO_NO_ZVN"));
        com10RVO.setTotaleAzotoAziendale(rs.getBigDecimal("TOTALE_AZOTO_AZIENDALE"));
        com10RVO.setStocNettoPalabile(rs.getBigDecimal("STOC_NETTO_PALABILE"));
        com10RVO.setStocNettoNonPalabile(rs.getBigDecimal("STOC_NETTO_NONPALABILE"));
        com10RVO.setStocDispPalabileVol(rs.getBigDecimal("STOC_DISP_PALABILE_VOL"));
        com10RVO.setStocDispNonPalabileVol(rs.getBigDecimal("STOC_DISP_NONPALABILE_VOL"));
        com10RVO.setVolumePiogge(rs.getBigDecimal("VOLUME_PIOGGE"));
        com10RVO.setAcqueLavaggio(rs.getBigDecimal("ACQUE_LAVAGGIO"));
        com10RVO.setStocAcqueNettoCessione(rs.getBigDecimal("STOC_ACQUE_NETTO_CESSIONE"));
        com10RVO.setStocAcqueNecGG(rs.getBigDecimal("STOC_ACQUE_NEC_GG"));
        com10RVO.setStocAcqueNecVol(rs.getBigDecimal("STOC_ACQUE_NEC_VOL"));
        com10RVO.setNote(rs.getString("NOTE"));
        com10RVO.setIdUte(rs.getLong("ID_UTE"));
        com10RVO.setTotaleAzoto(rs.getBigDecimal("TOTALE_AZOTO"));
        com10RVO.setSuperficieSauPiemonte(rs.getBigDecimal("SUPERFICIE_SAU_PIEMONTE"));
        com10RVO.setSuperficieSauPiemonteZVN(rs.getBigDecimal("SUPERFICIE_SAU_PIEMONTE_ZVN"));
        com10RVO.setAdesioneDeroga(rs.getString("ADESIONE_DEROGA"));
        com10RVO.setAzotoEscretoPascolo(rs.getBigDecimal("AZOTO_ESCRETO_PASCOLO"));
        com10RVO.setUtenteUltimaModifica(rs.getString("UT_DENOMINAZIONE"));
        com10RVO.setEnteUltimaModifica(rs.getString("UT_ENTE_APPART"));
      }
      return com10RVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("com10RVO", com10RVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUte", idUte),  
        new Parametro("idPianoRiferimento", idPianoRiferimento) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::getComunicazione10RByIdUteAndPianoRifererimento] ",
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
              "[Comunicazione10RGaaDAO::getComunicazione10RByIdUteAndPianoRifererimento] END.");
    }
  }
  
  
  /**
   * Ritorna un vettore di TipoCausaleEffluenteVO validi (data_fine_validita == null) 
   * Query una volta usata usata per cessionie e acquisizioni ora solo acquisizioni 
   * quindi inutile ma per facilità di codic nella jsp mantenuta...
   * chi ha eventualmente il coraggio...
   * 
   * @return
   * @throws DataAccessException
   */
  public Vector<TipoCausaleEffluenteVO> getListTipoCausaleEffluente() 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoCausaleEffluenteVO> vTipoCausaleEffluente = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[Comunicazione10RGaaDAO::getListTipoCausaleEffluente] BEGIN.");

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   SELECT   " 
              + "     TCA.ID_CAUSALE_EFFLUENTE, " 
              + "     TCA.DESCRIZIONE, "
              + "     TCA.DATA_INIZIO_VALIDITA   "
              + "   FROM DB_TIPO_CAUSALE_EFFLUENTE TCA   "
              + "   WHERE  TCA.DATA_FINE_VALIDITA IS NULL  "
              + "   AND    TCA.ID_CAUSALE_EFFLUENTE = 2 ");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::getListTipoCausaleEffluente] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vTipoCausaleEffluente == null)
        {
          vTipoCausaleEffluente = new Vector<TipoCausaleEffluenteVO>();
        }
        TipoCausaleEffluenteVO tipoVO  = new TipoCausaleEffluenteVO();
        tipoVO.setIdCausaleEffluente(rs.getLong("ID_CAUSALE_EFFLUENTE"));
        tipoVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        vTipoCausaleEffluente.add(tipoVO);
      }
      return vTipoCausaleEffluente;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vTipoCausaleEffluente", vTipoCausaleEffluente) };
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::getListTipoCausaleEffluente] ",
              t, query, variabili, null);
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
              "[Comunicazione10RGaaDAO::getListTipoCausaleEffluente] END.");
    }
  }
  
  
  /**
   * Ritorna un vettore di TipoEffluenteVO validi (data_fine_validita == null) 
   * 
   * @return
   * @throws DataAccessException
   */
  public Vector<TipoEffluenteVO> getListTipoEffluente() 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoEffluenteVO> vTipoEffluente = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[Comunicazione10RGaaDAO::getListTipoEffluente] BEGIN.");

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   SELECT   " 
              + "     TE.ID_EFFLUENTE, " 
              + "     TE.DESCRIZIONE, "
              + "     TE.FLAG_PALABILE, "
              + "     TE.DATA_INIZIO_VALIDITA   "
              + "   FROM   "
              + "     DB_TIPO_EFFLUENTE TE   "
              + "   WHERE  "
              + "     TE.DATA_FINE_VALIDITA IS NULL  "
              + " AND TE.FLAG_APPORTO_ORGANICO = 'N' "
              + "   ORDER BY TE.DESCRIZIONE ");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::getListTipoEffluente] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vTipoEffluente == null)
        {
          vTipoEffluente = new Vector<TipoEffluenteVO>();
        }
        TipoEffluenteVO tipoVO  = new TipoEffluenteVO();
        tipoVO.setIdEffluente(rs.getLong("ID_EFFLUENTE"));
        tipoVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoVO.setFlagPalabile(rs.getString("FLAG_PALABILE"));
        tipoVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        vTipoEffluente.add(tipoVO);
      }
      return vTipoEffluente;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vTipoEffluente", vTipoEffluente) };
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::getListTipoEffluente] ",
              t, query, variabili, null);
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
              "[Comunicazione10RGaaDAO::getListTipoEffluente] END.");
    }
  }
  
  
  public TipoEffluenteVO getTipoEffluenteById(long idEffluente) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    TipoEffluenteVO tipoEffluente = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[Comunicazione10RGaaDAO::getTipoEffluenteById] BEGIN.");

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   SELECT   " 
              + "     TE.ID_EFFLUENTE, " 
              + "     TE.DESCRIZIONE, "
              + "     TE.FLAG_PALABILE, "
              + "     TE.DATA_INIZIO_VALIDITA   "
              + "   FROM   "
              + "     DB_TIPO_EFFLUENTE TE   "
              + "   WHERE  "
              + "     TE.ID_EFFLUENTE  = ?  ");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::getTipoEffluenteById] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;
      stmt.setLong(++indice, idEffluente);
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {
        tipoEffluente = new TipoEffluenteVO();
        
        tipoEffluente.setIdEffluente(rs.getLong("ID_EFFLUENTE"));
        tipoEffluente.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoEffluente.setFlagPalabile(rs.getString("FLAG_PALABILE"));
        
      }
      return tipoEffluente;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("tipoEffluente", tipoEffluente) };
      
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idEffluente", idEffluente)};
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::getTipoEffluenteById] ",
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
              "[Comunicazione10RGaaDAO::getTipoEffluenteById] END.");
    }
  }
  
  /**
   * Ritorna un vettore di TipoAcquaAgronomicaVO validi (data_fine_validita == null) 
   * 
   * @return
   * @throws DataAccessException
   */
  public Vector<TipoAcquaAgronomicaVO> getListTipoAcquaAgronomica() 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoAcquaAgronomicaVO> vTipoAcquaAgronimica = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[Comunicazione10RGaaDAO::getListTipoAcquaAgronomica] BEGIN.");

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   SELECT   " 
              + "     TAA.ID_ACQUA_AGRONOMICA, " 
              + "     TAA.DESCRIZIONE, "
              + "     TAA.DATA_INIZIO_VALIDITA   "
              + "   FROM   "
              + "     DB_TIPO_ACQUA_AGRONOMICA TAA   "
              + "   WHERE  "
              + "     TAA.DATA_FINE_VALIDITA IS NULL  ");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::getListTipoAcquaAgronomica] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vTipoAcquaAgronimica == null)
        {
          vTipoAcquaAgronimica = new Vector<TipoAcquaAgronomicaVO>();
        }
        TipoAcquaAgronomicaVO tipoVO  = new TipoAcquaAgronomicaVO();
        tipoVO.setIdAcquaAgronomica(rs.getLong("ID_ACQUA_AGRONOMICA"));
        tipoVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        vTipoAcquaAgronimica.add(tipoVO);
      }
      return vTipoAcquaAgronimica;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vTipoAcquaAgronimica", vTipoAcquaAgronimica) };
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::getListTipoAcquaAgronomica] ",
              t, query, variabili, null);
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
              "[Comunicazione10RGaaDAO::getListTipoAcquaAgronomica] END.");
    }
  }
  
  /**
   * Ritorna un vettore di EffluenteCesAcqVO associato ad una comunicazione
   * 
   * @param idComunicazione10R
   * @return
   * @throws DataAccessException
   */
  public Vector<EffluenteCesAcqVO> getListEffluentiCessAcquByidComunicazione(long idComunicazione10R[], Long idTipoCausale) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<EffluenteCesAcqVO> vEffluenteCessAcqu = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[Comunicazione10RGaaDAO::getListEffluentiCessAcquByidComunicazione] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      String parametri=" (";
      
      if (idComunicazione10R!=null && idComunicazione10R.length!=0)
      {
        for(int i=0;i<idComunicazione10R.length;i++)
        {
          parametri+="?";
          if (i<idComunicazione10R.length-1)
            parametri+=",";
        }
      }
      parametri+=") ";
  
      queryBuf = new StringBuffer();
      queryBuf.append("   SELECT   " 
              + "     ECA.ID_EFFLUENTE_CES_ACQ_10R, " 
              + "     ECA.ID_COMUNICAZIONE_10R, "
              + "     ECA.ID_AZIENDA, "
              + "     ECA.CUAA AS CESCUAA,   "
              + "     ECA.ISTAT_COMUNE,   "
              + "     ECA.DENOMINAZIONE AS CESDENOMINAZIONE,  "
              + "     ECA.ID_EFFLUENTE,  "
              + "     ECA.ID_CAUSALE_EFFLUENTE,  "
              + "     ECA.QUANTITA,  "
              + "     ECA.QUANTITA_AZOTO,  "
              + "     ECA.QUANTITA_AZOTO_DICHIARATO,  "
              + "     ECA.FLAG_STOCCAGGIO,  "
              + "     TCE.DESCRIZIONE AS DESCRIZIONE, "
              + "     COM.DESCOM, "
              + "     PROV.SIGLA_PROVINCIA, "
              + "     CR.ID_UTE, "
              + "     AA.CUAA AS AZCUAA, "
              + "     AA.DENOMINAZIONE AS AZDENOMINAZIONE, "
              + "     TE.DESCRIZIONE AS DESC_TIPO_EFFLUENTE"
              + "   FROM   "
              + "     DB_EFFLUENTE_CES_ACQ_10R ECA, "
              + "     DB_TIPO_CAUSALE_EFFLUENTE TCE, "
              + "     DB_TIPO_EFFLUENTE TE, "
              +	"     COMUNE COM, PROVINCIA PROV, "
              + "     COMUNE COMUTE, "
              + "     DB_COMUNICAZIONE_10R CR, "
              + "     DB_UTE UT, "
              + "     DB_ANAGRAFICA_AZIENDA AA "
              + "   WHERE  "
              + "     ECA.ID_COMUNICAZIONE_10R IN " +parametri 
              + " AND ECA.ISTAT_COMUNE = COM.ISTAT_COMUNE  "
              + " AND COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA "
              + " AND ECA.ID_CAUSALE_EFFLUENTE = TCE.ID_CAUSALE_EFFLUENTE ");
           if(Validator.isNotEmpty(idTipoCausale))
           {
             queryBuf.append("" +
             " AND ECA.ID_CAUSALE_EFFLUENTE = ? ");
           }
           queryBuf.append(""  
              + " AND ECA.ID_EFFLUENTE = TE.ID_EFFLUENTE "
              + " AND ECA.ID_AZIENDA = AA.ID_AZIENDA (+) "
              + " AND CR.ID_COMUNICAZIONE_10R = ECA.ID_COMUNICAZIONE_10R "
              + " AND CR.ID_UTE = UT.ID_UTE "
              + " AND UT.COMUNE = COMUTE.ISTAT_COMUNE "
              + " AND AA.DATA_FINE_VALIDITA IS NULL "
              + " ORDER BY COMUTE.DESCOM,UT.INDIRIZZO,ECA.DENOMINAZIONE,TCE.DESCRIZIONE,TE.DESCRIZIONE ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::getListEffluentiCessAcquByidComunicazione] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      if (idComunicazione10R!=null && idComunicazione10R.length!=0)
      {
        for(int i=0;i<idComunicazione10R.length;i++)
          stmt.setLong(++indice,idComunicazione10R[i]);
      }
      
      
      if(Validator.isNotEmpty(idTipoCausale))
      {
        stmt.setLong(++indice, idTipoCausale.longValue());
      }
      
      
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vEffluenteCessAcqu == null)
        {
          vEffluenteCessAcqu = new Vector<EffluenteCesAcqVO>();
        }
        EffluenteCesAcqVO effVO = new EffluenteCesAcqVO();
        effVO.setIdEffluenteCesAcq10R(rs.getLong("ID_EFFLUENTE_CES_ACQ_10R"));
        effVO.setIdComunicazione10R(rs.getLong("ID_COMUNICAZIONE_10R"));
        
        effVO.setIdAzienda(checkLongNull(rs.getString("ID_AZIENDA")));
        
        if(Validator.isNotEmpty(rs.getString("AZCUAA")))
        {
          effVO.setCuaa(rs.getString("AZCUAA"));
        }
        else
        {
          effVO.setCuaa(rs.getString("CESCUAA"));
        }
        
        effVO.setIstatComune(rs.getString("ISTAT_COMUNE"));
        
        if(Validator.isNotEmpty(rs.getString("AZDENOMINAZIONE")))
        {
          effVO.setDenominazione(rs.getString("AZDENOMINAZIONE"));
        }
        else
        {
          effVO.setDenominazione(rs.getString("CESDENOMINAZIONE"));
        }
        
        effVO.setIdEffluente(new Long(rs.getLong("ID_EFFLUENTE")));
        effVO.setIdCausaleEffluente(new Long(rs.getLong("ID_CAUSALE_EFFLUENTE")));
        effVO.setQuantita(rs.getBigDecimal("QUANTITA"));
        effVO.setQuantitaAzoto(rs.getBigDecimal("QUANTITA_AZOTO"));
        effVO.setQuantitaAzotoDichiarato(rs.getBigDecimal("QUANTITA_AZOTO_DICHIARATO"));
        effVO.setFlagStoccaggio(rs.getString("FLAG_STOCCAGGIO"));
        if((rs.getString("FLAG_STOCCAGGIO") !=null) && rs.getString("FLAG_STOCCAGGIO").equals("S"))
        {
          effVO.setFlagStoccaggioBl(true);
        }
        else
        {
          effVO.setFlagStoccaggioBl(false);
        }
        effVO.setDescrizione(rs.getString("DESCRIZIONE"));
        effVO.setDescTipoEffluente(rs.getString("DESC_TIPO_EFFLUENTE"));
        effVO.setDescComune(rs.getString("DESCOM"));
        effVO.setSglProv(rs.getString("SIGLA_PROVINCIA"));
        effVO.setIdUte(rs.getLong("ID_UTE"));
        vEffluenteCessAcqu.add(effVO);
      }
      return vEffluenteCessAcqu;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vEffluenteCessAcqu", vEffluenteCessAcqu) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idComunicazione10R", idComunicazione10R)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::getListEffluentiCessAcquByidComunicazione] ",
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
              "[Comunicazione10RGaaDAO::getListEffluentiCessAcquByidComunicazione] END.");
    }
  }
  
  
  
  
  /**
   * Ritorna un vettore di EffluenteCesAcqVO associato ad una comunicazione
   * 
   * @param idComunicazione10R
   * @return
   * @throws DataAccessException
   */
  public Vector<EffluenteCesAcqVO> getListEffluentiCessAcquPerStampa(long idComunicazione10R[]) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<EffluenteCesAcqVO> vEffluenteCessAcqu = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[Comunicazione10RGaaDAO::getListEffluentiCessAcquPerStampa] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      String parametri=" (";
      
      if (idComunicazione10R!=null && idComunicazione10R.length!=0)
      {
        for(int i=0;i<idComunicazione10R.length;i++)
        {
          parametri+="?";
          if (i<idComunicazione10R.length-1)
            parametri+=",";
        }
      }
      parametri+=") ";
      
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   SELECT   " 
              + "     ECA.CUAA AS CESCUAA,   "
              + "     ECA.DENOMINAZIONE AS CESDENOMINAZIONE,  "
              + "     TCE.DESCRIZIONE AS DESCRIZIONE, "
              + "     COM.DESCOM, "
              + "     PROV.SIGLA_PROVINCIA, "
              + "     AA.CUAA AS AZCUAA, "
              + "     AA.DENOMINAZIONE AS AZDENOMINAZIONE, "
              + "     TE.DESCRIZIONE AS DESC_TIPO_EFFLUENTE, "
              + "     SUM(ECA.QUANTITA) AS QUANTITA,  "
              + "     SUM(ECA.QUANTITA_AZOTO_DICHIARATO) AS QUANTITA_AZOTO_DICHIARATO  "
              + "   FROM   "
              + "     DB_EFFLUENTE_CES_ACQ_10R ECA, "
              + "     DB_TIPO_CAUSALE_EFFLUENTE TCE, "
              + "     DB_TIPO_EFFLUENTE TE, "
              + "     COMUNE COM, PROVINCIA PROV, "
              + "     COMUNE COMUTE, "
              + "     DB_COMUNICAZIONE_10R CR, "
              + "     DB_UTE UT, "
              + "     DB_ANAGRAFICA_AZIENDA AA "
              + "   WHERE  "
              + "     ECA.ID_COMUNICAZIONE_10R IN " +parametri 
              + " AND ECA.ISTAT_COMUNE = COM.ISTAT_COMUNE  "
              + " AND COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA "
              + " AND ECA.ID_CAUSALE_EFFLUENTE = TCE.ID_CAUSALE_EFFLUENTE "
              + " AND ECA.ID_EFFLUENTE = TE.ID_EFFLUENTE "
              + " AND ECA.ID_AZIENDA = AA.ID_AZIENDA (+) "
              + " AND CR.ID_COMUNICAZIONE_10R = ECA.ID_COMUNICAZIONE_10R "
              + " AND CR.ID_UTE = UT.ID_UTE "
              + " AND UT.COMUNE = COMUTE.ISTAT_COMUNE "
              + " AND AA.DATA_FINE_VALIDITA IS NULL "
              + " AND AA.DATA_CESSAZIONE IS NULL "
              + " GROUP BY ECA.CUAA, ECA.DENOMINAZIONE, TCE.DESCRIZIONE, COM.DESCOM, PROV.SIGLA_PROVINCIA, " 
              + " AA.CUAA, AA.DENOMINAZIONE , TE.DESCRIZIONE ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::getListEffluentiCessAcquPerStampa] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      if (idComunicazione10R!=null && idComunicazione10R.length!=0)
      {
        for(int i=0;i<idComunicazione10R.length;i++)
          stmt.setLong(++indice,idComunicazione10R[i]);
      }
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vEffluenteCessAcqu == null)
        {
          vEffluenteCessAcqu = new Vector<EffluenteCesAcqVO>();
        }
        EffluenteCesAcqVO effVO = new EffluenteCesAcqVO();
        
        if(Validator.isNotEmpty(rs.getString("AZCUAA")))
        {
          effVO.setCuaa(rs.getString("AZCUAA"));
        }
        else
        {
          effVO.setCuaa(rs.getString("CESCUAA"));
        }
        
        if(Validator.isNotEmpty(rs.getString("AZDENOMINAZIONE")))
        {
          effVO.setDenominazione(rs.getString("AZDENOMINAZIONE"));
        }
        else
        {
          effVO.setDenominazione(rs.getString("CESDENOMINAZIONE"));
        }
        
        effVO.setQuantita(rs.getBigDecimal("QUANTITA"));
        effVO.setQuantitaAzotoDichiarato(rs.getBigDecimal("QUANTITA_AZOTO_DICHIARATO"));
        effVO.setDescrizione(rs.getString("DESCRIZIONE"));
        effVO.setDescTipoEffluente(rs.getString("DESC_TIPO_EFFLUENTE"));
        effVO.setDescComune(rs.getString("DESCOM"));
        effVO.setSglProv(rs.getString("SIGLA_PROVINCIA"));
        vEffluenteCessAcqu.add(effVO);
      }
      return vEffluenteCessAcqu;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vEffluenteCessAcqu", vEffluenteCessAcqu) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idComunicazione10R", idComunicazione10R)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::getListEffluentiCessAcquPerStampa] ",
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
              "[Comunicazione10RGaaDAO::getListEffluentiCessAcquPerStampa] END.");
    }
  }
  
  
  /**
   * Ritorna un vettore di EffluenteStocExtVO associato ad una comunicazione
   * 
   * @param idComunicazione10R
   * @return
   * @throws DataAccessException
   */
  public Vector<EffluenteStocExtVO> getListStoccaggiExtrAziendali(long idComunicazione10R[]) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<EffluenteStocExtVO> vStoccExtraAz = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[Comunicazione10RGaaDAO::getListStoccaggiExtrAziendali] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      String parametri=" (";
      
      if (idComunicazione10R!=null && idComunicazione10R.length!=0)
      {
        for(int i=0;i<idComunicazione10R.length;i++)
        {
          parametri+="?";
          if (i<idComunicazione10R.length-1)
            parametri+=",";
        }
      }
      parametri+=") ";
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   SELECT   " 
              + "     ESE.ID_EFFLUENTE_STOC_EXT_10R, " 
              + "     ESE.ID_COMUNICAZIONE_10R, "
              + "     ESE.ID_AZIENDA,   "
              + "     ESE.CUAA,   "
              + "     ESE.ISTAT_COMUNE,   "
              + "     ESE.DENOMINAZIONE,  "
              + "     ESE.ID_TIPOLOGIA_FABBRICATO,  "
              + "     ESE.QUANTITA,  "
              + "     TTF.DESCRIZIONE AS DESCRIZIONE, "
              + "     COM.DESCOM, "
              + "     PROV.SIGLA_PROVINCIA, "
              + "     CR.ID_UTE, "
              + "     AA.CUAA AS AZCUAA, "
              + "     AA.DENOMINAZIONE AS AZDENOMINAZIONE "
              + "   FROM   "
              + "     DB_EFFLUENTE_STOC_EXT_10R ESE," 
              + "     COMUNE COM, PROVINCIA PROV, "
              + "     COMUNE COMUTE, "
              + "     DB_TIPO_TIPOLOGIA_FABBRICATO TTF, "
              + "     DB_COMUNICAZIONE_10R CR, "
              + "     DB_UTE UT, "
              + "     DB_ANAGRAFICA_AZIENDA AA "
              + "   WHERE  "
              + "     ESE.ID_COMUNICAZIONE_10R IN " +parametri
              + " AND ESE.ISTAT_COMUNE = COM.ISTAT_COMUNE  "
              + " AND COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA "
              + " AND ESE.ID_TIPOLOGIA_FABBRICATO = TTF.ID_TIPOLOGIA_FABBRICATO "
              + " AND CR.ID_COMUNICAZIONE_10R = ESE.ID_COMUNICAZIONE_10R "
              + " AND CR.ID_UTE = UT.ID_UTE "
              + " AND UT.COMUNE = COMUTE.ISTAT_COMUNE "
              + " AND ESE.ID_AZIENDA = AA.ID_AZIENDA (+) "
              + " AND AA.DATA_FINE_VALIDITA IS NULL "
              + " ORDER BY COMUTE.DESCOM,UT.INDIRIZZO,ESE.DENOMINAZIONE,TTF.DESCRIZIONE ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::getListStoccaggiExtrAziendali] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      if (idComunicazione10R!=null && idComunicazione10R.length!=0)
      {
        for(int i=0;i<idComunicazione10R.length;i++)
          stmt.setLong(++indice,idComunicazione10R[i]);
      }
      
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vStoccExtraAz == null)
        {
          vStoccExtraAz = new Vector<EffluenteStocExtVO>();
        }
        EffluenteStocExtVO effVO = new EffluenteStocExtVO();
        effVO.setIdEffluenteStocExt10R(rs.getLong("ID_EFFLUENTE_STOC_EXT_10R"));
        effVO.setIdComunicazione10R(rs.getLong("ID_COMUNICAZIONE_10R"));
        
        effVO.setIdAzienda(checkLongNull(rs.getString("ID_AZIENDA")));
        
        if(Validator.isNotEmpty(rs.getString("AZCUAA")))
        {
          effVO.setCuaa(rs.getString("AZCUAA"));
        }
        else
        {
          effVO.setCuaa(rs.getString("CUAA"));
        }
        
        effVO.setIstatComune(rs.getString("ISTAT_COMUNE"));
        
        if(Validator.isNotEmpty(rs.getString("AZDENOMINAZIONE")))
        {
          effVO.setDenominazione(rs.getString("AZDENOMINAZIONE"));
        }
        else
        {
          effVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        }
        
        effVO.setIdTipologiaFabbricato(rs.getLong("ID_TIPOLOGIA_FABBRICATO"));
        effVO.setQuantita(rs.getBigDecimal("QUANTITA"));
        effVO.setDescrizione(rs.getString("DESCRIZIONE"));
        effVO.setDescComune(rs.getString("DESCOM"));
        effVO.setSglProv(rs.getString("SIGLA_PROVINCIA"));
        effVO.setIdUte(rs.getLong("ID_UTE"));
        
        vStoccExtraAz.add(effVO);
      }
      return vStoccExtraAz;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vStoccExtraAz", vStoccExtraAz) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idComunicazione10R", idComunicazione10R)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::getListStoccaggiExtrAziendali] ",
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
              "[Comunicazione10RGaaDAO::getListStoccaggiExtrAziendali] END.");
    }
  }
  
  
  /**
   * Ritorna un vettore di EffluenteVO associato ad una comunicazione
   * 
   * @param idComunicazione10R
   * @return
   * @throws DataAccessException
   */
  public Vector<EffluenteVO> getListEffluenti(long idComunicazione10R[]) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<EffluenteVO> vEffluenti = null;
    EffluenteVO effVO = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[Comunicazione10RGaaDAO::getListEffluenti] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      String parametri=" (";
      
      if (idComunicazione10R!=null && idComunicazione10R.length!=0)
      {
        for(int i=0;i<idComunicazione10R.length;i++)
        {
          parametri+="?";
          if (i<idComunicazione10R.length-1)
            parametri+=",";
        }
      }
      parametri+=") ";
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT  EF.ID_EFFLUENTE_10R, " + 
        "        EF.ID_COMUNICAZIONE_10R, " + 
        "        EF.ID_EFFLUENTE, " + 
        "        TE.FLAG_PALABILE, " + 
        "        TE.DESCRIZIONE, " + 
        "        EF.VOLUME_INIZIALE, " + 
        "        EF.AZOTO_INIZIALE, " + 
        "        EF.VOLUME_POST_TRATTAMENTO,  " + 
        "        EF.AZOTO_POST_TRATTAMENTO,  " + 
        "        EF.VOLUME_POST_DICHIARATO,  " + 
        "        EF.AZOTO_POST_DICHIARATO, " + 
        "        EF.VOLUME_CESSIONE, " + 
        "        EF.AZOTO_CESSIONE, " + 
        "        EF.VOLUME_ACQUISIZIONE, " + 
        "        EF.AZOTO_ACQUISIZIONE, " + 
        "        EF.STOC_DISPONIBILE_GG, " + 
        "        EF.STOC_NECESSARIO_VOL, " + 
        "        EF.STOC_NECESSARIO_GG, "  + 
        "        EF.VOLUME_CESSIONE_STOCCATO, " + 
        "        EF.VOLUME_ACQUISIZIONE_STOCCATO, " + 
        "        EF.AZOTO_INIZIALE_DEC, " + 
        "        EF.AZOTO_POST_TRATTAMENTO_DEC, " + 
        "        EF.VOLUME_INIZIALE_CON, " + 
        "        EF.VOLUME_POST_TRATTAMENTO_CON, " + 
        "        EF.ID_TRATTAMENTO, " +
        "        NVL((SELECT EFF10.VOLUME_INIZIALE " +
        "        FROM   DB_EFFLUENTE_10R EFF10 " +
        "        WHERE  EFF10.ID_COMUNICAZIONE_10R = EF.ID_COMUNICAZIONE_10R " +
        "        AND    EFF10.ID_EFFLUENTE_ORIGINE = EF.ID_EFFLUENTE " +
        "        AND    ROWNUM = 1),0) AS VOLUME_TRATTATO, " +
        "        NVL((SELECT SUM(EFF_CES.QUANTITA)  " +  
        "             FROM   DB_EFFLUENTE_CES_ACQ_10R EFF_CES " + 
        "             WHERE  EFF_CES.ID_COMUNICAZIONE_10R = EF.ID_COMUNICAZIONE_10R " +
        "             AND    EFF_CES.ID_CAUSALE_EFFLUENTE = 1 " +
        "             AND    NVL(EFF_CES.FLAG_STOCCAGGIO,'N')  = 'N' " + 
        "             AND    EFF_CES.ID_EFFLUENTE = EF.ID_EFFLUENTE ),0) " +
        "        AS VOLUME_CESSIONE_NO_STOCCATO " +
        "FROM    DB_EFFLUENTE_10R EF, " + 
        "        DB_TIPO_EFFLUENTE TE " + 
        "WHERE   EF.ID_COMUNICAZIONE_10R IN  " +parametri + 
        "AND     EF.ID_EFFLUENTE = TE.ID_EFFLUENTE  " + 
        "ORDER BY TE.FLAG_PALABILE DESC, TE.DESCRIZIONE ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::getListEffluenti] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;

      if (idComunicazione10R!=null && idComunicazione10R.length!=0)
      {
        for(int i=0;i<idComunicazione10R.length;i++)
          stmt.setLong(++indice,idComunicazione10R[i]);
      }
      
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      long idEffluenteTmp = 0;
      BigDecimal volPostDichiarato = new BigDecimal(0);
      
      while (rs.next())
      {
        if(vEffluenti == null)
        {
          vEffluenti = new Vector<EffluenteVO>();
        }
        
        
        long idEffluente = rs.getLong("ID_EFFLUENTE");
        if(idEffluente != idEffluenteTmp)
        {
          if(idEffluenteTmp != 0)
          {
            effVO.setVolumePostDichiarato(volPostDichiarato);            
            vEffluenti.add(effVO);            
            volPostDichiarato = new BigDecimal(0);            
          }       
        
          effVO = new EffluenteVO();
          effVO.setIdEffluente10R(rs.getLong("ID_EFFLUENTE_10R"));
          effVO.setIdComunicazione10R(rs.getLong("ID_COMUNICAZIONE_10R"));
          effVO.setIdEffluente(rs.getLong("ID_EFFLUENTE"));
          
          String flagPalabile = rs.getString("FLAG_PALABILE");
          if(Validator.isNotEmpty(flagPalabile))
          {
            if(flagPalabile.equalsIgnoreCase("S"))
            {
              effVO.setTipoEffluente("Palabile");
            }
            else if(flagPalabile.equalsIgnoreCase("N"))
            {
              effVO.setTipoEffluente("Non palabile");
            }
          }
          effVO.setDescrizione(rs.getString("DESCRIZIONE"));
          effVO.setVolumeIniziale(rs.getBigDecimal("VOLUME_INIZIALE"));
          effVO.setAzotoIniziale(rs.getBigDecimal("AZOTO_INIZIALE"));
          effVO.setAzotoInizialeDec(rs.getBigDecimal("AZOTO_INIZIALE_DEC"));
          effVO.setVolumePostTrattamento(rs.getBigDecimal("VOLUME_POST_TRATTAMENTO"));
          //effVO.setVolumePostDichiarato(rs.getBigDecimal("VOLUME_POST_DICHIARATO"));
          
          //BigDecimal azTratt = rs.getBigDecimal("AZOTO_POST_TRATTAMENTO");
          effVO.setAzotoPostTrattamento(rs.getBigDecimal("AZOTO_POST_TRATTAMENTO"));
          effVO.setAzotoPostTrattamentoDec(rs.getBigDecimal("AZOTO_POST_TRATTAMENTO_DEC"));
          //effVO.setAzotoPostDichiarato(rs.getBigDecimal("AZOTO_POST_DICHIARATO"));
          
          effVO.setVolumeCessione(rs.getBigDecimal("VOLUME_CESSIONE"));
          effVO.setAzotoCessione(rs.getBigDecimal("AZOTO_CESSIONE"));
          effVO.setVolumeAcquisizione(rs.getBigDecimal("VOLUME_ACQUISIZIONE"));
          effVO.setAzotoAcquisizione(rs.getBigDecimal("AZOTO_ACQUISIZIONE"));
          effVO.setStocDispGg(rs.getBigDecimal("STOC_DISPONIBILE_GG"));
          effVO.setStocNecVol(rs.getBigDecimal("STOC_NECESSARIO_VOL"));
          effVO.setStocNecGg(rs.getBigDecimal("STOC_NECESSARIO_GG"));
          effVO.setVolumeCessioneStoccato(rs.getBigDecimal("VOLUME_CESSIONE_STOCCATO"));
          effVO.setVolumeAcquisizioneStoccato(rs.getBigDecimal("VOLUME_ACQUISIZIONE_STOCCATO"));
          effVO.setVolumeInizialeCon(rs.getBigDecimal("VOLUME_INIZIALE_CON"));
          effVO.setVolumePostTrattamentoCon(rs.getBigDecimal("VOLUME_POST_TRATTAMENTO_CON"));
          effVO.setIdTrattamento(checkLongNull(rs.getString("ID_TRATTAMENTO")));
          effVO.setVolumeTrattato(rs.getBigDecimal("VOLUME_TRATTATO"));
          effVO.setVolumeCessioneNoStoccato(rs.getBigDecimal("VOLUME_CESSIONE_NO_STOCCATO"));
          
          idEffluenteTmp = idEffluente;
          
        }
        
        if(Validator.isNotEmpty(rs.getBigDecimal("VOLUME_POST_DICHIARATO")))
          volPostDichiarato = volPostDichiarato.add(rs.getBigDecimal("VOLUME_POST_DICHIARATO"));
        
      }
      
      
      
      if(vEffluenti != null)
      {
        effVO.setVolumePostDichiarato(volPostDichiarato);                
        vEffluenti.add(effVO);         
      }
      
      
      
      
      
      return vEffluenti;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vEffluenti", vEffluenti) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idComunicazione10R", idComunicazione10R)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::getListEffluenti] ",
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
              "[Comunicazione10RGaaDAO::getListEffluenti] END.");
    }
  }
  
  
  /**
   * Ritorna un vettore di EffluenteVO associato ad una comunicazione
   * 
   * @param idComunicazione10R
   * @return
   * @throws DataAccessException
   */
  public Vector<EffluenteVO> getListEffluentiStampa(long idComunicazione10R[]) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<EffluenteVO> vEffluenti = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[Comunicazione10RGaaDAO::getListEffluentiStampa] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      String parametri=" (";
      
      if (idComunicazione10R!=null && idComunicazione10R.length!=0)
      {
        for(int i=0;i<idComunicazione10R.length;i++)
        {
          parametri+="?";
          if (i<idComunicazione10R.length-1)
            parametri+=",";
        }
      }
      parametri+=") ";
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   SELECT   " 
              + "     TE.FLAG_PALABILE, "
              + "     TE.DESCRIZIONE, "
              + "     SUM(EF.VOLUME_POST_TRATTAMENTO) AS VOLUME_POST_TRATTAMENTO,  "
              + "     SUM(EF.AZOTO_POST_TRATTAMENTO_DEC) AS AZOTO_POST_TRATTAMENTO_DEC,  "
              + "     SUM(EF.VOLUME_POST_DICHIARATO) AS VOLUME_POST_DICHIARATO,  "
              + "     SUM(EF.AZOTO_POST_DICHIARATO) AS AZOTO_POST_DICHIARATO "
              + "   FROM   "
              + "     DB_EFFLUENTE_10R EF," 
              + "     DB_TIPO_EFFLUENTE TE "
              + "   WHERE  "
              + "     EF.ID_COMUNICAZIONE_10R IN  " +parametri
              + " AND EF.ID_EFFLUENTE = TE.ID_EFFLUENTE  "
              + " GROUP BY TE.FLAG_PALABILE, TE.DESCRIZIONE ");

      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::getListEffluentiStampa] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;

      if (idComunicazione10R!=null && idComunicazione10R.length!=0)
      {
        for(int i=0;i<idComunicazione10R.length;i++)
          stmt.setLong(++indice,idComunicazione10R[i]);
      }
      
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vEffluenti == null)
        {
          vEffluenti = new Vector<EffluenteVO>();
        }
        EffluenteVO effVO = new EffluenteVO();
        
        String flagPalabile = rs.getString("FLAG_PALABILE");
        if(Validator.isNotEmpty(flagPalabile))
        {
          if(flagPalabile.equalsIgnoreCase("S"))
          {
            effVO.setTipoEffluente("Palabile");
          }
          else if(flagPalabile.equalsIgnoreCase("N"))
          {
            effVO.setTipoEffluente("Non palabile");
          }
        }
        effVO.setDescrizione(rs.getString("DESCRIZIONE"));
        BigDecimal volTratt = rs.getBigDecimal("VOLUME_POST_TRATTAMENTO");
        effVO.setVolumePostTrattamento(volTratt);
        
        BigDecimal volDich = rs.getBigDecimal("VOLUME_POST_DICHIARATO");
        if(Validator.isNotEmpty(volDich))
        {
          effVO.setVolumePostDichiarato(volDich);
        }
        else
        {
          effVO.setVolumePostDichiarato(volTratt);
        }
        
        BigDecimal azTrattDec = rs.getBigDecimal("AZOTO_POST_TRATTAMENTO_DEC");
        effVO.setAzotoPostTrattamentoDec(azTrattDec);        
        BigDecimal azDich = rs.getBigDecimal("AZOTO_POST_DICHIARATO");
        if(Validator.isNotEmpty(azDich))
        {
          effVO.setAzotoPostDichiarato(azDich);
        }
        else
        {
          effVO.setAzotoPostDichiarato(azTrattDec);
        }
        
        
        vEffluenti.add(effVO);
      }
      return vEffluenti;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vEffluenti", vEffluenti) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idComunicazione10R", idComunicazione10R)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::getListEffluentiStampa] ",
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
              "[Comunicazione10RGaaDAO::getListEffluentiStampa] END.");
    }
  }
  
  
  /**
   * Ritorna un vettore di AcquaExtraVO associato ad una comunicazione
   * 
   * @param idComunicazione10R
   * @return
   * @throws DataAccessException
   */
  public Vector<AcquaExtraVO> getListAcquaExtra(long idComunicazione10R[]) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<AcquaExtraVO> vAcquaExtra = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[Comunicazione10RGaaDAO::getListAcquaExtra] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      String parametri=" (";
      
      if (idComunicazione10R!=null && idComunicazione10R.length!=0)
      {
        for(int i=0;i<idComunicazione10R.length;i++)
        {
          parametri+="?";
          if (i<idComunicazione10R.length-1)
            parametri+=",";
        }
      }
      parametri+=") ";
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   SELECT   " 
              + "     AE.ID_ACQUA_EXTRA_10R, " 
              + "     AE.ID_COMUNICAZIONE_10R, "
              + "     AE.ID_ACQUA_AGRONOMICA, "
              + "     AE.VOLUME_REFLUO, "
              + "     TAA.DESCRIZIONE, "
              + "     CR.ID_UTE "
              + "   FROM   "
              + "     DB_ACQUA_EXTRA_10R AE," 
              + "     DB_TIPO_ACQUA_AGRONOMICA TAA, "
              + "     COMUNE COMUTE, "
              + "     DB_COMUNICAZIONE_10R CR, "
              + "     DB_UTE UT "
              + "   WHERE  "
              + "     AE.ID_COMUNICAZIONE_10R IN " +parametri 
              + " AND AE.ID_ACQUA_AGRONOMICA = TAA.ID_ACQUA_AGRONOMICA  "
              + " AND CR.ID_COMUNICAZIONE_10R = AE.ID_COMUNICAZIONE_10R "
              + " AND CR.ID_UTE = UT.ID_UTE "
              + " AND UT.COMUNE = COMUTE.ISTAT_COMUNE "
              + " ORDER BY COMUTE.DESCOM,UT.INDIRIZZO,TAA.DESCRIZIONE ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::getListAcquaExtra] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      if (idComunicazione10R!=null && idComunicazione10R.length!=0)
      {
        for(int i=0;i<idComunicazione10R.length;i++)
          stmt.setLong(++indice,idComunicazione10R[i]);
      }
      
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vAcquaExtra == null)
        {
          vAcquaExtra = new Vector<AcquaExtraVO>();
        }
        AcquaExtraVO acqVO = new AcquaExtraVO();
        acqVO.setIdAcquaExtra10R(rs.getLong("ID_ACQUA_EXTRA_10R"));
        acqVO.setIdComunicazione10R(rs.getLong("ID_COMUNICAZIONE_10R"));
        acqVO.setIdAcquaAgronomica(rs.getLong("ID_ACQUA_AGRONOMICA"));
        acqVO.setVolumeRefluo(rs.getBigDecimal("VOLUME_REFLUO"));
        acqVO.setDescrizione(rs.getString("DESCRIZIONE"));
        acqVO.setIdUte(rs.getLong("ID_UTE"));
        
        
        vAcquaExtra.add(acqVO);
      }
      return vAcquaExtra;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vAcquaExtra", vAcquaExtra) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idComunicazione10R", idComunicazione10R)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::getListAcquaExtra] ",
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
              "[Comunicazione10RGaaDAO::getListAcquaExtra] END.");
    }
  }
  
  /**
   * update della tabella DB_COMUNICAZIONE_10R
   * @param com10RVO
   * @throws DataAccessException
   */
  public void updateComunicazione10R(Comunicazione10RVO com10RVO) 
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
              "[Comunicazione10RGaaDAO::updateComunicazione10R] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   UPDATE DB_COMUNICAZIONE_10R   " 
              + "     SET ID_UTENTE_AGGIORNAMENTO = ? , "
              + "     DATA_INIZIO_VALIDITA = ? ,   "
              + "     DATA_FINE_VALIDITA = ? , "
              + "     DATA_AGGIORNAMENTO = ?, "
              + "     DATA_RICALCOLO = ? , "
              + "     VOLUME_SOTTOGRIGLIATO = ? , "
              + "     VOLUME_REFLUO_AZIENDA = ? ,  "
              + "     SUPERFICIE_CONDUZIONE_ZVN = ? ,  "
              + "     SUPERFICIE_ASSERVIMENTO_ZVN = ?, "
              + "     SUPERFICIE_CONDUZIONE_NO_ZVN = ? ,  "
              + "     SUPERFICIE_ASSERVIMENTO_NO_ZVN = ?, "
              + "     AZOTO_CONDUZIONE_ZVN = ? , "
              + "     AZOTO_ASSERVIMENTO_ZVN = ? , "
              + "     AZOTO_CONDUZIONE_NO_ZVN = ? , "
              + "     AZOTO_ASSERVIMENTO_NO_ZVN = ? , "
              + "     TOTALE_AZOTO_AZIENDALE = ? , "
              + "     STOC_NETTO_PALABILE = ? , "
              + "     STOC_NETTO_NONPALABILE = ? , "
              + "     STOC_DISP_PALABILE_VOL = ? , "
              + "     STOC_DISP_NONPALABILE_VOL = ? , "
              + "     VOLUME_PIOGGE = ? , "
              + "     ACQUE_LAVAGGIO = ? , "
              + "     STOC_ACQUE_NETTO_CESSIONE = ? ,  "
              + "     STOC_ACQUE_NEC_GG = ? ,  "
              + "     STOC_ACQUE_NEC_VOL = ? ,  "
              + "     NOTE = ? , "
              + "     ID_UTE = ? , "
              + "     TOTALE_AZOTO = ?, "
              + "     SUPERFICIE_SAU_PIEMONTE = ?, "
              + "     SUPERFICIE_SAU_PIEMONTE_ZVN = ?, "
              + "     ADESIONE_DEROGA = ?, "
              + "     AZOTO_ESCRETO_PASCOLO = ? "
              + "   WHERE  "
              + "     ID_COMUNICAZIONE_10R = ?  ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::updateComunicazione10R] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice,com10RVO.getIdUtenteAggiornamento());
      stmt.setTimestamp(++indice, convertDateToTimestamp(com10RVO.getDataInizioValidita()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(com10RVO.getDataFineValidita()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(com10RVO.getDataAggiornamento()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(com10RVO.getDataRicalcolo()));
      stmt.setBigDecimal(++indice, com10RVO.getVolumeSottogrigliato());
      stmt.setBigDecimal(++indice, com10RVO.getVolumeRefluoAzienda());
      stmt.setBigDecimal(++indice, com10RVO.getSuperficieConduzioneZVN());
      stmt.setBigDecimal(++indice, com10RVO.getSuperficieAsservimentoZVN());
      stmt.setBigDecimal(++indice, com10RVO.getSuperficieConduzioneNoZVN());
      stmt.setBigDecimal(++indice, com10RVO.getSuperficieAsservimentoNoZVN());
      stmt.setBigDecimal(++indice, com10RVO.getAzotoConduzioneZVN());
      stmt.setBigDecimal(++indice, com10RVO.getAzotoAsservimentoZVN());
      stmt.setBigDecimal(++indice, com10RVO.getAzotoConduzioneNoZVN());
      stmt.setBigDecimal(++indice, com10RVO.getAzotoAsservimentoNoZVN());
      stmt.setBigDecimal(++indice, com10RVO.getTotaleAzotoAziendale());
      stmt.setBigDecimal(++indice, com10RVO.getStocNettoPalabile());
      stmt.setBigDecimal(++indice, com10RVO.getStocNettoNonPalabile());
      stmt.setBigDecimal(++indice, com10RVO.getStocDispPalabileVol());
      stmt.setBigDecimal(++indice, com10RVO.getStocDispNonPalabileVol());
      stmt.setBigDecimal(++indice, com10RVO.getVolumePiogge());
      stmt.setBigDecimal(++indice, com10RVO.getAcqueLavaggio());
      stmt.setBigDecimal(++indice, com10RVO.getStocAcqueNettoCessione());
      stmt.setBigDecimal(++indice, com10RVO.getStocAcqueNecGG());
      stmt.setBigDecimal(++indice, com10RVO.getStocAcqueNecVol());
      stmt.setString(++indice, com10RVO.getNote());
      stmt.setLong(++indice,com10RVO.getIdUte());
      stmt.setBigDecimal(++indice, com10RVO.getTotaleAzoto());
      stmt.setBigDecimal(++indice, com10RVO.getSuperficieSauPiemonte());
      stmt.setBigDecimal(++indice, com10RVO.getSuperficieSauPiemonteZVN());
      stmt.setString(++indice, com10RVO.getAdesioneDeroga());
      stmt.setBigDecimal(++indice, com10RVO.getAzotoEscretoPascolo());
      stmt.setLong(++indice, com10RVO.getIdComunicazione10R());
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("com10RVO", com10RVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::updateComunicazione10R] ",
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
              "[Comunicazione10RGaaDAO::updateComunicazione10R] END.");
    }
  }
  
  /**
   * Storicizza settando solo la data fina validita
   * 
   * 
   * @param idComunicazione10r
   * @throws DataAccessException
   */
  public void storicizzaComunicazione10R(long idComunicazione10r) 
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
              "[Comunicazione10RGaaDAO::storicizzaComunicazione10R] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   UPDATE DB_COMUNICAZIONE_10R   " 
              + "     SET  DATA_FINE_VALIDITA = ? "
              + "   WHERE  "
              + "     ID_COMUNICAZIONE_10R = ?  ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::updateComunicazione10R] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setTimestamp(++indice, convertDateToTimestamp(new Date()));
      stmt.setLong(++indice, idComunicazione10r);
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idComunicazione10r", idComunicazione10r)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::storicizzaComunicazione10R] ",
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
              "[Comunicazione10RGaaDAO::storicizzaComunicazione10R] END.");
    }
  }
  
  
  /**
   * insert della tabella DB_COMUNICAZIONE_10R
   * @param com10RVO
   * @throws DataAccessException
   */
  public Long insertComunicazione10R(Comunicazione10RVO com10RVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idComunicazione10R = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[Comunicazione10RGaaDAO::insertComunicazione10R] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idComunicazione10R = getNextPrimaryKey(SolmrConstants.SEQ_DB_COMUNICAZIONE_10R);
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_COMUNICAZIONE_10R   " 
              + "     (ID_COMUNICAZIONE_10R, "
              + "     ID_UTENTE_AGGIORNAMENTO , "
              + "     DATA_INIZIO_VALIDITA ,   "
              + "     DATA_FINE_VALIDITA , "
              + "     DATA_AGGIORNAMENTO , "
              + "     DATA_RICALCOLO , "
              + "     VOLUME_SOTTOGRIGLIATO , "
              + "     VOLUME_REFLUO_AZIENDA ,  "
              + "     SUPERFICIE_CONDUZIONE_ZVN ,  "
              + "     SUPERFICIE_ASSERVIMENTO_ZVN , "
              + "     SUPERFICIE_CONDUZIONE_NO_ZVN ,  "
              + "     SUPERFICIE_ASSERVIMENTO_NO_ZVN , "
              + "     AZOTO_CONDUZIONE_ZVN , "
              + "     AZOTO_ASSERVIMENTO_ZVN , "
              + "     AZOTO_CONDUZIONE_NO_ZVN , "
              + "     AZOTO_ASSERVIMENTO_NO_ZVN , "
              + "     TOTALE_AZOTO_AZIENDALE , "
              + "     STOC_NETTO_PALABILE , "
              + "     STOC_NETTO_NONPALABILE , "
              + "     STOC_DISP_PALABILE_VOL , "
              + "     STOC_DISP_NONPALABILE_VOL , "
              + "     VOLUME_PIOGGE , "
              + "     ACQUE_LAVAGGIO , "
              + "     STOC_ACQUE_NETTO_CESSIONE,  "
              + "     STOC_ACQUE_NEC_GG,  "
              + "     STOC_ACQUE_NEC_VOL,  "
              + "     NOTE, " 
              + "     ID_UTE, "
              + "     TOTALE_AZOTO, "
              +	"     SUPERFICIE_SAU_PIEMONTE, " 
              + "     SUPERFICIE_SAU_PIEMONTE_ZVN, "
              +	"	    ADESIONE_DEROGA, " 
              + "     AZOTO_ESCRETO_PASCOLO)"
              + "   VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::insertComunicazione10R] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idComunicazione10R.longValue());
      stmt.setLong(++indice,com10RVO.getIdUtenteAggiornamento());
      stmt.setTimestamp(++indice, convertDateToTimestamp(com10RVO.getDataInizioValidita()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(com10RVO.getDataFineValidita()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(com10RVO.getDataAggiornamento()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(com10RVO.getDataRicalcolo()));
      stmt.setBigDecimal(++indice, com10RVO.getVolumeSottogrigliato());
      stmt.setBigDecimal(++indice, com10RVO.getVolumeRefluoAzienda());
      stmt.setBigDecimal(++indice, com10RVO.getSuperficieConduzioneZVN());
      stmt.setBigDecimal(++indice, com10RVO.getSuperficieAsservimentoZVN());
      stmt.setBigDecimal(++indice, com10RVO.getSuperficieConduzioneNoZVN());
      stmt.setBigDecimal(++indice, com10RVO.getSuperficieAsservimentoNoZVN());
      stmt.setBigDecimal(++indice, com10RVO.getAzotoConduzioneZVN());
      stmt.setBigDecimal(++indice, com10RVO.getAzotoAsservimentoZVN());
      stmt.setBigDecimal(++indice, com10RVO.getAzotoConduzioneNoZVN());
      stmt.setBigDecimal(++indice, com10RVO.getAzotoAsservimentoNoZVN());
      stmt.setBigDecimal(++indice, com10RVO.getTotaleAzotoAziendale());
      stmt.setBigDecimal(++indice, com10RVO.getStocNettoPalabile());
      stmt.setBigDecimal(++indice, com10RVO.getStocNettoNonPalabile());
      stmt.setBigDecimal(++indice, com10RVO.getStocDispPalabileVol());
      stmt.setBigDecimal(++indice, com10RVO.getStocDispNonPalabileVol());
      stmt.setBigDecimal(++indice, com10RVO.getVolumePiogge());
      stmt.setBigDecimal(++indice, com10RVO.getAcqueLavaggio());
      stmt.setBigDecimal(++indice, com10RVO.getStocAcqueNettoCessione());
      stmt.setBigDecimal(++indice, com10RVO.getStocAcqueNecGG());
      stmt.setBigDecimal(++indice, com10RVO.getStocAcqueNecVol());
      stmt.setString(++indice, com10RVO.getNote());
      stmt.setLong(++indice,com10RVO.getIdUte());
      stmt.setBigDecimal(++indice, com10RVO.getTotaleAzoto());
      stmt.setBigDecimal(++indice, com10RVO.getSuperficieSauPiemonte());
      stmt.setBigDecimal(++indice, com10RVO.getSuperficieSauPiemonteZVN());
      stmt.setString(++indice, com10RVO.getAdesioneDeroga());
      stmt.setBigDecimal(++indice, com10RVO.getAzotoEscretoPascolo());
      
      stmt.executeUpdate();
      
      return idComunicazione10R;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idComunicazione10R", idComunicazione10R)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("com10RVO", com10RVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::insertComunicazione10R] ",
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
              "[Comunicazione10RGaaDAO::insertComunicazione10R] END.");
    }
  }
  
  /**
   * insert nella tabella DB_EFFLUENTE_10R
   * 
   * @param eff10RVO
   * @return
   * @throws DataAccessException
   */
  public Long insertEffluente10R(EffluenteVO eff10RVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idEffluente10R = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[Comunicazione10RGaaDAO::insertEffluente10R] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idEffluente10R = getNextPrimaryKey(SolmrConstants.SEQ_DB_EFFLUENTE_10R);
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_EFFLUENTE_10R   " 
              + "     (ID_EFFLUENTE_10R, "
              + "     ID_COMUNICAZIONE_10R , " 
              + "     ID_EFFLUENTE , "
              + "     VOLUME_INIZIALE ,   "
              + "     AZOTO_INIZIALE , "
              + "     VOLUME_POST_TRATTAMENTO , "
              + "     VOLUME_POST_DICHIARATO , "
              + "     AZOTO_POST_TRATTAMENTO , "
              + "     AZOTO_POST_DICHIARATO ,  "
              + "     VOLUME_CESSIONE ,  "
              + "     AZOTO_CESSIONE ,  "
              + "     VOLUME_ACQUISIZIONE , "
              + "     AZOTO_ACQUISIZIONE ,  "
              + "     STOC_DISPONIBILE_GG ,  "
              + "     STOC_NECESSARIO_VOL ,  "
              + "     STOC_NECESSARIO_GG ,  "
              + "     VOLUME_CESSIONE_STOCCATO, "
              + "     VOLUME_ACQUISIZIONE_STOCCATO, "
              +	"     VOLUME_INIZIALE_CON, "
              +	"     AZOTO_INIZIALE_DEC, "
              +	"     VOLUME_POST_TRATTAMENTO_CON, "
              +	"     AZOTO_POST_TRATTAMENTO_DEC, "
              + "     ID_EFFLUENTE_ORIGINE, "
              + "     ID_TRATTAMENTO) "
              + "   VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::insertEffluente10R] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idEffluente10R.longValue());
      stmt.setLong(++indice,eff10RVO.getIdComunicazione10R());
      stmt.setLong(++indice,eff10RVO.getIdEffluente());
      stmt.setBigDecimal(++indice, eff10RVO.getVolumeIniziale());
      stmt.setBigDecimal(++indice, eff10RVO.getAzotoIniziale());
      if(Validator.isNotEmpty(eff10RVO.getVolumePostTrattamento()))
        stmt.setBigDecimal(++indice, eff10RVO.getVolumePostTrattamento());
      else
        stmt.setBigDecimal(++indice, new BigDecimal(0));
      stmt.setBigDecimal(++indice, eff10RVO.getVolumePostDichiarato());
      if(Validator.isNotEmpty(eff10RVO.getAzotoPostTrattamento()))
        stmt.setBigDecimal(++indice, eff10RVO.getAzotoPostTrattamento());
      else
        stmt.setBigDecimal(++indice, new BigDecimal(0));
      stmt.setBigDecimal(++indice, eff10RVO.getAzotoPostDichiarato());
      if(Validator.isNotEmpty(eff10RVO.getVolumeCessione()))
        stmt.setBigDecimal(++indice, eff10RVO.getVolumeCessione() );
      else
        stmt.setBigDecimal(++indice, new BigDecimal(0));
      if(Validator.isNotEmpty(eff10RVO.getAzotoCessione()))
        stmt.setBigDecimal(++indice, eff10RVO.getAzotoCessione() );
      else
        stmt.setBigDecimal(++indice, new BigDecimal(0));
      if(Validator.isNotEmpty(eff10RVO.getVolumeAcquisizione()))
        stmt.setBigDecimal(++indice, eff10RVO.getVolumeAcquisizione());
      else
        stmt.setBigDecimal(++indice, new BigDecimal(0));
      if(Validator.isNotEmpty(eff10RVO.getAzotoAcquisizione()))
        stmt.setBigDecimal(++indice, eff10RVO.getAzotoAcquisizione());
      else
        stmt.setBigDecimal(++indice, new BigDecimal(0));
      if(Validator.isNotEmpty(eff10RVO.getStocDispGg()))
        stmt.setBigDecimal(++indice, eff10RVO.getStocDispGg());
      else
        stmt.setBigDecimal(++indice, new BigDecimal(0));
      if(Validator.isNotEmpty(eff10RVO.getStocNecVol()))
        stmt.setBigDecimal(++indice, eff10RVO.getStocNecVol());
      else
        stmt.setBigDecimal(++indice, new BigDecimal(0));
      if(Validator.isNotEmpty(eff10RVO.getStocNecGg()))
        stmt.setBigDecimal(++indice, eff10RVO.getStocNecGg());
      else
        stmt.setBigDecimal(++indice, new BigDecimal(0));
      
      stmt.setBigDecimal(++indice, eff10RVO.getVolumeCessioneStoccato());
      stmt.setBigDecimal(++indice, eff10RVO.getVolumeAcquisizioneStoccato());
      stmt.setBigDecimal(++indice, eff10RVO.getVolumeInizialeCon());
      stmt.setBigDecimal(++indice, eff10RVO.getAzotoInizialeDec());
      stmt.setBigDecimal(++indice, eff10RVO.getVolumePostTrattamentoCon());
      stmt.setBigDecimal(++indice, eff10RVO.getAzotoPostTrattamentoDec());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(eff10RVO.getIdEffluenteOrigine()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(eff10RVO.getIdTrattamento()));
      
  
      stmt.executeUpdate();
      
      return idEffluente10R;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idEffluente10R", idEffluente10R)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("eff10RVO", eff10RVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::insertEffluente10R] ",
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
              "[Comunicazione10RGaaDAO::insertEffluente10R] END.");
    }
  }
  
  /**
   * insert nella tabella DB_EFFLUENTE_CES_ACQ_10R
   * 
   * @param effCesAcq10RVO
   * @return
   * @throws DataAccessException
   */
  public Long insertEffluenteCesAcq10R(EffluenteCesAcqVO effCesAcq10RVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idEffluenteCesAcq10R = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[Comunicazione10RGaaDAO::insertEffluenteCesAcq10R] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idEffluenteCesAcq10R = getNextPrimaryKey(SolmrConstants.SEQ_DB_EFFLUENTE_CES_ACQ_10R);
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_EFFLUENTE_CES_ACQ_10R   " 
              + "     (ID_EFFLUENTE_CES_ACQ_10R, "
              + "     ID_COMUNICAZIONE_10R , " 
              + "     ID_AZIENDA , "
              + "     CUAA ,   "
              + "     ISTAT_COMUNE , "
              + "     DENOMINAZIONE , "
              + "     ID_EFFLUENTE , "
              + "     ID_CAUSALE_EFFLUENTE , "
              + "     QUANTITA ,  "
              + "     QUANTITA_AZOTO ,   "
              + "     QUANTITA_AZOTO_DICHIARATO ,   "
              + "     FLAG_STOCCAGGIO )  "
              + "   VALUES(?,?,?,?,?,?,?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::insertEffluente10R] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idEffluenteCesAcq10R.longValue());
      stmt.setLong(++indice,effCesAcq10RVO.getIdComunicazione10R());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(effCesAcq10RVO.getIdAzienda()));
      stmt.setString(++indice, effCesAcq10RVO.getCuaa());
      stmt.setString(++indice, effCesAcq10RVO.getIstatComune());
      stmt.setString(++indice, effCesAcq10RVO.getDenominazione() );
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(effCesAcq10RVO.getIdEffluente()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(effCesAcq10RVO.getIdCausaleEffluente()));
      stmt.setBigDecimal(++indice, effCesAcq10RVO.getQuantita());
      stmt.setBigDecimal(++indice, effCesAcq10RVO.getQuantitaAzoto());
      stmt.setBigDecimal(++indice, effCesAcq10RVO.getQuantitaAzotoDichiarato());
      stmt.setString(++indice, effCesAcq10RVO.getFlagStoccaggio());
      
      
  
      stmt.executeUpdate();
      
      return idEffluenteCesAcq10R;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idEffluenteCesAcq10R", idEffluenteCesAcq10R)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("effCesAcq10RVO", effCesAcq10RVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::insertEffluenteCesAcq10R] ",
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
              "[Comunicazione10RGaaDAO::insertEffluenteCesAcq10R] END.");
    }
  }
  
  
  /**
   * insert nella tabella DB_EFFLUENTE_STOC_EXT_10R
   * 
   * @param effStocExt10RVO
   * @return
   * @throws DataAccessException
   */
  public Long insertEffluenteStocExtAcq10R(EffluenteStocExtVO effStocExt10RVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idEffluenteStocExt10R = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[Comunicazione10RGaaDAO::insertEffluenteStocExtAcq10R] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idEffluenteStocExt10R = getNextPrimaryKey(SolmrConstants.SEQ_DB_EFFLUENTE_STOC_EXT_10R);
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_EFFLUENTE_STOC_EXT_10R   " 
              + "     (ID_EFFLUENTE_STOC_EXT_10R, "
              + "     ID_COMUNICAZIONE_10R , " 
              + "     ID_AZIENDA , "
              + "     CUAA ,   "
              + "     DENOMINAZIONE , "
              + "     ISTAT_COMUNE , "
              + "     ID_TIPOLOGIA_FABBRICATO , "
              + "     QUANTITA) "
              + "   VALUES(?,?,?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::insertEffluenteStocExt10R] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idEffluenteStocExt10R.longValue());
      stmt.setLong(++indice, effStocExt10RVO.getIdComunicazione10R());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(effStocExt10RVO.getIdAzienda()));
      stmt.setString(++indice, effStocExt10RVO.getCuaa());
      stmt.setString(++indice, effStocExt10RVO.getDenominazione());
      stmt.setString(++indice, effStocExt10RVO.getIstatComune());
      stmt.setLong(++indice, effStocExt10RVO.getIdTipologiaFabbricato());
      stmt.setBigDecimal(++indice, effStocExt10RVO.getQuantita());
      
      
  
      stmt.executeUpdate();
      
      return idEffluenteStocExt10R;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idEffluenteStocExt10R", idEffluenteStocExt10R)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("effStocExt10RVO", effStocExt10RVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::insertEffluenteStocAcq10R] ",
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
              "[Comunicazione10RGaaDAO::insertEffluenteStocAcq10R] END.");
    }
  }
  
  /**
   * insert nella tabella DB_ACQUA_EXTRA_10R
   * 
   * @param acquExtr10RVO
   * @return
   * @throws DataAccessException
   */
  public Long insertAcquaExtra10R(AcquaExtraVO acquExtr10RVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idAcquaExtra10R = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[Comunicazione10RGaaDAO::insertAcquaExtra10R] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idAcquaExtra10R = getNextPrimaryKey(SolmrConstants.SEQ_DB_ACQUA_EXTRA_10R);
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_ACQUA_EXTRA_10R   " 
              + "     (ID_ACQUA_EXTRA_10R, "
              + "     ID_COMUNICAZIONE_10R, " 
              + "     ID_ACQUA_AGRONOMICA, "
              + "     VOLUME_REFLUO )  "
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
            "[Comunicazione10RGaaDAO::insertAcquaExtra10R] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAcquaExtra10R.longValue());
      stmt.setLong(++indice,acquExtr10RVO.getIdComunicazione10R());
      stmt.setLong(++indice,acquExtr10RVO.getIdAcquaAgronomica());
      stmt.setBigDecimal(++indice, acquExtr10RVO.getVolumeRefluo());     
      
  
      stmt.executeUpdate();
      
      return idAcquaExtra10R;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idAcquaExtra10R", idAcquaExtra10R)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("acquExtr10RVO", acquExtr10RVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::insertAcquaExtra10R] ",
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
              "[Comunicazione10RGaaDAO::insertAcquaExtra10R] END.");
    }
  }
  
  
  /**
   * delete nella tabella DB_EFFLUENTE_10R
   * 
   * @param idEffluente10R
   * @return
   * @throws DataAccessException
   */
  public void deleteEffluente10R(long idEffluente10R) 
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
              "[Comunicazione10RGaaDAO::deleteEffluente10R] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   DELETE DB_EFFLUENTE_10R WHERE ID_EFFLUENTE_10R = ?  " );
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::deleteEffluente10R] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idEffluente10R);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idEffluente10R", idEffluente10R)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::deleteEffluente10R] ",
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
              "[Comunicazione10RGaaDAO::deleteEffluente10R] END.");
    }
  }
  
  
  /**
   * delete nella tabella DB_EFFLUENTE_CES_ACQ_10R
   * 
   * @param idEffluenteCesAcq10R
   * @return
   * @throws DataAccessException
   */
  public void deleteEffluenteCesAcq10R(long idEffluenteCesAcq10R) 
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
              "[Comunicazione10RGaaDAO::deleteEffluenteCesAcq10R] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   DELETE DB_EFFLUENTE_CES_ACQ_10R WHERE ID_EFFLUENTE_CES_ACQ_10R = ?  " );
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::deleteEffluenteCesAcq10R] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idEffluenteCesAcq10R);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idEffluenteCesAcq10R", idEffluenteCesAcq10R)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::deleteEffluenteCesAcq10R] ",
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
              "[Comunicazione10RGaaDAO::deleteEffluenteCesAcq10R] END.");
    }
  }
  
  
  /**
   * delete nella tabella DB_EFFLUENTE_STOC_EXT_10R
   * 
   * @param idEffluenteStocExt10R
   * @return
   * @throws DataAccessException
   */
  public void deleteEffluenteStocExt10R(long idEffluenteStocExt10R) 
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
              "[Comunicazione10RGaaDAO::deleteEffluenteStocExt10R] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   DELETE DB_EFFLUENTE_STOC_EXT_10R WHERE ID_EFFLUENTE_STOC_EXT_10R = ?  " );
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::deleteEffluenteStocExt10R] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idEffluenteStocExt10R);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idEffluenteStocExt10R", idEffluenteStocExt10R)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::deleteEffluenteStocExt10R] ",
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
              "[Comunicazione10RGaaDAO::deleteEffluenteStocExt10R] END.");
    }
  }
  
  /**
   * delete nella tabella DB_ACQUA_EXTRA_10R
   * 
   * @param idAcquaExtra10R
   * @return
   * @throws DataAccessException
   */
  public void deleteAcquaExtra10R(long idAcquaExtra10R) 
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
              "[Comunicazione10RGaaDAO::deleteAcquaExtra10R] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   DELETE DB_ACQUA_EXTRA_10R WHERE ID_ACQUA_EXTRA_10R = ?  " );
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::deleteAcquaExtra10R] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAcquaExtra10R);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAcquaExtra10R", idAcquaExtra10R)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::deleteAcquaExtra10R] ",
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
              "[Comunicazione10RGaaDAO::deleteAcquaExtra10R] END.");
    }
  }
  
  
  /**
   * Indica se l'azienda in questione possiede l'effluente 
   * 
   * @return
   * @throws DataAccessException
   */
  public boolean hasEffluenteProdotto(long idEffluente, long idUte) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean trovato = false;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[Comunicazione10RGaaDAO::hasEffluenteProdotto] BEGIN.");

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   SELECT   " 
              + "     DU.ID_UTE "
              + "   FROM   "
              + "     DB_EFFLUENTE_PRODOTTO EP, " 
              + "     DB_STABULAZIONE_TRATTAMENTO ST, "
              + "     DB_SOTTOCATEGORIA_ALLEVAMENTO SA, "
              + "     DB_CATEGORIE_ALLEVAMENTO CA, "
              + "     DB_ALLEVAMENTO AL, "
              + "     DB_UTE DU "
              + "   WHERE  "
              + "   DU.ID_UTE = ? "
              + " AND EP.ID_EFFLUENTE = ? "
              + " AND EP.ID_STABULAZIONE_TRATTAMENTO = ST.ID_STABULAZIONE_TRATTAMENTO  "
              + " AND ST.ID_SOTTOCATEGORIA_ALLEVAMENTO = SA.ID_SOTTOCATEGORIA_ALLEVAMENTO "
              + " AND SA.ID_CATEGORIE_ALLEVAMENTO = CA.ID_CATEGORIE_ALLEVAMENTO "
              + " AND CA.ID_ALLEVAMENTO = AL.ID_ALLEVAMENTO "
              + " AND AL.DATA_FINE IS NULL "
              + " AND DU.DATA_FINE_ATTIVITA IS NULL "          
          );
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::hasEffluenteProdotto] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idUte);
      stmt.setLong(++indice, idEffluente);
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        trovato = true;
      }
      
      return trovato;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idEffluente", idEffluente),
        new Parametro("idUte", idUte)  };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::hasEffluenteProdotto] ",
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
              "[Comunicazione10RGaaDAO::hasEffluenteProdotto] END.");
    }
  }
  
  /**
   * Ottiene un record della tabella DB_COMUNICAZIONE_10R relativo all'azienda e alla
   * dichiarazione di consistenza
   * 
   * @param idAzienda
   * @param idPianoRiferimento
   * @return
   * @throws DataAccessException
   */
  public Comunicazione10RVO[] getComunicazione10RByPianoRifererimento(long idAzienda, long idPianoRiferimento) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<Comunicazione10RVO> result=new Vector<Comunicazione10RVO>();
    
    try
    {
      SolmrLogger
          .debug(this,
              "[Comunicazione10RGaaDAO::getComunicazione10RByPianoRifererimento] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   SELECT   " 
              + "     COM.ID_UTENTE_AGGIORNAMENTO, "
              + "     COM.DATA_AGGIORNAMENTO, "
              + "     COM.ID_COMUNICAZIONE_10R, "
              + "     COM.DATA_RICALCOLO,   "
              + "     COM.VOLUME_SOTTOGRIGLIATO,   "
              + "     COM.VOLUME_REFLUO_AZIENDA,  "
              + "     COM.SUPERFICIE_CONDUZIONE_ZVN ,  "
              + "     COM.SUPERFICIE_ASSERVIMENTO_ZVN , "
              + "     COM.SUPERFICIE_CONDUZIONE_NO_ZVN ,  "
              + "     COM.SUPERFICIE_ASSERVIMENTO_NO_ZVN , "
              + "     COM.AZOTO_CONDUZIONE_ZVN , "
              + "     COM.AZOTO_ASSERVIMENTO_ZVN , "
              + "     COM.AZOTO_CONDUZIONE_NO_ZVN , "
              + "     COM.AZOTO_ASSERVIMENTO_NO_ZVN , "
              + "     COM.TOTALE_AZOTO_AZIENDALE , "
              + "     COM.STOC_NETTO_PALABILE , "
              + "     COM.STOC_NETTO_NONPALABILE , "
              + "     COM.STOC_DISP_PALABILE_VOL , "
              + "     COM.STOC_DISP_NONPALABILE_VOL , "
              + "     COM.VOLUME_PIOGGE , "
              + "     COM.ACQUE_LAVAGGIO , "
              + "     COM.STOC_ACQUE_NETTO_CESSIONE,  "
              + "     COM.STOC_ACQUE_NEC_GG,  "
              + "     COM.STOC_ACQUE_NEC_VOL,  "
              + "     COM.NOTE, "
              + "     COM.ID_UTE, "
              + "     COM.TOTALE_AZOTO, "
              + "     COM.SUPERFICIE_SAU_PIEMONTE, "
              + "     COM.SUPERFICIE_SAU_PIEMONTE_ZVN, "
              + "     COM.ADESIONE_DEROGA, "
              + "     COM.AZOTO_ESCRETO_PASCOLO, "
              + "     NVL(PVU.COGNOME_UTENTE, TRIM(UPPER(PVU.COGNOME_UTENTE_LOGIN)))||' '||NVL(PVU.NOME_UTENTE, TRIM(UPPER(PVU.NOME_UTENTE_LOGIN))) AS UT_DENOMINAZIONE, "
              +	"     PVU.DENOMINAZIONE AS UT_ENTE_APPART "
              + "   FROM   "
              + "     DB_COMUNICAZIONE_10R COM,   "
              + "     PAPUA_V_UTENTE_LOGIN PVU, "
              + "     COMUNE C, "
              +	"     DB_UTE U ");
      
      if(idPianoRiferimento > 0) 
      {
        queryBuf
          .append(""
              + "    ,DB_DICHIARAZIONE_CONSISTENZA DC ");
      }
      
      queryBuf
        .append(""
              +	"  WHERE  "
              + "     U.ID_AZIENDA = ?  "
              + " AND COM.ID_UTE = U.ID_UTE "
              + " AND U.COMUNE = C.ISTAT_COMUNE "
              + " AND COM.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN ");
              
              
              
      if(idPianoRiferimento > 0) {
        queryBuf
        .append(""
              + " AND  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
               "  AND  COM.DATA_INIZIO_VALIDITA < DC.DATA_INSERIMENTO_DICHIARAZIONE " +
               "  AND  NVL(COM.DATA_FINE_VALIDITA, ?) > DC.DATA_INSERIMENTO_DICHIARAZIONE " +
               "  AND  U.DATA_INIZIO_ATTIVITA < DC.DATA_INSERIMENTO_DICHIARAZIONE " +
               "  AND  NVL(U.DATA_FINE_ATTIVITA, ?) > DC.DATA_INSERIMENTO_DICHIARAZIONE ");
      }
      else 
      {
        queryBuf
          .append(""
              + " AND  COM.DATA_FINE_VALIDITA IS NULL "
              + " AND  U.DATA_FINE_ATTIVITA IS NULL ");
      }
      
      queryBuf.append("ORDER BY U.ID_UTE,C.DESCOM, U.INDIRIZZO ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::getComunicazione10RByPianoRifererimento] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice,idAzienda);
      
      if(idPianoRiferimento > 0) {
        stmt.setLong(++indice, idPianoRiferimento);
        stmt.setDate(++indice, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
        stmt.setDate(++indice, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
      }  
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        Comunicazione10RVO com10RVO = new Comunicazione10RVO();
        com10RVO.setIdUtenteAggiornamento(rs.getLong("ID_UTENTE_AGGIORNAMENTO"));
        com10RVO.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO"));
        com10RVO.setIdComunicazione10R(rs.getLong("ID_COMUNICAZIONE_10R"));
        com10RVO.setDataRicalcolo(rs.getTimestamp("DATA_RICALCOLO"));
        com10RVO.setVolumeSottogrigliato(rs.getBigDecimal("VOLUME_SOTTOGRIGLIATO"));
        com10RVO.setVolumeRefluoAzienda(rs.getBigDecimal("VOLUME_REFLUO_AZIENDA"));
        com10RVO.setSuperficieConduzioneZVN(rs.getBigDecimal("SUPERFICIE_CONDUZIONE_ZVN"));
        com10RVO.setSuperficieAsservimentoZVN(rs.getBigDecimal("SUPERFICIE_ASSERVIMENTO_ZVN"));
        com10RVO.setSuperficieConduzioneNoZVN(rs.getBigDecimal("SUPERFICIE_CONDUZIONE_NO_ZVN"));
        com10RVO.setSuperficieAsservimentoNoZVN(rs.getBigDecimal("SUPERFICIE_ASSERVIMENTO_NO_ZVN"));
        com10RVO.setAzotoConduzioneZVN(rs.getBigDecimal("AZOTO_CONDUZIONE_ZVN"));
        com10RVO.setAzotoAsservimentoZVN(rs.getBigDecimal("AZOTO_ASSERVIMENTO_ZVN"));
        com10RVO.setAzotoConduzioneNoZVN(rs.getBigDecimal("AZOTO_CONDUZIONE_NO_ZVN"));
        com10RVO.setAzotoAsservimentoNoZVN(rs.getBigDecimal("AZOTO_ASSERVIMENTO_NO_ZVN"));
        com10RVO.setStocNettoPalabile(rs.getBigDecimal("STOC_NETTO_PALABILE"));
        com10RVO.setStocNettoNonPalabile(rs.getBigDecimal("STOC_NETTO_NONPALABILE"));
        com10RVO.setStocDispPalabileVol(rs.getBigDecimal("STOC_DISP_PALABILE_VOL"));
        com10RVO.setStocDispNonPalabileVol(rs.getBigDecimal("STOC_DISP_NONPALABILE_VOL"));
        com10RVO.setTotaleAzotoAziendale(rs.getBigDecimal("TOTALE_AZOTO_AZIENDALE"));
        com10RVO.setVolumePiogge(rs.getBigDecimal("VOLUME_PIOGGE"));
        com10RVO.setAcqueLavaggio(rs.getBigDecimal("ACQUE_LAVAGGIO"));
        com10RVO.setStocAcqueNettoCessione(rs.getBigDecimal("STOC_ACQUE_NETTO_CESSIONE"));
        com10RVO.setStocAcqueNecGG(rs.getBigDecimal("STOC_ACQUE_NEC_GG"));
        com10RVO.setStocAcqueNecVol(rs.getBigDecimal("STOC_ACQUE_NEC_VOL"));
        com10RVO.setNote(rs.getString("NOTE"));
        com10RVO.setIdUte(rs.getLong("ID_UTE"));
        com10RVO.setTotaleAzoto(rs.getBigDecimal("TOTALE_AZOTO"));
        com10RVO.setSuperficieSauPiemonte(rs.getBigDecimal("SUPERFICIE_SAU_PIEMONTE"));
        com10RVO.setSuperficieSauPiemonteZVN(rs.getBigDecimal("SUPERFICIE_SAU_PIEMONTE_ZVN"));
        com10RVO.setAdesioneDeroga(rs.getString("ADESIONE_DEROGA"));
        com10RVO.setAzotoEscretoPascolo(rs.getBigDecimal("AZOTO_ESCRETO_PASCOLO"));
        com10RVO.setUtenteUltimaModifica(rs.getString("UT_DENOMINAZIONE"));
        com10RVO.setEnteUltimaModifica(rs.getString("UT_ENTE_APPART"));
        result.add(com10RVO);
      }
      return result.size()==0?null:(Comunicazione10RVO[])result.toArray(new Comunicazione10RVO[0]);
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("com10RVO", result) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),  
        new Parametro("idPianoRiferimento", idPianoRiferimento) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::getComunicazione10RByPianoRifererimento] ",
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
              "[Comunicazione10RGaaDAO::getComunicazione10RByPianoRifererimento] END.");
    }
  }
  
  
  /**
   * Restituisce il volume del refluo prodotto del tipo effluente
   * e dell'azienda in questione.
   * 
   * @param idAzienda
   * @param idEffluente
   * @return
   * @throws DataAccessException
   */
  public PlSqlCodeDescription controlloQuantitaEffluentePlSql(long idUte, long idEffluente) 
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
              "[Comunicazione10RGaaDAO::controlloQuantitaEffluentePlSql] BEGIN.");
      /***
       * PROCEDURE CALCOLA_EFFLUENTI_M3       ( pIdUte        IN NUMBER,
                            pFlagPalabile     IN VARCHAR2 DEFAULT NULL,
                            pFlagTrattamento    IN VARCHAR2,
                            pIdEffluente      IN  NUMBER DEFAULT NULL,
                            pVolumeProdotto   OUT NUMBER,
                            pVolumeProdottoAz   OUT NUMBER,
                            pAzotoProdotto      OUT NUMBER,
                            pAzotoProdottoAz    OUT NUMBER,
                            pCodErr           OUT VARCHAR2,
                            pDesErr         OUT VARCHAR2
                          );
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{call PACK_COMUNICAZIONE_10R.CALCOLA_EFFLUENTI_M3(?,?,?,?,?,?,?,?,?,?)}");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::controlloQuantitaEffluentePlSql] Query="
                + query);
      }
      
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.setLong(1, idUte);
      cs.setString(2, null);
      cs.setString(3, "S");
      cs.setLong(4, idEffluente);
      cs.registerOutParameter(5,Types.DECIMAL);
      cs.registerOutParameter(6,Types.DECIMAL);
      cs.registerOutParameter(7,Types.DECIMAL);
      cs.registerOutParameter(8,Types.DECIMAL);
      cs.registerOutParameter(9,Types.VARCHAR);
      cs.registerOutParameter(10,Types.VARCHAR);
      
      cs.executeUpdate();
      
      plqObj = new PlSqlCodeDescription();  
      plqObj.setDescription(cs.getString(9)); //codice errore
      plqObj.setOtherdescription(cs.getString(10)); //msg errore
      plqObj.setItem(cs.getBigDecimal(5)); //pVolumeProdotto
      
      
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
      { new Parametro("idUte", idUte),  
        new Parametro("idEffluente", idEffluente) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::controlloQuantitaEffluentePlSql] ",
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
              "[Comunicazione10RGaaDAO::controlloQuantitaEffluentePlSql] END.");
    }
  }
  
  
  /**
   * Calcola l'azoto prodotto relativo alla comunicazione 10r, alla causale dell'effluente,
   * all'effluente e alla quantità.
   * 
   * @param comunicazione10r
   * @param idCausaleEffluente
   * @param idEffluente
   * @param quantita
   * @return
   * @throws DataAccessException
   */
  public PlSqlCodeDescription calcolaQuantitaAzotoPlSql(long idUte, long idComunicazione10r, 
      long idCausaleEffluente, long idEffluente, BigDecimal quantita) 
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
              "[Comunicazione10RGaaDAO::calcolaQuantitaAzotoPlSql] BEGIN.");
      /***
       * PROCEDURE TROVA_CES_ACQ_10R (  pIdUte  IN  DB_COMUNICAZIONE_10R.ID_UTE%TYPE
                      pIdComunicazione    IN NUMBER,
                      pCausaleEffluente   IN NUMBER,
                      pIdEffluente      IN NUMBER,
                      pQuantitaIn       IN NUMBER,
                        pQuantitaOut      OUT NUMBER,
                        pCodErr           OUT VARCHAR2,
                        pDesErr         OUT VARCHAR2)
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{call PACK_COMUNICAZIONE_10R.TROVA_CES_ACQ_10R(?,?,?,?,?,?,?,?)}");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::calcolaQuantitaAzotoPlSql] Query="
                + query);
      }
      
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.setLong(1, idUte);
      cs.setLong(2, idComunicazione10r);
      cs.setLong(3, idCausaleEffluente);
      cs.setLong(4, idEffluente);
      cs.setBigDecimal(5, quantita);
      cs.registerOutParameter(6,Types.DECIMAL); //quantita
      cs.registerOutParameter(7,Types.VARCHAR); //codice errore
      cs.registerOutParameter(8,Types.VARCHAR); //msg errore
      
      cs.executeUpdate();
      
      plqObj = new PlSqlCodeDescription();  
      plqObj.setDescription(cs.getString(7)); //codice errore
      plqObj.setOtherdescription(cs.getString(8)); //msg errore
      plqObj.setItem(cs.getBigDecimal(6)); //quantita
      
      
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
      { new Parametro("idComunicazione10r", idComunicazione10r),
        new Parametro("idCausaleEffluente", idCausaleEffluente),
        new Parametro("idEffluente", idEffluente),
        new Parametro("quantita", quantita) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::calcolaQuantitaAzotoPlSql] ",
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
              "[Comunicazione10RGaaDAO::calcolaQuantitaAzotoPlSql] END.");
    }
  }
  
  /**
   * Ricalcola tutta la comunicazione 10r
   * 
   * @param idAzienda
   * @param idUtente
   * @return
   * @throws DataAccessException
   */
  public PlSqlCodeDescription ricalcolaPlSql(long idAzienda, 
      long idUtente) 
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
              "[Comunicazione10RGaaDAO::ricalcolaPlSql] BEGIN.");
      /***
       * PROCEDURE RICALCOLA_AZIENDA (  pIdAzienda        IN NUMBER,
                                pIdUtente       IN NUMBER,  
                                pCodErr           OUT VARCHAR2,
                                pDesErr         OUT VARCHAR2)
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{call PACK_COMUNICAZIONE_10R.RICALCOLA_AZIENDA(?,?,?,?)}");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::ricalcolaPlSql] Query="
                + query);
      }
      
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.setLong(1, idAzienda);
      cs.setLong(2, idUtente);
      cs.registerOutParameter(3,Types.VARCHAR); //codice errore
      cs.registerOutParameter(4,Types.VARCHAR); //msg errore
      
      cs.executeUpdate();
      
      plqObj = new PlSqlCodeDescription();  
      plqObj.setDescription(cs.getString(3)); //codice errore
      plqObj.setOtherdescription(cs.getString(4)); //msg errore
      
      
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
        new Parametro("idUtente", idUtente) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::ricalcolaPlSql] ",
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
              "[Comunicazione10RGaaDAO::ricalcolaPlSql] END.");
    }
  }
  
  
  /**
   * Ritorna un vettore di AcquaExtraVO associato ad una comunicazione
   * 
   * @param idComunicazione10R
   * @return
   * @throws DataAccessException
   */
  public boolean lockTableComunicazione10R(long idUte) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean flagLock = false;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[Comunicazione10RGaaDAO::lockTableComunicazione10R] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append(" SELECT * " +
          		"FROM DB_COMUNICAZIONE_10R " +
          		"WHERE ID_UTE  = ? " +
          		"AND DATA_FINE_VALIDITA IS NULL " +
          		"FOR UPDATE ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::lockTableComunicazione10R] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice,idUte);
      
      
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        flagLock = true;
      }
      
      return flagLock;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("flagLock", flagLock) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUte", idUte)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::lockTableComunicazione10R] ",
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
              "[Comunicazione10RGaaDAO::lockTableComunicazione10R] END.");
    }
  }
  
  
  /**
   * 
   * ricavo la somma degli affluneti ceduti o acquisiti.
   * ritorno 0 se non trovo nulla
   * 
   * @param idComunicazione10R
   * @param idCausaleEffluente
   * @return
   * @throws DataAccessException
   */
  public Vector<BigDecimal> getSommaEffluentiCessAcquPerStampa(long idComunicazione10R) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<BigDecimal> sommaAzotoDichiarato = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[Comunicazione10RGaaDAO::getSommaEffluentiCessAcquPerStampa] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
      	"SELECT NVL(SUM(DECODE(ECA.ID_CAUSALE_EFFLUENTE,1,ECA.QUANTITA_AZOTO_DICHIARATO)),0) " +
      	"       AS QUANTITA_AZOTO_CEDUTO, " +
        "       NVL(SUM(DECODE(ECA.ID_CAUSALE_EFFLUENTE,2,ECA.QUANTITA_AZOTO_DICHIARATO)),0) " +
        "       AS QUANTITA_AZOTO_ACQUISITO " +
        "FROM   " +
        "       DB_EFFLUENTE_CES_ACQ_10R ECA " +
        "WHERE  ECA.ID_COMUNICAZIONE_10R = ? ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::getSommaEffluentiCessAcquPerStampa] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idComunicazione10R);
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        sommaAzotoDichiarato = new Vector<BigDecimal>();
        sommaAzotoDichiarato.add(rs.getBigDecimal("QUANTITA_AZOTO_ACQUISITO"));
        sommaAzotoDichiarato.add(rs.getBigDecimal("QUANTITA_AZOTO_CEDUTO"));
      }
      return sommaAzotoDichiarato;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("sommaAzotoDichiarato", sommaAzotoDichiarato) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idComunicazione10R", idComunicazione10R)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::getSommaEffluentiCessAcquPerStampa] ",
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
              "[Comunicazione10RGaaDAO::getListEffluentiCessAcquPerStampa] END.");
    }
  }
  
  /**
   * restituisce la somma dello stoc necessario vol palabile e non palabile
   * relativamente all'ute
   * 
   * 
   * 
   * @param idComunicazione10R
   * @return
   * @throws DataAccessException
   */
  public BigDecimal getSommaEffluenti10RPerStampa(long idComunicazione10R, boolean palabile) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    BigDecimal sommaStocNecessarioVol = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[Comunicazione10RGaaDAO::getSommaEffluenti10RPerStampa] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "SELECT NVL(SUM(EE.STOC_NECESSARIO_VOL), 0) " +
        "       AS STOC_NECESSARIO_VOL " +
        "FROM   " +
        "       DB_EFFLUENTE_10R EE, " +
        "       DB_TIPO_EFFLUENTE TE " +
        "WHERE  EE.ID_COMUNICAZIONE_10R = ? " +
        "AND    EE.ID_EFFLUENTE = TE.ID_EFFLUENTE " +
        "AND    TE.FLAG_PALABILE = ? ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::getSommaEffluenti10RPerStampa] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idComunicazione10R);
      if(palabile)
      {
        stmt.setString(++indice, "S");
      }
      else
      {
        stmt.setString(++indice, "N");
      }
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        sommaStocNecessarioVol = rs.getBigDecimal("STOC_NECESSARIO_VOL");
      }
      return sommaStocNecessarioVol;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("sommaStocNecessarioVol", sommaStocNecessarioVol) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idComunicazione10R", idComunicazione10R)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::getSommaEffluenti10RPerStampa] ",
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
              "[Comunicazione10RGaaDAO::getSommaEffluenti10RPerStampa] END.");
    }
  }
  
  /**
   * 
   * Propone la quantità di refluo dell'ultima comunicazione 10/R dell'azienda cedente
   * 
   * 
   * 
   * @param idAzienda
   * @param idAziendaCess
   * @param idCausaleEffluente
   * @param idEffluente
   * @param quantita
   * @return
   * @throws DataAccessException
   */
  public PlSqlCodeDescription calcolaM3EffluenteAcquisitoPlSql(long idAzienda, 
      long idAziendaCess, long idCausaleEffluente, long idEffluente) 
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
              "[Comunicazione10RGaaDAO::calcolaM3EffluenteAcquisitoPlSql] BEGIN.");
      /***
       * PROCEDURE M3_EFFLUENTE_CEDUTO_ACQUISITO (pIdAziendaCorrente     IN DB_AZIENDA.ID_AZIENDA%TYPE,
                                                 pIdAziendaCesAcq       IN DB_AZIENDA.ID_AZIENDA%TYPE,
                                                 pIdEffluente           IN DB_TIPO_EFFLUENTE.ID_EFFLUENTE%TYPE,
                                                 pIdCausaleEffluente    IN DB_TIPO_CAUSALE_EFFLUENTE.ID_CAUSALE_EFFLUENTE%TYPE,
                                                 pTotEffluenteCedAcq   OUT DB_EFFLUENTE_CES_ACQ_10R.QUANTITA%TYPE,
                                                 pCodErr               OUT VARCHAR2, 
                                                 pMessErr              OUT VARCHAR2
                                                 )
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{call PACK_COMUNICAZIONE_10R.M3_EFFLUENTE_CEDUTO_ACQUISITO(?,?,?,?,?,?,?)}");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::calcolaM3EffluenteAcquisitoPlSql] Query="
                + query);
      }
      
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.setLong(1, idAzienda);
      cs.setLong(2, idAziendaCess);
      cs.setLong(3, idEffluente);
      cs.setLong(4, idCausaleEffluente);
      cs.registerOutParameter(5,Types.DECIMAL); //quantita
      cs.registerOutParameter(6,Types.VARCHAR); //codice errore
      cs.registerOutParameter(7,Types.VARCHAR); //msg errore
      
      cs.executeUpdate();
      
      plqObj = new PlSqlCodeDescription();  
      plqObj.setDescription(cs.getString(6)); //codice errore
      plqObj.setOtherdescription(cs.getString(7)); //msg errore
      plqObj.setItem(cs.getBigDecimal(5)); //quantita
      
      
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
        new Parametro("idAziendaCess", idAziendaCess),
        new Parametro("idCausaleEffluente", idCausaleEffluente),
        new Parametro("idEffluente", idEffluente)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::calcolaM3EffluenteAcquisitoPlSql] ",
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
              "[Comunicazione10RGaaDAO::calcolaM3EffluenteAcquisitoPlSql] END.");
    }
  }
  
  /**
   * IL controllo viene fatto solo se le specie sono diverse da 1/2
   * e se sono 1/2 hanno numero gg != 365
   * 
   * 
   * @param idUte
   * @return
   * @throws DataAccessException
   */
  public boolean controlloRefluoPascolo(long idUte) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean flagEseguiControllo = false;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[Comunicazione10RGaaDAO::controlloRefluoPascolo] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "SELECT NVL(SCA.GIORNI_PASCOLO_ESTATE,0) AS GG_ESTATE, " +
        "       NVL(SCA.GIORNI_PASCOLO_INVERNO,0) AS GG_INVERNO, " +
        "       A.ID_SPECIE_ANIMALE " +
        "FROM   DB_ALLEVAMENTO A, " +
        "       DB_CATEGORIE_ALLEVAMENTO CA, " + 
        "       DB_SOTTOCATEGORIA_ALLEVAMENTO SCA " + 
        "WHERE  A.ID_UTE = ? " +
        "AND    A.DATA_FINE IS NULL " +
        "AND    A.ID_ALLEVAMENTO = CA.ID_ALLEVAMENTO " + 
        "AND    CA.ID_CATEGORIE_ALLEVAMENTO = SCA.ID_CATEGORIE_ALLEVAMENTO " + 
        "ORDER BY SCA.ID_SOTTOCATEGORIA_ALLEVAMENTO ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::controlloRefluoPascolo] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idUte);      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(!flagEseguiControllo)
        {
          long idSpecie = rs.getLong("ID_SPECIE_ANIMALE");
          if((idSpecie != 1) && (idSpecie != 2))
          {
            flagEseguiControllo = true;
          }
          else
          {
            int ggEstate = rs.getInt("GG_ESTATE");
            int ggInverno = rs.getInt("GG_INVERNO");
            if((ggEstate+ggInverno) != 365)
            {
              flagEseguiControllo = true;
            }            
          }
        }
      }
      
      return flagEseguiControllo;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("flagEseguiControllo", flagEseguiControllo) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUte", idUte)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::controlloRefluoPascolo] ",
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
              "[Comunicazione10RGaaDAO::controlloRefluoPascolo] END.");
    }
  }
  
  
  public Vector<TipoEffluenteVO> getListTipoEffluenteTrattamenti(long idUte) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoEffluenteVO> vTipoEffluente = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[Comunicazione10RGaaDAO::getListTipoEffluenteTrattamenti] BEGIN.");

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT TE.ID_EFFLUENTE, " +
        "       TE.DESCRIZIONE, " +
        "       TE.FLAG_PALABILE, " +
        "       TE.DATA_INIZIO_VALIDITA, " +
        "       SUM(VOLUME_PRODOTTO) AS VOL_PROD, " +
        "       SUM(AZOTO_PRODOTTO) AS AZ_PROD " + 
        "FROM   DB_EFFLUENTE_PRODOTTO EP, " +
        "       DB_STABULAZIONE_TRATTAMENTO ST, " +
        "       DB_SOTTOCATEGORIA_ALLEVAMENTO SA, " +
        "       DB_CATEGORIE_ALLEVAMENTO CA, " +
        "       DB_ALLEVAMENTO AL, " +
        "       DB_TIPO_EFFLUENTE TE " +
        "WHERE  AL.ID_UTE = ? " +
        "AND EP.ID_STABULAZIONE_TRATTAMENTO = ST.ID_STABULAZIONE_TRATTAMENTO  " +
        "AND ST.ID_SOTTOCATEGORIA_ALLEVAMENTO = SA.ID_SOTTOCATEGORIA_ALLEVAMENTO " +
        "AND SA.ID_CATEGORIE_ALLEVAMENTO = CA.ID_CATEGORIE_ALLEVAMENTO " +
        "AND CA.ID_ALLEVAMENTO = AL.ID_ALLEVAMENTO " +
        "AND EP.ID_EFFLUENTE = TE.ID_EFFLUENTE " +
        "AND EP.FLAG_TRATTAMENTO = 'N' " +
        "AND AL.DATA_FINE IS NULL " +
        "GROUP BY TE.ID_EFFLUENTE, " +
        "       TE.DESCRIZIONE, " +
        "       TE.FLAG_PALABILE, " +
        "       TE.DATA_INIZIO_VALIDITA " +
        "ORDER BY TE.DESCRIZIONE ");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::getListTipoEffluenteTrattamenti] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idUte);
     
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vTipoEffluente == null)
        {
          vTipoEffluente = new Vector<TipoEffluenteVO>();
        }
        TipoEffluenteVO tipoVO  = new TipoEffluenteVO();
        tipoVO.setIdEffluente(rs.getLong("ID_EFFLUENTE"));
        tipoVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoVO.setFlagPalabile(rs.getString("FLAG_PALABILE"));
        tipoVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        tipoVO.setVolumeProdotto(rs.getBigDecimal("VOL_PROD"));
        tipoVO.setAzotoProdotto(rs.getBigDecimal("AZ_PROD"));
        
        vTipoEffluente.add(tipoVO);
      }
      
      return vTipoEffluente;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vTipoEffluente", vTipoEffluente)};
  
      
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUte", idUte)  };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::getListTipoEffluenteTrattamenti] ",
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
              "[Comunicazione10RGaaDAO::getListTipoEffluenteTrattamenti] END.");
    }
  }
  
  
  public Vector<TipoEffluenteVO> getListTipoEffluenteByLegameId(long idEffluente) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoEffluenteVO> vTipoEffluente = null;
    TipoEffluenteVO tipoEffluente = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[Comunicazione10RGaaDAO::getListTipoEffluenteByLegameId] BEGIN.");

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT TE.ID_EFFLUENTE, " + 
        "       TE.DESCRIZIONE, " +
        "       TE.FLAG_PALABILE, " +
        "       TE.DATA_INIZIO_VALIDITA   " +
        "FROM   DB_TIPO_EFFLUENTE TE,   " +
        "       DB_LEGAME_EFFLUENTE LE " +
        "WHERE  LE.ID_EFFLUENTE  = ?  " +
        "AND    LE.ID_EFFLUENTE_TRATTATO = TE.ID_EFFLUENTE " +
        "ORDER BY TE.DESCRIZIONE ");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::getListTipoEffluenteByLegameId] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;
      stmt.setLong(++indice, idEffluente);
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vTipoEffluente == null)
        {
          vTipoEffluente = new Vector<TipoEffluenteVO>();
        }
        tipoEffluente = new TipoEffluenteVO();
        
        tipoEffluente.setIdEffluente(rs.getLong("ID_EFFLUENTE"));
        tipoEffluente.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoEffluente.setFlagPalabile(rs.getString("FLAG_PALABILE"));
        
        vTipoEffluente.add(tipoEffluente);
        
      }
      return vTipoEffluente;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("tipoEffluente", tipoEffluente), 
          new Variabile("vTipoEffluente", vTipoEffluente)};
      
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idEffluente", idEffluente)};
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::getListTipoEffluenteByLegameId] ",
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
              "[Comunicazione10RGaaDAO::getListTipoEffluenteByLegameId] END.");
    }
  }
  
  
  public Vector<TipoEffluenteVO> getListTipoEffluenteAndValueByLegameId(long idComunicazione10R, long idEffluente) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoEffluenteVO> vTipoEffluente = null;
    TipoEffluenteVO tipoEffluente = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[Comunicazione10RGaaDAO::getListTipoEffluenteAndValueByLegameId] BEGIN.");

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT TE.ID_EFFLUENTE, " + 
        "       TE.DESCRIZIONE, " +
        "       TE.FLAG_PALABILE, " +
        "       TE.DATA_INIZIO_VALIDITA,   " +
        "       EFF.VOLUME_POST_DICHIARATO, " +
        "       EFF.AZOTO_POST_DICHIARATO  " +
        "FROM   DB_TIPO_EFFLUENTE TE,   " +
        "       DB_LEGAME_EFFLUENTE LE, " +
        "       DB_EFFLUENTE_10R EFF " +
        "WHERE  LE.ID_EFFLUENTE  = ?  " +
        "AND    LE.ID_EFFLUENTE_TRATTATO = TE.ID_EFFLUENTE " +
        "AND    EFF.ID_COMUNICAZIONE_10R = ? " +
        "AND    EFF.ID_EFFLUENTE_ORIGINE = ? " +
        "AND    EFF.ID_EFFLUENTE = LE.ID_EFFLUENTE_TRATTATO " +
        "ORDER BY TE.DESCRIZIONE ");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::getListTipoEffluenteAndValueByLegameId] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;
      stmt.setLong(++indice, idEffluente);
      stmt.setLong(++indice, idComunicazione10R);
      stmt.setLong(++indice, idEffluente);
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vTipoEffluente == null)
        {
          vTipoEffluente = new Vector<TipoEffluenteVO>();
        }
        tipoEffluente = new TipoEffluenteVO();
        
        tipoEffluente.setIdEffluente(rs.getLong("ID_EFFLUENTE"));
        tipoEffluente.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoEffluente.setFlagPalabile(rs.getString("FLAG_PALABILE"));
        tipoEffluente.setVolumeProdotto(rs.getBigDecimal("VOLUME_POST_DICHIARATO"));
        tipoEffluente.setAzotoProdotto(rs.getBigDecimal("AZOTO_POST_DICHIARATO"));
        
        vTipoEffluente.add(tipoEffluente);
        
      }
      return vTipoEffluente;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("tipoEffluente", tipoEffluente), 
          new Variabile("vTipoEffluente", vTipoEffluente)};
      
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idEffluente", idEffluente)};
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::getListTipoEffluenteAndValueByLegameId] ",
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
              "[Comunicazione10RGaaDAO::getListTipoEffluenteAndValueByLegameId] END.");
    }
  }
  
  
  
  public Vector<EffluenteVO> getListTrattamenti(long idComunicazione10R[]) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<EffluenteVO> vEffluenti = null;
    EffluenteVO effVO = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[Comunicazione10RGaaDAO::getListTrattamenti] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      String parametri=" (";
      
      if (idComunicazione10R!=null && idComunicazione10R.length!=0)
      {
        for(int i=0;i<idComunicazione10R.length;i++)
        {
          parametri+="?";
          if (i<idComunicazione10R.length-1)
            parametri+=",";
        }
      }
      parametri+=") ";
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT EF.ID_EFFLUENTE_10R, " + 
        "       EF.ID_COMUNICAZIONE_10R, " +
        "       EF.ID_EFFLUENTE,   " +
        "       TE.FLAG_PALABILE AS FP, " +
        "       TE_ORIG.FLAG_PALABILE AS FP_ORIG, " +
        "       TE_ORIG.DESCRIZIONE AS DESC_FP_ORIG, " +
        "       EF.VOLUME_INIZIALE, " +
        "       EF.AZOTO_INIZIALE, " +
        "       EF.VOLUME_POST_TRATTAMENTO,  " +
        "       EF.AZOTO_POST_TRATTAMENTO,  " +
        "       EF.VOLUME_POST_DICHIARATO,  " +
        "       EF.AZOTO_POST_DICHIARATO, " +
        "       EF.VOLUME_CESSIONE, " +
        "       EF.AZOTO_CESSIONE, " +
        "       EF.VOLUME_ACQUISIZIONE, " +
        "       EF.AZOTO_ACQUISIZIONE, " +
        "       EF.STOC_DISPONIBILE_GG, " +
        "       EF.STOC_NECESSARIO_VOL, " +
        "       EF.STOC_NECESSARIO_GG, " +
        "       EF.VOLUME_CESSIONE_STOCCATO, " +
        "       EF.VOLUME_ACQUISIZIONE_STOCCATO, " +
        "       EF.AZOTO_INIZIALE_DEC, " +
        "       EF.AZOTO_POST_TRATTAMENTO_DEC, " +
        "       EF.VOLUME_INIZIALE_CON, " +
        "       EF.VOLUME_POST_TRATTAMENTO_CON, " +
        "       EF.ID_TRATTAMENTO, " +
        "       EF.ID_EFFLUENTE_ORIGINE, " +
        "       COM10.ID_UTE, " +
        "       TT.CODICE_SIAP " +
        "FROM   DB_EFFLUENTE_10R EF, " + 
        "       DB_TIPO_EFFLUENTE TE, " +
        "       DB_TIPO_EFFLUENTE TE_ORIG, " +
        "       DB_COMUNICAZIONE_10R COM10, " +
        "       DB_TIPO_TRATTAMENTO TT " +
        "WHERE  EF.ID_COMUNICAZIONE_10R IN  " +parametri +
        "AND    EF.ID_EFFLUENTE_ORIGINE = TE_ORIG.ID_EFFLUENTE  " +
        "AND    EF.ID_EFFLUENTE = TE.ID_EFFLUENTE  " +
        "AND    EF.ID_TRATTAMENTO IS NOT NULL " +
        "AND    EF.ID_COMUNICAZIONE_10R = COM10.ID_COMUNICAZIONE_10R " +
        "AND    EF.ID_TRATTAMENTO = TT.ID_TRATTAMENTO " +
        "ORDER BY EF.ID_COMUNICAZIONE_10R, " +
        "         EF.ID_EFFLUENTE_ORIGINE, " +
        "         EF.ID_TRATTAMENTO," +
        "         TE.DESCRIZIONE ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::getListTrattamenti] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;

      if (idComunicazione10R!=null && idComunicazione10R.length!=0)
      {
        for(int i=0;i<idComunicazione10R.length;i++)
          stmt.setLong(++indice,idComunicazione10R[i]);
      }
      
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      Long idEffluenteOrigineTmp = new Long(0);
      Long idTrattamentoTmp = new Long(0);
      while (rs.next())
      {
        if(vEffluenti == null)
        {
          vEffluenti = new Vector<EffluenteVO>();
        }
        
        //Anche se il campo ID_EFFLUENTE_ORIGINE è nullabile nei trattamente deve essere
        //per forza sempre valorizzato...
        Long idEffluenteOrigine = new Long(rs.getString("ID_EFFLUENTE_ORIGINE"));
        Long idTrattamento = new Long(rs.getString("ID_TRATTAMENTO"));
        String flagPalabile = rs.getString("FP");
        
        if((idEffluenteOrigineTmp.compareTo(idEffluenteOrigine) != 0)
          || (idTrattamentoTmp.compareTo(idTrattamento) !=0))
        { 
          if((idEffluenteOrigineTmp.longValue() !=0)
            || (idTrattamentoTmp.longValue() !=0))
          {
            vEffluenti.add(effVO);
          }
          
          effVO = new EffluenteVO();   
        
          //inutile ne avrei due per ogni trattamento...
          //effVO.setIdEffluente10R(rs.getLong("ID_EFFLUENTE_10R"));
          effVO.setIdComunicazione10R(rs.getLong("ID_COMUNICAZIONE_10R"));
          effVO.setIdEffluente(rs.getLong("ID_EFFLUENTE"));
          
          String flagPalabileOrig = rs.getString("FP_ORIG");
          
          if(Validator.isNotEmpty(flagPalabileOrig))
          {
            if(flagPalabileOrig.equalsIgnoreCase("S"))
            {
              effVO.setTipoEffluente("Palabile");
            }
            else if(flagPalabileOrig.equalsIgnoreCase("N"))
            {
              effVO.setTipoEffluente("Non palabile");
            }
          }
          
          if(Validator.isNotEmpty(flagPalabile))
          {
            if(flagPalabile.equalsIgnoreCase("S"))
            {
              effVO.setVolumePostDichiarato(rs.getBigDecimal("VOLUME_POST_DICHIARATO"));
              effVO.setAzotoPostDichiarato(rs.getBigDecimal("AZOTO_POST_DICHIARATO"));
              effVO.setIdEffluentePalabile(new Long(rs.getLong("ID_EFFLUENTE")));
            }
            else if(flagPalabile.equalsIgnoreCase("N"))
            {
              effVO.setVolumePostDichiaratoNonPal(rs.getBigDecimal("VOLUME_POST_DICHIARATO"));
              effVO.setAzotoPostDichiaratoNonPal(rs.getBigDecimal("AZOTO_POST_DICHIARATO"));
              effVO.setIdEffluenteNonPalabile(new Long(rs.getLong("ID_EFFLUENTE")));
            }
          }
          effVO.setDescrizione(rs.getString("DESC_FP_ORIG"));
          effVO.setVolumeIniziale(rs.getBigDecimal("VOLUME_INIZIALE"));
          effVO.setAzotoIniziale(rs.getBigDecimal("AZOTO_INIZIALE"));
          effVO.setAzotoInizialeDec(rs.getBigDecimal("AZOTO_INIZIALE_DEC"));
          effVO.setVolumePostTrattamento(rs.getBigDecimal("VOLUME_POST_TRATTAMENTO"));          
          
          effVO.setAzotoPostTrattamento(rs.getBigDecimal("AZOTO_POST_TRATTAMENTO"));
          effVO.setAzotoPostTrattamentoDec(rs.getBigDecimal("AZOTO_POST_TRATTAMENTO_DEC"));          
          
          effVO.setVolumeCessione(rs.getBigDecimal("VOLUME_CESSIONE"));
          effVO.setAzotoCessione(rs.getBigDecimal("AZOTO_CESSIONE"));
          effVO.setVolumeAcquisizione(rs.getBigDecimal("VOLUME_ACQUISIZIONE"));
          effVO.setAzotoAcquisizione(rs.getBigDecimal("AZOTO_ACQUISIZIONE"));
          effVO.setStocDispGg(rs.getBigDecimal("STOC_DISPONIBILE_GG"));
          effVO.setStocNecVol(rs.getBigDecimal("STOC_NECESSARIO_VOL"));
          effVO.setStocNecGg(rs.getBigDecimal("STOC_NECESSARIO_GG"));
          effVO.setVolumeCessioneStoccato(rs.getBigDecimal("VOLUME_CESSIONE_STOCCATO"));
          effVO.setVolumeAcquisizioneStoccato(rs.getBigDecimal("VOLUME_ACQUISIZIONE_STOCCATO"));
          effVO.setVolumeInizialeCon(rs.getBigDecimal("VOLUME_INIZIALE_CON"));
          effVO.setVolumePostTrattamentoCon(rs.getBigDecimal("VOLUME_POST_TRATTAMENTO_CON"));
          effVO.setIdTrattamento(checkLongNull(rs.getString("ID_TRATTAMENTO")));
          effVO.setIdEffluenteOrigine(checkLongNull(rs.getString("ID_EFFLUENTE_ORIGINE")));
          effVO.setIdUte(rs.getLong("ID_UTE"));
          effVO.setCodiceTrattamento(rs.getString("CODICE_SIAP"));
          
        }
        else
        {
          if(Validator.isNotEmpty(flagPalabile))
          {
            if(flagPalabile.equalsIgnoreCase("S"))
            {
              effVO.setVolumePostDichiarato(rs.getBigDecimal("VOLUME_POST_DICHIARATO"));
              effVO.setAzotoPostDichiarato(rs.getBigDecimal("AZOTO_POST_DICHIARATO"));
              effVO.setIdEffluentePalabile(new Long(rs.getLong("ID_EFFLUENTE")));
            }
            else if(flagPalabile.equalsIgnoreCase("N"))
            {
              effVO.setVolumePostDichiaratoNonPal(rs.getBigDecimal("VOLUME_POST_DICHIARATO"));
              effVO.setAzotoPostDichiaratoNonPal(rs.getBigDecimal("AZOTO_POST_DICHIARATO"));
              effVO.setIdEffluenteNonPalabile(new Long(rs.getLong("ID_EFFLUENTE")));
            }
          }
        }        
        idEffluenteOrigineTmp = effVO.getIdEffluenteOrigine();
        idTrattamentoTmp = effVO.getIdTrattamento();
        
      }
      
      if(vEffluenti != null)
        vEffluenti.add(effVO);
      return vEffluenti;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vEffluenti", vEffluenti) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idComunicazione10R", idComunicazione10R)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::getListTrattamenti] ",
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
              "[Comunicazione10RGaaDAO::getListTrattamenti] END.");
    }
  }
  
  
  /**
   * Questo meotodo è usato nella storicizzazione della comunicazione 10 r per cancellare
   * gli effluenti trattati, poiche pe rogni effluente a Video ne ho due su DB!!!
   * 
   * 
   * 
   * @param idComunicazione10R
   * @return
   * @throws DataAccessException
   */
  public Vector<Long> getListTrattamentiElim(long idComunicazione10R[]) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<Long> vIdEFlluente10R = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[Comunicazione10RGaaDAO::getListTrattamentiElim] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      String parametri=" (";
      
      if (idComunicazione10R!=null && idComunicazione10R.length!=0)
      {
        for(int i=0;i<idComunicazione10R.length;i++)
        {
          parametri+="?";
          if (i<idComunicazione10R.length-1)
            parametri+=",";
        }
      }
      parametri+=") ";
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT EF.ID_EFFLUENTE_10R " + 
        "FROM   DB_EFFLUENTE_10R EF " + 
        "WHERE  EF.ID_COMUNICAZIONE_10R IN  " +parametri);
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::getListTrattamentiElim] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;

      if (idComunicazione10R!=null && idComunicazione10R.length!=0)
      {
        for(int i=0;i<idComunicazione10R.length;i++)
          stmt.setLong(++indice,idComunicazione10R[i]);
      }
      
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vIdEFlluente10R == null)
        {
          vIdEFlluente10R = new Vector<Long>();
        }
        
        vIdEFlluente10R.add(new Long(rs.getLong("ID_EFFLUENTE_10R")));   
        
      }
      
      return vIdEFlluente10R;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vIdEFlluente10R", vIdEFlluente10R) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idComunicazione10R", idComunicazione10R)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::getListTrattamentiElim] ",
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
              "[Comunicazione10RGaaDAO::getListTrattamentiElim] END.");
    }
  }
  
  
  public Vector<EffluenteVO> getListEffluentiNoTratt(long idComunicazione10R[]) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<EffluenteVO> vEffluenti = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[Comunicazione10RGaaDAO::getListEffluenti] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      String parametri=" (";
      
      if (idComunicazione10R!=null && idComunicazione10R.length!=0)
      {
        for(int i=0;i<idComunicazione10R.length;i++)
        {
          parametri+="?";
          if (i<idComunicazione10R.length-1)
            parametri+=",";
        }
      }
      parametri+=") ";
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   SELECT   " 
              + "     EF.ID_EFFLUENTE_10R, " 
              + "     EF.ID_COMUNICAZIONE_10R, "
              + "     EF.ID_EFFLUENTE,   "
              + "     TE.FLAG_PALABILE, "
              + "     TE.DESCRIZIONE, "
              + "     EF.VOLUME_INIZIALE, "
              + "     EF.AZOTO_INIZIALE, "
              + "     EF.VOLUME_POST_TRATTAMENTO,  "
              + "     EF.AZOTO_POST_TRATTAMENTO,  "
              + "     EF.VOLUME_POST_DICHIARATO,  "
              + "     EF.AZOTO_POST_DICHIARATO, "
              + "     EF.VOLUME_CESSIONE, "
              + "     EF.AZOTO_CESSIONE, "
              + "     EF.VOLUME_ACQUISIZIONE, "
              + "     EF.AZOTO_ACQUISIZIONE, "
              + "     EF.STOC_DISPONIBILE_GG, "
              + "     EF.STOC_NECESSARIO_VOL, "
              + "     EF.STOC_NECESSARIO_GG, "
              + "     EF.VOLUME_CESSIONE_STOCCATO, "
              + "     EF.VOLUME_ACQUISIZIONE_STOCCATO, "
              + "     EF.AZOTO_INIZIALE_DEC, "
              + "     EF.AZOTO_POST_TRATTAMENTO_DEC, "
              + "     EF.VOLUME_INIZIALE_CON, "
              + "     EF.VOLUME_POST_TRATTAMENTO_CON "
              + "   FROM   "
              + "     DB_EFFLUENTE_10R EF," 
              + "     DB_TIPO_EFFLUENTE TE "
              + "   WHERE  "
              + "     EF.ID_COMUNICAZIONE_10R IN  " +parametri
              + " AND EF.ID_EFFLUENTE = TE.ID_EFFLUENTE  "
              + " AND EF.ID_TRATTAMENTO IS NULL  "
              + " ORDER BY TE.FLAG_PALABILE DESC, TE.DESCRIZIONE ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::getListEffluenti] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;

      if (idComunicazione10R!=null && idComunicazione10R.length!=0)
      {
        for(int i=0;i<idComunicazione10R.length;i++)
          stmt.setLong(++indice,idComunicazione10R[i]);
      }
      
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vEffluenti == null)
        {
          vEffluenti = new Vector<EffluenteVO>();
        }
        EffluenteVO effVO = new EffluenteVO();
        effVO.setIdEffluente10R(rs.getLong("ID_EFFLUENTE_10R"));
        effVO.setIdComunicazione10R(rs.getLong("ID_COMUNICAZIONE_10R"));
        effVO.setIdEffluente(rs.getLong("ID_EFFLUENTE"));
        
        String flagPalabile = rs.getString("FLAG_PALABILE");
        if(Validator.isNotEmpty(flagPalabile))
        {
          if(flagPalabile.equalsIgnoreCase("S"))
          {
            effVO.setTipoEffluente("Palabile");
          }
          else if(flagPalabile.equalsIgnoreCase("N"))
          {
            effVO.setTipoEffluente("Non palabile");
          }
        }
        effVO.setDescrizione(rs.getString("DESCRIZIONE"));
        effVO.setVolumeIniziale(rs.getBigDecimal("VOLUME_INIZIALE"));
        effVO.setAzotoIniziale(rs.getBigDecimal("AZOTO_INIZIALE"));
        effVO.setAzotoInizialeDec(rs.getBigDecimal("AZOTO_INIZIALE_DEC"));
        BigDecimal volTratt = rs.getBigDecimal("VOLUME_POST_TRATTAMENTO");
        effVO.setVolumePostTrattamento(volTratt);
        
        BigDecimal volDich = rs.getBigDecimal("VOLUME_POST_DICHIARATO");
        if(Validator.isNotEmpty(volDich))
        {
          effVO.setVolumePostDichiarato(volDich);
        }
        else
        {
          effVO.setVolumePostDichiarato(volTratt);
        }
        
        //BigDecimal azTratt = rs.getBigDecimal("AZOTO_POST_TRATTAMENTO");
        effVO.setAzotoPostTrattamento(rs.getBigDecimal("AZOTO_POST_TRATTAMENTO"));
        BigDecimal azTrattDec = rs.getBigDecimal("AZOTO_POST_TRATTAMENTO_DEC");
        effVO.setAzotoPostTrattamentoDec(azTrattDec);
        
        BigDecimal azDich = rs.getBigDecimal("AZOTO_POST_DICHIARATO");
        
        if(Validator.isNotEmpty(azDich))
        {
          effVO.setAzotoPostDichiarato(azDich);
        }
        else
        {
          effVO.setAzotoPostDichiarato(azTrattDec);
        }
        
        effVO.setVolumeCessione(rs.getBigDecimal("VOLUME_CESSIONE"));
        effVO.setAzotoCessione(rs.getBigDecimal("AZOTO_CESSIONE"));
        effVO.setVolumeAcquisizione(rs.getBigDecimal("VOLUME_ACQUISIZIONE"));
        effVO.setAzotoAcquisizione(rs.getBigDecimal("AZOTO_ACQUISIZIONE"));
        effVO.setStocDispGg(rs.getBigDecimal("STOC_DISPONIBILE_GG"));
        effVO.setStocNecVol(rs.getBigDecimal("STOC_NECESSARIO_VOL"));
        effVO.setStocNecGg(rs.getBigDecimal("STOC_NECESSARIO_GG"));
        effVO.setVolumeCessioneStoccato(rs.getBigDecimal("VOLUME_CESSIONE_STOCCATO"));
        effVO.setVolumeAcquisizioneStoccato(rs.getBigDecimal("VOLUME_ACQUISIZIONE_STOCCATO"));
        effVO.setVolumeInizialeCon(rs.getBigDecimal("VOLUME_INIZIALE_CON"));
        effVO.setVolumePostTrattamentoCon(rs.getBigDecimal("VOLUME_POST_TRATTAMENTO_CON"));
        
        vEffluenti.add(effVO);
      }
      return vEffluenti;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vEffluenti", vEffluenti) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idComunicazione10R", idComunicazione10R)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::getListEffluenti] ",
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
              "[Comunicazione10RGaaDAO::getListEffluenti] END.");
    }
  }
  
  
  public Long getIdEffluenteByLegameAndTipo(long idEffluente, String palabile) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idEffluenteTrattato = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[Comunicazione10RGaaDAO::getIdEffluenteByLegameAndTipo] BEGIN.");

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT LE.ID_EFFLUENTE_TRATTATO " + 
        "FROM   DB_TIPO_EFFLUENTE TE,   " +
        "       DB_LEGAME_EFFLUENTE LE " +
        "WHERE  LE.ID_EFFLUENTE  = ?  " +
        "AND    LE.ID_EFFLUENTE_TRATTATO = TE.ID_EFFLUENTE " +
        "AND    TE.FLAG_PALABILE = ? " +
        "ORDER BY TE.DESCRIZIONE ");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::getIdEffluenteByLegameAndTipo] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;
      stmt.setLong(++indice, idEffluente);
      stmt.setString(++indice, palabile);
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        idEffluenteTrattato = new Long(rs.getLong("ID_EFFLUENTE_TRATTATO"));
        
      }
      return idEffluenteTrattato;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("idEffluenteTrattato", idEffluenteTrattato)};
      
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idEffluente", idEffluente)};
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::getIdEffluenteByLegameAndTipo] ",
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
              "[Comunicazione10RGaaDAO::getIdEffluenteByLegameAndTipo] END.");
    }
  }
  
  
  
  
  
  public Vector<RefluoEffluenteVO> getRefluiComunocazione10r(long idUte, long idComunicazione10r, 
      Date dataInserimentoDichiarazione) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<RefluoEffluenteVO> vReflui= null;
    RefluoEffluenteVO refluo = null; 
  
    try
    {
      SolmrLogger
        .debug(this,
            "[Comunicazione10RGaaDAO::getRefluiComunocazione10r] BEGIN.");

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
    
      queryBuf = new StringBuffer();
      queryBuf.append(
        "WITH EFFLUENTE_PRODOTTO AS " +
        "  (SELECT EP.ID_EFFLUENTE, " +
        "          SUM(VOLUME_PRODOTTO) AS VOL_PROD, " +
        "          SUM(AZOTO_PRODOTTO) AS AZ_PROD " +  
        "   FROM   DB_EFFLUENTE_PRODOTTO EP, " +
        "          DB_STABULAZIONE_TRATTAMENTO ST, " +
        "          DB_SOTTOCATEGORIA_ALLEVAMENTO SA, " + 
        "          DB_CATEGORIE_ALLEVAMENTO CA, " +
        "          DB_ALLEVAMENTO AL " +
        "WHERE     AL.ID_UTE = ? " +
        "AND       EP.ID_STABULAZIONE_TRATTAMENTO = ST.ID_STABULAZIONE_TRATTAMENTO " +
        "AND       ST.ID_SOTTOCATEGORIA_ALLEVAMENTO = SA.ID_SOTTOCATEGORIA_ALLEVAMENTO " +
        "AND       SA.ID_CATEGORIE_ALLEVAMENTO = CA.ID_CATEGORIE_ALLEVAMENTO " +
        "AND       CA.ID_ALLEVAMENTO = AL.ID_ALLEVAMENTO " +
        "AND       EP.FLAG_TRATTAMENTO = 'N' ");
      if(Validator.isEmpty(dataInserimentoDichiarazione))
      {
        queryBuf.append(
        "AND AL.DATA_FINE IS NULL ");
      }
      else
      {
        queryBuf.append(
        "AND  AL.DATA_INIZIO <= ? " + 
        "AND  NVL(AL.DATA_FINE, TO_DATE('31/12/9999','DD/MM/YYYY')) > ? ");
      }
      queryBuf.append(
        "GROUP BY EP.ID_EFFLUENTE ) " +
        "SELECT EFF10.ID_EFFLUENTE, " +
        "       TE.DESCRIZIONE, " +
        "       TE.FLAG_PALABILE, " +
        "       NVL(EFFP.VOL_PROD,0) AS VOL_PROD, " +
        "       NVL(EFFP.AZ_PROD,0) AS AZ_PROD, " +
        "       EFF10.ID_COMUNICAZIONE_10R, " +
        "       NVL((SELECT SUM(CESACQ.QUANTITA) " +
        "        FROM   DB_EFFLUENTE_CES_ACQ_10R CESACQ " +
        "        WHERE  CESACQ.ID_COMUNICAZIONE_10R = EFF10.ID_COMUNICAZIONE_10R " +
        "        AND    CESACQ.ID_EFFLUENTE = EFF10.ID_EFFLUENTE " +
        "        AND    CESACQ.ID_CAUSALE_EFFLUENTE = 1 " +
        "        GROUP BY CESACQ.ID_EFFLUENTE),0) AS VOLUME_CESSIONE, " +
        "       NVL((SELECT SUM(CESACQ.QUANTITA_AZOTO_DICHIARATO) " +
        "        FROM   DB_EFFLUENTE_CES_ACQ_10R CESACQ " +
        "        WHERE  CESACQ.ID_COMUNICAZIONE_10R = EFF10.ID_COMUNICAZIONE_10R " +
        "        AND    CESACQ.ID_EFFLUENTE = EFF10.ID_EFFLUENTE " +
        "        AND    CESACQ.ID_CAUSALE_EFFLUENTE = 1 " +
        "        GROUP BY CESACQ.ID_EFFLUENTE),0) AS AZOTO_CESSIONE, " +
        "       NVL((SELECT SUM(CESACQ.QUANTITA) " +
        "        FROM   DB_EFFLUENTE_CES_ACQ_10R CESACQ " +
        "        WHERE  CESACQ.ID_COMUNICAZIONE_10R = EFF10.ID_COMUNICAZIONE_10R " +
        "        AND    CESACQ.ID_EFFLUENTE = EFF10.ID_EFFLUENTE " +
        "        AND    CESACQ.ID_CAUSALE_EFFLUENTE = 2 " +
        "        GROUP BY CESACQ.ID_EFFLUENTE),0) AS VOLUME_ACQUISIZIONE, " +
        "       NVL((SELECT SUM(CESACQ.QUANTITA_AZOTO_DICHIARATO) " +
        "        FROM   DB_EFFLUENTE_CES_ACQ_10R CESACQ " +
        "        WHERE  CESACQ.ID_COMUNICAZIONE_10R = EFF10.ID_COMUNICAZIONE_10R " +
        "        AND    CESACQ.ID_EFFLUENTE = EFF10.ID_EFFLUENTE " +
        "        AND    CESACQ.ID_CAUSALE_EFFLUENTE = 2 " +
        "        GROUP BY CESACQ.ID_EFFLUENTE),0) AS AZOTO_ACQUISIZIONE, " + 
        "       NVL(EFF10.VOLUME_POST_TRATTAMENTO,0) AS VOLUME_POST_TRATTAMENTO, " + 
        "       NVL(EFF10.AZOTO_POST_TRATTAMENTO,0) AS AZOTO_POST_TRATTAMENTO, " +
        "       NVL(EFF10.VOLUME_POST_DICHIARATO,0) AS VOLUME_POST_DICHIARATO, " +
        "       NVL(EFF10.AZOTO_POST_DICHIARATO,0) AS AZOTO_POST_DICHIARATO, " +
        "       NVL(EFF10.VOLUME_INIZIALE,0) AS VOLUME_INIZIALE, " +
        "       NVL(EFF10.AZOTO_INIZIALE,0) AS AZOTO_INIZIALE, " +
        "       EFF10.ID_TRATTAMENTO, " +
        "       NVL((SELECT EFF102.VOLUME_INIZIALE " +
        "        FROM   DB_EFFLUENTE_10R EFF102 " +
        "        WHERE  EFF102.ID_COMUNICAZIONE_10R = EFF10.ID_COMUNICAZIONE_10R " +
        "        AND    EFF102.ID_EFFLUENTE_ORIGINE = EFF10.ID_EFFLUENTE " +
        "        AND    ROWNUM = 1),0) AS VOLUME_TRATTATO, " +
        "       NVL((SELECT EFF102.AZOTO_INIZIALE " +
        "        FROM   DB_EFFLUENTE_10R EFF102 " +
        "        WHERE  EFF102.ID_COMUNICAZIONE_10R = EFF10.ID_COMUNICAZIONE_10R " +
        "        AND    EFF102.ID_EFFLUENTE_ORIGINE = EFF10.ID_EFFLUENTE " +
        "        AND    ROWNUM = 1),0) AS AZOTO_TRATTATO " +
        "FROM   DB_EFFLUENTE_10R EFF10, " +
        "       DB_TIPO_EFFLUENTE TE, " +
        "       EFFLUENTE_PRODOTTO EFFP " +
        "WHERE  EFF10.ID_COMUNICAZIONE_10R = ? " + 
        "AND    EFF10.ID_EFFLUENTE = TE.ID_EFFLUENTE " +
        "AND    EFF10.ID_EFFLUENTE = EFFP.ID_EFFLUENTE (+) " + 
        "ORDER BY TE.FLAG_PALABILE DESC, TE.DESCRIZIONE ");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::getRefluiComunocazione10r] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;
      stmt.setLong(++indice, idUte);
      if(Validator.isNotEmpty(dataInserimentoDichiarazione))
      {
        stmt.setTimestamp(++indice, convertDateToTimestamp(dataInserimentoDichiarazione));
        stmt.setTimestamp(++indice, convertDateToTimestamp(dataInserimentoDichiarazione));
      }
      stmt.setLong(++indice, idComunicazione10r);
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      long idEffluenteTmp = 0;
      BigDecimal volPostTrattamentoDich = new BigDecimal(0);
      BigDecimal azotoPostTrattamentoDich = new BigDecimal(0);
      while(rs.next())
      {
        if(vReflui == null)
        {
          vReflui = new Vector<RefluoEffluenteVO>();
        }  
        
        
        long idEffluente = rs.getLong("ID_EFFLUENTE");
        if(idEffluente != idEffluenteTmp)
        {
          if(idEffluenteTmp != 0)
          {
            refluo.setVolumePostTrattamentoDich(volPostTrattamentoDich);
            refluo.setAzotoPostTrattamentoDich(azotoPostTrattamentoDich);
            
            if(Validator.isNotEmpty(refluo.getIdTrattamento()))
            {
              BigDecimal volumeUtilizzoAgronomico = refluo.getVolumeAcquisito();
              volumeUtilizzoAgronomico = volumeUtilizzoAgronomico.add(refluo.getVolumePostTrattamentoDich());
              volumeUtilizzoAgronomico = volumeUtilizzoAgronomico.subtract(refluo.getVolumeCeduto());       
              refluo.setVolumeUtilizzoAgronomico(volumeUtilizzoAgronomico);
              
              BigDecimal azotoUtilizzoAgronomico = refluo.getAzotoAcquisito();
              azotoUtilizzoAgronomico = azotoUtilizzoAgronomico.add(refluo.getAzotoPostTrattamentoDich());
              azotoUtilizzoAgronomico = azotoUtilizzoAgronomico.subtract(refluo.getAzotoCeduto());       
              refluo.setAzotoUtilizzoAgronomico(azotoUtilizzoAgronomico);
            }
            else
            {          
              
              BigDecimal volumeUtilizzoAgronomico = refluo.getVolumeIniziale();
              volumeUtilizzoAgronomico = volumeUtilizzoAgronomico.add(refluo.getVolumeAcquisito());
              volumeUtilizzoAgronomico = volumeUtilizzoAgronomico.subtract(refluo.getVolumeCeduto());
              volumeUtilizzoAgronomico = volumeUtilizzoAgronomico.subtract(refluo.getVolumeTrattato());
              refluo.setVolumeUtilizzoAgronomico(volumeUtilizzoAgronomico);
              
              BigDecimal azotoUtilizzoAgronomico = refluo.getAzotoIniziale();
              azotoUtilizzoAgronomico = azotoUtilizzoAgronomico.add(refluo.getAzotoAcquisito());
              azotoUtilizzoAgronomico = azotoUtilizzoAgronomico.subtract(refluo.getAzotoCeduto());
              azotoUtilizzoAgronomico = azotoUtilizzoAgronomico.subtract(refluo.getAzotoTrattato());
              refluo.setAzotoUtilizzoAgronomico(azotoUtilizzoAgronomico);
              
            }
            
            vReflui.add(refluo);
            
            volPostTrattamentoDich = new BigDecimal(0);
            azotoPostTrattamentoDich = new BigDecimal(0);
            
          }
          
          refluo = new RefluoEffluenteVO();
          refluo.setIdEffluente(idEffluente);
          refluo.setDescrizione(rs.getString("DESCRIZIONE"));
          String tipoEffluente = "Palabile";
          if("N".equalsIgnoreCase(rs.getString("FLAG_PALABILE")))
            tipoEffluente = "Non palabile";
          refluo.setTipoEffluente(tipoEffluente);
          refluo.setVolumeEffProdStalla(rs.getBigDecimal("VOL_PROD"));
          refluo.setAzotoEffProdStalla(rs.getBigDecimal("AZ_PROD"));
          refluo.setVolumeCeduto(rs.getBigDecimal("VOLUME_CESSIONE"));
          refluo.setAzotoCeduto(rs.getBigDecimal("AZOTO_CESSIONE"));
          refluo.setVolumeAcquisito(rs.getBigDecimal("VOLUME_ACQUISIZIONE"));
          refluo.setAzotoAcquisito(rs.getBigDecimal("AZOTO_ACQUISIZIONE"));
          refluo.setVolumePostTrattamentoCalc(rs.getBigDecimal("VOLUME_POST_TRATTAMENTO"));
          refluo.setAzotoPostTrattamentoCalc(rs.getBigDecimal("AZOTO_POST_TRATTAMENTO"));
          refluo.setIdTrattamento(checkLongNull(rs.getString("ID_TRATTAMENTO")));
          refluo.setVolumeIniziale(rs.getBigDecimal("VOLUME_INIZIALE"));
          refluo.setAzotoIniziale(rs.getBigDecimal("AZOTO_INIZIALE"));
          refluo.setVolumeTrattato(rs.getBigDecimal("VOLUME_TRATTATO"));
          refluo.setAzotoTrattato(rs.getBigDecimal("AZOTO_TRATTATO"));
          
          idEffluenteTmp = idEffluente;
          
        }
        
        if(Validator.isNotEmpty(rs.getBigDecimal("VOLUME_POST_DICHIARATO")))
          volPostTrattamentoDich = volPostTrattamentoDich.add(rs.getBigDecimal("VOLUME_POST_DICHIARATO"));
        if(Validator.isNotEmpty(rs.getBigDecimal("AZOTO_POST_DICHIARATO")))
          azotoPostTrattamentoDich = azotoPostTrattamentoDich.add(rs.getBigDecimal("AZOTO_POST_DICHIARATO"));
        
      }
      
      if(vReflui != null)
      {
        refluo.setVolumePostTrattamentoDich(volPostTrattamentoDich);
        refluo.setAzotoPostTrattamentoDich(azotoPostTrattamentoDich);
        
        if(Validator.isNotEmpty(refluo.getIdTrattamento()))
        {
          BigDecimal volumeUtilizzoAgronomico = refluo.getVolumeAcquisito();
          volumeUtilizzoAgronomico = volumeUtilizzoAgronomico.add(refluo.getVolumePostTrattamentoDich());
          volumeUtilizzoAgronomico = volumeUtilizzoAgronomico.subtract(refluo.getVolumeCeduto());       
          refluo.setVolumeUtilizzoAgronomico(volumeUtilizzoAgronomico);
          
          BigDecimal azotoUtilizzoAgronomico = refluo.getAzotoAcquisito();
          azotoUtilizzoAgronomico = azotoUtilizzoAgronomico.add(refluo.getAzotoPostTrattamentoDich());
          azotoUtilizzoAgronomico = azotoUtilizzoAgronomico.subtract(refluo.getAzotoCeduto());       
          refluo.setAzotoUtilizzoAgronomico(azotoUtilizzoAgronomico);
        }
        else
        {          
          
          BigDecimal volumeUtilizzoAgronomico = refluo.getVolumeIniziale();
          volumeUtilizzoAgronomico = volumeUtilizzoAgronomico.add(refluo.getVolumeAcquisito());
          volumeUtilizzoAgronomico = volumeUtilizzoAgronomico.subtract(refluo.getVolumeCeduto());
          volumeUtilizzoAgronomico = volumeUtilizzoAgronomico.subtract(refluo.getVolumeTrattato());
          refluo.setVolumeUtilizzoAgronomico(volumeUtilizzoAgronomico);
          
          BigDecimal azotoUtilizzoAgronomico = refluo.getAzotoIniziale();
          azotoUtilizzoAgronomico = azotoUtilizzoAgronomico.add(refluo.getAzotoAcquisito());
          azotoUtilizzoAgronomico = azotoUtilizzoAgronomico.subtract(refluo.getAzotoCeduto());
          azotoUtilizzoAgronomico = azotoUtilizzoAgronomico.subtract(refluo.getAzotoTrattato());
          refluo.setAzotoUtilizzoAgronomico(azotoUtilizzoAgronomico);
          
        }
        
        vReflui.add(refluo);
        
      }
      
      return vReflui;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("refluo", refluo),
          new Variabile("vReflui", vReflui)
      };
      
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUte", idUte),
          new Parametro("idComunicazione10r", idComunicazione10r),
          new Parametro("dataInserimentoDichiarazione", dataInserimentoDichiarazione)  };
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::getRefluiComunocazione10r] ",
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
              "[Comunicazione10RGaaDAO::getRefluiComunocazione10r] END.");
    }
  } 

  
  
  public Vector<RefluoEffluenteVO> getListRefluiStampa(long idComunicazione10R[]) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<RefluoEffluenteVO> vReflui = null;
    RefluoEffluenteVO refluo = null;
      
    try
    {
      SolmrLogger.debug(this,
                "[Comunicazione10RGaaDAO::getListRefluiStampa] BEGIN.");
    
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
        
      String parametri=" (";
        
      if (idComunicazione10R!=null && idComunicazione10R.length!=0)
      {
        for(int i=0;i<idComunicazione10R.length;i++)
        {
          parametri+="?";
          if (i<idComunicazione10R.length-1)
            parametri+=",";
        }
      }
      parametri+=") ";
        
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT EFF10.ID_EFFLUENTE, " +
        "       TE.DESCRIZIONE, " +
        "       TE.FLAG_PALABILE, " +
        "       EFF10.ID_COMUNICAZIONE_10R, " + 
        "       NVL((SELECT SUM(CESACQ.QUANTITA) " +
        "        FROM   DB_EFFLUENTE_CES_ACQ_10R CESACQ " +
        "        WHERE  CESACQ.ID_COMUNICAZIONE_10R = EFF10.ID_COMUNICAZIONE_10R " +
        "        AND    CESACQ.ID_EFFLUENTE = EFF10.ID_EFFLUENTE " +
        "        AND    CESACQ.ID_CAUSALE_EFFLUENTE = 1 " +
        "        GROUP BY CESACQ.ID_EFFLUENTE),0) AS VOLUME_CESSIONE, " +
        "       NVL((SELECT SUM(CESACQ.QUANTITA_AZOTO_DICHIARATO) " +
        "        FROM   DB_EFFLUENTE_CES_ACQ_10R CESACQ " +
        "        WHERE  CESACQ.ID_COMUNICAZIONE_10R = EFF10.ID_COMUNICAZIONE_10R " +
        "        AND    CESACQ.ID_EFFLUENTE = EFF10.ID_EFFLUENTE " +
        "        AND    CESACQ.ID_CAUSALE_EFFLUENTE = 1 " +
        "        GROUP BY CESACQ.ID_EFFLUENTE),0) AS AZOTO_CESSIONE, " +
        "       NVL((SELECT SUM(CESACQ.QUANTITA) " +
        "        FROM   DB_EFFLUENTE_CES_ACQ_10R CESACQ " +
        "        WHERE  CESACQ.ID_COMUNICAZIONE_10R = EFF10.ID_COMUNICAZIONE_10R " +
        "        AND    CESACQ.ID_EFFLUENTE = EFF10.ID_EFFLUENTE " +
        "        AND    CESACQ.ID_CAUSALE_EFFLUENTE = 2 " +
        "        GROUP BY CESACQ.ID_EFFLUENTE),0) AS VOLUME_ACQUISIZIONE, " +
        "       NVL((SELECT SUM(CESACQ.QUANTITA_AZOTO_DICHIARATO) " +
        "        FROM   DB_EFFLUENTE_CES_ACQ_10R CESACQ " +
        "        WHERE  CESACQ.ID_COMUNICAZIONE_10R = EFF10.ID_COMUNICAZIONE_10R " +
        "        AND    CESACQ.ID_EFFLUENTE = EFF10.ID_EFFLUENTE " +
        "        AND    CESACQ.ID_CAUSALE_EFFLUENTE = 2 " +
        "        GROUP BY CESACQ.ID_EFFLUENTE),0) AS AZOTO_ACQUISIZIONE, " +
        //"       NVL(EFF10.VOLUME_CESSIONE,0) AS VOLUME_CESSIONE, " + 
        //"       NVL(EFF10.AZOTO_CESSIONE,0) AS AZOTO_CESSIONE, " +
        //"       NVL(EFF10.VOLUME_ACQUISIZIONE,0) AS VOLUME_ACQUISIZIONE, " + 
        //"       NVL(EFF10.AZOTO_ACQUISIZIONE,0) AS AZOTO_ACQUISIZIONE, " +
        "       NVL(EFF10.VOLUME_POST_TRATTAMENTO,0) AS VOLUME_POST_TRATTAMENTO, " + 
        "       NVL(EFF10.AZOTO_POST_TRATTAMENTO,0) AS AZOTO_POST_TRATTAMENTO, " +
        "       NVL(EFF10.VOLUME_POST_DICHIARATO,0) AS VOLUME_POST_DICHIARATO, " +
        "       NVL(EFF10.AZOTO_POST_DICHIARATO,0) AS AZOTO_POST_DICHIARATO, " +
        "       NVL(EFF10.VOLUME_INIZIALE,0) AS VOLUME_INIZIALE, " +
        "       NVL(EFF10.AZOTO_INIZIALE,0) AS AZOTO_INIZIALE, " +
        "       EFF10.ID_TRATTAMENTO, " +
        "       NVL((SELECT EFF102.VOLUME_INIZIALE " +
        "        FROM   DB_EFFLUENTE_10R EFF102 " +
        "        WHERE  EFF102.ID_COMUNICAZIONE_10R = EFF10.ID_COMUNICAZIONE_10R " +
        "        AND    EFF102.ID_EFFLUENTE_ORIGINE = EFF10.ID_EFFLUENTE " +
        "        AND    ROWNUM = 1),0) AS VOLUME_TRATTATO, " +
        "       NVL((SELECT EFF102.AZOTO_INIZIALE " +
        "        FROM   DB_EFFLUENTE_10R EFF102 " +
        "        WHERE  EFF102.ID_COMUNICAZIONE_10R = EFF10.ID_COMUNICAZIONE_10R " +
        "        AND    EFF102.ID_EFFLUENTE_ORIGINE = EFF10.ID_EFFLUENTE " +
        "        AND    ROWNUM = 1),0) AS AZOTO_TRATTATO, " +
        "       U.INDIRIZZO, "+
        "       CUTE.DESCOM AS UT_DESCOM, " +
        "       PUTE.SIGLA_PROVINCIA AS UT_SIGLA_PROVINCIA " +
        "FROM   DB_EFFLUENTE_10R EFF10, " +
        "       DB_TIPO_EFFLUENTE TE, " +
        "       DB_COMUNICAZIONE_10R COM10, " +
        "       DB_UTE U, " +
        "       COMUNE CUTE, " +
        "       PROVINCIA PUTE "+
        "WHERE  EFF10.ID_COMUNICAZIONE_10R IN  " +parametri +" "+
        "AND    EFF10.ID_EFFLUENTE = TE.ID_EFFLUENTE " +
        "AND    EFF10.ID_COMUNICAZIONE_10R = COM10.ID_COMUNICAZIONE_10R " +
        "AND    COM10.ID_UTE = U.ID_UTE " +
        "AND    U.COMUNE = CUTE.ISTAT_COMUNE "+
        "AND    CUTE.ISTAT_PROVINCIA = PUTE.ISTAT_PROVINCIA "+
        "ORDER BY CUTE.DESCOM, TE.FLAG_PALABILE DESC, TE.DESCRIZIONE ");

        
        
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::getListRefluiStampa] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      if (idComunicazione10R!=null && idComunicazione10R.length!=0)
      {
        for(int i=0;i<idComunicazione10R.length;i++)
          stmt.setLong(++indice,idComunicazione10R[i]);
      }
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      long idEffluenteTmp = 0;
      long idCom10Rtmp= 0;
      BigDecimal volPostTrattamentoDich = new BigDecimal(0);
      BigDecimal azotoPostTrattamentoDich = new BigDecimal(0);
      while(rs.next())
      {
        if(vReflui == null)
        {
          vReflui = new Vector<RefluoEffluenteVO>();
        }  
        
        
        long idEffluente = rs.getLong("ID_EFFLUENTE");
        long idCom10R = rs.getLong("ID_COMUNICAZIONE_10R");
        if((idEffluente != idEffluenteTmp) || (idCom10Rtmp != idCom10R))
        {
          if(idEffluenteTmp != 0)
          {
            refluo.setVolumePostTrattamentoDich(volPostTrattamentoDich);
            refluo.setAzotoPostTrattamentoDich(azotoPostTrattamentoDich);
            
            if(Validator.isNotEmpty(refluo.getIdTrattamento()))
            {
              BigDecimal volumeUtilizzoAgronomico = refluo.getVolumeAcquisito();
              volumeUtilizzoAgronomico = volumeUtilizzoAgronomico.add(refluo.getVolumePostTrattamentoDich());
              volumeUtilizzoAgronomico = volumeUtilizzoAgronomico.subtract(refluo.getVolumeCeduto());       
              refluo.setVolumeUtilizzoAgronomico(volumeUtilizzoAgronomico);
              
              BigDecimal azotoUtilizzoAgronomico = refluo.getAzotoAcquisito();
              azotoUtilizzoAgronomico = azotoUtilizzoAgronomico.add(refluo.getAzotoPostTrattamentoDich());
              azotoUtilizzoAgronomico = azotoUtilizzoAgronomico.subtract(refluo.getAzotoCeduto());       
              refluo.setAzotoUtilizzoAgronomico(azotoUtilizzoAgronomico);
            }
            else
            {          
              
              BigDecimal volumeUtilizzoAgronomico = refluo.getVolumeIniziale();
              volumeUtilizzoAgronomico = volumeUtilizzoAgronomico.add(refluo.getVolumeAcquisito());
              volumeUtilizzoAgronomico = volumeUtilizzoAgronomico.subtract(refluo.getVolumeCeduto());
              volumeUtilizzoAgronomico = volumeUtilizzoAgronomico.subtract(refluo.getVolumeTrattato());
              refluo.setVolumeUtilizzoAgronomico(volumeUtilizzoAgronomico);
              
              BigDecimal azotoUtilizzoAgronomico = refluo.getAzotoIniziale();
              azotoUtilizzoAgronomico = azotoUtilizzoAgronomico.add(refluo.getAzotoAcquisito());
              azotoUtilizzoAgronomico = azotoUtilizzoAgronomico.subtract(refluo.getAzotoCeduto());
              azotoUtilizzoAgronomico = azotoUtilizzoAgronomico.subtract(refluo.getAzotoTrattato());
              refluo.setAzotoUtilizzoAgronomico(azotoUtilizzoAgronomico);
              
            }
            
            vReflui.add(refluo);
            
            volPostTrattamentoDich = new BigDecimal(0);
            azotoPostTrattamentoDich = new BigDecimal(0);
            
          }
          
          refluo = new RefluoEffluenteVO();
          refluo.setIdEffluente(idEffluente);
          refluo.setDescrizione(rs.getString("DESCRIZIONE"));
          String tipoEffluente = "Palabile";
          if("N".equalsIgnoreCase(rs.getString("FLAG_PALABILE")))
            tipoEffluente = "Non palabile";
          refluo.setTipoEffluente(tipoEffluente);
          refluo.setVolumeCeduto(rs.getBigDecimal("VOLUME_CESSIONE"));
          refluo.setAzotoCeduto(rs.getBigDecimal("AZOTO_CESSIONE"));
          refluo.setVolumeAcquisito(rs.getBigDecimal("VOLUME_ACQUISIZIONE"));
          refluo.setAzotoAcquisito(rs.getBigDecimal("AZOTO_ACQUISIZIONE"));
          refluo.setVolumePostTrattamentoCalc(rs.getBigDecimal("VOLUME_POST_TRATTAMENTO"));
          refluo.setAzotoPostTrattamentoCalc(rs.getBigDecimal("AZOTO_POST_TRATTAMENTO"));
          refluo.setIdTrattamento(checkLongNull(rs.getString("ID_TRATTAMENTO")));
          refluo.setVolumeIniziale(rs.getBigDecimal("VOLUME_INIZIALE"));
          refluo.setAzotoIniziale(rs.getBigDecimal("AZOTO_INIZIALE"));
          refluo.setVolumeTrattato(rs.getBigDecimal("VOLUME_TRATTATO"));
          refluo.setAzotoTrattato(rs.getBigDecimal("AZOTO_TRATTATO"));
          refluo.setIndirizzoUte(rs.getString("INDIRIZZO"));
          refluo.setComuneUte(rs.getString("UT_DESCOM"));
          refluo.setSglProvUte(rs.getString("UT_SIGLA_PROVINCIA"));
          
          idEffluenteTmp = idEffluente;
          idCom10Rtmp = idCom10R;
          
        }
        
        if(Validator.isNotEmpty(rs.getBigDecimal("VOLUME_POST_DICHIARATO")))
          volPostTrattamentoDich = volPostTrattamentoDich.add(rs.getBigDecimal("VOLUME_POST_DICHIARATO"));
        if(Validator.isNotEmpty(rs.getBigDecimal("AZOTO_POST_DICHIARATO")))
          azotoPostTrattamentoDich = azotoPostTrattamentoDich.add(rs.getBigDecimal("AZOTO_POST_DICHIARATO"));
        
      }
      
      if(vReflui != null)
      {
        refluo.setVolumePostTrattamentoDich(volPostTrattamentoDich);
        refluo.setAzotoPostTrattamentoDich(azotoPostTrattamentoDich);
        
        if(Validator.isNotEmpty(refluo.getIdTrattamento()))
        {
          BigDecimal volumeUtilizzoAgronomico = refluo.getVolumeAcquisito();
          volumeUtilizzoAgronomico = volumeUtilizzoAgronomico.add(refluo.getVolumePostTrattamentoDich());
          volumeUtilizzoAgronomico = volumeUtilizzoAgronomico.subtract(refluo.getVolumeCeduto());       
          refluo.setVolumeUtilizzoAgronomico(volumeUtilizzoAgronomico);
          
          BigDecimal azotoUtilizzoAgronomico = refluo.getAzotoAcquisito();
          azotoUtilizzoAgronomico = azotoUtilizzoAgronomico.add(refluo.getAzotoPostTrattamentoDich());
          azotoUtilizzoAgronomico = azotoUtilizzoAgronomico.subtract(refluo.getAzotoCeduto());       
          refluo.setAzotoUtilizzoAgronomico(azotoUtilizzoAgronomico);
        }
        else
        {          
          
          BigDecimal volumeUtilizzoAgronomico = refluo.getVolumeIniziale();
          volumeUtilizzoAgronomico = volumeUtilizzoAgronomico.add(refluo.getVolumeAcquisito());
          volumeUtilizzoAgronomico = volumeUtilizzoAgronomico.subtract(refluo.getVolumeCeduto());
          volumeUtilizzoAgronomico = volumeUtilizzoAgronomico.subtract(refluo.getVolumeTrattato());
          refluo.setVolumeUtilizzoAgronomico(volumeUtilizzoAgronomico);
          
          BigDecimal azotoUtilizzoAgronomico = refluo.getAzotoIniziale();
          azotoUtilizzoAgronomico = azotoUtilizzoAgronomico.add(refluo.getAzotoAcquisito());
          azotoUtilizzoAgronomico = azotoUtilizzoAgronomico.subtract(refluo.getAzotoCeduto());
          azotoUtilizzoAgronomico = azotoUtilizzoAgronomico.subtract(refluo.getAzotoTrattato());
          refluo.setAzotoUtilizzoAgronomico(azotoUtilizzoAgronomico);
          
        }
        
        vReflui.add(refluo);
        
      }
      
      return vReflui;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vReflui", vReflui),
          new Variabile("refluo", refluo) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idComunicazione10R", idComunicazione10R) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::getListRefluiStampa] ",
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
              "[Comunicazione10RGaaDAO::getListRefluiStampa] END.");
    }
  }
  
  
  public PlSqlCodeDescription calcolaVolumePioggeM3PlSql(long idUte) 
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
              "[Comunicazione10RGaaDAO::calcolaVolumePioggeM3PlSql] BEGIN.");
      /***
       * PROCEDURE CALCOLA_VOLUME_PIOGGIE_M3 (  pIdUte          IN NUMBER,
                                                pSuperficeScoperta  OUT NUMBER,
                                                pCodErr           OUT VARCHAR2,
                                                pDesErr         OUT VARCHAR2) IS
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{call PACK_COMUNICAZIONE_10R.CALCOLA_VOLUME_PIOGGIE_M3(?,?,?,?)}");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::calcolaVolumePioggeM3PlSql] Query="
                + query);
      }
      
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.setLong(1, idUte);
      cs.registerOutParameter(2,Types.DECIMAL); //quantita
      cs.registerOutParameter(3,Types.VARCHAR); //codice errore
      cs.registerOutParameter(4,Types.VARCHAR); //msg errore
      
      cs.executeUpdate();
      
      plqObj = new PlSqlCodeDescription();  
      plqObj.setDescription(cs.getString(3)); //codice errore
      plqObj.setOtherdescription(cs.getString(4)); //msg errore
      plqObj.setItem(cs.getBigDecimal(2)); //quantita
      
      
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
      { new Parametro("idUte", idUte)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::calcolaVolumePioggeM3PlSql] ",
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
              "[Comunicazione10RGaaDAO::calcolaVolumePioggeM3PlSql] END.");
    }
  }
  
  
  public PlSqlCodeDescription calcolaAcqueMungituraPlSql(long idUte) 
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
              "[Comunicazione10RGaaDAO::calcolaAcqueMungituraPlSql] BEGIN.");
      /***
       * PROCEDURE CALCOLA_ACQUE_MUNGITURA (  pIdUte          IN NUMBER,
                                              pTotMungitura     OUT NUMBER,
                                              pCodErr           OUT VARCHAR2,
                                              pDesErr         OUT VARCHAR2) IS
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{call PACK_COMUNICAZIONE_10R.CALCOLA_ACQUE_MUNGITURA(?,?,?,?)}");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::calcolaAcqueMungituraPlSql] Query="
                + query);
      }
      
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.setLong(1, idUte);
      cs.registerOutParameter(2,Types.DECIMAL); //quantita
      cs.registerOutParameter(3,Types.VARCHAR); //codice errore
      cs.registerOutParameter(4,Types.VARCHAR); //msg errore
      
      cs.executeUpdate();
      
      plqObj = new PlSqlCodeDescription();  
      plqObj.setDescription(cs.getString(3)); //codice errore
      plqObj.setOtherdescription(cs.getString(4)); //msg errore
      plqObj.setItem(cs.getBigDecimal(2)); //quantita
      
      
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
      { new Parametro("idUte", idUte)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::calcolaAcqueMungituraPlSql] ",
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
              "[Comunicazione10RGaaDAO::calcolaAcqueMungituraPlSql] END.");
    }
  }
  
  
  public PlSqlCodeDescription calcolaCesAcquisizionePlSql(long idComunicazione10r) 
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
              "[Comunicazione10RGaaDAO::calcolaCesAcquisizionePlSql] BEGIN.");
      /***
       * PROCEDURE CALCOLA_CES_ACQUISIZIONE   (   pIdComunicazione    IN NUMBER,
                                                  pFlagPalabile     IN VARCHAR2 DEFAULT NULL,
                                                  pCausaleEffluente   IN NUMBER,
                                                  pIdEffluente      IN  NUMBER DEFAULT NULL,
                                                  pStockAcqAz       OUT NUMBER,
                                                  pStockAcqAzSto      OUT NUMBER,
                                                  pCodErr           OUT VARCHAR2,
                                                  pDesErr         OUT VARCHAR2) IS
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{call PACK_COMUNICAZIONE_10R.CALCOLA_CES_ACQUISIZIONE(?,?,?,?,?,?,?,?)}");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[Comunicazione10RGaaDAO::calcolaCesAcquisizionePlSql] Query="
                + query);
      }
      
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.setLong(1, idComunicazione10r);
      cs.setString(2, null);
      cs.setInt(3, 1);
      cs.setInt(4, 19);
      cs.registerOutParameter(5,Types.DECIMAL); //quantita
      cs.registerOutParameter(6,Types.DECIMAL); //quantita
      cs.registerOutParameter(7,Types.VARCHAR); //codice errore
      cs.registerOutParameter(8,Types.VARCHAR); //msg errore
      
      cs.executeUpdate();
      
      plqObj = new PlSqlCodeDescription();  
      plqObj.setDescription(cs.getString(7)); //codice errore
      plqObj.setOtherdescription(cs.getString(8)); //msg errore
      plqObj.setItem(cs.getBigDecimal(7)); //quantita
      
      
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
      { new Parametro("idComunicazione10r", idComunicazione10r)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[Comunicazione10RGaaDAO::calcolaCesAcquisizionePlSql] ",
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
              "[Comunicazione10RGaaDAO::calcolaCesAcquisizionePlSql] END.");
    }
  }

  
  
}
