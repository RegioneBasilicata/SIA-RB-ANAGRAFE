package it.csi.solmr.integration.anag;
	
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.dto.terreni.TipoDestinazioneVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoDettaglioUsoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoFaseAllevamentoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoPeriodoSeminaVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoPraticaMantenimentoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoQualitaUsoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoSeminaVO;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.anag.terreni.*;
import it.csi.solmr.exception.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.util.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class UtilizzoDichiaratoDAO extends it.csi.solmr.integration.BaseDAO {


	public UtilizzoDichiaratoDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public UtilizzoDichiaratoDAO(String refName) throws ResourceAccessException {
		super(refName);
	}
	
	/**
	 * Metodo che mi restituisce l'elenco degli utilizzi a partire dall'id_dichiarazione_consistenza
	 * e dell'id_conduzione_particella in modo da reperire solo quelli della particella selezionata
	 * 
	 * @param idDichiarazioneConsistenza
	 * @param idConduzioneParticella
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.UtilizzoDichiaratoVO[]
	 * @throws DataAccessException
	 */
	public UtilizzoDichiaratoVO[] getListUtilizzoDichiaratoVOByIdDichiarazioneConsistenzaAndIdConduzioneParticella(Long idDichiarazioneConsistenza, Long idConduzioneParticella, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListUtilizzoDichiaratoVOByIdDichiarazioneConsistenzaAndIdConduzioneParticella method in UtilizzoDichiaratoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<UtilizzoDichiaratoVO> elencoUtilizzoDichiaratoVO = new Vector<UtilizzoDichiaratoVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListUtilizzoDichiaratoVOByIdDichiarazioneConsistenzaAndIdConduzioneParticella method in UtilizzoDichiaratoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListUtilizzoDichiaratoVOByIdDichiarazioneConsistenzaAndIdConduzioneParticella method in UtilizzoDichiaratoDAO and it values: "+conn+"\n");

			String query = 
			  " SELECT UD.ID_UTILIZZO_DICHIARATO, " +
			  "        UD.ID_CATALOGO_MATRICE, " +
	      "        UD.ID_CATALOGO_MATRICE_SECONDARIO, " +
			  "        UD.CODICE_FOTOGRAFIA_TERRENI, " +
				"        UD.ID_CONDUZIONE_DICHIARATA, " +
				"        UD.ANNO, " +
				"        RCM.ID_UTILIZZO, " +
		    "        TU.CODICE AS COD_PRIMARIO, " +
		    "        TU.DESCRIZIONE AS DESC_PRIMARIO, " +
		    "        UD.SUPERFICIE_UTILIZZATA, " +
		    "        UD.NOTE, " +
		    "        UD.DATA_AGGIORNAMENTO, " +
		    "        UD.ID_UTENTE_AGGIORNAMENTO, " +
		    "        RCM2.ID_UTILIZZO AS ID_UTILIZZO_SECONDARIO, " +
		    "        TU2.CODICE AS COD_SECONDARIO, " +
		    "        TU2.DESCRIZIONE AS DESC_SECONDARIO, " +
		    "        UD.SUP_UTILIZZATA_SECONDARIA, " +
		    "        RCM.ID_VARIETA, " +
		    "        TV.DESCRIZIONE AS DESC_VAR_PRIMARIA, " +
		    "        TV.CODICE_VARIETA AS COD_VAR_PRIMARIA, " +
		    "        RCM2.ID_VARIETA AS ID_VARIETA_SECONDARIA, " +
		    "        TV2.DESCRIZIONE AS DESC_VAR_SECONDARIA, " +
		    "        TV2.CODICE_VARIETA AS COD_VAR_SECONDARIA, " +
		    "        UD.ANNO_IMPIANTO, " +
		    "        UD.ID_IMPIANTO, " +
		    "        TI.DESCRIZIONE AS DESC_IMPIANTO, " +
		    "        TI.DATA_INIZIO_VALIDITA, " +	
		    "        TI.DATA_FINE_VALIDITA, " +
		    "        UD.SESTO_SU_FILE, " +
		    "        UD.SESTO_TRA_FILE, " +
		    "        UD.NUMERO_PIANTE_CEPPI, " +
		    "        RCM.ID_TIPO_DETTAGLIO_USO, " +
        "        TDU.CODICE_DETTAGLIO_USO AS COD_DETT_USO_PRIM, " +
        "        TDU.DESCRIZIONE_DETTAGLIO_USO AS DESC_DETT_USO_PRIM, " +
        "        RCM2.ID_TIPO_DETTAGLIO_USO AS ID_TIPO_DETT_USO_SECONDARIO, " +
        "        TDU2.CODICE_DETTAGLIO_USO AS COD_DETT_USO_SEC, " +
        "        TDU2.DESCRIZIONE_DETTAGLIO_USO AS DESC_DETT_USO_SEC, " +
        "        RCM.ID_TIPO_DESTINAZIONE AS ID_TIPO_DEST_PRIM, " +
        "        TTD.CODICE_DESTINAZIONE AS COD_DEST_USO_PRIM, " +
        "        TTD.DESCRIZIONE_DESTINAZIONE AS DESC_DEST_USO_PRIM, " +
        "        RCM2.ID_TIPO_DESTINAZIONE AS ID_TIPO_DEST_SEC, " +
        "        TTD2.CODICE_DESTINAZIONE AS COD_DEST_USO_SEC, " +
        "        TTD2.DESCRIZIONE_DESTINAZIONE AS DESC_DEST_USO_SEC, " +
        "        RCM.ID_TIPO_QUALITA_USO AS ID_TIPO_QUALITA_USO_PRIM, " +
        "        TQU.CODICE_QUALITA_USO AS COD_QUALITA_USO_PRIM, " +
        "        TQU.DESCRIZIONE_QUALITA_USO AS DESC_QUALITA_USO_PRIM, " +
        "        RCM2.ID_TIPO_QUALITA_USO AS ID_TIPO_QUALITA_USO_SEC, " +
        "        TQU2.CODICE_QUALITA_USO AS COD_QUALITA_USO_SEC, " +
        "        TQU2.DESCRIZIONE_QUALITA_USO AS DESC_QUALITA_USO_SEC, " +
        "        UD.ID_TIPO_EFA, " +
        "        UD.VALORE_ORIGINALE, " +
        "        UD.VALORE_DOPO_CONVERSIONE, " +
        "        UD.VALORE_DOPO_PONDERAZIONE, " +
        "        UD.ID_TIPO_PERIODO_SEMINA, " +
        "        TPS.CODICE AS COD_PER_SEM_PRIM, " +
        "        TPS.DESCRIZIONE AS DESC_PER_SEM_PRIM, " +
        "        UD.ID_TIPO_PERIODO_SEMINA_SECOND, " +
        "        TPS2.CODICE AS COD_PER_SEM_SEC, " +
        "        TPS2.DESCRIZIONE AS DESC_PER_SEM_SEC, " +
        "        TEF.DICHIARABILE, " +
        "        TEF.DESCRIZIONE_TIPO_EFA, " +
        "        UM.DESCRIZIONE AS DESC_UNITA_MISURA, " +
        "        UD.ID_SEMINA, " +
        "        TSM.CODICE_SEMINA AS COD_SEM_PRIM, " +
        "        TSM.DESCRIZIONE_SEMINA AS DESC_SEM_PRIM, " +
        "        UD.ID_SEMINA_SECONDARIA, " +
        "        TSM2.CODICE_SEMINA AS COD_SEM_SEC, " +
        "        TSM2.DESCRIZIONE_SEMINA AS DESC_SEM_SEC, " +
        "        UD.DATA_INIZIO_DESTINAZIONE, " +
        "        UD.DATA_FINE_DESTINAZIONE, " +
        "        UD.ID_FASE_ALLEVAMENTO, " +
        "        TFA.CODICE_FASE_ALLEVAMENTO, " +
        "        TFA.DESCRIZIONE_FASE_ALLEVAMENTO, " +
        "        UD.ID_PRATICA_MANTENIMENTO, " +
        "        TPM.CODICE_PRATICA_MANTENIMENTO, " +
        "        TPM.DESCRIZIONE_PRATICA_MANTENIMEN, " +
        "        UD.DATA_INIZIO_DESTINAZIONE_SEC, " +
        "        UD.DATA_FINE_DESTINAZIONE_SEC " +
		    " FROM   DB_UTILIZZO_DICHIARATO UD, " +
		    "        DB_R_CATALOGO_MATRICE RCM, " +
        "        DB_R_CATALOGO_MATRICE RCM2, " +
		    "        DB_TIPO_UTILIZZO TU, " +
		    "        DB_TIPO_UTILIZZO TU2, " +
		    "        DB_TIPO_VARIETA TV, " +
		    "        DB_TIPO_VARIETA TV2, " +
		    "        DB_TIPO_IMPIANTO TI, " +
		    "        DB_DICHIARAZIONE_CONSISTENZA DC, " +
		    "        DB_CONDUZIONE_DICHIARATA CD, " +
	      "        DB_TIPO_DESTINAZIONE TTD, " +
	      "        DB_TIPO_DESTINAZIONE TTD2, " +
	      "        DB_TIPO_DETTAGLIO_USO TDU, " +
	      "        DB_TIPO_DETTAGLIO_USO TDU2, " +
	      "        DB_TIPO_QUALITA_USO TQU, " +
	      "        DB_TIPO_QUALITA_USO TQU2, " +
        "        DB_TIPO_PERIODO_SEMINA TPS, " +
        "        DB_TIPO_PERIODO_SEMINA TPS2, " +
        "        DB_TIPO_EFA TEF, " +
        "        DB_UNITA_MISURA UM, " +
        "        DB_TIPO_PRATICA_MANTENIMENTO TPM, " +
        "        DB_TIPO_FASE_ALLEVAMENTO TFA, " +
        "        DB_TIPO_SEMINA TSM, " +
        "        DB_TIPO_SEMINA TSM2 " +
		    " WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
		    " AND    CD.ID_CONDUZIONE_PARTICELLA = ? " +
		    " AND    CD.CODICE_FOTOGRAFIA_TERRENI = DC.CODICE_FOTOGRAFIA_TERRENI " +
		    " AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA " +
		    " AND    UD.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE (+) " +
        " AND    UD.ID_CATALOGO_MATRICE_SECONDARIO = RCM2.ID_CATALOGO_MATRICE (+) " +
		    " AND    RCM.ID_UTILIZZO = TU.ID_UTILIZZO " +
		    " AND    RCM2.ID_UTILIZZO = TU2.ID_UTILIZZO(+) " +
		    " AND    RCM.ID_VARIETA = TV.ID_VARIETA(+) " +
		    " AND    RCM2.ID_VARIETA = TV2.ID_VARIETA(+) " +
		    " AND    RCM.ID_TIPO_DESTINAZIONE = TTD.ID_TIPO_DESTINAZIONE(+) " +
        " AND    RCM2.ID_TIPO_DESTINAZIONE = TTD2.ID_TIPO_DESTINAZIONE(+) " +
        " AND    RCM.ID_TIPO_DETTAGLIO_USO = TDU.ID_TIPO_DETTAGLIO_USO(+) " +
        " AND    RCM2.ID_TIPO_DETTAGLIO_USO = TDU2.ID_TIPO_DETTAGLIO_USO(+) " +
        " AND    RCM.ID_TIPO_QUALITA_USO = TQU.ID_TIPO_QUALITA_USO(+) " +
        " AND    RCM2.ID_TIPO_QUALITA_USO = TQU2.ID_TIPO_QUALITA_USO(+) " +
		    " AND    UD.ID_IMPIANTO = TI.ID_IMPIANTO(+) " +
        " AND    UD.ID_TIPO_PERIODO_SEMINA = TPS.ID_TIPO_PERIODO_SEMINA(+) " +
        " AND    UD.ID_TIPO_PERIODO_SEMINA_SECOND = TPS2.ID_TIPO_PERIODO_SEMINA(+) " +
        " AND    UD.ID_TIPO_EFA = TEF.ID_TIPO_EFA(+) " +
        " AND    TEF.ID_UNITA_MISURA = UM.ID_UNITA_MISURA (+) " +
        " AND    UD.ID_PRATICA_MANTENIMENTO = TPM.ID_PRATICA_MANTENIMENTO (+) " +
        " AND    UD.ID_FASE_ALLEVAMENTO = TFA.ID_FASE_ALLEVAMENTO (+) "  +
        " AND    UD.ID_SEMINA = TSM.ID_SEMINA (+) " +
        " AND    UD.ID_SEMINA_SECONDARIA = TSM2.ID_SEMINA (+) ";
	 	
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
			}
			else {
				query += " ORDER BY TU.CODICE, " +
				         "          TU.DESCRIZIONE ";
			}

			SolmrLogger.debug(this, "Value of parameter 1 [ID_DICHIARAZIONE_CONSISTENZA] in getListUtilizzoDichiaratoVOByIdDichiarazioneConsistenzaAndIdConduzioneParticella method in UtilizzoDichiaratoDAO: "+idDichiarazioneConsistenza+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ID_CONDUZIONE_PARTICELLA] in getListUtilizzoDichiaratoVOByIdDichiarazioneConsistenzaAndIdConduzioneParticella method in UtilizzoDichiaratoDAO: "+idConduzioneParticella+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ORDER_BY] in getListUtilizzoDichiaratoVOByIdDichiarazioneConsistenzaAndIdConduzioneParticella method in UtilizzoDichiaratoDAO: "+orderBy+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idDichiarazioneConsistenza.longValue());
			stmt.setLong(2, idConduzioneParticella.longValue());

			SolmrLogger.debug(this, "Executing getListUtilizzoDichiaratoVOByIdDichiarazioneConsistenzaAndIdConduzioneParticella: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				UtilizzoDichiaratoVO utilizzoDichiaratoVO = new UtilizzoDichiaratoVO();
				utilizzoDichiaratoVO.setIdUtilizzoDichiarato(new Long(rs.getLong("ID_UTILIZZO_DICHIARATO")));
				utilizzoDichiaratoVO.setIdCatalogoMatrice(checkLongNull(rs.getString("ID_CATALOGO_MATRICE")));
				utilizzoDichiaratoVO.setIdCatalogoMatriceSecondario(checkLongNull(rs.getString("ID_CATALOGO_MATRICE_SECONDARIO")));
				utilizzoDichiaratoVO.setCodiceFotografiaTerreni(rs.getString("CODICE_FOTOGRAFIA_TERRENI"));
				utilizzoDichiaratoVO.setIdConduzioneDichiarata(new Long(rs.getLong("ID_CONDUZIONE_DICHIARATA")));
				utilizzoDichiaratoVO.setAnno(rs.getString("ANNO"));
				utilizzoDichiaratoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
				TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
				tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
				tipoUtilizzoVO.setCodice(rs.getString("COD_PRIMARIO"));
				tipoUtilizzoVO.setDescrizione(rs.getString("DESC_PRIMARIO"));
				utilizzoDichiaratoVO.setTipoUtilizzoVO(tipoUtilizzoVO);
				utilizzoDichiaratoVO.setSuperficieUtilizzata(rs.getString("SUPERFICIE_UTILIZZATA"));
				utilizzoDichiaratoVO.setNote(rs.getString("NOTE"));
				utilizzoDichiaratoVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
				utilizzoDichiaratoVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO_SECONDARIO"))) {
					utilizzoDichiaratoVO.setIdUtilizzoSecondario(new Long(rs.getLong("ID_UTILIZZO_SECONDARIO")));
					TipoUtilizzoVO tipoUtilizzoSecondarioVO = new TipoUtilizzoVO();
					tipoUtilizzoSecondarioVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO_SECONDARIO")));
					tipoUtilizzoSecondarioVO.setCodice(rs.getString("COD_SECONDARIO"));
					tipoUtilizzoSecondarioVO.setDescrizione(rs.getString("DESC_SECONDARIO"));
					utilizzoDichiaratoVO.setTipoUtilizzoSecondarioVO(tipoUtilizzoSecondarioVO);
				}
				utilizzoDichiaratoVO.setSupUtilizzataSecondaria(rs.getString("SUP_UTILIZZATA_SECONDARIA"));
				if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) {
					utilizzoDichiaratoVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
					TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
					tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
					tipoVarietaVO.setDescrizione(rs.getString("DESC_VAR_PRIMARIA"));
					tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VAR_PRIMARIA"));
					utilizzoDichiaratoVO.setTipoVarietaVO(tipoVarietaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_VARIETA_SECONDARIA"))) {
					utilizzoDichiaratoVO.setIdVarietaSecondaria(new Long(rs.getLong("ID_VARIETA_SECONDARIA")));
					TipoVarietaVO tipoVarietaSecondariaVO = new TipoVarietaVO();
					tipoVarietaSecondariaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA_SECONDARIA")));
					tipoVarietaSecondariaVO.setDescrizione(rs.getString("DESC_VAR_SECONDARIA"));
					tipoVarietaSecondariaVO.setCodiceVarieta(rs.getString("COD_VAR_SECONDARIA"));
					utilizzoDichiaratoVO.setTipoVarietaSecondariaVO(tipoVarietaSecondariaVO);
				}
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
				utilizzoDichiaratoVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
				if(Validator.isNotEmpty(rs.getString("ID_IMPIANTO"))) {
					utilizzoDichiaratoVO.setIdImpianto(new Long(rs.getLong("ID_IMPIANTO")));
					TipoImpiantoVO tipoImpiantoVO = new TipoImpiantoVO();
					tipoImpiantoVO.setIdImpianto(new Long(rs.getLong("ID_IMPIANTO")));
					tipoImpiantoVO.setDescrizione(rs.getString("DESC_IMPIANTO"));
					tipoImpiantoVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
					tipoImpiantoVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
					utilizzoDichiaratoVO.setTipoImpiantoVO(tipoImpiantoVO);
				}
				utilizzoDichiaratoVO.setSestoSuFile(rs.getString("SESTO_SU_FILE"));
				utilizzoDichiaratoVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
				utilizzoDichiaratoVO.setNumeroPianteCeppi(rs.getString("NUMERO_PIANTE_CEPPI"));
				
				
		
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
        utilizzoDichiaratoVO.setValoreOriginale(rs.getBigDecimal("VALORE_ORIGINALE"));
        utilizzoDichiaratoVO.setValoreDopoConversione(rs.getBigDecimal("VALORE_DOPO_CONVERSIONE"));
        utilizzoDichiaratoVO.setValoreDopoPonderazione(rs.getBigDecimal("VALORE_DOPO_PONDERAZIONE"));
        utilizzoDichiaratoVO.setDichiarabileEfa(rs.getString("DICHIARABILE"));
        utilizzoDichiaratoVO.setDescTipoEfaEfa(rs.getString("DESCRIZIONE_TIPO_EFA"));
        utilizzoDichiaratoVO.setDescUnitaMisuraEfa(rs.getString("DESC_UNITA_MISURA"));
        
        
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
				
				
				
				
				
				
				elencoUtilizzoDichiaratoVO.add(utilizzoDichiaratoVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListUtilizzoDichiaratoVOByIdDichiarazioneConsistenzaAndIdConduzioneParticella in UtilizzoDichiaratoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListUtilizzoDichiaratoVOByIdDichiarazioneConsistenzaAndIdConduzioneParticella in UtilizzoDichiaratoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListUtilizzoDichiaratoVOByIdDichiarazioneConsistenzaAndIdConduzioneParticella in UtilizzoDichiaratoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListUtilizzoDichiaratoVOByIdDichiarazioneConsistenzaAndIdConduzioneParticella in UtilizzoDichiaratoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListUtilizzoDichiaratoVOByIdDichiarazioneConsistenzaAndIdConduzioneParticella method in UtilizzoDichiaratoDAO\n");
		return elencoUtilizzoDichiaratoVO.size() == 0 ? null :(UtilizzoDichiaratoVO[])elencoUtilizzoDichiaratoVO.toArray(new UtilizzoDichiaratoVO[0]);
	}
	
	/**
	 * Metodo che mi restituisce l'elenco degli utilizzi consociati dichiarati a 
	 * partire dall'id_utilizzo_dichiarato
	 * 
	 * @param idUtilizzoParticella
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.UtilizzoConsociatoDichiaratoVO[]
	 * @throws DataAccessException
	 */
	public UtilizzoConsociatoDichiaratoVO[] getListUtilizziConsociatiDichiaratiByIdUtilizzoDichiarato(Long idUtilizzoDichiarato, String[] orderBy) 
	    throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating getListUtilizziConsociatiDichiaratiByIdUtilizzoDichiarato method in UtilizzoDichiaratoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<UtilizzoConsociatoDichiaratoVO> elencoUtilizziConsociatiDichiarati = new Vector<UtilizzoConsociatoDichiaratoVO>();

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in getListUtilizziConsociatiDichiaratiByIdUtilizzoDichiarato method in UtilizzoDichiaratoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListUtilizziConsociatiDichiaratiByIdUtilizzoDichiarato method in UtilizzoDichiaratoDAO and it values: "+conn+"\n");

			String query = " SELECT UCD.ID_UTILIZZO_CONSOCIATO_DICH, " +
						   "        UCD.ID_UTILIZZO_DICHIARATO, " +
						   "        UCD.ID_PIANTE_CONSOCIATE, " +
						   "        UCD.NUMERO_PIANTE " +
						   " FROM   DB_TIPO_PIANTE_CONSOCIATE TPC, " +
						   "        DB_UTILIZZO_CONSOCIATO_DICH UCD " +
						   " WHERE  UCD.ID_UTILIZZO_DICHIARATO = ? " +
						   " AND    UCD.ID_PIANTE_CONSOCIATE = TPC.ID_PIANTE_CONSOCIATE " +
						   " UNION ALL " +
						   " SELECT -1, " +
						   "        -1, " +
						   "        TPC.ID_PIANTE_CONSOCIATE, " + 
						   "        0 " +
						   " FROM   DB_TIPO_PIANTE_CONSOCIATE TPC " +
						   " WHERE  TPC.ID_PIANTE_CONSOCIATE NOT IN " +
						   " (SELECT ID_PIANTE_CONSOCIATE " +
						   "  FROM   DB_UTILIZZO_CONSOCIATO_DICH " +
						   "  WHERE  ID_UTILIZZO_DICHIARATO = ?) ";
		
			if(orderBy != null && orderBy.length > 0) 
			{
				String criterio = "";
				for(int i = 0; i < orderBy.length; i++) {
					if(i == 0) {
						criterio = (String)orderBy[i];
					}
					else {
						criterio += ", "+(String)orderBy[i];
					}
				}
			}
			else 
			{
				query += " ORDER BY ID_PIANTE_CONSOCIATE ";
			}

			SolmrLogger.debug(this, "Value of parameter 1 [ID_UTILIZZO_DICHIARATO] in getListUtilizziConsociatiDichiaratiByIdUtilizzoDichiarato method in UtilizzoDichiaratoDAO: "+idUtilizzoDichiarato+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ID_UTILIZZO_DICHIARATO] in getListUtilizziConsociatiDichiaratiByIdUtilizzoDichiarato method in UtilizzoDichiaratoDAO: "+idUtilizzoDichiarato+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ORDER_BY] in getListUtilizziConsociatiDichiaratiByIdUtilizzoDichiarato method in UtilizzoDichiaratoDAO: "+orderBy+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idUtilizzoDichiarato.longValue());
			stmt.setLong(2, idUtilizzoDichiarato.longValue());

			SolmrLogger.debug(this, "Executing getListUtilizziConsociatiDichiaratiByIdUtilizzoDichiarato: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				UtilizzoConsociatoDichiaratoVO utilizzoConsociatoDichiaratoVO = new UtilizzoConsociatoDichiaratoVO();
				utilizzoConsociatoDichiaratoVO.setIdUtilizzoConsociatoDich(new Long(rs.getLong("ID_UTILIZZO_CONSOCIATO_DICH")));
				utilizzoConsociatoDichiaratoVO.setIdUtilizzoDichiarato(new Long(rs.getLong("ID_UTILIZZO_DICHIARATO")));
				utilizzoConsociatoDichiaratoVO.setIdPianteConsociate(new Long(rs.getLong("ID_PIANTE_CONSOCIATE")));
				utilizzoConsociatoDichiaratoVO.setNumeroPiante(rs.getString("NUMERO_PIANTE"));
				elencoUtilizziConsociatiDichiarati.add(utilizzoConsociatoDichiaratoVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListUtilizziConsociatiDichiaratiByIdUtilizzoDichiarato in UtilizzoDichiaratoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListUtilizziConsociatiDichiaratiByIdUtilizzoDichiarato in UtilizzoDichiaratoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListUtilizziConsociatiDichiaratiByIdUtilizzoDichiarato in UtilizzoDichiaratoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListUtilizziConsociatiDichiaratiByIdUtilizzoDichiarato in UtilizzoDichiaratoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListUtilizziConsociatiDichiaratiByIdUtilizzoDichiarato method in UtilizzoDichiaratoDAO\n");
		return elencoUtilizziConsociatiDichiarati.size() == 0 ? null :(UtilizzoConsociatoDichiaratoVO[])elencoUtilizziConsociatiDichiarati.toArray(new UtilizzoConsociatoDichiaratoVO[0]);
	}
	
	/**
	 * Metodo che mi restituisce l'utilizzo dichiarato a partire dalla sua chiave primaria
	 * 
	 * @param idUtilizzoDichiarato
	 * @return it.csi.solmr.dto.anag.terreni.UtilizzoDichiaratoVO
	 * @throws DataAccessException
	 */
	public UtilizzoDichiaratoVO findUtilizzoDichiaratoByPrimaryKey(Long idUtilizzoDichiarato) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating findUtilizzoDichiaratoByPrimaryKey method in UtilizzoDichiaratoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		UtilizzoDichiaratoVO utilizzoDichiaratoVO = new UtilizzoDichiaratoVO();

		try {
			SolmrLogger.debug(this, "Creating db-connection in findUtilizzoDichiaratoByPrimaryKey method in UtilizzoDichiaratoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in findUtilizzoDichiaratoByPrimaryKey method in UtilizzoDichiaratoDAO and it values: "+conn+"\n");

			String query = " SELECT UD.ID_UTILIZZO_DICHIARATO, " +
						   "        UD.CODICE_FOTOGRAFIA_TERRENI, " +
						   "        UD.ID_CONDUZIONE_DICHIARATA, " +
						   "        UD.ANNO, " +
						   "        UD.ID_UTILIZZO, " +
						   "        TU.CODICE AS COD_PRIMARIO, " +
						   "        TU.DESCRIZIONE AS DESC_PRIMARIO, " +
						   "        UD.SUPERFICIE_UTILIZZATA, " +
						   "        UD.NOTE, " +
						   "        UD.DATA_AGGIORNAMENTO, " +
						   "        UD.ID_UTENTE_AGGIORNAMENTO, " +
						   "        UD.ID_UTILIZZO_SECONDARIO, " +
						   "        TU2.CODICE AS COD_SECONDARIO, " +
						   "        TU2.DESCRIZIONE AS DESC_SECONDARIO, " +
						   "        UD.SUP_UTILIZZATA_SECONDARIA, " +
						   "        UD.ID_VARIETA, " +
						   "        TV.DESCRIZIONE AS DESC_VAR_PRIMARIA, " +
						   "        TV.CODICE_VARIETA AS COD_VAR_PRIMARIA, " +
						   "        UD.ID_VARIETA_SECONDARIA, " +
						   "        TV2.DESCRIZIONE AS DESC_VAR_SECONDARIA, " +
						   "        TV2.CODICE_VARIETA AS COD_VAR_SECONDARIA, " +
						   "        UD.ANNO_IMPIANTO, " +
						   "        UD.ID_IMPIANTO, " +
						   "        TI.DESCRIZIONE AS DESC_IMPIANTO, " +
						   "        TI.DATA_INIZIO_VALIDITA, " +	
						   "        TI.DATA_FINE_VALIDITA, " +
						   "        UD.SESTO_SU_FILE, " +
						   "        UD.SESTO_TRA_FILE, " +
						   "        UD.NUMERO_PIANTE_CEPPI " +
						   " FROM   DB_UTILIZZO_DICHIARATO UD, " +
						   "        DB_TIPO_UTILIZZO TU, " +
						   "        DB_TIPO_UTILIZZO TU2, " +
						   "        DB_TIPO_VARIETA TV, " +
						   "        DB_TIPO_VARIETA TV2, " +
						   "        DB_TIPO_IMPIANTO TI " +
						   " WHERE  UD.ID_UTILIZZO_DICHIARATO = ? " +
						   " AND    UD.ID_UTILIZZO = TU.ID_UTILIZZO " +
						   " AND    UD.ID_UTILIZZO_SECONDARIO = TU2.ID_UTILIZZO(+) " +
						   " AND    UD.ID_VARIETA = TV.ID_VARIETA(+) " +
						   " AND    UD.ID_VARIETA_SECONDARIA = TV2.ID_VARIETA(+) " +
						   " AND    UD.ID_IMPIANTO = TI.ID_IMPIANTO(+) "; 

			SolmrLogger.debug(this, "Value of parameter 1 [ID_UTILIZZO_DICHIARATO] in findUtilizzoDichiaratoByPrimaryKey method in UtilizzoDichiaratoDAO: "+idUtilizzoDichiarato+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idUtilizzoDichiarato.longValue());

			SolmrLogger.debug(this, "Executing findUtilizzoDichiaratoByPrimaryKey: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				utilizzoDichiaratoVO = new UtilizzoDichiaratoVO();
				utilizzoDichiaratoVO.setIdUtilizzoDichiarato(new Long(rs.getLong("ID_UTILIZZO_DICHIARATO")));
				utilizzoDichiaratoVO.setCodiceFotografiaTerreni(rs.getString("CODICE_FOTOGRAFIA_TERRENI"));
				utilizzoDichiaratoVO.setIdConduzioneDichiarata(new Long(rs.getLong("ID_CONDUZIONE_DICHIARATA")));
				utilizzoDichiaratoVO.setAnno(rs.getString("ANNO"));
				utilizzoDichiaratoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
				TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
				tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
				tipoUtilizzoVO.setCodice(rs.getString("COD_PRIMARIO"));
				tipoUtilizzoVO.setDescrizione(rs.getString("DESC_PRIMARIO"));
				utilizzoDichiaratoVO.setTipoUtilizzoVO(tipoUtilizzoVO);
				utilizzoDichiaratoVO.setSuperficieUtilizzata(rs.getString("SUPERFICIE_UTILIZZATA"));
				utilizzoDichiaratoVO.setNote(rs.getString("NOTE"));
				utilizzoDichiaratoVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
				utilizzoDichiaratoVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO_SECONDARIO"))) {
					utilizzoDichiaratoVO.setIdUtilizzoSecondario(new Long(rs.getLong("ID_UTILIZZO_SECONDARIO")));
					TipoUtilizzoVO tipoUtilizzoSecondarioVO = new TipoUtilizzoVO();
					tipoUtilizzoSecondarioVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO_SECONDARIO")));
					tipoUtilizzoSecondarioVO.setCodice(rs.getString("COD_SECONDARIO"));
					tipoUtilizzoSecondarioVO.setDescrizione(rs.getString("DESC_SECONDARIO"));
					utilizzoDichiaratoVO.setTipoUtilizzoSecondarioVO(tipoUtilizzoSecondarioVO);
				}
				utilizzoDichiaratoVO.setSupUtilizzataSecondaria(rs.getString("SUP_UTILIZZATA_SECONDARIA"));
				if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) {
					utilizzoDichiaratoVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
					TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
					tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
					tipoVarietaVO.setDescrizione(rs.getString("DESC_VAR_PRIMARIA"));
					tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VAR_PRIMARIA"));
					utilizzoDichiaratoVO.setTipoVarietaVO(tipoVarietaVO);
				}
				if(Validator.isNotEmpty(rs.getString("ID_VARIETA_SECONDARIA"))) {
					utilizzoDichiaratoVO.setIdVarietaSecondaria(new Long(rs.getLong("ID_VARIETA_SECONDARIA")));
					TipoVarietaVO tipoVarietaSecondariaVO = new TipoVarietaVO();
					tipoVarietaSecondariaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA_SECONDARIA")));
					tipoVarietaSecondariaVO.setDescrizione(rs.getString("DESC_VAR_SECONDARIA"));
					tipoVarietaSecondariaVO.setCodiceVarieta(rs.getString("COD_VAR_SECONDARIA"));
					utilizzoDichiaratoVO.setTipoVarietaSecondariaVO(tipoVarietaSecondariaVO);
				}
				utilizzoDichiaratoVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
				if(Validator.isNotEmpty(rs.getString("ID_IMPIANTO"))) {
					utilizzoDichiaratoVO.setIdImpianto(new Long(rs.getLong("ID_IMPIANTO")));
					TipoImpiantoVO tipoImpiantoVO = new TipoImpiantoVO();
					tipoImpiantoVO.setIdImpianto(new Long(rs.getLong("ID_IMPIANTO")));
					tipoImpiantoVO.setDescrizione(rs.getString("DESC_IMPIANTO"));
					tipoImpiantoVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
					tipoImpiantoVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
					utilizzoDichiaratoVO.setTipoImpiantoVO(tipoImpiantoVO);
				}
				utilizzoDichiaratoVO.setSestoSuFile(rs.getString("SESTO_SU_FILE"));
				utilizzoDichiaratoVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
				utilizzoDichiaratoVO.setNumeroPianteCeppi(rs.getString("NUMERO_PIANTE_CEPPI"));
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "findUtilizzoDichiaratoByPrimaryKey in UtilizzoDichiaratoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "findUtilizzoDichiaratoByPrimaryKey in UtilizzoDichiaratoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "findUtilizzoDichiaratoByPrimaryKey in UtilizzoDichiaratoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "findUtilizzoDichiaratoByPrimaryKey in UtilizzoDichiaratoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated findUtilizzoDichiaratoByPrimaryKey method in UtilizzoDichiaratoDAO\n");
		return utilizzoDichiaratoVO;
	}
	
	/**
	 * Metodo utilizzato per effettuare il riepilogo delle particelle relative
	 * ad un'azienda alla dichiarazione di consistenza per uso primario
	 * 
	 * @param idAzienda
	 * @param escludiAsservimento
	 * @param idDichiarazioneConsistenza
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.UtilizzoDichiaratoVO[]
	 * @throws DataAccessException
	 */
	public UtilizzoDichiaratoVO[] riepilogoUsoPrimarioDichiarato(Long idAzienda, Long idDichiarazioneConsistenza) throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating riepilogoUsoPrimarioDichiarato method in UtilizzoDichiaratoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<UtilizzoDichiaratoVO> elencoUtilizziDichiarati = new Vector<UtilizzoDichiaratoVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in riepilogoUsoPrimarioDichiarato method in UtilizzoDichiaratoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in riepilogoUsoPrimarioDichiarato method in UtilizzoDichiaratoDAO and it values: "+conn+"\n");

			String query = "" +
				"SELECT   RCM.ID_UTILIZZO, " +
				"         TU.DESCRIZIONE, " + 
        "         RCM.FLAG_SAU, " + 
        "         SUM(UD.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " + 
        "FROM     DB_TIPO_UTILIZZO TU, " + 
        "         DB_UTILIZZO_DICHIARATO UD, " + 
        "         DB_DICHIARAZIONE_CONSISTENZA DC, " + 
        "         DB_CONDUZIONE_DICHIARATA CD," +
        "         DB_R_CATALOGO_MATRICE RCM " +  
        "WHERE    DC.ID_AZIENDA = ? " + 
        "AND      DC.ID_DICHIARAZIONE_CONSISTENZA = ? " + 
        "AND      DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " + 
        "AND      CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA " + 
        "AND      UD.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +
        "AND      RCM.ID_UTILIZZO = TU.ID_UTILIZZO " +
			  "GROUP BY RCM.ID_UTILIZZO, " + 
				"         TU.DESCRIZIONE, " +
				"         RCM.FLAG_SAU " +
				"ORDER BY RCM.FLAG_SAU DESC, " +
        "         TU.DESCRIZIONE ASC ";

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoUsoPrimarioDichiarato method in UtilizzoDichiaratoDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ID_DICHIARAZIONE_CONSISTENZA] in riepilogoUsoPrimarioDichiarato method in UtilizzoDichiaratoDAO: "+idDichiarazioneConsistenza+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());
			stmt.setLong(2, idDichiarazioneConsistenza.longValue());

			SolmrLogger.debug(this, "Executing riepilogoUsoPrimarioDichiarato: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				UtilizzoDichiaratoVO utilizzoDichiaratoVO = new UtilizzoDichiaratoVO();
				utilizzoDichiaratoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
				TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
				tipoUtilizzoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
				tipoUtilizzoVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoUtilizzoVO.setFlagSau(rs.getString("FLAG_SAU"));
				utilizzoDichiaratoVO.setTipoUtilizzoVO(tipoUtilizzoVO);
				utilizzoDichiaratoVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
				elencoUtilizziDichiarati.add(utilizzoDichiaratoVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "riepilogoUsoPrimarioDichiarato in UtilizzoDichiaratoDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "riepilogoUsoPrimarioDichiarato in UtilizzoDichiaratoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "riepilogoUsoPrimarioDichiarato in UtilizzoDichiaratoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "riepilogoUsoPrimarioDichiarato in UtilizzoDichiaratoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated riepilogoUsoPrimarioDichiarato method in UtilizzoDichiaratoDAO\n");
		if(elencoUtilizziDichiarati.size() == 0) {
			return (UtilizzoDichiaratoVO[])elencoUtilizziDichiarati.toArray(new UtilizzoDichiaratoVO[0]);
		}
		else {
			return (UtilizzoDichiaratoVO[])elencoUtilizziDichiarati.toArray(new UtilizzoDichiaratoVO[elencoUtilizziDichiarati.size()]);
		}
	}
	
	/**
	 * Metodo utilizzato per effettuare il riepilogo delle particelle relative
	 * ad un'azienda alla dichiarazione di consistenza per uso primario
	 * 
	 * @param idAzienda
	 * @param escludiAsservimento
	 * @param idDichiarazioneConsistenza
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.UtilizzoDichiaratoVO[]
	 * @throws DataAccessException
	 */
	public UtilizzoDichiaratoVO[] riepilogoUsoSecondarioDichiarato(Long idAzienda, Long idDichiarazioneConsistenza) 
	  throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating riepilogoUsoSecondarioDichiarato method in UtilizzoDichiaratoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<UtilizzoDichiaratoVO> elencoUtilizziDichiarati = new Vector<UtilizzoDichiaratoVO>();

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in riepilogoUsoSecondarioDichiarato method in UtilizzoDichiaratoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in riepilogoUsoSecondarioDichiarato method in UtilizzoDichiaratoDAO and it values: "+conn+"\n");

			String query = "" +
			    "SELECT   RCM.ID_UTILIZZO, " +
	        "         TU.DESCRIZIONE, " + 
	        "         RCM.FLAG_SAU, " + 
	        "         SUM(UD.SUP_UTILIZZATA_SECONDARIA) AS SUP_UTILIZZATA_SEC " + 
	        "FROM     DB_TIPO_UTILIZZO TU, " + 
	        "         DB_UTILIZZO_DICHIARATO UD, " + 
	        "         DB_DICHIARAZIONE_CONSISTENZA DC, " + 
	        "         DB_CONDUZIONE_DICHIARATA CD, " +
	        "         DB_R_CATALOGO_MATRICE RCM " +  
	        "WHERE    DC.ID_AZIENDA = ? " + 
	        "AND      DC.ID_DICHIARAZIONE_CONSISTENZA = ? " + 
	        "AND      DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " + 
	        "AND      CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA " + 
	        "AND      UD.ID_CATALOGO_MATRICE_SECONDARIO = RCM.ID_CATALOGO_MATRICE " +
	        "AND      RCM.ID_UTILIZZO = TU.ID_UTILIZZO " +
	        "GROUP BY RCM.ID_UTILIZZO, " + 
	        "         TU.DESCRIZIONE, " +
	        "         RCM.FLAG_SAU " +
	        "ORDER BY RCM.FLAG_SAU DESC, " +
	        "         TU.DESCRIZIONE ASC";


			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoUsoSecondarioDichiarato method in UtilizzoDichiaratoDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ID_DICHIARAZIONE_CONSISTENZA] in riepilogoUsoSecondarioDichiarato method in UtilizzoDichiaratoDAO: "+idDichiarazioneConsistenza+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());
			stmt.setLong(2, idDichiarazioneConsistenza.longValue());

			SolmrLogger.debug(this, "Executing riepilogoUsoSecondarioDichiarato: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				UtilizzoDichiaratoVO utilizzoDichiaratoVO = new UtilizzoDichiaratoVO();
				utilizzoDichiaratoVO.setIdUtilizzoSecondario(new Long(rs.getLong("ID_UTILIZZO")));
				TipoUtilizzoVO tipoUtilizzoSecondarioVO = new TipoUtilizzoVO();
				tipoUtilizzoSecondarioVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
				tipoUtilizzoSecondarioVO.setDescrizione(rs.getString("DESCRIZIONE"));
				tipoUtilizzoSecondarioVO.setFlagSau(rs.getString("FLAG_SAU"));
				utilizzoDichiaratoVO.setTipoUtilizzoSecondarioVO(tipoUtilizzoSecondarioVO);
				utilizzoDichiaratoVO.setSupUtilizzataSecondaria(rs.getString("SUP_UTILIZZATA_SEC"));
				elencoUtilizziDichiarati.add(utilizzoDichiaratoVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "riepilogoUsoSecondarioDichiarato in UtilizzoDichiaratoDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "riepilogoUsoSecondarioDichiarato in UtilizzoDichiaratoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "riepilogoUsoSecondarioDichiarato in UtilizzoDichiaratoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "riepilogoUsoSecondarioDichiarato in UtilizzoDichiaratoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated riepilogoUsoSecondarioDichiarato method in UtilizzoDichiaratoDAO\n");
		if(elencoUtilizziDichiarati.size() == 0) {
			return (UtilizzoDichiaratoVO[])elencoUtilizziDichiarati.toArray(new UtilizzoDichiaratoVO[0]);
		}
		else {
			return (UtilizzoDichiaratoVO[])elencoUtilizziDichiarati.toArray(new UtilizzoDichiaratoVO[elencoUtilizziDichiarati.size()]);
		}
	}
	
	/**
	 * Metodo utilizzato per effettuare il riepilogo delle particelle relative
	 * ad un'azienda alla dichiarazione di consistenza per macro uso
	 * 
	 * @param idAzienda
	 * @param escludiAsservimento
	 * @param idDichiarazioneConsistenza
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.UtilizzoDichiaratoVO[]
	 * @throws DataAccessException
	 */
	public UtilizzoDichiaratoVO[] riepilogoMacroUsoDichiarato(Long idAzienda, Long idDichiarazioneConsistenza) 
	  throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating riepilogoMacroUsoDichiarato method in UtilizzoDichiaratoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<UtilizzoDichiaratoVO> elencoUtilizziDichiarati = new Vector<UtilizzoDichiaratoVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in riepilogoMacroUsoDichiarato method in UtilizzoDichiaratoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in riepilogoMacroUsoDichiarato method in UtilizzoDichiaratoDAO and it values: "+conn+"\n");

			String query = "" +
				"SELECT   TMU.ID_MACRO_USO, " +
			  "         TMU.CODICE, " + 
			  "         TMU.DESCRIZIONE, " +
			  "         SUM (UD.SUPERFICIE_UTILIZZATA) AS SUP_UTILIZZATA " +
			  "FROM     DB_CONDUZIONE_DICHIARATA CD, " +
			  "         DB_UTILIZZO_DICHIARATO UD, " +
			  "         DB_DICHIARAZIONE_CONSISTENZA DC, " +
			  "         DB_STORICO_PARTICELLA SP, " +
			  "         DB_TIPO_MACRO_USO TMU, " +
			  "         DB_TIPO_MACRO_USO_VARIETA TMUV " +
			  "WHERE    DC.ID_AZIENDA = ? " +
			  "AND      DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
			  "AND      DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
			  "AND      CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
			  "AND      CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA " +	
			  "AND      UD.ID_CATALOGO_MATRICE = TMUV.ID_CATALOGO_MATRICE " +
			  "AND      TMUV.ID_MACRO_USO = TMU.ID_MACRO_USO " +
			  "AND      TMUV.DATA_INIZIO_VALIDITA <= TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
			  "AND      NVL(TMUV.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > TRUNC(DC.DATA_INSERIMENTO_DICHIARAZIONE) " +
			  "GROUP BY TMU.ID_MACRO_USO, " +
				"         TMU.CODICE, " + 
				"         TMU.DESCRIZIONE " +
				"ORDER BY TMU.CODICE ASC, " + 
        "         TMU.DESCRIZIONE ASC ";

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in riepilogoMacroUsoDichiarato method in UtilizzoDichiaratoDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ID_DICHIARAZIONE_CONSISTENZA] in riepilogoMacroUsoDichiarato method in UtilizzoDichiaratoDAO: "+idDichiarazioneConsistenza+"\n");

			stmt = conn.prepareStatement(query);
			
			stmt.setLong(1, idAzienda.longValue());
			stmt.setLong(2, idDichiarazioneConsistenza.longValue());

			SolmrLogger.debug(this, "Executing riepilogoMacroUsoDichiarato: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				UtilizzoDichiaratoVO utilizzoDichiaratoVO = new UtilizzoDichiaratoVO();
				TipoMacroUsoVO tipoMacroUsoVO = new TipoMacroUsoVO();
				tipoMacroUsoVO.setIdMacroUso(new Long(rs.getLong("ID_MACRO_USO")));
				tipoMacroUsoVO.setCodice(rs.getString("CODICE"));
				tipoMacroUsoVO.setDescrizione(rs.getString("DESCRIZIONE"));
				utilizzoDichiaratoVO.setTipoMacroUsoVO(tipoMacroUsoVO);
				utilizzoDichiaratoVO.setSuperficieUtilizzata(rs.getString("SUP_UTILIZZATA"));
				elencoUtilizziDichiarati.add(utilizzoDichiaratoVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "riepilogoMacroUsoDichiarato in UtilizzoDichiaratoDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "riepilogoMacroUsoDichiarato in UtilizzoDichiaratoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "riepilogoMacroUsoDichiarato in UtilizzoDichiaratoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "riepilogoMacroUsoDichiarato in UtilizzoDichiaratoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated riepilogoMacroUsoDichiarato method in UtilizzoDichiaratoDAO\n");
		if(elencoUtilizziDichiarati.size() == 0) {
			return (UtilizzoDichiaratoVO[])elencoUtilizziDichiarati.toArray(new UtilizzoDichiaratoVO[0]);
		}
		else {
			return (UtilizzoDichiaratoVO[])elencoUtilizziDichiarati.toArray(new UtilizzoDichiaratoVO[elencoUtilizziDichiarati.size()]);
		}
	}
	
	
	public double getTotSupUtilizzataByIdConduzioneDichiarata(Long idConduzioneDichiarata) 
	  throws DataAccessException 
	{
    SolmrLogger.debug(this, "Invocating getTotSupUtilizzataByIdConduzioneDichiarata method in UtilizzoDichiaratoDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    double totSupUtilizzata = 0;

    try {
      SolmrLogger.debug(this, "Creating db-connection in getTotSupUtilizzataByIdConduzioneDichiarata method in UtilizzoDichiaratoDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getTotSupUtilizzataByIdConduzioneDichiarata method in UtilizzoDichiaratoDAO and it values: "+conn+"\n");

      String query = " SELECT SUM(SUPERFICIE_UTILIZZATA) AS TOT_SUP" +
                         " FROM   DB_UTILIZZO_DICHIARATO " +
                         " WHERE  ID_CONDUZIONE_DICHIARATA = ? ";

      SolmrLogger.debug(this, "Value of parameter 1 [ID_CONDUZIONE_DICHIARATA] in getTotSupUtilizzataByIdConduzioneDichiarata method in UtilizzoDichiaratoDAO: "+idConduzioneDichiarata+"\n");

      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idConduzioneDichiarata.longValue());

      SolmrLogger.debug(this, "Executing getTotSupUtilizzataByIdConduzioneDichiarata: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      if(rs.next()) {
        totSupUtilizzata = rs.getDouble("TOT_SUP");
      }
      
      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getTotSupUtilizzataByIdConduzioneDichiarata in UtilizzoDichiaratoDAO - SQLException: "+exc.getMessage()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getTotSupUtilizzataByIdConduzioneDichiarata in UtilizzoDichiaratoDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getTotSupUtilizzataByIdConduzioneDichiarata in UtilizzoDichiaratoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getTotSupUtilizzataByIdConduzioneDichiarata in UtilizzoDichiaratoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getTotSupUtilizzataByIdConduzioneDichiarata method in UtilizzoDichiaratoDAO\n");
    return totSupUtilizzata;
  }
	
	
	/**
	 * 
	 * restituisce tutta la superficie condotta dell'azienda in asservimento
	 * alla dichiarazione di consistenza
	 * 
	 * 
	 * @param idAzienda
	 * @param idDichiarazioneConsistenza
	 * @return
	 * @throws DataAccessException
	 */
	public BigDecimal getTotSupAsservimento(Long idAzienda, Long idDichiarazioneConsistenza) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating getTotSupAsservimento method in UtilizzoDichiaratoDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    BigDecimal result = null;
  
    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in getTotSupAsservimento method in UtilizzoDichiaratoDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getTotSupAsservimento method in UtilizzoDichiaratoDAO and it values: "+conn+"\n");
  
      String query = "" +
        "SELECT   CD.CODICE_FOTOGRAFIA_TERRENI, " +
        "         SUM(CD.SUPERFICIE_CONDOTTA) AS SUP_CONDOTTA " +
        "FROM     DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "         DB_CONDUZIONE_DICHIARATA CD " +
        "WHERE    DC.ID_AZIENDA = ? " +
        "AND      DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +  
        "AND      DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        "AND      CD.ID_TITOLO_POSSESSO = 5 " +
        "GROUP BY CD.CODICE_FOTOGRAFIA_TERRENI";
      
      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getTotSupAsservimento method in UtilizzoDichiaratoDAO: "+idAzienda+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [ID_DICHIARAZIONE_CONSISTENZA] in getTotSupAsservimento method in UtilizzoDichiaratoDAO: "+idDichiarazioneConsistenza+"\n");
      
  
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idAzienda.longValue());
      stmt.setLong(2, idDichiarazioneConsistenza.longValue());
  
      SolmrLogger.debug(this, "Executing getTotSupAsservimento: "+query+"\n");
  
      ResultSet rs = stmt.executeQuery();
  
      if(rs.next()) 
      {
        result = rs.getBigDecimal("SUP_CONDOTTA");
      }
      
      rs.close();
      stmt.close();
  
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getTotSupAsservimento in UtilizzoDichiaratoDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getTotSupAsservimento in UtilizzoDichiaratoDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getTotSupAsservimento in UtilizzoDichiaratoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getTotSupAsservimento in UtilizzoDichiaratoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getTotSupAsservimento method in UtilizzoDichiaratoDAO\n");
    
    return result;
  }
	
	
	
	public Vector<UtilizzoDichiaratoVO> getListUtilizzoDichiaratoByIdConduzioneDichiarata(long idConduzioneDichiarata) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<UtilizzoDichiaratoVO> result = null;
    UtilizzoDichiaratoVO utilizzoDichiaratoVO = null;
    
    try
    {
      SolmrLogger.debug(this,
          "[UtilizzoDichiaratoDAO::getListUtilizzoDichiaratoByIdConduzioneDichiarata] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT UD.ID_UTILIZZO_DICHIARATO, " +
        "       UD.CODICE_FOTOGRAFIA_TERRENI, " +
        "       UD.ID_CONDUZIONE_DICHIARATA, " +
        "       UD.ANNO, " +
        "       UD.ID_UTILIZZO, " +
        "       UD.SUPERFICIE_UTILIZZATA, " +
        "       UD.NOTE, " +
        "       UD.DATA_AGGIORNAMENTO, " +
        "       UD.ID_UTENTE_AGGIORNAMENTO, " +
        "       UD.ID_UTILIZZO_SECONDARIO, " +
        "       UD.SUP_UTILIZZATA_SECONDARIA, " +
        "       UD.ID_VARIETA, " +
        "       UD.ID_VARIETA_SECONDARIA, " +
        "       UD.ANNO_IMPIANTO, " +
        "       UD.ID_IMPIANTO, " +
        "       UD.SESTO_SU_FILE, " +
        "       UD.SESTO_TRA_FILE, " +
        "       UD.NUMERO_PIANTE_CEPPI," +
        "       UD.ID_TIPO_DETTAGLIO_USO, " +
        "       UD.ID_TIPO_DETT_USO_SECONDARIO, " +
        "       UD.ID_TIPO_EFA, " +
        "       UD.VALORE_ORIGINALE, " +
        "       UD.VALORE_DOPO_CONVERSIONE, " +
        "       UD.VALORE_DOPO_PONDERAZIONE," +
        "       UD.ID_TIPO_PERIODO_SEMINA," +
        "       UD.ID_TIPO_PERIODO_SEMINA_SECOND," +
        "       UD.ID_CATALOGO_MATRICE, " +
        "       UD.ID_CATALOGO_MATRICE_SECONDARIO, " +
        "       UD.ID_SEMINA," +
        "       UD.ID_SEMINA_SECONDARIA," +
        "       UD.DATA_INIZIO_DESTINAZIONE, " +
        "       UD.DATA_FINE_DESTINAZIONE, " +
        "       UD.ID_FASE_ALLEVAMENTO, " +
        "       UD.ID_PRATICA_MANTENIMENTO, " +
        "       UD.DATA_INIZIO_DESTINAZIONE_SEC, " +
        "       UD.DATA_FINE_DESTINAZIONE_SEC " +
        "FROM   DB_UTILIZZO_DICHIARATO UD " +
        "WHERE  UD.ID_CONDUZIONE_DICHIARATA = ? ");
      
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[UtilizzoDichiaratoDAO::getListUtilizzoDichiaratoByIdConduzioneDichiarata] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      // Setto i parametri della query
      int idx = 0;
      
      stmt.setLong(++idx, idConduzioneDichiarata);
      
      
      
      ResultSet rs = stmt.executeQuery();
      while(rs.next()) 
      {
        if(result == null)
        {
          result = new Vector<UtilizzoDichiaratoVO>();
        }
        
        utilizzoDichiaratoVO = new UtilizzoDichiaratoVO();
        utilizzoDichiaratoVO.setIdUtilizzoDichiarato(new Long(rs.getLong("ID_UTILIZZO_DICHIARATO")));
        utilizzoDichiaratoVO.setCodiceFotografiaTerreni(rs.getString("CODICE_FOTOGRAFIA_TERRENI"));
        utilizzoDichiaratoVO.setIdConduzioneDichiarata(new Long(rs.getLong("ID_CONDUZIONE_DICHIARATA")));
        utilizzoDichiaratoVO.setAnno(rs.getString("ANNO"));
        utilizzoDichiaratoVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
        utilizzoDichiaratoVO.setSuperficieUtilizzata(rs.getString("SUPERFICIE_UTILIZZATA"));
        utilizzoDichiaratoVO.setNote(rs.getString("NOTE"));
        utilizzoDichiaratoVO.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO"));
        utilizzoDichiaratoVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        utilizzoDichiaratoVO.setIdUtilizzoSecondario(checkLongNull(rs.getString("ID_UTILIZZO_SECONDARIO")));
        utilizzoDichiaratoVO.setSupUtilizzataSecondaria(rs.getString("SUP_UTILIZZATA_SECONDARIA"));
        utilizzoDichiaratoVO.setIdVarieta(checkLongNull(rs.getString("ID_VARIETA")));
        utilizzoDichiaratoVO.setIdVarietaSecondaria(checkLongNull(rs.getString("ID_VARIETA_SECONDARIA")));
        utilizzoDichiaratoVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
        utilizzoDichiaratoVO.setIdImpianto(checkLongNull(rs.getString("ID_IMPIANTO")));
        utilizzoDichiaratoVO.setSestoSuFile(rs.getString("SESTO_SU_FILE"));
        utilizzoDichiaratoVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
        utilizzoDichiaratoVO.setNumeroPianteCeppi(rs.getString("NUMERO_PIANTE_CEPPI"));
        utilizzoDichiaratoVO.setIdTipoDettaglioUso(checkLongNull(rs.getString("ID_TIPO_DETTAGLIO_USO")));
        utilizzoDichiaratoVO.setIdTipoDettaglioUsoSecondario(checkLongNull(rs.getString("ID_TIPO_DETT_USO_SECONDARIO")));
        utilizzoDichiaratoVO.setIdTipoEfa(checkLongNull(rs.getString("ID_TIPO_EFA")));
        utilizzoDichiaratoVO.setValoreOriginale(rs.getBigDecimal("VALORE_ORIGINALE"));
        utilizzoDichiaratoVO.setValoreDopoConversione(rs.getBigDecimal("VALORE_DOPO_CONVERSIONE"));
        utilizzoDichiaratoVO.setValoreDopoPonderazione(rs.getBigDecimal("VALORE_DOPO_PONDERAZIONE"));
        utilizzoDichiaratoVO.setIdTipoPeriodoSemina(checkLongNull(rs.getString("ID_TIPO_PERIODO_SEMINA")));
        utilizzoDichiaratoVO.setIdTipoPeriodoSeminaSecondario(checkLongNull(rs.getString("ID_TIPO_PERIODO_SEMINA_SECOND")));
        utilizzoDichiaratoVO.setIdCatalogoMatrice(checkLongNull(rs.getString("ID_CATALOGO_MATRICE")));
        utilizzoDichiaratoVO.setIdCatalogoMatriceSecondario(checkLongNull(rs.getString("ID_CATALOGO_MATRICE_SECONDARIO")));
        utilizzoDichiaratoVO.setIdSemina(checkLongNull(rs.getString("ID_SEMINA")));
        utilizzoDichiaratoVO.setIdSeminaSecondario(checkLongNull(rs.getString("ID_SEMINA_SECONDARIA")));
        utilizzoDichiaratoVO.setDataInizioDestinazione(rs.getTimestamp("DATA_INIZIO_DESTINAZIONE"));
        utilizzoDichiaratoVO.setDataFineDestinazione(rs.getTimestamp("DATA_FINE_DESTINAZIONE"));
        utilizzoDichiaratoVO.setIdFaseAllevamento(checkLongNull(rs.getString("ID_FASE_ALLEVAMENTO")));
        utilizzoDichiaratoVO.setIdPraticaMantenimento(checkLongNull(rs.getString("ID_PRATICA_MANTENIMENTO")));
        utilizzoDichiaratoVO.setDataInizioDestinazioneSec(rs.getTimestamp("DATA_INIZIO_DESTINAZIONE_SEC"));
        utilizzoDichiaratoVO.setDataFineDestinazioneSec(rs.getTimestamp("DATA_FINE_DESTINAZIONE_SEC"));
       
        
        result.add(utilizzoDichiaratoVO);
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("result", result),
          new Variabile("utilizzoDichiaratoVO", utilizzoDichiaratoVO)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idConduzioneDichiarata", idConduzioneDichiarata)
      };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[UtilizzoDichiaratoDAO::getListUtilizzoDichiaratoByIdConduzioneDichiarata] ", t,
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
          "[UtilizzoDichiaratoDAO::getListUtilizzoDichiaratoByIdConduzioneDichiarata] END.");
    }
  }
	
	
	
}
