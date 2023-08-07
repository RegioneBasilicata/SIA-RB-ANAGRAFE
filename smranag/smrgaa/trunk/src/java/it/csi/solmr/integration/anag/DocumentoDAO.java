package it.csi.solmr.integration.anag;

import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.UtenteIrideVO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.DocumentoConduzioneVO;
import it.csi.solmr.dto.anag.DocumentoProprietarioVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.TipoCategoriaDocumentoVO;
import it.csi.solmr.dto.anag.TipoDocumentoVO;
import it.csi.solmr.dto.anag.TipoStatoDocumentoVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

/**
 * DAO che si occupa delle gestione del documentale
 *
 * <p>Title: SMRGAA</p>
 *
 * <p>Description: Anagrafe delle Imprese Agricole e Agro-Alimentari</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: CSI - PIEMONTE</p>
 *
 * @author Mauro Vocale
 * @version 1.0
 */
public class DocumentoDAO extends it.csi.solmr.integration.BaseDAO {

	public DocumentoDAO() throws ResourceAccessException{
		super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
	}

	public DocumentoDAO(String refName) throws ResourceAccessException {
		super(refName);
	}

	/**
	 * Metodo per recuperare tutti i dati presenti nella tabella DB_TIPO_STATO_DOCUMENTO
	 * @param isActive boolean
	 * @return Vector
	 * @throws DataAccessException
	 * @throws SolmrException
	 */
	public Vector<TipoStatoDocumentoVO> getListTipoStatoDocumento(boolean isActive) throws DataAccessException, SolmrException {
		SolmrLogger.debug(this, "Invocating getListTipoStatoDocumento method in DocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<TipoStatoDocumentoVO> elencoTipoStatoDocumento = new Vector<TipoStatoDocumentoVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListTipoStatoDocumento method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListTipoStatoDocumento method in DocumentoDAO and it values: "+conn+"\n");

			String query = " SELECT TSD.ID_STATO_DOCUMENTO, " +
			"        TSD.DESCRIZIONE, " +
			"        TSD.DATA_INIZIO_VALIDITA, " +
			"        TSD.DATA_FINE_VALIDITA " +
			" FROM   DB_TIPO_STATO_DOCUMENTO TSD ";

			if(isActive) {
				query += " WHERE TSD.DATA_FINE_VALIDITA IS NULL ";
			}

			query += " ORDER BY TSD.DESCRIZIONE ASC ";

			SolmrLogger.debug(this, "Value of parameter 1 [IS_ACTIVE] in getListTipoStatoDocumento method in DocumentoDAO: "+isActive+"\n");

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing getListTipoStatoDocumento: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				TipoStatoDocumentoVO tipoStatoDocumentoVO = new TipoStatoDocumentoVO();
				tipoStatoDocumentoVO.setIdStatoDocumento(new Long(rs.getLong(1)));
				tipoStatoDocumentoVO.setDescrizione(rs.getString(2));
				tipoStatoDocumentoVO.setDataInizioValidita(rs.getDate(3));
				if(Validator.isNotEmpty(rs.getString(4))) {
					tipoStatoDocumentoVO.setDataFineValidita(rs.getDate(4));
				}
				elencoTipoStatoDocumento.add(tipoStatoDocumentoVO);
			}

			if(elencoTipoStatoDocumento.size() == 0) {
				throw new SolmrException((String)AnagErrors.get("ERR_NO_TIPO_STATO_DOCUMENTO"));
			}

			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListTipoStatoDocumento in DocumentoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(SolmrException se) {
			SolmrLogger.error(this, "getListTipoStatoDocumento in DocumentoDAO - SolmrException: "+se.getMessage()+"\n");
			throw new SolmrException(se.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListTipoStatoDocumento in DocumentoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.fatal(this, "getListTipoStatoDocumento in DocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.fatal(this, "getListTipoStatoDocumento in DocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipoStatoDocumento method in DocumentoDAO\n");
		return elencoTipoStatoDocumento;
	}

	/**
	 * Metodo per ricercare i documenti in relazione ai filtri di ricerca
	 * Utilizzato anche da SMRGAASV
	 *
	 * @param documentoVO DocumentoVO
	 * @param protocollazione String
	 * @param orderBy
	 * @return Vector
	 * @throws DataAccessException
	 */
	public Vector<DocumentoVO> searchDocumentiByParameters(DocumentoVO documentoVO, String protocollazione, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating searchDocumentiByParameters method in DocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<DocumentoVO> elencoDocumenti = new Vector<DocumentoVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in searchDocumentiByParameters method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in searchDocumentiByParameters method in DocumentoDAO and it values: "+conn+"\n");

			String query = " SELECT D.ID_DOCUMENTO, " +
			"        D.EXT_ID_DOCUMENTO, " +
			"        TTD.DESCRIZIONE AS TIPOLOGIA_DESC, " +
			"        TTD.ID_TIPOLOGIA_DOCUMENTO AS ID_TIPO_TIPOLOGIA_DOC, " +
			"        TCD.ID_CATEGORIA_DOCUMENTO, " +
			"        TCD.DESCRIZIONE AS CATEGORIA_DESC, " +
			"        TCD.IDENTIFICATIVO, " +
			"        TCD.TIPO_IDENTIFICATIVO, " +
			"        TD.ID_DOCUMENTO AS ID_TIPO, " +
			"        TD.DESCRIZIONE AS TIPO_DESC, " +
			"        D.ID_STATO_DOCUMENTO, " +
			"        D.ID_AZIENDA, " +
			"        D.CUAA, " +
			"        D.DATA_INIZIO_VALIDITA, " +
			"        D.DATA_FINE_VALIDITA, " +
			"        D.NUMERO_PROTOCOLLO, " +
			"        D.DATA_PROTOCOLLO, " +
			"        D.NUMERO_DOCUMENTO, " +
			"        D.ENTE_RILASCIO_DOCUMENTO, " +
			"        D.ID_DOCUMENTO_PRECEDENTE, " +
			"        D.NOTE, " +
			"        D.DATA_ULTIMO_AGGIORNAMENTO, " +
			"        D.UTENTE_ULTIMO_AGGIORNAMENTO, " +
			"         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
		    "          || ' ' " + 
		    "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
		    "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
		    "         WHERE D.UTENTE_ULTIMO_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
			"        AS DENOMINAZIONE, " +
			"       (SELECT PVU.DENOMINAZIONE " +
		    "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
		    "        WHERE D.UTENTE_ULTIMO_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
		    "        AS DESCRIZIONE_ENTE_APPARTENENZA, " +
			"        D.DATA_INSERIMENTO, " +
			"        D.DATA_VARIAZIONE_STATO, " +
			"        ID_UTENTE_AGGIORNAMENTO_SRV, " +
			"        D.NUMERO_PROTOCOLLO_ESTERNO, " +
			"        D.CUAA_SOCCIDARIO, " +
			"        D.ESITO_CONTROLLO, " +
			"        D.DATA_ESECUZIONE, " +
			"        D.FLAG_CUAA_SOCCIDARIO_VALIDATO " +
			" FROM   DB_DOCUMENTO D, " +
			"        DB_TIPO_DOCUMENTO TD, " +
			"        DB_TIPO_TIPOLOGIA_DOCUMENTO TTD, " +
			"        DB_TIPO_CATEGORIA_DOCUMENTO TCD, " +
			"        DB_DOCUMENTO_CATEGORIA DC, " +
			//"        PAPUA_V_UTENTE_LOGIN PVU, " +
			"        DB_TIPO_STATO_DOCUMENTO TSD" +
			" WHERE  D.CUAA = ? " +
			" AND    D.ID_AZIENDA = ? " +
			" AND    D.EXT_ID_DOCUMENTO = TD.ID_DOCUMENTO " +
			" AND    TD.ID_TIPOLOGIA_DOCUMENTO = TTD.ID_TIPOLOGIA_DOCUMENTO " +
			" AND    TCD.ID_TIPOLOGIA_DOCUMENTO = TTD.ID_TIPOLOGIA_DOCUMENTO " +
			" AND    TCD.ID_CATEGORIA_DOCUMENTO = DC.ID_CATEGORIA_DOCUMENTO " +
			" AND    DC.ID_DOCUMENTO = TD.ID_DOCUMENTO " +
			" AND    D.ID_STATO_DOCUMENTO = TSD.ID_STATO_DOCUMENTO(+) ";
			//" AND    D.UTENTE_ULTIMO_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN ";

			if(documentoVO.getTipoTipologiaDocumento() != null && documentoVO.getTipoTipologiaDocumento().getCode() != null) {
				query +=   " AND    TTD.ID_TIPOLOGIA_DOCUMENTO = ? ";
			}
			if(documentoVO.getTipoCategoriaDocumentoVO() != null && documentoVO.getTipoCategoriaDocumentoVO().getIdCategoriaDocumento() != null) {
				query +=   " AND    TCD.ID_CATEGORIA_DOCUMENTO = ? ";
			}
			if(documentoVO.getTipoDocumentoVO() != null && documentoVO.getTipoDocumentoVO().getIdDocumento() != null) {
				query +=   " AND    TD.ID_DOCUMENTO = ? ";
			}
			if(Validator.isNotEmpty(documentoVO.getIdStatoDocumento())) {
				if(documentoVO.getIdStatoDocumento().longValue() == Long.decode(SolmrConstants.ID_STATO_DOCUMENTO_ATTIVO).longValue()) {
					query += " AND  D.ID_STATO_DOCUMENTO IS NULL ";
				}
				else {
					query += " AND  D.ID_STATO_DOCUMENTO = ? ";
				}
			}
			
			if(Validator.isNotEmpty(documentoVO.getCheckScaduti()) 
			    && documentoVO.getCheckScaduti().equalsIgnoreCase("S"))
			{
  			if(documentoVO.getMinDataFineValidita() != null) {
  				query += " AND NVL(D.DATA_FINE_VALIDITA, ?) BETWEEN ? AND NVL(?, ?) ";
  			}
			}
			else 
			{
			  if(Validator.isNotEmpty(documentoVO.getIdStatoDocumento())) 
			  {
	        if(documentoVO.getIdStatoDocumento().longValue() == Long.decode(SolmrConstants.ID_STATO_DOCUMENTO_ATTIVO).longValue())
	        {	        
	          query += " AND NVL(D.DATA_FINE_VALIDITA, ?) > SYSDATE ";
	        }
			  }
      }
			
			if(Validator.isNotEmpty(protocollazione)) {
				// Protocollazione "effettuata"
				if(protocollazione.equalsIgnoreCase(SolmrConstants.PROTOCOLLAZIONE_EFFETTUATA)) {
					query += " AND D.NUMERO_PROTOCOLLO IS NOT NULL " +
					" AND D.DATA_PROTOCOLLO IS NOT NULL ";
				}
				// Protocollazione "da effettuare"
				else if(protocollazione.equalsIgnoreCase(SolmrConstants.PROTOCOLLAZIONE_DA_EFFETTUARE)) {
					query += " AND D.NUMERO_PROTOCOLLO IS NULL " +
					" AND D.DATA_PROTOCOLLO IS NULL ";
				}
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
			if(Validator.isNotEmpty(ordinamento)) 
			{
				query += ordinamento;
			}
			else {
				query += " ORDER BY D.ID_STATO_DOCUMENTO DESC, " +
				"          TTD.ID_TIPOLOGIA_DOCUMENTO, " +
				"          TD.ID_DOCUMENTO, D.DATA_INIZIO_VALIDITA";
			}

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Value of parameter 1 [CUAA] in searchDocumentiByParameters method in DocumentoDAO: "+documentoVO.getCuaa()+"\n");
			stmt.setString(1, documentoVO.getCuaa());
			SolmrLogger.debug(this, "Value of parameter 2 [ID_AZIENDA] in searchDocumentiByParameters method in DocumentoDAO: "+documentoVO.getIdAzienda()+"\n");
			stmt.setLong(2, documentoVO.getIdAzienda().longValue());
			int indice = 2;
			if(documentoVO.getTipoTipologiaDocumento() != null && documentoVO.getTipoTipologiaDocumento().getCode() != null) {
				indice++;
				SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_TIPOLOGIA_DOCUMENTO] in searchDocumentiByParameters method in DocumentoDAO: "+documentoVO.getTipoTipologiaDocumento().getCode().toString()+"\n");
				stmt.setLong(indice, documentoVO.getTipoTipologiaDocumento().getCode().longValue());
			}
			if(documentoVO.getTipoCategoriaDocumentoVO() != null && documentoVO.getTipoCategoriaDocumentoVO().getIdCategoriaDocumento() != null) {
				indice++;
				SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_CATEGORIA_DOCUMENTO] in searchDocumentiByParameters method in DocumentoDAO: "+documentoVO.getTipoCategoriaDocumentoVO().getIdCategoriaDocumento()+"\n");
				stmt.setLong(indice, documentoVO.getTipoCategoriaDocumentoVO().getIdCategoriaDocumento().longValue());
			}
			if(documentoVO.getTipoDocumentoVO() != null && documentoVO.getTipoDocumentoVO().getIdDocumento() != null) {
				indice++;
				SolmrLogger.debug(this, "Value of parameter "+indice+" [ID_DOCUMENTO] in searchDocumentiByParameters method in DocumentoDAO: "+documentoVO.getTipoDocumentoVO().getIdDocumento()+"\n");
				stmt.setLong(indice, documentoVO.getTipoDocumentoVO().getIdDocumento().longValue());
			}
			if(Validator.isNotEmpty(documentoVO.getIdStatoDocumento())) {
				if(documentoVO.getIdStatoDocumento().longValue() != Long.decode(SolmrConstants.ID_STATO_DOCUMENTO_ATTIVO).longValue()) {
					indice++;
					SolmrLogger.debug(this, "Value of parameter " + indice + " [ID_STATO_DOCUMENTO] in searchDocumentiByParameters method in DocumentoDAO: " +documentoVO.getIdStatoDocumento() + "\n");
					stmt.setLong(indice, documentoVO.getIdStatoDocumento().longValue());
				}
			}
			SolmrLogger.debug(this, "Value of parameter [PROTOCOLLAZIONE] in searchDocumentiByParameters method in DocumentoDAO: "+protocollazione+"\n");
			
			if(Validator.isNotEmpty(documentoVO.getCheckScaduti()) 
          && documentoVO.getCheckScaduti().equalsIgnoreCase("S"))
      {
  			if(Validator.isNotEmpty(documentoVO.getMinDataFineValidita())) {
  				indice++;
  				SolmrLogger.debug(this, "Value of parameter "+indice+" [DATA_FINE_VALIDITA] in searchDocumentiByParameters method in DocumentoDAO: "+SolmrConstants.ORACLE_FINAL_DATE+"\n");
  				stmt.setDate(indice, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
  				indice++;
  				SolmrLogger.debug(this, "Value of parameter "+indice+" [DATA_DAL] in searchDocumentiByParameters method in DocumentoDAO: "+documentoVO.getMinDataFineValidita()+"\n");
  				stmt.setDate(indice, new java.sql.Date(documentoVO.getMinDataFineValidita().getTime()));
  				indice++;
  				SolmrLogger.debug(this, "Value of parameter "+indice+" [DATA_AL] in searchDocumentiByParameters method in DocumentoDAO: "+documentoVO.getMaxDataFineValidita()+"\n");
  				if(documentoVO.getMaxDataFineValidita() != null) {
  					stmt.setDate(indice, new java.sql.Date(documentoVO.getMaxDataFineValidita().getTime()));
  				}
  				else {
  					stmt.setDate(indice, null);
  				}
  				indice++;
  				SolmrLogger.debug(this, "Value of parameter "+indice+" [DATA_FINE_VALIDITA] in searchDocumentiByParameters method in DocumentoDAO: "+SolmrConstants.ORACLE_FINAL_DATE+"\n");
  				stmt.setDate(indice, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
  			}
      }
			else 
			{
			  if(Validator.isNotEmpty(documentoVO.getIdStatoDocumento())) 
        {
          if(documentoVO.getIdStatoDocumento().longValue() == Long.decode(SolmrConstants.ID_STATO_DOCUMENTO_ATTIVO).longValue())
          {
            indice++;
            SolmrLogger.debug(this, "Value of parameter "+indice+" [DATA_FINE_VALIDITA] in searchDocumentiByParameters method in DocumentoDAO: "+SolmrConstants.ORACLE_FINAL_DATE+"\n");
            stmt.setDate(indice, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
          }
        }
      }
			
			SolmrLogger.debug(this, "Executing searchDocumentiByParameters: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			Long idDocumentoTmp = new Long(0);
			DocumentoVO documentoEsitoVO = null;
			while(rs.next()) 
			{
				
				Long idDocumento = new Long(rs.getLong("ID_DOCUMENTO"));
				if(idDocumento.compareTo(idDocumentoTmp) !=0)
				{
				  if(idDocumentoTmp.compareTo(new Long(0)) !=0)
				    elencoDocumenti.add(documentoEsitoVO);
				  
				  documentoEsitoVO = new DocumentoVO();
				  documentoEsitoVO.setIdDocumento(idDocumento);
  				documentoEsitoVO.setExtIdDocumento(new Long(rs.getLong("EXT_ID_DOCUMENTO")));
  				CodeDescription tipoTipologiaDocumento = new CodeDescription();
  				tipoTipologiaDocumento.setCode(Integer.decode(rs.getString("EXT_ID_DOCUMENTO")));
  				tipoTipologiaDocumento.setDescription(rs.getString("TIPOLOGIA_DESC"));
  				tipoTipologiaDocumento.setSecondaryCode(rs.getString("ID_TIPO_TIPOLOGIA_DOC"));
  				documentoEsitoVO.setTipoTipologiaDocumento(tipoTipologiaDocumento);
  				TipoCategoriaDocumentoVO tipoCategoriaDocumentoVO = new TipoCategoriaDocumentoVO();
  				tipoCategoriaDocumentoVO.setIdCategoriaDocumento(new Long(rs.getLong("ID_CATEGORIA_DOCUMENTO")));
  				tipoCategoriaDocumentoVO.setDescrizione(rs.getString("CATEGORIA_DESC"));
  				documentoEsitoVO.setTipoCategoriaDocumentoVO(tipoCategoriaDocumentoVO);
  				TipoDocumentoVO tipoDocumentoVO = new TipoDocumentoVO();
  				tipoDocumentoVO.setIdDocumento(new Long(rs.getLong("ID_TIPO")));
  				tipoDocumentoVO.setDescrizione(rs.getString("TIPO_DESC"));
  				documentoEsitoVO.setTipoDocumentoVO(tipoDocumentoVO);
  				if(Validator.isNotEmpty(rs.getString("ID_STATO_DOCUMENTO"))) {
  					documentoEsitoVO.setIdStatoDocumento(new Long(rs.getLong("ID_STATO_DOCUMENTO")));
  				}
  				documentoEsitoVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
  				documentoEsitoVO.setCuaa(rs.getString("CUAA"));
  				documentoEsitoVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
  				documentoEsitoVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
  				documentoEsitoVO.setNumeroProtocollo(rs.getString("NUMERO_PROTOCOLLO"));
  				documentoEsitoVO.setDataProtocollo(rs.getDate("DATA_PROTOCOLLO"));
  				documentoEsitoVO.setNumeroDocumento(rs.getString("NUMERO_DOCUMENTO"));
  				documentoEsitoVO.setEnteRilascioDocumento(rs.getString("ENTE_RILASCIO_DOCUMENTO"));
  				if(Validator.isNotEmpty(rs.getString("ID_DOCUMENTO_PRECEDENTE"))) {
  					documentoEsitoVO.setIdDocumentoPrecedente(new Long(rs.getLong("ID_DOCUMENTO_PRECEDENTE")));
  				}
  				documentoEsitoVO.setNote(rs.getString("NOTE"));
  				documentoEsitoVO.setDataUltimoAggiornamento(rs.getDate("DATA_ULTIMO_AGGIORNAMENTO"));
  				documentoEsitoVO.setUtenteUltimoAggiornamento(new Long(rs.getLong("UTENTE_ULTIMO_AGGIORNAMENTO")));
  				UtenteIrideVO utenteIrideVO = new UtenteIrideVO();
  				utenteIrideVO.setIdUtente(new Long(rs.getLong("UTENTE_ULTIMO_AGGIORNAMENTO")));
  				utenteIrideVO.setDenominazione(rs.getString("DENOMINAZIONE"));
  				utenteIrideVO.setDescrizioneEnteAppartenenza(rs.getString("DESCRIZIONE_ENTE_APPARTENENZA"));
  				documentoEsitoVO.setUtenteAggiornamento(utenteIrideVO);
  				documentoEsitoVO.setDataInserimento(rs.getDate("DATA_INSERIMENTO"));
  				documentoEsitoVO.setDataVariazioneStato(rs.getDate("DATA_VARIAZIONE_STATO"));
  				if(Validator.isNotEmpty(rs.getString("ID_UTENTE_AGGIORNAMENTO_SRV"))) {
  					documentoEsitoVO.setIdUtenteAggiornamentoSrv(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO_SRV")));
  				}
  				documentoEsitoVO.setNumeroProtocolloEsterno(rs.getString("NUMERO_PROTOCOLLO_ESTERNO"));
  				documentoEsitoVO.setCuaaSoccidario(rs.getString("CUAA_SOCCIDARIO"));
  				documentoEsitoVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
  				documentoEsitoVO.setDataEsecuzione(rs.getDate("DATA_ESECUZIONE"));
  				documentoEsitoVO.setFlagCuaaSoccidarioValidato(rs.getString("FLAG_CUAA_SOCCIDARIO_VALIDATO"));
  				
  				Long identificativo = checkLongNull(rs.getString("IDENTIFICATIVO"));
  				String tipoIdentificativo = rs.getString("TIPO_IDENTIFICATIVO");
  				
  				documentoEsitoVO.setFlagIstanzaRiesame("N");
  				if(Validator.isNotEmpty(identificativo) && Validator.isNotEmpty(tipoIdentificativo))
  				{
  				  if((identificativo.longValue() == 518)
  				      && tipoIdentificativo.equalsIgnoreCase("TC"))
  				  {
  				    documentoEsitoVO.setFlagIstanzaRiesame("S");
  				  }
  				    
  				}
  				
  				idDocumentoTmp = idDocumento;
				}
				else
				{
				  String descrizione = documentoEsitoVO.getTipoCategoriaDocumentoVO().getDescrizione();
				  descrizione += ", "+rs.getString("CATEGORIA_DESC");
				  documentoEsitoVO.getTipoCategoriaDocumentoVO().setDescrizione(descrizione);
				}
				
				
				//elencoDocumenti.add(documentoEsitoVO);
		
			}
			
			if(documentoEsitoVO != null)
			  elencoDocumenti.add(documentoEsitoVO);

			rs.close();
			stmt.close();
			SolmrLogger.debug(this, "Executed searchDocumentiByParameters and founded: "+elencoDocumenti.size()+" records\n");
		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "searchDocumentiByParameters in DocumentoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "searchDocumentiByParameters in DocumentoDAO - Generic Exception: "+ex+"\n");
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
				SolmrLogger.error(this, "searchDocumentiByParameters in DocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "searchDocumentiByParameters in DocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated searchDocumentiByParameters method in DocumentoDAO\n");
		return elencoDocumenti;
	}

	/**
	 * Metodo che si occupa di verificare se esiste già un record su DB_DOCUMENTO
	 * con i dati chiave dell'area anagrafica del documento
	 *
	 * @param documentoFiltroVO DocumentoVO
	 * @return DocumentoVO
	 * @throws DataAccessException
	 */
	public DocumentoVO findDocumentoVOBydDatiAnagrafici(DocumentoVO documentoFiltroVO) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating findDocumentoVOBydDatiAnagrafici method in DocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		DocumentoVO documentoVO = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in findDocumentoVOBydDatiAnagrafici method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in findDocumentoVOBydDatiAnagrafici method in DocumentoDAO and it values: "+conn+"\n");

			String query = " SELECT D.ID_DOCUMENTO, " +
			"        D.EXT_ID_DOCUMENTO, " +
			"        D.ID_STATO_DOCUMENTO, " +
			"        D.ID_AZIENDA, " +
			"        D.CUAA, " +
			"        D.DATA_INIZIO_VALIDITA, " +
			"        D.DATA_FINE_VALIDITA, " +
			"        D.NUMERO_PROTOCOLLO, " +
			"        D.DATA_PROTOCOLLO, " +
			"        D.NUMERO_DOCUMENTO, " +
			"        D.ENTE_RILASCIO_DOCUMENTO, " +
			"        D.ID_DOCUMENTO_PRECEDENTE, " +
			"        D.NOTE, " +
			"        D.DATA_ULTIMO_AGGIORNAMENTO, " +
			"        D.UTENTE_ULTIMO_AGGIORNAMENTO, " +
			"        D.DATA_INSERIMENTO, " +
			"        D.DATA_VARIAZIONE_STATO " +
			" FROM   DB_DOCUMENTO D " +
			" WHERE  D.ID_AZIENDA = ? " +
			" AND    D.CUAA = ? " +
			" AND    REPLACE(UPPER(D.ENTE_RILASCIO_DOCUMENTO), ' ', '') = REPLACE(UPPER(?), ' ', '') " +
			" AND    REPLACE(UPPER(D.NUMERO_DOCUMENTO), ' ', '') = REPLACE(UPPER(?), ' ', '') " +
			" AND    D.ID_STATO_DOCUMENTO IS NULL ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in findDocumentoVOBydDatiAnagrafici method in DocumentoDAO: "+documentoFiltroVO.getIdAzienda()+"\n");
			stmt.setLong(1, documentoFiltroVO.getIdAzienda().longValue());
			SolmrLogger.debug(this, "Value of parameter 2 [CUAA] in findDocumentoVOBydDatiAnagrafici method in DocumentoDAO: "+documentoFiltroVO.getCuaa()+"\n");
			stmt.setString(2, documentoFiltroVO.getCuaa());
			SolmrLogger.debug(this, "Value of parameter 3 [ENTE_RILASCIO_DOCUMENTO] in findDocumentoVOBydDatiAnagrafici method in DocumentoDAO: "+documentoFiltroVO.getEnteRilascioDocumento()+"\n");
			stmt.setString(3, documentoFiltroVO.getEnteRilascioDocumento());
			SolmrLogger.debug(this, "Value of parameter 4 [NUMERO_DOCUMENTO] in findDocumentoVOBydDatiAnagrafici method in DocumentoDAO: "+documentoFiltroVO.getNumeroDocumento()+"\n");
			stmt.setString(4, documentoFiltroVO.getNumeroDocumento());

			SolmrLogger.debug(this, "Executing findDocumentoVOBydDatiAnagrafici: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) {
				documentoVO = new DocumentoVO();
				documentoVO.setIdDocumento(new Long(rs.getLong(1)));
				documentoVO.setExtIdDocumento(new Long(rs.getLong(2)));
				if(Validator.isNotEmpty(rs.getString(3))) {
					documentoVO.setIdStatoDocumento(new Long(rs.getLong(3)));
				}
				documentoVO.setIdAzienda(new Long(rs.getLong(4)));
				documentoVO.setCuaa(rs.getString(5));
				documentoVO.setDataInizioValidita(new java.util.Date(rs.getDate(6).getTime()));
				if(Validator.isNotEmpty(rs.getString(7))) {
					documentoVO.setDataFineValidita(new java.util.Date(rs.getDate(7).getTime()));
				}
				documentoVO.setNumeroProtocollo(rs.getString(8));
				if(Validator.isNotEmpty(rs.getString(9))) {
					documentoVO.setDataProtocollo(new java.util.Date(rs.getDate(9).getTime()));
				}
				documentoVO.setNumeroDocumento(rs.getString(10));
				documentoVO.setEnteRilascioDocumento(rs.getString(11));
				if(Validator.isNotEmpty(rs.getString(12))) {
					documentoVO.setIdDocumentoPrecedente(new Long(rs.getLong(12)));
				}
				documentoVO.setNote(rs.getString(13));
				if(Validator.isNotEmpty(rs.getString(14))) {
					documentoVO.setDataUltimoAggiornamento(new java.util.Date(rs.getDate(14).getTime()));
				}
				documentoVO.setUtenteUltimoAggiornamento(new Long(rs.getLong(15)));
				documentoVO.setDataInserimento(new java.util.Date(rs.getDate(16).getTime()));
				if(Validator.isNotEmpty(rs.getString(17))) {
					documentoVO.setDataVariazioneStato(new java.util.Date(rs.getDate(17).getTime()));
				}
			}

			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "findDocumentoVOBydDatiAnagrafici in DocumentoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "findDocumentoVOBydDatiAnagrafici in DocumentoDAO - Generic Exception: "+ex+"\n");
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
				SolmrLogger.error(this, "findDocumentoVOBydDatiAnagrafici in DocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "findDocumentoVOBydDatiAnagrafici in DocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated findDocumentoVOBydDatiAnagrafici method in DocumentoDAO\n");
		return documentoVO;
	}

	/**
	 * Metodo per recuperare il numero protocollo
	 * @param ruoloUtenza
	 * @param anno String
	 * @return String
	 * @throws DataAccessException
	 */
	public String getNumeroProtocollo(RuoloUtenza ruoloUtenza, String anno) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getNumeroProtocollo method in DocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		String numeroProtocollo = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in getNumeroProtocollo method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getNumeroProtocollo method in DocumentoDAO and it values: "+conn+"\n");

			String query = "";
			// Se l'utente loggato è un intermediario o un OPR GESTORE
			if(ruoloUtenza.isUtenteIntermediario() || ruoloUtenza.isUtenteOPRGestore()) {
				query = " SELECT SUBSTR(CODICE_ENTE,1,3) " +
				"        || '.' ||               " +
				"        SUBSTR(CODICE_ENTE,4,3) " +
				"        || '.' ||               " +
				"        SUBSTR(CODICE_ENTE,7,3) " +
				"        || '.' ||               " +
				"        P.ANNO                  " +
				"        || '.' ||               " +
				"        P.PROGRESSIVO           " +
				" FROM   DB_NUMERO_PROTOCOLLO P  " +
				" WHERE  P.CODICE_ENTE = ? " +
				" AND    P.ANNO = ? " +
				" FOR UPDATE ";
			}
			// Se l'utente loggato è un PA
			else {
				if(Validator.isNumericInteger(ruoloUtenza.getCodiceEnte()) && ruoloUtenza.getCodiceEnte().length() > 3) {
					query = " SELECT SUBSTR(CODICE_ENTE,1,3) " +
					"        || '.' ||               " +
					"        SUBSTR(CODICE_ENTE,4) " +
					"        || '.' ||               " +
					"        P.ANNO                  " +
					"        || '.' ||               " +
					"        P.PROGRESSIVO           " +
					" FROM   DB_NUMERO_PROTOCOLLO P  " +
					" WHERE  P.CODICE_ENTE = ? " +
					" AND    P.ANNO = ? " +
					" FOR UPDATE ";

				}
				else {
					query = " SELECT SUBSTR(CODICE_ENTE,1)  " +
					"        || '.' ||              " +
					"        P.ANNO                 " +
					"        || '.' ||              " +
					"        P.PROGRESSIVO          " +
					" FROM   DB_NUMERO_PROTOCOLLO P " +
					" WHERE  P.CODICE_ENTE = ? " +
					" AND    P.ANNO = ? " +
					" FOR UPDATE ";
				}
			}

			stmt = conn.prepareStatement(query);

			if(ruoloUtenza.isUtenteIntermediario() || ruoloUtenza.isUtenteOPRGestore()) {
				SolmrLogger.debug(this, "Value of parameter 1 [CODICE_FISCALE_INTERMEDIARIO] in getNumeroProtocollo method in DocumentoDAO: "+ruoloUtenza.getCodiceEnte()+"\n");
				stmt.setString(1, ruoloUtenza.getCodiceEnte());
			}
			else {
				SolmrLogger.debug(this, "Value of parameter 1 [SIGLA_AMMINISTRAZIONE_PA] in getNumeroProtocollo method in DocumentoDAO: "+ruoloUtenza.getSiglaAmministrazione()+"\n");
				stmt.setString(1, ruoloUtenza.getSiglaAmministrazione());
			}
			SolmrLogger.debug(this, "Value of parameter 2 [ANNO] in getNumeroProtocollo method in DocumentoDAO: "+anno+"\n");
			stmt.setString(2, anno);

			SolmrLogger.debug(this, "Executing getNumeroProtocollo: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) {
				numeroProtocollo = rs.getString(1);
			}

			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getNumeroProtocollo in DocumentoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getNumeroProtocollo in DocumentoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.error(this, "getNumeroProtocollo in DocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.error(this, "getNumeroProtocollo in DocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getNumeroProtocollo method in DocumentoDAO\n");
		return numeroProtocollo;
	}

	/**
	 * Metodo per inserire un record sulla tabella DB_NUMERO_PROTOCOLLO
	 *
	 * @param ruoloUtenza
	 * @param anno String
	 * @return Long
	 * @throws DataAccessException
	 * @throws SolmrException
	 */
	public Long insertNumeroProtocollo(RuoloUtenza ruoloUtenza, String anno) throws DataAccessException, SolmrException {
		SolmrLogger.debug(this, "Invocating insertNumeroProtocollo method in DocumentoDAO\n");
		Long primaryKey = null;
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			SolmrLogger.debug(this, "Creating primary key in method insertNumeroProtocollo in DocumentoDAO\n");
			primaryKey = getNextPrimaryKey((String)SolmrConstants.get("SEQ_NUMERO_PROTOCOLLO"));
			SolmrLogger.debug(this, "Created primary key in method insertNumeroProtocollo in DocumentoDAO with value: "+primaryKey+"\n");

			SolmrLogger.debug(this, "Creating db-connection in insertNumeroProtocollo method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in insertNumeroProtocollo method in DocumentoDAO and it values: "+conn+"\n");


			String insert = " INSERT INTO DB_NUMERO_PROTOCOLLO "+
			" (ID_NUMERO_PROTOCOLLO, CODICE_ENTE, ANNO, "+
			"  PROGRESSIVO, FLAG_INTERMEDIARIO) " +
			"  VALUES (?, ?, ?, ?, ?)";

			stmt = conn.prepareStatement(insert);

			stmt.setLong(1, primaryKey.longValue());
			// Se si tratta di un utente intermediario o di un OPR GESTORE inserisco il codice ente
			if(ruoloUtenza.isUtenteIntermediario() || ruoloUtenza.isUtenteOPRGestore()) {
				SolmrLogger.debug(this,"Value of parameter 2 [CODICE_ENTE] in insertNumeroProtocollo method in DocumentoDAO: "+ruoloUtenza.getCodiceEnte()+"\n");
				stmt.setString(2, ruoloUtenza.getCodiceEnte());
			}
			// Altrimenti, per tutti gli altri utente, la sigla dell'amministrazione di competenza
			else {
				SolmrLogger.debug(this,"Value of parameter 2 [SIGLA_AMMINISTRAZIONE] in insertNumeroProtocollo method in DocumentoDAO: "+ruoloUtenza.getSiglaAmministrazione()+"\n");
				stmt.setString(2, ruoloUtenza.getSiglaAmministrazione());
			}
			SolmrLogger.debug(this,"Value of parameter 3 [ANNO] in insertNumeroProtocollo method in DocumentoDAO: "+anno+"\n");
			stmt.setString(3, anno);
			SolmrLogger.debug(this,"Value of parameter 4 [PROGRESSIVO] in insertNumeroProtocollo method in DocumentoDAO: 1\n");
			stmt.setString(4, "1");
			if(ruoloUtenza.isUtenteIntermediario() || ruoloUtenza.isUtenteOPRGestore()) {
				SolmrLogger.debug(this,"Value of parameter 5 [FLAG_INTERMEDIARIO] in insertNumeroProtocollo method in DocumentoDAO: "+SolmrConstants.FLAG_S+"\n");
				stmt.setString(5, SolmrConstants.FLAG_S);
			}
			else {
				SolmrLogger.debug(this,"Value of parameter 5 [FLAG_INTERMEDIARIO] in insertNumeroProtocollo method in DocumentoDAO: "+SolmrConstants.FLAG_N+"\n");
				stmt.setString(5, SolmrConstants.FLAG_N);
			}

			SolmrLogger.debug(this, "Executing insertNumeroProtocollo: "+insert+"\n");

			stmt.executeUpdate();

			stmt.close();

			SolmrLogger.debug(this, "Executed insertNumeroProtocollo method in DocumentoDAO\n");
		}
		catch (SQLException se) {
			if(se.getErrorCode() == ((Long)SolmrConstants.get("CODE_DUP_KEY_ORACLE")).intValue()) {
				SolmrLogger.error(this, "insertNumeroProtocollo in DocumentoDAO - SolmrException: "+(String)AnagErrors.get("ERR_BBAN_DUPLICATO")+"\n");
				throw new SolmrException((String)AnagErrors.get("ERR_BBAN_DUPLICATO"));
			}
			else {
				SolmrLogger.error(this, "insertNumeroProtocollo in DocumentoDAO - SQLException: "+se.getMessage()+"\n");
				throw new DataAccessException(se.getMessage());
			}
		}
		catch (Exception ex) {
			SolmrLogger.error(this, "Generic Exception in insertNumeroProtocollo: "+ex.getMessage()+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.error(this, "SQLException while closing Statement and Connection in insertNumeroProtocollo: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in insertNumeroProtocollo: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		return primaryKey;
	}

	/**
	 * Metodo per inserire il record sulla tabella DB_DOCUMENTO
	 *
	 * @param documentoVO DocumentoVO
	 * @param ruoloUtenza RuoloUtenza
	 * @return Long
	 * @throws DataAccessException
	 */
	public Long insertDocumento(DocumentoVO documentoVO, RuoloUtenza ruoloUtenza) throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating insertDocumento method in DocumentoDAO\n");
		Long primaryKey = null;
		Connection conn = null;
		PreparedStatement stmt = null;

		try 
		{
			SolmrLogger.debug(this, "Creating primary key in method insertDocumento in DocumentoDAO\n");
			primaryKey = getNextPrimaryKey((String)SolmrConstants.get("SEQ_DOCUMENTO"));
			SolmrLogger.debug(this, "Created primary key in method insertDocumento in DocumentoDAO with value: "+primaryKey+"\n");

			SolmrLogger.debug(this, "Creating db-connection in insertDocumento method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in insertDocumento method in DocumentoDAO and it values: "+conn+"\n");


			String insert = " INSERT INTO DB_DOCUMENTO (ID_DOCUMENTO, " +
			"                           EXT_ID_DOCUMENTO, " +
			"                           ID_STATO_DOCUMENTO, " +
			"                           ID_AZIENDA, " +
			"                           CUAA, " +
			"                           DATA_INIZIO_VALIDITA, "+
			"                           DATA_FINE_VALIDITA, " +
			"                           NUMERO_PROTOCOLLO, " +
			"                           DATA_PROTOCOLLO, " +
			"                           NUMERO_DOCUMENTO, " +
			"                           ENTE_RILASCIO_DOCUMENTO, " +
			"                           ID_DOCUMENTO_PRECEDENTE, " +
			"                           NOTE, " +
			"                           DATA_ULTIMO_AGGIORNAMENTO, " +
			"                           UTENTE_ULTIMO_AGGIORNAMENTO, " +
			"                           DATA_INSERIMENTO, " +
			"                           DATA_VARIAZIONE_STATO, " +
			"                           ID_UTENTE_AGGIORNAMENTO_SRV, " +
			"                           NUMERO_PROTOCOLLO_ESTERNO, " +
			"                           CUAA_SOCCIDARIO, " +
			"                           ESITO_CONTROLLO, " +
			"                           DATA_ESECUZIONE, " +
			"                           FLAG_CUAA_SOCCIDARIO_VALIDATO," +
			"                           ID_CONTO_CORRENTE," +
			"                           ID_CAUSALE_MODIFICA_DOCUMENTO) " +
			" VALUES (?, ?, ?, ?, UPPER(?), ?, ?, ?, ?, UPPER(?), UPPER(?), ?, ?, ?, ?, ?, ?, ?, UPPER(?), UPPER(?), ?, ?, ?, ?, ?)";

			stmt = conn.prepareStatement(insert);
			
			int idx = 0;

			stmt.setLong(++idx, primaryKey.longValue());
			SolmrLogger.debug(this,"Value of parameter 2 [EXT_ID_DOCUMENTO] in insertDocumento method in DocumentoDAO: "+documentoVO.getExtIdDocumento()+"\n");
			stmt.setLong(++idx, documentoVO.getExtIdDocumento().longValue());
			SolmrLogger.debug(this,"Value of parameter 3 [ID_STATO_DOCUMENTO] in insertDocumento method in DocumentoDAO: "+documentoVO.getIdStatoDocumento()+"\n");
			if(Validator.isNotEmpty(documentoVO.getIdStatoDocumento())) {
				stmt.setLong(++idx, documentoVO.getIdStatoDocumento().longValue());
			}
			else {
				stmt.setString(++idx, null);
			}
			SolmrLogger.debug(this,"Value of parameter 4 [ID_AZIENDA] in insertDocumento method in DocumentoDAO: "+documentoVO.getIdAzienda()+"\n");
			stmt.setLong(++idx, documentoVO.getIdAzienda().longValue());
			SolmrLogger.debug(this,"Value of parameter 5 [CUAA] in insertDocumento method in DocumentoDAO: "+documentoVO.getCuaa()+"\n");
			stmt.setString(++idx, documentoVO.getCuaa());
			SolmrLogger.debug(this,"Value of parameter 6 [DATA_INIZIO_VALIDITA] in insertDocumento method in DocumentoDAO: "+documentoVO.getDataInizioValidita()+"\n");
			stmt.setDate(++idx, new java.sql.Date(documentoVO.getDataInizioValidita().getTime()));
			SolmrLogger.debug(this,"Value of parameter 7 [DATA_FINE_VALIDITA] in insertDocumento method in DocumentoDAO: "+documentoVO.getDataFineValidita()+"\n");
			if(Validator.isNotEmpty(documentoVO.getDataFineValidita())) {
				stmt.setDate(++idx, new java.sql.Date(documentoVO.getDataFineValidita().getTime()));
			}
			else {
				stmt.setString(++idx, null);
			}
			SolmrLogger.debug(this,"Value of parameter 8 [NUMERO_PROTOCOLLO] in insertDocumento method in DocumentoDAO: "+documentoVO.getNumeroProtocollo()+"\n");
			stmt.setString(++idx, documentoVO.getNumeroProtocollo());
			SolmrLogger.debug(this,"Value of parameter 9 [DATA_PROTOCOLLO] in insertDocumento method in DocumentoDAO: "+documentoVO.getDataProtocollo()+"\n");
			if(Validator.isNotEmpty(documentoVO.getDataProtocollo())) {
				stmt.setDate(++idx, new java.sql.Date(documentoVO.getDataProtocollo().getTime()));
			}
			else {
				stmt.setString(++idx, null);
			}
			SolmrLogger.debug(this,"Value of parameter 10 [NUMERO_DOCUMENTO] in insertDocumento method in DocumentoDAO: "+documentoVO.getNumeroDocumento()+"\n");
			stmt.setString(++idx, documentoVO.getNumeroDocumento());
			SolmrLogger.debug(this,"Value of parameter 11 [ENTE_RILASCIO_DOCUMENTO] in insertDocumento method in DocumentoDAO: "+documentoVO.getEnteRilascioDocumento()+"\n");
			stmt.setString(++idx, documentoVO.getEnteRilascioDocumento());
			SolmrLogger.debug(this,"Value of parameter 12 [ID_DOCUMENTO_PRECEDENTE] in insertDocumento method in DocumentoDAO: "+documentoVO.getIdDocumentoPrecedente()+"\n");
			if(Validator.isNotEmpty(documentoVO.getIdDocumentoPrecedente())) {
				stmt.setLong(++idx, documentoVO.getIdDocumentoPrecedente().longValue());
			}
			else {
				stmt.setString(++idx, null);
			}
			SolmrLogger.debug(this,"Value of parameter 13 [NOTE] in insertDocumento method in DocumentoDAO: "+documentoVO.getNote()+"\n");
			stmt.setString(++idx, documentoVO.getNote());
			SolmrLogger.debug(this,"Value of parameter 14 [DATA_ULTIMO_AGGIORNAMENTO] in insertDocumento method in DocumentoDAO: "+documentoVO.getDataUltimoAggiornamento()+"\n");
			stmt.setTimestamp(++idx, new Timestamp(documentoVO.getDataUltimoAggiornamento().getTime()));
			// Se è un applicativo esterno ad inserire un documento forzo l'id_utente_aggiornamento
			// con la costante relativo all'utente inserito come default per questo tipo di operazioni:
			// questa operazione è resa necessaria dal fatto che SMRGAA non sfrutta
			// ancora IRIDE2 con tutte le sue caratteristiche ma una versione "IBRIDA".
			// Questa modifica andrà poi eliminata quando verrà effettuato il passaggio
			// definitivo
			if(ruoloUtenza.getIdProcedimento().intValue() != SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE) {
				SolmrLogger.debug(this,"Value of parameter 15 [UTENTE_ULTIMO_AGGIORNAMENTO] in insertDocumento method in DocumentoDAO: "+SolmrConstants.ID_UTENTE_DEFAULT_INSERIMENTO+"\n");
				stmt.setLong(++idx, Long.decode(SolmrConstants.ID_UTENTE_DEFAULT_INSERIMENTO).longValue());
			}
			else {
				SolmrLogger.debug(this,"Value of parameter 15 [UTENTE_ULTIMO_AGGIORNAMENTO] in insertDocumento method in DocumentoDAO: "+ruoloUtenza.getIdUtente()+"\n");
				stmt.setLong(++idx, ruoloUtenza.getIdUtente().longValue());
			}
			SolmrLogger.debug(this,"Value of parameter 16 [DATA_INSERIMENTO] in insertDocumento method in DocumentoDAO: "+documentoVO.getDataInserimento()+"\n");
			stmt.setTimestamp(++idx, new Timestamp(documentoVO.getDataInserimento().getTime()));
			SolmrLogger.debug(this,"Value of parameter 17 [DATA_VARIAZIONE_STATO] in insertDocumento method in DocumentoDAO: "+documentoVO.getDataVariazioneStato()+"\n");
			if(Validator.isNotEmpty(documentoVO.getDataVariazioneStato())) {
				stmt.setDate(++idx, new java.sql.Date(documentoVO.getDataVariazioneStato().getTime()));
			}
			else {
				stmt.setString(++idx, null);
			}
			// Se è un applicativo esterno ad inserire un documento forzo l'id_utente_aggiornamento
			// con la costante relativo all'utente inserito come default per questo tipo di operazioni:
			// questa operazione è resa necessaria dal fatto che SMRGAA non sfrutta
			// ancora IRIDE2 con tutte le sue caratteristiche ma una versione "IBRIDA".
			// Questa modifica andrà poi eliminata quando verrà effettuato il passaggio
			// definitivo
			if(ruoloUtenza.getIdProcedimento().compareTo(Long.decode(String.valueOf(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE))) != 0) {
				SolmrLogger.debug(this,"Value of parameter 18 [ID_UTENTE_AGGIORNAMENTO_SRV] in insertDocumento method in DocumentoDAO: "+ruoloUtenza.getIdUtente()+"\n");
				stmt.setLong(++idx, ruoloUtenza.getIdUtente().longValue());
			}
			else {
				SolmrLogger.debug(this,"Value of parameter 18 [ID_UTENTE_AGGIORNAMENTO_SRV] in insertDocumento method in DocumentoDAO: "+null+"\n");
				stmt.setString(++idx, null);
			}
			SolmrLogger.debug(this,"Value of parameter 19 [NUMERO_PROTOCOLLO_ESTERNO] in insertDocumento method in DocumentoDAO: "+documentoVO.getNumeroProtocolloEsterno()+"\n");
			stmt.setString(++idx, documentoVO.getNumeroProtocolloEsterno());
			SolmrLogger.debug(this,"Value of parameter 20 [CUAA_SOCCIDARIO] in insertDocumento method in DocumentoDAO: "+documentoVO.getCuaaSoccidario()+"\n");
			stmt.setString(++idx, documentoVO.getCuaaSoccidario());
			SolmrLogger.debug(this,"Value of parameter 21 [ESITO_CONTROLLO] in insertDocumento method in DocumentoDAO: "+documentoVO.getEsitoControllo()+"\n");
			stmt.setString(++idx, documentoVO.getEsitoControllo());
			SolmrLogger.debug(this,"Value of parameter 22 [DATA_ESECUZIONE] in insertDocumento method in DocumentoDAO: "+documentoVO.getDataEsecuzione()+"\n");
			if(documentoVO.getDataEsecuzione() != null) {
				stmt.setTimestamp(++idx, new Timestamp(documentoVO.getDataEsecuzione().getTime()));
			}
			else {
				stmt.setString(++idx, null);
			}
			SolmrLogger.debug(this,"Value of parameter 23 [FLAG_CUAA_SOCCIDARIO_VALIDATO] in insertDocumento method in DocumentoDAO: "+documentoVO.getFlagCuaaSoccidarioValidato()+"\n");
			stmt.setString(++idx, documentoVO.getFlagCuaaSoccidarioValidato());			
			SolmrLogger.debug(this,"Value of parameter 24 [ID_CONTO_CORRENTE] in insertDocumento method in DocumentoDAO: "+documentoVO.getIdContoCorrente()+"\n");
      if(Validator.isNotEmpty(documentoVO.getIdContoCorrente())) {
        stmt.setLong(++idx, documentoVO.getIdContoCorrente().longValue());
      }
      else {
        stmt.setString(++idx, null);
      }
      SolmrLogger.debug(this,"Value of parameter 25 [ID_CAUSALA_MODIFICA_DOCUMENTO] in insertDocumento method in DocumentoDAO: "+documentoVO.getIdCausaleModificaDocumento()+"\n");
      if(Validator.isNotEmpty(documentoVO.getIdCausaleModificaDocumento())) {
        stmt.setLong(++idx, documentoVO.getIdCausaleModificaDocumento().longValue());
      }
      else {
        stmt.setString(++idx, null);
      }

			SolmrLogger.debug(this, "Executing insertDocumento: "+insert+"\n");

			stmt.executeUpdate();

			stmt.close();

			SolmrLogger.debug(this, "Executed insertDocumento method in DocumentoDAO\n");
		}
		catch(SQLException se) {
			SolmrLogger.error(this, "insertDocumento in DocumentoDAO - SQLException: "+se.getMessage()+"\n");
			throw new DataAccessException(se.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "Generic Exception in insertDocumento: "+ex.getMessage()+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "SQLException while closing Statement and Connection in insertDocumento: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in insertDocumento: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		return primaryKey;
	}

	/**
	 * Metodo per aggiornare il campo progressivo relativo alla tabella
	 * DB_NUMERO_PROTOCOLLO
	 *
	 * @throws DataAccessException
	 * @param ruoloUtenza
	 * @param anno String
	 */
	public void aggiornaProgressivoNumeroProtocollo(RuoloUtenza ruoloUtenza, String anno) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating aggiornaProgressivoNumeroProtocollo method in DocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in aggiornaProgressivoNumeroProtocollo method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in aggiornaProgressivoNumeroProtocollo method in DocumentoDAO and it values: "+conn+"\n");


			String query = " UPDATE DB_NUMERO_PROTOCOLLO "+
			" SET    PROGRESSIVO = PROGRESSIVO + 1 " +
			" WHERE  CODICE_ENTE = ? " +
			" AND    ANNO = ? ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Executing aggiornaProgressivoNumeroProtocollo: "+query+"\n");


			if(ruoloUtenza.isUtenteIntermediario() || ruoloUtenza.isUtenteOPRGestore()) {
				SolmrLogger.debug(this, "Value of parameter 1 [CODICE_FISCALE_INTERMEDIARIO] in getNumeroProtocollo method in DocumentoDAO: "+ruoloUtenza.getCodiceEnte()+"\n");
				stmt.setString(1, ruoloUtenza.getCodiceEnte());
			}
			else {
				SolmrLogger.debug(this,"Value of parameter 1 [SIGLA_AMMINISTRAZIONE] in aggiornaProgressivoNumeroProtocollo method in DocumentoDAO: "+ruoloUtenza.getSiglaAmministrazione()+"\n");
				stmt.setString(1, ruoloUtenza.getSiglaAmministrazione());
			}
			SolmrLogger.debug(this,"Value of parameter 2 [ANNO] in aggiornaProgressivoNumeroProtocollo method in DocumentoDAO: "+anno+"\n");
			stmt.setString(2, anno);

			stmt.executeUpdate();

			stmt.close();

			SolmrLogger.debug(this, "Executed aggiornaProgressivoNumeroProtocollo in DocumentoDAO\n");
		}
		catch (SQLException exc) {
			SolmrLogger.error(this, "aggiornaProgressivoNumeroProtocollo in DocumentoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch (Exception ex) {
			SolmrLogger.error(this, "aggiornaProgressivoNumeroProtocollo in DocumentoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.error(this, "aggiornaProgressivoNumeroProtocollo in DocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.error(this, "aggiornaProgressivoNumeroProtocollo in DocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
	}

	/**
	 * Metodo per recuperare il documento a partire dalla chiave primaria
	 * @param idDocumento Long
	 * @return DocumentoVO
	 * @throws DataAccessException
	 */
	public DocumentoVO findDocumentoVOByPrimaryKey(Long idDocumento) throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating findDocumentoVOByPrimaryKey method in DocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		DocumentoVO documentoVO = null;

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in findDocumentoVOByPrimaryKey method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in findDocumentoVOByPrimaryKey method in DocumentoDAO and it values: "+conn+"\n");

			String query = " SELECT D.ID_DOCUMENTO, " +
			"        D.EXT_ID_DOCUMENTO, " +
			"        TTD.DESCRIZIONE AS TIPOLOGIA_DESC, " +
			"        TCD.ID_CATEGORIA_DOCUMENTO, " +
			"        TCD.DESCRIZIONE AS CATEGORIA_DESC, " +
			"        TCD.TIPO_IDENTIFICATIVO, " +
			"        TCD.IDENTIFICATIVO, " +
			"        TD.ID_DOCUMENTO AS ID_TIPO, " +
			"        TD.DESCRIZIONE AS TIPO_DESC, " +
			"        TD.EXT_ID_TIPO_DOCUMENTO_INDEX, " +
			"        D.ID_STATO_DOCUMENTO, " +
			"        TSD.DESCRIZIONE AS DESC_STATO_DOCUMENTO, " +
			"        D.ID_AZIENDA, " +
			"        D.CUAA, " +
			"        D.DATA_INIZIO_VALIDITA, " +
			"        D.DATA_FINE_VALIDITA, " +
			"        D.NUMERO_PROTOCOLLO, " +
			"        D.DATA_PROTOCOLLO, " +
			"        D.NUMERO_DOCUMENTO, " +
			"        D.ENTE_RILASCIO_DOCUMENTO, " +
			"        D.ID_DOCUMENTO_PRECEDENTE, " +
			"        D.NOTE, " +
			"        D.DATA_ULTIMO_AGGIORNAMENTO, " +
			"        D.UTENTE_ULTIMO_AGGIORNAMENTO, " +
			"         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
		    "          || ' ' " + 
		    "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
		    "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
		    "         WHERE D.UTENTE_ULTIMO_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
			"        AS DENOMINAZIONE, " +
			"       (SELECT PVU.DENOMINAZIONE " +
		    "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
		    "        WHERE D.UTENTE_ULTIMO_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
			"        AS DESCRIZIONE_ENTE_APPARTENENZA, " +
			"       (SELECT PVU.CODICE_ENTE " +
		      "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
		      "        WHERE D.UTENTE_ULTIMO_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
		      "        AS CODICE_ENTE, " +
			"        D.DATA_INSERIMENTO, " +
			"        D.DATA_VARIAZIONE_STATO, " +
			"        ID_UTENTE_AGGIORNAMENTO_SRV, " +
			"        D.NUMERO_PROTOCOLLO_ESTERNO, " +
			"        D.CUAA_SOCCIDARIO, " +
			"        D.ESITO_CONTROLLO, " +
			"        D.DATA_ESECUZIONE, " +
			"        D.ID_CONTO_CORRENTE, " +
			"        D.ID_CAUSALE_MODIFICA_DOCUMENTO," +
			"        CMD.DESCRIZIONE DESC_CAUSALE_DOC, " +
			"        TD.FLAG_ANNULLABILE " +
			" FROM   DB_DOCUMENTO D, " +
			"        DB_TIPO_DOCUMENTO TD, " +
			"        DB_TIPO_TIPOLOGIA_DOCUMENTO TTD, " +
			"        DB_TIPO_CATEGORIA_DOCUMENTO TCD, " +
			"        DB_DOCUMENTO_CATEGORIA DC, " +
			//"        PAPUA_V_UTENTE_LOGIN PVU, " +
			"        DB_CAUSALE_MODIFICA_DOCUMENTO CMD," +
			"        DB_TIPO_STATO_DOCUMENTO TSD " +
			" WHERE  D.ID_DOCUMENTO = ? " +
			" AND    D.EXT_ID_DOCUMENTO = TD.ID_DOCUMENTO " +
			" AND    TD.ID_TIPOLOGIA_DOCUMENTO = TTD.ID_TIPOLOGIA_DOCUMENTO " +
			" AND    TCD.ID_TIPOLOGIA_DOCUMENTO = TTD.ID_TIPOLOGIA_DOCUMENTO " +
			" AND    TCD.ID_CATEGORIA_DOCUMENTO = DC.ID_CATEGORIA_DOCUMENTO " +
			" AND    DC.ID_DOCUMENTO = TD.ID_DOCUMENTO " +
			//" AND    D.UTENTE_ULTIMO_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
			" AND    D.ID_CAUSALE_MODIFICA_DOCUMENTO = CMD.ID_CAUSALE_MODIFICA_DOCUMENTO(+) " +
			" AND    D.ID_STATO_DOCUMENTO = TSD.ID_STATO_DOCUMENTO(+) ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Value of parameter 1 [ID_DOCUMENTO] in findDocumentoVOByPrimaryKey method in DocumentoDAO: "+idDocumento+"\n");
			stmt.setLong(1, idDocumento.longValue());

			SolmrLogger.debug(this, "Executing findDocumentoVOByPrimaryKey: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) 
			{
				documentoVO = new DocumentoVO();
				documentoVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
				documentoVO.setExtIdDocumento(new Long(rs.getLong("EXT_ID_DOCUMENTO")));
				CodeDescription tipoTipologiaDocumento = 
				  new CodeDescription(Integer.decode(rs.getString("EXT_ID_DOCUMENTO")), rs.getString("TIPOLOGIA_DESC"));
				documentoVO.setTipoTipologiaDocumento(tipoTipologiaDocumento);
				TipoCategoriaDocumentoVO tipoCategoriaDocumentoVO = new TipoCategoriaDocumentoVO();
				tipoCategoriaDocumentoVO.setIdCategoriaDocumento(new Long(rs.getLong("ID_CATEGORIA_DOCUMENTO")));
				tipoCategoriaDocumentoVO.setDescrizione(rs.getString("CATEGORIA_DESC"));
				tipoCategoriaDocumentoVO.setTipoIdentificativo(rs.getString("TIPO_IDENTIFICATIVO"));
				tipoCategoriaDocumentoVO.setIdentificativo(rs.getString("IDENTIFICATIVO"));
				documentoVO.setTipoCategoriaDocumentoVO(tipoCategoriaDocumentoVO);
				TipoDocumentoVO tipoDocumentoVO = new TipoDocumentoVO();
				tipoDocumentoVO.setIdDocumento(new Long(rs.getLong("ID_TIPO")));
				tipoDocumentoVO.setDescrizione(rs.getString("TIPO_DESC"));
				tipoDocumentoVO.setExtIdTipoDocumentoIndex(checkLongNull(rs.getString("EXT_ID_TIPO_DOCUMENTO_INDEX")));
				tipoDocumentoVO.setFlagAnnullabile(rs.getString("FLAG_ANNULLABILE"));
				documentoVO.setTipoDocumentoVO(tipoDocumentoVO);
				if(Validator.isNotEmpty(rs.getString("ID_STATO_DOCUMENTO"))) {
					documentoVO.setIdStatoDocumento(new Long(rs.getLong("ID_STATO_DOCUMENTO")));
					documentoVO.setDescStatoDocumento(rs.getString("DESC_STATO_DOCUMENTO"));
				}
				//se null metto Attivo
				else
				{
				  documentoVO.setDescStatoDocumento("Attivo");
				}
				documentoVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
				documentoVO.setCuaa(rs.getString("CUAA"));
				documentoVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
				documentoVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
				documentoVO.setNumeroProtocollo(rs.getString("NUMERO_PROTOCOLLO"));
				documentoVO.setDataProtocollo(rs.getTimestamp("DATA_PROTOCOLLO"));
				documentoVO.setNumeroDocumento(rs.getString("NUMERO_DOCUMENTO"));
				documentoVO.setEnteRilascioDocumento(rs.getString("ENTE_RILASCIO_DOCUMENTO"));
				if(Validator.isNotEmpty(rs.getString("ID_DOCUMENTO_PRECEDENTE"))) {
					documentoVO.setIdDocumentoPrecedente(new Long(rs.getLong("ID_DOCUMENTO_PRECEDENTE")));
				}
				documentoVO.setNote(rs.getString("NOTE"));
				documentoVO.setDataUltimoAggiornamento(rs.getTimestamp("DATA_ULTIMO_AGGIORNAMENTO"));
				documentoVO.setUtenteUltimoAggiornamento(new Long(rs.getLong("UTENTE_ULTIMO_AGGIORNAMENTO")));
				UtenteIrideVO utenteIrideVO = new UtenteIrideVO();
				utenteIrideVO.setIdUtente(new Long(rs.getLong("UTENTE_ULTIMO_AGGIORNAMENTO")));
				utenteIrideVO.setDenominazione(rs.getString("DENOMINAZIONE"));
				utenteIrideVO.setDescrizioneEnteAppartenenza(rs.getString("DESCRIZIONE_ENTE_APPARTENENZA"));
				utenteIrideVO.setCodiceEnte(rs.getString("CODICE_ENTE"));
				documentoVO.setUtenteAggiornamento(utenteIrideVO);
				documentoVO.setDataInserimento(rs.getTimestamp("DATA_INSERIMENTO"));
				documentoVO.setDataVariazioneStato(rs.getTimestamp("DATA_VARIAZIONE_STATO"));
				if(Validator.isNotEmpty(rs.getString("ID_UTENTE_AGGIORNAMENTO_SRV"))) {
					documentoVO.setIdUtenteAggiornamentoSrv(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO_SRV")));
				}
				documentoVO.setNumeroProtocolloEsterno(rs.getString("NUMERO_PROTOCOLLO_ESTERNO"));
				documentoVO.setCuaaSoccidario(rs.getString("CUAA_SOCCIDARIO"));
				documentoVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
				documentoVO.setDataEsecuzione(rs.getTimestamp("DATA_ESECUZIONE"));
				documentoVO.setIdContoCorrente(checkLongNull(rs.getString("ID_CONTO_CORRENTE")));
				documentoVO.setIdCausaleModificaDocumento(checkLongNull(rs.getString("ID_CAUSALE_MODIFICA_DOCUMENTO")));
				documentoVO.setDescCausaleModificaDocumento(rs.getString("DESC_CAUSALE_DOC"));
				
				Long identificativo = checkLongNull(rs.getString("IDENTIFICATIVO"));
        String tipoIdentificativo = rs.getString("TIPO_IDENTIFICATIVO");
				
        documentoVO.setFlagIstanzaRiesame("N");
        if(Validator.isNotEmpty(identificativo) && Validator.isNotEmpty(tipoIdentificativo))
        {
          if((identificativo.longValue() == 518)
              && tipoIdentificativo.equalsIgnoreCase("TC"))
          {
            documentoVO.setFlagIstanzaRiesame("S");
          }
            
        }
				
			}

			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "findDocumentoVOByPrimaryKey in DocumentoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "findDocumentoVOByPrimaryKey in DocumentoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.error(this, "findDocumentoVOByPrimaryKey in DocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.error(this, "findDocumentoVOByPrimaryKey in DocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated findDocumentoVOByPrimaryKey method in DocumentoDAO\n");
		return documentoVO;
	}

	/**
	 * Metodo che si occupa di inserire un record sulla tabella DB_DOCUMENTO_PROPRIETARIO
	 * @param documentoProprietarioVO DocumentoProprietarioVO
	 * @param ruoloUtenza RuoloUtenza
	 * @return Long
	 * @throws DataAccessException
	 */
	public Long insertDocumentoProprietario(DocumentoProprietarioVO documentoProprietarioVO, RuoloUtenza ruoloUtenza) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating insertDocumentoProprietario method in DocumentoDAO\n");
		Long primaryKey = null;
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			SolmrLogger.debug(this, "Creating primary key in method insertDocumentoProprietario in DocumentoDAO\n");
			primaryKey = getNextPrimaryKey((String)SolmrConstants.get("SEQ_DOCUMENTO_PROPRIETARIO"));
			SolmrLogger.debug(this, "Created primary key in method insertDocumentoProprietario in DocumentoDAO with value: "+primaryKey+"\n");

			SolmrLogger.debug(this, "Creating db-connection in insertDocumentoProprietario method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in insertDocumentoProprietario method in DocumentoDAO and it values: "+conn+"\n");


			String insert = " INSERT INTO DB_DOCUMENTO_PROPRIETARIO "+
			" (ID_DOCUMENTO_PROPRIETARIO, ID_DOCUMENTO, CUAA, DENOMINAZIONE, DATA_ULTIMO_AGGIORNAMENTO, "+
			"  UTENTE_ULTIMO_AGGIORNAMENTO, DATA_INSERIMENTO, FLAG_VALIDATO) " +
			"  VALUES (?, ?, ?, ?, SYSDATE, ?, SYSDATE, ?)";

			stmt = conn.prepareStatement(insert);

			stmt.setLong(1, primaryKey.longValue());
			SolmrLogger.debug(this,"Value of parameter 2 [ID_DOCUMENTO] in insertDocumentoProprietario method in DocumentoDAO: "+documentoProprietarioVO.getIdDocumento()+"\n");
			stmt.setLong(2, documentoProprietarioVO.getIdDocumento().longValue());
			SolmrLogger.debug(this,"Value of parameter 3 [CUAA] in insertDocumentoProprietario method in DocumentoDAO: "+documentoProprietarioVO.getCuaa()+"\n");
			stmt.setString(3, documentoProprietarioVO.getCuaa().toUpperCase());
			SolmrLogger.debug(this,"Value of parameter 4 [DENOMINAZIONE] in insertDocumentoProprietario method in DocumentoDAO: "+documentoProprietarioVO.getDenominazione()+"\n");
			stmt.setString(4, documentoProprietarioVO.getDenominazione());
			SolmrLogger.debug(this,"Value of parameter 5 [UTENTE_ULTIMO_AGGIORNAMENTO] in insertDocumentoProprietario method in DocumentoDAO: "+ruoloUtenza.getIdUtente()+"\n");
			stmt.setLong(5, ruoloUtenza.getIdUtente().longValue());
			SolmrLogger.debug(this,"Value of parameter 6 [FLAG_VALIDATO] in insertDocumentoProprietario method in DocumentoDAO: "+documentoProprietarioVO.getFlagValidato()+"\n");
			stmt.setString(6, documentoProprietarioVO.getFlagValidato());

			SolmrLogger.debug(this, "Executing insertDocumentoProprietario: "+insert+"\n");

			stmt.executeUpdate();

			stmt.close();

			SolmrLogger.debug(this, "Executed insertDocumentoProprietario method in DocumentoDAO\n");
		}
		catch (SQLException se) {
			SolmrLogger.error(this, "insertDocumento in insertDocumentoProprietario - SQLException: "+se.getMessage()+"\n");
			throw new DataAccessException(se.getMessage());
		}
		catch (Exception ex) {
			SolmrLogger.error(this, "Generic Exception in insertDocumentoProprietario: "+ex.getMessage()+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.error(this, "SQLException while closing Statement and Connection in insertDocumentoProprietario: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in insertDocumentoProprietario: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		return primaryKey;
	}

	/**
	 * Metodo che si occupa di inserire un record nella tabella DB_DOCUMENTO_CONDUZIONE
	 * @param documentoConduzioneVO DocumentoConduzioneVO
	 * @return Long
	 * @throws DataAccessException
	 */
	public Long insertDocumentoConduzione(DocumentoConduzioneVO documentoConduzioneVO) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating insertDocumentoConduzione method in DocumentoDAO\n");
		Long primaryKey = null;
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			SolmrLogger.debug(this, "Creating primary key in method insertDocumentoConduzione in DocumentoDAO\n");
			primaryKey = getNextPrimaryKey((String)SolmrConstants.get("SEQ_DOCUMENTO_CONDUZIONE"));
			SolmrLogger.debug(this, "Created primary key in method insertDocumentoConduzione in DocumentoDAO with value: "+primaryKey+"\n");

			SolmrLogger.debug(this, "Creating db-connection in insertDocumentoConduzione method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in insertDocumentoConduzione method in DocumentoDAO and it values: "+conn+"\n");


			String insert = " INSERT INTO DB_DOCUMENTO_CONDUZIONE (ID_DOCUMENTO_CONDUZIONE, " +
			"                                      ID_CONDUZIONE_PARTICELLA, " +
			"                                      ID_DOCUMENTO, " +
			"                                      DATA_INSERIMENTO, " +
			"                                      DATA_INIZIO_VALIDITA, " +
			"                                      DATA_FINE_VALIDITA," +
			"                                      NOTE," +
			"                                      LAVORAZIONE_PRIORITARIA) " +
			"  VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

			stmt = conn.prepareStatement(insert);

			stmt.setLong(1, primaryKey.longValue());
			SolmrLogger.debug(this,"Value of parameter 2 [ID_CONDUZIONE_PARTICELLA] in insertDocumentoConduzione method in DocumentoDAO: "+documentoConduzioneVO.getIdConduzioneParticella()+"\n");
			stmt.setLong(2, documentoConduzioneVO.getIdConduzioneParticella().longValue());
			SolmrLogger.debug(this,"Value of parameter 3 [ID_DOCUMENTO] in insertDocumentoConduzione method in DocumentoDAO: "+documentoConduzioneVO.getIdDocumento()+"\n");
			stmt.setLong(3, documentoConduzioneVO.getIdDocumento().longValue());
			SolmrLogger.debug(this,"Value of parameter 4 [DATA_INSERIMENTO] in insertDocumentoConduzione method in DocumentoDAO: "+new Timestamp(documentoConduzioneVO.getDataInserimento().getTime())+"\n");
			stmt.setTimestamp(4, new Timestamp(documentoConduzioneVO.getDataInserimento().getTime()));
			SolmrLogger.debug(this,"Value of parameter 5 [DATA_INIZIO_VALIDITA] in insertDocumentoConduzione method in DocumentoDAO: "+new Timestamp(documentoConduzioneVO.getDataInizioValidita().getTime())+"\n");
			stmt.setTimestamp(5, new Timestamp(documentoConduzioneVO.getDataInizioValidita().getTime()));
			if(documentoConduzioneVO.getDataFineValidita() != null) {
				stmt.setTimestamp(6, new Timestamp(documentoConduzioneVO.getDataFineValidita().getTime()));
				SolmrLogger.debug(this,"Value of parameter 6 [DATA_FINE_VALIDITA] in insertDocumentoConduzione method in DocumentoDAO: "+new Timestamp(documentoConduzioneVO.getDataFineValidita().getTime())+"\n");
			}
			else {
				stmt.setString(6, null);
				SolmrLogger.debug(this,"Value of parameter 6 [DATA_FINE_VALIDITA] in insertDocumentoConduzione method in DocumentoDAO: "+null+"\n");
			}
			stmt.setString(7, documentoConduzioneVO.getNote());
			stmt.setString(8, documentoConduzioneVO.getLavorazionePrioritaria());

			SolmrLogger.debug(this, "Executing insertDocumentoConduzione: "+insert+"\n");

			stmt.executeUpdate();

			stmt.close();

			SolmrLogger.debug(this, "Executed insertDocumentoConduzione method in DocumentoDAO\n");
		}
		catch (SQLException se) {
			SolmrLogger.error(this, "insertDocumento in insertDocumentoConduzione - SQLException: "+se.getMessage()+"\n");
			throw new DataAccessException(se.getMessage());
		}
		catch (Exception ex) {
			SolmrLogger.error(this, "Generic Exception in insertDocumentoConduzione: "+ex.getMessage()+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.error(this, "SQLException while closing Statement and Connection in insertDocumentoConduzione: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in insertDocumentoConduzione: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		return primaryKey;
	}

	/**
	 * Metodo che si occupa di reperire i proprietari del documento
	 * @param idDocumento Long
	 * @return Vector
	 * @throws DataAccessException
	 */
	public Vector<DocumentoProprietarioVO> getListProprietariDocumento(Long idDocumento) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListProprietariDocumento method in DocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<DocumentoProprietarioVO> elencoProprietari = new Vector<DocumentoProprietarioVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListProprietariDocumento method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListProprietariDocumento method in DocumentoDAO and it values: "+conn+"\n");

			String query = " SELECT   ID_DOCUMENTO_PROPRIETARIO, " +
			"          ID_DOCUMENTO, " +
			"          CUAA, " +
			"          DENOMINAZIONE, " +
			"          DATA_ULTIMO_AGGIORNAMENTO, " +
			"          UTENTE_ULTIMO_AGGIORNAMENTO, " +
			"          DATA_INSERIMENTO, " +
			"          FLAG_VALIDATO " +
			" FROM     DB_DOCUMENTO_PROPRIETARIO " +
			" WHERE    ID_DOCUMENTO = ? ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Value of parameter 1 [ID_DOCUMENTO] in getListProprietariDocumento method in DocumentoDAO: "+idDocumento+"\n");
			stmt.setLong(1, idDocumento.longValue());


			SolmrLogger.debug(this, "Executing getListProprietariDocumento: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				DocumentoProprietarioVO documentoProprietarioVO = new DocumentoProprietarioVO();
				documentoProprietarioVO.setIdDocumentoProprietario(new Long(rs.getLong(1)));
				documentoProprietarioVO.setIdDocumento(new Long(rs.getLong(2)));
				documentoProprietarioVO.setCuaa(rs.getString(3));
				documentoProprietarioVO.setDenominazione(rs.getString(4));
				documentoProprietarioVO.setDataUltimoAggiornamento(new java.util.Date(rs.getDate(5).getTime()));
				documentoProprietarioVO.setUtenteUltimoAggiornamento(new Long(rs.getLong(6)));
				documentoProprietarioVO.setDataInserimento(new java.util.Date(rs.getDate(7).getTime()));
				documentoProprietarioVO.setFlagValidato(rs.getString("FLAG_VALIDATO"));
				elencoProprietari.add(documentoProprietarioVO);
			}

			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListProprietariDocumento in DocumentoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListProprietariDocumento in DocumentoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.error(this, "getListProprietariDocumento in DocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.error(this, "getListProprietariDocumento in DocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListTipoDocumentoByIdTipologiaDocumento method in DocumentoDAO\n");
		return elencoProprietari;
	}

	/**
	 * Metodo che si occupa di reperire tutti le conduzioni legate al documento
	 *
	 * @param idDocumento Long
	 * @return Vector
	 * @throws DataAccessException
	 * @throws SolmrException
	 */
	public Vector<DocumentoConduzioneVO> getListDocumentoConduzioni(Long idDocumento, boolean onlyActive) throws DataAccessException
	{
		SolmrLogger.debug(this, "Invocating getListDocumentoConduzioni method in DocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<DocumentoConduzioneVO> elencoConduzioni = new Vector<DocumentoConduzioneVO>();

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in getListDocumentoConduzioni method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListDocumentoConduzioni method in DocumentoDAO and it values: "+conn+"\n");
			
			String query = "";
			if(onlyActive) {
				query = 
				  "SELECT DC.ID_DOCUMENTO_CONDUZIONE, " +
					"       DC.ID_CONDUZIONE_PARTICELLA, " +
					"       DC.ID_DOCUMENTO, " +
					"       DC.DATA_FINE_VALIDITA," +
					"       CP.ID_PARTICELLA  " +
					"FROM   DB_DOCUMENTO_CONDUZIONE DC, " +
					"       DB_CONDUZIONE_PARTICELLA CP " +
					"WHERE  DC.ID_DOCUMENTO = ? " +
					"AND    DC.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA " +
					"AND    NVL(DC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > TRUNC(SYSDATE) ";
			}
			else 
			{
				query = 
				  "SELECT DC.ID_DOCUMENTO_CONDUZIONE, " +
					"       DC.ID_CONDUZIONE_PARTICELLA, " +
					"       DC.ID_DOCUMENTO, " +
					"       CP.ID_PARTICELLA,  " +
					"       MAX(DC.DATA_FINE_VALIDITA) AS DATA_FINE_VALIDITA " +
					"FROM   DB_DOCUMENTO_CONDUZIONE DC, " +
					"       DB_CONDUZIONE_PARTICELLA CP " +
					"WHERE  DC.ID_DOCUMENTO = ? " +
					"AND    DC.ID_CONDUZIONE_PARTICELLA = CP.ID_CONDUZIONE_PARTICELLA " +
					"GROUP BY DC.ID_DOCUMENTO_CONDUZIONE, " +
					"         DC.ID_CONDUZIONE_PARTICELLA, " +
					"         DC.ID_DOCUMENTO, " +
					"         CP.ID_PARTICELLA ";
			}

			stmt = conn.prepareStatement(query);
			
			// Setto il timeout possibile della query
			stmt.setQueryTimeout(SolmrConstants.MAX_TIME_WAIT);

			SolmrLogger.debug(this, "Value of parameter 1 [ID_DOCUMENTO] in getListDocumentoConduzioni method in DocumentoDAO: "+idDocumento+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ONLY_ACTIVE] in getListDocumentoConduzioni method in DocumentoDAO: "+onlyActive+"\n");
			stmt.setLong(1, idDocumento.longValue());

			SolmrLogger.debug(this, "Executing getListDocumentoConduzioni: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				DocumentoConduzioneVO documentoConduzioneVO = new DocumentoConduzioneVO();
				documentoConduzioneVO.setIdDocumentoConduzione(new Long(rs.getLong("ID_DOCUMENTO_CONDUZIONE")));
				documentoConduzioneVO.setIdConduzioneParticella(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));
				documentoConduzioneVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
				documentoConduzioneVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
				documentoConduzioneVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				elencoConduzioni.add(documentoConduzioneVO);
			}

			rs.close();
			stmt.close();

		}
		catch(SQLException exc) 
		{
			SolmrLogger.error(this, "getListDocumentoConduzioni in DocumentoDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) 
		{
			SolmrLogger.error(this, "getListDocumentoConduzioni in DocumentoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.error(this, "getListDocumentoConduzioni in DocumentoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.error(this, "getListDocumentoConduzioni in DocumentoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListDocumentoConduzioni method in DocumentoDAO\n");
		return elencoConduzioni;
	}

	/**
	 * Metodo che si occupa di storicizzare il record della tabella DB_DOCUMENTO
	 * @param documentoVO DocumentoVO
	 * @param RuoloUtenza ruoloUtenza
	 * @throws DataAccessException
	 */
	public void storicizzaDocumento(DocumentoVO documentoVO, long idUtenteAggiornamento) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating storicizzaDocumento method in DocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;

		try {

			SolmrLogger.debug(this, "Creating db-connection in storicizzaDocumento method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in storicizzaDocumento method in DocumentoDAO and it values: "+conn+"\n");


			String query = " UPDATE DB_DOCUMENTO " +
			" SET    ID_STATO_DOCUMENTO = ?,  " +
			"        NOTE = ?, " +
			"        DATA_ULTIMO_AGGIORNAMENTO = SYSDATE, " +
			"        UTENTE_ULTIMO_AGGIORNAMENTO = ?, " +
			"        DATA_VARIAZIONE_STATO = SYSDATE " +
			" WHERE  ID_DOCUMENTO = ? ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this,"Value of parameter 1 [ID_STATO_DOCUMENTO] in storicizzaDocumento method in DocumentoDAO: "+documentoVO.getIdStatoDocumento()+"\n");
			stmt.setLong(1, documentoVO.getIdStatoDocumento().longValue());
			SolmrLogger.debug(this,"Value of parameter 2 [NOTE] in storicizzaDocumento method in DocumentoDAO: "+documentoVO.getNote()+"\n");
			stmt.setString(2, documentoVO.getNote());
			SolmrLogger.debug(this,"Value of parameter 3 [UTENTE_ULTIMO_AGGIORNAMENTO] in storicizzaDocumento method in DocumentoDAO: "+idUtenteAggiornamento+"\n");
			stmt.setLong(3, idUtenteAggiornamento);
			SolmrLogger.debug(this,"Value of parameter 4 [ID_DOCUMENTO] in storicizzaDocumento method in DocumentoDAO: "+documentoVO.getIdDocumento()+"\n");
			stmt.setLong(4, documentoVO.getIdDocumento().longValue());

			SolmrLogger.debug(this, "Executing storicizzaDocumento: "+query+"\n");

			stmt.executeUpdate();

			stmt.close();

			SolmrLogger.debug(this, "Executed storicizzaDocumento method in DocumentoDAO\n");
		}
		catch (SQLException se) {
			SolmrLogger.error(this, "storicizzaDocumento in DocumentoDAO - SQLException: "+se.getMessage()+"\n");
			throw new DataAccessException(se.getMessage());
		}
		catch (Exception ex) {
			SolmrLogger.error(this, "Generic Exception in storicizzaDocumento: "+ex.getMessage()+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.error(this, "SQLException while closing Statement and Connection in storicizzaDocumento: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in storicizzaDocumento: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
	}
	
	/**
   * Metodo che si occupa di storicizzare il record della tabella DB_DOCUMENTO
   * ad esclusione del campo note..
   * 
   * @param documentoVO DocumentoVO
   * @param RuoloUtenza ruoloUtenza
   * @throws DataAccessException
   */
  public void storicizzaDocumentoNoNote(DocumentoVO documentoVO, RuoloUtenza ruoloUtenza) throws DataAccessException {
    SolmrLogger.debug(this, "Invocating storicizzaDocumento method in DocumentoDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;

    try {

      SolmrLogger.debug(this, "Creating db-connection in storicizzaDocumento method in DocumentoDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in storicizzaDocumento method in DocumentoDAO and it values: "+conn+"\n");


      String query = " UPDATE DB_DOCUMENTO " +
      " SET    ID_STATO_DOCUMENTO = ?,  " +
      "        DATA_ULTIMO_AGGIORNAMENTO = SYSDATE, " +
      "        UTENTE_ULTIMO_AGGIORNAMENTO = ?, " +
      "        DATA_VARIAZIONE_STATO = SYSDATE " +
      " WHERE  ID_DOCUMENTO = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,"Value of parameter 1 [ID_STATO_DOCUMENTO] in storicizzaDocumento method in DocumentoDAO: "+documentoVO.getIdStatoDocumento()+"\n");
      stmt.setLong(1, documentoVO.getIdStatoDocumento().longValue());
      SolmrLogger.debug(this,"Value of parameter 2 [UTENTE_ULTIMO_AGGIORNAMENTO] in storicizzaDocumento method in DocumentoDAO: "+ruoloUtenza.getIdUtente().longValue()+"\n");
      stmt.setLong(2, ruoloUtenza.getIdUtente().longValue());
      SolmrLogger.debug(this,"Value of parameter 3 [ID_DOCUMENTO] in storicizzaDocumento method in DocumentoDAO: "+documentoVO.getIdDocumento()+"\n");
      stmt.setLong(3, documentoVO.getIdDocumento().longValue());

      SolmrLogger.debug(this, "Executing storicizzaDocumento: "+query+"\n");

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed storicizzaDocumento method in DocumentoDAO\n");
    }
    catch (SQLException se) {
      SolmrLogger.error(this, "storicizzaDocumento in DocumentoDAO - SQLException: "+se.getMessage()+"\n");
      throw new DataAccessException(se.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.error(this, "Generic Exception in storicizzaDocumento: "+ex.getMessage()+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.error(this, "SQLException while closing Statement and Connection in storicizzaDocumento: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in storicizzaDocumento: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

	/**
	 * Metodo che si occupa di eliminare tutti i proprietari collegati ad un dato documento
	 * @param idDocumento Long
	 * @throws DataAccessException
	 */
	public void deleteProprietariDocumento(Long idDocumento) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating deleteProprietariDocumento method in DocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;

		try {

			SolmrLogger.debug(this, "Creating db-connection in deleteProprietariDocumento method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in deleteProprietariDocumento method in DocumentoDAO and it values: "+conn+"\n");


			String query = " DELETE "+
			" FROM   DB_DOCUMENTO_PROPRIETARIO " +
			" WHERE  ID_DOCUMENTO = ? ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this,"Value of parameter 1 [ID_DOCUMENTO] in deleteProprietariDocumento method in DocumentoDAO: "+idDocumento+"\n");
			stmt.setLong(1, idDocumento.longValue());

			SolmrLogger.debug(this, "Executing deleteProprietariDocumento: "+query+"\n");

			stmt.executeUpdate();

			stmt.close();

			SolmrLogger.debug(this, "Executed deleteProprietariDocumento method in DocumentoDAO\n");
		}
		catch (SQLException se) {
			SolmrLogger.error(this, "deleteProprietariDocumento in DocumentoDAO - SQLException: "+se.getMessage()+"\n");
			throw new DataAccessException(se.getMessage());
		}
		catch (Exception ex) {
			SolmrLogger.error(this, "Generic Exception in deleteProprietariDocumento: "+ex.getMessage()+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.error(this, "SQLException while closing Statement and Connection in deleteProprietariDocumento: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in deleteProprietariDocumento: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
	}

	/**
	 * Metodo che si occupa di eliminare tutte le conduzioni associate al documento
	 * @param idDocumento Long
	 * @throws DataAccessException
	 */
	public void deleteConduzioniDocumento(Long idDocumento) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating deleteConduzioniDocumento method in DocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;

		try {

			SolmrLogger.debug(this, "Creating db-connection in deleteConduzioniDocumento method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in deleteConduzioniDocumento method in DocumentoDAO and it values: "+conn+"\n");


			String query = " DELETE "+
			" FROM   DB_DOCUMENTO_CONDUZIONE " +
			" WHERE  ID_DOCUMENTO = ? ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this,"Value of parameter 1 [ID_DOCUMENTO] in deleteConduzioniDocumento method in DocumentoDAO: "+idDocumento+"\n");
			stmt.setLong(1, idDocumento.longValue());

			SolmrLogger.debug(this, "Executing deleteConduzioniDocumento: "+query+"\n");

			stmt.executeUpdate();

			stmt.close();

			SolmrLogger.debug(this, "Executed deleteConduzioniDocumento method in DocumentoDAO\n");
		}
		catch (SQLException se) {
			SolmrLogger.error(this, "deleteConduzioniDocumento in DocumentoDAO - SQLException: "+se.getMessage()+"\n");
			throw new DataAccessException(se.getMessage());
		}
		catch (Exception ex) {
			SolmrLogger.error(this, "Generic Exception in deleteConduzioniDocumento: "+ex.getMessage()+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.error(this, "SQLException while closing Statement and Connection in deleteConduzioniDocumento: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in deleteConduzioniDocumento: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
	}
	

	/**
	 * Metodo che si occupa di eliminare un record dalla tabella DB_DOCUMENTO
	 * @param idDocumento Long
	 * @throws DataAccessException
	 */
	public void deleteDocumento(Long idDocumento) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating deleteDocumento method in DocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;

		try {

			SolmrLogger.debug(this, "Creating db-connection in deleteDocumento method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in deleteDocumento method in DocumentoDAO and it values: "+conn+"\n");


			String query = " DELETE "+
			" FROM   DB_DOCUMENTO " +
			" WHERE  ID_DOCUMENTO = ? ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this,"Value of parameter 1 [ID_DOCUMENTO] in deleteDocumento method in DocumentoDAO: "+idDocumento+"\n");
			stmt.setLong(1, idDocumento.longValue());

			SolmrLogger.debug(this, "Executing deleteDocumento: "+query+"\n");

			stmt.executeUpdate();

			stmt.close();

			SolmrLogger.debug(this, "Executed deleteDocumento method in DocumentoDAO\n");
		}
		catch (SQLException se) {
			SolmrLogger.error(this, "deleteDocumento in DocumentoDAO - SQLException: "+se.getMessage()+"\n");
			throw new DataAccessException(se.getMessage());
		}
		catch (Exception ex) {
			SolmrLogger.error(this, "Generic Exception in deleteDocumento: "+ex.getMessage()+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.error(this, "SQLException while closing Statement and Connection in deleteDocumento: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in deleteDocumento: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
	}

	/**
	 * Metodo che si occupa di modificare il documento assegnando il numero di protocollazione
	 */
	public void protocollaDocumento(DocumentoVO documentoVO, long idUtenteAggiornamento) 
	    throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating protocollaDocumento method in DocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;

		try {

			SolmrLogger.debug(this, "Creating db-connection in protocollaDocumento method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in protocollaDocumento method in DocumentoDAO and it values: "+conn+"\n");


			String query = " UPDATE DB_DOCUMENTO "+
			" SET    NUMERO_PROTOCOLLO = ?,  " +
			"        DATA_PROTOCOLLO = TRUNC(SYSDATE), " +
			"        DATA_ULTIMO_AGGIORNAMENTO = SYSDATE, " +
			"        UTENTE_ULTIMO_AGGIORNAMENTO = ? " +
			" WHERE  ID_DOCUMENTO = ? ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this,"Value of parameter 1 [NUMERO_PROTOCOLLO] in protocollaDocumento method in DocumentoDAO: "+documentoVO.getNumeroProtocollo()+"\n");
			stmt.setString(1, documentoVO.getNumeroProtocollo());
			SolmrLogger.debug(this,"Value of parameter 2 [UTENTE_ULTIMO_AGGIORNAMENTO] in protocollaDocumento method in DocumentoDAO: "+idUtenteAggiornamento+"\n");
			stmt.setLong(2, idUtenteAggiornamento);
			SolmrLogger.debug(this,"Value of parameter 3 [ID_DOCUMENTO] in protocollaDocumento method in DocumentoDAO: "+documentoVO.getIdDocumento()+"\n");
			stmt.setLong(3, documentoVO.getIdDocumento().longValue());

			SolmrLogger.debug(this, "Executing protocollaDocumento: "+query+"\n");

			stmt.executeUpdate();

			stmt.close();

			SolmrLogger.debug(this, "Executed protocollaDocumento method in DocumentoDAO\n");
		}
		catch (SQLException se) {
			SolmrLogger.error(this, "protocollaDocumento in DocumentoDAO - SQLException: "+se.getMessage()+"\n");
			throw new DataAccessException(se.getMessage());
		}
		catch (Exception ex) {
			SolmrLogger.error(this, "Generic Exception in protocollaDocumento: "+ex.getMessage()+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.error(this, "SQLException while closing Statement and Connection in protocollaDocumento: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in protocollaDocumento: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
	}

	/**
	 * Metodo che si occupa di reperire il documento di assegnazione mandato relativo ad un'azienda agricola
	 * @param anagAziendaVO AnagAziendaVO
	 * @param idTipoDocumento Long
	 * @return DocumentoVO
	 * @throws DataAccessException
	 */
	public DocumentoVO findDocumentoMandatoOrRevocaAssistenza(AnagAziendaVO anagAziendaVO, Long idTipoDocumento) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating findDocumentoVOBydDatiAnagrafici method in DocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		DocumentoVO documentoVO = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in findDocumentoMandatoAssistenza method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in findDocumentoMandatoAssistenza method in DocumentoDAO and it values: "+conn+"\n");

			String query = " SELECT D.ID_DOCUMENTO, " +
			"        D.EXT_ID_DOCUMENTO, " +
			"        D.ID_STATO_DOCUMENTO, " +
			"        D.ID_AZIENDA, " +
			"        D.CUAA, " +
			"        D.DATA_INIZIO_VALIDITA, " +
			"        D.DATA_FINE_VALIDITA, " +
			"        D.NUMERO_PROTOCOLLO, " +
			"        D.DATA_PROTOCOLLO, " +
			"        D.NUMERO_DOCUMENTO, " +
			"        D.ENTE_RILASCIO_DOCUMENTO, " +
			"        D.ID_DOCUMENTO_PRECEDENTE, " +
			"        D.NOTE, " +
			"        D.DATA_ULTIMO_AGGIORNAMENTO, " +
			"        D.UTENTE_ULTIMO_AGGIORNAMENTO, " +
			"        D.DATA_INSERIMENTO, " +
			"        D.DATA_VARIAZIONE_STATO " +
			" FROM   DB_DOCUMENTO D " +
			" WHERE  D.ID_AZIENDA = ? " +
			" AND    D.CUAA = ? " +
			" AND    D.ID_STATO_DOCUMENTO IS NULL " +
			" AND    D.EXT_ID_DOCUMENTO = ? ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in findDocumentoMandatoAssistenza method in DocumentoDAO: "+anagAziendaVO.getIdAzienda()+"\n");
			stmt.setLong(1, anagAziendaVO.getIdAzienda().longValue());
			SolmrLogger.debug(this, "Value of parameter 2 [CUAA] in findDocumentoMandatoAssistenza method in DocumentoDAO: "+anagAziendaVO.getCUAA()+"\n");
			stmt.setString(2, anagAziendaVO.getCUAA());
			SolmrLogger.debug(this, "Value of parameter 3 [EXT_ID_DOCUMENTO] in findDocumentoMandatoAssistenza method in DocumentoDAO: "+idTipoDocumento+"\n");
			stmt.setLong(3, idTipoDocumento.longValue());

			SolmrLogger.debug(this, "Executing findDocumentoMandatoAssistenza: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) {
				documentoVO = new DocumentoVO();
				documentoVO.setIdDocumento(new Long(rs.getLong(1)));
				documentoVO.setExtIdDocumento(new Long(rs.getLong(2)));
				if(Validator.isNotEmpty(rs.getString(3))) {
					documentoVO.setIdStatoDocumento(new Long(rs.getLong(3)));
				}
				documentoVO.setIdAzienda(new Long(rs.getLong(4)));
				documentoVO.setCuaa(rs.getString(5));
				documentoVO.setDataInizioValidita(new java.util.Date(rs.getDate(6).getTime()));
				if(Validator.isNotEmpty(rs.getString(7))) {
					documentoVO.setDataFineValidita(new java.util.Date(rs.getDate(7).getTime()));
				}
				documentoVO.setNumeroProtocollo(rs.getString(8));
				if(Validator.isNotEmpty(rs.getString(9))) {
					documentoVO.setDataProtocollo(new java.util.Date(rs.getDate(9).getTime()));
				}
				documentoVO.setNumeroDocumento(rs.getString(10));
				documentoVO.setEnteRilascioDocumento(rs.getString(11));
				if(Validator.isNotEmpty(rs.getString(12))) {
					documentoVO.setIdDocumentoPrecedente(new Long(rs.getLong(12)));
				}
				documentoVO.setNote(rs.getString(13));
				if(Validator.isNotEmpty(rs.getString(14))) {
					documentoVO.setDataUltimoAggiornamento(new java.util.Date(rs.getDate(14).getTime()));
				}
				documentoVO.setUtenteUltimoAggiornamento(new Long(rs.getLong(15)));
				documentoVO.setDataInserimento(new java.util.Date(rs.getDate(16).getTime()));
				if(Validator.isNotEmpty(rs.getString(17))) {
					documentoVO.setDataVariazioneStato(new java.util.Date(rs.getDate(17).getTime()));
				}
			}

			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "findDocumentoMandatoAssistenza in DocumentoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "findDocumentoMandatoAssistenza in DocumentoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.error(this, "findDocumentoMandatoAssistenza in DocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.error(this, "findDocumentoMandatoAssistenza in DocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated findDocumentoMandatoAssistenza method in DocumentoDAO\n");
		return documentoVO;
	}

	/**
	 * Metodo che si occupa di eliminare tutte le particelle associate al documento a partire dall'id_conduzione_particella: utilizzato per
	 * eliminare i records nel momento in cui l'utente decide di rimuovere fisicamente la particella da anagrafe
	 * @param idConduzioneParticella Long
	 * @throws DataAccessException
	 */
	public void deleteConduzioniDocumentoByIdConduzioneParticella(Long idConduzioneParticella) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating deleteConduzioniDocumentoByIdConduzioneParticella method in DocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;

		try {

			SolmrLogger.debug(this, "Creating db-connection in deleteConduzioniDocumentoByIdConduzioneParticella method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in deleteConduzioniDocumentoByIdConduzioneParticella method in DocumentoDAO and it values: "+conn+"\n");


			String query = " DELETE "+
			" FROM   DB_DOCUMENTO_CONDUZIONE " +
			" WHERE  ID_CONDUZIONE_PARTICELLA = ? ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this,"Value of parameter 1 [ID_DOCUMENTO] in deleteConduzioniDocumentoByIdConduzioneParticella method in DocumentoDAO: "+idConduzioneParticella+"\n");
			stmt.setLong(1, idConduzioneParticella.longValue());

			SolmrLogger.debug(this, "Executing deleteConduzioniDocumentoByIdConduzioneParticella: "+query+"\n");

			stmt.executeUpdate();

			stmt.close();

			SolmrLogger.debug(this, "Executed deleteConduzioniDocumentoByIdConduzioneParticella method in DocumentoDAO\n");
		}
		catch (SQLException se) {
			SolmrLogger.error(this, "deleteConduzioniDocumentoByIdConduzioneParticella in DocumentoDAO - SQLException: "+se.getMessage()+"\n");
			throw new DataAccessException(se.getMessage());
		}
		catch (Exception ex) {
			SolmrLogger.error(this, "Generic Exception in deleteConduzioniDocumentoByIdConduzioneParticella: "+ex.getMessage()+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.error(this, "SQLException while closing Statement and Connection in deleteConduzioniDocumentoByIdConduzioneParticella: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in deleteConduzioniDocumentoByIdConduzioneParticella: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
	}
	
	
	public void deleteConduzioniDocumentoByIdConduzioneParticellaNoIstanza(Long idConduzioneParticella) throws DataAccessException {
    SolmrLogger.debug(this, "Invocating deleteConduzioniDocumentoByIdConduzioneParticella method in DocumentoDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;

    try {

      SolmrLogger.debug(this, "Creating db-connection in deleteConduzioniDocumentoByIdConduzioneParticella method in DocumentoDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in deleteConduzioniDocumentoByIdConduzioneParticella method in DocumentoDAO and it values: "+conn+"\n");


      String query = " DELETE "+
      " FROM   DB_DOCUMENTO_CONDUZIONE " +
      " WHERE  ID_CONDUZIONE_PARTICELLA = ? " +
      " AND    ID_DOCUMENTO NOT IN ( SELECT DOC.ID_DOCUMENTO " +
      "                              FROM   DB_DOCUMENTO_CATEGORIA DDC, " +
      "                                     DB_TIPO_CATEGORIA_DOCUMENTO TCD," +
      "                                     DB_DOCUMENTO DOC," +
      "                                     DB_DOCUMENTO_CONDUZIONE DC " +
      "                              WHERE  DC.ID_CONDUZIONE_PARTICELLA = ? " +
      "                              AND    DC.ID_DOCUMENTO = DOC.ID_DOCUMENTO " +
      "                              AND    DOC.EXT_ID_DOCUMENTO = DDC.ID_DOCUMENTO " +
      "                              AND    DDC.ID_CATEGORIA_DOCUMENTO = TCD.ID_CATEGORIA_DOCUMENTO " +
      "                              AND    TCD.IDENTIFICATIVO =  518 " +
      "                              AND    TCD.TIPO_IDENTIFICATIVO = 'TC' " +
      "                           )";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this,"Value of parameter 1 [ID_DOCUMENTO] in deleteConduzioniDocumentoByIdConduzioneParticella method in DocumentoDAO: "+idConduzioneParticella+"\n");
      stmt.setLong(1, idConduzioneParticella.longValue());
      stmt.setLong(2, idConduzioneParticella.longValue());

      SolmrLogger.debug(this, "Executing deleteConduzioniDocumentoByIdConduzioneParticella: "+query+"\n");

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed deleteConduzioniDocumentoByIdConduzioneParticella method in DocumentoDAO\n");
    }
    catch (SQLException se) {
      SolmrLogger.error(this, "deleteConduzioniDocumentoByIdConduzioneParticella in DocumentoDAO - SQLException: "+se.getMessage()+"\n");
      throw new DataAccessException(se.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.error(this, "Generic Exception in deleteConduzioniDocumentoByIdConduzioneParticella: "+ex.getMessage()+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.error(this, "SQLException while closing Statement and Connection in deleteConduzioniDocumentoByIdConduzioneParticella: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in deleteConduzioniDocumentoByIdConduzioneParticella: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

	/**
	 * Metodo che si occupa di reperire un record da DB_DOCUMENTO in relazione all'id_conduzione_particella
	 *
	 * @param idConduzioneParticella
	 * @param onlyActive
	 * @return java.util.Vector
	 * @throws DataAccessException
	 */
	public Vector<DocumentoVO> getListDocumentiByIdConduzioneParticella(Long idConduzioneParticella, boolean onlyActive) 
	    throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating getListDocumentiByIdConduzioneParticella method in DocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<DocumentoVO> elencoDocumenti = new Vector<DocumentoVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListDocumentiByIdConduzioneParticella method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListDocumentiByIdConduzioneParticella method in DocumentoDAO and it values: "+conn+"\n");

			String query = " SELECT   D.ID_DOCUMENTO, " +
						   "          D.EXT_ID_DOCUMENTO, " +
						   "          TD.DESCRIZIONE, " +
						   "          D.ID_AZIENDA, " +
						   "          D.CUAA, " +
						   "          D.DATA_INIZIO_VALIDITA, " +
						   "          D.DATA_FINE_VALIDITA, " +
						   "          D.NUMERO_PROTOCOLLO, " +
						   "          D.DATA_PROTOCOLLO, " +
						   "          D.NUMERO_DOCUMENTO, " +
						   "          D.ENTE_RILASCIO_DOCUMENTO, " +
						   "          D.ID_DOCUMENTO_PRECEDENTE, " +
						   "          D.NOTE, " +
						   "          D.DATA_ULTIMO_AGGIORNAMENTO, " +
						   "          D.UTENTE_ULTIMO_AGGIORNAMENTO, " +
						   "          D.DATA_INSERIMENTO, " +
						   "          D.DATA_VARIAZIONE_STATO, " +
						   "          FRD.ID_FASE_ISTANZA_RIESAME " +
						   " FROM     DB_DOCUMENTO D, " +
						   "          DB_DOCUMENTO_CONDUZIONE DC, " +
						   "          DB_TIPO_DOCUMENTO TD, " +
						   "          DB_R_FASE_RIESAME_TP_DOCUMENTO FRD " +
						   " WHERE    DC.ID_CONDUZIONE_PARTICELLA = ? " +
						   " AND      DC.ID_DOCUMENTO = D.ID_DOCUMENTO " +
						   " AND      D.EXT_ID_DOCUMENTO = TD.ID_DOCUMENTO " +
						   " AND      D.EXT_ID_DOCUMENTO = FRD.ID_DOCUMENTO (+) " +
						   " AND      FRD.DATA_FINE_VALIDITA (+) IS NULL ";
			if(onlyActive) 
			{
				query += " " +
						"AND NVL(DC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) >= SYSDATE " +
						"AND D.ID_STATO_DOCUMENTO IS NULL "; // Aggiunto con sergio il 11/11/2010
			}

			query += " ORDER BY D.DATA_INIZIO_VALIDITA ASC, " +
			"          D.DATA_FINE_VALIDITA ASC ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Value of parameter 1 [ID_CONDUZIONE_PARTICELLA] in getListDocumentiByIdConduzioneParticella method in DocumentoDAO: "+idConduzioneParticella+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ONLY_ACTIVE] in getListDocumentiByIdConduzioneParticella method in DocumentoDAO: "+onlyActive+"\n");
			stmt.setLong(1, idConduzioneParticella.longValue());

			SolmrLogger.debug(this, "Executing getListDocumentiByIdConduzioneParticella: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				DocumentoVO documentoVO = new DocumentoVO();
				documentoVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
				documentoVO.setExtIdDocumento(new Long(rs.getLong("EXT_ID_DOCUMENTO")));
				TipoDocumentoVO tipoDocumentoVO = new TipoDocumentoVO();
				tipoDocumentoVO.setIdDocumento(new Long(rs.getLong("EXT_ID_DOCUMENTO")));
				tipoDocumentoVO.setDescrizione(rs.getString("DESCRIZIONE"));
				documentoVO.setTipoDocumentoVO(tipoDocumentoVO);
				documentoVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
				documentoVO.setCuaa(rs.getString("CUAA"));
				documentoVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				documentoVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				documentoVO.setNumeroProtocollo(rs.getString("NUMERO_PROTOCOLLO"));
				documentoVO.setDataProtocollo(rs.getDate("DATA_PROTOCOLLO"));
				documentoVO.setNumeroDocumento(rs.getString("NUMERO_DOCUMENTO"));
				documentoVO.setEnteRilascioDocumento(rs.getString("ENTE_RILASCIO_DOCUMENTO"));
				if(Validator.isNotEmpty(rs.getString("ID_DOCUMENTO_PRECEDENTE"))) {
					documentoVO.setIdDocumentoPrecedente(new Long(rs.getLong("ID_DOCUMENTO_PRECEDENTE")));
				}
				documentoVO.setNote(rs.getString("NOTE"));
				documentoVO.setDataUltimoAggiornamento(rs.getDate("DATA_ULTIMO_AGGIORNAMENTO"));
				documentoVO.setUtenteUltimoAggiornamento(new Long(rs.getLong("UTENTE_ULTIMO_AGGIORNAMENTO")));
				documentoVO.setDataInserimento(rs.getDate("DATA_INSERIMENTO"));
				documentoVO.setDataVariazioneStato(rs.getDate("DATA_VARIAZIONE_STATO"));
				
				documentoVO.setFaseIstanzaRiesame(rs.getInt("ID_FASE_ISTANZA_RIESAME"));
				
				elencoDocumenti.add(documentoVO);
			}

			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListDocumentiByIdConduzioneParticella in DocumentoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListDocumentiByIdConduzioneParticella in DocumentoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.error(this, "getListDocumentiByIdConduzioneParticella in DocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.error(this, "getListDocumentiByIdConduzioneParticella in DocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListDocumentiByIdConduzioneParticella method in DocumentoDAO\n");
		return elencoDocumenti;
	}
	
	
	/**
	 * Prende anche i legami cessati con le particella a solo per i doc attivi
	 * 
	 * 
	 * @param idConduzioneParticella
	 * @return
	 * @throws DataAccessException
	 */
	public Vector<DocumentoVO> getListDocumentiByIdConduzioneParticellaLegami(Long idConduzioneParticella) throws DataAccessException {
    SolmrLogger.debug(this, "Invocating getListDocumentiByIdConduzioneParticella method in DocumentoDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<DocumentoVO> elencoDocumenti = new Vector<DocumentoVO>();

    try {
      SolmrLogger.debug(this, "Creating db-connection in getListDocumentiByIdConduzioneParticellaLegami method in DocumentoDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListDocumentiByIdConduzioneParticellaLegami method in DocumentoDAO and it values: "+conn+"\n");

      String query = "" +
      	"SELECT   D.ID_DOCUMENTO, " +
        "         D.EXT_ID_DOCUMENTO, " +
        "         TD.DESCRIZIONE, " +
        "         D.ID_AZIENDA, " +
        "         D.CUAA, " +
        "         D.DATA_INIZIO_VALIDITA, " +
        "         D.DATA_FINE_VALIDITA, " +
        "         D.NUMERO_PROTOCOLLO, " +
        "         D.DATA_PROTOCOLLO, " +
        "         D.NUMERO_DOCUMENTO, " +
        "         D.ENTE_RILASCIO_DOCUMENTO, " +
        "         D.ID_DOCUMENTO_PRECEDENTE, " +
        "         D.NOTE, " +
        "         D.DATA_ULTIMO_AGGIORNAMENTO, " +
        "         D.UTENTE_ULTIMO_AGGIORNAMENTO, " +
        "         D.DATA_INSERIMENTO, " +
        "         D.DATA_VARIAZIONE_STATO " +
        "FROM     DB_DOCUMENTO D, " +
        "         DB_DOCUMENTO_CONDUZIONE DC, " +
        "         DB_TIPO_DOCUMENTO TD " +
        "WHERE    DC.ID_CONDUZIONE_PARTICELLA = ? " +
        "AND      DC.ID_DOCUMENTO = D.ID_DOCUMENTO " +
        "AND      D.EXT_ID_DOCUMENTO = TD.ID_DOCUMENTO " +
        "AND      D.ID_STATO_DOCUMENTO IS NULL " +
        "AND      NVL(DC.DATA_FINE_VALIDITA,TO_DATE('31/12/9999','dd/mm/yyyy')) = (SELECT NVL(MAX(DATA_FINE_VALIDITA),TO_DATE('31/12/9999','dd/mm/yyyy')) " +
        "                                                                         FROM   DB_DOCUMENTO_CONDUZIONE DC2 " +
        "                                                                         WHERE  DC2.ID_DOCUMENTO = D.ID_DOCUMENTO " +
        "                                                                         AND    DC2.ID_CONDUZIONE_PARTICELLA = DC.ID_CONDUZIONE_PARTICELLA " +
        "                                                                         ) " +
        "ORDER BY D.DATA_INIZIO_VALIDITA ASC, " +
        "         D.DATA_FINE_VALIDITA ASC ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Value of parameter 1 [ID_CONDUZIONE_PARTICELLA] in getListDocumentiByIdConduzioneParticellaLegami method in DocumentoDAO: "+idConduzioneParticella+"\n");
      stmt.setLong(1, idConduzioneParticella.longValue());

      SolmrLogger.debug(this, "Executing getListDocumentiByIdConduzioneParticellaLegami: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) {
        DocumentoVO documentoVO = new DocumentoVO();
        documentoVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
        documentoVO.setExtIdDocumento(new Long(rs.getLong("EXT_ID_DOCUMENTO")));
        TipoDocumentoVO tipoDocumentoVO = new TipoDocumentoVO();
        tipoDocumentoVO.setIdDocumento(new Long(rs.getLong("EXT_ID_DOCUMENTO")));
        tipoDocumentoVO.setDescrizione(rs.getString("DESCRIZIONE"));
        documentoVO.setTipoDocumentoVO(tipoDocumentoVO);
        documentoVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        documentoVO.setCuaa(rs.getString("CUAA"));
        documentoVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        documentoVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        documentoVO.setNumeroProtocollo(rs.getString("NUMERO_PROTOCOLLO"));
        documentoVO.setDataProtocollo(rs.getDate("DATA_PROTOCOLLO"));
        documentoVO.setNumeroDocumento(rs.getString("NUMERO_DOCUMENTO"));
        documentoVO.setEnteRilascioDocumento(rs.getString("ENTE_RILASCIO_DOCUMENTO"));
        if(Validator.isNotEmpty(rs.getString("ID_DOCUMENTO_PRECEDENTE"))) {
          documentoVO.setIdDocumentoPrecedente(new Long(rs.getLong("ID_DOCUMENTO_PRECEDENTE")));
        }
        documentoVO.setNote(rs.getString("NOTE"));
        documentoVO.setDataUltimoAggiornamento(rs.getDate("DATA_ULTIMO_AGGIORNAMENTO"));
        documentoVO.setUtenteUltimoAggiornamento(new Long(rs.getLong("UTENTE_ULTIMO_AGGIORNAMENTO")));
        documentoVO.setDataInserimento(rs.getDate("DATA_INSERIMENTO"));
        documentoVO.setDataVariazioneStato(rs.getDate("DATA_VARIAZIONE_STATO"));
        elencoDocumenti.add(documentoVO);
      }

      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getListDocumentiByIdConduzioneParticellaLegami in DocumentoDAO - SQLException: "+exc.getMessage()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getListDocumentiByIdConduzioneParticellaLegami in DocumentoDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.error(this, "getListDocumentiByIdConduzioneParticellaLegami in DocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.error(this, "getListDocumentiByIdConduzioneParticellaLegami in DocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListDocumentiByIdConduzioneParticellaLegami method in DocumentoDAO\n");
    return elencoDocumenti;
  }

	/**
	 * Metodo che si occupa di reperire un record da DB_DOCUMENTO in relazione all'id_conduzione_dichiarata
	 *
	 * @param idConduzioneDichiarata
	 * @return java.util.Vector
	 * @throws DataAccessException
	 */
	public Vector<DocumentoVO> getListDocumentiByIdConduzioneDichiarata(Long idConduzioneDichiarata) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListDocumentiByIdConduzioneDichiarata method in DocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<DocumentoVO> elencoDocumenti = new Vector<DocumentoVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListDocumentiByIdConduzioneDichiarata method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListDocumentiByIdConduzioneDichiarata method in DocumentoDAO and it values: "+conn+"\n");

			String query = " SELECT   D.ID_DOCUMENTO, " +
			"          D.EXT_ID_DOCUMENTO, " +
			"          TD.DESCRIZIONE, " +
			"          D.ID_AZIENDA, " +
			"          D.CUAA, " +
			"          D.DATA_INIZIO_VALIDITA, " +
			"          D.DATA_FINE_VALIDITA, " +
			"          D.NUMERO_PROTOCOLLO, " +
			"          D.DATA_PROTOCOLLO, " +
			"          D.NUMERO_DOCUMENTO, " +
			"          D.ENTE_RILASCIO_DOCUMENTO, " +
			"          D.ID_DOCUMENTO_PRECEDENTE, " +
			"          D.NOTE, " +
			"          D.DATA_ULTIMO_AGGIORNAMENTO, " +
			"          D.UTENTE_ULTIMO_AGGIORNAMENTO, " +
			"          D.DATA_INSERIMENTO, " +
			"          D.DATA_VARIAZIONE_STATO " +
			" FROM     DB_DOCUMENTO D, " +
			"          DB_DOCUMENTO_CONDUZIONE DC, " +
			"          DB_CONDUZIONE_DICHIARATA CD, " +
			"          DB_TIPO_DOCUMENTO TD, " +
			"          DB_DICHIARAZIONE_CONSISTENZA DICH_C " +
			" WHERE    CD.ID_CONDUZIONE_DICHIARATA = ? " +
			" AND      CD.CODICE_FOTOGRAFIA_TERRENI = DICH_C.CODICE_FOTOGRAFIA_TERRENI " +
			" AND      TRUNC(DC.DATA_INIZIO_VALIDITA) <= TRUNC(DICH_C.DATA_INSERIMENTO_DICHIARAZIONE) " +
			" AND      TRUNC(NVL(DC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY'))) > TRUNC(DICH_C.DATA_INSERIMENTO_DICHIARAZIONE) " +
			" AND      CD.ID_CONDUZIONE_PARTICELLA = DC.ID_CONDUZIONE_PARTICELLA " +
			" AND      DC.ID_DOCUMENTO = D.ID_DOCUMENTO " +
			" AND      D.EXT_ID_DOCUMENTO = TD.ID_DOCUMENTO " +
			" ORDER BY D.DATA_INIZIO_VALIDITA ASC, " +
			"          D.DATA_FINE_VALIDITA ASC ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Value of parameter 1 [ID_CONDUZIONE_DICHIARATA] in getListDocumentiByIdConduzioneDichiarata method in DocumentoDAO: "+idConduzioneDichiarata+"\n");
			stmt.setLong(1, idConduzioneDichiarata.longValue());

			SolmrLogger.debug(this, "Executing getListDocumentiByIdConduzioneDichiarata: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				DocumentoVO documentoVO = new DocumentoVO();
				documentoVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
				documentoVO.setExtIdDocumento(new Long(rs.getLong("EXT_ID_DOCUMENTO")));
				TipoDocumentoVO tipoDocumentoVO = new TipoDocumentoVO();
				tipoDocumentoVO.setIdDocumento(new Long(rs.getLong("EXT_ID_DOCUMENTO")));
				tipoDocumentoVO.setDescrizione(rs.getString("DESCRIZIONE"));
				documentoVO.setTipoDocumentoVO(tipoDocumentoVO);
				documentoVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
				documentoVO.setCuaa(rs.getString("CUAA"));
				documentoVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				documentoVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				documentoVO.setNumeroProtocollo(rs.getString("NUMERO_PROTOCOLLO"));
				documentoVO.setDataProtocollo(rs.getDate("DATA_PROTOCOLLO"));
				documentoVO.setNumeroDocumento(rs.getString("NUMERO_DOCUMENTO"));
				documentoVO.setEnteRilascioDocumento(rs.getString("ENTE_RILASCIO_DOCUMENTO"));
				if(Validator.isNotEmpty(rs.getString("ID_DOCUMENTO_PRECEDENTE"))) {
					documentoVO.setIdDocumentoPrecedente(new Long(rs.getLong("ID_DOCUMENTO_PRECEDENTE")));
				}
				documentoVO.setNote(rs.getString("NOTE"));
				documentoVO.setDataUltimoAggiornamento(rs.getDate("DATA_ULTIMO_AGGIORNAMENTO"));
				documentoVO.setUtenteUltimoAggiornamento(new Long(rs.getLong("UTENTE_ULTIMO_AGGIORNAMENTO")));
				documentoVO.setDataInserimento(rs.getDate("DATA_INSERIMENTO"));
				documentoVO.setDataVariazioneStato(rs.getDate("DATA_VARIAZIONE_STATO"));
				elencoDocumenti.add(documentoVO);
			}

			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListDocumentiByIdConduzioneDichiarata in DocumentoDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListDocumentiByIdConduzioneDichiarata in DocumentoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.error(this, "getListDocumentiByIdConduzioneDichiarata in DocumentoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.error(this, "getListDocumentiByIdConduzioneDichiarata in DocumentoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListDocumentiByIdConduzioneDichiarata method in DocumentoDAO\n");
		return elencoDocumenti;
	}
	
	
	
	public Vector<DocumentoVO> getListDocumentiByIdConduzioneDichiarataPopUp(Long idConduzioneDichiarata) throws DataAccessException {
    SolmrLogger.debug(this, "Invocating getListDocumentiByIdConduzioneDichiarataPopUp method in DocumentoDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<DocumentoVO> elencoDocumenti = new Vector<DocumentoVO>();

    try {
      SolmrLogger.debug(this, "Creating db-connection in getListDocumentiByIdConduzioneDichiarataPopUp method in DocumentoDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListDocumentiByIdConduzioneDichiarataPopUp method in DocumentoDAO and it values: "+conn+"\n");

      String query = " SELECT   D.ID_DOCUMENTO, " +
      "          D.EXT_ID_DOCUMENTO, " +
      "          TD.DESCRIZIONE, " +
      "          D.ID_AZIENDA, " +
      "          D.CUAA, " +
      "          D.DATA_INIZIO_VALIDITA, " +
      "          D.DATA_FINE_VALIDITA, " +
      "          D.NUMERO_PROTOCOLLO, " +
      "          D.DATA_PROTOCOLLO, " +
      "          D.NUMERO_DOCUMENTO, " +
      "          D.ENTE_RILASCIO_DOCUMENTO, " +
      "          D.ID_DOCUMENTO_PRECEDENTE, " +
      "          D.NOTE, " +
      "          D.DATA_ULTIMO_AGGIORNAMENTO, " +
      "          D.UTENTE_ULTIMO_AGGIORNAMENTO, " +
      "          D.DATA_INSERIMENTO, " +
      "          D.DATA_VARIAZIONE_STATO " +
      " FROM     DB_DOCUMENTO D, " +
      "          DB_DOCUMENTO_CONDUZIONE DC, " +
      "          DB_CONDUZIONE_DICHIARATA CD, " +
      "          DB_TIPO_DOCUMENTO TD, " +
      "          DB_DICHIARAZIONE_CONSISTENZA DICH_C " +
      " WHERE    CD.ID_CONDUZIONE_DICHIARATA = ? " +
      " AND      CD.CODICE_FOTOGRAFIA_TERRENI = DICH_C.CODICE_FOTOGRAFIA_TERRENI " +
      " AND      TRUNC(D.DATA_INIZIO_VALIDITA) <= TRUNC(DICH_C.DATA_INSERIMENTO_DICHIARAZIONE) " +
      " AND      TRUNC(NVL(D.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY'))) > TRUNC(DICH_C.DATA_INSERIMENTO_DICHIARAZIONE) " +
      " AND      CD.ID_CONDUZIONE_PARTICELLA = DC.ID_CONDUZIONE_PARTICELLA " +
      " AND      DC.ID_DOCUMENTO = D.ID_DOCUMENTO " +
      " AND      D.EXT_ID_DOCUMENTO = TD.ID_DOCUMENTO " +
      " ORDER BY D.DATA_INIZIO_VALIDITA ASC, " +
      "          D.DATA_FINE_VALIDITA ASC ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Value of parameter 1 [ID_CONDUZIONE_DICHIARATA] in getListDocumentiByIdConduzioneDichiarataPopUp method in DocumentoDAO: "+idConduzioneDichiarata+"\n");
      stmt.setLong(1, idConduzioneDichiarata.longValue());

      SolmrLogger.debug(this, "Executing getListDocumentiByIdConduzioneDichiarataPopUp: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) {
        DocumentoVO documentoVO = new DocumentoVO();
        documentoVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
        documentoVO.setExtIdDocumento(new Long(rs.getLong("EXT_ID_DOCUMENTO")));
        TipoDocumentoVO tipoDocumentoVO = new TipoDocumentoVO();
        tipoDocumentoVO.setIdDocumento(new Long(rs.getLong("EXT_ID_DOCUMENTO")));
        tipoDocumentoVO.setDescrizione(rs.getString("DESCRIZIONE"));
        documentoVO.setTipoDocumentoVO(tipoDocumentoVO);
        documentoVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        documentoVO.setCuaa(rs.getString("CUAA"));
        documentoVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        documentoVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        documentoVO.setNumeroProtocollo(rs.getString("NUMERO_PROTOCOLLO"));
        documentoVO.setDataProtocollo(rs.getDate("DATA_PROTOCOLLO"));
        documentoVO.setNumeroDocumento(rs.getString("NUMERO_DOCUMENTO"));
        documentoVO.setEnteRilascioDocumento(rs.getString("ENTE_RILASCIO_DOCUMENTO"));
        if(Validator.isNotEmpty(rs.getString("ID_DOCUMENTO_PRECEDENTE"))) {
          documentoVO.setIdDocumentoPrecedente(new Long(rs.getLong("ID_DOCUMENTO_PRECEDENTE")));
        }
        documentoVO.setNote(rs.getString("NOTE"));
        documentoVO.setDataUltimoAggiornamento(rs.getDate("DATA_ULTIMO_AGGIORNAMENTO"));
        documentoVO.setUtenteUltimoAggiornamento(new Long(rs.getLong("UTENTE_ULTIMO_AGGIORNAMENTO")));
        documentoVO.setDataInserimento(rs.getDate("DATA_INSERIMENTO"));
        documentoVO.setDataVariazioneStato(rs.getDate("DATA_VARIAZIONE_STATO"));
        elencoDocumenti.add(documentoVO);
      }

      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getListDocumentiByIdConduzioneDichiarataPopUp in DocumentoDAO - SQLException: "+exc+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getListDocumentiByIdConduzioneDichiarataPopUp in DocumentoDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.error(this, "getListDocumentiByIdConduzioneDichiarataPopUp in DocumentoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.error(this, "getListDocumentiByIdConduzioneDichiarataPopUp in DocumentoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListDocumentiByIdConduzioneDichiarataPopUp method in DocumentoDAO\n");
    return elencoDocumenti;
  }


	/**
	 * Metodo che si occupa di reperire tutti le conduzioni legate al documento e all'id_conduzione_particella
	 * specificato
	 *
	 * @param idDocumento Long
	 * @param idConduzioneParticella Long
	 * @param onlyActive boolean
	 * @return java.util.Vector
	 * @throws DataAccessException
	 */
	public Vector<DocumentoConduzioneVO> getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella(
	    Long idDocumento, Long idConduzioneParticella, boolean onlyActive) throws DataAccessException 
	{
		SolmrLogger.debug(this, "Invocating getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella method in DocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<DocumentoConduzioneVO> elencoConduzioni = new Vector<DocumentoConduzioneVO>();

		try 
		{
			SolmrLogger.debug(this, "Creating db-connection in getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella method in DocumentoDAO and it values: "+conn+"\n");

			String query = " SELECT   ID_DOCUMENTO_CONDUZIONE, " +
						   "          ID_CONDUZIONE_PARTICELLA, " +
						   "          ID_DOCUMENTO, " +
						   "          DATA_INSERIMENTO, " +
						   "          DATA_INIZIO_VALIDITA, " +
						   "          DATA_FINE_VALIDITA," +
						   "          LAVORAZIONE_PRIORITARIA, " +
						   "          NOTE " +
						   " FROM     DB_DOCUMENTO_CONDUZIONE " +
						   " WHERE    ID_DOCUMENTO = ? " +
						   " AND      ID_CONDUZIONE_PARTICELLA = ? ";
			if(onlyActive) 
			{
				query += " AND NVL(TRUNC(DATA_FINE_VALIDITA), TO_DATE('31/12/9999', 'DD/MM/YYYY')) >= TRUNC(SYSDATE) ";
			}

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Value of parameter 1 [ID_DOCUMENTO] in getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella method in DocumentoDAO: "+idDocumento+"\n");
			stmt.setLong(1, idDocumento.longValue());
			SolmrLogger.debug(this, "Value of parameter 2 [ID_CONDUZIONE_PARTICELLA] in getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella method in DocumentoDAO: "+idConduzioneParticella+"\n");
			stmt.setLong(2, idConduzioneParticella.longValue());
			SolmrLogger.debug(this, "Value of parameter 3 [ONLY_ACTIVE] in getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella method in DocumentoDAO: "+onlyActive+"\n");

			SolmrLogger.debug(this, "Executing getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) 
			{
				DocumentoConduzioneVO documentoConduzioneVO = new DocumentoConduzioneVO();
				documentoConduzioneVO.setIdDocumentoConduzione(new Long(rs.getLong("ID_DOCUMENTO_CONDUZIONE")));
				documentoConduzioneVO.setIdConduzioneParticella(new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")));
				documentoConduzioneVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
				documentoConduzioneVO.setDataInserimento(rs.getTimestamp("DATA_INSERIMENTO"));
				documentoConduzioneVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
				documentoConduzioneVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
				documentoConduzioneVO.setLavorazionePrioritaria(rs.getString("LAVORAZIONE_PRIORITARIA"));
				documentoConduzioneVO.setNote(rs.getString("NOTE"));
				
				elencoConduzioni.add(documentoConduzioneVO);
			}

			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella in DocumentoDAO - SQLException: "+exc+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella in DocumentoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella in DocumentoDAO - SQLException while closing Statement and Connection: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella in DocumentoDAO - Generic Exception while closing Statement and Connection: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella method in DocumentoDAO\n");
		return elencoConduzioni;
	}

	/**
	 * Metodo che si occupa di reperire tutti le conduzioni legate al documento e all'id_conduzione_dichiarata
	 * specificato
	 *
	 * @param idDocumento Long
	 * @param idConduzioneDichiarata Long
	 * @return java.util.Vector
	 * @throws DataAccessException
	 */
	public Vector<DocumentoConduzioneVO> getListDocumentoConduzioniByIdDocumentoAndIdConduzioneDichiarata(Long idDocumento, Long idConduzioneDichiarata) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListDocumentoConduzioniByIdDocumentoAndIdConduzioneDichiarata method in DocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<DocumentoConduzioneVO> elencoConduzioni = new Vector<DocumentoConduzioneVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListDocumentoConduzioniByIdDocumentoAndIdConduzioneDichiarata method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListDocumentoConduzioniByIdDocumentoAndIdConduzioneDichiarata method in DocumentoDAO and it values: "+conn+"\n");

			String query = " SELECT   DC.ID_DOCUMENTO_CONDUZIONE, " +
			"          DC.ID_CONDUZIONE_PARTICELLA, " +
			"          DC.ID_DOCUMENTO, " +
			"          DC.DATA_INSERIMENTO " +
			" FROM     DB_DOCUMENTO_CONDUZIONE DC, " +
			"          DB_CONDUZIONE_DICHIARATA CD " +
			" WHERE    DC.ID_DOCUMENTO = ? " +
			" AND      CD.ID_CONDUZIONE_DICHIARATA = ? " +
			" AND      CD.ID_CONDUZIONE_PARTICELLA = DC.ID_CONDUZIONE_PARTICELLA ";

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this, "Value of parameter 1 [ID_DOCUMENTO] in getListDocumentoConduzioniByIdDocumentoAndIdConduzioneDichiarata method in DocumentoDAO: "+idDocumento+"\n");
			stmt.setLong(1, idDocumento.longValue());
			SolmrLogger.debug(this, "Value of parameter 2 [ID_CONDUZIONE_DICHIARATA] in getListDocumentoConduzioniByIdDocumentoAndIdConduzioneDichiarata method in DocumentoDAO: "+idConduzioneDichiarata+"\n");
			stmt.setLong(2, idConduzioneDichiarata.longValue());

			SolmrLogger.debug(this, "Executing getListDocumentoConduzioniByIdDocumentoAndIdConduzioneDichiarata: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				DocumentoConduzioneVO documentoConduzioneVO = new DocumentoConduzioneVO();
				documentoConduzioneVO.setIdDocumentoConduzione(new Long(rs.getLong(1)));
				documentoConduzioneVO.setIdConduzioneParticella(new Long(rs.getLong(2)));
				documentoConduzioneVO.setIdDocumento(new Long(rs.getLong(3)));
				documentoConduzioneVO.setDataInserimento(new java.util.Date(rs.getDate(4).getTime()));
				elencoConduzioni.add(documentoConduzioneVO);
			}

			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListDocumentoConduzioniByIdDocumentoAndIdConduzioneDichiarata in DocumentoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListDocumentoConduzioniByIdDocumentoAndIdConduzioneDichiarata in DocumentoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.error(this, "getListDocumentoConduzioniByIdDocumentoAndIdConduzioneDichiarata in DocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.error(this, "getListDocumentoConduzioniByIdDocumentoAndIdConduzioneDichiarata in DocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListDocumentoConduzioniByIdDocumentoAndIdConduzioneDichiarata method in DocumentoDAO\n");
		return elencoConduzioni;
	}

	/**
	 * Metodo che mi restituisce l'elenco dei documenti associati ad un'azienda
	 *
	 * @param idAzienda
	 * @param onlyActive
	 * @param orderBy
	 * @param idTipologiaDocumento
	 * @return it.csi.solmr.dto.anag.DocumentoVO[]
	 * @throws DataAccessException
	 */
	public DocumentoVO[] getListDocumentiByIdAzienda(Long idAzienda, boolean onlyActive, String[] orderBy, Long idTipologiaDocumento) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListDocumentiByIdAzienda method in DocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<DocumentoVO> elencoDocumenti = new Vector<DocumentoVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListDocumentiByIdAzienda method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListDocumentiByIdAzienda method in DocumentoDAO and it values: "+conn+"\n");

			String query = " SELECT D.ID_DOCUMENTO, " +
			"        D.EXT_ID_DOCUMENTO, " +
			"        TD.DESCRIZIONE AS DESC_DOC, " +
			"        D.ID_STATO_DOCUMENTO, " +
			"        D.ID_AZIENDA, " +
			"        D.CUAA, " +
			"        D.DATA_INIZIO_VALIDITA, " +
			"        D.DATA_FINE_VALIDITA, " +
			"        D.NUMERO_PROTOCOLLO, " +
			"        D.DATA_PROTOCOLLO, " +
			"        D.NUMERO_DOCUMENTO, " +
			"        D.ENTE_RILASCIO_DOCUMENTO, " +
			"        D.ID_DOCUMENTO_PRECEDENTE, " +
			"        D.NOTE, " +
			"        D.DATA_ULTIMO_AGGIORNAMENTO, " +
			"        D.UTENTE_ULTIMO_AGGIORNAMENTO, " +
			"        D.DATA_INSERIMENTO, " +
			"        D.DATA_VARIAZIONE_STATO " +
			" FROM   DB_DOCUMENTO D, " +
			"        DB_TIPO_DOCUMENTO TD " +
			" WHERE  ID_AZIENDA = ? " +
			" AND    D.EXT_ID_DOCUMENTO = TD.ID_DOCUMENTO ";
			if(onlyActive) {
				query +=   " AND    ID_STATO_DOCUMENTO IS NULL " +
				" AND    NVL(D.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > SYSDATE ";
			}
			if(idTipologiaDocumento != null) {
				query += "   AND    TD.ID_TIPOLOGIA_DOCUMENTO = ? ";
			}
			/*if(orderBy != null && orderBy.length > 0) 
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
			}*/

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListDocumentiByIdAzienda method in DocumentoDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [ONLY_ACTIVE] in getListDocumentiByIdAzienda method in DocumentoDAO: "+onlyActive+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ORDER_BY] in getListDocumentiByIdAzienda method in DocumentoDAO: "+orderBy+"\n");
			SolmrLogger.debug(this, "Value of parameter 4 [ID_TIPOLOGIA_DOCUMENTO] in getListDocumentiByIdAzienda method in DocumentoDAO: "+idTipologiaDocumento+"\n");

			stmt = conn.prepareStatement(query);

			stmt.setLong(1, idAzienda.longValue());
			if(idTipologiaDocumento != null) {
				stmt.setLong(2, idTipologiaDocumento.longValue());
			}

			SolmrLogger.debug(this, "Executing getListDocumentiByIdAzienda: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				DocumentoVO documentoVO = new DocumentoVO();
				documentoVO.setIdDocumento(new Long(rs.getLong(1)));
				documentoVO.setExtIdDocumento(new Long(rs.getLong(2)));
				TipoDocumentoVO tipoDocumentoVO = new TipoDocumentoVO();
				tipoDocumentoVO.setIdDocumento(new Long(rs.getLong(2)));
				tipoDocumentoVO.setDescrizione(rs.getString(3));
				documentoVO.setTipoDocumentoVO(tipoDocumentoVO);
				if(Validator.isNotEmpty(rs.getString(4))) {
					documentoVO.setIdStatoDocumento(new Long(rs.getLong(4)));
				}
				documentoVO.setIdAzienda(new Long(rs.getLong(5)));
				documentoVO.setCuaa(rs.getString(6));
				documentoVO.setDataInizioValidita(rs.getDate(7));
				documentoVO.setDataFineValidita(rs.getDate(8));
				documentoVO.setNumeroProtocollo(rs.getString(9));
				documentoVO.setDataProtocollo(rs.getDate(10));
				documentoVO.setNumeroDocumento(rs.getString(11));
				documentoVO.setEnteRilascioDocumento(rs.getString(12));
				if(Validator.isNotEmpty(rs.getString(13))) {
					documentoVO.setIdDocumentoPrecedente(new Long(rs.getLong(13)));
				}
				documentoVO.setNote(rs.getString(14));
				documentoVO.setDataUltimoAggiornamento(rs.getDate(15));
				documentoVO.setUtenteUltimoAggiornamento(new Long(rs.getLong(16)));
				documentoVO.setDataInserimento(rs.getDate(17));
				documentoVO.setDataVariazioneStato(rs.getDate(18));
				elencoDocumenti.add(documentoVO);
			}

			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListDocumentiByIdAzienda in DocumentoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListDocumentiByIdAzienda in DocumentoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListDocumentiByIdAzienda in DocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListDocumentiByIdAzienda in DocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListDocumentiByIdAzienda method in DocumentoDAO\n");
		if(elencoDocumenti.size() == 0) {
			return(DocumentoVO[])elencoDocumenti.toArray(new DocumentoVO[0]);
		}
		else {
			return(DocumentoVO[])elencoDocumenti.toArray(new DocumentoVO[elencoDocumenti.size()]);
		}
	}

	/**
	 * Metodo che si occupa di effettuare la modifica del record relativo alla tabella
	 * DB_DOCUMENTO
	 *
	 * @param documentoVO DocumentoVO
	 * @param RuoloUtenza ruoloUtenza
	 * @throws DataAccessException
	 */
	public void updateDocumento(DocumentoVO documentoVO, RuoloUtenza ruoloUtenza) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating updateDocumento method in DocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;

		try {

			SolmrLogger.debug(this, "Creating db-connection in updateDocumento method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in updateDocumento method in DocumentoDAO and it values: "+conn+"\n");


			String query = " UPDATE DB_DOCUMENTO "+
			" SET    EXT_ID_DOCUMENTO = ?,  " +
			"        ID_STATO_DOCUMENTO = ?, " +
			"        ID_AZIENDA = ?, " +
			"        CUAA = ?, " +
			"        DATA_INIZIO_VALIDITA = ?, " +
			"        DATA_FINE_VALIDITA = ?, " +
			"        NUMERO_PROTOCOLLO = ?, " +
			"        DATA_PROTOCOLLO = ?, " +
			"        NUMERO_DOCUMENTO = UPPER(?), " +
			"        ENTE_RILASCIO_DOCUMENTO = UPPER(?), " +
			"        ID_DOCUMENTO_PRECEDENTE = ?, " +
			"        NOTE = ?, " +
			"        DATA_ULTIMO_AGGIORNAMENTO = ?, " +
			"        UTENTE_ULTIMO_AGGIORNAMENTO = ?, " +
			"        DATA_INSERIMENTO = ?, " +
			"        DATA_VARIAZIONE_STATO = ?, " +
			"        ID_UTENTE_AGGIORNAMENTO_SRV = ?, " +
			"        NUMERO_PROTOCOLLO_ESTERNO = UPPER(?), " +
			"        CUAA_SOCCIDARIO = UPPER(?), " +
			"        ESITO_CONTROLLO = ?, " +
			"        DATA_ESECUZIONE = ?, " +
			"        FLAG_CUAA_SOCCIDARIO_VALIDATO = ?, " +
			"        ID_CONTO_CORRENTE = ?, " +
			"        ID_CAUSALE_MODIFICA_DOCUMENTO = ? " +
			" WHERE  ID_DOCUMENTO = ? ";

			stmt = conn.prepareStatement(query);

			int idx = 0;
			stmt.setLong(++idx, documentoVO.getExtIdDocumento().longValue());
			SolmrLogger.debug(this,"Value of parameter 1 [EXT_ID_DOCUMENTO] in updateDocumento method in DocumentoDAO: "+documentoVO.getExtIdDocumento().longValue()+"\n");
			if(documentoVO.getIdStatoDocumento() != null) {
				stmt.setLong(++idx, documentoVO.getIdStatoDocumento().longValue());
				SolmrLogger.debug(this,"Value of parameter 2 [ID_STATO_DOCUMENTO] in updateDocumento method in DocumentoDAO: "+documentoVO.getIdStatoDocumento().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this,"Value of parameter 2 [ID_STATO_DOCUMENTO] in updateDocumento method in DocumentoDAO: "+null+"\n");
			}
			stmt.setLong(++idx, documentoVO.getIdAzienda().longValue());
			SolmrLogger.debug(this,"Value of parameter 3 [ID_AZIENDA] in updateDocumento method in DocumentoDAO: "+documentoVO.getIdAzienda().longValue()+"\n");
			stmt.setString(++idx, documentoVO.getCuaa());
			SolmrLogger.debug(this,"Value of parameter 4 [CUAA] in updateDocumento method in DocumentoDAO: "+documentoVO.getCuaa()+"\n");
			stmt.setDate(++idx, new java.sql.Date(documentoVO.getDataInizioValidita().getTime()));
			SolmrLogger.debug(this,"Value of parameter 5 [DATA_INIZIO_VALIDITA] in updateDocumento method in DocumentoDAO: "+documentoVO.getDataInizioValidita()+"\n");
			if(documentoVO.getDataFineValidita() != null) {
				stmt.setDate(++idx, new java.sql.Date(documentoVO.getDataFineValidita().getTime()));
				SolmrLogger.debug(this,"Value of parameter 6 [DATA_FINE_VALIDITA] in updateDocumento method in DocumentoDAO: "+documentoVO.getDataFineValidita()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this,"Value of parameter 6 [DATA_FINE_VALIDITA] in updateDocumento method in DocumentoDAO: "+null+"\n");
			}
			stmt.setString(++idx, documentoVO.getNumeroProtocollo());
			SolmrLogger.debug(this,"Value of parameter 7 [NUMERO_PROTOCOLLO] in updateDocumento method in DocumentoDAO: "+documentoVO.getNumeroProtocollo()+"\n");
			if(documentoVO.getDataProtocollo() != null) {
				stmt.setDate(++idx, new java.sql.Date(documentoVO.getDataProtocollo().getTime()));
				SolmrLogger.debug(this,"Value of parameter 8 [DATA_PROTOCOLLO] in updateDocumento method in DocumentoDAO: "+documentoVO.getDataProtocollo()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this,"Value of parameter 8 [DATA_PROTOCOLLO] in updateDocumento method in DocumentoDAO: "+null+"\n");
			}
			stmt.setString(++idx, documentoVO.getNumeroDocumento());
			SolmrLogger.debug(this,"Value of parameter 9 [NUMERO_DOCUMENTO] in updateDocumento method in DocumentoDAO: "+documentoVO.getNumeroDocumento()+"\n");
			stmt.setString(++idx, documentoVO.getEnteRilascioDocumento());
			SolmrLogger.debug(this,"Value of parameter 10 [ENTE_RILASCIO_DOCUMENTO] in updateDocumento method in DocumentoDAO: "+documentoVO.getEnteRilascioDocumento()+"\n");
			if(documentoVO.getIdDocumentoPrecedente() != null) {
				stmt.setLong(++idx, documentoVO.getIdDocumentoPrecedente().longValue());
				SolmrLogger.debug(this,"Value of parameter 11 [ID_DOCUMENTO_PRECEDENTE] in updateDocumento method in DocumentoDAO: "+documentoVO.getIdDocumentoPrecedente()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this,"Value of parameter 11 [ID_DOCUMENTO_PRECEDENTE] in updateDocumento method in DocumentoDAO: "+null+"\n");
			}
			stmt.setString(++idx, documentoVO.getNote());
			SolmrLogger.debug(this,"Value of parameter 12 [NOTE] in updateDocumento method in DocumentoDAO: "+documentoVO.getNote()+"\n");
			stmt.setTimestamp(++idx, new Timestamp(documentoVO.getDataUltimoAggiornamento().getTime()));
			SolmrLogger.debug(this,"Value of parameter 13 [DATA_ULTIMO_AGGIORNAMENTO] in updateDocumento method in DocumentoDAO: "+new Timestamp(documentoVO.getDataUltimoAggiornamento().getTime())+"\n");
			stmt.setLong(++idx, ruoloUtenza.getIdUtente().longValue());
			SolmrLogger.debug(this,"Value of parameter 14 [UTENTE_ULTIMO_AGGIORNAMENTO] in updateDocumento method in DocumentoDAO: "+ruoloUtenza.getIdUtente().longValue()+"\n");
			stmt.setTimestamp(++idx, new Timestamp(documentoVO.getDataInserimento().getTime()));
			SolmrLogger.debug(this,"Value of parameter 15 [DATA_INSERIMENTO] in updateDocumento method in DocumentoDAO: "+new Timestamp(documentoVO.getDataInserimento().getTime())+"\n");
			if(documentoVO.getDataVariazioneStato() != null) {
				stmt.setTimestamp(++idx, new Timestamp(documentoVO.getDataVariazioneStato().getTime()));
				SolmrLogger.debug(this,"Value of parameter 16 [DATA_VARIAZIONE_STATO] in updateDocumento method in DocumentoDAO: "+new Timestamp(documentoVO.getDataVariazioneStato().getTime())+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this,"Value of parameter 16 [DATA_VARIAZIONE_STATO] in updateDocumento method in DocumentoDAO: "+null+"\n");
			}
			if(documentoVO.getIdUtenteAggiornamentoSrv() != null) {
				stmt.setLong(++idx, documentoVO.getIdUtenteAggiornamentoSrv().longValue());
				SolmrLogger.debug(this,"Value of parameter 17 [ID_UTENTE_AGGIORNAMENTO_SRV] in updateDocumento method in DocumentoDAO: "+documentoVO.getIdUtenteAggiornamentoSrv().longValue()+"\n");
			}
			else {
				stmt.setString(++idx, null);
				SolmrLogger.debug(this,"Value of parameter 17 [ID_UTENTE_AGGIORNAMENTO_SRV] in updateDocumento method in DocumentoDAO: "+null+"\n");
			}
			stmt.setString(++idx, documentoVO.getNumeroProtocolloEsterno());
			SolmrLogger.debug(this,"Value of parameter 18 [NUMERO_PROTOCOLLO_ESTERNO] in updateDocumento method in DocumentoDAO: "+documentoVO.getNumeroProtocolloEsterno()+"\n");
			stmt.setString(++idx, documentoVO.getCuaaSoccidario());
			SolmrLogger.debug(this,"Value of parameter 19 [CUAA_SOCCIDARIO] in updateDocumento method in DocumentoDAO: "+documentoVO.getCuaaSoccidario()+"\n");
			stmt.setString(++idx, documentoVO.getEsitoControllo());
			SolmrLogger.debug(this,"Value of parameter 20 [ESITO_CONTROLLO] in updateDocumento method in DocumentoDAO: "+documentoVO.getEsitoControllo()+"\n");
			if(documentoVO.getDataEsecuzione() != null) {
				stmt.setTimestamp(++idx, new Timestamp(documentoVO.getDataEsecuzione().getTime()));
			}
			else {
				stmt.setString(++idx, null);
			}
			SolmrLogger.debug(this,"Value of parameter 21 [DATA_ESECUZIONE] in updateDocumento method in DocumentoDAO: "+documentoVO.getDataEsecuzione()+"\n");
			stmt.setString(++idx, documentoVO.getFlagCuaaSoccidarioValidato());
			SolmrLogger.debug(this,"Value of parameter 22 [FLAG_CUAA_SOCCIDARIO_VALIDATO] in updateDocumento method in DocumentoDAO: "+documentoVO.getFlagCuaaSoccidarioValidato()+"\n");
			
      if(Validator.isNotEmpty(documentoVO.getIdContoCorrente())) {
        stmt.setLong(++idx, documentoVO.getIdContoCorrente().longValue());
      }
      else {
        stmt.setString(++idx, null);
      }
      SolmrLogger.debug(this,"Value of parameter 23 [ID_CONTO_CORRENTE] in updateDocumento method in DocumentoDAO: "+documentoVO.getIdContoCorrente()+"\n");
			
      if(Validator.isNotEmpty(documentoVO.getIdCausaleModificaDocumento())) {
        stmt.setLong(++idx, documentoVO.getIdCausaleModificaDocumento().longValue());
      }
      else {
        stmt.setString(++idx, null);
      }
      SolmrLogger.debug(this,"Value of parameter 24 [ID_CAUSALE_MODIFICA_DOCUMENTO] in updateDocumento method in DocumentoDAO: "+documentoVO.getIdCausaleModificaDocumento()+"\n");
			
			stmt.setLong(++idx, documentoVO.getIdDocumento().longValue());
			SolmrLogger.debug(this,"Value of parameter 25 [ID_DOCUMENTO] in updateDocumento method in DocumentoDAO: "+documentoVO.getIdDocumento()+"\n");

			SolmrLogger.debug(this, "Executing updateDocumento: "+query+"\n");

			stmt.executeUpdate();

			stmt.close();

			SolmrLogger.debug(this, "Executed updateDocumento method in DocumentoDAO\n");
		}
		catch (SQLException se) {
			SolmrLogger.error(this, "updateDocumento in DocumentoDAO - SQLException: "+se.getMessage()+"\n");
			throw new DataAccessException(se.getMessage());
		}
		catch (Exception ex) {
			SolmrLogger.error(this, "Generic Exception in updateDocumento: "+ex.getMessage()+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.error(this, "SQLException while closing Statement and Connection in updateDocumento: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in updateDocumento: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated updateDocumento method in DocumentoDAO\n");
	}
	
	/**
	 * 
	 * Uodate del documento utilizzato nel caso di cessazioni particelle in documenti istanza riesame...
	 * 
	 * 
	 * @param idDocumento
	 * @param idStatoDocumento
	 * @param ruoloUtenza
	 * @throws DataAccessException
	 */
	public void updateDocumentoIstanza(Long idDocumento, Long idStatoDocumento) throws DataAccessException 
	{
    SolmrLogger.debug(this, "Invocating updateDocumento method in DocumentoDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;

    try {

      SolmrLogger.debug(this, "Creating db-connection in updateDocumentoIstanza method in DocumentoDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in updateDocumentoIstanza method in DocumentoDAO and it values: "+conn+"\n");


      String query = 
        "UPDATE DB_DOCUMENTO ";
      if(Validator.isNotEmpty(idStatoDocumento))
      {
        query +=
        "SET    ID_STATO_DOCUMENTO = ? ";
      }
      else
      {
        query +=
        "SET    DATA_FINE_VALIDITA = SYSDATE ";
      }
      query +=  
        "WHERE  ID_DOCUMENTO = ? ";

      stmt = conn.prepareStatement(query);

      int idx = 0;
      if(Validator.isNotEmpty(idStatoDocumento))
      {
        stmt.setLong(++idx, idStatoDocumento.longValue());
      }          
      stmt.setLong(++idx, idDocumento.longValue());
      SolmrLogger.debug(this, "Executing updateDocumentoIstanza: "+query+"\n");

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed updateDocumentoIstanza method in DocumentoDAO\n");
    }
    catch (SQLException se) {
      SolmrLogger.error(this, "updateDocumentoIstanza in DocumentoDAO - SQLException: "+se.getMessage()+"\n");
      throw new DataAccessException(se.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.error(this, "Generic Exception in updateDocumentoIstanza: "+ex.getMessage()+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.error(this, "SQLException while closing Statement and Connection in updateDocumentoIstanza: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in updateDocumentoIstanza: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated updateDocumento method in DocumentoDAO\n");
  }

	/**
	 * Metodo che si occupa di effettuare la modifica del record relativa alla tabella
	 * DB_DOCUMENTO_CONDUZIONE
	 *
	 * @param documentoConduzioneVO
	 * @throws DataAccessException
	 */
	public void updateDocumentoConduzione(DocumentoConduzioneVO documentoConduzioneVO) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating updateDocumentoConduzione method in DocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;

		try {

			SolmrLogger.debug(this, "Creating db-connection in updateDocumentoConduzione method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in updateDocumentoConduzione method in DocumentoDAO and it values: "+conn+"\n");


			String query = " UPDATE DB_DOCUMENTO_CONDUZIONE "+
			" SET    ID_CONDUZIONE_PARTICELLA = ?,  " +
			"        ID_DOCUMENTO = ?, " +
			"        DATA_INSERIMENTO = ?, " +
			"        DATA_INIZIO_VALIDITA = ?, " +
			"        DATA_FINE_VALIDITA = ? " +
			" WHERE  ID_DOCUMENTO_CONDUZIONE = ? ";

			stmt = conn.prepareStatement(query);

			stmt.setLong(1, documentoConduzioneVO.getIdConduzioneParticella().longValue());
			SolmrLogger.debug(this,"Value of parameter 1 [ID_CONDUZIONE_PARTICELLA] in updateDocumentoConduzione method in DocumentoDAO: "+documentoConduzioneVO.getIdConduzioneParticella().longValue()+"\n");
			stmt.setLong(2, documentoConduzioneVO.getIdDocumento().longValue());
			SolmrLogger.debug(this,"Value of parameter 2 [ID_DOCUMENTO] in updateDocumentoConduzione method in DocumentoDAO: "+documentoConduzioneVO.getIdDocumento().longValue()+"\n");
			stmt.setTimestamp(3, new Timestamp(documentoConduzioneVO.getDataInserimento().getTime()));
			SolmrLogger.debug(this,"Value of parameter 3 [DATA_INSERIMENTO] in updateDocumentoConduzione method in DocumentoDAO: "+new Timestamp(documentoConduzioneVO.getDataInserimento().getTime())+"\n");
			if(documentoConduzioneVO.getDataInizioValidita() != null) {
				stmt.setTimestamp(4, new Timestamp(documentoConduzioneVO.getDataInizioValidita().getTime()));
				SolmrLogger.debug(this,"Value of parameter 4 [DATA_INIZIO_VALIDITA] in updateDocumentoConduzione method in DocumentoDAO: "+new Timestamp(documentoConduzioneVO.getDataInizioValidita().getTime())+"\n");
			}
			else {
				stmt.setString(4, null);
				SolmrLogger.debug(this,"Value of parameter 4 [DATA_INIZIO_VALIDITA] in updateDocumentoConduzione method in DocumentoDAO: "+null+"\n");
			}
			if(documentoConduzioneVO.getDataFineValidita() != null) {
				stmt.setTimestamp(5, new Timestamp(documentoConduzioneVO.getDataFineValidita().getTime()));
				SolmrLogger.debug(this,"Value of parameter 5 [DATA_FINE_VALIDITA] in updateDocumentoConduzione method in DocumentoDAO: "+new Timestamp(documentoConduzioneVO.getDataFineValidita().getTime())+"\n");
			}
			else {
				stmt.setString(5, null);
				SolmrLogger.debug(this,"Value of parameter 5 [DATA_FINE_VALIDITA] in updateDocumentoConduzione method in DocumentoDAO: "+null+"\n");
			}
			stmt.setLong(6, documentoConduzioneVO.getIdDocumentoConduzione().longValue());
			SolmrLogger.debug(this,"Value of parameter 6 [ID_DOCUMENTO_CONDUZIONE] in updateDocumentoConduzione method in DocumentoDAO: "+documentoConduzioneVO.getIdDocumentoConduzione().longValue()+"\n");

			SolmrLogger.debug(this, "Executing updateDocumentoConduzione: "+query+"\n");

			stmt.executeUpdate();

			stmt.close();

			SolmrLogger.debug(this, "Executed updateDocumentoConduzione method in DocumentoDAO\n");
		}
		catch (SQLException se) {
			SolmrLogger.error(this, "updateDocumentoConduzione in DocumentoDAO - SQLException: "+se.getMessage()+"\n");
			throw new DataAccessException(se.getMessage());
		}
		catch (Exception ex) {
			SolmrLogger.error(this, "Generic Exception in updateDocumentoConduzione: "+ex.getMessage()+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.error(this, "SQLException while closing Statement and Connection in updateDocumentoConduzione: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in updateDocumentoConduzione: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated updateDocumentoConduzione method in DocumentoDAO\n");
	}

	/**
	 * Metodo che mi permette di estrarre la data max di esecuzione controlli dei documenti
	 * relativi ad una determinata azienda
	 *
	 * @param idAzienda
	 * @return dataEsecuzione
	 * @throws DataAccessException
	 */
	public String getDataMaxEsecuzioneDocumento(Long idAzienda) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getDataMaxEsecuzioneDocumento method in DocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		String dataMaxEsecuzione = null;

		try {
			SolmrLogger.debug(this, "Creating db-connection in getDataMaxEsecuzioneDocumento method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getDataMaxEsecuzioneDocumento method in DocumentoDAO and it values: "+conn+"\n");

			String query = " SELECT MAX(DATA_ESECUZIONE) AS DATA_ESECUZIONE " +
			" FROM   DB_DOCUMENTO " +
			" WHERE  ID_AZIENDA = ? ";

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getDataMaxEsecuzioneDocumento method in DocumentoDAO: "+idAzienda+"\n");

			stmt = conn.prepareStatement(query);

			stmt.setLong(1, idAzienda.longValue());

			SolmrLogger.debug(this, "Executing getDataMaxEsecuzioneDocumento: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) {
				dataMaxEsecuzione = rs.getString("DATA_ESECUZIONE");
			}

			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getDataMaxEsecuzioneDocumento in DocumentoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getDataMaxEsecuzioneDocumento in DocumentoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getDataMaxEsecuzioneDocumento in DocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getDataMaxEsecuzioneDocumento in DocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getDataMaxEsecuzioneDocumento method in DocumentoDAO\n");
		return dataMaxEsecuzione;
	}

	/**
	 * Metodo per estrarre i cuaa di tutti i proprietari dei documenti dell'azienda
	 * agricola indicata
	 *
	 * @param idAzienda
	 * @param cuaa
	 * @param onlyActive
	 * @return java.lnag.String[]
	 * @throws DataAccessException
	 */
	public String[] getCuaaProprietariDocumentiAzienda(Long idAzienda, String cuaa, boolean onlyActive) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getCuaaProprietariDocumentiAzienda method in DocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<String> elencoCuaa = new Vector<String>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getCuaaProprietariDocumentiAzienda method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getCuaaProprietariDocumentiAzienda method in DocumentoDAO and it values: "+conn+"\n");

			String query = " SELECT DISTINCT DP.CUAA " +
			" FROM            DB_DOCUMENTO_PROPRIETARIO DP, " +
			"                 DB_DOCUMENTO D " +
			" WHERE           D.ID_AZIENDA = ? " +
			" AND             D.CUAA = ? " +
			" AND             D.ID_DOCUMENTO = DP.ID_DOCUMENTO ";

			if(onlyActive) {
				query +=   " AND             D.ID_STATO_DOCUMENTO IS NULL " +
				" AND             NVL(D.DATA_FINE_VALIDITA, ?) >= TRUNC(SYSDATE) ";
			}

			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getCuaaProprietariDocumentiAzienda method in DocumentoDAO: "+idAzienda+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [CUAA] in getCuaaProprietariDocumentiAzienda method in DocumentoDAO: "+cuaa+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ONLY_ACTIVE] in getCuaaProprietariDocumentiAzienda method in DocumentoDAO: "+onlyActive+"\n");

			stmt = conn.prepareStatement(query);

			stmt.setLong(1, idAzienda.longValue());
			stmt.setString(2, cuaa);
			if(onlyActive) {
				SolmrLogger.debug(this, "Value of parameter 3 [DATA_FINE_VALIDITA] in getCuaaProprietariDocumentiAzienda method in DocumentoDAO: "+SolmrConstants.ORACLE_FINAL_DATE+"\n");
				stmt.setDate(3, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
			}

			SolmrLogger.debug(this, "Executing getCuaaProprietariDocumentiAzienda: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				if(!rs.getString("CUAA").trim().equalsIgnoreCase("-")) {
					elencoCuaa.add(rs.getString("CUAA"));
				}
			}

			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getCuaaProprietariDocumentiAzienda in DocumentoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getCuaaProprietariDocumentiAzienda in DocumentoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getCuaaProprietariDocumentiAzienda in DocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getCuaaProprietariDocumentiAzienda in DocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getCuaaProprietariDocumentiAzienda method in DocumentoDAO\n");
		if(elencoCuaa.size() == 0) {
			return(String[])elencoCuaa.toArray(new String[0]);
		}
		else {
			return(String[])elencoCuaa.toArray(new String[elencoCuaa.size()]);
		}
	}

	/**
	 * Metodo che mi permette di reperire tutti i documenti di un'azienda in funzione
	 * di un determinato id controllo: necessario per la correzione delle anomalie
	 *
	 * @param anagAziendaVO
	 * @param idControllo
	 * @param onlyActive
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.DocumentoVO[]
	 * @throws DataAccessException
	 */
	public DocumentoVO[] getListDocumentiAziendaByIdControllo(AnagAziendaVO anagAziendaVO, Long idControllo, boolean onlyActive, String[] orderBy) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating getListDocumentiAziendaByIdControllo method in DocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;
		Vector<DocumentoVO> elencoDocumenti = new Vector<DocumentoVO>();

		try {
			SolmrLogger.debug(this, "Creating db-connection in getListDocumentiAziendaByIdControllo method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in getListDocumentiAziendaByIdControllo method in DocumentoDAO and it values: "+conn+"\n");

			String query = " SELECT D.ID_DOCUMENTO, " +
			"        D.EXT_ID_DOCUMENTO, " +
			"        TD.DESCRIZIONE AS DESC_DOC, " +
			"        D.ID_STATO_DOCUMENTO, " +
			"        D.ID_AZIENDA, " +
			"        D.CUAA, " +
			"        D.DATA_INIZIO_VALIDITA, " +
			"        D.DATA_FINE_VALIDITA, " +
			"        D.NUMERO_PROTOCOLLO, " +
			"        D.DATA_PROTOCOLLO, " +
			"        D.NUMERO_DOCUMENTO, " +
			"        D.ENTE_RILASCIO_DOCUMENTO, " +
			"        D.ID_DOCUMENTO_PRECEDENTE, " +
			"        D.NOTE, " +
			"        D.DATA_ULTIMO_AGGIORNAMENTO, " +
			"        D.UTENTE_ULTIMO_AGGIORNAMENTO, " +
			"        D.DATA_INSERIMENTO, " +
			"        D.DATA_VARIAZIONE_STATO, " +
			"        D.ID_UTENTE_AGGIORNAMENTO_SRV, " +
			"        D.NUMERO_PROTOCOLLO_ESTERNO, " +
			"        D.CUAA_SOCCIDARIO, " +
			"        D.ESITO_CONTROLLO, " +
			"        D.DATA_ESECUZIONE, " +
			"        D.FLAG_CUAA_SOCCIDARIO_VALIDATO " +
			" FROM   DB_DOCUMENTO D, " +
			"        DB_TIPO_DOCUMENTO TD, " +
			"        DB_DOCUMENTO_CONTROLLO DC " +
			" WHERE  D.ID_AZIENDA = ? " +
			" AND    D.CUAA = ? " +
			" AND    DC.ID_CONTROLLO = ? " +
			" AND    D.EXT_ID_DOCUMENTO = TD.ID_DOCUMENTO " +
			" AND    TD.ID_DOCUMENTO = DC.EXT_ID_DOCUMENTO ";
			if(onlyActive) {
				query +=   " AND    ID_STATO_DOCUMENTO IS NULL " +
				" AND    NVL(D.DATA_FINE_VALIDITA, ?) > TRUNC(SYSDATE) ";
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


			SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListDocumentiAziendaByIdControllo method in DocumentoDAO: "+anagAziendaVO.getIdAzienda()+"\n");
			SolmrLogger.debug(this, "Value of parameter 2 [CUAA] in getListDocumentiAziendaByIdControllo method in DocumentoDAO: "+anagAziendaVO.getCUAA()+"\n");
			SolmrLogger.debug(this, "Value of parameter 3 [ID_CONTROLLO] in getListDocumentiAziendaByIdControllo method in DocumentoDAO: "+idControllo+"\n");
			SolmrLogger.debug(this, "Value of parameter 4 [ONLY_ACTIVE] in getListDocumentiAziendaByIdControllo method in DocumentoDAO: "+onlyActive+"\n");
			SolmrLogger.debug(this, "Value of parameter 5 [ORACLE_FINAL_DATE] in getListDocumentiAziendaByIdControllo method in DocumentoDAO: "+SolmrConstants.ORACLE_FINAL_DATE+"\n");

			stmt = conn.prepareStatement(query);

			stmt.setLong(1, anagAziendaVO.getIdAzienda().longValue());
			stmt.setString(2, anagAziendaVO.getCUAA());
			stmt.setLong(3, idControllo.longValue());
			if(onlyActive) {
				stmt.setDate(4, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
			}

			SolmrLogger.debug(this, "Executing getListDocumentiAziendaByIdControllo: "+query+"\n");

			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				DocumentoVO documentoVO = new DocumentoVO();
				documentoVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
				documentoVO.setExtIdDocumento(new Long(rs.getLong("EXT_ID_DOCUMENTO")));
				TipoDocumentoVO tipoDocumentoVO = new TipoDocumentoVO();
				tipoDocumentoVO.setIdDocumento(new Long(rs.getLong("EXT_ID_DOCUMENTO")));
				tipoDocumentoVO.setDescrizione(rs.getString("DESC_DOC"));
				documentoVO.setTipoDocumentoVO(tipoDocumentoVO);
				if(Validator.isNotEmpty(rs.getString("ID_STATO_DOCUMENTO"))) {
					documentoVO.setIdStatoDocumento(new Long(rs.getLong("ID_STATO_DOCUMENTO")));
				}
				documentoVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
				documentoVO.setCuaa(rs.getString("CUAA"));
				documentoVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
				documentoVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
				documentoVO.setNumeroProtocollo(rs.getString("NUMERO_PROTOCOLLO"));
				documentoVO.setDataProtocollo(rs.getDate("DATA_PROTOCOLLO"));
				documentoVO.setNumeroDocumento(rs.getString("NUMERO_DOCUMENTO"));
				documentoVO.setEnteRilascioDocumento(rs.getString("ENTE_RILASCIO_DOCUMENTO"));
				if(Validator.isNotEmpty(rs.getString("ID_DOCUMENTO_PRECEDENTE"))) {
					documentoVO.setIdDocumentoPrecedente(new Long(rs.getLong("ID_DOCUMENTO_PRECEDENTE")));
				}
				documentoVO.setNote(rs.getString("NOTE"));
				documentoVO.setDataUltimoAggiornamento(rs.getDate("DATA_ULTIMO_AGGIORNAMENTO"));
				documentoVO.setUtenteUltimoAggiornamento(new Long(rs.getLong("UTENTE_ULTIMO_AGGIORNAMENTO")));
				documentoVO.setDataInserimento(rs.getDate("DATA_INSERIMENTO"));
				documentoVO.setDataVariazioneStato(rs.getDate("DATA_VARIAZIONE_STATO"));
				if(Validator.isNotEmpty(rs.getString("ID_UTENTE_AGGIORNAMENTO_SRV"))) {
					documentoVO.setIdUtenteAggiornamentoSrv(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO_SRV")));
				}
				documentoVO.setNumeroProtocolloEsterno(rs.getString("NUMERO_PROTOCOLLO_ESTERNO"));
				documentoVO.setCuaaSoccidario(rs.getString("CUAA_SOCCIDARIO"));
				documentoVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
				documentoVO.setDataEsecuzione(rs.getDate("DATA_ESECUZIONE"));
				documentoVO.setFlagCuaaSoccidarioValidato(rs.getString("FLAG_CUAA_SOCCIDARIO_VALIDATO"));
				elencoDocumenti.add(documentoVO);
			}

			rs.close();
			stmt.close();

		}
		catch(SQLException exc) {
			SolmrLogger.error(this, "getListDocumentiAziendaByIdControllo in DocumentoDAO - SQLException: "+exc.getMessage()+"\n");
			throw new DataAccessException(exc.getMessage());
		}
		catch(Exception ex) {
			SolmrLogger.error(this, "getListDocumentiAziendaByIdControllo in DocumentoDAO - Generic Exception: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}
			catch(SQLException exc) {
				SolmrLogger.error(this, "getListDocumentiAziendaByIdControllo in DocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch(Exception ex) {
				SolmrLogger.error(this, "getListDocumentiAziendaByIdControllo in DocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
		SolmrLogger.debug(this, "Invocated getListDocumentiAziendaByIdControllo method in DocumentoDAO\n");
		if(elencoDocumenti.size() == 0) {
			return(DocumentoVO[])elencoDocumenti.toArray(new DocumentoVO[0]);
		}
		else {
			return(DocumentoVO[])elencoDocumenti.toArray(new DocumentoVO[elencoDocumenti.size()]);
		}
	}
	
	/**
	 * Metodo che effettua la cessazione del legame, su DB_DOCUMENTO_CONDUZIONE,
	 * tra il documento selezionato e le particelle ad esso associatw
	 * 
	 * @param documentoConduzione
	 * @throws DataAccessException
	 */
	public void cessaDocumentoConduzioneByIdDocumento(DocumentoConduzioneVO documentoConduzioneVO) throws DataAccessException {
		SolmrLogger.debug(this, "Invocating cessaDocumentoConduzioneByIdDocumento method in DocumentoDAO\n");
		Connection conn = null;
		PreparedStatement stmt = null;

		try {

			SolmrLogger.debug(this, "Creating db-connection in cessaDocumentoConduzioneByIdDocumento method in DocumentoDAO\n");
			conn = getDatasource().getConnection();
			SolmrLogger.debug(this, "Created db-connection in cessaDocumentoConduzioneByIdDocumento method in DocumentoDAO and it values: "+conn+"\n");


			String query = " UPDATE DB_DOCUMENTO_CONDUZIONE " +
						   " SET    DATA_FINE_VALIDITA = ?  " +
						   " WHERE  ID_DOCUMENTO = ? " +
						   " AND    NVL(DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'DD/MM/YYYY')) > SYSDATE "; 

			stmt = conn.prepareStatement(query);

			SolmrLogger.debug(this,"Value of parameter 1 [DATA_FINE_VALIDITA] in cessaDocumentoConduzioneByIdDocumento method in DocumentoDAO: "+documentoConduzioneVO.getDataFineValidita()+"\n");
			stmt.setTimestamp(1, new Timestamp(documentoConduzioneVO.getDataFineValidita().getTime()));
			SolmrLogger.debug(this,"Value of parameter 2 [ID_DOCUMENTO] in cessaDocumentoConduzioneByIdDocumento method in DocumentoDAO: "+documentoConduzioneVO.getIdDocumento()+"\n");
			stmt.setLong(2, documentoConduzioneVO.getIdDocumento().longValue());

			SolmrLogger.debug(this, "Executing cessaDocumentoConduzioneByIdDocumento: "+query+"\n");

			stmt.executeUpdate();

			stmt.close();

			SolmrLogger.debug(this, "Executed storicizzaDocumento method in DocumentoDAO\n");
		}
		catch (SQLException se) {
			SolmrLogger.error(this, "storicizzaDocumento in DocumentoDAO - SQLException: "+se+"\n");
			throw new DataAccessException(se.getMessage());
		}
		catch (Exception ex) {
			SolmrLogger.error(this, "Generic Exception in storicizzaDocumento: "+ex+"\n");
			throw new DataAccessException(ex.getMessage());
		}
		finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			}
			catch (SQLException exc) {
				SolmrLogger.error(this, "SQLException while closing Statement and Connection in storicizzaDocumento: "+exc+"\n");
				throw new DataAccessException(exc.getMessage());
			}
			catch (Exception ex) {
				SolmrLogger.error(this, "Generic Exception while closing Statement and Connection in storicizzaDocumento: "+ex+"\n");
				throw new DataAccessException(ex.getMessage());
			}
		}
	}


	
	
	/**
   * Metodo che mi permette di reperire tutti i documenti di un'azienda in funzione
   * di un determinato id controllo,particella e tipologia documento: 
   * necessario per la correzione delle anomalie
   *
   * @param anagAziendaVO
   * @param idControllo
   * @param idStoricoParticella
   * @param tipologiaDocumento
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.DocumentoVO[]
   * @throws DataAccessException
   */
  public DocumentoVO[] getListDocumentiAziendaByIdControlloAndParticella(AnagAziendaVO anagAziendaVO, Long idControllo, 
      Long idStoricoParticella, boolean onlyActive, String[] orderBy) throws DataAccessException {
    SolmrLogger.debug(this, "Invocating getListDocumentiAziendaByIdControlloAndParticella method in DocumentoDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<DocumentoVO> elencoDocumenti = new Vector<DocumentoVO>();

    try {
      SolmrLogger.debug(this, "Creating db-connection in getListDocumentiAziendaByIdControlloAndParticella method in DocumentoDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListDocumentiAziendaByIdControlloAndParticella method in DocumentoDAO and it values: "+conn+"\n");

      String query = " SELECT DISTINCT D.ID_DOCUMENTO, " +
      "        D.EXT_ID_DOCUMENTO, " +
      "        TD.DESCRIZIONE AS DESC_DOC, " +
      "        TD.ID_TIPOLOGIA_DOCUMENTO, " +
      "        D.ID_STATO_DOCUMENTO, " +
      "        D.ID_AZIENDA, " +
      "        D.CUAA, " +
      "        D.DATA_INIZIO_VALIDITA, " +
      "        D.DATA_FINE_VALIDITA, " +
      "        D.NUMERO_PROTOCOLLO, " +
      "        D.DATA_PROTOCOLLO, " +
      "        D.NUMERO_DOCUMENTO, " +
      "        D.ENTE_RILASCIO_DOCUMENTO, " +
      "        D.ID_DOCUMENTO_PRECEDENTE, " +
      "        D.NOTE, " +
      "        D.DATA_ULTIMO_AGGIORNAMENTO, " +
      "        D.UTENTE_ULTIMO_AGGIORNAMENTO, " +
      "        D.DATA_INSERIMENTO, " +
      "        D.DATA_VARIAZIONE_STATO, " +
      "        D.ID_UTENTE_AGGIORNAMENTO_SRV, " +
      "        D.NUMERO_PROTOCOLLO_ESTERNO, " +
      "        D.CUAA_SOCCIDARIO, " +
      "        D.ESITO_CONTROLLO, " +
      "        D.DATA_ESECUZIONE, " +
      "        D.FLAG_CUAA_SOCCIDARIO_VALIDATO "+
      " FROM   DB_DOCUMENTO D, " +
      "        DB_TIPO_DOCUMENTO TD, " +
      "        DB_DOCUMENTO_CONTROLLO DC, ";
      //Aggiunta per gestione con associazione con particella
      query += "DB_DOCUMENTO_CONDUZIONE DCN, DB_CONDUZIONE_PARTICELLA DCP, " +
          "DB_STORICO_PARTICELLA DSP ";
      //****************************************
      query += " WHERE  D.ID_AZIENDA = ? " +
      " AND    D.CUAA = ? " +
      " AND    DC.ID_CONTROLLO = ? " +
      " AND    D.EXT_ID_DOCUMENTO = TD.ID_DOCUMENTO " +
      " AND    TD.ID_DOCUMENTO = DC.EXT_ID_DOCUMENTO ";
      //Aggiunta per gestione con associazione con particella
      query += "AND DCN.ID_DOCUMENTO = d.ID_DOCUMENTO  "+
          "AND DCN.ID_CONDUZIONE_PARTICELLA = DCP.ID_CONDUZIONE_PARTICELLA  "+
          "AND DCP.ID_PARTICELLA = DSP.ID_PARTICELLA "+
          "AND DSP.DATA_FINE_VALIDITA IS NULL "+
          "AND DCP.DATA_FINE_CONDUZIONE IS NULL "+
          "AND NVL(DCN.DATA_FINE_VALIDITA, ? ) > TRUNC(SYSDATE) "+
          "AND DSP.ID_STORICO_PARTICELLA = ? ";
      
      //******************************************************
      if(onlyActive) {
        query +=   " AND    ID_STATO_DOCUMENTO IS NULL " +
        " AND    NVL(D.DATA_FINE_VALIDITA, ? ) > TRUNC(SYSDATE) ";
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


      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListDocumentiAziendaByIdControlloAndParticella method in DocumentoDAO: "+anagAziendaVO.getIdAzienda()+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [CUAA] in getListDocumentiAziendaByIdControlloAndParticella method in DocumentoDAO: "+anagAziendaVO.getCUAA()+"\n");
      SolmrLogger.debug(this, "Value of parameter 3 [ID_CONTROLLO] in getListDocumentiAziendaByIdControlloAndParticella method in DocumentoDAO: "+idControllo+"\n");
      SolmrLogger.debug(this, "Value of parameter 4 [ORACLE_FINAL_DATE] in getListDocumentiAziendaByIdControlloAndParticella method in DocumentoDAO: "+SolmrConstants.ORACLE_FINAL_DATE+"\n");
      SolmrLogger.debug(this, "Value of parameter 5 [ID_STORICO_PARICELLA] in getListDocumentiAziendaByIdControlloAndParticella method in DocumentoDAO: "+idStoricoParticella+"\n");
      SolmrLogger.debug(this, "Value of parameter 6 [ONLY_ACTIVE] in getListDocumentiAziendaByIdControlloAndParticella method in DocumentoDAO: "+onlyActive+"\n");
      SolmrLogger.debug(this, "Value of parameter 7 [ORACLE_FINAL_DATE] in getListDocumentiAziendaByIdControlloAndParticella method in DocumentoDAO: "+SolmrConstants.ORACLE_FINAL_DATE+"\n");

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, anagAziendaVO.getIdAzienda().longValue());
      stmt.setString(2, anagAziendaVO.getCUAA());
      stmt.setLong(3, idControllo.longValue());
      stmt.setDate(4, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
      stmt.setLong(5, idStoricoParticella.longValue());
      if(onlyActive) {
        stmt.setDate(6, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
      }

      SolmrLogger.debug(this, "Executing getListDocumentiAziendaByIdControlloAndParticella: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) {
        DocumentoVO documentoVO = new DocumentoVO();
        documentoVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
        documentoVO.setExtIdDocumento(new Long(rs.getLong("EXT_ID_DOCUMENTO")));
        TipoDocumentoVO tipoDocumentoVO = new TipoDocumentoVO();
        tipoDocumentoVO.setIdDocumento(new Long(rs.getLong("EXT_ID_DOCUMENTO")));
        tipoDocumentoVO.setDescrizione(rs.getString("DESC_DOC"));
        
        documentoVO.setTipoDocumentoVO(tipoDocumentoVO);
        if(Validator.isNotEmpty(rs.getString("ID_STATO_DOCUMENTO"))) {
          documentoVO.setIdStatoDocumento(new Long(rs.getLong("ID_STATO_DOCUMENTO")));
        }
        documentoVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        documentoVO.setCuaa(rs.getString("CUAA"));
        documentoVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        documentoVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        documentoVO.setNumeroProtocollo(rs.getString("NUMERO_PROTOCOLLO"));
        documentoVO.setDataProtocollo(rs.getDate("DATA_PROTOCOLLO"));
        documentoVO.setNumeroDocumento(rs.getString("NUMERO_DOCUMENTO"));
        documentoVO.setEnteRilascioDocumento(rs.getString("ENTE_RILASCIO_DOCUMENTO"));
        if(Validator.isNotEmpty(rs.getString("ID_DOCUMENTO_PRECEDENTE"))) {
          documentoVO.setIdDocumentoPrecedente(new Long(rs.getLong("ID_DOCUMENTO_PRECEDENTE")));
        }
        documentoVO.setNote(rs.getString("NOTE"));
        documentoVO.setDataUltimoAggiornamento(rs.getDate("DATA_ULTIMO_AGGIORNAMENTO"));
        documentoVO.setUtenteUltimoAggiornamento(new Long(rs.getLong("UTENTE_ULTIMO_AGGIORNAMENTO")));
        documentoVO.setDataInserimento(rs.getDate("DATA_INSERIMENTO"));
        documentoVO.setDataVariazioneStato(rs.getDate("DATA_VARIAZIONE_STATO"));
        if(Validator.isNotEmpty(rs.getString("ID_UTENTE_AGGIORNAMENTO_SRV"))) {
          documentoVO.setIdUtenteAggiornamentoSrv(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO_SRV")));
        }
        documentoVO.setNumeroProtocolloEsterno(rs.getString("NUMERO_PROTOCOLLO_ESTERNO"));
        documentoVO.setCuaaSoccidario(rs.getString("CUAA_SOCCIDARIO"));
        documentoVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
        documentoVO.setDataEsecuzione(rs.getDate("DATA_ESECUZIONE"));
        documentoVO.setFlagCuaaSoccidarioValidato(rs.getString("FLAG_CUAA_SOCCIDARIO_VALIDATO"));
        elencoDocumenti.add(documentoVO);
      }

      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getListDocumentiAziendaByIdControlloAndParticella in DocumentoDAO - SQLException: "+exc.getMessage()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getListDocumentiAziendaByIdControlloAndParticella in DocumentoDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getListDocumentiAziendaByIdControlloAndParticella in DocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getListDocumentiAziendaByIdControlloAndParticella in DocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListDocumentiAziendaByIdControlloAndParticella method in DocumentoDAO\n");
    if(elencoDocumenti.size() == 0) {
      return(DocumentoVO[])elencoDocumenti.toArray(new DocumentoVO[0]);
    }
    else {
      return(DocumentoVO[])elencoDocumenti.toArray(new DocumentoVO[elencoDocumenti.size()]);
    }
  }
  
  /**
   * 
   * Metodo che mi permette di reperire tutti i documenti di un'azienda in funzione
   * di un determinato id controllo,particella e tipologia documento solo per tipologia di
   * dichiarazione correttiva: 
   * necessario per la correzione delle anomalie
   * 
   * 
   * @param anagAziendaVO
   * @param idControllo
   * @param idStoricoParticella
   * @param annoCampagna
   * @param orderBy
   * @return
   * @throws DataAccessException
   */
  public DocumentoVO[] getListDocumentiPerDichCorrettiveAziendaByIdControlloAndParticella(AnagAziendaVO anagAziendaVO, Long idControllo, 
      Long idStoricoParticella, String annoCampagna, String[] orderBy) throws DataAccessException {
    SolmrLogger.debug(this, "Invocating getListDocumentiPerDichCorrettiveAziendaByIdControlloAndParticella method in DocumentoDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<DocumentoVO> elencoDocumenti = new Vector<DocumentoVO>();

    try {
      SolmrLogger.debug(this, "Creating db-connection in getListDocumentiPerDichCorrettiveAziendaByIdControlloAndParticella method in DocumentoDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListDocumentiPerDichCorrettiveAziendaByIdControlloAndParticella method in DocumentoDAO and it values: "+conn+"\n");

      String query = " SELECT D.ID_DOCUMENTO, " +
      "        D.EXT_ID_DOCUMENTO, " +
      "        TD.DESCRIZIONE AS DESC_DOC, " +
      "        TD.ID_TIPOLOGIA_DOCUMENTO, " +
      "        D.ID_STATO_DOCUMENTO, " +
      "        D.ID_AZIENDA, " +
      "        D.CUAA, " +
      "        D.DATA_INIZIO_VALIDITA, " +
      "        D.DATA_FINE_VALIDITA, " +
      "        D.NUMERO_PROTOCOLLO, " +
      "        D.DATA_PROTOCOLLO, " +
      "        D.NUMERO_DOCUMENTO, " +
      "        D.ENTE_RILASCIO_DOCUMENTO, " +
      "        D.ID_DOCUMENTO_PRECEDENTE, " +
      "        D.NOTE, " +
      "        D.DATA_ULTIMO_AGGIORNAMENTO, " +
      "        D.UTENTE_ULTIMO_AGGIORNAMENTO, " +
      "        D.DATA_INSERIMENTO, " +
      "        D.DATA_VARIAZIONE_STATO, " +
      "        D.ID_UTENTE_AGGIORNAMENTO_SRV, " +
      "        D.NUMERO_PROTOCOLLO_ESTERNO, " +
      "        D.CUAA_SOCCIDARIO, " +
      "        D.ESITO_CONTROLLO, " +
      "        D.DATA_ESECUZIONE, " +
      "        D.FLAG_CUAA_SOCCIDARIO_VALIDATO "+
      " FROM   DB_DOCUMENTO D, " +
      "        DB_TIPO_DOCUMENTO TD, " +
      "        DB_DOCUMENTO_CONTROLLO DC, ";
      //Aggiunta per gestione con associazione con particella
      query += "DB_DOCUMENTO_CONDUZIONE DCN, DB_CONDUZIONE_PARTICELLA DCP, " +
          "DB_STORICO_PARTICELLA DSP ";
      //****************************************
      query += " " +
      "WHERE  D.ID_AZIENDA = ? " +
      " AND    D.CUAA = ? " +
      " AND    DC.ID_CONTROLLO = ? " +
      " AND    D.EXT_ID_DOCUMENTO = TD.ID_DOCUMENTO " +
      " AND    TD.ID_DOCUMENTO = DC.EXT_ID_DOCUMENTO ";
      //Aggiunta per gestione con associazione con particella
      query += "AND DCN.ID_DOCUMENTO = d.ID_DOCUMENTO  "+
          "AND DCN.ID_CONDUZIONE_PARTICELLA = DCP.ID_CONDUZIONE_PARTICELLA  "+
          "AND DCP.ID_PARTICELLA = DSP.ID_PARTICELLA "+
          "AND DSP.DATA_FINE_VALIDITA IS NULL "+
          "AND DCP.DATA_FINE_CONDUZIONE IS NULL "+
          "AND NVL(DCN.DATA_FINE_VALIDITA, ? ) > TRUNC(SYSDATE) "+
          "AND DSP.ID_STORICO_PARTICELLA = ? ";
      
      //******************************************************
      query +=   
        " AND  NVL(TD.DATA_FINE_VALIDITA, ? ) >  TRUNC(SYSDATE) ";
        //" AND  TO_CHAR(D.DATA_INIZIO_VALIDITA, 'YYYY') <= ? ";
      
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


      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListDocumentiPerDichCorrettiveAziendaByIdControlloAndParticella method in DocumentoDAO: "+anagAziendaVO.getIdAzienda()+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [CUAA] in getListDocumentiPerDichCorrettiveAziendaByIdControlloAndParticella method in DocumentoDAO: "+anagAziendaVO.getCUAA()+"\n");
      SolmrLogger.debug(this, "Value of parameter 3 [ID_CONTROLLO] in getListDocumentiPerDichCorrettiveAziendaByIdControlloAndParticella method in DocumentoDAO: "+idControllo+"\n");
      SolmrLogger.debug(this, "Value of parameter 4 [ORACLE_FINAL_DATE] in getListDocumentiPerDichCorrettiveAziendaByIdControlloAndParticella method in DocumentoDAO: "+SolmrConstants.ORACLE_FINAL_DATE+"\n");
      SolmrLogger.debug(this, "Value of parameter 5 [ID_STORICO_PARICELLA] in getListDocumentiPerDichCorrettiveAziendaByIdControlloAndParticella method in DocumentoDAO: "+idStoricoParticella+"\n");
      SolmrLogger.debug(this, "Value of parameter 6 [ORACLE_FINAL_DATE] in getListDocumentiPerDichCorrettiveAziendaByIdControlloAndParticella method in DocumentoDAO: "+SolmrConstants.ORACLE_FINAL_DATE+"\n");
      //SolmrLogger.debug(this, "Value of parameter 7 [annoCampagna] in getListDocumentiPerDichCorrettiveAziendaByIdControlloAndParticella method in DocumentoDAO: "+annoCampagna+"\n");

      stmt = conn.prepareStatement(query);
      
      int idx=0;

      stmt.setLong(++idx, anagAziendaVO.getIdAzienda().longValue());
      stmt.setString(++idx, anagAziendaVO.getCUAA());
      stmt.setLong(++idx, idControllo.longValue());
      stmt.setDate(++idx, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
      stmt.setLong(++idx, idStoricoParticella.longValue());
      stmt.setDate(++idx, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
      //stmt.setString(++idx, annoCampagna);

      SolmrLogger.debug(this, "Executing getListDocumentiPerDichCorrettiveAziendaByIdControlloAndParticella: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) {
        DocumentoVO documentoVO = new DocumentoVO();
        documentoVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
        documentoVO.setExtIdDocumento(new Long(rs.getLong("EXT_ID_DOCUMENTO")));
        TipoDocumentoVO tipoDocumentoVO = new TipoDocumentoVO();
        tipoDocumentoVO.setIdDocumento(new Long(rs.getLong("EXT_ID_DOCUMENTO")));
        tipoDocumentoVO.setDescrizione(rs.getString("DESC_DOC"));
        tipoDocumentoVO.setIdTipologiaDocumento(checkLongNull(rs.getString("ID_TIPOLOGIA_DOCUMENTO")));
        
        documentoVO.setTipoDocumentoVO(tipoDocumentoVO);
        if(Validator.isNotEmpty(rs.getString("ID_STATO_DOCUMENTO"))) {
          documentoVO.setIdStatoDocumento(new Long(rs.getLong("ID_STATO_DOCUMENTO")));
        }
        documentoVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        documentoVO.setCuaa(rs.getString("CUAA"));
        documentoVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        documentoVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        documentoVO.setNumeroProtocollo(rs.getString("NUMERO_PROTOCOLLO"));
        documentoVO.setDataProtocollo(rs.getDate("DATA_PROTOCOLLO"));
        documentoVO.setNumeroDocumento(rs.getString("NUMERO_DOCUMENTO"));
        documentoVO.setEnteRilascioDocumento(rs.getString("ENTE_RILASCIO_DOCUMENTO"));
        if(Validator.isNotEmpty(rs.getString("ID_DOCUMENTO_PRECEDENTE"))) {
          documentoVO.setIdDocumentoPrecedente(new Long(rs.getLong("ID_DOCUMENTO_PRECEDENTE")));
        }
        documentoVO.setNote(rs.getString("NOTE"));
        documentoVO.setDataUltimoAggiornamento(rs.getDate("DATA_ULTIMO_AGGIORNAMENTO"));
        documentoVO.setUtenteUltimoAggiornamento(new Long(rs.getLong("UTENTE_ULTIMO_AGGIORNAMENTO")));
        documentoVO.setDataInserimento(rs.getDate("DATA_INSERIMENTO"));
        documentoVO.setDataVariazioneStato(rs.getDate("DATA_VARIAZIONE_STATO"));
        if(Validator.isNotEmpty(rs.getString("ID_UTENTE_AGGIORNAMENTO_SRV"))) {
          documentoVO.setIdUtenteAggiornamentoSrv(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO_SRV")));
        }
        documentoVO.setNumeroProtocolloEsterno(rs.getString("NUMERO_PROTOCOLLO_ESTERNO"));
        documentoVO.setCuaaSoccidario(rs.getString("CUAA_SOCCIDARIO"));
        documentoVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
        documentoVO.setDataEsecuzione(rs.getDate("DATA_ESECUZIONE"));
        documentoVO.setFlagCuaaSoccidarioValidato(rs.getString("FLAG_CUAA_SOCCIDARIO_VALIDATO"));
        elencoDocumenti.add(documentoVO);
      }

      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getListDocumentiPerDichCorrettiveAziendaByIdControlloAndParticella in DocumentoDAO - SQLException: "+exc.getMessage()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getListDocumentiPerDichCorrettiveAziendaByIdControlloAndParticella in DocumentoDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getListDocumentiPerDichCorrettiveAziendaByIdControlloAndParticella in DocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getListDocumentiPerDichCorrettiveAziendaByIdControlloAndParticella in DocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListDocumentiPerDichCorrettiveAziendaByIdControlloAndParticella method in DocumentoDAO\n");
    if(elencoDocumenti.size() == 0) {
      return(DocumentoVO[])elencoDocumenti.toArray(new DocumentoVO[0]);
    }
    else {
      return(DocumentoVO[])elencoDocumenti.toArray(new DocumentoVO[elencoDocumenti.size()]);
    }
  }
  
  /**
   * 
   * Metodo che mi permette di reperire tutti i documenti di un'azienda in funzione
   * di un determinato id controllo, solo in funzione delle dichiarazione correttive:
   * necessario per la correzione delle anomalie
   * 
   * 
   * @param anagAziendaVO
   * @param idControllo
   * @param annoCampagna
   * @param orderBy
   * @return
   * @throws DataAccessException
   */
  public DocumentoVO[] getListDocumentiPerDichCorrettiveAziendaByIdControllo(
        AnagAziendaVO anagAziendaVO, Long idControllo, String annoCampagna, String[] orderBy) throws DataAccessException {
    SolmrLogger.debug(this, "Invocating getListDocumentiAziendaByIdControllo method in DocumentoDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<DocumentoVO> elencoDocumenti = new Vector<DocumentoVO>();

    try {
      SolmrLogger.debug(this, "Creating db-connection in getListDocumentiPerDichCorrettiveAziendaByIdControllo method in DocumentoDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in getListDocumentiPerDichCorrettiveAziendaByIdControllo method in DocumentoDAO and it values: "+conn+"\n");

      String query = " SELECT D.ID_DOCUMENTO, " +
      "        D.EXT_ID_DOCUMENTO, " +
      "        TD.DESCRIZIONE AS DESC_DOC, " +
      "        TD.ID_TIPOLOGIA_DOCUMENTO, " +
      "        D.ID_STATO_DOCUMENTO, " +
      "        D.ID_AZIENDA, " +
      "        D.CUAA, " +
      "        D.DATA_INIZIO_VALIDITA, " +
      "        D.DATA_FINE_VALIDITA, " +
      "        D.NUMERO_PROTOCOLLO, " +
      "        D.DATA_PROTOCOLLO, " +
      "        D.NUMERO_DOCUMENTO, " +
      "        D.ENTE_RILASCIO_DOCUMENTO, " +
      "        D.ID_DOCUMENTO_PRECEDENTE, " +
      "        D.NOTE, " +
      "        D.DATA_ULTIMO_AGGIORNAMENTO, " +
      "        D.UTENTE_ULTIMO_AGGIORNAMENTO, " +
      "        D.DATA_INSERIMENTO, " +
      "        D.DATA_VARIAZIONE_STATO, " +
      "        D.ID_UTENTE_AGGIORNAMENTO_SRV, " +
      "        D.NUMERO_PROTOCOLLO_ESTERNO, " +
      "        D.CUAA_SOCCIDARIO, " +
      "        D.ESITO_CONTROLLO, " +
      "        D.DATA_ESECUZIONE, " +
      "        D.FLAG_CUAA_SOCCIDARIO_VALIDATO " +
      " FROM   DB_DOCUMENTO D, " +
      "        DB_TIPO_DOCUMENTO TD, " +
      "        DB_DOCUMENTO_CONTROLLO DC " +
      " WHERE  D.ID_AZIENDA = ? " +
      " AND    D.CUAA = ? " +
      " AND    DC.ID_CONTROLLO = ? " +
      " AND    D.EXT_ID_DOCUMENTO = TD.ID_DOCUMENTO " +
      " AND    TD.ID_DOCUMENTO = DC.EXT_ID_DOCUMENTO ";
     
      query +=   
        " AND  NVL(TD.DATA_FINE_VALIDITA, ? ) >  TRUNC(SYSDATE) ";
        //" AND  TO_CHAR(D.DATA_INIZIO_VALIDITA, 'YYYY') <= ? ";
      
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


      SolmrLogger.debug(this, "Value of parameter 1 [ID_AZIENDA] in getListDocumentiPerDichCorrettiveAziendaByIdControllo method in DocumentoDAO: "+anagAziendaVO.getIdAzienda()+"\n");
      SolmrLogger.debug(this, "Value of parameter 2 [CUAA] in getListDocumentiPerDichCorrettiveAziendaByIdControllo method in DocumentoDAO: "+anagAziendaVO.getCUAA()+"\n");
      SolmrLogger.debug(this, "Value of parameter 3 [ID_CONTROLLO] in getListDocumentiPerDichCorrettiveAziendaByIdControllo method in DocumentoDAO: "+idControllo+"\n");
      SolmrLogger.debug(this, "Value of parameter 4 [ORACLE_FINAL_DATE] in getListDocumentiPerDichCorrettiveAziendaByIdControllo method in DocumentoDAO: "+SolmrConstants.ORACLE_FINAL_DATE+"\n");
      //SolmrLogger.debug(this, "Value of parameter 5 [ANNO_CAMPAGNA] in getListDocumentiPerDichCorrettiveAziendaByIdControllo method in DocumentoDAO: "+annoCampagna+"\n");

      stmt = conn.prepareStatement(query);

      int idx = 0;
      stmt.setLong(++idx, anagAziendaVO.getIdAzienda().longValue());
      stmt.setString(++idx, anagAziendaVO.getCUAA());
      stmt.setLong(++idx, idControllo.longValue());
      stmt.setDate(++idx, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
      //stmt.setString(++idx, annoCampagna);
      

      SolmrLogger.debug(this, "Executing getListDocumentiPerDichCorrettiveAziendaByIdControllo: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) {
        DocumentoVO documentoVO = new DocumentoVO();
        documentoVO.setIdDocumento(new Long(rs.getLong("ID_DOCUMENTO")));
        documentoVO.setExtIdDocumento(new Long(rs.getLong("EXT_ID_DOCUMENTO")));
        TipoDocumentoVO tipoDocumentoVO = new TipoDocumentoVO();
        tipoDocumentoVO.setIdDocumento(new Long(rs.getLong("EXT_ID_DOCUMENTO")));
        tipoDocumentoVO.setDescrizione(rs.getString("DESC_DOC"));
        tipoDocumentoVO.setIdTipologiaDocumento(checkLongNull(rs.getString("ID_TIPOLOGIA_DOCUMENTO")));
        documentoVO.setTipoDocumentoVO(tipoDocumentoVO);
        if(Validator.isNotEmpty(rs.getString("ID_STATO_DOCUMENTO"))) {
          documentoVO.setIdStatoDocumento(new Long(rs.getLong("ID_STATO_DOCUMENTO")));
        }
        documentoVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        documentoVO.setCuaa(rs.getString("CUAA"));
        documentoVO.setDataInizioValidita(rs.getDate("DATA_INIZIO_VALIDITA"));
        documentoVO.setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
        documentoVO.setNumeroProtocollo(rs.getString("NUMERO_PROTOCOLLO"));
        documentoVO.setDataProtocollo(rs.getDate("DATA_PROTOCOLLO"));
        documentoVO.setNumeroDocumento(rs.getString("NUMERO_DOCUMENTO"));
        documentoVO.setEnteRilascioDocumento(rs.getString("ENTE_RILASCIO_DOCUMENTO"));
        if(Validator.isNotEmpty(rs.getString("ID_DOCUMENTO_PRECEDENTE"))) {
          documentoVO.setIdDocumentoPrecedente(new Long(rs.getLong("ID_DOCUMENTO_PRECEDENTE")));
        }
        documentoVO.setNote(rs.getString("NOTE"));
        documentoVO.setDataUltimoAggiornamento(rs.getDate("DATA_ULTIMO_AGGIORNAMENTO"));
        documentoVO.setUtenteUltimoAggiornamento(new Long(rs.getLong("UTENTE_ULTIMO_AGGIORNAMENTO")));
        documentoVO.setDataInserimento(rs.getDate("DATA_INSERIMENTO"));
        documentoVO.setDataVariazioneStato(rs.getDate("DATA_VARIAZIONE_STATO"));
        if(Validator.isNotEmpty(rs.getString("ID_UTENTE_AGGIORNAMENTO_SRV"))) {
          documentoVO.setIdUtenteAggiornamentoSrv(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO_SRV")));
        }
        documentoVO.setNumeroProtocolloEsterno(rs.getString("NUMERO_PROTOCOLLO_ESTERNO"));
        documentoVO.setCuaaSoccidario(rs.getString("CUAA_SOCCIDARIO"));
        documentoVO.setEsitoControllo(rs.getString("ESITO_CONTROLLO"));
        documentoVO.setDataEsecuzione(rs.getDate("DATA_ESECUZIONE"));
        documentoVO.setFlagCuaaSoccidarioValidato(rs.getString("FLAG_CUAA_SOCCIDARIO_VALIDATO"));
        elencoDocumenti.add(documentoVO);
      }

      rs.close();
      stmt.close();

    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "getListDocumentiPerDichCorrettiveAziendaByIdControllo in DocumentoDAO - SQLException: "+exc.getMessage()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) {
      SolmrLogger.error(this, "getListDocumentiPerDichCorrettiveAziendaByIdControllo in DocumentoDAO - Generic Exception: "+ex+"\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "getListDocumentiPerDichCorrettiveAziendaByIdControllo in DocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "getListDocumentiPerDichCorrettiveAziendaByIdControllo in DocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListDocumentiPerDichCorrettiveAziendaByIdControllo method in DocumentoDAO\n");
    if(elencoDocumenti.size() == 0) {
      return(DocumentoVO[])elencoDocumenti.toArray(new DocumentoVO[0]);
    }
    else {
      return(DocumentoVO[])elencoDocumenti.toArray(new DocumentoVO[elencoDocumenti.size()]);
    }
  }
  
  /**
   * Controlle se il tipo di documento è istanza di riesame
   * 
   * 
   * @param idDocumento
   * @return
   * @throws DataAccessException
   */
  public boolean isDocIstanzaRiesame(long idDocumento) throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating isDocIstanzaRiesame method in DocumentoDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    boolean isIstanzaRiesame = false;

    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in isDocIstanzaRiesame method in DocumentoDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in isDocIstanzaRiesame method in DocumentoDAO and it values: "+conn+"\n");

      String query = 
        "SELECT * " +
        "FROM   DB_DOCUMENTO_CATEGORIA DDC, " +
        "       DB_TIPO_CATEGORIA_DOCUMENTO TCD " +
        "WHERE  DDC.ID_DOCUMENTO = ? " +
        "AND    DDC.ID_CATEGORIA_DOCUMENTO = TCD.ID_CATEGORIA_DOCUMENTO " +
        "AND    TCD.IDENTIFICATIVO =  518 " +
        "AND    TCD.TIPO_IDENTIFICATIVO = 'TC' ";

      

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idDocumento);
      
      SolmrLogger.debug(this, "Executing isDocIstanzaRiesame: "+query+"\n");

      ResultSet rs = stmt.executeQuery();

      if(rs.next()) 
      {
        isIstanzaRiesame = true;
      }

      rs.close();
      stmt.close();

    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "isDocIstanzaRiesame in DocumentoDAO - SQLException: "+exc.getMessage()+"\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) 
    {
      SolmrLogger.error(this, "isDocIstanzaRiesame in DocumentoDAO - Generic Exception: "+ex+"\n");
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
        SolmrLogger.error(this, "isDocIstanzaRiesame in DocumentoDAO - SQLException while closing Statement and Connection: "+exc.getMessage()+"\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "isDocIstanzaRiesame in DocumentoDAO - Generic Exception while closing Statement and Connection: "+ex.getMessage()+"\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated isDocIstanzaRiesame method in DocumentoDAO\n");
    
    return isIstanzaRiesame;
  }
  
  
}
