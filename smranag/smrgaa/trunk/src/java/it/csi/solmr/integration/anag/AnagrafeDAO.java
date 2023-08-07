package it.csi.solmr.integration.anag;

import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.AnagraficaAzVO;
import it.csi.solmr.dto.anag.AziendaCollegataVO;
import it.csi.solmr.dto.anag.DelegaVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.dto.anag.ProcedureAttiveVO;
import it.csi.solmr.dto.anag.SoggettoAssociatoVO;
import it.csi.solmr.dto.anag.TipoCessazioneVO;
import it.csi.solmr.dto.anag.services.DelegaAnagrafeVO;
import it.csi.solmr.dto.uma.DittaUMAVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.SolmrErrors;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.DataControlException;
import it.csi.solmr.exception.NotFoundException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.exception.services.ErrorTypes;
import it.csi.solmr.integration.BaseDAO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.ObjectNotFoundException;

//<p>Title: S.O.L.M.R.</p>
//<p>Description: Servizi On-Line per il Mondo Rurale</p>
//<p>Copyright: Copyright (c) 2003</p>
//<p>Company: TOBECONFIG</p>
//@author
//@version 1.0

public class AnagrafeDAO extends BaseDAO {

	public AnagrafeDAO() throws ResourceAccessException {
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public AnagrafeDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/*
  Il parametro isIdAzienda se vale false indica che la ricerca va fatta
  su ID_ANAGRAFICA_AZIENDA, se invece vale true indica che la ricerca va
  fatta su ID_AZIENDA
  Il parametro dataSituazioneAl viene preso in considerazione SOLO E SOLTANTO
	 */
	public AnagAziendaVO findByPrimaryKey(Long anagAziendaPK,
	    boolean isIdAzienda, Date dataSituazioneAl)
	  throws DataAccessException, NotFoundException 
	{

		AnagAziendaVO result = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		try 
		{
			conn = getDatasource().getConnection();

			String find =   
			  "SELECT AA.ID_ANAGRAFICA_AZIENDA, " +
			  "       AA.ID_AZIENDA, "+
			  "       AA.DATA_INIZIO_VALIDITA, " +
			  "       AA.DATA_FINE_VALIDITA, "+
			  "       AA.CUAA ALIAS_CUAA, " +
			  "       AA.PARTITA_IVA, " +
			  "       AA.DENOMINAZIONE ANAG_DENOMINAZIONE,  "+
			  "       AA.ID_FORMA_GIURIDICA ALIAS_FORMA_GIURIDICA, "+
			  "       AA.ID_TIPOLOGIA_AZIENDA ALIAS_TIPOLOGIA_AZIENDA, "+
			  "       AA.ID_ATTIVITA_ATECO ALIAS_ATTIVITA_ATECO, "+
			  "       AA.ID_ATTIVITA_OTE ALIAS_ATTIVITA_OTE, " +
			  "       AA.ID_UDE, " +
			  "       AA.RLS, " +
			  "       AA.ID_DIMENSIONE_AZIENDA, " +
			  "       TU.CLASSE_UDE, " +
			  "       AA.ESONERO_PAGAMENTO_GF, " +
			  "       TDA.DESCRIZIONE AS DESC_DIM_AZIENDA, "+
			  "       TFG.DESCRIZIONE ALIAS_DESC_TFG, "+
			  "       TTA.DESCRIZIONE ALIAS_DESC_TTA, "+
			  "       TTA.FLAG_FORMA_ASSOCIATA, "+
			  "       TAT.DESCRIZIONE ALIAS_DESC_TAT, "+
			  "       TOT.DESCRIZIONE ALIAS_DESC_TOT, "+
			  "       TAT.CODICE ALIAS_CODICE_TAT, " +
			  "       TOT.CODICE ALIAS_CODICE_TOT, "+
			  "       AA.PROVINCIA_COMPETENZA, " +
			  "       P2.SIGLA_PROVINCIA ALIAS_PROV_COMPETENZA, "+
			  "       AA.CCIAA_PROVINCIA_REA, " +
			  "       AA.CCIAA_NUMERO_REA, "+
			  "       AA.CCIAA_NUMERO_REGISTRO_IMPRESE, " +
			  "       AA.CCIAA_ANNO_ISCRIZIONE, "+
			  "       P1.ISTAT_PROVINCIA ALIAS_ISTAT_PROV, "+
			  "       P1.SIGLA_PROVINCIA ALIAS_PROV, " +
			  "       AA.SEDELEG_COMUNE, " +
			  "       COMUNE.FLAG_ESTERO ALIAS_SEDELE_ESTERO, "+
			  "       COMUNE.DESCOM ALIAS_DESCOM, " +
			  "       AA.SEDELEG_INDIRIZZO, " +
			  "       AA.SEDELEG_CAP, "+
			  "       AA.SEDELEG_CITTA_ESTERO, "+
			  "       AA.MAIL ALIAS_MAIL, " +
			  "       AA.SITOWEB, " +
			  "       AA.NOTE, " +
			  "       AA.DATA_CESSAZIONE, "+
			  "       AA.CAUSALE_CESSAZIONE, " +
			  "       AA.DATA_AGGIORNAMENTO, "+
			  "       AA.ID_UTENTE_AGGIORNAMENTO, " +
			  "       AA.MOTIVO_MODIFICA,  "+
			  "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
		      "          || ' ' " + 
		      "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
		      "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
		      "         WHERE AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
			  "       AS UT_DENOMINAZIONE, " +
			  "       (SELECT PVU.DENOMINAZIONE " +
			  "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
			  "        WHERE AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
			  "       AS UT_ENTE_APPART, "+
			  "       P2.DESCRIZIONE ALIAS_DESC_PROV_COMPETENZA, "+
			  "       A.FLAG_AZIENDA_PROVVISORIA, "+
			  "       A.FASCICOLO_DEMATERIALIZZATO, "+
			  "       A.ID_AZIENDA_PROVENIENZA, "+
			  "       AA.NUMERO_AGEA, " +
			  "       AA.INTESTAZIONE_PARTITA_IVA, "+
			  "       AA.ID_CESSAZIONE, " +
			  "       TC.DESCRIZIONE AS DESC_CESSAZIONE, " +
			  "       AA.ULU AS ULU, " +
			  "       AA.TELEFONO, " +
			  "       AA.FAX, " +
			  "       AA.PEC, " +
			  "       AA.CODICE_AGRITURISMO, " +
			  "       AA.DATA_AGGIORNAMENTO_UMA, " +
			  "       AA.FLAG_IAP, " +
			  "       AA.DATA_ISCRIZIONE_REA, " +
			  "       AA.DATA_CESSAZIONE_REA, " +
			  "       AA.DATA_ISCRIZIONE_RI, " +
			  "       AA.DATA_CESSAZIONE_RI, " +
			  "       AA.DATA_INIZIO_ATECO " +
			  "FROM   DB_ANAGRAFICA_AZIENDA AA, "+
			  "       DB_TIPO_FORMA_GIURIDICA TFG, "+
			  "       DB_TIPO_TIPOLOGIA_AZIENDA TTA, "+
			  "       DB_TIPO_ATTIVITA_ATECO TAT,   "+
			  "       DB_TIPO_ATTIVITA_OTE TOT,   "+
			  "       DB_AZIENDA A,   "+
			  "       COMUNE, " +
			  "       PROVINCIA P1, " +
			  "       PROVINCIA P2, " +
			//  "       PAPUA_V_UTENTE_LOGIN PVU, "+
			  "       DB_TIPO_CESSAZIONE TC, " +
			  "       DB_TIPO_UDE TU, " +
			  "       DB_TIPO_DIMENSIONE_AZIENDA TDA " +
			  "WHERE  AA.SEDELEG_COMUNE = COMUNE.ISTAT_COMUNE(+) "+
			  "AND    COMUNE.ISTAT_PROVINCIA = P1.ISTAT_PROVINCIA(+) "+
			  "AND    AA.PROVINCIA_COMPETENZA = P2.ISTAT_PROVINCIA(+) "+
			  "AND    AA.ID_FORMA_GIURIDICA = TFG.ID_FORMA_GIURIDICA(+) "+
			  "AND    AA.ID_TIPOLOGIA_AZIENDA = TTA.ID_TIPOLOGIA_AZIENDA(+) "+
			  "AND    AA.ID_ATTIVITA_ATECO = TAT.ID_ATTIVITA_ATECO(+) "+
			  "AND    AA.ID_ATTIVITA_OTE = TOT.ID_ATTIVITA_OTE(+) "+
			  "AND    A.ID_AZIENDA = AA.ID_AZIENDA "+
			//  "AND    AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
			  "AND    AA.ID_CESSAZIONE = TC.ID_CESSAZIONE(+) " +
			  "AND    AA.ID_UDE = TU.ID_UDE(+) " +
			  "AND    AA.ID_DIMENSIONE_AZIENDA = TDA.ID_DIMENSIONE_AZIENDA(+) ";

			if (isIdAzienda)
			{
				find+=
				"AND    AA.ID_AZIENDA = ? ";
				if (dataSituazioneAl!=null)
				{
					find+=
			  "AND    AA.DATA_INIZIO_VALIDITA  <= ? "+
			  "AND    (AA.DATA_FINE_VALIDITA IS NULL "+
			  "        OR AA.DATA_FINE_VALIDITA >= ?) ";
				}
				else
				{
					find+=
			  "AND   AA.DATA_FINE_VALIDITA IS NULL ";
				}
			}
			else
			{
				find+=
				"AND   AA.ID_ANAGRAFICA_AZIENDA = ? ";
			}

			SolmrLogger.debug(this, "Executing query findByPrimaryKey : "+find);

			stmt = conn.prepareStatement(find);

			SolmrLogger.debug(this,"-- ID_ANAGRAFICA_AZIENDA ="+anagAziendaPK.longValue());
			stmt.setLong(1, anagAziendaPK.longValue());
			if (isIdAzienda && dataSituazioneAl!=null)
			{
				SolmrLogger.debug(this,"--  dataSituazioneAl ="+dataSituazioneAl);
				stmt.setDate(2, new java.sql.Date(dataSituazioneAl.getTime()));
				SolmrLogger.debug(this,"--  dataSituazioneAl ="+dataSituazioneAl);
				stmt.setDate(3, new java.sql.Date(dataSituazioneAl.getTime()));
			}

			ResultSet rs = stmt.executeQuery();


			if (rs.next()) 
			{
				result = new AnagAziendaVO();
				result.setIntestazionePartitaIva(rs.getString("INTESTAZIONE_PARTITA_IVA"));
				result.setIdAnagAzienda(new Long(rs.getLong("ID_ANAGRAFICA_AZIENDA")));
				result.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
				result.setDataInizioVal(rs.getDate("DATA_INIZIO_VALIDITA"));
				result.setDataFineVal(rs.getDate("DATA_FINE_VALIDITA"));
				result.setCUAA(rs.getString("ALIAS_CUAA"));
				result.setPartitaIVA(rs.getString("PARTITA_IVA"));
				result.setDenominazione(rs.getString("ANAG_DENOMINAZIONE"));
				result.setIdUde(checkLongNull(rs.getString("ID_UDE")));
				result.setClasseUde(checkLongNull(rs.getString("CLASSE_UDE")));
				result.setIdDimensioneAzienda(checkLongNull(rs.getString("ID_DIMENSIONE_AZIENDA")));
				result.setRls(rs.getBigDecimal("RLS"));
				result.setUlu(rs.getBigDecimal("ULU"));
				result.setDescDimensioneAzienda(rs.getString("DESC_DIM_AZIENDA"));
				result.setEsoneroPagamentoGF(rs.getString("ESONERO_PAGAMENTO_GF"));
				result.setDataAggiornamentoUma(rs.getTimestamp("DATA_AGGIORNAMENTO_UMA"));
				result.setFlagIap(rs.getString("FLAG_IAP"));
				result.setDataIscrizioneRea(rs.getTimestamp("DATA_ISCRIZIONE_REA"));
				result.setDataCessazioneRea(rs.getTimestamp("DATA_CESSAZIONE_REA"));
				result.setDataIscrizioneRi(rs.getTimestamp("DATA_ISCRIZIONE_RI"));
        result.setDataCessazioneRi(rs.getTimestamp("DATA_CESSAZIONE_RI"));
        result.setDataInizioAteco(rs.getTimestamp("DATA_INIZIO_ATECO"));
				
				String codFormaGiur = rs.getString("ALIAS_FORMA_GIURIDICA");
				if(codFormaGiur!=null && !codFormaGiur.equals("")){
					result.setTipoFormaGiuridica(new CodeDescription(
							new Integer(codFormaGiur),
							rs.getString("ALIAS_DESC_TFG")));
				}
				else{
					result.setTipoFormaGiuridica(new CodeDescription());
				}
				String codTipolAzienda = rs.getString("ALIAS_TIPOLOGIA_AZIENDA");
				if(codTipolAzienda!=null)
					result.setTipoTipologiaAzienda(new CodeDescription(
							new Integer(codTipolAzienda),
							rs.getString("ALIAS_DESC_TTA")));

				else
					result.setTipoTipologiaAzienda(new CodeDescription());

				result.setTipiAzienda(codTipolAzienda);

				//Aggiunta per gestione aziende collegate
				result.setFlagFormaAssociata(rs.getString("FLAG_FORMA_ASSOCIATA"));

				String codATECO = rs.getString("ALIAS_ATTIVITA_ATECO");
				if(codATECO!=null){
					CodeDescription codeATECO = new CodeDescription(new Integer(codATECO),
							rs.getString("ALIAS_DESC_TAT"));
					codeATECO.setSecondaryCode(rs.getString("ALIAS_CODICE_TAT"));
					result.setTipoAttivitaATECO(codeATECO);
				}
				else
					result.setTipoAttivitaATECO(new CodeDescription());

				String codOTE = rs.getString("ALIAS_ATTIVITA_OTE");
				if(codOTE!=null){
					CodeDescription codeOTE = new CodeDescription(new Integer(codOTE),
							rs.getString("ALIAS_DESC_TOT"));
					codeOTE.setSecondaryCode(rs.getString("ALIAS_CODICE_TOT"));
					result.setTipoAttivitaOTE(codeOTE);
				}
				else
					result.setTipoAttivitaOTE(new CodeDescription());

				result.setProvCompetenza(rs.getString("PROVINCIA_COMPETENZA"));
				result.setSiglaProvCompetenza(rs.getString("ALIAS_PROV_COMPETENZA"));
				result.setDescrizioneProvCompetenza(rs.getString("ALIAS_DESC_PROV_COMPETENZA"));
				result.setCCIAAprovREA(rs.getString("CCIAA_PROVINCIA_REA"));
				result.setCCIAAnumeroREA(new Long(rs.getLong("CCIAA_NUMERO_REA")));
				result.setStrCCIAAnumeroREA(rs.getString("CCIAA_NUMERO_REA"));
				result.setCCIAAnumRegImprese(rs.getString("CCIAA_NUMERO_REGISTRO_IMPRESE"));
				result.setCCIAAannoIscrizione(rs.getString("CCIAA_ANNO_ISCRIZIONE"));

				String flagEstero = rs.getString("ALIAS_SEDELE_ESTERO");
				result.setSedelegComune(rs.getString("SEDELEG_COMUNE"));

				if(flagEstero==null || flagEstero.equals("")){
					result.setSedelegComune("");
					result.setSedelegEstero("");
					result.setStatoEstero("");
					result.setSedelegIstatProv("");
					result.setSedelegProv("");
					result.setDescComune("");
					result.setSedelegCAP("");
					result.setSedelegCittaEstero("");
				}
				else if(flagEstero.equals(SolmrConstants.FLAG_S)){
					String sedelegEstero = rs.getString("ALIAS_DESCOM");
					result.setSedelegEstero(sedelegEstero);
					result.setStatoEstero(sedelegEstero);
					result.setSedelegIstatProv("");
					result.setSedelegProv("");
					result.setDescComune("");
					result.setSedelegCAP("");
					result.setSedelegCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
				}
				else{
					result.setSedelegIstatProv(rs.getString("ALIAS_ISTAT_PROV"));
					result.setSedelegProv(rs.getString("ALIAS_PROV"));
					result.setDescComune(rs.getString("ALIAS_DESCOM"));
					result.setSedelegCAP(rs.getString("SEDELEG_CAP"));
					result.setSedelegCittaEstero("");
					result.setSedelegEstero("");
					result.setStatoEstero("");
				}

				result.setSedelegIndirizzo(rs.getString("SEDELEG_INDIRIZZO"));

				result.setMail(rs.getString("ALIAS_MAIL"));
				result.setSitoWEB(rs.getString("SITOWEB"));
				result.setTelefono(rs.getString("TELEFONO"));
				result.setFax(rs.getString("FAX"));
				result.setPec(rs.getString("PEC"));
				result.setCodiceAgriturismo(rs.getString("CODICE_AGRITURISMO"));
				result.setNote(rs.getString("NOTE"));
				result.setDataCessazione(rs.getDate("DATA_CESSAZIONE"));
				result.setCausaleCessazione(rs.getString("CAUSALE_CESSAZIONE"));
				java.util.Date dataAgg = rs.getDate("DATA_AGGIORNAMENTO");
				result.setDataAggiornamento(dataAgg);
				result.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				String motivoModifica = rs.getString("MOTIVO_MODIFICA");
				String utDenominazione = rs.getString("UT_DENOMINAZIONE");
				String utEnteAppart = rs.getString("UT_ENTE_APPART");
				String data = DateUtils.getCurrentDateString();
				if(result.getDataFineVal()!=null && result.getDataFineVal().before(DateUtils.parseDate(DateUtils.getCurrentDateString()))){
					data = DateUtils.formatDate(result.getDataInizioVal());
				}
				result.setDataSituazioneAlStr(data);

				String ultimaModifica = DateUtils.formatDate(dataAgg);
				String tmp="";
				if(utDenominazione!=null && !utDenominazione.equals("")){
					if(tmp.equals(""))
						tmp+=" (";
					tmp+=utDenominazione;
				}
				if(utEnteAppart!=null && !utEnteAppart.equals("")){
					if(tmp.equals(""))
						tmp+=" (";
					else
						tmp+=" - ";
					tmp+=utEnteAppart;
				}
				if(motivoModifica!=null && !motivoModifica.equals("")){
					if(tmp.equals(""))
						tmp+=" (";
					else
						tmp+=" - ";
					tmp+=motivoModifica;
				}
				if(!tmp.equals(""))
					tmp+=")";
				ultimaModifica+=tmp;
				result.setUltimaModifica(ultimaModifica);
				result.setPosizioneSchedario(rs.getString("NUMERO_AGEA"));

				//Ultima Modifica Spezzata
				result.setDataUltimaModifica(dataAgg);
				result.setUtenteUltimaModifica(utDenominazione);
				result.setEnteUltimaModifica(utEnteAppart);
				result.setMotivoModifica(motivoModifica);

				/**
				 * Questa porzione di codice va a vedere se l'azienda con cui sto
				 * lavorando è un'azienda provvisoria
				 */
				String temp=rs.getString("FLAG_AZIENDA_PROVVISORIA");
				if ("S".equals(temp))
				{
					result.setFlagAziendaProvvisoria(true);
					/*temp=rs.getString("ID_AZIENDA_PROVENIENZA");
					if (temp!=null)
						result.setIdAziendaSubentro(new Long(temp));*/
				}
				else
					result.setFlagAziendaProvvisoria(false);
				if(Validator.isNotEmpty(rs.getString("ID_AZIENDA_PROVENIENZA"))) {
					result.setIdAziendaProvenienza(new Long(rs.getLong("ID_AZIENDA_PROVENIENZA")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_CESSAZIONE"))) {
					result.setIdCessazione(new Long(rs.getLong("ID_CESSAZIONE")));
					TipoCessazioneVO tipoCessazioneVO = new TipoCessazioneVO();
					tipoCessazioneVO.setIdCessazione(new Long(rs.getLong("ID_CESSAZIONE")));
					tipoCessazioneVO.setDescrizione(rs.getString("DESC_CESSAZIONE"));
					result.setTipoCessazioneVO(tipoCessazioneVO);
				}
				
				result.setFascicoloDematerializzato(rs.getString("FASCICOLO_DEMATERIALIZZATO"));

			} else
				//throw new DataAccessException();

				//if (result == null)
				throw new NotFoundException(SolmrErrors.EXC_NOT_FOUND_PK);
			rs.close();

			SolmrLogger.debug(this, "Executed query - Found record with primary key: "+anagAziendaPK);
		} catch (SQLException exc) {
			SolmrLogger.fatal(this, "findByPrimaryKey - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} catch (DataAccessException daexc) {
			SolmrLogger.fatal(this, "findByPrimaryKey - ResultSet null");
			throw daexc;
		} catch (NotFoundException nfexc) {
			SolmrLogger.fatal(this, "findByPrimaryKey - NotFoundException: "+nfexc.getMessage());
			throw nfexc;
		} catch (Exception ex) {
			SolmrLogger.fatal(this, "findByPrimaryKey - Generic Exception: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, "findByPrimaryKey - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "findByPrimaryKey - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return result;
	}

	// Metodo per recuperare l'azienda attiva
	public AnagAziendaVO findAziendaAttiva(Long idAzienda) throws DataAccessException, NotFoundException {

		AnagAziendaVO result = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		try 
		{
			conn = getDatasource().getConnection();

			String find =   
			"SELECT   AA.ID_ANAGRAFICA_AZIENDA, " +
			"         AA.ID_AZIENDA, "+
			"         AA.DATA_INIZIO_VALIDITA, " +
			"         AA.DATA_FINE_VALIDITA, "+
			"         AA.CUAA ALIAS_CUAA, " +
			"         AA.PARTITA_IVA, " +
			"         AA.DENOMINAZIONE ANAG_DENOMINAZIONE,  "+
			"         AA.ID_FORMA_GIURIDICA ALIAS_FORMA_GIURIDICA, "+
			"         AA.ID_TIPOLOGIA_AZIENDA ALIAS_TIPOLOGIA_AZIENDA, "+
			"         AA.ID_ATTIVITA_ATECO ALIAS_ATTIVITA_ATECO, "+
			"         AA.ID_ATTIVITA_OTE ALIAS_ATTIVITA_OTE, " +
			"         AA.ID_UDE, " +
			"         AA.ID_DIMENSIONE_AZIENDA, " +
			"         AA.RLS, " +
			"         AA.INTESTAZIONE_PARTITA_IVA, " +
			"         TU.CLASSE_UDE, " +
			"         AA.ESONERO_PAGAMENTO_GF, " +
			"         TDA.DESCRIZIONE AS DESC_DIM_AZIENDA, "+
			"         TFG.DESCRIZIONE ALIAS_DESC_TFG, "+
			"         TAT.DESCRIZIONE ALIAS_DESC_TAT, "+
			"         TOT.DESCRIZIONE ALIAS_DESC_TOT, "+
			"         TAT.CODICE ALIAS_CODICE_TAT, " +
			"         TOT.CODICE ALIAS_CODICE_TOT, "+
			"         TTA.DESCRIZIONE ALIAS_DESC_TTA, "+
			"         TTA.FLAG_FORMA_ASSOCIATA, "+
			"         AA.PROVINCIA_COMPETENZA, " +
			"         P2.SIGLA_PROVINCIA ALIAS_PROV_COMPETENZA, "+
			"         AA.CCIAA_PROVINCIA_REA, " +
			"         AA.CCIAA_NUMERO_REA, "+
			"         AA.CCIAA_NUMERO_REGISTRO_IMPRESE, " +
			"         AA.CCIAA_ANNO_ISCRIZIONE, "+
			"         P1.ISTAT_PROVINCIA ALIAS_ISTAT_PROV, "+
			"         P1.SIGLA_PROVINCIA ALIAS_PROV, " +
			"         AA.SEDELEG_COMUNE, " +
			"         COMUNE.FLAG_ESTERO ALIAS_SEDELE_ESTERO, "+
			"         COMUNE.DESCOM ALIAS_DESCOM, " +
			"         AA.SEDELEG_INDIRIZZO, " +
			"         AA.SEDELEG_CAP, "+
			"         COMUNE.ISTAT_COMUNE ALIAS_ISTAT_COMUNE, " +
			"         AA.SEDELEG_CITTA_ESTERO, "+
			"         AA.MAIL ALIAS_MAIL, " +
			"         AA.SITOWEB, " +
			"         AA.NOTE, " +
			"         AA.DATA_CESSAZIONE, "+
			"         AA.CAUSALE_CESSAZIONE, " +
			"         AA.DATA_AGGIORNAMENTO, "+
			"         AA.ID_UTENTE_AGGIORNAMENTO, " +
			"         AA.MOTIVO_MODIFICA,  "+
			"         A.FASCICOLO_DEMATERIALIZZATO, " +
			"         A.FLAG_AZIENDA_PROVVISORIA, "+
			"         A.ID_AZIENDA_PROVENIENZA, "+
			"         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
		    "          || ' ' " + 
		    "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
		    "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
		    "         WHERE AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
			"         AS UT_DENOMINAZIONE, " +
			"         (SELECT PVU.DENOMINAZIONE " +
		    "          FROM PAPUA_V_UTENTE_LOGIN PVU " +
		    "          WHERE AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
			"         AS UT_ENTE_APPART, "+
			"         P2.DESCRIZIONE ALIAS_DESC_PROV_COMPETENZA, " +
			"         A.ID_OPR, " +
			"         A.ID_AZIENDA_PROVENIENZA, " +
			"         A.DATA_CONTROLLI_ALLEVAMENTI, " +
			// Michele, 27/11/2009 - INIZIO
      "         A.ID_TIPO_FORMA_ASSOCIATA, " +
      "         TFA.LABEL_ELENCO_ASSOCIATI, " +
      "         TFA.LABEL_SUB_ASSOCIATI, " +
      // Michele, 27/11/2009 - FINE
			"         AA.ID_CESSAZIONE, " +
			"         TC.DESCRIZIONE AS DESC_CESSAZIONE, AA.ULU AS ULU, " +
			"         AA.TELEFONO, AA.FAX, AA.PEC, AA.CODICE_AGRITURISMO, " +
			"         AA.DATA_AGGIORNAMENTO_UMA, " +
			"         AA.FLAG_IAP, " +
			"         AA.DATA_ISCRIZIONE_REA, " +
      "         AA.DATA_CESSAZIONE_REA, " +
      "         AA.DATA_ISCRIZIONE_RI, " +
      "         AA.DATA_CESSAZIONE_RI, " +
      "         AA.DATA_INIZIO_ATECO " +
			"FROM     DB_ANAGRAFICA_AZIENDA AA, "+
			"         DB_TIPO_FORMA_GIURIDICA TFG, "+
			"         DB_TIPO_TIPOLOGIA_AZIENDA TTA, "+
			"         DB_TIPO_ATTIVITA_ATECO TAT,   "+
			"         DB_AZIENDA A,   "+
			"         DB_TIPO_ATTIVITA_OTE TOT,   "+
			"         COMUNE, PROVINCIA P1, " +
			"         PROVINCIA P2, " +
		//	"         PAPUA_V_UTENTE_LOGIN PVU, "+
			"         DB_TIPO_CESSAZIONE TC, " +
			"         DB_TIPO_UDE TU, " +
			"         DB_TIPO_DIMENSIONE_AZIENDA TDA, " +
			// Michele, 27/11/2009 - INIZIO
      "         DB_TIPO_FORMA_ASSOCIATA TFA   "+
      // Michele, 27/11/2009 - FINE
			"WHERE    AA.SEDELEG_COMUNE = COMUNE.ISTAT_COMUNE(+) "+
			"AND      COMUNE.ISTAT_PROVINCIA = P1.ISTAT_PROVINCIA(+) "+
			"AND      AA.PROVINCIA_COMPETENZA = P2.ISTAT_PROVINCIA(+) "+
			"AND      AA.ID_FORMA_GIURIDICA = TFG.ID_FORMA_GIURIDICA(+) "+
			"AND      AA.ID_TIPOLOGIA_AZIENDA = TTA.ID_TIPOLOGIA_AZIENDA(+) "+
			"AND      AA.ID_ATTIVITA_ATECO = TAT.ID_ATTIVITA_ATECO(+) "+
			"AND      AA.ID_ATTIVITA_OTE = TOT.ID_ATTIVITA_OTE(+) "+
		//	"AND      AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN "+
			"AND      A.ID_AZIENDA = AA.ID_AZIENDA "+
			"AND      AA.ID_AZIENDA = ? "+
			"AND      AA.DATA_FINE_VALIDITA IS NULL " +
			"AND      AA.ID_CESSAZIONE = TC.ID_CESSAZIONE(+) " +
			"AND      AA.ID_UDE = TU.ID_UDE(+) " +
			"AND      AA.ID_DIMENSIONE_AZIENDA = TDA.ID_DIMENSIONE_AZIENDA(+) " +
			// Michele, 27/11/2009 - INIZIO
      "AND      A.ID_TIPO_FORMA_ASSOCIATA = TFA.ID_TIPO_FORMA_ASSOCIATA(+) ";
      // Michele, 27/11/2009 - FINE

			stmt = conn.prepareStatement(find);

			SolmrLogger.debug(this, "Executing query: "+find);

			stmt.setLong(1, idAzienda.longValue());
			ResultSet rs = stmt.executeQuery();

			if (rs != null) {
				boolean primaVolta = true;
				while(rs.next())
				{
					if(primaVolta)
					{
						result = new AnagAziendaVO();
						result.setIdAnagAzienda(new Long(rs.getLong("ID_ANAGRAFICA_AZIENDA")));
						result.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
						result.setDataInizioVal(rs.getDate("DATA_INIZIO_VALIDITA"));
						result.setDataFineVal(rs.getDate("DATA_FINE_VALIDITA"));
						result.setCUAA(rs.getString("ALIAS_CUAA"));
						result.setPartitaIVA(rs.getString("PARTITA_IVA"));
						result.setDenominazione(rs.getString("ANAG_DENOMINAZIONE"));
						result.setIdUde(checkLongNull(rs.getString("ID_UDE")));
						result.setIdDimensioneAzienda(checkLongNull(rs.getString("ID_DIMENSIONE_AZIENDA")));
						result.setRls(rs.getBigDecimal("RLS"));
						result.setUlu(rs.getBigDecimal("ULU"));
						result.setIntestazionePartitaIva(rs.getString("INTESTAZIONE_PARTITA_IVA"));
						result.setClasseUde(checkLongNull(rs.getString("CLASSE_UDE")));
						result.setDescDimensioneAzienda(rs.getString("DESC_DIM_AZIENDA"));
						result.setEsoneroPagamentoGF(rs.getString("ESONERO_PAGAMENTO_GF"));
						result.setDataAggiornamentoUma(rs.getTimestamp("DATA_AGGIORNAMENTO_UMA"));
						result.setFlagIap(rs.getString("FLAG_IAP"));
						result.setDataIscrizioneRea(rs.getTimestamp("DATA_ISCRIZIONE_REA"));
		        result.setDataCessazioneRea(rs.getTimestamp("DATA_CESSAZIONE_REA"));
		        result.setDataIscrizioneRi(rs.getTimestamp("DATA_ISCRIZIONE_RI"));
		        result.setDataCessazioneRi(rs.getTimestamp("DATA_CESSAZIONE_RI"));
		        result.setDataInizioAteco(rs.getTimestamp("DATA_INIZIO_ATECO"));

						String codFormaGiur = rs.getString("ALIAS_FORMA_GIURIDICA");
						if(codFormaGiur!=null && !codFormaGiur.equals("")){
							result.setTipoFormaGiuridica(new CodeDescription(
									new Integer(codFormaGiur),
									rs.getString("ALIAS_DESC_TFG")));
						}
						else{
							result.setTipoFormaGiuridica(new CodeDescription());
						}

						String codTipolAzienda = rs.getString("ALIAS_TIPOLOGIA_AZIENDA");
						if(codTipolAzienda!=null) {
							result.setTipoTipologiaAzienda(new CodeDescription(new Integer(codTipolAzienda), rs.getString("ALIAS_DESC_TTA")));
							result.setTipiAzienda(codTipolAzienda);
						}
						else {
							result.setTipoTipologiaAzienda(new CodeDescription());
						}
						
						//Aggiunta per gestione aziende collegate
		        result.setFlagFormaAssociata(rs.getString("FLAG_FORMA_ASSOCIATA"));
		        
		        // Michele, 27/11/2009 - INIZIO
            String idTipoFormaAssociata = rs.getString("ID_TIPO_FORMA_ASSOCIATA");
            result.setIdTipoFormaAssociata(idTipoFormaAssociata==null ? null : new Long(idTipoFormaAssociata));
            result.setLabelElencoAssociati(rs.getString("LABEL_ELENCO_ASSOCIATI"));
            result.setLabelSubAssociati(rs.getString("LABEL_SUB_ASSOCIATI"));
            // Michele, 27/11/2009 - FINE


						String codATECO = rs.getString("ALIAS_ATTIVITA_ATECO");
						if(codATECO!=null){
							CodeDescription codeATECO = new CodeDescription(new Integer(codATECO),
									rs.getString("ALIAS_DESC_TAT"));
							codeATECO.setSecondaryCode(rs.getString("ALIAS_CODICE_TAT"));
							result.setTipoAttivitaATECO(codeATECO);
						}
						else
							result.setTipoAttivitaATECO(new CodeDescription());

						String codOTE = rs.getString("ALIAS_ATTIVITA_OTE");
						if(codOTE!=null){
							CodeDescription codeOTE = new CodeDescription(new Integer(codOTE),
									rs.getString("ALIAS_DESC_TOT"));
							codeOTE.setSecondaryCode(rs.getString("ALIAS_CODICE_TOT"));
							result.setTipoAttivitaOTE(codeOTE);
						}
						else
							result.setTipoAttivitaOTE(new CodeDescription());

						result.setProvCompetenza(rs.getString("PROVINCIA_COMPETENZA"));
						result.setProvincePiemonte(rs.getString("PROVINCIA_COMPETENZA"));
						result.setSiglaProvCompetenza(rs.getString("ALIAS_PROV_COMPETENZA"));
						result.setDescrizioneProvCompetenza(rs.getString("ALIAS_DESC_PROV_COMPETENZA"));
						result.setCCIAAprovREA(rs.getString("CCIAA_PROVINCIA_REA"));
						if(Validator.isNotEmpty(rs.getString("CCIAA_NUMERO_REA"))) {
							result.setCCIAAnumeroREA(new Long(rs.getLong("CCIAA_NUMERO_REA")));
							result.setStrCCIAAnumeroREA(rs.getString("CCIAA_NUMERO_REA"));
						}
						else {
							result.setCCIAAnumeroREA(null);
						}
						result.setCCIAAnumRegImprese(rs.getString("CCIAA_NUMERO_REGISTRO_IMPRESE"));
						result.setCCIAAannoIscrizione(rs.getString("CCIAA_ANNO_ISCRIZIONE"));

						result.setSedelegComune(rs.getString("SEDELEG_COMUNE"));

						String flagEstero = rs.getString("ALIAS_SEDELE_ESTERO");
						if(flagEstero==null || flagEstero.equals("")){
							result.setSedelegComune("");
							result.setSedelegEstero("");
							result.setStatoEstero("");
							result.setSedelegIstatProv("");
							result.setSedelegProv("");
							result.setDescComune("");
							result.setSedelegCAP("");
							result.setSedelegCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
						}
						else if(flagEstero.equals(SolmrConstants.FLAG_S)){
							String sedelegEstero = rs.getString("ALIAS_DESCOM");
							result.setSedelegEstero(sedelegEstero);
							result.setStatoEstero(sedelegEstero);
							result.setSedelegIstatProv("");
							result.setSedelegProv("");
							result.setDescComune("");
							result.setSedelegCAP("");
							result.setSedelegCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
						}
						else{
							result.setSedelegIstatProv(rs.getString("ALIAS_ISTAT_PROV"));
							result.setSedelegProv(rs.getString("ALIAS_PROV"));
							result.setDescComune(rs.getString("ALIAS_DESCOM"));
							result.setSedelegIstatComune(rs.getString("ALIAS_ISTAT_COMUNE"));
							result.setSedelegCAP(rs.getString("SEDELEG_CAP"));
							result.setSedelegCittaEstero("");
							result.setSedelegEstero("");
							result.setStatoEstero("");
						}

						result.setSedelegIndirizzo(rs.getString("SEDELEG_INDIRIZZO"));

						result.setMail(rs.getString("ALIAS_MAIL"));
						result.setSitoWEB(rs.getString("SITOWEB"));
						result.setNote(rs.getString("NOTE"));
						result.setTelefono(rs.getString("TELEFONO"));
						result.setFax(rs.getString("FAX"));
						result.setPec(rs.getString("PEC"));
						result.setCodiceAgriturismo(rs.getString("CODICE_AGRITURISMO"));
						result.setDataCessazione(rs.getDate("DATA_CESSAZIONE"));
						result.setCausaleCessazione(rs.getString("CAUSALE_CESSAZIONE"));
						java.util.Date dataAgg = rs.getDate("DATA_AGGIORNAMENTO");
						result.setDataAggiornamento(dataAgg);
						result.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
						String motivoModifica = rs.getString("MOTIVO_MODIFICA");
						String utDenominazione = rs.getString("UT_DENOMINAZIONE");
						String utEnteAppart = rs.getString("UT_ENTE_APPART");
						String ultimaModifica = DateUtils.formatDate(dataAgg);
						result.setDataSituazioneAlStr(DateUtils.getCurrentDateString());
						String tmp="";
						if(utDenominazione!=null && !utDenominazione.equals("")){
							if(tmp.equals(""))
								tmp+=" (";
							tmp+=utDenominazione;
						}
						if(utEnteAppart!=null && !utEnteAppart.equals("")){
							if(tmp.equals(""))
								tmp+=" (";
							else
								tmp+=" - ";
							tmp+=utEnteAppart;
						}
						if(motivoModifica!=null && !motivoModifica.equals("")){
							if(tmp.equals(""))
								tmp+=" (";
							else
								tmp+=" - ";
							tmp+=motivoModifica;
						}
						if(!tmp.equals(""))
							tmp+=")";
						ultimaModifica+=tmp;
						result.setUltimaModifica(ultimaModifica);

						//Ultima Modifica Spezzata
						result.setDataUltimaModifica(dataAgg);
						result.setUtenteUltimaModifica(utDenominazione);
						result.setEnteUltimaModifica(utEnteAppart);
						result.setMotivoModifica(motivoModifica);

						primaVolta = false;
						if(Validator.isNotEmpty(rs.getString("ID_OPR"))) {
							result.setIdOpr(new Long(rs.getLong("ID_OPR")));
						}
						/**
						 * Questa porzione di codice va a vedere se l'azienda con cui sto
						 * lavorando è un'azienda provvisoria
						 */
						String temp=rs.getString("FLAG_AZIENDA_PROVVISORIA");
						if ("S".equals(temp))
						{
							result.setFlagAziendaProvvisoria(true);
							temp=rs.getString("ID_AZIENDA_PROVENIENZA");
							if (temp!=null)
								result.setIdAziendaSubentro(new Long(temp));
						}
						else
							result.setFlagAziendaProvvisoria(false);
						if(Validator.isNotEmpty(rs.getString("ID_AZIENDA_PROVENIENZA"))) {
							result.setIdAziendaProvenienza(new Long(rs.getLong("ID_AZIENDA_PROVENIENZA")));
						}
						if(Validator.isNotEmpty(rs.getString("ID_CESSAZIONE"))) {
							result.setIdCessazione(new Long(rs.getLong("ID_CESSAZIONE")));
							TipoCessazioneVO tipoCessazioneVO = new TipoCessazioneVO();
							tipoCessazioneVO.setIdCessazione(new Long(rs.getLong("ID_CESSAZIONE")));
							tipoCessazioneVO.setDescrizione(rs.getString("DESC_CESSAZIONE"));
							result.setTipoCessazioneVO(tipoCessazioneVO);
						}
						
						result.setFascicoloDematerializzato(rs.getString("FASCICOLO_DEMATERIALIZZATO"));
						
						result.setDataControlliAllevamenti(rs.getTimestamp("DATA_CONTROLLI_ALLEVAMENTI"));

					}
					else
						throw new SolmrException("Errore: verificare i dati, troppe occorrenze attive dell'azienda");
				}
				rs.close();
			} else
				throw new DataAccessException();

			if (result == null)
				throw new NotFoundException(SolmrErrors.EXC_NOT_FOUND_PK);

			SolmrLogger.debug(this, "Executed query - Found record with primary key: "+idAzienda);
		} catch (SQLException exc) {
			SolmrLogger.fatal(this, "findByPrimaryKey - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} catch (DataAccessException daexc) {
			SolmrLogger.fatal(this, "findByPrimaryKey - ResultSet null");
			throw daexc;
		} catch (NotFoundException nfexc) {
			SolmrLogger.fatal(this, "findByPrimaryKey - NotFoundException: "+nfexc.getMessage());
			throw nfexc;
		} catch (Exception ex) {
			SolmrLogger.fatal(this, "findByPrimaryKey - Generic Exception: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, "findByPrimaryKey - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "findByPrimaryKey - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return result;
	}

	public Vector<AnagAziendaVO> getAziendaByCriterio(String valore)
	  throws DataAccessException, NotFoundException, SolmrException
	{

		Vector<AnagAziendaVO> result = new Vector<AnagAziendaVO>();
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getDatasource().getConnection();

			String search = 
			  "SELECT AA.ID_ANAGRAFICA_AZIENDA, " +
			  "       AA.ID_AZIENDA, "+
  			"       AA.DATA_INIZIO_VALIDITA, " +
  			"       AA.DATA_FINE_VALIDITA, "+
  			"       AA.CUAA ANAG_CUAA, " +
  			"       AA.PARTITA_IVA, " +
  			"       AA.DENOMINAZIONE ANAG_DENOMINAZIONE,  "+
  			"       AA.ID_FORMA_GIURIDICA ALIAS_FORMA_GIURIDICA, "+
  			"       AA.ID_TIPOLOGIA_AZIENDA ALIAS_TIPOLOGIA_AZIENDA, "+
  			"       AA.ID_ATTIVITA_ATECO ALIAS_ATTIVITA_ATECO, "+
  			"       AA.ID_ATTIVITA_OTE ALIAS_ATTIVITA_OTE, "+
  			"       AA.ID_UDE, " +
  			"       AA.ID_DIMENSIONE_AZIENDA, " +
  			"       AA.RLS, " +
  			"       AA.ULU AS ULU, " +
  			"       AA.INTESTAZIONE_PARTITA_IVA, " +
  			"       TU.CLASSE_UDE, " +
  			"       TDA.DESCRIZIONE AS DES_DIM_AZIENDA, " +
  			"       AA.ESONERO_PAGAMENTO_GF, " +
  			"       TFG.DESCRIZIONE ALIAS_DESC_TFG, "+
  			"       TTA.DESCRIZIONE ALIAS_DESC_TTA, "+
  			"       TAT.DESCRIZIONE ALIAS_DESC_TAT, "+
  			"       TOT.DESCRIZIONE ALIAS_DESC_TOT, "+
  			"       TAT.CODICE ALIAS_CODICE_TAT, " +
  			"       TOT.CODICE ALIAS_CODICE_TOT, "+
  			"       AA.PROVINCIA_COMPETENZA, " +
  			"       P2.SIGLA_PROVINCIA ALIAS_PROV_COMPETENZA, "+
  			"       AA.CCIAA_PROVINCIA_REA, " +
  			"       AA.CCIAA_NUMERO_REA, "+
  			"       AA.CCIAA_NUMERO_REGISTRO_IMPRESE, " +
  			"       AA.CCIAA_ANNO_ISCRIZIONE, "+
  			"       P1.ISTAT_PROVINCIA ALIAS_ISTAT_PROV, "+
  			"       P1.SIGLA_PROVINCIA ALIAS_PROV, " +
  			"       AA.SEDELEG_COMUNE, " +
  			"       COMUNE.FLAG_ESTERO ALIAS_SEDELE_ESTERO, "+
  			"       COMUNE.DESCOM ALIAS_DESCOM, " +
  			"       AA.SEDELEG_INDIRIZZO, " +
  			"       AA.SEDELEG_CAP, "+
  			"       AA.SEDELEG_CITTA_ESTERO, "+
  			"       AA.MAIL ANAG_MAIL, " +
  			"       AA.PEC, " +
  			"       AA.SITOWEB, " +
  			"       AA.NOTE, " +
  			"       AA.TELEFONO, " +
  			"       AA.FAX, " +
  			"       AA.DATA_CESSAZIONE, "+
  			"       AA.CAUSALE_CESSAZIONE, " +
  			"       AA.DATA_AGGIORNAMENTO, "+
  			"       AA.ID_UTENTE_AGGIORNAMENTO, " +
  			"       AA.MOTIVO_MODIFICA, "+
  			"         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
  	        "          || ' ' " + 
  	        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
  	        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
  	        "         WHERE AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
  	  		"       AS UT_DENOMINAZIONE, " +
  	  		"       (SELECT PVU.DENOMINAZIONE " +
  	        "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
  	        "        WHERE AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
  	  		"       AS UT_ENTE_APPART, "+
  			"       P2.DESCRIZIONE ALIAS_DESC_PROV_COMPETENZA, "+
  			"       AA.NUMERO_AGEA, "+
  			"       A.FASCICOLO_DEMATERIALIZZATO, " +
  			"       A.FLAG_AZIENDA_PROVVISORIA, "+
  			"       A.ID_AZIENDA_PROVENIENZA, "+
  			"       A.VARIAZIONE_UTILIZZI_AMMESSA," +
  			"       AA.INTESTAZIONE_PARTITA_IVA, " +
  			"       AA.DATA_AGGIORNAMENTO_UMA, " +
  			"       AA.FLAG_IAP, " +
  			"       AA.DATA_ISCRIZIONE_REA, " +
        "       AA.DATA_CESSAZIONE_REA, " +
        "       AA.DATA_ISCRIZIONE_RI, " +
        "       AA.DATA_CESSAZIONE_RI, " +
        "       AA.DATA_INIZIO_ATECO " +
  			"FROM   DB_ANAGRAFICA_AZIENDA AA, "+
  			"       DB_TIPO_FORMA_GIURIDICA TFG, "+
  			"       DB_TIPO_TIPOLOGIA_AZIENDA TTA, "+
  			"       DB_TIPO_ATTIVITA_ATECO TAT, "+
  			"       DB_TIPO_ATTIVITA_OTE TOT, "+
  			"       COMUNE, PROVINCIA P1, " +
  			"       PROVINCIA P2, " +
  			//"       PAPUA_V_UTENTE_LOGIN PVU, "+
  			"       DB_AZIENDA A," +
  			"       DB_TIPO_UDE TU," +
  			"       DB_TIPO_DIMENSIONE_AZIENDA TDA " +
  			"WHERE  AA.SEDELEG_COMUNE = COMUNE.ISTAT_COMUNE(+) "+
  			"AND    COMUNE.ISTAT_PROVINCIA = P1.ISTAT_PROVINCIA(+) "+
  			"AND    AA.PROVINCIA_COMPETENZA = P2.ISTAT_PROVINCIA(+) "+
  			"AND    AA.ID_FORMA_GIURIDICA = TFG.ID_FORMA_GIURIDICA(+) "+
  			"AND    AA.ID_TIPOLOGIA_AZIENDA = TTA.ID_TIPOLOGIA_AZIENDA(+) "+
  			"AND    AA.ID_ATTIVITA_ATECO = TAT.ID_ATTIVITA_ATECO(+) "+
  			"AND    AA.ID_UDE = TU.ID_UDE(+) " +
  			"AND    AA.ID_DIMENSIONE_AZIENDA = TDA.ID_DIMENSIONE_AZIENDA(+) " +
  			"AND    AA.ID_ATTIVITA_OTE = TOT.ID_ATTIVITA_OTE(+) "+
  			//"AND    AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN "+
  			"AND    AA.DATA_FINE_VALIDITA IS NULL "+
  			"AND    AA.CUAA = UPPER(?) "+
  			"AND    A.ID_AZIENDA = AA.ID_AZIENDA " +
  			"ORDER BY AA.DATA_INIZIO_VALIDITA DESC ";

			stmt = conn.prepareStatement(search);

			stmt.setString(1, valore);

			SolmrLogger.debug(this, "Executing query: "+search);

			ResultSet rs = stmt.executeQuery();

			while(rs.next())
			{
				AnagAziendaVO aziendaTempt = new AnagAziendaVO();

				aziendaTempt.setIntestazionePartitaIva(rs.getString("INTESTAZIONE_PARTITA_IVA"));
				aziendaTempt.setIdAnagAzienda(new Long(rs.getLong("ID_ANAGRAFICA_AZIENDA")));
				aziendaTempt.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
				aziendaTempt.setDataInizioVal(rs.getDate("DATA_INIZIO_VALIDITA"));
				aziendaTempt.setDataFineVal(rs.getDate("DATA_FINE_VALIDITA"));
				aziendaTempt.setCUAA(rs.getString("ANAG_CUAA"));
				aziendaTempt.setOldCUAA(rs.getString("ANAG_CUAA"));
				aziendaTempt.setPartitaIVA(rs.getString("PARTITA_IVA"));
				aziendaTempt.setDenominazione(rs.getString("ANAG_DENOMINAZIONE"));
				aziendaTempt.setIdUde(checkLongNull(rs.getString("ID_UDE")));
				aziendaTempt.setIdDimensioneAzienda(checkLongNull(rs.getString("ID_DIMENSIONE_AZIENDA")));
				aziendaTempt.setRls(rs.getBigDecimal("RLS"));
				aziendaTempt.setUlu(rs.getBigDecimal("ULU"));
				aziendaTempt.setIntestazionePartitaIva(rs.getString("INTESTAZIONE_PARTITA_IVA"));
				aziendaTempt.setClasseUde(checkLongNull(rs.getString("CLASSE_UDE")));
				aziendaTempt.setDescDimensioneAzienda(rs.getString("DES_DIM_AZIENDA"));
				aziendaTempt.setEsoneroPagamentoGF(rs.getString("ESONERO_PAGAMENTO_GF"));
				aziendaTempt.setDataAggiornamentoUma(rs.getTimestamp("DATA_AGGIORNAMENTO_UMA"));
				aziendaTempt.setFlagIap(rs.getString("FLAG_IAP"));
				aziendaTempt.setDataIscrizioneRea(rs.getTimestamp("DATA_ISCRIZIONE_REA"));
				aziendaTempt.setDataCessazioneRea(rs.getTimestamp("DATA_CESSAZIONE_REA"));
				aziendaTempt.setDataIscrizioneRi(rs.getTimestamp("DATA_ISCRIZIONE_RI"));
				aziendaTempt.setDataCessazioneRi(rs.getTimestamp("DATA_CESSAZIONE_RI"));
				aziendaTempt.setDataInizioAteco(rs.getTimestamp("DATA_INIZIO_ATECO"));

				String codTipoFormaGiur = rs.getString("ALIAS_FORMA_GIURIDICA");
				if(codTipoFormaGiur!=null){
					aziendaTempt.setTipoFormaGiuridica(new CodeDescription(
							new Integer(codTipoFormaGiur),
							rs.getString("ALIAS_DESC_TFG")));
				}
				else{
					aziendaTempt.setTipoFormaGiuridica(new CodeDescription());
				}

				aziendaTempt.setTipiFormaGiuridica(rs.getString("ALIAS_DESC_TFG"));


				String codTipolAzienda = rs.getString("ALIAS_TIPOLOGIA_AZIENDA");
				if(codTipolAzienda!=null){
					aziendaTempt.setTipoTipologiaAzienda(new CodeDescription(
							new Integer(codTipolAzienda),
							rs.getString("ALIAS_DESC_TTA")));
					aziendaTempt.setTipiAzienda(new Integer(codTipolAzienda).toString());
				}
				else{
					aziendaTempt.setTipoTipologiaAzienda(new CodeDescription());
					aziendaTempt.setTipiAzienda("");
				}
				aziendaTempt.setTipiAzienda(rs.getString("ALIAS_TIPOLOGIA_AZIENDA"));
				String codATECO = rs.getString("ALIAS_ATTIVITA_ATECO");
				if(codATECO!=null){
					CodeDescription codeATECO = new CodeDescription(new Integer(codATECO),
							rs.getString("ALIAS_DESC_TAT"));
					codeATECO.setSecondaryCode(rs.getString("ALIAS_CODICE_TAT"));
					aziendaTempt.setTipoAttivitaATECO(codeATECO);
				}
				else
					aziendaTempt.setTipoAttivitaATECO(new CodeDescription());

				String codOTE = rs.getString("ALIAS_ATTIVITA_OTE");
				if(codOTE!=null){
					CodeDescription codeOTE = new CodeDescription(new Integer(codOTE),
							rs.getString("ALIAS_DESC_TOT"));
					codeOTE.setSecondaryCode(rs.getString("ALIAS_CODICE_TOT"));
					aziendaTempt.setTipoAttivitaOTE(codeOTE);
				}
				else
					aziendaTempt.setTipoAttivitaOTE(new CodeDescription());

				aziendaTempt.setProvCompetenza(rs.getString("PROVINCIA_COMPETENZA"));
				aziendaTempt.setProvincePiemonte(rs.getString("PROVINCIA_COMPETENZA"));
				aziendaTempt.setSiglaProvCompetenza(rs.getString("ALIAS_PROV_COMPETENZA"));
				aziendaTempt.setCCIAAprovREA(rs.getString("CCIAA_PROVINCIA_REA"));
				String numeroREA = rs.getString("CCIAA_NUMERO_REA");
				if(numeroREA!=null)
					aziendaTempt.setCCIAAnumeroREA(new Long(numeroREA));
				aziendaTempt.setCCIAAnumRegImprese(rs.getString("CCIAA_NUMERO_REGISTRO_IMPRESE"));
				aziendaTempt.setCCIAAannoIscrizione(rs.getString("CCIAA_ANNO_ISCRIZIONE"));
				aziendaTempt.setStrCCIAAnumeroREA(rs.getString("CCIAA_NUMERO_REA"));
				aziendaTempt.setSedelegComune(rs.getString("SEDELEG_COMUNE"));
				String sedelegDescComune = rs.getString("ALIAS_DESCOM");

				String flagEstero = rs.getString("ALIAS_SEDELE_ESTERO");
				aziendaTempt.setDescrizioneProvCompetenza(rs.getString("ALIAS_DESC_PROV_COMPETENZA"));

				if(flagEstero==null || flagEstero.equals("")){
					aziendaTempt.setSedelegComune("");
					aziendaTempt.setSedelegEstero("");
					aziendaTempt.setStatoEstero("");
					aziendaTempt.setSedelegCittaEstero("");
					aziendaTempt.setSedelegIstatProv("");
					aziendaTempt.setSedelegProv("");
					aziendaTempt.setDescComune("");
					aziendaTempt.setSedelegCAP("");
				}
				else if(flagEstero.equals(SolmrConstants.FLAG_S)){
					aziendaTempt.setSedelegEstero(sedelegDescComune);
					aziendaTempt.setStatoEstero(sedelegDescComune);
					aziendaTempt.setSedelegCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
					aziendaTempt.setSedelegIstatProv("");
					aziendaTempt.setSedelegProv("");
					aziendaTempt.setDescComune("");
					aziendaTempt.setSedelegCAP("");
				}
				else{
					aziendaTempt.setSedelegEstero("");
					aziendaTempt.setSedelegCittaEstero("");
					aziendaTempt.setSedelegIstatProv(rs.getString("ALIAS_ISTAT_PROV"));
					aziendaTempt.setSedelegProv(rs.getString("ALIAS_PROV"));
					aziendaTempt.setDescComune(sedelegDescComune);
					aziendaTempt.setSedelegCAP(rs.getString("SEDELEG_CAP"));
				}
				aziendaTempt.setSedelegIndirizzo(rs.getString("SEDELEG_INDIRIZZO"));

				aziendaTempt.setMail(rs.getString("ANAG_MAIL"));
				aziendaTempt.setPec(rs.getString("PEC"));
				aziendaTempt.setSitoWEB(rs.getString("SITOWEB"));
				aziendaTempt.setNote(rs.getString("NOTE"));
				aziendaTempt.setTelefono(rs.getString("TELEFONO"));
				aziendaTempt.setFax(rs.getString("FAX"));
				aziendaTempt.setDataCessazione(rs.getDate("DATA_CESSAZIONE"));
				aziendaTempt.setCausaleCessazione(rs.getString("CAUSALE_CESSAZIONE"));
				java.util.Date dataAgg = rs.getDate("DATA_AGGIORNAMENTO");
				aziendaTempt.setDataAggiornamento(dataAgg);
				aziendaTempt.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				String motivoModifica = rs.getString("MOTIVO_MODIFICA");
				String utDenominazione = rs.getString("UT_DENOMINAZIONE");
				String utEnteAppart = rs.getString("UT_ENTE_APPART");

				String ultimaModifica = DateUtils.formatDate(dataAgg);
				String tmp="";
				if(utDenominazione!=null && !utDenominazione.equals("")){
					if(tmp.equals(""))
						tmp+=" (";
					tmp+=utDenominazione;
				}
				if(utEnteAppart!=null && !utEnteAppart.equals("")){
					if(tmp.equals(""))
						tmp+=" (";
					else
						tmp+=" - ";
					tmp+=utEnteAppart;
				}
				if(motivoModifica!=null && !motivoModifica.equals("")){
					if(tmp.equals(""))
						tmp+=" (";
					else
						tmp+=" - ";
					tmp+=motivoModifica;
				}
				if(!tmp.equals(""))
					tmp+=")";
				ultimaModifica+=tmp;
				aziendaTempt.setUltimaModifica(ultimaModifica);

				//Ultima Modifica Spezzata
				aziendaTempt.setDataUltimaModifica(dataAgg);
				aziendaTempt.setUtenteUltimaModifica(utDenominazione);
				aziendaTempt.setEnteUltimaModifica(utEnteAppart);
				aziendaTempt.setMotivoModifica(motivoModifica);

				aziendaTempt.setPosizioneSchedario(rs.getString("NUMERO_AGEA"));
				aziendaTempt.setVariazioneUtilizziAmmessa(rs.getString("VARIAZIONE_UTILIZZI_AMMESSA"));

				/**
				 * Questa porzione di codice va a vedere se l'azienda con cui sto
				 * lavorando è un'azienda provvisoria
				 */
				String temp=rs.getString("FLAG_AZIENDA_PROVVISORIA");
				if ("S".equals(temp))
				{
					aziendaTempt.setFlagAziendaProvvisoria(true);
					temp=rs.getString("ID_AZIENDA_PROVENIENZA");
					if (temp!=null)
						aziendaTempt.setIdAziendaSubentro(new Long(temp));
				}
				else
					aziendaTempt.setFlagAziendaProvvisoria(false);
				
				aziendaTempt.setFascicoloDematerializzato(rs.getString("FASCICOLO_DEMATERIALIZZATO"));

				result.add(aziendaTempt);
			}

			rs.close();
			stmt.close();

			//if (result == null)
				//throw new NotFoundException();

		} catch (SQLException exc) {
			SolmrLogger.fatal(this, "getAziendaByCriterio - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} /*catch (NotFoundException nfexc) {
			SolmrLogger.fatal(this, "getAziendaByCriterio - NotFoundException: "+nfexc.getMessage());
			throw nfexc;
		}*/ catch (Exception ex) {
			SolmrLogger.fatal(this, "getAziendaByCriterio - Generic Exception: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, "getAziendaByCriterio - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "getAziendaByCriterio - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return result;
	}


	public AnagAziendaVO getAziendaByCriterio(String criterio, String valore, 
	    java.util.Date dataSituazioneAl) 
	throws DataAccessException, NotFoundException, SolmrException 
	{
		AnagAziendaVO result = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		try 
		{
			conn = getDatasource().getConnection();

			String search = 
			"SELECT   AA.ID_ANAGRAFICA_AZIENDA, " +
			"         AA.ID_AZIENDA, "+
			"         AA.DATA_INIZIO_VALIDITA, " +
			"         AA.DATA_FINE_VALIDITA, "+
			"         AA.CUAA ANAG_CUAA, " +
			"         AA.PARTITA_IVA, " +
			"         AA.DENOMINAZIONE ANAG_DENOMINAZIONE,  "+
			"         AA.ID_FORMA_GIURIDICA ALIAS_FORMA_GIURIDICA, "+
			"         AA.ID_TIPOLOGIA_AZIENDA ALIAS_TIPOLOGIA_AZIENDA, "+
			"         AA.ID_ATTIVITA_ATECO ALIAS_ATTIVITA_ATECO, "+
			"         AA.ID_ATTIVITA_OTE ALIAS_ATTIVITA_OTE, "+
			"         AA.ID_UDE, " +
			"         AA.ID_DIMENSIONE_AZIENDA, " +
			"         AA.RLS, " +
			"         AA.ULU, " +
			"         AA.INTESTAZIONE_PARTITA_IVA, " +
			"         TU.CLASSE_UDE, " +
			"         TDA.DESCRIZIONE AS DES_DIM_AZIENDA, " +
			"         AA.ESONERO_PAGAMENTO_GF, " +
			"         TFG.DESCRIZIONE ALIAS_DESC_TFG, "+
			"         TTA.DESCRIZIONE ALIAS_DESC_TTA, "+
			"         TAT.DESCRIZIONE ALIAS_DESC_TAT, "+
			"         TOT.DESCRIZIONE ALIAS_DESC_TOT, "+
			"         TAT.CODICE ALIAS_CODICE_TAT, " +
			"         TOT.CODICE ALIAS_CODICE_TOT, "+
			"         AA.PROVINCIA_COMPETENZA, " +
			"         P2.SIGLA_PROVINCIA ALIAS_PROV_COMPETENZA, "+
			"         AA.CCIAA_PROVINCIA_REA, " +
			"         AA.CCIAA_NUMERO_REA, "+
			"         AA.CCIAA_NUMERO_REGISTRO_IMPRESE, " +
			"         AA.CCIAA_ANNO_ISCRIZIONE, "+
			"         P1.ISTAT_PROVINCIA ALIAS_ISTAT_PROV, "+
			"         P1.SIGLA_PROVINCIA ALIAS_PROV, " +
			"         AA.SEDELEG_COMUNE, " +
			"         COMUNE.FLAG_ESTERO ALIAS_SEDELE_ESTERO, "+
			"         COMUNE.DESCOM ALIAS_DESCOM, " +
			"         AA.SEDELEG_INDIRIZZO, " +
			"         AA.SEDELEG_CAP, "+
			"         AA.SEDELEG_CITTA_ESTERO, "+
			"         AA.MAIL ANAG_MAIL, " +
			"         AA.SITOWEB, " +
			"         AA.NOTE, " +
			"         AA.DATA_CESSAZIONE, "+
			"         AA.CAUSALE_CESSAZIONE, " +
			"         AA.DATA_AGGIORNAMENTO, "+
			"         AA.ID_UTENTE_AGGIORNAMENTO, " +
			"         AA.MOTIVO_MODIFICA, "+
			"  (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
		    "          || ' ' " + 
		    "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
		    "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
		    "         WHERE AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
			"         AS UT_DENOMINAZIONE, " +
			"         (SELECT PVU.DENOMINAZIONE " +
		    "          FROM PAPUA_V_UTENTE_LOGIN PVU " +
		    "          WHERE AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
			"         AS UT_ENTE_APPART, "+
			"         P2.DESCRIZIONE ALIAS_DESC_PROV_COMPETENZA, "+
			"         AA.NUMERO_AGEA, "+
			"         A.FASCICOLO_DEMATERIALIZZATO, " +
			"         A.FLAG_AZIENDA_PROVVISORIA, "+
			"         A.ID_AZIENDA_PROVENIENZA, "+
			"         A.VARIAZIONE_UTILIZZI_AMMESSA, " +
			"         AA.INTESTAZIONE_PARTITA_IVA," +
			"         AA.DATA_AGGIORNAMENTO_UMA, " +
			"         AA.FLAG_IAP, " +
			"         AA.DATA_ISCRIZIONE_REA, " +
      "         AA.DATA_CESSAZIONE_REA, " +
      "         AA.DATA_ISCRIZIONE_RI, " +
      "         AA.DATA_CESSAZIONE_RI, " +
      "         AA.DATA_INIZIO_ATECO " +
			"FROM     DB_ANAGRAFICA_AZIENDA AA, "+
			"         DB_TIPO_FORMA_GIURIDICA TFG, "+
			"         DB_TIPO_TIPOLOGIA_AZIENDA TTA, "+
			"         DB_TIPO_ATTIVITA_ATECO TAT, "+
			"         DB_TIPO_ATTIVITA_OTE TOT, "+
			"         COMUNE, " +
			"         PROVINCIA P1, " +
			"         PROVINCIA P2, " +
		//	"         PAPUA_V_UTENTE_LOGIN PVU, "+
			"         DB_AZIENDA A, " +
			"         DB_TIPO_UDE TU," +
			"         DB_TIPO_DIMENSIONE_AZIENDA TDA " +  
			"WHERE    AA.SEDELEG_COMUNE = COMUNE.ISTAT_COMUNE(+) "+
			"AND      COMUNE.ISTAT_PROVINCIA = P1.ISTAT_PROVINCIA(+) "+
			"AND      AA.PROVINCIA_COMPETENZA = P2.ISTAT_PROVINCIA(+) "+
			"AND      AA.ID_FORMA_GIURIDICA = TFG.ID_FORMA_GIURIDICA(+) "+
			"AND      AA.ID_TIPOLOGIA_AZIENDA = TTA.ID_TIPOLOGIA_AZIENDA(+) "+
			"AND      AA.ID_ATTIVITA_ATECO = TAT.ID_ATTIVITA_ATECO(+) "+
			"AND      AA.ID_ATTIVITA_OTE = TOT.ID_ATTIVITA_OTE(+) "+
			"AND      AA.ID_UDE = TU.ID_UDE(+) " +
			"AND      AA.ID_DIMENSIONE_AZIENDA = TDA.ID_DIMENSIONE_AZIENDA(+) " +   
		//	"AND      AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN "+
			"AND      AA.DATA_INIZIO_VALIDITA  <= ? "+
			"AND      (AA.DATA_FINE_VALIDITA IS NULL "+
			"          OR AA.DATA_FINE_VALIDITA >= ?) "+
			"AND      AA."+criterio+" = UPPER(?) "+
			"AND      A.ID_AZIENDA = AA.ID_AZIENDA " +
			"ORDER BY AA.DATA_INIZIO_VALIDITA DESC ";

			stmt = conn.prepareStatement(search);

			stmt.setDate(1, new java.sql.Date(dataSituazioneAl.getTime()));
			stmt.setDate(2, new java.sql.Date(dataSituazioneAl.getTime()));
			stmt.setString(3, valore);

			SolmrLogger.debug(this, "Executing query: "+search);

			ResultSet rs = stmt.executeQuery();

			if (rs != null)
			{
				if(rs.next())
				{
					result = new AnagAziendaVO();

					result.setIntestazionePartitaIva(rs.getString("INTESTAZIONE_PARTITA_IVA"));
					result.setIdAnagAzienda(new Long(rs.getLong("ID_ANAGRAFICA_AZIENDA")));
					result.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
					result.setDataInizioVal(rs.getDate("DATA_INIZIO_VALIDITA"));
					result.setDataFineVal(rs.getDate("DATA_FINE_VALIDITA"));
					result.setCUAA(rs.getString("ANAG_CUAA"));
					result.setOldCUAA(rs.getString("ANAG_CUAA"));
					result.setPartitaIVA(rs.getString("PARTITA_IVA"));
					result.setDenominazione(rs.getString("ANAG_DENOMINAZIONE"));
					result.setIdUde(checkLongNull(rs.getString("ID_UDE")));
					result.setIdDimensioneAzienda(checkLongNull(rs.getString("ID_DIMENSIONE_AZIENDA")));
					result.setRls(rs.getBigDecimal("RLS"));
					result.setUlu(rs.getBigDecimal("ULU"));
					result.setIntestazionePartitaIva(rs.getString("INTESTAZIONE_PARTITA_IVA"));
					result.setClasseUde(checkLongNull(rs.getString("CLASSE_UDE")));
					result.setDescDimensioneAzienda(rs.getString("DES_DIM_AZIENDA"));
					result.setEsoneroPagamentoGF(rs.getString("ESONERO_PAGAMENTO_GF"));
					result.setDataAggiornamentoUma(rs.getTimestamp("DATA_AGGIORNAMENTO_UMA"));
					result.setFlagIap(rs.getString("FLAG_IAP"));
					result.setDataIscrizioneRea(rs.getTimestamp("DATA_ISCRIZIONE_REA"));
	        result.setDataCessazioneRea(rs.getTimestamp("DATA_CESSAZIONE_REA"));
	        result.setDataIscrizioneRi(rs.getTimestamp("DATA_ISCRIZIONE_RI"));
	        result.setDataCessazioneRi(rs.getTimestamp("DATA_CESSAZIONE_RI"));
	        result.setDataInizioAteco(rs.getTimestamp("DATA_INIZIO_ATECO"));

					String codTipoFormaGiur = rs.getString("ALIAS_FORMA_GIURIDICA");
					if(codTipoFormaGiur!=null){
						result.setTipoFormaGiuridica(new CodeDescription(
								new Integer(codTipoFormaGiur),
								rs.getString("ALIAS_DESC_TFG")));
					}
					else{
						result.setTipoFormaGiuridica(new CodeDescription());
					}

					result.setTipiFormaGiuridica(rs.getString("ALIAS_DESC_TFG"));


					String codTipolAzienda = rs.getString("ALIAS_TIPOLOGIA_AZIENDA");
					if(codTipolAzienda!=null){
						result.setTipoTipologiaAzienda(new CodeDescription(
								new Integer(codTipolAzienda),
								rs.getString("ALIAS_DESC_TTA")));
						result.setTipiAzienda(new Integer(codTipolAzienda).toString());
					}
					else{
						result.setTipoTipologiaAzienda(new CodeDescription());
						result.setTipiAzienda("");
					}
					result.setTipiAzienda(rs.getString("ALIAS_TIPOLOGIA_AZIENDA"));
					String codATECO = rs.getString("ALIAS_ATTIVITA_ATECO");
					if(codATECO!=null){
						CodeDescription codeATECO = new CodeDescription(new Integer(codATECO),
								rs.getString("ALIAS_DESC_TAT"));
						codeATECO.setSecondaryCode(rs.getString("ALIAS_CODICE_TAT"));
						result.setTipoAttivitaATECO(codeATECO);
					}
					else
						result.setTipoAttivitaATECO(new CodeDescription());

					String codOTE = rs.getString("ALIAS_ATTIVITA_OTE");
					if(codOTE!=null){
						CodeDescription codeOTE = new CodeDescription(new Integer(codOTE),
								rs.getString("ALIAS_DESC_TOT"));
						codeOTE.setSecondaryCode(rs.getString("ALIAS_CODICE_TOT"));
						result.setTipoAttivitaOTE(codeOTE);
					}
					else
						result.setTipoAttivitaOTE(new CodeDescription());

					result.setProvCompetenza(rs.getString("PROVINCIA_COMPETENZA"));
					result.setProvincePiemonte(rs.getString("PROVINCIA_COMPETENZA"));
					result.setSiglaProvCompetenza(rs.getString("ALIAS_PROV_COMPETENZA"));
					result.setCCIAAprovREA(rs.getString("CCIAA_PROVINCIA_REA"));
					String numeroREA = rs.getString("CCIAA_NUMERO_REA");
					if(numeroREA!=null)
						result.setCCIAAnumeroREA(new Long(numeroREA));
					result.setCCIAAnumRegImprese(rs.getString("CCIAA_NUMERO_REGISTRO_IMPRESE"));
					result.setCCIAAannoIscrizione(rs.getString("CCIAA_ANNO_ISCRIZIONE"));
					result.setStrCCIAAnumeroREA(rs.getString("CCIAA_NUMERO_REA"));
					result.setSedelegComune(rs.getString("SEDELEG_COMUNE"));
					String sedelegDescComune = rs.getString("ALIAS_DESCOM");

					String flagEstero = rs.getString("ALIAS_SEDELE_ESTERO");
					result.setDescrizioneProvCompetenza(rs.getString("ALIAS_DESC_PROV_COMPETENZA"));

					if(flagEstero==null || flagEstero.equals("")){
						result.setSedelegComune("");
						result.setSedelegEstero("");
						result.setStatoEstero("");
						result.setSedelegCittaEstero("");
						result.setSedelegIstatProv("");
						result.setSedelegProv("");
						result.setDescComune("");
						result.setSedelegCAP("");
					}
					else if(flagEstero.equals(SolmrConstants.FLAG_S)){
						result.setSedelegEstero(sedelegDescComune);
						result.setStatoEstero(sedelegDescComune);
						result.setSedelegCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
						result.setSedelegIstatProv("");
						result.setSedelegProv("");
						result.setDescComune("");
						result.setSedelegCAP("");
					}
					else{
						result.setSedelegEstero("");
						result.setSedelegCittaEstero("");
						result.setSedelegIstatProv(rs.getString("ALIAS_ISTAT_PROV"));
						result.setSedelegProv(rs.getString("ALIAS_PROV"));
						result.setDescComune(sedelegDescComune);
						result.setSedelegCAP(rs.getString("SEDELEG_CAP"));
					}
					result.setSedelegIndirizzo(rs.getString("SEDELEG_INDIRIZZO"));

					result.setMail(rs.getString("ANAG_MAIL"));
					result.setSitoWEB(rs.getString("SITOWEB"));
					result.setNote(rs.getString("NOTE"));
					result.setDataCessazione(rs.getDate("DATA_CESSAZIONE"));
					result.setCausaleCessazione(rs.getString("CAUSALE_CESSAZIONE"));
					java.util.Date dataAgg = rs.getDate("DATA_AGGIORNAMENTO");
					result.setDataAggiornamento(dataAgg);
					result.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
					String motivoModifica = rs.getString("MOTIVO_MODIFICA");
					String utDenominazione = rs.getString("UT_DENOMINAZIONE");
					String utEnteAppart = rs.getString("UT_ENTE_APPART");

					String ultimaModifica = DateUtils.formatDate(dataAgg);
					String tmp="";
					if(utDenominazione!=null && !utDenominazione.equals("")){
						if(tmp.equals(""))
							tmp+=" (";
						tmp+=utDenominazione;
					}
					if(utEnteAppart!=null && !utEnteAppart.equals("")){
						if(tmp.equals(""))
							tmp+=" (";
						else
							tmp+=" - ";
						tmp+=utEnteAppart;
					}
					if(motivoModifica!=null && !motivoModifica.equals("")){
						if(tmp.equals(""))
							tmp+=" (";
						else
							tmp+=" - ";
						tmp+=motivoModifica;
					}
					if(!tmp.equals(""))
						tmp+=")";
					ultimaModifica+=tmp;
					result.setUltimaModifica(ultimaModifica);

					//Ultima Modifica Spezzata
					result.setDataUltimaModifica(dataAgg);
					result.setUtenteUltimaModifica(utDenominazione);
					result.setEnteUltimaModifica(utEnteAppart);
					result.setMotivoModifica(motivoModifica);

					result.setDataSituazioneAlStr(DateUtils.formatDate(dataSituazioneAl));
					result.setPosizioneSchedario(rs.getString("NUMERO_AGEA"));
					result.setVariazioneUtilizziAmmessa(rs.getString("VARIAZIONE_UTILIZZI_AMMESSA"));

					/**
					 * Questa porzione di codice va a vedere se l'azienda con cui sto
					 * lavorando è un'azienda provvisoria
					 */
					String temp=rs.getString("FLAG_AZIENDA_PROVVISORIA");
					if ("S".equals(temp))
					{
						result.setFlagAziendaProvvisoria(true);
						temp=rs.getString("ID_AZIENDA_PROVENIENZA");
						if (temp!=null)
							result.setIdAziendaSubentro(new Long(temp));
					}
					else
						result.setFlagAziendaProvvisoria(false);
					
					result.setFascicoloDematerializzato(rs.getString("FASCICOLO_DEMATERIALIZZATO"));

				}
				if(result == null) {
					throw new SolmrException(AnagErrors.NESSUNA_AZIENDA_TROVATA);
				}
			} else
				throw new DataAccessException();

			rs.close();
			stmt.close();

			//if (result == null)
				//throw new NotFoundException();

			SolmrLogger.debug(this, "Executed query - Found record with primary key: "+result.getIdAnagAzienda());
		} catch (SQLException exc) {
			SolmrLogger.fatal(this, "getAziendaByCriterio - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} catch (DataAccessException daexc) {
			SolmrLogger.fatal(this, "getAziendaByCriterio - ResultSet null");
			throw daexc;
		} /*catch (NotFoundException nfexc) {
			SolmrLogger.fatal(this, "getAziendaByCriterio - NotFoundException: "+nfexc.getMessage());
			throw nfexc;
		}*/ catch (SolmrException exc) {
			SolmrLogger.debug(this, "getAziendaByCriterio - SolmrException: "+exc.getMessage());
			throw exc;
		} catch (Exception ex) {
			SolmrLogger.fatal(this, "getAziendaByCriterio - Generic Exception: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, "getAziendaByCriterio - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "getAziendaByCriterio - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return result;
	}


	/**
	 *
	 * @param anagAziendaVO AnagAziendaVO
	 * @param dataSituazioneAl Date
	 * @param attivita String
	 * @param schedario è stato aggiunto in seguito perchè viene utilizzato da
	 *   vitivinicolo
	 * @param isIdAzienda se vale false indica che la ricerca va fatta su
	 *   ID_ANAGRAFICA_AZIENDA, se invece vale true indica che la ricerca va
	 *   fatta su ID_AZIENDA
	 * @return java.util.Vector
	 * @throws DataAccessException
	 * @throws DataControlException
	 */
	public Vector<Long> getListIdAziende(AnagAziendaVO anagAziendaVO,
			java.util.Date dataSituazioneAl,
			String attivita,
			boolean schedario,
			boolean isIdAzienda)
	throws DataAccessException, DataControlException
	{

		Vector<Long> result = new Vector<Long>();
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getDatasource().getConnection();

			String search = null;

			if (isIdAzienda)
			{
				search = "SELECT ID_AZIENDA"+
				"  FROM DB_ANAGRAFICA_AZIENDA AA, COMUNE C, PROVINCIA P"+
				" WHERE AA.SEDELEG_COMUNE = C.ISTAT_COMUNE(+) "+
				"   AND C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) ";
			}
			else
			{
				search = "SELECT ID_ANAGRAFICA_AZIENDA"+
				"  FROM DB_ANAGRAFICA_AZIENDA AA, COMUNE C, PROVINCIA P"+
				" WHERE AA.SEDELEG_COMUNE = C.ISTAT_COMUNE(+) "+
				"   AND C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) ";
			}



			/**
			 * Utilizzato dai servizi di anag
			 * */
			if (dataSituazioneAl==null)
				search+="   AND AA.DATA_FINE_VALIDITA IS NULL ";
			else
			{
				search+="   AND AA.DATA_INIZIO_VALIDITA  <= ? "+
				"   AND (AA.DATA_FINE_VALIDITA IS NULL "+
				"       OR AA.DATA_FINE_VALIDITA >= ?)";
			}

			Vector<String> altriParamVector = new Vector<String>();


			/************************************************************************
			 ************************************************************************
			 ************************************************************************
			 * Ho aggiunto il .toUpperCase perchè dato che questo metodo
			 * verrà utilizzato come servizio non posso obblicìgare i client a
			 * passarmi i parametri della ricerca maiuscoli. Non ho creato dei metodi
			 * ad hoc sul VO per non appesantirlo ulteriormente
			 ************************************************************************
			 ************************************************************************
			 ************************************************************************
			 */



			if (anagAziendaVO.getPartitaIVA()!= null && !"".equals(anagAziendaVO.getPartitaIVA()))
			{
				search+=" AND AA.PARTITA_IVA = ? ";
				altriParamVector.addElement(anagAziendaVO.getPartitaIVA());
			}
			if (anagAziendaVO.getCUAA()!= null && !"".equals(anagAziendaVO.getCUAA()))
			{
				search+=" AND AA.CUAA = ? ";
				altriParamVector.addElement(anagAziendaVO.getCUAA().toUpperCase());
			}
			if (anagAziendaVO.isDenominazioneInitialized())
			{
				//search+=" AND UPPER(DENOMINAZIONE) LIKE UPPER('" + anagAziendaVO.getDenominazione() + "%') ";
				search+=" AND AA.DENOMINAZIONE LIKE UPPER(?) ";
				//stmt.setString(iParam, anagAziendaVO.getDenominazione()+"%");
				altriParamVector.addElement(anagAziendaVO.getDenominazione().trim().toUpperCase()+"%");
			}
			if (anagAziendaVO.isSedelegProvInitialized()){
				//search+=" AND SIGLA_PROVINCIA = '" + anagAziendaVO.getSedelegProv()+"' ";
				search+=" AND P.SIGLA_PROVINCIA = UPPER(?) ";
				//stmt.setString(iParam, anagAziendaVO.getSedelegProv());
				altriParamVector.addElement(anagAziendaVO.getSedelegProv().toUpperCase());
			}
			/**
			 * Ricerca puntuale sull'istat del comune
			 */
			if (anagAziendaVO.getSedelegComune()!= null && !"".equals(anagAziendaVO.getSedelegComune()))
			{
				search+=" AND AA.SEDELEG_COMUNE = ? ";
				altriParamVector.addElement(anagAziendaVO.getSedelegComune());
			}
			/**
			 * Ricerca sulla descrizione del comune
			 */
			if (anagAziendaVO.isDescComuneInitialized()){
				//search+=" AND UPPER(DESCOM) = UPPER('" + anagAziendaVO.getDescComune() + "')";
				search+=" AND C.DESCOM = UPPER(?)";
				//stmt.setString(iParam, anagAziendaVO.getDescComune());
				altriParamVector.addElement(anagAziendaVO.getDescComune().toUpperCase());
			}
			if (anagAziendaVO.isTipoFormaGiuridicaInitialized())
				search+=" AND AA.ID_FORMA_GIURIDICA = " + anagAziendaVO.getTipoFormaGiuridica().getCode().toString();
			if (anagAziendaVO.isTipoAziendaInitialized())
				search+=" AND AA.ID_TIPOLOGIA_AZIENDA = " + anagAziendaVO.getTipiAzienda();

			if("A".equals(attivita))
				search+= " AND AA.DATA_CESSAZIONE IS NULL ";
			if("C".equals(attivita))
				search+= " AND AA.DATA_CESSAZIONE IS NOT NULL ";

			if (anagAziendaVO.getPosizioneSchedario()!= null && !"".equals(anagAziendaVO.getPosizioneSchedario()))
			{
				search+=" AND AA.NUMERO_AGEA = ? ";
				altriParamVector.addElement(anagAziendaVO.getPosizioneSchedario());
			}
			if (schedario)
			{
				search+=" AND AA.NUMERO_AGEA IS NOT NULL ";
			}

			search += " ORDER BY AA.DENOMINAZIONE ";
			stmt = conn.prepareStatement(search);

			if (dataSituazioneAl!=null)
			{
				stmt.setDate(1, new java.sql.Date(dataSituazioneAl.getTime()));
				stmt.setDate(2, new java.sql.Date(dataSituazioneAl.getTime()));
				for(int i=0; i<altriParamVector.size(); i++)
					stmt.setString(i+3, (String)altriParamVector.elementAt(i));
			}
			else
			{
				for(int i=0; i<altriParamVector.size(); i++)
					stmt.setString(i+1, (String)altriParamVector.elementAt(i));
			}
			SolmrLogger.debug(this, "Executing query: "+search);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				result.add(new Long(rs.getLong(1)));
				if(rs.getRow()>SolmrConstants.NUM_MAX_ROWS_RESULT)
					throw new DataControlException(ErrorTypes.STR_MAX_RECORD);
			}

			rs.close();
			stmt.close();

			SolmrLogger.debug(this, "Executed query - Found "+result.size()+" result(s).");
		} catch (SQLException exc) {
			SolmrLogger.fatal(this, "getListIdAziende - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} catch (DataControlException exc) {
			SolmrLogger.fatal(this, "getListIdAziende - DataControlException: "+exc.getMessage());
			throw exc;
		} catch (Exception ex) {
			SolmrLogger.fatal(this, "getListIdAziende - Generic Exception: "+ex+" with message: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, "getListIdAziende - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "getListIdAziende - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return result;
	}


	/**
	 *
	 * @param anagAziendaVO AnagAziendaVO
	 * @param dataSituazioneAl Date
	 * @param attivitaBool boolean
	 * @param schedario è stato aggiunto in seguito perchè viene utilizzato da
	 *   vitivinicolo
	 * @param isIdAzienda se vale false indica che la ricerca va fatta su
	 *   ID_ANAGRAFICA_AZIENDA, se invece vale true indica che la ricerca va
	 *   fatta su ID_AZIENDA
	 * @return java.util.Vector
	 * @throws DataAccessException
	 * @throws DataControlException
	 */
	public Vector<Long> getListIdAziende(AnagAziendaVO anagAziendaVO,
			java.util.Date dataSituazioneAl,
			boolean attivitaBool,
			boolean schedario,
			boolean isIdAzienda)
	throws DataAccessException, DataControlException
	{

		Vector<Long> result = new Vector<Long>();
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getDatasource().getConnection();

			String search = null;

			if (isIdAzienda)
			{
				search = "SELECT ID_AZIENDA"+
				"  FROM DB_ANAGRAFICA_AZIENDA AA, COMUNE C, PROVINCIA P"+
				" WHERE AA.SEDELEG_COMUNE = C.ISTAT_COMUNE(+) "+
				"   AND C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) ";
			}
			else
			{
				search = "SELECT ID_ANAGRAFICA_AZIENDA"+
				"  FROM DB_ANAGRAFICA_AZIENDA AA, COMUNE C, PROVINCIA P"+
				" WHERE AA.SEDELEG_COMUNE = C.ISTAT_COMUNE(+) "+
				"   AND C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) ";
			}



			/**
			 * Utilizzato dai servizi di anag
			 * */
			if (dataSituazioneAl==null)
				search+="   AND AA.DATA_FINE_VALIDITA IS NULL ";
			else
			{
				search+="   AND AA.DATA_INIZIO_VALIDITA  <= ? "+
				"   AND (AA.DATA_FINE_VALIDITA IS NULL "+
				"       OR AA.DATA_FINE_VALIDITA >= ?)";
			}

			Vector<String> altriParamVector = new Vector<String>();


			/************************************************************************
			 ************************************************************************
			 ************************************************************************
			 * Ho aggiunto il .toUpperCase perchè dato che questo metodo
			 * verrà utilizzato come servizio non posso obblicìgare i client a
			 * passarmi i parametri della ricerca maiuscoli. Non ho creato dei metodi
			 * ad hoc sul VO per non appesantirlo ulteriormente
			 ************************************************************************
			 ************************************************************************
			 ************************************************************************
			 */



			if (anagAziendaVO.getPartitaIVA()!= null && !"".equals(anagAziendaVO.getPartitaIVA()))
			{
				search+=" AND AA.PARTITA_IVA = ? ";
				altriParamVector.addElement(anagAziendaVO.getPartitaIVA());
			}
			if (anagAziendaVO.getCUAA()!= null && !"".equals(anagAziendaVO.getCUAA()))
			{
				search+=" AND AA.CUAA = ? ";
				altriParamVector.addElement(anagAziendaVO.getCUAA().toUpperCase());
			}
			if (anagAziendaVO.isDenominazioneInitialized())
			{
				//search+=" AND UPPER(DENOMINAZIONE) LIKE UPPER('" + anagAziendaVO.getDenominazione() + "%') ";
				search+=" AND AA.DENOMINAZIONE LIKE UPPER(?) ";
				//stmt.setString(iParam, anagAziendaVO.getDenominazione()+"%");
				altriParamVector.addElement(anagAziendaVO.getDenominazione().trim().toUpperCase()+"%");
			}
			if (anagAziendaVO.isSedelegProvInitialized()){
				//search+=" AND SIGLA_PROVINCIA = '" + anagAziendaVO.getSedelegProv()+"' ";
				search+=" AND P.SIGLA_PROVINCIA = UPPER(?) ";
				//stmt.setString(iParam, anagAziendaVO.getSedelegProv());
				altriParamVector.addElement(anagAziendaVO.getSedelegProv().toUpperCase());
			}
			/**
			 * Ricerca puntuale sull'istat del comune
			 */
			if (anagAziendaVO.getSedelegComune()!= null && !"".equals(anagAziendaVO.getSedelegComune()))
			{
				search+=" AND AA.SEDELEG_COMUNE = ? ";
				altriParamVector.addElement(anagAziendaVO.getSedelegComune());
			}
			/**
			 * Ricerca sulla descrizione del comune
			 */
			if (anagAziendaVO.isDescComuneInitialized()){
				//search+=" AND UPPER(DESCOM) = UPPER('" + anagAziendaVO.getDescComune() + "')";
				search+=" AND C.DESCOM = UPPER(?)";
				//stmt.setString(iParam, anagAziendaVO.getDescComune());
				altriParamVector.addElement(anagAziendaVO.getDescComune().toUpperCase());
			}
			if (anagAziendaVO.isTipoFormaGiuridicaInitialized())
				search+=" AND AA.ID_FORMA_GIURIDICA = " + anagAziendaVO.getTipoFormaGiuridica().getCode().toString();
			if (anagAziendaVO.isTipoAziendaInitialized())
				search+=" AND AA.ID_TIPOLOGIA_AZIENDA = " + anagAziendaVO.getTipiAzienda();

			if(attivitaBool)
				search+= " AND AA.DATA_CESSAZIONE IS NULL ";

			if (anagAziendaVO.getPosizioneSchedario()!= null && !"".equals(anagAziendaVO.getPosizioneSchedario()))
			{
				search+=" AND AA.NUMERO_AGEA = ? ";
				altriParamVector.addElement(anagAziendaVO.getPosizioneSchedario());
			}
			if (schedario)
			{
				search+=" AND AA.NUMERO_AGEA IS NOT NULL ";
			}

			search += " ORDER BY AA.DENOMINAZIONE ";
			stmt = conn.prepareStatement(search);

			if (dataSituazioneAl!=null)
			{
				stmt.setDate(1, new java.sql.Date(dataSituazioneAl.getTime()));
				stmt.setDate(2, new java.sql.Date(dataSituazioneAl.getTime()));
				for(int i=0; i<altriParamVector.size(); i++)
					stmt.setString(i+3, (String)altriParamVector.elementAt(i));
			}
			else
			{
				for(int i=0; i<altriParamVector.size(); i++)
					stmt.setString(i+1, (String)altriParamVector.elementAt(i));
			}
			SolmrLogger.debug(this, "--  query getListIdAziende ="+search);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				result.add(new Long(rs.getLong(1)));
				if(rs.getRow()>SolmrConstants.NUM_MAX_ROWS_RESULT)
					throw new DataControlException(ErrorTypes.STR_MAX_RECORD);
			}

			rs.close();
			stmt.close();

			SolmrLogger.debug(this, "Executed query - Found "+result.size()+" result(s).");
		} catch (SQLException exc) {
			SolmrLogger.fatal(this, "getListIdAziende - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} catch (DataControlException exc) {
			SolmrLogger.fatal(this, "getListIdAziende - DataControlException: "+exc.getMessage());
			throw exc;
		} catch (Exception ex) {
			SolmrLogger.fatal(this, "getListIdAziende - Generic Exception: "+ex+" with message: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, "getListIdAziende - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "getListIdAziende - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return result;
	}

	/*
  Il parametro isIdAzienda se vale false indica che la ricerca va fatta
  su ID_ANAGRAFICA_AZIENDA, se invece vale true indica che la ricerca va
  fatta su ID_AZIENDA
	 */
	public Vector<AnagAziendaVO> getListAziendeByIdRange(Vector<Long> collIdAziende,boolean isIdAzienda,Date dataSituazioneAl) throws DataAccessException, NotFoundException, SolmrException {

		Vector<AnagAziendaVO> result = null;
		Connection conn = null;
		AnagAziendaVO anagAziendaVO;
		PreparedStatement stmt = null;
		try {
			conn = getDatasource().getConnection();

			if(collIdAziende == null || collIdAziende.isEmpty())
				throw new SolmrException(AnagErrors.NESSUNA_AZIENDA_TROVATA);

			/*
			 * 22/02/2006
			 * Modificata la costruzione della query,
			 * con passaggio da una concatenazione di UNION tra query con DB_AZIENDA = ?
			 * ad una concatenazione di UNION tra query con DB_AZIENDA IN (...)
			 * limitando la lunghezza dell'elenco tra parentesi ad un certo numero di elementi
			 * (il limite esplicitamente riportato da Oracle è 1000)
			 *
			 * Aggiunto l'hint per la forzatura dell'utilizzo dell'indice su chiave primaria
			 * della tabella DB_AZIENDA, poiché da un certo numero di elementi della lista IN in su
			 * l'ottimizzatore di Oracle decide (a mio avviso abbastanza inspiegabilmente, n.d.M.)
			 * di non usarlo più.
			 * La sintassi dell'hint prevede l'utilizzo dell'alias della tabella (se questa ha un alias nella query)
			 */
			String daQuery = 
			"SELECT AA.ID_ANAGRAFICA_AZIENDA, " +
			"         AA.ID_AZIENDA, "+
			"         AA.DATA_INIZIO_VALIDITA, " +
			"         AA.DATA_FINE_VALIDITA, "+
			"         AA.CUAA ANAG_CUAA, " +
			"         AA.PARTITA_IVA, " +
			"         AA.DENOMINAZIONE ANAG_DENOMINAZIONE,  "+
			"         AA.ID_FORMA_GIURIDICA ALIAS_FORMA_GIURIDICA, "+
			"         AA.ID_TIPOLOGIA_AZIENDA ALIAS_TIPOLOGIA_AZIENDA, "+
			"         AA.ID_ATTIVITA_ATECO ALIAS_ATTIVITA_ATECO, "+
			"         AA.ID_ATTIVITA_OTE ALIAS_ATTIVITA_OTE, " +
			"         AA.ID_UDE, " +
			"         AA.ID_DIMENSIONE_AZIENDA, " +
			"         AA.RLS, " +
			"         AA.ULU AS ULU, " +
			"         TU.CLASSE_UDE, " +
			"         TDA.DESCRIZIONE AS DESC_DIM_AZIENDA, "+
			"         AA.ESONERO_PAGAMENTO_GF, " +
			"         TFG.DESCRIZIONE ALIAS_DESC_TFG, "+
			"         TTA.DESCRIZIONE ALIAS_DESC_TTA, "+
			"         TTA.FLAG_FORMA_ASSOCIATA, "+
			"         TAT.DESCRIZIONE ALIAS_DESC_TAT, "+
			"         TOT.DESCRIZIONE ALIAS_DESC_TOT, "+
			"         TAT.CODICE ALIAS_CODICE_TAT, " +
			"         TOT.CODICE ALIAS_CODICE_TOT, "+
			"         AA.PROVINCIA_COMPETENZA, " +
			"         P2.SIGLA_PROVINCIA ALIAS_PROV_COMPETENZA, "+
			"         AA.CCIAA_PROVINCIA_REA, " +
			"         AA.CCIAA_NUMERO_REA, "+
			"         AA.CCIAA_NUMERO_REGISTRO_IMPRESE, " +
			"         AA.CCIAA_ANNO_ISCRIZIONE, "+
			"         P1.ISTAT_PROVINCIA ALIAS_ISTAT_PROV, "+
			"         P1.SIGLA_PROVINCIA ALIAS_PROV, " +
			"         AA.SEDELEG_COMUNE, " +
			"         COMUNE.FLAG_ESTERO ALIAS_SEDELE_ESTERO, "+
			"         COMUNE.DESCOM ALIAS_DESCOM, " +
			"         AA.SEDELEG_INDIRIZZO, " +
			"         AA.SEDELEG_CAP, "+
			"         AA.SEDELEG_CITTA_ESTERO, "+
			"         AA.MAIL ANAG_MAIL, " +
			"         AA.SITOWEB, " +
			"         AA.NOTE, " +
			"         AA.DATA_CESSAZIONE, "+
			"         AA.CAUSALE_CESSAZIONE, " +
			"         AA.DATA_AGGIORNAMENTO, "+
			"         AA.ID_UTENTE_AGGIORNAMENTO, " +
			"         AA.MOTIVO_MODIFICA, "+
			"  (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
			"          || ' ' " + 
			"          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
			"         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
			"         WHERE AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
			"         AS UT_DENOMINAZIONE, " +
			"         (SELECT PVU.DENOMINAZIONE " +
			"          FROM PAPUA_V_UTENTE_LOGIN PVU " +
			"          WHERE AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
			"         AS UT_ENTE_APPART, "+
			"         P2.DESCRIZIONE ALIAS_DESC_PROV_COMPETENZA,"+
			"         AA.NUMERO_AGEA, "+
			"         A.FASCICOLO_DEMATERIALIZZATO, " +
			"         A.FLAG_AZIENDA_PROVVISORIA, "+
			"         A.FASCICOLO_DEMATERIALIZZATO, "+
			"         A.ID_AZIENDA_PROVENIENZA, "+
			"         A.VARIAZIONE_UTILIZZI_AMMESSA," +
			"         AA.INTESTAZIONE_PARTITA_IVA, " +
			"         A.ID_AZIENDA_PROVENIENZA, " +
			// Michele Piantà - 26/11/2009 - INIZIO MODIFICA
			// aggiunta per modifica servizio serviceGetListAziendeByIdRange
			// richiesta da Cristina Allisiardi per ATRV il 26/11/2009
			"         A.ID_OPR, " +
			// Michele, 27/11/2009 - INIZIO
      "         A.ID_TIPO_FORMA_ASSOCIATA, " +
      "         TFA.LABEL_ELENCO_ASSOCIATI, " +
      "         TFA.LABEL_SUB_ASSOCIATI, " +
			// Michele Piantà - 27/11/2009 - FINE MODIFICA
			"         AA.ID_CESSAZIONE, " +
			"         TC.DESCRIZIONE AS DESC_CESSAZIONE, " +
			"         AA.TELEFONO, AA.FAX, AA.PEC, AA.CODICE_AGRITURISMO, " +
			"         AA.DATA_AGGIORNAMENTO_UMA, " +
			"         AA.FLAG_IAP, " +
			"         AA.DATA_ISCRIZIONE_REA, " +
      "         AA.DATA_CESSAZIONE_REA, " +
      "         AA.DATA_ISCRIZIONE_RI, " +
      "         AA.DATA_CESSAZIONE_RI, " +
      "         AA.DATA_INIZIO_ATECO " +
			"    FROM DB_ANAGRAFICA_AZIENDA AA, "+
			"         DB_TIPO_FORMA_GIURIDICA TFG, "+
			"         DB_TIPO_TIPOLOGIA_AZIENDA TTA, "+
			"         DB_TIPO_ATTIVITA_ATECO TAT, "+
			"         DB_TIPO_ATTIVITA_OTE TOT, "+
			"         COMUNE, PROVINCIA P1, " +
			"         PROVINCIA P2, " +
		//	"         PAPUA_V_UTENTE_LOGIN PVU, "+
			"         DB_AZIENDA A, " +
			"         DB_TIPO_CESSAZIONE TC," +
			"         DB_TIPO_UDE TU, " +
			"         DB_TIPO_DIMENSIONE_AZIENDA TDA, " +
			"         DB_TIPO_FORMA_ASSOCIATA TFA   "+
			"   WHERE AA.SEDELEG_COMUNE = COMUNE.ISTAT_COMUNE(+) "+
			"     AND COMUNE.ISTAT_PROVINCIA = P1.ISTAT_PROVINCIA(+) "+
			"     AND AA.PROVINCIA_COMPETENZA = P2.ISTAT_PROVINCIA(+) "+
			"     AND AA.ID_FORMA_GIURIDICA = TFG.ID_FORMA_GIURIDICA(+) "+
			"     AND AA.ID_TIPOLOGIA_AZIENDA = TTA.ID_TIPOLOGIA_AZIENDA(+) "+
			"     AND AA.ID_ATTIVITA_ATECO = TAT.ID_ATTIVITA_ATECO(+) "+
			"     AND AA.ID_ATTIVITA_OTE = TOT.ID_ATTIVITA_OTE(+) "+
		//	"     AND AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
			"     AND A.ID_AZIENDA = AA.ID_AZIENDA " +
			"     AND AA.ID_CESSAZIONE = TC.ID_CESSAZIONE(+) " +
			"     AND AA.ID_UDE = TU.ID_UDE(+) " +
			"     AND AA.ID_DIMENSIONE_AZIENDA = TDA.ID_DIMENSIONE_AZIENDA(+) " +
			// Michele, 27/11/2009 - INIZIO
      "     AND A.ID_TIPO_FORMA_ASSOCIATA = TFA.ID_TIPO_FORMA_ASSOCIATA(+) ";
      // Michele, 27/11/2009 - FINE

			/*
			 * 27/02/2006 - INIZIO CODICE AGGIUNTO PER MODIFICA QUERY
			 */
			if (isIdAzienda)
			{
				if (dataSituazioneAl!=null)
				{
					daQuery+="     AND AA.DATA_INIZIO_VALIDITA  <= ? "+
					"     AND (AA.DATA_FINE_VALIDITA IS NULL "+
					"         OR AA.DATA_FINE_VALIDITA >= ?) ";
				}
				else
				{
					daQuery+="     AND AA.DATA_FINE_VALIDITA IS NULL ";
				}

				daQuery+="     AND AA.ID_AZIENDA IN ( ";
			}
			else
			{
				daQuery+="     AND ID_ANAGRAFICA_AZIENDA IN ( ";
			}









			StringBuffer queryBuff = new StringBuffer("");

			int collIdAziendeSize = collIdAziende.size();
			int numQuery=0;
			numQuery=collIdAziendeSize/PASSO;
			if (collIdAziendeSize%PASSO !=0) numQuery++;

			for(int j=0;j<numQuery;j++)
			{
				boolean primo=true;
				for(int i=j*PASSO;i<((j+1)*PASSO) && i<collIdAziendeSize;i++)
				{
					if (!primo)
						queryBuff.append(",");
					else
					{
						if (queryBuff.length()!=0)
							queryBuff.append(" UNION ");
						queryBuff.append(daQuery);
						primo=false;
					}
					queryBuff.append(collIdAziende.get(i));
				}
				queryBuff.append(")");
			}
			queryBuff.append(" ORDER BY 7 ");

			String find = queryBuff.toString();
			/*
			 * 27/02/2006 - FINE CODICE PER MODIFICA QUERY
			 */

			SolmrLogger.debug(this, "Executing query: "+find);

			stmt = conn.prepareStatement(find);


			if (isIdAzienda && dataSituazioneAl!=null)
			{
				for(int j=0;j<numQuery;j++)
				{
					stmt.setDate(1+(2*j), new java.sql.Date(dataSituazioneAl.getTime()));
					stmt.setDate(2+(2*j), new java.sql.Date(dataSituazioneAl.getTime()));
				}
			}


			ResultSet rs = stmt.executeQuery();
			if (rs != null) 
			{
				result = new Vector<AnagAziendaVO>();
				while (rs.next()) 
				{
					anagAziendaVO = new AnagAziendaVO();
					anagAziendaVO.setIntestazionePartitaIva(rs.getString("INTESTAZIONE_PARTITA_IVA"));
					anagAziendaVO.setIdAnagAzienda(new Long(rs.getLong("ID_ANAGRAFICA_AZIENDA")));
					anagAziendaVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
					anagAziendaVO.setDataInizioVal(rs.getDate("DATA_INIZIO_VALIDITA"));
					anagAziendaVO.setDataFineVal(rs.getDate("DATA_FINE_VALIDITA"));
					anagAziendaVO.setCUAA(rs.getString("ANAG_CUAA"));
					anagAziendaVO.setPartitaIVA(rs.getString("PARTITA_IVA"));
					anagAziendaVO.setDenominazione(rs.getString("ANAG_DENOMINAZIONE"));
					anagAziendaVO.setIdUde(checkLongNull(rs.getString("ID_UDE")));
					anagAziendaVO.setIdDimensioneAzienda(checkLongNull(rs.getString("ID_DIMENSIONE_AZIENDA")));
					anagAziendaVO.setRls(rs.getBigDecimal("RLS"));
					anagAziendaVO.setUlu(rs.getBigDecimal("ULU"));
					anagAziendaVO.setClasseUde(checkLongNull(rs.getString("CLASSE_UDE")));
					anagAziendaVO.setDescDimensioneAzienda(rs.getString("DESC_DIM_AZIENDA"));
					anagAziendaVO.setEsoneroPagamentoGF(rs.getString("ESONERO_PAGAMENTO_GF"));
					anagAziendaVO.setDataAggiornamentoUma(rs.getTimestamp("DATA_AGGIORNAMENTO_UMA"));
					anagAziendaVO.setFlagIap(rs.getString("FLAG_IAP"));
					anagAziendaVO.setDataIscrizioneRea(rs.getTimestamp("DATA_ISCRIZIONE_REA"));
					anagAziendaVO.setDataCessazioneRea(rs.getTimestamp("DATA_CESSAZIONE_REA"));
					anagAziendaVO.setDataIscrizioneRi(rs.getTimestamp("DATA_ISCRIZIONE_RI"));
					anagAziendaVO.setDataCessazioneRi(rs.getTimestamp("DATA_CESSAZIONE_RI"));
					anagAziendaVO.setDataInizioAteco(rs.getTimestamp("DATA_INIZIO_ATECO"));
					
					String codTipoFormaGiur = rs.getString("ALIAS_FORMA_GIURIDICA");
					if(codTipoFormaGiur!=null){
						anagAziendaVO.setTipoFormaGiuridica(new CodeDescription(
								new Integer(rs.getInt("ALIAS_FORMA_GIURIDICA")),
								rs.getString("ALIAS_DESC_TFG")));
					}
					else{
						anagAziendaVO.setTipoFormaGiuridica(new CodeDescription());
					}

					anagAziendaVO.setTipiFormaGiuridica(rs.getString("ALIAS_DESC_TFG"));
					String codTipolAzienda = rs.getString("ALIAS_TIPOLOGIA_AZIENDA");
					if(codTipolAzienda!=null){
						anagAziendaVO.setTipoTipologiaAzienda(new CodeDescription(
								new Integer(codTipolAzienda),
								rs.getString("ALIAS_DESC_TTA")));
						anagAziendaVO.setTipiAzienda(new Integer(codTipolAzienda).toString());
					}
					else{
						anagAziendaVO.setTipoTipologiaAzienda(new CodeDescription());
						anagAziendaVO.setTipiAzienda("");
					}

					//Aggiunta per gestione aziende collegate
					anagAziendaVO.setFlagFormaAssociata(rs.getString("FLAG_FORMA_ASSOCIATA"));
					
					// Michele, 27/11/2009 - INIZIO
          String idTipoFormaAssociata = rs.getString("ID_TIPO_FORMA_ASSOCIATA");
          anagAziendaVO.setIdTipoFormaAssociata(idTipoFormaAssociata==null ? null : new Long(idTipoFormaAssociata));
          anagAziendaVO.setLabelElencoAssociati(rs.getString("LABEL_ELENCO_ASSOCIATI"));
          anagAziendaVO.setLabelSubAssociati(rs.getString("LABEL_SUB_ASSOCIATI"));
          // Michele, 27/11/2009 - FINE


					String codATECO = rs.getString("ALIAS_ATTIVITA_ATECO");
					if(codATECO!=null){
						CodeDescription codeATECO = new CodeDescription(new Integer(codATECO),
								rs.getString("ALIAS_DESC_TAT"));
						codeATECO.setSecondaryCode(rs.getString("ALIAS_CODICE_TAT"));
						anagAziendaVO.setTipoAttivitaATECO(codeATECO);

					}
					else
						anagAziendaVO.setTipoAttivitaATECO(new CodeDescription());

					String codOTE = rs.getString("ALIAS_ATTIVITA_OTE");
					if(codOTE!=null){
						CodeDescription codeOTE = new CodeDescription(new Integer(codOTE),
								rs.getString("ALIAS_DESC_TOT"));
						codeOTE.setSecondaryCode(rs.getString("ALIAS_CODICE_TOT"));
						anagAziendaVO.setTipoAttivitaOTE(codeOTE);
					}
					else
						anagAziendaVO.setTipoAttivitaOTE(new CodeDescription());

					anagAziendaVO.setProvCompetenza(rs.getString("PROVINCIA_COMPETENZA"));
					if(anagAziendaVO.getProvCompetenza() != null) {
						anagAziendaVO.setProvincePiemonte(anagAziendaVO.getProvCompetenza());
					}
					anagAziendaVO.setSiglaProvCompetenza(rs.getString("ALIAS_PROV_COMPETENZA"));
					anagAziendaVO.setCCIAAprovREA(rs.getString("CCIAA_PROVINCIA_REA"));

					String numeroREA = rs.getString("CCIAA_NUMERO_REA");
					if(numeroREA!=null)
						anagAziendaVO.setCCIAAnumeroREA(new Long(numeroREA));

					anagAziendaVO.setStrCCIAAnumeroREA(rs.getString("CCIAA_NUMERO_REA"));
					anagAziendaVO.setCCIAAnumRegImprese(rs.getString("CCIAA_NUMERO_REGISTRO_IMPRESE"));
					anagAziendaVO.setCCIAAannoIscrizione(rs.getString("CCIAA_ANNO_ISCRIZIONE"));

					anagAziendaVO.setSedelegComune(rs.getString("SEDELEG_COMUNE"));

					String sedelegDescComune = rs.getString("ALIAS_DESCOM");
					String flagEstero = rs.getString("ALIAS_SEDELE_ESTERO");
					if(flagEstero==null || flagEstero.equals("")){
						anagAziendaVO.setSedelegComune("");
						anagAziendaVO.setSedelegEstero("");
						anagAziendaVO.setStatoEstero("");
						anagAziendaVO.setSedelegCittaEstero("");
						anagAziendaVO.setSedelegIstatProv("");
						anagAziendaVO.setSedelegProv("");
						anagAziendaVO.setDescComune("");
						anagAziendaVO.setSedelegCAP("");
					}
					else if(flagEstero.equals(SolmrConstants.FLAG_S)){
						anagAziendaVO.setSedelegEstero(sedelegDescComune);
						anagAziendaVO.setStatoEstero(sedelegDescComune);
						anagAziendaVO.setSedelegCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
						anagAziendaVO.setSedelegIstatProv("");
						anagAziendaVO.setSedelegProv("");
						anagAziendaVO.setDescComune("");
						anagAziendaVO.setSedelegCAP("");
					}
					else{
						anagAziendaVO.setSedelegEstero("");
						anagAziendaVO.setStatoEstero("");
						anagAziendaVO.setSedelegCittaEstero("");
						anagAziendaVO.setSedelegIstatProv(rs.getString("ALIAS_ISTAT_PROV"));
						anagAziendaVO.setSedelegProv(rs.getString("ALIAS_PROV"));
						anagAziendaVO.setDescComune(sedelegDescComune);
						anagAziendaVO.setSedelegCAP(rs.getString("SEDELEG_CAP"));
					}
					anagAziendaVO.setSedelegIndirizzo(rs.getString("SEDELEG_INDIRIZZO"));
					anagAziendaVO.setMail(rs.getString("ANAG_MAIL"));
					anagAziendaVO.setSitoWEB(rs.getString("SITOWEB"));
					anagAziendaVO.setNote(rs.getString("NOTE"));
					anagAziendaVO.setTelefono(rs.getString("TELEFONO"));
					anagAziendaVO.setFax(rs.getString("FAX"));
					anagAziendaVO.setPec(rs.getString("PEC"));
					anagAziendaVO.setCodiceAgriturismo(rs.getString("CODICE_AGRITURISMO"));
					anagAziendaVO.setDataCessazione(rs.getDate("DATA_CESSAZIONE"));
					anagAziendaVO.setCausaleCessazione(rs.getString("CAUSALE_CESSAZIONE"));
					java.util.Date dataAgg = rs.getDate("DATA_AGGIORNAMENTO");
					anagAziendaVO.setDataAggiornamento(dataAgg);
					anagAziendaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
					String motivoModifica = rs.getString("MOTIVO_MODIFICA");
					String utDenominazione = rs.getString("UT_DENOMINAZIONE");
					String utEnteAppart = rs.getString("UT_ENTE_APPART");
					String ultimaModifica = DateUtils.formatDate(dataAgg);
					anagAziendaVO.setDescrizioneProvCompetenza(rs.getString("ALIAS_DESC_PROV_COMPETENZA"));
					String tmp="";
					if(utDenominazione!=null && !utDenominazione.equals("")){
						if(tmp.equals(""))
							tmp+=" (";
						tmp+=utDenominazione;
					}
					if(utEnteAppart!=null && !utEnteAppart.equals("")){
						if(tmp.equals(""))
							tmp+=" (";
						else
							tmp+=" - ";
						tmp+=utEnteAppart;
					}
					if(motivoModifica!=null && !motivoModifica.equals("")){
						if(tmp.equals(""))
							tmp+=" (";
						else
							tmp+=" - ";
						tmp+=motivoModifica;
					}
					if(!tmp.equals(""))
						tmp+=")";
					ultimaModifica+=tmp;
					anagAziendaVO.setUltimaModifica(ultimaModifica);

					//Ultima Modifica Spezzata
					anagAziendaVO.setDataUltimaModifica(dataAgg);
					anagAziendaVO.setUtenteUltimaModifica(utDenominazione);
					anagAziendaVO.setEnteUltimaModifica(utEnteAppart);
					anagAziendaVO.setMotivoModifica(motivoModifica);

					anagAziendaVO.setPosizioneSchedario(rs.getString("NUMERO_AGEA"));
					anagAziendaVO.setVariazioneUtilizziAmmessa(rs.getString("VARIAZIONE_UTILIZZI_AMMESSA"));

					// Michele Piantà - 26/11/2009 - INIZIO MODIFICA
					// aggiunta per modifica servizio serviceGetListAziendeByIdRange
					// richiesta da Cristina Allisiardi per ATRV il 26/11/2009
					tmp = rs.getString("ID_OPR");
					anagAziendaVO.setIdOpr(tmp==null? null : new Long(tmp));
					// Michele Piantà - 26/11/2009 - FINE MODIFICA

					/**
					 * Questa porzione di codice va a vedere se l'azienda con cui sto
					 * lavorando è un'azienda provvisoria
					 */
					String temp=rs.getString("FLAG_AZIENDA_PROVVISORIA");
					if ("S".equals(temp))
					{
						anagAziendaVO.setFlagAziendaProvvisoria(true);
						temp=rs.getString("ID_AZIENDA_PROVENIENZA");
						if (temp!=null)
							anagAziendaVO.setIdAziendaSubentro(new Long(temp));
					}
					else
						anagAziendaVO.setFlagAziendaProvvisoria(false);
					if(Validator.isNotEmpty(rs.getString("ID_AZIENDA_PROVENIENZA"))) {
						anagAziendaVO.setIdAziendaProvenienza(new Long(rs.getLong("ID_AZIENDA_PROVENIENZA")));
					}
					if(Validator.isNotEmpty(rs.getString("ID_CESSAZIONE"))) {
						anagAziendaVO.setIdCessazione(new Long(rs.getLong("ID_CESSAZIONE")));
						TipoCessazioneVO tipoCessazioneVO = new TipoCessazioneVO();
						tipoCessazioneVO.setIdCessazione(new Long(rs.getLong("ID_CESSAZIONE")));
						tipoCessazioneVO.setDescrizione(rs.getString("DESC_CESSAZIONE"));
						anagAziendaVO.setTipoCessazioneVO(tipoCessazioneVO);
					}
					
					anagAziendaVO.setFascicoloDematerializzato(rs.getString("FASCICOLO_DEMATERIALIZZATO"));

					result.add(anagAziendaVO);
				}
				rs.close();
			} else
				throw new DataAccessException();

			//if (result == null)
				//throw new SolmrException(AnagErrors.NESSUNA_AZIENDA_TROVATA);
			
		}	
		catch (SolmrException exc) {
      SolmrLogger.debug(this, "getListAziendeByIdRange - SolmrException: "+exc.getMessage());
      throw exc;
	  } catch (SQLException exc) {
			SolmrLogger.fatal(this, "getListAziendeByIdRange - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} catch (DataAccessException daexc) {
			SolmrLogger.fatal(this, "getListAziendeByIdRange - ResultSet null");
			throw daexc;
		} catch (Exception ex) {
    	SolmrLogger.fatal(this, "getListAziendeByIdRange - Generic Exception: "+ex.getMessage());
    	throw new DataAccessException(ex.getMessage());
    } finally {
    	try {
    		if (stmt != null) stmt.close();
    		if (conn != null) conn.close();
    	} catch (SQLException exc) {
    		SolmrLogger.fatal(this, "getListAziendeByIdRange - SQLException while closing Statement and Connection: "+exc.getMessage());
    		throw new DataAccessException(exc.getMessage());
    	} catch (Exception ex) {
    		SolmrLogger.fatal(this, "getListAziendeByIdRange - Generic Exception while closing Statement and Connection: "+ex.getMessage());
    		throw new DataAccessException(ex.getMessage());
    	}
    }
    return result;
	}

	public Long insertAnagAzienda(AnagAziendaVO anagAziendaVO) throws DataAccessException 
	{
		Long primaryKey;
		Connection conn = null;
		PreparedStatement stmt = null;
		try 
		{

			primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_ANAG_AZIENDA);
			conn = getDatasource().getConnection();

			String query = " insert into db_anagrafica_azienda ("+
			" id_anagrafica_azienda, id_azienda, data_inizio_validita, data_fine_validita, "+
			" cuaa, partita_iva, denominazione, id_forma_giuridica, id_tipologia_azienda, "+
			" provincia_competenza, cciaa_provincia_rea, cciaa_numero_rea, "+
			" cciaa_numero_registro_imprese, cciaa_anno_iscrizione, mail, "+
			" sedeleg_comune, sedeleg_indirizzo, sitoweb, sedeleg_cap, sedeleg_citta_estero, "+
			" data_cessazione, causale_cessazione,"+
			" note, data_aggiornamento, id_utente_aggiornamento, motivo_modifica,"+
			" id_attivita_ateco, id_attivita_ote, id_ude, id_dimensione_azienda, rls, ulu, telefono, fax, pec, codice_agriturismo, ESONERO_PAGAMENTO_GF, " +
			" INTESTAZIONE_PARTITA_IVA, DATA_AGGIORNAMENTO_UMA, FLAG_IAP, DATA_ISCRIZIONE_REA, " +
			" DATA_CESSAZIONE_REA, DATA_ISCRIZIONE_RI, DATA_CESSAZIONE_RI, DATA_INIZIO_ATECO) "+
			" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

			SolmrLogger.debug(this, "-- insertAnagAzienda ="+query);
			stmt = conn.prepareStatement(query);

			
			SolmrLogger.debug(this, "-- id_anagrafica_azienda ="+primaryKey);
			SolmrLogger.debug(this, "-- id_azienda ="+anagAziendaVO.getIdAzienda());
			stmt.setLong(1,primaryKey.longValue());
			stmt.setLong(2,anagAziendaVO.getIdAzienda().longValue());
			stmt.setDate(3,new java.sql.Date(System.currentTimeMillis()));
			stmt.setDate(4,null);
			stmt.setString(5,anagAziendaVO.getCUAA());
			stmt.setString(6,anagAziendaVO.getPartitaIVA());
			stmt.setString(7,anagAziendaVO.getDenominazione().toUpperCase().trim());
			stmt.setLong(8,anagAziendaVO.getTipoFormaGiuridica().getCode().longValue());
			if (anagAziendaVO.getTipoTipologiaAzienda()!=null&&anagAziendaVO.getTipoTipologiaAzienda().getCode()!=null)
				stmt.setLong(9, anagAziendaVO.getTipoTipologiaAzienda().getCode().longValue());
			else
				stmt.setBigDecimal(9, null);
			stmt.setString(10,anagAziendaVO.getProvCompetenza());
			stmt.setString(11,anagAziendaVO.getCCIAAprovREA());
			stmt.setLong(12,anagAziendaVO.getCCIAAnumeroREA().longValue());
			stmt.setString(13,anagAziendaVO.getCCIAAnumRegImprese());
			stmt.setString(14,anagAziendaVO.getCCIAAannoIscrizione());
			stmt.setString(15,anagAziendaVO.getMail());
			if(anagAziendaVO.getSedelegComune() != null && !anagAziendaVO.getSedelegComune().equals("")) {
				stmt.setString(16,anagAziendaVO.getSedelegComune());
			}
			else {
				stmt.setString(16,anagAziendaVO.getSedelegEstero());
			}
			if(anagAziendaVO.getSedelegIndirizzo() != null) {
				stmt.setString(17,anagAziendaVO.getSedelegIndirizzo().toUpperCase());
			}
			else {
				stmt.setString(17,null);
			}
			stmt.setString(18,anagAziendaVO.getSitoWEB());
			stmt.setString(19,anagAziendaVO.getSedelegCAP());
			if (Validator.isNotEmpty(anagAziendaVO.getSedelegCittaEstero()))
			{
				stmt.setString(20,anagAziendaVO.getSedelegCittaEstero().toUpperCase());
			}
			else
			{
				stmt.setString(20,null);
			}
			if(anagAziendaVO.getDataCessazione() != null) {
				stmt.setDate(21,new java.sql.Date(anagAziendaVO.getDataCessazione().getTime()));
			}
			else {
				stmt.setDate(21,null);
			}
			stmt.setString(22,anagAziendaVO.getCausaleCessazione());
			stmt.setString(23,anagAziendaVO.getNote());
			stmt.setDate(24,new java.sql.Date(System.currentTimeMillis()));
			stmt.setLong(25,anagAziendaVO.getIdUtenteAggiornamento().longValue());
			stmt.setString(26,anagAziendaVO.getMotivoModifica());
			if (anagAziendaVO.getTipoAttivitaATECO()!=null&&anagAziendaVO.getTipoAttivitaATECO().getCode()!=null)
				stmt.setLong(27, anagAziendaVO.getTipoAttivitaATECO().getCode().longValue());
			else
				stmt.setBigDecimal(27, null);
			if (anagAziendaVO.getTipoAttivitaOTE()!=null&&anagAziendaVO.getTipoAttivitaOTE().getCode()!=null)
				stmt.setLong(28, anagAziendaVO.getTipoAttivitaOTE().getCode().longValue());
			else
				stmt.setBigDecimal(28, null);
			if (anagAziendaVO.getIdUde()!=null)
        stmt.setLong(29, anagAziendaVO.getIdUde().longValue());
      else
        stmt.setBigDecimal(29, null);
			if (anagAziendaVO.getIdDimensioneAzienda()!=null)
        stmt.setLong(30, anagAziendaVO.getIdDimensioneAzienda().longValue());
      else
        stmt.setBigDecimal(30, null);
  	  if (anagAziendaVO.getRls()!=null)
  	        stmt.setBigDecimal(31, anagAziendaVO.getRls());
  	      else
  	        stmt.setBigDecimal(31, null);
  	  if (anagAziendaVO.getUlu()!=null)
  	        stmt.setBigDecimal(32, anagAziendaVO.getUlu());
  	      else
  	        stmt.setBigDecimal(32, null);
  	  stmt.setString(33,anagAziendaVO.getTelefono());
  	  stmt.setString(34,anagAziendaVO.getFax());
  	  stmt.setString(35,anagAziendaVO.getPec());
  	  stmt.setString(36,anagAziendaVO.getCodiceAgriturismo());
  	  stmt.setString(37, anagAziendaVO.getEsoneroPagamentoGF());
  	  stmt.setString(38, anagAziendaVO.getIntestazionePartitaIva());
  	  stmt.setTimestamp(39, convertDateToTimestamp(anagAziendaVO.getDataAggiornamentoUma()));
  	  String flagIap = "N";
  	  if(Validator.isNotEmpty(anagAziendaVO.getFlagIap()))
  	    flagIap = anagAziendaVO.getFlagIap();
  	  stmt.setString(40, flagIap);
  	  stmt.setTimestamp(41, convertDateToTimestamp(anagAziendaVO.getDataIscrizioneRea()));
  	  stmt.setTimestamp(42, convertDateToTimestamp(anagAziendaVO.getDataCessazioneRea()));
  	  stmt.setTimestamp(43, convertDateToTimestamp(anagAziendaVO.getDataIscrizioneRi()));
  	  stmt.setTimestamp(44, convertDateToTimestamp(anagAziendaVO.getDataCessazioneRi()));
  	  stmt.setTimestamp(45, convertDateToTimestamp(anagAziendaVO.getDataInizioAteco()));
  	  
			SolmrLogger.debug(this, "Executing insert: "+query);

			stmt.executeUpdate();

			stmt.close();

			SolmrLogger.debug(this, "Executed insert on Primary Key: "+primaryKey);
		}
		catch (SQLException exc) {
			SolmrLogger.fatal(this, "insertAnagAzienda - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex) {
			SolmrLogger.fatal(this, "insertAnagAzienda - Generic Exception: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			}
			catch (SQLException exc) {
				SolmrLogger.fatal(this, "insertAnagAzienda - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.fatal(this, "insertAnagAzienda - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return primaryKey;
	}

	public void updateAnagAzienda(Long anagAziendaPK, AnagAziendaVO anagAziendaVO) throws DataAccessException {

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getDatasource().getConnection();

			String update = "UPDATE DB_Anagrafica_Azienda "+
			"   SET <campo = valore> "+
			" WHERE <campoPK> = ? ";
			stmt = conn.prepareStatement(update);

			SolmrLogger.debug(this, "Executing update: "+update);

			stmt.executeUpdate();

			stmt.close();

			SolmrLogger.debug(this, "Executed update.");
		} catch (SQLException exc) {
			SolmrLogger.fatal(this, "updateAnagAzienda - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} catch (Exception ex) {
			SolmrLogger.fatal(this, "updateAnagAzienda - Generic Exception: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, "updateAnagAzienda - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "updateAnagAzienda - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
	}


	public void updateAnagAziendaForCessazione(Long anagAziendaPK, AnagAziendaVO anagAziendaVO, long idUtenteAggiornamento) throws DataAccessException {

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getDatasource().getConnection();

			String update = "UPDATE DB_Anagrafica_Azienda "+
			"   SET data_cessazione = ?, "+
			"       causale_cessazione = ?, "+
			"       id_utente_aggiornamento = ?, "+
			"       data_aggiornamento = SYSDATE, " +
			"       ID_CESSAZIONE = ? " +
			" WHERE id_anagrafica_azienda = ? ";

			stmt = conn.prepareStatement(update);


			SolmrLogger.debug(this, "Executing update: "+update);
			stmt.setDate(1, new java.sql.Date(anagAziendaVO.getDataCessazione().getTime()));
			stmt.setString(2, anagAziendaVO.getCausaleCessazione());
			stmt.setLong(3, idUtenteAggiornamento);
			SolmrLogger.debug(this, "Value of parameter 4 [ID_CESSAZIONE] in updateAnagAziendaForCessazione method in AnagrafeDAO: "+anagAziendaVO.getIdCessazione()+"\n");
			stmt.setLong(4, anagAziendaVO.getIdCessazione().longValue());
			stmt.setLong(5, anagAziendaPK.longValue());
			stmt.executeUpdate();

			stmt.close();

			SolmrLogger.debug(this, "Executed update.");
		} catch (SQLException exc) {
			SolmrLogger.fatal(this, "updateAnagAziendaForCessazione - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} catch (Exception ex) {
			SolmrLogger.fatal(this, "updateAnagAziendaForCessazione - Generic Exception: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, "updateAnagAziendaForCessazione - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "updateAnagAziendaForCessazione - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
	}

	public void deleteAnagAzienda(BigDecimal anagAziendaPK) throws DataAccessException {

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getDatasource().getConnection();

			String delete = "DELETE <tabella> "+
			" WHERE <campoPK> = ? ";
			stmt = conn.prepareStatement(delete);

			SolmrLogger.debug(this, "Executing delete: "+delete);

			stmt.setBigDecimal(1, anagAziendaPK);
			stmt.executeUpdate();

			stmt.close();

			SolmrLogger.debug(this, "Executed delete.");
		} catch (SQLException exc) {
			SolmrLogger.fatal(this, "deleteAnagAzienda - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} catch (Exception ex) {
			SolmrLogger.fatal(this, "deleteAnagAzienda - Generic Exception: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, "deleteAnagAzienda - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "deleteAnagAzienda - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
	}

	public PersonaFisicaVO getTitolareORappresentanteLegaleAzienda(Long idAzienda, java.util.Date dataSituazioneAl)
	  throws DataAccessException, NotFoundException
	{

		PersonaFisicaVO personaFisicaVO = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		Long idPersonaFisica = null;
		try {
			conn = getDatasource().getConnection();

			String find = "SELECT ID_PERSONA_FISICA, PF.ID_SOGGETTO ALIAS_SOGGETTO, "+
			"       CODICE_FISCALE, COGNOME, NOME, SESSO, NASCITA_COMUNE, "+
			"       NASCITA_DATA, RES_INDIRIZZO, RES_COMUNE, RES_CAP, "+
			"       RES_TELEFONO, RES_FAX, NUMERO_CELLULARE, RES_MAIL, NOTE, "+
			"       DATA_AGGIORNAMENTO, ID_UTENTE_AGGIORNAMENTO, "+
			"       DATA_INIZIO_RUOLO, DATA_FINE_RUOLO, "+
			"       DOM_INDIRIZZO, DOM_COMUNE, DOM_CITTA_ESTERO, DOM_CAP, "+
			"       RES_CITTA_ESTERO, NASCITA_CITTA_ESTERO, TTS.ID_TITOLO_STUDIO ALIAS_TITOLO_STUDIO, "+
			"       TTS.DESCRIZIONE ALIAS_TITOLO_DESCR, TIS.ID_INDIRIZZO_STUDIO ALIAS_INDIRIZZO_STUDIO, TIS.DESCRIZIONE ALIAS_INDIRIZZO_DESCR, " +
			"       C.FLAG_ESTERO ALIAS_FLAG_ESTERO, C.DESCOM ALIAS_DESCOM, P.SIGLA_PROVINCIA ALIAS_SIGLA_PROVINCIA, " +
			"       TRUNC(PF.DATA_INIZIO_RESIDENZA) AS DATA_INIZIO_RESIDENZA, "+
			"       FLAG_CF_OK " +
			"  FROM DB_CONTITOLARE C, DB_PERSONA_FISICA PF, DB_TIPO_TITOLO_STUDIO TTS, " +
			"       DB_TIPO_INDIRIZZO_STUDIO TIS, COMUNE C, PROVINCIA P "+
			" WHERE C.ID_SOGGETTO = PF.ID_SOGGETTO "+
			"   AND C.ID_AZIENDA = ? "+
			"   AND C.ID_RUOLO = "+SolmrConstants.TIPORUOLO_TITOL_RAPPR_LEG+
			"   AND C.DATA_INIZIO_RUOLO <= ? "+
			"   AND (C.DATA_FINE_RUOLO IS NULL OR C.DATA_FINE_RUOLO >= TRUNC(?)) "+
			"   AND PF.ID_TITOLO_STUDIO = TTS.ID_TITOLO_STUDIO(+) " +
			"   AND PF.ID_INDIRIZZO_STUDIO = TIS.ID_INDIRIZZO_STUDIO(+) " +
			"   AND C.ISTAT_COMUNE(+) = PF.DOM_COMUNE " +
			"   AND C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) " +
			" ORDER BY C.DATA_FINE_RUOLO DESC ";

			stmt = conn.prepareStatement(find);
			stmt.setLong(1, idAzienda.longValue());
			stmt.setDate(2, new java.sql.Date(dataSituazioneAl.getTime()));
			stmt.setDate(3, new java.sql.Date(dataSituazioneAl.getTime()));
			SolmrLogger.debug(this, "Executing query: "+find);

			ResultSet rs = stmt.executeQuery();
			if(rs != null) {
				if(rs.next()) {
					personaFisicaVO = new PersonaFisicaVO();
					idPersonaFisica = new Long(rs.getLong("ID_PERSONA_FISICA"));
					personaFisicaVO.setIdPersonaFisica(idPersonaFisica);
					personaFisicaVO.setIdSoggetto(new Long(rs.getLong("ALIAS_SOGGETTO")));
					personaFisicaVO.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
					personaFisicaVO.setCognome(rs.getString("COGNOME"));
					personaFisicaVO.setNome(rs.getString("NOME"));
					personaFisicaVO.setSesso(rs.getString("SESSO"));
					personaFisicaVO.setNascitaComune(rs.getString("NASCITA_COMUNE"));
					personaFisicaVO.setNascitaData(rs.getDate("NASCITA_DATA"));
					personaFisicaVO.setNascitaCittaEstero(rs.getString("NASCITA_CITTA_ESTERO"));
					personaFisicaVO.setResComune(rs.getString("RES_COMUNE"));
					personaFisicaVO.setResIndirizzo(rs.getString("RES_INDIRIZZO"));
					personaFisicaVO.setResCittaEstero(rs.getString("RES_CITTA_ESTERO"));
					personaFisicaVO.setResCAP(rs.getString("RES_CAP"));
					personaFisicaVO.setResTelefono(rs.getString("RES_TELEFONO"));
					personaFisicaVO.setResFax(rs.getString("RES_FAX"));
					personaFisicaVO.setNumeroCellulare(rs.getString("NUMERO_CELLULARE"));
					personaFisicaVO.setResMail(rs.getString("RES_MAIL"));
					personaFisicaVO.setNote(rs.getString("NOTE"));
					personaFisicaVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
					personaFisicaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
					personaFisicaVO.setDataInizioRuolo(rs.getDate("DATA_INIZIO_RUOLO"));
					personaFisicaVO.setDataFineRuolo(rs.getDate("DATA_FINE_RUOLO"));
					personaFisicaVO.setDomIndirizzo(rs.getString("DOM_INDIRIZZO"));
					personaFisicaVO.setIstatComuneDomicilio(rs.getString("DOM_COMUNE"));
					personaFisicaVO.setDescCittaEsteroDomicilio(rs.getString("DOM_CITTA_ESTERO"));
					personaFisicaVO.setDomCAP(rs.getString("DOM_CAP"));
					personaFisicaVO.setDataInizioResidenza(rs.getDate("DATA_INIZIO_RESIDENZA"));
					if(rs.getString("ALIAS_TITOLO_STUDIO") != null) {
						personaFisicaVO.setIdTitoloStudio(new Long(rs.getLong("ALIAS_TITOLO_STUDIO")));
						personaFisicaVO.setDescrizioneTitoloStudio(rs.getString("ALIAS_TITOLO_DESCR"));
					}
					if(rs.getString("ALIAS_INDIRIZZO_STUDIO") != null) {
						personaFisicaVO.setIdIndirizzoStudio(new Long(rs.getLong("ALIAS_INDIRIZZO_STUDIO")));
						personaFisicaVO.setDescrizioneIndirizzoStudio(rs.getString("ALIAS_INDIRIZZO_DESCR"));
					}
					personaFisicaVO.setDomicilioFlagEstero(rs.getString("ALIAS_FLAG_ESTERO"));

					if((SolmrConstants.FLAG_S).equalsIgnoreCase(rs.getString("ALIAS_FLAG_ESTERO"))) {
						personaFisicaVO.setDomicilioStatoEstero(rs.getString("ALIAS_DESCOM"));
						personaFisicaVO.setDomProvincia(null);
					}
					else {
						personaFisicaVO.setDomComune(rs.getString("ALIAS_DESCOM"));
						personaFisicaVO.setDomProvincia(rs.getString("ALIAS_SIGLA_PROVINCIA"));
					}

					personaFisicaVO.setFlagCfOk(rs.getString("FLAG_CF_OK"));

				}
			}
			else {
				throw new DataAccessException();
			}

			if (personaFisicaVO == null)
				throw new NotFoundException();
			
			if (idPersonaFisica!=null)
			{
			  //vado a recuperare i dati della residenza storicizzati
			  find = "SELECT INDIRIZZO, COMUNE,CAP, CITTA_ESTERO "+
			         "FROM DB_STORICO_RESIDENZA "+
			         "WHERE ID_PERSONA_FISICA= ? "+
			         "AND DATA_INIZIO_RESIDENZA <= ? "+
			         "AND TRUNC(DATA_FINE_RESIDENZA) > ? ";
			  rs.close();
			  stmt.close();
			  stmt = conn.prepareStatement(find);
	      stmt.setLong(1, idPersonaFisica.longValue());
	      stmt.setDate(2, new java.sql.Date(dataSituazioneAl.getTime()));
	      stmt.setDate(3, new java.sql.Date(dataSituazioneAl.getTime()));
	      SolmrLogger.debug(this, "Executing query: "+find);
	      rs = stmt.executeQuery();
	      if(rs.next()) 
	      {
	        //Se trovo dei dati sovrascrivo quelli trovati precedentemete
	        SolmrLogger.debug(this, "Resisenza storicizzata");
          personaFisicaVO.setResComune(rs.getString("COMUNE"));
          personaFisicaVO.setResCAP(rs.getString("CAP"));
          personaFisicaVO.setResIndirizzo(rs.getString("INDIRIZZO"));
          personaFisicaVO.setResCittaEstero(rs.getString("CITTA_ESTERO"));
	      }
	      rs.close();
			}

			personaFisicaVO = fillAnagWithProvAndCom(personaFisicaVO);

		} catch (NotFoundException exc) {
			SolmrLogger.fatal(this, "getTitolareORappresentanteLegaleAzienda - DataControlException: "+exc.getMessage());
			throw exc;
		} catch (SQLException exc) {
			SolmrLogger.fatal(this, "getTitolareORappresentanteLegaleAzienda - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} catch (DataAccessException daexc) {
			SolmrLogger.fatal(this, "getTitolareORappresentanteLegaleAzienda - ResultSet null");
			throw daexc;
		} catch (Exception ex) {
			SolmrLogger.fatal(this, "getTitolareORappresentanteLegaleAzienda - Generic Exception: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, "getTitolareORappresentanteLegaleAzienda - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "getTitolareORappresentanteLegaleAzienda - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}

		return personaFisicaVO;
	}

	// Metodo per effettuare la modifica degli elementi rappresentanti la "sede legale".
	public void updateSedeLegale(AnagAziendaVO anagAziendaVO,long idUtenteAggiornamento) 
	  throws SQLException, Exception, ObjectNotFoundException, DataAccessException 
	{
		PreparedStatement statement = null;
		Connection connection = null;
		try 
		{

			connection = getDatasource().getConnection();

			String query = "" +
				"update db_anagrafica_azienda " +
				"set sedeleg_indirizzo = ?, " +
				"    provincia_competenza = ?, " +
				"    sedeleg_comune = ?, " +
			  "    sedeleg_cap = ?, " +
			 // "    sitoweb = ?, " +
			 // "    mail = ?, " +
			  "    data_aggiornamento = SYSDATE, " +
			  "    id_utente_aggiornamento = ?, " +
			  "    data_fine_validita = ?, " +
			  "    sedeleg_citta_estero = ?  " +
			//  "    telefono = ?, " +
			//  "    fax = ? " +
			  "    where id_anagrafica_azienda = ? ";

			int indice = 0;
			statement = connection.prepareStatement(query);
			if(anagAziendaVO.getSedelegIndirizzo() != null) 
			{
				statement.setString(++indice,anagAziendaVO.getSedelegIndirizzo().toUpperCase());
			}
			else {
				statement.setString(++indice,null);
			}
			statement.setString(++indice,anagAziendaVO.getProvCompetenza());
			if(anagAziendaVO.getSedelegComune() != null && !anagAziendaVO.getSedelegComune().equals("")) {
				statement.setString(++indice,anagAziendaVO.getSedelegComune());
			}
			else {
				statement.setString(++indice,anagAziendaVO.getSedelegEstero());
			}
			statement.setString(++indice,anagAziendaVO.getSedelegCAP());
			//statement.setString(++indice,anagAziendaVO.getSitoWEB());
			//statement.setString(++indice,anagAziendaVO.getMail());
			statement.setLong(++indice, idUtenteAggiornamento);
			if(anagAziendaVO.getDataFineVal() != null) {
				statement.setDate(++indice,new java.sql.Date(anagAziendaVO.getDataFineVal().getTime()));
			}
			else {
				statement.setDate(++indice,null);
			}
			if (Validator.isNotEmpty(anagAziendaVO.getSedelegCittaEstero()))
			{
				statement.setString(++indice,anagAziendaVO.getSedelegCittaEstero().toUpperCase());
			}
			else
			{
				statement.setString(++indice,null);
			}
			//statement.setString(++indice,anagAziendaVO.getTelefono());
			//statement.setString(++indice,anagAziendaVO.getFax());
			statement.setLong(++indice,anagAziendaVO.getIdAnagAzienda().longValue());
			int result = statement.executeUpdate();
			if(result != 1) {
				throw new SQLException("Errore durante la modifica della sede legale nella tabella DB_ANAGRAFICA_AZIENDA!");
			}
			statement.close();
		}
		catch (SQLException exc) {
			SolmrLogger.fatal(this, " modifica sede legale azienda - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} catch (Exception ex) {
			SolmrLogger.fatal(this, " modifica sede legale azienda - Generic Exception: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		} finally {
			try {
				if (statement != null) statement.close();
				if (connection != null) connection.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, " modifica sede legale azienda - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, " modifica sede legale azienda - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
	}

	// Metodo per recuperare l'elenco delle attivita OTE a partire dal codice(anche parziale) e dalla descrizione
	// anche parziale dell'attivita
	public Vector<CodeDescription> getAttivitaLikeCodAndDescOTE(String codice, String descrizione) throws DataAccessException, NotFoundException, DataControlException {


		Vector<CodeDescription> elencoAttivita = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		String queryCodice = "";
		String queryDescrizione = "";
		String andStr ="";
		if(codice != null && !codice.equals("")) {
			queryCodice = " codice like ? ";
		}
		if(descrizione != null && !descrizione.equals("")) {
			queryDescrizione = "descrizione  like ? ";
		}
		if(codice != null && !codice.equals("") && descrizione != null && !descrizione.equals(""))
			andStr = " AND ";
		try {
			conn = getDatasource().getConnection();

			String query = "select codice, descrizione from db_tipo_attivita_ote p where "+queryCodice+andStr+queryDescrizione+
			"order by descrizione ";

			stmt = conn.prepareStatement(query);

			if(codice != null && !codice.equals("")) {
				stmt.setString(1,codice.toUpperCase()+"%");
			}
			if(descrizione != null && !descrizione.equals("")) {
				stmt.setString(2,"%"+descrizione.toUpperCase()+"%");
			}
			SolmrLogger.debug(this, "Executing query: "+query);

			ResultSet rs = stmt.executeQuery();

			if (rs != null) {
				elencoAttivita = new Vector<CodeDescription>();
				while (rs.next()) {
					CodeDescription cd = new CodeDescription();
					cd.setCode(new Integer(rs.getInt(1)));
					cd.setDescription(rs.getString(2));
					elencoAttivita.add(cd);
				}
				rs.close();
			}
			if(elencoAttivita.size() == 0) {
				throw new DataControlException(AnagErrors.SCELTA_ATTIVITA);
			}
			SolmrLogger.debug(this, "Executed query - Found "+elencoAttivita.size()+" result(s).");
		}
		catch (SQLException exc) {
			SolmrLogger.fatal(this, "SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		}
		catch(DataControlException dce) {
			SolmrLogger.fatal(this, "DataControlException: "+dce.getMessage());
			throw new DataControlException(dce.getMessage());
		}
		catch (Exception ex) {
			SolmrLogger.fatal(this, "Generic Exception: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, "SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return elencoAttivita;
	}


	// ricerca le unità produttive di un comune
	public int countUteByAziendaAndComune(Long idAzienda,String idComune) throws DataAccessException, NotFoundException {

		//UteVO result = null;
		int count = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getDatasource().getConnection();

			String find =   "  SELECT COUNT(*) "+
			"    FROM DB_UTE "+
			"   WHERE ID_AZIENDA = ? "+
			"     AND COMUNE = ? "+
			"     AND DATA_FINE_ATTIVITA IS NULL ";

			stmt = conn.prepareStatement(find);
			SolmrLogger.debug(this, "Executing query: "+find);

			stmt.setLong(1, idAzienda.longValue());
			stmt.setString(2, idComune);

			ResultSet rs = stmt.executeQuery();
			if (rs!=null){
				while(rs.next()){
					count = rs.getInt(1);
				}
				rs.close();
			}
			if(count>0){
				throw new SolmrException(AnagErrors.ESISTE_UTE_CON_COMUNE);
			}
			SolmrLogger.debug(this, "Executed query - Found record with key: "+idAzienda);
		} catch (SQLException exc) {
			SolmrLogger.fatal(this, "countUteByAziendaAndComune - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} catch (Exception ex) {
			SolmrLogger.fatal(this, "countUteByAziendaAndComune - Generic Exception: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, "countUteByAziendaAndComune - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "countUteByAziendaAndComune - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return count;
	}



	public Date getDataInizioRuoloRappresentanteLegale(Long idAzienda) throws DataAccessException, NotFoundException
	{

		Date date=null;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getDatasource().getConnection();

			String query =  "  SELECT COUNT(*)"+
			"    FROM DB_UTE"+
			"   WHERE ID_AZIENDA = ? "+
			"     AND COMUNE = ?"+
			"     AND DATA_FINE_ATTIVITA IS NULL";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing query: "+query);

			stmt.setLong(1, idAzienda.longValue());
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
			{
				date = rs.getDate(1);
			}
			else
			{
				throw new NotFoundException(AnagErrors.NESSUN_RAPPRESENTANTE_LEGALE);
			}
			rs.close();
			SolmrLogger.debug(this, "Executed query - Found record with key: "+idAzienda);
		} catch (SQLException exc) {
			SolmrLogger.fatal(this, "getDataInizioRuoloRappresentanteLegale - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} catch (Exception ex) {
			SolmrLogger.fatal(this, "getDataInizioRuoloRappresentanteLegale - Generic Exception: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, "getDataInizioRuoloRappresentanteLegale - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "getDataInizioRuoloRappresentanteLegale - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return date;
	}

	// Metodo utilizzato anche da UMA oltre che da anagrafe per controllare se un determinato
	// possiede la delega per lavorare su un'azienda agricola.
	// Niene utilizzato anche da UMA
	// prima di effettuare modifiche a questo metodo.
	public void utenteConDelega(UtenteAbilitazioni utenteAbilitazioni, Long idAzienda) throws DataAccessException, DataControlException 
	{


		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// Modifica effettuata per correggere l'anomalia dell'assenza dell'id_procedimento sugli utenti
		// UMA.Il metodo dovrà comunque essere modificato perchè in questo modo non potrà essere utilizzato
		// da altri applicativi diversi da UMA
		String idProcedimento = null;
		try 
		{

			conn = getDatasource().getConnection();

			String query = " SELECT PVU.ID_PROCEDIMENTO " +
			" FROM   PAPUA_V_UTENTE_LOGIN PVU " +
			" WHERE  PVU.ID_UTENTE_LOGIN = ? ";

			stmt = conn.prepareStatement(query);

			stmt.setLong(1, utenteAbilitazioni.getIdUtenteLogin().longValue());

			rs = stmt.executeQuery();

			if(rs.next()) 
			{
				idProcedimento = rs.getString(1);
			}
			rs.close();

			stmt.close();

			String query2 = null;

			// Questa parte la mantengo solo nel caso in cui UMA si appoggiasse ancora a questo
			// metodo: va tolta ed eliminata appena UMA verrà allineata ai nuovi criteri di profilazione
			// in quanto questa query è errata rispetto alla nuova profilazione di anagrafe
			if(!Validator.isNotEmpty(utenteAbilitazioni.getEnteAppartenenza().getIntermediario()) 
			    || !Validator.isNotEmpty(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello())) 
			{

				query2 = "  SELECT ID_DELEGA " +
				"  FROM   DB_DELEGA, " +
				"         PAPUA_V_UTENTE_LOGIN PVU, " +
				"         DB_INTERMEDIARIO " +
				"  WHERE  PVU.ID_UTENTE_LOGIN = ? " +
				"  AND    PVU.EXT_ID_INTERMEDIARIO = DB_INTERMEDIARIO.ID_INTERMEDIARIO " +
				"  AND    DB_INTERMEDIARIO.ID_INTERMEDIARIO = DB_DELEGA.ID_INTERMEDIARIO " +
				"  AND    DB_DELEGA.ID_AZIENDA = ? " +
				"  AND    DB_DELEGA.DATA_INIZIO <= SYSDATE " +
				"  AND    (DB_DELEGA.DATA_FINE IS NULL OR DB_DELEGA.DATA_FINE >= SYSDATE) " +
				"  AND    DB_DELEGA.ID_PROCEDIMENTO = ? ";
			}
			// SEZIONE DI ANAGRAFE CORRETTA
			else 
			{
				// Se l'intermediario che si logga possiede livello = "Z"
				if(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase((String)SolmrConstants.get("LIVELLO_INTERMEDIARIO_UFFICIO_ZONA"))) 
				{
					query2 = " SELECT D.ID_DELEGA, " +
					"        D.ID_INTERMEDIARIO " +
					" FROM   DB_DELEGA D, " +
					"        DB_INTERMEDIARIO I " +
					" WHERE  D.ID_AZIENDA = ? " +
					" AND    D.DATA_FINE IS NULL " +
					" AND    D.ID_INTERMEDIARIO = ? " +
					" AND    D.ID_PROCEDIMENTO = ? " +
					" AND    D.ID_INTERMEDIARIO = I.ID_INTERMEDIARIO ";
				}
				// Se l'intermediario che si logga possiede livello = "P"
				else if(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase((String)SolmrConstants.get("LIVELLO_INTERMEDIARIO_PROVINCIALE"))) 
				{
					query2 = " SELECT D.ID_DELEGA, " +
					"        D.ID_INTERMEDIARIO " +
					" FROM   DB_DELEGA D, " +
					"        DB_INTERMEDIARIO I " +
					" WHERE  D.ID_AZIENDA = ? " +
					" AND    D.DATA_FINE IS NULL " +
					" AND    SUBSTR(I.CODICE_FISCALE, 1, 6) = ? " +
					" AND    I.TIPO_INTERMEDIARIO = 'C' " +
					" AND    D.ID_PROCEDIMENTO = ? " +
					" AND    D.ID_INTERMEDIARIO = I.ID_INTERMEDIARIO ";
				}
				// Se l'intermediario che si logga possiede livello = "R"
				else if(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase((String)SolmrConstants.get("LIVELLO_INTERMEDIARIO_REGIONALE"))) 
				{
					query2 = " SELECT D.ID_DELEGA, " +
					"        D.ID_INTERMEDIARIO " +
					" FROM   DB_DELEGA D, " +
					"        DB_INTERMEDIARIO I " +
					" WHERE  D.ID_AZIENDA = ? " +
					" AND    D.DATA_FINE IS NULL " +
					" AND    SUBSTR(I.CODICE_FISCALE, 1, 3) = ? " +
					" AND    I.TIPO_INTERMEDIARIO = 'C' " +
					" AND    D.ID_PROCEDIMENTO = ? " +
					" AND    D.ID_INTERMEDIARIO = I.ID_INTERMEDIARIO ";
				}
			}


			stmt = conn.prepareStatement(query2);

			SolmrLogger.debug(this, "Executing utenteConDelega: "+query);

			// Questa parte la mantengo solo nel caso in cui UMA si appoggiasse ancora a questo
			// metodo: va tolta ed eliminata appena UMA verrà allineata ai nuovi criteri di profilazione
			// in quanto questa query è errata rispetto alla nuova profilazione di anagrafe
			if(!Validator.isNotEmpty(utenteAbilitazioni.getEnteAppartenenza().getIntermediario()) 
			    || !Validator.isNotEmpty(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello())) 
			{
				stmt.setLong(1, utenteAbilitazioni.getIdUtenteLogin().longValue());
				stmt.setLong(2, idAzienda.longValue());
				if (!Validator.isNotEmpty(idProcedimento) 
				    || idProcedimento.equalsIgnoreCase((String)SolmrConstants.get("ID_TIPO_PROCEDIMENTO_UMA"))) 
				{
					stmt.setString(3, (String) SolmrConstants.get("ID_TIPO_PROCEDIMENTO_UMA"));
				}
				else 
				{
					stmt.setString(3, idProcedimento);
				}
			}
			// SEZIONE DI ANAGRAFE CORRETTA
			else 
			{
				// Se l'intermediario che si logga possiede livello = "Z"
				if(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase((String)SolmrConstants.get("LIVELLO_INTERMEDIARIO_UFFICIO_ZONA"))) {
					stmt.setLong(1, idAzienda.longValue());
					stmt.setLong(2, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getIdIntermediario());
					stmt.setString(3, (String)SolmrConstants.get("ID_TIPO_PROCEDIMENTO_ANAG"));
				}
				// Se l'intermediario che si logga possiede livello = "P"
				else if(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase((String)SolmrConstants.get("LIVELLO_INTERMEDIARIO_PROVINCIALE"))) {
					stmt.setLong(1, idAzienda.longValue());
					stmt.setString(2, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getCodiceEnte().substring(0, 6));
					stmt.setString(3, (String)SolmrConstants.get("ID_TIPO_PROCEDIMENTO_ANAG"));
				}
				// Se l'intermediario che si logga possiede livello = "R"
				else if(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase((String)SolmrConstants.get("LIVELLO_INTERMEDIARIO_REGIONALE"))) {
					stmt.setLong(1, idAzienda.longValue());
					stmt.setString(2, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getCodiceEnte().substring(0, 3));
					stmt.setString(3, (String)SolmrConstants.get("ID_TIPO_PROCEDIMENTO_ANAG"));
				}
			}

			rs = stmt.executeQuery();

			if(!rs.next()) 
			{
				throw new DataControlException(AnagErrors.PROBLEMI_GESTIONE_DELEGA);
			}
			rs.close();
		}
		catch(SQLException sqle) {
			SolmrLogger.fatal(this, "utenteConDelega - SQLException: "+sqle.getMessage());
			throw new DataAccessException(sqle.getMessage());
		}
		catch(DataControlException dce) {
			throw dce;
		}
		catch (Exception ex) {
			SolmrLogger.fatal(this, "utenteConDelega - Generic Exception: "+ex);
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			}
			catch (SQLException exc) {
				SolmrLogger.fatal(this, "utenteConDelega - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.fatal(this, "utenteConDelega - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
	}

	public Vector<DelegaVO> getRangeDelegaByRangeAzienda(Vector<Long> vectAzienda) throws DataAccessException, NotFoundException
	{

		Vector<DelegaVO> result = new Vector<DelegaVO>();
		Connection conn = null;
		PreparedStatement stmt = null;
		String inCondition;
		try {
			conn = getDatasource().getConnection();
			if(vectAzienda != null && !vectAzienda.isEmpty()){
				Iterator<Long> iterator = vectAzienda.iterator();
				inCondition = ((Long)iterator.next()).toString();
				while(iterator.hasNext()){
					inCondition +=", "+((Long)iterator.next()).toString();
				}
			}
			else
				inCondition = "-1";


			String query =  "SELECT D.ID_DELEGA, D.ID_AZIENDA, "+
			"       D.ID_INTERMEDIARIO ALIAS_INTERM, "+
			"       I.DENOMINAZIONE ALIAS_DESC_INTERM, "+
			"       D.ID_PROCEDIMENTO, D.DATA_INIZIO, D.DATA_FINE, "+
			"       D.ID_UTENTE_INSERIMENTO_DELEGA, D.ID_UTENTE_FINE_DELEGA, "+
			"       D.ID_UFFICIO_ZONA_INTERMEDIARIO, "+
			"       I.CODICE_FISCALE " +
			"  FROM DB_DELEGA D, DB_INTERMEDIARIO I "+
			" WHERE D.ID_INTERMEDIARIO = I.ID_INTERMEDIARIO "+
			"   AND D.ID_AZIENDA IN ("+inCondition+") "+
			"   AND D.DATA_FINE IS NULL " +
			"   AND D.ID_PROCEDIMENTO = ? ";

			stmt = conn.prepareStatement(query);

			stmt.setString(1, String.valueOf(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE));

			SolmrLogger.debug(this, "Executing query: "+query);

			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				DelegaVO dVO = new DelegaVO();
				dVO.setIdDelega(new Long(rs.getLong("ID_DELEGA")));
				dVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
				dVO.setIdIntermediario(new Long(rs.getLong("ALIAS_INTERM")));
				dVO.setDenomIntermediario(rs.getString("ALIAS_DESC_INTERM"));
				dVO.setIdProcedimento(new Long(rs.getLong("ID_PROCEDIMENTO")));
				dVO.setDataInizio(rs.getDate("DATA_INIZIO"));
				dVO.setDataFine(rs.getDate("DATA_FINE"));
				dVO.setIdUtenteInsDelega(new Long(rs.getLong("ID_UTENTE_INSERIMENTO_DELEGA")));
				dVO.setIdUtenteFineDelega(new Long(rs.getLong("ID_UTENTE_FINE_DELEGA")));
				dVO.setIdUfficioZonaIntermediario(new Long(rs.getLong("ID_UFFICIO_ZONA_INTERMEDIARIO")));
				dVO.setCodiceFiscaleIntermediario(rs.getString("CODICE_FISCALE"));
				result.add(dVO);
			}
			rs.close();
			SolmrLogger.debug(this, "Executed query");
		}
		catch (SQLException exc) {
			SolmrLogger.fatal(this, "getRangeDelegaByRangeAzienda - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} catch (Exception ex) {
			SolmrLogger.fatal(this, "getRangeDelegaByRangeAzienda - Generic Exception: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, "getRangeDelegaByRangeAzienda - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "getRangeDelegaByRangeAzienda - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return result;
	}

	public PersonaFisicaVO fillAnagWithProvAndCom(PersonaFisicaVO result) throws DataAccessException, NotFoundException, SolmrException {

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getDatasource().getConnection();

			String search = "SELECT c.descom descom, p.descrizione descrizione, "+
			"       p.sigla_provincia sigla, c.flag_estero flag "+
			"  FROM Comune c, Provincia p "+
			" WHERE c.istat_provincia = p.istat_provincia "+
			"   AND c.istat_comune = ? ";
			if(result.getNascitaComune()!=null && !result.getNascitaComune().equals("")){
				stmt = conn.prepareStatement(search);

				SolmrLogger.debug(this, "Executing query: "+search);

				stmt.setString(1, result.getNascitaComune());

				ResultSet rs = stmt.executeQuery();
				String flagEstero1 = "";
				String aliasDescom1 = "";
				String siglaProv1 = "";

				if (rs != null) {
					while (rs.next()) {
						aliasDescom1 = rs.getString("descom");
						result.setDescNascitaComune(aliasDescom1);
						siglaProv1 = rs.getString("sigla");
						flagEstero1 = rs.getString("flag");

						result.setNascitaProv(siglaProv1);
						if(flagEstero1.equals(SolmrConstants.FLAG_S)){
							result.setLuogoNascita(aliasDescom1);
							result.setNascitaStatoEstero(aliasDescom1);
						}
						else{
							result.setLuogoNascita(aliasDescom1+" ("+siglaProv1+")");
							result.setNascitaCittaEstero("");
						}
					}
				} else
					throw new DataAccessException();

				rs.close();

			}
			if(result.getResComune()!=null && !result.getResComune().equals("")){
				stmt = conn.prepareStatement(search);

				SolmrLogger.debug(this, "Executing query: "+search);

				stmt.setString(1, result.getResComune());

				ResultSet rs = stmt.executeQuery();
				String flagEstero2 = "";
				String aliasDescom2 = "";
				String siglaProv2 = "";
				String descProv2 = "";

				if (rs != null) {
					while (rs.next()) {
						aliasDescom2 = rs.getString("descom");
						result.setDescResComune(aliasDescom2);
						siglaProv2 = rs.getString("sigla");
						flagEstero2 = rs.getString("flag");
						descProv2 = rs.getString("descrizione");

						if(flagEstero2.equals(SolmrConstants.FLAG_S)){
							result.setDescResProvincia("");
							result.setDescResComune("");
							result.setResCAP("");
							result.setResProvincia("");
							result.setStatoEsteroRes(aliasDescom2);
							result.setDescStatoEsteroResidenza(aliasDescom2);
						}
						else{
							result.setDescResProvincia(descProv2);
							result.setDescResComune(aliasDescom2);
							result.setResProvincia(siglaProv2);
							result.setStatoEsteroRes("");
							result.setResCittaEstero("");
						}
					}
				}
				else
					throw new DataAccessException();

				rs.close();
				stmt.close();
			}
			/*if (result.getResComune()!=null&&result.getResComune().equals(result.getNascitaComune())) {
  if(flagEstero.equals(SolmrConstants.FLAG_S)){
    result.setDescResProvincia("");
    result.setDescResComune("");
    result.setResCAP("");
    result.setStatoEsteroRes(aliasDescom1);
  }
  else{
    result.setDescResProvincia(descProv);
    result.setDescResComune(aliasDescom1);
    result.setStatoEsteroRes("");
    result.setResCittaEstero("");
  }
      } else {
  stmt.close();

  stmt = conn.prepareStatement(search);

  SolmrLogger.debug(this, "Executing query: "+search);

  stmt.setString(1, result.getResComune());

  ResultSet rs2 = stmt.executeQuery();

  if (rs2 != null) {
    while (rs2.next()) {
      String aliasDescom2 = rs2.getString("descom");
      if(rs2.getString("flag_estero").equals(SolmrConstants.FLAG_S)){
        result.setDescResProvincia("");
        result.setDescResComune("");
        result.setResCAP("");
        result.setStatoEsteroRes(aliasDescom2);
      }
      else{
        result.setDescResProvincia(rs2.getString("descrizione"));
        result.setDescResComune(aliasDescom2);
        result.setStatoEsteroRes("");
        result.setResCittaEstero("");
      }
    }
  } else
     throw new DataAccessException();

     rs2.close();
      }*/

			SolmrLogger.debug(this, "Executed fill.");
		} catch (SQLException exc) {
			SolmrLogger.fatal(this, "SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} catch (DataAccessException daexc) {
			SolmrLogger.fatal(this, "ResultSet null");
			throw daexc;
		} catch (Exception ex) {
			SolmrLogger.fatal(this, "Generic Exception: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, "SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return result;
	}
	/* Restituisce le informazioni sulle aziende da utilizzare nelle attestazioni di proprietà*/
	public AnagraficaAzVO getDatiAziendaPerMacchine(Long idAzienda)throws DataAccessException, NotFoundException{

		AnagraficaAzVO anagVO = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getDatasource().getConnection();
			String find = "SELECT AA.CUAA, AA.PARTITA_IVA, AA.DENOMINAZIONE, "+
			"       AA.SEDELEG_INDIRIZZO, AA.SEDELEG_COMUNE, "+
			"       C.FLAG_ESTERO, C.DESCOM, P.SIGLA_PROVINCIA, "+
			"       AA.SEDELEG_CAP, AA.SEDELEG_CITTA_ESTERO "+
			"  FROM DB_ANAGRAFICA_AZIENDA AA, COMUNE C, PROVINCIA P "+
			" WHERE AA.SEDELEG_COMUNE = C.ISTAT_COMUNE(+) "+
			"   AND C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) "+
			"   AND AA.ID_AZIENDA = ? "+
			"   AND AA.DATA_FINE_VALIDITA IS NULL ";
			stmt = conn.prepareStatement(find);
			SolmrLogger.debug(this, "Executing query: "+find);
			stmt.setLong(1, idAzienda.longValue());
			ResultSet rs = stmt.executeQuery();
			if (rs != null){
				if(rs.next()) {
					anagVO = new AnagraficaAzVO();
					anagVO.setCUAA(rs.getString(1));
					anagVO.setPartitaIVA(rs.getString(2));
					anagVO.setDenominazione(rs.getString(3));
					anagVO.setSedelegIndirizzo(rs.getString(4));
					anagVO.setSedelegComune(rs.getString(5));
					String flagEstero = rs.getString(6);
					if(flagEstero==null){
						anagVO.setDescComune("");
						anagVO.setSedelegProv("");
						anagVO.setSedelegCAP("");
						anagVO.setSedelegEstero("");
						anagVO.setSedelegCittaEstero("");
					}
					else if(flagEstero.equals(SolmrConstants.FLAG_S)){
						anagVO.setSedelegEstero(rs.getString(7));
						anagVO.setSedelegCittaEstero(rs.getString(10));
						anagVO.setDescComune("");
						anagVO.setSedelegProv("");
						anagVO.setSedelegCAP("");
					}
					else{
						anagVO.setDescComune(rs.getString(7));
						anagVO.setSedelegProv(rs.getString(8));
						anagVO.setSedelegCAP(rs.getString(9));
						anagVO.setSedelegEstero("");
						anagVO.setSedelegCittaEstero("");
					}
				}
				rs.close();
			}
			else
				throw new DataAccessException();

			if (anagVO == null)
				throw new NotFoundException(SolmrErrors.EXC_NOT_FOUND);
			SolmrLogger.debug(this, "Executed query - Found record with key: "+idAzienda);
		} catch (SQLException exc) {
			SolmrLogger.fatal(this, "getDatiAziendaPerMacchine - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} catch (DataAccessException daexc) {
			SolmrLogger.fatal(this, "getDatiAziendaPerMacchine - ResultSet null");
			throw daexc;
		} catch (NotFoundException nfexc) {
			SolmrLogger.fatal(this, "getDatiAziendaPerMacchine - NotFoundException: "+nfexc.getMessage());
			throw nfexc;
		} catch (Exception ex) {
			SolmrLogger.fatal(this, "getDatiAziendaPerMacchine - Generic Exception: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, "getDatiAziendaPerMacchine - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "getDatiAziendaPerMacchine - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return anagVO;
	}

	public AnagAziendaVO findAziendaAttivabyCriterio(String cuaa, String partitaIva) throws DataAccessException, NotFoundException {

		AnagAziendaVO result = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getDatasource().getConnection();

			String find =   
			"SELECT   AA.ID_ANAGRAFICA_AZIENDA, " +
			"         AA.ID_AZIENDA, "+
			"         AA.DATA_INIZIO_VALIDITA, " +
			"         AA.DATA_FINE_VALIDITA, "+
			"         AA.CUAA ALIAS_CUAA, " +
			"         AA.PARTITA_IVA, " +
			"         AA.DENOMINAZIONE ANAG_DENOMINAZIONE,  "+
			"         AA.ID_FORMA_GIURIDICA ALIAS_FORMA_GIURIDICA, "+
			"         AA.ID_TIPOLOGIA_AZIENDA ALIAS_TIPOLOGIA_AZIENDA, "+
			"         AA.ID_ATTIVITA_ATECO ALIAS_ATTIVITA_ATECO, "+
			"         AA.ID_ATTIVITA_OTE ALIAS_ATTIVITA_OTE, "+
			"         AA.ID_UDE, " +
			"         AA.ID_DIMENSIONE_AZIENDA, " +
			"         AA.RLS, " +
			"         AA.ULU AS ULU, " +
			"         TU.CLASSE_UDE, " +
			"         TDA.DESCRIZIONE AS DES_DIM_AZIENDA, " + 
			"         AA.ESONERO_PAGAMENTO_GF, " +
			"         TFG.DESCRIZIONE ALIAS_DESC_TFG, "+
			"         TAT.DESCRIZIONE ALIAS_DESC_TAT, "+
			"         TOT.DESCRIZIONE ALIAS_DESC_TOT, "+
			"         TAT.CODICE ALIAS_CODICE_TAT, " +
			"         TOT.CODICE ALIAS_CODICE_TOT, "+
			"         TTA.DESCRIZIONE ALIAS_DESC_TTA, "+
			"         AA.PROVINCIA_COMPETENZA, " +
			"         P2.SIGLA_PROVINCIA ALIAS_PROV_COMPETENZA, "+
			"         AA.CCIAA_PROVINCIA_REA, " +
			"         AA.CCIAA_NUMERO_REA, "+
			"         AA.CCIAA_NUMERO_REGISTRO_IMPRESE, " +
			"         AA.CCIAA_ANNO_ISCRIZIONE, "+
			"         P1.ISTAT_PROVINCIA ALIAS_ISTAT_PROV, "+
			"         P1.SIGLA_PROVINCIA ALIAS_PROV, " +
			"         AA.SEDELEG_COMUNE, " +
			"         COMUNE.FLAG_ESTERO ALIAS_SEDELE_ESTERO, "+
			"         COMUNE.DESCOM ALIAS_DESCOM, " +
			"         AA.SEDELEG_INDIRIZZO, " +
			"         AA.SEDELEG_CAP, "+
			"         AA.SEDELEG_CITTA_ESTERO, "+
			"         AA.MAIL ALIAS_MAIL, " +
			"         AA.SITOWEB, " +
			"         AA.NOTE, " +
			"         AA.DATA_CESSAZIONE, "+
			"         AA.CAUSALE_CESSAZIONE, " +
			"         AA.DATA_AGGIORNAMENTO, "+
			"         AA.ID_UTENTE_AGGIORNAMENTO, " +
			"         AA.MOTIVO_MODIFICA,  "+
			"         A.FASCICOLO_DEMATERIALIZZATO, " +
			"         A.FLAG_AZIENDA_PROVVISORIA, "+
			"         A.ID_AZIENDA_PROVENIENZA," +
			"         AA.INTESTAZIONE_PARTITA_IVA, "+
			"         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
		    "          || ' ' " + 
		    "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
		    "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
		    "         WHERE AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
			"         AS UT_DENOMINAZIONE, " +
			"         (SELECT PVU.DENOMINAZIONE " +
		    "          FROM PAPUA_V_UTENTE_LOGIN PVU " +
		    "          WHERE AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
			"         AS UT_ENTE_APPART, "+
			"         AA.TELEFONO, " +
			"         AA.FAX, " +
			"         AA.PEC, " +
			"         AA.CODICE_AGRITURISMO, " +
			"         AA.DATA_AGGIORNAMENTO_UMA, " +
			"         AA.FLAG_IAP, " +
			"         AA.DATA_ISCRIZIONE_REA, " +
      "         AA.DATA_CESSAZIONE_REA, " +
      "         AA.DATA_ISCRIZIONE_RI, " +
      "         AA.DATA_CESSAZIONE_RI, " +
      "         AA.DATA_INIZIO_ATECO " +
			"FROM     DB_ANAGRAFICA_AZIENDA AA, "+
			"         DB_TIPO_FORMA_GIURIDICA TFG, "+
			"         DB_TIPO_TIPOLOGIA_AZIENDA TTA, "+
			"         DB_TIPO_ATTIVITA_ATECO TAT,   "+
			"         DB_TIPO_ATTIVITA_OTE TOT,   "+
			"         DB_TIPO_UDE TU," +
			"         DB_TIPO_DIMENSIONE_AZIENDA TDA, " +  
			"         DB_AZIENDA A,   "+
			"         COMUNE, " +
			"         PROVINCIA P1, " +
			"         PROVINCIA P2 " +
			//"         PAPUA_V_UTENTE_LOGIN PVU "+
			"   WHERE AA.SEDELEG_COMUNE = COMUNE.ISTAT_COMUNE(+) "+
			"     AND COMUNE.ISTAT_PROVINCIA = P1.ISTAT_PROVINCIA(+) "+
			"     AND AA.PROVINCIA_COMPETENZA = P2.ISTAT_PROVINCIA(+) "+
			"     AND AA.ID_FORMA_GIURIDICA = TFG.ID_FORMA_GIURIDICA(+) "+
			"     AND AA.ID_TIPOLOGIA_AZIENDA = TTA.ID_TIPOLOGIA_AZIENDA(+) "+
			"     AND AA.ID_ATTIVITA_ATECO = TAT.ID_ATTIVITA_ATECO(+) "+
			"     AND AA.ID_ATTIVITA_OTE = TOT.ID_ATTIVITA_OTE(+) "+
			"     AND AA.ID_UDE = TU.ID_UDE(+) " +
			"     AND AA.ID_DIMENSIONE_AZIENDA = TDA.ID_DIMENSIONE_AZIENDA(+) " +
		//	"     AND AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN "+
			"     AND AA.DATA_CESSAZIONE IS NULL "+
			"     AND A.ID_AZIENDA = AA.ID_AZIENDA "+
			"     AND AA.DATA_FINE_VALIDITA IS NULL ";

			if(cuaa != null && !cuaa.equals("")) {
				find += "AND AA.CUAA = ?";
			}
			if(partitaIva != null && !partitaIva.equals("")) {
				find += "AND AA.PARTITA_IVA = ?";
			}

			find += "	ORDER BY AA.DATA_INIZIO_VALIDITA DESC ";

			stmt = conn.prepareStatement(find);

			if(cuaa != null && !cuaa.equals("")) {
				stmt.setString(1, cuaa.toUpperCase());
			}
			else {
				stmt.setString(1, partitaIva);
			}

			SolmrLogger.debug(this, "Executing query: "+find);
			ResultSet rs = stmt.executeQuery();


			if(rs.next())
			{
				result = new AnagAziendaVO();
				result.setIntestazionePartitaIva(rs.getString("INTESTAZIONE_PARTITA_IVA"));
				result.setIdAnagAzienda(new Long(rs.getLong("ID_ANAGRAFICA_AZIENDA")));

				result.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
				result.setDataInizioVal(rs.getDate("DATA_INIZIO_VALIDITA"));
				result.setDataFineVal(rs.getDate("DATA_FINE_VALIDITA"));
				result.setCUAA(rs.getString("ALIAS_CUAA"));
				result.setPartitaIVA(rs.getString("PARTITA_IVA"));
				result.setDenominazione(rs.getString("ANAG_DENOMINAZIONE"));
				result.setIdUde(checkLongNull(rs.getString("ID_UDE")));
				result.setIdDimensioneAzienda(checkLongNull(rs.getString("ID_DIMENSIONE_AZIENDA")));
				result.setRls(rs.getBigDecimal("RLS"));
				result.setUlu(rs.getBigDecimal("ULU"));
				result.setClasseUde(checkLongNull(rs.getString("CLASSE_UDE")));
				result.setDescDimensioneAzienda(rs.getString("DES_DIM_AZIENDA"));
				result.setEsoneroPagamentoGF(rs.getString("ESONERO_PAGAMENTO_GF"));
				result.setDataAggiornamentoUma(rs.getTimestamp("DATA_AGGIORNAMENTO_UMA"));
				result.setFlagIap(rs.getString("FLAG_IAP"));
				result.setDataIscrizioneRea(rs.getTimestamp("DATA_ISCRIZIONE_REA"));
        result.setDataCessazioneRea(rs.getTimestamp("DATA_CESSAZIONE_REA"));
        result.setDataIscrizioneRi(rs.getTimestamp("DATA_ISCRIZIONE_RI"));
        result.setDataCessazioneRi(rs.getTimestamp("DATA_CESSAZIONE_RI"));
        result.setDataInizioAteco(rs.getTimestamp("DATA_INIZIO_ATECO"));

				String codFormaGiur = rs.getString("ALIAS_FORMA_GIURIDICA");
				if(codFormaGiur!=null && !codFormaGiur.equals("")){
					result.setTipoFormaGiuridica(new CodeDescription(
							new Integer(codFormaGiur),
							rs.getString("ALIAS_DESC_TFG")));
				}
				else{
					result.setTipoFormaGiuridica(new CodeDescription());
				}

				String codTipolAzienda = rs.getString("ALIAS_TIPOLOGIA_AZIENDA");
				if(codTipolAzienda!=null) {
					result.setTipoTipologiaAzienda(new CodeDescription(new Integer(codTipolAzienda),
							rs.getString("ALIAS_DESC_TTA")));
					result.setTipiAzienda(new Integer(codTipolAzienda).toString());
				}
				else {
					result.setTipoTipologiaAzienda(new CodeDescription());
					result.setTipiAzienda("");
				}

				String codATECO = rs.getString("ALIAS_ATTIVITA_ATECO");
				if(codATECO!=null) {
					CodeDescription codeATECO = new CodeDescription(new Integer(codATECO),
							rs.getString("ALIAS_DESC_TAT"));
					codeATECO.setSecondaryCode(rs.getString("ALIAS_CODICE_TAT"));
					result.setTipoAttivitaATECO(codeATECO);
				}
				else {
					result.setTipoAttivitaATECO(new CodeDescription());
				}
				String codOTE = rs.getString("ALIAS_ATTIVITA_OTE");
				if(codOTE!=null) {
					CodeDescription codeOTE = new CodeDescription(new Integer(codOTE),
							rs.getString("ALIAS_DESC_TOT"));
					codeOTE.setSecondaryCode(rs.getString("ALIAS_CODICE_TOT"));
					result.setTipoAttivitaOTE(codeOTE);
				}
				else {
					result.setTipoAttivitaOTE(new CodeDescription());
				}
				result.setTipoTipologiaAzienda(new CodeDescription(new Integer(rs.getInt("ALIAS_TIPOLOGIA_AZIENDA")),
						rs.getString("ALIAS_DESC_TTA")));

				result.setProvCompetenza(rs.getString("PROVINCIA_COMPETENZA"));
				result.setSiglaProvCompetenza(rs.getString("ALIAS_PROV_COMPETENZA"));
				result.setCCIAAprovREA(rs.getString("CCIAA_PROVINCIA_REA"));
				result.setCCIAAnumeroREA(new Long(rs.getLong("CCIAA_NUMERO_REA")));
				result.setCCIAAnumRegImprese(rs.getString("CCIAA_NUMERO_REGISTRO_IMPRESE"));
				result.setCCIAAannoIscrizione(rs.getString("CCIAA_ANNO_ISCRIZIONE"));
				result.setSedelegComune(rs.getString("SEDELEG_COMUNE"));
				String flagEstero = rs.getString("ALIAS_SEDELE_ESTERO");
				if(flagEstero==null || flagEstero.equals("")){
					result.setSedelegComune("");
					result.setSedelegEstero("");
					result.setStatoEstero("");
					result.setSedelegIstatProv("");
					result.setSedelegProv("");
					result.setDescComune("");
					result.setSedelegCAP("");
					result.setSedelegCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
				}
				else if(flagEstero.equals(SolmrConstants.FLAG_S)){
					String sedelegEstero = rs.getString("ALIAS_DESCOM");
					result.setSedelegEstero(sedelegEstero);
					result.setStatoEstero(sedelegEstero);
					result.setSedelegIstatProv("");
					result.setSedelegProv("");
					result.setDescComune("");
					result.setSedelegCAP("");
					result.setSedelegCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
				}
				else{
					result.setSedelegIstatProv(rs.getString("ALIAS_ISTAT_PROV"));
					result.setSedelegProv(rs.getString("ALIAS_PROV"));
					result.setDescComune(rs.getString("ALIAS_DESCOM"));
					result.setSedelegCAP(rs.getString("SEDELEG_CAP"));
					result.setSedelegCittaEstero("");
					result.setSedelegEstero("");
					result.setStatoEstero("");
				}

				result.setSedelegIndirizzo(rs.getString("SEDELEG_INDIRIZZO"));
				result.setMail(rs.getString("ALIAS_MAIL"));
				result.setSitoWEB(rs.getString("SITOWEB"));
				result.setTelefono(rs.getString("TELEFONO"));
				result.setFax(rs.getString("FAX"));
				result.setPec(rs.getString("PEC"));
				result.setCodiceAgriturismo(rs.getString("CODICE_AGRITURISMO"));
				result.setNote(rs.getString("NOTE"));
				result.setDataCessazione(rs.getDate("DATA_CESSAZIONE"));
				result.setCausaleCessazione(rs.getString("CAUSALE_CESSAZIONE"));
				java.util.Date dataAgg = rs.getDate("DATA_AGGIORNAMENTO");
				result.setDataAggiornamento(dataAgg);
				result.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				String motivoModifica = rs.getString("MOTIVO_MODIFICA");
				String utDenominazione = rs.getString("UT_DENOMINAZIONE");
				String utEnteAppart = rs.getString("UT_ENTE_APPART");


				result.setDataSituazioneAlStr(DateUtils.getCurrentDateString());

				String ultimaModifica = DateUtils.formatDate(dataAgg);
				String tmp="";
				if(utDenominazione!=null && !utDenominazione.equals("")) {
					if(tmp.equals(""))
						tmp+=" (";
					tmp+=utDenominazione;
				}
				if(utEnteAppart!=null && !utEnteAppart.equals("")) {
					if(tmp.equals(""))
						tmp+=" (";
					else
						tmp+=" - ";
					tmp+=utEnteAppart;
				}
				if(motivoModifica!=null && !motivoModifica.equals("")) {
					if(tmp.equals(""))
						tmp+=" (";
					else
						tmp+=" - ";
					tmp+=motivoModifica;
				}
				if(!tmp.equals(""))
					tmp+=")";
				ultimaModifica+=tmp;
				result.setUltimaModifica(ultimaModifica);

				//Ultima Modifica Spezzata
				result.setDataUltimaModifica(dataAgg);
				result.setUtenteUltimaModifica(utDenominazione);
				result.setEnteUltimaModifica(utEnteAppart);
				result.setMotivoModifica(motivoModifica);


				/**
				 * Questa porzione di codice va a vedere se l'azienda con cui sto
				 * lavorando è un'azienda provvisoria
				 */
				String temp=rs.getString("FLAG_AZIENDA_PROVVISORIA");
				if ("S".equals(temp))
				{
					result.setFlagAziendaProvvisoria(true);
					temp=rs.getString("ID_AZIENDA_PROVENIENZA");
					if (temp!=null)
						result.setIdAziendaSubentro(new Long(temp));
				}
				else
					result.setFlagAziendaProvvisoria(false);
				
				result.setFascicoloDematerializzato(rs.getString("FASCICOLO_DEMATERIALIZZATO"));
				
			}

			rs.close();

			if (result == null)
				throw new NotFoundException(SolmrErrors.EXC_NOT_FOUND_PK);

		} catch (SQLException exc) {
			SolmrLogger.fatal(this, "findByPrimaryKey - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} catch (NotFoundException nfexc) {
			SolmrLogger.fatal(this, "findByPrimaryKey - NotFoundException: "+nfexc.getMessage());
			throw nfexc;
		} catch (Exception ex) {
			SolmrLogger.fatal(this, "findByPrimaryKey - Generic Exception: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, "findByPrimaryKey - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "findByPrimaryKey - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return result;
	}

	// metodo che restituisce la denominzione dell'ultima occorrenza dell'azienda
	// a partire dall'id dell'azienda stessa
	public String getDenominazioneByIdAzienda(Long idAzienda) throws DataAccessException, NotFoundException {

		String result = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getDatasource().getConnection();

			String find =   "  SELECT DENOMINAZIONE "+
			"    FROM DB_AZIENDA A, DB_ANAGRAFICA_AZIENDA AA "+
			"   WHERE A.ID_AZIENDA = AA.ID_AZIENDA"+
			"     AND A.DATA_INIZIO_VALIDITA = AA.DATA_INIZIO_VALIDITA "+
			"     AND A.ID_AZIENDA = ? ";

			stmt = conn.prepareStatement(find);

			SolmrLogger.debug(this, "Executing query: "+find);

			stmt.setLong(1, idAzienda.longValue());

			ResultSet rs = stmt.executeQuery();
			if (rs!=null){
				while(rs.next()){
					result = rs.getString(1);
				}
				rs.close();
			}
			SolmrLogger.debug(this, "Executed query - Found record with key: "+idAzienda);
		} catch (SQLException exc) {
			SolmrLogger.fatal(this, "getDenominazioneByIdAzienda - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} catch (Exception ex) {
			SolmrLogger.fatal(this, "getDenominazioneByIdAzienda - Generic Exception: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, "getDenominazioneByIdAzienda - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "getDenominazioneByIdAzienda - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return result;
	}


	// metodo che restituisce la denominzione dell'ultima occorrenza dell'azienda
	// a partire dall'id dell'azienda stessa
	public String getRappLegaleTitolareByIdAzienda(Long idAzienda) throws DataAccessException {

		String result = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getDatasource().getConnection();

			String find =   "  SELECT NOME, COGNOME "+
			"    FROM DB_PERSONA_FISICA, DB_CONTITOLARE "+
			"   WHERE DB_PERSONA_FISICA.ID_SOGGETTO = DB_CONTITOLARE.ID_SOGGETTO "+
			"     AND DATA_FINE_RUOLO IS NULL "+
			"     AND ID_RUOLO = "+SolmrConstants.TIPORUOLO_TITOL_RAPPR_LEG+
			"     AND ID_AZIENDA = ? ";

			stmt = conn.prepareStatement(find);

			SolmrLogger.debug(this, "Executing query: "+find);

			stmt.setLong(1, idAzienda.longValue());

			ResultSet rs = stmt.executeQuery();
			if (rs!=null){
				while(rs.next()){
					result = rs.getString(1)+" "+rs.getString(2);
				}
				rs.close();
			}
			SolmrLogger.debug(this, "Executed query - Found record with key: "+idAzienda);
		} catch (SQLException exc) {
			SolmrLogger.fatal(this, "getRappLegaleTitolareByIdAzienda - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} catch (Exception ex) {
			SolmrLogger.fatal(this, "getRappLegaleTitolareByIdAzienda - Generic Exception: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, "getRappLegaleTitolareByIdAzienda - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "getRappLegaleTitolareByIdAzienda - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return result;
	}



	public Vector<AnagAziendaVO> findAziendeByIdAziende(Vector<Long> idAziendeVect) throws DataAccessException, NotFoundException, SolmrException {

		Vector<AnagAziendaVO> result = new Vector<AnagAziendaVO>();
		AnagAziendaVO aaVO = null;
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = getDatasource().getConnection();

			String daQuery = 
			"select AA.ID_ANAGRAFICA_AZIENDA, " +
			"       AA.id_azienda, " +
			"       AA.denominazione, " +
			"       AA.INTESTAZIONE_PARTITA_IVA, " +
			"       AA.CUAA ALIAS_CUAA, "+
			"       AA.partita_iva, " +
			"       COMUNE.DESCOM ALIAS_DESCOM, "+
			"       AA.sedeleg_indirizzo, "+
			"       P1.SIGLA_PROVINCIA ALIAS_PROV, " +
			"       AA.SEDELEG_CAP, "+
			"       P1.ISTAT_PROVINCIA ALIAS_ISTAT_PROV, "+
			"       COMUNE.FLAG_ESTERO ALIAS_SEDELE_ESTERO, "+
			"       AA.SEDELEG_CITTA_ESTERO, "+
			"       AA.ID_FORMA_GIURIDICA, " +
			"       A.FASCICOLO_DEMATERIALIZZATO, "+
			"       A.FLAG_AZIENDA_PROVVISORIA, "+
			"       A.ID_AZIENDA_PROVENIENZA, "+
			"       AA.data_cessazione "+
			"from   db_anagrafica_azienda AA, " +
			"       comune, " +
			"       provincia p1, "+
			"       provincia p2, " +
			"       db_azienda a "+
			"where  aa.SEDELEG_COMUNE = comune.ISTAT_COMUNE(+) "+
			"AND    COMUNE.ISTAT_PROVINCIA = P1.ISTAT_PROVINCIA(+) "+
			"AND    AA.PROVINCIA_COMPETENZA = P2.ISTAT_PROVINCIA(+)  "+
			"and    a.ID_AZIENDA = aa.ID_AZIENDA "+
			"and    a.DATA_INIZIO_VALIDITA = aa.DATA_INIZIO_VALIDITA ";

			// Metto tra parentesi la query complessiva, UNION di query "puntuali",
			// per essere SICURO che ORDER BY 3 agisca su tutti i record recuperati
			String query = "(" + daQuery;

			for(int i=0;i<idAziendeVect.size();i++){
				query += " AND AA.ID_AZIENDA = ? ";
				if (i!=idAziendeVect.size()-1) {
					query += " UNION "+daQuery;
				}
			}

			query += ") ORDER BY 3 "; // Ordinamento per denominazione

			stmt = conn.prepareStatement(query);
			SolmrLogger.debug(this, "Executing query: "+query);

			int idx = 0;
			Iterator<Long> iter = idAziendeVect.iterator();
			while (iter.hasNext()) {
				Long daLong = (Long)iter.next();
				stmt.setLong(++idx, daLong.longValue());
				if (SolmrLogger.isDebugEnabled(this)) {
					SolmrLogger.debug(this, "Parametro: "+(idx)+" - "+daLong);
				}
			}

			ResultSet rs = stmt.executeQuery();

			if (rs != null) {
				while(rs.next()){
					aaVO = new AnagAziendaVO();
					aaVO.setIdAnagAzienda(new Long(rs.getLong("ID_ANAGRAFICA_AZIENDA")));
					aaVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
					aaVO.setDenominazione(rs.getString("DENOMINAZIONE"));
					aaVO.setIntestazionePartitaIva(rs.getString("INTESTAZIONE_PARTITA_IVA"));
					aaVO.setCUAA(rs.getString("ALIAS_CUAA"));
					aaVO.setPartitaIVA(rs.getString("PARTITA_IVA"));

					aaVO.setSedelegIndirizzo(rs.getString("SEDELEG_INDIRIZZO"));
					aaVO.setIdFormaGiuridica(rs.getLong("ID_FORMA_GIURIDICA")==0?null:new Long(rs.getLong("ID_FORMA_GIURIDICA")));
					aaVO.setDescComune(rs.getString("ALIAS_DESCOM"));
					String flagEstero = rs.getString("ALIAS_SEDELE_ESTERO");
					if(flagEstero==null || flagEstero.equals("")){
						aaVO.setDescComune("");
						aaVO.setSedelegEstero("");
						aaVO.setStatoEstero("");
						aaVO.setSedelegIstatProv("");
						aaVO.setSedelegProv("");
						aaVO.setSedelegComune("");
						aaVO.setSedelegCAP("");
						aaVO.setSedelegCittaEstero("");
					}
					else if(flagEstero.equals(SolmrConstants.FLAG_S)){
						String sedelegEstero = rs.getString("ALIAS_DESCOM");
						aaVO.setSedelegEstero(sedelegEstero);
						aaVO.setStatoEstero(sedelegEstero);
						aaVO.setSedelegIstatProv("");
						aaVO.setSedelegProv("");
						aaVO.setSedelegComune("");
						aaVO.setSedelegCAP("");
						aaVO.setSedelegCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
					}
					else{
						aaVO.setSedelegIstatProv(rs.getString("ALIAS_ISTAT_PROV"));
						aaVO.setSedelegProv(rs.getString("ALIAS_PROV"));
						aaVO.setSedelegComune(rs.getString("ALIAS_DESCOM"));
						aaVO.setSedelegCAP(rs.getString("SEDELEG_CAP"));
						aaVO.setSedelegCittaEstero("");
						aaVO.setSedelegEstero("");
						aaVO.setStatoEstero("");
					}
					if(Validator.isNotEmpty(rs.getString("DATA_CESSAZIONE"))) {
						aaVO.setDataCessazione(rs.getDate("DATA_CESSAZIONE"));
					}

					/**
					 * Questa porzione di codice va a vedere se l'azienda con cui sto
					 * lavorando è un'azienda provvisoria
					 */
					String temp=rs.getString("FLAG_AZIENDA_PROVVISORIA");
					if ("S".equals(temp))
					{
						aaVO.setFlagAziendaProvvisoria(true);
						temp=rs.getString("ID_AZIENDA_PROVENIENZA");
						if (temp!=null)
							aaVO.setIdAziendaSubentro(new Long(temp));
					}
					else
						aaVO.setFlagAziendaProvvisoria(false);
					
					aaVO.setFascicoloDematerializzato(rs.getString("FASCICOLO_DEMATERIALIZZATO"));

					result.add(aaVO);
				}
				rs.close();
			}
			else
				throw new DataAccessException();

			if (result.size()==0){
				throw new NotFoundException(SolmrErrors.EXC_NOT_FOUND_PK);
			}

		} catch (SQLException exc) {
			SolmrLogger.fatal(this, "findAziendeByIdAziende - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} catch (Exception ex) {
			SolmrLogger.fatal(this, "findAziendeByIdAziende - Generic Exception: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, "findAziendeByIdAziende - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "findAziendeByIdAziende - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return result;
	}

	public Long esisteDelegaAziendaIntermediario(Long idAzienda, Long idIntermediario) throws DataAccessException
	{

		Connection conn = null;
		PreparedStatement stmt = null;
		Long result = null;
		try {
			conn = getDatasource().getConnection();

			String query =  "SELECT ID_DELEGA "+
			"  FROM DB_DELEGA D "+
			" WHERE ID_AZIENDA = ? "+
			"   AND ID_INTERMEDIARIO = ? "+
			"   AND ID_PROCEDIMENTO = "+SolmrConstants.ID_TIPO_PROCEDIMENTO_UMA+
			"   AND DATA_INIZIO = TRUNC(SYSDATE) FOR UPDATE";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing query: "+query);

			stmt.setLong(1, idAzienda.longValue());
			stmt.setLong(2, idIntermediario.longValue());
			ResultSet rs = stmt.executeQuery();
			if(rs != null){
				if (rs.next()){
					result = new Long(rs.getInt(1));
				}
				rs.close();
			}
		}

		catch (SQLException exc) {
			SolmrLogger.fatal(this, "esisteDelegaAziendaIntermediario - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} catch (Exception ex) {
			SolmrLogger.fatal(this, "esisteDelegaAziendaIntermediario - Generic Exception: "+ex);
			//ex.printStackTrace(System.out);
			throw new DataAccessException(ex.getMessage());
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, "esisteDelegaAziendaIntermediario - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "esisteDelegaAziendaIntermediario - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return result;
	}


	public Vector<ProcedureAttiveVO> findProcedureAttive(Long idAzienda) throws DataAccessException, NotFoundException, SolmrException {

		ProcedureAttiveVO procedureAttiveVO = null;
		Vector<ProcedureAttiveVO> result = new Vector<ProcedureAttiveVO>();
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getDatasource().getConnection();

			String find = "SELECT ID_AZIENDA, DB_PROCEDIMENTO_AZIENDA.ID_PROCEDIMENTO, "+
			"  DB_TIPO_PROCEDIMENTO.DESCRIZIONE, STATO "+
			"  FROM DB_TIPO_PROCEDIMENTO, DB_PROCEDIMENTO_AZIENDA "+
			" WHERE DB_PROCEDIMENTO_AZIENDA.ID_AZIENDA = ? "+
			" AND DB_PROCEDIMENTO_AZIENDA.ID_PROCEDIMENTO = DB_TIPO_PROCEDIMENTO.ID_PROCEDIMENTO";

			stmt = conn.prepareStatement(find);
			stmt.setLong(1, idAzienda.longValue());
			SolmrLogger.debug(this, "Executing query: "+find);

			ResultSet rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					procedureAttiveVO = new ProcedureAttiveVO();
					procedureAttiveVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
					String codProcAttive = rs.getString("ID_PROCEDIMENTO");
					procedureAttiveVO.setTipoProcedimento(new CodeDescription(
							new Integer(codProcAttive),
							rs.getString("DESCRIZIONE")));
					procedureAttiveVO.setStato(new Long(rs.getLong("STATO")));
					result.add(procedureAttiveVO);
				}
				rs.close();
			} else
				throw new DataAccessException();

		} catch (SQLException exc) {
			SolmrLogger.fatal(this, "findProcedureAttive - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} catch (DataAccessException daexc) {
			SolmrLogger.fatal(this, "findProcedureAttive - ResultSet null");
			throw daexc;
		} catch (Exception ex) {
			SolmrLogger.fatal(this, "findProcedureAttive - Generic Exception: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, "findProcedureAttive - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "findProcedureAttive - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return result;
	}

	

	/*
	 * Aggiunto il 6/12/2004 per aggirare (temporaneamente) il mancato
	 * inserimento della chiamata al metodo omonimo di DittaUmaDAO di UMA
	 * tramite CSI
	 */
	//#-#
	public DittaUMAVO getDittaUmaByIdAzienda(Long idAzienda) throws DataAccessException
	{

		DittaUMAVO duVO=null;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getDatasource().getConnection();

			String query = " SELECT "+
			"  ID_DITTA_UMA, "+
			"  EXT_ID_AZIENDA, "+
			"  EXT_PROVINCIA_UMA, "+
			"  TIPO_DITTA, "+
			"  DITTA_UMA, "+
			"  DATA_ISCRIZIONE, "+
			"  DATA_CESSAZIONE, "+
			"  EXT_ID_UTENTE_AGGIORNAMENTO, "+
			"  DATA_AGGIORNAMENTO, "+
			"  P.SIGLA_PROVINCIA, "+
			"  P.DESCRIZIONE "+
			"FROM  "+
			"  DB_DITTA_UMA DU, "+
			"  PROVINCIA P "+
			"WHERE  "+
			"  DU.EXT_ID_AZIENDA = ? "+
			"  AND DU.DATA_CESSAZIONE IS NULL "+
			"  AND P.ISTAT_PROVINCIA=DU.EXT_PROVINCIA_UMA ";


			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing query: "+query);

			stmt.setLong(1, idAzienda.longValue());
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
			{
				duVO=new DittaUMAVO();
				duVO.setIdDitta(new Long(rs.getLong("ID_DITTA_UMA")));
				duVO.setExtIdAzienda(new Long(rs.getLong("EXT_ID_AZIENDA")));
				duVO.setExtProvinciaUMA(rs.getString("EXT_PROVINCIA_UMA"));
				duVO.setTipoDitta(rs.getString("TIPO_DITTA"));
				duVO.setDittaUMA(rs.getString("DITTA_UMA"));
				duVO.setDataIscrizione(rs.getDate("DATA_ISCRIZIONE"));
				duVO.setDataCessazione(rs.getDate("DATA_CESSAZIONE"));
				duVO.setExtIdUtenteAggiornamento(new Long(rs.getLong("EXT_ID_UTENTE_AGGIORNAMENTO")));
				duVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
				duVO.setProvincia(rs.getString("SIGLA_PROVINCIA"));
				duVO.setDescExtProvinciaUMA(rs.getString("DESCRIZIONE"));
			}
			rs.close();
			SolmrLogger.debug(this, "Executed query - Found record with key: "+idAzienda);
		}
		catch (SQLException exc)
		{
			SolmrLogger.fatal(this, "getDittaUmaByIdAzienda - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex)
		{
			SolmrLogger.fatal(this, "getDittaUmaByIdAzienda - Generic Exception: "+ex);
			//ex.printStackTrace(System.out);
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
				SolmrLogger.fatal(this, "getDittaUmaByIdAzienda - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "getDittaUmaByIdAzienda - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return duVO;
	}


	/**
	 * Questo metodo controlla se un'azienda è cessata return true se è cessata
	 * return false se non è cessata
	 *
	 * @param idanagraficaAzienda Long
	 * @throws DataAccessException
	 * @return boolean
	 */
	public boolean controllaAziendaCessata(Long idanagraficaAzienda)
	throws DataAccessException
	{

		boolean result=false;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getDatasource().getConnection();

			String query = " SELECT DAA.ID_ANAGRAFICA_AZIENDA FROM "+
			"  DB_ANAGRAFICA_AZIENDA DAA "+
			"  WHERE DAA.ID_ANAGRAFICA_AZIENDA = ? "+
			"  AND DAA.DATA_CESSAZIONE IS NOT NULL ";


			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing query: "+query);

			stmt.setLong(1, idanagraficaAzienda.longValue());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) result=true;
			rs.close();
			SolmrLogger.debug(this, "Executed query - Found record with key: "+idanagraficaAzienda);
		}
		catch (SQLException exc)
		{
			SolmrLogger.fatal(this, "controllaAziendaCessata - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex)
		{
			SolmrLogger.fatal(this, "controllaAziendaCessata - Generic Exception: "+ex);
			//ex.printStackTrace(System.out);
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
				SolmrLogger.fatal(this, "controllaAziendaCessata - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "controllaAziendaCessata - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return result;
	}

	/**
	 * Questo metodo controlla se la data di fine validita di un'azienda è null o
	 * no return true se data di fine validità diverso da null return false
	 * altrimenti
	 *
	 * @param idanagraficaAzienda Long
	 * @throws DataAccessException
	 * @return boolean
	 */
	public boolean controllaAziendaFineValidita(Long idanagraficaAzienda)
	throws DataAccessException
	{

		boolean result=false;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getDatasource().getConnection();

			String query = " SELECT DAA.ID_ANAGRAFICA_AZIENDA FROM "+
			"  DB_ANAGRAFICA_AZIENDA DAA "+
			"  WHERE DAA.ID_ANAGRAFICA_AZIENDA = ? "+
			"  AND DAA.DATA_FINE_VALIDITA IS NOT NULL ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing query: "+query);

			stmt.setLong(1, idanagraficaAzienda.longValue());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) result=true;
			rs.close();
			SolmrLogger.debug(this, "Executed query - Found record with key: "+idanagraficaAzienda);
		}
		catch (SQLException exc)
		{
			SolmrLogger.fatal(this, "controllaAziendaFineValidita - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex)
		{
			SolmrLogger.fatal(this, "controllaAziendaFineValidita - Generic Exception: "+ex);
			//ex.printStackTrace(System.out);
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
				SolmrLogger.fatal(this, "controllaAziendaFineValidita - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "controllaAziendaFineValidita - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return result;
	}

	/**
	 * Questo metodo controlla è presente una delega per questa azienda
	 * relativamente al procedimento di anagrafe. Se trova un record rilancia
	 * un'eccezione. Se non trova nessun record, controlla se l'azienda prevede
	 * l'obbligo del fascicolo. Restituisce true se l'azienda prevede l'obbligo
	 * False altrimenti.
	 *
	 */
	public boolean controllaDelegaObbligoFascicolo(AnagAziendaVO aziendaVO) throws DataAccessException {

		boolean result=true;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = getDatasource().getConnection();

			String query = " SELECT DA.ID_AZIENDA "+
			" FROM   DB_AZIENDA DA "+
			" WHERE  DA.ID_AZIENDA=? "+
			" AND    DA.FLAG_COMPETENZA_ESCLUSIVA_PA = 'S' ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing query: "+query);

			stmt.setLong(1, aziendaVO.getIdAzienda().longValue());
			rs = stmt.executeQuery();

			if (rs.next())
				result=false;

			rs.close();

		}
		catch (SQLException exc)
		{
			SolmrLogger.fatal(this, "controllaDelegaObbligoFascicolo - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex)
		{
			SolmrLogger.fatal(this, "controllaDelegaObbligoFascicolo - Generic Exception: "+ex);
			//ex.printStackTrace(System.out);
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
				SolmrLogger.fatal(this, "controllaDelegaObbligoFascicolo - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "controllaDelegaObbligoFascicolo - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return result;
	}

	/**
	 * Questo metodo controlla se l'azienda prevede l'obbligo del fascicolo.
	 * Restituisce true se l'azienda prevede l'obbligo False altrimenti.
	 *
	 * @param aziendaVO AnagAziendaVO
	 * @throws DataAccessException
	 * @return boolean
	 */
	public boolean controllaObbligoFascicolo(AnagAziendaVO aziendaVO)
	throws DataAccessException
	{

		boolean result=true;
		Connection conn = null;
		PreparedStatement stmt = null;
		String query = null;
		try
		{
			conn = getDatasource().getConnection();

			query = " SELECT DA.ID_AZIENDA "+
			"  FROM DB_AZIENDA DA "+
			"  WHERE DA.ID_AZIENDA=? "+
			"  AND DA.FLAG_COMPETENZA_ESCLUSIVA_PA = 'S' ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing query: "+query);

			stmt.setLong(1, aziendaVO.getIdAzienda().longValue());
			ResultSet rs = stmt.executeQuery();

			if (rs.next())
				result=false;

			rs.close();

		}
		catch (SQLException exc)
		{
			SolmrLogger.fatal(this, "controllaObbligoFascicolo - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex)
		{
			SolmrLogger.fatal(this, "controllaObbligoFascicolo - Generic Exception: "+ex);
			//ex.printStackTrace(System.out);
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
				SolmrLogger.fatal(this, "controllaObbligoFascicolo - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "controllaObbligoFascicolo - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return result;
	}

	/**
	 * Questo metodo controlla se è presente una delega per questa azienda
	 * relativamente al procedimento di anagrafe. Se è presente restituisce true.
	 * Se non è presente restituisce false
	 *
	 * @param aziendaVO AnagAziendaVO
	 * @throws DataAccessException
	 * @return boolean
	 */
	public boolean controllaPresenzaDelega(AnagAziendaVO aziendaVO)
	throws DataAccessException
	{

		boolean result=false;
		Connection conn = null;
		PreparedStatement stmt = null;
		try
		{
			conn = getDatasource().getConnection();

			String query = " SELECT DD.ID_INTERMEDIARIO,DD.CODICE_AMMINISTRAZIONE "+
			"  FROM DB_DELEGA DD "+
			"  WHERE DD.ID_AZIENDA = ? "+
			"  AND DD.ID_PROCEDIMENTO = "+
			((String)SolmrConstants.get("ID_TIPO_PROCEDIMENTO_ANAG"))+
			"  AND DD.DATA_FINE IS NULL ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing query: "+query);

			stmt.setLong(1, aziendaVO.getIdAzienda().longValue());
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				result=true;
			rs.close();

		}
		catch (SQLException exc)
		{
			SolmrLogger.fatal(this, "controllaPresenzaDelega - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex)
		{
			SolmrLogger.fatal(this, "controllaPresenzaDelega - Generic Exception: "+ex);
			//ex.printStackTrace(System.out);
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
				SolmrLogger.fatal(this, "controllaPresenzaDelega - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "controllaPresenzaDelega - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return result;
	}


	/**
	 * Restituisce se l'azienda è bloccata, oopure sono presneti warning ecc..
	 *
	 * @param idAzienda Long
	 * @return it.csi.solmr.dto.CodeDescription
	 * @throws DataAccessException
	 */
	/*public CodeDescription serviceIsAziendaBloccata(Long idAzienda)
	throws DataAccessException
	{

		Connection conn = null;
		PreparedStatement stmt = null;
		CodeDescription notifica = null;

		try
		{
			conn = getDatasource().getConnection();

			String query = null;

			query="SELECT N.ID_TIPOLOGIA_NOTIFICA,TTN.DESCRIZIONE "+
			"FROM DB_NOTIFICA N,DB_TIPO_TIPOLOGIA_NOTIFICA TTN "+
			"WHERE N.ID_AZIENDA=? AND N.DATA_CHIUSURA IS NULL "+
			"AND N.ID_TIPOLOGIA_NOTIFICA=TTN.ID_TIPOLOGIA_NOTIFICA "+
			"ORDER BY N.ID_TIPOLOGIA_NOTIFICA";

			stmt = conn.prepareStatement(query);
			stmt.setLong(1,idAzienda.longValue());

			SolmrLogger.debug(this, "Executing query: "+query);

			ResultSet rs = stmt.executeQuery();

			if (rs.next())
			{
				notifica= new CodeDescription();
				notifica.setCode(new Integer(rs.getInt("ID_TIPOLOGIA_NOTIFICA")));
				notifica.setDescription(rs.getString("DESCRIZIONE"));
			}

			stmt.close();
			rs.close();
		}
		catch (SQLException exc) {
			SolmrLogger.fatal(this, "isAziendaBloccata - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex) {
			SolmrLogger.fatal(this, "isAziendaBloccata - Generic Exception: "+ex);
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.fatal(this, "isAziendaBloccata - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.fatal(this, "isAziendaBloccata - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return notifica;
	}*/

	// Metodo per controllare se l'utente intermediario che si è loggato possiede una delega diretta
	// o tramite id_intermediario padre
	public DelegaVO intermediarioConDelega(UtenteAbilitazioni utenteAbilitazioni, Long idAzienda) throws DataAccessException, SolmrException {


		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		DelegaVO delegaVO = null;

		// Modifica effettuata per correggere l'anomalia dell'assenza dell'id_procedimento sugli utenti
		// UMA.Il metodo dovrà comunque essere modificato perchè in questo modo non potrà essere utilizzato
		// da altri applicativi diversi da UMA
		String idProcedimento = null;
		try {

			conn = getDatasource().getConnection();

			String query = " SELECT PVU.ID_PROCEDIMENTO " +
			" FROM   PAPUA_V_UTENTE_LOGIN PVU " +
			" WHERE  PVU.ID_UTENTE_LOGIN = ? ";

			stmt = conn.prepareStatement(query);
			stmt.setLong(1, utenteAbilitazioni.getIdUtenteLogin().longValue());

			rs = stmt.executeQuery();

			if(rs.next()) {
				idProcedimento = rs.getString(1);
			}
			
			rs.close();

			stmt.close();

			String query2 = null;

			// Questa parte la mantengo solo nel caso in cui UMA si appoggiasse ancora a questo
			// metodo: va tolta ed eliminata appena UMA verrà allineata ai nuovi criteri di profilazione
			// in quanto questa query è errata rispetto alla nuova profilazione di anagrafe
			if(!Validator.isNotEmpty(utenteAbilitazioni.getEnteAppartenenza().getIntermediario()) || !Validator.isNotEmpty(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello())) {
				query2 = " SELECT D.ID_DELEGA, " +
				"        D.ID_INTERMEDIARIO " +
				" FROM   DB_DELEGA D, " +
				"        DB_INTERMEDIARIO I " +
				" WHERE  I.ID_INTERMEDIARIO = D.ID_INTERMEDIARIO " +
				" AND    (I.ID_INTERMEDIARIO = ? OR I.ID_INTERMEDIARIO_PADRE = ?) " +
				" AND    D.ID_AZIENDA = ? " +
				" AND    D.DATA_INIZIO <= SYSDATE " +
				" AND    (D.DATA_FINE IS NULL OR D.DATA_FINE >= SYSDATE) " +
				" AND    D.ID_PROCEDIMENTO = ? ";
				stmt = conn.prepareStatement(query2);
			}
			// SEZIONE DI ANAGRAFE CORRETTA
			else {
				// Se l'intermediario che si logga possiede livello = "Z"
				if(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase((String)SolmrConstants.get("LIVELLO_INTERMEDIARIO_UFFICIO_ZONA"))) {
					query2 = " SELECT D.ID_DELEGA, " +
					"        D.ID_INTERMEDIARIO " +
					" FROM   DB_DELEGA D, " +
					"        DB_INTERMEDIARIO I " +
					" WHERE  D.ID_AZIENDA = ? " +
					" AND    D.DATA_FINE IS NULL " +
					" AND    D.ID_INTERMEDIARIO = ? " +
					" AND    D.ID_PROCEDIMENTO = ? " +
					" AND    D.ID_INTERMEDIARIO = I.ID_INTERMEDIARIO ";
				}
				// Se l'intermediario che si logga possiede livello = "P"
				else if(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase((String)SolmrConstants.get("LIVELLO_INTERMEDIARIO_PROVINCIALE"))) {
					query2 = " SELECT D.ID_DELEGA, " +
					"        D.ID_INTERMEDIARIO " +
					" FROM   DB_DELEGA D, " +
					"        DB_INTERMEDIARIO I " +
					" WHERE  D.ID_AZIENDA = ? " +
					" AND    D.DATA_FINE IS NULL " +
					" AND    SUBSTR(I.CODICE_FISCALE, 1, 6) = ? " +
					" AND    I.TIPO_INTERMEDIARIO = 'C' " +
					" AND    D.ID_PROCEDIMENTO = ? " +
					" AND    D.ID_INTERMEDIARIO = I.ID_INTERMEDIARIO ";
				}
				// Se l'intermediario che si logga possiede livello = "R"
				else if(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase((String)SolmrConstants.get("LIVELLO_INTERMEDIARIO_REGIONALE"))) {
					query2 = " SELECT D.ID_DELEGA, " +
					"        D.ID_INTERMEDIARIO " +
					" FROM   DB_DELEGA D, " +
					"        DB_INTERMEDIARIO I " +
					" WHERE  D.ID_AZIENDA = ? " +
					" AND    D.DATA_FINE IS NULL " +
					" AND    SUBSTR(I.CODICE_FISCALE, 1, 3) = ? " +
					" AND    I.TIPO_INTERMEDIARIO = 'C' " +
					" AND    D.ID_PROCEDIMENTO = ? " +
					" AND    D.ID_INTERMEDIARIO = I.ID_INTERMEDIARIO ";
				}
			}


			SolmrLogger.debug(this, "Executing intermediarioConDelega: "+query);

			stmt = conn.prepareStatement(query2);

			// Questa parte la mantengo solo nel caso in cui UMA si appoggiasse ancora a questo
			// metodo: va tolta ed eliminata appena UMA verrà allineata ai nuovi criteri di profilazione
			// in quanto questa query è errata rispetto alla nuova profilazione di anagrafe
			if(!Validator.isNotEmpty(utenteAbilitazioni.getEnteAppartenenza().getIntermediario()) || !Validator.isNotEmpty(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello())) {
				stmt.setLong(1, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getIdIntermediario());
				stmt.setLong(2, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getIdIntermediario());
				stmt.setLong(3, idAzienda.longValue());
				if(!Validator.isNotEmpty(idProcedimento) || idProcedimento.equalsIgnoreCase((String)SolmrConstants.get("ID_TIPO_PROCEDIMENTO_UMA"))) {
					stmt.setString(4, (String) SolmrConstants.get("ID_TIPO_PROCEDIMENTO_UMA"));
				}
				else {
					stmt.setString(4, idProcedimento);
				}
			}
			// SEZIONE DI ANAGRAFE CORRETTA
			else {
				// Se l'intermediario che si logga possiede livello = "Z"
				if(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase((String)SolmrConstants.get("LIVELLO_INTERMEDIARIO_UFFICIO_ZONA"))) {
					stmt.setLong(1, idAzienda.longValue());
					stmt.setLong(2, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getIdIntermediario());
					stmt.setString(3, (String)SolmrConstants.get("ID_TIPO_PROCEDIMENTO_ANAG"));
				}
				// Se l'intermediario che si logga possiede livello = "P"
				else if(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase((String)SolmrConstants.get("LIVELLO_INTERMEDIARIO_PROVINCIALE"))) {
					stmt.setLong(1, idAzienda.longValue());
					stmt.setString(2, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getCodiceEnte() .substring(0, 6));
					stmt.setString(3, (String)SolmrConstants.get("ID_TIPO_PROCEDIMENTO_ANAG"));
				}
				// Se l'intermediario che si logga possiede livello = "R"
				else if(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase((String)SolmrConstants.get("LIVELLO_INTERMEDIARIO_REGIONALE"))) {
					stmt.setLong(1, idAzienda.longValue());
					stmt.setString(2, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getCodiceEnte().substring(0, 3));
					stmt.setString(3, (String)SolmrConstants.get("ID_TIPO_PROCEDIMENTO_ANAG"));
				}
			}
			rs = stmt.executeQuery();

			if(rs.next()) {
				delegaVO = new DelegaVO();
				delegaVO.setIdDelega(new Long(rs.getLong(1)));
				delegaVO.setIdIntermediario(new Long(rs.getLong(2)));
			}

			if(delegaVO == null) {
				throw new SolmrException((String)AnagErrors.get("INTERMEDIARIO_SENZA_DELEGA"));
			}

			rs.close();
		}
		catch(SQLException sqle) {
			SolmrLogger.fatal(this, "intermediarioConDelega - SQLException: "+sqle.getMessage());
			throw new DataAccessException(sqle.getMessage());
		}
		catch(SolmrException se) {
			SolmrLogger.fatal(this, "intermediarioConDelega - SQLException: "+se.getMessage());
			throw new SolmrException(se.getMessage());
		}
		catch (Exception ex) {
			SolmrLogger.fatal(this, "intermediarioConDelega - Generic Exception: "+ex);
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			}
			catch (SQLException exc) {
				SolmrLogger.fatal(this, "intermediarioConDelega - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.fatal(this, "intermediarioConDelega - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return delegaVO;
	}


	// Metodo per controllare se l'utente intermediario che si è loggato possiede una delega diretta
	public boolean isIntermediarioConDelegaDiretta(long idIntermediario, Long idAzienda)
	throws DataAccessException
	{

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean result=false;

		try {

			conn = getDatasource().getConnection();


			String query = " SELECT D.ID_DELEGA, " +
			"        D.ID_INTERMEDIARIO " +
			" FROM   DB_DELEGA D, " +
			"        DB_INTERMEDIARIO I " +
			" WHERE  I.ID_INTERMEDIARIO = D.ID_INTERMEDIARIO " +
			" AND    I.ID_INTERMEDIARIO = ?  " +
			" AND    D.ID_AZIENDA = ? " +
			" AND    D.DATA_INIZIO <= SYSDATE " +
			" AND    (D.DATA_FINE IS NULL OR D.DATA_FINE >= SYSDATE) " +
			" AND    D.ID_PROCEDIMENTO = ? ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing intermediarioConDelegaDiretta: "+query);
			stmt.setLong(1, idIntermediario);
			stmt.setLong(2, idAzienda.longValue());
			stmt.setString(3, (String)SolmrConstants.get("ID_TIPO_PROCEDIMENTO_ANAG"));

			rs = stmt.executeQuery();

			if(rs.next()) result=true;

			rs.close();
		}
		catch(SQLException sqle) {
			SolmrLogger.fatal(this, "intermediarioConDelegaDiretta - SQLException: "+sqle.getMessage());
			throw new DataAccessException(sqle.getMessage());
		}
		catch (Exception ex) {
			SolmrLogger.fatal(this, "intermediarioConDelegaDiretta - Generic Exception: "+ex);
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			}
			catch (SQLException exc) {
				SolmrLogger.fatal(this, "intermediarioConDelegaDiretta - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.fatal(this, "intermediarioConDelegaDiretta - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return result;
	}

	// Metodo per controllare se l'utente intermediario che si è loggato è il padre
	//di un intemediario che possiede la delega sull'azienda
	public boolean isIntermediarioPadre(long idIntermediario, Long idAzienda)
	throws DataAccessException
	{


		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean result=false;

		try {

			conn = getDatasource().getConnection();


			String query = " SELECT D.ID_DELEGA, " +
			"        D.ID_INTERMEDIARIO " +
			" FROM   DB_DELEGA D, " +
			"        DB_INTERMEDIARIO I " +
			" WHERE  I.ID_INTERMEDIARIO = D.ID_INTERMEDIARIO " +
			" AND    I.ID_INTERMEDIARIO_PADRE = ?  " +
			" AND    D.ID_AZIENDA = ? " +
			" AND    D.DATA_INIZIO <= SYSDATE " +
			" AND    (D.DATA_FINE IS NULL OR D.DATA_FINE >= SYSDATE) " +
			" AND    D.ID_PROCEDIMENTO = ? ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing intermediarioPadre: "+query);

			stmt.setLong(1, idIntermediario);
			stmt.setLong(2, idAzienda.longValue());
			stmt.setString(3, (String)SolmrConstants.get("ID_TIPO_PROCEDIMENTO_ANAG"));

			rs = stmt.executeQuery();

			if(rs.next()) result=true;

			rs.close();
		}
		catch(SQLException sqle) {
			SolmrLogger.fatal(this, "intermediarioPadre - SQLException: "+sqle.getMessage());
			throw new DataAccessException(sqle.getMessage());
		}
		catch (Exception ex) {
			SolmrLogger.fatal(this, "intermediarioPadre - Generic Exception: "+ex);
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			}
			catch (SQLException exc) {
				SolmrLogger.fatal(this, "intermediarioPadre - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.fatal(this, "intermediarioPadre - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return result;
	}


	/**
	 * Questo metodo viene utilizzato dal testagent per permettere il monitoraggio
	 * dell'infrastruttuta dei servizi
	 *
	 * @throws DataAccessException
	 * @return boolean
	 */
	public boolean testResources()
	throws DataAccessException
	{

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getDatasource().getConnection();

			stmt = conn.prepareStatement("SELECT * FROM DB_TIPO_PROCEDIMENTO");

			ResultSet rs = stmt.executeQuery();
			rs.close();
		}
		catch (SQLException exc)
		{
			SolmrLogger.fatal(this, "testResources - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex)
		{
			SolmrLogger.fatal(this, "testResources - Generic Exception: "+ex.getMessage());
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
				SolmrLogger.fatal(this, "testResources - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex)
			{
				SolmrLogger.fatal(this, "testResources - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return true;
	}


	/**
	 * Metodo per richiamare la procedura plsql che ci dice se possiamo cessare
	 * un'azienda o no Il parametro restituito è un vettore di 2 stringhe dove la
	 * prima può assumere tre valori: - S l'azienda può essere cessata - W
	 * visualizzare a video p_msg e chiedere all'utente se vuole proseguire - B
	 * non è possibile proseguire non la cessazione dell'azienda. Visualizzare il
	 * seguente messaggio: Non è possibile cessare l'azienda: +p_msg La seconda
	 * contiene un messaggio che ha validità solo se il primo è uguale a W o B
	 *
	 * @param idAzienda Long
	 * @throws DataAccessException
	 * @throws SolmrException
	 * @return String[]
	 */
	public String[] cessazioneAziendaPLQSL(Long idAzienda)
	throws DataAccessException, SolmrException
	{

		Connection conn = null;
		CallableStatement cs = null;
		try
		{
			/***
        PROCEDURE CESSAZIONE_AMMESSA( P_ID_AZIENDA IN DB_AZIENDA.ID_AZIENDA%TYPE,
                                      P_ESITO      OUT VARCHAR2,
                                      P_MSG        OUT VARCHAR2,
                                      P_CODERR     OUT VARCHAR2);
			 */
			conn = getDatasource().getConnection();
			String sql = "{call PACK_PRATICA_AZIENDA.CESSAZIONE_AMMESSA(?,?,?,?)}";
			cs = conn.prepareCall(sql);
			cs.setLong(1, idAzienda.longValue());
			cs.registerOutParameter(2,Types.VARCHAR);
			cs.registerOutParameter(3,Types.CLOB);
			cs.registerOutParameter(4,Types.VARCHAR);

			cs.executeUpdate();
			String msgErr = null;
			Clob clob = cs.getClob(3);
      if(clob != null)
      {
        msgErr = clob.getSubString(1, (int) clob.length());
      }
			String errorCode=cs.getString(4);

			if (!(errorCode== null || "".equals(errorCode)))
				throw new SolmrException((String)SolmrErrors.get("GENERIC_SYSTEM_EXCEPTION")+":"+errorCode + " - " + msgErr);
			String risultato[]=new String[2];
			risultato[0]=cs.getString(2);
			risultato[1]=msgErr;
			return risultato;
		}
		catch(SolmrException se)
		{
			throw new SolmrException(se.getMessage());
		}
		catch(SQLException sqle) {
			SolmrLogger.fatal(this, "cessazioneAziendaPLQSL - SQLException: "+sqle.getMessage());
			throw new DataAccessException(sqle.getMessage());
		}
		catch (Exception ex) {
			SolmrLogger.fatal(this, "cessazioneAziendaPLQSL - Generic Exception: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (cs != null) cs.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.fatal(this, "cessazioneAziendaPLQSL - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.fatal(this, "cessazioneAziendaPLQSL - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
	}

	/**
	 *
	 * @param idAzienda Long
	 * @return Il valore restituito indica se l'azienda corrispondente al codice
	 *   contenuto nel parametro è esente da delega (true) oppure no (false)
	 * @throws DataAccessException
	 */
	public Boolean serviceIsEsenteDelega(Long idAzienda)
	throws DataAccessException {

		Boolean isEsenteDelega = null;

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet result = null;
		try {
			conn = getDatasource().getConnection();

			String query = "select FLAG_ESENTE_DELEGA from DB_AZIENDA where ID_AZIENDA=? ";

			stmt = conn.prepareStatement(query);


			SolmrLogger.debug(this, "Executing query: "+query);
			stmt.setLong(1, idAzienda.longValue());
			result = stmt.executeQuery();

			isEsenteDelega = new Boolean(result.next() &&
					SolmrConstants.get("FLAG_S").equals(result.getString("FLAG_ESENTE_DELEGA")));
			
			result.close();

			SolmrLogger.debug(this, "Executed query.");
		} catch (SQLException exc) {
			SolmrLogger.fatal(this, "serviceIsEsenteDelega - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} catch (Exception ex) {
			SolmrLogger.fatal(this, "serviceIsEsenteDelega - Generic Exception: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, "serviceIsEsenteDelega - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "serviceIsEsenteDelega - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return isEsenteDelega;
	}

	public DelegaAnagrafeVO serviceGetDelega(Long idAzienda,
			String codiceEnte,
			Boolean ricercaSuEntiFigli,
			Date data)

	throws DataAccessException
	{

		DelegaAnagrafeVO delegaAnagrafeVO = null;

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet result = null;
		boolean bRicercaSuEntiFigli=ricercaSuEntiFigli!=null && ricercaSuEntiFigli.booleanValue();
		try
		{
			conn = getDatasource().getConnection();
			String query = "SELECT "+
			"  a.ID_INTERMEDIARIO, "+
			"  b.DENOMINAZIONE, "+
			"  b.CODICE_FISCALE, "+
			"  a.DATA_INIZIO, "+
			"  a.DATA_FINE, "+
			"  c.CODICE_AGEA, "+
			"  c.DENOMINAZIONE AS DENOMINAZIONE_UFFICIO, "+
			"  c.INDIRIZZO, "+
			"  e.DESCOM, "+
			"  c.RECAPITO, "+
			"  c.CAP, "+
			"  e.ISTAT_COMUNE, "+
			"  b.RESPONSABILE "+
			"FROM  "+
			"  db_delega a , "+
			"  db_intermediario b, "+
			"  db_ufficio_zona_intermediario c, "+
			"  comune e "+
			"WHERE  "+
			"  id_azienda=? and "+
			"  id_procedimento=7 and "+
			"  a.id_intermediario=b.id_intermediario and "+
			"  a.id_ufficio_zona_intermediario=c.id_ufficio_zona_intermediario(+) and "+
			"  c.COMUNE=e.ISTAT_COMUNE(+) ";
			Long idIntermediario=null;
			String livello=null;
			if (codiceEnte!=null)
			{
				String queryIntermediario="SELECT ID_INTERMEDIARIO,LIVELLO FROM DB_INTERMEDIARIO WHERE CODICE_FISCALE=?";
				SolmrLogger.debug(this, "Executing query: "+queryIntermediario);
				stmt = conn.prepareStatement(queryIntermediario);
				stmt.setString(1, codiceEnte);
				result=stmt.executeQuery();
				if (result.next())
				{
					idIntermediario=new Long(result.getLong("ID_INTERMEDIARIO"));
					SolmrLogger.debug(this, "idIntermediario: "+idIntermediario);
					livello=result.getString("LIVELLO");
					SolmrLogger.debug(this, "livello: "+livello);
				}
				else
				{
					return null;
				}
				stmt.close();
			}
			if (codiceEnte!=null)
			{
				if (!bRicercaSuEntiFigli)
				{
					query+="and b.id_intermediario=? ";
				}
				if (bRicercaSuEntiFigli && "Z".equalsIgnoreCase(livello))
				{
					query+="and b.id_intermediario=? ";
				}
				if (bRicercaSuEntiFigli && "P".equalsIgnoreCase(livello))
				{
					query+="and substr(b.CODICE_FISCALE,1,6)=? ";
				}
				if (bRicercaSuEntiFigli && "R".equalsIgnoreCase(livello))
				{
					query+="and substr(b.CODICE_FISCALE,1,3)=? ";
				}
				
				query+= " AND    B.TIPO_INTERMEDIARIO = 'C' ";
			}
			if (data==null)
			{
				query+="and a.data_fine is null ";
			}
			else
			{
				query+="and (a.data_inizio<=? and (a.data_fine is null or a.data_fine >=?)) ";
			}
			SolmrLogger.debug(this, "Executing query: "+query);
			stmt = conn.prepareStatement(query);
			int index=1;
			stmt.setLong(index++, idAzienda.longValue());
			if (codiceEnte!=null)
			{

				if (!bRicercaSuEntiFigli)
				{
					stmt.setLong(index++, idIntermediario.longValue());
				}
				if (bRicercaSuEntiFigli && "Z".equalsIgnoreCase(livello))
				{
					stmt.setLong(index++, idIntermediario.longValue());
				}
				if (bRicercaSuEntiFigli && "P".equalsIgnoreCase(livello))
				{
					stmt.setString(index++, codiceEnte.substring(0,6));
					SolmrLogger.debug(this,"codiceEnte "+codiceEnte);
					SolmrLogger.debug(this,"codiceEnte.substring(0,6)"+codiceEnte.substring(0,6));
				}
				if (bRicercaSuEntiFigli && "R".equalsIgnoreCase(livello))
				{
					stmt.setString(index++, codiceEnte.substring(0,3));
					SolmrLogger.debug(this,"codiceEnte "+codiceEnte);
					SolmrLogger.debug(this,"codiceEnte.substring(0,3)"+codiceEnte.substring(0,3));
				}
				SolmrLogger.debug(this,"1.2");
			}
			SolmrLogger.debug(this,"2");
			if (data!=null)
			{
				/*java.sql.Date dateSql=checkDate(data);
				stmt.setDate(index++, dateSql);
				stmt.setDate(index++, dateSql); */
			  stmt.setTimestamp(index++, convertDateToTimestamp(data));
			  stmt.setTimestamp(index++, convertDateToTimestamp(data));
			}
			result = stmt.executeQuery();
			if (result.next())
			{
				delegaAnagrafeVO=new DelegaAnagrafeVO();
				delegaAnagrafeVO.setIdIntermediario(result.getString("ID_INTERMEDIARIO"));
				delegaAnagrafeVO.setDenominazione(result.getString("DENOMINAZIONE"));
				delegaAnagrafeVO.setCodiceFiscIntermediario(result.getString("CODICE_FISCALE"));
				delegaAnagrafeVO.setDataInizioDelega(result.getString("DATA_INIZIO"));
				delegaAnagrafeVO.setDataFineDelega(result.getString("DATA_FINE"));
				delegaAnagrafeVO.setCodiceUfficioZona(result.getString("CODICE_AGEA"));
				delegaAnagrafeVO.setDenominazioneUfficioZona(result.getString("DENOMINAZIONE_UFFICIO"));
				delegaAnagrafeVO.setIndirizzoUfficioZona(result.getString("INDIRIZZO"));
				delegaAnagrafeVO.setComuneUfficioZona(result.getString("DESCOM"));
				delegaAnagrafeVO.setCapUfficioZona(result.getString("CAP"));
				delegaAnagrafeVO.setRecapitoUfficioZona(result.getString("RECAPITO"));
				delegaAnagrafeVO.setIstatComuneUfficioZona(result.getString("ISTAT_COMUNE"));
				if (codiceEnte!=null && idIntermediario!=null)
				{
					delegaAnagrafeVO.setFlagFiglio(delegaAnagrafeVO.getIdIntermediario().equals(
							idIntermediario.toString())?SolmrConstants.FLAG_N:SolmrConstants.FLAG_S);
				}
				delegaAnagrafeVO.setResponsabile(result.getString("RESPONSABILE"));
			}
			result.close();
			SolmrLogger.debug(this, "Executed query.");
		}
		catch (SQLException exc)
		{
			SolmrLogger.fatal(this, "serviceGetDelega - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex)
		{
			SolmrLogger.fatal(this, "serviceGetDelega - Generic Exception: "+ex.getMessage());
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
				SolmrLogger.fatal(this, "serviceGetDelega - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex)
			{
				SolmrLogger.fatal(this, "serviceGetDelega - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return delegaAnagrafeVO;
	}

	/**
	 * Metodo utilizzato per recuperare l'elenco dell'aziende in funzione del CUAA
	 *
	 * @param cuaa
	 * @param onlyActive
	 * @param isCessata
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.AnagAziendaVO[]
	 * @throws DataAccessException
	 */
	public AnagAziendaVO[] getListAnagAziendaVOByCuaa(String cuaa, boolean onlyActive, boolean isCessata, String[] orderBy) throws DataAccessException {

		SolmrLogger.debug(this, "Invocating getListAnagAziendaVOByCuaa method in AnagrafeDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<AnagAziendaVO> elencoAziende = new Vector<AnagAziendaVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListAnagAziendaVOByCuaa method in AnagrafeDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListAnagAziendaVOByCuaa method in AnagrafeDAO and it values: "+conn+"\n");

			String query = 
			"SELECT  AA.ID_ANAGRAFICA_AZIENDA, " +
			"        AA.ID_AZIENDA, " +
			"        AA.ID_TIPOLOGIA_AZIENDA, " +
			"        TTA.DESCRIZIONE AS DESC_TIP_AZIENDA, " +
			"        AA.DATA_INIZIO_VALIDITA, " +
			"        AA.DATA_FINE_VALIDITA, " +
			"        AA.CUAA, " +
			"        AA.PARTITA_IVA, " +
			"        AA.DENOMINAZIONE, " +
			"        AA.ID_FORMA_GIURIDICA, " +
			"        TFG.DESCRIZIONE AS DESC_FORMA_GIUR, " +
			"        AA.ID_ATTIVITA_ATECO, " +
			"        TAA.DESCRIZIONE AS DESC_ATECO, " +
			"        AA.ID_UDE, " +
			"        AA.ID_DIMENSIONE_AZIENDA, " +
			"        AA.RLS, " +
			"		     AA.ULU AS ULU, " +
			"        TU.CLASSE_UDE, " +
			"        TDA.DESCRIZIONE AS DES_DIM_AZIENDA, " +
			"        AA.ESONERO_PAGAMENTO_GF, " +
			"        AA.PROVINCIA_COMPETENZA, " +
			"        AA.CCIAA_PROVINCIA_REA, " +
			"        AA.CCIAA_NUMERO_REA, " +
			"        AA.MAIL, " +
			"        AA.SEDELEG_COMUNE, " +
			"        C.DESCOM, " +
			"        P.SIGLA_PROVINCIA, " +
			"        AA.SEDELEG_INDIRIZZO, " +
			"        AA.SITOWEB, " +
			"        AA.SEDELEG_CITTA_ESTERO, " +
			"        AA.SEDELEG_CAP, " +
			"        AA.DATA_CESSAZIONE, " +
			"        AA.CAUSALE_CESSAZIONE, " +
			"        AA.NOTE, " +
			"        AA.DATA_AGGIORNAMENTO, " +
			"        AA.ID_UTENTE_AGGIORNAMENTO, " +
			"         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
		    "          || ' ' " + 
		    "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
		    "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
		    "         WHERE AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
			"        AS DENOMINAZIONE, " +
			"        (SELECT PVU.DENOMINAZIONE " +
		    "         FROM PAPUA_V_UTENTE_LOGIN PVU " +
		    "         WHERE AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
			"        AS DESCRIZIONE_ENTE_APPARTENENZA, " +
			"        AA.ID_ATTIVITA_OTE, " +
			"        TAO.DESCRIZIONE AS DESC_OTE, " +
			"        AA.MOTIVO_MODIFICA, " +
			"        AA.CCIAA_NUMERO_REGISTRO_IMPRESE, " +
			"        AA.CCIAA_ANNO_ISCRIZIONE, " +
			"        AA.MODIFICA_INTERMEDIARIO, " +
			"        AA.NUMERO_AGEA, " +
			"        AA.INTESTAZIONE_PARTITA_IVA, " +
			"        AA.ID_CESSAZIONE, " +
			"        TC.DESCRIZIONE AS DESC_CESSAZIONE, " +
			"        AA.TELEFONO, " +
			"        AA.FAX, " +
			"        AA.PEC, " +
			"        AA.CODICE_AGRITURISMO, " +
			"        AA.DATA_AGGIORNAMENTO_UMA, " +
			"        AA.FLAG_IAP, " +
			"        AA.DATA_ISCRIZIONE_REA, " +
      "        AA.DATA_CESSAZIONE_REA, " +
      "        AA.DATA_ISCRIZIONE_RI, " +
      "        AA.DATA_CESSAZIONE_RI, " +
      "        AA.DATA_INIZIO_ATECO " +
			" FROM   DB_ANAGRAFICA_AZIENDA AA, " +
			"        DB_TIPO_TIPOLOGIA_AZIENDA TTA, " +
			"        DB_TIPO_FORMA_GIURIDICA TFG, " +
			"        DB_TIPO_ATTIVITA_ATECO TAA, " +
			"        COMUNE C, " +
			"        PROVINCIA P, " +
//			"        PAPUA_V_UTENTE_LOGIN PVU, " +
			"        DB_TIPO_ATTIVITA_OTE TAO, " +
			"        DB_TIPO_CESSAZIONE TC," +
			"        DB_TIPO_UDE TU," +
			"        DB_TIPO_DIMENSIONE_AZIENDA TDA " +
			" WHERE  AA.CUAA = UPPER(?) " +
			" AND    AA.ID_TIPOLOGIA_AZIENDA = TTA.ID_TIPOLOGIA_AZIENDA(+) " +
			" AND    AA.ID_FORMA_GIURIDICA = TFG.ID_FORMA_GIURIDICA(+) " +
			" AND    AA.ID_ATTIVITA_ATECO = TAA.ID_ATTIVITA_ATECO(+) " +
			" AND    AA.SEDELEG_COMUNE = C.ISTAT_COMUNE(+) " +
			" AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
		//	" AND    AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
			" AND    AA.ID_ATTIVITA_OTE = TAO.ID_ATTIVITA_OTE(+) " +
			" AND    AA.ID_CESSAZIONE = TC.ID_CESSAZIONE(+) " +
			" AND    AA.ID_UDE = TU.ID_UDE(+) " +
			" AND    AA.ID_DIMENSIONE_AZIENDA = TDA.ID_DIMENSIONE_AZIENDA(+) ";

			if(onlyActive) {
				query +=   " AND    AA.DATA_FINE_VALIDITA IS NULL ";
			}
			if(isCessata) {
				query +=   " AND    AA.DATA_CESSAZIONE IS NOT NULL ";
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

			SolmrLogger.debug(this, "Value of parameter 1 [CUAA] in getListAnagAziendaVOByCuaa method in AnagrafeDAO: "+cuaa+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ONLY_ACTIVE] in getListAnagAziendaVOByCuaa method in AnagrafeDAO: "+onlyActive+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [IS_CESSATA] in getListAnagAziendaVOByCuaa method in AnagrafeDAO: "+isCessata+"\n");
			SolmrLogger.debug(this, "Value of parameter 4 [ORDER_BY] in getListAnagAziendaVOByCuaa method in AnagrafeDAO: "+orderBy+"\n");

			stmt = conn.prepareStatement(query);

			stmt.setString(1, cuaa.toUpperCase());

			SolmrLogger.debug(this, "Executing getListAnagAziendaVOByCuaa: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				AnagAziendaVO anagAziendaVO = new AnagAziendaVO();
				anagAziendaVO.setIdAnagAzienda(new Long(rs.getLong("ID_ANAGRAFICA_AZIENDA")));
				anagAziendaVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_AZIENDA"))) {
					anagAziendaVO.setTipoTipologiaAzienda(new CodeDescription(Integer.decode(rs.getString("ID_TIPOLOGIA_AZIENDA")), rs.getString("DESC_TIP_AZIENDA")));
				}
				anagAziendaVO.setDataInizioVal(rs.getDate("DATA_INIZIO_VALIDITA"));
				anagAziendaVO.setDataFineVal(rs.getDate("DATA_FINE_VALIDITA"));
				anagAziendaVO.setCUAA(rs.getString("CUAA"));
				anagAziendaVO.setPartitaIVA("PARTITA_IVA");
				anagAziendaVO.setDenominazione("DENOMINAZIONE");
				if(Validator.isNotEmpty(rs.getString("ID_FORMA_GIURIDICA"))) {
					anagAziendaVO.setIdFormaGiuridica(Long.decode(rs.getString("ID_FORMA_GIURIDICA")));
					anagAziendaVO.setTipoFormaGiuridica(new CodeDescription(Integer.decode(rs.getString("ID_FORMA_GIURIDICA")), rs.getString("DESC_FORMA_GIUR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_ATTIVITA_ATECO"))) {
					anagAziendaVO.setIdAteco(rs.getString("ID_ATTIVITA_ATECO"));
					anagAziendaVO.setTipoAttivitaATECO(new CodeDescription(Integer.decode(rs.getString("ID_ATTIVITA_ATECO")), rs.getString("DESC_ATECO")));
				}
				anagAziendaVO.setProvCompetenza(rs.getString("PROVINCIA_COMPETENZA"));
				anagAziendaVO.setCCIAAprovREA(rs.getString("CCIAA_PROVINCIA_REA"));
				if(Validator.isNotEmpty(rs.getString("CCIAA_NUMERO_REA"))) {
					anagAziendaVO.setCCIAAnumeroREA(Long.decode(rs.getString("CCIAA_NUMERO_REA")));
				}
				anagAziendaVO.setMail(rs.getString("MAIL"));
				anagAziendaVO.setTelefono(rs.getString("TELEFONO"));
				anagAziendaVO.setFax(rs.getString("FAX"));
				anagAziendaVO.setPec(rs.getString("PEC"));
				anagAziendaVO.setCodiceAgriturismo(rs.getString("CODICE_AGRITURISMO"));
				anagAziendaVO.setSedelegComune(rs.getString("SEDELEG_COMUNE"));
				anagAziendaVO.setDescComune(rs.getString("DESCOM"));
				anagAziendaVO.setSedelegProv(rs.getString("SIGLA_PROVINCIA"));
				anagAziendaVO.setSedelegIndirizzo(rs.getString("SEDELEG_INDIRIZZO"));
				anagAziendaVO.setSitoWEB(rs.getString("SITOWEB"));
				anagAziendaVO.setSedelegCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
				anagAziendaVO.setSedelegCAP(rs.getString("SEDELEG_CAP"));
				anagAziendaVO.setDataCessazione(rs.getDate("DATA_CESSAZIONE"));
				anagAziendaVO.setCausaleCessazione(rs.getString("CAUSALE_CESSAZIONE"));
				anagAziendaVO.setNote(rs.getString("NOTE"));
				anagAziendaVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
				anagAziendaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				anagAziendaVO.setDescrizioneUtenteModifica(rs.getString("DENOMINAZIONE"));
				anagAziendaVO.setDescrizioneEnteUtenteModifica(rs.getString("DESCRIZIONE_ENTE_APPARTENENZA"));
				anagAziendaVO.setIdUde(checkLongNull(rs.getString("ID_UDE")));
				anagAziendaVO.setIdDimensioneAzienda(checkLongNull(rs.getString("ID_DIMENSIONE_AZIENDA")));
				anagAziendaVO.setRls(rs.getBigDecimal("RLS"));
				anagAziendaVO.setUlu(rs.getBigDecimal("ULU"));
				anagAziendaVO.setClasseUde(checkLongNull(rs.getString("CLASSE_UDE")));
				anagAziendaVO.setDescDimensioneAzienda(rs.getString("DES_DIM_AZIENDA"));
				anagAziendaVO.setEsoneroPagamentoGF(rs.getString("ESONERO_PAGAMENTO_GF"));
				anagAziendaVO.setDataAggiornamentoUma(rs.getTimestamp("DATA_AGGIORNAMENTO_UMA"));
				anagAziendaVO.setFlagIap(rs.getString("FLAG_IAP"));
				anagAziendaVO.setDataIscrizioneRea(rs.getTimestamp("DATA_ISCRIZIONE_REA"));
				anagAziendaVO.setDataCessazioneRea(rs.getTimestamp("DATA_CESSAZIONE_REA"));
				anagAziendaVO.setDataIscrizioneRi(rs.getTimestamp("DATA_ISCRIZIONE_RI"));
				anagAziendaVO.setDataCessazioneRi(rs.getTimestamp("DATA_CESSAZIONE_RI"));
				anagAziendaVO.setDataInizioAteco(rs.getTimestamp("DATA_INIZIO_ATECO"));
				
				if(Validator.isNotEmpty(rs.getString("ID_ATTIVITA_OTE"))) {
					anagAziendaVO.setIdOte(rs.getString("ID_ATTIVITA_OTE"));
					anagAziendaVO.setTipoAttivitaOTE(new CodeDescription(Integer.decode(rs.getString("ID_ATTIVITA_OTE")), rs.getString("DESC_OTE")));
				}
				anagAziendaVO.setMotivoModifica(rs.getString("MOTIVO_MODIFICA"));
				anagAziendaVO.setCCIAAnumRegImprese(rs.getString("CCIAA_NUMERO_REGISTRO_IMPRESE"));
				anagAziendaVO.setCCIAAannoIscrizione(rs.getString("CCIAA_ANNO_ISCRIZIONE"));
				anagAziendaVO.setModificaIntermediario(rs.getString("MODIFICA_INTERMEDIARIO"));
				anagAziendaVO.setNumeroAgea(rs.getString("NUMERO_AGEA"));
				anagAziendaVO.setIntestazionePartitaIva(rs.getString("INTESTAZIONE_PARTITA_IVA"));
				if(Validator.isNotEmpty(rs.getString("ID_CESSAZIONE"))) {
					anagAziendaVO.setIdCessazione(new Long(rs.getLong("ID_CESSAZIONE")));
					TipoCessazioneVO tipoCessazioneVO = new TipoCessazioneVO();
					tipoCessazioneVO.setIdCessazione(new Long(rs.getLong("ID_CESSAZIONE")));
					tipoCessazioneVO.setDescrizione(rs.getString("DESC_CESSAZIONE"));
					anagAziendaVO.setTipoCessazioneVO(tipoCessazioneVO);
				}
				elencoAziende.add(anagAziendaVO);
			}

			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListAnagAziendaVOByCuaa in getListAnagAziendaVOByCuaa - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListAnagAziendaVOByCuaa in getListAnagAziendaVOByCuaa - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListAnagAziendaVOByCuaa in getListAnagAziendaVOByCuaa - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListAnagAziendaVOByCuaa in getListAnagAziendaVOByCuaa - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListAnagAziendaVOByCuaa method in getListAnagAziendaVOByCuaa\n");
		if(elencoAziende.size() == 0) {
			return (AnagAziendaVO[])elencoAziende.toArray(new AnagAziendaVO[0]);
		}
		else {
			return (AnagAziendaVO[])elencoAziende.toArray(new AnagAziendaVO[elencoAziende.size()]);
		}
	}

	/**
	 * Metodo che mi permette di estrarre tutte le occorrenze dalla tabella
	 * DB_ANAGRAFICA_AZIENDA in relazione all'id_azienda di destinazione
	 *
	 * @param idAzienda
	 * @param onlyActive
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.AnagAziendaVO[]
	 * @throws DataAccessException
	 */
	public AnagAziendaVO[] getListAnagAziendaDestinazioneByIdAzienda(Long idAzienda, boolean onlyActive, String[] orderBy) throws DataAccessException {

		SolmrLogger.debug(this, "Invocating getListAnagAziendaDestinazioneByIdAzienda method in AnagrafeDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<AnagAziendaVO> elencoAziende = new Vector<AnagAziendaVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListAnagAziendaDestinazioneByIdAzienda method in AnagrafeDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListAnagAziendaDestinazioneByIdAzienda method in AnagrafeDAO and it values: "+conn+"\n");

			String query = 
			"SELECT  AA.ID_ANAGRAFICA_AZIENDA, " +
			"        AA.ID_AZIENDA, " +
			"        AA.ID_TIPOLOGIA_AZIENDA, " +
			"        TTA.DESCRIZIONE AS DESC_TIP_AZIENDA, " +
			"        AA.DATA_INIZIO_VALIDITA, " +
			"        AA.DATA_FINE_VALIDITA, " +
			"        AA.CUAA, " +
			"        AA.PARTITA_IVA, " +
			"        AA.DENOMINAZIONE, " +
			"        AA.ID_FORMA_GIURIDICA, " +
			"        TFG.DESCRIZIONE AS DESC_FORMA_GIUR, " +
			"        AA.ID_ATTIVITA_ATECO, " +
			"        TAA.DESCRIZIONE AS DESC_ATECO, " +
			"        AA.ID_UDE, " +
			"        AA.ID_DIMENSIONE_AZIENDA, " +
			"        AA.RLS, " +
			"		     AA.ULU AS ULU, " +
			"        TU.CLASSE_UDE, " +
			"        TDA.DESCRIZIONE AS DES_DIM_AZIENDA, " +
			"        AA.ESONERO_PAGAMENTO_GF, " +
			"        AA.PROVINCIA_COMPETENZA, " +
			"        AA.CCIAA_PROVINCIA_REA, " +
			"        AA.CCIAA_NUMERO_REA, " +
			"        AA.MAIL, " +
			"        AA.SEDELEG_COMUNE, " +
			"        C.DESCOM, " +
			"        P.SIGLA_PROVINCIA, " +
			"        AA.SEDELEG_INDIRIZZO, " +
			"        AA.SITOWEB, " +
			"        AA.SEDELEG_CITTA_ESTERO, " +
			"        AA.SEDELEG_CAP, " +
			"        AA.DATA_CESSAZIONE, " +
			"        AA.CAUSALE_CESSAZIONE, " +
			"        AA.NOTE, " +
			"        AA.DATA_AGGIORNAMENTO, " +
			"         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
		    "          || ' ' " + 
		    "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
		    "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
		    "         WHERE AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
			"        AS DENOMINAZIONE, " +
			"        (SELECT PVU.DENOMINAZIONE " +
		    "         FROM PAPUA_V_UTENTE_LOGIN PVU " +
		    "         WHERE AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
			"        AS DESCRIZIONE_ENTE_APPARTENENZA, "+ 
			"        AA.ID_ATTIVITA_OTE, " +
			"        TAO.DESCRIZIONE AS DESC_OTE, " +
			"        AA.MOTIVO_MODIFICA, " +
			"        AA.CCIAA_NUMERO_REGISTRO_IMPRESE, " +
			"        AA.CCIAA_ANNO_ISCRIZIONE, " +
			"        AA.MODIFICA_INTERMEDIARIO, " +
			"        AA.NUMERO_AGEA, " +
			"        AA.INTESTAZIONE_PARTITA_IVA, " +
			"        AA.ID_CESSAZIONE, " +
			"        TC.DESCRIZIONE AS DESC_CESSAZIONE, " +
			"        AA.TELEFONO, " +
			"        AA.FAX, " +
			"        AA.PEC, " +
			"        AA.CODICE_AGRITURISMO, " +
			"        AA.DATA_AGGIORNAMENTO_UMA, " +
			"        AA.FLAG_IAP, " +
			"        AA.DATA_ISCRIZIONE_REA, " +
      "        AA.DATA_CESSAZIONE_REA, " +
      "        AA.DATA_ISCRIZIONE_RI, " +
      "        AA.DATA_CESSAZIONE_RI, " +
      "        AA.DATA_INIZIO_ATECO " +
			" FROM   DB_ANAGRAFICA_AZIENDA AA, " +
			"        DB_TIPO_TIPOLOGIA_AZIENDA TTA, " +
			"        DB_TIPO_FORMA_GIURIDICA TFG, " +
			"        DB_TIPO_ATTIVITA_ATECO TAA, " +
			"        COMUNE C, " +
			"        PROVINCIA P, " +
		//	"        PAPUA_V_UTENTE_LOGIN PVU, " +
			"        DB_TIPO_ATTIVITA_OTE TAO, " +
			"        DB_TIPO_CESSAZIONE TC, " +
			"        DB_AZIENDA_DESTINAZIONE AD, " +
			"        DB_TIPO_UDE TU," +
			"        DB_TIPO_DIMENSIONE_AZIENDA TDA " +  
			" WHERE  AD.ID_AZIENDA = ? " +
			" AND    AA.ID_TIPOLOGIA_AZIENDA = TTA.ID_TIPOLOGIA_AZIENDA(+) " +
			" AND    AA.ID_FORMA_GIURIDICA = TFG.ID_FORMA_GIURIDICA(+) " +
			" AND    AA.ID_ATTIVITA_ATECO = TAA.ID_ATTIVITA_ATECO(+) " +
			" AND    AA.SEDELEG_COMUNE = C.ISTAT_COMUNE(+) " +
			" AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
		//	" AND    AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
			" AND    AA.ID_ATTIVITA_OTE = TAO.ID_ATTIVITA_OTE(+) " +
			" AND    AA.ID_UDE = TU.ID_UDE(+) " +
			" AND    AA.ID_DIMENSIONE_AZIENDA = TDA.ID_DIMENSIONE_AZIENDA(+) " +   
			" AND    AA.ID_CESSAZIONE = TC.ID_CESSAZIONE(+) " +
			" AND    AA.ID_AZIENDA = AD.ID_AZIENDA_DI_DESTINAZIONE ";

			if(onlyActive) {
				query +=   " AND    AA.DATA_FINE_VALIDITA IS NULL ";
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

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListAnagAziendaDestinazioneByIdAzienda method in AnagrafeDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ONLY_ACTIVE] in getListAnagAziendaDestinazioneByIdAzienda method in AnagrafeDAO: "+onlyActive+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ORDER_BY] in getListAnagAziendaDestinazioneByIdAzienda method in AnagrafeDAO: "+orderBy+"\n");

			stmt = conn.prepareStatement(query);

			stmt.setLong(1, idAzienda.longValue());

			SolmrLogger.debug(this, "Executing getListAnagAziendaDestinazioneByIdAzienda: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				AnagAziendaVO anagAziendaVO = new AnagAziendaVO();
				anagAziendaVO.setIdAnagAzienda(new Long(rs.getLong("ID_ANAGRAFICA_AZIENDA")));
				anagAziendaVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
				if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_AZIENDA"))) {
					anagAziendaVO.setTipoTipologiaAzienda(new CodeDescription(Integer.decode(rs.getString("ID_TIPOLOGIA_AZIENDA")), rs.getString("DESC_TIP_AZIENDA")));
				}
				anagAziendaVO.setDataInizioVal(rs.getDate("DATA_INIZIO_VALIDITA"));
				anagAziendaVO.setDataFineVal(rs.getDate("DATA_FINE_VALIDITA"));
				anagAziendaVO.setCUAA(rs.getString("CUAA"));
				anagAziendaVO.setPartitaIVA(rs.getString("PARTITA_IVA"));
				anagAziendaVO.setDenominazione(rs.getString("DENOMINAZIONE"));
				if(Validator.isNotEmpty(rs.getString("ID_FORMA_GIURIDICA"))) {
					anagAziendaVO.setIdFormaGiuridica(Long.decode(rs.getString("ID_FORMA_GIURIDICA")));
					anagAziendaVO.setTipoFormaGiuridica(new CodeDescription(Integer.decode(rs.getString("ID_FORMA_GIURIDICA")), rs.getString("DESC_FORMA_GIUR")));
				}
				if(Validator.isNotEmpty(rs.getString("ID_ATTIVITA_ATECO"))) {
					anagAziendaVO.setIdAteco(rs.getString("ID_ATTIVITA_ATECO"));
					anagAziendaVO.setTipoAttivitaATECO(new CodeDescription(Integer.decode(rs.getString("ID_ATTIVITA_ATECO")), rs.getString("DESC_ATECO")));
				}
				anagAziendaVO.setProvCompetenza(rs.getString("PROVINCIA_COMPETENZA"));
				anagAziendaVO.setCCIAAprovREA(rs.getString("CCIAA_PROVINCIA_REA"));
				if(Validator.isNotEmpty(rs.getString("CCIAA_NUMERO_REA"))) {
					anagAziendaVO.setCCIAAnumeroREA(Long.decode(rs.getString("CCIAA_NUMERO_REA")));
				}
				anagAziendaVO.setMail(rs.getString("MAIL"));
				anagAziendaVO.setTelefono(rs.getString("TELEFONO"));
				anagAziendaVO.setFax(rs.getString("FAX"));
				anagAziendaVO.setPec(rs.getString("PEC"));
				anagAziendaVO.setCodiceAgriturismo(rs.getString("CODICE_AGRITURISMO"));
				anagAziendaVO.setSedelegComune(rs.getString("SEDELEG_COMUNE"));
				anagAziendaVO.setDescComune(rs.getString("DESCOM"));
				anagAziendaVO.setSedelegProv(rs.getString("SIGLA_PROVINCIA"));
				anagAziendaVO.setSedelegIndirizzo(rs.getString("SEDELEG_INDIRIZZO"));
				anagAziendaVO.setSitoWEB(rs.getString("SITOWEB"));
				anagAziendaVO.setSedelegCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
				anagAziendaVO.setSedelegCAP(rs.getString("SEDELEG_CAP"));
				anagAziendaVO.setDataCessazione(rs.getDate("DATA_CESSAZIONE"));
				anagAziendaVO.setCausaleCessazione(rs.getString("CAUSALE_CESSAZIONE"));
				anagAziendaVO.setNote(rs.getString("NOTE"));
				anagAziendaVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
				anagAziendaVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
				anagAziendaVO.setDescrizioneUtenteModifica(rs.getString("DENOMINAZIONE"));
				anagAziendaVO.setDescrizioneEnteUtenteModifica(rs.getString("DESCRIZIONE_ENTE_APPARTENENZA"));
				anagAziendaVO.setIdUde(checkLongNull(rs.getString("ID_UDE")));
				anagAziendaVO.setIdDimensioneAzienda(checkLongNull(rs.getString("ID_DIMENSIONE_AZIENDA")));
				anagAziendaVO.setRls(rs.getBigDecimal("RLS"));
				anagAziendaVO.setUlu(rs.getBigDecimal("ULU"));
				anagAziendaVO.setClasseUde(checkLongNull(rs.getString("CLASSE_UDE")));
				anagAziendaVO.setDescDimensioneAzienda(rs.getString("DES_DIM_AZIENDA"));
				anagAziendaVO.setEsoneroPagamentoGF(rs.getString("ESONERO_PAGAMENTO_GF"));
				anagAziendaVO.setDataAggiornamentoUma(rs.getTimestamp("DATA_AGGIORNAMENTO_UMA"));
				anagAziendaVO.setFlagIap(rs.getString("FLAG_IAP"));
				anagAziendaVO.setDataIscrizioneRea(rs.getTimestamp("DATA_ISCRIZIONE_REA"));
				anagAziendaVO.setDataCessazioneRea(rs.getTimestamp("DATA_CESSAZIONE_REA"));
				anagAziendaVO.setDataIscrizioneRi(rs.getTimestamp("DATA_ISCRIZIONE_RI"));
				anagAziendaVO.setDataCessazioneRi(rs.getTimestamp("DATA_CESSAZIONE_RI"));
				anagAziendaVO.setDataInizioAteco(rs.getTimestamp("DATA_INIZIO_ATECO"));
				
				
				if(Validator.isNotEmpty(rs.getString("ID_ATTIVITA_OTE"))) {
					anagAziendaVO.setIdOte(rs.getString("ID_ATTIVITA_OTE"));
					anagAziendaVO.setTipoAttivitaOTE(new CodeDescription(Integer.decode(rs.getString("ID_ATTIVITA_OTE")), rs.getString("DESC_OTE")));
				}
				anagAziendaVO.setMotivoModifica(rs.getString("MOTIVO_MODIFICA"));
				anagAziendaVO.setCCIAAnumRegImprese(rs.getString("CCIAA_NUMERO_REGISTRO_IMPRESE"));
				anagAziendaVO.setCCIAAannoIscrizione(rs.getString("CCIAA_ANNO_ISCRIZIONE"));
				anagAziendaVO.setModificaIntermediario(rs.getString("MODIFICA_INTERMEDIARIO"));
				anagAziendaVO.setNumeroAgea(rs.getString("NUMERO_AGEA"));
				anagAziendaVO.setIntestazionePartitaIva(rs.getString("INTESTAZIONE_PARTITA_IVA"));
				if(Validator.isNotEmpty(rs.getString("ID_CESSAZIONE"))) {
					anagAziendaVO.setIdCessazione(new Long(rs.getLong("ID_CESSAZIONE")));
					TipoCessazioneVO tipoCessazioneVO = new TipoCessazioneVO();
					tipoCessazioneVO.setIdCessazione(new Long(rs.getLong("ID_CESSAZIONE")));
					tipoCessazioneVO.setDescrizione(rs.getString("DESC_CESSAZIONE"));
					anagAziendaVO.setTipoCessazioneVO(tipoCessazioneVO);
				}
				elencoAziende.add(anagAziendaVO);
			}
			
			rs.close();

			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListAnagAziendaDestinazioneByIdAzienda in getListAnagAziendaVOByCuaa - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListAnagAziendaDestinazioneByIdAzienda in getListAnagAziendaVOByCuaa - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListAnagAziendaDestinazioneByIdAzienda in getListAnagAziendaVOByCuaa - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListAnagAziendaDestinazioneByIdAzienda in getListAnagAziendaVOByCuaa - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListAnagAziendaDestinazioneByIdAzienda method in getListAnagAziendaVOByCuaa\n");
		if(elencoAziende.size() == 0) {
			return (AnagAziendaVO[])elencoAziende.toArray(new AnagAziendaVO[0]);
		}
		else {
			return (AnagAziendaVO[])elencoAziende.toArray(new AnagAziendaVO[elencoAziende.size()]);
		}
	}





	/**
	 * Restituisce l'id dell' azienda di provenienza associato all'idAzienda passato
	 * @param idAzienda Long
	 * @throws DataAccessException
	 * @return Long azienda di provenienza
	 */
	public Long serviceGetIdAziendaProvenienza(Long idAzienda)
	throws DataAccessException
	{

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs=null;
		String query=null;
		Long result=null;
		try
		{
			conn = getDatasource().getConnection();
			query = "SELECT ID_AZIENDA_PROVENIENZA FROM DB_AZIENDA "+
			"WHERE ID_AZIENDA = ? ";

			SolmrLogger.debug(this, "Executing serviceGetIdAziendaProvenienza: "+query);

			SolmrLogger.debug(this, "Executing serviceGetIdAziendaProvenienza idAzienda: "+idAzienda.longValue());

			stmt = conn.prepareStatement(query);

			stmt.setLong(1,idAzienda.longValue());

			rs=stmt.executeQuery();
			if (rs.next())
			{
				try
				{
					result = new Long(rs.getString("ID_AZIENDA_PROVENIENZA"));
				}
				catch(Exception e){}
			}
			SolmrLogger.debug(this, "Executed serviceGetIdAziendaProvenienza");

			rs.close();

		}
		catch (SQLException exc)
		{
			SolmrLogger.fatal(this, "SQLException in serviceGetIdAziendaProvenienza query= "+query);
			SolmrLogger.fatal(this, "SQLException in serviceGetIdAziendaProvenienza exception="+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex)
		{
			SolmrLogger.fatal(this, "Generic Exception in serviceGetIdAziendaProvenienza"+ex.getMessage());
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
				SolmrLogger.fatal(this, "SQLException while closing Statement and Connection in serviceGetIdAziendaProvenienza: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex)
			{
				SolmrLogger.fatal(this, "Generic Exception while closing Statement and Connection in serviceGetIdAziendaProvenienza: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return result;
	}

	/**
	 * Restituisce un Vector di AnagAziendaVO.
	 * Il parametro intermediario è obbligatorio, cuaa invece facoltativo
	 *
	 * @param intermediario
	 * @param cuaa
	 * @return
	 * @throws DataAccessException
	 * @throws NotFoundException
	 * @throws SolmrException
	 */
	public Vector<AnagAziendaVO> getAziendaByIntermediarioAndCuaa(Long intermediario,String cuaa)
	throws DataAccessException, NotFoundException, SolmrException
	{
		Vector<AnagAziendaVO> result = new Vector<AnagAziendaVO>();
		Connection conn = null;
		PreparedStatement stmt = null;
		try
		{
			conn = getDatasource().getConnection();

			String query = "  SELECT AA.CUAA, AA.DENOMINAZIONE, AA.PARTITA_IVA, "+
			"AA.SEDELEG_INDIRIZZO, AA.SEDELEG_CAP, AA.SEDELEG_COMUNE, "+
			"AA.SEDELEG_CITTA_ESTERO, AA.ID_AZIENDA, " +
			"AA.ID_ANAGRAFICA_AZIENDA, " +
			"CO.DESCOM, CO.FLAG_ESTERO, " +
			"PR.SIGLA_PROVINCIA, PR.ISTAT_PROVINCIA "+
			"FROM DB_ANAGRAFICA_AZIENDA AA, "+
			"DB_DELEGA DE, COMUNE CO, PROVINCIA PR "+
			"WHERE DE.ID_INTERMEDIARIO = ? "+
			"AND DE.ID_AZIENDA = AA.ID_AZIENDA "+
			"AND AA.CUAA IS NOT NULL "+
			"AND AA.DATA_FINE_VALIDITA IS NULL "+
			"AND AA.DATA_CESSAZIONE IS NULL "+
			"AND DE.DATA_FINE IS NULL "+
			"AND AA.SEDELEG_COMUNE = CO.ISTAT_COMUNE(+) "+
			"AND CO.ISTAT_PROVINCIA = PR.ISTAT_PROVINCIA(+) ";

			if(Validator.isNotEmpty(cuaa))
			{
				query += "AND AA.CUAA = UPPER(?) ";
			}

			query += "ORDER BY AA.DENOMINAZIONE,AA.CUAA ";

			stmt = conn.prepareStatement(query);

			stmt.setLong(1, intermediario.longValue());

			if(Validator.isNotEmpty(cuaa))
			{
				stmt.setString(2, cuaa);
			}

			SolmrLogger.debug(this, "Executing query: "+query);

			ResultSet rs = stmt.executeQuery();

			while(rs.next())
			{
				AnagAziendaVO aziendaTempt = new AnagAziendaVO();
				aziendaTempt.setCUAA(rs.getString("CUAA"));
				aziendaTempt.setDenominazione(rs.getString("DENOMINAZIONE"));
				aziendaTempt.setPartitaIVA(rs.getString("PARTITA_IVA"));
				aziendaTempt.setSedelegComune(rs.getString("SEDELEG_COMUNE"));
				aziendaTempt.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
				aziendaTempt.setIdAnagAzienda(new Long(rs.getLong("ID_ANAGRAFICA_AZIENDA")));
				String sedelegDescComune = rs.getString("DESCOM");

				String flagEstero = rs.getString("FLAG_ESTERO");

				if(flagEstero==null || flagEstero.equals("")){
					aziendaTempt.setSedelegComune("");
					aziendaTempt.setSedelegEstero("");
					aziendaTempt.setStatoEstero("");
					aziendaTempt.setSedelegCittaEstero("");
					aziendaTempt.setSedelegIstatProv("");
					aziendaTempt.setSedelegProv("");
					aziendaTempt.setDescComune("");
					aziendaTempt.setSedelegCAP("");
				}
				else if(flagEstero.equals(SolmrConstants.FLAG_S))
				{
					aziendaTempt.setSedelegEstero(sedelegDescComune);
					aziendaTempt.setStatoEstero(sedelegDescComune);
					aziendaTempt.setSedelegCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
					aziendaTempt.setSedelegIstatProv("");
					aziendaTempt.setSedelegProv("");
					aziendaTempt.setDescComune("");
					aziendaTempt.setSedelegCAP("");
				}
				else
				{
					aziendaTempt.setSedelegEstero("");
					aziendaTempt.setSedelegCittaEstero("");
					aziendaTempt.setSedelegIstatProv(rs.getString("ISTAT_PROVINCIA"));
					aziendaTempt.setSedelegProv(rs.getString("SIGLA_PROVINCIA"));
					aziendaTempt.setDescComune(sedelegDescComune);
					aziendaTempt.setSedelegCAP(rs.getString("SEDELEG_CAP"));
				}

				aziendaTempt.setSedelegIndirizzo(rs.getString("SEDELEG_INDIRIZZO"));

				result.add(aziendaTempt);
			}

			rs.close();
			stmt.close();

			/*if (result == null)
			{
				throw new NotFoundException();
			}*/
		} catch (SQLException exc) {
			SolmrLogger.fatal(this, "getAziendaByIntermediarioAndCuaa - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} /*catch (NotFoundException nfexc) {
			SolmrLogger.fatal(this, "getAziendaByIntermediarioAndCuaa - NotFoundException: "+nfexc.getMessage());
			throw nfexc;
		}*/ catch (Exception ex) {
			SolmrLogger.fatal(this, "getAziendaByIntermediarioAndCuaa - Generic Exception: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		}
		finally
		{
			try
			{
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, "getAziendaByIntermediarioAndCuaa - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "getAziendaByIntermediarioAndCuaa - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return result;
	}

	/**
	 * Restituisce un Vector di AnagAziendaVO.
	 * Il parametro è un Vector di Long IdAzienda
	 *
	 * @param intermediario
	 * @param cuaa
	 * @return
	 * @throws DataAccessException
	 * @throws NotFoundException
	 * @throws SolmrException
	 */
	public Vector<AnagAziendaVO> getAziendeByListOfId(Vector<Long> vIdAzienda)
	throws DataAccessException, NotFoundException, SolmrException
	{
		Vector<AnagAziendaVO> result = new Vector<AnagAziendaVO>();
		Connection conn = null;
		PreparedStatement stmt = null;
		try
		{
			conn = getDatasource().getConnection();

			String query = "  SELECT AA.CUAA, AA.DENOMINAZIONE, AA.PARTITA_IVA, "+
			"AA.SEDELEG_INDIRIZZO, AA.SEDELEG_CAP, AA.SEDELEG_COMUNE, "+
			"AA.SEDELEG_CITTA_ESTERO, AA.ID_AZIENDA, " +
			"AA.ID_ANAGRAFICA_AZIENDA, DE.ID_PROCEDIMENTO, " +
			"DE.ID_DELEGA, DE.DATA_INIZIO_MANDATO, " +
			"CO.DESCOM, CO.FLAG_ESTERO, " +
			"PR.SIGLA_PROVINCIA, PR.ISTAT_PROVINCIA "+
			"FROM DB_ANAGRAFICA_AZIENDA AA, "+
			"DB_DELEGA DE, COMUNE CO, PROVINCIA PR "+
			"WHERE AA.DATA_FINE_VALIDITA IS NULL "+
			"AND AA.DATA_CESSAZIONE IS NULL "+
			"AND DE.ID_AZIENDA = AA.ID_AZIENDA "+
			"AND DE.DATA_FINE IS NULL "+
			"AND AA.SEDELEG_COMUNE = CO.ISTAT_COMUNE(+) "+
			"AND CO.ISTAT_PROVINCIA = PR.ISTAT_PROVINCIA(+) "+
			"AND AA.ID_AZIENDA IN ( ";

			for(int i=0;i<vIdAzienda.size();i++)
			{
				if(i == (vIdAzienda.size() -1))
				{
					query += "?) ";
				}
				else
				{
					query += "?, ";
				}
			}


			query += "ORDER BY AA.DENOMINAZIONE,AA.CUAA ";

			stmt = conn.prepareStatement(query);

			int j=1;
			for(int i=0;i<vIdAzienda.size();i++)
			{
				Long idAzienda = (Long)vIdAzienda.get(i);
				stmt.setLong(j, idAzienda.longValue());
				j++;
			}

			SolmrLogger.debug(this, "Executing query: "+query);

			ResultSet rs = stmt.executeQuery();

			while(rs.next())
			{
				AnagAziendaVO aziendaTempt = new AnagAziendaVO();
				aziendaTempt.setCUAA(rs.getString("CUAA"));
				aziendaTempt.setDenominazione(rs.getString("DENOMINAZIONE"));
				aziendaTempt.setPartitaIVA(rs.getString("PARTITA_IVA"));
				aziendaTempt.setSedelegComune(rs.getString("SEDELEG_COMUNE"));
				aziendaTempt.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
				aziendaTempt.setIdAnagAzienda(new Long(rs.getLong("ID_ANAGRAFICA_AZIENDA")));
				String sedelegDescComune = rs.getString("DESCOM");

				String flagEstero = rs.getString("FLAG_ESTERO");

				if(flagEstero==null || flagEstero.equals("")){
					aziendaTempt.setSedelegComune("");
					aziendaTempt.setSedelegEstero("");
					aziendaTempt.setStatoEstero("");
					aziendaTempt.setSedelegCittaEstero("");
					aziendaTempt.setSedelegIstatProv("");
					aziendaTempt.setSedelegProv("");
					aziendaTempt.setDescComune("");
					aziendaTempt.setSedelegCAP("");
				}
				else if(flagEstero.equals(SolmrConstants.FLAG_S))
				{
					aziendaTempt.setSedelegEstero(sedelegDescComune);
					aziendaTempt.setStatoEstero(sedelegDescComune);
					aziendaTempt.setSedelegCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
					aziendaTempt.setSedelegIstatProv("");
					aziendaTempt.setSedelegProv("");
					aziendaTempt.setDescComune("");
					aziendaTempt.setSedelegCAP("");
				}
				else
				{
					aziendaTempt.setSedelegEstero("");
					aziendaTempt.setSedelegCittaEstero("");
					aziendaTempt.setSedelegIstatProv(rs.getString("ISTAT_PROVINCIA"));
					aziendaTempt.setSedelegProv(rs.getString("SIGLA_PROVINCIA"));
					aziendaTempt.setDescComune(sedelegDescComune);
					aziendaTempt.setSedelegCAP(rs.getString("SEDELEG_CAP"));
				}

				aziendaTempt.setSedelegIndirizzo(rs.getString("SEDELEG_INDIRIZZO"));

				DelegaVO delVO = new DelegaVO();
				delVO.setIdDelega(new Long(rs.getLong("ID_DELEGA")));
				delVO.setDataInizioMandato(rs.getDate("DATA_INIZIO_MANDATO"));
				aziendaTempt.setDelegaVO(delVO);

				result.add(aziendaTempt);
			}

			rs.close();
			stmt.close();

			/*if (result == null)
			{
				throw new NotFoundException();
			}*/
		} catch (SQLException exc) {
			SolmrLogger.fatal(this, "getAziendeByListOfId - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} /*catch (NotFoundException nfexc) {
			SolmrLogger.fatal(this, "getAziendeByListOfId - NotFoundException: "+nfexc.getMessage());
			throw nfexc;
		}*/ catch (Exception ex) {
			SolmrLogger.fatal(this, "getAziendeByListOfId - Generic Exception: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		}
		finally
		{
			try
			{
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, "getAziendeByListOfId - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "getAziendeByListOfId - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return result;
	}

	/**
	 * Restituisce un Vector di AnagAziendaVO.
	 * I parametri sono IdAzienda e flagStorico, se a true devo fare 
	 * vedere anche le aziende storicizzate
	 *
	 * @param idAzienda
	 * @param flagStorico
	 * 
	 * @return
	 * @throws DataAccessException
	 * @throws NotFoundException
	 * @throws SolmrException
	 */
	public Vector<AziendaCollegataVO> getAziendeCollegateByIdAzienda(Long idAzienda, boolean flagStorico)
	throws DataAccessException, SolmrException
	{
		Vector<AziendaCollegataVO> result = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		try
		{
			conn = getDatasource().getConnection();

			String query = 
			  "SELECT  "+
			  "        NVL2(DAC.ID_AZIENDA_ASSOCIATA,AA.CUAA,DAS.CUAA) AS CUAA, " +
			  "        NVL2(DAC.ID_AZIENDA_ASSOCIATA,AA.DENOMINAZIONE,DAS.DENOMINAZIONE) AS DENOMINAZIONE, "+ 
			  "        NVL2(DAC.ID_AZIENDA_ASSOCIATA,AA.PARTITA_IVA,DAS.PARTITA_IVA) AS PARTITA_IVA, " + 
        "        NVL2(DAC.ID_AZIENDA_ASSOCIATA,AA.SEDELEG_INDIRIZZO,DAS.INDIRIZZO) AS INDIRIZZO," + 
        "        NVL2(DAC.ID_AZIENDA_ASSOCIATA,AA.SEDELEG_CAP,DAS.CAP) AS CAP, " + 
        "        NVL2(DAC.ID_AZIENDA_ASSOCIATA,AA.SEDELEG_COMUNE,DAS.COMUNE) AS COMUNE, " +
        "        AA.SEDELEG_CITTA_ESTERO, AA.ID_AZIENDA, AA.ID_ANAGRAFICA_AZIENDA, " +
        "        AA.ID_UTENTE_AGGIORNAMENTO, " +
        "        DAC.DATA_INGRESSO, DAC.DATA_USCITA, DAC.DATA_INIZIO_VALIDITA, " +
        "        DAC.DATA_FINE_VALIDITA, DAC.DATA_AGGIORNAMENTO, DAC.ID_AZIENDA_COLLEGATA, " +
        "        DAC.ID_SOGGETTO_ASSOCIATO, DAC.ID_AZIENDA_ASSOCIATA, " +
        "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
        "          || ' ' " + 
        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
        "         WHERE DAC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
        "        AS UI_DENOMINAZIONE, " +
        "        (SELECT PVU.DENOMINAZIONE " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " +
        "         WHERE DAC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
        "        AS UI_ENTEAPPARTENENZA, " +
        "        CO.DESCOM, CO.FLAG_ESTERO, " +
        "        PR.SIGLA_PROVINCIA, PR.ISTAT_PROVINCIA "+
        "FROM    DB_ANAGRAFICA_AZIENDA AA," +
        "        DB_AZIENDA_COLLEGATA DAC, "+
        "        DB_SOGGETTO_ASSOCIATO DAS," +
//        "        PAPUA_V_UTENTE_LOGIN PVU, " +
        "        COMUNE CO, PROVINCIA PR "+
        "WHERE   DAC.ID_AZIENDA = ? ";
    
    if(!flagStorico)
    {
      query += "AND NVL(TRUNC(DAC.DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > SYSDATE "+
      "AND NVL(TRUNC(DAC.DATA_USCITA), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > SYSDATE ";
    }
      
    query += 
      "AND DAC.ID_AZIENDA_ASSOCIATA = AA.ID_AZIENDA(+) "+
      "AND DAC.ID_SOGGETTO_ASSOCIATO = DAS.ID_SOGGETTO_ASSOCIATO(+) "+
      "AND AA.DATA_FINE_VALIDITA IS NULL "+
      "AND AA.DATA_CESSAZIONE IS NULL "+
    //  "AND DAC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
      "AND NVL2(DAC.ID_AZIENDA_ASSOCIATA,AA.SEDELEG_COMUNE,DAS.COMUNE) = CO.ISTAT_COMUNE "+
      "AND CO.ISTAT_PROVINCIA = PR.ISTAT_PROVINCIA ";




			query += 
			  "ORDER BY " +
			  "NVL2(DAC.ID_AZIENDA_ASSOCIATA,AA.DENOMINAZIONE,DAS.DENOMINAZIONE), " +
			  "NVL2(DAC.ID_AZIENDA_ASSOCIATA,AA.CUAA,DAS.CUAA) ";

			stmt = conn.prepareStatement(query);
			
			int idx = 0;
      stmt.setLong(++idx, idAzienda.longValue());
      
			SolmrLogger.debug(this, "Executing query: "+query);

      ResultSet rs = stmt.executeQuery();
			
			while(rs.next())
      {
        if(result == null)
        {
          result = new Vector<AziendaCollegataVO>();
        }
        AziendaCollegataVO aziendaTempt = new AziendaCollegataVO();
        Long idSoggettoAssociato = checkLongNull(rs.getString("ID_SOGGETTO_ASSOCIATO"));
        if(idSoggettoAssociato != null)
        {
          SoggettoAssociatoVO soggVO = new SoggettoAssociatoVO();
          soggVO.setCuaa(rs.getString("CUAA"));
          soggVO.setDenominazione(rs.getString("DENOMINAZIONE"));
          soggVO.setPartitaIva(rs.getString("PARTITA_IVA"));
          soggVO.setIndirizzo(rs.getString("INDIRIZZO"));
          soggVO.setCap(rs.getString("CAP"));
          soggVO.setComune(rs.getString("COMUNE"));
          soggVO.setSglProv(rs.getString("SIGLA_PROVINCIA"));
          soggVO.setDenominazioneComune(rs.getString("DESCOM"));
          
          aziendaTempt.setIdSoggettoAssociato(idSoggettoAssociato);
          aziendaTempt.setSoggettoAssociato(soggVO);
        }
        else
        {
          aziendaTempt.setCuaa(rs.getString("CUAA"));
          aziendaTempt.setDenominazione(rs.getString("DENOMINAZIONE"));
          aziendaTempt.setPartitaIva(rs.getString("PARTITA_IVA"));
          aziendaTempt.setIndirizzo(rs.getString("INDIRIZZO"));
          aziendaTempt.setIstatComune(rs.getString("COMUNE"));
          
          String flagEstero = rs.getString("FLAG_ESTERO");
          String descComune = rs.getString("DESCOM");

          if(flagEstero.equals(SolmrConstants.FLAG_S))
          {
            aziendaTempt.setSedeEstero(descComune);
            aziendaTempt.setSedeCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
            aziendaTempt.setSglProv("");
            aziendaTempt.setDenominazioneComune("");
            aziendaTempt.setCap("");
          }
          else
          {
            aziendaTempt.setSedeEstero("");
            aziendaTempt.setSedeCittaEstero("");
            aziendaTempt.setSglProv(rs.getString("SIGLA_PROVINCIA"));
            aziendaTempt.setDenominazioneComune(descComune);
            aziendaTempt.setCap(rs.getString("CAP"));
          }
                  
          aziendaTempt.setIdAziendaAssociata(new Long(rs.getLong("ID_AZIENDA_ASSOCIATA")));
          
        }
        aziendaTempt.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        aziendaTempt.setDataIngresso(rs.getDate("DATA_INGRESSO"));
        aziendaTempt.setDataUscita(rs.getDate("DATA_USCITA"));
        aziendaTempt.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        aziendaTempt.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        aziendaTempt.setIdAziendaCollegata(new Long(rs.getLong("ID_AZIENDA_COLLEGATA")));
        aziendaTempt.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
        aziendaTempt.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        aziendaTempt.setDescrizioneUtenteModifica(rs.getString("UI_DENOMINAZIONE"));
        aziendaTempt.setDescrizioneEnteUtenteModifica(rs.getString("UI_ENTEAPPARTENENZA"));

        result.add(aziendaTempt);
      }

      rs.close();
      stmt.close();

    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "getAziendeCollegateByIdAzienda - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "getAziendeCollegateByIdAzienda - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "getAziendeCollegateByIdAzienda - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "getAziendeCollegateByIdAzienda - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
	
	
	
	
	
	/**
   * Restituisce un Vector di AnagAziendaVO.
   * I parametri sono IdAzienda e flagStorico, se a true devo fare 
   * vedere anche le aziende storicizzate
   *
   * @param idAzienda
   * @param flagStorico
   * 
   * @return
   * @throws DataAccessException
   * @throws NotFoundException
   * @throws SolmrException
   */
  public Vector<AnagAziendaVO> getEntiAppartenenzaByIdAzienda(Long idAzienda, boolean flagStorico)
  throws DataAccessException, SolmrException
  {
    Vector<AnagAziendaVO> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "  SELECT AA.CUAA, AA.DENOMINAZIONE, AA.PARTITA_IVA, "+
      "AA.SEDELEG_INDIRIZZO, AA.SEDELEG_CAP, AA.SEDELEG_COMUNE, "+
      "AA.SEDELEG_CITTA_ESTERO, AA.ID_AZIENDA, " +
      "AA.ID_ANAGRAFICA_AZIENDA, "+
      "DAC.DATA_INGRESSO, " +
      "DAC.DATA_USCITA, " +
      "DAC.DATA_INIZIO_VALIDITA, " +
      "DAC.DATA_FINE_VALIDITA, " +
      "DAC.DATA_AGGIORNAMENTO, " +
      "DAC.ID_AZIENDA_COLLEGATA, " +
      "AA.ID_UTENTE_AGGIORNAMENTO, " +
      "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
      "          || ' ' " + 
      "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
      "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
      "         WHERE DAC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
      " AS UI_DENOMINAZIONE, " +
      " (SELECT PVU.DENOMINAZIONE " +
      " FROM PAPUA_V_UTENTE_LOGIN PVU " +
      " WHERE DAC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
      " AS UI_ENTEAPPARTENENZA, " +
      "CO.DESCOM, CO.FLAG_ESTERO, " +
      "PR.SIGLA_PROVINCIA, PR.ISTAT_PROVINCIA "+
      "FROM DB_ANAGRAFICA_AZIENDA AA," +
      "DB_AZIENDA_COLLEGATA DAC, "+
    //  "PAPUA_V_UTENTE_LOGIN PVU, " +
      "COMUNE CO, PROVINCIA PR "+
      "WHERE DAC.ID_AZIENDA_ASSOCIATA = ? ";
    
    if(!flagStorico)
    {
      query += "AND NVL(TRUNC(DAC.DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > SYSDATE "+
      "AND NVL(TRUNC(DAC.DATA_USCITA), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > SYSDATE ";
    }
      
    query += "AND DAC.ID_AZIENDA = AA.ID_AZIENDA "+
      "AND AA.DATA_FINE_VALIDITA IS NULL "+
      "AND AA.DATA_CESSAZIONE IS NULL "+
//      "AND DAC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
      "AND AA.SEDELEG_COMUNE = CO.ISTAT_COMUNE(+) "+
      "AND CO.ISTAT_PROVINCIA = PR.ISTAT_PROVINCIA(+) ";




      query += "ORDER BY AA.DENOMINAZIONE,AA.CUAA ";

      stmt = conn.prepareStatement(query);
      
      int idx = 0;
      stmt.setLong(++idx, idAzienda.longValue());
      
      SolmrLogger.debug(this, "Executing query: "+query);

      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(result == null)
        {
          result = new Vector<AnagAziendaVO>();
        }
        AnagAziendaVO aziendaTempt = new AnagAziendaVO();
        aziendaTempt.setCUAA(rs.getString("CUAA"));
        aziendaTempt.setDenominazione(rs.getString("DENOMINAZIONE"));
        aziendaTempt.setPartitaIVA(rs.getString("PARTITA_IVA"));
        aziendaTempt.setSedelegComune(rs.getString("SEDELEG_COMUNE"));
        aziendaTempt.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        aziendaTempt.setIdAnagAzienda(new Long(rs.getLong("ID_ANAGRAFICA_AZIENDA")));
        String sedelegDescComune = rs.getString("DESCOM");
        String flagEstero = rs.getString("FLAG_ESTERO");

        if(flagEstero==null || flagEstero.equals("")){
          aziendaTempt.setSedelegComune("");
          aziendaTempt.setSedelegEstero("");
          aziendaTempt.setStatoEstero("");
          aziendaTempt.setSedelegCittaEstero("");
          aziendaTempt.setSedelegIstatProv("");
          aziendaTempt.setSedelegProv("");
          aziendaTempt.setDescComune("");
          aziendaTempt.setSedelegCAP("");
        }
        else if(flagEstero.equals(SolmrConstants.FLAG_S))
        {
          aziendaTempt.setSedelegEstero(sedelegDescComune);
          aziendaTempt.setStatoEstero(sedelegDescComune);
          aziendaTempt.setSedelegCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
          aziendaTempt.setSedelegIstatProv("");
          aziendaTempt.setSedelegProv("");
          aziendaTempt.setDescComune("");
          aziendaTempt.setSedelegCAP("");
        }
        else
        {
          aziendaTempt.setSedelegEstero("");
          aziendaTempt.setSedelegCittaEstero("");
          aziendaTempt.setSedelegIstatProv(rs.getString("ISTAT_PROVINCIA"));
          aziendaTempt.setSedelegProv(rs.getString("SIGLA_PROVINCIA"));
          aziendaTempt.setDescComune(sedelegDescComune);
          aziendaTempt.setSedelegCAP(rs.getString("SEDELEG_CAP"));
        }

        aziendaTempt.setSedelegIndirizzo(rs.getString("SEDELEG_INDIRIZZO"));

        aziendaTempt.setDataIngresso(rs.getDate("DATA_INGRESSO"));
        aziendaTempt.setDataUscita(rs.getDate("DATA_USCITA"));
        aziendaTempt.setDataInizioVal(rs.getDate("DATA_INIZIO_VALIDITA"));
        aziendaTempt.setDataFineVal(rs.getDate("DATA_FINE_VALIDITA"));
        aziendaTempt.setIdAziendaCollegata(new Long(rs.getLong("ID_AZIENDA_COLLEGATA")));


        aziendaTempt.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
        aziendaTempt.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        aziendaTempt.setDescrizioneUtenteModifica(rs.getString("UI_DENOMINAZIONE"));
        aziendaTempt.setDescrizioneEnteUtenteModifica(rs.getString("UI_ENTEAPPARTENENZA"));

        result.add(aziendaTempt);
      }

      rs.close();
      stmt.close();

    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "getEntiAppartenenzaByIdAzienda - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "getEntiAppartenenzaByIdAzienda - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "getEntiAppartenenzaByIdAzienda - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "getEntiAppartenenzaByIdAzienda - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  
  
  /**
   * Cerca un'azienda con la chiave primaria ID_ANAGRAFICA_AZIENDA
   * 
   * @param anagAziendaPK
   * @return
   * @throws DataAccessException
   */
  public AnagAziendaVO findAziendaByIdAnagAzienda(Long anagAziendaPK)
    throws DataAccessException {


    AnagAziendaVO result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try 
    {
      conn = getDatasource().getConnection();

      String find =   
      "SELECT AA.ID_ANAGRAFICA_AZIENDA, " +
      "       AA.ID_AZIENDA, "+
      "       AA.DATA_INIZIO_VALIDITA, " +
      "       AA.DATA_FINE_VALIDITA, "+
      "       AA.CUAA ALIAS_CUAA, " +
      "       AA.PARTITA_IVA, " +
      "       AA.DENOMINAZIONE ANAG_DENOMINAZIONE,  "+
      "       AA.ID_FORMA_GIURIDICA ALIAS_FORMA_GIURIDICA, "+
      "       AA.ID_TIPOLOGIA_AZIENDA ALIAS_TIPOLOGIA_AZIENDA, "+
      "       AA.ID_ATTIVITA_ATECO ALIAS_ATTIVITA_ATECO, "+
      "       AA.ID_ATTIVITA_OTE ALIAS_ATTIVITA_OTE, "+
      "       AA.ID_UDE, " +
      "       AA.ID_DIMENSIONE_AZIENDA, " +
      "       AA.RLS, " +
      "       AA.ULU AS ULU, " +
      "       TU.CLASSE_UDE, " +
      "       TDA.DESCRIZIONE AS DES_DIM_AZIENDA, " +
      "       AA.ESONERO_PAGAMENTO_GF, " +
      "       TFG.DESCRIZIONE ALIAS_DESC_TFG, "+
      "       TTA.DESCRIZIONE ALIAS_DESC_TTA, "+
      "       TTA.FLAG_FORMA_ASSOCIATA, "+
      "       TAT.DESCRIZIONE ALIAS_DESC_TAT, "+
      "       TOT.DESCRIZIONE ALIAS_DESC_TOT, "+
      "       TAT.CODICE ALIAS_CODICE_TAT, " +
      "       TOT.CODICE ALIAS_CODICE_TOT, "+
      "       AA.PROVINCIA_COMPETENZA, " +
      "       P2.SIGLA_PROVINCIA ALIAS_PROV_COMPETENZA, "+
      "       AA.CCIAA_PROVINCIA_REA, " +
      "       AA.CCIAA_NUMERO_REA, "+
      "       AA.CCIAA_NUMERO_REGISTRO_IMPRESE, " +
      "       AA.CCIAA_ANNO_ISCRIZIONE, "+
      "       P1.ISTAT_PROVINCIA ALIAS_ISTAT_PROV, "+
      "       P1.SIGLA_PROVINCIA ALIAS_PROV, " +
      "       AA.SEDELEG_COMUNE, " +
      "       COMUNE.FLAG_ESTERO ALIAS_SEDELE_ESTERO, "+
      "       COMUNE.DESCOM ALIAS_DESCOM, " +
      "       AA.SEDELEG_INDIRIZZO, " +
      "       AA.SEDELEG_CAP, "+
      "       AA.SEDELEG_CITTA_ESTERO, "+
      "       AA.MAIL ALIAS_MAIL, " +
      "       AA.SITOWEB, " +
      "       AA.NOTE, " +
      "       AA.DATA_CESSAZIONE, "+
      "       AA.CAUSALE_CESSAZIONE, " +
      "       AA.DATA_AGGIORNAMENTO, "+
      "       AA.ID_UTENTE_AGGIORNAMENTO, " +
      "       AA.MOTIVO_MODIFICA,  "+
      "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
      "          || ' ' " + 
      "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
      "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
      "         WHERE AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
      "       AS UT_DENOMINAZIONE, " +
      "       (SELECT PVU.DENOMINAZIONE " +
      "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
      "        WHERE AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
      "       AS UT_ENTE_APPART, "+
      "       P2.DESCRIZIONE ALIAS_DESC_PROV_COMPETENZA, " +
      "       A.FASCICOLO_DEMATERIALIZZATO, "+
      "       A.FLAG_AZIENDA_PROVVISORIA, "+
      "       A.ID_AZIENDA_PROVENIENZA, "+
      "       AA.NUMERO_AGEA, " +
      "       AA.INTESTAZIONE_PARTITA_IVA, "+
      "       AA.ID_CESSAZIONE, " +
      "       TC.DESCRIZIONE AS DESC_CESSAZIONE, " +
      "       AA.TELEFONO, " +
      "       AA.FAX, " +
      "       AA.PEC, " +
      "       AA.CODICE_AGRITURISMO, " +
      "       AA.DATA_AGGIORNAMENTO_UMA, " +
      "       AA.FLAG_IAP, " +
      "       AA.DATA_ISCRIZIONE_REA, " +
      "       AA.DATA_CESSAZIONE_REA, " +
      "       AA.DATA_ISCRIZIONE_RI, " +
      "       AA.DATA_CESSAZIONE_RI, " +
      "       AA.DATA_INIZIO_ATECO " +
      "FROM   DB_ANAGRAFICA_AZIENDA AA, "+
      "       DB_TIPO_FORMA_GIURIDICA TFG, "+
      "       DB_TIPO_TIPOLOGIA_AZIENDA TTA, "+
      "       DB_TIPO_ATTIVITA_ATECO TAT,   "+
      "       DB_TIPO_ATTIVITA_OTE TOT,   "+
      "       DB_TIPO_UDE TU, " +
      "       DB_TIPO_DIMENSIONE_AZIENDA TDA, " +  
      "       DB_AZIENDA A,   "+
      "       COMUNE, " +
      "       PROVINCIA P1, " +
      "       PROVINCIA P2, " +
//      "       PAPUA_V_UTENTE_LOGIN PVU, "+
      "       DB_TIPO_CESSAZIONE TC " +
      "WHERE  AA.SEDELEG_COMUNE = COMUNE.ISTAT_COMUNE(+) "+
      "AND    COMUNE.ISTAT_PROVINCIA = P1.ISTAT_PROVINCIA(+) "+
      "AND    AA.PROVINCIA_COMPETENZA = P2.ISTAT_PROVINCIA(+) "+
      "AND    AA.ID_FORMA_GIURIDICA = TFG.ID_FORMA_GIURIDICA(+) "+
      "AND    AA.ID_TIPOLOGIA_AZIENDA = TTA.ID_TIPOLOGIA_AZIENDA(+) "+
      "AND    AA.ID_ATTIVITA_ATECO = TAT.ID_ATTIVITA_ATECO(+) "+
      "AND    AA.ID_ATTIVITA_OTE = TOT.ID_ATTIVITA_OTE(+) "+
      "AND    AA.ID_UDE = TU.ID_UDE(+) " +
      "AND    AA.ID_DIMENSIONE_AZIENDA = TDA.ID_DIMENSIONE_AZIENDA(+) " +   
      "AND    A.ID_AZIENDA = AA.ID_AZIENDA "+
//      "AND    AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
      "AND    AA.ID_CESSAZIONE = TC.ID_CESSAZIONE(+) " +
      "AND    AA.ID_ANAGRAFICA_AZIENDA = ? ";
      

      SolmrLogger.debug(this, "Executing query: "+find);

      stmt = conn.prepareStatement(find);


      stmt.setLong(1, anagAziendaPK.longValue());

      ResultSet rs = stmt.executeQuery();


      if (rs.next()) 
      {
        result = new AnagAziendaVO();
        result.setIntestazionePartitaIva(rs.getString("INTESTAZIONE_PARTITA_IVA"));
        result.setIdAnagAzienda(new Long(rs.getLong("ID_ANAGRAFICA_AZIENDA")));
        result.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        result.setDataInizioVal(rs.getDate("DATA_INIZIO_VALIDITA"));
        result.setDataFineVal(rs.getDate("DATA_FINE_VALIDITA"));
        result.setCUAA(rs.getString("ALIAS_CUAA"));
        result.setPartitaIVA(rs.getString("PARTITA_IVA"));
        result.setDenominazione(rs.getString("ANAG_DENOMINAZIONE"));
        result.setIdUde(checkLongNull(rs.getString("ID_UDE")));
        result.setIdDimensioneAzienda(checkLongNull(rs.getString("ID_DIMENSIONE_AZIENDA")));
        result.setRls(rs.getBigDecimal("RLS"));
        result.setUlu(rs.getBigDecimal("ULU"));
        result.setClasseUde(checkLongNull(rs.getString("CLASSE_UDE")));
        result.setDescDimensioneAzienda(rs.getString("DES_DIM_AZIENDA"));
        result.setEsoneroPagamentoGF(rs.getString("ESONERO_PAGAMENTO_GF"));
        result.setDataAggiornamentoUma(rs.getTimestamp("DATA_AGGIORNAMENTO_UMA"));
        result.setFlagIap(rs.getString("FLAG_IAP"));
        result.setDataIscrizioneRea(rs.getTimestamp("DATA_ISCRIZIONE_REA"));
        result.setDataCessazioneRea(rs.getTimestamp("DATA_CESSAZIONE_REA"));
        result.setDataIscrizioneRi(rs.getTimestamp("DATA_ISCRIZIONE_RI"));
        result.setDataCessazioneRi(rs.getTimestamp("DATA_CESSAZIONE_RI"));
        result.setDataInizioAteco(rs.getTimestamp("DATA_INIZIO_ATECO"));

        String codFormaGiur = rs.getString("ALIAS_FORMA_GIURIDICA");
        if(codFormaGiur!=null && !codFormaGiur.equals("")){
          result.setTipoFormaGiuridica(new CodeDescription(
              new Integer(codFormaGiur),
              rs.getString("ALIAS_DESC_TFG")));
        }
        else{
          result.setTipoFormaGiuridica(new CodeDescription());
        }
        String codTipolAzienda = rs.getString("ALIAS_TIPOLOGIA_AZIENDA");
        if(codTipolAzienda!=null)
          result.setTipoTipologiaAzienda(new CodeDescription(
              new Integer(codTipolAzienda),
              rs.getString("ALIAS_DESC_TTA")));

        else
          result.setTipoTipologiaAzienda(new CodeDescription());

        result.setTipiAzienda(codTipolAzienda);
        
      //Aggiunta per gestione aziende collegate
        result.setFlagFormaAssociata(rs.getString("FLAG_FORMA_ASSOCIATA"));

        String codATECO = rs.getString("ALIAS_ATTIVITA_ATECO");
        if(codATECO!=null){
          CodeDescription codeATECO = new CodeDescription(new Integer(codATECO),
              rs.getString("ALIAS_DESC_TAT"));
          codeATECO.setSecondaryCode(rs.getString("ALIAS_CODICE_TAT"));
          result.setTipoAttivitaATECO(codeATECO);
        }
        else
          result.setTipoAttivitaATECO(new CodeDescription());

        String codOTE = rs.getString("ALIAS_ATTIVITA_OTE");
        if(codOTE!=null){
          CodeDescription codeOTE = new CodeDescription(new Integer(codOTE),
              rs.getString("ALIAS_DESC_TOT"));
          codeOTE.setSecondaryCode(rs.getString("ALIAS_CODICE_TOT"));
          result.setTipoAttivitaOTE(codeOTE);
        }
        else
          result.setTipoAttivitaOTE(new CodeDescription());

        result.setProvCompetenza(rs.getString("PROVINCIA_COMPETENZA"));
        result.setSiglaProvCompetenza(rs.getString("ALIAS_PROV_COMPETENZA"));
        result.setDescrizioneProvCompetenza(rs.getString("ALIAS_DESC_PROV_COMPETENZA"));
        result.setCCIAAprovREA(rs.getString("CCIAA_PROVINCIA_REA"));
        result.setCCIAAnumeroREA(new Long(rs.getLong("CCIAA_NUMERO_REA")));
        result.setStrCCIAAnumeroREA(rs.getString("CCIAA_NUMERO_REA"));
        result.setCCIAAnumRegImprese(rs.getString("CCIAA_NUMERO_REGISTRO_IMPRESE"));
        result.setCCIAAannoIscrizione(rs.getString("CCIAA_ANNO_ISCRIZIONE"));

        String flagEstero = rs.getString("ALIAS_SEDELE_ESTERO");
        result.setSedelegComune(rs.getString("SEDELEG_COMUNE"));

        if(flagEstero==null || flagEstero.equals("")){
          result.setSedelegComune("");
          result.setSedelegEstero("");
          result.setStatoEstero("");
          result.setSedelegIstatProv("");
          result.setSedelegProv("");
          result.setDescComune("");
          result.setSedelegCAP("");
          result.setSedelegCittaEstero("");
        }
        else if(flagEstero.equals(SolmrConstants.FLAG_S)){
          String sedelegEstero = rs.getString("ALIAS_DESCOM");
          result.setSedelegEstero(sedelegEstero);
          result.setStatoEstero(sedelegEstero);
          result.setSedelegIstatProv("");
          result.setSedelegProv("");
          result.setDescComune("");
          result.setSedelegCAP("");
          result.setSedelegCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
        }
        else{
          result.setSedelegIstatProv(rs.getString("ALIAS_ISTAT_PROV"));
          result.setSedelegProv(rs.getString("ALIAS_PROV"));
          result.setDescComune(rs.getString("ALIAS_DESCOM"));
          result.setSedelegCAP(rs.getString("SEDELEG_CAP"));
          result.setSedelegCittaEstero("");
          result.setSedelegEstero("");
          result.setStatoEstero("");
        }

        result.setSedelegIndirizzo(rs.getString("SEDELEG_INDIRIZZO"));

        result.setMail(rs.getString("ALIAS_MAIL"));
        result.setSitoWEB(rs.getString("SITOWEB"));
        result.setTelefono(rs.getString("TELEFONO"));
        result.setFax(rs.getString("FAX"));
        result.setPec(rs.getString("PEC"));
        result.setCodiceAgriturismo(rs.getString("CODICE_AGRITURISMO"));
        result.setNote(rs.getString("NOTE"));
        result.setDataCessazione(rs.getDate("DATA_CESSAZIONE"));
        result.setCausaleCessazione(rs.getString("CAUSALE_CESSAZIONE"));
        java.util.Date dataAgg = rs.getDate("DATA_AGGIORNAMENTO");
        result.setDataAggiornamento(dataAgg);
        result.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        String motivoModifica = rs.getString("MOTIVO_MODIFICA");
        String utDenominazione = rs.getString("UT_DENOMINAZIONE");
        String utEnteAppart = rs.getString("UT_ENTE_APPART");
        String data = DateUtils.getCurrentDateString();
        if(result.getDataFineVal()!=null && result.getDataFineVal().before(DateUtils.parseDate(DateUtils.getCurrentDateString()))){
          data = DateUtils.formatDate(result.getDataInizioVal());
        }
        result.setDataSituazioneAlStr(data);

        String ultimaModifica = DateUtils.formatDate(dataAgg);
        String tmp="";
        if(utDenominazione!=null && !utDenominazione.equals("")){
          if(tmp.equals(""))
            tmp+=" (";
          tmp+=utDenominazione;
        }
        if(utEnteAppart!=null && !utEnteAppart.equals("")){
          if(tmp.equals(""))
            tmp+=" (";
          else
            tmp+=" - ";
          tmp+=utEnteAppart;
        }
        if(motivoModifica!=null && !motivoModifica.equals("")){
          if(tmp.equals(""))
            tmp+=" (";
          else
            tmp+=" - ";
          tmp+=motivoModifica;
        }
        if(!tmp.equals(""))
          tmp+=")";
        ultimaModifica+=tmp;
        result.setUltimaModifica(ultimaModifica);
        result.setPosizioneSchedario(rs.getString("NUMERO_AGEA"));
        
        //Ultima Modifica Spezzata
        result.setDataUltimaModifica(dataAgg);
        result.setUtenteUltimaModifica(utDenominazione);
        result.setEnteUltimaModifica(utEnteAppart);
        result.setMotivoModifica(motivoModifica);

        /**
         * Questa porzione di codice va a vedere se l'azienda con cui sto
         * lavorando è un'azienda provvisoria
         */
        String temp=rs.getString("FLAG_AZIENDA_PROVVISORIA");
        if ("S".equals(temp))
        {
          result.setFlagAziendaProvvisoria(true);
          /*temp=rs.getString("ID_AZIENDA_PROVENIENZA");
          if (temp!=null)
            result.setIdAziendaSubentro(new Long(temp));*/
        }
        else
          result.setFlagAziendaProvvisoria(false);
        if(Validator.isNotEmpty(rs.getString("ID_AZIENDA_PROVENIENZA"))) {
          result.setIdAziendaProvenienza(new Long(rs.getLong("ID_AZIENDA_PROVENIENZA")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_CESSAZIONE"))) {
          result.setIdCessazione(new Long(rs.getLong("ID_CESSAZIONE")));
          TipoCessazioneVO tipoCessazioneVO = new TipoCessazioneVO();
          tipoCessazioneVO.setIdCessazione(new Long(rs.getLong("ID_CESSAZIONE")));
          tipoCessazioneVO.setDescrizione(rs.getString("DESC_CESSAZIONE"));
          result.setTipoCessazioneVO(tipoCessazioneVO);
        }
        
        result.setFascicoloDematerializzato(rs.getString("FASCICOLO_DEMATERIALIZZATO"));

      }
      rs.close();

      SolmrLogger.debug(this, "Executed query - Found record with primary key: "+anagAziendaPK);
    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "findAziendaByIdAnagAzienda - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (DataAccessException daexc) {
      SolmrLogger.fatal(this, "findAziendaByIdAnagAzienda - ResultSet null");
      throw daexc;
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "findAziendaByIdAnagAzienda - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    } finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "findAziendaByIdAnagAzienda - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "findAziendaByIdAnagAzienda - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
	
	/**
	 * Metodo utilizzato per recuperare l'elenco delle aziende su cui insistono le particelle
	 * ad asservimento: va utilizzato dopo il search delle particelle in modo
	 * da ottenere gli id_storico_particelle indispensabili per la query
	 * 
	 * @param idStoricoParticella
	 * @param idAzienda
	 * @param idTitoloPossesso
	 * @return it.csi.solmr.dto.anag.AnagAziendaVO[]
	 * @throws DataAccessException
	 */
	public AnagAziendaVO[] getListAziendeParticelleAsservite(Long idStoricoParticella, 
	    Long idAzienda, Long idTitoloPossesso, Date dataInserimentoDichiarazione) throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating getListAziendeParticelleAsservite method in AnagrafeDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<AnagAziendaVO> elencoAziende = new Vector<AnagAziendaVO>();

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in getListAziendeParticelleAsservite method in AnagrafeDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListAziendeParticelleAsservite method in AnagrafeDAO and it values: "+conn+"\n");

			String query = " SELECT AA.CUAA, " +
						   "        AA.DENOMINAZIONE, " +
						   "        AA.DATA_CESSAZIONE " +
						   " FROM   DB_DICHIARAZIONE_CONSISTENZA DIC, " +
						   "        DB_CONDUZIONE_DICHIARATA CD, " + 
						   "        DB_STORICO_PARTICELLA SP, " +
						   "        DB_ANAGRAFICA_AZIENDA AA " + 
						   " WHERE  SP.ID_STORICO_PARTICELLA = ? " +
						   " AND    DIC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
						   " AND    DIC.ID_AZIENDA <> ? " +
						   " AND    DIC.DATA_INSERIMENTO_DICHIARAZIONE = " +
						   "                                             (SELECT MAX(DATA_INSERIMENTO_DICHIARAZIONE) " +
						   "                                              FROM   DB_DICHIARAZIONE_CONSISTENZA DC1 " +
						   "                                              WHERE  DIC.ID_AZIENDA = DC1.ID_AZIENDA ";
			if(Validator.isNotEmpty(dataInserimentoDichiarazione))
			{
			  query += 
			         "                                              AND DC1.DATA_INSERIMENTO_DICHIARAZIONE < ? ";
			}
			
			query += "                                             ) " +
						   " AND    CD.ID_PARTICELLA = SP.ID_PARTICELLA " +
						   " AND    CD.ID_TITOLO_POSSESSO = ? " +           
						   " AND    AA.ID_AZIENDA = DIC.ID_AZIENDA " +
						   " AND    AA.DATA_FINE_VALIDITA IS NULL ";
						   //" AND    AA.DATA_CESSAZIONE IS NULL ";

			
			stmt = conn.prepareStatement(query);
			
			int indice = 0;
			
			stmt.setLong(++indice, idStoricoParticella.longValue());
			stmt.setLong(++indice, idAzienda.longValue());
			if(Validator.isNotEmpty(dataInserimentoDichiarazione))
      {
			  stmt.setTimestamp(++indice, convertDateToTimestamp(dataInserimentoDichiarazione));
      }
			stmt.setLong(++indice, idTitoloPossesso.longValue());
			
			SolmrLogger.debug(this, "Executing getListAziendeParticelleAsservite: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				AnagAziendaVO anagAziendaVO = new AnagAziendaVO();
				anagAziendaVO.setCUAA(rs.getString("CUAA"));
				anagAziendaVO.setDenominazione(rs.getString("DENOMINAZIONE"));
				anagAziendaVO.setDataCessazione(rs.getTimestamp("DATA_CESSAZIONE"));
				elencoAziende.add(anagAziendaVO);
			}
			
			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListAziendeParticelleAsservite in AnagrafeDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListAziendeParticelleAsservite in AnagrafeDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListAziendeParticelleAsservite in AnagrafeDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListAziendeParticelleAsservite in AnagrafeDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListAziendeParticelleAsservite method in AnagrafeDAO\n");
		if(elencoAziende.size() == 0) {
			return (AnagAziendaVO[])elencoAziende.toArray(new AnagAziendaVO[0]);
		}
		else {
			return (AnagAziendaVO[])elencoAziende.toArray(new AnagAziendaVO[elencoAziende.size()]);
		}
	}
	
	
	/**
   * Restituisce un Vector di AnagAziendaVO.
   * I parametri sono vIdAzienda un vettore che contiene gli id_azienda_collegata
   *
   * @param idAzienda
   * 
   * 
   * @return
   * @throws DataAccessException
   * @throws SolmrException
   */
  public Vector<AziendaCollegataVO> getAziendeCollegateByRangeIdAziendaCollegata(Vector<Long> vIdAziendaCollegata)
    throws DataAccessException, SolmrException
  {
    Vector<AziendaCollegataVO> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "" +
      		"SELECT " +
      		"        NVL2(DAC.ID_AZIENDA_ASSOCIATA,AA.CUAA,DAS.CUAA) AS CUAA, " +
      		"        NVL2(DAC.ID_AZIENDA_ASSOCIATA,AA.DENOMINAZIONE,DAS.DENOMINAZIONE) AS DENOMINAZIONE, "+ 
      		"        NVL2(DAC.ID_AZIENDA_ASSOCIATA,AA.PARTITA_IVA,DAS.PARTITA_IVA) AS PARTITA_IVA, " + 
      		"        NVL2(DAC.ID_AZIENDA_ASSOCIATA,AA.SEDELEG_INDIRIZZO,DAS.INDIRIZZO) AS INDIRIZZO," + 
      		"        NVL2(DAC.ID_AZIENDA_ASSOCIATA,AA.SEDELEG_CAP,DAS.CAP) AS CAP, " + 
      		"        NVL2(DAC.ID_AZIENDA_ASSOCIATA,AA.SEDELEG_COMUNE,DAS.COMUNE) AS COMUNE, " +
      		"        AA.SEDELEG_CITTA_ESTERO, AA.ID_AZIENDA, AA.ID_ANAGRAFICA_AZIENDA, "+
      		"        AA.ID_UTENTE_AGGIORNAMENTO, " +
            "        DAC.DATA_INGRESSO, DAC.DATA_USCITA, DAC.DATA_INIZIO_VALIDITA, " +
            "        DAC.DATA_FINE_VALIDITA, DAC.DATA_AGGIORNAMENTO, DAC.ID_AZIENDA_COLLEGATA, " +
            "        DAC.ID_SOGGETTO_ASSOCIATO, DAC.ID_AZIENDA_ASSOCIATA, " +
            "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
            "          || ' ' " + 
            "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
            "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
          "         WHERE DAC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
          "        AS UI_DENOMINAZIONE, " +
          " (SELECT PVU.DENOMINAZIONE " +
          " FROM PAPUA_V_UTENTE_LOGIN PVU " +
          " WHERE DAC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
          "        AS UI_ENTEAPPARTENENZA, "+
          "        CO.DESCOM, CO.FLAG_ESTERO, " +
          "        PR.SIGLA_PROVINCIA, PR.ISTAT_PROVINCIA " +
          "FROM    DB_ANAGRAFICA_AZIENDA AA," +
          "        DB_AZIENDA_COLLEGATA DAC, " +
          "        DB_SOGGETTO_ASSOCIATO DAS, " +
          //"        PAPUA_V_UTENTE_LOGIN PVU, " +
          "        COMUNE CO, PROVINCIA PR "+
          "WHERE   DAC.ID_AZIENDA_COLLEGATA IN ";
      
      for(int i=0; i<vIdAziendaCollegata.size();i++)
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
      
      query +=
      "AND DAC.ID_AZIENDA_ASSOCIATA = AA.ID_AZIENDA(+) " +
      "AND DAC.ID_SOGGETTO_ASSOCIATO = DAS.ID_SOGGETTO_ASSOCIATO(+) " +
      "AND AA.DATA_FINE_VALIDITA IS NULL " +
      "AND AA.DATA_CESSAZIONE IS NULL " +
      //"AND DAC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
      "AND NVL2(DAC.ID_AZIENDA_ASSOCIATA,AA.SEDELEG_COMUNE,DAS.COMUNE) = CO.ISTAT_COMUNE "+
      "AND CO.ISTAT_PROVINCIA = PR.ISTAT_PROVINCIA ";




      query += "ORDER BY " +
      "NVL2(DAC.ID_AZIENDA_ASSOCIATA,AA.DENOMINAZIONE,DAS.DENOMINAZIONE), " +
      "NVL2(DAC.ID_AZIENDA_ASSOCIATA,AA.CUAA,DAS.CUAA) ";

      stmt = conn.prepareStatement(query);
      
      int idx = 0;
      for(int i=0; i<vIdAziendaCollegata.size();i++)
      {
        stmt.setLong(++idx, ((Long)vIdAziendaCollegata.get(i)).longValue());
      }
      
      SolmrLogger.debug(this, "Executing query: "+query);

      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(result == null)
        {
          result = new Vector<AziendaCollegataVO>();
        }
        AziendaCollegataVO aziendaTempt = new AziendaCollegataVO();
        Long idSoggettoAssociato = checkLongNull(rs.getString("ID_SOGGETTO_ASSOCIATO"));
        if(idSoggettoAssociato != null)
        {
          SoggettoAssociatoVO soggVO = new SoggettoAssociatoVO();
          soggVO.setCuaa(rs.getString("CUAA"));
          soggVO.setDenominazione(rs.getString("DENOMINAZIONE"));
          soggVO.setPartitaIva(rs.getString("PARTITA_IVA"));
          soggVO.setIndirizzo(rs.getString("INDIRIZZO"));
          soggVO.setCap(rs.getString("CAP"));
          soggVO.setComune(rs.getString("COMUNE"));
          soggVO.setSglProv(rs.getString("SIGLA_PROVINCIA"));
          soggVO.setDenominazioneComune(rs.getString("DESCOM"));
          
          aziendaTempt.setIdSoggettoAssociato(idSoggettoAssociato);
          aziendaTempt.setSoggettoAssociato(soggVO);
        }
        else
        {
          aziendaTempt.setCuaa(rs.getString("CUAA"));
          aziendaTempt.setDenominazione(rs.getString("DENOMINAZIONE"));
          aziendaTempt.setPartitaIva(rs.getString("PARTITA_IVA"));
          aziendaTempt.setIndirizzo(rs.getString("INDIRIZZO"));
          aziendaTempt.setIstatComune(rs.getString("COMUNE"));
          
          String flagEstero = rs.getString("FLAG_ESTERO");
          String descComune = rs.getString("DESCOM");

          if(flagEstero.equals(SolmrConstants.FLAG_S))
          {
            aziendaTempt.setSedeEstero(descComune);
            aziendaTempt.setSedeCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
            aziendaTempt.setSglProv("");
            aziendaTempt.setDenominazioneComune("");
            aziendaTempt.setCap("");
          }
          else
          {
            aziendaTempt.setSedeEstero("");
            aziendaTempt.setSedeCittaEstero("");
            aziendaTempt.setSglProv(rs.getString("SIGLA_PROVINCIA"));
            aziendaTempt.setDenominazioneComune(descComune);
            aziendaTempt.setCap(rs.getString("CAP"));
          }
                  
          aziendaTempt.setIdAziendaAssociata(new Long(rs.getLong("ID_AZIENDA_ASSOCIATA")));
          
        }
        aziendaTempt.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        aziendaTempt.setDataIngresso(rs.getDate("DATA_INGRESSO"));
        aziendaTempt.setDataUscita(rs.getDate("DATA_USCITA"));
        aziendaTempt.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        aziendaTempt.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        aziendaTempt.setIdAziendaCollegata(new Long(rs.getLong("ID_AZIENDA_COLLEGATA")));
        aziendaTempt.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
        aziendaTempt.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        aziendaTempt.setDescrizioneUtenteModifica(rs.getString("UI_DENOMINAZIONE"));
        aziendaTempt.setDescrizioneEnteUtenteModifica(rs.getString("UI_ENTEAPPARTENENZA"));

        result.add(aziendaTempt);
      }

      rs.close();
      stmt.close();

    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "getAziendeCollegateByRangeIdAziendaCollegata - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "getAziendeCollegateByRangeIdAziendaCollegata - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "getAziendeCollegateByRangeIdAziendaCollegata - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "getAziendeCollegateByRangeIdAziendaCollegata - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  
  /**
   * Passo come parametro l'azienda della quale la query restituisce
   * un vector di AnagAziendaVO con valorizzati solo l'id_azienda_collegata 
   * e l'id_azienda dei padri/nonni/ecc.
   * Se non ne ha restitusce null!
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   * @throws SolmrException
   */
  public Vector<AnagAziendaVO> getIdAziendaCollegataAncestor(Long idAzienda)
    throws DataAccessException, SolmrException
  {
    Vector<AnagAziendaVO> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
      
      String query = "SELECT "+
        "ID_AZIENDA_COLLEGATA, ID_AZIENDA, ID_AZIENDA_ASSOCIATA, LEVEL "+
        "FROM DB_AZIENDA_COLLEGATA "+
        "CONNECT BY PRIOR ID_AZIENDA = ID_AZIENDA_ASSOCIATA "+
        "AND NVL(TRUNC(DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > SYSDATE "+
        "START WITH ID_AZIENDA_ASSOCIATA = ? "+
        "AND NVL(TRUNC(DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > SYSDATE";
  
      stmt = conn.prepareStatement(query);
      
      int idx = 0;
      stmt.setLong(++idx, idAzienda.longValue());
      
      
      SolmrLogger.debug(this, "Executing query: "+query);
  
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(result == null)
        {
          result = new Vector<AnagAziendaVO>();
        }
        AnagAziendaVO aziendaTempt = new AnagAziendaVO();
        aziendaTempt.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        aziendaTempt.setIdAziendaCollegata(new Long(rs.getLong("ID_AZIENDA_COLLEGATA")));
        
  
        result.add(aziendaTempt);
      }
  
      rs.close();
      stmt.close();
  
    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "getIdAziendaCollegataAnchestor - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "getIdAziendaCollegataAnchestor - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "getIdAziendaCollegataAnchestor - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "getIdAziendaCollegataAnchestor - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  
  /**
   * Passo come parametro l'azienda della quale la query restituisce
   * un vector di AnagAziendaVO con valorizzati solo l'id_azienda_collegata 
   * e l'id_azienda dei discendenti.
   * Se non ne ha restitusce null!
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   * @throws SolmrException
   */
  public Vector<AnagAziendaVO> getIdAziendaCollegataDescendant(Long idAzienda)
    throws DataAccessException, SolmrException
  {
    Vector<AnagAziendaVO> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
  
      
      String query = "SELECT "+
        "ID_AZIENDA_COLLEGATA, ID_AZIENDA, ID_AZIENDA_ASSOCIATA, LEVEL "+ 
        "FROM DB_AZIENDA_COLLEGATA "+ 
        "CONNECT BY PRIOR ID_AZIENDA_ASSOCIATA = ID_AZIENDA "+ 
        "AND NVL(TRUNC(DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > SYSDATE "+
        "START WITH ID_AZIENDA = ? "+ 
        "AND NVL(TRUNC(DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > SYSDATE ";
  
      stmt = conn.prepareStatement(query);
      
      int idx = 0;
      stmt.setLong(++idx, idAzienda.longValue());
      
      
      SolmrLogger.debug(this, "Executing query: "+query);
  
      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(result == null)
        {
          result = new Vector<AnagAziendaVO>();
        }
        AnagAziendaVO aziendaTempt = new AnagAziendaVO();
        aziendaTempt.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        aziendaTempt.setIdAziendaCollegata(new Long(rs.getLong("ID_AZIENDA_COLLEGATA")));
        
  
        result.add(aziendaTempt);
      }
  
      rs.close();
      stmt.close();
  
    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "getIdAziendaCollegataAnchestor - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "getIdAziendaCollegataAnchestor - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "getIdAziendaCollegataAnchestor - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "getIdAziendaCollegataAnchestor - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  

  /**
   * Questo metodo serve per controllare che idAziendaCollegata faccia parte di
   * un azienda collegata a CUAAPapre
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public boolean controlloAziendeAssociate(String CUAApadre, Long idAziendaCollegata)
    throws DataAccessException
  {
    boolean result = false;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
  
      
      String query = "SELECT A.* FROM DB_ANAGRAFICA_AZIENDA A " +
          "WHERE ? " +
          "IN " +
          "(" +
          "SELECT " +
          "        ID_AZIENDA_ASSOCIATA  " +
          "        FROM DB_AZIENDA_COLLEGATA" +
          "        WHERE ID_AZIENDA = A.ID_AZIENDA " +
          "        AND NVL(TRUNC(DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > SYSDATE " +
          "        AND NVL(TRUNC(DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > SYSDATE " +
          ")" +
          "AND A.CUAA=? " +
          "AND A.DATA_CESSAZIONE IS NULL " +
          "AND A.DATA_FINE_VALIDITA IS NULL ";
  
      SolmrLogger.debug(this, "Executing controlloAziendeAssociate query: "+query);
      
      stmt = conn.prepareStatement(query);
      
      SolmrLogger.debug(this, "Executing controlloAziendeAssociate param CUAApadre: "+CUAApadre);
      SolmrLogger.debug(this, "Executing controlloAziendeAssociate param idAziendaCollegata: "+idAziendaCollegata);
      
      int idx = 0;
      stmt.setLong(++idx, idAziendaCollegata.longValue());
      stmt.setString(++idx, CUAApadre.toUpperCase());
      
  
  
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
        result=true;
      
      SolmrLogger.debug(this, "Executed query");
  
      rs.close();
      stmt.close();
  
    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "controlloAziendeAssociate - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "controlloAziendeAssociate - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "controlloAziendeAssociate - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "controlloAziendeAssociate - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  
  /**
   * Restituisce un Vector di AnagAziendaVO.
   * I parametri sono vIdAzienda un vettore che contiene gli id_azienda
   *
   * @param idAzienda
   * 
   * 
   * @return
   * @throws DataAccessException
   * @throws SolmrException
   */
  public Vector<AnagAziendaVO> getAziendeCollegateByRangeIdAzienda(Vector<Long> vIdAzienda, Long idAziendaPadre)
    throws DataAccessException, SolmrException
  {
    Vector<AnagAziendaVO> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String query = "  SELECT AA.CUAA, AA.DENOMINAZIONE, AA.PARTITA_IVA, "+
      "AA.SEDELEG_INDIRIZZO, AA.SEDELEG_CAP, AA.SEDELEG_COMUNE, "+
      "AA.SEDELEG_CITTA_ESTERO, AA.ID_AZIENDA, " +
      "AA.ID_ANAGRAFICA_AZIENDA, "+
      "DAC.DATA_INGRESSO, " +
      "DAC.DATA_USCITA, " +
      "DAC.DATA_INIZIO_VALIDITA, " +
      "DAC.DATA_FINE_VALIDITA, " +
      "DAC.DATA_AGGIORNAMENTO, " +
      "DAC.ID_AZIENDA_COLLEGATA, " +
      "AA.ID_UTENTE_AGGIORNAMENTO, " +
      "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
      "          || ' ' " + 
      "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
      "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
      "         WHERE DAC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
      "  AS UI_DENOMINAZIONE, " +
      " (SELECT PVU.DENOMINAZIONE " +
      " FROM PAPUA_V_UTENTE_LOGIN PVU " +
      " WHERE DAC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
      "  AS UI_ENTEAPPARTENENZA, " +
      "CO.DESCOM, CO.FLAG_ESTERO, " +
      "PR.SIGLA_PROVINCIA, PR.ISTAT_PROVINCIA "+
      "FROM DB_ANAGRAFICA_AZIENDA AA," +
      "DB_AZIENDA_COLLEGATA DAC, "+
      //"PAPUA_V_UTENTE_LOGIN PVU, " +
      "COMUNE CO, PROVINCIA PR "+
      "WHERE AA.ID_AZIENDA IN ";
      
      for(int i=0; i<vIdAzienda.size();i++)
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
      
      query +=
      "AND DAC.ID_AZIENDA = ? "+  
      "AND DAC.ID_AZIENDA_ASSOCIATA = AA.ID_AZIENDA "+
      "AND AA.DATA_FINE_VALIDITA IS NULL "+
      "AND AA.DATA_CESSAZIONE IS NULL "+
      //"AND DAC.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
      "AND AA.SEDELEG_COMUNE = CO.ISTAT_COMUNE(+) "+
      "AND CO.ISTAT_PROVINCIA = PR.ISTAT_PROVINCIA(+) "+
      "AND NVL(TRUNC(DAC.DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > SYSDATE "+
      "AND NVL(TRUNC(DAC.DATA_USCITA), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > SYSDATE ";




      query += "ORDER BY AA.DENOMINAZIONE,AA.CUAA ";

      stmt = conn.prepareStatement(query);
      
      int idx = 0;
      for(int i=0; i<vIdAzienda.size();i++)
      {
        stmt.setLong(++idx, ((Long)vIdAzienda.get(i)).longValue());
      }
      
      stmt.setLong(++idx, idAziendaPadre.longValue());
      
      SolmrLogger.debug(this, "Executing query: "+query);

      ResultSet rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(result == null)
        {
          result = new Vector<AnagAziendaVO>();
        }
        AnagAziendaVO aziendaTempt = new AnagAziendaVO();
        aziendaTempt.setCUAA(rs.getString("CUAA"));
        aziendaTempt.setDenominazione(rs.getString("DENOMINAZIONE"));
        aziendaTempt.setPartitaIVA(rs.getString("PARTITA_IVA"));
        aziendaTempt.setSedelegComune(rs.getString("SEDELEG_COMUNE"));
        aziendaTempt.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        aziendaTempt.setIdAnagAzienda(new Long(rs.getLong("ID_ANAGRAFICA_AZIENDA")));
        String sedelegDescComune = rs.getString("DESCOM");
        String flagEstero = rs.getString("FLAG_ESTERO");

        if(flagEstero==null || flagEstero.equals("")){
          aziendaTempt.setSedelegComune("");
          aziendaTempt.setSedelegEstero("");
          aziendaTempt.setStatoEstero("");
          aziendaTempt.setSedelegCittaEstero("");
          aziendaTempt.setSedelegIstatProv("");
          aziendaTempt.setSedelegProv("");
          aziendaTempt.setDescComune("");
          aziendaTempt.setSedelegCAP("");
        }
        else if(flagEstero.equals(SolmrConstants.FLAG_S))
        {
          aziendaTempt.setSedelegEstero(sedelegDescComune);
          aziendaTempt.setStatoEstero(sedelegDescComune);
          aziendaTempt.setSedelegCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
          aziendaTempt.setSedelegIstatProv("");
          aziendaTempt.setSedelegProv("");
          aziendaTempt.setDescComune("");
          aziendaTempt.setSedelegCAP("");
        }
        else
        {
          aziendaTempt.setSedelegEstero("");
          aziendaTempt.setSedelegCittaEstero("");
          aziendaTempt.setSedelegIstatProv(rs.getString("ISTAT_PROVINCIA"));
          aziendaTempt.setSedelegProv(rs.getString("SIGLA_PROVINCIA"));
          aziendaTempt.setDescComune(sedelegDescComune);
          aziendaTempt.setSedelegCAP(rs.getString("SEDELEG_CAP"));
        }

        aziendaTempt.setSedelegIndirizzo(rs.getString("SEDELEG_INDIRIZZO"));

        aziendaTempt.setDataIngresso(rs.getDate("DATA_INGRESSO"));
        aziendaTempt.setDataUscita(rs.getDate("DATA_USCITA"));
        aziendaTempt.setDataInizioVal(rs.getDate("DATA_INIZIO_VALIDITA"));
        aziendaTempt.setDataFineVal(rs.getDate("DATA_FINE_VALIDITA"));
        aziendaTempt.setIdAziendaCollegata(new Long(rs.getLong("ID_AZIENDA_COLLEGATA")));


        aziendaTempt.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
        aziendaTempt.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        aziendaTempt.setDescrizioneUtenteModifica(rs.getString("UI_DENOMINAZIONE"));
        aziendaTempt.setDescrizioneEnteUtenteModifica(rs.getString("UI_ENTEAPPARTENENZA"));

        result.add(aziendaTempt);
      }

      rs.close();
      stmt.close();

    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "getAziendeCollegateByRangeIdAzienda - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "getAziendeCollegateByRangeIdAzienda - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "getAziendeCollegateByRangeIdAzienda - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "getAziendeCollegateByRangeIdAzienda - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  /**
   * Metodo utilizzato per effettuare la modifica del record relativo alla tabella
   * DB_AZIENDA_COLLEGATA
   * 
   * @param AziendaCollegataVO
   * @throws DataAccessException
   */
  public void updateAziendaCollegata(AziendaCollegataVO aziendaCollegataVO) throws DataAccessException {
    SolmrLogger.debug(this, "Invocating updateAziendaCollegata method in AnagrafeDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in updateAziendaCollegata method in AnagrafeDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in updateAziendaCollegata method in AnagrafeDAO and it values: "+conn+"\n");
      
      String query = " UPDATE DB_AZIENDA_COLLEGATA " +
                         "SET ID_AZIENDA = ?, " +
                         "ID_AZIENDA_ASSOCIATA = ?, " +
                         "DATA_INGRESSO = ?, " +
                         "DATA_USCITA = ?, " +
                         "DATA_INIZIO_VALIDITA = ?, "+
                         "DATA_FINE_VALIDITA = ?, " +
                         "DATA_AGGIORNAMENTO = ?, " +
                         "ID_UTENTE_AGGIORNAMENTO = ?, " +
                         "ID_SOGGETTO_ASSOCIATO = ? " +
                         "WHERE  ID_AZIENDA_COLLEGATA = ? ";

      conn = getDatasource().getConnection();

      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in updateAziendaCollegata method in AnagrafeDAO: "+aziendaCollegataVO.getIdAzienda()+"\n");
      stmt.setLong(1, aziendaCollegataVO.getIdAzienda().longValue());
      
      SolmrLogger.debug(this, "Value of parameter 2 [ID_AZIENDA_ASSOCIATA] in updateAziendaCollegata method in AnagrafeDAO: "+aziendaCollegataVO.getIdAziendaAssociata()+"\n");
      if(aziendaCollegataVO.getIdAziendaAssociata() != null) {
        stmt.setLong(2, aziendaCollegataVO.getIdAziendaAssociata().longValue());
      }
      else {
        stmt.setString(2, null);
      }
      SolmrLogger.debug(this, "Value of parameter 3 [DATA_INGRESSO] in updateAziendaCollegata method in AnagrafeDAO: "+aziendaCollegataVO.getDataIngresso()+"\n");
      if(aziendaCollegataVO.getDataIngresso() != null) {
        stmt.setDate(3, new java.sql.Date(aziendaCollegataVO.getDataIngresso().getTime()));
      }
      else {
        stmt.setString(3, null);
      }
      SolmrLogger.debug(this, "Value of parameter 4 [DATA_USCITA] in updateAziendaCollegata method in AnagrafeDAO: "+aziendaCollegataVO.getDataUscita()+"\n");
      if(aziendaCollegataVO.getDataUscita() != null) {
        stmt.setDate(4, new java.sql.Date(aziendaCollegataVO.getDataUscita().getTime()));
      }
      else {
        stmt.setString(4, null);
      }
      SolmrLogger.debug(this, "Value of parameter 5 [DATA_INIZIO_VALIDITA] in updateAziendaCollegata method in AnagrafeDAO: "+aziendaCollegataVO.getDataInizioValidita()+"\n");
      if(aziendaCollegataVO.getDataInizioValidita() != null) {
        stmt.setTimestamp(5, convertDateToTimestamp(aziendaCollegataVO.getDataInizioValidita()));
      }
      else {
        stmt.setString(5, null);
      }
      SolmrLogger.debug(this, "Value of parameter 6 [DATA_FINE_VALIDITA] in updateAziendaCollegata method in AnagrafeDAO: "+aziendaCollegataVO.getDataFineValidita()+"\n");
      if(aziendaCollegataVO.getDataFineValidita() != null) {
        stmt.setTimestamp(6, convertDateToTimestamp(aziendaCollegataVO.getDataFineValidita()));
      }
      else {
        stmt.setString(6, null);
      }
      SolmrLogger.debug(this, "Value of parameter 7 [DATA_AGGIORNAMENTO] in updateAziendaCollegata method in AnagrafeDAO: "+aziendaCollegataVO.getDataAggiornamento()+"\n");
      if(aziendaCollegataVO.getDataAggiornamento() != null) {
        stmt.setTimestamp(7, convertDateToTimestamp(aziendaCollegataVO.getDataAggiornamento()));
      }
      else {
        stmt.setString(7, null);
      }
      SolmrLogger.debug(this, "Value of parameter 8 [ID_UTENTE_AGGIORNAMENTO] in updateAziendaCollegata method in AnagrafeDAO: "+aziendaCollegataVO.getIdUtenteAggiornamento()+"\n");
      stmt.setLong(8, aziendaCollegataVO.getIdUtenteAggiornamento().longValue());
      SolmrLogger.debug(this, "Value of parameter 9 [ID_SOGGETTO_ASSOCIATO] in updateAziendaCollegata method in AnagrafeDAO: "+aziendaCollegataVO.getIdSoggettoAssociato()+"\n");
      if(aziendaCollegataVO.getIdSoggettoAssociato() != null) {
        stmt.setLong(9, aziendaCollegataVO.getIdSoggettoAssociato().longValue());
      }
      else {
        stmt.setString(9, null);
      }
      
      SolmrLogger.debug(this, "Value of parameter 10 [ID_AZIENDA_COLLEGATA] in updateAziendaCollegata method in AnagrafeDAO: "+aziendaCollegataVO.getIdAziendaCollegata()+"\n");
      stmt.setLong(10, aziendaCollegataVO.getIdAziendaCollegata().longValue());
      

      stmt.executeUpdate();

      stmt.close();
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "updateAziendaCollegata in AnagrafeDAO - SQLException: "+exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.error(this, "updateAziendaCollegata in AnagrafeDAO - Generic Exception: "+ex);
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
        SolmrLogger.error(this, "updateAziendaCollegata in AnagrafeDAO - SQLException while closing Statement and Connection: "+exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "updateAziendaCollegata in AnagrafeDAO - Generic Exception while closing Statement and Connection: "+ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated updateAziendaCollegata method in AnagrafeDAO\n");
  }
  
  
  /**
   * Ricavo l'oggetto AziendaCollegataVo attravesro ida_azienda_collegata
   * 
   * @param idAziendaCollegata
   * @param dataSituazione
   * @return
   * @throws DataAccessException
   * @throws SolmrException
   */
  public AziendaCollegataVO findAziendaCollegataByPrimaryKey(long idAziendaCollegata)
    throws DataAccessException, SolmrException 
  {

    AziendaCollegataVO result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try 
    {
      conn = getDatasource().getConnection();

      String query =   
        "SELECT " +
        "       DAC.ID_AZIENDA_COLLEGATA, DAC.ID_AZIENDA, "+
        "       DAC.ID_AZIENDA_ASSOCIATA, DAC.DATA_INGRESSO, "+
        "       DAC.DATA_USCITA, DAC.DATA_INIZIO_VALIDITA, " +
        "       DAC.DATA_FINE_VALIDITA, DAC.DATA_AGGIORNAMENTO, " +
        "       DAC.ID_UTENTE_AGGIORNAMENTO, DAC.ID_SOGGETTO_ASSOCIATO, " +
        "       DAC.ID_AZIENDA_ASSOCIATA, " +
        "       DAS.CUAA, DAS.PARTITA_IVA, DAS.DENOMINAZIONE, " +
        "       DAS.INDIRIZZO, DAS.COMUNE, DAS.CAP " +
        "FROM   DB_AZIENDA_COLLEGATA DAC, " +
        "       DB_SOGGETTO_ASSOCIATO DAS " +
        "WHERE "+
        "      ID_AZIENDA_COLLEGATA = ? " +
        "AND   DAC.ID_SOGGETTO_ASSOCIATO = DAS.ID_SOGGETTO_ASSOCIATO(+) ";
          

      SolmrLogger.debug(this, "Executing query: "+query);

      stmt = conn.prepareStatement(query);

      int idx = 0;
      stmt.setLong(++idx, idAziendaCollegata);

      ResultSet rs = stmt.executeQuery();


      if (rs.next()) 
      {
        result = new AziendaCollegataVO();
        result.setIdAziendaCollegata(new Long(rs.getLong("ID_AZIENDA_COLLEGATA")));
        result.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        result.setIdAziendaAssociata(checkLongNull(rs.getString("ID_AZIENDA_ASSOCIATA")));
        result.setDataIngresso(rs.getDate("DATA_INGRESSO"));
        result.setDataUscita(rs.getDate("DATA_USCITA"));
        result.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        result.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        result.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO"));
        result.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        result.setIdSoggettoAssociato(checkLongNull(rs.getString("ID_SOGGETTO_ASSOCIATO")));
        if(Validator.isNotEmpty(result.getIdSoggettoAssociato()))
        {
          SoggettoAssociatoVO soggVO = new SoggettoAssociatoVO();
          soggVO.setIdSoggettoAssociato(result.getIdSoggettoAssociato().longValue());
          soggVO.setCuaa(rs.getString("CUAA"));
          soggVO.setPartitaIva(rs.getString("PARTITA_IVA"));
          soggVO.setDenominazione(rs.getString("DENOMINAZIONE"));
          soggVO.setIndirizzo(rs.getString("INDIRIZZO"));
          soggVO.setComune(rs.getString("COMUNE"));
          soggVO.setCap(rs.getString("CAP"));
          result.setSoggettoAssociato(soggVO);
        }
      }
     
      rs.close();
      stmt.close();

    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "findAziendaCollegataByPrimaryKey - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "findAziendaCollegataByPrimaryKey - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "findAziendaCollegataByPrimaryKey - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "findAziendaCollegataByPrimaryKey - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  /**
   * Metodo utilizzato per inserire un record su DB_AZIENDA_COLLEGATA
   * 
   * @param storicoUnitaArboreaVO
   * @return java.lang.Long
   * @throws DataAccessException
   */
  public Long insertAziendaCollegata(AziendaCollegataVO aziendaCollegataVO) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating insertAziendaCollegata method in AnagrafeDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Long idAziendaCollegata = null;
  
    try 
    {
      idAziendaCollegata = getNextPrimaryKey(SolmrConstants.SEQ_DB_AZIENDA_COLLEGATA);
      SolmrLogger.debug(this, "Creating db-connection in insertAziendaCollegata method in AnagrafeDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in insertAziendaCollegata method in AnagrafeDAO and it values: "+conn+"\n");
      
      String query = 
        "INSERT INTO DB_AZIENDA_COLLEGATA " +
        "(ID_AZIENDA_COLLEGATA, " +
        "ID_AZIENDA, " +
        "ID_AZIENDA_ASSOCIATA, " +
        "DATA_INGRESSO, " +
        "DATA_USCITA, " +
        "DATA_INIZIO_VALIDITA, "+
        "DATA_FINE_VALIDITA, " +
        "DATA_AGGIORNAMENTO, " +
        "ID_UTENTE_AGGIORNAMENTO," +
        "ID_SOGGETTO_ASSOCIATO) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
      
      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing insertAziendaCollegata: "+query);
      
      stmt.setLong(1, idAziendaCollegata.longValue());
      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA_COLLEGATA] in method insertAziendaCollegata method in AnagrafeDAO: "+idAziendaCollegata.longValue()+"\n");
      stmt.setLong(2, aziendaCollegataVO.getIdAzienda().longValue());
      SolmrLogger.debug(this, "Value of parameter 2 [ID_AZIENDA] in method insertAziendaCollegata method in AnagrafeDAO: "+aziendaCollegataVO.getIdAzienda().longValue()+"\n");
      if(aziendaCollegataVO.getIdAziendaAssociata() != null)
      {
        stmt.setLong(3, aziendaCollegataVO.getIdAziendaAssociata().longValue());
      }
      else
      {
        stmt.setString(3, null);
      }
      SolmrLogger.debug(this, "Value of parameter 3 [ID_AZIENDA_ASSOCIATA] in method insertAziendaCollegata method in AnagrafeDAO: "+aziendaCollegataVO.getIdAziendaAssociata()+"\n");
      if(aziendaCollegataVO.getDataIngresso() != null) {
        stmt.setDate(4, new java.sql.Date(aziendaCollegataVO.getDataIngresso().getTime()));
      }
      else {
        stmt.setString(4, null);
      }
      SolmrLogger.debug(this, "Value of parameter 4 [DATA_INGRESSO] in updateAziendaCollegata method in AnagrafeDAO: "+aziendaCollegataVO.getDataIngresso()+"\n");
      if(aziendaCollegataVO.getDataUscita() != null) {
        stmt.setDate(5, new java.sql.Date(aziendaCollegataVO.getDataUscita().getTime()));
      }
      else {
        stmt.setString(5, null);
      }
      SolmrLogger.debug(this, "Value of parameter 5 [DATA_USCITA] in updateAziendaCollegata method in AnagrafeDAO: "+aziendaCollegataVO.getDataUscita()+"\n");
      if(aziendaCollegataVO.getDataInizioValidita() != null) {
        stmt.setTimestamp(6, convertDateToTimestamp(aziendaCollegataVO.getDataInizioValidita()));
      }
      else {
        stmt.setString(6, null);
      }
      SolmrLogger.debug(this, "Value of parameter 6 [DATA_INIZIO_VALIDITA] in updateAziendaCollegata method in AnagrafeDAO: "+aziendaCollegataVO.getDataInizioValidita()+"\n");
      if(aziendaCollegataVO.getDataFineValidita() != null) {
        stmt.setTimestamp(7, convertDateToTimestamp(aziendaCollegataVO.getDataFineValidita()));
      }
      else {
        stmt.setString(7, null);
      }
      SolmrLogger.debug(this, "Value of parameter 7 [DATA_FINE_VALIDITA] in updateAziendaCollegata method in AnagrafeDAO: "+aziendaCollegataVO.getDataFineValidita()+"\n");
      if(aziendaCollegataVO.getDataAggiornamento() != null) {
        stmt.setTimestamp(8, convertDateToTimestamp(aziendaCollegataVO.getDataAggiornamento()));
      }
      else {
        stmt.setString(8, null);
      }
      SolmrLogger.debug(this, "Value of parameter 8 [DATA_AGGIORNAMENTO] in updateAziendaCollegata method in AnagrafeDAO: "+aziendaCollegataVO.getDataAggiornamento()+"\n");
      stmt.setLong(9, aziendaCollegataVO.getIdUtenteAggiornamento().longValue());
      SolmrLogger.debug(this, "Value of parameter 9 [ID_UTENTE_AGGIORNAMENTO] in method insertAziendaCollegata method in AnagrafeDAO: "+aziendaCollegataVO.getIdUtenteAggiornamento().longValue()+"\n");
      if(aziendaCollegataVO.getIdSoggettoAssociato() != null)
      {
        stmt.setLong(10, aziendaCollegataVO.getIdSoggettoAssociato().longValue());
      }
      else
      {
        stmt.setString(10, null);
      }
      SolmrLogger.debug(this, "Value of parameter 10 [ID_SOGGETTO_ASSOCIATO] in method insertAziendaCollegata method in AnagrafeDAO: "+aziendaCollegataVO.getIdSoggettoAssociato()+"\n");
      
      stmt.executeUpdate();

      stmt.close();
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "insertAziendaCollegata in AnagrafeDAO - SQLException: "+exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.error(this, "insertAziendaCollegata in AnagrafeDAO - Generic Exception: "+ex);
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
        SolmrLogger.error(this, "insertAziendaCollegata in AnagrafeDAO - SQLException while closing Statement and Connection: "+exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "insertAziendaCollegata in AnagrafeDAO - Generic Exception while closing Statement and Connection: "+ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated insertAziendaCollegata method in AnagrafeDAO\n");
      
    return idAziendaCollegata;
  }
  
  public Long insertSoggettoAssociato(SoggettoAssociatoVO soggVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idSoggettoAssociato = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[AnagrafeDAO::insertSoggettoAssociato] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idSoggettoAssociato = getNextPrimaryKey(SolmrConstants.SEQ_DB_SOGGETTO_ASSOCIATO);
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_SOGGETTO_ASSOCIATO   " 
              + "     (ID_SOGGETTO_ASSOCIATO, "
              + "     CUAA, " 
              + "     PARTITA_IVA, "
              + "     DENOMINAZIONE, "
              + "     INDIRIZZO, "
              + "     COMUNE, "
              + "     CAP) "
              + "   VALUES(?,?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[AnagrafeDAO::insertSoggettoAssociato] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idSoggettoAssociato.longValue());
      stmt.setString(++indice, soggVO.getCuaa());
      stmt.setString(++indice, soggVO.getPartitaIva());
      stmt.setString(++indice, soggVO.getDenominazione());
      stmt.setString(++indice, soggVO.getIndirizzo());
      stmt.setString(++indice, soggVO.getComune());
      stmt.setString(++indice, soggVO.getCap());
      
      
  
      stmt.executeUpdate();
      
      return idSoggettoAssociato;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idSoggettoAssociato", idSoggettoAssociato)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("soggVO", soggVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AnagrafeDAO::insertSoggettoAssociato] ",
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
              "[AnagrafeDAO::insertSoggettoAssociato] END.");
    }
  }
  
  /**
   * Restituisce un Vector di AziendaCollegataVO.
   * Sono tutti i record anche storicizzati, ottenuti con la coppia di valori
   * idAzienda e idAziendaAssociata
   * 
   * @param idAziendaPadre
   * @param idAziendaFiglia
   * @return
   * @throws DataAccessException
   * @throws SolmrException
   */
  public Vector<AziendaCollegataVO> getAllAziendeCollegateIdAziendaAssociata(Long idAziendaPadre, Long idAziendaFiglia)
    throws DataAccessException, SolmrException {

    Vector<AziendaCollegataVO> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try 
    {
      conn = getDatasource().getConnection();

      String query =   
        "SELECT ID_AZIENDA_COLLEGATA, ID_AZIENDA, "+
        "ID_AZIENDA_ASSOCIATA, DATA_INGRESSO, "+
        "DATA_USCITA, DATA_INIZIO_VALIDITA, " +
        "DATA_FINE_VALIDITA, DATA_AGGIORNAMENTO, " +
        "ID_UTENTE_AGGIORNAMENTO "+
        "FROM DB_AZIENDA_COLLEGATA "+
        "WHERE "+
        "ID_AZIENDA = ? "+
        "AND ID_AZIENDA_ASSOCIATA = ? ";
          

      SolmrLogger.debug(this, "Executing query: "+query);

      stmt = conn.prepareStatement(query);


      stmt.setLong(1, idAziendaPadre.longValue());
      stmt.setLong(2, idAziendaFiglia.longValue());

      ResultSet rs = stmt.executeQuery();


      while(rs.next()) 
      {
        AziendaCollegataVO azCollVO = new AziendaCollegataVO();
        azCollVO.setIdAziendaCollegata(new Long(rs.getLong("ID_AZIENDA_COLLEGATA")));
        azCollVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        azCollVO.setIdAziendaAssociata(new Long(rs.getLong("ID_AZIENDA_ASSOCIATA")));
        azCollVO.setDataIngresso(rs.getDate("DATA_INGRESSO"));
        azCollVO.setDataUscita(rs.getDate("DATA_USCITA"));
        azCollVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        azCollVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        azCollVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
        azCollVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        
        if(result == null)
        {
          result = new Vector<AziendaCollegataVO>();
        }
        result.add(azCollVO);
      }
     
      rs.close();
      stmt.close();

    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "getAllAziendeCollegateIdAziendaAssociata - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "getAllAziendeCollegateIdAziendaAssociata - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "getAllAziendeCollegateIdAziendaAssociata - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "getAllAziendeCollegateIdAziendaAssociata - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  /**
   * Restituisce un Vector di AziendaCollegataVO.
   * Sono tutti i record anche storicizzati, ottenuti con la coppia di valori
   * idAzienda e idSoggettoAssociato
   * 
   * @param idAziendaPadre
   * @param idAziendaFiglia
   * @return
   * @throws DataAccessException
   * @throws SolmrException
   */
  public Vector<AziendaCollegataVO> getAllAziendeCollegateIdSoggettoAssociato(Long idAziendaPadre, Long idSoggettoAssociato)
    throws DataAccessException, SolmrException {

    Vector<AziendaCollegataVO> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try 
    {
      conn = getDatasource().getConnection();

      String query =   
        "SELECT ID_AZIENDA_COLLEGATA, ID_AZIENDA, "+
        "ID_SOGGETTO_ASSOCIATO, DATA_INGRESSO, "+
        "DATA_USCITA, DATA_INIZIO_VALIDITA, " +
        "DATA_FINE_VALIDITA, DATA_AGGIORNAMENTO, " +
        "ID_UTENTE_AGGIORNAMENTO "+
        "FROM DB_AZIENDA_COLLEGATA "+
        "WHERE "+
        "ID_AZIENDA = ? "+
        "AND ID_SOGGETTO_ASSOCIATO = ? ";
          

      SolmrLogger.debug(this, "Executing query: "+query);

      stmt = conn.prepareStatement(query);


      stmt.setLong(1, idAziendaPadre.longValue());
      stmt.setLong(2, idSoggettoAssociato.longValue());

      ResultSet rs = stmt.executeQuery();


      while(rs.next()) 
      {
        AziendaCollegataVO azCollVO = new AziendaCollegataVO();
        azCollVO.setIdAziendaCollegata(new Long(rs.getLong("ID_AZIENDA_COLLEGATA")));
        azCollVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        azCollVO.setIdSoggettoAssociato(new Long(rs.getLong("ID_SOGGETTO_ASSOCIATO")));
        azCollVO.setDataIngresso(rs.getDate("DATA_INGRESSO"));
        azCollVO.setDataUscita(rs.getDate("DATA_USCITA"));
        azCollVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        azCollVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        azCollVO.setDataAggiornamento(rs.getDate("DATA_AGGIORNAMENTO"));
        azCollVO.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        
        if(result == null)
        {
          result = new Vector<AziendaCollegataVO>();
        }
        result.add(azCollVO);
      }
     
      rs.close();
      stmt.close();

    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "getAllAziendeCollegateIdSoggettoAssociato - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "getAllAziendeCollegateIdSoggettoAssociato - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "getAllAziendeCollegateIdSoggettoAssociato - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "getAllAziendeCollegateIdSoggettoAssociato - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  /**
   * Elimino un record su DB_AZIENZA_COLLEGATA con la primary key!
   * 
   * @param idAziendaCollegata
   * @throws DataAccessException
   */
  public void deleteAziendaCollegata(Long idAziendaCollegata)
  throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      //delete da db_segnalazione_dichiarazione di tutti i record con quell'
      //id_dichiarazione_consistenza
      String query = "DELETE DB_AZIENDA_COLLEGATA WHERE ID_AZIENDA_COLLEGATA = ? ";

      if (idAziendaCollegata !=null)
        SolmrLogger.debug(this, "idAziendaCollegata: "+idAziendaCollegata.longValue());

      stmt = conn.prepareStatement(query);

      stmt.setLong(1,idAziendaCollegata.longValue());

      SolmrLogger.debug(this, "Executing deleteAziendaCollegata"+query);

      stmt.executeUpdate();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in deleteAziendaCollegata "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in deleteAziendaCollegata: "+ex.getMessage());
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
        SolmrLogger.fatal(this, "SQLException while closing Statement and Connection in deleteAziendaCollegata "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "Generic Exception while closing Statement and Connection in deleteAziendaCollegata "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }
  
  /**
   * Elimino un record su DB_SOGGETTO_ASSOCIATO con la primary key
   * dopo aver verificato che non è associato e nessunrecord della tabella
   * DB_AZIENDA_COLLEGATA
   * 
   * @param idSoggettoAssociato
   * @throws DataAccessException
   */
  public void deleteSoggettoAssociato(long idSoggettoAssociato)
    throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      //delete da db_segnalazione_dichiarazione di tutti i record con quell'
      //id_dichiarazione_consistenza
      String query = "" +
      		"DELETE DB_SOGGETTO_ASSOCIATO " +
      		"WHERE  ID_SOGGETTO_ASSOCIATO = ? " +
      		"AND    NOT EXISTS (" +
      		"                    SELECT * " +
      		"                    FROM   DB_AZIENDA_COLLEGATA" +
      		"                    WHERE  ID_SOGGETTO_ASSOCIATO = ? )";

      SolmrLogger.debug(this, "idSoggettoAssociato: "+idSoggettoAssociato);

      stmt = conn.prepareStatement(query);

      int idx = 0;
      stmt.setLong(++idx,idSoggettoAssociato);
      stmt.setLong(++idx,idSoggettoAssociato);
      
      SolmrLogger.debug(this, "Executing deleteSoggettoAssociato"+query);

      stmt.executeUpdate();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in deleteSoggettoAssociato "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in deleteSoggettoAssociato: "+ex.getMessage());
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
        SolmrLogger.fatal(this, "SQLException while closing Statement and Connection in deleteSoggettoAssociato "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "Generic Exception while closing Statement and Connection in deleteAziendaCollegata "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }
  
  /**
   * 
   * Se flagDataCessazione == true l'azienda non è cessata
   * se flagAziendaProvvisoria == true l'azienda è provvisoria!
   * @param cuaa
   * @param flagDataCessazione
   * @param flagAziendaProvvisoria
   * @return
   * @throws DataAccessException
   * @throws SolmrException
   */
  public Vector<AnagAziendaVO> getAziendaByCriterioCessataAndProvvisoria(String cuaa, boolean flagDataCessazione, boolean flagAziendaProvvisoria)
    throws DataAccessException, SolmrException
  {

    Vector<AnagAziendaVO> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = getDatasource().getConnection();

      String query = 
      "SELECT AA.ID_ANAGRAFICA_AZIENDA, " +
      "       AA.ID_AZIENDA, "+
      "       AA.DATA_INIZIO_VALIDITA, " +
      "       AA.DATA_FINE_VALIDITA, "+
      "       AA.CUAA ANAG_CUAA, " +
      "       AA.PARTITA_IVA, AA.DENOMINAZIONE ANAG_DENOMINAZIONE,  "+
      "       AA.ID_FORMA_GIURIDICA ALIAS_FORMA_GIURIDICA, "+
      "       AA.ID_TIPOLOGIA_AZIENDA ALIAS_TIPOLOGIA_AZIENDA, "+
      "       AA.ID_ATTIVITA_ATECO ALIAS_ATTIVITA_ATECO, "+
      "       AA.ID_ATTIVITA_OTE ALIAS_ATTIVITA_OTE, "+
      "       AA.ID_UDE, " +
      "       AA.ID_DIMENSIONE_AZIENDA, " +
      "       AA.RLS, " +
      "       AA.ULU AS ULU, " +
      "       TU.CLASSE_UDE, " +
      "       TDA.DESCRIZIONE AS DES_DIM_AZIENDA, " +
      "       AA.ESONERO_PAGAMENTO_GF, " +
      "       TFG.DESCRIZIONE ALIAS_DESC_TFG, "+
      "       TTA.DESCRIZIONE ALIAS_DESC_TTA, "+
      "       TAT.DESCRIZIONE ALIAS_DESC_TAT, "+
      "       TOT.DESCRIZIONE ALIAS_DESC_TOT, "+
      "       TAT.CODICE ALIAS_CODICE_TAT, " +
      "       TOT.CODICE ALIAS_CODICE_TOT, "+
      "       AA.PROVINCIA_COMPETENZA, " +
      "       P2.SIGLA_PROVINCIA ALIAS_PROV_COMPETENZA, "+
      "       AA.CCIAA_PROVINCIA_REA, " +
      "       AA.CCIAA_NUMERO_REA, "+
      "       AA.CCIAA_NUMERO_REGISTRO_IMPRESE, " +
      "       AA.CCIAA_ANNO_ISCRIZIONE, "+
      "       P1.ISTAT_PROVINCIA ALIAS_ISTAT_PROV, "+
      "       P1.SIGLA_PROVINCIA ALIAS_PROV, " +
      "       AA.SEDELEG_COMUNE, " +
      "       COMUNE.FLAG_ESTERO ALIAS_SEDELE_ESTERO, "+
      "       COMUNE.DESCOM ALIAS_DESCOM, " +
      "       AA.SEDELEG_INDIRIZZO, " +
      "       AA.SEDELEG_CAP, "+
      "       AA.SEDELEG_CITTA_ESTERO, "+
      "       AA.MAIL ANAG_MAIL, " +
      "       AA.SITOWEB, " +
      "       AA.NOTE, " +
      "       AA.DATA_CESSAZIONE, "+
      "       AA.CAUSALE_CESSAZIONE, " +
      "       AA.DATA_AGGIORNAMENTO, "+
      "       AA.ID_UTENTE_AGGIORNAMENTO, " +
      "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
      "          || ' ' " + 
      "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
      "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
      "         WHERE AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
      "       AS UT_DENOMINAZIONE, " +
      "       (SELECT PVU.DENOMINAZIONE " +
      "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
      "        WHERE AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
      "       AS UT_ENTE_APPART, "+
      "       P2.DESCRIZIONE ALIAS_DESC_PROV_COMPETENZA, "+
      "       AA.NUMERO_AGEA, " +
      "       A.FASCICOLO_DEMATERIALIZZATO, "+
      "       A.FLAG_AZIENDA_PROVVISORIA, "+
      "       A.ID_AZIENDA_PROVENIENZA, "+
      "       A.VARIAZIONE_UTILIZZI_AMMESSA, " +
      "       AA.INTESTAZIONE_PARTITA_IVA, " +
      "       AA.TELEFONO, " +
      "       AA.FAX, " +
      "       AA.PEC, " +
      "       AA.CODICE_AGRITURISMO, " +
      "       AA.DATA_AGGIORNAMENTO_UMA, " +
      "       AA.FLAG_IAP, " +
      "       AA.DATA_ISCRIZIONE_REA, " +
      "       AA.DATA_CESSAZIONE_REA, " +
      "       AA.DATA_ISCRIZIONE_RI, " +
      "       AA.DATA_CESSAZIONE_RI, " +
      "       AA.DATA_INIZIO_ATECO " +
      "FROM   DB_ANAGRAFICA_AZIENDA AA, "+
      "       DB_TIPO_FORMA_GIURIDICA TFG, "+
      "       DB_TIPO_TIPOLOGIA_AZIENDA TTA, "+
      "       DB_TIPO_ATTIVITA_ATECO TAT, "+
      "       DB_TIPO_ATTIVITA_OTE TOT, "+
      "       COMUNE, " +
      "       PROVINCIA P1, " +
      "       PROVINCIA P2, " +
      //"       PAPUA_V_UTENTE_LOGIN PVU, "+
      "       DB_AZIENDA A, " +
      "       DB_TIPO_UDE TU," +
      "       DB_TIPO_DIMENSIONE_AZIENDA TDA " +  
      "WHERE  AA.SEDELEG_COMUNE = COMUNE.ISTAT_COMUNE(+) "+
      "AND    COMUNE.ISTAT_PROVINCIA = P1.ISTAT_PROVINCIA(+) "+
      "AND    AA.PROVINCIA_COMPETENZA = P2.ISTAT_PROVINCIA(+) "+
      "AND    AA.ID_FORMA_GIURIDICA = TFG.ID_FORMA_GIURIDICA(+) "+
      "AND    AA.ID_TIPOLOGIA_AZIENDA = TTA.ID_TIPOLOGIA_AZIENDA(+) "+
      "AND    AA.ID_ATTIVITA_ATECO = TAT.ID_ATTIVITA_ATECO(+) "+
      "AND    AA.ID_ATTIVITA_OTE = TOT.ID_ATTIVITA_OTE(+) "+
      "AND    AA.ID_UDE = TU.ID_UDE(+) " +
      "AND    AA.ID_DIMENSIONE_AZIENDA = TDA.ID_DIMENSIONE_AZIENDA(+) " +   
      //"AND    AA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN "+
      "AND    AA.DATA_FINE_VALIDITA IS NULL ";
     if(flagDataCessazione)
     {
       query += "AND AA.DATA_CESSAZIONE IS NULL ";
     }
     
     if(!flagAziendaProvvisoria)
     {
       query += "AND (A.FLAG_AZIENDA_PROVVISORIA = 'N' " +
       		"OR A.FLAG_AZIENDA_PROVVISORIA IS NULL) ";
     }
     else
     {
       query += "AND A.FLAG_AZIENDA_PROVVISORIA = 'S' ";
     }
        
     query += "AND AA.CUAA = UPPER(?) "+
        "AND A.ID_AZIENDA = AA.ID_AZIENDA " +
        "ORDER BY AA.DATA_INIZIO_VALIDITA DESC ";

      stmt = conn.prepareStatement(query);

      stmt.setString(1, cuaa);

      SolmrLogger.debug(this, "Executing query: "+query);

      ResultSet rs = stmt.executeQuery();

      while(rs.next())
      {
        AnagAziendaVO aziendaTempt = new AnagAziendaVO();

        aziendaTempt.setIntestazionePartitaIva(rs.getString("INTESTAZIONE_PARTITA_IVA"));
        aziendaTempt.setIdAnagAzienda(new Long(rs.getLong("ID_ANAGRAFICA_AZIENDA")));
        aziendaTempt.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        aziendaTempt.setDataInizioVal(rs.getDate("DATA_INIZIO_VALIDITA"));
        aziendaTempt.setDataFineVal(rs.getDate("DATA_FINE_VALIDITA"));
        aziendaTempt.setCUAA(rs.getString("ANAG_CUAA"));
        aziendaTempt.setOldCUAA(rs.getString("ANAG_CUAA"));
        aziendaTempt.setPartitaIVA(rs.getString("PARTITA_IVA"));
        aziendaTempt.setDenominazione(rs.getString("ANAG_DENOMINAZIONE"));
        aziendaTempt.setIdUde(checkLongNull(rs.getString("ID_UDE")));
        aziendaTempt.setIdDimensioneAzienda(checkLongNull(rs.getString("ID_DIMENSIONE_AZIENDA")));
        aziendaTempt.setRls(rs.getBigDecimal("RLS"));
        aziendaTempt.setUlu(rs.getBigDecimal("ULU"));
        aziendaTempt.setClasseUde(checkLongNull(rs.getString("CLASSE_UDE")));
        aziendaTempt.setDescDimensioneAzienda(rs.getString("DES_DIM_AZIENDA"));
        aziendaTempt.setEsoneroPagamentoGF(rs.getString("ESONERO_PAGAMENTO_GF"));
        aziendaTempt.setDataAggiornamentoUma(rs.getTimestamp("DATA_AGGIORNAMENTO_UMA"));
        aziendaTempt.setFlagIap(rs.getString("FLAG_IAP"));
        aziendaTempt.setDataIscrizioneRea(rs.getTimestamp("DATA_ISCRIZIONE_REA"));
        aziendaTempt.setDataCessazioneRea(rs.getTimestamp("DATA_CESSAZIONE_REA"));
        aziendaTempt.setDataIscrizioneRi(rs.getTimestamp("DATA_ISCRIZIONE_RI"));
        aziendaTempt.setDataCessazioneRi(rs.getTimestamp("DATA_CESSAZIONE_RI"));
        aziendaTempt.setDataInizioAteco(rs.getTimestamp("DATA_INIZIO_ATECO"));

        String codTipoFormaGiur = rs.getString("ALIAS_FORMA_GIURIDICA");
        if(codTipoFormaGiur!=null){
          aziendaTempt.setTipoFormaGiuridica(new CodeDescription(
              new Integer(codTipoFormaGiur),
              rs.getString("ALIAS_DESC_TFG")));
        }
        else{
          aziendaTempt.setTipoFormaGiuridica(new CodeDescription());
        }

        aziendaTempt.setTipiFormaGiuridica(rs.getString("ALIAS_DESC_TFG"));


        String codTipolAzienda = rs.getString("ALIAS_TIPOLOGIA_AZIENDA");
        if(codTipolAzienda!=null){
          aziendaTempt.setTipoTipologiaAzienda(new CodeDescription(
              new Integer(codTipolAzienda),
              rs.getString("ALIAS_DESC_TTA")));
          aziendaTempt.setTipiAzienda(new Integer(codTipolAzienda).toString());
        }
        else{
          aziendaTempt.setTipoTipologiaAzienda(new CodeDescription());
          aziendaTempt.setTipiAzienda("");
        }
        aziendaTempt.setTipiAzienda(rs.getString("ALIAS_TIPOLOGIA_AZIENDA"));
        String codATECO = rs.getString("ALIAS_ATTIVITA_ATECO");
        if(codATECO!=null){
          CodeDescription codeATECO = new CodeDescription(new Integer(codATECO),
              rs.getString("ALIAS_DESC_TAT"));
          codeATECO.setSecondaryCode(rs.getString("ALIAS_CODICE_TAT"));
          aziendaTempt.setTipoAttivitaATECO(codeATECO);
        }
        else
          aziendaTempt.setTipoAttivitaATECO(new CodeDescription());

        String codOTE = rs.getString("ALIAS_ATTIVITA_OTE");
        if(codOTE!=null){
          CodeDescription codeOTE = new CodeDescription(new Integer(codOTE),
              rs.getString("ALIAS_DESC_TOT"));
          codeOTE.setSecondaryCode(rs.getString("ALIAS_CODICE_TOT"));
          aziendaTempt.setTipoAttivitaOTE(codeOTE);
        }
        else
          aziendaTempt.setTipoAttivitaOTE(new CodeDescription());

        aziendaTempt.setProvCompetenza(rs.getString("PROVINCIA_COMPETENZA"));
        aziendaTempt.setProvincePiemonte(rs.getString("PROVINCIA_COMPETENZA"));
        aziendaTempt.setSiglaProvCompetenza(rs.getString("ALIAS_PROV_COMPETENZA"));
        aziendaTempt.setCCIAAprovREA(rs.getString("CCIAA_PROVINCIA_REA"));
        String numeroREA = rs.getString("CCIAA_NUMERO_REA");
        if(numeroREA!=null)
          aziendaTempt.setCCIAAnumeroREA(new Long(numeroREA));
        aziendaTempt.setCCIAAnumRegImprese(rs.getString("CCIAA_NUMERO_REGISTRO_IMPRESE"));
        aziendaTempt.setCCIAAannoIscrizione(rs.getString("CCIAA_ANNO_ISCRIZIONE"));
        aziendaTempt.setStrCCIAAnumeroREA(rs.getString("CCIAA_NUMERO_REA"));
        aziendaTempt.setSedelegComune(rs.getString("SEDELEG_COMUNE"));
        String sedelegDescComune = rs.getString("ALIAS_DESCOM");

        String flagEstero = rs.getString("ALIAS_SEDELE_ESTERO");
        aziendaTempt.setDescrizioneProvCompetenza(rs.getString("ALIAS_DESC_PROV_COMPETENZA"));

        if(flagEstero==null || flagEstero.equals("")){
          aziendaTempt.setSedelegComune("");
          aziendaTempt.setSedelegEstero("");
          aziendaTempt.setStatoEstero("");
          aziendaTempt.setSedelegCittaEstero("");
          aziendaTempt.setSedelegIstatProv("");
          aziendaTempt.setSedelegProv("");
          aziendaTempt.setDescComune("");
          aziendaTempt.setSedelegCAP("");
        }
        else if(flagEstero.equals(SolmrConstants.FLAG_S)){
          aziendaTempt.setSedelegEstero(sedelegDescComune);
          aziendaTempt.setStatoEstero(sedelegDescComune);
          aziendaTempt.setSedelegCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
          aziendaTempt.setSedelegIstatProv("");
          aziendaTempt.setSedelegProv("");
          aziendaTempt.setDescComune("");
          aziendaTempt.setSedelegCAP("");
        }
        else{
          aziendaTempt.setSedelegEstero("");
          aziendaTempt.setSedelegCittaEstero("");
          aziendaTempt.setSedelegIstatProv(rs.getString("ALIAS_ISTAT_PROV"));
          aziendaTempt.setSedelegProv(rs.getString("ALIAS_PROV"));
          aziendaTempt.setDescComune(sedelegDescComune);
          aziendaTempt.setSedelegCAP(rs.getString("SEDELEG_CAP"));
        }
        aziendaTempt.setSedelegIndirizzo(rs.getString("SEDELEG_INDIRIZZO"));

        aziendaTempt.setMail(rs.getString("ANAG_MAIL"));
        aziendaTempt.setSitoWEB(rs.getString("SITOWEB"));
        aziendaTempt.setTelefono(rs.getString("TELEFONO"));
        aziendaTempt.setFax(rs.getString("FAX"));
        aziendaTempt.setPec(rs.getString("PEC"));
        aziendaTempt.setCodiceAgriturismo(rs.getString("CODICE_AGRITURISMO"));
        aziendaTempt.setNote(rs.getString("NOTE"));
        aziendaTempt.setDataCessazione(rs.getDate("DATA_CESSAZIONE"));
        aziendaTempt.setCausaleCessazione(rs.getString("CAUSALE_CESSAZIONE"));
        java.util.Date dataAgg = rs.getDate("DATA_AGGIORNAMENTO");
        aziendaTempt.setDataAggiornamento(dataAgg);
        aziendaTempt.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        String motivoModifica = rs.getString("MOTIVO_MODIFICA");
        String utDenominazione = rs.getString("UT_DENOMINAZIONE");
        String utEnteAppart = rs.getString("UT_ENTE_APPART");

        String ultimaModifica = DateUtils.formatDate(dataAgg);
        String tmp="";
        if(utDenominazione!=null && !utDenominazione.equals("")){
          if(tmp.equals(""))
            tmp+=" (";
          tmp+=utDenominazione;
        }
        if(utEnteAppart!=null && !utEnteAppart.equals("")){
          if(tmp.equals(""))
            tmp+=" (";
          else
            tmp+=" - ";
          tmp+=utEnteAppart;
        }
        if(motivoModifica!=null && !motivoModifica.equals("")){
          if(tmp.equals(""))
            tmp+=" (";
          else
            tmp+=" - ";
          tmp+=motivoModifica;
        }
        if(!tmp.equals(""))
          tmp+=")";
        ultimaModifica+=tmp;
        aziendaTempt.setUltimaModifica(ultimaModifica);

        //Ultima Modifica Spezzata
        aziendaTempt.setDataUltimaModifica(dataAgg);
        aziendaTempt.setUtenteUltimaModifica(utDenominazione);
        aziendaTempt.setEnteUltimaModifica(utEnteAppart);
        aziendaTempt.setMotivoModifica(motivoModifica);

        aziendaTempt.setPosizioneSchedario(rs.getString("NUMERO_AGEA"));
        aziendaTempt.setVariazioneUtilizziAmmessa(rs.getString("VARIAZIONE_UTILIZZI_AMMESSA"));

        aziendaTempt.setFascicoloDematerializzato(rs.getString("FASCICOLO_DEMATERIALIZZATO"));
        if(result == null)
        {
          result = new Vector<AnagAziendaVO>();
        }
        result.add(aziendaTempt);
      }

      rs.close();
      stmt.close();

    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "getAziendaByCriterioCessataAndProvvisoria - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "getAziendaByCriterio - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    } finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "getAziendaByCriterioCessataAndProvvisoria - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "getAziendaByCriterio - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  /**
  *
  * @param anagAziendaVO AnagAziendaVO
  * @param dataSituazioneAl Date
  * @param attivitaBool boolean
  * @param provvisorio se true prende solo le aziende provvisorie, 
  * non provvisorie se null tutte
  * 
  * @return java.util.Vector
  * @throws DataAccessException
  * @throws DataControlException
  */
 public Vector<Long> getListIdAziendeFlagProvvisorio(AnagAziendaVO anagAziendaVO,
     java.util.Date dataSituazioneAl,
     boolean attivitaBool,
     boolean provvisorio)
 throws DataAccessException, DataControlException
 {

   Vector<Long> result = new Vector<Long>();
   Connection conn = null;
   PreparedStatement stmt = null;
   try {
     conn = getDatasource().getConnection();

     

     
     String  search = "SELECT AA.ID_AZIENDA AS ID_AZIENDA"+
       "  FROM DB_ANAGRAFICA_AZIENDA AA, DB_AZIENDA DBA, COMUNE C, PROVINCIA P"+
       " WHERE AA.SEDELEG_COMUNE = C.ISTAT_COMUNE(+) "+
       "   AND AA.ID_AZIENDA = DBA.ID_AZIENDA " +
       "   AND C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA(+) ";


     if (provvisorio)
     {
       search+=" AND DBA.FLAG_AZIENDA_PROVVISORIA = 'S' ";
     }
     else
     {
       search+=" AND (DBA.FLAG_AZIENDA_PROVVISORIA = 'N' OR DBA.FLAG_AZIENDA_PROVVISORIA IS NULL )";
     }
     /**
      * Utilizzato dai servizi di anag
      * */
     if (dataSituazioneAl==null)
     {
       search+="   AND AA.DATA_FINE_VALIDITA IS NULL ";
     }
     else
     {
       search+="   AND AA.DATA_INIZIO_VALIDITA  <= ? "+
       "   AND (AA.DATA_FINE_VALIDITA IS NULL "+
       "       OR AA.DATA_FINE_VALIDITA >= ?)";
     }

     Vector<String> altriParamVector = new Vector<String>();


     /************************************************************************
      ************************************************************************
      ************************************************************************
      * Ho aggiunto il .toUpperCase perchè dato che questo metodo
      * verrà utilizzato come servizio non posso obblicìgare i client a
      * passarmi i parametri della ricerca maiuscoli. Non ho creato dei metodi
      * ad hoc sul VO per non appesantirlo ulteriormente
      ************************************************************************
      ************************************************************************
      ************************************************************************
      */



     if (anagAziendaVO.getPartitaIVA()!= null && !"".equals(anagAziendaVO.getPartitaIVA()))
     {
       search+=" AND AA.PARTITA_IVA = ? ";
       altriParamVector.addElement(anagAziendaVO.getPartitaIVA());
     }
     if (anagAziendaVO.getCUAA()!= null && !"".equals(anagAziendaVO.getCUAA()))
     {
       search+=" AND AA.CUAA = ? ";
       altriParamVector.addElement(anagAziendaVO.getCUAA().toUpperCase());
     }
     if (anagAziendaVO.isDenominazioneInitialized())
     {
       //search+=" AND UPPER(DENOMINAZIONE) LIKE UPPER('" + anagAziendaVO.getDenominazione() + "%') ";
       search+=" AND AA.DENOMINAZIONE LIKE UPPER(?) ";
       //stmt.setString(iParam, anagAziendaVO.getDenominazione()+"%");
       altriParamVector.addElement(anagAziendaVO.getDenominazione().trim().toUpperCase()+"%");
     }
     if (anagAziendaVO.isSedelegProvInitialized()){
       //search+=" AND SIGLA_PROVINCIA = '" + anagAziendaVO.getSedelegProv()+"' ";
       search+=" AND P.SIGLA_PROVINCIA = UPPER(?) ";
       //stmt.setString(iParam, anagAziendaVO.getSedelegProv());
       altriParamVector.addElement(anagAziendaVO.getSedelegProv().toUpperCase());
     }
     /**
      * Ricerca puntuale sull'istat del comune
      */
     if (anagAziendaVO.getSedelegComune()!= null && !"".equals(anagAziendaVO.getSedelegComune()))
     {
       search+=" AND AA.SEDELEG_COMUNE = ? ";
       altriParamVector.addElement(anagAziendaVO.getSedelegComune());
     }
     /**
      * Ricerca sulla descrizione del comune
      */
     if (anagAziendaVO.isDescComuneInitialized()){
       //search+=" AND UPPER(DESCOM) = UPPER('" + anagAziendaVO.getDescComune() + "')";
       search+=" AND C.DESCOM = UPPER(?)";
       //stmt.setString(iParam, anagAziendaVO.getDescComune());
       altriParamVector.addElement(anagAziendaVO.getDescComune().toUpperCase());
     }
     if (anagAziendaVO.isTipoFormaGiuridicaInitialized())
       search+=" AND AA.ID_FORMA_GIURIDICA = " + anagAziendaVO.getTipoFormaGiuridica().getCode().toString();
     if (anagAziendaVO.isTipoAziendaInitialized())
       search+=" AND AA.ID_TIPOLOGIA_AZIENDA = " + anagAziendaVO.getTipiAzienda();

     if(attivitaBool)
       search+= " AND AA.DATA_CESSAZIONE IS NULL ";

     if (anagAziendaVO.getPosizioneSchedario()!= null && !"".equals(anagAziendaVO.getPosizioneSchedario()))
     {
       search+=" AND AA.NUMERO_AGEA = ? ";
       altriParamVector.addElement(anagAziendaVO.getPosizioneSchedario());
     }

     search += " ORDER BY AA.DENOMINAZIONE ";
     stmt = conn.prepareStatement(search);

     int indice = 0;
     
     if (dataSituazioneAl!=null)
     {
       stmt.setDate(++indice, new java.sql.Date(dataSituazioneAl.getTime()));
       stmt.setDate(++indice, new java.sql.Date(dataSituazioneAl.getTime()));
       for(int i=0; i<altriParamVector.size(); i++)
         stmt.setString(++indice, (String)altriParamVector.elementAt(i));
     }
     else
     {
       for(int i=0; i<altriParamVector.size(); i++)
         stmt.setString(++indice, (String)altriParamVector.elementAt(i));
     }
     SolmrLogger.debug(this, "Executing query: "+search);

     ResultSet rs = stmt.executeQuery();

     while (rs.next()) {
       result.add(new Long(rs.getLong(1)));
       if(rs.getRow()>SolmrConstants.NUM_MAX_ROWS_RESULT)
         throw new DataControlException(ErrorTypes.STR_MAX_RECORD);
     }

     rs.close();
     stmt.close();

     SolmrLogger.debug(this, "Executed query - Found "+result.size()+" result(s).");
   } catch (SQLException exc) {
     SolmrLogger.fatal(this, "getListIdAziendeFlagProvvisorio - SQLException: "+exc.getMessage());
     throw new DataAccessException(exc.getMessage());
   } catch (DataControlException exc) {
     SolmrLogger.fatal(this, "getListIdAziendeFlagProvvisorio - DataControlException: "+exc.getMessage());
     throw exc;
   } catch (Exception ex) {
     SolmrLogger.fatal(this, "getListIdAziendeFlagProvvisorio - Generic Exception: "+ex+" with message: "+ex.getMessage());
     throw new DataAccessException(ex.getMessage());
   } finally {
     try {
       if (stmt != null) stmt.close();
       if (conn != null) conn.close();
     } catch (SQLException exc) {
       SolmrLogger.fatal(this, "getListIdAziendeFlagProvvisorio - SQLException while closing Statement and Connection: "+exc.getMessage());
       throw new DataAccessException(exc.getMessage());
     } catch (Exception ex) {
       SolmrLogger.fatal(this, "getListIdAziendeFlagProvvisorio - Generic Exception while closing Statement and Connection: "+ex.getMessage());
       throw new DataAccessException(ex.getMessage());
     }
   }
   return result;
 }
 
 /**
  * Restituisce true se fa parte di un gruppo di soci anche se storicizzati
  * 
  * @param idAzienda
  * @return
  * @throws DataAccessException
  * @throws SolmrException
  */
 public boolean hasEntiAppartenenza(long idAzienda)
   throws DataAccessException, SolmrException {

   boolean result = false;
   Connection conn = null;
   PreparedStatement stmt = null;
   try 
   {
     conn = getDatasource().getConnection();

     String query =   
       " SELECT * " +
       " FROM DB_AZIENDA_COLLEGATA "+
       " WHERE " +
       "   ID_AZIENDA_ASSOCIATA = ? ";
         

     SolmrLogger.debug(this, "Executing query: "+query);

     stmt = conn.prepareStatement(query);


     stmt.setLong(1, idAzienda);
     
     ResultSet rs = stmt.executeQuery();


     if(rs.next()) 
     {
       result = true;
     }
    
     rs.close();
     stmt.close();

   } catch (SQLException exc) {
     SolmrLogger.fatal(this, "hasEntiAppartenenza - SQLException: "+exc.getMessage());
     throw new DataAccessException(exc.getMessage());
   } catch (Exception ex) {
     SolmrLogger.fatal(this, "hasEntiAppartenenza - Generic Exception: "+ex.getMessage());
     throw new DataAccessException(ex.getMessage());
   }
   finally
   {
     try
     {
       if (stmt != null) stmt.close();
       if (conn != null) conn.close();
     } catch (SQLException exc) {
       SolmrLogger.fatal(this, "hasEntiAppartenenza - SQLException while closing Statement and Connection: "+exc.getMessage());
       throw new DataAccessException(exc.getMessage());
     } catch (Exception ex) {
       SolmrLogger.fatal(this, "hasEntiAppartenenza - Generic Exception while closing Statement and Connection: "+ex.getMessage());
       throw new DataAccessException(ex.getMessage());
     }
   }
   return result;
 }
 
 /**
  * Restituisce tutti i cuaa collegati al cuaa passato
  * @param cuaa String  parametro sui cui si effettua la ricerca
  * @param cessata boolean se vale true verranno restituite anche le aziende cessate
  * @throws DataAccessException
  * @return String[]
  */
 public String[] getCUAACollegati(String cuaa)
   throws DataAccessException
 {
   Connection conn = null;
   PreparedStatement stmt = null;
   ResultSet rs = null;
   String query = null;
   Vector<String> collegati = new Vector<String>();
   try
   {
     SolmrLogger.debug(this,
         "[AnagraficaDAO::getCUAACollegati] BEGIN.");

     conn = getDatasource().getConnection();
     query="SELECT CUAA "+
           "FROM  TABLE(Pck_Utl_Obj.TABLE_CUAA_COLL_JAVA(?)) ";

     stmt = conn.prepareStatement(query);

     stmt.setString(1,cuaa);


     rs = stmt.executeQuery();

     while (rs.next())
     {
       String temp=rs.getString("CUAA");
       collegati.add(temp);
     }

     return collegati.size()==0?null:(String[])collegati.toArray(new String[0]);
   }
   catch (Throwable t)
   {
     Parametro parametri[] =
     { new Parametro("cuaa", cuaa)};
     Variabile variabili[] =
     { new Variabile("query", query),
       new Variabile("collegati", collegati)};
     // Logging dell'eccezione, query, variabili e parametri del metodo
     LoggerUtils.logDAOError(this,
         "[AnagraficaDAO::getCUAACollegati] ", t,
         query, variabili, parametri);
     
     throw new DataAccessException(t.getMessage());
   }
   finally
   {
     /*
      * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
      * ignora ogni eventuale eccezione)
      */
     close(null,stmt,conn);

     SolmrLogger.debug(this,
         "[AnagraficaDAO::getCUAACollegati] END.");
   }
 }
  
 
 /**
  * Restituisce tutti gli ID_ANAGRAFICA_AZIENDA collegati al cuaa passato
  * @param cuaa String  parametro sui cui si effettua la ricerca
  * @throws DataAccessException
  * @return Vector
  */
 public Vector<Long> getIdAnagAziendeCollegatebyCUAA(String cuaa)
   throws DataAccessException
 {
   Connection conn = null;
   PreparedStatement stmt = null;
   ResultSet rs = null;
   String query = null;
   Vector<Long> idAnagraficaAzienda = new Vector<Long>();
   try
   {
     SolmrLogger.debug(this,
         "[AnagraficaDAO::getIdAnagAziendeCollegatebyCUAA] BEGIN.");

     conn = getDatasource().getConnection();
     query="SELECT  ID_ANAGRAFICA_AZIENDA "+
         "FROM  TABLE(Pck_Utl_Obj.TABLE_CUAA_AZIENDE_COLLEGATE_J(?)) ";
         //"WHERE DATA_CESSAZIONE IS NULL ";

     SolmrLogger.debug(this,"-- cuaa ="+cuaa);
     SolmrLogger.debug(this,"getIdAnagAziendeCollegatebyCUAA ="+query);
     stmt = conn.prepareStatement(query);

     stmt.setString(1,cuaa);


     rs = stmt.executeQuery();

     while (rs.next())
     {
       String temp=rs.getString("ID_ANAGRAFICA_AZIENDA");
       if (temp!=null) idAnagraficaAzienda.add(new Long(temp));
     }

     return idAnagraficaAzienda;
   }
   catch (Throwable t)
   {
     Parametro parametri[] =
     { new Parametro("cuaa", cuaa)};
     Variabile variabili[] =
     { new Variabile("query", query),
       new Variabile("idAnagraficaAzienda", idAnagraficaAzienda)};
     // Logging dell'eccezione, query, variabili e parametri del metodo
     LoggerUtils.logDAOError(this,
         "[AnagraficaDAO::getIdAnagAziendeCollegatebyCUAA] ", t,
         query, variabili, parametri);
     
     throw new DataAccessException(t.getMessage());
   }
   finally
   {
     /*
      * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
      * ignora ogni eventuale eccezione)
      */
     close(rs,stmt,conn);

     SolmrLogger.debug(this,
         "[AnagraficaDAO::getIdAnagAziendeCollegatebyCUAA] END.");
   }
 }
 
 
  /**
  * ritorna true se ha trovato un record (ha fatto il lock!!)
  * 
  * @param long[] idAzienda
  * @return
  * @throws DataAccessException
  */
  public boolean lockTableAnagraficaAzienda(long[] idAzienda) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean flagLock = false;
    ResultSet rs =null; 
     
    String strIN =" (";
   
    if (idAzienda!=null && idAzienda.length!=0)
    {
      for(int i=0;i<idAzienda.length;i++)
      {
        strIN+="?";
        if (i<idAzienda.length-1)
           strIN+=",";
      }
    }
    strIN+=") ";
   
    try
    {
      SolmrLogger
        .debug(this,
            "[AnagrafeDAO::lockTableAnagraficaAzienda] BEGIN.");
   
     /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
   
      queryBuf = new StringBuffer();
      queryBuf
          .append(" SELECT * " +
        "FROM DB_ANAGRAFICA_AZIENDA " +
        "WHERE ID_AZIENDA IN " + strIN +
        "AND DATA_FINE_VALIDITA IS NULL " +
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
            "[AnagrafeDAO::lockTableAnagraficaAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
       
      int indice = 0;
       
      if (idAzienda!=null && idAzienda.length!=0)
      {
        for(int i=0;i<idAzienda.length;i++)
          stmt.setLong(++indice,idAzienda[i]);
      }       
   
      // Setto i parametri della query
      rs = stmt.executeQuery();
     
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
          { new Parametro("idAzienda", idAzienda)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
         .logDAOError(
           this,
           "[AnagrafeDAO::lockTableAnagraficaAzienda] ",
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
       close(rs, stmt, conn);
   
       // Fine metodo
       SolmrLogger
         .debug(this,
           "[AnagrafeDAO::lockTableAnagraficaAzienda] END.");
    }
  }
  
  
  public void updateAziendaCollegataCessazione(long idAzienda, long idUtenteAggiornamento, boolean padre) throws DataAccessException {
    SolmrLogger.debug(this, "Invocating updateAziendaCollegataCessazione method in AnagrafeDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in updateAziendaCollegataCessazione method in AnagrafeDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in updateAziendaCollegataCessazione method in AnagrafeDAO and it values: "+conn+"\n");
      
      String query = " " +
      		"UPDATE DB_AZIENDA_COLLEGATA " +
          "SET DATA_USCITA = ?, " +
          "DATA_FINE_VALIDITA = ?, " +
          "ID_UTENTE_AGGIORNAMENTO = ? " +
          "WHERE NVL(TRUNC(DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > SYSDATE ";
      
      if(padre)
      {
        query += "AND ID_AZIENDA = ? "; 
      }
      else
      {
        query += "AND ID_AZIENDA_ASSOCIATA = ? "; 
      }
        
                         
                    

      conn = getDatasource().getConnection();

      stmt = conn.prepareStatement(query);
      
      stmt.setDate(1, checkDate(new Date()));
      stmt.setDate(2, checkDate(new Date()));
      
      SolmrLogger.debug(this, "Value of parameter 1 [ID_UTENTE_AGGIORNAMENTO] in updateAziendaCollegataCessazione method in AnagrafeDAO: "+idUtenteAggiornamento+"\n");
      stmt.setLong(3, idUtenteAggiornamento);
      
      if(padre)
      {
        SolmrLogger.debug(this, "Value of parameter 2 [ID_AZIENDA] in updateAziendaCollegataCessazione method in AnagrafeDAO: "+idAzienda+"\n");
        stmt.setLong(4, idAzienda);
      }
      else
      {      
        SolmrLogger.debug(this, "Value of parameter 2 [ID_AZIENDA_ASSOCIATA] in updateAziendaCollegataCessazione method in AnagrafeDAO: "+idAzienda+"\n");
        stmt.setLong(4, idAzienda);
      }
      

      stmt.executeUpdate();

      stmt.close();
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "updateAziendaCollegataCessazione in AnagrafeDAO - SQLException: "+exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.error(this, "updateAziendaCollegataCessazione in AnagrafeDAO - Generic Exception: "+ex);
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
        SolmrLogger.error(this, "updateAziendaCollegataCessazione in AnagrafeDAO - SQLException while closing Statement and Connection: "+exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "updateAziendaCollegataCessazione in AnagrafeDAO - Generic Exception while closing Statement and Connection: "+ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated updateAziendaCollegataCessazione method in AnagrafeDAO\n");
  }

  public CodeDescription[] getListAtecoInAnag(Long[] idAteco, Long idAzienda) throws DataAccessException
  {

	  String query = null;
	  Connection conn = null;
	  PreparedStatement stmt = null;
	  StringBuffer queryBuf = null;
	  Vector<CodeDescription> result = new Vector<CodeDescription>();
	  ResultSet resultSet =null;
	  
	  try
	  {
	    SolmrLogger.debug(this,"[AnagrafeDAO::getListAtecoInAnag] BEGIN.");
	
	    /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
	
	    queryBuf = new StringBuffer();
	    queryBuf.append(" SELECT ANAG.ID_ATTIVITA_ATECO as ID_ATTIVITA_ATECO, " +
	    			" TIPO.CODICE as CODICE, " +
	    			" TIPO.DESCRIZIONE as DESCRIZIONE " +
	        		" FROM  DB_ANAGRAFICA_AZIENDA ANAG, DB_TIPO_ATTIVITA_ATECO TIPO " + 
	        		" WHERE ANAG.ID_ATTIVITA_ATECO = TIPO.ID_ATTIVITA_ATECO " +
	        		" AND ANAG.ID_ATTIVITA_ATECO IS NOT NULL " +
	        		" AND ANAG.ID_ATTIVITA_ATECO in (? ");
	    
	    int atecoDim = idAteco.length;
	    int atecoCount = 1;
	    for(int i=1;i<atecoDim;i++)
	    	if(idAteco[i]!=null){
	    		queryBuf.append(",?");
	    		atecoCount++;
	    	}
	    queryBuf.append(")");
	    queryBuf.append(" AND ANAG.ID_AZIENDA = ? " +
						" AND ANAG.DATA_FINE_VALIDITA IS NULL " +
						" AND DATA_CESSAZIONE IS NULL ");
	    
	    query = queryBuf.toString();
	    /* CONCATENAZIONE/CREAZIONE QUERY END. */
	
	    conn = getDatasource().getConnection();
	    if (SolmrLogger.isDebugEnabled(this))
	    {
	      // Dato che la query costruita dinamicamente è un dato importante la
	      // registro sul file di log se il
	      // debug è abilitato
	
	      SolmrLogger.debug(this,"[AnagrafeDAO::getListAtecoInAnag] Query="+ query);
	    }
	    stmt = conn.prepareStatement(query);
	    
	    int i=0;
	    for(int k=0;k<atecoCount;k++)
	    	stmt.setLong(++i,idAteco[k]);
	    
	    stmt.setLong(++i,idAzienda);
	    
        resultSet = stmt.executeQuery();

        while (resultSet.next())
        {
            CodeDescription cd = new CodeDescription();
            
            cd.setCode(new Integer(resultSet.getInt("ID_ATTIVITA_ATECO")));
            cd.setSecondaryCode(resultSet.getString("CODICE"));
            cd.setDescription(resultSet.getString("DESCRIZIONE"));
      
            result.add(cd);
        }
	  }

	  catch (Throwable t)
	  {
	    // Vettore di variabili interne del metodo
	    Variabile variabili[] = new Variabile[]
	    { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
	        new Variabile("result", result) };
	
	    // Vettore di parametri passati al metodo
	    Parametro parametri[] = new Parametro[]
	    { new Parametro("idAteco", idAteco) };
	    // Logging dell'eccezione, query, variabili e parametri del metodo
	    LoggerUtils.logDAOError(this,"[AnagrafeDAO::getListAtecoInAnag] ",t, query, variabili, parametri);
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
	    close(resultSet, stmt, conn);
	
	    // Fine metodo
	    SolmrLogger.debug(this,"[AnagrafeDAO::getListAtecoInAnag] END.");
	  }
	  return result.size() == 0 ? null : (CodeDescription[]) result.toArray(new CodeDescription[0]);
  	}
  
	public Vector<AnagAziendaVO> getAssociazioniCollegateByIdAzienda(Long idAzienda, Date dataFineValidita)
	throws DataAccessException, NotFoundException, SolmrException
	{

		Vector<AnagAziendaVO> result = new Vector<AnagAziendaVO>();
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getDatasource().getConnection();

			String search = "  SELECT ANAG.CUAA AS CUAA, ANAG.PARTITA_IVA AS PARTITA_IVA, " +
							"  ANAG.DENOMINAZIONE AS DENOMINAZIONE, COM.DESCOM AS DESCOM, "+
							"  COM.FLAG_ESTERO AS FLAG_ESTERO, ANAG.SEDELEG_CITTA_ESTERO AS SEDELEG_CITTA_ESTERO, " +
							"  ANAG.SEDELEG_INDIRIZZO AS SEDELEG_INDIRIZZO, ANAG.SEDELEG_CAP AS SEDELEG_CAP, PROV.SIGLA_PROVINCIA AS PROVINCIA "+
							"  FROM DB_ANAGRAFICA_AZIENDA ANAG, DB_AZIENDA_COLLEGATA COLL, COMUNE COM, PROVINCIA PROV "+
							"  WHERE COLL.ID_AZIENDA_ASSOCIATA = ? "+
							"  AND ANAG.ID_AZIENDA = COLL.ID_AZIENDA "+
							"  AND ANAG.SEDELEG_COMUNE(+) = COM.ISTAT_COMUNE " +
							"  AND ANAG.DATA_FINE_VALIDITA IS NULL " +
							"  AND PROV.ISTAT_PROVINCIA = COM.ISTAT_PROVINCIA ";

			if(dataFineValidita==null )
				search += "  AND COLL.DATA_FINE_VALIDITA IS NULL ";
			else search += "  AND COLL.DATA_INGRESSO <= TO_DATE('"+DateUtils.formatDate(dataFineValidita)+"','dd/MM/yyyy')" +
                           "  AND ANAG.DATA_FINE_VALIDITA <=NVL(COLL.DATA_USCITA, '31/12/9999') ";
			
			stmt = conn.prepareStatement(search);

			stmt.setLong(1, idAzienda);

			SolmrLogger.debug(this, "Executing query: "+search);

			ResultSet rs = stmt.executeQuery();

			while(rs.next())
			{
				AnagAziendaVO aziendaCollegata = new AnagAziendaVO();

				aziendaCollegata.setCUAA(rs.getString("CUAA"));
				aziendaCollegata.setPartitaIVA(rs.getString("PARTITA_IVA"));
				aziendaCollegata.setDenominazione(rs.getString("DENOMINAZIONE"));
				aziendaCollegata.setSedelegComune(rs.getString("DESCOM"));
				String flagEstero = rs.getString("FLAG_ESTERO");
				if(flagEstero.equals(SolmrConstants.FLAG_S))
					aziendaCollegata.setSedelegCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
					
				aziendaCollegata.setSedelegIndirizzo(rs.getString("SEDELEG_INDIRIZZO"));
				aziendaCollegata.setSedelegCAP(rs.getString("SEDELEG_CAP"));
				aziendaCollegata.setSedelegProv(rs.getString("PROVINCIA"));

				result.add(aziendaCollegata);
			}

			rs.close();
			stmt.close();

			//if (result == null)
				//throw new NotFoundException();

		} catch (SQLException exc) {
			SolmrLogger.fatal(this, "getAssociazioniCollegateByIdAzienda - SQLException: "+exc.getMessage());
			throw new DataAccessException(exc.getMessage());
		} /*catch (NotFoundException nfexc) {
			SolmrLogger.fatal(this, "getAssociazioniCollegateByIdAzienda - NotFoundException: "+nfexc.getMessage());
			throw nfexc;
		}*/ catch (Exception ex) {
			SolmrLogger.fatal(this, "getAssociazioniCollegateByIdAzienda - Generic Exception: "+ex.getMessage());
			throw new DataAccessException(ex.getMessage());
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException exc) {
				SolmrLogger.fatal(this, "getAssociazioniCollegateByIdAzienda - SQLException while closing Statement and Connection: "+exc.getMessage());
				throw new DataAccessException(exc.getMessage());
			} catch (Exception ex) {
				SolmrLogger.fatal(this, "getAssociazioniCollegateByIdAzienda - Generic Exception while closing Statement and Connection: "+ex.getMessage());
				throw new DataAccessException(ex.getMessage());
			}
		}
		return result;
	}
	
	
	/**
	 * Verifica se il padre dell'azienda socia ha la delega col caa allora
	 * anche essa può esser modificata dal caa in questione
	 * 
	 * 
	 * @param codFiscIntermediario
	 * @param idAziendaAssociata
	 * @return
	 * @throws DataAccessException
	 */
	public boolean getDelegaBySocio(String codFiscIntermediario, Long idAziendaAssociata) 
	    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    boolean result = false;
    
    try
    {
      SolmrLogger.debug(this, "[AnagrafeDAO::getDelegaBySocio] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append("" +
      	"SELECT DE.ID_AZIENDA " +
        "FROM   DB_INTERMEDIARIO DI, " +
        "       DB_DELEGA DE, " +
        "       DB_AZIENDA_COLLEGATA DAC " +
        "WHERE  DI.ID_INTERMEDIARIO = DE.ID_INTERMEDIARIO " +
        "AND    DE.DATA_FINE IS NULL " +
        "AND    DE.ID_AZIENDA = DAC.ID_AZIENDA " +
        "AND    DAC.ID_AZIENDA_ASSOCIATA = ? " +
        "AND    DAC.DATA_FINE_VALIDITA IS NULL " +
        "AND    NVL(DAC.DATA_USCITA,TO_DATE('31/12/9999','dd/mm/yyyy')) > SYSDATE ");
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      
      String livello = null;
      if (codFiscIntermediario !=null)
      {
        Long idIntermediario = null;
        
        String queryIntermediario= "" +
        		"SELECT ID_INTERMEDIARIO," +
        		"       LIVELLO " +
        		"FROM   DB_INTERMEDIARIO " +
        		"WHERE CODICE_FISCALE = ? ";
        SolmrLogger.debug(this, "Executing query: "+queryIntermediario);
        stmt = conn.prepareStatement(queryIntermediario);
        stmt.setString(1, codFiscIntermediario);
        rs = stmt.executeQuery();
        if(rs.next())
        {
          idIntermediario = new Long(rs.getLong("ID_INTERMEDIARIO"));
          SolmrLogger.debug(this, "idIntermediario: "+idIntermediario);
          livello = rs.getString("LIVELLO");
          SolmrLogger.debug(this, "livello: "+livello);
        }
        rs.close();
        stmt.close();
        
        
        if ("Z".equalsIgnoreCase(livello))
        {
          queryBuf.append(
            "AND DI.CODICE_FISCALE = ? ");
        }
        if ("P".equalsIgnoreCase(livello))
        {
          queryBuf.append(
            "AND SUBSTR(DI.CODICE_FISCALE,1,6) = ? ");
        }
        if ("R".equalsIgnoreCase(livello))
        {
          queryBuf.append(
            "AND SUBSTR(DI.CODICE_FISCALE,1,3) = ? ");
        }       
      }      
      
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
        SolmrLogger.debug(this, "[AnagrafeDAO::getDelegaBySocio] Query="+ query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idAziendaAssociata.longValue());
      if ("Z".equalsIgnoreCase(livello))
      {      
        stmt.setString(++indice, codFiscIntermediario);
      }
      if ("P".equalsIgnoreCase(livello))
      {
        stmt.setString(++indice, codFiscIntermediario.substring(0,6));
      }
      if ("R".equalsIgnoreCase(livello))
      {
        stmt.setString(++indice, codFiscIntermediario.substring(0,3));
      }
      
      rs = stmt.executeQuery();
          
      if(rs.next())
      {
        result = true;
      }
      
      return result;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("codFiscIntermediario", codFiscIntermediario),
        new Parametro("idAziendaAssociata", idAziendaAssociata)  };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[AnagrafeDAO::getDelegaBySocio] ",
              t, query, variabili, parametri);
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
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(rs, stmt, conn);

      // Fine metodo
      SolmrLogger
          .debug(this,
              "[AnagrafeDAO::getDelegaBySocio] END.");
    }
  }
	
	/**
   * Ricavo l'azienda collegata con la coppia di id_azienda e
   * id_azienda_associata.
   * Se dataSituasione == null prendo il record corrente.
   * 
   * 
   * @param idAziendaFather
   * @param idAziendaSon
   * @param dataSituazioneAl
   * @return
   * @throws DataAccessException
   * @throws SolmrException
   */
  public AziendaCollegataVO findAziendaCollegataByFatherAndSon(Long idAziendaFather, Long idAziendaSon,
      Date dataSituazione)
  throws DataAccessException {

    AziendaCollegataVO result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try 
    {
      conn = getDatasource().getConnection();

      String query =   
        "SELECT ID_AZIENDA_COLLEGATA, ID_AZIENDA, "+
        "ID_AZIENDA_ASSOCIATA, DATA_INGRESSO, "+
        "DATA_USCITA, DATA_INIZIO_VALIDITA, " +
        "DATA_FINE_VALIDITA, DATA_AGGIORNAMENTO, " +
        "ID_UTENTE_AGGIORNAMENTO "+
        "FROM DB_AZIENDA_COLLEGATA "+
        "WHERE "+
        "ID_AZIENDA = ? "+
        "AND ID_AZIENDA_ASSOCIATA = ? ";
      
      if (dataSituazione !=null)
      {
        query+=
          "AND DATA_INIZIO_VALIDITA  <= ? "+
          "AND (DATA_FINE_VALIDITA IS NULL "+
          "OR DATA_FINE_VALIDITA >= ?) ";
      }
      else
      {
        query +="AND NVL(TRUNC(DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > SYSDATE ";
      }
          

      SolmrLogger.debug(this, "Executing query: "+query);

      stmt = conn.prepareStatement(query);


      stmt.setLong(1, idAziendaFather.longValue());
      stmt.setLong(2, idAziendaSon.longValue());
      if (dataSituazione !=null)
      {
        stmt.setDate(3, new java.sql.Date(dataSituazione.getTime()));
        stmt.setDate(4, new java.sql.Date(dataSituazione.getTime()));
      }

      ResultSet rs = stmt.executeQuery();


      if (rs.next()) 
      {
        result = new AziendaCollegataVO();
        result.setIdAziendaCollegata(new Long(rs.getLong("ID_AZIENDA_COLLEGATA")));
        result.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        result.setIdAziendaAssociata(new Long(rs.getLong("ID_AZIENDA_ASSOCIATA")));
        result.setDataIngresso(rs.getDate("DATA_INGRESSO"));
        result.setDataUscita(rs.getDate("DATA_USCITA"));
        result.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        result.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        result.setDataAggiornamento(rs.getTimestamp("DATA_AGGIORNAMENTO"));
        result.setIdUtenteAggiornamento(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
      }
     
      rs.close();
      stmt.close();

    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "findAziendaCollegataByFatherAndSon - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "findAziendaCollegataByFatherAndSon - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "findAziendaCollegataByFatherAndSon - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "findAziendaCollegataByFatherAndSon - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  
  public boolean isSoggettoAssociatoByFatherAndSon(Long idAziendaFather, String cuaaSon,
      Date dataSituazione)
  throws DataAccessException {

    boolean trovato = false;
    Connection conn = null;
    PreparedStatement stmt = null;
    try 
    {
      conn = getDatasource().getConnection();

      String query =   
        "SELECT AC.ID_AZIENDA_COLLEGATA " +
        "FROM   DB_AZIENDA_COLLEGATA AC, " +
        "       DB_SOGGETTO_ASSOCIATO SA " +
        "WHERE  AC.ID_AZIENDA = ? " +
        "AND    AC.ID_SOGGETTO_ASSOCIATO = SA.ID_SOGGETTO_ASSOCIATO " +
        "AND    SA.CUAA = ? ";
      
      if (dataSituazione !=null)
      {
        query+=
          "AND AC.DATA_INIZIO_VALIDITA  <= ? "+
          "AND (AC.DATA_FINE_VALIDITA IS NULL "+
          "OR AC.DATA_FINE_VALIDITA >= ?) ";
      }
      else
      {
        query +="AND NVL(TRUNC(AC.DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY'))  > SYSDATE ";
      }
          

      SolmrLogger.debug(this, "Executing query: "+query);

      stmt = conn.prepareStatement(query);


      stmt.setLong(1, idAziendaFather.longValue());
      stmt.setString(2, cuaaSon);
      if (dataSituazione !=null)
      {
        stmt.setDate(3, new java.sql.Date(dataSituazione.getTime()));
        stmt.setDate(4, new java.sql.Date(dataSituazione.getTime()));
      }

      ResultSet rs = stmt.executeQuery();


      if (rs.next()) 
      {
        trovato = true;
      }
     
      rs.close();
      stmt.close();

    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "isSoggettoAssociatoByFatherAndSon - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "isSoggettoAssociatoByFatherAndSon - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "isSoggettoAssociatoByFatherAndSon - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "isSoggettoAssociatoByFatherAndSon - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return trovato;
  }
	
	
	
	
	
  
}
