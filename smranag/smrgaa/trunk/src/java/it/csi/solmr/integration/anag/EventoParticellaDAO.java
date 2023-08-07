package it.csi.solmr.integration.anag;
	
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.ParticellaAssVO;
import it.csi.solmr.dto.anag.terreni.EventoParticellaVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

public class EventoParticellaDAO extends it.csi.solmr.integration.BaseDAO {


	public EventoParticellaDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public EventoParticellaDAO(String refName) throws ResourceAccessException {
		super(refName);
	}
	
	/**
	 * Metodo utilizzato per inserire un record nella tabella DB_EVENTO_PARTICELLA
	 * 
	 * @param eventoParticellaVO
	 * @return java.lang.Long
	 * @throws DataAccessException
	 */
	public Long insertEventoParticella(EventoParticellaVO eventoParticellaVO) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating insertEventoParticella method in EventoParticellaDAO\n");
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    Long idEventoParticella = null;

	    try {
	    	idEventoParticella = getNextPrimaryKey(SolmrConstants.SEQ_EVENTO_PARTICELLA);
	    	SolmrLogger.debug(this, "Creating db-connection in insertEventoParticella method in EventoParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in insertEventoParticella method in EventoParticellaDAO and it values: "+conn+"\n");

			String query = " INSERT INTO DB_EVENTO_PARTICELLA "+
	                       "     		 (ID_PARTICELLA_EVENTO, " +
	                       "              ID_EVENTO, " +
	                       "              DATA_AGGIORNAMENTO, " +
	                       "              ID_UTENTE_AGGIORNAMENTO, " +
	                       "              ID_PARTICELLA_NUOVA, " +
	                       "              ID_PARTICELLA_CESSATA) " +
	                       " VALUES       (?, ?, ?, ?, ?, ?) ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing insertEventoParticella: "+query);
			
			stmt.setLong(1, idEventoParticella.longValue());
			SolmrLogger.debug(this, "Value of parameter 1 [ID_PARTICELLA_EVENTO] in method insertEventoParticella in EventoParticellaDAO: "+idEventoParticella.longValue()+"\n");
			stmt.setLong(2, eventoParticellaVO.getIdEvento().longValue());
			SolmrLogger.debug(this, "Value of parameter 2 [ID_EVENTO] in method insertEventoParticella in EventoParticellaDAO: "+eventoParticellaVO.getIdEvento()+"\n");
			stmt.setTimestamp(3, new Timestamp(eventoParticellaVO.getDataAggiornamento().getTime()));
			SolmrLogger.debug(this, "Value of parameter 3 [DATA_AGGIORNAMENTO] in method insertEventoParticella in EventoParticellaDAO: "+new Timestamp(eventoParticellaVO.getDataAggiornamento().getTime())+"\n");
			stmt.setLong(4, eventoParticellaVO.getIdUtenteAggiornamento().longValue());
			SolmrLogger.debug(this, "Value of parameter 4 [ID_UTENTE_AGGIORNAMENTO] in method insertEventoParticella in EventoParticellaDAO: "+eventoParticellaVO.getIdUtenteAggiornamento().longValue()+"\n");
			stmt.setLong(5, eventoParticellaVO.getIdParticellaNuova().longValue());
			SolmrLogger.debug(this, "Value of parameter 5 [ID_PARTICELLA_NUOVA] in method insertEventoParticella in EventoParticellaDAO: "+eventoParticellaVO.getIdParticellaNuova().longValue()+"\n");
			stmt.setLong(6, eventoParticellaVO.getIdParticellaCessata().longValue());
			SolmrLogger.debug(this, "Value of parameter 6 [ID_PARTICELLA_CESSATA] in method insertEventoParticella in EventoParticellaDAO: "+eventoParticellaVO.getIdParticellaCessata().longValue()+"\n");
			
			stmt.executeUpdate();

			stmt.close();
	    }
	    catch(SQLException exc) {
	    	SolmrLogger.error(this, "insertEventoParticella in EventoParticellaDAO - SQLException: "+exc.getMessage());
	    	throw new DataAccessException(exc.getMessage());
	    }
	    catch (Exception ex) {
	    	SolmrLogger.error(this, "insertEventoParticella in EventoParticellaDAO - Generic Exception: "+ex);
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
	    		SolmrLogger.error(this, "insertEventoParticella in EventoParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage());
	    		throw new DataAccessException(exc.getMessage());
	    	}
	    	catch(Exception ex) {
	    		SolmrLogger.error(this, "insertEventoParticella in EventoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage());
	    		throw new DataAccessException(ex.getMessage());
	    	}
	    }
	    SolmrLogger.debug(this, "Invocated insertEventoParticella method in EventoParticellaDAO\n");
	    return idEventoParticella;
	}
	
	/**
	 * Metodo che mi permette di estrarre tutti gli eventi relativi ad una particella
	 * 
	 * @param idParticella
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.EventoParticellaVO[]
	 * @throws DataAccessException
	 */
	public EventoParticellaVO[] getEventiParticellaByIdParticellaNuovaOrCessata(Long idParticella, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getEventiParticellaByIdParticellaNuovaOrCessata method in EventoParticellaDAO\n");
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    Vector<EventoParticellaVO> elencoEventoParticella = new Vector<EventoParticellaVO>();

	    try {
	    	SolmrLogger.debug(this, "Creating db-connection in getEventiParticellaByIdParticellaNuovaOrCessata method in EventoParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getEventiParticellaByIdParticellaNuovaOrCessata method in EventoParticellaDAO and it values: "+conn+"\n");

			String query = " SELECT EP.ID_PARTICELLA_EVENTO, " +
						   "        EP.ID_EVENTO, " +
						   "        TE.DESCRIZIONE, " +
						   "        EP.DATA_AGGIORNAMENTO, " +
						   "        EP.ID_UTENTE_AGGIORNAMENTO, " + 
						   "        EP.ID_PARTICELLA_NUOVA, " + 
						   "        EP.ID_PARTICELLA_CESSATA, " + 
						   " DECODE (?, ID_PARTICELLA_NUOVA, C2.DESCOM || '(' || P2.SIGLA_PROVINCIA || ') ' || DECODE(SP2.SEZIONE, NULL, '', ' Sz.:' || SP2.SEZIONE) || ' Fgl.:' || SP2.FOGLIO || ' Part.:' || SP2.PARTICELLA|| DECODE(SP2.SUBALTERNO, NULL, '', 'Sub.:' || SP2.SUBALTERNO), " +
                           "                                 C1.DESCOM || '(' || P1.SIGLA_PROVINCIA || ') ' || DECODE(SP1.SEZIONE, NULL, '', ' Sz.:'|| SP1.SEZIONE) || ' Fgl.:' || SP1.FOGLIO || ' Part.:' || SP1.PARTICELLA|| DECODE(SP1.SUBALTERNO, NULL, '', 'Sub.:' || SP1.SUBALTERNO)) PART_ORIGINE_EVENTO " +
                           " FROM   DB_EVENTO_PARTICELLA EP, " +
                           "        DB_TIPO_EVENTO TE, " +
                           "        DB_STORICO_PARTICELLA SP1, " +
                           "        DB_STORICO_PARTICELLA SP2, " +
                           "        COMUNE C1, " +
                           "        PROVINCIA P1, " +
                           "        COMUNE C2, " +
                           "        PROVINCIA P2 " +
                           " WHERE (ID_PARTICELLA_NUOVA = ? OR ID_PARTICELLA_CESSATA = ?) " +
                           " AND    TE.ID_EVENTO = EP.ID_EVENTO " +
                           " AND    SP1.ID_PARTICELLA = ID_PARTICELLA_NUOVA " +
                           " AND    SP1.DATA_FINE_VALIDITA IS NULL " +
                           " AND    SP1.COMUNE = C1.ISTAT_COMUNE " +
                           " AND    C1.ISTAT_PROVINCIA = P1.ISTAT_PROVINCIA " +
                           " AND    SP2.ID_PARTICELLA = ID_PARTICELLA_CESSATA " +
                           " AND    SP2.DATA_FINE_VALIDITA IS NULL " +
                           " AND    SP2.COMUNE = C2.ISTAT_COMUNE " +
                           " AND    C2.ISTAT_PROVINCIA = P2.ISTAT_PROVINCIA " ;

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
			
			SolmrLogger.debug(this, "Value of parameter 1 [ID_PARTICELLA] in getEventiParticellaByIdParticellaNuovaOrCessata method in EventoParticellaDAO: "+idParticella+"\n");
			
			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing query: "+query);

			stmt.setLong(1, idParticella.longValue());
			stmt.setLong(2, idParticella.longValue());
			stmt.setLong(3, idParticella.longValue());

			ResultSet rs = stmt.executeQuery();

	        while(rs.next()) {
	        	EventoParticellaVO eventoParticellaVO = new EventoParticellaVO();
	        	eventoParticellaVO.setIdParticellaEvento(new Long(rs.getLong("ID_PARTICELLA_EVENTO")));
	        	eventoParticellaVO.setIdEvento(new Long(rs.getLong("ID_EVENTO")));
	        	eventoParticellaVO.setTipoEvento(new CodeDescription(new Integer(rs.getInt("ID_EVENTO")), rs.getString("DESCRIZIONE")));
	        	eventoParticellaVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
	        	eventoParticellaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
	        	eventoParticellaVO.setIdParticellaNuova(new Long(rs.getLong("ID_PARTICELLA_NUOVA")));
	        	eventoParticellaVO.setIdParticellaCessata(new Long(rs.getLong("ID_PARTICELLA_CESSATA")));
	        	eventoParticellaVO.setDescParticellaOrigineEvento(rs.getString("PART_ORIGINE_EVENTO"));
	        	elencoEventoParticella.add(eventoParticellaVO);
	        }
	        
	        rs.close();
	        stmt.close();
	    }
	    catch(SQLException exc) {
			SolmrLogger.error(this, "getEventiParticellaByIdParticellaNuovaOrCessata in EventoParticellaDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getEventiParticellaByIdParticellaNuovaOrCessata in EventoParticellaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getEventiParticellaByIdParticellaNuovaOrCessata in EventoParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getEventiParticellaByIdParticellaNuovaOrCessata in EventoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getEventiParticellaByIdParticellaNuovaOrCessata method in EventoParticellaDAO\n");
		if(elencoEventoParticella.size() == 0) {
			return (EventoParticellaVO[])elencoEventoParticella.toArray(new EventoParticellaVO[0]);
		}
		else {
			return (EventoParticellaVO[])elencoEventoParticella.toArray(new EventoParticellaVO[elencoEventoParticella.size()]);
		}
	}
	
	/**
	 * Metodo che controlla se esiste un evento associato alla
	 * particella che sto inserendo e a quella che sto per cessare
	 * uguale a quello passato
	 * @param idParticellaNuova
	 * @param idParticellaCessata
	 * @param idEvento
	 * @throws DataAccessException
	 */
	public String getEventiParticellaByIdParticellaNuovaAndCessata(Long idParticellaNuova, Long idParticellaCessata, long idEvento) 
	  throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating getEventiParticellaByIdParticellaNuovaAndCessata method in EventoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    String message=null;

    try 
    {
    	SolmrLogger.debug(this, "Creating db-connection in getEventiParticellaByIdParticellaNuovaAndCessata method in EventoParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getEventiParticellaByIdParticellaNuovaAndCessata method in EventoParticellaDAO and it values: "+conn+"\n");

			String query = " SELECT * " + 
                     " FROM   DB_EVENTO_PARTICELLA " +
                     " WHERE  ID_PARTICELLA_NUOVA = ? " +
                     " AND    ID_PARTICELLA_CESSATA = ? "+
                     " AND    ID_EVENTO = ? ";

			
			SolmrLogger.debug(this, "Value of parameter 1 [ID_PARTICELLA_NUOVA] in getEventiParticellaByIdParticellaNuovaAndCessata method in EventoParticellaDAO: "+idParticellaNuova+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ID_PARTICELLA_CESSATA] in getEventiParticellaByIdParticellaNuovaAndCessata method in EventoParticellaDAO: "+idParticellaCessata+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ID_EVENTO] in getEventiParticellaByIdParticellaNuovaAndCessata method in EventoParticellaDAO: "+idEvento+"\n");
			
			
			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing query: "+query);

			stmt.setLong(1, idParticellaNuova.longValue());
			stmt.setLong(2, idParticellaCessata.longValue());
			stmt.setLong(3, idEvento);

			ResultSet rs = stmt.executeQuery();

      if(rs.next()) message="Lo stesso evento è già stato inserito per le particelle considerate";
        
      rs.close();
      stmt.close();
      return message;
    }
    catch(SQLException exc) 
    {
  		SolmrLogger.error(this, "getEventiParticellaByIdParticellaNuovaAndCessata in EventoParticellaDAO - SQLException: "+exc+"\n");
  		throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) 
		{
			SolmrLogger.error(this, "getEventiParticellaByIdParticellaNuovaAndCessata in EventoParticellaDAO - Generic Exception: "+ex+"\n");
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
				SolmrLogger.error(this, "getEventiParticellaByIdParticellaNuovaAndCessata in EventoParticellaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) 
			{
				SolmrLogger.error(this, "getEventiParticellaByIdParticellaNuovaAndCessata in EventoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
	}
	
	
	/**
   * Metodo che mi permette di Visualizzare i dati delle particelle legate alla particella selezionata (“Particella Scelta”), 
   * Cioè estrarre tutti i record di db_evento_particella per cui db_evento_particella.id_particella_cessata= id_particella selezionata 
   * e tramite id_particella_nuova accedere a db_storico_particella per recuperare i dati catastali (data_fine_validita null)
   * 
   * @param idParticella
   * @return it.csi.solmr.dto.anag.ParticellaAssVO[]
   * @throws DataAccessException
   */
  public ParticellaAssVO[] getParticellaForDocAzCessata(Long idParticella) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getParticellaForDocAzCessata method in EventoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaAssVO> elencoParticelle = new Vector<ParticellaAssVO>();

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getParticellaForDocAzCessata method in EventoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getParticellaForDocAzCessata method in EventoParticellaDAO and it values: "+conn+"\n");

      String query = "select sp.id_particella || sp.id_storico_particella || ep.id_evento as primarykey,ep.ID_PARTICELLA_EVENTO, " +
      		           "sp.id_particella,c.descom,p.sigla_provincia, sp.sezione, sp.foglio, sp.particella, sp.subalterno, "+
                     "sp.sup_catastale,te.descrizione as desc_evento, sp.id_storico_particella,ep.id_evento "+
                     "from db_evento_particella ep, db_storico_particella sp, comune c,provincia p, db_tipo_evento te "+
                     "where ep.id_particella_cessata=? "+
                     "and sp.id_particella= ep.id_particella_nuova "+
                     "and sp.comune=c.istat_comune "+
                     "and c.istat_provincia=p.istat_provincia "+
                     "and ep.id_evento= te.id_evento "+
                     "and sp.data_fine_validita is null " +
                     "order by 2,3,4,5,6,7,8 ";

      SolmrLogger.debug(this, "Value of parameter 1 [ID_PARTICELLA] in getParticellaForDocAzCessata method in EventoParticellaDAO: "+idParticella+"\n");
      
      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: "+query);

      stmt.setLong(1, idParticella.longValue());

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        ParticellaAssVO particellaVO = new ParticellaAssVO();
        
        particellaVO.setPrimaryKey(rs.getString("primarykey"));
        particellaVO.setIdParticella(checkLongNull(rs.getString("id_particella")));
        particellaVO.setIdStoricoParticella(checkLongNull(rs.getString("id_storico_particella")));
        particellaVO.setDescComuneParticella(rs.getString("descom"));
        particellaVO.setSiglaProvinciaParticella(rs.getString("sigla_provincia"));
        particellaVO.setSezione(rs.getString("sezione"));
        particellaVO.setFoglio(checkLongNull(rs.getString("foglio")));
        particellaVO.setParticella(checkLongNull(rs.getString("particella")));
        particellaVO.setSubalterno(rs.getString("subalterno"));
        particellaVO.setIdEvento(checkLongNull(rs.getString("id_evento")));
        particellaVO.setIdParticellaEvento(checkLongNull(rs.getString("ID_PARTICELLA_EVENTO")));
        particellaVO.setDescrizioneEvento(rs.getString("desc_evento"));
        particellaVO.setSupCatastale(StringUtils.parseSuperficieField(rs.getString("SUP_CATASTALE")));
        particellaVO.setSupCatastaleB(rs.getBigDecimal("SUP_CATASTALE"));
        elencoParticelle.add(particellaVO);
      }
      rs.close();
      stmt.close();
    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "getParticellaForDocAzCessata in EventoParticellaDAO - SQLException: "+exc.getMessage()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) 
    {
      SolmrLogger.error(this, "getParticellaForDocAzCessata in EventoParticellaDAO - Generic Exception: "+ex+"\n");
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
        SolmrLogger.error(this, "getParticellaForDocAzCessata in EventoParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) 
      {
        SolmrLogger.error(this, "getParticellaForDocAzCessata in EventoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getParticellaForDocAzCessata method in EventoParticellaDAO\n");
    if(elencoParticelle.size() == 0) 
      return (ParticellaAssVO[])elencoParticelle.toArray(new ParticellaAssVO[0]);
    else
      return (ParticellaAssVO[])elencoParticelle.toArray(new ParticellaAssVO[elencoParticelle.size()]);
  }
  
  
  /**
   * Metodo utilizzato per inserire un record nella tabella db_documento_corr_particella
   * 
   * @param ParticellaAssVO
   * @return java.lang.Long
   * @throws DataAccessException
   */
  public Long insertDocCorParticella(ParticellaAssVO particellaAssVO, Long idDocumento) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating insertDocCorParticella method in EventoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Long pKey = null;

    try 
    {
      pKey = getNextPrimaryKey(SolmrConstants.SEQ_DOC_COR_PARTICELLA);
      SolmrLogger.debug(this, "Creating db-connection in insertDocCorParticella method in EventoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in insertDocCorParticella method in EventoParticellaDAO and it values: "+conn+"\n");
  
      String query = " INSERT INTO db_documento_corr_particella "+
                         "         (    ID_DOCUMENTO_CORR_PARTICELLA, " +
                         "              ID_DOCUMENTO, " +
                         "              ID_STORICO_PARTICELLA, " +
                         "              ID_PARTICELLA_EVENTO, " +
                         "              ID_TITOLO_POSSESSO, " +
                         "              ID_UTILIZZO, " +
                         "              ID_VARIETA, " +
                         "              SUPERFICIE_CONDOTTA, " +
                         "              SUPERFICIE_UTILIZZATA, " +
                         "              DATA_INSERIMENTO, " +
                         "              DATA_INIZIO_VALIDITA, " +
                         "              DATA_FINE_VALIDITA) " +
                         " VALUES       (?,?,?,?,?,?,?,?,?,SYSDATE,SYSDATE,NULL) ";                                                                                                                                                                                

      
  
      stmt = conn.prepareStatement(query);
  
      SolmrLogger.debug(this, "Executing insertDocCorParticella: "+query);
      
      int indice=0;
      
      stmt.setLong(++indice, pKey.longValue());
      SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_DOCUMENTO_CORR_PARTICELLA] in method insertDocCorParticella in EventoParticellaDAO: "+pKey.longValue()+"\n");
      stmt.setLong(++indice, idDocumento.longValue());
      SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_DOCUMENTO] in method insertDocCorParticella in EventoParticellaDAO: "+idDocumento.longValue()+"\n");      
      stmt.setLong(++indice, particellaAssVO.getIdStoricoParticella().longValue());
      SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_STORICO_PARTICELLA] in method insertDocCorParticella in EventoParticellaDAO: "+particellaAssVO.getIdStoricoParticella().longValue()+"\n");
      stmt.setLong(++indice, particellaAssVO.getIdParticellaEvento().longValue());
      SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_PARTICELLA_EVENTO] in method insertDocCorParticella in EventoParticellaDAO: "+particellaAssVO.getIdParticellaEvento().longValue()+"\n");
      stmt.setLong(++indice, Long.parseLong(particellaAssVO.getIdConduzione()));
      SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_TITOLO_POSSESSO] in method insertDocCorParticella in EventoParticellaDAO: "+particellaAssVO.getIdConduzione()+"\n");
      stmt.setLong(++indice, Long.parseLong(particellaAssVO.getIdUtilizzo()));
      SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_UTILIZZO] in method insertDocCorParticella in EventoParticellaDAO: "+particellaAssVO.getIdUtilizzo()+"\n");
      stmt.setLong(++indice, Long.parseLong(particellaAssVO.getIdVarieta()));
      SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_VARIETA] in method insertDocCorParticella in EventoParticellaDAO: "+particellaAssVO.getIdVarieta()+"\n");
      stmt.setBigDecimal(++indice, particellaAssVO.getSupCondottaB());
      SolmrLogger.debug(this, "Value of parameter "+indice+" [SUPERFICIE_CONDOTTA] in method insertDocCorParticella in EventoParticellaDAO: "+particellaAssVO.getSupCondottaB()+"\n");
      stmt.setBigDecimal(++indice, particellaAssVO.getSupUtilizzataB());
      SolmrLogger.debug(this, "Value of parameter "+indice+" [SUPERFICIE_UTILIZZATA] in method insertDocCorParticella in EventoParticellaDAO: "+particellaAssVO.getSupUtilizzataB()+"\n");
      
      stmt.executeUpdate();
  
      stmt.close();
    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "insertDocCorParticella in EventoParticellaDAO - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) 
    {
      SolmrLogger.error(this, "insertDocCorParticella in EventoParticellaDAO - Generic Exception: "+ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null)  stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) 
      {
        SolmrLogger.error(this, "insertDocCorParticella in EventoParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) 
      {
        SolmrLogger.error(this, "insertDocCorParticella in EventoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated insertDocCorParticella method in EventoParticellaDAO\n");
    return pKey;
  }
  
  /**
   * Metodo che mi permette di estrarre tutti le particelle legate ad un documento di correzione terreni
   * utilizzato dalle aziende cessate
   * 
   * @param idDocumento
   * @return Vector
   * @throws DataAccessException
   */
  public Vector<ParticellaAssVO> getParticelleDocCor(Long idDocumento) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getParticelleDocCor method in EventoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaAssVO> elencoParticelleDocCor = new Vector<ParticellaAssVO>();

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getParticelleDocCor method in EventoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getParticelleDocCor method in EventoParticellaDAO and it values: "+conn+"\n");
  
      String query = "" +
      	"SELECT CP.ID_TITOLO_POSSESSO, " +
      	"       CP.ID_UTILIZZO, " +
      	"       CP.ID_VARIETA, " +
      	"       CP.ID_STORICO_PARTICELLA, " +
      	"       CP.ID_PARTICELLA_EVENTO, "+
        "       CP.SUPERFICIE_CONDOTTA, " +
        "       CP.SUPERFICIE_UTILIZZATA, " +
        "       C.DESCOM, " +
        "       P.SIGLA_PROVINCIA, " +
        "       SP.SEZIONE, " +
        "       SP.FOGLIO, " +
        "       SP.PARTICELLA, "+
        "       SP.SUBALTERNO, " +
        "       SP.SUP_CATASTALE, " +
        "       EP.ID_EVENTO, " +
        "       TE.DESCRIZIONE AS DESCRIZIONE_EVENTO, " +
        "       SP.ID_CASO_PARTICOLARE, "+
        "       '[' || TU.CODICE || '] ' || NVL(TU.DESCRIZIONE,'-') || ' - Var. [' || TV.CODICE_VARIETA || '] ' || NVL(TV.DESCRIZIONE,'-') AS DESC_USO_PRIMARIO "+
        "FROM   DB_DOCUMENTO_CORR_PARTICELLA CP, " +
        "       COMUNE C, " +
        "       DB_STORICO_PARTICELLA SP, " +
        "       PROVINCIA P, " +
        "       DB_EVENTO_PARTICELLA EP, "+
        "       DB_TIPO_EVENTO TE," +
        "       DB_TIPO_UTILIZZO TU, " +
        "       DB_TIPO_VARIETA TV "+
        "WHERE  CP.ID_DOCUMENTO = ? "+
        "AND    CP.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA "+
        "AND    SP.COMUNE = C.ISTAT_COMUNE "+
        "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "+
        "AND    CP.ID_PARTICELLA_EVENTO = EP.ID_PARTICELLA_EVENTO "+
        "AND    EP.ID_EVENTO = TE.ID_EVENTO "+
        "AND    TU.ID_UTILIZZO = CP.ID_UTILIZZO "+
        "AND    TV.ID_VARIETA = CP.ID_VARIETA "+
        "ORDER BY C.DESCOM," +
        "         SP.SEZIONE, " +
        "         SP.FOGLIO, " +
        "         SP.PARTICELLA, " +
        "         SP.SUBALTERNO, " +
        "         CP.ID_TITOLO_POSSESSO" ;
        
      SolmrLogger.debug(this, "Value of parameter 1 [ID_DOCUMENTO] in getParticelleDocCor method in EventoParticellaDAO: "+idDocumento+"\n");
      
      stmt = conn.prepareStatement(query);
  
      SolmrLogger.debug(this, "Executing query: "+query);
  
      stmt.setLong(1, idDocumento.longValue());
  
      ResultSet rs = stmt.executeQuery();
  
      while(rs.next()) 
      {
        ParticellaAssVO particellaAssVO=new ParticellaAssVO();
        
        particellaAssVO.setIdConduzione(rs.getString("ID_TITOLO_POSSESSO"));
        particellaAssVO.setIdUtilizzo(rs.getString("ID_UTILIZZO"));
        particellaAssVO.setIdVarieta(rs.getString("ID_VARIETA"));
        particellaAssVO.setIdConduzione(rs.getString("ID_TITOLO_POSSESSO"));        
        particellaAssVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
        particellaAssVO.setIdEvento(new Long(rs.getLong("ID_EVENTO")));
        particellaAssVO.setIdParticellaEvento(new Long(rs.getLong("ID_PARTICELLA_EVENTO")));
        particellaAssVO.setSupCondotta(StringUtils.parseSuperficieField(rs.getString("SUPERFICIE_CONDOTTA")));
        particellaAssVO.setSupCondottaB(rs.getBigDecimal("SUPERFICIE_CONDOTTA"));
        particellaAssVO.setSupUtilizzata(StringUtils.parseSuperficieField(rs.getString("SUPERFICIE_UTILIZZATA")));
        particellaAssVO.setSupUtilizzataB(rs.getBigDecimal("SUPERFICIE_UTILIZZATA"));
        particellaAssVO.setSupCatastale(StringUtils.parseSuperficieField(rs.getString("SUP_CATASTALE")));
        particellaAssVO.setSupCatastaleB(rs.getBigDecimal("SUP_CATASTALE"));
        particellaAssVO.setDescComuneParticella(rs.getString("DESCOM"));
        particellaAssVO.setSiglaProvinciaParticella(rs.getString("SIGLA_PROVINCIA"));
        particellaAssVO.setDescrizioneEvento(rs.getString("DESCRIZIONE_EVENTO"));
        particellaAssVO.setSezione(rs.getString("SEZIONE"));
        particellaAssVO.setFoglio(checkLongNull(rs.getString("foglio")));
        particellaAssVO.setParticella(checkLongNull(rs.getString("particella")));
        particellaAssVO.setSubalterno(rs.getString("SUBALTERNO"));
        particellaAssVO.setDescUsoPrimario(rs.getString("DESC_USO_PRIMARIO"));
        particellaAssVO.setIdCasoParticolare(checkLongNull(rs.getString("ID_CASO_PARTICOLARE")));
        
        
        elencoParticelleDocCor.add(particellaAssVO);
        
      }
          
      rs.close();
      stmt.close();
      SolmrLogger.debug(this, "Invocated getParticelleDocCor method in EventoParticellaDAO\n");
      return elencoParticelleDocCor;  
    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "getParticelleDocCor in EventoParticellaDAO - SQLException: "+exc.getMessage()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) 
    {
      SolmrLogger.error(this, "getParticelleDocCor in EventoParticellaDAO - Generic Exception: "+ex+"\n");
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
        SolmrLogger.error(this, "getParticelleDocCor in EventoParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) 
      {
        SolmrLogger.error(this, "getParticelleDocCor in EventoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
  }
}
