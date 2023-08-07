package it.csi.solmr.integration.anag;
	
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.dto.terreni.IstanzaRiesameVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoAreaVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoDestinazioneVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoDettaglioUsoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoFaseAllevamentoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoPeriodoSeminaVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoPraticaMantenimentoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoQualitaUsoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoSeminaVO;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.UtenteIrideVO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.AnagParticellaExcelVO;
import it.csi.solmr.dto.anag.DocumentoConduzioneVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.FoglioVO;
import it.csi.solmr.dto.anag.ParticellaCertElegVO;
import it.csi.solmr.dto.anag.ParticellaCertificataVO;
import it.csi.solmr.dto.anag.ParticellaVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneDichiarataVO;
import it.csi.solmr.dto.anag.terreni.FiltriParticellareRicercaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.dto.anag.terreni.TipoMacroUsoVO;
import it.csi.solmr.dto.anag.terreni.TipoUtilizzoVO;
import it.csi.solmr.dto.anag.terreni.TipoVarietaVO;
import it.csi.solmr.dto.anag.terreni.UnitaArboreaDichiarataVO;
import it.csi.solmr.dto.anag.terreni.UtilizzoDichiaratoVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

public class ConduzioneDichiarataDAO extends it.csi.solmr.integration.BaseDAO {


	public ConduzioneDichiarataDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public ConduzioneDichiarataDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo che mi restituisce il record su DB_CONDUZIONE_DICHIARATA a partire
	 * dalla chiave primaria
	 * 
	 * @param idConduzioneDichiarata
	 * @return it.csi.solmr.dto.anag.terreni.ConduzioneDichiarataVO
	 * @throws DataAccessException
	 */
	public ConduzioneDichiarataVO findConduzioneDichiarataByPrimaryKey(Long idConduzioneDichiarata) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating findConduzioneDichiarataByPrimaryKey method in ConduzioneDichiarataDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		ConduzioneDichiarataVO conduzioneDichiarataVO = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in findConduzioneDichiarataByPrimaryKey method in ConduzioneDichiarataDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in findConduzioneDichiarataByPrimaryKey method in ConduzioneDichiarata and it values: "+conn+"\n");

			String query = " SELECT CD.ID_CONDUZIONE_DICHIARATA, " +
						   "		CD.CODICE_FOTOGRAFIA_TERRENI, " +
						   "        CD.ID_UTE, " +
						   "        CD.ID_TITOLO_POSSESSO, " +
						   "        TTP.DESCRIZIONE, " +
						   "        CD.SUPERFICIE_CONDOTTA, " +
						   "        CD.NOTE, " +
						   "        CD.DATA_INIZIO_CONDUZIONE, " +
						   "        CD.DATA_FINE_CONDUZIONE, " +
						   "        CD.DATA_AGGIORNAMENTO, " +
						   "        CD.ID_UTENTE_AGGIORNAMENTO, " +
						   "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
					       "          || ' ' " + 
					       "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
					       "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
					       "         WHERE CD.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
						   "        AS DENOMINAZIONE, " +
						   "        (SELECT PVU.DENOMINAZIONE " +
						   "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
						   "        WHERE CD.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
						   "        AS DESCRIZIONE_ENTE_APPARTENENZA, " +
						   "        CD.ID_PARTICELLA, " +
						   "        CD.ID_CONDUZIONE_PARTICELLA, " +
						   "        CD.ID_STORICO_PARTICELLA, " +
						   "        CD.ESITO_CONTROLLO, " +
						   "        CD.DATA_ESECUZIONE, " +
						   "        CD.SUPERFICIE_AGRONOMICA, " +
						   "        CD.PERCENTUALE_POSSESSO " +
						   " FROM   DB_CONDUZIONE_DICHIARATA CD, " +
						   "        DB_TIPO_TITOLO_POSSESSO TTP " +
						   //"        PAPUA_V_UTENTE_LOGIN PVU " +
						   " WHERE  CD.ID_CONDUZIONE_DICHIARATA = ? " +
						   " AND    CD.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO ";
						   //" AND    CD.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN ";

			SolmrLogger.debug(this, "Value of parameter 1 [ID_CONDUZIONE_DICHIARATA] in findConduzioneDichiarataByPrimaryKey method in ConduzioneDichiarataDAO: "+idConduzioneDichiarata+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idConduzioneDichiarata.longValue());

			SolmrLogger.debug(this, "Executing findConduzioneDichiarataByPrimaryKey: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) {
				conduzioneDichiarataVO = new ConduzioneDichiarataVO();
				conduzioneDichiarataVO.setIdConduzioneDichiarata(new Long(rs.getLong("ID_CONDUZIONE_DICHIARATA")));
				conduzioneDichiarataVO.setCodiceFotografiaTerreni(rs.getString("CODICE_FOTOGRAFIA_TERRENI"));
				if(Validator.isNotEmpty(rs.getString("ID_UTE"))) {
					conduzioneDichiarataVO.setIdUte(new Long(rs.getLong("ID_UTE")));
				}
				conduzioneDichiarataVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
				CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_TITOLO_POSSESSO")), rs.getString("DESCRIZIONE"));
				conduzioneDichiarataVO.setTitoloPossesso(code);
				conduzioneDichiarataVO.setSuperficieCondotta(rs.getString("SUPERFICIE_CONDOTTA"));
				conduzioneDichiarataVO.setNote(rs.getString("NOTE"));
				conduzioneDichiarataVO.setDataInizioConduzione(rs.getDate("DATA_INIZIO_CONDUZIONE"));
				conduzioneDichiarataVO.setDataFineConduzione(rs.getDate("DATA_FINE_CONDUZIONE"));
				conduzioneDichiarataVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
				conduzioneDichiarataVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				UtenteIrideVO utenteIrideVO = new UtenteIrideVO();
				utenteIrideVO.setDenominazione(rs.getString("DENOMINAZIONE"));
				utenteIrideVO.setDescrizioneEnteAppartenenza(rs.getString("DESCRIZIONE_ENTE_APPARTENENZA"));
				conduzioneDichiarataVO.setUtenteAggiornamento(utenteIrideVO);
				conduzioneDichiarataVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
				conduzioneDichiarataVO.setIdConduzioneParticella(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));
				conduzioneDichiarataVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
				conduzioneDichiarataVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
				conduzioneDichiarataVO.setDataEsecuzione(rs.getDate("DATA_ESECUZIONE"));
				conduzioneDichiarataVO.setSuperficieAgronomica(rs.getString("SUPERFICIE_AGRONOMICA"));
				conduzioneDichiarataVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "findConduzioneDichiarataByPrimaryKey in ConduzioneDichiarataDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "findConduzioneDichiarataByPrimaryKey in ConduzioneDichiarataDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "findConduzioneDichiarataByPrimaryKey in ConduzioneDichiarataDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "findConduzioneDichiarataByPrimaryKey in ConduzioneDichiarataDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated findConduzioneDichiarataByPrimaryKey method in ConduzioneDichiarataDAO\n");
		return conduzioneDichiarataVO;
	}
	
	
	/**
	 * 
	 * Restituisce la sommatoria di:
	 * considerare l’ultima dichiarazione di consistenza  
	 * (escluse quelle dichiarazione per avversità "siccità 2006" ovvero id_motivo_dichiarazione=7)
	 * legata alla particella corrente di ciascuna azienda diversa dal CUAA in esame 
	 * escludendo le conduzioni in conferimento (id_titolo_possesso=6) 
	 * ed effettuare la sommatoria della superficie
	 * -	db_conduzione_dichiarata.superficie_condotta se id_titolo_possesso=5 (conduzione=asservimento)   
	 * -	db_conduzione_dichiarata.superficie_agronomica se id_titolo_possesso<>5 (conduzione<>asservimento)
	 * @param idParticella
	 * @param idAzienda
	 * @param flagEscludiAzienda se a true escludo l'azienda passata come secondo parametro
	 * @return
	 * @throws DataAccessException
	 */
	public BigDecimal getSumSuperficieFromParticellaAndLastDichCons(long idParticella, Long idAzienda, boolean flagEscludiAzienda) 
	  throws DataAccessException 
	{
    SolmrLogger.debug(this, "Invocating getSumSuperficieFromParticellaAndLastDichCons method in ConduzioneDichiarataDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    BigDecimal sumSup = null;

    try {
      SolmrLogger.debug(this, "Creating db-connection in getSumSuperficieFromParticellaAndLastDichCons method in ConduzioneDichiarataDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getSumSuperficieFromParticellaAndLastDichCons method in ConduzioneDichiarata and it values: "+conn+"\n");

      String query = " "
      		+ " SELECT "
      		+ "       SUM(DECODE(CD.ID_TITOLO_POSSESSO,5, NVL(CD.SUPERFICIE_CONDOTTA,0), NVL(CD.SUPERFICIE_AGRONOMICA,0))) SUPERFICIE " 
          + " FROM  DB_CONDUZIONE_DICHIARATA CD, DB_DICHIARAZIONE_CONSISTENZA DC, " 
          +	"       DB_ANAGRAFICA_AZIENDA AA "
          + " WHERE  AA.ID_AZIENDA = DC.ID_AZIENDA "
          + " AND    CD.CODICE_FOTOGRAFIA_TERRENI = DC.CODICE_FOTOGRAFIA_TERRENI "
          + " AND    CD.ID_PARTICELLA = ? "    
          + " AND    AA.DATA_FINE_VALIDITA IS NULL "
          + " AND    AA.DATA_CESSAZIONE IS NULL "
          + " AND    CD.ID_TITOLO_POSSESSO<>6 ";
      if(flagEscludiAzienda)
      {
          query += " AND    AA.ID_AZIENDA <> ? ";
      }
      query += " AND    DC.ID_MOTIVO_DICHIARAZIONE <> 7 "
          + " AND    DC.DATA_INSERIMENTO_DICHIARAZIONE = " 
          + "        (SELECT MAX(DC1.DATA_INSERIMENTO_DICHIARAZIONE) " 
          + "        FROM   DB_DICHIARAZIONE_CONSISTENZA DC1, DB_TIPO_MOTIVO_DICHIARAZIONE D " 
          + "        WHERE  DC1.ID_AZIENDA = DC.ID_AZIENDA "
          + "        AND D.ID_MOTIVO_DICHIARAZIONE=DC1.ID_MOTIVO_DICHIARAZIONE "
          + "        AND D.TIPO_DICHIARAZIONE<>'C' "
          + " AND    DC1.ID_MOTIVO_DICHIARAZIONE <> 7) "
          + " GROUP BY CD.ID_PARTICELLA ";

      SolmrLogger.debug(this, "Value of parameter 1 [ID_PARTICELLA] in getSumSuperficieFromParticellaAndLastDichCons method in ConduzioneDichiarataDAO: "+idParticella+"\n");
      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getSumSuperficieFromParticellaAndLastDichCons method in ConduzioneDichiarataDAO: "+idAzienda+"\n");
      
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idParticella);
      if(flagEscludiAzienda)
      {
        stmt.setLong(2, idAzienda.longValue());
      }

      SolmrLogger.debug(this, "Executing getSumSuperficieFromParticellaAndLastDichCons: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      if(rs.next()) {
        sumSup = rs.getBigDecimal("SUPERFICIE");
        
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getSumSuperficieFromParticellaAndLastDichCons in ConduzioneDichiarataDAO - SQLException: "+exc.getMessage()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getSumSuperficieFromParticellaAndLastDichCons in ConduzioneDichiarataDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getSumSuperficieFromParticellaAndLastDichCons in ConduzioneDichiarataDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getSumSuperficieFromParticellaAndLastDichCons in ConduzioneDichiarataDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getSumSuperficieFromParticellaAndLastDichCons method in ConduzioneDichiarataDAO\n");
    return sumSup;
  }
	
	/**
	 * Per ogni conduzione dichiarata (db_conduzione_dichiarata .id_conduzione_dichiarata) 
	 * accedere a db_utilizzo_dichiarato, estrarre tutti gli utilizzi 
	 * per cui db_tipo_utilizzo.flag_uso_agronomico=’S’ 
	 * (superficie spandibile, cioè è previsto utilizzo agronomico) 
	 * ed effettuare la sommatoria del campo superficie_utilizzata.
	 * @param idConduzioneDichiarata
	 * @return
	 * @throws DataAccessException
	 */
	public BigDecimal getSumSuperficieUtilizzoUsoAgronomico(long idConduzioneDichiarata) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getSumSuperficieUtilizzoUsoAgronomico method in ConduzioneDichiarataDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    BigDecimal sumSup = null;
  
    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getSumSuperficieUtilizzoUsoAgronomico method in ConduzioneDichiarataDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getSumSuperficieUtilizzoUsoAgronomico method in ConduzioneDichiarata and it values: "+conn+"\n");
  
      String query = " "
          + " SELECT "
          + "        SUM(UD.SUPERFICIE_UTILIZZATA) AS SUPERFICIE "
          + " FROM   DB_CONDUZIONE_DICHIARATA CD, DB_UTILIZZO_DICHIARATO UD, "
          + "        DB_TIPO_UTILIZZO TU "
          + " WHERE  CD.ID_CONDUZIONE_DICHIARATA = ? "
          + " AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA "
          + " AND    UD.ID_UTILIZZO = TU.ID_UTILIZZO "
          + " AND    TU.FLAG_USO_AGRONOMICO = 'S' "
          + " GROUP BY CD.ID_CONDUZIONE_DICHIARATA ";
  
      SolmrLogger.debug(this, "Value of parameter 1 [ID_CONDUZIONE_DICHIARATA] in getSumSuperficieUtilizzoUsoAgronomico method in ConduzioneDichiarataDAO: "+idConduzioneDichiarata+"\n");
      
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idConduzioneDichiarata);
  
      SolmrLogger.debug(this, "Executing getSumSuperficieUtilizzoUsoAgronomico: "+query+"\n");
  
      ResultSet rs = stmt.executeQuery();
  
      if(rs.next()) {
        sumSup = rs.getBigDecimal("SUPERFICIE");
        
      }
      
      rs.close();
      stmt.close();
  
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getSumSuperficieUtilizzoUsoAgronomico in ConduzioneDichiarataDAO - SQLException: "+exc.getMessage()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getSumSuperficieUtilizzoUsoAgronomico in ConduzioneDichiarataDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getSumSuperficieUtilizzoUsoAgronomico in ConduzioneDichiarataDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getSumSuperficieUtilizzoUsoAgronomico in ConduzioneDichiarataDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getSumSuperficieUtilizzoUsoAgronomico method in ConduzioneDichiarataDAO\n");
    return sumSup;
  }
	
	
	/**
   * Per ogni particella (db_conduzione_dichiarata.id_particella)
   * accedere a db_utilizzo_dichiarato, estrarre tutti gli utilizzi per cui
   * db_tipo_utilizzo.flag_uso_agronomico=’S’ (superficie spandibile, cioè è
   * previsto utilizzo agronomico) ed effettuare la sommatoria del campo
   * superficie_utilizzata. Per le conduzioni si apoprio che di altre aziende 
   * si usano i dati della dichiarazione di consistenza
   * più recente (escluse quelle dichiarazione per avversità "siccità 2006" ovvero id_motivo_dichiarazione=7 e correttive). 
   * 
   * @param idParticella
   * @return
   * @throws DataAccessException
   */
  public BigDecimal getSumSuperficieUtilizzoUsoAgronomicoParticella(long idParticella, long idAzienda) throws DataAccessException
  {
    SolmrLogger.debug(this, "Invocating getSumSuperficieUtilizzoUsoAgronomicoParticella method in ConduzioneParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    BigDecimal sumSup = new BigDecimal(0);
    
    try
    {
      SolmrLogger.debug(this, "Creating db-connection in getSumSuperficieUtilizzoUsoAgronomicoParticella method in ConduzioneParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getSumSuperficieUtilizzoUsoAgronomicoParticella method in ConduzioneParticellaDAO and it values: "
          + conn + "\n");
      
      String query = "" 
        + "SELECT NVL(SUM(SUPERFICIE),0) AS TOT_SUPERFICIE " 
        + "FROM ( "         
        + "      SELECT SUM(NVL(UD.SUPERFICIE_UTILIZZATA,0)) SUPERFICIE "  
        + "      FROM  DB_CONDUZIONE_DICHIARATA CD, DB_DICHIARAZIONE_CONSISTENZA DC, " 
        + "      DB_ANAGRAFICA_AZIENDA AA, DB_UTILIZZO_DICHIARATO UD, DB_TIPO_UTILIZZO TU "
        + "      WHERE  AA.ID_AZIENDA = DC.ID_AZIENDA " 
        + "      AND    CD.CODICE_FOTOGRAFIA_TERRENI = DC.CODICE_FOTOGRAFIA_TERRENI "  
        + "      AND    CD.ID_PARTICELLA = ? "   
        + "      AND    AA.DATA_FINE_VALIDITA IS NULL " 
        + "      AND    AA.DATA_CESSAZIONE IS NULL "
        + "      AND    CD.ID_TITOLO_POSSESSO <> 6 "
        + "      AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA "
        + "      AND    UD.ID_UTILIZZO = TU.ID_UTILIZZO "
        + "      AND    TU.FLAG_USO_AGRONOMICO = 'S' " 
        + "      AND    AA.ID_AZIENDA = ? "      
        + "      AND    DC.ID_MOTIVO_DICHIARAZIONE <> 7 "
        + "      AND    DC.DATA_INSERIMENTO_DICHIARAZIONE = "
        + "             (SELECT MAX(DC1.DATA_INSERIMENTO_DICHIARAZIONE) " 
        + "             FROM   DB_DICHIARAZIONE_CONSISTENZA DC1, DB_TIPO_MOTIVO_DICHIARAZIONE D "
        + "             WHERE  DC1.ID_AZIENDA = DC.ID_AZIENDA "
        + "             AND D.ID_MOTIVO_DICHIARAZIONE = DC1.ID_MOTIVO_DICHIARAZIONE "
        + "             AND D.TIPO_DICHIARAZIONE <> 'C' "
        + "      AND    DC1.ID_MOTIVO_DICHIARAZIONE <> 7) "
        + "      GROUP BY CD.ID_PARTICELLA "        
        + "UNION ALL   "     
        + "      SELECT SUM(NVL(UD.SUPERFICIE_UTILIZZATA,0)) SUPERFICIE "  
        + "      FROM  DB_CONDUZIONE_DICHIARATA CD, DB_DICHIARAZIONE_CONSISTENZA DC, " 
        + "      DB_ANAGRAFICA_AZIENDA AA, DB_UTILIZZO_DICHIARATO UD "
        + "      WHERE  AA.ID_AZIENDA = DC.ID_AZIENDA " 
        + "      AND    CD.CODICE_FOTOGRAFIA_TERRENI = DC.CODICE_FOTOGRAFIA_TERRENI "  
        + "      AND    CD.ID_PARTICELLA = ? "
        + "      AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA "
        + "      AND    AA.DATA_FINE_VALIDITA IS NULL " 
        + "      AND    AA.DATA_CESSAZIONE IS NULL "
        + "      AND    CD.ID_TITOLO_POSSESSO <> 6 "
        + "      AND    AA.ID_AZIENDA <> ? "      
        + "      AND    DC.ID_MOTIVO_DICHIARAZIONE <> 7 "
        + "      AND    DC.DATA_INSERIMENTO_DICHIARAZIONE = "
        + "             (SELECT MAX(DC1.DATA_INSERIMENTO_DICHIARAZIONE) " 
        + "             FROM   DB_DICHIARAZIONE_CONSISTENZA DC1, DB_TIPO_MOTIVO_DICHIARAZIONE D "
        + "             WHERE  DC1.ID_AZIENDA = DC.ID_AZIENDA "
        + "             AND D.ID_MOTIVO_DICHIARAZIONE = DC1.ID_MOTIVO_DICHIARAZIONE "
        + "             AND D.TIPO_DICHIARAZIONE <> 'C' "
        + "      AND    DC1.ID_MOTIVO_DICHIARAZIONE <> 7) "
        + "      GROUP BY CD.ID_PARTICELLA "
        + "    )";
      
      
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idParticella);
      stmt.setLong(2, idAzienda);
      stmt.setLong(3, idParticella);
      stmt.setLong(4, idAzienda);
      
      SolmrLogger.debug(this, "Executing getSumSuperficieUtilizzoUsoAgronomicoParticella: " + query + "\n");
      
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {
        sumSup = rs.getBigDecimal("TOT_SUPERFICIE");        
      }
      
      rs.close();
      stmt.close();
      
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "getSumSuperficieUtilizzoUsoAgronomicoParticella in ConduzioneParticellaDAO - SQLException: " + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this, "getSumSuperficieUtilizzoUsoAgronomicoParticella in ConduzioneParticellaDAO - Generic Exception: " + ex + "\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.error(this,
            "getSumSuperficieUtilizzoUsoAgronomicoParticella in ConduzioneParticellaDAO - SQLException while closing Statement and Connection: "
                + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.error(this,
            "getSumSuperficieUtilizzoUsoAgronomicoParticella in ConduzioneParticellaDAO - Generic Exception while closing Statement and Connection: "
                + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getSumSuperficieUtilizzoUsoAgronomicoParticella method in ConduzioneParticellaDAO\n");
    return sumSup;
  }
  
  
  public BigDecimal getSumSuperficieAgronomicaAltreconduzioni(long idParticella, long idConduzioneDichiarata, long idAzienda) 
      throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getSumSuperficieAgronomicaAltreconduzioni method in ConduzioneDichiarataDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    BigDecimal sumSup = new BigDecimal(0);

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getSumSuperficieAgronomicaAltreconduzioni method in ConduzioneDichiarataDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getSumSuperficieAgronomicaAltreconduzioni method in ConduzioneDichiarataDAO and it values: "+conn+"\n");

      String query = " "
          + " SELECT "
          + "       SUM(DECODE(CD.ID_TITOLO_POSSESSO,5, NVL(CD.SUPERFICIE_CONDOTTA,0), NVL(CD.SUPERFICIE_AGRONOMICA,0))) SUPERFICIE " 
          + " FROM  DB_CONDUZIONE_DICHIARATA CD, DB_UTE UT,"
          + "       DB_DICHIARAZIONE_CONSISTENZA DC " 
          + " WHERE  UT.ID_AZIENDA = ? "
          + " AND    UT.ID_AZIENDA = DC.ID_AZIENDA "
          + " AND    CD.CODICE_FOTOGRAFIA_TERRENI = DC.CODICE_FOTOGRAFIA_TERRENI "  
          + " AND    UT.ID_UTE = CD.ID_UTE "
          + " AND    CD.ID_PARTICELLA = ? "    
          + " AND    UT.DATA_FINE_ATTIVITA IS NULL "
          + " AND    CD.ID_TITOLO_POSSESSO <> 6 "
          + " AND    CD.ID_CONDUZIONE_DICHIARATA <> ? "
          + " AND    DC.ID_MOTIVO_DICHIARAZIONE <> 7 "
          + " AND    DC.DATA_INSERIMENTO_DICHIARAZIONE = " 
          + "        (SELECT MAX(DC1.DATA_INSERIMENTO_DICHIARAZIONE) " 
          + "        FROM   DB_DICHIARAZIONE_CONSISTENZA DC1, DB_TIPO_MOTIVO_DICHIARAZIONE D " 
          + "        WHERE  DC1.ID_AZIENDA = DC.ID_AZIENDA "
          + "        AND D.ID_MOTIVO_DICHIARAZIONE=DC1.ID_MOTIVO_DICHIARAZIONE "
          + "        AND D.TIPO_DICHIARAZIONE<>'C' "
          + " AND    DC1.ID_MOTIVO_DICHIARAZIONE <> 7) "
          + " GROUP BY CD.ID_PARTICELLA ";

     
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda);
      stmt.setLong(2, idParticella);
      stmt.setLong(3, idConduzioneDichiarata);
      

      SolmrLogger.debug(this, "Executing getSumSuperficieAgronomicaAltreconduzioni: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      if(rs.next()) {
        sumSup = rs.getBigDecimal("SUPERFICIE");        
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getSumSuperficieAgronomicaAltreconduzioni in ConduzioneDichiarataDAO - SQLException: "+exc.getMessage()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getSumSuperficieAgronomicaAltreconduzioni in ConduzioneDichiarataDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getSumSuperficieAgronomicaAltreconduzioni in ConduzioneDichiarataDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getSumSuperficieAgronomicaAltreconduzioni in ConduzioneDichiarataDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getSumSuperficieAgronomicaAltreconduzioni method in ConduzioneDichiarataDAO\n");
    return sumSup;
  }
	
	
	public BigDecimal getSumSuperficieUtilizzata(long idParticella) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getSumSuperficieUtilizzata method in ConduzioneDichiarataDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    BigDecimal sumSup = null;
  
    try {
      SolmrLogger.debug(this, "Creating db-connection in getSumSuperficieUtilizzata method in ConduzioneDichiarataDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getSumSuperficieUtilizzata method in ConduzioneDichiarata and it values: "+conn+"\n");
  
      String query = " "
          + " SELECT "
          +	"        SUM(NVL(DU.SUPERFICIE_UTILIZZATA,0))  SUPERFICIE " 
          + " FROM   DB_CONDUZIONE_DICHIARATA CD, DB_DICHIARAZIONE_CONSISTENZA DC, " 
          + "        DB_ANAGRAFICA_AZIENDA AA, DB_UTILIZZO_DICHIARATO DU, DB_TIPO_UTILIZZO TU "
          + " WHERE  AA.ID_AZIENDA = DC.ID_AZIENDA "
          + " AND    CD.CODICE_FOTOGRAFIA_TERRENI = DC.CODICE_FOTOGRAFIA_TERRENI "
          + " AND    CD.ID_PARTICELLA = ? "    
          + " AND    AA.DATA_FINE_VALIDITA IS NULL "
          + " AND    AA.DATA_CESSAZIONE IS NULL "
          + " AND    CD.ID_TITOLO_POSSESSO <> 5 "
          + " AND    CD.ID_CONDUZIONE_DICHIARATA = DU.ID_CONDUZIONE_DICHIARATA "
          + " AND    TU.ID_UTILIZZO = DU.ID_UTILIZZO "
          + " AND    TU.FLAG_USO_AGRONOMICO = 'S' "
          + " AND    DC.ID_MOTIVO_DICHIARAZIONE <> 7 "
          + " AND    DC.DATA_INSERIMENTO_DICHIARAZIONE = " 
          + "        (SELECT MAX(DC1.DATA_INSERIMENTO_DICHIARAZIONE) " 
          + "        FROM   DB_DICHIARAZIONE_CONSISTENZA DC1, DB_TIPO_MOTIVO_DICHIARAZIONE D " 
          + "        WHERE  DC1.ID_AZIENDA = DC.ID_AZIENDA "
          + "        AND D.ID_MOTIVO_DICHIARAZIONE=DC1.ID_MOTIVO_DICHIARAZIONE "
          + "        AND D.TIPO_DICHIARAZIONE<>'C' "
          + " AND    DC1.ID_MOTIVO_DICHIARAZIONE <> 7) "
          + " GROUP BY CD.ID_PARTICELLA ";
  
      SolmrLogger.debug(this, "Value of parameter 1 [ID_PARTICELLA] in getSumSuperficieUtilizzata method in ConduzioneDichiarataDAO: "+idParticella+"\n");
      
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idParticella);
  
      SolmrLogger.debug(this, "Executing getSumSuperficieUtilizzata: "+query+"\n");
  
      ResultSet rs = stmt.executeQuery();
  
      if(rs.next()) {
        sumSup = rs.getBigDecimal("SUPERFICIE");
        
      }
      
      rs.close();
      stmt.close();
  
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getSumSuperficieUtilizzata in ConduzioneDichiarataDAO - SQLException: "+exc.getMessage()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getSumSuperficieUtilizzata in ConduzioneDichiarataDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getSumSuperficieUtilizzata in ConduzioneDichiarataDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getSumSuperficieUtilizzata in ConduzioneDichiarataDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getSumSuperficieUtilizzata method in ConduzioneDichiarataDAO\n");
    return sumSup;
  }
	
	/**
	 * Metodo che mi restituisce l'elenco delle particelle in tutte le sue componenti
	 * (DB_STORICO_PARTICELLA, DB_CONDUZIONE_DICHIARATA, DB_UTILIZZO_DICHIARATO) in relazione dei parametri di ricerca impostati,
	 * di un criterio di ordinamento e dell'azienda selezionata 
	 * 
	 * @param filtriParticellareRicercaVO
	 * @param idAzienda
	 * @return java.util.Vector
	 * @throws DataAccessException
	 */
	public Vector<StoricoParticellaVO> searchParticelleDichiarateByParameters(FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();
		String listOfParameters = "";

		try 
		{
			
			
			
			SolmrLogger.debug(this, "Creating db-connection in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO and it values: "+conn+"\n");
			
			if(filtriParticellareRicercaVO.getIdUtilizzo().compareTo(new Long(0)) != 0) 
			{
			  String query = "";
			  
			    //vengono restitutiti singolarmente anche i campi TOTALE e TOTALE2, ma nel resulset viene usata
	        //solo la somma (che rappresenta la superficie eleggibile
			    query += " " +
              " SELECT PARTICELLE_TABLE_SUM.* , " +
              "        TOTALE AS SUPERFICIE_ELEG, " +
              "        (SELECT 'P30' " +
              "         FROM DB_DICHIARAZIONE_SEGNALAZIONE DS " +
              "         WHERE DS.ID_STORICO_PARTICELLA = PARTICELLE_TABLE_SUM.ID_STORICO_PARTICELLA " +
              "         AND DS.ID_CONTROLLO = 211 " +
              "         AND DS.ID_DICHIARAZIONE_CONSISTENZA = ? " +
              "         AND DS.ID_AZIENDA = ? " +
              "         AND ROWNUM = 1 " +
              "        ) AS P30, " +
              "        (SELECT 'P25' " +
              "         FROM DB_DICHIARAZIONE_SEGNALAZIONE DS " +
              "         WHERE DS.ID_STORICO_PARTICELLA = PARTICELLE_TABLE_SUM.ID_STORICO_PARTICELLA " +
              "         AND DS.ID_CONTROLLO = 213 " +
              "         AND DS.ID_DICHIARAZIONE_CONSISTENZA = ? " +
              "         AND DS.ID_AZIENDA = ? " +
              "         AND ROWNUM = 1 " +
              "        ) AS P25, " +
              "       (SELECT 'P26' " +
              "        FROM DB_DICHIARAZIONE_SEGNALAZIONE DS " +
              "        WHERE DS.ID_STORICO_PARTICELLA = PARTICELLE_TABLE_SUM.ID_STORICO_PARTICELLA " +
              "        AND DS.ID_CONTROLLO IN (518,530) " +
              "        AND DS.ID_DICHIARAZIONE_CONSISTENZA = ? " +
              "        AND DS.ID_AZIENDA = ? " +
              "        AND ROWNUM = 1 " +
              "       ) AS P26, " +
              "       (SELECT 'BIOLOGICO' " +
              "        FROM DB_PARTICELLA_BIO PB " +
              "        WHERE PB.ID_AZIENDA = ? " +
              "        AND   PB.ID_PARTICELLA = PARTICELLE_TABLE_SUM.ID_PARTICELLA " +
              "        AND   PB.DATA_INIZIO_VALIDITA < PARTICELLE_TABLE_SUM.DATA_INSERIMENTO_DICHIARAZIONE " +
              "        AND   NVL(PB.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > PARTICELLE_TABLE_SUM.DATA_INSERIMENTO_DICHIARAZIONE " +
              "        AND ROWNUM = 1 ) AS BIOLOGICO, " +
              "       (CASE WHEN ( " +
              "                   DATA_SOSPENSIONE <= DATA_INSERIMENTO_DICHIARAZIONE " +
              "                  )" +
              "        THEN  'SOSPESA_GIS' " +
              "        END) AS SOSPESA_GIS, " +
              "       (CASE WHEN EXISTS " +
              "              (SELECT NE.ID_NOTIFICA_ENTITA " + 
              "               FROM   DB_NOTIFICA_ENTITA NE, " +
              "                      DB_TIPO_ENTITA TE " +
              "               WHERE  TE.CODICE_TIPO_ENTITA = 'P' " +
              "               AND    TE.ID_TIPO_ENTITA = NE.ID_TIPO_ENTITA " +
              "               AND    NE.IDENTIFICATIVO = PARTICELLE_TABLE_SUM.ID_PARTICELLA " +
              "               AND    NE.ID_DICHIARAZIONE_CONSISTENZA = PARTICELLE_TABLE_SUM.ID_DICHIARAZIONE_CONSISTENZA " +
              "               AND    NE.DATA_FINE_VALIDITA IS NULL) " +
              "         THEN 'NOTIFICA' " +
              "         END) AS IN_NOTIFICA " +
              " FROM ( " +
              "        SELECT PARTICELLE_TABLE.*, " +
              "               (NVL(PCK_SMRGAA_LIBRERIA.SelSupElegDichByFotoIdPartVar(PARTICELLE_TABLE.CODICE_FOTOGRAFIA_TERRENI,PARTICELLE_TABLE.DATA_INSERIMENTO_DICHIARAZIONE,PARTICELLE_TABLE.ID_PARTICELLA,PARTICELLE_TABLE.ID_CATALOGO_MATRICE),0) " +
              "               ) AS TOTALE, " +
              "               PCK_SMRGAA_LIBRERIA.SelSupElegDichByFtIdPartVarNT ( " +
              "                            PARTICELLE_TABLE.CODICE_FOTOGRAFIA_TERRENI, " +
              "                            PARTICELLE_TABLE.DATA_INSERIMENTO_DICHIARAZIONE, " +
              "                            PARTICELLE_TABLE.ID_PARTICELLA, " +
              "                            PARTICELLE_TABLE.ID_CATALOGO_MATRICE) " +
              "                  AS SUPERFICIE_ELEG_NETTA " +
              "        FROM ( " +
              "              WITH ISTANZA_RIESAME AS  " +
              "                (SELECT IR.DATA_RICHIESTA, " +
              "                        (CASE " +
              "                         WHEN (NVL(IR.DATA_EVASIONE, SYSDATE)) <  DCIR.DATA_INSERIMENTO_DICHIARAZIONE " +
              "                         THEN IR.DATA_EVASIONE " +            
              "                        END) AS DATA_EVASIONE, " +                
              "                        IR.ID_PARTICELLA " +
              "                 FROM   DB_ISTANZA_RIESAME IR, " +
              "                        DB_DICHIARAZIONE_CONSISTENZA DCIR " +
              "                        WHERE IR.DATA_RICHIESTA = ( " +
              "                                                       SELECT MAX(IRTMP.DATA_RICHIESTA) " +
              "                                                       FROM   DB_ISTANZA_RIESAME IRTMP, " +                                    
              "                                                                   DB_DICHIARAZIONE_CONSISTENZA DCIRTMP " +
              "                                                       WHERE  IRTMP.ID_AZIENDA = IR.ID_AZIENDA " +
              "                                                       AND    IRTMP.ID_PARTICELLA = IR.ID_PARTICELLA " +
              "                                                       AND    DCIRTMP.ID_DICHIARAZIONE_CONSISTENZA = DCIR.ID_DICHIARAZIONE_CONSISTENZA " +
              "                                                       AND    IRTMP.DATA_ANNULLAMENTO IS NULL " +
              "                                                       AND    IRTMP.DATA_RICHIESTA <   DCIRTMP.DATA_INSERIMENTO_DICHIARAZIONE " +
              "                                                      ) " +
              "                        AND   IR.ID_AZIENDA = ? " +
              "                        AND   DCIR.ID_DICHIARAZIONE_CONSISTENZA = ? ) " +
				      "              SELECT " +
				      "                     CD.ID_CONDUZIONE_DICHIARATA, " +
				      "                     CD.ID_CONDUZIONE_PARTICELLA, " +
				      "                     CD.ID_PARTICELLA, " +
							"                     CD.ESITO_CONTROLLO, " +
						  "                     SP.COMUNE, " +
						  "                     C.DESCOM, " +
						  "                     P.SIGLA_PROVINCIA, " +
						  "                     SP.SEZIONE, " +
						  "                     SP.FOGLIO, " +
						  "                     SP.PARTICELLA, " +
						  "                     SP.SUBALTERNO, " +
						  "                     SP.SUP_CATASTALE, " +
						  "                     SP.SUPERFICIE_GRAFICA, " +
							"                     SP.ID_ZONA_ALTIMETRICA, " +
							"                     SP.ID_AREA_C, " +
							"                     TTP.ID_TITOLO_POSSESSO, " +
							"                     TTP.DESCRIZIONE AS DESC_POSSESSO, " +
							"                     CD.SUPERFICIE_CONDOTTA, " +
							"                     CD.PERCENTUALE_POSSESSO, " +
							"                     CD.DATA_INIZIO_CONDUZIONE, " +
							"                     CD.DATA_FINE_CONDUZIONE, " +
							"                     CD.CODICE_FOTOGRAFIA_TERRENI, " +
							"                     UD.ID_UTILIZZO_DICHIARATO, " +
							"                     UD.ID_CATALOGO_MATRICE, " +
							"                     TU.CODICE AS COD_PRIMARIO, " +
							"                     TU.DESCRIZIONE AS DESC_PRIMARIO, " +	
							"                     TV.DESCRIZIONE AS DESC_VARIETA, " +
							"                     TV.CODICE_VARIETA AS COD_VARIETA, " +
							"                     UD.SUPERFICIE_UTILIZZATA, " +
							"                     UD.ID_CATALOGO_MATRICE_SECONDARIO, " +
							"                     RCM2.ID_UTILIZZO AS ID_UTILIZZO_SECONDARIO, " +
							"                     TU2.CODICE AS COD_SECONDARIO, " +
							"                     TU2.DESCRIZIONE AS DESC_SECONDARIO, " +
							"                     TV2.DESCRIZIONE AS VAR_SECONDARIA, " +
							"                     TV2.CODICE_VARIETA AS COD_VAR_SECONDARIA, " +
							"                     UD.SUP_UTILIZZATA_SECONDARIA, " +
							"                     SP.ID_CASO_PARTICOLARE, " +
							"                     TCP.DESCRIZIONE AS DESC_PARTICOLARE, " +
							"                     PART.FLAG_SCHEDARIO, " +
							"                     SP.ID_STORICO_PARTICELLA, " +
							"                     SP.ID_AREA_A, " +
							"                     SP.ID_AREA_B, " +
							"                     TMU.CODICE, " +
							"                     TMU.DESCRIZIONE AS DESC_MACRO, " +
							"                     UV.ID_AZIENDA AS ID_AZIENDA_UV, " +
							"                     CD.SUPERFICIE_AGRONOMICA, " +
							"                     RCM.ID_VARIETA, " +  
							"                     DC.DATA_INSERIMENTO_DICHIARAZIONE, " +
							"                     DC.ID_DICHIARAZIONE_CONSISTENZA, " +
				      "                     PC.ID_PARTICELLA_CERTIFICATA, " +
							"                     PC.SUP_SEMINABILE, " +
							"                     PC.SUP_COLT_ARBOREA_SPECIALIZZATA, " +
							"                     PC.SUP_GRAFICA, " +
              "                     PC.SUP_USO_GRAFICA, " +
              "                     PC.DATA_SOSPENSIONE, " +
              "                     PC.MOTIVAZIONE_GIS, " +
              "                     IRI.DATA_RICHIESTA, " +
              "                     IRI.DATA_EVASIONE, " +
              "                     F.FLAG_STABILIZZAZIONE, " +
              "                     RCM.ID_TIPO_DESTINAZIONE AS ID_TIPO_DEST_PRIM, " +
              "                     TDE.CODICE_DESTINAZIONE AS COD_DEST_USO_PRIM, " +
              "                     TDE.DESCRIZIONE_DESTINAZIONE AS DESC_DEST_USO_PRIM, " +
              "                     RCM2.ID_TIPO_DESTINAZIONE AS ID_TIPO_DEST_SEC, " +
              "                     TDE2.CODICE_DESTINAZIONE AS COD_DEST_USO_SEC, " +
              "                     TDE2.DESCRIZIONE_DESTINAZIONE AS DESC_DEST_USO_SEC, " +
              "                     RCM.ID_TIPO_DETTAGLIO_USO, " +
              "                     TDU.CODICE_DETTAGLIO_USO AS COD_DETT_USO_PRIM, " +
              "                     TDU.DESCRIZIONE_DETTAGLIO_USO AS DESC_DETT_USO_PRIM, " +
              "                     RCM2.ID_TIPO_DETTAGLIO_USO AS ID_TIPO_DETT_USO_SECONDARIO, " +
              "                     TDU2.CODICE_DETTAGLIO_USO AS COD_DETT_USO_SEC, " +
              "                     TDU2.DESCRIZIONE_DETTAGLIO_USO AS DESC_DETT_USO_SEC, " +
              "                     RCM.ID_TIPO_QUALITA_USO AS ID_TIPO_QUALITA_USO_PRIM, " +
              "                     TQU.CODICE_QUALITA_USO AS COD_QUALITA_USO_PRIM, " +
              "                     TQU.DESCRIZIONE_QUALITA_USO AS DESC_QUALITA_USO_PRIM, " +
              "                     RCM2.ID_TIPO_QUALITA_USO AS ID_TIPO_QUALITA_USO_SEC, " +
              "                     TQU2.CODICE_QUALITA_USO AS COD_QUALITA_USO_SEC, " +
              "                     TQU2.DESCRIZIONE_QUALITA_USO AS DESC_QUALITA_USO_SEC, " +
              "                     UD.ID_TIPO_PERIODO_SEMINA, " +
              "                     TPS.CODICE AS COD_PER_SEM_PRIM, " +
              "                     TPS.DESCRIZIONE AS DESC_PER_SEM_PRIM, " +
              "                     UD.ID_TIPO_PERIODO_SEMINA_SECOND, " +
              "                     TPS2.CODICE AS COD_PER_SEM_SEC, " +
              "                     TPS2.DESCRIZIONE AS DESC_PER_SEM_SEC, " +
              "                     UD.ID_SEMINA, " +
              "                     TSE.CODICE_SEMINA AS COD_SEM_PRIM, " +
              "                     TSE.DESCRIZIONE_SEMINA AS DESC_SEM_PRIM, " +
              "                     UD.ID_SEMINA_SECONDARIA, " +
              "                     TSE2.CODICE_SEMINA AS COD_SEM_SEC, " +
              "                     TSE2.DESCRIZIONE_SEMINA AS DESC_SEM_SEC, " +
              "                     UD.ID_PRATICA_MANTENIMENTO, " +
              "                     TPM.CODICE_PRATICA_MANTENIMENTO, " +
              "                     TPM.DESCRIZIONE_PRATICA_MANTENIMEN, " +
              "                     UD.ID_FASE_ALLEVAMENTO, " +
              "                     TFA.CODICE_FASE_ALLEVAMENTO, " +
              "                     TFA.DESCRIZIONE_FASE_ALLEVAMENTO, " +
              "                     UD.ID_TIPO_EFA, " +
              "                     TEF.DESCRIZIONE_TIPO_EFA, " +
              "                     UM.DESCRIZIONE AS DESC_UNITA_MIS_EFA, " +
              "                     UD.VALORE_ORIGINALE, " +
              "                     UD.VALORE_DOPO_CONVERSIONE, " +
              "                     UD.VALORE_DOPO_PONDERAZIONE, " +
              "                     UD.DATA_INIZIO_DESTINAZIONE, " +
              "                     UD.DATA_FINE_DESTINAZIONE, " +
              "                     UD.DATA_INIZIO_DESTINAZIONE_SEC, " +
              "                     UD.DATA_FINE_DESTINAZIONE_SEC, " +
              "                     (SELECT  MIN(DCOND.DATA_INIZIO_VALIDITA) " +  
              "                      FROM    DB_DOCUMENTO D, " +
              "                              DB_DOCUMENTO_CONDUZIONE DCOND " +
              "                      WHERE   D.ID_DOCUMENTO = DCOND.ID_DOCUMENTO " +
              "                      AND     D.ID_AZIENDA = ? " +
              "                      AND     D.DATA_INIZIO_VALIDITA <= TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
              "                      AND     NVL(D.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) >= TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
              "                      AND     DCOND.ID_CONDUZIONE_PARTICELLA = CD.ID_CONDUZIONE_PARTICELLA " + 
              "                      ) AS MIN_DATA_INIZIO  "+ /*, " +
              "                     PCK_SMRGAA_LIBRERIA.SelSupElegDichByFtIdPartVarNT(CD.CODICE_FOTOGRAFIA_TERRENI,DC.DATA_INSERIMENTO_DICHIARAZIONE,CD.ID_PARTICELLA,UD.ID_CATALOGO_MATRICE) " +
              "                       AS SUPERFICIE_ELEG_NETTA " +*/
              "              FROM   DB_UTE U, " +
							"                     DB_STORICO_PARTICELLA SP, " +   	
							"                     DB_CONDUZIONE_DICHIARATA CD, " +
							"                     DB_DICHIARAZIONE_CONSISTENZA DC, " +
							"  	                  COMUNE C, " +
							"                     DB_TIPO_TITOLO_POSSESSO TTP, " +
							"                     DB_UTILIZZO_DICHIARATO UD, " +
							"                     DB_R_CATALOGO_MATRICE RCM, " +
	            "                     DB_R_CATALOGO_MATRICE RCM2, " +
							"                     DB_TIPO_UTILIZZO TU, " +
							"                     DB_TIPO_VARIETA TV, " +
							"                     DB_TIPO_UTILIZZO TU2, " +	
							"                     DB_TIPO_VARIETA TV2, " +
							"                     DB_TIPO_DETTAGLIO_USO TDU, " +
	            "                     DB_TIPO_DETTAGLIO_USO TDU2, " +
	            "                     DB_TIPO_DESTINAZIONE TDE, " +
	            "                     DB_TIPO_DESTINAZIONE TDE2, " +
	            "                     DB_TIPO_QUALITA_USO TQU, " +
	            "                     DB_TIPO_QUALITA_USO TQU2, " +
	            "                     DB_TIPO_PERIODO_SEMINA TPS, " +
	            "                     DB_TIPO_PERIODO_SEMINA TPS2, " +
	            "                     DB_TIPO_SEMINA TSE, " +
	            "                     DB_TIPO_SEMINA TSE2, " +
	            "                     DB_TIPO_PRATICA_MANTENIMENTO TPM, " +
	            "                     DB_TIPO_FASE_ALLEVAMENTO TFA, " +
	            "                     DB_TIPO_EFA TEF, " +
	            "                     DB_UNITA_MISURA UM, " +
							"                     PROVINCIA P, " +
							"                     DB_TIPO_CASO_PARTICOLARE TCP, " +
							"                     DB_PARTICELLA PART, " +
							"                     (SELECT UAD.ID_AZIENDA, " +
							"                             UAD.ID_STORICO_PARTICELLA " +
							"                      FROM   DB_UNITA_ARBOREA_DICHIARATA UAD, " +
							"                             DB_DICHIARAZIONE_CONSISTENZA DC " +
							"                      WHERE  UAD.ID_AZIENDA =  ? " + 
							"                      AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
							"                      AND    DC.CODICE_FOTOGRAFIA_TERRENI = UAD.CODICE_FOTOGRAFIA_TERRENI(+) " +
							"                      GROUP BY " +
							"                             UAD.ID_AZIENDA, " +
							"                             UAD.ID_STORICO_PARTICELLA) " +
							"                      UV, " +
							"                      DB_TIPO_MACRO_USO TMU, " +
							"                      DB_TIPO_MACRO_USO_VARIETA TMUV, " +
				      "                      DB_PARTICELLA_CERTIFICATA PC, " +
				      "                      ISTANZA_RIESAME IRI, " +
				      "                      DB_FOGLIO F ";
				
				//Se è attivo almeno uno dei due filtri tra solo asservite e solo conferite deve andare in join con la
				//DB_DICHIARAZIONE_CONSISTENZA
				if(filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S)
						|| filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S))  
				{
					query += 
					    "                    , DB_DICHIARAZIONE_CONSISTENZA DICH_CONS, " +
							"                      DB_CONDUZIONE_DICHIARATA CD2 ";
				}
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo())) 
				{
					query += 
					    "                    , DB_DICHIARAZIONE_SEGNALAZIONE DS ";
				}
				
				//Tabelle per la ricerca sui documenti
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()) 
            || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
        {
          query += "" +
          		"   ,DB_DOCUMENTO_CONDUZIONE DOCC, " +
          		"    DB_DOCUMENTO DOC1, " +
          		"    DB_TIPO_DOCUMENTO TDOC, " +
              "    DB_DOCUMENTO_CATEGORIA DOCCAT ";
        }
        
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoValoreArea()))
        {
          if("S".equalsIgnoreCase(filtriParticellareRicercaVO.getFlagFoglio()))
          {
            query += "" +
              "   ,DB_R_FOGLIO_AREA RFA ";
          }
          else
          {
            query += "" +
              "   ,DB_R_PARTICELLA_AREA RPA ";            
          }         
        }
				
				query += " WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
					         " AND    U.ID_AZIENDA = ? " +
					         " AND    TRUNC(U.DATA_INIZIO_ATTIVITA) <= TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
			             " AND    TRUNC(NVL(U.DATA_FINE_ATTIVITA, TO_DATE('31/12/9999', 'DD/MM/YYYY'))) > TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
			             " AND    U.ID_UTE = CD.ID_UTE " +
			             " AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
			             " AND    CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
			             " AND    PART.ID_PARTICELLA = SP.ID_PARTICELLA " +
			             " AND    SP.COMUNE = C.ISTAT_COMUNE " +
			             " AND    SP.ID_CASO_PARTICOLARE = TCP.ID_CASO_PARTICOLARE(+) " +
			             " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) " +
			             " AND    CD.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO " +
			             " AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA(+) " +
			             " AND    UD.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE (+) "  +
			             " AND    UD.ID_CATALOGO_MATRICE_SECONDARIO = RCM2.ID_CATALOGO_MATRICE (+) "  +
			             " AND    RCM.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
			             " AND    RCM.ID_VARIETA = TV.ID_VARIETA(+) " +
			             " AND    RCM2.ID_UTILIZZO = TU2.ID_UTILIZZO(+) " +
			             " AND    RCM2.ID_VARIETA = TV2.ID_VARIETA(+) " +
			             " AND    RCM.ID_TIPO_DETTAGLIO_USO = TDU.ID_TIPO_DETTAGLIO_USO(+) " +
			             " AND    RCM2.ID_TIPO_DETTAGLIO_USO = TDU2.ID_TIPO_DETTAGLIO_USO(+) " +
			             " AND    RCM.ID_TIPO_DESTINAZIONE = TDE.ID_TIPO_DESTINAZIONE(+) " +
			             " AND    RCM2.ID_TIPO_DESTINAZIONE = TDE2.ID_TIPO_DESTINAZIONE(+) " +
			             " AND    RCM.ID_TIPO_QUALITA_USO = TQU.ID_TIPO_QUALITA_USO(+) " +
			             " AND    RCM2.ID_TIPO_QUALITA_USO = TQU2.ID_TIPO_QUALITA_USO(+) " +
			             " AND    UD.ID_TIPO_PERIODO_SEMINA = TPS.ID_TIPO_PERIODO_SEMINA(+) " +
			             " AND    UD.ID_TIPO_PERIODO_SEMINA_SECOND = TPS2.ID_TIPO_PERIODO_SEMINA(+) " +
			             " AND    UD.ID_SEMINA = TSE.ID_SEMINA(+) " +
			             " AND    UD.ID_SEMINA_SECONDARIA = TSE2.ID_SEMINA(+) " +
			             " AND    UD.ID_PRATICA_MANTENIMENTO = TPM.ID_PRATICA_MANTENIMENTO(+) " +
			             " AND    UD.ID_FASE_ALLEVAMENTO = TFA.ID_FASE_ALLEVAMENTO(+) " +
			             " AND    UD.ID_TIPO_EFA = TEF.ID_TIPO_EFA(+) " +
			             " AND    TEF.ID_UNITA_MISURA = UM.ID_UNITA_MISURA(+) " +
			             " AND    SP.ID_PARTICELLA = IRI.ID_PARTICELLA (+) ";
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdMacroUso())) 
				{
					query += 
					     " AND RCM.ID_CATALOGO_MATRICE = TMUV.ID_CATALOGO_MATRICE " +
					     " AND TMUV.ID_MACRO_USO = TMU.ID_MACRO_USO " +
							 " AND TMUV.DATA_INIZIO_VALIDITA <= ? " +
							 " AND NVL(TMUV.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? " + 
							 " AND TMU.ID_MACRO_USO = ? ";
				}
				else 
				{
					query += 
					    " AND RCM.ID_CATALOGO_MATRICE = TMUV.ID_CATALOGO_MATRICE (+) " +
							" AND TMUV.ID_MACRO_USO = TMU.ID_MACRO_USO (+) " +
		        	" AND NVL(TMUV.DATA_INIZIO_VALIDITA, TO_DATE('01/01/1900', 'DD/MM/YYYY')) <= ? " +
		        	" AND NVL(TMUV.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? ";
				}
				// Ricerco i dati della particella certificata solo se non vengono
				// richieste le particelle solo asservite
				query += " AND    SP.COMUNE = PC.COMUNE(+) " +
							 " AND    NVL(SP.SEZIONE, '-') = NVL(PC.SEZIONE(+), '-') " +
							 " AND    SP.FOGLIO = PC.FOGLIO(+) " +
							 " AND    NVL(SP.PARTICELLA, 0) = NVL(PC.PARTICELLA(+), 0) " +
							 " AND    NVL(SP.SUBALTERNO, '-') = NVL(PC.SUBALTERNO(+), '-') " +
							 " AND    SP.ID_PARTICELLA =  PC.ID_PARTICELLA(+) " +
							 " AND    PC.DATA_INIZIO_VALIDITA(+) <= ? " +
							 " AND    TRUNC(NVL(PC.DATA_FINE_VALIDITA(+), TO_DATE('31/12/9999', 'DD/MM/YYYY'))) > ? " +
               " AND    UV.ID_STORICO_PARTICELLA(+) = SP.ID_STORICO_PARTICELLA ";
					
				
				
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo())) 
				{
					query += " AND DS.ID_AZIENDA = ? " +
							 " AND DS.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
							 " AND DS.ID_DICHIARAZIONE_CONSISTENZA = ? " +
							 " AND DS.ID_CONTROLLO = ? ";
				}
				// 	Se l'utente ha indicato l'ute di riferimento
				if(filtriParticellareRicercaVO.getIdUte() != null) 
				{
					query += " AND U.ID_UTE = ? ";
				}
				
				query += "" +
            " AND    SP.COMUNE = F.COMUNE(+) " +
            " AND    NVL( SP.SEZIONE,'-') = NVL( F.SEZIONE(+),'-') " +
            " AND    SP.FOGLIO = F.FOGLIO(+) ";
				
				//Combo documenti
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()) 
            || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento())
            || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
        {
          query += " AND CD.ID_CONDUZIONE_PARTICELLA = DOCC.ID_CONDUZIONE_PARTICELLA " +
                   " AND DOCC.ID_DOCUMENTO = DOC1.ID_DOCUMENTO " +
                   " AND DOC1.EXT_ID_DOCUMENTO = TDOC.ID_DOCUMENTO " +
                   " AND TDOC.ID_DOCUMENTO = DOCCAT.ID_DOCUMENTO " +
                   " AND DOCCAT.ID_CATEGORIA_DOCUMENTO = ? " +
                   " AND DOC1.DATA_INIZIO_VALIDITA <= DC.DATA_INSERIMENTO_DICHIARAZIONE " +
                   " AND NVL(DOC1.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) >= DC.DATA_INSERIMENTO_DICHIARAZIONE ";
          
          if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
          {
            query += " AND TDOC.ID_DOCUMENTO = ? ";
          }
          
          if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
          {
            query += " AND DOC1.ID_DOCUMENTO = ? ";
          }
          
        }
        
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoValoreArea()))
        {
          if("S".equalsIgnoreCase(filtriParticellareRicercaVO.getFlagFoglio()))
          {
            query += "" +
              "AND F.ID_FOGLIO = RFA.ID_FOGLIO " +
              "AND RFA.ID_TIPO_VALORE_AREA = ? ";
          }
          else
          {
            query += "" +
              "AND SP.ID_PARTICELLA = RPA.ID_PARTICELLA " +
              "AND RPA.ID_TIPO_VALORE_AREA = ? ";            
          }         
        }
        
        
        //Notifiche
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipologiaNotifica())
            || (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCategoriaNotifica())))
        {
          query +=   " AND  EXISTS  (SELECT NE.ID_NOTIFICA_ENTITA " + 
                     "               FROM   DB_NOTIFICA_ENTITA NE, " +
                     "                      DB_NOTIFICA NO, " +
                     "                      DB_TIPO_ENTITA TE " +
                     "               WHERE  TE.CODICE_TIPO_ENTITA = 'P' " +
                     "               AND    TE.ID_TIPO_ENTITA = NE.ID_TIPO_ENTITA " +
                     "               AND    NE.IDENTIFICATIVO = SP.ID_PARTICELLA " +
                     "               AND    NE.ID_NOTIFICA = NO.ID_NOTIFICA " +
                     "               AND    NE.ID_DICHIARAZIONE_CONSISTENZA = DC.ID_DICHIARAZIONE_CONSISTENZA ";
          if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipologiaNotifica()))
          {
            query += "               AND    NO.ID_TIPOLOGIA_NOTIFICA = ? ";          
          }
          if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCategoriaNotifica()))
          {
            query += "               AND    NO.ID_CATEGORIA_NOTIFICA = ? ";          
          }
          if(Validator.isNotEmpty(filtriParticellareRicercaVO.getFlagNotificheChiuse()))
          {
            query +=   "AND    (NE.DATA_FINE_VALIDITA IS NULL " +
                       "        OR NE.DATA_FINE_VALIDITA = (SELECT MAX(NE1.DATA_FINE_VALIDITA) " +
                       "                                    FROM   DB_NOTIFICA_ENTITA NE1 " +
                       "                                    WHERE  NE1.IDENTIFICATIVO = NE.IDENTIFICATIVO " +
                       "                                    AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA " +
                       "                                    AND    NE1.ID_DICHIARAZIONE_CONSISTENZA = NE.ID_DICHIARAZIONE_CONSISTENZA)) )";
          }
          else
          {
            query +=   " AND    NE.DATA_FINE_VALIDITA IS NULL )";
          }
          
        }
				
				// Se l'utente ha indicato il comune di riferimento
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIstatComune())) 
				{
					query += " AND SP.COMUNE = ? ";
				}
				// Se l'utente ha indicato la sezione di riferimento
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getSezione())) 
				{
					query += " AND SP.SEZIONE = ? ";
				}
				// Se l'utente ha indicato il foglio di riferimento
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getFoglio())) 
				{
					query += " AND SP.FOGLIO = ? ";
				}
				// Se l'utente ha indicato la particella di riferimento
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getParticella())) 
				{
					query += " AND SP.PARTICELLA = ? ";
				}
				// Se l'utente ha indicato il subalterno di riferimento
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getSubalterno())) 
				{
					query += " AND SP.SUBALTERNO = ? ";
				}
        // Se l'utente ha indicato il tipo Zona Altimetrica
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdZonaAltimetrica())) 
        {
          query += " AND SP.ID_ZONA_ALTIMETRICA = ? ";
        }
        // Se l'utente ha indicato il tipo Caso Particolare
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCasoParticolare())) 
        {
          query += " AND SP.ID_CASO_PARTICOLARE = ? ";
        }
				// Se l'utente invece ha specificato un particolare uso del suolo
				if(filtriParticellareRicercaVO.getIdUtilizzo().longValue() > 0) 
				{
					// Se non è stato indicato nei filtri di ricerca se l'utilizzo selezionato
					// è primario o secondario, viene usata come condizione di default quella
					// dell'utilizzo primario
					if(!Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario()) && !Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario())) 
					{
						query += " AND RCM.ID_UTILIZZO = ? ";
					}
					else 
					{
						// Uso Primario
						if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario())) 
						{
							query += " AND RCM.ID_UTILIZZO = ? ";
						}
						// Uso Secondario
						else if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario())) 
						{
							query += " AND RCM2.ID_UTILIZZO = ? ";
						}
						// Se ha selezionato sia l'opzione uso primario che quella uso secondario
						else if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario()) && Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario())) 
						{
							query += " AND (RCM.ID_UTILIZZO = ?  OR RCM2.ID_UTILIZZO = ?) ";
						}
					}				
				}
				
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoEfa())) 
        {
          query += " AND UD.ID_TIPO_EFA = ? ";
        }
				// Se l'utente ha specificato un particolare titolo di possesso
				if(filtriParticellareRicercaVO.getIdTitoloPossesso() != null) 
				{
					query += " AND CD.ID_TITOLO_POSSESSO = ? ";
				}
				else 
				{
					// Se l'utente ha scelto l'opzione escludi asservimento
					if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckEscludiAsservimento()) && filtriParticellareRicercaVO.getCheckEscludiAsservimento().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
					{
						query += " AND CD.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
					}
					// Se l'utente ha scelto l'opzione escludi conferimento
					if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckEscludiConferimento()) && filtriParticellareRicercaVO.getCheckEscludiConferimento().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
					{
						query += " AND CD.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO;
					}
				}
				// Se l'utente ha specificato la tipologia di anomalia bloccante
				boolean isFirst = true;
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneBloccante())) 
				{
					query += " AND (CD.ESITO_CONTROLLO = ? ";
					isFirst = false;
				}
				// Se l'utente ha specificato la tipologia di anomalia warning
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneWarning())) 
				{
					if(!isFirst) 
					{
						query += " OR ";
					}
					else {
						query += " AND (";
					}
					query += " CD.ESITO_CONTROLLO = ? ";
					isFirst = false;
				}
				// Se l'utente ha specificato la tipologia di anomalia OK
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneOk())) 
				{
					if(!isFirst) {
						query += " OR ";
					}
					else {
						query += " AND (";
					}
					query += " CD.ESITO_CONTROLLO = ? ";
					isFirst = false;
				}
				if(!isFirst) 
				{
					query += ")";
				}
				// Ricerco i dati delle particelle solo asservite
				if(filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
				{
					query += " AND DICH_CONS.ID_AZIENDA <> ? " +
							     " AND DICH_CONS.DATA_INSERIMENTO_DICHIARAZIONE = "+
							     "(SELECT MAX(DATA_INSERIMENTO_DICHIARAZIONE) " +
							     " FROM DB_DICHIARAZIONE_CONSISTENZA DC1 " +
							     " WHERE DICH_CONS.ID_AZIENDA = DC1.ID_AZIENDA" +
							     " AND DC1.DATA_INSERIMENTO_DICHIARAZIONE < DC.DATA_INSERIMENTO_DICHIARAZIONE ) " +
							     " AND DICH_CONS.CODICE_FOTOGRAFIA_TERRENI = CD2.CODICE_FOTOGRAFIA_TERRENI " +
							     " AND CD2.ID_TITOLO_POSSESSO = ? " +
							     " AND CD2.ID_PARTICELLA = SP.ID_PARTICELLA ";
				}
				// Ricerco i dati delle particelle solo conferite
				if(filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
				{
						query += " AND DICH_CONS.ID_AZIENDA <> ? " +
								     " AND DICH_CONS.DATA_INSERIMENTO_DICHIARAZIONE = "+
								     "(SELECT MAX(DATA_INSERIMENTO_DICHIARAZIONE) " +
								     " FROM DB_DICHIARAZIONE_CONSISTENZA DC1 " +
								     " WHERE DICH_CONS.ID_AZIENDA = DC1.ID_AZIENDA" +
								     " AND DC1.DATA_INSERIMENTO_DICHIARAZIONE < DC.DATA_INSERIMENTO_DICHIARAZIONE ) " +
								     " AND DICH_CONS.CODICE_FOTOGRAFIA_TERRENI = CD2.CODICE_FOTOGRAFIA_TERRENI " +
								     " AND CD2.ID_TITOLO_POSSESSO = ? " +
								     " AND CD2.ID_PARTICELLA = SP.ID_PARTICELLA ";
				}
				query += " GROUP BY CD.ID_CONDUZIONE_DICHIARATA, " +      
				     "          CD.ID_CONDUZIONE_PARTICELLA, " +
				     "          CD.ID_PARTICELLA, " +
					   "          CD.ESITO_CONTROLLO, " + 
						 "          SP.COMUNE, " +           
						 "          C.DESCOM, " +           
						 "          P.SIGLA_PROVINCIA, " +           
						 "          SP.SEZIONE, " +           
						 "          SP.FOGLIO, " +           
						 "          SP.PARTICELLA, " +           
						 "          SP.SUBALTERNO, " +           
						 "          SP.SUP_CATASTALE, " +
						 "          SP.SUPERFICIE_GRAFICA, " +
						 "          SP.ID_ZONA_ALTIMETRICA, " +
						 "          SP.ID_AREA_C, " +
						 "          TTP.ID_TITOLO_POSSESSO, " +
						 "          TTP.DESCRIZIONE, " +
						 "          CD.SUPERFICIE_CONDOTTA, " +
						 "          CD.PERCENTUALE_POSSESSO, " +
						 "          CD.DATA_INIZIO_CONDUZIONE, " +           
						 "          CD.DATA_FINE_CONDUZIONE, " +    
						 "          CD.CODICE_FOTOGRAFIA_TERRENI, " +
						 "          UD.ID_UTILIZZO_DICHIARATO, " +
						 "          UD.ID_CATALOGO_MATRICE, " +
						 "          TU.CODICE, " +
						 "          TU.DESCRIZIONE, " +	
						 "          TV.DESCRIZIONE, " +
						 "          TV.CODICE_VARIETA, " +
						 "          UD.SUPERFICIE_UTILIZZATA, " +
						 "          UD.ID_CATALOGO_MATRICE_SECONDARIO, " +
						 "          RCM2.ID_UTILIZZO, " +
						 "          TU2.CODICE, " +
						 "          TU2.DESCRIZIONE, " +
						 "          TV2.DESCRIZIONE, " +	
						 "          TV2.CODICE_VARIETA, " +
						 "          UD.SUP_UTILIZZATA_SECONDARIA, " +
						 "          SP.ID_CASO_PARTICOLARE, " +
						 "          TCP.DESCRIZIONE, " +
						 "          PART.FLAG_SCHEDARIO, " +
						 "          SP.ID_STORICO_PARTICELLA, " +
						 "          SP.ID_AREA_A, " +
					   "          SP.ID_AREA_B, " +
						 "          TMU.CODICE, " +
						 "          TMU.DESCRIZIONE, " +
						 "          UV.ID_AZIENDA, " +
						 "          CD.SUPERFICIE_AGRONOMICA, " +
						 "          RCM.ID_VARIETA, " + 
				     "          DC.DATA_INSERIMENTO_DICHIARAZIONE, " +
				     "          DC.ID_DICHIARAZIONE_CONSISTENZA, " +
				     "          PC.ID_PARTICELLA_CERTIFICATA, " +
						 "          PC.SUP_SEMINABILE, " +
						 "          PC.SUP_COLT_ARBOREA_SPECIALIZZATA, " +
						 "          PC.SUP_GRAFICA, " +
             "          PC.SUP_USO_GRAFICA," +
             "          PC.DATA_SOSPENSIONE, " +
             "          PC.MOTIVAZIONE_GIS, " +
             "          IRI.DATA_RICHIESTA, " +
             "          IRI.DATA_EVASIONE, " +
             "          F.FLAG_STABILIZZAZIONE, " +
             "          RCM.ID_TIPO_DESTINAZIONE, " +
             "          TDE.CODICE_DESTINAZIONE, " +
             "          TDE.DESCRIZIONE_DESTINAZIONE, " +
             "          RCM2.ID_TIPO_DESTINAZIONE, " +
             "          TDE2.CODICE_DESTINAZIONE, " +
             "          TDE2.DESCRIZIONE_DESTINAZIONE, " +
             "          RCM.ID_TIPO_DETTAGLIO_USO, " +
             "          TDU.CODICE_DETTAGLIO_USO, " +
             "          TDU.DESCRIZIONE_DETTAGLIO_USO, " +
             "          RCM2.ID_TIPO_DETTAGLIO_USO, " +
             "          TDU2.CODICE_DETTAGLIO_USO, " +
             "          TDU2.DESCRIZIONE_DETTAGLIO_USO, " +
             "          RCM.ID_TIPO_QUALITA_USO, " +
             "          TQU.CODICE_QUALITA_USO, " +
             "          TQU.DESCRIZIONE_QUALITA_USO, " +
             "          RCM2.ID_TIPO_QUALITA_USO, " +
             "          TQU2.CODICE_QUALITA_USO, " +
             "          TQU2.DESCRIZIONE_QUALITA_USO, " +
             "          UD.ID_TIPO_PERIODO_SEMINA, " +
             "          TPS.CODICE, " +
             "          TPS.DESCRIZIONE, " +
             "          UD.ID_TIPO_PERIODO_SEMINA_SECOND, " +
             "          TPS2.CODICE, " +
             "          TPS2.DESCRIZIONE, " +
             "          UD.ID_SEMINA, " +
             "          TSE.CODICE_SEMINA, " +
             "          TSE.DESCRIZIONE_SEMINA, " +
             "          UD.ID_SEMINA_SECONDARIA, " +
             "          TSE2.CODICE_SEMINA, " +
             "          TSE2.DESCRIZIONE_SEMINA, " +
             "          UD.ID_PRATICA_MANTENIMENTO, " +
             "          TPM.CODICE_PRATICA_MANTENIMENTO, " +
             "          TPM.DESCRIZIONE_PRATICA_MANTENIMEN, " +
             "          UD.ID_FASE_ALLEVAMENTO, " +
             "          TFA.CODICE_FASE_ALLEVAMENTO, " +
             "          TFA.DESCRIZIONE_FASE_ALLEVAMENTO, " +
             "          UD.ID_TIPO_EFA, " +
             "          TEF.DESCRIZIONE_TIPO_EFA, " +
             "          UM.DESCRIZIONE, " +
             "          UD.VALORE_ORIGINALE, " +
             "          UD.VALORE_DOPO_CONVERSIONE, " +
             "          UD.VALORE_DOPO_PONDERAZIONE, " +
             "          UD.DATA_INIZIO_DESTINAZIONE, " +
             "          UD.DATA_FINE_DESTINAZIONE, " +
             "          UD.DATA_INIZIO_DESTINAZIONE_SEC, " +
             "          UD.DATA_FINE_DESTINAZIONE_SEC ";
				
				if(!Validator.isNotEmpty(filtriParticellareRicercaVO.getOrderBy())) 
				{
					query += " ORDER BY C.DESCOM,P.SIGLA_PROVINCIA,SP.SEZIONE,SP.FOGLIO,SP.PARTICELLA,SP.SUBALTERNO," +
							"TTP.ID_TITOLO_POSSESSO,COD_PRIMARIO,DESC_PRIMARIO,COD_SECONDARIO,DESC_SECONDARIO,SP.ID_CASO_PARTICOLARE ";
				}
				else 
				{
					if(filtriParticellareRicercaVO.getOrderBy()
					     .equalsIgnoreCase((String)SolmrConstants.ORDER_BY_CHAR_COMUNE_DESC)) 
					{
						query += " ORDER BY "+filtriParticellareRicercaVO.getOrderBy()+",P.SIGLA_PROVINCIA,SP.SEZIONE,SP.FOGLIO,SP.PARTICELLA,SP.SUBALTERNO";
					}
					else if(filtriParticellareRicercaVO.getOrderBy()
               .equalsIgnoreCase((String)SolmrConstants.ORDER_BY_CHAR_COMUNE_ASC))
					{
					  query += " ORDER BY "+filtriParticellareRicercaVO.getOrderBy()+",P.SIGLA_PROVINCIA,SP.SEZIONE,SP.FOGLIO,SP.PARTICELLA,SP.SUBALTERNO";
					}
					else 
					{
						query += " ORDER BY "+filtriParticellareRicercaVO.getOrderBy()+",C.DESCOM,P.SIGLA_PROVINCIA,SP.SEZIONE,SP.FOGLIO,SP.PARTICELLA,SP.SUBALTERNO";
					}
				}      
				
				query += " ) PARTICELLE_TABLE ) PARTICELLE_TABLE_SUM ";
        
	      //**********************************
				stmt = conn.prepareStatement(query);
						
				SolmrLogger.debug(this, "Executing searchParticelleDichiarateByParameters: "+query+"\n");
				
				// Setto i parametri dello statement
				int indice = 0;
				
				//P30
				stmt.setLong(++indice, filtriParticellareRicercaVO.getIdPianoRiferimento().longValue());
				stmt.setLong(++indice, idAzienda.longValue());
				//P25
				stmt.setLong(++indice, filtriParticellareRicercaVO.getIdPianoRiferimento().longValue());
				stmt.setLong(++indice, idAzienda.longValue());
				//P26
				stmt.setLong(++indice, filtriParticellareRicercaVO.getIdPianoRiferimento().longValue());
				stmt.setLong(++indice, idAzienda.longValue());
				//Biologico
				stmt.setLong(++indice, idAzienda.longValue());
				
				
				//Istanza di riesame
        stmt.setLong(++indice, idAzienda.longValue());
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdPianoRiferimento().longValue());
        
				
				
				// ID_AZIENDA
				SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_AZIENDA] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+idAzienda+"\n");
				stmt.setLong(++indice, idAzienda.longValue());
				listOfParameters += "ID_AZIENDA: "+idAzienda;
				
			  //Istanza riesame
        //stmt.setLong(++indice, filtriParticellareRicercaVO.getIdPianoRiferimento().longValue());
				
				
				// ID_AZIENDA				
				SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_AZIENDA] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+idAzienda+"\n");
				stmt.setLong(++indice, idAzienda.longValue());
				listOfParameters += "ID_AZIENDA: "+idAzienda;
				// ID_DICHIARAZIONE_CONSISTENZA				
				SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_DICHIARAZIONE_CONSISTENZA] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdPianoRiferimento()+"\n");
				stmt.setLong(++indice, filtriParticellareRicercaVO.getIdPianoRiferimento().longValue());
				listOfParameters += "ID_DICHIARAZIONE_CONSISTENZA: "+filtriParticellareRicercaVO.getIdPianoRiferimento();
				// ID_DICHIARAZIONE_CONSISTENZA				
				SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_DICHIARAZIONE_CONSISTENZA] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdPianoRiferimento()+"\n");
				stmt.setLong(++indice, filtriParticellareRicercaVO.getIdPianoRiferimento().longValue());
				listOfParameters += "ID_DICHIARAZIONE_CONSISTENZA: "+filtriParticellareRicercaVO.getIdPianoRiferimento();
				// ID_AZIENDA				
				SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_AZIENDA] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+idAzienda+"\n");
				stmt.setLong(++indice, idAzienda.longValue());
				listOfParameters += "ID_AZIENDA: "+idAzienda;
				// DATA_INSERIMENTO_DICHIARAZIONE				
				SolmrLogger.debug(this, "Value of parameter "+indice+" [DATA_INSERIMENTO_DICHIARAZIONE] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getDataInserimentoDichiarazione()+"\n");
				stmt.setDate(++indice, new java.sql.Date(filtriParticellareRicercaVO.getDataInserimentoDichiarazione().getTime()));
				listOfParameters += "DATA_INSERIMENTO_DICHIARAZIONE: "+new java.sql.Date(filtriParticellareRicercaVO.getDataInserimentoDichiarazione().getTime());
				// DATA_INSERIMENTO_DICHIARAZIONE				
				SolmrLogger.debug(this, "Value of parameter "+indice+" [DATA_INSERIMENTO_DICHIARAZIONE] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getDataInserimentoDichiarazione()+"\n");
				stmt.setDate(++indice, new java.sql.Date(filtriParticellareRicercaVO.getDataInserimentoDichiarazione().getTime()));
				listOfParameters += "DATA_INSERIMENTO_DICHIARAZIONE: "+new java.sql.Date(filtriParticellareRicercaVO.getDataInserimentoDichiarazione().getTime());
				// ID_MACRO_USO
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdMacroUso())) {					
					SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_MACRO_USO] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdMacroUso()+"\n");
					stmt.setLong(++indice, filtriParticellareRicercaVO.getIdMacroUso().longValue());
					listOfParameters += "ID_MACRO_USO: "+filtriParticellareRicercaVO.getIdMacroUso();
				}
				
				// DATA_INSERIMENTO_DICHIARAZIONE					
				SolmrLogger.debug(this, "Value of parameter "+indice+" [DATA_INSERIMENTO_DICHIARAZIONE] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getDataInserimentoDichiarazione()+"\n");
				stmt.setDate(++indice, new java.sql.Date(filtriParticellareRicercaVO.getDataInserimentoDichiarazione().getTime()));
				listOfParameters += "DATA_INSERIMENTO_DICHIARAZIONE: "+new java.sql.Date(filtriParticellareRicercaVO.getDataInserimentoDichiarazione().getTime());
				// DATA_INSERIMENTO_DICHIARAZIONE					
				SolmrLogger.debug(this, "Value of parameter "+indice+" [DATA_INSERIMENTO_DICHIARAZIONE] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getDataInserimentoDichiarazione()+"\n");
				stmt.setDate(++indice, new java.sql.Date(filtriParticellareRicercaVO.getDataInserimentoDichiarazione().getTime()));
				listOfParameters += "DATA_INSERIMENTO_DICHIARAZIONE: "+new java.sql.Date(filtriParticellareRicercaVO.getDataInserimentoDichiarazione().getTime());
				// ID_CONTROLLO
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo())) {					
					SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_AZIENDA] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+idAzienda+"\n");
					stmt.setLong(++indice, idAzienda.longValue());
					listOfParameters += "ID_AZIENDA: "+idAzienda;					
					SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_DICHIARAZIONE_CONSISTENZA] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdPianoRiferimento()+"\n");
					stmt.setLong(++indice, filtriParticellareRicercaVO.getIdPianoRiferimento().longValue());
					listOfParameters += "ID_DICHIARAZIONE_CONSISTENZA: "+filtriParticellareRicercaVO.getIdPianoRiferimento();					
					SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_CONTROLLO] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdControllo()+"\n");
					stmt.setLong(++indice, filtriParticellareRicercaVO.getIdControllo().longValue());
					listOfParameters += "ID_CONTROLLO: "+filtriParticellareRicercaVO.getIdControllo();
				}
				// ID_UTE
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdUte())) {					
					SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_UTE] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdUte()+"\n");
					stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUte().longValue());
					listOfParameters += ", ID_UTE: "+filtriParticellareRicercaVO.getIdUte();
				}
        // TIPO DOCUMENTO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()))
        {
          SolmrLogger.debug(this, "Value of parameter " + indice 
              + "[ID_CATEGORIA_DOCUMENTO] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: " + filtriParticellareRicercaVO.getIdTipoDocumento()
              + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTipoDocumento().longValue());
          listOfParameters += ", ID_CATEGORIA_DOCUMENTO: " + filtriParticellareRicercaVO.getIdTipoDocumento();
        }
        
        // DOCUMENTO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_DOCUMENTO] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: " + filtriParticellareRicercaVO.getIdDocumento()
              + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdDocumento().longValue());
          listOfParameters += ", ID_DOCUMENTO: " + filtriParticellareRicercaVO.getIdDocumento();
        }
        // PROTOCOLLO DOCUMENTO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_PROTOCOLLO_DOCUMENTO] in searchParticelleDichiarateByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdProtocolloDocumento()
              + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdProtocolloDocumento().longValue());
          listOfParameters += ", ID_PROTOCOLLO_DOCUMENTO: " + filtriParticellareRicercaVO.getIdProtocolloDocumento();
        }
        // ID_TIPO_VALORE_AREA
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoValoreArea()))
        {
          SolmrLogger.debug(this, "Value of parameter " + indice + "[ID_TIPO_VALORE_AREA] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
              + filtriParticellareRicercaVO.getIdTipoValoreArea() + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTipoValoreArea().longValue());
          listOfParameters += ", ID_TIPO_VALORE_AREA: " + filtriParticellareRicercaVO.getIdTipoValoreArea();
        }    
        // Se l'utente ha indicato la tipologia notifica
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipologiaNotifica())) 
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_TIPOLOGIA_NOTIFICA] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: " + filtriParticellareRicercaVO.getIdTipologiaNotifica()
              + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTipologiaNotifica().longValue());
          listOfParameters += ", ID_TIPOLOGIA_NOTIFICA: " + filtriParticellareRicercaVO.getIdTipologiaNotifica();
        }
        // Se l'utente ha indicato la categoria notifica
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCategoriaNotifica())) 
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_CATEGORIA_NOTIFICA] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: " + filtriParticellareRicercaVO.getIdCategoriaNotifica()
              + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdCategoriaNotifica().longValue());
          listOfParameters += ", ID_CATEGORIA_NOTIFICA: " + filtriParticellareRicercaVO.getIdCategoriaNotifica();
        }
				// ISTAT_COMUNE
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIstatComune())) {					
					SolmrLogger.debug(this, "Value of parameter "+indice+"[ISTAT_COMUNE] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIstatComune()+"\n");
					stmt.setString(++indice, filtriParticellareRicercaVO.getIstatComune());
					listOfParameters += ", ISTAT_COMUNE: "+filtriParticellareRicercaVO.getIstatComune();
				}
				// SEZIONE
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getSezione())) {					
					SolmrLogger.debug(this, "Value of parameter "+indice+"[SEZIONE] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getSezione()+"\n");
					stmt.setString(++indice, filtriParticellareRicercaVO.getSezione().toUpperCase());
					listOfParameters += ", SEZIONE: "+filtriParticellareRicercaVO.getSezione();
				}
				// FOGLIO
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getFoglio())) {					
					SolmrLogger.debug(this, "Value of parameter "+indice+"[FOGLIO] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getFoglio()+"\n");
					stmt.setString(++indice, filtriParticellareRicercaVO.getFoglio());
					listOfParameters += ", FOGLIO: "+filtriParticellareRicercaVO.getFoglio();
				}
				// PARTICELLA
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getParticella())) {					
					SolmrLogger.debug(this, "Value of parameter "+indice+"[PARTICELLA] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getParticella()+"\n");
					stmt.setString(++indice, filtriParticellareRicercaVO.getParticella());
					listOfParameters += ", PARTICELLA: "+filtriParticellareRicercaVO.getParticella();
				}
				// SUBALTERNO
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getSubalterno())) {
					
					SolmrLogger.debug(this, "Value of parameter "+indice+"[SUBALTERNO] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getSubalterno()+"\n");
					stmt.setString(++indice, filtriParticellareRicercaVO.getSubalterno().toUpperCase());
					listOfParameters += ", SUBALTERNO: "+filtriParticellareRicercaVO.getSubalterno();
				}
        // ID_ZONA_ALTIMETRICA
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdZonaAltimetrica())) {          
          SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_ZONA_ALTIMETRICA] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdZonaAltimetrica()+"\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdZonaAltimetrica().longValue());
          listOfParameters += ", ID_ZONA_ALTIMETRICA: "+filtriParticellareRicercaVO.getIdZonaAltimetrica();
        }
        // ID_CASO_PARTICOLARE
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCasoParticolare())) {          
          SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_CASO_PARTICOLARE] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdCasoParticolare()+"\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdCasoParticolare().longValue());
          listOfParameters += ", ID_CASO_PARTICOLARE: "+filtriParticellareRicercaVO.getIdCasoParticolare();
        }
				// Se non è stato indicato se l'uso è primario o secondario
				if(!Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario()) && !Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario())) {
					// Se è stato indicato un particolare uso del suolo, seleziono di default l'opzione
					// uso primario
					if(filtriParticellareRicercaVO.getIdUtilizzo().intValue() > 0) {						
						SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_UTILIZZO] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdUtilizzo()+"\n");
						stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
						listOfParameters += ", ID_UTILIZZO: "+filtriParticellareRicercaVO.getIdUtilizzo();
					}
				}
				else {
					// Se è stato indicato un particolare uso del suolo, seleziono di default l'opzione
					// uso primario
					if(filtriParticellareRicercaVO.getIdUtilizzo().intValue() > 0) {
						// Uso primario
						if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario())) {							
							SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_UTILIZZO] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdUtilizzo()+"\n");
							stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
							listOfParameters += ", ID_UTILIZZO: "+filtriParticellareRicercaVO.getIdUtilizzo();
						}
						// Uso secondario
						else if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario())) {							
							SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_UTILIZZO_SECONDARIO] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdUtilizzo()+"\n");
							stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
							listOfParameters += ", ID_UTILIZZO_SECONDARIO: "+filtriParticellareRicercaVO.getIdUtilizzo();
						}
						// Altrimenti se ha selezionato sia l'opzione uso primario che quella uso secondario
						else if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario()) 
						    && Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario())) {							
							SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_UTILIZZO] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdUtilizzo()+"\n");
							stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
							listOfParameters += ", ID_UTILIZZO: "+filtriParticellareRicercaVO.getIdUtilizzo();							
							SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_UTILIZZO_SECONDARIO] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdUtilizzo()+"\n");
							stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
							listOfParameters += ", ID_UTILIZZO_SECONDARIO: "+filtriParticellareRicercaVO.getIdUtilizzo();
						}
					}
				}
				//TIPO EFA
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoEfa())) 
				{
          SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_TIPO_EFA] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdTipoEfa()+"\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTipoEfa().longValue());
          listOfParameters += ", ID_TIPO_EFA: "+filtriParticellareRicercaVO.getIdTipoEfa();
        }
				// ID_TIPO_TITOLO_POSSESSO
				if(filtriParticellareRicercaVO.getIdTitoloPossesso() != null) {					
					SolmrLogger.debug(this, "Value of parameter"+indice+"[ID_TITOLO_POSSESSO] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdTitoloPossesso()+"\n");
					stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTitoloPossesso().longValue());
					listOfParameters += ", ID_TITOLO_POSSESSO: "+filtriParticellareRicercaVO.getIdTitoloPossesso();
				}
				// SEGNALAZIONI
				// Se l'utente ha specificato la tipologia di anomalia bloccante
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneBloccante())) {					
					SolmrLogger.debug(this, "Value of parameter "+indice+"[TIPO_SEGNALAZIONE_BLOCCANTE] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getTipoSegnalazioneBloccante()+"\n");
					stmt.setString(++indice, filtriParticellareRicercaVO.getTipoSegnalazioneBloccante());
					listOfParameters += ", SEGNALAZIONE: "+filtriParticellareRicercaVO.getTipoSegnalazioneBloccante();
				}
				// Se l'utente ha specificato la tipologia di anomalia warning
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneWarning())) {					
					SolmrLogger.debug(this, "Value of parameter "+indice+"[TIPO_SEGNALAZIONE_WARNING] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getTipoSegnalazioneWarning()+"\n");
					stmt.setString(++indice, filtriParticellareRicercaVO.getTipoSegnalazioneWarning());
					listOfParameters += ", SEGNALAZIONE: "+filtriParticellareRicercaVO.getTipoSegnalazioneWarning();
				}
				// Se l'utente ha specificato la tipologia di anomalia OK
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneOk())) {					
					SolmrLogger.debug(this, "Value of parameter "+indice+"[TIPO_SEGNALAZIONE_OK] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getTipoSegnalazioneOk()+"\n");
					stmt.setString(++indice, filtriParticellareRicercaVO.getTipoSegnalazioneOk());
					listOfParameters += ", SEGNALAZIONE: "+filtriParticellareRicercaVO.getTipoSegnalazioneOk();					
				}
				// Ricerco i dati della particella certificata solo se vengono
				// richieste le particelle solo asservite
				if(filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S)) {					
					SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_AZIENDA] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+idAzienda+"\n");
					stmt.setLong(++indice, idAzienda.longValue());
					listOfParameters += ", ID_AZIENDA: "+idAzienda;					
					SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_TITOLO_POSSESSO] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO+"\n");
					stmt.setLong(++indice, SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO.longValue());
					listOfParameters += ", ID_TITOLO_POSSESSO: "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
				}
				// Ricerco i dati delle particelle solo conferite
				if(filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
				{					
					SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_AZIENDA] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+idAzienda+"\n");
					stmt.setLong(++indice, idAzienda.longValue());
					listOfParameters += ", ID_AZIENDA: "+idAzienda;					
					SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_TITOLO_POSSESSO] in searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO: "+SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO+"\n");
					stmt.setLong(++indice, SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO.longValue());
					listOfParameters += ", ID_TITOLO_POSSESSO: "+SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO;
				}
				
				ResultSet rs = stmt.executeQuery();
				
				// Primo monitoraggio
				SolmrLogger.debug(this, "ConduzioneDichiarataDAO::searchParticelleDichiarateByParameters - "+
				    "In searchParticelleDichiarateByParameters method from the creation of query to the execution, List of parameters: "+listOfParameters);
	
				while(rs.next()) {
					StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
					ConduzioneDichiarataVO conduzioneDichiarataVO = new ConduzioneDichiarataVO();
					conduzioneDichiarataVO.setIdConduzioneDichiarata(new Long(rs.getLong("ID_CONDUZIONE_DICHIARATA")));
					conduzioneDichiarataVO.setIdConduzioneParticella(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));
					
					conduzioneDichiarataVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
					if(Validator.isNotEmpty(rs.getTimestamp("MIN_DATA_INIZIO"))) {
						DocumentoVO documentoVO = new DocumentoVO();
						DocumentoConduzioneVO documentoConduzioneVO = new DocumentoConduzioneVO();
						//documentoVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
						documentoConduzioneVO.setMinDataInizioValidita(rs.getTimestamp("MIN_DATA_INIZIO"));
						//documentoConduzioneVO.setMinDataFineValidita(rs.getTimestamp("MIN_DATA_FINE"));
						documentoVO.setDocumentoConduzioneVO(documentoConduzioneVO);
						storicoParticellaVO.setDocumentoVO(documentoVO);
					}
					storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
					ComuneVO comuneVO = new ComuneVO();
					comuneVO.setIstatComune(rs.getString("COMUNE"));
					comuneVO.setDescom(rs.getString("DESCOM"));
					comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
					storicoParticellaVO.setComuneParticellaVO(comuneVO);
					storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
					storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
					storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
					storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
					storicoParticellaVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
					storicoParticellaVO.setSuperficieGrafica(rs.getString("SUPERFICIE_GRAFICA"));
					if(Validator.isNotEmpty(rs.getString("ID_ZONA_ALTIMETRICA"))) {
						storicoParticellaVO.setIdZonaAltimetrica(new Long(rs.getLong("ID_ZONA_ALTIMETRICA")));
					}
					if(Validator.isNotEmpty(rs.getString("ID_AREA_C"))) {
						storicoParticellaVO.setIdAreaC(new Long(rs.getLong("ID_AREA_C")));
					}
					conduzioneDichiarataVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_TITOLO_POSSESSO")), rs.getString("DESC_POSSESSO"));
					conduzioneDichiarataVO.setTitoloPossesso(code);
					conduzioneDichiarataVO.setSuperficieCondotta(rs.getString("SUPERFICIE_CONDOTTA"));
					conduzioneDichiarataVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
					conduzioneDichiarataVO.setDataInizioConduzione(rs.getDate("DATA_INIZIO_CONDUZIONE"));
					conduzioneDichiarataVO.setDataFineConduzione(rs.getDate("DATA_FINE_CONDUZIONE"));
					conduzioneDichiarataVO.setCodiceFotografiaTerreni(rs.getString("CODICE_FOTOGRAFIA_TERRENI"));
					conduzioneDichiarataVO.setSuperficieAgronomica(rs.getString("SUPERFICIE_AGRONOMICA"));
					if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO_DICHIARATO"))) 
					{
						UtilizzoDichiaratoVO utilizzoDichiaratoVO = new UtilizzoDichiaratoVO();
						utilizzoDichiaratoVO.setIdUtilizzoDichiarato(new Long(rs.getLong("ID_UTILIZZO_DICHIARATO")));
						utilizzoDichiaratoVO.setIdCatalogoMatrice(checkLongNull(rs.getString("ID_CATALOGO_MATRICE")));
						utilizzoDichiaratoVO.setIdCatalogoMatriceSecondario(checkLongNull(rs.getString("ID_CATALOGO_MATRICE_SECONDARIO")));
						if(Validator.isNotEmpty(rs.getString("CODICE"))) 
						{
							TipoMacroUsoVO tipoMacroUsoVO = new TipoMacroUsoVO();
							tipoMacroUsoVO.setCodice(rs.getString("CODICE"));
							tipoMacroUsoVO.setDescrizione(rs.getString("DESC_MACRO"));
							utilizzoDichiaratoVO.setTipoMacroUsoVO(tipoMacroUsoVO);
						}
						TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
						tipoUtilizzoVO.setCodice(rs.getString("COD_PRIMARIO"));
						tipoUtilizzoVO.setDescrizione(rs.getString("DESC_PRIMARIO"));
						if(Validator.isNotEmpty(rs.getString("DESC_VARIETA"))) {
							TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
							tipoVarietaVO.setDescrizione(rs.getString("DESC_VARIETA"));
							tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VARIETA"));
							utilizzoDichiaratoVO.setTipoVarietaVO(tipoVarietaVO);
						}
						utilizzoDichiaratoVO.setTipoUtilizzoVO(tipoUtilizzoVO);
						utilizzoDichiaratoVO.setSuperficieUtilizzata(rs.getString("SUPERFICIE_UTILIZZATA"));
						if(Validator.isNotEmpty(rs.getString("COD_SECONDARIO"))) {
							TipoUtilizzoVO tipoUtilizzoSecondarioVO = new TipoUtilizzoVO();
							tipoUtilizzoSecondarioVO.setCodice(rs.getString("COD_SECONDARIO"));
							tipoUtilizzoSecondarioVO.setDescrizione(rs.getString("DESC_SECONDARIO"));
							utilizzoDichiaratoVO.setTipoUtilizzoSecondarioVO(tipoUtilizzoSecondarioVO);
						}
						if(Validator.isNotEmpty(rs.getString("VAR_SECONDARIA"))) {
							TipoVarietaVO tipoVarietaSecondariaVO = new TipoVarietaVO();
							tipoVarietaSecondariaVO.setDescrizione(rs.getString("VAR_SECONDARIA"));
							tipoVarietaSecondariaVO.setCodiceVarieta(rs.getString("COD_VAR_SECONDARIA"));
							utilizzoDichiaratoVO.setTipoVarietaSecondariaVO(tipoVarietaSecondariaVO);
						}
						utilizzoDichiaratoVO.setSupUtilizzataSecondaria(rs.getString("SUP_UTILIZZATA_SECONDARIA"));
						
						
						if(Validator.isNotEmpty(rs.getString("ID_TIPO_DEST_PRIM"))) {
						  utilizzoDichiaratoVO.setIdTipoDestinazione(new Long(rs.getLong("ID_TIPO_DEST_PRIM")));
	            TipoDestinazioneVO tipoDestinazioneVO = new TipoDestinazioneVO();
	            tipoDestinazioneVO.setIdTipoDestinazione(new Long(rs.getLong("ID_TIPO_DEST_PRIM")));
	            tipoDestinazioneVO.setDescrizioneDestinazione(rs.getString("DESC_DEST_USO_PRIM"));
	            tipoDestinazioneVO.setCodiceDestinazione(rs.getString("COD_DEST_USO_PRIM"));
	            utilizzoDichiaratoVO.setTipoDestinazione(tipoDestinazioneVO);
	          }
	          if(Validator.isNotEmpty(rs.getString("ID_TIPO_DEST_SEC"))) {
	            utilizzoDichiaratoVO.setIdTipoDestinazioneSecondario(new Long(rs.getLong("ID_TIPO_DEST_SEC")));
	            TipoDestinazioneVO tipoDestinazioneVO = new TipoDestinazioneVO();
	            tipoDestinazioneVO.setIdTipoDestinazione(new Long(rs.getLong("ID_TIPO_DEST_SEC")));
	            tipoDestinazioneVO.setDescrizioneDestinazione(rs.getString("DESC_DEST_USO_SEC"));
	            tipoDestinazioneVO.setCodiceDestinazione(rs.getString("COD_DEST_USO_SEC"));
	            utilizzoDichiaratoVO.setTipoDestinazioneSecondario(tipoDestinazioneVO);
	          }
						
						
						if(Validator.isNotEmpty(rs.getString("ID_TIPO_DETTAGLIO_USO"))) {
						  utilizzoDichiaratoVO.setIdTipoDettaglioUso(new Long(rs.getLong("ID_TIPO_DETTAGLIO_USO")));
	            TipoDettaglioUsoVO tipoDettaglioUsoVO = new TipoDettaglioUsoVO();
	            tipoDettaglioUsoVO.setIdTipoDettaglioUso(new Long(rs.getLong("ID_TIPO_DETTAGLIO_USO")));
	            tipoDettaglioUsoVO.setDescrizione(rs.getString("DESC_DETT_USO_PRIM"));
	            tipoDettaglioUsoVO.setCodiceDettaglioUso(rs.getString("COD_DETT_USO_PRIM"));
	            utilizzoDichiaratoVO.setTipoDettaglioUso(tipoDettaglioUsoVO);
	          }
	          if(Validator.isNotEmpty(rs.getString("ID_TIPO_DETT_USO_SECONDARIO"))) {
	            utilizzoDichiaratoVO.setIdTipoDettaglioUsoSecondario(new Long(rs.getLong("ID_TIPO_DETT_USO_SECONDARIO")));
	            TipoDettaglioUsoVO tipoDettaglioUsoVO = new TipoDettaglioUsoVO();
	            tipoDettaglioUsoVO.setIdTipoDettaglioUso(new Long(rs.getLong("ID_TIPO_DETT_USO_SECONDARIO")));
	            tipoDettaglioUsoVO.setDescrizione(rs.getString("DESC_DETT_USO_SEC"));
	            tipoDettaglioUsoVO.setCodiceDettaglioUso(rs.getString("COD_DETT_USO_SEC"));
	            utilizzoDichiaratoVO.setTipoDettaglioUsoSecondario(tipoDettaglioUsoVO);
	          }         
	          if(Validator.isNotEmpty(rs.getString("ID_TIPO_QUALITA_USO_PRIM"))) {
	            utilizzoDichiaratoVO.setIdTipoQualitaUso(new Long(rs.getLong("ID_TIPO_QUALITA_USO_PRIM")));
	            TipoQualitaUsoVO tipoQualitaUsoVO = new TipoQualitaUsoVO();
	            tipoQualitaUsoVO.setIdTipoQualitaUso(new Long(rs.getLong("ID_TIPO_QUALITA_USO_PRIM")));
	            tipoQualitaUsoVO.setDescrizioneQualitaUso(rs.getString("DESC_QUALITA_USO_PRIM"));
	            tipoQualitaUsoVO.setCodiceQualitaUso(rs.getString("COD_QUALITA_USO_PRIM"));
	            utilizzoDichiaratoVO.setTipoQualitaUso(tipoQualitaUsoVO);
	          }
	          if(Validator.isNotEmpty(rs.getString("ID_TIPO_QUALITA_USO_SEC"))) {
	            utilizzoDichiaratoVO.setIdTipoQualitaUsoSecondario(new Long(rs.getLong("ID_TIPO_QUALITA_USO_SEC")));
	            TipoQualitaUsoVO tipoQualitaUsoVO = new TipoQualitaUsoVO();
	            tipoQualitaUsoVO.setIdTipoQualitaUso(new Long(rs.getLong("ID_TIPO_QUALITA_USO_SEC")));
	            tipoQualitaUsoVO.setDescrizioneQualitaUso(rs.getString("DESC_QUALITA_USO_SEC"));
	            tipoQualitaUsoVO.setCodiceQualitaUso(rs.getString("COD_QUALITA_USO_SEC"));
	            utilizzoDichiaratoVO.setTipoQualitaUsoSecondario(tipoQualitaUsoVO);
	          }
	          if(Validator.isNotEmpty(rs.getString("ID_TIPO_PERIODO_SEMINA"))) {
	            utilizzoDichiaratoVO.setIdTipoPeriodoSemina(new Long(rs.getLong("ID_TIPO_PERIODO_SEMINA")));
	            TipoPeriodoSeminaVO tipoPeriodoSeminaVO = new TipoPeriodoSeminaVO();
	            tipoPeriodoSeminaVO.setIdTipoPeriodoSemina(new Long(rs.getLong("ID_TIPO_PERIODO_SEMINA")));
	            tipoPeriodoSeminaVO.setDescrizione(rs.getString("DESC_PER_SEM_PRIM"));
	            tipoPeriodoSeminaVO.setCodice(rs.getString("COD_PER_SEM_PRIM"));
	            utilizzoDichiaratoVO.setTipoPeriodoSemina(tipoPeriodoSeminaVO);
	          }
	          if(Validator.isNotEmpty(rs.getString("ID_TIPO_PERIODO_SEMINA_SECOND"))) {
	            utilizzoDichiaratoVO.setIdTipoPeriodoSeminaSecondario(new Long(rs.getLong("ID_TIPO_PERIODO_SEMINA_SECOND")));
	            TipoPeriodoSeminaVO tipoPeriodoSeminaVO = new TipoPeriodoSeminaVO();
	            tipoPeriodoSeminaVO.setIdTipoPeriodoSemina(new Long(rs.getLong("ID_TIPO_PERIODO_SEMINA_SECOND")));
	            tipoPeriodoSeminaVO.setDescrizione(rs.getString("DESC_PER_SEM_SEC"));
	            tipoPeriodoSeminaVO.setCodice(rs.getString("COD_PER_SEM_SEC"));
	            utilizzoDichiaratoVO.setTipoPeriodoSeminaSecondario(tipoPeriodoSeminaVO);
	          }
	          
	          utilizzoDichiaratoVO.setIdTipoEfa(checkLongNull(rs.getString("ID_TIPO_EFA")));
	          utilizzoDichiaratoVO.setDescTipoEfaEfa(rs.getString("DESCRIZIONE_TIPO_EFA"));
	          utilizzoDichiaratoVO.setDescUnitaMisuraEfa(rs.getString("DESC_UNITA_MIS_EFA"));
	          utilizzoDichiaratoVO.setValoreOriginale(rs.getBigDecimal("VALORE_ORIGINALE"));
	          utilizzoDichiaratoVO.setValoreDopoConversione(rs.getBigDecimal("VALORE_DOPO_CONVERSIONE"));
	          utilizzoDichiaratoVO.setValoreDopoPonderazione(rs.getBigDecimal("VALORE_DOPO_PONDERAZIONE"));
	          
	          
	          
	          utilizzoDichiaratoVO.setDataInizioDestinazione(rs.getTimestamp("DATA_INIZIO_DESTINAZIONE"));
	          utilizzoDichiaratoVO.setDataFineDestinazione(rs.getTimestamp("DATA_FINE_DESTINAZIONE"));
	          if(Validator.isNotEmpty(rs.getString("ID_FASE_ALLEVAMENTO"))) {
	            utilizzoDichiaratoVO.setIdFaseAllevamento(new Long(rs.getLong("ID_FASE_ALLEVAMENTO")));
	            TipoFaseAllevamentoVO tipoFaseAllevamentoVO = new TipoFaseAllevamentoVO();
	            tipoFaseAllevamentoVO.setIdFaseAllevamento(new Long(rs.getLong("ID_FASE_ALLEVAMENTO")));
	            tipoFaseAllevamentoVO.setDescrizioneFaseAllevamento(rs.getString("DESCRIZIONE_FASE_ALLEVAMENTO"));
	            tipoFaseAllevamentoVO.setCodiceFaseAllevamento(rs.getString("CODICE_FASE_ALLEVAMENTO"));
	            utilizzoDichiaratoVO.setTipoFaseAllevamento(tipoFaseAllevamentoVO);
	          }
	          
	          if(Validator.isNotEmpty(rs.getString("ID_PRATICA_MANTENIMENTO"))) {
	            utilizzoDichiaratoVO.setIdPraticaMantenimento(new Long(rs.getLong("ID_PRATICA_MANTENIMENTO")));
	            TipoPraticaMantenimentoVO tipoPraticaMantenimentoVO = new TipoPraticaMantenimentoVO();
	            tipoPraticaMantenimentoVO.setIdPraticaMantenimento(new Long(rs.getLong("ID_PRATICA_MANTENIMENTO")));
	            tipoPraticaMantenimentoVO.setDescrizionePraticaMantenim(rs.getString("DESCRIZIONE_PRATICA_MANTENIMEN"));
	            tipoPraticaMantenimentoVO.setCodicePraticaMantenimento(rs.getString("CODICE_PRATICA_MANTENIMENTO"));
	            utilizzoDichiaratoVO.setTipoPraticaMantenimento(tipoPraticaMantenimentoVO);
	          }
	          
	          if(Validator.isNotEmpty(rs.getString("ID_SEMINA"))) {
	            utilizzoDichiaratoVO.setIdSemina(new Long(rs.getLong("ID_SEMINA")));
	            TipoSeminaVO tipoSeminaVO = new TipoSeminaVO();
	            tipoSeminaVO.setIdTipoSemina(new Long(rs.getLong("ID_SEMINA")));
	            tipoSeminaVO.setDescrizioneSemina(rs.getString("DESC_SEM_PRIM"));
	            tipoSeminaVO.setCodiceSemina(rs.getString("COD_SEM_PRIM"));
	            utilizzoDichiaratoVO.setTipoSemina(tipoSeminaVO);
	          }
	          
	          if(Validator.isNotEmpty(rs.getString("ID_SEMINA_SECONDARIA"))) {
	            utilizzoDichiaratoVO.setIdSeminaSecondario(new Long(rs.getLong("ID_SEMINA_SECONDARIA")));
	            TipoSeminaVO tipoSeminaVO = new TipoSeminaVO();
	            tipoSeminaVO.setIdTipoSemina(new Long(rs.getLong("ID_SEMINA_SECONDARIA")));
	            tipoSeminaVO.setDescrizioneSemina(rs.getString("DESC_SEM_SEC"));
	            tipoSeminaVO.setCodiceSemina(rs.getString("COD_SEM_SEC"));
	            utilizzoDichiaratoVO.setTipoSeminaSecondario(tipoSeminaVO);
	          }
	          
	          utilizzoDichiaratoVO.setDataInizioDestinazioneSec(rs.getTimestamp("DATA_INIZIO_DESTINAZIONE_SEC"));
	          utilizzoDichiaratoVO.setDataFineDestinazioneSec(rs.getTimestamp("DATA_FINE_DESTINAZIONE_SEC"));
	          
	          
	          
	          
	          
	          
	          
						
						UtilizzoDichiaratoVO[] elencoUtilizzi = new UtilizzoDichiaratoVO[1];
						elencoUtilizzi[0] = utilizzoDichiaratoVO;
						conduzioneDichiarataVO.setElencoUtilizzi(elencoUtilizzi);
					}
					ConduzioneDichiarataVO[] elencoConduzioniDich = new ConduzioneDichiarataVO[1];
					elencoConduzioniDich[0] = conduzioneDichiarataVO;
					storicoParticellaVO.setElencoConduzioniDichiarate(elencoConduzioniDich);
					if(Validator.isNotEmpty(rs.getString("ID_CASO_PARTICOLARE"))) {
						storicoParticellaVO.setIdCasoParticolare(new Long(rs.getLong("ID_CASO_PARTICOLARE")));
						CodeDescription codeParticolare = new CodeDescription(Integer.decode(rs.getString("ID_CASO_PARTICOLARE")), rs.getString("DESC_PARTICOLARE"));
						storicoParticellaVO.setCasoParticolare(codeParticolare);
					}
					ParticellaVO particellaVO = new ParticellaVO();
					particellaVO.setFlagSchedario(rs.getString("FLAG_SCHEDARIO"));
					storicoParticellaVO.setParticellaVO(particellaVO);
					storicoParticellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
					if(Validator.isNotEmpty(rs.getString("ID_AZIENDA_UV"))) {
						UnitaArboreaDichiarataVO unitaArboreaDichiarataVO = new UnitaArboreaDichiarataVO();
						unitaArboreaDichiarataVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA_UV")));
						storicoParticellaVO.setUnitaArboreaDichiarataVO(unitaArboreaDichiarataVO);
					}
					
					if(Validator.isNotEmpty(rs.getString("ID_PARTICELLA_CERTIFICATA"))) 
					{
						ParticellaCertificataVO particellaCertificataVO = new ParticellaCertificataVO();
						particellaCertificataVO.setIdParticellaCertificata(new Long(rs.getLong("ID_PARTICELLA_CERTIFICATA")));
						particellaCertificataVO.setSupSeminabile(rs.getString("SUP_SEMINABILE"));
						particellaCertificataVO.setSupColtArboreaSpecializzata(rs.getString("SUP_COLT_ARBOREA_SPECIALIZZATA"));
						particellaCertificataVO.setDataSospensione(rs.getDate("DATA_SOSPENSIONE"));
						particellaCertificataVO.setMotivazioneGis(rs.getString("MOTIVAZIONE_GIS"));
						//Nuova Eleggibilita Inizio ************
            Vector<ParticellaCertElegVO> vPartCertEleg = null;
            if(rs.getBigDecimal("SUPERFICIE_ELEG") !=null)
            {
              vPartCertEleg = new Vector<ParticellaCertElegVO>();
              ParticellaCertElegVO partCertElegVO = new ParticellaCertElegVO();
              partCertElegVO.setSuperficie(rs.getBigDecimal("SUPERFICIE_ELEG"));
              partCertElegVO.setSuperficieEleggibileNetta(rs.getBigDecimal("SUPERFICIE_ELEG_NETTA"));
          
              vPartCertEleg.add(partCertElegVO);
            }
            particellaCertificataVO.setVParticellaCertEleg(vPartCertEleg);
            particellaCertificataVO.setSupGrafica(rs.getString("SUP_GRAFICA"));
            particellaCertificataVO.setSupUsoGrafica(rs.getString("SUP_USO_GRAFICA"));
            //Nuova Eleggibilta Fine ***********
						storicoParticellaVO.setParticellaCertificataVO(particellaCertificataVO);
					}
					
					
					//*****************Controlli P
	        storicoParticellaVO.setGisP30(rs.getString("P30"));
	        storicoParticellaVO.setGisP25(rs.getString("P25"));
	        storicoParticellaVO.setGisP26(rs.getString("P26"));
	        storicoParticellaVO.setSospesaGis(rs.getString("SOSPESA_GIS"));
	        //**************************
	        
	        
	        //*****************Biologico
          storicoParticellaVO.setBiologico(rs.getString("BIOLOGICO"));
          
          //**************************
          
          //************* Istanza di riesame ****************
          if(rs.getTimestamp("DATA_RICHIESTA") != null)
          {
            Vector<IstanzaRiesameVO> vIstanzaRiesame = new Vector<IstanzaRiesameVO>();
            IstanzaRiesameVO istanzaRiesameVO = new IstanzaRiesameVO();
            istanzaRiesameVO.setDataRichiesta(rs.getTimestamp("DATA_RICHIESTA"));
            istanzaRiesameVO.setDataEvasione(rs.getTimestamp("DATA_EVASIONE"));
            vIstanzaRiesame.add(istanzaRiesameVO);
            storicoParticellaVO.setvIstanzaRiesame(vIstanzaRiesame);
          }
          
          FoglioVO foglioVO = new FoglioVO();
          foglioVO.setFlagStabilizzazione(checkIntegerNull(rs.getString("FLAG_STABILIZZAZIONE")));
          storicoParticellaVO.setFoglioVO(foglioVO);
          
          storicoParticellaVO.setInNotifica(rs.getString("IN_NOTIFICA"));
          
					
					elencoParticelle.add(storicoParticellaVO);
				}
				
				// Secondo monitoraggio
				SolmrLogger.debug(this, "ConduzioneDichiarataDAO::searchParticelleDichiarateByParameters - In searchParticelleDichiarateByParameters method from the creation of query to final setting of parameters inside of Vector after resultset's while cicle, List of parameters: "+listOfParameters);
				
			  rs.close();
				stmt.close();
			}

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "searchParticelleDichiarateByParameters in ConduzioneDichiarataDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "searchParticelleDichiarateByParameters in ConduzioneDichiarataDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "searchParticelleDichiarateByParameters in ConduzioneDichiarataDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "searchParticelleDichiarateByParameters in ConduzioneDichiarataDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated searchParticelleDichiarateByParameters method in ConduzioneDichiarataDAO\n");
		return elencoParticelle;
	}
	
	/**
	 * Metodo che mi restituisce l'ultima data nella quale sono stati effettuati controlli
	 * di validità relativi alla tabella DB_CONDUZIONE_DICHIARATA
	 * 
	 * @param idAzienda
	 * @return java.lang.String
	 * @throws DataAccessException
	 */
	public java.lang.String getMaxDataEsecuzioneControlliConduzioneDichiarata(Long idAzienda) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getMaxDataEsecuzioneControlliConduzioneDichiarata method in ConduzioneDichiarataDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		java.lang.String dataEsecuzioneControlliDichiarati = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in getMaxDataEsecuzioneControlliConduzioneDichiarata method in ConduzioneDichiarataDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getMaxDataEsecuzioneControlliConduzioneDichiarata method in ConduzioneDichiarataDAO and it values: "+conn+"\n");

			String query = " SELECT MAX(CD.DATA_ESECUZIONE) AS DATA_ESECUZIONE" +
                           " FROM   DB_CONDUZIONE_DICHIARATA CD, " +
                           "        DB_UTE U " +
                           " WHERE  U.ID_AZIENDA = ? " +
                           " AND    CD.ID_UTE = U.ID_UTE ";

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getMaxDataEsecuzioneControlliConduzioneDichiarata method in ConduzioneDichiarataDAO: "+idAzienda+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());

			SolmrLogger.debug(this, "Executing getMaxDataEsecuzioneControlliConduzioneDichiarata: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) {
				dataEsecuzioneControlliDichiarati = rs.getString("DATA_ESECUZIONE");
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getMaxDataEsecuzioneControlliConduzioneDichiarata in ConduzioneDichiarataDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getMaxDataEsecuzioneControlliConduzioneDichiarata in ConduzioneDichiarataDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getMaxDataEsecuzioneControlliConduzioneDichiarata in ConduzioneDichiarataDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getMaxDataEsecuzioneControlliConduzioneDichiarata in ConduzioneDichiarataDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getMaxDataEsecuzioneControlliConduzioneDichiarata method in ConduzioneDichiarataDAO\n");
		return dataEsecuzioneControlliDichiarati;
	}
	
	/**
	 * Metodo che si occupa di recuperare l'elenco delle conduzioni dichiarate a partire
	 * dall'id_conduzione_particella
	 * 
	 * @param idConduzioneParticella
	 * @param idAzienda
	 * @param onlyActive
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.ConduzioneDichiarataVO[]
	 * @throws DataAccessException
	 */
	public ConduzioneDichiarataVO[] getListConduzioneDichiarataByIdConduzioneParticellaAndIdAzienda(Long idConduzioneParticella, Long idAzienda, boolean onlyActive, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListConduzioneDichiarataByIdConduzioneParticellaAndIdAzienda method in ConduzioneDichiarataDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<ConduzioneDichiarataVO> elencoConduzioniDichiarate = new Vector<ConduzioneDichiarataVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListConduzioneDichiarataByIdConduzioneParticellaAndIdAzienda method in ConduzioneDichiarataDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListConduzioneDichiarataByIdConduzioneParticellaAndIdAzienda method in ConduzioneDichiarata and it values: "+conn+"\n");

			String query = " SELECT CD.ID_CONDUZIONE_DICHIARATA, " +
						   "		CD.CODICE_FOTOGRAFIA_TERRENI, " +
						   "        CD.ID_UTE, " +
						   "        CD.ID_TITOLO_POSSESSO, " +
						   "        TTP.DESCRIZIONE, " +
						   "        CD.SUPERFICIE_CONDOTTA, " +
						   "        CD.PERCENTUALE_POSSESSO, " +
						   "        CD.NOTE, " +
						   "        CD.DATA_INIZIO_CONDUZIONE, " +
						   "        CD.DATA_FINE_CONDUZIONE, " +
						   "        CD.DATA_AGGIORNAMENTO, " +
						   "        CD.ID_UTENTE_AGGIORNAMENTO, " +
						   "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
					       "          || ' ' " + 
					       "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
					       "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
					       "         WHERE CD.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
						   "        AS DENOMINAZIONE, " +
						   "        (SELECT PVU.DENOMINAZIONE " +
						   "         FROM PAPUA_V_UTENTE_LOGIN PVU " +
						   "          WHERE CD.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
						   "        AS DESCRIZIONE_ENTE_APPARTENENZA, " +
						   "        CD.ID_PARTICELLA, " +
						   "        CD.ID_CONDUZIONE_PARTICELLA, " +
						   "        CD.ID_STORICO_PARTICELLA, " +
						   "        CD.ESITO_CONTROLLO, " +
						   "        CD.DATA_ESECUZIONE " +
						   " FROM   DB_CONDUZIONE_DICHIARATA CD, " +
						   "        DB_TIPO_TITOLO_POSSESSO TTP, " +
//						   "        PAPUA_V_UTENTE_LOGIN PVU, " +
						   "        DB_UTE U " +
						   " WHERE  CD.ID_CONDUZIONE_PARTICELLA = ? " +
						   " AND    U.ID_AZIENDA = ? " +
						   " AND    U.ID_UTE = CD.ID_UTE " +
						   " AND    CD.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO ";
						   //" AND    CD.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN ";
			if(onlyActive) {
				query += " AND CD.DATA_FINE_CONDUZIONE IS NULL ";
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

			SolmrLogger.debug(this, "Value of parameter 1 [ID_CONDUZIONE_PARTICELLA] in getListConduzioneDichiarataByIdConduzioneParticellaAndIdAzienda method in ConduzioneDichiarataDAO: "+idConduzioneParticella+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ID_AZIENDA] in getListConduzioneDichiarataByIdConduzioneParticellaAndIdAzienda method in ConduzioneDichiarataDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ONLY_ACTIVE] in getListConduzioneDichiarataByIdConduzioneParticellaAndIdAzienda method in ConduzioneDichiarataDAO: "+onlyActive+"\n");
			SolmrLogger.debug(this, "Value of parameter 4 [ORDINAMENTO] in getListConduzioneDichiarataByIdConduzioneParticellaAndIdAzienda method in ConduzioneDichiarataDAO: "+ordinamento+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idConduzioneParticella.longValue());
			stmt.setLong(2, idAzienda.longValue());

			SolmrLogger.debug(this, "Executing getListConduzioneDichiarataByIdConduzioneParticella: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				ConduzioneDichiarataVO conduzioneDichiarataVO = new ConduzioneDichiarataVO();
				conduzioneDichiarataVO.setIdConduzioneDichiarata(new Long(rs.getLong("ID_CONDUZIONE_DICHIARATA")));
				conduzioneDichiarataVO.setCodiceFotografiaTerreni(rs.getString("CODICE_FOTOGRAFIA_TERRENI"));
				if(Validator.isNotEmpty(rs.getString("ID_UTE"))) {
					conduzioneDichiarataVO.setIdUte(new Long(rs.getLong("ID_UTE")));
				}
				conduzioneDichiarataVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
				CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_TITOLO_POSSESSO")), rs.getString("DESCRIZIONE"));
				conduzioneDichiarataVO.setTitoloPossesso(code);
				conduzioneDichiarataVO.setSuperficieCondotta(rs.getString("SUPERFICIE_CONDOTTA"));
				conduzioneDichiarataVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
				conduzioneDichiarataVO.setNote(rs.getString("NOTE"));
				conduzioneDichiarataVO.setDataInizioConduzione(rs.getDate("DATA_INIZIO_CONDUZIONE"));
				conduzioneDichiarataVO.setDataFineConduzione(rs.getDate("DATA_FINE_CONDUZIONE"));
				conduzioneDichiarataVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
				conduzioneDichiarataVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				UtenteIrideVO utenteIrideVO = new UtenteIrideVO();
				utenteIrideVO.setDenominazione(rs.getString("DENOMINAZIONE"));
				utenteIrideVO.setDescrizioneEnteAppartenenza(rs.getString("DESCRIZIONE_ENTE_APPARTENENZA"));
				conduzioneDichiarataVO.setUtenteAggiornamento(utenteIrideVO);
				conduzioneDichiarataVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
				conduzioneDichiarataVO.setIdConduzioneParticella(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));
				conduzioneDichiarataVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
				conduzioneDichiarataVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
				conduzioneDichiarataVO.setDataEsecuzione(rs.getDate("DATA_ESECUZIONE"));
				elencoConduzioniDichiarate.add(conduzioneDichiarataVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListConduzioneDichiarataByIdConduzioneParticellaAndIdAzienda in ConduzioneDichiarataDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListConduzioneDichiarataByIdConduzioneParticellaAndIdAzienda in ConduzioneDichiarataDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListConduzioneDichiarataByIdConduzioneParticellaAndIdAzienda in ConduzioneDichiarataDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListConduzioneDichiarataByIdConduzioneParticellaAndIdAzienda in ConduzioneDichiarataDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListConduzioneDichiarataByIdConduzioneParticellaAndIdAzienda method in ConduzioneDichiarataDAO\n");
		if(elencoConduzioniDichiarate.size() == 0) {
			return (ConduzioneDichiarataVO[])elencoConduzioniDichiarate.toArray(new ConduzioneDichiarataVO[0]);
		}
		else {
			return (ConduzioneDichiarataVO[])elencoConduzioniDichiarate.toArray(new ConduzioneDichiarataVO[elencoConduzioniDichiarate.size()]);
		}
	}
	
	/**
	 * Metodo utilizzato pr effettuare la modifica del record relativo alla tabella
	 * DB_CONDUZIONE_DICHIARATA
	 * 
	 * @param ConduzioneDichiarataVO
	 * @throws DataAccessException
	 */
	public void updateConduzioneDichiarata(ConduzioneDichiarataVO conduzioneDichiarataVO) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating updateConduzioneDichiarata method in ConduzioneDichiarataDAO\n");
	    Connection conn = null;
	    PreparedStatement stmt = null;

	    try {
	    	SolmrLogger.debug(this, "Creating db-connection in updateConduzioneDichiarata method in ConduzioneDichiarataDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in updateConduzioneDichiarata method in ConduzioneDichiarataDAO and it values: "+conn+"\n");

			String query = " UPDATE DB_CONDUZIONE_DICHIARATA "+
	                       " SET    CODICE_FOTOGRAFIA_TERRENI = ?, " +
	                       "        ID_UTE = ?, " +
	                       "        ID_TITOLO_POSSESSO = ?, " +
	                       "        SUPERFICIE_CONDOTTA = ?, " +
	                       "        NOTE = ?, " +
	                       "        DATA_INIZIO_CONDUZIONE = ?, " +
	                       "        DATA_FINE_CONDUZIONE = ?, " +
	                       "        DATA_AGGIORNAMENTO = ?, " +
	                       "        ID_UTENTE_AGGIORNAMENTO = ?, " +
	                       "        ID_PARTICELLA = ?, " +
	                       "        ID_CONDUZIONE_PARTICELLA = ?, " +
	                       "        ID_STORICO_PARTICELLA = ?, " +
	                       "        ESITO_CONTROLLO = ?, " +
	                       "        DATA_ESECUZIONE = ? " +
	                       " WHERE  ID_CONDUZIONE_DICHIARATA = ? ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing updateConduzioneDichiarata: "+query);

			stmt.setString(1, conduzioneDichiarataVO.getCodiceFotografiaTerreni());
			SolmrLogger.debug(this, "Value of parameter 1 [CODICE_FOTOGRAFIA_TERRENI] in method updateConduzioneDichiarata in ConduzioneDichiarataDAO: "+conduzioneDichiarataVO.getCodiceFotografiaTerreni()+"\n");
			stmt.setLong(2, conduzioneDichiarataVO.getIdUte().longValue());
			SolmrLogger.debug(this, "Value of parameter 2 [ID_UTE] in method updateConduzioneDichiarata in ConduzioneDichiarataDAO: "+conduzioneDichiarataVO.getIdUte()+"\n");
			stmt.setLong(3, conduzioneDichiarataVO.getIdTitoloPossesso().longValue());
			SolmrLogger.debug(this, "Value of parameter 3 [ID_TITOLO_POSSESSO] in method updateConduzioneDichiarata in ConduzioneDichiarataDAO: "+conduzioneDichiarataVO.getIdTitoloPossesso().longValue()+"\n");
			stmt.setString(4, StringUtils.parseSuperficieField(conduzioneDichiarataVO.getSuperficieCondotta()));
			SolmrLogger.debug(this, "Value of parameter 4 [SUPERFICIE_CONDOTTA] in method updateConduzioneDichiarata in ConduzioneDichiarataDAO: "+StringUtils.parseSuperficieField(conduzioneDichiarataVO.getSuperficieCondotta())+"\n");
			stmt.setString(5, conduzioneDichiarataVO.getNote());
			SolmrLogger.debug(this, "Value of parameter 5 [NOTE] in method updateConduzioneDichiarata in ConduzioneDichiarataDAO: "+conduzioneDichiarataVO.getNote()+"\n");
			stmt.setDate(6, new java.sql.Date(conduzioneDichiarataVO.getDataInizioConduzione().getTime()));
			SolmrLogger.debug(this, "Value of parameter 6 [DATA_INIZIO_CONDUZIONE] in method updateConduzioneDichiarata in ConduzioneDichiarataDAO: "+new java.sql.Date(conduzioneDichiarataVO.getDataInizioConduzione().getTime())+"\n");
			if(conduzioneDichiarataVO.getDataFineConduzione() != null) {
				stmt.setTimestamp(7, new Timestamp(conduzioneDichiarataVO.getDataFineConduzione().getTime()));
				SolmrLogger.debug(this, "Value of parameter 7 [DATA_FINE_CONDUZIONE] in method updateConduzioneDichiarata in ConduzioneDichiarataDAO: "+new Timestamp(conduzioneDichiarataVO.getDataFineConduzione().getTime())+"\n");
			}
			else {
				 stmt.setString(7, null);
				 SolmrLogger.debug(this, "Value of parameter 7 [DATA_FINE_CONDUZIONE] in method updateConduzioneDichiarata in ConduzioneDichiarataDAO: "+null+"\n");
			}
			stmt.setTimestamp(8, new Timestamp(conduzioneDichiarataVO.getDataAggiornamento().getTime()));
			SolmrLogger.debug(this, "Value of parameter 8 [DATA_AGGIORNAMENTO] in method updateConduzioneDichiarata in ConduzioneDichiarataDAO: "+new Timestamp(conduzioneDichiarataVO.getDataAggiornamento().getTime())+"\n");
			stmt.setLong(9, conduzioneDichiarataVO.getIdUtenteAggiornamento().longValue());
			SolmrLogger.debug(this, "Value of parameter 9 [ID_UTENTE_AGGIORNAMENTO] in method updateConduzioneDichiarata in ConduzioneDichiarataDAO: "+conduzioneDichiarataVO.getIdUtenteAggiornamento().longValue()+"\n");
			stmt.setLong(10, conduzioneDichiarataVO.getIdParticella().longValue());
			SolmrLogger.debug(this, "Value of parameter 10 [ID_PARTICELLA] in method updateConduzioneDichiarata in ConduzioneDichiarataDAO: "+conduzioneDichiarataVO.getIdParticella().longValue()+"\n");
			stmt.setLong(11, conduzioneDichiarataVO.getIdConduzioneParticella().longValue());
			SolmrLogger.debug(this, "Value of parameter 11 [ID_CONDUZIONE_PARTICELLA] in method updateConduzioneDichiarata in ConduzioneDichiarataDAO: "+conduzioneDichiarataVO.getIdConduzioneParticella().longValue()+"\n");
			stmt.setLong(12, conduzioneDichiarataVO.getIdStoricoParticella().longValue());
			SolmrLogger.debug(this, "Value of parameter 12 [ID_STORICO_PARTICELLA] in method updateConduzioneDichiarata in ConduzioneDichiarataDAO: "+conduzioneDichiarataVO.getIdStoricoParticella().longValue()+"\n");
			stmt.setString(13, conduzioneDichiarataVO.getEsitoControllo());
			SolmrLogger.debug(this, "Value of parameter 13 [ESITO_CONTROLLO] in method updateConduzioneDichiarata in ConduzioneDichiarataDAO: "+conduzioneDichiarataVO.getEsitoControllo()+"\n");
			if(conduzioneDichiarataVO.getDataEsecuzione() != null) {
				stmt.setTimestamp(14, new Timestamp(conduzioneDichiarataVO.getDataEsecuzione().getTime()));
				SolmrLogger.debug(this, "Value of parameter 14 [DATA_ESECUZIONE] in method updateConduzioneDichiarata in ConduzioneDichiarataDAO: "+new Timestamp(conduzioneDichiarataVO.getDataEsecuzione().getTime())+"\n");
			}
			else {
				stmt.setString(14, null);
				SolmrLogger.debug(this, "Value of parameter 14 [DATA_ESECUZIONE] in method updateConduzioneDichiarata in ConduzioneDichiarataDAO: "+null+"\n");
			}
			stmt.setLong(15 , conduzioneDichiarataVO.getIdConduzioneDichiarata().longValue());
			SolmrLogger.debug(this, "Value of parameter 16 [ID_CONDUZIONE_DICHIARATA] in method updateConduzioneDichiarata in ConduzioneDichiarataDAO: "+conduzioneDichiarataVO.getIdConduzioneDichiarata().longValue()+"\n");

			stmt.executeUpdate();

			stmt.close();
	    }
	    catch(SQLException exc) {
	    	SolmrLogger.error(this, "updateConduzioneDichiarata in ConduzioneDichiarataDAO - SQLException: "+exc.getMessage());
	    	throw new DataAccessException(exc.getMessage());
	    }
	    catch (Exception ex) {
	    	SolmrLogger.error(this, "updateConduzioneDichiarata in ConduzioneDichiarataDAO - Generic Exception: "+ex);
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
	    		SolmrLogger.error(this, "updateConduzioneDichiarata in ConduzioneDichiarataDAO - SQLException while closing Statement and Connection: "+exc.getMessage());
	    		throw new DataAccessException(exc.getMessage());
	    	}
	    	catch(Exception ex) {
	    		SolmrLogger.error(this, "updateConduzioneDichiarata in ConduzioneDichiarataDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage());
	    		throw new DataAccessException(ex.getMessage());
	    	}
	    }
	    SolmrLogger.debug(this, "Invocated updateConduzioneDichiarata method in ConduzioneDichiarataDAO\n");
	}
	
	/**
	 * Metodo che mi restituisce un elenco di conduzioni dichiarate a partire
	 * dall'ID_STORICO_PARTICELLA
	 * 
	 * @param idStoricoParticella
	 * @return it.csi.solmr.dto.anag.terreni.ConduzioneDichiarataVO[]
	 * @throws DataAccessException
	 */
	public ConduzioneDichiarataVO[] getListConduzioneDichiarataByidStoricoParticella(Long idStoricoParticella) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListConduzioneDichiarataByidStoricoParticella method in ConduzioneDichiarataDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<ConduzioneDichiarataVO> elencoConduzioneDichiarata = new Vector<ConduzioneDichiarataVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListConduzioneDichiarataByidStoricoParticella method in ConduzioneDichiarataDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListConduzioneDichiarataByidStoricoParticella method in ConduzioneDichiarata and it values: "+conn+"\n");

			String query = " SELECT CD.ID_CONDUZIONE_DICHIARATA, " +
						   "		CD.CODICE_FOTOGRAFIA_TERRENI, " +
						   "        CD.ID_UTE, " +
						   "        CD.ID_TITOLO_POSSESSO, " +
						   "        TTP.DESCRIZIONE, " +
						   "        CD.SUPERFICIE_CONDOTTA, " +
						   "        CD.PERCENTUALE_POSSESSO, " +
						   "        CD.NOTE, " +
						   "        CD.DATA_INIZIO_CONDUZIONE, " +
						   "        CD.DATA_FINE_CONDUZIONE, " +
						   "        CD.DATA_AGGIORNAMENTO, " +
						   "        CD.ID_UTENTE_AGGIORNAMENTO, " +
						   "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
					       "          || ' ' " + 
					       "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
					       "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
					       "         WHERE CD.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
						   "        AS DENOMINAZIONE, " +
						   "       (SELECT PVU.DENOMINAZIONE " +
						   "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
						   "        WHERE CD.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
						   "        AS DESCRIZIONE_ENTE_APPARTENENZA, " +
						   "        CD.ID_PARTICELLA, " +
						   "        CD.ID_CONDUZIONE_PARTICELLA, " +
						   "        CD.ID_STORICO_PARTICELLA, " +
						   "        CD.ESITO_CONTROLLO, " +
						   "        CD.DATA_ESECUZIONE " +
						   " FROM   DB_CONDUZIONE_DICHIARATA CD, " +
						   "        DB_TIPO_TITOLO_POSSESSO TTP " +
						   //"        PAPUA_V_UTENTE_LOGIN PVU " +
						   " WHERE  CD.ID_STORICO_PARTICELLA = ? " +
						   " AND    CD.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO " ;
						   //" AND    CD.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN ";

			SolmrLogger.debug(this, "Value of parameter 1 [ID_STORICO_PARTICELLA] in getListConduzioneDichiarataByidStoricoParticella method in ConduzioneDichiarataDAO: "+idStoricoParticella+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idStoricoParticella.longValue());

			SolmrLogger.debug(this, "Executing getListConduzioneDichiarataByidStoricoParticella: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) {
				ConduzioneDichiarataVO conduzioneDichiarataVO = new ConduzioneDichiarataVO();
				conduzioneDichiarataVO.setIdConduzioneDichiarata(new Long(rs.getLong("ID_CONDUZIONE_DICHIARATA")));
				conduzioneDichiarataVO.setCodiceFotografiaTerreni(rs.getString("CODICE_FOTOGRAFIA_TERRENI"));
				if(Validator.isNotEmpty(rs.getString("ID_UTE"))) {
					conduzioneDichiarataVO.setIdUte(new Long(rs.getLong("ID_UTE")));
				}
				conduzioneDichiarataVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
				CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_TITOLO_POSSESSO")), rs.getString("DESCRIZIONE"));
				conduzioneDichiarataVO.setTitoloPossesso(code);
				conduzioneDichiarataVO.setSuperficieCondotta(rs.getString("SUPERFICIE_CONDOTTA"));
				conduzioneDichiarataVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
				conduzioneDichiarataVO.setNote(rs.getString("NOTE"));
				conduzioneDichiarataVO.setDataInizioConduzione(rs.getDate("DATA_INIZIO_CONDUZIONE"));
				conduzioneDichiarataVO.setDataFineConduzione(rs.getDate("DATA_FINE_CONDUZIONE"));
				conduzioneDichiarataVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
				conduzioneDichiarataVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				UtenteIrideVO utenteIrideVO = new UtenteIrideVO();
				utenteIrideVO.setDenominazione(rs.getString("DENOMINAZIONE"));
				utenteIrideVO.setDescrizioneEnteAppartenenza(rs.getString("DESCRIZIONE_ENTE_APPARTENENZA"));
				conduzioneDichiarataVO.setUtenteAggiornamento(utenteIrideVO);
				conduzioneDichiarataVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
				conduzioneDichiarataVO.setIdConduzioneParticella(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));
				conduzioneDichiarataVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
				conduzioneDichiarataVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
				conduzioneDichiarataVO.setDataEsecuzione(rs.getDate("DATA_ESECUZIONE"));
				elencoConduzioneDichiarata.add(conduzioneDichiarataVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListConduzioneDichiarataByidStoricoParticella in ConduzioneDichiarataDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListConduzioneDichiarataByidStoricoParticella in ConduzioneDichiarataDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListConduzioneDichiarataByidStoricoParticella in ConduzioneDichiarataDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListConduzioneDichiarataByidStoricoParticella in ConduzioneDichiarataDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListConduzioneDichiarataByidStoricoParticella method in ConduzioneDichiarataDAO\n");
		if(elencoConduzioneDichiarata.size() == 0) {
			return (ConduzioneDichiarataVO[])elencoConduzioneDichiarata.toArray(new ConduzioneDichiarataVO[0]);
		}
		else {
			return (ConduzioneDichiarataVO[])elencoConduzioneDichiarata.toArray(new ConduzioneDichiarataVO[elencoConduzioneDichiarata.size()]);
		}
	}
	
	/**
	 * Metodo che mi restituisce l'elenco delle particelle in tutte le sue componenti
	 * (DB_STORICO_PARTICELLA, DB_CONDUZIONE_DICHIARATA, DB_UTILIZZO_DICHIARATO) in relazione dei parametri di ricerca impostati,
	 * di un criterio di ordinamento e dell'azienda selezionata utilizzato per il brogliaccio del 
	 * nuovo territoriale 
	 * 
	 * @param filtriParticellareRicercaVO
	 * @param idAzienda
	 * @return java.util.Vector
	 * @throws DataAccessException
	 */
	public Vector<AnagParticellaExcelVO> searchParticelleDichiarateExcelByParameters(
	    FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda) 
	throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<AnagParticellaExcelVO> elencoParticelle = new Vector<AnagParticellaExcelVO>();
		String listOfParameters = "";
		// Creo l'oggetto Stop Watch per monitorare le operazioni eseguite all'interno del metodo

		try 
		{
			
			SolmrLogger.debug(this, "Creating db-connection in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO and it values: "+conn+"\n");
			
			String query = "" +
        "WITH PARTICELLE AS " +
        "  (SELECT DISTINCT PC.ID_PARTICELLA_CERTIFICATA, DC.DATA_INSERIMENTO_DICHIARAZIONE, DC.ANNO_CAMPAGNA " +
        "   FROM   DB_CONDUZIONE_DICHIARATA CD, " +
        "          DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "          DB_PARTICELLA_CERTIFICATA PC " +
        "   WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ?  " +
        "   AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        "   AND    CD.ID_PARTICELLA = PC.ID_PARTICELLA " +
        "   AND    PC.DATA_FINE_VALIDITA IS NULL " +
        "  ), " +
        "     PASCOLO_MAGRO AS " +
        "    (SELECT ID_PARTICELLA_CERTIFICATA, LISTAGG(DESCRIZIONE || ' (sup ' || TO_CHAR(SUPERFICIE,'9999999990D9999') || ' ha)',' / ') WITHIN GROUP (ORDER BY DESCRIZIONE) AS DESCRIZIONE " +
        "     FROM  (SELECT PM.ID_PARTICELLA_CERTIFICATA, PM.ID_FONTE, TF.DESCRIZIONE, PM.SUPERFICIE " +
        "            FROM   DB_ESITO_PASCOLO_MAGRO PM, " +
        "                   DB_TIPO_FONTE TF, " +
        "                   PARTICELLE P " +
        "            WHERE  PM.ID_PARTICELLA_CERTIFICATA = P.ID_PARTICELLA_CERTIFICATA " +
        "            AND    PM.DATA_FINE_VALIDITA IS NULL " +
        "            AND    PM.ANNO_CAMPAGNA = (SELECT MAX(ANNO_CAMPAGNA) " +
        "                                       FROM DB_ESITO_PASCOLO_MAGRO EPM " +
        "                                       WHERE EPM.ID_PARTICELLA_CERTIFICATA = PM.ID_PARTICELLA_CERTIFICATA" +
        "                                       AND EPM.ANNO_CAMPAGNA <= P.ANNO_CAMPAGNA) " + 
        "            AND    PM.ID_FONTE = TF.ID_FONTE) " +
        "     GROUP BY ID_PARTICELLA_CERTIFICATA), " +
        "     START_RIESAME AS " +
        "                (SELECT IR.DATA_RICHIESTA, " +
        "                        (CASE " +
        "                         WHEN (NVL(IR.DATA_EVASIONE, SYSDATE)) <  DCIR.DATA_INSERIMENTO_DICHIARAZIONE " +
        "                         THEN IR.DATA_EVASIONE " +            
        "                        END) AS DATA_EVASIONE, " +                
        "                        IR.ID_PARTICELLA " +
        "                 FROM   DB_ISTANZA_RIESAME IR, " +
        "                        DB_DICHIARAZIONE_CONSISTENZA DCIR " +
        "                        WHERE IR.DATA_RICHIESTA = ( " +
        "                                                   SELECT MAX(IRTMP.DATA_RICHIESTA) " +
        "                                                   FROM   DB_ISTANZA_RIESAME IRTMP, " +                                    
        "                                                          DB_DICHIARAZIONE_CONSISTENZA DCIRTMP " +
        "                                                   WHERE  IRTMP.ID_AZIENDA = IR.ID_AZIENDA " +
        "                                                   AND    IRTMP.ID_PARTICELLA = IR.ID_PARTICELLA " +
        "                                                   AND    DCIRTMP.ID_DICHIARAZIONE_CONSISTENZA = DCIR.ID_DICHIARAZIONE_CONSISTENZA " +
        "                                                   AND    IRTMP.DATA_ANNULLAMENTO IS NULL " +
        "                                                   AND    IRTMP.DATA_RICHIESTA <   DCIRTMP.DATA_INSERIMENTO_DICHIARAZIONE " +
        "                                                  ) " +
        "                        AND   IR.ID_AZIENDA = ? " +
        "                        AND   DCIR.ID_DICHIARAZIONE_CONSISTENZA = ? ), " +
        "PARTICELLA_CERT AS " +
        "  (SELECT PC.ID_PARTICELLA_CERTIFICATA, " +
        "          PC.SUP_SEMINABILE, " +
        "          PC.SUP_COLT_ARBOREA_SPECIALIZZATA, " +
        "          PC.SUP_USO_GRAFICA, " +
        "          CD.ID_STORICO_PARTICELLA " +
        "   FROM   DB_CONDUZIONE_DICHIARATA CD, " +
        "          DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "          DB_PARTICELLA_CERTIFICATA PC " +
        "   WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "   AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        "   AND    CD.ID_PARTICELLA = PC.ID_PARTICELLA  " +
        "   AND    PC.DATA_INIZIO_VALIDITA <= DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "   AND    NVL(PC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "   GROUP BY " +
        "          PC.ID_PARTICELLA_CERTIFICATA, " +
        "          PC.SUP_SEMINABILE, " +
        "          PC.SUP_COLT_ARBOREA_SPECIALIZZATA, " +
        "          PC.SUP_USO_GRAFICA," +
        "          CD.ID_STORICO_PARTICELLA), " +
			  "  BIOLOGICO AS " +
			  "    (SELECT MAX(PB.ID_PARTICELLA_BIO) AS ID_PARTICELLA_BIO, PB.ID_PARTICELLA " +
        "     FROM   DB_PARTICELLA_BIO PB, " +
        "            DB_DICHIARAZIONE_CONSISTENZA DCBIO " + 
        "     WHERE  PB.ID_AZIENDA = ? " + 
        "     AND    DCBIO.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "     AND    PB.DATA_INIZIO_VALIDITA < DCBIO.DATA_INSERIMENTO_DICHIARAZIONE " + 
        "     AND    NVL(PB.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > DCBIO.DATA_INSERIMENTO_DICHIARAZIONE " +
        "     GROUP BY PB.ID_PARTICELLA) ";		
			
			query += "" +
    	      " SELECT CD.ID_CONDUZIONE_DICHIARATA, " +
            "        CD.ESITO_CONTROLLO, " +
            "        CD.ID_PARTICELLA, " +
            "        SP.COMUNE, " +
            "        C.DESCOM, " +
            "        P.SIGLA_PROVINCIA, " +
            "        SP.SEZIONE, " +
            "        SP.FOGLIO, " +
            "        SP.PARTICELLA, " +
            "        SP.SUBALTERNO, " +
            "        SP.SUP_CATASTALE, " +
            "        SP.SUPERFICIE_GRAFICA, " +
            "        SP.ID_ZONA_ALTIMETRICA, " +
            "        TZA.DESCRIZIONE AS DESC_ZONA_ALT, " +
        //    "        TAM.DESCRIZIONE AS DESC_AREA_M, " +
        //    "        SP.ID_AREA_C, " +
            "        SP.FLAG_IRRIGABILE, " +
            "        TTP.ID_TITOLO_POSSESSO, " +
            "        TTP.DESCRIZIONE AS DESC_POSSESSO, " +
            "        CD.SUPERFICIE_CONDOTTA, " +
            "        CD.PERCENTUALE_POSSESSO, " +
            "        CD.DATA_INIZIO_CONDUZIONE, " +
            "        CD.DATA_FINE_CONDUZIONE, " +
            "        CD.CODICE_FOTOGRAFIA_TERRENI, " +
            "        CD.SUPERFICIE_AGRONOMICA, ";
			
			// Se l'utente vuole estrarre qualunque o un determinato uso del suolo
			if(filtriParticellareRicercaVO.getIdUtilizzo().intValue() != 0) 
			{
				query += 
				    "        UD.ID_UTILIZZO_DICHIARATO, " +
				    "        UD.ID_CATALOGO_MATRICE, " +
		        "        TU.CODICE AS COD_PRIMARIO, " +
		        "        TU.DESCRIZIONE AS DESC_PRIMARIO, " +	
		        "        TV.DESCRIZIONE AS DESC_VARIETA, " +
		        "        TV.CODICE_VARIETA AS COD_VARIETA, " +
		        "        UD.SUPERFICIE_UTILIZZATA, " +
		        "        RCM2.ID_UTILIZZO AS ID_UTILIZZO_SECONDARIO, " +
		        "        TU2.CODICE AS COD_SECONDARIO, " +
		        "        TU2.DESCRIZIONE AS DESC_SECONDARIO, " +
		        "        TV2.DESCRIZIONE AS VAR_SECONDARIA, " +
		        "        TV2.CODICE_VARIETA AS COD_VAR_SECONDARIA, " +
		        "        UD.SUP_UTILIZZATA_SECONDARIA, " +
		        "        TDU.CODICE_DETTAGLIO_USO AS COD_DETT_USO_PRIM, " +
	          "        TDU.DESCRIZIONE_DETTAGLIO_USO AS DESC_DETT_USO_PRIM, " +
	          "        TDU2.CODICE_DETTAGLIO_USO AS COD_DETT_USO_SEC, " +
	          "        TDU2.DESCRIZIONE_DETTAGLIO_USO AS DESC_DETT_USO_SEC, " +
	          "        TDE.CODICE_DESTINAZIONE AS COD_DESTINAZIONE_PRIM, " +
	          "        TDE.DESCRIZIONE_DESTINAZIONE AS DESC_DESTINAZIONE_PRIM, " +
	          "        TDE2.CODICE_DESTINAZIONE AS COD_DESTINAZIONE_SEC, " +
	          "        TDE2.DESCRIZIONE_DESTINAZIONE AS DESC_DESTINAZIONE_SEC, " +
	          "        TQU.CODICE_QUALITA_USO AS COD_QUAL_USO_PRIM, " +
	          "        TQU.DESCRIZIONE_QUALITA_USO AS DESC_QUAL_USO_PRIM, " +
	          "        TQU2.CODICE_QUALITA_USO AS COD_QUAL_USO_SEC, " +
	          "        TQU2.DESCRIZIONE_QUALITA_USO AS DESC_QUAL_USO_SEC, " +
	          "        TPS.CODICE AS COD_PER_SEM_PRIM, " +
	          "        TPS.DESCRIZIONE AS DESC_PER_SEM_PRIM, " +
	          "        TPS2.CODICE AS COD_PER_SEM_SEC, " +
	          "        TPS2.DESCRIZIONE AS DESC_PER_SEM_SEC, " +
	          "        TSS.CODICE_SEMINA AS COD_SEM_PRIM, " +
	          "        TSS.DESCRIZIONE_SEMINA AS DESC_SEM_PRIM, " +
	          "        TSS2.CODICE_SEMINA AS COD_SEM_SEC, " +
	          "        TSS2.DESCRIZIONE_SEMINA AS DESC_SEM_SEC, " +
	          "        TPM.CODICE_PRATICA_MANTENIMENTO, " +
	          "        TPM.DESCRIZIONE_PRATICA_MANTENIMEN, " +
	          "        TFA.CODICE_FASE_ALLEVAMENTO, " +
	          "        TFA.DESCRIZIONE_FASE_ALLEVAMENTO, " +
	          "        UD.DATA_INIZIO_DESTINAZIONE, " +
	          "        UD.DATA_FINE_DESTINAZIONE, " +
	          "        UD.DATA_INIZIO_DESTINAZIONE_SEC, " +
	          "        UD.DATA_FINE_DESTINAZIONE_SEC, " +
	          "        UD.ID_TIPO_EFA, " +
	          "        TEF.DESCRIZIONE_TIPO_EFA, " +
	          "        UM.DESCRIZIONE AS DESC_UNITA_MIS_EFA, " +
	          "        UD.VALORE_ORIGINALE, " +
	          "        UD.VALORE_DOPO_CONVERSIONE, " +
	          "        UD.VALORE_DOPO_PONDERAZIONE, ";
			}
			// Se invece desidera estrarre solo le particelle senza uso del suolo
			else 
			{
				query += 
				    "        -1 AS ID_UTILIZZO_DICHIARATO, " +
				    "        -1 AS ID_CATALOGO_MATRICE, " +
    	 		  "        NULL AS COD_PRIMARIO, " +
    	 		  "        NULL AS DESC_PRIMARIO, " +
    	 		  "        NULL AS DESC_VARIETA, " +
    	 		  "        NULL AS COD_VAR_SECONDARIA, " +
    	 		  "        (CD.SUPERFICIE_CONDOTTA - SUM(NVL(UD.SUPERFICIE_UTILIZZATA,0))) SUPERFICIE_UTILIZZATA, " +
    	 		  "        -1 AS ID_UTILIZZO_SECONDARIO, " +
    	 		  "        NULL AS COD_SECONDARIO, " +
    	 		  "        NULL AS DESC_SECONDARIO, " +
    	 		  "        NULL AS VAR_SECONDARIA, " +
    	 		  "        NULL AS COD_VARIETA, " +
    	 		  "        0 AS SUP_UTILIZZATA_SECONDARIA, " +
    	 		  "        NULL AS COD_DETT_USO_PRIM, " +
            "        NULL AS DESC_DETT_USO_PRIM, " +
            "        NULL AS COD_DETT_USO_SEC, " +
            "        NULL AS DESC_DETT_USO_SEC, " +
            "        NULL AS COD_DESTINAZIONE_PRIM, " +
            "        NULL AS DESC_DESTINAZIONE_PRIM, " +
            "        NULL AS COD_DESTINAZIONE_SEC, " +
            "        NULL AS DESC_DESTINAZIONE_SEC, " +
            "        NULL AS COD_QUAL_USO_PRIM, " +
            "        NULL AS DESC_QUAL_USO_PRIM, " +
            "        NULL AS COD_QUAL_USO_SEC, " +
            "        NULL AS DESC_QUAL_USO_SEC, " +
            "        NULL AS COD_PER_SEM_PRIM, " +
            "        NULL AS DESC_PER_SEM_PRIM, " +
            "        NULL AS COD_PER_SEM_SEC, " +
            "        NULL AS DESC_PER_SEM_SEC, " +
            "        NULL AS COD_SEM_PRIM, " +
            "        NULL AS DESC_SEM_PRIM, " +
            "        NULL AS COD_SEM_SEC, " +
            "        NULL AS DESC_SEM_SEC, " +
            "        NULL AS CODICE_PRATICA_MANTENIMENTO, " +
            "        NULL AS DESCRIZIONE_PRATICA_MANTENIMEN, " +
            "        NULL AS CODICE_FASE_ALLEVAMENTO, " +
            "        NULL AS DESCRIZIONE_FASE_ALLEVAMENTO, " +
            "        NULL AS DATA_INIZIO_DESTINAZIONE, " +
            "        NULL AS DATA_FINE_DESTINAZIONE, " +
            "        NULL AS DATA_INIZIO_DESTINAZIONE_SEC, " +
            "        NULL AS DATA_FINE_DESTINAZIONE_SEC, " +
            "        -1 AS ID_TIPO_EFA, " +
            "        NULL AS DESCRIZIONE_TIPO_EFA, " +
            "        NULL AS DESC_UNITA_MIS_EFA, " +
            "        NULL AS VALORE_ORIGINALE, " +
            "        NULL AS VALORE_DOPO_CONVERSIONE, " +
            "        NULL AS VALORE_DOPO_PONDERAZIONE, "; 
			}
			
			
			query += ""+
      			"        SP.ID_CASO_PARTICOLARE, " +
            "        TCP.DESCRIZIONE AS DESC_PARTICOLARE, " +
            "        CD.ID_UTE, " +
            "        UD.ANNO_IMPIANTO, " +
            "        UD.NUMERO_PIANTE_CEPPI, " +
            "        U.COMUNE AS COM_UTE, " +
            "        C2.DESCOM AS DESC_COM_UTE, " +
            "        F.ID_FOGLIO, " +
         //   "        F.ID_AREA_E, " +
            "        PC.SUP_SEMINABILE, " +
            "        PC.SUP_COLT_ARBOREA_SPECIALIZZATA, " +
            "        PC.SUP_USO_GRAFICA, " +
            "        PC.ID_PARTICELLA_CERTIFICATA, " +
            "        TV.ID_VARIETA, " +
            "        AA.CUAA, " +
            "        AA.DENOMINAZIONE, " +
            "        DC.DATA_INSERIMENTO_DICHIARAZIONE, " +
            "        DC.ANNO_CAMPAGNA, " +
            "        DC.ID_DICHIARAZIONE_CONSISTENZA, " +
            "        PART_BIO.SUP_BIOLOGICO, " +
            "        PART_BIO.SUP_CONVENZIONALE, " +
            "        PART_BIO.SUP_IN_CONVERSIONE, " +
            "        PART_BIO.DATA_INIZIO_CONVERSIONE, " +
            "        PART_BIO.DATA_FINE_CONVERSIONE, " +
            "        PAS.DESCRIZIONE AS DESC_FONTE_REG_PASCOLI, " +
            "        SRI.DATA_RICHIESTA, " +    
            "        SRI.DATA_EVASIONE, " +
            "       (CASE WHEN EXISTS " +
            "              (SELECT NE.ID_NOTIFICA_ENTITA " + 
            "               FROM   DB_NOTIFICA_ENTITA NE, " +
            "                      DB_TIPO_ENTITA TE " +
            "               WHERE  TE.CODICE_TIPO_ENTITA = 'P' " +
            "               AND    TE.ID_TIPO_ENTITA = NE.ID_TIPO_ENTITA " +
            "               AND    NE.IDENTIFICATIVO = CD.ID_PARTICELLA " +
            "               AND    NE.ID_DICHIARAZIONE_CONSISTENZA = DC.ID_DICHIARAZIONE_CONSISTENZA " +
            "               AND    NE.DATA_FINE_VALIDITA IS NULL) " +
            "         THEN 'NOTIFICA' " +
            "         END) AS IN_NOTIFICA ";
			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getvTipoArea()))
      {
        for(int t=0;t<filtriParticellareRicercaVO.getvTipoArea().size();t++)
        {
          TipoAreaVO tipoAreaVO = filtriParticellareRicercaVO.getvTipoArea().get(t);
          if("S".equalsIgnoreCase(tipoAreaVO.getFlagEsclusivoFoglio()))
          {
            query += 
              "  , (SELECT TVA1.DESCRIZIONE " +
              "     FROM   DB_R_FOGLIO_AREA  RFA1, " +
              "            DB_TIPO_VALORE_AREA TVA1 " +
              "     WHERE  RFA1.ID_FOGLIO = F.ID_FOGLIO " +
              "     AND    RFA1.ID_TIPO_VALORE_AREA = TVA1.ID_TIPO_VALORE_AREA " + 
              "     AND    TVA1.ID_TIPO_AREA = "+tipoAreaVO.getIdTipoArea()+" " +
              "     AND    TVA1.DATA_INIZIO_VALIDITA  <= DC.DATA_INSERIMENTO_DICHIARAZIONE " +
              "     AND    NVL(TVA1.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) >= DC.DATA_INSERIMENTO_DICHIARAZIONE ) " +
              "     AS DESC_TIPO_VALORE_AREA"+t+"   ";          
          }
          else
          {
            query += 
                "  , (SELECT TVA1.DESCRIZIONE " +
                "     FROM   DB_R_PARTICELLA_AREA  RPA1, " +
                "            DB_TIPO_VALORE_AREA TVA1 " +
                "     WHERE  RPA1.ID_PARTICELLA = CD.ID_PARTICELLA " +
                "     AND    RPA1.ID_TIPO_VALORE_AREA = TVA1.ID_TIPO_VALORE_AREA " + 
                "     AND    TVA1.ID_TIPO_AREA = "+tipoAreaVO.getIdTipoArea()+" " +
                "     AND    TVA1.DATA_INIZIO_VALIDITA  <= DC.DATA_INSERIMENTO_DICHIARAZIONE " +
                "     AND    NVL(TVA1.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) >= DC.DATA_INSERIMENTO_DICHIARAZIONE ) " +
                "     AS DESC_TIPO_VALORE_AREA"+t+"   ";      
          }
          
          
        }
      }
			query +=
            " FROM   DB_UTE U, " +
            "        DB_STORICO_PARTICELLA SP, " +    
            "        DB_CONDUZIONE_DICHIARATA CD, " +
            "        DB_DICHIARAZIONE_CONSISTENZA DC, " +
            "        COMUNE C, " +
            "        DB_TIPO_TITOLO_POSSESSO TTP, " +
            "        DB_UTILIZZO_DICHIARATO UD, " +
            "        DB_R_CATALOGO_MATRICE RCM, " +
            "        DB_R_CATALOGO_MATRICE RCM2, " +
            "        DB_TIPO_UTILIZZO TU, " +
            "        DB_TIPO_VARIETA TV, " +
            "        DB_TIPO_UTILIZZO TU2, " +  
            "        DB_TIPO_VARIETA TV2, " +
            "        DB_TIPO_DESTINAZIONE TDE, " +
            "        DB_TIPO_DESTINAZIONE TDE2, " +
            "        DB_TIPO_DETTAGLIO_USO TDU, " +
            "        DB_TIPO_DETTAGLIO_USO TDU2, " +
            "        DB_TIPO_QUALITA_USO TQU, " +
            "        DB_TIPO_QUALITA_USO TQU2, " +
            "        DB_TIPO_PERIODO_SEMINA TPS, " +
            "        DB_TIPO_PERIODO_SEMINA TPS2, " +
            "        DB_TIPO_SEMINA TSS, " +
            "        DB_TIPO_SEMINA TSS2, " +
            "        DB_TIPO_PRATICA_MANTENIMENTO TPM, " +
            "        DB_TIPO_FASE_ALLEVAMENTO TFA, " +
            "        DB_TIPO_EFA TEF, " +
            "        DB_UNITA_MISURA UM, " +
            "        PROVINCIA P, " +
            "        DB_TIPO_CASO_PARTICOLARE TCP, " +
            "        COMUNE C2, " +
            "        DB_FOGLIO F, " +
            "        PARTICELLA_CERT PC, " +
            "        DB_TIPO_ZONA_ALTIMETRICA TZA, " +
       //     "        DB_TIPO_AREA_M TAM, " +
            "        DB_ANAGRAFICA_AZIENDA AA, " +
            "        BIOLOGICO BIO, " +
            "        DB_PARTICELLA_BIO PART_BIO, " +
            "        PASCOLO_MAGRO PAS, " +
            "        START_RIESAME SRI ";
			
			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdMacroUso())) 
      {
			  query += "" +
			  		"       ,DB_TIPO_MACRO_USO TMU, " +
            "        DB_TIPO_MACRO_USO_VARIETA TMUV ";
      }
			
			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo())) 
      {
        query += 
            "       ,DB_DICHIARAZIONE_SEGNALAZIONE DS ";
      }
			
		  //Se è attivo almeno uno dei due filtri tra solo asservite e solo conferite deve andare in join con la
      //DB_DICHIARAZIONE_CONSISTENZA
      if(filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S)
          || filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S))  
      {
        query += 
            "      ,DB_DICHIARAZIONE_CONSISTENZA DICH_CONS, " +
            "       DB_CONDUZIONE_DICHIARATA CD2 ";
      }
      
      //Tabelle per la ricerca sui documenti
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()) 
          || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
      {
        query += "" +
            "   ,DB_DOCUMENTO_CONDUZIONE DOCC, " +
            "    DB_DOCUMENTO DOC1, " +
            "    DB_TIPO_DOCUMENTO TDOC, " +
            "    DB_DOCUMENTO_CATEGORIA DOCCAT ";
      }
      
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoValoreArea()))
      {
        if("S".equalsIgnoreCase(filtriParticellareRicercaVO.getFlagFoglio()))
        {
          query += "" +
            "   ,DB_R_FOGLIO_AREA RFA ";
        }
        else
        {
          query += "" +
            "   ,DB_R_PARTICELLA_AREA RPA ";            
        }         
      }
			
			query += ""+
			      " WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
            " AND    U.ID_AZIENDA = ? " +
            " AND    TRUNC(U.DATA_INIZIO_ATTIVITA) <= TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
            " AND    TRUNC(NVL(U.DATA_FINE_ATTIVITA, TO_DATE('31/12/9999', 'DD/MM/YYYY'))) > TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
            " AND    U.ID_AZIENDA = AA.ID_AZIENDA " +
            " AND    AA.DATA_FINE_VALIDITA IS NULL " +
            " AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
            " AND    CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +  
            " AND    SP.COMUNE = C.ISTAT_COMUNE " +
            " AND    SP.ID_CASO_PARTICOLARE = TCP.ID_CASO_PARTICOLARE(+) " +
            " AND    SP.ID_STORICO_PARTICELLA = PC.ID_STORICO_PARTICELLA (+) " +
            " AND    SP.ID_PARTICELLA = SRI.ID_PARTICELLA (+) " +
            " AND    SP.ID_ZONA_ALTIMETRICA = TZA.ID_ZONA_ALTIMETRICA(+) " +
         //   " AND    SP.ID_AREA_M = TAM.ID_AREA_M(+) " +
            " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) " +
            " AND    CD.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO " +
            " AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA(+) " +
            " AND    UD.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE(+) " +
            " AND    UD.ID_CATALOGO_MATRICE_SECONDARIO = RCM2.ID_CATALOGO_MATRICE(+) " +
            " AND    RCM.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
            " AND    RCM.ID_VARIETA = TV.ID_VARIETA(+) " +
            " AND    RCM2.ID_UTILIZZO = TU2.ID_UTILIZZO(+) " +
            " AND    RCM2.ID_VARIETA = TV2.ID_VARIETA(+) " +
            " AND    RCM.ID_TIPO_DETTAGLIO_USO = TDU.ID_TIPO_DETTAGLIO_USO(+) " +
            " AND    RCM2.ID_TIPO_DETTAGLIO_USO = TDU2.ID_TIPO_DETTAGLIO_USO(+) " +
            " AND    RCM.ID_TIPO_DESTINAZIONE = TDE.ID_TIPO_DESTINAZIONE (+) " +
            " AND    RCM.ID_TIPO_QUALITA_USO = TQU.ID_TIPO_QUALITA_USO (+) " +
            " AND    RCM2.ID_TIPO_DESTINAZIONE = TDE2.ID_TIPO_DESTINAZIONE (+) " +
            " AND    RCM2.ID_TIPO_QUALITA_USO = TQU2.ID_TIPO_QUALITA_USO (+) " +
            " AND    UD.ID_TIPO_PERIODO_SEMINA = TPS.ID_TIPO_PERIODO_SEMINA(+) " +
            " AND    UD.ID_TIPO_PERIODO_SEMINA_SECOND = TPS2.ID_TIPO_PERIODO_SEMINA(+) " +
            " AND    UD.ID_SEMINA = TSS.ID_SEMINA(+) " +
            " AND    UD.ID_SEMINA_SECONDARIA = TSS2.ID_SEMINA(+) " +
            " AND    UD.ID_PRATICA_MANTENIMENTO = TPM.ID_PRATICA_MANTENIMENTO (+) " +
            " AND    UD.ID_FASE_ALLEVAMENTO = TFA.ID_FASE_ALLEVAMENTO (+) " +
            " AND    UD.ID_TIPO_EFA = TEF.ID_TIPO_EFA(+) " +
            " AND    TEF.ID_UNITA_MISURA = UM.ID_UNITA_MISURA(+) " +
            " AND    C2.ISTAT_COMUNE = U.COMUNE " +
            " AND    CD.ID_PARTICELLA = BIO.ID_PARTICELLA(+) " +
            " AND    BIO.ID_PARTICELLA_BIO = PART_BIO.ID_PARTICELLA_BIO(+) " +
            " AND    PC.ID_PARTICELLA_CERTIFICATA = PAS.ID_PARTICELLA_CERTIFICATA(+) ";
			
			if(filtriParticellareRicercaVO.getIdUtilizzo().intValue() != 0) 
      {			  
			  query +="" +
			      " AND    U.ID_UTE = CD.ID_UTE ";
      }
			
			
			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdMacroUso())) 
      {
        query += 
             " AND UD.ID_CATALOGO_MATRICE = TMUV.ID_CATALOGO_MATRICE " +
             " AND TMUV.ID_MACRO_USO = TMU.ID_MACRO_USO " +
             " AND TMUV.DATA_INIZIO_VALIDITA <= ? " +
             " AND NVL(TMUV.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? " + 
             " AND TMU.ID_MACRO_USO = ? ";
      }
			
			
			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo())) 
      {
        query += "" +
        		 " AND DS.ID_AZIENDA = ? " +
             " AND DS.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
             " AND DS.ID_DICHIARAZIONE_CONSISTENZA = ? " +
             " AND DS.ID_CONTROLLO = ? ";
      }
			
			// Se l'utente ha indicato l'ute di riferimento
			if(filtriParticellareRicercaVO.getIdUte() != null) {
				query += " AND U.ID_UTE = ? ";
			}
      
      query += "" +
          " AND    SP.COMUNE = F.COMUNE(+) " +
          " AND    NVL( SP.SEZIONE,'-') = NVL( F.SEZIONE(+),'-') " +
          " AND    SP.FOGLIO = F.FOGLIO(+) ";
      
      
      //Combo documenti
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()) 
          || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento())
          || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
      {
        query += " AND CD.ID_CONDUZIONE_PARTICELLA = DOCC.ID_CONDUZIONE_PARTICELLA " +
                 " AND DOCC.ID_DOCUMENTO = DOC1.ID_DOCUMENTO " +
                 " AND DOC1.EXT_ID_DOCUMENTO = TDOC.ID_DOCUMENTO " +
                 " AND TDOC.ID_DOCUMENTO = DOCCAT.ID_DOCUMENTO " +
                 " AND DOCCAT.ID_CATEGORIA_DOCUMENTO = ? " +
                 " AND DOC1.DATA_INIZIO_VALIDITA <= DC.DATA_INSERIMENTO_DICHIARAZIONE " +
                 " AND NVL(DOC1.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) >= DC.DATA_INSERIMENTO_DICHIARAZIONE ";
        
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
        {
          query += " AND TDOC.ID_DOCUMENTO = ? ";
        }
        
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
        {
          query += " AND DOC1.ID_DOCUMENTO = ? ";
        }
        
      }
      
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoValoreArea()))
      {
        if("S".equalsIgnoreCase(filtriParticellareRicercaVO.getFlagFoglio()))
        {
          query += "" +
            "AND F.ID_FOGLIO = RFA.ID_FOGLIO " +
            "AND RFA.ID_TIPO_VALORE_AREA = ? ";
        }
        else
        {
          query += "" +
            "AND SP.ID_PARTICELLA = RPA.ID_PARTICELLA " +
            "AND RPA.ID_TIPO_VALORE_AREA = ? ";            
        }         
      }
      
      //Notifiche
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipologiaNotifica())
          || (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCategoriaNotifica())))
      {
        query +=   " AND  EXISTS  (SELECT NE.ID_NOTIFICA_ENTITA " + 
                   "               FROM   DB_NOTIFICA_ENTITA NE, " +
                   "                      DB_NOTIFICA NO, " +
                   "                      DB_TIPO_ENTITA TE " +
                   "               WHERE  TE.CODICE_TIPO_ENTITA = 'P' " +
                   "               AND    TE.ID_TIPO_ENTITA = NE.ID_TIPO_ENTITA " +
                   "               AND    NE.IDENTIFICATIVO = SP.ID_PARTICELLA " +
                   "               AND    NE.ID_NOTIFICA = NO.ID_NOTIFICA " +
                   "               AND    NE.ID_DICHIARAZIONE_CONSISTENZA = DC.ID_DICHIARAZIONE_CONSISTENZA ";
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipologiaNotifica()))
        {
          query += "               AND    NO.ID_TIPOLOGIA_NOTIFICA = ? ";          
        }
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCategoriaNotifica()))
        {
          query += "               AND    NO.ID_CATEGORIA_NOTIFICA = ? ";          
        }
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getFlagNotificheChiuse()))
        {
          query +=   "AND    (NE.DATA_FINE_VALIDITA IS NULL " +
                     "        OR NE.DATA_FINE_VALIDITA = (SELECT MAX(NE1.DATA_FINE_VALIDITA) " +
                     "                                    FROM   DB_NOTIFICA_ENTITA NE1 " +
                     "                                    WHERE  NE1.IDENTIFICATIVO = NE.IDENTIFICATIVO " +
                     "                                    AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA " +
                     "                                    AND    NE1.ID_DICHIARAZIONE_CONSISTENZA = NE.ID_DICHIARAZIONE_CONSISTENZA)) )";
        }
        else
        {
          query +=   " AND    NE.DATA_FINE_VALIDITA IS NULL )";
        }
        
      }
			
			
			// Se l'utente ha indicato il comune di riferimento
			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIstatComune())) {
				query += " AND SP.COMUNE = ? ";
			}
			// Se l'utente ha indicato la sezione di riferimento
			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getSezione())) {
				query += " AND SP.SEZIONE = ? ";
			}
			// Se l'utente ha indicato il foglio di riferimento
			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getFoglio())) {
				query += " AND SP.FOGLIO = ? ";
			}
			// Se l'utente ha indicato la particella di riferimento
			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getParticella())) {
				query += " AND SP.PARTICELLA = ? ";
			}
			// Se l'utente ha indicato il subalterno di riferimento
			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getSubalterno())) {
				query += " AND SP.SUBALTERNO = ? ";
			}
      // Se l'utente ha indicato il tipo Zona Altimetrica
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdZonaAltimetrica())) 
      {
        query += " AND SP.ID_ZONA_ALTIMETRICA = ? ";
      }
      // Se l'utente ha indicato il tipo Caso Particolare
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCasoParticolare())) 
      {
        query += " AND SP.ID_CASO_PARTICOLARE = ? ";
      }
			// Se l'utente invece ha specificato un particolare uso del suolo
			if(filtriParticellareRicercaVO.getIdUtilizzo().longValue() > 0) {
				// Se non è stato indicato nei filtri di ricerca se l'utilizzo selezionato
				// è primario o secondario, viene usata come condizione di default quella
				// dell'utilizzo primario
				if(!Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario()) && !Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario())) {
					query += " AND RCM.ID_UTILIZZO = ? ";
				}
				else {
					// Uso Primario
					if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario())) {
						query += " AND RCM.ID_UTILIZZO = ? ";
					}
					// Uso Secondario
					else if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario())) {
						query += " AND RCM2.ID_UTILIZZO = ? ";
					}
					// Se ha selezionato sia l'opzione uso primario che quella uso secondario
					else if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario()) && Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario())) {
						query += " AND (RCM.ID_UTILIZZO = ?  OR RCM2.ID_UTILIZZO = ?) ";
					}
				}
				
			}
			//tipo EFA
			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoEfa())) 
			{
        query += " AND UD.ID_TIPO_EFA = ? ";
      }
			// Se l'utente ha specificato un particolare titolo di possesso
			if(filtriParticellareRicercaVO.getIdTitoloPossesso() != null) {
				query += " AND CD.ID_TITOLO_POSSESSO = ? ";
			}
			else 
			{
				// Se l'utente ha scelto l'opzione escludi asservimento
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckEscludiAsservimento()) && filtriParticellareRicercaVO.getCheckEscludiAsservimento().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
				{
					query += " AND CD.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
				}
				// Se l'utente ha scelto l'opzione escludi conferimento
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckEscludiConferimento()) && filtriParticellareRicercaVO.getCheckEscludiConferimento().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
				{
					query += " AND CD.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO;
				}
			}
			
			// Se l'utente ha specificato la tipologia di anomalia bloccante
			boolean isFirst = true;
			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneBloccante())) {
				query += " AND (CD.ESITO_CONTROLLO = ? ";
				isFirst = false;
			}
			// Se l'utente ha specificato la tipologia di anomalia warning
			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneWarning())) {
				if(!isFirst) {
					query += " OR ";
				}
				else {
					query += " AND (";
				}
				query += " CD.ESITO_CONTROLLO = ? ";
				isFirst = false;
			}
			// Se l'utente ha specificato la tipologia di anomalia OK
			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneOk())) {
				if(!isFirst) {
					query += " OR ";
				}
				else {
					query += " AND (";
				}
				query += " CD.ESITO_CONTROLLO = ? ";
				isFirst = false;
			}
			if(!isFirst) {
				query += ")";
			}
			
		  // Ricerco i dati delle particelle solo asservite
      if(filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {
        query += " AND DICH_CONS.ID_AZIENDA <> ? " +
                 " AND DICH_CONS.DATA_INSERIMENTO_DICHIARAZIONE = "+
                 "(SELECT MAX(DATA_INSERIMENTO_DICHIARAZIONE) " +
                 " FROM DB_DICHIARAZIONE_CONSISTENZA DC1 " +
                 " WHERE DICH_CONS.ID_AZIENDA = DC1.ID_AZIENDA" +
                 " AND DC1.DATA_INSERIMENTO_DICHIARAZIONE < DC.DATA_INSERIMENTO_DICHIARAZIONE ) " +
                 " AND DICH_CONS.CODICE_FOTOGRAFIA_TERRENI = CD2.CODICE_FOTOGRAFIA_TERRENI " +
                 " AND CD2.ID_TITOLO_POSSESSO = ? " +
                 " AND CD2.ID_PARTICELLA = SP.ID_PARTICELLA ";
      }
      // Ricerco i dati delle particelle solo conferite
      if(filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {
          query += " AND DICH_CONS.ID_AZIENDA <> ? " +
                   " AND DICH_CONS.DATA_INSERIMENTO_DICHIARAZIONE = "+
                   "(SELECT MAX(DATA_INSERIMENTO_DICHIARAZIONE) " +
                   " FROM DB_DICHIARAZIONE_CONSISTENZA DC1 " +
                   " WHERE DICH_CONS.ID_AZIENDA = DC1.ID_AZIENDA" +
                   " AND DC1.DATA_INSERIMENTO_DICHIARAZIONE < DC.DATA_INSERIMENTO_DICHIARAZIONE ) " +
                   " AND DICH_CONS.CODICE_FOTOGRAFIA_TERRENI = CD2.CODICE_FOTOGRAFIA_TERRENI " +
                   " AND CD2.ID_TITOLO_POSSESSO = ? " +
                   " AND CD2.ID_PARTICELLA = SP.ID_PARTICELLA ";
      }
			
			
			//	Se l'utente vuole estrarre qualunque o un determinato uso del suolo
			if(filtriParticellareRicercaVO.getIdUtilizzo().intValue() != 0) {
				query += " GROUP BY CD.ID_CONDUZIONE_DICHIARATA, " +
				    	 "          CD.ESITO_CONTROLLO, " +
				    	 "          CD.ID_PARTICELLA, " +
				       "          SP.COMUNE, " +
				       "          C.DESCOM, " +
				       "          P.SIGLA_PROVINCIA, " +
				       "          SP.SEZIONE, " +
				       "          SP.FOGLIO, " +
				       "          SP.PARTICELLA, " +
				       "          SP.SUBALTERNO, " +
				       "          SP.SUP_CATASTALE, " +
				       "          SP.SUPERFICIE_GRAFICA, " +
					     "          SP.ID_ZONA_ALTIMETRICA, " +
					     "          TZA.DESCRIZIONE, " +
					//     "          TAM.DESCRIZIONE, " +
					//     "          SP.ID_AREA_C, " +
					     "          SP.FLAG_IRRIGABILE, " +
				       "          TTP.ID_TITOLO_POSSESSO, " +
				       "          TTP.DESCRIZIONE, " +
				       "          CD.SUPERFICIE_CONDOTTA, " +
				       "          CD.PERCENTUALE_POSSESSO, " +
				       "          CD.DATA_INIZIO_CONDUZIONE, " +
				       "          CD.DATA_FINE_CONDUZIONE, " +
				       "          CD.CODICE_FOTOGRAFIA_TERRENI, " +
				       "          CD.SUPERFICIE_AGRONOMICA, " +
				       "          UD.ID_UTILIZZO_DICHIARATO, " +
				       "          UD.ID_CATALOGO_MATRICE, " +
				       "          TU.CODICE, " +
				       "          TU.DESCRIZIONE, " +	
				       "          TV.DESCRIZIONE, " +
				       "          TV.CODICE_VARIETA, " +
				       "          UD.SUPERFICIE_UTILIZZATA, " +
				       "          RCM2.ID_UTILIZZO, " +
				       "          TU2.CODICE, " +
				       "          TU2.DESCRIZIONE, " +
				       "          TV2.DESCRIZIONE, " +
				       "          TV2.CODICE_VARIETA, " +
				       "          UD.SUP_UTILIZZATA_SECONDARIA, " +
				       "          SP.ID_CASO_PARTICOLARE, " +
				       "          TCP.DESCRIZIONE, " +
				       "          CD.ID_UTE, " +
					     "          UD.ANNO_IMPIANTO, " +
					     "          UD.NUMERO_PIANTE_CEPPI, " +
					     "          U.COMUNE, " +
					     "          C2.DESCOM, " +
					     "          F.ID_FOGLIO, " +
				//	     "          F.ID_AREA_E, " +
					     "          PC.SUP_SEMINABILE, " +
					     "          PC.SUP_COLT_ARBOREA_SPECIALIZZATA, " +
					     "          PC.SUP_USO_GRAFICA, " +
					     "          PC.ID_PARTICELLA_CERTIFICATA, " +
	             "          TV.ID_VARIETA, " +
					     "          AA.CUAA, " +
					     "          AA.DENOMINAZIONE, " +
					     "          DC.DATA_INSERIMENTO_DICHIARAZIONE, " +
					     "          DC.ANNO_CAMPAGNA, " +
					     "          DC.ID_DICHIARAZIONE_CONSISTENZA, " +
					     "          PART_BIO.SUP_BIOLOGICO, " +
		           "          PART_BIO.SUP_CONVENZIONALE, " +
		           "          PART_BIO.SUP_IN_CONVERSIONE, " +
		           "          PART_BIO.DATA_INIZIO_CONVERSIONE, " +
		           "          PART_BIO.DATA_FINE_CONVERSIONE, " +
		           "          PAS.DESCRIZIONE," +
		           "          SRI.DATA_RICHIESTA, " +
		           "          SRI.DATA_EVASIONE, " +
		           "          TDU.CODICE_DETTAGLIO_USO, " +
		           "          TDU.DESCRIZIONE_DETTAGLIO_USO, " +
		           "          TDU2.CODICE_DETTAGLIO_USO, " +
		           "          TDU2.DESCRIZIONE_DETTAGLIO_USO, " +
		           "          TDE.CODICE_DESTINAZIONE, " +
		           "          TDE.DESCRIZIONE_DESTINAZIONE, " +
		           "          TDE2.CODICE_DESTINAZIONE, " +
		           "          TDE2.DESCRIZIONE_DESTINAZIONE, " +
		           "          TQU.CODICE_QUALITA_USO, " +
		           "          TQU.DESCRIZIONE_QUALITA_USO, " +
		           "          TQU2.CODICE_QUALITA_USO, " +
		           "          TQU2.DESCRIZIONE_QUALITA_USO, " +
		           "          TPS.CODICE, " +
		           "          TPS.DESCRIZIONE, " +
		           "          TPS2.CODICE, " +
		           "          TPS2.DESCRIZIONE, " +
		           "          TSS.CODICE_SEMINA, " +
		           "          TSS.DESCRIZIONE_SEMINA, " +
		           "          TSS2.CODICE_SEMINA, " +
		           "          TSS2.DESCRIZIONE_SEMINA, " +
		           "          TPM.CODICE_PRATICA_MANTENIMENTO, " +
		           "          TPM.DESCRIZIONE_PRATICA_MANTENIMEN, " +
		           "          TFA.CODICE_FASE_ALLEVAMENTO, " +
		           "          TFA.DESCRIZIONE_FASE_ALLEVAMENTO, " +
		           "          UD.DATA_INIZIO_DESTINAZIONE, " +
		           "          UD.DATA_FINE_DESTINAZIONE, " +
		           "          UD.DATA_INIZIO_DESTINAZIONE_SEC, " +
		           "          UD.DATA_FINE_DESTINAZIONE_SEC, " +
		           "          UD.ID_TIPO_EFA, " +
		           "          TEF.DESCRIZIONE_TIPO_EFA, " +
		           "          UM.DESCRIZIONE, " +
		           "          UD.VALORE_ORIGINALE, " +
		           "          UD.VALORE_DOPO_CONVERSIONE, " +
		           "          UD.VALORE_DOPO_PONDERAZIONE ";
			}
			// Se invece desidera estrarre solo le particelle senza uso del suolo
			else {
				query += " GROUP BY CD.ID_CONDUZIONE_DICHIARATA, " +
				    	   "          CD.ESITO_CONTROLLO, " +
				    	   "          CD.ID_PARTICELLA, " +
				         "          SP.COMUNE, " +
				         "          C.DESCOM, " +
				         "          P.SIGLA_PROVINCIA, " +
				         "          SP.SEZIONE, " +
				         "          SP.FOGLIO, " +
				         "          SP.PARTICELLA, " +
				         "          SP.SUBALTERNO, " +
				         "          SP.SUP_CATASTALE, " +
				         "          SP.SUPERFICIE_GRAFICA, " +
					       "          SP.ID_ZONA_ALTIMETRICA, " +
					       "          TZA.DESCRIZIONE, " +
					 //      "          TAM.DESCRIZIONE, " +
					//       "          SP.ID_AREA_C, " +
					       "          SP.FLAG_IRRIGABILE, " +
				         "          TTP.ID_TITOLO_POSSESSO, " +
				         "          TTP.DESCRIZIONE, " +
				         "          CD.SUPERFICIE_CONDOTTA, " +
				         "          CD.PERCENTUALE_POSSESSO, " +
				         "          CD.DATA_INIZIO_CONDUZIONE, " +
				         "          CD.DATA_FINE_CONDUZIONE, " +
				         "          CD.CODICE_FOTOGRAFIA_TERRENI, " +
				         "          CD.SUPERFICIE_AGRONOMICA, " +
						     "          -1, " +
						     "          -1, " +
						     "          SP.ID_CASO_PARTICOLARE, " +
						     "          TCP.DESCRIZIONE, " +
						     "          CD.ID_UTE, " +
  					     "          UD.ANNO_IMPIANTO, " +
  					     "          UD.NUMERO_PIANTE_CEPPI, " +
  					     "          U.COMUNE, " +
  					     "          C2.DESCOM, " +
  					     "          F.ID_FOGLIO, " +
  					//     "          F.ID_AREA_E, " +
  					     "          PC.SUP_SEMINABILE, " +
  					     "          PC.SUP_COLT_ARBOREA_SPECIALIZZATA, " +
  					     "          PC.SUP_USO_GRAFICA, " +
  					     "          PC.ID_PARTICELLA_CERTIFICATA, " +
  	             "          TV.ID_VARIETA, " +
  					     "          AA.CUAA, " +
  					     "          AA.DENOMINAZIONE, " +
  					     "          DC.DATA_INSERIMENTO_DICHIARAZIONE, " +
  					     "          DC.ANNO_CAMPAGNA, " +
  					     "          DC.ID_DICHIARAZIONE_CONSISTENZA, " +
  					     "          PART_BIO.SUP_BIOLOGICO, " +
  		           "          PART_BIO.SUP_CONVENZIONALE, " +
  		           "          PART_BIO.SUP_IN_CONVERSIONE, " +
  		           "          PART_BIO.DATA_INIZIO_CONVERSIONE, " +
  		           "          PART_BIO.DATA_FINE_CONVERSIONE, " +
  		           "          PAS.DESCRIZIONE, " +
  		           "          SRI.DATA_RICHIESTA, " +
  		           "          SRI.DATA_EVASIONE, " +
  		           "          -1 ";
				
	           query +=
  					     "          HAVING CD.SUPERFICIE_CONDOTTA - SUM(NVL(UD.SUPERFICIE_UTILIZZATA,0)) > 0 ";
			}
		    
		  query += " ORDER BY DESCOM,SIGLA_PROVINCIA,SEZIONE,FOGLIO,PARTICELLA,SUBALTERNO," +
		  		"DESC_POSSESSO,DESC_PRIMARIO,DESC_VARIETA,DESC_SECONDARIO,VAR_SECONDARIA,ID_CASO_PARTICOLARE ";
		   
			
			stmt = conn.prepareStatement(query);
			
			SolmrLogger.debug(this, "Executing searchParticelleDichiarateExcelByParameters: "+query+"\n");
			
			// Setto i parametri dello statement
			int indice = 0;
			
			SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_DICHIARAZIONE_CONSISTENZA] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdPianoRiferimento()+"\n");
      stmt.setLong(++indice, filtriParticellareRicercaVO.getIdPianoRiferimento().longValue());
      listOfParameters += "ID_DICHIARAZIONE_CONSISTENZA: "+filtriParticellareRicercaVO.getIdPianoRiferimento();
      
      //Istanza riesame
      SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_AZIENDA] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+idAzienda+"\n");
      stmt.setLong(++indice, idAzienda.longValue());
      listOfParameters += "ID_AZIENDA: "+idAzienda;     
      SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_DICHIARAZIONE_CONSISTENZA] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdPianoRiferimento()+"\n");
      stmt.setLong(++indice, filtriParticellareRicercaVO.getIdPianoRiferimento().longValue());
      listOfParameters += "ID_DICHIARAZIONE_CONSISTENZA: "+filtriParticellareRicercaVO.getIdPianoRiferimento();
      
      //Particella certificata
      SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_DICHIARAZIONE_CONSISTENZA] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdPianoRiferimento()+"\n");
      stmt.setLong(++indice, filtriParticellareRicercaVO.getIdPianoRiferimento().longValue());
      listOfParameters += "ID_DICHIARAZIONE_CONSISTENZA: "+filtriParticellareRicercaVO.getIdPianoRiferimento();
      
		  //biologico
      SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_AZIENDA] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+idAzienda+"\n");
      stmt.setLong(++indice, idAzienda.longValue());
      listOfParameters += "ID_AZIENDA: "+idAzienda;			
			SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_DICHIARAZIONE_CONSISTENZA] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdPianoRiferimento()+"\n");
			stmt.setLong(++indice, filtriParticellareRicercaVO.getIdPianoRiferimento().longValue());
			listOfParameters += "ID_DICHIARAZIONE_CONSISTENZA: "+filtriParticellareRicercaVO.getIdPianoRiferimento();
			
			stmt.setLong(++indice, filtriParticellareRicercaVO.getIdPianoRiferimento().longValue());
			stmt.setLong(++indice, idAzienda.longValue());
		  // ID_MACRO_USO
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdMacroUso())) 
      { 
        // DATA_INSERIMENTO_DICHIARAZIONE         
        SolmrLogger.debug(this, "Value of parameter "+indice+" [DATA_INSERIMENTO_DICHIARAZIONE] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getDataInserimentoDichiarazione()+"\n");
        stmt.setDate(++indice, new java.sql.Date(filtriParticellareRicercaVO.getDataInserimentoDichiarazione().getTime()));
        listOfParameters += "DATA_INSERIMENTO_DICHIARAZIONE: "+new java.sql.Date(filtriParticellareRicercaVO.getDataInserimentoDichiarazione().getTime());
        // DATA_INSERIMENTO_DICHIARAZIONE         
        SolmrLogger.debug(this, "Value of parameter "+indice+" [DATA_INSERIMENTO_DICHIARAZIONE] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getDataInserimentoDichiarazione()+"\n");
        stmt.setDate(++indice, new java.sql.Date(filtriParticellareRicercaVO.getDataInserimentoDichiarazione().getTime()));
        listOfParameters += "DATA_INSERIMENTO_DICHIARAZIONE: "+new java.sql.Date(filtriParticellareRicercaVO.getDataInserimentoDichiarazione().getTime());
        SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_MACRO_USO] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdMacroUso()+"\n");
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdMacroUso().longValue());
        listOfParameters += "ID_MACRO_USO: "+filtriParticellareRicercaVO.getIdMacroUso();
      }
      // ID_CONTROLLO
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo())) {          
        SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_AZIENDA] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+idAzienda+"\n");
        stmt.setLong(++indice, idAzienda.longValue());
        listOfParameters += "ID_AZIENDA: "+idAzienda;         
        SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_DICHIARAZIONE_CONSISTENZA] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdPianoRiferimento()+"\n");
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdPianoRiferimento().longValue());
        listOfParameters += "ID_DICHIARAZIONE_CONSISTENZA: "+filtriParticellareRicercaVO.getIdPianoRiferimento();         
        SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_CONTROLLO] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdControllo()+"\n");
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdControllo().longValue());
        listOfParameters += "ID_CONTROLLO: "+filtriParticellareRicercaVO.getIdControllo();
      }
			// ID_UTE
			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdUte())) {
				indice++;
				SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_UTE] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdUte()+"\n");
				stmt.setLong(indice, filtriParticellareRicercaVO.getIdUte().longValue());
				listOfParameters += ", ID_UTE: "+filtriParticellareRicercaVO.getIdUte();
			}
      // TIPO DOCUMENTO
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()))
      {
        SolmrLogger.debug(this, "Value of parameter " + indice 
            + "[ID_CATEGORIA_DOCUMENTO] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: " + filtriParticellareRicercaVO.getIdTipoDocumento()
            + "\n");
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTipoDocumento().longValue());
        listOfParameters += ", ID_CATEGORIA_DOCUMENTO: " + filtriParticellareRicercaVO.getIdTipoDocumento();
      }
      
      // DOCUMENTO
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
      {
        SolmrLogger.debug(this, "Value of parameter " + indice
            + "[ID_DOCUMENTO] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: " + filtriParticellareRicercaVO.getIdDocumento()
            + "\n");
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdDocumento().longValue());
        listOfParameters += ", ID_DOCUMENTO: " + filtriParticellareRicercaVO.getIdDocumento();
      }
      
      // PROTOCOLLO DOCUMENTO
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
      {
        SolmrLogger.debug(this, "Value of parameter " + indice
            + "[ID_PROTOCOLLO_DOCUMENTO] in searchParticelleDichiarateExcelByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdProtocolloDocumento()
            + "\n");
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdProtocolloDocumento().longValue());
        listOfParameters += ", ID_PROTOCOLLO_DOCUMENTO: " + filtriParticellareRicercaVO.getIdProtocolloDocumento();
      }
      // ID_TIPO_VALORE_AREA
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoValoreArea()))
      {
        SolmrLogger.debug(this, "Value of parameter " + indice + "[ID_TIPO_VALORE_AREA] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
            + filtriParticellareRicercaVO.getIdTipoValoreArea() + "\n");
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTipoValoreArea().longValue());
        listOfParameters += ", ID_TIPO_VALORE_AREA: " + filtriParticellareRicercaVO.getIdTipoValoreArea();
      }       
      //ID_TIPOLOGIA_NOTIFICA
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipologiaNotifica())) 
      {
        SolmrLogger.debug(this, "Value of parameter " + indice
            + "[ID_TIPOLOGIA_NOTIFICA] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: " + filtriParticellareRicercaVO.getIdTipologiaNotifica()
            + "\n");
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTipologiaNotifica().longValue());
        listOfParameters += ", ID_TIPOLOGIA_NOTIFICA: " + filtriParticellareRicercaVO.getIdTipologiaNotifica();
      }
      //ID_CATEGORIA_NOTIFICA
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCategoriaNotifica())) 
      {
        SolmrLogger.debug(this, "Value of parameter " + indice
            + "[ID_CATEGORIA_NOTIFICA] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: " + filtriParticellareRicercaVO.getIdCategoriaNotifica()
            + "\n");
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdCategoriaNotifica().longValue());
        listOfParameters += ", ID_CATEGORIA_NOTIFICA: " + filtriParticellareRicercaVO.getIdCategoriaNotifica();
      }
			// ISTAT_COMUNE
			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIstatComune())) {
				indice++;
				SolmrLogger.debug(this, "Value of parameter "+indice+"[ISTAT_COMUNE] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIstatComune()+"\n");
				stmt.setString(indice, filtriParticellareRicercaVO.getIstatComune());
				listOfParameters += ", ISTAT_COMUNE: "+filtriParticellareRicercaVO.getIstatComune();
			}
			// SEZIONE
			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getSezione())) {
				indice++;
				SolmrLogger.debug(this, "Value of parameter "+indice+"[SEZIONE] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getSezione()+"\n");
				stmt.setString(indice, filtriParticellareRicercaVO.getSezione().toUpperCase());
				listOfParameters += ", SEZIONE: "+filtriParticellareRicercaVO.getSezione();
			}
			// FOGLIO
			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getFoglio())) {
				indice++;
				SolmrLogger.debug(this, "Value of parameter "+indice+"[FOGLIO] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getFoglio()+"\n");
				stmt.setString(indice, filtriParticellareRicercaVO.getFoglio());
				listOfParameters += ", FOGLIO: "+filtriParticellareRicercaVO.getFoglio();
			}
			// PARTICELLA
			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getParticella())) {
				indice++;
				SolmrLogger.debug(this, "Value of parameter "+indice+"[PARTICELLA] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getParticella()+"\n");
				stmt.setString(indice, filtriParticellareRicercaVO.getParticella());
				listOfParameters += ", PARTICELLA: "+filtriParticellareRicercaVO.getParticella();
			}
			// SUBALTERNO
			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getSubalterno())) {
				indice++;
				SolmrLogger.debug(this, "Value of parameter "+indice+"[SUBALTERNO] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getSubalterno()+"\n");
				stmt.setString(indice, filtriParticellareRicercaVO.getSubalterno().toUpperCase());
				listOfParameters += ", SUBALTERNO: "+filtriParticellareRicercaVO.getSubalterno();
			}
      // ID_ZONA_ALTIMETRICA
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdZonaAltimetrica())) {          
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_ZONA_ALTIMETRICA] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdZonaAltimetrica()+"\n");
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdZonaAltimetrica().longValue());
        listOfParameters += ", ID_ZONA_ALTIMETRICA: "+filtriParticellareRicercaVO.getIdZonaAltimetrica();
      }
      // ID_CASO_PARTICOLARE
      if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCasoParticolare())) {          
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_CASO_PARTICOLARE] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdCasoParticolare()+"\n");
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdCasoParticolare().longValue());
        listOfParameters += ", ID_CASO_PARTICOLARE: "+filtriParticellareRicercaVO.getIdCasoParticolare();
      }
			// Se non è stato indicato se l'uso è primario o secondario
			if(!Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario()) && !Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario())) {
				// Se è stato indicato un particolare uso del suolo, seleziono di default l'opzione
				// uso primario
				if(filtriParticellareRicercaVO.getIdUtilizzo().intValue() > 0) {
					indice++;
					SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_UTILIZZO] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdUtilizzo()+"\n");
					stmt.setLong(indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
					listOfParameters += ", ID_UTILIZZO: "+filtriParticellareRicercaVO.getIdUtilizzo();
				}
			}
			else {
				// Se è stato indicato un particolare uso del suolo, seleziono di default l'opzione
				// uso primario
				if(filtriParticellareRicercaVO.getIdUtilizzo().intValue() > 0) {
					// Uso primario
					if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario())) {
						indice++;
						SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_UTILIZZO] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdUtilizzo()+"\n");
						stmt.setLong(indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
						listOfParameters += ", ID_UTILIZZO: "+filtriParticellareRicercaVO.getIdUtilizzo();
					}
					// Uso secondario
					else if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario())) {
						indice++;
						SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_UTILIZZO_SECONDARIO] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdUtilizzo()+"\n");
						stmt.setLong(indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
						listOfParameters += ", ID_UTILIZZO_SECONDARIO: "+filtriParticellareRicercaVO.getIdUtilizzo();
					}
					// Altrimenti se ha selezionato sia l'opzione uso primario che quella uso secondario
					else if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario()) && Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario())) {
						indice++;
						SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_UTILIZZO] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdUtilizzo()+"\n");
						stmt.setLong(indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
						listOfParameters += ", ID_UTILIZZO: "+filtriParticellareRicercaVO.getIdUtilizzo();
						indice++;
						SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_UTILIZZO_SECONDARIO] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdUtilizzo()+"\n");
						stmt.setLong(indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
						listOfParameters += ", ID_UTILIZZO_SECONDARIO: "+filtriParticellareRicercaVO.getIdUtilizzo();
					}
				}
			}
			//TIPO EFA
			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoEfa())) 
			{
        indice++;
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_TIPO_EFA] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdTipoEfa()+"\n");
        stmt.setLong(indice, filtriParticellareRicercaVO.getIdTipoEfa().longValue());
        listOfParameters += ", ID_TIPO_EFA: "+filtriParticellareRicercaVO.getIdTipoEfa();
      }
			// ID_TIPO_TITOLO_POSSESSO
			if(filtriParticellareRicercaVO.getIdTitoloPossesso() != null) {
				indice++;
				SolmrLogger.debug(this, "Value of parameter"+indice+"[ID_TITOLO_POSSESSO] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getIdTitoloPossesso()+"\n");
				stmt.setLong(indice, filtriParticellareRicercaVO.getIdTitoloPossesso().longValue());
				listOfParameters += ", ID_TITOLO_POSSESSO: "+filtriParticellareRicercaVO.getIdTitoloPossesso();
			}
			// SEGNALAZIONI
			// Se l'utente ha specificato la tipologia di anomalia bloccante
			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneBloccante())) {
				indice++;
				SolmrLogger.debug(this, "Value of parameter "+indice+"[TIPO_SEGNALAZIONE_BLOCCANTE] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getTipoSegnalazioneBloccante()+"\n");
				stmt.setString(indice, filtriParticellareRicercaVO.getTipoSegnalazioneBloccante());
				listOfParameters += ", SEGNALAZIONE: "+filtriParticellareRicercaVO.getTipoSegnalazioneBloccante();
			}
			// Se l'utente ha specificato la tipologia di anomalia warning
			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneWarning())) {
				indice++;
				SolmrLogger.debug(this, "Value of parameter "+indice+"[TIPO_SEGNALAZIONE_WARNING] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getTipoSegnalazioneWarning()+"\n");
				stmt.setString(indice, filtriParticellareRicercaVO.getTipoSegnalazioneWarning());
				listOfParameters += ", SEGNALAZIONE: "+filtriParticellareRicercaVO.getTipoSegnalazioneWarning();
			}
			// Se l'utente ha specificato la tipologia di anomalia OK
			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneOk())) {
				indice++;
				SolmrLogger.debug(this, "Value of parameter "+indice+"[TIPO_SEGNALAZIONE_OK] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+filtriParticellareRicercaVO.getTipoSegnalazioneOk()+"\n");
				stmt.setString(indice, filtriParticellareRicercaVO.getTipoSegnalazioneOk());
				listOfParameters += ", SEGNALAZIONE: "+filtriParticellareRicercaVO.getTipoSegnalazioneOk();					
			}
			
		  // Ricerco i dati della particella certificata solo se vengono
      // richieste le particelle solo asservite
      if(filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S)) {         
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_AZIENDA] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+idAzienda+"\n");
        stmt.setLong(++indice, idAzienda.longValue());
        listOfParameters += ", ID_AZIENDA: "+idAzienda;         
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_TITOLO_POSSESSO] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO+"\n");
        stmt.setLong(++indice, SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO.longValue());
        listOfParameters += ", ID_TITOLO_POSSESSO: "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
      }
      // Ricerco i dati delle particelle solo conferite
      if(filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {         
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_AZIENDA] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+idAzienda+"\n");
        stmt.setLong(++indice, idAzienda.longValue());
        listOfParameters += ", ID_AZIENDA: "+idAzienda;         
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_TITOLO_POSSESSO] in searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO: "+SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO+"\n");
        stmt.setLong(++indice, SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO.longValue());
        listOfParameters += ", ID_TITOLO_POSSESSO: "+SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO;
      }
			
			ResultSet rs = stmt.executeQuery();
			
			// Primo monitoraggio
			SolmrLogger.debug(this, "ConduzioneDichiarataDAO::searchParticelleDichiarateExcelByParameters - "+
			    "In searchParticelleDichiarateExcelByParameters method from the creation of query to the execution, List of parameters: "+listOfParameters);

			Vector<Long> vIdConduzioneDichiarata = null;
			while(rs.next()) 
			{
				AnagParticellaExcelVO anagParticellaExcelVO = new AnagParticellaExcelVO();
				AnagAziendaVO anagAziendaVO = new AnagAziendaVO();
				anagParticellaExcelVO.setLabelUte(rs.getString("COM_UTE")+" - "+rs.getString("DESC_COM_UTE"));
				anagParticellaExcelVO.setIstatComuneParticella(rs.getString("COMUNE"));
				anagParticellaExcelVO.setDescrizioneComuneParticella(rs.getString("DESCOM"));
				anagParticellaExcelVO.setSezione(rs.getString("SEZIONE"));
				anagParticellaExcelVO.setFoglio(rs.getString("FOGLIO"));
				anagParticellaExcelVO.setParticella(rs.getString("PARTICELLA"));
				anagParticellaExcelVO.setSubalterno(rs.getString("SUBALTERNO"));
				anagParticellaExcelVO.setSuperficieCatastale(StringUtils.parseSuperficieField(rs.getString("SUP_CATASTALE")));
				anagParticellaExcelVO.setSuperficieGrafica(StringUtils.parseSuperficieField(rs.getString("SUPERFICIE_GRAFICA")));
				anagParticellaExcelVO.setDescrizioneZonaAltimetrica(rs.getString("DESC_ZONA_ALT"));
				//anagParticellaExcelVO.setDescrizioneCorpoIdrico(rs.getString("DESC_AREA_M"));
				/*if(Validator.isNotEmpty(rs.getString("ID_AREA_C"))) 
				{
					if(!rs.getString("ID_AREA_C").equalsIgnoreCase(String.valueOf(SolmrConstants.ID_AREA_C_NO_NATURA_2000))) 
					{
						anagParticellaExcelVO.setNatura2000("X");
					}
				}*/
				anagParticellaExcelVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
				anagParticellaExcelVO.setSuperficieCondotta(StringUtils.parseSuperficieField(rs.getString("SUPERFICIE_CONDOTTA")));
				anagParticellaExcelVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
				if(Validator.isNotEmpty(rs.getString("ID_CASO_PARTICOLARE"))) 
				{
					anagParticellaExcelVO.setIdCasoParticolare(new Long(rs.getLong("ID_CASO_PARTICOLARE")));
				}
				if(rs.getString("FLAG_IRRIGABILE").equalsIgnoreCase(SolmrConstants.FLAG_S)) 
				{
	      	anagParticellaExcelVO.setIrrigua("X");
	      }
				if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO_DICHIARATO"))) 
				{
					if(Long.decode(rs.getString("ID_UTILIZZO_DICHIARATO")).longValue() > - 1) 
					{
						anagParticellaExcelVO.setIdUtilizzoParticella(new Long(rs.getLong("ID_UTILIZZO_DICHIARATO")));
						String usoPrimario = "";
       			if(Validator.isNotEmpty(rs.getString("COD_PRIMARIO"))) 
       			{
       				usoPrimario += "["+rs.getString("COD_PRIMARIO")+"] ";
       			}
       			if(Validator.isNotEmpty(rs.getString("DESC_PRIMARIO"))) 
       			{
       				usoPrimario += rs.getString("DESC_PRIMARIO");
       			}
       			anagParticellaExcelVO.setUsoPrimario(usoPrimario);
       			String tipoDestinazionePrimario = "";
            if (Validator.isNotEmpty(rs.getString("COD_DESTINAZIONE_PRIM")))
            {
              tipoDestinazionePrimario += "[" + rs.getString("COD_DESTINAZIONE_PRIM") + "] ";
            }
            if (Validator.isNotEmpty(rs.getString("DESC_DESTINAZIONE_PRIM")))
            {
              tipoDestinazionePrimario += rs.getString("DESC_DESTINAZIONE_PRIM");
            }
            anagParticellaExcelVO.setTipoDestinazionePrimario(tipoDestinazionePrimario);
            if(Validator.isNotEmpty(rs.getString("DESC_DETT_USO_PRIM")))
            {
              anagParticellaExcelVO.setTipoDettUsoPrimario(
                " ["+rs.getString("COD_DETT_USO_PRIM")+"] "+rs.getString("DESC_DETT_USO_PRIM"));
            }
            if(Validator.isNotEmpty(rs.getString("DESC_QUAL_USO_PRIM")))
            {
              anagParticellaExcelVO.setTipoQualitaUsoPrimario(
                " ["+rs.getString("COD_QUAL_USO_PRIM")+"] "+rs.getString("DESC_QUAL_USO_PRIM"));
            }
       			String varieta = "";
       			if(Validator.isNotEmpty(rs.getString("COD_VARIETA"))) {
       				varieta += "["+rs.getString("COD_VARIETA")+"] ";
       			}
       			if(Validator.isNotEmpty(rs.getString("DESC_VARIETA"))) {
       				varieta += rs.getString("DESC_VARIETA");
       			}
       			anagParticellaExcelVO.setVarieta(varieta);
       			String usoSecondario = "";
       			if(Validator.isNotEmpty(rs.getString("COD_SECONDARIO"))) {
       				usoSecondario += "["+rs.getString("COD_SECONDARIO")+"] ";
       			}
       			if(Validator.isNotEmpty(rs.getString("DESC_SECONDARIO"))) {
       				usoSecondario += rs.getString("DESC_SECONDARIO");
       			}
       			anagParticellaExcelVO.setUsoSecondario(usoSecondario);
       			String tipoDestinazioneSecondario = "";
            if (Validator.isNotEmpty(rs.getString("COD_DESTINAZIONE_SEC")))
            {
              tipoDestinazioneSecondario += "[" + rs.getString("COD_DESTINAZIONE_SEC") + "] ";
            }
            if (Validator.isNotEmpty(rs.getString("DESC_DESTINAZIONE_SEC")))
            {
              tipoDestinazioneSecondario += rs.getString("DESC_DESTINAZIONE_SEC");
            }
            anagParticellaExcelVO.setTipoDestinazioneSecondario(tipoDestinazioneSecondario);
            if(Validator.isNotEmpty(rs.getString("DESC_DETT_USO_SEC")))
            {
              anagParticellaExcelVO.setTipoDettUsoSecondario(
                " ["+rs.getString("COD_DETT_USO_SEC")+"] "+rs.getString("DESC_DETT_USO_SEC"));
            }
            if(Validator.isNotEmpty(rs.getString("DESC_QUAL_USO_SEC")))
            {
              anagParticellaExcelVO.setTipoQualitaUsoSecondario(
                " ["+rs.getString("COD_QUAL_USO_SEC")+"] "+rs.getString("DESC_QUAL_USO_SEC"));
            }
       			String varietaSecondaria = "";
       			if(Validator.isNotEmpty(rs.getString("COD_VAR_SECONDARIA"))) {
       				varietaSecondaria += "["+rs.getString("COD_VAR_SECONDARIA")+"] ";
       			}
       			if(Validator.isNotEmpty(rs.getString("VAR_SECONDARIA"))) {
       				varietaSecondaria += rs.getString("VAR_SECONDARIA");
       			}
       			anagParticellaExcelVO.setVarietaSecondaria(varietaSecondaria);
       			if(Validator.isNotEmpty(rs.getString("SUP_UTILIZZATA_SECONDARIA"))) {
       				anagParticellaExcelVO.setSuperficieUtilizzataSecondaria(StringUtils.parseSuperficieField(rs.getString("SUP_UTILIZZATA_SECONDARIA")));
       			}
       			anagParticellaExcelVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
						anagParticellaExcelVO.setNumeroPianteCeppi(rs.getString("NUMERO_PIANTE_CEPPI"));
					}
					anagParticellaExcelVO.setSuperficieUtilizzata(StringUtils.parseSuperficieField(rs.getString("SUPERFICIE_UTILIZZATA")));
				}
				/*if(Validator.isNotEmpty(rs.getString("ID_AREA_E")) && rs.getString("ID_AREA_E").equalsIgnoreCase(String.valueOf(SolmrConstants.ID_AREA_E_ZVN))) {
					anagParticellaExcelVO.setDescrizioneZonaVulnerabile("X");
				}*/
				if(Validator.isNotEmpty(rs.getString("SUP_SEMINABILE"))) {
					anagParticellaExcelVO.setSuperficieSeminabile(StringUtils.parseSuperficieField(rs.getString("SUP_SEMINABILE")));
				}
				else {
					anagParticellaExcelVO.setSuperficieSeminabile(SolmrConstants.DEFAULT_SUPERFICIE);
				}
				if(Validator.isNotEmpty(rs.getString("SUP_COLT_ARBOREA_SPECIALIZZATA"))) {
					anagParticellaExcelVO.setSuperficieArboreaSpecializzata(StringUtils.parseSuperficieField(rs.getString("SUP_COLT_ARBOREA_SPECIALIZZATA")));
				}
				else {
					anagParticellaExcelVO.setSuperficieArboreaSpecializzata(SolmrConstants.DEFAULT_SUPERFICIE);
				}
				
				if(Validator.isNotEmpty(rs.getString("SUPERFICIE_AGRONOMICA"))) {
          anagParticellaExcelVO.setSupAgronomica(StringUtils.parseSuperficieField(rs.getString("SUPERFICIE_AGRONOMICA")));
        }
        else {
          anagParticellaExcelVO.setSupAgronomica(SolmrConstants.DEFAULT_SUPERFICIE);
        }
				
				if(Validator.isNotEmpty(rs.getString("SUP_USO_GRAFICA"))) {
          anagParticellaExcelVO.setSupUsoGrafico(StringUtils.parseSuperficieField(rs.getString("SUP_USO_GRAFICA")));
        }
        else {
          anagParticellaExcelVO.setSupUsoGrafico(SolmrConstants.DEFAULT_SUPERFICIE);
        }
				
				
				
				anagParticellaExcelVO.setTipoSeminaPrimario(rs.getString("DESC_SEM_PRIM"));
        anagParticellaExcelVO.setTipoSeminaSecondario(rs.getString("DESC_SEM_SEC"));
        anagParticellaExcelVO.setTipoEpocaSeminaPrimario(rs.getString("DESC_PER_SEM_PRIM"));
        anagParticellaExcelVO.setTipoEpocaSeminaSecondario(rs.getString("DESC_PER_SEM_SEC"));
        anagParticellaExcelVO.setMantenimento(rs.getString("DESCRIZIONE_PRATICA_MANTENIMEN"));
        anagParticellaExcelVO.setAllevamento(rs.getString("DESCRIZIONE_FASE_ALLEVAMENTO"));
        anagParticellaExcelVO.setDataInzioDestPrim(rs.getTimestamp("DATA_INIZIO_DESTINAZIONE"));
        anagParticellaExcelVO.setDataFineDestPrim(rs.getTimestamp("DATA_FINE_DESTINAZIONE"));
        anagParticellaExcelVO.setDataInzioDestSec(rs.getTimestamp("DATA_INIZIO_DESTINAZIONE_SEC"));
        anagParticellaExcelVO.setDataFineDestSec(rs.getTimestamp("DATA_FINE_DESTINAZIONE_SEC"));
        anagParticellaExcelVO.setTipoEfa(rs.getString("DESCRIZIONE_TIPO_EFA"));
        anagParticellaExcelVO.setDescUnitaMisura(rs.getString("DESC_UNITA_MIS_EFA"));
        if (Validator.isNotEmpty(rs.getString("VALORE_ORIGINALE")))
        {
          anagParticellaExcelVO.setValoreOriginale(StringUtils.parseDoubleFieldTwoDecimal(rs.getString("VALORE_ORIGINALE")));
        }
        if (Validator.isNotEmpty(rs.getString("VALORE_DOPO_CONVERSIONE")))
        {
          anagParticellaExcelVO.setValoreDopoConversione(StringUtils.parseSuperficieField(rs.getString("VALORE_DOPO_CONVERSIONE")));
        }
        if (Validator.isNotEmpty(rs.getString("VALORE_DOPO_PONDERAZIONE")))
        {
          anagParticellaExcelVO.setValoreDopoPonderazione(StringUtils.parseSuperficieField(rs.getString("VALORE_DOPO_PONDERAZIONE")));
        }       
				
			  //Biologico
        if (Validator.isNotEmpty(rs.getString("SUP_BIOLOGICO")))
        {
          anagParticellaExcelVO.setSupBiologico(StringUtils.parseSuperficieField(rs.getString("SUP_BIOLOGICO")));
        }
        
        if (Validator.isNotEmpty(rs.getString("SUP_CONVENZIONALE")))
        {
          anagParticellaExcelVO.setSupConvenzionale(StringUtils.parseSuperficieField(rs.getString("SUP_CONVENZIONALE")));
        }
        
        if (Validator.isNotEmpty(rs.getString("SUP_IN_CONVERSIONE")))
        {
          anagParticellaExcelVO.setSupInConversione(StringUtils.parseSuperficieField(rs.getString("SUP_IN_CONVERSIONE")));
        }
        
        anagParticellaExcelVO.setDataInizioConversione(rs.getTimestamp("DATA_INIZIO_CONVERSIONE"));
        anagParticellaExcelVO.setDataFineConversione(rs.getTimestamp("DATA_FINE_CONVERSIONE"));
        anagParticellaExcelVO.setDataRichiesta(rs.getTimestamp("DATA_RICHIESTA"));
        anagParticellaExcelVO.setDataEvasione(rs.getTimestamp("DATA_EVASIONE"));
				
				try
				{
				  //long idParticellaCertificata=Long.parseLong(rs.getString("ID_PARTICELLA_CERTIFICATA"));
				  long idCatalogoMatrice = Long.parseLong(rs.getString("ID_CATALOGO_MATRICE"));
				  java.util.Date dataInserimentoDichiarazione = rs.getDate("DATA_INSERIMENTO_DICHIARAZIONE");
				  long idParticella = Long.parseLong(rs.getString("ID_PARTICELLA")); 
				  long codiceFotografiaTerreni = Long.parseLong(rs.getString("CODICE_FOTOGRAFIA_TERRENI"));
				  
				  anagParticellaExcelVO.setSupEleggibile(StringUtils
				        .parseSuperficieFieldBigDecimal(getSupEleggPlSqlTotaleDich(codiceFotografiaTerreni, 
				            dataInserimentoDichiarazione, idParticella, idCatalogoMatrice)));
				}
				catch(Exception e)
				{
				  anagParticellaExcelVO.setSupEleggibile(SolmrConstants.DEFAULT_SUPERFICIE);
				}
				
				anagParticellaExcelVO.setSupEleggibileNetta(SolmrConstants.DEFAULT_SUPERFICIE);
				
				
				Long idConduzioneDichiarata = new Long(rs.getLong("ID_CONDUZIONE_DICHIARATA"));
        if(vIdConduzioneDichiarata == null)
          vIdConduzioneDichiarata = new Vector<Long>();
        
        if(!vIdConduzioneDichiarata.contains(idConduzioneDichiarata))
          vIdConduzioneDichiarata.add(idConduzioneDichiarata);
        anagParticellaExcelVO.setIdConduzioneDichiarata(idConduzioneDichiarata);
        
        
        /*if (Validator.isNotEmpty(rs.getString("SUP_REG_PASCOLI")))
        {
          anagParticellaExcelVO.setSupRegPascoli(StringUtils.parseSuperficieField(rs.getString("SUP_REG_PASCOLI")));
        }*/
        anagParticellaExcelVO.setDescFonteRegPascoli(rs.getString("DESC_FONTE_REG_PASCOLI"));
				
        anagParticellaExcelVO.setInNotifica(rs.getString("IN_NOTIFICA"));

				
				
				anagAziendaVO.setCUAA(rs.getString("CUAA"));
				anagAziendaVO.setDenominazione(rs.getString("DENOMINAZIONE"));
				anagParticellaExcelVO.setAnagAziendaVO(anagAziendaVO);
				
				
				
				if(Validator.isNotEmpty(filtriParticellareRicercaVO.getvTipoArea()))
        {
          Vector<String> vDescValoreArea = new Vector<String>(); 
          for(int t=0;t<filtriParticellareRicercaVO.getvTipoArea().size();t++)
          {
            vDescValoreArea.add(rs.getString("DESC_TIPO_VALORE_AREA"+t));
          }
          
          anagParticellaExcelVO.setvDescValoreArea(vDescValoreArea);
        }
				
				
				
				elencoParticelle.add(anagParticellaExcelVO);
			}
			
			
			try
      {
        if(vIdConduzioneDichiarata != null)
        {
          HashMap<Long, String> hDocumenti = getHashDocumentiFromIdConduzioneDichiarata(vIdConduzioneDichiarata, idAzienda, filtriParticellareRicercaVO.getDataInserimentoDichiarazione());
          if((hDocumenti != null) && (elencoParticelle != null))
          {
            for(int s=0;s<elencoParticelle.size();s++)
            {
              elencoParticelle.get(s).setDocumento(hDocumenti.get(elencoParticelle.get(s).getIdConduzioneDichiarata()));              
            }            
          }         
        }
        
        
        //anagParticellaExcelVO.setDocumento(
            //getDocumentiFromIdConduzioneParticella(idConduzioneParticella, idAzienda));
      }
      catch (Exception e)
      {}
			
			// Secondo monitoraggio
			SolmrLogger.debug(this, "ConduzioneDichiarataDAO::searchParticelleDichiarateExcelByParameters - "+ 
			  "In searchParticelleDichiarateByParameters method from the creation of query to final setting of parameters inside of Vector after resultset's while cicle, List of parameters: "+listOfParameters);
			
		  rs.close();
			stmt.close();
			

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "searchParticelleDichiarateExcelByParameters in ConduzioneDichiarataDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "searchParticelleDichiarateExcelByParameters in ConduzioneDichiarataDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "searchParticelleDichiarateExcelByParameters in ConduzioneDichiarataDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "searchParticelleDichiarateExcelByParameters in ConduzioneDichiarataDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated searchParticelleDichiarateExcelByParameters method in ConduzioneDichiarataDAO\n");
		return elencoParticelle;
	}
	
	
	public BigDecimal getSupEleggPlSqlTotaleDich(long codiceFotograficaTerreni, 
	    Date dataInserimentoDichiarazione, long idParticella, long idCatalogoMatrice) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    CallableStatement cs = null;
    StringBuffer queryBuf = null;
    BigDecimal supEleggibile = new BigDecimal(0);
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ConduzioneDichiarataDAO::getSupEleggPlSqlTotaleDich] BEGIN.");
      /***
       *  FUNCTION SelSupElegDichByFotoIdPartVar (pCodiceFoto      IN DB_ELEGGIBILITA_DICHIARATA.CODICE_FOTOGRAFIA_TERRENI%TYPE,
       *                                          pDataInsDich     IN DB_DICHIARAZIONE_CONSISTENZA.DATA_INSERIMENTO_DICHIARAZIONE%TYPE,
       *                                          pIdParticella    IN DB_ELEGGIBILITA_DICHIARATA.ID_PARTICELLA%TYPE,
       *                                          pIdVarieta       IN DB_VARIETA_ELEGGIBILITA.ID_VARIETA%TYPE
       *                                          ) RETURN DB_ELEGGIBILITA_DICHIARATA.SUPERFICIE%TYPE IS
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      
      queryBuf = new StringBuffer();
      queryBuf
          .append("{ ? = call Pck_Smrgaa_Libreria.SelSupElegDichByFotoIdPartVar(?,?,?,?)}");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ConduzioneDichiarataDAO::getSupEleggPlSqlTotaleDich] Query="
                + query);
      }
      
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.registerOutParameter(1,Types.DECIMAL);
      cs.setLong(2, codiceFotograficaTerreni);
      cs.setDate(3, convertSqlDate(dataInserimentoDichiarazione));
      cs.setLong(4, idParticella);
      cs.setLong(5, idCatalogoMatrice);
      
      
      cs.executeUpdate();
      
      BigDecimal supTmp = cs.getBigDecimal(1);
      if(supTmp != null)
      {
        supEleggibile = supEleggibile.add(supTmp);
      }
      
      
      
      
      return supEleggibile;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("supEleggibile", supEleggibile) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("codiceFotograficaTerreni", codiceFotograficaTerreni),
        new Parametro("dataInserimentoDichiarazione", dataInserimentoDichiarazione),
        new Parametro("idParticella", idParticella),
        new Parametro("idCatalogoMatrice", idCatalogoMatrice)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[ConduzioneDichiarataDAO::getSupEleggPlSqlTotaleDich] ",
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
      closePlSql(cs, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[ConduzioneDichiarataDAO::getSupEleggPlSqlTotaleDich] END.");
    }
  }
	
	
	
	public BigDecimal getSupEleggNettaPlSqlTotale(long idParticellaCertificata, long idCatalogoMatrice) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    CallableStatement cs = null;
    StringBuffer queryBuf = null;
    BigDecimal supEleggibile = new BigDecimal(0);
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ConduzioneDichiarataDAO::getSupEleggNettaPlSqlTotale] BEGIN.");
      /***
       *  FUNCTION SelTotSupElegByPartEVetVarNTar (pIdPartCertif  IN DB_PARTICELLA_CERT_ELEG.ID_PARTICELLA_CERTIFICATA%TYPE,
       *                                       pStrVetVarieta IN VARCHAR2
       *                                       ) RETURN  DB_PARTICELLA_CERT_ELEG.SUPERFICIE%TYPE IS
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{ ? = call Pck_Smrgaa_Libreria.SelTotSupElegByPartEVetVarNTar(?,?)}");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ConduzioneDichiarataDAO::getSupEleggPlSqlTotale] Query="
                + query);
      }
      
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.registerOutParameter(1,Types.DECIMAL);
      cs.setLong(2, idParticellaCertificata);
      cs.setLong(3, idCatalogoMatrice);
      
      cs.executeUpdate();
      
      BigDecimal supTmp = cs.getBigDecimal(1);
      if(supTmp != null)
      {
        supEleggibile = supEleggibile.add(supTmp);
      }
      
      
      
      
      return supEleggibile;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("supEleggibile", supEleggibile) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idParticellaCertificata", idParticellaCertificata),
        new Parametro("idCatalogoMatrice", idCatalogoMatrice)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[ConduzioneDichiarataDAO::getSupEleggNettaPlSqlTotale] ",
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
      closePlSql(cs, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[ConduzioneDichiarataDAO::getSupEleggNettaPlSqlTotale] END.");
    }
  }
	
	
	
  
  /**
   * 
   * non usato più, usato il metodo getSupElegg
   * 
   * 
   * @param idAzienda
   * @param idDichiarazioneConsistenza
   * @param idParticella
   * @param idParticellaCertificata
   * @param idUtilizzoDichiarato
   * @return
   * @throws DataAccessException
   */
  public BigDecimal getSupEleggPlSql(long idAzienda, long idDichiarazioneConsistenza,
      long idParticella, long idParticellaCertificata, long idUtilizzoDichiarato) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    CallableStatement cs = null;
    StringBuffer queryBuf = null;
    BigDecimal supEleggibile = new BigDecimal(0);
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ConduzioneParticellaDAO::getSupEleggPlSql] BEGIN.");
      /***
       *  FUNCTION Sup_Eleggibile_Riproporzionata (pIdAzienda       IN DB_ANAGRAFICA_AZIENDA.ID_AZIENDA%TYPE,
       *                                           pIdDichCons      IN DB_DICHIARAZIONE_CONSISTENZA.ID_DICHIARAZIONE_CONSISTENZA%TYPE,
       *                                           pIdParticella    IN DB_PARTICELLA.ID_PARTICELLA%TYPE,
       *                                           pIdPartCertif    IN DB_PARTICELLA_CERTIFICATA.ID_PARTICELLA_CERTIFICATA%TYPE,
       *                                           pIdUtilizzoDich  IN DB_UTILIZZO_DICHIARATO.ID_UTILIZZO_DICHIARATO%TYPE
       *                                      ) RETURN DB_UTILIZZO_DICHIARATO.SUPERFICIE_UTILIZZATA%TYPE IS
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{ ? = call Pck_Smrgaa_Libreria.Sup_Eleggibile_Riproporzionata(?,?,?,?,?)}");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ConduzioneParticellaDAO::getSupEleggPlSql] Query="
                + query);
      }
      
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.registerOutParameter(1,Types.DECIMAL);
      cs.setLong(2, idAzienda);
      cs.setLong(3, idDichiarazioneConsistenza);
      cs.setLong(4, idParticella);
      cs.setLong(5, idParticellaCertificata);
      cs.setLong(6, idUtilizzoDichiarato);
      
      cs.executeUpdate();
      
      BigDecimal supTmp = cs.getBigDecimal(1);
      if(supTmp != null)
      {
        supEleggibile = supEleggibile.add(supTmp);
      }
      
      
      
      
      return supEleggibile;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("supEleggibile", supEleggibile) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza),
        new Parametro("idParticella", idParticella),
        new Parametro("idParticellaCertificata", idParticellaCertificata),
        new Parametro("idUtilizzoDichiarato", idUtilizzoDichiarato)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[ConduzioneParticellaDAO::getSupEleggPlSql] ",
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
      closePlSql(cs, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[ConduzioneParticellaDAO::getSupEleggPlSql] END.");
    }
  }
	
	
	/**
	 * Metodo utilizzato per effettuare i riepiloghi per titolo di possesso relativi ad'azienda agricola
	 * e ad una determinata dichiarazione di consistenza
	 * 
	 * @param idAzienda
	 * @param idDichiarazioneConsistenza
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.ConduzioneDichiarataVO[]
	 * @throws DataAccessException
	 */
	public StoricoParticellaVO[] riepilogoTitoloPossessoDichiarato(Long idAzienda, Long idDichiarazioneConsistenza) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating riepilogoTitoloPossessoDichiarato method in ConduzioneDichiarataDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in riepilogoTitoloPossessoDichiarato method in ConduzioneDichiarataDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in riepilogoTitoloPossessoDichiarato method in ConduzioneDichiarata and it values: "+conn+"\n");

			String query = " " +
    			"SELECT COND.ID_TITOLO_POSSESSO, " +
          "       COND.DESCRIZIONE, " +
          "       SUM(SUP_COND) AS SUP_CONDOTTA, " +
          "       NVL(SUM(SUP_UTILIZZATA),0) AS SUP_UTILIZZATA " +
          "FROM " +
          "        (SELECT CD.ID_TITOLO_POSSESSO, " +
          "                TTP.DESCRIZIONE, " +
          "                CD.SUPERFICIE_CONDOTTA SUP_COND, " +
          "                CD.ID_CONDUZIONE_DICHIARATA, " +
          "                SUM(UD.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
          "         FROM   DB_CONDUZIONE_DICHIARATA CD, " + 
          "                DB_DICHIARAZIONE_CONSISTENZA DC, " +
          "                DB_UTILIZZO_DICHIARATO UD, " +
          "                DB_TIPO_TITOLO_POSSESSO TTP " +
          "         WHERE  DC.ID_AZIENDA = ? " + 
          "         AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
          "         AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
          "         AND    CD.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO " +
          "         AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA(+) " +
          "         GROUP BY CD.ID_TITOLO_POSSESSO, " +
          "                  TTP.DESCRIZIONE, " +
          "                  CD.SUPERFICIE_CONDOTTA, " +
          "                  CD.ID_CONDUZIONE_DICHIARATA) COND " +
          "GROUP BY  COND.ID_TITOLO_POSSESSO, COND.DESCRIZIONE " +
          "ORDER BY DESCRIZIONE ASC";
			
			

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoTitoloPossessoDichiarato method in ConduzioneDichiarataDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ID_DICHIARAZIONE_CONSISTENZA] in riepilogoTitoloPossessoDichiarato method in ConduzioneDichiarataDAO: "+idDichiarazioneConsistenza+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());
			stmt.setLong(2, idDichiarazioneConsistenza.longValue());

			SolmrLogger.debug(this, "Executing riepilogoTitoloPossessoDichiarato: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) 
			{
			  StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
				ConduzioneDichiarataVO conduzioneDichiarataVO = new ConduzioneDichiarataVO();
				UtilizzoDichiaratoVO utilizzoDichiaratoVO = new UtilizzoDichiaratoVO();
				conduzioneDichiarataVO.setSuperficieCondotta(rs.getString("SUP_CONDOTTA"));
				conduzioneDichiarataVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
				CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_TITOLO_POSSESSO")), rs.getString("DESCRIZIONE"));
				conduzioneDichiarataVO.setTitoloPossesso(code);
				utilizzoDichiaratoVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
				conduzioneDichiarataVO.setElencoUtilizzi(new UtilizzoDichiaratoVO[]{utilizzoDichiaratoVO});
				ConduzioneDichiarataVO[] elencoConduzioni = new ConduzioneDichiarataVO[1];
				elencoConduzioni[0] = conduzioneDichiarataVO;
				storicoParticellaVO.setElencoConduzioniDichiarate(elencoConduzioni);
				elencoParticelle.add(storicoParticellaVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "riepilogoTitoloPossessoDichiarato in ConduzioneDichiarataDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "riepilogoTitoloPossessoDichiarato in ConduzioneDichiarataDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "riepilogoTitoloPossessoDichiarato in ConduzioneDichiarataDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "riepilogoTitoloPossessoDichiarato in ConduzioneDichiarataDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated riepilogoTitoloPossessoDichiarato method in ConduzioneDichiarataDAO\n");
		if(elencoParticelle.size() == 0) {
			return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
		}
		else {
			return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
		}
	}
	
	/**
	 * Controlla se esiste almeno una conduzione dichiarata relativa alla particella che 
	 * non sia in asservimento/conferimento tranne l'azienda corrente!
	 * 
	 * @param idParticella
	 * @return
	 * @throws DataAccessException
	 */
	public boolean existsConduzioneDichiarataNotAsservimentoConferimento(long idParticella, long idAzienda) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating existsConduzioneDichiarataNotAsservimentoConferimento method in ConduzioneDichiarataDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    boolean trovato = false;
  
    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in existsConduzioneDichiarataNotAsservimentoConferimento method in ConduzioneDichiarataDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in existsConduzioneDichiarataNotAsservimentoConferimento method in ConduzioneDichiarata and it values: "+conn+"\n");
  
      String query = " "
        + " SELECT CD.ID_CONDUZIONE_DICHIARATA "
        + " FROM   DB_CONDUZIONE_DICHIARATA CD, DB_DICHIARAZIONE_CONSISTENZA DC, " 
        + "        DB_ANAGRAFICA_AZIENDA AA "
        + " WHERE  AA.ID_AZIENDA = DC.ID_AZIENDA "
        + " AND    CD.CODICE_FOTOGRAFIA_TERRENI = DC.CODICE_FOTOGRAFIA_TERRENI "
        + " AND    CD.ID_PARTICELLA = ? "    
        + " AND    AA.DATA_FINE_VALIDITA IS NULL "
        + " AND    AA.DATA_CESSAZIONE IS NULL "
        + " AND    AA.ID_AZIENDA <> ? "
        + " AND    CD.ID_TITOLO_POSSESSO <> 5 "
        + " AND    CD.ID_TITOLO_POSSESSO <> 6 "
        + " AND    DC.ID_MOTIVO_DICHIARAZIONE <> 7 "
        + " AND    DC.DATA_INSERIMENTO_DICHIARAZIONE = " 
        + "        (SELECT MAX(DC1.DATA_INSERIMENTO_DICHIARAZIONE) " 
        + "        FROM   DB_DICHIARAZIONE_CONSISTENZA DC1, DB_TIPO_MOTIVO_DICHIARAZIONE D " 
        + "        WHERE  DC1.ID_AZIENDA = DC.ID_AZIENDA "
        + "        AND D.ID_MOTIVO_DICHIARAZIONE=DC1.ID_MOTIVO_DICHIARAZIONE "
        + "        AND D.TIPO_DICHIARAZIONE<>'C' "
        + " AND    DC1.ID_MOTIVO_DICHIARAZIONE <> 7) ";
      
          
  
      SolmrLogger.debug(this, "Value of parameter 1 [ID_PARTICELLA] in existsConduzioneDichiarataNotAsservimentoConferimento method in ConduzioneDichiarataDAO: "+idParticella+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ID_AZIENDA] in existsConduzioneDichiarataNotAsservimentoConferimento method in ConduzioneDichiarataDAO: "+idAzienda+"\n");
      
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idParticella);
      stmt.setLong(2, idAzienda);
  
      SolmrLogger.debug(this, "Executing existsConduzioneDichiarataNotAsservimentoConferimento: "+query+"\n");
  
      ResultSet rs = stmt.executeQuery();
  
      if(rs.next()) 
      {
        trovato = true;        
      }
      
      rs.close();
      stmt.close();
  
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "existsConduzioneDichiarataNotAsservimentoConferimento in ConduzioneDichiarataDAO - SQLException: "+exc.getMessage()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "existsConduzioneDichiarataNotAsservimentoConferimento in ConduzioneDichiarataDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "existsConduzioneDichiarataNotAsservimentoConferimento in ConduzioneDichiarataDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "existsConduzioneDichiarataNotAsservimentoConferimento in ConduzioneDichiarataDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated existsConduzioneDichiarataNotAsservimentoConferimento method in ConduzioneDichiarataDAO\n");
    return trovato;
  }
	
	/**
	 * Ricavo la somma della superifice condotta dell'ultima dichiarazione di consistenza
	 * relativa alla particella tranne quelle dell'azienda corrente
	 * 
	 * @param idParticella
	 * @param idAzienda
	 * @return
	 * @throws DataAccessException
	 */
	public BigDecimal getSumSuperficieCondottaAsservita(long idParticella, long idAzienda) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getSumSuperficieCondottaAsservita method in ConduzioneDichiarataDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    BigDecimal sumSup = null;
  
    try {
      SolmrLogger.debug(this, "Creating db-connection in getSumSuperficieCondottaAsservita method in ConduzioneDichiarataDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getSumSuperficieCondottaAsservita method in ConduzioneDichiarata and it values: "+conn+"\n");
  
      String query = " "
          + " SELECT "
          + "        SUM(NVL(CD.SUPERFICIE_CONDOTTA,0))  SUPERFICIE " 
          + " FROM   DB_CONDUZIONE_DICHIARATA CD, DB_DICHIARAZIONE_CONSISTENZA DC, " 
          + "        DB_ANAGRAFICA_AZIENDA AA "
          + " WHERE  AA.ID_AZIENDA = DC.ID_AZIENDA "
          + " AND    CD.CODICE_FOTOGRAFIA_TERRENI = DC.CODICE_FOTOGRAFIA_TERRENI "
          + " AND    CD.ID_PARTICELLA = ? "    
          + " AND    AA.DATA_FINE_VALIDITA IS NULL "
          + " AND    AA.DATA_CESSAZIONE IS NULL "
          + " AND    AA.ID_AZIENDA <> ? "
          + " AND    CD.ID_TITOLO_POSSESSO = 5 "
          + " AND    DC.ID_MOTIVO_DICHIARAZIONE <> 7 "
          + " AND    DC.DATA_INSERIMENTO_DICHIARAZIONE = " 
          + "        (SELECT MAX(DC1.DATA_INSERIMENTO_DICHIARAZIONE) " 
          + "        FROM   DB_DICHIARAZIONE_CONSISTENZA DC1, DB_TIPO_MOTIVO_DICHIARAZIONE D " 
          + "        WHERE  DC1.ID_AZIENDA = DC.ID_AZIENDA "
          + "        AND D.ID_MOTIVO_DICHIARAZIONE=DC1.ID_MOTIVO_DICHIARAZIONE "
          + "        AND D.TIPO_DICHIARAZIONE<>'C' "
          + " AND    DC1.ID_MOTIVO_DICHIARAZIONE <> 7) "
          + " GROUP BY CD.ID_PARTICELLA ";
  
      SolmrLogger.debug(this, "Value of parameter 1 [ID_PARTICELLA] in getSumSuperficieCondottaAsservita method in ConduzioneDichiarataDAO: "+idParticella+"\n");
      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getSumSuperficieCondottaAsservita method in ConduzioneDichiarataDAO: "+idAzienda+"\n");
      
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idParticella);
      stmt.setLong(2, idAzienda);
  
      SolmrLogger.debug(this, "Executing getSumSuperficieCondottaAsservita: "+query+"\n");
  
      ResultSet rs = stmt.executeQuery();
  
      if(rs.next()) {
        sumSup = rs.getBigDecimal("SUPERFICIE");
        
      }
      
      rs.close();
      stmt.close();
  
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getSumSuperficieCondottaAsservita in ConduzioneDichiarataDAO - SQLException: "+exc.getMessage()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getSumSuperficieCondottaAsservita in ConduzioneDichiarataDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getSumSuperficieCondottaAsservita in ConduzioneDichiarataDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getSumSuperficieCondottaAsservita in ConduzioneDichiarataDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getSumSuperficieCondottaAsservita method in ConduzioneDichiarataDAO\n");
    return sumSup;
  }
	
	
	
	
	private HashMap<Long,String> getHashDocumentiFromIdConduzioneDichiarata(Vector<Long> vIdConduzioneDichiarata,
	    long idAzienda, Date dataInserimentoDichiarazione) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    HashMap<Long,String> hmDocumenti = null;
    String documento = "";
    
    try
    {
      SolmrLogger.debug(this,
          "[ConduzioneDichiarataDAO::getHashDocumentiFromIdConduzioneDichiarata] BEGIN.");
      
      
     
      conn = getDatasource().getConnection();

      Long idJavaIns = getNextPrimaryKey(SolmrConstants.SEQ_SMRGAA_W_JAVA_INSERT);

      String insert="INSERT INTO SMRGAA_W_JAVA_INSERT (ID_JAVA_INSERT, ID_DETTAGLIO_INS) "+
                    "VALUES ( ?, ?) ";

      stmt = conn.prepareStatement(insert);

      for (int i=0;i<vIdConduzioneDichiarata.size();i++)
      {
        stmt.setLong(1,idJavaIns.longValue());
        stmt.setLong(2, vIdConduzioneDichiarata.get(i).longValue());
        stmt.addBatch();
      }
      stmt.executeBatch();

      SolmrLogger.debug(this,"[ConduzioneDichiarataDAO::getHashDocumentiFromIdConduzioneDichiarata] insert executed");

      stmt.close();

      
      
      

      queryBuf = new StringBuffer();
      
      queryBuf.append(
        "SELECT  CD.ID_CONDUZIONE_DICHIARATA, " +
        "        TD.DESCRIZIONE, " +       
        "        DOC.NUMERO_PROTOCOLLO, " +
        "        DOC.DATA_PROTOCOLLO " + 
        "FROM    DB_DOCUMENTO_CONDUZIONE DOCC," +
        "        DB_DOCUMENTO DOC, " +
        "        DB_TIPO_DOCUMENTO TD, " +
        "        DB_DOCUMENTO_CATEGORIA DCAT, " +
        "        DB_TIPO_CATEGORIA_DOCUMENTO TCD," +
        "        DB_CONDUZIONE_DICHIARATA CD, "+
        "        SMRGAA_W_JAVA_INSERT WIJ "+
        "WHERE   WIJ.ID_JAVA_INSERT = ? " +
        "AND     WIJ.ID_DETTAGLIO_INS = CD.ID_CONDUZIONE_DICHIARATA " +
        "AND     CD.ID_CONDUZIONE_PARTICELLA = DOCC.ID_CONDUZIONE_PARTICELLA " +
        "AND     DOCC.ID_DOCUMENTO = DOC.ID_DOCUMENTO " +
        "AND     DOC.EXT_ID_DOCUMENTO = TD.ID_DOCUMENTO " +
        "AND     TD.ID_DOCUMENTO = DCAT.ID_DOCUMENTO " +
        "AND     DCAT.ID_CATEGORIA_DOCUMENTO = TCD.ID_CATEGORIA_DOCUMENTO " +
        "AND     DOC.ID_AZIENDA = ? " +    
        "AND     TD.ID_TIPOLOGIA_DOCUMENTO = 2 " +
        "AND     TCD.TIPO_IDENTIFICATIVO = 'TP' " +
        "AND     DOCC.ID_CONDUZIONE_PARTICELLA = CD.ID_CONDUZIONE_PARTICELLA " +
        "AND     TCD.IDENTIFICATIVO = CD.ID_TITOLO_POSSESSO " +
        "AND     DOCC.DATA_INIZIO_VALIDITA < ? " +
        "AND     NVL(DOCC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? " +
        "ORDER BY CD.ID_CONDUZIONE_DICHIARATA ");

  
      
      
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ConduzioneDichiarataDAO::getHashDocumentiFromIdConduzioneDichiarata] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1,idJavaIns.longValue());
      stmt.setLong(2,idAzienda);
      stmt.setTimestamp(3,convertDateToTimestamp(dataInserimentoDichiarazione));
      stmt.setTimestamp(4,convertDateToTimestamp(dataInserimentoDichiarazione));
      
      ResultSet rs = stmt.executeQuery();
      
      
      while (rs.next())
      {
        if(hmDocumenti == null)
          hmDocumenti = new HashMap<Long,String>();
        
        Long idConduzioneDichiarata = new Long(rs.getLong("ID_CONDUZIONE_DICHIARATA"));
        
        if(hmDocumenti.get(idConduzioneDichiarata) != null)
        {
          documento = hmDocumenti.get(idConduzioneDichiarata)+" - ";
        }
        else
        {
          documento = "";   
        }  
        
      
        
        String documentoDesc = rs.getString("DESCRIZIONE");
        String protocollo = rs.getString("NUMERO_PROTOCOLLO");
        Date dataProtocollo = rs.getTimestamp("DATA_PROTOCOLLO");
        
        if(Validator.isNotEmpty(documentoDesc))
        {
          documento +=  documentoDesc;
        }
        
        if(Validator.isNotEmpty(protocollo))
        {
          documento += " Repertorio n. " + protocollo;
        }
        
        if(Validator.isNotEmpty(dataProtocollo))
        {
          documento += " del "+DateUtils.formatDate(dataProtocollo);
        }
       
        hmDocumenti.put(idConduzioneDichiarata, documento);
      }
      
      return hmDocumenti;
      
    }
    catch (Exception t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("documento", documento) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("vIdConduzioneDichiarata", vIdConduzioneDichiarata),
        new Parametro("idAzienda", idAzienda),
        new Parametro("dataInserimentoDichiarazione", dataInserimentoDichiarazione)};

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ConduzioneDichiarataDAO::getHashDocumentiFromIdConduzioneDichiarata] ", t,
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
          "[ConduzioneDichiarataDAO::getHashDocumentiFromIdConduzioneDichiarata] END.");
    }
  }
	
	
	
	
}
