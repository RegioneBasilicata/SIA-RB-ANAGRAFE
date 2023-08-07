package it.csi.solmr.integration.anag;
	
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.anag.terreni.TipoTipologiaVinoVO;
import it.csi.solmr.dto.anag.terreni.VignaVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

public class TipoTipologiaVinoDAO extends it.csi.solmr.integration.BaseDAO {


	public TipoTipologiaVinoDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public TipoTipologiaVinoDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo che mi restituisce l'elenco delle tipologie vino
	 * 
	 * @param onlyActive
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.TipoTipologiaVinoVO[]
	 * @throws DataAccessException
	 */
	public TipoTipologiaVinoVO[] getListTipoTipologiaVino(boolean onlyActive, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListTipoTipologiaVino method in TipoTipologiaVinoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoTipologiaVinoVO> elencoTipoTipologiaVino = new Vector<TipoTipologiaVinoVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListTipoTipologiaVino method in TipoTipologiaVinoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListTipoTipologiaVino method in TipoTipologiaVinoDAO and it values: "+conn+"\n");

			String query = " SELECT ID_TIPOLOGIA_VINO, " +
						   "        ID_VINO, " +
						   "        CODICE, " +
						   "        DESCRIZIONE, " +
						   "        DATA_INIZIO_VALIDITA, " +
						   "        DATA_FINE_VALIDITA " + 
						   " FROM   DB_TIPO_TIPOLOGIA_VINO ";
			if(onlyActive) {
				query +=   " WHERE  DATA_FINE_VALIDITA IS NULL ";
			}
			String ordinamento = "";
			if(orderBy != null && orderBy.length > 0) {
				String criterio = "";
				for(int i = 0; i < orderBy.length; i++) {
					if(i == 0) {
						criterio = (String)orderBy[i];
					}
					else {
						criterio += ", "+(String)orderBy[i];
					}
				}
				ordinamento = "ORDER BY "+criterio;
			}
			if(!ordinamento.equals("")) {
				query += ordinamento;
			}

			SolmrLogger.debug(this, "Value of parameter 1 [ONLY_ACTIVE] in getListTipoTipologiaVino method in TipoTipologiaVinoDAO: "+onlyActive+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ORDER_BY] in getListTipoTipologiaVino method in TipoTipologiaVinoDAO: "+orderBy+"\n");

			stmt = conn.prepareStatement(query);
			

			SolmrLogger.debug(this, "Executing getListTipoTipologiaVino: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				TipoTipologiaVinoVO tipoTipologiaVinoVO = new TipoTipologiaVinoVO();
				tipoTipologiaVinoVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
				tipoTipologiaVinoVO.setIdVino(new Long(rs.getLong("ID_VINO")));
				tipoTipologiaVinoVO.setCodice(rs.getString("CODICE"));
				tipoTipologiaVinoVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoTipologiaVinoVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				tipoTipologiaVinoVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				elencoTipoTipologiaVino.add(tipoTipologiaVinoVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListTipoTipologiaVino in TipoTipologiaVinoDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListTipoTipologiaVino in TipoTipologiaVinoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListTipoTipologiaVino in TipoTipologiaVinoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListTipoVino in TipoTipologiaVinoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipoTipologiaVino method in TipoTipologiaVinoDAO\n");
		if(elencoTipoTipologiaVino.size() == 0) {
			return (TipoTipologiaVinoVO[])elencoTipoTipologiaVino.toArray(new TipoTipologiaVinoVO[0]);
		}
		else {
			return (TipoTipologiaVinoVO[])elencoTipoTipologiaVino.toArray(new TipoTipologiaVinoVO[elencoTipoTipologiaVino.size()]);
		}
	}
	
	/**
	 * Metodo per recuperare l'elenco delle tipologie vino a partire dalla categoria
	 * 
	 * @param idVino
	 * @param onlyActive
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.TipoTipologiaVinoVO[]
	 * @throws DataAccessException
	 */
	public TipoTipologiaVinoVO[] getListTipoTipologiaVinoByIdVino(Long idVino, boolean onlyActive, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListTipoTipologiaVinoByIdVino method in TipoTipologiaVinoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoTipologiaVinoVO> elencoTipoTipologiaVino = new Vector<TipoTipologiaVinoVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListTipoTipologiaVinoByIdVino method in TipoTipologiaVinoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListTipoTipologiaVinoByIdVino method in TipoTipologiaVinoDAO and it values: "+conn+"\n");

			String query = " SELECT ID_TIPOLOGIA_VINO, " +
						   "        ID_VINO, " +
						   "        CODICE, " +
						   "        DESCRIZIONE, " +
						   "        DATA_INIZIO_VALIDITA, " +
						   "        DATA_FINE_VALIDITA " + 
						   " FROM   DB_TIPO_TIPOLOGIA_VINO " +
						   " WHERE  ID_VINO = ? ";
			if(onlyActive) {
				query +=   " AND    DATA_FINE_VALIDITA IS NULL ";
			}
			String ordinamento = "";
			if(orderBy != null && orderBy.length > 0) {
				String criterio = "";
				for(int i = 0; i < orderBy.length; i++) {
					if(i == 0) {
						criterio = (String)orderBy[i];
					}
					else {
						criterio += ", "+(String)orderBy[i];
					}
				}
				ordinamento = "ORDER BY "+criterio;
			}
			if(!ordinamento.equals("")) {
				query += ordinamento;
			}

			SolmrLogger.debug(this, "Value of parameter 1 [ID_VINO] in getListTipoTipologiaVinoByIdVino method in TipoTipologiaVinoDAO: "+idVino+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ONLY_ACTIVE] in getListTipoTipologiaVinoByIdVino method in TipoTipologiaVinoDAO: "+onlyActive+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ORDER_BY] in getListTipoTipologiaVinoByIdVino method in TipoTipologiaVinoDAO: "+orderBy+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idVino.longValue());
			
			SolmrLogger.debug(this, "Executing getListTipoTipologiaVinoByIdVino: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				TipoTipologiaVinoVO tipoTipologiaVinoVO = new TipoTipologiaVinoVO();
				tipoTipologiaVinoVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
				tipoTipologiaVinoVO.setIdVino(new Long(rs.getLong("ID_VINO")));
				tipoTipologiaVinoVO.setCodice(rs.getString("CODICE"));
				tipoTipologiaVinoVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoTipologiaVinoVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				tipoTipologiaVinoVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				elencoTipoTipologiaVino.add(tipoTipologiaVinoVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListTipoTipologiaVinoByIdVino in TipoTipologiaVinoDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListTipoTipologiaVinoByIdVino in TipoTipologiaVinoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListTipoTipologiaVinoByIdVino in TipoTipologiaVinoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListTipoTipologiaVinoByIdVino in TipoTipologiaVinoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipoTipologiaVinoByIdVino method in TipoTipologiaVinoDAO\n");
		if(elencoTipoTipologiaVino.size() == 0) {
			return (TipoTipologiaVinoVO[])elencoTipoTipologiaVino.toArray(new TipoTipologiaVinoVO[0]);
		}
		else {
			return (TipoTipologiaVinoVO[])elencoTipoTipologiaVino.toArray(new TipoTipologiaVinoVO[elencoTipoTipologiaVino.size()]);
		}
	}
	
	
	/**
   * Restituiesce un vettore di TipoTipologiaVinoVO
   * 
   * @param istatComune
   * @return
   * @throws DataAccessException
   */
  public Vector<TipoTipologiaVinoVO> getListActiveTipoTipologiaVinoByComune(String istatComune) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoTipologiaVinoVO> result = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[TipoTipologiaVinoDAO::getListActiveTipoTipologiaVinoByComune] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   "
              + "SELECT   " 
              + "       TTV.ID_TIPOLOGIA_VINO, " 
              + "       TTV.DESCRIZIONE, "
              + "       TTV.CODICE, "
              + "       TTV.VINO_DOC, "
              + "       TTV.CODICE_MIPAF, "
              + "       TTV.FLAG_GESTIONE_VIGNA, "
              + "       TTV.FLAG_GESTIONE_ETICHETTA "
              + "FROM   "
              + "       DB_TIPO_TIPOLOGIA_VINO TTV,"
              + "       DB_TIPO_VINIDOC_COMUNI TVC "
              + "WHERE  "
              + "       TVC.ISTAT_COMUNE = ? "
              + "AND    TVC.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO "
              +	"AND    TTV.DATA_FINE_VALIDITA IS NULL "
              + "AND    TTV.FLAG_SCHEDARIO = 'S' "
              + "ORDER BY "
              + "       TTV.DESCRIZIONE ");   
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[TipoTipologiaVinoDAO::getListActiveTipoTipologiaVinoByComune] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      int indice = 0;
      stmt.setString(++indice,istatComune);
      
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(result == null)
        {
          result = new Vector<TipoTipologiaVinoVO>();
        }
        TipoTipologiaVinoVO tipoTipologiaVinoVO  = new TipoTipologiaVinoVO(); 
        tipoTipologiaVinoVO.setIdTipologiaVino(checkLong(rs.getString("ID_TIPOLOGIA_VINO")));
        tipoTipologiaVinoVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoTipologiaVinoVO.setCodice(rs.getString("CODICE"));
        tipoTipologiaVinoVO.setVinoDoc(rs.getString("VINO_DOC"));
        tipoTipologiaVinoVO.setCodiceMipaf(rs.getString("CODICE_MIPAF"));
        tipoTipologiaVinoVO.setFlagGestioneVigna(rs.getString("FLAG_GESTIONE_VIGNA"));
        tipoTipologiaVinoVO.setFlagGestioneEtichetta(rs.getString("FLAG_GESTIONE_ETICHETTA"));
        result.add(tipoTipologiaVinoVO);
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
      { new Parametro("istatComune", istatComune) };
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[TipoTipologiaVinoDAO::getListActiveTipoTipologiaVinoByComune] ",
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
              "[TipoTipologiaVinoDAO::getListActiveTipoTipologiaVinoByComune] END.");
    }
  }
  
  /**
   * 
   * Restituisce i vini legati ai doc di ricaduta e di resa
   * 
   * 
   * @param istatComune
   * @param dataInserimentoDichiarazione
   * @return
   * @throws DataAccessException
   */
  public Vector<TipoTipologiaVinoVO> getListTipoTipologiaVinoRicaduta(
      String istatComune, long idVarieta, long idTipologiaVino, Date dataInserimentoDichiarazione) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoTipologiaVinoVO> result = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[TipoTipologiaVinoDAO::getListTipoTipologiaVinoRicaduta] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append(""
              + "SELECT   " 
              + "       TTV.ID_TIPOLOGIA_VINO, " 
              + "       TTV.DESCRIZIONE, "
              + "       TTV.RESA "
              + "FROM   "
              + "       DB_TIPO_TIPOLOGIA_VINO TTV, "
              + "       DB_TIPO_VINIDOC_COMUNI TVC, "
              + "       DB_TIPO_VINO_VARIETA TVV, " 
              + "       DB_TIPO_TIPOLOGIA_VINO_RICAD TTVR "
              + "WHERE  "
              + "       TVC.ISTAT_COMUNE = ? " 
              + "AND    TVC.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO "
              + "AND    TVV.ID_VARIETA = ? " 
              + "AND    TTVR.ID_TIPOLOGIA_VINO = ? " 
              + "AND    TTV.ID_TIPOLOGIA_VINO = TVV.ID_TIPOLOGIA_VINO "   
              + "AND    TTVR.ID_TIPOLOGIA_VINO_RICADUTA = TTV.ID_TIPOLOGIA_VINO ");
      
      if(Validator.isNotEmpty(dataInserimentoDichiarazione))
      {
        queryBuf
        .append(""
              + "AND    TTVR.DATA_INIZIO_VALIDITA < ? " 
              + "AND    NVL(TTVR.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'dd/mm/yyyy')) > ? ");
        
        queryBuf
        .append(""
              + "AND    TTV.DATA_INIZIO_VALIDITA < ? " 
              + "AND    NVL(TTV.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'dd/mm/yyyy')) > ? ");
      }
      else
      {
        queryBuf
        .append(""
              + "AND    TTVR.DATA_FINE_VALIDITA IS NULL ");
        
        queryBuf
        .append(""
              + "AND    TTV.DATA_FINE_VALIDITA IS NULL ");
      }
      
      queryBuf
      .append(""
            + "ORDER BY "
            + "       TTV.DESCRIZIONE ");   
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[TipoTipologiaVinoDAO::getListTipoTipologiaVinoRicaduta] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      int indice = 0;
      stmt.setString(++indice, istatComune);
      stmt.setLong(++indice, idVarieta);
      stmt.setLong(++indice, idTipologiaVino);
      
      if(Validator.isNotEmpty(dataInserimentoDichiarazione))
      {
        stmt.setTimestamp(++indice,convertDateToTimestamp(dataInserimentoDichiarazione));
        stmt.setTimestamp(++indice,convertDateToTimestamp(dataInserimentoDichiarazione));
        stmt.setTimestamp(++indice,convertDateToTimestamp(dataInserimentoDichiarazione));
        stmt.setTimestamp(++indice,convertDateToTimestamp(dataInserimentoDichiarazione));
      }
      
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(result == null)
        {
          result = new Vector<TipoTipologiaVinoVO>();
        }
        TipoTipologiaVinoVO tipoTipologiaVinoVO  = new TipoTipologiaVinoVO(); 
        tipoTipologiaVinoVO.setIdTipologiaVino(checkLong(rs.getString("ID_TIPOLOGIA_VINO")));
        tipoTipologiaVinoVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoTipologiaVinoVO.setResa(rs.getBigDecimal("RESA"));
        result.add(tipoTipologiaVinoVO);
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
      { new Parametro("idTipologiaVino", idTipologiaVino), 
          new Parametro("dataInserimentoDichiarazione", dataInserimentoDichiarazione)};
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[TipoTipologiaVinoDAO::getListTipoTipologiaVinoRicaduta] ",
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
              "[TipoTipologiaVinoDAO::getListTipoTipologiaVinoRicaduta] END.");
    }
  }
	
  /**
   * Metodo per recuperare la descrizione della tipologie vino a fronte dell' idTipologiaVino
   */
  public String getDescrizioneByIdTipologiaVino(Long idTipologiaVino) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getDescrizioneByIdTipologiaVino method in TipoTipologiaVinoDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    String descrizione=null;

    try {
      SolmrLogger.debug(this, "Creating db-connection in getDescrizioneByIdTipologiaVino method in TipoTipologiaVinoDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getDescrizioneByIdTipologiaVino method in TipoTipologiaVinoDAO and it values: "+conn+"\n");

      String query = " SELECT DESCRIZIONE " +
               " FROM   DB_TIPO_TIPOLOGIA_VINO " +
               " WHERE  ID_TIPOLOGIA_VINO = ? ";

      SolmrLogger.debug(this, "Value of parameter 1 [ID_TIPOLOGIA_VINO] in getDescrizioneByIdTipologiaVino method in TipoTipologiaVinoDAO: "+idTipologiaVino+"\n");

      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idTipologiaVino.longValue());
      
      SolmrLogger.debug(this, "Executing getDescrizioneByIdTipologiaVino: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      if(rs.next())
        descrizione=rs.getString("DESCRIZIONE");
      
      rs.close();
      stmt.close();
      return descrizione;

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getDescrizioneByIdTipologiaVino in TipoTipologiaVinoDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getDescrizioneByIdTipologiaVino in TipoTipologiaVinoDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) 
      {
        SolmrLogger.error(this, "getDescrizioneByIdTipologiaVino in TipoTipologiaVinoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getDescrizioneByIdTipologiaVino in TipoTipologiaVinoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
      SolmrLogger.debug(this, "Invocated getDescrizioneByIdTipologiaVino method in TipoTipologiaVinoDAO\n");
    }
  }
  
  
  /**
   * Metodo per recuperare la descrizione della tipologie vino a fronte dell' idVitigno
   */
  public String getVarietaByIdVitigno(Long idVitigno) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getVarietaByIdVitigno method in TipoTipologiaVinoDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    String descrizione=null;

    try {
      SolmrLogger.debug(this, "Creating db-connection in getVarietaByIdVitigno method in TipoTipologiaVinoDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getVarietaByIdVitigno method in TipoTipologiaVinoDAO and it values: "+conn+"\n");

      String query = " SELECT DISTINCT(DESCRIZIONE) FROM DB_TIPO_VARIETA "+
      "WHERE TO_NUMBER(CODICE_VARIETA)=? "+
      "AND ANNO_FINE_VALIDITA IS NULL "+
      "AND ID_UTILIZZO IN (SELECT ID_UTILIZZO "+
      "FROM DB_TIPO_UTILIZZO WHERE TIPO = 'V') ";

      SolmrLogger.debug(this, "Value of parameter 1 [ID_TIPOLOGIA_VINO] in getVarietaByIdVitigno method in TipoTipologiaVinoDAO: "+idVitigno+"\n");

      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idVitigno.longValue());
      
      SolmrLogger.debug(this, "Executing getVarietaByIdVitigno: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      if(rs.next())
        descrizione=rs.getString("DESCRIZIONE");
      
      rs.close();
      stmt.close();
      return descrizione;

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getVarietaByIdVitigno in TipoTipologiaVinoDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getVarietaByIdVitigno in TipoTipologiaVinoDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) 
      {
        SolmrLogger.error(this, "getVarietaByIdVitigno in TipoTipologiaVinoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getVarietaByIdVitigno in TipoTipologiaVinoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
      SolmrLogger.debug(this, "Invocated getVarietaByIdVitigno method in TipoTipologiaVinoDAO\n");
    }
  }
  
  /**
   * 
   * Restituisce tutte le occorrenze della tiplogia vino 
   * sia presenti nelle dichiarazioni di consistenza sia in quella al piano corrente
   * relative alla singola azienda 
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public TipoTipologiaVinoVO[] getListTipoTipologiaVinoForAzienda(long idAzienda) throws DataAccessException {
    SolmrLogger.debug(this, "Invocating getListTipoTipologiaVinoForAzienda method in TipoTipologiaVinoDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoTipologiaVinoVO> elencoTipoTipologiaVino = new Vector<TipoTipologiaVinoVO>();

    try {
      SolmrLogger.debug(this, "Creating db-connection in getListTipoTipologiaVinoForAzienda method in TipoTipologiaVinoDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListTipoTipologiaVinoForAzienda method in TipoTipologiaVinoDAO and it values: "+conn+"\n");

      String query = 
          "SELECT " +
      		"       DISTINCT TTV.ID_TIPOLOGIA_VINO, TTV.DESCRIZIONE " +
      		"FROM " + 
      		"       DB_STORICO_UNITA_ARBOREA SUA, " +  
      		"       DB_TIPO_TIPOLOGIA_VINO TTV " +
      		"WHERE " +
      		"       SUA.ID_AZIENDA = ? " +
        	"AND    SUA.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO " +
      		"ORDER BY  TTV.DESCRIZIONE ";

      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListTipoTipologiaVinoForAzienda method in TipoTipologiaVinoDAO: "+idAzienda+"\n");

      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda);
      
      SolmrLogger.debug(this, "Executing getListTipoTipologiaVinoForAzienda: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) {
        TipoTipologiaVinoVO tipoTipologiaVinoVO = new TipoTipologiaVinoVO();
        tipoTipologiaVinoVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
        tipoTipologiaVinoVO.setDescrizione(rs.getString("DESCRIZIONE"));
        elencoTipoTipologiaVino.add(tipoTipologiaVinoVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getListTipoTipologiaVinoForAzienda in TipoTipologiaVinoDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getListTipoTipologiaVinoForAzienda in TipoTipologiaVinoDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getListTipoTipologiaVinoForAzienda in TipoTipologiaVinoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getListTipoTipologiaVinoForAzienda in TipoTipologiaVinoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListTipoTipologiaVinoForAzienda method in TipoTipologiaVinoDAO\n");
    if(elencoTipoTipologiaVino.size() == 0) {
      return (TipoTipologiaVinoVO[])elencoTipoTipologiaVino.toArray(new TipoTipologiaVinoVO[0]);
    }
    else {
      return (TipoTipologiaVinoVO[])elencoTipoTipologiaVino.toArray(new TipoTipologiaVinoVO[elencoTipoTipologiaVino.size()]);
    }
  }
  
  public TipoTipologiaVinoVO[] getListTipoTipologiaVinoForDichCons(long idDichiarazioneConsistenza) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getListTipoTipologiaVinoForDichCons method in TipoTipologiaVinoDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoTipologiaVinoVO> elencoTipoTipologiaVino = new Vector<TipoTipologiaVinoVO>();

    try {
      SolmrLogger.debug(this, "Creating db-connection in getListTipoTipologiaVinoForDichCons method in TipoTipologiaVinoDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListTipoTipologiaVinoForDichCons method in TipoTipologiaVinoDAO and it values: "+conn+"\n");

      String query = 
          "SELECT " +
          "       DISTINCT TTV.ID_TIPOLOGIA_VINO, TTV.DESCRIZIONE " +
          "FROM " + 
          "       DB_UNITA_ARBOREA_DICHIARATA UAD, " +  
          "       DB_TIPO_TIPOLOGIA_VINO TTV, " +
          "       DB_DICHIARAZIONE_CONSISTENZA DC " +
          "WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
          "AND    DC.CODICE_FOTOGRAFIA_TERRENI = UAD.CODICE_FOTOGRAFIA_TERRENI  " +
          "AND    UAD.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO " +
          "ORDER BY  TTV.DESCRIZIONE ";

      SolmrLogger.debug(this, "Value of parameter 1 [ID_DICHIARAZIONE_CONSISTENZA] in getListTipoTipologiaVinoForDichCons method in TipoTipologiaVinoDAO: "+idDichiarazioneConsistenza+"\n");

      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idDichiarazioneConsistenza);
      
      SolmrLogger.debug(this, "Executing getListTipoTipologiaVinoForDichCons: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) {
        TipoTipologiaVinoVO tipoTipologiaVinoVO = new TipoTipologiaVinoVO();
        tipoTipologiaVinoVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
        tipoTipologiaVinoVO.setDescrizione(rs.getString("DESCRIZIONE"));
        elencoTipoTipologiaVino.add(tipoTipologiaVinoVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getListTipoTipologiaVinoForDichCons in TipoTipologiaVinoDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getListTipoTipologiaVinoForDichCons in TipoTipologiaVinoDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getListTipoTipologiaVinoForDichCons in TipoTipologiaVinoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getListTipoTipologiaVinoForDichCons in TipoTipologiaVinoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListTipoTipologiaVinoForDichCons method in TipoTipologiaVinoDAO\n");
    if(elencoTipoTipologiaVino.size() == 0) {
      return (TipoTipologiaVinoVO[])elencoTipoTipologiaVino.toArray(new TipoTipologiaVinoVO[0]);
    }
    else {
      return (TipoTipologiaVinoVO[])elencoTipoTipologiaVino.toArray(new TipoTipologiaVinoVO[elencoTipoTipologiaVino.size()]);
    }
  }
  
  
  /**
   * Restituiesce un vettore di TipoTipologiaVinoVO
   * 
   * @param istatComune
   * @return
   * @throws DataAccessException
   */
  public Vector<TipoTipologiaVinoVO> getListActiveTipoTipologiaVinoByComuneAndVarieta(String istatComune, Long idVarieta) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoTipologiaVinoVO> result = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[TipoTipologiaVinoDAO::getListActiveTipoTipologiaVinoByComuneAndVarieta] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   "
              + "SELECT DISTINCT  " 
              + "       TTV.ID_TIPOLOGIA_VINO, " 
              + "       TTV.DESCRIZIONE, "
              + "       TTV.CODICE, "
              + "       TTV.VINO_DOC, "
              + "       TTV.CODICE_MIPAF, "
              + "       TTV.FLAG_GESTIONE_VIGNA, "
              + "       TTV.FLAG_GESTIONE_ETICHETTA, "
              + "       TTV.FLAG_GESTIONE_MENZIONE, "
              + "       TTV.INDICAZIONE_VIGNA "
              + "FROM   "
              + "       DB_TIPO_TIPOLOGIA_VINO TTV,"
              + "       DB_TIPO_VINIDOC_COMUNI TVC, "
              + "       DB_TIPO_VINO_VARIETA TVV "
              + "WHERE  "
              + "       TVC.ISTAT_COMUNE = ? "
              + "AND    TVC.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO "
              + "AND    TTV.DATA_FINE_VALIDITA IS NULL "
              + "AND    TTV.FLAG_SCHEDARIO = 'S' "
              + "AND    TVV.ID_VARIETA = ? "
              + "AND    TTV.ID_TIPOLOGIA_VINO = TVV.ID_TIPOLOGIA_VINO "
              + "ORDER BY "
              + "       TTV.ID_TIPOLOGIA_VINO, TTV.DESCRIZIONE ");   
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[TipoTipologiaVinoDAO::getListActiveTipoTipologiaVinoByComuneAndVarieta] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      int indice = 0;
      stmt.setString(++indice,istatComune);
      stmt.setLong(++indice,idVarieta.longValue());
      
      ResultSet rs = stmt.executeQuery();
     
      TipoTipologiaVinoVO tipoTipologiaVinoVO = null;
      while(rs.next())
      {
        if(result == null)
        {
          result = new Vector<TipoTipologiaVinoVO>();
        }       
          
        tipoTipologiaVinoVO  = new TipoTipologiaVinoVO(); 
        tipoTipologiaVinoVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
        tipoTipologiaVinoVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoTipologiaVinoVO.setCodice(rs.getString("CODICE"));
        tipoTipologiaVinoVO.setVinoDoc(rs.getString("VINO_DOC"));
        tipoTipologiaVinoVO.setCodiceMipaf(rs.getString("CODICE_MIPAF"));
        tipoTipologiaVinoVO.setFlagGestioneVigna(rs.getString("FLAG_GESTIONE_VIGNA"));
        tipoTipologiaVinoVO.setFlagGestioneEtichetta(rs.getString("FLAG_GESTIONE_ETICHETTA"));
        tipoTipologiaVinoVO.setFlagGestioneMenzione(rs.getString("FLAG_GESTIONE_MENZIONE"));
        tipoTipologiaVinoVO.setIndicazioneVigna(rs.getString("INDICAZIONE_VIGNA"));
        
        
        result.add(tipoTipologiaVinoVO);
        
        
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
      { new Parametro("istatComune", istatComune) };
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[TipoTipologiaVinoDAO::getListActiveTipoTipologiaVinoByComuneAndVarieta] ",
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
              "[TipoTipologiaVinoDAO::getListActiveTipoTipologiaVinoByComuneAndVarieta] END.");
    }
  }
  
  
  /**
   * 
   * restituisce i reciord di DB vigna attivi
   * passando l'idTipologiaVino
   * 
   * @param idTipologiaVino
   * @return
   * @throws DataAccessException
   */
  public VignaVO getVignaByIdTipologiaVinoAndParticella(long idTipologiaVino, long idParticella) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    VignaVO vignaVO = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[TipoTipologiaVinoDAO::getVignaByIdTipologiaVinoAndParticella] BEGIN.");
  
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
          "SELECT DV.ID_VIGNA, " +
          "       DV.ID_TIPOLOGIA_VINO, " + 
          "       DV.MENZIONE, " +
          "       DV.NOTE, " +
          "       DV.DATA_INIZIO_VALIDITA, " +
          "       DV.DATA_FINE_VALIDITA " +
          "FROM   DB_VIGNA DV, " +
          "       DB_VIGNA_PARTICELLA VP " +
          "WHERE  DV.ID_TIPOLOGIA_VINO = ? " +
          "AND    DV.DATA_FINE_VALIDITA IS NULL " +
          "AND    VP.ID_PARTICELLA = ? " +
          "AND    DV.ID_VIGNA = VP.ID_VIGNA " +
          "AND    VP.DATA_FINE_VALIDITA IS NULL " +
          "ORDER BY DV.MENZIONE ");   
      
      
      
      query = queryBuf.toString();
      
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[TipoTipologiaVinoDAO::getVignaByIdTipologiaVinoAndParticella] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      int indice = 0;
      stmt.setLong(++indice,idTipologiaVino);
      stmt.setLong(++indice,idParticella);
      
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        vignaVO = new VignaVO();
        vignaVO.setIdVigna(new Long(rs.getLong("ID_VIGNA")));
        vignaVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
        vignaVO.setMenzione(rs.getString("MENZIONE"));
        vignaVO.setNote(rs.getString("NOTE"));
        vignaVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        vignaVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        
      }
      
      return vignaVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vignaVO", vignaVO) };
      
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idTipologiaVino", idTipologiaVino),
        new Parametro("idParticella", idParticella) };
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[TipoTipologiaVinoDAO::getVignaByIdTipologiaVinoAndParticella] ",
              t, query, variabili, parametri);
      
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[TipoTipologiaVinoDAO::getVignaByIdTipologiaVinoAndParticella] END.");
    }
  }
  
  
  /**
   * 
   * restituisce i record di DB vigna relativi alla dichiarazione di consistenza
   * passando l'idTipologiaVino
   * 
   * 
   * @param idTipologiaVino
   * @param dataInserimentoDichiarazione
   * @return
   * @throws DataAccessException
   */
  /*public Vector<VignaVO> getListVignaByIdTipologiaVinoAllaDichiarazione(long idTipologiaVino, Date dataInserimentoDichiarazione) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<VignaVO> result = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[TipoTipologiaVinoDAO::getListVignaByIdTipologiaVinoAllaDichiarazione] BEGIN.");
  
      
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   "
              + "SELECT DV.ID_VIGNA, "
              + "       DV.ID_TIPOLOGIA_VINO, " 
              + "       DV.MENZIONE, "
              + "       DV.NOTE, "
              + "       DV.DATA_INIZIO_VALIDITA, "
              + "       DV.DATA_FINE_VALIDITA "
              + "FROM   DB_VIGNA DV " 
              + "WHERE  DV.ID_TIPOLOGIA_VINO = ? "
              + "AND    DV.DATA_INIZIO_VALIDITA < ? " 
              + "AND    NVL(DV.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'dd/mm/yyyy')) > ? " 
              + "ORDER BY "
              + "       DV.MENZIONE ");   
      
      
      
      query = queryBuf.toString();
      
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[TipoTipologiaVinoDAO::getListVignaByIdTipologiaVinoAllaDichiarazione] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      int indice = 0;
      stmt.setLong(++indice,idTipologiaVino);
      stmt.setTimestamp(++indice,convertDateToTimestamp(dataInserimentoDichiarazione));
      stmt.setTimestamp(++indice,convertDateToTimestamp(dataInserimentoDichiarazione));
      
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(result == null)
        {
          result = new Vector<VignaVO>();
        }
        VignaVO vignaVO  = new VignaVO();
        vignaVO.setIdVigna(new Long(rs.getLong("ID_VIGNA")));
        vignaVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
        vignaVO.setMenzione(rs.getString("MENZIONE"));
        vignaVO.setNote(rs.getString("NOTE"));
        vignaVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        vignaVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        result.add(vignaVO);
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
      { new Parametro("idTipologiaVino", idTipologiaVino) };
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[TipoTipologiaVinoDAO::getListVignaByIdTipologiaVinoAllaDichiarazione] ",
              t, query, variabili, parametri);
      
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[TipoTipologiaVinoDAO::getListVignaByIdTipologiaVinoAllaDichiarazione] END.");
    }
  }*/
  
  
  /**
   * restituisce la tipologia vino dalla sua chiave primaria
   * 
   * 
   * 
   * @param idTipologiaVino
   * @return
   * @throws DataAccessException
   */
  public TipoTipologiaVinoVO getTipoTipologiaVinoByPrimaryKey(long idTipologiaVino) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    TipoTipologiaVinoVO tipoTipologiaVinoVO = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[TipoTipologiaVinoDAO::getTipoTipologiaVinoByPrimaryKey] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   "
              + "SELECT TTV.ID_TIPOLOGIA_VINO, " 
              + "       TTV.DESCRIZIONE, "
              + "       TTV.CODICE, "
              + "       TTV.VINO_DOC, "
              + "       TTV.CODICE_MIPAF, "
              + "       TTV.FLAG_GESTIONE_VIGNA, "
              + "       TTV.FLAG_GESTIONE_ETICHETTA "
              + "FROM   DB_TIPO_TIPOLOGIA_VINO TTV "
              + "WHERE  TTV.ID_TIPOLOGIA_VINO = ? ");   
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[TipoTipologiaVinoDAO::getTipoTipologiaVinoByPrimaryKey] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      int indice = 0;
      stmt.setLong(++indice,idTipologiaVino);
      
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        tipoTipologiaVinoVO  = new TipoTipologiaVinoVO(); 
        tipoTipologiaVinoVO.setIdTipologiaVino(checkLong(rs.getString("ID_TIPOLOGIA_VINO")));
        tipoTipologiaVinoVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoTipologiaVinoVO.setCodice(rs.getString("CODICE"));
        tipoTipologiaVinoVO.setVinoDoc(rs.getString("VINO_DOC"));
        tipoTipologiaVinoVO.setCodiceMipaf(rs.getString("CODICE_MIPAF"));
        tipoTipologiaVinoVO.setFlagGestioneVigna(rs.getString("FLAG_GESTIONE_VIGNA"));
        tipoTipologiaVinoVO.setFlagGestioneEtichetta(rs.getString("FLAG_GESTIONE_ETICHETTA"));
      }
      
      return tipoTipologiaVinoVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("tipoTipologiaVinoVO", tipoTipologiaVinoVO) };
      
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idTipologiaVino", idTipologiaVino) };
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[TipoTipologiaVinoDAO::getTipoTipologiaVinoByPrimaryKey] ",
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
              "[TipoTipologiaVinoDAO::getTipoTipologiaVinoByPrimaryKey] END.");
    }
  }
  
  
  public VignaVO getVignaByByPrimary(long idVigna) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    VignaVO vignaVO = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[TipoTipologiaVinoDAO::getVignaByByPrimary] BEGIN.");
  
      
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
          "SELECT DV.ID_VIGNA, " +
          "       DV.ID_TIPOLOGIA_VINO, " + 
          "       DV.MENZIONE, " +
          "       DV.NOTE, " +
          "       DV.DATA_INIZIO_VALIDITA, " +
          "       DV.DATA_FINE_VALIDITA " +
          "FROM   DB_VIGNA DV " +
          "WHERE  DV.ID_VIGNA = ? ");   
      
      
      
      query = queryBuf.toString();
      
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[TipoTipologiaVinoDAO::getVignaByByPrimary] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      int indice = 0;
      stmt.setLong(++indice,idVigna);
      
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        vignaVO  = new VignaVO();
        vignaVO.setIdVigna(new Long(rs.getLong("ID_VIGNA")));
        vignaVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
        vignaVO.setMenzione(rs.getString("MENZIONE"));
        vignaVO.setNote(rs.getString("NOTE"));
        vignaVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        vignaVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
       
      }
      
      return vignaVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vignaVO", vignaVO) };
      
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idVigna", idVigna) };
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[TipoTipologiaVinoDAO::getVignaByByPrimary] ",
              t, query, variabili, parametri);
      
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[TipoTipologiaVinoDAO::getVignaByByPrimary] END.");
    }
  }
  
  
}

