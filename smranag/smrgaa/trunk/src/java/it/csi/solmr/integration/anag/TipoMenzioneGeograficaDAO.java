package it.csi.solmr.integration.anag;
	
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.anag.terreni.MenzioneParticellaVO;
import it.csi.solmr.dto.anag.terreni.TipoMenzioneGeograficaVO;
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

public class TipoMenzioneGeograficaDAO extends it.csi.solmr.integration.BaseDAO 
{


	public TipoMenzioneGeograficaDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public TipoMenzioneGeograficaDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	
	/**
	 * 
	 * Estrae le menzioni geografiche relative alla particella ed alla dichiarazione di consistenza
	 * se valorizzata
	 * 
	 * 
	 * @param idParticella
	 * @param dataInserimentoDichiarazione
	 * @return
	 * @throws DataAccessException
	 */
	public Vector<TipoMenzioneGeograficaVO> getListTipoMenzioneGeografica(long idParticella, 
	    Long idTipologiaVino, java.util.Date dataInserimentoDichiarazione) 
	  throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating getListTipoMenzioneGeografica method in TipoMenzioneGeograficaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoMenzioneGeograficaVO> elencoMenzioneGeografica = null;

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in getListTipoMenzioneGeografica method in TipoMenzioneGeograficaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListTipoMenzioneGeografica method in TipoMenzioneGeograficaDAO and it values: "+conn+"\n");

			String query = "" +
					"SELECT MG.ID_MENZIONE_GEOGRAFICA, " +
					"       MG.DESCRIZIONE, " +
					"       MP.ID_PARTICELLA, " +
					"       MG.ID_TIPOLOGIA_VINO " +
					"FROM   DB_R_MENZIONE_PARTICELLA MP," +
					"       DB_TIPO_MENZIONE_GEOGRAFICA MG " +
					"WHERE  MP.ID_PARTICELLA = ? " +
					"AND    MP.ID_MENZIONE_GEOGRAFICA = MG.ID_MENZIONE_GEOGRAFICA " +
			    "AND    MG.ID_TIPOLOGIA_VINO = ? ";
			
			if(Validator.isNotEmpty(dataInserimentoDichiarazione))
			{
			  query +=""+
			  "  AND  MP.DATA_INIZIO_VALIDITA < ? " +
        "  AND  NVL(MP.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'dd/mm/yyyy')) > ? " +
			  "  AND  MG.DATA_INIZIO_VALIDITA < ? " +
        "  AND  NVL(MG.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'dd/mm/yyyy')) > ? ";
			}
			else
			{
			  query +=""+
			    "AND MP.DATA_FINE_VALIDITA IS NULL "+
			    "AND MG.DATA_FINE_VALIDITA IS NULL ";
			}
			

			SolmrLogger.debug(this, "Value of parameter 1 [ID_PARTICELLA] in getListTipoMenzioneGeografica method in TipoMenzioneGeograficaDAO: "+idParticella+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ID_TIPOLOGIA_VINO] in getListTipoMenzioneGeografica method in TipoMenzioneGeograficaDAO: "+idTipologiaVino+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [DATA_INSERIMENTO_DICHIARAZIONE] in getListTipoMenzioneGeografica method in TipoMenzioneGeograficaDAO: "+dataInserimentoDichiarazione+"\n");

			stmt = conn.prepareStatement(query);
			
			int indice = 0;
      stmt.setLong(++indice,idParticella);
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(idTipologiaVino));      
      
      if(Validator.isNotEmpty(dataInserimentoDichiarazione))
      {
        stmt.setTimestamp(++indice,convertDateToTimestamp(dataInserimentoDichiarazione));
        stmt.setTimestamp(++indice,convertDateToTimestamp(dataInserimentoDichiarazione));
        stmt.setTimestamp(++indice,convertDateToTimestamp(dataInserimentoDichiarazione));
        stmt.setTimestamp(++indice,convertDateToTimestamp(dataInserimentoDichiarazione));
      }
			

			SolmrLogger.debug(this, "Executing getListTipoMenzioneGeografica: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) 
			{
			  if(elencoMenzioneGeografica == null)
			  {
			    elencoMenzioneGeografica = new Vector<TipoMenzioneGeograficaVO>();
			  }
				TipoMenzioneGeograficaVO tipoMenzioneGeograficaVO = new TipoMenzioneGeograficaVO();
				tipoMenzioneGeograficaVO.setIdParticella(checkLongNull(rs.getString("ID_PARTICELLA")));
				tipoMenzioneGeograficaVO.setIdMenzioneGeografica(checkLongNull(rs.getString("ID_MENZIONE_GEOGRAFICA")));
				tipoMenzioneGeograficaVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoMenzioneGeograficaVO.setIdTipologiaVino(checkLongNull(rs.getString("ID_TIPOLOGIA_VINO")));
				
				
				elencoMenzioneGeografica.add(tipoMenzioneGeograficaVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) 
		{
			SolmrLogger.error(this, "getListTipoMenzioneGeografica in TipoMenzioneGeograficaDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) 
		{
			SolmrLogger.error(this, "getListTipoMenzioneGeografica in TipoMenzioneGeograficaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally 
		{
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListTipoMenzioneGeografica in TipoMenzioneGeograficaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListTipoMenzioneGeografica in TipoMenzioneGeograficaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipoMenzioneGeografica method in TipoMenzioneGeograficaDAO\n");
		
		return elencoMenzioneGeografica;
		
	}
	
	
	
	/**
	 * ritorna le menzione geografica attraverso la chiave primaria...
	 * 
	 * 
	 * 
	 * @param idMenzioneGeografica
	 * @return
	 * @throws DataAccessException
	 */
	public TipoMenzioneGeograficaVO getTipoMenzioneGeografica(long idMenzioneGeografica) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getTipoMenzioneGeografica method in TipoMenzioneGeograficaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    TipoMenzioneGeograficaVO tipoMenzioneGeograficaVO = null;
  
    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getTipoMenzioneGeografica method in TipoMenzioneGeograficaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getTipoMenzioneGeografica method in TipoMenzioneGeograficaDAO and it values: "+conn+"\n");
  
      String query = "" +
          "SELECT MG.ID_MENZIONE_GEOGRAFICA, " +
          "       MG.DESCRIZIONE " +
          "FROM   DB_TIPO_MENZIONE_GEOGRAFICA MG " +
          "WHERE  MG.ID_MENZIONE_GEOGRAFICA = ? ";
  
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idMenzioneGeografica);
      
      
  
      SolmrLogger.debug(this, "Executing getTipoMenzioneGeografica: "+query+"\n");
  
      ResultSet rs = stmt.executeQuery();
  
      if(rs.next()) 
      {
        tipoMenzioneGeograficaVO = new TipoMenzioneGeograficaVO();
        
        tipoMenzioneGeograficaVO.setIdMenzioneGeografica(checkLongNull(rs.getString("ID_MENZIONE_GEOGRAFICA")));
        tipoMenzioneGeograficaVO.setDescrizione(rs.getString("DESCRIZIONE"));
        
      }
      
      rs.close();
      stmt.close();
  
    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "getTipoMenzioneGeografica in TipoMenzioneGeograficaDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) 
    {
      SolmrLogger.error(this, "getTipoMenzioneGeografica in TipoMenzioneGeograficaDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getTipoMenzioneGeografica in TipoMenzioneGeograficaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getTipoMenzioneGeografica in TipoMenzioneGeograficaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getTipoMenzioneGeografica method in TipoMenzioneGeograficaDAO\n");
    
    return tipoMenzioneGeograficaVO;
    
  }
	
	
	/**
	 * ritorna le Menzioni particella attiva per la singola particella
	 * 
	 * 
	 * @param idParticella
	 * @return
	 * @throws DataAccessException
	 */
	public Vector<MenzioneParticellaVO> getVMenzioneParticellaAttiva(long idParticella) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getVMenzioneParticellaAttiva method in TipoMenzioneGeograficaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<MenzioneParticellaVO> vMenzioneParticella = null;

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getVMenzioneParticellaAttiva method in TipoMenzioneGeograficaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getVMenzioneParticellaAttiva method in TipoMenzioneGeograficaDAO and it values: "+conn+"\n");

      String query = "" +
          "SELECT MP.ID_MENZIONE_GEOGRAFICA, " +
          "       MP.ID_MENZIONE_PARTICELLA, " +
          "       MP.ID_PARTICELLA, " +
          "       MP.DATA_INIZIO_VALIDITA, " +
          "       MP.DATA_FINE_VALIDITA " +
          "FROM   DB_R_MENZIONE_PARTICELLA MP," +
          "       DB_TIPO_MENZIONE_GEOGRAFICA MG " +
          "WHERE  MP.ID_PARTICELLA = ? " +
          "AND    MP.ID_MENZIONE_GEOGRAFICA = MG.ID_MENZIONE_GEOGRAFICA " +
          "AND    MP.DATA_FINE_VALIDITA IS NULL " +
          "AND    MG.DATA_FINE_VALIDITA IS NULL ";
      
      

      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice,idParticella);
      
      

      SolmrLogger.debug(this, "Executing getVMenzioneParticellaAttiva: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        if(vMenzioneParticella == null)
        {
          vMenzioneParticella = new Vector<MenzioneParticellaVO>();
        }
        MenzioneParticellaVO menzioneParticellaVO = new MenzioneParticellaVO();
        menzioneParticellaVO.setIdParticella(checkLongNull(rs.getString("ID_PARTICELLA")));
        menzioneParticellaVO.setIdMenzioneGeografica(checkLongNull(rs.getString("ID_MENZIONE_GEOGRAFICA")));
        menzioneParticellaVO.setIdMenzioneParticella(checkLong(rs.getString("ID_MENZIONE_PARTICELLA")));
        menzioneParticellaVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        menzioneParticellaVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        
        vMenzioneParticella.add(menzioneParticellaVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "getVMenzioneParticellaAttiva in TipoMenzioneGeograficaDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) 
    {
      SolmrLogger.error(this, "getVMenzioneParticellaAttiva in TipoMenzioneGeograficaDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getVMenzioneParticellaAttiva in TipoMenzioneGeograficaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getVMenzioneParticellaAttiva in TipoMenzioneGeograficaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getVMenzioneParticellaAttiva method in TipoMenzioneGeograficaDAO\n");
    
    return vMenzioneParticella;
    
  }
	
	
	public Long insertMenzioneParticella(MenzioneParticellaVO menzioneParticellaVO) 
	    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idMenzioneParticella = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[TipoMenzioneGeograficaDAO::insertMenzioneParticella] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idMenzioneParticella = getNextPrimaryKey(SolmrConstants.SEQ_DB_R_MENZIONE_PARTICELLA);
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_R_MENZIONE_PARTICELLA   " 
              + "     (ID_MENZIONE_PARTICELLA, "
              + "     ID_PARTICELLA, " 
              + "     ID_MENZIONE_GEOGRAFICA, "
              + "     DATA_INIZIO_VALIDITA, "
              + "     DATA_FINE_VALIDITA)  "
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
            "[TipoMenzioneGeograficaDAO::insertMenzioneParticella] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idMenzioneParticella.longValue());
      stmt.setLong(++indice, menzioneParticellaVO.getIdParticella());
      stmt.setLong(++indice, menzioneParticellaVO.getIdMenzioneGeografica());
      stmt.setTimestamp(++indice, convertDateToTimestamp(menzioneParticellaVO.getDataInizioValidita()));   
      stmt.setTimestamp(++indice, convertDateToTimestamp(menzioneParticellaVO.getDataFineValidita()));   
  
      stmt.executeUpdate();
      
      return idMenzioneParticella;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idMenzioneParticella", idMenzioneParticella)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("menzioneParticellaVO", menzioneParticellaVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[TipoMenzioneGeograficaDAO::insertMenzioneParticella] ",
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
              "[TipoMenzioneGeograficaDAO::insertMenzioneParticella] END.");
    }
  }
	
	
	public void storicizzaMenzioneParticella(long idMenzioneParticella) 
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
              "[TipoMenzioneGeograficaDAO::storicizzaMenzioneParticella] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   UPDATE DB_R_MENZIONE_PARTICELLA   " 
              + "     SET  DATA_FINE_VALIDITA = ? "
              + "   WHERE  "
              + "     ID_MENZIONE_PARTICELLA = ?  ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[TipoMenzioneGeograficaDAO::storicizzaMenzioneParticella] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setTimestamp(++indice, convertDateToTimestamp(new Date()));
      stmt.setLong(++indice, idMenzioneParticella);
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idMenzioneParticella", idMenzioneParticella)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[TipoMenzioneGeograficaDAO::storicizzaMenzioneParticella] ",
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
              "[TipoMenzioneGeograficaDAO::storicizzaMenzioneParticella] END.");
    }
  }
	
	
	
	
}
