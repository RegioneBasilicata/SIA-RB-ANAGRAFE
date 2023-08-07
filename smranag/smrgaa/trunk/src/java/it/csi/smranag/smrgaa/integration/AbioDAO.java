package it.csi.smranag.smrgaa.integration;


import it.csi.smranag.smrgaa.dto.abio.OperatoreBiologicoVO;
import it.csi.smranag.smrgaa.dto.abio.PosizioneOperatoreVO;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Vector;


public class AbioDAO extends it.csi.solmr.integration.BaseDAO {
  
  
  public AbioDAO() throws ResourceAccessException{
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public AbioDAO(String refName) throws ResourceAccessException {
   super(refName);
  }

  /**
   * Restituisce il OperatoreBiologicoVO
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public OperatoreBiologicoVO getOperatoreBiologicoByExtIdAzienda(
      Long idAzienda, Date dataInizioAttivita) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    OperatoreBiologicoVO operatoreBiologicoVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[AbioDAO::getOperatoreBiologicoByExtIdAzienda] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("SELECT ID_OPERATORE_BIOLOGICO, DATA_INIZIO_ATTIVITA, " +
      				  "DATA_FINE_ATTIVITA "+ 
      				  "FROM ABIO_T_OPERATORE_BIOLOGICO "+
      				  "WHERE EXT_ID_AZIENDA = ? ");
      
      if(dataInizioAttivita!=null)
    	  queryBuf.append(" AND DATA_INIZIO_ATTIVITA <= TO_DATE('"+DateUtils.formatDate(dataInizioAttivita)+"','dd/MM/yyyy')");
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[AbioDAO::getOperatoreBiologicoByExtIdAzienda] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idAzienda.longValue());
      ResultSet rs = stmt.executeQuery();
          
      if (rs.next())
      {
    	  operatoreBiologicoVO = new OperatoreBiologicoVO();
    	  operatoreBiologicoVO.setIdOperatoreBiologico(rs.getLong("ID_OPERATORE_BIOLOGICO"));
    	  operatoreBiologicoVO.setDataInizioAttivita(rs.getDate("DATA_INIZIO_ATTIVITA"));
    	  operatoreBiologicoVO.setDataFineAttivita(rs.getDate("DATA_FINE_ATTIVITA"));
      }
      return operatoreBiologicoVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[AbioDAO::getOperatoreBiologicoByExtIdAzienda] ", t, query, variabili, parametri);
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
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[AbioDAO::getOperatoreBiologicoByExtIdAzienda] END.");
    }
  }
  
  /**
   * 
   * Vrifica se è un'azienda biologica attiva
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public OperatoreBiologicoVO getOperatoreBiologicoAttivo(
      Long idAzienda) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    OperatoreBiologicoVO operatoreBiologicoVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[AbioDAO::getOperatoreBiologicoAttivo] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("SELECT ID_OPERATORE_BIOLOGICO, DATA_INIZIO_ATTIVITA, " +
                "DATA_FINE_ATTIVITA "+ 
                "FROM ABIO_T_OPERATORE_BIOLOGICO "+
                "WHERE EXT_ID_AZIENDA = ? " +
                "AND DATA_FINE_ATTIVITA IS NULL ");
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[AbioDAO::getOperatoreBiologicoAttivo] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idAzienda.longValue());
      ResultSet rs = stmt.executeQuery();
          
      if (rs.next())
      {
        operatoreBiologicoVO = new OperatoreBiologicoVO();
        operatoreBiologicoVO.setIdOperatoreBiologico(rs.getLong("ID_OPERATORE_BIOLOGICO"));
        operatoreBiologicoVO.setDataInizioAttivita(rs.getDate("DATA_INIZIO_ATTIVITA"));
        operatoreBiologicoVO.setDataFineAttivita(rs.getDate("DATA_FINE_ATTIVITA"));
      }
      return operatoreBiologicoVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[AbioDAO::getOperatoreBiologicoAttivo] ", t, query, variabili, parametri);
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
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[AbioDAO::getOperatoreBiologicoAttivo] END.");
    }
  }
  
  /**
   * Restituisce PosizioneOperatoreVO[]
   * 
   * @param idOperatoreBiologico
   * @param dataFineValidita
   * @param checkStorico
   * @return
   * @throws DataAccessException
   */
  public PosizioneOperatoreVO[] getAttivitaBiologicheByIdAzienda(
      Long idOperatoreBiologico, Date dataFineValidita, boolean checkStorico) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector <PosizioneOperatoreVO> result = new Vector<PosizioneOperatoreVO>();
    
    
    try
    {
      SolmrLogger.debug(this, "[AbioDAO::getAttivitaBiologicheByIdAzienda] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(" SELECT CAT.DESCRIZIONE AS DESCRIZIONE_CAT, " +
      				  " STATO.DESCRIZIONE AS DESCRIZIONE_STATO, " +
      				  " POSIZIONE.DATA_INIZIO_VALIDITA AS DATA_INIZIO_VALIDITA, " +
      				  " POSIZIONE.DATA_FINE_VALIDITA AS DATA_FINE_VALIDITA " +
      				  " FROM ABIO_T_POSIZIONE_OPERATORE POSIZIONE, " +
      				  " ABIO_D_CATEGORIA_ATTIVITA CAT, "+
      				  " ABIO_D_STATO_ATTIVITA STATO "+
      				  " WHERE POSIZIONE.ID_OPERATORE_BIOLOGICO = ? " +
      				  " AND CAT.ID_CATEGORIA_ATTIVITA = POSIZIONE.ID_CATEGORIA_ATTIVITA " +
      				  " AND STATO.ID_STATO_ATTIVITA = POSIZIONE.ID_STATO_ATTIVITA ");

      if(!checkStorico && dataFineValidita==null)
    	  queryBuf.append(" AND POSIZIONE.DATA_FINE_VALIDITA IS NULL");
      else if(dataFineValidita!=null)//dataFineValidita valorizzata
    	  queryBuf.append(" AND POSIZIONE.DATA_INIZIO_VALIDITA <= TO_DATE('"+DateUtils.formatDate(dataFineValidita)+"','dd/MM/yyyy')");

      queryBuf.append(" ORDER BY POSIZIONE.DATA_FINE_VALIDITA DESC, CAT.DESCRIZIONE");

      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[AbioDAO::getAttivitaBiologicheByIdAzienda] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idOperatoreBiologico.longValue());
      ResultSet rs = stmt.executeQuery();
          
      while (rs.next())
      {
    	  PosizioneOperatoreVO posizioneOperatoreVO = new PosizioneOperatoreVO();
    	  posizioneOperatoreVO.setDescCategoria(rs.getString("DESCRIZIONE_CAT"));
    	  posizioneOperatoreVO.setDescStatoAttivita(rs.getString("DESCRIZIONE_STATO"));
    	  posizioneOperatoreVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
    	  posizioneOperatoreVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
    	  
    	  result.add(posizioneOperatoreVO);
      }
      return result.size() == 0 ? null : (PosizioneOperatoreVO[]) result
    	        .toArray(new PosizioneOperatoreVO[0]);
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idOperatoreBiologico", idOperatoreBiologico),
    	new Parametro("dataFineValidita", dataFineValidita)};

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[AbioDAO::getAttivitaBiologicheByIdAzienda] ", t, query, variabili, parametri);
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
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[AbioDAO::getAttivitaBiologicheByIdAzienda] END.");
    }
  }

  /**
   * Restituisce il CodeDescription[]
   * 
   * @param idOperatoreBiologico
   * @param dataInizioValidita
   * @return
   * @throws DataAccessException
   */
  public CodeDescription[] getODCbyIdOperatoreBiologico(
      Long idOperatoreBiologico, Date dataInizioValidita, boolean pianoCorrente) throws DataAccessException
  {
    
    //parametro piano corrente non più usato
    //ma al momento lasciato perchè modifica lampo //08/06/2012
    
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector <CodeDescription> result = new Vector<CodeDescription>();
    
    try
    {
      SolmrLogger.debug(this, "[AbioDAO::getODCbyIdOperatoreBiologico] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(" SELECT CONTROLLO.CODICE_UE AS CODICE_UE, " +
      				  " 	   CONTROLLO.DESCRIZIONE AS DESCRIZIONE " +
      				  " FROM ABIO_R_ODC_OPERATORE OPERATORE, " +
      				  " 	 ABIO_D_ORGANISMO_CONTROLLO CONTROLLO " +
      				  " WHERE ID_OPERATORE_BIOLOGICO = ? " +
      				  " 	  AND CONTROLLO.ID_ORGANISMO_CONTROLLO = OPERATORE.ID_ORGANISMO_CONTROLLO ");

      if(dataInizioValidita!=null)
      {
    	  queryBuf.append(" AND TRUNC(OPERATORE.DATA_INIZIO_VALIDITA) <= TO_DATE('"+DateUtils.formatDate(dataInizioValidita)+"','dd/MM/yyyy')");
    	  queryBuf.append(" AND TRUNC(NVL(OPERATORE.DATA_FINE_VALIDITA,TO_DATE('31/12/9999','dd/MM/yyyy'))) > TO_DATE('"+DateUtils.formatDate(dataInizioValidita)+"','dd/MM/yyyy')");
      }
      
      //if(pianoCorrente)
    	  //queryBuf.append(" AND OPERATORE.DATA_FINE_VALIDITA IS NULL ");
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[AbioDAO::getODCbyIdOperatoreBiologico] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idOperatoreBiologico.longValue());
      ResultSet rs = stmt.executeQuery();
          
      while (rs.next())
      {
    	  CodeDescription codeDesc = new CodeDescription();
    	  codeDesc.setCodeFlag(rs.getString("CODICE_UE"));
    	  codeDesc.setDescription(rs.getString("DESCRIZIONE"));
    	  
    	  result.add(codeDesc);
      }
      return result.size() == 0 ? null : (CodeDescription[]) result
    	        .toArray(new CodeDescription[0]);
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idOperatoreBiologico", idOperatoreBiologico),
    	new Parametro("dataInizioValidita", dataInizioValidita)};

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[AbioDAO::getODCbyIdOperatoreBiologico] ", t, query, variabili, parametri);
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
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[AbioDAO::getODCbyIdOperatoreBiologico] END.");
    }
  }
}