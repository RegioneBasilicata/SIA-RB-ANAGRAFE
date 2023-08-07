package it.csi.solmr.integration.anag;

import it.csi.smranag.smrgaa.dto.SuperficieDescription;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.dto.terreni.TipoAreaVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoDestinazioneVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoDettaglioUsoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoFaseAllevamentoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoPeriodoSeminaVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoPraticaMantenimentoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoQualitaUsoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoSeminaVO;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.smranag.smrgaa.util.PaginazioneUtils;
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
import it.csi.solmr.dto.anag.terreni.ConduzioneParticellaVO;
import it.csi.solmr.dto.anag.terreni.FiltriParticellareRicercaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.dto.anag.terreni.StoricoUnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.TipoMacroUsoVO;
import it.csi.solmr.dto.anag.terreni.TipoUtilizzoVO;
import it.csi.solmr.dto.anag.terreni.TipoVarietaVO;
import it.csi.solmr.dto.anag.terreni.UtilizzoParticellaVO;
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
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

public class ConduzioneParticellaDAO extends it.csi.solmr.integration.BaseDAO
{
	
	public ConduzioneParticellaDAO() throws ResourceAccessException
	{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}
	
	public ConduzioneParticellaDAO(String refName) throws ResourceAccessException
	{
		super(refName);
	}
	
	/**
	 * Metodo che mi restituisce l'ultima data nella quale sono stati effettuati
	 * controlli di validità relativi alla tabella DB_CONDUZIONE_PARTICELLA
	 * 
	 * @param idAzienda
	 * @return java.lang.String
	 * @throws DataAccessException
	 */
	public java.lang.String getMaxDataEsecuzioneControlliConduzioneParticella(Long idAzienda) throws DataAccessException
	{
		SolmrLogger.debug(this, "Invocating getMaxDataEsecuzioneControlliConduzioneParticella method in ConduzioneParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		java.lang.String dataEsecuzioneControlli = null;
		
		try
		{
			SolmrLogger.debug(this, "Creating db-connection in getMaxDataEsecuzioneControlliConduzioneParticella method in ConduzioneParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this,
					"Created db-connection in getMaxDataEsecuzioneControlliConduzioneParticella method in ConduzioneParticellaDAO and it values: " + conn
							+ "\n");
			
			String query = " SELECT MAX(CP.DATA_ESECUZIONE) AS DATA_ESECUZIONE" + " FROM   DB_CONDUZIONE_PARTICELLA CP, " + "        DB_UTE U "
					+ " WHERE  U.ID_AZIENDA = ? " + " AND    CP.ID_UTE = U.ID_UTE ";
			
			SolmrLogger.debug(this,
					"Value of parameter 1 [ID_AZIENDA] in getMaxDataEsecuzioneControlliConduzioneParticella method in ConduzioneParticellaDAO: " + idAzienda
							+ "\n");
			
			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());
			
			SolmrLogger.debug(this, "Executing getMaxDataEsecuzioneControlliConduzioneParticella: " + query + "\n");
			
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next())
			{
				dataEsecuzioneControlli = rs.getString("DATA_ESECUZIONE");
			}
			
			rs.close();
			stmt.close();
			
		}
		catch (SQLException exc)
		{
			SolmrLogger.error(this, "getMaxDataEsecuzioneControlliConduzioneParticella in ConduzioneParticellaDAO - SQLException: " + exc.getMessage()
					+ "\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex)
		{
			SolmrLogger.error(this, "getMaxDataEsecuzioneControlliConduzioneParticella in ConduzioneParticellaDAO - Generic Exception: " + ex + "\n");
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
						"getMaxDataEsecuzioneControlliConduzioneParticella in ConduzioneParticellaDAO - SQLException while closing Statement and Connection: "
								+ exc.getMessage() + "\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex)
			{
				SolmrLogger.error(this,
						"getMaxDataEsecuzioneControlliConduzioneParticella in ConduzioneParticellaDAO - Generic Exception while closing Statement and Connection: "
								+ ex.getMessage() + "\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getMaxDataEsecuzioneControlliConduzioneParticella method in ConduzioneParticellaDAO\n");
		return dataEsecuzioneControlli;
	}
	
	/**
	 * Metodo che mi restituisce l'elenco delle particelle in tutte le sue
	 * componenti (DB_STORICO_PARTICELLA, DB_CONDUZIONE_PARTICELLA,
	 * DB_UTILIZZO_PARTICELLA) in relazione dei parametri di ricerca impostati, di
	 * un criterio di ordinamento e dell'azienda selezionata
	 * 
	 * @param filtriParticellareRicercaVO
	 * @param idAzienda
	 * @return java.util.Vector
	 * @throws DataAccessException
	 */
	public Vector<StoricoParticellaVO> searchParticelleByParameters(FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda, BigDecimal p26Valore)
			throws DataAccessException
	{
		SolmrLogger.debug(this, "Invocating searchParticelleByParameters method in ConduzioneParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();
		String listOfParameters = "";
		
		// Creo l'oggetto Stop Watch per monitorare le operazioni eseguite
		// all'interno del metodo
		//StopWatch watcher = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
		
		try
		{
			
			// START
			//watcher.start();
			
			SolmrLogger.debug(this, "Creating db-connection in searchParticelleByParameters method in ConduzioneParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in searchParticelleByParameters method in ConduzioneParticellaDAO and it values: " + conn + "\n");
			
			// Query per i conrolli GIS
			String query = "";
			
			if (filtriParticellareRicercaVO.getIdUtilizzo().intValue() != 0){
			  SolmrLogger.debug(this,"-- IdUtilizzo ="+filtriParticellareRicercaVO.getIdUtilizzo().intValue());
  			query += "" +
  					"SELECT SP.ID_STORICO_PARTICELLA, " +
  					"       CP.ID_CONDUZIONE_PARTICELLA, " +
  					"       CP.ESITO_CONTROLLO, "	+
  					"       C.DESCOM, " +
  					"       P.SIGLA_PROVINCIA, " +
  					"       SP.SEZIONE, " +
  					"       SP.FOGLIO, " +
  					"       SP.PARTICELLA, " +
  					"       SP.SUBALTERNO, " +
  					"       SP.COMUNE, " +
  					"       SP.SUP_CATASTALE, " +
  					"       SP.SUPERFICIE_GRAFICA, " +
  					"       SP.ID_ZONA_ALTIMETRICA, "	+
  					"       CP.ID_TITOLO_POSSESSO, " +
  					"       TTP.DESCRIZIONE AS DESC_POSSESSO, " +
  					"       SP.ID_AREA_C, "	+
  					"       CP.SUPERFICIE_CONDOTTA, " +
  					"       CP.PERCENTUALE_POSSESSO, " +
  					"       CP.DATA_INIZIO_CONDUZIONE, " +
  					"       CP.DATA_FINE_CONDUZIONE, " +
  					"       CP.RECORD_MODIFICATO, " +
  					"       UP.ID_UTILIZZO_PARTICELLA, " +
  					"       UP.ID_CATALOGO_MATRICE, " +
  					"       TU.CODICE AS COD_PRIMARIO, " +
  					"       TU.DESCRIZIONE AS DESC_PRIMARIO, " +
  					"       TV.DESCRIZIONE AS DESC_VARIETA, " +
  					"       TV.CODICE_VARIETA AS COD_VARIETA, "	+
  					"       UP.SUPERFICIE_UTILIZZATA, " +
  					"       UP.ID_CATALOGO_MATRICE_SECONDARIO, " +
  					"       RCM2.ID_UTILIZZO AS ID_UTILIZZO_SECONDARIO, " +
  					"       TU2.CODICE AS COD_SECONDARIO, "	+
  					"       TU2.DESCRIZIONE AS DESC_SECONDARIO, " +
  					"       TV2.DESCRIZIONE AS VAR_SECONDARIA, " +
  					"       TV2.CODICE_VARIETA AS COD_VAR_SECONDARIA, " +
  					"       UP.SUP_UTILIZZATA_SECONDARIA, " +
  					"       SP.ID_CASO_PARTICOLARE, "	+
  					"       TCP.DESCRIZIONE AS DESC_PARTICOLARE, " +
  					"       CP.ID_UTE, " +
  					"       UP.ANNO_IMPIANTO, " +
  					"       UP.NUMERO_PIANTE_CEPPI, "	+
  					"       PART.FLAG_SCHEDARIO , " +
  					"       TMU.CODICE, " +
  					"       TMU.DESCRIZIONE AS DESC_MACRO, " +
  					"       CP.SUPERFICIE_AGRONOMICA, " +
  					"       RCM.ID_VARIETA, " +
  					"       SP.ID_PARTICELLA," +
  					"       F.FLAG_STABILIZZAZIONE, " +
  					"       RCM.ID_TIPO_DESTINAZIONE AS ID_TIPO_DEST_PRIM, " +
  	        "       TDE.CODICE_DESTINAZIONE AS COD_DEST_USO_PRIM, " +
  	        "       TDE.DESCRIZIONE_DESTINAZIONE AS DESC_DEST_USO_PRIM, " +
  	        "       RCM2.ID_TIPO_DESTINAZIONE AS ID_TIPO_DEST_SEC, " +
  	        "       TDE2.CODICE_DESTINAZIONE AS COD_DEST_USO_SEC, " +
  	        "       TDE2.DESCRIZIONE_DESTINAZIONE AS DESC_DEST_USO_SEC, " +
  	        "       RCM.ID_TIPO_DETTAGLIO_USO, " +
            "       TDU.CODICE_DETTAGLIO_USO AS COD_DETT_USO_PRIM, " +
            "       TDU.DESCRIZIONE_DETTAGLIO_USO AS DESC_DETT_USO_PRIM, " +
            "       RCM2.ID_TIPO_DETTAGLIO_USO AS ID_TIPO_DETT_USO_SECONDARIO, " +
            "       TDU2.CODICE_DETTAGLIO_USO AS COD_DETT_USO_SEC, " +
            "       TDU2.DESCRIZIONE_DETTAGLIO_USO AS DESC_DETT_USO_SEC, " +
            "       RCM.ID_TIPO_QUALITA_USO AS ID_TIPO_QUALITA_USO_PRIM, " +
            "       TQU.CODICE_QUALITA_USO AS COD_QUALITA_USO_PRIM, " +
            "       TQU.DESCRIZIONE_QUALITA_USO AS DESC_QUALITA_USO_PRIM, " +
            "       RCM2.ID_TIPO_QUALITA_USO AS ID_TIPO_QUALITA_USO_SEC, " +
            "       TQU2.CODICE_QUALITA_USO AS COD_QUALITA_USO_SEC, " +
            "       TQU2.DESCRIZIONE_QUALITA_USO AS DESC_QUALITA_USO_SEC, " +
  	        "       UP.ID_TIPO_PERIODO_SEMINA, " +
  	        "       TPS.CODICE AS COD_PER_SEM_PRIM, " +
  	        "       TPS.DESCRIZIONE AS DESC_PER_SEM_PRIM, " +
  	        "       UP.ID_TIPO_PERIODO_SEMINA_SECOND, " +
  	        "       TPS2.CODICE AS COD_PER_SEM_SEC, " +
  	        "       TPS2.DESCRIZIONE AS DESC_PER_SEM_SEC, " +
  	        "       UP.ID_SEMINA, " +
            "       TSE.CODICE_SEMINA AS COD_SEM_PRIM, " +
            "       TSE.DESCRIZIONE_SEMINA AS DESC_SEM_PRIM, " +
            "       UP.ID_SEMINA_SECONDARIA, " +
            "       TSE2.CODICE_SEMINA AS COD_SEM_SEC, " +
            "       TSE2.DESCRIZIONE_SEMINA AS DESC_SEM_SEC, " +
            "       UP.ID_PRATICA_MANTENIMENTO, " +
            "       TPM.CODICE_PRATICA_MANTENIMENTO, " +
            "       TPM.DESCRIZIONE_PRATICA_MANTENIMEN, " +
            "       UP.ID_FASE_ALLEVAMENTO, " +
            "       TFA.CODICE_FASE_ALLEVAMENTO, " +
            "       TFA.DESCRIZIONE_FASE_ALLEVAMENTO, " +
  	        "       UP.ID_TIPO_EFA, " +
  	        "       TEF.DESCRIZIONE_TIPO_EFA, " +
  	        "       UM.DESCRIZIONE AS DESC_UNITA_MIS_EFA, " +
  	        "       UP.VALORE_ORIGINALE, " +
  	        "       UP.VALORE_DOPO_CONVERSIONE, " +
  	        "       UP.VALORE_DOPO_PONDERAZIONE, " +
  	        "       UP.DATA_INIZIO_DESTINAZIONE, " +
  	        "       UP.DATA_FINE_DESTINAZIONE, " +
  	        "       UP.DATA_INIZIO_DESTINAZIONE_SEC, " +
  	        "       UP.DATA_FINE_DESTINAZIONE_SEC " +
  					"FROM   DB_UTE U, " +
  					"       DB_STORICO_PARTICELLA SP, "	+
  					"       DB_CONDUZIONE_PARTICELLA CP, " +
  					"       COMUNE C, " +
  					"       DB_TIPO_TITOLO_POSSESSO TTP, " +
  					"       DB_UTILIZZO_PARTICELLA UP, " +
  					"       DB_R_CATALOGO_MATRICE RCM, " +
  					"       DB_R_CATALOGO_MATRICE RCM2, " +
  					"       DB_TIPO_UTILIZZO TU, " +
  					"       DB_TIPO_VARIETA TV, " +
  					"       DB_TIPO_UTILIZZO TU2, "	+
  					"       DB_TIPO_VARIETA TV2, " +
  					"       DB_TIPO_DETTAGLIO_USO TDU, " +
  		      "       DB_TIPO_DETTAGLIO_USO TDU2, " +
  		      "       DB_TIPO_DESTINAZIONE TDE, " +
            "       DB_TIPO_DESTINAZIONE TDE2, " +
            "       DB_TIPO_QUALITA_USO TQU, " +
            "       DB_TIPO_QUALITA_USO TQU2, " +
  		      "       DB_TIPO_PERIODO_SEMINA TPS, " +
  		      "       DB_TIPO_PERIODO_SEMINA TPS2, " +
  		      "       DB_TIPO_SEMINA TSE, " +
            "       DB_TIPO_SEMINA TSE2, " +
            "       DB_TIPO_PRATICA_MANTENIMENTO TPM, " +
            "       DB_TIPO_FASE_ALLEVAMENTO TFA, " +
  		      "       DB_TIPO_EFA TEF, " +
  		      "       DB_UNITA_MISURA UM, " +
  					"       PROVINCIA P, " +
  					"       DB_TIPO_CASO_PARTICOLARE TCP, " +
  					"       DB_PARTICELLA PART , " +
  					"       DB_TIPO_MACRO_USO TMU, " +
  					"       DB_TIPO_MACRO_USO_VARIETA TMUV, " +
  					"       DB_FOGLIO F ";
  			
  			if (filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S)
  					|| filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S))
  			{
  				query += "" +
  						"    ,DB_DICHIARAZIONE_CONSISTENZA DICH_CONS, " +
  				    "     DB_CONDUZIONE_DICHIARATA CD ";
  			}
  			// Metto in join DB_DICHIARAZIONE_SEGNALAZIONE solo se l'utente
  			// ha specificato un determinato controllo effettuato sulle particelle
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo()))
  			{
  				query += "   ,DB_DICHIARAZIONE_SEGNALAZIONE DS ";
  			}
  			
  			//Tabelle per la ricerca sui documenti
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()) 
  			    || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
        {
          query += "" +
          		"   ,DB_DOCUMENTO_CONDUZIONE DOCC, " +
          		"    DB_DOCUMENTO DOC, " +
          		"    DB_TIPO_DOCUMENTO TDOC," +
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
  			
  			
  			query += " " +
  					" WHERE U.ID_AZIENDA = ? " +
  					" AND   U.ID_UTE = CP.ID_UTE ";
  			// Estraggo le conduzione attive solo se il piano di riferimento è
  			// "in lavorazione(solo conduzioni attive)"
  			if (filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() == -1)
  			{
  				SolmrLogger.debug(this,"-- filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() ="+filtriParticellareRicercaVO.getIdPianoRiferimento().longValue());
  				query += " AND   U.DATA_FINE_ATTIVITA IS NULL " + 
  				    "   AND CP.DATA_FINE_CONDUZIONE IS NULL ";
  			}
  			query += " " +
  					" AND   PART.ID_PARTICELLA = SP.ID_PARTICELLA " +
  					" AND   CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
  					" AND   SP.DATA_FINE_VALIDITA IS NULL " +
  					" AND   SP.COMUNE = C.ISTAT_COMUNE " +
  					" AND   SP.ID_CASO_PARTICOLARE = TCP.ID_CASO_PARTICOLARE(+) " +
  					" AND   C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) " +
  					" AND   CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO " +
  					" AND   CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA "	+
  					" AND   UP.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE "  +
  					" AND   UP.ID_CATALOGO_MATRICE_SECONDARIO = RCM2.ID_CATALOGO_MATRICE (+) "  +
  					" AND   RCM.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
  					" AND   RCM.ID_VARIETA = TV.ID_VARIETA(+) " +
  					" AND   RCM2.ID_UTILIZZO = TU2.ID_UTILIZZO(+) " +
  					" AND   RCM2.ID_VARIETA = TV2.ID_VARIETA(+) " +
  					" AND   RCM.ID_TIPO_DETTAGLIO_USO = TDU.ID_TIPO_DETTAGLIO_USO(+) " +
  		      " AND   RCM2.ID_TIPO_DETTAGLIO_USO = TDU2.ID_TIPO_DETTAGLIO_USO(+) " +
  		      " AND   RCM.ID_TIPO_DESTINAZIONE = TDE.ID_TIPO_DESTINAZIONE(+) " +
            " AND   RCM2.ID_TIPO_DESTINAZIONE = TDE2.ID_TIPO_DESTINAZIONE(+) " +
            " AND   RCM.ID_TIPO_QUALITA_USO = TQU.ID_TIPO_QUALITA_USO(+) " +
            " AND   RCM2.ID_TIPO_QUALITA_USO = TQU2.ID_TIPO_QUALITA_USO(+) " +
  		      " AND   UP.ID_TIPO_PERIODO_SEMINA = TPS.ID_TIPO_PERIODO_SEMINA(+) " +
  		      " AND   UP.ID_TIPO_PERIODO_SEMINA_SECOND = TPS2.ID_TIPO_PERIODO_SEMINA(+) " +
  		      " AND   UP.ID_SEMINA = TSE.ID_SEMINA(+) " +
            " AND   UP.ID_SEMINA_SECONDARIA = TSE2.ID_SEMINA(+) " +
            " AND   UP.ID_PRATICA_MANTENIMENTO = TPM.ID_PRATICA_MANTENIMENTO(+) " +
            " AND   UP.ID_FASE_ALLEVAMENTO = TFA.ID_FASE_ALLEVAMENTO(+) " +
  		      " AND   UP.ID_TIPO_EFA = TEF.ID_TIPO_EFA(+) " +
  			    " AND   TEF.ID_UNITA_MISURA = UM.ID_UNITA_MISURA(+) ";
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdMacroUso()))
  			{
  				query += 
  				  " AND RCM.ID_CATALOGO_MATRICE = TMUV.ID_CATALOGO_MATRICE " +
  				  " AND TMUV.DATA_FINE_VALIDITA IS NULL " +
  				  " AND TMUV.ID_MACRO_USO = TMU.ID_MACRO_USO " + 
  				  " AND TMU.ID_MACRO_USO = ? ";
  			}  			
  			else
  			{
  				query += 
  				  " AND RCM.ID_CATALOGO_MATRICE = TMUV.ID_CATALOGO_MATRICE (+) " +
  				  " AND TMUV.DATA_FINE_VALIDITA (+) IS NULL " +
  				  " AND TMUV.ID_MACRO_USO = TMU.ID_MACRO_USO (+) ";
  				
  			}
  			
  			// Se l'utente ha indicato l'ute di riferimento
  			if (filtriParticellareRicercaVO.getIdUte() != null)
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
  			  query += " AND CP.ID_CONDUZIONE_PARTICELLA = DOCC.ID_CONDUZIONE_PARTICELLA " +
  			           " AND DOCC.ID_DOCUMENTO = DOC.ID_DOCUMENTO " +
  			           " AND DOC.EXT_ID_DOCUMENTO = TDOC.ID_DOCUMENTO " +
  			           " AND TDOC.ID_DOCUMENTO = DOCCAT.ID_DOCUMENTO " +
  			           " AND DOCCAT.ID_CATEGORIA_DOCUMENTO = ? " +
  			           " AND DOC.ID_STATO_DOCUMENTO IS NULL " +
  			           " AND NVL(DOC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > SYSDATE ";
  			  if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
  	      {
  			    query += " AND TDOC.ID_DOCUMENTO = ? ";
  	      }
  			  
  			  if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
          {
            query += " AND DOC.ID_DOCUMENTO = ? ";
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
                     "               AND    NO.ID_AZIENDA = U.ID_AZIENDA ";
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
                       "                                    AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA )) )";
          }
          else
          {
            query +=   " AND    NE.DATA_FINE_VALIDITA IS NULL )";
          }
          
        }
  			
  			
  			// Se l'utente ha indicato il comune di riferimento
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIstatComune()))
  			{
  				query += " AND SP.COMUNE = ? ";
  			}
  			// Se l'utente ha indicato la sezione di riferimento
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSezione()))
  			{
  				query += " AND SP.SEZIONE = ? ";
  			}
  			// Se l'utente ha indicato il foglio di riferimento
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getFoglio()))
  			{
  				query += " AND SP.FOGLIO = ? ";
  			}
  			// Se l'utente ha indicato la particella di riferimento
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getParticella()))
  			{
  				query += " AND SP.PARTICELLA = ? ";
  			}
  			// Se l'utente ha indicato il subalterno di riferimento
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSubalterno()))
  			{
  				query += " AND SP.SUBALTERNO = ? ";
  			}
  			
  			// Se l'utente ha indicato il tipo Zona Altimetrica
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdZonaAltimetrica()))
  			{
  				query += " AND SP.ID_ZONA_ALTIMETRICA = ? ";
  			}
  			// Se l'utente ha indicato il tipo Caso Particolare
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCasoParticolare()))
  			{
  				query += " AND SP.ID_CASO_PARTICOLARE = ? ";
  			}
  			// Se l'utente invece ha specificato un particolare uso del suolo
  			if (filtriParticellareRicercaVO.getIdUtilizzo().longValue() > 0)
  			{
  				// Se non è stato indicato nei filtri di ricerca se l'utilizzo
  				// selezionato
  				// è primario o secondario, viene usata come condizione di default
  				// quella
  				// dell'utilizzo primario
  				if (!Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario())
  						&& !Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario()))
  				{
  					query += " AND RCM.ID_UTILIZZO = ? ";
  				}
  				else
  				{
  					// Uso Primario
  					if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario())
  							&& Validator.isEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario()))
  					{
  						query += " AND RCM.ID_UTILIZZO = ? ";
  					}
  					// Uso Secondario
  					else if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario())
  							&& Validator.isEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario()))
  					{
  						query += " AND RCM2.ID_UTILIZZO = ? ";
  					}
  					// Se ha selezionato sia l'opzione uso primario che quella uso
  					// secondario
  					else if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario())
  							&& Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario()))
  					{
  						query += " AND (RCM.ID_UTILIZZO = ?  OR RCM2.ID_UTILIZZO = ?) ";
  					}
  				}
  			}
  			
  			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoEfa()))
  			{
  			  query += " AND UP.ID_TIPO_EFA = ? ";  			  
  			}
  			// Se l'utente ha specificato un particolare titolo di possesso
  			if (filtriParticellareRicercaVO.getIdTitoloPossesso() != null)
  			{
  				query += " AND CP.ID_TITOLO_POSSESSO = ? ";
  			}
  			else
  			{
  				// Se l'utente ha scelto l'opzione escludi asservimento
  				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckEscludiAsservimento())
  						&& filtriParticellareRicercaVO.getCheckEscludiAsservimento().equalsIgnoreCase(SolmrConstants.FLAG_S))
  				{
  					query += " AND CP.ID_TITOLO_POSSESSO <> " + SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
  				}
  				// Se l'utente ha scelto l'opzione escludi conferimento
  				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckEscludiConferimento())
  						&& filtriParticellareRicercaVO.getCheckEscludiConferimento().equalsIgnoreCase(SolmrConstants.FLAG_S))
  				{
  					query += " AND CP.ID_TITOLO_POSSESSO <> " + SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO;
  				}
  			}
  			// Se l'utente ha specificato la tipologia di anomalia bloccante
  			boolean isFirst = true;
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneBloccante()))
  			{
  				query += " AND (CP.ESITO_CONTROLLO = ? ";
  				isFirst = false;
  			}
  			// Se l'utente ha specificato la tipologia di anomalia warning
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneWarning()))
  			{
  				if (!isFirst)
  				{
  					query += " OR ";
  				}
  				else
  				{
  					query += " AND (";
  				}
  				query += " CP.ESITO_CONTROLLO = ? ";
  				isFirst = false;
  			}
  			// Se l'utente ha specificato la tipologia di anomalia OK
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneOk()))
  			{
  				if (!isFirst)
  				{
  					query += " OR ";
  				}
  				else
  				{
  					query += " AND (";
  				}
  				query += " CP.ESITO_CONTROLLO = ? ";
  				isFirst = false;
  			}
  			if (!isFirst)
  			{
  				query += ")";
  			}
  			// Se l'utente ha specificato un determinato tipo di controllo
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo()))
  			{
  				query += " " +
  						" AND DS.ID_AZIENDA = ? " +
  						" AND DS.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA "	+
  						" AND DS.ID_DICHIARAZIONE_CONSISTENZA IS NULL " +
  						" AND DS.ID_CONTROLLO = ? ";
  			}
  			// Ricerco i dati delle particelle solo asservite
  			if (filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S))
  			{
  				query += " AND DICH_CONS.ID_AZIENDA <> ? " 
  			      + " AND DICH_CONS.DATA_INSERIMENTO_DICHIARAZIONE = "
  						+ "     (SELECT MAX(DATA_INSERIMENTO_DICHIARAZIONE) " 
  			      + "      FROM   DB_DICHIARAZIONE_CONSISTENZA DC1 "
  						+ "      WHERE  DICH_CONS.ID_AZIENDA = DC1.ID_AZIENDA" 
  			      + "     ) "
  						+ " AND DICH_CONS.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " 
  			      + " AND CD.ID_TITOLO_POSSESSO = ? "
  						+ " AND CD.ID_PARTICELLA = SP.ID_PARTICELLA ";
  			}
  			// Ricerco i dati delle particelle solo conferite
  			if (filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S))
  			{
  				query += " AND DICH_CONS.ID_AZIENDA <> ? "
  						+ " AND DICH_CONS.DATA_INSERIMENTO_DICHIARAZIONE = (SELECT MAX(DATA_INSERIMENTO_DICHIARAZIONE) FROM DB_DICHIARAZIONE_CONSISTENZA DC1 WHERE DICH_CONS.ID_AZIENDA = DC1.ID_AZIENDA) "
  						+ " AND DICH_CONS.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " + " AND CD.ID_TITOLO_POSSESSO = ? "
  						+ " AND CD.ID_PARTICELLA = SP.ID_PARTICELLA ";
  			}
  			query += " GROUP BY SP.ID_STORICO_PARTICELLA, " +
  			    "          CP.ID_CONDUZIONE_PARTICELLA, " +
  			    "          CP.ESITO_CONTROLLO, " +
  					"          C.DESCOM, " +
  					"          P.SIGLA_PROVINCIA, " + 
  					"          SP.SEZIONE, " +
  					"          SP.FOGLIO, " +
  					"          SP.PARTICELLA, " + 
  					"          SP.SUBALTERNO, " +
  					"          SP.COMUNE, " +
  					"          SP.SUP_CATASTALE, " +
  					"          SP.SUPERFICIE_GRAFICA, " +
  					"          SP.ID_ZONA_ALTIMETRICA, " +
  					"          CP.ID_TITOLO_POSSESSO, " +
  					"          TTP.DESCRIZIONE, " +
  					"          SP.ID_AREA_C, " +
  					"          CP.SUPERFICIE_CONDOTTA, " +
  					"          CP.PERCENTUALE_POSSESSO, " +
  					"          CP.DATA_INIZIO_CONDUZIONE, " +
  					"          CP.DATA_FINE_CONDUZIONE, " +
  					"          CP.RECORD_MODIFICATO, " +
  					"          UP.ID_UTILIZZO_PARTICELLA, " +
  					"          UP.ID_CATALOGO_MATRICE, " +
  					"          TU.CODICE, " +
  					"          TU.DESCRIZIONE, " + 
  					"          TV.DESCRIZIONE, " +
  					"          TV.CODICE_VARIETA, " +
  					"          UP.SUPERFICIE_UTILIZZATA, " + 
  					"          UP.ID_CATALOGO_MATRICE_SECONDARIO, " +
  					"          RCM2.ID_UTILIZZO, " +
  					"          TU2.CODICE, " +
  					"          TU2.DESCRIZIONE, " + 
  					"          TV2.DESCRIZIONE, " +
  					"          TV2.CODICE_VARIETA, " +
  					"          UP.SUP_UTILIZZATA_SECONDARIA, " + 
  					"          SP.ID_CASO_PARTICOLARE, " +
  					"          TCP.DESCRIZIONE, " +
  					"          CP.ID_UTE, " +
  					"          UP.ANNO_IMPIANTO, " + 
  					"          UP.NUMERO_PIANTE_CEPPI, " + 
  					"          PART.FLAG_SCHEDARIO, " +
  					"          TMU.CODICE, " +
  					"          TMU.DESCRIZIONE, " + 
  					"          CP.SUPERFICIE_AGRONOMICA, " + 
  					"          RCM.ID_VARIETA,  " +
  					"          SP.ID_PARTICELLA, " +
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
            "          UP.ID_TIPO_PERIODO_SEMINA, " +
            "          TPS.CODICE, " +
            "          TPS.DESCRIZIONE, " +
            "          UP.ID_TIPO_PERIODO_SEMINA_SECOND, " +
            "          TPS2.CODICE, " +
            "          TPS2.DESCRIZIONE, " +
            "          UP.ID_SEMINA, " +
            "          TSE.CODICE_SEMINA, " +
            "          TSE.DESCRIZIONE_SEMINA, " +
            "          UP.ID_SEMINA_SECONDARIA, " +
            "          TSE2.CODICE_SEMINA, " +
            "          TSE2.DESCRIZIONE_SEMINA, " +
            "          UP.ID_PRATICA_MANTENIMENTO, " +
            "          TPM.CODICE_PRATICA_MANTENIMENTO, " +
            "          TPM.DESCRIZIONE_PRATICA_MANTENIMEN, " +
            "          UP.ID_FASE_ALLEVAMENTO, " +
            "          TFA.CODICE_FASE_ALLEVAMENTO, " +
            "          TFA.DESCRIZIONE_FASE_ALLEVAMENTO, " +
            "          UP.ID_TIPO_EFA, " +
            "          TEF.DESCRIZIONE_TIPO_EFA, " +
            "          UM.DESCRIZIONE, " +
            "          UP.VALORE_ORIGINALE, " +
            "          UP.VALORE_DOPO_CONVERSIONE, " +
            "          UP.VALORE_DOPO_PONDERAZIONE, " +
            "          UP.DATA_INIZIO_DESTINAZIONE, " +
            "          UP.DATA_FINE_DESTINAZIONE, " +
            "          UP.DATA_INIZIO_DESTINAZIONE_SEC, " +
            "          UP.DATA_FINE_DESTINAZIONE_SEC ";
				
			}
				
				
			// Metto le query in UNION solo se l'utente sceglie di visualizzare
			// "qualunque uso del suolo" ma senza specificare il macro uso
			if ((filtriParticellareRicercaVO.getIdUtilizzo().intValue() < 0) 
			    && (filtriParticellareRicercaVO.getIdMacroUso() == null)
			    && (filtriParticellareRicercaVO.getIdTipoEfa() == null))
			{
				SolmrLogger.debug(this,"-- Aggiungo UNION"); 
				query += " UNION ";
			}
			// La seconda parte della query viene eseguita solo se l'utente non ha
			// scelto un particolare uso del suolo da visualizzare
			if ((filtriParticellareRicercaVO.getIdUtilizzo().intValue() <= 0) 
			    && (filtriParticellareRicercaVO.getIdMacroUso() == null)
			    && (filtriParticellareRicercaVO.getIdTipoEfa() == null))
			{
				
				query += "" +
						"SELECT  " +
						"       SP.ID_STORICO_PARTICELLA, " +
				    "       CP.ID_CONDUZIONE_PARTICELLA, " +
				    "       CP.ESITO_CONTROLLO, "	+
				    "       C.DESCOM, " +
				    "       P.SIGLA_PROVINCIA, " +
				    "       SP.SEZIONE, " +
				    "       SP.FOGLIO, " +
				    "       SP.PARTICELLA, " +
				    "       SP.SUBALTERNO, " +
				    "       SP.COMUNE, " +
				    "       SP.SUP_CATASTALE, " +
				    "       SP.SUPERFICIE_GRAFICA, " +
				    "       SP.ID_ZONA_ALTIMETRICA, "	+
				    "       CP.ID_TITOLO_POSSESSO, " +
				    "       TTP.DESCRIZIONE AS DESC_POSSESSO, " +
				    "       SP.ID_AREA_C, "	+
				    "       CP.SUPERFICIE_CONDOTTA, " +
				    "       CP.PERCENTUALE_POSSESSO, " +
				    "       CP.DATA_INIZIO_CONDUZIONE, " +
				    "       CP.DATA_FINE_CONDUZIONE, " +
				    "       CP.RECORD_MODIFICATO, " +
				    "       -1 AS ID_UTILIZZO_PARTICELLA, " +
				    "       -1 AS ID_CATALOGO_MATRICE, " +
				    "       NULL AS COD_PRIMARIO, "	+
				    "       NULL AS DESC_PRIMARIO, " +
				    "       NULL AS DESC_VARIETA, " +
				    "       NULL AS COD_VARIETA, "	+
				    "       DECODE(CP.ID_TITOLO_POSSESSO, 5 , CP.SUPERFICIE_CONDOTTA, " + 
            "           TRUNC(DECODE(SP.SUPERFICIE_GRAFICA,0,SP.SUP_CATASTALE,SP.SUPERFICIE_GRAFICA) " +
            "         * CP.PERCENTUALE_POSSESSO / 100,4) - TRUNC(SUM(NVL(UP.SUPERFICIE_UTILIZZATA,0)),4)) " +
				    "       SUPERFICIE_UTILIZZATA, " +
				    "       -1 AS ID_UTILIZZO_SECONDARIO, " +
				    "       -1 AS ID_CATALOGO_MATRICE_SECONDARIO, " +
				    "       NULL AS COD_SECONDARIO, " +
				    "       NULL AS DESC_SECONDARIO, " +
				    "       NULL AS VAR_SECONDARIA, " +
				    "       NULL AS COD_VAR_SECONDARIA, " +
				    "       0 AS SUP_UTILIZZATA_SECONDARIA, "	+
				    "       SP.ID_CASO_PARTICOLARE, " +
				    "       TCP.DESCRIZIONE AS DESC_PARTICOLARE, " +
				    "       CP.ID_UTE, " +
				    "       0 AS ANNO_IMPIANTO, " +
				    "       0 AS NUMERO_PIANTE_CEPPI, " +
				    "       PART.FLAG_SCHEDARIO, " +
				    "       NULL AS CODICE, "	+
				    "       NULL AS DESC_MACRO, " +
				    "       CP.SUPERFICIE_AGRONOMICA, " +
				    "       -1 AS ID_VARIETA, " +
				    "       SP.ID_PARTICELLA, " +
				    "       F.FLAG_STABILIZZAZIONE, " +
				    "       -1 AS ID_TIPO_DEST_PRIM, " +
            "       NULL AS COD_DEST_USO_PRIM, " +
            "       NULL AS DESC_DEST_USO_PRIM, " +
            "       -1 AS ID_TIPO_DEST_SEC, " +
            "       NULL AS COD_DEST_USO_SEC, " +
            "       NULL AS DESC_DEST_USO_SEC, " +
            "       -1 AS ID_TIPO_DETTAGLIO_USO, " +
            "       NULL AS COD_DETT_USO_PRIM, " +
            "       NULL AS DESC_DETT_USO_PRIM, " +
            "       -1 AS ID_TIPO_DETT_USO_SECONDARIO, " +
            "       NULL AS COD_DETT_USO_SEC, " +
            "       NULL AS DESC_DETT_USO_SEC, " +
            "       -1 AS ID_TIPO_QUALITA_USO_PRIM, " +
            "       NULL AS COD_QUALITA_USO_PRIM, " +
            "       NULL AS DESC_QUALITA_USO_PRIM, " +
            "       -1 AS ID_TIPO_QUALITA_USO_SEC, " +
            "       NULL AS COD_QUALITA_USO_SEC, " +
            "       NULL AS DESC_QUALITA_USO_SEC, " +
            "       -1 AS ID_TIPO_PERIODO_SEMINA, " +
            "       NULL AS COD_PER_SEM_PRIM, " +
            "       NULL AS DESC_PER_SEM_PRIM, " +
            "       -1 AS ID_TIPO_PERIODO_SEMINA_SECOND, " +
            "       NULL AS COD_PER_SEM_SEC, " +
            "       NULL AS DESC_PER_SEM_SEC, " +
            "       -1 AS ID_SEMINA, " +
            "       NULL AS COD_SEM_PRIM, " +
            "       NULL AS DESC_SEM_PRIM, " +
            "       -1 AS ID_SEMINA_SECONDARIA, " +
            "       NULL AS COD_SEM_SEC, " +
            "       NULL AS DESC_SEM_SEC, " +
            "       -1 AS ID_PRATICA_MANTENIMENTO, " +
            "       NULL AS CODICE_PRATICA_MANTENIMENTO, " +
            "       NULL AS DESCRIZIONE_PRATICA_MANTENIMEN, " +
            "       -1 AS ID_FASE_ALLEVAMENTO, " +
            "       NULL AS CODICE_FASE_ALLEVAMENTO, " +
            "       NULL AS DESCRIZIONE_FASE_ALLEVAMENTO, " +
            "       -1 AS ID_TIPO_EFA, " +
            "       NULL AS DESCRIZIONE_TIPO_EFA, " +
            "       NULL AS DESC_UNITA_MIS_EFA, " +
            "       NULL AS VALORE_ORIGINALE, " +
            "       NULL AS VALORE_DOPO_CONVERSIONE, " +
            "       NULL AS VALORE_DOPO_PONDERAZIONE, " +
            "       NULL AS DATA_INIZIO_DESTINAZIONE, " +
            "       NULL AS DATA_FINE_DESTINAZIONE, " +
            "       NULL AS DATA_INIZIO_DESTINAZIONE_SEC, " +
            "       NULL AS DATA_FINE_DESTINAZIONE_SEC " +
				    "FROM   DB_UTE U, "	+
				    "       DB_STORICO_PARTICELLA SP, " +
				    "       DB_CONDUZIONE_PARTICELLA CP, " +
				    "       COMUNE C, "	+
				    "       DB_TIPO_TITOLO_POSSESSO TTP, " +
				    "       DB_UTILIZZO_PARTICELLA UP, " +
				    "       PROVINCIA P, " +
				    "       DB_TIPO_CASO_PARTICOLARE TCP, " +
				    "       DB_PARTICELLA PART, " +
				    "       DB_FOGLIO F ";
				
				// Se è attivo almeno uno dei due filtri tra solo asservite e solo
				// conferite deve andare in join con la
				// DB_DICHIARAZIONE_CONSISTENZA
				if (filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S)
						|| filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S))
				{
					query += "" +
							"    ,DB_DICHIARAZIONE_CONSISTENZA DICH_CONS, " +
							"     DB_CONDUZIONE_DICHIARATA CD ";
				}
				// Metto in join DB_DICHIARAZIONE_SEGNALAZIONE solo se l'utente
				// ha specificato un determinato controllo effettuato sulle particelle
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo()))
				{
					query += "      ,DB_DICHIARAZIONE_SEGNALAZIONE DS ";
				}
				
				//Tabelle per la ricerca sui documenti
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()) 
            || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
        {
          query += "" +
          		"   ,DB_DOCUMENTO_CONDUZIONE DOCC, " +
          		"    DB_DOCUMENTO DOC, DB_TIPO_DOCUMENTO TDOC, " +
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
        
				query += " " +
						"WHERE    U.ID_AZIENDA = ? " +
						//" AND     U.DATA_FINE_ATTIVITA IS NULL " +
						" AND     U.ID_UTE = CP.ID_UTE ";
				// Estraggo le conduzione attive solo se il piano di riferimento è
				// "in lavorazione(solo conduzioni attive)"
				if (filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() == -1)
				{
					query += " AND     U.DATA_FINE_ATTIVITA IS NULL " +
					    "   AND CP.DATA_FINE_CONDUZIONE IS NULL ";
				}
				query += " AND    PART.ID_PARTICELLA = SP.ID_PARTICELLA " + " AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA "
						+ " AND    SP.DATA_FINE_VALIDITA IS NULL " + " AND    SP.COMUNE = C.ISTAT_COMUNE "
						+ " AND    SP.ID_CASO_PARTICOLARE = TCP.ID_CASO_PARTICOLARE(+) " + " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) "
						+ " AND    CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO " + " AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA(+) ";
				// Se l'utente ha indicato l'ute di riferimento
				if (filtriParticellareRicercaVO.getIdUte() != null)
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
          query += " AND CP.ID_CONDUZIONE_PARTICELLA = DOCC.ID_CONDUZIONE_PARTICELLA " +
                   " AND DOCC.ID_DOCUMENTO = DOC.ID_DOCUMENTO " +
                   " AND DOC.EXT_ID_DOCUMENTO = TDOC.ID_DOCUMENTO " +
                   " AND TDOC.ID_DOCUMENTO = DOCCAT.ID_DOCUMENTO " +
                   " AND DOCCAT.ID_CATEGORIA_DOCUMENTO = ? " +
                   " AND DOC.ID_STATO_DOCUMENTO IS NULL " +
                   " AND NVL(DOCC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > SYSDATE ";
          if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
          {
            query += " AND TDOC.ID_DOCUMENTO = ? ";
          }
          
          if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
          {
            query += " AND DOC.ID_DOCUMENTO = ? ";
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
                     "               AND    NO.ID_AZIENDA = U.ID_AZIENDA ";
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
                       "                                    AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA )) )";
          }
          else
          {
            query +=   " AND    NE.DATA_FINE_VALIDITA IS NULL )";
          }
          
        }
				
				// Se l'utente ha indicato il comune di riferimento
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIstatComune()))
				{
					query += " AND SP.COMUNE = ? ";
				}
				// Se l'utente ha indicato la sezione di riferimento
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSezione()))
				{
					query += " AND SP.SEZIONE = ? ";
				}
				// Se l'utente ha indicato il foglio di riferimento
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getFoglio()))
				{
					query += " AND SP.FOGLIO = ? ";
				}
				// Se l'utente ha indicato la particella di riferimento
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getParticella()))
				{
					query += " AND SP.PARTICELLA = ? ";
				}
				// Se l'utente ha indicato il subalterno di riferimento
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSubalterno()))
				{
					query += " AND SP.SUBALTERNO = ? ";
				}
				
				// Se l'utente ha indicato il tipo Zona Altimetrica
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdZonaAltimetrica()))
				{
					query += " AND SP.ID_ZONA_ALTIMETRICA = ? ";
				}
				// Se l'utente ha indicato il tipo Caso Particolare
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCasoParticolare()))
				{
					query += " AND SP.ID_CASO_PARTICOLARE = ? ";
				}
				// Se l'utente ha specificato un particolare titolo di possesso
				if (filtriParticellareRicercaVO.getIdTitoloPossesso() != null)
				{
					query += " AND CP.ID_TITOLO_POSSESSO = ? ";
				}
				else
				{
					// Se l'utente ha scelto l'opzione escludi asservimento
					if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckEscludiAsservimento())
							&& filtriParticellareRicercaVO.getCheckEscludiAsservimento().equalsIgnoreCase(SolmrConstants.FLAG_S))
					{
						query += " AND CP.ID_TITOLO_POSSESSO <> " + SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
					}
					// Se l'utente ha scelto l'opzione escludi conferimento
					if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckEscludiConferimento())
							&& filtriParticellareRicercaVO.getCheckEscludiConferimento().equalsIgnoreCase(SolmrConstants.FLAG_S))
					{
						query += " AND CP.ID_TITOLO_POSSESSO <> " + SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO;
					}
				}
				// Se l'utente ha specificato la tipologia di anomalia bloccante
				boolean isFirst = true;
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneBloccante()))
				{
					query += " AND (CP.ESITO_CONTROLLO = ? ";
					isFirst = false;
				}
				// Se l'utente ha specificato la tipologia di anomalia warning
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneWarning()))
				{
					if (!isFirst)
					{
						query += " OR ";
					}
					else
					{
						query += " AND (";
					}
					query += " CP.ESITO_CONTROLLO = ? ";
					isFirst = false;
				}
				// Se l'utente ha specificato la tipologia di anomalia OK
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneOk()))
				{
					if (!isFirst)
					{
						query += " OR ";
					}
					else
					{
						query += " AND (";
					}
					query += " CP.ESITO_CONTROLLO = ? ";
					isFirst = false;
				}
				if (!isFirst)
				{
					query += ")";
				}
				// Se l'utente ha specificato un determinato tipo di controllo
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo()))
				{
					query += " " +
							" AND DS.ID_AZIENDA = ? " +
							" AND DS.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA "	+
							" AND DS.ID_DICHIARAZIONE_CONSISTENZA IS NULL " +
							" AND DS.ID_CONTROLLO = ? ";
				}
				// Ricerco i dati della particella certificata solo se vengono
				// richieste le particelle solo asservite
				if (filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S))
				{
					query += " AND DICH_CONS.ID_AZIENDA <> ? "
							+ " AND DICH_CONS.DATA_INSERIMENTO_DICHIARAZIONE = (SELECT MAX(DATA_INSERIMENTO_DICHIARAZIONE) FROM DB_DICHIARAZIONE_CONSISTENZA DC1 WHERE DICH_CONS.ID_AZIENDA = DC1.ID_AZIENDA) "
							+ " AND DICH_CONS.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " + " AND CD.ID_TITOLO_POSSESSO = ? "
							+ " AND CD.ID_PARTICELLA = SP.ID_PARTICELLA ";
				}
				// Ricerco i dati della particella certificata solo se vengono
				// richieste le particelle solo conferite
				if (filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S))
				{
					query += " AND DICH_CONS.ID_AZIENDA <> ? "
							+ " AND DICH_CONS.DATA_INSERIMENTO_DICHIARAZIONE = (SELECT MAX(DATA_INSERIMENTO_DICHIARAZIONE) FROM DB_DICHIARAZIONE_CONSISTENZA DC1 WHERE DICH_CONS.ID_AZIENDA = DC1.ID_AZIENDA) "
							+ " AND DICH_CONS.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " + " AND CD.ID_TITOLO_POSSESSO = ? "
							+ " AND CD.ID_PARTICELLA = SP.ID_PARTICELLA ";
				}
				query += " " +
						"GROUP BY SP.ID_STORICO_PARTICELLA, " +
						"         CP.ID_CONDUZIONE_PARTICELLA, " +
						"         CP.ESITO_CONTROLLO, "	+
						"         C.DESCOM, " +
						"         P.SIGLA_PROVINCIA, " +
						"         SP.SEZIONE, " +
						"         SP.FOGLIO, " +
						"         SP.PARTICELLA, " +
						"         SP.SUBALTERNO, " +
						"         SP.COMUNE, " +
						"         SP.SUP_CATASTALE, "	+
						"         SP.SUPERFICIE_GRAFICA, " +
						"         SP.ID_ZONA_ALTIMETRICA, " +
						"         CP.ID_TITOLO_POSSESSO, " +
						"         TTP.DESCRIZIONE, " +
						"         SP.ID_AREA_C, " +
						"         CP.SUPERFICIE_CONDOTTA, " +
						"         CP.PERCENTUALE_POSSESSO, " +
						"         CP.DATA_INIZIO_CONDUZIONE, " +
						"         CP.DATA_FINE_CONDUZIONE, " +
						"         CP.RECORD_MODIFICATO, " +
						"         -1, " +
						"         -1, " +
						"         SP.ID_CASO_PARTICOLARE, "	+
						"         TCP.DESCRIZIONE, " +
						"         CP.ID_UTE, " +
						"         PART.FLAG_SCHEDARIO, " +
						"         CP.SUPERFICIE_AGRONOMICA, "	+
						"         -1,  " +
						"         SP.ID_PARTICELLA, " +
						"         F.FLAG_STABILIZZAZIONE, " +
						"         -1, " +
            "         -1, " +
            "         -1, " +
            "         -1, " +
            "         -1, " +
            "         -1, " +
            "         -1, " +
            "         -1, " +
            "         -1, " +
            "         -1, " +
            "         -1, " +
            "         -1, " +
            "         -1, " +
            "         -1, " +
            "         -1 " +
					  "         HAVING  DECODE(CP.ID_TITOLO_POSSESSO, 5 , CP.SUPERFICIE_CONDOTTA, TRUNC(DECODE(SP.SUPERFICIE_GRAFICA,0,SP.SUP_CATASTALE,SP.SUPERFICIE_GRAFICA) " +
            "                      * CP.PERCENTUALE_POSSESSO / 100,4) " +
            "                - TRUNC(SUM(NVL(UP.SUPERFICIE_UTILIZZATA,0)),4)) > 0 ";
						
						/*HAVING TRUNC(SUM(UC.SUPERFICIE_UTILIZZATA),2) <
	          TRUNC(LEAST(DECODE(SP.SUPERFICIE_GRAFICA,0,SP.SUP_CATASTALE,SP.SUPERFICIE_GRAFICA),
	                      DECODE(SP.SUP_CATASTALE,0,SP.SUPERFICIE_GRAFICA,SP.SUP_CATASTALE)) * CP.PERCENTUALE_POSSESSO / 100,2)*/
			}
			if (!Validator.isNotEmpty(filtriParticellareRicercaVO.getOrderBy()))
			{
				query += " ORDER BY DESCOM,SIGLA_PROVINCIA,SEZIONE,FOGLIO,PARTICELLA,SUBALTERNO," +
						"ID_TITOLO_POSSESSO,COD_PRIMARIO,DESC_PRIMARIO,DESC_VARIETA,COD_SECONDARIO,DESC_SECONDARIO," +
						"ID_CASO_PARTICOLARE";
			}
			else
			{
				if (filtriParticellareRicercaVO.getOrderBy().equalsIgnoreCase((String) SolmrConstants.ORDER_BY_DESC_COMUNE_DESC_PIANO_ATTUALE)
						|| filtriParticellareRicercaVO.getOrderBy().equalsIgnoreCase((String) SolmrConstants.ORDER_BY_DESC_COMUNE_ASC_PIANO_ATTUALE))
				{
					query += " ORDER BY " + filtriParticellareRicercaVO.getOrderBy() + " ,SIGLA_PROVINCIA,SEZIONE,FOGLIO,PARTICELLA,SUBALTERNO";
				}
				else
				{
					query += " ORDER BY " + filtriParticellareRicercaVO.getOrderBy() + " ,DESCOM,SIGLA_PROVINCIA,SEZIONE,FOGLIO,PARTICELLA,SUBALTERNO";
				}
			}
			
			// **********************************
			
			stmt = conn.prepareStatement(query);
			
			SolmrLogger.debug(this, "Executing searchParticelleByParameters: " + query + "\n");
			
			// Prima parte della query: viene eseguita solo se l'utente non sceglie
			// di visualizzare solo le particella senza uso del suolo specificato
			int indice = 0;
			boolean isFirst = true;
			
			//Eliminata la possibilità di selezionare "senza uso del suolo"
			if (filtriParticellareRicercaVO.getIdUtilizzo().intValue() != 0)
			{
  			isFirst = false;
  			// ID_AZIENDA
  			SolmrLogger.debug(this, "Value of parameter " + indice + " [ID_AZIENDA] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
  					+ idAzienda + "\n");
  			stmt.setLong(++indice, idAzienda.longValue());
  			
  			listOfParameters += "ID_AZIENDA: " + idAzienda;
  			// ID_MACRO_USO
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdMacroUso()))
  			{
  				SolmrLogger.debug(this, "Value of parameter " + indice
  						+ "[ID_MACRO_USO] in searchParticelleByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdMacroUso()
  						+ "\n");
  				stmt.setLong(++indice, filtriParticellareRicercaVO.getIdMacroUso().longValue());
  				listOfParameters += "ID_MACRO_USO: " + filtriParticellareRicercaVO.getIdMacroUso().longValue();
  			}
  			// ID_UTE
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdUte()))
  			{
  				SolmrLogger.debug(this, "Value of parameter " + indice + "[ID_UTE] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
  						+ filtriParticellareRicercaVO.getIdUte() + "\n");
  				stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUte().longValue());
  				listOfParameters += ", ID_UTE: " + filtriParticellareRicercaVO.getIdUte();
  			}  			
  			// TIPO DOCUMENTO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()))
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_CATEGORIA_DOCUMENTO] in searchParticelleByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdTipoDocumento()
              + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTipoDocumento().longValue());
          listOfParameters += ", ID_CATEGORIA_DOCUMENTO: " + filtriParticellareRicercaVO.getIdTipoDocumento();
        }
        
        // DOCUMENTO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_DOCUMENTO] in searchParticelleByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdDocumento()
              + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdDocumento().longValue());
          listOfParameters += ", ID_DOCUMENTO: " + filtriParticellareRicercaVO.getIdDocumento();
        }
        
        // PROTOCOLLO DOCUMENTO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_PROTOCOLLO_DOCUMENTO] in searchParticelleByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdProtocolloDocumento()
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
        // TIPOLOGIA NOTIFICA
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipologiaNotifica())) 
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_TIPOLOGIA_NOTIFICA] in searchParticelleByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdTipologiaNotifica()
              + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTipologiaNotifica().longValue());
          listOfParameters += ", ID_TIPOLOGIA_NOTIFICA: " + filtriParticellareRicercaVO.getIdTipologiaNotifica();
        }
        // CATEGORIA_NOTIFICA
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCategoriaNotifica())) 
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_CATEGORIA_NOTIFICA] in searchParticelleByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdCategoriaNotifica()
              + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdCategoriaNotifica().longValue());
          listOfParameters += ", ID_CATEGORIA_NOTIFICA: " + filtriParticellareRicercaVO.getIdCategoriaNotifica();
        }
  			
  		
  			// ISTAT_COMUNE
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIstatComune()))
  			{
  				SolmrLogger.debug(this, "Value of parameter " + indice
  						+ "[ISTAT_COMUNE] in searchParticelleByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIstatComune()
  						+ "\n");
  				stmt.setString(++indice, filtriParticellareRicercaVO.getIstatComune());
  				listOfParameters += ", ISTAT_COMUNE: " + filtriParticellareRicercaVO.getIstatComune();
  			}
  			// SEZIONE
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSezione()))
  			{
  				SolmrLogger.debug(this, "Value of parameter " + indice + "[SEZIONE] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
  						+ filtriParticellareRicercaVO.getSezione() + "\n");
  				stmt.setString(++indice, filtriParticellareRicercaVO.getSezione().toUpperCase());
  				listOfParameters += ", SEZIONE: " + filtriParticellareRicercaVO.getSezione();
  			}
  			// FOGLIO
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getFoglio()))
  			{
  				
  				if (Validator.isNumericInteger(filtriParticellareRicercaVO.getFoglio()))
  				{
  					SolmrLogger.debug(this, "Value of parameter " + indice + "[FOGLIO] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
  							+ filtriParticellareRicercaVO.getFoglio() + "\n");
  					stmt.setString(++indice, filtriParticellareRicercaVO.getFoglio());
  					listOfParameters += ", FOGLIO: " + filtriParticellareRicercaVO.getFoglio();
  				}
  				else
  				{
  					SolmrLogger.debug(this, "Value of parameter " + indice + "[FOGLIO] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
  							+ null + "\n");
  					stmt.setString(++indice, null);
  					listOfParameters += ", FOGLIO: " + null;
  				}
  			}
  			// PARTICELLA
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getParticella()))
  			{
  				if (Validator.isNumericInteger(filtriParticellareRicercaVO.getParticella()))
  				{
  					SolmrLogger.debug(this, "Value of parameter " + indice
  							+ "[PARTICELLA] in searchParticelleByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getParticella()
  							+ "\n");
  					stmt.setString(++indice, filtriParticellareRicercaVO.getParticella());
  					listOfParameters += ", PARTICELLA: " + filtriParticellareRicercaVO.getParticella();
  				}
  				else
  				{
  					SolmrLogger.debug(this, "Value of parameter " + indice
  							+ "[PARTICELLA] in searchParticelleByParameters method in ConduzioneParticellaDAO: " + null + "\n");
  					stmt.setString(++indice, null);
  					listOfParameters += ", PARTICELLA: " + null;
  				}
  			}
  			// SUBALTERNO
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSubalterno()))
  			{
  				SolmrLogger.debug(this, "Value of parameter " + indice + "[SUBALTERNO] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
  						+ filtriParticellareRicercaVO.getSubalterno() + "\n");
  				stmt.setString(++indice, filtriParticellareRicercaVO.getSubalterno().toUpperCase());
  				listOfParameters += ", SUBALTERNO: " + filtriParticellareRicercaVO.getSubalterno();
  			}
  			
  			// ID_ZONA_ALTIMETRICA
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdZonaAltimetrica()))
  			{
  				SolmrLogger.debug(this, "Value of parameter " + indice
  						+ "[ID_ZONA_ALTIMETRICA] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
  						+ filtriParticellareRicercaVO.getIdZonaAltimetrica() + "\n");
  				stmt.setLong(++indice, filtriParticellareRicercaVO.getIdZonaAltimetrica().longValue());
  				listOfParameters += ", ID_ZONA_ALTIMETRICA: " + filtriParticellareRicercaVO.getIdZonaAltimetrica();
  			}
  			// ID_CASO_PARTICOLARE
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCasoParticolare()))
  			{
  				SolmrLogger.debug(this, "Value of parameter " + indice
  						+ "[ID_CASO_PARTICOLARE] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
  						+ filtriParticellareRicercaVO.getIdCasoParticolare() + "\n");
  				stmt.setLong(++indice, filtriParticellareRicercaVO.getIdCasoParticolare().longValue());
  				listOfParameters += ", ID_CASO_PARTICOLARE: " + filtriParticellareRicercaVO.getIdCasoParticolare();
  			}
  			// Se non è stato indicato nè l'uso primario nè il secondario
  			if (Validator.isEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario())
  					&& Validator.isEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario()))
  			{
  				// Se è stato indicato un particolare uso del suolo, seleziono di
  				// default l'opzione
  				// uso primario
  				if (filtriParticellareRicercaVO.getIdUtilizzo().intValue() > 0)
  				{
  					SolmrLogger.debug(this, "Value of parameter " + indice
  							+ "[ID_UTILIZZO] in searchParticelleByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdUtilizzo()
  							+ "\n");
  					stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
  					listOfParameters += ", ID_UTILIZZO: " + filtriParticellareRicercaVO.getIdUtilizzo();
  				}
  			}
  			else
  			{
  				if (filtriParticellareRicercaVO.getIdUtilizzo().intValue() > 0)
  				{
  					// Uso primario
  					if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario())
  							&& Validator.isEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario()))
  					{
  						SolmrLogger.debug(this, "Value of parameter " + indice
  								+ "[ID_UTILIZZO] in searchParticelleByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdUtilizzo()
  								+ "\n");
  						stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
  						listOfParameters += ", ID_UTILIZZO: " + filtriParticellareRicercaVO.getIdUtilizzo();
  					}
  					// Uso secondario
  					else if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario())
  							&& Validator.isEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario()))
  					{
  						SolmrLogger.debug(this, "Value of parameter " + indice
  								+ "[ID_UTILIZZO_SECONDARIO] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
  								+ filtriParticellareRicercaVO.getIdUtilizzo() + "\n");
  						stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
  						listOfParameters += ", ID_UTILIZZO_SECONDARIO: " + filtriParticellareRicercaVO.getIdUtilizzo();
  					}
  					// Altrimenti se ha selezionato sia l'opzione uso primario che
  					// quella uso secondario
  					else if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario())
  							&& Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario()))
  					{
  						SolmrLogger.debug(this, "Value of parameter " + indice
  								+ "[ID_UTILIZZO] in searchParticelleByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdUtilizzo()
  								+ "\n");
  						stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
  						listOfParameters += ", ID_UTILIZZO: " + filtriParticellareRicercaVO.getIdUtilizzo();
  						SolmrLogger.debug(this, "Value of parameter " + indice
  								+ "[ID_UTILIZZO_SECONDARIO] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
  								+ filtriParticellareRicercaVO.getIdUtilizzo() + "\n");
  						stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
  						listOfParameters += ", ID_UTILIZZO_SECONDARIO: " + filtriParticellareRicercaVO.getIdUtilizzo();
  					}
  				}
  			}
  			
  			
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoEfa()))
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
            + "[ID_TIPO_EFA] in searchParticelleByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdTipoEfa()
            + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTipoEfa().longValue());
          listOfParameters += ", ID_TIPO_EFA: " + filtriParticellareRicercaVO.getIdTipoEfa();
        }
  			
  			
  			// ID_TIPO_TITOLO_POSSESSO
  			if (filtriParticellareRicercaVO.getIdTitoloPossesso() != null)
  			{
  				SolmrLogger.debug(this, "Value of parameter" + indice
  						+ "[ID_TITOLO_POSSESSO] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
  						+ filtriParticellareRicercaVO.getIdTitoloPossesso() + "\n");
  				stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTitoloPossesso().longValue());
  				listOfParameters += ", ID_TITOLO_POSSESSO: " + filtriParticellareRicercaVO.getIdTitoloPossesso();
  			}
  			// SEGNALAZIONI
  			// Se l'utente ha specificato la tipologia di anomalia bloccante
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneBloccante()))
  			{
  				SolmrLogger.debug(this, "Value of parameter " + indice
  						+ "[TIPO_SEGNALAZIONE_BLOCCANTE] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
  						+ filtriParticellareRicercaVO.getTipoSegnalazioneBloccante() + "\n");
  				stmt.setString(++indice, filtriParticellareRicercaVO.getTipoSegnalazioneBloccante());
  				listOfParameters += ", SEGNALAZIONE: " + filtriParticellareRicercaVO.getTipoSegnalazioneBloccante();
  			}
  			// Se l'utente ha specificato la tipologia di anomalia warning
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneWarning()))
  			{
  				SolmrLogger.debug(this, "Value of parameter " + indice
  						+ "[TIPO_SEGNALAZIONE_WARNING] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
  						+ filtriParticellareRicercaVO.getTipoSegnalazioneWarning() + "\n");
  				stmt.setString(++indice, filtriParticellareRicercaVO.getTipoSegnalazioneWarning());
  				listOfParameters += ", SEGNALAZIONE: " + filtriParticellareRicercaVO.getTipoSegnalazioneWarning();
  			}
  			// Se l'utente ha specificato la tipologia di anomalia OK
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneOk()))
  			{
  				SolmrLogger.debug(this, "Value of parameter " + indice
  						+ "[TIPO_SEGNALAZIONE_OK] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
  						+ filtriParticellareRicercaVO.getTipoSegnalazioneOk() + "\n");
  				stmt.setString(++indice, filtriParticellareRicercaVO.getTipoSegnalazioneOk());
  				listOfParameters += ", SEGNALAZIONE: " + filtriParticellareRicercaVO.getTipoSegnalazioneOk();
  			}
  			// Se l'utente ha specificato un determinato controllo
  			if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo()))
  			{
  				SolmrLogger.debug(this, "Value of parameter " + indice + "[ID_AZIENDA] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
  						+ idAzienda + "\n");
  				stmt.setLong(++indice, idAzienda.longValue());
  				listOfParameters += ", ID_AZIENDA: " + idAzienda;
  				SolmrLogger.debug(this, "Value of parameter " + indice
  						+ "[ID_CONTROLLO] in searchParticelleByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdControllo()
  						+ "\n");
  				stmt.setLong(++indice, filtriParticellareRicercaVO.getIdControllo().longValue());
  				listOfParameters += ", ID_CONTROLLO: " + filtriParticellareRicercaVO.getIdControllo();
  			}
  			// Ricerco i dati della particella certificata solo se vengono
  			// richieste le particelle solo asservite
  			if (filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S))
  			{
  				SolmrLogger.debug(this, "Value of parameter " + indice + "[ID_AZIENDA] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
  						+ idAzienda + "\n");
  				stmt.setLong(++indice, idAzienda.longValue());
  				listOfParameters += ", ID_AZIENDA: " + idAzienda;
  				SolmrLogger.debug(this, "Value of parameter " + indice
  						+ "[ID_TITOLO_POSSESSO] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
  						+ SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO + "\n");
  				stmt.setLong(++indice, SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO.longValue());
  				listOfParameters += ", ID_TITOLO_POSSESSO: " + SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
  			}
  			// Ricerco i dati delle particelle solo conferite
  			if (filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S))
  			{
  				SolmrLogger.debug(this, "Value of parameter " + indice + "[ID_AZIENDA] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
  						+ idAzienda + "\n");
  				stmt.setLong(++indice, idAzienda.longValue());
  				listOfParameters += ", ID_AZIENDA: " + idAzienda;
  				SolmrLogger.debug(this, "Value of parameter " + indice
  						+ "[ID_TITOLO_POSSESSO] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
  						+ SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO + "\n");
  				stmt.setLong(++indice, SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO.longValue());
  				listOfParameters += ", ID_TITOLO_POSSESSO: " + SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO;
  			}
  				
  		}
				
			// Settaggio parametri seconda query
			// La seconda parte della query viene eseguita solo se l'utente non ha
			// scelto un particolare uso del suolo da visualizzare
			if ((filtriParticellareRicercaVO.getIdUtilizzo().intValue() <= 0) 
			    && (filtriParticellareRicercaVO.getIdMacroUso() == null)
			    && (filtriParticellareRicercaVO.getIdTipoEfa() == null))
			{
				// ID_AZIENDA
				SolmrLogger.debug(this, "Value of parameter" + indice + "[ID_AZIENDA] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
						+ idAzienda + "\n");
				
				stmt.setLong(++indice, idAzienda.longValue());
				
				if (isFirst)
				{
					listOfParameters += "ID_AZIENDA: " + idAzienda;
				}
				// ID_UTE
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdUte()))
				{
					SolmrLogger.debug(this, "Value of parameter" + indice + "[ID_UTE] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
							+ filtriParticellareRicercaVO.getIdUte() + "\n");
					stmt.setLong(++indice, filtriParticellareRicercaVO.getIdUte().longValue());
					if (isFirst)
					{
						listOfParameters += ", ID_UTE: " + filtriParticellareRicercaVO.getIdUte();
					}
				}
				
				// TIPO DOCUMENTO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()))
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_CATEGORIA_DOCUMENTO] in searchParticelleByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdTipoDocumento()
              + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTipoDocumento().longValue());
          listOfParameters += ", ID_CATEGORIA_DOCUMENTO: " + filtriParticellareRicercaVO.getIdTipoDocumento();
        }
        
        // DOCUMENTO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_DOCUMENTO] in searchParticelleByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdDocumento()
              + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdDocumento().longValue());
          listOfParameters += ", ID_DOCUMENTO: " + filtriParticellareRicercaVO.getIdDocumento();
        }
        // PROTOCOLLO DOCUMENTO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_PROTOCOLLO_DOCUMENTO] in searchParticelleByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdProtocolloDocumento()
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
        
        // TIPOLOGIA NOTIFICA
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipologiaNotifica())) 
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_TIPOLOGIA_NOTIFICA] in searchParticelleByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdTipologiaNotifica()
              + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTipologiaNotifica().longValue());
          listOfParameters += ", ID_TIPOLOGIA_NOTIFICA: " + filtriParticellareRicercaVO.getIdTipologiaNotifica();
        }
        // CATEGORIA_NOTIFICA
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCategoriaNotifica())) 
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_CATEGORIA_NOTIFICA] in searchParticelleByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdCategoriaNotifica()
              + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdCategoriaNotifica().longValue());
          listOfParameters += ", ID_CATEGORIA_NOTIFICA: " + filtriParticellareRicercaVO.getIdCategoriaNotifica();
        }
        
				// ISTAT_COMUNE
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIstatComune()))
				{
					SolmrLogger.debug(this, "Value of parameter" + indice
							+ "[ISTAT_COMUNE] in searchParticelleByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIstatComune()
							+ "\n");
					stmt.setString(++indice, filtriParticellareRicercaVO.getIstatComune());
					if (isFirst)
					{
						listOfParameters += ", ISTAT_COMUNE: " + filtriParticellareRicercaVO.getIstatComune();
					}
				}
				// SEZIONE
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSezione()))
				{
					SolmrLogger.debug(this, "Value of parameter" + indice + "[SEZIONE] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
							+ filtriParticellareRicercaVO.getSezione() + "\n");
					stmt.setString(++indice, filtriParticellareRicercaVO.getSezione().toUpperCase());
					if (isFirst)
					{
						listOfParameters += ", SEZIONE: " + filtriParticellareRicercaVO.getSezione();
					}
				}
				// FOGLIO
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getFoglio()))
				{
					if (Validator.isNumericInteger(filtriParticellareRicercaVO.getFoglio()))
					{
						SolmrLogger.debug(this, "Value of parameter" + indice + "[FOGLIO] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
								+ filtriParticellareRicercaVO.getFoglio() + "\n");
						stmt.setString(++indice, filtriParticellareRicercaVO.getFoglio());
						if (isFirst)
						{
							listOfParameters += ", FOGLIO: " + filtriParticellareRicercaVO.getFoglio();
						}
					}
					else
					{
						SolmrLogger.debug(this, "Value of parameter" + indice + "[FOGLIO] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
								+ null + "\n");
						stmt.setString(++indice, null);
						if (isFirst)
						{
							listOfParameters += ", FOGLIO: " + null;
						}
					}
				}
				// PARTICELLA
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getParticella()))
				{
					if (Validator.isNumericInteger(filtriParticellareRicercaVO.getParticella()))
					{
						SolmrLogger.debug(this, "Value of parameter" + indice
								+ "[PARTICELLA] in searchParticelleByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getParticella()
								+ "\n");
						stmt.setString(++indice, filtriParticellareRicercaVO.getParticella());
						if (isFirst)
						{
							listOfParameters += ", PARTICELLA: " + filtriParticellareRicercaVO.getParticella();
						}
					}
					else
					{
						SolmrLogger.debug(this, "Value of parameter" + indice
								+ "[PARTICELLA] in searchParticelleByParameters method in ConduzioneParticellaDAO: " + null + "\n");
						stmt.setString(++indice, null);
						if (isFirst)
						{
							listOfParameters += ", PARTICELLA: " + null;
						}
					}
				}
				// SUBALTERNO
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSubalterno()))
				{
					SolmrLogger.debug(this, "Value of parameter" + indice + "[SUBALTERNO] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
							+ filtriParticellareRicercaVO.getSubalterno() + "\n");
					stmt.setString(++indice, filtriParticellareRicercaVO.getSubalterno().toUpperCase());
					if (isFirst)
					{
						listOfParameters += ", SUBALTERNO: " + filtriParticellareRicercaVO.getSubalterno();
					}
				}
				
				// ID_ZONA_ALTIMETRICA
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdZonaAltimetrica()))
				{
					SolmrLogger.debug(this, "Value of parameter " + indice
							+ "[ID_ZONA_ALTIMETRICA] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
							+ filtriParticellareRicercaVO.getIdZonaAltimetrica() + "\n");
					stmt.setLong(++indice, filtriParticellareRicercaVO.getIdZonaAltimetrica().longValue());
					listOfParameters += ", ID_ZONA_ALTIMETRICA: " + filtriParticellareRicercaVO.getIdZonaAltimetrica();
				}
				// ID_CASO_PARTICOLARE
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCasoParticolare()))
				{
					SolmrLogger.debug(this, "Value of parameter " + indice
							+ "[ID_CASO_PARTICOLARE] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
							+ filtriParticellareRicercaVO.getIdCasoParticolare() + "\n");
					stmt.setLong(++indice, filtriParticellareRicercaVO.getIdCasoParticolare().longValue());
					listOfParameters += ", ID_CASO_PARTICOLARE: " + filtriParticellareRicercaVO.getIdCasoParticolare();
				}
				// ID_TIPO_TITOLO_POSSESSO
				if (filtriParticellareRicercaVO.getIdTitoloPossesso() != null)
				{
					SolmrLogger.debug(this, "Value of parameter" + indice
							+ "[ID_TITOLO_POSSESSO] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
							+ filtriParticellareRicercaVO.getIdTitoloPossesso() + "\n");
					stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTitoloPossesso().longValue());
					if (isFirst)
					{
						listOfParameters += ", ID_TITOLO_POSSESSO: " + filtriParticellareRicercaVO.getIdTitoloPossesso();
					}
				}
				// Se l'utente ha specificato la tipologia di anomalia bloccante
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneBloccante()))
				{
					SolmrLogger.debug(this, "Value of parameter " + indice
							+ "[TIPO_SEGNALAZIONE_BLOCCANTE] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
							+ filtriParticellareRicercaVO.getTipoSegnalazioneBloccante() + "\n");
					stmt.setString(++indice, filtriParticellareRicercaVO.getTipoSegnalazioneBloccante());
					listOfParameters += ", SEGNALAZIONE: " + filtriParticellareRicercaVO.getTipoSegnalazioneBloccante();
				}
				// Se l'utente ha specificato la tipologia di anomalia warning
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneWarning()))
				{
					SolmrLogger.debug(this, "Value of parameter " + indice
							+ "[TIPO_SEGNALAZIONE_WARNING] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
							+ filtriParticellareRicercaVO.getTipoSegnalazioneWarning() + "\n");
					stmt.setString(++indice, filtriParticellareRicercaVO.getTipoSegnalazioneWarning());
					listOfParameters += ", SEGNALAZIONE: " + filtriParticellareRicercaVO.getTipoSegnalazioneWarning();
				}
				// Se l'utente ha specificato la tipologia di anomalia OK
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneOk()))
				{
					SolmrLogger.debug(this, "Value of parameter " + indice
							+ "[TIPO_SEGNALAZIONE_OK] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
							+ filtriParticellareRicercaVO.getTipoSegnalazioneOk() + "\n");
					stmt.setString(++indice, filtriParticellareRicercaVO.getTipoSegnalazioneOk());
					listOfParameters += ", SEGNALAZIONE: " + filtriParticellareRicercaVO.getTipoSegnalazioneOk();
				}
				// Se l'utente ha specificato un determinato controllo
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo()))
				{
					SolmrLogger.debug(this, "Value of parameter " + indice + "[ID_AZIENDA] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
							+ idAzienda + "\n");
					stmt.setLong(++indice, idAzienda.longValue());
					listOfParameters += ", ID_AZIENDA: " + idAzienda;
					SolmrLogger.debug(this, "Value of parameter " + indice
							+ "[ID_CONTROLLO] in searchParticelleByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdControllo()
							+ "\n");
					stmt.setLong(++indice, filtriParticellareRicercaVO.getIdControllo().longValue());
					listOfParameters += ", ID_CONTROLLO: " + filtriParticellareRicercaVO.getIdControllo();
				}
				// Ricerco i dati della particella certificata solo se vengono
				// richieste le particelle solo asservite
				if (filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S))
				{
					SolmrLogger.debug(this, "Value of parameter " + indice + "[ID_AZIENDA] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
							+ idAzienda + "\n");
					stmt.setLong(++indice, idAzienda.longValue());
					listOfParameters += ", ID_AZIENDA: " + idAzienda;
					SolmrLogger.debug(this, "Value of parameter " + indice
							+ "[ID_TITOLO_POSSESSO] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
							+ SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO + "\n");
					stmt.setLong(++indice, SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO.longValue());
					listOfParameters += ", ID_TITOLO_POSSESSO: " + SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
				}
				// Ricerco i dati delle particelle solo conferite
				if (filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S))
				{
					SolmrLogger.debug(this, "Value of parameter " + indice + "[ID_AZIENDA] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
							+ idAzienda + "\n");
					stmt.setLong(++indice, idAzienda.longValue());
					listOfParameters += ", ID_AZIENDA: " + idAzienda;
					SolmrLogger.debug(this, "Value of parameter " + indice
							+ "[ID_TITOLO_POSSESSO] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
							+ SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO + "\n");
					stmt.setLong(++indice, SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO.longValue());
					listOfParameters += ", ID_TITOLO_POSSESSO: " + SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO;
				}
			}
			
			ResultSet rs = stmt.executeQuery();
			
			// Primo monitoraggio
			SolmrLogger.debug(this, "ConduzioneParticellaDAO::searchParticelleByParameters - " +
					"In searchParticelleByParameters method from the creation of query to the execution, List of parameters: " + listOfParameters);
			
			while (rs.next())
			{
				StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
				storicoParticellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
				storicoParticellaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
				ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
				conduzioneParticellaVO.setIdConduzioneParticella(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));
				conduzioneParticellaVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
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
				if (Validator.isNotEmpty(rs.getString("ID_ZONA_ALTIMETRICA")))
				{
					storicoParticellaVO.setIdZonaAltimetrica(new Long(rs.getLong("ID_ZONA_ALTIMETRICA")));
				}
				if (Validator.isNotEmpty(rs.getString("ID_AREA_C")))
				{
					storicoParticellaVO.setIdAreaC(new Long(rs.getLong("ID_AREA_C")));
				}
				conduzioneParticellaVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
				CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_TITOLO_POSSESSO")), rs.getString("DESC_POSSESSO"));
				conduzioneParticellaVO.setTitoloPossesso(code);
				conduzioneParticellaVO.setSuperficieCondotta(rs.getString("SUPERFICIE_CONDOTTA"));
				conduzioneParticellaVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
				conduzioneParticellaVO.setDataInizioConduzione(rs.getDate("DATA_INIZIO_CONDUZIONE"));
				conduzioneParticellaVO.setDataFineConduzione(rs.getDate("DATA_FINE_CONDUZIONE"));
				conduzioneParticellaVO.setRecordModificato(rs.getString("RECORD_MODIFICATO"));
				if (Validator.isNotEmpty(rs.getString("ID_UTILIZZO_PARTICELLA")))
				{
					UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
					utilizzoParticellaVO.setIdUtilizzoParticella(new Long(rs.getLong("ID_UTILIZZO_PARTICELLA")));
					utilizzoParticellaVO.setIdCatalogoMatrice(checkLongNull(rs.getString("ID_CATALOGO_MATRICE")));
	        utilizzoParticellaVO.setIdCatalogoMatriceSecondario(checkLongNull(rs.getString("ID_CATALOGO_MATRICE_SECONDARIO")));
					if (utilizzoParticellaVO.getIdUtilizzoParticella().intValue() > 0)
					{
						TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
						tipoUtilizzoVO.setCodice(rs.getString("COD_PRIMARIO"));
						tipoUtilizzoVO.setDescrizione(rs.getString("DESC_PRIMARIO"));
						if (Validator.isNotEmpty(rs.getString("DESC_VARIETA")))
						{
							TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
							tipoVarietaVO.setDescrizione(rs.getString("DESC_VARIETA"));
							tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VARIETA"));
							utilizzoParticellaVO.setTipoVarietaVO(tipoVarietaVO);
						}
						utilizzoParticellaVO.setTipoUtilizzoVO(tipoUtilizzoVO);
						utilizzoParticellaVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
						utilizzoParticellaVO.setNumeroPianteCeppi(rs.getString("NUMERO_PIANTE_CEPPI"));
					}
					utilizzoParticellaVO.setSuperficieUtilizzata(rs.getString("SUPERFICIE_UTILIZZATA"));
					if (Validator.isNotEmpty(rs.getString("CODICE")))
					{
						TipoMacroUsoVO tipoMacroUsoVO = new TipoMacroUsoVO();
						tipoMacroUsoVO.setCodice(rs.getString("CODICE"));
						tipoMacroUsoVO.setDescrizione(rs.getString("DESC_MACRO"));
						utilizzoParticellaVO.setTipoMacroUsoVO(tipoMacroUsoVO);
					}
					if (Validator.isNotEmpty(rs.getString("COD_SECONDARIO")))
					{
						TipoUtilizzoVO tipoUtilizzoSecondarioVO = new TipoUtilizzoVO();
						tipoUtilizzoSecondarioVO.setCodice(rs.getString("COD_SECONDARIO"));
						tipoUtilizzoSecondarioVO.setDescrizione(rs.getString("DESC_SECONDARIO"));
						utilizzoParticellaVO.setTipoUtilizzoSecondarioVO(tipoUtilizzoSecondarioVO);
					}
					if (Validator.isNotEmpty(rs.getString("VAR_SECONDARIA")))
					{
						TipoVarietaVO tipoVarietaSecondariaVO = new TipoVarietaVO();
						tipoVarietaSecondariaVO.setDescrizione(rs.getString("VAR_SECONDARIA"));
						tipoVarietaSecondariaVO.setCodiceVarieta(rs.getString("COD_VAR_SECONDARIA"));
						utilizzoParticellaVO.setTipoVarietaSecondariaVO(tipoVarietaSecondariaVO);
					}
					utilizzoParticellaVO.setSupUtilizzataSecondaria(rs.getString("SUP_UTILIZZATA_SECONDARIA"));
					
					if(Validator.isNotEmpty(rs.getString("ID_TIPO_DEST_PRIM"))) {
	          utilizzoParticellaVO.setIdTipoDestinazione(new Long(rs.getLong("ID_TIPO_DEST_PRIM")));
	          TipoDestinazioneVO tipoDestinazioneVO = new TipoDestinazioneVO();
	          tipoDestinazioneVO.setIdTipoDestinazione(new Long(rs.getLong("ID_TIPO_DEST_PRIM")));
	          tipoDestinazioneVO.setDescrizioneDestinazione(rs.getString("DESC_DEST_USO_PRIM"));
	          tipoDestinazioneVO.setCodiceDestinazione(rs.getString("COD_DEST_USO_PRIM"));
	          utilizzoParticellaVO.setTipoDestinazione(tipoDestinazioneVO);
	        }
	        if(Validator.isNotEmpty(rs.getString("ID_TIPO_DEST_SEC"))) {
	          utilizzoParticellaVO.setIdTipoDestinazioneSecondario(new Long(rs.getLong("ID_TIPO_DEST_SEC")));
	          TipoDestinazioneVO tipoDestinazioneVO = new TipoDestinazioneVO();
	          tipoDestinazioneVO.setIdTipoDestinazione(new Long(rs.getLong("ID_TIPO_DEST_SEC")));
	          tipoDestinazioneVO.setDescrizioneDestinazione(rs.getString("DESC_DEST_USO_SEC"));
	          tipoDestinazioneVO.setCodiceDestinazione(rs.getString("COD_DEST_USO_SEC"));
	          utilizzoParticellaVO.setTipoDestinazioneSecondario(tipoDestinazioneVO);
	        }
					
					
					if(Validator.isNotEmpty(rs.getString("ID_TIPO_DETTAGLIO_USO"))) {
	          utilizzoParticellaVO.setIdTipoDettaglioUso(new Long(rs.getLong("ID_TIPO_DETTAGLIO_USO")));
	          TipoDettaglioUsoVO tipoDettaglioUsoVO = new TipoDettaglioUsoVO();
	          tipoDettaglioUsoVO.setIdTipoDettaglioUso(new Long(rs.getLong("ID_TIPO_DETTAGLIO_USO")));
	          tipoDettaglioUsoVO.setDescrizione(rs.getString("DESC_DETT_USO_PRIM"));
	          tipoDettaglioUsoVO.setCodiceDettaglioUso(rs.getString("COD_DETT_USO_PRIM"));
	          utilizzoParticellaVO.setTipoDettaglioUso(tipoDettaglioUsoVO);
	        }
	        if(Validator.isNotEmpty(rs.getString("ID_TIPO_DETT_USO_SECONDARIO"))) {
	          utilizzoParticellaVO.setIdTipoDettaglioUsoSecondario(new Long(rs.getLong("ID_TIPO_DETT_USO_SECONDARIO")));
	          TipoDettaglioUsoVO tipoDettaglioUsoVO = new TipoDettaglioUsoVO();
	          tipoDettaglioUsoVO.setIdTipoDettaglioUso(new Long(rs.getLong("ID_TIPO_DETT_USO_SECONDARIO")));
	          tipoDettaglioUsoVO.setDescrizione(rs.getString("DESC_DETT_USO_SEC"));
	          tipoDettaglioUsoVO.setCodiceDettaglioUso(rs.getString("COD_DETT_USO_SEC"));
	          utilizzoParticellaVO.setTipoDettaglioUsoSecondario(tipoDettaglioUsoVO);
	        }	 
	        
	        
	        if(Validator.isNotEmpty(rs.getString("ID_TIPO_QUALITA_USO_PRIM"))) {
	          utilizzoParticellaVO.setIdTipoQualitaUso(new Long(rs.getLong("ID_TIPO_QUALITA_USO_PRIM")));
	          TipoQualitaUsoVO tipoQualitaUsoVO = new TipoQualitaUsoVO();
	          tipoQualitaUsoVO.setIdTipoQualitaUso(new Long(rs.getLong("ID_TIPO_QUALITA_USO_PRIM")));
	          tipoQualitaUsoVO.setDescrizioneQualitaUso(rs.getString("DESC_QUALITA_USO_PRIM"));
	          tipoQualitaUsoVO.setCodiceQualitaUso(rs.getString("COD_QUALITA_USO_PRIM"));
	          utilizzoParticellaVO.setTipoQualitaUso(tipoQualitaUsoVO);
	        }
	        if(Validator.isNotEmpty(rs.getString("ID_TIPO_QUALITA_USO_SEC"))) {
	          utilizzoParticellaVO.setIdTipoQualitaUsoSecondario(new Long(rs.getLong("ID_TIPO_QUALITA_USO_SEC")));
	          TipoQualitaUsoVO tipoQualitaUsoVO = new TipoQualitaUsoVO();
	          tipoQualitaUsoVO.setIdTipoQualitaUso(new Long(rs.getLong("ID_TIPO_QUALITA_USO_SEC")));
	          tipoQualitaUsoVO.setDescrizioneQualitaUso(rs.getString("DESC_QUALITA_USO_SEC"));
	          tipoQualitaUsoVO.setCodiceQualitaUso(rs.getString("COD_QUALITA_USO_SEC"));
	          utilizzoParticellaVO.setTipoQualitaUsoSecondario(tipoQualitaUsoVO);
	        }
	        
	        
	        if(Validator.isNotEmpty(rs.getString("ID_TIPO_PERIODO_SEMINA"))) {
            utilizzoParticellaVO.setIdTipoPeriodoSemina(new Long(rs.getLong("ID_TIPO_PERIODO_SEMINA")));
            TipoPeriodoSeminaVO tipoPeriodoSeminaVO = new TipoPeriodoSeminaVO();
            tipoPeriodoSeminaVO.setIdTipoPeriodoSemina(new Long(rs.getLong("ID_TIPO_PERIODO_SEMINA")));
            tipoPeriodoSeminaVO.setDescrizione(rs.getString("DESC_PER_SEM_PRIM"));
            tipoPeriodoSeminaVO.setCodice(rs.getString("COD_PER_SEM_PRIM"));
            utilizzoParticellaVO.setTipoPeriodoSemina(tipoPeriodoSeminaVO);
          }
	        if(Validator.isNotEmpty(rs.getString("ID_TIPO_PERIODO_SEMINA_SECOND"))) {
            utilizzoParticellaVO.setIdTipoPeriodoSeminaSecondario(new Long(rs.getLong("ID_TIPO_PERIODO_SEMINA_SECOND")));
            TipoPeriodoSeminaVO tipoPeriodoSeminaVO = new TipoPeriodoSeminaVO();
            tipoPeriodoSeminaVO.setIdTipoPeriodoSemina(new Long(rs.getLong("ID_TIPO_PERIODO_SEMINA_SECOND")));
            tipoPeriodoSeminaVO.setDescrizione(rs.getString("DESC_PER_SEM_SEC"));
            tipoPeriodoSeminaVO.setCodice(rs.getString("COD_PER_SEM_SEC"));
            utilizzoParticellaVO.setTipoPeriodoSeminaSecondario(tipoPeriodoSeminaVO);
          }
	        utilizzoParticellaVO.setIdTipoEfa(checkLongNull(rs.getString("ID_TIPO_EFA")));
	        utilizzoParticellaVO.setDescTipoEfaEfa(rs.getString("DESCRIZIONE_TIPO_EFA"));
	        utilizzoParticellaVO.setDescUnitaMisuraEfa(rs.getString("DESC_UNITA_MIS_EFA"));
	        utilizzoParticellaVO.setValoreOriginale(rs.getBigDecimal("VALORE_ORIGINALE"));
	        utilizzoParticellaVO.setValoreDopoConversione(rs.getBigDecimal("VALORE_DOPO_CONVERSIONE"));
	        utilizzoParticellaVO.setValoreDopoPonderazione(rs.getBigDecimal("VALORE_DOPO_PONDERAZIONE"));
	        
          
	        utilizzoParticellaVO.setDataInizioDestinazione(rs.getTimestamp("DATA_INIZIO_DESTINAZIONE"));
	        utilizzoParticellaVO.setDataFineDestinazione(rs.getTimestamp("DATA_FINE_DESTINAZIONE"));
	        if(Validator.isNotEmpty(rs.getString("ID_FASE_ALLEVAMENTO"))) {
	          utilizzoParticellaVO.setIdFaseAllevamento(new Long(rs.getLong("ID_FASE_ALLEVAMENTO")));
	          TipoFaseAllevamentoVO tipoFaseAllevamentoVO = new TipoFaseAllevamentoVO();
	          tipoFaseAllevamentoVO.setIdFaseAllevamento(new Long(rs.getLong("ID_FASE_ALLEVAMENTO")));
	          tipoFaseAllevamentoVO.setDescrizioneFaseAllevamento(rs.getString("DESCRIZIONE_FASE_ALLEVAMENTO"));
	          tipoFaseAllevamentoVO.setCodiceFaseAllevamento(rs.getString("CODICE_FASE_ALLEVAMENTO"));
	          utilizzoParticellaVO.setTipoFaseAllevamento(tipoFaseAllevamentoVO);
	        }
	        
	        if(Validator.isNotEmpty(rs.getString("ID_PRATICA_MANTENIMENTO"))) {
	          utilizzoParticellaVO.setIdPraticaMantenimento(new Long(rs.getLong("ID_PRATICA_MANTENIMENTO")));
	          TipoPraticaMantenimentoVO tipoPraticaMantenimentoVO = new TipoPraticaMantenimentoVO();
	          tipoPraticaMantenimentoVO.setIdPraticaMantenimento(new Long(rs.getLong("ID_PRATICA_MANTENIMENTO")));
	          tipoPraticaMantenimentoVO.setDescrizionePraticaMantenim(rs.getString("DESCRIZIONE_PRATICA_MANTENIMEN"));
	          tipoPraticaMantenimentoVO.setCodicePraticaMantenimento(rs.getString("CODICE_PRATICA_MANTENIMENTO"));
	          utilizzoParticellaVO.setTipoPraticaMantenimento(tipoPraticaMantenimentoVO);
	        }
	        
	        if(Validator.isNotEmpty(rs.getString("ID_SEMINA"))) {
	          utilizzoParticellaVO.setIdSemina(new Long(rs.getLong("ID_SEMINA")));
	          TipoSeminaVO tipoSeminaVO = new TipoSeminaVO();
	          tipoSeminaVO.setIdTipoSemina(new Long(rs.getLong("ID_SEMINA")));
	          tipoSeminaVO.setDescrizioneSemina(rs.getString("DESC_SEM_PRIM"));
	          tipoSeminaVO.setCodiceSemina(rs.getString("COD_SEM_PRIM"));
	          utilizzoParticellaVO.setTipoSemina(tipoSeminaVO);
	        }
	        
	        if(Validator.isNotEmpty(rs.getString("ID_SEMINA_SECONDARIA"))) {
	          utilizzoParticellaVO.setIdSeminaSecondario(new Long(rs.getLong("ID_SEMINA_SECONDARIA")));
	          TipoSeminaVO tipoSeminaVO = new TipoSeminaVO();
	          tipoSeminaVO.setIdTipoSemina(new Long(rs.getLong("ID_SEMINA_SECONDARIA")));
	          tipoSeminaVO.setDescrizioneSemina(rs.getString("DESC_SEM_SEC"));
	          tipoSeminaVO.setCodiceSemina(rs.getString("COD_SEM_SEC"));
	          utilizzoParticellaVO.setTipoSeminaSecondario(tipoSeminaVO);
	        }
	        
	        utilizzoParticellaVO.setDataInizioDestinazioneSec(rs.getTimestamp("DATA_INIZIO_DESTINAZIONE_SEC"));
	        utilizzoParticellaVO.setDataFineDestinazioneSec(rs.getTimestamp("DATA_FINE_DESTINAZIONE_SEC"));
					
					
					UtilizzoParticellaVO[] elencoUtilizzi = new UtilizzoParticellaVO[1];
					elencoUtilizzi[0] = utilizzoParticellaVO;
					conduzioneParticellaVO.setElencoUtilizzi(elencoUtilizzi);
				}
				conduzioneParticellaVO.setIdUte(new Long(rs.getLong("ID_UTE")));
				conduzioneParticellaVO.setSuperficieAgronomica(rs.getString("SUPERFICIE_AGRONOMICA"));
				ConduzioneParticellaVO[] elencoConduzioniPart = new ConduzioneParticellaVO[1];
				elencoConduzioniPart[0] = conduzioneParticellaVO;
				storicoParticellaVO.setElencoConduzioni(elencoConduzioniPart);
				if (Validator.isNotEmpty(rs.getString("ID_CASO_PARTICOLARE")))
				{
					storicoParticellaVO.setIdCasoParticolare(new Long(rs.getLong("ID_CASO_PARTICOLARE")));
					CodeDescription codeParticolare = new CodeDescription(Integer.decode(rs.getString("ID_CASO_PARTICOLARE")), rs.getString("DESC_PARTICOLARE"));
					storicoParticellaVO.setCasoParticolare(codeParticolare);
				}
				ParticellaVO particellaVO = new ParticellaVO();
				particellaVO.setFlagSchedario(rs.getString("FLAG_SCHEDARIO"));
				storicoParticellaVO.setParticellaVO(particellaVO);
				
				FoglioVO foglioVO = new FoglioVO();
				foglioVO.setFlagStabilizzazione(checkIntegerNull(rs.getString("FLAG_STABILIZZAZIONE")));
				storicoParticellaVO.setFoglioVO(foglioVO);
				
				elencoParticelle.add(storicoParticellaVO);
			}
			rs.close();
			
			stmt.close();
			
			if (elencoParticelle != null && elencoParticelle.size() > 0)
			{
				PaginazioneUtils page = PaginazioneUtils.newInstance(elencoParticelle.size(), Integer
						.parseInt(SolmrConstants.NUMBER_RECORDS_FOR_PAGE_TERRENI), filtriParticellareRicercaVO.getPaginaCorrente());
				if (page != null)
				{
					Vector<Long> listIdStoricoParticella = getIdStoricoParticella(elencoParticelle, page);
					Vector<Long> listIdConduzioneParticella = getIdConduzioneParticella(elencoParticelle, page);
					Vector<Long> listIdParticella = getIdParticella(elencoParticelle, page);
					Hashtable<String,StoricoParticellaVO> caso=new Hashtable<String,StoricoParticellaVO>();
					Vector<Long> vIdParticellaCertificata = ricercaSecondaQuery(caso, idAzienda, listIdStoricoParticella, listIdConduzioneParticella,
					    listIdParticella, p26Valore, conn);					
					aggiungiRisultatiSecondaQuery(elencoParticelle,page,caso);
					//Aggiungo dati biologico
					if(Validator.isNotEmpty(listIdParticella))
					{
					  if(listIdParticella.size() > 0)
					  {
					    Vector<Long> listIdParticellaBio = getIdParticellaBiologico(idAzienda, listIdParticella, conn);
					    aggiungiRisultatiBiologico(elencoParticelle, listIdParticellaBio, page);
					  }
					}
					//Aggiunti dati eleggibilita fittizia
					if(Validator.isNotEmpty(vIdParticellaCertificata))
					{
  					HashMap<Long,Vector<SuperficieDescription>> hSupElegFit = getDatiEleggibilitaFittizia(
  					    vIdParticellaCertificata, conn);
  					aggiungiRisultatiEleggibilitaFittizia(elencoParticelle, hSupElegFit, page);
					}
					
				}
			}
			
			// Secondo monitoraggio
			SolmrLogger.debug(this, "ConduzioneParticellaDAO::searchParticelleByParameters - " + 
			  "In searchParticelleByParameters method from the creation of query to final setting of parameters inside of Vector after resultset's while cicle, " +
							"List of parameters: " + listOfParameters);
			
			//stmt.close();
			
			// STOP
			//watcher.stop();
			
		}
		catch (SQLException exc)
		{
			SolmrLogger.error(this, "searchParticelleByParameters in ConduzioneParticellaDAO - SQLException: " + exc.getMessage() + "\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex)
		{
			SolmrLogger.error(this, "searchParticelleByParameters in ConduzioneParticellaDAO - Generic Exception: " + ex + "\n");
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
				SolmrLogger.error(this, "searchParticelleByParameters in ConduzioneParticellaDAO - SQLException while closing Statement and Connection: "
						+ exc.getMessage() + "\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex)
			{
				SolmrLogger.error(this,
						"searchParticelleByParameters in ConduzioneParticellaDAO - Generic Exception while closing Statement and Connection: " + ex.getMessage()
								+ "\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated searchParticelleByParameters method in ConduzioneParticellaDAO\n");
		return elencoParticelle;
	}
	
	private void aggiungiRisultatiSecondaQuery(Vector<StoricoParticellaVO> elencoParticelle, PaginazioneUtils page,Hashtable<String,StoricoParticellaVO> caso)
	{
		//Ciclo sulle particelle per andare ad aggiungere i risultati ottenuti dalla seconda query
		for (int i = page.getPrimoElementoPaginaCorrente(); i < page.getUltimoElementoPaginaCorrente(); i++)
		{
			StoricoParticellaVO storicoParticellaVO = ((StoricoParticellaVO) elencoParticelle.get(i));
			if (storicoParticellaVO!=null)
			{
				ConduzioneParticellaVO conduzioneParticellaVO = storicoParticellaVO.getElencoConduzioni()[0];
				String idUtilizzoParticella=null;
				
				if (conduzioneParticellaVO.getElencoUtilizzi()!=null &&
						conduzioneParticellaVO.getElencoUtilizzi().length>0 &&
						conduzioneParticellaVO.getElencoUtilizzi()[0]!=null &&
						conduzioneParticellaVO.getElencoUtilizzi()[0].getIdUtilizzoParticella() !=null)
					idUtilizzoParticella=conduzioneParticellaVO.getElencoUtilizzi()[0].getIdUtilizzoParticella().toString();
				
				/*In dettaglio per ogni elemento presente nella paginazione accedere alla hashtable:
				o	Se ID_UTILIZZO_PARTICELLA=-1 (Utilizzi fittizzi)
				Accedere per ID_CONDUZIONE_PARTICELLA, considerando solamente la prima occorrenza
				aggiornare il piano colturale a video.
				Attenzione in questo caso è necessario forzare sul piano colturale a video il campo SUPERFICIE_ELEG=0
	
	
				o	Se ID_UTILIZZO_PARTICELLA<>-1 (attenzione potrebbe essere null)
				Accedere per ID_CONDUZIONE_PARTICELLA & ‘-‘
				& ID_UTILIZZO_PARTICELLA,  consideranado solamente la prima occorrenza
				aggiornare il piano colturale a video.*/
				
				String idConduzioneParticella=""+conduzioneParticellaVO.getIdConduzioneParticella();
				StoricoParticellaVO storicoPartDatiAgg = (StoricoParticellaVO) caso.get(idConduzioneParticella+"-"+idUtilizzoParticella);
				
				if (storicoPartDatiAgg!=null)
				{
					if(storicoPartDatiAgg.getStoricoUnitaArboreaVO()!=null) 
						storicoParticellaVO.setStoricoUnitaArboreaVO(storicoPartDatiAgg.getStoricoUnitaArboreaVO());
					
					if(storicoPartDatiAgg.getDocumentoVO()!=null) 
						storicoParticellaVO.setDocumentoVO(storicoPartDatiAgg.getDocumentoVO());
					
					//*****************Controlli P
					storicoParticellaVO.setGisP30(storicoPartDatiAgg.getGisP30());
					storicoParticellaVO.setGisP25(storicoPartDatiAgg.getGisP25());
					storicoParticellaVO.setGisP26(storicoPartDatiAgg.getGisP26());
					storicoParticellaVO.setSospesaGis(storicoPartDatiAgg.getSospesaGis());
					//**************************
					
					if(storicoPartDatiAgg.getParticellaCertificataVO()!=null) 
						storicoParticellaVO.setParticellaCertificataVO(storicoPartDatiAgg.getParticellaCertificataVO());
					
					//storicoParticellaVO.setDataRichiestaRiesame(storicoPartDatiAgg.getDataRichiestaRiesame());
					storicoParticellaVO.setIstanzaRiesame(storicoPartDatiAgg.getIstanzaRiesame());
					storicoParticellaVO.setInNotifica(storicoPartDatiAgg.getInNotifica());
				}
			}
		}
	}
	
	
	private void aggiungiRisultatiBiologico(Vector<StoricoParticellaVO> elencoParticelle, 
	    Vector<Long> listIdParticellaBio, PaginazioneUtils page)
  {
    //Ciclo sulle particelle per andare ad aggiungere i risultati ottenuti dalla query del biologico
    for (int i = page.getPrimoElementoPaginaCorrente(); i < page.getUltimoElementoPaginaCorrente(); i++)
    {
      StoricoParticellaVO storicoParticellaVO = ((StoricoParticellaVO) elencoParticelle.get(i));
      if((storicoParticellaVO !=null) && Validator.isNotEmpty(listIdParticellaBio))
      {
        if(listIdParticellaBio.contains(storicoParticellaVO.getIdParticella()))
        {
          storicoParticellaVO.setBiologico("BIOLOGICO");
        }        
      }
    }
  }
	
	private void aggiungiRisultatiEleggibilitaFittizia(Vector<StoricoParticellaVO> elencoParticelle, 
	    HashMap<Long,Vector<SuperficieDescription>> hSupElegFit, PaginazioneUtils page)
  {
    //Ciclo sulle particelle per andare ad aggiungere i risultati ottenuti dalla query del biologico
    for (int i = page.getPrimoElementoPaginaCorrente(); i < page.getUltimoElementoPaginaCorrente(); i++)
    {
      StoricoParticellaVO storicoParticellaVO = ((StoricoParticellaVO) elencoParticelle.get(i));
      if((storicoParticellaVO !=null) && Validator.isNotEmpty(hSupElegFit)
          && Validator.isNotEmpty(storicoParticellaVO.getParticellaCertificataVO()))
      {
        if(hSupElegFit.get(storicoParticellaVO.getParticellaCertificataVO().getIdParticellaCertificata())
            != null)
        {
          storicoParticellaVO.setvSupElegFit(hSupElegFit.get(storicoParticellaVO.getParticellaCertificataVO().getIdParticellaCertificata()));
        }        
      }
    }
  }
	
	
	private Vector<Long> getIdStoricoParticella(Vector<StoricoParticellaVO> elencoParticelle, PaginazioneUtils page)
  {
    Vector<Long> result = new Vector<Long>();
    for (int i = page.getPrimoElementoPaginaCorrente(); i < page.getUltimoElementoPaginaCorrente(); i++)
    {
      Long idStoricoParticella = ((StoricoParticellaVO) elencoParticelle.get(i)).getIdStoricoParticella();
      if(!result.contains(idStoricoParticella))
      {
        result.add(idStoricoParticella);
      }
    }
    
    return result;
  }
	
	private Vector<Long> getIdConduzioneParticella(Vector<StoricoParticellaVO> elencoParticelle, PaginazioneUtils page)
  {
    Vector<Long> result = new Vector<Long>();
    for (int i = page.getPrimoElementoPaginaCorrente(); i < page.getUltimoElementoPaginaCorrente(); i++)
    {
      ConduzioneParticellaVO[] conVO = ((StoricoParticellaVO) elencoParticelle.get(i)).getElencoConduzioni();
      Long idConduzioneParticella = conVO[0].getIdConduzioneParticella();
      if(!result.contains(idConduzioneParticella))
      {
        result.add(idConduzioneParticella);
      }
    }
    
    return result;
  }
	
	private Vector<Long> getIdParticella(Vector<StoricoParticellaVO> elencoParticelle, PaginazioneUtils page)
  {
    Vector<Long> result = new Vector<Long>();
    for (int i = page.getPrimoElementoPaginaCorrente(); i < page.getUltimoElementoPaginaCorrente(); i++)
    {
      Long idParticella = ((StoricoParticellaVO) elencoParticelle.get(i)).getIdParticella();
      if(!result.contains(idParticella))
      {
        result.add(idParticella);
      }
    }
    
    return result;
  }
	
	/**
	 * Esegue la seconda query della ricerca terreni
	 * 
	 * Prima che il sistema proceda nella visualizzazione paginata delle informazioni è necessario memorizzarsi in una hashtable i risultati 
	 * di una una seconda qry la quale non sarà filtrata per i criteri di ricerca ma esclusivamente per l’identificativo azienda ID_AZIENDA 
	 * e per le particelle distinte ID_STORICO_PARTICELLA presenti nella paginazione corrente.
	 * 
	 * Le informazioni estratte che andranno ad integrare il piano colturale sono:
	 * o	Particella Certificata
	 * o	Anomalie Gis (Sospesa, P30, P25, P26)
	 * o	Documenti associati alla conduzione
	 * o	Presenza UV
	 * Il motivo per cui non è possibile filtrare ulteriormente il set di risultati, in relazione agli eventuali filtri impostati dall’utente, 
	 * è che l’anomalia P26, calcolata sulla base dell’utilizzo, se presente si propaga automaticamente a tutta la particella!
	 * In dettaglio per ogni elemento presente nella paginazione accedere alla hashtable:
	 * o	Se ID_UTILIZZO_PARTICELLA=-1 (Utilizzi fittizzi) Accedere per ID_STORICO_PARTICELLA, considerando solamente la prima occorrenza
	 * aggiornare il piano colturale a video. Attenzione in questo caso è necessario forzare sul piano colturale a video il campo SUPERFICIE_ELEG=0
	 * o	Se ID_UTILIZZO_PARTICELLA<>-1 (attenzione potrebbe essere null) Accedere per ID_CONDUZIONE_PARTICELLA & ‘-‘ & ID_UTILIZZO_PARTICELLA,  
	 * consideranado solamente la prima occorrenza aggiornare il piano colturale a video.

	 * @param idConduzioneParticella
	 * @return
	 * @throws DataAccessException
	 */
	private Vector<Long> ricercaSecondaQuery(Hashtable<String,StoricoParticellaVO> caso, 
	    Long idAzienda, Vector<Long> listaIdStoricoParticella, Vector<Long> listaIdConduzioneParticella,
	    Vector<Long> listaIdParticella, BigDecimal p26Valore, Connection conn)
			throws DataAccessException
	{
		SolmrLogger.debug(this, "Invocating ricercaSecondaQuery method in ConduzioneParticellaDAO\n");
		PreparedStatement stmt = null;
		
		Integer numElementiPaginazioneIntg = new Integer(SolmrConstants.NUMBER_RECORDS_FOR_PAGE_TERRENI);
		int numElementiPaginazione = numElementiPaginazioneIntg.intValue();
		String interrogativiByPaginazione = getPuntiInterrogativiByPaginazione(numElementiPaginazione);
		Vector<Long> vIdParticellaCertificata = null;
		
		try
		{
			String query = "WITH DOCUMENTI AS "+
			" (SELECT MAX(D.ID_DOCUMENTO) AS ID_DOCUMENTO, "+
			"         MIN(NVL(DC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY'))) AS MIN_DATA_FINE, "+
			"         MAX(NVL(DC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY'))) AS MAX_DATA_FINE, "+
			"         DC.ID_CONDUZIONE_PARTICELLA "+
			"    FROM DB_DOCUMENTO D, DB_DOCUMENTO_CONDUZIONE DC "+
			"   WHERE D.ID_DOCUMENTO = DC.ID_DOCUMENTO "+
			"     AND DC.ID_CONDUZIONE_PARTICELLA IN " +interrogativiByPaginazione+ //*********************** 1
			"     AND D.ID_AZIENDA = ?    "+  //******************** 2
			"     AND D.ID_STATO_DOCUMENTO IS NULL   "+ 
			"     GROUP BY DC.ID_CONDUZIONE_PARTICELLA), "+
			"UNITA_VITATE AS "+
			" (SELECT SUA.ID_AZIENDA, SUA.ID_PARTICELLA "+
			"    FROM DB_STORICO_UNITA_ARBOREA SUA "+
			"   WHERE SUA.ID_AZIENDA = ? "+  //******************* 3
			"     AND SUA.ID_PARTICELLA IN " +interrogativiByPaginazione+//******************* 4
			"     AND SUA.DATA_FINE_VALIDITA IS NULL "+
			"   GROUP BY SUA.ID_AZIENDA, SUA.ID_PARTICELLA) "+
			"SELECT ANOMALIE_TABLE.*, "+
			"               (CASE "+
			"                 WHEN (ANOMALIE_TABLE.EFFETTUA_CONTROLLI_GIS = 'S' " +
			"                       AND NVL(PCK_SMRGAA_LIBRERIA.Calcola_P30(ANOMALIE_TABLE.ID_STORICO_PARTICELLA,SYSDATE),0) !=0) " +
			"                 THEN  'P30' "+
			"               END) AS P30, "+
			"               (CASE "+
			"                 WHEN (ANOMALIE_TABLE.EFFETTUA_CONTROLLI_GIS = 'S' " +
			"                       AND NVL(PCK_SMRGAA_LIBRERIA.Calcola_P25(ANOMALIE_TABLE.ID_STORICO_PARTICELLA,SYSDATE),0) !=0) " +
			"                 THEN 'P25' "+
			"               END) AS P25, "+
			"               (CASE "+
			"                 WHEN (ANOMALIE_TABLE.EFFETTUA_CONTROLLI_GIS = 'S' " +
			"                       AND NVL(PCK_SMRGAA_LIBRERIA.Calcola_P26_Con_Tolleranza(?,ANOMALIE_TABLE.ID_PARTICELLA,ANOMALIE_TABLE.ID_PARTICELLA_CERTIFICATA,?),0) !=0) THEN "+ //******** 5,6
			"                  'P26' "+
			"               END) AS P26, "+
			"               (CASE "+
			"                 WHEN (ANOMALIE_TABLE.DATA_SOSPENSIONE IS NOT NULL) THEN "+
			"                  'SOSPESA_GIS' "+
			"               END) AS SOSPESA_GIS, "+
			"               (CASE " +
      "                 WHEN EXISTS (SELECT NE.ID_NOTIFICA_ENTITA " + 
      "                              FROM   DB_NOTIFICA_ENTITA NE, " +
      "                                     DB_NOTIFICA NO, " +
      "                                     DB_TIPO_ENTITA TE " +
      "                              WHERE  TE.CODICE_TIPO_ENTITA = 'P' " +
      "                              AND    TE.ID_TIPO_ENTITA = NE.ID_TIPO_ENTITA " +
      "                              AND    NE.IDENTIFICATIVO = ANOMALIE_TABLE.ID_PARTICELLA " +
      "                              AND    NE.ID_NOTIFICA = NO.ID_NOTIFICA " +
      "                              AND    NO.ID_AZIENDA = ? ) THEN " +
      "                   'NOTIFICA' " +
      "                 END ) AS IN_NOTIFICA " +
			"          FROM (SELECT SUP_ELEGGIBILE_TABLE.*, "+
			"                (NVL(PCK_SMRGAA_LIBRERIA.SelTotSupElegByPartEVetCat(SUP_ELEGGIBILE_TABLE.ID_PARTICELLA_CERTIFICATA," +
			"                                                                    SUP_ELEGGIBILE_TABLE.ID_CATALOGO_MATRICE),0)" +
			"                ) AS SUPERFICIE_ELEG, "+
      "                (NVL(PCK_SMRGAA_LIBRERIA.SelTotSupElegByPartEVetVarNTar(SUP_ELEGGIBILE_TABLE.ID_PARTICELLA_CERTIFICATA," +
      "                                                                    SUP_ELEGGIBILE_TABLE.ID_CATALOGO_MATRICE),0)" +
      "                ) AS SUPERFICIE_ELEG_NETTA "+
			"                  FROM (SELECT SP.ID_STORICO_PARTICELLA, "+
			"                               SP.ID_PARTICELLA, "+
			"                               TCP.EFFETTUA_CONTROLLI_GIS, "+
			"                               SP.SUPERFICIE_GRAFICA, "+
      "                               SP.SUP_CATASTALE, "+
			"                               CP.ID_CONDUZIONE_PARTICELLA, "+
			"                               DOC.ID_DOCUMENTO, "+
			"                               DOC.MIN_DATA_FINE, "+
			"                               DOC.MAX_DATA_FINE, "+
			"                               UV.ID_AZIENDA AS ID_AZIENDA_UV, "+
			"                               RCM.ID_CATALOGO_MATRICE, "+
			"                               UP.ID_UTILIZZO_PARTICELLA, "+
			"                               PC.ID_PARTICELLA_CERTIFICATA, "+
			"                               PC.SUP_GRAFICA, "+
			"                               PC.SUP_USO_GRAFICA, "+
			"                               PC.PARTICELLA_A_GIS, "+
			"                               PC.ESITO, "+
			"                               PC.DATA_SOSPENSIONE, "+
			"                               PC.MOTIVAZIONE_GIS, "+
			"                               CP.SUPERFICIE_CONDOTTA, "+
			"                               CP.PERCENTUALE_POSSESSO, " +
			"                               Pck_Smrgaa_Libreria.CercaIstanzaRiesame(EXTRACT(YEAR FROM SYSDATE), ?, SP.ID_PARTICELLA) AS ISTANZA_RIESAME, " + //*********7
			"                               UP.SUPERFICIE_UTILIZZATA " +
			"                          FROM DB_UTE                    U, "+
			"                               DB_STORICO_PARTICELLA     SP, "+
			"                               DB_CONDUZIONE_PARTICELLA  CP, "+
			"                               DOCUMENTI                 DOC, "+
			"                               DB_UTILIZZO_PARTICELLA    UP, "+
			"                               UNITA_VITATE              UV, "+
			"                               DB_PARTICELLA_CERTIFICATA PC, "+
			"                               DB_TIPO_CASO_PARTICOLARE TCP, " +
			"                               DB_R_CATALOGO_MATRICE RCM " +
			"                         WHERE U.ID_AZIENDA = ? "+          //************ 8  
			"						                AND U.DATA_FINE_ATTIVITA IS NULL "+
			"                           AND U.ID_UTE = CP.ID_UTE "+
			"                           AND CP.DATA_FINE_CONDUZIONE IS NULL "+
			"                           AND CP.ID_PARTICELLA = SP.ID_PARTICELLA "+
			"                           AND SP.DATA_FINE_VALIDITA IS NULL "+
			"                           AND CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA " +
			"                           AND UP.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE "+
			"                           AND PC.DATA_FINE_VALIDITA(+) IS NULL "+
			"                           AND SP.ID_PARTICELLA = PC.ID_PARTICELLA(+) "+
			"                           AND UV.ID_PARTICELLA(+) = SP.ID_PARTICELLA "+
			"                           AND DOC.ID_CONDUZIONE_PARTICELLA(+) = CP.ID_CONDUZIONE_PARTICELLA "+
			"                           AND SP.ID_CASO_PARTICOLARE = TCP.ID_CASO_PARTICOLARE (+) " +
			"							              AND SP.ID_STORICO_PARTICELLA IN "+ interrogativiByPaginazione+  //************ 9
			"                         GROUP BY SP.ID_STORICO_PARTICELLA, "+
			"                                  SP.ID_PARTICELLA, "+
			"                                  TCP.EFFETTUA_CONTROLLI_GIS, "+
			"                                  SP.SUPERFICIE_GRAFICA, "+
      "                                  SP.SUP_CATASTALE, "+
			"                                  CP.ID_CONDUZIONE_PARTICELLA, "+
			"                                  DOC.ID_DOCUMENTO, "+
			"                                  DOC.MIN_DATA_FINE, "+
			"                                  DOC.MAX_DATA_FINE, "+
			"                                  UV.ID_AZIENDA, "+
			"                                  RCM.ID_CATALOGO_MATRICE, "+
			"                                  UP.ID_UTILIZZO_PARTICELLA, "+
			"                                  PC.ID_PARTICELLA_CERTIFICATA, "+
			"                                  PC.SUP_GRAFICA, "+
			"                                  PC.SUP_USO_GRAFICA, "+
			"                                  PC.PARTICELLA_A_GIS, "+
			"                                  PC.ESITO, "+
			"                                  PC.DATA_SOSPENSIONE, "+
			"                                  PC.MOTIVAZIONE_GIS, "+
			"                                  CP.SUPERFICIE_CONDOTTA, "+
			"                                  CP.PERCENTUALE_POSSESSO, "+
			"                                  UP.SUPERFICIE_UTILIZZATA " +
			"                        UNION ALL "+
			"                        SELECT SP.ID_STORICO_PARTICELLA, "+
			"                               SP.ID_PARTICELLA, "+
			"                               TCP.EFFETTUA_CONTROLLI_GIS, "+
			"                               SP.SUPERFICIE_GRAFICA, "+
      "                               SP.SUP_CATASTALE, "+
			"                               CP.ID_CONDUZIONE_PARTICELLA, "+
			"                               DOC.ID_DOCUMENTO, "+
			"                               DOC.MIN_DATA_FINE, "+
			"                               DOC.MAX_DATA_FINE, "+
			"                               UV.ID_AZIENDA AS ID_AZIENDA_UV, "+
			"                               -1 AS ID_CATALOGO_MATRICE, "+
			"                               -1 AS ID_UTILIZZO_PARTICELLA, "+
			"                               PC.ID_PARTICELLA_CERTIFICATA, "+
			"                               PC.SUP_GRAFICA, "+
			"                               PC.SUP_USO_GRAFICA, "+
			"                               PC.PARTICELLA_A_GIS, "+
			"                               PC.ESITO, "+
			"                               PC.DATA_SOSPENSIONE, "+
			"                               PC.MOTIVAZIONE_GIS, "+
			"                               CP.SUPERFICIE_CONDOTTA, "+
			"                               CP.PERCENTUALE_POSSESSO, "+
			"                               Pck_Smrgaa_Libreria.CercaIstanzaRiesame(EXTRACT(YEAR FROM SYSDATE), ?, SP.ID_PARTICELLA) AS ISTANZA_RIESAME, " + //******** 10
			"                               TRUNC(DECODE(SP.SUPERFICIE_GRAFICA,0,SP.SUP_CATASTALE,SP.SUPERFICIE_GRAFICA) " +
      "                                   * CP.PERCENTUALE_POSSESSO / 100,4) " +
      "                                 - TRUNC(SUM(NVL(UP.SUPERFICIE_UTILIZZATA,0)),4) " +
			"                               SUPERFICIE_UTILIZZATA " +
			"                          FROM DB_UTE                    U, "+
			"                               DB_STORICO_PARTICELLA     SP, "+
			"                               DB_CONDUZIONE_PARTICELLA  CP, "+
			"                               DOCUMENTI                 DOC, "+
			"                               DB_UTILIZZO_PARTICELLA    UP, "+
			"                               UNITA_VITATE              UV, "+
			"                               DB_PARTICELLA_CERTIFICATA PC, " +
			"                               DB_TIPO_CASO_PARTICOLARE TCP " +
			"                         WHERE U.ID_AZIENDA = ? "+  //************* 11
			"                           AND U.DATA_FINE_ATTIVITA IS NULL "+
			"                           AND U.ID_UTE = CP.ID_UTE "+
			"                           AND CP.DATA_FINE_CONDUZIONE IS NULL "+
			"                           AND CP.ID_PARTICELLA = SP.ID_PARTICELLA "+
			"                           AND SP.DATA_FINE_VALIDITA IS NULL "+
			"                           AND CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA(+) "+
			"                           AND SP.ID_PARTICELLA = PC.ID_PARTICELLA (+) "+
			"                           AND PC.DATA_FINE_VALIDITA(+) IS NULL "+
			"                           AND SP.ID_PARTICELLA = PC.ID_PARTICELLA(+) "+
			"                           AND UV.ID_PARTICELLA(+) = SP.ID_PARTICELLA "+
			"                           AND DOC.ID_CONDUZIONE_PARTICELLA(+) = CP.ID_CONDUZIONE_PARTICELLA " +
			"                           AND SP.ID_CASO_PARTICOLARE = TCP.ID_CASO_PARTICOLARE (+) "+
			"                           AND SP.ID_STORICO_PARTICELLA IN "+interrogativiByPaginazione+ //********** 12
			"                         GROUP BY SP.ID_STORICO_PARTICELLA, "+
			"                                  SP.ID_PARTICELLA, "+
			"                                  TCP.EFFETTUA_CONTROLLI_GIS, "+
			"                                  SP.SUPERFICIE_GRAFICA, "+
      "                                  SP.SUP_CATASTALE, "+
			"                                  CP.ID_CONDUZIONE_PARTICELLA, "+
			"                                  DOC.ID_DOCUMENTO, "+
			"                                  DOC.MIN_DATA_FINE, "+
			"                                  DOC.MAX_DATA_FINE, "+
			"                                  UV.ID_AZIENDA, "+
			"                                  -1, "+
			"                                  -1, "+
			"                                  PC.ID_PARTICELLA_CERTIFICATA, "+
			"                                  PC.SUP_GRAFICA, "+
			"                                  PC.SUP_USO_GRAFICA, "+
			"                                  PC.PARTICELLA_A_GIS, "+
			"                                  PC.ESITO, "+
			"                                  PC.DATA_SOSPENSIONE, "+
			"                                  PC.MOTIVAZIONE_GIS, "+
			"                                  CP.SUPERFICIE_CONDOTTA, "+
			"                                  CP.PERCENTUALE_POSSESSO "+
			"                          HAVING TRUNC(DECODE(SP.SUPERFICIE_GRAFICA,0,SP.SUP_CATASTALE,SP.SUPERFICIE_GRAFICA) " +
      "                              * CP.PERCENTUALE_POSSESSO / 100,4) " +
      "                            - TRUNC(SUM(NVL(UP.SUPERFICIE_UTILIZZATA,0)),4) > 0 " +
			"                        ) SUP_ELEGGIBILE_TABLE) ANOMALIE_TABLE  ";

			stmt = conn.prepareStatement(query);
			
			int idx=0;
			
			idx = setLongStatementPaginazione(stmt,listaIdConduzioneParticella,idx,numElementiPaginazione);
			stmt.setLong(++idx, idAzienda.longValue());
			stmt.setLong(++idx, idAzienda.longValue());
			idx = setLongStatementPaginazione(stmt,listaIdParticella,idx,numElementiPaginazione);
			stmt.setLong(++idx, idAzienda.longValue());
			stmt.setBigDecimal(++idx, p26Valore);
			stmt.setLong(++idx, idAzienda.longValue());
			stmt.setLong(++idx, idAzienda.longValue());
			stmt.setLong(++idx, idAzienda.longValue());
			idx = setLongStatementPaginazione(stmt,listaIdStoricoParticella,idx,numElementiPaginazione);
			stmt.setLong(++idx, idAzienda.longValue());
			stmt.setLong(++idx, idAzienda.longValue());		
			idx = setLongStatementPaginazione(stmt,listaIdStoricoParticella,idx,numElementiPaginazione);
			
			SolmrLogger.debug(this, "Executing ricercaSecondaQuery: " + query + "\n");
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next())
			{
				StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
				String idConduzioneParticella=rs.getString("ID_CONDUZIONE_PARTICELLA");
				String idUtilizzoParticella=rs.getString("ID_UTILIZZO_PARTICELLA");
					
				if(Validator.isNotEmpty(rs.getString("ID_DOCUMENTO"))) 
				{
					DocumentoVO documentoVO = new DocumentoVO();
					documentoVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
					DocumentoConduzioneVO documentoConduzioneVO = new DocumentoConduzioneVO();
					documentoConduzioneVO.setMinDataFineValidita(rs.getDate("MIN_DATA_FINE"));
					documentoConduzioneVO.setMaxDataFineValidita(rs.getDate("MAX_DATA_FINE"));
					documentoVO.setDocumentoConduzioneVO(documentoConduzioneVO);
					storicoParticellaVO.setDocumentoVO(documentoVO);
				}
				
				if(Validator.isNotEmpty(rs.getString("ID_AZIENDA_UV"))) 
				{
					StoricoUnitaArboreaVO storicoUnitaArboreaVO = new StoricoUnitaArboreaVO();
					storicoUnitaArboreaVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA_UV")));
					storicoParticellaVO.setStoricoUnitaArboreaVO(storicoUnitaArboreaVO);
				}		
				
				if(Validator.isNotEmpty(rs.getString("ID_PARTICELLA_CERTIFICATA"))) 
				{
					ParticellaCertificataVO particellaCertificataVO = new ParticellaCertificataVO();
					particellaCertificataVO.setIdParticellaCertificata(new Long(rs.getLong("ID_PARTICELLA_CERTIFICATA")));
					if(vIdParticellaCertificata == null)
					{
					  vIdParticellaCertificata = new Vector<Long>();
					}
					if(!vIdParticellaCertificata.contains(particellaCertificataVO.getIdParticellaCertificata()))
					{
					  vIdParticellaCertificata.add(particellaCertificataVO.getIdParticellaCertificata());
					}
					particellaCertificataVO.setDataSospensione(rs.getDate("DATA_SOSPENSIONE"));
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
          
          if (Validator.isNotEmpty(rs.getString("ESITO")))
          	particellaCertificataVO.setEsito(new Integer(rs.getInt("ESITO")));
          
          if (Validator.isNotEmpty(rs.getString("PARTICELLA_A_GIS")))
          	particellaCertificataVO.setEsito(new Integer(rs.getInt("PARTICELLA_A_GIS")));
          
          particellaCertificataVO.setMotivazioneGis(rs.getString("MOTIVAZIONE_GIS"));
				  //Nuova Eleggibilta Fine ***********
					storicoParticellaVO.setParticellaCertificataVO(particellaCertificataVO);
				}
				
				//*****************Controlli P
				storicoParticellaVO.setGisP30(rs.getString("P30"));
				storicoParticellaVO.setGisP25(rs.getString("P25"));
				storicoParticellaVO.setGisP26(rs.getString("P26"));
				storicoParticellaVO.setSospesaGis(rs.getString("SOSPESA_GIS"));
				//**************************
				
				storicoParticellaVO.setIstanzaRiesame(checkLongNull(rs.getString("ISTANZA_RIESAME")));
				storicoParticellaVO.setInNotifica(rs.getString("IN_NOTIFICA"));
				
				caso.put(idConduzioneParticella+"-"+idUtilizzoParticella,storicoParticellaVO);
			}
			
			rs.close();
			stmt.close();
			
		}
		catch (SQLException exc)
		{
			SolmrLogger.error(this, "ricercaSecondaQuery in ConduzioneParticellaDAO - SQLException: " + exc + "\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex)
		{
			SolmrLogger.error(this, "ricercaSecondaQuery in ConduzioneParticellaDAO - Generic Exception: " + ex + "\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally
		{
			try
			{
				if (stmt != null)
					stmt.close();
			}
			catch (SQLException exc)
			{
				SolmrLogger
						.error(this, "ricercaSecondaQuery in ConduzioneParticellaDAO - SQLException while closing Statement and Connection: " + exc + "\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex)
			{
				SolmrLogger.error(this, "ricercaSecondaQuery in ConduzioneParticellaDAO - Generic Exception while closing Statement and Connection: " + ex
						+ "\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		
		SolmrLogger.debug(this, "Invocated ricercaSecondaQuery method in ConduzioneParticellaDAO\n");
		
		return vIdParticellaCertificata;
	}
	
	
	/**
	 * 
	 * restituisce se esiste un record attivo del biologico legato alla
	 * particella
	 * 
	 * 
	 * @param idAzienda
	 * @param listIdParticella
	 * @param conn
	 * @return
	 * @throws DataAccessException
	 */
	public Vector<Long> getIdParticellaBiologico(long idAzienda, Vector<Long> listIdParticella, Connection conn) throws DataAccessException
  {
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    Vector<Long> vIdParticella = null;
    
    String strIN = " (";
    
    if (listIdParticella != null && listIdParticella.size() != 0)
    {
      for (int i = 0; i < listIdParticella.size(); i++)
      {
        strIN += "?";
        if (i < listIdParticella.size() - 1)
          strIN += ",";
      }
    }
    strIN += ") ";
    
    try
    {
      SolmrLogger.debug(this, "[ConduzioneParticellaDAO::getIdParticellaBiologico] BEGIN.");
      
      // CONCATENAZIONE/CREAZIONE QUERY BEGIN. 

      queryBuf = new StringBuffer();
      queryBuf.append(" " +
          "SELECT " +
          "       PB.ID_PARTICELLA " + 
          "FROM   DB_PARTICELLA_BIO PB " +
          "WHERE  PB.ID_AZIENDA = ? " +
          "AND    PB.ID_PARTICELLA IN "+ strIN +
          "AND    PB.DATA_FINE_VALIDITA IS NULL ");
      
      query = queryBuf.toString();
      // CONCATENAZIONE/CREAZIONE QUERY END. 

      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
        
        SolmrLogger.debug(this, "[ConduzioneParticellaDAO::getIdParticellaBiologico] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      stmt.setLong(++indice, idAzienda);
      
      if (listIdParticella != null && listIdParticella.size() != 0)
      {
        for (int i = 0; i < listIdParticella.size(); i++)
          stmt.setLong(++indice, listIdParticella.get(i).longValue());
      }
      
      // Setto i parametri della query
      rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vIdParticella == null)
        {
          vIdParticella = new Vector<Long>();
        }
        
        vIdParticella.add(new Long(rs.getLong("ID_PARTICELLA")));
      }
      
      return vIdParticella;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[] {
          new Variabile("query", query), 
          new Variabile("queryBuf", queryBuf), 
          new Variabile("vIdParticella", vIdParticella)
      };
      
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[] {
        new Parametro("idAzienda", idAzienda),
        new Parametro("listIdParticella", listIdParticella)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[ConduzioneParticellaDAO::getIdParticellaBiologico] ", t, query, variabili, parametri);
      // Rimappo e rilancio l'eccezione come DataAccessException.
       
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      
       // Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       //* ignora ogni eventuale eccezione)
       
      close(rs, stmt, null);
      
      // Fine metodo
      SolmrLogger.debug(this, "[ConduzioneParticellaDAO::getIdParticellaBiologico] END.");
    }
  }
	
	
	
	
	/**
	 * Metodo che mi restituisce il record su DB_CONDUZIONE_PARTICELLA a partire
	 * dalla chiave primaria
	 * 
	 * @param idConduzioneParticella
	 * @return
	 * @throws DataAccessException
	 */
	public ConduzioneParticellaVO findConduzioneParticellaByPrimaryKey(Long idConduzioneParticella) throws DataAccessException
	{
		SolmrLogger.debug(this, "Invocating findConduzioneParticellaByPrimaryKey method in ConduzioneParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		ConduzioneParticellaVO conduzioneParticellaVO = null;
		
		try
		{
			SolmrLogger.debug(this, "Creating db-connection in findConduzioneParticellaByPrimaryKey method in ConduzioneParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in findConduzioneParticellaByPrimaryKey method in ConduzioneParticellaDAO and it values: " + conn
					+ "\n");
			
			String query = " " +
					"SELECT CP.ID_CONDUZIONE_PARTICELLA, " +
					"		    CP.ID_PARTICELLA, " +
					"		    CP.ID_TITOLO_POSSESSO, " +
					"		    TTP.DESCRIZIONE, " +
					"		    CP.ID_UTE, " +
					"		    CP.SUPERFICIE_CONDOTTA, " +
					"		    CP.FLAG_UTILIZZO_PARTE, " +
					"		    CP.DATA_INIZIO_CONDUZIONE, " +
					"		    CP.DATA_FINE_CONDUZIONE, " +
					"		    CP.NOTE, " +
					"		    CP.DATA_AGGIORNAMENTO, " +
					"		    CP.ID_UTENTE_AGGIORNAMENTO, "	+
					"         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
			        "          || ' ' " + 
			        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
			        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
			        "         WHERE CP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
					"       AS DENOMINAZIONE, " +
					"       (SELECT PVU.DENOMINAZIONE " +
				    "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
				    "        WHERE CP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
					"       AS DESCRIZIONE_ENTE_APPARTENENZA, " +
					"		    CP.ESITO_CONTROLLO, " +
					"		    CP.DATA_ESECUZIONE, "	+
					"       CP.RECORD_MODIFICATO, " +
					"       CP.SUPERFICIE_AGRONOMICA, " +
					"       CP.PERCENTUALE_POSSESSO " +
					"FROM   DB_CONDUZIONE_PARTICELLA CP, " +
					"       DB_TIPO_TITOLO_POSSESSO TTP " +
					//"       PAPUA_V_UTENTE_LOGIN PVU " +
					"WHERE  CP.ID_CONDUZIONE_PARTICELLA = ? "	+
					"AND    CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO ";
					//"AND    CP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN ";
			
			SolmrLogger.debug(this,
					"Value of parameter 1 [ID_CONDUZIONE_PARTICELLA] in findConduzioneParticellaByPrimaryKey method in ConduzioneParticellaDAO: "
							+ idConduzioneParticella + "\n");
			
			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idConduzioneParticella.longValue());
			
			SolmrLogger.debug(this, "Executing findConduzioneParticellaByPrimaryKey: " + query + "\n");
			
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next())
			{
				conduzioneParticellaVO = new ConduzioneParticellaVO();
				conduzioneParticellaVO.setIdConduzioneParticella(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));
				conduzioneParticellaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
				conduzioneParticellaVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
				CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_TITOLO_POSSESSO")), rs.getString("DESCRIZIONE"));
				conduzioneParticellaVO.setTitoloPossesso(code);
				conduzioneParticellaVO.setIdUte(new Long(rs.getLong("ID_UTE")));
				conduzioneParticellaVO.setSuperficieCondotta(rs.getString("SUPERFICIE_CONDOTTA"));
				conduzioneParticellaVO.setFlagUtilizzoParte(rs.getString("FLAG_UTILIZZO_PARTE"));
				conduzioneParticellaVO.setDataInizioConduzione(rs.getTimestamp("DATA_INIZIO_CONDUZIONE"));
				conduzioneParticellaVO.setDataFineConduzione(rs.getTimestamp("DATA_FINE_CONDUZIONE"));
				conduzioneParticellaVO.setNote(rs.getString("NOTE"));
				conduzioneParticellaVO.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO"));
				conduzioneParticellaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				UtenteIrideVO utenteIrideVO = new UtenteIrideVO();
				utenteIrideVO.setDenominazione(rs.getString("DENOMINAZIONE"));
				utenteIrideVO.setDescrizioneEnteAppartenenza(rs.getString("DESCRIZIONE_ENTE_APPARTENENZA"));
				utenteIrideVO.setDenominazione("");
        utenteIrideVO.setDescrizioneEnteAppartenenza("");
				conduzioneParticellaVO.setUtenteAggiornamento(utenteIrideVO);
				conduzioneParticellaVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
				conduzioneParticellaVO.setDataEsecuzione(rs.getDate("DATA_ESECUZIONE"));
				conduzioneParticellaVO.setRecordModificato(rs.getString("RECORD_MODIFICATO"));
				conduzioneParticellaVO.setSuperficieAgronomica(rs.getString("SUPERFICIE_AGRONOMICA"));
				conduzioneParticellaVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
			}
			
			rs.close();
			stmt.close();
			
		}
		catch (SQLException exc)
		{
			SolmrLogger.error(this, "findConduzioneParticellaByPrimaryKey in ConduzioneParticellaDAO - SQLException: " + exc + "\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex)
		{
			SolmrLogger.error(this, "findConduzioneParticellaByPrimaryKey in ConduzioneParticellaDAO - Generic Exception: " + ex + "\n");
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
						"findConduzioneParticellaByPrimaryKey in ConduzioneParticellaDAO - SQLException while closing Statement and Connection: " + exc + "\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex)
			{
				SolmrLogger.error(this,
						"findConduzioneParticellaByPrimaryKey in ConduzioneParticellaDAO - Generic Exception while closing Statement and Connection: " + ex
								+ "\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated findConduzioneParticellaByPrimaryKey method in ConduzioneParticellaDAO\n");
		return conduzioneParticellaVO;
	}
	
	/**
	 * Metodo utilizzato per effettuare la modifica del record relativo alla
	 * tabella DB_CONDUZIONE_PARTICELLA
	 * 
	 * @param conduzioneParticellaVO
	 * @throws DataAccessException
	 */
	public void updateConduzioneParticella(ConduzioneParticellaVO conduzioneParticellaVO) throws DataAccessException
	{
		SolmrLogger.debug(this, "Invocating updateConduzioneParticella method in ConduzioneParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try
		{
			SolmrLogger.debug(this, "Creating db-connection in updateConduzioneParticella method in ConduzioneParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in updateConduzioneParticella method in ConduzioneParticellaDAO and it values: " + conn + "\n");
			
			String query = " " +
					"UPDATE DB_CONDUZIONE_PARTICELLA " +
			    "SET    ID_PARTICELLA = ?, " +
			    "       ID_TITOLO_POSSESSO = ?, "	+
			    "       ID_UTE = ?, " +
			    "       SUPERFICIE_CONDOTTA = ?, " +
			    "       PERCENTUALE_POSSESSO = ?, " +
			    "       FLAG_UTILIZZO_PARTE = ?, " +
			    "       DATA_INIZIO_CONDUZIONE = ?, " +
			    "       DATA_FINE_CONDUZIONE = ?, " +
			    "       NOTE = ?, " +
			    "       DATA_AGGIORNAMENTO = ?, "	+
			    "       ID_UTENTE_AGGIORNAMENTO = ?, " +
			    "       ESITO_CONTROLLO = ?, " +
			    "       DATA_ESECUZIONE = ?, " +
			    "       RECORD_MODIFICATO = ?, " +
			    "       SUPERFICIE_AGRONOMICA = ? " +
			    "WHERE  ID_CONDUZIONE_PARTICELLA = ? ";
			
			stmt = conn.prepareStatement(query);
			
			int indice = 0;
			
			SolmrLogger.debug(this, "Executing updateConduzioneParticella: " + query);
			
			stmt.setLong(++indice, conduzioneParticellaVO.getIdParticella().longValue());
			stmt.setLong(++indice, conduzioneParticellaVO.getIdTitoloPossesso().longValue());
			stmt.setLong(++indice, conduzioneParticellaVO.getIdUte().longValue());
		  stmt.setString(++indice, StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieCondotta()));
			stmt.setBigDecimal(++indice, conduzioneParticellaVO.getPercentualePossesso());
   		stmt.setString(++indice, conduzioneParticellaVO.getFlagUtilizzoParte());
			stmt.setTimestamp(++indice, convertDateToTimestamp(conduzioneParticellaVO.getDataInizioConduzione()));
			if (conduzioneParticellaVO.getDataFineConduzione() != null)
			{
				stmt.setTimestamp(++indice, convertDateToTimestamp(conduzioneParticellaVO.getDataFineConduzione()));
			}
			else
			{
				stmt.setString(++indice, null);
		  }
			stmt.setString(++indice, conduzioneParticellaVO.getNote());
			stmt.setTimestamp(++indice, convertDateToTimestamp(conduzioneParticellaVO.getDataAggiornamento()));
			stmt.setLong(++indice, conduzioneParticellaVO.getIdUtenteAggiornamento().longValue());
			stmt.setString(++indice, conduzioneParticellaVO.getEsitoControllo());
		  if (conduzioneParticellaVO.getDataEsecuzione() != null)
			{
				stmt.setTimestamp(++indice, convertDateToTimestamp(conduzioneParticellaVO.getDataEsecuzione()));
			}
			else
			{
				stmt.setString(++indice, null);
			}
			stmt.setString(++indice, conduzioneParticellaVO.getRecordModificato());
			if (Validator.isNotEmpty(conduzioneParticellaVO.getSuperficieAgronomica()))
			{
				stmt.setString(++indice, StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieAgronomica()));
			}
			else
			{
				stmt.setString(++indice, null);
			}
			stmt.setLong(++indice, conduzioneParticellaVO.getIdConduzioneParticella().longValue());
			
			stmt.executeUpdate();
			
			stmt.close();
		}
		catch (SQLException exc)
		{
			SolmrLogger.error(this, "updateConduzioneParticella in ConduzioneParticellaDAO - SQLException: " + exc);
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex)
		{
			SolmrLogger.error(this, "updateConduzioneParticella in ConduzioneParticellaDAO - Generic Exception: " + ex);
			throw new DataAccessException(ex.getMessage());
		}
		finally
		{
			try
			{
				if (stmt != null)
				{
					stmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (SQLException exc)
			{
				SolmrLogger
						.error(this, "updateConduzioneParticella in ConduzioneParticellaDAO - SQLException while closing Statement and Connection: " + exc);
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex)
			{
				SolmrLogger.error(this, "updateConduzioneParticella in ConduzioneParticellaDAO - Generic Exception while closing Statement and Connection: "
						+ ex);
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated updateConduzioneParticella method in ConduzioneParticellaDAO\n");
	}
	
	
	public void storicizzaConduzioneParticella(ConduzioneParticellaVO conduzioneParticellaVO, Long idUtenteAggiornamento) throws DataAccessException
  {
    SolmrLogger.debug(this, "Invocating storicizzaConduzioneParticella method in ConduzioneParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    
    try
    {
      SolmrLogger.debug(this, "Creating db-connection in storicizzaConduzioneParticella method in ConduzioneParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in storicizzaConduzioneParticella method in ConduzioneParticellaDAO and it values: " + conn + "\n");
      
      String query = " " +
          "UPDATE DB_CONDUZIONE_PARTICELLA " +
          "SET    DATA_FINE_CONDUZIONE = SYSDATE, " +
          "       DATA_AGGIORNAMENTO = SYSDATE, " +
          "       ID_UTENTE_AGGIORNAMENTO = ?, " +
          "       NOTE = ? " +
          "WHERE  ID_CONDUZIONE_PARTICELLA = ? ";
      
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      SolmrLogger.debug(this, "Executing storicizzaConduzioneParticella: " + query);
      
      stmt.setLong(++indice, idUtenteAggiornamento.longValue());
      stmt.setString(++indice, conduzioneParticellaVO.getNote());
      stmt.setLong(++indice, conduzioneParticellaVO.getIdConduzioneParticella().longValue());
      
      stmt.executeUpdate();
      
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "storicizzaConduzioneParticella in ConduzioneParticellaDAO - SQLException: " + exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this, "storicizzaConduzioneParticella in ConduzioneParticellaDAO - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
        {
          stmt.close();
        }
        if (conn != null)
        {
          conn.close();
        }
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .error(this, "storicizzaConduzioneParticella in ConduzioneParticellaDAO - SQLException while closing Statement and Connection: " + exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.error(this, "storicizzaConduzioneParticella in ConduzioneParticellaDAO - Generic Exception while closing Statement and Connection: "
            + ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated storicizzaConduzioneParticella method in ConduzioneParticellaDAO\n");
  }
	
	public void updateCondottaConduzioneParticella(long idConduzioneParticella, String supCondotta) 
	  throws DataAccessException
  {
    SolmrLogger.debug(this, "Invocating updateCondottaConduzioneParticella method in ConduzioneParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    
    try
    {
      SolmrLogger.debug(this, "Creating db-connection in updateCondottaConduzioneParticella method in ConduzioneParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in updateCondottaConduzioneParticella method in ConduzioneParticellaDAO and it values: " + conn + "\n");
      
      String query = " " +
          "UPDATE DB_CONDUZIONE_PARTICELLA " +
          "SET    SUPERFICIE_CONDOTTA = ? " +
          "WHERE  ID_CONDUZIONE_PARTICELLA = ? ";
      
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      SolmrLogger.debug(this, "Executing updateConduzioneParticella: " + query);
      
      stmt.setString(++indice, StringUtils.parseSuperficieField(supCondotta));
      SolmrLogger.debug(this, "Value of parameter [SUPERFICIE_CONDOTTA] in method updateCondottaConduzioneParticella in ConduzioneParticellaDAO: "
          + StringUtils.parseSuperficieField(supCondotta) + "\n");
      stmt.setLong(++indice, idConduzioneParticella);
      SolmrLogger.debug(this, "Value of parameter [ID_CONDUZIONE_PARTICELLA] in method updateCondottaConduzioneParticella in ConduzioneParticellaDAO: "
          + idConduzioneParticella + "\n");
      
      stmt.executeUpdate();
      
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "updateCondottaConduzioneParticella in ConduzioneParticellaDAO - SQLException: " + exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this, "updateCondottaConduzioneParticella in ConduzioneParticellaDAO - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
        {
          stmt.close();
        }
        if (conn != null)
        {
          conn.close();
        }
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .error(this, "updateCondottaConduzioneParticella in ConduzioneParticellaDAO - SQLException while closing Statement and Connection: " + exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.error(this, "updateCondottaConduzioneParticella in ConduzioneParticellaDAO - Generic Exception while closing Statement and Connection: "
            + ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated updateCondottaConduzioneParticella method in ConduzioneParticellaDAO\n");
  }
	
	/**
	 * Metodo utilizzato per effettuare l'inserimento di un record nella tabella
	 * DB_CONDUZIONE_PARTICELLA
	 * 
	 * @param conduzioneParticellaVO
	 * @return java.lang.Long idConduzioneParticella
	 * @throws DataAccessException
	 */
	public Long insertConduzioneParticella(ConduzioneParticellaVO conduzioneParticellaVO) throws DataAccessException
	{
		SolmrLogger.debug(this, "Invocating insertConduzioneParticella method in ConduzioneParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Long idConduzioneParticella = null;
		
		try
		{
			idConduzioneParticella = getNextPrimaryKey((String) SolmrConstants.get("SEQ_CONDUZIONE_PARTICELLA"));
			SolmrLogger.debug(this, "Creating db-connection in insertConduzioneParticella method in ConduzioneParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in insertConduzioneParticella method in ConduzioneParticellaDAO and it values: " + conn + "\n");
			
			String query = " " +
					"INSERT INTO DB_CONDUZIONE_PARTICELLA " +
					"            (ID_CONDUZIONE_PARTICELLA, " +
					"             ID_PARTICELLA, " +
					"             ID_TITOLO_POSSESSO, " +
					"             ID_UTE, " +
					"             SUPERFICIE_CONDOTTA, " +
					"             PERCENTUALE_POSSESSO, " +
					"             FLAG_UTILIZZO_PARTE, " +
					"             DATA_INIZIO_CONDUZIONE, " +
					"             DATA_FINE_CONDUZIONE, "	+
					"             NOTE, " +
					"             DATA_AGGIORNAMENTO, " +
					"             ID_UTENTE_AGGIORNAMENTO, " +
					"             ESITO_CONTROLLO, " +
					"             DATA_ESECUZIONE, " +
					"             RECORD_MODIFICATO, " +
					"             SUPERFICIE_AGRONOMICA) " +
					" VALUES       (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			stmt = conn.prepareStatement(query);
			
			int indice = 0;
			SolmrLogger.debug(this, "Executing insertConduzioneParticella: " + query);
			
			stmt.setLong(++indice, idConduzioneParticella.longValue());
			stmt.setLong(++indice, conduzioneParticellaVO.getIdParticella().longValue());
			stmt.setLong(++indice, conduzioneParticellaVO.getIdTitoloPossesso().longValue());
			stmt.setLong(++indice, conduzioneParticellaVO.getIdUte().longValue());
		  stmt.setString(++indice, StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieCondotta()));
			stmt.setBigDecimal(++indice, conduzioneParticellaVO.getPercentualePossesso());
    	stmt.setString(++indice, conduzioneParticellaVO.getFlagUtilizzoParte());
		  stmt.setTimestamp(++indice, convertDateToTimestamp(conduzioneParticellaVO.getDataInizioConduzione()));
			if (conduzioneParticellaVO.getDataFineConduzione() != null)
			{
				stmt.setTimestamp(++indice, convertDateToTimestamp(conduzioneParticellaVO.getDataFineConduzione()));
	  	}
			else
			{
				stmt.setString(++indice, null);
			}
			stmt.setString(++indice, conduzioneParticellaVO.getNote());
			stmt.setTimestamp(++indice, convertDateToTimestamp(conduzioneParticellaVO.getDataAggiornamento()));
			stmt.setLong(++indice, conduzioneParticellaVO.getIdUtenteAggiornamento().longValue());
			stmt.setString(++indice, conduzioneParticellaVO.getEsitoControllo());
			if (conduzioneParticellaVO.getDataEsecuzione() != null)
			{
				stmt.setTimestamp(++indice, convertDateToTimestamp(conduzioneParticellaVO.getDataEsecuzione()));
			
			}
			else
			{
				stmt.setString(++indice, null);
			}
			stmt.setString(++indice, conduzioneParticellaVO.getRecordModificato());
			if (Validator.isNotEmpty(conduzioneParticellaVO.getSuperficieAgronomica()))
			{
				stmt.setString(++indice, StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieAgronomica()));
			}
			else
			{
				stmt.setString(++indice, null);
			}
			
			stmt.executeUpdate();
			
			stmt.close();
		}
		catch (SQLException exc)
		{
			SolmrLogger.error(this, "insertConduzioneParticella in ConduzioneParticellaDAO - SQLException: " + exc);
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex)
		{
			SolmrLogger.error(this, "insertConduzioneParticella in ConduzioneParticellaDAO - Generic Exception: " + ex);
			throw new DataAccessException(ex.getMessage());
		}
		finally
		{
			try
			{
				if (stmt != null)
				{
					stmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (SQLException exc)
			{
				SolmrLogger
						.error(this, "insertConduzioneParticella in ConduzioneParticellaDAO - SQLException while closing Statement and Connection: " + exc);
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex)
			{
				SolmrLogger.error(this, "insertConduzioneParticella in ConduzioneParticellaDAO - Generic Exception while closing Statement and Connection: "
						+ ex);
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated insertConduzioneParticella method in ConduzioneParticellaDAO\n");
		return idConduzioneParticella;
	}
	
	/**
	 * Metodo che mi restituisce la data più recente relativa ad un'elenco di
	 * conduzioni selezionate
	 * 
	 * @param idConduzioneParticella
	 * @return java.util.Date
	 * @throws DataAccessException
	 */
	public java.util.Date getMaxDataInizioConduzioneParticella(Vector<Long> elencoConduzioni) throws DataAccessException
	{
		SolmrLogger.debug(this, "Invocating getMaxDataInizioConduzioneParticella method in ConduzioneParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		java.util.Date dataMax = null;
		
		try
		{
			SolmrLogger.debug(this, "Creating db-connection in getMaxDataInizioConduzioneParticella method in ConduzioneParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getMaxDataInizioConduzioneParticella method in ConduzioneParticellaDAO and it values: " + conn
					+ "\n");
			
			String query = " SELECT MAX(DATA_INIZIO_CONDUZIONE) AS DATA_MAX " + " FROM   DB_CONDUZIONE_PARTICELLA " + " WHERE  ";
			
			for (int i = 0; i < elencoConduzioni.size(); i++)
			{
				if (i >= 1)
				{
					query += " OR ";
				}
				query += " ID_CONDUZIONE_PARTICELLA =  " + elencoConduzioni.elementAt(i);
			}
			
			SolmrLogger.debug(this, "Value of parameter 1 [ELENCO_CONDUZIONI] in getMaxDataInizioConduzioneParticella method in ConduzioneParticellaDAO: "
					+ elencoConduzioni + "\n");
			
			stmt = conn.prepareStatement(query);
			
			SolmrLogger.debug(this, "Executing getMaxDataInizioConduzioneParticella: " + query + "\n");
			
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next())
			{
				dataMax = rs.getDate("DATA_MAX");
			}
			
			rs.close();
			stmt.close();
			
		}
		catch (SQLException exc)
		{
			SolmrLogger.error(this, "getMaxDataInizioConduzioneParticella in ConduzioneParticellaDAO - SQLException: " + exc.getMessage() + "\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex)
		{
			SolmrLogger.error(this, "getMaxDataInizioConduzioneParticella in ConduzioneParticellaDAO - Generic Exception: " + ex + "\n");
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
						"getMaxDataInizioConduzioneParticella in ConduzioneParticellaDAO - SQLException while closing Statement and Connection: "
								+ exc.getMessage() + "\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex)
			{
				SolmrLogger.error(this,
						"getMaxDataInizioConduzioneParticella in ConduzioneParticellaDAO - Generic Exception while closing Statement and Connection: "
								+ ex.getMessage() + "\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getMaxDataInizioConduzioneParticella method in ConduzioneParticellaDAO\n");
		return dataMax;
	}
	
	/**
	 * Metodo che mi restituisce l'elenco delle conduzioni a partire
	 * dall'id_azienda
	 * 
	 * @param Long
	 *          idAzienda
	 * @param boolean
	 *          onlyActive
	 * @param String[]
	 *          orderBy
	 * @return it.csi.solmr.dto.anag.terreni.ConduzioneParticellaVO[]
	 * @throws DataAccessException
	 */
	public ConduzioneParticellaVO[] getListConduzioneParticellaByIdAzienda(Long idAzienda, boolean onlyActive, String[] orderBy)
			throws DataAccessException
	{
		SolmrLogger.debug(this, "Invocating getListConduzioneParticellaByIdAzienda method in ConduzioneParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<ConduzioneParticellaVO> elencoConduzioni = new Vector<ConduzioneParticellaVO>();
		
		try
		{
			SolmrLogger.debug(this, "Creating db-connection in getListConduzioneParticellaByIdAzienda method in ConduzioneParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListConduzioneParticellaByIdAzienda method in ConduzioneParticellaDAO and it values: "
					+ conn + "\n");
			
			String query = 
			  " SELECT CP.ID_CONDUZIONE_PARTICELLA, " +
			  "		CP.ID_PARTICELLA, " +
			  "		CP.ID_TITOLO_POSSESSO, " +
			  "		TTP.DESCRIZIONE, " +
			  "		CP.ID_UTE, " +
			  "		CP.SUPERFICIE_CONDOTTA, " +
			  "		CP.FLAG_UTILIZZO_PARTE, " +
			  "		CP.DATA_INIZIO_CONDUZIONE, " +
			  "		CP.DATA_FINE_CONDUZIONE, " +
			  "		CP.NOTE, " +
			  "		CP.DATA_AGGIORNAMENTO, " +
			  "		CP.ID_UTENTE_AGGIORNAMENTO, " +
			  "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
		      "          || ' ' " + 
		      "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
		      "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
		      "         WHERE CP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
			  "   AS DENOMINAZIONE, " +
			  "       (SELECT PVU.DENOMINAZIONE " +
			  "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
			  "        WHERE CP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
			  "   AS DESCRIZIONE_ENTE_APPARTENENZA, " +
			  "		CP.ESITO_CONTROLLO, " +
			  "		CP.DATA_ESECUZIONE, "	+
			  "   CP.RECORD_MODIFICATO " +
			  " FROM   DB_CONDUZIONE_PARTICELLA CP, " +
			  "        DB_TIPO_TITOLO_POSSESSO TTP, "	+
			  //"        PAPUA_V_UTENTE_LOGIN PVU, " +
			  "        DB_UTE U " +
			  " WHERE  U.ID_AZIENDA = ? " +
			  " AND    U.ID_UTE = CP.ID_UTE "	+
			  " AND    CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO ";
			  //" AND    CP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN ";
			if (onlyActive)
			{
				query += " AND    CP.DATA_FINE_CONDUZIONE IS NULL ";
			}
			String ordinamento = "";
			if (orderBy != null && orderBy.length > 0)
			{
				String criterio = "";
				for (int i = 0; i < orderBy.length; i++)
				{
					if (i == 0)
					{
						criterio = (String) orderBy[i];
					}
					else
					{
						criterio += ", " + (String) orderBy[i];
					}
				}
				ordinamento = "ORDER BY " + criterio;
			}
			if (!ordinamento.equals(""))
			{
				query += ordinamento;
			}
			
			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListConduzioneParticellaByIdAzienda method in ConduzioneParticellaDAO: "
					+ idAzienda + "\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ONLY_ACTIVE] in getListConduzioneParticellaByIdAzienda method in ConduzioneParticellaDAO: "
					+ onlyActive + "\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ORDER_BY] in getListConduzioneParticellaByIdAzienda method in ConduzioneParticellaDAO: "
					+ orderBy + "\n");
			
			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());
			
			SolmrLogger.debug(this, "Executing getListConduzioneParticellaByIdAzienda: " + query + "\n");
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next())
			{
				ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
				conduzioneParticellaVO.setIdConduzioneParticella(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));
				conduzioneParticellaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
				conduzioneParticellaVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
				CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_TITOLO_POSSESSO")), rs.getString("DESCRIZIONE"));
				conduzioneParticellaVO.setTitoloPossesso(code);
				conduzioneParticellaVO.setIdUte(new Long(rs.getLong("ID_UTE")));
				conduzioneParticellaVO.setSuperficieCondotta(rs.getString("SUPERFICIE_CONDOTTA"));
				conduzioneParticellaVO.setFlagUtilizzoParte(rs.getString("FLAG_UTILIZZO_PARTE"));
				conduzioneParticellaVO.setDataInizioConduzione(rs.getDate("DATA_INIZIO_CONDUZIONE"));
				conduzioneParticellaVO.setDataFineConduzione(rs.getDate("DATA_FINE_CONDUZIONE"));
				conduzioneParticellaVO.setNote(rs.getString("NOTE"));
				conduzioneParticellaVO.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO"));
				conduzioneParticellaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				UtenteIrideVO utenteIrideVO = new UtenteIrideVO();
				utenteIrideVO.setDenominazione(rs.getString("DENOMINAZIONE"));
				utenteIrideVO.setDescrizioneEnteAppartenenza(rs.getString("DESCRIZIONE_ENTE_APPARTENENZA"));
				conduzioneParticellaVO.setUtenteAggiornamento(utenteIrideVO);
				conduzioneParticellaVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
				conduzioneParticellaVO.setDataEsecuzione(rs.getDate("DATA_ESECUZIONE"));
				conduzioneParticellaVO.setRecordModificato(rs.getString("RECORD_MODIFICATO"));
				elencoConduzioni.add(conduzioneParticellaVO);
			}
			
			rs.close();
			stmt.close();
			
		}
		catch (SQLException exc)
		{
			SolmrLogger.error(this, "getListConduzioneParticellaByIdAzienda in ConduzioneParticellaDAO - SQLException: " + exc.getMessage() + "\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex)
		{
			SolmrLogger.error(this, "getListConduzioneParticellaByIdAzienda in ConduzioneParticellaDAO - Generic Exception: " + ex + "\n");
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
						"getListConduzioneParticellaByIdAzienda in ConduzioneParticellaDAO - SQLException while closing Statement and Connection: "
								+ exc.getMessage() + "\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex)
			{
				SolmrLogger.error(this,
						"getListConduzioneParticellaByIdAzienda in ConduzioneParticellaDAO - Generic Exception while closing Statement and Connection: "
								+ ex.getMessage() + "\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListConduzioneParticellaByIdAzienda method in ConduzioneParticellaDAO\n");
		if (elencoConduzioni.size() == 0)
		{
			return (ConduzioneParticellaVO[]) elencoConduzioni.toArray(new ConduzioneParticellaVO[0]);
		}
		else
		{
			return (ConduzioneParticellaVO[]) elencoConduzioni.toArray(new ConduzioneParticellaVO[elencoConduzioni.size()]);
		}
	}
	
	/**
	 * Metodo che mi restituisce l'elenco delle conduzioni a partire
	 * dall'id_azienda e dall'id_particella
	 * 
	 * @param Long
	 *          idAzienda
	 * @param Long
	 *          idParticella
	 * @param boolean
	 *          onlyActive
	 * @param String[]
	 *          orderBy
	 * @return it.csi.solmr.dto.anag.terreni.ConduzioneParticellaVO[]
	 * @throws DataAccessException
	 */
	public ConduzioneParticellaVO[] getListConduzioneParticellaByIdAziendaAndIdParticella(Long idAzienda, 
	    Long idParticella, boolean onlyActive,
			String[] orderBy) throws DataAccessException
	{
		SolmrLogger.debug(this, "Invocating getListConduzioneParticellaByIdAziendaAndIdParticella method in ConduzioneParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<ConduzioneParticellaVO> elencoConduzioni = new Vector<ConduzioneParticellaVO>();
		
		try
		{
			SolmrLogger.debug(this, "Creating db-connection in getListConduzioneParticellaByIdAziendaAndIdParticella method in ConduzioneParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this,
					"Created db-connection in getListConduzioneParticellaByIdAziendaAndIdParticella method in ConduzioneParticellaDAO and it values: " + conn
							+ "\n");
			
			String query = 
			  " SELECT CP.ID_CONDUZIONE_PARTICELLA, " +
			  "		     CP.ID_PARTICELLA, " +
			  "		     CP.ID_TITOLO_POSSESSO, " +
			  "		     TTP.DESCRIZIONE, "	+
			  "		     CP.ID_UTE, " +
			  "		     CP.SUPERFICIE_CONDOTTA, " +
			  "		     CP.FLAG_UTILIZZO_PARTE, " +
			  "		     CP.DATA_INIZIO_CONDUZIONE, "	+
			  "		     CP.DATA_FINE_CONDUZIONE, " +
			  "		     CP.NOTE, " +
			  "		     CP.DATA_AGGIORNAMENTO, " +
			  "		     CP.ID_UTENTE_AGGIORNAMENTO, " +
			  "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
		      "          || ' ' " + 
		      "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
		      "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
		      "         WHERE CP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
			  "        AS DENOMINAZIONE, " +
			  "       (SELECT PVU.DENOMINAZIONE " +
			  "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
			  "        WHERE CP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
			  "        AS DESCRIZIONE_ENTE_APPARTENENZA, " +
			  "		     CP.ESITO_CONTROLLO, " +
			  "		     CP.DATA_ESECUZIONE, " +
			  "        CP.RECORD_MODIFICATO " +
			  " FROM   DB_CONDUZIONE_PARTICELLA CP, " +
			  "        DB_TIPO_TITOLO_POSSESSO TTP, "	+
			  //"        PAPUA_V_UTENTE_LOGIN PVU, " +
			  "        DB_UTE U " +
			  " WHERE  U.ID_AZIENDA = ? " +
			  " AND    CP.ID_PARTICELLA = ? "	+
			  " AND    U.ID_UTE = CP.ID_UTE " +
			  " AND    CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO ";
			  //" AND    CP.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN ";
			
			if (onlyActive)
			{
				query += " AND    CP.DATA_FINE_CONDUZIONE IS NULL ";
			}
			String ordinamento = "";
			if (orderBy != null && orderBy.length > 0)
			{
				String criterio = "";
				for (int i = 0; i < orderBy.length; i++)
				{
					if (i == 0)
					{
						criterio = (String) orderBy[i];
					}
					else
					{
						criterio += ", " + (String) orderBy[i];
					}
				}
				ordinamento = "ORDER BY " + criterio;
			}
			if (!ordinamento.equals(""))
			{
				query += ordinamento;
			}
			
			SolmrLogger.debug(this,
					"Value of parameter 1 [ID_AZIENDA] in getListConduzioneParticellaByIdAziendaAndIdParticella method in ConduzioneParticellaDAO: "
							+ idAzienda + "\n");
			SolmrLogger.debug(this,
					"Value of parameter 2 [ID_PARTICELLA] in getListConduzioneParticellaByIdAziendaAndIdParticella method in ConduzioneParticellaDAO: "
							+ idParticella + "\n");
			SolmrLogger.debug(this,
					"Value of parameter 3 [ONLY_ACTIVE] in getListConduzioneParticellaByIdAziendaAndIdParticella method in ConduzioneParticellaDAO: "
							+ onlyActive + "\n");
			SolmrLogger.debug(this,
					"Value of parameter 4 [ORDER_BY] in getListConduzioneParticellaByIdAziendaAndIdParticella method in ConduzioneParticellaDAO: " + orderBy
							+ "\n");
			
			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());
			stmt.setLong(2, idParticella.longValue());
			
			SolmrLogger.debug(this, "Executing getListConduzioneParticellaByIdAziendaAndIdParticella: " + query + "\n");
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next())
			{
				ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
				conduzioneParticellaVO.setIdConduzioneParticella(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));
				conduzioneParticellaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
				conduzioneParticellaVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
				CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_TITOLO_POSSESSO")), rs.getString("DESCRIZIONE"));
				conduzioneParticellaVO.setTitoloPossesso(code);
				conduzioneParticellaVO.setIdUte(new Long(rs.getLong("ID_UTE")));
				conduzioneParticellaVO.setSuperficieCondotta(rs.getString("SUPERFICIE_CONDOTTA"));
				conduzioneParticellaVO.setFlagUtilizzoParte(rs.getString("FLAG_UTILIZZO_PARTE"));
				conduzioneParticellaVO.setDataInizioConduzione(rs.getDate("DATA_INIZIO_CONDUZIONE"));
				conduzioneParticellaVO.setDataFineConduzione(rs.getDate("DATA_FINE_CONDUZIONE"));
				conduzioneParticellaVO.setNote(rs.getString("NOTE"));
				conduzioneParticellaVO.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO"));
				conduzioneParticellaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				UtenteIrideVO utenteIrideVO = new UtenteIrideVO();
				utenteIrideVO.setDenominazione(rs.getString("DENOMINAZIONE"));
				utenteIrideVO.setDescrizioneEnteAppartenenza(rs.getString("DESCRIZIONE_ENTE_APPARTENENZA"));
				conduzioneParticellaVO.setUtenteAggiornamento(utenteIrideVO);
				conduzioneParticellaVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
				conduzioneParticellaVO.setDataEsecuzione(rs.getDate("DATA_ESECUZIONE"));
				conduzioneParticellaVO.setRecordModificato(rs.getString("RECORD_MODIFICATO"));
				elencoConduzioni.add(conduzioneParticellaVO);
			}
			
			rs.close();
			stmt.close();
			
		}
		catch (SQLException exc)
		{
			SolmrLogger.error(this, "getListConduzioneParticellaByIdAziendaAndIdParticella in ConduzioneParticellaDAO - SQLException: " + exc.getMessage()
					+ "\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex)
		{
			SolmrLogger.error(this, "getListConduzioneParticellaByIdAziendaAndIdParticella in ConduzioneParticellaDAO - Generic Exception: " + ex + "\n");
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
						"getListConduzioneParticellaByIdAziendaAndIdParticella in ConduzioneParticellaDAO - SQLException while closing Statement and Connection: "
								+ exc.getMessage() + "\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex)
			{
				SolmrLogger.error(this,
						"getListConduzioneParticellaByIdAziendaAndIdParticella in ConduzioneParticellaDAO - Generic Exception while closing Statement and Connection: "
								+ ex.getMessage() + "\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListConduzioneParticellaByIdAziendaAndIdParticella method in ConduzioneParticellaDAO\n");
		if (elencoConduzioni.size() == 0)
		{
			return (ConduzioneParticellaVO[]) elencoConduzioni.toArray(new ConduzioneParticellaVO[0]);
		}
		else
		{
			return (ConduzioneParticellaVO[]) elencoConduzioni.toArray(new ConduzioneParticellaVO[elencoConduzioni.size()]);
		}
	}
	
	/**
	 * Metodo che mi restituisce l'elenco delle particelle in tutte le sue
	 * componenti (DB_STORICO_PARTICELLA, DB_CONDUZIONE_PARTICELLA,
	 * DB_UTILIZZO_PARTICELLA) in relazione dei parametri di ricerca impostati, di
	 * un criterio di ordinamento e dell'azienda selezionata utilizzato per il
	 * brogliaccio del nuovo territoriale
	 * 
	 * @param filtriParticellareRicercaVO
	 * @param idAzienda
	 * @return java.util.Vector
	 * @throws DataAccessException
	 */
	public Vector<AnagParticellaExcelVO> searchParticelleExcelByParameters(FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda)
			throws DataAccessException
	{
		SolmrLogger.debug(this, "Invocating searchParticelleExcelByParameters method in ConduzioneParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<AnagParticellaExcelVO> elencoParticelle = new Vector<AnagParticellaExcelVO>();
		String listOfParameters = "";
		
		try
		{
			
			
			SolmrLogger.debug(this, "Creating db-connection in searchParticelleExcelByParameters method in ConduzioneParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in searchParticelleExcelByParameters method in ConduzioneParticellaDAO and it values: " + conn
					+ "\n");
			
			// Prima parte della query: viene eseguita solo se l'utente non sceglie
			// di visualizzare solo le particella senza uso del suolo specificato
			String query = "" +
					"WITH PARTICELLE AS " +
          "  (SELECT DISTINCT PC.ID_PARTICELLA_CERTIFICATA," +
          "                   PC.ID_PARTICELLA " +
          "   FROM   DB_UTE U,  " +
          "          DB_CONDUZIONE_PARTICELLA CP, " +
          "          DB_PARTICELLA_CERTIFICATA PC " +
          "   WHERE  U.ID_AZIENDA = ?  " +
          "   AND    U.DATA_FINE_ATTIVITA IS NULL " + 
          "   AND    U.ID_UTE = CP.ID_UTE " +
          "   AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
          "   AND    CP.ID_PARTICELLA = PC.ID_PARTICELLA " +
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
          "                                       FROM   DB_ESITO_PASCOLO_MAGRO EPM " +
          "                                       WHERE EPM.ID_PARTICELLA_CERTIFICATA = PM.ID_PARTICELLA_CERTIFICATA) " + 
          "            AND    PM.ID_FONTE = TF.ID_FONTE) " +
          "     GROUP BY ID_PARTICELLA_CERTIFICATA ), " +
          "    ISTANZA_RIESAME AS " +
          "                (SELECT IR.DATA_RICHIESTA, " +
          "                        IR.DATA_EVASIONE, " +                
          "                        IR.ID_PARTICELLA " +
          "                 FROM   DB_ISTANZA_RIESAME IR " +
          "                        WHERE IR.DATA_RICHIESTA = ( " +
          "                                                   SELECT MAX(IRTMP.DATA_RICHIESTA) " +
          "                                                   FROM   DB_ISTANZA_RIESAME IRTMP " +                                  
          "                                                   WHERE  IRTMP.ID_AZIENDA = IR.ID_AZIENDA " +
          "                                                   AND    IRTMP.ID_PARTICELLA = IR.ID_PARTICELLA " +
          "                                                   AND    IRTMP.DATA_ANNULLAMENTO IS NULL " +
          "                                                  ) " +
          "                 AND   IR.ID_AZIENDA = ? " +
          "                ) ";			
			
			if (filtriParticellareRicercaVO.getIdUtilizzo().intValue() != 0)
			{
				query += 
				  " SELECT CP.ID_CONDUZIONE_PARTICELLA, " + 
				  "        CP.ESITO_CONTROLLO, " +
				  "        CP.ID_PARTICELLA, " + 
				  "        SP.COMUNE, "	+
				  "        C.DESCOM, " +
				  "        P.SIGLA_PROVINCIA, " +
				  "        SP.SEZIONE, " +
				  "        SP.FOGLIO, " +
				  "        SP.PARTICELLA, " +
				  "        SP.SUBALTERNO, " +
				  "        SP.SUP_CATASTALE, " +
				  "        SP.SUPERFICIE_GRAFICA, " +
				  "        TZA.DESCRIZIONE AS DESC_ZONA_ALT, " +
				//  "        TAM.DESCRIZIONE AS DESC_AREA_M, " +
				 // "        SP.ID_AREA_C, " +
				  "        SP.FLAG_IRRIGABILE, " +
				  "        CP.ID_TITOLO_POSSESSO, " +
				  "        TTP.DESCRIZIONE AS DESC_POSSESSO, " +
				  "        CP.SUPERFICIE_CONDOTTA, " +
				  "        CP.PERCENTUALE_POSSESSO, " +
				  "        CP.DATA_INIZIO_CONDUZIONE, " +
				  "        CP.DATA_FINE_CONDUZIONE, "	+
				  "        CP.RECORD_MODIFICATO, " +
				  "        CP.SUPERFICIE_AGRONOMICA, " +
				  "        UP.ID_UTILIZZO_PARTICELLA, "	+
				  "        RCM.ID_CATALOGO_MATRICE, " +
				  "        TU.CODICE AS COD_PRIMARIO, " +
				  "        TU.DESCRIZIONE AS DESC_PRIMARIO, " +
				  "        TV.DESCRIZIONE AS DESC_VARIETA, " +
				  "        TV.CODICE_VARIETA AS COD_VARIETA, " +
				  "        UP.SUPERFICIE_UTILIZZATA, " +
				  "        RCM2.ID_UTILIZZO AS ID_UTILIZZO_SECONDARIO, "	+
				  "        TU2.CODICE AS COD_SECONDARIO, " +
				  "        TU2.DESCRIZIONE AS DESC_SECONDARIO, " +
				  "        TV2.DESCRIZIONE AS VAR_SECONDARIA, " +
				  "        TV2.CODICE_VARIETA AS COD_VAR_SECONDARIA, " +
				  "        UP.SUP_UTILIZZATA_SECONDARIA, " +
				  "        SP.ID_CASO_PARTICOLARE, " +
				  "        TCP.DESCRIZIONE AS DESC_PARTICOLARE, "	+
				  "        CP.ID_UTE, " +
				  "        UP.ANNO_IMPIANTO, " +
				  "        UP.NUMERO_PIANTE_CEPPI, " +
				  "        U.COMUNE AS COM_UTE, "	+
				  "        C2.DESCOM AS DESC_COM_UTE, " +
				  "        F.ID_FOGLIO, " +
			//	  "        F.ID_AREA_E, " +
				  "        PC.SUP_SEMINABILE, "	+
				  "        PC.SUP_COLT_ARBOREA_SPECIALIZZATA, " +
				  "        PC.SUP_USO_GRAFICA, " +
				  "        PC.ID_PARTICELLA_CERTIFICATA, " +
				  "        RCM.ID_VARIETA, " +
				  "        AA.CUAA, " +
				  "        AA.DENOMINAZIONE, " +
				  "        BIOLOGICO.SUP_BIOLOGICO, " +
          "        BIOLOGICO.SUP_CONVENZIONALE, " +
          "        BIOLOGICO.SUP_IN_CONVERSIONE, " +
          "        BIOLOGICO.DATA_INIZIO_CONVERSIONE, " +
          "        BIOLOGICO.DATA_FINE_CONVERSIONE, " +
          "        PAS.DESCRIZIONE AS DESC_FONTE_REG_PASCOLI, " +
          "        ISR.DATA_RICHIESTA, " +
          "        ISR.DATA_EVASIONE, " +
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
          "        UP.DATA_INIZIO_DESTINAZIONE, " +
          "        UP.DATA_FINE_DESTINAZIONE, " +
          "        UP.DATA_INIZIO_DESTINAZIONE_SEC, " +
          "        UP.DATA_FINE_DESTINAZIONE_SEC, " +
          "        UP.ID_TIPO_EFA, " +
          "        TEF.DESCRIZIONE_TIPO_EFA, " +
          "        UM.DESCRIZIONE AS DESC_UNITA_MIS_EFA, " +
          "        UP.VALORE_ORIGINALE, " +
          "        UP.VALORE_DOPO_CONVERSIONE, " +
          "        UP.VALORE_DOPO_PONDERAZIONE, " +          
          "        (CASE " +
          "         WHEN EXISTS (SELECT NE.ID_NOTIFICA_ENTITA " + 
          "                      FROM   DB_NOTIFICA_ENTITA NE, " +
          "                             DB_NOTIFICA NO, " +
          "                             DB_TIPO_ENTITA TE " +
          "                      WHERE  TE.CODICE_TIPO_ENTITA = 'P' " +
          "                      AND    TE.ID_TIPO_ENTITA = NE.ID_TIPO_ENTITA " +
          "                      AND    NE.IDENTIFICATIVO = CP.ID_PARTICELLA " +
          "                      AND    NE.ID_NOTIFICA = NO.ID_NOTIFICA " +
          "                      AND    NO.ID_AZIENDA = ? ) THEN " +
          "                   'NOTIFICA' " +
          "         END ) AS IN_NOTIFICA, " +
          "        NVL(PCK_SMRGAA_LIBRERIA.SelTotSupElegByPartEVetCat(PC.ID_PARTICELLA_CERTIFICATA, RCM.ID_CATALOGO_MATRICE),0) AS SUP_ELEGGIBILE, " +
          "        NVL(PCK_SMRGAA_LIBRERIA.SelTotSupElegByPartEVetVarNTar(PC.ID_PARTICELLA_CERTIFICATA, RCM.ID_CATALOGO_MATRICE),0) AS SUP_ELEGGIBILE_NETTA ";
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
                "     AND    TVA1.DATA_FINE_VALIDITA IS NULL ) " +
                "     AS DESC_TIPO_VALORE_AREA"+t+"   ";          
            }
            else
            {
              query += 
                  "  , (SELECT TVA1.DESCRIZIONE " +
                  "     FROM   DB_R_PARTICELLA_AREA  RPA1, " +
                  "            DB_TIPO_VALORE_AREA TVA1 " +
                  "     WHERE  RPA1.ID_PARTICELLA = CP.ID_PARTICELLA " +
                  "     AND    RPA1.ID_TIPO_VALORE_AREA = TVA1.ID_TIPO_VALORE_AREA " + 
                  "     AND    TVA1.ID_TIPO_AREA = "+tipoAreaVO.getIdTipoArea()+" " +
                  "     AND    TVA1.DATA_FINE_VALIDITA IS NULL ) " +
                  "     AS DESC_TIPO_VALORE_AREA"+t+"   ";      
            }
            
            
          }
        }
				
				
        query += 	
        	" FROM   DB_UTE U, " +
				  "        DB_STORICO_PARTICELLA SP, " +
				  "        DB_CONDUZIONE_PARTICELLA CP, " +
				  "        COMUNE C, " +
				  "        DB_TIPO_TITOLO_POSSESSO TTP, " +
				  "        DB_UTILIZZO_PARTICELLA UP, " +
				  "        DB_R_CATALOGO_MATRICE RCM, "	+
				  "        DB_R_CATALOGO_MATRICE RCM2, " +
				  "        DB_TIPO_UTILIZZO TU, " +
				  "        DB_TIPO_VARIETA TV, " +
				  "        DB_TIPO_UTILIZZO TU2, " +
				  "        DB_TIPO_VARIETA TV2, "	+
				  "        DB_TIPO_DETTAGLIO_USO TDU, " +
          "        DB_TIPO_DETTAGLIO_USO TDU2, " +
          "        DB_TIPO_DESTINAZIONE TDE, " +
          "        DB_TIPO_DESTINAZIONE TDE2, " +
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
		//		  "        DB_TIPO_AREA_M TAM, " +
				  "        PROVINCIA P, " +
				  "        DB_TIPO_CASO_PARTICOLARE TCP, " +
				  "        COMUNE C2, " +
				  "        DB_FOGLIO F, "	+
				  "        DB_PARTICELLA_CERTIFICATA PC, " +
				  "        DB_TIPO_ZONA_ALTIMETRICA TZA, " +
				  "        DB_ANAGRAFICA_AZIENDA AA," +
				  "        (SELECT SUP_BIOLOGICO, SUP_CONVENZIONALE, SUP_IN_CONVERSIONE, " +
          "                DATA_INIZIO_CONVERSIONE, DATA_FINE_CONVERSIONE, ID_PARTICELLA " +
          "         FROM   DB_PARTICELLA_BIO " +
          "         WHERE  ID_AZIENDA = ? " +
          "         AND    DATA_FINE_VALIDITA IS NULL ) BIOLOGICO, " +
          "        PASCOLO_MAGRO PAS, " +
          "        ISTANZA_RIESAME ISR ";
			
				
				if (filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S)
            || filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S))
        {
          query += "" +
              "    ,DB_DICHIARAZIONE_CONSISTENZA DICH_CONS, " +
              "     DB_CONDUZIONE_DICHIARATA CD ";
        }
				
			  // Metto in join DB_DICHIARAZIONE_SEGNALAZIONE solo se l'utente
        // ha specificato un determinato controllo effettuato sulle particelle
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo()))
        {
          query += "   ,DB_DICHIARAZIONE_SEGNALAZIONE DS ";
        }
				
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdMacroUso()))
        {
				  query += "" +
				  		"   ,DB_TIPO_MACRO_USO TMU, " +
              "    DB_TIPO_MACRO_USO_VARIETA TMUV ";
        }
				
			  //Tabelle per la ricerca sui documenti
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()) 
            || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
        {
          query += "" +
              "   ,DB_DOCUMENTO_CONDUZIONE DOCC, " +
              "    DB_DOCUMENTO DOC, " +
              "    DB_TIPO_DOCUMENTO TDOC," +
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
				
				query +="" +
					" WHERE  U.ID_AZIENDA = ? " +
				  " AND    U.ID_AZIENDA = AA.ID_AZIENDA " +
				  " AND    AA.DATA_FINE_VALIDITA IS NULL " +
				  //" AND    U.DATA_FINE_ATTIVITA IS NULL " +
				  " AND    U.ID_UTE = CP.ID_UTE " +
				  " AND    U.COMUNE = C2.ISTAT_COMUNE ";
				// Estraggo le conduzione attive solo se il piano di riferimento è
				// "in lavorazione(solo conduzioni attive)"
				if (filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() == -1)
				{
					query += " AND    U.DATA_FINE_ATTIVITA IS NULL " +
					    "   AND CP.DATA_FINE_CONDUZIONE IS NULL ";
				}
				query += 
				  " AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
				  " AND    SP.DATA_FINE_VALIDITA IS NULL " +
				  " AND    SP.COMUNE = C.ISTAT_COMUNE "	+
				  " AND    SP.ID_CASO_PARTICOLARE = TCP.ID_CASO_PARTICOLARE(+) " +
				  " AND    SP.ID_PARTICELLA = PC.ID_PARTICELLA(+) " +
				  " AND    PC.DATA_FINE_VALIDITA(+) IS NULL " +
				  " AND    SP.ID_ZONA_ALTIMETRICA = TZA.ID_ZONA_ALTIMETRICA(+) " +
				  " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) " +
				  " AND    CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO "	+
				  " AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA " +
				  " AND    UP.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE (+) " +
				  " AND    UP.ID_CATALOGO_MATRICE_SECONDARIO = RCM2.ID_CATALOGO_MATRICE (+) " +
				  " AND    RCM.ID_UTILIZZO = TU.ID_UTILIZZO (+) "	+
				  " AND    RCM.ID_VARIETA = TV.ID_VARIETA (+) " +
				  " AND    RCM.ID_TIPO_DESTINAZIONE = TDE.ID_TIPO_DESTINAZIONE (+) " +
				  " AND    RCM.ID_TIPO_QUALITA_USO = TQU.ID_TIPO_QUALITA_USO (+) " +
				  " AND    RCM2.ID_UTILIZZO = TU2.ID_UTILIZZO(+) "	+
				  " AND    RCM2.ID_VARIETA = TV2.ID_VARIETA(+) " +
				  " AND    RCM.ID_TIPO_DETTAGLIO_USO = TDU.ID_TIPO_DETTAGLIO_USO(+) " +
          " AND    RCM2.ID_TIPO_DETTAGLIO_USO = TDU2.ID_TIPO_DETTAGLIO_USO(+) " +
          " AND    RCM2.ID_TIPO_DESTINAZIONE = TDE2.ID_TIPO_DESTINAZIONE (+) " +
          " AND    RCM2.ID_TIPO_QUALITA_USO = TQU2.ID_TIPO_QUALITA_USO (+) " +
          " AND    UP.ID_TIPO_PERIODO_SEMINA = TPS.ID_TIPO_PERIODO_SEMINA(+) " +
          " AND    UP.ID_TIPO_PERIODO_SEMINA_SECOND = TPS2.ID_TIPO_PERIODO_SEMINA(+) " +
          " AND    UP.ID_SEMINA = TSS.ID_SEMINA(+) " +
          " AND    UP.ID_SEMINA_SECONDARIA = TSS2.ID_SEMINA(+) " +
          " AND    UP.ID_PRATICA_MANTENIMENTO = TPM.ID_PRATICA_MANTENIMENTO (+) " +
          " AND    UP.ID_FASE_ALLEVAMENTO = TFA.ID_FASE_ALLEVAMENTO (+) " +
          " AND    UP.ID_TIPO_EFA = TEF.ID_TIPO_EFA(+) " +
          " AND    TEF.ID_UNITA_MISURA = UM.ID_UNITA_MISURA(+) " +
				//  " AND    SP.ID_AREA_M = TAM.ID_AREA_M(+) " +
				  " AND    CP.ID_PARTICELLA = BIOLOGICO.ID_PARTICELLA(+) " +
				  " AND    PC.ID_PARTICELLA_CERTIFICATA = PAS.ID_PARTICELLA_CERTIFICATA(+) " +
				  " AND    SP.ID_PARTICELLA = ISR.ID_PARTICELLA (+) ";
				
				
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdMacroUso()))
        {
          query += "" +
          		" AND RCM.ID_CATALOGO_MATRICE = TMUV.ID_CATALOGO_MATRICE " +
              " AND TMUV.DATA_FINE_VALIDITA IS NULL " +
              " AND TMUV.ID_MACRO_USO = TMU.ID_MACRO_USO " +
              " AND TMU.ID_MACRO_USO = ? ";
        }
				
				// Se l'utente ha indicato l'ute di riferimento
				if (filtriParticellareRicercaVO.getIdUte() != null)
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
          query += " AND CP.ID_CONDUZIONE_PARTICELLA = DOCC.ID_CONDUZIONE_PARTICELLA " +
                   " AND DOCC.ID_DOCUMENTO = DOC.ID_DOCUMENTO " +
                   " AND DOC.EXT_ID_DOCUMENTO = TDOC.ID_DOCUMENTO " +
                   " AND TDOC.ID_DOCUMENTO = DOCCAT.ID_DOCUMENTO " +
                   " AND DOCCAT.ID_CATEGORIA_DOCUMENTO = ? " +
                   " AND DOC.ID_STATO_DOCUMENTO IS NULL " +
                   " AND NVL(DOC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > SYSDATE ";
          
          if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
          {
            query += " AND TDOC.ID_DOCUMENTO = ? ";
          }
          
          if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
          {
            query += " AND DOC.ID_DOCUMENTO = ? ";
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
                     "               AND    NO.ID_AZIENDA = U.ID_AZIENDA ";
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
                       "                                    AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA )) )";
          }
          else
          {
            query +=   " AND    NE.DATA_FINE_VALIDITA IS NULL )";
          }
          
        }
        
				// Se l'utente ha indicato il comune di riferimento
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIstatComune()))
				{
					query += " AND SP.COMUNE = ? ";
				}
				// Se l'utente ha indicato la sezione di riferimento
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSezione()))
				{
					query += " AND SP.SEZIONE = ? ";
				}
				// Se l'utente ha indicato il foglio di riferimento
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getFoglio()))
				{
					query += " AND SP.FOGLIO = ? ";
				}
				// Se l'utente ha indicato la particella di riferimento
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getParticella()))
				{
					query += " AND SP.PARTICELLA = ? ";
				}
				// Se l'utente ha indicato il subalterno di riferimento
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSubalterno()))
				{
					query += " AND SP.SUBALTERNO = ? ";
				}
        // Se l'utente ha indicato il tipo Zona Altimetrica
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdZonaAltimetrica()))
        {
          query += " AND SP.ID_ZONA_ALTIMETRICA = ? ";
        }
        // Se l'utente ha indicato il tipo Caso Particolare
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCasoParticolare()))
        {
          query += " AND SP.ID_CASO_PARTICOLARE = ? ";
        }
				// Se l'utente invece ha specificato un particolare uso del suolo
				if (filtriParticellareRicercaVO.getIdUtilizzo().longValue() > 0)
				{
					// Se non è stato indicato nei filtri di ricerca se l'utilizzo
					// selezionato
					// è primario o secondario, viene usata come condizione di default
					// quella
					// dell'utilizzo primario
					if (!Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario())
							&& !Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario()))
					{
						query += " AND RCM.ID_UTILIZZO = ? ";
					}
					else
					{
						// Uso Primario
						if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario()))
						{
							query += " AND RCM.ID_UTILIZZO = ? ";
						}
						// Uso Secondario
						if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario()))
						{
							query += " AND RCM2.ID_UTILIZZO = ? ";
						}
					}
					
				}
				//Tipo EFA
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoEfa()))
        {
          query += " AND UP.ID_TIPO_EFA = ? ";
        }
				// Se l'utente ha specificato un particolare titolo di possesso
				if (filtriParticellareRicercaVO.getIdTitoloPossesso() != null)
				{
					query += " AND CP.ID_TITOLO_POSSESSO = ? ";
				}
				else
				{
					// Se l'utente ha scelto l'opzione escludi asservimento
					if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckEscludiAsservimento())
							&& filtriParticellareRicercaVO.getCheckEscludiAsservimento().equalsIgnoreCase(SolmrConstants.FLAG_S))
					{
						query += " AND CP.ID_TITOLO_POSSESSO <> " + SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
					}
					// Se l'utente ha scelto l'opzione escludi conferimento
					if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckEscludiConferimento())
							&& filtriParticellareRicercaVO.getCheckEscludiConferimento().equalsIgnoreCase(SolmrConstants.FLAG_S))
					{
						query += " AND CP.ID_TITOLO_POSSESSO <> " + SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO;
					}
				}
				// Se l'utente ha specificato la tipologia di anomalia bloccante
				boolean isFirst = true;
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneBloccante()))
				{
					query += " AND (CP.ESITO_CONTROLLO = ? ";
					isFirst = false;
				}
				// Se l'utente ha specificato la tipologia di anomalia warning
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneWarning()))
				{
					if (!isFirst)
					{
						query += " OR ";
					}
					else
					{
						query += " AND (";
					}
					query += " CP.ESITO_CONTROLLO = ? ";
					isFirst = false;
				}
				// Se l'utente ha specificato la tipologia di anomalia OK
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneOk()))
				{
					if (!isFirst)
					{
						query += " OR ";
					}
					else
					{
						query += " AND (";
					}
					query += " CP.ESITO_CONTROLLO = ? ";
					isFirst = false;
				}
				if (!isFirst)
				{
					query += ")";
				}
				
			  // Se l'utente ha specificato un determinato tipo di controllo
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo()))
        {
          query += " " +
              "AND DS.ID_AZIENDA = ? " +
              " AND DS.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
              " AND DS.ID_DICHIARAZIONE_CONSISTENZA IS NULL " +
              " AND DS.ID_CONTROLLO = ? ";
        }
				
			  // Ricerco i dati delle particelle solo asservite
        if (filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S))
        {
          query += "" +
          		" AND DICH_CONS.ID_AZIENDA <> ? " +
              " AND DICH_CONS.DATA_INSERIMENTO_DICHIARAZIONE = " +
              "     (SELECT MAX(DATA_INSERIMENTO_DICHIARAZIONE) " +
              "      FROM   DB_DICHIARAZIONE_CONSISTENZA DC1 " +
              "      WHERE  DICH_CONS.ID_AZIENDA = DC1.ID_AZIENDA" + "     ) " +
              " AND DICH_CONS.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
              " AND CD.ID_TITOLO_POSSESSO = ? " +
              " AND CD.ID_PARTICELLA = SP.ID_PARTICELLA ";
        }
        // Ricerco i dati delle particelle solo conferite
        if (filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S))
        {
          query += " AND DICH_CONS.ID_AZIENDA <> ? "
              + " AND DICH_CONS.DATA_INSERIMENTO_DICHIARAZIONE = (SELECT MAX(DATA_INSERIMENTO_DICHIARAZIONE) FROM DB_DICHIARAZIONE_CONSISTENZA DC1 WHERE DICH_CONS.ID_AZIENDA = DC1.ID_AZIENDA) "
              + " AND DICH_CONS.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " + " AND CD.ID_TITOLO_POSSESSO = ? "
              + " AND CD.ID_PARTICELLA = SP.ID_PARTICELLA ";
        }
				
				
				query += " " +
						"GROUP BY CP.ID_CONDUZIONE_PARTICELLA, " +
						"         CP.ESITO_CONTROLLO, " +
						"         CP.ID_PARTICELLA, " + 
						"         SP.COMUNE, " +
						"         C.DESCOM, " +
						"         P.SIGLA_PROVINCIA, " +
						"         SP.SEZIONE, " +
						"         SP.FOGLIO, " +
						"         SP.PARTICELLA, " +
						"         SP.SUBALTERNO, " +
						"         SP.SUP_CATASTALE, " +
						"         SP.SUPERFICIE_GRAFICA, " +
						"         TZA.DESCRIZIONE, " +
			//			"         TAM.DESCRIZIONE, " +
			//			"         SP.ID_AREA_C, "	+ 
						"         SP.FLAG_IRRIGABILE, " + 
						"         CP.ID_TITOLO_POSSESSO, " + 
						"         TTP.DESCRIZIONE, "	+ 
						"         CP.SUPERFICIE_CONDOTTA, " +
						"         CP.PERCENTUALE_POSSESSO, " + 
						"         CP.DATA_INIZIO_CONDUZIONE, " + 
						"         CP.DATA_FINE_CONDUZIONE, "	+ 
						"         CP.RECORD_MODIFICATO, " + 
						"         CP.SUPERFICIE_AGRONOMICA, " + 
						"         UP.ID_UTILIZZO_PARTICELLA, " +
						"         RCM.ID_CATALOGO_MATRICE, " +
						"         TU.CODICE, " +
						"         TU.DESCRIZIONE, " +
						"         TV.DESCRIZIONE, " +
						"         TV.CODICE_VARIETA, " +
						"         UP.SUPERFICIE_UTILIZZATA, " +
						"         RCM2.ID_UTILIZZO, " +
						"         TU2.CODICE, "	+ 
						"         TU2.DESCRIZIONE, " +
						"         TV2.DESCRIZIONE, " +
						"         TV2.CODICE_VARIETA, "	+
						"         UP.SUP_UTILIZZATA_SECONDARIA, " +
						"         SP.ID_CASO_PARTICOLARE, " +
						"         TCP.DESCRIZIONE, " +
						"         CP.ID_UTE, " +
						"         UP.ANNO_IMPIANTO, " +
						"         UP.NUMERO_PIANTE_CEPPI, " +
						"         U.COMUNE, "	+
						"         C2.DESCOM, " +
						"         F.ID_FOGLIO, " +
		//				"         F.ID_AREA_E, " +
						"         PC.SUP_SEMINABILE, " +
						"         PC.SUP_COLT_ARBOREA_SPECIALIZZATA, " +
						"         PC.SUP_USO_GRAFICA, " +
						"         PC.ID_PARTICELLA_CERTIFICATA, " +
						"         RCM.ID_VARIETA, " +
						"         AA.CUAA, " +
						"         AA.DENOMINAZIONE," +
						"         BIOLOGICO.SUP_BIOLOGICO, " +
            "         BIOLOGICO.SUP_CONVENZIONALE, " +
            "         BIOLOGICO.SUP_IN_CONVERSIONE, " +
            "         BIOLOGICO.DATA_INIZIO_CONVERSIONE, " +
            "         BIOLOGICO.DATA_FINE_CONVERSIONE, " +
            "         PAS.DESCRIZIONE, " +
            "         SP.ID_PARTICELLA, " +
            "         ISR.DATA_RICHIESTA, " +
            "         ISR.DATA_EVASIONE, " +
            "         TDU.CODICE_DETTAGLIO_USO, " +
            "         TDU.DESCRIZIONE_DETTAGLIO_USO, " +
            "         TDU2.CODICE_DETTAGLIO_USO, " +
            "         TDU2.DESCRIZIONE_DETTAGLIO_USO, " +
            "         TDE.CODICE_DESTINAZIONE, " +
            "         TDE.DESCRIZIONE_DESTINAZIONE, " +
            "         TDE2.CODICE_DESTINAZIONE, " +
            "         TDE2.DESCRIZIONE_DESTINAZIONE, " +
            "         TQU.CODICE_QUALITA_USO, " +
            "         TQU.DESCRIZIONE_QUALITA_USO, " +
            "         TQU2.CODICE_QUALITA_USO, " +
            "         TQU2.DESCRIZIONE_QUALITA_USO, " +
            "         TPS.CODICE, " +
            "         TPS.DESCRIZIONE, " +
            "         TPS2.CODICE, " +
            "         TPS2.DESCRIZIONE, " +
            "         TSS.CODICE_SEMINA, " +
            "         TSS.DESCRIZIONE_SEMINA, " +
            "         TSS2.CODICE_SEMINA, " +
            "         TSS2.DESCRIZIONE_SEMINA, " +
            "         TPM.CODICE_PRATICA_MANTENIMENTO, " +
            "         TPM.DESCRIZIONE_PRATICA_MANTENIMEN, " +
            "         TFA.CODICE_FASE_ALLEVAMENTO, " +
            "         TFA.DESCRIZIONE_FASE_ALLEVAMENTO, " +
            "         UP.DATA_INIZIO_DESTINAZIONE, " +
            "         UP.DATA_FINE_DESTINAZIONE, " +
            "         UP.DATA_INIZIO_DESTINAZIONE_SEC, " +
            "         UP.DATA_FINE_DESTINAZIONE_SEC, " +
            "         UP.ID_TIPO_EFA, " +
            "         TEF.DESCRIZIONE_TIPO_EFA, " +
            "         UM.DESCRIZIONE, " +
            "         UP.VALORE_ORIGINALE, " +
            "         UP.VALORE_DOPO_CONVERSIONE, " +
            "         UP.VALORE_DOPO_PONDERAZIONE ";
			}
			// Metto le query in UNION solo se l'utente sceglie di visualizzare
			// "qualunque uso del suolo"
			if ((filtriParticellareRicercaVO.getIdUtilizzo().intValue() < 0) 
			    && (filtriParticellareRicercaVO.getIdMacroUso() == null)
			    && (filtriParticellareRicercaVO.getIdTipoEfa() == null))
			{
				query += " UNION ";
			}
			// La seconda parte della query viene eseguita solo se l'utente non ha
			// scelto un particolare uso del suolo da visualizzare
			if ((filtriParticellareRicercaVO.getIdUtilizzo().intValue() <= 0) 
			    && (filtriParticellareRicercaVO.getIdMacroUso() == null)
			    && (filtriParticellareRicercaVO.getIdTipoEfa() == null))
			{
				query += " " +
						" SELECT  CP.ID_CONDUZIONE_PARTICELLA, " +
						"         CP.ESITO_CONTROLLO, " +
						"         CP.ID_PARTICELLA, " + 
					  "         SP.COMUNE, " +
						"         C.DESCOM, " +
						"         P.SIGLA_PROVINCIA, " +
						"         SP.SEZIONE, " +
						"         SP.FOGLIO, " +
						"         SP.PARTICELLA, " +
						"         SP.SUBALTERNO, " +
						"         SP.SUP_CATASTALE, "	+
						"         SP.SUPERFICIE_GRAFICA, " +
						"         TZA.DESCRIZIONE AS DESC_ZONA_ALT, " +
			//			"         TAM.DESCRIZIONE AS DESC_AREA_M, " +
			//			"         SP.ID_AREA_C, " +
						"         SP.FLAG_IRRIGABILE, "	+
						"         CP.ID_TITOLO_POSSESSO, " +
						"         TTP.DESCRIZIONE AS DESC_POSSESSO, " +
						"         CP.SUPERFICIE_CONDOTTA, "	+
						"         CP.PERCENTUALE_POSSESSO, " +
						"         CP.DATA_INIZIO_CONDUZIONE, " +
						"         CP.DATA_FINE_CONDUZIONE, " +
						"         CP.RECORD_MODIFICATO, "	+
						"         CP.SUPERFICIE_AGRONOMICA, " +
						"         -1 AS ID_UTILIZZO_PARTICELLA, " +
						"         -1 AS ID_CATALOGO_MATRICE, " +
						"         NULL AS COD_PRIMARIO, "	+
						"         NULL AS DESC_PRIMARIO, " +
						"         NULL AS DESC_VARIETA, " +
						"         NULL AS COD_VAR_SECONDARIA, "	+
						"         DECODE(CP.ID_TITOLO_POSSESSO, 5 , CP.SUPERFICIE_CONDOTTA, " +
						"         TRUNC(DECODE(SP.SUPERFICIE_GRAFICA,0,SP.SUP_CATASTALE,SP.SUPERFICIE_GRAFICA) " +
						"                * CP.PERCENTUALE_POSSESSO / 100,4) " +
            "                - TRUNC(SUM(NVL(UP.SUPERFICIE_UTILIZZATA,0)),4)) " +
						"         SUPERFICIE_UTILIZZATA, " +
						"         -1 AS ID_UTILIZZO_SECONDARIO, " +
						"         NULL AS COD_SECONDARIO, " +
						"         NULL AS DESC_SECONDARIO, " +
						"         NULL AS VAR_SECONDARIA, " +
						"         NULL AS COD_VARIETA, " +
						"         0 AS SUP_UTILIZZATA_SECONDARIA, "	+
						"         SP.ID_CASO_PARTICOLARE, " +
						"         TCP.DESCRIZIONE AS DESC_PARTICOLARE, " +
						"         CP.ID_UTE, " +
						"         0, " +
						"         0, " +
						"         U.COMUNE AS COM_UTE, " +
						"         C2.DESCOM AS DESC_COM_UTE, " +
						"         F.ID_FOGLIO, " +
			//			"         F.ID_AREA_E, " +
						"         PC.SUP_SEMINABILE, " +
						"         PC.SUP_COLT_ARBOREA_SPECIALIZZATA, " +
						"         PC.SUP_USO_GRAFICA, "	+
						"         -1 AS ID_PARTICELLA_CERTIFICATA, " +
						"         -1 AS ID_VARIETA, " +
						"         AA.CUAA, " +
						"         AA.DENOMINAZIONE, " +
						"         BIOLOGICO.SUP_BIOLOGICO, " +
            "         BIOLOGICO.SUP_CONVENZIONALE, " +
            "         BIOLOGICO.SUP_IN_CONVERSIONE, " +
            "         BIOLOGICO.DATA_INIZIO_CONVERSIONE, " +
            "         BIOLOGICO.DATA_FINE_CONVERSIONE, " +
            "         PAS.DESCRIZIONE AS DESC_FONTE_REG_PASCOLI, " +
            "         ISR.DATA_RICHIESTA, " +
            "         ISR.DATA_EVASIONE, " +
            "         NULL AS COD_DETT_USO_PRIM, " +
            "         NULL AS DESC_DETT_USO_PRIM, " +
            "         NULL AS COD_DETT_USO_SEC, " +
            "         NULL AS DESC_DETT_USO_SEC, " +
            "         NULL AS COD_DESTINAZIONE_PRIM, " +
            "         NULL AS DESC_DESTINAZIONE_PRIM, " +
            "         NULL AS COD_DESTINAZIONE_SEC, " +
            "         NULL AS DESC_DESTINAZIONE_SEC, " +
            "         NULL AS COD_QUAL_USO_PRIM, " +
            "         NULL AS DESC_QUAL_USO_PRIM, " +
            "         NULL AS COD_QUAL_USO_SEC, " +
            "         NULL AS DESC_QUAL_USO_SEC, " +
            "         NULL AS COD_PER_SEM_PRIM, " +
            "         NULL AS DESC_PER_SEM_PRIM, " +
            "         NULL AS COD_PER_SEM_SEC, " +
            "         NULL AS DESC_PER_SEM_SEC, " +
            "         NULL AS COD_SEM_PRIM, " +
            "         NULL AS DESC_SEM_PRIM, " +
            "         NULL AS COD_SEM_SEC, " +
            "         NULL AS DESC_SEM_SEC, " +
            "         NULL AS CODICE_PRATICA_MANTENIMENTO, " +
            "         NULL AS DESCRIZIONE_PRATICA_MANTENIMEN, " +
            "         NULL AS CODICE_FASE_ALLEVAMENTO, " +
            "         NULL AS DESCRIZIONE_FASE_ALLEVAMENTO, " +
            "         NULL AS DATA_INIZIO_DESTINAZIONE, " +
            "         NULL AS DATA_FINE_DESTINAZIONE, " +
            "         NULL AS DATA_INIZIO_DESTINAZIONE_SEC, " +
            "         NULL AS DATA_FINE_DESTINAZIONE_SEC, " +
            "         -1 AS ID_TIPO_EFA, " +
            "         NULL AS DESCRIZIONE_TIPO_EFA, " +
            "         NULL AS DESC_UNITA_MIS_EFA, " +
            "         NULL AS VALORE_ORIGINALE, " +
            "         NULL AS VALORE_DOPO_CONVERSIONE, " +
            "         NULL AS VALORE_DOPO_PONDERAZIONE, " +  
            "         (CASE " +
            "          WHEN EXISTS (SELECT NE.ID_NOTIFICA_ENTITA " + 
            "                       FROM   DB_NOTIFICA_ENTITA NE, " +
            "                              DB_NOTIFICA NO, " +
            "                              DB_TIPO_ENTITA TE " +
            "                       WHERE  TE.CODICE_TIPO_ENTITA = 'P' " +
            "                       AND    TE.ID_TIPO_ENTITA = NE.ID_TIPO_ENTITA " +
            "                       AND    NE.IDENTIFICATIVO = CP.ID_PARTICELLA " +
            "                       AND    NE.ID_NOTIFICA = NO.ID_NOTIFICA " +
            "                       AND    NO.ID_AZIENDA = ? ) THEN " +
            "                   'NOTIFICA' " +
            "           END ) AS IN_NOTIFICA, " +
            "         0 AS SUP_ELEGGIBILE, " +
            "         0 AS SUP_ELEGGIBILE_NETTA ";
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
                "     AND    TVA1.DATA_FINE_VALIDITA IS NULL ) " +
                "     AS DESC_TIPO_VALORE_AREA"+t+"   ";          
            }
            else
            {
              query += 
                  "  , (SELECT TVA1.DESCRIZIONE " +
                  "     FROM   DB_R_PARTICELLA_AREA  RPA1, " +
                  "            DB_TIPO_VALORE_AREA TVA1 " +
                  "     WHERE  RPA1.ID_PARTICELLA = CP.ID_PARTICELLA " +
                  "     AND    RPA1.ID_TIPO_VALORE_AREA = TVA1.ID_TIPO_VALORE_AREA " + 
                  "     AND    TVA1.ID_TIPO_AREA = "+tipoAreaVO.getIdTipoArea()+" " +
                  "     AND    TVA1.DATA_FINE_VALIDITA IS NULL ) " +
                  "     AS DESC_TIPO_VALORE_AREA"+t+"   ";      
            }
            
            
          }
        }
          
          query +=
						" FROM    DB_UTE U, " +
						"         DB_STORICO_PARTICELLA SP, " +
						"         DB_CONDUZIONE_PARTICELLA CP, " +
						"         COMUNE C, "	+
						"         DB_TIPO_TITOLO_POSSESSO TTP, " +
						"         DB_UTILIZZO_PARTICELLA UP, " +
						"         PROVINCIA P, " +
						"         DB_TIPO_CASO_PARTICOLARE TCP, " +
						"         COMUNE C2, " +
						"         DB_FOGLIO F, " +
						"         DB_PARTICELLA_CERTIFICATA PC, " +
						"         DB_TIPO_ZONA_ALTIMETRICA TZA, " +
				//		"         DB_TIPO_AREA_M TAM, " +
						"         DB_ANAGRAFICA_AZIENDA AA, " +
						"         (SELECT SUP_BIOLOGICO, SUP_CONVENZIONALE, SUP_IN_CONVERSIONE, " +
		        "                 DATA_INIZIO_CONVERSIONE, DATA_FINE_CONVERSIONE, ID_PARTICELLA " +
		        "          FROM   DB_PARTICELLA_BIO " +
		        "          WHERE  ID_AZIENDA = ? " +
		        "          AND    DATA_FINE_VALIDITA IS NULL ) BIOLOGICO," +
		        "         PASCOLO_MAGRO PAS, " +
		        "         ISTANZA_RIESAME ISR ";
          
            
				// Se è attivo almeno uno dei due filtri tra solo asservite e solo
        // conferite deve andare in join con la
        // DB_DICHIARAZIONE_CONSISTENZA
        if (filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S)
            || filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S))
        {
          query += "" +
              "    ,DB_DICHIARAZIONE_CONSISTENZA DICH_CONS, " +
              "     DB_CONDUZIONE_DICHIARATA CD ";
        }
        
        // Metto in join DB_DICHIARAZIONE_SEGNALAZIONE solo se l'utente
        // ha specificato un determinato controllo effettuato sulle particelle
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo()))
        {
          query += "" +
          		"    ,DB_DICHIARAZIONE_SEGNALAZIONE DS ";
        }
        
        //Tabelle per la ricerca sui documenti
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()) 
            || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
        {
          query += "" +
              "   ,DB_DOCUMENTO_CONDUZIONE DOCC, " +
              "    DB_DOCUMENTO DOC, DB_TIPO_DOCUMENTO TDOC, " +
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
				
        query += "" +
        		" WHERE   U.ID_AZIENDA = ? " +
						" AND     U.ID_AZIENDA = AA.ID_AZIENDA " +
						" AND     AA.DATA_FINE_VALIDITA IS NULL "	+
						//" AND     U.DATA_FINE_ATTIVITA IS NULL " +
						" AND     U.ID_UTE = CP.ID_UTE ";
				// Estraggo le conduzione attive solo se il piano di riferimento è
				// "in lavorazione(solo conduzioni attive)"
				if (filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() == -1)
				{
					query += " AND     U.DATA_FINE_ATTIVITA IS NULL " +
						" AND CP.DATA_FINE_CONDUZIONE IS NULL ";
				}
				query += "" +
						" AND    U.COMUNE = C2.ISTAT_COMUNE " +
				    " AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
				    " AND    SP.DATA_FINE_VALIDITA IS NULL " +
				    " AND    SP.COMUNE = C.ISTAT_COMUNE " +
				    " AND    SP.ID_CASO_PARTICOLARE = TCP.ID_CASO_PARTICOLARE(+) " +
				    " AND    SP.COMUNE = F.COMUNE(+) " +
				    " AND    NVL( SP.SEZIONE,'-') = NVL( F.SEZIONE(+),'-') " +
				    " AND    SP.FOGLIO = F.FOGLIO(+) " +
				    " AND    SP.ID_PARTICELLA = PC.ID_PARTICELLA(+) " +
				    " AND    PC.DATA_FINE_VALIDITA(+) IS NULL " +
				    " AND    SP.ID_ZONA_ALTIMETRICA = TZA.ID_ZONA_ALTIMETRICA(+) " +
			//	    " AND    SP.ID_AREA_M = TAM.ID_AREA_M(+) " +
				    " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) " +
				    " AND    CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO "	+
				    " AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA(+) " +
				    " AND    CP.ID_PARTICELLA = BIOLOGICO.ID_PARTICELLA(+) " +
				    " AND    PC.ID_PARTICELLA_CERTIFICATA = PAS.ID_PARTICELLA_CERTIFICATA(+) " +
            " AND    SP.ID_PARTICELLA = ISR.ID_PARTICELLA (+) ";
				
				
			  
				
				
				
				// Se l'utente ha indicato l'ute di riferimento
				if (filtriParticellareRicercaVO.getIdUte() != null)
				{
					query += " AND U.ID_UTE = ? ";
				}
				
			  
        
        query +="" +
            " AND    SP.COMUNE = F.COMUNE(+) " +
            " AND    NVL( SP.SEZIONE,'-') = NVL( F.SEZIONE(+),'-') " +
            " AND    SP.FOGLIO = F.FOGLIO(+) ";
        
        
        
        //Combo documenti
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()) 
            || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento())
            || Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
        {
          query += " AND CP.ID_CONDUZIONE_PARTICELLA = DOCC.ID_CONDUZIONE_PARTICELLA " +
                   " AND DOCC.ID_DOCUMENTO = DOC.ID_DOCUMENTO " +
                   " AND DOC.EXT_ID_DOCUMENTO = TDOC.ID_DOCUMENTO " +
                   " AND TDOC.ID_DOCUMENTO = DOCCAT.ID_DOCUMENTO " +
                   " AND DOCCAT.ID_CATEGORIA_DOCUMENTO = ? " +
                   " AND DOC.ID_STATO_DOCUMENTO IS NULL " +
                   " AND NVL(DOC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > SYSDATE ";
          
          if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
          {
            query += " AND TDOC.ID_DOCUMENTO = ? ";
          }
          
          if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
          {
            query += " AND DOC.ID_DOCUMENTO = ? ";
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
                     "               AND    NO.ID_AZIENDA = U.ID_AZIENDA ";
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
                       "                                    AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA )) )";
          }
          else
          {
            query +=   " AND    NE.DATA_FINE_VALIDITA IS NULL )";
          }
          
        }
        
        
				// Se l'utente ha indicato il comune di riferimento
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIstatComune()))
				{
					query += " AND SP.COMUNE = ? ";
				}
				// Se l'utente ha indicato la sezione di riferimento
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSezione()))
				{
					query += " AND SP.SEZIONE = ? ";
				}
				// Se l'utente ha indicato il foglio di riferimento
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getFoglio()))
				{
					query += " AND SP.FOGLIO = ? ";
				}
				// Se l'utente ha indicato la particella di riferimento
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getParticella()))
				{
					query += " AND SP.PARTICELLA = ? ";
				}
				// Se l'utente ha indicato il subalterno di riferimento
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSubalterno()))
				{
					query += " AND SP.SUBALTERNO = ? ";
				}
				
        // Se l'utente ha indicato il tipo Zona Altimetrica
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdZonaAltimetrica()))
        {
          query += " AND SP.ID_ZONA_ALTIMETRICA = ? ";
        }
        // Se l'utente ha indicato il tipo Caso Particolare
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCasoParticolare()))
        {
          query += " AND SP.ID_CASO_PARTICOLARE = ? ";
        }
				// Se l'utente ha specificato un particolare titolo di possesso
				if (filtriParticellareRicercaVO.getIdTitoloPossesso() != null)
				{
					query += " AND CP.ID_TITOLO_POSSESSO = ? ";
				}
				else
				{
					// Se l'utente ha scelto l'opzione escludi asservimento
					if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckEscludiAsservimento())
							&& filtriParticellareRicercaVO.getCheckEscludiAsservimento().equalsIgnoreCase(SolmrConstants.FLAG_S))
					{
						query += " AND CP.ID_TITOLO_POSSESSO <> " + SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
					}
					// Se l'utente ha scelto l'opzione escludi conferimento
					if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckEscludiConferimento())
							&& filtriParticellareRicercaVO.getCheckEscludiConferimento().equalsIgnoreCase(SolmrConstants.FLAG_S))
					{
						query += " AND CP.ID_TITOLO_POSSESSO <> " + SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO;
					}
				}
				// Se l'utente ha specificato la tipologia di anomalia bloccante
				boolean isFirst = true;
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneBloccante()))
				{
					query += " AND (CP.ESITO_CONTROLLO = ? ";
					isFirst = false;
				}
				// Se l'utente ha specificato la tipologia di anomalia warning
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneWarning()))
				{
					if (!isFirst)
					{
						query += " OR ";
					}
					else
					{
						query += " AND (";
					}
					query += " CP.ESITO_CONTROLLO = ? ";
					isFirst = false;
				}
				// Se l'utente ha specificato la tipologia di anomalia OK
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneOk()))
				{
					if (!isFirst)
					{
						query += " OR ";
					}
					else
					{
						query += " AND (";
					}
					query += " CP.ESITO_CONTROLLO = ? ";
					isFirst = false;
				}
				if (!isFirst)
				{
					query += ")";
				}
				
			  // Se l'utente ha specificato un determinato tipo di controllo
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo()))
        {
          query += " " +
              " AND DS.ID_AZIENDA = ? " +
              " AND DS.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
              " AND DS.ID_DICHIARAZIONE_CONSISTENZA IS NULL " +
              " AND DS.ID_CONTROLLO = ? ";
        }
				
			  // Ricerco i dati della particella certificata solo se vengono
        // richieste le particelle solo asservite
        if (filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S))
        {
          query += " AND DICH_CONS.ID_AZIENDA <> ? "
              + " AND DICH_CONS.DATA_INSERIMENTO_DICHIARAZIONE = (SELECT MAX(DATA_INSERIMENTO_DICHIARAZIONE) FROM DB_DICHIARAZIONE_CONSISTENZA DC1 WHERE DICH_CONS.ID_AZIENDA = DC1.ID_AZIENDA) "
              + " AND DICH_CONS.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " + " AND CD.ID_TITOLO_POSSESSO = ? "
              + " AND CD.ID_PARTICELLA = SP.ID_PARTICELLA ";
        }
        // Ricerco i dati della particella certificata solo se vengono
        // richieste le particelle solo conferite
        if (filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S))
        {
          query += " AND DICH_CONS.ID_AZIENDA <> ? "
              + " AND DICH_CONS.DATA_INSERIMENTO_DICHIARAZIONE = (SELECT MAX(DATA_INSERIMENTO_DICHIARAZIONE) FROM DB_DICHIARAZIONE_CONSISTENZA DC1 WHERE DICH_CONS.ID_AZIENDA = DC1.ID_AZIENDA) "
              + " AND DICH_CONS.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " + " AND CD.ID_TITOLO_POSSESSO = ? "
              + " AND CD.ID_PARTICELLA = SP.ID_PARTICELLA ";
        }
        
				query += " " +
						"GROUP BY CP.ID_CONDUZIONE_PARTICELLA, " +
						"         CP.ESITO_CONTROLLO, " +
						"         CP.ID_PARTICELLA, " + 
						"	        SP.COMUNE, " +
						"         C.DESCOM, " +
						"         P.SIGLA_PROVINCIA, " +
						"         SP.SEZIONE, " +
						"         SP.FOGLIO, " +
						"         SP.PARTICELLA, " +
						"         SP.SUBALTERNO, " +
						"         SP.SUP_CATASTALE, " +
						"         SP.SUPERFICIE_GRAFICA, " +
						"         TZA.DESCRIZIONE, " +
				//		"         TAM.DESCRIZIONE, " +
				//		"         SP.ID_AREA_C, "	+
						"         SP.FLAG_IRRIGABILE, " +
						"         CP.ID_TITOLO_POSSESSO, " +
						"         TTP.DESCRIZIONE, " +
						"         CP.SUPERFICIE_CONDOTTA, " +
						"         CP.PERCENTUALE_POSSESSO, " +
						"         CP.DATA_INIZIO_CONDUZIONE, " +
						"         CP.DATA_FINE_CONDUZIONE, " +
						"         CP.RECORD_MODIFICATO, " +
						"         CP.SUPERFICIE_AGRONOMICA, " +
						"         -1, " +
						"         -1, " +
						"         SP.ID_CASO_PARTICOLARE, "	+
						"         TCP.DESCRIZIONE, " +
						"         CP.ID_UTE, " +
						"         U.COMUNE, " +
						"         C2.DESCOM, " +
						"         F.ID_FOGLIO, " +
		//				"         F.ID_AREA_E, " +
						"         PC.SUP_SEMINABILE, " +
						"         PC.SUP_COLT_ARBOREA_SPECIALIZZATA, " +
						"         PC.SUP_USO_GRAFICA, " +
						"         -1, "	+
						"         -1, " +
						"         AA.CUAA, " +
						"         AA.DENOMINAZIONE, " +
						"         BIOLOGICO.SUP_BIOLOGICO, " +
	          "         BIOLOGICO.SUP_CONVENZIONALE, " +
	          "         BIOLOGICO.SUP_IN_CONVERSIONE, " +
	          "         BIOLOGICO.DATA_INIZIO_CONVERSIONE, " +
	          "         BIOLOGICO.DATA_FINE_CONVERSIONE, " +
            "         PAS.DESCRIZIONE, " +
            "         SP.ID_PARTICELLA, " +
            "         ISR.DATA_RICHIESTA, " +
            "         ISR.DATA_EVASIONE, " +
            "         -1 ";
				query +=
    				"         HAVING DECODE(CP.ID_TITOLO_POSSESSO, 5 , CP.SUPERFICIE_CONDOTTA, " +
    				"             TRUNC(DECODE(SP.SUPERFICIE_GRAFICA,0,SP.SUP_CATASTALE,SP.SUPERFICIE_GRAFICA) " +
            "                      * CP.PERCENTUALE_POSSESSO / 100,4) " +
            "                - TRUNC(SUM(NVL(UP.SUPERFICIE_UTILIZZATA,0)),4)) > 0 ";
			}
			
			query += " ORDER BY DESCOM,SIGLA_PROVINCIA,SEZIONE,FOGLIO,PARTICELLA,SUBALTERNO," +
					"ID_TITOLO_POSSESSO,COD_PRIMARIO,DESC_PRIMARIO,COD_SECONDARIO,DESC_SECONDARIO,ID_CASO_PARTICOLARE";
			
			
			stmt = conn.prepareStatement(query);
			
			SolmrLogger.debug(this, "Executing searchParticelleExcelByParameters: " + query + "\n");
			
			// Prima parte della query: viene eseguita solo se l'utente non sceglie
			// di visualizzare solo le particella senza uso del suolo specificato
			int indice = 0;
			boolean isFirst = true;
		  // ID_AZIENDA PARTICELLA
      SolmrLogger.debug(this, "Value of parameter " + indice
          + " [ID_AZIENDA] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: " + idAzienda + "\n");
      stmt.setLong(++indice, idAzienda.longValue());
      
      //Istanza riesame
      stmt.setLong(++indice, idAzienda.longValue());
			
			
			if (filtriParticellareRicercaVO.getIdUtilizzo().intValue() != 0)
			{
				isFirst = false;
				
			  //Notifiche
	      stmt.setLong(++indice, idAzienda.longValue());
				
				// ID_AZIENDA
				SolmrLogger.debug(this, "Value of parameter " + indice
						+ " [ID_AZIENDA] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: " + idAzienda + "\n");
				stmt.setLong(++indice, idAzienda.longValue());
				//biologico
				stmt.setLong(++indice, idAzienda.longValue());
				listOfParameters += "ID_AZIENDA: " + idAzienda;
			  // ID_MACRO_USO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdMacroUso()))
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_MACRO_USO] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdMacroUso()
              + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdMacroUso().longValue());
          listOfParameters += "ID_MACRO_USO: " + filtriParticellareRicercaVO.getIdMacroUso().longValue();
        }
				// ID_UTE
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdUte()))
				{
					indice++;
					SolmrLogger.debug(this, "Value of parameter " + indice
							+ "[ID_UTE] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdUte() + "\n");
					stmt.setLong(indice, filtriParticellareRicercaVO.getIdUte().longValue());
					listOfParameters += ", ID_UTE: " + filtriParticellareRicercaVO.getIdUte();
				}
        // TIPO DOCUMENTO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()))
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_CATEGORIA_DOCUMENTO] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdTipoDocumento()
              + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTipoDocumento().longValue());
          listOfParameters += ", ID_CATEGORIA_DOCUMENTO: " + filtriParticellareRicercaVO.getIdTipoDocumento();
        }
        
        // DOCUMENTO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_DOCUMENTO] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdDocumento()
              + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdDocumento().longValue());
          listOfParameters += ", ID_DOCUMENTO: " + filtriParticellareRicercaVO.getIdDocumento();
        }
        // PROTOCOLLO DOCUMENTO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_PROTOCOLLO_DOCUMENTO] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdProtocolloDocumento()
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
        // TIPOLOGIA NOTIFICA
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipologiaNotifica())) 
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_TIPOLOGIA_NOTIFICA] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdTipologiaNotifica()
              + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTipologiaNotifica().longValue());
          listOfParameters += ", ID_TIPOLOGIA_NOTIFICA: " + filtriParticellareRicercaVO.getIdTipologiaNotifica();
        }
        // CATEGORIA_NOTIFICA
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCategoriaNotifica())) 
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_CATEGORIA_NOTIFICA] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdCategoriaNotifica()
              + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdCategoriaNotifica().longValue());
          listOfParameters += ", ID_CATEGORIA_NOTIFICA: " + filtriParticellareRicercaVO.getIdCategoriaNotifica();
        }
				// ISTAT_COMUNE
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIstatComune()))
				{
					indice++;
					SolmrLogger.debug(this, "Value of parameter " + indice
							+ "[ISTAT_COMUNE] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: "
							+ filtriParticellareRicercaVO.getIstatComune() + "\n");
					stmt.setString(indice, filtriParticellareRicercaVO.getIstatComune());
					listOfParameters += ", ISTAT_COMUNE: " + filtriParticellareRicercaVO.getIstatComune();
				}
				// SEZIONE
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSezione()))
				{
					indice++;
					SolmrLogger.debug(this, "Value of parameter " + indice
							+ "[SEZIONE] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getSezione()
							+ "\n");
					stmt.setString(indice, filtriParticellareRicercaVO.getSezione().toUpperCase());
					listOfParameters += ", SEZIONE: " + filtriParticellareRicercaVO.getSezione();
				}
				// FOGLIO
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getFoglio()))
				{
					indice++;
					if (Validator.isNumericInteger(filtriParticellareRicercaVO.getFoglio()))
					{
						SolmrLogger.debug(this, "Value of parameter " + indice
								+ "[FOGLIO] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getFoglio()
								+ "\n");
						stmt.setString(indice, filtriParticellareRicercaVO.getFoglio());
						listOfParameters += ", FOGLIO: " + filtriParticellareRicercaVO.getFoglio();
					}
					else
					{
						SolmrLogger.debug(this, "Value of parameter " + indice
								+ "[FOGLIO] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: " + null + "\n");
						stmt.setString(indice, null);
						listOfParameters += ", FOGLIO: " + null;
					}
				}
				// PARTICELLA
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getParticella()))
				{
					indice++;
					if (Validator.isNumericInteger(filtriParticellareRicercaVO.getParticella()))
					{
						SolmrLogger.debug(this, "Value of parameter " + indice
								+ "[PARTICELLA] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: "
								+ filtriParticellareRicercaVO.getParticella() + "\n");
						stmt.setString(indice, filtriParticellareRicercaVO.getParticella());
						listOfParameters += ", PARTICELLA: " + filtriParticellareRicercaVO.getParticella();
					}
					else
					{
						SolmrLogger.debug(this, "Value of parameter " + indice
								+ "[PARTICELLA] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: " + null + "\n");
						stmt.setString(indice, null);
						listOfParameters += ", PARTICELLA: " + null;
					}
				}
				// SUBALTERNO
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSubalterno()))
				{
					indice++;
					SolmrLogger.debug(this, "Value of parameter " + indice
							+ "[SUBALTERNO] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getSubalterno()
							+ "\n");
					stmt.setString(indice, filtriParticellareRicercaVO.getSubalterno().toUpperCase());
					listOfParameters += ", SUBALTERNO: " + filtriParticellareRicercaVO.getSubalterno();
				}
        // ID_ZONA_ALTIMETRICA
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdZonaAltimetrica()))
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_ZONA_ALTIMETRICA] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
              + filtriParticellareRicercaVO.getIdZonaAltimetrica() + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdZonaAltimetrica().longValue());
          listOfParameters += ", ID_ZONA_ALTIMETRICA: " + filtriParticellareRicercaVO.getIdZonaAltimetrica();
        }
        // ID_CASO_PARTICOLARE
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCasoParticolare()))
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_CASO_PARTICOLARE] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
              + filtriParticellareRicercaVO.getIdCasoParticolare() + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdCasoParticolare().longValue());
          listOfParameters += ", ID_CASO_PARTICOLARE: " + filtriParticellareRicercaVO.getIdCasoParticolare();
        }
				if (filtriParticellareRicercaVO.getIdUtilizzo().longValue() > 0)
        {
  				// Se non è stato indicato se l'uso è primario o secondario
  				if (!Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario())
  						&& !Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario()))
  				{
  					// Se è stato indicato un particolare uso del suolo, seleziono di
  					// default l'opzione
  					// uso primario
  					
						indice++;
						SolmrLogger.debug(this, "Value of parameter " + indice
								+ "[ID_UTILIZZO] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: "
								+ filtriParticellareRicercaVO.getIdUtilizzo() + "\n");
						stmt.setLong(indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
						listOfParameters += ", ID_UTILIZZO: " + filtriParticellareRicercaVO.getIdUtilizzo();
  				}
  				else
  				{
  					// Uso primario
  					if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario()))
  					{
  						indice++;
  						SolmrLogger.debug(this, "Value of parameter " + indice
  								+ "[ID_UTILIZZO] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: "
  								+ filtriParticellareRicercaVO.getIdUtilizzo() + "\n");
  						stmt.setLong(indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
  						listOfParameters += ", ID_UTILIZZO: " + filtriParticellareRicercaVO.getIdUtilizzo();
  					}
  					// Uso secondario
  					if (Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario()))
  					{
  						indice++;
  						SolmrLogger.debug(this, "Value of parameter " + indice
  								+ "[ID_UTILIZZO_SECONDARIO] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: "
  								+ filtriParticellareRicercaVO.getIdUtilizzo() + "\n");
  						stmt.setLong(indice, filtriParticellareRicercaVO.getIdUtilizzo().longValue());
  						listOfParameters += ", ID_UTILIZZO_SECONDARIO: " + filtriParticellareRicercaVO.getIdUtilizzo();
  					}
  				}
        }
				//TIPO EFA
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoEfa()))
        {        
          indice++;
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_TIPO_EFA] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: "
              + filtriParticellareRicercaVO.getIdTipoEfa() + "\n");
          stmt.setLong(indice, filtriParticellareRicercaVO.getIdTipoEfa().longValue());
          listOfParameters += ", ID_TIPO_EFA: " + filtriParticellareRicercaVO.getIdTipoEfa();
        }
				// ID_TIPO_TITOLO_POSSESSO
				if (filtriParticellareRicercaVO.getIdTitoloPossesso() != null)
				{
					indice++;
					SolmrLogger.debug(this, "Value of parameter" + indice
							+ "[ID_TITOLO_POSSESSO] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: "
							+ filtriParticellareRicercaVO.getIdTitoloPossesso() + "\n");
					stmt.setLong(indice, filtriParticellareRicercaVO.getIdTitoloPossesso().longValue());
					listOfParameters += ", ID_TITOLO_POSSESSO: " + filtriParticellareRicercaVO.getIdTitoloPossesso();
				}
				// SEGNALAZIONI
				// Se l'utente ha specificato la tipologia di anomalia bloccante
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneBloccante()))
				{
					indice++;
					SolmrLogger.debug(this, "Value of parameter " + indice
							+ "[TIPO_SEGNALAZIONE_BLOCCANTE] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: "
							+ filtriParticellareRicercaVO.getTipoSegnalazioneBloccante() + "\n");
					stmt.setString(indice, filtriParticellareRicercaVO.getTipoSegnalazioneBloccante());
					listOfParameters += ", SEGNALAZIONE: " + filtriParticellareRicercaVO.getTipoSegnalazioneBloccante();
				}
				// Se l'utente ha specificato la tipologia di anomalia warning
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneWarning()))
				{
					indice++;
					SolmrLogger.debug(this, "Value of parameter " + indice
							+ "[TIPO_SEGNALAZIONE_WARNING] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: "
							+ filtriParticellareRicercaVO.getTipoSegnalazioneWarning() + "\n");
					stmt.setString(indice, filtriParticellareRicercaVO.getTipoSegnalazioneWarning());
					listOfParameters += ", SEGNALAZIONE: " + filtriParticellareRicercaVO.getTipoSegnalazioneWarning();
				}
				// Se l'utente ha specificato la tipologia di anomalia OK
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneOk()))
				{
					indice++;
					SolmrLogger.debug(this, "Value of parameter " + indice
							+ "[TIPO_SEGNALAZIONE_OK] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: "
							+ filtriParticellareRicercaVO.getTipoSegnalazioneOk() + "\n");
					stmt.setString(indice, filtriParticellareRicercaVO.getTipoSegnalazioneOk());
					listOfParameters += ", SEGNALAZIONE: " + filtriParticellareRicercaVO.getTipoSegnalazioneOk();
				}
			}
		  // Se l'utente ha specificato un determinato controllo
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo()))
      {
        SolmrLogger.debug(this, "Value of parameter " + indice + "[ID_AZIENDA] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
            + idAzienda + "\n");
        stmt.setLong(++indice, idAzienda.longValue());
        listOfParameters += ", ID_AZIENDA: " + idAzienda;
        SolmrLogger.debug(this, "Value of parameter " + indice
            + "[ID_CONTROLLO] in searchParticelleByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdControllo()
            + "\n");
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdControllo().longValue());
        listOfParameters += ", ID_CONTROLLO: " + filtriParticellareRicercaVO.getIdControllo();
      }
		  // Ricerco i dati della particella certificata solo se vengono
      // richieste le particelle solo asservite
      if (filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S))
      {
        SolmrLogger.debug(this, "Value of parameter " + indice + "[ID_AZIENDA] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
            + idAzienda + "\n");
        stmt.setLong(++indice, idAzienda.longValue());
        listOfParameters += ", ID_AZIENDA: " + idAzienda;
        SolmrLogger.debug(this, "Value of parameter " + indice
            + "[ID_TITOLO_POSSESSO] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
            + SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO + "\n");
        stmt.setLong(++indice, SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO.longValue());
        listOfParameters += ", ID_TITOLO_POSSESSO: " + SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
      }
      // Ricerco i dati delle particelle solo conferite
      if (filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S))
      {
        SolmrLogger.debug(this, "Value of parameter " + indice + "[ID_AZIENDA] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
            + idAzienda + "\n");
        stmt.setLong(++indice, idAzienda.longValue());
        listOfParameters += ", ID_AZIENDA: " + idAzienda;
        SolmrLogger.debug(this, "Value of parameter " + indice
            + "[ID_TITOLO_POSSESSO] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
            + SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO + "\n");
        stmt.setLong(++indice, SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO.longValue());
        listOfParameters += ", ID_TITOLO_POSSESSO: " + SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO;
      }
			// Settaggio parametri seconda query
			// La seconda parte della query viene eseguita solo se l'utente non ha
			// scelto un particolare uso del suolo da visualizzare
			if ((filtriParticellareRicercaVO.getIdUtilizzo().intValue() <= 0) 
			    && (filtriParticellareRicercaVO.getIdMacroUso() == null)
			    && (filtriParticellareRicercaVO.getIdTipoEfa() == null))
			{
			  
			  //Notifiche
        stmt.setLong(++indice, idAzienda.longValue());
			  
				// ID_AZIENDA
			  //biologico
        stmt.setLong(++indice, idAzienda.longValue());
				
        stmt.setLong(++indice, idAzienda.longValue());
				if (isFirst)
				{
					listOfParameters += "ID_AZIENDA: " + idAzienda;
				}
				// ID_UTE
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdUte()))
				{
					indice++;
					SolmrLogger.debug(this, "Value of parameter" + indice + "[ID_UTE] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: "
							+ filtriParticellareRicercaVO.getIdUte() + "\n");
					stmt.setLong(indice, filtriParticellareRicercaVO.getIdUte().longValue());
					if (isFirst)
					{
						listOfParameters += ", ID_UTE: " + filtriParticellareRicercaVO.getIdUte();
					}
				}
        // TIPO DOCUMENTO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento()))
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_CATEGORIA_DOCUMENTO] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdTipoDocumento()
              + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTipoDocumento().longValue());
          listOfParameters += ", ID_CATEGORIA_DOCUMENTO: " + filtriParticellareRicercaVO.getIdTipoDocumento();
        }
        
        // DOCUMENTO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento()))
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_DOCUMENTO] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdDocumento()
              + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdDocumento().longValue());
          listOfParameters += ", ID_DOCUMENTO: " + filtriParticellareRicercaVO.getIdDocumento();
        }
        // PROTOCOLLO DOCUMENTO
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento()))
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_PROTOCOLLO_DOCUMENTO] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdProtocolloDocumento()
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
        // TIPOLOGIA NOTIFICA
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipologiaNotifica())) 
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_TIPOLOGIA_NOTIFICA] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdTipologiaNotifica()
              + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdTipologiaNotifica().longValue());
          listOfParameters += ", ID_TIPOLOGIA_NOTIFICA: " + filtriParticellareRicercaVO.getIdTipologiaNotifica();
        }
        // CATEGORIA_NOTIFICA
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCategoriaNotifica())) 
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_CATEGORIA_NOTIFICA] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdCategoriaNotifica()
              + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdCategoriaNotifica().longValue());
          listOfParameters += ", ID_CATEGORIA_NOTIFICA: " + filtriParticellareRicercaVO.getIdCategoriaNotifica();
        }
				// ISTAT_COMUNE
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIstatComune()))
				{
					indice++;
					SolmrLogger.debug(this, "Value of parameter" + indice
							+ "[ISTAT_COMUNE] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: "
							+ filtriParticellareRicercaVO.getIstatComune() + "\n");
					stmt.setString(indice, filtriParticellareRicercaVO.getIstatComune());
					if (isFirst)
					{
						listOfParameters += ", ISTAT_COMUNE: " + filtriParticellareRicercaVO.getIstatComune();
					}
				}
				// SEZIONE
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSezione()))
				{
					indice++;
					SolmrLogger.debug(this, "Value of parameter" + indice
							+ "[SEZIONE] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getSezione()
							+ "\n");
					stmt.setString(indice, filtriParticellareRicercaVO.getSezione().toUpperCase());
					if (isFirst)
					{
						listOfParameters += ", SEZIONE: " + filtriParticellareRicercaVO.getSezione();
					}
				}
				// FOGLIO
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getFoglio()))
				{
					indice++;
					if (Validator.isNumericInteger(filtriParticellareRicercaVO.getFoglio()))
					{
						SolmrLogger.debug(this, "Value of parameter" + indice
								+ "[FOGLIO] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getFoglio()
								+ "\n");
						stmt.setString(indice, filtriParticellareRicercaVO.getFoglio());
						if (isFirst)
						{
							listOfParameters += ", FOGLIO: " + filtriParticellareRicercaVO.getFoglio();
						}
					}
					else
					{
						SolmrLogger.debug(this, "Value of parameter" + indice
								+ "[FOGLIO] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: " + null + "\n");
						stmt.setString(indice, null);
						if (isFirst)
						{
							listOfParameters += ", FOGLIO: " + null;
						}
					}
				}
				// PARTICELLA
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getParticella()))
				{
					indice++;
					if (Validator.isNumericInteger(filtriParticellareRicercaVO.getParticella()))
					{
						SolmrLogger.debug(this, "Value of parameter" + indice
								+ "[PARTICELLA] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: "
								+ filtriParticellareRicercaVO.getParticella() + "\n");
						stmt.setString(indice, filtriParticellareRicercaVO.getParticella());
						if (isFirst)
						{
							listOfParameters += ", PARTICELLA: " + filtriParticellareRicercaVO.getParticella();
						}
					}
					else
					{
						SolmrLogger.debug(this, "Value of parameter" + indice
								+ "[PARTICELLA] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: " + null + "\n");
						stmt.setString(indice, null);
						if (isFirst)
						{
							listOfParameters += ", PARTICELLA: " + null;
						}
					}
				}
				// SUBALTERNO
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getSubalterno()))
				{
					indice++;
					SolmrLogger.debug(this, "Value of parameter" + indice
							+ "[SUBALTERNO] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getSubalterno()
							+ "\n");
					stmt.setString(indice, filtriParticellareRicercaVO.getSubalterno().toUpperCase());
					if (isFirst)
					{
						listOfParameters += ", SUBALTERNO: " + filtriParticellareRicercaVO.getSubalterno();
					}
				}
        // ID_ZONA_ALTIMETRICA
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdZonaAltimetrica()))
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_ZONA_ALTIMETRICA] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
              + filtriParticellareRicercaVO.getIdZonaAltimetrica() + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdZonaAltimetrica().longValue());
          listOfParameters += ", ID_ZONA_ALTIMETRICA: " + filtriParticellareRicercaVO.getIdZonaAltimetrica();
        }
        // ID_CASO_PARTICOLARE
        if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCasoParticolare()))
        {
          SolmrLogger.debug(this, "Value of parameter " + indice
              + "[ID_CASO_PARTICOLARE] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
              + filtriParticellareRicercaVO.getIdCasoParticolare() + "\n");
          stmt.setLong(++indice, filtriParticellareRicercaVO.getIdCasoParticolare().longValue());
          listOfParameters += ", ID_CASO_PARTICOLARE: " + filtriParticellareRicercaVO.getIdCasoParticolare();
        }
				// ID_TIPO_TITOLO_POSSESSO
				if (filtriParticellareRicercaVO.getIdTitoloPossesso() != null)
				{
					indice++;
					SolmrLogger.debug(this, "Value of parameter" + indice
							+ "[ID_TITOLO_POSSESSO] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: "
							+ filtriParticellareRicercaVO.getIdTitoloPossesso() + "\n");
					stmt.setLong(indice, filtriParticellareRicercaVO.getIdTitoloPossesso().longValue());
					if (isFirst)
					{
						listOfParameters += ", ID_TITOLO_POSSESSO: " + filtriParticellareRicercaVO.getIdTitoloPossesso();
					}
				}
				// Se l'utente ha specificato la tipologia di anomalia bloccante
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneBloccante()))
				{
					indice++;
					SolmrLogger.debug(this, "Value of parameter " + indice
							+ "[TIPO_SEGNALAZIONE_BLOCCANTE] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: "
							+ filtriParticellareRicercaVO.getTipoSegnalazioneBloccante() + "\n");
					stmt.setString(indice, filtriParticellareRicercaVO.getTipoSegnalazioneBloccante());
					listOfParameters += ", SEGNALAZIONE: " + filtriParticellareRicercaVO.getTipoSegnalazioneBloccante();
				}
				// Se l'utente ha specificato la tipologia di anomalia warning
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneWarning()))
				{
					indice++;
					SolmrLogger.debug(this, "Value of parameter " + indice
							+ "[TIPO_SEGNALAZIONE_WARNING] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: "
							+ filtriParticellareRicercaVO.getTipoSegnalazioneWarning() + "\n");
					stmt.setString(indice, filtriParticellareRicercaVO.getTipoSegnalazioneWarning());
					listOfParameters += ", SEGNALAZIONE: " + filtriParticellareRicercaVO.getTipoSegnalazioneWarning();
				}
				// Se l'utente ha specificato la tipologia di anomalia OK
				if (Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneOk()))
				{
					indice++;
					SolmrLogger.debug(this, "Value of parameter " + indice
							+ "[TIPO_SEGNALAZIONE_OK] in searchParticelleExcelByParameters method in ConduzioneParticellaDAO: "
							+ filtriParticellareRicercaVO.getTipoSegnalazioneOk() + "\n");
					stmt.setString(indice, filtriParticellareRicercaVO.getTipoSegnalazioneOk());
					listOfParameters += ", SEGNALAZIONE: " + filtriParticellareRicercaVO.getTipoSegnalazioneOk();
				}
			}
		  // Se l'utente ha specificato un determinato controllo
      if (Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo()))
      {
        SolmrLogger.debug(this, "Value of parameter " + indice + "[ID_AZIENDA] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
            + idAzienda + "\n");
        stmt.setLong(++indice, idAzienda.longValue());
        listOfParameters += ", ID_AZIENDA: " + idAzienda;
        SolmrLogger.debug(this, "Value of parameter " + indice
            + "[ID_CONTROLLO] in searchParticelleByParameters method in ConduzioneParticellaDAO: " + filtriParticellareRicercaVO.getIdControllo()
            + "\n");
        stmt.setLong(++indice, filtriParticellareRicercaVO.getIdControllo().longValue());
        listOfParameters += ", ID_CONTROLLO: " + filtriParticellareRicercaVO.getIdControllo();
      }
		  // Ricerco i dati della particella certificata solo se vengono
      // richieste le particelle solo asservite
      if (filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S))
      {
        SolmrLogger.debug(this, "Value of parameter " + indice + "[ID_AZIENDA] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
            + idAzienda + "\n");
        stmt.setLong(++indice, idAzienda.longValue());
        listOfParameters += ", ID_AZIENDA: " + idAzienda;
        SolmrLogger.debug(this, "Value of parameter " + indice
            + "[ID_TITOLO_POSSESSO] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
            + SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO + "\n");
        stmt.setLong(++indice, SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO.longValue());
        listOfParameters += ", ID_TITOLO_POSSESSO: " + SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO;
      }
      // Ricerco i dati delle particelle solo conferite
      if (filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S))
      {
        SolmrLogger.debug(this, "Value of parameter " + indice + "[ID_AZIENDA] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
            + idAzienda + "\n");
        stmt.setLong(++indice, idAzienda.longValue());
        listOfParameters += ", ID_AZIENDA: " + idAzienda;
        SolmrLogger.debug(this, "Value of parameter " + indice
            + "[ID_TITOLO_POSSESSO] in searchParticelleByParameters method in ConduzioneParticellaDAO: "
            + SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO + "\n");
        stmt.setLong(++indice, SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO.longValue());
        listOfParameters += ", ID_TITOLO_POSSESSO: " + SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO;
      }
			
			ResultSet rs = stmt.executeQuery();
			
			// Primo monitoraggio
			SolmrLogger.debug(this, "ConduzioneParticellaDAO::searchParticelleExcelByParameters - "+
					"In searchParticelleExcelByParameters method from the creation of query to the execution, List of parameters: " + listOfParameters);
			
			
			Vector<Long> vIdConduzioneParticella = null;
			
			while (rs.next())
			{		  
				AnagParticellaExcelVO anagParticellaExcelVO = new AnagParticellaExcelVO();
				AnagAziendaVO anagAziendaVO = new AnagAziendaVO();
				anagParticellaExcelVO.setLabelUte(rs.getString("COM_UTE") + " - " + rs.getString("DESC_COM_UTE"));
				anagParticellaExcelVO.setIstatComuneParticella(rs.getString("COMUNE"));
				anagParticellaExcelVO.setDescrizioneComuneParticella(rs.getString("DESCOM") +" ("+rs.getString("SIGLA_PROVINCIA")+")");
				anagParticellaExcelVO.setSezione(rs.getString("SEZIONE"));
				anagParticellaExcelVO.setFoglio(rs.getString("FOGLIO"));
				anagParticellaExcelVO.setParticella(rs.getString("PARTICELLA"));
				anagParticellaExcelVO.setSubalterno(rs.getString("SUBALTERNO"));
				anagParticellaExcelVO.setSuperficieCatastale(StringUtils.parseSuperficieField(rs.getString("SUP_CATASTALE")));
				anagParticellaExcelVO.setSuperficieGrafica(StringUtils.parseSuperficieField(rs.getString("SUPERFICIE_GRAFICA")));
				anagParticellaExcelVO.setDescrizioneZonaAltimetrica(rs.getString("DESC_ZONA_ALT"));
				//anagParticellaExcelVO.setDescrizioneCorpoIdrico(rs.getString("DESC_AREA_M"));
        
				/*if (Validator.isNotEmpty(rs.getString("ID_AREA_C")))
				{
					if (!rs.getString("ID_AREA_C").equalsIgnoreCase(String.valueOf(SolmrConstants.ID_AREA_C_NO_NATURA_2000)))
					{
						anagParticellaExcelVO.setNatura2000("X");
					}
				}*/
				anagParticellaExcelVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
				anagParticellaExcelVO.setSuperficieCondotta(StringUtils.parseSuperficieField(rs.getString("SUPERFICIE_CONDOTTA")));
				anagParticellaExcelVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
				if (Validator.isNotEmpty(rs.getString("ID_CASO_PARTICOLARE")))
				{
					anagParticellaExcelVO.setIdCasoParticolare(new Long(rs.getLong("ID_CASO_PARTICOLARE")));
				}
				if (rs.getString("FLAG_IRRIGABILE").equalsIgnoreCase(SolmrConstants.FLAG_S))
				{
					anagParticellaExcelVO.setIrrigua("X");
				}
				if (Validator.isNotEmpty(rs.getString("ID_UTILIZZO_PARTICELLA")))
				{
					if (Long.decode(rs.getString("ID_UTILIZZO_PARTICELLA")).longValue() > -1)
					{
						anagParticellaExcelVO.setIdUtilizzoParticella(new Long(rs.getLong("ID_UTILIZZO_PARTICELLA")));
						String usoPrimario = "";
						if (Validator.isNotEmpty(rs.getString("COD_PRIMARIO")))
						{
							usoPrimario += "[" + rs.getString("COD_PRIMARIO") + "] ";
						}
						if (Validator.isNotEmpty(rs.getString("DESC_PRIMARIO")))
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
						if (Validator.isNotEmpty(rs.getString("COD_VARIETA")))
						{
							varieta += "[" + rs.getString("COD_VARIETA") + "] ";
						}
						if (Validator.isNotEmpty(rs.getString("DESC_VARIETA")))
						{
							varieta += rs.getString("DESC_VARIETA");
						}
						anagParticellaExcelVO.setVarieta(varieta);
						String usoSecondario = "";
						if (Validator.isNotEmpty(rs.getString("COD_SECONDARIO")))
						{
							usoSecondario += "[" + rs.getString("COD_SECONDARIO") + "] ";
						}
						if (Validator.isNotEmpty(rs.getString("DESC_SECONDARIO")))
						{
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
						if (Validator.isNotEmpty(rs.getString("COD_VAR_SECONDARIA")))
						{
							varietaSecondaria += "[" + rs.getString("COD_VAR_SECONDARIA") + "] ";
						}
						if (Validator.isNotEmpty(rs.getString("VAR_SECONDARIA")))
						{
							varietaSecondaria += rs.getString("VAR_SECONDARIA");
						}
						anagParticellaExcelVO.setVarietaSecondaria(varietaSecondaria);
						if (Validator.isNotEmpty(rs.getString("SUP_UTILIZZATA_SECONDARIA")))
						{
							anagParticellaExcelVO.setSuperficieUtilizzataSecondaria(StringUtils.parseSuperficieField(rs.getString("SUP_UTILIZZATA_SECONDARIA")));
						}
						anagParticellaExcelVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
						anagParticellaExcelVO.setNumeroPianteCeppi(rs.getString("NUMERO_PIANTE_CEPPI"));
					}
					anagParticellaExcelVO.setSuperficieUtilizzata(StringUtils.parseSuperficieField(rs.getString("SUPERFICIE_UTILIZZATA")));
				}
				/*if (Validator.isNotEmpty(rs.getString("ID_AREA_E"))
						&& rs.getString("ID_AREA_E").equalsIgnoreCase(String.valueOf(SolmrConstants.ID_AREA_E_ZVN)))
				{
					anagParticellaExcelVO.setDescrizioneZonaVulnerabile("X");
				}*/
				if (Validator.isNotEmpty(rs.getString("SUP_SEMINABILE")))
				{
					anagParticellaExcelVO.setSuperficieSeminabile(StringUtils.parseSuperficieField(rs.getString("SUP_SEMINABILE")));
				}
				else
				{
					anagParticellaExcelVO.setSuperficieSeminabile(SolmrConstants.DEFAULT_SUPERFICIE);
				}
				if (Validator.isNotEmpty(rs.getString("SUP_COLT_ARBOREA_SPECIALIZZATA")))
				{
					anagParticellaExcelVO.setSuperficieArboreaSpecializzata(StringUtils.parseSuperficieField(rs.getString("SUP_COLT_ARBOREA_SPECIALIZZATA")));
				}
				else
				{
					anagParticellaExcelVO.setSuperficieArboreaSpecializzata(SolmrConstants.DEFAULT_SUPERFICIE);
				}
				if (Validator.isNotEmpty(rs.getString("SUPERFICIE_AGRONOMICA")))
				{
					anagParticellaExcelVO.setSupAgronomica(StringUtils.parseSuperficieField(rs.getString("SUPERFICIE_AGRONOMICA")));
				}
				else
				{
					anagParticellaExcelVO.setSupAgronomica(SolmrConstants.DEFAULT_SUPERFICIE);
				}
				if (Validator.isNotEmpty(rs.getString("SUP_USO_GRAFICA")))
				{
					anagParticellaExcelVO.setSupUsoGrafico(StringUtils.parseSuperficieField(rs.getString("SUP_USO_GRAFICA")));
				}
				else
				{
					anagParticellaExcelVO.setSupUsoGrafico(SolmrConstants.DEFAULT_SUPERFICIE);
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
        
				
        anagParticellaExcelVO.setSupEleggibile(rs.getString("SUP_ELEGGIBILE"));
        anagParticellaExcelVO.setSupEleggibileNetta(rs.getString("SUP_ELEGGIBILE_NETTA"));
        
        anagParticellaExcelVO.setInNotifica(rs.getString("IN_NOTIFICA"));     
        
        
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
				
				
				Long idConduzioneParticella = new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA"));
				if(vIdConduzioneParticella == null)
			    vIdConduzioneParticella = new Vector<Long>();
				
				if(!vIdConduzioneParticella.contains(idConduzioneParticella))
				  vIdConduzioneParticella.add(idConduzioneParticella);
				
				anagParticellaExcelVO.setIdConduzioneParticella(idConduzioneParticella);
				  
          //anagParticellaExcelVO.setDocumento(
              //getDocumentiFromIdConduzioneParticella(idConduzioneParticella, idAzienda));
        
        
        /*if (Validator.isNotEmpty(rs.getString("SUP_REG_PASCOLI")))
        {
          anagParticellaExcelVO.setSupRegPascoli(StringUtils.parseSuperficieField(rs.getString("SUP_REG_PASCOLI")));
        }*/
        anagParticellaExcelVO.setDescFonteRegPascoli(rs.getString("DESC_FONTE_REG_PASCOLI"));
				
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
        if(vIdConduzioneParticella != null)
        {
          HashMap<Long, String> hDocumenti = getHashDocumentiFromIdConduzioneParticella(vIdConduzioneParticella, idAzienda);
          if((hDocumenti != null) && (elencoParticelle != null))
          {
            for(int s=0;s<elencoParticelle.size();s++)
            {
              elencoParticelle.get(s).setDocumento(hDocumenti.get(elencoParticelle.get(s).getIdConduzioneParticella()));              
            }
            
          }
          
          
        }
        
        
        //anagParticellaExcelVO.setDocumento(
            //getDocumentiFromIdConduzioneParticella(idConduzioneParticella, idAzienda));
      }
      catch (Exception e)
      {}
			
			
			
			
			
			// Secondo monitoraggio
			SolmrLogger.debug(this, "ConduzioneParticellaDAO::searchParticelleExcelByParameters - "+
							"In searchParticelleExcelByParameters method from the creation of query to final setting of parameters inside of Vector after resultset's while cicle, "+
							"List of parameters: " + listOfParameters);
			
			rs.close();
			stmt.close();
			
		}
		catch (SQLException exc)
		{
			SolmrLogger.error(this, "searchParticelleExcelByParameters in ConduzioneParticellaDAO - SQLException: " + exc.getMessage() + "\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex)
		{
			SolmrLogger.error(this, "searchParticelleExcelByParameters in ConduzioneParticellaDAO - Generic Exception: " + ex + "\n");
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
						"searchParticelleExcelByParameters in ConduzioneParticellaDAO - SQLException while closing Statement and Connection: " + exc.getMessage()
								+ "\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex)
			{
				SolmrLogger.error(this,
						"searchParticelleExcelByParameters in ConduzioneParticellaDAO - Generic Exception while closing Statement and Connection: "
								+ ex.getMessage() + "\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated searchParticelleExcelByParameters method in ConduzioneParticellaDAO\n");
		return elencoParticelle;
	}	
	
	
	public BigDecimal getSupEleggPlSqlTotale(long idParticellaCertificata, long idCatalogoMatrice) 
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
              "[ConduzioneParticellaDAO::getSupEleggPlSqlTotale] BEGIN.");
      /***
       *  FUNCTION SelTotSupElegByPartEVetCat (pIdPartCertif  IN DB_PARTICELLA_CERT_ELEG.ID_PARTICELLA_CERTIFICATA%TYPE,
       *                                       pStrVetVarieta IN VARCHAR2
       *                                       ) RETURN  DB_PARTICELLA_CERT_ELEG.SUPERFICIE%TYPE;
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{ ? = call Pck_Smrgaa_Libreria.SelTotSupElegByPartEVetCat(?,?)}");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ConduzioneParticellaDAO::getSupEleggPlSqlTotale] Query="
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
              "[ConduzioneParticellaDAO::getSupEleggPlSqlTotale] ",
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
              "[ConduzioneParticellaDAO::getSupEleggPlSqlTotale] END.");
    }
  }
	
	
	
	
	
	/**
	 * non piu usato ora usato getSupElegg
	 * 
	 * 
	 * @param idAzienda
	 * @param idParticella
	 * @param idParticellaCertificata
	 * @param idUtilizzoParticella
	 * @return
	 * @throws DataAccessException
	 */
	public BigDecimal getSupEleggPlSql(long idAzienda, long idParticella,
	    long idParticellaCertificata, long idUtilizzoParticella) 
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
       *  FUNCTION Sup_Eleggibile_Riproporzionata (
       *    pIdAzienda       IN DB_ANAGRAFICA_AZIENDA.ID_AZIENDA%TYPE,
       *    pIdParticella    IN DB_PARTICELLA.ID_PARTICELLA%TYPE,
       *    pIdPartCertif    IN DB_PARTICELLA_CERTIFICATA.ID_PARTICELLA_CERTIFICATA%TYPE,
       *    pIdUtilizzoPart  IN DB_UTILIZZO_PARTICELLA.ID_UTILIZZO_PARTICELLA%TYPE
       *    ) RETURN DB_UTILIZZO_PARTICELLA.SUPERFICIE_UTILIZZATA%TYPE;
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{ ? = call Pck_Smrgaa_Libreria.Sup_Eleggibile_Riproporzionata(?,?,?,?)}");
      
      
      
      
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
      cs.setLong(3, idParticella);
      cs.setLong(4, idParticellaCertificata);
      cs.setLong(5, idUtilizzoParticella);
      
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
        new Parametro("idParticella", idParticella),
        new Parametro("idParticellaCertificata", idParticellaCertificata),
        new Parametro("idUtilizzoParticella", idUtilizzoParticella)};
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
	 * Metodo che mi permette di estrarre la somma delle superfici condotte
	 * relative ad una determinata particella che insiste su una determinata
	 * azienda agriocola
	 * 
	 * @param idAzienda
	 * @param idParticella
	 * @param onlyActive
	 * @return java.lang.String
	 * @throws DataAccessException
	 */
	public BigDecimal getTotSupCondottaByAziendaAndParticella(Long idAzienda, Long idParticella) throws DataAccessException
	{
		SolmrLogger.debug(this, "Invocating getTotSupCondottaByAziendaAndParticella method in ConduzioneParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		BigDecimal totSupCondotta = null;
		
		try
		{
			SolmrLogger.debug(this, "Creating db-connection in getTotSupCondottaByAziendaAndParticella method in ConduzioneParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getTotSupCondottaByAziendaAndParticella method in ConduzioneParticellaDAO and it values: "
					+ conn + "\n");
			
			String query = 
			  "SELECT SUM(CP.SUPERFICIE_CONDOTTA) AS TOT_CONDOTTA " + 
			  "FROM   DB_CONDUZIONE_PARTICELLA CP, " + 
			  "       DB_UTE U " +
			  "WHERE  U.ID_AZIENDA = ? " +
			  "AND    CP.ID_PARTICELLA = ? " +
			  "AND    CP.ID_TITOLO_POSSESSO <> 5 " +
			  "AND    U.ID_UTE = CP.ID_UTE " +
			  "AND    CP.DATA_FINE_CONDUZIONE IS NULL ";
			
			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getTotSupCondottaByAziendaAndParticella method in ConduzioneParticellaDAO: "
					+ idAzienda + "\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ID_PARTICELLA] in getTotSupCondottaByAziendaAndParticella method in ConduzioneParticellaDAO: "
					+ idParticella + "\n");
			
			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());
			stmt.setLong(2, idParticella.longValue());
			
			SolmrLogger.debug(this, "Executing getTotSupCondottaByAziendaAndParticella: " + query + "\n");
			
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next())
			{
				totSupCondotta = rs.getBigDecimal("TOT_CONDOTTA");
			}
			
			rs.close();
			stmt.close();
			
		}
		catch (SQLException exc)
		{
			SolmrLogger.error(this, "getTotSupCondottaByAziendaAndParticella in ConduzioneParticellaDAO - SQLException: " + exc.getMessage() + "\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex)
		{
			SolmrLogger.error(this, "getTotSupCondottaByAziendaAndParticella in ConduzioneParticellaDAO - Generic Exception: " + ex + "\n");
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
						"getTotSupCondottaByAziendaAndParticella in ConduzioneParticellaDAO - SQLException while closing Statement and Connection: "
								+ exc.getMessage() + "\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex)
			{
				SolmrLogger.error(this,
						"getTotSupCondottaByAziendaAndParticella in ConduzioneParticellaDAO - Generic Exception while closing Statement and Connection: "
								+ ex.getMessage() + "\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getTotSupCondottaByAziendaAndParticella method in ConduzioneParticellaDAO\n");
		return totSupCondotta;
	}
	
	/**
	 * Metodo che verifica se esiste una o più conduzioni ripristinate da una
	 * precedente dichiarazione di consistenza
	 * 
	 * @param idAzienda
	 * @return boolean
	 * @throws DataAccessException
	 */
	public boolean isConduzioneRipristinata(Long idAzienda) throws DataAccessException
	{
		SolmrLogger.debug(this, "Invocating isConduzioneRipristinata method in ConduzioneParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean isRipristinata = false;
		
		try
		{
			SolmrLogger.debug(this, "Creating db-connection in isConduzioneRipristinata method in ConduzioneParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in isConduzioneRipristinata method in ConduzioneParticellaDAO and it values: " + conn + "\n");
			
			String query = " SELECT CP.ID_CONDUZIONE_PARTICELLA " + " FROM   DB_CONDUZIONE_PARTICELLA CP, " + "        DB_UTE U "
					+ " WHERE  U.ID_AZIENDA = ? " + " AND    U.ID_UTE = CP.ID_UTE " + " AND    CP.DATA_FINE_CONDUZIONE IS NULL "
					+ " AND    CP.DICHIARAZIONE_RIPRISTINATA = ? ";
			
			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in isConduzioneRipristinata method in ConduzioneParticellaDAO: " + idAzienda + "\n");
			SolmrLogger.debug(this, "Value of parameter 2 [DICHIARAZIONE_RIPRISTINATA] in isConduzioneRipristinata method in ConduzioneParticellaDAO: "
					+ SolmrConstants.FLAG_S + "\n");
			
			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());
			stmt.setString(2, SolmrConstants.FLAG_S);
			
			SolmrLogger.debug(this, "Executing isConduzioneRipristinata: " + query + "\n");
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next())
			{
				if (Validator.isNotEmpty(rs.getString("ID_CONDUZIONE_PARTICELLA")))
				{
					isRipristinata = true;
					break;
				}
			}
			
			rs.close();
			stmt.close();
			
		}
		catch (SQLException exc)
		{
			SolmrLogger.error(this, "isConduzioneRipristinata in ConduzioneParticellaDAO - SQLException: " + exc + "\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex)
		{
			SolmrLogger.error(this, "isConduzioneRipristinata in ConduzioneParticellaDAO - Generic Exception: " + ex + "\n");
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
				SolmrLogger.error(this, "isConduzioneRipristinata in ConduzioneParticellaDAO - SQLException while closing Statement and Connection: " + exc
						+ "\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex)
			{
				SolmrLogger.error(this, "isConduzioneRipristinata in ConduzioneParticellaDAO - Generic Exception while closing Statement and Connection: "
						+ ex + "\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated isConduzioneRipristinata method in ConduzioneParticellaDAO\n");
		return isRipristinata;
	}
	
	/**
	 * Metodo utilizzato per effettuare i riepiloghi per titolo di possesso
	 * relativi ad'azienda agricola
	 * 
	 * @param idAzienda
	 * @param onlyActive
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.ConduzioneParticellaVO[]
	 * @throws DataAccessException
	 */
	public StoricoParticellaVO[] riepilogoTitoloPossesso(Long idAzienda) 
	  throws DataAccessException
	{
		SolmrLogger.debug(this, "Invocating riepilogoTitoloPossesso method in ConduzioneParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();
		
		try
		{
			SolmrLogger.debug(this, "Creating db-connection in riepilogoTitoloPossesso method in ConduzioneParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in riepilogoTitoloPossesso method in ConduzioneParticellaDAO and it values: " + conn + "\n");
			
			String query = " " +
    			"SELECT COND.ID_TITOLO_POSSESSO, " +
          "       COND.DESCRIZIONE, " +
          "       SUM(SUP_COND) AS SUP_CONDOTTA, " +
          "       NVL(SUM(SUP_UTILIZZATA),0) AS SUP_UTILIZZATA " +
          "FROM " +
          "      (SELECT CP.ID_TITOLO_POSSESSO, " +
          "              TTP.DESCRIZIONE, " +
          "              CP.SUPERFICIE_CONDOTTA SUP_COND, " +
          "              CP.ID_CONDUZIONE_PARTICELLA, " +
          "              SUM(UP.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
          "       FROM   DB_CONDUZIONE_PARTICELLA CP, " +
          "              DB_UTE U, " +
          "              DB_TIPO_TITOLO_POSSESSO TTP, " +
          "              DB_UTILIZZO_PARTICELLA UP " +
          "       WHERE  U.ID_AZIENDA = ? " +
          "       AND    U.ID_UTE = CP.ID_UTE " +
          "       AND    CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO " +
          "       AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA(+) " +
				  "       AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
				  "       AND    U.DATA_FINE_ATTIVITA IS NULL " +
					"       GROUP BY CP.ID_TITOLO_POSSESSO, " +
          "                TTP.DESCRIZIONE, " +
          "                CP.SUPERFICIE_CONDOTTA, " + 
          "                CP.ID_CONDUZIONE_PARTICELLA " +
          "        ) COND " +
          "GROUP BY COND.ID_TITOLO_POSSESSO, " + 
          "         COND.DESCRIZIONE " +
          "ORDER BY COND.DESCRIZIONE ASC";	
			
			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoTitoloPossesso method in ConduzioneParticellaDAO: " + idAzienda + "\n");
			
			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());
			
			SolmrLogger.debug(this, "Executing riepilogoTitoloPossesso: " + query + "\n");
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next())
			{
			  StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
				ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
				UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
				conduzioneParticellaVO.setSuperficieCondotta(rs.getString("SUP_CONDOTTA"));
				conduzioneParticellaVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
				CodeDescription code = new CodeDescription(Integer.decode(rs.getString("ID_TITOLO_POSSESSO")), rs.getString("DESCRIZIONE"));
				conduzioneParticellaVO.setTitoloPossesso(code);
				utilizzoParticellaVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
        conduzioneParticellaVO.setElencoUtilizzi(new UtilizzoParticellaVO[]{utilizzoParticellaVO});
				ConduzioneParticellaVO[] elencoConduzioni = new ConduzioneParticellaVO[1];
				elencoConduzioni[0] = conduzioneParticellaVO;
				storicoParticellaVO.setElencoConduzioni(elencoConduzioni);
				elencoParticelle.add(storicoParticellaVO);
			}
			
			rs.close();
			stmt.close();
			
		}
		catch (SQLException exc)
		{
			SolmrLogger.error(this, "riepilogoTitoloPossesso in ConduzioneParticellaDAO - SQLException: " + exc + "\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex)
		{
			SolmrLogger.error(this, "riepilogoTitoloPossesso in ConduzioneParticellaDAO - Generic Exception: " + ex + "\n");
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
				SolmrLogger.error(this, "riepilogoTitoloPossesso in ConduzioneParticellaDAO - SQLException while closing Statement and Connection: " + exc
						+ "\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex)
			{
				SolmrLogger.error(this, "riepilogoTitoloPossesso in ConduzioneParticellaDAO - Generic Exception while closing Statement and Connection: "
						+ ex + "\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated riepilogoTitoloPossesso method in ConduzioneParticellaDAO\n");
		if (elencoParticelle.size() == 0)
		{
			return (StoricoParticellaVO[]) elencoParticelle.toArray(new StoricoParticellaVO[0]);
		}
		else
		{
			return (StoricoParticellaVO[]) elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
		}
	}
	
	/**
	 * Metodo che mi permette di estrarre la somma delle superfici condotte
	 * relative ad una determinata azienda agriocola
	 * 
	 * @param idAzienda
	 * @param onlyActive
	 * @return java.lang.String
	 * @throws DataAccessException
	 */
	public java.lang.String getTotSupCondottaByAzienda(Long idAzienda, boolean onlyActive) throws DataAccessException
	{
		SolmrLogger.debug(this, "Invocating getTotSupCondottaByAzienda method in ConduzioneParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		String totSupCondotta = null;
		
		try
		{
			SolmrLogger.debug(this, "Creating db-connection in getTotSupCondottaByAzienda method in ConduzioneParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getTotSupCondottaByAzienda method in ConduzioneParticellaDAO and it values: " + conn + "\n");
			
			String query = " SELECT SUM(CP.SUPERFICIE_CONDOTTA) AS TOT_CONDOTTA " + " FROM   DB_CONDUZIONE_PARTICELLA CP, " + "        DB_UTE U "
					+ " WHERE  U.ID_AZIENDA = ? " + " AND    U.ID_UTE = CP.ID_UTE ";
			if (onlyActive)
			{
				query += " AND    CP.DATA_FINE_CONDUZIONE IS NULL ";
			}
			
			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getTotSupCondottaByAzienda method in ConduzioneParticellaDAO: " + idAzienda
					+ "\n");
			
			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());
			
			SolmrLogger.debug(this, "Executing getTotSupCondottaByAzienda: " + query + "\n");
			
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next())
			{
				totSupCondotta = rs.getString("TOT_CONDOTTA");
			}
			
			rs.close();
			stmt.close();
			
		}
		catch (SQLException exc)
		{
			SolmrLogger.error(this, "getTotSupCondottaByAzienda in ConduzioneParticellaDAO - SQLException: " + exc + "\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex)
		{
			SolmrLogger.error(this, "getTotSupCondottaByAzienda in ConduzioneParticellaDAO - Generic Exception: " + ex + "\n");
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
				SolmrLogger.error(this, "getTotSupCondottaByAzienda in ConduzioneParticellaDAO - SQLException while closing Statement and Connection: " + exc
						+ "\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex)
			{
				SolmrLogger.error(this, "getTotSupCondottaByAzienda in ConduzioneParticellaDAO - Generic Exception while closing Statement and Connection: "
						+ ex + "\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getTotSupCondottaByAzienda method in ConduzioneParticellaDAO\n");
		return totSupCondotta;
	}
	
	/**
	 * Per ogni conduzione (db_conduzione_particella .id_conduzione_particella)
	 * accedere a db_utilizzo_particella, estrarre tutti gli utilizzi per cui
	 * db_tipo_utilizzo.flag_uso_agronomico=’S’ (superficie spandibile, cioè è
	 * previsto utilizzo agronomico) ed effettuare la sommatoria del campo
	 * superficie_utilizzata.
	 * 
	 * @param idConduzioneParticella
	 * @return
	 * @throws DataAccessException
	 */
	public BigDecimal getSumSuperficieUtilizzoUsoAgronomico(long idConduzioneParticella) throws DataAccessException
	{
		SolmrLogger.debug(this, "Invocating getSumSuperficieUtilizzoUsoAgronomico method in ConduzioneParticellaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		BigDecimal sumSup = null;
		
		try
		{
			SolmrLogger.debug(this, "Creating db-connection in getSumSuperficieUtilizzoUsoAgronomico method in ConduzioneParticellaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getSumSuperficieUtilizzoUsoAgronomico method in ConduzioneParticellaDAO and it values: "
					+ conn + "\n");
			
			String query = "" 
			  + " SELECT " 
			  + "        SUM(UP.SUPERFICIE_UTILIZZATA) AS SUPERFICIE "
				+ " FROM   DB_CONDUZIONE_PARTICELLA CP, DB_UTILIZZO_PARTICELLA UP, " 
				+ "        DB_TIPO_UTILIZZO TU "
				+ " WHERE  CP.ID_CONDUZIONE_PARTICELLA = ? " 
				+ " AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA "
				+ " AND    UP.ID_UTILIZZO = TU.ID_UTILIZZO " 
				+ " AND    TU.FLAG_USO_AGRONOMICO = 'S' " 
				+ " GROUP BY CP.ID_CONDUZIONE_PARTICELLA ";
			
			SolmrLogger.debug(this,
					"Value of parameter 1 [ID_CONDUZIONE_PARTICELLA] in getSumSuperficieUtilizzoUsoAgronomico method in ConduzioneParticellaDAO: "
							+ idConduzioneParticella + "\n");
			
			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idConduzioneParticella);
			
			SolmrLogger.debug(this, "Executing getSumSuperficieUtilizzoUsoAgronomico: " + query + "\n");
			
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next())
			{
				sumSup = rs.getBigDecimal("SUPERFICIE");
				
			}
			
			rs.close();
			stmt.close();
			
		}
		catch (SQLException exc)
		{
			SolmrLogger.error(this, "getSumSuperficieUtilizzoUsoAgronomico in ConduzioneParticellaDAO - SQLException: " + exc.getMessage() + "\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex)
		{
			SolmrLogger.error(this, "getSumSuperficieUtilizzoUsoAgronomico in ConduzioneParticellaDAO - Generic Exception: " + ex + "\n");
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
						"getSumSuperficieUtilizzoUsoAgronomico in ConduzioneParticellaDAO - SQLException while closing Statement and Connection: "
								+ exc.getMessage() + "\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex)
			{
				SolmrLogger.error(this,
						"getSumSuperficieUtilizzoUsoAgronomico in ConduzioneParticellaDAO - Generic Exception while closing Statement and Connection: "
								+ ex.getMessage() + "\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getSumSuperficieUtilizzoUsoAgronomico method in ConduzioneParticellaDAO\n");
		return sumSup;
	}
	
	
	/**
   * Per ogni particella (db_conduzione_particella .id_particella)
   * accedere a db_utilizzo_particella, estrarre tutti gli utilizzi per cui
   * db_tipo_utilizzo.flag_uso_agronomico=’S’ (superficie spandibile, cioè è
   * previsto utilizzo agronomico) ed effettuare la sommatoria del campo
   * superficie_utilizzata.Per le conduzioni relative all'azienda corrente
   * si utilizzano i dati al piano in lavorazione.
   * Per le conduzioni di altre aziende si usano i dati della dichiarazione di consistenza
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
        + "      SELECT SUM(UP.SUPERFICIE_UTILIZZATA) AS SUPERFICIE " 
        + "      FROM   DB_CONDUZIONE_PARTICELLA CP, DB_UTILIZZO_PARTICELLA UP, " 
        + "             DB_TIPO_UTILIZZO TU, DB_UTE UT "
        + "      WHERE  CP.ID_PARTICELLA = ? "
        + "      AND    UT.ID_AZIENDA = ? "
        + "      AND    UT.DATA_FINE_ATTIVITA IS NULL " 
        + "      AND    CP.ID_UTE = UT.ID_UTE "
        + "      AND    CP.ID_CONDUZIONE_PARTICELLA = UP.ID_CONDUZIONE_PARTICELLA " 
        + "      AND    CP.ID_TITOLO_POSSESSO <> 6 "
        + "      AND    CP.DATA_FINE_CONDUZIONE IS NULL " 
        + "      AND    UP.ID_UTILIZZO = TU.ID_UTILIZZO "
        + "      AND    TU.FLAG_USO_AGRONOMICO = 'S' " 
        + "      GROUP BY CP.ID_PARTICELLA "
        + "UNION ALL   "     
        + "      SELECT SUM(NVL(UD.SUPERFICIE_UTILIZZATA,0)) SUPERFICIE "  
        + "      FROM  DB_CONDUZIONE_DICHIARATA CD, DB_DICHIARAZIONE_CONSISTENZA DC, " 
        + "      DB_ANAGRAFICA_AZIENDA AA, DB_UTILIZZO_DICHIARATO UD  "
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
	
	/**
	 * Estrae la somma della superficie agronomica/asservimento per le conduzioni sulla particella
	 * della stessa azienda meno la conduzione passata come parametro. 
	 * 
	 * 
	 * 
	 * @param idParticella
	 * @param idConduzioneParticella
	 * @param idAzienda
	 * @return
	 * @throws DataAccessException
	 */
	public BigDecimal getSumSuperficieAgronomicaAltreconduzioni(long idParticella, long idConduzioneParticella, long idAzienda) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getSumSuperficieAgronomicaAltreconduzioni method in ConduzioneParticellaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    BigDecimal sumSup = new BigDecimal(0);

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getSumSuperficieAgronomicaAltreconduzioni method in ConduzioneParticellaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getSumSuperficieAgronomicaAltreconduzioni method in ConduzioneParticellaDAO and it values: "+conn+"\n");

      String query = " "
          + " SELECT "
          + "       SUM(DECODE(CP.ID_TITOLO_POSSESSO,5, NVL(CP.SUPERFICIE_CONDOTTA,0), NVL(CP.SUPERFICIE_AGRONOMICA,0))) SUPERFICIE " 
          + " FROM  DB_CONDUZIONE_PARTICELLA CP, DB_UTE UT " 
          + " WHERE  UT.ID_AZIENDA = ? "
          + " AND    UT.ID_UTE = CP.ID_UTE "
          + " AND    CP.ID_PARTICELLA = ? "    
          + " AND    UT.DATA_FINE_ATTIVITA IS NULL "
          + " AND    CP.ID_TITOLO_POSSESSO <> 6 "
          + " AND    CP.ID_CONDUZIONE_PARTICELLA <> ? "
          + " AND    CP.DATA_FINE_CONDUZIONE IS NULL "
          + " GROUP BY CP.ID_PARTICELLA ";

     
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda);
      stmt.setLong(2, idParticella);
      stmt.setLong(3, idConduzioneParticella);
      

      SolmrLogger.debug(this, "Executing getSumSuperficieAgronomicaAltreconduzioni: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      if(rs.next()) {
        sumSup = rs.getBigDecimal("SUPERFICIE");        
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getSumSuperficieAgronomicaAltreconduzioni in ConduzioneParticellaDAO - SQLException: "+exc.getMessage()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getSumSuperficieAgronomicaAltreconduzioni in ConduzioneParticellaDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getSumSuperficieAgronomicaAltreconduzioni in ConduzioneParticellaDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getSumSuperficieAgronomicaAltreconduzioni in ConduzioneParticellaDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getSumSuperficieAgronomicaAltreconduzioni method in ConduzioneParticellaDAO\n");
    return sumSup;
  }
	
	/**
	 * ritorna true se ha trovato un record (ha fatto il lock!!)
	 * 
	 * @param long[]
	 *          idConduzioneParticella
	 * @return
	 * @throws DataAccessException
	 */
	public boolean lockTableConduzioneParticella(long[] idConduzioneParticella) throws DataAccessException
	{
		String query = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		StringBuffer queryBuf = null;
		boolean flagLock = false;
		
		String strIN = " (";
		
		if (idConduzioneParticella != null && idConduzioneParticella.length != 0)
		{
			for (int i = 0; i < idConduzioneParticella.length; i++)
			{
				strIN += "?";
				if (i < idConduzioneParticella.length - 1)
					strIN += ",";
			}
		}
		strIN += ") ";
		
		try
		{
			SolmrLogger.debug(this, "[ConduzioneParticellaDAO::lockTableConduzioneParticella] BEGIN.");
			
			/* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */

			queryBuf = new StringBuffer();
			queryBuf.append(" SELECT * " + "FROM DB_CONDUZIONE_PARTICELLA " + "WHERE ID_CONDUZIONE_PARTICELLA  IN " + strIN
					+ "AND DATA_FINE_CONDUZIONE IS NULL " + "FOR UPDATE ");
			
			query = queryBuf.toString();
			/* CONCATENAZIONE/CREAZIONE QUERY END. */

			conn = getDatasource().getConnection();
			if (SolmrLogger.isDebugEnabled(this))
			{
				// Dato che la query costruita dinamicamente è un dato importante la
				// registro sul file di log se il
				// debug è abilitato
				
				SolmrLogger.debug(this, "[ConduzioneParticellaDAO::lockTableConduzioneParticella] Query=" + query);
			}
			stmt = conn.prepareStatement(query);
			
			int indice = 0;
			
			if (idConduzioneParticella != null && idConduzioneParticella.length != 0)
			{
				for (int i = 0; i < idConduzioneParticella.length; i++)
					stmt.setLong(++indice, idConduzioneParticella[i]);
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
			Variabile variabili[] = new Variabile[] {
					new Variabile("query", query), new Variabile("queryBuf", queryBuf), new Variabile("flagLock", flagLock)
			};
			
			// Vettore di parametri passati al metodo
			Parametro parametri[] = new Parametro[] {
				new Parametro("idConduzioneParticella", idConduzioneParticella)
			};
			// Logging dell'eccezione, query, variabili e parametri del metodo
			LoggerUtils.logDAOError(this, "[ConduzioneParticellaDAO::lockTableConduzioneParticella] ", t, query, variabili, parametri);
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
			SolmrLogger.debug(this, "[ConduzioneParticellaDAO::lockTableConduzioneParticella] END.");
		}
	}
	
	/**
	 * 
	 * Restituisce un array di stringhe in cui sono messe in disrtinct la sigla
	 * delle provincie delle particelle
	 * 
	 * @param idUte
	 * @return
	 * @throws DataAccessException
	 */
	public String[] getIstatProvFromConduzione(long idAzienda) throws DataAccessException
	{
		String query = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		StringBuffer queryBuf = null;
		Vector<String> vProvince = null;
		String[] arrProvince = null;
		try
		{
			SolmrLogger.debug(this, "[ConduzioneParticellaGaaDAO::getIstatProvFromConduzione] BEGIN.");
			
			/* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */

			queryBuf = new StringBuffer();
			queryBuf.append("" + "SELECT " + "       DISTINCT(PROV.ISTAT_PROVINCIA) " + "FROM   DB_UTE UT, " + "       DB_CONDUZIONE_PARTICELLA CP, "
					+ "       DB_STORICO_PARTICELLA SP, " + "       COMUNE COM, " + "       PROVINCIA PROV " + "WHERE  UT.ID_UTE IN ( "
					+ "                      SELECT UT2.ID_UTE " + "                      FROM   DB_UTE UT2 "
					+ "                      WHERE UT2.ID_AZIENDA = ? " + "                      AND UT2.DATA_FINE_ATTIVITA IS NULL "
					+ "                     ) " + "AND    UT.ID_UTE = CP.ID_UTE " + "AND    CP.DATA_FINE_CONDUZIONE IS NULL "
					+ "AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " + "AND    CP.ID_TITOLO_POSSESSO <> 5 " + "AND    CP.ID_TITOLO_POSSESSO <> 6 "
					+ "AND    SP.DATA_FINE_VALIDITA IS NULL " + "AND    SP.COMUNE = COM.ISTAT_COMUNE " + "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA ");
			query = queryBuf.toString();
			/* CONCATENAZIONE/CREAZIONE QUERY END. */

			conn = getDatasource().getConnection();
			if (SolmrLogger.isDebugEnabled(this))
			{
				// Dato che la query costruita dinamicamente è un dato importante la
				// registro sul file di log se il
				// debug è abilitato
				
				SolmrLogger.debug(this, "[ConduzioneParticellaGaaDAO::getIstatProvFromConduzione] Query=" + query);
			}
			stmt = conn.prepareStatement(query);
			stmt.setLong(1, idAzienda);
			// Setto i parametri della query
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next())
			{
				if (vProvince == null)
				{
					vProvince = new Vector<String>();
				}
				vProvince.add(rs.getString("ISTAT_PROVINCIA"));
			}
			
			if (vProvince != null)
			{
				arrProvince = new String[vProvince.size()];
				for (int i = 0; i < vProvince.size(); i++)
				{
					arrProvince[i] = (String) vProvince.get(i);
				}
			}
			
			return arrProvince;
		}
		catch (Throwable t)
		{
			// Vettore di variabili interne del metodo
			Variabile variabili[] = new Variabile[] {
					new Variabile("query", query), new Variabile("queryBuf", queryBuf), new Variabile("vProvince", vProvince)
			};
			
			// Vettore di parametri passati al metodo
			Parametro parametri[] = new Parametro[] {
				new Parametro("idAzienda", idAzienda)
			};
			// Logging dell'eccezione, query, variabili e parametri del metodo
			LoggerUtils.logDAOError(this, "[ConduzioneParticellaGaaDAO::getIstatProvFromConduzione] ", t, query, variabili, parametri);
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
			SolmrLogger.debug(this, "[ConduzioneParticellaGaaDAO::getIstatProvFromConduzione] END.");
		}
	}
	
	/**
	 * 
	 * Restituisce true se esistono conduzioni attive con esito controllo B e W
	 * per l'azienda in questione.
	 * Utilizzato per ricavare le associazioni particelle documento nel popDocumentoParticelle
	 * 
	 * 
	 * 
	 * @param idAzienda
	 * @return
	 * @throws DataAccessException
	 */
	public boolean isConduzionePopParticelleFromEsitoControllo(long idAzienda) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean trovato = false;
    try
    {
      SolmrLogger.debug(this, "[ConduzioneParticellaDAO::isConduzionePopParticelleFromEsitoControllo] BEGIN.");
      
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */

      queryBuf = new StringBuffer();
      queryBuf.append("" +
      		"SELECT CP.ID_CONDUZIONE_PARTICELLA " +
      		"FROM   DB_CONDUZIONE_PARTICELLA CP, " + 
          "       DB_UTE UT " + 
          "WHERE  UT.ID_AZIENDA = ? " + 
          "AND    UT.DATA_FINE_ATTIVITA IS NULL " + 
          "AND    UT.ID_UTE = CP.ID_UTE " + 
          "AND    CP.DATA_INIZIO_CONDUZIONE IS NULL " + 
          "AND    (CP.ESITO_CONTROLLO = 'B' " + 
          "        OR CP.ESITO_CONTROLLO = 'W')");
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
        
        SolmrLogger.debug(this, "[ConduzioneParticellaDAO::isConduzionePopParticelleFromEsitoControllo] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      stmt.setLong(1, idAzienda);
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        trovato = true;
      }
      
      return trovato;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]{
          new Variabile("query", query), 
          new Variabile("queryBuf", queryBuf), 
          new Variabile("trovato", trovato)
      };
      
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]{
        new Parametro("idAzienda", idAzienda)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[ConduzioneParticellaDAO::isConduzionePopParticelleFromEsitoControllo] ", t, query, variabili, parametri);
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
      SolmrLogger.debug(this, "[ConduzioneParticellaDAO::isConduzionePopParticelleFromEsitoControllo] END.");
    }
  }
	
	
	
	
	private HashMap<Long,String> getHashDocumentiFromIdConduzioneParticella(Vector<Long> vIdConduzioneParticella,
      long idAzienda) throws DataAccessException
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
          "[ConduzioneParticellaDAO::getHashDocumentiFromIdConduzioneParticella] BEGIN.");
      
      
     
      conn = getDatasource().getConnection();

      Long idJavaIns = getNextPrimaryKey(SolmrConstants.SEQ_SMRGAA_W_JAVA_INSERT);

      String insert="INSERT INTO SMRGAA_W_JAVA_INSERT (ID_JAVA_INSERT, ID_DETTAGLIO_INS) "+
                    "VALUES ( ?, ?) ";

      stmt = conn.prepareStatement(insert);

      for (int i=0;i<vIdConduzioneParticella.size();i++)
      {
        stmt.setLong(1,idJavaIns.longValue());
        stmt.setLong(2, vIdConduzioneParticella.get(i).longValue());
        stmt.addBatch();
      }
      stmt.executeBatch();

      SolmrLogger.debug(this,"[ConduzioneParticellaDAO::getHashDocumentiFromIdConduzioneParticella] insert executed");

      stmt.close();

      
      
      

      queryBuf = new StringBuffer();

  
      queryBuf.append(
        "SELECT  DOCC.ID_CONDUZIONE_PARTICELLA, " +
        "        TD.DESCRIZIONE, " +       
        "        DOC.NUMERO_PROTOCOLLO, " +
        "        DOC.DATA_PROTOCOLLO " + 
        "FROM    DB_DOCUMENTO_CONDUZIONE DOCC," +
        "        DB_DOCUMENTO DOC, " +
        "        DB_TIPO_DOCUMENTO TD, " +
        "        DB_DOCUMENTO_CATEGORIA DCAT, " +
        "        DB_TIPO_CATEGORIA_DOCUMENTO TCD," +
        "        DB_CONDUZIONE_PARTICELLA CP, " +
        "        SMRGAA_W_JAVA_INSERT WIJ "+
        "WHERE   WIJ.ID_JAVA_INSERT = ? " +
        "AND     WIJ.ID_DETTAGLIO_INS = DOCC.ID_CONDUZIONE_PARTICELLA " +
        "AND     DOCC.ID_DOCUMENTO = DOC.ID_DOCUMENTO " +
        "AND     DOC.EXT_ID_DOCUMENTO = TD.ID_DOCUMENTO " +
        "AND     TD.ID_DOCUMENTO = DCAT.ID_DOCUMENTO " +
        "AND     DCAT.ID_CATEGORIA_DOCUMENTO = TCD.ID_CATEGORIA_DOCUMENTO " +
        "AND     DOC.ID_AZIENDA = ? " +
        "AND     DOC.ID_STATO_DOCUMENTO IS NULL " +
        "AND     NVL(DOC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > SYSDATE " +    
        "AND     TD.ID_TIPOLOGIA_DOCUMENTO = 2 " +
        "AND     TCD.TIPO_IDENTIFICATIVO = 'TP' " +
        "AND     DOCC.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA " +
        "AND     TCD.IDENTIFICATIVO = CP.ID_TITOLO_POSSESSO " +
        "ORDER BY DOCC.ID_CONDUZIONE_PARTICELLA ");
      
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ConduzioneParticellaDAO::getHashDocumentiFromIdConduzioneParticella] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1,idJavaIns.longValue());
      stmt.setLong(2,idAzienda);
      
      ResultSet rs = stmt.executeQuery();
      
      
      while (rs.next())
      {
        if(hmDocumenti == null)
          hmDocumenti = new HashMap<Long,String>();
        
        Long idConduzioneParticella = new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA"));
        
        if(hmDocumenti.get(idConduzioneParticella) != null)
        {
          documento = hmDocumenti.get(idConduzioneParticella)+" - ";
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
          documento += " Prot. n. " + protocollo;
        }
        
        if(Validator.isNotEmpty(dataProtocollo))
        {
          documento += " del "+DateUtils.formatDate(dataProtocollo);
        }
       
        hmDocumenti.put(idConduzioneParticella, documento);
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
      { new Parametro("vIdConduzioneParticella", vIdConduzioneParticella),
        new Parametro("idAzienda", idAzienda) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ConduzioneParticellaDAO::getHashDocumentiFromIdConduzioneParticella] ", t,
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
          "[ConduzioneParticellaDAO::getHashDocumentiFromIdConduzioneParticella] END.");
    }
  }
	
	/**
	 * 
	 * restituisce il minimo valore tra la sup grafica e catastale
	 * normalizzate con la percentauale possesso relativamente alla conduzione
	 * 
	 * 
	 * @param idConduzioneParticella
	 * @return
	 * @throws DataAccessException
	 */
	public double getMaxSupGrafCatIdConduzioneParticella(Long idConduzioneParticella) 
	  throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    double result = 0;
    try
    {
      SolmrLogger.debug(this,
          "[ConduzioneParticellaDAO::getMaxSupGrafCatIdConduzioneParticella] BEGIN.");      
       
      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT (LEAST(DECODE(SP.SUPERFICIE_GRAFICA,0,SP.SUP_CATASTALE,SP.SUPERFICIE_GRAFICA), " +
        "              DECODE(SP.SUP_CATASTALE,0,SP.SUPERFICIE_GRAFICA,SP.SUP_CATASTALE)) * CP.PERCENTUALE_POSSESSO / 100) " +
        "       AS TOT_SUP " +
        "FROM   DB_CONDUZIONE_PARTICELLA CP, " +
        "       DB_STORICO_PARTICELLA SP " +
        "WHERE  CP.ID_CONDUZIONE_PARTICELLA = ? " +
        "AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
        "AND    SP.DATA_FINE_VALIDITA IS NULL");
      
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ConduzioneParticellaDAO::getMaxSupGrafCatIdConduzioneParticella] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1,idConduzioneParticella);
      
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        result = rs.getDouble("TOT_SUP");        
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
      { new Parametro("idConduzioneParticella", idConduzioneParticella) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ConduzioneParticellaDAO::getMaxSupGrafCatIdConduzioneParticella] ", t,
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
          "[ConduzioneParticellaDAO::getMaxSupGrafCatIdConduzioneParticella] END.");
    }
  }
	
	public double getSupGrafNormalizIdConduzioneParticella(Long idConduzioneParticella) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    double result = 0;
    try
    {
      SolmrLogger.debug(this,
          "[ConduzioneParticellaDAO::getSupGrafNormalizIdConduzioneParticella] BEGIN.");      
       
      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT (SP.SUPERFICIE_GRAFICA * CP.PERCENTUALE_POSSESSO / 100) " +
        "       AS TOT_SUP " +
        "FROM   DB_CONDUZIONE_PARTICELLA CP, " +
        "       DB_STORICO_PARTICELLA SP " +
        "WHERE  CP.ID_CONDUZIONE_PARTICELLA = ? " +
        "AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
        "AND    SP.DATA_FINE_VALIDITA IS NULL");
      
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ConduzioneParticellaDAO::getSupGrafNormalizIdConduzioneParticella] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1,idConduzioneParticella);
      
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        result = rs.getDouble("TOT_SUP");        
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
      { new Parametro("idConduzioneParticella", idConduzioneParticella) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ConduzioneParticellaDAO::getSupGrafNormalizIdConduzioneParticella] ", t,
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
          "[ConduzioneParticellaDAO::getSupGrafNormalizIdConduzioneParticella] END.");
    }
  }
	
	public double getSupCatNormalizIdConduzioneParticella(Long idConduzioneParticella) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    double result = 0;
    try
    {
      SolmrLogger.debug(this,
          "[ConduzioneParticellaDAO::getSupCatNormalizIdConduzioneParticella] BEGIN.");      
       
      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT (SP.SUP_CATASTALE * CP.PERCENTUALE_POSSESSO / 100) " +
        "       AS TOT_SUP " +
        "FROM   DB_CONDUZIONE_PARTICELLA CP, " +
        "       DB_STORICO_PARTICELLA SP " +
        "WHERE  CP.ID_CONDUZIONE_PARTICELLA = ? " +
        "AND    CP.ID_PARTICELLA = SP.ID_PARTICELLA " +
        "AND    SP.DATA_FINE_VALIDITA IS NULL");
      
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[ConduzioneParticellaDAO::getSupCatNormalizIdConduzioneParticella] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1,idConduzioneParticella);
      
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        result = rs.getDouble("TOT_SUP");        
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
      { new Parametro("idConduzioneParticella", idConduzioneParticella) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ConduzioneParticellaDAO::getSupCatNormalizIdConduzioneParticella] ", t,
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
          "[ConduzioneParticellaDAO::getSupCatNormalizIdConduzioneParticella] END.");
    }
  }
	
	/**
	 * 
	 * estrae la somma della percentuale possesso della particella
	 * relativa all'azienda
	 * 
	 * 
	 * @param idAzienda
	 * @param idParticella
	 * @return
	 * @throws DataAccessException
	 */
	public BigDecimal getPercentualePosesso(long idAzienda, long idParticella) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    BigDecimal result = null;
    try
    {
      SolmrLogger.debug(this,
          "[ConduzioneParticellaDAO::getPercentualePosesso] BEGIN.");      
       
      queryBuf = new StringBuffer();
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT NVL(SUM(CP.PERCENTUALE_POSSESSO),0) AS SUM_PERCENTUALE " +
        "FROM   DB_CONDUZIONE_PARTICELLA CP, " +
        "       DB_UTE UT " +
        "WHERE  CP.DATA_FINE_CONDUZIONE IS NULL " +
        "AND    CP.ID_TITOLO_POSSESSO <> 5 " +
        "AND    UT.ID_UTE = CP.ID_UTE " +           
        "AND    UT.DATA_FINE_ATTIVITA IS NULL " +             
        "AND    UT.ID_AZIENDA = ? " +
        "AND    CP.ID_PARTICELLA = ? ");
      
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ConduzioneParticellaDAO::getPercentualePosesso] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1,idAzienda);
      stmt.setLong(2,idParticella);
      
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        result = rs.getBigDecimal("SUM_PERCENTUALE");        
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
      { new Parametro("idAzienda", idAzienda),
        new Parametro("idParticella", idParticella) };
  
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ConduzioneParticellaDAO::getPercentualePosesso] ", t,
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
          "[ConduzioneParticellaDAO::getPercentualePosesso] END.");
    }
  } 
	
	
	
	public HashMap<Long,Vector<SuperficieDescription>> getDatiEleggibilitaFittizia(
	    Vector<Long> listIdParticellaCertificata, Connection conn) 
	throws DataAccessException
  {
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    HashMap<Long,Vector<SuperficieDescription>> hSupElegFit = null;
    
    String strIN = " (";
    
    if (listIdParticellaCertificata != null && listIdParticellaCertificata.size() != 0)
    {
      for (int i = 0; i < listIdParticellaCertificata.size(); i++)
      {
        strIN += "?";
        if (i < listIdParticellaCertificata.size() - 1)
          strIN += ",";
      }
    }
    strIN += ") ";
    
    try
    {
      SolmrLogger.debug(this, "[ConduzioneParticellaDAO::getDatiEleggibilitaFittizia] BEGIN.");
      
      // CONCATENAZIONE/CREAZIONE QUERY BEGIN. 

      queryBuf = new StringBuffer();
      queryBuf.append(" " +
        "SELECT PCE.ID_PARTICELLA_CERTIFICATA, " +
        "       1 AS TIPO, " +
        "       PCE.SUPERFICIE, " + 
        "       TEF.DESCRIZIONE " +
        "FROM   DB_PARTICELLA_CERT_ELEG PCE, " + 
        "       DB_TIPO_ELEGGIBILITA_FIT TEF " +
        "WHERE  PCE.ID_PARTICELLA_CERTIFICATA IN "+ strIN +
        "AND    PCE.ANNO_CAMPAGNA = (SELECT MAX(PCE1.ANNO_CAMPAGNA) " +
        "                            FROM DB_PARTICELLA_CERT_ELEG PCE1 " +
        "                            WHERE PCE1.ID_PARTICELLA_CERTIFICATA = PCE.ID_PARTICELLA_CERTIFICATA) " + 
        "AND    PCE.ID_ELEGGIBILITA_FIT = TEF.ID_ELEGGIBILITA_FIT " +
        "UNION " +
        "SELECT EPM.ID_PARTICELLA_CERTIFICATA, " +
        "       2 AS TIPO, " +
        "       EPM.SUPERFICIE, " + 
        "       TF.DESCRIZIONE " +
        "FROM   DB_ESITO_PASCOLO_MAGRO EPM, " + 
        "       DB_TIPO_FONTE TF " +
        "WHERE  EPM.ID_PARTICELLA_CERTIFICATA IN "+ strIN +
        "AND    EPM.ID_FONTE = TF.ID_FONTE " +
        "AND    EPM.DATA_FINE_VALIDITA IS NULL " +
        "AND    EPM.ANNO_CAMPAGNA = (SELECT MAX(EPM1.ANNO_CAMPAGNA) " + 
        "                            FROM   DB_ESITO_PASCOLO_MAGRO EPM1 " + 
        "                            WHERE  EPM1.ID_PARTICELLA_CERTIFICATA = EPM.ID_PARTICELLA_CERTIFICATA )" +
        "ORDER BY ID_PARTICELLA_CERTIFICATA, TIPO, DESCRIZIONE ASC ");
      
      query = queryBuf.toString();
      // CONCATENAZIONE/CREAZIONE QUERY END. 

      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
        
        SolmrLogger.debug(this, "[ConduzioneParticellaDAO::getDatiEleggibilitaFittizia] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      if (listIdParticellaCertificata != null && listIdParticellaCertificata.size() != 0)
      {
        for (int i = 0; i < listIdParticellaCertificata.size(); i++)
          stmt.setLong(++indice, listIdParticellaCertificata.get(i).longValue());
      }
      
      if (listIdParticellaCertificata != null && listIdParticellaCertificata.size() != 0)
      {
        for (int i = 0; i < listIdParticellaCertificata.size(); i++)
          stmt.setLong(++indice, listIdParticellaCertificata.get(i).longValue());
      }
      
      // Setto i parametri della query
      rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(hSupElegFit == null)
        {
          hSupElegFit = new HashMap<Long,Vector<SuperficieDescription>>();
        }
        
        Long idParticellaCertificata = new Long(rs.getLong("ID_PARTICELLA_CERTIFICATA"));
        if(hSupElegFit.get(idParticellaCertificata) != null)
        {
          Vector<SuperficieDescription> vSupDesc = hSupElegFit.get(idParticellaCertificata);
          SuperficieDescription supDesc = new SuperficieDescription();
          supDesc.setSuperficie(rs.getBigDecimal("SUPERFICIE"));
          supDesc.setDescrizione(rs.getString("DESCRIZIONE"));
          vSupDesc.add(supDesc);          
          hSupElegFit.put(idParticellaCertificata, vSupDesc);
        }
        else
        {
          Vector<SuperficieDescription> vSupDesc = new Vector<SuperficieDescription>();
          SuperficieDescription supDesc = new SuperficieDescription();
          supDesc.setSuperficie(rs.getBigDecimal("SUPERFICIE"));
          supDesc.setDescrizione(rs.getString("DESCRIZIONE"));
          vSupDesc.add(supDesc);          
          hSupElegFit.put(idParticellaCertificata, vSupDesc);
        }
      }
      
      return hSupElegFit;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[] {
          new Variabile("query", query), 
          new Variabile("queryBuf", queryBuf), 
          new Variabile("hSupElegFit", hSupElegFit)
      };
      
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[] {
        new Parametro("listIdParticellaCertificata", listIdParticellaCertificata)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[ConduzioneParticellaDAO::getDatiEleggibilitaFittizia] ", t, query, variabili, parametri);
      // Rimappo e rilancio l'eccezione come DataAccessException.
       
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      
       // Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       //* ignora ogni eventuale eccezione)
       
      close(rs, stmt, null);
      
      // Fine metodo
      SolmrLogger.debug(this, "[ConduzioneParticellaDAO::getDatiEleggibilitaFittizia] END.");
    }
  }
	
	
	
}
