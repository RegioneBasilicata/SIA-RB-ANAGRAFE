package it.csi.solmr.integration.anag;
	
import it.csi.solmr.dto.anag.terreni.UtilizzoConsociatoVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class UtilizzoConsociatoDAO extends it.csi.solmr.integration.BaseDAO {


	public UtilizzoConsociatoDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public UtilizzoConsociatoDAO(String refName) throws ResourceAccessException {
		super(refName);
	}
	
	/**
	 * Metodo che mi restituisce l'elenco degli utilizzi consociati a partire dall'id_utilizzo_particella
	 * 
	 * @param idUtilizzoParticella
	 * @param orderBy
	 * @return java.util.Vector
	 * @throws DataAccessException
	 */
	public Vector<UtilizzoConsociatoVO> getListUtilizziConsociatiByIdUtilizzoParticella(Long idUtilizzoParticella, String[] orderBy) throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating getListUtilizziConsociatiByIdUtilizzoParticella method in UtilizzoConsociatoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<UtilizzoConsociatoVO> elencoUtilizziConsociati = new Vector<UtilizzoConsociatoVO>();

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in getListUtilizziConsociatiByIdUtilizzoParticella method in UtilizzoConsociatoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListUtilizziConsociatiByIdUtilizzoParticella method in UtilizzoConsociatoDAO and it values: "+conn+"\n");

			String query = " SELECT UC.ID_UTILIZZO_CONSOCIATO, " +
						   "        UC.ID_UTILIZZO_PARTICELLA, " +
						   "        UC.ID_PIANTE_CONSOCIATE, " +
						   "        UC.NUMERO_PIANTE " +
						   " FROM   DB_TIPO_PIANTE_CONSOCIATE TPC, " +
						   "        DB_UTILIZZO_CONSOCIATO UC " +
						   " WHERE  UC.ID_UTILIZZO_PARTICELLA = ? " +
						   " AND    UC.ID_PIANTE_CONSOCIATE = TPC.ID_PIANTE_CONSOCIATE " +
						   " UNION ALL " +
						   " SELECT -1, " +
						   "        -1, " +
						   "        TPC.ID_PIANTE_CONSOCIATE, " + 
						   "        0 " +
						   " FROM   DB_TIPO_PIANTE_CONSOCIATE TPC " +
						   " WHERE  TPC.ID_PIANTE_CONSOCIATE NOT IN " +
						   " (SELECT ID_PIANTE_CONSOCIATE " +
						   "  FROM   DB_UTILIZZO_CONSOCIATO " +
						   "  WHERE  ID_UTILIZZO_PARTICELLA = ?) ";
		
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
			}
			else 
			{
				query += " ORDER BY ID_PIANTE_CONSOCIATE ";
			}

			SolmrLogger.debug(this, "Value of parameter 1 [ID_UTILIZZO_PARTICELLA] in getListUtilizziConsociatiByIdUtilizzoParticella method in UtilizzoConsociatoDAO: "+idUtilizzoParticella+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ID_UTILIZZO_PARTICELLA] in getListUtilizziConsociatiByIdUtilizzoParticella method in UtilizzoConsociatoDAO: "+idUtilizzoParticella+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ORDER_BY] in getListUtilizziConsociatiByIdUtilizzoParticella method in UtilizzoConsociatoDAO: "+orderBy+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idUtilizzoParticella.longValue());
			stmt.setLong(2, idUtilizzoParticella.longValue());

			SolmrLogger.debug(this, "Executing getListUtilizziConsociatiByIdUtilizzoParticella: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				UtilizzoConsociatoVO utilizzoConsociatoVO = new UtilizzoConsociatoVO();
				utilizzoConsociatoVO.setIdUtilizzoConsociato(new Long(rs.getLong("ID_UTILIZZO_CONSOCIATO")));
				utilizzoConsociatoVO.setIdUtilizzoParticella(new Long(rs.getLong("ID_UTILIZZO_PARTICELLA")));
				utilizzoConsociatoVO.setIdPianteConsociate(new Long(rs.getLong("ID_PIANTE_CONSOCIATE")));
				utilizzoConsociatoVO.setNumeroPiante(rs.getString("NUMERO_PIANTE"));
				elencoUtilizziConsociati.add(utilizzoConsociatoVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListUtilizziConsociatiByIdUtilizzoParticella in UtilizzoConsociatoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListUtilizziConsociatiByIdUtilizzoParticella in UtilizzoConsociatoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListUtilizziConsociatiByIdUtilizzoParticella in UtilizzoConsociatoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListUtilizziConsociatiByIdUtilizzoParticella in UtilizzoConsociatoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListUtilizziConsociatiByIdUtilizzoParticella method in UtilizzoConsociatoDAO\n");
		return elencoUtilizziConsociati;
	}
	
	/**
	 * Metodo utilizzato per eliminare un record dalla tabella DB_UTILIZZO_CONSOCIATO
	 * 
	 * @param idUtilizzoParticella
	 * @throws DataAccessException
	 */
	public void deleteUtilizzoConsociatoByIdUtilizzoParticella(Long idUtilizzoParticella) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating deleteUtilizzoConsociatoByIdUtilizzoParticella method in UtilizzoConsociatoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in deleteUtilizzoConsociatoByIdUtilizzoParticella method in UtilizzoConsociatoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in deleteUtilizzoConsociatoByIdUtilizzoParticella method in UtilizzoConsociatoDAO and it values: "+conn+"\n");

			String query = " DELETE FROM  DB_UTILIZZO_CONSOCIATO " +
						   "        WHERE ID_UTILIZZO_PARTICELLA = ? ";

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idUtilizzoParticella.longValue());
			SolmrLogger.debug(this, "Value of parameter 1 [ID_UTILIZZO_PARTICELLA] in getListUtilizziConsociatiByIdUtilizzoParticella method in UtilizzoConsociatoDAO: "+idUtilizzoParticella+"\n");

			SolmrLogger.debug(this, "Executing deleteUtilizzoConsociatoByIdUtilizzoParticella: "+query+"\n");

			stmt.executeUpdate();
			stmt.close();
		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "deleteUtilizzoConsociatoByIdUtilizzoParticella in UtilizzoConsociatoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "deleteUtilizzoConsociatoByIdUtilizzoParticella in UtilizzoConsociatoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "deleteUtilizzoConsociatoByIdUtilizzoParticella in UtilizzoConsociatoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "deleteUtilizzoConsociatoByIdUtilizzoParticella in UtilizzoConsociatoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated deleteUtilizzoConsociatoByIdUtilizzoParticella method in UtilizzoConsociatoDAO\n");
	}
	
	/**
	 * Metodo utilizzato per eliminare un record dalla tabella DB_UTILIZZO_CONSOCIATO
	 * a partire dall'id_conduzione_particella
	 * 
	 * @param idConduzioneParticella
	 * @throws DataAccessException
	 */
	public void deleteUtilizzoConsociatoByIdConduzioneParticella(Long idConduzioneParticella) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating deleteUtilizzoConsociatoByIdConduzioneParticella method in UtilizzoConsociatoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in deleteUtilizzoConsociatoByIdConduzioneParticella method in UtilizzoConsociatoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in deleteUtilizzoConsociatoByIdConduzioneParticella method in UtilizzoConsociatoDAO and it values: "+conn+"\n");

			String query = " DELETE FROM DB_UTILIZZO_CONSOCIATO " +
						   " WHERE       ID_UTILIZZO_PARTICELLA IN " +
						   "             (SELECT ID_UTILIZZO_PARTICELLA " +
						   "              FROM   DB_CONDUZIONE_PARTICELLA CP, " +
						   "                     DB_UTILIZZO_PARTICELLA UP " +
						   "              WHERE  CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA " +
						   "              AND    CP.ID_CONDUZIONE_PARTICELLA = ?) ";

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idConduzioneParticella.longValue());
			SolmrLogger.debug(this, "Value of parameter 1 [ID_UTILIZZO_PARTICELLA] in deleteUtilizzoConsociatoByIdConduzioneParticella method in UtilizzoConsociatoDAO: "+idConduzioneParticella+"\n");

			SolmrLogger.debug(this, "Executing deleteUtilizzoConsociatoByIdConduzioneParticella: "+query+"\n");

			stmt.executeUpdate();
			stmt.close();
		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "deleteUtilizzoConsociatoByIdConduzioneParticella in UtilizzoConsociatoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "deleteUtilizzoConsociatoByIdConduzioneParticella in UtilizzoConsociatoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "deleteUtilizzoConsociatoByIdConduzioneParticella in UtilizzoConsociatoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "deleteUtilizzoConsociatoByIdConduzioneParticella in UtilizzoConsociatoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated deleteUtilizzoConsociatoByIdConduzioneParticella method in UtilizzoConsociatoDAO\n");
	}
	
	/**
	 * Metodo utilizzato per inserire un record nella tabella DB_UTILIZZO_CONSOCIATO
	 * 
	 * @param it.csi.solmr.dto.anag.terreni.UtilizzoConsociatoVO
	 * @throws DataAccessException
	 */
	public void insertUtilizzoConsociato(UtilizzoConsociatoVO utilizzoConsociatoVO) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating insertUtilizzoConsociato method in UtilizzoConsociatoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Long idUtilizzoConsociato = null;

		try {
			idUtilizzoConsociato = getNextPrimaryKey(SolmrConstants.SEQ_UTILIZZO_CONSOCIATO);
			SolmrLogger.debug(this, "Creating db-connection in insertUtilizzoConsociato method in UtilizzoConsociatoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in insertUtilizzoConsociato method in UtilizzoConsociatoDAO and it values: "+conn+"\n");

			String query = " INSERT INTO DB_UTILIZZO_CONSOCIATO " +
						   "             (ID_UTILIZZO_CONSOCIATO, " +
						   "              ID_UTILIZZO_PARTICELLA, " +
						   "              ID_PIANTE_CONSOCIATE, " +
						   "              NUMERO_PIANTE) " +
						   " VALUES (?, ?, ?, ? )";

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idUtilizzoConsociato.longValue());
			SolmrLogger.debug(this, "Value of parameter 1 [ID_UTILIZZO_CONSOCIATO] in insertUtilizzoConsociato method in UtilizzoConsociatoDAO: "+idUtilizzoConsociato+"\n");
			stmt.setLong(2, utilizzoConsociatoVO.getIdUtilizzoParticella().longValue());
			SolmrLogger.debug(this, "Value of parameter 2 [ID_UTILIZZO_PARTICELLA] in insertUtilizzoConsociato method in UtilizzoConsociatoDAO: "+utilizzoConsociatoVO.getIdUtilizzoParticella().longValue()+"\n");
			stmt.setLong(3, utilizzoConsociatoVO.getIdPianteConsociate().longValue());
			SolmrLogger.debug(this, "Value of parameter 3 [ID_PIANTE_CONSOCIATE] in insertUtilizzoConsociato method in UtilizzoConsociatoDAO: "+utilizzoConsociatoVO.getIdPianteConsociate().longValue()+"\n");
			stmt.setLong(4, Long.parseLong(utilizzoConsociatoVO.getNumeroPiante()));
			SolmrLogger.debug(this, "Value of parameter 4 [NUMERO_PIANTE] in insertUtilizzoConsociato method in UtilizzoConsociatoDAO: "+Long.parseLong(utilizzoConsociatoVO.getNumeroPiante())+"\n");

			SolmrLogger.debug(this, "Executing insertUtilizzoConsociato: "+query+"\n");

			stmt.executeUpdate();
			stmt.close();
		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "insertUtilizzoConsociato in UtilizzoConsociatoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "insertUtilizzoConsociato in UtilizzoConsociatoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "insertUtilizzoConsociato in UtilizzoConsociatoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "insertUtilizzoConsociato in UtilizzoConsociatoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated insertUtilizzoConsociato method in UtilizzoConsociatoDAO\n");
	}
	
	/**
	 * estraggo gli utilizzi consociati per l'importazione particellare
	 * 
	 * 
	 * 
	 * @param idUtilizzoParticella
	 * @return
	 * @throws DataAccessException
	 */
	public Vector<UtilizzoConsociatoVO> getUtilizziConsociatiByIdUtilizzoParticellaForImport(Long idUtilizzoParticella) 
	    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getUtilizziConsociatiByIdUtilizzoParticellaForImport method in UtilizzoConsociatoDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<UtilizzoConsociatoVO> elencoUtilizziConsociati = null;

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getUtilizziConsociatiByIdUtilizzoParticellaForImport method in UtilizzoConsociatoDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getUtilizziConsociatiByIdUtilizzoParticellaForImport method in UtilizzoConsociatoDAO and it values: "+conn+"\n");

      String query = 
        "SELECT UC.ID_UTILIZZO_CONSOCIATO, " +
        "       UC.ID_UTILIZZO_PARTICELLA, " +
        "       UC.ID_PIANTE_CONSOCIATE, " +
        "       UC.NUMERO_PIANTE " +
        "FROM   DB_UTILIZZO_CONSOCIATO UC " +
        "WHERE  UC.ID_UTILIZZO_PARTICELLA = ? ";
    
      SolmrLogger.debug(this, "Value of parameter 1 [ID_UTILIZZO_PARTICELLA] in getUtilizziConsociatiByIdUtilizzoParticellaForImport method in UtilizzoConsociatoDAO: "+idUtilizzoParticella+"\n");

      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idUtilizzoParticella.longValue());
 
      SolmrLogger.debug(this, "Executing getUtilizziConsociatiByIdUtilizzoParticellaForImport: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        if(elencoUtilizziConsociati == null)
        {
          elencoUtilizziConsociati = new Vector<UtilizzoConsociatoVO>();
        }
        UtilizzoConsociatoVO utilizzoConsociatoVO = new UtilizzoConsociatoVO();
        utilizzoConsociatoVO.setIdUtilizzoConsociato(new Long(rs.getLong("ID_UTILIZZO_CONSOCIATO")));
        utilizzoConsociatoVO.setIdUtilizzoParticella(new Long(rs.getLong("ID_UTILIZZO_PARTICELLA")));
        utilizzoConsociatoVO.setIdPianteConsociate(new Long(rs.getLong("ID_PIANTE_CONSOCIATE")));
        utilizzoConsociatoVO.setNumeroPiante(rs.getString("NUMERO_PIANTE"));
        elencoUtilizziConsociati.add(utilizzoConsociatoVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "getUtilizziConsociatiByIdUtilizzoParticellaForImport in UtilizzoConsociatoDAO - SQLException: "+exc.getMessage()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) 
    {
      SolmrLogger.error(this, "getUtilizziConsociatiByIdUtilizzoParticellaForImport in UtilizzoConsociatoDAO - Generic Exception: "+ex+"\n");
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
        SolmrLogger.error(this, "getUtilizziConsociatiByIdUtilizzoParticellaForImport in UtilizzoConsociatoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) 
      {
        SolmrLogger.error(this, "getUtilizziConsociatiByIdUtilizzoParticellaForImport in UtilizzoConsociatoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getUtilizziConsociatiByIdUtilizzoParticellaForImport method in UtilizzoConsociatoDAO\n");
    
    return elencoUtilizziConsociati;
  }
	
	
}
