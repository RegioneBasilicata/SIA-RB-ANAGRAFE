package it.csi.smranag.smrgaa.business;

import it.csi.csi.wrapper.SystemException;
import it.csi.csi.wrapper.UnrecoverableException;
import it.csi.smranag.smrgaa.dto.ws.ResponseWsBridgeVO;
import it.csi.smranag.smrgaa.util.LocalProxySelector;
import it.csi.smranags.wsbridge.dto.Response;
import it.csi.smranags.wsbridge.dto.bdn.WBAnagraficaAllevamentoVO;
import it.csi.smranags.wsbridge.dto.bdn.WBConsistenzaStatAllevamentoVO;
import it.csi.smranags.wsbridge.dto.bdn.WBUbaCensimentoOvino2012VO;
import it.csi.smrcomms.rpparser.util.Validator;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.StringcodeDescription;
import it.csi.solmr.dto.anag.sian.SianAllevamentiVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.integration.CommonDAO;
import it.csi.solmr.integration.anag.SianDAO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SianConstants;
import it.csi.solmr.util.SolmrLogger;
import it.izs.bdr.webservices.AnagraficaAllevamenti;
import it.izs.bdr.webservices.AnagraficaAllevamentiResponse;
import it.izs.bdr.webservices.ConsistenzaStatisticaMediaAllevamento;
import it.izs.bdr.webservices.ConsistenzaStatisticaMediaAllevamentoResponse;
import it.izs.bdr.webservices.ConsistenzaUBACensimOvini2012;
import it.izs.bdr.webservices.ConsistenzaUBACensimOvini2012Response;
import it.izs.bdr.webservices.SOAPAutenticazione;
import it.izs.bdr.webservices.WsAgeaAut;
import it.izs.bdr.webservices.WsAgeaAutSoap;
import it.izs.bdr.webservices.responsequery.Root;
import it.izs.bdr.webservices.responsequery.RootDati;
import it.izs.bdr.webservices.responsequery.RootDatiANAGRAFICAALLEVAMENTO;
import it.izs.bdr.webservices.responsequery.RootDatiCONSISTENZASTATALLEVAMENTO;
import it.izs.bdr.webservices.responsequery.RootDatiUBACENSIMENTOOVINO2012;
import it.izs.bdr.webservices.responsequery.RootErrorInfo;
import it.izs.bdr.webservices.responsequery.RootErrorInfoError;

import java.math.BigDecimal;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URL;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.xml.namespace.QName;

@Stateless(name = WsBridgeGaaBean.jndiName, mappedName = WsBridgeGaaBean.jndiName)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class WsBridgeGaaBean implements WsBridgeGaaLocal {

	private static final String THIS_CLASS = "BDNBridgeBean";

	// Definisco il "Qualified Name" per il webservice da chiamare
	private static final QName SERVICE_NAME = new QName(SianConstants.WS_NAMESPACE, SianConstants.WS_NAME);

	private SOAPAutenticazione sOAPAutenticazione;

	/**
   * 
   */
	private static final long serialVersionUID = -7137564296712481137L;
	public final static String jndiName = "comp/env/solmr/gaa/WsBridge";

	SessionContext sessionContext;

	private transient SianDAO sianDAO = null;
	private transient CommonDAO commonDAO = null;

	private void initializeDAO() throws EJBException {
		try {
			sianDAO = new SianDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
			commonDAO = new CommonDAO(
					SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
		} catch (ResourceAccessException ex) {
			SolmrLogger.fatal(this, ex.getMessage());
			throw new EJBException(ex);
		}
	}

	@Resource
	public void setSessionContext(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
		try {
			initializeDAO();
			// caricamento autenticazione
			this.sOAPAutenticazione = new SOAPAutenticazione();
			sOAPAutenticazione.setUsername(SianConstants.WS_USERNAME);
			sOAPAutenticazione.setPassword(SianConstants.WS_PASSWORD);
		} catch (Exception e) {
			SolmrLogger.fatal(this,
					"WsBridgeBean:setSessionContext: " + e.getMessage());
		}
	}

	private SolmrException checkServicesUnavailable(SystemException ex)
			throws SolmrException {
		SolmrLogger.fatal(this,
				"SystemException rilevata su chiamata a servizio di WsBridge");
		SolmrLogger.dumpStackTrace(this, "Dump exception", ex);

		if (SolmrConstants.NAME_NOT_FOUND_EXCEPTION.equals(ex
				.getNestedExcClassName())
				|| SolmrConstants.COMMUNICATION_EXCEPTION.equals(ex
						.getNestedExcClassName())) {
			return new SolmrException(
					AnagErrors.ERRORE_WSBRIDGE_NON_DISPONIBILE,
					SolmrException.CODICE_ERRORE_WSBRIDGE_NON_DISPONIBILE);
		} else {
			return new SolmrException(AnagErrors.ERRORE_ACCESSO_A_WSBRIDGE,
					SolmrException.CODICE_ERRORE_DI_ACCESSO_A_WSBRIDGE);
		}
	}

	public Hashtable<BigDecimal, SianAllevamentiVO> serviceAnagraficaAllevamenti(
			String cuaa, String dataRichiesta) throws SolmrException {
		SolmrLogger.debug(this,
				"[WSBridgeServiceBean::serviceAnagraficaAllevamenti] BEGIN.");

		Hashtable<BigDecimal, SianAllevamentiVO> elencoAllevamenti = null;
		try {
			Response response = getAnagraficaAllevamenti(cuaa, dataRichiesta);

			if ((response != null) && (response.getDati() != null)) {
				@SuppressWarnings("unchecked")
				Vector<WBAnagraficaAllevamentoVO> vDatiAllevamento = (Vector<WBAnagraficaAllevamentoVO>) response
						.getDati();
				elencoAllevamenti = new Hashtable<BigDecimal, SianAllevamentiVO>();
				for (int i = 0; i < vDatiAllevamento.size(); i++) {
					SianAllevamentiVO sianAllevamentiVO = convertiSianAllevamentiVO(vDatiAllevamento
							.get(i));
					// Recupero inoltre le descrizioni dei codici dal momento
					// che la
					// BDN non me li fornisce
					StringcodeDescription sianTipoSpecie = null;
					try {
						sianTipoSpecie = sianDAO
								.getSianTipoSpecieByCodiceSpecie(sianAllevamentiVO
										.getSpeCodice());
						sianAllevamentiVO.setDescrizioneSpecie(sianTipoSpecie
								.getDescription());
						sianAllevamentiVO.setIdSpecieAnimale(sianTipoSpecie
								.getSecondaryCode());
					} catch (SolmrException se) {
					} catch (DataAccessException dae) {
						throw new SolmrException(
								(String) AnagErrors
										.get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
										+ " " + dae.getMessage());
					}

					// Vado a decodificare la descrizione e identificativo in
					// anagrafe
					// del
					// orientamento produttivo restituito dal sian
					CodeDescription temp = commonDAO
							.getDescrizioneOrientamentoFromCode(
									sianAllevamentiVO.getSpeCodice(),
									sianAllevamentiVO
											.getOrientamentoProduttivo(),
									sianAllevamentiVO.getTipoProduzione());

					if (temp != null) {
						sianAllevamentiVO.setDescOrientamentoProduttivo(temp
								.getDescription());
						sianAllevamentiVO.setIdOrientamentoProduttivo(temp
								.getCode().toString());
					}

					// Vado a decodificare la descrizione e identificativo in
					// anagrafe
					// del
					// tipo produazione restituito dal sian
					temp = commonDAO
							.getDescrizioneProduzioneFromCode(sianAllevamentiVO
									.getTipoProduzione());

					if (temp != null) {
						sianAllevamentiVO.setDescTipoProduzione(temp
								.getDescription());
						sianAllevamentiVO.setIdTipoProduzione(temp.getCode()
								.toString());
					}

					// Vado a leggere le speci in anagrafe da associare a quella
					// sian
					sianAllevamentiVO.setSpecieSiap(commonDAO
							.getSpeciAnagrafeFromTeramo(sianAllevamentiVO
									.getSpeCodice()));

					// Vado a ricercare la sigla della provinica dato che Teramo
					// mi fornisce solamente la descrizione del
					// comune
					ComuneVO comuneVO = commonDAO.getComuneByParameters(
							sianAllevamentiVO.getComune(), null, null, null,
							null);
					if (comuneVO != null)
						sianAllevamentiVO.setSiglaProvincia(comuneVO
								.getSiglaProv());

					elencoAllevamenti.put(sianAllevamentiVO.getAllevId(),
							sianAllevamentiVO);
				}

			} else {
				if (response != null) {
					if ("0".equalsIgnoreCase(response.getInfo())
							&& "doc".equalsIgnoreCase(response.getTipoOutput())) {
						elencoAllevamenti = new Hashtable<BigDecimal, SianAllevamentiVO>();
						SianAllevamentiVO sianAllevamentiVO = new SianAllevamentiVO();
						sianAllevamentiVO.setCodErrore("999");
						sianAllevamentiVO
								.setDescErrore("Non sono presenti allevamenti attivi in BDN");
						elencoAllevamenti.put(new BigDecimal(-1),
								sianAllevamentiVO);
					} else {
						elencoAllevamenti = new Hashtable<BigDecimal, SianAllevamentiVO>();
						SianAllevamentiVO sianAllevamentiVO = new SianAllevamentiVO();
						sianAllevamentiVO.setCodErrore(response
								.getCodiceErrore());
						sianAllevamentiVO.setDescErrore(response
								.getDescrizioneErrore());
						elencoAllevamenti.put(new BigDecimal(-1),
								sianAllevamentiVO);
					}
				} else {
					throw new SolmrException(
							AnagErrors.ERRORE_ACCESSO_A_WSBRIDGE,
							SolmrException.CODICE_ERRORE_DI_ACCESSO_A_WSBRIDGE);
				}
			}

			return elencoAllevamenti;
		} catch (SystemException ex) {
			throw checkServicesUnavailable(ex);
		} catch (UnrecoverableException ex) {
			SolmrLogger.dumpStackTrace(this,
					AnagErrors.ERRORE_WSBRIDGE_EXCEPTION, ex);
			throw new SolmrException(ex.getMessage());
		} catch (EJBException ex) {
			SolmrLogger.dumpStackTrace(this,
					AnagErrors.ERRORE_WSBRIDGE_EXCEPTION, ex);
			throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_WSBRIDGE,
					SolmrException.CODICE_ERRORE_DI_ACCESSO_A_WSBRIDGE);
		} catch (Exception ex) {
			SolmrLogger.dumpStackTrace(this,
					AnagErrors.ERRORE_WSBRIDGE_EXCEPTION, ex);
			throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_WSBRIDGE,
					SolmrException.CODICE_ERRORE_DI_ACCESSO_A_WSBRIDGE);
		} finally {
			SolmrLogger.debug(this,
					"[WSBridgeServiceBean::serviceAnagraficaAllevamenti] END.");
		}
	}

	private SianAllevamentiVO convertiSianAllevamentiVO(
			WBAnagraficaAllevamentoVO allevamento) {
		if (allevamento == null)
			return null;
		SianAllevamentiVO risposta = new SianAllevamentiVO();
		risposta.setAllevId(allevamento.getIdAllevamento());
		risposta.setAutorizzazioneLatte(allevamento.getAutorizzazioneLatte());
		risposta.setAziendaCodice(allevamento.getCodiceAzienda());
		risposta.setCap(allevamento.getCap());
		risposta.setCapiTotali(allevamento.getCapiTotali());
		risposta.setCodFiscaleDeten(allevamento.getCodiceFiscaleDetentore());
		risposta.setCodFiscaleProp(allevamento.getCodiceFiscaleProprietario());
		risposta.setComune(allevamento.getComune());
		risposta.setDataCalcoloCapi(allevamento.getDataCalcoloCapi());
		risposta.setDenomDetentore(allevamento.getDenominazioneDetentore());
		risposta.setDenomProprietario(allevamento
				.getDenominazioneProprietario());
		risposta.setDenominazione(allevamento.getDenominazione());
		risposta.setDtFineAttivita(allevamento.getDataFineAttivita());
		risposta.setDtFineDetentore(allevamento.getDataFineDetentore());
		risposta.setDtInizioAttivita(allevamento.getDataInizioAttivita());
		risposta.setDtInizioDetentore(allevamento.getDataInizioDetentore());
		risposta.setFoglioCatastale(allevamento.getFoglioCatastale());
		risposta.setIndirizzo(allevamento.getIndirizzo());
		risposta.setLatitudine(allevamento.getLatitudine());
		risposta.setLocalita(allevamento.getLocalita());
		risposta.setLongitudine(allevamento.getLongitudine());
		risposta.setParticella(allevamento.getParticella());
		risposta.setSezione(allevamento.getSezione());
		risposta.setSoccida(allevamento.getSoccida());
		risposta.setSpeCodice(allevamento.getCodiceSpecie());
		risposta.setSubalterno(allevamento.getSubAlterno());
		risposta.setTipoProduzione(allevamento.getTipoProduzione());
		risposta.setOrientamentoProduttivo(allevamento
				.getOrientamentoProduttivo());
		return risposta;
	}

	public ResponseWsBridgeVO serviceConsistenzaStatisticaMediaAllevamento(
			String cuaa) throws SolmrException {
		SolmrLogger
				.debug(this,
						"[WSBridgeGaaBean::serviceConsistenzaStatisticaMediaAllevamento] BEGIN.");

		ResponseWsBridgeVO responseWs = new ResponseWsBridgeVO();
		try {
			// devo chiamare il servizio 3 volte uno per ogni anno precedente
			for (int i = 1; i < 4; i++) {
				int anno = DateUtils.getCurrentYear() - i;
				String dataInizio = "01/01/" + anno;
				String dataFine = "31/12/" + anno;
				Response response = getConsistenzaStatisticaMediaAllevamento(
						cuaa, dataInizio, dataFine, SolmrConstants.DETENTORE);

				if ((response != null) && (response.getDati() != null)) {
					@SuppressWarnings("unchecked")
					Vector<WBConsistenzaStatAllevamentoVO> vDatiConsistenza = (Vector<WBConsistenzaStatAllevamentoVO>) response
							.getDati();
					for (int j = 0; j < vDatiConsistenza.size(); j++) {
						if (responseWs.getvDati() == null) {
							responseWs.setvDati(new Vector<Object>());
						}

						responseWs.getvDati().add(vDatiConsistenza.get(j));
					}
				} else {
					if (responseWs.getvErrori() == null) {
						responseWs.setvErrori(new Vector<CodeDescription>());
					}
					CodeDescription cd = new CodeDescription();
					cd.setCode(new Integer(anno));
					String codiceErrore = "000";
					if (Validator.isNotEmpty(response.getCodiceErrore()))
						codiceErrore = response.getCodiceErrore();
					cd.setCodeFlag(codiceErrore);
					String descrizioneErrore = "il servizio BDN non ritorna nessun dato";
					if (Validator.isNotEmpty(response.getDescrizioneErrore()))
						descrizioneErrore = response.getDescrizioneErrore();
					cd.setDescription(descrizioneErrore);

					responseWs.getvErrori().add(cd);
				}

			}

			return responseWs;
		} catch (SystemException ex) {
			throw checkServicesUnavailable(ex);
		} catch (UnrecoverableException ex) {
			SolmrLogger.dumpStackTrace(this,
					AnagErrors.ERRORE_WSBRIDGE_EXCEPTION, ex);
			throw new SolmrException(ex.getMessage());
		} catch (EJBException ex) {
			SolmrLogger.dumpStackTrace(this,
					AnagErrors.ERRORE_WSBRIDGE_EXCEPTION, ex);
			throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_WSBRIDGE,
					SolmrException.CODICE_ERRORE_DI_ACCESSO_A_WSBRIDGE);
		} catch (Exception ex) {
			SolmrLogger.dumpStackTrace(this,
					AnagErrors.ERRORE_WSBRIDGE_EXCEPTION, ex);
			throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_WSBRIDGE,
					SolmrException.CODICE_ERRORE_DI_ACCESSO_A_WSBRIDGE);
		} finally {
			SolmrLogger
					.debug(this,
							"[WSBridgeGaaBean::serviceConsistenzaStatisticaMediaAllevamento] END.");
		}
	}

	public ResponseWsBridgeVO serviceConsistenzaUbaCensimOvini(String cuaa)
			throws SolmrException {
		SolmrLogger.debug(this,
				"[WSBridgeGaaBean::serviceConsistenzaUbaCensimOvini] BEGIN.");

		ResponseWsBridgeVO responseWs = new ResponseWsBridgeVO();
		try {
			// devo chiamare il servizio 3 volte uno per ogni anno precedente
			for (int i = 1; i < 4; i++) {
				int anno = DateUtils.getCurrentYear() - i;
				String dataInizio = "01/01/" + anno;
				String dataFine = "31/12/" + anno;
				Response response = getConsistenzaUbaCensimOvini2012(cuaa,
						dataInizio, dataFine, SolmrConstants.DETENTORE);

				if ((response != null) && (response.getDati() != null)) {
					@SuppressWarnings("unchecked")
					Vector<WBUbaCensimentoOvino2012VO> vDatiConsistenza = (Vector<WBUbaCensimentoOvino2012VO>) response
							.getDati();
					for (int j = 0; j < vDatiConsistenza.size(); j++) {
						if (responseWs.getvDati() == null) {
							responseWs.setvDati(new Vector<Object>());
						}

						responseWs.getvDati().add(vDatiConsistenza.get(j));
					}
				} else {
					if (responseWs.getvErrori() == null) {
						responseWs.setvErrori(new Vector<CodeDescription>());
					}
					CodeDescription cd = new CodeDescription();
					cd.setCode(new Integer(anno));
					String codiceErrore = "000";
					if (Validator.isNotEmpty(response.getCodiceErrore()))
						codiceErrore = response.getCodiceErrore();
					cd.setCodeFlag(codiceErrore);
					String descrizioneErrore = "il servizio BDN non ritorna nessun dato";
					if (Validator.isNotEmpty(response.getDescrizioneErrore()))
						descrizioneErrore = response.getDescrizioneErrore();
					cd.setDescription(descrizioneErrore);

					responseWs.getvErrori().add(cd);
				}

			}

			return responseWs;
		} catch (SystemException ex) {
			throw checkServicesUnavailable(ex);
		} catch (UnrecoverableException ex) {
			SolmrLogger.dumpStackTrace(this,
					AnagErrors.ERRORE_WSBRIDGE_EXCEPTION, ex);
			throw new SolmrException(ex.getMessage());
		} catch (EJBException ex) {
			SolmrLogger.dumpStackTrace(this,
					AnagErrors.ERRORE_WSBRIDGE_EXCEPTION, ex);
			throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_WSBRIDGE,
					SolmrException.CODICE_ERRORE_DI_ACCESSO_A_WSBRIDGE);
		} catch (Exception ex) {
			SolmrLogger.dumpStackTrace(this,
					AnagErrors.ERRORE_WSBRIDGE_EXCEPTION, ex);
			throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_WSBRIDGE,
					SolmrException.CODICE_ERRORE_DI_ACCESSO_A_WSBRIDGE);
		} finally {
			SolmrLogger.debug(this,
					"[WSBridgeGaaBean::serviceConsistenzaUbaCensimOvini] END.");
		}
	}

	public void serviceWsBridgeAggiornaDatiBDN(String cuaa)
			throws SolmrException {
		SianAllevamentiVO[] elencoSianAllevamentiVO = null;
		Response response = null;
		SianAllevamentiVO sianAllevamentiErrVO = null;

		try {
			response = getAnagraficaAllevamenti(cuaa,DateUtils.getCurrentDateString());

			if ((response != null) && (response.getDati() != null)) {

				Vector<WBAnagraficaAllevamentoVO> vDatiAllevamento = (Vector<WBAnagraficaAllevamentoVO>) response
						.getDati();
				elencoSianAllevamentiVO = new SianAllevamentiVO[vDatiAllevamento
						.size()];
				// elencoAllevamenti = new
				// Hashtable<BigDecimal,SianAllevamentiVO>();
				for (int i = 0; i < vDatiAllevamento.size(); i++) {
					SianAllevamentiVO sianAllevamentiVO = convertiSianAllevamentiVO(vDatiAllevamento
							.get(i));
					elencoSianAllevamentiVO[i] = sianAllevamentiVO;
				}
			} else {
				if (response != null) {
					if ("0".equalsIgnoreCase(response.getInfo())
							&& "doc".equalsIgnoreCase(response.getTipoOutput())) {
						sianAllevamentiErrVO = new SianAllevamentiVO();
						sianAllevamentiErrVO.setCodErrore("999");
						sianAllevamentiErrVO
								.setDescErrore("Non sono presenti allevamenti attivi in BDN");
					} else {
						sianAllevamentiErrVO = new SianAllevamentiVO();
						sianAllevamentiErrVO.setCodErrore(response
								.getCodiceErrore());
						sianAllevamentiErrVO.setDescErrore(response
								.getDescrizioneErrore());
					}
				} else {
					throw new SolmrException(
							AnagErrors.ERRORE_ACCESSO_A_WSBRIDGE,
							SolmrException.CODICE_ERRORE_DI_ACCESSO_A_WSBRIDGE);
				}
			}
		} catch (SystemException ex) {
			// throw checkServicesUnavailable(ex);
			sianAllevamentiErrVO = new SianAllevamentiVO();
			sianAllevamentiErrVO.setCodErrore("999");
			sianAllevamentiErrVO
					.setDescErrore("Servizio teramo non disponibile");
		} catch (UnrecoverableException ex) {
			SolmrLogger.dumpStackTrace(this,
					AnagErrors.ERRORE_WSBRIDGE_EXCEPTION, ex);
			// throw new SolmrException(ex.getMessage());
			sianAllevamentiErrVO = new SianAllevamentiVO();
			sianAllevamentiErrVO.setCodErrore("999");
			sianAllevamentiErrVO
					.setDescErrore("Servizio teramo non disponibile");
		} catch (EJBException ex) {
			SolmrLogger.dumpStackTrace(this,
					AnagErrors.ERRORE_WSBRIDGE_EXCEPTION, ex);
			// throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_WSBRIDGE,
			// SolmrException.CODICE_ERRORE_DI_ACCESSO_A_WSBRIDGE);
			sianAllevamentiErrVO = new SianAllevamentiVO();
			sianAllevamentiErrVO.setCodErrore("999");
			sianAllevamentiErrVO
					.setDescErrore("Servizio teramo non disponibile");
		} catch (Exception ex) {
			SolmrLogger.dumpStackTrace(this,
					AnagErrors.ERRORE_WSBRIDGE_EXCEPTION, ex);
			// throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_WSBRIDGE,
			// SolmrException.CODICE_ERRORE_DI_ACCESSO_A_WSBRIDGE);
			sianAllevamentiErrVO = new SianAllevamentiVO();
			sianAllevamentiErrVO.setCodErrore("999");
			sianAllevamentiErrVO
					.setDescErrore("Servizio teramo non disponibile");
		}

		try {

			sianDAO.deleteAllevamentiSian(cuaa);

			if (sianAllevamentiErrVO == null) {
				if (elencoSianAllevamentiVO != null
						&& elencoSianAllevamentiVO.length > 0) {
					for (int i = 0; i < elencoSianAllevamentiVO.length; i++) {
						sianDAO.insertAllevamentiSian(
								elencoSianAllevamentiVO[i], cuaa, null,
								SolmrConstants.SIAN_FLAG_PRESENTE_AT, true);
					}
				} else {

					sianDAO.insertAllevamentiSian(null, cuaa, null,
							SolmrConstants.SIAN_FLAG_PRESENTE_AT_ERRORE, false);
				}
			} else {
				sianDAO.insertAllevamentiSian(null, cuaa,
						sianAllevamentiErrVO.getCodErrore() + " - "
								+ sianAllevamentiErrVO.getDescErrore(),
						SolmrConstants.SIAN_FLAG_PRESENTE_AT_ERRORE, false);
			}
		} catch (DataAccessException dae) {
			throw new SolmrException(dae.getMessage());
		}
	}

	/**********************************************************
	 **********************************************************
	 **********************************************************
	 I seguenti metodi erano su ws-bridge, li abbiamo portati di quà per la
	 * migrazioen a jboss
	 **********************************************************
	 **********************************************************
	 **********************************************************
	 * */

	private Response getAnagraficaAllevamenti(String CUAA, String dataRichiesta)
			throws Exception {
		final String THIS_METHOD = "getAnagraficaAllevamenti";
		final String THIS_LOG = "[" + THIS_CLASS + "::" + THIS_METHOD + "] ";
		SolmrLogger.debug(this, THIS_LOG + "BEGIN.");
		SolmrLogger.debug(this, THIS_LOG + "- CUAA: " + CUAA);
		SolmrLogger.debug(this, THIS_LOG + "- dataRichiesta: " + dataRichiesta);

		WsAgeaAutSoap wsAgeaAutSoap = getBinding();

		// Procedo con l'invocazione dell'operazione anagraficaAllevamenti (cioè
		// Anagrafica_Allevamenti)
		AnagraficaAllevamenti aaInput = new AnagraficaAllevamenti();
		aaInput.setCUUA(CUAA);
		aaInput.setPDataRichiesta(dataRichiesta);

		SolmrLogger.debug(this, THIS_LOG + "- Procedo con l'invocazione dell'operazione anagraficaAllevamenti");
		AnagraficaAllevamentiResponse aaResponse = wsAgeaAutSoap
				.anagraficaAllevamenti(aaInput, sOAPAutenticazione);

		// oggetto da restituire
		Response response = new Response();

		if (aaResponse != null) {
			SolmrLogger.debug(this, THIS_LOG + "- anagraficaAllevamenti ha risposto");
			Root root = aaResponse.getAnagraficaAllevamentiResult();
			// copy delle informazioni
			copyRootInfo(response, root);

			// copy dei dati
			RootDati rootDati = root.getDati();
			copyDataAnagraficaAllevamenti(response, rootDati);
		}

		SolmrLogger.debug(this, THIS_LOG + "END.");

		// Restituisco i dati già travasati nell'oggetto WSBridge
		return response;
	}

	private Response getConsistenzaStatisticaMediaAllevamento(String cuaa,
			String dataInizioPeriodo, String dataFinePeriodo,
			String tipoResponsabilita) throws Exception {
		final String THIS_METHOD = "getConsistenzaStatisticaMediaAllevamento";
		final String THIS_LOG = "[" + THIS_CLASS + "::" + THIS_METHOD + "] ";
		SolmrLogger.debug(this, THIS_LOG + "BEGIN.");
		SolmrLogger.debug(this, THIS_LOG + "- cuaa: " + cuaa);
		SolmrLogger.debug(this, THIS_LOG + "- dataInizioPeriodo: "
				+ dataInizioPeriodo);
		SolmrLogger.debug(this, THIS_LOG + "- dataFinePeriodo: "
				+ dataFinePeriodo);
		SolmrLogger.debug(this, THIS_LOG + "- tipoResponsabilita: "
				+ tipoResponsabilita);

		WsAgeaAutSoap wsAgeaAutSoap = getBinding();

		// Procedo con l'invocazione dell'operazione anagraficaAllevamenti (cioè
		// Anagrafica_Allevamenti)
		ConsistenzaStatisticaMediaAllevamento csmaInput = new ConsistenzaStatisticaMediaAllevamento();
		csmaInput.setPCuaa(cuaa);
		csmaInput.setDataInizioPeriodo(dataInizioPeriodo);
		csmaInput.setDataFinePeriodo(dataFinePeriodo);
		csmaInput.setPTipoResponsabilita(tipoResponsabilita);

		ConsistenzaStatisticaMediaAllevamentoResponse csmaResponse = wsAgeaAutSoap
				.consistenzaStatisticaMediaAllevamento(csmaInput,
						sOAPAutenticazione);

		// oggetto da restituire
		Response response = new Response();

		if (csmaResponse != null) {
			Root root = csmaResponse
					.getConsistenzaStatisticaMediaAllevamentoResult();
			// copy delle informazioni
			copyRootInfo(response, root);

			// copy dei dati
			RootDati rootDati = root.getDati();
			copyDataConsistenzaStatAllevamenti(response, rootDati);
		}

		SolmrLogger.debug(this, THIS_LOG + "END.");

		// Restituisco i dati già travasati nell'oggetto WSBridge
		return response;
	}

	private Response getConsistenzaUbaCensimOvini2012(String cuaa,
			String dataInizioPeriodo, String dataFinePeriodo,
			String tipoResponsabilita) throws Exception {
		final String THIS_METHOD = "getConsistenzaUbaCensimOvini2012";
		final String THIS_LOG = "[" + THIS_CLASS + "::" + THIS_METHOD + "] ";
		SolmrLogger.debug(this, THIS_LOG + "BEGIN.");
		SolmrLogger.debug(this, THIS_LOG + "- cuaa: " + cuaa);
		SolmrLogger.debug(this, THIS_LOG + "- dataInizioPeriodo: "
				+ dataInizioPeriodo);
		SolmrLogger.debug(this, THIS_LOG + "- dataFinePeriodo: "
				+ dataFinePeriodo);
		SolmrLogger.debug(this, THIS_LOG + "- tipoResponsabilita: "
				+ tipoResponsabilita);

		WsAgeaAutSoap wsAgeaAutSoap = getBinding();

		// Procedo con l'invocazione dell'operazione anagraficaAllevamenti (cioè
		// Anagrafica_Allevamenti)
		ConsistenzaUBACensimOvini2012 cucoInput = new ConsistenzaUBACensimOvini2012();
		cucoInput.setPCuaa(cuaa);
		cucoInput.setDataInizioPeriodo(dataInizioPeriodo);
		cucoInput.setDataFinePeriodo(dataFinePeriodo);
		cucoInput.setPTipoResponsabilita(tipoResponsabilita);

		ConsistenzaUBACensimOvini2012Response cucoResponse = wsAgeaAutSoap
				.consistenzaUBACensimOvini2012(cucoInput, sOAPAutenticazione);

		// oggetto da restituire
		Response response = new Response();

		if (cucoResponse != null) {
			Root root = cucoResponse.getConsistenzaUBACensimOvini2012Result();
			// copy delle informazioni
			copyRootInfo(response, root);

			// copy dei dati
			RootDati rootDati = root.getDati();
			copyDataUbaCensimentoOvino2012(response, rootDati);
		}

		SolmrLogger.debug(this, THIS_LOG + "END.");

		// Restituisco i dati già travasati nell'oggetto WSBridge
		return response;
	}

	private WsAgeaAutSoap getBinding() throws Exception {
		final String THIS_METHOD = "getBinding";
		final String THIS_LOG = "[" + THIS_CLASS + "::" + THIS_METHOD + "] ";
		SolmrLogger.debug(this, THIS_LOG + "BEGIN.");

		// Definisco l'URI del wsdl e ne ricavo l'URL
		// URI wsdlURI = new
		// URI("http://premi.izs.it/wsBDNAgea/wsAgeaAut.asmx?wsdl");
		URI wsdlURI = new URI(SianConstants.WS_WSDLURI);
		URL wsdlURL = wsdlURI.toURL();

		// Imposto il proxy mediante la classe LocalProxySelector (generico; da
		// approfondire, ma funziona)
		if (SianConstants.PROXY_ACTIVE) {
			ProxySelector selector = new LocalProxySelector();
			ProxySelector.setDefault(selector);
			selector.connectFailed(wsdlURI, null, null);
			selector.connectFailed(new URI(SianConstants.WS_URI), null, null);
		}

		// Definisco il riferimento al web service
		WsAgeaAut wsAgeaAut = new WsAgeaAut(wsdlURL, SERVICE_NAME);

		// Recupero lo stub
		WsAgeaAutSoap wsAgeaAutSoap = wsAgeaAut.getWsAgeaAutSoap();

		SolmrLogger.debug(this, THIS_LOG + "END.");

		return wsAgeaAutSoap;
	}

	// Mappo nell'oggetto WSBridge le 'informazioni' contenute nell'oggetto BDN
	private static void copyRootInfo(Response response, Root root) {
		if (root != null) {
			response.setTipoOutput(root.getTipoOutput());
			RootErrorInfo rootErrorInfo = root.getErrorInfo();
			if (rootErrorInfo != null) {
				response.setInfo(rootErrorInfo.getInfo());
				response.setWarning(rootErrorInfo.getWarning());
				RootErrorInfoError rootErrorInfoError = rootErrorInfo
						.getError();
				if (rootErrorInfoError != null) {
					response.setCodiceErrore(rootErrorInfoError.getId());
					response.setDescrizioneErrore(rootErrorInfoError.getDes());
				}
			}
		}
	}

	// Mappo nell'oggetto WSBridge i 'dati' contenuti nell'oggetto BDN
	private static void copyDataAnagraficaAllevamenti(Response response,
			RootDati rootDati) {
		//
		if (rootDati != null
				&& rootDati.getDsANAGRAFICAALLEVAMENTI() != null
				&& rootDati.getDsANAGRAFICAALLEVAMENTI()
						.getANAGRAFICAALLEVAMENTO() != null) {
			List<RootDatiANAGRAFICAALLEVAMENTO> wsaaList = rootDati
					.getDsANAGRAFICAALLEVAMENTI().getANAGRAFICAALLEVAMENTO();
			Vector dati = new Vector();
			for (RootDatiANAGRAFICAALLEVAMENTO rootDatiANAGRAFICAALLEVAMENTO : wsaaList) {
				WBAnagraficaAllevamentoVO wbaa = new WBAnagraficaAllevamentoVO();
				remapAnagraficaAllevamento(rootDatiANAGRAFICAALLEVAMENTO, wbaa);
				dati.add(wbaa);
			}
			response.setDati(dati);
		}
	}

	private static void remapAnagraficaAllevamento(
			RootDatiANAGRAFICAALLEVAMENTO wsaa, WBAnagraficaAllevamentoVO wbaa) {
		wbaa.setIdAllevamento(wsaa.getALLEVID());
		wbaa.setAutorizzazioneLatte(wsaa.getAUTORIZZAZIONELATTE());
		wbaa.setCodiceAzienda(wsaa.getAZIENDACODICE());
		wbaa.setCap(wsaa.getCAP());
		wbaa.setCapiTotali(wsaa.getCAPITOTALI());
		wbaa.setCodiceFiscaleDetentore(wsaa.getCODFISCALEDETEN());
		wbaa.setCodiceFiscaleProprietario(wsaa.getCODFISCALEPROP());
		wbaa.setCodiceSpecie(wsaa.getSPECODICE());
		wbaa.setComune(wsaa.getCOMUNE());
		wbaa.setDataCalcoloCapi(wsaa.getDATACALCOLOCAPI());
		wbaa.setDataFineAttivita(wsaa.getDTFINEATTIVITA());
		wbaa.setDataFineDetentore(wsaa.getDTFINEDETENTORE());
		wbaa.setDataInizioAttivita(wsaa.getDTINIZIOATTIVITA());
		wbaa.setDataInizioDetentore(wsaa.getDTINIZIODETENTORE());
		wbaa.setDenominazione(wsaa.getDENOMINAZIONE());
		wbaa.setDenominazioneDetentore(wsaa.getDENOMDETENTORE());
		wbaa.setDenominazioneProprietario(wsaa.getDENOMPROPRIETARIO());
		wbaa.setFoglioCatastale(wsaa.getFOGLIOCATASTALE());
		wbaa.setIndirizzo(wsaa.getINDIRIZZO());
		wbaa.setLatitudine(wsaa.getLATITUDINE());
		wbaa.setLocalita(wsaa.getLOCALITA());
		wbaa.setLongitudine(wsaa.getLONGITUDINE());
		wbaa.setOrientamentoProduttivo(wsaa.getORIENTAMENTOPRODUTTIVO());
		wbaa.setParticella(wsaa.getPARTICELLA());
		wbaa.setSezione(wsaa.getSEZIONE());
		wbaa.setSoccida(wsaa.getSOCCIDA());
		wbaa.setSubAlterno(wsaa.getSUBALTERNO());
		wbaa.setTipoProduzione(wsaa.getTIPOPRODUZIONE());
		wbaa.setTipoAllevamentoCodice(wsaa.getTIPOALLEVCOD());
		wbaa.setTipoAllevamentoDescrizione(wsaa.getTIPOALLEVDESCR());
	}

	private static void copyDataConsistenzaStatAllevamenti(Response response,
			RootDati rootDati) {
		//
		if (rootDati != null
				&& rootDati.getDsCONSISTENZASTATALLEVAMENTI() != null
				&& rootDati.getDsCONSISTENZASTATALLEVAMENTI()
						.getCONSISTENZASTATALLEVAMENTO() != null) {
			List<RootDatiCONSISTENZASTATALLEVAMENTO> wsaaList = rootDati
					.getDsCONSISTENZASTATALLEVAMENTI()
					.getCONSISTENZASTATALLEVAMENTO();
			Vector dati = new Vector();
			for (RootDatiCONSISTENZASTATALLEVAMENTO rootDatiCONSISTENZASTATALLEVAMENTO : wsaaList) {
				WBConsistenzaStatAllevamentoVO wbaa = new WBConsistenzaStatAllevamentoVO();
				remapConsistenzaStatAllevamento(
						rootDatiCONSISTENZASTATALLEVAMENTO, wbaa);
				dati.add(wbaa);
			}
			response.setDati(dati);
		}
	}

	public static void remapConsistenzaStatAllevamento(
			RootDatiCONSISTENZASTATALLEVAMENTO wsaa,
			WBConsistenzaStatAllevamentoVO wbaa) {
		wbaa.setIdAllevamento(wsaa.getPALLEVID());
		wbaa.setCodiceAzienda(wsaa.getAZIENDACODICE());
		wbaa.setCodiceSpecie(wsaa.getSPECODICE());
		if (wsaa.getALLEVDTINIZIOATTIVITA() != null)
			wbaa.setDataInizioAttivitaAllevamento(wsaa
					.getALLEVDTINIZIOATTIVITA().toGregorianCalendar().getTime());
		if (wsaa.getALLEVDTFINEATTIVITA() != null)
			wbaa.setDataFineAttivitaAllevamento(wsaa.getALLEVDTFINEATTIVITA()
					.toGregorianCalendar().getTime());
		wbaa.setCodFiscProprietario(wsaa.getCODFISCALEPROP());
		wbaa.setCodFiscDetentore(wsaa.getCODFISCALEDETE());
		wbaa.setMediaMaschiBov0_12Mesi(wsaa.getMEDIAMASCHIBOV012MESI());
		wbaa.setMediaMaschiBuf0_12Mesi(wsaa.getMEDIAMASCHIBUF012MESI());
		wbaa.setMediaFemmineBov0_12Mesi(wsaa.getMEDIAFEMMINEBOV012MESI());
		wbaa.setMediaFemmineBuf0_12Mesi(wsaa.getMEDIAFEMMINEBUF012MESI());
		wbaa.setMediaMaschiBov12_24Mesi(wsaa.getMEDIAMASCHIBOV1224MESI());
		wbaa.setMediaMaschiBuf12_24Mesi(wsaa.getMEDIAMASCHIBUF1224MESI());
		wbaa.setMediaFemmineBov12_24Mesi(wsaa.getMEDIAFEMMINEBOV1224MESI());
		wbaa.setMediaFemmineBuf12_24Mesi(wsaa.getMEDIAFEMMINEBUF1224MESI());
		wbaa.setMediaCapiTotali(wsaa.getMEDIACAPITOTALI());
		if (wsaa.getMAXDATACALCOLOCAPI() != null)
			wbaa.setDataMaxCalcoloCapi(wsaa.getMAXDATACALCOLOCAPI()
					.toGregorianCalendar().getTime());
		if (wsaa.getMINDATACALCOLOCAPI() != null)
			wbaa.setDataMinCalcoloCapi(wsaa.getMINDATACALCOLOCAPI()
					.toGregorianCalendar().getTime());
		wbaa.setNumElaborazioni(wsaa.getNUMELABORAZIONI());
		if (wsaa.getDTINIZIOPERIODO() != null)
			wbaa.setDataInizioPeriodo(wsaa.getDTINIZIOPERIODO()
					.toGregorianCalendar().getTime());
		if (wsaa.getDTFINEPERIODO() != null)
			wbaa.setDataFinePeriodo(wsaa.getDTFINEPERIODO()
					.toGregorianCalendar().getTime());

	}

	public static void copyDataUbaCensimentoOvino2012(Response response,
			RootDati rootDati) {
		//
		if (rootDati != null
				&& rootDati.getDsUBACENSIMENTIOVINI2012() != null
				&& rootDati.getDsUBACENSIMENTIOVINI2012()
						.getUBACENSIMENTOOVINO2012() != null) {
			List<RootDatiUBACENSIMENTOOVINO2012> wsaaList = rootDati
					.getDsUBACENSIMENTIOVINI2012().getUBACENSIMENTOOVINO2012();
			Vector dati = new Vector();
			for (RootDatiUBACENSIMENTOOVINO2012 rootDatiUBACENSIMENTOOVINO2012 : wsaaList) {
				WBUbaCensimentoOvino2012VO wbaa = new WBUbaCensimentoOvino2012VO();
				remapUbaCensimentoOvino2012(rootDatiUBACENSIMENTOOVINO2012,
						wbaa);
				dati.add(wbaa);
			}
			response.setDati(dati);
		}
	}

	public static void remapUbaCensimentoOvino2012(
			RootDatiUBACENSIMENTOOVINO2012 wsaa, WBUbaCensimentoOvino2012VO wbaa) {
		wbaa.setIdCensimento(wsaa.getCENSIMENTOID());
		wbaa.setIdAllevamento(wsaa.getPALLEVID());
		wbaa.setCodiceAzienda(wsaa.getAZIENDACODICE());
		wbaa.setCodFiscProprietario(wsaa.getCODFISCALEPROP());
		wbaa.setCodFiscDetentore(wsaa.getCODFISCALEDETE());
		wbaa.setCodiceSpecie(wsaa.getSPECIECODICE());
		wbaa.setDescrizioneSpecie(wsaa.getSPECIEDESCRIZIONE());
		if (wsaa.getDATAINIZIOPERIODO() != null)
			wbaa.setDataInizioPeriodo(wsaa.getDATAINIZIOPERIODO()
					.toGregorianCalendar().getTime());
		if (wsaa.getDATAFINEPERIODO() != null)
			wbaa.setDataFinePeriodo(wsaa.getDATAFINEPERIODO()
					.toGregorianCalendar().getTime());
		wbaa.setOviniMaschiAdulti(wsaa.getOMASCHIADULTI());
		wbaa.setOviniMaschiAdultiLib(wsaa.getOMASCHIADULTILIB());
		wbaa.setOviniFemmineAdulte(wsaa.getOFEMMINEADULTE());
		wbaa.setOviniFemmineAdulteLib(wsaa.getOFEMMINEADULTELIB());
		wbaa.setOviniMaschiRimonta(wsaa.getOMASCHIRIMONTA());
		wbaa.setOviniMaschiRimontaLib(wsaa.getOMASCHIRIMONTALIB());
		wbaa.setOviniFemmineRimonta(wsaa.getOFEMMINERIMONTA());
		wbaa.setOviniFemmineRimontaLib(wsaa.getOFEMMINERIMONTALIB());
		wbaa.setOviniCapiTotali(wsaa.getOCAPITOT());
		wbaa.setAgnelliMacellatiTotali(wsaa.getOAGNELLIMACTOT());
		wbaa.setCapriniMaschiAdulti(wsaa.getCMASCHIADULTI());
		wbaa.setCapriniMaschiAdultiLib(wsaa.getCMASCHIADULTILIB());
		wbaa.setCapriniFemmineAdulte(wsaa.getCFEMMINEADULTE());
		wbaa.setCapriniFemmineAdulteLib(wsaa.getCFEMMINEADULTELIB());
		wbaa.setCapriniMaschiRimonta(wsaa.getCMASCHIRIMONTA());
		wbaa.setCapriniMaschiRimontaLib(wsaa.getCMASCHIRIMONTALIB());
		wbaa.setCapriniFemmineRimonta(wsaa.getCFEMMINERIMONTA());
		wbaa.setCapriniFemmineRimontaLib(wsaa.getCFEMMINERIMONTALIB());
		wbaa.setCapriniCapiTotali(wsaa.getCCAPITOT());
		wbaa.setCaprettiMacellatiTotali(wsaa.getCCAPRETTIMACTOT());
		if (wsaa.getDATACENSIMENTO() != null)
			wbaa.setDataCensimento(wsaa.getDATACENSIMENTO()
					.toGregorianCalendar().getTime());
		if (wsaa.getDTCOMAUTORITA() != null)
			wbaa.setDataComunicazioneAutorita(wsaa.getDTCOMAUTORITA()
					.toGregorianCalendar().getTime());

	}
}
