package it.csi.smranag.smrgaa.integration;


import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni;
import it.csi.smranag.smrgaa.dto.PlSqlCodeDescription;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaAtecoSecVO;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaColVarExcelVO;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaSezioniVO;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaUmaExcelVO;
import it.csi.smranag.smrgaa.dto.anagrafe.ColturaUmaVO;
import it.csi.smranag.smrgaa.dto.anagrafe.ColturaVarietaVO;
import it.csi.smranag.smrgaa.dto.anagrafe.FruttaGuscioVO;
import it.csi.smranag.smrgaa.dto.anagrafe.GreeningVO;
import it.csi.smranag.smrgaa.dto.anagrafe.GruppoGreeningVO;
import it.csi.smranag.smrgaa.dto.anagrafe.PlSqlCalcoloOteVO;
import it.csi.smranag.smrgaa.dto.anagrafe.TipoAttivitaOteVO;
import it.csi.smranag.smrgaa.dto.anagrafe.TipoDimensioneAziendaVO;
import it.csi.smranag.smrgaa.dto.anagrafe.TipoInfoAggiuntivaVO;
import it.csi.smranag.smrgaa.dto.anagrafe.TipoUdeVO;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.dto.search.FiltriRicercaAziendeCollegateVO;
import it.csi.smranag.smrgaa.dto.search.FiltriRicercaVariazioniAziendaliVO;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaAziendeCollegateVO;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaVariazioniAziendaliVO;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.DelegaVO;
import it.csi.solmr.dto.anag.SoggettoAssociatoVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.HashMap;
import java.util.Vector;


public class AnagrafeGaaDAO extends it.csi.solmr.integration.BaseDAO {
  
  
  public AnagrafeGaaDAO() throws ResourceAccessException{
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public AnagrafeGaaDAO(String refName) throws ResourceAccessException {
   super(refName);
  }

  /**
   * Calcola del reddito lordo standard, attività OTE e unità dimensione economica
   * passando idAzienda e idUte
   * 
   * 
   * @param idAzienda
   * @param idUte
   * @return
   * @throws DataAccessException
   */
  public PlSqlCalcoloOteVO calcolaIneaPlSql(long idAzienda, 
      Long idUte) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    CallableStatement cs = null;
    StringBuffer queryBuf = null;
    PlSqlCalcoloOteVO plqObj = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[AnagrafeGaaDAO::calcolaIneaPlSql] BEGIN.");
      /***
       *  PROCEDURE CALCOLO_INEA    (    p_id_azienda     IN     DB_AZIENDA.ID_AZIENDA%TYPE, 
                                         p_id_ute         IN     DB_UTE.ID_UTE%TYPE,
                                         rls                 OUT     NUMBER, 
                                         ude                 OUT     DB_TIPO_UDE.ID_UDE%TYPE, 
                                         ote                 OUT     DB_TIPO_ATTIVITA_OTE.ID_ATTIVITA_OTE%TYPE,
                                         returnVal         OUT     NUMBER,
                                         vDescErr            OUT     VARCHAR2)
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{call PACK_INEA.CALCOLO_INEA(?,?,?,?,?,?,?)}");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::calcolaIneaPlSql] Query="
                + query);
      }
      
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.setLong(1, idAzienda);
      if(idUte == null)
      {
        cs.setString(2, null);
      }
      else
      {
        cs.setLong(2, idUte.longValue());
      }
      cs.registerOutParameter(3,Types.DECIMAL); //rls
      cs.registerOutParameter(4,Types.INTEGER); //idUde
      cs.registerOutParameter(5,Types.INTEGER); //idOte
      cs.registerOutParameter(6,Types.INTEGER); //codice errore
      cs.registerOutParameter(7,Types.VARCHAR); //msg errore
      
      cs.executeUpdate();
      
      plqObj = new PlSqlCalcoloOteVO();  
      plqObj.setRls(cs.getBigDecimal(3));
      plqObj.setIdUde(checkLongNull(cs.getString(4)));
      plqObj.setIdAttivitaOte(checkLongNull(cs.getString(5)));
      plqObj.setCodeResult(cs.getLong(6)); 
      plqObj.setDescError(cs.getString(7));
      
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
        new Parametro("idUte", idUte) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AnagrafeGaaDAO::calcolaIneaPlSql] ",
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
              "[AnagrafeGaaDAO::calcolaIneaPlSql] END.");
    }
  }
  
  
  /**
   * 
   * Calcola l'ulu con id_azienda e idUde.
   * idUde passato è preso dal pl CALCOLO_INEA, se è null
   * come valore dell'ulu restituito sarà null!!
   * 
   * 
   * 
   * @param idAzienda
   * @param idUde
   * @return
   * @throws DataAccessException
   */
  public PlSqlCalcoloOteVO calcolaUluPlSql(long idAzienda, 
      Long idUde) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    CallableStatement cs = null;
    StringBuffer queryBuf = null;
    PlSqlCalcoloOteVO plqObj = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[AnagrafeGaaDAO::calcolaUluPlSql] BEGIN.");
      /***
       *  PROCEDURE MAIN (  pIdAzienda  IN  NUMBER,
                            pIdUde    IN  NUMBER,
                            pUlu      OUT   NUMBER,
                            pRetVal     OUT   NUMBER,
                            pDeserr   OUT   VARCHAR2
                         )
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{call PCK_CALCOLO_ULU.MAIN(?,?,?,?,?)}");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::calcolaUluPlSql] Query="
                + query);
      }
      
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.setLong(1, idAzienda);
      if(idUde == null)
      {
        cs.setString(2, null);
      }
      else
      {
        cs.setLong(2, idUde.longValue());
      }
      cs.registerOutParameter(3,Types.DECIMAL); //ulu
      cs.registerOutParameter(4,Types.INTEGER); //codice errore
      cs.registerOutParameter(5,Types.VARCHAR); //msg errore
      
      cs.executeUpdate();
      
      plqObj = new PlSqlCalcoloOteVO();  
      plqObj.setUlu(cs.getBigDecimal(3));
      plqObj.setCodeResult(cs.getLong(4)); 
      plqObj.setDescError(cs.getString(5));
      
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
        new Parametro("idUde", idUde), };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AnagrafeGaaDAO::calcolaUluPlSql] ",
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
              "[AnagrafeGaaDAO::calcolaUluPlSql] END.");
    }
  }
  
  /**
   * Restituiesce l'oggetto TipoUdeVo passando idUde
   * 
   * @param idUde
   * @return
   * @throws DataAccessException
   */
  public TipoUdeVO getTipoUdeByPrimaryKey(long idUde) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    TipoUdeVO tipoUdeVO = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[AnagrafeGaaDAO::getTipoUdeByPrimaryKey] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   SELECT   " 
              + "     TU.ID_UDE, " 
              + "     TU.CLASSE_UDE, "
              + "     TU.LIM_INF_UDE, "
              + "     TU.LIM_SUP_UDE, "
              + "     TU.LIM_INF_RLS, "
              + "     TU.LIM_SUP_RLS, "
              + "     TU.DATA_INIZIO_VALIDITA, "
              + "     TU.DATA_FINE_VALIDITA "
              + "   FROM   "
              + "     DB_TIPO_UDE TU   "
              + "   WHERE  "
              + "     TU.ID_UDE = ?  ");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::getTipoUdeByPrimaryKey] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      int indice = 0;
      stmt.setLong(++indice,idUde);
      
      
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        tipoUdeVO = new TipoUdeVO(); 
        tipoUdeVO.setIdUde(rs.getLong("ID_UDE"));
        tipoUdeVO.setClasseUde(rs.getLong("CLASSE_UDE"));
        tipoUdeVO.setLimInfUde(rs.getLong("LIM_INF_UDE"));
        tipoUdeVO.setLimSupUde(rs.getLong("LIM_SUP_UDE"));
        tipoUdeVO.setLimInfRls(rs.getBigDecimal("LIM_INF_RLS"));
        tipoUdeVO.setLimSupRls(rs.getBigDecimal("LIM_SUP_RLS"));
        tipoUdeVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        tipoUdeVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
      }
      
      return tipoUdeVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("tipoUdeVO", tipoUdeVO) };
      
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUde", idUde) };
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AnagrafeGaaDAO::getTipoUdeByPrimaryKey] ",
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
              "[AnagrafeGaaDAO::getTipoUdeByPrimaryKey] END.");
    }
  }
  
  
  /**
   * Restituiesce un vettore di AziendaAtecoSecVO
   * 
   * @param idUde
   * @return
   * @throws DataAccessException
   */
  public Vector<AziendaAtecoSecVO> getListActiveAziendaAtecoSecByIdAzienda(long idAzienda) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<AziendaAtecoSecVO> result = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[AnagrafeGaaDAO::getListActiveAziendaAtecoSecByIdAzienda] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   "
          		+ "SELECT   " 
              + "       AAS.ID_AZIENDA_ATECO_SEC, " 
              + "       AAS.ID_AZIENDA, "
              + "       AAS.ID_ATTIVITA_ATECO, "
              + "       AAS.DATA_INIZIO_VALIDITA, "
              + "       AAS.DATA_FINE_VALIDITA,"
              + "       TAA.CODICE, "
              + "       TAA.DESCRIZIONE "
              + "FROM   "
              + "       DB_AZIENDA_ATECO_SEC AAS,"
              + "       DB_TIPO_ATTIVITA_ATECO TAA "
              + "WHERE  "
              + "       AAS.ID_AZIENDA = ? "
              + "AND    AAS.DATA_FINE_VALIDITA IS NULL "
              //+ "AND    TAA.DATA_FINE_VALIDITA IS NULL "
              +	"AND    AAS.ID_ATTIVITA_ATECO = TAA.ID_ATTIVITA_ATECO "
              + "ORDER BY "
              + "       TAA.DESCRIZIONE ");		
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::getListActiveAziendaAtecoSecByIdAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      int indice = 0;
      stmt.setLong(++indice,idAzienda);
      
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(result == null)
        {
          result = new Vector<AziendaAtecoSecVO>();
        }
        AziendaAtecoSecVO aziendaAtecoSecVO  = new AziendaAtecoSecVO(); 
        aziendaAtecoSecVO.setIdAziendaAtecoSec(rs.getLong("ID_AZIENDA_ATECO_SEC"));
        aziendaAtecoSecVO.setIdAzienda(rs.getLong("ID_AZIENDA"));
        aziendaAtecoSecVO.setIdAttivitaAteco(rs.getLong("ID_ATTIVITA_ATECO"));
        aziendaAtecoSecVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        aziendaAtecoSecVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        aziendaAtecoSecVO.setCodAttivitaAteco(rs.getString("CODICE"));
        aziendaAtecoSecVO.setDescAttivitaAteco(rs.getString("DESCRIZIONE"));
        
        result.add(aziendaAtecoSecVO);
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
              "[AnagrafeGaaDAO::getListActiveAziendaAtecoSecByIdAzienda] ",
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
              "[AnagrafeGaaDAO::getListActiveAziendaAtecoSecByIdAzienda] END.");
    }
  }
  
  
  
  public Vector<AziendaAtecoSecVO> getListActiveAziendaAtecoSecByIdAziendaAndValid(long idAzienda,
      Long idDichiarazioneConsistenza) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<AziendaAtecoSecVO> result = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[AnagrafeGaaDAO::getListActiveAziendaAtecoSecByIdAziendaAndValid] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   "
              + "SELECT AAS.ID_AZIENDA_ATECO_SEC, " 
              + "       AAS.ID_AZIENDA, "
              + "       AAS.ID_ATTIVITA_ATECO, "
              + "       AAS.DATA_INIZIO_VALIDITA, "
              + "       AAS.DATA_FINE_VALIDITA,"
              + "       TAA.CODICE, "
              + "       TAA.DESCRIZIONE "
              + "FROM   DB_AZIENDA_ATECO_SEC AAS,"
              + "       DB_TIPO_ATTIVITA_ATECO TAA,"
              + "       DB_DICHIARAZIONE_CONSISTENZA DC "
              + "WHERE  AAS.ID_AZIENDA = ? "
              + "AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? " 
              + "AND    AAS.DATA_INIZIO_VALIDITA < DC.DATA_INSERIMENTO_DICHIARAZIONE "
              + "AND    NVL(AAS.DATA_FINE_VALIDITA,TO_DATE('31/12/9999','DD/MM/YYYY')) > DC.DATA_INSERIMENTO_DICHIARAZIONE "
              + "AND    AAS.ID_ATTIVITA_ATECO = TAA.ID_ATTIVITA_ATECO "
              + "ORDER BY "
              + "       TAA.DESCRIZIONE ");   
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::getListActiveAziendaAtecoSecByIdAziendaAndValid] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      int indice = 0;
      stmt.setLong(++indice,idAzienda);
      stmt.setLong(++indice,idDichiarazioneConsistenza);
      
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(result == null)
        {
          result = new Vector<AziendaAtecoSecVO>();
        }
        AziendaAtecoSecVO aziendaAtecoSecVO  = new AziendaAtecoSecVO(); 
        aziendaAtecoSecVO.setIdAziendaAtecoSec(rs.getLong("ID_AZIENDA_ATECO_SEC"));
        aziendaAtecoSecVO.setIdAzienda(rs.getLong("ID_AZIENDA"));
        aziendaAtecoSecVO.setIdAttivitaAteco(rs.getLong("ID_ATTIVITA_ATECO"));
        aziendaAtecoSecVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        aziendaAtecoSecVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        aziendaAtecoSecVO.setCodAttivitaAteco(rs.getString("CODICE"));
        aziendaAtecoSecVO.setDescAttivitaAteco(rs.getString("DESCRIZIONE"));
        
        result.add(aziendaAtecoSecVO);
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
      { new Parametro("idAzienda", idAzienda),
        new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza) };
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AnagrafeGaaDAO::getListActiveAziendaAtecoSecByIdAziendaAndValid] ",
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
              "[AnagrafeGaaDAO::getListActiveAziendaAtecoSecByIdAziendaAndValid] END.");
    }
  }
  
  
  public CodeDescription getAttivitaAtecoAllaValid(long idAzienda,
      long idDichiarazioneConsistenza) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    CodeDescription codeAteco = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[AnagrafeGaaDAO::getAttivitaAtecoAllaValid] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   "
              + "SELECT TAA.ID_ATTIVITA_ATECO, "
              + "       TAA.CODICE, "
              + "       TAA.DESCRIZIONE "
              + "FROM   DB_ANAGRAFICA_AZIENDA AA,"
              + "       DB_TIPO_ATTIVITA_ATECO TAA,"
              + "       DB_DICHIARAZIONE_CONSISTENZA DC "
              + "WHERE  AA.ID_AZIENDA = ? "
              + "AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? " 
              + "AND    AA.DATA_INIZIO_VALIDITA < DC.DATA_INSERIMENTO_DICHIARAZIONE "
              + "AND    NVL(AA.DATA_FINE_VALIDITA,TO_DATE('31/12/9999','DD/MM/YYYY')) > DC.DATA_INSERIMENTO_DICHIARAZIONE "
              + "AND    AA.ID_ATTIVITA_ATECO = TAA.ID_ATTIVITA_ATECO ");   
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::getAttivitaAtecoAllaValid] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      int indice = 0;
      stmt.setLong(++indice,idAzienda);
      stmt.setLong(++indice,idDichiarazioneConsistenza);
      
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        String codATECO = rs.getString("ID_ATTIVITA_ATECO");
        codeAteco = new CodeDescription(new Integer(codATECO),
              rs.getString("DESCRIZIONE"));
        codeAteco.setSecondaryCode(rs.getString("CODICE"));
         
      }
      
      return codeAteco;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("codeAteco", codeAteco) };
      
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza) };
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AnagrafeGaaDAO::getAttivitaAtecoAllaValid] ",
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
              "[AnagrafeGaaDAO::getAttivitaAtecoAllaValid] END.");
    }
  }
  
  /**
   * 
   * Restituisce un vettore di occorrenze attive di TipoDimensioneAziendaVO
   * 
   * 
   * @return
   * @throws DataAccessException
   */
  public Vector<TipoDimensioneAziendaVO> getListActiveTipoDimensioneAzienda() 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoDimensioneAziendaVO> result = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[AnagrafeGaaDAO::getListActiveTipoDimensioneAzienda] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   "
              + "SELECT   " 
              + "       TDA.ID_DIMENSIONE_AZIENDA, " 
              + "       TDA.DESCRIZIONE, "
              + "       TDA.DATA_INIZIO_VALIDITA, "
              + "       TDA.DATA_FINE_VALIDITA "
              + "FROM   "
              + "       DB_TIPO_DIMENSIONE_AZIENDA TDA "
              + "WHERE  "
              + "       TDA.DATA_FINE_VALIDITA IS NULL "
              + "ORDER BY "
              + "       TDA.DESCRIZIONE ");
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::getListActiveTipoDimensioneAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(result == null)
        {
          result = new Vector<TipoDimensioneAziendaVO>();
        }
        TipoDimensioneAziendaVO tipoDimensioneAziendaVO  = new TipoDimensioneAziendaVO(); 
        tipoDimensioneAziendaVO.setIdDimensioneAzienda(rs.getLong("ID_DIMENSIONE_AZIENDA"));
        tipoDimensioneAziendaVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoDimensioneAziendaVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        tipoDimensioneAziendaVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        
        result.add(tipoDimensioneAziendaVO);
      }
      
      return result;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("result", result) };
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AnagrafeGaaDAO::getListActiveTipoDimensioneAzienda] ",
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
              "[AnagrafeGaaDAO::getListActiveTipoDimensioneAzienda] END.");
    }
  }
  
  /**
   * 
   * Restituiesce l'oggetto TipoAttivitaOteVO passando idAttivitaOte
   * 
   * @param idAttivitaOte
   * @return
   * @throws DataAccessException
   */
  public TipoAttivitaOteVO getTipoAttivitaOteByPrimaryKey(long idAttivitaOte) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    TipoAttivitaOteVO tipoAttivitaVO = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[AnagrafeGaaDAO::getTipoAttivitaOteByPrimaryKey] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   SELECT   " 
              + "     TAO.ID_ATTIVITA_OTE, " 
              + "     TAO.CODICE, "
              + "     TAO.DESCRIZIONE "
              + "   FROM   "
              + "     DB_TIPO_ATTIVITA_OTE TAO   "
              + "   WHERE  "
              + "     TAO.ID_ATTIVITA_OTE = ?  ");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::getTipoAttivitaOteByPrimaryKey] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      int indice = 0;
      stmt.setLong(++indice,idAttivitaOte);
      
      
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        tipoAttivitaVO = new TipoAttivitaOteVO(); 
        tipoAttivitaVO.setIdAttivitaOte(rs.getLong("ID_ATTIVITA_OTE"));
        tipoAttivitaVO.setCodice(rs.getString("CODICE"));
        tipoAttivitaVO.setDescrizione(rs.getString("DESCRIZIONE"));
      }
      
      return tipoAttivitaVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("tipoUdeVO", tipoAttivitaVO) };
      
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAttivitaOte", idAttivitaOte) };
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AnagrafeGaaDAO::getTipoUdeByPrimaryKey] ",
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
              "[AnagrafeGaaDAO::getTipoUdeByPrimaryKey] END.");
    }
  }
  
  /**
   * Elimina un record di DB_AZIENDA_ATECO_SEC passando la chiave primaria 
   * 
   * @param idAziendaAtecoSec
   * @throws DataAccessException
   */
  public void deleteAziendaAtecoSec(long idAziendaAtecoSec) 
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
              "[AnagrafeGaaDAO::deleteAziendaAtecoSec] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   DELETE DB_AZIENDA_ATECO_SEC WHERE ID_AZIENDA_ATECO_SEC = ?  " );
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::deleteAziendaAtecoSec] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAziendaAtecoSec);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAziendaAtecoSec", idAziendaAtecoSec)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AnagrafeGaaDAO::deleteAziendaAtecoSec] ",
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
              "[AnagrafeGaaDAO::deleteAziendaAtecoSec] END.");
    }
  }
  
  
  /**
   * Iserisce un record sulla tabella DB_AZIENDA_ATECO_SEC
   * 
   * @param aziendaAtecoSecVO
   * @return
   * @throws DataAccessException
   */
  public Long insertAziendaAtecoSec(AziendaAtecoSecVO aziendaAtecoSecVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idAziendaAtecoSec = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[AnagrafeGaaDAO::insertAziendaAtecoSec] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idAziendaAtecoSec = getNextPrimaryKey(SolmrConstants.SEQ_DB_AZIENDA_ATECO_SEC);
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_AZIENDA_ATECO_SEC   " 
              + "     (ID_AZIENDA_ATECO_SEC, "
              + "     ID_AZIENDA, " 
              + "     ID_ATTIVITA_ATECO, "
              + "     DATA_INIZIO_VALIDITA,"
              +	"     DATA_FINE_VALIDITA) "
              + "   VALUES(?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::insertAziendaAtecoSec] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAziendaAtecoSec.longValue());
      stmt.setLong(++indice, aziendaAtecoSecVO.getIdAzienda());
      stmt.setLong(++indice, aziendaAtecoSecVO.getIdAttivitaAteco());
      stmt.setTimestamp(++indice, convertDateToTimestamp(aziendaAtecoSecVO.getDataInizioValidita()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(aziendaAtecoSecVO.getDataFineValidita()));
      
  
      stmt.executeUpdate();
      
      return idAziendaAtecoSec;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idAziendaAtecoSec", idAziendaAtecoSec)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("aziendaAtecoSecVO", aziendaAtecoSecVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AnagrafeGaaDAO::insertAziendaAtecoSec] ",
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
              "[AnagrafeGaaDAO::insertAziendaAtecoSec] END.");
    }
  }
  
  /**
   * Fà l'update della tabella DB_AZIENDA_ATECO_SEC
   * 
   * 
   * @param aziendaAtecoSecVO
   * @throws DataAccessException
   */
  public void updateAziendaAtecoSec(AziendaAtecoSecVO aziendaAtecoSecVO) 
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
              "[AnagrafeGaaDAO::updateAziendaAtecoSec] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   UPDATE DB_AZIENDA_ATECO_SEC   " 
              + "     SET ID_AZIENDA = ? , "
              + "     ID_ATTIVITA_ATECO = ? , "
              + "     DATA_INIZIO_VALIDITA = ? ,   "
              + "     DATA_FINE_VALIDITA = ?  "
              + "   WHERE  "
              + "     ID_AZIENDA_ATECO_SEC = ?  ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::updateAziendaAtecoSec] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice,aziendaAtecoSecVO.getIdAzienda());
      stmt.setLong(++indice,aziendaAtecoSecVO.getIdAttivitaAteco());
      stmt.setTimestamp(++indice, convertDateToTimestamp(aziendaAtecoSecVO.getDataInizioValidita()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(aziendaAtecoSecVO.getDataFineValidita()));
      stmt.setLong(++indice, aziendaAtecoSecVO.getIdAziendaAtecoSec());
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("aziendaAtecoSecVO", aziendaAtecoSecVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AnagrafeGaaDAO::updateAziendaAtecoSec] ",
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
              "[AnagrafeGaaDAO::updateAziendaAtecoSec] END.");
    }
  }
  
  
  public void storicizzaAziendaAtecoSecForAzienda(long idAzienda) 
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
                "[AnagrafeGaaDAO::storicizzaAziendaAtecoSecForAzienda] BEGIN.");
    
        /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
    
        queryBuf = new StringBuffer();
        queryBuf.append("" +
          "UPDATE DB_AZIENDA_ATECO_SEC " +
          "     SET DATA_FINE_VALIDITA = SYSDATE  " +
          "WHERE  " +
          "     ID_AZIENDA = ?  " +
          "     AND DATA_FINE_VALIDITA IS NULL ");
        
        query = queryBuf.toString();
        /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
        conn = getDatasource().getConnection();
        if (SolmrLogger.isDebugEnabled(this))
        {
          // Dato che la query costruita dinamicamente è un dato importante la
          // registro sul file di log se il
          // debug è abilitato
    
          SolmrLogger.debug(this,
              "[AnagrafeGaaDAO::storicizzaAziendaAtecoSecForAzienda] Query="
                  + query);
        }
        stmt = conn.prepareStatement(query);
        
        int indice = 0;
        stmt.setLong(++indice, idAzienda);
        
    
        stmt.executeUpdate();
      }
      catch (Throwable t)
      {
        // Vettore di variabili interne del metodo
        Variabile variabili[] = new Variabile[]
        { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
    
        // Vettore di parametri passati al metodo
        Parametro parametri[] = new Parametro[]
        { new Parametro("idAzienda", idAzienda)};
        // Logging dell'eccezione, query, variabili e parametri del metodo
        LoggerUtils
            .logDAOError(
                this,
                "[AnagrafeGaaDAO::storicizzaAziendaAtecoSecForAzienda] ",
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
                "[AnagrafeGaaDAO::storicizzaAziendaAtecoSecForAzienda] END.");
      }
    }
  
  
  /**
   * 
   * Restituisce l'elenco degli ID_AZIENDA_COLLEGATA che soddisfano i filtri
   * della ricerca terreni
   * 
   * @param filtriRicercaAziendeCollegateVO
   * @return
   * @throws DataAccessException
   */
  public long[] ricercaIdAziendeCollegate(
      FiltriRicercaAziendeCollegateVO filtriRicercaAziendeCollegateVO) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<Long> vIds = null;
    long ids[] = null;
    try
    {
      SolmrLogger.debug(this, "[AnagrafeGaaDAO::ricercaIdAziendeCollegate] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf = new StringBuffer(" " +
        "SELECT  "+
        "        DAC.ID_AZIENDA_COLLEGATA " +
        "FROM    DB_ANAGRAFICA_AZIENDA AA," +
        "        DB_AZIENDA_COLLEGATA DAC, "+
        "        DB_SOGGETTO_ASSOCIATO DAS " +
        "WHERE   DAC.ID_AZIENDA = ? ");
      
      
      if(!filtriRicercaAziendeCollegateVO.isStorico())
      {
        queryBuf.append(
        "AND NVL(TRUNC(DAC.DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > SYSDATE "+
        "AND NVL(TRUNC(DAC.DATA_USCITA), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > SYSDATE ");
      }
        
      queryBuf.append(
        "AND DAC.ID_AZIENDA_ASSOCIATA = AA.ID_AZIENDA(+) "+
        "AND DAC.ID_SOGGETTO_ASSOCIATO = DAS.ID_SOGGETTO_ASSOCIATO(+) "+
        "AND AA.DATA_FINE_VALIDITA IS NULL ");
      //Eliminato richiesta Ravera 03/01/2014
      //  "AND AA.DATA_CESSAZIONE IS NULL ");
        
      if(filtriRicercaAziendeCollegateVO.getIstatComuneRicerca() != null)
      {
        queryBuf.append(        
        "AND NVL2(DAC.ID_AZIENDA_ASSOCIATA,AA.SEDELEG_COMUNE,DAS.COMUNE) = ? ");
      }
      
      if(filtriRicercaAziendeCollegateVO.getCuaaRicerca() != null)
      {
        queryBuf.append(
        "AND NVL2(DAC.ID_AZIENDA_ASSOCIATA,AA.CUAA,DAS.CUAA) = ? ");
      }
      
      if(filtriRicercaAziendeCollegateVO.getPartitaIvaRicerca() != null)
      {
        queryBuf.append(        
        "AND NVL2(DAC.ID_AZIENDA_ASSOCIATA,AA.PARTITA_IVA,DAS.PARTITA_IVA) = ? ");
      }
      
      if(filtriRicercaAziendeCollegateVO.getDenominazioneRicerca() != null)
      {
        queryBuf.append(       
        "AND NVL2(DAC.ID_AZIENDA_ASSOCIATA,AA.DENOMINAZIONE,DAS.DENOMINAZIONE) LIKE ? ");
      }




      queryBuf.append(
        "ORDER BY " +
        "NVL2(DAC.ID_AZIENDA_ASSOCIATA,AA.DENOMINAZIONE,DAS.DENOMINAZIONE), " +
        "NVL2(DAC.ID_AZIENDA_ASSOCIATA,AA.CUAA,DAS.CUAA) ");

      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[AnagrafeGaaDAO::ricercaIdAziendeCollegate] Query="
            + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, filtriRicercaAziendeCollegateVO
        .getIdAzienda().longValue());
      if(filtriRicercaAziendeCollegateVO.getIstatComuneRicerca() != null)
      {
        stmt.setString(++indice, filtriRicercaAziendeCollegateVO
          .getIstatComuneRicerca());
      }
      if(filtriRicercaAziendeCollegateVO.getCuaaRicerca() != null)
      {
        stmt.setString(++indice, filtriRicercaAziendeCollegateVO
          .getCuaaRicerca().toUpperCase());
      }
      if(filtriRicercaAziendeCollegateVO.getPartitaIvaRicerca() != null)
      {
        stmt.setString(++indice, filtriRicercaAziendeCollegateVO
          .getPartitaIvaRicerca());
      }
      if(filtriRicercaAziendeCollegateVO.getDenominazioneRicerca() != null)
      {
        stmt.setString(++indice, filtriRicercaAziendeCollegateVO
          .getDenominazioneRicerca().toUpperCase()+"%");
      }
      

      ResultSet rs = stmt.executeQuery();
      vIds = new Vector<Long>();
      while (rs.next())
      {
        vIds.add(new Long(rs.getLong("ID_AZIENDA_COLLEGATA")));
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
      { new Parametro("filtriRicercaAziendeCollegateVO", filtriRicercaAziendeCollegateVO) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[AnagrafeGaaDAO::ricercaIdAziendeCollegate] ", t,
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
      SolmrLogger.debug(this, "[AnagrafeGaaDAO::ricercaIdAziendeCollegate] END.");
    }
  }
  
  
  /**
   * 
   * Restituisce i dati aziende collegate di cui si conoscono gli id_azienda_collegate 
   * 
   * @param ids
   * @return
   * @throws DataAccessException
   */
  public RigaRicercaAziendeCollegateVO[] getRigheRicercaAziendeCollegateByIdAziendaCollegata(
      long ids[]) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    RigaRicercaAziendeCollegateVO rigaRicercaAziendeCollegateVO = null;
    RigaRicercaAziendeCollegateVO righe[] = null;
    HashMap<Long,RigaRicercaAziendeCollegateVO> hmRighe = null;
    try
    {
      SolmrLogger.debug(this,
          "[AnagrafeGaaDAO::getRigheRicercaAziendeCollegateByIdAziendaCollegata] BEGIN.");
      
      
     
      conn = getDatasource().getConnection();

      Long idJavaIns = getNextPrimaryKey(SolmrConstants.SEQ_SMRGAA_W_JAVA_INSERT);

      String insert="INSERT INTO SMRGAA_W_JAVA_INSERT (ID_JAVA_INSERT, ID_DETTAGLIO_INS) "+
                    "VALUES ( ?, ?) ";

      stmt = conn.prepareStatement(insert);

      for (int i=0;i<ids.length;i++)
      {
        stmt.setLong(1,idJavaIns.longValue());
        stmt.setLong(2,ids[i]);
        stmt.addBatch();
      }
      stmt.executeBatch();

      SolmrLogger.debug(this,"[AnagrafeGaaDAO::getRigheRicercaAziendeCollegateByIdAziendaCollegata] insert executed");

      stmt.close();

      
      
      

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
     
      queryBuf.append("" +   
        "WITH DICH_CONS AS " +
        "     (SELECT MAX(DC.DATA_INSERIMENTO_DICHIARAZIONE) AS MAX_DATA, " +
        "             DAC.ID_AZIENDA_ASSOCIATA " +       
        "      FROM   DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "             DB_TIPO_MOTIVO_DICHIARAZIONE TI, " +
        "             DB_AZIENDA_COLLEGATA DAC, " +
        "             SMRGAA_W_JAVA_INSERT WIJ " +
        "      WHERE  DAC.ID_AZIENDA_ASSOCIATA = DC.ID_AZIENDA (+) " +
        "      AND    DC.ID_MOTIVO_DICHIARAZIONE (+) <> 7 " +
        "      AND    DC.ID_MOTIVO_DICHIARAZIONE = TI.ID_MOTIVO_DICHIARAZIONE (+) " +
        "      AND    TI.TIPO_DICHIARAZIONE (+) != 'C' " +
        "      AND    DAC.ID_AZIENDA_COLLEGATA = WIJ.ID_DETTAGLIO_INS " +
        "      AND    WIJ.ID_JAVA_INSERT = ? " +
        "      GROUP BY DAC.ID_AZIENDA_ASSOCIATA " +       
        "     ) " +
        "SELECT NVL2 (DAC.ID_AZIENDA_ASSOCIATA, AA.CUAA, DAS.CUAA) AS CUAA, " +
        "       NVL2 (DAC.ID_AZIENDA_ASSOCIATA, AA.DENOMINAZIONE, DAS.DENOMINAZIONE) AS DENOMINAZIONE, " +
        "       NVL2 (DAC.ID_AZIENDA_ASSOCIATA, AA.PARTITA_IVA, DAS.PARTITA_IVA) AS PARTITA_IVA, " +
        "       NVL2 (DAC.ID_AZIENDA_ASSOCIATA, AA.SEDELEG_INDIRIZZO, DAS.INDIRIZZO) AS INDIRIZZO, " +
        "       NVL2 (DAC.ID_AZIENDA_ASSOCIATA, AA.SEDELEG_CAP, DAS.CAP) AS CAP, " +
        "       NVL2 (DAC.ID_AZIENDA_ASSOCIATA, AA.SEDELEG_COMUNE, DAS.COMUNE) AS COMUNE, " +
        "       AA.SEDELEG_CITTA_ESTERO, AA.ID_UTENTE_AGGIORNAMENTO, DAC.ID_AZIENDA, " +
        "       DAC.DATA_INGRESSO, DAC.DATA_USCITA, DAC.DATA_INIZIO_VALIDITA, " +
        "       DAC.DATA_FINE_VALIDITA, DAC.DATA_AGGIORNAMENTO, " +
        "       DAC.ID_AZIENDA_COLLEGATA, DAC.ID_SOGGETTO_ASSOCIATO, " +
        "       DAC.ID_AZIENDA_ASSOCIATA, " +
       // "       NVL(PVU.COGNOME_UTENTE, TRIM(UPPER(PVU.COGNOME_UTENTE_LOGIN)))||' '||NVL(PVU.NOME_UTENTE, TRIM(UPPER(PVU.NOME_UTENTE_LOGIN))) AS UI_DENOMINAZIONE, " +
        "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
        "          || ' ' " + 
        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
        "         WHERE DAC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
        "       AS UI_DENOMINAZIONE, " +
       //"       PVU.DENOMINAZIONE " +
       "       (SELECT PVU.DENOMINAZIONE " +
       "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
       "        WHERE DAC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
       "       AS UI_ENTEAPPARTENENZA, " +
        "       CO.DESCOM, " +
        "       CO.FLAG_ESTERO, PR.SIGLA_PROVINCIA, PR.ISTAT_PROVINCIA, " +
        "       DC.DATA AS DATA_VALIDAZIONE, " +
        "       DC.ID_UTENTE AS ID_UTENTE_VALIDAZIONE, " +
        "       (SELECT NVL(SUM (DCD.SUPERFICIE_CONDOTTA), 0) " +
        "        FROM   DB_DICHIARAZIONE_CONSISTENZA DDC, " +
        "               DB_CONDUZIONE_DICHIARATA DCD " +
        "        WHERE  DDC.ID_AZIENDA = DAC.ID_AZIENDA_ASSOCIATA " +
        "        AND    DDC.CODICE_FOTOGRAFIA_TERRENI = DCD.CODICE_FOTOGRAFIA_TERRENI " +
        "        AND    DCD.ID_TITOLO_POSSESSO <> 5 " +
        "        AND    DDC.ID_MOTIVO_DICHIARAZIONE <> 7 " +
        "        AND    DDC.DATA_INSERIMENTO_DICHIARAZIONE = NEWDC.MAX_DATA) AS TOT_SUP_COND, " +
        "       (SELECT NVL(SUM (DUD.SUPERFICIE_UTILIZZATA), 0) " +
        "        FROM   DB_DICHIARAZIONE_CONSISTENZA DDC, " +
        "               DB_CONDUZIONE_DICHIARATA DCD, " +
        "               DB_UTILIZZO_DICHIARATO DUD, " +
        "               DB_TIPO_UTILIZZO DTU " +
        "        WHERE  DDC.ID_AZIENDA = DAC.ID_AZIENDA_ASSOCIATA " +
        "        AND    DDC.CODICE_FOTOGRAFIA_TERRENI = DCD.CODICE_FOTOGRAFIA_TERRENI " +
        "        AND    DCD.ID_CONDUZIONE_DICHIARATA = DUD.ID_CONDUZIONE_DICHIARATA " +
        "        AND    DUD.ID_UTILIZZO = DTU.ID_UTILIZZO " +
        "        AND    DTU.FLAG_SAU = 'S' " +
        "        AND    DCD.ID_TITOLO_POSSESSO <> 5 " +
        "        AND    DDC.ID_MOTIVO_DICHIARAZIONE <> 7 " +
        "        AND    DDC.DATA_INSERIMENTO_DICHIARAZIONE = NEWDC.MAX_DATA) AS TOT_SUP_SAU " +
        "FROM  DB_ANAGRAFICA_AZIENDA AA, " +
        "      DB_AZIENDA_COLLEGATA DAC, " +
        "      DB_SOGGETTO_ASSOCIATO DAS, " +
       // "      PAPUA_V_UTENTE_LOGIN PVU, " +
        "      COMUNE CO, " +
        "      PROVINCIA PR, " +
        "      SMRGAA_W_JAVA_INSERT WIJ, " +
        "      DICH_CONS NEWDC, " +
        "      DB_DICHIARAZIONE_CONSISTENZA DC " +
        "WHERE DAC.ID_AZIENDA_ASSOCIATA = AA.ID_AZIENDA(+) " +
        "AND DAC.ID_SOGGETTO_ASSOCIATO = DAS.ID_SOGGETTO_ASSOCIATO(+) " +
        "AND AA.DATA_FINE_VALIDITA IS NULL " +
        //Eliminata richiesta Ravera 03/01
        //"AND AA.DATA_CESSAZIONE IS NULL " +
        //"AND DAC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
        "AND NVL2 (DAC.ID_AZIENDA_ASSOCIATA, AA.SEDELEG_COMUNE, DAS.COMUNE) =  CO.ISTAT_COMUNE " +
        "AND CO.ISTAT_PROVINCIA = PR.ISTAT_PROVINCIA " +
        "AND DAC.ID_AZIENDA_COLLEGATA = WIJ.ID_DETTAGLIO_INS " +
        "AND WIJ.ID_JAVA_INSERT = ? " +
        "AND DAC.ID_AZIENDA_ASSOCIATA = NEWDC.ID_AZIENDA_ASSOCIATA (+) " +
        "AND NEWDC.MAX_DATA = DC.DATA_INSERIMENTO_DICHIARAZIONE (+) " +
        "AND NEWDC.ID_AZIENDA_ASSOCIATA = DC.ID_AZIENDA (+) ");
         
      
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      //conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::getRigheRicercaAziendeCollegateByIdAziendaCollegata] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1,idJavaIns.longValue());
      stmt.setLong(2,idJavaIns.longValue());
      
      ResultSet rs = stmt.executeQuery();
      
      hmRighe = new HashMap<Long,RigaRicercaAziendeCollegateVO>();
      while (rs.next())
      {
        rigaRicercaAziendeCollegateVO = new RigaRicercaAziendeCollegateVO();
        Long idSoggettoAssociato = checkLongNull(rs.getString("ID_SOGGETTO_ASSOCIATO"));
        if(idSoggettoAssociato != null)
        {
          SoggettoAssociatoVO soggVO = new SoggettoAssociatoVO();
          soggVO.setCuaa(rs.getString("CUAA"));
          soggVO.setDenominazione(rs.getString("DENOMINAZIONE"));
          soggVO.setPartitaIva(rs.getString("PARTITA_IVA"));
          soggVO.setIndirizzo(rs.getString("INDIRIZZO"));
          soggVO.setCap(rs.getString("CAP"));
          soggVO.setComune(rs.getString("COMUNE"));
          soggVO.setSglProv(rs.getString("SIGLA_PROVINCIA"));
          soggVO.setDenominazioneComune(rs.getString("DESCOM"));
          
          rigaRicercaAziendeCollegateVO.setIdSoggettoAssociato(idSoggettoAssociato);
          rigaRicercaAziendeCollegateVO.setSoggettoAssociato(soggVO);
        }
        else
        {
          rigaRicercaAziendeCollegateVO.setCuaa(rs.getString("CUAA"));
          rigaRicercaAziendeCollegateVO.setNomeAzienda(rs.getString("DENOMINAZIONE"));
          rigaRicercaAziendeCollegateVO.setPartitaIva(rs.getString("PARTITA_IVA"));
          rigaRicercaAziendeCollegateVO.setIndirizzo(rs.getString("INDIRIZZO"));
          rigaRicercaAziendeCollegateVO.setIstatComune(rs.getString("COMUNE"));
          
          String flagEstero = rs.getString("FLAG_ESTERO");
          String descComune = rs.getString("DESCOM");

          if(flagEstero.equals(SolmrConstants.FLAG_S))
          {
            rigaRicercaAziendeCollegateVO.setSedeEstero(descComune);
            rigaRicercaAziendeCollegateVO.setSedeCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
            rigaRicercaAziendeCollegateVO.setSglProvincia("");
            rigaRicercaAziendeCollegateVO.setComune("");
            rigaRicercaAziendeCollegateVO.setCap("");
          }
          else
          {
            rigaRicercaAziendeCollegateVO.setSedeEstero("");
            rigaRicercaAziendeCollegateVO.setSedeCittaEstero("");
            rigaRicercaAziendeCollegateVO.setSglProvincia(rs.getString("SIGLA_PROVINCIA"));
            rigaRicercaAziendeCollegateVO.setComune(descComune);
            rigaRicercaAziendeCollegateVO.setCap(rs.getString("CAP"));
          }
                  
          rigaRicercaAziendeCollegateVO.setIdAziendaAssociata(new Long(rs.getLong("ID_AZIENDA_ASSOCIATA")));
          
        }
        rigaRicercaAziendeCollegateVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        rigaRicercaAziendeCollegateVO.setDataIngresso(rs.getDate("DATA_INGRESSO"));
        rigaRicercaAziendeCollegateVO.setDataUscita(rs.getDate("DATA_USCITA"));
        rigaRicercaAziendeCollegateVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        rigaRicercaAziendeCollegateVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        rigaRicercaAziendeCollegateVO.setIdAziendaCollegata(new Long(rs.getLong("ID_AZIENDA_COLLEGATA")));
        rigaRicercaAziendeCollegateVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
        rigaRicercaAziendeCollegateVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        rigaRicercaAziendeCollegateVO.setDescrizioneUtenteModifica(rs.getString("UI_DENOMINAZIONE"));
        rigaRicercaAziendeCollegateVO.setDescrizioneEnteUtenteModifica(rs.getString("UI_ENTEAPPARTENENZA"));
        rigaRicercaAziendeCollegateVO.setSupCondotta(rs.getBigDecimal("TOT_SUP_COND"));
        rigaRicercaAziendeCollegateVO.setSupSAU(rs.getBigDecimal("TOT_SUP_SAU"));
        rigaRicercaAziendeCollegateVO.setDataValidazione(rs.getTimestamp("DATA_VALIDAZIONE"));
        rigaRicercaAziendeCollegateVO.setIdUtenteValidazione(checkLongNull(rs.getString("ID_UTENTE_VALIDAZIONE")));
        
        hmRighe.put(rigaRicercaAziendeCollegateVO.getIdAziendaCollegata(),
            rigaRicercaAziendeCollegateVO);
      }
      int size = hmRighe.size();
      if (size == 0)
      {
        return null;
      }
      else
      {
        righe = new RigaRicercaAziendeCollegateVO[size];
        for (int i = 0; i < size; ++i)
        {
          righe[i] = (RigaRicercaAziendeCollegateVO) hmRighe.get(new Long(ids[i]));
        }
        return righe;
      }
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("hmRighe", hmRighe),
          new Variabile("rigaRicercaAziendeCollegateVO", rigaRicercaAziendeCollegateVO),
          new Variabile("righe", righe) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("ids", ids) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[AnagrafeGaaDAO::getRigheRicercaAziendeCollegateByIdAziendaCollegata] ", t,
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
          "[AnagrafeGaaDAO::getRigheRicercaAziendeCollegateByIdAziendaCollegata] END.");
    }
  }
  
  
  /**
   * Restituisce i dati delle variazioni per la presa visione
   * @param elencoIdPresaVisione elenco degli id delle variazioni selezionate
   * @param filtriRicercaVariazioniAziendaliVO usato solo per l'ordinamento
   * @return
   * @throws DataAccessException
   */
  public RigaRicercaVariazioniAziendaliVO[] getRigheVariazioniVisione(Vector<Long> elencoIdPresaVisione,FiltriRicercaVariazioniAziendaliVO filtriRicercaVariazioniAziendaliVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<RigaRicercaVariazioniAziendaliVO> hmRighe = null;
    try
    {
      SolmrLogger.debug(this,
          "[AnagrafeGaaDAO::getRigheVariazioniVisione] BEGIN.");

      queryBuf = new StringBuffer();

      queryBuf.append("SELECT AA.CUAA,AA.PARTITA_IVA,AA.DENOMINAZIONE,AA.SEDELEG_INDIRIZZO,AA.SEDELEG_CAP,AA.SEDELEG_CITTA_ESTERO, ");
      queryBuf.append("DVA.DATA_VARIAZIONE,PC.DESCRIZIONE AS PROV_COMP,TVA.DESCRIZIONE AS VARIAZIONE, TTV.DESCRIZIONE AS TIPOLOGIA_VARIAZIONE,  ");
      queryBuf.append("C.DESCOM || ' (' || P.SIGLA_PROVINCIA || ')' AS COMUME_SEDE_LEGALE, C.FLAG_ESTERO, VA.FLAG_STORICIZZATO,DVA.ID_DETTAGLIO_VARIAZIONE_AZI ");
      queryBuf.append("FROM DB_VARIAZIONE_AZIENDALE VA, DB_ANAGRAFICA_AZIENDA AA, DB_DETTAGLIO_VARIAZIONE_AZI DVA,PROVINCIA PC, ");
      queryBuf.append("DB_TIPO_VARIAZIONE_AZIENDA TVA, DB_TIPO_TIPOLOGIA_VARIAZIONE TTV, COMUNE C, PROVINCIA P ");
      queryBuf.append("WHERE AA.ID_AZIENDA=VA.ID_AZIENDA ");
      queryBuf.append("AND AA.DATA_FINE_VALIDITA IS NULL ");
      queryBuf.append("AND DVA.ID_VARIAZIONE_AZIENDALE= VA.ID_VARIAZIONE_AZIENDALE ");
      queryBuf.append("AND AA.PROVINCIA_COMPETENZA= PC.ISTAT_PROVINCIA ");
      queryBuf.append("AND DVA.ID_TIPO_VARIAZIONE_AZIENDA=TVA.ID_TIPO_VARIAZIONE_AZIENDA ");
      queryBuf.append("AND TVA.ID_TIPO_TIPOLOGIA_VARIAZIONE=TTV.ID_TIPO_TIPOLOGIA_VARIAZIONE ");
      queryBuf.append("AND AA.SEDELEG_COMUNE= C.ISTAT_COMUNE ");
      queryBuf.append("AND C.ISTAT_PROVINCIA=P.ISTAT_PROVINCIA ");
      queryBuf.append("AND DVA.ID_DETTAGLIO_VARIAZIONE_AZI IN ( ");      
      
      //metto in IN gli di passati
      int size=elencoIdPresaVisione.size();
      for (int i=0;i<size;i++)
      {
        if (i!=0) queryBuf.append(",");
        queryBuf.append(elencoIdPresaVisione.get(i));
      }
      queryBuf.append(" ) ");
      
      
      //Se l'utente ha impostato un filtro di ricerca lo uso, altrimenti uso quello di default
      if (filtriRicercaVariazioniAziendaliVO.getCampiPerOrderBy()!=null) 
        queryBuf.append(filtriRicercaVariazioniAziendaliVO.getCampiPerOrderBy().getOrderBy()).append(" ");
      

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::getRigheVariazioniVisione] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);

      ResultSet rs = stmt.executeQuery();
      hmRighe = new Vector<RigaRicercaVariazioniAziendaliVO>();
      while (rs.next())
      {
        RigaRicercaVariazioniAziendaliVO rigaRicercaVariazioniAziendaliVO = new RigaRicercaVariazioniAziendaliVO();
        
        rigaRicercaVariazioniAziendaliVO.setDescProvAmmComp(rs.getString("PROV_COMP"));
        rigaRicercaVariazioniAziendaliVO.setCuaa(rs.getString("CUAA"));
        rigaRicercaVariazioniAziendaliVO.setPartitaIva(rs.getString("PARTITA_IVA"));
        rigaRicercaVariazioniAziendaliVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        
        //Se il comune è in uno stato estero visualizzo la citta estera
        if ( SolmrConstants.FLAG_S.equals(rs.getString("FLAG_ESTERO")))
          rigaRicercaVariazioniAziendaliVO.setComune(rs.getString("SEDELEG_CITTA_ESTERO"));
        else  rigaRicercaVariazioniAziendaliVO.setComune(rs.getString("COMUME_SEDE_LEGALE"));
        
        rigaRicercaVariazioniAziendaliVO.setIndirizzo(rs.getString("SEDELEG_INDIRIZZO"));
        rigaRicercaVariazioniAziendaliVO.setCap(rs.getString("SEDELEG_CAP"));
        rigaRicercaVariazioniAziendaliVO.setVariazione(rs.getString("VARIAZIONE"));
        rigaRicercaVariazioniAziendaliVO.setTipologiaVariazione(rs.getString("TIPOLOGIA_VARIAZIONE"));
        rigaRicercaVariazioniAziendaliVO.setDataVariazione(rs.getDate("DATA_VARIAZIONE"));
        
        String temp=rs.getString("ID_DETTAGLIO_VARIAZIONE_AZI");
        if (Validator.isNotEmpty(temp))
          rigaRicercaVariazioniAziendaliVO.setIdDettaglioVariazioneAzi(new Long((temp)));
        
        if (SolmrConstants.FLAG_S.equals(rs.getString("FLAG_STORICIZZATO")))
          rigaRicercaVariazioniAziendaliVO.setFlagStoricizzato(true);
        
        hmRighe.add(rigaRicercaVariazioniAziendaliVO);   
      }
      return hmRighe.size() == 0 ? null : (RigaRicercaVariazioniAziendaliVO[]) hmRighe.toArray(new RigaRicercaVariazioniAziendaliVO[0]);
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("hmRighe", hmRighe) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("filtriRicercaVariazioniAziendaliVO", filtriRicercaVariazioniAziendaliVO),
      new Parametro("elencoIdPresaVisione", elencoIdPresaVisione) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[AnagrafeGaaDAO::getRigheVariazioniVisione] ", t,
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
          "[AnagrafeGaaDAO::getRigheVariazioniVisione] END.");
    }
  }
  
  
  /**
   * Questa funzione controlla se fra le variazione selezionate qualcuna è già in presa visione
   * @param elencoIdPresaVisione elenco degli id delle variazioni selezionate
   * @return restituisce true se almeno una fra le variazioni selezionate è in presa visione
   * @throws DataAccessException
   */
  public boolean isPresaVisione(Vector<Long> elencoIdPresaVisione, RuoloUtenza ruoloUtenza) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean result=false;
    try
    {
      SolmrLogger.debug(this,
          "[AnagrafeGaaDAO::isPresaVisione] BEGIN.");

      queryBuf = new StringBuffer();

      queryBuf.append(" SELECT DVA.ID_DETTAGLIO_VARIAZIONE_AZI ");
      queryBuf.append(" FROM DB_DETTAGLIO_VARIAZIONE_AZI DVA,DB_VISIONE_VARIAZIONE_AZI VVA ");
      queryBuf.append(" WHERE VVA.ID_DETTAGLIO_VARIAZIONE_AZI=DVA.ID_DETTAGLIO_VARIAZIONE_AZI ");
      queryBuf.append(" AND VVA.ID_TIPO_GRUPPO_RUOLO = ? ");
      queryBuf.append(" AND DVA.ID_DETTAGLIO_VARIAZIONE_AZI IN ( ");      
      
      //metto in IN gli di passati
      int size=elencoIdPresaVisione.size();
      for (int i=0;i<size;i++)
      {
        if (i!=0) queryBuf.append(",");
        queryBuf.append(elencoIdPresaVisione.get(i));
      }
      queryBuf.append(" ) ");
      
      
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::isPresaVisione] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      stmt.setInt(1, ruoloUtenza.getTipoGruppoRuolo().getCode().intValue());

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        result=true;
      return result;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("elencoIdPresaVisione", elencoIdPresaVisione) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[AnagrafeGaaDAO::isPresaVisione] ", t,
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
          "[AnagrafeGaaDAO::isPresaVisione] END.");
    }
  }
  

  
  /**
   * 
   * Restituisce i dati delle variazioni aziendali paginati
   * 
   * @param filtriRicercaVariazioniAziendaliVO
   * @param excel se è true prendo tutte le variazioni in quanto non devo paginare
   * @return
   * @throws DataAccessException
   */
  public RigaRicercaVariazioniAziendaliVO[] getRigheRicercaVariazioni(FiltriRicercaVariazioniAziendaliVO filtriRicercaVariazioniAziendaliVO,
                                                                      UtenteAbilitazioni utenteAbilitazioni, RuoloUtenza ruoloUtenza, boolean excel) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<RigaRicercaVariazioniAziendaliVO> hmRighe = null;
    try
    {
      SolmrLogger.debug(this,
          "[AnagrafeGaaDAO::getRigheRicercaVariazioni] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      //Parte per il rownum (paginazione)
      queryBuf.append("SELECT * FROM ( ");
      queryBuf.append("SELECT ROWNUM RID, PAGE.* FROM (");
      
      //Parte relativa alla query vera e propria begin
      queryBuf.append("SELECT AA.CUAA,AA.PARTITA_IVA,AA.DENOMINAZIONE,AA.SEDELEG_INDIRIZZO,AA.SEDELEG_CAP,AA.SEDELEG_CITTA_ESTERO, ");
      queryBuf.append("DVA.DATA_VARIAZIONE,PC.DESCRIZIONE AS PROV_COMP,TVA.DESCRIZIONE AS VARIAZIONE, TTV.DESCRIZIONE AS TIPOLOGIA_VARIAZIONE,  ");
      queryBuf.append("C.DESCOM || ' (' || P.SIGLA_PROVINCIA || ')' AS COMUME_SEDE_LEGALE, C.FLAG_ESTERO, VA.FLAG_STORICIZZATO,VVA.ID_VISIONE_VARIAZIONE_AZI,DVA.ID_DETTAGLIO_VARIAZIONE_AZI ");
      queryBuf.append("FROM DB_VARIAZIONE_AZIENDALE VA, DB_ANAGRAFICA_AZIENDA AA, DB_DETTAGLIO_VARIAZIONE_AZI DVA,PROVINCIA PC, ");
      queryBuf.append("DB_VISIONE_VARIAZIONE_AZI VVA, DB_TIPO_VARIAZIONE_AZIENDA TVA, DB_TIPO_TIPOLOGIA_VARIAZIONE TTV, COMUNE C, PROVINCIA P ");
      
      //L’estrazione delle variazioni è soggetta al ruolo di appartenenza dell’utente ovvero
      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        queryBuf.append(",DB_DELEGA D, DB_INTERMEDIARIO I ");
      }
      
      queryBuf.append("WHERE AA.ID_AZIENDA=VA.ID_AZIENDA ");
      queryBuf.append("AND AA.DATA_FINE_VALIDITA IS NULL ");
      queryBuf.append("AND DVA.ID_VARIAZIONE_AZIENDALE= VA.ID_VARIAZIONE_AZIENDALE ");
      queryBuf.append("AND AA.PROVINCIA_COMPETENZA= PC.ISTAT_PROVINCIA ");
      queryBuf.append("AND DVA.ID_TIPO_VARIAZIONE_AZIENDA=TVA.ID_TIPO_VARIAZIONE_AZIENDA ");
      queryBuf.append("AND TVA.ID_TIPO_TIPOLOGIA_VARIAZIONE=TTV.ID_TIPO_TIPOLOGIA_VARIAZIONE ");
      queryBuf.append("AND AA.SEDELEG_COMUNE= C.ISTAT_COMUNE ");
      queryBuf.append("AND C.ISTAT_PROVINCIA=P.ISTAT_PROVINCIA ");
      queryBuf.append("AND DVA.ID_DETTAGLIO_VARIAZIONE_AZI=VVA.ID_DETTAGLIO_VARIAZIONE_AZI(+) ");
      queryBuf.append("AND VVA.ID_TIPO_GRUPPO_RUOLO (+) =? ");
      
      if (filtriRicercaVariazioniAziendaliVO!=null)
      {
        if (filtriRicercaVariazioniAziendaliVO.getPresaVisione()!=null)
        {
          if (filtriRicercaVariazioniAziendaliVO.getPresaVisione().booleanValue())
          {
            //è stato selezionato il filtro si quindi devo visualizzare solo i record con la presa visione
            //legata all'utente connesso
            queryBuf.append("AND DVA.ID_DETTAGLIO_VARIAZIONE_AZI IN ( ");
            queryBuf.append("SELECT ID_DETTAGLIO_VARIAZIONE_AZI FROM DB_VISIONE_VARIAZIONE_AZI WHERE ID_TIPO_GRUPPO_RUOLO =?) ");
          }
          else
          {
            //è stato selezionato il filtro no: devo fare vedere quelle non presenti o presenti ma legate a qualcun altro
            queryBuf.append("AND DVA.ID_DETTAGLIO_VARIAZIONE_AZI NOT IN ( ");
            queryBuf.append("SELECT ID_DETTAGLIO_VARIAZIONE_AZI FROM DB_VISIONE_VARIAZIONE_AZI WHERE ID_TIPO_GRUPPO_RUOLO =?) ");
          }
        }
        
        
        //Applico gli eventuali filtri inseriti dall'utente
        if (Validator.isNotEmpty(filtriRicercaVariazioniAziendaliVO.getIstatProvAmmComp()))
          queryBuf.append("AND AA.PROVINCIA_COMPETENZA = ? ");
        if (Validator.isNotEmpty(filtriRicercaVariazioniAziendaliVO.getIdVariazioneAziendale()))
          queryBuf.append("AND DVA.ID_TIPO_VARIAZIONE_AZIENDA = ? ");
        if (Validator.isNotEmpty(filtriRicercaVariazioniAziendaliVO.getIdTipoTipologiaVariazione()))
          queryBuf.append("AND TVA.ID_TIPO_TIPOLOGIA_VARIAZIONE = ? ");
        if (filtriRicercaVariazioniAziendaliVO.getDataVariazioneDal()!=null)
          queryBuf.append("AND TRUNC(DVA.DATA_VARIAZIONE)  >= ? ");
        if (filtriRicercaVariazioniAziendaliVO.getDataVariazioneAl()!=null)
          queryBuf.append("AND TRUNC(DVA.DATA_VARIAZIONE)  <= ? ");
        if (Validator.isNotEmpty(filtriRicercaVariazioniAziendaliVO.getCuaa()))
          queryBuf.append("AND AA.CUAA= ? ");
        if (!filtriRicercaVariazioniAziendaliVO.isVariazioniStoricizzate())
          queryBuf.append("AND VA.FLAG_STORICIZZATO = '").append(SolmrConstants.FLAG_N).append("' ");
        if (Validator.isNotEmpty(filtriRicercaVariazioniAziendaliVO.getIstatComuneRicerca()))
          queryBuf.append("AND AA.SEDELEG_COMUNE = ? ");
      }
      
      //L’estrazione delle variazioni è soggetta al ruolo di appartenenza dell’utente ovvero
      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        //Filtrare le aziende che hanno delega attiva (db_delega.data_fine=null) 
        queryBuf.append("AND AA.ID_AZIENDA=D.ID_AZIENDA ");
        queryBuf.append("AND D.DATA_FINE IS NULL ");
        //porre id_procedimento=7, 
        queryBuf.append("AND D.ID_PROCEDIMENTO = ").append(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE).append(" ");
        //quindi mettere in join con la tabella degli intermediari db_intermediario 
        queryBuf.append("AND D.ID_INTERMEDIARIO=I.ID_INTERMEDIARIO ");
        //filtrando per tipo_intermediario=’C’ (Ovverro i CAA).   
        queryBuf.append("AND I.TIPO_INTERMEDIARIO='C' ");
        
        // Quindi se l’utente connesso appartiene a:
        // CAA Regionale:
        // filtrare per substr(db_intermediario.codice_fiscale,1, 3)= ai primi tre caratteri del codice fiscale del CAA connesso)
        if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_REGIONALE")))
        {
          queryBuf.append("AND SUBSTR(I.CODICE_FISCALE, 1, 3) = ? ");
        }
        // CAA Provinciale:
        // filtrare per substr(db_intermediario.codice_fiscale,1, 6)= ai primi sei caratteri del codice fiscale del CAA connesso)
        else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_PROVINCIALE")))
        {
          queryBuf.append("AND SUBSTR(I.CODICE_FISCALE, 1, 6) = ? ");
        }
        // CAA di Zona
        // filtrare per db_intermediario.codice_fiscale= codice fiscale del CAA connesso
        else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_UFFICIO_ZONA")))
        {
          queryBuf.append("AND I.CODICE_FISCALE = ? ");
        }
      }
      
      //Se l'utente ha impostato un filtro di ricerca lo uso, altrimenti uso quello di default
      if (filtriRicercaVariazioniAziendaliVO.getCampiPerOrderBy()!=null) 
        queryBuf.append(filtriRicercaVariazioniAziendaliVO.getCampiPerOrderBy().getOrderBy()).append(" ");
      
      //Parte relativa alla query vera e propria end
      
      //Parte per il rownum (paginazione)
      queryBuf.append(") PAGE ");
      queryBuf.append(") ");
      if (!excel)
        queryBuf.append("WHERE RID BETWEEN ? AND ?  ");
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::getRigheRicercaVariazioni] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      int indice=1;
      
      stmt.setInt(indice++, ruoloUtenza.getTipoGruppoRuolo().getCode().intValue());
      
      if (filtriRicercaVariazioniAziendaliVO!=null)
      {
        if (filtriRicercaVariazioniAziendaliVO.getPresaVisione()!=null)
          stmt.setInt(indice++, ruoloUtenza.getTipoGruppoRuolo().getCode().intValue());
        //Applico gli eventuali filtri inseriti dall'utente       
        if (Validator.isNotEmpty(filtriRicercaVariazioniAziendaliVO.getIstatProvAmmComp()))
          stmt.setString(indice++, filtriRicercaVariazioniAziendaliVO.getIstatProvAmmComp());
        if (Validator.isNotEmpty(filtriRicercaVariazioniAziendaliVO.getIdVariazioneAziendale()))
          stmt.setString(indice++, filtriRicercaVariazioniAziendaliVO.getIdVariazioneAziendale());
        if (Validator.isNotEmpty(filtriRicercaVariazioniAziendaliVO.getIdTipoTipologiaVariazione()))
          stmt.setString(indice++, filtriRicercaVariazioniAziendaliVO.getIdTipoTipologiaVariazione());
        if (filtriRicercaVariazioniAziendaliVO.getDataVariazioneDal()!=null)
          stmt.setDate(indice++, new java.sql.Date(filtriRicercaVariazioniAziendaliVO.getDataVariazioneDal().getTime()));
        if (filtriRicercaVariazioniAziendaliVO.getDataVariazioneAl()!=null)
          stmt.setDate(indice++, new java.sql.Date(filtriRicercaVariazioniAziendaliVO.getDataVariazioneAl().getTime()));
        if (Validator.isNotEmpty(filtriRicercaVariazioniAziendaliVO.getCuaa()))
          stmt.setString(indice++, filtriRicercaVariazioniAziendaliVO.getCuaa().toUpperCase());
        if (Validator.isNotEmpty(filtriRicercaVariazioniAziendaliVO.getIstatComuneRicerca()))
          stmt.setString(indice++, filtriRicercaVariazioniAziendaliVO.getIstatComuneRicerca());
      }
      
      //L’estrazione delle variazioni è soggetta al ruolo di appartenenza dell’utente ovvero
      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        // Se il livello intermediario è "R"
        if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_REGIONALE")))
        {
          stmt.setString(indice++, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getCodiceEnte()
              .substring(0, 3));
        }
        // Se il livello intermediario è "P"
        else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_PROVINCIALE")))
        {
          stmt.setString(indice++, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getCodiceEnte()
              .substring(0, 6));
        }
        // Se il livello intermediario è "Z"
        else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_UFFICIO_ZONA")))
        {
          stmt.setString(indice++, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getCodiceEnte());
        }
      }
      
      //Se sto generando il file voglio tutti i record quindi non devo inserire gli estremi
      if (!excel)
      {
        stmt.setInt(indice++, filtriRicercaVariazioniAziendaliVO.getPrimoElemento()+1);//estremo inferiore di ricerca
        stmt.setInt(indice++, filtriRicercaVariazioniAziendaliVO.getPrimoElemento()+filtriRicercaVariazioniAziendaliVO.getPasso());//estremo superiore di ricerca
      }
      ResultSet rs = stmt.executeQuery();
      hmRighe = new Vector<RigaRicercaVariazioniAziendaliVO>();
      while (rs.next())
      {
        RigaRicercaVariazioniAziendaliVO rigaRicercaVariazioniAziendaliVO = new RigaRicercaVariazioniAziendaliVO();
        
        rigaRicercaVariazioniAziendaliVO.setDescProvAmmComp(rs.getString("PROV_COMP"));
        rigaRicercaVariazioniAziendaliVO.setCuaa(rs.getString("CUAA"));
        rigaRicercaVariazioniAziendaliVO.setPartitaIva(rs.getString("PARTITA_IVA"));
        rigaRicercaVariazioniAziendaliVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        
        //Se il comune è in uno stato estero visualizzo la citta estera
        if ( SolmrConstants.FLAG_S.equals(rs.getString("FLAG_ESTERO")))
          rigaRicercaVariazioniAziendaliVO.setComune(rs.getString("SEDELEG_CITTA_ESTERO"));
        else  rigaRicercaVariazioniAziendaliVO.setComune(rs.getString("COMUME_SEDE_LEGALE"));
        
        rigaRicercaVariazioniAziendaliVO.setIndirizzo(rs.getString("SEDELEG_INDIRIZZO"));
        rigaRicercaVariazioniAziendaliVO.setCap(rs.getString("SEDELEG_CAP"));
        rigaRicercaVariazioniAziendaliVO.setVariazione(rs.getString("VARIAZIONE"));
        rigaRicercaVariazioniAziendaliVO.setTipologiaVariazione(rs.getString("TIPOLOGIA_VARIAZIONE"));
        rigaRicercaVariazioniAziendaliVO.setDataVariazione(rs.getDate("DATA_VARIAZIONE"));
        
        String temp=rs.getString("ID_VISIONE_VARIAZIONE_AZI");
        if (Validator.isNotEmpty(temp))
          rigaRicercaVariazioniAziendaliVO.setIdVisioneVariazioneAzi(new Long((temp)));
        
        temp=rs.getString("ID_DETTAGLIO_VARIAZIONE_AZI");
        if (Validator.isNotEmpty(temp))
          rigaRicercaVariazioniAziendaliVO.setIdDettaglioVariazioneAzi(new Long((temp)));
        
        if (SolmrConstants.FLAG_S.equals(rs.getString("FLAG_STORICIZZATO")))
          rigaRicercaVariazioniAziendaliVO.setFlagStoricizzato(true);
        
        hmRighe.add(rigaRicercaVariazioniAziendaliVO);   
      }
      return hmRighe.size() == 0 ? null : (RigaRicercaVariazioniAziendaliVO[]) hmRighe.toArray(new RigaRicercaVariazioniAziendaliVO[0]);
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("hmRighe", hmRighe) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("filtriRicercaVariazioniAziendaliVO", filtriRicercaVariazioniAziendaliVO) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[AnagrafeGaaDAO::getRigheRicercaVariazioni] ", t,
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
          "[AnagrafeGaaDAO::getRigheRicercaVariazioni] END.");
    }
  }
  
  
  /**
   * 
   * Restituisce il numero di variazioniaziendali trovate
   * 
   * @param filtriRicercaVariazioniAziendaliVO
   * @return
   * @throws DataAccessException
   */
  public int ricercaNumVariazioni(FiltriRicercaVariazioniAziendaliVO filtriRicercaVariazioniAziendaliVO,
                                  UtenteAbilitazioni utenteAbilitazioni, RuoloUtenza ruoloUtenza) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    int num = 0;
    try
    {
      SolmrLogger.debug(this,
          "[AnagrafeGaaDAO::ricercaNumVariazioni] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("SELECT COUNT(*) AS TOT  ");
      queryBuf.append("FROM DB_VARIAZIONE_AZIENDALE VA, DB_ANAGRAFICA_AZIENDA AA, DB_DETTAGLIO_VARIAZIONE_AZI DVA,PROVINCIA PC, ");
      queryBuf.append("DB_TIPO_VARIAZIONE_AZIENDA TVA, DB_TIPO_TIPOLOGIA_VARIAZIONE TTV ");
      
      //L’estrazione delle variazioni è soggetta al ruolo di appartenenza dell’utente ovvero
      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        queryBuf.append(",DB_DELEGA D, DB_INTERMEDIARIO I ");
      }
      
      queryBuf.append("WHERE AA.ID_AZIENDA=VA.ID_AZIENDA ");
      queryBuf.append("AND AA.DATA_FINE_VALIDITA IS NULL ");
      queryBuf.append("AND DVA.ID_VARIAZIONE_AZIENDALE= VA.ID_VARIAZIONE_AZIENDALE ");
      queryBuf.append("AND AA.PROVINCIA_COMPETENZA= PC.ISTAT_PROVINCIA ");
      queryBuf.append("AND DVA.ID_TIPO_VARIAZIONE_AZIENDA=TVA.ID_TIPO_VARIAZIONE_AZIENDA ");
      queryBuf.append("AND TVA.ID_TIPO_TIPOLOGIA_VARIAZIONE=TTV.ID_TIPO_TIPOLOGIA_VARIAZIONE ");
      
      if (filtriRicercaVariazioniAziendaliVO!=null)
      {
        if (filtriRicercaVariazioniAziendaliVO.getPresaVisione()!=null)
        {
          if (filtriRicercaVariazioniAziendaliVO.getPresaVisione().booleanValue())
          {
            //è stato selezionato il filtro si quindi devo visualizzare solo i record con la presa visione
            //legata all'utente connesso
            queryBuf.append("AND DVA.ID_DETTAGLIO_VARIAZIONE_AZI IN ( ");
            queryBuf.append("SELECT ID_DETTAGLIO_VARIAZIONE_AZI FROM DB_VISIONE_VARIAZIONE_AZI WHERE ID_TIPO_GRUPPO_RUOLO =?) ");
          }
          else
          {
            //è stato selezionato il filtro no: devo fare vedere quelle non presenti o presenti ma legate a qualcun altro
            queryBuf.append("AND DVA.ID_DETTAGLIO_VARIAZIONE_AZI NOT IN ( ");
            queryBuf.append("SELECT ID_DETTAGLIO_VARIAZIONE_AZI FROM DB_VISIONE_VARIAZIONE_AZI WHERE ID_TIPO_GRUPPO_RUOLO =?) ");
          }
        }
        //Applico gli eventuali filtri inseriti dall'utente
        if (Validator.isNotEmpty(filtriRicercaVariazioniAziendaliVO.getIstatProvAmmComp()))
          queryBuf.append("AND AA.PROVINCIA_COMPETENZA = ? ");
        if (Validator.isNotEmpty(filtriRicercaVariazioniAziendaliVO.getIdVariazioneAziendale()))
          queryBuf.append("AND DVA.ID_TIPO_VARIAZIONE_AZIENDA = ? ");
        if (Validator.isNotEmpty(filtriRicercaVariazioniAziendaliVO.getIdTipoTipologiaVariazione()))
          queryBuf.append("AND TVA.ID_TIPO_TIPOLOGIA_VARIAZIONE = ? ");
        if (filtriRicercaVariazioniAziendaliVO.getDataVariazioneDal()!=null)
          queryBuf.append("AND TRUNC(DVA.DATA_VARIAZIONE)  >= ? ");
        if (filtriRicercaVariazioniAziendaliVO.getDataVariazioneAl()!=null)
          queryBuf.append("AND TRUNC(DVA.DATA_VARIAZIONE)  <= ? ");
        if (Validator.isNotEmpty(filtriRicercaVariazioniAziendaliVO.getCuaa()))
          queryBuf.append("AND AA.CUAA= ? ");
        if (!filtriRicercaVariazioniAziendaliVO.isVariazioniStoricizzate())
          queryBuf.append("AND VA.FLAG_STORICIZZATO = '").append(SolmrConstants.FLAG_N).append("' ");
        if (Validator.isNotEmpty(filtriRicercaVariazioniAziendaliVO.getIstatComuneRicerca()))
          queryBuf.append("AND AA.SEDELEG_COMUNE = ? ");
        
      }
      
      //L’estrazione delle variazioni è soggetta al ruolo di appartenenza dell’utente ovvero
      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        //Filtrare le aziende che hanno delega attiva (db_delega.data_fine=null) 
        queryBuf.append("AND AA.ID_AZIENDA=D.ID_AZIENDA ");
        queryBuf.append("AND D.DATA_FINE IS NULL ");
        //porre id_procedimento=7, 
        queryBuf.append("AND D.ID_PROCEDIMENTO = ").append(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE).append(" ");
        //quindi mettere in join con la tabella degli intermediari db_intermediario 
        queryBuf.append("AND D.ID_INTERMEDIARIO=I.ID_INTERMEDIARIO ");
        //filtrando per tipo_intermediario=’C’ (Ovverro i CAA).   
        queryBuf.append("AND I.TIPO_INTERMEDIARIO='C' ");
        
        // Quindi se l’utente connesso appartiene a:
        // CAA Regionale:
        // filtrare per substr(db_intermediario.codice_fiscale,1, 3)= ai primi tre caratteri del codice fiscale del CAA connesso)
        if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_REGIONALE")))
        {
          queryBuf.append("AND SUBSTR(I.CODICE_FISCALE, 1, 3) = ? ");
        }
        // CAA Provinciale:
        // filtrare per substr(db_intermediario.codice_fiscale,1, 6)= ai primi sei caratteri del codice fiscale del CAA connesso)
        else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_PROVINCIALE")))
        {
          queryBuf.append("AND SUBSTR(I.CODICE_FISCALE, 1, 6) = ? ");
        }
        // CAA di Zona
        // filtrare per db_intermediario.codice_fiscale= codice fiscale del CAA connesso
        else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_UFFICIO_ZONA")))
        {
          queryBuf.append("AND I.CODICE_FISCALE = ? ");
        }
      }
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      SolmrLogger.debug(this, "--- query ricercaNumVariazioni ="+query);
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::ricercaNumVariazioni] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice=1;
      
      if (filtriRicercaVariazioniAziendaliVO!=null)
      {
        if (filtriRicercaVariazioniAziendaliVO.getPresaVisione()!=null)
          stmt.setInt(indice++, ruoloUtenza.getTipoGruppoRuolo().getCode().intValue());
        //Applico gli eventuali filtri inseriti dall'utente
        if (Validator.isNotEmpty(filtriRicercaVariazioniAziendaliVO.getIstatProvAmmComp()))
          stmt.setString(indice++, filtriRicercaVariazioniAziendaliVO.getIstatProvAmmComp());
        if (Validator.isNotEmpty(filtriRicercaVariazioniAziendaliVO.getIdVariazioneAziendale()))
          stmt.setString(indice++, filtriRicercaVariazioniAziendaliVO.getIdVariazioneAziendale());
        if (Validator.isNotEmpty(filtriRicercaVariazioniAziendaliVO.getIdTipoTipologiaVariazione()))
          stmt.setString(indice++, filtriRicercaVariazioniAziendaliVO.getIdTipoTipologiaVariazione());
        if (filtriRicercaVariazioniAziendaliVO.getDataVariazioneDal()!=null)
          stmt.setDate(indice++, new java.sql.Date(filtriRicercaVariazioniAziendaliVO.getDataVariazioneDal().getTime()));
        if (filtriRicercaVariazioniAziendaliVO.getDataVariazioneAl()!=null)
          stmt.setDate(indice++, new java.sql.Date(filtriRicercaVariazioniAziendaliVO.getDataVariazioneAl().getTime()));
        if (Validator.isNotEmpty(filtriRicercaVariazioniAziendaliVO.getCuaa()))
          stmt.setString(indice++, filtriRicercaVariazioniAziendaliVO.getCuaa().toUpperCase());
        if (Validator.isNotEmpty(filtriRicercaVariazioniAziendaliVO.getIstatComuneRicerca()))
          stmt.setString(indice++, filtriRicercaVariazioniAziendaliVO.getIstatComuneRicerca());
      }
      
      //L’estrazione delle variazioni è soggetta al ruolo di appartenenza dell’utente ovvero
      if(utenteAbilitazioni.getRuolo().isUtenteIntermediario())
      {
        // Se il livello intermediario è "R"
        if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_REGIONALE")))
        {
          stmt.setString(indice++, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getCodiceEnte()
              .substring(0, 3));
        }
        // Se il livello intermediario è "P"
        else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_PROVINCIALE")))
        {
          stmt.setString(indice++, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getCodiceEnte()
              .substring(0, 6));
        }
        // Se il livello intermediario è "Z"
        else if (utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase(
            (String) SolmrConstants.get("LIVELLO_INTERMEDIARIO_UFFICIO_ZONA")))
        {
          stmt.setString(indice++, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getCodiceEnte());
        }
      }
      
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
        num=rs.getInt("TOT");
        
      return num;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("filtriRicercaVariazioniAziendaliVO", filtriRicercaVariazioniAziendaliVO) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[AnagrafeGaaDAO::ricercaNumVariazioni] ", t,
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
          "[AnagrafeGaaDAO::ricercaNumVariazioni] END.");
    }
  }
  
  
  /**
   * Iserisce n record sulla tabella DB_VISIONE_VARIAZIONE_AZI
   * 
   * @param elencoIdPresaVisione
   * @return
   * @throws DataAccessException
   */
  public void insertVisioneVariazione(Vector<Long> elencoIdPresaVisione, RuoloUtenza ruoloUtenza) 
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
              "[AnagrafeGaaDAO::insertVisioneVariazione] BEGIN.");
  
  
      queryBuf = new StringBuffer();
      queryBuf.append("INSERT INTO DB_VISIONE_VARIAZIONE_AZI(");
      queryBuf.append("ID_VISIONE_VARIAZIONE_AZI, ");
      queryBuf.append("ID_DETTAGLIO_VARIAZIONE_AZI, "); 
      queryBuf.append("DATA_VISIONE, ");
      queryBuf.append("ID_UTENTE, "); 
      queryBuf.append("ID_TIPO_GRUPPO_RUOLO) "); 
      queryBuf.append("VALUES(").append(SolmrConstants.SEQ_DB_VISIONE_VARIAZIONE_AZI).append(".NEXTVAL, ");
      queryBuf.append("?,SYSDATE,?,? )");
             
      
      query = queryBuf.toString();
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::insertVisioneVariazione] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int size=elencoIdPresaVisione.size();
      for (int i=0;i<size;i++)
      {
        stmt.setLong(1, ((Long)elencoIdPresaVisione.get(i)).longValue()); //ID_DETTAGLIO_VARIAZIONE_AZI
        stmt.setLong(2, ruoloUtenza.getIdUtente().longValue());
        stmt.setLong(3, ruoloUtenza.getTipoGruppoRuolo().getCode().intValue());
        stmt.addBatch();
      }
      
  
      stmt.executeBatch();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("elencoIdPresaVisione", elencoIdPresaVisione)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AnagrafeGaaDAO::insertVisioneVariazione] ",
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
              "[AnagrafeGaaDAO::insertVisioneVariazione] END.");
    }
  }
  
  
  /**
   * 
   * Restituiesce il totale della superficie
   * condotta e SAU delle aziende associate
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public BigDecimal[] getTOTSupCondottaAndSAU(long idAzienda) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    BigDecimal result[] = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[AnagrafeGaaDAO::getTOTSupCondottaAndSAU] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer(); 
      queryBuf
          .append("SELECT SUM ( " +
                  "             ( SELECT NVL(SUM(DCD.SUPERFICIE_CONDOTTA),0) " +
                  "               FROM   DB_DICHIARAZIONE_CONSISTENZA DDC, " + 
                  "                      DB_CONDUZIONE_DICHIARATA DCD " + 
                  "               WHERE  DDC.id_azienda = DAC.ID_AZIENDA_ASSOCIATA " + 
                  "               AND    DDC.CODICE_FOTOGRAFIA_TERRENI = DCD.CODICE_FOTOGRAFIA_TERRENI " + 
                  "               AND    DCD.ID_TITOLO_POSSESSO <> 5 " + 
                  "               AND    DDC.ID_MOTIVO_DICHIARAZIONE <> 7 " + 
                  "               AND    DDC.DATA_INSERIMENTO_DICHIARAZIONE = " +
                  "                      ( SELECT MAX(DATA_INSERIMENTO_DICHIARAZIONE) " + 
                  "                        FROM   DB_DICHIARAZIONE_CONSISTENZA DC, " + 
                  "                               DB_TIPO_MOTIVO_DICHIARAZIONE TI " + 
                  "                        WHERE  DC.ID_AZIENDA = DAC.ID_AZIENDA_ASSOCIATA " + 
                  "                        AND    DC.ID_MOTIVO_DICHIARAZIONE <> 7 " + 
                  "                        AND    TI.ID_MOTIVO_DICHIARAZIONE = DC.ID_MOTIVO_DICHIARAZIONE " + 
                  "                        AND    TI.TIPO_DICHIARAZIONE != 'C' " + 
                  "                      ) " + 
                  "             ) " +
                  "           ) SUP_CONDOTTA, " + 
                  "       SUM ( " +
                  "             ( SELECT NVL(SUM(DUD.superficie_utilizzata),0) " + 
                  "               FROM   DB_DICHIARAZIONE_CONSISTENZA DDC, " + 
                  "                      DB_CONDUZIONE_DICHIARATA DCD, " + 
                  "                      DB_UTILIZZO_DICHIARATO DUD, " + 
                  "                      DB_TIPO_UTILIZZO DTU " + 
                  "               WHERE  DDC.ID_AZIENDA = DAC.ID_AZIENDA_ASSOCIATA " + 
                  "               AND    DDC.CODICE_FOTOGRAFIA_TERRENI = DCD.CODICE_FOTOGRAFIA_TERRENI " + 
                  "               AND    DCD.ID_CONDUZIONE_DICHIARATA = DUD.ID_CONDUZIONE_DICHIARATA " + 
                  "               AND    DUD.ID_UTILIZZO = DTU.ID_UTILIZZO " + 
                  "               AND    DTU.FLAG_SAU = 'S' " +
                  "               AND    DCD.ID_TITOLO_POSSESSO <> 5 " + 
                  "               AND    DDC.ID_MOTIVO_DICHIARAZIONE <> 7 " + 
                  "               AND    DDC.DATA_INSERIMENTO_DICHIARAZIONE = " +
                  "                      ( SELECT MAX(DATA_INSERIMENTO_DICHIARAZIONE) " + 
                  "                        FROM   DB_DICHIARAZIONE_CONSISTENZA DC, " + 
                  "                               DB_TIPO_MOTIVO_DICHIARAZIONE TI " + 
                  "                        WHERE  DC.ID_AZIENDA=DAC.ID_AZIENDA_ASSOCIATA " + 
                  "                        AND    DC.ID_MOTIVO_DICHIARAZIONE <> 7 " + 
                  "                        AND    TI.ID_MOTIVO_DICHIARAZIONE = DC.ID_MOTIVO_DICHIARAZIONE " + 
                  "                        AND    TI.TIPO_DICHIARAZIONE != 'C' " + 
                  "                      ) " + 
                  "             ) " +
                  "           ) SUP_SAU " +
                  "FROM   DB_ANAGRAFICA_AZIENDA AA, " + 
                  "       DB_AZIENDA_COLLEGATA DAC, " + 
                  "       DB_SOGGETTO_ASSOCIATO DAS " + 
                  "WHERE  DAC.ID_AZIENDA = ? " + 
                  "AND    DAC.ID_AZIENDA_ASSOCIATA = AA.ID_AZIENDA(+) " + 
                  "AND    DAC.ID_SOGGETTO_ASSOCIATO = DAS.ID_SOGGETTO_ASSOCIATO(+) " +
                  "AND    NVL(TRUNC(DAC.DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > SYSDATE " +
                  "AND    NVL(TRUNC(DAC.DATA_USCITA), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > SYSDATE " +
                  "AND    AA.DATA_FINE_VALIDITA IS NULL " + 
                  "AND    AA.DATA_CESSAZIONE IS NULL " );
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::getTOTSupCondottaAndSAU] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      int indice = 0;
      stmt.setLong(++indice,idAzienda);
      
      
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        result = new BigDecimal[2];
        result[0] = rs.getBigDecimal("SUP_CONDOTTA");
        result[1] = rs.getBigDecimal("SUP_SAU");
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
              "[AnagrafeGaaDAO::getTOTSupCondottaAndSAU] ",
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
              "[AnagrafeGaaDAO::getTOTSupCondottaAndSAU] END.");
    }
  }
  
  /**
   * 
   * Restituisce se l'azienda ha la delega i dati dell'intermediario!!
   * 
   * 
   * @param ids
   * @return
   * @throws DataAccessException
   */
  public HashMap<Long,DelegaVO> getDelegaAndIntermediario(
      long ids[]) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    DelegaVO delegaVO = null;
    HashMap<Long,DelegaVO> hmRighe = null;
    try
    {
      SolmrLogger.debug(this,
          "[AnagrafeGaaDAO::getDelegaAndIntermediario] BEGIN.");
      
      
      
      conn = getDatasource().getConnection();
      
      Long idJavaIns = getNextPrimaryKey(SolmrConstants.SEQ_SMRGAA_W_JAVA_INSERT);

      String insert="INSERT INTO SMRGAA_W_JAVA_INSERT (ID_JAVA_INSERT, ID_DETTAGLIO_INS) "+
                    "VALUES ( ?, ?) ";

      stmt = conn.prepareStatement(insert);

      for (int i=0;i<ids.length;i++)
      {
        stmt.setLong(1,idJavaIns.longValue());
        stmt.setLong(2,ids[i]);
        stmt.addBatch();
      }
      stmt.executeBatch();
      

      SolmrLogger.debug(this,"[AnagrafeGaaDAO::getDelegaAndIntermediario] insert executed");

      stmt.close();
      
      
      
      
      
      
      
  
      queryBuf = new StringBuffer();
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT " +
        "       DD.ID_INTERMEDIARIO, " +
        "       DD.CODICE_AMMINISTRAZIONE, " +
        "       DD.ID_AZIENDA, " +
        "       I.DENOMINAZIONE, " +
        "       UZI.CODICE_AGEA " +
        "FROM   DB_DELEGA DD, " +
        "       DB_INTERMEDIARIO I, " +
        "       DB_UFFICIO_ZONA_INTERMEDIARIO UZI, " +
        "       SMRGAA_W_JAVA_INSERT WIJ "+
        "WHERE  DD.ID_PROCEDIMENTO = 7 " +
        "AND    DD.DATA_FINE IS NULL " +
        "AND    DD.ID_INTERMEDIARIO = I.ID_INTERMEDIARIO " +
        "AND    I.ID_INTERMEDIARIO = UZI.ID_INTERMEDIARIO " +
        "AND    DD.ID_AZIENDA = WIJ.ID_DETTAGLIO_INS "+
        "AND    WIJ.ID_JAVA_INSERT = ? ");
      
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      //conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::getDelegaAndIntermediario] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1,idJavaIns.longValue());
      
      
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {
        delegaVO = new DelegaVO();
        delegaVO.setIdIntermediario(checkLongNull(rs.getString("ID_INTERMEDIARIO")));
        delegaVO.setCodiceAgea(rs.getString("CODICE_AGEA"));
        delegaVO.setDenomIntermediario(rs.getString("DENOMINAZIONE"));
        delegaVO.setCodiceAmministrazione(rs.getString("CODICE_AMMINISTRAZIONE"));
        
        Long idAzienda = new Long(rs.getLong("ID_AZIENDA"));
        
        if(hmRighe == null)
        {
          hmRighe = new HashMap<Long,DelegaVO>();
        }
        hmRighe.put(idAzienda, delegaVO);
      }
      
      return hmRighe;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("hmRighe", hmRighe),
          new Variabile("delegaVO", delegaVO),
          new Variabile("hmRighe", hmRighe) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("ids", ids) };
  
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[AnagrafeGaaDAO::getDelegaAndIntermediario] ", t,
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
          "[AnagrafeGaaDAO::getDelegaAndIntermediario] END.");
    }
  }
  
  /**
   * 
   * Restituisce true se idAziendaSearch è figlia di 
   * idAzienda
   * 
   * 
   * @param idAzienda
   * @param idAziendaSearch
   * @return
   * @throws DataAccessException
   */
  public boolean isAziendeCollegataFiglia(
      long idAzienda, long idAziendaSearch) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean trovato = false;
    try
    {
      SolmrLogger.debug(this, "[AnagrafeGaaDAO::isAziendeCollegataFiglia] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf = new StringBuffer(" " +
        "SELECT ID_AZIENDA "+ 
        "FROM   DB_AZIENDA_COLLEGATA " +
        "WHERE  ID_AZIENDA = ? " +
        "AND    ID_AZIENDA_ASSOCIATA = ? " +
        "AND    NVL(TRUNC(DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > SYSDATE");

      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[AnagrafeGaaDAO::isAziendeCollegataFiglia] Query="
            + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idAzienda);
      stmt.setLong(++indice, idAziendaSearch);      

      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {
        trovato = true;
      }
      
      return trovato;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("trovato", trovato) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
          new Parametro("idAziendaSearch", idAziendaSearch)  };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[AnagrafeGaaDAO::isAziendeCollegataFiglia] ", t,
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
      SolmrLogger.debug(this, "[AnagrafeGaaDAO::isAziendeCollegataFiglia] END.");
    }
  }
  
  
  /**
   * Verifica se l'azienda in questione ha aziende collegate
   * oppure è collegata ad un'altra azienda, 
   * per la visualizzazione del menu'..
   * 
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public boolean isAziendeCollegataMenu(
      long idAzienda) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean trovato = false;
    try
    {
      SolmrLogger.debug(this, "[AnagrafeGaaDAO::isAziendeCollegataMenu] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf = new StringBuffer(" " +
        "SELECT ID_AZIENDA "+ 
        "FROM   DB_AZIENDA_COLLEGATA " +
        "WHERE  ID_AZIENDA = ? " +
        "AND    NVL(TRUNC(DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > SYSDATE " +
        "UNION ALL " +
        "SELECT ID_AZIENDA " + 
        "FROM   DB_AZIENDA_COLLEGATA " +
        "WHERE  ID_AZIENDA_ASSOCIATA = ? " +
        "AND    NVL(TRUNC(DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > SYSDATE ");

      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[AnagrafeGaaDAO::isAziendeCollegataMenu] Query="
            + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idAzienda);
      stmt.setLong(++indice, idAzienda);      

      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {
        trovato = true;
      }
      
      return trovato;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("trovato", trovato) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda)  };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[AnagrafeGaaDAO::isAziendeCollegataMenu] ", t,
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
      SolmrLogger.debug(this, "[AnagrafeGaaDAO::isAziendeCollegataMenu] END.");
    }
  }
  
  /**
   * 
   * usata nello scarico excel elenco soci ditte UMA
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<AziendaUmaExcelVO> getScaricoExcelElencoSociUma(
      long idAzienda) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    AziendaUmaExcelVO aziendaUmaExcelVO = null;
    Vector<AziendaUmaExcelVO> result = null;
    Vector<ColturaUmaVO> vColturaUma = null;
    try
    {
      SolmrLogger.debug(this, "[AnagrafeGaaDAO::getScaricExcelElencoSociUma] BEGIN.");
      
  
      queryBuf = new StringBuffer();
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "WITH DICH_CONS AS " +
        "   (SELECT MAX(DC.DATA_INSERIMENTO_DICHIARAZIONE) AS MAX_DATA, " +
        "           DAC.ID_AZIENDA_ASSOCIATA " +
        "    FROM   DB_DICHIARAZIONE_CONSISTENZA DC, " + 
        "           DB_TIPO_MOTIVO_DICHIARAZIONE TI, " +
        "           DB_AZIENDA_COLLEGATA DAC " +
        "    WHERE  DAC.ID_AZIENDA_ASSOCIATA = DC.ID_AZIENDA (+) " + 
        "    AND    DC.ID_MOTIVO_DICHIARAZIONE (+) <> 7 " +
        "    AND    DC.ID_MOTIVO_DICHIARAZIONE = TI.ID_MOTIVO_DICHIARAZIONE (+) " + 
        "    AND    TI.TIPO_DICHIARAZIONE (+) != 'C' " +
        "    AND    DAC.ID_AZIENDA = ? " +
        "    GROUP BY DAC.ID_AZIENDA_ASSOCIATA " + 
        "   ) " +
        "SELECT " +
        "       NVL2 (DAC.ID_AZIENDA_ASSOCIATA, AA.CUAA, DAS.CUAA) AS CUAA, " +
        "       NVL2 (DAC.ID_AZIENDA_ASSOCIATA, AA.PARTITA_IVA, DAS.PARTITA_IVA) AS PARTITA_IVA, " +
        "       NVL2 (DAC.ID_AZIENDA_ASSOCIATA, AA.DENOMINAZIONE, DAS.DENOMINAZIONE) AS DENOMINAZIONE, " + 
        "       CO.DESCOM||' '||'('||PR.SIGLA_PROVINCIA||')' AS COMUNE, " +
        "       NVL2 (DAC.ID_AZIENDA_ASSOCIATA, AA.SEDELEG_INDIRIZZO, DAS.INDIRIZZO) AS INDIRIZZO, " + 
        "       NVL2 (DAC.ID_AZIENDA_ASSOCIATA, AA.SEDELEG_CAP, DAS.CAP) AS CAP, " +
        "       AA.CCIAA_NUMERO_REGISTRO_IMPRESE, " +
        "       DAC.DATA_INGRESSO, " +
        "       DAC.DATA_INIZIO_VALIDITA, " +
        "       TO_CHAR(DAC.DATA_AGGIORNAMENTO,'dd/mm/yyyy')||'-'" +
        "       ||(SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
        "          || ' ' " + 
        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
        "         WHERE DAC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
        "       ||(SELECT PVU.DENOMINAZIONE " +
        "          FROM PAPUA_V_UTENTE_LOGIN PVU " +
        "          WHERE DAC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
        "        AS DATI_ULTIMO_AGGIORNAMENTO, " + 
        "       DC.DATA AS DATA_ULTIMA_VALIDAZIONE, " +
        "       (SELECT NVL(SUM (DCD.SUPERFICIE_CONDOTTA), 0) " +
        "        FROM   DB_DICHIARAZIONE_CONSISTENZA DDC, " +
        "               DB_CONDUZIONE_DICHIARATA DCD " +
        "        WHERE  DDC.ID_AZIENDA = DAC.ID_AZIENDA_ASSOCIATA " + 
        "        AND    DDC.CODICE_FOTOGRAFIA_TERRENI = DCD.CODICE_FOTOGRAFIA_TERRENI " + 
        "        AND    DCD.ID_TITOLO_POSSESSO <> 5 " +
        "        AND    DDC.ID_MOTIVO_DICHIARAZIONE <> 7 " +
        "        AND    DDC.DATA_INSERIMENTO_DICHIARAZIONE = NEWDC.MAX_DATA" +
        "       ) AS TOT_SUP_COND, " +
        "       PR2.SIGLA_PROVINCIA, " +                                                         
        "       DU.DITTA_UMA, " +
        "       UMA.DESCR AS COLTURA_UMA, " +
        "       UMA.SUP AS SUPERFICIE_UMA " +
        "FROM   DB_ANAGRAFICA_AZIENDA AA, " +
        "       DB_AZIENDA_COLLEGATA DAC, " +
        "       DB_SOGGETTO_ASSOCIATO DAS, " +
        "       COMUNE CO, " +
        "       PROVINCIA PR, " +
        "       DICH_CONS NEWDC, " +
        "       DB_DICHIARAZIONE_CONSISTENZA DC, " + 
        "       DB_DITTA_UMA DU, " +
        "       PROVINCIA PR2, " +
        "       (SELECT NVL(SUM (DUD.SUPERFICIE_UTILIZZATA), 0) SUP, " +
        "               TCU.DESCRIZIONE DESCR, " +
        "               DDC.ID_AZIENDA " +    
        "        FROM   DB_DICHIARAZIONE_CONSISTENZA DDC, " +
        "               DB_CONDUZIONE_DICHIARATA DCD, " +
        "               DB_UTILIZZO_DICHIARATO DUD, " +        
        "               DB_TIPO_COLTURA TCU, " +
        "               DICH_CONS, " +
        "               DB_R_CATALOGO_MATRICE_UMA CMU, " +
        "               DB_STORICO_PARTICELLA SP " +
        "        WHERE  DDC.ID_AZIENDA = DICH_CONS.ID_AZIENDA_ASSOCIATA " +
        "        AND DDC.CODICE_FOTOGRAFIA_TERRENI = DCD.CODICE_FOTOGRAFIA_TERRENI " + 
        "        AND DCD.ID_CONDUZIONE_DICHIARATA = DUD.ID_CONDUZIONE_DICHIARATA " +
        "        AND DCD.ID_TITOLO_POSSESSO <> 5 " +
        "        AND DDC.ID_MOTIVO_DICHIARAZIONE <> 7 " + 
        "        AND DDC.DATA_INSERIMENTO_DICHIARAZIONE = DICH_CONS.MAX_DATA " +    
        "        AND DUD.ID_CATALOGO_MATRICE = CMU.EXT_ID_CATALOGO_MATRICE " +
        "        AND DCD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
        "        AND TCU.ID_COLTURA = PACK_IMPORTA_DATI.DecodeColturaUma(DUD.ID_CATALOGO_MATRICE, SP.FLAG_IRRIGABILE, DDC.DATA_INSERIMENTO_DICHIARAZIONE,CMU.ID_COLTURA) " +
        "        GROUP BY  TCU.DESCRIZIONE, " +
        "                  DDC.ID_AZIENDA " +    
        "       ) UMA " + 
        "WHERE  DAC.ID_AZIENDA_ASSOCIATA = AA.ID_AZIENDA(+) " +
        "AND    DAC.ID_SOGGETTO_ASSOCIATO = DAS.ID_SOGGETTO_ASSOCIATO(+) " +
        "AND    NVL(DAC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) > SYSDATE " +
        "AND    AA.DATA_FINE_VALIDITA IS NULL " +    
        "AND    NVL2 (DAC.ID_AZIENDA_ASSOCIATA, AA.SEDELEG_COMUNE, DAS.COMUNE) =  CO.ISTAT_COMUNE " +
        "AND    CO.ISTAT_PROVINCIA = PR.ISTAT_PROVINCIA " +
        "AND    DAC.ID_AZIENDA_ASSOCIATA = NEWDC.ID_AZIENDA_ASSOCIATA (+) " + 
        "AND    NEWDC.MAX_DATA = DC.DATA_INSERIMENTO_DICHIARAZIONE (+) " +
        "AND    NEWDC.ID_AZIENDA_ASSOCIATA = DC.ID_AZIENDA (+) " +
        "AND    AA.ID_AZIENDA = DU.EXT_ID_AZIENDA(+) " +
        "AND    DU.EXT_PROVINCIA_UMA = PR2.ISTAT_PROVINCIA(+) " +
        "AND    AA.ID_AZIENDA = UMA.ID_AZIENDA(+) " +
        "AND    DAC.ID_AZIENDA = ? " +
        "ORDER BY DENOMINAZIONE, CUAA ");
        
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      //conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::getScaricExcelElencoSociUma] Query="
                + query);
      }
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);
      int idx = 0;
      
      stmt.setLong(++idx, idAzienda);
      stmt.setLong(++idx, idAzienda);
      
      
      ResultSet rs = stmt.executeQuery();
      String cuaaTmp = "";
      while (rs.next())
      {
        //prima volta
        if(result == null)
        {
          result = new Vector<AziendaUmaExcelVO>();
          aziendaUmaExcelVO = new AziendaUmaExcelVO();
          vColturaUma = new Vector<ColturaUmaVO>();
          aziendaUmaExcelVO.setColturaUma(vColturaUma);
        }
        String cuaa = rs.getString("CUAA"); 
        if(!cuaaTmp.equalsIgnoreCase(cuaa))
        {
          //dalla seconda volta
          if(Validator.isNotEmpty(cuaaTmp))
          {
            //aziendaUmaExcelVO.setColturaUma(vColturaUma);
            result.add(aziendaUmaExcelVO);
            aziendaUmaExcelVO = new AziendaUmaExcelVO();
            vColturaUma = new Vector<ColturaUmaVO>();
            aziendaUmaExcelVO.setColturaUma(vColturaUma);
          }
          
          
          aziendaUmaExcelVO.setCuaa(cuaa);
          aziendaUmaExcelVO.setPartitaIva(rs.getString("PARTITA_IVA"));
          aziendaUmaExcelVO.setDenominazione(rs.getString("DENOMINAZIONE"));
          aziendaUmaExcelVO.setComune(rs.getString("COMUNE"));
          aziendaUmaExcelVO.setIndirizzo(rs.getString("INDIRIZZO"));
          aziendaUmaExcelVO.setCap(rs.getString("CAP"));
          aziendaUmaExcelVO.setDataIngresso(rs.getTimestamp("DATA_INGRESSO"));
          aziendaUmaExcelVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
          aziendaUmaExcelVO.setDataUltimaValidazione(rs.getTimestamp("DATA_ULTIMA_VALIDAZIONE"));
          aziendaUmaExcelVO.setDatiUltimoAggiornamento(rs.getString("DATI_ULTIMO_AGGIORNAMENTO"));
          aziendaUmaExcelVO.setSuperficieCondotta(rs.getBigDecimal("TOT_SUP_COND"));
          aziendaUmaExcelVO.setProvDittaUma(rs.getString("SIGLA_PROVINCIA"));
          aziendaUmaExcelVO.setNumDittaUma(checkLongNull(rs.getString("DITTA_UMA")));
          aziendaUmaExcelVO.setNumeroRegistroImprese(rs.getString("CCIAA_NUMERO_REGISTRO_IMPRESE"));
            
          
          
          
          
        }
        ColturaUmaVO colturaUmaVO = new ColturaUmaVO();
        colturaUmaVO.setColturaUma(rs.getString("COLTURA_UMA"));
        colturaUmaVO.setSuperficieUma(rs.getBigDecimal("SUPERFICIE_UMA"));
        aziendaUmaExcelVO.getColturaUma().add(colturaUmaVO);
        
        
        
        cuaaTmp = cuaa;
      }
      
      //aziendaUmaExcelVO.setColturaUma(vColturaUma);
      result.add(aziendaUmaExcelVO);
      
      return result;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("aziendaUmaExcelVO", aziendaUmaExcelVO),
          new Variabile("result", result),
          new Variabile("vColturaUma", vColturaUma) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
  
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[AnagrafeGaaDAO::getScaricExcelElencoSociUma] ", t,
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
          "[AnagrafeGaaDAO::getScaricExcelElencoSociUma] END.");
    }
  }
  
  
  public Vector<AziendaColVarExcelVO> getScaricoExcelElencoSociColturaVarieta(
      long idAzienda) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    AziendaColVarExcelVO aziendaColVarExcelVO = null;
    Vector<AziendaColVarExcelVO> result = null;
    Vector<ColturaVarietaVO> vColturaVarieta = null;
    try
    {
      SolmrLogger.debug(this, "[AnagrafeGaaDAO::getScaricoExcelElencoSociColturaVarieta] BEGIN.");
      
  
      queryBuf = new StringBuffer();
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        " WITH DICH_CONS AS " +
        "   (SELECT MAX(DC.DATA_INSERIMENTO_DICHIARAZIONE) AS MAX_DATA, " +
        "           DAC.ID_AZIENDA_ASSOCIATA " +
        "    FROM   DB_DICHIARAZIONE_CONSISTENZA DC, " +  
        "           DB_TIPO_MOTIVO_DICHIARAZIONE TI, " +
        "           DB_AZIENDA_COLLEGATA DAC " +
        "    WHERE  DAC.ID_AZIENDA_ASSOCIATA = DC.ID_AZIENDA (+) " +  
        "    AND    DC.ID_MOTIVO_DICHIARAZIONE (+) <> 7 " +
        "    AND    DC.ID_MOTIVO_DICHIARAZIONE = TI.ID_MOTIVO_DICHIARAZIONE (+) " +
        "    AND    TI.TIPO_DICHIARAZIONE (+) != 'C' " +
        "    AND    DAC.ID_AZIENDA = ? " +
        "    GROUP BY DAC.ID_AZIENDA_ASSOCIATA " +  
        "   ) " +
        "SELECT " +
        "       NVL2 (DAC.ID_AZIENDA_ASSOCIATA, AA.CUAA, DAS.CUAA) AS CUAA, " +
        "       NVL2 (DAC.ID_AZIENDA_ASSOCIATA, AA.PARTITA_IVA, DAS.PARTITA_IVA) AS PARTITA_IVA, " + 
        "       NVL2 (DAC.ID_AZIENDA_ASSOCIATA, AA.DENOMINAZIONE, DAS.DENOMINAZIONE) AS DENOMINAZIONE, " +  
        "       CO.DESCOM||' '||'('||PR.SIGLA_PROVINCIA||')' AS COMUNE, " +
        "       NVL2 (DAC.ID_AZIENDA_ASSOCIATA, AA.SEDELEG_INDIRIZZO, DAS.INDIRIZZO) AS INDIRIZZO, " +  
        "       NVL2 (DAC.ID_AZIENDA_ASSOCIATA, AA.SEDELEG_CAP, DAS.CAP) AS CAP, " +
        "       DAC.DATA_INGRESSO, " +
       // "       DAC.DATA_USCITA, " +
        "       DAC.DATA_INIZIO_VALIDITA, " +
       // "       DAC.DATA_FINE_VALIDITA, " +
        "       TO_CHAR(DAC.DATA_AGGIORNAMENTO,'dd/mm/yyyy')||'-'" +
        "       ||(SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
        "          || ' ' " + 
        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
        "         WHERE DAC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
        "       ||'-'" +
        "       ||(SELECT PVU.DENOMINAZIONE " +
        "          FROM PAPUA_V_UTENTE_LOGIN PVU " +
        "          WHERE DAC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
        "        AS DATI_ULTIMO_AGGIORNAMENTO, " +
        "       DC.DATA AS DATA_ULTIMA_VALIDAZIONE, " +                                                 
        "       UTILVAR.DESCR_UTIL, " +
        "       UTILVAR.DESCR_VAR, " +
        "       NVL(UTILVAR.SUP,0) AS SUPERFICIE_UTIL " + 
        "FROM   DB_ANAGRAFICA_AZIENDA AA, " +
        "       DB_AZIENDA_COLLEGATA DAC, " +
        "       DB_SOGGETTO_ASSOCIATO DAS, " +
      //  "       PAPUA_V_UTENTE_LOGIN PVU, " +
        "       COMUNE CO, " +
        "       PROVINCIA PR, " +
        "       DICH_CONS NEWDC, " +
        "       DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "       (SELECT NVL(SUM (DUD.SUPERFICIE_UTILIZZATA), 0) SUP, " + 
        "               TU.DESCRIZIONE DESCR_UTIL, " +
        "               TV.DESCRIZIONE DESCR_VAR, " +
        "               DDC.ID_AZIENDA " +    
        "        FROM   DB_DICHIARAZIONE_CONSISTENZA DDC, " + 
        "               DB_CONDUZIONE_DICHIARATA DCD, " +
        "               DB_UTILIZZO_DICHIARATO DUD, " +
        "               DB_TIPO_UTILIZZO TU, " +
        "               DB_TIPO_VARIETA TV, " +
        "               DICH_CONS " +
        "        WHERE  DDC.ID_AZIENDA = DICH_CONS.ID_AZIENDA_ASSOCIATA " +
        "        AND DDC.CODICE_FOTOGRAFIA_TERRENI = DCD.CODICE_FOTOGRAFIA_TERRENI " +  
        "        AND DCD.ID_CONDUZIONE_DICHIARATA = DUD.ID_CONDUZIONE_DICHIARATA " +
        "        AND DCD.ID_TITOLO_POSSESSO <> 5 " +
        "        AND DDC.ID_MOTIVO_DICHIARAZIONE <> 7 " +  
        "        AND DDC.DATA_INSERIMENTO_DICHIARAZIONE = DICH_CONS.MAX_DATA " + 
        "        AND DUD.ID_UTILIZZO = TU.ID_UTILIZZO " +
        "        AND DUD.ID_UTILIZZO  = TV.ID_UTILIZZO " +
        "        AND DUD.ID_VARIETA = TV.ID_VARIETA " +
        "        GROUP BY  TU.DESCRIZIONE, TV.DESCRIZIONE, " + 
        "                  DDC.ID_AZIENDA " +
        "       ) UTILVAR  " +
        "WHERE  DAC.ID_AZIENDA_ASSOCIATA = AA.ID_AZIENDA(+) " +
        "AND    NVL(DAC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) > SYSDATE " +
        "AND    DAC.ID_SOGGETTO_ASSOCIATO = DAS.ID_SOGGETTO_ASSOCIATO(+) " +  
        "AND    AA.DATA_FINE_VALIDITA IS NULL " +
        //Richiesta Ravera 03/01/2014
        //"AND    AA.DATA_CESSAZIONE IS NULL " +
      //  "AND    DAC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
        "AND    NVL2 (DAC.ID_AZIENDA_ASSOCIATA, AA.SEDELEG_COMUNE, DAS.COMUNE) =  CO.ISTAT_COMUNE " +
        "AND    CO.ISTAT_PROVINCIA = PR.ISTAT_PROVINCIA " +
        "AND    DAC.ID_AZIENDA_ASSOCIATA = NEWDC.ID_AZIENDA_ASSOCIATA (+) " +  
        "AND    NEWDC.MAX_DATA = DC.DATA_INSERIMENTO_DICHIARAZIONE (+) " +
        "AND    NEWDC.ID_AZIENDA_ASSOCIATA = DC.ID_AZIENDA (+) " +
        "AND    AA.ID_AZIENDA = UTILVAR.ID_AZIENDA (+) " + 
        "AND    DAC.ID_AZIENDA = ? " +
        "ORDER BY DENOMINAZIONE, CUAA, UTILVAR.DESCR_UTIL ");
        
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::getScaricoExcelElencoSociColturaVarieta] Query="
                + query);
      }
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);
      int idx = 0;
      
      stmt.setLong(++idx, idAzienda);
      stmt.setLong(++idx, idAzienda);
      
      
      ResultSet rs = stmt.executeQuery();
      String cuaaTmp = "";
      while (rs.next())
      {
        //prima volta
        if(result == null)
        {
          result = new Vector<AziendaColVarExcelVO>();
          aziendaColVarExcelVO = new AziendaColVarExcelVO();
          vColturaVarieta = new Vector<ColturaVarietaVO>();
          aziendaColVarExcelVO.setColturaVarieta(vColturaVarieta);
        }
        String cuaa = rs.getString("CUAA"); 
        if(!cuaaTmp.equalsIgnoreCase(cuaa))
        {
          //dalla seconda volta
          if(Validator.isNotEmpty(cuaaTmp))
          {
            result.add(aziendaColVarExcelVO);
            aziendaColVarExcelVO = new AziendaColVarExcelVO();
            vColturaVarieta = new Vector<ColturaVarietaVO>();
            aziendaColVarExcelVO.setColturaVarieta(vColturaVarieta);
          }
          
          
          aziendaColVarExcelVO.setCuaa(cuaa);
          aziendaColVarExcelVO.setPartitaIva(rs.getString("PARTITA_IVA"));
          aziendaColVarExcelVO.setDenominazione(rs.getString("DENOMINAZIONE"));
          aziendaColVarExcelVO.setComune(rs.getString("COMUNE"));
          aziendaColVarExcelVO.setIndirizzo(rs.getString("INDIRIZZO"));
          aziendaColVarExcelVO.setCap(rs.getString("CAP"));
          aziendaColVarExcelVO.setDataIngresso(rs.getTimestamp("DATA_INGRESSO"));
          //aziendaColVarExcelVO.setDataUscita(rs.getTimestamp("DATA_USCITA"));
          aziendaColVarExcelVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
          //aziendaColVarExcelVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
          aziendaColVarExcelVO.setDataUltimaValidazione(rs.getTimestamp("DATA_ULTIMA_VALIDAZIONE"));
          aziendaColVarExcelVO.setDatiUltimoAggiornamento(rs.getString("DATI_ULTIMO_AGGIORNAMENTO"));      
          
          
          
          
        }
        ColturaVarietaVO colturaVarietaVO = new ColturaVarietaVO();
        colturaVarietaVO.setDescUtilizzo(rs.getString("DESCR_UTIL"));
        colturaVarietaVO.setDescVarieta(rs.getString("DESCR_VAR"));
        colturaVarietaVO.setSuperficieUtilizzo(rs.getBigDecimal("SUPERFICIE_UTIL"));
        aziendaColVarExcelVO.getColturaVarieta().add(colturaVarietaVO);
        
        
        
        cuaaTmp = cuaa;
      }
      
      //aziendaUmaExcelVO.setColturaUma(vColturaUma);
      result.add(aziendaColVarExcelVO);
      
      return result;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("aziendaColVarExcelVO", aziendaColVarExcelVO),
          new Variabile("result", result),
          new Variabile("vColturaVarieta", vColturaVarieta) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
  
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[AnagrafeGaaDAO::getScaricoExcelElencoSociColturaVarieta] ", t,
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
          "[AnagrafeGaaDAO::getScaricoExcelElencoSociColturaVarieta] END.");
    }
  }
  
  
  
  public Vector<AziendaColVarExcelVO> getScaricoExcelElencoSociFruttaGuscio(
      long idAzienda) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    AziendaColVarExcelVO aziendaColVarExcelVO = null;
    Vector<AziendaColVarExcelVO> result = null;
    Vector<FruttaGuscioVO> vFruttaGuscio = null;
    try
    {
      SolmrLogger.debug(this, "[AnagrafeGaaDAO::getScaricoExcelElencoSociFruttaGuscio] BEGIN.");
      
  
      queryBuf = new StringBuffer();
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "WITH DICH_CONS AS  ( " +
        "  SELECT  MAX(DC.DATA_INSERIMENTO_DICHIARAZIONE) AS MAX_DATA, " +
        "             DAC.ID_AZIENDA, " +
        "             DAC.ID_AZIENDA_ASSOCIATA, " +
        "             DAC.DATA_INGRESSO, " +
        "             DAC.DATA_INIZIO_VALIDITA, " +
        "             DAC.DATA_AGGIORNAMENTO, " +
        "             DAC.ID_UTENTE_AGGIORNAMENTO " +
        "      FROM   DB_DICHIARAZIONE_CONSISTENZA DC, " +   
        "             DB_TIPO_MOTIVO_DICHIARAZIONE TI, " +
        "             DB_AZIENDA_COLLEGATA DAC " +
        "      WHERE  DAC.ID_AZIENDA_ASSOCIATA = DC.ID_AZIENDA " +
        "      AND    DC.ID_MOTIVO_DICHIARAZIONE <> 7 " +
        "      AND    DC.ID_MOTIVO_DICHIARAZIONE = TI.ID_MOTIVO_DICHIARAZIONE " +
        "      AND    TI.TIPO_DICHIARAZIONE  != 'C' " +
        "      AND    DAC.ID_AZIENDA = ? " +
        "      AND    NVL(DAC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) > SYSDATE " +
        "      GROUP BY " +
        "             DAC.ID_AZIENDA, " + 
        "             DAC.ID_AZIENDA_ASSOCIATA, " +
        "             DAC.DATA_INGRESSO, " +
        "             DAC.DATA_INIZIO_VALIDITA, " + 
        "             DAC.DATA_AGGIORNAMENTO, " +
        "             DAC.ID_UTENTE_AGGIORNAMENTO " +
        "     ) " +
        "  SELECT AA.CUAA AS CUAA, " + 
        "         AA.PARTITA_IVA AS PARTITA_IVA, " +  
        "         AA.DENOMINAZIONE AS DENOMINAZIONE, " +  
        "         CO.DESCOM||' '||'('||PR.SIGLA_PROVINCIA||')' AS COMUNE, " + 
        "         AA.SEDELEG_INDIRIZZO AS INDIRIZZO, " +
        "         AA.SEDELEG_CAP AS CAP, " +
        "         NEWDC.DATA_INGRESSO, " +        
        "         NEWDC.DATA_INIZIO_VALIDITA, " +
        "         TO_CHAR(NEWDC.DATA_AGGIORNAMENTO,'dd/mm/yyyy')||'-'" +
        "         ||(SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
        "          || ' ' " + 
        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
        "         WHERE NEWDC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN)||'-'" +
        "         ||(SELECT PVU.DENOMINAZIONE " +
        "            FROM PAPUA_V_UTENTE_LOGIN PVU " +
        "            WHERE NEWDC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +   
        "          AS DATI_ULTIMO_AGGIORNAMENTO, " +
        "         DC.DATA AS DATA_ULTIMA_VALIDAZIONE, " +
        "         CO_PART.DESCOM AS DESCOM_PART, " +
        "         PR_PART.SIGLA_PROVINCIA AS SGLPROV_PART, " +
        "         SP.SEZIONE, " +
        "         SP.FOGLIO, " +
        "         SP.PARTICELLA, " +
        "         SP.SUBALTERNO, " +
        "         TU.CODICE AS COD_UTILIZZO, " +
        "         TU.DESCRIZIONE AS DESC_UTIL, " +
        "         TV.DESCRIZIONE AS DESC_VARIETA, " +
        "         DUD.ANNO_IMPIANTO, " +
        "         DUD.SESTO_TRA_FILE, " +
        "         DUD.SESTO_SU_FILE, " +
        "         DUD.NUMERO_PIANTE_CEPPI, " +
        "         DUD.SUPERFICIE_UTILIZZATA, " +
        "         SP.SUPERFICIE_GRAFICA, " +
        "         SP.ID_PARTICELLA, " +
        "         DCD.ID_CONDUZIONE_PARTICELLA, " +
        "         DCD.SUPERFICIE_CONDOTTA " +
        "  FROM   DB_ANAGRAFICA_AZIENDA AA, " +
        "         COMUNE CO, " +
        "         PROVINCIA PR, " +
        "         DICH_CONS NEWDC, " +
        "         DB_DICHIARAZIONE_CONSISTENZA DC, " + 
        "         DB_CONDUZIONE_DICHIARATA DCD, " +
        "         DB_UTILIZZO_DICHIARATO DUD, " +
        "         DB_TIPO_UTILIZZO TU, " +
        "         DB_TIPO_VARIETA TV, " +
        "         DB_STORICO_PARTICELLA SP, " +
        "         COMUNE CO_PART, " +
        "         PROVINCIA PR_PART " +
        "  WHERE  NEWDC.ID_AZIENDA = ? " + 
        "  AND    NEWDC.ID_AZIENDA_ASSOCIATA = AA.ID_AZIENDA " +
        "  AND    AA.DATA_FINE_VALIDITA IS NULL " +       
        "  AND    AA.SEDELEG_COMUNE = CO.ISTAT_COMUNE " +
        "  AND    CO.ISTAT_PROVINCIA = PR.ISTAT_PROVINCIA " +
        "  AND    NEWDC.MAX_DATA = DC.DATA_INSERIMENTO_DICHIARAZIONE " + 
        "  AND    NEWDC.ID_AZIENDA_ASSOCIATA = DC.ID_AZIENDA " +
        "  AND    DC.CODICE_FOTOGRAFIA_TERRENI = DCD.CODICE_FOTOGRAFIA_TERRENI " +   
        "  AND    DCD.ID_CONDUZIONE_DICHIARATA = DUD.ID_CONDUZIONE_DICHIARATA " +
        "  AND    DCD.ID_TITOLO_POSSESSO <> 5 " +
        "  AND    DC.ID_MOTIVO_DICHIARAZIONE <> 7 " +
        "  AND    DUD.ID_UTILIZZO = TU.ID_UTILIZZO " +
        "  AND    DUD.ID_VARIETA = TV.ID_VARIETA " +
        "  AND    TU.FLAG_FRUTTA_GUSCIO = 'S' " +
        "  AND    DCD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
        "  AND    SP.COMUNE =  CO_PART.ISTAT_COMUNE " +
        "  AND    CO_PART.ISTAT_PROVINCIA = PR_PART.ISTAT_PROVINCIA " +
        "UNION " +
        "  SELECT DAS.CUAA AS CUAA, " + 
        "         DAS.PARTITA_IVA AS PARTITA_IVA, " +  
        "         DAS.DENOMINAZIONE AS DENOMINAZIONE, " +   
        "         CO.DESCOM||' '||'('||PR.SIGLA_PROVINCIA||')' AS COMUNE, " +
        "         DAS.INDIRIZZO AS INDIRIZZO, " +
        "         DAS.CAP AS CAP, " +
        "         DAC.DATA_INGRESSO, " +
        "         DAC.DATA_INIZIO_VALIDITA, " + 
        "         TO_CHAR(DAC.DATA_AGGIORNAMENTO,'dd/mm/yyyy')||'-'" +
        "         ||(SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
        "          || ' ' " + 
        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
        "         WHERE DAC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN)||'-'" +
        "         ||(SELECT PVU.DENOMINAZIONE " +
        "            FROM PAPUA_V_UTENTE_LOGIN PVU " +
        "            WHERE DAC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +  
        "          AS DATI_ULTIMO_AGGIORNAMENTO, " +
        "         NULL AS DATA_ULTIMA_VALIDAZIONE, " +
        "         NULL AS DESCOM_PART, " +
        "         NULL AS SGLPROV_PART, " +
        "         NULL AS SEZIONE, " +
        "         NULL AS FOGLIO, " +
        "         NULL AS PARTICELLA, " +
        "         NULL AS SUBALTERNO, " +
        "         NULL AS COD_UTILIZZO, " +
        "         NULL AS DESC_UTIL, " +
        "         NULL AS DESC_VARIETA, " +
        "         NULL, " +
        "         NULL, " +
        "         NULL, " +
        "         NULL, " +
        "         NULL, " +
        "         NULL, " +
        "         NULL, " +
        "         NULL, " +
        "         NULL " +
        "  FROM   DB_AZIENDA_COLLEGATA DAC, " +
        "         DB_SOGGETTO_ASSOCIATO DAS, " +
        "         COMUNE CO, " +
        "         PROVINCIA PR " +
        "  WHERE  DAC.ID_AZIENDA = ? " + 
        "  AND    DAC.ID_SOGGETTO_ASSOCIATO = DAS.ID_SOGGETTO_ASSOCIATO " +
        "  AND    DAS.COMUNE =  CO.ISTAT_COMUNE " +
        "  AND    CO.ISTAT_PROVINCIA = PR.ISTAT_PROVINCIA " +
        "UNION " +
        "  SELECT AA.CUAA AS CUAA, " + 
        "         AA.PARTITA_IVA AS PARTITA_IVA, " + 
        "         AA.DENOMINAZIONE AS DENOMINAZIONE, " +   
        "         CO.DESCOM||' '||'('||PR.SIGLA_PROVINCIA||')' AS COMUNE, " +
        "         AA.SEDELEG_INDIRIZZO AS INDIRIZZO, " +
        "         AA.SEDELEG_CAP AS CAP, " +
        "         NEWDC.DATA_INGRESSO, " +
        "         NEWDC.DATA_INIZIO_VALIDITA, " + 
        "         TO_CHAR(NEWDC.DATA_AGGIORNAMENTO,'dd/mm/yyyy')||'-'" +
        "         ||(SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
        "          || ' ' " + 
        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
        "         WHERE NEWDC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN)||'-'" +
        "         ||(SELECT PVU.DENOMINAZIONE " +
        "            FROM PAPUA_V_UTENTE_LOGIN PVU " +
        "            WHERE NEWDC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +   
        "          AS DATI_ULTIMO_AGGIORNAMENTO, " +
        "         NULL AS DATA_ULTIMA_VALIDAZIONE, " +
        "         NULL AS DESCOM_PART, " +
        "         NULL AS SGLPROV_PART, " +
        "         NULL AS SEZIONE, " +
        "         NULL AS FOGLIO, " +
        "         NULL AS PARTICELLA, " +
        "         NULL AS SUBALTERNO, " +
        "         NULL AS COD_UTILIZZO, " +
        "         NULL AS DESC_UTIL, " +
        "         NULL AS DESC_VARIETA, " +
        "         NULL, " +
        "         NULL, " +
        "         NULL, " +
        "         NULL, " +
        "         NULL, " +
        "         NULL, " +
        "         NULL, " +
        "         NULL, " +
        "         NULL " +
        "  FROM   DB_ANAGRAFICA_AZIENDA AA, " +
        "         DICH_CONS NEWDC, " +
        "         COMUNE CO, " + 
        "         PROVINCIA PR " +
        "  WHERE  NEWDC.ID_AZIENDA = ? " + 
        "  AND    NEWDC.ID_AZIENDA_ASSOCIATA = AA.ID_AZIENDA " +
        "  AND    AA.DATA_FINE_VALIDITA IS NULL " +    
        "  AND    AA.SEDELEG_COMUNE = CO.ISTAT_COMUNE  " +
        "  AND    CO.ISTAT_PROVINCIA = PR.ISTAT_PROVINCIA " +
        "  AND NOT EXISTS (SELECT 1 " +
        "                  FROM   DB_DICHIARAZIONE_CONSISTENZA DC, " +   
        "                         DB_TIPO_MOTIVO_DICHIARAZIONE TI " +
        "                  WHERE  DC.ID_AZIENDA = NEWDC.ID_AZIENDA_ASSOCIATA " +
        "                  AND    DC.ID_MOTIVO_DICHIARAZIONE <> 7 " +
        "                  AND    DC.ID_MOTIVO_DICHIARAZIONE = TI.ID_MOTIVO_DICHIARAZIONE " +
        "                  AND    TI.TIPO_DICHIARAZIONE  != 'C' " +
        "                 ) " +
        "UNION " +
        " SELECT AA.CUAA AS CUAA, " +
        "        AA.PARTITA_IVA AS PARTITA_IVA, " +
        "        AA.DENOMINAZIONE AS DENOMINAZIONE, " +   
        "        CO.DESCOM||' '||'('||PR.SIGLA_PROVINCIA||')' AS COMUNE, " + 
        "        AA.SEDELEG_INDIRIZZO AS INDIRIZZO, " +  
        "        AA.SEDELEG_CAP AS CAP, " +
        "        NEWDC.DATA_INGRESSO, " +
        "        NEWDC.DATA_INIZIO_VALIDITA, " +      
        "        TO_CHAR(NEWDC.DATA_AGGIORNAMENTO,'dd/mm/yyyy')||'-'" +
        "         ||(SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
        "          || ' ' " + 
        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
        "         WHERE NEWDC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN)||'-'" +
        "         ||(SELECT PVU.DENOMINAZIONE " +
        "            FROM PAPUA_V_UTENTE_LOGIN PVU " +
        "            WHERE NEWDC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +  
        "          AS DATI_ULTIMO_AGGIORNAMENTO, " +
        "        DC.DATA AS DATA_ULTIMA_VALIDAZIONE, " +
        "        NULL AS DESCOM_PART, " +
        "        NULL AS SGLPROV_PART, " +
        "        NULL AS SEZIONE, " +
        "        NULL AS FOGLIO, " +
        "        NULL AS PARTICELLA, " +
        "        NULL AS SUBALTERNO, " +
        "        NULL AS COD_UTILIZZO, " +
        "        NULL AS DESC_UTIL, " +
        "        NULL AS DESC_VARIETA, " +
        "        NULL, " +
        "        NULL, " +
        "        NULL, " +
        "        NULL, " +
        "        NULL, " +
        "        NULL, " +
        "        NULL, " +
        "        NULL, " +
        "        NULL " +
        " FROM   DB_ANAGRAFICA_AZIENDA AA, " +       
        "        COMUNE CO, " +
        "        PROVINCIA PR, " +
        "        DICH_CONS NEWDC, " +
        "        DB_DICHIARAZIONE_CONSISTENZA DC " +
        "  WHERE NEWDC.ID_AZIENDA = ? " + 
        "  AND   NEWDC.ID_AZIENDA_ASSOCIATA = AA.ID_AZIENDA " +
        "  AND   AA.DATA_FINE_VALIDITA IS NULL " +      
        "  AND   AA.SEDELEG_COMUNE =  CO.ISTAT_COMUNE " +
        "  AND   CO.ISTAT_PROVINCIA = PR.ISTAT_PROVINCIA " +
        "  AND   NEWDC.ID_AZIENDA_ASSOCIATA = NEWDC.ID_AZIENDA_ASSOCIATA " +
        "  AND   NEWDC.MAX_DATA = DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "  AND   NEWDC.ID_AZIENDA_ASSOCIATA = DC.ID_AZIENDA " +
        "  AND NOT EXISTS (SELECT 1 " +
        "                  FROM  DB_CONDUZIONE_DICHIARATA DCD, " +
        "                        DB_UTILIZZO_DICHIARATO DUD, " +
        "                        DB_TIPO_UTILIZZO TU " +
        "                  WHERE DCD.CODICE_FOTOGRAFIA_TERRENI  = DC.CODICE_FOTOGRAFIA_TERRENI " +
        "                  AND   DCD.ID_CONDUZIONE_DICHIARATA = DUD.ID_CONDUZIONE_DICHIARATA " +
        "                  AND   DCD.ID_TITOLO_POSSESSO <> 5 " +
        "                  AND   DC.ID_MOTIVO_DICHIARAZIONE <> 7 " +
        "                  AND   DUD.ID_UTILIZZO = TU.ID_UTILIZZO " +
        "                  AND   TU.FLAG_FRUTTA_GUSCIO = 'S' " +
        "                 ) " +
        "  ORDER BY DENOMINAZIONE, " +
        "           CUAA,  " +
        "           DESCOM_PART, " +
        "           SGLPROV_PART, " +
        "           SEZIONE, " +
        "           FOGLIO, " +
        "           PARTICELLA, " +
        "           SUBALTERNO ");
        
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::getScaricoExcelElencoSociFruttaGuscio] Query="
                + query);
      }
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);
      int idx = 0;
      
      stmt.setLong(++idx, idAzienda);
      stmt.setLong(++idx, idAzienda);
      stmt.setLong(++idx, idAzienda);
      stmt.setLong(++idx, idAzienda);
      stmt.setLong(++idx, idAzienda);
      
      
      ResultSet rs = stmt.executeQuery();
      String cuaaTmp = "";
      while (rs.next())
      {
        //prima volta
        if(result == null)
        {
          result = new Vector<AziendaColVarExcelVO>();
          aziendaColVarExcelVO = new AziendaColVarExcelVO();
          vFruttaGuscio = new Vector<FruttaGuscioVO>();
          aziendaColVarExcelVO.setvFruttaGuscio(vFruttaGuscio);
        }
        String cuaa = rs.getString("CUAA"); 
        if(!cuaaTmp.equalsIgnoreCase(cuaa))
        {
          //dalla seconda volta
          if(Validator.isNotEmpty(cuaaTmp))
          {
            result.add(aziendaColVarExcelVO);
            aziendaColVarExcelVO = new AziendaColVarExcelVO();
            vFruttaGuscio = new Vector<FruttaGuscioVO>();
            aziendaColVarExcelVO.setvFruttaGuscio(vFruttaGuscio);
          }
          
          
          aziendaColVarExcelVO.setCuaa(cuaa);
          aziendaColVarExcelVO.setPartitaIva(rs.getString("PARTITA_IVA"));
          aziendaColVarExcelVO.setDenominazione(rs.getString("DENOMINAZIONE"));
          aziendaColVarExcelVO.setComune(rs.getString("COMUNE"));
          aziendaColVarExcelVO.setIndirizzo(rs.getString("INDIRIZZO"));
          aziendaColVarExcelVO.setCap(rs.getString("CAP"));
          aziendaColVarExcelVO.setDataIngresso(rs.getTimestamp("DATA_INGRESSO"));
         // aziendaColVarExcelVO.setDataUscita(rs.getTimestamp("DATA_USCITA"));
          aziendaColVarExcelVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
         // aziendaColVarExcelVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
          aziendaColVarExcelVO.setDataUltimaValidazione(rs.getTimestamp("DATA_ULTIMA_VALIDAZIONE"));
          aziendaColVarExcelVO.setDatiUltimoAggiornamento(rs.getString("DATI_ULTIMO_AGGIORNAMENTO"));      
          
          
          
          
        }
        
        String comune = rs.getString("DESCOM_PART");
        if(Validator.isNotEmpty(comune))
        {
          FruttaGuscioVO fruttaGuscioVO = new FruttaGuscioVO();
          fruttaGuscioVO.setDescComune(comune);
          fruttaGuscioVO.setSglProvincia(rs.getString("SGLPROV_PART"));
          fruttaGuscioVO.setSezione(rs.getString("SEZIONE"));
          fruttaGuscioVO.setFoglio(rs.getString("FOGLIO"));
          fruttaGuscioVO.setParticella(rs.getString("PARTICELLA"));
          fruttaGuscioVO.setSubalterno(rs.getString("SUBALTERNO"));
          fruttaGuscioVO.setCodUtilizzo(rs.getString("COD_UTILIZZO"));
          fruttaGuscioVO.setDescUtilizzo(rs.getString("DESC_UTIL"));
          fruttaGuscioVO.setDescVarieta(rs.getString("DESC_VARIETA"));
          fruttaGuscioVO.setAnnImpianto(checkIntegerNull(rs.getString("ANNO_IMPIANTO")));
          fruttaGuscioVO.setSestoTraFile(checkLongNull(rs.getString("SESTO_TRA_FILE")));
          fruttaGuscioVO.setSestoSuFile(checkLongNull(rs.getString("SESTO_SU_FILE")));
          fruttaGuscioVO.setnPiante(checkLongNull(rs.getString("NUMERO_PIANTE_CEPPI")));
          fruttaGuscioVO.setSupUtilizzo(rs.getBigDecimal("SUPERFICIE_UTILIZZATA"));
          fruttaGuscioVO.setSupGrafica(rs.getBigDecimal("SUPERFICIE_GRAFICA"));
          fruttaGuscioVO.setSupCondotta(rs.getBigDecimal("SUPERFICIE_CONDOTTA"));
          fruttaGuscioVO.setIdParticella(checkLongNull(rs.getString("ID_PARTICELLA")));
          fruttaGuscioVO.setIdConduzione(checkLongNull(rs.getString("ID_CONDUZIONE_PARTICELLA")));
        
          aziendaColVarExcelVO.getvFruttaGuscio().add(fruttaGuscioVO);
        }
        
        
        
        cuaaTmp = cuaa;
      }
      
      if(result != null)      
        result.add(aziendaColVarExcelVO);
      
      return result;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("aziendaColVarExcelVO", aziendaColVarExcelVO),
          new Variabile("result", result),
          new Variabile("vFruttaGuscio", vFruttaGuscio) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
  
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
    		  "[AnagrafeGaaDAO::getScaricoExcelElencoSociFruttaGuscio] ", t,
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
          "[AnagrafeGaaDAO::getScaricoExcelElencoSociFruttaGuscio] END.");
    }
  }
  
  /**
   * l'azienda in questione è già stato chiusa e 
   * assegnata ad un'altra..
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public boolean isInAziendaProvenienza(long idAzienda) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean result = false;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[AnagrafeGaaDAO::isInAziendaProvenienza] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   "
              + "SELECT  A.ID_AZIENDA "
              + "FROM    DB_AZIENDA A " 
              + "WHERE  A.ID_AZIENDA_PROVENIENZA = ? ");   
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::isInAziendaProvenienza] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      int indice = 0;
      stmt.setLong(++indice,idAzienda);
      
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        result = true;
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
              "[AnagrafeGaaDAO::isInAziendaProvenienza] ",
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
              "[AnagrafeGaaDAO::isInAziendaProvenienza] END.");
    }
  }
  
  
  public Vector<TipoInfoAggiuntivaVO> getTipoInfoAggiuntive(long idAzienda) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoInfoAggiuntivaVO> vTipoInfoAggiuntiva = null;
    TipoInfoAggiuntivaVO tipoInfoAggiuntivaVO = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[AnagrafeGaaDAO::getTipoInfoAggiuntive] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "SELECT TIA.ID_INFO_AGGIUNTIVA, " + 
        "       TIA.CODICE_INFO_AGGIUNTIVA, " + 
        "       TIA.DESCRIZIONE " +
        "FROM   DB_TIPO_INFO_AGGIUNTIVA TIA, " +
        "       DB_R_AZIENDA_INFO_AGGIUNTIVA AIA " +
        "WHERE  AIA.ID_AZIENDA = ? " +
        "AND    AIA.DATA_FINE_VALIDITA IS NULL " +
        "AND    AIA.ID_INFO_AGGIUNTIVA = TIA.ID_INFO_AGGIUNTIVA " +
        "ORDER BY TIA.DESCRIZIONE ASC ");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::getTipoInfoAggiuntive] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      int indice = 0;
      stmt.setLong(++indice, idAzienda);
      
      
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(vTipoInfoAggiuntiva == null)
        {
          vTipoInfoAggiuntiva = new Vector<TipoInfoAggiuntivaVO>();
        }
        tipoInfoAggiuntivaVO = new TipoInfoAggiuntivaVO();
        tipoInfoAggiuntivaVO.setIdInfoAggiuntiva(rs.getLong("ID_INFO_AGGIUNTIVA"));
        tipoInfoAggiuntivaVO.setCodiceInfoAggiuntiva(rs.getString("CODICE_INFO_AGGIUNTIVA"));
        tipoInfoAggiuntivaVO.setDescrizione(rs.getString("DESCRIZIONE"));
        
        vTipoInfoAggiuntiva.add(tipoInfoAggiuntivaVO);
      }
      
      return vTipoInfoAggiuntiva;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vTipoInfoAggiuntiva", vTipoInfoAggiuntiva),
          new Variabile("tipoInfoAggiuntivaVO", tipoInfoAggiuntivaVO)};
      
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AnagrafeGaaDAO::getTipoInfoAggiuntive] ",
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
              "[AnagrafeGaaDAO::getTipoInfoAggiuntive] END.");
    }
  }
  
  /**
   * Restituisce un vettore di GruppoGreeningVO
   * 
   * @param idDichConsistenza identificativo della dichiarazione di consistenza
   * @param dataDichConsistenza data della dichiarazione di consistenza (relativa all'id, se valorizzato)
   * @param idAzienda identificativo dell'azienda
   * 
   * @return
   * @throws DataAccessException
   */
  public Vector<GruppoGreeningVO> getListGruppiGreening(Long idDichConsistenza, 
  		java.util.Date dataDichConsistenza, Long idAzienda) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<GruppoGreeningVO> result = null;
    String filtroDate = null;
    
    try {
      SolmrLogger.debug(this, "[AnagrafeGaaDAO::getListGruppiGreening] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      if (idDichConsistenza!=null) {
      	filtroDate = "? BETWEEN [ALIAS].DATA_INIZIO_VALIDITA AND NVL([ALIAS].DATA_FINE_VALIDITA, ?)";
      }else {
      	filtroDate = "[ALIAS].DATA_FINE_VALIDITA IS NULL";
      }
      
      queryBuf = new StringBuffer();
      queryBuf
          .append("   "
          		+ "SELECT   " 
              + "       GRUPPO.ID_GRUPPO_GREENING, "
          		+ "				GRUPPO.DESCRIZIONE_GRUPPO_GREENING, "
              + "				TIPO_GREENING.ID_TIPO_GREENING, "
          		+ "				TIPO_GREENING.DESCRIZIONE_TIPO_GREENING, "
              + "				AZIENDA.ID_AZIENDA_GREENING, "
          		+ "				ESITO.DESCRIZIONE_ESITO_GREENING, "
          		+ "				AZIENDA.VALORE_CALCOLATO_VARCHAR2, "
          		+ "				AZIENDA.VALORE_CALCOLATO_NUMBER, "
          		+ "				AZIENDA.VALORE_CALCOLATO_DATE, " 
          		+ "				TIPO_ESONERO.DESCRIZIONE_TIPO_ESONERO "
              + "FROM   "
              + "       DB_GRUPPO_GREENING GRUPPO "
              + "       LEFT JOIN DB_TIPO_GREENING TIPO_GREENING "
              + "       	ON GRUPPO.ID_GRUPPO_GREENING = TIPO_GREENING.ID_GRUPPO_GREENING "
              + "       	AND "+filtroDate.replace("[ALIAS]", "TIPO_GREENING")+" "
              + "         AND TIPO_GREENING.FLAG_VISIBILE = 'S' "
              + "       LEFT JOIN DB_AZIENDA_GREENING AZIENDA "
              + "       	ON AZIENDA.ID_AZIENDA = ? "
              + "       	AND TIPO_GREENING.ID_TIPO_GREENING = AZIENDA.ID_TIPO_GREENING ");
      if (idDichConsistenza!=null) {
      	queryBuf.append(
      					"       	AND AZIENDA.ID_DICHIARAZIONE_CONSISTENZA = ? ");
      }else {
      	queryBuf.append("       	AND ").append(filtroDate.replace("[ALIAS]", "AZIENDA")).append(" ");
      }
      queryBuf.append(
      					"       LEFT JOIN DB_ESITO_GREENING ESITO "
      				+ "       	ON AZIENDA.ID_ESITO_GREENING = ESITO.ID_ESITO_GREENING "
      				+ "       LEFT JOIN DB_AZIENDA_GREENING_ESONERO ESONERO "
      				+ "       	ON AZIENDA.ID_AZIENDA_GREENING = ESONERO.ID_AZIENDA_GREENING "
      				+ "       LEFT JOIN DB_TIPO_ESONERO_GREENING TIPO_ESONERO "
      				+ "       	ON ESONERO.ID_TIPO_ESONERO_GREENING = TIPO_ESONERO.ID_TIPO_ESONERO_GREENING "
              + "WHERE "+filtroDate.replace("[ALIAS]", "GRUPPO")+" "
              + "ORDER BY "
              + "       GRUPPO.ORDINAMENTO, "
              + "       TIPO_GREENING.ORDINAMENTO, "
              + "       AZIENDA.ID_AZIENDA_GREENING ");		
         
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this)) {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
        SolmrLogger.debug(this, "[AnagrafeGaaDAO::getListGruppiGreening] Query="+query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      int indice = 0;
      java.sql.Date dataParametro = null;
      
      SolmrLogger.debug(this, "[AnagrafeGaaDAO::getListGruppiGreening] idDichConsistenza="+idDichConsistenza);
      SolmrLogger.debug(this, "[AnagrafeGaaDAO::getListGruppiGreening] idAzienda="+idAzienda);
      
      if (idDichConsistenza!=null) {
      	dataParametro = checkDate(dataDichConsistenza);
      	stmt.setDate(++indice, dataParametro);
      	stmt.setDate(++indice, dataParametro);
      }
      
      stmt.setLong(++indice, idAzienda);
      
      if (idDichConsistenza!=null) {
      	stmt.setLong(++indice, idDichConsistenza);
      	stmt.setDate(++indice, dataParametro);
      	stmt.setDate(++indice, dataParametro);
      }
      
      ResultSet rs = stmt.executeQuery();
     
			result = new Vector<GruppoGreeningVO>();
      GruppoGreeningVO gruppo = null;
      GreeningVO greening = null;
      Long idGruppoGreening = null;
      Long idTipoGreening = null;
      String descTipoEsonero = null;
      
      while (rs.next()) {
      	idGruppoGreening = checkLongNull(rs.getString("ID_GRUPPO_GREENING"));
      	
      	if (gruppo==null||!idGruppoGreening.equals(gruppo.getIdGruppoGreening())) {
      		gruppo = new GruppoGreeningVO();
      		gruppo.setIdGruppoGreening(idGruppoGreening);
      		gruppo.setDescGruppoGreening(rs.getString("DESCRIZIONE_GRUPPO_GREENING"));
      		gruppo.setListaGreening(new Vector<GreeningVO>());
      		result.add(gruppo);
      	}
      	
      	idTipoGreening = checkLongNull(rs.getString("ID_TIPO_GREENING"));
      	
      	if (idTipoGreening==null) {
      		continue;
      	}
      	
      	if (greening==null||!idTipoGreening.equals(greening.getIdTipoGreening())) {
      		greening = new GreeningVO();
      		greening.setIdTipoGreening(idTipoGreening);
      		greening.setDescTipoGreening(rs.getString("DESCRIZIONE_TIPO_GREENING"));
      		greening.setIdAziendaGreening(checkLongNull(rs.getString("ID_AZIENDA_GREENING")));
      		greening.setDescEsitoGreening(rs.getString("DESCRIZIONE_ESITO_GREENING"));
      		greening.setValoreCalcolato(rs.getString("VALORE_CALCOLATO_VARCHAR2"));
      		greening.setValoreCalcolatoNumber(rs.getBigDecimal("VALORE_CALCOLATO_NUMBER"));
      		greening.setValoreCalcolatoDate(rs.getTimestamp("VALORE_CALCOLATO_DATE"));
      		greening.setListaDescTipiEsonero(new Vector<String>());
      		gruppo.getListaGreening().add(greening);
      	}
      	
      	descTipoEsonero = rs.getString("DESCRIZIONE_TIPO_ESONERO");
      	
      	if (Validator.isNotEmpty(descTipoEsonero)) {
      		greening.getListaDescTipiEsonero().add(descTipoEsonero);
      	}
      }
      
      return result;
    }catch (Throwable t) {
      //Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("result", result) };
      
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDichConsistenza", idDichConsistenza),
      	new Parametro("dataDichConsistenza", dataDichConsistenza),
      	new Parametro("idAzienda", idAzienda) };
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(
              this,
              "[AnagrafeGaaDAO::getListGruppiGreening] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }finally {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger.debug(this, "[AnagrafeGaaDAO::getListGruppiGreening] END.");
    }
  }
  
  /**
   * Effettua il calcolo dell'esito greening per l'azienda passata in input, utilizzando per il 
   * salvataggio dei dati anche l'idUtente ed eventualmente l'idDichiarazioneConsistenza, se valorizzato
   * 
   * @param idAzienda
   * @param idUtente
   * @param idDichiarazioneConsistenza
   * 
   * @return PlSqlCodeDescription
   * @throws DataAccessException
   */
  public PlSqlCodeDescription calcolaGreeningPlSql(long idAzienda, long IdUtente, 
  		Long idDichiarazioneConsistenza) throws DataAccessException {
    String query = null;
    Connection conn = null;
    CallableStatement cs = null;
    PlSqlCodeDescription result = null;
    
    try {
      SolmrLogger.debug(this, "[AnagrafeGaaDAO::calcolaGreeningPlSql] BEGIN.");
      /***
       *  PROCEDURE Calcola_Esito_Greening (pIdAzienda        IN DB_ANAGRAFICA_AZIENDA.ID_AZIENDA%TYPE,
                                      pIdDichCons       IN DB_DICHIARAZIONE_CONSISTENZA.ID_DICHIARAZIONE_CONSISTENZA%TYPE,
                                      pIdUtenteIride    IN DB_AZIENDA_GREENING.ID_UTENTE_AGGIORNAMENTO%TYPE,
                                      pRisultato       OUT VARCHAR2,
                                      pMessaggio       OUT VARCHAR2
                         )
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      query = "{call PCK_CALCOLO_ESITO_GREENING.CALCOLA_ESITO_GREENING(?,?,?,?,?)}";
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this)) {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this, "[AnagrafeGaaDAO::calcolaGreeningPlSql] Query="+query);
      }
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.setLong(1, idAzienda);
      if (Validator.isNotEmpty(idDichiarazioneConsistenza)) {
        cs.setLong(2, idDichiarazioneConsistenza.longValue());
      }else {
        cs.setNull(2, Types.DECIMAL);
      }
      cs.setLong(3, IdUtente);
      cs.registerOutParameter(4,Types.INTEGER); //codice errore
      cs.registerOutParameter(5,Types.VARCHAR); //msg errore
      
      cs.executeUpdate();
      
      result = new PlSqlCodeDescription();  
      result.setCode(cs.getLong(4)); 
      result.setDescription(cs.getString(5));
      
      return result;
    }catch (Throwable t) {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("query", query),
          new Variabile("result", result) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
      	new Parametro("IdUtente", IdUtente),
        new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza), };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[AnagrafeGaaDAO::calcolaGreeningPlSql] ",
      		t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }finally {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      closePlSql(cs, conn);
  
      // Fine metodo
      SolmrLogger.debug(this, "[AnagrafeGaaDAO::calcolaGreeningPlSql] END.");
    }
  }
  
  
  public PlSqlCodeDescription calcolaEfaPlSql(long idAzienda, Long idDichiarazioneConsistenza,
      long IdUtente) throws DataAccessException {
    String query = null;
    Connection conn = null;
    CallableStatement cs = null;
    PlSqlCodeDescription result = null;
    
    try {
      SolmrLogger.debug(this, "[AnagrafeGaaDAO::calcolaEfaPlSql] BEGIN.");
      /***
       *  PROCEDURE Calcola_Efa (pIdAzienda     IN DB_AZIENDA_EFA.ID_AZIENDA%TYPE,
                           pIdDichCons    IN DB_AZIENDA_EFA.ID_DICHIARAZIONE_CONSISTENZA%TYPE,
                           pIdUtenteIride IN DB_AZIENDA_EFA.ID_UTENTE_AGGIORNAMENTO%TYPE,
                           pRisultato     OUT VARCHAR2,
                           pMessaggio     OUT VARCHAR2)
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      query = "{call PCK_CALCOLO_EFA.CALCOLA_EFA(?,?,?,?,?)}";
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this)) {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this, "[AnagrafeGaaDAO::calcolaEfaPlSql] Query="+query);
      }
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.setLong(1, idAzienda);
      if (Validator.isNotEmpty(idDichiarazioneConsistenza)) {
        cs.setLong(2, idDichiarazioneConsistenza.longValue());
      }else {
        cs.setNull(2, Types.DECIMAL);
      }
      cs.setLong(3, IdUtente);
      cs.registerOutParameter(4,Types.INTEGER); //codice errore
      cs.registerOutParameter(5,Types.VARCHAR); //msg errore
      
      cs.executeUpdate();
      
      result = new PlSqlCodeDescription();  
      result.setCode(cs.getLong(4)); 
      result.setDescription(cs.getString(5));
      
      return result;
    }catch (Throwable t) {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("query", query),
          new Variabile("result", result) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("IdUtente", IdUtente),
        new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza), };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[AnagrafeGaaDAO::calcolaEfaPlSql] ",
          t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }finally {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      closePlSql(cs, conn);
  
      // Fine metodo
      SolmrLogger.debug(this, "[AnagrafeGaaDAO::calcolaEfaPlSql] END.");
    }
  }
  
  
  public void updateDataAggiornamentoUma(long idAzienda) 
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
              "[AnagrafeGaaDAO::updateDataAggiornamentoUma] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "UPDATE DB_ANAGRAFICA_AZIENDA   " +
        "SET DATA_AGGIORNAMENTO_UMA = SYSDATE " +
        "WHERE  ID_ANAGRAFICA_AZIENDA = (SELECT AA.ID_ANAGRAFICA_AZIENDA  " +
        "                                FROM   DB_ANAGRAFICA_AZIENDA AA " +
        "                                WHERE  AA.ID_AZIENDA = ? " +
        "                                AND    AA.DATA_FINE_VALIDITA IS NULL)");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::updateDataAggiornamentoUma] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice,idAzienda);      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AnagrafeGaaDAO::updateDataAggiornamentoUma] ",
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
              "[AnagrafeGaaDAO::updateDataAggiornamentoUma] END.");
    }
  }
  
  
  public Vector<AziendaSezioniVO> getListActiveAziendaSezioniByIdAzienda(long idAzienda) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<AziendaSezioniVO> result = null;
    AziendaSezioniVO aziendaSezioniVO = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[AnagrafeGaaDAO::getListActiveAziendaSezioniByIdAzienda] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "SELECT AAS.ID_AZIENDA_SEZIONI, " +
        "       AAS.ID_AZIENDA, " +
        "       AAS.ID_TIPO_SEZIONI_AAEP, " +
        "       AAS.DATA_INIZIO_VALIDITA, " +
        "       TSA.CODICE_SEZIONE, " +
        "       TSA.DESCRIZIONE " +
        "FROM   DB_AZIENDA_SEZIONI AAS, " +
        "       DB_TIPO_SEZIONI_AAEP TSA " +
        "WHERE  AAS.ID_AZIENDA = ? " +
        "AND    AAS.DATA_FINE_VALIDITA IS NULL " +
        "AND    AAS.ID_TIPO_SEZIONI_AAEP = TSA.ID_TIPO_SEZIONI_AAEP " +
        "ORDER BY  TSA.DESCRIZIONE ");   
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::getListActiveAziendaSezioniByIdAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      int indice = 0;
      stmt.setLong(++indice,idAzienda);
      
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(result == null)
        {
          result = new Vector<AziendaSezioniVO>();
        }
        aziendaSezioniVO  = new AziendaSezioniVO(); 
        aziendaSezioniVO.setIdAziendaSezioni(rs.getLong("ID_AZIENDA_SEZIONI"));
        aziendaSezioniVO.setIdAzienda(rs.getLong("ID_AZIENDA"));
        aziendaSezioniVO.setIdTipoSezioniAaep(rs.getLong("ID_TIPO_SEZIONI_AAEP"));
        aziendaSezioniVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        aziendaSezioniVO.setCodiceSezione(rs.getString("CODICE_SEZIONE"));
        aziendaSezioniVO.setDescrizione(rs.getString("DESCRIZIONE"));
        
        result.add(aziendaSezioniVO);
      }
      
      return result;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("result", result),
          new Variabile("aziendaSezioniVO", aziendaSezioniVO) };
      
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AnagrafeGaaDAO::getListActiveAziendaSezioniByIdAzienda] ",
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
              "[AnagrafeGaaDAO::getListActiveAziendaSezioniByIdAzienda] END.");
    }
  }
  
  public void storicizzaAziendaSezioniForAzienda(long idAzienda) 
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
              "[AnagrafeGaaDAO::storicizzaAziendaSezioniForAzienda] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "UPDATE DB_AZIENDA_SEZIONI " +
        "     SET DATA_FINE_VALIDITA = SYSDATE  " +
        "WHERE  " +
        "     ID_AZIENDA = ?  " +
        "     AND DATA_FINE_VALIDITA IS NULL ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::storicizzaAziendaSezioniForAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAzienda);
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AnagrafeGaaDAO::storicizzaAziendaSezioniForAzienda] ",
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
              "[AnagrafeGaaDAO::storicizzaAziendaSezioniForAzienda] END.");
    }
  }
  
  public Long insertAziendaSezione(AziendaSezioniVO aziendaSezioniVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idAziendaSezioni = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[AnagrafeGaaDAO::insertAziendaSezione] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idAziendaSezioni = getNextPrimaryKey(SolmrConstants.SEQ_DB_AZIENDA_SEZIONI);
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_AZIENDA_SEZIONI   " 
              + "     (ID_AZIENDA_SEZIONI, "
              + "     ID_AZIENDA, " 
              + "     ID_TIPO_SEZIONI_AAEP, "
              + "     DATA_INIZIO_VALIDITA,"
              + "     DATA_FINE_VALIDITA) "
              + "   VALUES(?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AnagrafeGaaDAO::insertAziendaSezione] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAziendaSezioni.longValue());
      stmt.setLong(++indice, aziendaSezioniVO.getIdAzienda());
      stmt.setLong(++indice, aziendaSezioniVO.getIdTipoSezioniAaep());
      stmt.setTimestamp(++indice, convertDateToTimestamp(aziendaSezioniVO.getDataInizioValidita()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(aziendaSezioniVO.getDataFineValidita()));
      
  
      stmt.executeUpdate();
      
      return idAziendaSezioni;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idAziendaSezioni", idAziendaSezioni)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("aziendaSezioniVO", aziendaSezioniVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AnagrafeGaaDAO::insertAziendaSezione] ",
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
              "[AnagrafeGaaDAO::insertAziendaSezione] END.");
    }
  }

}
