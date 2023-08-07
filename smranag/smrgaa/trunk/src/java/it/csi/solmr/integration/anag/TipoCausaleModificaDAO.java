package it.csi.solmr.integration.anag;
	
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.anag.terreni.TipoCausaleModificaVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class TipoCausaleModificaDAO extends it.csi.solmr.integration.BaseDAO {


	public TipoCausaleModificaDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public TipoCausaleModificaDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo che mi restituisce l'elenco delle causali modifica
	 * 
	 * @param onlyActive
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.TipoCausaleModificaVO[]
	 * @throws DataAccessException
	 */
	public TipoCausaleModificaVO[] getListTipoCausaleModifica(boolean onlyActive, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListTipoCausaleModifica method in TipoCausaleModificaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoCausaleModificaVO> elencoCausaliModifica = new Vector<TipoCausaleModificaVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListTipoCausaleModifica method in TipoCausaleModificaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListTipoCausaleModifica method in TipoCausaleModificaDAO and it values: "+conn+"\n");

			String query = " SELECT ID_CAUSALE_MODIFICA, " +
						   "        DESCRIZIONE, " +
						   "        DATA_INIZIO_VALIDITA, " +
						   "        DATA_FINE_VALIDITA " +
						   " FROM   DB_TIPO_CAUSALE_MODIFICA ";
			
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

			SolmrLogger.debug(this, "Value of parameter 1 [ONLY_ACTIVE] in getListTipoCausaleModifica method in TipoCausaleModificaDAO: "+onlyActive+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ORDER_BY] in getListTipoCausaleModifica method in TipoCausaleModificaDAO: "+orderBy+"\n");

			stmt = conn.prepareStatement(query);
			
			SolmrLogger.debug(this, "Executing getListTipoCausaleModifica: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				TipoCausaleModificaVO tipoCausaleModificaVO = new TipoCausaleModificaVO();
				tipoCausaleModificaVO.setIdCausaleModifica(new Long(rs.getLong("ID_CAUSALE_MODIFICA")));
				tipoCausaleModificaVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoCausaleModificaVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				tipoCausaleModificaVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				elencoCausaliModifica.add(tipoCausaleModificaVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListTipoCausaleModifica in TipoCausaleModificaDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListTipoCausaleModifica in TipoCausaleModificaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListTipoCausaleModifica in TipoCausaleModificaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListTipoCausaleModifica in TipoCausaleModificaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipoCausaleModifica method in TipoCausaleModificaDAO\n");
		if(elencoCausaliModifica.size() == 0) {
			return (TipoCausaleModificaVO[])elencoCausaliModifica.toArray(new TipoCausaleModificaVO[0]);
		}
		else {
			return (TipoCausaleModificaVO[])elencoCausaliModifica.toArray(new TipoCausaleModificaVO[elencoCausaliModifica.size()]);
		}
	}
	
	/**
	 * ritorna l'elenco della cauali modifiche relative ad una azienda
	 * 
	 * 
	 * 
	 * @param idAzienda
	 * @return
	 * @throws DataAccessException
	 */
	public Vector<TipoCausaleModificaVO> getListTipoCuasaleModificaByIdAzienda(long idAzienda)  
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoCausaleModificaVO> result = null;
    try
    {
      SolmrLogger.debug(this,
          "[TipoCausaleModificaDAO::getListTipoCuasaleModificaByIdAzienda] BEGIN.");      
       
      queryBuf = new StringBuffer();
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT DISTINCT " +
        "       TCM.ID_CAUSALE_MODIFICA, " +
        "       TCM.DESCRIZIONE " +
        "FROM " + 
        "       DB_STORICO_UNITA_ARBOREA SUA, " +
        "       DB_TIPO_CAUSALE_MODIFICA TCM " +
        "WHERE " +
        "       SUA.ID_AZIENDA = ? " +
        "AND    SUA.ID_CAUSALE_MODIFICA = TCM.ID_CAUSALE_MODIFICA " +
        "ORDER BY  TCM.DESCRIZIONE ");
      
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[TipoCausaleModificaDAO::getListTipoCuasaleModificaByIdAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1,idAzienda);
      
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(result == null)
        {
          result = new Vector<TipoCausaleModificaVO>();
        }
        TipoCausaleModificaVO tipoCausaleModificaVO = new TipoCausaleModificaVO();
        tipoCausaleModificaVO.setIdCausaleModifica(new Long(rs.getLong("ID_CAUSALE_MODIFICA")));
        tipoCausaleModificaVO.setDescrizione(rs.getString("DESCRIZIONE"));
        
        result.add(tipoCausaleModificaVO);        
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("result", result) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
  
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[TipoCausaleModificaDAO::getListTipoCuasaleModificaByIdAzienda] ", t,
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
          "[TipoCausaleModificaDAO::getListTipoCuasaleModificaByIdAzienda] END.");
    }
  }
	
	
	
	
	
}
