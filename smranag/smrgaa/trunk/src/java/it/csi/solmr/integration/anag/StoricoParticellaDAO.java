package it.csi.solmr.integration.anag;
	
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.dto.terreni.TipoMetodoIrriguoVO;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.ProvinciaVO;
import it.csi.solmr.dto.UtenteIrideVO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.ConsistenzaVO;
import it.csi.solmr.dto.anag.DocumentoConduzioneVO;
import it.csi.solmr.dto.anag.FoglioVO;
import it.csi.solmr.dto.anag.ParticellaCertificataVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneDichiarataVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneParticellaVO;
import it.csi.solmr.dto.anag.terreni.SezioneVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.dto.anag.terreni.TipoIrrigazioneVO;
import it.csi.solmr.dto.anag.terreni.TipoPotenzialitaIrriguaVO;
import it.csi.solmr.dto.anag.terreni.TipoRotazioneColturaleVO;
import it.csi.solmr.dto.anag.terreni.TipoTerrazzamentoVO;
import it.csi.solmr.dto.anag.terreni.UtilizzoDichiaratoVO;
import it.csi.solmr.dto.anag.terreni.UtilizzoParticellaVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;
import it.csi.util.performance.StopWatch;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

public class StoricoParticellaDAO extends it.csi.solmr.integration.BaseDAO {


	public StoricoParticellaDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public StoricoParticellaDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo che mi restituisce l'elenco dei comuni su cui insistono le particelle
	 * @param idAzienda
	 * @param onlyActive
	 * @param orderBy
	 * @return java.util.Vector
	 * @throws DataAccessException
	 */
	public Vector<ComuneVO> getListComuniParticelleByIdAzienda(Long idAzienda, boolean onlyActive, String[] orderBy) throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating getListComuniParticelleByIdAzienda method in StoricoParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<ComuneVO> elencoComuni = new Vector<ComuneVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListComuniParticelleByIdAzienda method in StoricoParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListComuniParticelleByIdAzienda method in StoricoParticellaDAO and it values: "+conn+"\n");

			String query = " SELECT DISTINCT C.ISTAT_COMUNE, " +
						   "  	             C.ISTAT_PROVINCIA AS ISTAT_PROV, " +
						   "                 P.ID_REGIONE, " +	
						   "                 P.SIGLA_PROVINCIA, " +
						   "                 P.DESCRIZIONE, " +
						   "                 C.DESCOM, " +
						   "                 C.CAP, " +
						   "                 C.CODFISC, " +
						   "                 C.MONTANA, " +
						   "                 C.ZONAALT, " +
						   "                 TZA.DESCRIZIONE AS DESC_ALT, " +
						   "                 C.ZONAALTS1, " +
						   "                 C.ZONAALTS2, " +
						   "                 C.REGAGRI, " +
						   "                 C.PROV_OLD, " +
						   "                 C.COMUNE_OLD, " +
						   "                 C.USL_OLD, " +
						   "                 C.PROV_NEW, " +
						   "                 C.FLAG_ESTERO, " +
						   "                 C.PREFISSO, " +
						   "                 C.FLAG_ESTINTO, " +
						   "                 C.SIGLA_ESTERO " +
						   " FROM            DB_STORICO_PARTICELLA SP, " +	
						   "                 DB_CONDUZIONE_PARTICELLA CP, " +
						   "                 DB_UTE U, " +
						   "                 COMUNE C, " +
						   "                 DB_TIPO_ZONA_ALTIMETRICA TZA, " +	
						   "                 PROVINCIA P " +
						   " WHERE           U.ID_AZIENDA = ? " +
						   " AND             U.ID_UTE = CP.ID_UTE " +
						   " AND             CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
						   " AND             SP.COMUNE = C.ISTAT_COMUNE " +	
						   " AND             C.ZONAALT = TZA.ID_ZONA_ALTIMETRICA(+) " +
						   " AND             C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) ";
			
			if(onlyActive) {
				query +=   " AND             CP.DATA_FINE_CONDUZIONE IS NULL ";
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

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListComuniParticelleByIdAzienda method in StoricoParticellaDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ONLY_ACTIVE] in getListComuniParticelleByIdAzienda method in StoricoParticellaDAO: "+onlyActive+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ORDER_BY] in getListComuniParticelleByIdAzienda method in StoricoParticellaDAO: "+orderBy+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());

			SolmrLogger.debug(this, "Executing getListComuniParticelleByIdAzienda: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				ComuneVO comuneVO = new ComuneVO();
				comuneVO.setIstatComune(rs.getString("ISTAT_COMUNE"));
				if(Validator.isNotEmpty(rs.getString("ISTAT_PROV"))) {
					ProvinciaVO provinciaVO = new ProvinciaVO();
					provinciaVO.setIstatProvincia(rs.getString("ISTAT_PROV"));
					comuneVO.setIstatProvincia(provinciaVO.getIstatProvincia());
					provinciaVO.setIdRegione(rs.getString("ID_REGIONE"));
					provinciaVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));
					comuneVO.setSiglaProv(provinciaVO.getSiglaProvincia());
					provinciaVO.setDescrizione(rs.getString("DESCRIZIONE"));
					comuneVO.setProvinciaVO(provinciaVO);
				}
				comuneVO.setDescom(rs.getString("DESCOM"));
				comuneVO.setCap(rs.getString("CAP"));
				comuneVO.setCodfisc(rs.getString("CODFISC"));
				comuneVO.setMontana(rs.getString("MONTANA"));
				if(Validator.isNotEmpty(rs.getString("ZONAALT"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ZONAALT")), rs.getString("DESC_ALT"));
					comuneVO.setTipoZonaAltimetrica(code);
				}
				comuneVO.setZonaAltS1(rs.getString("ZONAALTS1"));
				comuneVO.setZonaAltS2(rs.getString("ZONAALTS2"));
				comuneVO.setRegAgri(rs.getString("REGAGRI"));
				comuneVO.setProvOld(rs.getString("PROV_OLD"));
				comuneVO.setComuneOld(rs.getString("COMUNE_OLD"));
				comuneVO.setUslOld(rs.getString("USL_OLD"));
				comuneVO.setProvNew(rs.getString("PROV_NEW"));
				comuneVO.setFlagEstero(rs.getString("FLAG_ESTERO"));
				comuneVO.setPrefisso(rs.getString("PREFISSO"));
				comuneVO.setFlagEstinto(rs.getString("FLAG_ESTINTO"));
				comuneVO.setSiglaEstero(rs.getString("SIGLA_ESTERO"));
				elencoComuni.add(comuneVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListComuniParticelleByIdAzienda in StoricoParticellaDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListComuniParticelleByIdAzienda in StoricoParticellaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListComuniParticelleByIdAzienda in StoricoParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListComuniParticelleByIdAzienda in StoricoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListComuniParticelleByIdAzienda method in StoricoParticellaDAO\n");
		return elencoComuni;
	}
	
	
	public Vector<ComuneVO> getListComuniParticelleByIdDichiarazioneConsistenza(Long idDichiarazioneConsistenza, String[] orderBy) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getListComuniParticelleByIdDichiarazioneConsistenza method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ComuneVO> elencoComuni = new Vector<ComuneVO>();

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getListComuniParticelleByIdDichiarazioneConsistenza method in StoricoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListComuniParticelleByIdDichiarazioneConsistenza method in StoricoParticellaDAO and it values: "+conn+"\n");

      String query = " SELECT DISTINCT C.ISTAT_COMUNE, " +
               "                 C.ISTAT_PROVINCIA AS ISTAT_PROV, " +
               "                 P.ID_REGIONE, " +  
               "                 P.SIGLA_PROVINCIA, " +
               "                 P.DESCRIZIONE, " +
               "                 C.DESCOM, " +
               "                 C.CAP, " +
               "                 C.CODFISC, " +
               "                 C.MONTANA, " +
               "                 C.ZONAALT, " +
               "                 TZA.DESCRIZIONE AS DESC_ALT, " +
               "                 C.ZONAALTS1, " +
               "                 C.ZONAALTS2, " +
               "                 C.REGAGRI, " +
               "                 C.PROV_OLD, " +
               "                 C.COMUNE_OLD, " +
               "                 C.USL_OLD, " +
               "                 C.PROV_NEW, " +
               "                 C.FLAG_ESTERO, " +
               "                 C.PREFISSO, " +
               "                 C.FLAG_ESTINTO, " +
               "                 C.SIGLA_ESTERO " +
               " FROM            DB_STORICO_PARTICELLA SP, " +  
               "                 DB_CONDUZIONE_DICHIARATA CD, " +
               "                 DB_DICHIARAZIONE_CONSISTENZA DC, " +
               "                 COMUNE C, " +
               "                 DB_TIPO_ZONA_ALTIMETRICA TZA, " +  
               "                 PROVINCIA P " +
               " WHERE           DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
               " AND             DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
               " AND             CD.ID_PARTICELLA = SP.ID_PARTICELLA " +
               " AND             SP.COMUNE = C.ISTAT_COMUNE " + 
               " AND             C.ZONAALT = TZA.ID_ZONA_ALTIMETRICA(+) " +
               " AND             C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) ";
      
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

      SolmrLogger.debug(this, "Value of parameter 1 [ID_DICHIARAZIONE_CONSISTENZA] in getListComuniParticelleByIdDichiarazioneConsistenza method in StoricoParticellaDAO: "+idDichiarazioneConsistenza+"\n");
      SolmrLogger.debug(this, "Value of parameter 3 [ORDER_BY] in getListComuniParticelleByIdDichiarazioneConsistenza method in StoricoParticellaDAO: "+orderBy+"\n");

      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idDichiarazioneConsistenza.longValue());

      SolmrLogger.debug(this, "Executing getListComuniParticelleByIdDichiarazioneConsistenza: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) {
        ComuneVO comuneVO = new ComuneVO();
        comuneVO.setIstatComune(rs.getString("ISTAT_COMUNE"));
        if(Validator.isNotEmpty(rs.getString("ISTAT_PROV"))) {
          ProvinciaVO provinciaVO = new ProvinciaVO();
          provinciaVO.setIstatProvincia(rs.getString("ISTAT_PROV"));
          comuneVO.setIstatProvincia(provinciaVO.getIstatProvincia());
          provinciaVO.setIdRegione(rs.getString("ID_REGIONE"));
          provinciaVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));
          comuneVO.setSiglaProv(provinciaVO.getSiglaProvincia());
          provinciaVO.setDescrizione(rs.getString("DESCRIZIONE"));
          comuneVO.setProvinciaVO(provinciaVO);
        }
        comuneVO.setDescom(rs.getString("DESCOM"));
        comuneVO.setCap(rs.getString("CAP"));
        comuneVO.setCodfisc(rs.getString("CODFISC"));
        comuneVO.setMontana(rs.getString("MONTANA"));
        if(Validator.isNotEmpty(rs.getString("ZONAALT"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ZONAALT")), rs.getString("DESC_ALT"));
          comuneVO.setTipoZonaAltimetrica(code);
        }
        comuneVO.setZonaAltS1(rs.getString("ZONAALTS1"));
        comuneVO.setZonaAltS2(rs.getString("ZONAALTS2"));
        comuneVO.setRegAgri(rs.getString("REGAGRI"));
        comuneVO.setProvOld(rs.getString("PROV_OLD"));
        comuneVO.setComuneOld(rs.getString("COMUNE_OLD"));
        comuneVO.setUslOld(rs.getString("USL_OLD"));
        comuneVO.setProvNew(rs.getString("PROV_NEW"));
        comuneVO.setFlagEstero(rs.getString("FLAG_ESTERO"));
        comuneVO.setPrefisso(rs.getString("PREFISSO"));
        comuneVO.setFlagEstinto(rs.getString("FLAG_ESTINTO"));
        comuneVO.setSiglaEstero(rs.getString("SIGLA_ESTERO"));
        elencoComuni.add(comuneVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getListComuniParticelleByIdDichiarazioneConsistenza in StoricoParticellaDAO - SQLException: "+exc.getMessage()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getListComuniParticelleByIdDichiarazioneConsistenza in StoricoParticellaDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getListComuniParticelleByIdDichiarazioneConsistenza in StoricoParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getListComuniParticelleByIdDichiarazioneConsistenza in StoricoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListComuniParticelleByIdDichiarazioneConsistenza method in StoricoParticellaDAO\n");
    return elencoComuni;
  }
	
	
	
	public Long findIdUteByIdCondPartIdAz(Long idConduzioneParticella, Long idAzienda) throws DataAccessException{
      SolmrLogger.debug(this, "BEGIN findIdUteByIdCondPartIdAz");
      Connection conn = null;
	  PreparedStatement stmt = null;
	  Long idUte = null;

  	  try{
	    SolmrLogger.debug(this, "Creating db-connection in findIdUteByIdCondPartIdAz method in StoricoParticellaDAO\n");
	    conn = getDatasource().getConnection();
	     
	    String query = "SELECT "+
	    					"U.ID_UTE "+
	    			   "FROM "+  
	    			   		"DB_CONDUZIONE_PARTICELLA CP,"+
	    			   		"DB_UTE U "+
	    			   "WHERE "+  
	    			   		"CP.ID_CONDUZIONE_PARTICELLA = ? "+
	    			   		"AND U.ID_AZIENDA = ? "+
	    			   		"AND U.DATA_FINE_ATTIVITA IS NULL "+
	    			   		"AND CP.ID_TITOLO_POSSESSO != 5 "+
	    			   		"AND CP.ID_UTE = U.ID_UTE";
	    
	    stmt = conn.prepareStatement(query);		
	    SolmrLogger.debug(this, "--- idConduzioneParticella ="+idConduzioneParticella);
		stmt.setLong(1, idConduzioneParticella);
		SolmrLogger.debug(this, "--- idAzienda ="+idAzienda);
		stmt.setLong(2, idAzienda);

		SolmrLogger.debug(this, "--- Executing findIdUteByIdCondPartIdAz: "+query+"\n");

		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
		  idUte = rs.getLong("ID_UTE");	
		}
  	  }
	  catch(SQLException exc) {
	    SolmrLogger.error(this, "findIdUteByIdCondPartIdAz in StoricoParticellaDAO - SQLException: "+exc.getMessage()+"\n");
		throw new DataAccessException(exc.getMessage());
	  }
	  catch(Exception ex) {
		SolmrLogger.error(this, "findIdUteByIdCondPartIdAz in StoricoParticellaDAO - Generic Exception: "+ex+"\n");
		throw new DataAccessException(ex.getMessage());
	  }
	  finally {
		try {
			if(stmt != null) stmt.close();
			if(conn != null) conn.close();
		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "findIdUteByIdCondPartIdAz in StoricoParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "findIdUteByIdCondPartIdAz in StoricoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
			throw new DataAccessException(ex.getMessage());
		}
	}
	SolmrLogger.debug(this, "END findIdUteByIdCondPartIdAz");
	return idUte;  
  }
	
	/**
	 * Metodo che mi restituisce il record di DB_STORICO_PARTICELLA a partire
	 * dall'id_conduzione_particella
	 * 
	 * @param idConduzioneParticella
	 * @return it.csi.solmr.dto.StoricoParticellaVO
	 * @throws DataAccessException
	 */
	public StoricoParticellaVO findStoricoParticellaVOByIdConduzioneParticella(Long idConduzioneParticella) throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating findStoricoParticellaVOByIdConduzioneParticella method in StoricoParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		StoricoParticellaVO storicoParticellaVO = null;

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in findStoricoParticellaVOByIdConduzioneParticella method in StoricoParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in findStoricoParticellaVOByIdConduzioneParticella method in StoricoParticellaDAO and it values: "+conn+"\n");

			String query = " SELECT SP.ID_STORICO_PARTICELLA, " +
						   "    SP.ID_PARTICELLA, " +
						   "    SP.DATA_INIZIO_VALIDITA, " +
						   "    SP.DATA_FINE_VALIDITA, " +
						   "    SP.COMUNE, " +
						   "    C.DESCOM, " +
						   "    P.SIGLA_PROVINCIA, " +
						   "    SP.SEZIONE, " +
						   "    SP.FOGLIO, " +
						   "    SP.PARTICELLA, " +
						   "    SP.SUBALTERNO, " +
						   "    SP.SUP_CATASTALE, " +
						   "    SP.SUPERFICIE_GRAFICA, " +
						   "    SP.ID_ZONA_ALTIMETRICA, " +
						   "    TZA.DESCRIZIONE AS DESC_ALTIMETRICA, " +
						   "    SP.ID_AREA_A, " +
						   "    TAA.DESCRIZIONE AS DESC_AREA_A, " +
						   "    SP.ID_AREA_B, " +
						   "    TAB.DESCRIZIONE AS DESC_AREA_B, " +
						   "    SP.ID_AREA_C, " +
						   "    TAC.DESCRIZIONE AS DESC_AREA_C, " +
						   "    SP.ID_AREA_D, " +
						   "    TAD.DESCRIZIONE AS DESC_AREA_D, " +
						   "    SP.ID_AREA_G, " +
               "    TAG.DESCRIZIONE AS DESC_AREA_G, " +
               "    SP.ID_AREA_H, " +
               "    TAH.DESCRIZIONE AS DESC_AREA_H, " +
               "    SP.ID_AREA_I, " +
               "    TAI.DESCRIZIONE AS DESC_AREA_I, " +
               "    SP.ID_AREA_L, " +
               "    TAL.DESCRIZIONE AS DESC_AREA_L, " +
               "    SP.ID_AREA_M, " +
               "    TAM.DESCRIZIONE AS DESC_AREA_M, " +
						   "    SP.FLAG_CAPTAZIONE_POZZI, " +
						   "    SP.FLAG_IRRIGABILE, " +
						   "    SP.ID_CASO_PARTICOLARE, " +
						   "    TCP.DESCRIZIONE AS DESC_CASO_PART, " +
						   "    SP.MOTIVO_MODIFICA, " +	
						   "    SP.DATA_AGGIORNAMENTO, " +
						   "    SP.ID_UTENTE_AGGIORNAMENTO, " +
						   "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
					       "          || ' ' " + 
					       "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
					       "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
					       "         WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
						   "    AS DENOMINAZIONE, " +
						   "       (SELECT PVU.DENOMINAZIONE " +
						   "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
						   "        WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
						   "    AS DESCRIZIONE_ENTE_APPARTENENZA, " +
						   "    SP.ID_CAUSALE_MOD_PARTICELLA, " +
						   "    TCMP.DESCRIZIONE AS DESC_CAUSALE_MOD, " +
						   "    SP.SUP_NON_ELEGGIBILE, " +
						   "    SP.SUP_NE_BOSCO_ACQUE_FABBRICATO, " +
						   "    SP.SUP_NE_FORAGGIERE, " +
						   "    SP.SUP_EL_FRUTTA_GUSCIO, " +
						   "    SP.SUP_EL_PRATO_PASCOLO, " +
						   "    SP.SUP_EL_COLTURE_MISTE, " +
						   "    SP.SUP_COLTIVAZ_ARBOREA_CONS, " +
						   "    SP.SUP_COLTIVAZ_ARBOREA_SPEC, " +
						   "    SP.DATA_FOTO, " +
						   "    SP.TIPO_FOTO, " +
						   "    SP.ID_FONTE, " +
						   "    TF.DESCRIZIONE AS DESC_FONTE, " +
						   "    SP.ID_DOCUMENTO, " +
						   "    PART.DATA_CESSAZIONE, " +
						   "    SP.ID_DOCUMENTO_PROTOCOLLATO, " +
						   "    SP.ID_IRRIGAZIONE, " +
						   "    SP.ID_FASCIA_FLUVIALE, " +
						   "    TFF.DESCRIZIONE AS DESC_FLUVIALE, " +
						   "    TI.DESCRIZIONE AS DESC_IRRIGAZIONE, " +
						   "    TI.DATA_INIZIO_VALIDITA AS DATA_INI_IRRIGAZ, " +
						   "    TI.DATA_FINE_VALIDITA AS DATA_FINE_IRRIGAZ, " +
						   "    SP.ID_POTENZIALITA_IRRIGUA, " +
               "    TPI.DESCRIZIONE AS DESC_POTENZIALITA_IRRIGUA, " +
               "    TPI.CODICE AS COD_POTENZIALITA_IRRIGUA, " +
               "    SP.ID_TERRAZZAMENTO, " +
               "    TTR.DESCRIZIONE AS DESC_TERRAZZAMENTO, " +
               "    TTR.CODICE AS COD_TERRAZZAMENTO, " +
               "    SP.ID_ROTAZIONE_COLTURALE, " +
               "    TRC.DESCRIZIONE AS DESC_ROTAZIONE_COLTURALE, " +
               "    TRC.CODICE AS COD_ROTAZIONE_COLTURALE, " +
						   "    CP.ID_CONDUZIONE_PARTICELLA, " +
						   "    CP.ID_TITOLO_POSSESSO, " +
						   "    CP.SUPERFICIE_CONDOTTA, " +
						   "    CP.PERCENTUALE_POSSESSO, " +
						   "    (SELECT ID_PARTICELLA_CERTIFICATA " +
               "     FROM DB_PARTICELLA_CERTIFICATA PC " +
						   "     WHERE PC.ID_PARTICELLA = SP.ID_PARTICELLA " +
						   "     AND PC.DATA_FINE_VALIDITA  IS NULL) AS ID_PARTICELLA_CERTIFICATA, " +
						   "    SP.PERCENTUALE_PENDENZA_MEDIA, " +
			         "    SP.GRADI_PENDENZA_MEDIA, " +
			         "    SP.GRADI_ESPOSIZIONE_MEDIA, " +
			         "    SP.METRI_ALTITUDINE_MEDIA, " +
			         "    SP.ID_METODO_IRRIGUO, " +
			         "    TMI.DESCRIZIONE_METODO_IRRIGUO, " +
			         "    TMI.CODICE_METODO_IRRIGUO " +
						   " FROM   DB_STORICO_PARTICELLA SP, " +
						   "    DB_CONDUZIONE_PARTICELLA CP, " +	
						   "    COMUNE C, " +
						   "    PROVINCIA P, " +
						   "    DB_TIPO_ZONA_ALTIMETRICA TZA, " +
						   "    DB_TIPO_AREA_A TAA, " +
						   "    DB_TIPO_AREA_B TAB, " +
						   "    DB_TIPO_AREA_C TAC, " +
						   "    DB_TIPO_AREA_D TAD, " +
						   "    DB_TIPO_AREA_G TAG, " +
						   "    DB_TIPO_AREA_H TAH, " +
						   "    DB_TIPO_AREA_I TAI, " +
						   "    DB_TIPO_AREA_L TAL, " +
						   "    DB_TIPO_AREA_M TAM, " +
						   "    DB_TIPO_CASO_PARTICOLARE TCP, " +
						   //"    PAPUA_V_UTENTE_LOGIN PVU, " +
						   "    DB_TIPO_CAUSALE_MOD_PARTICELLA TCMP, " +
						   "    DB_TIPO_FONTE TF, " +
						   "    DB_TIPO_DOCUMENTO TD, " +
						   "    DB_PARTICELLA PART, " +
						   "    DB_TIPO_IRRIGAZIONE TI, " +
						   "    DB_TIPO_FASCIA_FLUVIALE TFF, " +
						   "    DB_TIPO_POTENZIALITA_IRRIGUA TPI, " +
						   "    DB_TIPO_TERRAZZAMENTO TTR, " +
						   "    DB_TIPO_ROTAZIONE_COLTURALE TRC, " +
						   "    DB_TIPO_METODO_IRRIGUO TMI " +
						   " WHERE  CP.ID_CONDUZIONE_PARTICELLA = ? " +
						   " AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
						   " AND    SP.DATA_FINE_VALIDITA IS NULL " +
						   " AND    SP.COMUNE = C.ISTAT_COMUNE " +
						   " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) " +
						   " AND    SP.ID_ZONA_ALTIMETRICA = TZA.ID_ZONA_ALTIMETRICA(+) " + 
						   " AND    SP.ID_AREA_A = TAA.ID_AREA_A(+) " +
						   " AND    SP.ID_AREA_B = TAB.ID_AREA_B(+) " + 
						   " AND    SP.ID_AREA_C = TAC.ID_AREA_C(+) " +
						   " AND    SP.ID_AREA_D = TAD.ID_AREA_D(+) " +
						   " AND    SP.ID_AREA_G = TAG.ID_AREA_G(+) " +
						   " AND    SP.ID_AREA_H = TAH.ID_AREA_H(+) " +
						   " AND    SP.ID_AREA_I = TAI.ID_AREA_I(+) " +
						   " AND    SP.ID_AREA_L = TAL.ID_AREA_L(+) " +
						   " AND    SP.ID_AREA_M = TAM.ID_AREA_M(+) " +
						   " AND    SP.ID_CASO_PARTICOLARE = TCP.ID_CASO_PARTICOLARE(+) " +
						   //" AND    SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
						   " AND    SP.ID_CAUSALE_MOD_PARTICELLA = TCMP.ID_CAUSALE_MOD_PARTICELLA(+) " +
						   " AND    SP.ID_FONTE = TF.ID_FONTE(+) " +
						   " AND    SP.ID_DOCUMENTO = TD.ID_DOCUMENTO(+) " +
						   " AND    SP.ID_PARTICELLA = PART.ID_PARTICELLA " +
						   " AND    SP.ID_IRRIGAZIONE = TI.ID_IRRIGAZIONE(+) " +
						   " AND    SP.ID_FASCIA_FLUVIALE = TFF.ID_FASCIA_FLUVIALE(+) " +
			         " AND    SP.ID_POTENZIALITA_IRRIGUA = TPI.ID_POTENZIALITA_IRRIGUA(+) " +
			         " AND    SP.ID_TERRAZZAMENTO = TTR.ID_TERRAZZAMENTO(+) " +
			         " AND    SP.ID_ROTAZIONE_COLTURALE = TRC.ID_ROTAZIONE_COLTURALE(+) " +
			         " AND    SP.ID_METODO_IRRIGUO = TMI.ID_METODO_IRRIGUO (+) ";

			SolmrLogger.debug(this, "Value of parameter 1 [ID_CONDUZIONE_PARTICELLA] in findStoricoParticellaVOByIdConduzioneParticella method in StoricoParticellaDAO: "+idConduzioneParticella+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idConduzioneParticella.longValue());

			SolmrLogger.debug(this, "Executing findStoricoParticellaVOByIdConduzioneParticella: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) {
				storicoParticellaVO = new StoricoParticellaVO(); 
				ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
				storicoParticellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
				storicoParticellaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
				storicoParticellaVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				storicoParticellaVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
				ComuneVO comuneVO = new ComuneVO();
				comuneVO.setIstatComune(rs.getString("COMUNE"));
				comuneVO.setDescom(rs.getString("DESCOM"));				
				if(Validator.isNotEmpty(rs.getString("SIGLA_PROVINCIA"))) {
					comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
					ProvinciaVO provinciaVO = new ProvinciaVO();
					provinciaVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));
					comuneVO.setProvinciaVO(provinciaVO);
				}
				storicoParticellaVO.setComuneParticellaVO(comuneVO);
				storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
				storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
				storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
				storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
				storicoParticellaVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
				storicoParticellaVO.setSuperficieGrafica(rs.getString("SUPERFICIE_GRAFICA"));
				if(Validator.isNotEmpty(rs.getString("ID_ZONA_ALTIMETRICA"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_ZONA_ALTIMETRICA")), rs.getString("DESC_ALTIMETRICA"));
					storicoParticellaVO.setIdZonaAltimetrica(Long.decode(rs.getString("ID_ZONA_ALTIMETRICA")));
					storicoParticellaVO.setZonaAltimetrica(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_AREA_A"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_A")), rs.getString("DESC_AREA_A"));
					storicoParticellaVO.setIdAreaA(new Long(rs.getString("ID_AREA_A")));
					storicoParticellaVO.setAreaA(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_AREA_B"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_B")), rs.getString("DESC_AREA_B"));
					storicoParticellaVO.setIdAreaB(new Long(rs.getString("ID_AREA_B")));
					storicoParticellaVO.setAreaB(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_AREA_C"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_C")), rs.getString("DESC_AREA_C"));
					storicoParticellaVO.setIdAreaC(new Long(rs.getString("ID_AREA_C")));
					storicoParticellaVO.setAreaC(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_AREA_D"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_D")), rs.getString("DESC_AREA_D"));
					storicoParticellaVO.setIdAreaD(new Long(rs.getString("ID_AREA_D")));
					storicoParticellaVO.setAreaD(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_AREA_G"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_G")), rs.getString("DESC_AREA_G"));
          storicoParticellaVO.setIdAreaG(new Long(rs.getString("ID_AREA_G")));
          storicoParticellaVO.setAreaG(code);
        }
				if(Validator.isNotEmpty(rs.getString("ID_AREA_H"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_H")), rs.getString("DESC_AREA_H"));
          storicoParticellaVO.setIdAreaH(new Long(rs.getString("ID_AREA_H")));
          storicoParticellaVO.setAreaH(code);
        }
				if(Validator.isNotEmpty(rs.getString("ID_AREA_I"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_I")), rs.getString("DESC_AREA_I"));
          storicoParticellaVO.setIdAreaI(new Long(rs.getString("ID_AREA_I")));
          storicoParticellaVO.setAreaI(code);
        }
				if(Validator.isNotEmpty(rs.getString("ID_AREA_L"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_L")), rs.getString("DESC_AREA_L"));
          storicoParticellaVO.setIdAreaL(new Long(rs.getString("ID_AREA_L")));
          storicoParticellaVO.setAreaL(code);
        }
				if(Validator.isNotEmpty(rs.getString("ID_AREA_M"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_M")), rs.getString("DESC_AREA_M"));
          storicoParticellaVO.setIdAreaM(new Long(rs.getString("ID_AREA_M")));
          storicoParticellaVO.setAreaM(code);
        }
				storicoParticellaVO.setFlagCaptazionePozzi(rs.getString("FLAG_CAPTAZIONE_POZZI"));
				storicoParticellaVO.setFlagIrrigabile(rs.getString("FLAG_IRRIGABILE"));
				if(Validator.isNotEmpty(rs.getString("ID_CASO_PARTICOLARE"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_CASO_PARTICOLARE")), rs.getString("DESC_CASO_PART"));
					storicoParticellaVO.setIdCasoParticolare(new Long(rs.getString("ID_CASO_PARTICOLARE")));
					storicoParticellaVO.setCasoParticolare(code);
				}
				storicoParticellaVO.setMotivoModifica(rs.getString("MOTIVO_MODIFICA"));
				storicoParticellaVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
				storicoParticellaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				UtenteIrideVO utenteIrideVO = new UtenteIrideVO();
				utenteIrideVO.setIdUtente(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				utenteIrideVO.setDenominazione(rs.getString("DENOMINAZIONE"));
				utenteIrideVO.setDescrizioneEnteAppartenenza(rs.getString("DESCRIZIONE_ENTE_APPARTENENZA"));
        utenteIrideVO.setDenominazione("");
		    utenteIrideVO.setDescrizioneEnteAppartenenza("");
				storicoParticellaVO.setUtenteAggiornamento(utenteIrideVO);
				if(Validator.isNotEmpty(rs.getString("ID_CAUSALE_MOD_PARTICELLA"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_CAUSALE_MOD_PARTICELLA")), rs.getString("DESC_CAUSALE_MOD"));
					storicoParticellaVO.setIdCausaleModParticella(new Long(rs.getString("ID_CAUSALE_MOD_PARTICELLA")));
					storicoParticellaVO.setCausaleModParticella(code);
				}
				storicoParticellaVO.setSupNonEleggibile(rs.getString("SUP_NON_ELEGGIBILE"));
				storicoParticellaVO.setSupNeBoscoAcqueFabbricato(rs.getString("SUP_NE_BOSCO_ACQUE_FABBRICATO"));
				storicoParticellaVO.setSupNeForaggere(rs.getString("SUP_NE_FORAGGIERE"));
				storicoParticellaVO.setSupElFruttaGuscio(rs.getString("SUP_EL_FRUTTA_GUSCIO"));
				storicoParticellaVO.setSupElPratoPascolo(rs.getString("SUP_EL_PRATO_PASCOLO"));
				storicoParticellaVO.setSupElColtureMiste(rs.getString("SUP_EL_COLTURE_MISTE"));
				storicoParticellaVO.setSupColtivazArboreaCons(rs.getString("SUP_COLTIVAZ_ARBOREA_CONS"));
				storicoParticellaVO.setSupColtivazArboreaSpec(rs.getString("SUP_COLTIVAZ_ARBOREA_SPEC"));
				storicoParticellaVO.setDataFoto(rs.getDate("DATA_FOTO"));
				storicoParticellaVO.setTipoFoto(rs.getString("TIPO_FOTO"));
				if(Validator.isNotEmpty(rs.getString("ID_FONTE"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_FONTE")), rs.getString("DESC_FONTE"));
					storicoParticellaVO.setIdFonte(new Long(rs.getString("ID_FONTE")));
					storicoParticellaVO.setFonte(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_DOCUMENTO"))) {
					storicoParticellaVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
				}
				storicoParticellaVO.setDataCessazione(rs.getDate("DATA_CESSAZIONE"));
				if(Validator.isNotEmpty(rs.getString("ID_DOCUMENTO_PROTOCOLLATO"))) {
					storicoParticellaVO.setIdDocumentoProtocollato(new Long(rs.getString("ID_DOCUMENTO_PROTOCOLLATO")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_IRRIGAZIONE"))) {
					storicoParticellaVO.setIdIrrigazione(new Long(rs.getLong("ID_IRRIGAZIONE")));
					TipoIrrigazioneVO tipoIrrigazioneVO = new TipoIrrigazioneVO();
					tipoIrrigazioneVO.setIdIrrigazione(new Long(rs.getLong("ID_IRRIGAZIONE")));
					tipoIrrigazioneVO.setDescrizione(rs.getString("DESC_IRRIGAZIONE"));
					tipoIrrigazioneVO.setDataInizioValidita(rs.getDate("DATA_INI_IRRIGAZ"));
					tipoIrrigazioneVO.setDataFineValidita(rs.getDate("DATA_FINE_IRRIGAZ"));
					storicoParticellaVO.setTipoIrrigazioneVO(tipoIrrigazioneVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_POTENZIALITA_IRRIGUA"))) {
          storicoParticellaVO.setIdPotenzialitaIrrigua(new Long(rs.getLong("ID_POTENZIALITA_IRRIGUA")));
          TipoPotenzialitaIrriguaVO tipoPotenzialitaIrriguaVO = new TipoPotenzialitaIrriguaVO();
          tipoPotenzialitaIrriguaVO.setIdPotenzialitaIrrigua(new Long(rs.getLong("ID_POTENZIALITA_IRRIGUA")));
          tipoPotenzialitaIrriguaVO.setDescrizione(rs.getString("DESC_POTENZIALITA_IRRIGUA"));
          tipoPotenzialitaIrriguaVO.setCodice(rs.getString("COD_POTENZIALITA_IRRIGUA"));
          storicoParticellaVO.setTipoPotenzialitaIrriguaVO(tipoPotenzialitaIrriguaVO);
        }
				if(Validator.isNotEmpty(rs.getString("ID_TERRAZZAMENTO"))) {
          storicoParticellaVO.setIdTerrazzamento(new Long(rs.getLong("ID_TERRAZZAMENTO")));
          TipoTerrazzamentoVO tipoTerrazzamentoVO = new TipoTerrazzamentoVO();
          tipoTerrazzamentoVO.setIdTerrazzamento(new Long(rs.getLong("ID_TERRAZZAMENTO")));
          tipoTerrazzamentoVO.setDescrizione(rs.getString("DESC_TERRAZZAMENTO"));
          tipoTerrazzamentoVO.setCodice(rs.getString("COD_TERRAZZAMENTO"));
          storicoParticellaVO.setTipoTerrazzamentoVO(tipoTerrazzamentoVO);
        }
				if(Validator.isNotEmpty(rs.getString("ID_ROTAZIONE_COLTURALE"))) {
          storicoParticellaVO.setIdRotazioneColturale(new Long(rs.getLong("ID_ROTAZIONE_COLTURALE")));
          TipoRotazioneColturaleVO tipoRotazioneColturaleVO = new TipoRotazioneColturaleVO();
          tipoRotazioneColturaleVO.setIdRotazioneColturale(new Long(rs.getLong("ID_ROTAZIONE_COLTURALE")));
          tipoRotazioneColturaleVO.setDescrizione(rs.getString("DESC_ROTAZIONE_COLTURALE"));
          tipoRotazioneColturaleVO.setCodice(rs.getString("COD_ROTAZIONE_COLTURALE"));
          storicoParticellaVO.setTipoRotazioneColturaleVO(tipoRotazioneColturaleVO);
        }
				conduzioneParticellaVO.setIdConduzioneParticella(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));
				conduzioneParticellaVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
				conduzioneParticellaVO.setSuperficieCondotta(rs.getString("SUPERFICIE_CONDOTTA"));
				conduzioneParticellaVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
				conduzioneParticellaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
				storicoParticellaVO.setElencoConduzioni((ConduzioneParticellaVO[]) new ConduzioneParticellaVO[] {conduzioneParticellaVO});
				if(Validator.isNotEmpty(rs.getString("ID_FASCIA_FLUVIALE"))) {
					storicoParticellaVO.setIdFasciaFluviale(new Long(rs.getLong("ID_FASCIA_FLUVIALE")));
					CodeDescription fasciaFluviale = new CodeDescription(Integer.decode(rs.getString("ID_FASCIA_FLUVIALE")), rs.getString("DESC_FLUVIALE"));
					storicoParticellaVO.setFasciaFluviale(fasciaFluviale);
				}
				
				if(Validator.isNotEmpty(rs.getString("ID_PARTICELLA_CERTIFICATA")))
				{
				  ParticellaCertificataVO partVO = new ParticellaCertificataVO();
				  partVO.setIdParticellaCertificata(new Long(rs.getString("ID_PARTICELLA_CERTIFICATA")));
				  storicoParticellaVO.setParticellaCertificataVO(partVO);
				}
				
				
				storicoParticellaVO.setPercentualePendenzaMedia(rs.getBigDecimal("PERCENTUALE_PENDENZA_MEDIA"));
        storicoParticellaVO.setGradiPendenzaMedia(rs.getBigDecimal("GRADI_PENDENZA_MEDIA"));
        storicoParticellaVO.setGradiEsposizioneMedia(rs.getBigDecimal("GRADI_ESPOSIZIONE_MEDIA"));
        storicoParticellaVO.setMetriAltitudineMedia(rs.getBigDecimal("METRI_ALTITUDINE_MEDIA"));
				
        if(Validator.isNotEmpty(rs.getString("ID_METODO_IRRIGUO"))) {
          storicoParticellaVO.setIdMetodoIrriguo(new Long(rs.getLong("ID_METODO_IRRIGUO")));
          TipoMetodoIrriguoVO tipoMetodoIrriguo = new TipoMetodoIrriguoVO();
          tipoMetodoIrriguo.setCodiceMetodoIrriguo(rs.getString("CODICE_METODO_IRRIGUO"));
          tipoMetodoIrriguo.setDescrizioneMetodoIrriguo(rs.getString("DESCRIZIONE_METODO_IRRIGUO"));
          
          storicoParticellaVO.setTipoMetodoIrriguo(tipoMetodoIrriguo);
        }
				
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "findStoricoParticellaVOByIdConduzioneParticella in StoricoParticellaDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "findStoricoParticellaVOByIdConduzioneParticella in StoricoParticellaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "findStoricoParticellaVOByIdConduzioneParticella in StoricoParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "findStoricoParticellaVOByIdConduzioneParticella in StoricoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated findStoricoParticellaVOByIdConduzioneParticella method in StoricoParticellaDAO\n");
		return storicoParticellaVO;
	}
	
	/**
	 * Metodo che mi restituisce il record di DB_STORICO_PARTICELLA a partire
	 * dall'id_conduzione_dichiarata
	 * 
	 * @param idConduzioneDichiarata
	 * @return it.csi.solmr.dto.StoricoParticellaVO
	 * @throws DataAccessException
	 */
	public StoricoParticellaVO findStoricoParticellaVOByIdConduzioneDichiarata(Long idConduzioneDichiarata) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating findStoricoParticellaVOByIdConduzioneDichiarata method in StoricoParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		StoricoParticellaVO storicoParticellaVO = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in findStoricoParticellaVOByIdConduzioneDichiarata method in StoricoParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in findStoricoParticellaVOByIdConduzioneDichiarata method in StoricoParticellaDAO and it values: "+conn+"\n");

			String query = " SELECT SP.ID_STORICO_PARTICELLA, " +
						   "    SP.ID_PARTICELLA, " +
						   "    SP.DATA_INIZIO_VALIDITA, " +
						   " 		SP.DATA_FINE_VALIDITA, " +
						   " 		SP.COMUNE, " +
						   "		C.DESCOM, " +
						   "		P.SIGLA_PROVINCIA, " +
						   "		SP.SEZIONE, " +
						   "		SP.FOGLIO, " +
						   "		SP.PARTICELLA, " +
						   "		SP.SUBALTERNO, " +
						   "		SP.SUP_CATASTALE, " +
						   "    SP.SUPERFICIE_GRAFICA, " +
						   " 		SP.ID_ZONA_ALTIMETRICA, " +
						   "		TZA.DESCRIZIONE AS DESC_ALTIMETRICA, " +
						   "		SP.ID_AREA_A, " +
						   "		TAA.DESCRIZIONE AS DESC_AREA_A, " +
						   " 		SP.ID_AREA_B, " +
						   "		TAB.DESCRIZIONE AS DESC_AREA_B, " +
						   "		SP.ID_AREA_C, " +
						   "		TAC.DESCRIZIONE AS DESC_AREA_C, " +
						   "		SP.ID_AREA_D, " +
						   "		TAD.DESCRIZIONE AS DESC_AREA_D, " +
						   "    SP.ID_AREA_G, " +
               "    TAG.DESCRIZIONE AS DESC_AREA_G, " +
               "    SP.ID_AREA_H, " +
               "    TAH.DESCRIZIONE AS DESC_AREA_H, " +
               "    SP.ID_AREA_I, " +
               "    TAI.DESCRIZIONE AS DESC_AREA_I, " +
               "    SP.ID_AREA_L, " +
               "    TAL.DESCRIZIONE AS DESC_AREA_L, " +
               "    SP.ID_AREA_M, " +
               "    TAM.DESCRIZIONE AS DESC_AREA_M, " +
						   "		SP.FLAG_CAPTAZIONE_POZZI, " +
						   "		SP.FLAG_IRRIGABILE, " +
						   "		SP.ID_CASO_PARTICOLARE, " +
						   "		TCP.DESCRIZIONE AS DESC_CASO_PART, " +
						   "		SP.MOTIVO_MODIFICA, " +	
						   "		SP.DATA_AGGIORNAMENTO, " +
						   "		SP.ID_UTENTE_AGGIORNAMENTO, " +
						   "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
					       "          || ' ' " + 
					       "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
					       "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
					       "         WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
						   "    AS DENOMINAZIONE, " +
						   "       (SELECT PVU.DENOMINAZIONE " +
						   "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
						   "        WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
						   "    AS DESCRIZIONE_ENTE_APPARTENENZA, " +
						   "    SP.ID_CAUSALE_MOD_PARTICELLA, " +
						   "    TCMP.DESCRIZIONE AS DESC_CAUSALE_MOD, " +
						   "		SP.SUP_NON_ELEGGIBILE, " +
						   "		SP.SUP_NE_BOSCO_ACQUE_FABBRICATO, " +
						   "		SP.SUP_NE_FORAGGIERE, " +
						   "		SP.SUP_EL_FRUTTA_GUSCIO, " +
						   "		SP.SUP_EL_PRATO_PASCOLO, " +
						   "		SP.SUP_EL_COLTURE_MISTE, " +
						   "		SP.SUP_COLTIVAZ_ARBOREA_CONS, " +
						   "		SP.SUP_COLTIVAZ_ARBOREA_SPEC, " +
						   "		SP.DATA_FOTO, " +
						   "		SP.TIPO_FOTO, " +
						   "		SP.ID_FONTE, " +
						   "		TF.DESCRIZIONE AS DESC_FONTE, " +
						   "		SP.ID_DOCUMENTO , " +
						   "    PART.DATA_CESSAZIONE, " +
						   "    SP.ID_DOCUMENTO_PROTOCOLLATO, " +
						   "    SP.ID_IRRIGAZIONE, " +
						   "    SP.ID_FASCIA_FLUVIALE, " +
						   "    TFF.DESCRIZIONE AS DESC_FLUVIALE, " +
						   "    TI.DESCRIZIONE AS DESC_IRRIGAZIONE, " +
						   "    TI.DATA_INIZIO_VALIDITA AS DATA_INI_IRRIGAZ, " +
						   "    TI.DATA_FINE_VALIDITA AS DATA_FINE_IRRIGAZ, " +
						   "    SP.ID_POTENZIALITA_IRRIGUA, " +
               "    TPI.DESCRIZIONE AS DESC_POTENZIALITA_IRRIGUA, " +
               "    TPI.CODICE AS COD_POTENZIALITA_IRRIGUA, " +
               "    SP.ID_TERRAZZAMENTO, " +
               "    TTR.DESCRIZIONE AS DESC_TERRAZZAMENTO, " +
               "    TTR.CODICE AS COD_TERRAZZAMENTO, " +
               "    SP.ID_ROTAZIONE_COLTURALE, " +
               "    TRC.DESCRIZIONE AS DESC_ROTAZIONE_COLTURALE, " +
               "    TRC.CODICE AS COD_ROTAZIONE_COLTURALE, " +
               "    SP.PERCENTUALE_PENDENZA_MEDIA, " +
               "    SP.GRADI_PENDENZA_MEDIA, " +
               "    SP.GRADI_ESPOSIZIONE_MEDIA, " +
               "    SP.METRI_ALTITUDINE_MEDIA, " +
               "    SP.ID_METODO_IRRIGUO, " +
               "    TMI.DESCRIZIONE_METODO_IRRIGUO, " +
               "    TMI.CODICE_METODO_IRRIGUO " +
						   " FROM   DB_STORICO_PARTICELLA SP, " +
						   "		DB_CONDUZIONE_DICHIARATA CD, " +	
						   "		COMUNE C, " +
						   "		PROVINCIA P, " +
						   "		DB_TIPO_ZONA_ALTIMETRICA TZA, " +
						   "		DB_TIPO_AREA_A TAA, " +
						   "		DB_TIPO_AREA_B TAB, " +
						   "		DB_TIPO_AREA_C TAC, " +
						   "		DB_TIPO_AREA_D TAD, " +
						   "    DB_TIPO_AREA_G TAG, " +
						   "    DB_TIPO_AREA_H TAH, " +
						   "    DB_TIPO_AREA_I TAI, " +
						   "    DB_TIPO_AREA_L TAL, " +
						   "    DB_TIPO_AREA_M TAM, " +
						   "		DB_TIPO_CASO_PARTICOLARE TCP, " +
						   //"		PAPUA_V_UTENTE_LOGIN PVU, " +
						   "		DB_TIPO_CAUSALE_MOD_PARTICELLA TCMP, " +
						   "		DB_TIPO_FONTE TF, " +
						   " 		DB_TIPO_DOCUMENTO TD , " +
						   "    DB_PARTICELLA PART, " +
						   "    DB_TIPO_IRRIGAZIONE TI, " +
						   "    DB_TIPO_FASCIA_FLUVIALE TFF, " +
						   "    DB_TIPO_POTENZIALITA_IRRIGUA TPI, " +
               "    DB_TIPO_TERRAZZAMENTO TTR, " +
               "    DB_TIPO_ROTAZIONE_COLTURALE TRC, " +
               "    DB_TIPO_METODO_IRRIGUO TMI " +
						   " WHERE  CD.ID_CONDUZIONE_DICHIARATA = ? " +
						   " AND    CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
						   " AND    SP.COMUNE = C.ISTAT_COMUNE " +
						   " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) " +
						   " AND    SP.ID_ZONA_ALTIMETRICA = TZA.ID_ZONA_ALTIMETRICA(+) " + 
						   " AND    SP.ID_AREA_A = TAA.ID_AREA_A(+) " +
						   " AND    SP.ID_AREA_B = TAB.ID_AREA_B(+) " + 
						   " AND    SP.ID_AREA_C = TAC.ID_AREA_C(+) " +
						   " AND    SP.ID_AREA_D = TAD.ID_AREA_D(+) " +
						   " AND    SP.ID_AREA_G = TAG.ID_AREA_G(+) " +
						   " AND    SP.ID_AREA_H = TAH.ID_AREA_H(+) " +
						   " AND    SP.ID_AREA_I = TAI.ID_AREA_I(+) " +
						   " AND    SP.ID_AREA_L = TAL.ID_AREA_L(+) " +
						   " AND    SP.ID_AREA_M = TAM.ID_AREA_M(+) " +
						   " AND    SP.ID_CASO_PARTICOLARE = TCP.ID_CASO_PARTICOLARE(+) " +
						   //" AND    SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
						   " AND    SP.ID_CAUSALE_MOD_PARTICELLA = TCMP.ID_CAUSALE_MOD_PARTICELLA(+) " +
						   " AND    SP.ID_FONTE = TF.ID_FONTE(+) " +
						   " AND    SP.ID_DOCUMENTO = TD.ID_DOCUMENTO(+) " +
						   " AND    SP.ID_PARTICELLA = PART.ID_PARTICELLA " +
						   " AND    SP.ID_IRRIGAZIONE = TI.ID_IRRIGAZIONE(+) " +
						   " AND    SP.ID_FASCIA_FLUVIALE = TFF.ID_FASCIA_FLUVIALE(+) " +
          		 " AND    SP.ID_POTENZIALITA_IRRIGUA = TPI.ID_POTENZIALITA_IRRIGUA(+) " +
               " AND    SP.ID_TERRAZZAMENTO = TTR.ID_TERRAZZAMENTO(+) " +
               " AND    SP.ID_ROTAZIONE_COLTURALE = TRC.ID_ROTAZIONE_COLTURALE(+) " +
               " AND    SP.ID_METODO_IRRIGUO = TMI.ID_METODO_IRRIGUO (+) ";

			SolmrLogger.debug(this, "Value of parameter 1 [ID_CONDUZIONE_DICHIARATA] in findStoricoParticellaVOByIdConduzioneDichiarata method in StoricoParticellaDAO: "+idConduzioneDichiarata+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idConduzioneDichiarata.longValue());

			SolmrLogger.debug(this, "Executing findStoricoParticellaVOByIdConduzioneDichiarata: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) 
			{
				storicoParticellaVO = new StoricoParticellaVO(); 
				storicoParticellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
				storicoParticellaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
				storicoParticellaVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				storicoParticellaVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
				ComuneVO comuneVO = new ComuneVO();
				comuneVO.setIstatComune(rs.getString("COMUNE"));
				comuneVO.setDescom(rs.getString("DESCOM"));
				comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
				if(Validator.isNotEmpty(rs.getString("SIGLA_PROVINCIA"))) {
					ProvinciaVO provinciaVO = new ProvinciaVO();
					provinciaVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));
					comuneVO.setProvinciaVO(provinciaVO);
				}
				storicoParticellaVO.setComuneParticellaVO(comuneVO);
				storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
				storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
				storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
				storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
				storicoParticellaVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
				storicoParticellaVO.setSuperficieGrafica(rs.getString("SUPERFICIE_GRAFICA"));
				if(Validator.isNotEmpty(rs.getString("ID_ZONA_ALTIMETRICA"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_ZONA_ALTIMETRICA")), rs.getString("DESC_ALTIMETRICA"));
					storicoParticellaVO.setIdZonaAltimetrica(Long.decode(rs.getString("ID_ZONA_ALTIMETRICA")));
					storicoParticellaVO.setZonaAltimetrica(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_AREA_A"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_A")), rs.getString("DESC_AREA_A"));
					storicoParticellaVO.setIdAreaA(new Long(rs.getString("ID_AREA_A")));
					storicoParticellaVO.setAreaA(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_AREA_B"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_B")), rs.getString("DESC_AREA_B"));
					storicoParticellaVO.setIdAreaB(new Long(rs.getString("ID_AREA_B")));
					storicoParticellaVO.setAreaB(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_AREA_C"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_C")), rs.getString("DESC_AREA_C"));
					storicoParticellaVO.setIdAreaC(new Long(rs.getString("ID_AREA_C")));
					storicoParticellaVO.setAreaC(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_AREA_D"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_D")), rs.getString("DESC_AREA_D"));
					storicoParticellaVO.setIdAreaD(new Long(rs.getString("ID_AREA_D")));
					storicoParticellaVO.setAreaD(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_AREA_G"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_G")), rs.getString("DESC_AREA_G"));
          storicoParticellaVO.setIdAreaG(new Long(rs.getString("ID_AREA_G")));
          storicoParticellaVO.setAreaG(code);
        }
				if(Validator.isNotEmpty(rs.getString("ID_AREA_H"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_H")), rs.getString("DESC_AREA_H"));
          storicoParticellaVO.setIdAreaH(new Long(rs.getString("ID_AREA_H")));
          storicoParticellaVO.setAreaH(code);
        }
				if(Validator.isNotEmpty(rs.getString("ID_AREA_I"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_I")), rs.getString("DESC_AREA_I"));
          storicoParticellaVO.setIdAreaI(new Long(rs.getString("ID_AREA_I")));
          storicoParticellaVO.setAreaI(code);
        }
				if(Validator.isNotEmpty(rs.getString("ID_AREA_L"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_L")), rs.getString("DESC_AREA_L"));
          storicoParticellaVO.setIdAreaL(new Long(rs.getString("ID_AREA_L")));
          storicoParticellaVO.setAreaL(code);
        }
				if(Validator.isNotEmpty(rs.getString("ID_AREA_M"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_M")), rs.getString("DESC_AREA_M"));
          storicoParticellaVO.setIdAreaM(new Long(rs.getString("ID_AREA_M")));
          storicoParticellaVO.setAreaM(code);
        }
				storicoParticellaVO.setFlagCaptazionePozzi(rs.getString("FLAG_CAPTAZIONE_POZZI"));
				storicoParticellaVO.setFlagIrrigabile(rs.getString("FLAG_IRRIGABILE"));
				if(Validator.isNotEmpty(rs.getString("ID_CASO_PARTICOLARE"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_CASO_PARTICOLARE")), rs.getString("DESC_CASO_PART"));
					storicoParticellaVO.setIdCasoParticolare(new Long(rs.getString("ID_CASO_PARTICOLARE")));
					storicoParticellaVO.setCasoParticolare(code);
				}
				storicoParticellaVO.setMotivoModifica(rs.getString("MOTIVO_MODIFICA"));
				storicoParticellaVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
				storicoParticellaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				UtenteIrideVO utenteIrideVO = new UtenteIrideVO();
				utenteIrideVO.setIdUtente(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				utenteIrideVO.setDenominazione(rs.getString("DENOMINAZIONE"));
				utenteIrideVO.setDescrizioneEnteAppartenenza(rs.getString("DESCRIZIONE_ENTE_APPARTENENZA"));
				storicoParticellaVO.setUtenteAggiornamento(utenteIrideVO);
				if(Validator.isNotEmpty(rs.getString("ID_CAUSALE_MOD_PARTICELLA"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_CAUSALE_MOD_PARTICELLA")), rs.getString("DESC_CAUSALE_MOD"));
					storicoParticellaVO.setIdCausaleModParticella(new Long(rs.getString("ID_CAUSALE_MOD_PARTICELLA")));
					storicoParticellaVO.setCausaleModParticella(code);
				}
				storicoParticellaVO.setSupNonEleggibile(rs.getString("SUP_NON_ELEGGIBILE"));
				storicoParticellaVO.setSupNeBoscoAcqueFabbricato(rs.getString("SUP_NE_BOSCO_ACQUE_FABBRICATO"));
				storicoParticellaVO.setSupNeForaggere(rs.getString("SUP_NE_FORAGGIERE"));
				storicoParticellaVO.setSupElFruttaGuscio(rs.getString("SUP_EL_FRUTTA_GUSCIO"));
				storicoParticellaVO.setSupElPratoPascolo(rs.getString("SUP_EL_PRATO_PASCOLO"));
				storicoParticellaVO.setSupElColtureMiste(rs.getString("SUP_EL_COLTURE_MISTE"));
				storicoParticellaVO.setSupColtivazArboreaCons(rs.getString("SUP_COLTIVAZ_ARBOREA_CONS"));
				storicoParticellaVO.setSupColtivazArboreaSpec(rs.getString("SUP_COLTIVAZ_ARBOREA_SPEC"));
				storicoParticellaVO.setDataFoto(rs.getDate("DATA_FOTO"));
				storicoParticellaVO.setTipoFoto(rs.getString("TIPO_FOTO"));
				if(Validator.isNotEmpty(rs.getString("ID_FONTE"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_FONTE")), rs.getString("DESC_FONTE"));
					storicoParticellaVO.setIdFonte(new Long(rs.getString("ID_FONTE")));
					storicoParticellaVO.setFonte(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_DOCUMENTO"))) {
					storicoParticellaVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
				}
				storicoParticellaVO.setDataCessazione(rs.getDate("DATA_CESSAZIONE"));
				if(Validator.isNotEmpty(rs.getString("ID_DOCUMENTO_PROTOCOLLATO"))) {
					storicoParticellaVO.setIdDocumentoProtocollato(new Long(rs.getString("ID_DOCUMENTO_PROTOCOLLATO")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_IRRIGAZIONE"))) {
					storicoParticellaVO.setIdIrrigazione(new Long(rs.getLong("ID_IRRIGAZIONE")));
					TipoIrrigazioneVO tipoIrrigazioneVO = new TipoIrrigazioneVO();
					tipoIrrigazioneVO.setIdIrrigazione(new Long(rs.getLong("ID_IRRIGAZIONE")));
					tipoIrrigazioneVO.setDescrizione(rs.getString("DESC_IRRIGAZIONE"));
					tipoIrrigazioneVO.setDataInizioValidita(rs.getDate("DATA_INI_IRRIGAZ"));
					tipoIrrigazioneVO.setDataFineValidita(rs.getDate("DATA_FINE_IRRIGAZ"));
					storicoParticellaVO.setTipoIrrigazioneVO(tipoIrrigazioneVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_FASCIA_FLUVIALE"))) {
					storicoParticellaVO.setIdFasciaFluviale(new Long(rs.getLong("ID_FASCIA_FLUVIALE")));
					CodeDescription fasciaFluviale = new CodeDescription(Integer.decode(rs.getString("ID_FASCIA_FLUVIALE")), rs.getString("DESC_FLUVIALE"));
					storicoParticellaVO.setFasciaFluviale(fasciaFluviale);
				}
				if(Validator.isNotEmpty(rs.getString("ID_POTENZIALITA_IRRIGUA"))) {
          storicoParticellaVO.setIdPotenzialitaIrrigua(new Long(rs.getLong("ID_POTENZIALITA_IRRIGUA")));
          TipoPotenzialitaIrriguaVO tipoPotenzialitaIrriguaVO = new TipoPotenzialitaIrriguaVO();
          tipoPotenzialitaIrriguaVO.setIdPotenzialitaIrrigua(new Long(rs.getLong("ID_POTENZIALITA_IRRIGUA")));
          tipoPotenzialitaIrriguaVO.setDescrizione(rs.getString("DESC_POTENZIALITA_IRRIGUA"));
          tipoPotenzialitaIrriguaVO.setCodice(rs.getString("COD_POTENZIALITA_IRRIGUA"));
          storicoParticellaVO.setTipoPotenzialitaIrriguaVO(tipoPotenzialitaIrriguaVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_TERRAZZAMENTO"))) {
          storicoParticellaVO.setIdTerrazzamento(new Long(rs.getLong("ID_TERRAZZAMENTO")));
          TipoTerrazzamentoVO tipoTerrazzamentoVO = new TipoTerrazzamentoVO();
          tipoTerrazzamentoVO.setIdTerrazzamento(new Long(rs.getLong("ID_TERRAZZAMENTO")));
          tipoTerrazzamentoVO.setDescrizione(rs.getString("DESC_TERRAZZAMENTO"));
          tipoTerrazzamentoVO.setCodice(rs.getString("COD_TERRAZZAMENTO"));
          storicoParticellaVO.setTipoTerrazzamentoVO(tipoTerrazzamentoVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_ROTAZIONE_COLTURALE"))) {
          storicoParticellaVO.setIdRotazioneColturale(new Long(rs.getLong("ID_ROTAZIONE_COLTURALE")));
          TipoRotazioneColturaleVO tipoRotazioneColturaleVO = new TipoRotazioneColturaleVO();
          tipoRotazioneColturaleVO.setIdRotazioneColturale(new Long(rs.getLong("ID_ROTAZIONE_COLTURALE")));
          tipoRotazioneColturaleVO.setDescrizione(rs.getString("DESC_ROTAZIONE_COLTURALE"));
          tipoRotazioneColturaleVO.setCodice(rs.getString("COD_ROTAZIONE_COLTURALE"));
          storicoParticellaVO.setTipoRotazioneColturaleVO(tipoRotazioneColturaleVO);
        }
        
        storicoParticellaVO.setPercentualePendenzaMedia(rs.getBigDecimal("PERCENTUALE_PENDENZA_MEDIA"));
        storicoParticellaVO.setGradiPendenzaMedia(rs.getBigDecimal("GRADI_PENDENZA_MEDIA"));
        storicoParticellaVO.setGradiEsposizioneMedia(rs.getBigDecimal("GRADI_ESPOSIZIONE_MEDIA"));
        storicoParticellaVO.setMetriAltitudineMedia(rs.getBigDecimal("METRI_ALTITUDINE_MEDIA"));
			
        if(Validator.isNotEmpty(rs.getString("ID_METODO_IRRIGUO"))) {
          storicoParticellaVO.setIdMetodoIrriguo(new Long(rs.getLong("ID_METODO_IRRIGUO")));
          TipoMetodoIrriguoVO tipoMetodoIrriguo = new TipoMetodoIrriguoVO();
          tipoMetodoIrriguo.setCodiceMetodoIrriguo(rs.getString("CODICE_METODO_IRRIGUO"));
          tipoMetodoIrriguo.setDescrizioneMetodoIrriguo(rs.getString("DESCRIZIONE_METODO_IRRIGUO"));
          
          storicoParticellaVO.setTipoMetodoIrriguo(tipoMetodoIrriguo);
        }
			
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "findStoricoParticellaVOByIdConduzioneDichiarata in StoricoParticellaDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "findStoricoParticellaVOByIdConduzioneDichiarata in StoricoParticellaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "findStoricoParticellaVOByIdConduzioneDichiarata in StoricoParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "findStoricoParticellaVOByIdConduzioneDichiarata in StoricoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated findStoricoParticellaVOByIdConduzioneDichiarata method in StoricoParticellaDAO\n");
		return storicoParticellaVO;
	}
	
	/**
	 * Metodo che mi restituisce il record di DB_STORICO_PARTICELLA a partire
	 * dalla sua chiave primaria
	 * 
	 * @param idConduzioneParticella
	 * @return it.csi.solmr.dto.StoricoParticellaVO
	 * @throws DataAccessException
	 */
	public StoricoParticellaVO findStoricoParticellaByPrimaryKey(Long idStoricoParticella) throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating findStoricoParticellaByPrimaryKey method in StoricoParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		StoricoParticellaVO storicoParticellaVO = null;

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in findStoricoParticellaByPrimaryKey method in StoricoParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in findStoricoParticellaByPrimaryKey method in StoricoParticellaDAO and it values: "+conn+"\n");

			String query = " SELECT SP.ID_STORICO_PARTICELLA, " +
						   "    SP.ID_PARTICELLA, " +
						   "    SP.DATA_INIZIO_VALIDITA, " +
						   " 		SP.DATA_FINE_VALIDITA, " +
						   " 		SP.COMUNE, " +
						   "		C.DESCOM, " +
						   "		P.SIGLA_PROVINCIA, " +
						   "		SP.SEZIONE, " +
						   "		SP.FOGLIO, " +
						   "		SP.PARTICELLA, " +
						   "		SP.SUBALTERNO, " +
						   "		SP.SUP_CATASTALE, " +
						   "    SP.SUPERFICIE_GRAFICA, " +
						   " 		SP.ID_ZONA_ALTIMETRICA, " +
						   "    SP.ID_FASCIA_FLUVIALE, " +
						   "		TZA.DESCRIZIONE AS DESC_ALTIMETRICA, " +
						   "		SP.ID_AREA_A, " +
						   "		TAA.DESCRIZIONE AS DESC_AREA_A, " +
						   " 		SP.ID_AREA_B, " +
						   "		TAB.DESCRIZIONE AS DESC_AREA_B, " +
						   "		SP.ID_AREA_C, " +
						   "		TAC.DESCRIZIONE AS DESC_AREA_C, " +
						   "		SP.ID_AREA_D, " +
						   "		TAD.DESCRIZIONE AS DESC_AREA_D, " +
						   "    SP.ID_AREA_G, " +
               "    TAG.DESCRIZIONE AS DESC_AREA_G, " +
               "    SP.ID_AREA_H, " +
               "    TAH.DESCRIZIONE AS DESC_AREA_H, " +
               "    SP.ID_AREA_I, " +
               "    TAI.DESCRIZIONE AS DESC_AREA_I, " +
               "    SP.ID_AREA_L, " +
               "    TAL.DESCRIZIONE AS DESC_AREA_L, " +
               "    SP.ID_AREA_M, " +
               "    TAM.DESCRIZIONE AS DESC_AREA_M, " +
						   "		SP.FLAG_CAPTAZIONE_POZZI, " +
						   "		SP.FLAG_IRRIGABILE, " +
						   "		SP.ID_CASO_PARTICOLARE, " +
						   "		TCP.DESCRIZIONE AS DESC_CASO_PART, " +
						   "		SP.MOTIVO_MODIFICA, " +	
						   "		SP.DATA_AGGIORNAMENTO, " +
						   "		SP.ID_UTENTE_AGGIORNAMENTO, " +
						   "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
					       "          || ' ' " + 
					       "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
					       "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
					       "         WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
						   "    AS DENOMINAZIONE, " +
						   "       (SELECT PVU.DENOMINAZIONE " +
						   "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
						   "        WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
						   "    AS DESCRIZIONE_ENTE_APPARTENENZA, " +
						   "    SP.ID_CAUSALE_MOD_PARTICELLA, " +
						   "    TCMP.DESCRIZIONE AS DESC_CAUSALE_MOD, " +
						   "		SP.SUP_NON_ELEGGIBILE, " +
						   "		SP.SUP_NE_BOSCO_ACQUE_FABBRICATO, " +
						   "		SP.SUP_NE_FORAGGIERE, " +
						   "		SP.SUP_EL_FRUTTA_GUSCIO, " +
						   "		SP.SUP_EL_PRATO_PASCOLO, " +
						   "		SP.SUP_EL_COLTURE_MISTE, " +
						   "		SP.SUP_COLTIVAZ_ARBOREA_CONS, " +
						   "		SP.SUP_COLTIVAZ_ARBOREA_SPEC, " +
						   "		SP.DATA_FOTO, " +
						   "		SP.TIPO_FOTO, " +
						   "		SP.ID_FONTE, " +
						   "		TF.DESCRIZIONE AS DESC_FONTE, " +
						   "		SP.ID_DOCUMENTO, " +
						   "    PART.DATA_CESSAZIONE, " +
						   "    SP.ID_DOCUMENTO_PROTOCOLLATO, " +
						   "    SP.ID_IRRIGAZIONE, " +
						   "    TI.DESCRIZIONE AS DESC_IRRIGAZIONE, " +
						   "    TI.DATA_INIZIO_VALIDITA AS DATA_INI_IRRIGAZ, " +
						   "    TI.DATA_FINE_VALIDITA AS DATA_FINE_IRRIGAZ, " +
						   "    SP.ID_POTENZIALITA_IRRIGUA, " +
               "    TPI.DESCRIZIONE AS DESC_POTENZIALITA_IRRIGUA, " +
               "    TPI.CODICE AS COD_POTENZIALITA_IRRIGUA, " +
               "    SP.ID_TERRAZZAMENTO, " +
               "    TTR.DESCRIZIONE AS DESC_TERRAZZAMENTO, " +
               "    TTR.CODICE AS COD_TERRAZZAMENTO, " +
               "    SP.ID_ROTAZIONE_COLTURALE, " +
               "    TRC.DESCRIZIONE AS DESC_ROTAZIONE_COLTURALE, " +
               "    TRC.CODICE AS COD_ROTAZIONE_COLTURALE, " +
               "    SP.PERCENTUALE_PENDENZA_MEDIA, " +
               "    SP.GRADI_PENDENZA_MEDIA, " +
               "    SP.GRADI_ESPOSIZIONE_MEDIA, " +
               "    SP.METRI_ALTITUDINE_MEDIA, " +
               "    SP.ID_METODO_IRRIGUO, " +
               "    TMI.DESCRIZIONE_METODO_IRRIGUO, " +
               "    TMI.CODICE_METODO_IRRIGUO " +
						   " FROM   DB_STORICO_PARTICELLA SP, " +
						   "		COMUNE C, " +
						   "		PROVINCIA P, " +
						   "		DB_TIPO_ZONA_ALTIMETRICA TZA, " +
						   "		DB_TIPO_AREA_A TAA, " +
						   "		DB_TIPO_AREA_B TAB, " +
						   "		DB_TIPO_AREA_C TAC, " +
						   "		DB_TIPO_AREA_D TAD, " +
						   "    DB_TIPO_AREA_G TAG, " +
						   "    DB_TIPO_AREA_H TAH, " +
						   "    DB_TIPO_AREA_I TAI, " +
						   "    DB_TIPO_AREA_L TAL, " +
						   "    DB_TIPO_AREA_M TAM, " +
						   "		DB_TIPO_CASO_PARTICOLARE TCP, " +
						   //"		PAPUA_V_UTENTE_LOGIN PVU, " +
						   "		DB_TIPO_CAUSALE_MOD_PARTICELLA TCMP, " +
						   "		DB_TIPO_FONTE TF, " +
						   " 		DB_TIPO_DOCUMENTO TD, " +
						   "    DB_PARTICELLA PART, " +
						   "    DB_TIPO_IRRIGAZIONE TI, " +
						   "    DB_TIPO_POTENZIALITA_IRRIGUA TPI, " +
               "    DB_TIPO_TERRAZZAMENTO TTR, " +
               "    DB_TIPO_ROTAZIONE_COLTURALE TRC, " +
               "    DB_TIPO_METODO_IRRIGUO TMI " +
						   " WHERE  SP.ID_STORICO_PARTICELLA = ? " +
						   " AND    PART.ID_PARTICELLA = SP.ID_PARTICELLA " +
						   " AND    SP.COMUNE = C.ISTAT_COMUNE " +
						   " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) " +
						   " AND    SP.ID_ZONA_ALTIMETRICA = TZA.ID_ZONA_ALTIMETRICA(+) " + 
						   " AND    SP.ID_AREA_A = TAA.ID_AREA_A(+) " +
						   " AND    SP.ID_AREA_B = TAB.ID_AREA_B(+) " + 
						   " AND    SP.ID_AREA_C = TAC.ID_AREA_C(+) " +
						   " AND    SP.ID_AREA_D = TAD.ID_AREA_D(+) " +
						   " AND    SP.ID_AREA_G = TAG.ID_AREA_G(+) " +
						   " AND    SP.ID_AREA_H = TAH.ID_AREA_H(+) " +
						   " AND    SP.ID_AREA_I = TAI.ID_AREA_I(+) " +
						   " AND    SP.ID_AREA_L = TAL.ID_AREA_L(+) " +
						   " AND    SP.ID_AREA_M = TAM.ID_AREA_M(+) " +
						   " AND    SP.ID_CASO_PARTICOLARE = TCP.ID_CASO_PARTICOLARE(+) " +
						   //" AND    SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
						   " AND    SP.ID_CAUSALE_MOD_PARTICELLA = TCMP.ID_CAUSALE_MOD_PARTICELLA(+) " +
						   " AND    SP.ID_FONTE = TF.ID_FONTE(+) " +
						   " AND    SP.ID_DOCUMENTO = TD.ID_DOCUMENTO(+) " +
						   " AND    SP.ID_PARTICELLA = PART.ID_PARTICELLA " +
						   " AND    SP.ID_IRRIGAZIONE = TI.ID_IRRIGAZIONE(+) " +
						   " AND    SP.ID_POTENZIALITA_IRRIGUA = TPI.ID_POTENZIALITA_IRRIGUA(+) " +
               " AND    SP.ID_TERRAZZAMENTO = TTR.ID_TERRAZZAMENTO(+) " +
               " AND    SP.ID_ROTAZIONE_COLTURALE = TRC.ID_ROTAZIONE_COLTURALE(+) " +
               " AND    SP.ID_METODO_IRRIGUO = TMI.ID_METODO_IRRIGUO (+) ";

			SolmrLogger.debug(this, "Value of parameter 1 [ID_STORICO_PARTICELLA] in findStoricoParticellaByPrimaryKey method in StoricoParticellaDAO: "+idStoricoParticella+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idStoricoParticella.longValue());

			SolmrLogger.debug(this, "Executing findStoricoParticellaByPrimaryKey: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) 
			{
				storicoParticellaVO = new StoricoParticellaVO(); 
				storicoParticellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
				storicoParticellaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
				storicoParticellaVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				storicoParticellaVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
				ComuneVO comuneVO = new ComuneVO();
				comuneVO.setIstatComune(rs.getString("COMUNE"));
				comuneVO.setDescom(rs.getString("DESCOM"));
				if(Validator.isNotEmpty(rs.getString("SIGLA_PROVINCIA"))) 
				{
					ProvinciaVO provinciaVO = new ProvinciaVO();
					provinciaVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));
					comuneVO.setProvinciaVO(provinciaVO);
				}
				storicoParticellaVO.setComuneParticellaVO(comuneVO);
				storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
				storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
				storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
				storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
				storicoParticellaVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
				storicoParticellaVO.setSuperficieGrafica(rs.getString("SUPERFICIE_GRAFICA"));
				if(Validator.isNotEmpty(rs.getString("ID_ZONA_ALTIMETRICA"))) 
				{
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_ZONA_ALTIMETRICA")), rs.getString("DESC_ALTIMETRICA"));
					storicoParticellaVO.setIdZonaAltimetrica(Long.decode(rs.getString("ID_ZONA_ALTIMETRICA")));
					storicoParticellaVO.setZonaAltimetrica(code);
				}
				storicoParticellaVO.setIdFasciaFluviale(checkLongNull(rs.getString("ID_FASCIA_FLUVIALE")));
				if(Validator.isNotEmpty(rs.getString("ID_AREA_A"))) 
				{
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_A")), rs.getString("DESC_AREA_A"));
					storicoParticellaVO.setIdAreaA(new Long(rs.getString("ID_AREA_A")));
					storicoParticellaVO.setAreaA(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_AREA_B"))) 
				{
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_B")), rs.getString("DESC_AREA_B"));
					storicoParticellaVO.setIdAreaB(new Long(rs.getString("ID_AREA_B")));
					storicoParticellaVO.setAreaB(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_AREA_C"))) 
				{
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_C")), rs.getString("DESC_AREA_C"));
					storicoParticellaVO.setIdAreaC(new Long(rs.getString("ID_AREA_C")));
					storicoParticellaVO.setAreaC(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_AREA_D"))) 
				{
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_D")), rs.getString("DESC_AREA_D"));
					storicoParticellaVO.setIdAreaD(new Long(rs.getString("ID_AREA_D")));
					storicoParticellaVO.setAreaD(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_AREA_G"))) 
				{
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_G")), rs.getString("DESC_AREA_G"));
          storicoParticellaVO.setIdAreaG(new Long(rs.getString("ID_AREA_G")));
          storicoParticellaVO.setAreaG(code);
        }
				if(Validator.isNotEmpty(rs.getString("ID_AREA_H"))) 
				{
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_H")), rs.getString("DESC_AREA_H"));
          storicoParticellaVO.setIdAreaH(new Long(rs.getString("ID_AREA_H")));
          storicoParticellaVO.setAreaH(code);
        }
				if(Validator.isNotEmpty(rs.getString("ID_AREA_I"))) 
        {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_I")), rs.getString("DESC_AREA_I"));
          storicoParticellaVO.setIdAreaI(new Long(rs.getString("ID_AREA_I")));
          storicoParticellaVO.setAreaI(code);
        }
				if(Validator.isNotEmpty(rs.getString("ID_AREA_L"))) 
        {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_L")), rs.getString("DESC_AREA_L"));
          storicoParticellaVO.setIdAreaL(new Long(rs.getString("ID_AREA_L")));
          storicoParticellaVO.setAreaL(code);
        }
				if(Validator.isNotEmpty(rs.getString("ID_AREA_M"))) 
        {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_M")), rs.getString("DESC_AREA_M"));
          storicoParticellaVO.setIdAreaM(new Long(rs.getString("ID_AREA_M")));
          storicoParticellaVO.setAreaM(code);
        }
				storicoParticellaVO.setFlagCaptazionePozzi(rs.getString("FLAG_CAPTAZIONE_POZZI"));
				storicoParticellaVO.setFlagIrrigabile(rs.getString("FLAG_IRRIGABILE"));
				if(Validator.isNotEmpty(rs.getString("ID_CASO_PARTICOLARE"))) 
				{
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_CASO_PARTICOLARE")), rs.getString("DESC_CASO_PART"));
					storicoParticellaVO.setIdCasoParticolare(new Long(rs.getString("ID_CASO_PARTICOLARE")));
					storicoParticellaVO.setCasoParticolare(code);
				}
				storicoParticellaVO.setMotivoModifica(rs.getString("MOTIVO_MODIFICA"));
				storicoParticellaVO.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO"));
				storicoParticellaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				UtenteIrideVO utenteIrideVO = new UtenteIrideVO();
				utenteIrideVO.setIdUtente(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				utenteIrideVO.setDenominazione(rs.getString("DENOMINAZIONE"));
				utenteIrideVO.setDescrizioneEnteAppartenenza(rs.getString("DESCRIZIONE_ENTE_APPARTENENZA"));
				storicoParticellaVO.setUtenteAggiornamento(utenteIrideVO);
				if(Validator.isNotEmpty(rs.getString("ID_CAUSALE_MOD_PARTICELLA"))) 
				{
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_CAUSALE_MOD_PARTICELLA")), rs.getString("DESC_CAUSALE_MOD"));
					storicoParticellaVO.setIdCausaleModParticella(new Long(rs.getString("ID_CAUSALE_MOD_PARTICELLA")));
					storicoParticellaVO.setCausaleModParticella(code);
				}
				storicoParticellaVO.setSupNonEleggibile(rs.getString("SUP_NON_ELEGGIBILE"));
				storicoParticellaVO.setSupNeBoscoAcqueFabbricato(rs.getString("SUP_NE_BOSCO_ACQUE_FABBRICATO"));
				storicoParticellaVO.setSupNeForaggere(rs.getString("SUP_NE_FORAGGIERE"));
				storicoParticellaVO.setSupElFruttaGuscio(rs.getString("SUP_EL_FRUTTA_GUSCIO"));
				storicoParticellaVO.setSupElPratoPascolo(rs.getString("SUP_EL_PRATO_PASCOLO"));
				storicoParticellaVO.setSupElColtureMiste(rs.getString("SUP_EL_COLTURE_MISTE"));
				storicoParticellaVO.setSupColtivazArboreaCons(rs.getString("SUP_COLTIVAZ_ARBOREA_CONS"));
				storicoParticellaVO.setSupColtivazArboreaSpec(rs.getString("SUP_COLTIVAZ_ARBOREA_SPEC"));
				storicoParticellaVO.setDataFoto(rs.getDate("DATA_FOTO"));
				storicoParticellaVO.setTipoFoto(rs.getString("TIPO_FOTO"));
				if(Validator.isNotEmpty(rs.getString("ID_FONTE"))) 
				{
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_FONTE")), rs.getString("DESC_FONTE"));
					storicoParticellaVO.setIdFonte(new Long(rs.getString("ID_FONTE")));
					storicoParticellaVO.setFonte(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_DOCUMENTO"))) 
				{
					storicoParticellaVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
				}
				storicoParticellaVO.setDataCessazione(rs.getDate("DATA_CESSAZIONE"));
				if(Validator.isNotEmpty(rs.getString("ID_DOCUMENTO_PROTOCOLLATO"))) {
					storicoParticellaVO.setIdDocumentoProtocollato(new Long(rs.getString("ID_DOCUMENTO_PROTOCOLLATO")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_IRRIGAZIONE"))) 
				{
					storicoParticellaVO.setIdIrrigazione(new Long(rs.getLong("ID_IRRIGAZIONE")));
					TipoIrrigazioneVO tipoIrrigazioneVO = new TipoIrrigazioneVO();
					tipoIrrigazioneVO.setIdIrrigazione(new Long(rs.getLong("ID_IRRIGAZIONE")));
					tipoIrrigazioneVO.setDescrizione(rs.getString("DESC_IRRIGAZIONE"));
					tipoIrrigazioneVO.setDataInizioValidita(rs.getDate("DATA_INI_IRRIGAZ"));
					tipoIrrigazioneVO.setDataFineValidita(rs.getDate("DATA_FINE_IRRIGAZ"));
					storicoParticellaVO.setTipoIrrigazioneVO(tipoIrrigazioneVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_POTENZIALITA_IRRIGUA"))) {
          storicoParticellaVO.setIdPotenzialitaIrrigua(new Long(rs.getLong("ID_POTENZIALITA_IRRIGUA")));
          TipoPotenzialitaIrriguaVO tipoPotenzialitaIrriguaVO = new TipoPotenzialitaIrriguaVO();
          tipoPotenzialitaIrriguaVO.setIdPotenzialitaIrrigua(new Long(rs.getLong("ID_POTENZIALITA_IRRIGUA")));
          tipoPotenzialitaIrriguaVO.setDescrizione(rs.getString("DESC_POTENZIALITA_IRRIGUA"));
          tipoPotenzialitaIrriguaVO.setCodice(rs.getString("COD_POTENZIALITA_IRRIGUA"));
          storicoParticellaVO.setTipoPotenzialitaIrriguaVO(tipoPotenzialitaIrriguaVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_TERRAZZAMENTO"))) {
          storicoParticellaVO.setIdTerrazzamento(new Long(rs.getLong("ID_TERRAZZAMENTO")));
          TipoTerrazzamentoVO tipoTerrazzamentoVO = new TipoTerrazzamentoVO();
          tipoTerrazzamentoVO.setIdTerrazzamento(new Long(rs.getLong("ID_TERRAZZAMENTO")));
          tipoTerrazzamentoVO.setDescrizione(rs.getString("DESC_TERRAZZAMENTO"));
          tipoTerrazzamentoVO.setCodice(rs.getString("COD_TERRAZZAMENTO"));
          storicoParticellaVO.setTipoTerrazzamentoVO(tipoTerrazzamentoVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_ROTAZIONE_COLTURALE"))) {
          storicoParticellaVO.setIdRotazioneColturale(new Long(rs.getLong("ID_ROTAZIONE_COLTURALE")));
          TipoRotazioneColturaleVO tipoRotazioneColturaleVO = new TipoRotazioneColturaleVO();
          tipoRotazioneColturaleVO.setIdRotazioneColturale(new Long(rs.getLong("ID_ROTAZIONE_COLTURALE")));
          tipoRotazioneColturaleVO.setDescrizione(rs.getString("DESC_ROTAZIONE_COLTURALE"));
          tipoRotazioneColturaleVO.setCodice(rs.getString("COD_ROTAZIONE_COLTURALE"));
          storicoParticellaVO.setTipoRotazioneColturaleVO(tipoRotazioneColturaleVO);
        }
        
        storicoParticellaVO.setPercentualePendenzaMedia(rs.getBigDecimal("PERCENTUALE_PENDENZA_MEDIA"));
        storicoParticellaVO.setGradiPendenzaMedia(rs.getBigDecimal("GRADI_PENDENZA_MEDIA"));
        storicoParticellaVO.setGradiEsposizioneMedia(rs.getBigDecimal("GRADI_ESPOSIZIONE_MEDIA"));
        storicoParticellaVO.setMetriAltitudineMedia(rs.getBigDecimal("METRI_ALTITUDINE_MEDIA"));
        
        
        if(Validator.isNotEmpty(rs.getString("ID_METODO_IRRIGUO"))) {
          storicoParticellaVO.setIdMetodoIrriguo(new Long(rs.getLong("ID_METODO_IRRIGUO")));
          TipoMetodoIrriguoVO tipoMetodoIrriguo = new TipoMetodoIrriguoVO();
          tipoMetodoIrriguo.setCodiceMetodoIrriguo(rs.getString("CODICE_METODO_IRRIGUO"));
          tipoMetodoIrriguo.setDescrizioneMetodoIrriguo(rs.getString("DESCRIZIONE_METODO_IRRIGUO"));
          
          storicoParticellaVO.setTipoMetodoIrriguo(tipoMetodoIrriguo);
        }
        
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "findStoricoParticellaByPrimaryKey in StoricoParticellaDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "findStoricoParticellaByPrimaryKey in StoricoParticellaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "findStoricoParticellaByPrimaryKey in StoricoParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "findStoricoParticellaByPrimaryKey in StoricoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated findStoricoParticellaByPrimaryKey method in StoricoParticellaDAO\n");
		return storicoParticellaVO;
	}
	
	
	/**
   * Metodo che mi restituisce il record DB_STORICO_PARTICELLA del piano attuale a partire
   * dalll'idParticella
   * 
   * @param idConduzioneParticella
   * @return it.csi.solmr.dto.StoricoParticellaVO
   * @throws DataAccessException
   */
  public StoricoParticellaVO findCurrStoricoParticellaByIdParticella(Long idParticella) throws DataAccessException {
    SolmrLogger.debug(this, "Invocating findStoricoParticellaByPrimaryKey method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    StoricoParticellaVO storicoParticellaVO = null;

    try {
      SolmrLogger.debug(this, "Creating db-connection in findCurrStoricoParticellaByIdParticella method in StoricoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in findCurrStoricoParticellaByIdParticella method in StoricoParticellaDAO and it values: "+conn+"\n");

      String query = " SELECT SP.ID_STORICO_PARTICELLA, " +
               "    SP.ID_PARTICELLA, " +
               "    SP.DATA_INIZIO_VALIDITA, " +
               "    SP.DATA_FINE_VALIDITA, " +
               "    SP.COMUNE, " +
               "    C.DESCOM, " +
               "    P.SIGLA_PROVINCIA, " +
               "    SP.SEZIONE, " +
               "    SP.FOGLIO, " +
               "    SP.PARTICELLA, " +
               "    SP.SUBALTERNO, " +
               "    SP.SUP_CATASTALE, " +
               "    SP.SUPERFICIE_GRAFICA, " +
               "    SP.ID_ZONA_ALTIMETRICA, " +
               "    TZA.DESCRIZIONE AS DESC_ALTIMETRICA, " +
               "    SP.ID_AREA_A, " +
               "    TAA.DESCRIZIONE AS DESC_AREA_A, " +
               "    SP.ID_AREA_B, " +
               "    TAB.DESCRIZIONE AS DESC_AREA_B, " +
               "    SP.ID_AREA_C, " +
               "    TAC.DESCRIZIONE AS DESC_AREA_C, " +
               "    SP.ID_AREA_D, " +
               "    TAD.DESCRIZIONE AS DESC_AREA_D, " +
               "    SP.FLAG_CAPTAZIONE_POZZI, " +
               "    SP.FLAG_IRRIGABILE, " +
               "    SP.ID_CASO_PARTICOLARE, " +
               "    TCP.DESCRIZIONE AS DESC_CASO_PART, " +
               "    SP.MOTIVO_MODIFICA, " + 
               "    SP.DATA_AGGIORNAMENTO, " +
               "    SP.ID_UTENTE_AGGIORNAMENTO, " +
               "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
               "          || ' ' " + 
               "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
               "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
               "         WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
               "    AS DENOMINAZIONE, " +
               "       (SELECT PVU.DENOMINAZIONE " +
               "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
               "        WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
               "    AS DESCRIZIONE_ENTE_APPARTENENZA, " +
               "    SP.ID_CAUSALE_MOD_PARTICELLA, " +
               "    TCMP.DESCRIZIONE AS DESC_CAUSALE_MOD, " +
               "    SP.SUP_NON_ELEGGIBILE, " +
               "    SP.SUP_NE_BOSCO_ACQUE_FABBRICATO, " +
               "    SP.SUP_NE_FORAGGIERE, " +
               "    SP.SUP_EL_FRUTTA_GUSCIO, " +
               "    SP.SUP_EL_PRATO_PASCOLO, " +
               "    SP.SUP_EL_COLTURE_MISTE, " +
               "    SP.SUP_COLTIVAZ_ARBOREA_CONS, " +
               "    SP.SUP_COLTIVAZ_ARBOREA_SPEC, " +
               "    SP.DATA_FOTO, " +
               "    SP.TIPO_FOTO, " +
               "    SP.ID_FONTE, " +
               "    TF.DESCRIZIONE AS DESC_FONTE, " +
               "    SP.ID_DOCUMENTO, " +
               "    PART.DATA_CESSAZIONE, " +
               "    SP.ID_DOCUMENTO_PROTOCOLLATO, " +
               "    SP.ID_IRRIGAZIONE, " +
               "    TI.DESCRIZIONE AS DESC_IRRIGAZIONE, " +
               "    TI.DATA_INIZIO_VALIDITA AS DATA_INI_IRRIGAZ, " +
               "    TI.DATA_FINE_VALIDITA AS DATA_FINE_IRRIGAZ " +
               " FROM   DB_STORICO_PARTICELLA SP, " +
               "    COMUNE C, " +
               "    PROVINCIA P, " +
               "    DB_TIPO_ZONA_ALTIMETRICA TZA, " +
               "    DB_TIPO_AREA_A TAA, " +
               "    DB_TIPO_AREA_B TAB, " +
               "    DB_TIPO_AREA_C TAC, " +
               "    DB_TIPO_AREA_D TAD, " +
               "    DB_TIPO_CASO_PARTICOLARE TCP, " +
               //"    PAPUA_V_UTENTE_LOGIN PVU, " +
               "    DB_TIPO_CAUSALE_MOD_PARTICELLA TCMP, " +
               "    DB_TIPO_FONTE TF, " +
               "    DB_TIPO_DOCUMENTO TD, " +
               "    DB_PARTICELLA PART, " +
               "    DB_TIPO_IRRIGAZIONE TI " +
               " WHERE  SP.ID_PARTICELLA = ? " +
               " AND    SP.DATA_FINE_VALIDITA IS NULL " +
               " AND    PART.ID_PARTICELLA = SP.ID_PARTICELLA " +
               " AND    SP.COMUNE = C.ISTAT_COMUNE " +
               " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) " +
               " AND    SP.ID_ZONA_ALTIMETRICA = TZA.ID_ZONA_ALTIMETRICA(+) " + 
               " AND    SP.ID_AREA_A = TAA.ID_AREA_A(+) " +
               " AND    SP.ID_AREA_B = TAB.ID_AREA_B(+) " + 
               " AND    SP.ID_AREA_C = TAC.ID_AREA_C(+) " +
               " AND    SP.ID_AREA_D = TAD.ID_AREA_D(+) " +
               " AND    SP.ID_CASO_PARTICOLARE = TCP.ID_CASO_PARTICOLARE(+) " +
               //" AND    SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
               " AND    SP.ID_CAUSALE_MOD_PARTICELLA = TCMP.ID_CAUSALE_MOD_PARTICELLA(+) " +
               " AND    SP.ID_FONTE = TF.ID_FONTE(+) " +
               " AND    SP.ID_DOCUMENTO = TD.ID_DOCUMENTO(+) " +
               " AND    SP.ID_PARTICELLA = PART.ID_PARTICELLA " +
               " AND    SP.ID_IRRIGAZIONE = TI.ID_IRRIGAZIONE(+) ";

      SolmrLogger.debug(this, "Value of parameter 1 [ID_STORICO_PARTICELLA] in findCurrStoricoParticellaByIdParticella method in StoricoParticellaDAO: "+idParticella+"\n");

      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idParticella.longValue());

      SolmrLogger.debug(this, "Executing findCurrStoricoParticellaByIdParticella: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      if(rs.next()) {
        storicoParticellaVO = new StoricoParticellaVO(); 
        storicoParticellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
        storicoParticellaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
        storicoParticellaVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        storicoParticellaVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
        ComuneVO comuneVO = new ComuneVO();
        comuneVO.setIstatComune(rs.getString("COMUNE"));
        comuneVO.setDescom(rs.getString("DESCOM"));
        if(Validator.isNotEmpty(rs.getString("SIGLA_PROVINCIA"))) {
          ProvinciaVO provinciaVO = new ProvinciaVO();
          provinciaVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));
          comuneVO.setProvinciaVO(provinciaVO);
        }
        storicoParticellaVO.setComuneParticellaVO(comuneVO);
        storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
        storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
        storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
        storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
        storicoParticellaVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
        storicoParticellaVO.setSuperficieGrafica(rs.getString("SUPERFICIE_GRAFICA"));
        if(Validator.isNotEmpty(rs.getString("ID_ZONA_ALTIMETRICA"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_ZONA_ALTIMETRICA")), rs.getString("DESC_ALTIMETRICA"));
          storicoParticellaVO.setIdZonaAltimetrica(Long.decode(rs.getString("ID_ZONA_ALTIMETRICA")));
          storicoParticellaVO.setZonaAltimetrica(code);
        }
        if(Validator.isNotEmpty(rs.getString("ID_AREA_A"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_A")), rs.getString("DESC_AREA_A"));
          storicoParticellaVO.setIdAreaA(new Long(rs.getString("ID_AREA_A")));
          storicoParticellaVO.setAreaA(code);
        }
        if(Validator.isNotEmpty(rs.getString("ID_AREA_B"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_B")), rs.getString("DESC_AREA_B"));
          storicoParticellaVO.setIdAreaB(new Long(rs.getString("ID_AREA_B")));
          storicoParticellaVO.setAreaB(code);
        }
        if(Validator.isNotEmpty(rs.getString("ID_AREA_C"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_C")), rs.getString("DESC_AREA_C"));
          storicoParticellaVO.setIdAreaC(new Long(rs.getString("ID_AREA_C")));
          storicoParticellaVO.setAreaC(code);
        }
        if(Validator.isNotEmpty(rs.getString("ID_AREA_D"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_D")), rs.getString("DESC_AREA_D"));
          storicoParticellaVO.setIdAreaD(new Long(rs.getString("ID_AREA_D")));
          storicoParticellaVO.setAreaD(code);
        }
        storicoParticellaVO.setFlagCaptazionePozzi(rs.getString("FLAG_CAPTAZIONE_POZZI"));
        storicoParticellaVO.setFlagIrrigabile(rs.getString("FLAG_IRRIGABILE"));
        if(Validator.isNotEmpty(rs.getString("ID_CASO_PARTICOLARE"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_CASO_PARTICOLARE")), rs.getString("DESC_CASO_PART"));
          storicoParticellaVO.setIdCasoParticolare(new Long(rs.getString("ID_CASO_PARTICOLARE")));
          storicoParticellaVO.setCasoParticolare(code);
        }
        storicoParticellaVO.setMotivoModifica(rs.getString("MOTIVO_MODIFICA"));
        storicoParticellaVO.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO"));
        storicoParticellaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        UtenteIrideVO utenteIrideVO = new UtenteIrideVO();
        utenteIrideVO.setIdUtente(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        utenteIrideVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        utenteIrideVO.setDescrizioneEnteAppartenenza(rs.getString("DESCRIZIONE_ENTE_APPARTENENZA"));
        storicoParticellaVO.setUtenteAggiornamento(utenteIrideVO);
        if(Validator.isNotEmpty(rs.getString("ID_CAUSALE_MOD_PARTICELLA"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_CAUSALE_MOD_PARTICELLA")), rs.getString("DESC_CAUSALE_MOD"));
          storicoParticellaVO.setIdCausaleModParticella(new Long(rs.getString("ID_CAUSALE_MOD_PARTICELLA")));
          storicoParticellaVO.setCausaleModParticella(code);
        }
        storicoParticellaVO.setSupNonEleggibile(rs.getString("SUP_NON_ELEGGIBILE"));
        storicoParticellaVO.setSupNeBoscoAcqueFabbricato(rs.getString("SUP_NE_BOSCO_ACQUE_FABBRICATO"));
        storicoParticellaVO.setSupNeForaggere(rs.getString("SUP_NE_FORAGGIERE"));
        storicoParticellaVO.setSupElFruttaGuscio(rs.getString("SUP_EL_FRUTTA_GUSCIO"));
        storicoParticellaVO.setSupElPratoPascolo(rs.getString("SUP_EL_PRATO_PASCOLO"));
        storicoParticellaVO.setSupElColtureMiste(rs.getString("SUP_EL_COLTURE_MISTE"));
        storicoParticellaVO.setSupColtivazArboreaCons(rs.getString("SUP_COLTIVAZ_ARBOREA_CONS"));
        storicoParticellaVO.setSupColtivazArboreaSpec(rs.getString("SUP_COLTIVAZ_ARBOREA_SPEC"));
        storicoParticellaVO.setDataFoto(rs.getDate("DATA_FOTO"));
        storicoParticellaVO.setTipoFoto(rs.getString("TIPO_FOTO"));
        if(Validator.isNotEmpty(rs.getString("ID_FONTE"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_FONTE")), rs.getString("DESC_FONTE"));
          storicoParticellaVO.setIdFonte(new Long(rs.getString("ID_FONTE")));
          storicoParticellaVO.setFonte(code);
        }
        if(Validator.isNotEmpty(rs.getString("ID_DOCUMENTO"))) {
          storicoParticellaVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
        }
        storicoParticellaVO.setDataCessazione(rs.getDate("DATA_CESSAZIONE"));
        if(Validator.isNotEmpty(rs.getString("ID_DOCUMENTO_PROTOCOLLATO"))) {
          storicoParticellaVO.setIdDocumentoProtocollato(new Long(rs.getString("ID_DOCUMENTO_PROTOCOLLATO")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_IRRIGAZIONE"))) {
          storicoParticellaVO.setIdIrrigazione(new Long(rs.getLong("ID_IRRIGAZIONE")));
          TipoIrrigazioneVO tipoIrrigazioneVO = new TipoIrrigazioneVO();
          tipoIrrigazioneVO.setIdIrrigazione(new Long(rs.getLong("ID_IRRIGAZIONE")));
          tipoIrrigazioneVO.setDescrizione(rs.getString("DESC_IRRIGAZIONE"));
          tipoIrrigazioneVO.setDataInizioValidita(rs.getDate("DATA_INI_IRRIGAZ"));
          tipoIrrigazioneVO.setDataFineValidita(rs.getDate("DATA_FINE_IRRIGAZ"));
          storicoParticellaVO.setTipoIrrigazioneVO(tipoIrrigazioneVO);
        }
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "findCurrStoricoParticellaByIdParticella in StoricoParticellaDAO - SQLException: "+exc.getMessage()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "findCurrStoricoParticellaByIdParticella in StoricoParticellaDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "findCurrStoricoParticellaByIdParticella in StoricoParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "findCurrStoricoParticellaByIdParticella in StoricoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated findCurrStoricoParticellaByIdParticella method in StoricoParticellaDAO\n");
    return storicoParticellaVO;
  }
  
  public StoricoParticellaVO findStoricoParticellaByIdParticellaForImpAss(Long idParticella) throws DataAccessException {
    SolmrLogger.debug(this, "Invocating findStoricoParticellaByPrimaryKey method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    StoricoParticellaVO storicoParticellaVO = null;

    try {
      SolmrLogger.debug(this, "Creating db-connection in findStoricoParticellaByIdParticellaForImpAss method in StoricoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in findStoricoParticellaByIdParticellaForImpAss method in StoricoParticellaDAO and it values: "+conn+"\n");

      String query = " SELECT SP.ID_STORICO_PARTICELLA, " +
               "    SP.ID_PARTICELLA, " +
               "    SP.SUP_CATASTALE, " +
               "    SP.SUPERFICIE_GRAFICA " +
               " FROM   DB_STORICO_PARTICELLA SP " +
               " WHERE  SP.ID_PARTICELLA = ? " +
               " AND    SP.DATA_FINE_VALIDITA IS NULL ";

      SolmrLogger.debug(this, "Value of parameter 1 [ID_STORICO_PARTICELLA] in findStoricoParticellaByIdParticellaForImpAss method in StoricoParticellaDAO: "+idParticella+"\n");

      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idParticella.longValue());

      SolmrLogger.debug(this, "Executing findStoricoParticellaByIdParticellaForImpAss: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      if(rs.next()) {
        storicoParticellaVO = new StoricoParticellaVO(); 
        storicoParticellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
        storicoParticellaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
        storicoParticellaVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
        storicoParticellaVO.setSuperficieGrafica(rs.getString("SUPERFICIE_GRAFICA"));
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "findStoricoParticellaByIdParticellaForImpAss in StoricoParticellaDAO - SQLException: "+exc.getMessage()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "findStoricoParticellaByIdParticellaForImpAss in StoricoParticellaDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "findStoricoParticellaByIdParticellaForImpAss in StoricoParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "findStoricoParticellaByIdParticellaForImpAss in StoricoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated findStoricoParticellaByIdParticellaForImpAss method in StoricoParticellaDAO\n");
    return storicoParticellaVO;
  }
	
	/**
	 * Metodo utilizzato per effettuare la modifica del record relativo alla tabella
	 * DB_STORICO_PARTICELLA
	 * 
	 * @param StoricoParticellaVO
	 * @throws DataAccessException
	 */
	public void updateStoricoParticella(StoricoParticellaVO storicoParticellaVO) throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating updateStoricoParticella method in StoricoParticellaDAO\n");
	  Connection conn = null;
	  PreparedStatement stmt = null;

	  try 
	  {
	  	SolmrLogger.debug(this, "Creating db-connection in updateStoricoParticella method in StoricoParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in updateStoricoParticella method in StoricoParticellaDAO and it values: "+conn+"\n");

			String query = " UPDATE DB_STORICO_PARTICELLA "+
	                       " SET    ID_PARTICELLA = ?, " +
	                       "        DATA_INIZIO_VALIDITA = ?, " +
	                       "        DATA_FINE_VALIDITA = ?, " +
	                       "        COMUNE = ?, " +
	                       "        SEZIONE = ?, " +
	                       "        FOGLIO = ?, " +
	                       "        PARTICELLA = ?, " +
	                       "        SUBALTERNO = ?, " +
	                       "        SUP_CATASTALE = ?, " +
	                       "        SUPERFICIE_GRAFICA = ?, " +
	                       "        ID_ZONA_ALTIMETRICA = ?, " +
	                       "        ID_AREA_A = ?, " +
	                       "        ID_AREA_B = ?, " +
	                       "        ID_AREA_C = ?, " +
	                       "        ID_AREA_D = ?, " +
	                       "        ID_AREA_G = ?, " +
	                       "        ID_AREA_H = ?, " +
	                       "        ID_AREA_I = ?, " +
	                       "        ID_AREA_L = ?, " +
	                       "        ID_AREA_M = ?, " +
	                       "        FLAG_CAPTAZIONE_POZZI = ?, " +
	                       "        FLAG_IRRIGABILE = ?, " +
	                       "        ID_CASO_PARTICOLARE = ?, " +
	                       "        MOTIVO_MODIFICA = ?, " +
	                       "        DATA_AGGIORNAMENTO = ?, " +
	                       "        ID_UTENTE_AGGIORNAMENTO = ?, " +
	                       "        ID_CAUSALE_MOD_PARTICELLA = ?, " +
	                       "        SUP_NON_ELEGGIBILE = ?, " +
	                       "        SUP_NE_BOSCO_ACQUE_FABBRICATO = ?, " +
	                       "        SUP_NE_FORAGGIERE = ?, " +
	                       "        SUP_EL_FRUTTA_GUSCIO = ?, " +
	                       "        SUP_EL_PRATO_PASCOLO = ?, " +
	                       "        SUP_EL_COLTURE_MISTE = ?, " +
	                       "        SUP_COLTIVAZ_ARBOREA_CONS = ?, " +
	                       "        SUP_COLTIVAZ_ARBOREA_SPEC = ?, " +
	                       "        DATA_FOTO = ?, " +
	                       "        TIPO_FOTO = ?, " +
	                       "        ID_FONTE = ?, " +
	                       "        ID_DOCUMENTO = ?, " +
	                       "        ID_DOCUMENTO_PROTOCOLLATO = ?, " +
	                       "        ID_IRRIGAZIONE = ?, " +
	                       "        ID_FASCIA_FLUVIALE = ?, " +
	                       "        ID_POTENZIALITA_IRRIGUA = ?, " +
	                       "        ID_TERRAZZAMENTO = ?, " +
	                       "        ID_ROTAZIONE_COLTURALE = ?, " +
	                       "        PERCENTUALE_PENDENZA_MEDIA = ?, " +
	                       "        GRADI_PENDENZA_MEDIA = ?, " +
	                       "        GRADI_ESPOSIZIONE_MEDIA = ?, " +
	                       "        METRI_ALTITUDINE_MEDIA = ?, " +
	                       "        ID_METODO_IRRIGUO = ? " +
	                       " WHERE  ID_STORICO_PARTICELLA = ? ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing updateStoricoParticella: "+query);
			
			int index = 0;

			stmt.setLong(++index, storicoParticellaVO.getIdParticella().longValue());
			SolmrLogger.debug(this, "Value of parameter [ID_PARTICELLA] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdParticella().longValue()+"\n");
			stmt.setDate(++index, new java.sql.Date(storicoParticellaVO.getDataInizioValidita().getTime()));
			SolmrLogger.debug(this, "Value of parameter [DATA_INIZIO_VALIDITA] in method updateStoricoParticella in StoricoParticellaDAO: "+new java.sql.Date(storicoParticellaVO.getDataInizioValidita().getTime())+"\n");
			if(storicoParticellaVO.getDataFineValidita() != null) {
				stmt.setTimestamp(++index, new Timestamp(storicoParticellaVO.getDataFineValidita().getTime()));
				SolmrLogger.debug(this, "Value of parameter [DATA_FINE_VALIDITA] in method updateStoricoParticella in StoricoParticellaDAO: "+new java.sql.Date(storicoParticellaVO.getDataFineValidita().getTime())+"\n");
			}
			else {
				stmt.setString(++index, null);
				SolmrLogger.debug(this, "Value of parameter [DATA_FINE_VALIDITA] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			stmt.setString(++index, storicoParticellaVO.getIstatComune());
			SolmrLogger.debug(this, "Value of parameter [COMUNE] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIstatComune()+"\n");
			if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) {
				stmt.setString(++index, storicoParticellaVO.getSezione().toUpperCase());
				SolmrLogger.debug(this, "Value of parameter [SEZIONE] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getSezione().toUpperCase()+"\n");
			}
			else {
				stmt.setString(++index, null);
				SolmrLogger.debug(this, "Value of parameter [SEZIONE] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			stmt.setLong(++index, Long.parseLong(storicoParticellaVO.getFoglio()));
			SolmrLogger.debug(this, "Value of parameter [FOGLIO] in method updateStoricoParticella in StoricoParticellaDAO: "+Long.parseLong(storicoParticellaVO.getFoglio())+"\n");
			if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) {
				stmt.setLong(++index, Long.parseLong(storicoParticellaVO.getParticella()));
				SolmrLogger.debug(this, "Value of parameter [PARTICELLA] in method updateStoricoParticella in StoricoParticellaDAO: "+Long.parseLong(storicoParticellaVO.getParticella())+"\n");
			}
			else {
				stmt.setString(++index, null);
				SolmrLogger.debug(this, "Value of parameter [PARTICELLA] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno())) {
				stmt.setString(++index, storicoParticellaVO.getSubalterno().toUpperCase());
				SolmrLogger.debug(this, "Value of parameter [SUBALTERNO] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getSubalterno().toUpperCase()+"\n");
			}
			else {
				stmt.setString(++index, null);
				SolmrLogger.debug(this, "Value of parameter [SUBALTERNO] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			stmt.setString(++index, StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
			SolmrLogger.debug(this, "Value of parameter [SUP_CATASTALE] in method updateStoricoParticella in StoricoParticellaDAO: "+StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale())+"\n");
			stmt.setString(++index, StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica()));
      SolmrLogger.debug(this, "Value of parameter [SUPERFICIE_GRAFICA] in method updateStoricoParticella in StoricoParticellaDAO: "+StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica())+"\n");
			if(storicoParticellaVO.getIdZonaAltimetrica() != null) {
				stmt.setLong(++index, storicoParticellaVO.getIdZonaAltimetrica().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_ZONA_ALTIMETRICA] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdZonaAltimetrica().longValue()+"\n");
			}
			else {
				stmt.setString(++index, null);
				SolmrLogger.debug(this, "Value of parameter [ID_ZONA_ALTIMETRICA] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(storicoParticellaVO.getIdAreaA() != null) {
				stmt.setLong(++index, storicoParticellaVO.getIdAreaA().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_AREA_A] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdAreaA().longValue()+"\n");
			}
			else {
				stmt.setString(++index, null);
				SolmrLogger.debug(this, "Value of parameter [ID_AREA_A] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(storicoParticellaVO.getIdAreaB() != null) {
				stmt.setLong(++index, storicoParticellaVO.getIdAreaB().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_AREA_B] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdAreaB().longValue()+"\n");
			}
			else {
				stmt.setString(++index, null);
				SolmrLogger.debug(this, "Value of parameter [ID_AREA_B] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(storicoParticellaVO.getIdAreaC() != null) {
				stmt.setLong(++index, storicoParticellaVO.getIdAreaC().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_AREA_C] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdAreaC().longValue()+"\n");
			}
			else {
				stmt.setString(++index, null);
				SolmrLogger.debug(this, "Value of parameter [ID_AREA_C] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(storicoParticellaVO.getIdAreaD() != null) {
				stmt.setLong(++index, storicoParticellaVO.getIdAreaD().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_AREA_D] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdAreaD().longValue()+"\n");
			}
			else {
				stmt.setString(++index, null);
				SolmrLogger.debug(this, "Value of parameter [ID_AREA_D] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(storicoParticellaVO.getIdAreaG() != null) {
        stmt.setLong(++index, storicoParticellaVO.getIdAreaG().longValue());
        SolmrLogger.debug(this, "Value of parameter [ID_AREA_G] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdAreaG().longValue()+"\n");
      }
      else {
        stmt.setString(++index, null);
        SolmrLogger.debug(this, "Value of parameter [ID_AREA_G] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
      }
			if(storicoParticellaVO.getIdAreaH() != null) {
        stmt.setLong(++index, storicoParticellaVO.getIdAreaH().longValue());
        SolmrLogger.debug(this, "Value of parameter [ID_AREA_H] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdAreaH().longValue()+"\n");
      }
      else {
        stmt.setString(++index, null);
        SolmrLogger.debug(this, "Value of parameter [ID_AREA_H] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
      }
			if(storicoParticellaVO.getIdAreaI() != null) {
        stmt.setLong(++index, storicoParticellaVO.getIdAreaI().longValue());
        SolmrLogger.debug(this, "Value of parameter [ID_AREA_I] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdAreaI().longValue()+"\n");
      }
      else {
        stmt.setString(++index, null);
        SolmrLogger.debug(this, "Value of parameter [ID_AREA_I] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
      }
			if(storicoParticellaVO.getIdAreaL() != null) {
        stmt.setLong(++index, storicoParticellaVO.getIdAreaL().longValue());
        SolmrLogger.debug(this, "Value of parameter [ID_AREA_L] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdAreaL().longValue()+"\n");
      }
      else {
        stmt.setString(++index, null);
        SolmrLogger.debug(this, "Value of parameter [ID_AREA_L] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
      }
			if(storicoParticellaVO.getIdAreaM() != null) {
        stmt.setLong(++index, storicoParticellaVO.getIdAreaM().longValue());
        SolmrLogger.debug(this, "Value of parameter [ID_AREA_M] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdAreaM().longValue()+"\n");
      }
      else {
        stmt.setString(++index, null);
        SolmrLogger.debug(this, "Value of parameter [ID_AREA_M] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
      }
			stmt.setString(++index, storicoParticellaVO.getFlagCaptazionePozzi());
			SolmrLogger.debug(this, "Value of parameter [FLAG_CAPTAZIONE_POZZI] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getFlagCaptazionePozzi()+"\n");
			stmt.setString(++index, storicoParticellaVO.getFlagIrrigabile());
			SolmrLogger.debug(this, "Value of parameter [FLAG_IRRIGABILE] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getFlagIrrigabile()+"\n");
			if(storicoParticellaVO.getIdCasoParticolare() != null) {
				stmt.setLong(++index, storicoParticellaVO.getIdCasoParticolare().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_CASO_PARTICOLARE] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdCasoParticolare().longValue()+"\n");
			}
			else {
				stmt.setString(++index, null);
				SolmrLogger.debug(this, "Value of parameter [ID_CASO_PARTICOLARE] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(storicoParticellaVO.getMotivoModifica() != null) {
				stmt.setString(++index, storicoParticellaVO.getMotivoModifica());
				SolmrLogger.debug(this, "Value of parameter [MOTIVO_MODIFICA] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getMotivoModifica()+"\n");
			}
			else {
				stmt.setString(++index, null);
				SolmrLogger.debug(this, "Value of parameter [MOTIVO_MODIFICA] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			stmt.setTimestamp(++index, new Timestamp(storicoParticellaVO.getDataAggiornamento().getTime()));
			SolmrLogger.debug(this, "Value of parameter [DATA_AGGIORNAMENTO] in method updateStoricoParticella in StoricoParticellaDAO: "+new Timestamp(storicoParticellaVO.getDataAggiornamento().getTime())+"\n");
			stmt.setLong(++index, storicoParticellaVO.getIdUtenteAggiornamento().longValue());
			SolmrLogger.debug(this, "Value of parameter [ID_UTENTE_AGGIORNAMENTO] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdUtenteAggiornamento().longValue()+"\n");
			if(storicoParticellaVO.getIdCausaleModParticella() != null) {
				stmt.setLong(++index, storicoParticellaVO.getIdCausaleModParticella().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_CAUSALE_MOD_PARTICELLA] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdCausaleModParticella().longValue()+"\n");
			}
			else {
				stmt.setString(++index, null);
				SolmrLogger.debug(this, "Value of parameter [ID_CAUSALE_MOD_PARTICELLA] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(Validator.isNotEmpty(storicoParticellaVO.getSupNonEleggibile())) {
				stmt.setString(++index, StringUtils.parseSuperficieField(storicoParticellaVO.getSupNonEleggibile()));
				SolmrLogger.debug(this, "Value of parameter [SUP_NON_ELEGGIBILE] in method updateStoricoParticella in StoricoParticellaDAO: "+StringUtils.parseSuperficieField(storicoParticellaVO.getSupNonEleggibile())+"\n");
			}
			else {
				stmt.setString(++index, null);
				SolmrLogger.debug(this, "Value of parameter [SUP_NON_ELEGGIBILE] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(Validator.isNotEmpty(storicoParticellaVO.getSupNeBoscoAcqueFabbricato())) {
				stmt.setString(++index, StringUtils.parseSuperficieField(storicoParticellaVO.getSupNeBoscoAcqueFabbricato()));
				SolmrLogger.debug(this, "Value of parameter [SUP_NE_BOSCO_ACQUE_FABBRICATO] in method updateStoricoParticella in StoricoParticellaDAO: "+StringUtils.parseSuperficieField(storicoParticellaVO.getSupNeBoscoAcqueFabbricato())+"\n");
			}
			else {
				stmt.setString(++index, null);
				SolmrLogger.debug(this, "Value of parameter [SUP_NE_BOSCO_ACQUE_FABBRICATO] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(Validator.isNotEmpty(storicoParticellaVO.getSupNeForaggere())) {
				stmt.setString(++index, StringUtils.parseSuperficieField(storicoParticellaVO.getSupNeForaggere()));
				SolmrLogger.debug(this, "Value of parameter [SUP_NE_FORAGGERE] in method updateStoricoParticella in StoricoParticellaDAO: "+StringUtils.parseSuperficieField(storicoParticellaVO.getSupNeForaggere())+"\n");
			}
			else {
				stmt.setString(++index, null);
				SolmrLogger.debug(this, "Value of parameter [SUP_NE_FORAGGERE] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(Validator.isNotEmpty(storicoParticellaVO.getSupElFruttaGuscio())) {
				stmt.setString(++index, StringUtils.parseSuperficieField(storicoParticellaVO.getSupElFruttaGuscio()));
				SolmrLogger.debug(this, "Value of parameter [SUP_EL_FRUTTA_GUSCIO] in method updateStoricoParticella in StoricoParticellaDAO: "+StringUtils.parseSuperficieField(storicoParticellaVO.getSupElFruttaGuscio())+"\n");
			}
			else {
				stmt.setString(++index, null);
				SolmrLogger.debug(this, "Value of parameter [SUP_EL_FRUTTA_GUSCIO] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(Validator.isNotEmpty(storicoParticellaVO.getSupElPratoPascolo())) {
				stmt.setString(++index, StringUtils.parseSuperficieField(storicoParticellaVO.getSupElPratoPascolo()));
				SolmrLogger.debug(this, "Value of parameter [SUP_EL_PRATO_PASCOLO] in method updateStoricoParticella in StoricoParticellaDAO: "+StringUtils.parseSuperficieField(storicoParticellaVO.getSupElPratoPascolo())+"\n");
			}
			else {
				stmt.setString(++index, null);
				SolmrLogger.debug(this, "Value of parameter [SUP_EL_PRATO_PASCOLO] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(Validator.isNotEmpty(storicoParticellaVO.getSupElColtureMiste())) {
				stmt.setString(++index, StringUtils.parseSuperficieField(storicoParticellaVO.getSupElColtureMiste()));
				SolmrLogger.debug(this, "Value of parameter [SUP_EL_COLTURE_MISTE] in method updateStoricoParticella in StoricoParticellaDAO: "+StringUtils.parseSuperficieField(storicoParticellaVO.getSupElColtureMiste())+"\n");
			}
			else {
				stmt.setString(++index, null);
				SolmrLogger.debug(this, "Value of parameter [SUP_EL_COLTURE_MISTE] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(Validator.isNotEmpty(storicoParticellaVO.getSupColtivazArboreaCons())) {
				stmt.setString(++index, StringUtils.parseSuperficieField(storicoParticellaVO.getSupColtivazArboreaCons()));
				SolmrLogger.debug(this, "Value of parameter [SUP_COLTIVAZ_ARBOREA_CONS] in method updateStoricoParticella in StoricoParticellaDAO: "+StringUtils.parseSuperficieField(storicoParticellaVO.getSupColtivazArboreaCons())+"\n");
			}
			else {
				stmt.setString(++index, null);
				SolmrLogger.debug(this, "Value of parameter [SUP_COLTIVAZ_ARBOREA_CONS] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(Validator.isNotEmpty(storicoParticellaVO.getSupColtivazArboreaSpec())) {
				stmt.setString(++index, StringUtils.parseSuperficieField(storicoParticellaVO.getSupColtivazArboreaSpec()));
				SolmrLogger.debug(this, "Value of parameter [SUP_COLTIVAZ_ARBOREA_SPEC] in method updateStoricoParticella in StoricoParticellaDAO: "+StringUtils.parseSuperficieField(storicoParticellaVO.getSupColtivazArboreaSpec())+"\n");
			}
			else {
				stmt.setString(++index, null);
				SolmrLogger.debug(this, "Value of parameter [SUP_COLTIVAZ_ARBOREA_SPEC] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(storicoParticellaVO.getDataFoto() != null) {
				stmt.setDate(++index, new java.sql.Date(storicoParticellaVO.getDataFoto().getTime()));
				SolmrLogger.debug(this, "Value of parameter [DATA_FOTO] in method updateStoricoParticella in StoricoParticellaDAO: "+new java.sql.Date(storicoParticellaVO.getDataFoto().getTime())+"\n");
			}
			else {
				stmt.setString(++index, null);
				SolmrLogger.debug(this, "Value of parameter [DATA_FOTO] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(Validator.isNotEmpty(storicoParticellaVO.getTipoFoto())) {
				stmt.setString(++index, storicoParticellaVO.getTipoFoto());
				SolmrLogger.debug(this, "Value of parameter [TIPO_FOTO] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getTipoFoto()+"\n");
			}
			else {
				stmt.setString(++index, null);
				SolmrLogger.debug(this, "Value of parameter [TIPO_FOTO] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(storicoParticellaVO.getIdFonte() != null) {
				stmt.setLong(++index, storicoParticellaVO.getIdFonte().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_FONTE] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdFonte().longValue()+"\n");
			}
			else {
				stmt.setString(++index, null);
				SolmrLogger.debug(this, "Value of parameter [ID_FONTE] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(storicoParticellaVO.getIdDocumento() != null) {
				stmt.setLong(++index, storicoParticellaVO.getIdDocumento().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_DOCUMENTO] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdDocumento().longValue()+"\n");
			}
			else {
				stmt.setString(++index, null);
				SolmrLogger.debug(this, "Value of parameter [ID_DOCUMENTO] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(storicoParticellaVO.getIdDocumentoProtocollato() != null) {
				stmt.setLong(++index, storicoParticellaVO.getIdDocumentoProtocollato().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_DOCUMENTO_PROTOCOLLATO] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdDocumentoProtocollato().longValue()+"\n");
			}
			else {
				stmt.setString(++index, null);
				SolmrLogger.debug(this, "Value of parameter [ID_DOCUMENTO_PROTOCOLLATO] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(storicoParticellaVO.getIdIrrigazione() != null) {
				stmt.setLong(++index, storicoParticellaVO.getIdIrrigazione().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_IRRIGAZIONE] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdIrrigazione().longValue()+"\n");
			}
			else {
				stmt.setString(++index, null);
				SolmrLogger.debug(this, "Value of parameter [ID_IRRIGAZIONE] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(storicoParticellaVO.getIdFasciaFluviale() != null) {
        stmt.setLong(++index, storicoParticellaVO.getIdFasciaFluviale().longValue());
        SolmrLogger.debug(this, "Value of parameter [ID_FASCIA_FLUVIALE] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdFasciaFluviale().longValue()+"\n");
      }
      else {
        stmt.setString(++index, null);
        SolmrLogger.debug(this, "Value of parameter [ID_FASCIA_FLUVIALE] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
      }
			if(storicoParticellaVO.getIdPotenzialitaIrrigua() != null) {
        stmt.setLong(++index, storicoParticellaVO.getIdPotenzialitaIrrigua().longValue());
        SolmrLogger.debug(this, "Value of parameter [ID_POTENZIALITA_IRRIGUA] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdPotenzialitaIrrigua().longValue()+"\n");
      }
      else {
        stmt.setString(++index, null);
        SolmrLogger.debug(this, "Value of parameter [ID_POTENZIALITA_IRRIGUA] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
      }
			if(storicoParticellaVO.getIdTerrazzamento() != null) {
        stmt.setLong(++index, storicoParticellaVO.getIdTerrazzamento().longValue());
        SolmrLogger.debug(this, "Value of parameter [ID_TERRAZZAMENTO] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdTerrazzamento().longValue()+"\n");
      }
      else {
        stmt.setString(++index, null);
        SolmrLogger.debug(this, "Value of parameter [ID_TERRAZZAMENTO] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
      }
			if(storicoParticellaVO.getIdRotazioneColturale() != null) {
        stmt.setLong(++index, storicoParticellaVO.getIdRotazioneColturale().longValue());
        SolmrLogger.debug(this, "Value of parameter [ID_ROTAZIONE_COLTURALE] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdRotazioneColturale().longValue()+"\n");
      }
      else {
        stmt.setString(++index, null);
        SolmrLogger.debug(this, "Value of parameter [ID_ROTAZIONE_COLTURALE] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
      }
			
			stmt.setBigDecimal(++index, storicoParticellaVO.getPercentualePendenzaMedia());
			SolmrLogger.debug(this, "Value of parameter [PERCENTUALE_PENDENZA_MEDIA] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getPercentualePendenzaMedia()+"\n");
			stmt.setBigDecimal(++index, storicoParticellaVO.getGradiPendenzaMedia());
      SolmrLogger.debug(this, "Value of parameter [GRADI_PENDENZA_MEDIA] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getGradiPendenzaMedia()+"\n");
      stmt.setBigDecimal(++index, storicoParticellaVO.getGradiEsposizioneMedia());
      SolmrLogger.debug(this, "Value of parameter [GRADI_ESPOSIZIONE_MEDIA] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getGradiEsposizioneMedia()+"\n");
      stmt.setBigDecimal(++index, storicoParticellaVO.getMetriAltitudineMedia());
      SolmrLogger.debug(this, "Value of parameter [METRI_ALTITUDINE_MEDIA] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getMetriAltitudineMedia()+"\n");
      stmt.setBigDecimal(++index, convertLongToBigDecimal(storicoParticellaVO.getIdMetodoIrriguo()));
      SolmrLogger.debug(this, "Value of parameter [ID_METODO_IRRIGUO] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdMetodoIrriguo()+"\n");
			
			
			
			stmt.setLong(++index, storicoParticellaVO.getIdStoricoParticella().longValue());
			SolmrLogger.debug(this, "Value of parameter [ID_STORICO_PARTICELLA] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdStoricoParticella().longValue()+"\n");
			
			stmt.executeUpdate();

			stmt.close();
	    }
	    catch(SQLException exc) {
	    	SolmrLogger.error(this, "updateStoricoParticella in StoricoParticellaDAO - SQLException: "+exc.getMessage());
	    	throw new DataAccessException(exc.getMessage());
	    }
	    catch (Exception ex) {
	    	SolmrLogger.error(this, "updateStoricoParticella in StoricoParticellaDAO - Generic Exception: "+ex);
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
	    		SolmrLogger.error(this, "updateStoricoParticella in StoricoParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage());
	    		throw new DataAccessException(exc.getMessage());
	    	}
	    	catch(Exception ex) {
	    		SolmrLogger.error(this, "updateStoricoParticella in StoricoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage());
	    		throw new DataAccessException(ex.getMessage());
	    	}
	    }
	    SolmrLogger.debug(this, "Invocated updateStoricoParticella method in StoricoParticellaDAO\n");
	}
	
	
	public void storicizzaStoricoParticella(long idStoricoParticella) throws DataAccessException 
	{
    SolmrLogger.debug(this, "Invocating storicizzaStoricoParticella method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in storicizzaStoricoParticella method in StoricoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in storicizzaStoricoParticella method in StoricoParticellaDAO and it values: "+conn+"\n");

      String query = " UPDATE DB_STORICO_PARTICELLA "+
                         " SET    DATA_FINE_VALIDITA = SYSDATE " +
                         " WHERE  ID_STORICO_PARTICELLA = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing storicizzaStoricoParticella: "+query);
      
      int index = 0;     
      
      stmt.setLong(++index, idStoricoParticella);
      SolmrLogger.debug(this, "Value of parameter [ID_STORICO_PARTICELLA] in method storicizzaStoricoParticella in StoricoParticellaDAO: "+idStoricoParticella+"\n");
      
      stmt.executeUpdate();

      stmt.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "storicizzaStoricoParticella in StoricoParticellaDAO - SQLException: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.error(this, "storicizzaStoricoParticella in StoricoParticellaDAO - Generic Exception: "+ex);
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
          SolmrLogger.error(this, "storicizzaStoricoParticella in StoricoParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage());
          throw new DataAccessException(exc.getMessage());
        }
        catch(Exception ex) {
          SolmrLogger.error(this, "storicizzaStoricoParticella in StoricoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage());
          throw new DataAccessException(ex.getMessage());
        }
      }
      SolmrLogger.debug(this, "Invocated storicizzaStoricoParticella method in StoricoParticellaDAO\n");
  }
	
	/**
	 * Metodo utilizzato per effettuare l'inserimento del record relativo alla tabella
	 * DB_STORICO_PARTICELLA
	 * 
	 * @param storicoParticellaVO
	 * return java.lang.Long idStoricoParticella
	 * @throws DataAccessException
	 */
	public Long insertStoricoParticella(StoricoParticellaVO storicoParticellaVO) throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating insertStoricoParticella method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Long idStoricoParticella = null;

	  try 
	  {
	  	idStoricoParticella = getNextPrimaryKey((String)SolmrConstants.get("SEQ_STORICO_PARTICELLA"));
	   	SolmrLogger.debug(this, "Creating db-connection in insertStoricoParticella method in StoricoParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in insertStoricoParticella method in StoricoParticellaDAO and it values: "+conn+"\n");

			String query = " INSERT INTO DB_STORICO_PARTICELLA "+
	                       "     		 (ID_STORICO_PARTICELLA, " +
	                       "              ID_PARTICELLA, " +
	                       "              DATA_INIZIO_VALIDITA, " +
	                       "              DATA_FINE_VALIDITA, " +
	                       "              COMUNE, " +
	                       "              SEZIONE, " +
	                       "              FOGLIO, " +
	                       "              PARTICELLA, " +
	                       "              SUBALTERNO, " +
	                       "              SUP_CATASTALE, " +
	                       "              SUPERFICIE_GRAFICA, " +
	                       "              ID_ZONA_ALTIMETRICA, " +
	                       "              ID_AREA_A, " +
	                       "              ID_AREA_B, " +
	                       "              ID_AREA_C, " +
	                       "              ID_AREA_D, " +
	                       "              ID_AREA_G, " +
	                       "              ID_AREA_H, " +
	                       "              ID_AREA_I, " +
	                       "              ID_AREA_L, " +
	                       "              ID_AREA_M, " +
	                       "              FLAG_CAPTAZIONE_POZZI, " +
	                       "              FLAG_IRRIGABILE, " +
	                       "              ID_CASO_PARTICOLARE, " +
	                       "              MOTIVO_MODIFICA, " +
	                       "              DATA_AGGIORNAMENTO, " +
	                       "              ID_UTENTE_AGGIORNAMENTO, " +
	                       "              ID_CAUSALE_MOD_PARTICELLA, " +
	                       "              SUP_NON_ELEGGIBILE, " +
	                       "              SUP_NE_BOSCO_ACQUE_FABBRICATO, " +
	                       "              SUP_NE_FORAGGIERE, " +
	                       "              SUP_EL_FRUTTA_GUSCIO, " +
	                       "              SUP_EL_PRATO_PASCOLO, " +
	                       "              SUP_EL_COLTURE_MISTE, " +
	                       "              SUP_COLTIVAZ_ARBOREA_CONS, " +
	                       "              SUP_COLTIVAZ_ARBOREA_SPEC, " +
	                       "              DATA_FOTO, " +
	                       "              TIPO_FOTO, " +
	                       "              ID_FONTE, " +
	                       "              ID_DOCUMENTO, " +
	                       "              ID_DOCUMENTO_PROTOCOLLATO, " +
	                       "              ID_IRRIGAZIONE, " +
	                       "              ID_FASCIA_FLUVIALE, " +
	                       "              ID_POTENZIALITA_IRRIGUA, " +
                         "              ID_TERRAZZAMENTO, " +
                         "              ID_ROTAZIONE_COLTURALE, " +
                         "              PERCENTUALE_PENDENZA_MEDIA, " +
                         "              GRADI_PENDENZA_MEDIA, " +
                         "              GRADI_ESPOSIZIONE_MEDIA, " +
                         "              METRI_ALTITUDINE_MEDIA," +
                         "              ID_METODO_IRRIGUO) " +
	                       " VALUES       (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
	                       "               ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
	                       "               ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

			stmt = conn.prepareStatement(query);

			int indice = 0;
			
			SolmrLogger.debug(this, "Executing insertStoricoParticella: "+query);
			
			stmt.setLong(++indice, idStoricoParticella.longValue());
			SolmrLogger.debug(this, "Value of parameter [ID_STORICO_PARTICELLA] in method insertStoricoParticella in StoricoParticellaDAO: "+idStoricoParticella.longValue()+"\n");
			stmt.setLong(++indice, storicoParticellaVO.getIdParticella().longValue());
			SolmrLogger.debug(this, "Value of parameter [ID_PARTICELLA] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdParticella().longValue()+"\n");
			stmt.setTimestamp(++indice, new Timestamp(storicoParticellaVO.getDataInizioValidita().getTime()));
			SolmrLogger.debug(this, "Value of parameter [DATA_INIZIO_VALIDITA] in method insertStoricoParticella in StoricoParticellaDAO: "+new java.sql.Date(storicoParticellaVO.getDataInizioValidita().getTime())+"\n");
			if(storicoParticellaVO.getDataFineValidita() != null) {
				stmt.setTimestamp(++indice, new Timestamp(storicoParticellaVO.getDataFineValidita().getTime()));
				SolmrLogger.debug(this, "Value of parameter [DATA_FINE_VALIDITA] in method insertStoricoParticella in StoricoParticellaDAO: "+new java.sql.Date(storicoParticellaVO.getDataFineValidita().getTime())+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [DATA_FINE_VALIDITA] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			stmt.setString(++indice, storicoParticellaVO.getIstatComune());
			SolmrLogger.debug(this, "Value of parameter [COMUNE] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIstatComune()+"\n");
			if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) {
				stmt.setString(++indice, storicoParticellaVO.getSezione().toUpperCase());
				SolmrLogger.debug(this, "Value of parameter [SEZIONE] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getSezione().toUpperCase()+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [SEZIONE] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			stmt.setLong(++indice, Long.parseLong(storicoParticellaVO.getFoglio()));
			SolmrLogger.debug(this, "Value of parameter [FOGLIO] in method insertStoricoParticella in StoricoParticellaDAO: "+Long.parseLong(storicoParticellaVO.getFoglio())+"\n");
			if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) {
				stmt.setLong(++indice, Long.parseLong(storicoParticellaVO.getParticella()));
				SolmrLogger.debug(this, "Value of parameter [PARTICELLA] in method insertStoricoParticella in StoricoParticellaDAO: "+Long.parseLong(storicoParticellaVO.getParticella())+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [PARTICELLA] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno())) {
				stmt.setString(++indice, storicoParticellaVO.getSubalterno().toUpperCase());
				SolmrLogger.debug(this, "Value of parameter [SUBALTERNO] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getSubalterno().toUpperCase()+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [SUBALTERNO] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			stmt.setString(++indice, StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
			SolmrLogger.debug(this, "Value of parameter [SUP_CATASTALE] in method insertStoricoParticella in StoricoParticellaDAO: "+StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale())+"\n");
			stmt.setString(++indice, StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica()));
      SolmrLogger.debug(this, "Value of parameter [SUPERFICIE_GRAFICA] in method updateStoricoParticella in StoricoParticellaDAO: "+StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica())+"\n");
      if(storicoParticellaVO.getIdZonaAltimetrica() != null) {
				stmt.setLong(++indice, storicoParticellaVO.getIdZonaAltimetrica().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_ZONA_ALTIMETRICA] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdZonaAltimetrica().longValue()+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [ID_ZONA_ALTIMETRICA] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(storicoParticellaVO.getIdAreaA() != null) {
				stmt.setLong(++indice, storicoParticellaVO.getIdAreaA().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_AREA_A] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdAreaA().longValue()+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [ID_AREA_A] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(storicoParticellaVO.getIdAreaB() != null) {
				stmt.setLong(++indice, storicoParticellaVO.getIdAreaB().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_AREA_B] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdAreaB().longValue()+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [ID_AREA_B] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(storicoParticellaVO.getIdAreaC() != null) {
				stmt.setLong(++indice, storicoParticellaVO.getIdAreaC().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_AREA_C] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdAreaC().longValue()+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [ID_AREA_C] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(storicoParticellaVO.getIdAreaD() != null) {
				stmt.setLong(++indice, storicoParticellaVO.getIdAreaD().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_AREA_D] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdAreaD().longValue()+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [ID_AREA_D] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(storicoParticellaVO.getIdAreaG() != null) {
        stmt.setLong(++indice, storicoParticellaVO.getIdAreaG().longValue());
        SolmrLogger.debug(this, "Value of parameter [ID_AREA_G] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdAreaG().longValue()+"\n");
      }
      else {
        stmt.setString(++indice, null);
        SolmrLogger.debug(this, "Value of parameter [ID_AREA_G] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
      }
			if(storicoParticellaVO.getIdAreaH() != null) {
        stmt.setLong(++indice, storicoParticellaVO.getIdAreaH().longValue());
        SolmrLogger.debug(this, "Value of parameter [ID_AREA_H] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdAreaH().longValue()+"\n");
      }
      else {
        stmt.setString(++indice, null);
        SolmrLogger.debug(this, "Value of parameter [ID_AREA_H] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
      }
			if(storicoParticellaVO.getIdAreaI() != null) {
        stmt.setLong(++indice, storicoParticellaVO.getIdAreaI().longValue());
        SolmrLogger.debug(this, "Value of parameter [ID_AREA_I] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdAreaI().longValue()+"\n");
      }
      else {
        stmt.setString(++indice, null);
        SolmrLogger.debug(this, "Value of parameter [ID_AREA_I] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
      }
			if(storicoParticellaVO.getIdAreaL() != null) {
        stmt.setLong(++indice, storicoParticellaVO.getIdAreaL().longValue());
        SolmrLogger.debug(this, "Value of parameter [ID_AREA_L] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdAreaL().longValue()+"\n");
      }
      else {
        stmt.setString(++indice, null);
        SolmrLogger.debug(this, "Value of parameter [ID_AREA_L] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
      }
			if(storicoParticellaVO.getIdAreaM() != null) {
        stmt.setLong(++indice, storicoParticellaVO.getIdAreaM().longValue());
        SolmrLogger.debug(this, "Value of parameter [ID_AREA_M] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdAreaM().longValue()+"\n");
      }
      else {
        stmt.setString(++indice, null);
        SolmrLogger.debug(this, "Value of parameter [ID_AREA_M] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
      }
			stmt.setString(++indice, storicoParticellaVO.getFlagCaptazionePozzi());
			SolmrLogger.debug(this, "Value of parameter [FLAG_CAPTAZIONE_POZZI] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getFlagCaptazionePozzi()+"\n");
			stmt.setString(++indice, storicoParticellaVO.getFlagIrrigabile());
			SolmrLogger.debug(this, "Value of parameter [FLAG_IRRIGABILE] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getFlagIrrigabile()+"\n");
			if(storicoParticellaVO.getIdCasoParticolare() != null) {
				stmt.setLong(++indice, storicoParticellaVO.getIdCasoParticolare().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_CASO_PARTICOLARE] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdCasoParticolare().longValue()+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [ID_CASO_PARTICOLARE] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(storicoParticellaVO.getMotivoModifica() != null) {
				stmt.setString(++indice, storicoParticellaVO.getMotivoModifica());
				SolmrLogger.debug(this, "Value of parameter [MOTIVO_MODIFICA] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getMotivoModifica()+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [MOTIVO_MODIFICA] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			stmt.setTimestamp(++indice, new Timestamp(storicoParticellaVO.getDataAggiornamento().getTime()));
			SolmrLogger.debug(this, "Value of parameter [DATA_AGGIORNAMENTO] in method insertStoricoParticella in StoricoParticellaDAO: "+new Timestamp(storicoParticellaVO.getDataAggiornamento().getTime())+"\n");
			stmt.setLong(++indice, storicoParticellaVO.getIdUtenteAggiornamento().longValue());
			SolmrLogger.debug(this, "Value of parameter [ID_UTENTE_AGGIORNAMENTO] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdUtenteAggiornamento().longValue()+"\n");
			if(storicoParticellaVO.getIdCausaleModParticella() != null) {
				stmt.setLong(++indice, storicoParticellaVO.getIdCausaleModParticella().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_CAUSALE_MOD_PARTICELLA] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdCausaleModParticella().longValue()+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [ID_CAUSALE_MOD_PARTICELLA] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(Validator.isNotEmpty(storicoParticellaVO.getSupNonEleggibile())) {
				stmt.setString(++indice, StringUtils.parseSuperficieField(storicoParticellaVO.getSupNonEleggibile()));
				SolmrLogger.debug(this, "Value of parameter [SUP_NON_ELEGGIBILE] in method insertStoricoParticella in StoricoParticellaDAO: "+StringUtils.parseSuperficieField(storicoParticellaVO.getSupNonEleggibile())+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [SUP_NON_ELEGGIBILE] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(Validator.isNotEmpty(storicoParticellaVO.getSupNeBoscoAcqueFabbricato())) {
				stmt.setString(++indice, StringUtils.parseSuperficieField(storicoParticellaVO.getSupNeBoscoAcqueFabbricato()));
				SolmrLogger.debug(this, "Value of parameter [SUP_NE_BOSCO_ACQUE_FABBRICATO] in method insertStoricoParticella in StoricoParticellaDAO: "+StringUtils.parseSuperficieField(storicoParticellaVO.getSupNeBoscoAcqueFabbricato())+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [SUP_NE_BOSCO_ACQUE_FABBRICATO] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(Validator.isNotEmpty(storicoParticellaVO.getSupNeForaggere())) {
				stmt.setString(++indice, StringUtils.parseSuperficieField(storicoParticellaVO.getSupNeForaggere()));
				SolmrLogger.debug(this, "Value of parameter [SUP_NE_FORAGGERE] in method insertStoricoParticella in StoricoParticellaDAO: "+StringUtils.parseSuperficieField(storicoParticellaVO.getSupNeForaggere())+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [SUP_NE_FORAGGERE] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(Validator.isNotEmpty(storicoParticellaVO.getSupElFruttaGuscio())) {
				stmt.setString(++indice, StringUtils.parseSuperficieField(storicoParticellaVO.getSupElFruttaGuscio()));
				SolmrLogger.debug(this, "Value of parameter [SUP_EL_FRUTTA_GUSCIO] in method insertStoricoParticella in StoricoParticellaDAO: "+StringUtils.parseSuperficieField(storicoParticellaVO.getSupElFruttaGuscio())+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [SUP_EL_FRUTTA_GUSCIO] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(Validator.isNotEmpty(storicoParticellaVO.getSupElPratoPascolo())) {
				stmt.setString(++indice, StringUtils.parseSuperficieField(storicoParticellaVO.getSupElPratoPascolo()));
				SolmrLogger.debug(this, "Value of parameter [SUP_EL_PRATO_PASCOLO] in method insertStoricoParticella in StoricoParticellaDAO: "+StringUtils.parseSuperficieField(storicoParticellaVO.getSupElPratoPascolo())+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [SUP_EL_PRATO_PASCOLO] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(Validator.isNotEmpty(storicoParticellaVO.getSupElColtureMiste())) {
				stmt.setString(++indice, StringUtils.parseSuperficieField(storicoParticellaVO.getSupElColtureMiste()));
				SolmrLogger.debug(this, "Value of parameter [SUP_EL_COLTURE_MISTE] in method insertStoricoParticella in StoricoParticellaDAO: "+StringUtils.parseSuperficieField(storicoParticellaVO.getSupElColtureMiste())+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [SUP_EL_COLTURE_MISTE] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(Validator.isNotEmpty(storicoParticellaVO.getSupColtivazArboreaCons())) {
				stmt.setString(++indice, StringUtils.parseSuperficieField(storicoParticellaVO.getSupColtivazArboreaCons()));
				SolmrLogger.debug(this, "Value of parameter [SUP_COLTIVAZ_ARBOREA_CONS] in method insertStoricoParticella in StoricoParticellaDAO: "+StringUtils.parseSuperficieField(storicoParticellaVO.getSupColtivazArboreaCons())+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [SUP_COLTIVAZ_ARBOREA_CONS] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(Validator.isNotEmpty(storicoParticellaVO.getSupColtivazArboreaSpec())) {
				stmt.setString(++indice, StringUtils.parseSuperficieField(storicoParticellaVO.getSupColtivazArboreaSpec()));
				SolmrLogger.debug(this, "Value of parameter [SUP_COLTIVAZ_ARBOREA_SPEC] in method insertStoricoParticella in StoricoParticellaDAO: "+StringUtils.parseSuperficieField(storicoParticellaVO.getSupColtivazArboreaSpec())+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [SUP_COLTIVAZ_ARBOREA_SPEC] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(storicoParticellaVO.getDataFoto() != null) {
				stmt.setDate(++indice, new java.sql.Date(storicoParticellaVO.getDataFoto().getTime()));
				SolmrLogger.debug(this, "Value of parameter [DATA_FOTO] in method insertStoricoParticella in StoricoParticellaDAO: "+new java.sql.Date(storicoParticellaVO.getDataFoto().getTime())+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [DATA_FOTO] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(Validator.isNotEmpty(storicoParticellaVO.getTipoFoto())) {
				stmt.setString(++indice, storicoParticellaVO.getTipoFoto());
				SolmrLogger.debug(this, "Value of parameter [TIPO_FOTO] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getTipoFoto()+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [TIPO_FOTO] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(storicoParticellaVO.getIdFonte() != null) {
				stmt.setLong(++indice, storicoParticellaVO.getIdFonte().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_FONTE] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdFonte().longValue()+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [ID_FONTE] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(storicoParticellaVO.getIdDocumento() != null) {
				stmt.setLong(++indice, storicoParticellaVO.getIdDocumento().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_DOCUMENTO] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdDocumento().longValue()+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [ID_DOCUMENTO] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(storicoParticellaVO.getIdDocumentoProtocollato() != null) {
				stmt.setLong(++indice, storicoParticellaVO.getIdDocumentoProtocollato().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_DOCUMENTO_PROTOCOLLATO] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdDocumentoProtocollato().longValue()+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [ID_DOCUMENTO_PROTOCOLLATO] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(storicoParticellaVO.getIdIrrigazione() != null) {
				stmt.setLong(++indice, storicoParticellaVO.getIdIrrigazione().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_IRRIGAZIONE] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdIrrigazione().longValue()+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [ID_IRRIGAZIONE] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(storicoParticellaVO.getIdFasciaFluviale() != null) {
				stmt.setLong(++indice, storicoParticellaVO.getIdFasciaFluviale().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_FASCIA_FLUVIALE] in method insertStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdFasciaFluviale().longValue()+"\n");
			}
			else {
				stmt.setString(++indice, null);
				SolmrLogger.debug(this, "Value of parameter [ID_FASCIA_FLUVIALE] in method insertStoricoParticella in StoricoParticellaDAO: "+null+"\n");
			}
			if(storicoParticellaVO.getIdPotenzialitaIrrigua() != null) {
        stmt.setLong(++indice, storicoParticellaVO.getIdPotenzialitaIrrigua().longValue());
        SolmrLogger.debug(this, "Value of parameter [ID_POTENZIALITA_IRRIGUA] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdPotenzialitaIrrigua().longValue()+"\n");
      }
      else {
        stmt.setString(++indice, null);
        SolmrLogger.debug(this, "Value of parameter [ID_POTENZIALITA_IRRIGUA] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
      }
      if(storicoParticellaVO.getIdTerrazzamento() != null) {
        stmt.setLong(++indice, storicoParticellaVO.getIdTerrazzamento().longValue());
        SolmrLogger.debug(this, "Value of parameter [ID_TERRAZZAMENTO] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdTerrazzamento().longValue()+"\n");
      }
      else {
        stmt.setString(++indice, null);
        SolmrLogger.debug(this, "Value of parameter [ID_TERRAZZAMENTO] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
      }
      if(storicoParticellaVO.getIdRotazioneColturale() != null) {
        stmt.setLong(++indice, storicoParticellaVO.getIdRotazioneColturale().longValue());
        SolmrLogger.debug(this, "Value of parameter [ID_ROTAZIONE_COLTURALE] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdRotazioneColturale().longValue()+"\n");
      }
      else {
        stmt.setString(++indice, null);
        SolmrLogger.debug(this, "Value of parameter [ID_ROTAZIONE_COLTURALE] in method updateStoricoParticella in StoricoParticellaDAO: "+null+"\n");
      }
			
      
      stmt.setBigDecimal(++indice, storicoParticellaVO.getPercentualePendenzaMedia());
      SolmrLogger.debug(this, "Value of parameter [PERCENTUALE_PENDENZA_MEDIA] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getPercentualePendenzaMedia()+"\n");
      stmt.setBigDecimal(++indice, storicoParticellaVO.getGradiPendenzaMedia());
      SolmrLogger.debug(this, "Value of parameter [GRADI_PENDENZA_MEDIA] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getGradiPendenzaMedia()+"\n");
      stmt.setBigDecimal(++indice, storicoParticellaVO.getGradiEsposizioneMedia());
      SolmrLogger.debug(this, "Value of parameter [GRADI_ESPOSIZIONE_MEDIA] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getGradiEsposizioneMedia()+"\n");
      stmt.setBigDecimal(++indice, storicoParticellaVO.getMetriAltitudineMedia());
      SolmrLogger.debug(this, "Value of parameter [METRI_ALTITUDINE_MEDIA] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getMetriAltitudineMedia()+"\n");
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(storicoParticellaVO.getIdMetodoIrriguo()));
      SolmrLogger.debug(this, "Value of parameter [ID_METODO_IRRIGUO] in method updateStoricoParticella in StoricoParticellaDAO: "+storicoParticellaVO.getIdMetodoIrriguo()+"\n");
			
			stmt.executeUpdate();

			stmt.close();
	    }
	    catch(SQLException exc) {
	    	SolmrLogger.error(this, "insertStoricoParticella in StoricoParticellaDAO - SQLException: "+exc.getMessage());
	    	throw new DataAccessException(exc.getMessage());
	    }
	    catch (Exception ex) {
	    	SolmrLogger.error(this, "insertStoricoParticella in StoricoParticellaDAO - Generic Exception: "+ex);
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
	    		SolmrLogger.error(this, "insertStoricoParticella in StoricoParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage());
	    		throw new DataAccessException(exc.getMessage());
	    	}
	    	catch(Exception ex) {
	    		SolmrLogger.error(this, "insertStoricoParticella in StoricoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage());
	    		throw new DataAccessException(ex.getMessage());
	    	}
	    }
	    SolmrLogger.debug(this, "Invocated insertStoricoParticella method in StoricoParticellaDAO\n");
	    return idStoricoParticella;
	}
	
	/**
	 * Metodo che mi restituisce l'elenco dei records su DB_STORICO_PARTICELLA a partire
	 * dalla chiave logica della particella(COMUNE, SEZIONE, FOGLIO, PARTICELLA, SUBALTERNO)
	 * Se ricerco per onlyActive == true l'elenco dovrà contenere un solo elemento
	 * altrimenti avrò tutte le fotografie dello storico particella
	 * NB
	 * Sono esclusi i titoli di possesso asservimento (5) e conferimento (6)
	 * 
	 * @param istatComune
	 * @param sezione
	 * @param foglio
	 * @param particella
	 * @param subalterno
	 * @param idAzienda
	 * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
	 * @throws DataAccessException
	 * @throws SolmrException
	 */
	public StoricoParticellaVO[] getListStoricoParticellaVOByParameters(String istatComune, String sezione, String foglio, String particella, String subalterno, boolean onlyActive, String orderBy, Long idAzienda) throws DataAccessException, SolmrException 
	{
		SolmrLogger.debug(this, "Invocating getListStoricoParticellaVOByParameters method in StoricoParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<StoricoParticellaVO> elencoStorico = new Vector<StoricoParticellaVO>();

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in getListStoricoParticellaVOByParameters method in StoricoParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListStoricoParticellaVOByParameters method in StoricoParticellaDAO and it values: "+conn+"\n");

			String query = " SELECT SP.ID_STORICO_PARTICELLA, " +
						   "    SP.ID_PARTICELLA, " +
						   "    SP.DATA_INIZIO_VALIDITA, " +
						   " 		SP.DATA_FINE_VALIDITA, " +
						   " 		SP.COMUNE, " +
						   "		C.DESCOM, " +
						   "		P.SIGLA_PROVINCIA, " +
						   "		SP.SEZIONE, " +
						   "    S.ISTAT_COMUNE, " +
						   "    S.DESCRIZIONE AS DESC_SEZIONE, " +
						   "		SP.FOGLIO, " +
						   "		SP.PARTICELLA, " +
						   "		SP.SUBALTERNO, " +
						   "		SP.SUP_CATASTALE, " +
						   "    SP.SUPERFICIE_GRAFICA, " +
						   " 		SP.ID_ZONA_ALTIMETRICA, " +
						   "		TZA.DESCRIZIONE AS DESC_ALTIMETRICA, " +
						   "		SP.ID_AREA_A, " +
						   "		TAA.DESCRIZIONE AS DESC_AREA_A, " +
						   " 		SP.ID_AREA_B, " +
						   "		TAB.DESCRIZIONE AS DESC_AREA_B, " +
						   "		SP.ID_AREA_C, " +
						   "		TAC.DESCRIZIONE AS DESC_AREA_C, " +
						   "		SP.ID_AREA_D, " +
						   "		TAD.DESCRIZIONE AS DESC_AREA_D, " +
						   "    SP.ID_AREA_G, " +
               "    TAG.DESCRIZIONE AS DESC_AREA_G, " +
               "    SP.ID_AREA_H, " +
               "    TAH.DESCRIZIONE AS DESC_AREA_H, " +
               "    SP.ID_AREA_I, " +
               "    TAI.DESCRIZIONE AS DESC_AREA_I, " +
               "    SP.ID_AREA_L, " +
               "    TAL.DESCRIZIONE AS DESC_AREA_L, " +
               "    SP.ID_AREA_M, " +
               "    TAM.DESCRIZIONE AS DESC_AREA_M, " +
						   "		SP.FLAG_CAPTAZIONE_POZZI, " +
						   "		SP.FLAG_IRRIGABILE, " +
						   "		SP.ID_CASO_PARTICOLARE, " +
						   "		TCP.DESCRIZIONE AS DESC_CASO_PART, " +
						   "		SP.MOTIVO_MODIFICA, " +	
						   "		SP.DATA_AGGIORNAMENTO, " +
						   "		SP.ID_UTENTE_AGGIORNAMENTO, " +
						   "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
					       "          || ' ' " + 
					       "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
					       "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
					       "         WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
						   "    AS DENOMINAZIONE, " +
						   "       (SELECT PVU.DENOMINAZIONE " +
						   "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
						   "        WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
						   "    AS DESCRIZIONE_ENTE_APPARTENENZA, " +
						   "    SP.ID_CAUSALE_MOD_PARTICELLA, " +
						   "    TCMP.DESCRIZIONE AS DESC_CAUSALE_MOD, " +
						   "		SP.SUP_NON_ELEGGIBILE, " +
						   "		SP.SUP_NE_BOSCO_ACQUE_FABBRICATO, " +
						   "		SP.SUP_NE_FORAGGIERE, " +
						   "		SP.SUP_EL_FRUTTA_GUSCIO, " +
						   "		SP.SUP_EL_PRATO_PASCOLO, " +
						   "		SP.SUP_EL_COLTURE_MISTE, " +
						   "		SP.SUP_COLTIVAZ_ARBOREA_CONS, " +
						   "		SP.SUP_COLTIVAZ_ARBOREA_SPEC, " +
						   "		SP.DATA_FOTO, " +
						   "		SP.TIPO_FOTO, " +
						   "		SP.ID_FONTE, " +
						   "		TF.DESCRIZIONE AS DESC_FONTE, " +
						   "		SP.ID_DOCUMENTO, " +
						   "    PART.DATA_CESSAZIONE, " +
						   "    SP.ID_DOCUMENTO_PROTOCOLLATO, " +
						   "    SP.ID_FASCIA_FLUVIALE, " +
               "    TFF.DESCRIZIONE AS DESC_FLUVIALE, " +
						   "    SP.ID_IRRIGAZIONE, " +
						   "    TI.DESCRIZIONE AS DESC_IRRIGAZIONE, " +
						   "    TI.DATA_INIZIO_VALIDITA AS DATA_INI_IRRIGAZ, " +
						   "    TI.DATA_FINE_VALIDITA AS DATA_FINE_IRRIGAZ, " +
						   "    SP.ID_POTENZIALITA_IRRIGUA, " +
               "    TPI.DESCRIZIONE AS DESC_POTENZIALITA_IRRIGUA, " +
               "    TPI.CODICE AS COD_POTENZIALITA_IRRIGUA, " +
               "    SP.ID_TERRAZZAMENTO, " +
               "    TTR.DESCRIZIONE AS DESC_TERRAZZAMENTO, " +
               "    TTR.CODICE AS COD_TERRAZZAMENTO, " +
               "    SP.ID_ROTAZIONE_COLTURALE, " +
               "    TRC.DESCRIZIONE AS DESC_ROTAZIONE_COLTURALE, " +
               "    TRC.CODICE AS COD_ROTAZIONE_COLTURALE, " +
               "    SP.PERCENTUALE_PENDENZA_MEDIA, " +
               "    SP.GRADI_PENDENZA_MEDIA, " +
               "    SP.GRADI_ESPOSIZIONE_MEDIA, " +
               "    SP.METRI_ALTITUDINE_MEDIA, " +
               "    SP.ID_METODO_IRRIGUO, " +
               "    TMI.DESCRIZIONE_METODO_IRRIGUO, " +
               "    TMI.CODICE_METODO_IRRIGUO " +
						   " FROM   DB_STORICO_PARTICELLA SP, " +
						   "		COMUNE C, " +
						   "		PROVINCIA P, " +
						   "		DB_TIPO_ZONA_ALTIMETRICA TZA, " +
						   "		DB_TIPO_AREA_A TAA, " +
						   "		DB_TIPO_AREA_B TAB, " +
						   "		DB_TIPO_AREA_C TAC, " +
						   "		DB_TIPO_AREA_D TAD, " +
						   "    DB_TIPO_AREA_G TAG, " +
               "    DB_TIPO_AREA_H TAH, " +
               "    DB_TIPO_AREA_I TAI, " +
               "    DB_TIPO_AREA_L TAL, " +
               "    DB_TIPO_AREA_M TAM, " +
						   "		DB_TIPO_CASO_PARTICOLARE TCP, " +
						   //"		PAPUA_V_UTENTE_LOGIN PVU, " +
						   "		DB_TIPO_CAUSALE_MOD_PARTICELLA TCMP, " +
						   "		DB_TIPO_FONTE TF, " +
						   " 		DB_TIPO_DOCUMENTO TD, " +
						   "    DB_PARTICELLA PART, " +
						   "    DB_TIPO_IRRIGAZIONE TI, " +
						   "    DB_TIPO_FASCIA_FLUVIALE TFF, " +
						   "    DB_TIPO_POTENZIALITA_IRRIGUA TPI, " +
               "    DB_TIPO_TERRAZZAMENTO TTR, " +
               "    DB_TIPO_ROTAZIONE_COLTURALE TRC, " +
               "    DB_TIPO_METODO_IRRIGUO TMI, " +
						   "    DB_SEZIONE S ";
			if(idAzienda != null) 
			{
				query +=   "        ,DB_UTE U, " +
				           "        DB_CONDUZIONE_PARTICELLA CP ";
			}
			query +=	   " WHERE  SP.COMUNE = C.ISTAT_COMUNE " +
						   " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) " +
						   " AND    SP.ID_ZONA_ALTIMETRICA = TZA.ID_ZONA_ALTIMETRICA(+) " + 
						   " AND    SP.ID_AREA_A = TAA.ID_AREA_A(+) " +
						   " AND    SP.ID_AREA_B = TAB.ID_AREA_B(+) " + 
						   " AND    SP.ID_AREA_C = TAC.ID_AREA_C(+) " +
						   " AND    SP.ID_AREA_D = TAD.ID_AREA_D(+) " +
						   " AND    SP.ID_AREA_G = TAG.ID_AREA_G(+) " +
               " AND    SP.ID_AREA_H = TAH.ID_AREA_H(+) " +
               " AND    SP.ID_AREA_I = TAI.ID_AREA_I(+) " +
               " AND    SP.ID_AREA_L = TAL.ID_AREA_L(+) " +
               " AND    SP.ID_AREA_M = TAM.ID_AREA_M(+) " +
						   " AND    SP.ID_CASO_PARTICOLARE = TCP.ID_CASO_PARTICOLARE(+) " +
						   //" AND    SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
						   " AND    SP.ID_CAUSALE_MOD_PARTICELLA = TCMP.ID_CAUSALE_MOD_PARTICELLA(+) " +
						   " AND    SP.ID_FONTE = TF.ID_FONTE(+) " +
						   " AND    SP.ID_DOCUMENTO = TD.ID_DOCUMENTO(+) " +
						   " AND    SP.ID_PARTICELLA = PART.ID_PARTICELLA " +
						   " AND    SP.ID_IRRIGAZIONE = TI.ID_IRRIGAZIONE(+) " +
						   " AND    SP.ID_FASCIA_FLUVIALE = TFF.ID_FASCIA_FLUVIALE(+) " +
						   " AND    SP.ID_POTENZIALITA_IRRIGUA = TPI.ID_POTENZIALITA_IRRIGUA(+) " +
               " AND    SP.ID_TERRAZZAMENTO = TTR.ID_TERRAZZAMENTO(+) " +
               " AND    SP.ID_ROTAZIONE_COLTURALE = TRC.ID_ROTAZIONE_COLTURALE(+) " +
               " AND    SP.ID_METODO_IRRIGUO = TMI.ID_METODO_IRRIGUO (+) " +
						   " AND    SP.SEZIONE = S.SEZIONE(+) " +
						   " AND    SP.COMUNE = S.ISTAT_COMUNE(+) ";
				           
			if(Validator.isNotEmpty(istatComune)) {
				query +=   " AND    SP.COMUNE = ? " ;
			}
			if(idAzienda != null) {
				query +=   " AND    U.ID_AZIENDA = ? " +
				           " AND    U.ID_UTE = CP.ID_UTE " +
				           " AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA ";
			}
			if(Validator.isNotEmpty(foglio)) {
				query +=   " AND    SP.FOGLIO = ? ";
			}
			if(Validator.isNotEmpty(particella)) {
				query +=   " AND    SP.PARTICELLA = ? ";
			}
			if(Validator.isNotEmpty(sezione)) {
				query +=   " AND    UPPER(SP.SEZIONE) = ? ";
			}
			if(Validator.isNotEmpty(subalterno)) {
				query +=   " AND    UPPER(SP.SUBALTERNO) = ? ";
			}
			if(onlyActive) {
				query +=   " AND   SP.DATA_FINE_VALIDITA IS NULL ";
				if(idAzienda != null) {
					query += " AND   CP.DATA_FINE_CONDUZIONE IS NULL ";
				}
			}
			query += " GROUP BY SP.ID_STORICO_PARTICELLA, " +
			         "          SP.ID_PARTICELLA, " +
			         "          SP.DATA_INIZIO_VALIDITA, " +
			         "          SP.DATA_FINE_VALIDITA, " +
			         "          SP.COMUNE, " +
			         "          C.DESCOM, " +
			         "          P.SIGLA_PROVINCIA, " +
			         "          SP.SEZIONE, " +
			         "          S.ISTAT_COMUNE, " +
			         "          S.DESCRIZIONE, " +
			         "          SP.FOGLIO, " +
			         "          SP.PARTICELLA, " +
			         "          SP.SUBALTERNO, " +
			         "          SP.SUP_CATASTALE, " +
			         "          SP.SUPERFICIE_GRAFICA, " +
			         "          SP.ID_ZONA_ALTIMETRICA, " +
			         "          TZA.DESCRIZIONE, " +
			         "          SP.ID_AREA_A, " +
			         "          TAA.DESCRIZIONE, " +
			         "          SP.ID_AREA_B, " +
			         "          TAB.DESCRIZIONE, " +
			         "          SP.ID_AREA_C, " +
			         "          TAC.DESCRIZIONE, " +
			         "          SP.ID_AREA_D, " +
			         "          TAD.DESCRIZIONE, " +
			         "          SP.ID_AREA_G, " +
			         "          TAG.DESCRIZIONE, " +
               "          SP.ID_AREA_H, " +
               "          TAH.DESCRIZIONE, " +
               "          SP.ID_AREA_I, " +
               "          TAI.DESCRIZIONE, " +
               "          SP.ID_AREA_L, " +
               "          TAL.DESCRIZIONE, " +
               "          SP.ID_AREA_M, " +
               "          TAM.DESCRIZIONE, " +
			         "          SP.FLAG_CAPTAZIONE_POZZI, " +
			         "          SP.FLAG_IRRIGABILE, " +
			         "          SP.ID_CASO_PARTICOLARE, " +
			         "          TCP.DESCRIZIONE, " +
			         "          SP.MOTIVO_MODIFICA, " +
			         "          SP.DATA_AGGIORNAMENTO, " +
			         "          SP.ID_UTENTE_AGGIORNAMENTO, " +			         
			         "          SP.ID_CAUSALE_MOD_PARTICELLA, " +
			         "          TCMP.DESCRIZIONE, " +
			         "          SP.SUP_NON_ELEGGIBILE, " +
			         "          SP.SUP_NE_BOSCO_ACQUE_FABBRICATO, " +
			         "          SP.SUP_NE_FORAGGIERE, " +
			         "          SP.SUP_EL_FRUTTA_GUSCIO, " +
			         "          SP.SUP_EL_PRATO_PASCOLO, " +
			         "          SP.SUP_EL_COLTURE_MISTE, " +
			         "          SP.SUP_COLTIVAZ_ARBOREA_CONS, " +
			         "          SP.SUP_COLTIVAZ_ARBOREA_SPEC, " +
			         "          SP.DATA_FOTO, " +
			         "          SP.TIPO_FOTO, " +
			         "          SP.ID_FONTE, " +
			         "          TF.DESCRIZIONE, " +
			         "          SP.ID_DOCUMENTO, " +
			         "          PART.DATA_CESSAZIONE, " +
			         "          SP.ID_DOCUMENTO_PROTOCOLLATO, " +
			         "          SP.ID_FASCIA_FLUVIALE, " +
               "          TFF.DESCRIZIONE, " +
			         "          SP.ID_IRRIGAZIONE, " +
			         "          TI.DESCRIZIONE, " +
			         "          TI.DATA_INIZIO_VALIDITA, " +
			         "          TI.DATA_FINE_VALIDITA, " +
        			 "          SP.ID_POTENZIALITA_IRRIGUA, " +
               "          TPI.DESCRIZIONE, " +
               "          TPI.CODICE, " +
               "          SP.ID_TERRAZZAMENTO, " +
               "          TTR.DESCRIZIONE, " +
               "          TTR.CODICE, " +
               "          SP.ID_ROTAZIONE_COLTURALE, " +
               "          TRC.DESCRIZIONE, " +
               "          TRC.CODICE, " +
			         "          SP.PERCENTUALE_PENDENZA_MEDIA, " +
               "          SP.GRADI_PENDENZA_MEDIA, " +
               "          SP.GRADI_ESPOSIZIONE_MEDIA, " +
               "          SP.METRI_ALTITUDINE_MEDIA, " +
               "          SP.ID_METODO_IRRIGUO, " +
               "          TMI.DESCRIZIONE_METODO_IRRIGUO, " +
               "          TMI.CODICE_METODO_IRRIGUO ";
			if(Validator.isNotEmpty(orderBy)) 
			{
				query += "ORDER BY "+orderBy;
			}

			SolmrLogger.debug(this, "Value of parameter 1 [ISTAT_COMUNE] in getListStoricoParticellaVOByParameters method in StoricoParticellaDAO: "+istatComune+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [SEZIONE] in getListStoricoParticellaVOByParameters method in StoricoParticellaDAO: "+sezione+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [FOGLIO] in getListStoricoParticellaVOByParameters method in StoricoParticellaDAO: "+foglio+"\n");
			SolmrLogger.debug(this, "Value of parameter 4 [PARTICELLA] in getListStoricoParticellaVOByParameters method in StoricoParticellaDAO: "+particella+"\n");
			SolmrLogger.debug(this, "Value of parameter 5 [SUBALTERNO] in getListStoricoParticellaVOByParameters method in StoricoParticellaDAO: "+subalterno+"\n");
			SolmrLogger.debug(this, "Value of parameter 6 [ONLY_ACTIVE] in getListStoricoParticellaVOByParameters method in StoricoParticellaDAO: "+onlyActive+"\n");
			SolmrLogger.debug(this, "Value of parameter 7 [ORDER_BY] in getListStoricoParticellaVOByParameters method in StoricoParticellaDAO: "+orderBy+"\n");
			SolmrLogger.debug(this, "Value of parameter 8 [ID_AZIENDA] in getListStoricoParticellaVOByParameters method in StoricoParticellaDAO: "+idAzienda+"\n");

			stmt = conn.prepareStatement(query);
			
			// Setto il timeout possibile della query
			stmt.setQueryTimeout(SolmrConstants.MAX2_TIME_WAIT);
			
			int indice = 1;
			if(Validator.isNotEmpty(istatComune)) {
				stmt.setString(indice, istatComune);
				indice++;
			}
			if(idAzienda != null) {
				stmt.setLong(indice, idAzienda.longValue());
				indice++;
			}
			if(Validator.isNotEmpty(foglio)) {
				stmt.setString(indice, foglio);
				indice++;
			}
			if(Validator.isNotEmpty(particella)) {
				stmt.setString(indice, particella);
				indice++;
			}
			if(Validator.isNotEmpty(sezione)) {
				stmt.setString(indice, sezione.toUpperCase());
				indice++;
			}
			if(Validator.isNotEmpty(subalterno)) {
				stmt.setString(indice, subalterno.toUpperCase());
			}
			
			SolmrLogger.debug(this, "Executing getListStoricoParticellaVOByParameters: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO(); 
				storicoParticellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
				storicoParticellaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
				storicoParticellaVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				storicoParticellaVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
				ComuneVO comuneVO = new ComuneVO();
				comuneVO.setIstatComune(rs.getString("COMUNE"));
				comuneVO.setDescom(rs.getString("DESCOM"));
				if(Validator.isNotEmpty(rs.getString("SIGLA_PROVINCIA"))) {
					ProvinciaVO provinciaVO = new ProvinciaVO();
					provinciaVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));
					comuneVO.setProvinciaVO(provinciaVO);
				}
				storicoParticellaVO.setComuneParticellaVO(comuneVO);
				storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
				if(Validator.isNotEmpty(rs.getString("SEZIONE"))) {
					SezioneVO sezioneVO = new SezioneVO();
					sezioneVO.setIstatComune(rs.getString("ISTAT_COMUNE"));
					sezioneVO.setSezione(rs.getString("SEZIONE"));
					sezioneVO.setDescrizione(rs.getString("DESC_SEZIONE"));
					storicoParticellaVO.setSezioneVO(sezioneVO);
				}
				storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
				storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
				storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
				storicoParticellaVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
				storicoParticellaVO.setSuperficieGrafica(rs.getString("SUPERFICIE_GRAFICA"));
				if(Validator.isNotEmpty(rs.getString("ID_ZONA_ALTIMETRICA"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_ZONA_ALTIMETRICA")), rs.getString("DESC_ALTIMETRICA"));
					storicoParticellaVO.setIdZonaAltimetrica(Long.decode(rs.getString("ID_ZONA_ALTIMETRICA")));
					storicoParticellaVO.setZonaAltimetrica(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_AREA_A"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_A")), rs.getString("DESC_AREA_A"));
					storicoParticellaVO.setIdAreaA(new Long(rs.getString("ID_AREA_A")));
					storicoParticellaVO.setAreaA(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_AREA_B"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_B")), rs.getString("DESC_AREA_B"));
					storicoParticellaVO.setIdAreaB(new Long(rs.getString("ID_AREA_B")));
					storicoParticellaVO.setAreaB(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_AREA_C"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_C")), rs.getString("DESC_AREA_C"));
					storicoParticellaVO.setIdAreaC(new Long(rs.getString("ID_AREA_C")));
					storicoParticellaVO.setAreaC(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_AREA_D"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_D")), rs.getString("DESC_AREA_D"));
					storicoParticellaVO.setIdAreaD(new Long(rs.getString("ID_AREA_D")));
					storicoParticellaVO.setAreaD(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_AREA_G"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_G")), rs.getString("DESC_AREA_G"));
          storicoParticellaVO.setIdAreaG(new Long(rs.getString("ID_AREA_G")));
          storicoParticellaVO.setAreaG(code);
        }
        if(Validator.isNotEmpty(rs.getString("ID_AREA_H"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_H")), rs.getString("DESC_AREA_H"));
          storicoParticellaVO.setIdAreaH(new Long(rs.getString("ID_AREA_H")));
          storicoParticellaVO.setAreaH(code);
        }
        if(Validator.isNotEmpty(rs.getString("ID_AREA_I"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_I")), rs.getString("DESC_AREA_I"));
          storicoParticellaVO.setIdAreaI(new Long(rs.getString("ID_AREA_I")));
          storicoParticellaVO.setAreaI(code);
        }
        if(Validator.isNotEmpty(rs.getString("ID_AREA_L"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_L")), rs.getString("DESC_AREA_L"));
          storicoParticellaVO.setIdAreaL(new Long(rs.getString("ID_AREA_L")));
          storicoParticellaVO.setAreaL(code);
        }
        if(Validator.isNotEmpty(rs.getString("ID_AREA_M"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_M")), rs.getString("DESC_AREA_M"));
          storicoParticellaVO.setIdAreaM(new Long(rs.getString("ID_AREA_M")));
          storicoParticellaVO.setAreaM(code);
        }
				storicoParticellaVO.setFlagCaptazionePozzi(rs.getString("FLAG_CAPTAZIONE_POZZI"));
				storicoParticellaVO.setFlagIrrigabile(rs.getString("FLAG_IRRIGABILE"));
				if(Validator.isNotEmpty(rs.getString("ID_CASO_PARTICOLARE"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_CASO_PARTICOLARE")), rs.getString("DESC_CASO_PART"));
					storicoParticellaVO.setIdCasoParticolare(new Long(rs.getString("ID_CASO_PARTICOLARE")));
					storicoParticellaVO.setCasoParticolare(code);
				}
				storicoParticellaVO.setMotivoModifica(rs.getString("MOTIVO_MODIFICA"));
				storicoParticellaVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
				storicoParticellaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				UtenteIrideVO utenteIrideVO = new UtenteIrideVO();
				utenteIrideVO.setIdUtente(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				utenteIrideVO.setDenominazione(rs.getString("DENOMINAZIONE"));
				utenteIrideVO.setDescrizioneEnteAppartenenza(rs.getString("DESCRIZIONE_ENTE_APPARTENENZA"));
				storicoParticellaVO.setUtenteAggiornamento(utenteIrideVO);
				if(Validator.isNotEmpty(rs.getString("ID_CAUSALE_MOD_PARTICELLA"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_CAUSALE_MOD_PARTICELLA")), rs.getString("DESC_CAUSALE_MOD"));
					storicoParticellaVO.setIdCausaleModParticella(new Long(rs.getString("ID_CAUSALE_MOD_PARTICELLA")));
					storicoParticellaVO.setCausaleModParticella(code);
				}
				storicoParticellaVO.setSupNonEleggibile(rs.getString("SUP_NON_ELEGGIBILE"));
				storicoParticellaVO.setSupNeBoscoAcqueFabbricato(rs.getString("SUP_NE_BOSCO_ACQUE_FABBRICATO"));
				storicoParticellaVO.setSupNeForaggere(rs.getString("SUP_NE_FORAGGIERE"));
				storicoParticellaVO.setSupElFruttaGuscio(rs.getString("SUP_EL_FRUTTA_GUSCIO"));
				storicoParticellaVO.setSupElPratoPascolo(rs.getString("SUP_EL_PRATO_PASCOLO"));
				storicoParticellaVO.setSupElColtureMiste(rs.getString("SUP_EL_COLTURE_MISTE"));
				storicoParticellaVO.setSupColtivazArboreaCons(rs.getString("SUP_COLTIVAZ_ARBOREA_CONS"));
				storicoParticellaVO.setSupColtivazArboreaSpec(rs.getString("SUP_COLTIVAZ_ARBOREA_SPEC"));
				storicoParticellaVO.setDataFoto(rs.getDate("DATA_FOTO"));
				storicoParticellaVO.setTipoFoto(rs.getString("TIPO_FOTO"));
				if(Validator.isNotEmpty(rs.getString("ID_FONTE"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_FONTE")), rs.getString("DESC_FONTE"));
					storicoParticellaVO.setIdFonte(new Long(rs.getString("ID_FONTE")));
					storicoParticellaVO.setFonte(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_DOCUMENTO"))) {
					storicoParticellaVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
				}
				storicoParticellaVO.setDataCessazione(rs.getDate("DATA_CESSAZIONE"));
				if(Validator.isNotEmpty(rs.getString("ID_DOCUMENTO_PROTOCOLLATO"))) {
					storicoParticellaVO.setIdDocumentoProtocollato(new Long(rs.getString("ID_DOCUMENTO_PROTOCOLLATO")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_FASCIA_FLUVIALE"))) {
          storicoParticellaVO.setIdFasciaFluviale(new Long(rs.getLong("ID_FASCIA_FLUVIALE")));
          CodeDescription fasciaFluviale = new CodeDescription(Integer.decode(rs.getString("ID_FASCIA_FLUVIALE")), rs.getString("DESC_FLUVIALE"));
          storicoParticellaVO.setFasciaFluviale(fasciaFluviale);
        }
				if(Validator.isNotEmpty(rs.getString("ID_IRRIGAZIONE"))) {
					storicoParticellaVO.setIdIrrigazione(new Long(rs.getLong("ID_IRRIGAZIONE")));
					TipoIrrigazioneVO tipoIrrigazioneVO = new TipoIrrigazioneVO();
					tipoIrrigazioneVO.setIdIrrigazione(new Long(rs.getLong("ID_IRRIGAZIONE")));
					tipoIrrigazioneVO.setDescrizione(rs.getString("DESC_IRRIGAZIONE"));
					tipoIrrigazioneVO.setDataInizioValidita(rs.getDate("DATA_INI_IRRIGAZ"));
					tipoIrrigazioneVO.setDataFineValidita(rs.getDate("DATA_FINE_IRRIGAZ"));
					storicoParticellaVO.setTipoIrrigazioneVO(tipoIrrigazioneVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_POTENZIALITA_IRRIGUA"))) {
          storicoParticellaVO.setIdPotenzialitaIrrigua(new Long(rs.getLong("ID_POTENZIALITA_IRRIGUA")));
          TipoPotenzialitaIrriguaVO tipoPotenzialitaIrriguaVO = new TipoPotenzialitaIrriguaVO();
          tipoPotenzialitaIrriguaVO.setIdPotenzialitaIrrigua(new Long(rs.getLong("ID_POTENZIALITA_IRRIGUA")));
          tipoPotenzialitaIrriguaVO.setDescrizione(rs.getString("DESC_POTENZIALITA_IRRIGUA"));
          tipoPotenzialitaIrriguaVO.setCodice(rs.getString("COD_POTENZIALITA_IRRIGUA"));
          storicoParticellaVO.setTipoPotenzialitaIrriguaVO(tipoPotenzialitaIrriguaVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_TERRAZZAMENTO"))) {
          storicoParticellaVO.setIdTerrazzamento(new Long(rs.getLong("ID_TERRAZZAMENTO")));
          TipoTerrazzamentoVO tipoTerrazzamentoVO = new TipoTerrazzamentoVO();
          tipoTerrazzamentoVO.setIdTerrazzamento(new Long(rs.getLong("ID_TERRAZZAMENTO")));
          tipoTerrazzamentoVO.setDescrizione(rs.getString("DESC_TERRAZZAMENTO"));
          tipoTerrazzamentoVO.setCodice(rs.getString("COD_TERRAZZAMENTO"));
          storicoParticellaVO.setTipoTerrazzamentoVO(tipoTerrazzamentoVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_ROTAZIONE_COLTURALE"))) {
          storicoParticellaVO.setIdRotazioneColturale(new Long(rs.getLong("ID_ROTAZIONE_COLTURALE")));
          TipoRotazioneColturaleVO tipoRotazioneColturaleVO = new TipoRotazioneColturaleVO();
          tipoRotazioneColturaleVO.setIdRotazioneColturale(new Long(rs.getLong("ID_ROTAZIONE_COLTURALE")));
          tipoRotazioneColturaleVO.setDescrizione(rs.getString("DESC_ROTAZIONE_COLTURALE"));
          tipoRotazioneColturaleVO.setCodice(rs.getString("COD_ROTAZIONE_COLTURALE"));
          storicoParticellaVO.setTipoRotazioneColturaleVO(tipoRotazioneColturaleVO);
        }
        
        storicoParticellaVO.setPercentualePendenzaMedia(rs.getBigDecimal("PERCENTUALE_PENDENZA_MEDIA"));
        storicoParticellaVO.setGradiPendenzaMedia(rs.getBigDecimal("GRADI_PENDENZA_MEDIA"));
        storicoParticellaVO.setGradiEsposizioneMedia(rs.getBigDecimal("GRADI_ESPOSIZIONE_MEDIA"));
        storicoParticellaVO.setMetriAltitudineMedia(rs.getBigDecimal("METRI_ALTITUDINE_MEDIA"));
				
        
        if(Validator.isNotEmpty(rs.getString("ID_METODO_IRRIGUO"))) {
          storicoParticellaVO.setIdMetodoIrriguo(new Long(rs.getLong("ID_METODO_IRRIGUO")));
          TipoMetodoIrriguoVO tipoMetodoIrriguo = new TipoMetodoIrriguoVO();
          tipoMetodoIrriguo.setCodiceMetodoIrriguo(rs.getString("CODICE_METODO_IRRIGUO"));
          tipoMetodoIrriguo.setDescrizioneMetodoIrriguo(rs.getString("DESCRIZIONE_METODO_IRRIGUO"));
          
          storicoParticellaVO.setTipoMetodoIrriguo(tipoMetodoIrriguo);
        }
				
				
				elencoStorico.add(storicoParticellaVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListStoricoParticellaVOByParameters in StoricoParticellaDAO - SQLException: "+exc.getMessage()+"\n");
			if(exc.getErrorCode() == SolmrConstants.ORACLE_PREPARE_STATEMENT_TIME_OUT) {
				throw new SolmrException(AnagErrors.ERRORE_FILTRI_GENERICI);
			}
			else {
				throw new DataAccessException(exc.getMessage());
			}
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListStoricoParticellaVOByParameters in StoricoParticellaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListStoricoParticellaVOByParameters in StoricoParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListStoricoParticellaVOByParameters in StoricoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListStoricoParticellaVOByParameters method in StoricoParticellaDAO\n");
		if(elencoStorico.size() == 0) {
			return (StoricoParticellaVO[])elencoStorico.toArray(new StoricoParticellaVO[0]);
		}
		else {
			return (StoricoParticellaVO[])elencoStorico.toArray(new StoricoParticellaVO[elencoStorico.size()]);
		}
	}
	
	
	/**
   * Metodo che mi restituisce l'elenco dei records su DB_STORICO_PARTICELLA a partire
   * dalla chiave logica della particella(COMUNE, SEZIONE, FOGLIO, PARTICELLA, SUBALTERNO)
   * N.B.
   * Sono esclusi i titoli di possesso asservimento (5) e conferimento (6)
   * 
   * @param istatComune
   * @param sezione
   * @param foglio
   * @param particella
   * @param subalterno
   * @param onlyActive
   * @param orderBy
   * @param idAzienda
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws DataAccessException
   * @throws SolmrException
   */
  public StoricoParticellaVO[] getListStoricoParticellaVOByParametersImpUnar(String istatComune, 
      String sezione, String foglio, String particella, String subalterno, long idAzienda) 
      throws DataAccessException, SolmrException 
  {
    SolmrLogger.debug(this, "Invocating getListStoricoParticellaVOByParametersImpUnar method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoStorico = new Vector<StoricoParticellaVO>();

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getListStoricoParticellaVOByParametersImpUnar method in StoricoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListStoricoParticellaVOByParametersImpUnar method in StoricoParticellaDAO and it values: "+conn+"\n");

      String query = " SELECT SP.ID_STORICO_PARTICELLA, " +
               "    SP.ID_PARTICELLA, " +
               "    SP.DATA_INIZIO_VALIDITA, " +
               "    SP.DATA_FINE_VALIDITA, " +
               "    SP.COMUNE, " +
               "    C.DESCOM, " +
               "    P.SIGLA_PROVINCIA, " +
               "    SP.SEZIONE, " +
               "    S.ISTAT_COMUNE, " +
               "    S.DESCRIZIONE AS DESC_SEZIONE, " +
               "    SP.FOGLIO, " +
               "    SP.PARTICELLA, " +
               "    SP.SUBALTERNO, " +
               "    SP.SUP_CATASTALE, " +
               "    SP.SUPERFICIE_GRAFICA, " +
               "    SP.ID_ZONA_ALTIMETRICA, " +
               "    TZA.DESCRIZIONE AS DESC_ALTIMETRICA, " +
               "    SP.ID_AREA_A, " +
               "    TAA.DESCRIZIONE AS DESC_AREA_A, " +
               "    SP.ID_AREA_B, " +
               "    TAB.DESCRIZIONE AS DESC_AREA_B, " +
               "    SP.ID_AREA_C, " +
               "    TAC.DESCRIZIONE AS DESC_AREA_C, " +
               "    SP.ID_AREA_D, " +
               "    TAD.DESCRIZIONE AS DESC_AREA_D, " +
               "    SP.FLAG_CAPTAZIONE_POZZI, " +
               "    SP.FLAG_IRRIGABILE, " +
               "    SP.ID_CASO_PARTICOLARE, " +
               "    TCP.DESCRIZIONE AS DESC_CASO_PART, " +
               "    SP.MOTIVO_MODIFICA, " + 
               "    SP.DATA_AGGIORNAMENTO, " +
               "    SP.ID_UTENTE_AGGIORNAMENTO, " +
               "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
               "          || ' ' " + 
               "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
               "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
               "         WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
               "    AS DENOMINAZIONE, " +
               "       (SELECT PVU.DENOMINAZIONE " +
               "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
               "        WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
               "    AS DESCRIZIONE_ENTE_APPARTENENZA, " +
               "    SP.ID_CAUSALE_MOD_PARTICELLA, " +
               "    TCMP.DESCRIZIONE AS DESC_CAUSALE_MOD, " +
               "    SP.SUP_NON_ELEGGIBILE, " +
               "    SP.SUP_NE_BOSCO_ACQUE_FABBRICATO, " +
               "    SP.SUP_NE_FORAGGIERE, " +
               "    SP.SUP_EL_FRUTTA_GUSCIO, " +
               "    SP.SUP_EL_PRATO_PASCOLO, " +
               "    SP.SUP_EL_COLTURE_MISTE, " +
               "    SP.SUP_COLTIVAZ_ARBOREA_CONS, " +
               "    SP.SUP_COLTIVAZ_ARBOREA_SPEC, " +
               "    SP.DATA_FOTO, " +
               "    SP.TIPO_FOTO, " +
               "    SP.ID_FONTE, " +
               "    TF.DESCRIZIONE AS DESC_FONTE, " +
               "    SP.ID_DOCUMENTO, " +
               "    PART.DATA_CESSAZIONE, " +
               "    SP.ID_DOCUMENTO_PROTOCOLLATO, " +
               "    SP.ID_FASCIA_FLUVIALE, " +
               "    TFF.DESCRIZIONE AS DESC_FLUVIALE, " +
               "    SP.ID_IRRIGAZIONE, " +
               "    TI.DESCRIZIONE AS DESC_IRRIGAZIONE, " +
               "    TI.DATA_INIZIO_VALIDITA AS DATA_INI_IRRIGAZ, " +
               "    TI.DATA_FINE_VALIDITA AS DATA_FINE_IRRIGAZ, " +
               "    SP.ID_POTENZIALITA_IRRIGUA, " +
               "    TPI.DESCRIZIONE AS DESC_POTENZIALITA_IRRIGUA, " +
               "    TPI.CODICE AS COD_POTENZIALITA_IRRIGUA, " +
               "    SP.ID_TERRAZZAMENTO, " +
               "    TTR.DESCRIZIONE AS DESC_TERRAZZAMENTO, " +
               "    TTR.CODICE AS COD_TERRAZZAMENTO, " +
               "    SP.ID_ROTAZIONE_COLTURALE, " +
               "    TRC.DESCRIZIONE AS DESC_ROTAZIONE_COLTURALE, " +
               "    TRC.CODICE AS COD_ROTAZIONE_COLTURALE " +
               " FROM   DB_STORICO_PARTICELLA SP, " +
               "    COMUNE C, " +
               "    PROVINCIA P, " +
               "    DB_TIPO_ZONA_ALTIMETRICA TZA, " +
               "    DB_TIPO_AREA_A TAA, " +
               "    DB_TIPO_AREA_B TAB, " +
               "    DB_TIPO_AREA_C TAC, " +
               "    DB_TIPO_AREA_D TAD, " +
               "    DB_TIPO_CASO_PARTICOLARE TCP, " +
               //"    PAPUA_V_UTENTE_LOGIN PVU, " +
               "    DB_TIPO_CAUSALE_MOD_PARTICELLA TCMP, " +
               "    DB_TIPO_FONTE TF, " +
               "    DB_TIPO_DOCUMENTO TD, " +
               "    DB_PARTICELLA PART, " +
               "    DB_TIPO_IRRIGAZIONE TI, " +
               "    DB_SEZIONE S, " +
               "    DB_UTE U, " +
               "    DB_CONDUZIONE_PARTICELLA CP, " +
               "    DB_TIPO_FASCIA_FLUVIALE TFF, " +
               "    DB_TIPO_POTENZIALITA_IRRIGUA TPI, " +
               "    DB_TIPO_TERRAZZAMENTO TTR, " +
               "    DB_TIPO_ROTAZIONE_COLTURALE TRC " +
               " WHERE  SP.COMUNE = C.ISTAT_COMUNE " +
               " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) " +
               " AND    SP.ID_ZONA_ALTIMETRICA = TZA.ID_ZONA_ALTIMETRICA(+) " + 
               " AND    SP.ID_AREA_A = TAA.ID_AREA_A(+) " +
               " AND    SP.ID_AREA_B = TAB.ID_AREA_B(+) " + 
               " AND    SP.ID_AREA_C = TAC.ID_AREA_C(+) " +
               " AND    SP.ID_AREA_D = TAD.ID_AREA_D(+) " +
               " AND    SP.ID_CASO_PARTICOLARE = TCP.ID_CASO_PARTICOLARE(+) " +
              // " AND    SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
               " AND    SP.ID_CAUSALE_MOD_PARTICELLA = TCMP.ID_CAUSALE_MOD_PARTICELLA(+) " +
               " AND    SP.ID_FONTE = TF.ID_FONTE(+) " +
               " AND    SP.ID_DOCUMENTO = TD.ID_DOCUMENTO(+) " +
               " AND    SP.ID_PARTICELLA = PART.ID_PARTICELLA " +
               " AND    SP.ID_IRRIGAZIONE = TI.ID_IRRIGAZIONE(+) " +
               " AND    SP.ID_FASCIA_FLUVIALE = TFF.ID_FASCIA_FLUVIALE(+) " +
               " AND    SP.ID_POTENZIALITA_IRRIGUA = TPI.ID_POTENZIALITA_IRRIGUA(+) " +
               " AND    SP.ID_TERRAZZAMENTO = TTR.ID_TERRAZZAMENTO(+) " +
               " AND    SP.ID_ROTAZIONE_COLTURALE = TRC.ID_ROTAZIONE_COLTURALE(+) " +
               " AND    SP.SEZIONE = S.SEZIONE(+) " +
               " AND    SP.COMUNE = S.ISTAT_COMUNE(+) " +
               " AND    U.ID_AZIENDA = ? " +
               " AND    U.ID_UTE = CP.ID_UTE " +
               " AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
               " AND    SP.DATA_FINE_VALIDITA IS NULL " +
               " AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
               " AND    CP.ID_TITOLO_POSSESSO <> 5 " + //Asservite
               " AND    CP.ID_TITOLO_POSSESSO <> 6 "; //Conferite
                   
      if(Validator.isNotEmpty(istatComune)) {
        query +=   " AND    SP.COMUNE = ? " ;
      }
      if(Validator.isNotEmpty(foglio)) {
        query +=   " AND    SP.FOGLIO = ? ";
      }
      if(Validator.isNotEmpty(particella)) {
        query +=   " AND    SP.PARTICELLA = ? ";
      }
      if(Validator.isNotEmpty(sezione)) {
        query +=   " AND    UPPER(SP.SEZIONE) = ? ";
      }
      if(Validator.isNotEmpty(subalterno)) {
        query +=   " AND    UPPER(SP.SUBALTERNO) = ? ";
      }
      
      query += " GROUP BY SP.ID_STORICO_PARTICELLA, " +
               "          SP.ID_PARTICELLA, " +
               "          SP.DATA_INIZIO_VALIDITA, " +
               "          SP.DATA_FINE_VALIDITA, " +
               "          SP.COMUNE, " +
               "          C.DESCOM, " +
               "          P.SIGLA_PROVINCIA, " +
               "          SP.SEZIONE, " +
               "          S.ISTAT_COMUNE, " +
               "          S.DESCRIZIONE, " +
               "          SP.FOGLIO, " +
               "          SP.PARTICELLA, " +
               "          SP.SUBALTERNO, " +
               "          SP.SUP_CATASTALE, " +
               "          SP.SUPERFICIE_GRAFICA, " +
               "          SP.ID_ZONA_ALTIMETRICA, " +
               "          TZA.DESCRIZIONE, " +
               "          SP.ID_AREA_A, " +
               "          TAA.DESCRIZIONE, " +
               "          SP.ID_AREA_B, " +
               "          TAB.DESCRIZIONE, " +
               "          SP.ID_AREA_C, " +
               "          TAC.DESCRIZIONE, " +
               "          SP.ID_AREA_D, " +
               "          TAD.DESCRIZIONE, " +
               "          SP.FLAG_CAPTAZIONE_POZZI, " +
               "          SP.FLAG_IRRIGABILE, " +
               "          SP.ID_CASO_PARTICOLARE, " +
               "          TCP.DESCRIZIONE, " +
               "          SP.MOTIVO_MODIFICA, " +
               "          SP.DATA_AGGIORNAMENTO, " +
               "          SP.ID_UTENTE_AGGIORNAMENTO, " +             
               "          SP.ID_CAUSALE_MOD_PARTICELLA, " +
               "          TCMP.DESCRIZIONE, " +
               "          SP.SUP_NON_ELEGGIBILE, " +
               "          SP.SUP_NE_BOSCO_ACQUE_FABBRICATO, " +
               "          SP.SUP_NE_FORAGGIERE, " +
               "          SP.SUP_EL_FRUTTA_GUSCIO, " +
               "          SP.SUP_EL_PRATO_PASCOLO, " +
               "          SP.SUP_EL_COLTURE_MISTE, " +
               "          SP.SUP_COLTIVAZ_ARBOREA_CONS, " +
               "          SP.SUP_COLTIVAZ_ARBOREA_SPEC, " +
               "          SP.DATA_FOTO, " +
               "          SP.TIPO_FOTO, " +
               "          SP.ID_FONTE, " +
               "          TF.DESCRIZIONE, " +
               "          SP.ID_DOCUMENTO, " +
               "          PART.DATA_CESSAZIONE, " +
               "          SP.ID_DOCUMENTO_PROTOCOLLATO, " +
               "          SP.ID_FASCIA_FLUVIALE, " +
               "          TFF.DESCRIZIONE, " +
               "          SP.ID_IRRIGAZIONE, " +
               "          TI.DESCRIZIONE, " +
               "          TI.DATA_INIZIO_VALIDITA, " +
               "          TI.DATA_FINE_VALIDITA, " +
               "          SP.ID_POTENZIALITA_IRRIGUA, " +
               "          TPI.DESCRIZIONE, " +
               "          TPI.CODICE, " +
               "          SP.ID_TERRAZZAMENTO, " +
               "          TTR.DESCRIZIONE, " +
               "          TTR.CODICE, " +
               "          SP.ID_ROTAZIONE_COLTURALE, " +
               "          TRC.DESCRIZIONE, " +
               "          TRC.CODICE " +
               "ORDER BY  C.DESCOM, SP.SEZIONE, SP.FOGLIO, SP.PARTICELLA, SP.SUBALTERNO";

      SolmrLogger.debug(this, "Value of parameter 1 [ISTAT_COMUNE] in getListStoricoParticellaVOByParametersImpUnar method in StoricoParticellaDAO: "+istatComune+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [SEZIONE] in getListStoricoParticellaVOByParametersImpUnar method in StoricoParticellaDAO: "+sezione+"\n");
      SolmrLogger.debug(this, "Value of parameter 3 [FOGLIO] in getListStoricoParticellaVOByParametersImpUnar method in StoricoParticellaDAO: "+foglio+"\n");
      SolmrLogger.debug(this, "Value of parameter 4 [PARTICELLA] in getListStoricoParticellaVOByParametersImpUnar method in StoricoParticellaDAO: "+particella+"\n");
      SolmrLogger.debug(this, "Value of parameter 5 [SUBALTERNO] in getListStoricoParticellaVOByParametersImpUnar method in StoricoParticellaDAO: "+subalterno+"\n");
      SolmrLogger.debug(this, "Value of parameter 6 [ID_AZIENDA] in getListStoricoParticellaVOByParametersImpUnar method in StoricoParticellaDAO: "+idAzienda+"\n");

      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      stmt.setLong(++indice, idAzienda);
      
      if(Validator.isNotEmpty(istatComune)) {
        stmt.setString(++indice, istatComune);
      }
      
      if(Validator.isNotEmpty(foglio)) {
        stmt.setString(++indice, foglio);
      }
      if(Validator.isNotEmpty(particella)) {
        stmt.setString(++indice, particella);
      }
      if(Validator.isNotEmpty(sezione)) {
        stmt.setString(++indice, sezione.toUpperCase());
      }
      if(Validator.isNotEmpty(subalterno)) {
        stmt.setString(++indice, subalterno.toUpperCase());
      }
      
      SolmrLogger.debug(this, "Executing getListStoricoParticellaVOByParametersImpUnar: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO(); 
        storicoParticellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
        storicoParticellaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
        storicoParticellaVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        storicoParticellaVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
        ComuneVO comuneVO = new ComuneVO();
        comuneVO.setIstatComune(rs.getString("COMUNE"));
        comuneVO.setDescom(rs.getString("DESCOM"));
        if(Validator.isNotEmpty(rs.getString("SIGLA_PROVINCIA"))) {
          ProvinciaVO provinciaVO = new ProvinciaVO();
          provinciaVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));
          comuneVO.setProvinciaVO(provinciaVO);
        }
        storicoParticellaVO.setComuneParticellaVO(comuneVO);
        storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
        if(Validator.isNotEmpty(rs.getString("SEZIONE"))) {
          SezioneVO sezioneVO = new SezioneVO();
          sezioneVO.setIstatComune(rs.getString("ISTAT_COMUNE"));
          sezioneVO.setSezione(rs.getString("SEZIONE"));
          sezioneVO.setDescrizione(rs.getString("DESC_SEZIONE"));
          storicoParticellaVO.setSezioneVO(sezioneVO);
        }
        storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
        storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
        storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
        storicoParticellaVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
        storicoParticellaVO.setSuperficieGrafica(rs.getString("SUPERFICIE_GRAFICA"));
        if(Validator.isNotEmpty(rs.getString("ID_ZONA_ALTIMETRICA"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_ZONA_ALTIMETRICA")), rs.getString("DESC_ALTIMETRICA"));
          storicoParticellaVO.setIdZonaAltimetrica(Long.decode(rs.getString("ID_ZONA_ALTIMETRICA")));
          storicoParticellaVO.setZonaAltimetrica(code);
        }
        if(Validator.isNotEmpty(rs.getString("ID_AREA_A"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_A")), rs.getString("DESC_AREA_A"));
          storicoParticellaVO.setIdAreaA(new Long(rs.getString("ID_AREA_A")));
          storicoParticellaVO.setAreaA(code);
        }
        if(Validator.isNotEmpty(rs.getString("ID_AREA_B"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_B")), rs.getString("DESC_AREA_B"));
          storicoParticellaVO.setIdAreaB(new Long(rs.getString("ID_AREA_B")));
          storicoParticellaVO.setAreaB(code);
        }
        if(Validator.isNotEmpty(rs.getString("ID_AREA_C"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_C")), rs.getString("DESC_AREA_C"));
          storicoParticellaVO.setIdAreaC(new Long(rs.getString("ID_AREA_C")));
          storicoParticellaVO.setAreaC(code);
        }
        if(Validator.isNotEmpty(rs.getString("ID_AREA_D"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_D")), rs.getString("DESC_AREA_D"));
          storicoParticellaVO.setIdAreaD(new Long(rs.getString("ID_AREA_D")));
          storicoParticellaVO.setAreaD(code);
        }
        storicoParticellaVO.setFlagCaptazionePozzi(rs.getString("FLAG_CAPTAZIONE_POZZI"));
        storicoParticellaVO.setFlagIrrigabile(rs.getString("FLAG_IRRIGABILE"));
        if(Validator.isNotEmpty(rs.getString("ID_CASO_PARTICOLARE"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_CASO_PARTICOLARE")), rs.getString("DESC_CASO_PART"));
          storicoParticellaVO.setIdCasoParticolare(new Long(rs.getString("ID_CASO_PARTICOLARE")));
          storicoParticellaVO.setCasoParticolare(code);
        }
        storicoParticellaVO.setMotivoModifica(rs.getString("MOTIVO_MODIFICA"));
        storicoParticellaVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
        storicoParticellaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        UtenteIrideVO utenteIrideVO = new UtenteIrideVO();
        utenteIrideVO.setIdUtente(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        utenteIrideVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        utenteIrideVO.setDescrizioneEnteAppartenenza(rs.getString("DESCRIZIONE_ENTE_APPARTENENZA"));
        storicoParticellaVO.setUtenteAggiornamento(utenteIrideVO);
        if(Validator.isNotEmpty(rs.getString("ID_CAUSALE_MOD_PARTICELLA"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_CAUSALE_MOD_PARTICELLA")), rs.getString("DESC_CAUSALE_MOD"));
          storicoParticellaVO.setIdCausaleModParticella(new Long(rs.getString("ID_CAUSALE_MOD_PARTICELLA")));
          storicoParticellaVO.setCausaleModParticella(code);
        }
        storicoParticellaVO.setSupNonEleggibile(rs.getString("SUP_NON_ELEGGIBILE"));
        storicoParticellaVO.setSupNeBoscoAcqueFabbricato(rs.getString("SUP_NE_BOSCO_ACQUE_FABBRICATO"));
        storicoParticellaVO.setSupNeForaggere(rs.getString("SUP_NE_FORAGGIERE"));
        storicoParticellaVO.setSupElFruttaGuscio(rs.getString("SUP_EL_FRUTTA_GUSCIO"));
        storicoParticellaVO.setSupElPratoPascolo(rs.getString("SUP_EL_PRATO_PASCOLO"));
        storicoParticellaVO.setSupElColtureMiste(rs.getString("SUP_EL_COLTURE_MISTE"));
        storicoParticellaVO.setSupColtivazArboreaCons(rs.getString("SUP_COLTIVAZ_ARBOREA_CONS"));
        storicoParticellaVO.setSupColtivazArboreaSpec(rs.getString("SUP_COLTIVAZ_ARBOREA_SPEC"));
        storicoParticellaVO.setDataFoto(rs.getDate("DATA_FOTO"));
        storicoParticellaVO.setTipoFoto(rs.getString("TIPO_FOTO"));
        if(Validator.isNotEmpty(rs.getString("ID_FONTE"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_FONTE")), rs.getString("DESC_FONTE"));
          storicoParticellaVO.setIdFonte(new Long(rs.getString("ID_FONTE")));
          storicoParticellaVO.setFonte(code);
        }
        if(Validator.isNotEmpty(rs.getString("ID_DOCUMENTO"))) {
          storicoParticellaVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
        }
        storicoParticellaVO.setDataCessazione(rs.getDate("DATA_CESSAZIONE"));
        if(Validator.isNotEmpty(rs.getString("ID_DOCUMENTO_PROTOCOLLATO"))) {
          storicoParticellaVO.setIdDocumentoProtocollato(new Long(rs.getString("ID_DOCUMENTO_PROTOCOLLATO")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_IRRIGAZIONE"))) {
          storicoParticellaVO.setIdIrrigazione(new Long(rs.getLong("ID_IRRIGAZIONE")));
          TipoIrrigazioneVO tipoIrrigazioneVO = new TipoIrrigazioneVO();
          tipoIrrigazioneVO.setIdIrrigazione(new Long(rs.getLong("ID_IRRIGAZIONE")));
          tipoIrrigazioneVO.setDescrizione(rs.getString("DESC_IRRIGAZIONE"));
          tipoIrrigazioneVO.setDataInizioValidita(rs.getDate("DATA_INI_IRRIGAZ"));
          tipoIrrigazioneVO.setDataFineValidita(rs.getDate("DATA_FINE_IRRIGAZ"));
          storicoParticellaVO.setTipoIrrigazioneVO(tipoIrrigazioneVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_POTENZIALITA_IRRIGUA"))) {
          storicoParticellaVO.setIdPotenzialitaIrrigua(new Long(rs.getLong("ID_POTENZIALITA_IRRIGUA")));
          TipoPotenzialitaIrriguaVO tipoPotenzialitaIrriguaVO = new TipoPotenzialitaIrriguaVO();
          tipoPotenzialitaIrriguaVO.setIdPotenzialitaIrrigua(new Long(rs.getLong("ID_POTENZIALITA_IRRIGUA")));
          tipoPotenzialitaIrriguaVO.setDescrizione(rs.getString("DESC_POTENZIALITA_IRRIGUA"));
          tipoPotenzialitaIrriguaVO.setCodice(rs.getString("COD_POTENZIALITA_IRRIGUA"));
          storicoParticellaVO.setTipoPotenzialitaIrriguaVO(tipoPotenzialitaIrriguaVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_TERRAZZAMENTO"))) {
          storicoParticellaVO.setIdTerrazzamento(new Long(rs.getLong("ID_TERRAZZAMENTO")));
          TipoTerrazzamentoVO tipoTerrazzamentoVO = new TipoTerrazzamentoVO();
          tipoTerrazzamentoVO.setIdTerrazzamento(new Long(rs.getLong("ID_TERRAZZAMENTO")));
          tipoTerrazzamentoVO.setDescrizione(rs.getString("DESC_TERRAZZAMENTO"));
          tipoTerrazzamentoVO.setCodice(rs.getString("COD_TERRAZZAMENTO"));
          storicoParticellaVO.setTipoTerrazzamentoVO(tipoTerrazzamentoVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_ROTAZIONE_COLTURALE"))) {
          storicoParticellaVO.setIdRotazioneColturale(new Long(rs.getLong("ID_ROTAZIONE_COLTURALE")));
          TipoRotazioneColturaleVO tipoRotazioneColturaleVO = new TipoRotazioneColturaleVO();
          tipoRotazioneColturaleVO.setIdRotazioneColturale(new Long(rs.getLong("ID_ROTAZIONE_COLTURALE")));
          tipoRotazioneColturaleVO.setDescrizione(rs.getString("DESC_ROTAZIONE_COLTURALE"));
          tipoRotazioneColturaleVO.setCodice(rs.getString("COD_ROTAZIONE_COLTURALE"));
          storicoParticellaVO.setTipoRotazioneColturaleVO(tipoRotazioneColturaleVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_FASCIA_FLUVIALE"))) {
          storicoParticellaVO.setIdFasciaFluviale(new Long(rs.getLong("ID_FASCIA_FLUVIALE")));
          CodeDescription fasciaFluviale = new CodeDescription(Integer.decode(rs.getString("ID_FASCIA_FLUVIALE")), rs.getString("DESC_FLUVIALE"));
          storicoParticellaVO.setFasciaFluviale(fasciaFluviale);
        }
        
        
        elencoStorico.add(storicoParticellaVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) 
    {
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getListStoricoParticellaVOByParametersImpUnar in StoricoParticellaDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getListStoricoParticellaVOByParametersImpUnar in StoricoParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getListStoricoParticellaVOByParametersImpUnar in StoricoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListStoricoParticellaVOByParametersImpUnar method in StoricoParticellaDAO\n");
    if(elencoStorico.size() == 0) {
      return (StoricoParticellaVO[])elencoStorico.toArray(new StoricoParticellaVO[0]);
    }
    else {
      return (StoricoParticellaVO[])elencoStorico.toArray(new StoricoParticellaVO[elencoStorico.size()]);
    }
  }
	
	/**
	 * Metodo per recuperare l'elenco delle particelle: se il boolean è true le estraggo solo in relazione ai filtri della chiave
	 * logica della particella, in caso contrario estraggo solo le particelle la cui conduzione non risulti essere legata già ai documenti
	 * inseriti sull'azienda selezionata
	 * 
	 * @param storicoParticellaVO
	 * @param anagAziendaVO
	 * @param hasUnitToDocument
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
	 * @throws DataAccessException
	 */
	public StoricoParticellaVO[] getListParticelleForDocument(StoricoParticellaVO storicoParticellaVO, 
	    AnagAziendaVO anagAziendaVO, boolean hasUnitToDocument, Long idAnomalia, String orderBy) 
	throws DataAccessException 
	{
	  SolmrLogger.debug(this, "Invocating getListParticelleForDocument method in StoricoParticellaDAO\n");
	  Connection conn = null;
	  PreparedStatement stmt = null;
	  Vector<StoricoParticellaVO> elencoParticelle = null;

	  // Creo l'oggetto Stop Watch per monitorare le operazioni eseguite all'interno del metodo
	  StopWatch watcher = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);

	  try 
	  {

	  	SolmrLogger.debug(this, "Creating db-connection in getListParticelleForDocument method in StoricoParticellaDAO\n");
	   	conn = getDatasource().getConnection();
	   	SolmrLogger.debug(this, "Created db-connection in getListParticelleForDocument method in StoricoParticellaDAO and it values: "+conn+"\n");

	   	// START
	   	watcher.start();

	   	String query = 
	   	  " SELECT SP.ID_STORICO_PARTICELLA, " +
	   	  "        SP.ID_PARTICELLA, " +
	      "        SP.COMUNE, " +
	      "        P.SIGLA_PROVINCIA, " +
	      "        C.DESCOM, " +
	      "        SP.SEZIONE, " +
	      "        SP.FOGLIO, " +
	      "        SP.PARTICELLA, " +
	      "        SP.SUBALTERNO, " +
	      "        SP.SUP_CATASTALE, " +
	      "        SP.ID_CASO_PARTICOLARE, " +
	      "        CP.SUPERFICIE_CONDOTTA, " +
	      "        TTP.DESCRIZIONE, " +
	      "        CP.DATA_INIZIO_CONDUZIONE, " +
	      "        CP.ID_CONDUZIONE_PARTICELLA, " +
	      "        CP.DATA_FINE_CONDUZIONE, " +
	      "        CP.ESITO_CONTROLLO, " +
	      "        CP.ID_TITOLO_POSSESSO, " +
	      "        PC.ID_PARTICELLA_CERTIFICATA " +
        " FROM   PROVINCIA P, " +
        "        COMUNE C, " +
        "        DB_STORICO_PARTICELLA SP, " +
        "        DB_CONDUZIONE_PARTICELLA CP, " +
        "        DB_TIPO_TITOLO_POSSESSO TTP, " +
        "        DB_UTE U, " +
        "        DB_PARTICELLA_CERTIFICATA PC ";
	    	
	    if(Validator.isNotEmpty(idAnomalia))
	    {
	      query +=
	      ",DB_DICHIARAZIONE_SEGNALAZIONE DS ";
	    }
	    
      query +=
        " WHERE  SP.COMUNE = C.ISTAT_COMUNE " +
        " AND    SP.DATA_FINE_VALIDITA IS NULL " +
        " AND    U.ID_AZIENDA = ? " +
        " AND    U.ID_UTE = CP.ID_UTE " +
        " AND    U.DATA_FINE_ATTIVITA IS NULL " +
        " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
        " AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
        " AND    TTP.ID_TITOLO_POSSESSO = CP.ID_TITOLO_POSSESSO " +
        " AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        " AND    SP.ID_PARTICELLA =  PC.ID_PARTICELLA(+) " +
        " AND    PC.DATA_FINE_VALIDITA(+) IS NULL ";

    	if(Validator.isNotEmpty(storicoParticellaVO.getIstatComune())) {
    		query += 
    		" AND SP.COMUNE = ? ";
    	}
    	if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) {
    		query += 
    		" AND SP.SEZIONE = ? ";
    	}
    	if(Validator.isNotEmpty(storicoParticellaVO.getFoglio())) {
    		query += 
    		" AND SP.FOGLIO = ? ";
    	}
    	if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) {
    		query += 
    		" AND SP.PARTICELLA = ? ";
    	}
    	if(Validator.isNotEmpty(storicoParticellaVO.getIdCasoParticolare())) {
    		query += 
    		" AND SP.ID_CASO_PARTICOLARE = ? ";
    	}
    	if(Validator.isNotEmpty(storicoParticellaVO.getElencoConduzioni()[0].getIdTitoloPossesso())) {
    		query += 
    		" AND CP.ID_TITOLO_POSSESSO = ? ";
    	}

    	// Se il boolean hasUnitToDocument == false estraggo solo le particelle le cui conduzioni non risultino già legate a documenti
    	// relativi all'azienda
    	if(!hasUnitToDocument) {
    		query += 
    		" AND     NOT EXISTS " +
        " (SELECT 1 " +
	      "  FROM   DB_DOCUMENTO_CONDUZIONE DC, " +
	      "         DB_DOCUMENTO D " +
	      "  WHERE  D.CUAA = ? " +
	      "  AND    D.ID_AZIENDA = ? " +
	      "  AND    D.ID_STATO_DOCUMENTO IS NULL " +
	      "  AND    NVL(D.DATA_FINE_VALIDITA, ?) > SYSDATE " +
	      "  AND    D.ID_DOCUMENTO = DC.ID_DOCUMENTO " +
	      "  AND    DC.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA " +
	      " ) ";
    	}
    	
    	if(Validator.isNotEmpty(idAnomalia))
      {
        query +=
        "AND  SP.ID_STORICO_PARTICELLA = DS.ID_STORICO_PARTICELLA " +
        "AND  DS.ID_DICHIARAZIONE_CONSISTENZA IS NULL " +
        "AND  DS.ID_CONTROLLO = ? " +
        "AND  DS.ID_AZIENDA = U.ID_AZIENDA ";
      }
    	
    	if(Validator.isNotEmpty(orderBy)) {
    		query += "ORDER BY "+orderBy;
    	}
    	stmt = conn.prepareStatement(query);

    	int indice = 0;

    	SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListParticelleForDocument method in StoricoParticellaDAO: "+anagAziendaVO.getIdAzienda().longValue()+"\n");
    	stmt.setLong(++indice, anagAziendaVO.getIdAzienda().longValue());

    	if(Validator.isNotEmpty(storicoParticellaVO.getIstatComune())) {
    		stmt.setString(++indice, storicoParticellaVO.getIstatComune());
    		SolmrLogger.debug(this, "Value of parameter" + indice + " [ISTAT_COMUNE] in getListParticelleForDocument method in StoricoParticellaDAO: "+storicoParticellaVO.getIstatComune()+"\n");
    	}
    	if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) {
    		stmt.setString(++indice, storicoParticellaVO.getSezione().toUpperCase());
    		SolmrLogger.debug(this, "Value of parameter" + indice + " [SEZIONE] in getListParticelleForDocument method in StoricoParticellaDAO: "+storicoParticellaVO.getSezione()+"\n");
    	}
	    if(Validator.isNotEmpty(storicoParticellaVO.getFoglio())) {
	    	stmt.setString(++indice, storicoParticellaVO.getFoglio());
	    	SolmrLogger.debug(this, "Value of parameter" + indice + " [FOGLIO] in getListParticelleForDocument method in StoricoParticellaDAO: "+storicoParticellaVO.getFoglio()+"\n");
	    }
	    if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) {
	    	stmt.setString(++indice, storicoParticellaVO.getParticella());
	    	SolmrLogger.debug(this, "Value of parameter" + indice + " [PARTICELLA] in getListParticelleForDocument method in StoricoParticellaDAO: "+storicoParticellaVO.getParticella()+"\n");
	    }
	    if(Validator.isNotEmpty(storicoParticellaVO.getIdCasoParticolare())) {
	    	stmt.setLong(++indice, storicoParticellaVO.getIdCasoParticolare().longValue());
	    	SolmrLogger.debug(this, "Value of parameter" + indice + " [ID_CASO_PARTICOLARE] in getListParticelleForDocument method in StoricoParticellaDAO: "+storicoParticellaVO.getIdCasoParticolare().longValue()+"\n");
	    }
	    if(storicoParticellaVO.getElencoConduzioni() != null && storicoParticellaVO.getElencoConduzioni()[0] != null && storicoParticellaVO.getElencoConduzioni()[0].getIdTitoloPossesso() != null) {
	    	stmt.setLong(++indice, storicoParticellaVO.getElencoConduzioni()[0].getIdTitoloPossesso().longValue());
	    	SolmrLogger.debug(this, "Value of parameter" + indice + " [ID_TITOLO_POSSESSO] in getListParticelleForDocument method in StoricoParticellaDAO: "+storicoParticellaVO.getElencoConduzioni()[0].getIdTitoloPossesso().longValue()+"\n");
	    }
	    if(!hasUnitToDocument) {
	    	stmt.setString(++indice, anagAziendaVO.getCUAA().toUpperCase());
	    	SolmrLogger.debug(this, "Value of parameter" + indice + " [CUAA] in getListParticelleForDocument method in StoricoParticellaDAO: "+anagAziendaVO.getCUAA()+"\n");
	    	stmt.setLong(++indice, anagAziendaVO.getIdAzienda().longValue());
	    	SolmrLogger.debug(this, "Value of parameter" + indice + " [ID_AZIENDA] in getListParticelleForDocument method in StoricoParticellaDAO: "+anagAziendaVO.getIdAzienda()+"\n");
	    	stmt.setDate(++indice, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
	    	SolmrLogger.debug(this, "Value of parameter" + indice + " [DATA_FINE_VALIDITA] in getListParticelleForDocument method in StoricoParticellaDAO: "+new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime())+"\n");
	    }
	    
	    if(Validator.isNotEmpty(idAnomalia))
      {
	      stmt.setLong(++indice, idAnomalia.longValue());
        SolmrLogger.debug(this, "Value of parameter" + indice + " [ID_CONTROLLO] in getListParticelleForDocument method in StoricoParticellaDAO: "+idAnomalia.longValue()+"\n");
      }

	    SolmrLogger.debug(this, "Executing getListParticelleForDocument in StoricoParticellaDAO: "+query+"\n");

	    ResultSet rs = stmt.executeQuery();

	    // Primo monitoraggio
	    watcher.dumpElapsed("StoricoParticellaDAO", "getListParticelleForDocument", "In getListParticelleForDocument method from the creation of query to the execution", "List of parameters: storicoParticellaVO: "+storicoParticellaVO.toString()+" anagAziendaVO: "+anagAziendaVO.toString()+" hasUnitToDocument: "+hasUnitToDocument);

	    elencoParticelle = new Vector<StoricoParticellaVO>();

	    while(rs.next()) {
	    	StoricoParticellaVO storicoParticellaElencoVO = new StoricoParticellaVO();
	    	ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
	    	storicoParticellaElencoVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
	    	storicoParticellaElencoVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
	    	storicoParticellaElencoVO.setIstatComune(rs.getString("COMUNE"));
	    	ComuneVO comuneVO = new ComuneVO();
	    	comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
	    	comuneVO.setDescom(rs.getString("DESCOM"));
	    	storicoParticellaElencoVO.setComuneParticellaVO(comuneVO);
	    	storicoParticellaElencoVO.setSezione(rs.getString("SEZIONE"));
	    	storicoParticellaElencoVO.setFoglio(rs.getString("FOGLIO"));
	    	storicoParticellaElencoVO.setParticella(rs.getString("PARTICELLA"));
	    	storicoParticellaElencoVO.setSubalterno(rs.getString("SUBALTERNO"));
	    	storicoParticellaElencoVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
	    	if(Validator.isNotEmpty(rs.getString("ID_CASO_PARTICOLARE"))) {
	    		storicoParticellaElencoVO.setIdCasoParticolare(Long.decode(rs.getString("ID_CASO_PARTICOLARE")));
	    	}
	    	conduzioneParticellaVO.setSuperficieCondotta(rs.getString("SUPERFICIE_CONDOTTA"));
	    	CodeDescription titoloPossesso = new CodeDescription(Integer.decode(rs.getString("ID_TITOLO_POSSESSO")), rs.getString("DESCRIZIONE"));
	    	conduzioneParticellaVO.setTitoloPossesso(titoloPossesso);
	    	conduzioneParticellaVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
	    	conduzioneParticellaVO.setDataInizioConduzione(rs.getDate("DATA_INIZIO_CONDUZIONE"));
	    	conduzioneParticellaVO.setDataFineConduzione(rs.getDate("DATA_FINE_CONDUZIONE"));
	    	conduzioneParticellaVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
	    	conduzioneParticellaVO.setIdConduzioneParticella(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));
	    	storicoParticellaElencoVO.setElencoConduzioni((ConduzioneParticellaVO[])new ConduzioneParticellaVO[]{conduzioneParticellaVO});
	    	
	    	if(Validator.isNotEmpty(rs.getString("ID_PARTICELLA_CERTIFICATA"))) 
        {
          ParticellaCertificataVO particellaCertificataVO = new ParticellaCertificataVO();
          particellaCertificataVO.setIdParticellaCertificata(new Long(rs.getLong("ID_PARTICELLA_CERTIFICATA")));
          storicoParticellaElencoVO.setParticellaCertificataVO(particellaCertificataVO);
        }
	    	
	    	
	    	elencoParticelle.add(storicoParticellaElencoVO);
	    }	

	    // Secondo monitoraggio
	    watcher.dumpElapsed("StoricoParticellaDAO", "getListParticelleForDocument", "In getListParticelleForDocument method from the creation of query to final setting of parameters inside of VO after resultset's while cicle", "List of parameters: particellaVO: "+storicoParticellaVO.toString()+" anagAziendaVO: "+anagAziendaVO.toString()+" hasUnitToDocument: "+hasUnitToDocument);
      
	    rs.close();
	    stmt.close();	      

	    // STOP
	    watcher.stop();
    }
    catch(SQLException exc) {
    	SolmrLogger.error(this, "getListParticelleForDocument - SQLException: "+exc.getMessage()+"\n");
    	SolmrLogger.debug(this, "Value of error code in method getListParticelleForDocument - SQLException: "+exc.getErrorCode()+"\n");
    	throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
    	SolmrLogger.error(this, "getListParticelleForDocument - Generic Exception: "+ex+"\n");
    	throw new DataAccessException(ex.getMessage());
    }
	  finally 
	  {
	  	try 
	  	{
	    	if(stmt != null) 
	    	{
	      	stmt.close();
	      }
	      if(conn != null) 
	      {
	      	conn.close();
	      }
	    }
	    catch(SQLException exc) 
	    {
    		SolmrLogger.error(this, "getListParticelleForDocument - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
    		SolmrLogger.debug(this, "Value of error code in method getListParticelleForDocument - SQLException: "+exc.getErrorCode()+"\n");
    		throw new DataAccessException(exc.getMessage());
    	}
	    catch(Exception ex) 
	    {
	    	SolmrLogger.error(this, "getListParticelleForDocument - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
	    	throw new DataAccessException(ex.getMessage());
	    }
	  }
	  SolmrLogger.debug(this, "Invocated getListParticelleForDocument method in StoricoParticellaDAO\n");
	  if(elencoParticelle.size() == 0) 
	  {
			return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
		}
		else 
		{
			return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
		}
	}
	
	
	/**
   * Metodo per recuperare l'elenco delle particelle: se il boolean è true le estraggo solo in relazione ai filtri della chiave
   * logica della particella, in caso contrario estraggo solo le particelle la cui conduzione non risulti essere legata già ai documenti
   * inseriti sull'azienda selezionata
   * 
   * @param storicoParticellaVO
   * @param anagAziendaVO
   * @param hasUnitToDocument
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws DataAccessException
   */
  public StoricoParticellaVO[] getListParticelleForDocument(StoricoParticellaVO storicoParticellaVO, 
      AnagAziendaVO anagAziendaVO, ConsistenzaVO consistenzaVO, boolean hasUnitToDocument, 
      Long idAnomalia, String orderBy) 
  throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getListParticelleForDocument method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelle = null;

    // Creo l'oggetto Stop Watch per monitorare le operazioni eseguite all'interno del metodo
    StopWatch watcher = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);

    try 
    {

      SolmrLogger.debug(this, "Creating db-connection in getListParticelleForDocument method in StoricoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListParticelleForDocument method in StoricoParticellaDAO and it values: "+conn+"\n");

      // START
      watcher.start();

      String query = 
        "SELECT SP.ID_STORICO_PARTICELLA," +
        "       SP.ID_PARTICELLA, " +
        "       SP.COMUNE,  " +
        "       P.SIGLA_PROVINCIA, " +
        "       C.DESCOM, " +
        "       SP.SEZIONE, " +
        "       SP.FOGLIO, " +
        "       SP.PARTICELLA,  "+
        "       SP.SUBALTERNO, " +
        "       SP.SUP_CATASTALE,  " +
        "       SP.ID_CASO_PARTICOLARE, " +
        "       CD.SUPERFICIE_CONDOTTA,  " +
        "       TTP.DESCRIZIONE, "+
        "       CD.DATA_INIZIO_CONDUZIONE, " +
        "       CD.ID_CONDUZIONE_PARTICELLA, " +
        "       CD.DATA_FINE_CONDUZIONE, " +
        "       CD.ESITO_CONTROLLO, "+
        "       CD.ID_TITOLO_POSSESSO, " +
        "       PC.ID_PARTICELLA_CERTIFICATA "+
        "FROM   PROVINCIA P, " +
        "       COMUNE C, " +
        "       DB_STORICO_PARTICELLA SP, " +
        "       DB_CONDUZIONE_DICHIARATA CD, " +
        "       DB_TIPO_TITOLO_POSSESSO TTP, "+
        "       DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "       DB_PARTICELLA_CERTIFICATA PC ";
      if(Validator.isNotEmpty(idAnomalia))
      {
        query +=
        ",DB_DICHIARAZIONE_SEGNALAZIONE DS ";
      }
      
      query +=
        "WHERE  SP.COMUNE = C.ISTAT_COMUNE "+
        "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA "+
        "AND    CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA "+
        "AND    TTP.ID_TITOLO_POSSESSO = CD.ID_TITOLO_POSSESSO "+
        "AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? "+
        "AND    DC.CODICE_FOTOGRAFIA_TERRENI=CD.CODICE_FOTOGRAFIA_TERRENI " +
        "AND    SP.ID_PARTICELLA =  PC.ID_PARTICELLA(+) " +
        "AND    PC.DATA_INIZIO_VALIDITA(+) <= ? " +
        "AND    TRUNC(NVL(PC.DATA_FINE_VALIDITA(+), TO_DATE('31/12/9999', 'DD/MM/YYYY'))) > ? ";

      if(Validator.isNotEmpty(storicoParticellaVO.getIstatComune())) {
        query += " AND SP.COMUNE = ? ";
      }
      if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) {
        query += " AND SP.SEZIONE = ? ";
      }
      if(Validator.isNotEmpty(storicoParticellaVO.getFoglio())) {
        query += " AND SP.FOGLIO = ? ";
      }
      if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) {
        query += " AND SP.PARTICELLA = ? ";
      }
      if(Validator.isNotEmpty(storicoParticellaVO.getIdCasoParticolare())) {
        query += " AND SP.ID_CASO_PARTICOLARE = ? ";
      }
      if(Validator.isNotEmpty(storicoParticellaVO.getElencoConduzioni()[0].getIdTitoloPossesso())) {
        query += " AND CD.ID_TITOLO_POSSESSO = ? ";
      }

      // Se il boolean hasUnitToDocument == false estraggo solo le particelle le cui conduzioni non risultino già legate a documenti
      // relativi all'azienda
      if(!hasUnitToDocument) {
        query += 
        " AND     NOT EXISTS " +
        " (SELECT 1 " +
        "  FROM   DB_DOCUMENTO_CONDUZIONE DC, " +
        "         DB_DOCUMENTO D " +
        "  WHERE  D.CUAA = ? " +
        "  AND    D.ID_AZIENDA = ? " +
        "  AND    D.ID_STATO_DOCUMENTO IS NULL " +
        "  AND    NVL(D.DATA_FINE_VALIDITA, ?) > SYSDATE " +
        "  AND    D.ID_DOCUMENTO = DC.ID_DOCUMENTO " +
        "  AND    DC.ID_CONDUZIONE_PARTICELLA = CD.ID_CONDUZIONE_PARTICELLA " +
        " ) ";
      }
      
      if(Validator.isNotEmpty(idAnomalia))
      {
        query +=
        "AND  SP.ID_STORICO_PARTICELLA = DS.ID_STORICO_PARTICELLA " +
        "AND  DS.ID_DICHIARAZIONE_CONSISTENZA = DC.ID_DICHIARAZIONE_CONSISTENZA " +
        "AND  DS.ID_CONTROLLO = ? " +
        "AND  DS.ID_AZIENDA = ? ";
      }
      
      if(Validator.isNotEmpty(orderBy)) {
        query += "ORDER BY "+orderBy;
      }
      stmt = conn.prepareStatement(query);

      int indice = 0;

      SolmrLogger.debug(this, "Value of parameter 1 [ID_DICHIARAZIONE_CONSISTENZA] in getListParticelleForDocument method in StoricoParticellaDAO: "+consistenzaVO.getIdDichiarazioneConsistenza()+"\n");
      stmt.setLong(++indice, new Long(consistenzaVO.getIdDichiarazioneConsistenza()).longValue());
      
      stmt.setTimestamp(++indice, convertDateToTimestamp(consistenzaVO.getDataInserimentoDichiarazione()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(consistenzaVO.getDataInserimentoDichiarazione()));

      if(Validator.isNotEmpty(storicoParticellaVO.getIstatComune())) {
        stmt.setString(++indice, storicoParticellaVO.getIstatComune());
        SolmrLogger.debug(this, "Value of parameter" + indice + " [ISTAT_COMUNE] in getListParticelleForDocument method in StoricoParticellaDAO: "+storicoParticellaVO.getIstatComune()+"\n");
      }
      if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) {
        stmt.setString(++indice, storicoParticellaVO.getSezione().toUpperCase());
        SolmrLogger.debug(this, "Value of parameter" + indice + " [SEZIONE] in getListParticelleForDocument method in StoricoParticellaDAO: "+storicoParticellaVO.getSezione()+"\n");
      }
      if(Validator.isNotEmpty(storicoParticellaVO.getFoglio())) {
        stmt.setString(++indice, storicoParticellaVO.getFoglio());
        SolmrLogger.debug(this, "Value of parameter" + indice + " [FOGLIO] in getListParticelleForDocument method in StoricoParticellaDAO: "+storicoParticellaVO.getFoglio()+"\n");
      }
      if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) {
        stmt.setString(++indice, storicoParticellaVO.getParticella());
        SolmrLogger.debug(this, "Value of parameter" + indice + " [PARTICELLA] in getListParticelleForDocument method in StoricoParticellaDAO: "+storicoParticellaVO.getParticella()+"\n");
      }
      if(Validator.isNotEmpty(storicoParticellaVO.getIdCasoParticolare())) {
        stmt.setLong(++indice, storicoParticellaVO.getIdCasoParticolare().longValue());
        SolmrLogger.debug(this, "Value of parameter" + indice + " [ID_CASO_PARTICOLARE] in getListParticelleForDocument method in StoricoParticellaDAO: "+storicoParticellaVO.getIdCasoParticolare().longValue()+"\n");
      }
      if(storicoParticellaVO.getElencoConduzioni() != null && storicoParticellaVO.getElencoConduzioni()[0] != null && storicoParticellaVO.getElencoConduzioni()[0].getIdTitoloPossesso() != null) {
        stmt.setLong(++indice, storicoParticellaVO.getElencoConduzioni()[0].getIdTitoloPossesso().longValue());
        SolmrLogger.debug(this, "Value of parameter" + indice + " [ID_TITOLO_POSSESSO] in getListParticelleForDocument method in StoricoParticellaDAO: "+storicoParticellaVO.getElencoConduzioni()[0].getIdTitoloPossesso().longValue()+"\n");
      }
      if(!hasUnitToDocument) {
        stmt.setString(++indice, anagAziendaVO.getCUAA().toUpperCase());
        SolmrLogger.debug(this, "Value of parameter" + indice + " [CUAA] in getListParticelleForDocument method in StoricoParticellaDAO: "+anagAziendaVO.getCUAA()+"\n");
        stmt.setLong(++indice, anagAziendaVO.getIdAzienda().longValue());
        SolmrLogger.debug(this, "Value of parameter" + indice + " [ID_AZIENDA] in getListParticelleForDocument method in StoricoParticellaDAO: "+anagAziendaVO.getIdAzienda()+"\n");
        stmt.setDate(++indice, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
        SolmrLogger.debug(this, "Value of parameter" + indice + " [DATA_FINE_VALIDITA] in getListParticelleForDocument method in StoricoParticellaDAO: "+new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime())+"\n");
      }
      
      if(Validator.isNotEmpty(idAnomalia))
      {
        stmt.setLong(++indice, idAnomalia.longValue());
        SolmrLogger.debug(this, "Value of parameter" + indice + " [ID_CONTROLLO] in getListParticelleForDocument method in StoricoParticellaDAO: "+idAnomalia.longValue()+"\n");
        stmt.setLong(++indice, anagAziendaVO.getIdAzienda().longValue());
      }

      SolmrLogger.debug(this, "Executing getListParticelleForDocument in StoricoParticellaDAO: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      // Primo monitoraggio
      watcher.dumpElapsed("StoricoParticellaDAO", "getListParticelleForDocument", "In getListParticelleForDocument method from the creation of query to the execution", "List of parameters: storicoParticellaVO: "+storicoParticellaVO.toString()+" anagAziendaVO: "+anagAziendaVO.toString()+" hasUnitToDocument: "+hasUnitToDocument);

      elencoParticelle = new Vector<StoricoParticellaVO>();

      while(rs.next()) 
      {
        StoricoParticellaVO storicoParticellaElencoVO = new StoricoParticellaVO();
        ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
        storicoParticellaElencoVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
        storicoParticellaElencoVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
        storicoParticellaElencoVO.setIstatComune(rs.getString("COMUNE"));
        ComuneVO comuneVO = new ComuneVO();
        comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
        comuneVO.setDescom(rs.getString("DESCOM"));
        storicoParticellaElencoVO.setComuneParticellaVO(comuneVO);
        storicoParticellaElencoVO.setSezione(rs.getString("SEZIONE"));
        storicoParticellaElencoVO.setFoglio(rs.getString("FOGLIO"));
        storicoParticellaElencoVO.setParticella(rs.getString("PARTICELLA"));
        storicoParticellaElencoVO.setSubalterno(rs.getString("SUBALTERNO"));
        storicoParticellaElencoVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
        if(Validator.isNotEmpty(rs.getString("ID_CASO_PARTICOLARE"))) {
          storicoParticellaElencoVO.setIdCasoParticolare(Long.decode(rs.getString("ID_CASO_PARTICOLARE")));
        }
        conduzioneParticellaVO.setSuperficieCondotta(rs.getString("SUPERFICIE_CONDOTTA"));
        CodeDescription titoloPossesso = new CodeDescription(Integer.decode(rs.getString("ID_TITOLO_POSSESSO")), rs.getString("DESCRIZIONE"));
        conduzioneParticellaVO.setTitoloPossesso(titoloPossesso);
        conduzioneParticellaVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
        conduzioneParticellaVO.setDataInizioConduzione(rs.getDate("DATA_INIZIO_CONDUZIONE"));
        conduzioneParticellaVO.setDataFineConduzione(rs.getDate("DATA_FINE_CONDUZIONE"));
        conduzioneParticellaVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
        conduzioneParticellaVO.setIdConduzioneParticella(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));
        storicoParticellaElencoVO.setElencoConduzioni((ConduzioneParticellaVO[])new ConduzioneParticellaVO[]{conduzioneParticellaVO});
        
        
        if(Validator.isNotEmpty(rs.getString("ID_PARTICELLA_CERTIFICATA"))) 
        {
          ParticellaCertificataVO particellaCertificataVO = new ParticellaCertificataVO();
          particellaCertificataVO.setIdParticellaCertificata(new Long(rs.getLong("ID_PARTICELLA_CERTIFICATA")));
          storicoParticellaElencoVO.setParticellaCertificataVO(particellaCertificataVO);
        }
        
        
        elencoParticelle.add(storicoParticellaElencoVO);
      } 

      // Secondo monitoraggio
      watcher.dumpElapsed("StoricoParticellaDAO", "getListParticelleForDocument", "In getListParticelleForDocument method from the creation of query to final setting of parameters inside of VO after resultset's while cicle", "List of parameters: particellaVO: "+storicoParticellaVO.toString()+" anagAziendaVO: "+anagAziendaVO.toString()+" hasUnitToDocument: "+hasUnitToDocument);
      
      rs.close();
      stmt.close();       

      // STOP
      watcher.stop();
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getListParticelleForDocument - SQLException: "+exc.getMessage()+"\n");
      SolmrLogger.debug(this, "Value of error code in method getListParticelleForDocument - SQLException: "+exc.getErrorCode()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getListParticelleForDocument - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) 
        {
          stmt.close();
        }
        if(conn != null) 
        {
          conn.close();
        }
      }
      catch(SQLException exc) 
      {
        SolmrLogger.error(this, "getListParticelleForDocument - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        SolmrLogger.debug(this, "Value of error code in method getListParticelleForDocument - SQLException: "+exc.getErrorCode()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) 
      {
        SolmrLogger.error(this, "getListParticelleForDocument - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListParticelleForDocument method in StoricoParticellaDAO\n");
    if(elencoParticelle.size() == 0) 
    {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
    }
    else 
    {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
    }
  }
  
  
  
  public StoricoParticellaVO[] getListParticelleForDocumentValoreC( 
      AnagAziendaVO anagAziendaVO, int anno, boolean hasUnitToDocument) 
  throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getListParticelleForDocumentValoreC method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelle = null;

    try 
    {

      SolmrLogger.debug(this, "Creating db-connection in getListParticelleForDocumentValoreC method in StoricoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListParticelleForDocumentValoreC method in StoricoParticellaDAO and it values: "+conn+"\n");


      String query = 
        "WITH PARTICELLE AS " +
        "  (SELECT SP.ID_STORICO_PARTICELLA, " +
        "          SP.ID_PARTICELLA, " +
        "          SP.COMUNE, " +
        "          P.SIGLA_PROVINCIA, " +
        "          C.DESCOM, " +
        "          SP.SEZIONE, " +
        "          SP.FOGLIO, " +
        "          SP.PARTICELLA, " +
        "          SP.SUBALTERNO, " +
        "          SP.SUP_CATASTALE, " +
        "          SP.ID_CASO_PARTICOLARE, " +
        "          CD.SUPERFICIE_CONDOTTA, " +
        "          TTP.DESCRIZIONE, " +
        "          CD.DATA_INIZIO_CONDUZIONE, " +
        "          CD.ID_CONDUZIONE_PARTICELLA, " +
        "          CD.DATA_FINE_CONDUZIONE, " +
        "          CD.ESITO_CONTROLLO, " +
        "          CD.ID_TITOLO_POSSESSO, " +
        "          DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "   FROM   PROVINCIA P, " +
        "          COMUNE C, " +
        "          DB_STORICO_PARTICELLA SP, " +
        "          DB_CONDUZIONE_DICHIARATA CD, " +
        "          DB_TIPO_TITOLO_POSSESSO TTP, " +
        "          DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "          DB_ISTANZA_RIESAME_POTENZIALE IRP " +
        "   WHERE  IRP.ID_AZIENDA = ? " +
        "   AND    IRP.ID_PARTICELLA = SP.ID_PARTICELLA " +
        "   AND    IRP.ANNO_ISTANZA = ? " +
        "   AND    IRP.DATA_FINE_VALIDITA IS NULL " +
        "   AND    IRP.ID_ISTANZA_RIESAME IS NULL " +
        "   AND    IRP.ID_DICHIARAZIONE_CONSISTENZA = DC.ID_DICHIARAZIONE_CONSISTENZA " +
        "   AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        "   AND    CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
        "   AND    SP.COMUNE = C.ISTAT_COMUNE " +
        "   AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
        "   AND    CD.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO ";
      // Se il boolean hasUnitToDocument == false estraggo solo le particelle le cui conduzioni non risultino già legate a documenti
      // relativi all'azienda
      if(!hasUnitToDocument) {
        query += 
        " AND     NOT EXISTS " +
        " (SELECT 1 " +
        "  FROM   DB_DOCUMENTO_CONDUZIONE DC, " +
        "         DB_DOCUMENTO D " +
        "  WHERE  D.CUAA = ? " +
        "  AND    D.ID_AZIENDA = ? " +
        "  AND    D.ID_STATO_DOCUMENTO IS NULL " +
        "  AND    NVL(D.DATA_FINE_VALIDITA, ?) > SYSDATE " +
        "  AND    D.ID_DOCUMENTO = DC.ID_DOCUMENTO " +
        "  AND    DC.ID_CONDUZIONE_PARTICELLA = CD.ID_CONDUZIONE_PARTICELLA " +
        " ) ";
      }
      
      query +=
        " ) " +         
        "SELECT PA.ID_STORICO_PARTICELLA, " +
        "       PA.ID_PARTICELLA, " +
        "       PA.COMUNE, " +
        "       PA.SIGLA_PROVINCIA, " +
        "       PA.DESCOM, " +
        "       PA.SEZIONE, " +
        "       PA.FOGLIO, " +
        "       PA.PARTICELLA, " +
        "       PA.SUBALTERNO, " +
        "       PA.SUP_CATASTALE, " +
        "       PA.ID_CASO_PARTICOLARE, " +
        "       PA.SUPERFICIE_CONDOTTA, " +
        "       PA.DESCRIZIONE, " +
        "       PA.DATA_INIZIO_CONDUZIONE, " +
        "       PA.ID_CONDUZIONE_PARTICELLA, " +
        "       PA.DATA_FINE_CONDUZIONE, " +
        "       PA.ESITO_CONTROLLO, " +
        "       PA.ID_TITOLO_POSSESSO, " +
        "       PC.ID_PARTICELLA_CERTIFICATA " +
        "FROM   PARTICELLE PA, " +
        "       DB_PARTICELLA_CERTIFICATA PC " +
        "WHERE  PA.ID_PARTICELLA = PC.ID_PARTICELLA(+) " +
        "AND    PA.DATA_INSERIMENTO_DICHIARAZIONE >= PC.DATA_INIZIO_VALIDITA(+) " +
        "AND    PA.DATA_INSERIMENTO_DICHIARAZIONE < TRUNC (NVL (PC.DATA_FINE_VALIDITA(+), " +
        "                     TO_DATE ('31/12/9999', 'DD/MM/YYYY')))  " +              
        "ORDER BY PA.DESCOM, " +
        "         PA.SEZIONE, " +
        "         PA.FOGLIO, " +
        "         PA.PARTICELLA, " +
        "         PA.SUBALTERNO ";

      
      stmt = conn.prepareStatement(query);

      int indice = 0;

      stmt.setLong(++indice, anagAziendaVO.getIdAzienda().longValue());
      stmt.setInt(++indice, anno);
 
      
      if(!hasUnitToDocument) 
      {
        stmt.setString(++indice, anagAziendaVO.getCUAA().toUpperCase());
        stmt.setLong(++indice, anagAziendaVO.getIdAzienda().longValue());
        stmt.setDate(++indice, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
      }
      
      

      SolmrLogger.debug(this, "Executing getListParticelleForDocumentValoreC in StoricoParticellaDAO: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

    
      elencoParticelle = new Vector<StoricoParticellaVO>();

      while(rs.next()) 
      {
        StoricoParticellaVO storicoParticellaElencoVO = new StoricoParticellaVO();
        ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
        storicoParticellaElencoVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
        storicoParticellaElencoVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
        storicoParticellaElencoVO.setIstatComune(rs.getString("COMUNE"));
        ComuneVO comuneVO = new ComuneVO();
        comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
        comuneVO.setDescom(rs.getString("DESCOM"));
        storicoParticellaElencoVO.setComuneParticellaVO(comuneVO);
        storicoParticellaElencoVO.setSezione(rs.getString("SEZIONE"));
        storicoParticellaElencoVO.setFoglio(rs.getString("FOGLIO"));
        storicoParticellaElencoVO.setParticella(rs.getString("PARTICELLA"));
        storicoParticellaElencoVO.setSubalterno(rs.getString("SUBALTERNO"));
        storicoParticellaElencoVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
        if(Validator.isNotEmpty(rs.getString("ID_CASO_PARTICOLARE"))) {
          storicoParticellaElencoVO.setIdCasoParticolare(Long.decode(rs.getString("ID_CASO_PARTICOLARE")));
        }
        conduzioneParticellaVO.setSuperficieCondotta(rs.getString("SUPERFICIE_CONDOTTA"));
        CodeDescription titoloPossesso = new CodeDescription(Integer.decode(rs.getString("ID_TITOLO_POSSESSO")), rs.getString("DESCRIZIONE"));
        conduzioneParticellaVO.setTitoloPossesso(titoloPossesso);
        conduzioneParticellaVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
        conduzioneParticellaVO.setDataInizioConduzione(rs.getDate("DATA_INIZIO_CONDUZIONE"));
        conduzioneParticellaVO.setDataFineConduzione(rs.getDate("DATA_FINE_CONDUZIONE"));
        conduzioneParticellaVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
        conduzioneParticellaVO.setIdConduzioneParticella(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));
        storicoParticellaElencoVO.setElencoConduzioni((ConduzioneParticellaVO[])new ConduzioneParticellaVO[]{conduzioneParticellaVO});
        
        
        if(Validator.isNotEmpty(rs.getString("ID_PARTICELLA_CERTIFICATA"))) 
        {
          ParticellaCertificataVO particellaCertificataVO = new ParticellaCertificataVO();
          particellaCertificataVO.setIdParticellaCertificata(new Long(rs.getLong("ID_PARTICELLA_CERTIFICATA")));
          storicoParticellaElencoVO.setParticellaCertificataVO(particellaCertificataVO);
        }
        
        
        elencoParticelle.add(storicoParticellaElencoVO);
      } 

      
      rs.close();
      stmt.close();       

     }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getListParticelleForDocumentValoreC - SQLException: "+exc.getMessage()+"\n");
      SolmrLogger.debug(this, "Value of error code in method getListParticelleForDocumentValoreC - SQLException: "+exc.getErrorCode()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getListParticelleForDocumentValoreC - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) 
        {
          stmt.close();
        }
        if(conn != null) 
        {
          conn.close();
        }
      }
      catch(SQLException exc) 
      {
        SolmrLogger.error(this, "getListParticelleForDocumentValoreC - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        SolmrLogger.debug(this, "Value of error code in method getListParticelleForDocument - SQLException: "+exc.getErrorCode()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) 
      {
        SolmrLogger.error(this, "getListParticelleForDocumentValoreC - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListParticelleForDocumentValoreC method in StoricoParticellaDAO\n");
    if(elencoParticelle.size() == 0) 
    {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
    }
    else 
    {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
    }
  }
  
  
  public StoricoParticellaVO[] getListParticelleForDocumentExtraSistema( 
      AnagAziendaVO anagAziendaVO, int anno, boolean hasUnitToDocument) 
  throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getListParticelleForDocumentExtraSistema method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelle = null;

    try 
    {

      SolmrLogger.debug(this, "Creating db-connection in getListParticelleForDocumentExtraSistema method in StoricoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListParticelleForDocumentExtraSistema method in StoricoParticellaDAO and it values: "+conn+"\n");


      String query = 
        "SELECT SP.ID_STORICO_PARTICELLA, " +
        "       SP.ID_PARTICELLA, " +
        "       SP.COMUNE, " +
        "       P.SIGLA_PROVINCIA, " +
        "       C.DESCOM, " +
        "       SP.SEZIONE, " +
        "       SP.FOGLIO, " +
        "       SP.PARTICELLA, " +
        "       SP.SUBALTERNO, " +
        "       SP.SUP_CATASTALE, " +
        "       SP.ID_CASO_PARTICOLARE, " +
        "       CP.SUPERFICIE_CONDOTTA, " +
        "       TTP.DESCRIZIONE, " +
        "       CP.DATA_INIZIO_CONDUZIONE, " +
        "       CP.ID_CONDUZIONE_PARTICELLA, " +
        "       CP.DATA_FINE_CONDUZIONE, " +
        "       CP.ESITO_CONTROLLO, " + 
        "       CP.ID_TITOLO_POSSESSO, " +
        "       PC.ID_PARTICELLA_CERTIFICATA " +
        "FROM   DB_ISTANZA_RIESAME_POTENZIALE IRP, " + 
        "       PROVINCIA P, " +
        "       COMUNE C, " +
        "       DB_STORICO_PARTICELLA SP, " +
        "       DB_CONDUZIONE_PARTICELLA CP, " +
        "       DB_TIPO_TITOLO_POSSESSO TTP, " +
        "       DB_UTE U, " +
        "       DB_PARTICELLA_CERTIFICATA PC " +
        "WHERE  IRP.ID_AZIENDA = ? " +
        "AND    IRP.ANNO_ISTANZA = ? " +
        "AND    (IRP.ANNO_PRATICA = IRP.ANNO_ISTANZA " +
        "          OR IRP.ANNO_PRATICA IS NULL) " +
        "AND    IRP.DATA_FINE_VALIDITA IS NULL " +
        "AND    IRP.ID_ISTANZA_RIESAME IS NULL " +
        "AND    U.ID_AZIENDA = IRP.ID_AZIENDA " +
        "AND    U.ID_UTE = CP.ID_UTE " +
        "AND    U.DATA_FINE_ATTIVITA IS NULL " +
        "AND    CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO " +
        "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "AND    IRP.ID_PARTICELLA = CP.ID_PARTICELLA " +
        "AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
        "AND    SP.DATA_FINE_VALIDITA IS NULL " +
        "AND    SP.COMUNE = C.ISTAT_COMUNE " +
        "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +      
        "AND    SP.ID_PARTICELLA = PC.ID_PARTICELLA(+) " +
        "AND    PC.DATA_FINE_VALIDITA(+) IS NULL ";
      // Se il boolean hasUnitToDocument == false estraggo solo le particelle le cui conduzioni non risultino già legate a documenti
      // relativi all'azienda
      if(!hasUnitToDocument) 
      {
        query += 
        " AND     NOT EXISTS " +
        " (SELECT 1 " +
        "  FROM   DB_DOCUMENTO_CONDUZIONE DC, " +
        "         DB_DOCUMENTO D " +
        "  WHERE  D.CUAA = ? " +
        "  AND    D.ID_AZIENDA = ? " +
        "  AND    D.ID_STATO_DOCUMENTO IS NULL " +
        "  AND    NVL(D.DATA_FINE_VALIDITA, ?) > SYSDATE " +
        "  AND    D.ID_DOCUMENTO = DC.ID_DOCUMENTO " +
        "  AND    DC.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA " +
        " ) ";
      }
      
      query +=           
        "ORDER BY C.DESCOM, " +
        "         SP.SEZIONE, " +
        "         SP.FOGLIO, " +
        "         SP.PARTICELLA, " +
        "         SP.SUBALTERNO ";

      
      stmt = conn.prepareStatement(query);

      int indice = 0;

      stmt.setLong(++indice, anagAziendaVO.getIdAzienda().longValue());
      stmt.setInt(++indice, anno);
 
      
      if(!hasUnitToDocument) 
      {
        stmt.setString(++indice, anagAziendaVO.getCUAA().toUpperCase());
        stmt.setLong(++indice, anagAziendaVO.getIdAzienda().longValue());
        stmt.setDate(++indice, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
      }
      
      

      SolmrLogger.debug(this, "Executing getListParticelleForDocumentExtraSistema in StoricoParticellaDAO: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

    
      elencoParticelle = new Vector<StoricoParticellaVO>();

      while(rs.next()) 
      {
        StoricoParticellaVO storicoParticellaElencoVO = new StoricoParticellaVO();
        ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
        storicoParticellaElencoVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
        storicoParticellaElencoVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
        storicoParticellaElencoVO.setIstatComune(rs.getString("COMUNE"));
        ComuneVO comuneVO = new ComuneVO();
        comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
        comuneVO.setDescom(rs.getString("DESCOM"));
        storicoParticellaElencoVO.setComuneParticellaVO(comuneVO);
        storicoParticellaElencoVO.setSezione(rs.getString("SEZIONE"));
        storicoParticellaElencoVO.setFoglio(rs.getString("FOGLIO"));
        storicoParticellaElencoVO.setParticella(rs.getString("PARTICELLA"));
        storicoParticellaElencoVO.setSubalterno(rs.getString("SUBALTERNO"));
        storicoParticellaElencoVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
        if(Validator.isNotEmpty(rs.getString("ID_CASO_PARTICOLARE"))) {
          storicoParticellaElencoVO.setIdCasoParticolare(Long.decode(rs.getString("ID_CASO_PARTICOLARE")));
        }
        conduzioneParticellaVO.setSuperficieCondotta(rs.getString("SUPERFICIE_CONDOTTA"));
        CodeDescription titoloPossesso = new CodeDescription(Integer.decode(rs.getString("ID_TITOLO_POSSESSO")), rs.getString("DESCRIZIONE"));
        conduzioneParticellaVO.setTitoloPossesso(titoloPossesso);
        conduzioneParticellaVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
        conduzioneParticellaVO.setDataInizioConduzione(rs.getDate("DATA_INIZIO_CONDUZIONE"));
        conduzioneParticellaVO.setDataFineConduzione(rs.getDate("DATA_FINE_CONDUZIONE"));
        conduzioneParticellaVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
        conduzioneParticellaVO.setIdConduzioneParticella(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));
        storicoParticellaElencoVO.setElencoConduzioni((ConduzioneParticellaVO[])new ConduzioneParticellaVO[]{conduzioneParticellaVO});
        
        
        if(Validator.isNotEmpty(rs.getString("ID_PARTICELLA_CERTIFICATA"))) 
        {
          ParticellaCertificataVO particellaCertificataVO = new ParticellaCertificataVO();
          particellaCertificataVO.setIdParticellaCertificata(new Long(rs.getLong("ID_PARTICELLA_CERTIFICATA")));
          storicoParticellaElencoVO.setParticellaCertificataVO(particellaCertificataVO);
        }
        
        
        elencoParticelle.add(storicoParticellaElencoVO);
      } 

      
      rs.close();
      stmt.close();       

     }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getListParticelleForDocumentExtraSistema - SQLException: "+exc.getMessage()+"\n");
      SolmrLogger.debug(this, "Value of error code in method getListParticelleForDocumentValoreC - SQLException: "+exc.getErrorCode()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getListParticelleForDocumentExtraSistema - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) 
        {
          stmt.close();
        }
        if(conn != null) 
        {
          conn.close();
        }
      }
      catch(SQLException exc) 
      {
        SolmrLogger.error(this, "getListParticelleForDocumentExtraSistema - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        SolmrLogger.debug(this, "Value of error code in method getListParticelleForDocument - SQLException: "+exc.getErrorCode()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) 
      {
        SolmrLogger.error(this, "getListParticelleForDocumentExtraSistema - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListParticelleForDocumentExtraSistema method in StoricoParticellaDAO\n");
    if(elencoParticelle.size() == 0) 
    {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
    }
    else 
    {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
    }
  }
	
	
	/**
	 * Metodo che mi restituisce il record su DB_STORICO_PARTICELLA a partire
	 * dalla chiave logica della particella(COMUNE, SEZIONE, FOGLIO, PARTICELLA, SUBALTERNO)
	 * Se specifico la data inizio validita ricerco il record valido a quella data, in caso
	 * contrario ricerco il record attivo.
	 * Metodo utilizzato da SMRGAASV
	 * 
	 * @param istatComune
	 * @param sezione
	 * @param foglio
	 * @param particella
	 * @param subalterno
	 * @param dataInizioValidita
	 * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO
	 * @throws DataAccessException
	 */
	public StoricoParticellaVO findStoricoParticellaVOByParameters(String istatComune, String sezione, String foglio, String particella, String subalterno, java.util.Date dataInizioValidita) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating findStoricoParticellaVOByParameters method in StoricoParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		StoricoParticellaVO storicoParticellaVO = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in findStoricoParticellaVOByParameters method in StoricoParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in findStoricoParticellaVOByParameters method in StoricoParticellaDAO and it values: "+conn+"\n");

			String query = " " +
					     "SELECT " +
					     "       SP.ID_STORICO_PARTICELLA, " +
						   "       SP.ID_PARTICELLA, " +
						   "       SP.DATA_INIZIO_VALIDITA, " +
						   " 		   SP.DATA_FINE_VALIDITA, " +
						   " 		   SP.COMUNE, " +
						   "		   C.DESCOM, " +
						   "		   P.SIGLA_PROVINCIA, " +
						   "       F.ID_AREA_PSN, "+
						   "       TAP.DESCRIZIONE AS DESC_PSN, " +
						   "		   SP.SEZIONE, " +
						   "		   SP.FOGLIO, " +
						   "		   SP.PARTICELLA, " +
						   "		   SP.SUBALTERNO, " +
						   "		   SP.SUP_CATASTALE, " +
						   "       SP.SUPERFICIE_GRAFICA, " +
						   " 		   SP.ID_ZONA_ALTIMETRICA, " +
						   "		   TZA.DESCRIZIONE AS DESC_ALTIMETRICA, " +
						   "		   SP.ID_AREA_A, " +
						   "		   TAA.DESCRIZIONE AS DESC_AREA_A, " +
						   " 		   SP.ID_AREA_B, " +
						   "		   TAB.DESCRIZIONE AS DESC_AREA_B, " +
						   "		   SP.ID_AREA_C, " +
						   "		   TAC.DESCRIZIONE AS DESC_AREA_C, " +
						   "		   SP.ID_AREA_D, " +
						   "		   TAD.DESCRIZIONE AS DESC_AREA_D, " +
						   "       SP.ID_AREA_G, " +
               "       TAG.DESCRIZIONE AS DESC_AREA_G, " +
               "       SP.ID_AREA_H, " +
               "       TAH.DESCRIZIONE AS DESC_AREA_H, " +
               "       SP.ID_AREA_I, " +
               "       TAI.DESCRIZIONE AS DESC_AREA_I, " +
               "       SP.ID_AREA_L, " +
               "       TAL.DESCRIZIONE AS DESC_AREA_L, " +
               "       SP.ID_AREA_M, " +
               "       TAM.DESCRIZIONE AS DESC_AREA_M, " +
						   "		   SP.FLAG_CAPTAZIONE_POZZI, " +
						   "		   SP.FLAG_IRRIGABILE, " +
						   "		   SP.ID_CASO_PARTICOLARE, " +
						   "		   TCP.DESCRIZIONE AS DESC_CASO_PART, " +
						   "		   SP.MOTIVO_MODIFICA, " +	
						   "		   SP.DATA_AGGIORNAMENTO, " +
						   "		   SP.ID_UTENTE_AGGIORNAMENTO, " +
						   "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
					       "          || ' ' " + 
					       "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
					       "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
					       "         WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
						   "       AS DENOMINAZIONE, " +
						   "       (SELECT PVU.DENOMINAZIONE " +
						   "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
						   "        WHERE SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
						   "       AS DESCRIZIONE_ENTE_APPARTENENZA, " +
						   "       SP.ID_CAUSALE_MOD_PARTICELLA, " +
						   "       TCMP.DESCRIZIONE AS DESC_CAUSALE_MOD, " +
						   "		   SP.SUP_NON_ELEGGIBILE, " +
						   "		   SP.SUP_NE_BOSCO_ACQUE_FABBRICATO, " +
						   "		   SP.SUP_NE_FORAGGIERE, " +
						   "		   SP.SUP_EL_FRUTTA_GUSCIO, " +
						   "		   SP.SUP_EL_PRATO_PASCOLO, " +
						   "		   SP.SUP_EL_COLTURE_MISTE, " +
						   "		   SP.SUP_COLTIVAZ_ARBOREA_CONS, " +
						   "		   SP.SUP_COLTIVAZ_ARBOREA_SPEC, " +
						   "		   SP.DATA_FOTO, " +
						   "		   SP.TIPO_FOTO, " +
						   "		   SP.ID_FONTE, " +
						   "		   TF.DESCRIZIONE AS DESC_FONTE, " +
						   "		   SP.ID_DOCUMENTO, " +
						   "       PART.DATA_CESSAZIONE, " +
						   "       SP.ID_DOCUMENTO_PROTOCOLLATO, " +
						   "       SP.ID_IRRIGAZIONE, " +
						   "       TI.DESCRIZIONE AS DESC_IRRIG, " +
						   "       TI.DESCRIZIONE AS DESC_IRRIGAZIONE, " +
               "       TI.DATA_INIZIO_VALIDITA AS DATA_INI_IRRIGAZ, " +
               "       TI.DATA_FINE_VALIDITA AS DATA_FINE_IRRIGAZ, " +
						   "       SP.ID_FASCIA_FLUVIALE, " +
						   "       TFF.DESCRIZIONE AS DESC_FLUVIALE, " +
						   "       SP.ID_POTENZIALITA_IRRIGUA, " +
               "       TPI.DESCRIZIONE AS DESC_POTENZIALITA_IRRIGUA, " +
               "       TPI.CODICE AS COD_POTENZIALITA_IRRIGUA, " +
               "       SP.ID_TERRAZZAMENTO, " +
               "       TTR.DESCRIZIONE AS DESC_TERRAZZAMENTO, " +
               "       TTR.CODICE AS COD_TERRAZZAMENTO, " +
               "       SP.ID_ROTAZIONE_COLTURALE, " +
               "       TRC.DESCRIZIONE AS DESC_ROTAZIONE_COLTURALE, " +
               "       TRC.CODICE AS COD_ROTAZIONE_COLTURALE, " +
               "       SP.PERCENTUALE_PENDENZA_MEDIA, " +
               "       SP.GRADI_PENDENZA_MEDIA, " +
               "       SP.GRADI_ESPOSIZIONE_MEDIA, " +
               "       SP.METRI_ALTITUDINE_MEDIA, " +
               "       SP.ID_METODO_IRRIGUO, " +
               "       TMI.DESCRIZIONE_METODO_IRRIGUO, " +
               "       TMI.CODICE_METODO_IRRIGUO " +
						   " FROM  " +
						   "       DB_STORICO_PARTICELLA SP, " +
						   "		   COMUNE C, " +
						   "		   PROVINCIA P, " +
						   "		   DB_TIPO_ZONA_ALTIMETRICA TZA, " +
						   "		   DB_TIPO_AREA_A TAA, " +
						   "		   DB_TIPO_AREA_B TAB, " +
						   "		   DB_TIPO_AREA_C TAC, " +
						   "		   DB_TIPO_AREA_D TAD, " +
						   "       DB_TIPO_AREA_G TAG, " +
						   "       DB_TIPO_AREA_H TAH, " +
						   "       DB_TIPO_AREA_I TAI, " +
						   "       DB_TIPO_AREA_L TAL, " +
						   "       DB_TIPO_AREA_M TAM, " +
						   "		   DB_TIPO_CASO_PARTICOLARE TCP, " +
						 //  "		   PAPUA_V_UTENTE_LOGIN PVU, " +
						   "		   DB_TIPO_CAUSALE_MOD_PARTICELLA TCMP, " +
						   "		   DB_TIPO_FONTE TF, " +
						   " 		   DB_TIPO_DOCUMENTO TD, " +
						   "       DB_PARTICELLA PART, " +
						   "       DB_TIPO_IRRIGAZIONE TI, " +
						   "       DB_TIPO_AREA_PSN TAP, " +
						   "       DB_TIPO_FASCIA_FLUVIALE TFF, " +
						   "       DB_FOGLIO F, "+
						   "       DB_TIPO_POTENZIALITA_IRRIGUA TPI, " +
               "       DB_TIPO_TERRAZZAMENTO TTR, " +
               "       DB_TIPO_ROTAZIONE_COLTURALE TRC, " +
               "       DB_TIPO_METODO_IRRIGUO TMI " +
						   "WHERE " +
						   "       SP.COMUNE = C.ISTAT_COMUNE " +
						   "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) " +
						   "AND    F.ID_AREA_PSN = TAP.ID_AREA_PSN(+) " +
						   "AND    SP.ID_ZONA_ALTIMETRICA = TZA.ID_ZONA_ALTIMETRICA(+) " + 
						   "AND    SP.ID_AREA_A = TAA.ID_AREA_A(+) " +
						   "AND    SP.ID_AREA_B = TAB.ID_AREA_B(+) " + 
						   "AND    SP.ID_AREA_C = TAC.ID_AREA_C(+) " +
						   "AND    SP.ID_AREA_D = TAD.ID_AREA_D(+) " +
						   "AND    SP.ID_AREA_G = TAG.ID_AREA_G(+) " +
						   "AND    SP.ID_AREA_H = TAH.ID_AREA_H(+) " +
						   "AND    SP.ID_AREA_I = TAI.ID_AREA_I(+) " +
						   "AND    SP.ID_AREA_L = TAL.ID_AREA_L(+) " +
						   "AND    SP.ID_AREA_M = TAM.ID_AREA_M(+) " +
						   "AND    SP.ID_CASO_PARTICOLARE = TCP.ID_CASO_PARTICOLARE(+) " +
						   //"AND    SP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
						   "AND    SP.ID_CAUSALE_MOD_PARTICELLA = TCMP.ID_CAUSALE_MOD_PARTICELLA(+) " +
						   "AND    SP.ID_FONTE = TF.ID_FONTE(+) " +
						   "AND    SP.ID_DOCUMENTO = TD.ID_DOCUMENTO(+) " +
						   "AND    SP.ID_PARTICELLA = PART.ID_PARTICELLA " +
						   "AND    SP.ID_IRRIGAZIONE = TI.ID_IRRIGAZIONE(+) " +
						   "AND    SP.COMUNE = F.COMUNE(+) "+
	             "AND    SP.FOGLIO = F.FOGLIO(+) "+
	             "AND    nvl(SP.SEZIONE,' ') = nvl(F.SEZIONE(+),' ') "+
				       "AND    SP.COMUNE = ? " +
				       "AND    NVL(SP.SEZIONE, '-') = UPPER(NVL(?, '-')) " +
				       "AND    SP.FOGLIO = ? ";
			if(Validator.isNotEmpty(particella))
			{
			  query +=      "AND    SP.PARTICELLA = ? ";
			}
			else
			{
			  query +=      "AND    SP.PARTICELLA IS NULL ";
			}
			query += "AND    UPPER(NVL(SP.SUBALTERNO, '-')) = UPPER(NVL(?, '-')) " +
			         "AND    SP.ID_FASCIA_FLUVIALE = TFF.ID_FASCIA_FLUVIALE(+) " +
          		 "AND    SP.ID_POTENZIALITA_IRRIGUA = TPI.ID_POTENZIALITA_IRRIGUA(+) " +
               "AND    SP.ID_TERRAZZAMENTO = TTR.ID_TERRAZZAMENTO(+) " +
               "AND    SP.ID_ROTAZIONE_COLTURALE = TRC.ID_ROTAZIONE_COLTURALE(+) " +
               "AND    SP.ID_METODO_IRRIGUO = TMI.ID_METODO_IRRIGUO (+) ";
			if(dataInizioValidita == null) {
				query +=   " AND    SP.DATA_FINE_VALIDITA IS NULL ";
			}
			else {
				query +=   " AND    SP.DATA_INIZIO_VALIDITA >= ? " +
				           " AND    NVL(SP.DATA_FINE_VALIDITA, ?) > ? ";
			}

			SolmrLogger.debug(this, "Value of parameter 1 [ISTAT_COMUNE] in findStoricoParticellaVOByParameters method in StoricoParticellaDAO: "+istatComune+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [SEZIONE] in findStoricoParticellaVOByParameters method in StoricoParticellaDAO: "+sezione+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [FOGLIO] in findStoricoParticellaVOByParameters method in StoricoParticellaDAO: "+foglio+"\n");
			SolmrLogger.debug(this, "Value of parameter 4 [PARTICELLA] in findStoricoParticellaVOByParameters method in StoricoParticellaDAO: "+particella+"\n");
			SolmrLogger.debug(this, "Value of parameter 5 [SUBALTERNO] in findStoricoParticellaVOByParameters method in StoricoParticellaDAO: "+subalterno+"\n");
			SolmrLogger.debug(this, "Value of parameter 6 [DATA_INIZIO_VALIDITA] in findStoricoParticellaVOByParameters method in StoricoParticellaDAO: "+dataInizioValidita+"\n");

			stmt = conn.prepareStatement(query);
			
			int idx = 0;
			
			stmt.setString(++idx, istatComune);
			stmt.setString(++idx, sezione);
			stmt.setString(++idx, foglio);
			if(Validator.isNotEmpty(particella))
      {
			  stmt.setString(++idx, particella);
      }
			stmt.setString(++idx, subalterno);
			if(dataInizioValidita != null) {
				stmt.setDate(++idx, new java.sql.Date(dataInizioValidita.getTime()));
				stmt.setDate(++idx, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
				stmt.setDate(++idx, new java.sql.Date(dataInizioValidita.getTime()));
			}
			
			SolmrLogger.debug(this, "Executing findStoricoParticellaVOByParameters: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) 
			{
				storicoParticellaVO = new StoricoParticellaVO(); 
				storicoParticellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
				storicoParticellaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
				storicoParticellaVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				storicoParticellaVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
				ComuneVO comuneVO = new ComuneVO();
				comuneVO.setIstatComune(rs.getString("COMUNE"));
				comuneVO.setDescom(rs.getString("DESCOM"));
				if(Validator.isNotEmpty(rs.getString("SIGLA_PROVINCIA"))) {
					ProvinciaVO provinciaVO = new ProvinciaVO();
					provinciaVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));
					comuneVO.setProvinciaVO(provinciaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_AREA_PSN"))) {
					CodeDescription areaPsn = new CodeDescription(new Integer(rs.getInt("ID_AREA_PSN")), rs.getString("DESC_PSN"));
					comuneVO.setAreaPsn(areaPsn);
				}
				storicoParticellaVO.setComuneParticellaVO(comuneVO);
				storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
				storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
				storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
				storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
				storicoParticellaVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
				storicoParticellaVO.setSuperficieGrafica(rs.getString("SUPERFICIE_GRAFICA"));
				if(Validator.isNotEmpty(rs.getString("ID_ZONA_ALTIMETRICA"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_ZONA_ALTIMETRICA")), rs.getString("DESC_ALTIMETRICA"));
					storicoParticellaVO.setIdZonaAltimetrica(Long.decode(rs.getString("ID_ZONA_ALTIMETRICA")));
					storicoParticellaVO.setZonaAltimetrica(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_AREA_A"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_A")), rs.getString("DESC_AREA_A"));
					storicoParticellaVO.setIdAreaA(new Long(rs.getString("ID_AREA_A")));
					storicoParticellaVO.setAreaA(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_AREA_B"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_B")), rs.getString("DESC_AREA_B"));
					storicoParticellaVO.setIdAreaB(new Long(rs.getString("ID_AREA_B")));
					storicoParticellaVO.setAreaB(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_AREA_C"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_C")), rs.getString("DESC_AREA_C"));
					storicoParticellaVO.setIdAreaC(new Long(rs.getString("ID_AREA_C")));
					storicoParticellaVO.setAreaC(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_AREA_D"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_D")), rs.getString("DESC_AREA_D"));
					storicoParticellaVO.setIdAreaD(new Long(rs.getString("ID_AREA_D")));
					storicoParticellaVO.setAreaD(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_AREA_G"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_G")), rs.getString("DESC_AREA_G"));
          storicoParticellaVO.setIdAreaG(new Long(rs.getString("ID_AREA_G")));
          storicoParticellaVO.setAreaG(code);
        }
				if(Validator.isNotEmpty(rs.getString("ID_AREA_H"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_H")), rs.getString("DESC_AREA_H"));
          storicoParticellaVO.setIdAreaH(new Long(rs.getString("ID_AREA_H")));
          storicoParticellaVO.setAreaH(code);
        }
				if(Validator.isNotEmpty(rs.getString("ID_AREA_I"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_I")), rs.getString("DESC_AREA_I"));
          storicoParticellaVO.setIdAreaI(new Long(rs.getString("ID_AREA_I")));
          storicoParticellaVO.setAreaI(code);
        }
				if(Validator.isNotEmpty(rs.getString("ID_AREA_L"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_L")), rs.getString("DESC_AREA_L"));
          storicoParticellaVO.setIdAreaL(new Long(rs.getString("ID_AREA_L")));
          storicoParticellaVO.setAreaL(code);
        }
				if(Validator.isNotEmpty(rs.getString("ID_AREA_M"))) {
          CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_AREA_M")), rs.getString("DESC_AREA_M"));
          storicoParticellaVO.setIdAreaM(new Long(rs.getString("ID_AREA_M")));
          storicoParticellaVO.setAreaM(code);
        }
				storicoParticellaVO.setFlagCaptazionePozzi(rs.getString("FLAG_CAPTAZIONE_POZZI"));
				storicoParticellaVO.setFlagIrrigabile(rs.getString("FLAG_IRRIGABILE"));
				if(Validator.isNotEmpty(rs.getString("ID_CASO_PARTICOLARE"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_CASO_PARTICOLARE")), rs.getString("DESC_CASO_PART"));
					storicoParticellaVO.setIdCasoParticolare(new Long(rs.getString("ID_CASO_PARTICOLARE")));
					storicoParticellaVO.setCasoParticolare(code);
				}
				storicoParticellaVO.setMotivoModifica(rs.getString("MOTIVO_MODIFICA"));
				storicoParticellaVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
				storicoParticellaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				UtenteIrideVO utenteIrideVO = new UtenteIrideVO();
				utenteIrideVO.setIdUtente(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				utenteIrideVO.setDenominazione(rs.getString("DENOMINAZIONE"));
				utenteIrideVO.setDescrizioneEnteAppartenenza(rs.getString("DESCRIZIONE_ENTE_APPARTENENZA"));
				storicoParticellaVO.setUtenteAggiornamento(utenteIrideVO);
				if(Validator.isNotEmpty(rs.getString("ID_CAUSALE_MOD_PARTICELLA"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_CAUSALE_MOD_PARTICELLA")), rs.getString("DESC_CAUSALE_MOD"));
					storicoParticellaVO.setIdCausaleModParticella(new Long(rs.getString("ID_CAUSALE_MOD_PARTICELLA")));
					storicoParticellaVO.setCausaleModParticella(code);
				}
				storicoParticellaVO.setSupNonEleggibile(rs.getString("SUP_NON_ELEGGIBILE"));
				storicoParticellaVO.setSupNeBoscoAcqueFabbricato(rs.getString("SUP_NE_BOSCO_ACQUE_FABBRICATO"));
				storicoParticellaVO.setSupNeForaggere(rs.getString("SUP_NE_FORAGGIERE"));
				storicoParticellaVO.setSupElFruttaGuscio(rs.getString("SUP_EL_FRUTTA_GUSCIO"));
				storicoParticellaVO.setSupElPratoPascolo(rs.getString("SUP_EL_PRATO_PASCOLO"));
				storicoParticellaVO.setSupElColtureMiste(rs.getString("SUP_EL_COLTURE_MISTE"));
				storicoParticellaVO.setSupColtivazArboreaCons(rs.getString("SUP_COLTIVAZ_ARBOREA_CONS"));
				storicoParticellaVO.setSupColtivazArboreaSpec(rs.getString("SUP_COLTIVAZ_ARBOREA_SPEC"));
				storicoParticellaVO.setDataFoto(rs.getDate("DATA_FOTO"));
				storicoParticellaVO.setTipoFoto(rs.getString("TIPO_FOTO"));
				if(Validator.isNotEmpty(rs.getString("ID_FONTE"))) {
					CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_FONTE")), rs.getString("DESC_FONTE"));
					storicoParticellaVO.setIdFonte(new Long(rs.getString("ID_FONTE")));
					storicoParticellaVO.setFonte(code);
				}
				if(Validator.isNotEmpty(rs.getString("ID_DOCUMENTO"))) {
					storicoParticellaVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
				}
				storicoParticellaVO.setDataCessazione(rs.getDate("DATA_CESSAZIONE"));
				if(Validator.isNotEmpty(rs.getString("ID_DOCUMENTO_PROTOCOLLATO"))) {
					storicoParticellaVO.setIdDocumentoProtocollato(new Long(rs.getString("ID_DOCUMENTO_PROTOCOLLATO")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_IRRIGAZIONE"))) {
					storicoParticellaVO.setIdIrrigazione(new Long(rs.getLong("ID_IRRIGAZIONE")));
					TipoIrrigazioneVO tipoIrrigazioneVO = new TipoIrrigazioneVO();
					tipoIrrigazioneVO.setIdIrrigazione(new Long(rs.getLong("ID_IRRIGAZIONE")));
					tipoIrrigazioneVO.setDescrizione(rs.getString("DESC_IRRIGAZIONE"));
					tipoIrrigazioneVO.setDataInizioValidita(rs.getDate("DATA_INI_IRRIGAZ"));
					tipoIrrigazioneVO.setDataFineValidita(rs.getDate("DATA_FINE_IRRIGAZ"));
					tipoIrrigazioneVO.setDescrizione(rs.getString("DESC_IRRIG"));
					storicoParticellaVO.setTipoIrrigazioneVO(tipoIrrigazioneVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_FASCIA_FLUVIALE"))) {
					storicoParticellaVO.setIdFasciaFluviale(new Long(rs.getLong("ID_FASCIA_FLUVIALE")));
					CodeDescription fasciaFluviale = new CodeDescription(Integer.decode(rs.getString("ID_FASCIA_FLUVIALE")), rs.getString("DESC_FLUVIALE"));
					storicoParticellaVO.setFasciaFluviale(fasciaFluviale);
				}
				if(Validator.isNotEmpty(rs.getString("ID_POTENZIALITA_IRRIGUA"))) {
          storicoParticellaVO.setIdPotenzialitaIrrigua(new Long(rs.getLong("ID_POTENZIALITA_IRRIGUA")));
          TipoPotenzialitaIrriguaVO tipoPotenzialitaIrriguaVO = new TipoPotenzialitaIrriguaVO();
          tipoPotenzialitaIrriguaVO.setIdPotenzialitaIrrigua(new Long(rs.getLong("ID_POTENZIALITA_IRRIGUA")));
          tipoPotenzialitaIrriguaVO.setDescrizione(rs.getString("DESC_POTENZIALITA_IRRIGUA"));
          tipoPotenzialitaIrriguaVO.setCodice(rs.getString("COD_POTENZIALITA_IRRIGUA"));
          storicoParticellaVO.setTipoPotenzialitaIrriguaVO(tipoPotenzialitaIrriguaVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_TERRAZZAMENTO"))) {
          storicoParticellaVO.setIdTerrazzamento(new Long(rs.getLong("ID_TERRAZZAMENTO")));
          TipoTerrazzamentoVO tipoTerrazzamentoVO = new TipoTerrazzamentoVO();
          tipoTerrazzamentoVO.setIdTerrazzamento(new Long(rs.getLong("ID_TERRAZZAMENTO")));
          tipoTerrazzamentoVO.setDescrizione(rs.getString("DESC_TERRAZZAMENTO"));
          tipoTerrazzamentoVO.setCodice(rs.getString("COD_TERRAZZAMENTO"));
          storicoParticellaVO.setTipoTerrazzamentoVO(tipoTerrazzamentoVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_ROTAZIONE_COLTURALE"))) {
          storicoParticellaVO.setIdRotazioneColturale(new Long(rs.getLong("ID_ROTAZIONE_COLTURALE")));
          TipoRotazioneColturaleVO tipoRotazioneColturaleVO = new TipoRotazioneColturaleVO();
          tipoRotazioneColturaleVO.setIdRotazioneColturale(new Long(rs.getLong("ID_ROTAZIONE_COLTURALE")));
          tipoRotazioneColturaleVO.setDescrizione(rs.getString("DESC_ROTAZIONE_COLTURALE"));
          tipoRotazioneColturaleVO.setCodice(rs.getString("COD_ROTAZIONE_COLTURALE"));
          storicoParticellaVO.setTipoRotazioneColturaleVO(tipoRotazioneColturaleVO);
        }
        
        storicoParticellaVO.setPercentualePendenzaMedia(rs.getBigDecimal("PERCENTUALE_PENDENZA_MEDIA"));
        storicoParticellaVO.setGradiPendenzaMedia(rs.getBigDecimal("GRADI_PENDENZA_MEDIA"));
        storicoParticellaVO.setGradiEsposizioneMedia(rs.getBigDecimal("GRADI_ESPOSIZIONE_MEDIA"));
        storicoParticellaVO.setMetriAltitudineMedia(rs.getBigDecimal("METRI_ALTITUDINE_MEDIA"));
        
        
        if(Validator.isNotEmpty(rs.getString("ID_METODO_IRRIGUO"))) {
          storicoParticellaVO.setIdMetodoIrriguo(new Long(rs.getLong("ID_METODO_IRRIGUO")));
          TipoMetodoIrriguoVO tipoMetodoIrriguo = new TipoMetodoIrriguoVO();
          tipoMetodoIrriguo.setCodiceMetodoIrriguo(rs.getString("CODICE_METODO_IRRIGUO"));
          tipoMetodoIrriguo.setDescrizioneMetodoIrriguo(rs.getString("DESCRIZIONE_METODO_IRRIGUO"));
          
          storicoParticellaVO.setTipoMetodoIrriguo(tipoMetodoIrriguo);
        }
				
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "findStoricoParticellaVOByParameters in StoricoParticellaDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "findStoricoParticellaVOByParameters in StoricoParticellaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "findStoricoParticellaVOByParameters in StoricoParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "findStoricoParticellaVOByParameters in StoricoParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated findStoricoParticellaVOByParameters method in StoricoParticellaDAO\n");
		return storicoParticellaVO;
	}
	
	/**
	 * Metodo che mi permette di estrarre l'elenco delle particelle associabili ad
	 * un fabbricato relative ad un determinato piano di riferimento
	 * 
	 * @param idFabbricato
	 * @param idPianoRiferimento
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
	 * @throws DataAccessException
	 */
	public StoricoParticellaVO[] getListParticelleForFabbricato(Long idFabbricato, Long idPianoRiferimento, String[] orderBy) throws DataAccessException {
	    SolmrLogger.debug(this, "Invocating getListParticelleForFabbricato method in StoricoParticellaDAO\n");
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    Vector<StoricoParticellaVO> elencoParticelle = null;

	    try {

	    	SolmrLogger.debug(this, "Creating db-connection in getListParticelleForFabbricato method in StoricoParticellaDAO\n");
	    	conn = getDatasource().getConnection();
	    	SolmrLogger.debug(this, "Created db-connection in getListParticelleForFabbricato method in StoricoParticellaDAO and it values: "+conn+"\n");

	    	String query = " SELECT DISTINCT SP.ID_PARTICELLA, " +
	    				   "                 SP.ID_STORICO_PARTICELLA, " +
	    			       "                 SP.COMUNE, " +
	    			       "                 P.SIGLA_PROVINCIA, " +
	    			       "                 C.DESCOM, " +
	    			       "                 SP.SEZIONE, " +
	    			       "                 SP.FOGLIO, " +
	    			       "                 SP.PARTICELLA, " +
	    			       "                 SP.SUBALTERNO, " +
	    			       "                 SP.SUP_CATASTALE, " +
	    			       "                 SP.ID_CASO_PARTICOLARE, " +
	    			       "	             PF.DATA_FINE_VALIDITA " +
		                   " FROM            PROVINCIA P, " +
		                   "                 COMUNE C, " +
		                   "                 DB_STORICO_PARTICELLA SP, " +
		                   "                 DB_FABBRICATO_PARTICELLA PF ";
	    	if(idPianoRiferimento.intValue() > 0) {
	    		query +=   "                ,DB_DICHIARAZIONE_CONSISTENZA DC ";
	    	}
		    query+=        " WHERE           PF.ID_FABBRICATO = ? " +
		                   " AND             PF.ID_PARTICELLA = SP.ID_PARTICELLA " +
		                   " AND             SP.COMUNE = C.ISTAT_COMUNE " +
		                   " AND             C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA ";
	    	if(idPianoRiferimento.intValue() > 0) {
				query +=   " AND             DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
				           " AND             SP.DATA_INIZIO_VALIDITA < DC.DATA_INSERIMENTO_DICHIARAZIONE " +
				           " AND             PF.DATA_INIZIO_VALIDITA < DC.DATA_INSERIMENTO_DICHIARAZIONE " +
				           " AND             NVL(SP.DATA_FINE_VALIDITA, ?) > DC.DATA_INSERIMENTO_DICHIARAZIONE " +
				           " AND             NVL(PF.DATA_FINE_VALIDITA, ?) > DC.DATA_INSERIMENTO_DICHIARAZIONE ";
			}
			else {
				query +=   " AND             SP.DATA_FINE_VALIDITA IS NULL " +
				 		   " AND             PF.DATA_FINE_VALIDITA IS NULL ";
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
			
			SolmrLogger.debug(this, "Value of parameter 1 [ID_FABBRICATO] in getListParticelleForFabbricato method in StoricoParticellaDAO: "+idFabbricato+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ID_PIANO_RIFERIMENTO] in getListParticelleForFabbricato method in StoricoParticellaDAO: "+idPianoRiferimento+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idFabbricato.longValue());
			if(idPianoRiferimento.intValue() > 0) {
				stmt.setLong(2, idPianoRiferimento.longValue());
				stmt.setDate(3, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
				stmt.setDate(4, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
			}

		    SolmrLogger.debug(this, "Executing getListParticelleForFabbricato:"+query+"\n");

		    ResultSet rs = stmt.executeQuery();

		    elencoParticelle = new Vector<StoricoParticellaVO>();

		    while(rs.next()) {
		    	StoricoParticellaVO storicoParticellaElencoVO = new StoricoParticellaVO();
		    	storicoParticellaElencoVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
		    	storicoParticellaElencoVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
		    	storicoParticellaElencoVO.setIstatComune(rs.getString("COMUNE"));
		    	ComuneVO comuneVO = new ComuneVO();
		    	comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
		    	comuneVO.setDescom(rs.getString("DESCOM"));
		    	storicoParticellaElencoVO.setComuneParticellaVO(comuneVO);
		    	storicoParticellaElencoVO.setSezione(rs.getString("SEZIONE"));
		    	storicoParticellaElencoVO.setFoglio(rs.getString("FOGLIO"));
		    	storicoParticellaElencoVO.setParticella(rs.getString("PARTICELLA"));
		    	storicoParticellaElencoVO.setSubalterno(rs.getString("SUBALTERNO"));
		    	storicoParticellaElencoVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
		    	storicoParticellaElencoVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
		    	elencoParticelle.add(storicoParticellaElencoVO);
		    }	
		    
		    rs.close();
		    stmt.close();	      
	    }
	    catch(SQLException exc) {
	    	SolmrLogger.error(this, "getListParticelleForFabbricato - SQLException: "+exc.getMessage()+"\n");
	    	throw new DataAccessException(exc.getMessage());
	    }
	    catch(Exception ex) {
	    	SolmrLogger.error(this, "getListParticelleForFabbricato - Generic Exception: "+ex+"\n");
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
	    		SolmrLogger.error(this, "getListParticelleForFabbricato - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
	    		throw new DataAccessException(exc.getMessage());
	    	}
	    	catch(Exception ex) {
	    		SolmrLogger.error(this, "getListParticelleForFabbricato - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
	    		throw new DataAccessException(ex.getMessage());
	    	}
	    }
	    SolmrLogger.debug(this, "Invocated getListParticelleForFabbricato method in StoricoParticellaDAO\n");
	    if(elencoParticelle.size() == 0) {
			return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
		}
		else {
			return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
		}
	}
	
	/**
	 * Metodo che mi permette di estrarre tutte le particelle associate ad un determinato documento ed, eventualmente, di escludere quella sulla
	 * quale si sta facendo il dettaglio
	 *  
	 * @param idDocumento
	 * @param idStoricoParticellaToExclude
	 * @param orderBy[]
	 * @return java.util.Vector
	 * @throws DataAccessException
	 */
	public Vector<StoricoParticellaVO> getListParticelleByIdDocumentoAlPianoCorrente(Long idDocumento, Long idStoricoParticellaToExclude, String[] orderBy) 
	  throws DataAccessException 
	{
	  SolmrLogger.debug(this, "Invocating getListParticelleByIdDocumentoAlPianoCorrente method in StoricoParticellaDAO\n");
	  Connection conn = null;
	  PreparedStatement stmt = null;
	  Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();

	  try 
	  {
	   	SolmrLogger.debug(this, "Creating db-connection in getListParticelleByIdDocumentoAlPianoCorrente method in StoricoParticellaDAO\n");
    	conn = getDatasource().getConnection();
    	SolmrLogger.debug(this, "Created db-connection in getListParticelleByIdDocumentoAlPianoCorrente method in StoricoParticellaDAO and it values: "+conn+"\n");

    	String query = 
    	  "SELECT  SP.ID_PARTICELLA, "+
    	  "        SP.ID_STORICO_PARTICELLA, " +
	    	"        SP.COMUNE, " +
	    	"        C.DESCOM, " +
	    	"        P.SIGLA_PROVINCIA, " +
	    	"        SP.SEZIONE, " +
	    	"        SP.FOGLIO, " +
	    	"        SP.PARTICELLA, " +
	    	"        SP.SUBALTERNO, " +
	    	"        SP.SUP_CATASTALE, " +
	    	"        SP.ID_CASO_PARTICOLARE, " +
	    	"        CP.ID_CONDUZIONE_PARTICELLA, " +
	    	"        CP.ID_TITOLO_POSSESSO, " +
	    	"        CP.SUPERFICIE_CONDOTTA, " + 
	    	"        CP.PERCENTUALE_POSSESSO," +
	    	"        DC.NOTE, " +
	    	"        DC.LAVORAZIONE_PRIORITARIA, " +
	    	"        PC.ID_PARTICELLA_CERTIFICATA " + 
	    	"FROM	   DB_DOCUMENTO D, " +     
	    	"        DB_DOCUMENTO_CONDUZIONE DC, " +
	    	"        DB_CONDUZIONE_PARTICELLA CP, " +
	    	"        DB_STORICO_PARTICELLA SP, " +
	    	"        DB_PARTICELLA_CERTIFICATA PC, " +
	    	"        COMUNE C, " +
	    	"        PROVINCIA P " +
	    	"WHERE   D.ID_DOCUMENTO = ? " +
	    	"AND     D.ID_DOCUMENTO = DC.ID_DOCUMENTO " +
	    	"AND     NVL(DC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > SYSDATE " +  
	    	"AND     DC.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA " +
	    	"AND     CP.DATA_FINE_CONDUZIONE IS NULL " +
	    	"AND     CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
	    	"AND     SP.DATA_FINE_VALIDITA IS NULL " +
	    	"AND     SP.COMUNE = C.ISTAT_COMUNE " +
	    	"AND     C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
	    	"AND     SP.ID_PARTICELLA = PC.ID_PARTICELLA (+) " +
	    	"AND     PC.DATA_FINE_VALIDITA(+)  IS NULL ";
	    if(idStoricoParticellaToExclude != null) 
	    {
	    	query +=   
	    	"AND    SP.ID_STORICO_PARTICELLA != ? ";
	    }
	    query +=       
	      "GROUP BY SP.ID_PARTICELLA, " +
	      "         SP.ID_STORICO_PARTICELLA, " +
	      "         SP.COMUNE, " +
	    	"         C.DESCOM, " +
	    	"         P.SIGLA_PROVINCIA, " +
	    	"         SP.SEZIONE, " +
	    	"         SP.FOGLIO, " +
	    	"         SP.PARTICELLA, " +
	    	"         SP.SUBALTERNO, " +
	    	"         SP.SUP_CATASTALE, " +
	    	"         SP.ID_CASO_PARTICOLARE, " +
	    	"         CP.ID_CONDUZIONE_PARTICELLA, " +
	    	"         CP.ID_TITOLO_POSSESSO, " +
	    	"         CP.SUPERFICIE_CONDOTTA, " +
	    	"         CP.PERCENTUALE_POSSESSO," +
	    	"         DC.NOTE, " +
        "         DC.LAVORAZIONE_PRIORITARIA, " +
        "         PC.ID_PARTICELLA_CERTIFICATA "; 
	    	
	    String ordinamento = "";
			if(orderBy != null && orderBy.length > 0) 
			{
				String criterio = "";
				for(int i = 0; i < orderBy.length; i++) 
				{
					if(i == 0) {
						criterio = (String)orderBy[i];
					}
					else {
						criterio += ", "+(String)orderBy[i];
					}
				}
				ordinamento = "ORDER BY "+criterio;
			}
			if(!ordinamento.equals("")) 
			{
				query += ordinamento;
			}
	    	
	    stmt = conn.prepareStatement(query);

	    SolmrLogger.debug(this, "Value of parameter 1 [ID_DOCUMENTO] in getListParticelleByIdDocumentoAlPianoCorrente method in StoricoParticellaDAO: "+idDocumento+"\n");
	    SolmrLogger.debug(this, "Value of parameter 2 [ID_STORICO_PARTICELLA] in getListParticelleByIdDocumentoAlPianoCorrente method in StoricoParticellaDAO: "+idStoricoParticellaToExclude+"\n");
	    	
	    stmt.setLong(1, idDocumento.longValue());
	    if(idStoricoParticellaToExclude != null) 
	    {
	    	stmt.setLong(2, idStoricoParticellaToExclude.longValue());
	    }

		  SolmrLogger.debug(this, "Executing getListParticelleByIdDocumentoAlPianoCorrente in StoricoParticellaDAO: "+query+"\n");

		  ResultSet rs = stmt.executeQuery();

		  while(rs.next()) 
		  {
		   	StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
		   	ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
		   	DocumentoConduzioneVO documentoConduzioneVO = new DocumentoConduzioneVO();
		   	storicoParticellaVO.setIdParticella(checkLongNull(rs.getString("ID_PARTICELLA")));
		   	storicoParticellaVO.setIdStoricoParticella(checkLongNull(rs.getString("ID_STORICO_PARTICELLA")));
		   	storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
		   	ComuneVO comuneVO = new ComuneVO();
		   	comuneVO.setDescom(rs.getString("DESCOM"));
		   	comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
		   	storicoParticellaVO.setComuneParticellaVO(comuneVO);
		   	storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
		   	storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
		   	storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
		   	storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
		   	storicoParticellaVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
		   	storicoParticellaVO.setIdCasoParticolare(checkLongNull(rs.getString("ID_CASO_PARTICOLARE")));
		   	conduzioneParticellaVO.setIdConduzioneParticella(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));
		   	conduzioneParticellaVO.setSuperficieCondotta(rs.getString("SUPERFICIE_CONDOTTA"));
		   	conduzioneParticellaVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
		   	conduzioneParticellaVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
		   	documentoConduzioneVO.setNote(rs.getString("NOTE"));
		   	documentoConduzioneVO.setLavorazionePrioritaria(rs.getString("LAVORAZIONE_PRIORITARIA"));
		   	conduzioneParticellaVO.setElencoDocumentoConduzione((DocumentoConduzioneVO[])new DocumentoConduzioneVO[]{documentoConduzioneVO});
		   	storicoParticellaVO.setElencoConduzioni((ConduzioneParticellaVO[])new ConduzioneParticellaVO[]{conduzioneParticellaVO});
		   	ParticellaCertificataVO particellaCertificataVO = new ParticellaCertificataVO();
		   	particellaCertificataVO.setIdParticellaCertificata(checkLongNull(rs.getString("ID_PARTICELLA_CERTIFICATA")));
		   	storicoParticellaVO.setParticellaCertificataVO(particellaCertificataVO);
		   	elencoParticelle.add(storicoParticellaVO);
		  }	
		  rs.close();
		  stmt.close();	      
	  }
	  catch(SQLException exc) 
	  {
	   	SolmrLogger.error(this, "getListParticelleByIdDocumentoAlPianoCorrente - SQLException: "+exc+"\n");
	   	throw new DataAccessException(exc.getMessage());
	  }
	  catch(Exception ex) 
	  {
	   	SolmrLogger.error(this, "getListParticelleByIdDocumentoAlPianoCorrente - Generic Exception: "+ex+"\n");
	   	throw new DataAccessException(ex.getMessage());
	  }
	  finally 
	  {
	   	try 
	   	{
	    	if(stmt != null) 
	    	{
	      	stmt.close();
	      }
	      if(conn != null) 
	      {
	      	conn.close();
	      }
	    }
	    catch(SQLException exc) 
	    {
	    	SolmrLogger.error(this, "getListParticelleByIdDocumentoAlPianoCorrente - SQLException while closing Statement and Connection: "+exc+"\n");
	    	throw new DataAccessException(exc.getMessage());
	    }
	    catch(Exception ex) 
	    {
	    	SolmrLogger.error(this, "getListParticelleByIdDocumentoAlPianoCorrente - Generic Exception while closing Statement and Connection: "+ex+"\n");
	    	throw new DataAccessException(ex.getMessage());
	    }
	  }
	  SolmrLogger.debug(this, "Invocated getListParticelleByIdDocumentoAlPianoCorrente method in StoricoParticellaDAO\n");
	  return elencoParticelle;
	}
	
	
	public Vector<StoricoParticellaVO> getAllParticelleByIdDocumento(Long idDocumento) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getAllParticelleByIdDocumento method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getAllParticelleByIdDocumento method in StoricoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getAllParticelleByIdDocumento method in StoricoParticellaDAO and it values: "+conn+"\n");

      String query = 
        "SELECT  SP.ID_PARTICELLA, "+
        "        SP.ID_STORICO_PARTICELLA, " +
        "        SP.COMUNE, " +
        "        C.DESCOM, " +
        "        P.SIGLA_PROVINCIA, " +
        "        SP.SEZIONE, " +
        "        SP.FOGLIO, " +
        "        SP.PARTICELLA, " +
        "        SP.SUBALTERNO, " +
        "        SP.SUP_CATASTALE, " +
        "        SP.ID_CASO_PARTICOLARE, " +
        "        CP.ID_CONDUZIONE_PARTICELLA, " +
        "        CP.ID_TITOLO_POSSESSO, " +
        "        CP.SUPERFICIE_CONDOTTA, " + 
        "        CP.PERCENTUALE_POSSESSO," +
        "        DC.NOTE, " +
        "        DC.LAVORAZIONE_PRIORITARIA, " +
        "        PC.ID_PARTICELLA_CERTIFICATA " + 
        "FROM    DB_DOCUMENTO D, " +     
        "        DB_DOCUMENTO_CONDUZIONE DC, " +
        "        DB_CONDUZIONE_PARTICELLA CP, " +
        "        DB_STORICO_PARTICELLA SP, " +
        "        DB_PARTICELLA_CERTIFICATA PC, " +
        "        COMUNE C, " +
        "        PROVINCIA P " +
        "WHERE   D.ID_DOCUMENTO = ? " +
        "AND     D.ID_DOCUMENTO = DC.ID_DOCUMENTO " +
        "AND     NVL(DC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > SYSDATE " +  
        "AND     DC.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA " +
        "AND     CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
        "AND     SP.DATA_FINE_VALIDITA IS NULL " +
        "AND     SP.COMUNE = C.ISTAT_COMUNE " +
        "AND     C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
        "AND     SP.ID_PARTICELLA = PC.ID_PARTICELLA (+) " +
        "AND     PC.DATA_FINE_VALIDITA(+)  IS NULL " +  
        "GROUP BY SP.ID_PARTICELLA, " +
        "         SP.ID_STORICO_PARTICELLA, " +
        "         SP.COMUNE, " +
        "         C.DESCOM, " +
        "         P.SIGLA_PROVINCIA, " +
        "         SP.SEZIONE, " +
        "         SP.FOGLIO, " +
        "         SP.PARTICELLA, " +
        "         SP.SUBALTERNO, " +
        "         SP.SUP_CATASTALE, " +
        "         SP.ID_CASO_PARTICOLARE, " +
        "         CP.ID_CONDUZIONE_PARTICELLA, " +
        "         CP.ID_TITOLO_POSSESSO, " +
        "         CP.SUPERFICIE_CONDOTTA, " +
        "         CP.PERCENTUALE_POSSESSO," +
        "         DC.NOTE, " +
        "         DC.LAVORAZIONE_PRIORITARIA, " +
        "         PC.ID_PARTICELLA_CERTIFICATA "; 
        
     
        
      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Value of parameter 1 [ID_DOCUMENTO] in getAllParticelleByIdDocumento method in StoricoParticellaDAO: "+idDocumento+"\n");
        
      stmt.setLong(1, idDocumento.longValue());

      SolmrLogger.debug(this, "Executing getAllParticelleByIdDocumento in StoricoParticellaDAO: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
        ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
        DocumentoConduzioneVO documentoConduzioneVO = new DocumentoConduzioneVO();
        storicoParticellaVO.setIdParticella(checkLongNull(rs.getString("ID_PARTICELLA")));
        storicoParticellaVO.setIdStoricoParticella(checkLongNull(rs.getString("ID_STORICO_PARTICELLA")));
        storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
        ComuneVO comuneVO = new ComuneVO();
        comuneVO.setDescom(rs.getString("DESCOM"));
        comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
        storicoParticellaVO.setComuneParticellaVO(comuneVO);
        storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
        storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
        storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
        storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
        storicoParticellaVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
        storicoParticellaVO.setIdCasoParticolare(checkLongNull(rs.getString("ID_CASO_PARTICOLARE")));
        conduzioneParticellaVO.setIdConduzioneParticella(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));
        conduzioneParticellaVO.setSuperficieCondotta(rs.getString("SUPERFICIE_CONDOTTA"));
        conduzioneParticellaVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
        conduzioneParticellaVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
        documentoConduzioneVO.setNote(rs.getString("NOTE"));
        documentoConduzioneVO.setLavorazionePrioritaria(rs.getString("LAVORAZIONE_PRIORITARIA"));
        conduzioneParticellaVO.setElencoDocumentoConduzione((DocumentoConduzioneVO[])new DocumentoConduzioneVO[]{documentoConduzioneVO});
        storicoParticellaVO.setElencoConduzioni((ConduzioneParticellaVO[])new ConduzioneParticellaVO[]{conduzioneParticellaVO});
        ParticellaCertificataVO particellaCertificataVO = new ParticellaCertificataVO();
        particellaCertificataVO.setIdParticellaCertificata(checkLongNull(rs.getString("ID_PARTICELLA_CERTIFICATA")));
        storicoParticellaVO.setParticellaCertificataVO(particellaCertificataVO);
        elencoParticelle.add(storicoParticellaVO);
      } 
      rs.close();
      stmt.close();       
    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "getAllParticelleByIdDocumento - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) 
    {
      SolmrLogger.error(this, "getAllParticelleByIdDocumento - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) 
        {
          stmt.close();
        }
        if(conn != null) 
        {
          conn.close();
        }
      }
      catch(SQLException exc) 
      {
        SolmrLogger.error(this, "getAllParticelleByIdDocumento - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) 
      {
        SolmrLogger.error(this, "getAllParticelleByIdDocumento - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getAllParticelleByIdDocumento method in StoricoParticellaDAO\n");
    return elencoParticelle;
  }
	
	
	/**
	 * 
	 * metodo usato nella pop up dei documenti, visualizza anche legami su db_documento_conduzione_scaduti
	 * 
	 * 
	 * @param idDocumento
	 * @param idStoricoParticellaToExclude
	 * @param orderBy
	 * @return
	 * @throws DataAccessException
	 */
	public Vector<StoricoParticellaVO> getListParticelleByIdDocumentoAlPianoCorrenteLegamiScaduti(Long idDocumento, String[] orderBy) 
	    throws DataAccessException 
	  {
	    SolmrLogger.debug(this, "Invocating getListParticelleByIdDocumentoAlPianoCorrenteLegamiScaduti method in StoricoParticellaDAO\n");
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();

	    try 
	    {
	      SolmrLogger.debug(this, "Creating db-connection in getListParticelleByIdDocumentoAlPianoCorrenteLegamiScaduti method in StoricoParticellaDAO\n");
	      conn = getDatasource().getConnection();
	      SolmrLogger.debug(this, "Created db-connection in getListParticelleByIdDocumentoAlPianoCorrenteLegamiScaduti method in StoricoParticellaDAO and it values: "+conn+"\n");

	      String query = 
	        "SELECT  SP.ID_PARTICELLA, "+
	        "        SP.ID_STORICO_PARTICELLA, " +
	        "        SP.COMUNE, " +
	        "        C.DESCOM, " +
	        "        P.SIGLA_PROVINCIA, " +
	        "        SP.SEZIONE, " +
	        "        SP.FOGLIO, " +
	        "        SP.PARTICELLA, " +
	        "        SP.SUBALTERNO, " +
	        "        SP.SUP_CATASTALE, " +
	        "        SP.ID_CASO_PARTICOLARE, " +
	        "        CP.ID_CONDUZIONE_PARTICELLA, " +
	        "        CP.ID_TITOLO_POSSESSO, " +
	        "        CP.SUPERFICIE_CONDOTTA, " + 
	        "        CP.PERCENTUALE_POSSESSO," +
	        "        DC.NOTE, " +
	        "        DC.LAVORAZIONE_PRIORITARIA, " +
	        "        PC.ID_PARTICELLA_CERTIFICATA, " +
	        "        DC.DATA_FINE_VALIDITA " + 
	        "FROM    DB_DOCUMENTO D, " +     
	        "        DB_DOCUMENTO_CONDUZIONE DC, " +
	        "        DB_CONDUZIONE_PARTICELLA CP, " +
	        "        DB_STORICO_PARTICELLA SP, " +
	        "        DB_PARTICELLA_CERTIFICATA PC, " +
	        "        COMUNE C, " +
	        "        PROVINCIA P " +
	        "WHERE   D.ID_DOCUMENTO = ? " +
	        "AND     D.ID_DOCUMENTO = DC.ID_DOCUMENTO " +
	        //"AND     NVL(DC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > SYSDATE " +  
	        "AND     DC.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA " +
	        "AND     CP.DATA_FINE_CONDUZIONE IS NULL " +
	        "AND     CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
	        "AND     SP.DATA_FINE_VALIDITA IS NULL " +
	        "AND     SP.COMUNE = C.ISTAT_COMUNE " +
	        "AND     C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
	        "AND     SP.ID_PARTICELLA = PC.ID_PARTICELLA (+) " +
	        "AND     PC.DATA_FINE_VALIDITA(+)  IS NULL " +
	        "GROUP BY SP.ID_PARTICELLA, " +
	        "         SP.ID_STORICO_PARTICELLA, " +
	        "         SP.COMUNE, " +
	        "         C.DESCOM, " +
	        "         P.SIGLA_PROVINCIA, " +
	        "         SP.SEZIONE, " +
	        "         SP.FOGLIO, " +
	        "         SP.PARTICELLA, " +
	        "         SP.SUBALTERNO, " +
	        "         SP.SUP_CATASTALE, " +
	        "         SP.ID_CASO_PARTICOLARE, " +
	        "         CP.ID_CONDUZIONE_PARTICELLA, " +
	        "         CP.ID_TITOLO_POSSESSO, " +
	        "         CP.SUPERFICIE_CONDOTTA, " +
	        "         CP.PERCENTUALE_POSSESSO," +
	        "         DC.NOTE, " +
	        "         DC.LAVORAZIONE_PRIORITARIA, " +
	        "         PC.ID_PARTICELLA_CERTIFICATA," +
	        "         DC.DATA_FINE_VALIDITA "; 
	        
	      String ordinamento = "";
	      if(orderBy != null && orderBy.length > 0) 
	      {
	        String criterio = "";
	        for(int i = 0; i < orderBy.length; i++) 
	        {
	          if(i == 0) {
	            criterio = (String)orderBy[i];
	          }
	          else {
	            criterio += ", "+(String)orderBy[i];
	          }
	        }
	        ordinamento = "ORDER BY "+criterio;
	      }
	      if(!ordinamento.equals("")) 
	      {
	        query += ordinamento;
	      }
	        
	      stmt = conn.prepareStatement(query);

	      SolmrLogger.debug(this, "Value of parameter 1 [ID_DOCUMENTO] in getListParticelleByIdDocumentoAlPianoCorrenteLegamiScaduti method in StoricoParticellaDAO: "+idDocumento+"\n");      
	      stmt.setLong(1, idDocumento.longValue());

	      SolmrLogger.debug(this, "Executing getListParticelleByIdDocumentoAlPianoCorrenteLegamiScaduti in StoricoParticellaDAO: "+query+"\n");

	      ResultSet rs = stmt.executeQuery();

	      while(rs.next()) 
	      {
	        StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
	        ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
	        DocumentoConduzioneVO documentoConduzioneVO = new DocumentoConduzioneVO();
	        storicoParticellaVO.setIdParticella(checkLongNull(rs.getString("ID_PARTICELLA")));
	        storicoParticellaVO.setIdStoricoParticella(checkLongNull(rs.getString("ID_STORICO_PARTICELLA")));
	        storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
	        ComuneVO comuneVO = new ComuneVO();
	        comuneVO.setDescom(rs.getString("DESCOM"));
	        comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
	        storicoParticellaVO.setComuneParticellaVO(comuneVO);
	        storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
	        storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
	        storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
	        storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
	        storicoParticellaVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
	        storicoParticellaVO.setIdCasoParticolare(checkLongNull(rs.getString("ID_CASO_PARTICOLARE")));
	        conduzioneParticellaVO.setIdConduzioneParticella(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));
	        conduzioneParticellaVO.setSuperficieCondotta(rs.getString("SUPERFICIE_CONDOTTA"));
	        conduzioneParticellaVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
	        conduzioneParticellaVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
	        documentoConduzioneVO.setNote(rs.getString("NOTE"));
	        documentoConduzioneVO.setLavorazionePrioritaria(rs.getString("LAVORAZIONE_PRIORITARIA"));
	        documentoConduzioneVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
	        conduzioneParticellaVO.setElencoDocumentoConduzione((DocumentoConduzioneVO[])new DocumentoConduzioneVO[]{documentoConduzioneVO});
	        storicoParticellaVO.setElencoConduzioni((ConduzioneParticellaVO[])new ConduzioneParticellaVO[]{conduzioneParticellaVO});
	        ParticellaCertificataVO particellaCertificataVO = new ParticellaCertificataVO();
	        particellaCertificataVO.setIdParticellaCertificata(checkLongNull(rs.getString("ID_PARTICELLA_CERTIFICATA")));
	        storicoParticellaVO.setParticellaCertificataVO(particellaCertificataVO);
	        elencoParticelle.add(storicoParticellaVO);
	      } 
	      rs.close();
	      stmt.close();       
	    }
	    catch(SQLException exc) 
	    {
	      SolmrLogger.error(this, "getListParticelleByIdDocumentoAlPianoCorrenteLegamiScaduti - SQLException: "+exc+"\n");
	      throw new DataAccessException(exc.getMessage());
	    }
	    catch(Exception ex) 
	    {
	      SolmrLogger.error(this, "getListParticelleByIdDocumentoAlPianoCorrenteLegamiScaduti - Generic Exception: "+ex+"\n");
	      throw new DataAccessException(ex.getMessage());
	    }
	    finally 
	    {
	      try 
	      {
	        if(stmt != null) 
	        {
	          stmt.close();
	        }
	        if(conn != null) 
	        {
	          conn.close();
	        }
	      }
	      catch(SQLException exc) 
	      {
	        SolmrLogger.error(this, "getListParticelleByIdDocumentoAlPianoCorrenteLegamiScaduti - SQLException while closing Statement and Connection: "+exc+"\n");
	        throw new DataAccessException(exc.getMessage());
	      }
	      catch(Exception ex) 
	      {
	        SolmrLogger.error(this, "getListParticelleByIdDocumentoAlPianoCorrenteLegamiScaduti - Generic Exception while closing Statement and Connection: "+ex+"\n");
	        throw new DataAccessException(ex.getMessage());
	      }
	    }
	    SolmrLogger.debug(this, "Invocated getListParticelleByIdDocumentoAlPianoCorrenteLegamiScaduti method in StoricoParticellaDAO\n");
	    return elencoParticelle;
	  }
	
	/**
	 * Metodo che mi permette di estrarre tutte le particelle associate ad un determinato documento ed, eventualmente, di escludere quella sulla
	 * quale si sta facendo il dettaglio, relative ad una determinata dichiarazione di consistenza
	 *  
	 * @param idDocumento
	 * @param idStoricoParticellaToExclude
	 * @param orderBy[]
	 * @return java.util.Vector
	 * @throws DataAccessException
	 */
	public Vector<StoricoParticellaVO> getListParticelleByIdDocumentoAllaDichiarazione(Long idDocumento, Long idStoricoParticellaToExclude, java.util.Date dataInserimentoDichiarazione, String[] orderBy) throws DataAccessException {
	    SolmrLogger.debug(this, "Invocating getListParticelleByIdDocumentoAllaDichiarazione method in StoricoParticellaDAO\n");
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();

	    try {

	    	SolmrLogger.debug(this, "Creating db-connection in getListParticelleByIdDocumentoAllaDichiarazione method in StoricoParticellaDAO\n");
	    	conn = getDatasource().getConnection();
	    	SolmrLogger.debug(this, "Created db-connection in getListParticelleByIdDocumentoAllaDichiarazione method in StoricoParticellaDAO and it values: "+conn+"\n");

	    	String query = " SELECT SP.COMUNE, " +
	    				   "        C.DESCOM, " +
	    				   "        P.SIGLA_PROVINCIA, " +
	    				   "        SP.SEZIONE, " +
	    				   "        SP.FOGLIO, " +
	    				   "        SP.PARTICELLA, " +
	    				   "        SP.SUBALTERNO, " +
	    				   "        SP.SUP_CATASTALE, " +
	    				   "        CP.ID_CONDUZIONE_PARTICELLA, " +
	    				   "        CP.ID_TITOLO_POSSESSO, " +
	    				   "        CP.SUPERFICIE_CONDOTTA " + 
	    				   " FROM   DB_DOCUMENTO D, " +     
	    				   "        DB_DOCUMENTO_CONDUZIONE DC, " +
	    				   "        DB_CONDUZIONE_PARTICELLA CP, " +
	    				   "        DB_STORICO_PARTICELLA SP, " +
	    				   "        COMUNE C, " +
	    				   "        PROVINCIA P " +
	    				   " WHERE  D.ID_DOCUMENTO = ? " +
	    				   " AND    D.ID_DOCUMENTO = DC.ID_DOCUMENTO " +
	    				   " AND    TRUNC(DC.DATA_INIZIO_VALIDITA) <= TRUNC(?) " +  
	    				   " AND    TRUNC(NVL(DC.DATA_FINE_VALIDITA, TO_DATE(?, 'DD/MM/YYYY'))) > TRUNC(?) " +  
	    				   " AND    DC.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA " +
	    				   " AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
	    				   " AND    TRUNC(SP.DATA_INIZIO_VALIDITA) <= TRUNC(?) " +  
	    				   " AND    TRUNC(NVL(SP.DATA_FINE_VALIDITA, TO_DATE(?, 'DD/MM/YYYY'))) > TRUNC(?) " +
	    				   " AND    SP.COMUNE = C.ISTAT_COMUNE " +
	    				   " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA ";
	    	if(idStoricoParticellaToExclude != null) {
	    		query += " AND SP.ID_STORICO_PARTICELLA != ? ";
	    	}
	    	query +=       " GROUP BY SP.COMUNE, " +
	    				   "          C.DESCOM, " +
	    				   "          P.SIGLA_PROVINCIA, " +
	    				   "          SP.SEZIONE, " +
	    				   "          SP.FOGLIO, " +
	    				   "          SP.PARTICELLA, " +
	    				   "          SP.SUBALTERNO, " +
	    				   "          SP.SUP_CATASTALE, " +
	    				   "          CP.ID_CONDUZIONE_PARTICELLA, " +
	    				   "          CP.ID_TITOLO_POSSESSO, " +
	    				   "          CP.SUPERFICIE_CONDOTTA ";
	    	
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
	    	
	    	stmt = conn.prepareStatement(query);

	    	SolmrLogger.debug(this, "Value of parameter 1 [ID_DOCUMENTO] in getListParticelleByIdDocumentoAllaDichiarazione method in StoricoParticellaDAO: "+idDocumento+"\n");
	    	SolmrLogger.debug(this, "Value of parameter 2 [ID_STORICO_PARTICELLA] in getListParticelleByIdDocumentoAllaDichiarazione method in StoricoParticellaDAO: "+idStoricoParticellaToExclude+"\n");
	    	
	    	stmt.setLong(1, idDocumento.longValue());
	    	stmt.setDate(2, new java.sql.Date(dataInserimentoDichiarazione.getTime()));
	    	stmt.setString(3, SolmrConstants.ORACLE_FINAL_DATE);
	    	stmt.setDate(4, new java.sql.Date(dataInserimentoDichiarazione.getTime()));
	    	stmt.setDate(5, new java.sql.Date(dataInserimentoDichiarazione.getTime()));
	    	stmt.setString(6, SolmrConstants.ORACLE_FINAL_DATE);
	    	stmt.setDate(7, new java.sql.Date(dataInserimentoDichiarazione.getTime()));
	    	if(idStoricoParticellaToExclude != null) {
	    		stmt.setLong(8, idStoricoParticellaToExclude.longValue());
	    	}

		    SolmrLogger.debug(this, "Executing getListParticelleByIdDocumentoAllaDichiarazione in StoricoParticellaDAO: "+query+"\n");

		    ResultSet rs = stmt.executeQuery();

		    while(rs.next()) {
		    	StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
		    	ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
		    	storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
		    	ComuneVO comuneVO = new ComuneVO();
		    	comuneVO.setDescom(rs.getString("DESCOM"));
		    	comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
		    	storicoParticellaVO.setComuneParticellaVO(comuneVO);
		    	storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
		    	storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
		    	storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
		    	storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
		    	storicoParticellaVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
		    	conduzioneParticellaVO.setIdConduzioneParticella(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));
		    	conduzioneParticellaVO.setSuperficieCondotta(rs.getString("SUPERFICIE_CONDOTTA"));
		    	conduzioneParticellaVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
		    	storicoParticellaVO.setElencoConduzioni((ConduzioneParticellaVO[])new ConduzioneParticellaVO[]{conduzioneParticellaVO});
		    	elencoParticelle.add(storicoParticellaVO);
		    }	
		    rs.close();
		    stmt.close();	      
	    }
	    catch(SQLException exc) {
	    	SolmrLogger.error(this, "getListParticelleByIdDocumentoAllaDichiarazione - SQLException: "+exc+"\n");
	    	throw new DataAccessException(exc.getMessage());
	    }
	    catch(Exception ex) {
	    	SolmrLogger.error(this, "getListParticelleByIdDocumentoAllaDichiarazione - Generic Exception: "+ex+"\n");
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
	    		SolmrLogger.error(this, "getListParticelleByIdDocumentoAllaDichiarazione - SQLException while closing Statement and Connection: "+exc+"\n");
	    		throw new DataAccessException(exc.getMessage());
	    	}
	    	catch(Exception ex) {
	    		SolmrLogger.error(this, "getListParticelleByIdDocumentoAllaDichiarazione - Generic Exception while closing Statement and Connection: "+ex+"\n");
	    		throw new DataAccessException(ex.getMessage());
	    	}
	    }
	    SolmrLogger.debug(this, "Invocated getListParticelleByIdDocumentoAllaDichiarazione method in StoricoParticellaDAO\n");
	    return elencoParticelle;
	}
	
	public Vector<StoricoParticellaVO> getListParticelleByIdDocumento(Long idDocumento, String[] orderBy) 
	  throws DataAccessException 
  {
	  SolmrLogger.debug(this, "Invocating getListParticelleByIdDocumento method in StoricoParticellaDAO\n");
	  Connection conn = null;
	  PreparedStatement stmt = null;
	  Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();

	  try 
	  {
    	SolmrLogger.debug(this, "Creating db-connection in getListParticelleByIdDocumento method in StoricoParticellaDAO\n");
    	conn = getDatasource().getConnection();
    	SolmrLogger.debug(this, "Created db-connection in getListParticelleByIdDocumento method in StoricoParticellaDAO and it values: "+conn+"\n");

    	String query = 
    	  "SELECT  SP.ID_PARTICELLA," +
    	  "        SP.COMUNE, " +
		    "        C.DESCOM, " +
		    "        P.SIGLA_PROVINCIA, " +
		    "        SP.SEZIONE, " +
		    "        SP.FOGLIO, " +
		    "        SP.PARTICELLA, " +
		    "        SP.SUBALTERNO, " +
		    "        CP.ID_CONDUZIONE_PARTICELLA, " +
		    "        SP.SUP_CATASTALE, " +
		    "        SP.ID_CASO_PARTICOLARE, " +
		    "        CP.ID_TITOLO_POSSESSO, " +
		    "        CP.SUPERFICIE_CONDOTTA, " +
		    "        CP.PERCENTUALE_POSSESSO, " +
		    "        DC.DATA_FINE_VALIDITA, " +
        "        DC.NOTE, " +
        "        DC.LAVORAZIONE_PRIORITARIA " + 
	    	"FROM    DB_DOCUMENTO D, " +     
	    	"        DB_DOCUMENTO_CONDUZIONE DC, " +
		    "        DB_CONDUZIONE_PARTICELLA CP, " +
		    "        DB_STORICO_PARTICELLA SP, " +
		    "        COMUNE C, " +
		    "        PROVINCIA P " +
		    "WHERE   D.ID_DOCUMENTO = ? " +
		    "AND      D.ID_DOCUMENTO = DC.ID_DOCUMENTO " +  
		    "AND     DC.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA " +
		    "AND     CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
		    "AND     SP.COMUNE = C.ISTAT_COMUNE " +
		    "AND     C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
		    "AND     (SP.DATA_FINE_VALIDITA IS NULL " +
		    "OR      (SP.DATA_INIZIO_VALIDITA <= DC.DATA_INIZIO_VALIDITA " +
		    "AND     NVL (SP.DATA_FINE_VALIDITA, TO_DATE ('31/12/9999', 'DD/MM/YYYY')) >= NVL (DC.DATA_FINE_VALIDITA, TO_DATE ('31/12/9999', 'DD/MM/YYYY')))) " +
		    /*"AND     NVL(DC.DATA_FINE_VALIDITA,TO_DATE('31/12/9999','dd/mm/yyyy')) = (SELECT NVL(MAX(DATA_FINE_VALIDITA),TO_DATE('31/12/9999','dd/mm/yyyy')) " +
        "                                                                         FROM   DB_DOCUMENTO_CONDUZIONE DC2 " +
        "                                                                         WHERE  DC2.ID_DOCUMENTO = D.ID_DOCUMENTO " +
        "                                                                         AND    DC2.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA " +
        "                                                                         ) " +*/
		    "GROUP BY SP.ID_PARTICELLA," +
		    "         SP.COMUNE, " +
		    "         C.DESCOM, " +
		    "         P.SIGLA_PROVINCIA, " +
		    "         SP.SEZIONE, " +
		    "         SP.FOGLIO, " +
		    "         SP.PARTICELLA, " +
		    "         SP.SUBALTERNO, " +
        "         CP.ID_CONDUZIONE_PARTICELLA, " +
		    "         SP.SUP_CATASTALE, " +
		    "         SP.ID_CASO_PARTICOLARE, " +
		    "         CP.ID_TITOLO_POSSESSO, " +
		    "         CP.SUPERFICIE_CONDOTTA, " +
		    "         CP.PERCENTUALE_POSSESSO," +
		    "         DC.DATA_FINE_VALIDITA, " +
        "         DC.NOTE, " +
        "         DC.LAVORAZIONE_PRIORITARIA ";
	    	
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
				ordinamento = "ORDER BY "+criterio+",DC.DATA_FINE_VALIDITA";
			}
			if(!ordinamento.equals("")) 
			{
				query += ordinamento;
			}
	    	
	    stmt = conn.prepareStatement(query);

	    SolmrLogger.debug(this, "Value of parameter 1 [ID_DOCUMENTO] in getListParticelleByIdDocumento method in StoricoParticellaDAO: "+idDocumento+"\n");
	    	
	    stmt.setLong(1, idDocumento.longValue());

	    SolmrLogger.debug(this, "Executing getListParticelleByIdDocumento in StoricoParticellaDAO: "+query+"\n");

		  ResultSet rs = stmt.executeQuery();

		  while(rs.next()) 
		  {
		   	StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
		   	ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
		   	DocumentoConduzioneVO documentoConduzioneVO = new DocumentoConduzioneVO();
		   	storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
		   	ComuneVO comuneVO = new ComuneVO();
		   	comuneVO.setDescom(rs.getString("DESCOM"));
		   	comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
		   	storicoParticellaVO.setIdParticella(checkLongNull(rs.getString("id_particella")));
		   	storicoParticellaVO.setComuneParticellaVO(comuneVO);
		   	storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
		   	storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
		   	storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
		   	storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
		   	storicoParticellaVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
		   	if(Validator.isNotEmpty(rs.getString("ID_CASO_PARTICOLARE"))) 
		   	{
		   		storicoParticellaVO.setIdCasoParticolare(new Long(rs.getLong("ID_CASO_PARTICOLARE")));
		    }
		    conduzioneParticellaVO.setIdConduzioneParticella(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));
		    conduzioneParticellaVO.setSuperficieCondotta(rs.getString("SUPERFICIE_CONDOTTA"));
		    conduzioneParticellaVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
		    conduzioneParticellaVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
		    documentoConduzioneVO.setMaxDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
		    documentoConduzioneVO.setNote(rs.getString("NOTE"));
		    documentoConduzioneVO.setLavorazionePrioritaria(rs.getString("LAVORAZIONE_PRIORITARIA"));
		    conduzioneParticellaVO.setElencoDocumentoConduzione((DocumentoConduzioneVO[])new DocumentoConduzioneVO[]{documentoConduzioneVO});
		    storicoParticellaVO.setElencoConduzioni((ConduzioneParticellaVO[])new ConduzioneParticellaVO[]{conduzioneParticellaVO});
		    elencoParticelle.add(storicoParticellaVO);
		  }	
		  rs.close();
		  stmt.close();	      
	  }
	  catch(SQLException exc) 
	  {
	   	SolmrLogger.error(this, "getListParticelleByIdDocumento - SQLException: "+exc+"\n");
	   	throw new DataAccessException(exc.getMessage());
	  }
	  catch(Exception ex) 
	  {
	   	SolmrLogger.error(this, "getListParticelleByIdDocumento - Generic Exception: "+ex+"\n");
	   	throw new DataAccessException(ex.getMessage());
	  }
	  finally 
	  {
	   	try 
	   	{
	      if(stmt != null) 
	      {
	      	stmt.close();
	      }
	      if(conn != null) 
	      {
	      	conn.close();
	      }
	    }
	    catch(SQLException exc) 
	    {
	    	SolmrLogger.error(this, "getListParticelleByIdDocumento - SQLException while closing Statement and Connection: "+exc+"\n");
	    	throw new DataAccessException(exc.getMessage());
	    }
	    catch(Exception ex) 
	    {
	    	SolmrLogger.error(this, "getListParticelleByIdDocumento - Generic Exception while closing Statement and Connection: "+ex+"\n");
	    	throw new DataAccessException(ex.getMessage());
	    }
	  }
	  SolmrLogger.debug(this, "Invocated getListParticelleByIdDocumento method in StoricoParticellaDAO\n");
	  return elencoParticelle;
	}
	
	/**
	 * Metodo per effettuare il riepilogo delle particelle per titolo di possesso
	 * e comune relativi ad un'azienda agricola
	 * 
	 * @param idAzienda
	 * @param onlyActive
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
	 * @throws DataAccessException
	 */
	public StoricoParticellaVO[] riepilogoPossessoComune(Long idAzienda) 
	  throws DataAccessException 
	{
	  SolmrLogger.debug(this, "Invocating riepilogoPossessoComune method in StoricoParticellaDAO\n");
	  Connection conn = null;
	  PreparedStatement stmt = null;
	  Vector<StoricoParticellaVO> elencoParticelle = null;

	  try 
	  {

    	SolmrLogger.debug(this, "Creating db-connection in riepilogoPossessoComune method in StoricoParticellaDAO\n");
    	conn = getDatasource().getConnection();
    	SolmrLogger.debug(this, "Created db-connection in riepilogoPossessoComune method in StoricoParticellaDAO and it values: "+conn+"\n");

    	String query = " " +
    	  "SELECT COND.ID_TITOLO_POSSESSO, " +
        "       COND.DESCRIZIONE, " +
        "       COND.COMUNE, " +
        "       COND.DESCOM, " +
        "       COND.SIGLA_PROVINCIA, " + 
        "       SUM(SUP_COND) AS SUP_CONDOTTA, " +
        "       NVL(SUM(SUP_UTILIZZATA),0) AS SUP_UTILIZZATA " +
        "FROM " +
        "      (SELECT CP.ID_TITOLO_POSSESSO, " +
        "              TTP.DESCRIZIONE, " +
        "              CP.SUPERFICIE_CONDOTTA SUP_COND, " +
        "              CP.ID_CONDUZIONE_PARTICELLA, " +
        "              SP.COMUNE, " +
        "              C.DESCOM, " +
        "              P.SIGLA_PROVINCIA, " +
        "              SP.ID_PARTICELLA, " +
        "              SUM(UP.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
        "       FROM   DB_CONDUZIONE_PARTICELLA CP, " +
        "              DB_STORICO_PARTICELLA SP, " +
        "              DB_UTE U, " +
        "              DB_TIPO_TITOLO_POSSESSO TTP, " +
        "              DB_UTILIZZO_PARTICELLA UP, " +
        "              COMUNE C, " +
        "              PROVINCIA P " +
        "       WHERE  U.ID_AZIENDA = ? " +
        "       AND    U.ID_UTE = CP.ID_UTE " +
        "       AND    CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO " +
        "       AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA(+) " +
        "       AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
        "       AND    SP.COMUNE = C.ISTAT_COMUNE " +
        "       AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
        "       AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "       AND    U.DATA_FINE_ATTIVITA IS NULL " +
        "       AND    SP.DATA_FINE_VALIDITA   IS NULL " +
        "       GROUP BY CP.ID_TITOLO_POSSESSO, " +
        "                TTP.DESCRIZIONE, " +
        "                CP.SUPERFICIE_CONDOTTA, " + 
        "                CP.ID_CONDUZIONE_PARTICELLA, " + 
        "                SP.COMUNE, " +
        "                C.DESCOM, " +
        "                P.SIGLA_PROVINCIA, " + 
        "                SP.ID_PARTICELLA " +
        "       ) COND " +
        "GROUP BY COND.ID_TITOLO_POSSESSO, " + 
        "         COND.DESCRIZIONE, " +
        "         COND.COMUNE, " +
        "         COND.DESCOM, " +
        "         COND.SIGLA_PROVINCIA " +
        "ORDER BY DESCRIZIONE ASC";
			
			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoPossessoComune method in StoricoParticellaDAO: "+idAzienda+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());

	    SolmrLogger.debug(this, "Executing riepilogoPossessoComune:"+query+"\n");

	    ResultSet rs = stmt.executeQuery();

	    elencoParticelle = new Vector<StoricoParticellaVO>();

	    while(rs.next()) 
	    {
	    	StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
	    	ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
        UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
	    	storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
	    	ComuneVO comuneVO = new ComuneVO();
	    	comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
	    	comuneVO.setDescom(rs.getString("DESCOM"));
	    	storicoParticellaVO.setComuneParticellaVO(comuneVO);
	    	conduzioneParticellaVO.setSuperficieCondotta(rs.getString("SUP_CONDOTTA"));
	    	conduzioneParticellaVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
	    	CodeDescription titoloPossesso = new CodeDescription(new Integer(rs.getInt("ID_TITOLO_POSSESSO")), rs.getString("DESCRIZIONE"));
	    	conduzioneParticellaVO.setTitoloPossesso(titoloPossesso);
	    	utilizzoParticellaVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
        conduzioneParticellaVO.setElencoUtilizzi(new UtilizzoParticellaVO[]{utilizzoParticellaVO});
	    	storicoParticellaVO.setElencoConduzioni(new ConduzioneParticellaVO[]{conduzioneParticellaVO});
	    	elencoParticelle.add(storicoParticellaVO);
	    }	
	    
	    rs.close();
	    stmt.close();	      
    }
    catch(SQLException exc) 
    {
    	SolmrLogger.error(this, "riepilogoPossessoComune - SQLException: "+exc+"\n");
    	throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
    	SolmrLogger.error(this, "riepilogoPossessoComune - Generic Exception: "+ex+"\n");
    	throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
    	try 
    	{
    		if(stmt != null) {
        		stmt.close();
        	}
        	if(conn != null) {
        		conn.close();
        	}
    	}
    	catch(SQLException exc) {
    		SolmrLogger.error(this, "riepilogoPossessoComune - SQLException while closing Statement and Connection: "+exc+"\n");
    		throw new DataAccessException(exc.getMessage());
    	}
    	catch(Exception ex) {
    		SolmrLogger.error(this, "riepilogoPossessoComune - Generic Exception while closing Statement and Connection: "+ex+"\n");
    		throw new DataAccessException(ex.getMessage());
    	}
	  }
	  SolmrLogger.debug(this, "Invocated riepilogoPossessoComune method in StoricoParticellaDAO\n");
	  if(elencoParticelle.size() == 0) 
	  {
			return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
		}
		else 
		{
			return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
		}
	}
	
	/**
	 * Metodo per effettuare il riepilogo delle particelle per titolo di possesso
	 * e comune relativi ad un'azienda agricola ad una determinata dichiarazione
	 * di consistenza
	 * 
	 * @param idAzienda
	 * @param idDichiarazioneConsistenza
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
	 * @throws DataAccessException
	 */
	public StoricoParticellaVO[] riepilogoPossessoComuneDichiarato(Long idAzienda, Long idDichiarazioneConsistenza) 
	  throws DataAccessException 
	{
	  SolmrLogger.debug(this, "Invocating riepilogoPossessoComuneDichiarato method in StoricoParticellaDAO\n");
	  Connection conn = null;
	  PreparedStatement stmt = null;
	  Vector<StoricoParticellaVO> elencoParticelle = null;

	  try 
	  {

	   	SolmrLogger.debug(this, "Creating db-connection in riepilogoPossessoComuneDichiarato method in StoricoParticellaDAO\n");
	   	conn = getDatasource().getConnection();
	   	SolmrLogger.debug(this, "Created db-connection in riepilogoPossessoComuneDichiarato method in StoricoParticellaDAO and it values: "+conn+"\n");

    	String query = "" +
		    "SELECT COND.ID_TITOLO_POSSESSO, " +
        "       COND.DESCRIZIONE, " +
        "       COND.COMUNE, " +
        "       COND.DESCOM, " +
        "       COND.SIGLA_PROVINCIA, " +
        "       SUM(SUP_COND) AS SUP_CONDOTTA, "+
        "       NVL(SUM(SUP_UTILIZZATA),0) AS SUP_UTILIZZATA " +
        "FROM " +
        "       (SELECT CD.ID_TITOLO_POSSESSO, " +
        "               TTP.DESCRIZIONE, " +
        "               CD.SUPERFICIE_CONDOTTA SUP_COND, " +
        "               CD.ID_CONDUZIONE_DICHIARATA, " +
        "               SP.COMUNE, " +
        "               C.DESCOM, " +
        "               P.SIGLA_PROVINCIA, " +
        "               SP.ID_STORICO_PARTICELLA, " +
        "               SUM(UD.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
        "        FROM   DB_TIPO_TITOLO_POSSESSO TTP, " +
        "               DB_CONDUZIONE_DICHIARATA CD, " +
        "               DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "               DB_UTILIZZO_DICHIARATO UD, " +
        "               DB_STORICO_PARTICELLA SP, " +
        "               COMUNE C, " +
        "               PROVINCIA P " +
        "        WHERE  CD.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO " +
        "        AND    CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
        "        AND    DC.ID_AZIENDA = ? " +
        "        AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "        AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA(+) " +
        "        AND    C.ISTAT_COMUNE = SP.COMUNE " +
        "        AND    P.ISTAT_PROVINCIA = C.ISTAT_PROVINCIA " +
        "        AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        "        GROUP BY CD.ID_TITOLO_POSSESSO, " +
        "                 TTP.DESCRIZIONE, " +
        "                 CD.SUPERFICIE_CONDOTTA, " +
        "                 CD.ID_CONDUZIONE_DICHIARATA, " +
        "                 SP.COMUNE, " +
        "                 C.DESCOM, " +
        "                 P.SIGLA_PROVINCIA, " +
        "                 SP.ID_STORICO_PARTICELLA) COND " +
        "GROUP BY COND.ID_TITOLO_POSSESSO, " +
        "         COND.DESCRIZIONE, " +
        "         COND.COMUNE,  " +
        "         COND.DESCOM, " +
        "         COND.SIGLA_PROVINCIA " +
        "ORDER BY COND.DESCRIZIONE ASC, " +
        "         COND.DESCOM ASC";
	    	
			
			
			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoPossessoComuneDichiarato method in StoricoParticellaDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ID_DICHIARAZIONE_CONSISTENZA] in riepilogoPossessoComuneDichiarato method in StoricoParticellaDAO: "+idDichiarazioneConsistenza+"\n");
			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());
			stmt.setLong(2, idDichiarazioneConsistenza.longValue());
			
		  SolmrLogger.debug(this, "Executing riepilogoPossessoComuneDichiarato:"+query+"\n");

		  ResultSet rs = stmt.executeQuery();

		  elencoParticelle = new Vector<StoricoParticellaVO>();

		  while(rs.next()) 
		  {
		   	StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
		   	ConduzioneDichiarataVO conduzioneDichiarataVO = new ConduzioneDichiarataVO();
		   	UtilizzoDichiaratoVO utilizzoDichiaratoVO = new UtilizzoDichiaratoVO();
		   	storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
		   	ComuneVO comuneVO = new ComuneVO();
		   	comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
		   	comuneVO.setDescom(rs.getString("DESCOM"));
		   	storicoParticellaVO.setComuneParticellaVO(comuneVO);
		   	conduzioneDichiarataVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
		   	conduzioneDichiarataVO.setSuperficieCondotta(rs.getString("SUP_CONDOTTA"));
		   	CodeDescription titoloPossesso = new CodeDescription(new Integer(rs.getInt("ID_TITOLO_POSSESSO")), rs.getString("DESCRIZIONE"));
		   	conduzioneDichiarataVO.setTitoloPossesso(titoloPossesso);
		   	utilizzoDichiaratoVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
        conduzioneDichiarataVO.setElencoUtilizzi(new UtilizzoDichiaratoVO[]{utilizzoDichiaratoVO});
		   	storicoParticellaVO.setElencoConduzioniDichiarate(new ConduzioneDichiarataVO[]{conduzioneDichiarataVO});
		   	elencoParticelle.add(storicoParticellaVO);
		  }	
		    
		  rs.close();
		  stmt.close();	      
	  }
	  catch(SQLException exc) 
	  {
	   	SolmrLogger.error(this, "riepilogoPossessoComuneDichiarato - SQLException: "+exc+"\n");
	   	throw new DataAccessException(exc.getMessage());
	  }
	  catch(Exception ex) 
	  {
	   	SolmrLogger.error(this, "riepilogoPossessoComuneDichiarato - Generic Exception: "+ex+"\n");
	   	throw new DataAccessException(ex.getMessage());
	  }
	  finally 
	  {
	  	try 
	  	{
    		if(stmt != null) 
    		{
        	stmt.close();
        }
        if(conn != null) 
        {
        	conn.close();
        }
	    }
	    catch(SQLException exc) 
	    {
	    	SolmrLogger.error(this, "riepilogoPossessoComuneDichiarato - SQLException while closing Statement and Connection: "+exc+"\n");
	    	throw new DataAccessException(exc.getMessage());
	    }
	    catch(Exception ex) 
	    {
	    	SolmrLogger.error(this, "riepilogoPossessoComuneDichiarato - Generic Exception while closing Statement and Connection: "+ex+"\n");
	    	throw new DataAccessException(ex.getMessage());
	    }
	  }
	  SolmrLogger.debug(this, "Invocated riepilogoPossessoComuneDichiarato method in StoricoParticellaDAO\n");
	  if(elencoParticelle.size() == 0) 
	  {
			return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
		}
		else 
		{
			return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
		}
	}
	
	/**
	 * Metodo per effettuare il riepilogo delle particelle per comune relative ad un'azienda agricola
	 * 
	 * @param idAzienda
	 * @param escludiAsservimento
	 * @param onlyActive
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
	 * @throws DataAccessException
	 */
	public StoricoParticellaVO[] riepilogoComune(Long idAzienda, String escludiAsservimento) 
	  throws DataAccessException 
	{
	  SolmrLogger.debug(this, "Invocating riepilogoComune method in StoricoParticellaDAO\n");
	  Connection conn = null;
	  PreparedStatement stmt = null;
	  Vector<StoricoParticellaVO> elencoParticelle = null;

	  try 
	  {
    	SolmrLogger.debug(this, "Creating db-connection in riepilogoComune method in StoricoParticellaDAO\n");
    	conn = getDatasource().getConnection();
	   	SolmrLogger.debug(this, "Created db-connection in riepilogoComune method in StoricoParticellaDAO and it values: "+conn+"\n");

	  	String query = 
	  	  "SELECT COM.COMUNE, " +
        "       COM.DESCOM, " +
        "       COM.SIGLA_PROVINCIA, " +
        "       NVL(SUM(DECODE(COM.ID_TITOLO_POSSESSO,5,COM.SUPERFICIE_CONDOTTA,COM.SUP_UTILIZZATA)),0) AS SUP_UTILIZZATA " +
        "FROM " +
        "       (SELECT SP.COMUNE, " +
        "               C.DESCOM, " +
        "               P.SIGLA_PROVINCIA, " +
        "               SP.ID_PARTICELLA, " +
        "               CP.SUPERFICIE_CONDOTTA, " +
        "               CP.ID_CONDUZIONE_PARTICELLA, " +
        "               CP.ID_TITOLO_POSSESSO, " +
        "               SUM(UP.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
        "        FROM   DB_CONDUZIONE_PARTICELLA CP, " +
        "               DB_UTE U, " +
        "               DB_STORICO_PARTICELLA SP, " +
        "               DB_UTILIZZO_PARTICELLA UP, " +
        "               COMUNE C, " +
        "               PROVINCIA P " +
        "        WHERE  CP.ID_UTE = U.ID_UTE " +
        "        AND    U.ID_AZIENDA = ? " +
        "        AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
        "        AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA(+)";
      if(Validator.isNotEmpty(escludiAsservimento) && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {
        query +=   
          "      AND    CP.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
      }
      query +=
        "        AND    C.ISTAT_COMUNE = SP.COMUNE " +
        "        AND    P.ISTAT_PROVINCIA = C.ISTAT_PROVINCIA " +
        "        AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "        AND    SP.DATA_FINE_VALIDITA IS NULL " +
        "        AND    U.DATA_FINE_ATTIVITA IS NULL " +
        "        GROUP BY SP.COMUNE, " +
        "                 C.DESCOM, " +
        "                 P.SIGLA_PROVINCIA, " + 
        "                 SP.ID_PARTICELLA, " +
        "                 CP.SUPERFICIE_CONDOTTA, " + 
        "                 CP.ID_CONDUZIONE_PARTICELLA, " + 
        "                 CP.ID_TITOLO_POSSESSO " +
        "  ) COM " +
        "GROUP BY COM.COMUNE, " + 
        "         COM.DESCOM, " +
        "         COM.SIGLA_PROVINCIA " +
        "ORDER BY COM.DESCOM ASC"; 
			
			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoComune method in StoricoParticellaDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ESCLUDI_ASSERVIMENTO] in riepilogoComune method in StoricoParticellaDAO: "+escludiAsservimento+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());

	    SolmrLogger.debug(this, "Executing riepilogoComune:"+query+"\n");

	    ResultSet rs = stmt.executeQuery();

	    elencoParticelle = new Vector<StoricoParticellaVO>();

	    while(rs.next()) 
	    {
	    	StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
	    	ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
	    	UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
	    	storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
	    	ComuneVO comuneVO = new ComuneVO();
	    	comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
	    	comuneVO.setDescom(rs.getString("DESCOM"));
	    	storicoParticellaVO.setComuneParticellaVO(comuneVO);
	    	utilizzoParticellaVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
        conduzioneParticellaVO.setElencoUtilizzi(new UtilizzoParticellaVO[]{utilizzoParticellaVO});
	    	storicoParticellaVO.setElencoConduzioni(new ConduzioneParticellaVO[]{conduzioneParticellaVO});
	    	elencoParticelle.add(storicoParticellaVO);
	    }	
		    
	    rs.close();
	    stmt.close();	      
    }
    catch(SQLException exc) 
    {
    	SolmrLogger.error(this, "riepilogoComune - SQLException: "+exc+"\n");
    	throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) 
    {
    	SolmrLogger.error(this, "riepilogoComune - Generic Exception: "+ex+"\n");
    	throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
    	try 
    	{
    		if(stmt != null) 
    		{
        	stmt.close();
        }
        if(conn != null) 
        {
        	conn.close();
        }
    	}
    	catch(SQLException exc) {
    		SolmrLogger.error(this, "riepilogoComune - SQLException while closing Statement and Connection: "+exc+"\n");
    		throw new DataAccessException(exc.getMessage());
    	}
    	catch(Exception ex) {
    		SolmrLogger.error(this, "riepilogoComune - Generic Exception while closing Statement and Connection: "+ex+"\n");
    		throw new DataAccessException(ex.getMessage());
    	}
    }
    SolmrLogger.debug(this, "Invocated riepilogoComune method in StoricoParticellaDAO\n");
    if(elencoParticelle.size() == 0) 
    {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
    }
    else 
    {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
    }
	}
	
	/**
	 * Metodo per effettuare il riepilogo delle particelle per comune relative ad un'azienda agricola
	 * ad una determinata dichiarazione di consistenza
	 * 
	 * @param idAzienda
	 * @param escludiAsservimento
	 * @param idDichiarazioneConsistenza
	 * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
	 * @throws DataAccessException
	 */
	public StoricoParticellaVO[] riepilogoComuneDichiarato(
	    Long idAzienda, String escludiAsservimento, Long idDichiarazioneConsistenza) 
	throws DataAccessException 
	{
    SolmrLogger.debug(this, "Invocating riepilogoComuneDichiarato method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelle = null;

    try 
    {

    	SolmrLogger.debug(this, "Creating db-connection in riepilogoComuneDichiarato method in StoricoParticellaDAO\n");
    	conn = getDatasource().getConnection();
    	SolmrLogger.debug(this, "Created db-connection in riepilogoComuneDichiarato method in StoricoParticellaDAO and it values: "+conn+"\n");

    	String query = 
    	  "SELECT COM.COMUNE, " +
        "       COM.DESCOM, " +
        "       COM.SIGLA_PROVINCIA, " +
        "       NVL(SUM(DECODE(COM.ID_TITOLO_POSSESSO,5,COM.SUPERFICIE_CONDOTTA,COM.SUP_UTILIZZATA)),0) AS SUP_UTILIZZATA " +
        "FROM " +
        "       (SELECT SP.COMUNE, " +
        "               C.DESCOM, " +
        "               P.SIGLA_PROVINCIA, " +
        "               SP.ID_STORICO_PARTICELLA, " +
        "               CD.SUPERFICIE_CONDOTTA, " +
        "               CD.ID_CONDUZIONE_PARTICELLA, " +
        "               CD.ID_TITOLO_POSSESSO, " +
        "               SUM(UD.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
        "        FROM   DB_CONDUZIONE_DICHIARATA CD, " +
        "               DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "               DB_STORICO_PARTICELLA SP, " +
        "               DB_UTILIZZO_DICHIARATO UD, " +
        "               COMUNE C, " +
        "               PROVINCIA P " +
        "        WHERE  CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
        "        AND    DC.ID_AZIENDA  = ? " +
        "        AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "        AND    C.ISTAT_COMUNE = SP.COMUNE " +
        "        AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA(+) ";
    	if(Validator.isNotEmpty(escludiAsservimento) && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
    	{
        query +=   
          "      AND      CD.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
      }
    	
    	query +=
    	  "        AND    P.ISTAT_PROVINCIA = C.ISTAT_PROVINCIA " +
        "        AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        "        GROUP BY SP.COMUNE, " +
        "                 C.DESCOM, " +
        "                 P.SIGLA_PROVINCIA, " +
        "                 SP.ID_STORICO_PARTICELLA, " +
        "                 CD.SUPERFICIE_CONDOTTA, " +
        "                 CD.ID_CONDUZIONE_PARTICELLA, " +
        "                 CD.ID_TITOLO_POSSESSO " +
        "        ) COM " + 
        "GROUP BY COM.COMUNE, " +
        "         COM.DESCOM, " +
        "         COM.SIGLA_PROVINCIA " +             
        "ORDER BY COM.DESCOM ASC";
			
			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoComuneDichiarato method in StoricoParticellaDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ESCLUDI_ASSERVIMENTO] in riepilogoComuneDichiarato method in StoricoParticellaDAO: "+escludiAsservimento+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ID_DICHIARAZIONE_CONSISTENZA] in riepilogoComuneDichiarato method in StoricoParticellaDAO: "+idDichiarazioneConsistenza+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());
			stmt.setLong(2, idDichiarazioneConsistenza.longValue());

	    SolmrLogger.debug(this, "Executing riepilogoComuneDichiarato:"+query+"\n");

	    ResultSet rs = stmt.executeQuery();

	    elencoParticelle = new Vector<StoricoParticellaVO>();

	    while(rs.next()) 
	    {
	    	StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
	    	ConduzioneDichiarataVO conduzioneDichiarataVO = new ConduzioneDichiarataVO();
	    	UtilizzoDichiaratoVO utilizzoDichiaratoVO = new UtilizzoDichiaratoVO();
	    	storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
	    	ComuneVO comuneVO = new ComuneVO();
	    	comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
	    	comuneVO.setDescom(rs.getString("DESCOM"));
	    	storicoParticellaVO.setComuneParticellaVO(comuneVO);
	    	utilizzoDichiaratoVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
        conduzioneDichiarataVO.setElencoUtilizzi(new UtilizzoDichiaratoVO[]{utilizzoDichiaratoVO});
	    	storicoParticellaVO.setElencoConduzioniDichiarate(new ConduzioneDichiarataVO[]{conduzioneDichiarataVO});
	    	elencoParticelle.add(storicoParticellaVO);
	    }	
		    
	    rs.close();
	    stmt.close();	      
    }
    catch(SQLException exc) 
    {
    	SolmrLogger.error(this, "riepilogoComuneDichiarato - SQLException: "+exc+"\n");
    	throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
    	SolmrLogger.error(this, "riepilogoComuneDichiarato - Generic Exception: "+ex+"\n");
    	throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
    	try 
    	{
    		if(stmt != null) 
    		{
        	stmt.close();
        }
        if(conn != null) 
        {
        	conn.close();
        }
    	}
    	catch(SQLException exc) {
    		SolmrLogger.error(this, "riepilogoComuneDichiarato - SQLException while closing Statement and Connection: "+exc+"\n");
    		throw new DataAccessException(exc.getMessage());
    	}
    	catch(Exception ex) {
    		SolmrLogger.error(this, "riepilogoComuneDichiarato - Generic Exception while closing Statement and Connection: "+ex+"\n");
    		throw new DataAccessException(ex.getMessage());
    	}
    }
    SolmrLogger.debug(this, "Invocated riepilogoComuneDichiarato method in StoricoParticellaDAO\n");
    if(elencoParticelle.size() == 0) 
    {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
		}
		else 
		{
			return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
		}
	}
	
	/**
	 * Metodo per effettuare il riepilogo delle particelle per ZVF solo dei comuni
	 * TOBECONFIGsi
	 * 
	 * @param idAzienda
	 * @param escludiAsservimento
	 * @param onlyActive
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
	 * @throws DataAccessException
	 */
	public StoricoParticellaVO[] riepilogoZVFParticellePiemonte(Long idAzienda, String escludiAsservimento) 
	  throws DataAccessException 
	{
    SolmrLogger.debug(this, "Invocating riepilogoZVFParticelleTOBECONFIG method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();

    try 
    {

    	SolmrLogger.debug(this, "Creating db-connection in riepilogoZVFParticelleTOBECONFIG method in StoricoParticellaDAO\n");
    	conn = getDatasource().getConnection();
    	SolmrLogger.debug(this, "Created db-connection in riepilogoZVFParticelleTOBECONFIG method in StoricoParticellaDAO and it values: "+conn+"\n");

    	String query = 
    	  " SELECT COM.ID_AREA_F, " +
			  "        COM.DESCRIZIONE, " +
			  "        NVL(SUM(DECODE(COM.ID_TITOLO_POSSESSO,5,COM.SUPERFICIE_CONDOTTA,COM.SUP_UTILIZZATA)),0) AS SUP_UTILIZZATA " +
			  " FROM   (SELECT F.ID_AREA_F ID_AREA_F, " + 
			  "                TAF.DESCRIZIONE DESCRIZIONE, " +
			  "                CP.SUPERFICIE_CONDOTTA, " +
        "                CP.ID_TITOLO_POSSESSO, " +
			  "                CP.ID_CONDUZIONE_PARTICELLA, " +
			  "                SUM (UP.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
			  "         FROM   DB_CONDUZIONE_PARTICELLA CP, " +
			  "                DB_UTILIZZO_PARTICELLA UP, " +
			  "                DB_UTE U, " +
			  "                DB_STORICO_PARTICELLA SP, " +
			  "                DB_FOGLIO F, " +
			  "                DB_TIPO_AREA_F TAF " +
			  "         WHERE  U.ID_AZIENDA = ? " +
			  "         AND    U.ID_UTE = CP.ID_UTE " +
			  "         AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
			  "         AND    SP.COMUNE = F.COMUNE " +
			  "         AND    NVL (SP.SEZIONE, '-') = NVL (F.SEZIONE, '-') " +
			  "         AND    SP.FOGLIO = F.FOGLIO " +
			  "         AND    F.ID_AREA_F = TAF.ID_AREA_F " +
			  "         AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA(+) " +
    	 	"         AND    U.DATA_FINE_ATTIVITA IS NULL " +
    		"         AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
    		"         AND    SP.DATA_FINE_VALIDITA IS NULL ";    
    	if(Validator.isNotEmpty(escludiAsservimento) && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
    	{
    		query +=   
    		"         AND    CP.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
    	}
    	query +=       
    	  "         GROUP BY F.ID_AREA_F, " +
    	  "                  TAF.DESCRIZIONE, " +
    		"                  CP.SUPERFICIE_CONDOTTA, " +
    		"                  CP.ID_TITOLO_POSSESSO, " +
    		"                  CP.ID_CONDUZIONE_PARTICELLA) COM " +
    		" GROUP BY COM.ID_AREA_F, " +
    		"          COM.DESCRIZIONE " +
    	  " ORDER BY COM.DESCRIZIONE ASC ";
			
			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoZVFParticelleTOBECONFIG method in StoricoParticellaDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ESCLUDI_ASSERVIMENTO] in riepilogoZVFParticelleTOBECONFIG method in StoricoParticellaDAO: "+escludiAsservimento+"\n");
			

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());

	    SolmrLogger.debug(this, "Executing riepilogoZVFParticelleTOBECONFIG:"+query+"\n");

	    ResultSet rs = stmt.executeQuery();

	    while(rs.next()) 
	    {
	    	StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
	    	FoglioVO foglioVO = new FoglioVO();
	    	ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
	    	UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
	    	foglioVO.setIdAreaF(new Long(rs.getLong("ID_AREA_F")));
	    	foglioVO.setDescrizioneAreaF(rs.getString("DESCRIZIONE"));
	    	storicoParticellaVO.setFoglioVO(foglioVO);
	    	utilizzoParticellaVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
	    	conduzioneParticellaVO.setElencoUtilizzi(new UtilizzoParticellaVO[]{utilizzoParticellaVO});
	    	storicoParticellaVO.setElencoConduzioni(new ConduzioneParticellaVO[]{conduzioneParticellaVO});
	    	elencoParticelle.add(storicoParticellaVO);
	    }	
	    
	    rs.close();
	    stmt.close();	      
    }
    catch(SQLException exc) {
    	SolmrLogger.error(this, "riepilogoZVFParticelleTOBECONFIG - SQLException: "+exc+"\n");
    	throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
    	SolmrLogger.error(this, "riepilogoZVFParticelleTOBECONFIG - Generic Exception: "+ex+"\n");
    	throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
    	try 
    	{
    		if(stmt != null) 
    		{
        	stmt.close();
        }
        if(conn != null) 
        {
        	conn.close();
        }
    	}
    	catch(SQLException exc) {
    		SolmrLogger.error(this, "riepilogoZVFParticelleTOBECONFIG - SQLException while closing Statement and Connection: "+exc+"\n");
    		throw new DataAccessException(exc.getMessage());
    	}
    	catch(Exception ex) {
    		SolmrLogger.error(this, "riepilogoZVFParticelleTOBECONFIG - Generic Exception while closing Statement and Connection: "+ex+"\n");
    		throw new DataAccessException(ex.getMessage());
    	}
    }
    SolmrLogger.debug(this, "Invocated riepilogoZVFParticelleTOBECONFIG method in StoricoParticellaDAO\n");
	  if(elencoParticelle.size() == 0) 
	  {
			return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
		}
		else 
		{
			return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
		}
	}
	
	/**
	 * Metodo per effettuare il riepilogo delle particelle per ZVF solo dei comuni
	 * TOBECONFIGsi ad una determinata dichiarazione di consistenza
	 * 
	 * @param idAzienda
	 * @param escludiAsservimento
	 * @param idDichiarazioneConsistenza
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
	 * @throws DataAccessException
	 */
	public StoricoParticellaVO[] riepilogoZVFParticelleDichiaratePiemonte(Long idAzienda, String escludiAsservimento, Long idDichiarazioneConsistenza) 
	  throws DataAccessException 
	{
    SolmrLogger.debug(this, "Invocating riepilogoZVFParticelleDichiaratePiemonte method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();

	  try 
	  {

    	SolmrLogger.debug(this, "Creating db-connection in riepilogoZVFParticelleDichiaratePiemonte method in StoricoParticellaDAO\n");
    	conn = getDatasource().getConnection();
    	SolmrLogger.debug(this, "Created db-connection in riepilogoZVFParticelleDichiaratePiemonte method in StoricoParticellaDAO and it values: "+conn+"\n");
    	
    	
    	String query = 
    	  " SELECT COM.ID_AREA_F, " +
		    "        COM.DESCRIZIONE, " +
		    "        NVL(SUM(DECODE(COM.ID_TITOLO_POSSESSO,5,COM.SUPERFICIE_CONDOTTA,COM.SUP_UTILIZZATA)),0) AS SUP_UTILIZZATA " +
		    " FROM   (SELECT F.ID_AREA_F ID_AREA_F, " + 
		    "                TAF.DESCRIZIONE DESCRIZIONE, " +
		    "                CD.SUPERFICIE_CONDOTTA, " +
		    "                CD.ID_TITOLO_POSSESSO, " +
		    "                CD.ID_CONDUZIONE_DICHIARATA, " +
		    "                SUM(UD.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
		    "         FROM   DB_CONDUZIONE_DICHIARATA CD, " +
		    "                DB_UTILIZZO_DICHIARATO UD, " +
		    "                DB_DICHIARAZIONE_CONSISTENZA DC, " +
		    "                DB_STORICO_PARTICELLA SP, " +
		    "                DB_FOGLIO F, " +
		    "                DB_TIPO_AREA_F TAF " +
		    "         WHERE  DC.ID_AZIENDA = ? " +
		    "         AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
		    " 		    AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
		    " 		    AND    CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
//		    " 		    AND    TRUNC(SP.DATA_INIZIO_VALIDITA) <= TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
//		    " 		    AND    TRUNC(NVL(SP.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY'))) > TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
		    "		      AND    SP.COMUNE = F.COMUNE " +
		    "		      AND    NVL(SP.SEZIONE,'-') = NVL(F.SEZIONE,'-') " +
		    "		      AND    SP.FOGLIO = F.FOGLIO " +
		    "		      AND    F.ID_AREA_F = TAF.ID_AREA_F " +
		    "		      AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA(+) ";
    	if(Validator.isNotEmpty(escludiAsservimento) 
    	    && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
    	{
    		query +=   
    		"         AND    CD.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
    	}
    	query +=	   
    	  "         GROUP BY F.ID_AREA_F, " +
    		"               TAF.DESCRIZIONE, " +
    		"               CD.SUPERFICIE_CONDOTTA, " +
    		"               CD.ID_TITOLO_POSSESSO, " +
    		"               CD.ID_CONDUZIONE_DICHIARATA) COM " +
    		" GROUP BY COM.ID_AREA_F, " +
    		"          COM.DESCRIZIONE " +
    		" ORDER BY COM.DESCRIZIONE ASC ";
			
			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoZVFParticelleDichiaratePiemonte method in StoricoParticellaDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ESCLUDI_ASSERVIMENTO] in riepilogoZVFParticelleDichiaratePiemonte method in StoricoParticellaDAO: "+escludiAsservimento+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ID_DICHIARAZIONE_CONSISTENZA] in riepilogoZVFParticelleDichiaratePiemonte method in StoricoParticellaDAO: "+idDichiarazioneConsistenza+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());
			stmt.setLong(2, idDichiarazioneConsistenza.longValue());

	    SolmrLogger.debug(this, "Executing riepilogoZVFParticelleDichiaratePiemonte:"+query+"\n");

	    ResultSet rs = stmt.executeQuery();

	    while(rs.next()) 
	    {
	    	StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
	    	FoglioVO foglioVO = new FoglioVO();
	    	ConduzioneDichiarataVO conduzioneDichiarataVO = new ConduzioneDichiarataVO();
	    	UtilizzoDichiaratoVO utilizzoDichiaratoVO = new UtilizzoDichiaratoVO();
	    	foglioVO.setIdAreaF(new Long(rs.getLong("ID_AREA_F")));
	    	foglioVO.setDescrizioneAreaF(rs.getString("DESCRIZIONE"));
	    	storicoParticellaVO.setFoglioVO(foglioVO);
	    	utilizzoDichiaratoVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
	    	conduzioneDichiarataVO.setElencoUtilizzi(new UtilizzoDichiaratoVO[]{utilizzoDichiaratoVO});
	    	storicoParticellaVO.setElencoConduzioniDichiarate(new ConduzioneDichiarataVO[]{conduzioneDichiarataVO});
	    	elencoParticelle.add(storicoParticellaVO);
	    }	
	    
	    rs.close();
	    stmt.close();	      
    }
    catch(SQLException exc) 
    {
    	SolmrLogger.error(this, "riepilogoZVFParticelleDichiaratePiemonte - SQLException: "+exc+"\n");
    	throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
    	SolmrLogger.error(this, "riepilogoZVFParticelleDichiaratePiemonte - Generic Exception: "+ex+"\n");
    	throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
    	try 
    	{
    		if(stmt != null) 
    		{
       		stmt.close();
        }
        if(conn != null) {
        	conn.close();
        }
    	}
    	catch(SQLException exc) {
    		SolmrLogger.error(this, "riepilogoZVFParticelleDichiaratePiemonte - SQLException while closing Statement and Connection: "+exc+"\n");
    		throw new DataAccessException(exc.getMessage());
    	}
    	catch(Exception ex) {
    		SolmrLogger.error(this, "riepilogoZVFParticelleDichiaratePiemonte - Generic Exception while closing Statement and Connection: "+ex+"\n");
    		throw new DataAccessException(ex.getMessage());
    	}
    }
    SolmrLogger.debug(this, "Invocated riepilogoZVFParticelleDichiaratePiemonte method in StoricoParticellaDAO\n");
    if(elencoParticelle.size() == 0) 
    {
			return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
		}
		else 
		{
			return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
		}
	}
	
	/**
	 * Metodo per effettuare il riepilogo delle particelle per ZVN solo dei comuni
	 * TOBECONFIGsi
	 * 
	 * @param idAzienda
	 * @param escludiAsservimento
	 * @param onlyActive
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
	 * @throws DataAccessException
	 */
	public StoricoParticellaVO[] riepilogoZVNParticellePiemonte(Long idAzienda, String escludiAsservimento) throws DataAccessException 
	{
    SolmrLogger.debug(this, "Invocating riepilogoZVNParticellePiemonte method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();

    try 
    {

    	SolmrLogger.debug(this, "Creating db-connection in riepilogoZVNParticellePiemonte method in StoricoParticellaDAO\n");
    	conn = getDatasource().getConnection();
    	SolmrLogger.debug(this, "Created db-connection in riepilogoZVNParticellePiemonte method in StoricoParticellaDAO and it values: "+conn+"\n");

    	String query = "" +
    		"SELECT COM.ID_AREA_E, " +
		    "       COM.DESCRIZIONE, " +
		    "       NVL(SUM(DECODE(COM.ID_TITOLO_POSSESSO,5,COM.SUPERFICIE_CONDOTTA,COM.SUP_UTILIZZATA)),0) AS SUP_UTILIZZATA " +
		    "FROM   (SELECT F.ID_AREA_E ID_AREA_E, " + 
		    "               TAE.DESCRIZIONE DESCRIZIONE, " +
		    "               CP.SUPERFICIE_CONDOTTA, " +
		    "               CP.ID_TITOLO_POSSESSO, " +
		    "               CP.ID_CONDUZIONE_PARTICELLA, " +
		    "               SUM (UP.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
		    "        FROM   DB_CONDUZIONE_PARTICELLA CP, " +
		    "               DB_UTILIZZO_PARTICELLA UP, " +
		    "               DB_UTE U, " +
		    "               DB_STORICO_PARTICELLA SP, " +
		    "               DB_FOGLIO F, " +
		    "                DB_TIPO_AREA_E TAE " +
		    "        WHERE  U.ID_AZIENDA = ? " +
		    "        AND    U.ID_UTE = CP.ID_UTE " +
		    "        AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
		    "        AND    SP.COMUNE = F.COMUNE " +
		    "        AND    NVL (SP.SEZIONE, '-') = NVL (F.SEZIONE, '-') " +
		    "        AND    SP.FOGLIO = F.FOGLIO " +
		    "        AND    F.ID_AREA_E = TAE.ID_AREA_E " +
		    "        AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA(+) " +
    		"        AND    U.DATA_FINE_ATTIVITA IS NULL " +
    		"        AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
    		"        AND    SP.DATA_FINE_VALIDITA IS NULL ";
    	
    	if(Validator.isNotEmpty(escludiAsservimento) && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_S)) {
    		query +=   "         AND    CP.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
    	}
    	query += "" +
    		"        GROUP BY F.ID_AREA_E, " +
    	  "                 TAE.DESCRIZIONE, " +
    		"                 CP.SUPERFICIE_CONDOTTA, " +
    		"                 CP.ID_TITOLO_POSSESSO, " +
    		"                 CP.ID_CONDUZIONE_PARTICELLA) COM " +
    		"GROUP BY COM.ID_AREA_E, COM.DESCRIZIONE " +
    		"ORDER BY COM.DESCRIZIONE ASC ";
		
		  SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoZVNParticellePiemonte method in StoricoParticellaDAO: "+idAzienda+"\n");
		  SolmrLogger.debug(this, "Value of parameter 2 [ESCLUDI_ASSERVIMENTO] in riepilogoZVNParticellePiemonte method in StoricoParticellaDAO: "+escludiAsservimento+"\n");

		  stmt = conn.prepareStatement(query);
		
		  stmt.setLong(1, idAzienda.longValue());

	    SolmrLogger.debug(this, "Executing riepilogoZVNParticellePiemonte:"+query+"\n");

	    ResultSet rs = stmt.executeQuery();

	    while(rs.next()) 
	    {
	    	StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
	    	FoglioVO foglioVO = new FoglioVO();
	    	ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
	    	UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
	    	foglioVO.setIdAreaE(new Long(rs.getLong("ID_AREA_E")));
	    	foglioVO.setDescrizioneAreaE(rs.getString("DESCRIZIONE"));
	    	storicoParticellaVO.setFoglioVO(foglioVO);
	    	utilizzoParticellaVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
	    	conduzioneParticellaVO.setElencoUtilizzi(new UtilizzoParticellaVO[]{utilizzoParticellaVO});
	    	storicoParticellaVO.setElencoConduzioni(new ConduzioneParticellaVO[]{conduzioneParticellaVO});
	    	elencoParticelle.add(storicoParticellaVO);
	    }	
	    
	    rs.close();
	    stmt.close();	      
    }
    catch(SQLException exc) {
    	SolmrLogger.error(this, "riepilogoZVNParticellePiemonte - SQLException: "+exc+"\n");
    	throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
    	SolmrLogger.error(this, "riepilogoZVNParticellePiemonte - Generic Exception: "+ex+"\n");
    	throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
    	try 
    	{
    		if(stmt != null) 
    		{
        	stmt.close();
        }
        if(conn != null) 
        {
        	conn.close();
        }
	    }
	    catch(SQLException exc) 
	    {
	    	SolmrLogger.error(this, "riepilogoZVNParticellePiemonte - SQLException while closing Statement and Connection: "+exc+"\n");
	    	throw new DataAccessException(exc.getMessage());
	    }
	    catch(Exception ex) 
	    {
	    	SolmrLogger.error(this, "riepilogoZVNParticellePiemonte - Generic Exception while closing Statement and Connection: "+ex+"\n");
	    	throw new DataAccessException(ex.getMessage());
	    }
	  }
	  SolmrLogger.debug(this, "Invocated riepilogoZVNParticellePiemonte method in StoricoParticellaDAO\n");
	  if(elencoParticelle.size() == 0) 
	  {
	    return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
		}
		else 
		{
			return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
		}
	}
	
	/**
	 * Metodo per effettuare il riepilogo delle particelle per ZVN solo dei comuni
	 * TOBECONFIGsi ad una determinata dichiarazione di consistenza
	 * 
	 * @param idAzienda
	 * @param escludiAsservimento
	 * @param idDichiarazioneConsistenza
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
	 * @throws DataAccessException
	 */
	public StoricoParticellaVO[] riepilogoZVNParticelleDichiaratePiemonte(Long idAzienda, String escludiAsservimento, Long idDichiarazioneConsistenza) 
	  throws DataAccessException 
	{
    SolmrLogger.debug(this, "Invocating riepilogoZVNParticelleDichiaratePiemonte method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();

    try {

    	SolmrLogger.debug(this, "Creating db-connection in riepilogoZVNParticelleDichiaratePiemonte method in StoricoParticellaDAO\n");
    	conn = getDatasource().getConnection();
    	SolmrLogger.debug(this, "Created db-connection in riepilogoZVNParticelleDichiaratePiemonte method in StoricoParticellaDAO and it values: "+conn+"\n");
    	
    	
    	String query = "" +
    		" SELECT COM.ID_AREA_E, " +
		    "        COM.DESCRIZIONE, " +
		    "        NVL(SUM(DECODE(COM.ID_TITOLO_POSSESSO,5,COM.SUPERFICIE_CONDOTTA,COM.SUP_UTILIZZATA)),0) AS SUP_UTILIZZATA " +
		    " FROM   (SELECT F.ID_AREA_E ID_AREA_E, " + 
		    "                TAE.DESCRIZIONE DESCRIZIONE, " +
		    "                CD.SUPERFICIE_CONDOTTA, " +
		    "                CD.ID_CONDUZIONE_DICHIARATA, " +
		    "                CD.ID_TITOLO_POSSESSO, " +
		    "                SUM(UD.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
		    "         FROM   DB_CONDUZIONE_DICHIARATA CD, " +
		    "                DB_UTILIZZO_DICHIARATO UD, " +
		    "                DB_DICHIARAZIONE_CONSISTENZA DC, " +
		    "                DB_STORICO_PARTICELLA SP, " +
		    "                DB_FOGLIO F, " +
		    "                DB_TIPO_AREA_E TAE " +
		    "         WHERE  DC.ID_AZIENDA = ? " +
		    "         AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
		    " 		    AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
		    " 		    AND    CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
		//    " 		    AND    TRUNC(SP.DATA_INIZIO_VALIDITA) <= TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
		//    " 		    AND    TRUNC(NVL(SP.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY'))) > TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
		    "		      AND    SP.COMUNE = F.COMUNE " +
		    "		      AND    NVL(SP.SEZIONE,'-') = NVL(F.SEZIONE,'-') " +
		    "		      AND    SP.FOGLIO = F.FOGLIO " +
		    "		      AND    F.ID_AREA_E = TAE.ID_AREA_E " +
		    "		      AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA(+) ";
    	if(Validator.isNotEmpty(escludiAsservimento) 
    	    && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
    	{
    		query +=   
    		"         AND    CD.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
    	}
    	query +=	   
    	  "         GROUP BY F.ID_AREA_E, " +
			  "               TAE.DESCRIZIONE, " +
			  "               CD.SUPERFICIE_CONDOTTA, " +
			  "               CD.ID_TITOLO_POSSESSO, " +
			  "               CD.ID_CONDUZIONE_DICHIARATA) COM " +
			  " GROUP BY COM.ID_AREA_E, COM.DESCRIZIONE " +
			  " ORDER BY COM.DESCRIZIONE ASC ";
		
		SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoZVNParticelleDichiaratePiemonte method in StoricoParticellaDAO: "+idAzienda+"\n");
		SolmrLogger.debug(this, "Value of parameter 2 [ESCLUDI_ASSERVIMENTO] in riepilogoZVNParticelleDichiaratePiemonte method in StoricoParticellaDAO: "+escludiAsservimento+"\n");
		SolmrLogger.debug(this, "Value of parameter 3 [ID_DICHIARAZIONE_CONSISTENZA] in riepilogoZVNParticelleDichiaratePiemonte method in StoricoParticellaDAO: "+idDichiarazioneConsistenza+"\n");
		stmt = conn.prepareStatement(query);
		
		stmt.setLong(1, idAzienda.longValue());
		stmt.setLong(2, idDichiarazioneConsistenza.longValue());

	    SolmrLogger.debug(this, "Executing riepilogoZVNParticelleDichiaratePiemonte:"+query+"\n");

	    ResultSet rs = stmt.executeQuery();

	    while(rs.next()) {
	    	StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
	    	FoglioVO foglioVO = new FoglioVO();
	    	ConduzioneDichiarataVO conduzioneDichiarataVO = new ConduzioneDichiarataVO();
	    	UtilizzoDichiaratoVO utilizzoDichiaratoVO = new UtilizzoDichiaratoVO();
	    	foglioVO.setIdAreaE(new Long(rs.getLong("ID_AREA_E")));
	    	foglioVO.setDescrizioneAreaE(rs.getString("DESCRIZIONE"));
	    	storicoParticellaVO.setFoglioVO(foglioVO);
	    	utilizzoDichiaratoVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
	    	conduzioneDichiarataVO.setElencoUtilizzi(new UtilizzoDichiaratoVO[]{utilizzoDichiaratoVO});
	    	storicoParticellaVO.setElencoConduzioniDichiarate(new ConduzioneDichiarataVO[]{conduzioneDichiarataVO});
	    	elencoParticelle.add(storicoParticellaVO);
	    }	
	    
	    rs.close();
	    stmt.close();	      
    }
    catch(SQLException exc) {
    	SolmrLogger.error(this, "riepilogoZVNParticelleDichiaratePiemonte - SQLException: "+exc+"\n");
    	throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
    	SolmrLogger.error(this, "riepilogoZVNParticelleDichiaratePiemonte - Generic Exception: "+ex+"\n");
    	throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
    	try 
    	{
    		if(stmt != null) {
        		stmt.close();
        	}
        	if(conn != null) {
        		conn.close();
        	}
    	}
    	catch(SQLException exc) {
    		SolmrLogger.error(this, "riepilogoZVNParticelleDichiaratePiemonte - SQLException while closing Statement and Connection: "+exc+"\n");
    		throw new DataAccessException(exc.getMessage());
    	}
    	catch(Exception ex) {
    		SolmrLogger.error(this, "riepilogoZVNParticelleDichiaratePiemonte - Generic Exception while closing Statement and Connection: "+ex+"\n");
    		throw new DataAccessException(ex.getMessage());
    	}
	  }
    SolmrLogger.debug(this, "Invocated riepilogoZVNParticelleDichiaratePiemonte method in StoricoParticellaDAO\n");
    if(elencoParticelle.size() == 0) 
    {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
		}
		else 
		{
			return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
		}
	}
	
	/**
	 * Metodo utilizzato per effettuare il riepilogo per i vari tipi area
	 * censiti su DB_STORICO_PARTICELLA al piano di lavorazione corrente
	 * 
	 * @param idAzienda
	 * @param escludiAsservimento
	 * @param tipoArea
	 * @param onlyActive
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
	 * @throws DataAccessException
	 */
	public StoricoParticellaVO[] riepilogoTipoArea(Long idAzienda, String escludiAsservimento, String tipoArea) 
	  throws DataAccessException 
	{
    SolmrLogger.debug(this, "Invocating riepilogoTipoArea method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();

    try 
    {

    	SolmrLogger.debug(this, "Creating db-connection in riepilogoTipoArea method in StoricoParticellaDAO\n");
    	conn = getDatasource().getConnection();
    	SolmrLogger.debug(this, "Created db-connection in riepilogoTipoArea method in StoricoParticellaDAO and it values: "+conn+"\n");

    	String query = "" +
    		" SELECT COM.ID_AREA_"+tipoArea+"," +
	      "        COM.DESCRIZIONE, " +
	      "        NVL(SUM(DECODE(COM.ID_TITOLO_POSSESSO,5,COM.SUPERFICIE_CONDOTTA,COM.SUP_UTILIZZATA)),0) AS SUP_UTILIZZATA " +
	      " FROM   (SELECT TA"+tipoArea+".ID_AREA_"+tipoArea+", " + 
	      "                TA"+tipoArea+".DESCRIZIONE DESCRIZIONE, " +
	      "                CP.SUPERFICIE_CONDOTTA, " +
	      "                CP.ID_CONDUZIONE_PARTICELLA, " +
	      "                CP.ID_TITOLO_POSSESSO, " +
	      "                SUM (UP.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
	      "         FROM   DB_CONDUZIONE_PARTICELLA CP, " +
	      "                DB_UTILIZZO_PARTICELLA UP, " +
	      "                DB_UTE U, " +
	      "                DB_STORICO_PARTICELLA SP, " +
	      "                DB_TIPO_AREA_"+tipoArea+" TA"+tipoArea+
	      "         WHERE  U.ID_AZIENDA = ? " +
	      "         AND    U.ID_UTE = CP.ID_UTE " +
	      "         AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
	      "         AND    SP.ID_AREA_"+tipoArea+" = TA"+tipoArea+".ID_AREA_"+tipoArea+
	      "         AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA(+) " +
    	  "         AND    U.DATA_FINE_ATTIVITA IS NULL " +
    		"         AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
    		"         AND    SP.DATA_FINE_VALIDITA IS NULL ";    
    	if(Validator.isNotEmpty(escludiAsservimento) && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
    	{
    		query +=   
    		"         AND    CP.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
    	}
    	query +=       
    	  "         GROUP BY TA"+tipoArea+".ID_AREA_"+tipoArea+"," +
		    "                  TA"+tipoArea+".DESCRIZIONE, " +
		    "                  CP.SUPERFICIE_CONDOTTA, " +
		    "                  CP.ID_CONDUZIONE_PARTICELLA," +
		    "                  CP.ID_TITOLO_POSSESSO) COM " +
    		"GROUP BY COM.ID_AREA_"+tipoArea+", " +
    		"         COM.DESCRIZIONE " +
    		"ORDER BY COM.DESCRIZIONE ASC ";
		
		  SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoTipoArea method in StoricoParticellaDAO: "+idAzienda+"\n");
		  SolmrLogger.debug(this, "Value of parameter 2 [ESCLUDI_ASSERVIMENTO] in riepilogoTipoArea method in StoricoParticellaDAO: "+escludiAsservimento+"\n");
		  SolmrLogger.debug(this, "Value of parameter 3 [TIPO_AREA] in riepilogoTipoArea method in StoricoParticellaDAO: "+tipoArea+"\n");

		  stmt = conn.prepareStatement(query);
		
		  stmt.setLong(1, idAzienda.longValue());

		  SolmrLogger.debug(this, "Executing riepilogoTipoArea:"+query+"\n");

      ResultSet rs = stmt.executeQuery();
  
      while(rs.next()) 
      {
      	StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
      	ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
      	UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
      	Method setId = storicoParticellaVO.getClass().getMethod("setIdArea" +org.apache.commons.lang.StringUtils.capitalize(tipoArea), new Class[]{new Long(rs.getLong("ID_AREA_"+tipoArea)).getClass()});
      	setId.invoke(storicoParticellaVO, new Object[]{new Long(rs.getLong("ID_AREA_"+tipoArea))});
      	CodeDescription code = new CodeDescription(new Integer(rs.getInt("ID_AREA_"+tipoArea)), rs.getString("DESCRIZIONE"));
      	Method setTipoArea = storicoParticellaVO.getClass().getMethod("setArea" +org.apache.commons.lang.StringUtils.capitalize(tipoArea),new Class[]{code.getClass()});
      	setTipoArea.invoke(storicoParticellaVO, new Object[]{code});
      	utilizzoParticellaVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
      	conduzioneParticellaVO.setElencoUtilizzi(new UtilizzoParticellaVO[]{utilizzoParticellaVO});
      	storicoParticellaVO.setElencoConduzioni(new ConduzioneParticellaVO[]{conduzioneParticellaVO});
      	elencoParticelle.add(storicoParticellaVO);
      }	
  		    
      rs.close();
      stmt.close();	      
    }
    catch(SQLException exc) 
    {
    	SolmrLogger.error(this, "riepilogoTipoArea - SQLException: "+exc+"\n");
    	throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
    	SolmrLogger.error(this, "riepilogoTipoArea - Generic Exception: "+ex+"\n");
    	throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
    	try 
    	{
    		if(stmt != null) 
    		{
        	stmt.close();
        }
        if(conn != null) 
        {
        	conn.close();
        }
  	  }
    	catch(SQLException exc) {
    		SolmrLogger.error(this, "riepilogoTipoArea - SQLException while closing Statement and Connection: "+exc+"\n");
    		throw new DataAccessException(exc.getMessage());
    	}
    	catch(Exception ex) {
    		SolmrLogger.error(this, "riepilogoTipoArea - Generic Exception while closing Statement and Connection: "+ex+"\n");
    		throw new DataAccessException(ex.getMessage());
    	}
    }
    SolmrLogger.debug(this, "Invocated riepilogoTipoArea method in StoricoParticellaDAO\n");
    if(elencoParticelle.size() == 0) 
    {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
  	}
  	else 
  	{
  		return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
  	}
	}
	
	/**
	 * Metodo utilizzato per effettuare il riepilogo per i vari tipi area
	 * censiti su DB_STORICO_PARTICELLA ad una determinata dichiarazione di consistenza
	 * 
	 * @param idAzienda
	 * @param escludiAsservimento
	 * @param idDichiarazioneConsistenza
	 * @param tipoArea
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
	 * @throws DataAccessException
	 */
	public StoricoParticellaVO[] riepilogoTipoAreaDichiarato(Long idAzienda, String escludiAsservimento, Long idDichiarazioneConsistenza, String tipoArea) 
	  throws DataAccessException 
	{
    SolmrLogger.debug(this, "Invocating riepilogoTipoAreaDichiarato method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();

    try 
    {

    	SolmrLogger.debug(this, "Creating db-connection in riepilogoTipoAreaDichiarato method in StoricoParticellaDAO\n");
    	conn = getDatasource().getConnection();
    	SolmrLogger.debug(this, "Created db-connection in riepilogoTipoAreaDichiarato method in StoricoParticellaDAO and it values: "+conn+"\n");

    	String query = 
    	  " SELECT COM.ID_AREA_"+tipoArea+"," +
			  "        COM.DESCRIZIONE, " +
			  "        NVL(SUM(DECODE(COM.ID_TITOLO_POSSESSO,5,COM.SUPERFICIE_CONDOTTA,COM.SUP_UTILIZZATA)),0) AS SUP_UTILIZZATA " +
			  " FROM   (SELECT TA"+tipoArea+".ID_AREA_"+tipoArea+" ID_AREA_"+tipoArea+"," + 
			  "                TA"+tipoArea+".DESCRIZIONE DESCRIZIONE, " +
			  "                CD.SUPERFICIE_CONDOTTA, " +
			  "                CD.ID_TITOLO_POSSESSO, " +
			  "                CD.ID_CONDUZIONE_DICHIARATA, " +
			  "                SUM(UD.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
			  "         FROM   DB_CONDUZIONE_DICHIARATA CD, " +
			  "                DB_UTILIZZO_DICHIARATO UD, " +
			  "                DB_DICHIARAZIONE_CONSISTENZA DC, " +
			  "                DB_STORICO_PARTICELLA SP, " +
			  "                DB_TIPO_AREA_"+tipoArea+" TA"+tipoArea+
			  "         WHERE  DC.ID_AZIENDA = ? " + 
			  " 		    AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
			  " 		    AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
			  " 		    AND    CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
		//	  " 		    AND    TRUNC(SP.DATA_INIZIO_VALIDITA) <= TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
		//	  "         AND    TRUNC(NVL(SP.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY'))) >= TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
			  "         AND    SP.ID_AREA_"+tipoArea+" = TA"+tipoArea+".ID_AREA_"+tipoArea+
			  "         AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA(+) ";
    	if(Validator.isNotEmpty(escludiAsservimento) 
    	    && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
    	{
    		query +=   
    		"         AND    CD.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
    	}
    	query +=	   
    	  "         GROUP BY TA"+tipoArea+".ID_AREA_"+tipoArea+","+
    		"                  TA"+tipoArea+".DESCRIZIONE," +
    		"                  CD.SUPERFICIE_CONDOTTA, " +
    		"                  CD.ID_TITOLO_POSSESSO, " +
    		"                  CD.ID_CONDUZIONE_DICHIARATA) COM " +
    		" GROUP BY COM.ID_AREA_"+tipoArea+", " +
    		"          COM.DESCRIZIONE " +
    		" ORDER BY COM.DESCRIZIONE ASC ";
	    	
			
			
			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoTipoAreaDichiarato method in StoricoParticellaDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ESCLUDI_ASSERVIMENTO] in riepilogoTipoAreaDichiarato method in StoricoParticellaDAO: "+escludiAsservimento+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ID_DICHIARAZIONE_CONSISTENZA] in riepilogoTipoAreaDichiarato method in StoricoParticellaDAO: "+idDichiarazioneConsistenza+"\n");
			SolmrLogger.debug(this, "Value of parameter 4 [TIPO_AREA] in riepilogoTipoAreaDichiarato method in StoricoParticellaDAO: "+tipoArea+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());
			stmt.setLong(2, idDichiarazioneConsistenza.longValue());
			

	    SolmrLogger.debug(this, "Executing riepilogoTipoAreaDichiarato:"+query+"\n");

	    ResultSet rs = stmt.executeQuery();

	    while(rs.next()) {
	    	StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
	    	ConduzioneDichiarataVO conduzioneDichiarataVO = new ConduzioneDichiarataVO();
	    	UtilizzoDichiaratoVO utilizzoDichiaratoVO = new UtilizzoDichiaratoVO();
	    	Method setId = storicoParticellaVO.getClass().getMethod("setIdArea" +org.apache.commons.lang.StringUtils.capitalize(tipoArea), new Class[]{new Long(rs.getLong("ID_AREA_"+tipoArea)).getClass()});
	    	setId.invoke(storicoParticellaVO, new Object[]{new Long(rs.getLong("ID_AREA_"+tipoArea))});
	    	CodeDescription code = new CodeDescription(new Integer(rs.getInt("ID_AREA_"+tipoArea)), rs.getString("DESCRIZIONE"));
	    	Method setTipoArea = storicoParticellaVO.getClass().getMethod("setArea" +org.apache.commons.lang.StringUtils.capitalize(tipoArea),new Class[]{code.getClass()});
	    	setTipoArea.invoke(storicoParticellaVO, new Object[]{code});
	    	utilizzoDichiaratoVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
	    	conduzioneDichiarataVO.setElencoUtilizzi(new UtilizzoDichiaratoVO[]{utilizzoDichiaratoVO});
	    	storicoParticellaVO.setElencoConduzioniDichiarate(new ConduzioneDichiarataVO[]{conduzioneDichiarataVO});
	    	elencoParticelle.add(storicoParticellaVO);
	    }	
	    
	    rs.close();
	    stmt.close();	      
    }
    catch(SQLException exc) {
    	SolmrLogger.error(this, "riepilogoTipoAreaDichiarato - SQLException: "+exc+"\n");
    	throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
    	SolmrLogger.error(this, "riepilogoTipoAreaDichiarato - Generic Exception: "+ex+"\n");
    	throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
    	try 
    	{
    		if(stmt != null) 
    		{
        	stmt.close();
        }
        if(conn != null) 
        {
        	conn.close();
        }
    	}
    	catch(SQLException exc) {
    		SolmrLogger.error(this, "riepilogoTipoAreaDichiarato - SQLException while closing Statement and Connection: "+exc+"\n");
    		throw new DataAccessException(exc.getMessage());
    	}
    	catch(Exception ex) {
    		SolmrLogger.error(this, "riepilogoTipoAreaDichiarato - Generic Exception while closing Statement and Connection: "+ex+"\n");
    		throw new DataAccessException(ex.getMessage());
    	}
    }
    SolmrLogger.debug(this, "Invocated riepilogoTipoAreaDichiarato method in StoricoParticellaDAO\n");
    if(elencoParticelle.size() == 0) 
    {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
		}
		else 
		{
			return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
		}
	}
	
	
	/**
   * Metodo per effettuare il riepilogo delle particelle per Localizzazione solo dei comuni
   * TOBECONFIGsi
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws DataAccessException
   */
  public StoricoParticellaVO[] riepilogoLocalizzazioneParticellePiemonte(Long idAzienda, String escludiAsservimento) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating riepilogoLocalizzazioneParticellePiemonte method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();

    try 
    {

      SolmrLogger.debug(this, "Creating db-connection in riepilogoLocalizzazioneParticellePiemonte method in StoricoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in riepilogoLocalizzazioneParticellePiemonte method in StoricoParticellaDAO and it values: "+conn+"\n");

      String query = 
        " SELECT COM.ID_AREA_PSN, " +
        "        COM.DESCRIZIONE, " +
        "        NVL(SUM(DECODE(COM.ID_TITOLO_POSSESSO,5,COM.SUPERFICIE_CONDOTTA,COM.SUP_UTILIZZATA)),0) AS SUP_UTILIZZATA " +
        " FROM   (SELECT F.ID_AREA_PSN ID_AREA_PSN, " + 
        "                TPSN.DESCRIZIONE DESCRIZIONE, " +
        "                CP.SUPERFICIE_CONDOTTA, " +
        "                CP.ID_TITOLO_POSSESSO, " +
        "                CP.ID_CONDUZIONE_PARTICELLA, " +
        "                SUM (UP.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
        "         FROM   DB_CONDUZIONE_PARTICELLA CP, " +
        "                DB_UTILIZZO_PARTICELLA UP, " +
        "                DB_UTE U, " +
        "                DB_STORICO_PARTICELLA SP, " +
        "                DB_FOGLIO F, " +
        "                DB_TIPO_AREA_PSN TPSN " +
        "         WHERE  U.ID_AZIENDA = ? " +
        "         AND    U.ID_UTE = CP.ID_UTE " +
        "         AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
        "         AND    SP.COMUNE = F.COMUNE " +
        "         AND    NVL (SP.SEZIONE, '-') = NVL (F.SEZIONE, '-') " +
        "         AND    SP.FOGLIO = F.FOGLIO " +
        "         AND    F.ID_AREA_PSN = TPSN.ID_AREA_PSN " +
        "         AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA(+) " +
        "         AND    U.DATA_FINE_ATTIVITA IS NULL " +
        "         AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "         AND    SP.DATA_FINE_VALIDITA IS NULL ";
        
      if(Validator.isNotEmpty(escludiAsservimento) 
          && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {
        query +=   
        "         AND    CP.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
      }
      query +=       
        "         GROUP BY F.ID_AREA_PSN, " +
        "                  TPSN.DESCRIZIONE, " +
        "                  CP.SUPERFICIE_CONDOTTA, " +
        "                  CP.ID_TITOLO_POSSESSO, " +
        "                  CP.ID_CONDUZIONE_PARTICELLA) COM " +
        " GROUP BY COM.ID_AREA_PSN, " +
        "          COM.DESCRIZIONE " +
        " ORDER BY COM.DESCRIZIONE ASC ";
      
    
      
      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoLocalizzazioneParticellePiemonte method in StoricoParticellaDAO: "+idAzienda+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ESCLUDI_ASSERVIMENTO] in riepilogoLocalizzazioneParticellePiemonte method in StoricoParticellaDAO: "+escludiAsservimento+"\n");

      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda.longValue());

      SolmrLogger.debug(this, "Executing riepilogoLocalizzazioneParticellePiemonte:"+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) {
        StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
        FoglioVO foglioVO = new FoglioVO();
        ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
        UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
        foglioVO.setIdAreaPSN(new Long(rs.getLong("ID_AREA_PSN")));
        foglioVO.setDescrizioneAreaPSN(rs.getString("DESCRIZIONE"));
        storicoParticellaVO.setFoglioVO(foglioVO);
        utilizzoParticellaVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
        conduzioneParticellaVO.setElencoUtilizzi(new UtilizzoParticellaVO[]{utilizzoParticellaVO});
        storicoParticellaVO.setElencoConduzioni(new ConduzioneParticellaVO[]{conduzioneParticellaVO});
        elencoParticelle.add(storicoParticellaVO);
      } 
        
      rs.close();
      stmt.close();       
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "riepilogoLocalizzazioneParticellePiemonte - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "riepilogoLocalizzazioneParticellePiemonte - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) 
        {
          stmt.close();
        }
        if(conn != null) 
        {
          conn.close();
        }
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "riepilogoLocalizzazioneParticellePiemonte - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "riepilogoLocalizzazioneParticellePiemonte - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated riepilogoLocalizzazioneParticellePiemonte method in StoricoParticellaDAO\n");
    if(elencoParticelle.size() == 0) 
    {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
    }
    else 
    {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
    }
  } 
  
  /**
   * Metodo per effettuare il riepilogo delle particelle per Localizzazione solo dei comuni
   * TOBECONFIGsi ad una determinata dichiarazione di consistenza
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws DataAccessException
   */
  public StoricoParticellaVO[] riepilogoLocalizzazioneParticelleDichiaratePiemonte(Long idAzienda, 
      String escludiAsservimento, Long idDichiarazioneConsistenza) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating riepilogoLocalizzazioneParticelleDichiarateTOBECONFIG method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();

    try 
    {

      SolmrLogger.debug(this, "Creating db-connection in riepilogoLocalizzazioneParticelleDichiarateTOBECONFIG method in StoricoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in riepilogoLocalizzazioneParticelleDichiarateTOBECONFIG method in StoricoParticellaDAO and it values: "+conn+"\n");
      
      
      String query = 
        " SELECT COM.ID_AREA_PSN, " +
        "        COM.DESCRIZIONE, " +
        "        NVL(SUM(DECODE(COM.ID_TITOLO_POSSESSO,5,COM.SUPERFICIE_CONDOTTA,COM.SUP_UTILIZZATA)),0) AS SUP_UTILIZZATA " +
        " FROM   (SELECT F.ID_AREA_PSN ID_AREA_PSN, " + 
        "                TPSN.DESCRIZIONE DESCRIZIONE, " +
        "                CD.SUPERFICIE_CONDOTTA, " +
        "                CD.ID_TITOLO_POSSESSO, " +
        "                CD.ID_CONDUZIONE_DICHIARATA, " +
        "                SUM(UD.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
        "         FROM   DB_CONDUZIONE_DICHIARATA CD, " +
        "                DB_UTILIZZO_DICHIARATO UD, " +
        "                DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "                DB_STORICO_PARTICELLA SP, " +
        "                DB_FOGLIO F, " +
        "                DB_TIPO_AREA_PSN TPSN " +
        "         WHERE  DC.ID_AZIENDA = ? " +
        "         AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "         AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        "         AND    CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
 //       "         AND    TRUNC(SP.DATA_INIZIO_VALIDITA) <= TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
 //       "         AND    TRUNC(NVL(SP.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY'))) > TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
        "         AND    SP.COMUNE = F.COMUNE " +
        "         AND    NVL(SP.SEZIONE,'-') = NVL(F.SEZIONE,'-') " +
        "         AND    SP.FOGLIO = F.FOGLIO " +
        "         AND    F.ID_AREA_PSN = TPSN.ID_AREA_PSN " +
        "         AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA(+) ";
      if(Validator.isNotEmpty(escludiAsservimento) 
          && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {
        query +=   
        "         AND    CD.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
      }
      query +=     
        " GROUP BY F.ID_AREA_PSN, " +
        "          TPSN.DESCRIZIONE, " +
        "          CD.SUPERFICIE_CONDOTTA, " +
        "          CD.ID_TITOLO_POSSESSO, " +
        "          CD.ID_CONDUZIONE_DICHIARATA) COM " +
        " GROUP BY COM.ID_AREA_PSN, " +
        "          COM.DESCRIZIONE "+
        " ORDER BY COM.DESCRIZIONE ASC ";
      
      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoLocalizzazioneParticelleDichiarateTOBECONFIG method in StoricoParticellaDAO: "+idAzienda+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ESCLUDI_ASSERVIMENTO] in riepilogoLocalizzazioneParticelleDichiarateTOBECONFIG method in StoricoParticellaDAO: "+escludiAsservimento+"\n");
      SolmrLogger.debug(this, "Value of parameter 3 [ID_DICHIARAZIONE_CONSISTENZA] in riepilogoLocalizzazioneParticelleDichiarateTOBECONFIG method in StoricoParticellaDAO: "+idDichiarazioneConsistenza+"\n");

      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda.longValue());
      stmt.setLong(2, idDichiarazioneConsistenza.longValue());

      SolmrLogger.debug(this, "Executing riepilogoLocalizzazioneParticelleDichiarateTOBECONFIG:"+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) {
        StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
        FoglioVO foglioVO = new FoglioVO();
        ConduzioneDichiarataVO conduzioneDichiarataVO = new ConduzioneDichiarataVO();
        UtilizzoDichiaratoVO utilizzoDichiaratoVO = new UtilizzoDichiaratoVO();
        foglioVO.setIdAreaPSN(new Long(rs.getLong("ID_AREA_PSN")));
        foglioVO.setDescrizioneAreaPSN(rs.getString("DESCRIZIONE"));
        storicoParticellaVO.setFoglioVO(foglioVO);
        utilizzoDichiaratoVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
        conduzioneDichiarataVO.setElencoUtilizzi(new UtilizzoDichiaratoVO[]{utilizzoDichiaratoVO});
        storicoParticellaVO.setElencoConduzioniDichiarate(new ConduzioneDichiarataVO[]{conduzioneDichiarataVO});
        elencoParticelle.add(storicoParticellaVO);
      } 
      
      rs.close();
      stmt.close();       
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "riepilogoLocalizzazioneParticelleDichiarateTOBECONFIG - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "riepilogoLocalizzazioneParticelleDichiarateTOBECONFIG - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) 
        {
          stmt.close();
        }
        if(conn != null) 
        {
          conn.close();
        }
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "riepilogoLocalizzazioneParticelleDichiarateTOBECONFIG - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "riepilogoLocalizzazioneParticelleDichiarateTOBECONFIG - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated riepilogoLocalizzazioneParticelleDichiarateTOBECONFIG method in StoricoParticellaDAO\n");
    if(elencoParticelle.size() == 0) 
    {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
    }
    else {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
    }
  }
  
  /**
   * Metodo per effettuare il riepilogo delle particelle per Fascia Fluviale
   * TOBECONFIGsi
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws DataAccessException
   */
  public StoricoParticellaVO[] riepilogoFasciaFluvialeParticelle(Long idAzienda, String escludiAsservimento) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating riepilogoFasciaFluvialeParticelle method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();

    try 
    {

      SolmrLogger.debug(this, "Creating db-connection in riepilogoFasciaFluvialeParticelle method in StoricoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in riepilogoFasciaFluvialeParticelle method in StoricoParticellaDAO and it values: "+conn+"\n");

      String query = 
        " SELECT COM.ID_FASCIA_FLUVIALE, " +
        "        COM.DESCRIZIONE, " +
        "        NVL(SUM(DECODE(COM.ID_TITOLO_POSSESSO,5,COM.SUPERFICIE_CONDOTTA,COM.SUP_UTILIZZATA)),0) AS SUP_UTILIZZATA " +
        " FROM   (SELECT SP.ID_FASCIA_FLUVIALE ID_FASCIA_FLUVIALE, " + 
        "                TFF.DESCRIZIONE DESCRIZIONE, " +
        "                CP.SUPERFICIE_CONDOTTA, " +
        "                CP.ID_TITOLO_POSSESSO, " +
        "                CP.ID_CONDUZIONE_PARTICELLA, " +
        "                SUM (UP.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
        "         FROM   DB_CONDUZIONE_PARTICELLA CP, " +
        "                DB_UTILIZZO_PARTICELLA UP, " +
        "                DB_UTE U, " +
        "                DB_STORICO_PARTICELLA SP, " +
        "                DB_TIPO_FASCIA_FLUVIALE TFF " +
        "         WHERE  U.ID_AZIENDA = ? " +
        "         AND    U.ID_UTE = CP.ID_UTE " +
        "         AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
        "         AND    SP.ID_FASCIA_FLUVIALE = TFF.ID_FASCIA_FLUVIALE " +
        "         AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA(+) " +
        "         AND    U.DATA_FINE_ATTIVITA IS NULL " +
        "         AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "         AND    SP.DATA_FINE_VALIDITA IS NULL ";    
      if(Validator.isNotEmpty(escludiAsservimento) 
          && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {
        query +=   
        "         AND    CP.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
      }
      query +=
        "         GROUP BY SP.ID_FASCIA_FLUVIALE, " +
        "                  TFF.DESCRIZIONE, " +
        "                  CP.SUPERFICIE_CONDOTTA, " +
        "                  CP.ID_TITOLO_POSSESSO, " +
        "          CP.ID_CONDUZIONE_PARTICELLA) COM " +
        " GROUP BY COM.ID_FASCIA_FLUVIALE, " +
        "          COM.DESCRIZIONE " +
        " ORDER BY COM.DESCRIZIONE ASC ";
    
      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoFasciaFluvialeParticelle method in StoricoParticellaDAO: "+idAzienda+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ESCLUDI_ASSERVIMENTO] in riepilogoFasciaFluvialeParticelle method in StoricoParticellaDAO: "+escludiAsservimento+"\n");
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda.longValue());
  
      SolmrLogger.debug(this, "Executing riepilogoLocalizzazioneParticellePiemonte:"+query+"\n");
  
      ResultSet rs = stmt.executeQuery();
  
      while(rs.next()) 
      {
        StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
        ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
        UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
        storicoParticellaVO.setIdFasciaFluviale(new Long(rs.getLong("ID_FASCIA_FLUVIALE")));
        CodeDescription code = new CodeDescription(new Integer(rs.getInt("ID_FASCIA_FLUVIALE")), rs.getString("DESCRIZIONE"));
        storicoParticellaVO.setFasciaFluviale(code);
        utilizzoParticellaVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
        conduzioneParticellaVO.setElencoUtilizzi(new UtilizzoParticellaVO[]{utilizzoParticellaVO});
        storicoParticellaVO.setElencoConduzioni(new ConduzioneParticellaVO[]{conduzioneParticellaVO});
        elencoParticelle.add(storicoParticellaVO);
      } 
        
      rs.close();
      stmt.close();       
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "riepilogoFasciaFluvialeParticelle - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "riepilogoFasciaFluvialeParticelle - Generic Exception: "+ex+"\n");
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
        SolmrLogger.error(this, "riepilogoFasciaFluvialeParticelle - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "riepilogoFasciaFluvialeParticelle - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated riepilogoFasciaFluvialeParticelle method in StoricoParticellaDAO\n");
    if(elencoParticelle.size() == 0) {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
    }
    else {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
    }
  }
  
  
  /**
   * Metodo per effettuare il riepilogo delle particelle per Fascia Fluviale
   * ad una determinata dichiarazione di consistenza
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws DataAccessException
   */
  public StoricoParticellaVO[] riepilogoFasciaFluvialeParticelleDichiarate(Long idAzienda, String escludiAsservimento, 
      Long idDichiarazioneConsistenza) 
  throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating riepilogoLocalizzazioneParticelleDichiarateTOBECONFIG method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();

    try 
    {

      SolmrLogger.debug(this, "Creating db-connection in riepilogoFasciaFluvialeParticelleDichiarate method in StoricoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in riepilogoFasciaFluvialeParticelleDichiarate method in StoricoParticellaDAO and it values: "+conn+"\n");
      
      
      String query = 
        " SELECT COM.ID_FASCIA_FLUVIALE, " +
        "        COM.DESCRIZIONE, " +
        "        NVL(SUM(DECODE(COM.ID_TITOLO_POSSESSO,5,COM.SUPERFICIE_CONDOTTA,COM.SUP_UTILIZZATA)),0) AS SUP_UTILIZZATA " +
        " FROM   (SELECT SP.ID_FASCIA_FLUVIALE ID_FASCIA_FLUVIALE, " + 
        "                TFF.DESCRIZIONE DESCRIZIONE, " +
        "                CD.SUPERFICIE_CONDOTTA, " +
        "                CD.ID_TITOLO_POSSESSO, " +
        "                CD.ID_CONDUZIONE_DICHIARATA, " +
        "                SUM(UD.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
        "         FROM   DB_CONDUZIONE_DICHIARATA CD, " +
        "                DB_UTILIZZO_DICHIARATO UD, " +
        "                DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "                DB_STORICO_PARTICELLA SP, " +
        "                DB_TIPO_FASCIA_FLUVIALE TFF " +
        "         WHERE  DC.ID_AZIENDA = ? " +
        "         AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "         AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        "         AND    CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
   //     "         AND    TRUNC(SP.DATA_INIZIO_VALIDITA) <= TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
   //     "         AND    TRUNC(NVL(SP.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY'))) > TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
        "         AND    SP.ID_FASCIA_FLUVIALE = TFF.ID_FASCIA_FLUVIALE " +
        "         AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA(+) ";
      if(Validator.isNotEmpty(escludiAsservimento) 
          && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {
        query +=   
        "         AND    CD.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
      }
      query +=     
        "         GROUP BY SP.ID_FASCIA_FLUVIALE, " +
        "                  TFF.DESCRIZIONE, " +
        "                  CD.SUPERFICIE_CONDOTTA, " +
        "                  CD.ID_TITOLO_POSSESSO, " +
        "                  CD.ID_CONDUZIONE_DICHIARATA) COM " +
        " GROUP BY COM.ID_FASCIA_FLUVIALE, " +
        "          COM.DESCRIZIONE " +
        " ORDER BY COM.DESCRIZIONE ASC ";
      
    
    
      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoFasciaFluvialeParticelleDichiarate method in StoricoParticellaDAO: "+idAzienda+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ESCLUDI_ASSERVIMENTO] in riepilogoFasciaFluvialeParticelleDichiarate method in StoricoParticellaDAO: "+escludiAsservimento+"\n");
      SolmrLogger.debug(this, "Value of parameter 3 [ID_DICHIARAZIONE_CONSISTENZA] in riepilogoFasciaFluvialeParticelleDichiarate method in StoricoParticellaDAO: "+idDichiarazioneConsistenza+"\n");
  
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda.longValue());
      stmt.setLong(2, idDichiarazioneConsistenza.longValue());

      SolmrLogger.debug(this, "Executing riepilogoFasciaFluvialeParticelleDichiarate:"+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) {
        StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
        ConduzioneDichiarataVO conduzioneDichiarataVO = new ConduzioneDichiarataVO();
        UtilizzoDichiaratoVO utilizzoDichiaratoVO = new UtilizzoDichiaratoVO();
        storicoParticellaVO.setIdFasciaFluviale(new Long(rs.getLong("ID_FASCIA_FLUVIALE")));
        CodeDescription code = new CodeDescription(new Integer(rs.getInt("ID_FASCIA_FLUVIALE")), rs.getString("DESCRIZIONE"));
        storicoParticellaVO.setFasciaFluviale(code);
        utilizzoDichiaratoVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
        conduzioneDichiarataVO.setElencoUtilizzi(new UtilizzoDichiaratoVO[]{utilizzoDichiaratoVO});
        storicoParticellaVO.setElencoConduzioniDichiarate(new ConduzioneDichiarataVO[]{conduzioneDichiarataVO});
        elencoParticelle.add(storicoParticellaVO);
      } 
      
      rs.close();
      stmt.close();       
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "riepilogoFasciaFluvialeParticelleDichiarate - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "riepilogoFasciaFluvialeParticelleDichiarate - Generic Exception: "+ex+"\n");
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
        SolmrLogger.error(this, "riepilogoFasciaFluvialeParticelleDichiarate - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "riepilogoFasciaFluvialeParticelleDichiarate - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated riepilogoFasciaFluvialeParticelleDichiarate method in StoricoParticellaDAO\n");
    if(elencoParticelle.size() == 0) {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
    }
    else {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
    }
  }
  
  /**
   * ritorna true se ha trovato un record (ha fatto il lock!!)
   * 
   * @param long[] idConduzioneParticella
   * @return
   * @throws DataAccessException
   */
  public boolean lockTableStoricoParticella(long[] idStoricoParticella) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean flagLock = false;
    
    
    String strIN =" (";
    
    if (idStoricoParticella!=null && idStoricoParticella.length!=0)
    {
      for(int i=0;i<idStoricoParticella.length;i++)
      {
        strIN+="?";
        if (i<idStoricoParticella.length-1)
          strIN+=",";
      }
    }
    strIN+=") ";
    
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoParticellaDAO::lockTableStoricoParticella] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append(" SELECT * " +
              "FROM DB_STORICO_PARTICELLA " +
              "WHERE ID_STORICO_PARTICELLA  IN " + strIN +
              "FOR UPDATE ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[StoricoParticellaDAO::lockTableStoricoParticella] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      if (idStoricoParticella!=null && idStoricoParticella.length!=0)
      {
        for(int i=0;i<idStoricoParticella.length;i++)
          stmt.setLong(++indice,idStoricoParticella[i]);
      }
      
      
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        flagLock = true;
      }
      
      return flagLock;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("flagLock", flagLock) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idStoricoParticella", idStoricoParticella)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[StoricoParticellaDAO::lockTableStoricoParticella] ",
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
              "[StoricoParticellaDAO::lockTableStoricoParticella] END.");
    }
  }
  
  
  public long getIdConduzioneDichiarata(long idConduzioneParticella,long idDichiarazioneConsistenza) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getIdConduzioneDichiarata method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    long result=0;
    
    try 
    {
    
      SolmrLogger.debug(this, "Creating db-connection in getIdConduzioneDichiarata method in StoricoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getIdConduzioneDichiarata method in StoricoParticellaDAO and it values: "+conn+"\n");
      
      
      String query = " SELECT CD.ID_CONDUZIONE_DICHIARATA "+
                     "FROM DB_CONDUZIONE_DICHIARATA CD, DB_DICHIARAZIONE_CONSISTENZA DC "+
                     "WHERE CD.ID_CONDUZIONE_PARTICELLA=? "+
                     "AND CD.CODICE_FOTOGRAFIA_TERRENI=DC.CODICE_FOTOGRAFIA_TERRENI "+
                     "AND DC.ID_DICHIARAZIONE_CONSISTENZA=? ";
        
      
      
      SolmrLogger.debug(this, "Value of parameter 1 [ID_CONDUZIONE_PARTICELLA] in getIdConduzioneDichiarata method in StoricoParticellaDAO: "+idConduzioneParticella+"\n");
      SolmrLogger.debug(this, "Value of parameter 3 [ID_DICHIARAZIONE_CONSISTENZA] in getIdConduzioneDichiarata method in StoricoParticellaDAO: "+idDichiarazioneConsistenza+"\n");
      
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idConduzioneParticella);
      stmt.setLong(2, idDichiarazioneConsistenza);
      
      SolmrLogger.debug(this, "Executing getIdConduzioneDichiarata:"+query+"\n");
      
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next()) 
        result=rs.getLong("ID_CONDUZIONE_DICHIARATA");
        
      rs.close();
      stmt.close();       
    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "getIdConduzioneDichiarata - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) 
    {
      SolmrLogger.error(this, "getIdConduzioneDichiarata - Generic Exception: "+ex+"\n");
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
        SolmrLogger.error(this, "getIdConduzioneDichiarata - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) 
      {
        SolmrLogger.error(this, "getIdConduzioneDichiarata - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  
  /**
   * Metodo per effettuare il riepilogo delle particelle per ZVN 
   * comprensivo di fascia fluviale
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws DataAccessException
   */
  public StoricoParticellaVO[] riepilogoZVNFasciaFluviale(Long idAzienda, String escludiAsservimento) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating riepilogoZVNFasciaFluviale method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();

    try 
    {

      SolmrLogger.debug(this, "Creating db-connection in riepilogoZVNFasciaFluviale method in StoricoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in riepilogoZVNFasciaFluviale method in StoricoParticellaDAO and it values: "+conn+"\n");

      String query = ""+
        "SELECT " +
        "       COM.TIPO, " + 
        "       NVL(SUM(DECODE(COM.ID_TITOLO_POSSESSO,5,COM.SUPERFICIE_CONDOTTA,COM.SUP_UTILIZZATA)),0) AS SUP_UTILIZZATA " +
        "FROM   (SELECT  " +
        "               1 AS TIPO, " +
        "               CP.SUPERFICIE_CONDOTTA, " +
        "               CP.ID_TITOLO_POSSESSO, " +
        "               CP.ID_CONDUZIONE_PARTICELLA, " +
        "               SUM (UP.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " + 
        "        FROM   DB_CONDUZIONE_PARTICELLA CP, " +
        "               DB_UTILIZZO_PARTICELLA UP, " +
        "               DB_UTE U, " +
        "               DB_STORICO_PARTICELLA SP, " + 
        "               DB_FOGLIO F " +
        "        WHERE  U.ID_AZIENDA = ? " + 
        "        AND    U.ID_UTE = CP.ID_UTE " + 
        "        AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " + 
        "        AND    SP.COMUNE = F.COMUNE " +
        "        AND    NVL (SP.SEZIONE, '-') = NVL (F.SEZIONE, '-') " + 
        "        AND    SP.FOGLIO = F.FOGLIO " +
        "        AND (SP.ID_FASCIA_FLUVIALE IS NOT NULL OR NVL(F.ID_AREA_E,0) = 2) " +
        "        AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA(+) " +
        "        AND    U.DATA_FINE_ATTIVITA IS NULL " +
        "        AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "        AND    SP.DATA_FINE_VALIDITA IS NULL ";
      if(Validator.isNotEmpty(escludiAsservimento) && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_S)) {
        query +="" +
          "      AND    CP.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
      }
      query +=""+
        "        GROUP BY " +
        "               CP.SUPERFICIE_CONDOTTA, " +
        "               CP.ID_TITOLO_POSSESSO, " +
        "               CP.ID_CONDUZIONE_PARTICELLA " +
        "        UNION " +
        "        SELECT " +
        "               2 AS TIPO, " + 
        "               CP.SUPERFICIE_CONDOTTA, " +
        "               CP.ID_TITOLO_POSSESSO, " +
        "               CP.ID_CONDUZIONE_PARTICELLA, " +
        "               SUM (UP.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
        "        FROM   DB_CONDUZIONE_PARTICELLA CP, " +
        "               DB_UTILIZZO_PARTICELLA UP, " +
        "               DB_UTE U, " +
        "               DB_STORICO_PARTICELLA SP, " + 
        "               DB_FOGLIO F " +
        "        WHERE  U.ID_AZIENDA = ? " + 
        "        AND    U.ID_UTE = CP.ID_UTE " + 
        "        AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " + 
        "        AND    SP.COMUNE = F.COMUNE " +
        "        AND    NVL (SP.SEZIONE, '-') = NVL (F.SEZIONE, '-') " +
        "        AND    SP.FOGLIO = F.FOGLIO " +
        "        AND    ID_FASCIA_FLUVIALE IS NULL " + 
        "        AND    NVL(F.ID_AREA_E,1) = 1 " +
        "        AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA(+) " + 
        "        AND    U.DATA_FINE_ATTIVITA IS NULL " +
        "        AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "        AND    SP.DATA_FINE_VALIDITA IS NULL ";      
      if(Validator.isNotEmpty(escludiAsservimento) && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_S)) {
        query +=   "" +
        "        AND    CP.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
      }
      
      query += ""+
        "        GROUP BY " +
        "               CP.SUPERFICIE_CONDOTTA, " +
        "               CP.ID_TITOLO_POSSESSO, " +
        "               CP.ID_CONDUZIONE_PARTICELLA) COM " + 
        "GROUP BY COM.TIPO " +
        "ORDER BY COM.TIPO ASC ";

      
      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoZVNFasciaFluviale method in StoricoParticellaDAO: "+idAzienda+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ESCLUDI_ASSERVIMENTO] in riepilogoZVNFasciaFluviale method in StoricoParticellaDAO: "+escludiAsservimento+"\n");

      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda.longValue());
      stmt.setLong(2, idAzienda.longValue());
      
      SolmrLogger.debug(this, "Executing riepilogoZVNFasciaFluviale:"+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
        FoglioVO foglioVO = new FoglioVO();
        ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
        UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
        foglioVO.setIdAreaEFasciaFluviale(new Long(rs.getLong("TIPO")));
        if(foglioVO.getIdAreaEFasciaFluviale().longValue() == 1)
        {
          foglioVO.setDescrizioneAreaEFasciaFluviale(SolmrConstants.DESC_IN_ZVN_FASCIEFLUVIALI);
        }
        else if(foglioVO.getIdAreaEFasciaFluviale().longValue() == 2)
        {
          foglioVO.setDescrizioneAreaEFasciaFluviale(SolmrConstants.DESC_OUT_ZVN_FASCIEFLUVIALI);
        }
        storicoParticellaVO.setFoglioVO(foglioVO);
        utilizzoParticellaVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
        conduzioneParticellaVO.setElencoUtilizzi(new UtilizzoParticellaVO[]{utilizzoParticellaVO});
        storicoParticellaVO.setElencoConduzioni(new ConduzioneParticellaVO[]{conduzioneParticellaVO});
        elencoParticelle.add(storicoParticellaVO);
      } 
        
        rs.close();
        stmt.close();       
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "riepilogoZVNFasciaFluviale - SQLException: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "riepilogoZVNFasciaFluviale - Generic Exception: "+ex+"\n");
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
          SolmrLogger.error(this, "riepilogoZVNFasciaFluviale - SQLException while closing Statement and Connection: "+exc+"\n");
          throw new DataAccessException(exc.getMessage());
        }
        catch(Exception ex) {
          SolmrLogger.error(this, "riepilogoZVNFasciaFluviale - Generic Exception while closing Statement and Connection: "+ex+"\n");
          throw new DataAccessException(ex.getMessage());
        }
      }
      SolmrLogger.debug(this, "Invocated riepilogoZVNFasciaFluviale method in StoricoParticellaDAO\n");
      if(elencoParticelle.size() == 0) {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
    }
    else {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
    }
  }
  
  /**
   * Metodo per effettuare il riepilogo delle particelle per ZVN 
   * comprensivo di fascia fluviale ad una determiinata dichiarazione di 
   * consistenza
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws DataAccessException
   */
  public StoricoParticellaVO[] riepilogoZVNFasciaFluvialeDichiarate(Long idAzienda, 
      String escludiAsservimento, Long idDichiarazioneConsistenza) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating riepilogoZVNFasciaFluvialeDichiarate method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in riepilogoZVNFasciaFluvialeDichiarate method in StoricoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in riepilogoZVNFasciaFluvialeDichiarate method in StoricoParticellaDAO and it values: "+conn+"\n");
        
        
      String query = " " +
        "SELECT " +
        "       COM.TIPO, " +
        "       NVL(SUM(DECODE(COM.ID_TITOLO_POSSESSO,5,COM.SUPERFICIE_CONDOTTA,COM.SUP_UTILIZZATA)),0) AS SUP_UTILIZZATA " +
        "FROM   (SELECT  " +
        "               1 AS TIPO, " +
        "               CD.SUPERFICIE_CONDOTTA, " +
        "               CD.ID_TITOLO_POSSESSO, " +
        "               CD.ID_CONDUZIONE_DICHIARATA, " +
        "               SUM (UD.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
        "        FROM   DB_CONDUZIONE_DICHIARATA CD, " +
        "               DB_UTILIZZO_DICHIARATO UD, " +
        "               DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "               DB_STORICO_PARTICELLA SP, " + 
        "               DB_FOGLIO F " +
        "        WHERE  DC.ID_AZIENDA = ? " +
        "        AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "        AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        "        AND    CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
 //       "        AND    TRUNC(SP.DATA_INIZIO_VALIDITA) <= TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
 //       "        AND    TRUNC(NVL(SP.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY'))) > TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
        "        AND    SP.COMUNE = F.COMUNE " +
        "        AND    NVL (SP.SEZIONE, '-') = NVL (F.SEZIONE, '-') " + 
        "        AND    SP.FOGLIO = F.FOGLIO " +
        "        AND    (SP.ID_FASCIA_FLUVIALE IS NOT NULL " +
        "                OR NVL(F.ID_AREA_E,0) = 2) " +
        "        AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA(+) ";         
      if(Validator.isNotEmpty(escludiAsservimento) && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_S)) {
        query += ""+
        "        AND    CD.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
      }
      
      query += ""+
        "        GROUP BY " + 
        "               CD.SUPERFICIE_CONDOTTA, " +
        "               CD.ID_TITOLO_POSSESSO, " +
        "               CD.ID_CONDUZIONE_DICHIARATA " + 
        "        UNION " +
        "        SELECT " +
        "               2 AS TIPO, " + 
        "               CD.SUPERFICIE_CONDOTTA, " +
        "               CD.ID_TITOLO_POSSESSO, " +
        "               CD.ID_CONDUZIONE_DICHIARATA, " +
        "               SUM (UD.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " + 
        "        FROM   DB_CONDUZIONE_DICHIARATA CD, " +
        "               DB_UTILIZZO_DICHIARATO UD, " +
        "               DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "               DB_STORICO_PARTICELLA SP, " + 
        "               DB_FOGLIO F " +
        "        WHERE  DC.ID_AZIENDA = ? " +
        "        AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "        AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        "        AND    CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
        "        AND    TRUNC(SP.DATA_INIZIO_VALIDITA) <= TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
        "        AND    TRUNC(NVL(SP.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY'))) > TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
        "        AND    SP.COMUNE = F.COMUNE " +
        "        AND    NVL (SP.SEZIONE, '-') = NVL (F.SEZIONE, '-') " + 
        "        AND    SP.FOGLIO = F.FOGLIO " +
        "        AND    ID_FASCIA_FLUVIALE IS NULL " +  
        "        AND    NVL(F.ID_AREA_E,1) = 1 " +  
        "        AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA(+) ";
      if(Validator.isNotEmpty(escludiAsservimento) && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_S)) {
        query +=   "" +
        "        AND    CD.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
      }
      
      query += "" +
        "        GROUP BY " + 
        "               CD.SUPERFICIE_CONDOTTA, " +
        "               CD.ID_TITOLO_POSSESSO, " +
        "               CD.ID_CONDUZIONE_DICHIARATA) COM " +
        "GROUP BY COM.TIPO " +
        "ORDER BY COM.TIPO ASC ";
      
      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoZVNFasciaFluvialeDichiarate method in StoricoParticellaDAO: "+idAzienda+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ESCLUDI_ASSERVIMENTO] in riepilogoZVNFasciaFluvialeDichiarate method in StoricoParticellaDAO: "+escludiAsservimento+"\n");
      SolmrLogger.debug(this, "Value of parameter 3 [ID_DICHIARAZIONE_CONSISTENZA] in riepilogoZVNFasciaFluvialeDichiarate method in StoricoParticellaDAO: "+idDichiarazioneConsistenza+"\n");
      
      stmt = conn.prepareStatement(query);
       
      stmt.setLong(1, idAzienda.longValue());
      stmt.setLong(2, idDichiarazioneConsistenza.longValue());
      stmt.setLong(3, idAzienda.longValue());
      stmt.setLong(4, idDichiarazioneConsistenza.longValue());
  
      SolmrLogger.debug(this, "Executing riepilogoZVNFasciaFluvialeDichiarate:"+query+"\n");
  
      ResultSet rs = stmt.executeQuery();
  
      while(rs.next()) 
      {
        StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
        FoglioVO foglioVO = new FoglioVO();
        ConduzioneDichiarataVO conduzioneDichiarataVO = new ConduzioneDichiarataVO();
        UtilizzoDichiaratoVO utilizzoDichiaratoVO = new UtilizzoDichiaratoVO();
        foglioVO.setIdAreaEFasciaFluviale(new Long(rs.getLong("TIPO")));
        if(foglioVO.getIdAreaEFasciaFluviale().longValue() == 1)
        {
          foglioVO.setDescrizioneAreaEFasciaFluviale(SolmrConstants.DESC_IN_ZVN_FASCIEFLUVIALI);
        }
        else if(foglioVO.getIdAreaEFasciaFluviale().longValue() == 2)
        {
          foglioVO.setDescrizioneAreaEFasciaFluviale(SolmrConstants.DESC_OUT_ZVN_FASCIEFLUVIALI);
        }
        storicoParticellaVO.setFoglioVO(foglioVO);
        utilizzoDichiaratoVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
        conduzioneDichiarataVO.setElencoUtilizzi(new UtilizzoDichiaratoVO[]{utilizzoDichiaratoVO});
        storicoParticellaVO.setElencoConduzioniDichiarate(new ConduzioneDichiarataVO[]{conduzioneDichiarataVO});
        elencoParticelle.add(storicoParticellaVO);
      } 
        
      rs.close();
      stmt.close();       
    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "riepilogoZVNFasciaFluvialeDichiarate - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "riepilogoZVNFasciaFluvialeDichiarate - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) {
          stmt.close();
        }
        if(conn != null) {
          conn.close();
        }
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "riepilogoZVNFasciaFluvialeDichiarate - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "riepilogoZVNFasciaFluvialeDichiarate - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated riepilogoZVNFasciaFluvialeDichiarate method in StoricoParticellaDAO\n");
    if(elencoParticelle.size() == 0) {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
    }
    else {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
    }
  }
  
  /**
   * 
   * Metodo per effettuare il riepilogo delle particelle con basso contenuto 
   * carbonio organico 
   * 
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param onlyActive
   * @param orderBy
   * @return
   * @throws DataAccessException
   */
  public StoricoParticellaVO[] riepilogoAreaG(Long idAzienda, String escludiAsservimento) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating riepilogoAreaG method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();

    try 
    {

      SolmrLogger.debug(this, "Creating db-connection in riepilogoAreaG method in StoricoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in riepilogoAreaG method in StoricoParticellaDAO and it values: "+conn+"\n");

      String query = 
        " SELECT COM.ID_AREA_G, " +
        "        COM.DESCRIZIONE, " +
        "        NVL(SUM(DECODE(COM.ID_TITOLO_POSSESSO,5,COM.SUPERFICIE_CONDOTTA,COM.SUP_UTILIZZATA)),0) AS SUP_UTILIZZATA " +
        " FROM   (SELECT TAG.ID_AREA_G ID_AREA_G, " + 
        "                TAG.DESCRIZIONE DESCRIZIONE, " +
        "                CP.SUPERFICIE_CONDOTTA, " +
        "                CP.ID_TITOLO_POSSESSO, " +
        "                CP.ID_CONDUZIONE_PARTICELLA, " +
        "                SUM (UP.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
        "         FROM   DB_CONDUZIONE_PARTICELLA CP, " +
        "                DB_UTILIZZO_PARTICELLA UP, " +
        "                DB_UTE U, " +
        "                DB_STORICO_PARTICELLA SP, " +
        "                DB_TIPO_AREA_G TAG " +
        "         WHERE  U.ID_AZIENDA = ? " +
        "         AND    U.ID_UTE = CP.ID_UTE " +
        "         AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
        "         AND    NVL(SP.ID_AREA_G,1) = TAG.ID_AREA_G " +
        "         AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA(+) " +
        "         AND    U.DATA_FINE_ATTIVITA IS NULL " +
        "         AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "         AND    SP.DATA_FINE_VALIDITA IS NULL ";
      
      if(Validator.isNotEmpty(escludiAsservimento) && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_S)) {
        query +=   
        "         AND    CP.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
      }
      query +=       
        "         GROUP BY TAG.ID_AREA_G, " +
        "                  TAG.DESCRIZIONE, " +
        "                  CP.SUPERFICIE_CONDOTTA, " +
        "                  CP.ID_TITOLO_POSSESSO, " +
        "                  CP.ID_CONDUZIONE_PARTICELLA) COM " +
        " GROUP BY COM.ID_AREA_G, " +
        "          COM.DESCRIZIONE " +
        " ORDER BY COM.DESCRIZIONE ASC ";
      
      
      
      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoAreaG method in StoricoParticellaDAO: "+idAzienda+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ESCLUDI_ASSERVIMENTO] in riepilogoAreaG method in StoricoParticellaDAO: "+escludiAsservimento+"\n");
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda.longValue());
  
      SolmrLogger.debug(this, "Executing riepilogoAreaG:"+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
        ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
        UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
        CodeDescription code = new CodeDescription(new Integer(rs.getInt("ID_AREA_G")), rs.getString("DESCRIZIONE"));
        storicoParticellaVO.setAreaG(code);
        storicoParticellaVO.setIdAreaG(checkLongNull(rs.getString("ID_AREA_G")));
        utilizzoParticellaVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
        conduzioneParticellaVO.setElencoUtilizzi(new UtilizzoParticellaVO[]{utilizzoParticellaVO});
        storicoParticellaVO.setElencoConduzioni(new ConduzioneParticellaVO[]{conduzioneParticellaVO});
        elencoParticelle.add(storicoParticellaVO);
      } 
        
      rs.close();
      stmt.close();       
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "riepilogoAreaG - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "riepilogoAreaG - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) {
          stmt.close();
        }
        if(conn != null) {
          conn.close();
        }
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "riepilogoAreaG - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "riepilogoAreaG - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated riepilogoAreaG method in StoricoParticellaDAO\n");
    if(elencoParticelle.size() == 0) {
    return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
    }
    else 
    {
    return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
    }
  }
  
  /**
   * 
   * Metodo per effettuare il riepilogo delle particelle 
   * per basso contenuto carbonio organico
   * ad una determiinata dichiarazione di consistenza
   * 
   * 
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @param orderBy
   * @return
   * @throws DataAccessException
   */
  public StoricoParticellaVO[] riepilogoAreaGParticelleDichiarate(Long idAzienda, String escludiAsservimento, Long idDichiarazioneConsistenza) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating riepilogoAreaGParticelleDichiarate method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();

    try 
    {

      SolmrLogger.debug(this, "Creating db-connection in riepilogoAreaGParticelleDichiarate method in StoricoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in riepilogoAreaGParticelleDichiarate method in StoricoParticellaDAO and it values: "+conn+"\n");
      
      
      String query = 
        " SELECT COM.ID_AREA_G, " +
        "        COM.DESCRIZIONE, " +
        "        NVL(SUM(DECODE(COM.ID_TITOLO_POSSESSO,5,COM.SUPERFICIE_CONDOTTA,COM.SUP_UTILIZZATA)),0) AS SUP_UTILIZZATA " +
        " FROM   (SELECT TAG.ID_AREA_G, " + 
        "                TAG.DESCRIZIONE, " +
        "                CD.SUPERFICIE_CONDOTTA, " +
        "                CD.ID_TITOLO_POSSESSO, " +
        "                CD.ID_CONDUZIONE_DICHIARATA, " +
        "                SUM(UD.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
        "         FROM   DB_CONDUZIONE_DICHIARATA CD, " +
        "                DB_UTILIZZO_DICHIARATO UD, " +
        "                DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "                DB_STORICO_PARTICELLA SP, " +
        "                DB_TIPO_AREA_G TAG " +
        "         WHERE  DC.ID_AZIENDA = ? " +
        "         AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "         AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        "         AND    CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
//        "         AND    TRUNC(SP.DATA_INIZIO_VALIDITA) <= TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
//        "         AND    TRUNC(NVL(SP.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY'))) > TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
        "         AND    NVL(SP.ID_AREA_G,1) = TAG.ID_AREA_G " +
        "         AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA(+) ";
      if(Validator.isNotEmpty(escludiAsservimento) && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_S)) {
        query +=   
        "         AND    CD.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
      }
      query +=     
        "         GROUP BY TAG.ID_AREA_G, " +
        "                  TAG.DESCRIZIONE, " +
        "                  CD.SUPERFICIE_CONDOTTA, " +
        "                  CD.ID_TITOLO_POSSESSO, " +
        "                  CD.ID_CONDUZIONE_DICHIARATA) COM " +
        " GROUP BY COM.ID_AREA_G, " +
        "          COM.DESCRIZIONE " +
        " ORDER BY COM.DESCRIZIONE ASC ";
    
      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoAreaGParticelleDichiarate method in StoricoParticellaDAO: "+idAzienda+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ESCLUDI_ASSERVIMENTO] in riepilogoAreaGParticelleDichiarate method in StoricoParticellaDAO: "+escludiAsservimento+"\n");
      SolmrLogger.debug(this, "Value of parameter 3 [ID_DICHIARAZIONE_CONSISTENZA] in riepilogoAreaGParticelleDichiarate method in StoricoParticellaDAO: "+idDichiarazioneConsistenza+"\n");
 
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda.longValue());
      stmt.setLong(2, idDichiarazioneConsistenza.longValue());

      SolmrLogger.debug(this, "Executing riepilogoAreaGParticelleDichiarate:"+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
        ConduzioneDichiarataVO conduzioneDichiarataVO = new ConduzioneDichiarataVO();
        UtilizzoDichiaratoVO utilizzoDichiaratoVO = new UtilizzoDichiaratoVO();
        CodeDescription code = new CodeDescription(new Integer(rs.getInt("ID_AREA_G")), rs.getString("DESCRIZIONE"));
        storicoParticellaVO.setAreaG(code);
        storicoParticellaVO.setIdAreaG(new Long(rs.getLong("ID_AREA_G")));
        utilizzoDichiaratoVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
        conduzioneDichiarataVO.setElencoUtilizzi(new UtilizzoDichiaratoVO[]{utilizzoDichiaratoVO});
        storicoParticellaVO.setElencoConduzioniDichiarate(new ConduzioneDichiarataVO[]{conduzioneDichiarataVO});
        elencoParticelle.add(storicoParticellaVO);
      } 
      
      rs.close();
      stmt.close();       
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "riepilogoAreaGParticelleDichiarate - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "riepilogoAreaGParticelleDichiarate - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) 
        {
          stmt.close();
        }
        if(conn != null) {
          conn.close();
        }
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "riepilogoAreaGParticelleDichiarate - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "riepilogoAreaGParticelleDichiarate - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated riepilogoAreaGParticelleDichiarate method in StoricoParticellaDAO\n");
    if(elencoParticelle.size() == 0) 
    {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
    }
    else {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
    }
  }
  
  
  /**
   * 
   * Metodo per effettuare il riepilogo delle particelle con area soggetta ad erosione
   * carbonio organico 
   * 
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param onlyActive
   * @param orderBy
   * @return
   * @throws DataAccessException
   */
  public StoricoParticellaVO[] riepilogoAreaH(Long idAzienda, String escludiAsservimento) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating riepilogoAreaH method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();

    try 
    {

      SolmrLogger.debug(this, "Creating db-connection in riepilogoAreaG method in StoricoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in riepilogoAreaG method in StoricoParticellaDAO and it values: "+conn+"\n");

      String query = 
        " SELECT COM.ID_AREA_H, " +
        "        COM.DESCRIZIONE, " +
        "        NVL(SUM(DECODE(COM.ID_TITOLO_POSSESSO,5,COM.SUPERFICIE_CONDOTTA,COM.SUP_UTILIZZATA)),0) AS SUP_UTILIZZATA " +
        " FROM   (SELECT TAH.ID_AREA_H ID_AREA_H, " + 
        "                TAH.DESCRIZIONE, " +
        "                CP.SUPERFICIE_CONDOTTA, " +
        "                CP.ID_TITOLO_POSSESSO, " +
        "                CP.ID_CONDUZIONE_PARTICELLA, " +
        "                SUM (UP.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
        "         FROM   DB_CONDUZIONE_PARTICELLA CP, " +
        "                DB_UTILIZZO_PARTICELLA UP, " +
        "                DB_UTE U, " +
        "                DB_STORICO_PARTICELLA SP, " +
        "                DB_TIPO_AREA_H TAH " +
        "         WHERE  U.ID_AZIENDA = ? " +
        "         AND    U.ID_UTE = CP.ID_UTE " +
        "         AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
        "         AND    NVL(SP.ID_AREA_H,1) = TAH.ID_AREA_H " +
        "         AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA(+) " +
        "         AND    U.DATA_FINE_ATTIVITA IS NULL " +
        "         AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "         AND    SP.DATA_FINE_VALIDITA IS NULL ";
      
      if(Validator.isNotEmpty(escludiAsservimento) && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_S)) {
        query +=   
        "         AND    CP.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
      }
      query +=       
        "         GROUP BY TAH.ID_AREA_H, " +
        "                  TAH.DESCRIZIONE, " +
        "                  CP.SUPERFICIE_CONDOTTA, " +
        "                  CP.ID_TITOLO_POSSESSO, " +
        "                  CP.ID_CONDUZIONE_PARTICELLA) COM " +
        " GROUP BY COM.ID_AREA_H, " +
        "          COM.DESCRIZIONE " +
        " ORDER BY COM.DESCRIZIONE ASC ";
      
      
      
      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoAreaH method in StoricoParticellaDAO: "+idAzienda+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ESCLUDI_ASSERVIMENTO] in riepilogoAreaH method in StoricoParticellaDAO: "+escludiAsservimento+"\n");
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda.longValue());
  
      SolmrLogger.debug(this, "Executing riepilogoAreaH:"+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
        ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
        UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
        CodeDescription code = new CodeDescription(new Integer(rs.getInt("ID_AREA_H")), rs.getString("DESCRIZIONE"));
        storicoParticellaVO.setAreaH(code);
        storicoParticellaVO.setIdAreaH(checkLongNull(rs.getString("ID_AREA_H")));
        utilizzoParticellaVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
        conduzioneParticellaVO.setElencoUtilizzi(new UtilizzoParticellaVO[]{utilizzoParticellaVO});
        storicoParticellaVO.setElencoConduzioni(new ConduzioneParticellaVO[]{conduzioneParticellaVO});
        elencoParticelle.add(storicoParticellaVO);
      } 
        
      rs.close();
      stmt.close();       
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "riepilogoAreaH - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "riepilogoAreaH - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) {
          stmt.close();
        }
        if(conn != null) {
          conn.close();
        }
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "riepilogoAreaH - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "riepilogoAreaH - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated riepilogoAreaH method in StoricoParticellaDAO\n");
    if(elencoParticelle.size() == 0) {
    return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
    }
    else 
    {
    return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
    }
  }
  
  
  /**
   * 
   * Metodo per effettuare il riepilogo delle particelle 
   * per area soggetta ad erosione
   * ad una determiinata dichiarazione di consistenza
   * 
   * 
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @param orderBy
   * @return
   * @throws DataAccessException
   */
  public StoricoParticellaVO[] riepilogoAreaHParticelleDichiarate(Long idAzienda, String escludiAsservimento, Long idDichiarazioneConsistenza) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating riepilogoAreaHParticelleDichiarate method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();

    try 
    {

      SolmrLogger.debug(this, "Creating db-connection in riepilogoAreaHParticelleDichiarate method in StoricoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in riepilogoAreaHParticelleDichiarate method in StoricoParticellaDAO and it values: "+conn+"\n");
      
      
      String query = 
        " SELECT COM.ID_AREA_H, " +
        "        COM.DESCRIZIONE, " +
        "        NVL(SUM(DECODE(COM.ID_TITOLO_POSSESSO,5,COM.SUPERFICIE_CONDOTTA,COM.SUP_UTILIZZATA)),0) AS SUP_UTILIZZATA " +
        " FROM   (SELECT TAH.ID_AREA_H, " + 
        "                TAH.DESCRIZIONE, " +
        "                CD.SUPERFICIE_CONDOTTA, " +
        "                CD.ID_TITOLO_POSSESSO, " +
        "                CD.ID_CONDUZIONE_DICHIARATA, " +
        "                SUM(UD.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
        "         FROM   DB_CONDUZIONE_DICHIARATA CD, " +
        "                DB_UTILIZZO_DICHIARATO UD, " +
        "                 DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "                DB_STORICO_PARTICELLA SP, " +
        "                DB_TIPO_AREA_H TAH " +
        "         WHERE  DC.ID_AZIENDA = ? " +
        "         AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "         AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        "         AND    CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
   //     "         AND    TRUNC(SP.DATA_INIZIO_VALIDITA) <= TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
   //     "         AND    TRUNC(NVL(SP.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY'))) > TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
        "         AND    NVL(SP.ID_AREA_H,1) = TAH.ID_AREA_H " +
        "         AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA(+) ";
      if(Validator.isNotEmpty(escludiAsservimento) && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_S)) {
        query +=   
        "         AND    CD.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
      }
      query +=     
        "         GROUP BY TAH.ID_AREA_H, " +
        "                  TAH.DESCRIZIONE, " +
        "                  CD.SUPERFICIE_CONDOTTA, " +
        "                  CD.ID_TITOLO_POSSESSO, " +
        "                  CD.ID_CONDUZIONE_DICHIARATA) COM " +
        " GROUP BY COM.ID_AREA_H, " +
        "          COM.DESCRIZIONE " +
        " ORDER BY COM.DESCRIZIONE ASC ";
    
      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoAreaHParticelleDichiarate method in StoricoParticellaDAO: "+idAzienda+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ESCLUDI_ASSERVIMENTO] in riepilogoAreaHParticelleDichiarate method in StoricoParticellaDAO: "+escludiAsservimento+"\n");
      SolmrLogger.debug(this, "Value of parameter 3 [ID_DICHIARAZIONE_CONSISTENZA] in riepilogoAreaHParticelleDichiarate method in StoricoParticellaDAO: "+idDichiarazioneConsistenza+"\n");
 
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda.longValue());
      stmt.setLong(2, idDichiarazioneConsistenza.longValue());

      SolmrLogger.debug(this, "Executing riepilogoAreaHParticelleDichiarate:"+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
        ConduzioneDichiarataVO conduzioneDichiarataVO = new ConduzioneDichiarataVO();
        UtilizzoDichiaratoVO utilizzoDichiaratoVO = new UtilizzoDichiaratoVO();
        CodeDescription code = new CodeDescription(new Integer(rs.getInt("ID_AREA_H")), rs.getString("DESCRIZIONE"));
        storicoParticellaVO.setAreaH(code);
        storicoParticellaVO.setIdAreaH(new Long(rs.getLong("ID_AREA_H")));
        utilizzoDichiaratoVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
        conduzioneDichiarataVO.setElencoUtilizzi(new UtilizzoDichiaratoVO[]{utilizzoDichiaratoVO});
        storicoParticellaVO.setElencoConduzioniDichiarate(new ConduzioneDichiarataVO[]{conduzioneDichiarataVO});
        elencoParticelle.add(storicoParticellaVO);
      } 
      
      rs.close();
      stmt.close();       
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "riepilogoAreaHParticelleDichiarate - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "riepilogoAreaHParticelleDichiarate - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) 
        {
          stmt.close();
        }
        if(conn != null) {
          conn.close();
        }
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "riepilogoAreaHParticelleDichiarate - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "riepilogoAreaHParticelleDichiarate - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated riepilogoAreaHParticelleDichiarate method in StoricoParticellaDAO\n");
    if(elencoParticelle.size() == 0) 
    {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
    }
    else {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
    }
  }
  
  /**
   * 
   * Metodo per effettuare il riepilogo delle particelle per Zona Altimetrica
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param onlyActive
   * @param orderBy
   * @return
   * @throws DataAccessException
   */
  public StoricoParticellaVO[] riepilogoZonaAltimetrica(Long idAzienda, String escludiAsservimento) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating riepilogoZonaAltimetrica method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();

    try 
    {

      SolmrLogger.debug(this, "Creating db-connection in riepilogoZonaAltimetrica method in StoricoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in riepilogoZonaAltimetrica method in StoricoParticellaDAO and it values: "+conn+"\n");

      String query = 
        " SELECT COM.ID_ZONA_ALTIMETRICA, " +
        "        COM.DESCRIZIONE, " +
        "        NVL(SUM(DECODE(COM.ID_TITOLO_POSSESSO,5,COM.SUPERFICIE_CONDOTTA,COM.SUP_UTILIZZATA)),0) AS SUP_UTILIZZATA " +
        " FROM   (SELECT TAZ.ID_ZONA_ALTIMETRICA ID_ZONA_ALTIMETRICA, " + 
        "                TAZ.DESCRIZIONE, " +
        "                CP.SUPERFICIE_CONDOTTA, " +
        "                CP.ID_TITOLO_POSSESSO, " +
        "                CP.ID_CONDUZIONE_PARTICELLA, " +
        "                SUM (UP.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
        "         FROM   DB_CONDUZIONE_PARTICELLA CP, " +
        "                DB_UTILIZZO_PARTICELLA UP, " +
        "                DB_UTE U, " +
        "                DB_STORICO_PARTICELLA SP, " +
        "                DB_TIPO_ZONA_ALTIMETRICA TAZ " +
        "         WHERE  U.ID_AZIENDA = ? " +
        "         AND    U.ID_UTE = CP.ID_UTE " +
        "         AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
        "         AND    SP.ID_ZONA_ALTIMETRICA = TAZ.ID_ZONA_ALTIMETRICA " +
        "         AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA(+) " +
        "         AND    U.DATA_FINE_ATTIVITA IS NULL " +
        "         AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "         AND    SP.DATA_FINE_VALIDITA IS NULL ";
      
      if(Validator.isNotEmpty(escludiAsservimento) && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_S)) {
        query +=   
        "         AND    CP.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
      }
      query +=       
        "         GROUP BY TAZ.ID_ZONA_ALTIMETRICA, " +
        "                  TAZ.DESCRIZIONE, " +
        "                  CP.SUPERFICIE_CONDOTTA, " +
        "                  CP.ID_TITOLO_POSSESSO, " +
        "                  CP.ID_CONDUZIONE_PARTICELLA) COM " +
        " GROUP BY COM.ID_ZONA_ALTIMETRICA, " +
        "          COM.DESCRIZIONE " +
        " ORDER BY COM.DESCRIZIONE ASC ";
      
      
      
      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoZonaAltimetrica method in StoricoParticellaDAO: "+idAzienda+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ESCLUDI_ASSERVIMENTO] in riepilogoZonaAltimetrica method in StoricoParticellaDAO: "+escludiAsservimento+"\n");

      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda.longValue());
  
      SolmrLogger.debug(this, "Executing riepilogoZonaAltimetrica:"+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
        ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
        UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
        CodeDescription code = new CodeDescription(new Integer(rs.getInt("ID_ZONA_ALTIMETRICA")), rs.getString("DESCRIZIONE"));
        storicoParticellaVO.setZonaAltimetrica(code);
        storicoParticellaVO.setIdZonaAltimetrica(new Long(rs.getLong("ID_ZONA_ALTIMETRICA")));
        utilizzoParticellaVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
        conduzioneParticellaVO.setElencoUtilizzi(new UtilizzoParticellaVO[]{utilizzoParticellaVO});
        storicoParticellaVO.setElencoConduzioni(new ConduzioneParticellaVO[]{conduzioneParticellaVO});
        elencoParticelle.add(storicoParticellaVO);
      } 
        
      rs.close();
      stmt.close();       
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "riepilogoZonaAltimetrica - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "riepilogoZonaAltimetrica - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) {
          stmt.close();
        }
        if(conn != null) {
          conn.close();
        }
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "riepilogoZonaAltimetrica - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "riepilogoZonaAltimetrica - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated riepilogoZonaAltimetrica method in StoricoParticellaDAO\n");
    if(elencoParticelle.size() == 0) {
    return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
    }
    else 
    {
    return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
    }
  }
  
  /**
   * 
   * Metodo per effettuare il riepilogo delle particelle per Zona Altimetrica
   * ad una determiinata dichiarazione di consistenza
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @param orderBy
   * @return
   * @throws DataAccessException
   */
  public StoricoParticellaVO[] riepilogoZonaAltimetricaParticelleDichiarate(Long idAzienda, String escludiAsservimento, Long idDichiarazioneConsistenza) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating riepilogoZonaAltimetricaParticelleDichiarate method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();

    try 
    {

      SolmrLogger.debug(this, "Creating db-connection in riepilogoZonaAltimetricaParticelleDichiarate method in StoricoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in riepilogoZonaAltimetricaParticelleDichiarate method in StoricoParticellaDAO and it values: "+conn+"\n");
      
      
      String query = 
        " SELECT COM.ID_ZONA_ALTIMETRICA, " +
        "        COM.DESCRIZIONE, " +
        "        NVL(SUM(DECODE(COM.ID_TITOLO_POSSESSO,5,COM.SUPERFICIE_CONDOTTA,COM.SUP_UTILIZZATA)),0) AS SUP_UTILIZZATA " +
        " FROM   (SELECT TAZ.ID_ZONA_ALTIMETRICA ID_ZONA_ALTIMETRICA, " + 
        "                TAZ.DESCRIZIONE, " +
        "                CD.SUPERFICIE_CONDOTTA, " +
        "                CD.ID_TITOLO_POSSESSO, " +
        "                CD.ID_CONDUZIONE_DICHIARATA, " +
        "                SUM(UD.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
        "         FROM   DB_CONDUZIONE_DICHIARATA CD, " +
        "                DB_UTILIZZO_DICHIARATO UD, " +
        "                DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "                DB_STORICO_PARTICELLA SP, " +
        "                DB_TIPO_ZONA_ALTIMETRICA TAZ " +
        "         WHERE  DC.ID_AZIENDA = ? " +
        "         AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "         AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        "         AND    CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
  //      "         AND    TRUNC(SP.DATA_INIZIO_VALIDITA) <= TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
  //      "         AND    TRUNC(NVL(SP.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY'))) > TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
        "         AND    SP.ID_ZONA_ALTIMETRICA = TAZ.ID_ZONA_ALTIMETRICA " +
        "         AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA(+) ";
      if(Validator.isNotEmpty(escludiAsservimento) && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_S)) {
        query +=   
        "         AND    CD.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
      }
      query +=     
        "         GROUP BY TAZ.ID_ZONA_ALTIMETRICA, " +
        "                  TAZ.DESCRIZIONE, " +
        "                  CD.SUPERFICIE_CONDOTTA, " +
        "                  CD.ID_TITOLO_POSSESSO, " +
        "                  CD.ID_CONDUZIONE_DICHIARATA) COM " +
        " GROUP BY COM.ID_ZONA_ALTIMETRICA, " +
        "          COM.DESCRIZIONE " +
        " ORDER BY COM.DESCRIZIONE ASC ";
      
      
    
      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoZonaAltimetricaParticelleDichiarate method in StoricoParticellaDAO: "+idAzienda+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ESCLUDI_ASSERVIMENTO] in riepilogoZonaAltimetricaParticelleDichiarate method in StoricoParticellaDAO: "+escludiAsservimento+"\n");
      SolmrLogger.debug(this, "Value of parameter 3 [ID_DICHIARAZIONE_CONSISTENZA] in riepilogoZonaAltimetricaParticelleDichiarate method in StoricoParticellaDAO: "+idDichiarazioneConsistenza+"\n");
 
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda.longValue());
      stmt.setLong(2, idDichiarazioneConsistenza.longValue());

      SolmrLogger.debug(this, "Executing riepilogoZonaAltimetricaParticelleDichiarate:"+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
        ConduzioneDichiarataVO conduzioneDichiarataVO = new ConduzioneDichiarataVO();
        UtilizzoDichiaratoVO utilizzoDichiaratoVO = new UtilizzoDichiaratoVO();
        CodeDescription code = new CodeDescription(new Integer(rs.getInt("ID_ZONA_ALTIMETRICA")), rs.getString("DESCRIZIONE"));
        storicoParticellaVO.setZonaAltimetrica(code);
        storicoParticellaVO.setIdZonaAltimetrica(new Long(rs.getLong("ID_ZONA_ALTIMETRICA")));
        utilizzoDichiaratoVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
        conduzioneDichiarataVO.setElencoUtilizzi(new UtilizzoDichiaratoVO[]{utilizzoDichiaratoVO});
        storicoParticellaVO.setElencoConduzioniDichiarate(new ConduzioneDichiarataVO[]{conduzioneDichiarataVO});
        elencoParticelle.add(storicoParticellaVO);
      } 
      
      rs.close();
      stmt.close();       
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "riepilogoZonaAltimetricaParticelleDichiarate - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "riepilogoZonaAltimetricaParticelleDichiarate - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) 
        {
          stmt.close();
        }
        if(conn != null) {
          conn.close();
        }
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "riepilogoZonaAltimetricaParticelleDichiarate - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "riepilogoZonaAltimetricaParticelleDichiarate - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated riepilogoZonaAltimetricaParticelleDichiarate method in StoricoParticellaDAO\n");
    if(elencoParticelle.size() == 0) 
    {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
    }
    else {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
    }
  }
  
  /**
   * 
   * Metodo per effettuare il riepilogo delle particelle per Caso Particolare
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param onlyActive
   * @param orderBy
   * @return
   * @throws DataAccessException
   */
  public StoricoParticellaVO[] riepilogoCasoParticolare(Long idAzienda, String escludiAsservimento) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating riepilogoCasoParticolare method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();

    try 
    {

      SolmrLogger.debug(this, "Creating db-connection in riepilogoCasoParticolare method in StoricoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in riepilogoCasoParticolare method in StoricoParticellaDAO and it values: "+conn+"\n");

      String query = 
        " SELECT COM.ID_CASO_PARTICOLARE, " +
        "        COM.DESCRIZIONE, " +
        "        NVL(SUM(DECODE(COM.ID_TITOLO_POSSESSO,5,COM.SUPERFICIE_CONDOTTA,COM.SUP_UTILIZZATA)),0) AS SUP_UTILIZZATA " +
        " FROM   (SELECT TAC.ID_CASO_PARTICOLARE ID_CASO_PARTICOLARE, " + 
        "                TAC.DESCRIZIONE, " +
        "                CP.SUPERFICIE_CONDOTTA, " +
        "                CP.ID_TITOLO_POSSESSO, " +
        "                CP.ID_CONDUZIONE_PARTICELLA, " +
        "                SUM (UP.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
        "         FROM   DB_CONDUZIONE_PARTICELLA CP, " +
        "                DB_UTILIZZO_PARTICELLA UP, " +
        "                DB_UTE U, " +
        "                DB_STORICO_PARTICELLA SP, " +
        "                DB_TIPO_CASO_PARTICOLARE TAC " +
        "         WHERE  U.ID_AZIENDA = ? " +
        "         AND    U.ID_UTE = CP.ID_UTE " +
        "         AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
        "         AND    SP.ID_CASO_PARTICOLARE = TAC.ID_CASO_PARTICOLARE " +
        "         AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA(+) "+
        "         AND    U.DATA_FINE_ATTIVITA IS NULL " +
        "         AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "         AND    SP.DATA_FINE_VALIDITA IS NULL ";
      
      if(Validator.isNotEmpty(escludiAsservimento) && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_S)) {
        query +=   
        "         AND    CP.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
      }
      query +=       
        "         GROUP BY TAC.ID_CASO_PARTICOLARE, " +
        "                  TAC.DESCRIZIONE, " +
        "                  CP.SUPERFICIE_CONDOTTA, " +
        "                  CP.ID_TITOLO_POSSESSO, " +
        "                  CP.ID_CONDUZIONE_PARTICELLA) COM " +
        " GROUP BY COM.ID_CASO_PARTICOLARE, " +
        "          COM.DESCRIZIONE " +
        " ORDER BY COM.DESCRIZIONE ASC ";
      
      
      
      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoCasoParticolare method in StoricoParticellaDAO: "+idAzienda+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ESCLUDI_ASSERVIMENTO] in riepilogoCasoParticolare method in StoricoParticellaDAO: "+escludiAsservimento+"\n");
 
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda.longValue());
  
      SolmrLogger.debug(this, "Executing riepilogoCasoParticolare:"+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
        ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
        UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
        CodeDescription code = new CodeDescription(new Integer(rs.getInt("ID_CASO_PARTICOLARE")), rs.getString("DESCRIZIONE"));
        storicoParticellaVO.setCasoParticolare(code);
        storicoParticellaVO.setIdCasoParticolare(new Long(rs.getLong("ID_CASO_PARTICOLARE")));
        utilizzoParticellaVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
        conduzioneParticellaVO.setElencoUtilizzi(new UtilizzoParticellaVO[]{utilizzoParticellaVO});
        storicoParticellaVO.setElencoConduzioni(new ConduzioneParticellaVO[]{conduzioneParticellaVO});
        elencoParticelle.add(storicoParticellaVO);
      } 
        
      rs.close();
      stmt.close();       
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "riepilogoCasoParticolare - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "riepilogoCasoParticolare - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) {
          stmt.close();
        }
        if(conn != null) {
          conn.close();
        }
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "riepilogoCasoParticolare - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "riepilogoCasoParticolare - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated riepilogoCasoParticolare method in StoricoParticellaDAO\n");
    if(elencoParticelle.size() == 0) {
    return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
    }
    else 
    {
    return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
    }
  }
  
  
  /**
   * 
   * Metodo per effettuare il riepilogo delle particelle per Caso Particolare 
   * ad una determiinata dichiarazione di consistenza
   * 
   * @param idAzienda
   * @param escludiAsservimento
   * @param idDichiarazioneConsistenza
   * @param orderBy
   * @return
   * @throws DataAccessException
   */
  public StoricoParticellaVO[] riepilogoCasoParticolareParticelleDichiarate(Long idAzienda, String escludiAsservimento, Long idDichiarazioneConsistenza) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating riepilogoCasoParticolareParticelleDichiarate method in StoricoParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();

    try 
    {

      SolmrLogger.debug(this, "Creating db-connection in riepilogoCasoParticolareParticelleDichiarate method in StoricoParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in riepilogoCasoParticolareParticelleDichiarate method in StoricoParticellaDAO and it values: "+conn+"\n");
      
      
      String query = 
        " SELECT COM.ID_CASO_PARTICOLARE, " +
        "        COM.DESCRIZIONE, " +
        "        NVL(SUM(DECODE(COM.ID_TITOLO_POSSESSO,5,COM.SUPERFICIE_CONDOTTA,COM.SUP_UTILIZZATA)),0) AS SUP_UTILIZZATA " +
        " FROM   (SELECT TAC.ID_CASO_PARTICOLARE ID_CASO_PARTICOLARE, " + 
        "                TAC.DESCRIZIONE, " +
        "                CD.SUPERFICIE_CONDOTTA, " +
        "                CD.ID_TITOLO_POSSESSO, " +
        "                CD.ID_CONDUZIONE_DICHIARATA, " +
        "                SUM(UD.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
        "         FROM   DB_CONDUZIONE_DICHIARATA CD, " +
        "                DB_UTILIZZO_DICHIARATO UD, " +
        "                DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "                DB_STORICO_PARTICELLA SP, " +
        "                DB_TIPO_CASO_PARTICOLARE TAC " +
        "         WHERE  DC.ID_AZIENDA = ? " +
        "         AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "         AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        "         AND    CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
 //       "         AND    TRUNC(SP.DATA_INIZIO_VALIDITA) <= TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
 //       "         AND    TRUNC(NVL(SP.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY'))) > TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
        "         AND    SP.ID_CASO_PARTICOLARE = TAC.ID_CASO_PARTICOLARE " +
        "         AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA(+) ";
      if(Validator.isNotEmpty(escludiAsservimento) && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_S)) {
        query +=   
        "         AND    CD.ID_TITOLO_POSSESSO <> "+SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
      }
      query +=     
        "         GROUP BY TAC.ID_CASO_PARTICOLARE, " +
        "          TAC.DESCRIZIONE, " +
        "          CD.SUPERFICIE_CONDOTTA, " +
        "          CD.ID_TITOLO_POSSESSO, " +
        "          CD.ID_CONDUZIONE_DICHIARATA) COM " +
        " GROUP BY COM.ID_CASO_PARTICOLARE, " +
        "          COM.DESCRIZIONE " +
        " ORDER BY COM.DESCRIZIONE ASC ";
      
      
    
      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoCasoParticolareParticelleDichiarate method in StoricoParticellaDAO: "+idAzienda+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ESCLUDI_ASSERVIMENTO] in riepilogoCasoParticolareParticelleDichiarate method in StoricoParticellaDAO: "+escludiAsservimento+"\n");
      SolmrLogger.debug(this, "Value of parameter 3 [ID_DICHIARAZIONE_CONSISTENZA] in riepilogoCasoParticolareParticelleDichiarate method in StoricoParticellaDAO: "+idDichiarazioneConsistenza+"\n");
 
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda.longValue());
      stmt.setLong(2, idDichiarazioneConsistenza.longValue());

      SolmrLogger.debug(this, "Executing riepilogoCasoParticolareParticelleDichiarate:"+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
        ConduzioneDichiarataVO conduzioneDichiarataVO = new ConduzioneDichiarataVO();
        UtilizzoDichiaratoVO utilizzoDichiaratoVO = new UtilizzoDichiaratoVO();
        CodeDescription code = new CodeDescription(new Integer(rs.getInt("ID_CASO_PARTICOLARE")), rs.getString("DESCRIZIONE"));
        storicoParticellaVO.setCasoParticolare(code);
        storicoParticellaVO.setIdCasoParticolare(new Long(rs.getLong("ID_CASO_PARTICOLARE")));
        utilizzoDichiaratoVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
        conduzioneDichiarataVO.setElencoUtilizzi(new UtilizzoDichiaratoVO[]{utilizzoDichiaratoVO});
        storicoParticellaVO.setElencoConduzioniDichiarate(new ConduzioneDichiarataVO[]{conduzioneDichiarataVO});
        elencoParticelle.add(storicoParticellaVO);
      } 
      
      rs.close();
      stmt.close();       
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "riepilogoCasoParticolareParticelleDichiarate - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "riepilogoCasoParticolareParticelleDichiarate - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) 
        {
          stmt.close();
        }
        if(conn != null) {
          conn.close();
        }
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "riepilogoCasoParticolareParticelleDichiarate - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "riepilogoCasoParticolareParticelleDichiarate - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated riepilogoCasoParticolareParticelleDichiarate method in StoricoParticellaDAO\n");
    if(elencoParticelle.size() == 0) 
    {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[0]);
    }
    else {
      return (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
    }
  }
}
