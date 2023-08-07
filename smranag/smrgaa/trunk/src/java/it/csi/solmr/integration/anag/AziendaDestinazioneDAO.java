package it.csi.solmr.integration.anag;

import it.csi.solmr.dto.anag.*;
import it.csi.solmr.exception.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.util.*;

import java.sql.*;
import java.util.*;

public class AziendaDestinazioneDAO extends it.csi.solmr.integration.BaseDAO {


	public AziendaDestinazioneDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public AziendaDestinazioneDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo utilizzato per inserire un record nella tabella DB_AZIENDA_DESTINAZIONE
	 *
	 * @param aziendaDestinazioneVO
	 * @return java.lang.Long
	 * @throws DataAccessException
	 */
	public Long insertAziendaDestinazione(AziendaDestinazioneVO aziendaDestinazioneVO) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating insertAziendaDestinazione method in AziendaDestinazioneDAO\n");
		Long idAziendaDestinazione = null;
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			idAziendaDestinazione = getNextPrimaryKey(SolmrConstants.SEQ_AZIENDA_DESTINAZIONE);
			SolmrLogger.debug(this, "Creating db-connection in insertAziendaDestinazione method in AziendaDestinazioneDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in insertAziendaDestinazione method in AziendaDestinazioneDAO and it values: "+conn+"\n");

			String query  = " INSERT INTO DB_AZIENDA_DESTINAZIONE "+
                    		"            (ID_AZIENDA_DESTINAZIONE, " +
                    		"             ID_AZIENDA, " +
                    		"             DATA_AGGIORNAMENTO, "+
                    		"             ID_UTENTE_AGGIORNAMENTO, " +
                    		"             ID_AZIENDA_DI_DESTINAZIONE) " +
                    		" VALUES     (?, ?, ?, ?, ?)";


			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing insertAziendaDestinazione: "+query);

			stmt.setLong(1, idAziendaDestinazione.longValue());
			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA_DESTINAZIONE] in method insertAziendaDestinazione in AziendaDestinazioneDAO: "+idAziendaDestinazione.longValue()+"\n");
			stmt.setLong(2, aziendaDestinazioneVO.getIdAzienda().longValue());
			SolmrLogger.debug(this, "Value of parameter 2 [ID_AZIENDA] in method insertAziendaDestinazione in AziendaDestinazioneDAO: "+aziendaDestinazioneVO.getIdAzienda().longValue()+"\n");
			stmt.setTimestamp(3, new Timestamp(aziendaDestinazioneVO.getDataAggiornamento().getTime()));
			SolmrLogger.debug(this, "Value of parameter 3 [DATA_AGGIORNAMENTO] in method insertAziendaDestinazione in AziendaDestinazioneDAO: "+new Timestamp(aziendaDestinazioneVO.getDataAggiornamento().getTime())+"\n");
			stmt.setLong(4, aziendaDestinazioneVO.getIdUtenteAggiornamento().longValue());
			SolmrLogger.debug(this, "Value of parameter 4 [ID_UTENTE_AGGIORNAMENTO] in method insertAziendaDestinazione in AziendaDestinazioneDAO: "+aziendaDestinazioneVO.getIdUtenteAggiornamento().longValue()+"\n");
			stmt.setLong(5, aziendaDestinazioneVO.getIdAziendaDiDestinazione().longValue());
			SolmrLogger.debug(this, "Value of parameter 5 [ID_AZIENDA_DI_DESTINAZIONE] in method insertAziendaDestinazione in AziendaDestinazioneDAO: "+aziendaDestinazioneVO.getIdAziendaDiDestinazione().longValue()+"\n");

			stmt.executeUpdate();

			stmt.close();
		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "SQLException in insertAziendaDestinazione: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "Generic Exception in insertAziendaDestinazione: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) {
					stmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "SQLException while closing Statement and Connection in insertAziendaDestinazione: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in insertAziendaDestinazione: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated insertAziendaDestinazione method in AziendaDestinazioneDAO\n");
		return idAziendaDestinazione;
	}



/**
 * Restituisce gli id delle aziende di destinazione
 * associate all'idAzienda passato
 * @param idAzienda Long
 * @throws DataAccessException
 * @return Long[] aziende di destinazione
 */
 public Long[] serviceGetIdAziendeDestinazione(Long idAzienda)
  throws DataAccessException
 {

  Connection conn = null;
  PreparedStatement stmt = null;
  ResultSet rs=null;
  String query=null;
  Vector<Long> result=new Vector<Long>();
  try
  {
    conn = getDatasource().getConnection();
    query = "SELECT ID_AZIENDA_DESTINAZIONE FROM DB_AZIENDA_DESTINAZIONE "+
            "WHERE ID_AZIENDA=? ";

    SolmrLogger.debug(this, "Executing serviceGetIdAziendeDestinazione: "+query);

    SolmrLogger.debug(this, "Executing serviceGetIdAziendeDestinazione idAzienda: "+idAzienda.longValue());

    stmt = conn.prepareStatement(query);

    stmt.setLong(1,idAzienda.longValue());

    rs=stmt.executeQuery();
    while (rs.next())
    {
      Long temp=new Long(rs.getString("ID_AZIENDA_DESTINAZIONE"));
      result.add(temp);
    }
    SolmrLogger.debug(this, "Executed serviceGetIdAziendeDestinazione");

    rs.close();

  }
  catch (SQLException exc)
  {
    SolmrLogger.fatal(this, "SQLException in serviceGetIdAziendeDestinazione query= "+query);
    SolmrLogger.fatal(this, "SQLException in serviceGetIdAziendeDestinazione exception="+exc.getMessage());
    throw new DataAccessException(exc.getMessage());
  }
  catch (Exception ex)
  {
    SolmrLogger.fatal(this, "Generic Exception in serviceGetIdAziendeDestinazione"+ex.getMessage());
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
      SolmrLogger.fatal(this, "SQLException while closing Statement and Connection in serviceGetIdAziendeDestinazione: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception while closing Statement and Connection in serviceGetIdAziendeDestinazione: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
  }
  return result.size()==0?null:(Long[])result.toArray(new Long[0]);
}

}
