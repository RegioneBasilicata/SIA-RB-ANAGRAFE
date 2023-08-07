package it.csi.solmr.integration.anag;

import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.terreni.*;
import it.csi.solmr.exception.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.util.*;
import java.sql.*;
import java.util.*;

public class DichiarazioneSegnalazioneDAO extends it.csi.solmr.integration.BaseDAO {


	public DichiarazioneSegnalazioneDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public DichiarazioneSegnalazioneDAO(String refName) throws ResourceAccessException {
		super(refName);
	}
	
	/**
	 * Metodo che mi restituisce l'elenco delle segnalazioni relative all'azienda, ad
	 * una determinata dichiarazione di consistenza di una particella
	 * 
	 * @param idAzienda
	 * @param idDichiarazioneConsistenza
	 * @param idStoricoParticella
	 * @return java.util.Vector
	 * @throws DataAccessException
	 */
	public Vector<DichiarazioneSegnalazioneVO> getListDichiarazioneSegnalazioni(Long idAzienda, Long idDichiarazioneConsistenza, Long idStoricoParticella) 
	    throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating getListDichiarazioneSegnalazioni method in DichiarazioneSegnalazioneDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<DichiarazioneSegnalazioneVO> elencoDichiarazioniSegnalazioni = new Vector<DichiarazioneSegnalazioneVO>();

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in getListDichiarazioneSegnalazioni method in DichiarazioneSegnalazioneDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListDichiarazioneSegnalazioni method in DichiarazioneSegnalazioneDAO and it values: "+conn+"\n");

			String query = " SELECT DS.ID_DICHIARAZIONE_SEGNALAZIONE, " +
						   "		DS.ID_DICHIARAZIONE_CONSISTENZA, " +
						   "		DS.ID_CONTROLLO, " +
						   "        TC.DESCRIZIONE AS DESC_CONTROLLO, " +
						   "		DS.DESCRIZIONE, " +
						   "		DS.ID_AZIENDA, " +
						   "		DS.DATA_CONTROLLO, " +
						   "		DS.BLOCCANTE, " +
						   "		DS.ID_STORICO_PARTICELLA, " +
						   "        DS.ID_STORICO_UNITA_ARBOREA " +
						   " FROM   DB_DICHIARAZIONE_SEGNALAZIONE DS, " +
						   "        DB_TIPO_CONTROLLO TC " +
						   " WHERE  DS.ID_AZIENDA = ? " +
						   " AND    DS.ID_DICHIARAZIONE_CONSISTENZA = ? " +
						   " AND    DS.ID_STORICO_PARTICELLA = ? " +
						   " AND    DS.ID_CONTROLLO = TC.ID_CONTROLLO ";

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListDichiarazioneSegnalazioni method in DichiarazioneSegnalazioneDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ID_DICHIARAZIONE_CONSISTENZA] in getListDichiarazioneSegnalazioni method in DichiarazioneSegnalazioneDAO: "+idDichiarazioneConsistenza+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ID_STORICO_PARTICELLA] in getListDichiarazioneSegnalazioni method in DichiarazioneSegnalazioneDAO: "+idStoricoParticella+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());
			stmt.setLong(2, idDichiarazioneConsistenza.longValue());
			stmt.setLong(3, idStoricoParticella.longValue());

			SolmrLogger.debug(this, "Executing getListDichiarazioneSegnalazioni: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				DichiarazioneSegnalazioneVO dichiarazioneSegnalazioneVO = new DichiarazioneSegnalazioneVO();
				dichiarazioneSegnalazioneVO.setIdDichiarazioneSegnalazione(new Long(rs.getLong("ID_DICHIARAZIONE_SEGNALAZIONE")));
				if(Validator.isNotEmpty(rs.getString("ID_DICHIARAZIONE_CONSISTENZA"))) {
					dichiarazioneSegnalazioneVO.setIdDichiarazioneConsistenza(new Long(rs.getLong("ID_DICHIARAZIONE_CONSISTENZA")));
				}
				dichiarazioneSegnalazioneVO.setIdControllo(new Long(rs.getLong("ID_CONTROLLO")));
				CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_CONTROLLO")), rs.getString("DESC_CONTROLLO"));
				dichiarazioneSegnalazioneVO.setControllo(code);
				dichiarazioneSegnalazioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
				dichiarazioneSegnalazioneVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
				dichiarazioneSegnalazioneVO.setDataControllo(rs.getDate("DATA_CONTROLLO"));
				dichiarazioneSegnalazioneVO.setBloccante(rs.getString("BLOCCANTE"));
				if(Validator.isNotEmpty(rs.getString("ID_STORICO_PARTICELLA"))) {
					dichiarazioneSegnalazioneVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_STORICO_UNITA_ARBOREA"))) {
					dichiarazioneSegnalazioneVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
				}
				elencoDichiarazioniSegnalazioni.add(dichiarazioneSegnalazioneVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListDichiarazioneSegnalazioni in DichiarazioneSegnalazioneDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListDichiarazioneSegnalazioni in DichiarazioneSegnalazioneDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListDichiarazioneSegnalazioni in DichiarazioneSegnalazioneDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListDichiarazioneSegnalazioni in DichiarazioneSegnalazioneDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListDichiarazioneSegnalazioni method in DichiarazioneSegnalazioneDAO\n");
		return elencoDichiarazioniSegnalazioni;
	}
	
	/**
	 * Metodo che mi restituisce l'elenco delle segnalazioni relative all'azienda, ad
	 * una determinata dichiarazione di consistenza di una determinata unità arborea
	 * 
	 * @param idAzienda
	 * @param idDichiarazioneConsistenza
	 * @param idStoricoUnitaArborea
	 * @param orderBy
	 * @return java.util.Vector
	 * @throws DataAccessException
	 */
	public Vector<DichiarazioneSegnalazioneVO> getListDichiarazioneSegnalazioniUnar(Long idAzienda, Long idDichiarazioneConsistenza, Long idStoricoUnitaArborea, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListDichiarazioneSegnalazioniUnar method in DichiarazioneSegnalazioneDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<DichiarazioneSegnalazioneVO> elencoDichiarazioniSegnalazioni = new Vector<DichiarazioneSegnalazioneVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListDichiarazioneSegnalazioniUnar method in DichiarazioneSegnalazioneDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListDichiarazioneSegnalazioniUnar method in DichiarazioneSegnalazioneDAO and it values: "+conn+"\n");

			String query = " SELECT DS.ID_DICHIARAZIONE_SEGNALAZIONE, " +
						   "		DS.ID_DICHIARAZIONE_CONSISTENZA, " +
						   "		DS.ID_CONTROLLO, " +
						   "        TC.DESCRIZIONE AS DESC_CONTROLLO, " +
						   "		DS.DESCRIZIONE, " +
						   "		DS.ID_AZIENDA, " +
						   "		DS.DATA_CONTROLLO, " +
						   "		DS.BLOCCANTE, " +
						   "		DS.ID_STORICO_PARTICELLA, " +
						   "        DS.ID_STORICO_UNITA_ARBOREA " +
						   " FROM   DB_DICHIARAZIONE_SEGNALAZIONE DS, " +
						   "        DB_TIPO_CONTROLLO TC " +
						   " WHERE  DS.ID_AZIENDA = ? " +
						   " AND    DS.ID_DICHIARAZIONE_CONSISTENZA = ? " +
						   " AND    DS.ID_STORICO_UNITA_ARBOREA = ? " +
						   " AND    DS.ID_CONTROLLO = TC.ID_CONTROLLO ";
			
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

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListDichiarazioneSegnalazioniUnar method in DichiarazioneSegnalazioneDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ID_DICHIARAZIONE_CONSISTENZA] in getListDichiarazioneSegnalazioniUnar method in DichiarazioneSegnalazioneDAO: "+idDichiarazioneConsistenza+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ID_STORICO_UNITA_ARBOREA] in getListDichiarazioneSegnalazioniUnar method in DichiarazioneSegnalazioneDAO: "+idStoricoUnitaArborea+"\n");
			SolmrLogger.debug(this, "Value of parameter 4 [ORDER_BY] in getListDichiarazioneSegnalazioniUnar method in DichiarazioneSegnalazioneDAO: "+orderBy+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());
			stmt.setLong(2, idDichiarazioneConsistenza.longValue());
			stmt.setLong(3, idStoricoUnitaArborea.longValue());

			SolmrLogger.debug(this, "Executing getListDichiarazioneSegnalazioniUnar: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				DichiarazioneSegnalazioneVO dichiarazioneSegnalazioneVO = new DichiarazioneSegnalazioneVO();
				dichiarazioneSegnalazioneVO.setIdDichiarazioneSegnalazione(new Long(rs.getLong("ID_DICHIARAZIONE_SEGNALAZIONE")));
				if(Validator.isNotEmpty(rs.getString("ID_DICHIARAZIONE_CONSISTENZA"))) {
					dichiarazioneSegnalazioneVO.setIdDichiarazioneConsistenza(new Long(rs.getLong("ID_DICHIARAZIONE_CONSISTENZA")));
				}
				dichiarazioneSegnalazioneVO.setIdControllo(new Long(rs.getLong("ID_CONTROLLO")));
				CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_CONTROLLO")), rs.getString("DESC_CONTROLLO"));
				dichiarazioneSegnalazioneVO.setControllo(code);
				dichiarazioneSegnalazioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
				dichiarazioneSegnalazioneVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
				dichiarazioneSegnalazioneVO.setDataControllo(rs.getDate("DATA_CONTROLLO"));
				dichiarazioneSegnalazioneVO.setBloccante(rs.getString("BLOCCANTE"));
				if(Validator.isNotEmpty(rs.getString("ID_STORICO_PARTICELLA"))) {
					dichiarazioneSegnalazioneVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_STORICO_UNITA_ARBOREA"))) {
					dichiarazioneSegnalazioneVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
				}
				elencoDichiarazioniSegnalazioni.add(dichiarazioneSegnalazioneVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListDichiarazioneSegnalazioni in DichiarazioneSegnalazioneDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListDichiarazioneSegnalazioni in DichiarazioneSegnalazioneDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListDichiarazioneSegnalazioni in DichiarazioneSegnalazioneDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListDichiarazioneSegnalazioni in DichiarazioneSegnalazioneDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListDichiarazioneSegnalazioni method in DichiarazioneSegnalazioneDAO\n");
		return elencoDichiarazioniSegnalazioni;
	}
	
	
	/**
	 * 
	 * Controlo se esiste almeno un record sulla tavola db_dichiarazione_segnalazione accedendo per:
   * id_azienda=identificativo dell’azienda in esame
   * porre in join con db_tipo_controllo tramite id_controllo filtrando per id_gruppo_controllo=4 (Terreni)
   * id_dichiarazione_consistenza  is null.
   * Utilizzato per ricavare le associazioni particelle documento nel popDocumentoParticelle
	 * 
	 * 
	 * 
	 * @param idAzienda
	 * @return
	 * @throws DataAccessException
	 */
	public boolean isSegnalazioneForDocumentoParticella(long idAzienda) 
	  throws DataAccessException {
    SolmrLogger.debug(this, "Invocating isSegnalazioneForDocumentoParticella method in DichiarazioneSegnalazioneDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    boolean trovato = false;

    try {
      SolmrLogger.debug(this, "Creating db-connection in isSegnalazioneForDocumentoParticella method in DichiarazioneSegnalazioneDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in isSegnalazioneForDocumentoParticella method in DichiarazioneSegnalazioneDAO and it values: "+conn+"\n");

      String query = " " +
      		     "SELECT DS.ID_DICHIARAZIONE_SEGNALAZIONE " +
               "FROM   DB_DICHIARAZIONE_SEGNALAZIONE DS, " +
               "       DB_TIPO_CONTROLLO TC " +
               "WHERE  DS.ID_AZIENDA = ? " +
               "AND    DS.ID_DICHIARAZIONE_CONSISTENZA IS NULL " +
               "AND    DS.ID_CONTROLLO = TC.ID_CONTROLLO " +
               "AND    TC.ID_GRUPPO_CONTROLLO = ? ";

      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in isSegnalazioneForDocumentoParticella method in DichiarazioneSegnalazioneDAO: "+idAzienda+"\n");

      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda);
      stmt.setLong(2, SolmrConstants.ID_GRUPPO_CONTROLLO_PARTICELLARE.longValue()); 

      SolmrLogger.debug(this, "Executing isSegnalazioneForDocumentoParticella: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      if(rs.next()) 
      {
        trovato = true;
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "isSegnalazioneForDocumentoParticella in DichiarazioneSegnalazioneDAO - SQLException: "+exc.getMessage()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "isSegnalazioneForDocumentoParticella in DichiarazioneSegnalazioneDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "isSegnalazioneForDocumentoParticella in DichiarazioneSegnalazioneDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "isSegnalazioneForDocumentoParticella in DichiarazioneSegnalazioneDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated isSegnalazioneForDocumentoParticella method in DichiarazioneSegnalazioneDAO\n");
    return trovato;
  }
	
	/**
	 * 
	 * 
	 * Ritorna le segnalazioni generate dal plsql PCK_DICHIARAZIONE_CONSISTENZA.controlli_fase
	 * 
	 * 
	 * @param idAzienda
	 * @return
	 * @throws DataAccessException
	 */
	public Vector<DichiarazioneSegnalazioneVO> getListDichiarazioneSegnalazioniConsolidamentoUV(Long idAzienda) 
      throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getListDichiarazioneSegnalazioniConsolidamentoUV method in DichiarazioneSegnalazioneDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<DichiarazioneSegnalazioneVO> elencoDichiarazioniSegnalazioni = null;

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getListDichiarazioneSegnalazioniConsolidamentoUV method in DichiarazioneSegnalazioneDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListDichiarazioneSegnalazioniConsolidamentoUV method in DichiarazioneSegnalazioneDAO and it values: "+conn+"\n");

      String query = 
        "SELECT DS.ID_DICHIARAZIONE_SEGNALAZIONE, " +
        "       DS.ID_CONTROLLO, " +
        "       DS.DESCRIZIONE, " +
        "       DS.BLOCCANTE, " +
        "       TC.DESCRIZIONE AS DESC_CONTROLLO, " +
        "       TGC.DESCRIZIONE AS DESC_GRUPPO_CONTROLLO " +
        "FROM   DB_DICHIARAZIONE_SEGNALAZIONE DS, " +
        "       DB_TIPO_CONTROLLO TC, " +
        "       DB_TIPO_GRUPPO_CONTROLLO TGC " +
        "WHERE  DS.ID_AZIENDA = ? " +
        "AND    DS.ID_DICHIARAZIONE_CONSISTENZA IS NULL " +
        "AND    DS.ID_CONTROLLO = TC.ID_CONTROLLO " +
        "AND    TC.ID_GRUPPO_CONTROLLO = TGC.ID_GRUPPO_CONTROLLO " +
        "ORDER BY" +
        "       TGC.DESCRIZIONE ASC, " +
        "       TC.ORDINAMENTO ASC ";

      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListDichiarazioneSegnalazioniConsolidamentoUV method in DichiarazioneSegnalazioneDAO: "+idAzienda+"\n");
  
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda.longValue());

      SolmrLogger.debug(this, "Executing getListDichiarazioneSegnalazioniConsolidamentoUV: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        if(elencoDichiarazioniSegnalazioni == null)
        {
          elencoDichiarazioniSegnalazioni = new Vector<DichiarazioneSegnalazioneVO>();
        }
        
        DichiarazioneSegnalazioneVO dichiarazioneSegnalazioneVO = new DichiarazioneSegnalazioneVO();
        dichiarazioneSegnalazioneVO.setIdDichiarazioneSegnalazione(
            new Long(rs.getLong("ID_DICHIARAZIONE_SEGNALAZIONE")));
        
        dichiarazioneSegnalazioneVO.setIdControllo(new Long(rs.getLong("ID_CONTROLLO")));
        CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_CONTROLLO")), rs.getString("DESC_CONTROLLO"));
        dichiarazioneSegnalazioneVO.setControllo(code);
        dichiarazioneSegnalazioneVO.setDescrizione(rs.getString("DESCRIZIONE"));
        dichiarazioneSegnalazioneVO.setBloccante(rs.getString("BLOCCANTE"));
        dichiarazioneSegnalazioneVO.setDescGruppoControllo(rs.getString("DESC_GRUPPO_CONTROLLO"));
        
        elencoDichiarazioniSegnalazioni.add(dichiarazioneSegnalazioneVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "getListDichiarazioneSegnalazioniConsolidamentoUV in DichiarazioneSegnalazioneDAO - SQLException: "+exc.getMessage()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) 
    {
      SolmrLogger.error(this, "getListDichiarazioneSegnalazioniConsolidamentoUV in DichiarazioneSegnalazioneDAO - Generic Exception: "+ex+"\n");
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
        SolmrLogger.error(this, "getListDichiarazioneSegnalazioniConsolidamentoUV in DichiarazioneSegnalazioneDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getListDichiarazioneSegnalazioniConsolidamentoUV in DichiarazioneSegnalazioneDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListDichiarazioneSegnalazioniConsolidamentoUV method in DichiarazioneSegnalazioneDAO\n");
    
    return elencoDichiarazioniSegnalazioni;
  }
	
	
}
