package it.csi.solmr.integration.anag;

import it.csi.smranag.smrgaa.dto.SuperficieDescription;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.smranags.sitiserv.dto.SitiPlavVO;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.StringcodeDescription;
import it.csi.solmr.dto.anag.ParticellaCertElegVO;
import it.csi.solmr.dto.anag.ParticellaCertificataVO;
import it.csi.solmr.dto.anag.ParticellaVO;
import it.csi.solmr.dto.anag.ProprietaCertificataVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

public class ParticellaCertificataDAO extends it.csi.solmr.integration.BaseDAO {


	public ParticellaCertificataDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public ParticellaCertificataDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	// Metodo recuperare i dati della particella che abbiamo ricevuto dal SIAN e da ABACO in relazione
	// alla particella selezionata dopo la ricerca particellare/piano colturale
	// DEPRECATO DA NON USARE!
	public ParticellaCertificataVO findParticellaCertificataByParametri(ParticellaVO particellaVO) throws DataAccessException {

		Connection conn = null;
		PreparedStatement stmt = null;
		ParticellaCertificataVO particellaCertificataVO = null;

		try {
			conn = getDatasource().getConnection();

			String query = " SELECT PC.ID_PARTICELLA_CERTIFICATA, " +
						   "        PC.COMUNE, " +
						   "        C.DESCOM, " +
						   "        PC.SEZIONE, " +
						   "        PC.FOGLIO, " +
						   "        PC.PARTICELLA, " +
						   "        PC.ID_ZONA_ALTIMETRICA, " +
						   "        TZA.DESCRIZIONE, " +
						   "        PC.ID_PARTICELLA, " +
						   "        PC.SUP_CATASTALE, " +
						   "        PC.SUP_NON_ELEGGIBILE, " +
						   "        PC.SUP_NE_BOSCO_ACQUE_FABBRICATO, " +
						   "        PC.SUP_NE_FORAGGIERE, " +
						   "        PC.SUP_EL_FRUTTA_GUSCIO, " +
						   "        PC.SUP_EL_PRATO_PASCOLO, " +
						   "        PC.SUP_EL_COLTURE_MISTE, " +
						   "        PC.SUP_COLTIVAZ_ARBOREA_CONS, " +
						   "        PC.SUP_COLTIVAZ_ARBOREA_SPEC, " +
						   "        PC.DATA_FOTO, " +
						   "        PC.TIPO_FOTO, " +
						   "        PC.DATA_CARICAMENTO, " +
						   "        PC.ID_FONTE, " +
						   "        TF.DESCRIZIONE, " +
						   "        PC.DATA_VALIDAZIONE_FONTE_ELEGG, " +
						   "        PC.STATO, " +
						   "        PC.SUBALTERNO, " +
						   "        PC.QUALITA, " +
						   "        PC.CLASSE, " +
						   "        PC.PARTITA, " +
						   "        PC.DATA_ULTIMO_AGGIORNAMENTO, " +
						   "        PC.DENOMINATORE, " +
						   "        PC.DATA_VALIDAZIONE_FONTE_CATASTO, " +
						   "        PC.DATA_INIZIO_VALIDITA, " +
						   "        PC.DATA_FINE_VALIDITA, " +
						   "        PC.ID_QUALITA, " +
						   "        TQ.CODICE AS COD_QUALITA, " +
						   "        TQ.DESCRIZIONE AS DESC_QUALITA, " +
						   "        PC.FLAG_PROVENIENZA, " +
						   "        PC.DATA_SOPPRESSIONE, " +
						   "        PC.SUP_SEMINABILE, " +
						   "        PC.PARTICELLA_A_GIS, " +
						   "        PC.SUP_ACQUE, " +
						   "        PC.SUP_INCOLTI, " +
						   "        PC.SUP_COLT_ARBOREA_SPECIALIZZATA, " +
						   "        PC.SUP_USO_NON_AGRICOLO, " +
						   "        PC.SUP_STRADE, " +
						   "        PC.SUP_COLTIVAZIONE_ARBOREA_CONSO, " +
						   "        PC.SUP_AREA_SCOPERTA, " +
						   "        PC.SUP_AREA_COPERTA, " +
						   "        PC.ESITO " +
						   " FROM   DB_PARTICELLA_CERTIFICATA PC, " +
						   "        COMUNE C, " +
						   "        DB_TIPO_ZONA_ALTIMETRICA TZA, " +
						   "        DB_TIPO_FONTE TF, " +
						   "        DB_TIPO_QUALITA TQ " +
						   " WHERE  C.ISTAT_COMUNE = PC.COMUNE " +
						   " AND    TZA.ID_ZONA_ALTIMETRICA = PC.ID_ZONA_ALTIMETRICA " +
						   " AND    TF.ID_FONTE(+) = PC.ID_FONTE " +
						   " AND    TQ.ID_QUALITA(+) = PC.ID_QUALITA " +
						   " AND    PC.COMUNE = ? " +
						   " AND    PC.FOGLIO = ? ";

			if(Validator.isNotEmpty(particellaVO.getSezione())) {
				query += " AND PC.SEZIONE = ? ";
			}
			if(Validator.isNotEmpty(particellaVO.getParticella())) {
				query += " AND PC.PARTICELLA = ? ";
			}
			if(Validator.isNotEmpty(particellaVO.getSubalterno())) {
				query += " AND PC.SUBALTERNO = ? ";
			}

			stmt = conn.prepareStatement(query);

			int indice = 0;

			stmt.setString(++indice, particellaVO.getIstatComuneParticella());
			stmt.setLong(++indice, particellaVO.getFoglio().longValue());
			if(Validator.isNotEmpty(particellaVO.getSezione())) {
				stmt.setString(++indice, particellaVO.getSezione().toUpperCase());
			}
			if(Validator.isNotEmpty(particellaVO.getParticella())) {
				stmt.setLong(++indice, particellaVO.getParticella().longValue());
			}
			if(Validator.isNotEmpty(particellaVO.getSubalterno())) {
				stmt.setString(++indice, particellaVO.getSubalterno());
			}

			SolmrLogger.debug(this, "Executing findParticellaCertificataByParametri: "+query);

			ResultSet rs = stmt.executeQuery();

			int valoreControllo = 0;

			while(rs.next()) {
				particellaCertificataVO = new ParticellaCertificataVO();
				// Se trovo più di un record significa che ci sono dei problemi o sulla procedura
				// PL-SQL che si occupa di popolare la tabella DB_PARTICELLA_CERTIFICATA o nei dati
				// provenienti dal SIAN quindi restituisco un VO contenente tutti i campi codificati
				// a "non identificata univocamente"
				if(valoreControllo >= 1) {
					particellaCertificataVO.setCertificata(true);
					particellaCertificataVO.setUnivoca(false);
					particellaCertificataVO.setIdParticellaCertificata(null);
					particellaCertificataVO.setIstatComune("non identificata univocamente");
					particellaCertificataVO.setDescrizioneComune("non identificata univocamente");
					particellaCertificataVO.setSezione("non identificata univocamente");
					particellaCertificataVO.setFoglio("non identificata univocamente");
					particellaCertificataVO.setParticella("non identificata univocamente");
					particellaCertificataVO.setZonaAltimetrica(null);
					particellaCertificataVO.setIdParticella(null);
					particellaCertificataVO.setSupCatastaleCertificata("non identificata univocamente");
					particellaCertificataVO.setSupNonEleggibile("non identificata univocamente");
					particellaCertificataVO.setSupNeBoscoAcqueFabbricato("non identificata univocamente");
					particellaCertificataVO.setSupNeForaggiere("non identificata univocamente");
					particellaCertificataVO.setSupElFruttaGuscio("non identificata univocamente");
					particellaCertificataVO.setSupElPratoPascolo("non identificata univocamente");
					particellaCertificataVO.setSupElColtureMiste("non identificata univocamente");
					particellaCertificataVO.setSupColtivazArboreaCons("non identificata univocamente");
					particellaCertificataVO.setSupColtivazArboreaSpec("non identificata univocamente");
					particellaCertificataVO.setDataFoto(null);
					particellaCertificataVO.setTipoFoto("non identificata univocamente");
					particellaCertificataVO.setDataCaricamento(null);
					particellaCertificataVO.setFonteDato(null);
					particellaCertificataVO.setDataValidazioneFonteElegg(null);
					particellaCertificataVO.setStato("non identificata univocamente");
					particellaCertificataVO.setSubalterno("non identificata univocamente");
					particellaCertificataVO.setDataInizioValidita(null);
					particellaCertificataVO.setDataFineValidita(null);
					particellaCertificataVO.setFlagProvenienza("non identificata univocamente");
					particellaCertificataVO.setDataSoppressione(null);
					particellaCertificataVO.setSupSeminabile("non identificata univocamente");
					particellaCertificataVO.setParticellaAGis(null);
					particellaCertificataVO.setSupAcque("non identificata univocamente");
					particellaCertificataVO.setSupIncolti("non identificata univocamente");
					particellaCertificataVO.setSupColtArboreaSpecializzata("non identificata univocamente");
					particellaCertificataVO.setSupUsoNonAgricolo("non identificata univocamente");
					particellaCertificataVO.setSupStrade("non identificata univocamente");
					particellaCertificataVO.setSupColtivazioneArboreaConso("non identificata univocamente");
					particellaCertificataVO.setSupAreaScoperta("non identificata univocamente");
					particellaCertificataVO.setSupAreaCoperta("non identificata univocamente");
					particellaCertificataVO.setEsito(null);
				}
				// Se non si è verificata nessuna delle condizioni precedenti allora restituisco i valori
				// presenti nella tabella
				else {
					particellaCertificataVO.setCertificata(true);
					particellaCertificataVO.setUnivoca(true);
					particellaCertificataVO.setIdParticellaCertificata(new Long(rs.getLong(1)));
					particellaCertificataVO.setIstatComune(rs.getString(2));
					particellaCertificataVO.setDescrizioneComune(rs.getString(3));
					particellaCertificataVO.setSezione(rs.getString(4));
					particellaCertificataVO.setFoglio(rs.getString(5));
					particellaCertificataVO.setParticella(rs.getString(6));
					CodeDescription zonaAltimetrica = new CodeDescription(Integer.decode(rs.getString(7)), rs.getString(8));
					particellaCertificataVO.setZonaAltimetrica(zonaAltimetrica);
					if(Validator.isNotEmpty(rs.getString(9))) {
						particellaCertificataVO.setIdParticella(new Long(rs.getLong(9)));
					}
					else {
						particellaCertificataVO.setIdParticella(null);
					}
					particellaCertificataVO.setSupCatastaleCertificata(StringUtils.parseSuperficieField(rs.getString(10)));
					if(Validator.isNotEmpty(rs.getString(11))) {
						particellaCertificataVO.setSupNonEleggibile(StringUtils.parseSuperficieField(rs.getString(11)));
					}
					else {
						particellaCertificataVO.setSupNonEleggibile(null);
					}
					if(Validator.isNotEmpty(rs.getString(12))) {
						particellaCertificataVO.setSupNeBoscoAcqueFabbricato(StringUtils.parseSuperficieField(rs.getString(12)));
					}
					else {
						particellaCertificataVO.setSupNeBoscoAcqueFabbricato(null);
					}
					if(Validator.isNotEmpty(rs.getString(13))) {
						particellaCertificataVO.setSupNeForaggiere(StringUtils.parseSuperficieField(rs.getString(13)));
					}
					else {
						particellaCertificataVO.setSupNeForaggiere(null);
					}
					if(Validator.isNotEmpty(rs.getString(14))) {
						particellaCertificataVO.setSupElFruttaGuscio(StringUtils.parseSuperficieField(rs.getString(14)));
					}
					else {
						particellaCertificataVO.setSupElFruttaGuscio(null);
					}
					if(Validator.isNotEmpty(rs.getString(15))) {
						particellaCertificataVO.setSupElPratoPascolo(StringUtils.parseSuperficieField(rs.getString(15)));
					}
					else {
						particellaCertificataVO.setSupElPratoPascolo(null);
					}
					if(Validator.isNotEmpty(rs.getString(16))) {
						particellaCertificataVO.setSupElColtureMiste(StringUtils.parseSuperficieField(rs.getString(16)));
					}
					else {
						particellaCertificataVO.setSupElColtureMiste(null);
					}
					if(Validator.isNotEmpty(rs.getString(17))) {
						particellaCertificataVO.setSupColtivazArboreaCons(StringUtils.parseSuperficieField(rs.getString(17)));
					}
					else {
						particellaCertificataVO.setSupColtivazArboreaCons(null);
					}
					if(Validator.isNotEmpty(rs.getString(18))) {
						particellaCertificataVO.setSupColtivazArboreaSpec(StringUtils.parseSuperficieField(rs.getString(18)));
					}
					else {
						particellaCertificataVO.setSupColtivazArboreaSpec(null);
					}
					if(Validator.isNotEmpty(rs.getString(19))) {
						particellaCertificataVO.setDataFoto(rs.getDate(19));
					}
					else {
						particellaCertificataVO.setDataFoto(null);
					}
					particellaCertificataVO.setTipoFoto(rs.getString(20));
					particellaCertificataVO.setDataCaricamento(rs.getDate(21));
					CodeDescription fonteDato =  null;
					if(Validator.isNotEmpty(rs.getString(22))) {
						fonteDato = new CodeDescription(Integer.decode(rs.getString(22)),rs.getString(23));
					}
					particellaCertificataVO.setFonteDato(fonteDato);
					if(Validator.isNotEmpty(rs.getString(24))) {
						particellaCertificataVO.setDataValidazioneFonteElegg(rs.getDate(24));
					}
					else {
						particellaCertificataVO.setDataValidazioneFonteElegg(null);
					}
					particellaCertificataVO.setStato(rs.getString(25));
					particellaCertificataVO.setSubalterno(rs.getString(26));
					particellaCertificataVO.setQualita(rs.getString(27));
					particellaCertificataVO.setClasse(rs.getString(28));
					particellaCertificataVO.setPartita(rs.getString(29));
					if(Validator.isNotEmpty(rs.getString(30))) {
						particellaCertificataVO.setDataUltimoAggiornamento(rs.getDate(30));
					}
					else {
						particellaCertificataVO.setDataUltimoAggiornamento(null);
					}
					particellaCertificataVO.setDenominatore(rs.getString(31));
					if(Validator.isNotEmpty(rs.getString(32))) {
						particellaCertificataVO.setDataValidazioneFonteCatasto(rs.getDate(32));
					}
					else {
						particellaCertificataVO.setDataValidazioneFonteCatasto(null);
					}
					particellaCertificataVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
					particellaCertificataVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
					particellaCertificataVO.setFlagProvenienza(rs.getString("FLAG_PROVENIENZA"));
					particellaCertificataVO.setDataSoppressione(rs.getDate("DATA_SOPPRESSIONE"));
					particellaCertificataVO.setSupSeminabile(rs.getString("SUP_SEMINABILE"));
					if(Validator.isNotEmpty(rs.getString("PARTICELLA_A_GIS"))) {
						particellaCertificataVO.setParticellaAGis(new Integer(rs.getInt("PARTICELLA_A_GIS")));
					}
					particellaCertificataVO.setSupAcque(rs.getString("SUP_ACQUE"));
					particellaCertificataVO.setSupIncolti(rs.getString("SUP_INCOLTI"));
					particellaCertificataVO.setSupColtArboreaSpecializzata(rs.getString("SUP_COLT_ARBOREA_SPECIALIZZATA"));
					particellaCertificataVO.setSupUsoNonAgricolo(rs.getString("SUP_USO_NON_AGRICOLO"));
					particellaCertificataVO.setSupStrade(rs.getString("SUP_STRADE"));
					particellaCertificataVO.setSupColtivazioneArboreaConso(rs.getString("SUP_COLTIVAZIONE_ARBOREA_CONSO"));
					particellaCertificataVO.setSupAreaScoperta(rs.getString("SUP_AREA_SCOPERTA"));
					particellaCertificataVO.setSupAreaCoperta(rs.getString("SUP_AREA_COPERTA"));
					if(Validator.isNotEmpty(rs.getString("ESITO"))) {
						particellaCertificataVO.setEsito(new Integer(rs.getInt("ESITO")));
					}
				}
				valoreControllo++;
			}

			// Se non viene trovato nessun record restituisco un VO contenente tutti i campi
			// codificati a "non presente"
			if(particellaCertificataVO == null) {
				particellaCertificataVO = new ParticellaCertificataVO();
				particellaCertificataVO.setCertificata(false);
				particellaCertificataVO.setIdParticellaCertificata(null);
				particellaCertificataVO.setIstatComune("non presente");
				particellaCertificataVO.setDescrizioneComune("non presente");
				particellaCertificataVO.setSezione("non presente");
				particellaCertificataVO.setFoglio("non presente");
				particellaCertificataVO.setParticella("non presente");
				particellaCertificataVO.setZonaAltimetrica(null);
				particellaCertificataVO.setIdParticella(null);
				particellaCertificataVO.setSupCatastaleCertificata("non presente");
				particellaCertificataVO.setSupNonEleggibile("non presente");
				particellaCertificataVO.setSupNeBoscoAcqueFabbricato("non presente");
				particellaCertificataVO.setSupNeForaggiere("non presente");
				particellaCertificataVO.setSupElFruttaGuscio("non presente");
				particellaCertificataVO.setSupElPratoPascolo("non presente");
				particellaCertificataVO.setSupElColtureMiste("non presente");
				particellaCertificataVO.setSupColtivazArboreaCons("non presente");
				particellaCertificataVO.setSupColtivazArboreaSpec("non presente");
				particellaCertificataVO.setDataFoto(null);
				particellaCertificataVO.setTipoFoto("non presente");
				particellaCertificataVO.setDataCaricamento(null);
				particellaCertificataVO.setFonteDato(null);
				particellaCertificataVO.setDataValidazioneFonteElegg(null);
				particellaCertificataVO.setStato("non presente");
				particellaCertificataVO.setSubalterno("non presente");
				particellaCertificataVO.setDataInizioValidita(null);
				particellaCertificataVO.setDataFineValidita(null);
				particellaCertificataVO.setFlagProvenienza("non presente");
				particellaCertificataVO.setDataSoppressione(null);
				particellaCertificataVO.setSupSeminabile("non presente");
				particellaCertificataVO.setParticellaAGis(null);
				particellaCertificataVO.setSupAcque("non presente");
				particellaCertificataVO.setSupIncolti("non presente");
				particellaCertificataVO.setSupColtArboreaSpecializzata("non presente");
				particellaCertificataVO.setSupUsoNonAgricolo("non presente");
				particellaCertificataVO.setSupStrade("non presente");
				particellaCertificataVO.setSupColtivazioneArboreaConso("non presente");
				particellaCertificataVO.setSupAreaScoperta("non presente");
				particellaCertificataVO.setSupAreaCoperta("non presente");
				particellaCertificataVO.setEsito(null);
			}

			rs.close();
			stmt.close();
		}
		catch (SQLException exc) {
			SolmrLogger.fatal(this, "findParticellaCertificataByParametri - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex) {
			SolmrLogger.fatal(this, "findParticellaCertificataByParametri - Generic Exception: "+ex);
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.fatal(this, "findParticellaCertificataByParametri - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.fatal(this, "findParticellaCertificataByParametri - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return particellaCertificataVO;
	}

	/**
 	 * Metodo che mi restituisce la particella certificata presente su DB_PARTICELLA_CERTIFICATA
	 * in relazione alla chiave logica(comune, sezione, foglio, particella, subalterno)
	 * (Metodo utilizzato da SMRGAASV per servizio serviceGetParticellaCertificata)
	 *
	 * @param istatComune
	 * @param sezione
	 * @param foglio
	 * @param particella
	 * @param subalterno
	 * @param onlyActive
	 * @param dataDichiarazioneConsistenza
	 * @return it.csi.solmr.dto.anag.ParticellaCertificataVO
	 * @throws DataAccessException
	 */
	public ParticellaCertificataVO findParticellaCertificataByParameters(String istatComune, String sezione, String foglio, String particella, String subalterno, boolean onlyActive, java.util.Date dataDichiarazioneConsistenza) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating findParticellaCertificataByParameters method in ParticellaCertificataDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		ParticellaCertificataVO particellaCertificataVO = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in findParticellaCertificataByParameters method in ParticellaCertificataDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in findParticellaCertificataByParameters method in ParticellaCertificataDAO and it values: "+conn+"\n");

			String query = "" +
				 "SELECT  PC.ID_PARTICELLA_CERTIFICATA, " +
			   "        PC.COMUNE, " +
			   "        C.DESCOM, " +
			   "        PC.SEZIONE, " +
			   "        PC.FOGLIO, " +
			   "        PC.PARTICELLA, " +
			   "        PC.ID_ZONA_ALTIMETRICA, " +
			   "        TZA.DESCRIZIONE, " +
			   "        PC.ID_PARTICELLA, " +
			   "        PC.SUP_CATASTALE, " +
			   "        PC.SUP_NON_ELEGGIBILE, " +
			   "        PC.SUP_NE_BOSCO_ACQUE_FABBRICATO, " +
			   "        PC.SUP_NE_FORAGGIERE, " +
			   "        PC.SUP_EL_FRUTTA_GUSCIO, " +
			   "        PC.SUP_EL_PRATO_PASCOLO, " +
			   "        PC.SUP_EL_COLTURE_MISTE, " +
			   "        PC.SUP_COLTIVAZ_ARBOREA_CONS, " +
			   "        PC.SUP_COLTIVAZ_ARBOREA_SPEC, " +
			   "        PC.DATA_FOTO, " +
			   "        PC.TIPO_FOTO, " +
			   "        TTF.DESCRIZIONE AS DESC_FOTO, " +
			   "        PC.DATA_CARICAMENTO, " +
			   "        PC.ID_FONTE, " +
			   "        TF.DESCRIZIONE AS DESC_FONTE, " +
			   "        PC.DATA_VALIDAZIONE_FONTE_ELEGG, " +
			   "        PC.STATO, " +
			   "        PC.SUBALTERNO, " +
			   "        PC.ID_QUALITA, " +
			   "        TQ.CODICE AS COD_QUALITA, " +
			   "        TQ.DESCRIZIONE AS DESC_QUALITA, " +
			   "        PC.CLASSE, " +
			   "        PC.PARTITA, " +
			   "        PC.DATA_ULTIMO_AGGIORNAMENTO, " +
			   "        PC.DENOMINATORE, " +
			   "        PC.DATA_VALIDAZIONE_FONTE_CATASTO, " +
			   "        PC.DATA_INIZIO_VALIDITA, " +
			   "        PC.DATA_FINE_VALIDITA, " +
			   "        PC.FLAG_PROVENIENZA, " +
			   "        PC.DATA_SOPPRESSIONE, " +
			   "        PC.SUP_SEMINABILE, " +
			   "        PC.PARTICELLA_A_GIS, " +
			   "        PC.SUP_ACQUE, " +
			   "        PC.SUP_INCOLTI, " +
			   "        PC.SUP_COLT_ARBOREA_SPECIALIZZATA, " +
			   "        PC.SUP_USO_NON_AGRICOLO, " +
			   "        PC.SUP_STRADE, " +
			   "        PC.SUP_COLTIVAZIONE_ARBOREA_CONSO, " +
			   "        PC.SUP_AREA_SCOPERTA, " +
			   "        PC.SUP_AREA_COPERTA, " +
			   "        PC.ESITO, " +
			   "        PC.ID_FONTE_ELEGG, " +
			   "        PC.SUP_GRAFICA, " + //Nuova eleggibilità
			   "        PC.SUP_USO_GRAFICA, " + //Nuova eleggibilità
			   "        TF2.DESCRIZIONE AS DESC_FONTE_ELEGG " +
			   " FROM   DB_PARTICELLA_CERTIFICATA PC, " +
			   "        COMUNE C, " +
			   "        DB_TIPO_ZONA_ALTIMETRICA TZA, " +
			   "        DB_TIPO_FONTE TF, " +
			   "        DB_TIPO_FONTE TF2, " +
			   "        DB_TIPO_QUALITA TQ, " +
			   "        DB_TIPO_TIPO_FOTO TTF " +
        	   " WHERE  C.ISTAT_COMUNE = PC.COMUNE " +
			   " AND    TZA.ID_ZONA_ALTIMETRICA = PC.ID_ZONA_ALTIMETRICA " +
		       " AND    TF.ID_FONTE(+) = PC.ID_FONTE " +
		       " AND    TF2.ID_FONTE(+) = PC.ID_FONTE_ELEGG " +
		       " AND    TQ.ID_QUALITA(+) = PC.ID_QUALITA " +
		       " AND    TTF.TIPO_FOTO(+) = PC.TIPO_FOTO " +
			   " AND    PC.COMUNE = ? " +
			   " AND    PC.FOGLIO = ? ";

			if(Validator.isNotEmpty(sezione)) {
				query += " AND PC.SEZIONE = ? ";
			}
			else
			{
			  query += " AND PC.SEZIONE IS NULL ";
			}
			if(Validator.isNotEmpty(particella)) {
				query += " AND PC.PARTICELLA = ? ";
			}
			else
      {
        query += " AND PC.PARTICELLA IS NULL ";
      }
			if(Validator.isNotEmpty(subalterno)) {
				query += " AND PC.SUBALTERNO = ? ";
			}
			else
      {
        query += " AND PC.SUBALTERNO IS NULL ";
      }
			if(!onlyActive) {
				query += " AND PC.DATA_INIZIO_VALIDITA <= ? " +
					     " AND NVL(PC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? ";
			}
			else {
				query += " AND PC.DATA_FINE_VALIDITA IS NULL ";
			}

			SolmrLogger.debug(this, "Value of parameter 1 [ISTAT_COMUNE] in findParticellaCertificataByParameters method in ParticellaCertificataDAO: "+istatComune+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [FOGLIO] in findParticellaCertificataByParameters method in ParticellaCertificataDAO: "+foglio+"\n");

			stmt = conn.prepareStatement(query);

			stmt.setString(1, istatComune);
			stmt.setLong(2, Long.decode(foglio).longValue());
			int indice = 2;
			if(Validator.isNotEmpty(sezione)) {
				stmt.setString(++indice, sezione.toUpperCase());
				SolmrLogger.debug(this, "Value of parameter "+indice+" [SEZIONE] in findParticellaCertificataByParameters method in ParticellaCertificataDAO: "+sezione+"\n");
			}
			if(Validator.isNotEmpty(particella)) {
				stmt.setString(++indice, particella);
				SolmrLogger.debug(this, "Value of parameter "+indice+" [PARTICELLA] in findParticellaCertificataByParameters method in ParticellaCertificataDAO: "+particella+"\n");
			}
			if(Validator.isNotEmpty(subalterno)) {
				stmt.setString(++indice, subalterno);
				SolmrLogger.debug(this, "Value of parameter "+indice+" [SUBALTERNO] in findParticellaCertificataByParameters method in ParticellaCertificataDAO: "+subalterno+"\n");
			}
			if(!onlyActive) {
				stmt.setDate(++indice, new java.sql.Date(dataDichiarazioneConsistenza.getTime()));
				SolmrLogger.debug(this, "Value of parameter "+indice+" [DATA_DICHIARAZIONE_CONSISTENZA] in findParticellaCertificataByParameters method in ParticellaCertificataDAO: "+dataDichiarazioneConsistenza+"\n");
				stmt.setDate(++indice, new java.sql.Date(dataDichiarazioneConsistenza.getTime()));
				SolmrLogger.debug(this, "Value of parameter "+indice+" [DATA_DICHIARAZIONE_CONSISTENZA] in findParticellaCertificataByParameters method in ParticellaCertificataDAO: "+dataDichiarazioneConsistenza+"\n");
			}

			SolmrLogger.debug(this, "Executing findParticellaCertificataByParameters: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			int valoreControllo = 0;

			while(rs.next()) {
				particellaCertificataVO = new ParticellaCertificataVO();
				// Se trovo più di un record significa che ci sono dei problemi o sulla procedura
				// PL-SQL che si occupa di popolare la tabella DB_PARTICELLA_CERTIFICATA o nei dati
				// provenienti dal SIAN o da ABACO quindi restituisco un VO che informi che la particella
				// è certificata ma non in modo univoco
				if(valoreControllo >= 1) {
					particellaCertificataVO.setCertificata(true);
					particellaCertificataVO.setUnivoca(false);
				}
				// Se non si è verificata nessuna delle condizioni precedenti allora restituisco i valori
				// presenti nella tabella
				else {
					particellaCertificataVO.setCertificata(true);
					particellaCertificataVO.setUnivoca(true);
					particellaCertificataVO.setIdParticellaCertificata(new Long(rs.getLong("ID_PARTICELLA_CERTIFICATA")));
					particellaCertificataVO.setIstatComune(rs.getString("COMUNE"));
					particellaCertificataVO.setDescrizioneComune(rs.getString("DESCOM"));
					particellaCertificataVO.setSezione(rs.getString("SEZIONE"));
					particellaCertificataVO.setFoglio(rs.getString("FOGLIO"));
					particellaCertificataVO.setParticella(rs.getString("PARTICELLA"));
					CodeDescription zonaAltimetrica = new CodeDescription(Integer.decode(rs.getString("ID_ZONA_ALTIMETRICA")), rs.getString("DESCRIZIONE"));
					particellaCertificataVO.setZonaAltimetrica(zonaAltimetrica);
					if(Validator.isNotEmpty(rs.getString("ID_PARTICELLA"))) {
						particellaCertificataVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
					}
					particellaCertificataVO.setSupCatastaleCertificata(rs.getString("SUP_CATASTALE"));
					particellaCertificataVO.setSupNonEleggibile(rs.getString("SUP_NON_ELEGGIBILE"));
					particellaCertificataVO.setSupNeBoscoAcqueFabbricato(rs.getString("SUP_NE_BOSCO_ACQUE_FABBRICATO"));
					particellaCertificataVO.setSupNeForaggiere(rs.getString("SUP_NE_FORAGGIERE"));
					particellaCertificataVO.setSupElFruttaGuscio(rs.getString("SUP_EL_FRUTTA_GUSCIO"));
					particellaCertificataVO.setSupElPratoPascolo(rs.getString("SUP_EL_PRATO_PASCOLO"));
					particellaCertificataVO.setSupElColtureMiste(rs.getString("SUP_EL_COLTURE_MISTE"));
					particellaCertificataVO.setSupColtivazArboreaCons(rs.getString("SUP_COLTIVAZ_ARBOREA_CONS"));
					particellaCertificataVO.setSupColtivazArboreaSpec(rs.getString("SUP_COLTIVAZ_ARBOREA_SPEC"));
					particellaCertificataVO.setDataFoto(rs.getDate("DATA_FOTO"));
					if(Validator.isNotEmpty(rs.getString("TIPO_FOTO"))) {
						particellaCertificataVO.setTipoFoto(rs.getString("TIPO_FOTO"));
						StringcodeDescription tipologiaFoto = new StringcodeDescription(rs.getString("TIPO_FOTO"), rs.getString("DESC_FOTO"));
						particellaCertificataVO.setTipologiaFoto(tipologiaFoto);
					}
					particellaCertificataVO.setDataCaricamento(rs.getDate("DATA_CARICAMENTO"));
					if(Validator.isNotEmpty(rs.getString("ID_FONTE"))) {
						CodeDescription fonteDato = new CodeDescription(Integer.decode(rs.getString("ID_FONTE")),rs.getString("DESC_FONTE"));
						particellaCertificataVO.setFonteDato(fonteDato);
					}
					particellaCertificataVO.setDataValidazioneFonteElegg(rs.getDate("DATA_VALIDAZIONE_FONTE_ELEGG"));
					particellaCertificataVO.setStato(rs.getString("STATO"));
					particellaCertificataVO.setSubalterno(rs.getString("SUBALTERNO"));
					if(Validator.isNotEmpty(rs.getString("ID_QUALITA"))) {
						particellaCertificataVO.setIdQualita(new Long(rs.getLong("ID_QUALITA")));
						CodeDescription tipoQualita = new CodeDescription(Integer.decode(rs.getString("ID_QUALITA")), rs.getString("DESC_QUALITA"));
						tipoQualita.setSecondaryCode(rs.getString("COD_QUALITA"));
						particellaCertificataVO.setTipoQualita(tipoQualita);
					}
					particellaCertificataVO.setClasse(rs.getString("CLASSE"));
					particellaCertificataVO.setPartita(rs.getString("PARTITA"));
					particellaCertificataVO.setDataUltimoAggiornamento(rs.getDate("DATA_ULTIMO_AGGIORNAMENTO"));
					particellaCertificataVO.setDenominatore(rs.getString("DENOMINATORE"));
					particellaCertificataVO.setDataValidazioneFonteCatasto(rs.getDate("DATA_VALIDAZIONE_FONTE_CATASTO"));
					particellaCertificataVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
					particellaCertificataVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
					particellaCertificataVO.setFlagProvenienza(rs.getString("FLAG_PROVENIENZA"));
					particellaCertificataVO.setDataSoppressione(rs.getDate("DATA_SOPPRESSIONE"));
					particellaCertificataVO.setSupSeminabile(rs.getString("SUP_SEMINABILE"));
					if(Validator.isNotEmpty(rs.getString("PARTICELLA_A_GIS"))) {
						particellaCertificataVO.setParticellaAGis(new Integer(rs.getInt("PARTICELLA_A_GIS")));
					}
					particellaCertificataVO.setSupAcque(rs.getString("SUP_ACQUE"));
					particellaCertificataVO.setSupIncolti(rs.getString("SUP_INCOLTI"));
					particellaCertificataVO.setSupColtArboreaSpecializzata(rs.getString("SUP_COLT_ARBOREA_SPECIALIZZATA"));
					particellaCertificataVO.setSupUsoNonAgricolo(rs.getString("SUP_USO_NON_AGRICOLO"));
					particellaCertificataVO.setSupStrade(rs.getString("SUP_STRADE"));
					particellaCertificataVO.setSupColtivazioneArboreaConso(rs.getString("SUP_COLTIVAZIONE_ARBOREA_CONSO"));
					particellaCertificataVO.setSupAreaScoperta(rs.getString("SUP_AREA_SCOPERTA"));
					particellaCertificataVO.setSupAreaCoperta(rs.getString("SUP_AREA_COPERTA"));
					particellaCertificataVO.setSupGrafica(rs.getString("SUP_GRAFICA")); //nuova eleggibilità
					particellaCertificataVO.setSupUsoGrafica(rs.getString("SUP_USO_GRAFICA")); //nuova eleggibilità
					if(Validator.isNotEmpty(rs.getString("ESITO"))) {
						particellaCertificataVO.setEsito(new Integer(rs.getInt("ESITO")));
					}
					if(Validator.isNotEmpty(rs.getString("ID_FONTE_ELEGG"))) {
						particellaCertificataVO.setIdFonteElegg(new Long(rs.getLong("ID_FONTE_ELEGG")));
						CodeDescription fonteDatoElegg = new CodeDescription(Integer.decode(rs.getString("ID_FONTE_ELEGG")),rs.getString("DESC_FONTE_ELEGG"));
						particellaCertificataVO.setFonteDatoElegg(fonteDatoElegg);
					}
				}
				valoreControllo++;
			}
			// Se non viene trovato nessun record restituisco un VO che informi che la particella
			// non risulta essere certificata da nessuna fonte SIAN o ABACO
			if(particellaCertificataVO == null) {
				particellaCertificataVO = new ParticellaCertificataVO();
				particellaCertificataVO.setCertificata(false);
			}

			rs.close();
			stmt.close();
		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "findParticellaCertificataByParameters - SQLException: "+exc);
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "findParticellaCertificataByParameters - Generic Exception: "+ex);
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "findParticellaCertificataByParameters - SQLException while closing Statement and Connection: "+exc);
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "findParticellaCertificataByParameters - Generic Exception while closing Statement and Connection: "+ex);
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated findParticellaCertificataByParameters method in ParticellaCertificataDAO\n");
		return particellaCertificataVO;
	}
	
	
	/**
	 * 
	 * Estrae i dati della particella certificata alla dichiarazione di consistenza
	 * 
	 * 
	 * @param idParticella
	 * @param dataDichiarazioneConsistenza
	 * @return
	 * @throws DataAccessException
	 */
	public ParticellaCertificataVO findParticellaCertificataAllaDichiarazione
	  (Long idParticella, Date dataDichiarazioneConsistenza) throws DataAccessException 
	{
    SolmrLogger.debug(this, "Invocating findParticellaCertificataAllaDichiarazione method in ParticellaCertificataDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    ParticellaCertificataVO particellaCertificataVO = null;

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in findParticellaCertificataAllaDichiarazione method in ParticellaCertificataDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in findParticellaCertificataAllaDichiarazione method in ParticellaCertificataDAO and it values: "+conn+"\n");

      String query = "" +
         "SELECT  PC.ID_PARTICELLA_CERTIFICATA, " +
         "        PC.COMUNE, " +
         "        C.DESCOM, " +
         "        PC.SEZIONE, " +
         "        PC.FOGLIO, " +
         "        PC.PARTICELLA, " +
         "        PC.ID_ZONA_ALTIMETRICA, " +
         "        TZA.DESCRIZIONE, " +
         "        PC.ID_PARTICELLA, " +
         "        PC.SUP_CATASTALE, " +
         "        PC.SUP_NON_ELEGGIBILE, " +
         "        PC.SUP_NE_BOSCO_ACQUE_FABBRICATO, " +
         "        PC.SUP_NE_FORAGGIERE, " +
         "        PC.SUP_EL_FRUTTA_GUSCIO, " +
         "        PC.SUP_EL_PRATO_PASCOLO, " +
         "        PC.SUP_EL_COLTURE_MISTE, " +
         "        PC.SUP_COLTIVAZ_ARBOREA_CONS, " +
         "        PC.SUP_COLTIVAZ_ARBOREA_SPEC, " +
         "        PC.DATA_FOTO, " +
         "        PC.TIPO_FOTO, " +
         "        TTF.DESCRIZIONE AS DESC_FOTO, " +
         "        PC.DATA_CARICAMENTO, " +
         "        PC.ID_FONTE, " +
         "        TF.DESCRIZIONE AS DESC_FONTE, " +
         "        PC.DATA_VALIDAZIONE_FONTE_ELEGG, " +
         "        PC.STATO, " +
         "        PC.SUBALTERNO, " +
         "        PC.ID_QUALITA, " +
         "        TQ.CODICE AS COD_QUALITA, " +
         "        TQ.DESCRIZIONE AS DESC_QUALITA, " +
         "        PC.CLASSE, " +
         "        PC.PARTITA, " +
         "        PC.DATA_ULTIMO_AGGIORNAMENTO, " +
         "        PC.DENOMINATORE, " +
         "        PC.DATA_VALIDAZIONE_FONTE_CATASTO, " +
         "        PC.DATA_INIZIO_VALIDITA, " +
         "        PC.DATA_FINE_VALIDITA, " +
         "        PC.FLAG_PROVENIENZA, " +
         "        PC.DATA_SOPPRESSIONE, " +
         "        PC.SUP_SEMINABILE, " +
         "        PC.PARTICELLA_A_GIS, " +
         "        PC.SUP_ACQUE, " +
         "        PC.SUP_INCOLTI, " +
         "        PC.SUP_COLT_ARBOREA_SPECIALIZZATA, " +
         "        PC.SUP_USO_NON_AGRICOLO, " +
         "        PC.SUP_STRADE, " +
         "        PC.SUP_COLTIVAZIONE_ARBOREA_CONSO, " +
         "        PC.SUP_AREA_SCOPERTA, " +
         "        PC.SUP_AREA_COPERTA, " +
         "        PC.ESITO, " +
         "        PC.ID_FONTE_ELEGG, " +
         "        PC.SUP_GRAFICA, " + //Nuova eleggibilità
         "        PC.SUP_USO_GRAFICA, " + //Nuova eleggibilità
         "        TF2.DESCRIZIONE AS DESC_FONTE_ELEGG " +
         " FROM   DB_PARTICELLA_CERTIFICATA PC, " +
         "        COMUNE C, " +
         "        DB_TIPO_ZONA_ALTIMETRICA TZA, " +
         "        DB_TIPO_FONTE TF, " +
         "        DB_TIPO_FONTE TF2, " +
         "        DB_TIPO_QUALITA TQ, " +
         "        DB_TIPO_TIPO_FOTO TTF " +
         " WHERE  C.ISTAT_COMUNE = PC.COMUNE " +
         " AND    TZA.ID_ZONA_ALTIMETRICA = PC.ID_ZONA_ALTIMETRICA " +
         " AND    TF.ID_FONTE(+) = PC.ID_FONTE " +
         " AND    TF2.ID_FONTE(+) = PC.ID_FONTE_ELEGG " +
         " AND    TQ.ID_QUALITA(+) = PC.ID_QUALITA " +
         " AND    TTF.TIPO_FOTO(+) = PC.TIPO_FOTO " +
         " AND    PC.ID_PARTICELLA = ? " +
         " AND PC.DATA_INIZIO_VALIDITA <= ? " +
         " AND NVL(PC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? ";
      
      
      stmt = conn.prepareStatement(query);
      
      int indice = 0;      
      stmt.setLong(++indice, idParticella.longValue());
      stmt.setTimestamp(++indice, convertDateToTimestamp(dataDichiarazioneConsistenza));
      stmt.setTimestamp(++indice, convertDateToTimestamp(dataDichiarazioneConsistenza));
      

      SolmrLogger.debug(this, "Executing findParticellaCertificataAllaDichiarazione: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      int valoreControllo = 0;

      while(rs.next()) 
      {
        particellaCertificataVO = new ParticellaCertificataVO();
        // Se trovo più di un record significa che ci sono dei problemi o sulla procedura
        // PL-SQL che si occupa di popolare la tabella DB_PARTICELLA_CERTIFICATA o nei dati
        // provenienti dal SIAN o da ABACO quindi restituisco un VO che informi che la particella
        // è certificata ma non in modo univoco
        if(valoreControllo >= 1) 
        {
          particellaCertificataVO.setCertificata(true);
          particellaCertificataVO.setUnivoca(false);
        }
        // Se non si è verificata nessuna delle condizioni precedenti allora restituisco i valori
        // presenti nella tabella
        else 
        {
          particellaCertificataVO.setCertificata(true);
          particellaCertificataVO.setUnivoca(true);
          particellaCertificataVO.setIdParticellaCertificata(new Long(rs.getLong("ID_PARTICELLA_CERTIFICATA")));
          particellaCertificataVO.setIstatComune(rs.getString("COMUNE"));
          particellaCertificataVO.setDescrizioneComune(rs.getString("DESCOM"));
          particellaCertificataVO.setSezione(rs.getString("SEZIONE"));
          particellaCertificataVO.setFoglio(rs.getString("FOGLIO"));
          particellaCertificataVO.setParticella(rs.getString("PARTICELLA"));
          CodeDescription zonaAltimetrica = new CodeDescription(Integer.decode(rs.getString("ID_ZONA_ALTIMETRICA")), rs.getString("DESCRIZIONE"));
          particellaCertificataVO.setZonaAltimetrica(zonaAltimetrica);
          if(Validator.isNotEmpty(rs.getString("ID_PARTICELLA"))) {
            particellaCertificataVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
          }
          particellaCertificataVO.setSupCatastaleCertificata(rs.getString("SUP_CATASTALE"));
          particellaCertificataVO.setSupNonEleggibile(rs.getString("SUP_NON_ELEGGIBILE"));
          particellaCertificataVO.setSupNeBoscoAcqueFabbricato(rs.getString("SUP_NE_BOSCO_ACQUE_FABBRICATO"));
          particellaCertificataVO.setSupNeForaggiere(rs.getString("SUP_NE_FORAGGIERE"));
          particellaCertificataVO.setSupElFruttaGuscio(rs.getString("SUP_EL_FRUTTA_GUSCIO"));
          particellaCertificataVO.setSupElPratoPascolo(rs.getString("SUP_EL_PRATO_PASCOLO"));
          particellaCertificataVO.setSupElColtureMiste(rs.getString("SUP_EL_COLTURE_MISTE"));
          particellaCertificataVO.setSupColtivazArboreaCons(rs.getString("SUP_COLTIVAZ_ARBOREA_CONS"));
          particellaCertificataVO.setSupColtivazArboreaSpec(rs.getString("SUP_COLTIVAZ_ARBOREA_SPEC"));
          particellaCertificataVO.setDataFoto(rs.getDate("DATA_FOTO"));
          if(Validator.isNotEmpty(rs.getString("TIPO_FOTO"))) {
            particellaCertificataVO.setTipoFoto(rs.getString("TIPO_FOTO"));
            StringcodeDescription tipologiaFoto = new StringcodeDescription(rs.getString("TIPO_FOTO"), rs.getString("DESC_FOTO"));
            particellaCertificataVO.setTipologiaFoto(tipologiaFoto);
          }
          particellaCertificataVO.setDataCaricamento(rs.getDate("DATA_CARICAMENTO"));
          if(Validator.isNotEmpty(rs.getString("ID_FONTE"))) {
            CodeDescription fonteDato = new CodeDescription(Integer.decode(rs.getString("ID_FONTE")),rs.getString("DESC_FONTE"));
            particellaCertificataVO.setFonteDato(fonteDato);
          }
          particellaCertificataVO.setDataValidazioneFonteElegg(rs.getDate("DATA_VALIDAZIONE_FONTE_ELEGG"));
          particellaCertificataVO.setStato(rs.getString("STATO"));
          particellaCertificataVO.setSubalterno(rs.getString("SUBALTERNO"));
          if(Validator.isNotEmpty(rs.getString("ID_QUALITA"))) {
            particellaCertificataVO.setIdQualita(new Long(rs.getLong("ID_QUALITA")));
            CodeDescription tipoQualita = new CodeDescription(Integer.decode(rs.getString("ID_QUALITA")), rs.getString("DESC_QUALITA"));
            tipoQualita.setSecondaryCode(rs.getString("COD_QUALITA"));
            particellaCertificataVO.setTipoQualita(tipoQualita);
          }
          particellaCertificataVO.setClasse(rs.getString("CLASSE"));
          particellaCertificataVO.setPartita(rs.getString("PARTITA"));
          particellaCertificataVO.setDataUltimoAggiornamento(rs.getDate("DATA_ULTIMO_AGGIORNAMENTO"));
          particellaCertificataVO.setDenominatore(rs.getString("DENOMINATORE"));
          particellaCertificataVO.setDataValidazioneFonteCatasto(rs.getDate("DATA_VALIDAZIONE_FONTE_CATASTO"));
          particellaCertificataVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
          particellaCertificataVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
          particellaCertificataVO.setFlagProvenienza(rs.getString("FLAG_PROVENIENZA"));
          particellaCertificataVO.setDataSoppressione(rs.getDate("DATA_SOPPRESSIONE"));
          particellaCertificataVO.setSupSeminabile(rs.getString("SUP_SEMINABILE"));
          if(Validator.isNotEmpty(rs.getString("PARTICELLA_A_GIS"))) {
            particellaCertificataVO.setParticellaAGis(new Integer(rs.getInt("PARTICELLA_A_GIS")));
          }
          particellaCertificataVO.setSupAcque(rs.getString("SUP_ACQUE"));
          particellaCertificataVO.setSupIncolti(rs.getString("SUP_INCOLTI"));
          particellaCertificataVO.setSupColtArboreaSpecializzata(rs.getString("SUP_COLT_ARBOREA_SPECIALIZZATA"));
          particellaCertificataVO.setSupUsoNonAgricolo(rs.getString("SUP_USO_NON_AGRICOLO"));
          particellaCertificataVO.setSupStrade(rs.getString("SUP_STRADE"));
          particellaCertificataVO.setSupColtivazioneArboreaConso(rs.getString("SUP_COLTIVAZIONE_ARBOREA_CONSO"));
          particellaCertificataVO.setSupAreaScoperta(rs.getString("SUP_AREA_SCOPERTA"));
          particellaCertificataVO.setSupAreaCoperta(rs.getString("SUP_AREA_COPERTA"));
          particellaCertificataVO.setSupGrafica(rs.getString("SUP_GRAFICA")); //nuova eleggibilità
          particellaCertificataVO.setSupUsoGrafica(rs.getString("SUP_USO_GRAFICA")); //nuova eleggibilità
          if(Validator.isNotEmpty(rs.getString("ESITO"))) {
            particellaCertificataVO.setEsito(new Integer(rs.getInt("ESITO")));
          }
          if(Validator.isNotEmpty(rs.getString("ID_FONTE_ELEGG"))) {
            particellaCertificataVO.setIdFonteElegg(new Long(rs.getLong("ID_FONTE_ELEGG")));
            CodeDescription fonteDatoElegg = new CodeDescription(Integer.decode(rs.getString("ID_FONTE_ELEGG")),rs.getString("DESC_FONTE_ELEGG"));
            particellaCertificataVO.setFonteDatoElegg(fonteDatoElegg);
          }
        }
        valoreControllo++;
      }
      // Se non viene trovato nessun record restituisco un VO che informi che la particella
      // non risulta essere certificata da nessuna fonte SIAN o ABACO
      if(particellaCertificataVO == null) {
        particellaCertificataVO = new ParticellaCertificataVO();
        particellaCertificataVO.setCertificata(false);
      }

      rs.close();
      stmt.close();
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "findParticellaCertificataAllaDichiarazione - SQLException: "+exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "findParticellaCertificataAllaDichiarazione - Generic Exception: "+ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "findParticellaCertificataAllaDichiarazione - SQLException while closing Statement and Connection: "+exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "findParticellaCertificataAllaDichiarazione - Generic Exception while closing Statement and Connection: "+ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated findParticellaCertificataAllaDichiarazione method in ParticellaCertificataDAO\n");
    return particellaCertificataVO;
  }


	public void updateParticellaCertificata(SitiPlavVO[] part,ParticellaVO[] particelle)
	  throws DataAccessException
	{
		SolmrLogger.debug(this, "updateParticellaCertificata BEGIN");

		Connection conn = null;
		PreparedStatement stmt = null;
    try
    {
      String sql = "UPDATE DB_PARTICELLA_CERTIFICATA "+
                   "  SET "+
                   "  DATA_ULTIMA_LAVORAZIONE = ?, "+
                   "  STATO_ULTIMA_LAVORAZIONE = ? "+
                   "WHERE  COMUNE = ? "+
                   "   AND NVL(SEZIONE,'#') = NVL(?,'#') "+
                   "   AND FOGLIO = ? "+
                   "   AND PARTICELLA = ? "+
                   "   AND NVL(SUBALTERNO,'#')	= NVL(?,'#') ";

			conn = getDatasource().getConnection();

			stmt = conn.prepareStatement(sql);

			for (int i=0;i<particelle.length;i++)
			{
				stmt.setTimestamp(1, this.convertDateToTimestamp(part[i].getDataLav()));
				if (part[i].getStato()!=null)
					stmt.setString(2, part[i].getStato().toString());
				else  stmt.setString(2, null);
				stmt.setString(3, particelle[i].getIstatComuneParticella());
				stmt.setString(4, particelle[i].getSezione());
				if (particelle[i].getFoglio()!=null)
					stmt.setLong(5, particelle[i].getFoglio().longValue());
				else  stmt.setString(5, null);
				if (particelle[i].getParticella()!=null)
					stmt.setLong(6, particelle[i].getParticella().longValue());
				else  stmt.setString(6, null);
				stmt.setString(7, particelle[i].getSubalterno());

				stmt.addBatch();
			}

			SolmrLogger.debug(this, "updateParticellaCertificata Esecuzione della query : "+sql);

			stmt.executeBatch();
		}
		catch(SQLException exc)
		{
			SolmrLogger.error(this, "updateParticellaCertificata in ParticellaCertificataDAO - SQLException: "+exc);
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex) {
			SolmrLogger.error(this, "updateParticellaCertificata in ParticellaCertificataDAO - Generic Exception: "+ex);
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
				SolmrLogger.error(this, "updateParticellaCertificata in ParticellaCertificataDAO - SQLException while closing Statement and Connection: "+exc);
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex)
			{
				SolmrLogger.error(this, "updateParticellaCertificata in ParticellaCertificataDAO - Generic Exception while closing Statement and Connection: "+ex);
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated updateParticellaCertificata method in ParticellaCertificataDAO\n");
	}
	
	
	
	public void updateParticellaCertificataFrazAcc(ParticellaCertificataVO particellaCertificataVO)
    throws DataAccessException
  {
    SolmrLogger.debug(this, "updateParticellaCertificataFrazAcc BEGIN");

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      String sql = "UPDATE DB_PARTICELLA_CERTIFICATA "+
                   "  SET DATA_ULTIMO_AGGIORNAMENTO = SYSDATE, " +
                   "      PARTICELLA_A_GIS = 0, "+
                   "      SUP_GRAFICA = 0, " +
                   "      SUP_USO_GRAFICA = 0 "+
                   "WHERE  ID_PARTICELLA_CERTIFICATA = ? ";

      conn = getDatasource().getConnection();

      stmt = conn.prepareStatement(sql);
      int indice = 0;

      stmt.setLong(++indice, particellaCertificataVO.getIdParticellaCertificata());

      SolmrLogger.debug(this, "updateParticellaCertificataFrazAcc Esecuzione della query : "+sql);

      stmt.executeUpdate();

      

      SolmrLogger.debug(this, "updateParticellaCertificataFrazAcc Esecuzione della query : "+sql);

      stmt.executeBatch();
    }
    catch(SQLException exc)
    {
      SolmrLogger.error(this, "updateParticellaCertificataFrazAcc in ParticellaCertificataDAO - SQLException: "+exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.error(this, "updateParticellaCertificataFrazAcc in ParticellaCertificataDAO - Generic Exception: "+ex);
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
        SolmrLogger.error(this, "updateParticellaCertificataFrazAcc in ParticellaCertificataDAO - SQLException while closing Statement and Connection: "+exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex)
      {
        SolmrLogger.error(this, "updateParticellaCertificataFrazAcc in ParticellaCertificataDAO - Generic Exception while closing Statement and Connection: "+ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated updateParticellaCertificataFrazAcc method in ParticellaCertificataDAO\n");
  }

	/**
	 * Metodo effettuato per creare un legame tra DB_STORICO_PARTICELLA e DB_PARTICELLA_CERTIFICATA
	 * da non usare se non in inserimento particella.
	 * 
	 * @param particellaCertificataVO
	 * @throws DataAccessException
	 */
	public void allineaParticellaCertificata(ParticellaCertificataVO particellaCertificataVO) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating allineaParticellaCertificata method in ParticellaCertificataDAO\n");
	    Connection conn = null;
	    PreparedStatement stmt = null;

	    try {
	    	SolmrLogger.debug(this, "Creating db-connection in allineaParticellaCertificata method in ParticellaCertificataDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in allineaParticellaCertificata method in ParticellaCertificataDAO and it values: "+conn+"\n");

			String query = " UPDATE DB_PARTICELLA_CERTIFICATA "+
	                       " SET    ID_PARTICELLA = ? " +
	                       " WHERE  ID_PARTICELLA_CERTIFICATA = ? ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing allineaParticellaCertificata: "+query);

			stmt.setLong(1, particellaCertificataVO.getIdParticella().longValue());
			SolmrLogger.debug(this, "Value of parameter 1 [ID_PARTICELLA] in method allineaParticellaCertificata in ParticellaCertificataDAO: "+particellaCertificataVO.getIdParticella().longValue()+"\n");
			stmt.setLong(2, particellaCertificataVO.getIdParticellaCertificata().longValue());
			SolmrLogger.debug(this, "Value of parameter 2 [ID_PARTICELLA_CERTIFICATA] in method allineaParticellaCertificata in ParticellaCertificataDAO: "+particellaCertificataVO.getIdParticellaCertificata().longValue()+"\n");

			stmt.executeUpdate();

			stmt.close();
	    }
	    catch(SQLException exc) {
	    	SolmrLogger.error(this, "allineaParticellaCertificata in ParticellaCertificataDAO - SQLException: "+exc);
	    	throw new DataAccessException(exc.getMessage());
	    }
	    catch (Exception ex) {
	    	SolmrLogger.error(this, "allineaParticellaCertificata in ParticellaCertificataDAO - Generic Exception: "+ex);
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
	    		SolmrLogger.error(this, "allineaParticellaCertificata in ParticellaCertificataDAO - SQLException while closing Statement and Connection: "+exc);
	    		throw new DataAccessException(exc.getMessage());
	    	}
	    	catch(Exception ex) {
	    		SolmrLogger.error(this, "allineaParticellaCertificata in ParticellaCertificataDAO - Generic Exception while closing Statement and Connection: "+ex);
	    		throw new DataAccessException(ex.getMessage());
	    	}
	    }
	    SolmrLogger.debug(this, "Invocated allineaParticellaCertificata method in ParticellaCertificataDAO\n");
	}
	
	
	/**
	 * Ricavo un Vector di ParticellaCertElegVO passando come unico parametro idParticellaCertificata
	 * 
	 * @param idParticellaCertificata
	 * @return
	 * @throws DataAccessException
	 */
  public Vector<ParticellaCertElegVO> getEleggibilitaByIdPartCertificata(Long idParticellaCertificata) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getEleggibilitaByIdPartCertificata method in ParticellaCertificataDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaCertElegVO> vParticellaCertEleg = null;

    try {
      SolmrLogger.debug(this, "Creating db-connection in getEleggibilitaByIdPartCertificata method in ParticellaCertificataDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getEleggibilitaByIdPartCertificata method in ParticellaCertificataDAO and it values: "+conn+"\n");

      String query = " SELECT " +
               "        PCE.ID_PARTICELLA_CERT_ELEG, " +
               "        PCE.ID_PARTICELLA_CERTIFICATA, "+
               "        PCE.ID_ELEGGIBILITA, " +
               "        PCE.ID_ELEGGIBILITA_FIT, " +
               "        PCE.SUPERFICIE, " +
               "        TEF.DESCRIZIONE AS DESCRIZIONE_FIT, " +
               "        TE.DESCRIZIONE AS DESCRIZIONE " +
               " FROM   DB_PARTICELLA_CERT_ELEG PCE, " +
               "        DB_TIPO_ELEGGIBILITA_FIT TEF, " +
               "        DB_TIPO_ELEGGIBILITA TE " +
               " WHERE  PCE.ID_PARTICELLA_CERTIFICATA = ? "+
               " AND    PCE.ID_ELEGGIBILITA = TE.ID_ELEGGIBILITA " +
               " AND    PCE.ANNO_CAMPAGNA = (SELECT MAX(PCE1.ANNO_CAMPAGNA)" +
               "                              FROM DB_PARTICELLA_CERT_ELEG PCE1 " +
               "                              WHERE PCE1.ID_PARTICELLA_CERTIFICATA = ? ) " + 
               " AND    PCE.ID_ELEGGIBILITA_FIT = TEF.ID_ELEGGIBILITA_FIT (+) " +
               " ORDER BY TE.DESCRIZIONE, TEF.DESCRIZIONE ";

      

      SolmrLogger.debug(this, "Value of parameter 1 [ID_PARTICELLA_CERTIFICATA] in getEleggibilitaByIdPartCertificata method in ParticellaCertificataDAO: "+idParticellaCertificata+"\n");

      stmt = conn.prepareStatement(query);
      stmt.setLong(1, idParticellaCertificata.longValue());
      stmt.setLong(2, idParticellaCertificata.longValue());

      SolmrLogger.debug(this, "Executing getEleggibilitaByIdPartCertificata: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        if(vParticellaCertEleg == null)
        {
          vParticellaCertEleg = new Vector<ParticellaCertElegVO>();
        }
        ParticellaCertElegVO particellaCertElegVO = new ParticellaCertElegVO();
        particellaCertElegVO.setIdParticellaCertEleg(rs.getLong("ID_PARTICELLA_CERT_ELEG"));
        particellaCertElegVO.setIdParticellaCertificata(rs.getLong("ID_PARTICELLA_CERTIFICATA"));
        particellaCertElegVO.setIdEleggibilita(rs.getLong("ID_ELEGGIBILITA"));
        particellaCertElegVO.setIdEleggibilitaFit(checkLongNull(rs.getString("ID_ELEGGIBILITA_FIT")));
        particellaCertElegVO.setSuperficie(rs.getBigDecimal("SUPERFICIE"));
        particellaCertElegVO.setDescrizioneFit(rs.getString("DESCRIZIONE_FIT"));
        particellaCertElegVO.setDescrizione(rs.getString("DESCRIZIONE"));
        
        vParticellaCertEleg.add(particellaCertElegVO);
        
      }
      

      rs.close();
      stmt.close();
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getEleggibilitaByIdPartCertificata - SQLException: "+exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getEleggibilitaByIdPartCertificata - Generic Exception: "+ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getEleggibilitaByIdPartCertificata - SQLException while closing Statement and Connection: "+exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getEleggibilitaByIdPartCertificata - Generic Exception while closing Statement and Connection: "+ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getEleggibilitaByIdPartCertificata method in ParticellaCertificataDAO\n");
    return vParticellaCertEleg;
  }
  
  /**
   * ricavo eleggibilita ed eleggibilità fittizie alla dichiarazione
   * di consistenza
   * 
   * 
   * 
   * @param idParticellaCertificata
   * @return
   * @throws DataAccessException
   */
  public Vector<ParticellaCertElegVO> getEleggibilitaAllaDichiarazione(Long idParticella, Long codiceFotografiaTerreni) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getEleggibilitaAllaDichiarazione method in ParticellaCertificataDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaCertElegVO> vParticellaCertEleg = null;

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getEleggibilitaAllaDichiarazione method in ParticellaCertificataDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getEleggibilitaAllaDichiarazione method in ParticellaCertificataDAO and it values: "+conn+"\n");

      String query = " SELECT " +
               "        ED.ID_PARTICELLA_CERTIFICATA, "+
               "        ED.ID_ELEGGIBILITA, " +
               "        ED.ID_ELEGGIBILITA_FIT, " +
               "        ED.SUPERFICIE, " +
               "        TEF.DESCRIZIONE AS DESCRIZIONE_FIT, " +
               "        TE.DESCRIZIONE AS DESCRIZIONE " +
               " FROM   DB_ELEGGIBILITA_DICHIARATA ED, " +
               "        DB_TIPO_ELEGGIBILITA_FIT TEF, " +
               "        DB_TIPO_ELEGGIBILITA TE " +
               " WHERE  ED.ID_PARTICELLA = ? " +
               " AND    ED.CODICE_FOTOGRAFIA_TERRENI = ? " +
               " AND    ED.ID_ELEGGIBILITA = TE.ID_ELEGGIBILITA " +
               " AND    ED.ID_ELEGGIBILITA_FIT = TEF.ID_ELEGGIBILITA_FIT (+) " +
               " ORDER BY TE.DESCRIZIONE, TEF.DESCRIZIONE ";

      stmt = conn.prepareStatement(query);
      stmt.setLong(1, idParticella.longValue());
      stmt.setLong(2, codiceFotografiaTerreni.longValue());

      SolmrLogger.debug(this, "Executing getEleggibilitaAllaDichiarazione: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        if(vParticellaCertEleg == null)
        {
          vParticellaCertEleg = new Vector<ParticellaCertElegVO>();
        }
        ParticellaCertElegVO particellaCertElegVO = new ParticellaCertElegVO();
        particellaCertElegVO.setIdParticellaCertificata(rs.getLong("ID_PARTICELLA_CERTIFICATA"));
        particellaCertElegVO.setIdEleggibilita(rs.getLong("ID_ELEGGIBILITA"));
        particellaCertElegVO.setIdEleggibilitaFit(checkLongNull(rs.getString("ID_ELEGGIBILITA_FIT")));
        particellaCertElegVO.setSuperficie(rs.getBigDecimal("SUPERFICIE"));
        particellaCertElegVO.setDescrizioneFit(rs.getString("DESCRIZIONE_FIT"));
        particellaCertElegVO.setDescrizione(rs.getString("DESCRIZIONE"));
        
        vParticellaCertEleg.add(particellaCertElegVO);
        
      }
      

      rs.close();
      stmt.close();
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getEleggibilitaAllaDichiarazione - SQLException: "+exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getEleggibilitaAllaDichiarazione - Generic Exception: "+ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getEleggibilitaAllaDichiarazione - SQLException while closing Statement and Connection: "+exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getEleggibilitaAllaDichiarazione - Generic Exception while closing Statement and Connection: "+ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getEleggibilitaAllaDichiarazione method in ParticellaCertificataDAO\n");
    return vParticellaCertEleg;
  }
  
  
  
  public Vector<ParticellaCertElegVO> getEleggibilitaByIdParticella(long idParticella) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getEleggibilitaByIdParticella method in ParticellaCertificataDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ParticellaCertElegVO> vParticellaCertEleg = null;

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getEleggibilitaByIdParticella method in ParticellaCertificataDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getEleggibilitaByIdParticella method in ParticellaCertificataDAO and it values: "+conn+"\n");

      String query = " " +
      		"SELECT PCE.ID_ELEGGIBILITA_FIT, " + 
          "       PCE.SUPERFICIE " +
          "FROM   DB_PARTICELLA_CERTIFICATA PC, " +
          "       DB_PARTICELLA_CERT_ELEG PCE " +
          "WHERE  PC.ID_PARTICELLA = ? " +
          "AND    PC.DATA_FINE_VALIDITA IS NULL " +
          "AND    PC.ID_PARTICELLA_CERTIFICATA = PCE.ID_PARTICELLA_CERTIFICATA " +
          "AND    PCE.ANNO_CAMPAGNA = (SELECT MAX(PCE1.ANNO_CAMPAGNA) " +
          "                            FROM DB_PARTICELLA_CERT_ELEG PCE1 " +
          "                            WHERE PCE1.ID_PARTICELLA_CERTIFICATA = PCE.ID_PARTICELLA_CERTIFICATA)";

      

      SolmrLogger.debug(this, "Value of parameter 1 [ID_PARTICELLA] in getEleggibilitaByIdParticella method in ParticellaCertificataDAO: "+idParticella+"\n");

      stmt = conn.prepareStatement(query);
      stmt.setLong(1, idParticella);

      SolmrLogger.debug(this, "Executing getEleggibilitaByIdParticella: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        if(vParticellaCertEleg == null)
        {
          vParticellaCertEleg = new Vector<ParticellaCertElegVO>();
        }
        ParticellaCertElegVO particellaCertElegVO = new ParticellaCertElegVO();
        particellaCertElegVO.setIdEleggibilitaFit(checkLongNull(rs.getString("ID_ELEGGIBILITA_FIT")));
        particellaCertElegVO.setSuperficie(rs.getBigDecimal("SUPERFICIE"));
        
        vParticellaCertEleg.add(particellaCertElegVO);
        
      }
      

      rs.close();
      stmt.close();
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getEleggibilitaByIdParticella - SQLException: "+exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getEleggibilitaByIdParticella - Generic Exception: "+ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getEleggibilitaByIdParticella - SQLException while closing Statement and Connection: "+exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getEleggibilitaByIdParticella - Generic Exception while closing Statement and Connection: "+ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getEleggibilitaByIdParticella method in ParticellaCertificataDAO\n");
    return vParticellaCertEleg;
  }
  
  
  /**
   * 
   * restituisce reletivamente all'id_particella gli usi fittizi del suolo
   * e il pascolo magro
   * 
   * 
   * 
   * @param listIdParticella
   * @return
   * @throws DataAccessException
   */
  public HashMap<Long,Vector<SuperficieDescription>> getEleggibilitaTooltipByIdParticella(
      Vector<Long> listIdParticella) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getEleggibilitaTooltipByIdParticella method in ParticellaCertificataDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    
    HashMap<Long,Vector<SuperficieDescription>> hSupElegFit = null;
    
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
      SolmrLogger.debug(this, "Creating db-connection in getEleggibilitaTooltipByIdParticella method in ParticellaCertificataDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getEleggibilitaTooltipByIdParticella method in ParticellaCertificataDAO and it values: "+conn+"\n");
  
      String query = " " +
        "SELECT PC.ID_PARTICELLA, " +
        "       1 AS TIPO, " +
        "       PCE.SUPERFICIE, " + 
        "       TEF.DESCRIZIONE " +
        "FROM   DB_PARTICELLA_CERT_ELEG PCE, " + 
        "       DB_TIPO_ELEGGIBILITA_FIT TEF, " +
        "       DB_PARTICELLA_CERTIFICATA PC " +
        "WHERE  PC.ID_PARTICELLA IN "+ strIN +
        "AND    PC.DATA_FINE_VALIDITA IS NULL " +
        "AND    PC.ID_PARTICELLA_CERTIFICATA = PCE.ID_PARTICELLA_CERTIFICATA " +
        "AND    PCE.ANNO_CAMPAGNA = (SELECT MAX(PCE1.ANNO_CAMPAGNA) " +
        "                            FROM DB_PARTICELLA_CERT_ELEG PCE1 " +
        "                            WHERE PCE1.ID_PARTICELLA_CERTIFICATA = PCE.ID_PARTICELLA_CERTIFICATA) " + 
        "AND    PCE.ID_ELEGGIBILITA_FIT = TEF.ID_ELEGGIBILITA_FIT " +
        "UNION " +
        "SELECT PC.ID_PARTICELLA, " +
        "       2 AS TIPO, " +
        "       EPM.SUPERFICIE, " + 
        "       TF.DESCRIZIONE " +
        "FROM   DB_ESITO_PASCOLO_MAGRO EPM, " + 
        "       DB_TIPO_FONTE TF, " +
        "       DB_PARTICELLA_CERTIFICATA PC " +
        "WHERE  PC.ID_PARTICELLA IN "+ strIN +
        "AND    PC.DATA_FINE_VALIDITA IS NULL " +
        "AND    PC.ID_PARTICELLA_CERTIFICATA = EPM.ID_PARTICELLA_CERTIFICATA " +
        "AND    EPM.ID_FONTE = TF.ID_FONTE " +
        "AND    EPM.DATA_FINE_VALIDITA IS NULL " +
        "AND    EPM.ANNO_CAMPAGNA = (SELECT MAX(EPM1.ANNO_CAMPAGNA) " + 
        "                            FROM   DB_ESITO_PASCOLO_MAGRO EPM1 " + 
        "                            WHERE  EPM1.ID_PARTICELLA_CERTIFICATA = EPM.ID_PARTICELLA_CERTIFICATA ) " +
        "ORDER BY ID_PARTICELLA, TIPO, DESCRIZIONE ASC";
      
  
      stmt = conn.prepareStatement(query);
      int indice = 0;
      
      if (listIdParticella != null && listIdParticella.size() != 0)
      {
        for (int i = 0; i < listIdParticella.size(); i++)
          stmt.setLong(++indice, listIdParticella.get(i).longValue());
      }
      
      if (listIdParticella != null && listIdParticella.size() != 0)
      {
        for (int i = 0; i < listIdParticella.size(); i++)
          stmt.setLong(++indice, listIdParticella.get(i).longValue());
      }
  
      SolmrLogger.debug(this, "Executing getEleggibilitaTooltipByIdParticella: "+query+"\n");
  
      ResultSet rs = stmt.executeQuery();
  
      while(rs.next()) 
      {
        if(hSupElegFit == null)
        {
          hSupElegFit = new HashMap<Long,Vector<SuperficieDescription>>();
        }
        
        Long idParticella = new Long(rs.getLong("ID_PARTICELLA"));
        if(hSupElegFit.get(idParticella) != null)
        {
          Vector<SuperficieDescription> vSupDesc = hSupElegFit.get(idParticella);
          SuperficieDescription supDesc = new SuperficieDescription();
          supDesc.setSuperficie(rs.getBigDecimal("SUPERFICIE"));
          supDesc.setDescrizione(rs.getString("DESCRIZIONE"));
          vSupDesc.add(supDesc);          
          hSupElegFit.put(idParticella, vSupDesc);
        }
        else
        {
          Vector<SuperficieDescription> vSupDesc = new Vector<SuperficieDescription>();
          SuperficieDescription supDesc = new SuperficieDescription();
          supDesc.setSuperficie(rs.getBigDecimal("SUPERFICIE"));
          supDesc.setDescrizione(rs.getString("DESCRIZIONE"));
          vSupDesc.add(supDesc);          
          hSupElegFit.put(idParticella, vSupDesc);
        }
        
      }
      
  
      rs.close();
      stmt.close();
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getEleggibilitaTooltipByIdParticella - SQLException: "+exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getEleggibilitaTooltipByIdParticella - Generic Exception: "+ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getEleggibilitaTooltipByIdParticella - SQLException while closing Statement and Connection: "+exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getEleggibilitaTooltipByIdParticella - Generic Exception while closing Statement and Connection: "+ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getEleggibilitaTooltipByIdParticella method in ParticellaCertificataDAO\n");
    return hSupElegFit;
  }
  
  
  
  
  public Vector<Vector<ParticellaCertElegVO>> getListStoricoParticellaCertEleg(long idParticella)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<Vector<ParticellaCertElegVO>> result = null;
    Vector<ParticellaCertElegVO> vPartCert = null;
    StringBuffer queryBuf = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaCertificataDAO::getListStoricoParticellaCertEleg] BEGIN.");
      
      queryBuf = new StringBuffer();
      
      queryBuf.append(
        "SELECT " +
        "       PCE.ID_PARTICELLA_CERT_ELEG, " +
        "       PCE.ID_PARTICELLA_CERTIFICATA, "+
        "       PCE.ID_ELEGGIBILITA, " +
        "       PCE.ID_ELEGGIBILITA_FIT, " +
        "       PCE.SUPERFICIE, " +
        "       TEF.DESCRIZIONE AS DESCRIZIONE_FIT, " +
        "       TE.DESCRIZIONE AS DESCRIZIONE, " +
        "       PCE.ANNO_CAMPAGNA " +
        "FROM   DB_PARTICELLA_CERTIFICATA PC, " +
        "       DB_PARTICELLA_CERT_ELEG PCE, " +
        "       DB_TIPO_ELEGGIBILITA_FIT TEF, " +
        "       DB_TIPO_ELEGGIBILITA TE " +
        "WHERE  PC.ID_PARTICELLA = ? " +
        "AND    PC.DATA_FINE_VALIDITA IS NULL " +
        "AND    PC.ID_PARTICELLA_CERTIFICATA = PCE.ID_PARTICELLA_CERTIFICATA "+
        "AND    PCE.ID_ELEGGIBILITA = TE.ID_ELEGGIBILITA " +
        "AND    PCE.ID_ELEGGIBILITA_FIT = TEF.ID_ELEGGIBILITA_FIT (+) " +
        "ORDER BY " +
        "       PCE.ANNO_CAMPAGNA DESC, " +
        "       TEF.DESCRIZIONE ASC");
       
      
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[ParticellaCertificataDAO::getListStoricoParticellaCertEleg] Query="
                    + query);
      }
      
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idParticella);      
      int annoCampagnaTmp = -1;
      ResultSet rs = stmt.executeQuery();
      boolean primo = true;
      while (rs.next())
      {
        if(result == null)
        {
          result = new Vector<Vector<ParticellaCertElegVO>>();
          vPartCert = new Vector<ParticellaCertElegVO>();
        }
        int annoCampagna = rs.getInt("ANNO_CAMPAGNA");
        if(!primo && (annoCampagnaTmp != annoCampagna))
        {
          result.add(vPartCert);
          vPartCert = new Vector<ParticellaCertElegVO>();          
        }
        
        ParticellaCertElegVO particellaCertElegVO = new ParticellaCertElegVO();
        particellaCertElegVO.setIdParticellaCertEleg(rs.getLong("ID_PARTICELLA_CERT_ELEG"));
        particellaCertElegVO.setIdParticellaCertificata(rs.getLong("ID_PARTICELLA_CERTIFICATA"));
        particellaCertElegVO.setIdEleggibilita(rs.getLong("ID_ELEGGIBILITA"));
        particellaCertElegVO.setIdEleggibilitaFit(checkLongNull(rs.getString("ID_ELEGGIBILITA_FIT")));
        particellaCertElegVO.setSuperficie(rs.getBigDecimal("SUPERFICIE"));
        particellaCertElegVO.setDescrizioneFit(rs.getString("DESCRIZIONE_FIT"));
        particellaCertElegVO.setDescrizione(rs.getString("DESCRIZIONE"));
        particellaCertElegVO.setAnnoCampagna(annoCampagna);
        annoCampagnaTmp = annoCampagna;
        primo = false;
        
        vPartCert.add(particellaCertElegVO);
        
        
      }
      
      if(result != null)
      {
        result.add(vPartCert);
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query), 
        new Variabile("result", result)};
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idParticella", idParticella) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaCertificataDAO::getListStoricoParticellaCertEleg] ", t,
          query, variabili, parametri);
    
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
      SolmrLogger.debug(this,
          "[ParticellaCertificataDAO::getListStoricoParticellaCertEleg] END.");
    }
  }
  
  
  
  
  
  /**
   * 
   * ricavo la particella certificata con l'idParticella
   * 
   * 
   * 
   * @param idParticella
   * @param dataDichiarazioneConsistenza
   * @return
   * @throws DataAccessException
   */
  public ParticellaCertificataVO findParticellaCertificataByIdParticella(Long idParticella, 
      Date dataDichiarazioneConsistenza) 
  throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating findParticellaCertificataByIdParticella method in ParticellaCertificataDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    ParticellaCertificataVO particellaCertificataVO = null;

    try {
      SolmrLogger.debug(this, "Creating db-connection in findParticellaCertificataByIdParticella method in ParticellaCertificataDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in findParticellaCertificataByIdParticella method in ParticellaCertificataDAO and it values: "+conn+"\n");

      String query = "" +
         "SELECT  PC.ID_PARTICELLA_CERTIFICATA, " +
         "        PC.COMUNE, " +
         "        C.DESCOM, " +
         "        PC.SEZIONE, " +
         "        PC.FOGLIO, " +
         "        PC.PARTICELLA, " +
         "        PC.ID_ZONA_ALTIMETRICA, " +
         "        TZA.DESCRIZIONE, " +
         "        PC.ID_PARTICELLA, " +
         "        PC.SUP_CATASTALE, " +
         "        PC.SUP_NON_ELEGGIBILE, " +
         "        PC.SUP_NE_BOSCO_ACQUE_FABBRICATO, " +
         "        PC.SUP_NE_FORAGGIERE, " +
         "        PC.SUP_EL_FRUTTA_GUSCIO, " +
         "        PC.SUP_EL_PRATO_PASCOLO, " +
         "        PC.SUP_EL_COLTURE_MISTE, " +
         "        PC.SUP_COLTIVAZ_ARBOREA_CONS, " +
         "        PC.SUP_COLTIVAZ_ARBOREA_SPEC, " +
         "        PC.DATA_FOTO, " +
         "        PC.TIPO_FOTO, " +
         "        TTF.DESCRIZIONE AS DESC_FOTO, " +
         "        PC.DATA_CARICAMENTO, " +
         "        PC.ID_FONTE, " +
         "        TF.DESCRIZIONE AS DESC_FONTE, " +
         "        PC.DATA_VALIDAZIONE_FONTE_ELEGG, " +
         "        PC.STATO, " +
         "        PC.SUBALTERNO, " +
         "        PC.ID_QUALITA, " +
         "        TQ.CODICE AS COD_QUALITA, " +
         "        TQ.DESCRIZIONE AS DESC_QUALITA, " +
         "        PC.CLASSE, " +
         "        PC.PARTITA, " +
         "        PC.DATA_ULTIMO_AGGIORNAMENTO, " +
         "        PC.DENOMINATORE, " +
         "        PC.DATA_VALIDAZIONE_FONTE_CATASTO, " +
         "        PC.DATA_INIZIO_VALIDITA, " +
         "        PC.DATA_FINE_VALIDITA, " +
         "        PC.FLAG_PROVENIENZA, " +
         "        PC.DATA_SOPPRESSIONE, " +
         "        PC.SUP_SEMINABILE, " +
         "        PC.PARTICELLA_A_GIS, " +
         "        PC.SUP_ACQUE, " +
         "        PC.SUP_INCOLTI, " +
         "        PC.SUP_COLT_ARBOREA_SPECIALIZZATA, " +
         "        PC.SUP_USO_NON_AGRICOLO, " +
         "        PC.SUP_STRADE, " +
         "        PC.SUP_COLTIVAZIONE_ARBOREA_CONSO, " +
         "        PC.SUP_AREA_SCOPERTA, " +
         "        PC.SUP_AREA_COPERTA, " +
         "        PC.ESITO, " +
         "        PC.ID_FONTE_ELEGG, " +
         "        PC.SUP_GRAFICA, " + //Nuova eleggibilità
         "        PC.SUP_USO_GRAFICA, " + //Nuova eleggibilità
         "        TF2.DESCRIZIONE AS DESC_FONTE_ELEGG " +
         " FROM   DB_PARTICELLA_CERTIFICATA PC, " +
         "        COMUNE C, " +
         "        DB_TIPO_ZONA_ALTIMETRICA TZA, " +
         "        DB_TIPO_FONTE TF, " +
         "        DB_TIPO_FONTE TF2, " +
         "        DB_TIPO_QUALITA TQ, " +
         "        DB_TIPO_TIPO_FOTO TTF " +
         " WHERE  C.ISTAT_COMUNE = PC.COMUNE " +
         " AND    TZA.ID_ZONA_ALTIMETRICA = PC.ID_ZONA_ALTIMETRICA " +
         " AND    TF.ID_FONTE(+) = PC.ID_FONTE " +
         " AND    TF2.ID_FONTE(+) = PC.ID_FONTE_ELEGG " +
         " AND    TQ.ID_QUALITA(+) = PC.ID_QUALITA " +
         " AND    TTF.TIPO_FOTO(+) = PC.TIPO_FOTO " +
         " AND    PC.ID_PARTICELLA = ? ";

     
      if(Validator.isNotEmpty(dataDichiarazioneConsistenza)) 
      {
        query += " AND PC.DATA_INIZIO_VALIDITA <= ? " +
               " AND NVL(PC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > ? ";
      }
      else 
      {
        query += " AND PC.DATA_FINE_VALIDITA IS NULL ";
      }
      
      stmt = conn.prepareStatement(query);

      int indice = 0;
      stmt.setLong(++indice, idParticella.longValue());
      if(Validator.isNotEmpty(dataDichiarazioneConsistenza))
      {
        stmt.setDate(++indice, new java.sql.Date(dataDichiarazioneConsistenza.getTime()));
        stmt.setDate(++indice, new java.sql.Date(dataDichiarazioneConsistenza.getTime()));
      }

      SolmrLogger.debug(this, "Executing findParticellaCertificataByIdParticella: "+query+"\n");

      ResultSet rs = stmt.executeQuery();
      if(rs.next()) 
      {
        particellaCertificataVO = new ParticellaCertificataVO();
        
        
        particellaCertificataVO.setCertificata(true);
        particellaCertificataVO.setUnivoca(true);
        particellaCertificataVO.setIdParticellaCertificata(new Long(rs.getLong("ID_PARTICELLA_CERTIFICATA")));
        particellaCertificataVO.setIstatComune(rs.getString("COMUNE"));
        particellaCertificataVO.setDescrizioneComune(rs.getString("DESCOM"));
        particellaCertificataVO.setSezione(rs.getString("SEZIONE"));
        particellaCertificataVO.setFoglio(rs.getString("FOGLIO"));
        particellaCertificataVO.setParticella(rs.getString("PARTICELLA"));
        CodeDescription zonaAltimetrica = new CodeDescription(Integer.decode(rs.getString("ID_ZONA_ALTIMETRICA")), rs.getString("DESCRIZIONE"));
        particellaCertificataVO.setZonaAltimetrica(zonaAltimetrica);
        if(Validator.isNotEmpty(rs.getString("PARTICELLA"))) {
          particellaCertificataVO.setIdParticella(new Long(rs.getLong("PARTICELLA")));
        }
        particellaCertificataVO.setSupCatastaleCertificata(rs.getString("SUP_CATASTALE"));
        particellaCertificataVO.setSupNonEleggibile(rs.getString("SUP_NON_ELEGGIBILE"));
        particellaCertificataVO.setSupNeBoscoAcqueFabbricato(rs.getString("SUP_NE_BOSCO_ACQUE_FABBRICATO"));
        particellaCertificataVO.setSupNeForaggiere(rs.getString("SUP_NE_FORAGGIERE"));
        particellaCertificataVO.setSupElFruttaGuscio(rs.getString("SUP_EL_FRUTTA_GUSCIO"));
        particellaCertificataVO.setSupElPratoPascolo(rs.getString("SUP_EL_PRATO_PASCOLO"));
        particellaCertificataVO.setSupElColtureMiste(rs.getString("SUP_EL_COLTURE_MISTE"));
        particellaCertificataVO.setSupColtivazArboreaCons(rs.getString("SUP_COLTIVAZ_ARBOREA_CONS"));
        particellaCertificataVO.setSupColtivazArboreaSpec(rs.getString("SUP_COLTIVAZ_ARBOREA_SPEC"));
        particellaCertificataVO.setDataFoto(rs.getDate("DATA_FOTO"));
        if(Validator.isNotEmpty(rs.getString("TIPO_FOTO"))) {
          particellaCertificataVO.setTipoFoto(rs.getString("TIPO_FOTO"));
          StringcodeDescription tipologiaFoto = new StringcodeDescription(rs.getString("TIPO_FOTO"), rs.getString("DESC_FOTO"));
          particellaCertificataVO.setTipologiaFoto(tipologiaFoto);
        }
        particellaCertificataVO.setDataCaricamento(rs.getDate("DATA_CARICAMENTO"));
        if(Validator.isNotEmpty(rs.getString("ID_FONTE"))) {
          CodeDescription fonteDato = new CodeDescription(Integer.decode(rs.getString("ID_FONTE")),rs.getString("DESC_FONTE"));
          particellaCertificataVO.setFonteDato(fonteDato);
        }
        particellaCertificataVO.setDataValidazioneFonteElegg(rs.getDate("DATA_VALIDAZIONE_FONTE_ELEGG"));
        particellaCertificataVO.setStato(rs.getString("STATO"));
        particellaCertificataVO.setSubalterno(rs.getString("SUBALTERNO"));
        if(Validator.isNotEmpty(rs.getString("ID_QUALITA"))) {
          particellaCertificataVO.setIdQualita(new Long(rs.getLong("ID_QUALITA")));
          CodeDescription tipoQualita = new CodeDescription(Integer.decode(rs.getString("ID_QUALITA")), rs.getString("DESC_QUALITA"));
          tipoQualita.setSecondaryCode(rs.getString("COD_QUALITA"));
          particellaCertificataVO.setTipoQualita(tipoQualita);
        }
        particellaCertificataVO.setClasse(rs.getString("CLASSE"));
        particellaCertificataVO.setPartita(rs.getString("PARTITA"));
        particellaCertificataVO.setDataUltimoAggiornamento(rs.getDate("DATA_ULTIMO_AGGIORNAMENTO"));
        particellaCertificataVO.setDenominatore(rs.getString("DENOMINATORE"));
        particellaCertificataVO.setDataValidazioneFonteCatasto(rs.getDate("DATA_VALIDAZIONE_FONTE_CATASTO"));
        particellaCertificataVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        particellaCertificataVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        particellaCertificataVO.setFlagProvenienza(rs.getString("FLAG_PROVENIENZA"));
        particellaCertificataVO.setDataSoppressione(rs.getDate("DATA_SOPPRESSIONE"));
        particellaCertificataVO.setSupSeminabile(rs.getString("SUP_SEMINABILE"));
        if(Validator.isNotEmpty(rs.getString("PARTICELLA_A_GIS"))) {
          particellaCertificataVO.setParticellaAGis(new Integer(rs.getInt("PARTICELLA_A_GIS")));
        }
        particellaCertificataVO.setSupAcque(rs.getString("SUP_ACQUE"));
        particellaCertificataVO.setSupIncolti(rs.getString("SUP_INCOLTI"));
        particellaCertificataVO.setSupColtArboreaSpecializzata(rs.getString("SUP_COLT_ARBOREA_SPECIALIZZATA"));
        particellaCertificataVO.setSupUsoNonAgricolo(rs.getString("SUP_USO_NON_AGRICOLO"));
        particellaCertificataVO.setSupStrade(rs.getString("SUP_STRADE"));
        particellaCertificataVO.setSupColtivazioneArboreaConso(rs.getString("SUP_COLTIVAZIONE_ARBOREA_CONSO"));
        particellaCertificataVO.setSupAreaScoperta(rs.getString("SUP_AREA_SCOPERTA"));
        particellaCertificataVO.setSupAreaCoperta(rs.getString("SUP_AREA_COPERTA"));
        particellaCertificataVO.setSupGrafica(rs.getString("SUP_GRAFICA")); //nuova eleggibilità
        particellaCertificataVO.setSupUsoGrafica(rs.getString("SUP_USO_GRAFICA")); //nuova eleggibilità
        if(Validator.isNotEmpty(rs.getString("ESITO"))) {
          particellaCertificataVO.setEsito(new Integer(rs.getInt("ESITO")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_FONTE_ELEGG"))) {
          particellaCertificataVO.setIdFonteElegg(new Long(rs.getLong("ID_FONTE_ELEGG")));
          CodeDescription fonteDatoElegg = new CodeDescription(Integer.decode(rs.getString("ID_FONTE_ELEGG")),rs.getString("DESC_FONTE_ELEGG"));
          particellaCertificataVO.setFonteDatoElegg(fonteDatoElegg);
        }
        
      }

      rs.close();
      stmt.close();
    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "findParticellaCertificataByIdParticella - SQLException: "+exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "findParticellaCertificataByIdParticella - Generic Exception: "+ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "findParticellaCertificataByIdParticella - SQLException while closing Statement and Connection: "+exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "findParticellaCertificataByParameters - Generic Exception while closing Statement and Connection: "+ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    
    SolmrLogger.debug(this, "Invocated findParticellaCertificataByIdParticella method in ParticellaCertificataDAO\n");
    return particellaCertificataVO;
  }
  
  
  
  /**
   * 
   * Etraggo data fotointepretazione del gis
   * Devono essere entrambi i parametri valorizzati!!!!
   * 
   * 
   * 
   * @param idParticellaCertificata
   * @param dataRichiestaRiesame
   * @return
   * @throws DataAccessException
   */
  public Date getDataFotoInterpretazione(long idParticellaCertificata, Date dataRichiestaRiesame)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Date result = null;
    StringBuffer queryBuf = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaCertificataDAO::getDataFotoInterpretazione] BEGIN.");
      
      queryBuf = new StringBuffer();
      
      queryBuf.append(
          "SELECT MAX(DPC.DATA_FOTOINTERPRETAZIONE) AS DATA_FOTOINTERPRETAZIONE " +
          "FROM   DB_PARTICELLA_CERT_ELEG DPC " +
          "WHERE  DPC.ID_PARTICELLA_CERTIFICATA = ? " +
          "AND    DPC.DATA_FOTOINTERPRETAZIONE  >= ? " +
          "AND    DPC.ANNO_CAMPAGNA = (SELECT MAX(DPC1.ANNO_CAMPAGNA) " +
          "                            FROM   DB_PARTICELLA_CERT_ELEG DPC1 " +
          "                            WHERE  DPC1.ID_PARTICELLA_CERTIFICATA = ? " +
          "                            AND    DPC1.ANNO_CAMPAGNA >= TO_NUMBER(TO_CHAR(?,'YYYY')) " +
          "                           ) ");
      
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[ParticellaCertificataDAO::getDataFotoInterpretazione] Query="
                    + query);
      }
      
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      int indice = 0;
      stmt.setLong(++indice, idParticellaCertificata);
      stmt.setTimestamp(++indice, convertDateToTimestamp(dataRichiestaRiesame));
      stmt.setLong(++indice, idParticellaCertificata);
      stmt.setTimestamp(++indice, convertDateToTimestamp(dataRichiestaRiesame));
    
      ResultSet rs = stmt.executeQuery();
      if(rs.next())
      {   
        result = rs.getTimestamp("DATA_FOTOINTERPRETAZIONE");
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query), 
        new Variabile("result", result)};
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idParticellaCertificata", idParticellaCertificata),
          new Parametro("dataRichiestaRiesame", dataRichiestaRiesame) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[ParticellaCertificataDAO::getDataFotoInterpretazione] ", t,
          query, variabili, parametri);
    
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
      SolmrLogger.debug(this,
          "[ParticellaCertificataDAO::getDataFotoInterpretazione] END.");
    }
  }
  
  
  /**
   * 
   * mi ritorna la superifice vitata eleggibile
   * 
   * 
   * @param idParticella
   * @return
   * @throws DataAccessException
   */
  public BigDecimal getEleggibilitaVitataByIdParticella(long idParticella) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getEleggibilitaVitataByIdParticella method in ParticellaCertificataDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    BigDecimal supVitataGis = new BigDecimal(0);

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getEleggibilitaVitataByIdParticella method in ParticellaCertificataDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getEleggibilitaVitataByIdParticella method in ParticellaCertificataDAO and it values: "+conn+"\n");

      String query = " " +
          "SELECT PCE.SUPERFICIE " +
          "FROM   DB_PARTICELLA_CERTIFICATA PC, " +
          "       DB_PARTICELLA_CERT_ELEG PCE " +
          "WHERE  PC.ID_PARTICELLA = ? " +
          "AND    PC.DATA_FINE_VALIDITA IS NULL " +
          "AND    PC.ID_PARTICELLA_CERTIFICATA = PCE.ID_PARTICELLA_CERTIFICATA " +
          "AND    PCE.ID_ELEGGIBILITA_FIT = "+SolmrConstants.ELEGGIBILITA_FIT_VINO+" "+
          "AND    PCE.ANNO_CAMPAGNA = (SELECT MAX(PCE1.ANNO_CAMPAGNA) " +
          "                            FROM DB_PARTICELLA_CERT_ELEG PCE1 " +
          "                            WHERE PCE1.ID_PARTICELLA_CERTIFICATA = PCE.ID_PARTICELLA_CERTIFICATA" +
          "                            AND PCE1.ID_ELEGGIBILITA_FIT = "+SolmrConstants.ELEGGIBILITA_FIT_VINO+" "+")";

      

      SolmrLogger.debug(this, "Value of parameter 1 [ID_PARTICELLA] in getEleggibilitaVitataByIdParticella method in ParticellaCertificataDAO: "+idParticella+"\n");

      stmt = conn.prepareStatement(query);
      stmt.setLong(1, idParticella);

      SolmrLogger.debug(this, "Executing getEleggibilitaVitataByIdParticella: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      if(rs.next()) 
      {        
        supVitataGis = rs.getBigDecimal("SUPERFICIE");
      }
      

      rs.close();
      stmt.close();
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getEleggibilitaVitataByIdParticella - SQLException: "+exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getEleggibilitaVitataByIdParticella - Generic Exception: "+ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getEleggibilitaVitataByIdParticella - SQLException while closing Statement and Connection: "+exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getEleggibilitaVitataByIdParticella - Generic Exception while closing Statement and Connection: "+ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getEleggibilitaVitataByIdParticella method in ParticellaCertificataDAO\n");
    
    return supVitataGis;
  }
  
  
  public void storicizzaProprietaCertificata(long idProprietaCertificata, long idUtente)
    throws DataAccessException
  {
    SolmrLogger.debug(this, "storicizzaProprietaCertificata BEGIN");

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      String sql = 
        "UPDATE DB_PROPRIETA_CERTIFICATA "+
        "SET    DATA_FINE_VALIDITA = SYSDATE, " +
        "       DATA_AGGIORNAMENTO = SYSDATE, "+
        "       ID_UTENTE_AGGIORNAMENTO = ? "+
        "WHERE  ID_PROPRIETA_CERTIFICATA = ? ";

      conn = getDatasource().getConnection();

      stmt = conn.prepareStatement(sql);
      int indice = 0;

      stmt.setLong(++indice, idUtente);
      stmt.setLong(++indice, idProprietaCertificata);

      SolmrLogger.debug(this, "storicizzaProprietaCertificata Esecuzione della query : "+sql);

      stmt.executeUpdate();
    }
    catch(SQLException exc)
    {
      SolmrLogger.error(this, "storicizzaProprietaCertificata in ParticellaCertificataDAO - SQLException: "+exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.error(this, "storicizzaProprietaCertificata in ParticellaCertificataDAO - Generic Exception: "+ex);
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
        SolmrLogger.error(this, "storicizzaProprietaCertificata in ParticellaCertificataDAO - SQLException while closing Statement and Connection: "+exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex)
      {
        SolmrLogger.error(this, "storicizzaProprietaCertificata in ParticellaCertificataDAO - Generic Exception while closing Statement and Connection: "+ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated storicizzaProprietaCertificata method in ParticellaCertificataDAO\n");
  }
  
  
  public Vector<ProprietaCertificataVO> getListProprietaCertifByIdParticella(long idParticella) 
     throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getListProprietaCertifByIdParticella method in ParticellaCertificataDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<ProprietaCertificataVO> vIdProprietaCertificata = null;
    ProprietaCertificataVO proprietaCertificataVO = null;

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getListProprietaCertifByIdParticella method in ParticellaCertificataDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListProprietaCertifByIdParticella method in ParticellaCertificataDAO and it values: "+conn+"\n");

      String query = 
        "SELECT PROC.ID_PROPRIETA_CERTIFICATA, " +
        "       PROC.ID_PARTICELLA_CERTIFICATA, " +
        "       DECODE(SO.FLAG_FISICO,'N',PG.CODICE_FISCALE, PF.CODICE_FISCALE) AS CUAA, " +
        "       PROC.ID_TIPO_DIRITTO, " +
        "       PROC.PERCENTUALE_POSSESSO, " +
        "       PROC.DATA_INIZIO_TITOLARITA " +
        "FROM   DB_PROPRIETA_CERTIFICATA PROC, " +
        "       DB_PARTICELLA_CERTIFICATA PC," +
        "       DB_SOGGETTO SO, " +
        "       DB_PERSONA_FISICA PF, " +
        "       DB_PERSONA_GIURIDICA PG " +
        "WHERE  PC.ID_PARTICELLA = ? "+
        "AND    PC.DATA_FINE_VALIDITA IS NULL " +
        "AND    PC.ID_PARTICELLA_CERTIFICATA = PROC.ID_PARTICELLA_CERTIFICATA " +
        "AND    PROC.ID_SOGGETTO = SO.ID_SOGGETTO " +
        "AND    PROC.DATA_FINE_VALIDITA IS NULL " +
        "AND    SO.ID_SOGGETTO = PF.ID_SOGGETTO (+) " +
        "AND    SO.ID_SOGGETTO = PG.ID_SOGGETTO (+) " +
        "ORDER BY CUAA, PROC.PERCENTUALE_POSSESSO ";

      

      SolmrLogger.debug(this, "Value of parameter 1 [ID_PARTICELLA] in getListProprietaCertifByIdParticella method in ParticellaCertificataDAO: "+idParticella+"\n");

      stmt = conn.prepareStatement(query);
      stmt.setLong(1, idParticella);
      

      SolmrLogger.debug(this, "Executing getListProprietaCertifByIdParticella: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        if(vIdProprietaCertificata == null)
        {
          vIdProprietaCertificata = new Vector<ProprietaCertificataVO>();
        }
        
        proprietaCertificataVO = new ProprietaCertificataVO();
        proprietaCertificataVO.setIdProprietaCertificata(new Long(rs.getLong("ID_PROPRIETA_CERTIFICATA")));
        proprietaCertificataVO.setIdParticellaCertificata(new Long(rs.getLong("ID_PARTICELLA_CERTIFICATA")));
        proprietaCertificataVO.setCuaa(rs.getString("CUAA"));
        proprietaCertificataVO.setIdTipoDiritto(checkLongNull(rs.getString("ID_TIPO_DIRITTO")));
        proprietaCertificataVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
        proprietaCertificataVO.setDataInizioTitolarita(rs.getTimestamp("DATA_INIZIO_TITOLARITA"));
        
        vIdProprietaCertificata.add(proprietaCertificataVO);
        
      }
      

      rs.close();
      stmt.close();
    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "getListProprietaCertifByIdParticella - SQLException: "+exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) 
    {
      SolmrLogger.error(this, "getListProprietaCertifByIdParticella - Generic Exception: "+ex);
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
        SolmrLogger.error(this, "getListProprietaCertifByIdParticella - SQLException while closing Statement and Connection: "+exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) 
      {
        SolmrLogger.error(this, "getListProprietaCertifByIdParticella - Generic Exception while closing Statement and Connection: "+ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListProprietaCertifByIdParticella method in ParticellaCertificataDAO\n");
    
    return vIdProprietaCertificata;
  }
  
  
  public Vector<ProprietaCertificataVO> getListDettaglioProprietaCertifByIdParticella(long idParticella, 
      Date dataInserimentoValidazione) 
      throws DataAccessException 
   {
     SolmrLogger.debug(this, "Invocating getListProprietaCertifByIdParticella method in ParticellaCertificataDAO\n");
     Connection conn = null;
     PreparedStatement stmt = null;
     Vector<ProprietaCertificataVO> vIdProprietaCertificata = null;
     ProprietaCertificataVO proprietaCertificataVO = null;

     try 
     {
       SolmrLogger.debug(this, "Creating db-connection in getListDettaglioProprietaCertifByIdParticella method in ParticellaCertificataDAO\n");
       conn = getDatasource().getConnection();
       SolmrLogger.debug(this, "Created db-connection in getListDettaglioProprietaCertifByIdParticella method in ParticellaCertificataDAO and it values: "+conn+"\n");

       String query = 
         "SELECT PROC.ID_PROPRIETA_CERTIFICATA, " +
         "       PROC.ID_PARTICELLA_CERTIFICATA, " +
         "       DECODE(SO.FLAG_FISICO,'N',PG.CODICE_FISCALE, PF.CODICE_FISCALE) AS CUAA, " +
         "       DECODE(SO.FLAG_FISICO,'N',PG.DENOMINAZIONE, '') AS DENOMINAZIONE, " +
         "       DECODE(SO.FLAG_FISICO,'S', PF.COGNOME, '') AS COGNOME, " +
         "       DECODE(SO.FLAG_FISICO,'S', PF.NOME, '') AS NOME, " +
         "       PROC.ID_TIPO_DIRITTO, " +
         "       PROC.PERCENTUALE_POSSESSO, " +
         "       PROC.DATA_INIZIO_TITOLARITA, " +
         "       PROC.DATA_AGGIORNAMENTO, " +
         "       TD.CODICE, " +
         "       TD.DESCRIZIONE AS DESC_DIRITTO, " +
         "       TD.ID_TITOLO_POSSESSO, " +
         "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
         "          || ' ' " + 
         "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
         "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
         "         WHERE PROC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
         "       AS DESC_UTENTE_AGG, " +
         "       TF.DESCRIZIONE AS DESC_FONTE " +
         "FROM   DB_PROPRIETA_CERTIFICATA PROC, " +
         "       DB_PARTICELLA_CERTIFICATA PC," +
         "       DB_SOGGETTO SO, " +
         "       DB_PERSONA_FISICA PF, " +
         "       DB_PERSONA_GIURIDICA PG, " +
         "       DB_TIPO_DIRITTO TD, " +
         //"       PAPUA_V_UTENTE_LOGIN PVU, " +
         "       DB_TIPO_FONTE TF " +
         "WHERE  PC.ID_PARTICELLA = ? ";
       if(Validator.isNotEmpty(dataInserimentoValidazione))
       {
         query +=
         "AND    PC.DATA_INIZIO_VALIDITA <= ? " +
         "AND    NVL(PC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) >= ? ";
       }
       else
       {
         query +=
         "AND    PC.DATA_FINE_VALIDITA IS NULL ";
       }
       query +=
         "AND    PC.ID_PARTICELLA_CERTIFICATA = PROC.ID_PARTICELLA_CERTIFICATA " +
         "AND    PROC.ID_SOGGETTO = SO.ID_SOGGETTO ";
       if(Validator.isNotEmpty(dataInserimentoValidazione))
       {
         query +=
         "AND    PROC.DATA_INIZIO_VALIDITA <= ? " +
         "AND    NVL(PROC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) >= ? ";
       }
       else
       {
         query +=
         "AND    PROC.DATA_FINE_VALIDITA IS NULL ";
       }
       query +=  
         "AND    SO.ID_SOGGETTO = PF.ID_SOGGETTO (+) " +
         "AND    SO.ID_SOGGETTO = PG.ID_SOGGETTO (+) " +
         "AND    PROC.ID_TIPO_DIRITTO = TD.ID_TIPO_DIRITTO " +
         //"AND    PROC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
         "AND    PROC.ID_FONTE = TF.ID_FONTE ";

       

       SolmrLogger.debug(this, "Value of parameter 1 [ID_PARTICELLA] in getListDettaglioProprietaCertifByIdParticella method in ParticellaCertificataDAO: "+idParticella+"\n");

       stmt = conn.prepareStatement(query);
       int indice = 0;
       stmt.setLong(++indice, idParticella);
       if(Validator.isNotEmpty(dataInserimentoValidazione))
       {
         stmt.setTimestamp(++indice, convertDateToTimestamp(dataInserimentoValidazione));
         stmt.setTimestamp(++indice, convertDateToTimestamp(dataInserimentoValidazione));
         stmt.setTimestamp(++indice, convertDateToTimestamp(dataInserimentoValidazione));
         stmt.setTimestamp(++indice, convertDateToTimestamp(dataInserimentoValidazione));
       }
       

       SolmrLogger.debug(this, "Executing getListDettaglioProprietaCertifByIdParticella: "+query+"\n");

       ResultSet rs = stmt.executeQuery();

       while(rs.next()) 
       {
         if(vIdProprietaCertificata == null)
         {
           vIdProprietaCertificata = new Vector<ProprietaCertificataVO>();
         }
         
         proprietaCertificataVO = new ProprietaCertificataVO();
         proprietaCertificataVO.setIdProprietaCertificata(new Long(rs.getLong("ID_PROPRIETA_CERTIFICATA")));
         proprietaCertificataVO.setIdParticellaCertificata(new Long(rs.getLong("ID_PARTICELLA_CERTIFICATA")));
         proprietaCertificataVO.setCuaa(rs.getString("CUAA"));
         proprietaCertificataVO.setIdTipoDiritto(checkLongNull(rs.getString("ID_TIPO_DIRITTO")));
         proprietaCertificataVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
         proprietaCertificataVO.setDataInizioTitolarita(rs.getTimestamp("DATA_INIZIO_TITOLARITA"));
         proprietaCertificataVO.setCognome(rs.getString("COGNOME"));
         proprietaCertificataVO.setNome(rs.getString("NOME"));
         proprietaCertificataVO.setDenominazione(rs.getString("DENOMINAZIONE"));
         proprietaCertificataVO.setCodiceDiritto(rs.getString("CODICE"));
         proprietaCertificataVO.setDescDiritto(rs.getString("DESC_DIRITTO"));
         proprietaCertificataVO.setIdTitoloPossesso(checkLongNull(rs.getString("ID_TITOLO_POSSESSO")));
         proprietaCertificataVO.setDescUtenteAggiornamento(rs.getString("DESC_UTENTE_AGG"));
         proprietaCertificataVO.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO"));
         proprietaCertificataVO.setDescFonte(rs.getString("DESC_FONTE"));
         
         vIdProprietaCertificata.add(proprietaCertificataVO);
         
       }
       

       rs.close();
       stmt.close();
     }
     catch(SQLException exc) 
     {
       SolmrLogger.error(this, "getListDettaglioProprietaCertifByIdParticella - SQLException: "+exc);
       throw new DataAccessException(exc.getMessage());
     }
     catch(Exception ex) 
     {
       SolmrLogger.error(this, "getListDettaglioProprietaCertifByIdParticella - Generic Exception: "+ex);
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
         SolmrLogger.error(this, "getListDettaglioProprietaCertifByIdParticella - SQLException while closing Statement and Connection: "+exc);
         throw new DataAccessException(exc.getMessage());
       }
       catch(Exception ex) 
       {
         SolmrLogger.error(this, "getListDettaglioProprietaCertifByIdParticella - Generic Exception while closing Statement and Connection: "+ex);
         throw new DataAccessException(ex.getMessage());
       }
     }
     SolmrLogger.debug(this, "Invocated getListDettaglioProprietaCertifByIdParticella method in ParticellaCertificataDAO\n");
     
     return vIdProprietaCertificata;
   }
  
  
  public Long insertProprietaCertificata(ProprietaCertificataVO propCertVO, long idUtente)
      throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_DB_PROPRIETA_CERTIFICATA);
      conn = getDatasource().getConnection();

      String insert = 
        "INSERT INTO DB_PROPRIETA_CERTIFICATA "+
        "  (ID_PROPRIETA_CERTIFICATA, ID_PARTICELLA_CERTIFICATA, ID_SOGGETTO, " +
        "   ID_TIPO_DIRITTO, PERCENTUALE_POSSESSO, DATA_INIZIO_VALIDITA, " +
        "   ID_FONTE, DATA_INIZIO_TITOLARITA, DATA_AGGIORNAMENTO, " +
        "   ID_UTENTE_AGGIORNAMENTO) " +
        " VALUES  (?, ?, ?, ?, ?, SYSDATE, 8, ?, SYSDATE, ?) ";

      stmt = conn.prepareStatement(insert);
      int indice = 0;
      stmt.setLong(++indice, primaryKey.longValue());
      stmt.setLong(++indice, propCertVO.getIdParticellaCertificata().longValue());
      stmt.setLong(++indice, propCertVO.getIdSoggetto().longValue());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(propCertVO.getIdTipoDiritto()));
      stmt.setBigDecimal(++indice, propCertVO.getPercentualePossesso());
      stmt.setTimestamp(++indice, convertDateToTimestamp(propCertVO.getDataInizioTitolarita()));      
      stmt.setLong(++indice, idUtente);

      SolmrLogger.debug(this, "Executing insert: " + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insert.");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException: " + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: " + ex.getMessage());
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
        SolmrLogger.fatal(this,
            "SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this,
            "Generic Exception while closing Statement and Connection: "
                + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }
  
  

}
