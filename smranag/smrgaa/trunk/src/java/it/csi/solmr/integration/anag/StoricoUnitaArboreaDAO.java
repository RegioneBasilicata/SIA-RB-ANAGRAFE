package it.csi.solmr.integration.anag;

import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.dto.terreni.TipoDestinazioneVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoDettaglioUsoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoQualitaUsoVO;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.smranag.smrgaa.util.PaginazioneUtils;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.ProvinciaVO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.FoglioVO;
import it.csi.solmr.dto.anag.ParticellaCertElegVO;
import it.csi.solmr.dto.anag.ParticellaCertificataVO;
import it.csi.solmr.dto.anag.ParticellaVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneParticellaVO;
import it.csi.solmr.dto.anag.terreni.FiltriUnitaArboreaRicercaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaArboreaExcelVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.dto.anag.terreni.StoricoUnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.TipoCausaleModificaVO;
import it.csi.solmr.dto.anag.terreni.TipoCessazioneUnarVO;
import it.csi.solmr.dto.anag.terreni.TipoColtivazioneUnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.TipoFormaAllevamentoVO;
import it.csi.solmr.dto.anag.terreni.TipoGenereIscrizioneVO;
import it.csi.solmr.dto.anag.terreni.TipoInterventoViticoloVO;
import it.csi.solmr.dto.anag.terreni.TipoIrrigazioneUnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.TipoTipologiaUnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.TipoTipologiaVinoVO;
import it.csi.solmr.dto.anag.terreni.TipoUtilizzoVO;
import it.csi.solmr.dto.anag.terreni.TipoVarietaVO;
import it.csi.solmr.dto.anag.terreni.TipoVinoVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;
import it.csi.util.performance.StopWatch;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import oracle.sql.ARRAY;

public class StoricoUnitaArboreaDAO extends it.csi.solmr.integration.BaseDAO {


	public StoricoUnitaArboreaDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public StoricoUnitaArboreaDAO(String refName) throws ResourceAccessException {
		super(refName);
	}
	
	/**
	 * Metodo che mi consente di visualizzare l'elenco delle unità arboree associate ad
	 * una determinata particella ed un'azienda
	 * 
	 * @param idParticella
	 * @param idPianoRiferimento
	 * @param idAzienda
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.StoricoUnitaArboreaVO[]
	 * @throws DataAccessException
	 */
	public StoricoUnitaArboreaVO[] getListStoricoUnitaArboreaByLogicKey(Long idParticella, Long idPianoRiferimento, Long idAzienda, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListStoricoUnitaArboreaByLogicKey method in StoricoUnitaArboreaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<StoricoUnitaArboreaVO> elencoUnitaArboree = new Vector<StoricoUnitaArboreaVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListStoricoUnitaArboreaByLogicKey method in StoricoUnitaArboreaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListStoricoUnitaArboreaByLogicKey method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");

			String query = " SELECT SUA.ID_STORICO_UNITA_ARBOREA, " +
						   "        SUA.ID_UNITA_ARBOREA, " +
						   "        SUA.ID_PARTICELLA, " +
						   "        SUA.PROGR_UNAR, " +
						   "        SUA.DATA_INIZIO_VALIDITA, " +
						   "        SUA.DATA_FINE_VALIDITA, " +
						   "        SUA.DATA_LAVORAZIONE, " +
						   "        SUA.ID_TIPOLOGIA_UNAR, " +
						   "        TTU.DESCRIZIONE AS DESC_TIPO_UNAR, " +
						   "        SUA.AREA, " +
						   "        SUA.SESTO_SU_FILA, " +
						   "        SUA.SESTO_TRA_FILE, " +
						   "        SUA.NUM_CEPPI, " +
						   "        SUA.ANNO_IMPIANTO, " +
						   "        SUA.ANNO_REINNESTO, " +
						   "        SUA.ID_FORMA_ALLEVAMENTO, " +
						   "        TFA.DESCRIZIONE AS DESC_FORMA_ALLEVAMENTO, " +
						   "        SUA.ID_IRRIGAZIONE_UNAR, " +
						   "        SUA.ID_COLTIVAZIONE_UNAR, " +
						   "        SUA.CODICE_TIPO_VARIETA, " +
						   "        SUA.PRESENZA_ALTRI_VITIGNI, " +
						   "        SUA.NUMERO_PIANTE_PRODUTTIVO, " +
						   "        SUA.NUMERO_ALTRE_PIANTE, " +
						   "        SUA.CAMPAGNA, " +
						   "        SUA.ID_TIPOLOGIA_VIGNETO, " +
						   "        SUA.TIPO_IMPIANTO, " +
						   "        SUA.NUMERO_CASTAGNI, " +
						   "        SUA.GRUPPO, " +
						   "        SUA.RICADUTA, " +
						   "        SUA.ID_GIACITURA_UNAR, " +
						   "        SUA.ID_ROCCIA_UNAR, " +
						   "        SUA.ID_SCHELETRO_UNAR, " +
						   "        SUA.ID_STATO_VEGETATIVO_UNAR, " +
						   "        SUA.ID_POTATURA_UNAR, " +
						   "        SUA.ID_GIUDIZIO_UNAR, " +
						   "        SUA.SUPPLEMENTARI, " +
						   "        SUA.MECCANIZZABILE, " +
						   "        SUA.DIMENSIONE_CHIOMA, " +
						   "        SUA.ID_ETA_IMPIANTO_UNAR, " +
						   "        SUA.PROVINCIA_CCIAA, " +
						   "        SUA.MATRICOLA_CCIAA, " +
						   "        SUA.CONFERMA_PREC_ISCRIZIONE_ALBO, " +
						   "        SUA.RICHIESTA_NUOVA_ISCR_ALBO, " +
						   "        SUA.CONFERMA_RICH_NUOVA_ISCR_ALBO, " +
						   "        SUA.SUPERFICIE_DA_ISCRIVERE_ALBO, " +
						   "        SUA.ANNO_ISCRIZIONE_ALBO, " +
						   "        SUA.ID_FONTE, " +
						   "        SUA.ID_VARIAZIONE_UNAR, " +
						   "        SUA.NOTE, " +
						   "        SUA.DATA_AGGIORNAMENTO, " +
						   "        SUA.ID_UTENTE_AGGIORNAMENTO, " +
						   "        SUA.ID_VARIETA, " +
						   "        TVAR.DESCRIZIONE AS DESC_VARIETA, " +
						   "        TVAR.CODICE_VARIETA AS COD_VAR, " +
						   "        SUA.ID_UTILIZZO, " +
						   "        TU.CODICE, " +
						   "        TU.DESCRIZIONE AS DESC_TIPO_UTILIZZO, " +
						   "        SUA.PERCENTUALE_VARIETA, " +
						   "        SUA.ID_VINO, " +
						   "        TV.DESCRIZIONE AS DESC_TIPO_VINO, " +
						   "        SUA.DATA_ESECUZIONE, " +
						   "        SUA.RECORD_MODIFICATO, " +
						   "        SUA.ID_CAUSALE_MODIFICA, " +
						   "        TCM.DESCRIZIONE AS CAUSA_MOD, " +
						   "        SUA.ID_AZIENDA, " +
						   "        SUA.ID_TIPOLOGIA_VINO, " +
						   "        TTV.DESCRIZIONE AS DESC_TIPO_VINO, " +
						   "        SUA.ANNO_RIFERIMENTO, " +
						   "        SUA.COLTURA_SPECIALIZZATA, " +
						   "        SUA.ANNO_PRIMA_PRODUZIONE, " +
               "        SUA.VIGNA, " +
               "        SUA.ID_VIGNA, " +
               "        SUA.ID_MENZIONE_GEOGRAFICA, " +
               "        SUA.ETICHETTA, " +
               "        SUA.ID_GENERE_ISCRIZIONE, " +
               "        SUA.FLAG_IMPRODUTTIVA, " +
               "        SUA.PERCENTUALE_FALLANZA, " +
               "        SUA.DATA_IMPIANTO, " +
               "        SUA.DATA_PRIMA_PRODUZIONE, " +
               "        SUA.DATA_SOVRAINNESTO, " +
               "        SUA.DATA_INTERVENTO, " +
               "        SUA.ID_TIPO_INTERVENTO_VITICOLO, " +
               "        SUA.ID_FILO_SOSTEGNO, " +
               "        SUA.ID_PALO_TESTATA, " +
               "        SUA.ID_PALO_TESSITURA, " +
               "        SUA.ID_ANCORAGGIO_UNAR, " +
               "        SUA.ID_STATO_COLTIVAZIONE_UNAR, " +
               "        SUA.DISTANZA_PALI, " +
               "        SUA.ALTITUDINE_SLM, " +
               "        SUA.AREA_SERVIZIO, " +
               "        SUA.STATO_UNITA_ARBOREA, " +
               "        SUA.PERCENTUALE_PENDENZA_MEDIA, " +
               "        SUA.GRADI_PENDENZA_MEDIA, " +
               "        SUA.GRADI_ESPOSIZIONE_MEDIA," +
               "        SUA.METRI_ALTITUDINE_MEDIA, " +
               "        SUA.ID_UNITA_ARBOREA_MADRE, " +
               "        SUA.ID_CATALOGO_MATRICE " +
						   " FROM   DB_STORICO_UNITA_ARBOREA SUA, " +
						   "        DB_TIPO_TIPOLOGIA_UNAR TTU, " +
						   "        DB_TIPO_FORMA_ALLEVAMENTO TFA, " + 
						   "        DB_TIPO_VARIETA TVAR, " +
						   "        DB_TIPO_VINO TV, " +
						   "        DB_TIPO_UTILIZZO TU, " +
						   "        DB_TIPO_CAUSALE_MODIFICA TCM, " +
						   "        DB_TIPO_TIPOLOGIA_VINO TTV ";
			
			if(idPianoRiferimento.longValue() > 0) {
				// TO DO ...
			}
			query +=       " WHERE  SUA.ID_PARTICELLA = ? " +
			               " AND    SUA.ID_AZIENDA = ? " +
			               " AND    SUA.ID_TIPOLOGIA_UNAR = TTU.ID_TIPOLOGIA_UNAR(+) " +
			               " AND    SUA.ID_FORMA_ALLEVAMENTO = TFA.ID_FORMA_ALLEVAMENTO(+) " +
			               " AND    SUA.ID_VARIETA = TVAR.ID_VARIETA(+) " +
			               " AND    SUA.ID_VINO = TV.ID_VINO(+) " +
			               " AND    SUA.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
			               " AND    SUA.ID_CAUSALE_MODIFICA = TCM.ID_CAUSALE_MODIFICA " +
			               " AND    SUA.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO(+) ";
			if(idPianoRiferimento.longValue() > 0) {
				// TO DO ...
			}
			else{
				query +=   " AND    SUA.DATA_FINE_VALIDITA IS NULL ";
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

			SolmrLogger.debug(this, "Value of parameter 1 [ID_PARTICELLA] in getListStoricoUnitaArboreaByLogicKey method in StoricoUnitaArboreaDAO: "+idParticella+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ID_PIANO_RIFERIMENTO] in getListStoricoUnitaArboreaByLogicKey method in StoricoUnitaArboreaDAO: "+idPianoRiferimento+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ID_AZIENDA] in getListStoricoUnitaArboreaByLogicKey method in StoricoUnitaArboreaDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 4 [ORDINAMENTO] in getListStoricoUnitaArboreaByLogicKey method in StoricoUnitaArboreaDAO: "+ordinamento+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idParticella.longValue());
			stmt.setLong(2, idAzienda.longValue());
			if(idPianoRiferimento.longValue() > 0) {
				// TO DO ...
			}

			SolmrLogger.debug(this, "Executing getListStoricoUnitaArboreaByLogicKey: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) 
			{
				StoricoUnitaArboreaVO storicoUnitaArboreaVO = new StoricoUnitaArboreaVO();
				TipoCausaleModificaVO tipoCausaleModificaVO = new TipoCausaleModificaVO();
				storicoUnitaArboreaVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
				storicoUnitaArboreaVO.setIdUnitaArborea(new Long(rs.getLong("ID_UNITA_ARBOREA")));
				storicoUnitaArboreaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
				storicoUnitaArboreaVO.setProgrUnar(rs.getString("PROGR_UNAR"));
				storicoUnitaArboreaVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				storicoUnitaArboreaVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				storicoUnitaArboreaVO.setDataLavorazione(rs.getDate("DATA_LAVORAZIONE"));
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_UNAR"))) {
					storicoUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
					TipoTipologiaUnitaArboreaVO tipoTipologiaUnitaArboreaVO = new TipoTipologiaUnitaArboreaVO();
					tipoTipologiaUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
					tipoTipologiaUnitaArboreaVO.setDescrizione(rs.getString("DESC_TIPO_UNAR"));
					storicoUnitaArboreaVO.setTipoTipologiaUnitaArboreaVO(tipoTipologiaUnitaArboreaVO);
				}
				storicoUnitaArboreaVO.setArea(rs.getString("AREA"));
				storicoUnitaArboreaVO.setSestoSuFila(rs.getString("SESTO_SU_FILA"));
				storicoUnitaArboreaVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
				storicoUnitaArboreaVO.setNumCeppi(rs.getString("NUM_CEPPI"));
				storicoUnitaArboreaVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
				storicoUnitaArboreaVO.setAnnoReinnesto(rs.getString("ANNO_REINNESTO"));
				if(Validator.isNotEmpty(rs.getString("ID_FORMA_ALLEVAMENTO"))) {
					storicoUnitaArboreaVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
					TipoFormaAllevamentoVO tipoFormaAllevamentoVO = new TipoFormaAllevamentoVO();
					tipoFormaAllevamentoVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
					tipoFormaAllevamentoVO.setDescrizione(rs.getString("DESC_FORMA_ALLEVAMENTO"));
					storicoUnitaArboreaVO.setTipoFormaAllevamentoVO(tipoFormaAllevamentoVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_IRRIGAZIONE_UNAR"))) {
					storicoUnitaArboreaVO.setIdIrrigazioneUnar(new Long(rs.getLong("ID_IRRIGAZIONE_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_COLTIVAZIONE_UNAR"))) {
					storicoUnitaArboreaVO.setIdColtivazioneUnar(new Long(rs.getLong("ID_COLTIVAZIONE_UNAR")));
				}
				storicoUnitaArboreaVO.setCodiceTipoVarieta(rs.getString("CODICE_TIPO_VARIETA"));
				storicoUnitaArboreaVO.setPresenzaAltriVitigni(rs.getString("PRESENZA_ALTRI_VITIGNI"));
				storicoUnitaArboreaVO.setNumeroPianteProduttivo(rs.getString("NUMERO_PIANTE_PRODUTTIVO"));
				storicoUnitaArboreaVO.setNumeroAltrePiante(rs.getString("NUMERO_ALTRE_PIANTE"));
				storicoUnitaArboreaVO.setCampagna(rs.getString("CAMPAGNA"));
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VIGNETO"))) {
					storicoUnitaArboreaVO.setIdTipologiaVigneto(new Long(rs.getLong("ID_TIPOLOGIA_VIGNETO")));
				}
				storicoUnitaArboreaVO.setTipoImpianto(rs.getString("TIPO_IMPIANTO"));
				storicoUnitaArboreaVO.setNumeroCastagni(rs.getString("NUMERO_CASTAGNI"));
				storicoUnitaArboreaVO.setGruppo(rs.getString("GRUPPO"));
				storicoUnitaArboreaVO.setRicaduta(rs.getString("RICADUTA"));
				if(Validator.isNotEmpty(rs.getString("ID_GIACITURA_UNAR"))) {
					storicoUnitaArboreaVO.setIdGiacituraUnar(new Long(rs.getLong("ID_GIACITURA_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_ROCCIA_UNAR"))) {
					storicoUnitaArboreaVO.setIdRocciaUnar(new Long(rs.getLong("ID_ROCCIA_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_SCHELETRO_UNAR"))) {
					storicoUnitaArboreaVO.setIdScheletroUnar(new Long(rs.getLong("ID_SCHELETRO_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_STATO_VEGETATIVO_UNAR"))) {
					storicoUnitaArboreaVO.setIdStatoVegetativoUnar(new Long(rs.getLong("ID_STATO_VEGETATIVO_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_POTATURA_UNAR"))) {
					storicoUnitaArboreaVO.setIdPotaturaUnar(new Long(rs.getLong("ID_POTATURA_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_GIUDIZIO_UNAR"))) {
					storicoUnitaArboreaVO.setIdGiudizioUnar(new Long(rs.getLong("ID_GIUDIZIO_UNAR")));
				}
				storicoUnitaArboreaVO.setSupplementari(rs.getString("SUPPLEMENTARI"));
				storicoUnitaArboreaVO.setMeccanizzabile(rs.getString("MECCANIZZABILE"));
				storicoUnitaArboreaVO.setDimensioneChioma(rs.getString("DIMENSIONE_CHIOMA"));
				if(Validator.isNotEmpty(rs.getString("ID_ETA_IMPIANTO_UNAR"))) {
					storicoUnitaArboreaVO.setIdEtaImpiantoUnar(new Long(rs.getLong("ID_ETA_IMPIANTO_UNAR")));
				}
				storicoUnitaArboreaVO.setProvinciaCCIAA(rs.getString("PROVINCIA_CCIAA"));
				storicoUnitaArboreaVO.setMatricolaCCIAA(rs.getString("MATRICOLA_CCIAA"));
				storicoUnitaArboreaVO.setConfermaPrecIscrizioneAlbo(rs.getString("CONFERMA_PREC_ISCRIZIONE_ALBO"));
				storicoUnitaArboreaVO.setRichiestaNuovaIscrAlbo(rs.getString("RICHIESTA_NUOVA_ISCR_ALBO"));
				storicoUnitaArboreaVO.setConfermaRichNuovaIscrAlbo(rs.getString("CONFERMA_RICH_NUOVA_ISCR_ALBO"));
				storicoUnitaArboreaVO.setSuperficieDaIscrivereAlbo(rs.getString("SUPERFICIE_DA_ISCRIVERE_ALBO"));
				storicoUnitaArboreaVO.setAnnoIscrizioneAlbo(rs.getString("ANNO_ISCRIZIONE_ALBO"));
				storicoUnitaArboreaVO.setIdFonte(new Long(rs.getLong("ID_FONTE")));
				if(Validator.isNotEmpty(rs.getString("ID_VARIAZIONE_UNAR"))) {
					storicoUnitaArboreaVO.setIdVariazioneUnar(new Long(rs.getLong("ID_VARIAZIONE_UNAR")));
				}
				storicoUnitaArboreaVO.setNote(rs.getString("NOTE"));
				storicoUnitaArboreaVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
				storicoUnitaArboreaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) {
					storicoUnitaArboreaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
					TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
					tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
					tipoVarietaVO.setDescrizione(rs.getString("DESC_VARIETA"));
					tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VAR"));
					storicoUnitaArboreaVO.setTipoVarietaVO(tipoVarietaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO"))) {
					storicoUnitaArboreaVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
					TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
					tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
					tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
					tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTILIZZO"));
					storicoUnitaArboreaVO.setTipoUtilizzoVO(tipoUtilizzoVO);
				}
				storicoUnitaArboreaVO.setPercentualeVarieta(rs.getString("PERCENTUALE_VARIETA"));
				if(Validator.isNotEmpty(rs.getString("ID_VINO"))) {
					storicoUnitaArboreaVO.setIdVino(new Long(rs.getLong("ID_VINO")));
				}
				if(rs.getDate("DATA_ESECUZIONE") != null) {
					storicoUnitaArboreaVO.setDataEsecuzione(new Timestamp(rs.getDate("DATA_ESECUZIONE").getTime()));
				}
				storicoUnitaArboreaVO.setRecordModificato(rs.getString("RECORD_MODIFICATO"));
				storicoUnitaArboreaVO.setIdCausaleModifica(new Long(rs.getLong("ID_CAUSALE_MODIFICA")));
				tipoCausaleModificaVO.setIdCausaleModifica(new Long(rs.getLong("ID_CAUSALE_MODIFICA")));
				tipoCausaleModificaVO.setDescrizione(rs.getString("CAUSA_MOD"));
				storicoUnitaArboreaVO.setTipoCausaleModificaVO(tipoCausaleModificaVO);
				if(Validator.isNotEmpty(rs.getString("ID_AZIENDA"))) {
					storicoUnitaArboreaVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VINO"))) {
					storicoUnitaArboreaVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
					TipoTipologiaVinoVO tipoTipologiaVinoVO = new TipoTipologiaVinoVO();
					tipoTipologiaVinoVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
					tipoTipologiaVinoVO.setDescrizione(rs.getString("DESC_TIPO_VINO"));
					storicoUnitaArboreaVO.setTipoTipologiaVinoVO(tipoTipologiaVinoVO);
				}
				storicoUnitaArboreaVO.setAnnoRiferimento(rs.getString("ANNO_RIFERIMENTO"));
				storicoUnitaArboreaVO.setColturaSpecializzata(rs.getString("COLTURA_SPECIALIZZATA"));
				storicoUnitaArboreaVO.setAnnoPrimaProduzione(rs.getString("ANNO_PRIMA_PRODUZIONE"));
				storicoUnitaArboreaVO.setVigna(rs.getString("VIGNA"));
				storicoUnitaArboreaVO.setIdVigna(checkLongNull(rs.getString("ID_VIGNA")));
				storicoUnitaArboreaVO.setIdMenzioneGeografica(checkLongNull(rs.getString("ID_MENZIONE_GEOGRAFICA")));
				storicoUnitaArboreaVO.setEtichetta(rs.getString("ETICHETTA"));
				storicoUnitaArboreaVO.setIdGenereIscrizione(checkLongNull(rs.getString("ID_GENERE_ISCRIZIONE")));
				storicoUnitaArboreaVO.setFlagImproduttiva(rs.getString("FLAG_IMPRODUTTIVA"));
				storicoUnitaArboreaVO.setPercentualeFallanza(rs.getBigDecimal("PERCENTUALE_FALLANZA"));
				storicoUnitaArboreaVO.setDataImpianto(rs.getTimestamp("DATA_IMPIANTO"));
				storicoUnitaArboreaVO.setDataPrimaProduzione(rs.getTimestamp("DATA_PRIMA_PRODUZIONE"));
				
				storicoUnitaArboreaVO.setIdTipoInterventoViticolo(checkLongNull(rs.getString("ID_TIPO_INTERVENTO_VITICOLO")));
				storicoUnitaArboreaVO.setDataIntervento(rs.getTimestamp("DATA_INTERVENTO"));
				storicoUnitaArboreaVO.setDataSovrainnesto(rs.getTimestamp("DATA_SOVRAINNESTO"));
				
				storicoUnitaArboreaVO.setIdFiloSostegno(checkLongNull(rs.getString("ID_FILO_SOSTEGNO")));
				storicoUnitaArboreaVO.setIdPaloTestata(checkLongNull(rs.getString("ID_PALO_TESTATA")));
				storicoUnitaArboreaVO.setIdPaloTessitura(checkLongNull(rs.getString("ID_PALO_TESSITURA")));
				storicoUnitaArboreaVO.setIdAncoraggioUnar(checkLongNull(rs.getString("ID_ANCORAGGIO_UNAR")));
				storicoUnitaArboreaVO.setIdStatoColtivazioneUnar(checkLongNull(rs.getString("ID_STATO_COLTIVAZIONE_UNAR")));
				storicoUnitaArboreaVO.setDistanzaPali(checkLongNull(rs.getString("DISTANZA_PALI")));
				storicoUnitaArboreaVO.setAltitudineSlm(checkLongNull(rs.getString("ALTITUDINE_SLM")));
				storicoUnitaArboreaVO.setAreaServizio(rs.getBigDecimal("AREA_SERVIZIO"));
				storicoUnitaArboreaVO.setStatoUnitaArborea(rs.getString("STATO_UNITA_ARBOREA"));
				
				storicoUnitaArboreaVO.setPercentualePendenzaMedia(rs.getBigDecimal("PERCENTUALE_PENDENZA_MEDIA"));
				storicoUnitaArboreaVO.setGradiPendenzaMedia(rs.getBigDecimal("GRADI_PENDENZA_MEDIA"));
				storicoUnitaArboreaVO.setGradiEsposizioneMedia(rs.getBigDecimal("GRADI_ESPOSIZIONE_MEDIA"));
				storicoUnitaArboreaVO.setMetriAltitudineMedia(checkIntegerNull(rs.getString("METRI_ALTITUDINE_MEDIA")));
				storicoUnitaArboreaVO.setIdUnitaArboreaMadre(checkLongNull(rs.getString("ID_UNITA_ARBOREA_MADRE")));
				storicoUnitaArboreaVO.setIdCatalogoMatrice(checkLongNull(rs.getString("ID_CATALOGO_MATRICE")));
				
				
				elencoUnitaArboree.add(storicoUnitaArboreaVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListStoricoUnitaArboreaByLogicKey in StoricoUnitaArboreaDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListStoricoUnitaArboreaByLogicKey in StoricoUnitaArboreaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListStoricoUnitaArboreaByLogicKey in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListStoricoUnitaArboreaByLogicKey in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListStoricoUnitaArboreaByLogicKey method in StoricoUnitaArboreaDAO\n");
		if(elencoUnitaArboree.size() == 0) {
			return (StoricoUnitaArboreaVO[])elencoUnitaArboree.toArray(new StoricoUnitaArboreaVO[0]);
		}
		else {
			return (StoricoUnitaArboreaVO[])elencoUnitaArboree.toArray(new StoricoUnitaArboreaVO[elencoUnitaArboree.size()]);
		}
	}
	
	/**
	 * Restituisce la lista di di SToricoUnitaArboreaVO passando l'elenco degli idStoricoUnitaArborea
	 * usato nell'elimina particella per ora...
	 * 
	 * 
	 * 
	 * @param vIdStoricoUnitaArborea
	 * @return
	 * @throws DataAccessException
	 */
	public StoricoUnitaArboreaVO[] getListStoricoUnitaArboreaByVidSoricoUnitaArborea(long[] vIdStoricoUnitaArborea) 
	  throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getListStoricoUnitaArboreaByVidSoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoUnitaArboreaVO> elencoUnitaArboree = new Vector<StoricoUnitaArboreaVO>();

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getListStoricoUnitaArboreaByVidSoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListStoricoUnitaArboreaByVidSoricoUnitaArborea method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");

      String query = " SELECT SUA.ID_STORICO_UNITA_ARBOREA, " +
               "        SUA.ID_UNITA_ARBOREA, " +
               "        SUA.ID_PARTICELLA, " +
               "        SUA.PROGR_UNAR, " +
               "        SUA.DATA_INIZIO_VALIDITA, " +
               "        SUA.DATA_FINE_VALIDITA, " +
               "        SUA.DATA_LAVORAZIONE, " +
               "        SUA.ID_TIPOLOGIA_UNAR, " +
               "        TTU.DESCRIZIONE AS DESC_TIPO_UNAR, " +
               "        SUA.AREA, " +
               "        SUA.SESTO_SU_FILA, " +
               "        SUA.SESTO_TRA_FILE, " +
               "        SUA.NUM_CEPPI, " +
               "        SUA.ANNO_IMPIANTO, " +
               "        SUA.DATA_IMPIANTO, " +
               "        SUA.ANNO_REINNESTO, " +
               "        SUA.ID_FORMA_ALLEVAMENTO, " +
               "        TFA.DESCRIZIONE AS DESC_FORMA_ALLEVAMENTO, " +
               "        SUA.ID_IRRIGAZIONE_UNAR, " +
               "        SUA.ID_COLTIVAZIONE_UNAR, " +
               "        SUA.CODICE_TIPO_VARIETA, " +
               "        SUA.PRESENZA_ALTRI_VITIGNI, " +
               "        SUA.NUMERO_PIANTE_PRODUTTIVO, " +
               "        SUA.NUMERO_ALTRE_PIANTE, " +
               "        SUA.CAMPAGNA, " +
               "        SUA.ID_TIPOLOGIA_VIGNETO, " +
               "        SUA.TIPO_IMPIANTO, " +
               "        SUA.NUMERO_CASTAGNI, " +
               "        SUA.GRUPPO, " +
               "        SUA.RICADUTA, " +
               "        SUA.ID_GIACITURA_UNAR, " +
               "        SUA.ID_ROCCIA_UNAR, " +
               "        SUA.ID_SCHELETRO_UNAR, " +
               "        SUA.ID_STATO_VEGETATIVO_UNAR, " +
               "        SUA.ID_POTATURA_UNAR, " +
               "        SUA.ID_GIUDIZIO_UNAR, " +
               "        SUA.SUPPLEMENTARI, " +
               "        SUA.MECCANIZZABILE, " +
               "        SUA.DIMENSIONE_CHIOMA, " +
               "        SUA.ID_ETA_IMPIANTO_UNAR, " +
               "        SUA.PROVINCIA_CCIAA, " +
               "        SUA.MATRICOLA_CCIAA, " +
               "        SUA.CONFERMA_PREC_ISCRIZIONE_ALBO, " +
               "        SUA.RICHIESTA_NUOVA_ISCR_ALBO, " +
               "        SUA.CONFERMA_RICH_NUOVA_ISCR_ALBO, " +
               "        SUA.SUPERFICIE_DA_ISCRIVERE_ALBO, " +
               "        SUA.ANNO_ISCRIZIONE_ALBO, " +
               "        SUA.ID_FONTE, " +
               "        SUA.ID_VARIAZIONE_UNAR, " +
               "        SUA.NOTE, " +
               "        SUA.DATA_AGGIORNAMENTO, " +
               "        SUA.ID_UTENTE_AGGIORNAMENTO, " +
               "        SUA.ID_VARIETA, " +
               "        TVAR.DESCRIZIONE AS DESC_VARIETA, " +
               "        TVAR.CODICE_VARIETA AS COD_VAR, " +
               "        SUA.ID_UTILIZZO, " +
               "        TU.CODICE, " +
               "        TU.DESCRIZIONE AS DESC_TIPO_UTILIZZO, " +
               "        SUA.PERCENTUALE_VARIETA, " +
               "        SUA.ID_VINO, " +
               "        TV.DESCRIZIONE AS DESC_TIPO_VINO, " +
               "        SUA.DATA_ESECUZIONE, " +
               "        SUA.RECORD_MODIFICATO, " +
               "        SUA.ID_CAUSALE_MODIFICA, " +
               "        TCM.DESCRIZIONE AS CAUSA_MOD, " +
               "        SUA.ID_AZIENDA, " +
               "        SUA.ID_TIPOLOGIA_VINO, " +
               "        TTV.DESCRIZIONE AS DESC_TIPO_VINO, " +
               "        SUA.ANNO_RIFERIMENTO, " +
               "        SUA.COLTURA_SPECIALIZZATA, " +
               "        SUA.ANNO_PRIMA_PRODUZIONE, " +
               "        SUA.DATA_PRIMA_PRODUZIONE, " +
               "        SUA.VIGNA, " +
               "        SUA.ID_VIGNA, " +
               "        SUA.ID_MENZIONE_GEOGRAFICA, " +
               "        SUA.ETICHETTA, " +
               "        SUA.ID_GENERE_ISCRIZIONE, " +
               "        SUA.FLAG_IMPRODUTTIVA, " +
               "        SUA.PERCENTUALE_FALLANZA, " +
               "        SUA.STATO_UNITA_ARBOREA, " +
               "        SUA.DATA_SOVRAINNESTO, " +
               "        SUA.DATA_INTERVENTO, " +
               "        SUA.ID_TIPO_INTERVENTO_VITICOLO, " +
               "        SUA.ID_FILO_SOSTEGNO, " +
               "        SUA.ID_PALO_TESTATA, " +
               "        SUA.ID_PALO_TESSITURA, " +
               "        SUA.ID_ANCORAGGIO_UNAR, " +
               "        SUA.ID_STATO_COLTIVAZIONE_UNAR, " +
               "        SUA.DISTANZA_PALI, " +
               "        SUA.ALTITUDINE_SLM, " +
               "        SUA.AREA_SERVIZIO, " +
               "        SUA.PERCENTUALE_PENDENZA_MEDIA, " +
               "        SUA.GRADI_PENDENZA_MEDIA, " +
               "        SUA.GRADI_ESPOSIZIONE_MEDIA," +
               "        SUA.METRI_ALTITUDINE_MEDIA, " +
               "        SUA.ID_UNITA_ARBOREA_MADRE, " +
               "        SUA.ID_CATALOGO_MATRICE " +
               " FROM   DB_STORICO_UNITA_ARBOREA SUA, " +
               "        DB_TIPO_TIPOLOGIA_UNAR TTU, " +
               "        DB_TIPO_FORMA_ALLEVAMENTO TFA, " + 
               "        DB_TIPO_VARIETA TVAR, " +
               "        DB_TIPO_VINO TV, " +
               "        DB_TIPO_UTILIZZO TU, " +
               "        DB_TIPO_CAUSALE_MODIFICA TCM, " +
               "        DB_TIPO_TIPOLOGIA_VINO TTV " +
               " WHERE  SUA.ID_STORICO_UNITA_ARBOREA IN (";
     query +=  getIdListFromArrayForSQL(vIdStoricoUnitaArborea);
     query +=  " ) " +
               " AND    SUA.ID_TIPOLOGIA_UNAR = TTU.ID_TIPOLOGIA_UNAR(+) " +
               " AND    SUA.ID_FORMA_ALLEVAMENTO = TFA.ID_FORMA_ALLEVAMENTO(+) " +
               " AND    SUA.ID_VARIETA = TVAR.ID_VARIETA(+) " +
               " AND    SUA.ID_VINO = TV.ID_VINO(+) " +
               " AND    SUA.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
               " AND    SUA.ID_CAUSALE_MODIFICA = TCM.ID_CAUSALE_MODIFICA " +
               " AND    SUA.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO(+) ";
      
      stmt = conn.prepareStatement(query);
      
      SolmrLogger.debug(this, "Executing getListStoricoUnitaArboreaByVidSoricoUnitaArborea: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        StoricoUnitaArboreaVO storicoUnitaArboreaVO = new StoricoUnitaArboreaVO();
        TipoCausaleModificaVO tipoCausaleModificaVO = new TipoCausaleModificaVO();
        storicoUnitaArboreaVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
        storicoUnitaArboreaVO.setIdUnitaArborea(new Long(rs.getLong("ID_UNITA_ARBOREA")));
        storicoUnitaArboreaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
        storicoUnitaArboreaVO.setProgrUnar(rs.getString("PROGR_UNAR"));
        storicoUnitaArboreaVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        storicoUnitaArboreaVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        storicoUnitaArboreaVO.setDataLavorazione(rs.getDate("DATA_LAVORAZIONE"));
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_UNAR"))) {
          storicoUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
          TipoTipologiaUnitaArboreaVO tipoTipologiaUnitaArboreaVO = new TipoTipologiaUnitaArboreaVO();
          tipoTipologiaUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
          tipoTipologiaUnitaArboreaVO.setDescrizione(rs.getString("DESC_TIPO_UNAR"));
          storicoUnitaArboreaVO.setTipoTipologiaUnitaArboreaVO(tipoTipologiaUnitaArboreaVO);
        }
        storicoUnitaArboreaVO.setArea(rs.getString("AREA"));
        storicoUnitaArboreaVO.setSestoSuFila(rs.getString("SESTO_SU_FILA"));
        storicoUnitaArboreaVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
        storicoUnitaArboreaVO.setNumCeppi(rs.getString("NUM_CEPPI"));
        storicoUnitaArboreaVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
        storicoUnitaArboreaVO.setAnnoReinnesto(rs.getString("ANNO_REINNESTO"));
        if(Validator.isNotEmpty(rs.getString("ID_FORMA_ALLEVAMENTO"))) {
          storicoUnitaArboreaVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
          TipoFormaAllevamentoVO tipoFormaAllevamentoVO = new TipoFormaAllevamentoVO();
          tipoFormaAllevamentoVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
          tipoFormaAllevamentoVO.setDescrizione(rs.getString("DESC_FORMA_ALLEVAMENTO"));
          storicoUnitaArboreaVO.setTipoFormaAllevamentoVO(tipoFormaAllevamentoVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_IRRIGAZIONE_UNAR"))) {
          storicoUnitaArboreaVO.setIdIrrigazioneUnar(new Long(rs.getLong("ID_IRRIGAZIONE_UNAR")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_COLTIVAZIONE_UNAR"))) {
          storicoUnitaArboreaVO.setIdColtivazioneUnar(new Long(rs.getLong("ID_COLTIVAZIONE_UNAR")));
        }
        storicoUnitaArboreaVO.setCodiceTipoVarieta(rs.getString("CODICE_TIPO_VARIETA"));
        storicoUnitaArboreaVO.setPresenzaAltriVitigni(rs.getString("PRESENZA_ALTRI_VITIGNI"));
        storicoUnitaArboreaVO.setNumeroPianteProduttivo(rs.getString("NUMERO_PIANTE_PRODUTTIVO"));
        storicoUnitaArboreaVO.setNumeroAltrePiante(rs.getString("NUMERO_ALTRE_PIANTE"));
        storicoUnitaArboreaVO.setCampagna(rs.getString("CAMPAGNA"));
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VIGNETO"))) {
          storicoUnitaArboreaVO.setIdTipologiaVigneto(new Long(rs.getLong("ID_TIPOLOGIA_VIGNETO")));
        }
        storicoUnitaArboreaVO.setTipoImpianto(rs.getString("TIPO_IMPIANTO"));
        storicoUnitaArboreaVO.setNumeroCastagni(rs.getString("NUMERO_CASTAGNI"));
        storicoUnitaArboreaVO.setGruppo(rs.getString("GRUPPO"));
        storicoUnitaArboreaVO.setRicaduta(rs.getString("RICADUTA"));
        if(Validator.isNotEmpty(rs.getString("ID_GIACITURA_UNAR"))) {
          storicoUnitaArboreaVO.setIdGiacituraUnar(new Long(rs.getLong("ID_GIACITURA_UNAR")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_ROCCIA_UNAR"))) {
          storicoUnitaArboreaVO.setIdRocciaUnar(new Long(rs.getLong("ID_ROCCIA_UNAR")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_SCHELETRO_UNAR"))) {
          storicoUnitaArboreaVO.setIdScheletroUnar(new Long(rs.getLong("ID_SCHELETRO_UNAR")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_STATO_VEGETATIVO_UNAR"))) {
          storicoUnitaArboreaVO.setIdStatoVegetativoUnar(new Long(rs.getLong("ID_STATO_VEGETATIVO_UNAR")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_POTATURA_UNAR"))) {
          storicoUnitaArboreaVO.setIdPotaturaUnar(new Long(rs.getLong("ID_POTATURA_UNAR")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_GIUDIZIO_UNAR"))) {
          storicoUnitaArboreaVO.setIdGiudizioUnar(new Long(rs.getLong("ID_GIUDIZIO_UNAR")));
        }
        storicoUnitaArboreaVO.setSupplementari(rs.getString("SUPPLEMENTARI"));
        storicoUnitaArboreaVO.setMeccanizzabile(rs.getString("MECCANIZZABILE"));
        storicoUnitaArboreaVO.setDimensioneChioma(rs.getString("DIMENSIONE_CHIOMA"));
        if(Validator.isNotEmpty(rs.getString("ID_ETA_IMPIANTO_UNAR"))) {
          storicoUnitaArboreaVO.setIdEtaImpiantoUnar(new Long(rs.getLong("ID_ETA_IMPIANTO_UNAR")));
        }
        storicoUnitaArboreaVO.setProvinciaCCIAA(rs.getString("PROVINCIA_CCIAA"));
        storicoUnitaArboreaVO.setMatricolaCCIAA(rs.getString("MATRICOLA_CCIAA"));
        storicoUnitaArboreaVO.setConfermaPrecIscrizioneAlbo(rs.getString("CONFERMA_PREC_ISCRIZIONE_ALBO"));
        storicoUnitaArboreaVO.setRichiestaNuovaIscrAlbo(rs.getString("RICHIESTA_NUOVA_ISCR_ALBO"));
        storicoUnitaArboreaVO.setConfermaRichNuovaIscrAlbo(rs.getString("CONFERMA_RICH_NUOVA_ISCR_ALBO"));
        storicoUnitaArboreaVO.setSuperficieDaIscrivereAlbo(rs.getString("SUPERFICIE_DA_ISCRIVERE_ALBO"));
        storicoUnitaArboreaVO.setAnnoIscrizioneAlbo(rs.getString("ANNO_ISCRIZIONE_ALBO"));
        storicoUnitaArboreaVO.setIdFonte(new Long(rs.getLong("ID_FONTE")));
        if(Validator.isNotEmpty(rs.getString("ID_VARIAZIONE_UNAR"))) {
          storicoUnitaArboreaVO.setIdVariazioneUnar(new Long(rs.getLong("ID_VARIAZIONE_UNAR")));
        }
        storicoUnitaArboreaVO.setNote(rs.getString("NOTE"));
        storicoUnitaArboreaVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
        storicoUnitaArboreaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) {
          storicoUnitaArboreaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
          tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          tipoVarietaVO.setDescrizione(rs.getString("DESC_VARIETA"));
          tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VAR"));
          storicoUnitaArboreaVO.setTipoVarietaVO(tipoVarietaVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO"))) {
          storicoUnitaArboreaVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
          TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
          tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
          tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
          tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTILIZZO"));
          storicoUnitaArboreaVO.setTipoUtilizzoVO(tipoUtilizzoVO);
        }
        storicoUnitaArboreaVO.setPercentualeVarieta(rs.getString("PERCENTUALE_VARIETA"));
        if(Validator.isNotEmpty(rs.getString("ID_VINO"))) {
          storicoUnitaArboreaVO.setIdVino(new Long(rs.getLong("ID_VINO")));
        }
        if(rs.getDate("DATA_ESECUZIONE") != null) {
          storicoUnitaArboreaVO.setDataEsecuzione(new Timestamp(rs.getDate("DATA_ESECUZIONE").getTime()));
        }
        storicoUnitaArboreaVO.setRecordModificato(rs.getString("RECORD_MODIFICATO"));
        storicoUnitaArboreaVO.setIdCausaleModifica(new Long(rs.getLong("ID_CAUSALE_MODIFICA")));
        tipoCausaleModificaVO.setIdCausaleModifica(new Long(rs.getLong("ID_CAUSALE_MODIFICA")));
        tipoCausaleModificaVO.setDescrizione(rs.getString("CAUSA_MOD"));
        storicoUnitaArboreaVO.setTipoCausaleModificaVO(tipoCausaleModificaVO);
        if(Validator.isNotEmpty(rs.getString("ID_AZIENDA"))) {
          storicoUnitaArboreaVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VINO"))) {
          storicoUnitaArboreaVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
          TipoTipologiaVinoVO tipoTipologiaVinoVO = new TipoTipologiaVinoVO();
          tipoTipologiaVinoVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
          tipoTipologiaVinoVO.setDescrizione(rs.getString("DESC_TIPO_VINO"));
          storicoUnitaArboreaVO.setTipoTipologiaVinoVO(tipoTipologiaVinoVO);
        }
        storicoUnitaArboreaVO.setAnnoRiferimento(rs.getString("ANNO_RIFERIMENTO"));
        storicoUnitaArboreaVO.setColturaSpecializzata(rs.getString("COLTURA_SPECIALIZZATA"));
        storicoUnitaArboreaVO.setAnnoPrimaProduzione(rs.getString("ANNO_PRIMA_PRODUZIONE"));
        storicoUnitaArboreaVO.setVigna(rs.getString("VIGNA"));
        storicoUnitaArboreaVO.setIdVigna(checkLongNull(rs.getString("ID_VIGNA")));
        storicoUnitaArboreaVO.setIdMenzioneGeografica(checkLongNull(rs.getString("ID_MENZIONE_GEOGRAFICA")));
        storicoUnitaArboreaVO.setEtichetta(rs.getString("ETICHETTA"));
        storicoUnitaArboreaVO.setIdGenereIscrizione(checkLongNull(rs.getString("ID_GENERE_ISCRIZIONE")));
        storicoUnitaArboreaVO.setFlagImproduttiva(rs.getString("FLAG_IMPRODUTTIVA"));
        storicoUnitaArboreaVO.setPercentualeFallanza(rs.getBigDecimal("PERCENTUALE_FALLANZA"));
        storicoUnitaArboreaVO.setStatoUnitaArborea(rs.getString("STATO_UNITA_ARBOREA"));
        storicoUnitaArboreaVO.setDataImpianto(rs.getTimestamp("DATA_IMPIANTO"));
        storicoUnitaArboreaVO.setDataPrimaProduzione(rs.getTimestamp("DATA_PRIMA_PRODUZIONE"));
        storicoUnitaArboreaVO.setIdTipoInterventoViticolo(checkLongNull(rs.getString("ID_TIPO_INTERVENTO_VITICOLO")));
        storicoUnitaArboreaVO.setDataIntervento(rs.getTimestamp("DATA_INTERVENTO"));
        storicoUnitaArboreaVO.setDataSovrainnesto(rs.getTimestamp("DATA_SOVRAINNESTO"));
        
        storicoUnitaArboreaVO.setIdFiloSostegno(checkLongNull(rs.getString("ID_FILO_SOSTEGNO")));
        storicoUnitaArboreaVO.setIdPaloTestata(checkLongNull(rs.getString("ID_PALO_TESTATA")));
        storicoUnitaArboreaVO.setIdPaloTessitura(checkLongNull(rs.getString("ID_PALO_TESSITURA")));
        storicoUnitaArboreaVO.setIdAncoraggioUnar(checkLongNull(rs.getString("ID_ANCORAGGIO_UNAR")));
        storicoUnitaArboreaVO.setIdStatoColtivazioneUnar(checkLongNull(rs.getString("ID_STATO_COLTIVAZIONE_UNAR")));
        storicoUnitaArboreaVO.setDistanzaPali(checkLongNull(rs.getString("DISTANZA_PALI")));
        storicoUnitaArboreaVO.setAltitudineSlm(checkLongNull(rs.getString("ALTITUDINE_SLM")));
        storicoUnitaArboreaVO.setAreaServizio(rs.getBigDecimal("AREA_SERVIZIO"));        
        
        storicoUnitaArboreaVO.setPercentualePendenzaMedia(rs.getBigDecimal("PERCENTUALE_PENDENZA_MEDIA"));
        storicoUnitaArboreaVO.setGradiPendenzaMedia(rs.getBigDecimal("GRADI_PENDENZA_MEDIA"));
        storicoUnitaArboreaVO.setGradiEsposizioneMedia(rs.getBigDecimal("GRADI_ESPOSIZIONE_MEDIA"));
        storicoUnitaArboreaVO.setMetriAltitudineMedia(checkIntegerNull(rs.getString("METRI_ALTITUDINE_MEDIA")));
        storicoUnitaArboreaVO.setIdUnitaArboreaMadre(checkLongNull(rs.getString("ID_UNITA_ARBOREA_MADRE")));
        storicoUnitaArboreaVO.setIdCatalogoMatrice(checkLongNull(rs.getString("ID_CATALOGO_MATRICE")));
        
        elencoUnitaArboree.add(storicoUnitaArboreaVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getListStoricoUnitaArboreaByVidSoricoUnitaArborea in StoricoUnitaArboreaDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getListStoricoUnitaArboreaByVidSoricoUnitaArborea in StoricoUnitaArboreaDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getListStoricoUnitaArboreaByVidSoricoUnitaArborea in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getListStoricoUnitaArboreaByVidSoricoUnitaArborea in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListStoricoUnitaArboreaByVidSoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
    if(elencoUnitaArboree.size() == 0) 
    {
      return null;
    }
    else 
    {
      return (StoricoUnitaArboreaVO[])elencoUnitaArboree.toArray(new StoricoUnitaArboreaVO[elencoUnitaArboree.size()]);
    }
  }
	
	/**
	 * Metodo che mi permette di estrarre l'elenco delle unità arboree relative all'azienda
	 * agricola(stato attuale o piano di riferimento).
	 * 
	 * @param idAzienda
	 * @param idPianoRiferimento
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
	 * @throws DataAccessException
	 */
	public StoricoParticellaVO[] getListStoricoUnitaArboreaByIdAzienda(Long idAzienda, Long idPianoRiferimento, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListStoricoUnitaArboreaByIdAzienda method in StoricoUnitaArboreaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<StoricoParticellaVO> elencoParticelleArboree = new Vector<StoricoParticellaVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListStoricoUnitaArboreaByIdAzienda method in StoricoUnitaArboreaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListStoricoUnitaArboreaByIdAzienda method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");

			String query = " SELECT SP.ID_STORICO_PARTICELLA, " +
					     "        SP.COMUNE, " +
						   "        C.DESCOM, " +
						   "        P.SIGLA_PROVINCIA, " +
						   "        SP.SEZIONE, " +
						   "        SP.FOGLIO, " +
						   "        SP.PARTICELLA, " +
						   "        SP.SUBALTERNO, " +
						   "        SP.SUP_CATASTALE, " +
						   "        PART.FLAG_SCHEDARIO, " +
					     "        SUA.ID_STORICO_UNITA_ARBOREA, " +
						   "        SUA.ID_UNITA_ARBOREA, " +
						   "        SUA.ID_PARTICELLA, " +
						   "        SUA.PROGR_UNAR, " +
						   "        SUA.DATA_INIZIO_VALIDITA, " +
						   "        SUA.DATA_FINE_VALIDITA, " +
						   "        SUA.DATA_LAVORAZIONE, " +
						   "        SUA.ID_TIPOLOGIA_UNAR, " +
						   "        TTU.DESCRIZIONE AS DESC_TIPO_UNAR, " +
						   "        SUA.AREA, " +
						   "        SUA.SESTO_SU_FILA, " +
						   "        SUA.SESTO_TRA_FILE, " +
						   "        SUA.NUM_CEPPI, " +
						   "        SUA.ANNO_IMPIANTO, " +
						   "        SUA.ANNO_REINNESTO, " +
						   "        SUA.ID_FORMA_ALLEVAMENTO, " +
						   "        TFA.DESCRIZIONE AS DESC_FORMA_ALLEVAMENTO, " +
						   "        SUA.ID_IRRIGAZIONE_UNAR, " +
						   "        SUA.ID_COLTIVAZIONE_UNAR, " +
						   "        SUA.CODICE_TIPO_VARIETA, " +
						   "        SUA.PRESENZA_ALTRI_VITIGNI, " +
						   "        SUA.NUMERO_PIANTE_PRODUTTIVO, " +
						   "        SUA.NUMERO_ALTRE_PIANTE, " +
						   "        SUA.CAMPAGNA, " +
						   "        SUA.ID_TIPOLOGIA_VIGNETO, " +
						   "        SUA.TIPO_IMPIANTO, " +
						   "        SUA.NUMERO_CASTAGNI, " +
						   "        SUA.GRUPPO, " +
						   "        SUA.RICADUTA, " +
						   "        SUA.ID_GIACITURA_UNAR, " +
						   "        SUA.ID_ROCCIA_UNAR, " +
						   "        SUA.ID_SCHELETRO_UNAR, " +
						   "        SUA.ID_STATO_VEGETATIVO_UNAR, " +
						   "        SUA.ID_POTATURA_UNAR, " +
						   "        SUA.ID_GIUDIZIO_UNAR, " +
						   "        SUA.SUPPLEMENTARI, " +
						   "        SUA.MECCANIZZABILE, " +
						   "        SUA.DIMENSIONE_CHIOMA, " +
						   "        SUA.ID_ETA_IMPIANTO_UNAR, " +
						   "        SUA.PROVINCIA_CCIAA, " +
						   "        SUA.MATRICOLA_CCIAA, " +
						   "        SUA.CONFERMA_PREC_ISCRIZIONE_ALBO, " +
						   "        SUA.RICHIESTA_NUOVA_ISCR_ALBO, " +
						   "        SUA.CONFERMA_RICH_NUOVA_ISCR_ALBO, " +
						   "        SUA.SUPERFICIE_DA_ISCRIVERE_ALBO, " +
						   "        SUA.ANNO_ISCRIZIONE_ALBO, " +
						   "        SUA.ID_FONTE, " +
						   "        SUA.ID_VARIAZIONE_UNAR, " +
						   "        SUA.NOTE, " +
						   "        SUA.DATA_AGGIORNAMENTO, " +
						   "        SUA.ID_UTENTE_AGGIORNAMENTO, " +
						   "        SUA.ID_VARIETA, " +
						   "        TVAR.DESCRIZIONE AS DESC_VARIETA, " +
						   "        TVAR.CODICE_VARIETA AS COD_VAR, " +
						   "        SUA.ID_UTILIZZO, " +
						   "        TU.CODICE, " +
						   "        TU.DESCRIZIONE AS DESC_TIPO_UTILIZZO, " +
						   "        SUA.PERCENTUALE_VARIETA, " +
						   "        SUA.ID_VINO, " +
						   "        TV.DESCRIZIONE AS DESC_TIPO_VINO, " +
						   "        SUA.DATA_ESECUZIONE, " +
						   "        SUA.RECORD_MODIFICATO, " +
						   "        SUA.ESITO_CONTROLLO, " +
						   "        SUA.ID_TIPOLOGIA_VINO, " +
						   "        TTV.DESCRIZIONE AS DESC_TIPO_VINO " +
						   "        CP.ID_CONDUZIONE_PARTICELLA, " +
						   "        CP.ID_TITOLO_POSSESSO, " +
						   "        TTP.DESCRIZIONE AS DESC_POSSESSO, " +
						   "        CP.SUPERFICIE_CONDOTTA, " +
						   "        PC.SUP_COLT_ARBOREA_SPECIALIZZATA, " +
						   "        SUA.ANNO_PRIMA_PRODUZIONE, " +
               "        SUA.VIGNA, " +
               "        SUA.ID_VIGNA, " +
               "        SUA.ID_MENZIONE_GEOGRAFICA, " +
               "        SUA.ETICHETTA, " +
               "        SUA.ID_GENERE_ISCRIZIONE, " +
               "        SUA.FLAG_IMPRODUTTIVA, " +
               "        SUA.PERCENTUALE_FALLANZA, " +
               "        SUA.DATA_SOVRAINNESTO, " +
               "        SUA.DATA_INTERVENTO, " +
               "        SUA.ID_TIPO_INTERVENTO_VITICOLO, " +
               "        SUA.ID_FILO_SOSTEGNO, " +
               "        SUA.ID_PALO_TESTATA, " +
               "        SUA.ID_PALO_TESSITURA, " +
               "        SUA.ID_ANCORAGGIO_UNAR, " +
               "        SUA.ID_STATO_COLTIVAZIONE_UNAR, " +
               "        SUA.DISTANZA_PALI, " +
               "        SUA.ALTITUDINE_SLM, " +
               "        SUA.AREA_SERVIZIO, " +
               "        SUA.PERCENTUALE_PENDENZA_MEDIA, " +
               "        SUA.GRADI_PENDENZA_MEDIA, " +
               "        SUA.GRADI_ESPOSIZIONE_MEDIA," +
               "        SUA.METRI_ALTITUDINE_MEDIA, " +
               "        SUA.ID_UNITA_ARBOREA_MADRE, " +
               "        SUA.ID_CATALOGO_MATRICE " +
						   " FROM   DB_STORICO_PARTICELLA SP, " +
						   "        COMUNE C, " +
						   "        PROVINCIA P, " +
						   "        DB_PARTICELLA PART, " +
						   "        DB_STORICO_UNITA_ARBOREA SUA, " +
						   "        DB_TIPO_TIPOLOGIA_UNAR TTU, " +
						   "        DB_TIPO_FORMA_ALLEVAMENTO TFA, " + 
						   "        DB_TIPO_VARIETA TVAR, " +
						   "        DB_TIPO_VINO TV, " +
						   "        DB_TIPO_UTILIZZO TU, " +
						   "        DB_UTE U, " +
						   "        DB_CONDUZIONE_PARTICELLA CP, " +
						   "        DB_TIPO_TITOLO_POSSESSO TTP, " +
						   "        DB_PARTICELLA_CERTIFICATA PC, " +
						   "        DB_TIPO_TIPOLOGIA_VINO TTV " +
						   " WHERE  SUA.ID_PARTICELLA = SP.ID_PARTICELLA " +
						   " AND    SP.ID_PARTICELLA = PART.ID_PARTICELLA " +
						   " AND    SP.COMUNE = C.ISTAT_COMUNE " +
						   " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
					     " AND    SUA.ID_TIPOLOGIA_UNAR = TTU.ID_TIPOLOGIA_UNAR(+) " +
			         " AND    SUA.ID_FORMA_ALLEVAMENTO = TFA.ID_FORMA_ALLEVAMENTO(+) " +
			         " AND    SUA.ID_VARIETA = TVAR.ID_VARIETA(+) " +
			         " AND    SUA.ID_VINO = TV.ID_VINO(+) " +
			         " AND    SUA.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
			         " AND    SUA.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO(+) " +
						   " AND    U.ID_AZIENDA = ? " +
						   " AND    SUA.ID_AZIENDA = ? " +
		           " AND    U.ID_UTE = CP.ID_UTE " +
		           " AND    CP.ID_PARTICELLA = SUA.ID_PARTICELLA " +
		           " AND    CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO " +
		           " AND    U.DATA_FINE_ATTIVITA IS NULL " +
		           " AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
		           " AND    SP.DATA_FINE_VALIDITA IS NULL " +
		           " AND    SP.COMUNE = PC.COMUNE(+) " +
		           " AND    NVL(SP.SEZIONE, '-') = NVL(PC.SEZIONE(+), '-') " +
		           " AND    SP.FOGLIO = PC.FOGLIO(+) " +
		           " AND    SP.PARTICELLA = PC.PARTICELLA(+) " +
		           " AND    NVL(SP.SUBALTERNO, '-') = NVL(PC.SUBALTERNO(+), '-') ";
			if(idPianoRiferimento.longValue() < 0) {
				query +=   " AND    SUA.DATA_FINE_VALIDITA IS NULL ";
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

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListStoricoUnitaArboreaByIdAzienda method in StoricoUnitaArboreaDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ORDINAMENTO] in getListStoricoUnitaArboreaByIdAzienda method in StoricoUnitaArboreaDAO: "+ordinamento+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());
			stmt.setLong(2, idAzienda.longValue());

			SolmrLogger.debug(this, "Executing getListStoricoUnitaArboreaByIdAzienda: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
				ParticellaVO particellaVO = new ParticellaVO();
				StoricoUnitaArboreaVO storicoUnitaArboreaVO = new StoricoUnitaArboreaVO();
				ParticellaCertificataVO particellaCertificataVO = new ParticellaCertificataVO();
				ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
				conduzioneParticellaVO.setIdConduzioneParticella(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));
				conduzioneParticellaVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
				conduzioneParticellaVO.setTitoloPossesso(new CodeDescription(new Integer(rs.getInt("ID_TITOLO_POSSESSO")), rs.getString("DESC_POSSESSO")));
				conduzioneParticellaVO.setSuperficieCondotta(rs.getString("SUPERFICIE_CONDOTTA"));
				storicoParticellaVO.setElencoConduzioni((ConduzioneParticellaVO[])new ConduzioneParticellaVO[]{conduzioneParticellaVO});
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
				storicoUnitaArboreaVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
				storicoUnitaArboreaVO.setIdUnitaArborea(new Long(rs.getLong("ID_UNITA_ARBOREA")));
				storicoUnitaArboreaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
				storicoUnitaArboreaVO.setProgrUnar(rs.getString("PROGR_UNAR"));
				storicoUnitaArboreaVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				storicoUnitaArboreaVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				storicoUnitaArboreaVO.setDataLavorazione(rs.getDate("DATA_LAVORAZIONE"));
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_UNAR"))) {
					storicoUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
					TipoTipologiaUnitaArboreaVO tipoTipologiaUnitaArboreaVO = new TipoTipologiaUnitaArboreaVO();
					tipoTipologiaUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
					tipoTipologiaUnitaArboreaVO.setDescrizione(rs.getString("DESC_TIPO_UNAR"));
					storicoUnitaArboreaVO.setTipoTipologiaUnitaArboreaVO(tipoTipologiaUnitaArboreaVO);
				}
				storicoUnitaArboreaVO.setArea(rs.getString("AREA"));
				storicoUnitaArboreaVO.setSestoSuFila(rs.getString("SESTO_SU_FILA"));
				storicoUnitaArboreaVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
				storicoUnitaArboreaVO.setNumCeppi(rs.getString("NUM_CEPPI"));
				storicoUnitaArboreaVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
				storicoUnitaArboreaVO.setAnnoReinnesto(rs.getString("ANNO_REINNESTO"));
				if(Validator.isNotEmpty(rs.getString("ID_FORMA_ALLEVAMENTO"))) {
					storicoUnitaArboreaVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
					TipoFormaAllevamentoVO tipoFormaAllevamentoVO = new TipoFormaAllevamentoVO();
					tipoFormaAllevamentoVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
					tipoFormaAllevamentoVO.setDescrizione(rs.getString("DESC_FORMA_ALLEVAMENTO"));
					storicoUnitaArboreaVO.setTipoFormaAllevamentoVO(tipoFormaAllevamentoVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_IRRIGAZIONE_UNAR"))) {
					storicoUnitaArboreaVO.setIdIrrigazioneUnar(new Long(rs.getLong("ID_IRRIGAZIONE_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_COLTIVAZIONE_UNAR"))) {
					storicoUnitaArboreaVO.setIdColtivazioneUnar(new Long(rs.getLong("ID_COLTIVAZIONE_UNAR")));
				}
				storicoUnitaArboreaVO.setCodiceTipoVarieta(rs.getString("CODICE_TIPO_VARIETA"));
				storicoUnitaArboreaVO.setPresenzaAltriVitigni(rs.getString("PRESENZA_ALTRI_VITIGNI"));
				storicoUnitaArboreaVO.setNumeroPianteProduttivo(rs.getString("NUMERO_PIANTE_PRODUTTIVO"));
				storicoUnitaArboreaVO.setNumeroAltrePiante(rs.getString("NUMERO_ALTRE_PIANTE"));
				storicoUnitaArboreaVO.setCampagna(rs.getString("CAMPAGNA"));
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VIGNETO"))) {
					storicoUnitaArboreaVO.setIdTipologiaVigneto(new Long(rs.getLong("ID_TIPOLOGIA_VIGNETO")));
				}
				storicoUnitaArboreaVO.setTipoImpianto(rs.getString("TIPO_IMPIANTO"));
				storicoUnitaArboreaVO.setNumeroCastagni(rs.getString("NUMERO_CASTAGNI"));
				storicoUnitaArboreaVO.setGruppo(rs.getString("GRUPPO"));
				storicoUnitaArboreaVO.setRicaduta(rs.getString("RICADUTA"));
				if(Validator.isNotEmpty(rs.getString("ID_GIACITURA_UNAR"))) {
					storicoUnitaArboreaVO.setIdGiacituraUnar(new Long(rs.getLong("ID_GIACITURA_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_ROCCIA_UNAR"))) {
					storicoUnitaArboreaVO.setIdRocciaUnar(new Long(rs.getLong("ID_ROCCIA_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_SCHELETRO_UNAR"))) {
					storicoUnitaArboreaVO.setIdScheletroUnar(new Long(rs.getLong("ID_SCHELETRO_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_STATO_VEGETATIVO_UNAR"))) {
					storicoUnitaArboreaVO.setIdStatoVegetativoUnar(new Long(rs.getLong("ID_STATO_VEGETATIVO_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_POTATURA_UNAR"))) {
					storicoUnitaArboreaVO.setIdPotaturaUnar(new Long(rs.getLong("ID_POTATURA_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_GIUDIZIO_UNAR"))) {
					storicoUnitaArboreaVO.setIdGiudizioUnar(new Long(rs.getLong("ID_GIUDIZIO_UNAR")));
				}
				storicoUnitaArboreaVO.setSupplementari(rs.getString("SUPPLEMENTARI"));
				storicoUnitaArboreaVO.setMeccanizzabile(rs.getString("MECCANIZZABILE"));
				storicoUnitaArboreaVO.setDimensioneChioma(rs.getString("DIMENSIONE_CHIOMA"));
				if(Validator.isNotEmpty(rs.getString("ID_ETA_IMPIANTO_UNAR"))) {
					storicoUnitaArboreaVO.setIdEtaImpiantoUnar(new Long(rs.getLong("ID_ETA_IMPIANTO_UNAR")));
				}
				storicoUnitaArboreaVO.setProvinciaCCIAA(rs.getString("PROVINCIA_CCIAA"));
				storicoUnitaArboreaVO.setMatricolaCCIAA(rs.getString("MATRICOLA_CCIAA"));
				storicoUnitaArboreaVO.setConfermaPrecIscrizioneAlbo(rs.getString("CONFERMA_PREC_ISCRIZIONE_ALBO"));
				storicoUnitaArboreaVO.setRichiestaNuovaIscrAlbo(rs.getString("RICHIESTA_NUOVA_ISCR_ALBO"));
				storicoUnitaArboreaVO.setConfermaRichNuovaIscrAlbo(rs.getString("CONFERMA_RICH_NUOVA_ISCR_ALBO"));
				storicoUnitaArboreaVO.setSuperficieDaIscrivereAlbo(rs.getString("SUPERFICIE_DA_ISCRIVERE_ALBO"));
				storicoUnitaArboreaVO.setAnnoIscrizioneAlbo(rs.getString("ANNO_ISCRIZIONE_ALBO"));
				storicoUnitaArboreaVO.setIdFonte(new Long(rs.getLong("ID_FONTE")));
				if(Validator.isNotEmpty(rs.getString("ID_VARIAZIONE_UNAR"))) {
					storicoUnitaArboreaVO.setIdVariazioneUnar(new Long(rs.getLong("ID_VARIAZIONE_UNAR")));
				}
				storicoUnitaArboreaVO.setNote(rs.getString("NOTE"));
				storicoUnitaArboreaVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
				storicoUnitaArboreaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) {
					storicoUnitaArboreaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
					TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
					tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
					tipoVarietaVO.setDescrizione(rs.getString("DESC_VARIETA"));
					tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VAR"));
					storicoUnitaArboreaVO.setTipoVarietaVO(tipoVarietaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO"))) {
					storicoUnitaArboreaVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
					TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
					tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
					tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
					tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTILIZZO"));
					storicoUnitaArboreaVO.setTipoUtilizzoVO(tipoUtilizzoVO);
				}
				storicoUnitaArboreaVO.setPercentualeVarieta(rs.getString("PERCENTUALE_VARIETA"));
				if(Validator.isNotEmpty(rs.getString("ID_VINO"))) {
					storicoUnitaArboreaVO.setIdVino(new Long(rs.getLong("ID_VINO")));
					TipoVinoVO tipoVinoVO = new TipoVinoVO();
					tipoVinoVO.setIdVino(new Long(rs.getLong("ID_VINO")));
					tipoVinoVO.setDescrizione(rs.getString("DESC_TIPO_VINO"));
					storicoUnitaArboreaVO.setTipoVinoVO(tipoVinoVO);
				}
				if(rs.getDate("DATA_ESECUZIONE") != null) {
					storicoUnitaArboreaVO.setDataEsecuzione(new Timestamp(rs.getDate("DATA_ESECUZIONE").getTime()));
				}
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VINO"))) {
					storicoUnitaArboreaVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
					TipoTipologiaVinoVO tipoTipologiaVinoVO = new TipoTipologiaVinoVO();
					tipoTipologiaVinoVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
					tipoTipologiaVinoVO.setDescrizione(rs.getString("DESC_TIPO_VINO"));
					storicoUnitaArboreaVO.setTipoTipologiaVinoVO(tipoTipologiaVinoVO);
				}
				storicoUnitaArboreaVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
				storicoUnitaArboreaVO.setRecordModificato(rs.getString("RECORD_MODIFICATO"));
				storicoParticellaVO.setStoricoUnitaArboreaVO(storicoUnitaArboreaVO);
				particellaCertificataVO.setSupColtArboreaSpecializzata(rs.getString("SUP_COLT_ARBOREA_SPECIALIZZATA"));
				storicoParticellaVO.setParticellaCertificataVO(particellaCertificataVO);
				
				
				storicoUnitaArboreaVO.setAnnoPrimaProduzione(rs.getString("ANNO_PRIMA_PRODUZIONE"));
        storicoUnitaArboreaVO.setVigna(rs.getString("VIGNA"));
        storicoUnitaArboreaVO.setIdVigna(checkLongNull(rs.getString("ID_VIGNA")));
        storicoUnitaArboreaVO.setIdMenzioneGeografica(checkLongNull(rs.getString("ID_MENZIONE_GEOGRAFICA")));
        storicoUnitaArboreaVO.setEtichetta(rs.getString("ETICHETTA"));
        storicoUnitaArboreaVO.setIdGenereIscrizione(checkLongNull(rs.getString("ID_GENERE_ISCRIZIONE")));
        storicoUnitaArboreaVO.setFlagImproduttiva(rs.getString("FLAG_IMPRODUTTIVA"));
        storicoUnitaArboreaVO.setPercentualeFallanza(rs.getBigDecimal("PERCENTUALE_FALLANZA"));
        storicoUnitaArboreaVO.setIdTipoInterventoViticolo(checkLongNull(rs.getString("ID_TIPO_INTERVENTO_VITICOLO")));
        storicoUnitaArboreaVO.setDataIntervento(rs.getTimestamp("DATA_INTERVENTO"));
        storicoUnitaArboreaVO.setDataSovrainnesto(rs.getTimestamp("DATA_SOVRAINNESTO"));
        
        storicoUnitaArboreaVO.setIdFiloSostegno(checkLongNull(rs.getString("ID_FILO_SOSTEGNO")));
        storicoUnitaArboreaVO.setIdPaloTestata(checkLongNull(rs.getString("ID_PALO_TESTATA")));
        storicoUnitaArboreaVO.setIdPaloTessitura(checkLongNull(rs.getString("ID_PALO_TESSITURA")));
        storicoUnitaArboreaVO.setIdAncoraggioUnar(checkLongNull(rs.getString("ID_ANCORAGGIO_UNAR")));
        storicoUnitaArboreaVO.setIdStatoColtivazioneUnar(checkLongNull(rs.getString("ID_STATO_COLTIVAZIONE_UNAR")));
        storicoUnitaArboreaVO.setDistanzaPali(checkLongNull(rs.getString("DISTANZA_PALI")));
        storicoUnitaArboreaVO.setAltitudineSlm(checkLongNull(rs.getString("ALTITUDINE_SLM")));
        storicoUnitaArboreaVO.setAreaServizio(rs.getBigDecimal("AREA_SERVIZIO"));
        
        storicoUnitaArboreaVO.setPercentualePendenzaMedia(rs.getBigDecimal("PERCENTUALE_PENDENZA_MEDIA"));
        storicoUnitaArboreaVO.setGradiPendenzaMedia(rs.getBigDecimal("GRADI_PENDENZA_MEDIA"));
        storicoUnitaArboreaVO.setGradiEsposizioneMedia(rs.getBigDecimal("GRADI_ESPOSIZIONE_MEDIA"));
        storicoUnitaArboreaVO.setMetriAltitudineMedia(checkIntegerNull(rs.getString("METRI_ALTITUDINE_MEDIA")));
        storicoUnitaArboreaVO.setIdUnitaArboreaMadre(checkLongNull(rs.getString("ID_UNITA_ARBOREA_MADRE")));
        storicoUnitaArboreaVO.setIdCatalogoMatrice(checkLongNull(rs.getString("ID_CATALOGO_MATRICE")));
				
				elencoParticelleArboree.add(storicoParticellaVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListStoricoUnitaArboreaByIdAzienda in StoricoUnitaArboreaDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListStoricoUnitaArboreaByIdAzienda in StoricoUnitaArboreaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListStoricoUnitaArboreaByIdAzienda in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListStoricoUnitaArboreaByIdAzienda in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListStoricoUnitaArboreaByIdAzienda method in StoricoUnitaArboreaDAO\n");
		if(elencoParticelleArboree.size() == 0) {
			return (StoricoParticellaVO[])elencoParticelleArboree.toArray(new StoricoParticellaVO[0]);
		}
		else {
			return (StoricoParticellaVO[])elencoParticelleArboree.toArray(new StoricoParticellaVO[elencoParticelleArboree.size()]);
		}
	}
	
	/**
	 * Metodo per recuperare la max data esecuzione controlli relativa alle unità
	 * arboree
	 * 
	 * @param idAzienda
	 * @return java.lang.String
	 * @throws DataAccessException
	 */
	public java.lang.String getMaxDataEsecuzioneControlliUnitaArborea(Long idAzienda) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getMaxDataEsecuzioneControlliUnitaArborea method in StoricoUnitaArboreaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		java.lang.String dataEsecuzioneControlli = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in getMaxDataEsecuzioneControlliUnitaArborea method in StoricoUnitaArboreaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getMaxDataEsecuzioneControlliUnitaArborea method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");

			String query = " SELECT MAX(SUA.DATA_ESECUZIONE) AS DATA_ESECUZIONE" +
                           " FROM   DB_CONDUZIONE_PARTICELLA CP, " +
                           "        DB_UTE U, " +
                           "        DB_STORICO_UNITA_ARBOREA SUA " +
                           " WHERE  U.ID_AZIENDA = ? " +
                           " AND    CP.ID_UTE = U.ID_UTE " +
                           " AND    CP.ID_PARTICELLA = SUA.ID_PARTICELLA ";

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getMaxDataEsecuzioneControlliUnitaArborea method in StoricoUnitaArboreaDAO: "+idAzienda+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());

			SolmrLogger.debug(this, "Executing getMaxDataEsecuzioneControlliUnitaArborea: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) {
				dataEsecuzioneControlli = rs.getString("DATA_ESECUZIONE");
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getMaxDataEsecuzioneControlliUnitaArborea in StoricoUnitaArboreaDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getMaxDataEsecuzioneControlliUnitaArborea in StoricoUnitaArboreaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getMaxDataEsecuzioneControlliUnitaArborea in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getMaxDataEsecuzioneControlliUnitaArborea in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getMaxDataEsecuzioneControlliUnitaArborea method in StoricoUnitaArboreaDAO\n");
		return dataEsecuzioneControlli;
	}
	
	/**
	 * Metodo che mi permette di recuperare l'unita arborea e i dati della particella
	 * in funzione della chiave primaria
	 * 
	 * @param idStoricoUnitaArborea
	 * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO
	 * @throws DataAccessException
	 */
	public StoricoParticellaVO findStoricoParticellaArborea(Long idStoricoUnitaArborea) throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating findStoricoParticellaArborea method in StoricoUnitaArboreaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		StoricoParticellaVO storicoParticellaVO = null;

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in findStoricoParticellaArborea method in StoricoUnitaArboreaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in findStoricoParticellaArborea method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");
			
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
					     "        SUA.ID_STORICO_UNITA_ARBOREA, " +
						   "        SUA.ID_UNITA_ARBOREA, " +
						   "        SUA.ID_PARTICELLA, " +
						   "        SUA.PROGR_UNAR, " +
						   "        SUA.DATA_INIZIO_VALIDITA, " +
						   "        SUA.DATA_FINE_VALIDITA, " +
						   "        SUA.DATA_LAVORAZIONE, " +
						   "        SUA.ID_TIPOLOGIA_UNAR, " +
						   "        TTU.DESCRIZIONE AS DESC_TIPO_UNAR, " +
						   "        SUA.AREA, " +
						   "        SUA.SESTO_SU_FILA, " +
						   "        SUA.SESTO_TRA_FILE, " +
						   "        SUA.NUM_CEPPI, " +
						   "        SUA.ANNO_IMPIANTO, " +
						   "        SUA.DATA_IMPIANTO, " +
						   "        SUA.ANNO_REINNESTO, " +
						   "        SUA.ID_FORMA_ALLEVAMENTO, " +
						   "        TFA.DESCRIZIONE AS DESC_FORMA_ALLEVAMENTO, " +
						   "        SUA.ID_IRRIGAZIONE_UNAR, " +
						   "        TIU.DESCRIZIONE AS DESC_IRRIGAZIONE, " +
						   "        SUA.ID_COLTIVAZIONE_UNAR, " +
						   "        TCU.DESCRIZIONE AS DESC_COLT_UNAR, " +
						   "        SUA.CODICE_TIPO_VARIETA, " +
						   "        SUA.PRESENZA_ALTRI_VITIGNI, " +
						   "        SUA.NUMERO_PIANTE_PRODUTTIVO, " +
						   "        SUA.NUMERO_ALTRE_PIANTE, " +
						   "        SUA.CAMPAGNA, " +
						   "        SUA.ID_TIPOLOGIA_VIGNETO, " +
						   "        SUA.TIPO_IMPIANTO, " +
						   "        SUA.NUMERO_CASTAGNI, " +
						   "        SUA.GRUPPO, " +
						   "        SUA.RICADUTA, " +
						   "        SUA.ID_GIACITURA_UNAR, " +
						   "        SUA.ID_ROCCIA_UNAR, " +
						   "        SUA.ID_SCHELETRO_UNAR, " +
						   "        SUA.ID_STATO_VEGETATIVO_UNAR, " +
						   "        SUA.ID_POTATURA_UNAR, " +
						   "        SUA.ID_GIUDIZIO_UNAR, " +
						   "        SUA.SUPPLEMENTARI, " +
						   "        SUA.MECCANIZZABILE, " +
						   "        SUA.DIMENSIONE_CHIOMA, " +
						   "        SUA.ID_ETA_IMPIANTO_UNAR, " +
						   "        SUA.PROVINCIA_CCIAA, " +
						   "        SUA.MATRICOLA_CCIAA, " +
						   "        SUA.CONFERMA_PREC_ISCRIZIONE_ALBO, " +
						   "        SUA.RICHIESTA_NUOVA_ISCR_ALBO, " +
						   "        SUA.CONFERMA_RICH_NUOVA_ISCR_ALBO, " +
						   "        SUA.SUPERFICIE_DA_ISCRIVERE_ALBO, " +
						   "        SUA.ANNO_ISCRIZIONE_ALBO, " +
						   "        SUA.DATA_ISCRIZIONE_ALBO, " +
						   "        SUA.ID_FONTE, " +
						   "        TF.DESCRIZIONE AS DESC_FONTE, " +
						   "        SUA.ID_VARIAZIONE_UNAR, " +
						   "        SUA.NOTE, " +
						   "        SUA.DATA_AGGIORNAMENTO, " +
						   "        SUA.ID_UTENTE_AGGIORNAMENTO, " +
						   "        RCM.ID_UTILIZZO, " +
						   "        TU.CODICE, " +
						   "        TU.DESCRIZIONE AS DESC_TIPO_UTILIZZO, " +
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
               "        TVAR.DESCRIZIONE AS DESC_VARIETA, " +
               "        TVAR.CODICE_VARIETA AS COD_VAR, " +
						   "        SUA.PERCENTUALE_VARIETA, " +
						   "        SUA.ID_VINO, " +
						   "        TV.DESCRIZIONE AS DESC_TIPO_VINO, " +
						   "        SUA.DATA_ESECUZIONE, " +
						   "        SUA.RECORD_MODIFICATO, " +
						   "        SUA.ESITO_CONTROLLO, " +
						   "        PC.ID_PARTICELLA_CERTIFICATA, "+
               "        PC.SUP_COLT_ARBOREA_SPECIALIZZATA, " +
               "        PC.SUP_GRAFICA, " +
               "        PC.SUP_USO_GRAFICA, " +
						   "        SUA.ID_CAUSALE_MODIFICA, " +
						   "        TCM.DESCRIZIONE AS CAUSA_MOD, " +
						   "        TCM.ALTRO_PROCEDIMENTO, " +
						   "        SUA.ID_AZIENDA, " +
						   "        SUA.ID_CESSAZIONE_UNAR, " +
						   "        TCEUNAR.DESCRIZIONE AS DESC_CESS_UNAR, " +
						   "        SUA.ID_TIPOLOGIA_VINO, " +
						   "        TTV.DESCRIZIONE AS DESC_TIPO_TIPOLOGIA_VINO, " +
						   "        TTV.VINO_DOC, " +
						   "        TTV.CODICE_MIPAF, " +
						   "        TTV.RESA, " +
						   "        TTV.FLAG_GESTIONE_VIGNA, " +
						   "        TTV.FLAG_GESTIONE_ETICHETTA, " +
						   "        TTV.DENSITA_MIN_CEPPI_HA, " +
						   "        SUA.ANNO_RIFERIMENTO, " +
						   "        SUA.COLTURA_SPECIALIZZATA, " +
						   "        SUA.STATO_UNITA_ARBOREA, " +
						   "        SUA.ANNO_PRIMA_PRODUZIONE, " +
						   "        SUA.DATA_PRIMA_PRODUZIONE, " +
						   "        SUA.VIGNA, " +
						   "        SUA.ID_VIGNA, " +
						   "        SUA.ID_MENZIONE_GEOGRAFICA, " +
						   "        SUA.ETICHETTA, " +
						   "        SUA.ID_GENERE_ISCRIZIONE, " +
						   "        SUA.FLAG_IMPRODUTTIVA, " +
						   "        SUA.PERCENTUALE_FALLANZA, " +
						   "        UA.DATA_CONSOLIDAMENTO_GIS, " +
						   "        TGI.DESCRIZIONE AS DESC_GENERE_ISCRIZIONE, " +
						   "        TGI.FLAG_DEFINITIVA, " +
						   "        NVL(PCK_SMRGAA_LIBRERIA.SelTotSupElegByPartEVetCat(PC.ID_PARTICELLA_CERTIFICATA, SUA.ID_CATALOGO_MATRICE),0) AS SUPERFICIE_ELEG, " +
						   "        SUA.DATA_SOVRAINNESTO, " +
						   "        SUA.DATA_INTERVENTO, " +
						   "        SUA.ID_TIPO_INTERVENTO_VITICOLO, " +
						   "        TIV.DESCRIZIONE AS DESC_INTERVENTO, " +
						   "        SUA.ID_FILO_SOSTEGNO, " +
               "        SUA.ID_PALO_TESTATA, " +
               "        SUA.ID_PALO_TESSITURA, " +
               "        SUA.ID_ANCORAGGIO_UNAR, " +
               "        SUA.ID_STATO_COLTIVAZIONE_UNAR, " +
               "        SUA.DISTANZA_PALI, " +
               "        SUA.ALTITUDINE_SLM, " +
               "        SUA.AREA_SERVIZIO, " +
               "        SUA.PERCENTUALE_PENDENZA_MEDIA, " +
               "        SUA.GRADI_PENDENZA_MEDIA, " +
               "        SUA.GRADI_ESPOSIZIONE_MEDIA," +
               "        SUA.METRI_ALTITUDINE_MEDIA, " +
               "        SUA.ID_UNITA_ARBOREA_MADRE, " +
               "        SUA.ID_CATALOGO_MATRICE " +
						   " FROM   DB_STORICO_PARTICELLA SP, " +
						   "        COMUNE C, " +
						   "        PROVINCIA P, " +
						   "        DB_STORICO_UNITA_ARBOREA SUA, " +
						   "        DB_R_CATALOGO_MATRICE RCM, " +
						   "        DB_UNITA_ARBOREA UA, " +
						   "        DB_TIPO_TIPOLOGIA_UNAR TTU, " +
						   "        DB_TIPO_FORMA_ALLEVAMENTO TFA, " + 
						   "        DB_TIPO_VINO TV, " +
						   "        DB_TIPO_UTILIZZO TU, " +
						   "        DB_TIPO_DESTINAZIONE TDE, " +
						   "        DB_TIPO_DETTAGLIO_USO TDU, " +
						   "        DB_TIPO_QUALITA_USO TQU, " +
						   "        DB_TIPO_VARIETA TVAR, " +
						   "        DB_TIPO_FONTE TF, " +
						   "        DB_TIPO_IRRIGAZIONE_UNAR TIU, " +
						   "        DB_TIPO_COLTIVAZIONE_UNAR TCU, " +
						   "        DB_PARTICELLA_CERTIFICATA PC, " +
						   "        DB_TIPO_CAUSALE_MODIFICA TCM, " +
						   "        DB_TIPO_CESSAZIONE_UNAR TCEUNAR, " +
						   "        DB_TIPO_TIPOLOGIA_VINO TTV, " +
						   "        DB_TIPO_GENERE_ISCRIZIONE TGI, " +
						   "        DB_TIPO_INTERVENTO_VITICOLO TIV " +
						   " WHERE  SUA.ID_STORICO_UNITA_ARBOREA = ? " +
						   " AND    SUA.ID_PARTICELLA = SP.ID_PARTICELLA " +
						   " AND    SUA.ID_UNITA_ARBOREA = UA.ID_UNITA_ARBOREA " +
						   " AND    SP.COMUNE = C.ISTAT_COMUNE " +
						   " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
					     " AND    SUA.ID_TIPOLOGIA_UNAR = TTU.ID_TIPOLOGIA_UNAR(+) " +
			         " AND    SUA.ID_FORMA_ALLEVAMENTO = TFA.ID_FORMA_ALLEVAMENTO(+) " +
			         " AND    SUA.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE(+) " +
			         " AND    RCM.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
			         " AND    RCM.ID_TIPO_DESTINAZIONE = TDE.ID_TIPO_DESTINAZIONE(+) " +
			         " AND    RCM.ID_TIPO_DETTAGLIO_USO = TDU.ID_TIPO_DETTAGLIO_USO(+) " +
			         " AND    RCM.ID_TIPO_QUALITA_USO = TQU.ID_TIPO_QUALITA_USO(+) " +
			         " AND    RCM.ID_VARIETA = TVAR.ID_VARIETA(+) " +
			         " AND    SUA.ID_VINO = TV.ID_VINO(+) " +
		           " AND    SP.DATA_FINE_VALIDITA IS NULL " +
		           " AND    SUA.ID_FONTE = TF.ID_FONTE(+) " +
		           " AND    SUA.ID_COLTIVAZIONE_UNAR = TCU.ID_COLTIVAZIONE_UNAR(+) " +
		           " AND    SUA.ID_IRRIGAZIONE_UNAR = TIU.ID_IRRIGAZIONE_UNAR(+) " +
		           " AND    SP.COMUNE = PC.COMUNE(+) " +
		           " AND    NVL(SP.SEZIONE, '-') = NVL(PC.SEZIONE(+), '-') " +
		           " AND    SP.FOGLIO = PC.FOGLIO(+) " +
		           " AND    SP.PARTICELLA = PC.PARTICELLA(+) " +
		           " AND    NVL(SP.SUBALTERNO, '-') = NVL(PC.SUBALTERNO(+), '-') " +
		           " AND    PC.DATA_FINE_VALIDITA(+) IS NULL " +
		           " AND    SUA.ID_CAUSALE_MODIFICA = TCM.ID_CAUSALE_MODIFICA " +
		           " AND    SUA.ID_CESSAZIONE_UNAR = TCEUNAR.ID_CESSAZIONE_UNAR(+) " +
		           " AND    SUA.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO(+) " +
			         " AND    SUA.ID_GENERE_ISCRIZIONE = TGI.ID_GENERE_ISCRIZIONE(+) " +
			         " AND    SUA.ID_TIPO_INTERVENTO_VITICOLO = TIV.ID_TIPO_INTERVENTO_VITICOLO(+) ";
			
			

			SolmrLogger.debug(this, "Value of parameter 1 [ID_STORICO_UNITA_ARBOREA] in findStoricoParticellaArborea method in StoricoUnitaArboreaDAO: "+idStoricoUnitaArborea+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idStoricoUnitaArborea.longValue());

			SolmrLogger.debug(this, "Executing findStoricoParticellaArborea: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) 
			{
				storicoParticellaVO = new StoricoParticellaVO();
				StoricoUnitaArboreaVO storicoUnitaArboreaVO = new StoricoUnitaArboreaVO();
				ParticellaCertificataVO particellaCertificataVO = new ParticellaCertificataVO();
				TipoCausaleModificaVO tipoCausaleModificaVO = new TipoCausaleModificaVO();
				storicoParticellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
				storicoParticellaVO.setIdParticella(new Long(rs.getLong("ID_PARTICEL")));
				storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
	    	ComuneVO comuneVO = new ComuneVO();
	    	comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
	    	comuneVO.setDescom(rs.getString("DESCOM"));
	    	comuneVO.setIstatComune(rs.getString("COMUNE"));
	    	ProvinciaVO provinciaVO = new ProvinciaVO();
	    	provinciaVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));
	    	comuneVO.setProvinciaVO(provinciaVO);
	    	storicoParticellaVO.setComuneParticellaVO(comuneVO);
	    	storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
	    	storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
	    	storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
	    	storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
	    	storicoParticellaVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
	    	storicoParticellaVO.setSuperficieGrafica(rs.getString("SUPERFICIE_GRAFICA"));
				storicoUnitaArboreaVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
				storicoUnitaArboreaVO.setIdUnitaArborea(new Long(rs.getLong("ID_UNITA_ARBOREA")));
				storicoUnitaArboreaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
				storicoUnitaArboreaVO.setProgrUnar(rs.getString("PROGR_UNAR"));
				storicoUnitaArboreaVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
				storicoUnitaArboreaVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
				storicoUnitaArboreaVO.setDataLavorazione(rs.getDate("DATA_LAVORAZIONE"));
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_UNAR"))) 
				{
					storicoUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
					TipoTipologiaUnitaArboreaVO tipoTipologiaUnitaArboreaVO = new TipoTipologiaUnitaArboreaVO();
					tipoTipologiaUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
					tipoTipologiaUnitaArboreaVO.setDescrizione(rs.getString("DESC_TIPO_UNAR"));
					storicoUnitaArboreaVO.setTipoTipologiaUnitaArboreaVO(tipoTipologiaUnitaArboreaVO);
				}
				storicoUnitaArboreaVO.setArea(rs.getString("AREA"));
				storicoUnitaArboreaVO.setSestoSuFila(rs.getString("SESTO_SU_FILA"));
				storicoUnitaArboreaVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
				storicoUnitaArboreaVO.setNumCeppi(rs.getString("NUM_CEPPI"));
				storicoUnitaArboreaVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
				storicoUnitaArboreaVO.setDataImpianto(rs.getDate("DATA_IMPIANTO"));
				storicoUnitaArboreaVO.setAnnoReinnesto(rs.getString("ANNO_REINNESTO"));
				if(Validator.isNotEmpty(rs.getString("ID_FORMA_ALLEVAMENTO"))) 
				{
					storicoUnitaArboreaVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
					TipoFormaAllevamentoVO tipoFormaAllevamentoVO = new TipoFormaAllevamentoVO();
					tipoFormaAllevamentoVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
					tipoFormaAllevamentoVO.setDescrizione(rs.getString("DESC_FORMA_ALLEVAMENTO"));
					storicoUnitaArboreaVO.setTipoFormaAllevamentoVO(tipoFormaAllevamentoVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_IRRIGAZIONE_UNAR"))) 
				{
					storicoUnitaArboreaVO.setIdIrrigazioneUnar(new Long(rs.getLong("ID_IRRIGAZIONE_UNAR")));
					TipoIrrigazioneUnitaArboreaVO tipoIrrigazioneUnitaArboreaVO = new TipoIrrigazioneUnitaArboreaVO();
					tipoIrrigazioneUnitaArboreaVO.setIdIrrigazioneUnar(new Long(rs.getLong("ID_IRRIGAZIONE_UNAR")));
					tipoIrrigazioneUnitaArboreaVO.setDescrizione(rs.getString("DESC_IRRIGAZIONE"));
					storicoUnitaArboreaVO.setTipoIrrigazioneUnitaArboreaVO(tipoIrrigazioneUnitaArboreaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_COLTIVAZIONE_UNAR"))) 
				{
					storicoUnitaArboreaVO.setIdColtivazioneUnar(new Long(rs.getLong("ID_COLTIVAZIONE_UNAR")));
					TipoColtivazioneUnitaArboreaVO tipoColtivazioneUnitaArboreaVO = new TipoColtivazioneUnitaArboreaVO();
					tipoColtivazioneUnitaArboreaVO.setIdColtivazioneUnar(new Long(rs.getLong("ID_COLTIVAZIONE_UNAR")));
					tipoColtivazioneUnitaArboreaVO.setDescrizione(rs.getString("DESC_COLT_UNAR"));
					storicoUnitaArboreaVO.setTipoColtivazioneUnitaArboreaVO(tipoColtivazioneUnitaArboreaVO);
				}
				storicoUnitaArboreaVO.setCodiceTipoVarieta(rs.getString("CODICE_TIPO_VARIETA"));
				storicoUnitaArboreaVO.setPresenzaAltriVitigni(rs.getString("PRESENZA_ALTRI_VITIGNI"));
				storicoUnitaArboreaVO.setNumeroPianteProduttivo(rs.getString("NUMERO_PIANTE_PRODUTTIVO"));
				storicoUnitaArboreaVO.setNumeroAltrePiante(rs.getString("NUMERO_ALTRE_PIANTE"));
				storicoUnitaArboreaVO.setCampagna(rs.getString("CAMPAGNA"));
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VIGNETO"))) 
				{
					storicoUnitaArboreaVO.setIdTipologiaVigneto(new Long(rs.getLong("ID_TIPOLOGIA_VIGNETO")));
				}
				storicoUnitaArboreaVO.setTipoImpianto(rs.getString("TIPO_IMPIANTO"));
				storicoUnitaArboreaVO.setNumeroCastagni(rs.getString("NUMERO_CASTAGNI"));
				storicoUnitaArboreaVO.setGruppo(rs.getString("GRUPPO"));
				storicoUnitaArboreaVO.setRicaduta(rs.getString("RICADUTA"));
				if(Validator.isNotEmpty(rs.getString("ID_GIACITURA_UNAR"))) 
				{
					storicoUnitaArboreaVO.setIdGiacituraUnar(new Long(rs.getLong("ID_GIACITURA_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_ROCCIA_UNAR"))) {
					storicoUnitaArboreaVO.setIdRocciaUnar(new Long(rs.getLong("ID_ROCCIA_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_SCHELETRO_UNAR"))) 
				{
					storicoUnitaArboreaVO.setIdScheletroUnar(new Long(rs.getLong("ID_SCHELETRO_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_STATO_VEGETATIVO_UNAR"))) 
				{
					storicoUnitaArboreaVO.setIdStatoVegetativoUnar(new Long(rs.getLong("ID_STATO_VEGETATIVO_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_POTATURA_UNAR"))) 
				{
					storicoUnitaArboreaVO.setIdPotaturaUnar(new Long(rs.getLong("ID_POTATURA_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_GIUDIZIO_UNAR"))) 
				{
					storicoUnitaArboreaVO.setIdGiudizioUnar(new Long(rs.getLong("ID_GIUDIZIO_UNAR")));
				}
				storicoUnitaArboreaVO.setSupplementari(rs.getString("SUPPLEMENTARI"));
				storicoUnitaArboreaVO.setMeccanizzabile(rs.getString("MECCANIZZABILE"));
				storicoUnitaArboreaVO.setDimensioneChioma(rs.getString("DIMENSIONE_CHIOMA"));
				if(Validator.isNotEmpty(rs.getString("ID_ETA_IMPIANTO_UNAR"))) 
				{
					storicoUnitaArboreaVO.setIdEtaImpiantoUnar(new Long(rs.getLong("ID_ETA_IMPIANTO_UNAR")));
				}
				storicoUnitaArboreaVO.setProvinciaCCIAA(rs.getString("PROVINCIA_CCIAA"));
				storicoUnitaArboreaVO.setMatricolaCCIAA(rs.getString("MATRICOLA_CCIAA"));
				storicoUnitaArboreaVO.setConfermaPrecIscrizioneAlbo(rs.getString("CONFERMA_PREC_ISCRIZIONE_ALBO"));
				storicoUnitaArboreaVO.setRichiestaNuovaIscrAlbo(rs.getString("RICHIESTA_NUOVA_ISCR_ALBO"));
				storicoUnitaArboreaVO.setConfermaRichNuovaIscrAlbo(rs.getString("CONFERMA_RICH_NUOVA_ISCR_ALBO"));
				storicoUnitaArboreaVO.setSuperficieDaIscrivereAlbo(rs.getString("SUPERFICIE_DA_ISCRIVERE_ALBO"));
				storicoUnitaArboreaVO.setAnnoIscrizioneAlbo(rs.getString("ANNO_ISCRIZIONE_ALBO"));
				storicoUnitaArboreaVO.setDataIscrizioneAlbo(rs.getDate("DATA_ISCRIZIONE_ALBO"));
				if(Validator.isNotEmpty(rs.getString("ID_FONTE"))) 
				{
					storicoUnitaArboreaVO.setIdFonte(new Long(rs.getLong("ID_FONTE")));
					CodeDescription tipoFonte = new CodeDescription(new Integer(rs.getInt("ID_FONTE")), rs.getString("DESC_FONTE"));
					storicoUnitaArboreaVO.setTipoFonte(tipoFonte);
				}
				if(Validator.isNotEmpty(rs.getString("ID_VARIAZIONE_UNAR"))) 
				{
					storicoUnitaArboreaVO.setIdVariazioneUnar(new Long(rs.getLong("ID_VARIAZIONE_UNAR")));
				}
				storicoUnitaArboreaVO.setNote(rs.getString("NOTE"));
				storicoUnitaArboreaVO.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO"));
				storicoUnitaArboreaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				
				if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO"))) 
				{
					storicoUnitaArboreaVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
					TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
					tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
					tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
					tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTILIZZO"));
					storicoUnitaArboreaVO.setTipoUtilizzoVO(tipoUtilizzoVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_TIPO_DESTINAZIONE"))) 
        {
          storicoUnitaArboreaVO.setIdTipoDestinazione(new Long(rs.getLong("ID_TIPO_DESTINAZIONE")));
          TipoDestinazioneVO tipoDestinazioneVO = new TipoDestinazioneVO();
          tipoDestinazioneVO.setIdTipoDestinazione(new Long(rs.getLong("ID_TIPO_DESTINAZIONE")));
          tipoDestinazioneVO.setCodiceDestinazione(rs.getString("CODICE_DESTINAZIONE"));
          tipoDestinazioneVO.setDescrizioneDestinazione(rs.getString("DESCRIZIONE_DESTINAZIONE"));
          storicoUnitaArboreaVO.setTipoDestinazioneVO(tipoDestinazioneVO);
        }
				if(Validator.isNotEmpty(rs.getString("ID_TIPO_DETTAGLIO_USO"))) 
        {
          storicoUnitaArboreaVO.setIdTipoDettaglioUso(new Long(rs.getLong("ID_TIPO_DETTAGLIO_USO")));
          TipoDettaglioUsoVO tipoDettaglioUsoVO = new TipoDettaglioUsoVO();
          tipoDettaglioUsoVO.setIdTipoDettaglioUso(new Long(rs.getLong("ID_TIPO_DETTAGLIO_USO")));
          tipoDettaglioUsoVO.setCodiceDettaglioUso(rs.getString("CODICE_DETTAGLIO_USO"));
          tipoDettaglioUsoVO.setDescrizione(rs.getString("DESCRIZIONE_DETTAGLIO_USO"));
          storicoUnitaArboreaVO.setTipoDettaglioUsoVO(tipoDettaglioUsoVO);
        }
				if(Validator.isNotEmpty(rs.getString("ID_TIPO_QUALITA_USO"))) 
        {
          storicoUnitaArboreaVO.setIdTipoQualitaUso(new Long(rs.getLong("ID_TIPO_QUALITA_USO")));
          TipoQualitaUsoVO tipoQualitaUsoVO = new TipoQualitaUsoVO();
          tipoQualitaUsoVO.setIdTipoQualitaUso(new Long(rs.getLong("ID_TIPO_QUALITA_USO")));
          tipoQualitaUsoVO.setCodiceQualitaUso(rs.getString("CODICE_QUALITA_USO"));
          tipoQualitaUsoVO.setDescrizioneQualitaUso(rs.getString("DESCRIZIONE_QUALITA_USO"));
          storicoUnitaArboreaVO.setTipoQualitaUsoVO(tipoQualitaUsoVO);
        }
				if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) 
        {
          storicoUnitaArboreaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
          tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          tipoVarietaVO.setDescrizione(rs.getString("DESC_VARIETA"));
          tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VAR"));
          storicoUnitaArboreaVO.setTipoVarietaVO(tipoVarietaVO);
        }
				storicoUnitaArboreaVO.setPercentualeVarieta(rs.getString("PERCENTUALE_VARIETA"));
				if(Validator.isNotEmpty(rs.getString("ID_VINO"))) 
				{
					storicoUnitaArboreaVO.setIdVino(new Long(rs.getLong("ID_VINO")));
					TipoVinoVO tipoVinoVO = new TipoVinoVO();
					tipoVinoVO.setIdVino(new Long(rs.getLong("ID_VINO")));
					tipoVinoVO.setDescrizione(rs.getString("DESC_TIPO_VINO"));
					storicoUnitaArboreaVO.setTipoVinoVO(tipoVinoVO);
				}
				if(rs.getDate("DATA_ESECUZIONE") != null) 
				{
					storicoUnitaArboreaVO.setDataEsecuzione(new Timestamp(rs.getDate("DATA_ESECUZIONE").getTime()));
				}
				storicoUnitaArboreaVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
				storicoUnitaArboreaVO.setRecordModificato(rs.getString("RECORD_MODIFICATO"));
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
				storicoUnitaArboreaVO.setIdCausaleModifica(new Long(rs.getLong("ID_CAUSALE_MODIFICA")));
				tipoCausaleModificaVO.setIdCausaleModifica(new Long(rs.getLong("ID_CAUSALE_MODIFICA")));
				tipoCausaleModificaVO.setDescrizione(rs.getString("CAUSA_MOD"));
				tipoCausaleModificaVO.setAltroProcedimento(rs.getString("ALTRO_PROCEDIMENTO"));
				storicoUnitaArboreaVO.setTipoCausaleModificaVO(tipoCausaleModificaVO);
				if(Validator.isNotEmpty(rs.getString("ID_AZIENDA"))) {
					storicoUnitaArboreaVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_CESSAZIONE_UNAR"))) {
					storicoUnitaArboreaVO.setIdCessazioneUnar(new Long(rs.getLong("ID_CESSAZIONE_UNAR")));
					TipoCessazioneUnarVO tipoCessazioneUnarVO = new TipoCessazioneUnarVO();
					tipoCessazioneUnarVO.setIdCessazioneUnar(new Long(rs.getLong("ID_CESSAZIONE_UNAR")));
					tipoCessazioneUnarVO.setDescrizione(rs.getString("DESC_CESS_UNAR"));
					storicoUnitaArboreaVO.setTipoCessazioneUnarVO(tipoCessazioneUnarVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VINO"))) {
					storicoUnitaArboreaVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
					TipoTipologiaVinoVO tipoTipologiaVinoVO = new TipoTipologiaVinoVO();
					tipoTipologiaVinoVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
					tipoTipologiaVinoVO.setDescrizione(rs.getString("DESC_TIPO_TIPOLOGIA_VINO"));
					tipoTipologiaVinoVO.setCodiceMipaf(rs.getString("CODICE_MIPAF")); 
					tipoTipologiaVinoVO.setVinoDoc(rs.getString("VINO_DOC"));
					tipoTipologiaVinoVO.setResa(rs.getBigDecimal("RESA"));
					tipoTipologiaVinoVO.setFlagGestioneVigna(rs.getString("FLAG_GESTIONE_VIGNA"));
					tipoTipologiaVinoVO.setFlagGestioneEtichetta(rs.getString("FLAG_GESTIONE_ETICHETTA"));
					tipoTipologiaVinoVO.setDensitaMinCeppiHa(checkLongNull(rs.getString("DENSITA_MIN_CEPPI_HA")));
					storicoUnitaArboreaVO.setTipoTipologiaVinoVO(tipoTipologiaVinoVO);
				}
				storicoUnitaArboreaVO.setAnnoRiferimento(rs.getString("ANNO_RIFERIMENTO"));
				storicoUnitaArboreaVO.setColturaSpecializzata(rs.getString("COLTURA_SPECIALIZZATA"));
				storicoUnitaArboreaVO.setStatoUnitaArborea(rs.getString("STATO_UNITA_ARBOREA"));
				storicoUnitaArboreaVO.setAnnoPrimaProduzione(rs.getString("ANNO_PRIMA_PRODUZIONE"));
				storicoUnitaArboreaVO.setDataPrimaProduzione(rs.getDate("DATA_PRIMA_PRODUZIONE"));
				storicoUnitaArboreaVO.setVigna(rs.getString("VIGNA"));
				storicoUnitaArboreaVO.setIdVigna(checkLongNull(rs.getString("ID_VIGNA")));
				storicoUnitaArboreaVO.setIdMenzioneGeografica(checkLongNull(rs.getString("ID_MENZIONE_GEOGRAFICA")));
				storicoUnitaArboreaVO.setEtichetta(rs.getString("ETICHETTA"));
				if(Validator.isNotEmpty(rs.getString("ID_GENERE_ISCRIZIONE"))) 
				{
				  storicoUnitaArboreaVO.setIdGenereIscrizione(checkLong(rs.getString("ID_GENERE_ISCRIZIONE")));
				  TipoGenereIscrizioneVO tipoGenereIscrizioneVO = new TipoGenereIscrizioneVO();
				  tipoGenereIscrizioneVO.setIdGenereIscrizione(rs.getLong("ID_GENERE_ISCRIZIONE"));
				  tipoGenereIscrizioneVO.setDescrizione(rs.getString("DESC_GENERE_ISCRIZIONE"));
				  tipoGenereIscrizioneVO.setFlagDefinitiva(rs.getString("FLAG_DEFINITIVA"));
				  storicoUnitaArboreaVO.setTipoGenereIscrizioneVO(tipoGenereIscrizioneVO);
				}
				
				storicoUnitaArboreaVO.setFlagImproduttiva(rs.getString("FLAG_IMPRODUTTIVA"));
				storicoUnitaArboreaVO.setPercentualeFallanza(rs.getBigDecimal("PERCENTUALE_FALLANZA"));
				
				storicoUnitaArboreaVO.setDataConsolidamentoGis(rs.getTimestamp("DATA_CONSOLIDAMENTO_GIS"));
				
				if(rs.getString("DESC_INTERVENTO") !=null)
				{
				  storicoUnitaArboreaVO.setIdTipoInterventoViticolo(new Long(rs.getLong("ID_TIPO_INTERVENTO_VITICOLO")));
				  TipoInterventoViticoloVO tipoInterventoViticoloVO = new TipoInterventoViticoloVO();
				  tipoInterventoViticoloVO.setDescrizione(rs.getString("DESC_INTERVENTO"));
				  tipoInterventoViticoloVO.setIdTipoInterventoViticolo(new Long(rs.getLong("ID_TIPO_INTERVENTO_VITICOLO")));
				  storicoUnitaArboreaVO.setTipoInterventoViticoloVO(tipoInterventoViticoloVO);
				}
				storicoUnitaArboreaVO.setDataIntervento(rs.getTimestamp("DATA_INTERVENTO"));
				storicoUnitaArboreaVO.setDataSovrainnesto(rs.getTimestamp("DATA_SOVRAINNESTO"));
				
				storicoUnitaArboreaVO.setIdFiloSostegno(checkLongNull(rs.getString("ID_FILO_SOSTEGNO")));
        storicoUnitaArboreaVO.setIdPaloTestata(checkLongNull(rs.getString("ID_PALO_TESTATA")));
        storicoUnitaArboreaVO.setIdPaloTessitura(checkLongNull(rs.getString("ID_PALO_TESSITURA")));
        storicoUnitaArboreaVO.setIdAncoraggioUnar(checkLongNull(rs.getString("ID_ANCORAGGIO_UNAR")));
        storicoUnitaArboreaVO.setIdStatoColtivazioneUnar(checkLongNull(rs.getString("ID_STATO_COLTIVAZIONE_UNAR")));
        storicoUnitaArboreaVO.setDistanzaPali(checkLongNull(rs.getString("DISTANZA_PALI")));
        storicoUnitaArboreaVO.setAltitudineSlm(checkLongNull(rs.getString("ALTITUDINE_SLM")));
        storicoUnitaArboreaVO.setAreaServizio(rs.getBigDecimal("AREA_SERVIZIO"));
        
        storicoUnitaArboreaVO.setPercentualePendenzaMedia(rs.getBigDecimal("PERCENTUALE_PENDENZA_MEDIA"));
        storicoUnitaArboreaVO.setGradiPendenzaMedia(rs.getBigDecimal("GRADI_PENDENZA_MEDIA"));
        storicoUnitaArboreaVO.setGradiEsposizioneMedia(rs.getBigDecimal("GRADI_ESPOSIZIONE_MEDIA"));
        storicoUnitaArboreaVO.setMetriAltitudineMedia(checkIntegerNull(rs.getString("METRI_ALTITUDINE_MEDIA")));
        storicoUnitaArboreaVO.setIdUnitaArboreaMadre(checkLongNull(rs.getString("ID_UNITA_ARBOREA_MADRE")));
        storicoUnitaArboreaVO.setIdCatalogoMatrice(checkLongNull(rs.getString("ID_CATALOGO_MATRICE")));
				
				
				
				storicoParticellaVO.setStoricoUnitaArboreaVO(storicoUnitaArboreaVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "findStoricoParticellaArborea in StoricoUnitaArboreaDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "findStoricoParticellaArborea in StoricoUnitaArboreaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "findStoricoParticellaArborea in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "findStoricoParticellaArborea in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated findStoricoParticellaArborea method in StoricoUnitaArboreaDAO\n");
		return storicoParticellaVO;
	}
	
	
	public StoricoParticellaVO findStoricoParticellaArboreaBasic(Long idStoricoUnitaArborea) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating findStoricoParticellaArboreaBasic method in StoricoUnitaArboreaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    StoricoParticellaVO storicoParticellaVO = null;

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in findStoricoParticellaArboreaBasic method in StoricoUnitaArboreaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in findStoricoParticellaArboreaBasic method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");
      
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
               "        SUA.ID_STORICO_UNITA_ARBOREA, " +
               "        SUA.ID_UNITA_ARBOREA, " +
               "        SUA.ID_PARTICELLA, " +
               "        SUA.PROGR_UNAR, " +
               "        SUA.ID_TIPOLOGIA_UNAR, " +
               "        TTU.DESCRIZIONE AS DESC_TIPO_UNAR, " +
               "        SUA.AREA, " +
               "        SUA.SESTO_SU_FILA, " +
               "        SUA.SESTO_TRA_FILE, " +
               "        SUA.NUM_CEPPI, " +
               "        SUA.ANNO_IMPIANTO, " +
               "        SUA.DATA_IMPIANTO, " +
               "        SUA.ANNO_ISCRIZIONE_ALBO, " +
               "        SUA.DATA_ISCRIZIONE_ALBO, " +
               "        SUA.ID_VARIETA, " +
               "        TVAR.DESCRIZIONE AS DESC_VARIETA, " +
               "        TVAR.CODICE_VARIETA AS COD_VAR, " +
               "        SUA.ID_UTILIZZO, " +
               "        TU.CODICE, " +
               "        TU.DESCRIZIONE AS DESC_TIPO_UTILIZZO, " +
               "        SUA.PERCENTUALE_VARIETA, " +
               "        SUA.ID_VINO, " +
               "        TV.DESCRIZIONE AS DESC_TIPO_VINO, " +
               "        SUA.ID_AZIENDA, " +
               "        SUA.ID_TIPOLOGIA_VINO, " +
               "        TTV.DESCRIZIONE AS DESC_TIPO_TIPOLOGIA_VINO " +
               " FROM   DB_STORICO_PARTICELLA SP, " +
               "        COMUNE C, " +
               "        PROVINCIA P, " +
               "        DB_STORICO_UNITA_ARBOREA SUA, " +
               "        DB_TIPO_TIPOLOGIA_UNAR TTU, " +
               "        DB_TIPO_VARIETA TVAR, " +
               "        DB_TIPO_VINO TV, " +
               "        DB_TIPO_UTILIZZO TU, " +
               "        DB_TIPO_TIPOLOGIA_VINO TTV " +
               " WHERE  SUA.ID_STORICO_UNITA_ARBOREA = ? " +
               " AND    SUA.ID_PARTICELLA = SP.ID_PARTICELLA " +
               " AND    SP.COMUNE = C.ISTAT_COMUNE " +
               " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
               " AND    SUA.ID_TIPOLOGIA_UNAR = TTU.ID_TIPOLOGIA_UNAR(+) " +
               " AND    SUA.ID_VARIETA = TVAR.ID_VARIETA(+) " +
               " AND    SUA.ID_VINO = TV.ID_VINO(+) " +
               " AND    SUA.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
               " AND    SP.DATA_FINE_VALIDITA IS NULL " +
               " AND    SUA.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO(+) ";
      
      

      SolmrLogger.debug(this, "Value of parameter 1 [ID_STORICO_UNITA_ARBOREA] in findStoricoParticellaArboreaBasic method in StoricoUnitaArboreaDAO: "+idStoricoUnitaArborea+"\n");

      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idStoricoUnitaArborea.longValue());

      SolmrLogger.debug(this, "Executing findStoricoParticellaArboreaBasic: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        storicoParticellaVO = new StoricoParticellaVO();
        StoricoUnitaArboreaVO storicoUnitaArboreaVO = new StoricoUnitaArboreaVO();
        storicoParticellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
        storicoParticellaVO.setIdParticella(new Long(rs.getLong("ID_PARTICEL")));
        storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
        ComuneVO comuneVO = new ComuneVO();
        comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
        comuneVO.setDescom(rs.getString("DESCOM"));
        comuneVO.setIstatComune(rs.getString("COMUNE"));
        comuneVO.setIstatProvincia(rs.getString("ISTAT_PROVINCIA"));
        storicoParticellaVO.setComuneParticellaVO(comuneVO);
        storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
        storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
        storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
        storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
        storicoUnitaArboreaVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
        storicoUnitaArboreaVO.setIdUnitaArborea(new Long(rs.getLong("ID_UNITA_ARBOREA")));
        storicoUnitaArboreaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
        storicoUnitaArboreaVO.setProgrUnar(rs.getString("PROGR_UNAR"));
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_UNAR"))) 
        {
          storicoUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
          TipoTipologiaUnitaArboreaVO tipoTipologiaUnitaArboreaVO = new TipoTipologiaUnitaArboreaVO();
          tipoTipologiaUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
          tipoTipologiaUnitaArboreaVO.setDescrizione(rs.getString("DESC_TIPO_UNAR"));
          storicoUnitaArboreaVO.setTipoTipologiaUnitaArboreaVO(tipoTipologiaUnitaArboreaVO);
        }
        storicoUnitaArboreaVO.setArea(rs.getString("AREA"));
        storicoUnitaArboreaVO.setSestoSuFila(rs.getString("SESTO_SU_FILA"));
        storicoUnitaArboreaVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
        storicoUnitaArboreaVO.setNumCeppi(rs.getString("NUM_CEPPI"));
        storicoUnitaArboreaVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
        storicoUnitaArboreaVO.setDataImpianto(rs.getDate("DATA_IMPIANTO"));
        storicoUnitaArboreaVO.setAnnoIscrizioneAlbo(rs.getString("ANNO_ISCRIZIONE_ALBO"));
        storicoUnitaArboreaVO.setDataIscrizioneAlbo(rs.getDate("DATA_ISCRIZIONE_ALBO"));
        
        if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) 
        {
          storicoUnitaArboreaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
          tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          tipoVarietaVO.setDescrizione(rs.getString("DESC_VARIETA"));
          tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VAR"));
          storicoUnitaArboreaVO.setTipoVarietaVO(tipoVarietaVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO"))) 
        {
          storicoUnitaArboreaVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
          TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
          tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
          tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
          tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTILIZZO"));
          storicoUnitaArboreaVO.setTipoUtilizzoVO(tipoUtilizzoVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_VINO"))) 
        {
          storicoUnitaArboreaVO.setIdVino(new Long(rs.getLong("ID_VINO")));
          TipoVinoVO tipoVinoVO = new TipoVinoVO();
          tipoVinoVO.setIdVino(new Long(rs.getLong("ID_VINO")));
          tipoVinoVO.setDescrizione(rs.getString("DESC_TIPO_VINO"));
          storicoUnitaArboreaVO.setTipoVinoVO(tipoVinoVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_AZIENDA"))) {
          storicoUnitaArboreaVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        }
        
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VINO"))) {
          storicoUnitaArboreaVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
          TipoTipologiaVinoVO tipoTipologiaVinoVO = new TipoTipologiaVinoVO();
          tipoTipologiaVinoVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
          tipoTipologiaVinoVO.setDescrizione(rs.getString("DESC_TIPO_TIPOLOGIA_VINO"));
          storicoUnitaArboreaVO.setTipoTipologiaVinoVO(tipoTipologiaVinoVO);
        }       
        
        storicoParticellaVO.setStoricoUnitaArboreaVO(storicoUnitaArboreaVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "findStoricoParticellaArboreaBasic in StoricoUnitaArboreaDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "findStoricoParticellaArboreaBasic in StoricoUnitaArboreaDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "findStoricoParticellaArboreaBasic in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "findStoricoParticellaArboreaBasic in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated findStoricoParticellaArboreaBasic method in StoricoUnitaArboreaDAO\n");
    return storicoParticellaVO;
  }
	
	
	
	public StoricoParticellaVO findStoricoParticellaArboreaConduzione(Long idStoricoUnitaArborea, long idAzienda) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating findStoricoParticellaArboreaConduzione method in StoricoUnitaArboreaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    StoricoParticellaVO storicoParticellaVO = null;

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in findStoricoParticellaArboreaConduzione method in StoricoUnitaArboreaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in findStoricoParticellaArboreaConduzione method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");
      
      String query =
        "WITH COND_ELEG AS " +
        "  (SELECT CE.ID_PARTICELLA, " +
        "          CE.PERCENTUALE_UTILIZZO " +
        "   FROM   DB_CONDUZIONE_ELEGGIBILITA CE " +
        "   WHERE  CE.ID_AZIENDA = ? " + 
        "   AND    CE.DATA_FINE_VALIDITA IS NULL) " +          
        "SELECT SP.ID_STORICO_PARTICELLA, " +
        "       SP.ID_PARTICELLA AS ID_PARTICEL, " +
        "       SP.COMUNE, " +
        "       C.DESCOM, " +
        "       P.SIGLA_PROVINCIA, " +
        "       SP.SEZIONE, " +
        "       SP.FOGLIO, " +
        "       SP.PARTICELLA, " +
        "       SP.SUBALTERNO, " +
        "       SP.SUP_CATASTALE, " +
        "       SUA.ID_STORICO_UNITA_ARBOREA, " +
        "       SUA.ID_UNITA_ARBOREA, " +
        "       SUA.ID_PARTICELLA, " +
        "       SUA.PROGR_UNAR, " +
        "       SUA.DATA_INIZIO_VALIDITA, " +
        "       SUA.DATA_FINE_VALIDITA, " +
        "       SUA.DATA_CESSAZIONE, " +
        "       SUA.AREA, " +
        "       SUA.DATA_AGGIORNAMENTO, " +
        "       SUA.ID_UTENTE_AGGIORNAMENTO, " +
        "       SUA.ID_CATALOGO_MATRICE, " +
        "       RCM.ID_UTILIZZO, " +
        "       TU.CODICE, " +
        "       TU.DESCRIZIONE AS DESC_TIPO_UTILIZZO, " +
        "       RCM.ID_VARIETA, " +
        "       PC.ID_PARTICELLA_CERTIFICATA, "+
        "       SUA.ID_AZIENDA, " +
        "       NVL(PCK_SMRGAA_LIBRERIA.SelTotSupElegByPartEVetCat(PC.ID_PARTICELLA_CERTIFICATA, SUA.ID_CATALOGO_MATRICE),0) AS SUPERFICIE_ELEG, " +
        "       SUM(CP.PERCENTUALE_POSSESSO) AS PERCENTUALE_POSSESSO, " +
        "       NVL(CEL.PERCENTUALE_UTILIZZO, SUM(CP.PERCENTUALE_POSSESSO)) AS PERC_USO_ELEG " +
        "FROM   DB_STORICO_PARTICELLA SP, " +
        "       COMUNE C, " +
        "       PROVINCIA P, " +
        "       DB_STORICO_UNITA_ARBOREA SUA, " +
        "       DB_R_CATALOGO_MATRICE RCM, " +
        "       DB_TIPO_UTILIZZO TU, " +
        "       DB_PARTICELLA_CERTIFICATA PC, " +
        "       DB_UTE U, " +
        "       DB_CONDUZIONE_PARTICELLA CP, " +
        "       COND_ELEG CEL " +
        "WHERE  SUA.ID_STORICO_UNITA_ARBOREA = ? " +
        "AND    SUA.ID_PARTICELLA = SP.ID_PARTICELLA " +
        "AND    SP.COMUNE = C.ISTAT_COMUNE " +
        "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
        "AND    SUA.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE(+) " +
        "AND    RCM.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
        "AND    SP.DATA_FINE_VALIDITA IS NULL " +
        "AND    SP.COMUNE = PC.COMUNE(+) " +
        "AND    NVL(SP.SEZIONE, '-') = NVL(PC.SEZIONE(+), '-') " +
        "AND    SP.FOGLIO = PC.FOGLIO(+) " +
        "AND    SP.PARTICELLA = PC.PARTICELLA(+) " +
        "AND    NVL(SP.SUBALTERNO, '-') = NVL(PC.SUBALTERNO(+), '-') " +
        "AND    PC.DATA_FINE_VALIDITA(+) IS NULL " +
        "AND    SUA.ID_AZIENDA = U.ID_AZIENDA " +
        "AND    U.ID_UTE = CP.ID_UTE " +
        "AND    CP.ID_PARTICELLA = SUA.ID_PARTICELLA " +
        "AND    CP.ID_TITOLO_POSSESSO NOT IN (5,6) " +
        "AND    U.DATA_FINE_ATTIVITA IS NULL " +
        "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "AND    SUA.ID_PARTICELLA = CEL.ID_PARTICELLA(+) " +
        "GROUP BY " +
        "       SP.ID_STORICO_PARTICELLA, " +
        "       SP.ID_PARTICELLA, " +
        "       SP.COMUNE, " +
        "       C.DESCOM, " +
        "       P.SIGLA_PROVINCIA, " +
        "       SP.SEZIONE, " +
        "       SP.FOGLIO, " +
        "       SP.PARTICELLA, " +
        "       SP.SUBALTERNO, " +
        "       SP.SUP_CATASTALE, " +
        "       SUA.ID_STORICO_UNITA_ARBOREA, " +
        "       SUA.ID_UNITA_ARBOREA, " +
        "       SUA.ID_PARTICELLA, " +
        "       SUA.PROGR_UNAR, " +
        "       SUA.DATA_INIZIO_VALIDITA, " +
        "       SUA.DATA_FINE_VALIDITA, " +
        "       SUA.DATA_CESSAZIONE, " +
        "       SUA.AREA, " +
        "       SUA.DATA_AGGIORNAMENTO, " +
        "       SUA.ID_UTENTE_AGGIORNAMENTO, " +
        "       SUA.ID_CATALOGO_MATRICE, " +
        "       RCM.ID_UTILIZZO, " +
        "       TU.CODICE, " +
        "       TU.DESCRIZIONE, " +
        "       RCM.ID_VARIETA, " +
        "       PC.ID_PARTICELLA_CERTIFICATA, "+
        "       SUA.ID_AZIENDA, " +
        "       CEL.PERCENTUALE_UTILIZZO ";


      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda);
      stmt.setLong(2, idStoricoUnitaArborea.longValue());

      SolmrLogger.debug(this, "Executing findStoricoParticellaArboreaConduzione: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        storicoParticellaVO = new StoricoParticellaVO();
        StoricoUnitaArboreaVO storicoUnitaArboreaVO = new StoricoUnitaArboreaVO();
        ParticellaCertificataVO particellaCertificataVO = new ParticellaCertificataVO();
        storicoParticellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
        storicoParticellaVO.setIdParticella(new Long(rs.getLong("ID_PARTICEL")));
        storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
        ComuneVO comuneVO = new ComuneVO();
        comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
        comuneVO.setDescom(rs.getString("DESCOM"));
        comuneVO.setIstatComune(rs.getString("COMUNE"));
        ProvinciaVO provinciaVO = new ProvinciaVO();
        provinciaVO.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));
        comuneVO.setProvinciaVO(provinciaVO);
        storicoParticellaVO.setComuneParticellaVO(comuneVO);
        storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
        storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
        storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
        storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
        storicoParticellaVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
        storicoUnitaArboreaVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
        storicoUnitaArboreaVO.setIdUnitaArborea(new Long(rs.getLong("ID_UNITA_ARBOREA")));
        storicoUnitaArboreaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
        storicoUnitaArboreaVO.setProgrUnar(rs.getString("PROGR_UNAR"));
        storicoUnitaArboreaVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        storicoUnitaArboreaVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        storicoUnitaArboreaVO.setDataCessazione(rs.getTimestamp("DATA_CESSAZIONE"));
        storicoUnitaArboreaVO.setArea(rs.getString("AREA"));     
        storicoUnitaArboreaVO.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO"));
        storicoUnitaArboreaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));        
        if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO"))) 
        {
          storicoUnitaArboreaVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
          TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
          tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
          tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
          tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTILIZZO"));
          storicoUnitaArboreaVO.setTipoUtilizzoVO(tipoUtilizzoVO);
        }
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
        //Nuova Eleggibilta Fine ***********
        storicoParticellaVO.setParticellaCertificataVO(particellaCertificataVO);
        if(Validator.isNotEmpty(rs.getString("ID_AZIENDA"))) {
          storicoUnitaArboreaVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        }
        
        ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
        conduzioneParticellaVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
        ConduzioneParticellaVO[] elencoConduzioniPart = new ConduzioneParticellaVO[1];
        elencoConduzioniPart[0] = conduzioneParticellaVO;
        storicoParticellaVO.setElencoConduzioni(elencoConduzioniPart);
        
        BigDecimal percUtilizzoElegg = rs.getBigDecimal("PERC_USO_ELEG");
        if(Validator.isNotEmpty(percUtilizzoElegg)
            && (percUtilizzoElegg.compareTo(new BigDecimal(100)) > 0))
        {
          percUtilizzoElegg = new BigDecimal(100);
        }
        storicoParticellaVO.setPercentualeUtilizzoEleg(percUtilizzoElegg);
        
        
        storicoParticellaVO.setStoricoUnitaArboreaVO(storicoUnitaArboreaVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "findStoricoParticellaArboreaConduzione in StoricoUnitaArboreaDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "findStoricoParticellaArboreaConduzione in StoricoUnitaArboreaDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "findStoricoParticellaArboreaConduzione in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "findStoricoParticellaArboreaConduzione in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated findStoricoParticellaArboreaConduzione method in StoricoUnitaArboreaDAO\n");
    return storicoParticellaVO;
  }
	
	/**
	 * Metodo che mi permette di estrarre tutte le particelle libere, cioè non legate
	 * a nessuna azienda ma solo ad una particella
	 * 
	 * @param idParticella
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.StoricoUnitaArboreaVO[]
	 * @throws DataAccessException
	 */
	public StoricoUnitaArboreaVO[] getListStoricoUnitaArboreaFreeByIdParticella(Long idParticella, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListStoricoUnitaArboreaFreeByIdParticella method in StoricoUnitaArboreaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<StoricoUnitaArboreaVO> elencoUnitaArboree = new Vector<StoricoUnitaArboreaVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListStoricoUnitaArboreaFreeByIdParticella method in StoricoUnitaArboreaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListStoricoUnitaArboreaFreeByIdParticella method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");

			String query = " SELECT SUA.ID_STORICO_UNITA_ARBOREA, " +
						   "        SUA.ID_UNITA_ARBOREA, " +
						   "        SUA.ID_PARTICELLA, " +
						   "        SUA.PROGR_UNAR, " +
						   "        SUA.DATA_INIZIO_VALIDITA, " +
						   "        SUA.DATA_FINE_VALIDITA, " +
						   "        SUA.DATA_LAVORAZIONE, " +
						   "        SUA.ID_TIPOLOGIA_UNAR, " +
						   "        TTU.DESCRIZIONE AS DESC_TIPO_UNAR, " +
						   "        SUA.AREA, " +
						   "        SUA.SESTO_SU_FILA, " +
						   "        SUA.SESTO_TRA_FILE, " +
						   "        SUA.NUM_CEPPI, " +
						   "        SUA.ANNO_IMPIANTO, " +
						   "        SUA.DATA_IMPIANTO, " +
						   "        SUA.ANNO_REINNESTO, " +
						   "        SUA.ID_FORMA_ALLEVAMENTO, " +
						   "        TFA.DESCRIZIONE AS DESC_FORMA_ALLEVAMENTO, " +
						   "        SUA.ID_IRRIGAZIONE_UNAR, " +
						   "        SUA.ID_COLTIVAZIONE_UNAR, " +
						   "        SUA.CODICE_TIPO_VARIETA, " +
						   "        SUA.PRESENZA_ALTRI_VITIGNI, " +
						   "        SUA.NUMERO_PIANTE_PRODUTTIVO, " +
						   "        SUA.NUMERO_ALTRE_PIANTE, " +
						   "        SUA.CAMPAGNA, " +
						   "        SUA.ID_TIPOLOGIA_VIGNETO, " +
						   "        SUA.TIPO_IMPIANTO, " +
						   "        SUA.NUMERO_CASTAGNI, " +
						   "        SUA.GRUPPO, " +
						   "        SUA.RICADUTA, " +
						   "        SUA.ID_GIACITURA_UNAR, " +
						   "        SUA.ID_ROCCIA_UNAR, " +
						   "        SUA.ID_SCHELETRO_UNAR, " +
						   "        SUA.ID_STATO_VEGETATIVO_UNAR, " +
						   "        SUA.ID_POTATURA_UNAR, " +
						   "        SUA.ID_GIUDIZIO_UNAR, " +
						   "        SUA.SUPPLEMENTARI, " +
						   "        SUA.MECCANIZZABILE, " +
						   "        SUA.DIMENSIONE_CHIOMA, " +
						   "        SUA.ID_ETA_IMPIANTO_UNAR, " +
						   "        SUA.PROVINCIA_CCIAA, " +
						   "        SUA.MATRICOLA_CCIAA, " +
						   "        SUA.CONFERMA_PREC_ISCRIZIONE_ALBO, " +
						   "        SUA.RICHIESTA_NUOVA_ISCR_ALBO, " +
						   "        SUA.CONFERMA_RICH_NUOVA_ISCR_ALBO, " +
						   "        SUA.SUPERFICIE_DA_ISCRIVERE_ALBO, " +
						   "        SUA.ANNO_ISCRIZIONE_ALBO, " +
						   "        SUA.ID_FONTE, " +
						   "        SUA.ID_VARIAZIONE_UNAR, " +
						   "        SUA.NOTE, " +
						   "        SUA.DATA_AGGIORNAMENTO, " +
						   "        SUA.ID_UTENTE_AGGIORNAMENTO, " +
						   "        SUA.ID_VARIETA, " +
						   "        TVAR.DESCRIZIONE AS DESC_VARIETA, " +
						   "        TVAR.CODICE_VARIETA AS COD_VAR, " +
						   "        SUA.ID_UTILIZZO, " +
						   "        TU.CODICE, " +
						   "        TU.DESCRIZIONE AS DESC_TIPO_UTILIZZO, " +
						   "        SUA.PERCENTUALE_VARIETA, " +
						   "        SUA.ID_VINO, " +
						   "        TV.DESCRIZIONE AS DESC_TIPO_VINO, " +
						   "        SUA.DATA_ESECUZIONE, " +
						   "        SUA.RECORD_MODIFICATO, " +
						   "        SUA.ID_CAUSALE_MODIFICA, " +
						   "        TCM.DESCRIZIONE AS DESC_MODIFICA, " +
						   "        SUA.ID_TIPOLOGIA_VINO, " +
						   "        TTV.DESCRIZIONE AS DESC_TIPO_VINO, " +
						   "        SUA.COLTURA_SPECIALIZZATA, " +
						   "        SUA.RICADUTA, " +
						   "        SUA.ANNO_RIFERIMENTO, " +
						   "        SUA.ANNO_PRIMA_PRODUZIONE, " +
						   "        SUA.DATA_PRIMA_PRODUZIONE, " +
               "        SUA.VIGNA, " +
               "        SUA.ID_VIGNA, " +
               "        SUA.ID_MENZIONE_GEOGRAFICA, " +
               "        SUA.ETICHETTA, " +
               "        SUA.ID_GENERE_ISCRIZIONE, " +
               "        SUA.FLAG_IMPRODUTTIVA, " +
               "        SUA.PERCENTUALE_FALLANZA " +
               "        SUA.DATA_SOVRAINNESTO, " +
               "        SUA.DATA_INTERVENTO, " +
               "        SUA.ID_TIPO_INTERVENTO_VITICOLO, " +
               "        SUA.ID_FILO_SOSTEGNO, " +
               "        SUA.ID_PALO_TESTATA, " +
               "        SUA.ID_PALO_TESSITURA, " +
               "        SUA.ID_ANCORAGGIO_UNAR, " +
               "        SUA.ID_STATO_COLTIVAZIONE_UNAR, " +
               "        SUA.DISTANZA_PALI, " +
               "        SUA.ALTITUDINE_SLM, " +
               "        SUA.AREA_SERVIZIO, " +
               "        SUA.PERCENTUALE_PENDENZA_MEDIA, " +
               "        SUA.GRADI_PENDENZA_MEDIA, " +
               "        SUA.GRADI_ESPOSIZIONE_MEDIA," +
               "        SUA.METRI_ALTITUDINE_MEDIA, " +
               "        SUA.ID_UNITA_ARBOREA_MADRE, " +
               "        SUA.ID_CATALOGO_MATRICE " +               
						   " FROM   DB_STORICO_UNITA_ARBOREA SUA, " +
						   "        DB_TIPO_TIPOLOGIA_UNAR TTU, " +
						   "        DB_TIPO_FORMA_ALLEVAMENTO TFA, " + 
						   "        DB_TIPO_VARIETA TVAR, " +
						   "        DB_TIPO_VINO TV, " +
						   "        DB_TIPO_UTILIZZO TU, " +
						   "        DB_TIPO_CAUSALE_MODIFICA TCM, " +
						   "        DB_TIPO_TIPOLOGIA_VINO TTV " +
						   " WHERE  SUA.ID_PARTICELLA = ? " +
			               " AND    SUA.ID_AZIENDA IS NULL " +
			               " AND    SUA.ID_TIPOLOGIA_UNAR = TTU.ID_TIPOLOGIA_UNAR(+) " +
			               " AND    SUA.ID_FORMA_ALLEVAMENTO = TFA.ID_FORMA_ALLEVAMENTO(+) " +
			               " AND    SUA.ID_VARIETA = TVAR.ID_VARIETA(+) " +
			               " AND    SUA.ID_VINO = TV.ID_VINO(+) " +
			               " AND    SUA.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
			               " AND    SUA.ID_CAUSALE_MODIFICA = TCM.ID_CAUSALE_MODIFICA(+) " +
						   " AND    SUA.DATA_FINE_VALIDITA IS NULL " +
						   " AND    SUA.DATA_CESSAZIONE IS NULL " +
						   " AND    SUA.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO(+) ";
			
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

			SolmrLogger.debug(this, "Value of parameter 1 [ID_PARTICELLA] in getListStoricoUnitaArboreaFreeByIdParticella method in StoricoUnitaArboreaDAO: "+idParticella+"\n");
			SolmrLogger.debug(this, "Value of parameter 4 [ORDINAMENTO] in getListStoricoUnitaArboreaFreeByIdParticella method in StoricoUnitaArboreaDAO: "+ordinamento+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idParticella.longValue());

			SolmrLogger.debug(this, "Executing getListStoricoUnitaArboreaFreeByIdParticella: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				StoricoUnitaArboreaVO storicoUnitaArboreaVO = new StoricoUnitaArboreaVO();
				storicoUnitaArboreaVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
				storicoUnitaArboreaVO.setIdUnitaArborea(new Long(rs.getLong("ID_UNITA_ARBOREA")));
				storicoUnitaArboreaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
				storicoUnitaArboreaVO.setProgrUnar(rs.getString("PROGR_UNAR"));
				storicoUnitaArboreaVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				storicoUnitaArboreaVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				storicoUnitaArboreaVO.setDataLavorazione(rs.getDate("DATA_LAVORAZIONE"));
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_UNAR"))) {
					storicoUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
					TipoTipologiaUnitaArboreaVO tipoTipologiaUnitaArboreaVO = new TipoTipologiaUnitaArboreaVO();
					tipoTipologiaUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
					tipoTipologiaUnitaArboreaVO.setDescrizione(rs.getString("DESC_TIPO_UNAR"));
					storicoUnitaArboreaVO.setTipoTipologiaUnitaArboreaVO(tipoTipologiaUnitaArboreaVO);
				}
				storicoUnitaArboreaVO.setArea(rs.getString("AREA"));
				storicoUnitaArboreaVO.setSestoSuFila(rs.getString("SESTO_SU_FILA"));
				storicoUnitaArboreaVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
				storicoUnitaArboreaVO.setNumCeppi(rs.getString("NUM_CEPPI"));
				storicoUnitaArboreaVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
				storicoUnitaArboreaVO.setDataImpianto(rs.getDate("DATA_IMPIANTO"));
				storicoUnitaArboreaVO.setAnnoReinnesto(rs.getString("ANNO_REINNESTO"));
				if(Validator.isNotEmpty(rs.getString("ID_FORMA_ALLEVAMENTO"))) {
					storicoUnitaArboreaVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
					TipoFormaAllevamentoVO tipoFormaAllevamentoVO = new TipoFormaAllevamentoVO();
					tipoFormaAllevamentoVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
					tipoFormaAllevamentoVO.setDescrizione(rs.getString("DESC_FORMA_ALLEVAMENTO"));
					storicoUnitaArboreaVO.setTipoFormaAllevamentoVO(tipoFormaAllevamentoVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_IRRIGAZIONE_UNAR"))) {
					storicoUnitaArboreaVO.setIdIrrigazioneUnar(new Long(rs.getLong("ID_IRRIGAZIONE_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_COLTIVAZIONE_UNAR"))) {
					storicoUnitaArboreaVO.setIdColtivazioneUnar(new Long(rs.getLong("ID_COLTIVAZIONE_UNAR")));
				}
				storicoUnitaArboreaVO.setCodiceTipoVarieta(rs.getString("CODICE_TIPO_VARIETA"));
				storicoUnitaArboreaVO.setPresenzaAltriVitigni(rs.getString("PRESENZA_ALTRI_VITIGNI"));
				storicoUnitaArboreaVO.setNumeroPianteProduttivo(rs.getString("NUMERO_PIANTE_PRODUTTIVO"));
				storicoUnitaArboreaVO.setNumeroAltrePiante(rs.getString("NUMERO_ALTRE_PIANTE"));
				storicoUnitaArboreaVO.setCampagna(rs.getString("CAMPAGNA"));
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VIGNETO"))) {
					storicoUnitaArboreaVO.setIdTipologiaVigneto(new Long(rs.getLong("ID_TIPOLOGIA_VIGNETO")));
				}
				storicoUnitaArboreaVO.setTipoImpianto(rs.getString("TIPO_IMPIANTO"));
				storicoUnitaArboreaVO.setNumeroCastagni(rs.getString("NUMERO_CASTAGNI"));
				storicoUnitaArboreaVO.setGruppo(rs.getString("GRUPPO"));
				storicoUnitaArboreaVO.setRicaduta(rs.getString("RICADUTA"));
				if(Validator.isNotEmpty(rs.getString("ID_GIACITURA_UNAR"))) {
					storicoUnitaArboreaVO.setIdGiacituraUnar(new Long(rs.getLong("ID_GIACITURA_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_ROCCIA_UNAR"))) {
					storicoUnitaArboreaVO.setIdRocciaUnar(new Long(rs.getLong("ID_ROCCIA_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_SCHELETRO_UNAR"))) {
					storicoUnitaArboreaVO.setIdScheletroUnar(new Long(rs.getLong("ID_SCHELETRO_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_STATO_VEGETATIVO_UNAR"))) {
					storicoUnitaArboreaVO.setIdStatoVegetativoUnar(new Long(rs.getLong("ID_STATO_VEGETATIVO_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_POTATURA_UNAR"))) {
					storicoUnitaArboreaVO.setIdPotaturaUnar(new Long(rs.getLong("ID_POTATURA_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_GIUDIZIO_UNAR"))) {
					storicoUnitaArboreaVO.setIdGiudizioUnar(new Long(rs.getLong("ID_GIUDIZIO_UNAR")));
				}
				storicoUnitaArboreaVO.setSupplementari(rs.getString("SUPPLEMENTARI"));
				storicoUnitaArboreaVO.setMeccanizzabile(rs.getString("MECCANIZZABILE"));
				storicoUnitaArboreaVO.setDimensioneChioma(rs.getString("DIMENSIONE_CHIOMA"));
				if(Validator.isNotEmpty(rs.getString("ID_ETA_IMPIANTO_UNAR"))) {
					storicoUnitaArboreaVO.setIdEtaImpiantoUnar(new Long(rs.getLong("ID_ETA_IMPIANTO_UNAR")));
				}
				storicoUnitaArboreaVO.setProvinciaCCIAA(rs.getString("PROVINCIA_CCIAA"));
				storicoUnitaArboreaVO.setMatricolaCCIAA(rs.getString("MATRICOLA_CCIAA"));
				storicoUnitaArboreaVO.setConfermaPrecIscrizioneAlbo(rs.getString("CONFERMA_PREC_ISCRIZIONE_ALBO"));
				storicoUnitaArboreaVO.setRichiestaNuovaIscrAlbo(rs.getString("RICHIESTA_NUOVA_ISCR_ALBO"));
				storicoUnitaArboreaVO.setConfermaRichNuovaIscrAlbo(rs.getString("CONFERMA_RICH_NUOVA_ISCR_ALBO"));
				storicoUnitaArboreaVO.setSuperficieDaIscrivereAlbo(rs.getString("SUPERFICIE_DA_ISCRIVERE_ALBO"));
				storicoUnitaArboreaVO.setAnnoIscrizioneAlbo(rs.getString("ANNO_ISCRIZIONE_ALBO"));
				storicoUnitaArboreaVO.setIdFonte(new Long(rs.getLong("ID_FONTE")));
				if(Validator.isNotEmpty(rs.getString("ID_VARIAZIONE_UNAR"))) {
					storicoUnitaArboreaVO.setIdVariazioneUnar(new Long(rs.getLong("ID_VARIAZIONE_UNAR")));
				}
				storicoUnitaArboreaVO.setNote(rs.getString("NOTE"));
				storicoUnitaArboreaVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
				storicoUnitaArboreaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) {
					storicoUnitaArboreaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
					TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
					tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
					tipoVarietaVO.setDescrizione(rs.getString("DESC_VARIETA"));
					tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VAR"));
					storicoUnitaArboreaVO.setTipoVarietaVO(tipoVarietaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO"))) {
					storicoUnitaArboreaVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
					TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
					tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
					tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
					tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTILIZZO"));
					storicoUnitaArboreaVO.setTipoUtilizzoVO(tipoUtilizzoVO);
				}
				storicoUnitaArboreaVO.setPercentualeVarieta(rs.getString("PERCENTUALE_VARIETA"));
				if(Validator.isNotEmpty(rs.getString("ID_VINO"))) {
					storicoUnitaArboreaVO.setIdVino(new Long(rs.getLong("ID_VINO")));
				}
				if(rs.getDate("DATA_ESECUZIONE") != null) {
					storicoUnitaArboreaVO.setDataEsecuzione(new Timestamp(rs.getDate("DATA_ESECUZIONE").getTime()));
				}
				storicoUnitaArboreaVO.setRecordModificato(rs.getString("RECORD_MODIFICATO"));
				if(Validator.isNotEmpty(rs.getString("ID_CAUSALE_MODIFICA"))) {
					storicoUnitaArboreaVO.setIdCausaleModifica(new Long(rs.getLong("ID_CAUSALE_MODIFICA")));
					TipoCausaleModificaVO tipoCausaleModificaVO = new TipoCausaleModificaVO();
					tipoCausaleModificaVO.setIdCausaleModifica(new Long(rs.getLong("ID_CAUSALE_MODIFICA")));
					tipoCausaleModificaVO.setDescrizione(rs.getString("DESC_MODIFICA"));
					storicoUnitaArboreaVO.setTipoCausaleModificaVO(tipoCausaleModificaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VINO"))) {
					storicoUnitaArboreaVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
					TipoTipologiaVinoVO tipoTipologiaVinoVO = new TipoTipologiaVinoVO();
					tipoTipologiaVinoVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
					tipoTipologiaVinoVO.setDescrizione(rs.getString("DESC_TIPO_VINO"));
					storicoUnitaArboreaVO.setTipoTipologiaVinoVO(tipoTipologiaVinoVO);
				}
				storicoUnitaArboreaVO.setColturaSpecializzata(rs.getString("COLTURA_SPECIALIZZATA"));
				storicoUnitaArboreaVO.setRicaduta(rs.getString("RICADUTA"));
				storicoUnitaArboreaVO.setAnnoRiferimento(rs.getString("ANNO_RIFERIMENTO"));
				
				storicoUnitaArboreaVO.setAnnoPrimaProduzione(rs.getString("ANNO_PRIMA_PRODUZIONE"));
				storicoUnitaArboreaVO.setDataPrimaProduzione(rs.getDate("DATA_PRIMA_PRODUZIONE"));
        storicoUnitaArboreaVO.setVigna(rs.getString("VIGNA"));
        storicoUnitaArboreaVO.setIdVigna(checkLongNull(rs.getString("ID_VIGNA")));
        storicoUnitaArboreaVO.setIdMenzioneGeografica(checkLongNull(rs.getString("ID_MENZIONE_GEOGRAFICA")));
        storicoUnitaArboreaVO.setEtichetta(rs.getString("ETICHETTA"));
        storicoUnitaArboreaVO.setIdGenereIscrizione(checkLongNull(rs.getString("ID_GENERE_ISCRIZIONE")));
        storicoUnitaArboreaVO.setFlagImproduttiva(rs.getString("FLAG_IMPRODUTTIVA"));
        storicoUnitaArboreaVO.setPercentualeFallanza(rs.getBigDecimal("PERCENTUALE_FALLANZA"));
        storicoUnitaArboreaVO.setIdTipoInterventoViticolo(checkLongNull(rs.getString("ID_TIPO_INTERVENTO_VITICOLO")));
        storicoUnitaArboreaVO.setDataIntervento(rs.getTimestamp("DATA_INTERVENTO"));
        storicoUnitaArboreaVO.setDataSovrainnesto(rs.getTimestamp("DATA_SOVRAINNESTO"));
        
        storicoUnitaArboreaVO.setIdFiloSostegno(checkLongNull(rs.getString("ID_FILO_SOSTEGNO")));
        storicoUnitaArboreaVO.setIdPaloTestata(checkLongNull(rs.getString("ID_PALO_TESTATA")));
        storicoUnitaArboreaVO.setIdPaloTessitura(checkLongNull(rs.getString("ID_PALO_TESSITURA")));
        storicoUnitaArboreaVO.setIdAncoraggioUnar(checkLongNull(rs.getString("ID_ANCORAGGIO_UNAR")));
        storicoUnitaArboreaVO.setIdStatoColtivazioneUnar(checkLongNull(rs.getString("ID_STATO_COLTIVAZIONE_UNAR")));
        storicoUnitaArboreaVO.setDistanzaPali(checkLongNull(rs.getString("DISTANZA_PALI")));
        storicoUnitaArboreaVO.setAltitudineSlm(checkLongNull(rs.getString("ALTITUDINE_SLM")));
        storicoUnitaArboreaVO.setAreaServizio(rs.getBigDecimal("AREA_SERVIZIO"));
        
        storicoUnitaArboreaVO.setPercentualePendenzaMedia(rs.getBigDecimal("PERCENTUALE_PENDENZA_MEDIA"));
        storicoUnitaArboreaVO.setGradiPendenzaMedia(rs.getBigDecimal("GRADI_PENDENZA_MEDIA"));
        storicoUnitaArboreaVO.setGradiEsposizioneMedia(rs.getBigDecimal("GRADI_ESPOSIZIONE_MEDIA"));
        storicoUnitaArboreaVO.setMetriAltitudineMedia(checkIntegerNull(rs.getString("METRI_ALTITUDINE_MEDIA")));
        storicoUnitaArboreaVO.setIdUnitaArboreaMadre(checkLongNull(rs.getString("ID_UNITA_ARBOREA_MADRE")));
        storicoUnitaArboreaVO.setIdCatalogoMatrice(checkLongNull(rs.getString("ID_CATALOGO_MATRICE")));
        
				
				elencoUnitaArboree.add(storicoUnitaArboreaVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListStoricoUnitaArboreaFreeByIdParticella in StoricoUnitaArboreaDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListStoricoUnitaArboreaFreeByIdParticella in StoricoUnitaArboreaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListStoricoUnitaArboreaFreeByIdParticella in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListStoricoUnitaArboreaFreeByIdParticella in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListStoricoUnitaArboreaFreeByIdParticella method in StoricoUnitaArboreaDAO\n");
		if(elencoUnitaArboree.size() == 0) {
			return (StoricoUnitaArboreaVO[])elencoUnitaArboree.toArray(new StoricoUnitaArboreaVO[0]);
		}
		else {
			return (StoricoUnitaArboreaVO[])elencoUnitaArboree.toArray(new StoricoUnitaArboreaVO[elencoUnitaArboree.size()]);
		}
	}
	
	/**
	 * Metodo utilizzato per modificare i records sulla tabella DB_STORICO_UNITA_ARBOREA
	 * @param storicoUnitaArboreaVO
	 * @throws DataAccessException
	 */
	public void updateStoricoUnitaArborea(StoricoUnitaArboreaVO storicoUnitaArboreaVO) throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating updateStoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
	  Connection conn = null;
	  PreparedStatement stmt = null;

	  try 
	  {
	  	SolmrLogger.debug(this, "Creating db-connection in updateStoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in updateStoricoUnitaArborea method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");
			
			String query = " UPDATE DB_STORICO_UNITA_ARBOREA " +
						   " SET    ID_UNITA_ARBOREA = ?, " +
						   "        ID_PARTICELLA = ?, " +
						   "        PROGR_UNAR = ?, " +
						   "        DATA_INIZIO_VALIDITA = ?, " +
						   "        DATA_FINE_VALIDITA = ?, " +
						   "        DATA_LAVORAZIONE = ?, " +
						   "        ID_TIPOLOGIA_UNAR = ?, " +
						   "        AREA = ?, " +
						   "        SESTO_SU_FILA = ?, " +
						   "        SESTO_TRA_FILE = ?, " +
						   "        NUM_CEPPI = ?, " +
						   "        ANNO_IMPIANTO = ?, " +
						   "        DATA_IMPIANTO = ?, " +
						   "        ANNO_REINNESTO = ?, " +
						   "        ID_FORMA_ALLEVAMENTO = ?, " +
						   "        ID_IRRIGAZIONE_UNAR = ?, " +
						   "        ID_COLTIVAZIONE_UNAR = ?, " +
						   "        CODICE_TIPO_VARIETA = ?, " +
						   "        PRESENZA_ALTRI_VITIGNI = ?, " +
						   "        NUMERO_PIANTE_PRODUTTIVO = ?, " +
						   "        NUMERO_ALTRE_PIANTE = ?, " +
						   "        CAMPAGNA = ?, " +
						   "        ID_TIPOLOGIA_VIGNETO = ?, " +
						   "        TIPO_IMPIANTO = ?, " +
						   "        NUMERO_CASTAGNI = ?, " +
						   "        GRUPPO = ?, " +
						   "        RICADUTA = ?, " +
						   "        ID_GIACITURA_UNAR = ?, " +
						   "        ID_ROCCIA_UNAR = ?, " +
						   "        ID_SCHELETRO_UNAR = ?, " +
						   "        ID_STATO_VEGETATIVO_UNAR = ?, " +
						   "        ID_POTATURA_UNAR = ?, " +
						   "        ID_GIUDIZIO_UNAR = ?, " +
						   "        SUPPLEMENTARI = ?, " +
						   "        MECCANIZZABILE = ?, " +
						   "        DIMENSIONE_CHIOMA = ?, " +
						   "        ID_ETA_IMPIANTO_UNAR = ?, " +
						   "        PROVINCIA_CCIAA = ?, " +
						   "        MATRICOLA_CCIAA = ?, " +
						   "        CONFERMA_PREC_ISCRIZIONE_ALBO = ?, " +
						   "        RICHIESTA_NUOVA_ISCR_ALBO = ?, " +
						   "        CONFERMA_RICH_NUOVA_ISCR_ALBO = ?, " +
						   "        SUPERFICIE_DA_ISCRIVERE_ALBO = ?, " +
						   "        ANNO_ISCRIZIONE_ALBO = ?, " +
						   "        ID_FONTE = ?, " +
						   "        ID_VARIAZIONE_UNAR = ?, " +
						   "        NOTE = ?, " +
						   "        DATA_AGGIORNAMENTO = ?, " +
						   "        ID_UTENTE_AGGIORNAMENTO = ?, " +
						   "        ID_VARIETA = ?, " +
						   "        ID_UTILIZZO = ?, " +
						   "        PERCENTUALE_VARIETA = ?, " +
						   "        ID_VINO = ?, " +
						   "        DATA_ESECUZIONE = ?, " +
						   "        RECORD_MODIFICATO = ?, " +
						   "        ID_AZIENDA = ?, " +
						   "        DATA_CESSAZIONE = ?, " +
						   "        ID_CESSAZIONE_UNAR = ?, " +
						   "        ID_CAUSALE_MODIFICA = ?, " +
						   "        ESITO_CONTROLLO = ?, " +
						   "        ID_TIPOLOGIA_VINO = ?, " +
						   "        ANNO_RIFERIMENTO = ?, " +
						   "        COLTURA_SPECIALIZZATA = ?, " +
						   "        STATO_UNITA_ARBOREA = ?, " +
						   "        DATA_ISCRIZIONE_ALBO = ?, " +
						   "        ANNO_PRIMA_PRODUZIONE = ?, " +
						   "        DATA_PRIMA_PRODUZIONE = ?, " +
						   "        VIGNA = ?, " +
						   "        ID_GENERE_ISCRIZIONE = ?, " +
						   "        FLAG_IMPRODUTTIVA = ?, " +
						   "        PERCENTUALE_FALLANZA = ?, " +
						   "        ID_VIGNA = ?, " +
						   "        ID_MENZIONE_GEOGRAFICA = ?, " +
						   "        ETICHETTA = ?," +
						   "        DATA_SOVRAINNESTO = ?, " +
						   "        DATA_INTERVENTO = ?, " +
						   "        ID_TIPO_INTERVENTO_VITICOLO = ?, " +
						   "        ID_FILO_SOSTEGNO = ?, " +
               "        ID_PALO_TESTATA = ?, " +
               "        ID_PALO_TESSITURA = ?, " +
               "        ID_ANCORAGGIO_UNAR = ?, " +
               "        ID_STATO_COLTIVAZIONE_UNAR = ?, " +
               "        DISTANZA_PALI = ?, " +
               "        ALTITUDINE_SLM = ?, " +
               "        AREA_SERVIZIO = ?, " +
               "        PERCENTUALE_PENDENZA_MEDIA = ?, " +
               "        GRADI_PENDENZA_MEDIA = ?, " +
               "        GRADI_ESPOSIZIONE_MEDIA = ?," +
               "        METRI_ALTITUDINE_MEDIA = ?, " +
               "        ID_UNITA_ARBOREA_MADRE = ?, " +
               "        ID_CATALOGO_MATRICE = ? " +
						   " WHERE  ID_STORICO_UNITA_ARBOREA = ? ";
			
			stmt = conn.prepareStatement(query);
			
			int idx = 0;

			SolmrLogger.debug(this, "Executing updateStoricoUnitaArborea: "+query);
			
			stmt.setLong(++idx, storicoUnitaArboreaVO.getIdUnitaArborea().longValue());
			SolmrLogger.debug(this, "Value of parameter [ID_UNITA_ARBOREA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdUnitaArborea().longValue()+"\n");
			stmt.setLong(++idx, storicoUnitaArboreaVO.getIdParticella().longValue());
			SolmrLogger.debug(this, "Value of parameter [ID_PARTICELLA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdParticella().longValue()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getProgrUnar());
			SolmrLogger.debug(this, "Value of parameter [PROGR_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getProgrUnar()+"\n");
			stmt.setTimestamp(++idx, new Timestamp(storicoUnitaArboreaVO.getDataInizioValidita().getTime()));
			SolmrLogger.debug(this, "Value of parameter [DATA_INIZIO_VALIDITA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new Timestamp(storicoUnitaArboreaVO.getDataInizioValidita().getTime())+"\n");
			SolmrLogger.debug(this, "Value of parameter [DATA_INIZIO_VALIDITA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+convertDateToTimestamp(storicoUnitaArboreaVO.getDataInizioValidita())+"\n");
			if(storicoUnitaArboreaVO.getDataFineValidita() != null) {
				stmt.setTimestamp(++idx, new Timestamp(storicoUnitaArboreaVO.getDataFineValidita().getTime()));
				SolmrLogger.debug(this, "Value of parameter [DATA_FINE_VALIDITA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new Timestamp(storicoUnitaArboreaVO.getDataFineValidita().getTime())+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [DATA_FINE_VALIDITA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getDataLavorazione() != null) {
				stmt.setDate(++idx, new java.sql.Date(storicoUnitaArboreaVO.getDataLavorazione().getTime()));
				SolmrLogger.debug(this, "Value of parameter [DATA_LAVORAZIONE] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new java.sql.Date(storicoUnitaArboreaVO.getDataLavorazione().getTime())+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [DATA_LAVORAZIONE] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getIdTipologiaUnar() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdTipologiaUnar().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_TIPOLOGIA_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdTipologiaUnar().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_TIPOLOGIA_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(Validator.isNotEmpty(storicoUnitaArboreaVO.getArea())) {
				stmt.setString(++idx, StringUtils.parseSuperficieField(storicoUnitaArboreaVO.getArea()));
				SolmrLogger.debug(this, "Value of parameter [AREA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getArea()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [AREA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			stmt.setString(++idx, storicoUnitaArboreaVO.getSestoSuFila());
			SolmrLogger.debug(this, "Value of parameter [SESTO_SU_FILA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getSestoSuFila()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getSestoTraFile());
			SolmrLogger.debug(this, "Value of parameter [SESTO_TRA_FILE] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getSestoTraFile()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getNumCeppi());
			SolmrLogger.debug(this, "Value of parameter [NUMERO_CEPPI] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getNumCeppi()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getAnnoImpianto());
			SolmrLogger.debug(this, "Value of parameter [ANNO_IMPIANTO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getAnnoImpianto()+"\n");
			if(storicoUnitaArboreaVO.getDataImpianto() != null) {
        stmt.setTimestamp(++idx, new Timestamp(storicoUnitaArboreaVO.getDataImpianto().getTime()));
        SolmrLogger.debug(this, "Value of parameter [DATA_IMPIANTO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new Timestamp(storicoUnitaArboreaVO.getDataImpianto().getTime())+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [DATA_IMPIANTO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }			
			stmt.setString(++idx, storicoUnitaArboreaVO.getAnnoReinnesto());
			SolmrLogger.debug(this, "Value of parameter [ANNO_REINNESTO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getAnnoReinnesto()+"\n");
			if(storicoUnitaArboreaVO.getIdFormaAllevamento() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdFormaAllevamento().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_FORMA_ALLEVAMENTO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdFormaAllevamento().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_FORMA_ALLEVAMENTO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getIdIrrigazioneUnar() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdIrrigazioneUnar().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_IRRIGAZIONE_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdIrrigazioneUnar().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_IRRIGAZIONE_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getIdColtivazioneUnar() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdColtivazioneUnar().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_COLTIVAZIONE_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdColtivazioneUnar().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_COLTIVAZIONE_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			stmt.setString(++idx, storicoUnitaArboreaVO.getCodiceTipoVarieta());
			SolmrLogger.debug(this, "Value of parameter [CODICE_TIPO_VARIETA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getCodiceTipoVarieta()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getPresenzaAltriVitigni());
			SolmrLogger.debug(this, "Value of parameter [PRESENZA_ALTRI_VITIGNI] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getPresenzaAltriVitigni()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getNumeroPianteProduttivo());
			SolmrLogger.debug(this, "Value of parameter [NUMERO_PIANTE_PRODUTTIVO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getNumeroPianteProduttivo()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getNumeroAltrePiante());
			SolmrLogger.debug(this, "Value of parameter [NUMERO_ALTRE_PIANTE] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getNumeroAltrePiante()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getCampagna());
			SolmrLogger.debug(this, "Value of parameter [CAMPAGNA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getCampagna()+"\n");
			if(storicoUnitaArboreaVO.getIdTipologiaVigneto() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdTipologiaVigneto().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_TIPOLOGIA_VIGNETO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdTipologiaVigneto().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_TIPOLOGIA_VIGNETO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			stmt.setString(++idx, storicoUnitaArboreaVO.getTipoImpianto());
			SolmrLogger.debug(this, "Value of parameter [TIPO_IMPIANTO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getTipoImpianto()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getNumeroCastagni());
			SolmrLogger.debug(this, "Value of parameter [NUMERO_CASTAGNI] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getNumeroCastagni()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getGruppo());
			SolmrLogger.debug(this, "Value of parameter [GRUPPO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getGruppo()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getRicaduta());
			SolmrLogger.debug(this, "Value of parameter [RICADUTA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getRicaduta()+"\n");
			if(storicoUnitaArboreaVO.getIdGiacituraUnar() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdGiacituraUnar().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_GIACITURA_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdGiacituraUnar().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_GIACITURA_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getIdRocciaUnar() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdRocciaUnar().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_ROCCIA_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdRocciaUnar().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_ROCCIA_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getIdScheletroUnar() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdScheletroUnar().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_SCHELETRO_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdScheletroUnar().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_SCHELETRO_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getIdStatoVegetativoUnar() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdStatoVegetativoUnar().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_STATO_VEGETATIVO_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdStatoVegetativoUnar().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_STATO_VEGETATIVO_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getIdPotaturaUnar() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdPotaturaUnar().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_POTATURA_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdPotaturaUnar().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_POTATURA_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getIdGiudizioUnar() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdGiudizioUnar().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_GIUDIZIO_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdGiudizioUnar().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_GIUDIZIO_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			stmt.setString(++idx, storicoUnitaArboreaVO.getSupplementari());
			SolmrLogger.debug(this, "Value of parameter [SUPPLEMENTARI] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getSupplementari()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getMeccanizzabile());
			SolmrLogger.debug(this, "Value of parameter [MECCANIZZABILE] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getMeccanizzabile()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getDimensioneChioma());
			SolmrLogger.debug(this, "Value of parameter [DIMENSIONE_CHIOMA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getDimensioneChioma()+"\n");
			if(storicoUnitaArboreaVO.getIdEtaImpiantoUnar() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdEtaImpiantoUnar().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_ETA_IMPIANTO_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdEtaImpiantoUnar().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_ETA_IMPIANTO_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(Validator.isNotEmpty(storicoUnitaArboreaVO.getProvinciaCCIAA())) {
        stmt.setString(++idx, storicoUnitaArboreaVO.getProvinciaCCIAA());
        SolmrLogger.debug(this, "Value of parameter [PROVINCIA_CCIAA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getProvinciaCCIAA()+"\n");
      }
      else
      {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [PROVINCIA_CCIAA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
			if(Validator.isNotEmpty(storicoUnitaArboreaVO.getMatricolaCCIAA())) {
				stmt.setString(++idx, storicoUnitaArboreaVO.getMatricolaCCIAA());
				SolmrLogger.debug(this, "Value of parameter [MATRICOLA_CCIAA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getMatricolaCCIAA()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [MATRICOLA_CCIAA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			stmt.setString(++idx, storicoUnitaArboreaVO.getConfermaPrecIscrizioneAlbo());
			SolmrLogger.debug(this, "Value of parameter [CONFERMA_PREC_ISCRIZIONE_ALBO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getConfermaPrecIscrizioneAlbo()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getRichiestaNuovaIscrAlbo());
			SolmrLogger.debug(this, "Value of parameter [RICHIESTA_NUOVA_ISCRIZIONE_ALBO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getRichiestaNuovaIscrAlbo()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getConfermaRichNuovaIscrAlbo());
			SolmrLogger.debug(this, "Value of parameter [CONFERMA_RIC_NUOVA_ISCR_ALBO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getConfermaPrecIscrizioneAlbo()+"\n");
			if(Validator.isNotEmpty(storicoUnitaArboreaVO.getSuperficieDaIscrivereAlbo())) {
				stmt.setString(++idx, StringUtils.parseSuperficieField(storicoUnitaArboreaVO.getSuperficieDaIscrivereAlbo()));
			}
			else {
				stmt.setString(++idx, storicoUnitaArboreaVO.getSuperficieDaIscrivereAlbo());
			}
			SolmrLogger.debug(this, "Value of parameter [SUPERFICIE_DA_ISCRIVERE_ALBO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getSuperficieDaIscrivereAlbo()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getAnnoIscrizioneAlbo());
			SolmrLogger.debug(this, "Value of parameter [ANNO_ISCRIZIONE_ALBO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getAnnoIscrizioneAlbo()+"\n");
			if(storicoUnitaArboreaVO.getIdFonte() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdFonte().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_FONTE] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdFonte().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_FONTE] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getIdVariazioneUnar() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdVariazioneUnar().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_VARIAZIONE_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdVariazioneUnar().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_VARIAZIONE_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			stmt.setString(++idx, storicoUnitaArboreaVO.getNote());
			SolmrLogger.debug(this, "Value of parameter [NOTE] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getNote()+"\n");
			stmt.setTimestamp(++idx, convertDateToTimestamp(storicoUnitaArboreaVO.getDataAggiornamento()));
			SolmrLogger.debug(this, "Value of parameter [DATA_AGGIORNAMENTO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new Timestamp(storicoUnitaArboreaVO.getDataAggiornamento().getTime())+"\n");
			stmt.setLong(++idx, storicoUnitaArboreaVO.getIdUtenteAggiornamento().longValue());
			SolmrLogger.debug(this, "Value of parameter [ID_UTENTE_AGGIORNAMENTO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdUtenteAggiornamento().longValue()+"\n");
			if(storicoUnitaArboreaVO.getIdVarieta() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdVarieta().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_VARIETA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdVarieta().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_VARIETA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getIdUtilizzo() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdUtilizzo().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_UTILIZZO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdUtilizzo().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_UTILIZZO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			stmt.setString(++idx, storicoUnitaArboreaVO.getPercentualeVarieta());
			SolmrLogger.debug(this, "Value of parameter [PERCENTUALE_VARIETA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getPercentualeVarieta()+"\n");
			if(storicoUnitaArboreaVO.getIdVino() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdVino().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_VINO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdVino().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_VINO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getDataEsecuzione() != null) {
				stmt.setTimestamp(++idx, new Timestamp(storicoUnitaArboreaVO.getDataEsecuzione().getTime()));
				SolmrLogger.debug(this, "Value of parameter [DATA_ESECUZIONE] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new Timestamp(storicoUnitaArboreaVO.getDataEsecuzione().getTime())+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [DATA_ESECUZIONE] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			stmt.setString(++idx, storicoUnitaArboreaVO.getRecordModificato());
			SolmrLogger.debug(this, "Value of parameter [RECORD_MODIFICATO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getRecordModificato()+"\n");
			if(storicoUnitaArboreaVO.getIdAzienda() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdAzienda().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_AZIENDA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdAzienda().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_AZIENDA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getDataCessazione() != null) {
				stmt.setTimestamp(++idx, new Timestamp(storicoUnitaArboreaVO.getDataCessazione().getTime()));
				SolmrLogger.debug(this, "Value of parameter [DATA_CESSAZIONE] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new Timestamp(storicoUnitaArboreaVO.getDataCessazione().getTime())+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [DATA_CESSAZIONE] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getIdCessazioneUnar() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdCessazioneUnar().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_CESSAZIONE_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdCessazioneUnar().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_CESSAZIONE] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			stmt.setLong(++idx, storicoUnitaArboreaVO.getIdCausaleModifica().longValue());
			SolmrLogger.debug(this, "Value of parameter [ID_CAUSALE_MODIFICA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdCausaleModifica().longValue()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getEsitoControllo());
			SolmrLogger.debug(this, "Value of parameter [ESITO_CONTROLLO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getEsitoControllo()+"\n");
			if(storicoUnitaArboreaVO.getIdTipologiaVino() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdTipologiaVino().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_TIPOLOGIA_VINO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdTipologiaVino().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_TIPOLOGIA_VINO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			stmt.setString(++idx, storicoUnitaArboreaVO.getAnnoRiferimento());
			SolmrLogger.debug(this, "Value of parameter [ANNO_RIFERIMENTO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getAnnoRiferimento()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getColturaSpecializzata());
			SolmrLogger.debug(this, "Value of parameter [COLTURA_SPECIALIZZATA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getColturaSpecializzata()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getStatoUnitaArborea());
			SolmrLogger.debug(this, "Value of parameter [STATO_UNITA_ARBOREA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getStatoUnitaArborea()+"\n");
			if(storicoUnitaArboreaVO.getDataIscrizioneAlbo() != null) {
        stmt.setDate(++idx, new java.sql.Date(storicoUnitaArboreaVO.getDataIscrizioneAlbo().getTime()));
        SolmrLogger.debug(this, "Value of parameter [DATA_ISCRIZIONE_ALBO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new java.sql.Date(storicoUnitaArboreaVO.getDataIscrizioneAlbo().getTime())+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [DATA_ISCRIZIONE_ALBO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
			if(storicoUnitaArboreaVO.getAnnoPrimaProduzione() != null) {
        stmt.setString(++idx, storicoUnitaArboreaVO.getAnnoPrimaProduzione());
        SolmrLogger.debug(this, "Value of parameter [ANNO_PRIMA_PRODUZIONE] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getAnnoPrimaProduzione()+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [ANNO_PRIMA_PRODUZIONE] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
			if(storicoUnitaArboreaVO.getDataPrimaProduzione() != null) {
        stmt.setTimestamp(++idx, new Timestamp(storicoUnitaArboreaVO.getDataPrimaProduzione().getTime()));
        SolmrLogger.debug(this, "Value of parameter [DATA_PRIMA_PRODUZIONE] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new Timestamp(storicoUnitaArboreaVO.getDataPrimaProduzione().getTime())+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [DATA_PRIMA_PRODUZIONE] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
			if(storicoUnitaArboreaVO.getVigna() != null) {
        stmt.setString(++idx, storicoUnitaArboreaVO.getVigna());
        SolmrLogger.debug(this, "Value of parameter [VIGNA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getVigna()+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [VIGNA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
			if(storicoUnitaArboreaVO.getIdGenereIscrizione() != null) {
        stmt.setLong(++idx, storicoUnitaArboreaVO.getIdGenereIscrizione());
        SolmrLogger.debug(this, "Value of parameter [ID_GENERE_ISCRIZIONE] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdGenereIscrizione()+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [ID_GENERE_ISCRIZIONE] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
			stmt.setString(++idx, storicoUnitaArboreaVO.getFlagImproduttiva());
      SolmrLogger.debug(this, "Value of parameter [FLAG_IMPRODUTTIVA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getFlagImproduttiva()+"\n");
      stmt.setBigDecimal(++idx, storicoUnitaArboreaVO.getPercentualeFallanza());
      SolmrLogger.debug(this, "Value of parameter [PERCENTUALE_FALLANZA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getPercentualeFallanza()+"\n");      
      if(storicoUnitaArboreaVO.getIdVigna() != null) {
        stmt.setLong(++idx, storicoUnitaArboreaVO.getIdVigna());
        SolmrLogger.debug(this, "Value of parameter [ID_VIGNA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdVigna()+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [ID_VIGNA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
      if(storicoUnitaArboreaVO.getIdMenzioneGeografica() != null) {
        stmt.setLong(++idx, storicoUnitaArboreaVO.getIdMenzioneGeografica());
        SolmrLogger.debug(this, "Value of parameter [ID_MENZIONE_GEOGRAFICA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdMenzioneGeografica()+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [ID_MENZIONE_GEOGRAFICA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
      if(storicoUnitaArboreaVO.getEtichetta() != null) {
        stmt.setString(++idx, storicoUnitaArboreaVO.getEtichetta());
        SolmrLogger.debug(this, "Value of parameter [ETICHETTA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getEtichetta()+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [ETICHETTA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
      if(storicoUnitaArboreaVO.getDataSovrainnesto() != null) {
        stmt.setTimestamp(++idx, new Timestamp(storicoUnitaArboreaVO.getDataSovrainnesto().getTime()));
        SolmrLogger.debug(this, "Value of parameter [DATA_SOVRAINNESTO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new Timestamp(storicoUnitaArboreaVO.getDataSovrainnesto().getTime())+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [DATA_SOVRAINNESTO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
      if(storicoUnitaArboreaVO.getDataIntervento() != null) {
        stmt.setTimestamp(++idx, new Timestamp(storicoUnitaArboreaVO.getDataIntervento().getTime()));
        SolmrLogger.debug(this, "Value of parameter [DATA_INTERVENTO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new Timestamp(storicoUnitaArboreaVO.getDataIntervento().getTime())+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [DATA_INTERVENTO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
      if(storicoUnitaArboreaVO.getIdTipoInterventoViticolo() != null) {
        stmt.setLong(++idx, storicoUnitaArboreaVO.getIdTipoInterventoViticolo());
        SolmrLogger.debug(this, "Value of parameter [ID_TIPO_INTERVENTO_VITICOLO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdTipoInterventoViticolo()+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [ID_TIPO_INTERVENTO_VITICOLO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
      if(storicoUnitaArboreaVO.getIdFiloSostegno() != null) {
        stmt.setLong(++idx, storicoUnitaArboreaVO.getIdFiloSostegno());
        SolmrLogger.debug(this, "Value of parameter [ID_FILO_SOSTEGNO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdFiloSostegno()+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [ID_FILO_SOSTEGNO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
      if(storicoUnitaArboreaVO.getIdPaloTestata() != null) {
        stmt.setLong(++idx, storicoUnitaArboreaVO.getIdPaloTestata());
        SolmrLogger.debug(this, "Value of parameter [ID_PALO_TESTATA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdPaloTestata()+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [ID_PALO_TESTATA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
      if(storicoUnitaArboreaVO.getIdPaloTessitura() != null) {
        stmt.setLong(++idx, storicoUnitaArboreaVO.getIdPaloTessitura());
        SolmrLogger.debug(this, "Value of parameter [ID_PALO_TESSITURA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdPaloTessitura()+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [ID_PALO_TESSITURA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
      if(storicoUnitaArboreaVO.getIdAncoraggioUnar() != null) {
        stmt.setLong(++idx, storicoUnitaArboreaVO.getIdAncoraggioUnar());
        SolmrLogger.debug(this, "Value of parameter [ID_ANCORAGGIO_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdAncoraggioUnar()+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [ID_ANCORAGGIO_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
      if(storicoUnitaArboreaVO.getIdStatoColtivazioneUnar() != null) {
        stmt.setLong(++idx, storicoUnitaArboreaVO.getIdStatoColtivazioneUnar());
        SolmrLogger.debug(this, "Value of parameter [ID_STATO_COLTIVAZIONE_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdStatoColtivazioneUnar()+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [ID_STATO_COLTIVAZIONE_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
      if(storicoUnitaArboreaVO.getDistanzaPali() != null) {
        stmt.setLong(++idx, storicoUnitaArboreaVO.getDistanzaPali());
        SolmrLogger.debug(this, "Value of parameter [DISTANZA_PALI] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getDistanzaPali()+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [DISTANZA_PALI] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
      if(storicoUnitaArboreaVO.getAltitudineSlm() != null) {
        stmt.setLong(++idx, storicoUnitaArboreaVO.getAltitudineSlm());
        SolmrLogger.debug(this, "Value of parameter [ALTITUDINE_SLM] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getAltitudineSlm()+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [ALTITUDINE_SLM] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
      stmt.setBigDecimal(++idx, storicoUnitaArboreaVO.getAreaServizio());
      
      stmt.setBigDecimal(++idx, storicoUnitaArboreaVO.getPercentualePendenzaMedia());
      stmt.setBigDecimal(++idx, storicoUnitaArboreaVO.getGradiPendenzaMedia());
      stmt.setBigDecimal(++idx, storicoUnitaArboreaVO.getGradiEsposizioneMedia());
      stmt.setBigDecimal(++idx, convertIntegerToBigDecimal(storicoUnitaArboreaVO.getMetriAltitudineMedia()));
      stmt.setBigDecimal(++idx, convertLongToBigDecimal(storicoUnitaArboreaVO.getIdUnitaArboreaMadre()));
      stmt.setBigDecimal(++idx, convertLongToBigDecimal(storicoUnitaArboreaVO.getIdCatalogoMatrice()));
			
			stmt.setLong(++idx, storicoUnitaArboreaVO.getIdStoricoUnitaArborea().longValue());			
			SolmrLogger.debug(this, "Value of parameter [ID_STORICO_UNITA_ARBOREA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdStoricoUnitaArborea().longValue()+"\n");
			
			
			stmt.executeUpdate();

			stmt.close();
	    }
	    catch(SQLException exc) {
	    	SolmrLogger.error(this, "updateStoricoUnitaArborea in StoricoUnitaArboreaDAO - SQLException: "+exc);
	    	throw new DataAccessException(exc.getMessage());
	    }
	    catch (Exception ex) {
	    	SolmrLogger.error(this, "updateStoricoUnitaArborea in StoricoUnitaArboreaDAO - Generic Exception: "+ex);
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
	    		SolmrLogger.error(this, "updateStoricoUnitaArborea in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc);
	    		throw new DataAccessException(exc.getMessage());
	    	}
	    	catch(Exception ex) {
	    		SolmrLogger.error(this, "updateStoricoUnitaArborea in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex);
	    		throw new DataAccessException(ex.getMessage());
	    	}
	    }
	    SolmrLogger.debug(this, "Invocated updateStoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
	}
	
	/**
	 * Metodo utilizzato per inserire un record su DB_STORICO_UNITA_ARBOREA
	 * 
	 * @param storicoUnitaArboreaVO
	 * @return java.lang.Long
	 * @throws DataAccessException
	 */
	public Long insertStoricoUnitaArborea(StoricoUnitaArboreaVO storicoUnitaArboreaVO) throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating insertStoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Long idStoricoUnitaArborea = null;

    try 
    {
	   	idStoricoUnitaArborea = getNextPrimaryKey(SolmrConstants.SEQ_STORICO_UNITA_ARBOREA);
	   	SolmrLogger.debug(this, "Creating db-connection in insertStoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in insertStoricoUnitaArborea method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");
			
			String query = " INSERT INTO DB_STORICO_UNITA_ARBOREA " +
			   			   "        (ID_STORICO_UNITA_ARBOREA, " +
			   			   "         ID_UNITA_ARBOREA, " +
			   			   "         ID_PARTICELLA, " +
			   			   "         PROGR_UNAR, " +
			   			   "         DATA_INIZIO_VALIDITA, " +
			   			   "         DATA_FINE_VALIDITA, " +
			   			   "         DATA_LAVORAZIONE, " +
			   			   "         ID_TIPOLOGIA_UNAR, " +
			   			   "         AREA, " +
			   			   "         SESTO_SU_FILA, " +
			   			   "         SESTO_TRA_FILE, " +
			   			   "         NUM_CEPPI, " +
			   			   "         ANNO_IMPIANTO, " +
			   			   "         DATA_IMPIANTO, " +
			   			   "         ANNO_REINNESTO, " +
			   			   "         ID_FORMA_ALLEVAMENTO, " +
			   			   "         ID_IRRIGAZIONE_UNAR, " +
			   			   "         ID_COLTIVAZIONE_UNAR, " +
			   			   "         CODICE_TIPO_VARIETA, " +
			   			   "         PRESENZA_ALTRI_VITIGNI, " +
			   			   "         NUMERO_PIANTE_PRODUTTIVO, " +
			   			   "         NUMERO_ALTRE_PIANTE, " +
			   			   "         CAMPAGNA, " +
			   			   "         ID_TIPOLOGIA_VIGNETO, " +
			   			   "         TIPO_IMPIANTO, " +
			   			   "         NUMERO_CASTAGNI, " +
			   			   "         GRUPPO, " +
			   			   "         RICADUTA, " +
			   			   "         ID_GIACITURA_UNAR, " +
			   			   "         ID_ROCCIA_UNAR, " +
			   			   "         ID_SCHELETRO_UNAR, " +
			   			   "         ID_STATO_VEGETATIVO_UNAR, " +
			   			   "         ID_POTATURA_UNAR, " +
			   			   "         ID_GIUDIZIO_UNAR, " +
			   			   "         SUPPLEMENTARI, " +
			   			   "         MECCANIZZABILE, " +
			   			   "         DIMENSIONE_CHIOMA, " +
			   			   "         ID_ETA_IMPIANTO_UNAR, " +
			   			   "         PROVINCIA_CCIAA, " +
			   			   "         MATRICOLA_CCIAA, " +
			   			   "         CONFERMA_PREC_ISCRIZIONE_ALBO, " +
			   			   "         RICHIESTA_NUOVA_ISCR_ALBO, " +
			   			   "         CONFERMA_RICH_NUOVA_ISCR_ALBO, " +
			   			   "         SUPERFICIE_DA_ISCRIVERE_ALBO, " +
			   			   "         ANNO_ISCRIZIONE_ALBO, " +
			   			   "         ID_FONTE, " +
			   			   "         ID_VARIAZIONE_UNAR, " +
			   			   "         NOTE, " +
			   			   "         DATA_AGGIORNAMENTO, " +
			   			   "         ID_UTENTE_AGGIORNAMENTO, " +
			   			   "         ID_VARIETA, " +
			   			   "         ID_UTILIZZO, " +
			   			   "         PERCENTUALE_VARIETA, " +
			   			   "         ID_VINO, " +
			   			   "         DATA_ESECUZIONE, " +
			   			   "         RECORD_MODIFICATO, " +
			   			   "         ID_AZIENDA, " +
			   			   "         DATA_CESSAZIONE, " +
			   			   "         ID_CESSAZIONE_UNAR," +
			   			   "         ID_CAUSALE_MODIFICA, " +
			   			   "         ID_TIPOLOGIA_VINO, " +
			   			   "         STATO_UNITA_ARBOREA, " +
			   			   "         ANNO_RIFERIMENTO, " +
			   			   "         COLTURA_SPECIALIZZATA, " +
			   			   "         DATA_ISCRIZIONE_ALBO, " +
			   			   "         ANNO_PRIMA_PRODUZIONE, " +
			   			   "         DATA_PRIMA_PRODUZIONE, " +
			   			   "         VIGNA," +
			   			   "         ID_VIGNA," +
			   			   "         ID_MENZIONE_GEOGRAFICA," +
			   			   "         ID_GENERE_ISCRIZIONE, " +
			   			   "         FLAG_IMPRODUTTIVA, " +
			   			   "         PERCENTUALE_FALLANZA, " +
			   			   "         ETICHETTA, " +
			   			   "         DATA_SOVRAINNESTO, " +
                 "         DATA_INTERVENTO, " +
                 "         ID_TIPO_INTERVENTO_VITICOLO, " +
                 "         ID_FILO_SOSTEGNO, " +
                 "         ID_PALO_TESTATA, " +
                 "         ID_PALO_TESSITURA, " +
                 "         ID_ANCORAGGIO_UNAR, " +
                 "         ID_STATO_COLTIVAZIONE_UNAR, " +
                 "         DISTANZA_PALI, " +
                 "         ALTITUDINE_SLM, " +
                 "         AREA_SERVIZIO, " +
                 "         PERCENTUALE_PENDENZA_MEDIA, " +
                 "         GRADI_PENDENZA_MEDIA, " +
                 "         GRADI_ESPOSIZIONE_MEDIA," +
                 "         METRI_ALTITUDINE_MEDIA, " +
                 "         ID_UNITA_ARBOREA_MADRE, " +
                 "         ID_CATALOGO_MATRICE) " +
			   			   " VALUES  (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
			   			   "          ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
			   			   "          ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
			   			   "          ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
			stmt = conn.prepareStatement(query);
			int idx = 0;
			SolmrLogger.debug(this, "Executing insertStoricoUnitaArborea: "+query);
			
			stmt.setLong(++idx, idStoricoUnitaArborea.longValue());
			SolmrLogger.debug(this, "Value of parameter [ID_STORICO_UNITA_ARBOREA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+idStoricoUnitaArborea.longValue()+"\n");
			stmt.setLong(++idx, storicoUnitaArboreaVO.getIdUnitaArborea().longValue());
			SolmrLogger.debug(this, "Value of parameter [ID_UNITA_ARBOREA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdUnitaArborea().longValue()+"\n");
			stmt.setLong(++idx, storicoUnitaArboreaVO.getIdParticella().longValue());
			SolmrLogger.debug(this, "Value of parameter [ID_PARTICELLA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdParticella().longValue()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getProgrUnar());
			SolmrLogger.debug(this, "Value of parameter [PROGR_UNAR] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getProgrUnar()+"\n");
			stmt.setTimestamp(++idx, new Timestamp(storicoUnitaArboreaVO.getDataInizioValidita().getTime()));
			SolmrLogger.debug(this, "Value of parameter [DATA_INIZIO_VALIDITA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new Timestamp(storicoUnitaArboreaVO.getDataInizioValidita().getTime())+"\n");
			if(storicoUnitaArboreaVO.getDataFineValidita() != null) {
				stmt.setTimestamp(++idx, new Timestamp(storicoUnitaArboreaVO.getDataFineValidita().getTime()));
				SolmrLogger.debug(this, "Value of parameter [DATA_FINE_VALIDITA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new Timestamp(storicoUnitaArboreaVO.getDataFineValidita().getTime())+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [DATA_FINE_VALIDITA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getDataLavorazione() != null) {
				stmt.setDate(++idx, new java.sql.Date(storicoUnitaArboreaVO.getDataLavorazione().getTime()));
				SolmrLogger.debug(this, "Value of parameter [DATA_LAVORAZIONE] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new java.sql.Date(storicoUnitaArboreaVO.getDataLavorazione().getTime())+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [DATA_LAVORAZIONE] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getIdTipologiaUnar() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdTipologiaUnar().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_TIPOLOGIA_UNAR] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdTipologiaUnar().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_TIPOLOGIA_UNAR] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(Validator.isNotEmpty(storicoUnitaArboreaVO.getArea())) {
				stmt.setString(++idx, StringUtils.parseSuperficieField(storicoUnitaArboreaVO.getArea()));
				SolmrLogger.debug(this, "Value of parameter [AREA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getArea()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [AREA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			stmt.setString(++idx, storicoUnitaArboreaVO.getSestoSuFila());
			SolmrLogger.debug(this, "Value of parameter [SESTO_SU_FILA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getSestoSuFila()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getSestoTraFile());
			SolmrLogger.debug(this, "Value of parameter [SESTO_TRA_FILE] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getSestoTraFile()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getNumCeppi());
			SolmrLogger.debug(this, "Value of parameter [NUMERO_CEPPI] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getNumCeppi()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getAnnoImpianto());
			SolmrLogger.debug(this, "Value of parameter [ANNO_IMPIANTO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getAnnoImpianto()+"\n");
			if(storicoUnitaArboreaVO.getDataImpianto() != null) {
        stmt.setDate(++idx, new java.sql.Date(storicoUnitaArboreaVO.getDataImpianto().getTime()));
        SolmrLogger.debug(this, "Value of parameter [DATA_IMPIANTO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new java.sql.Date(storicoUnitaArboreaVO.getDataImpianto().getTime())+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [DATA_IMPIANTO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
			stmt.setString(++idx, storicoUnitaArboreaVO.getAnnoReinnesto());
			SolmrLogger.debug(this, "Value of parameter [ANNO_REINNESTO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getAnnoReinnesto()+"\n");
			if(storicoUnitaArboreaVO.getIdFormaAllevamento() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdFormaAllevamento().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_FORMA_ALLEVAMENTO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdFormaAllevamento().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_FORMA_ALLEVAMENTO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getIdIrrigazioneUnar() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdIrrigazioneUnar().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_IRRIGAZIONE_UNAR] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdIrrigazioneUnar().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_IRRIGAZIONE_UNAR] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getIdColtivazioneUnar() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdColtivazioneUnar().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_COLTIVAZIONE_UNAR] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdColtivazioneUnar().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_COLTIVAZIONE_UNAR] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			stmt.setString(++idx, storicoUnitaArboreaVO.getCodiceTipoVarieta());
			SolmrLogger.debug(this, "Value of parameter [CODICE_TIPO_VARIETA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getCodiceTipoVarieta()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getPresenzaAltriVitigni());
			SolmrLogger.debug(this, "Value of parameter [PRESENZA_ALTRI_VITIGNI] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getPresenzaAltriVitigni()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getNumeroPianteProduttivo());
			SolmrLogger.debug(this, "Value of parameter [NUMERO_PIANTE_PRODUTTIVO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getNumeroPianteProduttivo()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getNumeroAltrePiante());
			SolmrLogger.debug(this, "Value of parameter [NUMERO_ALTRE_PIANTE] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getNumeroAltrePiante()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getCampagna());
			SolmrLogger.debug(this, "Value of parameter [CAMPAGNA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getCampagna()+"\n");
			if(storicoUnitaArboreaVO.getIdTipologiaVigneto() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdTipologiaVigneto().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_TIPOLOGIA_VIGNETO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdTipologiaVigneto().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_TIPOLOGIA_VIGNETO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			stmt.setString(++idx, storicoUnitaArboreaVO.getTipoImpianto());
			SolmrLogger.debug(this, "Value of parameter [TIPO_IMPIANTO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getTipoImpianto()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getNumeroCastagni());
			SolmrLogger.debug(this, "Value of parameter [NUMERO_CASTAGNI] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getNumeroCastagni()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getGruppo());
			SolmrLogger.debug(this, "Value of parameter [GRUPPO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getGruppo()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getRicaduta());
			SolmrLogger.debug(this, "Value of parameter [RICADUTA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getRicaduta()+"\n");
			if(storicoUnitaArboreaVO.getIdGiacituraUnar() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdGiacituraUnar().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_GIACITURA_UNAR] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdGiacituraUnar().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_GIACITURA_UNAR] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getIdRocciaUnar() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdRocciaUnar().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_ROCCIA_UNAR] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdRocciaUnar().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_ROCCIA_UNAR] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getIdScheletroUnar() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdScheletroUnar().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_SCHELETRO_UNAR] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdScheletroUnar().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_SCHELETRO_UNAR] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getIdStatoVegetativoUnar() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdStatoVegetativoUnar().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_STATO_VEGETATIVO_UNAR] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdStatoVegetativoUnar().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_STATO_VEGETATIVO_UNAR] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getIdPotaturaUnar() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdPotaturaUnar().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_POTATURA_UNAR] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdPotaturaUnar().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_POTATURA_UNAR] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getIdGiudizioUnar() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdGiudizioUnar().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_GIUDIZIO_UNAR] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdGiudizioUnar().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_GIUDIZIO_UNAR] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			stmt.setString(++idx, storicoUnitaArboreaVO.getSupplementari());
			SolmrLogger.debug(this, "Value of parameter [SUPPLEMENTARI] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getSupplementari()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getMeccanizzabile());
			SolmrLogger.debug(this, "Value of parameter [MECCANIZZABILE] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getMeccanizzabile()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getDimensioneChioma());
			SolmrLogger.debug(this, "Value of parameter [DIMENSIONE_CHIOMA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getDimensioneChioma()+"\n");
			if(storicoUnitaArboreaVO.getIdEtaImpiantoUnar() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdEtaImpiantoUnar().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_ETA_IMPIANTO_UNAR] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdEtaImpiantoUnar().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_ETA_IMPIANTO_UNAR] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(Validator.isNotEmpty(storicoUnitaArboreaVO.getProvinciaCCIAA())) {
			  stmt.setString(++idx, storicoUnitaArboreaVO.getProvinciaCCIAA());
        SolmrLogger.debug(this, "Value of parameter [PROVINCIA_CCIAA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getProvinciaCCIAA()+"\n");
			}
			else
			{
			  stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [PROVINCIA_CCIAA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(Validator.isNotEmpty(storicoUnitaArboreaVO.getMatricolaCCIAA())) {
			  stmt.setString(++idx, storicoUnitaArboreaVO.getMatricolaCCIAA());
				SolmrLogger.debug(this, "Value of parameter [MATRICOLA_CCIAA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getMatricolaCCIAA()+"\n");
			}
			else {
			  stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [MATRICOLA_CCIAA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			stmt.setString(++idx, storicoUnitaArboreaVO.getConfermaPrecIscrizioneAlbo());
			SolmrLogger.debug(this, "Value of parameter [CONFERMA_PREC_ISCRIZIONE_ALBO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getConfermaPrecIscrizioneAlbo()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getRichiestaNuovaIscrAlbo());
			SolmrLogger.debug(this, "Value of parameter [RICHIESTA_NUOVA_ISCRIZIONE_ALBO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getRichiestaNuovaIscrAlbo()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getConfermaRichNuovaIscrAlbo());
			SolmrLogger.debug(this, "Value of parameter [CONFERMA_RIC_NUOVA_ISCR_ALBO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getConfermaPrecIscrizioneAlbo()+"\n");
			if(Validator.isNotEmpty(storicoUnitaArboreaVO.getSuperficieDaIscrivereAlbo())) {
				stmt.setString(++idx, StringUtils.parseSuperficieField(storicoUnitaArboreaVO.getSuperficieDaIscrivereAlbo()));
			}
			else {
				stmt.setString(++idx, storicoUnitaArboreaVO.getSuperficieDaIscrivereAlbo());
			}
			SolmrLogger.debug(this, "Value of parameter [SUPERFICIE_DA_ISCRIVERE_ALBO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getSuperficieDaIscrivereAlbo()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getAnnoIscrizioneAlbo());
			SolmrLogger.debug(this, "Value of parameter [ANNO_ISCRIZIONE_ALBO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getAnnoIscrizioneAlbo()+"\n");
			if(storicoUnitaArboreaVO.getIdFonte() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdFonte().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_FONTE] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdFonte().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_FONTE] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getIdVariazioneUnar() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdVariazioneUnar().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_VARIAZIONE_UNAR] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdVariazioneUnar().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_VARIAZIONE_UNAR] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			stmt.setString(++idx, storicoUnitaArboreaVO.getNote());
			SolmrLogger.debug(this, "Value of parameter [NOTE] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getNote()+"\n");
			stmt.setTimestamp(++idx, new Timestamp(storicoUnitaArboreaVO.getDataAggiornamento().getTime()));
			SolmrLogger.debug(this, "Value of parameter [DATA_AGGIORNAMENTO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new Timestamp(storicoUnitaArboreaVO.getDataAggiornamento().getTime())+"\n");
			stmt.setLong(++idx, storicoUnitaArboreaVO.getIdUtenteAggiornamento().longValue());
			SolmrLogger.debug(this, "Value of parameter [ID_UTENTE_AGGIORNAMENTO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdUtenteAggiornamento().longValue()+"\n");
			if(storicoUnitaArboreaVO.getIdVarieta() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdVarieta().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_VARIETA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdVarieta().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_VARIETA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getIdUtilizzo() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdUtilizzo().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_UTILIZZO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdUtilizzo().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_UTILIZZO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			stmt.setString(++idx, storicoUnitaArboreaVO.getPercentualeVarieta());
			SolmrLogger.debug(this, "Value of parameter [PERCENTUALE_VARIETA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getPercentualeVarieta()+"\n");
			if(storicoUnitaArboreaVO.getIdVino() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdVino().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_VINO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdVino().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_VINO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getDataEsecuzione() != null) {
				stmt.setTimestamp(++idx, new Timestamp(storicoUnitaArboreaVO.getDataEsecuzione().getTime()));
				SolmrLogger.debug(this, "Value of parameter [DATA_ESECUZIONE] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new Timestamp(storicoUnitaArboreaVO.getDataEsecuzione().getTime())+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [DATA_ESECUZIONE] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			stmt.setString(++idx, storicoUnitaArboreaVO.getRecordModificato());
			SolmrLogger.debug(this, "Value of parameter [RECORD_MODIFICATO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getRecordModificato()+"\n");
			if(storicoUnitaArboreaVO.getIdAzienda() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdAzienda().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_AZIENDA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdAzienda().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_AZIENDA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getDataCessazione() != null) {
				stmt.setTimestamp(++idx, new Timestamp(storicoUnitaArboreaVO.getDataCessazione().getTime()));
				SolmrLogger.debug(this, "Value of parameter [DATA_CESSAZIONE] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new Timestamp(storicoUnitaArboreaVO.getDataCessazione().getTime())+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [DATA_CESSAZIONE] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			if(storicoUnitaArboreaVO.getIdCessazioneUnar() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdCessazioneUnar().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_CESSAZIONE_UNAR] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdCessazioneUnar().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_CESSAZIONE] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			stmt.setLong(++idx, storicoUnitaArboreaVO.getIdCausaleModifica().longValue());
			SolmrLogger.debug(this, "Value of parameter [ID_CAUSALE_MODIFICA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdCausaleModifica().longValue()+"\n");
			if(storicoUnitaArboreaVO.getIdTipologiaVino() != null) {
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdTipologiaVino().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_TIPOLOGIA_VINO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdTipologiaVino().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this, "Value of parameter [ID_TIPOLOGIA_VINO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
			}
			stmt.setString(++idx, storicoUnitaArboreaVO.getStatoUnitaArborea());
			SolmrLogger.debug(this, "Value of parameter [STATO_UNITA_ARBOREA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getStatoUnitaArborea()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getAnnoRiferimento());
			SolmrLogger.debug(this, "Value of parameter [ANNO_RIFERIMENTO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getAnnoRiferimento()+"\n");
			stmt.setString(++idx, storicoUnitaArboreaVO.getColturaSpecializzata());
			SolmrLogger.debug(this, "Value of parameter [COLTURA_SPECIALIZZATA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getColturaSpecializzata()+"\n");
			if(storicoUnitaArboreaVO.getDataIscrizioneAlbo() != null) {
        stmt.setTimestamp(++idx, new Timestamp(storicoUnitaArboreaVO.getDataIscrizioneAlbo().getTime()));
        SolmrLogger.debug(this, "Value of parameter [DATA_ISCRIZIONE_ALBO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new Timestamp(storicoUnitaArboreaVO.getDataIscrizioneAlbo().getTime())+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [DATA_ISCRIZIONE_ALBO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
			
      stmt.setString(++idx, storicoUnitaArboreaVO.getAnnoPrimaProduzione());
      SolmrLogger.debug(this, "Value of parameter [ANNO_PRIMA_PRODUZIONE] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getAnnoPrimaProduzione()+"\n");
      if(storicoUnitaArboreaVO.getDataPrimaProduzione() != null) {
        stmt.setTimestamp(++idx, new Timestamp(storicoUnitaArboreaVO.getDataPrimaProduzione().getTime()));
        SolmrLogger.debug(this, "Value of parameter [DATA_PRIMA_PRODUZIONE] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new Timestamp(storicoUnitaArboreaVO.getDataPrimaProduzione().getTime())+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [DATA_PRIMA_PRODUZIONE] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
      stmt.setString(++idx, storicoUnitaArboreaVO.getVigna());
      SolmrLogger.debug(this, "Value of parameter [VIGNA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getVigna()+"\n");
      if(storicoUnitaArboreaVO.getIdVigna() != null) {
        stmt.setLong(++idx, storicoUnitaArboreaVO.getIdVigna().longValue());
        SolmrLogger.debug(this, "Value of parameter [ID_VIGNA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdVigna()+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [ID_VIGNA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
      if(storicoUnitaArboreaVO.getIdMenzioneGeografica() != null) {
        stmt.setLong(++idx, storicoUnitaArboreaVO.getIdMenzioneGeografica().longValue());
        SolmrLogger.debug(this, "Value of parameter [ID_MENZIONE_GEOGRAFICA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdMenzioneGeografica()+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [ID_MENZIONE_GEOGRAFICA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }   
      if(storicoUnitaArboreaVO.getIdGenereIscrizione() != null) {
        stmt.setLong(++idx, storicoUnitaArboreaVO.getIdGenereIscrizione().longValue());
        SolmrLogger.debug(this, "Value of parameter [ID_GENERE_ISCRIZIONE] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdGenereIscrizione()+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [ID_GENERE_ISCRIZIONE] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
      stmt.setString(++idx, storicoUnitaArboreaVO.getFlagImproduttiva());
      SolmrLogger.debug(this, "Value of parameter [FLAG_IMPRODUTTIVA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getFlagImproduttiva()+"\n");
      stmt.setBigDecimal(++idx, storicoUnitaArboreaVO.getPercentualeFallanza());
      SolmrLogger.debug(this, "Value of parameter [PERCENTUALE_FALLANZA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getPercentualeFallanza()+"\n");
      stmt.setString(++idx, storicoUnitaArboreaVO.getEtichetta());
      SolmrLogger.debug(this, "Value of parameter [ETICHETTA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getEtichetta()+"\n");
      if(storicoUnitaArboreaVO.getDataSovrainnesto() != null) {
        stmt.setDate(++idx, new java.sql.Date(storicoUnitaArboreaVO.getDataSovrainnesto().getTime()));
        SolmrLogger.debug(this, "Value of parameter [DATA_SOVRAINNESTO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new java.sql.Date(storicoUnitaArboreaVO.getDataSovrainnesto().getTime())+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [DATA_SOVRAINNESTO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
      if(storicoUnitaArboreaVO.getDataIntervento() != null) {
        stmt.setDate(++idx, new java.sql.Date(storicoUnitaArboreaVO.getDataIntervento().getTime()));
        SolmrLogger.debug(this, "Value of parameter [DATA_INTERVENTO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new java.sql.Date(storicoUnitaArboreaVO.getDataIntervento().getTime())+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [DATA_INTERVENTO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
      if(storicoUnitaArboreaVO.getIdTipoInterventoViticolo() != null) {
        stmt.setLong(++idx, storicoUnitaArboreaVO.getIdTipoInterventoViticolo().longValue());
        SolmrLogger.debug(this, "Value of parameter [ID_TIPO_INTERVENTO_VITICOLO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdTipoInterventoViticolo()+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [ID_TIPO_INTERVENTO_VITICOLO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
      if(storicoUnitaArboreaVO.getIdFiloSostegno() != null) {
        stmt.setLong(++idx, storicoUnitaArboreaVO.getIdFiloSostegno());
        SolmrLogger.debug(this, "Value of parameter [ID_FILO_SOSTEGNO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdFiloSostegno()+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [ID_FILO_SOSTEGNO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
      if(storicoUnitaArboreaVO.getIdPaloTestata() != null) {
        stmt.setLong(++idx, storicoUnitaArboreaVO.getIdPaloTestata());
        SolmrLogger.debug(this, "Value of parameter [ID_PALO_TESTATA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdPaloTestata()+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [ID_PALO_TESTATA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
      if(storicoUnitaArboreaVO.getIdPaloTessitura() != null) {
        stmt.setLong(++idx, storicoUnitaArboreaVO.getIdPaloTessitura());
        SolmrLogger.debug(this, "Value of parameter [ID_PALO_TESSITURA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdPaloTessitura()+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [ID_PALO_TESSITURA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
      if(storicoUnitaArboreaVO.getIdAncoraggioUnar() != null) {
        stmt.setLong(++idx, storicoUnitaArboreaVO.getIdAncoraggioUnar());
        SolmrLogger.debug(this, "Value of parameter [ID_ANCORAGGIO_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdAncoraggioUnar()+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [ID_ANCORAGGIO_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
      if(storicoUnitaArboreaVO.getIdStatoColtivazioneUnar() != null) {
        stmt.setLong(++idx, storicoUnitaArboreaVO.getIdStatoColtivazioneUnar());
        SolmrLogger.debug(this, "Value of parameter [ID_STATO_COLTIVAZIONE_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdStatoColtivazioneUnar()+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [ID_STATO_COLTIVAZIONE_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
      if(storicoUnitaArboreaVO.getDistanzaPali() != null) {
        stmt.setLong(++idx, storicoUnitaArboreaVO.getDistanzaPali());
        SolmrLogger.debug(this, "Value of parameter [DISTANZA_PALI] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getDistanzaPali()+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [DISTANZA_PALI] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
      if(storicoUnitaArboreaVO.getAltitudineSlm() != null) {
        stmt.setLong(++idx, storicoUnitaArboreaVO.getAltitudineSlm());
        SolmrLogger.debug(this, "Value of parameter [ALTITUDINE_SLM] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getAltitudineSlm()+"\n");
      }
      else {
        stmt.setString(++idx, null);
        SolmrLogger.debug(this, "Value of parameter [ALTITUDINE_SLM] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
      }
      stmt.setBigDecimal(++idx, storicoUnitaArboreaVO.getAreaServizio());
      
      stmt.setBigDecimal(++idx, storicoUnitaArboreaVO.getPercentualePendenzaMedia());
      stmt.setBigDecimal(++idx, storicoUnitaArboreaVO.getGradiPendenzaMedia());
      stmt.setBigDecimal(++idx, storicoUnitaArboreaVO.getGradiEsposizioneMedia());
      stmt.setBigDecimal(++idx, convertIntegerToBigDecimal(storicoUnitaArboreaVO.getMetriAltitudineMedia()));
      stmt.setBigDecimal(++idx, convertLongToBigDecimal(storicoUnitaArboreaVO.getIdUnitaArboreaMadre()));
      stmt.setBigDecimal(++idx, convertLongToBigDecimal(storicoUnitaArboreaVO.getIdCatalogoMatrice()));
      
      
      
			stmt.executeUpdate();

			stmt.close();
	    }
	    catch(SQLException exc) {
	    	SolmrLogger.error(this, "insertStoricoUnitaArborea in StoricoUnitaArboreaDAO - SQLException: "+exc);
	    	throw new DataAccessException(exc.getMessage());
	    }
	    catch (Exception ex) {
	    	SolmrLogger.error(this, "insertStoricoUnitaArborea in StoricoUnitaArboreaDAO - Generic Exception: "+ex);
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
	    		SolmrLogger.error(this, "insertStoricoUnitaArborea in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc);
	    		throw new DataAccessException(exc.getMessage());
	    	}
	    	catch(Exception ex) {
	    		SolmrLogger.error(this, "insertStoricoUnitaArborea in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex);
	    		throw new DataAccessException(ex.getMessage());
	    	}
	    }
	    SolmrLogger.debug(this, "Invocated insertStoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
	    return idStoricoUnitaArborea;
	}
	
	/**
	 * Metodo che mi consente di recuperare l'elenco delle particelle importabili
	 * 
	 * @param idAzienda
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
	 * @throws DataAccessException
	 */
	public StoricoParticellaVO[] getListStoricoParticelleArboreeImportabili(Long idAzienda, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListStoricoParticelleArboreeImportabili method in StoricoUnitaArboreaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<StoricoParticellaVO> elencoUnitaArboreeImportabili = new Vector<StoricoParticellaVO>();
		//serve per evitare di mettere due volte lo stesso id_unita_arborea
		//in caso di doppio tengo quello che ha data_fine_validita a null
		TreeMap<Long, StoricoParticellaVO> tIdUnitaArborea = new TreeMap<Long, StoricoParticellaVO>();

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in getListStoricoParticelleArboreeImportabili method in StoricoUnitaArboreaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListStoricoParticelleArboreeImportabili method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");

			String query = " SELECT  * FROM ( " +
					       "SELECT  1 AS IMPORT, " +
					       "        SUA.ID_UNITA_ARBOREA, " +
					       "        SUA.ID_STORICO_UNITA_ARBOREA, " +
					       "        AA.CUAA, " +
					       "        AA.DENOMINAZIONE, " +
					       "        (SELECT I.CODICE_FISCALE " +
					       "         FROM   DB_INTERMEDIARIO I, " +
					       "                DB_DELEGA D " +
					       "         WHERE  I.ID_INTERMEDIARIO = D.ID_INTERMEDIARIO " +          
					       "         AND    D.ID_AZIENDA(+) = SUA.ID_AZIENDA " +          
					       "         AND    D.DATA_FINE IS NULL) CAA, " +         
					       "        SP.ID_STORICO_PARTICELLA, " +         
					       "        C.DESCOM, " +         
					       "        P.SIGLA_PROVINCIA, " +         
					       "        SP.SEZIONE, " +         
					       "        SP.FOGLIO, " +         
					       "        SP.PARTICELLA, " +         
					       "        SP.SUBALTERNO, " +         
					       "        SP.SUP_CATASTALE, " +
					       "        SP.SUPERFICIE_GRAFICA, " +
					       "        SUA.PROGR_UNAR, " +         
					       "        SUA.ID_UTILIZZO, " +         
					       "        TU.CODICE, " +         
					       "        TU.DESCRIZIONE DEST_PROD, " +         
					       "        SUA.ID_VARIETA, " +         
					       "        TV.CODICE_VARIETA, " +         
					       "        TV.DESCRIZIONE VITIGNO, " +         
					       "        SUA.AREA, " +         
					       "        SUA.ANNO_IMPIANTO, " +
					       "        SUA.DATA_IMPIANTO, " +
					       "        SUA.DATA_PRIMA_PRODUZIONE, " +
					       "        SUA.SESTO_SU_FILA, " +         
					       "        SUA.SESTO_TRA_FILE, " +         
					       "        SUA.NUM_CEPPI, " +         
					       "        SUA.ID_FORMA_ALLEVAMENTO, " +         
					       "        TFA.DESCRIZIONE AS DESC_ALLEVAMENTO, " +         
					       "        SUA.PERCENTUALE_VARIETA, " +         
					       "        SUA.PRESENZA_ALTRI_VITIGNI, " +         
					       "        SUA.DATA_CESSAZIONE " +
						     "FROM    DB_CONDUZIONE_PARTICELLA CP, " + 
						     "        DB_UTE U, " + 
						     "        DB_STORICO_UNITA_ARBOREA SUA, " + 
						     "        DB_STORICO_PARTICELLA SP, " +
						     "        DB_ANAGRAFICA_AZIENDA AA, " + 
						     "        COMUNE C, " + 
						     "        PROVINCIA P, " + 
						     "        DB_TIPO_UTILIZZO TU, " + 
						     "        DB_TIPO_VARIETA TV, " + 
						     "        DB_TIPO_FORMA_ALLEVAMENTO TFA " + 
						     "WHERE   CP.ID_UTE = U.ID_UTE " +
						     "AND     U.ID_AZIENDA =  ? " +
						     "AND     CP.DATA_FINE_CONDUZIONE IS NULL " +
						     "AND     CP.ID_TITOLO_POSSESSO <> 5 " +
						     "AND     CP.ID_TITOLO_POSSESSO <> 6 " +
						     "AND     U.DATA_FINE_ATTIVITA IS NULL " +
						     "AND     CP.ID_PARTICELLA = SUA.ID_PARTICELLA " +
						     "AND     NVL(SUA.ID_AZIENDA,0) <> ? " +
						     "AND     SUA.ID_PARTICELLA = SP.ID_PARTICELLA " +
						     "AND     SP.DATA_FINE_VALIDITA IS NULL " +	
						     "AND     NVL(SUA.ID_UTILIZZO,0) = TU.ID_UTILIZZO(+) " +
						     "AND     NVL(SUA.ID_VARIETA,0) = TV.ID_VARIETA(+) " +
						     "AND     NVL(SUA.ID_AZIENDA,0) = AA.ID_AZIENDA(+) " +
						     "AND     AA.DATA_FINE_VALIDITA(+) IS NULL " +
						     "AND     AA.DATA_CESSAZIONE(+) IS NULL " +
						     "AND     SP.COMUNE = C.ISTAT_COMUNE " +
						     "AND     NVL(SUA.ID_FORMA_ALLEVAMENTO,0) = TFA.ID_FORMA_ALLEVAMENTO(+) " +
						     "AND     P.ISTAT_PROVINCIA = C.ISTAT_PROVINCIA " +
						     "AND     (SUA.DATA_FINE_VALIDITA IS NULL OR SUA.ID_CESSAZIONE_UNAR IS NOT NULL)) " +
						     "GROUP BY" +
						     "        IMPORT , " +
						     "        ID_UNITA_ARBOREA, " +
						     "        ID_STORICO_UNITA_ARBOREA, " +
						     "        CUAA, " +
						     "        DENOMINAZIONE, " +
						     "        CAA, " +
						     "        ID_STORICO_PARTICELLA, " +         
						     "        DESCOM, " +         
						     "        SIGLA_PROVINCIA, " +         
						     "        SEZIONE, " +         
						     "        FOGLIO, " +         
						     "        PARTICELLA, " +         
						     "        SUBALTERNO, " +         
						     "        SUP_CATASTALE, " +
						     "        SUPERFICIE_GRAFICA, " +
						     "        PROGR_UNAR, " +          
						     "        ID_UTILIZZO, " +         
						     "        CODICE, " +         
						     "        DEST_PROD , " +         
						     "        ID_VARIETA, " +         
						     "        CODICE_VARIETA, " +         
						     "        VITIGNO , " +         
						     "        AREA, " +         
						     "        ANNO_IMPIANTO, " +
						     "        DATA_IMPIANTO, " + 
						     "        DATA_PRIMA_PRODUZIONE, " + 
						     "        SESTO_SU_FILA, " +         
						     "        SESTO_TRA_FILE, " +         
						     "        NUM_CEPPI, " +         
						     "        ID_FORMA_ALLEVAMENTO, " +         
						     "        DESC_ALLEVAMENTO , " +         
						     "        PERCENTUALE_VARIETA, " +          
						     "        PRESENZA_ALTRI_VITIGNI, " +         
						     "        DATA_CESSAZIONE " +
						     "UNION " +    
						     "SELECT 0 AS IMPORT, " +
						     "       SUA.ID_UNITA_ARBOREA, " +
						     "       SUA.ID_STORICO_UNITA_ARBOREA, " +
						     "       AA.CUAA, " +
						     "       AA.DENOMINAZIONE, " +
						     "       (SELECT I.CODICE_FISCALE " +
						     "        FROM   DB_INTERMEDIARIO I, " +
						     "               DB_DELEGA D " +
						     "        WHERE  I.ID_INTERMEDIARIO = D.ID_INTERMEDIARIO " +
						     "        AND    D.ID_AZIENDA(+) = SUA.ID_AZIENDA " +
						     "        AND    D.DATA_FINE IS NULL) CAA, " +
						     "       SP.ID_STORICO_PARTICELLA, " +
						     "       C.DESCOM, " +
						     "       P.SIGLA_PROVINCIA, " +
						     "       SP.SEZIONE, " +
						     "       SP.FOGLIO, " +
						     "       SP.PARTICELLA, " +
						     "       SP.SUBALTERNO, " +
						     "       SP.SUP_CATASTALE, " +
						     "       SP.SUPERFICIE_GRAFICA, " +
						     "       SUA.PROGR_UNAR, " +
						     "       SUA.ID_UTILIZZO, " +
						     "       TU.CODICE, " +
						     "       TU.DESCRIZIONE DEST_PROD, " +
						     "       SUA.ID_VARIETA, " +
						     "       TV.CODICE_VARIETA, " +
						     "       TV.DESCRIZIONE VITIGNO, " +
						     "       SUA.AREA, " +
						     "       SUA.ANNO_IMPIANTO, " +
						     "       SUA.DATA_IMPIANTO, " +
						     "       SUA.DATA_PRIMA_PRODUZIONE, " +
						     "       SUA.SESTO_SU_FILA, " +
						     "       SUA.SESTO_TRA_FILE, " +
						     "       SUA.NUM_CEPPI, " +
						     "       SUA.ID_FORMA_ALLEVAMENTO, " +
						     "       TFA.DESCRIZIONE AS DESC_ALLEVAMENTO, " +
						     "       SUA.PERCENTUALE_VARIETA, " +
						     "       SUA.PRESENZA_ALTRI_VITIGNI, " +
						     "       SUA.DATA_CESSAZIONE " +
						     "FROM   DB_STORICO_UNITA_ARBOREA SUA, " +
						     "       DB_STORICO_PARTICELLA SP, " +
						     "       COMUNE C, " +
						     "       PROVINCIA P, " +
						     "       DB_TIPO_UTILIZZO TU, " +
						     "       DB_TIPO_VARIETA TV, " +
						     "       DB_TIPO_FORMA_ALLEVAMENTO TFA, " +
						     "       DB_ANAGRAFICA_AZIENDA AA " +
						     "WHERE  SUA.ID_AZIENDA = ? " +
						     "AND    SUA.ID_PARTICELLA = SP.ID_PARTICELLA " +
						     "AND    AA.ID_AZIENDA = ? " +
						     "AND    AA.DATA_FINE_VALIDITA IS NULL " +
						     "AND    SP.DATA_FINE_VALIDITA IS NULL " +
						     "AND    SUA.DATA_FINE_VALIDITA IS NULL " +
						     "AND    C.ISTAT_COMUNE = SP.COMUNE " +
						     "AND    P.ISTAT_PROVINCIA = C.ISTAT_PROVINCIA " +
						     "AND    NVL(SUA.ID_UTILIZZO,0) = TU.ID_UTILIZZO(+) " +
						     "AND    NVL(SUA.ID_VARIETA,0) = TV.ID_VARIETA(+) " +
						     "AND    NVL(SUA.ID_FORMA_ALLEVAMENTO,0) = TFA.ID_FORMA_ALLEVAMENTO(+) " +
						     "AND    SUA.ID_PARTICELLA NOT IN " +
						     "                         (SELECT ID_PARTICELLA " +
						     "                          FROM   DB_CONDUZIONE_PARTICELLA CP, " +
						     "                                 DB_UTE U " +
						     "                          WHERE  CP.ID_UTE = U.ID_UTE " +
						     "                          AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
						     "                          AND    U.ID_AZIENDA = ? " +
						     "                          AND    U.DATA_FINE_ATTIVITA IS NULL)" ;
			
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

			SolmrLogger.debug(this, "Value of parameter 1/2 [ID_AZIENDA] in getListStoricoParticelleArboreeImportabili method in StoricoUnitaArboreaDAO: "+idAzienda+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());
			stmt.setLong(2, idAzienda.longValue());
			stmt.setLong(3, idAzienda.longValue());
			stmt.setLong(4, idAzienda.longValue());
			stmt.setLong(5, idAzienda.longValue());

			SolmrLogger.debug(this, "Executing getListStoricoParticelleArboreeImportabili: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) 
			{
				StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
				StoricoUnitaArboreaVO storicoUnitaArboreaVO = new StoricoUnitaArboreaVO();
				ComuneVO comuneVO = new ComuneVO();
				storicoUnitaArboreaVO.setImportabile(rs.getString("IMPORT"));
				storicoUnitaArboreaVO.setIdUnitaArborea(checkLong(rs.getString("ID_UNITA_ARBOREA")));
				storicoUnitaArboreaVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
				storicoUnitaArboreaVO.setCuaa(rs.getString("CUAA"));
				storicoUnitaArboreaVO.setDenominazione(rs.getString("DENOMINAZIONE"));
				storicoUnitaArboreaVO.setCodFiscaleIntermediario(rs.getString("CAA"));
				storicoParticellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
				comuneVO.setDescom(rs.getString("DESCOM"));
				comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
				storicoParticellaVO.setComuneParticellaVO(comuneVO);
				storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
				storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
				storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
				storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
				storicoParticellaVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
				storicoParticellaVO.setSuperficieGrafica(rs.getString("SUPERFICIE_GRAFICA"));
				storicoUnitaArboreaVO.setProgrUnar(rs.getString("PROGR_UNAR"));
				if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO"))) {
					storicoUnitaArboreaVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
					TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
					tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
					tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
					tipoUtilizzoVO.setDescrizione(rs.getString("DEST_PROD"));
					storicoUnitaArboreaVO.setTipoUtilizzoVO(tipoUtilizzoVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) {
					storicoUnitaArboreaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
					TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
					tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
					tipoVarietaVO.setDescrizione(rs.getString("VITIGNO"));
					tipoVarietaVO.setCodiceVarieta(rs.getString("CODICE_VARIETA"));
					storicoUnitaArboreaVO.setTipoVarietaVO(tipoVarietaVO);
				}
				storicoUnitaArboreaVO.setArea(rs.getString("AREA"));
				storicoUnitaArboreaVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
				storicoUnitaArboreaVO.setDataImpianto(rs.getDate("DATA_IMPIANTO"));
				storicoUnitaArboreaVO.setDataPrimaProduzione(rs.getDate("DATA_PRIMA_PRODUZIONE"));
				storicoUnitaArboreaVO.setSestoSuFila(rs.getString("SESTO_SU_FILA"));
				storicoUnitaArboreaVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
				storicoUnitaArboreaVO.setNumCeppi(rs.getString("NUM_CEPPI"));
				if(Validator.isNotEmpty(rs.getString("ID_FORMA_ALLEVAMENTO"))) {
					storicoUnitaArboreaVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
					TipoFormaAllevamentoVO tipoFormaAllevamentoVO = new TipoFormaAllevamentoVO();
					tipoFormaAllevamentoVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
					tipoFormaAllevamentoVO.setDescrizione(rs.getString("DESC_ALLEVAMENTO"));
					storicoUnitaArboreaVO.setTipoFormaAllevamentoVO(tipoFormaAllevamentoVO);
				}
				storicoUnitaArboreaVO.setPercentualeVarieta(rs.getString("PERCENTUALE_VARIETA"));
				storicoUnitaArboreaVO.setPresenzaAltriVitigni(rs.getString("PRESENZA_ALTRI_VITIGNI"));
				storicoUnitaArboreaVO.setDataCessazione(rs.getDate("DATA_CESSAZIONE"));
				storicoParticellaVO.setStoricoUnitaArboreaVO(storicoUnitaArboreaVO);
				
				if(tIdUnitaArborea.get(storicoUnitaArboreaVO.getIdUnitaArborea()) != null)
				{
				  if(Validator.isEmpty(storicoUnitaArboreaVO.getDataFineValidita()))
				  {
				    tIdUnitaArborea.put(storicoUnitaArboreaVO.getIdUnitaArborea(), storicoParticellaVO);
				  }
				}
				else
				{
				  tIdUnitaArborea.put(storicoUnitaArboreaVO.getIdUnitaArborea(), storicoParticellaVO);
				}
				
				
				
				
				//elencoUnitaArboreeImportabili.add(storicoParticellaVO);
			}
			
			
			Collection<StoricoParticellaVO> c = tIdUnitaArborea.values();

	    //obtain an Iterator for Collection
	    Iterator<StoricoParticellaVO> itr = c.iterator();

	    //iterate through TreeMap values iterator
	    while(itr.hasNext())
	    {
	      elencoUnitaArboreeImportabili.add(itr.next());
	    }
			
			
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListStoricoParticelleArboreeImportabili in StoricoUnitaArboreaDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListStoricoParticelleArboreeImportabili in StoricoUnitaArboreaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListStoricoParticelleArboreeImportabili in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListStoricoParticelleArboreeImportabili in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListStoricoParticelleArboreeImportabili method in StoricoUnitaArboreaDAO\n");
		if(elencoUnitaArboreeImportabili.size() == 0) {
			return (StoricoParticellaVO[])elencoUnitaArboreeImportabili.toArray(new StoricoParticellaVO[0]);
		}
		else {
			return (StoricoParticellaVO[])elencoUnitaArboreeImportabili.toArray(new StoricoParticellaVO[elencoUnitaArboreeImportabili.size()]);
		}
	}
	
	/**
	 * Metodo utilizzato per recuperare il progressivo UNAR inseribile
	 *  
	 * @param idAzienda
	 * @return java.lang.String
	 * @throws DataAccessException
	 */
	public String getProgressivoUnar(Long idAzienda) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getProgressivoUnar method in StoricoUnitaArboreaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		String progrUnar = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in getProgressivoUnar method in StoricoUnitaArboreaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getProgressivoUnar method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");
			
			String query = " SELECT NVL(MAX(PROGR_UNAR),0) + 1 AS PROGR_UNAR " +
			        	   " FROM   DB_STORICO_UNITA_ARBOREA  " +
			        	   " WHERE  ID_AZIENDA = ? ";
			
			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getProgressivoUnar method in StoricoUnitaArboreaDAO: "+idAzienda+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());

			SolmrLogger.debug(this, "Executing getProgressivoUnar: "+query+"\n");

			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()) {
				progrUnar = rs.getString("PROGR_UNAR");
			}
			
			rs.close();
			stmt.close();
		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getProgressivoUnar in StoricoUnitaArboreaDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getProgressivoUnar in StoricoUnitaArboreaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getProgressivoUnar in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getProgressivoUnar in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getProgressivoUnar method in StoricoUnitaArboreaDAO\n");
		return progrUnar;
	}
	
	/**
	 * Metodo utilizzato per ricercare le UV
	 * nella stampa
	 * 
	 * @param idAzienda
	 * @param filtriUnitaArboreaRicercaVO
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
	 * @throws DataAccessException
	 */
	public StoricoParticellaVO[] searchStoricoUnitaArboreaByParametersForStampa(Long idAzienda, FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating searchStoricoUnitaArboreaByParametersForStampa method in StoricoUnitaArboreaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<StoricoParticellaVO> elencoParticelleArboree = new Vector<StoricoParticellaVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in searchStoricoUnitaArboreaByParametersForStampa method in StoricoUnitaArboreaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in searchStoricoUnitaArboreaByParametersForStampa method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");
			
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
						   "        P.ISTAT_PROVINCIA, " +
						   "        SP.SEZIONE, " +
						   "        SP.FOGLIO, " +
						   "        SP.PARTICELLA, " +
						   "        SP.SUBALTERNO, " +
						   "        SP.SUP_CATASTALE, " +
						   "        PART.FLAG_SCHEDARIO, " +
					     "        SUA.ID_STORICO_UNITA_ARBOREA, " +
					     "        SUA.ID_CATALOGO_MATRICE, " +
						   "        SUA.ID_UNITA_ARBOREA, " +
						   "        SUA.ID_PARTICELLA, " +
						   "        SUA.PROGR_UNAR, " +
						   "        SUA.DATA_INIZIO_VALIDITA, " +
						   "        SUA.DATA_FINE_VALIDITA, " +
						   "        SUA.DATA_LAVORAZIONE, " +
						   "        SUA.ID_TIPOLOGIA_UNAR, " +
						   "        TTU.DESCRIZIONE AS DESC_TIPO_UNAR, " +
						   "        SUA.AREA, " +
						   "        SUA.SESTO_SU_FILA, " +
						   "        SUA.SESTO_TRA_FILE, " +
						   "        SUA.NUM_CEPPI, " +
						   "        SUA.ANNO_IMPIANTO, " +
						   "        SUA.DATA_IMPIANTO, " +
						   "        SUA.ANNO_REINNESTO, " +
						   "        SUA.ID_FORMA_ALLEVAMENTO, " +
						   "        TFA.DESCRIZIONE AS DESC_FORMA_ALLEVAMENTO, " +
						   "        SUA.ID_IRRIGAZIONE_UNAR, " +
						   "        SUA.ID_COLTIVAZIONE_UNAR, " +
						   "        SUA.CODICE_TIPO_VARIETA, " +
						   "        SUA.PRESENZA_ALTRI_VITIGNI, " +
						   "        SUA.NUMERO_PIANTE_PRODUTTIVO, " +
						   "        SUA.NUMERO_ALTRE_PIANTE, " +
						   "        SUA.CAMPAGNA, " +
						   "        SUA.ID_TIPOLOGIA_VIGNETO, " +
						   "        SUA.TIPO_IMPIANTO, " +
						   "        SUA.NUMERO_CASTAGNI, " +
						   "        SUA.GRUPPO, " +
						   "        SUA.RICADUTA, " +
						   "        SUA.ID_GIACITURA_UNAR, " +
						   "        SUA.ID_ROCCIA_UNAR, " +
						   "        SUA.ID_SCHELETRO_UNAR, " +
						   "        SUA.ID_STATO_VEGETATIVO_UNAR, " +
						   "        SUA.ID_POTATURA_UNAR, " +
						   "        SUA.ID_GIUDIZIO_UNAR, " +
						   "        SUA.SUPPLEMENTARI, " +
						   "        SUA.MECCANIZZABILE, " +
						   "        SUA.DIMENSIONE_CHIOMA, " +
						   "        SUA.ID_ETA_IMPIANTO_UNAR, " +
						   "        SUA.PROVINCIA_CCIAA, " +
						   "        SUA.MATRICOLA_CCIAA, " +
						   "        SUA.CONFERMA_PREC_ISCRIZIONE_ALBO, " +
						   "        SUA.RICHIESTA_NUOVA_ISCR_ALBO, " +
						   "        SUA.CONFERMA_RICH_NUOVA_ISCR_ALBO, " +
						   "        SUA.SUPERFICIE_DA_ISCRIVERE_ALBO, " +
						   "        SUA.ANNO_ISCRIZIONE_ALBO, " +
						   "        SUA.ID_FONTE, " +
						   "        SUA.ID_VARIAZIONE_UNAR, " +
						   "        SUA.NOTE, " +
						   "        SUA.DATA_AGGIORNAMENTO, " +
						   "        SUA.ID_UTENTE_AGGIORNAMENTO, " +
						   "        RCM.ID_VARIETA, " +
						   "        TVAR.DESCRIZIONE AS DESC_VARIETA, " +
						   "        TVAR.CODICE_VARIETA AS COD_VAR, " +
						   "        RCM.ID_UTILIZZO, " +
						   "        TU.CODICE, " +
						   "        TU.DESCRIZIONE AS DESC_TIPO_UTILIZZO, " +
						   "        SUA.PERCENTUALE_VARIETA, " +
						   "        SUA.DATA_ESECUZIONE, " +
						   "        SUA.RECORD_MODIFICATO, " +
						   "        SUA.ESITO_CONTROLLO, " +
						   "        SUA.ID_TIPOLOGIA_VINO, " +
               "        TTV.DESCRIZIONE AS DESC_TIPO_VINO, " +
						   "        PC.ID_PARTICELLA_CERTIFICATA, "+
						   "        PC.SUP_COLT_ARBOREA_SPECIALIZZATA, " +
						   "        PC.SUP_GRAFICA, " +
						   "        PC.SUP_USO_GRAFICA, " +
						   "        SUA.STATO_UNITA_ARBOREA, " +
						   "        SUA.ANNO_RIFERIMENTO, " +
						   "        SUA.VIGNA " +
						   " FROM   DB_STORICO_PARTICELLA SP, " +
						   "        COMUNE C, " +
						   "        PROVINCIA P, " +
						   "        DB_PARTICELLA PART, " +
						   "        DB_STORICO_UNITA_ARBOREA SUA, " +
						   "        DB_R_CATALOGO_MATRICE RCM, " +
						   "        DB_TIPO_TIPOLOGIA_UNAR TTU, " +
						   "        DB_TIPO_FORMA_ALLEVAMENTO TFA, " + 
						   "        DB_TIPO_VARIETA TVAR, " +
						   "        DB_TIPO_UTILIZZO TU, " +
						   "        DB_UTE U, " +
						   "        DB_CONDUZIONE_PARTICELLA CP, " +
						   "        DB_TIPO_TITOLO_POSSESSO TTP, " +
						   "        DB_PARTICELLA_CERTIFICATA PC, " +
						   "        DB_TIPO_TIPOLOGIA_VINO TTV " +
						   " WHERE  SUA.ID_PARTICELLA = SP.ID_PARTICELLA " +
						   " AND    SP.ID_PARTICELLA = PART.ID_PARTICELLA " +
						   " AND    SP.COMUNE = C.ISTAT_COMUNE " +
						   " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
					     " AND    SUA.ID_TIPOLOGIA_UNAR = TTU.ID_TIPOLOGIA_UNAR(+) " +
			         " AND    SUA.ID_FORMA_ALLEVAMENTO = TFA.ID_FORMA_ALLEVAMENTO(+) " +
			         " AND    SUA.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE(+) " +
			         " AND    RCM.ID_VARIETA = TVAR.ID_VARIETA(+) " +
			         " AND    RCM.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
			         " AND    SUA.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO(+) " +
						   " AND    SUA.ID_AZIENDA = ? " +
						   " AND    SUA.ID_AZIENDA = U.ID_AZIENDA " +
               " AND    U.ID_UTE = CP.ID_UTE " +
               " AND    CP.ID_PARTICELLA = SUA.ID_PARTICELLA " +
               " AND    CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO " +
               " AND    U.DATA_FINE_ATTIVITA IS NULL " +
               " AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
               " AND    SP.DATA_FINE_VALIDITA IS NULL " +
               " AND    SP.COMUNE = PC.COMUNE(+) " +
               " AND    NVL(SP.SEZIONE, '-') = NVL(PC.SEZIONE(+), '-') " +
               " AND    SP.FOGLIO = PC.FOGLIO(+) " +
               " AND    SP.PARTICELLA = PC.PARTICELLA(+) " +
               " AND    NVL(SP.SUBALTERNO, '-') = NVL(PC.SUBALTERNO(+), '-') " +
               " AND    PC.DATA_FINE_VALIDITA(+) IS NULL ";
			if(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() < 0) {
				query +=   " AND    SUA.DATA_FINE_VALIDITA IS NULL ";
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
	    		  filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue()!=new Long(-1)) {
	    	  
			  //IdTipologiaVino()==-1 = qualunque tipologia di vino
			  //IdTipologiaVino()==0 = senza tipologia di vino
	    	  
			if(filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue()==new Long(0)){
			  //senza tipologia di vino
				query += " AND      SUA.ID_TIPOLOGIA_VINO IS NULL ";
			  
	    	}
	    	else query += " AND      SUA.ID_TIPOLOGIA_VINO = ? ";
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
				query += " AND (SUA.ESITO_CONTROLLO = ? ";
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
				query += " SUA.ESITO_CONTROLLO = ? ";
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
				query += " SUA.ESITO_CONTROLLO = ? ";
				isFirst = false;
			}
			if(!isFirst) {
				query += ")";
			}
			query += " GROUP BY SP.ID_STORICO_PARTICELLA, " +
			  		 "          SP.COMUNE, " +
			  		 "          C.DESCOM, " +
			  		 "          P.SIGLA_PROVINCIA, " +
			  		 "          P.ISTAT_PROVINCIA, " +
			  		 "          SP.SEZIONE, " +
			  		 "          SP.FOGLIO, " +
			  		 "          SP.PARTICELLA, " +
			  		 "          SP.SUBALTERNO, " +
			  		 "          SP.SUP_CATASTALE, " +
			  		 "          PART.FLAG_SCHEDARIO, " +
			  		 "          SUA.ID_STORICO_UNITA_ARBOREA, " +
			  		 "          SUA.ID_CATALOGO_MATRICE, " +
			  		 "          SUA.ID_UNITA_ARBOREA, " +
			  		 "          SUA.ID_PARTICELLA, " +
			  		 "          SUA.PROGR_UNAR, " +
			  		 "          SUA.DATA_INIZIO_VALIDITA, " +
			  		 "          SUA.DATA_FINE_VALIDITA, " +
			  		 "          SUA.DATA_LAVORAZIONE, " +
			  		 "          SUA.ID_TIPOLOGIA_UNAR, " +
			  		 "          TTU.DESCRIZIONE, " +
			  		 "          SUA.AREA, " +
			  		 "          SUA.SESTO_SU_FILA, " +
			  		 "          SUA.SESTO_TRA_FILE, " +
			  		 "          SUA.NUM_CEPPI, " +
			  		 "          SUA.ANNO_IMPIANTO, " +
			  		 "          SUA.DATA_IMPIANTO, " +
			  		 "          SUA.ANNO_REINNESTO, " +
			  		 "          SUA.ID_FORMA_ALLEVAMENTO, " +
			  		 "          TFA.DESCRIZIONE, " +
			  		 "          SUA.ID_IRRIGAZIONE_UNAR, " +
			  		 "          SUA.ID_COLTIVAZIONE_UNAR, " +
			  		 "          SUA.CODICE_TIPO_VARIETA, " +
			  		 "          SUA.PRESENZA_ALTRI_VITIGNI, " +
			  		 "          SUA.NUMERO_PIANTE_PRODUTTIVO, " +
			  		 "          SUA.NUMERO_ALTRE_PIANTE, " +
			  		 "          SUA.CAMPAGNA, " +
			  		 "          SUA.ID_TIPOLOGIA_VIGNETO, " +
			  		 "          SUA.TIPO_IMPIANTO, " +
			  		 "          SUA.NUMERO_CASTAGNI, " +
			  		 "          SUA.GRUPPO, " +
			  		 "          SUA.RICADUTA, " +
			  		 "          SUA.ID_GIACITURA_UNAR, " +
			  		 "          SUA.ID_ROCCIA_UNAR, " +
			  		 "          SUA.ID_SCHELETRO_UNAR, " +
			  		 "          SUA.ID_STATO_VEGETATIVO_UNAR, " +
			  		 "          SUA.ID_POTATURA_UNAR, " +
			  		 "          SUA.ID_GIUDIZIO_UNAR, " +
			  		 "          SUA.SUPPLEMENTARI, " +
			  		 "          SUA.MECCANIZZABILE, " +
			  		 "          SUA.DIMENSIONE_CHIOMA, " +
			  		 "          SUA.ID_ETA_IMPIANTO_UNAR, " +
			  		 "          SUA.PROVINCIA_CCIAA, " +
			  		 "          SUA.MATRICOLA_CCIAA, " +
			  		 "          SUA.CONFERMA_PREC_ISCRIZIONE_ALBO, " +
			  		 "          SUA.RICHIESTA_NUOVA_ISCR_ALBO, " +
			  		 "          SUA.CONFERMA_RICH_NUOVA_ISCR_ALBO, " +
			  		 "          SUA.SUPERFICIE_DA_ISCRIVERE_ALBO, " +
			  		 "          SUA.ANNO_ISCRIZIONE_ALBO, " +
			  		 "          SUA.ID_FONTE, " +
			  		 "          SUA.ID_VARIAZIONE_UNAR, " +
			  		 "          SUA.NOTE, " +
			  		 "          SUA.DATA_AGGIORNAMENTO, " +
			  		 "          SUA.ID_UTENTE_AGGIORNAMENTO, " +
			  		 "          RCM.ID_VARIETA, " +
			  		 "          TVAR.DESCRIZIONE, " +
			  		 "          TVAR.CODICE_VARIETA, " +
			  		 "          RCM.ID_UTILIZZO, " +
			  		 "          TU.CODICE, " + 
			  		 "          TU.DESCRIZIONE, " +
			  		 "          SUA.PERCENTUALE_VARIETA, " +
			  		 "          SUA.DATA_ESECUZIONE, " +
			  		 "          SUA.RECORD_MODIFICATO, " +
			  		 "          SUA.ESITO_CONTROLLO, " +
			  		 "          SUA.ID_TIPOLOGIA_VINO, " +
             "          TTV.DESCRIZIONE, " +
			  		 "          PC.ID_PARTICELLA_CERTIFICATA, "+
			  		 "          PC.SUP_COLT_ARBOREA_SPECIALIZZATA, " +
			  		 "          PC.SUP_GRAFICA, " +
             "          PC.SUP_USO_GRAFICA, " +
			  		 "          SUA.STATO_UNITA_ARBOREA, " +
			  		 "          SUA.ANNO_RIFERIMENTO, " +
			  		 "          SUA.VIGNA ";
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
      //query += " ) PARTICELLE_TABLE ";
      query += " ) PARTICELLE_TABLE ) PARTICELLE_TABLE_SUM ";
      //**********************************

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in searchStoricoUnitaArboreaByParametersForStampa method in StoricoUnitaArboreaDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ORDINAMENTO] in searchStoricoUnitaArboreaByParametersForStampa method in StoricoUnitaArboreaDAO: "+ordinamento+"\n");

			stmt = conn.prepareStatement(query);
			
			int indice = 0;
			
			stmt.setLong(++indice, idAzienda.longValue());
			// Se l'utente ha indicato la destinazione produttiva
			if(filtriUnitaArboreaRicercaVO.getIdUtilizzo() != null) {
				stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdUtilizzo().longValue());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_UTILIZZO] in searchStoricoUnitaArboreaByParametersForStampa method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIdUtilizzo()+"\n");
			}
			// Se l'utente ha indicato il vitigno
			if(filtriUnitaArboreaRicercaVO.getIdVarieta() != null) {
				stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdVarieta().longValue());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_VARIETA] in searchStoricoUnitaArboreaByParametersForStampa method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIdVarieta()+"\n");
			}
			// Se l'utente ha indicato il tipo vino
      if(filtriUnitaArboreaRicercaVO.getIdTipologiaVino() != null &&
    	 filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue() != new Long(-1) &&
    	 filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue() != new Long(0)) {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_TIPOLOGIA_VINO] in searchStoricoUnitaArboreaByParametersForStampa method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIdTipologiaVino()+"\n");
      }
			// Se l'utente ha indicato il comune di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIstatComune())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getIstatComune());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[ISTAT_COMUNE] in searchStoricoUnitaArboreaByParametersForStampa method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIstatComune()+"\n");
			}
			// Se l'utente ha indicato la sezione di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSezione())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getSezione().toUpperCase());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[SEZIONE] in searchStoricoUnitaArboreaByParametersForStampa method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getSezione().toUpperCase()+"\n");
			}
			// Se l'utente ha indicato il foglio di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getFoglio())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getFoglio());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[FOGLIO] in searchStoricoUnitaArboreaByParametersForStampa method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getFoglio()+"\n");
			}
			// Se l'utente ha indicato la particella di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getParticella())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getParticella());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[PARTICELLA] in searchStoricoUnitaArboreaByParametersForStampa method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getParticella()+"\n");
			}
			// Se l'utente ha indicato il subalterno di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSubalterno())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getSubalterno());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[SUBALTERNO] in searchStoricoUnitaArboreaByParametersForStampa method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getSubalterno()+"\n");
			}
			// SEGNALAZIONI:
			// Se l'utente ha specificato la tipologia di anomalia bloccante
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneBloccante())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getTipoSegnalazioneBloccante());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[TIPO_SEGNALAZIONE_BLOCCANTE] in searchStoricoUnitaArboreaByParametersForStampa method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getTipoSegnalazioneBloccante()+"\n");
			}
			// Se l'utente ha specificato la tipologia di anomalia warning
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneWarning())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getTipoSegnalazioneWarning());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[TIPO_SEGNALAZIONE_WARNING] in searchStoricoUnitaArboreaByParametersForStampa method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getTipoSegnalazioneWarning()+"\n");
			}
			// Se l'utente ha specificato la tipologia di anomalia OK
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneOk())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getTipoSegnalazioneOk());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[TIPO_SEGNALAZIONE_OK] in searchStoricoUnitaArboreaByParametersForStampa method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getTipoSegnalazioneOk()+"\n");					
			}

			SolmrLogger.debug(this, "Executing searchStoricoUnitaArboreaByParametersForStampa: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
				ParticellaVO particellaVO = new ParticellaVO();
				StoricoUnitaArboreaVO storicoUnitaArboreaVO = new StoricoUnitaArboreaVO();
				ParticellaCertificataVO particellaCertificataVO = new ParticellaCertificataVO();
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
	    	storicoParticellaVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
	    	particellaVO.setFlagSchedario(rs.getString("FLAG_SCHEDARIO"));
	    	storicoParticellaVO.setParticellaVO(particellaVO);
				storicoUnitaArboreaVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
				storicoUnitaArboreaVO.setIdUnitaArborea(new Long(rs.getLong("ID_UNITA_ARBOREA")));
				storicoUnitaArboreaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
				storicoUnitaArboreaVO.setProgrUnar(rs.getString("PROGR_UNAR"));
				storicoUnitaArboreaVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				storicoUnitaArboreaVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				storicoUnitaArboreaVO.setDataLavorazione(rs.getDate("DATA_LAVORAZIONE"));
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_UNAR"))) {
					storicoUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
					TipoTipologiaUnitaArboreaVO tipoTipologiaUnitaArboreaVO = new TipoTipologiaUnitaArboreaVO();
					tipoTipologiaUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
					tipoTipologiaUnitaArboreaVO.setDescrizione(rs.getString("DESC_TIPO_UNAR"));
					storicoUnitaArboreaVO.setTipoTipologiaUnitaArboreaVO(tipoTipologiaUnitaArboreaVO);
				}
				storicoUnitaArboreaVO.setArea(rs.getString("AREA"));
				storicoUnitaArboreaVO.setSestoSuFila(rs.getString("SESTO_SU_FILA"));
				storicoUnitaArboreaVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
				storicoUnitaArboreaVO.setNumCeppi(rs.getString("NUM_CEPPI"));
				storicoUnitaArboreaVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
				storicoUnitaArboreaVO.setDataImpianto(rs.getDate("DATA_IMPIANTO"));
				storicoUnitaArboreaVO.setAnnoReinnesto(rs.getString("ANNO_REINNESTO"));
				if(Validator.isNotEmpty(rs.getString("ID_FORMA_ALLEVAMENTO"))) {
					storicoUnitaArboreaVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
					TipoFormaAllevamentoVO tipoFormaAllevamentoVO = new TipoFormaAllevamentoVO();
					tipoFormaAllevamentoVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
					tipoFormaAllevamentoVO.setDescrizione(rs.getString("DESC_FORMA_ALLEVAMENTO"));
					storicoUnitaArboreaVO.setTipoFormaAllevamentoVO(tipoFormaAllevamentoVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_IRRIGAZIONE_UNAR"))) {
					storicoUnitaArboreaVO.setIdIrrigazioneUnar(new Long(rs.getLong("ID_IRRIGAZIONE_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_COLTIVAZIONE_UNAR"))) {
					storicoUnitaArboreaVO.setIdColtivazioneUnar(new Long(rs.getLong("ID_COLTIVAZIONE_UNAR")));
				}
				storicoUnitaArboreaVO.setCodiceTipoVarieta(rs.getString("CODICE_TIPO_VARIETA"));
				storicoUnitaArboreaVO.setPresenzaAltriVitigni(rs.getString("PRESENZA_ALTRI_VITIGNI"));
				storicoUnitaArboreaVO.setNumeroPianteProduttivo(rs.getString("NUMERO_PIANTE_PRODUTTIVO"));
				storicoUnitaArboreaVO.setNumeroAltrePiante(rs.getString("NUMERO_ALTRE_PIANTE"));
				storicoUnitaArboreaVO.setCampagna(rs.getString("CAMPAGNA"));
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VIGNETO"))) {
					storicoUnitaArboreaVO.setIdTipologiaVigneto(new Long(rs.getLong("ID_TIPOLOGIA_VIGNETO")));
				}
				storicoUnitaArboreaVO.setTipoImpianto(rs.getString("TIPO_IMPIANTO"));
				storicoUnitaArboreaVO.setNumeroCastagni(rs.getString("NUMERO_CASTAGNI"));
				storicoUnitaArboreaVO.setGruppo(rs.getString("GRUPPO"));
				storicoUnitaArboreaVO.setRicaduta(rs.getString("RICADUTA"));
				if(Validator.isNotEmpty(rs.getString("ID_GIACITURA_UNAR"))) {
					storicoUnitaArboreaVO.setIdGiacituraUnar(new Long(rs.getLong("ID_GIACITURA_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_ROCCIA_UNAR"))) {
					storicoUnitaArboreaVO.setIdRocciaUnar(new Long(rs.getLong("ID_ROCCIA_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_SCHELETRO_UNAR"))) {
					storicoUnitaArboreaVO.setIdScheletroUnar(new Long(rs.getLong("ID_SCHELETRO_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_STATO_VEGETATIVO_UNAR"))) {
					storicoUnitaArboreaVO.setIdStatoVegetativoUnar(new Long(rs.getLong("ID_STATO_VEGETATIVO_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_POTATURA_UNAR"))) {
					storicoUnitaArboreaVO.setIdPotaturaUnar(new Long(rs.getLong("ID_POTATURA_UNAR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_GIUDIZIO_UNAR"))) {
					storicoUnitaArboreaVO.setIdGiudizioUnar(new Long(rs.getLong("ID_GIUDIZIO_UNAR")));
				}
				storicoUnitaArboreaVO.setSupplementari(rs.getString("SUPPLEMENTARI"));
				storicoUnitaArboreaVO.setMeccanizzabile(rs.getString("MECCANIZZABILE"));
				storicoUnitaArboreaVO.setDimensioneChioma(rs.getString("DIMENSIONE_CHIOMA"));
				if(Validator.isNotEmpty(rs.getString("ID_ETA_IMPIANTO_UNAR"))) {
					storicoUnitaArboreaVO.setIdEtaImpiantoUnar(new Long(rs.getLong("ID_ETA_IMPIANTO_UNAR")));
				}
				storicoUnitaArboreaVO.setProvinciaCCIAA(rs.getString("PROVINCIA_CCIAA"));
				storicoUnitaArboreaVO.setMatricolaCCIAA(rs.getString("MATRICOLA_CCIAA"));
				storicoUnitaArboreaVO.setConfermaPrecIscrizioneAlbo(rs.getString("CONFERMA_PREC_ISCRIZIONE_ALBO"));
				storicoUnitaArboreaVO.setRichiestaNuovaIscrAlbo(rs.getString("RICHIESTA_NUOVA_ISCR_ALBO"));
				storicoUnitaArboreaVO.setConfermaRichNuovaIscrAlbo(rs.getString("CONFERMA_RICH_NUOVA_ISCR_ALBO"));
				storicoUnitaArboreaVO.setSuperficieDaIscrivereAlbo(rs.getString("SUPERFICIE_DA_ISCRIVERE_ALBO"));
				storicoUnitaArboreaVO.setAnnoIscrizioneAlbo(rs.getString("ANNO_ISCRIZIONE_ALBO"));
				storicoUnitaArboreaVO.setIdFonte(new Long(rs.getLong("ID_FONTE")));
				if(Validator.isNotEmpty(rs.getString("ID_VARIAZIONE_UNAR"))) {
					storicoUnitaArboreaVO.setIdVariazioneUnar(new Long(rs.getLong("ID_VARIAZIONE_UNAR")));
				}
				storicoUnitaArboreaVO.setNote(rs.getString("NOTE"));
				storicoUnitaArboreaVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
				storicoUnitaArboreaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) {
					storicoUnitaArboreaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
					TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
					tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
					tipoVarietaVO.setDescrizione(rs.getString("DESC_VARIETA"));
					tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VAR"));
					storicoUnitaArboreaVO.setTipoVarietaVO(tipoVarietaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO"))) {
					storicoUnitaArboreaVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
					TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
					tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
					tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
					tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTILIZZO"));
					storicoUnitaArboreaVO.setTipoUtilizzoVO(tipoUtilizzoVO);
				}
				storicoUnitaArboreaVO.setPercentualeVarieta(rs.getString("PERCENTUALE_VARIETA"));
				if(rs.getDate("DATA_ESECUZIONE") != null) {
					storicoUnitaArboreaVO.setDataEsecuzione(new Timestamp(rs.getDate("DATA_ESECUZIONE").getTime()));
				}
				storicoUnitaArboreaVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
				storicoUnitaArboreaVO.setRecordModificato(rs.getString("RECORD_MODIFICATO"));
				storicoUnitaArboreaVO.setStatoUnitaArborea(rs.getString("STATO_UNITA_ARBOREA"));
				storicoUnitaArboreaVO.setAnnoRiferimento(rs.getString("ANNO_RIFERIMENTO"));
				
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
        
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VINO"))) {
          storicoUnitaArboreaVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
          TipoTipologiaVinoVO tipoTipologiaVinoVO = new TipoTipologiaVinoVO();
          tipoTipologiaVinoVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
          tipoTipologiaVinoVO.setDescrizione(rs.getString("DESC_TIPO_VINO"));
          storicoUnitaArboreaVO.setTipoTipologiaVinoVO(tipoTipologiaVinoVO);
        }
        
        storicoUnitaArboreaVO.setVigna(rs.getString("VIGNA"));
        storicoParticellaVO.setStoricoUnitaArboreaVO(storicoUnitaArboreaVO);
        
				storicoParticellaVO.setParticellaCertificataVO(particellaCertificataVO);
				elencoParticelleArboree.add(storicoParticellaVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "searchStoricoUnitaArboreaByParametersForStampa in StoricoUnitaArboreaDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "searchStoricoUnitaArboreaByParametersForStampa in StoricoUnitaArboreaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "searchStoricoUnitaArboreaByParametersForStampa in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "searchStoricoUnitaArboreaByParametersForStampa in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated searchStoricoUnitaArboreaByParametersForStampa method in StoricoUnitaArboreaDAO\n");
		if(elencoParticelleArboree.size() == 0) {
			return (StoricoParticellaVO[])elencoParticelleArboree.toArray(new StoricoParticellaVO[0]);
		}
		else {
			return (StoricoParticellaVO[])elencoParticelleArboree.toArray(new StoricoParticellaVO[elencoParticelleArboree.size()]);
		}
	}
	
	
	
	/**
   * Metodo utilizzato per ricercare le UV
   * 
   * @param idAzienda
   * @param filtriUnitaArboreaRicercaVO
   * @param orderBy
   * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
   * @throws DataAccessException
   */
  public StoricoParticellaVO[] searchStoricoUnitaArboreaByParameters(String nomeLib,
      Long idAzienda, FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO, BigDecimal p26Valore, 
      String[] orderBy) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating searchStoricoUnitaArboreaByParameters method in StoricoUnitaArboreaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelleArboree = new Vector<StoricoParticellaVO>();
    
    StopWatch watcher = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);

    try 
    {
      
      // START
      watcher.start();
      
      SolmrLogger.debug(this, "Creating db-connection in searchStoricoUnitaArboreaByParameters method in StoricoUnitaArboreaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in searchStoricoUnitaArboreaByParameters method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");

      String query = " " +
		    "SELECT SP.ID_STORICO_PARTICELLA, " +
        "       SP.COMUNE, " +
        "       C.DESCOM, " +
        "       P.SIGLA_PROVINCIA, " +
        "       P.ISTAT_PROVINCIA, " +
        "       SP.SEZIONE, " +
        "       SP.FOGLIO, " +
        "       SP.PARTICELLA, " +
        "       SP.SUBALTERNO, " +
        "       SP.SUP_CATASTALE, " +
        "       SP.SUPERFICIE_GRAFICA, " +
        "       PART.FLAG_SCHEDARIO, " +
        "       SUA.ID_STORICO_UNITA_ARBOREA, " +
        "       SUA.ID_UNITA_ARBOREA, " +
        "       SUA.ID_PARTICELLA, " +
        "       SUA.PROGR_UNAR, " +
        "       SUA.DATA_INIZIO_VALIDITA, " +
        "       SUA.DATA_FINE_VALIDITA, " +
        "       SUA.DATA_LAVORAZIONE, " +
        "       SUA.ID_TIPOLOGIA_UNAR, " +
        "       TTU.DESCRIZIONE AS DESC_TIPO_UNAR, " +
        "       SUA.AREA, " +
        "       SUA.SESTO_SU_FILA, " +
        "       SUA.SESTO_TRA_FILE, " +
        "       SUA.NUM_CEPPI, " +
        "       SUA.ANNO_IMPIANTO, " +
        "       SUA.DATA_IMPIANTO, " +
        "       SUA.ANNO_REINNESTO, " +
        "       SUA.ID_FORMA_ALLEVAMENTO, " +
        "       TFA.DESCRIZIONE AS DESC_FORMA_ALLEVAMENTO, " +
        "       SUA.ID_IRRIGAZIONE_UNAR, " +
        "       SUA.ID_COLTIVAZIONE_UNAR, " +
        "       SUA.CODICE_TIPO_VARIETA, " +
        "       SUA.PRESENZA_ALTRI_VITIGNI, " +
        "       SUA.NUMERO_PIANTE_PRODUTTIVO, " +
        "       SUA.NUMERO_ALTRE_PIANTE, " +
        "       SUA.CAMPAGNA, " +
        "       SUA.ID_TIPOLOGIA_VIGNETO, " +
        "       SUA.TIPO_IMPIANTO, " +
        "       SUA.NUMERO_CASTAGNI, " +
        "       SUA.GRUPPO, " +
        "       SUA.RICADUTA, " +
        "       SUA.ID_GIACITURA_UNAR, " +
        "       SUA.ID_ROCCIA_UNAR, " +
        "       SUA.ID_SCHELETRO_UNAR, " +
        "       SUA.ID_STATO_VEGETATIVO_UNAR, " +
        "       SUA.ID_POTATURA_UNAR, " +
        "       SUA.ID_GIUDIZIO_UNAR, " +
        "       SUA.SUPPLEMENTARI, " +
        "       SUA.MECCANIZZABILE, " +
        "       SUA.DIMENSIONE_CHIOMA, " +
        "       SUA.ID_ETA_IMPIANTO_UNAR, " +
        "       SUA.PROVINCIA_CCIAA, " +
        "       SUA.MATRICOLA_CCIAA, " +
        "       SUA.CONFERMA_PREC_ISCRIZIONE_ALBO, " +
        "       SUA.RICHIESTA_NUOVA_ISCR_ALBO, " +
        "       SUA.CONFERMA_RICH_NUOVA_ISCR_ALBO, " +
        "       SUA.SUPERFICIE_DA_ISCRIVERE_ALBO, " +
        "       SUA.ANNO_ISCRIZIONE_ALBO, " +
        "       SUA.ID_FONTE, " +
        "       SUA.ID_VARIAZIONE_UNAR, " +
        "       SUA.NOTE, " +
        "       SUA.DATA_AGGIORNAMENTO, " +
        "       SUA.ID_UTENTE_AGGIORNAMENTO, " +
        "       SUA.ID_VARIETA, " +
        "       TVAR.DESCRIZIONE AS DESC_VARIETA, " +
        "       TVAR.CODICE_VARIETA AS COD_VAR, " +
        "       SUA.ID_UTILIZZO, " +
        "       TU.CODICE, " +
        "       TU.DESCRIZIONE AS DESC_TIPO_UTILIZZO, " +
        "       SUA.PERCENTUALE_VARIETA, " +
        "       SUA.DATA_ESECUZIONE, " +
        "       SUA.RECORD_MODIFICATO, " +
        "       SUA.ESITO_CONTROLLO, " +
        "       SUA.ID_TIPOLOGIA_VINO, " +
        "       TTV.DESCRIZIONE AS DESC_TIPO_VINO, " +
        "       TTV.FLAG_GESTIONE_VIGNA, " +
        "       SUA.STATO_UNITA_ARBOREA, " +
        "       SUA.ANNO_RIFERIMENTO, " +
        "       SUA.ID_CAUSALE_MODIFICA, " +
        "       SUA.FLAG_IMPRODUTTIVA, " +
        "       SUA.PERCENTUALE_FALLANZA, " +
        "       TCM.DESCRIZIONE AS DESC_CAUS_MOD, " +
        "       SUM(CP.PERCENTUALE_POSSESSO) AS PERCENTUALE_POSSESSO, " +
        "       F.FLAG_STABILIZZAZIONE " +
        "FROM   DB_STORICO_PARTICELLA SP, " +
        "       COMUNE C, " +
        "       PROVINCIA P, " +
        "       DB_PARTICELLA PART, " +
        "       DB_STORICO_UNITA_ARBOREA SUA, " +
        "       DB_TIPO_TIPOLOGIA_UNAR TTU, " +
        "       DB_TIPO_FORMA_ALLEVAMENTO TFA, " +
        "       DB_TIPO_VARIETA TVAR, " +
        "       DB_TIPO_UTILIZZO TU, " +
        "       DB_UTE U, " +
        "       DB_CONDUZIONE_PARTICELLA CP, " +
        "       DB_TIPO_TITOLO_POSSESSO TTP, " +                       
        "       DB_TIPO_TIPOLOGIA_VINO TTV, " +
        "       DB_TIPO_CAUSALE_MODIFICA TCM, " +
        "       DB_FOGLIO F ";
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdControllo()))
      {
        query += 
        "       ,DB_ESITO_CONTROLLO_UNAR ECU ";
      }
      query +=
        "WHERE  SUA.ID_PARTICELLA = SP.ID_PARTICELLA " +
        "AND    SP.ID_PARTICELLA = PART.ID_PARTICELLA " +
        "AND    SP.COMUNE = C.ISTAT_COMUNE " +
        "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
        "AND    SUA.ID_TIPOLOGIA_UNAR = TTU.ID_TIPOLOGIA_UNAR(+) " +
        "AND    SUA.ID_FORMA_ALLEVAMENTO = TFA.ID_FORMA_ALLEVAMENTO(+) " +
        "AND    SUA.ID_VARIETA = TVAR.ID_VARIETA(+) " +
        "AND    SUA.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
        "AND    SUA.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO(+) " +
        "AND    SUA.ID_AZIENDA = ? " +
        "AND    SUA.ID_AZIENDA = U.ID_AZIENDA " +
        "AND    U.ID_UTE = CP.ID_UTE " +
        "AND    CP.ID_PARTICELLA = SUA.ID_PARTICELLA " +
        "AND    CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO " +
        "AND    CP.ID_TITOLO_POSSESSO NOT IN (5,6) " +
        "AND    U.DATA_FINE_ATTIVITA IS NULL " +
        "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "AND    SP.DATA_FINE_VALIDITA IS NULL " +
        "AND    SUA.ID_CAUSALE_MODIFICA = TCM.ID_CAUSALE_MODIFICA(+) " +
        "AND    SP.COMUNE = F.COMUNE(+) " +
        "AND    NVL( SP.SEZIONE,'-') = NVL( F.SEZIONE(+),'-') " +
        "AND    SP.FOGLIO = F.FOGLIO(+) ";
      
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdControllo()))
      {
        query += 
        "AND    SUA.ID_STORICO_UNITA_ARBOREA = ECU.ID_STORICO_UNITA_ARBOREA " +
        "AND    ECU.ID_CONTROLLO = ? ";
      }
      
      if(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() < 0) {
        query +=   " AND    SUA.DATA_FINE_VALIDITA IS NULL ";
      }
      // Se l'utente ha indicato la destinazione produttiva
      if(filtriUnitaArboreaRicercaVO.getIdUtilizzo() != null) {
        query +=   " AND    SUA.ID_UTILIZZO = ? ";
      }
      // Se l'utente ha indicato il vitigno
      if(filtriUnitaArboreaRicercaVO.getIdVarieta() != null) {
        query +=   " AND    SUA.ID_VARIETA = ? ";
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
          query += " AND      SUA.ID_TIPOLOGIA_VINO IS NULL ";
        }
        else query += " AND      SUA.ID_TIPOLOGIA_VINO = ? ";
      }
      // Se l'utente ha indicato il genere iscrizione
      if(filtriUnitaArboreaRicercaVO.getIdGenereIscrizione() != null) {
        query +=   " AND    SUA.ID_GENERE_ISCRIZIONE = ? ";
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
                   "               AND    NO.ID_AZIENDA = SUA.ID_AZIENDA ";
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
                     "                                    AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA )) )";
        }
        else
        {
          query +=   " AND    NE.DATA_FINE_VALIDITA IS NULL )";
        }
        
      }
      
      
      
      
      // Se l'utente ha indicato la causale modifica
      if(filtriUnitaArboreaRicercaVO.getIdCausaleModifica() != null) {
        query +=   " AND    SUA.ID_CAUSALE_MODIFICA = ? ";
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
        query += " AND (SUA.ESITO_CONTROLLO = ? ";
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
        query += " SUA.ESITO_CONTROLLO = ? ";
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
        query += " SUA.ESITO_CONTROLLO = ? ";
        isFirst = false;
      }
      if(!isFirst) {
        query += ")";
      }
      query += " GROUP BY SP.ID_STORICO_PARTICELLA, " +
               "          SP.COMUNE, " +
               "          C.DESCOM, " +
               "          P.SIGLA_PROVINCIA, " +
               "          P.ISTAT_PROVINCIA, " +
               "          SP.SEZIONE, " +
               "          SP.FOGLIO, " +
               "          SP.PARTICELLA, " +
               "          SP.SUBALTERNO, " +
               "          SP.SUP_CATASTALE, " +
               "          SP.SUPERFICIE_GRAFICA, " +
               "          PART.FLAG_SCHEDARIO, " +
               "          SUA.ID_STORICO_UNITA_ARBOREA, " +
               "          SUA.ID_UNITA_ARBOREA, " +
               "          SUA.ID_PARTICELLA, " +
               "          SUA.PROGR_UNAR, " +
               "          SUA.DATA_INIZIO_VALIDITA, " +
               "          SUA.DATA_FINE_VALIDITA, " +
               "          SUA.DATA_LAVORAZIONE, " +
               "          SUA.ID_TIPOLOGIA_UNAR, " +
               "          TTU.DESCRIZIONE, " +
               "          SUA.AREA, " +
               "          SUA.SESTO_SU_FILA, " +
               "          SUA.SESTO_TRA_FILE, " +
               "          SUA.NUM_CEPPI, " +
               "          SUA.ANNO_IMPIANTO, " +
               "          SUA.DATA_IMPIANTO, " +
               "          SUA.ANNO_REINNESTO, " +
               "          SUA.ID_FORMA_ALLEVAMENTO, " +
               "          TFA.DESCRIZIONE, " +
               "          SUA.ID_IRRIGAZIONE_UNAR, " +
               "          SUA.ID_COLTIVAZIONE_UNAR, " +
               "          SUA.CODICE_TIPO_VARIETA, " +
               "          SUA.PRESENZA_ALTRI_VITIGNI, " +
               "          SUA.NUMERO_PIANTE_PRODUTTIVO, " +
               "          SUA.NUMERO_ALTRE_PIANTE, " +
               "          SUA.CAMPAGNA, " +
               "          SUA.ID_TIPOLOGIA_VIGNETO, " +
               "          SUA.TIPO_IMPIANTO, " +
               "          SUA.NUMERO_CASTAGNI, " +
               "          SUA.GRUPPO, " +
               "          SUA.RICADUTA, " +
               "          SUA.ID_GIACITURA_UNAR, " +
               "          SUA.ID_ROCCIA_UNAR, " +
               "          SUA.ID_SCHELETRO_UNAR, " +
               "          SUA.ID_STATO_VEGETATIVO_UNAR, " +
               "          SUA.ID_POTATURA_UNAR, " +
               "          SUA.ID_GIUDIZIO_UNAR, " +
               "          SUA.SUPPLEMENTARI, " +
               "          SUA.MECCANIZZABILE, " +
               "          SUA.DIMENSIONE_CHIOMA, " +
               "          SUA.ID_ETA_IMPIANTO_UNAR, " +
               "          SUA.PROVINCIA_CCIAA, " +
               "          SUA.MATRICOLA_CCIAA, " +
               "          SUA.CONFERMA_PREC_ISCRIZIONE_ALBO, " +
               "          SUA.RICHIESTA_NUOVA_ISCR_ALBO, " +
               "          SUA.CONFERMA_RICH_NUOVA_ISCR_ALBO, " +
               "          SUA.SUPERFICIE_DA_ISCRIVERE_ALBO, " +
               "          SUA.ANNO_ISCRIZIONE_ALBO, " +
               "          SUA.ID_FONTE, " +
               "          SUA.ID_VARIAZIONE_UNAR, " +
               "          SUA.NOTE, " +
               "          SUA.DATA_AGGIORNAMENTO, " +
               "          SUA.ID_UTENTE_AGGIORNAMENTO, " +
               "          SUA.ID_VARIETA, " +
               "          TVAR.DESCRIZIONE, " +
               "          TVAR.CODICE_VARIETA, " +
               "          SUA.ID_UTILIZZO, " +
               "          TU.CODICE, " +
               "          TU.DESCRIZIONE, " +
               "          SUA.PERCENTUALE_VARIETA, " +
               "          SUA.DATA_ESECUZIONE, " +
               "          SUA.RECORD_MODIFICATO, " +
               "          SUA.ESITO_CONTROLLO, " +
               "          SUA.ID_TIPOLOGIA_VINO, " +
               "          TTV.DESCRIZIONE, " +
               "          TTV.FLAG_GESTIONE_VIGNA, " +
               "          SUA.STATO_UNITA_ARBOREA, " +
               "          SUA.ANNO_RIFERIMENTO, " +
               "          SUA.ID_CAUSALE_MODIFICA," +
               "          SUA.FLAG_IMPRODUTTIVA, " +
               "          SUA.PERCENTUALE_FALLANZA, " +
               "          TCM.DESCRIZIONE, " +
               "          F.FLAG_STABILIZZAZIONE ";
         //      "          CP.PERCENTUALE_POSSESSO ";
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
      
      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in searchStoricoUnitaArboreaByParameters method in StoricoUnitaArboreaDAO: "+idAzienda+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ORDINAMENTO] in searchStoricoUnitaArboreaByParameters method in StoricoUnitaArboreaDAO: "+ordinamento+"\n");

      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      //stmt.setLong(++indice, idAzienda.longValue()); //plsql
      stmt.setLong(++indice, idAzienda.longValue());
      //se l'utente ha selezionato il tipo di controllo
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdControllo()))
      {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdControllo().longValue());
      }
      // Se l'utente ha indicato la destinazione produttiva
      if(filtriUnitaArboreaRicercaVO.getIdUtilizzo() != null) {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdUtilizzo().longValue());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_UTILIZZO] in searchStoricoUnitaArboreaByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIdUtilizzo()+"\n");
      }
      // Se l'utente ha indicato il vitigno
      if(filtriUnitaArboreaRicercaVO.getIdVarieta() != null) {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdVarieta().longValue());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_VARIETA] in searchStoricoUnitaArboreaByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIdVarieta()+"\n");
      }
      // Se l'utente ha indicato il tipo vino
      if(filtriUnitaArboreaRicercaVO.getIdTipologiaVino() != null &&
        filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue() != new Long(-1) &&
        filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue() != new Long(0)) 
      {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_TIPOLOGIA_VINO] in searchStoricoUnitaArboreaByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIdTipologiaVino()+"\n");
      }
      // Se l'utente ha indicato il genere iscrizione
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdGenereIscrizione())) {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdGenereIscrizione().longValue());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_GENERE_ISCRIZIONE] in searchStoricoUnitaArboreaByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIdGenereIscrizione()+"\n");
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
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_CAUSALE_MODIFICA] in searchStoricoUnitaArboreaByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIdCausaleModifica()+"\n");
      }
      // Se l'utente ha indicato la provincia di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIstatProvincia())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getIstatProvincia());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ISTAT_PROVINCIA] in searchStoricoUnitaArboreaByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIstatProvincia()+"\n");
      }
      // Se l'utente ha indicato il comune di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIstatComune())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getIstatComune());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ISTAT_COMUNE] in searchStoricoUnitaArboreaByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIstatComune()+"\n");
      }
      // Se l'utente ha indicato la sezione di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSezione())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getSezione().toUpperCase());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[SEZIONE] in searchStoricoUnitaArboreaByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getSezione().toUpperCase()+"\n");
      }
      // Se l'utente ha indicato il foglio di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getFoglio())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getFoglio());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[FOGLIO] in searchStoricoUnitaArboreaByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getFoglio()+"\n");
      }
      // Se l'utente ha indicato la particella di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getParticella())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getParticella());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[PARTICELLA] in searchStoricoUnitaArboreaByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getParticella()+"\n");
      }
      // Se l'utente ha indicato il subalterno di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSubalterno())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getSubalterno());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[SUBALTERNO] in searchStoricoUnitaArboreaByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getSubalterno()+"\n");
      }
      // SEGNALAZIONI:
      // Se l'utente ha specificato la tipologia di anomalia bloccante
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneBloccante())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getTipoSegnalazioneBloccante());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[TIPO_SEGNALAZIONE_BLOCCANTE] in searchStoricoUnitaArboreaByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getTipoSegnalazioneBloccante()+"\n");
      }
      // Se l'utente ha specificato la tipologia di anomalia warning
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneWarning())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getTipoSegnalazioneWarning());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[TIPO_SEGNALAZIONE_WARNING] in searchStoricoUnitaArboreaByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getTipoSegnalazioneWarning()+"\n");
      }
      // Se l'utente ha specificato la tipologia di anomalia OK
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneOk())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getTipoSegnalazioneOk());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[TIPO_SEGNALAZIONE_OK] in searchStoricoUnitaArboreaByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getTipoSegnalazioneOk()+"\n");         
      }

      SolmrLogger.debug(this, "Executing searchStoricoUnitaArboreaByParameters: "+query+"\n");

      ResultSet rs = stmt.executeQuery();
      
      
     // Primo monitoraggio
      watcher.dumpElapsed("StoricoUnitaArboreaDAO", "searchParticelleByParameters",
          "In searchParticelleByParameters method from the creation of query to the execution","");

      while(rs.next()) {
        StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
        ParticellaVO particellaVO = new ParticellaVO();
        StoricoUnitaArboreaVO storicoUnitaArboreaVO = new StoricoUnitaArboreaVO();
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
        storicoParticellaVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
        storicoParticellaVO.setSuperficieGrafica(rs.getString("SUPERFICIE_GRAFICA"));
        particellaVO.setFlagSchedario(rs.getString("FLAG_SCHEDARIO"));
        storicoParticellaVO.setParticellaVO(particellaVO);
        storicoUnitaArboreaVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
        storicoUnitaArboreaVO.setIdUnitaArborea(new Long(rs.getLong("ID_UNITA_ARBOREA")));
        storicoUnitaArboreaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
        storicoUnitaArboreaVO.setProgrUnar(rs.getString("PROGR_UNAR"));
        storicoUnitaArboreaVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        storicoUnitaArboreaVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        storicoUnitaArboreaVO.setDataLavorazione(rs.getDate("DATA_LAVORAZIONE"));
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_UNAR"))) {
          storicoUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
          TipoTipologiaUnitaArboreaVO tipoTipologiaUnitaArboreaVO = new TipoTipologiaUnitaArboreaVO();
          tipoTipologiaUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
          tipoTipologiaUnitaArboreaVO.setDescrizione(rs.getString("DESC_TIPO_UNAR"));
          storicoUnitaArboreaVO.setTipoTipologiaUnitaArboreaVO(tipoTipologiaUnitaArboreaVO);
        }
        storicoUnitaArboreaVO.setArea(rs.getString("AREA"));
        storicoUnitaArboreaVO.setSestoSuFila(rs.getString("SESTO_SU_FILA"));
        storicoUnitaArboreaVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
        storicoUnitaArboreaVO.setNumCeppi(rs.getString("NUM_CEPPI"));
        storicoUnitaArboreaVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
        storicoUnitaArboreaVO.setDataImpianto(rs.getDate("DATA_IMPIANTO"));
        storicoUnitaArboreaVO.setAnnoReinnesto(rs.getString("ANNO_REINNESTO"));
        if(Validator.isNotEmpty(rs.getString("ID_FORMA_ALLEVAMENTO"))) {
          storicoUnitaArboreaVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
          TipoFormaAllevamentoVO tipoFormaAllevamentoVO = new TipoFormaAllevamentoVO();
          tipoFormaAllevamentoVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
          tipoFormaAllevamentoVO.setDescrizione(rs.getString("DESC_FORMA_ALLEVAMENTO"));
          storicoUnitaArboreaVO.setTipoFormaAllevamentoVO(tipoFormaAllevamentoVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_IRRIGAZIONE_UNAR"))) {
          storicoUnitaArboreaVO.setIdIrrigazioneUnar(new Long(rs.getLong("ID_IRRIGAZIONE_UNAR")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_COLTIVAZIONE_UNAR"))) {
          storicoUnitaArboreaVO.setIdColtivazioneUnar(new Long(rs.getLong("ID_COLTIVAZIONE_UNAR")));
        }
        storicoUnitaArboreaVO.setCodiceTipoVarieta(rs.getString("CODICE_TIPO_VARIETA"));
        storicoUnitaArboreaVO.setPresenzaAltriVitigni(rs.getString("PRESENZA_ALTRI_VITIGNI"));
        storicoUnitaArboreaVO.setNumeroPianteProduttivo(rs.getString("NUMERO_PIANTE_PRODUTTIVO"));
        storicoUnitaArboreaVO.setNumeroAltrePiante(rs.getString("NUMERO_ALTRE_PIANTE"));
        storicoUnitaArboreaVO.setCampagna(rs.getString("CAMPAGNA"));
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VIGNETO"))) {
          storicoUnitaArboreaVO.setIdTipologiaVigneto(new Long(rs.getLong("ID_TIPOLOGIA_VIGNETO")));
        }
        storicoUnitaArboreaVO.setTipoImpianto(rs.getString("TIPO_IMPIANTO"));
        storicoUnitaArboreaVO.setNumeroCastagni(rs.getString("NUMERO_CASTAGNI"));
        storicoUnitaArboreaVO.setGruppo(rs.getString("GRUPPO"));
        storicoUnitaArboreaVO.setRicaduta(rs.getString("RICADUTA"));
        if(Validator.isNotEmpty(rs.getString("ID_GIACITURA_UNAR"))) {
          storicoUnitaArboreaVO.setIdGiacituraUnar(new Long(rs.getLong("ID_GIACITURA_UNAR")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_ROCCIA_UNAR"))) {
          storicoUnitaArboreaVO.setIdRocciaUnar(new Long(rs.getLong("ID_ROCCIA_UNAR")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_SCHELETRO_UNAR"))) {
          storicoUnitaArboreaVO.setIdScheletroUnar(new Long(rs.getLong("ID_SCHELETRO_UNAR")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_STATO_VEGETATIVO_UNAR"))) {
          storicoUnitaArboreaVO.setIdStatoVegetativoUnar(new Long(rs.getLong("ID_STATO_VEGETATIVO_UNAR")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_POTATURA_UNAR"))) {
          storicoUnitaArboreaVO.setIdPotaturaUnar(new Long(rs.getLong("ID_POTATURA_UNAR")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_GIUDIZIO_UNAR"))) {
          storicoUnitaArboreaVO.setIdGiudizioUnar(new Long(rs.getLong("ID_GIUDIZIO_UNAR")));
        }
        storicoUnitaArboreaVO.setSupplementari(rs.getString("SUPPLEMENTARI"));
        storicoUnitaArboreaVO.setMeccanizzabile(rs.getString("MECCANIZZABILE"));
        storicoUnitaArboreaVO.setDimensioneChioma(rs.getString("DIMENSIONE_CHIOMA"));
        if(Validator.isNotEmpty(rs.getString("ID_ETA_IMPIANTO_UNAR"))) {
          storicoUnitaArboreaVO.setIdEtaImpiantoUnar(new Long(rs.getLong("ID_ETA_IMPIANTO_UNAR")));
        }
        storicoUnitaArboreaVO.setProvinciaCCIAA(rs.getString("PROVINCIA_CCIAA"));
        storicoUnitaArboreaVO.setMatricolaCCIAA(rs.getString("MATRICOLA_CCIAA"));
        storicoUnitaArboreaVO.setConfermaPrecIscrizioneAlbo(rs.getString("CONFERMA_PREC_ISCRIZIONE_ALBO"));
        storicoUnitaArboreaVO.setRichiestaNuovaIscrAlbo(rs.getString("RICHIESTA_NUOVA_ISCR_ALBO"));
        storicoUnitaArboreaVO.setConfermaRichNuovaIscrAlbo(rs.getString("CONFERMA_RICH_NUOVA_ISCR_ALBO"));
        storicoUnitaArboreaVO.setSuperficieDaIscrivereAlbo(rs.getString("SUPERFICIE_DA_ISCRIVERE_ALBO"));
        storicoUnitaArboreaVO.setAnnoIscrizioneAlbo(rs.getString("ANNO_ISCRIZIONE_ALBO"));
        storicoUnitaArboreaVO.setIdFonte(new Long(rs.getLong("ID_FONTE")));
        if(Validator.isNotEmpty(rs.getString("ID_VARIAZIONE_UNAR"))) {
          storicoUnitaArboreaVO.setIdVariazioneUnar(new Long(rs.getLong("ID_VARIAZIONE_UNAR")));
        }
        storicoUnitaArboreaVO.setNote(rs.getString("NOTE"));
        storicoUnitaArboreaVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
        storicoUnitaArboreaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) {
          storicoUnitaArboreaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
          tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          tipoVarietaVO.setDescrizione(rs.getString("DESC_VARIETA"));
          tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VAR"));
          storicoUnitaArboreaVO.setTipoVarietaVO(tipoVarietaVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO"))) {
          storicoUnitaArboreaVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
          TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
          tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
          tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
          tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTILIZZO"));
          storicoUnitaArboreaVO.setTipoUtilizzoVO(tipoUtilizzoVO);
        }
        storicoUnitaArboreaVO.setPercentualeVarieta(rs.getString("PERCENTUALE_VARIETA"));
        if(rs.getDate("DATA_ESECUZIONE") != null) {
          storicoUnitaArboreaVO.setDataEsecuzione(new Timestamp(rs.getDate("DATA_ESECUZIONE").getTime()));
        }
        storicoUnitaArboreaVO.setRecordModificato(rs.getString("RECORD_MODIFICATO"));
        storicoUnitaArboreaVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VINO"))) 
        {
          storicoUnitaArboreaVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
          TipoTipologiaVinoVO tipoTipologiaVinoVO = new TipoTipologiaVinoVO();
          tipoTipologiaVinoVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
          tipoTipologiaVinoVO.setDescrizione(rs.getString("DESC_TIPO_VINO"));
          tipoTipologiaVinoVO.setFlagGestioneVigna(rs.getString("FLAG_GESTIONE_VIGNA"));
          storicoUnitaArboreaVO.setTipoTipologiaVinoVO(tipoTipologiaVinoVO);
        }
        storicoUnitaArboreaVO.setStatoUnitaArborea(rs.getString("STATO_UNITA_ARBOREA"));
        storicoUnitaArboreaVO.setAnnoRiferimento(rs.getString("ANNO_RIFERIMENTO"));
        
        
        if(Validator.isNotEmpty(rs.getString("ID_CAUSALE_MODIFICA"))) {
          storicoUnitaArboreaVO.setIdCausaleModifica(new Long(rs.getLong("ID_CAUSALE_MODIFICA")));
          TipoCausaleModificaVO tipoCausaleModificaVO = new TipoCausaleModificaVO();
          tipoCausaleModificaVO.setIdCausaleModifica(new Long(rs.getLong("ID_CAUSALE_MODIFICA")));
          tipoCausaleModificaVO.setDescrizione(rs.getString("DESC_CAUS_MOD"));
          storicoUnitaArboreaVO.setTipoCausaleModificaVO(tipoCausaleModificaVO);
        }
        
        storicoUnitaArboreaVO.setFlagImproduttiva(rs.getString("FLAG_IMPRODUTTIVA"));
        storicoUnitaArboreaVO.setPercentualeFallanza(rs.getBigDecimal("PERCENTUALE_FALLANZA"));
        
        ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
        conduzioneParticellaVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
        ConduzioneParticellaVO[] elencoConduzioniPart = new ConduzioneParticellaVO[1];
        elencoConduzioniPart[0] = conduzioneParticellaVO;
        storicoParticellaVO.setElencoConduzioni(elencoConduzioniPart);
        storicoParticellaVO.setStoricoUnitaArboreaVO(storicoUnitaArboreaVO);
        
        
        FoglioVO foglioVO = new FoglioVO();
        foglioVO.setFlagStabilizzazione(checkIntegerNull(rs.getString("FLAG_STABILIZZAZIONE")));
        storicoParticellaVO.setFoglioVO(foglioVO);
        
        
        
        elencoParticelleArboree.add(storicoParticellaVO);
      }
      
      rs.close();
      stmt.close();
      
      if (elencoParticelleArboree != null && elencoParticelleArboree.size() > 0)
      {
        PaginazioneUtils page = PaginazioneUtils.newInstance(elencoParticelleArboree.size(), Integer
            .parseInt(SolmrConstants.NUMBER_RECORDS_FOR_PAGE_TERRENI), filtriUnitaArboreaRicercaVO.getPaginaCorrente());
        if (page != null)
        {
          Vector<Long> listIdStoricoUnitaArborea = getIdStoricoUnitaArborea(elencoParticelleArboree, page);
          
          Hashtable<String,StoricoParticellaVO> caso=new Hashtable<String,StoricoParticellaVO>();
          ricercaSecondaQuery(caso, idAzienda, listIdStoricoUnitaArborea, p26Valore, nomeLib, conn);
          aggiungiRisultatiSecondaQuery(elencoParticelleArboree,page,caso);
        }
      }
      
      
      
      // Secondo monitoraggio
      watcher
          .dumpElapsed(
              "StoricoUnitaArboreaDAO",
              "searchParticelleByParameters",
              "In searchParticelleByParameters method from the creation of query to final setting of parameters inside of Vector after resultset's while cicle",
              "");
      
      // STOP
      watcher.stop();
      

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "searchStoricoUnitaArboreaByParameters in StoricoUnitaArboreaDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "searchStoricoUnitaArboreaByParameters in StoricoUnitaArboreaDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "searchStoricoUnitaArboreaByParameters in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "searchStoricoUnitaArboreaByParameters in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated searchStoricoUnitaArboreaByParameters method in StoricoUnitaArboreaDAO\n");
    if(elencoParticelleArboree.size() == 0) {
      return (StoricoParticellaVO[])elencoParticelleArboree.toArray(new StoricoParticellaVO[0]);
    }
    else {
      return (StoricoParticellaVO[])elencoParticelleArboree.toArray(new StoricoParticellaVO[elencoParticelleArboree.size()]);
    }
  }
  
  
  public Vector<StoricoParticellaVO> getElencoStoricoUnitaArboreaBasic(
      Long idAzienda, FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getElencoStoricoUnitaArboreaBasic method in StoricoUnitaArboreaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> elencoParticelleArboree = new Vector<StoricoParticellaVO>();

    try 
    {
      
      SolmrLogger.debug(this, "Creating db-connection in getElencoStoricoUnitaArboreaBasic method in StoricoUnitaArboreaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getElencoStoricoUnitaArboreaBasic method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");

      String query = " " +
        "SELECT SP.ID_STORICO_PARTICELLA, " +
        "       SP.COMUNE, " +
        "       C.DESCOM, " +
        "       P.SIGLA_PROVINCIA, " +
        "       P.ISTAT_PROVINCIA, " +
        "       SP.SEZIONE, " +
        "       SP.FOGLIO, " +
        "       SP.PARTICELLA, " +
        "       SP.SUBALTERNO, " +
        "       SUA.ID_STORICO_UNITA_ARBOREA, " +
        "       SUA.ID_UNITA_ARBOREA, " +
        "       SUA.ID_PARTICELLA, " +
        "       SUA.PROGR_UNAR, " +
        "       SUA.ID_TIPOLOGIA_UNAR, " +
        "       TTU.DESCRIZIONE AS DESC_TIPO_UNAR, " +
        "       SUA.AREA, " +
        "       SUA.SESTO_SU_FILA, " +
        "       SUA.SESTO_TRA_FILE, " +
        "       SUA.NUM_CEPPI, " +
        "       SUA.ANNO_IMPIANTO, " +
        "       SUA.DATA_IMPIANTO, " +
        "       SUA.ANNO_ISCRIZIONE_ALBO, " +
        "       SUA.ID_VARIETA, " +
        "       TVAR.DESCRIZIONE AS DESC_VARIETA, " +
        "       TVAR.CODICE_VARIETA AS COD_VAR, " +
        "       SUA.ID_UTILIZZO, " +
        "       TU.CODICE, " +
        "       TU.DESCRIZIONE AS DESC_TIPO_UTILIZZO, " +
        "       SUA.ID_TIPOLOGIA_VINO, " +
        "       TTV.DESCRIZIONE AS DESC_TIPO_VINO " +
        "FROM   DB_STORICO_PARTICELLA SP, " +
        "       COMUNE C, " +
        "       PROVINCIA P, " +
        "       DB_STORICO_UNITA_ARBOREA SUA, " +
        "       DB_TIPO_TIPOLOGIA_UNAR TTU, " +
        "       DB_TIPO_VARIETA TVAR, " +
        "       DB_TIPO_UTILIZZO TU, " +
        "       DB_UTE U, " +
        "       DB_CONDUZIONE_PARTICELLA CP, " +                      
        "       DB_TIPO_TIPOLOGIA_VINO TTV " +
        "WHERE  SUA.ID_PARTICELLA = SP.ID_PARTICELLA " +
        "AND    SP.COMUNE = C.ISTAT_COMUNE " +
        "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
        "AND    SUA.ID_TIPOLOGIA_UNAR = TTU.ID_TIPOLOGIA_UNAR(+) " +
        "AND    SUA.ID_VARIETA = TVAR.ID_VARIETA(+) " +
        "AND    SUA.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
        "AND    SUA.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO(+) " +
        "AND    SUA.ID_AZIENDA = ? " +
        "AND    SUA.ID_AZIENDA = U.ID_AZIENDA " +
        "AND    U.ID_UTE = CP.ID_UTE " +
        "AND    CP.ID_PARTICELLA = SUA.ID_PARTICELLA " +
        "AND    CP.ID_TITOLO_POSSESSO NOT IN (5,6) " +
        "AND    U.DATA_FINE_ATTIVITA IS NULL " +
        "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "AND    SP.DATA_FINE_VALIDITA IS NULL " +
        "AND    SUA.DATA_FINE_VALIDITA IS NULL ";
      // Se l'utente ha indicato la destinazione produttiva
      if(filtriUnitaArboreaRicercaVO.getIdUtilizzo() != null) {
        query +=   " AND    SUA.ID_UTILIZZO = ? ";
      }
      // Se l'utente ha indicato il vitigno
      if(filtriUnitaArboreaRicercaVO.getIdVarieta() != null) {
        query +=   " AND    SUA.ID_VARIETA = ? ";
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
          query += " AND      SUA.ID_TIPOLOGIA_VINO IS NULL ";
        }
        else query += " AND      SUA.ID_TIPOLOGIA_VINO = ? ";
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
         "          P.ISTAT_PROVINCIA, " +
         "          SP.SEZIONE, " +
         "          SP.FOGLIO, " +
         "          SP.PARTICELLA, " +
         "          SP.SUBALTERNO, " +
         "          SUA.ID_STORICO_UNITA_ARBOREA, " +
         "          SUA.ID_UNITA_ARBOREA, " +
         "          SUA.ID_PARTICELLA, " +
         "          SUA.PROGR_UNAR, " +
         "          SUA.ID_TIPOLOGIA_UNAR, " +
         "          TTU.DESCRIZIONE, " +
         "          SUA.AREA, " +
         "          SUA.SESTO_SU_FILA, " +
         "          SUA.SESTO_TRA_FILE, " +
         "          SUA.NUM_CEPPI, " +
         "          SUA.ANNO_IMPIANTO, " +
         "          SUA.DATA_IMPIANTO, " +
         "          SUA.ANNO_ISCRIZIONE_ALBO, " +
         "          SUA.ID_VARIETA, " +
         "          TVAR.DESCRIZIONE, " +
         "          TVAR.CODICE_VARIETA, " +
         "          SUA.ID_UTILIZZO, " +
         "          TU.CODICE, " +
         "          TU.DESCRIZIONE, " +
         "          SUA.ID_TIPOLOGIA_VINO, " +
         "          TTV.DESCRIZIONE " +
         "ORDER BY  C.DESCOM, " +
         "          SP.SEZIONE, " +
         "          SP.FOGLIO, " +
         "          SP.PARTICELLA, " +
         "          SP.SUBALTERNO, " +
         "          TTU.DESCRIZIONE ASC, " +
         "          PROGR_UNAR ASC ";
     
      
      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getElencoStoricoUnitaArboreaBasic method in StoricoUnitaArboreaDAO: "+idAzienda+"\n");

      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      stmt.setLong(++indice, idAzienda.longValue());
      // Se l'utente ha indicato la destinazione produttiva
      if(filtriUnitaArboreaRicercaVO.getIdUtilizzo() != null) {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdUtilizzo().longValue());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_UTILIZZO] in getElencoStoricoUnitaArboreaBasic method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIdUtilizzo()+"\n");
      }
      // Se l'utente ha indicato il vitigno
      if(filtriUnitaArboreaRicercaVO.getIdVarieta() != null) {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdVarieta().longValue());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_VARIETA] in getElencoStoricoUnitaArboreaBasic method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIdVarieta()+"\n");
      }
      // Se l'utente ha indicato il tipo vino
      if(filtriUnitaArboreaRicercaVO.getIdTipologiaVino() != null &&
        filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue() != new Long(-1) &&
        filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue() != new Long(0)) 
      {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_TIPOLOGIA_VINO] in getElencoStoricoUnitaArboreaBasic method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIdTipologiaVino()+"\n");
      }
      // Se l'utente ha indicato la provincia di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIstatProvincia())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getIstatProvincia());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ISTAT_PROVINCIA] in getElencoStoricoUnitaArboreaBasic method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIstatProvincia()+"\n");
      }
      // Se l'utente ha indicato il comune di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIstatComune())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getIstatComune());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ISTAT_COMUNE] in getElencoStoricoUnitaArboreaBasic method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIstatComune()+"\n");
      }
      // Se l'utente ha indicato la sezione di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSezione())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getSezione().toUpperCase());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[SEZIONE] in getElencoStoricoUnitaArboreaBasic method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getSezione().toUpperCase()+"\n");
      }
      // Se l'utente ha indicato il foglio di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getFoglio())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getFoglio());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[FOGLIO] in getElencoStoricoUnitaArboreaBasic method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getFoglio()+"\n");
      }
      // Se l'utente ha indicato la particella di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getParticella())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getParticella());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[PARTICELLA] in getElencoStoricoUnitaArboreaBasic method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getParticella()+"\n");
      }
      // Se l'utente ha indicato il subalterno di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSubalterno())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getSubalterno());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[SUBALTERNO] in getElencoStoricoUnitaArboreaBasic method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getSubalterno()+"\n");
      }
      
      SolmrLogger.debug(this, "Executing getElencoStoricoUnitaArboreaBasic: "+query+"\n");

      ResultSet rs = stmt.executeQuery();
      
      while(rs.next()) 
      {
        StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
        StoricoUnitaArboreaVO storicoUnitaArboreaVO = new StoricoUnitaArboreaVO();
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
        storicoUnitaArboreaVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
        storicoUnitaArboreaVO.setIdUnitaArborea(new Long(rs.getLong("ID_UNITA_ARBOREA")));
        storicoUnitaArboreaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
        storicoUnitaArboreaVO.setProgrUnar(rs.getString("PROGR_UNAR"));
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_UNAR"))) {
          storicoUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
          TipoTipologiaUnitaArboreaVO tipoTipologiaUnitaArboreaVO = new TipoTipologiaUnitaArboreaVO();
          tipoTipologiaUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
          tipoTipologiaUnitaArboreaVO.setDescrizione(rs.getString("DESC_TIPO_UNAR"));
          storicoUnitaArboreaVO.setTipoTipologiaUnitaArboreaVO(tipoTipologiaUnitaArboreaVO);
        }
        storicoUnitaArboreaVO.setArea(rs.getString("AREA"));
        storicoUnitaArboreaVO.setSestoSuFila(rs.getString("SESTO_SU_FILA"));
        storicoUnitaArboreaVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
        storicoUnitaArboreaVO.setNumCeppi(rs.getString("NUM_CEPPI"));
        storicoUnitaArboreaVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
        storicoUnitaArboreaVO.setDataImpianto(rs.getDate("DATA_IMPIANTO"));
        storicoUnitaArboreaVO.setAnnoIscrizioneAlbo(rs.getString("ANNO_ISCRIZIONE_ALBO"));
        if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) {
          storicoUnitaArboreaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
          tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          tipoVarietaVO.setDescrizione(rs.getString("DESC_VARIETA"));
          tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VAR"));
          storicoUnitaArboreaVO.setTipoVarietaVO(tipoVarietaVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO"))) {
          storicoUnitaArboreaVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
          TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
          tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
          tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
          tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTILIZZO"));
          storicoUnitaArboreaVO.setTipoUtilizzoVO(tipoUtilizzoVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VINO"))) 
        {
          storicoUnitaArboreaVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
          TipoTipologiaVinoVO tipoTipologiaVinoVO = new TipoTipologiaVinoVO();
          tipoTipologiaVinoVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
          tipoTipologiaVinoVO.setDescrizione(rs.getString("DESC_TIPO_VINO"));
          storicoUnitaArboreaVO.setTipoTipologiaVinoVO(tipoTipologiaVinoVO);
        }        
        
        
        storicoParticellaVO.setStoricoUnitaArboreaVO(storicoUnitaArboreaVO);  
        
        elencoParticelleArboree.add(storicoParticellaVO);
      }
      
      rs.close();
      stmt.close();    
      

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getElencoStoricoUnitaArboreaBasic in StoricoUnitaArboreaDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getElencoStoricoUnitaArboreaBasic in StoricoUnitaArboreaDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getElencoStoricoUnitaArboreaBasic in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getElencoStoricoUnitaArboreaBasic in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getElencoStoricoUnitaArboreaBasic method in StoricoUnitaArboreaDAO\n");
    
    return elencoParticelleArboree;
  }
  
  
  
  private Vector<Long> getIdStoricoUnitaArborea(Vector<StoricoParticellaVO> elencoParticelle, PaginazioneUtils page)
  {
    Vector<Long> result = new Vector<Long>();
    for (int i = page.getPrimoElementoPaginaCorrente(); i < page.getUltimoElementoPaginaCorrente(); i++)
    {
      StoricoParticellaVO stVO = ((StoricoParticellaVO) elencoParticelle.get(i));
      Long idStoricoUnitaArborea = stVO.getStoricoUnitaArboreaVO().getIdStoricoUnitaArborea();
      if(!result.contains(idStoricoUnitaArborea))
      {
        result.add(idStoricoUnitaArborea);
      }
    }
    
    return result;
  }
  
  
  /**
   * 
   * Esegue la seconda query della ricerca unita arborea per le varie anomalie P...
   * 
   * 
   * 
   * 
   * @param caso
   * @param idAzienda
   * @param listaIdStoricoParticella
   * @param listaIdConduzioneParticella
   * @param listaIdParticella
   * @param conn
   * @throws DataAccessException
   */
  private void ricercaSecondaQuery(Hashtable<String,StoricoParticellaVO> caso, 
      Long idAzienda, Vector<Long> listaIdStoricoUnitaArborea, BigDecimal p26Valore, String nomeLib, Connection conn)
      throws DataAccessException
  {
    SolmrLogger.debug(this, "Invocating ricercaSecondaQuery method in StoricoUnitaArboreaDAO\n");
    PreparedStatement stmt = null;
    
    Integer numElementiPaginazioneIntg = new Integer(SolmrConstants.NUMBER_RECORDS_FOR_PAGE_TERRENI);
    int numElementiPaginazione = numElementiPaginazioneIntg.intValue();
    String interrogativiByPaginazione = getPuntiInterrogativiByPaginazione(numElementiPaginazione); 
    
    try
    {
      String query = "" +
      		"SELECT UV_TABLE.*, "+
      		"       (NVL(PCK_SMRGAA_LIBRERIA.SelTotSupElegByPartEVetCat(UV_TABLE.ID_PARTICELLA_CERTIFICATA," +
          "                                                           UV_TABLE.ID_CATALOGO_MATRICE),0) " +
          "       ) AS SUPERFICIE_ELEG, " +
          "       ( " +
          "         CASE " +
          "         WHEN (UV_TABLE.EFFETTUA_CONTROLLI_GIS = 'S' " +
          "               AND NVL(PCK_SMRGAA_LIBRERIA.Calcola_P30(UV_TABLE.ID_STORICO_PARTICELLA,SYSDATE),0) !=0) " +
          "         THEN 'P30' " +
          "         END " +
          "       ) AS P30, " +
          "       ( " +
          "         CASE " +
          "         WHEN (UV_TABLE.EFFETTUA_CONTROLLI_GIS = 'S' " + 
          "               AND NVL(PCK_SMRGAA_LIBRERIA.Calcola_P25(UV_TABLE.ID_STORICO_PARTICELLA,SYSDATE),0) !=0) " +
          "         THEN 'P25' " +
          "         END " +
          "       ) AS P25, " +
          "       ( " +
          "         CASE " +
          "         WHEN (UV_TABLE.EFFETTUA_CONTROLLI_GIS = 'S' " +
          "         AND   NVL(PCK_SMRGAA_LIBRERIA.Calcola_P26_Con_Tolleranza( ?, UV_TABLE.ID_PARTICELLA,UV_TABLE.ID_PARTICELLA_CERTIFICATA,?),0) !=0) " +
          "         THEN 'P26' " +
          "         END " +
          "       ) AS P26, " +
          "       ( " +
          "         CASE " +
          "         WHEN (UV_TABLE.DATA_SOSPENSIONE IS NOT NULL) " +
          "         THEN 'SOSPESA_GIS' " +
          "         END " +
          "       ) AS SOSPESA_GIS," +
          "       (" +
          "         CASE " +
          "         WHEN EXISTS (SELECT NE.ID_NOTIFICA_ENTITA " + 
          "               FROM   DB_NOTIFICA_ENTITA NE, " +
          "                      DB_NOTIFICA NO, " +
          "                      DB_TIPO_ENTITA TE " +
          "               WHERE  TE.CODICE_TIPO_ENTITA = 'U' " +
          "               AND    TE.ID_TIPO_ENTITA = NE.ID_TIPO_ENTITA " +
          "               AND    NE.IDENTIFICATIVO = UV_TABLE.ID_UNITA_ARBOREA " +
          "               AND    NE.ID_NOTIFICA = NO.ID_NOTIFICA " +
          "               AND    NO.ID_AZIENDA = UV_TABLE.ID_AZIENDA ) " +
          "         THEN 'NOTIFICA' " +
          "         END " +
          "        ) AS IN_NOTIFICA " +
          "FROM " +
          "      (WITH COND_ELEG AS " +
          "       (SELECT CE.ID_PARTICELLA, " +
          "               CE.PERCENTUALE_UTILIZZO " +
          "        FROM   DB_CONDUZIONE_ELEGGIBILITA CE " +
          "        WHERE  CE.ID_AZIENDA = ? " + 
          "        AND    CE.DATA_FINE_VALIDITA IS NULL" +
          "        AND    CE.ID_ELEGGIBILITA_FIT = 26 ) " +
          "       SELECT PC.SUP_COLT_ARBOREA_SPECIALIZZATA, " +
          "              PC.SUP_USO_GRAFICA, " +
          "              PC.SUP_GRAFICA, " +             
          "              PC.ID_PARTICELLA_CERTIFICATA, " +
          "              PC.DATA_SOSPENSIONE, " +
          "              PC.MOTIVAZIONE_GIS, " +
          "              PC.ESITO, " +
          "              PC.PARTICELLA_A_GIS, " +
          "              SUA.ID_STORICO_UNITA_ARBOREA, " +
          "              SUA.ID_CATALOGO_MATRICE, " +
          "              SUA.ID_UNITA_ARBOREA, " +
          "              SUA.ID_AZIENDA, " +
          "              SUA.ID_PARTICELLA, " +
          "              SUA.ID_VARIETA, " +
          "              STO.ID_STORICO_PARTICELLA, " +
          "              TCP.EFFETTUA_CONTROLLI_GIS, " +
          "              CEL.PERCENTUALE_UTILIZZO, " +
          "              1 AS TOLLERANZA, " +
          "              Pck_Smrgaa_Libreria.CercaIstanzaRiesame(EXTRACT(YEAR FROM SYSDATE), ?, SUA.ID_PARTICELLA) AS ISTANZA_RIESAME " +
          "        FROM  DB_STORICO_UNITA_ARBOREA SUA, " +
          "              DB_PARTICELLA_CERTIFICATA PC, " +
          "              DB_STORICO_PARTICELLA STO, " +
          "              COND_ELEG CEL, " +
          "              DB_TIPO_CASO_PARTICOLARE TCP " +
          "        WHERE PC.DATA_FINE_VALIDITA(+)  IS NULL " +
          "        AND   SUA.ID_PARTICELLA = PC.ID_PARTICELLA(+) " +
          "        AND   SUA.ID_STORICO_UNITA_ARBOREA IN "+interrogativiByPaginazione+
          "        AND   STO.ID_PARTICELLA = SUA.ID_PARTICELLA " +
          "        AND   STO.DATA_FINE_VALIDITA IS NULL " +
          "        AND   SUA.ID_PARTICELLA = CEL.ID_PARTICELLA (+) " +
          "        AND   STO.ID_CASO_PARTICOLARE = TCP.ID_CASO_PARTICOLARE (+) " +
          "      ) UV_TABLE ";

      stmt = conn.prepareStatement(query);
      
      int idx=0;
      stmt.setLong(++idx, idAzienda.longValue());
      stmt.setBigDecimal(++idx, p26Valore);
      //CONDUZIONE_ELEGGIBILE
      stmt.setLong(++idx, idAzienda.longValue());
      //Tolleranza
     // stmt.setLong(++idx, idAzienda.longValue());
      //istanza riesame
      stmt.setLong(++idx, idAzienda.longValue());
      idx = setLongStatementPaginazione(stmt,listaIdStoricoUnitaArborea,idx,numElementiPaginazione);
      
      
      SolmrLogger.debug(this, "Executing ricercaSecondaQuery: " + query + "\n");
      
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        String idStoricoUnitaArborea = rs.getString("ID_STORICO_UNITA_ARBOREA");
        StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
        
        if(Validator.isNotEmpty(rs.getString("ID_PARTICELLA_CERTIFICATA"))) 
        {
          ParticellaCertificataVO particellaCertificataVO = new ParticellaCertificataVO();
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
          particellaCertificataVO.setDataSospensione(rs.getDate("DATA_SOSPENSIONE"));
          particellaCertificataVO.setMotivazioneGis(rs.getString("MOTIVAZIONE_GIS"));
          particellaCertificataVO.setSupGrafica(rs.getString("SUP_GRAFICA"));
          particellaCertificataVO.setSupUsoGrafica(rs.getString("SUP_USO_GRAFICA"));
          
          if (Validator.isNotEmpty(rs.getString("ESITO")))
            particellaCertificataVO.setEsito(new Integer(rs.getInt("ESITO")));
          
          if (Validator.isNotEmpty(rs.getString("PARTICELLA_A_GIS")))
            particellaCertificataVO.setEsito(new Integer(rs.getInt("PARTICELLA_A_GIS")));         
          
          storicoParticellaVO.setParticellaCertificataVO(particellaCertificataVO);
        }
        
        //*****************Controlli P
        storicoParticellaVO.setGisP30(rs.getString("P30"));
        storicoParticellaVO.setGisP25(rs.getString("P25"));
        storicoParticellaVO.setGisP26(rs.getString("P26"));
        storicoParticellaVO.setSospesaGis(rs.getString("SOSPESA_GIS"));
        //**************************
        storicoParticellaVO.setIstanzaRiesame(checkLong(rs.getString("ISTANZA_RIESAME")));
        
        StoricoUnitaArboreaVO storicoUnitaArboreaVO = new StoricoUnitaArboreaVO();
        storicoUnitaArboreaVO.setTolleranza(checkInt(rs.getString("TOLLERANZA")));
        storicoUnitaArboreaVO.setInNotifica(rs.getString("IN_NOTIFICA"));
        storicoParticellaVO.setStoricoUnitaArboreaVO(storicoUnitaArboreaVO);
        storicoParticellaVO.setPercentualeUtilizzoEleg(rs.getBigDecimal("PERCENTUALE_UTILIZZO"));
        
        caso.put(idStoricoUnitaArborea,storicoParticellaVO);
      }
      
      rs.close();
      stmt.close();
      
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "ricercaSecondaQuery in StoricoUnitaArboreaDAO - SQLException: " + exc + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this, "ricercaSecondaQuery in StoricoUnitaArboreaDAO - Generic Exception: " + ex + "\n");
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
            .error(this, "ricercaSecondaQuery in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: " + exc + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.error(this, "ricercaSecondaQuery in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: " + ex
            + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated ricercaSecondaQuery method in StoricoUnitaArboreaDAO\n");
  }
  
  
  private void aggiungiRisultatiSecondaQuery(Vector<StoricoParticellaVO> elencoParticelle, PaginazioneUtils page,
      Hashtable<String,StoricoParticellaVO> caso)
  {
    //Ciclo sulle particelle per andare ad aggiungere i risultati ottenuti dalla seconda query
    for (int i = page.getPrimoElementoPaginaCorrente(); i < page.getUltimoElementoPaginaCorrente(); i++)
    {
      StoricoParticellaVO storicoParticellaVO = ((StoricoParticellaVO) elencoParticelle.get(i));
      if (storicoParticellaVO!=null)
      {
        String idStoricoUnitaArborea =""+storicoParticellaVO.getStoricoUnitaArboreaVO().getIdStoricoUnitaArborea();
        StoricoParticellaVO storicoPartDatiAgg = (StoricoParticellaVO) caso.get(idStoricoUnitaArborea);
        
        if (storicoPartDatiAgg!=null)
        {          
          //*****************Controlli P
          storicoParticellaVO.setGisP30(storicoPartDatiAgg.getGisP30());
          storicoParticellaVO.setGisP25(storicoPartDatiAgg.getGisP25());
          storicoParticellaVO.setGisP26(storicoPartDatiAgg.getGisP26());
          storicoParticellaVO.setSospesaGis(storicoPartDatiAgg.getSospesaGis());
          //**************************
          
          storicoParticellaVO.setIstanzaRiesame(storicoPartDatiAgg.getIstanzaRiesame());
          
          if(storicoPartDatiAgg.getParticellaCertificataVO()!=null) 
            storicoParticellaVO.setParticellaCertificataVO(storicoPartDatiAgg.getParticellaCertificataVO());
          
          storicoParticellaVO.getStoricoUnitaArboreaVO().setTolleranza(
              storicoPartDatiAgg.getStoricoUnitaArboreaVO().getTolleranza());
          storicoParticellaVO.getStoricoUnitaArboreaVO().setInNotifica(
              storicoPartDatiAgg.getStoricoUnitaArboreaVO().getInNotifica());
          if(storicoPartDatiAgg.getPercentualeUtilizzoEleg() != null)
          {
            storicoParticellaVO.setPercentualeUtilizzoEleg(storicoPartDatiAgg.getPercentualeUtilizzoEleg());            
          }
          else
          {
            storicoParticellaVO.setPercentualeUtilizzoEleg(storicoParticellaVO.getElencoConduzioni()[0].getPercentualePossesso());
          }
        }
      }
    }
  }
  
  
	
	/**
	 * Metodo che mi consente di effettuare la ricerca delle UV in funzione del loro scarico in excel
	 * 
	 * @param idAzienda
	 * @param idPianoRiferimento
	 * @param orderBy
	 * @return java.util.Vector
	 * @throws DataAccessException
	 */
	public Vector<StoricoParticellaArboreaExcelVO> searchStoricoUnitaArboreaExcelByParameters(String nomeLib, Long idAzienda, 
	    FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO, String parametroPTUV, String parametroMTUV) throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating searchStoricoUnitaArboreaExcelByParameters method in StoricoUnitaArboreaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<StoricoParticellaArboreaExcelVO> elencoParticelleArboree = new Vector<StoricoParticellaArboreaExcelVO>();

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in searchStoricoUnitaArboreaExcelByParameters method in StoricoUnitaArboreaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in searchStoricoUnitaArboreaExcelByParameters method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");

			String query = 
			  /*"WITH PARCELLE_GIS AS " +
			  "     (SELECT I.ID_ILO, " +
        "             IP.SUPERFICIE AS SUP_PARCELLA, " +
        "             CD.ID_PARTICELLA, " +
        "             IP.TOLLERANZA AS TOLLERANZA_PARCELLA," +
        "             IP.ID_ISOLA_PARCELLA " +
        "      FROM   DB_ISOLA_PARCELLA IP, " +
        "             DB_ISOLA I, " +
        "             DB_ISOLA_DICHIARATA ID, " +
        "             DB_PARCELLA_CONDUZIONE PC, " +
        "             DB_CONDUZIONE_DICHIARATA CD " +
        "      WHERE  I.ID_ISOLA = IP.ID_ISOLA " + 
        "      AND    I.ID_ISOLA_DICHIARATA = ID.ID_ISOLA_DICHIARATA " +
        "      AND    ID.ID_DICHIARAZIONE_CONSISTENZA = " +
        "                                              (SELECT MAX(DC1.ID_DICHIARAZIONE_CONSISTENZA) " +
        "                                               FROM   DB_DICHIARAZIONE_CONSISTENZA DC1, " +
        "                                                      DB_ISOLA_DICHIARATA ID1, " +
        "                                                      DB_TIPO_MOTIVO_DICHIARAZIONE MD " +
        "                                               WHERE  DC1.ID_DICHIARAZIONE_CONSISTENZA = ID1.ID_DICHIARAZIONE_CONSISTENZA " +
        "                                               AND    ID1.DATA_FINE_VALIDITA IS NULL " +
        "                                               AND    DC1.ID_AZIENDA = ? " +
        "                                               AND    MD.ID_MOTIVO_DICHIARAZIONE = DC1.ID_MOTIVO_DICHIARAZIONE " +
        "                                               AND    MD.TIPO_DICHIARAZIONE <> 'C' " +
        "                                              ) " +
        "      AND    PC.ID_ISOLA_PARCELLA = IP.ID_ISOLA_PARCELLA " +
        "      AND    PC.ID_CONDUZIONE_DICHIARATA = CD.ID_CONDUZIONE_DICHIARATA " +
        "      AND    IP.ID_ELEGGIBILITA_FIT = 26 " +
        "      AND    ID.DATA_FINE_VALIDITA IS NULL " +
        "     ), " +*/
        "WITH  PARTICELLE AS " +
        "  (SELECT DISTINCT PC.ID_PARTICELLA_CERTIFICATA," +
        "                   PC.ID_PARTICELLA," +
        "                   PC.SUP_GRAFICA, " +
        "                   PC.SUP_USO_GRAFICA " + 
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
        "    ISTANZA_RIESAME AS " +
        "                (SELECT IR.DATA_RICHIESTA, " +
        "                        IR.DATA_EVASIONE, " +                
        "                        IR.ID_PARTICELLA " +
        "                 FROM   DB_ISTANZA_RIESAME IR " +
        "                        WHERE IR.DATA_RICHIESTA = ( " +
        "                                                    SELECT MAX(IRTMP.DATA_RICHIESTA) " +
        "                                                    FROM   DB_ISTANZA_RIESAME IRTMP " +                                  
        "                                                    WHERE  IRTMP.ID_AZIENDA = IR.ID_AZIENDA " +
        "                                                    AND    IRTMP.ID_PARTICELLA = IR.ID_PARTICELLA " +
        "                                                    AND    IRTMP.DATA_ANNULLAMENTO IS NULL " +
        "                                                   ) " +
        "                        AND   IR.ID_AZIENDA = ? " +
        "                ), " +
        "    COND_ELEG AS " +
        "       (SELECT CE.ID_PARTICELLA, " +
        "               CE.PERCENTUALE_UTILIZZO " +
        "        FROM   DB_CONDUZIONE_ELEGGIBILITA CE " +
        "        WHERE  CE.ID_AZIENDA = ? " + 
        "        AND    CE.DATA_FINE_VALIDITA IS NULL" +
        "        AND    CE.ID_ELEGGIBILITA_FIT = 26 ) " +
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
		    "        SUM(CP.PERCENTUALE_POSSESSO) AS PERCENTUALE_POSSESSO, " +
		    "        PAR.FLAG_SCHEDARIO, " +
	      "        SUA.ID_STORICO_UNITA_ARBOREA, " +
		    "        SUA.ID_UNITA_ARBOREA, " +
		    "        SUA.ID_CATALOGO_MATRICE, " +
		    "        SUA.ID_PARTICELLA, " +
		    "        SUA.PROGR_UNAR, " +
		    "        SUA.DATA_INIZIO_VALIDITA, " +
		    "        SUA.DATA_LAVORAZIONE, " +
		    "        SUA.ID_TIPOLOGIA_UNAR, " +
		    "        TTU.DESCRIZIONE AS DESC_TIPO_UNAR, " +
		    "        SUA.AREA, " +
		    "        SUA.DATA_IMPIANTO, " +
		    "        SUA.DATA_PRIMA_PRODUZIONE, " +
		    "        SUA.ANNO_REINNESTO, " +
		    "        SUA.ID_IRRIGAZIONE_UNAR, " +
		    "        SUA.ID_COLTIVAZIONE_UNAR, " +
		    "        SUA.CODICE_TIPO_VARIETA, " +
		    "        SUA.NUMERO_PIANTE_PRODUTTIVO, " +
		    "        SUA.NUMERO_ALTRE_PIANTE, " +
		    "        SUA.CAMPAGNA, " +
		    "        SUA.ID_TIPOLOGIA_VIGNETO, " +
		    "        SUA.TIPO_IMPIANTO, " +
		    "        SUA.NUMERO_CASTAGNI, " +
		    "        SUA.GRUPPO, " +
		    "        SUA.RICADUTA, " +
		    "        SUA.ID_GIACITURA_UNAR, " +
		    "        SUA.ID_ROCCIA_UNAR, " +
		    "        SUA.ID_SCHELETRO_UNAR, " +
		    "        SUA.ID_STATO_VEGETATIVO_UNAR, " +
		    "        SUA.ID_POTATURA_UNAR, " +
		    "        SUA.ID_GIUDIZIO_UNAR, " +
		    "        SUA.SUPPLEMENTARI, " +
		    "        SUA.MECCANIZZABILE, " +
		    "        SUA.DIMENSIONE_CHIOMA, " +
		    "        SUA.ID_ETA_IMPIANTO_UNAR, " +
		    "        SUA.PROVINCIA_CCIAA, " +
		    "        SUA.MATRICOLA_CCIAA, " +
		    "        SUA.CONFERMA_PREC_ISCRIZIONE_ALBO, " +
		    "        SUA.RICHIESTA_NUOVA_ISCR_ALBO, " +
		    "        SUA.CONFERMA_RICH_NUOVA_ISCR_ALBO, " +
		    "        SUA.SUPERFICIE_DA_ISCRIVERE_ALBO, " +
		    "        SUA.ANNO_ISCRIZIONE_ALBO, " +
		    "        SUA.ID_FONTE, " +
		    "        SUA.ID_VARIAZIONE_UNAR, " +
		    "        SUA.NOTE, " +
		    "        SUA.DATA_AGGIORNAMENTO, " +
		    "        SUA.ID_UTENTE_AGGIORNAMENTO, " +
		    "        RCM.ID_VARIETA, " +
		    "        TVAR.DESCRIZIONE AS DESC_VARIETA, " +
		    "        TVAR.CODICE_VARIETA AS COD_VAR, " +
		    "        RCM.ID_UTILIZZO, " +
		    "        TU.CODICE, " +
		    "        TU.DESCRIZIONE AS DESC_TIPO_UTILIZZO, " +
		    "        SUA.DATA_ESECUZIONE, " +
		    "        SUA.RECORD_MODIFICATO, " +
		    "        SUA.ESITO_CONTROLLO, " +
		    "        SUA.ID_TIPOLOGIA_VINO, " +
        "        TTV.DESCRIZIONE AS DESC_TIPO_VINO, " +
		    "        AA.CUAA, " +
		    "        AA.DENOMINAZIONE, " +
		    "        PART.ID_PARTICELLA_CERTIFICATA, "+
		    "        PART.SUP_GRAFICA, " +
        "        PART.SUP_USO_GRAFICA, " +
        "        NVL(PCK_SMRGAA_LIBRERIA.SelTotSupElegByPartEVetCat(PART.ID_PARTICELLA_CERTIFICATA, SUA.ID_CATALOGO_MATRICE),0) AS SUPERFICIE_ELEG, " +
        /*"        "+nomeLib+".UV_IN_TOLLERANZA_GIS(?, SUA.ID_UNITA_ARBOREA) AS TOLLERANZA, " +
        "        PAG.ID_ILO, " +
        "        PAG.SUP_PARCELLA, " +    
        "        PAG.TOLLERANZA_PARCELLA, " +
        "        PAG.ID_ISOLA_PARCELLA, " +
        "        (SELECT PARCELLA_SELEZIONATA " +
        "         FROM   DB_UNAR_PARCELLA UP " +
        "         WHERE  UP.ID_ISOLA_PARCELLA = PAG.ID_ISOLA_PARCELLA " +
        "         AND    UP.ID_UNITA_ARBOREA = SUA.ID_UNITA_ARBOREA ) " +
        "        ASSOCIATA_PARCELLA, " +*/
        "        ISR.DATA_RICHIESTA, " +
        "        ISR.DATA_EVASIONE, " +
        "        NVL(CEL.PERCENTUALE_UTILIZZO, SUM(CP.PERCENTUALE_POSSESSO)) AS PERCENTUALE_UTILIZZO " +
		    " FROM   DB_STORICO_PARTICELLA SP, " +
		    "        COMUNE C, " +
		    "        PROVINCIA P, " +
		    "        DB_PARTICELLA PAR, " +
		    "        DB_STORICO_UNITA_ARBOREA SUA, " +
		    "        DB_R_CATALOGO_MATRICE RCM, " +
		    "        DB_TIPO_TIPOLOGIA_UNAR TTU, " +
		    "        DB_TIPO_VARIETA TVAR, " +
		    "        DB_TIPO_UTILIZZO TU, " +
		    "        DB_UTE U, " +
		    "        DB_CONDUZIONE_PARTICELLA CP, " +
		    "        DB_TIPO_TITOLO_POSSESSO TTP, " +
		    "        DB_ANAGRAFICA_AZIENDA AA, " +
		    "        PARTICELLE PART, " +
		    "        DB_TIPO_TIPOLOGIA_VINO TTV, " +
		   // "        PARCELLE_GIS PAG, " +
        "        ISTANZA_RIESAME ISR," +
        "        COND_ELEG CEL ";
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdControllo()))
      {
        query += 
        "       ,DB_ESITO_CONTROLLO_UNAR ECU ";
      }
			query +=
		    " WHERE  SUA.ID_PARTICELLA = SP.ID_PARTICELLA " +
		    " AND    SP.ID_PARTICELLA = PAR.ID_PARTICELLA " +
		    " AND    SP.COMUNE = C.ISTAT_COMUNE " +
		    " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
	      " AND    SUA.ID_TIPOLOGIA_UNAR = TTU.ID_TIPOLOGIA_UNAR(+) " +
	      " AND    SUA.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE(+) " +
        " AND    RCM.ID_VARIETA = TVAR.ID_VARIETA(+) " +
        " AND    RCM.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
        " AND    SUA.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO(+) " +
		    " AND    SUA.ID_AZIENDA = ? " +
		    " AND    AA.ID_AZIENDA = SUA.ID_AZIENDA " +
		    " AND    AA.ID_AZIENDA = U.ID_AZIENDA " +
        " AND    U.ID_UTE = CP.ID_UTE " +
        " AND    CP.ID_PARTICELLA = SUA.ID_PARTICELLA " +
        " AND    CP.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO " +
        " AND    CP.ID_TITOLO_POSSESSO NOT IN (5,6) " +
        " AND    U.DATA_FINE_ATTIVITA IS NULL " +
        " AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
		    " AND    AA.DATA_FINE_VALIDITA IS NULL " +
        " AND    SP.DATA_FINE_VALIDITA IS NULL " +
        " AND    SP.ID_PARTICELLA = PART.ID_PARTICELLA(+) " +
       // " AND    CP.ID_PARTICELLA = PAG.ID_PARTICELLA(+) " +
        " AND    SP.ID_PARTICELLA = ISR.ID_PARTICELLA(+) " +
        " AND    SP.ID_PARTICELLA = CEL.ID_PARTICELLA(+) ";
			if(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() < 0) {
				query +=   " AND    SUA.DATA_FINE_VALIDITA IS NULL ";
			}
			
			//se l'utente ha selezionato un tipo di controllo
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdControllo()))
      {
        query += 
        "AND    SUA.ID_STORICO_UNITA_ARBOREA = ECU.ID_STORICO_UNITA_ARBOREA " +
        "AND    ECU.ID_CONTROLLO = ? ";
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
				  query += " AND      SUA.ID_TIPOLOGIA_VINO IS NULL ";			  
	    	}
	    	else
	    	{
	    	  query += " AND      SUA.ID_TIPOLOGIA_VINO = ? ";
	    	}
	    }
	    // Se l'utente ha indicato il genere iscrizione
      if(filtriUnitaArboreaRicercaVO.getIdGenereIscrizione() != null) {
        query +=   " AND    SUA.ID_GENERE_ISCRIZIONE = ? ";
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
                   "               AND    NO.ID_AZIENDA = SUA.ID_AZIENDA ";
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
                     "                                    AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA )) )";
        }
        else
        {
          query +=   " AND    NE.DATA_FINE_VALIDITA IS NULL )";
        }
        
      }
      
      
      // Se l'utente ha indicato la causale modifica
      if(filtriUnitaArboreaRicercaVO.getIdCausaleModifica() != null) {
        query +=   " AND    SUA.ID_CAUSALE_MODIFICA = ? ";
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
				query += " AND (SUA.ESITO_CONTROLLO = ? ";
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
				query += " SUA.ESITO_CONTROLLO = ? ";
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
				query += " SUA.ESITO_CONTROLLO = ? ";
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
					 "          PAR.FLAG_SCHEDARIO, " +
					 "          SUA.ID_STORICO_UNITA_ARBOREA, " +
					 "          SUA.ID_UNITA_ARBOREA, " +
					 "          SUA.ID_CATALOGO_MATRICE, " +
					 "          SUA.ID_PARTICELLA, " +
					 "          SUA.PROGR_UNAR, " +
					 "          SUA.DATA_INIZIO_VALIDITA, " +
					 "          SUA.DATA_LAVORAZIONE, " +
					 "          SUA.ID_TIPOLOGIA_UNAR, " +
					 "          TTU.DESCRIZIONE, " +
					 "          SUA.AREA, " +
					 "          SUA.DATA_IMPIANTO, " +
					 "          SUA.DATA_PRIMA_PRODUZIONE, " +
					 "          SUA.ANNO_REINNESTO, " +
					 "          SUA.ID_IRRIGAZIONE_UNAR, " +
					 "          SUA.ID_COLTIVAZIONE_UNAR, " +
					 "          SUA.CODICE_TIPO_VARIETA, " +
					 "          SUA.NUMERO_PIANTE_PRODUTTIVO, " +
					 "          SUA.NUMERO_ALTRE_PIANTE, " +
					 "          SUA.CAMPAGNA, " +
					 "          SUA.ID_TIPOLOGIA_VIGNETO, " +
					 "          SUA.TIPO_IMPIANTO, " +
					 "          SUA.NUMERO_CASTAGNI, " +
					 "          SUA.GRUPPO, " +
					 "          SUA.RICADUTA, " +
					 "          SUA.ID_GIACITURA_UNAR, " +
					 "          SUA.ID_ROCCIA_UNAR, " +
					 "          SUA.ID_SCHELETRO_UNAR, " +
					 "          SUA.ID_STATO_VEGETATIVO_UNAR, " +
					 "          SUA.ID_POTATURA_UNAR, " +
					 "          SUA.ID_GIUDIZIO_UNAR, " +
					 "          SUA.SUPPLEMENTARI, " +
					 "          SUA.MECCANIZZABILE, " +
					 "          SUA.DIMENSIONE_CHIOMA, " +
					 "          SUA.ID_ETA_IMPIANTO_UNAR, " +
					 "          SUA.PROVINCIA_CCIAA, " +
					 "          SUA.MATRICOLA_CCIAA, " +
					 "          SUA.CONFERMA_PREC_ISCRIZIONE_ALBO, " +
					 "          SUA.RICHIESTA_NUOVA_ISCR_ALBO, " +
					 "          SUA.CONFERMA_RICH_NUOVA_ISCR_ALBO, " +
					 "          SUA.SUPERFICIE_DA_ISCRIVERE_ALBO, " +
					 "          SUA.ANNO_ISCRIZIONE_ALBO, " +
					 "          SUA.ID_FONTE, " +
					 "          SUA.ID_VARIAZIONE_UNAR, " +
					 "          SUA.NOTE, " +
					 "          SUA.DATA_AGGIORNAMENTO, " +
					 "          SUA.ID_UTENTE_AGGIORNAMENTO, " +
					 "          RCM.ID_VARIETA, " +
					 "          TVAR.DESCRIZIONE, " +
					 "          TVAR.CODICE_VARIETA, " +
					 "          RCM.ID_UTILIZZO, " +
					 "          TU.CODICE, " +
					 "          TU.DESCRIZIONE, " +
					 "          SUA.DATA_ESECUZIONE, " +
					 "          SUA.RECORD_MODIFICATO, " +
					 "          SUA.ESITO_CONTROLLO, " +
					 "          SUA.ID_TIPOLOGIA_VINO, " +
           "          TTV.DESCRIZIONE, " +
					 "          AA.CUAA, " +
					 "          AA.DENOMINAZIONE, " +
					 "          PART.ID_PARTICELLA_CERTIFICATA, "+
					 "          PART.SUP_GRAFICA, " +
           "          PART.SUP_USO_GRAFICA, " +
           /*"          PAG.ID_ILO, " +
           "          PAG.SUP_PARCELLA, " +    
           "          PAG.TOLLERANZA_PARCELLA, " +
           "          PAG.ID_ISOLA_PARCELLA, " +*/
           "          ISR.DATA_RICHIESTA, " +
           "          ISR.DATA_EVASIONE, " +
           "          CEL.PERCENTUALE_UTILIZZO " +
    			 "ORDER BY  C.DESCOM, " +
    	     "          SP.SEZIONE, " +
    	     "          SP.FOGLIO, " +
    	     "          SP.PARTICELLA, " +
    	     "          SP.SUBALTERNO, " +
    	     "          TTU.DESCRIZIONE ASC, " +
    	     "          PROGR_UNAR ASC " ;
      
			
			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in searchStoricoUnitaArboreaExcelByParameters method in StoricoUnitaArboreaDAO: "+idAzienda+"\n");
			
			stmt = conn.prepareStatement(query);
			
			int indice = 0;
			
			//Parcelle
			//stmt.setLong(++indice, idAzienda.longValue());	
			
			//Particelle
			stmt.setLong(++indice, idAzienda.longValue());
			
		  //Istanza di riesame
      stmt.setLong(++indice, idAzienda.longValue());
      
      //Conduzione eleggibile
      stmt.setLong(++indice, idAzienda.longValue());
			
			//Tolleranza
			//stmt.setLong(++indice, idAzienda.longValue());
			
		  //Istanza di riesame
      //stmt.setLong(++indice, idAzienda.longValue());
      
      stmt.setLong(++indice, idAzienda.longValue());
			
			
		  //se l'utente ha selezionato il tipo di controllo
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdControllo()))
      {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdControllo().longValue());
      }
			// Se l'utente ha indicato la destinazione produttiva
			if(filtriUnitaArboreaRicercaVO.getIdUtilizzo() != null) {
				stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdUtilizzo().longValue());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_UTILIZZO] in searchStoricoUnitaArboreaExcelByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIdUtilizzo()+"\n");
			}
			// Se l'utente ha indicato il vitigno
			if(filtriUnitaArboreaRicercaVO.getIdVarieta() != null) {
				stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdVarieta().longValue());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_VARIETA] in searchStoricoUnitaArboreaExcelByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIdVarieta()+"\n");
			}
			// Se l'utente ha indicato il tipo vino
			if(filtriUnitaArboreaRicercaVO.getIdTipologiaVino() != null &&
    	    	 filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue() != new Long(-1) &&
    	    	 filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue() != new Long(0)) 
			{
		    stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue());
		    SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_TIPOLOGIA_VINO] in searchStoricoUnitaArboreaExcelByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIdTipologiaVino()+"\n");
    	}
		  // Se l'utente ha indicato il genere iscrizione
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdGenereIscrizione())) {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdGenereIscrizione().longValue());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_GENERE_ISCRIZIONE] in searchStoricoUnitaArboreaExcelByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIdGenereIscrizione()+"\n");
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
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ID_CAUSALE_MODIFICA] in searchStoricoUnitaArboreaExcelByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIdCausaleModifica()+"\n");
      }
      // Se l'utente ha indicato la provincia di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIstatProvincia())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getIstatProvincia());
        SolmrLogger.debug(this, "Value of parameter "+indice+"[ISTAT_PROVINCIA] in searchStoricoUnitaArboreaExcelByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIstatProvincia()+"\n");
      }
			// Se l'utente ha indicato il comune di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIstatComune())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getIstatComune());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[ISTAT_COMUNE] in searchStoricoUnitaArboreaExcelByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getIstatComune()+"\n");
			}
			// Se l'utente ha indicato la sezione di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSezione())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getSezione().toUpperCase());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[SEZIONE] in searchStoricoUnitaArboreaExcelByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getSezione().toUpperCase()+"\n");
			}
			// Se l'utente ha indicato il foglio di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getFoglio())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getFoglio());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[FOGLIO] in searchStoricoUnitaArboreaExcelByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getFoglio()+"\n");
			}
			// Se l'utente ha indicato la particella di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getParticella())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getParticella());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[PARTICELLA] in searchStoricoUnitaArboreaExcelByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getParticella()+"\n");
			}
			// Se l'utente ha indicato il subalterno di riferimento
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSubalterno())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getSubalterno());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[SUBALTERNO] in searchStoricoUnitaArboreaExcelByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getSubalterno()+"\n");
			}
			// SEGNALAZIONI:
			// Se l'utente ha specificato la tipologia di anomalia bloccante
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneBloccante())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getTipoSegnalazioneBloccante());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[TIPO_SEGNALAZIONE_BLOCCANTE] in searchStoricoUnitaArboreaExcelByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getTipoSegnalazioneBloccante()+"\n");
			}
			// Se l'utente ha specificato la tipologia di anomalia warning
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneWarning())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getTipoSegnalazioneWarning());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[TIPO_SEGNALAZIONE_WARNING] in searchStoricoUnitaArboreaExcelByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getTipoSegnalazioneWarning()+"\n");
			}
			// Se l'utente ha specificato la tipologia di anomalia OK
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneOk())) {
				stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getTipoSegnalazioneOk());
				SolmrLogger.debug(this, "Value of parameter "+indice+"[TIPO_SEGNALAZIONE_OK] in searchStoricoUnitaArboreaExcelByParameters method in StoricoUnitaArboreaDAO: "+filtriUnitaArboreaRicercaVO.getTipoSegnalazioneOk()+"\n");					
			}
			

			SolmrLogger.debug(this, "Executing searchStoricoUnitaArboreaExcelByParameters: "+query+"\n");
			
			ResultSet rs = stmt.executeQuery();

			while(rs.next()) 
			{
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
				
				storicoParticellaArboreaExcelVO.setIdCatalogoMatrice(checkLongNull(rs.getString("ID_CATALOGO_MATRICE")));
				
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
				//storicoParticellaArboreaExcelVO.setIdIlo(rs.getString("ID_ILO"));				
				/*String supParcella = rs.getString("SUP_PARCELLA");
        if(Validator.isNotEmpty(supParcella)) 
        {
          storicoParticellaArboreaExcelVO.setSupParcella(
            StringUtils.parseSuperficieField(supParcella));    
        }*/
        
				/*Integer codTolleranza = checkInt(rs.getString("TOLLERANZA"));
				storicoParticellaArboreaExcelVO.setCodTolleranza(codTolleranza);
				String tolleranzaExcel = null;
				if(Validator.isNotEmpty(codTolleranza))
        {
  				if(codTolleranza.compareTo(SolmrConstants.IN_TOLLERANZA) == 0) 
          {     
  				  tolleranzaExcel = SolmrConstants.UV_TOLLERANZA_OK;
          }
          else if(codTolleranza.compareTo(SolmrConstants.FUORI_TOLLERANZA) == 0) 
          {
            tolleranzaExcel = SolmrConstants.UV_TOLLERANZA_FUORI_KO;              
          }
          else if(codTolleranza.compareTo(SolmrConstants.UVDOPPIE_TOLLERANZA) == 0) 
          {
            tolleranzaExcel = SolmrConstants.UV_TOLLERANZA_UVDOPPIE_KO;              
          }
          else if(codTolleranza.compareTo(SolmrConstants.NO_PARCELLE_TOLLERANZA) == 0) 
          {
            tolleranzaExcel = SolmrConstants.ISO_TOLLERANZA_NO_PARCELLE_KO;              
          }
          else if(codTolleranza.compareTo(SolmrConstants.ERR_PL_TOLLERANZA) == 0)
          {
            tolleranzaExcel = SolmrConstants.ISO_TOLLERANZA_PLSQL_KO;              
          }
          else if(codTolleranza.compareTo(SolmrConstants.UV_NON_PRESENTE) == 0)
          {
            tolleranzaExcel = SolmrConstants.UV_NON_PRESENTE_KO;              
          }
          else if(codTolleranza.compareTo(SolmrConstants.PARTICELLA_ORFANA) == 0)
          {
            tolleranzaExcel = SolmrConstants.UV_TOLLERANZA_PARTICELLA_ORFANA_KO;              
          }
          else if(codTolleranza.compareTo(SolmrConstants.UV_PIU_OCCORR_ATTIVE) == 0)
          {
            tolleranzaExcel = SolmrConstants.UV_TOLLERANZA_PIU_OCCORR_ATTIVE_KO;              
          }
          else
          {
            tolleranzaExcel = SolmrConstants.UV_TOLLERANZA_PARCELLE_NO_VITE_KO;              
          }
        }*/
				
				
				//storicoParticellaArboreaExcelVO.setTolleranza(tolleranzaExcel);
				storicoParticellaArboreaExcelVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
				
				
				/*if((rs.getString("TOLLERANZA_PARCELLA") != null)
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
          
        }*/
				
				
				//storicoParticellaArboreaExcelVO.setAssociataParcella(rs.getString("ASSOCIATA_PARCELLA"));
				//Se questo campo è vuoto visualizzare a S  quelle che non sono presenti 
				//su  db_unar_parcella (ovvero quelle per cui la relazione tra unar e parcella è 1:1) 
				//e che l'esito della funzione di tolleranza è uno tra i seguenti valori (0, 1)
				/*if(Validator.isEmpty(storicoParticellaArboreaExcelVO.getAssociataParcella()))
				{
				  if(Validator.isNotEmpty(codTolleranza))
	        {
	          if((codTolleranza.compareTo(SolmrConstants.IN_TOLLERANZA) == 0)
	              || (codTolleranza.compareTo(SolmrConstants.FUORI_TOLLERANZA) == 0))
	          {     
	            storicoParticellaArboreaExcelVO.setAssociataParcella("S");
	          }
	        }
				}*/
				
				
			  storicoParticellaArboreaExcelVO.setDataRichiesta(rs.getTimestamp("DATA_RICHIESTA"));
	      storicoParticellaArboreaExcelVO.setDataEvasione(rs.getTimestamp("DATA_EVASIONE"));
	      
	      storicoParticellaArboreaExcelVO.setPercentualeUsoElegg(rs.getBigDecimal("PERCENTUALE_UTILIZZO"));
	      
	      
	      //usati non in visualizzazione
	      storicoParticellaArboreaExcelVO.setIdParticellaCertificata(checkLongNull(rs.getString("ID_PARTICELLA_CERTIFICATA")));
	      storicoParticellaArboreaExcelVO.setIdVarieta(checkLongNull(rs.getString("ID_VARIETA")));
	      
				
				
				anagAziendaVO.setCUAA(rs.getString("CUAA"));
				anagAziendaVO.setDenominazione(rs.getString("DENOMINAZIONE"));
				storicoParticellaArboreaExcelVO.setAnagAziendaVO(anagAziendaVO);
				elencoParticelleArboree.add(storicoParticellaArboreaExcelVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "searchStoricoUnitaArboreaExcelByParameters in StoricoUnitaArboreaDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "searchStoricoUnitaArboreaExcelByParameters in StoricoUnitaArboreaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "searchStoricoUnitaArboreaExcelByParameters in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "searchStoricoUnitaArboreaExcelByParameters in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated searchStoricoUnitaArboreaExcelByParameters method in StoricoUnitaArboreaDAO\n");
		return elencoParticelleArboree;
	}
	
	/**
	 * Metodo che mi consente di estrarre le unità vitate in funzione della chiave
	 * logica della particella alla quale sono legate
	 * 
	 * @param istatComune
	 * @param sezione
	 * @param foglio
	 * @param particella
	 * @param subalterno
	 * @param onlyActive
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.StoricoParticellaVO[]
	 * @throws DataAccessException
	 * @throws SolmrException
	 */
	public StoricoParticellaVO[] getListStoricoParticelleArboreeByParameters(String istatComune, String sezione, String foglio, String particella, String subalterno, boolean onlyActive, String[] orderBy) throws DataAccessException, SolmrException {
		SolmrLogger.debug(this, "Invocating getListStoricoParticelleArboreeByParameters method in StoricoUnitaArboreaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<StoricoParticellaVO> elencoParticelleArboree = new Vector<StoricoParticellaVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListStoricoParticelleArboreeByParameters method in StoricoUnitaArboreaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListStoricoParticelleArboreeByParameters method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");

			String query = " SELECT SUA.ID_STORICO_UNITA_ARBOREA, " +
						   "        AA.CUAA, " +
						   "        AA.DENOMINAZIONE, " + 
						   "        (SELECT I.CODICE_FISCALE " + 
						   "         FROM   DB_INTERMEDIARIO I, " + 
						   "                DB_DELEGA D " + 
						   "         WHERE  I.ID_INTERMEDIARIO = D.ID_INTERMEDIARIO " + 
						   "         AND    D.ID_AZIENDA(+) = SUA.ID_AZIENDA " + 
						   "         AND    D.DATA_FINE IS NULL) CAA, " +
						   "        SP.ID_STORICO_PARTICELLA, " +  
						   "        C.DESCOM, " +  
						   "        P.SIGLA_PROVINCIA, " +   
						   "        SP.SEZIONE, " +   
						   "        SP.FOGLIO, " +   
						   "        SP.PARTICELLA, " +   
						   "        SP.SUBALTERNO, " +   
						   "        SP.SUP_CATASTALE, " +
						   "        SP.SUPERFICIE_GRAFICA, " +
						   "        SUA.PROGR_UNAR, " +
						   "        SUA.ID_UTILIZZO, " +  
						   "        TU.CODICE, " +  
						   "        TU.DESCRIZIONE DEST_PROD, " +  
						   "        SUA.ID_VARIETA, " +  
						   "        TV.CODICE_VARIETA, " +  
						   "        TV.DESCRIZIONE VITIGNO, " +   
						   "        SUA.AREA, " +  
						   "        SUA.ANNO_IMPIANTO, " +
						   "        SUA.DATA_IMPIANTO, " +
						   "        SUA.ANNO_PRIMA_PRODUZIONE, " +
               "        SUA.DATA_PRIMA_PRODUZIONE, " +
						   "        SUA.SESTO_SU_FILA, " +   
						   "        SUA.SESTO_TRA_FILE, " +   
						   "        SUA.NUM_CEPPI, " +   
						   "        SUA.ID_FORMA_ALLEVAMENTO, " +  
						   "        TFA.DESCRIZIONE AS DESC_ALLEVAMENTO, " +  
						   "        SUA.PERCENTUALE_VARIETA, " +   
						   "        SUA.PRESENZA_ALTRI_VITIGNI, " +   
						   "        SUA.DATA_CESSAZIONE " +  
						   " FROM   DB_STORICO_UNITA_ARBOREA SUA, " +   
						   "        DB_STORICO_PARTICELLA SP, " +  
						   "        DB_ANAGRAFICA_AZIENDA AA, " +
						   "        COMUNE C, " +   
						   "        PROVINCIA P, " +   
						   "        DB_TIPO_UTILIZZO TU, " +   
						   "        DB_TIPO_VARIETA TV, " +   
						   "        DB_TIPO_FORMA_ALLEVAMENTO TFA " +   
						   " WHERE  SUA.ID_PARTICELLA = SP.ID_PARTICELLA " + 
						   " AND    SUA.ID_AZIENDA = AA.ID_AZIENDA(+) " +
						   " AND    NVL(SUA.ID_UTILIZZO,0) = TU.ID_UTILIZZO(+) " +  
						   " AND    NVL(SUA.ID_VARIETA,0) = TV.ID_VARIETA(+) " +  
						   " AND    NVL(SUA.ID_AZIENDA,0) = AA.ID_AZIENDA(+) " +    
						   " AND    SP.COMUNE = C.ISTAT_COMUNE " +  
						   " AND    NVL(SUA.ID_FORMA_ALLEVAMENTO,0) = TFA.ID_FORMA_ALLEVAMENTO(+) " +  
						   " AND    P.ISTAT_PROVINCIA = C.ISTAT_PROVINCIA ";
			if(Validator.isNotEmpty(istatComune)) {
				query +=   " AND    SP.COMUNE = ? ";
			}
			if(Validator.isNotEmpty(sezione)) {
				query +=   " AND    UPPER(SP.SEZIONE) = ? ";
			}
			if(Validator.isNotEmpty(foglio)) {
				query +=    " AND    SP.FOGLIO = ? ";
			}
			if(Validator.isNotEmpty(particella)) {
				query +=    " AND    SP.PARTICELLA = ? ";
			}
			if(Validator.isNotEmpty(subalterno)) {
				query +=    " AND    UPPER(SP.SUBALTERNO) = ? ";
			}
			if(onlyActive) {
				query +=    " AND    SP.DATA_FINE_VALIDITA IS NULL " +
							" AND    SUA.DATA_FINE_VALIDITA IS NULL " +
							" AND    AA.DATA_FINE_VALIDITA IS NULL ";
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

			SolmrLogger.debug(this, "Value of parameter 1 [ISTAT_COMUNE] in getListStoricoParticelleArboreeByParameters method in StoricoUnitaArboreaDAO: "+istatComune+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [SEZIONE] in getListStoricoParticelleArboreeByParameters method in StoricoUnitaArboreaDAO: "+sezione+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [FOGLIO] in getListStoricoParticelleArboreeByParameters method in StoricoUnitaArboreaDAO: "+foglio+"\n");
			SolmrLogger.debug(this, "Value of parameter 4 [PARTICELLA] in getListStoricoParticelleArboreeByParameters method in StoricoUnitaArboreaDAO: "+particella+"\n");
			SolmrLogger.debug(this, "Value of parameter 5 [SUBALTERNO] in getListStoricoParticelleArboreeByParameters method in StoricoUnitaArboreaDAO: "+subalterno+"\n");
			SolmrLogger.debug(this, "Value of parameter 7 [ONLY_ACTIVE] in getListStoricoParticelleArboreeByParameters method in StoricoUnitaArboreaDAO: "+onlyActive+"\n");

			stmt = conn.prepareStatement(query);
			
			// Setto il timeout possibile della query
			stmt.setQueryTimeout(SolmrConstants.MEDIUM_TIME_WAIT);
			
			int indice = 1;
			if(Validator.isNotEmpty(istatComune)) {
				stmt.setString(indice, istatComune);
				indice++;
			}
			if(Validator.isNotEmpty(sezione)) {
				stmt.setString(indice, sezione.toUpperCase());
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
			if(Validator.isNotEmpty(subalterno)) {
				stmt.setString(indice, subalterno.toUpperCase());
			}

			SolmrLogger.debug(this, "Executing getListStoricoParticelleArboreeByParameters: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) 
			{
				StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
				StoricoUnitaArboreaVO storicoUnitaArboreaVO = new StoricoUnitaArboreaVO();
				ComuneVO comuneVO = new ComuneVO();
				storicoUnitaArboreaVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
				storicoUnitaArboreaVO.setCuaa(rs.getString("CUAA"));
				storicoUnitaArboreaVO.setDenominazione(rs.getString("DENOMINAZIONE"));
				storicoUnitaArboreaVO.setCodFiscaleIntermediario(rs.getString("CAA"));
				storicoParticellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
				comuneVO.setDescom(rs.getString("DESCOM"));
				comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
				storicoParticellaVO.setComuneParticellaVO(comuneVO);
				storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
				storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
				storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
				storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
				storicoParticellaVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
				storicoParticellaVO.setSuperficieGrafica(rs.getString("SUPERFICIE_GRAFICA"));
				storicoUnitaArboreaVO.setProgrUnar(rs.getString("PROGR_UNAR"));
				if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO"))) {
					storicoUnitaArboreaVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
					TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
					tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
					tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
					tipoUtilizzoVO.setDescrizione(rs.getString("DEST_PROD"));
					storicoUnitaArboreaVO.setTipoUtilizzoVO(tipoUtilizzoVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) {
					storicoUnitaArboreaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
					TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
					tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
					tipoVarietaVO.setDescrizione(rs.getString("VITIGNO"));
					tipoVarietaVO.setCodiceVarieta(rs.getString("CODICE_VARIETA"));
					storicoUnitaArboreaVO.setTipoVarietaVO(tipoVarietaVO);
				}
				storicoUnitaArboreaVO.setArea(rs.getString("AREA"));
				storicoUnitaArboreaVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
				storicoUnitaArboreaVO.setDataImpianto(rs.getDate("DATA_IMPIANTO"));
				storicoUnitaArboreaVO.setAnnoPrimaProduzione(rs.getString("ANNO_PRIMA_PRODUZIONE"));
        storicoUnitaArboreaVO.setDataPrimaProduzione(rs.getDate("DATA_PRIMA_PRODUZIONE"));
				storicoUnitaArboreaVO.setSestoSuFila(rs.getString("SESTO_SU_FILA"));
				storicoUnitaArboreaVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
				storicoUnitaArboreaVO.setNumCeppi(rs.getString("NUM_CEPPI"));
				if(Validator.isNotEmpty(rs.getString("ID_FORMA_ALLEVAMENTO"))) {
					storicoUnitaArboreaVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
					TipoFormaAllevamentoVO tipoFormaAllevamentoVO = new TipoFormaAllevamentoVO();
					tipoFormaAllevamentoVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
					tipoFormaAllevamentoVO.setDescrizione(rs.getString("DESC_ALLEVAMENTO"));
					storicoUnitaArboreaVO.setTipoFormaAllevamentoVO(tipoFormaAllevamentoVO);
				}
				storicoUnitaArboreaVO.setPercentualeVarieta(rs.getString("PERCENTUALE_VARIETA"));
				storicoUnitaArboreaVO.setPresenzaAltriVitigni(rs.getString("PRESENZA_ALTRI_VITIGNI"));
				storicoUnitaArboreaVO.setDataCessazione(rs.getDate("DATA_CESSAZIONE"));
				storicoParticellaVO.setStoricoUnitaArboreaVO(storicoUnitaArboreaVO);
				elencoParticelleArboree.add(storicoParticellaVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListStoricoParticelleArboreeByParameters in StoricoParticellaDAO - SQLException: "+exc.getMessage()+"\n");
			if(exc.getErrorCode() == SolmrConstants.ORACLE_PREPARE_STATEMENT_TIME_OUT) {
				throw new SolmrException(AnagErrors.ERRORE_FILTRI_GENERICI);
			}
			else {
				throw new DataAccessException(exc.getMessage());
			}
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListStoricoParticelleArboreeByParameters in StoricoParticellaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListStoricoParticelleArboreeByParameters in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListStoricoParticelleArboreeByParameters in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListStoricoParticelleArboreeByParameters method in StoricoUnitaArboreaDAO\n");
		if(elencoParticelleArboree.size() == 0) {
			return (StoricoParticellaVO[])elencoParticelleArboree.toArray(new StoricoParticellaVO[0]);
		}
		else {
			return (StoricoParticellaVO[])elencoParticelleArboree.toArray(new StoricoParticellaVO[elencoParticelleArboree.size()]);
		}
	}
	
	/**
	 * Metodo che richiama una procedura PL-SQL che ribalta le UV sul piano colturale
	 * allineandolo
	 * 
	 * @param idAzienda
	 * @param idUtente
	 * @throws SolmrException
	 * @throws DataAccessException
	 */
	public void ribaltaUVOnPianoColturale(Long idAzienda, BigDecimal[] idStoricoUnitaArborea, Long idUtente) 
	  throws SolmrException, DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating ribaltaUVOnPianoColturale method in StoricoUnitaArboreaDAO\n");
		
    Connection conn = null;
    CallableStatement cs = null;
    String command = "{call PACK_RIBALTA_UV_SU_PCOLTURALE.RIBALTA_UV(?,?,?,?,?)}";
    try {
    	SolmrLogger.debug(this, "Creating db-connection in ribaltaUVOnPianoColturale method in StoricoUnitaArboreaDAO\n");
		conn = getDatasource().getConnection();
		SolmrLogger.debug(this, "Created db-connection in ribaltaUVOnPianoColturale method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");
		
		SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in ribaltaUVOnPianoColturale method in StoricoUnitaArboreaDAO: "+idAzienda+"\n");
		SolmrLogger.debug(this, "Value of parameter 2 [ID_UTENTE] in ribaltaUVOnPianoColturale method in StoricoUnitaArboreaDAO: "+idUtente+"\n");
		
		cs = conn.prepareCall(command);		
		
		/*java.sql.Connection connessione = ((weblogic.jdbc.extensions.WLConnection)conn).getVendorConnection();
	    oracle.sql.ArrayDescriptor arrayDescriptor = oracle.sql.ArrayDescriptor.createDescriptor("NUM_VARRAY", connessione);
	    oracle.sql.ARRAY array = new ARRAY(arrayDescriptor, connessione, idStoricoUnitaArborea);*/
		
		// JBOSS
		org.jboss.jca.adapters.jdbc.WrappedConnection vendorConnection = (org.jboss.jca.adapters.jdbc.WrappedConnection) conn;
		oracle.sql.ArrayDescriptor arrayDescriptor = oracle.sql.ArrayDescriptor.createDescriptor("NUM_VARRAY", vendorConnection.getUnderlyingConnection());
		oracle.sql.ARRAY array = new oracle.sql.ARRAY(arrayDescriptor, vendorConnection.getUnderlyingConnection(),idStoricoUnitaArborea);
    
    
		cs.setLong(1, idAzienda.longValue());
		if(Validator.isNotEmpty(idStoricoUnitaArborea))
		{
		  cs.setObject(2, array);
		}
		else
		{
		  cs.setNull(2, Types.ARRAY, "NUM_VARRAY"); 
		}
		cs.setLong(3, idUtente.longValue());
		cs.registerOutParameter(4,Types.VARCHAR);
		cs.registerOutParameter(5,Types.VARCHAR);
		
		cs.executeUpdate();
		String msgErr = cs.getString(4);
		String errorCode = cs.getString(5);

		if(Validator.isNotEmpty(errorCode)) {
			throw new SolmrException((String)AnagErrors.get("ERR_PLSQL")+errorCode + " - " + msgErr);
		}
		cs.close();
		conn.close();
    }
    catch(SolmrException se) {
    	SolmrLogger.error(this, "SolmrException in ribaltaUVOnPianoColturale in StoricoUnitaArboreaDAO: "+se);
    	throw new SolmrException(se.getMessage());
    }
    catch(SQLException exc) {
    	char a = '"';
    	String messaggioErrore = StringUtils.replace(exc.getMessage(), "'", "''");
    	messaggioErrore = StringUtils.replace(exc.getMessage(), System.getProperty("line.separator"), "\\n");
    	messaggioErrore = StringUtils.replace(exc.getMessage(), String.valueOf(a), " ");
    	SolmrLogger.error(this, "SQLException in ribaltaUVOnPianoColturale in StoricoUnitaArboreaDAO: "+messaggioErrore);
    	throw new SolmrException((String)AnagErrors.get("ERR_PLSQL")+" "+ StringUtils.replace(messaggioErrore, System.getProperty("line.separator"), "\\n"));
    }
    catch(Exception ex) {
    	SolmrLogger.error(this, "Generic Exception in ribaltaUVOnPianoColturale in StoricoUnitaArboreaDAO: "+ex);
    	throw new DataAccessException(ex.getMessage());
    }
    catch(Throwable ex) {
    	SolmrLogger.error(this, "Throwable in ribaltaUVOnPianoColturale in StoricoUnitaArboreaDAO: "+ex);
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
    		SolmrLogger.error(this, "SQLException while closing Statement and Connection in ribaltaUVOnPianoColturale in StoricoUnitaArboreaDAO: "+exc);
    		throw new DataAccessException(exc.getMessage());
    	}
    	catch(Exception ex) {
    		SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in ribaltaUVOnPianoColturale in StoricoUnitaArboreaDAO: "+ex);
    		throw new DataAccessException(ex.getMessage());
    	}
    }
    SolmrLogger.debug(this, "Invocated ribaltaUVOnPianoColturale method in StoricoUnitaArboreaDAO\n");
	}
	
	/**
	 * Metodo che si occupa di effettuare la cessazione di un insieme di unità arboree(simile ad un batch).Gestisco il tutto in modo che, in caso di
	 * errore, venga fatto rollback su tutti i records interessati dalla modifica
	 * 
	 * @param elencoUnitaArboree
	 * @throws DataAccessException
	 */
	public void cessaAllStoricoUnitaArborea(Long[] elencoIdStoricoUnitaArboree, Date dataFineValidita) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating cessaAllStoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in cessaAllStoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in cessaAllStoricoUnitaArborea method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");
			
			String query = " UPDATE DB_STORICO_UNITA_ARBOREA " +
                   		   " SET    DATA_FINE_VALIDITA = ? " +
                   		   " WHERE  ID_STORICO_UNITA_ARBOREA = ? ";

			conn = getDatasource().getConnection();

			stmt = conn.prepareStatement(query);

			for(int i = 0; i < elencoIdStoricoUnitaArboree.length; i++) 
			{
        stmt.setTimestamp(1, convertDateToTimestamp(dataFineValidita));
				stmt.setLong(2, elencoIdStoricoUnitaArboree[i].longValue());				
				stmt.addBatch();
			}

			SolmrLogger.debug(this, "Executing cessaAllStoricoUnitaArborea: "+query+"\n");

			int[] risultati = stmt.executeBatch();
			for(int i = 0; i < risultati.length; i++) {
				int risultato = risultati[i];
				if(risultato < 0 && risultato != -2) {
					throw new DataAccessException();
				}
			}
		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "cessaAllStoricoUnitaArborea in StoricoUnitaArboreaDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "cessaAllStoricoUnitaArborea in StoricoUnitaArboreaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "cessaAllStoricoUnitaArborea in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "cessaAllStoricoUnitaArborea in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated cessaAllStoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
	}
	
	/**
	 * Metodo che si occupa di effettuare la storicizzazione di un insieme di unità arboree(simile ad un batch).Gestisco il tutto in modo che, in caso di
	 * errore, venga fatto rollback su tutti i records interessati dalla modifica
	 * 
	 * @param elencoUnitaArboree
	 * @throws DataAccessException
	 */
	public void storicizzaAllStoricoUnitaArborea(StoricoUnitaArboreaVO[] elencoUnitaArboree) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating storicizzaAllStoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in storicizzaAllStoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in storicizzaAllStoricoUnitaArborea method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");
			
			String query = " UPDATE DB_STORICO_UNITA_ARBOREA " +
                   		   " SET    DATA_FINE_VALIDITA = ? " +
                   		   " WHERE  ID_STORICO_UNITA_ARBOREA = ? ";

			conn = getDatasource().getConnection();

			stmt = conn.prepareStatement(query);

			for(int i = 0; i < elencoUnitaArboree.length; i++) {
				StoricoUnitaArboreaVO storicoUnitaArboreaVO = (StoricoUnitaArboreaVO)elencoUnitaArboree[i];
				SolmrLogger.debug(this, "Value of parameter 1 [DATA_FINE_VALIDITA] in storicizzaAllStoricoUnitaArborea method in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getDataFineValidita()+"\n");
				stmt.setTimestamp(1, convertDateToTimestamp(storicoUnitaArboreaVO.getDataFineValidita()));
				SolmrLogger.debug(this, "Value of parameter 2 [ID_STORICO_UNITA_ARBOREA] in storicizzaAllStoricoUnitaArborea method in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdStoricoUnitaArborea()+"\n");
				stmt.setLong(2, storicoUnitaArboreaVO.getIdStoricoUnitaArborea().longValue());				
				stmt.addBatch();
			}

			SolmrLogger.debug(this, "Executing storicizzaAllStoricoUnitaArborea: "+query+"\n");

			int[] risultati = stmt.executeBatch();
			for(int i = 0; i < risultati.length; i++) {
				int risultato = risultati[i];
				if(risultato < 0 && risultato != -2) {
					throw new DataAccessException();
				}
			}
			
			stmt.close();
		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "storicizzaAllStoricoUnitaArborea in StoricoUnitaArboreaDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "storicizzaAllStoricoUnitaArborea in StoricoUnitaArboreaDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "storicizzaAllStoricoUnitaArborea in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "storicizzaAllStoricoUnitaArborea in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated storicizzaAllStoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
	}
	
	/**
	 * Metodo che si occupa di effettuare l'inserimento di un insieme di unità arboree(simile ad un batch).Gestisco il tutto in modo che, in caso di
	 * errore, venga fatto rollback su tutti i records interessati dalla modifica
	 * 
	 * @param elencoUnitaArboree
	 * @throws DataAccessException
	 */
	public void insertAllStoricoUnitaArborea(StoricoUnitaArboreaVO[] elencoUnitaArboree) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating insertAllStoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    Long[] idStoricoUnitaArborea = new Long[elencoUnitaArboree.length];
	
	    try {
	    	for(int i = 0; i < elencoUnitaArboree.length; i++) {
	    		idStoricoUnitaArborea[i] = getNextPrimaryKey(SolmrConstants.SEQ_STORICO_UNITA_ARBOREA);
	    	}
	    	SolmrLogger.debug(this, "Creating db-connection in insertAllStoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in insertAllStoricoUnitaArborea method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");
			
			String query = " INSERT INTO DB_STORICO_UNITA_ARBOREA " +
			   			   "        (ID_STORICO_UNITA_ARBOREA, " +
			   			   "         ID_UNITA_ARBOREA, " +
			   			   "         ID_PARTICELLA, " +
			   			   "         PROGR_UNAR, " +
			   			   "         DATA_INIZIO_VALIDITA, " +
			   			   "         DATA_FINE_VALIDITA, " +
			   			   "         DATA_LAVORAZIONE, " +
			   			   "         ID_TIPOLOGIA_UNAR, " +
			   			   "         AREA, " +
			   			   "         SESTO_SU_FILA, " +
			   			   "         SESTO_TRA_FILE, " +
			   			   "         NUM_CEPPI, " +
			   			   "         ANNO_IMPIANTO, " +
			   			   "         DATA_IMPIANTO, " +
			   			   "         ANNO_REINNESTO, " +
			   			   "         ID_FORMA_ALLEVAMENTO, " +
			   			   "         ID_IRRIGAZIONE_UNAR, " +
			   			   "         ID_COLTIVAZIONE_UNAR, " +
			   			   "         CODICE_TIPO_VARIETA, " +
			   			   "         PRESENZA_ALTRI_VITIGNI, " +
			   			   "         NUMERO_PIANTE_PRODUTTIVO, " +
			   			   "         NUMERO_ALTRE_PIANTE, " +
			   			   "         CAMPAGNA, " +
			   			   "         ID_TIPOLOGIA_VIGNETO, " +
			   			   "         TIPO_IMPIANTO, " +
			   			   "         NUMERO_CASTAGNI, " +
			   			   "         GRUPPO, " +
			   			   "         RICADUTA, " +
			   			   "         ID_GIACITURA_UNAR, " +
			   			   "         ID_ROCCIA_UNAR, " +
			   			   "         ID_SCHELETRO_UNAR, " +
			   			   "         ID_STATO_VEGETATIVO_UNAR, " +
			   			   "         ID_POTATURA_UNAR, " +
			   			   "         ID_GIUDIZIO_UNAR, " +
			   			   "         SUPPLEMENTARI, " +
			   			   "         MECCANIZZABILE, " +
			   			   "         DIMENSIONE_CHIOMA, " +
			   			   "         ID_ETA_IMPIANTO_UNAR, " +
			   			   "         PROVINCIA_CCIAA, " +
			   			   "         MATRICOLA_CCIAA, " +
			   			   "         CONFERMA_PREC_ISCRIZIONE_ALBO, " +
			   			   "         RICHIESTA_NUOVA_ISCR_ALBO, " +
			   			   "         CONFERMA_RICH_NUOVA_ISCR_ALBO, " +
			   			   "         SUPERFICIE_DA_ISCRIVERE_ALBO, " +
			   			   "         ANNO_ISCRIZIONE_ALBO, " +
			   			   "         ID_FONTE, " +
			   			   "         ID_VARIAZIONE_UNAR, " +
			   			   "         NOTE, " +
			   			   "         DATA_AGGIORNAMENTO, " +
			   			   "         ID_UTENTE_AGGIORNAMENTO, " +
			   			   "         ID_VARIETA, " +
			   			   "         ID_UTILIZZO, " +
			   			   "         PERCENTUALE_VARIETA, " +
			   			   "         ID_VINO, " +
			   			   "         DATA_ESECUZIONE, " +
			   			   "         RECORD_MODIFICATO, " +
			   			   "         ID_AZIENDA, " +
			   			   "         DATA_CESSAZIONE, " +
			   			   "         ID_CESSAZIONE_UNAR," +
			   			   "         ID_CAUSALE_MODIFICA, " +
			   			   "         ID_TIPOLOGIA_VINO, " +
			   			   "         STATO_UNITA_ARBOREA, " +
			   			   "         ANNO_RIFERIMENTO, " +
			   			   "         COLTURA_SPECIALIZZATA, " +
			   			   "         DATA_ISCRIZIONE_ALBO, " +
			   			   "         ANNO_PRIMA_PRODUZIONE, " +
                 "         DATA_PRIMA_PRODUZIONE, " +
			   			   "         VIGNA, " +
			   			   "         ID_VIGNA, " +
			   			   "         ID_MENZIONE_GEOGRAFICA, " +
			   			   "         ETICHETTA, " +
			   			   "         ID_GENERE_ISCRIZIONE, " +
			   			   "         FLAG_IMPRODUTTIVA, " +
			   			   "         PERCENTUALE_FALLANZA, " +
			   			   "         DATA_SOVRAINNESTO, " +
                 "         DATA_INTERVENTO, " +
                 "         ID_TIPO_INTERVENTO_VITICOLO, " +
                 "         ID_FILO_SOSTEGNO, " +
                 "         ID_PALO_TESTATA, " +
                 "         ID_PALO_TESSITURA, " +
                 "         ID_ANCORAGGIO_UNAR, " +
                 "         ID_STATO_COLTIVAZIONE_UNAR, " +
                 "         DISTANZA_PALI, " +
                 "         ALTITUDINE_SLM, " +
                 "         AREA_SERVIZIO, " +
                 "         PERCENTUALE_PENDENZA_MEDIA, " +
                 "         GRADI_PENDENZA_MEDIA, " +
                 "         GRADI_ESPOSIZIONE_MEDIA," +
                 "         METRI_ALTITUDINE_MEDIA, " +
                 "         ID_UNITA_ARBOREA_MADRE, " +
                 "         ID_CATALOGO_MATRICE) " +
			   			   " VALUES  (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
			   			   "          ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
			   			   "          ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
			   			   "          ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
			stmt = conn.prepareStatement(query);
			

			SolmrLogger.debug(this, "Executing insertAllStoricoUnitaArborea: "+query);
			
			for(int i = 0; i < elencoUnitaArboree.length; i++) 
			{
			  int idx = 0;
			  
				StoricoUnitaArboreaVO storicoUnitaArboreaVO = (StoricoUnitaArboreaVO)elencoUnitaArboree[i];
				stmt.setLong(++idx, idStoricoUnitaArborea[i].longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_STORICO_UNITA_ARBOREA] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+idStoricoUnitaArborea[i].longValue()+"\n");
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdUnitaArborea().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_UNITA_ARBOREA] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdUnitaArborea().longValue()+"\n");
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdParticella().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_PARTICELLA] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdParticella().longValue()+"\n");
				stmt.setString(++idx, storicoUnitaArboreaVO.getProgrUnar());
				SolmrLogger.debug(this, "Value of parameter [PROGR_UNAR] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getProgrUnar()+"\n");
				stmt.setTimestamp(++idx, new Timestamp(storicoUnitaArboreaVO.getDataInizioValidita().getTime()));
				SolmrLogger.debug(this, "Value of parameter [DATA_FINE_VALIDITA] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new Timestamp(storicoUnitaArboreaVO.getDataInizioValidita().getTime())+"\n");
				if(storicoUnitaArboreaVO.getDataFineValidita() != null) {
					stmt.setTimestamp(++idx, new Timestamp(storicoUnitaArboreaVO.getDataFineValidita().getTime()));
					SolmrLogger.debug(this, "Value of parameter [DATA_FINE_VALIDITA] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new Timestamp(storicoUnitaArboreaVO.getDataFineValidita().getTime())+"\n");
				}
				else {
					stmt.setString(++idx, null);
					SolmrLogger.debug(this, "Value of parameter [DATA_FINE_VALIDITA] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
				}
				if(storicoUnitaArboreaVO.getDataLavorazione() != null) {
					stmt.setDate(++idx, new java.sql.Date(storicoUnitaArboreaVO.getDataLavorazione().getTime()));
					SolmrLogger.debug(this, "Value of parameter [DATA_LAVORAZIONE] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new java.sql.Date(storicoUnitaArboreaVO.getDataLavorazione().getTime())+"\n");
				}
				else {
					stmt.setString(++idx, null);
					SolmrLogger.debug(this, "Value of parameter [DATA_LAVORAZIONE] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
				}
				if(storicoUnitaArboreaVO.getIdTipologiaUnar() != null) {
					stmt.setLong(++idx, storicoUnitaArboreaVO.getIdTipologiaUnar().longValue());
					SolmrLogger.debug(this, "Value of parameter [ID_TIPOLOGIA_UNAR] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdTipologiaUnar().longValue()+"\n");
				}
				else {
					stmt.setString(++idx, null);
					SolmrLogger.debug(this, "Value of parameter [ID_TIPOLOGIA_UNAR] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
				}
				if(Validator.isNotEmpty(storicoUnitaArboreaVO.getArea())) {
					stmt.setString(++idx, StringUtils.parseSuperficieField(storicoUnitaArboreaVO.getArea()));
					SolmrLogger.debug(this, "Value of parameter [AREA] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getArea()+"\n");
				}
				else {
					stmt.setString(++idx, null);
					SolmrLogger.debug(this, "Value of parameter [AREA] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
				}
				stmt.setString(++idx, storicoUnitaArboreaVO.getSestoSuFila());
				SolmrLogger.debug(this, "Value of parameter [SESTO_SU_FILA] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getSestoSuFila()+"\n");
				stmt.setString(++idx, storicoUnitaArboreaVO.getSestoTraFile());
				SolmrLogger.debug(this, "Value of parameter [SESTO_TRA_FILE] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getSestoTraFile()+"\n");
				stmt.setString(++idx, storicoUnitaArboreaVO.getNumCeppi());
				SolmrLogger.debug(this, "Value of parameter [NUMERO_CEPPI] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getNumCeppi()+"\n");
				stmt.setString(++idx, storicoUnitaArboreaVO.getAnnoImpianto());
				SolmrLogger.debug(this, "Value of parameter [ANNO_IMPIANTO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getAnnoImpianto()+"\n");
				if(storicoUnitaArboreaVO.getDataImpianto() != null) {
          stmt.setTimestamp(++idx, new Timestamp(storicoUnitaArboreaVO.getDataImpianto().getTime()));
          SolmrLogger.debug(this, "Value of parameter [DATA_IMPIANTO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new Timestamp(storicoUnitaArboreaVO.getDataImpianto().getTime())+"\n");
        }
        else {
          stmt.setString(++idx, null);
          SolmrLogger.debug(this, "Value of parameter [DATA_IMPIANTO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
        }
				stmt.setString(++idx, storicoUnitaArboreaVO.getAnnoReinnesto());
				SolmrLogger.debug(this, "Value of parameter [ANNO_REINNESTO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getAnnoReinnesto()+"\n");
				if(storicoUnitaArboreaVO.getIdFormaAllevamento() != null) {
					stmt.setLong(++idx, storicoUnitaArboreaVO.getIdFormaAllevamento().longValue());
					SolmrLogger.debug(this, "Value of parameter [ID_FORMA_ALLEVAMENTO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdFormaAllevamento().longValue()+"\n");
				}
				else {
					stmt.setString(++idx, null);
					SolmrLogger.debug(this, "Value of parameter [ID_FORMA_ALLEVAMENTO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
				}
				if(storicoUnitaArboreaVO.getIdIrrigazioneUnar() != null) {
					stmt.setLong(++idx, storicoUnitaArboreaVO.getIdIrrigazioneUnar().longValue());
					SolmrLogger.debug(this, "Value of parameter [ID_IRRIGAZIONE_UNAR] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdIrrigazioneUnar().longValue()+"\n");
				}
				else {
					stmt.setString(++idx, null);
					SolmrLogger.debug(this, "Value of parameter [ID_IRRIGAZIONE_UNAR] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
				}
				if(storicoUnitaArboreaVO.getIdColtivazioneUnar() != null) {
					stmt.setLong(++idx, storicoUnitaArboreaVO.getIdColtivazioneUnar().longValue());
					SolmrLogger.debug(this, "Value of parameter [ID_COLTIVAZIONE_UNAR] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdColtivazioneUnar().longValue()+"\n");
				}
				else {
					stmt.setString(++idx, null);
					SolmrLogger.debug(this, "Value of parameter [ID_COLTIVAZIONE_UNAR] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
				}
				stmt.setString(++idx, storicoUnitaArboreaVO.getCodiceTipoVarieta());
				SolmrLogger.debug(this, "Value of parameter [CODICE_TIPO_VARIETA] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getCodiceTipoVarieta()+"\n");
				stmt.setString(++idx, storicoUnitaArboreaVO.getPresenzaAltriVitigni());
				SolmrLogger.debug(this, "Value of parameter [PRESENZA_ALTRI_VITIGNI] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getPresenzaAltriVitigni()+"\n");
				stmt.setString(++idx, storicoUnitaArboreaVO.getNumeroPianteProduttivo());
				SolmrLogger.debug(this, "Value of parameter [NUMERO_PIANTE_PRODUTTIVO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getNumeroPianteProduttivo()+"\n");
				stmt.setString(++idx, storicoUnitaArboreaVO.getNumeroAltrePiante());
				SolmrLogger.debug(this, "Value of parameter [NUMERO_ALTRE_PIANTE] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getNumeroAltrePiante()+"\n");
				stmt.setString(++idx, storicoUnitaArboreaVO.getCampagna());
				SolmrLogger.debug(this, "Value of parameter [CAMPAGNA] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getCampagna()+"\n");
				if(storicoUnitaArboreaVO.getIdTipologiaVigneto() != null) {
					stmt.setLong(++idx, storicoUnitaArboreaVO.getIdTipologiaVigneto().longValue());
					SolmrLogger.debug(this, "Value of parameter [ID_TIPOLOGIA_VIGNETO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdTipologiaVigneto().longValue()+"\n");
				}
				else {
					stmt.setString(++idx, null);
					SolmrLogger.debug(this, "Value of parameter [ID_TIPOLOGIA_VIGNETO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
				}
				stmt.setString(++idx, storicoUnitaArboreaVO.getTipoImpianto());
				SolmrLogger.debug(this, "Value of parameter [TIPO_IMPIANTO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getTipoImpianto()+"\n");
				stmt.setString(++idx, storicoUnitaArboreaVO.getNumeroCastagni());
				SolmrLogger.debug(this, "Value of parameter [NUMERO_CASTAGNI] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getNumeroCastagni()+"\n");
				stmt.setString(++idx, storicoUnitaArboreaVO.getGruppo());
				SolmrLogger.debug(this, "Value of parameter [GRUPPO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getGruppo()+"\n");
				stmt.setString(++idx, storicoUnitaArboreaVO.getRicaduta());
				SolmrLogger.debug(this, "Value of parameter [RICADUTA] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getRicaduta()+"\n");
				if(storicoUnitaArboreaVO.getIdGiacituraUnar() != null) {
					stmt.setLong(++idx, storicoUnitaArboreaVO.getIdGiacituraUnar().longValue());
					SolmrLogger.debug(this, "Value of parameter [ID_GIACITURA_UNAR] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdGiacituraUnar().longValue()+"\n");
				}
				else {
					stmt.setString(++idx, null);
					SolmrLogger.debug(this, "Value of parameter [ID_GIACITURA_UNAR] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
				}
				if(storicoUnitaArboreaVO.getIdRocciaUnar() != null) {
					stmt.setLong(++idx, storicoUnitaArboreaVO.getIdRocciaUnar().longValue());
					SolmrLogger.debug(this, "Value of parameter [ID_ROCCIA_UNAR] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdRocciaUnar().longValue()+"\n");
				}
				else {
					stmt.setString(++idx, null);
					SolmrLogger.debug(this, "Value of parameter [ID_ROCCIA_UNAR] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
				}
				if(storicoUnitaArboreaVO.getIdScheletroUnar() != null) {
					stmt.setLong(++idx, storicoUnitaArboreaVO.getIdScheletroUnar().longValue());
					SolmrLogger.debug(this, "Value of parameter [ID_SCHELETRO_UNAR] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdScheletroUnar().longValue()+"\n");
				}
				else {
					stmt.setString(++idx, null);
					SolmrLogger.debug(this, "Value of parameter [ID_SCHELETRO_UNAR] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
				}
				if(storicoUnitaArboreaVO.getIdStatoVegetativoUnar() != null) {
					stmt.setLong(++idx, storicoUnitaArboreaVO.getIdStatoVegetativoUnar().longValue());
					SolmrLogger.debug(this, "Value of parameter [ID_STATO_VEGETATIVO_UNAR] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdStatoVegetativoUnar().longValue()+"\n");
				}
				else {
					stmt.setString(++idx, null);
					SolmrLogger.debug(this, "Value of parameter [ID_STATO_VEGETATIVO_UNAR] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
				}
				if(storicoUnitaArboreaVO.getIdPotaturaUnar() != null) {
					stmt.setLong(++idx, storicoUnitaArboreaVO.getIdPotaturaUnar().longValue());
					SolmrLogger.debug(this, "Value of parameter [ID_POTATURA_UNAR] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdPotaturaUnar().longValue()+"\n");
				}
				else {
					stmt.setString(++idx, null);
					SolmrLogger.debug(this, "Value of parameter [ID_POTATURA_UNAR] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
				}
				if(storicoUnitaArboreaVO.getIdGiudizioUnar() != null) {
					stmt.setLong(++idx, storicoUnitaArboreaVO.getIdGiudizioUnar().longValue());
					SolmrLogger.debug(this, "Value of parameter [ID_GIUDIZIO_UNAR] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdGiudizioUnar().longValue()+"\n");
				}
				else {
					stmt.setString(++idx, null);
					SolmrLogger.debug(this, "Value of parameter [ID_GIUDIZIO_UNAR] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
				}
				stmt.setString(++idx, storicoUnitaArboreaVO.getSupplementari());
				SolmrLogger.debug(this, "Value of parameter [SUPPLEMENTARI] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getSupplementari()+"\n");
				stmt.setString(++idx, storicoUnitaArboreaVO.getMeccanizzabile());
				SolmrLogger.debug(this, "Value of parameter [MECCANIZZABILE] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getMeccanizzabile()+"\n");
				stmt.setString(++idx, storicoUnitaArboreaVO.getDimensioneChioma());
				SolmrLogger.debug(this, "Value of parameter [DIMENSIONE_CHIOMA] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getDimensioneChioma()+"\n");
				if(storicoUnitaArboreaVO.getIdEtaImpiantoUnar() != null) {
					stmt.setLong(++idx, storicoUnitaArboreaVO.getIdEtaImpiantoUnar().longValue());
					SolmrLogger.debug(this, "Value of parameter [ID_ETA_IMPIANTO_UNAR] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdEtaImpiantoUnar().longValue()+"\n");
				}
				else {
					stmt.setString(++idx, null);
					SolmrLogger.debug(this, "Value of parameter [ID_ETA_IMPIANTO_UNAR] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
				}
				if(Validator.isNotEmpty(storicoUnitaArboreaVO.getProvinciaCCIAA())) 
        {
          stmt.setString(++idx, storicoUnitaArboreaVO.getProvinciaCCIAA());
          SolmrLogger.debug(this, "Value of parameter [PROVINCIA_CCIAA] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getProvinciaCCIAA()+"\n");
        }
        else
        {
          stmt.setString(++idx, null);
          SolmrLogger.debug(this, "Value of parameter [PROVINCIA_CCIAA] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
        }
				if(Validator.isNotEmpty(storicoUnitaArboreaVO.getMatricolaCCIAA())) 
				{
					stmt.setString(++idx, storicoUnitaArboreaVO.getMatricolaCCIAA());
					SolmrLogger.debug(this, "Value of parameter [MATRICOLA_CCIAA] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getMatricolaCCIAA()+"\n");
				}
				else 
				{
					stmt.setString(++idx, null);
					SolmrLogger.debug(this, "Value of parameter [MATRICOLA_CCIAA] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
				}
				stmt.setString(++idx, storicoUnitaArboreaVO.getConfermaPrecIscrizioneAlbo());
				SolmrLogger.debug(this, "Value of parameter [CONFERMA_PREC_ISCRIZIONE_ALBO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getConfermaPrecIscrizioneAlbo()+"\n");
				stmt.setString(++idx, storicoUnitaArboreaVO.getRichiestaNuovaIscrAlbo());
				SolmrLogger.debug(this, "Value of parameter [RICHIESTA_NUOVA_ISCRIZIONE_ALBO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getRichiestaNuovaIscrAlbo()+"\n");
				stmt.setString(++idx, storicoUnitaArboreaVO.getConfermaRichNuovaIscrAlbo());
				SolmrLogger.debug(this, "Value of parameter [CONFERMA_RIC_NUOVA_ISCR_ALBO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getConfermaPrecIscrizioneAlbo()+"\n");
				if(Validator.isNotEmpty(storicoUnitaArboreaVO.getSuperficieDaIscrivereAlbo())) {
					stmt.setString(++idx, StringUtils.parseSuperficieField(storicoUnitaArboreaVO.getSuperficieDaIscrivereAlbo()));
				}
				else {
					stmt.setString(++idx, storicoUnitaArboreaVO.getSuperficieDaIscrivereAlbo());
				}
				SolmrLogger.debug(this, "Value of parameter [SUPERFICIE_DA_ISCRIVERE_ALBO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getSuperficieDaIscrivereAlbo()+"\n");
				stmt.setString(++idx, storicoUnitaArboreaVO.getAnnoIscrizioneAlbo());
				SolmrLogger.debug(this, "Value of parameter [ANNO_ISCRIZIONE_ALBO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getAnnoIscrizioneAlbo()+"\n");
				if(storicoUnitaArboreaVO.getIdFonte() != null) {
					stmt.setLong(++idx, storicoUnitaArboreaVO.getIdFonte().longValue());
					SolmrLogger.debug(this, "Value of parameter [ID_FONTE] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdFonte().longValue()+"\n");
				}
				else {
					stmt.setString(++idx, null);
					SolmrLogger.debug(this, "Value of parameter [ID_FONTE] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
				}
				if(storicoUnitaArboreaVO.getIdVariazioneUnar() != null) {
					stmt.setLong(++idx, storicoUnitaArboreaVO.getIdVariazioneUnar().longValue());
					SolmrLogger.debug(this, "Value of parameter [ID_VARIAZIONE_UNAR] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdVariazioneUnar().longValue()+"\n");
				}
				else {
					stmt.setString(++idx, null);
					SolmrLogger.debug(this, "Value of parameter [ID_VARIAZIONE_UNAR] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
				}
				stmt.setString(++idx, storicoUnitaArboreaVO.getNote());
				SolmrLogger.debug(this, "Value of parameter [NOTE] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getNote()+"\n");
				stmt.setTimestamp(++idx, new Timestamp(storicoUnitaArboreaVO.getDataAggiornamento().getTime()));
				SolmrLogger.debug(this, "Value of parameter [DATA_AGGIORNAMENTO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new Timestamp(storicoUnitaArboreaVO.getDataAggiornamento().getTime())+"\n");
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdUtenteAggiornamento().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_UTENTE_AGGIORNAMENTO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdUtenteAggiornamento().longValue()+"\n");
				if(storicoUnitaArboreaVO.getIdVarieta() != null) {
					stmt.setLong(++idx, storicoUnitaArboreaVO.getIdVarieta().longValue());
					SolmrLogger.debug(this, "Value of parameter [ID_VARIETA] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdVarieta().longValue()+"\n");
				}
				else {
					stmt.setString(++idx, null);
					SolmrLogger.debug(this, "Value of parameter [ID_VARIETA] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
				}
				if(storicoUnitaArboreaVO.getIdUtilizzo() != null) {
					stmt.setLong(++idx, storicoUnitaArboreaVO.getIdUtilizzo().longValue());
					SolmrLogger.debug(this, "Value of parameter [ID_UTILIZZO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdUtilizzo().longValue()+"\n");
				}
				else {
					stmt.setString(++idx, null);
					SolmrLogger.debug(this, "Value of parameter [ID_UTILIZZO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
				}
				stmt.setString(++idx, storicoUnitaArboreaVO.getPercentualeVarieta());
				SolmrLogger.debug(this, "Value of parameter [PERCENTUALE_VARIETA] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getPercentualeVarieta()+"\n");
				if(storicoUnitaArboreaVO.getIdVino() != null) {
					stmt.setLong(++idx, storicoUnitaArboreaVO.getIdVino().longValue());
					SolmrLogger.debug(this, "Value of parameter [ID_VINO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdVino().longValue()+"\n");
				}
				else {
					stmt.setString(++idx, null);
					SolmrLogger.debug(this, "Value of parameter [ID_VINO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
				}
				if(storicoUnitaArboreaVO.getDataEsecuzione() != null) {
					stmt.setTimestamp(++idx, new Timestamp(storicoUnitaArboreaVO.getDataEsecuzione().getTime()));
					SolmrLogger.debug(this, "Value of parameter [DATA_ESECUZIONE] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new Timestamp(storicoUnitaArboreaVO.getDataEsecuzione().getTime())+"\n");
				}
				else {
					stmt.setString(++idx, null);
					SolmrLogger.debug(this, "Value of parameter [DATA_ESECUZIONE] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
				}
				stmt.setString(++idx, storicoUnitaArboreaVO.getRecordModificato());
				SolmrLogger.debug(this, "Value of parameter [RECORD_MODIFICATO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getRecordModificato()+"\n");
				if(storicoUnitaArboreaVO.getIdAzienda() != null) {
					stmt.setLong(++idx, storicoUnitaArboreaVO.getIdAzienda().longValue());
					SolmrLogger.debug(this, "Value of parameter [ID_AZIENDA] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdAzienda().longValue()+"\n");
				}
				else {
					stmt.setString(++idx, null);
					SolmrLogger.debug(this, "Value of parameter [ID_AZIENDA] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
				}
				if(storicoUnitaArboreaVO.getDataCessazione() != null) {
					stmt.setTimestamp(++idx, new Timestamp(storicoUnitaArboreaVO.getDataCessazione().getTime()));
					SolmrLogger.debug(this, "Value of parameter [DATA_CESSAZIONE] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new Timestamp(storicoUnitaArboreaVO.getDataCessazione().getTime())+"\n");
				}
				else {
					stmt.setString(++idx, null);
					SolmrLogger.debug(this, "Value of parameter [DATA_CESSAZIONE] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
				}
				if(storicoUnitaArboreaVO.getIdCessazioneUnar() != null) {
					stmt.setLong(++idx, storicoUnitaArboreaVO.getIdCessazioneUnar().longValue());
					SolmrLogger.debug(this, "Value of parameter [ID_CESSAZIONE_UNAR] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdCessazioneUnar().longValue()+"\n");
				}
				else {
					stmt.setString(++idx, null);
					SolmrLogger.debug(this, "Value of parameter [ID_CESSAZIONE] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
				}
				stmt.setLong(++idx, storicoUnitaArboreaVO.getIdCausaleModifica().longValue());
				SolmrLogger.debug(this, "Value of parameter [ID_CAUSALE_MODIFICA] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdCausaleModifica().longValue()+"\n");
				if(storicoUnitaArboreaVO.getIdTipologiaVino() != null) {
					stmt.setLong(++idx, storicoUnitaArboreaVO.getIdTipologiaVino().longValue());
					SolmrLogger.debug(this, "Value of parameter [ID_TIPOLOGIA_VINO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdTipologiaVino().longValue()+"\n");
				}
				else {
					stmt.setString(++idx, null);
					SolmrLogger.debug(this, "Value of parameter [ID_TIPOLOGIA_VINO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
				}
				stmt.setString(++idx, storicoUnitaArboreaVO.getStatoUnitaArborea());
				SolmrLogger.debug(this, "Value of parameter STATO_UNITA_ARBOREA] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getStatoUnitaArborea()+"\n");
				stmt.setString(++idx, storicoUnitaArboreaVO.getAnnoRiferimento());
				SolmrLogger.debug(this, "Value of parameter [ANNO_RIFERIMENTO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getAnnoRiferimento()+"\n");
				stmt.setString(++idx, storicoUnitaArboreaVO.getColturaSpecializzata());
        SolmrLogger.debug(this, "Value of parameter [COLTURA_SPECIALIZZATA] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getColturaSpecializzata()+"\n");
        if(storicoUnitaArboreaVO.getDataIscrizioneAlbo() != null) {
          stmt.setTimestamp(++idx, new Timestamp(storicoUnitaArboreaVO.getDataIscrizioneAlbo().getTime()));
          SolmrLogger.debug(this, "Value of parameter [DATA_ISCRIZIONE_ALBO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new Timestamp(storicoUnitaArboreaVO.getDataIscrizioneAlbo().getTime())+"\n");
        }
        else {
          stmt.setString(++idx, null);
          SolmrLogger.debug(this, "Value of parameter [DATA_ISCRIZIONE_ALBO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
        }
        stmt.setString(++idx, storicoUnitaArboreaVO.getAnnoPrimaProduzione());
        SolmrLogger.debug(this, "Value of parameter [ANNO_PRIMA_PRODUZIONE] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getAnnoPrimaProduzione()+"\n");
        if(storicoUnitaArboreaVO.getDataPrimaProduzione() != null) {
          stmt.setTimestamp(++idx, new Timestamp(storicoUnitaArboreaVO.getDataPrimaProduzione().getTime()));
          SolmrLogger.debug(this, "Value of parameter [DATA_PRIMA_PRODUZIONE] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new Timestamp(storicoUnitaArboreaVO.getDataPrimaProduzione().getTime())+"\n");
        }
        else {
          stmt.setString(++idx, null);
          SolmrLogger.debug(this, "Value of parameter [DATA_PRIMA_PRODUZIONE] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
        }
        stmt.setString(++idx, storicoUnitaArboreaVO.getVigna());
        SolmrLogger.debug(this, "Value of parameter [VIGNA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getVigna()+"\n");
        if(storicoUnitaArboreaVO.getIdVigna() != null) {
          stmt.setLong(++idx, storicoUnitaArboreaVO.getIdVigna().longValue());
          SolmrLogger.debug(this, "Value of parameter [ID_VIGNA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdVigna()+"\n");
        }
        else {
          stmt.setString(++idx, null);
          SolmrLogger.debug(this, "Value of parameter [ID_VIGNA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
        }
        if(storicoUnitaArboreaVO.getIdMenzioneGeografica() != null) {
          stmt.setLong(++idx, storicoUnitaArboreaVO.getIdMenzioneGeografica().longValue());
          SolmrLogger.debug(this, "Value of parameter [ID_MENZIONE_GEOGRAFICA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdMenzioneGeografica()+"\n");
        }
        else {
          stmt.setString(++idx, null);
          SolmrLogger.debug(this, "Value of parameter [ID_MENZIONE_GEOGRAFICA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
        }
        stmt.setString(++idx, storicoUnitaArboreaVO.getEtichetta());
        SolmrLogger.debug(this, "Value of parameter [ETICHETTA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getEtichetta()+"\n");
        if(storicoUnitaArboreaVO.getIdGenereIscrizione() != null) {
          stmt.setLong(++idx, storicoUnitaArboreaVO.getIdGenereIscrizione().longValue());
          SolmrLogger.debug(this, "Value of parameter [ID_GENERE_ISCRIZIONE] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdGenereIscrizione()+"\n");
        }
        else {
          stmt.setString(++idx, null);
          SolmrLogger.debug(this, "Value of parameter [ID_GENERE_ISCRIZIONE] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
        }
        
        stmt.setString(++idx, storicoUnitaArboreaVO.getFlagImproduttiva());
        SolmrLogger.debug(this, "Value of parameter [FLAG_IMPRODUTTIVA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getFlagImproduttiva()+"\n");
        
        stmt.setBigDecimal(++idx, storicoUnitaArboreaVO.getPercentualeFallanza());
        SolmrLogger.debug(this, "Value of parameter [PERCENTUALE_FALLANZA] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getPercentualeFallanza()+"\n");
        
        
        if(storicoUnitaArboreaVO.getDataSovrainnesto() != null) {
          stmt.setTimestamp(++idx, new Timestamp(storicoUnitaArboreaVO.getDataSovrainnesto().getTime()));
          SolmrLogger.debug(this, "Value of parameter [DATA_SOVRAINNESTO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new Timestamp(storicoUnitaArboreaVO.getDataSovrainnesto().getTime())+"\n");
        }
        else {
          stmt.setString(++idx, null);
          SolmrLogger.debug(this, "Value of parameter [DATA_SOVRAINNESTO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
        }
        if(storicoUnitaArboreaVO.getDataIntervento() != null) {
          stmt.setTimestamp(++idx, new Timestamp(storicoUnitaArboreaVO.getDataIntervento().getTime()));
          SolmrLogger.debug(this, "Value of parameter [DATA_INTERVENTO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+new Timestamp(storicoUnitaArboreaVO.getDataIntervento().getTime())+"\n");
        }
        else {
          stmt.setString(++idx, null);
          SolmrLogger.debug(this, "Value of parameter [DATA_INTERVENTO] in method insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
        }
        if(storicoUnitaArboreaVO.getIdTipoInterventoViticolo() != null) {
          stmt.setLong(++idx, storicoUnitaArboreaVO.getIdTipoInterventoViticolo().longValue());
          SolmrLogger.debug(this, "Value of parameter [ID_TIPO_INTERVENTO_VITICOLO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdTipoInterventoViticolo()+"\n");
        }
        else {
          stmt.setString(++idx, null);
          SolmrLogger.debug(this, "Value of parameter [ID_TIPO_INTERVENTO_VITICOLO] in method insertStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
        }
        if(storicoUnitaArboreaVO.getIdFiloSostegno() != null) {
          stmt.setLong(++idx, storicoUnitaArboreaVO.getIdFiloSostegno());
          SolmrLogger.debug(this, "Value of parameter [ID_FILO_SOSTEGNO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdFiloSostegno()+"\n");
        }
        else {
          stmt.setString(++idx, null);
          SolmrLogger.debug(this, "Value of parameter [ID_FILO_SOSTEGNO] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
        }
        if(storicoUnitaArboreaVO.getIdPaloTestata() != null) {
          stmt.setLong(++idx, storicoUnitaArboreaVO.getIdPaloTestata());
          SolmrLogger.debug(this, "Value of parameter [ID_PALO_TESTATA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdPaloTestata()+"\n");
        }
        else {
          stmt.setString(++idx, null);
          SolmrLogger.debug(this, "Value of parameter [ID_PALO_TESTATA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
        }
        if(storicoUnitaArboreaVO.getIdPaloTessitura() != null) {
          stmt.setLong(++idx, storicoUnitaArboreaVO.getIdPaloTessitura());
          SolmrLogger.debug(this, "Value of parameter [ID_PALO_TESSITURA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdPaloTessitura()+"\n");
        }
        else {
          stmt.setString(++idx, null);
          SolmrLogger.debug(this, "Value of parameter [ID_PALO_TESSITURA] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
        }
        if(storicoUnitaArboreaVO.getIdAncoraggioUnar() != null) {
          stmt.setLong(++idx, storicoUnitaArboreaVO.getIdAncoraggioUnar());
          SolmrLogger.debug(this, "Value of parameter [ID_ANCORAGGIO_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdAncoraggioUnar()+"\n");
        }
        else {
          stmt.setString(++idx, null);
          SolmrLogger.debug(this, "Value of parameter [ID_ANCORAGGIO_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
        }
        if(storicoUnitaArboreaVO.getIdStatoColtivazioneUnar() != null) {
          stmt.setLong(++idx, storicoUnitaArboreaVO.getIdStatoColtivazioneUnar());
          SolmrLogger.debug(this, "Value of parameter [ID_STATO_COLTIVAZIONE_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getIdStatoColtivazioneUnar()+"\n");
        }
        else {
          stmt.setString(++idx, null);
          SolmrLogger.debug(this, "Value of parameter [ID_STATO_COLTIVAZIONE_UNAR] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
        }
        if(storicoUnitaArboreaVO.getDistanzaPali() != null) {
          stmt.setLong(++idx, storicoUnitaArboreaVO.getDistanzaPali());
          SolmrLogger.debug(this, "Value of parameter [DISTANZA_PALI] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getDistanzaPali()+"\n");
        }
        else {
          stmt.setString(++idx, null);
          SolmrLogger.debug(this, "Value of parameter [DISTANZA_PALI] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
        }
        if(storicoUnitaArboreaVO.getAltitudineSlm() != null) {
          stmt.setLong(++idx, storicoUnitaArboreaVO.getAltitudineSlm());
          SolmrLogger.debug(this, "Value of parameter [ALTITUDINE_SLM] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+storicoUnitaArboreaVO.getAltitudineSlm()+"\n");
        }
        else {
          stmt.setString(++idx, null);
          SolmrLogger.debug(this, "Value of parameter [ALTITUDINE_SLM] in method updateStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+null+"\n");
        }
        stmt.setBigDecimal(++idx, storicoUnitaArboreaVO.getAreaServizio());
        
        stmt.setBigDecimal(++idx, storicoUnitaArboreaVO.getPercentualePendenzaMedia());
        stmt.setBigDecimal(++idx, storicoUnitaArboreaVO.getGradiPendenzaMedia());
        stmt.setBigDecimal(++idx, storicoUnitaArboreaVO.getGradiEsposizioneMedia());
        stmt.setBigDecimal(++idx, convertIntegerToBigDecimal(storicoUnitaArboreaVO.getMetriAltitudineMedia()));
        stmt.setBigDecimal(++idx, convertLongToBigDecimal(storicoUnitaArboreaVO.getIdUnitaArboreaMadre()));
        stmt.setBigDecimal(++idx, convertLongToBigDecimal(storicoUnitaArboreaVO.getIdCatalogoMatrice()));
        
				stmt.addBatch();
			}

			int[] risultati = stmt.executeBatch();
			for(int i = 0; i < risultati.length; i++) {
				int risultato = risultati[i];
				if(risultato < 0 && risultato != -2) {
					throw new DataAccessException();
				}
			}

			stmt.close();
	    }
	    catch(SQLException exc) {
	    	SolmrLogger.error(this, "insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO - SQLException: "+exc);
	    	throw new DataAccessException(exc.getMessage());
	    }
	    catch (Exception ex) {
	    	SolmrLogger.error(this, "insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO - Generic Exception: "+ex);
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
	    		SolmrLogger.error(this, "insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc);
	    		throw new DataAccessException(exc.getMessage());
	    	}
	    	catch(Exception ex) {
	    		SolmrLogger.error(this, "insertAllStoricoUnitaArborea in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex);
	    		throw new DataAccessException(ex.getMessage());
	    	}
	    }
	    SolmrLogger.debug(this, "Invocated insertAllStoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
	}
	
	
	/**
   * Metodo che mi permette di recuperare le provincie delle unita arboree
   * 
   * @param idStoricoUnitaArborea
   * @return Vector di String
   * @throws DataAccessException
   */
  public Vector<String> findProvinciaStoricoParticellaArborea(long[] idStoricoUnitaArborea) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating findProvinciaStoricoParticellaArborea method in StoricoUnitaArboreaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<String> vIstatProv = null;

    try {
      SolmrLogger.debug(this, "Creating db-connection in findProvinciaStoricoParticellaArborea method in StoricoUnitaArboreaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in findProvinciaStoricoParticellaArborea method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");

      String query = " SELECT   " +
               "        DISTINCT(P.ISTAT_PROVINCIA) " +
               " FROM   DB_STORICO_PARTICELLA SP, " +
               "        COMUNE C, " +
               "        PROVINCIA P, " +
               "        DB_STORICO_UNITA_ARBOREA SUA " +
               " WHERE  SUA.ID_STORICO_UNITA_ARBOREA IN ";
               
               for(int i=0; i<idStoricoUnitaArborea.length;i++)
               {
                 if(i==0)
                 {
                   query += "(?";
                 }
                 else
                 {
                   query += ",?";
                 }
               }
               query += ") ";
      
      query += " AND    SP.DATA_FINE_VALIDITA IS NULL " +
               " AND    SUA.ID_PARTICELLA = SP.ID_PARTICELLA " +
               " AND    SP.COMUNE = C.ISTAT_COMUNE " +
               " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA ";

      stmt = conn.prepareStatement(query);
      
      int idx = 0;
      for(int i=0; i<idStoricoUnitaArborea.length;i++)
      {
        stmt.setLong(++idx, idStoricoUnitaArborea[i]);
      }

      SolmrLogger.debug(this, "Executing findProvinciaStoricoParticellaArborea: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        if(vIstatProv == null)
        {
          vIstatProv = new Vector<String>();
        }
        
        vIstatProv.add(rs.getString("ISTAT_PROVINCIA"));
          
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "findProvinciaStoricoParticellaArborea in StoricoUnitaArboreaDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "findProvinciaStoricoParticellaArborea in StoricoUnitaArboreaDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "findProvinciaStoricoParticellaArborea in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "findProvinciaStoricoParticellaArborea in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated findProvinciaStoricoParticellaArborea method in StoricoUnitaArboreaDAO\n");
    return vIstatProv;
  }
  
  /**
   * 
   * restituisce le provincie su cui insistono le unità vitate delle parcelle
   * 
   * 
   * @param idAzienda
   * @param idIsolaParcella
   * @return
   * @throws DataAccessException
   */
  public Vector<String> findProvinciaStoricoParticellaArboreaIsolaParcella(long idAzienda, long[] idIsolaParcella) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating findProvinciaStoricoParticellaArboreaIsolaParcella method in StoricoUnitaArboreaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<String> vIstatProv = null;

    try {
      SolmrLogger.debug(this, "Creating db-connection in findProvinciaStoricoParticellaArboreaIsolaParcella method in StoricoUnitaArboreaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in findProvinciaStoricoParticellaArboreaIsolaParcella method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");

      String query = "" +
      		"SELECT  " + 
          "        DISTINCT(P.ISTAT_PROVINCIA) " + 
          "FROM    DB_STORICO_PARTICELLA SP, " +
          "        DB_STORICO_UNITA_ARBOREA SUA, " +
          "        COMUNE C, " +
          "        PROVINCIA P, " +
          "        DB_PARCELLA_CONDUZIONE PC, " +
          "        DB_CONDUZIONE_DICHIARATA CD, " +
          "        DB_CONDUZIONE_PARTICELLA CP, " +
          "        DB_UTE UT " +
          "WHERE   SUA.ID_AZIENDA = ? " +
          "AND     PC.ID_ISOLA_PARCELLA IN ";
      
     for(int i=0; i<idIsolaParcella.length;i++)
     {
       if(i==0)
       {
         query += "(?";
       }
       else
       {
         query += ",?";
       }
     }
     query += ") ";
         
     query += "" +
     		"AND     PC.ID_CONDUZIONE_DICHIARATA = CD.ID_CONDUZIONE_DICHIARATA " +
     		"AND     CD.ID_STORICO_PARTICELLA    = SP.ID_STORICO_PARTICELLA " +
        "AND     SP.ID_PARTICELLA = SUA.ID_PARTICELLA " +
        "AND     SUA.ID_TIPOLOGIA_UNAR = 2 " +
        "AND     SUA.ID_AZIENDA              = UT.ID_AZIENDA " +
        "AND     UT.ID_UTE                   = CP.ID_UTE " +
        "AND     CP.ID_PARTICELLA            = SUA.ID_PARTICELLA " +
        "AND     UT.DATA_FINE_ATTIVITA      IS NULL " +
        "AND     CP.DATA_FINE_CONDUZIONE    IS NULL " +
        "AND     SP.COMUNE = C.ISTAT_COMUNE  " +
        "AND     C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA";

      stmt = conn.prepareStatement(query);
      
      int idx = 0;
      
      stmt.setLong(++idx, idAzienda);
      for(int i=0; i<idIsolaParcella.length;i++)
      {
        stmt.setLong(++idx, idIsolaParcella[i]);
      }

      SolmrLogger.debug(this, "Executing findProvinciaStoricoParticellaArboreaIsolaParcella: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        if(vIstatProv == null)
        {
          vIstatProv = new Vector<String>();
        }
        
        vIstatProv.add(rs.getString("ISTAT_PROVINCIA"));
          
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "findProvinciaStoricoParticellaArboreaIsolaParcella in StoricoUnitaArboreaDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "findProvinciaStoricoParticellaArboreaIsolaParcella in StoricoUnitaArboreaDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "findProvinciaStoricoParticellaArboreaIsolaParcella in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "findProvinciaStoricoParticellaArboreaIsolaParcella in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated findProvinciaStoricoParticellaArboreaIsolaParcella method in StoricoUnitaArboreaDAO\n");
    return vIstatProv;
  }
  
  /**
   * Metodo che mi permette di recuperare le provincie delle unita arboree dichiarate
   * 
   * @param idUnitaArboreaDichiarata
   * @return Vector di String
   * @throws DataAccessException
   */
  public Vector<String> findProvinciaParticellaArboreaDichiarata(long[] idUnitaArboreaDichiarata) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating findProvinciaParticellaArboreaDichiarata method in StoricoUnitaArboreaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<String> vIstatProv = null;

    try {
      SolmrLogger.debug(this, "Creating db-connection in findProvinciaParticellaArboreaDichiarata method in StoricoUnitaArboreaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in findProvinciaParticellaArboreaDichiarata method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");

      String query = " SELECT   " +
               "        DISTINCT(P.ISTAT_PROVINCIA) " +
               " FROM   DB_STORICO_PARTICELLA SP, " +
               "        COMUNE C, " +
               "        PROVINCIA P, " +
               "        DB_UNITA_ARBOREA_DICHIARATA UAD " +
               " WHERE  UAD.ID_UNITA_ARBOREA_DICHIARATA IN ";
               
               for(int i=0; i<idUnitaArboreaDichiarata.length;i++)
               {
                 if(i==0)
                 {
                   query += "(?";
                 }
                 else
                 {
                   query += ",?";
                 }
               }
               query += ") ";
      
      query += " AND    UAD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
               " AND    SP.COMUNE = C.ISTAT_COMUNE " +
               " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA ";

      stmt = conn.prepareStatement(query);
      
      int idx = 0;
      for(int i=0; i<idUnitaArboreaDichiarata.length;i++)
      {
        stmt.setLong(++idx, idUnitaArboreaDichiarata[i]);
      }

      SolmrLogger.debug(this, "Executing findProvinciaParticellaArboreaDichiarata: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        if(vIstatProv == null)
        {
          vIstatProv = new Vector<String>();
        }
        
        vIstatProv.add(rs.getString("ISTAT_PROVINCIA"));
          
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "findProvinciaParticellaArboreaDichiarata in StoricoUnitaArboreaDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "findProvinciaParticellaArboreaDichiarata in StoricoUnitaArboreaDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "findProvinciaParticellaArboreaDichiarata in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "findProvinciaParticellaArboreaDichiarata in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated findProvinciaParticellaArboreaDichiarata method in StoricoUnitaArboreaDAO\n");
    return vIstatProv;
  }
  
  
  /**
   * Restituisce la somma del campo area di tutte le uv relative
   * alla particella
   * 
   * 
   * @param idAzienda
   * @param idParticella
   * @return
   * @throws DataAccessException
   */
  public BigDecimal getSumAreaUVParticella(long idAzienda, long idParticella) 
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
          "[StoricoUnitaArboreaDAO::getSumAreaUVParticella] BEGIN.");      
       
      queryBuf = new StringBuffer();
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT NVL(SUM(SUA.AREA),0) AS SUM_AREA " +
        "FROM   DB_CONDUZIONE_PARTICELLA CP, " +
        "       DB_UTE UT, " +
        "       DB_STORICO_UNITA_ARBOREA SUA " +
        "WHERE  SUA.ID_AZIENDA =  ?  " +
        "AND    SUA.ID_PARTICELLA = ? " +
        "AND    SUA.ID_AZIENDA = UT.ID_AZIENDA " + 
        "AND    SUA.ID_TIPOLOGIA_UNAR = 2 " +
        "AND    UT.ID_UTE = CP.ID_UTE " +
        "AND    CP.ID_PARTICELLA = SUA.ID_PARTICELLA " + 
        "AND    UT.DATA_FINE_ATTIVITA IS NULL " +
        "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "AND    SUA.DATA_FINE_VALIDITA IS NULL");
      
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[StoricoUnitaArboreaDAO::getSumAreaUVParticella] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1,idAzienda);
      stmt.setLong(2,idParticella);
      
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        result = rs.getBigDecimal("SUM_AREA");        
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
          "[StoricoUnitaArboreaDAO::getSumAreaUVParticella] ", t,
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
          "[StoricoUnitaArboreaDAO::getSumAreaUVParticella] END.");
    }
  }
  
  /**
   * restituisce il numero delle uv relative alla 
   * particella e alla azienda
   * 
   * 
   * @param idAzienda
   * @param idParticella
   * @return
   * @throws DataAccessException
   */
  public int getNumUVParticella(long idAzienda, long idParticella) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Integer result = null;
    try
    {
      SolmrLogger.debug(this,
          "[StoricoUnitaArboreaDAO::getNumUVParticella] BEGIN.");      
       
      queryBuf = new StringBuffer();
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT COUNT(*) AS NUM_UV " +
        "FROM   DB_CONDUZIONE_PARTICELLA CP, " +
        "       DB_UTE UT, " +
        "       DB_STORICO_UNITA_ARBOREA SUA " +
        "WHERE  SUA.ID_AZIENDA =  ?  " +
        "AND    SUA.ID_PARTICELLA = ? " +
        "AND    SUA.ID_AZIENDA = UT.ID_AZIENDA " + 
        "AND    SUA.ID_TIPOLOGIA_UNAR = 2 " +
        "AND    UT.ID_UTE = CP.ID_UTE " +
        "AND    CP.ID_PARTICELLA = SUA.ID_PARTICELLA " + 
        "AND    UT.DATA_FINE_ATTIVITA IS NULL " +
        "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "AND    SUA.DATA_FINE_VALIDITA IS NULL");
      
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[StoricoUnitaArboreaDAO::getNumUVParticella] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1,idAzienda);
      stmt.setLong(2,idParticella);
      
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        result = checkInt(rs.getString("NUM_UV"));        
      }
      
      if(result == null)
        return 0;
      else
        return result.intValue();
      
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
          "[StoricoUnitaArboreaDAO::getNumUVParticella] ", t,
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
          "[StoricoUnitaArboreaDAO::getNumUVParticella] END.");
    }
  }
  
  
  
  public StoricoUnitaArboreaVO findStoricoUnitaArborea(Long idStoricoUnitaArborea) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating findStoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    StoricoUnitaArboreaVO storicoUnitaArboreaVO = null;

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in findStoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in findStoricoUnitaArborea method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");
      
      

      String query = " " +
      		     "SELECT  SUA.ID_STORICO_UNITA_ARBOREA, " +
               "        SUA.ID_UNITA_ARBOREA, " +
               "        SUA.ID_PARTICELLA, " +
               "        SUA.PROGR_UNAR, " +
               "        SUA.DATA_INIZIO_VALIDITA, " +
               "        SUA.DATA_FINE_VALIDITA, " +
               "        SUA.DATA_LAVORAZIONE, " +
               "        SUA.ID_TIPOLOGIA_UNAR, " +
               "        SUA.AREA, " +
               "        SUA.SESTO_SU_FILA, " +
               "        SUA.SESTO_TRA_FILE, " +
               "        SUA.NUM_CEPPI, " +
               "        SUA.ANNO_IMPIANTO, " +
               "        SUA.DATA_IMPIANTO, " +
               "        SUA.ANNO_REINNESTO, " +
               "        SUA.ID_FORMA_ALLEVAMENTO, " +
               "        SUA.ID_IRRIGAZIONE_UNAR, " +
               "        SUA.ID_COLTIVAZIONE_UNAR, " +
               "        SUA.CODICE_TIPO_VARIETA, " +
               "        SUA.PRESENZA_ALTRI_VITIGNI, " +
               "        SUA.NUMERO_PIANTE_PRODUTTIVO, " +
               "        SUA.NUMERO_ALTRE_PIANTE, " +
               "        SUA.CAMPAGNA, " +
               "        SUA.ID_TIPOLOGIA_VIGNETO, " +
               "        SUA.TIPO_IMPIANTO, " +
               "        SUA.NUMERO_CASTAGNI, " +
               "        SUA.GRUPPO, " +
               "        SUA.RICADUTA, " +
               "        SUA.ID_GIACITURA_UNAR, " +
               "        SUA.ID_ROCCIA_UNAR, " +
               "        SUA.ID_SCHELETRO_UNAR, " +
               "        SUA.ID_STATO_VEGETATIVO_UNAR, " +
               "        SUA.ID_POTATURA_UNAR, " +
               "        SUA.ID_GIUDIZIO_UNAR, " +
               "        SUA.SUPPLEMENTARI, " +
               "        SUA.MECCANIZZABILE, " +
               "        SUA.DIMENSIONE_CHIOMA, " +
               "        SUA.ID_ETA_IMPIANTO_UNAR, " +
               "        SUA.PROVINCIA_CCIAA, " +
               "        SUA.MATRICOLA_CCIAA, " +
               "        SUA.CONFERMA_PREC_ISCRIZIONE_ALBO, " +
               "        SUA.RICHIESTA_NUOVA_ISCR_ALBO, " +
               "        SUA.CONFERMA_RICH_NUOVA_ISCR_ALBO, " +
               "        SUA.SUPERFICIE_DA_ISCRIVERE_ALBO, " +
               "        SUA.ANNO_ISCRIZIONE_ALBO, " +
               "        SUA.DATA_ISCRIZIONE_ALBO, " +
               "        SUA.ID_FONTE, " +
               "        SUA.ID_VARIAZIONE_UNAR, " +
               "        SUA.NOTE, " +
               "        SUA.DATA_AGGIORNAMENTO, " +
               "        SUA.ID_UTENTE_AGGIORNAMENTO, " +
               "        SUA.ID_VARIETA, " +
               "        SUA.ID_UTILIZZO, " +
               "        SUA.PERCENTUALE_VARIETA, " +
               "        SUA.ID_VINO, " +
               "        SUA.DATA_ESECUZIONE, " +
               "        SUA.RECORD_MODIFICATO, " +
               "        SUA.ESITO_CONTROLLO, " +
               "        SUA.ID_CAUSALE_MODIFICA, " +
               "        TCM.ALTRO_PROCEDIMENTO, " +
               "        SUA.ID_AZIENDA, " +
               "        SUA.ID_CESSAZIONE_UNAR, " +
               "        SUA.ID_TIPOLOGIA_VINO, " +
               "        SUA.ANNO_RIFERIMENTO, " +
               "        SUA.COLTURA_SPECIALIZZATA, " +
               "        SUA.STATO_UNITA_ARBOREA, " +
               "        SUA.ANNO_PRIMA_PRODUZIONE, " +
               "        SUA.DATA_PRIMA_PRODUZIONE, " +
               "        SUA.VIGNA, " +
               "        SUA.ID_VIGNA, " +
               "        SUA.ID_MENZIONE_GEOGRAFICA, " +
               "        SUA.ETICHETTA, " +
               "        SUA.ID_GENERE_ISCRIZIONE, " +
               "        SUA.FLAG_IMPRODUTTIVA, " +
               "        SUA.PERCENTUALE_FALLANZA, " +
               "        SUA.DATA_SOVRAINNESTO, " +
               "        SUA.DATA_INTERVENTO, " +
               "        SUA.ID_TIPO_INTERVENTO_VITICOLO, " +
               "        SUA.ID_FILO_SOSTEGNO, " +
               "        SUA.ID_PALO_TESTATA, " +
               "        SUA.ID_PALO_TESSITURA, " +
               "        SUA.ID_ANCORAGGIO_UNAR, " +
               "        SUA.ID_STATO_COLTIVAZIONE_UNAR, " +
               "        SUA.DISTANZA_PALI, " +
               "        SUA.ALTITUDINE_SLM, " +
               "        SUA.AREA_SERVIZIO, " +
               "        SUA.PERCENTUALE_PENDENZA_MEDIA, " +
               "        SUA.GRADI_PENDENZA_MEDIA, " +
               "        SUA.GRADI_ESPOSIZIONE_MEDIA," +
               "        SUA.METRI_ALTITUDINE_MEDIA, " +
               "        SUA.ID_UNITA_ARBOREA_MADRE, " +
               "        SUA.ID_CATALOGO_MATRICE " +
               " FROM   DB_STORICO_UNITA_ARBOREA SUA," +
               "        DB_TIPO_CAUSALE_MODIFICA TCM " +
               " WHERE  SUA.ID_STORICO_UNITA_ARBOREA = ? " +
               " AND    SUA.ID_CAUSALE_MODIFICA = TCM.ID_CAUSALE_MODIFICA(+) ";

      SolmrLogger.debug(this, "Value of parameter 1 [ID_STORICO_UNITA_ARBOREA] in findStoricoUnitaArborea method in StoricoUnitaArboreaDAO: "+idStoricoUnitaArborea+"\n");

      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idStoricoUnitaArborea.longValue());

      SolmrLogger.debug(this, "Executing findStoricoUnitaArborea: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      if(rs.next()) 
      {
        storicoUnitaArboreaVO = new StoricoUnitaArboreaVO();
        storicoUnitaArboreaVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
        storicoUnitaArboreaVO.setIdUnitaArborea(new Long(rs.getLong("ID_UNITA_ARBOREA")));
        storicoUnitaArboreaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
        storicoUnitaArboreaVO.setProgrUnar(rs.getString("PROGR_UNAR"));
        storicoUnitaArboreaVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        storicoUnitaArboreaVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        storicoUnitaArboreaVO.setDataLavorazione(rs.getTimestamp("DATA_LAVORAZIONE"));
        storicoUnitaArboreaVO.setIdTipologiaUnar(checkLongNull(rs.getString("ID_TIPOLOGIA_UNAR")));
        storicoUnitaArboreaVO.setArea(rs.getString("AREA"));
        storicoUnitaArboreaVO.setSestoSuFila(rs.getString("SESTO_SU_FILA"));
        storicoUnitaArboreaVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
        storicoUnitaArboreaVO.setNumCeppi(rs.getString("NUM_CEPPI"));
        storicoUnitaArboreaVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
        storicoUnitaArboreaVO.setDataImpianto(rs.getTimestamp("DATA_IMPIANTO"));
        storicoUnitaArboreaVO.setAnnoReinnesto(rs.getString("ANNO_REINNESTO"));
        storicoUnitaArboreaVO.setIdFormaAllevamento(checkLongNull(rs.getString("ID_FORMA_ALLEVAMENTO")));
        storicoUnitaArboreaVO.setIdIrrigazioneUnar(checkLongNull(rs.getString("ID_IRRIGAZIONE_UNAR")));
        storicoUnitaArboreaVO.setIdColtivazioneUnar(checkLongNull(rs.getString("ID_COLTIVAZIONE_UNAR")));
        storicoUnitaArboreaVO.setCodiceTipoVarieta(rs.getString("CODICE_TIPO_VARIETA"));
        storicoUnitaArboreaVO.setPresenzaAltriVitigni(rs.getString("PRESENZA_ALTRI_VITIGNI"));
        storicoUnitaArboreaVO.setNumeroPianteProduttivo(rs.getString("NUMERO_PIANTE_PRODUTTIVO"));
        storicoUnitaArboreaVO.setNumeroAltrePiante(rs.getString("NUMERO_ALTRE_PIANTE"));
        storicoUnitaArboreaVO.setCampagna(rs.getString("CAMPAGNA"));
        storicoUnitaArboreaVO.setIdTipologiaVigneto(checkLongNull(rs.getString("ID_TIPOLOGIA_VIGNETO")));
        storicoUnitaArboreaVO.setTipoImpianto(rs.getString("TIPO_IMPIANTO"));
        storicoUnitaArboreaVO.setNumeroCastagni(rs.getString("NUMERO_CASTAGNI"));
        storicoUnitaArboreaVO.setGruppo(rs.getString("GRUPPO"));
        storicoUnitaArboreaVO.setRicaduta(rs.getString("RICADUTA"));
        storicoUnitaArboreaVO.setIdGiacituraUnar(checkLongNull(rs.getString("ID_GIACITURA_UNAR")));
        storicoUnitaArboreaVO.setIdRocciaUnar(checkLongNull(rs.getString("ID_ROCCIA_UNAR")));
        storicoUnitaArboreaVO.setIdScheletroUnar(checkLongNull(rs.getString("ID_SCHELETRO_UNAR")));
        storicoUnitaArboreaVO.setIdStatoVegetativoUnar(checkLongNull(rs.getString("ID_STATO_VEGETATIVO_UNAR")));
        storicoUnitaArboreaVO.setIdPotaturaUnar(checkLongNull(rs.getString("ID_POTATURA_UNAR")));
        storicoUnitaArboreaVO.setIdGiudizioUnar(checkLongNull(rs.getString("ID_GIUDIZIO_UNAR")));
        storicoUnitaArboreaVO.setSupplementari(rs.getString("SUPPLEMENTARI"));
        storicoUnitaArboreaVO.setMeccanizzabile(rs.getString("MECCANIZZABILE"));
        storicoUnitaArboreaVO.setDimensioneChioma(rs.getString("DIMENSIONE_CHIOMA"));
        storicoUnitaArboreaVO.setIdEtaImpiantoUnar(checkLongNull(rs.getString("ID_ETA_IMPIANTO_UNAR")));
        storicoUnitaArboreaVO.setProvinciaCCIAA(rs.getString("PROVINCIA_CCIAA"));
        storicoUnitaArboreaVO.setMatricolaCCIAA(rs.getString("MATRICOLA_CCIAA"));
        storicoUnitaArboreaVO.setConfermaPrecIscrizioneAlbo(rs.getString("CONFERMA_PREC_ISCRIZIONE_ALBO"));
        storicoUnitaArboreaVO.setRichiestaNuovaIscrAlbo(rs.getString("RICHIESTA_NUOVA_ISCR_ALBO"));
        storicoUnitaArboreaVO.setConfermaRichNuovaIscrAlbo(rs.getString("CONFERMA_RICH_NUOVA_ISCR_ALBO"));
        storicoUnitaArboreaVO.setSuperficieDaIscrivereAlbo(rs.getString("SUPERFICIE_DA_ISCRIVERE_ALBO"));
        storicoUnitaArboreaVO.setAnnoIscrizioneAlbo(rs.getString("ANNO_ISCRIZIONE_ALBO"));
        storicoUnitaArboreaVO.setDataIscrizioneAlbo(rs.getTimestamp("DATA_ISCRIZIONE_ALBO"));
        storicoUnitaArboreaVO.setIdFonte(checkLongNull(rs.getString("ID_FONTE")));
        storicoUnitaArboreaVO.setIdVariazioneUnar(checkLongNull(rs.getString("ID_VARIAZIONE_UNAR")));
        storicoUnitaArboreaVO.setNote(rs.getString("NOTE"));
        storicoUnitaArboreaVO.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO"));
        storicoUnitaArboreaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        storicoUnitaArboreaVO.setIdVarieta(checkLongNull(rs.getString("ID_VARIETA")));
        storicoUnitaArboreaVO.setIdUtilizzo(checkLongNull(rs.getString("ID_UTILIZZO")));
        storicoUnitaArboreaVO.setPercentualeVarieta(rs.getString("PERCENTUALE_VARIETA"));
        storicoUnitaArboreaVO.setIdVino(checkLongNull(rs.getString("ID_VINO")));          
        storicoUnitaArboreaVO.setDataEsecuzione(rs.getTimestamp("DATA_ESECUZIONE"));
        storicoUnitaArboreaVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
        Long idCausaleModifica = checkLongNull(rs.getString("ID_CAUSALE_MODIFICA"));
        storicoUnitaArboreaVO.setIdCausaleModifica(idCausaleModifica);
        if(Validator.isNotEmpty(idCausaleModifica))
        {
          TipoCausaleModificaVO tipoCausaleModificaVO = new TipoCausaleModificaVO();
          tipoCausaleModificaVO.setIdCausaleModifica(idCausaleModifica);
          tipoCausaleModificaVO.setAltroProcedimento(rs.getString("ALTRO_PROCEDIMENTO"));
          storicoUnitaArboreaVO.setTipoCausaleModificaVO(tipoCausaleModificaVO);
        }
        storicoUnitaArboreaVO.setRecordModificato(rs.getString("RECORD_MODIFICATO"));
        storicoUnitaArboreaVO.setIdAzienda(checkLongNull(rs.getString("ID_AZIENDA")));
        storicoUnitaArboreaVO.setIdCessazioneUnar(checkLongNull(rs.getString("ID_CESSAZIONE_UNAR")));
        storicoUnitaArboreaVO.setIdTipologiaVino(checkLongNull(rs.getString("ID_TIPOLOGIA_VINO")));
        storicoUnitaArboreaVO.setAnnoRiferimento(rs.getString("ANNO_RIFERIMENTO"));
        storicoUnitaArboreaVO.setColturaSpecializzata(rs.getString("COLTURA_SPECIALIZZATA"));
        storicoUnitaArboreaVO.setStatoUnitaArborea(rs.getString("STATO_UNITA_ARBOREA"));
        storicoUnitaArboreaVO.setAnnoPrimaProduzione(rs.getString("ANNO_PRIMA_PRODUZIONE"));
        storicoUnitaArboreaVO.setDataPrimaProduzione(rs.getDate("DATA_PRIMA_PRODUZIONE"));
        storicoUnitaArboreaVO.setVigna(rs.getString("VIGNA"));
        storicoUnitaArboreaVO.setIdVigna(checkLongNull(rs.getString("ID_VIGNA")));
        storicoUnitaArboreaVO.setIdMenzioneGeografica(checkLongNull(rs.getString("ID_MENZIONE_GEOGRAFICA")));
        storicoUnitaArboreaVO.setEtichetta(rs.getString("ETICHETTA"));
        storicoUnitaArboreaVO.setIdGenereIscrizione(checkLongNull(rs.getString("ID_GENERE_ISCRIZIONE")));
        storicoUnitaArboreaVO.setFlagImproduttiva(rs.getString("FLAG_IMPRODUTTIVA"));
        storicoUnitaArboreaVO.setPercentualeFallanza(rs.getBigDecimal("PERCENTUALE_FALLANZA"));
        storicoUnitaArboreaVO.setIdTipoInterventoViticolo(checkLongNull(rs.getString("ID_TIPO_INTERVENTO_VITICOLO")));
        storicoUnitaArboreaVO.setDataIntervento(rs.getTimestamp("DATA_INTERVENTO"));
        storicoUnitaArboreaVO.setDataSovrainnesto(rs.getTimestamp("DATA_SOVRAINNESTO"));
        
        storicoUnitaArboreaVO.setIdFiloSostegno(checkLongNull(rs.getString("ID_FILO_SOSTEGNO")));
        storicoUnitaArboreaVO.setIdPaloTestata(checkLongNull(rs.getString("ID_PALO_TESTATA")));
        storicoUnitaArboreaVO.setIdPaloTessitura(checkLongNull(rs.getString("ID_PALO_TESSITURA")));
        storicoUnitaArboreaVO.setIdAncoraggioUnar(checkLongNull(rs.getString("ID_ANCORAGGIO_UNAR")));
        storicoUnitaArboreaVO.setIdStatoColtivazioneUnar(checkLongNull(rs.getString("ID_STATO_COLTIVAZIONE_UNAR")));
        storicoUnitaArboreaVO.setDistanzaPali(checkLongNull(rs.getString("DISTANZA_PALI")));
        storicoUnitaArboreaVO.setAltitudineSlm(checkLongNull(rs.getString("ALTITUDINE_SLM")));
        storicoUnitaArboreaVO.setAreaServizio(rs.getBigDecimal("AREA_SERVIZIO"));
        
        storicoUnitaArboreaVO.setPercentualePendenzaMedia(rs.getBigDecimal("PERCENTUALE_PENDENZA_MEDIA"));
        storicoUnitaArboreaVO.setGradiPendenzaMedia(rs.getBigDecimal("GRADI_PENDENZA_MEDIA"));
        storicoUnitaArboreaVO.setGradiEsposizioneMedia(rs.getBigDecimal("GRADI_ESPOSIZIONE_MEDIA"));
        storicoUnitaArboreaVO.setMetriAltitudineMedia(checkIntegerNull(rs.getString("METRI_ALTITUDINE_MEDIA")));
        storicoUnitaArboreaVO.setIdUnitaArboreaMadre(checkLongNull(rs.getString("ID_UNITA_ARBOREA_MADRE")));
        storicoUnitaArboreaVO.setIdCatalogoMatrice(checkLongNull(rs.getString("ID_CATALOGO_MATRICE")));
          
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "findStoricoUnitaArborea in StoricoUnitaArboreaDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "findStoricoUnitaArborea in StoricoUnitaArboreaDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "findStoricoUnitaArborea in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "findStoricoUnitaArborea in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated findStoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
    
    return storicoUnitaArboreaVO;
  }
  
  
  /**
   * 
   * valorizza la data fine validita
   * 
   * @param idStoricoUnitaArborea
   * @throws DataAccessException
   */
  public void storicizzaStoricoUnitaArborea(long idStoricoUnitaArborea) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating storicizzaStoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in storicizzaStoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in storicizzaStoricoUnitaArborea method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");
      
      String query = " UPDATE DB_STORICO_UNITA_ARBOREA " +
               " SET    DATA_FINE_VALIDITA = SYSDATE "+
               " WHERE  ID_STORICO_UNITA_ARBOREA = ? ";
      
      stmt = conn.prepareStatement(query);
      
      int idx = 0;

      SolmrLogger.debug(this, "Executing storicizzaStoricoUnitaArborea: "+query);
      
      
      stmt.setLong(++idx, idStoricoUnitaArborea);      
      SolmrLogger.debug(this, "Value of parameter [ID_STORICO_UNITA_ARBOREA] in method storicizzaStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+idStoricoUnitaArborea+"\n");
      
      
      stmt.executeUpdate();

      stmt.close();
    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "storicizzaStoricoUnitaArborea in StoricoUnitaArboreaDAO - SQLException: "+exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) 
    {
      SolmrLogger.error(this, "storicizzaStoricoUnitaArborea in StoricoUnitaArboreaDAO - Generic Exception: "+ex);
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
        SolmrLogger.error(this, "storicizzaStoricoUnitaArborea in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) 
      {
        SolmrLogger.error(this, "storicizzaStoricoUnitaArborea in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated storicizzaStoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
  }
  
  public void storicizzaStoricoUnitaArboreaDataAggiornamento(long idStoricoUnitaArborea, long idUtenteAggiornamento) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating storicizzaStoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in storicizzaStoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in storicizzaStoricoUnitaArborea method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");
      
      String query = " UPDATE DB_STORICO_UNITA_ARBOREA " +
               " SET    DATA_FINE_VALIDITA = SYSDATE, " +
               "        DATA_AGGIORNAMENTO = SYSDATE, " +
               "        ID_UTENTE_AGGIORNAMENTO = ? "+
               " WHERE  ID_STORICO_UNITA_ARBOREA = ? ";
      
      stmt = conn.prepareStatement(query);
      
      int idx = 0;

      SolmrLogger.debug(this, "Executing storicizzaStoricoUnitaArborea: "+query);
      
      stmt.setLong(++idx, idUtenteAggiornamento);
      stmt.setLong(++idx, idStoricoUnitaArborea);      
      SolmrLogger.debug(this, "Value of parameter [ID_STORICO_UNITA_ARBOREA] in method storicizzaStoricoUnitaArborea in StoricoUnitaArboreaDAO: "+idStoricoUnitaArborea+"\n");
      
      
      stmt.executeUpdate();

      stmt.close();
    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "storicizzaStoricoUnitaArborea in StoricoUnitaArboreaDAO - SQLException: "+exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) 
    {
      SolmrLogger.error(this, "storicizzaStoricoUnitaArborea in StoricoUnitaArboreaDAO - Generic Exception: "+ex);
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
        SolmrLogger.error(this, "storicizzaStoricoUnitaArborea in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) 
      {
        SolmrLogger.error(this, "storicizzaStoricoUnitaArborea in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated storicizzaStoricoUnitaArborea method in StoricoUnitaArboreaDAO\n");
  }
  
  
  /**
   * 
   * rispetto al metodo findStoricoParticellaArborea
   * ha in più la chiamata alla funzione tolleranza
   * 
   * @param idStoricoUnitaArborea
   * @param idAzienda
   * @param nomeLib
   * @return
   * @throws DataAccessException
   */
  public StoricoParticellaVO findStoricoParticellaArboreaTolleranza(Long idStoricoUnitaArborea, 
      long idAzienda, String nomeLib) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating findStoricoParticellaArboreaTolleranza method in StoricoUnitaArboreaDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    StoricoParticellaVO storicoParticellaVO = null;

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in findStoricoParticellaArboreaTolleranza method in StoricoUnitaArboreaDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in findStoricoParticellaArboreaTolleranza method in StoricoUnitaArboreaDAO and it values: "+conn+"\n");

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
               "        SUA.ID_STORICO_UNITA_ARBOREA, " +
               "        SUA.ID_UNITA_ARBOREA, " +
               "        SUA.ID_PARTICELLA, " +
               "        SUA.PROGR_UNAR, " +
               "        SUA.DATA_INIZIO_VALIDITA, " +
               "        SUA.DATA_FINE_VALIDITA, " +
               "        SUA.DATA_LAVORAZIONE, " +
               "        SUA.ID_TIPOLOGIA_UNAR, " +
               "        TTU.DESCRIZIONE AS DESC_TIPO_UNAR, " +
               "        SUA.AREA, " +
               "        SUA.SESTO_SU_FILA, " +
               "        SUA.SESTO_TRA_FILE, " +
               "        SUA.NUM_CEPPI, " +
               "        SUA.ANNO_IMPIANTO, " +
               "        SUA.DATA_IMPIANTO, " +
               "        SUA.ANNO_REINNESTO, " +
               "        SUA.ID_FORMA_ALLEVAMENTO, " +
               "        TFA.DESCRIZIONE AS DESC_FORMA_ALLEVAMENTO, " +
               "        SUA.ID_IRRIGAZIONE_UNAR, " +
               "        TIU.DESCRIZIONE AS DESC_IRRIGAZIONE, " +
               "        SUA.ID_COLTIVAZIONE_UNAR, " +
               "        TCU.DESCRIZIONE AS DESC_COLT_UNAR, " +
               "        SUA.CODICE_TIPO_VARIETA, " +
               "        SUA.PRESENZA_ALTRI_VITIGNI, " +
               "        SUA.NUMERO_PIANTE_PRODUTTIVO, " +
               "        SUA.NUMERO_ALTRE_PIANTE, " +
               "        SUA.CAMPAGNA, " +
               "        SUA.ID_TIPOLOGIA_VIGNETO, " +
               "        SUA.TIPO_IMPIANTO, " +
               "        SUA.NUMERO_CASTAGNI, " +
               "        SUA.GRUPPO, " +
               "        SUA.RICADUTA, " +
               "        SUA.ID_GIACITURA_UNAR, " +
               "        SUA.ID_ROCCIA_UNAR, " +
               "        SUA.ID_SCHELETRO_UNAR, " +
               "        SUA.ID_STATO_VEGETATIVO_UNAR, " +
               "        SUA.ID_POTATURA_UNAR, " +
               "        SUA.ID_GIUDIZIO_UNAR, " +
               "        SUA.SUPPLEMENTARI, " +
               "        SUA.MECCANIZZABILE, " +
               "        SUA.DIMENSIONE_CHIOMA, " +
               "        SUA.ID_ETA_IMPIANTO_UNAR, " +
               "        SUA.PROVINCIA_CCIAA, " +
               "        SUA.MATRICOLA_CCIAA, " +
               "        SUA.CONFERMA_PREC_ISCRIZIONE_ALBO, " +
               "        SUA.RICHIESTA_NUOVA_ISCR_ALBO, " +
               "        SUA.CONFERMA_RICH_NUOVA_ISCR_ALBO, " +
               "        SUA.SUPERFICIE_DA_ISCRIVERE_ALBO, " +
               "        SUA.ANNO_ISCRIZIONE_ALBO, " +
               "        SUA.DATA_ISCRIZIONE_ALBO, " +
               "        SUA.ID_FONTE, " +
               "        TF.DESCRIZIONE AS DESC_FONTE, " +
               "        SUA.ID_VARIAZIONE_UNAR, " +
               "        SUA.NOTE, " +
               "        SUA.DATA_AGGIORNAMENTO, " +
               "        SUA.ID_UTENTE_AGGIORNAMENTO, " +
               "        RCM.ID_UTILIZZO, " +
               "        TU.CODICE, " +
               "        TU.DESCRIZIONE AS DESC_TIPO_UTILIZZO, " +
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
               "        TVAR.DESCRIZIONE AS DESC_VARIETA, " +
               "        TVAR.CODICE_VARIETA AS COD_VAR, " +
               "        SUA.PERCENTUALE_VARIETA, " +
               "        SUA.ID_VINO, " +
               "        TV.DESCRIZIONE AS DESC_TIPO_VINO, " +
               "        SUA.DATA_ESECUZIONE, " +
               "        SUA.RECORD_MODIFICATO, " +
               "        SUA.ESITO_CONTROLLO, " +
               "        PC.ID_PARTICELLA_CERTIFICATA, "+
               "        PC.SUP_COLT_ARBOREA_SPECIALIZZATA, " +
               "        PC.SUP_GRAFICA, " +
               "        PC.SUP_USO_GRAFICA, " +
               "        SUA.ID_CAUSALE_MODIFICA, " +
               "        TCM.DESCRIZIONE AS CAUSA_MOD, " +
               "        TCM.ALTRO_PROCEDIMENTO, " +
               "        SUA.ID_AZIENDA, " +
               "        SUA.ID_CESSAZIONE_UNAR, " +
               "        TCEUNAR.DESCRIZIONE AS DESC_CESS_UNAR, " +
               "        SUA.ID_TIPOLOGIA_VINO, " +
               "        TTV.DESCRIZIONE AS DESC_TIPO_TIPOLOGIA_VINO, " +
               "        TTV.VINO_DOC, " +
               "        TTV.CODICE_MIPAF, " +
               "        TTV.RESA, " +
               "        TTV.FLAG_GESTIONE_VIGNA, " +
               "        TTV.FLAG_GESTIONE_ETICHETTA, " +
               "        SUA.ANNO_RIFERIMENTO, " +
               "        SUA.COLTURA_SPECIALIZZATA, " +
               "        SUA.STATO_UNITA_ARBOREA, " +
               "        SUA.ANNO_PRIMA_PRODUZIONE, " +
               "        SUA.DATA_PRIMA_PRODUZIONE, " +
               "        SUA.VIGNA, " +
               "        SUA.ID_VIGNA, " +
               "        SUA.ID_MENZIONE_GEOGRAFICA, " +
               "        SUA.ETICHETTA, " +
               "        SUA.ID_GENERE_ISCRIZIONE, " +
               "        SUA.FLAG_IMPRODUTTIVA, " +
               "        SUA.PERCENTUALE_FALLANZA, " +
               "        UA.DATA_CONSOLIDAMENTO_GIS, " +
               "        TGI.DESCRIZIONE AS DESC_GENERE_ISCRIZIONE, " +
               "        TGI.FLAG_DEFINITIVA, " +
               "        SUA.DATA_SOVRAINNESTO, " +
               "        SUA.DATA_INTERVENTO, " +
               "        SUA.ID_TIPO_INTERVENTO_VITICOLO, " +
               "        SUA.ID_FILO_SOSTEGNO, " +
               "        SUA.ID_PALO_TESTATA, " +
               "        SUA.ID_PALO_TESSITURA, " +
               "        SUA.ID_ANCORAGGIO_UNAR, " +
               "        SUA.ID_STATO_COLTIVAZIONE_UNAR, " +
               "        SUA.DISTANZA_PALI, " +
               "        SUA.ALTITUDINE_SLM, " +
               "        SUA.AREA_SERVIZIO, " +
               "        SUA.PERCENTUALE_PENDENZA_MEDIA, " +
               "        SUA.GRADI_PENDENZA_MEDIA, " +
               "        SUA.GRADI_ESPOSIZIONE_MEDIA," +
               "        SUA.METRI_ALTITUDINE_MEDIA, " +
               "        SUA.ID_UNITA_ARBOREA_MADRE, " +
               "        SUA.ID_CATALOGO_MATRICE, " +
               "        NVL(PCK_SMRGAA_LIBRERIA.SelTotSupElegByPartEVetCat(PC.ID_PARTICELLA_CERTIFICATA, SUA.ID_CATALOGO_MATRICE),0) AS SUPERFICIE_ELEG " +
               //"       "+nomeLib+".UV_IN_TOLLERANZA_GIS(?, SUA.ID_UNITA_ARBOREA) AS TOLLERANZA " +
               " FROM   DB_STORICO_PARTICELLA SP, " +
               "        COMUNE C, " +
               "        PROVINCIA P, " +
               "        DB_STORICO_UNITA_ARBOREA SUA, " +
               "        DB_R_CATALOGO_MATRICE RCM, " +
               "        DB_UNITA_ARBOREA UA, " +
               "        DB_TIPO_TIPOLOGIA_UNAR TTU, " +
               "        DB_TIPO_FORMA_ALLEVAMENTO TFA, " +
               "        DB_TIPO_UTILIZZO TU, " +
               "        DB_TIPO_DESTINAZIONE TDE, " +
               "        DB_TIPO_DETTAGLIO_USO TDU, " +
               "        DB_TIPO_QUALITA_USO TQU, " +
               "        DB_TIPO_VARIETA TVAR, " +
               "        DB_TIPO_VINO TV, " +
               "        DB_TIPO_FONTE TF, " +
               "        DB_TIPO_IRRIGAZIONE_UNAR TIU, " +
               "        DB_TIPO_COLTIVAZIONE_UNAR TCU, " +
               "        DB_PARTICELLA_CERTIFICATA PC, " +
               "        DB_TIPO_CAUSALE_MODIFICA TCM, " +
               "        DB_TIPO_CESSAZIONE_UNAR TCEUNAR, " +
               "        DB_TIPO_TIPOLOGIA_VINO TTV, " +
               "        DB_TIPO_GENERE_ISCRIZIONE TGI " +
               " WHERE  SUA.ID_STORICO_UNITA_ARBOREA = ? " +
               " AND    SUA.ID_UNITA_ARBOREA = UA.ID_UNITA_ARBOREA " +
               " AND    SUA.ID_PARTICELLA = SP.ID_PARTICELLA " +
               " AND    SP.COMUNE = C.ISTAT_COMUNE " +
               " AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
               " AND    SUA.ID_TIPOLOGIA_UNAR = TTU.ID_TIPOLOGIA_UNAR(+) " +
               " AND    SUA.ID_FORMA_ALLEVAMENTO = TFA.ID_FORMA_ALLEVAMENTO(+) " +
               " AND    SUA.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE(+) " +
               " AND    RCM.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
               " AND    RCM.ID_TIPO_DESTINAZIONE = TDE.ID_TIPO_DESTINAZIONE(+) " +
               " AND    RCM.ID_TIPO_DETTAGLIO_USO = TDU.ID_TIPO_DETTAGLIO_USO(+) " +
               " AND    RCM.ID_TIPO_QUALITA_USO = TQU.ID_TIPO_QUALITA_USO(+) " +
               " AND    RCM.ID_VARIETA = TVAR.ID_VARIETA(+) " +
               " AND    SUA.ID_VINO = TV.ID_VINO(+) " +
               " AND    SP.DATA_FINE_VALIDITA IS NULL " +
               " AND    SUA.ID_FONTE = TF.ID_FONTE(+) " +
               " AND    SUA.ID_COLTIVAZIONE_UNAR = TCU.ID_COLTIVAZIONE_UNAR(+) " +
               " AND    SUA.ID_IRRIGAZIONE_UNAR = TIU.ID_IRRIGAZIONE_UNAR(+) " +
               " AND    SP.COMUNE = PC.COMUNE(+) " +
               " AND    NVL(SP.SEZIONE, '-') = NVL(PC.SEZIONE(+), '-') " +
               " AND    SP.FOGLIO = PC.FOGLIO(+) " +
               " AND    SP.PARTICELLA = PC.PARTICELLA(+) " +
               " AND    NVL(SP.SUBALTERNO, '-') = NVL(PC.SUBALTERNO(+), '-') " +
               " AND    PC.DATA_FINE_VALIDITA(+) IS NULL " +
               " AND    SUA.ID_CAUSALE_MODIFICA = TCM.ID_CAUSALE_MODIFICA " +
               " AND    SUA.ID_CESSAZIONE_UNAR = TCEUNAR.ID_CESSAZIONE_UNAR(+) " +
               " AND    SUA.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO(+) " +
               " AND    SUA.ID_GENERE_ISCRIZIONE = TGI.ID_GENERE_ISCRIZIONE(+) ";

      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in findStoricoParticellaArborea method in StoricoUnitaArboreaDAO: "+idAzienda+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ID_STORICO_UNITA_ARBOREA] in findStoricoParticellaArborea method in StoricoUnitaArboreaDAO: "+idStoricoUnitaArborea+"\n");

      stmt = conn.prepareStatement(query);
      
      //stmt.setLong(1, idAzienda);
      stmt.setLong(1, idStoricoUnitaArborea.longValue());

      SolmrLogger.debug(this, "Executing findStoricoParticellaArboreaTolleranza: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        storicoParticellaVO = new StoricoParticellaVO();
        StoricoUnitaArboreaVO storicoUnitaArboreaVO = new StoricoUnitaArboreaVO();
        ParticellaCertificataVO particellaCertificataVO = new ParticellaCertificataVO();
        TipoCausaleModificaVO tipoCausaleModificaVO = new TipoCausaleModificaVO();
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
        storicoUnitaArboreaVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
        storicoUnitaArboreaVO.setIdUnitaArborea(new Long(rs.getLong("ID_UNITA_ARBOREA")));
        storicoUnitaArboreaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
        storicoUnitaArboreaVO.setProgrUnar(rs.getString("PROGR_UNAR"));
        storicoUnitaArboreaVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        storicoUnitaArboreaVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        storicoUnitaArboreaVO.setDataLavorazione(rs.getDate("DATA_LAVORAZIONE"));
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_UNAR"))) 
        {
          storicoUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
          TipoTipologiaUnitaArboreaVO tipoTipologiaUnitaArboreaVO = new TipoTipologiaUnitaArboreaVO();
          tipoTipologiaUnitaArboreaVO.setIdTipologiaUnar(new Long(rs.getLong("ID_TIPOLOGIA_UNAR")));
          tipoTipologiaUnitaArboreaVO.setDescrizione(rs.getString("DESC_TIPO_UNAR"));
          storicoUnitaArboreaVO.setTipoTipologiaUnitaArboreaVO(tipoTipologiaUnitaArboreaVO);
        }
        storicoUnitaArboreaVO.setArea(rs.getString("AREA"));
        storicoUnitaArboreaVO.setSestoSuFila(rs.getString("SESTO_SU_FILA"));
        storicoUnitaArboreaVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
        storicoUnitaArboreaVO.setNumCeppi(rs.getString("NUM_CEPPI"));
        storicoUnitaArboreaVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
        storicoUnitaArboreaVO.setDataImpianto(rs.getDate("DATA_IMPIANTO"));
        storicoUnitaArboreaVO.setAnnoReinnesto(rs.getString("ANNO_REINNESTO"));
        if(Validator.isNotEmpty(rs.getString("ID_FORMA_ALLEVAMENTO"))) 
        {
          storicoUnitaArboreaVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
          TipoFormaAllevamentoVO tipoFormaAllevamentoVO = new TipoFormaAllevamentoVO();
          tipoFormaAllevamentoVO.setIdFormaAllevamento(new Long(rs.getLong("ID_FORMA_ALLEVAMENTO")));
          tipoFormaAllevamentoVO.setDescrizione(rs.getString("DESC_FORMA_ALLEVAMENTO"));
          storicoUnitaArboreaVO.setTipoFormaAllevamentoVO(tipoFormaAllevamentoVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_IRRIGAZIONE_UNAR"))) 
        {
          storicoUnitaArboreaVO.setIdIrrigazioneUnar(new Long(rs.getLong("ID_IRRIGAZIONE_UNAR")));
          TipoIrrigazioneUnitaArboreaVO tipoIrrigazioneUnitaArboreaVO = new TipoIrrigazioneUnitaArboreaVO();
          tipoIrrigazioneUnitaArboreaVO.setIdIrrigazioneUnar(new Long(rs.getLong("ID_IRRIGAZIONE_UNAR")));
          tipoIrrigazioneUnitaArboreaVO.setDescrizione(rs.getString("DESC_IRRIGAZIONE"));
          storicoUnitaArboreaVO.setTipoIrrigazioneUnitaArboreaVO(tipoIrrigazioneUnitaArboreaVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_COLTIVAZIONE_UNAR"))) 
        {
          storicoUnitaArboreaVO.setIdColtivazioneUnar(new Long(rs.getLong("ID_COLTIVAZIONE_UNAR")));
          TipoColtivazioneUnitaArboreaVO tipoColtivazioneUnitaArboreaVO = new TipoColtivazioneUnitaArboreaVO();
          tipoColtivazioneUnitaArboreaVO.setIdColtivazioneUnar(new Long(rs.getLong("ID_COLTIVAZIONE_UNAR")));
          tipoColtivazioneUnitaArboreaVO.setDescrizione(rs.getString("DESC_COLT_UNAR"));
          storicoUnitaArboreaVO.setTipoColtivazioneUnitaArboreaVO(tipoColtivazioneUnitaArboreaVO);
        }
        storicoUnitaArboreaVO.setCodiceTipoVarieta(rs.getString("CODICE_TIPO_VARIETA"));
        storicoUnitaArboreaVO.setPresenzaAltriVitigni(rs.getString("PRESENZA_ALTRI_VITIGNI"));
        storicoUnitaArboreaVO.setNumeroPianteProduttivo(rs.getString("NUMERO_PIANTE_PRODUTTIVO"));
        storicoUnitaArboreaVO.setNumeroAltrePiante(rs.getString("NUMERO_ALTRE_PIANTE"));
        storicoUnitaArboreaVO.setCampagna(rs.getString("CAMPAGNA"));
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VIGNETO"))) 
        {
          storicoUnitaArboreaVO.setIdTipologiaVigneto(new Long(rs.getLong("ID_TIPOLOGIA_VIGNETO")));
        }
        storicoUnitaArboreaVO.setTipoImpianto(rs.getString("TIPO_IMPIANTO"));
        storicoUnitaArboreaVO.setNumeroCastagni(rs.getString("NUMERO_CASTAGNI"));
        storicoUnitaArboreaVO.setGruppo(rs.getString("GRUPPO"));
        storicoUnitaArboreaVO.setRicaduta(rs.getString("RICADUTA"));
        if(Validator.isNotEmpty(rs.getString("ID_GIACITURA_UNAR"))) 
        {
          storicoUnitaArboreaVO.setIdGiacituraUnar(new Long(rs.getLong("ID_GIACITURA_UNAR")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_ROCCIA_UNAR"))) {
          storicoUnitaArboreaVO.setIdRocciaUnar(new Long(rs.getLong("ID_ROCCIA_UNAR")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_SCHELETRO_UNAR"))) 
        {
          storicoUnitaArboreaVO.setIdScheletroUnar(new Long(rs.getLong("ID_SCHELETRO_UNAR")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_STATO_VEGETATIVO_UNAR"))) 
        {
          storicoUnitaArboreaVO.setIdStatoVegetativoUnar(new Long(rs.getLong("ID_STATO_VEGETATIVO_UNAR")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_POTATURA_UNAR"))) 
        {
          storicoUnitaArboreaVO.setIdPotaturaUnar(new Long(rs.getLong("ID_POTATURA_UNAR")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_GIUDIZIO_UNAR"))) 
        {
          storicoUnitaArboreaVO.setIdGiudizioUnar(new Long(rs.getLong("ID_GIUDIZIO_UNAR")));
        }
        storicoUnitaArboreaVO.setSupplementari(rs.getString("SUPPLEMENTARI"));
        storicoUnitaArboreaVO.setMeccanizzabile(rs.getString("MECCANIZZABILE"));
        storicoUnitaArboreaVO.setDimensioneChioma(rs.getString("DIMENSIONE_CHIOMA"));
        if(Validator.isNotEmpty(rs.getString("ID_ETA_IMPIANTO_UNAR"))) 
        {
          storicoUnitaArboreaVO.setIdEtaImpiantoUnar(new Long(rs.getLong("ID_ETA_IMPIANTO_UNAR")));
        }
        storicoUnitaArboreaVO.setProvinciaCCIAA(rs.getString("PROVINCIA_CCIAA"));
        storicoUnitaArboreaVO.setMatricolaCCIAA(rs.getString("MATRICOLA_CCIAA"));
        storicoUnitaArboreaVO.setConfermaPrecIscrizioneAlbo(rs.getString("CONFERMA_PREC_ISCRIZIONE_ALBO"));
        storicoUnitaArboreaVO.setRichiestaNuovaIscrAlbo(rs.getString("RICHIESTA_NUOVA_ISCR_ALBO"));
        storicoUnitaArboreaVO.setConfermaRichNuovaIscrAlbo(rs.getString("CONFERMA_RICH_NUOVA_ISCR_ALBO"));
        storicoUnitaArboreaVO.setSuperficieDaIscrivereAlbo(rs.getString("SUPERFICIE_DA_ISCRIVERE_ALBO"));
        storicoUnitaArboreaVO.setAnnoIscrizioneAlbo(rs.getString("ANNO_ISCRIZIONE_ALBO"));
        storicoUnitaArboreaVO.setDataIscrizioneAlbo(rs.getDate("DATA_ISCRIZIONE_ALBO"));
        if(Validator.isNotEmpty(rs.getString("ID_FONTE"))) 
        {
          storicoUnitaArboreaVO.setIdFonte(new Long(rs.getLong("ID_FONTE")));
          CodeDescription tipoFonte = new CodeDescription(new Integer(rs.getInt("ID_FONTE")), rs.getString("DESC_FONTE"));
          storicoUnitaArboreaVO.setTipoFonte(tipoFonte);
        }
        if(Validator.isNotEmpty(rs.getString("ID_VARIAZIONE_UNAR"))) 
        {
          storicoUnitaArboreaVO.setIdVariazioneUnar(new Long(rs.getLong("ID_VARIAZIONE_UNAR")));
        }
        storicoUnitaArboreaVO.setNote(rs.getString("NOTE"));
        storicoUnitaArboreaVO.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO"));
        storicoUnitaArboreaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        
        if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO"))) 
        {
          storicoUnitaArboreaVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
          TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
          tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
          tipoUtilizzoVO.setCodice(rs.getString("CODICE"));
          tipoUtilizzoVO.setDescrizione(rs.getString("DESC_TIPO_UTILIZZO"));
          storicoUnitaArboreaVO.setTipoUtilizzoVO(tipoUtilizzoVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_TIPO_DESTINAZIONE"))) 
        {
          storicoUnitaArboreaVO.setIdTipoDestinazione(new Long(rs.getLong("ID_TIPO_DESTINAZIONE")));
          TipoDestinazioneVO tipoDestinazioneVO = new TipoDestinazioneVO();
          tipoDestinazioneVO.setIdTipoDestinazione(new Long(rs.getLong("ID_TIPO_DESTINAZIONE")));
          tipoDestinazioneVO.setCodiceDestinazione(rs.getString("CODICE_DESTINAZIONE"));
          tipoDestinazioneVO.setDescrizioneDestinazione(rs.getString("DESCRIZIONE_DESTINAZIONE"));
          storicoUnitaArboreaVO.setTipoDestinazioneVO(tipoDestinazioneVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_TIPO_DETTAGLIO_USO"))) 
        {
          storicoUnitaArboreaVO.setIdTipoDettaglioUso(new Long(rs.getLong("ID_TIPO_DETTAGLIO_USO")));
          TipoDettaglioUsoVO tipoDettaglioUsoVO = new TipoDettaglioUsoVO();
          tipoDettaglioUsoVO.setIdTipoDettaglioUso(new Long(rs.getLong("ID_TIPO_DETTAGLIO_USO")));
          tipoDettaglioUsoVO.setCodiceDettaglioUso(rs.getString("CODICE_DETTAGLIO_USO"));
          tipoDettaglioUsoVO.setDescrizione(rs.getString("DESCRIZIONE_DETTAGLIO_USO"));
          storicoUnitaArboreaVO.setTipoDettaglioUsoVO(tipoDettaglioUsoVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_TIPO_QUALITA_USO"))) 
        {
          storicoUnitaArboreaVO.setIdTipoQualitaUso(new Long(rs.getLong("ID_TIPO_QUALITA_USO")));
          TipoQualitaUsoVO tipoQualitaUsoVO = new TipoQualitaUsoVO();
          tipoQualitaUsoVO.setIdTipoQualitaUso(new Long(rs.getLong("ID_TIPO_QUALITA_USO")));
          tipoQualitaUsoVO.setCodiceQualitaUso(rs.getString("CODICE_QUALITA_USO"));
          tipoQualitaUsoVO.setDescrizioneQualitaUso(rs.getString("DESCRIZIONE_QUALITA_USO"));
          storicoUnitaArboreaVO.setTipoQualitaUsoVO(tipoQualitaUsoVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) 
        {
          storicoUnitaArboreaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
          tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          tipoVarietaVO.setDescrizione(rs.getString("DESC_VARIETA"));
          tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VAR"));
          storicoUnitaArboreaVO.setTipoVarietaVO(tipoVarietaVO);
        }
        storicoUnitaArboreaVO.setPercentualeVarieta(rs.getString("PERCENTUALE_VARIETA"));
        if(Validator.isNotEmpty(rs.getString("ID_VINO"))) 
        {
          storicoUnitaArboreaVO.setIdVino(new Long(rs.getLong("ID_VINO")));
          TipoVinoVO tipoVinoVO = new TipoVinoVO();
          tipoVinoVO.setIdVino(new Long(rs.getLong("ID_VINO")));
          tipoVinoVO.setDescrizione(rs.getString("DESC_TIPO_VINO"));
          storicoUnitaArboreaVO.setTipoVinoVO(tipoVinoVO);
        }
        if(rs.getDate("DATA_ESECUZIONE") != null) 
        {
          storicoUnitaArboreaVO.setDataEsecuzione(new Timestamp(rs.getDate("DATA_ESECUZIONE").getTime()));
        }
        storicoUnitaArboreaVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
        storicoUnitaArboreaVO.setRecordModificato(rs.getString("RECORD_MODIFICATO"));
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
        storicoUnitaArboreaVO.setIdCausaleModifica(new Long(rs.getLong("ID_CAUSALE_MODIFICA")));
        tipoCausaleModificaVO.setIdCausaleModifica(new Long(rs.getLong("ID_CAUSALE_MODIFICA")));
        tipoCausaleModificaVO.setDescrizione(rs.getString("CAUSA_MOD"));
        tipoCausaleModificaVO.setAltroProcedimento(rs.getString("ALTRO_PROCEDIMENTO"));
        storicoUnitaArboreaVO.setTipoCausaleModificaVO(tipoCausaleModificaVO);
        if(Validator.isNotEmpty(rs.getString("ID_AZIENDA"))) {
          storicoUnitaArboreaVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_CESSAZIONE_UNAR"))) {
          storicoUnitaArboreaVO.setIdCessazioneUnar(new Long(rs.getLong("ID_CESSAZIONE_UNAR")));
          TipoCessazioneUnarVO tipoCessazioneUnarVO = new TipoCessazioneUnarVO();
          tipoCessazioneUnarVO.setIdCessazioneUnar(new Long(rs.getLong("ID_CESSAZIONE_UNAR")));
          tipoCessazioneUnarVO.setDescrizione(rs.getString("DESC_CESS_UNAR"));
          storicoUnitaArboreaVO.setTipoCessazioneUnarVO(tipoCessazioneUnarVO);
        }
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VINO"))) {
          storicoUnitaArboreaVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
          TipoTipologiaVinoVO tipoTipologiaVinoVO = new TipoTipologiaVinoVO();
          tipoTipologiaVinoVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
          tipoTipologiaVinoVO.setDescrizione(rs.getString("DESC_TIPO_TIPOLOGIA_VINO"));
          tipoTipologiaVinoVO.setCodiceMipaf(rs.getString("CODICE_MIPAF")); 
          tipoTipologiaVinoVO.setVinoDoc(rs.getString("VINO_DOC"));
          tipoTipologiaVinoVO.setResa(rs.getBigDecimal("RESA"));
          tipoTipologiaVinoVO.setFlagGestioneVigna(rs.getString("FLAG_GESTIONE_VIGNA"));
          tipoTipologiaVinoVO.setFlagGestioneEtichetta(rs.getString("FLAG_GESTIONE_ETICHETTA"));
          storicoUnitaArboreaVO.setTipoTipologiaVinoVO(tipoTipologiaVinoVO);
        }
        storicoUnitaArboreaVO.setAnnoRiferimento(rs.getString("ANNO_RIFERIMENTO"));
        storicoUnitaArboreaVO.setColturaSpecializzata(rs.getString("COLTURA_SPECIALIZZATA"));
        storicoUnitaArboreaVO.setStatoUnitaArborea(rs.getString("STATO_UNITA_ARBOREA"));
        storicoUnitaArboreaVO.setAnnoPrimaProduzione(rs.getString("ANNO_PRIMA_PRODUZIONE"));
        storicoUnitaArboreaVO.setDataPrimaProduzione(rs.getDate("DATA_PRIMA_PRODUZIONE"));
        storicoUnitaArboreaVO.setVigna(rs.getString("VIGNA"));
        storicoUnitaArboreaVO.setIdVigna(checkLongNull(rs.getString("ID_VIGNA")));
        storicoUnitaArboreaVO.setIdMenzioneGeografica(checkLongNull(rs.getString("ID_MENZIONE_GEOGRAFICA")));
        storicoUnitaArboreaVO.setEtichetta(rs.getString("ETICHETTA"));
        if(Validator.isNotEmpty(rs.getString("ID_GENERE_ISCRIZIONE"))) 
        {
          storicoUnitaArboreaVO.setIdGenereIscrizione(checkLong(rs.getString("ID_GENERE_ISCRIZIONE")));
          TipoGenereIscrizioneVO tipoGenereIscrizioneVO = new TipoGenereIscrizioneVO();
          tipoGenereIscrizioneVO.setIdGenereIscrizione(rs.getLong("ID_GENERE_ISCRIZIONE"));
          tipoGenereIscrizioneVO.setDescrizione(rs.getString("DESC_GENERE_ISCRIZIONE"));
          tipoGenereIscrizioneVO.setFlagDefinitiva(rs.getString("FLAG_DEFINITIVA"));
          storicoUnitaArboreaVO.setTipoGenereIscrizioneVO(tipoGenereIscrizioneVO);
        }
        
        storicoUnitaArboreaVO.setFlagImproduttiva(rs.getString("FLAG_IMPRODUTTIVA"));
        storicoUnitaArboreaVO.setPercentualeFallanza(rs.getBigDecimal("PERCENTUALE_FALLANZA"));
        
        //storicoUnitaArboreaVO.setTolleranza(checkInt(rs.getString("TOLLERANZA")));
        storicoUnitaArboreaVO.setDataConsolidamentoGis(rs.getTimestamp("DATA_CONSOLIDAMENTO_GIS"));
        
        storicoUnitaArboreaVO.setIdTipoInterventoViticolo(checkLongNull(rs.getString("ID_TIPO_INTERVENTO_VITICOLO")));
        storicoUnitaArboreaVO.setDataIntervento(rs.getTimestamp("DATA_INTERVENTO"));
        storicoUnitaArboreaVO.setDataSovrainnesto(rs.getTimestamp("DATA_SOVRAINNESTO"));
        
        storicoUnitaArboreaVO.setIdFiloSostegno(checkLongNull(rs.getString("ID_FILO_SOSTEGNO")));
        storicoUnitaArboreaVO.setIdPaloTestata(checkLongNull(rs.getString("ID_PALO_TESTATA")));
        storicoUnitaArboreaVO.setIdPaloTessitura(checkLongNull(rs.getString("ID_PALO_TESSITURA")));
        storicoUnitaArboreaVO.setIdAncoraggioUnar(checkLongNull(rs.getString("ID_ANCORAGGIO_UNAR")));
        storicoUnitaArboreaVO.setIdStatoColtivazioneUnar(checkLongNull(rs.getString("ID_STATO_COLTIVAZIONE_UNAR")));
        storicoUnitaArboreaVO.setDistanzaPali(checkLongNull(rs.getString("DISTANZA_PALI")));
        storicoUnitaArboreaVO.setAltitudineSlm(checkLongNull(rs.getString("ALTITUDINE_SLM")));
        storicoUnitaArboreaVO.setAreaServizio(rs.getBigDecimal("AREA_SERVIZIO"));
        
        storicoUnitaArboreaVO.setPercentualePendenzaMedia(rs.getBigDecimal("PERCENTUALE_PENDENZA_MEDIA"));
        storicoUnitaArboreaVO.setGradiPendenzaMedia(rs.getBigDecimal("GRADI_PENDENZA_MEDIA"));
        storicoUnitaArboreaVO.setGradiEsposizioneMedia(rs.getBigDecimal("GRADI_ESPOSIZIONE_MEDIA"));
        storicoUnitaArboreaVO.setMetriAltitudineMedia(checkIntegerNull(rs.getString("METRI_ALTITUDINE_MEDIA")));
        storicoUnitaArboreaVO.setIdUnitaArboreaMadre(checkLongNull(rs.getString("ID_UNITA_ARBOREA_MADRE")));
        storicoUnitaArboreaVO.setIdCatalogoMatrice(checkLongNull(rs.getString("ID_CATALOGO_MATRICE")));
        
        storicoParticellaVO.setStoricoUnitaArboreaVO(storicoUnitaArboreaVO);
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "findStoricoParticellaArboreaTolleranza in StoricoUnitaArboreaDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "findStoricoParticellaArboreaTolleranza in StoricoUnitaArboreaDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "findStoricoParticellaArboreaTolleranza in StoricoUnitaArboreaDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "findStoricoParticellaArboreaTolleranza in StoricoUnitaArboreaDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated findStoricoParticellaArboreaTolleranza method in StoricoUnitaArboreaDAO\n");
    return storicoParticellaVO;
  }
  
  
  
}
