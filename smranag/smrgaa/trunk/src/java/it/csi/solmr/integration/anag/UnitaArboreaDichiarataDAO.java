package it.csi.solmr.integration.anag;
	
import it.csi.smranag.smrgaa.dto.terreni.IstanzaRiesameVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoDestinazioneVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoDettaglioUsoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoQualitaUsoVO;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.FoglioVO;
import it.csi.solmr.dto.anag.ParticellaCertElegVO;
import it.csi.solmr.dto.anag.ParticellaCertificataVO;
import it.csi.solmr.dto.anag.ParticellaVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneDichiarataVO;
import it.csi.solmr.dto.anag.terreni.FiltriUnitaArboreaRicercaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaArboreaExcelVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.dto.anag.terreni.StoricoUnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.TipoCausaleModificaVO;
import it.csi.solmr.dto.anag.terreni.TipoCessazioneUnarVO;
import it.csi.solmr.dto.anag.terreni.TipoColtivazioneUnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.TipoEtaImpiantoUnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.TipoFormaAllevamentoVO;
import it.csi.solmr.dto.anag.terreni.TipoGenereIscrizioneVO;
import it.csi.solmr.dto.anag.terreni.TipoGiacituraUnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.TipoGiudizioUnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.TipoInterventoViticoloVO;
import it.csi.solmr.dto.anag.terreni.TipoIrrigazioneUnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.TipoPotaturaUnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.TipoRocciaUnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.TipoScheletroUnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.TipoStatoVegetativoUnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.TipoTipologiaUnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.TipoTipologiaVignetoVO;
import it.csi.solmr.dto.anag.terreni.TipoTipologiaVinoVO;
import it.csi.solmr.dto.anag.terreni.TipoUtilizzoVO;
import it.csi.solmr.dto.anag.terreni.TipoVarietaUnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.TipoVarietaVO;
import it.csi.solmr.dto.anag.terreni.TipoVinoVO;
import it.csi.solmr.dto.anag.terreni.UnitaArboreaDichiarataVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
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
import java.util.Vector;

import oracle.sql.ARRAY;

public class UnitaArboreaDichiarataDAO extends it.csi.solmr.integration.BaseDAO {


	public UnitaArboreaDichiarataDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public UnitaArboreaDichiarataDAO(String refName) throws ResourceAccessException {
		super(refName);
	}
	
	/**
	 * Metodo che mi consente di recuperare l'elenco delle unità arboree dichiarate
	 * relative all'id_storico_unita_arborea
	 * 
	 * @param idStoricoUnitaArborea
	 * @return it.csi.solmr.dto.anag.UnitaArboreaDichiarataVO[]
	 * @throws DataAccessException
	 */
	public UnitaArboreaDichiarataVO[] getListUnitaArboreaDichiarataByIdStoricoUnitaArborea(Long idStoricoUnitaArborea, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListUnitaArboreaDichiarataByIdStoricoUnitaArborea method in UnitaArboreaDichiarataDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<UnitaArboreaDichiarataVO> elencoUnitaArboreeDichiarate = new Vector<UnitaArboreaDichiarataVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListUnitaArboreaDichiarataByIdStoricoUnitaArborea method in UnitaArboreaDichiarataDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListUnitaArboreaDichiarataByIdStoricoUnitaArborea method in UnitaArboreaDichiarataDAO and it values: "+conn+"\n");

			String query = " SELECT UAD.ID_UNITA_ARBOREA_DICHIARATA, " +
						   "        UAD.CODICE_FOTOGRAFIA_TERRENI, " +
						   "        UAD.ID_STORICO_UNITA_ARBOREA, " +
						   "        UAD.ID_STORICO_PARTICELLA, " + 
						   "	      UAD.PROGR_UNAR, " + 
						   "        UAD.DATA_INIZIO_VALIDITA, " + 
						   "        UAD.DATA_FINE_VALIDITA, " + 
						   "        UAD.DATA_LAVORAZIONE, " + 
						   "        UAD.ID_TIPOLOGIA_UNAR, " + 
						   "        TTU.DESCRIZIONE AS DESC_TIPO, " +
						   "        UAD.AREA, " + 
						   "        UAD.SESTO_SU_FILA, " + 
						   "        UAD.SESTO_TRA_FILE, " + 
						   "        UAD.NUM_CEPPI, " + 
						   "        UAD.ANNO_IMPIANTO, " + 
						   "        UAD.DATA_IMPIANTO, " + 
						   "        UAD.ANNO_REINNESTO, " + 
						   "        UAD.ID_FORMA_ALLEVAMENTO, " +
						   "        TFA.DESCRIZIONE AS DESC_ALLEVAMENTO, " +
						   "        UAD.ID_IRRIGAZIONE_UNAR, " +
						   "        TIU.DESCRIZIONE AS DESC_IRRIGAZIONE, " +
						   "        UAD.ID_COLTIVAZIONE_UNAR, " +
						   "        TCU.DESCRIZIONE AS DESC_COLTIVAZIONE, " +
						   "        UAD.CODICE_TIPO_VARIETA, " + 
						   "        UAD.PRESENZA_ALTRI_VITIGNI, " +  
						   "        UAD.NUMERO_PIANTE_PRODUTTIVO, " + 
						   "        UAD.NUMERO_ALTRE_PIANTE, " + 
						   "        UAD.CAMPAGNA, " + 
						   "        UAD.ID_TIPOLOGIA_VIGNETO, " +
						   "        TTV.DESCRIZIONE AS DESC_VIGNETO, " + 
						   "        UAD.TIPO_IMPIANTO, " + 
						   "        UAD.NUMERO_CASTAGNI, " + 
						   "        UAD.GRUPPO, " + 
						   "        UAD.RICADUTA, " + 
						   "        UAD.ID_GIACITURA_UNAR, " +
						   "        TGU.DESCRIZIONE AS DESC_GIACITURA, " +
						   "        UAD.ID_ROCCIA_UNAR, " + 
						   "	      TRU.DESCRIZIONE AS DESC_ROCCIA, " +
						   "        UAD.ID_SCHELETRO_UNAR, " + 
						   "        TSU.DESCRIZIONE AS DESC_SCHELETRO, " +
						   "        UAD.ID_STATO_VEGETATIVO_UNAR, " + 
						   "        TSVU.DESCRIZIONE AS DESC_STATO_VEG, " +
						   "        UAD.ID_POTATURA_UNAR, " + 
						   "        TPU.DESCRIZIONE AS DESC_POTATURA, " +
						   "        UAD.ID_GIUDIZIO_UNAR, " + 
						   "        TGIU.DESCRIZIONE AS DESC_GIUDIZIO, " +
						   " 	      UAD.SUPPLEMENTARI, " + 
						   "        UAD.MECCANIZZABILE, " + 
						   "        UAD.DIMENSIONE_CHIOMA, " + 
						   "        UAD.ID_ETA_IMPIANTO_UNAR, " + 
						   "        TEIU.DESCRIZIONE AS DESC_ETA_IMPIANTO, " +
						   "        UAD.PROVINCIA_CCIAA, " + 
						   "        UAD.MATRICOLA_CCIAA, " + 
						   "        UAD.CONFERMA_PREC_ISCRIZIONE_ALBO, " + 
						   "        UAD.RICHIESTA_NUOVA_ISCR_ALBO, " + 
						   "        UAD.CONFERMA_RICH_NUOVA_ISCR_ALBO, " + 
						   "        UAD.SUPERFICIE_DA_ISCRIVERE_ALBO, " + 
						   "        UAD.ANNO_ISCRIZIONE_ALBO, " + 
						   "        UAD.ID_FONTE, " + 
						   "        TF.DESCRIZIONE AS DESC_FONTE, " +
						   "        UAD.NOTE, " + 
						   "        UAD.DATA_AGGIORNAMENTO, " + 
						   "        UAD.ID_UTENTE_AGGIORNAMENTO, " + 
						   "        UAD.ID_VARIETA, " + 
						   "        TV.CODICE_VARIETA, " +
						   "        TV.DESCRIZIONE AS DESC_VARIETA, " +
						   "        UAD.ID_UTILIZZO, " + 
						   "        TU.CODICE, " +
						   "        TU.DESCRIZIONE AS DESC_UTILIZZO, " +
						   "        UAD.ID_VINO, " + 
						   "        TVIN.DESCRIZIONE AS DESC_VINO, " +
						   "        UAD.PERCENTUALE_VARIETA, " +
						   "        UAD.ID_TIPOLOGIA_VINO, " + 
						   "        TTVIN.DESCRIZIONE AS DESC_TIPO_VINO, " +
						   "	      UAD.DATA_CESSAZIONE, " + 
						   "        UAD.ID_CESSAZIONE_UNAR, " + 
						   "        TCEU.DESCRIZIONE AS DESC_CESSAZIONE, " +
						   "        UAD.ID_CAUSALE_MODIFICA, " + 
						   "        TCM.DESCRIZIONE AS DESC_MODIFICA, " +
						   "        UAD.DATA_ESECUZIONE, " + 
						   "        UAD.ESITO_CONTROLLO, " +  
						   "        UAD.ID_AZIENDA, " +
						   "        UAD.ANNO_PRIMA_PRODUZIONE, " +
               "        UAD.DATA_PRIMA_PRODUZIONE " +
						   " FROM   DB_UNITA_ARBOREA_DICHIARATA UAD, " +
						   "        DB_TIPO_TIPOLOGIA_UNAR TTU, " +
						   "        DB_TIPO_FORMA_ALLEVAMENTO TFA, " +
						   "        DB_TIPO_IRRIGAZIONE_UNAR TIU, " +
						   "        DB_TIPO_COLTIVAZIONE_UNAR TCU, " +
						   "        DB_TIPO_TIPOLOGIA_VIGNETO TTV, " +
						   "        DB_TIPO_GIACITURA_UNAR TGU, " +
						   "        DB_TIPO_ROCCIA_UNAR TRU, " +
						   "        DB_TIPO_SCHELETRO_UNAR TSU, " +
						   "        DB_TIPO_STATO_VEGETATIVO_UNAR TSVU, " +
						   "        DB_TIPO_POTATURA_UNAR TPU, " +
						   "        DB_TIPO_GIUDIZIO_UNAR TGIU, " +
						   "        DB_TIPO_ETA_IMPIANTO_UNAR TEIU, " +
						   "        DB_TIPO_FONTE TF, " +
						   "        DB_TIPO_VARIETA TV, " +
						   "        DB_TIPO_UTILIZZO TU, " +
						   "        DB_TIPO_VINO TVIN, " +
						   "        DB_TIPO_TIPOLOGIA_VINO TTVIN, " +
						   "        DB_TIPO_CESSAZIONE_UNAR TCEU, " +
						   "        DB_TIPO_CAUSALE_MODIFICA TCM " +
						   " WHERE  ID_STORICO_UNITA_ARBOREA = ? " +
						   " AND    UAD.ID_TIPOLOGIA_UNAR = TTU.ID_TIPOLOGIA_UNAR(+) " +
						   " AND    UAD.ID_FORMA_ALLEVAMENTO = TFA.ID_FORMA_ALLEVAMENTO(+) " +
						   " AND    UAD.ID_IRRIGAZIONE_UNAR = TIU.ID_IRRIGAZIONE_UNAR(+) " +
						   " AND    UAD.ID_COLTIVAZIONE_UNAR = TCU.ID_COLTIVAZIONE_UNAR(+) " +
						   " AND    UAD.ID_TIPOLOGIA_VIGNETO = TTV.ID_TIPOLOGIA_VIGNETO(+) " +	
						   " AND    UAD.ID_GIACITURA_UNAR = TGU.ID_GIACITURA_UNAR(+) " +
						   " AND    UAD.ID_ROCCIA_UNAR = TRU.ID_ROCCIA_UNAR(+) " +
						   " AND    UAD.ID_SCHELETRO_UNAR = TSU.ID_SCHELETRO_UNAR(+) " +
						   " AND    UAD.ID_STATO_VEGETATIVO_UNAR = TSVU.ID_STATO_VEGETATIVO_UNAR(+) " +
						   " AND    UAD.ID_POTATURA_UNAR = TPU.ID_POTATURA_UNAR(+) " +
						   " AND    UAD.ID_GIUDIZIO_UNAR = TGIU.ID_GIUDIZIO_UNAR(+) " +
						   " AND    UAD.ID_ETA_IMPIANTO_UNAR = TEIU.ID_ETA_IMPIANTO_UNAR(+) " +
						   " AND    UAD.ID_FONTE = TF.ID_FONTE " + 
						   " AND    UAD.ID_VARIETA = TV.ID_VARIETA(+) " +
						   " AND    UAD.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
						   " AND    UAD.ID_VINO = TVIN.ID_VINO(+) " +
						   " AND    UAD.ID_TIPOLOGIA_VINO = TTVIN.ID_TIPOLOGIA_VINO(+) " +
						   " AND    UAD.ID_CESSAZIONE_UNAR = TCEU.ID_CESSAZIONE_UNAR(+) " +
						   " AND    UAD.ID_CAUSALE_MODIFICA = TCM.ID_CAUSALE_MODIFICA(+) "; 
			
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
			
			SolmrLogger.debug(this, "Value of parameter 1 [ID_STORICO_UNITA_ARBOREA] in getListUnitaArboreaDichiarataByIdStoricoUnitaArborea method in UnitaArboreaDichiarataDAO: "+idStoricoUnitaArborea+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idStoricoUnitaArborea.longValue());
			
			SolmrLogger.debug(this, "Executing getListUnitaArboreaDichiarataByIdStoricoUnitaArborea: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				UnitaArboreaDichiarataVO unitaArboreaDichiarataVO = new UnitaArboreaDichiarataVO();
				unitaArboreaDichiarataVO.setIdUnitaArboreaDichiarata(new Long(rs.getLong("ID_UNITA_ARBOREA_DICHIARATA")));
				unitaArboreaDichiarataVO.setCodiceFotografiaTerreni(rs.getString("CODICE_FOTOGRAFIA_TERRENI"));
				unitaArboreaDichiarataVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
				unitaArboreaDichiarataVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
				unitaArboreaDichiarataVO.setProgrUnar(rs.getString("PROGR_UNAR"));
				unitaArboreaDichiarataVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
				unitaArboreaDichiarataVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
				unitaArboreaDichiarataVO.setDataLavorazione(rs.getTimestamp("DATA_LAVORAZIONE"));
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_UNAR"))) {
					unitaArboreaDichiarataVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
					TipoTipologiaUnitaArboreaVO tipoTipologiaUnitaArboreaVO = new TipoTipologiaUnitaArboreaVO();
					tipoTipologiaUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
					tipoTipologiaUnitaArboreaVO.setDescrizione(rs.getString("DESC_TIPO"));
					unitaArboreaDichiarataVO.setTipoTipologiaUnitaArboreaVO(tipoTipologiaUnitaArboreaVO);
				}
				unitaArboreaDichiarataVO.setArea(rs.getString("AREA"));
				unitaArboreaDichiarataVO.setSestoSuFila(rs.getString("SESTO_SU_FILA"));
				unitaArboreaDichiarataVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
				unitaArboreaDichiarataVO.setNumCeppi(rs.getString("NUM_CEPPI"));
				unitaArboreaDichiarataVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
				unitaArboreaDichiarataVO.setDataImpianto(rs.getDate("DATA_IMPIANTO"));
				unitaArboreaDichiarataVO.setAnnoReinnesto(rs.getString("ANNO_REINNESTO"));
				if(Validator.isNotEmpty(rs.getString("ID_FORMA_ALLEVAMENTO"))) {
					unitaArboreaDichiarataVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
					TipoFormaAllevamentoVO tipoFormaAllevamentoVO = new TipoFormaAllevamentoVO();
					tipoFormaAllevamentoVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
					tipoFormaAllevamentoVO.setDescrizione(rs.getString("DESC_ALLEVAMENTO"));
					unitaArboreaDichiarataVO.setTipoFormaAllevamentoVO(tipoFormaAllevamentoVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_IRRIGAZIONE_UNAR"))) {
					unitaArboreaDichiarataVO.setIdIrrigazioneUnar(new Long(rs.getLong("ID_IRRIGAZIONE_UNAR")));
					TipoIrrigazioneUnitaArboreaVO tipoIrrigazioneUnitaArboreaVO = new TipoIrrigazioneUnitaArboreaVO();
					tipoIrrigazioneUnitaArboreaVO.setIdIrrigazioneUnar(new Long(rs.getLong("ID_IRRIGAZIONE_UNAR")));
					tipoIrrigazioneUnitaArboreaVO.setDescrizione(rs.getString("DESC_IRRIGAZIONE"));
					unitaArboreaDichiarataVO.setTipoIrrigazioneUnitaArboreaVO(tipoIrrigazioneUnitaArboreaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_COLTIVAZIONE_UNAR"))) {
					unitaArboreaDichiarataVO.setIdColtivazioneUnar(new Long(rs.getLong("ID_COLTIVAZIONE_UNAR")));
					TipoColtivazioneUnitaArboreaVO tipoColtivazioneUnitaArboreaVO = new TipoColtivazioneUnitaArboreaVO();
					tipoColtivazioneUnitaArboreaVO.setIdColtivazioneUnar(new Long(rs.getLong("ID_COLTIVAZIONE_UNAR")));
					tipoColtivazioneUnitaArboreaVO.setDescrizione(rs.getString("DESC_COLTIVAZIONE"));
					unitaArboreaDichiarataVO.setTipoColtivazioneUnitaArboreaVO(tipoColtivazioneUnitaArboreaVO);
				}
				unitaArboreaDichiarataVO.setCodiceTipoVarieta(rs.getString("CODICE_TIPO_VARIETA"));
				unitaArboreaDichiarataVO.setPresenzaAltriVitigni(rs.getString("PRESENZA_ALTRI_VITIGNI"));
				unitaArboreaDichiarataVO.setNumeroPianteProduttivo(rs.getString("NUMERO_PIANTE_PRODUTTIVO"));
				unitaArboreaDichiarataVO.setNumeroAltrePiante(rs.getString("NUMERO_ALTRE_PIANTE"));
				unitaArboreaDichiarataVO.setCampagna(rs.getString("CAMPAGNA"));
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VIGNETO"))) {
					unitaArboreaDichiarataVO.setIdTipologiaVigneto(new Long(rs.getLong("ID_TIPOLOGIA_VIGNETO")));
					TipoTipologiaVignetoVO tipoTipologiaVignetoVO = new TipoTipologiaVignetoVO();
					tipoTipologiaVignetoVO.setIdTipologiaVigneto(new Long(rs.getLong("ID_TIPOLOGIA_VIGNETO")));
					tipoTipologiaVignetoVO.setDescrizione(rs.getString("DESC_VIGNETO"));
					unitaArboreaDichiarataVO.setTipoTipologiaVignetoVO(tipoTipologiaVignetoVO);
				}
				unitaArboreaDichiarataVO.setTipoImpianto(rs.getString("TIPO_IMPIANTO"));
				unitaArboreaDichiarataVO.setNumeroCastagni(rs.getString("NUMERO_CASTAGNI"));
				unitaArboreaDichiarataVO.setGruppo(rs.getString("GRUPPO"));
				unitaArboreaDichiarataVO.setRicaduta(rs.getString("RICADUTA"));
				if(Validator.isNotEmpty(rs.getString("ID_GIACITURA_UNAR"))) {
					unitaArboreaDichiarataVO.setIdGiacituraUnar(new Long(rs.getLong("ID_GIACITURA_UNAR")));
					TipoGiacituraUnitaArboreaVO tipoGiacituraUnitaArboreaVO = new TipoGiacituraUnitaArboreaVO();
					tipoGiacituraUnitaArboreaVO.setIdGiacituraUnar(new Long(rs.getLong("ID_GIACITURA_UNAR")));
					tipoGiacituraUnitaArboreaVO.setDescrizione(rs.getString("DESC_GIACITURA"));
					unitaArboreaDichiarataVO.setTipoGiacituraUnitaArboreaVO(tipoGiacituraUnitaArboreaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_ROCCIA_UNAR"))) {
					unitaArboreaDichiarataVO.setIdRocciaUnar(new Long(rs.getLong("ID_ROCCIA_UNAR")));
					TipoRocciaUnitaArboreaVO tipoRocciaUnitaArboreaVO = new TipoRocciaUnitaArboreaVO();
					tipoRocciaUnitaArboreaVO.setIdRocciaUnar(new Long(rs.getLong("ID_ROCCIA_UNAR")));
					tipoRocciaUnitaArboreaVO.setDescrizione(rs.getString("DESC_ROCCIA"));
					unitaArboreaDichiarataVO.setTipoRocciaUnitaArboreaVO(tipoRocciaUnitaArboreaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_SCHELETRO_UNAR"))) {
					unitaArboreaDichiarataVO.setIdScheletroUnar(new Long(rs.getLong("ID_SCHELETRO_UNAR")));
					TipoScheletroUnitaArboreaVO tipoScheletroUnitaArboreaVO = new TipoScheletroUnitaArboreaVO();
					tipoScheletroUnitaArboreaVO.setIdScheletroUnar(new Long(rs.getLong("ID_SCHELETRO_UNAR")));
					tipoScheletroUnitaArboreaVO.setDescrizione(rs.getString("DESC_SCHELETRO"));
					unitaArboreaDichiarataVO.setTipoScheletroUnitaArboreaVO(tipoScheletroUnitaArboreaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_STATO_VEGETATIVO_UNAR"))) {
					unitaArboreaDichiarataVO.setIdStatoVegetativoUnar(new Long(rs.getLong("ID_STATO_VEGETATIVO_UNAR")));
					TipoStatoVegetativoUnitaArboreaVO tipoStatoVegetativoUnitaArboreaVO = new TipoStatoVegetativoUnitaArboreaVO();
					tipoStatoVegetativoUnitaArboreaVO.setIdStatoVegetativoUnar(new Long(rs.getLong("ID_STATO_VEGETATIVO_UNAR")));
					tipoStatoVegetativoUnitaArboreaVO.setDescrizione(rs.getString("DESC_STATO_VEG"));
					unitaArboreaDichiarataVO.setTipoStatoVegetativoUnitaArboreaVO(tipoStatoVegetativoUnitaArboreaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_POTATURA_UNAR"))) {
					unitaArboreaDichiarataVO.setIdPotaturaUnar(new Long(rs.getLong("ID_POTATURA_UNAR")));
					TipoPotaturaUnitaArboreaVO tipoPotaturaUnitaArboreaVO = new TipoPotaturaUnitaArboreaVO();
					tipoPotaturaUnitaArboreaVO.setIdPotaturaUnar(new Long(rs.getLong("ID_POTATURA_UNAR")));
					tipoPotaturaUnitaArboreaVO.setDescrizione(rs.getString("DESC_POTATURA"));
					unitaArboreaDichiarataVO.setTipoPotaturaUnitaArboreaVO(tipoPotaturaUnitaArboreaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_GIUDIZIO_UNAR"))) {
					unitaArboreaDichiarataVO.setIdGiudizioUnar(new Long(rs.getLong("ID_GIUDIZIO_UNAR")));
					TipoGiudizioUnitaArboreaVO tipoGiudizioUnitaArboreaVO = new TipoGiudizioUnitaArboreaVO();
					tipoGiudizioUnitaArboreaVO.setIdGiudizioUnar(new Long(rs.getLong("ID_GIUDIZIO_UNAR")));
					tipoGiudizioUnitaArboreaVO.setDescrizione(rs.getString("DESC_GIUDIZIO"));
					unitaArboreaDichiarataVO.setTipoGiudizioUnitaArboreaVO(tipoGiudizioUnitaArboreaVO);
				}
				unitaArboreaDichiarataVO.setSupplementari(rs.getString("SUPPLEMENTARI"));
				unitaArboreaDichiarataVO.setMeccanizzabile(rs.getString("MECCANIZZABILE"));
				unitaArboreaDichiarataVO.setDimensioneChioma(rs.getString("DIMENSIONE_CHIOMA"));
				if(Validator.isNotEmpty(rs.getString("ID_ETA_IMPIANTO_UNAR"))) {
					unitaArboreaDichiarataVO.setIdEtaImpiantoUnar(new Long(rs.getLong("ID_ETA_IMPIANTO_UNAR")));
					TipoEtaImpiantoUnitaArboreaVO tipoEtaImpiantoUnitaArboreaVO = new TipoEtaImpiantoUnitaArboreaVO();
					tipoEtaImpiantoUnitaArboreaVO.setIdEtaImpiantoUnar(new Long(rs.getLong("ID_ETA_IMPIANTO_UNAR")));
					tipoEtaImpiantoUnitaArboreaVO.setDescrizione(rs.getString("DESC_ETA_IMPIANTO"));
					unitaArboreaDichiarataVO.setTipoEtaImpiantoUnitaArboreaVO(tipoEtaImpiantoUnitaArboreaVO);
				}
				unitaArboreaDichiarataVO.setProvinciaCCIAA(rs.getString("PROVINCIA_CCIAA"));
				unitaArboreaDichiarataVO.setMatricolaCCIAA(rs.getString("MATRICOLA_CCIAA"));
				unitaArboreaDichiarataVO.setConfermaPrecIscrizioneAlbo(rs.getString("CONFERMA_PREC_ISCRIZIONE_ALBO"));
				unitaArboreaDichiarataVO.setRichiestaNuovaIscrAlbo(rs.getString("RICHIESTA_NUOVA_ISCR_ALBO"));
				unitaArboreaDichiarataVO.setConfermaRichNuovaIscrAlbo(rs.getString("CONFERMA_RICH_NUOVA_ISCR_ALBO"));
				unitaArboreaDichiarataVO.setSuperficieDaIscrivereAlbo(rs.getString("SUPERFICIE_DA_ISCRIVERE_ALBO"));
				unitaArboreaDichiarataVO.setAnnoIscrizioneAlbo(rs.getString("ANNO_ISCRIZIONE_ALBO"));
				unitaArboreaDichiarataVO.setIdFonte(new Long(rs.getLong("ID_FONTE")));
				CodeDescription fonte = new CodeDescription(Integer.decode(rs.getString("ID_FONTE")), rs.getString("DESC_FONTE"));
				unitaArboreaDichiarataVO.setTipoFonte(fonte);
				unitaArboreaDichiarataVO.setNote(rs.getString("NOTE"));
				unitaArboreaDichiarataVO.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO"));
				unitaArboreaDichiarataVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) {
					unitaArboreaDichiarataVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
					TipoVarietaUnitaArboreaVO tipoVarietaUnitaArboreaVO = new TipoVarietaUnitaArboreaVO();
					tipoVarietaUnitaArboreaVO.setIdVarietaUnar(new Long(rs.getLong("ID_VARIETA")));
					tipoVarietaUnitaArboreaVO.setCodice(rs.getString("CODICE"));
					tipoVarietaUnitaArboreaVO.setDescrizione(rs.getString("DESC_VARIETA"));
					unitaArboreaDichiarataVO.setTipoVarietaUnitaArboreaVO(tipoVarietaUnitaArboreaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO"))) {
					unitaArboreaDichiarataVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
					TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
					tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
					tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
					tipoUtilizzoVO.setDescrizione(rs.getString("DESC_UTILIZZO"));
					unitaArboreaDichiarataVO.setTipoUtilizzoVO(tipoUtilizzoVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_VINO"))) {
					unitaArboreaDichiarataVO.setIdVino(new Long(rs.getLong("ID_VINO")));
					TipoVinoVO tipoVinoVO = new TipoVinoVO();
					tipoVinoVO.setIdVino(new Long(rs.getLong("ID_VINO")));
					tipoVinoVO.setDescrizione(rs.getString("DESC_VINO"));
					unitaArboreaDichiarataVO.setTipoVinoVO(tipoVinoVO);
				}
				unitaArboreaDichiarataVO.setPercentualeVarieta(rs.getString("PERCENTUALE_VARIETA"));
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VINO"))) {
					unitaArboreaDichiarataVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
					TipoTipologiaVinoVO tipoTipologiaVinoVO = new TipoTipologiaVinoVO();
					tipoTipologiaVinoVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
					tipoTipologiaVinoVO.setDescrizione(rs.getString("DESC_TIPO_VINO"));
					unitaArboreaDichiarataVO.setTipoTipologiaVinoVO(tipoTipologiaVinoVO);
				}
				unitaArboreaDichiarataVO.setDataCessazione(rs.getTimestamp("DATA_CESSAZIONE"));
				if(Validator.isNotEmpty(rs.getString("ID_CESSAZIONE_UNAR"))) {
					unitaArboreaDichiarataVO.setIdCessazioneUnar(new Long(rs.getLong("ID_CESSAZIONE_UNAR")));
					TipoCessazioneUnarVO tipoCessazioneUnarVO = new TipoCessazioneUnarVO();
					tipoCessazioneUnarVO.setIdCessazioneUnar(new Long(rs.getLong("ID_CESSAZIONE_UNAR")));
					tipoCessazioneUnarVO.setDescrizione(rs.getString("DESC_CESSAZIONE"));
					unitaArboreaDichiarataVO.setTipoCessazioneUnarVO(tipoCessazioneUnarVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_CAUSALE_MODIFICA"))) {
					unitaArboreaDichiarataVO.setIdCausaleModifica(new Long(rs.getLong("ID_CAUSALE_MODIFICA")));
					TipoCausaleModificaVO tipoCausaleModificaVO = new TipoCausaleModificaVO();
					tipoCausaleModificaVO.setIdCausaleModifica(new Long(rs.getLong("ID_CAUSALE_MODIFICA")));
					tipoCausaleModificaVO.setDescrizione(rs.getString("DESC_MODIFICA"));
					unitaArboreaDichiarataVO.setTipoCausaleModificaVO(tipoCausaleModificaVO);
				}
				unitaArboreaDichiarataVO.setDataEsecuzione(rs.getTimestamp("DATA_ESECUZIONE"));
				unitaArboreaDichiarataVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
				unitaArboreaDichiarataVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
				unitaArboreaDichiarataVO.setAnnoPrimaProduzione(rs.getString("ANNO_PRIMA_PRODUZIONE"));
        unitaArboreaDichiarataVO.setDataPrimaProduzione(rs.getDate("DATA_PRIMA_PRODUZIONE"));
				
				
				elencoUnitaArboreeDichiarate.add(unitaArboreaDichiarataVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListUnitaArboreaDichiarataByIdStoricoUnitaArborea in UnitaArboreaDichiarataDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListUnitaArboreaDichiarataByIdStoricoUnitaArborea in UnitaArboreaDichiarataDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListUnitaArboreaDichiarataByIdStoricoUnitaArborea in UnitaArboreaDichiarataDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListUnitaArboreaDichiarataByIdStoricoUnitaArborea in UnitaArboreaDichiarataDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListUnitaArboreaDichiarataByIdStoricoUnitaArborea method in UnitaArboreaDichiarataDAO\n");
		if(elencoUnitaArboreeDichiarate.size() == 0) {
			return (UnitaArboreaDichiarataVO[])elencoUnitaArboreeDichiarate.toArray(new UnitaArboreaDichiarataVO[0]);
		}
		else {
			return (UnitaArboreaDichiarataVO[])elencoUnitaArboreeDichiarate.toArray(new UnitaArboreaDichiarataVO[elencoUnitaArboreeDichiarate.size()]);
		}
	}
	
	/**
	 * Metodo utilizzato per estrarre l'elenco delle particelle arboree(storico
	 * particelle e unita arborea dichiarata) di una data azienda alla dichiarazione
	 * di consistenza selezionata per la stampa
	 * 
	 * @param idAzienda
	 * @param filtriUnitaArboreaRicercaVO
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
	 * @throws DataAccessException
	 */
	public StoricoParticellaVO[] searchUnitaArboreaDichiarataByParametersForStampa(Long idAzienda, FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<StoricoParticellaVO> elencoParticelleArboreeDichiarate = new Vector<StoricoParticellaVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO and it values: "+conn+"\n");
			
			//***************Nuova eleggibilità fittizia *************
			String query = " " +
          "SELECT PARTICELLE_TABLE_SUM.* , TOTALE AS SUPERFICIE_ELEG " +
          "FROM " +
          "     (SELECT PARTICELLE_TABLE.*, " +
          "             (NVL(PCK_SMRGAA_LIBRERIA.SelTotSupElegByPartEVetCat(PARTICELLE_TABLE.ID_PARTICELLA_CERTIFICATA," +
          "                                                                 PARTICELLE_TABLE.ID_CATALOGO_MATRICE),0) " + 
          "              ) AS TOTALE " +
          "      FROM ( ";
			//*********************************************************

			query += " SELECT SP.ID_STORICO_PARTICELLA, " +
						   "        SP.COMUNE, " +
						   "        C.DESCOM, " +
						   "        P.SIGLA_PROVINCIA, " +
						   "        SP.SEZIONE, " +
						   "        SP.FOGLIO, " +
						   "        SP.PARTICELLA, " +
						   "        SP.SUBALTERNO, " +
						   "        SP.SUP_CATASTALE, " +
						   "        PART.FLAG_SCHEDARIO, " +
						   "        UAD.ID_UNITA_ARBOREA_DICHIARATA, " +
						   "        UAD.ID_STORICO_UNITA_ARBOREA, " +
						   "        UAD.ID_CATALOGO_MATRICE, " +
						   "        UAD.CODICE_FOTOGRAFIA_TERRENI, " +
						   "        UAD.PROGR_UNAR, " +
						   "        UAD.DATA_INIZIO_VALIDITA, " +
						   "        UAD.DATA_FINE_VALIDITA, " +
						   "        UAD.DATA_LAVORAZIONE, " +
						   "        UAD.ID_TIPOLOGIA_UNAR, " +
						   "        TTU.DESCRIZIONE AS DESC_TIPO_UNAR, " +
						   "        UAD.AREA, " +
						   "        UAD.SESTO_SU_FILA, " +
						   "        UAD.SESTO_TRA_FILE, " +
						   "        UAD.NUM_CEPPI, " +
						   "        UAD.ANNO_IMPIANTO, " +
						   "        UAD.DATA_IMPIANTO, " +
						   "        UAD.ANNO_REINNESTO, " +
						   "        UAD.ID_FORMA_ALLEVAMENTO, " +
						   "        TFA.DESCRIZIONE AS DESC_FORMA_ALLEVAMENTO, " +
						   "        UAD.ID_IRRIGAZIONE_UNAR, " +
						   "        UAD.ID_COLTIVAZIONE_UNAR, " +
						   "        UAD.CODICE_TIPO_VARIETA, " +
						   "        UAD.PRESENZA_ALTRI_VITIGNI, " +
						   "        UAD.NUMERO_PIANTE_PRODUTTIVO, " +
						   "        UAD.NUMERO_ALTRE_PIANTE, " +
						   "        UAD.CAMPAGNA, " +
						   "        UAD.ID_TIPOLOGIA_VIGNETO, " +
						   "        UAD.TIPO_IMPIANTO, " +
						   "        UAD.NUMERO_CASTAGNI, " +
						   "        UAD.GRUPPO, " +
						   "        UAD.RICADUTA, " +
						   "        UAD.ID_GIACITURA_UNAR, " +
						   "        UAD.ID_ROCCIA_UNAR, " +
						   "        UAD.ID_SCHELETRO_UNAR, " +
						   "        UAD.ID_STATO_VEGETATIVO_UNAR, " +
						   "        UAD.ID_POTATURA_UNAR, " +
						   "        UAD.ID_GIUDIZIO_UNAR, " +
						   "        UAD.SUPPLEMENTARI, " +
						   "        UAD.MECCANIZZABILE, " +
						   "        UAD.DIMENSIONE_CHIOMA, " +
						   "        UAD.ID_ETA_IMPIANTO_UNAR, " +
						   "        UAD.PROVINCIA_CCIAA, " +
						   "        UAD.MATRICOLA_CCIAA, " +
						   "        UAD.CONFERMA_PREC_ISCRIZIONE_ALBO, " +
						   "        UAD.RICHIESTA_NUOVA_ISCR_ALBO, " +
						   "        UAD.CONFERMA_RICH_NUOVA_ISCR_ALBO, " +
						   "        UAD.SUPERFICIE_DA_ISCRIVERE_ALBO, " +
						   "        UAD.ANNO_ISCRIZIONE_ALBO, " +
						   "        UAD.ID_FONTE, " +
						   "        UAD.ID_VARIAZIONE_UNAR, " +
						   "        UAD.NOTE, " +
						   "        UAD.DATA_AGGIORNAMENTO, " +
						   "        UAD.ID_UTENTE_AGGIORNAMENTO, " +
						   "        RCM.ID_VARIETA, " +
						   "        TVAR.DESCRIZIONE AS DESC_VARIETA, " +
						   "        TVAR.CODICE_VARIETA AS COD_VAR, " +
						   "        RCM.ID_UTILIZZO, " +
						   "        TU.CODICE, " +
						   "        TU.DESCRIZIONE AS DESC_TIPO_UTILIZZO, " +
						   "        UAD.PERCENTUALE_VARIETA, " +
						   "        UAD.DATA_ESECUZIONE, " +
						   "        UAD.ESITO_CONTROLLO, " +
						   "        UAD.ID_TIPOLOGIA_VINO, " +
               "        TTV.DESCRIZIONE AS DESC_TIPO_VINO, " +
						   "        PC.ID_PARTICELLA_CERTIFICATA, "+
						   "        PC.SUP_COLT_ARBOREA_SPECIALIZZATA, " +
						   "        PC.SUP_GRAFICA, " +
						   "        PC.SUP_USO_GRAFICA, " +
						   "        UAD.STATO_UNITA_ARBOREA, " +
						   "        UAD.ANNO_RIFERIMENTO, " +
						   "        DC.DATA_INSERIMENTO_DICHIARAZIONE, " +
						   "        UAD.VIGNA " +
						   " FROM   DB_STORICO_PARTICELLA SP, " +
						   "        COMUNE C, " +
						   "        PROVINCIA P, " +
						   "        DB_PARTICELLA PART, " +
						   "        DB_UNITA_ARBOREA_DICHIARATA UAD, " +
						   "        DB_R_CATALOGO_MATRICE RCM, " +
						   "        DB_TIPO_TIPOLOGIA_UNAR TTU, " +
						   "        DB_TIPO_FORMA_ALLEVAMENTO TFA, " +
						   "        DB_TIPO_VARIETA TVAR, " +
						   "        DB_TIPO_UTILIZZO TU, " +
						   "        DB_UTE U, " +
						   "        DB_CONDUZIONE_DICHIARATA CD, " +
						   "        DB_TIPO_TITOLO_POSSESSO TTP, " +
						   "        DB_PARTICELLA_CERTIFICATA PC, " +
						   "        DB_DICHIARAZIONE_CONSISTENZA DC, " +
						   "        DB_TIPO_TIPOLOGIA_VINO TTV " +
						   " WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
						   " AND    DC.CODICE_FOTOGRAFIA_TERRENI = UAD.CODICE_FOTOGRAFIA_TERRENI " +
						   " AND    UAD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +  
						   " AND    SP.ID_PARTICELLA = PART.ID_PARTICELLA " +  
						   " AND    SP.COMUNE = C.ISTAT_COMUNE " +  
						   " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
						   " AND    UAD.ID_TIPOLOGIA_UNAR = TTU.ID_TIPOLOGIA_UNAR(+) " +  
						   " AND    UAD.ID_FORMA_ALLEVAMENTO = TFA.ID_FORMA_ALLEVAMENTO(+) " +
						   " AND    UAD.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE(+) " +  
						   " AND    RCM.ID_VARIETA = TVAR.ID_VARIETA(+) " +  
						   " AND    RCM.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
						   " AND    UAD.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO(+) " +
						   " AND    UAD.ID_AZIENDA = ? " +  
						   " AND    UAD.ID_AZIENDA = U.ID_AZIENDA " +
						   " AND    TRUNC(U.DATA_INIZIO_ATTIVITA) <= TRUNC(DC.DATA) " +
						   " AND    TRUNC(NVL(U.DATA_FINE_ATTIVITA, ?)) > TRUNC(DC.DATA) " +
						   " AND    U.ID_UTE = CD.ID_UTE " +  
						   " AND    CD.ID_STORICO_PARTICELLA = UAD.ID_STORICO_PARTICELLA " +  
						   " AND    CD.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO " +  
						   " AND    SP.COMUNE = PC.COMUNE(+) " +  
						   " AND    NVL(SP.SEZIONE, '-') = NVL(PC.SEZIONE(+), '-') " +  
						   " AND    SP.FOGLIO = PC.FOGLIO(+) " +  
						   " AND    SP.PARTICELLA = PC.PARTICELLA(+) " +  
						   " AND    NVL(SP.SUBALTERNO, '-') = NVL(PC.SUBALTERNO(+), '-') " +
						   " AND    PC.DATA_INIZIO_VALIDITA(+) <= ? " +
				       " AND    TRUNC(NVL(PC.DATA_FINE_VALIDITA(+), TO_DATE('31/12/9999', 'DD/MM/YYYY'))) > ? "; 
			// Se l'utente ha indicato la destinazione produttiva
			if(filtriUnitaArboreaRicercaVO.getIdUtilizzo() != null) {
				query +=   " AND    RCM.ID_UTILIZZO = ? ";
			}
			// Se l'utente ha indicato il vitigno
			if(filtriUnitaArboreaRicercaVO.getIdVarieta() != null) {
				query +=   " AND    RCM.ID_VARIETA = ? ";
			}
			// Se l'utente ha selezionato la tipologia del vino
      //if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdTipologiaVino())) {
      //  query += " AND      UAD.ID_TIPOLOGIA_VINO = ? ";
      //}
	      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdTipologiaVino())&&
	    		  filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue()!=new Long(-1)) {
	    	  
			  //IdTipologiaVino()==-1 = qualunque tipologia di vino
			  //IdTipologiaVino()==0 = senza tipologia di vino
	    	  
			if(filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue()==new Long(0)){
			  //senza tipologia di vino
				query += " AND      UAD.ID_TIPOLOGIA_VINO IS NULL ";
			  
	    	}
	    	else query += " AND     UAD.ID_TIPOLOGIA_VINO = ? ";
	      }      
			// Se l'utente ha indicato il comune di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIstatComune())) {
				query += " AND SP.COMUNE = ? ";
			}
			// Se l'utente ha indicato la sezione di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSezione())) {
				query += " AND SP.SEZIONE = ? ";
			}
			// Se l'utente ha indicato il foglio di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getFoglio())) {
				query += " AND SP.FOGLIO = ? ";
			}
			// Se l'utente ha indicato la particella di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getParticella())) {
				query += " AND SP.PARTICELLA = ? ";
			}
			// Se l'utente ha indicato il subalterno di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSubalterno())) {
				query += " AND SP.SUBALTERNO = ? ";
			}
			// Se l'utente ha specificato la tipologia di anomalia bloccante
			boolean isFirst = true;
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneBloccante())) {
				query += " AND (UAD.ESITO_CONTROLLO = ? ";
				isFirst = false;
			}
			// Se l'utente ha specificato la tipologia di anomalia warning
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneWarning())) {
				if(!isFirst) {
					query += " OR ";
				}
				else {
					query += " AND (";
				}
				query += " UAD.ESITO_CONTROLLO = ? ";
				isFirst = false;
			}
			// Se l'utente ha specificato la tipologia di anomalia OK
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneOk())) {
				if(!isFirst) {
					query += " OR ";
				}
				else {
					query += " AND (";
				}
				query += " UAD.ESITO_CONTROLLO = ? ";
				isFirst = false;
			}
			if(!isFirst) {
				query += ")";
			}
			query += " GROUP BY SP.ID_STORICO_PARTICELLA, " +
					 "          SP.COMUNE, " +
					 "          C.DESCOM, " +
					 "          P.SIGLA_PROVINCIA, " +
					 "          SP.SEZIONE, " +
					 "          SP.FOGLIO, " +
					 "          SP.PARTICELLA, " +
					 "          SP.SUBALTERNO, " +
					 "          SP.SUP_CATASTALE, " +
					 "          PART.FLAG_SCHEDARIO, " +
					 "          UAD.ID_UNITA_ARBOREA_DICHIARATA, " +
					 "          UAD.ID_CATALOGO_MATRICE, " +
					 "          UAD.ID_STORICO_UNITA_ARBOREA, " +
					 "          UAD.CODICE_FOTOGRAFIA_TERRENI, " +
					 "          UAD.PROGR_UNAR, " +
					 "          UAD.DATA_INIZIO_VALIDITA, " +
					 "          UAD.DATA_FINE_VALIDITA, " +
					 "          UAD.DATA_LAVORAZIONE, " +
					 "          UAD.ID_TIPOLOGIA_UNAR, " +
					 "          TTU.DESCRIZIONE, " +
					 "          UAD.AREA, " +
					 "          UAD.SESTO_SU_FILA, " +
					 "          UAD.SESTO_TRA_FILE, " +
					 "          UAD.NUM_CEPPI, " +
					 "          UAD.ANNO_IMPIANTO, " +
					 "          UAD.DATA_IMPIANTO, " +
					 "          UAD.ANNO_REINNESTO, " +
					 "          UAD.ID_FORMA_ALLEVAMENTO, " +
					 "          TFA.DESCRIZIONE, " +
					 "          UAD.ID_IRRIGAZIONE_UNAR, " +
					 "          UAD.ID_COLTIVAZIONE_UNAR, " +
					 "          UAD.CODICE_TIPO_VARIETA, " +
					 "          UAD.PRESENZA_ALTRI_VITIGNI, " +
					 "          UAD.NUMERO_PIANTE_PRODUTTIVO, " +
					 "          UAD.NUMERO_ALTRE_PIANTE, " +
					 "          UAD.CAMPAGNA, " +
					 "          UAD.ID_TIPOLOGIA_VIGNETO, " +
					 "          UAD.TIPO_IMPIANTO, " +	
					 "          UAD.NUMERO_CASTAGNI, " +
					 "          UAD.GRUPPO, " +
					 "          UAD.RICADUTA, " +
					 "          UAD.ID_GIACITURA_UNAR, " +
					 "          UAD.ID_ROCCIA_UNAR, " +
					 "          UAD.ID_SCHELETRO_UNAR, " +
					 "          UAD.ID_STATO_VEGETATIVO_UNAR, " +
					 "          UAD.ID_POTATURA_UNAR, " +
					 "          UAD.ID_GIUDIZIO_UNAR, " +
					 "          UAD.SUPPLEMENTARI, " +
					 "          UAD.MECCANIZZABILE, " +
					 "          UAD.DIMENSIONE_CHIOMA, " +
					 "          UAD.ID_ETA_IMPIANTO_UNAR, " +
					 "          UAD.PROVINCIA_CCIAA, " +
					 "          UAD.MATRICOLA_CCIAA, " +
					 "          UAD.CONFERMA_PREC_ISCRIZIONE_ALBO, " +
					 "          UAD.RICHIESTA_NUOVA_ISCR_ALBO, " +
					 "          UAD.CONFERMA_RICH_NUOVA_ISCR_ALBO, " +
					 "          UAD.SUPERFICIE_DA_ISCRIVERE_ALBO, " +
					 "          UAD.ANNO_ISCRIZIONE_ALBO, " +
					 "          UAD.ID_FONTE, " +
					 "          UAD.ID_VARIAZIONE_UNAR, " +
					 "          UAD.NOTE, " +
					 "          UAD.DATA_AGGIORNAMENTO, " +
					 "          UAD.ID_UTENTE_AGGIORNAMENTO, " +
					 "          RCM.ID_VARIETA, " +
					 "          TVAR.DESCRIZIONE, " +
					 "          TVAR.CODICE_VARIETA, " +
					 "          RCM.ID_UTILIZZO, " +
					 "          TU.CODICE, " +
					 "          TU.DESCRIZIONE, " +
					 "          UAD.PERCENTUALE_VARIETA, " +
					 "          UAD.DATA_ESECUZIONE, " +
					 "          UAD.ESITO_CONTROLLO, " +
					 "          UAD.ID_TIPOLOGIA_VINO, " +
           "          TTV.DESCRIZIONE, " +
					 "          PC.ID_PARTICELLA_CERTIFICATA, "+
					 "          PC.SUP_COLT_ARBOREA_SPECIALIZZATA, " +
					 "          PC.SUP_GRAFICA, " +
					 "          PC.SUP_USO_GRAFICA, " +
					 "          UAD.STATO_UNITA_ARBOREA, " +
					 "          UAD.ANNO_RIFERIMENTO, " +
					 "          DC.DATA_INSERIMENTO_DICHIARAZIONE, " +
					 "          UAD.VIGNA ";
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
			
			//NUOVA ELEGGIBILITA FITTIZIA
      query += " ) PARTICELLE_TABLE ) PARTICELLE_TABLE_SUM ";
      //**********************************

			SolmrLogger.debug(this, "Value of parameter 1 [ID_DICHIARAZIONE_CONSISTENZA] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue()+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ID_AZIENDA] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [DATA] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime())+"\n");

			stmt = conn.prepareStatement(query);
			
			int indice = 0;
			
			stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue());
			stmt.setLong(++indice, idAzienda.longValue());
			stmt.setDate(++indice, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
			SolmrLogger.debug(this, "Value of parameter 4 [DATA_INSERIMENTO_DICHIARAZIONE] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getDataInserimentoDichiarazione()+"\n");
			stmt.setDate(++indice, new java.sql.Date(filtriUnitaArboreaRicercaVO.getDataInserimentoDichiarazione().getTime()));
			SolmrLogger.debug(this, "Value of parameter 5 [DATA_INSERIMENTO_DICHIARAZIONE] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getDataInserimentoDichiarazione()+"\n");
			stmt.setDate(++indice, new java.sql.Date(filtriUnitaArboreaRicercaVO.getDataInserimentoDichiarazione().getTime()));
			// Se l'utente ha indicato la destinazione produttiva
			if(filtriUnitaArboreaRicercaVO.getIdUtilizzo() != null) {
				stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdUtilizzo().longValue());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_UTILIZZO] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getIdUtilizzo()+"\n");
			}
			// Se l'utente ha indicato il vitigno
			if(filtriUnitaArboreaRicercaVO.getIdVarieta() != null) {
				stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdVarieta().longValue());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_VARIETA] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getIdVarieta()+"\n");
			}
			// Se l'utente ha indicato il tipo vino
      //if(filtriUnitaArboreaRicercaVO.getIdTipologiaVino() != null) {
      //  stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue());
      //  SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_TIPOLOGIA_VINO] in searchUnitaArboreaDichiarataByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIdTipologiaVino()+"\n");
      //}
		if(filtriUnitaArboreaRicercaVO.getIdTipologiaVino() != null &&
   	    	 filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue() != new Long(-1) &&
   	    	 filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue() != new Long(0)) {
   	        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue());
   	        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_TIPOLOGIA_VINO] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getIdTipologiaVino()+"\n");
   	      }
      
			// Se l'utente ha indicato il comune di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIstatComune())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getIstatComune());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[ISTAT_COMUNE] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getIstatComune()+"\n");
			}
			// Se l'utente ha indicato la sezione di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSezione())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getSezione().toUpperCase());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[SEZIONE] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getSezione().toUpperCase()+"\n");
			}
			// Se l'utente ha indicato il foglio di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getFoglio())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getFoglio());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[FOGLIO] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getFoglio()+"\n");
			}
			// Se l'utente ha indicato la particella di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getParticella())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getParticella());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[PARTICELLA] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getParticella()+"\n");
			}
			// Se l'utente ha indicato il subalterno di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSubalterno())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getSubalterno());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[SUBALTERNO] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getSubalterno()+"\n");
			}
			// SEGNALAZIONI:
			// Se l'utente ha specificato la tipologia di anomalia bloccante
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneBloccante())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getTipoSegnalazioneBloccante());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[TIPO_SEGNALAZIONE_BLOCCANTE] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getTipoSegnalazioneBloccante()+"\n");
			}
			// Se l'utente ha specificato la tipologia di anomalia warning
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneWarning())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getTipoSegnalazioneWarning());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[TIPO_SEGNALAZIONE_WARNING] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getTipoSegnalazioneWarning()+"\n");
			}
			// Se l'utente ha specificato la tipologia di anomalia OK
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneOk())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getTipoSegnalazioneOk());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[TIPO_SEGNALAZIONE_OK] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getTipoSegnalazioneOk()+"\n");					
			}

			SolmrLogger.debug(this, "Executing searchUnitaArboreaDichiarataByParameters: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
				ParticellaVO particellaVO = new ParticellaVO();
				UnitaArboreaDichiarataVO unitaArboreaDichiarataVO = new UnitaArboreaDichiarataVO();
				ParticellaCertificataVO particellaCertificataVO = new ParticellaCertificataVO();
				storicoParticellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
				storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
		    	ComuneVO comuneVO = new ComuneVO();
		    	comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
		    	comuneVO.setDescom(rs.getString("DESCOM"));
		    	storicoParticellaVO.setComuneParticellaVO(comuneVO);
		    	storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
		    	storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
		    	storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
		    	storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
		    	storicoParticellaVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
		    	particellaVO.setFlagSchedario(rs.getString("FLAG_SCHEDARIO"));
		    	storicoParticellaVO.setParticellaVO(particellaVO);
		    	unitaArboreaDichiarataVO.setIdUnitaArboreaDichiarata(new Long(rs.getLong("ID_UNITA_ARBOREA_DICHIARATA")));
		    	unitaArboreaDichiarataVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
		    	unitaArboreaDichiarataVO.setCodiceFotografiaTerreni(rs.getString("CODICE_FOTOGRAFIA_TERRENI"));
		    	unitaArboreaDichiarataVO.setProgrUnar(rs.getString("PROGR_UNAR"));
		    	unitaArboreaDichiarataVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
		    	unitaArboreaDichiarataVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
		    	unitaArboreaDichiarataVO.setDataLavorazione(rs.getDate("DATA_LAVORAZIONE"));
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_UNAR"))) {
					unitaArboreaDichiarataVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
					TipoTipologiaUnitaArboreaVO tipoTipologiaUnitaArboreaVO = new TipoTipologiaUnitaArboreaVO();
					tipoTipologiaUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
					tipoTipologiaUnitaArboreaVO.setDescrizione(rs.getString("DESC_TIPO_UNAR"));
					unitaArboreaDichiarataVO.setTipoTipologiaUnitaArboreaVO(tipoTipologiaUnitaArboreaVO);
				}
				unitaArboreaDichiarataVO.setArea(rs.getString("AREA"));
				unitaArboreaDichiarataVO.setSestoSuFila(rs.getString("SESTO_SU_FILA"));
				unitaArboreaDichiarataVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
				unitaArboreaDichiarataVO.setNumCeppi(rs.getString("NUM_CEPPI"));
				unitaArboreaDichiarataVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
				unitaArboreaDichiarataVO.setDataImpianto(rs.getDate("DATA_IMPIANTO"));
				unitaArboreaDichiarataVO.setAnnoReinnesto(rs.getString("ANNO_REINNESTO"));
				if(Validator.isNotEmpty(rs.getString("ID_FORMA_ALLEVAMENTO"))) {
					unitaArboreaDichiarataVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
					TipoFormaAllevamentoVO tipoFormaAllevamentoVO = new TipoFormaAllevamentoVO();
					tipoFormaAllevamentoVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
					tipoFormaAllevamentoVO.setDescrizione(rs.getString("DESC_FORMA_ALLEVAMENTO"));
					unitaArboreaDichiarataVO.setTipoFormaAllevamentoVO(tipoFormaAllevamentoVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_IRRIGAZIONE_UNAR"))) {
					unitaArboreaDichiarataVO.setIdIrrigazioneUnar(new Long(rs.getLong("ID_IRRIGAZIONE_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_COLTIVAZIONE_UNAR"))) {
					unitaArboreaDichiarataVO.setIdColtivazioneUnar(new Long(rs.getLong("ID_COLTIVAZIONE_UNAR")));
				}
				unitaArboreaDichiarataVO.setCodiceTipoVarieta(rs.getString("CODICE_TIPO_VARIETA"));
				unitaArboreaDichiarataVO.setPresenzaAltriVitigni(rs.getString("PRESENZA_ALTRI_VITIGNI"));
				unitaArboreaDichiarataVO.setNumeroPianteProduttivo(rs.getString("NUMERO_PIANTE_PRODUTTIVO"));
				unitaArboreaDichiarataVO.setNumeroAltrePiante(rs.getString("NUMERO_ALTRE_PIANTE"));
				unitaArboreaDichiarataVO.setCampagna(rs.getString("CAMPAGNA"));
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VIGNETO"))) {
					unitaArboreaDichiarataVO.setIdTipologiaVigneto(new Long(rs.getLong("ID_TIPOLOGIA_VIGNETO")));
				}
				unitaArboreaDichiarataVO.setTipoImpianto(rs.getString("TIPO_IMPIANTO"));
				unitaArboreaDichiarataVO.setNumeroCastagni(rs.getString("NUMERO_CASTAGNI"));
				unitaArboreaDichiarataVO.setGruppo(rs.getString("GRUPPO"));
				unitaArboreaDichiarataVO.setRicaduta(rs.getString("RICADUTA"));
				if(Validator.isNotEmpty(rs.getString("ID_GIACITURA_UNAR"))) {
					unitaArboreaDichiarataVO.setIdGiacituraUnar(new Long(rs.getLong("ID_GIACITURA_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_ROCCIA_UNAR"))) {
					unitaArboreaDichiarataVO.setIdRocciaUnar(new Long(rs.getLong("ID_ROCCIA_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_SCHELETRO_UNAR"))) {
					unitaArboreaDichiarataVO.setIdScheletroUnar(new Long(rs.getLong("ID_SCHELETRO_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_STATO_VEGETATIVO_UNAR"))) {
					unitaArboreaDichiarataVO.setIdStatoVegetativoUnar(new Long(rs.getLong("ID_STATO_VEGETATIVO_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_POTATURA_UNAR"))) {
					unitaArboreaDichiarataVO.setIdPotaturaUnar(new Long(rs.getLong("ID_POTATURA_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_GIUDIZIO_UNAR"))) {
					unitaArboreaDichiarataVO.setIdGiudizioUnar(new Long(rs.getLong("ID_GIUDIZIO_UNAR")));
				}
				unitaArboreaDichiarataVO.setSupplementari(rs.getString("SUPPLEMENTARI"));
				unitaArboreaDichiarataVO.setMeccanizzabile(rs.getString("MECCANIZZABILE"));
				unitaArboreaDichiarataVO.setDimensioneChioma(rs.getString("DIMENSIONE_CHIOMA"));
				if(Validator.isNotEmpty(rs.getString("ID_ETA_IMPIANTO_UNAR"))) {
					unitaArboreaDichiarataVO.setIdEtaImpiantoUnar(new Long(rs.getLong("ID_ETA_IMPIANTO_UNAR")));
				}
				unitaArboreaDichiarataVO.setProvinciaCCIAA(rs.getString("PROVINCIA_CCIAA"));
				unitaArboreaDichiarataVO.setMatricolaCCIAA(rs.getString("MATRICOLA_CCIAA"));
				unitaArboreaDichiarataVO.setConfermaPrecIscrizioneAlbo(rs.getString("CONFERMA_PREC_ISCRIZIONE_ALBO"));
				unitaArboreaDichiarataVO.setRichiestaNuovaIscrAlbo(rs.getString("RICHIESTA_NUOVA_ISCR_ALBO"));
				unitaArboreaDichiarataVO.setConfermaRichNuovaIscrAlbo(rs.getString("CONFERMA_RICH_NUOVA_ISCR_ALBO"));
				unitaArboreaDichiarataVO.setSuperficieDaIscrivereAlbo(rs.getString("SUPERFICIE_DA_ISCRIVERE_ALBO"));
				unitaArboreaDichiarataVO.setAnnoIscrizioneAlbo(rs.getString("ANNO_ISCRIZIONE_ALBO"));
				unitaArboreaDichiarataVO.setIdFonte(new Long(rs.getLong("ID_FONTE")));
				unitaArboreaDichiarataVO.setNote(rs.getString("NOTE"));
				unitaArboreaDichiarataVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
				unitaArboreaDichiarataVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) {
					unitaArboreaDichiarataVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
					TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
					tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
					tipoVarietaVO.setDescrizione(rs.getString("DESC_VARIETA"));
					tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VAR"));
					unitaArboreaDichiarataVO.setTipoVarietaVO(tipoVarietaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO"))) {
					unitaArboreaDichiarataVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
					TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
					tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
					tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
					tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTILIZZO"));
					unitaArboreaDichiarataVO.setTipoUtilizzoVO(tipoUtilizzoVO);
				}
				unitaArboreaDichiarataVO.setPercentualeVarieta(rs.getString("PERCENTUALE_VARIETA"));
				if(rs.getDate("DATA_ESECUZIONE") != null) {
					unitaArboreaDichiarataVO.setDataEsecuzione(new Timestamp(rs.getDate("DATA_ESECUZIONE").getTime()));
				}
				unitaArboreaDichiarataVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
				unitaArboreaDichiarataVO.setStatoUnitaArborea(rs.getString("STATO_UNITA_ARBOREA"));
				unitaArboreaDichiarataVO.setAnnoRiferimento(rs.getString("ANNO_RIFERIMENTO"));
				particellaCertificataVO.setSupColtArboreaSpecializzata(rs.getString("SUP_COLT_ARBOREA_SPECIALIZZATA"));
				//Nuova Eleggibilita Inizio ************
        Vector<ParticellaCertElegVO> vPartCertEleg = null;
        if(rs.getBigDecimal("SUPERFICIE_ELEG") !=null)
        {
          vPartCertEleg = new Vector<ParticellaCertElegVO>();
          ParticellaCertElegVO partCertElegVO = new ParticellaCertElegVO();
          partCertElegVO.setSuperficie(rs.getBigDecimal("SUPERFICIE_ELEG"));
          vPartCertEleg.add(partCertElegVO);
        }
        particellaCertificataVO.setVParticellaCertEleg(vPartCertEleg);
        particellaCertificataVO.setSupGrafica(rs.getString("SUP_GRAFICA"));
        particellaCertificataVO.setSupUsoGrafica(rs.getString("SUP_USO_GRAFICA"));
        //Nuova Eleggibilta Fine ***********
        
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VINO"))) {
          unitaArboreaDichiarataVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
          TipoTipologiaVinoVO tipoTipologiaVinoVO = new TipoTipologiaVinoVO();
          tipoTipologiaVinoVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
          tipoTipologiaVinoVO.setDescrizione(rs.getString("DESC_TIPO_VINO"));
          unitaArboreaDichiarataVO.setTipoTipologiaVinoVO(tipoTipologiaVinoVO);
        }
        
        unitaArboreaDichiarataVO.setVigna(rs.getString("VIGNA"));
        storicoParticellaVO.setUnitaArboreaDichiarataVO(unitaArboreaDichiarataVO);
				storicoParticellaVO.setParticellaCertificataVO(particellaCertificataVO);
				elencoParticelleArboreeDichiarate.add(storicoParticellaVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "searchUnitaArboreaDichiarataByParameters in UnitaArboreaDichiarataDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "searchUnitaArboreaDichiarataByParameters in UnitaArboreaDichiarataDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "searchUnitaArboreaDichiarataByParameters in UnitaArboreaDichiarataDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "searchUnitaArboreaDichiarataByParameters in UnitaArboreaDichiarataDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO\n");
		if(elencoParticelleArboreeDichiarate.size() == 0) {
			return (StoricoParticellaVO[])elencoParticelleArboreeDichiarate.toArray(new StoricoParticellaVO[0]);
		}
		else {
			return (StoricoParticellaVO[])elencoParticelleArboreeDichiarate.toArray(new StoricoParticellaVO[elencoParticelleArboreeDichiarate.size()]);
		}
	}
	
	
	
	/**
   * Metodo utilizzato per estrarre l'elenco delle particelle arboree(storico
   * particelle e unita arborea dichiarata) di una data azienda alla dichiarazione
   * di consistenza selezionata
   * 
   * @param idAzienda
   * @param filtriUnitaArboreaRicercaVO
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws DataAccessException
   */
  public StoricoParticellaVO[] searchUnitaArboreaDichiarataByParameters(Long idAzienda, 
      FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO, String[] orderBy) 
  throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelleArboreeDichiarate = new Vector<StoricoParticellaVO>();

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO and it values: "+conn+"\n");
      
      //***************Nuova eleggibilità fittizia *************
      String query = " " +
        "SELECT PARTICELLE_TABLE_SUM.* , " +
        "       TOTALE AS SUPERFICIE_ELEG, " +
        "       (SELECT 'P30' " +
        "        FROM   DB_DICHIARAZIONE_SEGNALAZIONE DS " +
        "        WHERE  DS.ID_STORICO_PARTICELLA = PARTICELLE_TABLE_SUM.ID_STORICO_PARTICELLA " +
        "        AND    DS.ID_CONTROLLO = 211 " +
        "        AND    DS.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "        AND    DS.ID_AZIENDA = ? " +
        "        AND    ROWNUM = 1 " +
        "       ) AS P30, " +
        "       (SELECT 'P25' " +
        "        FROM   DB_DICHIARAZIONE_SEGNALAZIONE DS " +
        "        WHERE  DS.ID_STORICO_PARTICELLA = PARTICELLE_TABLE_SUM.ID_STORICO_PARTICELLA " +
        "        AND    DS.ID_CONTROLLO = 213 " +
        "        AND    DS.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "        AND    DS.ID_AZIENDA = ? " +
        "        AND    ROWNUM = 1 " +
        "       ) AS P25, " +
        "       (SELECT 'P26' " +
        "        FROM   DB_DICHIARAZIONE_SEGNALAZIONE DS " +
        "        WHERE  DS.ID_STORICO_PARTICELLA = PARTICELLE_TABLE_SUM.ID_STORICO_PARTICELLA " +
        "        AND    DS.ID_CONTROLLO = 518 " +
        "        AND    DS.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "        AND    DS.ID_AZIENDA = ? " +
        "        AND    ROWNUM = 1 " +
        "       ) AS P26, " +
        "       (CASE " +
        "        WHEN ( PARTICELLE_TABLE_SUM.DATA_SOSPENSIONE <= PARTICELLE_TABLE_SUM.DATA_INSERIMENTO_DICHIARAZIONE ) " +
        "        THEN 'SOSPESA_GIS' " +
        "        END" + 
        "       ) AS SOSPESA_GIS, " +
        "       (" +
        "         CASE " +
        "         WHEN EXISTS (SELECT NE.ID_NOTIFICA_ENTITA " + 
        "               FROM   DB_NOTIFICA_ENTITA NE, " +
        "                      DB_TIPO_ENTITA TE " +
        "               WHERE  TE.CODICE_TIPO_ENTITA = 'U' " +
        "               AND    TE.ID_TIPO_ENTITA = NE.ID_TIPO_ENTITA " +
        "               AND    NE.IDENTIFICATIVO = PARTICELLE_TABLE_SUM.ID_UNITA_ARBOREA " +
        "               AND    NE.ID_DICHIARAZIONE_CONSISTENZA = PARTICELLE_TABLE_SUM.ID_DICHIARAZIONE_CONSISTENZA " +
        "               AND    NE.DATA_FINE_VALIDITA IS NULL) " +
        "         THEN 'NOTIFICA' " +
        "         END " +
        "        ) AS IN_NOTIFICA " +
        "FROM " +
        "     (SELECT PARTICELLE_TABLE.*, " +
        "             (NVL(PCK_SMRGAA_LIBRERIA.SelSupElegDichByFotoIdPartVar(PARTICELLE_TABLE.CODICE_FOTOGRAFIA_TERRENI,PARTICELLE_TABLE.DATA_INSERIMENTO_DICHIARAZIONE,PARTICELLE_TABLE.ID_PARTICELLA," +
        "                                                                     PARTICELLE_TABLE.ID_CATALOGO_MATRICE),0)" +
        "             ) AS TOTALE " +
        "      FROM ( " +
        "              WITH ISTANZA_RIESAME AS  " +
        "                (SELECT IR.DATA_RICHIESTA, " +
        "                        (CASE " +
        "                         WHEN (NVL(IR.DATA_EVASIONE, SYSDATE)) <  DCIR.DATA_INSERIMENTO_DICHIARAZIONE " +
        "                         THEN IR.DATA_EVASIONE " +            
        "                        END) AS DATA_EVASIONE, " +                
        "                        IR.ID_PARTICELLA " +
        "                 FROM   DB_ISTANZA_RIESAME IR, " +
        "                        DB_DICHIARAZIONE_CONSISTENZA DCIR " +
        "                        WHERE IR.DATA_RICHIESTA = (SELECT MAX(IRTMP.DATA_RICHIESTA) " +
        "                                                   FROM   DB_ISTANZA_RIESAME IRTMP, " +                                    
        "                                                          DB_DICHIARAZIONE_CONSISTENZA DCIRTMP " +
        "                                                   WHERE  IRTMP.ID_AZIENDA = IR.ID_AZIENDA " +
        "                                                   AND    IRTMP.ID_PARTICELLA = IR.ID_PARTICELLA " +
        "                                                   AND    DCIRTMP.ID_DICHIARAZIONE_CONSISTENZA = DCIR.ID_DICHIARAZIONE_CONSISTENZA " +
        "                                                   AND    IRTMP.DATA_ANNULLAMENTO IS NULL " +
        "                                                   AND    IRTMP.DATA_RICHIESTA < DCIRTMP.DATA_INSERIMENTO_DICHIARAZIONE " +
        "                                                  ) " +
        "                        AND   IR.ID_AZIENDA = ? " +
        "                        AND   DCIR.ID_DICHIARAZIONE_CONSISTENZA = ? ), " +
        "              COND_ELEG AS " +
        "              (SELECT ED.ID_PARTICELLA, " +
        "                      ED.PERCENTUALE_UTILIZZO " +
        "               FROM   DB_ELEGGIBILITA_DICHIARATA ED, " +
        "                      DB_DICHIARAZIONE_CONSISTENZA CD " +
        "               WHERE  CD.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "               AND    CD.CODICE_FOTOGRAFIA_TERRENI = ED.CODICE_FOTOGRAFIA_TERRENI " +
        "               AND    ED.ID_ELEGGIBILITA_FIT = 26 ) ";
      //*********************************************************

      query += 
        " SELECT SP.ID_STORICO_PARTICELLA, " +
        "        SP.COMUNE, " +
        "        C.DESCOM, " +
        "        P.SIGLA_PROVINCIA, " +
        "        SP.SEZIONE, " +
        "        SP.FOGLIO, " +
        "        SP.PARTICELLA, " +
        "        SP.SUBALTERNO, " +
        "        SP.SUP_CATASTALE, " +
        "        SP.SUPERFICIE_GRAFICA, " +
        "        SP.ID_PARTICELLA, " +
        "        SUA.ID_UNITA_ARBOREA, " +
        "        PART.FLAG_SCHEDARIO, " +
        "        UAD.ID_UNITA_ARBOREA_DICHIARATA, " +
        "        UAD.ID_CATALOGO_MATRICE, " +
        "        UAD.ID_STORICO_UNITA_ARBOREA, " +
        "        UAD.CODICE_FOTOGRAFIA_TERRENI, " +
        "        DC.ID_DICHIARAZIONE_CONSISTENZA, " +
        "        UAD.PROGR_UNAR, " +
        "        UAD.DATA_INIZIO_VALIDITA, " +
        "        UAD.DATA_FINE_VALIDITA, " +
        "        UAD.DATA_LAVORAZIONE, " +
        "        UAD.ID_TIPOLOGIA_UNAR, " +
        "        TTU.DESCRIZIONE AS DESC_TIPO_UNAR, " +
        "        UAD.AREA, " +
        "        UAD.SESTO_SU_FILA, " +
        "        UAD.SESTO_TRA_FILE, " +
        "        UAD.NUM_CEPPI, " +
        "        UAD.ANNO_IMPIANTO, " +
        "        UAD.DATA_IMPIANTO, " +
        "        UAD.ANNO_REINNESTO, " +
        "        UAD.ID_FORMA_ALLEVAMENTO, " +
        "        TFA.DESCRIZIONE AS DESC_FORMA_ALLEVAMENTO, " +
        "        UAD.ID_IRRIGAZIONE_UNAR, " +
        "        UAD.ID_COLTIVAZIONE_UNAR, " +
        "        UAD.CODICE_TIPO_VARIETA, " +
        "        UAD.PRESENZA_ALTRI_VITIGNI, " +
        "        UAD.NUMERO_PIANTE_PRODUTTIVO, " +
        "        UAD.NUMERO_ALTRE_PIANTE, " +
        "        UAD.CAMPAGNA, " +
        "        UAD.ID_TIPOLOGIA_VIGNETO, " +
        "        UAD.TIPO_IMPIANTO, " +
        "        UAD.NUMERO_CASTAGNI, " +
        "        UAD.GRUPPO, " +
        "        UAD.RICADUTA, " +
        "        UAD.ID_GIACITURA_UNAR, " +
        "        UAD.ID_ROCCIA_UNAR, " +
        "        UAD.ID_SCHELETRO_UNAR, " +
        "        UAD.ID_STATO_VEGETATIVO_UNAR, " +
        "        UAD.ID_POTATURA_UNAR, " +
        "        UAD.ID_GIUDIZIO_UNAR, " +
        "        UAD.SUPPLEMENTARI, " +
        "        UAD.MECCANIZZABILE, " +
        "        UAD.DIMENSIONE_CHIOMA, " +
        "        UAD.ID_ETA_IMPIANTO_UNAR, " +
        "        UAD.PROVINCIA_CCIAA, " +
        "        UAD.MATRICOLA_CCIAA, " +
        "        UAD.CONFERMA_PREC_ISCRIZIONE_ALBO, " +
        "        UAD.RICHIESTA_NUOVA_ISCR_ALBO, " +
        "        UAD.CONFERMA_RICH_NUOVA_ISCR_ALBO, " +
        "        UAD.SUPERFICIE_DA_ISCRIVERE_ALBO, " +
        "        UAD.ANNO_ISCRIZIONE_ALBO, " +
        "        UAD.ID_FONTE, " +
        "        UAD.ID_VARIAZIONE_UNAR, " +
        "        UAD.NOTE, " +
        "        UAD.DATA_AGGIORNAMENTO, " +
        "        UAD.ID_UTENTE_AGGIORNAMENTO, " +
        "        UAD.ESITO_TOLLERANZA_GIS AS TOLLERANZA, " +
        "        RCM.ID_VARIETA, " +
        "        TVAR.DESCRIZIONE AS DESC_VARIETA, " +
        "        TVAR.CODICE_VARIETA AS COD_VAR, " +
        "        RCM.ID_UTILIZZO, " +
        "        TU.CODICE, " +
        "        TU.DESCRIZIONE AS DESC_TIPO_UTILIZZO, " +
        "        UAD.PERCENTUALE_VARIETA, " +
        "        UAD.DATA_ESECUZIONE, " +
        "        UAD.ESITO_CONTROLLO, " +
        "        UAD.ID_TIPOLOGIA_VINO, " +
        "        TTV.DESCRIZIONE AS DESC_TIPO_VINO, " +
        "        TTV.FLAG_GESTIONE_VIGNA, " +
        "        PC.ID_PARTICELLA_CERTIFICATA, "+
        "        PC.SUP_COLT_ARBOREA_SPECIALIZZATA, " +
        "        PC.SUP_GRAFICA, " +
        "        PC.SUP_USO_GRAFICA, " +
        "        UAD.STATO_UNITA_ARBOREA, " +
        "        UAD.ANNO_RIFERIMENTO, " +
        "        DC.DATA_INSERIMENTO_DICHIARAZIONE, " +
        "        PC.DATA_SOSPENSIONE, " +
        "        PC.MOTIVAZIONE_GIS, " +
        "        UAD.ID_CAUSALE_MODIFICA, " +
        "        UAD.FLAG_IMPRODUTTIVA, " +
        "        UAD.PERCENTUALE_FALLANZA, " +
        "        TCM.DESCRIZIONE AS DESC_CAUS_MOD, " +
        "        IRI.DATA_RICHIESTA, " +
        "        IRI.DATA_EVASIONE, " +
        "        F.FLAG_STABILIZZAZIONE, " +
        "        SUM(CD.PERCENTUALE_POSSESSO) AS PERCENTUALE_POSSESSO, " +
        "        NVL(CEL.PERCENTUALE_UTILIZZO, SUM(CD.PERCENTUALE_POSSESSO)) AS PERCENTUALE_UTILIZZO " +
        " FROM   DB_STORICO_PARTICELLA SP, " +
        "        DB_STORICO_UNITA_ARBOREA SUA, " +
        "        COMUNE C, " +
        "        PROVINCIA P, " +
        "        DB_PARTICELLA PART, " +
        "        DB_UNITA_ARBOREA_DICHIARATA UAD, " +
        "        DB_R_CATALOGO_MATRICE RCM, " +
        "        DB_TIPO_TIPOLOGIA_UNAR TTU, " +
        "        DB_TIPO_FORMA_ALLEVAMENTO TFA, " +
        "        DB_TIPO_VARIETA TVAR, " +
        "        DB_TIPO_UTILIZZO TU, " +
        "        DB_UTE U, " +
        "        DB_CONDUZIONE_DICHIARATA CD, " +
        "        DB_TIPO_TITOLO_POSSESSO TTP, " +
        "        DB_PARTICELLA_CERTIFICATA PC, " +
        "        DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "        DB_TIPO_TIPOLOGIA_VINO TTV, " +
        "        DB_TIPO_CAUSALE_MODIFICA TCM, " +
        "        ISTANZA_RIESAME IRI, " +
        "        COND_ELEG CEL, " +
        "        DB_FOGLIO F ";
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdControllo()))
      {
        query += 
        "       ,DB_DICHIARAZIONE_SEGNALAZIONE DDS ";
      }
      query +=
        " WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        " AND    DC.CODICE_FOTOGRAFIA_TERRENI = UAD.CODICE_FOTOGRAFIA_TERRENI " +
        " AND    UAD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +  
        " AND    UAD.ID_STORICO_UNITA_ARBOREA = SUA.ID_STORICO_UNITA_ARBOREA " +
        " AND    UAD.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +  
        " AND    SP.ID_PARTICELLA = PART.ID_PARTICELLA " +  
        " AND    SP.COMUNE = C.ISTAT_COMUNE " +  
        " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
        " AND    UAD.ID_TIPOLOGIA_UNAR = TTU.ID_TIPOLOGIA_UNAR(+) " +  
        " AND    UAD.ID_FORMA_ALLEVAMENTO = TFA.ID_FORMA_ALLEVAMENTO(+) " +  
        " AND    UAD.ID_VARIETA = TVAR.ID_VARIETA(+) " +  
        " AND    UAD.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
        " AND    UAD.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO(+) " +
        " AND    UAD.ID_CAUSALE_MODIFICA = TCM.ID_CAUSALE_MODIFICA(+) " +
        " AND    UAD.ID_AZIENDA = ? " +  
        " AND    UAD.ID_AZIENDA = U.ID_AZIENDA " +
        " AND    TRUNC(U.DATA_INIZIO_ATTIVITA) <= TRUNC(DC.DATA) " +
        " AND    TRUNC(NVL(U.DATA_FINE_ATTIVITA, ?)) > TRUNC(DC.DATA) " +
        " AND    U.ID_UTE = CD.ID_UTE " +  
        " AND    CD.ID_STORICO_PARTICELLA = UAD.ID_STORICO_PARTICELLA " +  
        " AND    CD.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO " +
        " AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        " AND    CD.ID_TITOLO_POSSESSO NOT IN (5,6) " +
        " AND    SP.COMUNE = PC.COMUNE(+) " +  
        " AND    NVL(SP.SEZIONE, '-') = NVL(PC.SEZIONE(+), '-') " +  
        " AND    SP.FOGLIO = PC.FOGLIO(+) " +  
        " AND    SP.PARTICELLA = PC.PARTICELLA(+) " +  
        " AND    NVL(SP.SUBALTERNO, '-') = NVL(PC.SUBALTERNO(+), '-') " +
        " AND    PC.DATA_INIZIO_VALIDITA(+) <= ? " +
        " AND    TRUNC(NVL(PC.DATA_FINE_VALIDITA(+), TO_DATE('31/12/9999', 'DD/MM/YYYY'))) > ? " +
        " AND    SP.ID_PARTICELLA = IRI.ID_PARTICELLA(+) " +
        " AND    SP.ID_PARTICELLA = CEL.ID_PARTICELLA (+) " +
        " AND    SP.COMUNE = F.COMUNE(+) " +
        " AND    NVL( SP.SEZIONE,'-') = NVL( F.SEZIONE(+),'-') " +
        " AND    SP.FOGLIO = F.FOGLIO(+) ";
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdControllo()))
      {
        query +=
        " AND    UAD.ID_STORICO_UNITA_ARBOREA = DDS.ID_STORICO_UNITA_ARBOREA " +
        " AND    DDS.ID_DICHIARAZIONE_CONSISTENZA = DC.ID_DICHIARAZIONE_CONSISTENZA " +
        " AND    DDS.ID_CONTROLLO = ? ";
      }
      
      // Se l'utente ha indicato la destinazione produttiva
      if(filtriUnitaArboreaRicercaVO.getIdUtilizzo() != null) {
        query +=   " AND    RCM.ID_UTILIZZO = ? ";
      }
      // Se l'utente ha indicato il vitigno
      if(filtriUnitaArboreaRicercaVO.getIdVarieta() != null) {
        query +=   " AND    RCM.ID_VARIETA = ? ";
      }
      // Se l'utente ha selezionato la tipologia del vino
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdTipologiaVino())&&
            filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue()!=new Long(-1)) 
      {         
        //IdTipologiaVino()==-1 = qualunque tipologia di vino
        //IdTipologiaVino()==0 = senza tipologia di vino          
        if(filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue()==new Long(0))
        {
          //senza tipologia di vino
          query += " AND      UAD.ID_TIPOLOGIA_VINO IS NULL ";
        
        }
        else
        {
          query += " AND     UAD.ID_TIPOLOGIA_VINO = ? ";
        }
      }
      // Se l'utente ha indicato il genere iscrizione
      if(filtriUnitaArboreaRicercaVO.getIdGenereIscrizione() != null) 
      {
        query +=   " AND    UAD.ID_GENERE_ISCRIZIONE = ? ";
      }
      
      //Notifiche
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdTipologiaNotifica())
          || (Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdCategoriaNotifica())))
      {
        query +=   " AND  EXISTS  (SELECT NE.ID_NOTIFICA_ENTITA " + 
                   "               FROM   DB_NOTIFICA_ENTITA NE, " +
                   "                      DB_NOTIFICA NO, " +
                   "                      DB_TIPO_ENTITA TE " +
                   "               WHERE  TE.CODICE_TIPO_ENTITA = 'U' " +
                   "               AND    TE.ID_TIPO_ENTITA = NE.ID_TIPO_ENTITA " +
                   "               AND    NE.IDENTIFICATIVO = SUA.ID_UNITA_ARBOREA " +
                   "               AND    NE.ID_NOTIFICA = NO.ID_NOTIFICA " +
                   "               AND    NE.ID_DICHIARAZIONE_CONSISTENZA = DC.ID_DICHIARAZIONE_CONSISTENZA ";
        if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdTipologiaNotifica()))
        {
          query += "               AND    NO.ID_TIPOLOGIA_NOTIFICA = ? ";          
        }
        if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdCategoriaNotifica()))
        {
          query += "               AND    NO.ID_CATEGORIA_NOTIFICA = ? ";          
        }
        if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getFlagNotificheChiuse()))
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
      
      
      // Se l'utente ha indicato la causale modifica
      if(filtriUnitaArboreaRicercaVO.getIdCausaleModifica() != null) 
      {
        query +=   " AND    UAD.ID_CAUSALE_MODIFICA = ? ";
      }
      // Se l'utente ha indicato la provincia di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIstatProvincia())) {
        query += " AND P.ISTAT_PROVINCIA = ? ";
      }
      // Se l'utente ha indicato il comune di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIstatComune())) {
        query += " AND SP.COMUNE = ? ";
      }
      // Se l'utente ha indicato la sezione di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSezione())) {
        query += " AND SP.SEZIONE = ? ";
      }
      // Se l'utente ha indicato il foglio di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getFoglio())) {
        query += " AND SP.FOGLIO = ? ";
      }
      // Se l'utente ha indicato la particella di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getParticella())) {
        query += " AND SP.PARTICELLA = ? ";
      }
      // Se l'utente ha indicato il subalterno di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSubalterno())) {
        query += " AND SP.SUBALTERNO = ? ";
      }
      // Se l'utente ha specificato la tipologia di anomalia bloccante
      boolean isFirst = true;
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneBloccante())) {
        query += " AND (UAD.ESITO_CONTROLLO = ? ";
        isFirst = false;
      }
      // Se l'utente ha specificato la tipologia di anomalia warning
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneWarning())) {
        if(!isFirst) {
          query += " OR ";
        }
        else {
          query += " AND (";
        }
        query += " UAD.ESITO_CONTROLLO = ? ";
        isFirst = false;
      }
      // Se l'utente ha specificato la tipologia di anomalia OK
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneOk())) {
        if(!isFirst) {
          query += " OR ";
        }
        else {
          query += " AND (";
        }
        query += " UAD.ESITO_CONTROLLO = ? ";
        isFirst = false;
      }
      if(!isFirst) {
        query += ")";
      }
      query += " GROUP BY SP.ID_STORICO_PARTICELLA, " +
           "          SP.COMUNE, " +
           "          C.DESCOM, " +
           "          P.SIGLA_PROVINCIA, " +
           "          SP.SEZIONE, " +
           "          SP.FOGLIO, " +
           "          SP.PARTICELLA, " +
           "          SP.SUBALTERNO, " +
           "          SP.SUP_CATASTALE, " +
           "          SP.SUPERFICIE_GRAFICA, " +
           "          SP.ID_PARTICELLA, " +
           "          SUA.ID_UNITA_ARBOREA, " +
           "          PART.FLAG_SCHEDARIO, " +
           "          UAD.ID_UNITA_ARBOREA_DICHIARATA, " +
           "          UAD.ID_CATALOGO_MATRICE, " +
           "          UAD.ID_STORICO_UNITA_ARBOREA, " +
           "          UAD.CODICE_FOTOGRAFIA_TERRENI, " +
           "          DC.ID_DICHIARAZIONE_CONSISTENZA, " +
           "          UAD.PROGR_UNAR, " +
           "          UAD.DATA_INIZIO_VALIDITA, " +
           "          UAD.DATA_FINE_VALIDITA, " +
           "          UAD.DATA_LAVORAZIONE, " +
           "          UAD.ID_TIPOLOGIA_UNAR, " +
           "          TTU.DESCRIZIONE, " +
           "          UAD.AREA, " +
           "          UAD.SESTO_SU_FILA, " +
           "          UAD.SESTO_TRA_FILE, " +
           "          UAD.NUM_CEPPI, " +
           "          UAD.ANNO_IMPIANTO, " +
           "          UAD.DATA_IMPIANTO, " +
           "          UAD.ANNO_REINNESTO, " +
           "          UAD.ID_FORMA_ALLEVAMENTO, " +
           "          TFA.DESCRIZIONE, " +
           "          UAD.ID_IRRIGAZIONE_UNAR, " +
           "          UAD.ID_COLTIVAZIONE_UNAR, " +
           "          UAD.CODICE_TIPO_VARIETA, " +
           "          UAD.PRESENZA_ALTRI_VITIGNI, " +
           "          UAD.NUMERO_PIANTE_PRODUTTIVO, " +
           "          UAD.NUMERO_ALTRE_PIANTE, " +
           "          UAD.CAMPAGNA, " +
           "          UAD.ID_TIPOLOGIA_VIGNETO, " +
           "          UAD.TIPO_IMPIANTO, " +  
           "          UAD.NUMERO_CASTAGNI, " +
           "          UAD.GRUPPO, " +
           "          UAD.RICADUTA, " +
           "          UAD.ID_GIACITURA_UNAR, " +
           "          UAD.ID_ROCCIA_UNAR, " +
           "          UAD.ID_SCHELETRO_UNAR, " +
           "          UAD.ID_STATO_VEGETATIVO_UNAR, " +
           "          UAD.ID_POTATURA_UNAR, " +
           "          UAD.ID_GIUDIZIO_UNAR, " +
           "          UAD.SUPPLEMENTARI, " +
           "          UAD.MECCANIZZABILE, " +
           "          UAD.DIMENSIONE_CHIOMA, " +
           "          UAD.ID_ETA_IMPIANTO_UNAR, " +
           "          UAD.PROVINCIA_CCIAA, " +
           "          UAD.MATRICOLA_CCIAA, " +
           "          UAD.CONFERMA_PREC_ISCRIZIONE_ALBO, " +
           "          UAD.RICHIESTA_NUOVA_ISCR_ALBO, " +
           "          UAD.CONFERMA_RICH_NUOVA_ISCR_ALBO, " +
           "          UAD.SUPERFICIE_DA_ISCRIVERE_ALBO, " +
           "          UAD.ANNO_ISCRIZIONE_ALBO, " +
           "          UAD.ID_FONTE, " +
           "          UAD.ID_VARIAZIONE_UNAR, " +
           "          UAD.NOTE, " +
           "          UAD.DATA_AGGIORNAMENTO, " +
           "          UAD.ID_UTENTE_AGGIORNAMENTO, " +
           "          UAD.ESITO_TOLLERANZA_GIS, " +
           "          RCM.ID_VARIETA, " +
           "          TVAR.DESCRIZIONE, " +
           "          TVAR.CODICE_VARIETA, " +
           "          RCM.ID_UTILIZZO, " +
           "          TU.CODICE, " +
           "          TU.DESCRIZIONE, " +
           "          UAD.PERCENTUALE_VARIETA, " +
           "          UAD.DATA_ESECUZIONE, " +
           "          UAD.ESITO_CONTROLLO, " +
           "          UAD.ID_TIPOLOGIA_VINO, " +
           "          TTV.DESCRIZIONE, " +
           "          TTV.FLAG_GESTIONE_VIGNA, " +
           "          PC.ID_PARTICELLA_CERTIFICATA, "+
           "          PC.SUP_COLT_ARBOREA_SPECIALIZZATA, " +
           "          PC.SUP_GRAFICA, " +
           "          PC.SUP_USO_GRAFICA, " +
           "          UAD.STATO_UNITA_ARBOREA, " +
           "          UAD.ANNO_RIFERIMENTO, " +
           "          DC.DATA_INSERIMENTO_DICHIARAZIONE, " +
           "          PC.DATA_SOSPENSIONE, " +
           "          PC.MOTIVAZIONE_GIS, " +
           "          UAD.ID_CAUSALE_MODIFICA, " +
           "          UAD.FLAG_IMPRODUTTIVA, " +
           "          UAD.PERCENTUALE_FALLANZA, " +
           "          TCM.DESCRIZIONE, " +
           "          IRI.DATA_RICHIESTA, " +
           "          IRI.DATA_EVASIONE, " +
           "          F.FLAG_STABILIZZAZIONE, " +
           "          CEL.PERCENTUALE_UTILIZZO ";
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
      
      //NUOVA ELEGGIBILITA FITTIZIA
      query += " ) PARTICELLE_TABLE ) PARTICELLE_TABLE_SUM ";
      //**********************************

      SolmrLogger.debug(this, "Value of parameter 1 [ID_DICHIARAZIONE_CONSISTENZA] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue()+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ID_AZIENDA] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+idAzienda+"\n");
      SolmrLogger.debug(this, "Value of parameter 3 [DATA] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime())+"\n");

      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
     
      stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue());
      stmt.setLong(++indice, idAzienda.longValue());
      stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue());
      stmt.setLong(++indice, idAzienda.longValue());
      stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue());
      stmt.setLong(++indice, idAzienda.longValue());
      
      //Istanza di riesame
      stmt.setLong(++indice, idAzienda.longValue());
      stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue());
      
      //Conduzione eleggibile
      stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue());
      
      
      stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue());
      stmt.setLong(++indice, idAzienda.longValue());
      stmt.setDate(++indice, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
      stmt.setDate(++indice, new java.sql.Date(filtriUnitaArboreaRicercaVO.getDataInserimentoDichiarazione().getTime()));
      stmt.setDate(++indice, new java.sql.Date(filtriUnitaArboreaRicercaVO.getDataInserimentoDichiarazione().getTime()));
      //Se l'utente ha selezionato il tipo di controllo
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdControllo()))
      {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdControllo().longValue());
      }
      
      // Se l'utente ha indicato la destinazione produttiva
      if(filtriUnitaArboreaRicercaVO.getIdUtilizzo() != null) {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdUtilizzo().longValue());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_UTILIZZO] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getIdUtilizzo()+"\n");
      }
      // Se l'utente ha indicato il vitigno
      if(filtriUnitaArboreaRicercaVO.getIdVarieta() != null) {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdVarieta().longValue());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_VARIETA] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getIdVarieta()+"\n");
      }
      // Se l'utente ha indicato il tipo vino
      if(filtriUnitaArboreaRicercaVO.getIdTipologiaVino() != null &&
           filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue() != new Long(-1) &&
           filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue() != new Long(0)) 
      {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_TIPOLOGIA_VINO] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getIdTipologiaVino()+"\n");
      }      
      // Se l'utente ha indicato il genere iscrizione
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdGenereIscrizione())) {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdGenereIscrizione().longValue());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_GENERE_ISCRIZIONE] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getIdGenereIscrizione()+"\n");
      }
      // Se l'utente ha indicato la tipologia notifica
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdTipologiaNotifica())) {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdTipologiaNotifica().longValue());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_TIPOLOGIA_NOTIFICA] in searchStoricoUnitaArboreaByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIdTipologiaNotifica()+"\n");
      }
      // Se l'utente ha indicato la categoria notifica
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdCategoriaNotifica())) {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdCategoriaNotifica().longValue());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_CATEGORIA_NOTIFICA] in searchStoricoUnitaArboreaByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIdCategoriaNotifica()+"\n");
      }
      // Se l'utente ha indicato la causale modifica
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdCausaleModifica())) {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdCausaleModifica().longValue());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_CAUSALE_MODIFICA] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getIdCausaleModifica()+"\n");
      }
      // Se l'utente ha indicato il comune di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIstatProvincia())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getIstatProvincia());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ISTAT_PROVINCIA] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getIstatProvincia()+"\n");
      }
      // Se l'utente ha indicato il comune di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIstatComune())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getIstatComune());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ISTAT_COMUNE] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getIstatComune()+"\n");
      }
      // Se l'utente ha indicato la sezione di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSezione())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getSezione().toUpperCase());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[SEZIONE] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getSezione().toUpperCase()+"\n");
      }
      // Se l'utente ha indicato il foglio di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getFoglio())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getFoglio());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[FOGLIO] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getFoglio()+"\n");
      }
      // Se l'utente ha indicato la particella di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getParticella())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getParticella());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[PARTICELLA] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getParticella()+"\n");
      }
      // Se l'utente ha indicato il subalterno di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSubalterno())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getSubalterno());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[SUBALTERNO] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getSubalterno()+"\n");
      }
      // SEGNALAZIONI:
      // Se l'utente ha specificato la tipologia di anomalia bloccante
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneBloccante())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getTipoSegnalazioneBloccante());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[TIPO_SEGNALAZIONE_BLOCCANTE] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getTipoSegnalazioneBloccante()+"\n");
      }
      // Se l'utente ha specificato la tipologia di anomalia warning
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneWarning())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getTipoSegnalazioneWarning());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[TIPO_SEGNALAZIONE_WARNING] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getTipoSegnalazioneWarning()+"\n");
      }
      // Se l'utente ha specificato la tipologia di anomalia OK
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneOk())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getTipoSegnalazioneOk());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[TIPO_SEGNALAZIONE_OK] in searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getTipoSegnalazioneOk()+"\n");          
      }

      SolmrLogger.debug(this, "Executing searchUnitaArboreaDichiarataByParameters: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
        ParticellaVO particellaVO = new ParticellaVO();
        UnitaArboreaDichiarataVO unitaArboreaDichiarataVO = new UnitaArboreaDichiarataVO();
        ParticellaCertificataVO particellaCertificataVO = new ParticellaCertificataVO();
        storicoParticellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
        storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
        ComuneVO comuneVO = new ComuneVO();
        comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
        comuneVO.setDescom(rs.getString("DESCOM"));
        storicoParticellaVO.setComuneParticellaVO(comuneVO);
        storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
        storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
        storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
        storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
        storicoParticellaVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
        storicoParticellaVO.setSuperficieGrafica(rs.getString("SUPERFICIE_GRAFICA"));
        particellaVO.setFlagSchedario(rs.getString("FLAG_SCHEDARIO"));
        storicoParticellaVO.setParticellaVO(particellaVO);
        unitaArboreaDichiarataVO.setIdUnitaArboreaDichiarata(new Long(rs.getLong("ID_UNITA_ARBOREA_DICHIARATA")));
        unitaArboreaDichiarataVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
        unitaArboreaDichiarataVO.setCodiceFotografiaTerreni(rs.getString("CODICE_FOTOGRAFIA_TERRENI"));
        unitaArboreaDichiarataVO.setProgrUnar(rs.getString("PROGR_UNAR"));
        unitaArboreaDichiarataVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        unitaArboreaDichiarataVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        unitaArboreaDichiarataVO.setDataLavorazione(rs.getDate("DATA_LAVORAZIONE"));
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_UNAR"))) 
        {
          unitaArboreaDichiarataVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
          TipoTipologiaUnitaArboreaVO tipoTipologiaUnitaArboreaVO = new TipoTipologiaUnitaArboreaVO();
          tipoTipologiaUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
          tipoTipologiaUnitaArboreaVO.setDescrizione(rs.getString("DESC_TIPO_UNAR"));
          unitaArboreaDichiarataVO.setTipoTipologiaUnitaArboreaVO(tipoTipologiaUnitaArboreaVO);
        }
        unitaArboreaDichiarataVO.setArea(rs.getString("AREA"));
        unitaArboreaDichiarataVO.setSestoSuFila(rs.getString("SESTO_SU_FILA"));
        unitaArboreaDichiarataVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
        unitaArboreaDichiarataVO.setNumCeppi(rs.getString("NUM_CEPPI"));
        unitaArboreaDichiarataVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
        unitaArboreaDichiarataVO.setDataImpianto(rs.getDate("DATA_IMPIANTO"));
        unitaArboreaDichiarataVO.setAnnoReinnesto(rs.getString("ANNO_REINNESTO"));
        if(Validator.isNotEmpty(rs.getString("ID_FORMA_ALLEVAMENTO"))) 
        {
          unitaArboreaDichiarataVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
          TipoFormaAllevamentoVO tipoFormaAllevamentoVO = new TipoFormaAllevamentoVO();
          tipoFormaAllevamentoVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
          tipoFormaAllevamentoVO.setDescrizione(rs.getString("DESC_FORMA_ALLEVAMENTO"));
          unitaArboreaDichiarataVO.setTipoFormaAllevamentoVO(tipoFormaAllevamentoVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_IRRIGAZIONE_UNAR"))) {
          unitaArboreaDichiarataVO.setIdIrrigazioneUnar(new Long(rs.getLong("ID_IRRIGAZIONE_UNAR")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_COLTIVAZIONE_UNAR"))) {
          unitaArboreaDichiarataVO.setIdColtivazioneUnar(new Long(rs.getLong("ID_COLTIVAZIONE_UNAR")));
        }
        unitaArboreaDichiarataVO.setCodiceTipoVarieta(rs.getString("CODICE_TIPO_VARIETA"));
        unitaArboreaDichiarataVO.setPresenzaAltriVitigni(rs.getString("PRESENZA_ALTRI_VITIGNI"));
        unitaArboreaDichiarataVO.setNumeroPianteProduttivo(rs.getString("NUMERO_PIANTE_PRODUTTIVO"));
        unitaArboreaDichiarataVO.setNumeroAltrePiante(rs.getString("NUMERO_ALTRE_PIANTE"));
        unitaArboreaDichiarataVO.setCampagna(rs.getString("CAMPAGNA"));
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VIGNETO"))) {
          unitaArboreaDichiarataVO.setIdTipologiaVigneto(new Long(rs.getLong("ID_TIPOLOGIA_VIGNETO")));
        }
        unitaArboreaDichiarataVO.setTipoImpianto(rs.getString("TIPO_IMPIANTO"));
        unitaArboreaDichiarataVO.setNumeroCastagni(rs.getString("NUMERO_CASTAGNI"));
        unitaArboreaDichiarataVO.setGruppo(rs.getString("GRUPPO"));
        unitaArboreaDichiarataVO.setRicaduta(rs.getString("RICADUTA"));
        if(Validator.isNotEmpty(rs.getString("ID_GIACITURA_UNAR"))) {
          unitaArboreaDichiarataVO.setIdGiacituraUnar(new Long(rs.getLong("ID_GIACITURA_UNAR")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_ROCCIA_UNAR"))) {
          unitaArboreaDichiarataVO.setIdRocciaUnar(new Long(rs.getLong("ID_ROCCIA_UNAR")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_SCHELETRO_UNAR"))) {
          unitaArboreaDichiarataVO.setIdScheletroUnar(new Long(rs.getLong("ID_SCHELETRO_UNAR")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_STATO_VEGETATIVO_UNAR"))) {
          unitaArboreaDichiarataVO.setIdStatoVegetativoUnar(new Long(rs.getLong("ID_STATO_VEGETATIVO_UNAR")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_POTATURA_UNAR"))) {
          unitaArboreaDichiarataVO.setIdPotaturaUnar(new Long(rs.getLong("ID_POTATURA_UNAR")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_GIUDIZIO_UNAR"))) {
          unitaArboreaDichiarataVO.setIdGiudizioUnar(new Long(rs.getLong("ID_GIUDIZIO_UNAR")));
        }
        unitaArboreaDichiarataVO.setSupplementari(rs.getString("SUPPLEMENTARI"));
        unitaArboreaDichiarataVO.setMeccanizzabile(rs.getString("MECCANIZZABILE"));
        unitaArboreaDichiarataVO.setDimensioneChioma(rs.getString("DIMENSIONE_CHIOMA"));
        if(Validator.isNotEmpty(rs.getString("ID_ETA_IMPIANTO_UNAR"))) {
          unitaArboreaDichiarataVO.setIdEtaImpiantoUnar(new Long(rs.getLong("ID_ETA_IMPIANTO_UNAR")));
        }
        unitaArboreaDichiarataVO.setProvinciaCCIAA(rs.getString("PROVINCIA_CCIAA"));
        unitaArboreaDichiarataVO.setMatricolaCCIAA(rs.getString("MATRICOLA_CCIAA"));
        unitaArboreaDichiarataVO.setConfermaPrecIscrizioneAlbo(rs.getString("CONFERMA_PREC_ISCRIZIONE_ALBO"));
        unitaArboreaDichiarataVO.setRichiestaNuovaIscrAlbo(rs.getString("RICHIESTA_NUOVA_ISCR_ALBO"));
        unitaArboreaDichiarataVO.setConfermaRichNuovaIscrAlbo(rs.getString("CONFERMA_RICH_NUOVA_ISCR_ALBO"));
        unitaArboreaDichiarataVO.setSuperficieDaIscrivereAlbo(rs.getString("SUPERFICIE_DA_ISCRIVERE_ALBO"));
        unitaArboreaDichiarataVO.setAnnoIscrizioneAlbo(rs.getString("ANNO_ISCRIZIONE_ALBO"));
        unitaArboreaDichiarataVO.setIdFonte(new Long(rs.getLong("ID_FONTE")));
        unitaArboreaDichiarataVO.setNote(rs.getString("NOTE"));
        unitaArboreaDichiarataVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
        unitaArboreaDichiarataVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) {
          unitaArboreaDichiarataVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
          tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          tipoVarietaVO.setDescrizione(rs.getString("DESC_VARIETA"));
          tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VAR"));
          unitaArboreaDichiarataVO.setTipoVarietaVO(tipoVarietaVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO"))) {
          unitaArboreaDichiarataVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
          TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
          tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
          tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
          tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTILIZZO"));
          unitaArboreaDichiarataVO.setTipoUtilizzoVO(tipoUtilizzoVO);
        }
        unitaArboreaDichiarataVO.setPercentualeVarieta(rs.getString("PERCENTUALE_VARIETA"));
        if(rs.getDate("DATA_ESECUZIONE") != null) {
          unitaArboreaDichiarataVO.setDataEsecuzione(new Timestamp(rs.getDate("DATA_ESECUZIONE").getTime()));
        }
        unitaArboreaDichiarataVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
        unitaArboreaDichiarataVO.setStatoUnitaArborea(rs.getString("STATO_UNITA_ARBOREA"));
        unitaArboreaDichiarataVO.setAnnoRiferimento(rs.getString("ANNO_RIFERIMENTO"));
        unitaArboreaDichiarataVO.setTolleranza(checkInt(rs.getString("TOLLERANZA")));
        
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
          vPartCertEleg.add(partCertElegVO);
        }
        particellaCertificataVO.setVParticellaCertEleg(vPartCertEleg);
        particellaCertificataVO.setSupGrafica(rs.getString("SUP_GRAFICA"));
        particellaCertificataVO.setSupUsoGrafica(rs.getString("SUP_USO_GRAFICA"));
        //Nuova Eleggibilta Fine ***********
        
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VINO"))) {
          unitaArboreaDichiarataVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
          TipoTipologiaVinoVO tipoTipologiaVinoVO = new TipoTipologiaVinoVO();
          tipoTipologiaVinoVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
          tipoTipologiaVinoVO.setDescrizione(rs.getString("DESC_TIPO_VINO"));
          tipoTipologiaVinoVO.setFlagGestioneVigna(rs.getString("FLAG_GESTIONE_VIGNA"));
          unitaArboreaDichiarataVO.setTipoTipologiaVinoVO(tipoTipologiaVinoVO);
        }
        
        if(Validator.isNotEmpty(rs.getString("ID_CAUSALE_MODIFICA"))) {
          unitaArboreaDichiarataVO.setIdCausaleModifica(new Long(rs.getLong("ID_CAUSALE_MODIFICA")));
          TipoCausaleModificaVO tipoCausaleModificaVO = new TipoCausaleModificaVO();
          tipoCausaleModificaVO.setIdCausaleModifica(new Long(rs.getLong("ID_CAUSALE_MODIFICA")));
          tipoCausaleModificaVO.setDescrizione(rs.getString("DESC_CAUS_MOD"));
          unitaArboreaDichiarataVO.setTipoCausaleModificaVO(tipoCausaleModificaVO);
        }
        
        
        unitaArboreaDichiarataVO.setFlagImproduttiva(rs.getString("FLAG_IMPRODUTTIVA"));
        unitaArboreaDichiarataVO.setPercentualeFallanza(rs.getBigDecimal("PERCENTUALE_FALLANZA"));
        unitaArboreaDichiarataVO.setInNotifica(rs.getString("IN_NOTIFICA"));
        
        
        storicoParticellaVO.setUnitaArboreaDichiarataVO(unitaArboreaDichiarataVO);
        
        
        //*****************Controlli P
        storicoParticellaVO.setGisP30(rs.getString("P30"));
        storicoParticellaVO.setGisP25(rs.getString("P25"));
        storicoParticellaVO.setGisP26(rs.getString("P26"));
        storicoParticellaVO.setSospesaGis(rs.getString("SOSPESA_GIS"));
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
        
        storicoParticellaVO.setParticellaCertificataVO(particellaCertificataVO);
        
        
        
        ConduzioneDichiarataVO conduzioneDichiarataVO = new ConduzioneDichiarataVO();
        conduzioneDichiarataVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
        ConduzioneDichiarataVO[] elencoConduzioniPart = new ConduzioneDichiarataVO[1];
        elencoConduzioniPart[0] = conduzioneDichiarataVO;
        storicoParticellaVO.setElencoConduzioniDichiarate(elencoConduzioniPart);
        
        storicoParticellaVO.setPercentualeUtilizzoEleg(rs.getBigDecimal("PERCENTUALE_UTILIZZO"));
        
        
        FoglioVO foglioVO = new FoglioVO();
        foglioVO.setFlagStabilizzazione(checkIntegerNull(rs.getString("FLAG_STABILIZZAZIONE")));
        storicoParticellaVO.setFoglioVO(foglioVO);
        
        
        elencoParticelleArboreeDichiarate.add(storicoParticellaVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "searchUnitaArboreaDichiarataByParameters in UnitaArboreaDichiarataDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "searchUnitaArboreaDichiarataByParameters in UnitaArboreaDichiarataDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "searchUnitaArboreaDichiarataByParameters in UnitaArboreaDichiarataDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "searchUnitaArboreaDichiarataByParameters in UnitaArboreaDichiarataDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO\n");
    if(elencoParticelleArboreeDichiarate.size() == 0) {
      return (StoricoParticellaVO[])elencoParticelleArboreeDichiarate.toArray(new StoricoParticellaVO[0]);
    }
    else {
      return (StoricoParticellaVO[])elencoParticelleArboreeDichiarate.toArray(new StoricoParticellaVO[elencoParticelleArboreeDichiarate.size()]);
    }
  }
  
  
  public Vector<StoricoParticellaVO> getElencoStoricoUnitaArboreaDichiarataBasic(Long idAzienda, 
      FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getElencoStoricoUnitaArboreaDichiarataBasic method in UnitaArboreaDichiarataDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelleArboreeDichiarate = new Vector<StoricoParticellaVO>();

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getElencoStoricoUnitaArboreaDichiarataBasic method in UnitaArboreaDichiarataDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getElencoStoricoUnitaArboreaDichiarataBasic method in UnitaArboreaDichiarataDAO and it values: "+conn+"\n");

      String query = 
        " SELECT SP.ID_STORICO_PARTICELLA, " +
        "        SP.COMUNE, " +
        "        C.DESCOM, " +
        "        P.SIGLA_PROVINCIA, " +
        "        SP.SEZIONE, " +
        "        SP.FOGLIO, " +
        "        SP.PARTICELLA, " +
        "        SP.SUBALTERNO, " +
        "        SP.ID_PARTICELLA, " +
        "        UAD.ID_UNITA_ARBOREA_DICHIARATA, " +
        "        UAD.ID_STORICO_UNITA_ARBOREA, " +
        "        UAD.CODICE_FOTOGRAFIA_TERRENI, " +
        "        UAD.PROGR_UNAR, " +
        "        UAD.ID_TIPOLOGIA_UNAR, " +
        "        TTU.DESCRIZIONE AS DESC_TIPO_UNAR, " +
        "        UAD.AREA, " +
        "        UAD.SESTO_SU_FILA, " +
        "        UAD.SESTO_TRA_FILE, " +
        "        UAD.NUM_CEPPI, " +
        "        UAD.ANNO_IMPIANTO, " +
        "        UAD.DATA_IMPIANTO, " +
        "        UAD.ANNO_ISCRIZIONE_ALBO, " +
        "        UAD.ID_VARIETA, " +
        "        TVAR.DESCRIZIONE AS DESC_VARIETA, " +
        "        TVAR.CODICE_VARIETA AS COD_VAR, " +
        "        UAD.ID_UTILIZZO, " +
        "        TU.CODICE, " +
        "        TU.DESCRIZIONE AS DESC_TIPO_UTILIZZO, " +
        "        UAD.ID_TIPOLOGIA_VINO, " +
        "        TTV.DESCRIZIONE AS DESC_TIPO_VINO " +
        " FROM   DB_STORICO_PARTICELLA SP, " +
        "        COMUNE C, " +
        "        PROVINCIA P, " +
        "        DB_UNITA_ARBOREA_DICHIARATA UAD, " +
        "        DB_TIPO_TIPOLOGIA_UNAR TTU, " +
        "        DB_TIPO_VARIETA TVAR, " +
        "        DB_TIPO_UTILIZZO TU, " +
        "        DB_UTE U, " +
        "        DB_CONDUZIONE_DICHIARATA CD, " +
        "        DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "        DB_TIPO_TIPOLOGIA_VINO TTV " +
        " WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        " AND    DC.CODICE_FOTOGRAFIA_TERRENI = UAD.CODICE_FOTOGRAFIA_TERRENI " +
        " AND    UAD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +  
        " AND    SP.COMUNE = C.ISTAT_COMUNE " +  
        " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
        " AND    UAD.ID_TIPOLOGIA_UNAR = TTU.ID_TIPOLOGIA_UNAR(+) " +  
        " AND    UAD.ID_VARIETA = TVAR.ID_VARIETA(+) " +  
        " AND    UAD.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
        " AND    UAD.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO(+) " +
        " AND    UAD.ID_AZIENDA = ? " +  
        " AND    UAD.ID_AZIENDA = U.ID_AZIENDA " +
        " AND    TRUNC(U.DATA_INIZIO_ATTIVITA) <= TRUNC(DC.DATA) " +
        " AND    TRUNC(NVL(U.DATA_FINE_ATTIVITA, ?)) > TRUNC(DC.DATA) " +
        " AND    U.ID_UTE = CD.ID_UTE " +  
        " AND    CD.ID_STORICO_PARTICELLA = UAD.ID_STORICO_PARTICELLA " +
        " AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        " AND    CD.ID_TITOLO_POSSESSO NOT IN (5,6) ";
      
      // Se l'utente ha indicato la destinazione produttiva
      if(filtriUnitaArboreaRicercaVO.getIdUtilizzo() != null) {
        query +=   " AND    UAD.ID_UTILIZZO = ? ";
      }
      // Se l'utente ha indicato il vitigno
      if(filtriUnitaArboreaRicercaVO.getIdVarieta() != null) {
        query +=   " AND    UAD.ID_VARIETA = ? ";
      }
      // Se l'utente ha selezionato la tipologia del vino
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdTipologiaVino())&&
            filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue()!=new Long(-1)) 
      {         
        //IdTipologiaVino()==-1 = qualunque tipologia di vino
        //IdTipologiaVino()==0 = senza tipologia di vino          
        if(filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue()==new Long(0))
        {
          //senza tipologia di vino
          query += " AND      UAD.ID_TIPOLOGIA_VINO IS NULL ";
        
        }
        else
        {
          query += " AND     UAD.ID_TIPOLOGIA_VINO = ? ";
        }
      }
      // Se l'utente ha indicato la provincia di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIstatProvincia())) {
        query += " AND P.ISTAT_PROVINCIA = ? ";
      }
      // Se l'utente ha indicato il comune di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIstatComune())) {
        query += " AND SP.COMUNE = ? ";
      }
      // Se l'utente ha indicato la sezione di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSezione())) {
        query += " AND SP.SEZIONE = ? ";
      }
      // Se l'utente ha indicato il foglio di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getFoglio())) {
        query += " AND SP.FOGLIO = ? ";
      }
      // Se l'utente ha indicato la particella di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getParticella())) {
        query += " AND SP.PARTICELLA = ? ";
      }
      // Se l'utente ha indicato il subalterno di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSubalterno())) {
        query += " AND SP.SUBALTERNO = ? ";
      }
      query += 
      " GROUP BY SP.ID_STORICO_PARTICELLA, " +
      "          SP.COMUNE, " +
      "          C.DESCOM, " +
      "          P.SIGLA_PROVINCIA, " +
      "          SP.SEZIONE, " +
      "          SP.FOGLIO, " +
      "          SP.PARTICELLA, " +
      "          SP.SUBALTERNO, " +
      "          SP.ID_PARTICELLA, " +
      "          UAD.ID_UNITA_ARBOREA_DICHIARATA, " +
      "          UAD.ID_STORICO_UNITA_ARBOREA, " +
      "          UAD.CODICE_FOTOGRAFIA_TERRENI, " +
      "          UAD.PROGR_UNAR, " +
      "          UAD.ID_TIPOLOGIA_UNAR, " +
      "          TTU.DESCRIZIONE, " +
      "          UAD.AREA, " +
      "          UAD.SESTO_SU_FILA, " +
      "          UAD.SESTO_TRA_FILE, " +
      "          UAD.NUM_CEPPI, " +
      "          UAD.ANNO_IMPIANTO, " +
      "          UAD.DATA_IMPIANTO, " +
      "          UAD.ANNO_ISCRIZIONE_ALBO, " +
      "          UAD.ID_VARIETA, " +
      "          TVAR.DESCRIZIONE, " +
      "          TVAR.CODICE_VARIETA, " +
      "          UAD.ID_UTILIZZO, " +
      "          TU.CODICE, " +
      "          TU.DESCRIZIONE, " +
      "          UAD.ID_TIPOLOGIA_VINO, " +
      "          TTV.DESCRIZIONE " +
      "ORDER BY  C.DESCOM, " +
      "          SP.SEZIONE, " +
      "          SP.FOGLIO, " +
      "          SP.PARTICELLA, " +
      "          SP.SUBALTERNO, " +
      "          TTU.DESCRIZIONE ASC, " +
      "          PROGR_UNAR ASC ";
      
      

      SolmrLogger.debug(this, "Value of parameter 1 [ID_DICHIARAZIONE_CONSISTENZA] in getElencoStoricoUnitaArboreaDichiarataBasic method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue()+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ID_AZIENDA] in getElencoStoricoUnitaArboreaDichiarataBasic method in UnitaArboreaDichiarataDAO: "+idAzienda+"\n");
      SolmrLogger.debug(this, "Value of parameter 3 [DATA] in getElencoStoricoUnitaArboreaDichiarataBasic method in UnitaArboreaDichiarataDAO: "+new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime())+"\n");

      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      
      stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue());
      stmt.setLong(++indice, idAzienda.longValue());
      stmt.setDate(++indice, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
      // Se l'utente ha indicato la destinazione produttiva
      if(filtriUnitaArboreaRicercaVO.getIdUtilizzo() != null) {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdUtilizzo().longValue());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_UTILIZZO] in getElencoStoricoUnitaArboreaDichiarataBasic method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getIdUtilizzo()+"\n");
      }
      // Se l'utente ha indicato il vitigno
      if(filtriUnitaArboreaRicercaVO.getIdVarieta() != null) {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdVarieta().longValue());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_VARIETA] in getElencoStoricoUnitaArboreaDichiarataBasic method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getIdVarieta()+"\n");
      }
      // Se l'utente ha indicato il tipo vino
      if(filtriUnitaArboreaRicercaVO.getIdTipologiaVino() != null &&
           filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue() != new Long(-1) &&
           filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue() != new Long(0)) 
      {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_TIPOLOGIA_VINO] in getElencoStoricoUnitaArboreaDichiarataBasic method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getIdTipologiaVino()+"\n");
      }      
      // Se l'utente ha indicato il comune di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIstatProvincia())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getIstatProvincia());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ISTAT_PROVINCIA] in getElencoStoricoUnitaArboreaDichiarataBasic method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getIstatProvincia()+"\n");
      }
      // Se l'utente ha indicato il comune di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIstatComune())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getIstatComune());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ISTAT_COMUNE] in getElencoStoricoUnitaArboreaDichiarataBasic method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getIstatComune()+"\n");
      }
      // Se l'utente ha indicato la sezione di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSezione())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getSezione().toUpperCase());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[SEZIONE] in getElencoStoricoUnitaArboreaDichiarataBasic method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getSezione().toUpperCase()+"\n");
      }
      // Se l'utente ha indicato il foglio di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getFoglio())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getFoglio());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[FOGLIO] in getElencoStoricoUnitaArboreaDichiarataBasic method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getFoglio()+"\n");
      }
      // Se l'utente ha indicato la particella di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getParticella())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getParticella());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[PARTICELLA] in getElencoStoricoUnitaArboreaDichiarataBasic method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getParticella()+"\n");
      }
      // Se l'utente ha indicato il subalterno di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSubalterno())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getSubalterno());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[SUBALTERNO] in getElencoStoricoUnitaArboreaDichiarataBasic method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getSubalterno()+"\n");
      }
      

      SolmrLogger.debug(this, "Executing getElencoStoricoUnitaArboreaDichiarataBasic: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
        UnitaArboreaDichiarataVO unitaArboreaDichiarataVO = new UnitaArboreaDichiarataVO();
        storicoParticellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
        storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
        ComuneVO comuneVO = new ComuneVO();
        comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
        comuneVO.setDescom(rs.getString("DESCOM"));
        storicoParticellaVO.setComuneParticellaVO(comuneVO);
        storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
        storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
        storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
        storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
        unitaArboreaDichiarataVO.setIdUnitaArboreaDichiarata(new Long(rs.getLong("ID_UNITA_ARBOREA_DICHIARATA")));
        unitaArboreaDichiarataVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
        unitaArboreaDichiarataVO.setCodiceFotografiaTerreni(rs.getString("CODICE_FOTOGRAFIA_TERRENI"));
        unitaArboreaDichiarataVO.setProgrUnar(rs.getString("PROGR_UNAR"));
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_UNAR"))) 
        {
          unitaArboreaDichiarataVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
          TipoTipologiaUnitaArboreaVO tipoTipologiaUnitaArboreaVO = new TipoTipologiaUnitaArboreaVO();
          tipoTipologiaUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
          tipoTipologiaUnitaArboreaVO.setDescrizione(rs.getString("DESC_TIPO_UNAR"));
          unitaArboreaDichiarataVO.setTipoTipologiaUnitaArboreaVO(tipoTipologiaUnitaArboreaVO);
        }
        unitaArboreaDichiarataVO.setArea(rs.getString("AREA"));
        unitaArboreaDichiarataVO.setSestoSuFila(rs.getString("SESTO_SU_FILA"));
        unitaArboreaDichiarataVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
        unitaArboreaDichiarataVO.setNumCeppi(rs.getString("NUM_CEPPI"));
        unitaArboreaDichiarataVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
        unitaArboreaDichiarataVO.setDataImpianto(rs.getDate("DATA_IMPIANTO"));
        unitaArboreaDichiarataVO.setAnnoIscrizioneAlbo(rs.getString("ANNO_ISCRIZIONE_ALBO"));
        if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) {
          unitaArboreaDichiarataVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
          tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          tipoVarietaVO.setDescrizione(rs.getString("DESC_VARIETA"));
          tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VAR"));
          unitaArboreaDichiarataVO.setTipoVarietaVO(tipoVarietaVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO"))) {
          unitaArboreaDichiarataVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
          TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
          tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
          tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
          tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTILIZZO"));
          unitaArboreaDichiarataVO.setTipoUtilizzoVO(tipoUtilizzoVO);
        }
        
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VINO"))) {
          unitaArboreaDichiarataVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
          TipoTipologiaVinoVO tipoTipologiaVinoVO = new TipoTipologiaVinoVO();
          tipoTipologiaVinoVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
          tipoTipologiaVinoVO.setDescrizione(rs.getString("DESC_TIPO_VINO"));
          unitaArboreaDichiarataVO.setTipoTipologiaVinoVO(tipoTipologiaVinoVO);
        }
              
        
        storicoParticellaVO.setUnitaArboreaDichiarataVO(unitaArboreaDichiarataVO);        
        
        elencoParticelleArboreeDichiarate.add(storicoParticellaVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getElencoStoricoUnitaArboreaDichiarataBasic in UnitaArboreaDichiarataDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getElencoStoricoUnitaArboreaDichiarataBasic in UnitaArboreaDichiarataDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getElencoStoricoUnitaArboreaDichiarataBasic in UnitaArboreaDichiarataDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getElencoStoricoUnitaArboreaDichiarataBasic in UnitaArboreaDichiarataDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO\n");
    
    return elencoParticelleArboreeDichiarate;
    
  }
	
	
	
	/**
	 * Metodo utilizzato per estrarre le informazioni delle UV alla dichiarazione di 
	 * consistenza per lo scarico in excel
	 * @param idAzienda
	 * @param filtriUnitaArboreaRicercaVO
	 * @param orderBy
	 * @return
	 * @throws DataAccessException
	 */
	public Vector<StoricoParticellaArboreaExcelVO> searchUnitaArboreaDichiarataExcelByParameters(
	    Long idAzienda, FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO, 
	    String parametroPTUV, String parametroMTUV) throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating searchUnitaArboreaDichiarataExcelByParameters method in UnitaArboreaDichiarataDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<StoricoParticellaArboreaExcelVO> elencoParticelleArboree = new Vector<StoricoParticellaArboreaExcelVO>();

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in searchUnitaArboreaDichiarataExcelByParameters method in UnitaArboreaDichiarataDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in searchUnitaArboreaDichiarataExcelByParameters method in UnitaArboreaDichiarataDAO and it values: "+conn+"\n");

			String query =
			  "WITH PARCELLE_GIS AS " +
			  "   (SELECT  PC1.ID_CONDUZIONE_DICHIARATA, " +
        "            I.ID_ILO AS ID_ILO, " +   
        "            IP.SUPERFICIE AS SUP_PARCELLA, " +        
        "            IP.TOLLERANZA AS TOLLERANZA_PARCELLA " +
        "    FROM    DB_PARCELLA_CONDUZIONE PC1, " +
        "            DB_ISOLA_PARCELLA IP, " +
        "            DB_ISOLA I, " +
        "            DB_ISOLA_DICHIARATA ID " +
        "    WHERE   ID.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "    AND     ID.DATA_FINE_VALIDITA IS NULL " +
        "    AND     ID.ID_ISOLA_DICHIARATA = I.ID_ISOLA_DICHIARATA " +
        "    AND     I.ID_ISOLA = IP.ID_ISOLA " +
        "    AND     NVL(IP.ID_ELEGGIBILITA_FIT,26) = 26 " +
        "    AND     IP.ID_ISOLA_PARCELLA = PC1.ID_ISOLA_PARCELLA " +
        "  ), " +
        "   START_RIESAME AS " +
        "      (SELECT IR.DATA_RICHIESTA, " +
        "              (CASE " +
        "               WHEN (NVL(IR.DATA_EVASIONE, SYSDATE)) <  DCIR.DATA_INSERIMENTO_DICHIARAZIONE " +
        "               THEN IR.DATA_EVASIONE  " +           
        "               END) AS DATA_EVASIONE, " +               
        "               IR.ID_PARTICELLA " +
        "       FROM   DB_ISTANZA_RIESAME IR, " +
        "              DB_DICHIARAZIONE_CONSISTENZA DCIR " +
        "       WHERE  IR.DATA_RICHIESTA = (  " +                                      
        "                                    SELECT MAX(IRTMP.DATA_RICHIESTA) " +
        "                                    FROM   DB_ISTANZA_RIESAME IRTMP, " +                                    
        "                                           DB_DICHIARAZIONE_CONSISTENZA DCIRTMP " +
        "                                    WHERE  IRTMP.ID_AZIENDA = IR.ID_AZIENDA " +
        "                                    AND    IRTMP.ID_PARTICELLA = IR.ID_PARTICELLA " +
        "                                    AND    DCIRTMP.ID_DICHIARAZIONE_CONSISTENZA = DCIR.ID_DICHIARAZIONE_CONSISTENZA " +
        "                                    AND    IRTMP.DATA_ANNULLAMENTO IS NULL " +
        "                                    AND    IRTMP.DATA_RICHIESTA <   DCIRTMP.DATA_INSERIMENTO_DICHIARAZIONE " +
        "                                    ) " +
        "       AND     DCIR.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "       AND     IR.ID_AZIENDA = ? " +
        "      ), " +
        "   COND_ELEG AS " +
        "              (SELECT ED.ID_PARTICELLA, " +
        "                      ED.PERCENTUALE_UTILIZZO " +
        "               FROM   DB_ELEGGIBILITA_DICHIARATA ED, " +
        "                      DB_DICHIARAZIONE_CONSISTENZA CD " +
        "               WHERE  CD.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "               AND    CD.CODICE_FOTOGRAFIA_TERRENI = ED.CODICE_FOTOGRAFIA_TERRENI " +
        "               AND    ED.ID_ELEGGIBILITA_FIT = 26 ) " +
			  " SELECT SP.ID_STORICO_PARTICELLA, " +
	      "        SP.COMUNE, " +
		    "        C.DESCOM, " +
		    "        P.SIGLA_PROVINCIA, " +
		    "        SP.SEZIONE, " +
		    "        SP.FOGLIO, " +
		    "        SP.PARTICELLA, " +
		    "        SP.SUBALTERNO, " +
		    "        SP.SUP_CATASTALE, " +
		    "        SP.SUPERFICIE_GRAFICA, " +
		    "        SUM(CD.PERCENTUALE_POSSESSO) AS PERCENTUALE_POSSESSO, " +
		    "        PART.FLAG_SCHEDARIO, " +
	      "        UAD.ID_STORICO_UNITA_ARBOREA, " +
	      "        UAD.ID_CATALOGO_MATRICE, " +
		    "        UAD.PROGR_UNAR, " +
		    "        UAD.DATA_INIZIO_VALIDITA, " +
		    "        UAD.DATA_LAVORAZIONE, " +
		    "        UAD.ID_TIPOLOGIA_UNAR, " +
		    "        TTU.DESCRIZIONE AS DESC_TIPO_UNAR, " +
		    "        UAD.AREA, " +
		    "        UAD.DATA_IMPIANTO, " +
		    "        UAD.DATA_PRIMA_PRODUZIONE, " +
		    "        UAD.ANNO_REINNESTO, " +
		    "        UAD.ID_IRRIGAZIONE_UNAR, " +
		    "        UAD.ID_COLTIVAZIONE_UNAR, " +
		    "        UAD.CODICE_TIPO_VARIETA, " +
		    "        UAD.NUMERO_PIANTE_PRODUTTIVO, " +
		    "        UAD.NUMERO_ALTRE_PIANTE, " +
		    "        UAD.CAMPAGNA, " +
		    "        UAD.ID_TIPOLOGIA_VIGNETO, " +
		    "        UAD.TIPO_IMPIANTO, " +
		    "        UAD.NUMERO_CASTAGNI, " +
		    "        UAD.GRUPPO, " +
		    "        UAD.RICADUTA, " +
		    "        UAD.ID_GIACITURA_UNAR, " +
		    "        UAD.ID_ROCCIA_UNAR, " +
		    "        UAD.ID_SCHELETRO_UNAR, " +
		    "        UAD.ID_STATO_VEGETATIVO_UNAR, " +
		    "        UAD.ID_POTATURA_UNAR, " +
		    "        UAD.ID_GIUDIZIO_UNAR, " +
		    "        UAD.SUPPLEMENTARI, " +
		    "        UAD.MECCANIZZABILE, " +
		    "        UAD.DIMENSIONE_CHIOMA, " +
		    "        UAD.ID_ETA_IMPIANTO_UNAR, " +
		    "        UAD.PROVINCIA_CCIAA, " +
		    "        UAD.MATRICOLA_CCIAA, " +
		    "        UAD.CONFERMA_PREC_ISCRIZIONE_ALBO, " +
		    "        UAD.RICHIESTA_NUOVA_ISCR_ALBO, " +
		    "        UAD.CONFERMA_RICH_NUOVA_ISCR_ALBO, " +
		    "        UAD.SUPERFICIE_DA_ISCRIVERE_ALBO, " +
		    "        UAD.ANNO_ISCRIZIONE_ALBO, " +
		    "        UAD.ID_FONTE, " +
		    "        UAD.ID_VARIAZIONE_UNAR, " +
		    "        UAD.NOTE, " +
		    "        UAD.DATA_AGGIORNAMENTO, " +
		    "        UAD.ID_UTENTE_AGGIORNAMENTO, " +
		    "        UAD.ESITO_TOLLERANZA_GIS AS TOLLERANZA, " +
		    "        RCM.ID_VARIETA, " +
		    "        TVAR.DESCRIZIONE AS DESC_VARIETA, " +
		    "        TVAR.CODICE_VARIETA AS COD_VAR, " +
		    "        RCM.ID_UTILIZZO, " +
		    "        TU.CODICE, " +
		    "        TU.DESCRIZIONE AS DESC_TIPO_UTILIZZO, " +
		    "        UAD.DATA_ESECUZIONE, " +
		    "        UAD.ESITO_CONTROLLO, " +
		    "        UAD.ID_TIPOLOGIA_VINO, " +
		    "        TTV.DESCRIZIONE AS DESC_TIPO_VINO, " +
		    "        AA.CUAA, " +
		    "        AA.DENOMINAZIONE, " +
		    "        PC.ID_PARTICELLA_CERTIFICATA, "+
		    "        PC.SUP_GRAFICA, " +
        "        PC.SUP_USO_GRAFICA, " +
        "        DC.DATA_INSERIMENTO_DICHIARAZIONE, " +
        "        SP.ID_PARTICELLA, " +
        "        NVL(PCK_SMRGAA_LIBRERIA.SelTotSupElegByPartEVetCat(PC.ID_PARTICELLA_CERTIFICATA, UAD.ID_CATALOGO_MATRICE),0) AS SUPERFICIE_ELEG, " +
        "        PAG.ID_ILO, " +
        "        PAG.SUP_PARCELLA, " +
        "        PAG.TOLLERANZA_PARCELLA, " +
        "        SRI.DATA_RICHIESTA, " +
        "        SRI.DATA_EVASIONE, " +
        "        NVL(CEL.PERCENTUALE_UTILIZZO, SUM(CD.PERCENTUALE_POSSESSO)) AS PERCENTUALE_UTILIZZO " +
		    " FROM   DB_STORICO_PARTICELLA SP, " +
		    "        DB_STORICO_UNITA_ARBOREA SUA, " +
		    "        COMUNE C, " +
		    "        PROVINCIA P, " +
		    "        DB_PARTICELLA PART, " +
		    "        DB_UNITA_ARBOREA_DICHIARATA UAD, " +
		    "        DB_R_CATALOGO_MATRICE RCM, " +
		    "        DB_TIPO_TIPOLOGIA_UNAR TTU, " +
		    "        DB_TIPO_VARIETA TVAR, " +
		    "        DB_TIPO_UTILIZZO TU, " +
		    "        DB_UTE U, " +
		    "        DB_CONDUZIONE_DICHIARATA CD, " +
		    "        DB_TIPO_TITOLO_POSSESSO TTP, " +
		    "        DB_ANAGRAFICA_AZIENDA AA, " +
		    "        DB_PARTICELLA_CERTIFICATA PC, " +
		    "        DB_DICHIARAZIONE_CONSISTENZA DC, " +
		    "        DB_TIPO_TIPOLOGIA_VINO TTV, " +
		    "        PARCELLE_GIS PAG, " +
		    "        START_RIESAME SRI, " +
		    "        COND_ELEG CEL ";
	    if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdControllo()))
      {
        query += 
        "       ,DB_DICHIARAZIONE_SEGNALAZIONE DDS ";
      }
	    query +=
		    " WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
		    " AND    DC.CODICE_FOTOGRAFIA_TERRENI = UAD.CODICE_FOTOGRAFIA_TERRENI " +
		    " AND    UAD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
		    " AND    UAD.ID_STORICO_UNITA_ARBOREA = SUA.ID_STORICO_UNITA_ARBOREA " +  
		    " AND    SP.ID_PARTICELLA = PART.ID_PARTICELLA " +
		    " AND    SP.COMUNE = C.ISTAT_COMUNE " +
		    " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
	      " AND    UAD.ID_TIPOLOGIA_UNAR = TTU.ID_TIPOLOGIA_UNAR(+) " +
	      " AND    UAD.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE (+) " + 
        " AND    RCM.ID_VARIETA = TVAR.ID_VARIETA(+) " +
        " AND    RCM.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
        " AND    UAD.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO(+) " +
        " AND    UAD.ID_AZIENDA = ? " +  
		    " AND    UAD.ID_AZIENDA = U.ID_AZIENDA " +
		    " AND    TRUNC(U.DATA_INIZIO_ATTIVITA) <= TRUNC(DC.DATA) " +
		    " AND    TRUNC(NVL(U.DATA_FINE_ATTIVITA, ?)) > TRUNC(DC.DATA) " +
		    " AND    AA.ID_AZIENDA = U.ID_AZIENDA " +
		    " AND    AA.DATA_FINE_VALIDITA IS NULL " +
		    " AND    U.ID_UTE = CD.ID_UTE " +  
		    " AND    CD.ID_STORICO_PARTICELLA = UAD.ID_STORICO_PARTICELLA " +
		    " AND    CD.CODICE_FOTOGRAFIA_TERRENI = DC.CODICE_FOTOGRAFIA_TERRENI " +
		    " AND    CD.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO " +
        " AND    SP.COMUNE = PC.COMUNE(+) " +
        " AND    NVL(SP.SEZIONE, '-') = NVL(PC.SEZIONE(+), '-') " +
        " AND    SP.FOGLIO = PC.FOGLIO(+) " +
        " AND    SP.PARTICELLA = PC.PARTICELLA(+) " +
        " AND    NVL(SP.SUBALTERNO, '-') = NVL(PC.SUBALTERNO(+), '-') " +
        " AND    PC.DATA_INIZIO_VALIDITA(+) <= ? " +
        " AND    TRUNC(NVL(PC.DATA_FINE_VALIDITA(+), TO_DATE('31/12/9999', 'DD/MM/YYYY'))) > ? " +
        " AND    CD.ID_CONDUZIONE_DICHIARATA = PAG.ID_CONDUZIONE_DICHIARATA(+) " +
        " AND    SP.ID_PARTICELLA = SRI.ID_PARTICELLA (+) " +
        " AND    SP.ID_PARTICELLA = CEL.ID_PARTICELLA (+) ";
	    // Se l'utente ha indicato il tipo di controllo
	    if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdControllo()))
      {
        query +=
        " AND    UAD.ID_STORICO_UNITA_ARBOREA = DDS.ID_STORICO_UNITA_ARBOREA " +
        " AND    DDS.ID_DICHIARAZIONE_CONSISTENZA = DC.ID_DICHIARAZIONE_CONSISTENZA " +
        " AND    DDS.ID_CONTROLLO = ? ";
      }
			// Se l'utente ha indicato la destinazione produttiva
			if(filtriUnitaArboreaRicercaVO.getIdUtilizzo() != null) {
				query +=   " AND    RCM.ID_UTILIZZO = ? ";
			}
			// Se l'utente ha indicato il vitigno
			if(filtriUnitaArboreaRicercaVO.getIdVarieta() != null) {
				query +=   " AND    RCM.ID_VARIETA = ? ";
			}
			// Se l'utente ha selezionato la tipologia del vino
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdTipologiaVino())&&
    		  filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue()!=new Long(-1)) 
      {
    	  //IdTipologiaVino()==-1 = qualunque tipologia di vino
		    //IdTipologiaVino()==0 = senza tipologia di vino
    	  if(filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue()==new Long(0))
		    {
		      //senza tipologia di vino
			    query += " AND      UAD.ID_TIPOLOGIA_VINO IS NULL ";
		   	}
    	  else
    	  {
    	    query += " AND     UAD.ID_TIPOLOGIA_VINO = ? ";
    	  }
      }
      // Se l'utente ha indicato il genere iscrizione
      if(filtriUnitaArboreaRicercaVO.getIdGenereIscrizione() != null) 
      {
        query +=   " AND    UAD.ID_GENERE_ISCRIZIONE = ? ";
      }
      //Notifiche
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdTipologiaNotifica())
          || (Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdCategoriaNotifica())))
      {
        query +=   " AND  EXISTS  (SELECT NE.ID_NOTIFICA_ENTITA " + 
                   "               FROM   DB_NOTIFICA_ENTITA NE, " +
                   "                      DB_NOTIFICA NO, " +
                   "                      DB_TIPO_ENTITA TE " +
                   "               WHERE  TE.CODICE_TIPO_ENTITA = 'U' " +
                   "               AND    TE.ID_TIPO_ENTITA = NE.ID_TIPO_ENTITA " +
                   "               AND    NE.IDENTIFICATIVO = SUA.ID_UNITA_ARBOREA " +
                   "               AND    NE.ID_NOTIFICA = NO.ID_NOTIFICA " +
                   "               AND    NE.ID_DICHIARAZIONE_CONSISTENZA = DC.ID_DICHIARAZIONE_CONSISTENZA ";
        if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdTipologiaNotifica()))
        {
          query += "               AND    NO.ID_TIPOLOGIA_NOTIFICA = ? ";          
        }
        if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdCategoriaNotifica()))
        {
          query += "               AND    NO.ID_CATEGORIA_NOTIFICA = ? ";          
        }
        if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getFlagNotificheChiuse()))
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
      // Se l'utente ha indicato la causale modifica
      if(filtriUnitaArboreaRicercaVO.getIdCausaleModifica() != null) 
      {
        query +=   " AND    UAD.ID_CAUSALE_MODIFICA = ? ";
      }
      // Se l'utente ha indicato la provincia di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIstatProvincia())) {
        query += " AND P.ISTAT_PROVINCIA = ? ";
      }
      // Se l'utente ha indicato il comune di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIstatComune())) {
				query += " AND SP.COMUNE = ? ";
			}
			// Se l'utente ha indicato la sezione di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSezione())) {
				query += " AND SP.SEZIONE = ? ";
			}
			// Se l'utente ha indicato il foglio di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getFoglio())) {
				query += " AND SP.FOGLIO = ? ";
			}
			// Se l'utente ha indicato la particella di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getParticella())) {
				query += " AND SP.PARTICELLA = ? ";
			}
			// Se l'utente ha indicato il subalterno di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSubalterno())) {
				query += " AND SP.SUBALTERNO = ? ";
			}
			// Se l'utente ha specificato la tipologia di anomalia bloccante
			boolean isFirst = true;
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneBloccante())) {
				query += " AND (UAD.ESITO_CONTROLLO = ? ";
				isFirst = false;
			}
			// Se l'utente ha specificato la tipologia di anomalia warning
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneWarning())) {
				if(!isFirst) {
					query += " OR ";
				}
				else {
					query += " AND (";
				}
				query += " UAD.ESITO_CONTROLLO = ? ";
				isFirst = false;
			}
			// Se l'utente ha specificato la tipologia di anomalia OK
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneOk())) {
				if(!isFirst) {
					query += " OR ";
				}
				else {
					query += " AND (";
				}
				query += " UAD.ESITO_CONTROLLO = ? ";
				isFirst = false;
			}
			if(!isFirst) {
				query += ")";
			}
			query += " GROUP BY SP.ID_STORICO_PARTICELLA, " +
				     "          SP.COMUNE, " +
				     "          C.DESCOM, " +
				     "	        P.SIGLA_PROVINCIA, " +
				     "          SP.SEZIONE, " +
				     "          SP.FOGLIO, " +
				     "          SP.PARTICELLA, " +
				     "          SP.SUBALTERNO, " +
				     "          SP.SUP_CATASTALE, " +
				     "          SP.SUPERFICIE_GRAFICA, " +
				     "          SP.ID_PARTICELLA, " +
				     "          PART.FLAG_SCHEDARIO, " +
				     "          UAD.ID_STORICO_UNITA_ARBOREA, " +
				     "          UAD.ID_CATALOGO_MATRICE, " +
				     "          UAD.PROGR_UNAR, " +
				     "          UAD.DATA_INIZIO_VALIDITA, " +
				     "          UAD.DATA_LAVORAZIONE, " +
				     "          UAD.ID_TIPOLOGIA_UNAR, " +
				     "          TTU.DESCRIZIONE, " +
				     "          UAD.AREA, " +
				     "          UAD.DATA_IMPIANTO, " +
				     "          UAD.DATA_PRIMA_PRODUZIONE, " +
				     "          UAD.ANNO_REINNESTO, " +
				     "          UAD.ID_IRRIGAZIONE_UNAR, " +
				     "          UAD.ID_COLTIVAZIONE_UNAR, " +
				     "          UAD.CODICE_TIPO_VARIETA, " +
				     "          UAD.NUMERO_PIANTE_PRODUTTIVO, " +
				     "          UAD.NUMERO_ALTRE_PIANTE, " +
				     "          UAD.CAMPAGNA, " +
				     "          UAD.ID_TIPOLOGIA_VIGNETO, " +
				     "          UAD.TIPO_IMPIANTO, " +
				     "          UAD.NUMERO_CASTAGNI, " +
				     "          UAD.GRUPPO, " +
				     "          UAD.RICADUTA, " +
				     "          UAD.ID_GIACITURA_UNAR, " +
				     "          UAD.ID_ROCCIA_UNAR, " +
				     "          UAD.ID_SCHELETRO_UNAR, " +
				     "          UAD.ID_STATO_VEGETATIVO_UNAR, " +
				     "          UAD.ID_POTATURA_UNAR, " +
				     "          UAD.ID_GIUDIZIO_UNAR, " +
				     "          UAD.SUPPLEMENTARI, " +
				     "          UAD.MECCANIZZABILE, " +
				     "          UAD.DIMENSIONE_CHIOMA, " +
				     "          UAD.ID_ETA_IMPIANTO_UNAR, " +
				     "          UAD.PROVINCIA_CCIAA, " +
				     "          UAD.MATRICOLA_CCIAA, " +
				     "          UAD.CONFERMA_PREC_ISCRIZIONE_ALBO, " +
				     "          UAD.RICHIESTA_NUOVA_ISCR_ALBO, " +
				     "          UAD.CONFERMA_RICH_NUOVA_ISCR_ALBO, " +
				     "          UAD.SUPERFICIE_DA_ISCRIVERE_ALBO, " +
				     "          UAD.ANNO_ISCRIZIONE_ALBO, " +
				     "          UAD.ID_FONTE, " +
				     "          UAD.ID_VARIAZIONE_UNAR, " +
				     "          UAD.NOTE, " +
				     "          UAD.DATA_AGGIORNAMENTO, " +
				     "          UAD.ID_UTENTE_AGGIORNAMENTO, " +
				     "          UAD.ESITO_TOLLERANZA_GIS, " +
				     "          RCM.ID_VARIETA, " +
				     "          TVAR.DESCRIZIONE, " +
				     "          TVAR.CODICE_VARIETA, " +
				     "          RCM.ID_UTILIZZO, " +
				     "          TU.CODICE, " +
				     "          TU.DESCRIZIONE, " +
				     "          UAD.DATA_ESECUZIONE, " +
				     "          UAD.ESITO_CONTROLLO, " +
				     "          UAD.ID_TIPOLOGIA_VINO, " +
             "          TTV.DESCRIZIONE, " +
				     "          AA.CUAA, " +
				     "          AA.DENOMINAZIONE, " +
				     "          PC.ID_PARTICELLA_CERTIFICATA, "+
				     "          PC.SUP_GRAFICA, " +
             "          PC.SUP_USO_GRAFICA, " +
             "          DC.DATA_INSERIMENTO_DICHIARAZIONE, " +
             "          SP.ID_PARTICELLA, " +
             "          PAG.ID_ILO, " +
             "          PAG.SUP_PARCELLA, " +    
             "          PAG.TOLLERANZA_PARCELLA, " +
             "          SRI.DATA_RICHIESTA, " +
             "          SRI.DATA_EVASIONE, " +
             "          CEL.PERCENTUALE_UTILIZZO " +
        		 "ORDER BY C.DESCOM, " +
             "         SP.SEZIONE, " +
             "         SP.FOGLIO, " +
             "         SP.PARTICELLA, " +
             "         SP.SUBALTERNO, " +
             "         TTU.DESCRIZIONE ASC, " +
             "         PROGR_UNAR ASC";
		
			SolmrLogger.debug(this, "Value of parameter 1 [ID_DICHIARAZIONE_CONSISTENZA] in searchUnitaArboreaDichiarataExcelByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue()+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ID_AZIENDA] in searchUnitaArboreaDichiarataExcelByParameters method in UnitaArboreaDichiarataDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [DATA] in searchUnitaArboreaDichiarataExcelByParameters method in UnitaArboreaDichiarataDAO: "+new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime())+"\n");

			stmt = conn.prepareStatement(query);
			
			int indice = 0;
			
			//With AS
			stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue());
			//Istanza di riesame 
			stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue());
			stmt.setLong(++indice, idAzienda.longValue());
			//CONDUZIONE_ELEGGIBILE
			stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue());
			
			
			stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue());
			stmt.setLong(++indice, idAzienda.longValue());
			stmt.setDate(++indice, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
			stmt.setDate(++indice, new java.sql.Date(filtriUnitaArboreaRicercaVO.getDataInserimentoDichiarazione().getTime()));
			stmt.setDate(++indice, new java.sql.Date(filtriUnitaArboreaRicercaVO.getDataInserimentoDichiarazione().getTime()));
			
			
		  //Se l'utente ha selezionato il tipo di controllo
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdControllo()))
      {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdControllo().longValue());
      }
			
			// Se l'utente ha indicato la destinazione produttiva
			if(filtriUnitaArboreaRicercaVO.getIdUtilizzo() != null) {
				stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdUtilizzo().longValue());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_UTILIZZO] in searchUnitaArboreaDichiarataExcelByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getIdUtilizzo()+"\n");
			}
			// Se l'utente ha indicato il vitigno
			if(filtriUnitaArboreaRicercaVO.getIdVarieta() != null) {
				stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdVarieta().longValue());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_VARIETA] in searchUnitaArboreaDichiarataExcelByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getIdVarieta()+"\n");
			}
			// Se l'utente ha indicato il tipo vino
			if(filtriUnitaArboreaRicercaVO.getIdTipologiaVino() != null &&
    	    	 filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue() != new Long(-1) &&
    	    	 filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue() != new Long(0)) 
			{
    	  stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue());
    	  SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_TIPOLOGIA_VINO] in searchUnitaArboreaDichiarataExcelByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getIdTipologiaVino()+"\n");
    	}
		  // Se l'utente ha indicato il genere iscrizione
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdGenereIscrizione())) {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdGenereIscrizione().longValue());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_GENERE_ISCRIZIONE] in searchUnitaArboreaDichiarataExcelByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getIdGenereIscrizione()+"\n");
      }
      // Se l'utente ha indicato la tipologia notifica
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdTipologiaNotifica())) {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdTipologiaNotifica().longValue());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_TIPOLOGIA_NOTIFICA] in searchStoricoUnitaArboreaByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIdTipologiaNotifica()+"\n");
      }
      // Se l'utente ha indicato la categoria notifica
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdCategoriaNotifica())) {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdCategoriaNotifica().longValue());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_CATEGORIA_NOTIFICA] in searchStoricoUnitaArboreaByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIdCategoriaNotifica()+"\n");
      }
      // Se l'utente ha indicato la causale modifica
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdCausaleModifica())) {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdCausaleModifica().longValue());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_CAUSALE_MODIFICA] in searchUnitaArboreaDichiarataExcelByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getIdCausaleModifica()+"\n");
      }
      // Se l'utente ha indicato la provincia di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIstatProvincia())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getIstatProvincia());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ISTAT_PROVINCIA] in searchUnitaArboreaDichiarataExcelByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIstatProvincia()+"\n");
      }
      // Se l'utente ha indicato il comune di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIstatComune())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getIstatComune());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[ISTAT_COMUNE] in searchUnitaArboreaDichiarataExcelByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getIstatComune()+"\n");
			}
			// Se l'utente ha indicato la sezione di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSezione())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getSezione().toUpperCase());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[SEZIONE] in searchUnitaArboreaDichiarataExcelByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getSezione().toUpperCase()+"\n");
			}
			// Se l'utente ha indicato il foglio di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getFoglio())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getFoglio());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[FOGLIO] in searchUnitaArboreaDichiarataExcelByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getFoglio()+"\n");
			}
			// Se l'utente ha indicato la particella di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getParticella())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getParticella());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[PARTICELLA] in searchUnitaArboreaDichiarataExcelByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getParticella()+"\n");
			}
			// Se l'utente ha indicato il subalterno di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSubalterno())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getSubalterno());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[SUBALTERNO] in searchUnitaArboreaDichiarataExcelByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getSubalterno()+"\n");
			}
			// SEGNALAZIONI:
			// Se l'utente ha specificato la tipologia di anomalia bloccante
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneBloccante())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getTipoSegnalazioneBloccante());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[TIPO_SEGNALAZIONE_BLOCCANTE] in searchUnitaArboreaDichiarataExcelByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getTipoSegnalazioneBloccante()+"\n");
			}
			// Se l'utente ha specificato la tipologia di anomalia warning
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneWarning())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getTipoSegnalazioneWarning());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[TIPO_SEGNALAZIONE_WARNING] in searchUnitaArboreaDichiarataExcelByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getTipoSegnalazioneWarning()+"\n");
			}
			// Se l'utente ha specificato la tipologia di anomalia OK
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneOk())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getTipoSegnalazioneOk());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[TIPO_SEGNALAZIONE_OK] in searchUnitaArboreaDichiarataExcelByParameters method in UnitaArboreaDichiarataDAO: "+filtriUnitaArboreaRicercaVO.getTipoSegnalazioneOk()+"\n");					
			}
			

			SolmrLogger.debug(this, "Executing searchUnitaArboreaDichiarataExcelByParameters: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				StoricoParticellaArboreaExcelVO storicoParticellaArboreaExcelVO = new StoricoParticellaArboreaExcelVO();
				AnagAziendaVO anagAziendaVO = new AnagAziendaVO();
				storicoParticellaArboreaExcelVO.setDescrizioneComuneParticella(rs.getString("DESCOM")+" ("+rs.getString("SIGLA_PROVINCIA")+")");
				storicoParticellaArboreaExcelVO.setSezione(rs.getString("SEZIONE"));
				storicoParticellaArboreaExcelVO.setFoglio(rs.getString("FOGLIO"));
				storicoParticellaArboreaExcelVO.setParticella(rs.getString("PARTICELLA"));
				storicoParticellaArboreaExcelVO.setSubalterno(rs.getString("SUBALTERNO"));
				storicoParticellaArboreaExcelVO.setSuperficieCatastale(StringUtils.parseSuperficieField(rs.getString("SUP_CATASTALE")));
				
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_UNAR"))) {
					storicoParticellaArboreaExcelVO.setDescTipoUnar(rs.getString("DESC_TIPO_UNAR"));
				}
				storicoParticellaArboreaExcelVO.setProgressivo(rs.getString("PROGR_UNAR"));
				storicoParticellaArboreaExcelVO.setDataImpianto(rs.getTimestamp("DATA_IMPIANTO"));
				storicoParticellaArboreaExcelVO.setDataPrimaProduzione(rs.getTimestamp("DATA_PRIMA_PRODUZIONE"));
				
				String area = rs.getString("AREA");
        if(Validator.isNotEmpty(area)) 
        {
          storicoParticellaArboreaExcelVO.setArea(
            StringUtils.parseSuperficieField(area));    
        }
        else
        {
          storicoParticellaArboreaExcelVO.setArea(SolmrConstants.DEFAULT_SUPERFICIE);
        }
				if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO"))) {
					String destinazioneProduttiva = "";
					if(Validator.isNotEmpty(rs.getString("CODICE"))) {
						destinazioneProduttiva += "["+rs.getString("CODICE")+"] ";
					}
					destinazioneProduttiva += rs.getString("DESC_TIPO_UTILIZZO");
					storicoParticellaArboreaExcelVO.setDestinazioneProduttiva(destinazioneProduttiva);
				}
				if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) {
					storicoParticellaArboreaExcelVO.setVarieta("["+rs.getString("COD_VAR")+"] "+rs.getString("DESC_VARIETA"));
				}
				
				storicoParticellaArboreaExcelVO.setMatricolaCCIAA(rs.getString("MATRICOLA_CCIAA"));
        
        
        String supEleggibile = rs.getString("SUPERFICIE_ELEG");
        if(Validator.isNotEmpty(supEleggibile))
        {
          storicoParticellaArboreaExcelVO.setSupEleggibile(
              StringUtils.parseSuperficieField(supEleggibile));         
        }
        else
        {
          storicoParticellaArboreaExcelVO.setSupEleggibile(SolmrConstants.DEFAULT_SUPERFICIE);
        }
        
        String superficieGrafica = rs.getString("SUPERFICIE_GRAFICA");
        if(Validator.isNotEmpty(superficieGrafica))
        {
          storicoParticellaArboreaExcelVO.setSuperficieGrafica(
              StringUtils.parseSuperficieField(superficieGrafica));
        }
        else
        {
          storicoParticellaArboreaExcelVO.setSuperficieGrafica(SolmrConstants.DEFAULT_SUPERFICIE);
        }
        
        storicoParticellaArboreaExcelVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
        
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VINO"))) {
          storicoParticellaArboreaExcelVO.setDescTipologiaVino(rs.getString("DESC_TIPO_VINO"));
        } 
        
        storicoParticellaArboreaExcelVO.setAnnoIscrizioneAlbo(rs.getString("ANNO_ISCRIZIONE_ALBO"));    
				
				
				storicoParticellaArboreaExcelVO.setIdIlo(rs.getString("ID_ILO"));
				
				String supParcella = rs.getString("SUP_PARCELLA");
        if(Validator.isNotEmpty(supParcella))
        {
          storicoParticellaArboreaExcelVO.setSupParcella(
              StringUtils.parseSuperficieField(supParcella));
        }
        
        
        Integer tolleranza = checkInt(rs.getString("TOLLERANZA"));
        String tolleranzaExcel = null;
        if(Validator.isNotEmpty(tolleranza))
        {
          if(tolleranza.compareTo(SolmrConstants.IN_TOLLERANZA) == 0) 
          {     
            tolleranzaExcel = SolmrConstants.UV_TOLLERANZA_OK;
          }
          else if(tolleranza.compareTo(SolmrConstants.FUORI_TOLLERANZA) == 0) 
          {
            tolleranzaExcel = SolmrConstants.UV_TOLLERANZA_FUORI_KO;              
          }
          else if(tolleranza.compareTo(SolmrConstants.UVDOPPIE_TOLLERANZA) == 0) 
          {
            tolleranzaExcel = SolmrConstants.UV_TOLLERANZA_UVDOPPIE_KO;              
          }
          else if(tolleranza.compareTo(SolmrConstants.NO_PARCELLE_TOLLERANZA) == 0) 
          {
            tolleranzaExcel = SolmrConstants.ISO_TOLLERANZA_NO_PARCELLE_KO;              
          }
          else if(tolleranza.compareTo(SolmrConstants.ERR_PL_TOLLERANZA) == 0)
          {
            tolleranzaExcel = SolmrConstants.ISO_TOLLERANZA_PLSQL_KO;              
          }
          else if(tolleranza.compareTo(SolmrConstants.UV_NON_PRESENTE) == 0)
          {
            tolleranzaExcel = SolmrConstants.UV_NON_PRESENTE_KO;              
          }
          else if(tolleranza.compareTo(SolmrConstants.PARTICELLA_ORFANA) == 0)
          {
            tolleranzaExcel = SolmrConstants.UV_TOLLERANZA_PARTICELLA_ORFANA_KO;              
          }
          else if(tolleranza.compareTo(SolmrConstants.UV_PIU_OCCORR_ATTIVE) == 0)
          {
            tolleranzaExcel = SolmrConstants.UV_TOLLERANZA_PIU_OCCORR_ATTIVE_KO;              
          }
          else
          {
            tolleranzaExcel = SolmrConstants.UV_TOLLERANZA_PARCELLE_NO_VITE_KO;              
          }
        }        
        storicoParticellaArboreaExcelVO.setTolleranza(tolleranzaExcel);
        
        
        
        
        if((rs.getString("TOLLERANZA_PARCELLA") != null)
            && Validator.isNotEmpty(supParcella))
        {
        
          BigDecimal paramInf = new BigDecimal(parametroPTUV.replace(',','.'));
          BigDecimal paramSup = new BigDecimal(parametroMTUV.replace(',','.'));
          BigDecimal tolleranzaParcella = new BigDecimal(rs.getString("TOLLERANZA_PARCELLA").replace(',','.'));
          BigDecimal tolleranzaBd = tolleranzaParcella.multiply(paramInf);
          BigDecimal supParcellaBd = new BigDecimal(rs.getString("SUP_PARCELLA").replace(',','.'));
          if(tolleranzaBd.compareTo(paramSup) > 0)
          {
            tolleranzaBd = paramSup;
          }
          BigDecimal limiteInf = supParcellaBd.subtract(tolleranzaBd);
          if(limiteInf.compareTo(new BigDecimal(0)) < 0)
          {
            limiteInf = new BigDecimal(0);
          }
          BigDecimal limiteSup = supParcellaBd.add(tolleranzaBd);
          
          storicoParticellaArboreaExcelVO.setSupParcellaInf(
              StringUtils.parseSuperficieFieldBigDecimal(limiteInf));
          storicoParticellaArboreaExcelVO.setSupParcellaSup(
              StringUtils.parseSuperficieFieldBigDecimal(limiteSup));
          
        }
        
        storicoParticellaArboreaExcelVO.setDataRichiesta(rs.getTimestamp("DATA_RICHIESTA"));
        storicoParticellaArboreaExcelVO.setDataEvasione(rs.getTimestamp("DATA_EVASIONE"));
        
        storicoParticellaArboreaExcelVO.setPercentualeUsoElegg(rs.getBigDecimal("PERCENTUALE_UTILIZZO"));
    
        
        
				
				anagAziendaVO.setCUAA(rs.getString("CUAA"));
				anagAziendaVO.setDenominazione(rs.getString("DENOMINAZIONE"));
				storicoParticellaArboreaExcelVO.setAnagAziendaVO(anagAziendaVO);
				elencoParticelleArboree.add(storicoParticellaArboreaExcelVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "searchUnitaArboreaDichiarataExcelByParameters in UnitaArboreaDichiarataDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "searchUnitaArboreaDichiarataExcelByParameters in UnitaArboreaDichiarataDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "searchUnitaArboreaDichiarataExcelByParameters in UnitaArboreaDichiarataDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "searchUnitaArboreaDichiarataExcelByParameters in UnitaArboreaDichiarataDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated searchUnitaArboreaDichiarataExcelByParameters method in UnitaArboreaDichiarataDAO\n");
		return elencoParticelleArboree;
	}
	
	/**
	 * Metodo che mi consente di recuperare il dettaglio dell'unità arborea dichiarata
	 * 
	 * @param idUnitaArboreaDichiarata
	 * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO
	 * @throws DataAccessException
	 */
	public StoricoParticellaVO findParticellaArboreaDichiarata(Long idUnitaArboreaDichiarata) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating findParticellaArboreaDichiarata method in UnitaArboreaDichiarataDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		StoricoParticellaVO storicoParticellaVO = null;

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in findParticellaArboreaDichiarata method in UnitaArboreaDichiarataDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in findParticellaArboreaDichiarata method in UnitaArboreaDichiarataDAO and it values: "+conn+"\n");

			String query = 
			  " SELECT SP.ID_STORICO_PARTICELLA, " +
        "        SP.ID_PARTICELLA AS ID_PARTICEL, " +
        "        SP.COMUNE, " +
        "        C.DESCOM, " +
        "        P.SIGLA_PROVINCIA, " +
        "        SP.SEZIONE, " +
        "        SP.FOGLIO, " +
        "        SP.PARTICELLA, " +
        "        SP.SUBALTERNO, " +
        "        SP.SUP_CATASTALE, " +
        "        SP.SUPERFICIE_GRAFICA, " +
        "        PART.FLAG_SCHEDARIO, " +
        "        UAD.ID_UNITA_ARBOREA_DICHIARATA, " +
        "        UAD.ID_CATALOGO_MATRICE, " +
        "        UAD.CODICE_FOTOGRAFIA_TERRENI, " +
        "        UAD.ID_STORICO_UNITA_ARBOREA, " +
        "        UAD.PROGR_UNAR, " + 
        "        UAD.DATA_INIZIO_VALIDITA, " + 
        "        UAD.DATA_FINE_VALIDITA, " + 
        "        UAD.DATA_LAVORAZIONE, " + 
        "        UAD.ID_TIPOLOGIA_UNAR, " + 
        "        TTU.DESCRIZIONE AS DESC_TIPO, " +
        "        UAD.AREA, " + 
        "        UAD.SESTO_SU_FILA, " + 
        "        UAD.SESTO_TRA_FILE, " + 
        "        UAD.NUM_CEPPI, " + 
        "        UAD.ANNO_IMPIANTO, " +
        "        UAD.DATA_IMPIANTO, " + 
        "        UAD.ANNO_REINNESTO, " + 
        "        UAD.ID_FORMA_ALLEVAMENTO, " +
        "        TFA.DESCRIZIONE AS DESC_ALLEVAMENTO, " +
        "        UAD.ID_IRRIGAZIONE_UNAR, " +
        "        TIU.DESCRIZIONE AS DESC_IRRIGAZIONE, " +
        "        UAD.ID_COLTIVAZIONE_UNAR, " +
        "        TCU.DESCRIZIONE AS DESC_COLTIVAZIONE, " +
        "        UAD.CODICE_TIPO_VARIETA, " + 
        "        UAD.PRESENZA_ALTRI_VITIGNI, " +  
        "        UAD.NUMERO_PIANTE_PRODUTTIVO, " + 
        "        UAD.NUMERO_ALTRE_PIANTE, " + 
        "        UAD.CAMPAGNA, " + 
        "        UAD.ID_TIPOLOGIA_VIGNETO, " +
        "        TTV.DESCRIZIONE AS DESC_VIGNETO, " + 
        "        UAD.TIPO_IMPIANTO, " + 
        "        UAD.NUMERO_CASTAGNI, " + 
        "        UAD.GRUPPO, " + 
        "        UAD.RICADUTA, " + 
        "        UAD.ID_GIACITURA_UNAR, " +
        "        TGU.DESCRIZIONE AS DESC_GIACITURA, " +
        "        UAD.ID_ROCCIA_UNAR, " + 
        "	       TRU.DESCRIZIONE AS DESC_ROCCIA, " +
        "        UAD.ID_SCHELETRO_UNAR, " + 
        "        TSU.DESCRIZIONE AS DESC_SCHELETRO, " +
        "        UAD.ID_STATO_VEGETATIVO_UNAR, " + 
        "        TSVU.DESCRIZIONE AS DESC_STATO_VEG, " +
        "        UAD.ID_POTATURA_UNAR, " + 
        "        TPU.DESCRIZIONE AS DESC_POTATURA, " +
        "        UAD.ID_GIUDIZIO_UNAR, " + 
        "        TGIU.DESCRIZIONE AS DESC_GIUDIZIO, " +
        " 	     UAD.SUPPLEMENTARI, " + 
        "        UAD.MECCANIZZABILE, " + 
        "        UAD.DIMENSIONE_CHIOMA, " + 
        "        UAD.ID_ETA_IMPIANTO_UNAR, " + 
        "        TEIU.DESCRIZIONE AS DESC_ETA_IMPIANTO, " +
        "        UAD.PROVINCIA_CCIAA, " + 
        "        UAD.MATRICOLA_CCIAA, " + 
        "        UAD.CONFERMA_PREC_ISCRIZIONE_ALBO, " + 
        "        UAD.RICHIESTA_NUOVA_ISCR_ALBO, " + 
        "        UAD.CONFERMA_RICH_NUOVA_ISCR_ALBO, " + 
        "        UAD.SUPERFICIE_DA_ISCRIVERE_ALBO, " + 
        "        UAD.ANNO_ISCRIZIONE_ALBO, " + 
        "        UAD.ID_FONTE, " + 
        "        TF.DESCRIZIONE AS DESC_FONTE, " +
        "        UAD.NOTE, " + 
        "        UAD.DATA_AGGIORNAMENTO, " + 
        "        UAD.ID_UTENTE_AGGIORNAMENTO, " +
        "        RCM.ID_CATALOGO_MATRICE, " +
        "        RCM.ID_UTILIZZO, " + 
        "        TU.CODICE, " +
        "        TU.DESCRIZIONE AS DESC_UTILIZZO, " +
        "        RCM.ID_TIPO_DESTINAZIONE, " +
        "        TDE.CODICE_DESTINAZIONE, " +
        "        TDE.DESCRIZIONE_DESTINAZIONE, " +
        "        RCM.ID_TIPO_DETTAGLIO_USO, " +
        "        TDU.CODICE_DETTAGLIO_USO, " +
        "        TDU.DESCRIZIONE_DETTAGLIO_USO, " +
        "        RCM.ID_TIPO_QUALITA_USO, " +
        "        TQU.CODICE_QUALITA_USO, " +
        "        TQU.DESCRIZIONE_QUALITA_USO, " +
        "        RCM.ID_VARIETA, " + 
        "        TV.CODICE_VARIETA, " +
        "        TV.DESCRIZIONE AS DESC_VARIETA, " +
        "        UAD.ID_VINO, " + 
        "        TVIN.DESCRIZIONE AS DESC_VINO, " +
        "        UAD.PERCENTUALE_VARIETA, " +
        "        UAD.ID_TIPOLOGIA_VINO, " + 
        "        TTVIN.DESCRIZIONE AS DESC_TIPO_VINO, " +
        "        TTVIN.RESA, " +
        "        TTVIN.DENSITA_MIN_CEPPI_HA, " +
        "        TTVIN.CODICE_MIPAF, " +
        "        TTVIN.VINO_DOC, " +
        "        TTVIN.FLAG_GESTIONE_VIGNA, " +
        "        TTVIN.FLAG_GESTIONE_ETICHETTA, " +
        "        UAD.DATA_CESSAZIONE, " + 
        "        UAD.ID_CESSAZIONE_UNAR, " + 
        "        TCEU.DESCRIZIONE AS DESC_CESSAZIONE, " +
        "        UAD.ID_CAUSALE_MODIFICA, " + 
        "        TCM.DESCRIZIONE AS DESC_MODIFICA, " +
        "        UAD.DATA_ESECUZIONE, " + 
        "        UAD.ESITO_CONTROLLO, " +  
        "        UAD.ID_AZIENDA, " +
        "        PC.ID_PARTICELLA_CERTIFICATA, "+
        "        PC.SUP_COLT_ARBOREA_SPECIALIZZATA, " +
        "        PC.SUP_GRAFICA, " +
        "        PC.SUP_USO_GRAFICA, " +
        "        UAD.ANNO_RIFERIMENTO, " +
        "        UAD.COLTURA_SPECIALIZZATA, " +
        "        UAD.STATO_UNITA_ARBOREA, " +
        "        UAD.ANNO_PRIMA_PRODUZIONE, " +
        "        UAD.DATA_PRIMA_PRODUZIONE, " +
        "        UAD.VIGNA, " +
        "        UAD.ID_VIGNA, " +
        "        UAD.ID_MENZIONE_GEOGRAFICA, " +
        "        UAD.ETICHETTA, " +
        "        UAD.ID_GENERE_ISCRIZIONE, " +
        "        UAD.FLAG_IMPRODUTTIVA, " +
        "        UAD.PERCENTUALE_FALLANZA, " +
        "        TGI.DESCRIZIONE AS DESC_GENERE_ISCRIZIONE, " +
        "        TGI.FLAG_DEFINITIVA, " +
        "        DC.DATA_INSERIMENTO_DICHIARAZIONE, " +
        "        NVL(PCK_SMRGAA_LIBRERIA.SelSupElegDichByFotoIdPartVar(UAD.CODICE_FOTOGRAFIA_TERRENI, DC.DATA_INSERIMENTO_DICHIARAZIONE, SP.ID_PARTICELLA, UAD.ID_CATALOGO_MATRICE),0) " +
        "        AS SUPERFICIE_ELEG, " +
        "        UAD.DATA_SOVRAINNESTO, " +
        "        UAD.DATA_INTERVENTO, " +
        "        TIV.DESCRIZIONE AS DESC_INTERVENTO " +
        " FROM   DB_STORICO_PARTICELLA SP, " +
        "        COMUNE C, " +
        "        PROVINCIA P, " +
        "        DB_PARTICELLA PART, " +
        "        DB_UNITA_ARBOREA_DICHIARATA UAD, " +
        "        DB_R_CATALOGO_MATRICE RCM, " +
        "        DB_TIPO_TIPOLOGIA_UNAR TTU, " +
        "        DB_TIPO_FORMA_ALLEVAMENTO TFA, " +
        "        DB_TIPO_IRRIGAZIONE_UNAR TIU, " +
        "        DB_TIPO_COLTIVAZIONE_UNAR TCU, " +
        "        DB_TIPO_TIPOLOGIA_VIGNETO TTV, " +
        "        DB_TIPO_GIACITURA_UNAR TGU, " +
        "        DB_TIPO_ROCCIA_UNAR TRU, " +
        "        DB_TIPO_SCHELETRO_UNAR TSU, " +
        "        DB_TIPO_STATO_VEGETATIVO_UNAR TSVU, " +
        "        DB_TIPO_POTATURA_UNAR TPU, " +
        "        DB_TIPO_GIUDIZIO_UNAR TGIU, " +
        "        DB_TIPO_ETA_IMPIANTO_UNAR TEIU, " +
        "        DB_TIPO_FONTE TF, " +
        "        DB_TIPO_UTILIZZO TU, " +
        "        DB_TIPO_DESTINAZIONE TDE, " +
        "        DB_TIPO_DETTAGLIO_USO TDU, " +
        "        DB_TIPO_QUALITA_USO TQU, " +
        "        DB_TIPO_VARIETA TV, " +
        "        DB_TIPO_VINO TVIN, " +
        "        DB_TIPO_TIPOLOGIA_VINO TTVIN, " +
        "        DB_TIPO_CESSAZIONE_UNAR TCEU, " +
        "        DB_TIPO_CAUSALE_MODIFICA TCM, " +
        "        DB_PARTICELLA_CERTIFICATA PC, " +
        "        DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "        DB_TIPO_GENERE_ISCRIZIONE TGI, " +
        "        DB_TIPO_INTERVENTO_VITICOLO TIV " +
        " WHERE  UAD.ID_UNITA_ARBOREA_DICHIARATA = ? " +
        " AND    UAD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
        " AND    UAD.CODICE_FOTOGRAFIA_TERRENI = DC.CODICE_FOTOGRAFIA_TERRENI " +
        " AND    SP.ID_PARTICELLA = PART.ID_PARTICELLA " +  
        " AND    SP.COMUNE = C.ISTAT_COMUNE " +  
        " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +  
        " AND    UAD.ID_TIPOLOGIA_UNAR = TTU.ID_TIPOLOGIA_UNAR(+) " +
        " AND    UAD.ID_FORMA_ALLEVAMENTO = TFA.ID_FORMA_ALLEVAMENTO(+) " +
        " AND    UAD.ID_IRRIGAZIONE_UNAR = TIU.ID_IRRIGAZIONE_UNAR(+) " +
        " AND    UAD.ID_COLTIVAZIONE_UNAR = TCU.ID_COLTIVAZIONE_UNAR(+) " +
        " AND    UAD.ID_TIPOLOGIA_VIGNETO = TTV.ID_TIPOLOGIA_VIGNETO(+) " +	
        " AND    UAD.ID_GIACITURA_UNAR = TGU.ID_GIACITURA_UNAR(+) " +
        " AND    UAD.ID_ROCCIA_UNAR = TRU.ID_ROCCIA_UNAR(+) " +
        " AND    UAD.ID_SCHELETRO_UNAR = TSU.ID_SCHELETRO_UNAR(+) " +
        " AND    UAD.ID_STATO_VEGETATIVO_UNAR = TSVU.ID_STATO_VEGETATIVO_UNAR(+) " +
        " AND    UAD.ID_POTATURA_UNAR = TPU.ID_POTATURA_UNAR(+) " +
        " AND    UAD.ID_GIUDIZIO_UNAR = TGIU.ID_GIUDIZIO_UNAR(+) " +
        " AND    UAD.ID_ETA_IMPIANTO_UNAR = TEIU.ID_ETA_IMPIANTO_UNAR(+) " +
        " AND    UAD.ID_FONTE = TF.ID_FONTE " +
        " AND    UAD.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE (+) " +
        " AND    UAD.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
        " AND    RCM.ID_TIPO_DESTINAZIONE = TDE.ID_TIPO_DESTINAZIONE(+) " +
        " AND    RCM.ID_TIPO_DETTAGLIO_USO = TDU.ID_TIPO_DETTAGLIO_USO(+) " +
        " AND    RCM.ID_TIPO_QUALITA_USO = TQU.ID_TIPO_QUALITA_USO(+) " +
        " AND    UAD.ID_VARIETA = TV.ID_VARIETA(+) " +
        " AND    UAD.ID_VINO = TVIN.ID_VINO(+) " +
        " AND    UAD.ID_TIPOLOGIA_VINO = TTVIN.ID_TIPOLOGIA_VINO(+) " +
        " AND    UAD.ID_CESSAZIONE_UNAR = TCEU.ID_CESSAZIONE_UNAR(+) " +
        " AND    UAD.ID_CAUSALE_MODIFICA = TCM.ID_CAUSALE_MODIFICA(+) " +    
        " AND    SP.COMUNE = PC.COMUNE(+) " +  
        " AND    NVL(SP.SEZIONE, '-') = NVL(PC.SEZIONE(+), '-') " +  
        " AND    SP.FOGLIO = PC.FOGLIO(+) " +  
        " AND    SP.PARTICELLA = PC.PARTICELLA(+) " +  
        " AND    NVL(SP.SUBALTERNO, '-') = NVL(PC.SUBALTERNO(+), '-') " +
        " AND    TRUNC(PC.DATA_INIZIO_VALIDITA) <= TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
        " AND    TRUNC(NVL(PC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY'))) > TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
        " AND    UAD.ID_GENERE_ISCRIZIONE = TGI.ID_GENERE_ISCRIZIONE(+) " +
        " AND    UAD.ID_TIPO_INTERVENTO_VITICOLO = TIV.ID_TIPO_INTERVENTO_VITICOLO(+) ";

			SolmrLogger.debug(this, "Value of parameter 1 [ID_UNITA_ARBOREA_DICHIARATA] in findParticellaArboreaDichiarata method in UnitaArboreaDichiarataDAO: "+idUnitaArboreaDichiarata+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idUnitaArboreaDichiarata.longValue());

			SolmrLogger.debug(this, "Executing findParticellaArboreaDichiarata: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) 
			{
				storicoParticellaVO = new StoricoParticellaVO();
				UnitaArboreaDichiarataVO unitaArboreaDichiarataVO = new UnitaArboreaDichiarataVO();
				ParticellaCertificataVO particellaCertificataVO = new ParticellaCertificataVO();
				storicoParticellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
				storicoParticellaVO.setIdParticella(new Long(rs.getLong("ID_PARTICEL")));
				storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
	    	ComuneVO comuneVO = new ComuneVO();
	    	comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
	    	comuneVO.setDescom(rs.getString("DESCOM"));
	    	storicoParticellaVO.setComuneParticellaVO(comuneVO);
	    	storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
	    	storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
	    	storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
	    	storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
	    	storicoParticellaVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
	    	storicoParticellaVO.setSuperficieGrafica(rs.getString("SUPERFICIE_GRAFICA"));
		    unitaArboreaDichiarataVO.setIdUnitaArboreaDichiarata(new Long(rs.getLong("ID_UNITA_ARBOREA_DICHIARATA")));
				unitaArboreaDichiarataVO.setCodiceFotografiaTerreni(rs.getString("CODICE_FOTOGRAFIA_TERRENI"));
				unitaArboreaDichiarataVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
				unitaArboreaDichiarataVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
				unitaArboreaDichiarataVO.setProgrUnar(rs.getString("PROGR_UNAR"));
				unitaArboreaDichiarataVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
				unitaArboreaDichiarataVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
				unitaArboreaDichiarataVO.setDataLavorazione(rs.getTimestamp("DATA_LAVORAZIONE"));
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_UNAR"))) 
				{
					unitaArboreaDichiarataVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
					TipoTipologiaUnitaArboreaVO tipoTipologiaUnitaArboreaVO = new TipoTipologiaUnitaArboreaVO();
					tipoTipologiaUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
					tipoTipologiaUnitaArboreaVO.setDescrizione(rs.getString("DESC_TIPO"));
					unitaArboreaDichiarataVO.setTipoTipologiaUnitaArboreaVO(tipoTipologiaUnitaArboreaVO);
				}
				unitaArboreaDichiarataVO.setArea(rs.getString("AREA"));
				unitaArboreaDichiarataVO.setSestoSuFila(rs.getString("SESTO_SU_FILA"));
				unitaArboreaDichiarataVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
				unitaArboreaDichiarataVO.setNumCeppi(rs.getString("NUM_CEPPI"));
				unitaArboreaDichiarataVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
				unitaArboreaDichiarataVO.setDataImpianto(rs.getDate("DATA_IMPIANTO"));
				unitaArboreaDichiarataVO.setAnnoReinnesto(rs.getString("ANNO_REINNESTO"));
				if(Validator.isNotEmpty(rs.getString("ID_FORMA_ALLEVAMENTO"))) 
				{
					unitaArboreaDichiarataVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
					TipoFormaAllevamentoVO tipoFormaAllevamentoVO = new TipoFormaAllevamentoVO();
					tipoFormaAllevamentoVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
					tipoFormaAllevamentoVO.setDescrizione(rs.getString("DESC_ALLEVAMENTO"));
					unitaArboreaDichiarataVO.setTipoFormaAllevamentoVO(tipoFormaAllevamentoVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_IRRIGAZIONE_UNAR"))) 
				{
					unitaArboreaDichiarataVO.setIdIrrigazioneUnar(new Long(rs.getLong("ID_IRRIGAZIONE_UNAR")));
					TipoIrrigazioneUnitaArboreaVO tipoIrrigazioneUnitaArboreaVO = new TipoIrrigazioneUnitaArboreaVO();
					tipoIrrigazioneUnitaArboreaVO.setIdIrrigazioneUnar(new Long(rs.getLong("ID_IRRIGAZIONE_UNAR")));
					tipoIrrigazioneUnitaArboreaVO.setDescrizione(rs.getString("DESC_IRRIGAZIONE"));
					unitaArboreaDichiarataVO.setTipoIrrigazioneUnitaArboreaVO(tipoIrrigazioneUnitaArboreaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_COLTIVAZIONE_UNAR"))) 
				{
					unitaArboreaDichiarataVO.setIdColtivazioneUnar(new Long(rs.getLong("ID_COLTIVAZIONE_UNAR")));
					TipoColtivazioneUnitaArboreaVO tipoColtivazioneUnitaArboreaVO = new TipoColtivazioneUnitaArboreaVO();
					tipoColtivazioneUnitaArboreaVO.setIdColtivazioneUnar(new Long(rs.getLong("ID_COLTIVAZIONE_UNAR")));
					tipoColtivazioneUnitaArboreaVO.setDescrizione(rs.getString("DESC_COLTIVAZIONE"));
					unitaArboreaDichiarataVO.setTipoColtivazioneUnitaArboreaVO(tipoColtivazioneUnitaArboreaVO);
				}
				unitaArboreaDichiarataVO.setCodiceTipoVarieta(rs.getString("CODICE_TIPO_VARIETA"));
				unitaArboreaDichiarataVO.setPresenzaAltriVitigni(rs.getString("PRESENZA_ALTRI_VITIGNI"));
				unitaArboreaDichiarataVO.setNumeroPianteProduttivo(rs.getString("NUMERO_PIANTE_PRODUTTIVO"));
				unitaArboreaDichiarataVO.setNumeroAltrePiante(rs.getString("NUMERO_ALTRE_PIANTE"));
				unitaArboreaDichiarataVO.setCampagna(rs.getString("CAMPAGNA"));
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VIGNETO"))) 
				{
					unitaArboreaDichiarataVO.setIdTipologiaVigneto(new Long(rs.getLong("ID_TIPOLOGIA_VIGNETO")));
					TipoTipologiaVignetoVO tipoTipologiaVignetoVO = new TipoTipologiaVignetoVO();
					tipoTipologiaVignetoVO.setIdTipologiaVigneto(new Long(rs.getLong("ID_TIPOLOGIA_VIGNETO")));
					tipoTipologiaVignetoVO.setDescrizione(rs.getString("DESC_VIGNETO"));
					unitaArboreaDichiarataVO.setTipoTipologiaVignetoVO(tipoTipologiaVignetoVO);
				}
				unitaArboreaDichiarataVO.setTipoImpianto(rs.getString("TIPO_IMPIANTO"));
				unitaArboreaDichiarataVO.setNumeroCastagni(rs.getString("NUMERO_CASTAGNI"));
				unitaArboreaDichiarataVO.setGruppo(rs.getString("GRUPPO"));
				unitaArboreaDichiarataVO.setRicaduta(rs.getString("RICADUTA"));
				if(Validator.isNotEmpty(rs.getString("ID_GIACITURA_UNAR"))) 
				{
					unitaArboreaDichiarataVO.setIdGiacituraUnar(new Long(rs.getLong("ID_GIACITURA_UNAR")));
					TipoGiacituraUnitaArboreaVO tipoGiacituraUnitaArboreaVO = new TipoGiacituraUnitaArboreaVO();
					tipoGiacituraUnitaArboreaVO.setIdGiacituraUnar(new Long(rs.getLong("ID_GIACITURA_UNAR")));
					tipoGiacituraUnitaArboreaVO.setDescrizione(rs.getString("DESC_GIACITURA"));
					unitaArboreaDichiarataVO.setTipoGiacituraUnitaArboreaVO(tipoGiacituraUnitaArboreaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_ROCCIA_UNAR"))) 
				{
					unitaArboreaDichiarataVO.setIdRocciaUnar(new Long(rs.getLong("ID_ROCCIA_UNAR")));
					TipoRocciaUnitaArboreaVO tipoRocciaUnitaArboreaVO = new TipoRocciaUnitaArboreaVO();
					tipoRocciaUnitaArboreaVO.setIdRocciaUnar(new Long(rs.getLong("ID_ROCCIA_UNAR")));
					tipoRocciaUnitaArboreaVO.setDescrizione(rs.getString("DESC_ROCCIA"));
					unitaArboreaDichiarataVO.setTipoRocciaUnitaArboreaVO(tipoRocciaUnitaArboreaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_SCHELETRO_UNAR"))) 
				{
					unitaArboreaDichiarataVO.setIdScheletroUnar(new Long(rs.getLong("ID_SCHELETRO_UNAR")));
					TipoScheletroUnitaArboreaVO tipoScheletroUnitaArboreaVO = new TipoScheletroUnitaArboreaVO();
					tipoScheletroUnitaArboreaVO.setIdScheletroUnar(new Long(rs.getLong("ID_SCHELETRO_UNAR")));
					tipoScheletroUnitaArboreaVO.setDescrizione(rs.getString("DESC_SCHELETRO"));
					unitaArboreaDichiarataVO.setTipoScheletroUnitaArboreaVO(tipoScheletroUnitaArboreaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_STATO_VEGETATIVO_UNAR"))) 
				{
					unitaArboreaDichiarataVO.setIdStatoVegetativoUnar(new Long(rs.getLong("ID_STATO_VEGETATIVO_UNAR")));
					TipoStatoVegetativoUnitaArboreaVO tipoStatoVegetativoUnitaArboreaVO = new TipoStatoVegetativoUnitaArboreaVO();
					tipoStatoVegetativoUnitaArboreaVO.setIdStatoVegetativoUnar(new Long(rs.getLong("ID_STATO_VEGETATIVO_UNAR")));
					tipoStatoVegetativoUnitaArboreaVO.setDescrizione(rs.getString("DESC_STATO_VEG"));
					unitaArboreaDichiarataVO.setTipoStatoVegetativoUnitaArboreaVO(tipoStatoVegetativoUnitaArboreaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_POTATURA_UNAR"))) 
				{
					unitaArboreaDichiarataVO.setIdPotaturaUnar(new Long(rs.getLong("ID_POTATURA_UNAR")));
					TipoPotaturaUnitaArboreaVO tipoPotaturaUnitaArboreaVO = new TipoPotaturaUnitaArboreaVO();
					tipoPotaturaUnitaArboreaVO.setIdPotaturaUnar(new Long(rs.getLong("ID_POTATURA_UNAR")));
					tipoPotaturaUnitaArboreaVO.setDescrizione(rs.getString("DESC_POTATURA"));
					unitaArboreaDichiarataVO.setTipoPotaturaUnitaArboreaVO(tipoPotaturaUnitaArboreaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_GIUDIZIO_UNAR"))) 
				{
					unitaArboreaDichiarataVO.setIdGiudizioUnar(new Long(rs.getLong("ID_GIUDIZIO_UNAR")));
					TipoGiudizioUnitaArboreaVO tipoGiudizioUnitaArboreaVO = new TipoGiudizioUnitaArboreaVO();
					tipoGiudizioUnitaArboreaVO.setIdGiudizioUnar(new Long(rs.getLong("ID_GIUDIZIO_UNAR")));
					tipoGiudizioUnitaArboreaVO.setDescrizione(rs.getString("DESC_GIUDIZIO"));
					unitaArboreaDichiarataVO.setTipoGiudizioUnitaArboreaVO(tipoGiudizioUnitaArboreaVO);
				}
				unitaArboreaDichiarataVO.setSupplementari(rs.getString("SUPPLEMENTARI"));
				unitaArboreaDichiarataVO.setMeccanizzabile(rs.getString("MECCANIZZABILE"));
				unitaArboreaDichiarataVO.setDimensioneChioma(rs.getString("DIMENSIONE_CHIOMA"));
				if(Validator.isNotEmpty(rs.getString("ID_ETA_IMPIANTO_UNAR"))) 
				{
					unitaArboreaDichiarataVO.setIdEtaImpiantoUnar(new Long(rs.getLong("ID_ETA_IMPIANTO_UNAR")));
					TipoEtaImpiantoUnitaArboreaVO tipoEtaImpiantoUnitaArboreaVO = new TipoEtaImpiantoUnitaArboreaVO();
					tipoEtaImpiantoUnitaArboreaVO.setIdEtaImpiantoUnar(new Long(rs.getLong("ID_ETA_IMPIANTO_UNAR")));
					tipoEtaImpiantoUnitaArboreaVO.setDescrizione(rs.getString("DESC_ETA_IMPIANTO"));
					unitaArboreaDichiarataVO.setTipoEtaImpiantoUnitaArboreaVO(tipoEtaImpiantoUnitaArboreaVO);
				}
				unitaArboreaDichiarataVO.setProvinciaCCIAA(rs.getString("PROVINCIA_CCIAA"));
				unitaArboreaDichiarataVO.setMatricolaCCIAA(rs.getString("MATRICOLA_CCIAA"));
				unitaArboreaDichiarataVO.setConfermaPrecIscrizioneAlbo(rs.getString("CONFERMA_PREC_ISCRIZIONE_ALBO"));
				unitaArboreaDichiarataVO.setRichiestaNuovaIscrAlbo(rs.getString("RICHIESTA_NUOVA_ISCR_ALBO"));
				unitaArboreaDichiarataVO.setConfermaRichNuovaIscrAlbo(rs.getString("CONFERMA_RICH_NUOVA_ISCR_ALBO"));
				unitaArboreaDichiarataVO.setSuperficieDaIscrivereAlbo(rs.getString("SUPERFICIE_DA_ISCRIVERE_ALBO"));
				unitaArboreaDichiarataVO.setAnnoIscrizioneAlbo(rs.getString("ANNO_ISCRIZIONE_ALBO"));
				unitaArboreaDichiarataVO.setIdFonte(new Long(rs.getLong("ID_FONTE")));
				CodeDescription fonte = new CodeDescription(Integer.decode(rs.getString("ID_FONTE")), rs.getString("DESC_FONTE"));
				unitaArboreaDichiarataVO.setTipoFonte(fonte);
				unitaArboreaDichiarataVO.setNote(rs.getString("NOTE"));
				unitaArboreaDichiarataVO.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO"));
				unitaArboreaDichiarataVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO"))) 
				{
					unitaArboreaDichiarataVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
					TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
					tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
					tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
					tipoUtilizzoVO.setDescrizione(rs.getString("DESC_UTILIZZO"));
					unitaArboreaDichiarataVO.setTipoUtilizzoVO(tipoUtilizzoVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_TIPO_DESTINAZIONE"))) 
        {
				  unitaArboreaDichiarataVO.setIdTipoDestinazione(new Long(rs.getLong("ID_TIPO_DESTINAZIONE")));
          TipoDestinazioneVO tipoDestinazioneVO = new TipoDestinazioneVO();
          tipoDestinazioneVO.setIdTipoDestinazione(new Long(rs.getLong("ID_TIPO_DESTINAZIONE")));
          tipoDestinazioneVO.setCodiceDestinazione(rs.getString("CODICE_DESTINAZIONE"));
          tipoDestinazioneVO.setDescrizioneDestinazione(rs.getString("DESCRIZIONE_DESTINAZIONE"));
          unitaArboreaDichiarataVO.setTipoDestinazioneVO(tipoDestinazioneVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_TIPO_DETTAGLIO_USO"))) 
        {
          unitaArboreaDichiarataVO.setIdTipoDettaglioUso(new Long(rs.getLong("ID_TIPO_DETTAGLIO_USO")));
          TipoDettaglioUsoVO tipoDettaglioUsoVO = new TipoDettaglioUsoVO();
          tipoDettaglioUsoVO.setIdTipoDettaglioUso(new Long(rs.getLong("ID_TIPO_DETTAGLIO_USO")));
          tipoDettaglioUsoVO.setCodiceDettaglioUso(rs.getString("CODICE_DETTAGLIO_USO"));
          tipoDettaglioUsoVO.setDescrizione(rs.getString("DESCRIZIONE_DETTAGLIO_USO"));
          unitaArboreaDichiarataVO.setTipoDettaglioUsoVO(tipoDettaglioUsoVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_TIPO_QUALITA_USO"))) 
        {
          unitaArboreaDichiarataVO.setIdTipoQualitaUso(new Long(rs.getLong("ID_TIPO_QUALITA_USO")));
          TipoQualitaUsoVO tipoQualitaUsoVO = new TipoQualitaUsoVO();
          tipoQualitaUsoVO.setIdTipoQualitaUso(new Long(rs.getLong("ID_TIPO_QUALITA_USO")));
          tipoQualitaUsoVO.setCodiceQualitaUso(rs.getString("CODICE_QUALITA_USO"));
          tipoQualitaUsoVO.setDescrizioneQualitaUso(rs.getString("DESCRIZIONE_QUALITA_USO"));
          unitaArboreaDichiarataVO.setTipoQualitaUsoVO(tipoQualitaUsoVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) 
        {
          unitaArboreaDichiarataVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          TipoVarietaUnitaArboreaVO tipoVarietaUnitaArboreaVO = new TipoVarietaUnitaArboreaVO();
          tipoVarietaUnitaArboreaVO.setIdVarietaUnar(new Long(rs.getLong("ID_VARIETA")));
          tipoVarietaUnitaArboreaVO.setCodice(rs.getString("CODICE_VARIETA"));
          tipoVarietaUnitaArboreaVO.setDescrizione(rs.getString("DESC_VARIETA"));
          unitaArboreaDichiarataVO.setTipoVarietaUnitaArboreaVO(tipoVarietaUnitaArboreaVO);
        }
				if(Validator.isNotEmpty(rs.getString("ID_VINO"))) 
				{
					unitaArboreaDichiarataVO.setIdVino(new Long(rs.getLong("ID_VINO")));
					TipoVinoVO tipoVinoVO = new TipoVinoVO();
					tipoVinoVO.setIdVino(new Long(rs.getLong("ID_VINO")));
					tipoVinoVO.setDescrizione(rs.getString("DESC_VINO"));
					unitaArboreaDichiarataVO.setTipoVinoVO(tipoVinoVO);
				}
				unitaArboreaDichiarataVO.setPercentualeVarieta(rs.getString("PERCENTUALE_VARIETA"));
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VINO"))) 
				{
					unitaArboreaDichiarataVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
					TipoTipologiaVinoVO tipoTipologiaVinoVO = new TipoTipologiaVinoVO();
					tipoTipologiaVinoVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
					tipoTipologiaVinoVO.setDescrizione(rs.getString("DESC_TIPO_VINO"));
					tipoTipologiaVinoVO.setResa(rs.getBigDecimal("RESA"));
					tipoTipologiaVinoVO.setDensitaMinCeppiHa(checkLongNull(rs.getString("DENSITA_MIN_CEPPI_HA")));					
					tipoTipologiaVinoVO.setCodiceMipaf(rs.getString("CODICE_MIPAF")); 
          tipoTipologiaVinoVO.setVinoDoc(rs.getString("VINO_DOC"));
          tipoTipologiaVinoVO.setFlagGestioneVigna(rs.getString("FLAG_GESTIONE_VIGNA"));
          tipoTipologiaVinoVO.setFlagGestioneEtichetta(rs.getString("FLAG_GESTIONE_ETICHETTA"));					
					unitaArboreaDichiarataVO.setTipoTipologiaVinoVO(tipoTipologiaVinoVO);
				}
				unitaArboreaDichiarataVO.setDataCessazione(rs.getTimestamp("DATA_CESSAZIONE"));
				if(Validator.isNotEmpty(rs.getString("ID_CESSAZIONE_UNAR"))) 
				{
					unitaArboreaDichiarataVO.setIdCessazioneUnar(new Long(rs.getLong("ID_CESSAZIONE_UNAR")));
					TipoCessazioneUnarVO tipoCessazioneUnarVO = new TipoCessazioneUnarVO();
					tipoCessazioneUnarVO.setIdCessazioneUnar(new Long(rs.getLong("ID_CESSAZIONE_UNAR")));
					tipoCessazioneUnarVO.setDescrizione(rs.getString("DESC_CESSAZIONE"));
					unitaArboreaDichiarataVO.setTipoCessazioneUnarVO(tipoCessazioneUnarVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_CAUSALE_MODIFICA"))) 
				{
					unitaArboreaDichiarataVO.setIdCausaleModifica(new Long(rs.getLong("ID_CAUSALE_MODIFICA")));
					TipoCausaleModificaVO tipoCausaleModificaVO = new TipoCausaleModificaVO();
					tipoCausaleModificaVO.setIdCausaleModifica(new Long(rs.getLong("ID_CAUSALE_MODIFICA")));
					tipoCausaleModificaVO.setDescrizione(rs.getString("DESC_MODIFICA"));
					unitaArboreaDichiarataVO.setTipoCausaleModificaVO(tipoCausaleModificaVO);
				}
				unitaArboreaDichiarataVO.setDataEsecuzione(rs.getTimestamp("DATA_ESECUZIONE"));
				unitaArboreaDichiarataVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
				unitaArboreaDichiarataVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
				particellaCertificataVO.setSupColtArboreaSpecializzata(rs.getString("SUP_COLT_ARBOREA_SPECIALIZZATA"));
				//Nuova Eleggibilita Inizio ************
        Vector<ParticellaCertElegVO> vPartCertEleg = null;
        if(rs.getBigDecimal("SUPERFICIE_ELEG") !=null)
        {
          vPartCertEleg = new Vector<ParticellaCertElegVO>();
          ParticellaCertElegVO partCertElegVO = new ParticellaCertElegVO();
          partCertElegVO.setSuperficie(rs.getBigDecimal("SUPERFICIE_ELEG"));
          vPartCertEleg.add(partCertElegVO);
        }
        particellaCertificataVO.setVParticellaCertEleg(vPartCertEleg);
        particellaCertificataVO.setIdParticellaCertificata(checkLongNull(rs.getString("ID_PARTICELLA_CERTIFICATA")));
        particellaCertificataVO.setSupGrafica(rs.getString("SUP_GRAFICA"));
        particellaCertificataVO.setSupUsoGrafica(rs.getString("SUP_USO_GRAFICA"));
        //Nuova Eleggibilta Fine ***********
				storicoParticellaVO.setParticellaCertificataVO(particellaCertificataVO);
				unitaArboreaDichiarataVO.setAnnoRiferimento(rs.getString("ANNO_RIFERIMENTO"));
				unitaArboreaDichiarataVO.setColturaSpecializzata(rs.getString("COLTURA_SPECIALIZZATA"));
				
				unitaArboreaDichiarataVO.setAnnoPrimaProduzione(rs.getString("ANNO_PRIMA_PRODUZIONE"));
				unitaArboreaDichiarataVO.setDataPrimaProduzione(rs.getDate("DATA_PRIMA_PRODUZIONE"));
				unitaArboreaDichiarataVO.setIdVigna(checkLongNull(rs.getString("ID_VIGNA")));
				unitaArboreaDichiarataVO.setIdMenzioneGeografica(checkLongNull(rs.getString("ID_MENZIONE_GEOGRAFICA")));
				unitaArboreaDichiarataVO.setVigna(rs.getString("VIGNA"));
				unitaArboreaDichiarataVO.setEtichetta(rs.getString("ETICHETTA"));
        if(Validator.isNotEmpty(rs.getString("ID_GENERE_ISCRIZIONE"))) 
        {
          TipoGenereIscrizioneVO tipoGenereIscrizioneVO = new TipoGenereIscrizioneVO();
          tipoGenereIscrizioneVO.setIdGenereIscrizione(rs.getLong("ID_GENERE_ISCRIZIONE"));
          tipoGenereIscrizioneVO.setDescrizione(rs.getString("DESC_GENERE_ISCRIZIONE"));
          tipoGenereIscrizioneVO.setFlagDefinitiva(rs.getString("FLAG_DEFINITIVA"));
          unitaArboreaDichiarataVO.setTipoGenereIscrizioneVO(tipoGenereIscrizioneVO);
        }
        
        unitaArboreaDichiarataVO.setFlagImproduttiva(rs.getString("FLAG_IMPRODUTTIVA"));
        unitaArboreaDichiarataVO.setPercentualeFallanza(rs.getBigDecimal("PERCENTUALE_FALLANZA"));
        
        unitaArboreaDichiarataVO.setDataInserimentoDichiarazione(
            rs.getTimestamp("DATA_INSERIMENTO_DICHIARAZIONE"));
        
        
        if(rs.getString("DESC_INTERVENTO") !=null)
        {
          TipoInterventoViticoloVO tipoInterventoViticoloVO = new TipoInterventoViticoloVO();
          tipoInterventoViticoloVO.setDescrizione(rs.getString("DESC_INTERVENTO"));
          unitaArboreaDichiarataVO.setTipoInterventoViticoloVO(tipoInterventoViticoloVO);
        }
        unitaArboreaDichiarataVO.setDataIntervento(rs.getTimestamp("DATA_INTERVENTO"));
        unitaArboreaDichiarataVO.setDataSovrainnesto(rs.getTimestamp("DATA_SOVRAINNESTO"));
				
				
				
				storicoParticellaVO.setUnitaArboreaDichiarataVO(unitaArboreaDichiarataVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "findParticellaArboreaDichiarata in UnitaArboreaDichiarataDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "findParticellaArboreaDichiarata in UnitaArboreaDichiarataDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "findParticellaArboreaDichiarata in UnitaArboreaDichiarataDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "findParticellaArboreaDichiarata in UnitaArboreaDichiarataDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated findParticellaArboreaDichiarata method in UnitaArboreaDichiarataDAO\n");
		return storicoParticellaVO;
	}
	
	
	public StoricoParticellaVO findParticellaArboreaDichiarataBasic(Long idUnitaArboreaDichiarata) throws DataAccessException 
	{
    SolmrLogger.debug(this, "Invocating findParticellaArboreaDichiarataBasic method in UnitaArboreaDichiarataDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    StoricoParticellaVO storicoParticellaVO = null;

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in findParticellaArboreaDichiarataBasic method in UnitaArboreaDichiarataDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in findParticellaArboreaDichiarataBasic method in UnitaArboreaDichiarataDAO and it values: "+conn+"\n");

      String query = 
        " SELECT SP.ID_STORICO_PARTICELLA, " +
        "        SP.ID_PARTICELLA AS ID_PARTICEL, " +
        "        SP.COMUNE, " +
        "        C.DESCOM, " +
        "        P.SIGLA_PROVINCIA, " +
        "        P.ISTAT_PROVINCIA, " +
        "        SP.SEZIONE, " +
        "        SP.FOGLIO, " +
        "        SP.PARTICELLA, " +
        "        SP.SUBALTERNO, " +
        "        UAD.ID_UNITA_ARBOREA_DICHIARATA, " +
        "        UAD.CODICE_FOTOGRAFIA_TERRENI, " +
        "        UAD.ID_STORICO_UNITA_ARBOREA, " +
        "        SUA.ID_UNITA_ARBOREA, " +
        "        UAD.PROGR_UNAR, " + 
        "        UAD.AREA, " + 
        "        UAD.SESTO_SU_FILA, " + 
        "        UAD.SESTO_TRA_FILE, " + 
        "        UAD.NUM_CEPPI, " + 
        "        UAD.ANNO_IMPIANTO, " +
        "        UAD.DATA_IMPIANTO, " + 
        "        UAD.ANNO_ISCRIZIONE_ALBO, " + 
        "        UAD.ID_VARIETA, " + 
        "        TV.CODICE_VARIETA, " +
        "        TV.DESCRIZIONE AS DESC_VARIETA, " +
        "        UAD.ID_UTILIZZO, " + 
        "        TU.CODICE, " +
        "        TU.DESCRIZIONE AS DESC_UTILIZZO, " +
        "        UAD.ID_TIPOLOGIA_VINO, " +
        "        TTV.DESCRIZIONE AS DESC_TIPO_TIPOLOGIA_VINO, " +
        "        UAD.ID_AZIENDA " +
        " FROM   DB_STORICO_PARTICELLA SP, " +
        "        COMUNE C, " +
        "        PROVINCIA P, " +
        "        DB_UNITA_ARBOREA_DICHIARATA UAD, " +
        "        DB_STORICO_UNITA_ARBOREA SUA, " +
        "        DB_TIPO_VARIETA TV, " +
        "        DB_TIPO_UTILIZZO TU, " +
        "        DB_TIPO_TIPOLOGIA_VINO TTV, " +
        "        DB_DICHIARAZIONE_CONSISTENZA DC " +
        " WHERE  UAD.ID_UNITA_ARBOREA_DICHIARATA = ? " +
        " AND    UAD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
        " AND    UAD.ID_STORICO_UNITA_ARBOREA = SUA.ID_STORICO_UNITA_ARBOREA " +
        " AND    UAD.CODICE_FOTOGRAFIA_TERRENI = DC.CODICE_FOTOGRAFIA_TERRENI " +
        " AND    SP.COMUNE = C.ISTAT_COMUNE " +  
        " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +  
        " AND    UAD.ID_VARIETA = TV.ID_VARIETA(+) " +
        " AND    UAD.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
        " AND    UAD.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO(+) ";

      SolmrLogger.debug(this, "Value of parameter 1 [ID_UNITA_ARBOREA_DICHIARATA] in findParticellaArboreaDichiarataBasic method in UnitaArboreaDichiarataDAO: "+idUnitaArboreaDichiarata+"\n");

      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idUnitaArboreaDichiarata.longValue());

      SolmrLogger.debug(this, "Executing findParticellaArboreaDichiarataBasic: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      if(rs.next()) 
      {
        storicoParticellaVO = new StoricoParticellaVO();
        UnitaArboreaDichiarataVO unitaArboreaDichiarataVO = new UnitaArboreaDichiarataVO();
        storicoParticellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
        storicoParticellaVO.setIdParticella(new Long(rs.getLong("ID_PARTICEL")));
        storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
        ComuneVO comuneVO = new ComuneVO();
        comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
        comuneVO.setIstatProvincia(rs.getString("ISTAT_PROVINCIA"));
        comuneVO.setDescom(rs.getString("DESCOM"));
        storicoParticellaVO.setComuneParticellaVO(comuneVO);
        storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
        storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
        storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
        storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
        StoricoUnitaArboreaVO storicoUnitaArboreaVO = new StoricoUnitaArboreaVO();
        storicoUnitaArboreaVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
        storicoUnitaArboreaVO.setIdUnitaArborea(new Long(rs.getLong("ID_UNITA_ARBOREA")));
        storicoParticellaVO.setStoricoUnitaArboreaVO(storicoUnitaArboreaVO);
        unitaArboreaDichiarataVO.setIdUnitaArboreaDichiarata(new Long(rs.getLong("ID_UNITA_ARBOREA_DICHIARATA")));
        unitaArboreaDichiarataVO.setCodiceFotografiaTerreni(rs.getString("CODICE_FOTOGRAFIA_TERRENI"));
        unitaArboreaDichiarataVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
        unitaArboreaDichiarataVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
        unitaArboreaDichiarataVO.setProgrUnar(rs.getString("PROGR_UNAR"));
        
        unitaArboreaDichiarataVO.setArea(rs.getString("AREA"));
        unitaArboreaDichiarataVO.setSestoSuFila(rs.getString("SESTO_SU_FILA"));
        unitaArboreaDichiarataVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
        unitaArboreaDichiarataVO.setNumCeppi(rs.getString("NUM_CEPPI"));
        unitaArboreaDichiarataVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
        unitaArboreaDichiarataVO.setDataImpianto(rs.getDate("DATA_IMPIANTO")); 
        
        unitaArboreaDichiarataVO.setAnnoIscrizioneAlbo(rs.getString("ANNO_ISCRIZIONE_ALBO"));
        if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) {
          unitaArboreaDichiarataVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
          tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          tipoVarietaVO.setDescrizione(rs.getString("DESC_VARIETA"));
          tipoVarietaVO.setCodiceVarieta(rs.getString("CODICE_VARIETA"));
          unitaArboreaDichiarataVO.setTipoVarietaVO(tipoVarietaVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO"))) 
        {
          unitaArboreaDichiarataVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
          TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
          tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
          tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
          tipoUtilizzoVO.setDescrizione(rs.getString("DESC_UTILIZZO"));
          unitaArboreaDichiarataVO.setTipoUtilizzoVO(tipoUtilizzoVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VINO"))) {
          storicoUnitaArboreaVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
          TipoTipologiaVinoVO tipoTipologiaVinoVO = new TipoTipologiaVinoVO();
          tipoTipologiaVinoVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
          tipoTipologiaVinoVO.setDescrizione(rs.getString("DESC_TIPO_TIPOLOGIA_VINO"));
          unitaArboreaDichiarataVO.setTipoTipologiaVinoVO(tipoTipologiaVinoVO);
        }     
        unitaArboreaDichiarataVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA"))); 
        
        storicoParticellaVO.setUnitaArboreaDichiarataVO(unitaArboreaDichiarataVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "findParticellaArboreaDichiarataBasic in UnitaArboreaDichiarataDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "findParticellaArboreaDichiarataBasic in UnitaArboreaDichiarataDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "findParticellaArboreaDichiarataBasic in UnitaArboreaDichiarataDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "findParticellaArboreaDichiarataBasic in UnitaArboreaDichiarataDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated findParticellaArboreaDichiarataBasic method in UnitaArboreaDichiarataDAO\n");
    return storicoParticellaVO;
  }
	
	/**
	 * Metodo che si occupa di validare le unità arboree alla dichiarazione di consistenza
	 * 
	 * @param elencoIdUnitaArboreaDichiarata
	 * @throws SolmrException
	 * @throws DataAccessException
	 */
	public void validaUVPlSql(Long[] elencoIdUnitaArboreaDichiarata) throws SolmrException, DataAccessException {
		SolmrLogger.debug(this, "Invocating validaUVPlSql method in UnitaArboreaDichiarataDAO\n");
		
	    Connection conn = null;
	    CallableStatement cs = null;
	    String command = "{call PACK_AGGIORNA_UV.AGGIORNA_STATO_UV_DICH(?,?,?)}";
	    try {
	    	SolmrLogger.debug(this, "Creating db-connection in validaUVPlSql method in UnitaArboreaDichiarataDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in validaUVPlSql method in UnitaArboreaDichiarataDAO and it values: "+conn+"\n");
			
			SolmrLogger.debug(this, "Value of parameter 1 [ELENCO_ID_UNITA_ARBOREA_DICHIARATA[]] in validaUVPlSql method in UnitaArboreaDichiarataDAO: "+elencoIdUnitaArboreaDichiarata+"\n");
			
			cs = conn.prepareCall(command);
			
			
			/*java.sql.Connection connessione = ((weblogic.jdbc.extensions.WLConnection)conn).getVendorConnection();
			oracle.sql.ArrayDescriptor arrayDescriptor = oracle.sql.ArrayDescriptor.createDescriptor("NUM_VARRAY", connessione);
			oracle.sql.ARRAY array = new ARRAY(arrayDescriptor, connessione, elencoIdUnitaArboreaDichiarata);*/
			
			// JBOSS
			org.jboss.jca.adapters.jdbc.WrappedConnection vendorConnection = (org.jboss.jca.adapters.jdbc.WrappedConnection) conn;
			oracle.sql.ArrayDescriptor arrayDescriptor = oracle.sql.ArrayDescriptor.createDescriptor("NUM_VARRAY", vendorConnection.getUnderlyingConnection());
			oracle.sql.ARRAY array = new oracle.sql.ARRAY(arrayDescriptor, vendorConnection.getUnderlyingConnection(),elencoIdUnitaArboreaDichiarata);
			
			cs.setObject(1, array);
			cs.registerOutParameter(2,Types.VARCHAR);
			cs.registerOutParameter(3,Types.VARCHAR);
			
			cs.executeUpdate();
			String msgErr = cs.getString(2);
			String errorCode = cs.getString(3);

			if(Validator.isNotEmpty(errorCode)) {
				throw new SolmrException((String)AnagErrors.get("ERR_PLSQL")+errorCode + " - " + msgErr);
			}
			cs.close();
			conn.close();
	    }
	    catch(SolmrException se) {
	    	SolmrLogger.error(this, "SolmrException in validaUVPlSql in UnitaArboreaDichiarataDAO: "+se);
	    	throw new SolmrException(se.getMessage());
	    }
	    catch(SQLException exc) {
	    	char a = '"';
	    	String messaggioErrore = StringUtils.replace(exc.getMessage(), "'", "''");
	    	messaggioErrore = StringUtils.replace(exc.getMessage(), System.getProperty("line.separator"), "\\n");
	    	messaggioErrore = StringUtils.replace(exc.getMessage(), String.valueOf(a), " ");
	    	SolmrLogger.error(this, "SQLException in validaUVPlSql in ParticellaDAO: "+messaggioErrore);
	    	throw new SolmrException((String)AnagErrors.get("ERR_PLSQL")+" "+ StringUtils.replace(messaggioErrore, System.getProperty("line.separator"), "\\n"));
	    }
	    catch(Exception ex) {
	    	SolmrLogger.error(this, "Generic Exception in validaUVPlSql in UnitaArboreaDichiarataDAO: "+ex);
	    	throw new DataAccessException(ex.getMessage());
	    }
	    catch(Throwable ex) {
	    	SolmrLogger.error(this, "Throwable in validaUVPlSql in UnitaArboreaDichiarataDAO: "+ex);
	    	throw new DataAccessException(ex.getMessage());
	    }
	    finally {
	    	try {
	    		if(cs != null) {
	    			cs.close();
	    		}
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	}
	    	catch(SQLException exc) {
	    		SolmrLogger.error(this, "SQLException while closing Statement and Connection in validaUVPlSql in UnitaArboreaDichiarataDAO: "+exc);
	    		throw new DataAccessException(exc.getMessage());
	    	}
	    	catch(Exception ex) {
	    		SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in validaUVPlSql in UnitaArboreaDichiarataDAO: "+ex);
	    		throw new DataAccessException(ex.getMessage());
	    	}
	    }
	    SolmrLogger.debug(this, "Invocated validaUVPlSql method in UnitaArboreaDichiarataDAO\n");
	}
	
	/**
	 * Metodo che mi consente di recuperare l'elenco delle unità arboree dichiarate
	 * relative ad una determinata particella di un'azienda al piano di riferimento
	 * dichiarato
	 * 
	 * @param idStoricoParticella
	 * @param idAzienda
	 * @param idPianoRiferimento
	 * 
	 * @return it.csi.solmr.dto.anag.UnitaArboreaDichiarataVO[]
	 * @throws DataAccessException
	 */
	public UnitaArboreaDichiarataVO[] getListUnitaArboreaDichiarataByPianoRiferimentoAndIdStoricoUnitaArborea(Long idStoricoParticella, Long idAzienda, Long idPianoRiferimento, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListUnitaArboreaDichiarataByPianoRiferimentoAndIdStoricoUnitaArborea method in UnitaArboreaDichiarataDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<UnitaArboreaDichiarataVO> elencoUnitaArboreeDichiarate = new Vector<UnitaArboreaDichiarataVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListUnitaArboreaDichiarataByPianoRiferimentoAndIdStoricoUnitaArborea method in UnitaArboreaDichiarataDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListUnitaArboreaDichiarataByPianoRiferimentoAndIdStoricoUnitaArborea method in UnitaArboreaDichiarataDAO and it values: "+conn+"\n");

			String query = " SELECT UAD.ID_UNITA_ARBOREA_DICHIARATA, " +
						   "        UAD.CODICE_FOTOGRAFIA_TERRENI, " +
						   "        UAD.ID_STORICO_UNITA_ARBOREA, " +
						   "        UAD.ID_STORICO_PARTICELLA, " + 
						   "	    UAD.PROGR_UNAR, " + 
						   "        UAD.DATA_INIZIO_VALIDITA, " + 
						   "        UAD.DATA_FINE_VALIDITA, " + 
						   "        UAD.DATA_LAVORAZIONE, " + 
						   "        UAD.ID_TIPOLOGIA_UNAR, " + 
						   "        TTU.DESCRIZIONE AS DESC_TIPO, " +
						   "        UAD.AREA, " + 
						   "        UAD.SESTO_SU_FILA, " + 
						   "        UAD.SESTO_TRA_FILE, " + 
						   "        UAD.NUM_CEPPI, " + 
						   "        UAD.ANNO_IMPIANTO, " + 
						   "        UAD.ANNO_REINNESTO, " + 
						   "        UAD.ID_FORMA_ALLEVAMENTO, " +
						   "        TFA.DESCRIZIONE AS DESC_ALLEVAMENTO, " +
						   "        UAD.ID_IRRIGAZIONE_UNAR, " +
						   "        TIU.DESCRIZIONE AS DESC_IRRIGAZIONE, " +
						   "        UAD.ID_COLTIVAZIONE_UNAR, " +
						   "        TCU.DESCRIZIONE AS DESC_COLTIVAZIONE, " +
						   "        UAD.CODICE_TIPO_VARIETA, " + 
						   "        UAD.PRESENZA_ALTRI_VITIGNI, " +  
						   "        UAD.NUMERO_PIANTE_PRODUTTIVO, " + 
						   "        UAD.NUMERO_ALTRE_PIANTE, " + 
						   "        UAD.CAMPAGNA, " + 
						   "        UAD.ID_TIPOLOGIA_VIGNETO, " +
						   "        TTV.DESCRIZIONE AS DESC_VIGNETO, " + 
						   "        UAD.TIPO_IMPIANTO, " + 
						   "        UAD.NUMERO_CASTAGNI, " + 
						   "        UAD.GRUPPO, " + 
						   "        UAD.RICADUTA, " + 
						   "        UAD.ID_GIACITURA_UNAR, " +
						   "        TGU.DESCRIZIONE AS DESC_GIACITURA, " +
						   "        UAD.ID_ROCCIA_UNAR, " + 
						   "	    TRU.DESCRIZIONE AS DESC_ROCCIA, " +
						   "        UAD.ID_SCHELETRO_UNAR, " + 
						   "        TSU.DESCRIZIONE AS DESC_SCHELETRO, " +
						   "        UAD.ID_STATO_VEGETATIVO_UNAR, " + 
						   "        TSVU.DESCRIZIONE AS DESC_STATO_VEG, " +
						   "        UAD.ID_POTATURA_UNAR, " + 
						   "        TPU.DESCRIZIONE AS DESC_POTATURA, " +
						   "        UAD.ID_GIUDIZIO_UNAR, " + 
						   "        TGIU.DESCRIZIONE AS DESC_GIUDIZIO, " +
						   " 	    UAD.SUPPLEMENTARI, " + 
						   "        UAD.MECCANIZZABILE, " + 
						   "        UAD.DIMENSIONE_CHIOMA, " + 
						   "        UAD.ID_ETA_IMPIANTO_UNAR, " + 
						   "        TEIU.DESCRIZIONE AS DESC_ETA_IMPIANTO, " +
						   "        UAD.PROVINCIA_CCIAA, " + 
						   "        UAD.MATRICOLA_CCIAA, " + 
						   "        UAD.CONFERMA_PREC_ISCRIZIONE_ALBO, " + 
						   "        UAD.RICHIESTA_NUOVA_ISCR_ALBO, " + 
						   "        UAD.CONFERMA_RICH_NUOVA_ISCR_ALBO, " + 
						   "        UAD.SUPERFICIE_DA_ISCRIVERE_ALBO, " + 
						   "        UAD.ANNO_ISCRIZIONE_ALBO, " + 
						   "        UAD.ID_FONTE, " + 
						   "        TF.DESCRIZIONE AS DESC_FONTE, " +
						   "        UAD.NOTE, " + 
						   "        UAD.DATA_AGGIORNAMENTO, " + 
						   "        UAD.ID_UTENTE_AGGIORNAMENTO, " + 
						   "        UAD.ID_VARIETA, " + 
						   "        TV.CODICE_VARIETA, " +
						   "        TV.DESCRIZIONE AS DESC_VARIETA, " +
						   "        UAD.ID_UTILIZZO, " + 
						   "        TU.CODICE, " +
						   "        TU.DESCRIZIONE AS DESC_UTILIZZO, " +
						   "        UAD.ID_VINO, " + 
						   "        TVIN.DESCRIZIONE AS DESC_VINO, " +
						   "        UAD.PERCENTUALE_VARIETA, " +
						   "        UAD.ID_TIPOLOGIA_VINO, " + 
						   "        TTVIN.DESCRIZIONE AS DESC_TIPO_VINO, " +
						   "	    UAD.DATA_CESSAZIONE, " + 
						   "        UAD.ID_CESSAZIONE_UNAR, " + 
						   "        TCEU.DESCRIZIONE AS DESC_CESSAZIONE, " +
						   "        UAD.ID_CAUSALE_MODIFICA, " + 
						   "        TCM.DESCRIZIONE AS DESC_MODIFICA, " +
						   "        UAD.DATA_ESECUZIONE, " + 
						   "        UAD.ESITO_CONTROLLO, " +  
						   "        UAD.ID_AZIENDA " +
						   " FROM   DB_UNITA_ARBOREA_DICHIARATA UAD, " +
						   "        DB_DICHIARAZIONE_CONSISTENZA DC, " +
						   "        DB_TIPO_TIPOLOGIA_UNAR TTU, " +
						   "        DB_TIPO_FORMA_ALLEVAMENTO TFA, " +
						   "        DB_TIPO_IRRIGAZIONE_UNAR TIU, " +
						   "        DB_TIPO_COLTIVAZIONE_UNAR TCU, " +
						   "        DB_TIPO_TIPOLOGIA_VIGNETO TTV, " +
						   "        DB_TIPO_GIACITURA_UNAR TGU, " +
						   "        DB_TIPO_ROCCIA_UNAR TRU, " +
						   "        DB_TIPO_SCHELETRO_UNAR TSU, " +
						   "        DB_TIPO_STATO_VEGETATIVO_UNAR TSVU, " +
						   "        DB_TIPO_POTATURA_UNAR TPU, " +
						   "        DB_TIPO_GIUDIZIO_UNAR TGIU, " +
						   "        DB_TIPO_ETA_IMPIANTO_UNAR TEIU, " +
						   "        DB_TIPO_FONTE TF, " +
						   "        DB_TIPO_VARIETA TV, " +
						   "        DB_TIPO_UTILIZZO TU, " +
						   "        DB_TIPO_VINO TVIN, " +
						   "        DB_TIPO_TIPOLOGIA_VINO TTVIN, " +
						   "        DB_TIPO_CESSAZIONE_UNAR TCEU, " +
						   "        DB_TIPO_CAUSALE_MODIFICA TCM " +
						   " WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " + 
						   " AND    DC.CODICE_FOTOGRAFIA_TERRENI = UAD.CODICE_FOTOGRAFIA_TERRENI " +
						   " AND    UAD.ID_AZIENDA = ? " +
						   " AND    UAD.ID_STORICO_PARTICELLA = ? " +
						   " AND    UAD.ID_TIPOLOGIA_UNAR = TTU.ID_TIPOLOGIA_UNAR(+) " +
						   " AND    UAD.ID_FORMA_ALLEVAMENTO = TFA.ID_FORMA_ALLEVAMENTO(+) " +
						   " AND    UAD.ID_IRRIGAZIONE_UNAR = TIU.ID_IRRIGAZIONE_UNAR(+) " +
						   " AND    UAD.ID_COLTIVAZIONE_UNAR = TCU.ID_COLTIVAZIONE_UNAR(+) " +
						   " AND    UAD.ID_TIPOLOGIA_VIGNETO = TTV.ID_TIPOLOGIA_VIGNETO(+) " +	
						   " AND    UAD.ID_GIACITURA_UNAR = TGU.ID_GIACITURA_UNAR(+) " +
						   " AND    UAD.ID_ROCCIA_UNAR = TRU.ID_ROCCIA_UNAR(+) " +
						   " AND    UAD.ID_SCHELETRO_UNAR = TSU.ID_SCHELETRO_UNAR(+) " +
						   " AND    UAD.ID_STATO_VEGETATIVO_UNAR = TSVU.ID_STATO_VEGETATIVO_UNAR(+) " +
						   " AND    UAD.ID_POTATURA_UNAR = TPU.ID_POTATURA_UNAR(+) " +
						   " AND    UAD.ID_GIUDIZIO_UNAR = TGIU.ID_GIUDIZIO_UNAR(+) " +
						   " AND    UAD.ID_ETA_IMPIANTO_UNAR = TEIU.ID_ETA_IMPIANTO_UNAR(+) " +
						   " AND    UAD.ID_FONTE = TF.ID_FONTE " + 
						   " AND    UAD.ID_VARIETA = TV.ID_VARIETA(+) " +
						   " AND    UAD.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
						   " AND    UAD.ID_VINO = TVIN.ID_VINO(+) " +
						   " AND    UAD.ID_TIPOLOGIA_VINO = TTVIN.ID_TIPOLOGIA_VINO(+) " +
						   " AND    UAD.ID_CESSAZIONE_UNAR = TCEU.ID_CESSAZIONE_UNAR(+) " +
						   " AND    UAD.ID_CAUSALE_MODIFICA = TCM.ID_CAUSALE_MODIFICA(+) "; 
			
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
			
			SolmrLogger.debug(this, "Value of parameter 1 [ID_DICHIARAZIONE_CONSISTENZA] in getListUnitaArboreaDichiarataByPianoRiferimentoAndIdStoricoUnitaArborea method in UnitaArboreaDichiarataDAO: "+idPianoRiferimento+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ID_AZIENDA] in getListUnitaArboreaDichiarataByPianoRiferimentoAndIdStoricoUnitaArborea method in UnitaArboreaDichiarataDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ID_STORICO_PARTICELLA] in getListUnitaArboreaDichiarataByPianoRiferimentoAndIdStoricoUnitaArborea method in UnitaArboreaDichiarataDAO: "+idStoricoParticella+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idPianoRiferimento.longValue());
			stmt.setLong(2, idAzienda.longValue());
			stmt.setLong(3, idStoricoParticella.longValue());
			
			SolmrLogger.debug(this, "Executing getListUnitaArboreaDichiarataByPianoRiferimentoAndIdStoricoUnitaArborea: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				UnitaArboreaDichiarataVO unitaArboreaDichiarataVO = new UnitaArboreaDichiarataVO();
				unitaArboreaDichiarataVO.setIdUnitaArboreaDichiarata(new Long(rs.getLong("ID_UNITA_ARBOREA_DICHIARATA")));
				unitaArboreaDichiarataVO.setCodiceFotografiaTerreni(rs.getString("CODICE_FOTOGRAFIA_TERRENI"));
				unitaArboreaDichiarataVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
				unitaArboreaDichiarataVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
				unitaArboreaDichiarataVO.setProgrUnar(rs.getString("PROGR_UNAR"));
				unitaArboreaDichiarataVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
				unitaArboreaDichiarataVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
				unitaArboreaDichiarataVO.setDataLavorazione(rs.getTimestamp("DATA_LAVORAZIONE"));
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_UNAR"))) {
					unitaArboreaDichiarataVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
					TipoTipologiaUnitaArboreaVO tipoTipologiaUnitaArboreaVO = new TipoTipologiaUnitaArboreaVO();
					tipoTipologiaUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
					tipoTipologiaUnitaArboreaVO.setDescrizione(rs.getString("DESC_TIPO"));
					unitaArboreaDichiarataVO.setTipoTipologiaUnitaArboreaVO(tipoTipologiaUnitaArboreaVO);
				}
				unitaArboreaDichiarataVO.setArea(rs.getString("AREA"));
				unitaArboreaDichiarataVO.setSestoSuFila(rs.getString("SESTO_SU_FILA"));
				unitaArboreaDichiarataVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
				unitaArboreaDichiarataVO.setNumCeppi(rs.getString("NUM_CEPPI"));
				unitaArboreaDichiarataVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
				unitaArboreaDichiarataVO.setAnnoReinnesto(rs.getString("ANNO_REINNESTO"));
				if(Validator.isNotEmpty(rs.getString("ID_FORMA_ALLEVAMENTO"))) {
					unitaArboreaDichiarataVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
					TipoFormaAllevamentoVO tipoFormaAllevamentoVO = new TipoFormaAllevamentoVO();
					tipoFormaAllevamentoVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
					tipoFormaAllevamentoVO.setDescrizione(rs.getString("DESC_ALLEVAMENTO"));
					unitaArboreaDichiarataVO.setTipoFormaAllevamentoVO(tipoFormaAllevamentoVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_IRRIGAZIONE_UNAR"))) {
					unitaArboreaDichiarataVO.setIdIrrigazioneUnar(new Long(rs.getLong("ID_IRRIGAZIONE_UNAR")));
					TipoIrrigazioneUnitaArboreaVO tipoIrrigazioneUnitaArboreaVO = new TipoIrrigazioneUnitaArboreaVO();
					tipoIrrigazioneUnitaArboreaVO.setIdIrrigazioneUnar(new Long(rs.getLong("ID_IRRIGAZIONE_UNAR")));
					tipoIrrigazioneUnitaArboreaVO.setDescrizione(rs.getString("DESC_IRRIGAZIONE"));
					unitaArboreaDichiarataVO.setTipoIrrigazioneUnitaArboreaVO(tipoIrrigazioneUnitaArboreaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_COLTIVAZIONE_UNAR"))) {
					unitaArboreaDichiarataVO.setIdColtivazioneUnar(new Long(rs.getLong("ID_COLTIVAZIONE_UNAR")));
					TipoColtivazioneUnitaArboreaVO tipoColtivazioneUnitaArboreaVO = new TipoColtivazioneUnitaArboreaVO();
					tipoColtivazioneUnitaArboreaVO.setIdColtivazioneUnar(new Long(rs.getLong("ID_COLTIVAZIONE_UNAR")));
					tipoColtivazioneUnitaArboreaVO.setDescrizione(rs.getString("DESC_COLTIVAZIONE"));
					unitaArboreaDichiarataVO.setTipoColtivazioneUnitaArboreaVO(tipoColtivazioneUnitaArboreaVO);
				}
				unitaArboreaDichiarataVO.setCodiceTipoVarieta(rs.getString("CODICE_TIPO_VARIETA"));
				unitaArboreaDichiarataVO.setPresenzaAltriVitigni(rs.getString("PRESENZA_ALTRI_VITIGNI"));
				unitaArboreaDichiarataVO.setNumeroPianteProduttivo(rs.getString("NUMERO_PIANTE_PRODUTTIVO"));
				unitaArboreaDichiarataVO.setNumeroAltrePiante(rs.getString("NUMERO_ALTRE_PIANTE"));
				unitaArboreaDichiarataVO.setCampagna(rs.getString("CAMPAGNA"));
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VIGNETO"))) {
					unitaArboreaDichiarataVO.setIdTipologiaVigneto(new Long(rs.getLong("ID_TIPOLOGIA_VIGNETO")));
					TipoTipologiaVignetoVO tipoTipologiaVignetoVO = new TipoTipologiaVignetoVO();
					tipoTipologiaVignetoVO.setIdTipologiaVigneto(new Long(rs.getLong("ID_TIPOLOGIA_VIGNETO")));
					tipoTipologiaVignetoVO.setDescrizione(rs.getString("DESC_VIGNETO"));
					unitaArboreaDichiarataVO.setTipoTipologiaVignetoVO(tipoTipologiaVignetoVO);
				}
				unitaArboreaDichiarataVO.setTipoImpianto(rs.getString("TIPO_IMPIANTO"));
				unitaArboreaDichiarataVO.setNumeroCastagni(rs.getString("NUMERO_CASTAGNI"));
				unitaArboreaDichiarataVO.setGruppo(rs.getString("GRUPPO"));
				unitaArboreaDichiarataVO.setRicaduta(rs.getString("RICADUTA"));
				if(Validator.isNotEmpty(rs.getString("ID_GIACITURA_UNAR"))) {
					unitaArboreaDichiarataVO.setIdGiacituraUnar(new Long(rs.getLong("ID_GIACITURA_UNAR")));
					TipoGiacituraUnitaArboreaVO tipoGiacituraUnitaArboreaVO = new TipoGiacituraUnitaArboreaVO();
					tipoGiacituraUnitaArboreaVO.setIdGiacituraUnar(new Long(rs.getLong("ID_GIACITURA_UNAR")));
					tipoGiacituraUnitaArboreaVO.setDescrizione(rs.getString("DESC_GIACITURA"));
					unitaArboreaDichiarataVO.setTipoGiacituraUnitaArboreaVO(tipoGiacituraUnitaArboreaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_ROCCIA_UNAR"))) {
					unitaArboreaDichiarataVO.setIdRocciaUnar(new Long(rs.getLong("ID_ROCCIA_UNAR")));
					TipoRocciaUnitaArboreaVO tipoRocciaUnitaArboreaVO = new TipoRocciaUnitaArboreaVO();
					tipoRocciaUnitaArboreaVO.setIdRocciaUnar(new Long(rs.getLong("ID_ROCCIA_UNAR")));
					tipoRocciaUnitaArboreaVO.setDescrizione(rs.getString("DESC_ROCCIA"));
					unitaArboreaDichiarataVO.setTipoRocciaUnitaArboreaVO(tipoRocciaUnitaArboreaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_SCHELETRO_UNAR"))) {
					unitaArboreaDichiarataVO.setIdScheletroUnar(new Long(rs.getLong("ID_SCHELETRO_UNAR")));
					TipoScheletroUnitaArboreaVO tipoScheletroUnitaArboreaVO = new TipoScheletroUnitaArboreaVO();
					tipoScheletroUnitaArboreaVO.setIdScheletroUnar(new Long(rs.getLong("ID_SCHELETRO_UNAR")));
					tipoScheletroUnitaArboreaVO.setDescrizione(rs.getString("DESC_SCHELETRO"));
					unitaArboreaDichiarataVO.setTipoScheletroUnitaArboreaVO(tipoScheletroUnitaArboreaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_STATO_VEGETATIVO_UNAR"))) {
					unitaArboreaDichiarataVO.setIdStatoVegetativoUnar(new Long(rs.getLong("ID_STATO_VEGETATIVO_UNAR")));
					TipoStatoVegetativoUnitaArboreaVO tipoStatoVegetativoUnitaArboreaVO = new TipoStatoVegetativoUnitaArboreaVO();
					tipoStatoVegetativoUnitaArboreaVO.setIdStatoVegetativoUnar(new Long(rs.getLong("ID_STATO_VEGETATIVO_UNAR")));
					tipoStatoVegetativoUnitaArboreaVO.setDescrizione(rs.getString("DESC_STATO_VEG"));
					unitaArboreaDichiarataVO.setTipoStatoVegetativoUnitaArboreaVO(tipoStatoVegetativoUnitaArboreaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_POTATURA_UNAR"))) {
					unitaArboreaDichiarataVO.setIdPotaturaUnar(new Long(rs.getLong("ID_POTATURA_UNAR")));
					TipoPotaturaUnitaArboreaVO tipoPotaturaUnitaArboreaVO = new TipoPotaturaUnitaArboreaVO();
					tipoPotaturaUnitaArboreaVO.setIdPotaturaUnar(new Long(rs.getLong("ID_POTATURA_UNAR")));
					tipoPotaturaUnitaArboreaVO.setDescrizione(rs.getString("DESC_POTATURA"));
					unitaArboreaDichiarataVO.setTipoPotaturaUnitaArboreaVO(tipoPotaturaUnitaArboreaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_GIUDIZIO_UNAR"))) {
					unitaArboreaDichiarataVO.setIdGiudizioUnar(new Long(rs.getLong("ID_GIUDIZIO_UNAR")));
					TipoGiudizioUnitaArboreaVO tipoGiudizioUnitaArboreaVO = new TipoGiudizioUnitaArboreaVO();
					tipoGiudizioUnitaArboreaVO.setIdGiudizioUnar(new Long(rs.getLong("ID_GIUDIZIO_UNAR")));
					tipoGiudizioUnitaArboreaVO.setDescrizione(rs.getString("DESC_GIUDIZIO"));
					unitaArboreaDichiarataVO.setTipoGiudizioUnitaArboreaVO(tipoGiudizioUnitaArboreaVO);
				}
				unitaArboreaDichiarataVO.setSupplementari(rs.getString("SUPPLEMENTARI"));
				unitaArboreaDichiarataVO.setMeccanizzabile(rs.getString("MECCANIZZABILE"));
				unitaArboreaDichiarataVO.setDimensioneChioma(rs.getString("DIMENSIONE_CHIOMA"));
				if(Validator.isNotEmpty(rs.getString("ID_ETA_IMPIANTO_UNAR"))) {
					unitaArboreaDichiarataVO.setIdEtaImpiantoUnar(new Long(rs.getLong("ID_ETA_IMPIANTO_UNAR")));
					TipoEtaImpiantoUnitaArboreaVO tipoEtaImpiantoUnitaArboreaVO = new TipoEtaImpiantoUnitaArboreaVO();
					tipoEtaImpiantoUnitaArboreaVO.setIdEtaImpiantoUnar(new Long(rs.getLong("ID_ETA_IMPIANTO_UNAR")));
					tipoEtaImpiantoUnitaArboreaVO.setDescrizione(rs.getString("DESC_ETA_IMPIANTO"));
					unitaArboreaDichiarataVO.setTipoEtaImpiantoUnitaArboreaVO(tipoEtaImpiantoUnitaArboreaVO);
				}
				unitaArboreaDichiarataVO.setProvinciaCCIAA(rs.getString("PROVINCIA_CCIAA"));
				unitaArboreaDichiarataVO.setMatricolaCCIAA(rs.getString("MATRICOLA_CCIAA"));
				unitaArboreaDichiarataVO.setConfermaPrecIscrizioneAlbo(rs.getString("CONFERMA_PREC_ISCRIZIONE_ALBO"));
				unitaArboreaDichiarataVO.setRichiestaNuovaIscrAlbo(rs.getString("RICHIESTA_NUOVA_ISCR_ALBO"));
				unitaArboreaDichiarataVO.setConfermaRichNuovaIscrAlbo(rs.getString("CONFERMA_RICH_NUOVA_ISCR_ALBO"));
				unitaArboreaDichiarataVO.setSuperficieDaIscrivereAlbo(rs.getString("SUPERFICIE_DA_ISCRIVERE_ALBO"));
				unitaArboreaDichiarataVO.setAnnoIscrizioneAlbo(rs.getString("ANNO_ISCRIZIONE_ALBO"));
				unitaArboreaDichiarataVO.setIdFonte(new Long(rs.getLong("ID_FONTE")));
				CodeDescription fonte = new CodeDescription(Integer.decode(rs.getString("ID_FONTE")), rs.getString("DESC_FONTE"));
				unitaArboreaDichiarataVO.setTipoFonte(fonte);
				unitaArboreaDichiarataVO.setNote(rs.getString("NOTE"));
				unitaArboreaDichiarataVO.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO"));
				unitaArboreaDichiarataVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) {
					unitaArboreaDichiarataVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
					TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
					tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
					tipoVarietaVO.setDescrizione(rs.getString("DESC_VARIETA"));
					tipoVarietaVO.setCodiceVarieta(rs.getString("CODICE_VARIETA"));
					unitaArboreaDichiarataVO.setTipoVarietaVO(tipoVarietaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO"))) {
					unitaArboreaDichiarataVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
					TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
					tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
					tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
					tipoUtilizzoVO.setDescrizione(rs.getString("DESC_UTILIZZO"));
					unitaArboreaDichiarataVO.setTipoUtilizzoVO(tipoUtilizzoVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_VINO"))) {
					unitaArboreaDichiarataVO.setIdVino(new Long(rs.getLong("ID_VINO")));
					TipoVinoVO tipoVinoVO = new TipoVinoVO();
					tipoVinoVO.setIdVino(new Long(rs.getLong("ID_VINO")));
					tipoVinoVO.setDescrizione(rs.getString("DESC_VINO"));
					unitaArboreaDichiarataVO.setTipoVinoVO(tipoVinoVO);
				}
				unitaArboreaDichiarataVO.setPercentualeVarieta(rs.getString("PERCENTUALE_VARIETA"));
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VINO"))) {
					unitaArboreaDichiarataVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
					TipoTipologiaVinoVO tipoTipologiaVinoVO = new TipoTipologiaVinoVO();
					tipoTipologiaVinoVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
					tipoTipologiaVinoVO.setDescrizione(rs.getString("DESC_TIPO_VINO"));
					unitaArboreaDichiarataVO.setTipoTipologiaVinoVO(tipoTipologiaVinoVO);
				}
				unitaArboreaDichiarataVO.setDataCessazione(rs.getTimestamp("DATA_CESSAZIONE"));
				if(Validator.isNotEmpty(rs.getString("ID_CESSAZIONE_UNAR"))) {
					unitaArboreaDichiarataVO.setIdCessazioneUnar(new Long(rs.getLong("ID_CESSAZIONE_UNAR")));
					TipoCessazioneUnarVO tipoCessazioneUnarVO = new TipoCessazioneUnarVO();
					tipoCessazioneUnarVO.setIdCessazioneUnar(new Long(rs.getLong("ID_CESSAZIONE_UNAR")));
					tipoCessazioneUnarVO.setDescrizione(rs.getString("DESC_CESSAZIONE"));
					unitaArboreaDichiarataVO.setTipoCessazioneUnarVO(tipoCessazioneUnarVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_CAUSALE_MODIFICA"))) {
					unitaArboreaDichiarataVO.setIdCausaleModifica(new Long(rs.getLong("ID_CAUSALE_MODIFICA")));
					TipoCausaleModificaVO tipoCausaleModificaVO = new TipoCausaleModificaVO();
					tipoCausaleModificaVO.setIdCausaleModifica(new Long(rs.getLong("ID_CAUSALE_MODIFICA")));
					tipoCausaleModificaVO.setDescrizione(rs.getString("DESC_MODIFICA"));
					unitaArboreaDichiarataVO.setTipoCausaleModificaVO(tipoCausaleModificaVO);
				}
				unitaArboreaDichiarataVO.setDataEsecuzione(rs.getTimestamp("DATA_ESECUZIONE"));
				unitaArboreaDichiarataVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
				unitaArboreaDichiarataVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
				elencoUnitaArboreeDichiarate.add(unitaArboreaDichiarataVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListUnitaArboreaDichiarataByPianoRiferimentoAndIdStoricoUnitaArborea in UnitaArboreaDichiarataDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListUnitaArboreaDichiarataByPianoRiferimentoAndIdStoricoUnitaArborea in UnitaArboreaDichiarataDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListUnitaArboreaDichiarataByPianoRiferimentoAndIdStoricoUnitaArborea in UnitaArboreaDichiarataDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListUnitaArboreaDichiarataByPianoRiferimentoAndIdStoricoUnitaArborea in UnitaArboreaDichiarataDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListUnitaArboreaDichiarataByPianoRiferimentoAndIdStoricoUnitaArborea method in UnitaArboreaDichiarataDAO\n");
		if(elencoUnitaArboreeDichiarate.size() == 0) {
			return (UnitaArboreaDichiarataVO[])elencoUnitaArboreeDichiarate.toArray(new UnitaArboreaDichiarataVO[0]);
		}
		else {
			return (UnitaArboreaDichiarataVO[])elencoUnitaArboreeDichiarate.toArray(new UnitaArboreaDichiarataVO[elencoUnitaArboreeDichiarate.size()]);
		}
	}
	
	
	public long getIdUnitaArboreaDichiarata(long idStoricoUnitaArborea, long idDichiarazioneConsistenza) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getIdUnitaArboreaDichiarata method in UnitaArboreaDichiarataDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    long result=0;
    
    try 
    {
    
      SolmrLogger.debug(this, "Creating db-connection in getIdUnitaArboreaDichiarata method in UnitaArboreaDichiarataDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getIdUnitaArboreaDichiarata method in UnitaArboreaDichiarataDAO and it values: "+conn+"\n");
      
      
      String query = " SELECT UAD.ID_UNITA_ARBOREA_DICHIARATA "+
                     "FROM DB_UNITA_ARBOREA_DICHIARATA UAD, DB_DICHIARAZIONE_CONSISTENZA DC "+
                     "WHERE UAD.ID_STORICO_UNITA_ARBOREA = ? "+
                     "AND UAD.CODICE_FOTOGRAFIA_TERRENI = DC.CODICE_FOTOGRAFIA_TERRENI "+
                     "AND DC.ID_DICHIARAZIONE_CONSISTENZA = ? ";
        
      
      
      SolmrLogger.debug(this, "Value of parameter 1 [ID_STORICO_UNITA_ARBOREA] in getIdUnitaArboreaDichiarata method in UnitaArboreaDichiarataDAO: "+idStoricoUnitaArborea+"\n");
      SolmrLogger.debug(this, "Value of parameter 3 [ID_DICHIARAZIONE_CONSISTENZA] in getIdUnitaArboreaDichiarata method in UnitaArboreaDichiarataDAO: "+idDichiarazioneConsistenza+"\n");
      
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idStoricoUnitaArborea);
      stmt.setLong(2, idDichiarazioneConsistenza);
      
      SolmrLogger.debug(this, "Executing getIdUnitaArboreaDichiarata:"+query+"\n");
      
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next()) 
        result=rs.getLong("ID_UNITA_ARBOREA_DICHIARATA");
        
      rs.close();
      stmt.close();       
    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "getIdUnitaArboreaDichiarata - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) 
    {
      SolmrLogger.error(this, "getIdUnitaArboreaDichiarata - Generic Exception: "+ex+"\n");
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
        SolmrLogger.error(this, "getIdUnitaArboreaDichiarata - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) 
      {
        SolmrLogger.error(this, "getIdUnitaArboreaDichiarata - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
	
	
	/*public Vector<StoricoParticellaVO> getElencoStoricoUnitaArboreaByNotifica(long idNotifica, 
      long idDichiarazioneConsistenza) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getElencoStoricoUnitaArboreaByNotifica method in UnitaArboreaDichiarataDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelleArboreeDichiarate = new Vector<StoricoParticellaVO>();

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getElencoStoricoUnitaArboreaByNotifica method in UnitaArboreaDichiarataDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getElencoStoricoUnitaArboreaByNotifica method in UnitaArboreaDichiarataDAO and it values: "+conn+"\n");

      String query = 
        " SELECT SP.ID_STORICO_PARTICELLA, " +
        "        SP.COMUNE, " +
        "        C.DESCOM, " +
        "        P.SIGLA_PROVINCIA, " +
        "        P.ISTAT_PROVINCIA, " +
        "        SP.SEZIONE, " +
        "        SP.FOGLIO, " +
        "        SP.PARTICELLA, " +
        "        SP.SUBALTERNO, " +
        "        SP.ID_PARTICELLA, " +
        "        SUA.ID_UNITA_ARBOREA, " +
        "        UAD.ID_UNITA_ARBOREA_DICHIARATA, " +
        "        UAD.ID_STORICO_UNITA_ARBOREA, " +
        "        UAD.CODICE_FOTOGRAFIA_TERRENI, " +
        "        UAD.PROGR_UNAR, " +
        "        UAD.ID_TIPOLOGIA_UNAR, " +
        "        TTU.DESCRIZIONE AS DESC_TIPO_UNAR, " +
        "        UAD.AREA, " +
        "        UAD.SESTO_SU_FILA, " +
        "        UAD.SESTO_TRA_FILE, " +
        "        UAD.NUM_CEPPI, " +
        "        UAD.ANNO_IMPIANTO, " +
        "        UAD.DATA_IMPIANTO, " +
        "        UAD.ANNO_ISCRIZIONE_ALBO, " +
        "        UAD.ID_VARIETA, " +
        "        TVAR.DESCRIZIONE AS DESC_VARIETA, " +
        "        TVAR.CODICE_VARIETA AS COD_VAR, " +
        "        UAD.ID_UTILIZZO, " +
        "        TU.CODICE, " +
        "        TU.DESCRIZIONE AS DESC_TIPO_UTILIZZO, " +
        "        UAD.ID_TIPOLOGIA_VINO, " +
        "        TTV.DESCRIZIONE AS DESC_TIPO_VINO " +
        " FROM   DB_STORICO_PARTICELLA SP, " +
        "        COMUNE C, " +
        "        PROVINCIA P, " +
        "        DB_UNITA_ARBOREA_DICHIARATA UAD, " +
        "        DB_STORICO_UNITA_ARBOREA SUA, " +
        "        DB_TIPO_TIPOLOGIA_UNAR TTU, " +
        "        DB_TIPO_VARIETA TVAR, " +
        "        DB_TIPO_UTILIZZO TU, " +
        "        DB_UTE U, " +
        "        DB_CONDUZIONE_DICHIARATA CD, " +
        "        DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "        DB_TIPO_TIPOLOGIA_VINO TTV, " +
        "        DB_NOTIFICA_ENTITA NE " +
        " WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        " AND    NE.ID_NOTIFICA = ? " +
        " AND    NE.IDENTIFICATIVO = SUA.ID_UNITA_ARBOREA " +
        " AND    NE.DATA_FINE_VALIDITA IS NULL " +
        " AND    DC.CODICE_FOTOGRAFIA_TERRENI = UAD.CODICE_FOTOGRAFIA_TERRENI " +
        " AND    UAD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
        " AND    UAD.ID_STORICO_UNITA_ARBOREA = SUA.ID_STORICO_UNITA_ARBOREA " +
        " AND    SP.COMUNE = C.ISTAT_COMUNE " +  
        " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
        " AND    UAD.ID_TIPOLOGIA_UNAR = TTU.ID_TIPOLOGIA_UNAR(+) " +  
        " AND    UAD.ID_VARIETA = TVAR.ID_VARIETA(+) " +  
        " AND    UAD.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
        " AND    UAD.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO(+) " +
        " AND    UAD.ID_AZIENDA = U.ID_AZIENDA " +
        " AND    TRUNC(U.DATA_INIZIO_ATTIVITA) <= TRUNC(DC.DATA) " +
        " AND    TRUNC(NVL(U.DATA_FINE_ATTIVITA, ?)) > TRUNC(DC.DATA) " +
        " AND    U.ID_UTE = CD.ID_UTE " +  
        " AND    CD.ID_STORICO_PARTICELLA = UAD.ID_STORICO_PARTICELLA " +
        " AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        " AND    CD.ID_TITOLO_POSSESSO NOT IN (5,6) " +
        " ORDER BY  C.DESCOM, " +
        "          SP.SEZIONE, " +
        "          SP.FOGLIO, " +
        "          SP.PARTICELLA, " +
        "          SP.SUBALTERNO, " +
        "          TTU.DESCRIZIONE ASC, " +
        "          PROGR_UNAR ASC ";
      
      

      SolmrLogger.debug(this, "Value of parameter 1 [ID_DICHIARAZIONE_CONSISTENZA] in getElencoStoricoUnitaArboreaDichiarataBasic method in UnitaArboreaDichiarataDAO: "+idDichiarazioneConsistenza+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ID_NOTIFICA] in getElencoStoricoUnitaArboreaDichiarataBasic method in UnitaArboreaDichiarataDAO: "+idNotifica+"\n");

      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      
      stmt.setLong(++indice, idDichiarazioneConsistenza);
      stmt.setLong(++indice, idNotifica);
      stmt.setDate(++indice, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
      

      SolmrLogger.debug(this, "Executing getElencoStoricoUnitaArboreaDichiarataBasic: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
        UnitaArboreaDichiarataVO unitaArboreaDichiarataVO = new UnitaArboreaDichiarataVO();
        storicoParticellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
        storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
        ComuneVO comuneVO = new ComuneVO();
        comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
        comuneVO.setDescom(rs.getString("DESCOM"));
        comuneVO.setIstatProvincia(rs.getString("ISTAT_PROVINCIA"));
        storicoParticellaVO.setComuneParticellaVO(comuneVO);
        storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
        storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
        storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
        storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
        unitaArboreaDichiarataVO.setIdUnitaArboreaDichiarata(new Long(rs.getLong("ID_UNITA_ARBOREA_DICHIARATA")));
        StoricoUnitaArboreaVO storicoUnitaArboreaVO = new StoricoUnitaArboreaVO();
        storicoUnitaArboreaVO.setIdUnitaArborea(new Long(rs.getLong("ID_UNITA_ARBOREA")));
        storicoUnitaArboreaVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
        storicoParticellaVO.setStoricoUnitaArboreaVO(storicoUnitaArboreaVO);
        unitaArboreaDichiarataVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
        unitaArboreaDichiarataVO.setCodiceFotografiaTerreni(rs.getString("CODICE_FOTOGRAFIA_TERRENI"));
        unitaArboreaDichiarataVO.setProgrUnar(rs.getString("PROGR_UNAR"));
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_UNAR"))) 
        {
          unitaArboreaDichiarataVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
          TipoTipologiaUnitaArboreaVO tipoTipologiaUnitaArboreaVO = new TipoTipologiaUnitaArboreaVO();
          tipoTipologiaUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
          tipoTipologiaUnitaArboreaVO.setDescrizione(rs.getString("DESC_TIPO_UNAR"));
          unitaArboreaDichiarataVO.setTipoTipologiaUnitaArboreaVO(tipoTipologiaUnitaArboreaVO);
        }
        unitaArboreaDichiarataVO.setArea(rs.getString("AREA"));
        unitaArboreaDichiarataVO.setSestoSuFila(rs.getString("SESTO_SU_FILA"));
        unitaArboreaDichiarataVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
        unitaArboreaDichiarataVO.setNumCeppi(rs.getString("NUM_CEPPI"));
        unitaArboreaDichiarataVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
        unitaArboreaDichiarataVO.setDataImpianto(rs.getDate("DATA_IMPIANTO"));
        unitaArboreaDichiarataVO.setAnnoIscrizioneAlbo(rs.getString("ANNO_ISCRIZIONE_ALBO"));
        if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) {
          unitaArboreaDichiarataVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
          tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          tipoVarietaVO.setDescrizione(rs.getString("DESC_VARIETA"));
          tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VAR"));
          unitaArboreaDichiarataVO.setTipoVarietaVO(tipoVarietaVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO"))) {
          unitaArboreaDichiarataVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
          TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
          tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
          tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
          tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTILIZZO"));
          unitaArboreaDichiarataVO.setTipoUtilizzoVO(tipoUtilizzoVO);
        }
        
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VINO"))) {
          unitaArboreaDichiarataVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
          TipoTipologiaVinoVO tipoTipologiaVinoVO = new TipoTipologiaVinoVO();
          tipoTipologiaVinoVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
          tipoTipologiaVinoVO.setDescrizione(rs.getString("DESC_TIPO_VINO"));
          unitaArboreaDichiarataVO.setTipoTipologiaVinoVO(tipoTipologiaVinoVO);
        }
              
        
        storicoParticellaVO.setUnitaArboreaDichiarataVO(unitaArboreaDichiarataVO);        
        
        elencoParticelleArboreeDichiarate.add(storicoParticellaVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getElencoStoricoUnitaArboreaDichiarataBasic in UnitaArboreaDichiarataDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getElencoStoricoUnitaArboreaDichiarataBasic in UnitaArboreaDichiarataDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getElencoStoricoUnitaArboreaDichiarataBasic in UnitaArboreaDichiarataDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getElencoStoricoUnitaArboreaDichiarataBasic in UnitaArboreaDichiarataDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated searchUnitaArboreaDichiarataByParameters method in UnitaArboreaDichiarataDAO\n");
    
    return elencoParticelleArboreeDichiarate;
    
  }*/
	
	/**
	 * mi estraggo anche le entita che sono state chiuse
	 * singolarmente
	 * 
	 * 
	 * 
	 * @param idNotifica
	 * @param idDichiarazioneConsistenza
	 * @return
	 * @throws DataAccessException
	 */
	public Vector<StoricoParticellaVO> getElencoStoricoUnitaArboreaByNotifica(long idNotifica, 
      long idDichiarazioneConsistenza, boolean storico) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getElencoStoricoUnitaArboreaByNotifica method in UnitaArboreaDichiarataDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelleArboreeDichiarate = new Vector<StoricoParticellaVO>();

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getElencoStoricoUnitaArboreaByNotifica method in UnitaArboreaDichiarataDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getElencoStoricoUnitaArboreaByNotifica method in UnitaArboreaDichiarataDAO and it values: "+conn+"\n");

      String query = 
        "WITH ENTITA AS " +
        "      (SELECT NE.IDENTIFICATIVO " +
        "       FROM DB_NOTIFICA_ENTITA NE " +
        "       WHERE NE.ID_NOTIFICA = ? " +
        "       AND NE.DATA_FINE_VALIDITA IS NULL ";
      if(storico)
      {
        query +=
        "       UNION " +
        "       SELECT NE.IDENTIFICATIVO " +
        "       FROM DB_NOTIFICA_ENTITA NE " +
        "       WHERE NE.ID_NOTIFICA = ? " +
        "       AND NE.DATA_FINE_VALIDITA = (SELECT MAX(NE1.DATA_FINE_VALIDITA) " +
        "                                    FROM   DB_NOTIFICA_ENTITA NE1 " +
        "                                    WHERE  NE1.IDENTIFICATIVO = NE.IDENTIFICATIVO " +
        "                                    AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA " +
        "                                    AND    NE1.DATA_FINE_VALIDITA IS NOT NULL) " +
        "                                    AND    NE.IDENTIFICATIVO NOT IN (SELECT NE1.IDENTIFICATIVO " +
        "                                                                     FROM   DB_NOTIFICA_ENTITA NE1 " +
        "                                                                     WHERE  NE1.IDENTIFICATIVO = NE.IDENTIFICATIVO " +
        "                                                                     AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA " +
        "                                                                     AND    NE1.DATA_FINE_VALIDITA IS NULL) ";
      }
      query +=
        " ) " +
        "SELECT DISTINCT " +
        "       SP.ID_STORICO_PARTICELLA, " +
        "       SP.COMUNE, " +
        "       C.DESCOM, " +
        "       P.SIGLA_PROVINCIA, " +
        "       P.ISTAT_PROVINCIA, " +
        "       SP.SEZIONE, " +
        "       SP.FOGLIO, " +
        "       SP.PARTICELLA, " +
        "       SP.SUBALTERNO, " +
        "       SP.ID_PARTICELLA, " +
        "       SUA.ID_UNITA_ARBOREA, " +
        "       UAD.ID_UNITA_ARBOREA_DICHIARATA, " +
        "       UAD.ID_STORICO_UNITA_ARBOREA, " +
        "       UAD.CODICE_FOTOGRAFIA_TERRENI, " +
        "       UAD.PROGR_UNAR, " +
        "       UAD.ID_TIPOLOGIA_UNAR, " +
        "       TTU.DESCRIZIONE AS DESC_TIPO_UNAR, " +
        "       UAD.AREA, " +
        "       UAD.SESTO_SU_FILA, " +
        "       UAD.SESTO_TRA_FILE, " +
        "       UAD.NUM_CEPPI, " +
        "       UAD.ANNO_IMPIANTO, " +
        "       UAD.DATA_IMPIANTO, " +
        "       UAD.ANNO_ISCRIZIONE_ALBO, " +
        "       UAD.ID_VARIETA, " +
        "       TVAR.DESCRIZIONE AS DESC_VARIETA, " +
        "       TVAR.CODICE_VARIETA AS COD_VAR, " +
        "       UAD.ID_UTILIZZO, " +
        "       TU.CODICE, " +
        "       TU.DESCRIZIONE AS DESC_TIPO_UTILIZZO, " +
        "       UAD.ID_TIPOLOGIA_VINO, " +
        "       TTV.DESCRIZIONE AS DESC_TIPO_VINO " +
        "FROM   DB_STORICO_PARTICELLA SP, " +
        "       COMUNE C, " +
        "       PROVINCIA P, " +
        "       DB_UNITA_ARBOREA_DICHIARATA UAD, " +
        "       DB_STORICO_UNITA_ARBOREA SUA, " +
        "       DB_TIPO_TIPOLOGIA_UNAR TTU, " +
        "       DB_TIPO_VARIETA TVAR, " +
        "       DB_TIPO_UTILIZZO TU, " +
        "       DB_CONDUZIONE_DICHIARATA CD, " +
        "       DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "       DB_TIPO_TIPOLOGIA_VINO TTV, " +
        "       ENTITA EN " +
        "WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "AND SUA.ID_UNITA_ARBOREA = EN.IDENTIFICATIVO " +
        "AND DC.CODICE_FOTOGRAFIA_TERRENI = UAD.CODICE_FOTOGRAFIA_TERRENI " +
        "AND UAD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
        "AND UAD.ID_STORICO_UNITA_ARBOREA = SUA.ID_STORICO_UNITA_ARBOREA " +
        "AND SP.COMUNE = C.ISTAT_COMUNE " +
        "AND C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
        "AND UAD.ID_TIPOLOGIA_UNAR = TTU.ID_TIPOLOGIA_UNAR(+) " +
        "AND UAD.ID_VARIETA = TVAR.ID_VARIETA(+) " +
        "AND UAD.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
        "AND UAD.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO(+) " +
        "AND UAD.ID_AZIENDA = DC.ID_AZIENDA " +
        "AND CD.ID_STORICO_PARTICELLA = UAD.ID_STORICO_PARTICELLA " +
        "AND DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        "AND CD.ID_TITOLO_POSSESSO NOT IN (5, 6) " +
        "ORDER BY C.DESCOM, " +
        "            SP.SEZIONE, " +
        "            SP.FOGLIO, " +
        "            SP.PARTICELLA, " +
        "            SP.SUBALTERNO, " +
        "            PROGR_UNAR ASC, " +
        "            TTU.DESCRIZIONE ASC ";
      
      

      SolmrLogger.debug(this, "Value of parameter 1 [ID_DICHIARAZIONE_CONSISTENZA] in getElencoStoricoUnitaArboreaDichiarataBasic method in UnitaArboreaDichiarataDAO: "+idDichiarazioneConsistenza+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ID_NOTIFICA] in getElencoStoricoUnitaArboreaDichiarataBasic method in UnitaArboreaDichiarataDAO: "+idNotifica+"\n");

      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      
      stmt.setLong(++indice, idNotifica);
      if(storico)
      {
        stmt.setLong(++indice, idNotifica);
      }
      stmt.setLong(++indice, idDichiarazioneConsistenza);
      
     
      

      SolmrLogger.debug(this, "Executing getElencoStoricoUnitaArboreaDichiarataBasic: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
        UnitaArboreaDichiarataVO unitaArboreaDichiarataVO = new UnitaArboreaDichiarataVO();
        storicoParticellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
        storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
        ComuneVO comuneVO = new ComuneVO();
        comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
        comuneVO.setDescom(rs.getString("DESCOM"));
        comuneVO.setIstatProvincia(rs.getString("ISTAT_PROVINCIA"));
        storicoParticellaVO.setComuneParticellaVO(comuneVO);
        storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
        storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
        storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
        storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
        unitaArboreaDichiarataVO.setIdUnitaArboreaDichiarata(new Long(rs.getLong("ID_UNITA_ARBOREA_DICHIARATA")));
        StoricoUnitaArboreaVO storicoUnitaArboreaVO = new StoricoUnitaArboreaVO();
        storicoUnitaArboreaVO.setIdUnitaArborea(new Long(rs.getLong("ID_UNITA_ARBOREA")));
        storicoUnitaArboreaVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
        storicoParticellaVO.setStoricoUnitaArboreaVO(storicoUnitaArboreaVO);
        unitaArboreaDichiarataVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
        unitaArboreaDichiarataVO.setCodiceFotografiaTerreni(rs.getString("CODICE_FOTOGRAFIA_TERRENI"));
        unitaArboreaDichiarataVO.setProgrUnar(rs.getString("PROGR_UNAR"));
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_UNAR"))) 
        {
          unitaArboreaDichiarataVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
          TipoTipologiaUnitaArboreaVO tipoTipologiaUnitaArboreaVO = new TipoTipologiaUnitaArboreaVO();
          tipoTipologiaUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
          tipoTipologiaUnitaArboreaVO.setDescrizione(rs.getString("DESC_TIPO_UNAR"));
          unitaArboreaDichiarataVO.setTipoTipologiaUnitaArboreaVO(tipoTipologiaUnitaArboreaVO);
        }
        unitaArboreaDichiarataVO.setArea(rs.getString("AREA"));
        unitaArboreaDichiarataVO.setSestoSuFila(rs.getString("SESTO_SU_FILA"));
        unitaArboreaDichiarataVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
        unitaArboreaDichiarataVO.setNumCeppi(rs.getString("NUM_CEPPI"));
        unitaArboreaDichiarataVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
        unitaArboreaDichiarataVO.setDataImpianto(rs.getDate("DATA_IMPIANTO"));
        unitaArboreaDichiarataVO.setAnnoIscrizioneAlbo(rs.getString("ANNO_ISCRIZIONE_ALBO"));
        if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) {
          unitaArboreaDichiarataVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
          tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          tipoVarietaVO.setDescrizione(rs.getString("DESC_VARIETA"));
          tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VAR"));
          unitaArboreaDichiarataVO.setTipoVarietaVO(tipoVarietaVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO"))) {
          unitaArboreaDichiarataVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
          TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
          tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
          tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
          tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTILIZZO"));
          unitaArboreaDichiarataVO.setTipoUtilizzoVO(tipoUtilizzoVO);
        }
        
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VINO"))) {
          unitaArboreaDichiarataVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
          TipoTipologiaVinoVO tipoTipologiaVinoVO = new TipoTipologiaVinoVO();
          tipoTipologiaVinoVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
          tipoTipologiaVinoVO.setDescrizione(rs.getString("DESC_TIPO_VINO"));
          unitaArboreaDichiarataVO.setTipoTipologiaVinoVO(tipoTipologiaVinoVO);
        }
              
        
        storicoParticellaVO.setUnitaArboreaDichiarataVO(unitaArboreaDichiarataVO);        
        
        elencoParticelleArboreeDichiarate.add(storicoParticellaVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getElencoStoricoUnitaArboreaDichiarataBasic in UnitaArboreaDichiarataDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getElencoStoricoUnitaArboreaDichiarataBasic in UnitaArboreaDichiarataDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getElencoStoricoUnitaArboreaDichiarataBasic in UnitaArboreaDichiarataDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getElencoStoricoUnitaArboreaDichiarataBasic in UnitaArboreaDichiarataDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getElencoStoricoUnitaArboreaByNotifica method in UnitaArboreaDichiarataDAO\n");
    
    return elencoParticelleArboreeDichiarate;
    
  }
	
	
	
}
