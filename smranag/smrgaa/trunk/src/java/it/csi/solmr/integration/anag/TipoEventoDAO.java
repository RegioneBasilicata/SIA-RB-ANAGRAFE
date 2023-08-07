package it.csi.solmr.integration.anag;
	
import it.csi.solmr.dto.anag.terreni.TipoEventoVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class TipoEventoDAO extends it.csi.solmr.integration.BaseDAO {


	public TipoEventoDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public TipoEventoDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo che mi restituisce l'elenco dei tipi eventi
	 * 
	 * @return 
	 * @throws DataAccessException
	 */
	public Vector<TipoEventoVO> getListTipiEvento() throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating getListTipiEvento method in TipoEventoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoEventoVO> elencoTipoEvento = null;

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in getListTipiEvento method in TipoEventoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListTipiEvento method in TipoEventoDAO and it values: "+conn+"\n");

			String query = " " +
					" SELECT ID_EVENTO, " +
					"        DESCRIZIONE, " +
					"        CESSA_PARTICELLA " +
					" FROM   DB_TIPO_EVENTO " +
			    " ORDER BY DESCRIZIONE ";
		

			stmt = conn.prepareStatement(query);
			

			SolmrLogger.debug(this, "Executing getListTipiEvento: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) 
			{
			  if(elencoTipoEvento == null)
			  {
			    elencoTipoEvento = new Vector<TipoEventoVO>();
			  }
			  TipoEventoVO tipoEventoVO = new TipoEventoVO();
			  tipoEventoVO.setIdEvento(rs.getLong("ID_EVENTO"));
			  tipoEventoVO.setDescrizione(rs.getString("DESCRIZIONE"));
			  String cessa = rs.getString("CESSA_PARTICELLA");
			  if(Validator.isNotEmpty(cessa) && cessa.equalsIgnoreCase("S"))
			  {
			    tipoEventoVO.setCessParticella(true);
			  }
			  
			  elencoTipoEvento.add(tipoEventoVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) 
		{
			SolmrLogger.error(this, "getListTipiEvento in TipoEventoDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) 
		{
			SolmrLogger.error(this, "getListTipiEvento in TipoEventoDAO - Generic Exception: "+ex+"\n");
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
				SolmrLogger.error(this, "getListTipiEvento in TipoEventoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) 
			{
				SolmrLogger.error(this, "getListTipiEvento in TipoEventoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipiEvento method in TipoEventoDAO\n");
		
		return elencoTipoEvento;
	}
	
	
	public String getFlagEventoCessaParticella(long idEvento) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getFlagEventoCessaParticella method in TipoEventoDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    String cessaParticella = "";

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getFlagEventoCessaParticella method in TipoEventoDAO\n");
      conn = getDatasource().getConnection();
   
      String query = " " +
          " SELECT CESSA_PARTICELLA " +
          " FROM   DB_TIPO_EVENTO " +
          " WHERE  ID_EVENTO = ? ";
    

      stmt = conn.prepareStatement(query);      

      SolmrLogger.debug(this, "Executing getFlagEventoCessaParticella: "+query+"\n");
      
      stmt.setLong(1, idEvento);

      ResultSet rs = stmt.executeQuery();

      if(rs.next()) 
      {
        cessaParticella = rs.getString("CESSA_PARTICELLA");      
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "getFlagEventoCessaParticella in TipoEventoDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) 
    {
      SolmrLogger.error(this, "getFlagEventoCessaParticella in TipoEventoDAO - Generic Exception: "+ex+"\n");
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
        SolmrLogger.error(this, "getFlagEventoCessaParticella in TipoEventoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) 
      {
        SolmrLogger.error(this, "getFlagEventoCessaParticella in TipoEventoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getFlagEventoCessaParticella method in TipoEventoDAO\n");
    
    return cessaParticella;
  }
}
