package it.csi.solmr.integration.anag;
	
import it.csi.solmr.dto.anag.terreni.*;
import it.csi.solmr.exception.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.util.*;
import java.sql.*;
import java.util.*;

public class TipoFormaAllevamentoDAO extends it.csi.solmr.integration.BaseDAO {


	public TipoFormaAllevamentoDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public TipoFormaAllevamentoDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo che mi restituisce l'elenco delle forme allevamento
	 * 
	 * @param onlyActive
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.TipoFormaAllevamentoVO
	 * @throws DataAccessException
	 */
	public TipoFormaAllevamentoVO[] getListTipoFormaAllevamento(
	    boolean onlyActive, String[] orderBy) throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating getListTipoFormaAllevamento method in TipoFormaAllevamentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoFormaAllevamentoVO> elencoFormeAllevamento = new Vector<TipoFormaAllevamentoVO>();

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in getListTipoFormaAllevamento method in TipoFormaAllevamentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListTipoFormaAllevamento method in TipoFormaAllevamentoDAO and it values: "+conn+"\n");

			String query = " SELECT ID_FORMA_ALLEVAMENTO, " +
						   "        ID_TIPOLOGIA_UNAR, " +
						   "        CODICE, " +
						   "        DESCRIZIONE, " +
						   "        DATA_INIZIO_VALIDITA, " +
						   "        DATA_FINE_VALIDITA " +
						   " FROM   DB_TIPO_FORMA_ALLEVAMENTO " +
						   " WHERE  ID_TIPOLOGIA_UNAR = 2 ";
			
			if(onlyActive) 
			{
				query +=   " AND  DATA_FINE_VALIDITA IS NULL ";
			}
			String ordinamento = "";
			if(orderBy != null && orderBy.length > 0) 
			{
				String criterio = "";
				for(int i = 0; i < orderBy.length; i++) 
				{
					if(i == 0) 
					{
						criterio = (String)orderBy[i];
					}
					else 
					{
						criterio += ", "+(String)orderBy[i];
					}
				}
				ordinamento = "ORDER BY "+criterio;
			}
			if(!ordinamento.equals("")) 
			{
				query += ordinamento;
			}

			SolmrLogger.debug(this, "Value of parameter 1 [ONLY_ACTIVE] in getListTipoFormaAllevamento method in TipoFormaAllevamentoDAO: "+onlyActive+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ORDER_BY] in getListTipoFormaAllevamento method in TipoFormaAllevamentoDAO: "+orderBy+"\n");

			stmt = conn.prepareStatement(query);
			

			SolmrLogger.debug(this, "Executing getListTipoFormaAllevamento: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) 
			{
				TipoFormaAllevamentoVO tipoFormaAllevamentoVO = new TipoFormaAllevamentoVO();
				tipoFormaAllevamentoVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
				tipoFormaAllevamentoVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
				tipoFormaAllevamentoVO.setCodice(rs.getString("CODICE"));
				tipoFormaAllevamentoVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoFormaAllevamentoVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				tipoFormaAllevamentoVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				elencoFormeAllevamento.add(tipoFormaAllevamentoVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListTipoFormaAllevamento in TipoFormaAllevamentoDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListTipoFormaAllevamento in TipoFormaAllevamentoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListTipoFormaAllevamento in TipoFormaAllevamentoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListTipoFormaAllevamento in TipoFormaAllevamentoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipoFormaAllevamento method in TipoFormaAllevamentoDAO\n");
		if(elencoFormeAllevamento.size() == 0) {
			return (TipoFormaAllevamentoVO[])elencoFormeAllevamento.toArray(new TipoFormaAllevamentoVO[0]);
		}
		else {
			return (TipoFormaAllevamentoVO[])elencoFormeAllevamento.toArray(new TipoFormaAllevamentoVO[elencoFormeAllevamento.size()]);
		}
	}
}
