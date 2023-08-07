package it.csi.smranag.smrgaa.business;

/**
 * <p>Title: Applicativo Gnps</p>
 * <p>Description: Inclusione servizi Papuaserv per la messaggistica</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: TOBECONFIG</p>
 * @author Gallarato Federico
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.csi.csi.porte.InfoPortaDelegata;
import it.csi.csi.porte.proxy.PDProxy;
import it.csi.csi.util.xml.PDConfigReader;
import it.csi.csi.wrapper.SystemException;
import it.csi.csi.wrapper.UnrecoverableException;
import it.csi.papua.papuaserv.dto.UtenteAggiornamento;
import it.csi.papua.papuaserv.dto.messaggistica.*;
import it.csi.papua.papuaserv.exception.messaggistica.LogoutException;
import it.csi.papua.papuaserv.interfacecsi.IMessaggisticaCSIInterface;
import it.csi.smranag.smrgaa.ws.papuaserv.messaggistica.IMessaggisticaWS;
import it.csi.smranag.smrgaa.ws.papuaserv.messaggistica.Messaggistica;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.exception.profile.AgriException;
import it.csi.solmr.exception.services.InvalidParameterException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;

import javax.annotation.Resource;
import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.xml.ws.BindingProvider;

@Stateless(name = MessaggisticaGaaBean.jndiName, mappedName = MessaggisticaGaaBean.jndiName)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class MessaggisticaGaaBean implements MessaggisticaGaaLocal {
	public static IMessaggisticaWS portStub = null;

	static {

		ResourceBundle res = ResourceBundle.getBundle("config");
		String endPointUrl = res.getString("messaggistica_endpoint_url");

		SolmrLogger.debug("MessaggisticaGaaBean",
				"-- endPointUrl messaggistica =" + endPointUrl);

		Messaggistica ss = new Messaggistica();
		portStub = ss.getMessaggisticaPort();

		BindingProvider bp = (BindingProvider) portStub;

		Map<String, Object> context = bp.getRequestContext();
		context.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPointUrl);

	}

	/**
   * 
   */
	private static final long serialVersionUID = -138900328814782715L;
	public final static String jndiName = "comp/env/solmr/gaa/Messaggistica";

	SessionContext sessionContext;

	@Resource
	public void setSessionContext(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
	}

	private SolmrException checkServicesUnavailable(SystemException ex)
			throws SolmrException {
		if (SolmrConstants.NAME_NOT_FOUND_EXCEPTION.equals(ex
				.getNestedExcClassName())
				|| SolmrConstants.COMMUNICATION_EXCEPTION.equals(ex
						.getNestedExcClassName())) {
			return new SolmrException(
					AnagErrors.ERRORE_SERVIZIO_PAPUA_NON_DISPONIBILE, 1);
		} else {
			throw new SolmrException(ex.getMessage());
		}
	}

	/**
	 * Conferma l'avvenuta lettura del messaggio obbligatorio da parte
	 * dell'utente specifico
	 * 
	 * @param idElencoMessaggi
	 * @param codiceFiscale
	 * @throws SystemException
	 * @throws InvalidParameterException
	 * @throws UnrecoverableException
	 * @throws AgriException
	 */
	public void confermaLetturaMessaggio(long idElencoMessaggi,
			String codiceFiscale) throws SolmrException {
		try {
			portStub.confermaLetturaMessaggio(idElencoMessaggi, codiceFiscale);
		} catch (Exception ex) {
			throw new SolmrException(ex.getMessage());
		}
	}

	/*
	 * public boolean isAlive() throws SystemException,
	 * InvalidParameterException, UnrecoverableException,SolmrException { return
	 * true; }
	 */

	public byte[] getAllegato(long idAllegato) throws SolmrException {
		try {
			return portStub.getAllegato(idAllegato);
		} catch (Exception ex) {
			throw new SolmrException(ex.getMessage());
		}
	}

	public ListaMessaggi getListaMessaggi(int idProcedimento,
			String codiceRuolo, String codiceFiscale, int tipoMessaggio,
			Boolean letto, Boolean obbligatorio, Boolean visibile)
			throws SolmrException, LogoutException {
		try {
			it.csi.smranag.smrgaa.ws.papuaserv.messaggistica.ListaMessaggi resp = portStub
					.getListaMessaggi(idProcedimento, codiceRuolo,
							codiceFiscale, tipoMessaggio, letto, obbligatorio,
							visibile);
			ListaMessaggi result = null;

			if (resp != null) {
				List<Messaggio> listMessaggi = new ArrayList<Messaggio>();
				if (resp.getMessaggi() != null && resp.getMessaggi().size() > 0) {

					for (it.csi.smranag.smrgaa.ws.papuaserv.messaggistica.Messaggio messResp : resp.getMessaggi()) {
						Messaggio mess = new Messaggio();
						mess.setConAllegati(messResp.isConAllegati());

						mess.setDataInizioValidita(DateUtils.convert(messResp
								.getDataInizioValidita()));
						mess.setIdElencoMessaggi(messResp.getIdElencoMessaggi());
						mess.setIdTipoMessaggio(messResp.getIdTipoMessaggio());
						mess.setLetto(messResp.isLetto());
						mess.setLetturaObbligatoria(messResp
								.isLetturaObbligatoria());
						mess.setTitolo(messResp.getTitolo());
						listMessaggi.add(mess);
					}

				}
				Map<Integer, Long> mapConteggio = new HashMap<Integer, Long>();
				mapConteggio.put(ListaMessaggi.TIPO.GENERICO,
						resp.getNumeroMessaggiGenerici());
				mapConteggio.put(ListaMessaggi.TIPO.TESTATA,
						resp.getNumeroMessaggiTestata());
				mapConteggio.put(ListaMessaggi.TIPO.LOGOUT,
						resp.getNumeroMessaggiLogout());

				result = new ListaMessaggi(mapConteggio,
						listMessaggi.toArray(new Messaggio[0]));
			}
			return result;
		} catch (Exception ex) {
			throw new SolmrException(ex.getMessage());
		}
	}

	public DettagliMessaggio getDettagliMessaggio(long idElencoMessaggi,
			String codiceFiscale) throws SolmrException {
		try {
			DettagliMessaggio result = null;
			it.csi.smranag.smrgaa.ws.papuaserv.messaggistica.DettagliMessaggio resp = portStub
					.getDettagliMessaggio(idElencoMessaggi, codiceFiscale);

			if (resp != null) {
				result = new DettagliMessaggio();

				List<Allegato> listAllegati = new ArrayList<Allegato>();
				
				if (resp.getAllegati() != null && resp.getAllegati().size() > 0) {

					for (it.csi.smranag.smrgaa.ws.papuaserv.messaggistica.Allegato allegatoResp : resp.getAllegati()) {
						Allegato allegato=new Allegato();
						allegato.setDescrizione(allegatoResp.getDescrizione());
						allegato.setNomeFile(allegatoResp.getNomeFile());
						allegato.setIdAllegato(allegatoResp.getIdAllegato());
					}
				}
					

				result.setAllegati(listAllegati.toArray(new Allegato[0]));

				result.setDataInizioValidita(DateUtils.convert(resp
						.getDataInizioValidita()));
				result.setIdElencoMessaggi(resp.getIdElencoMessaggi());
				result.setIdTipoMessaggio(resp.getIdTipoMessaggio());
				result.setLetto(resp.isLetto());
				result.setLetturaObbligatoria(resp.isLetturaObbligatoria());
				result.setTitolo(resp.getTitolo());
				if (resp.getUtenteAggiornamento()!=null){
					UtenteAggiornamento utenteAggiornamento=new UtenteAggiornamento();
					utenteAggiornamento.setCognome(resp.getUtenteAggiornamento().getCognome());
					utenteAggiornamento.setNome(resp.getUtenteAggiornamento().getNome());
					utenteAggiornamento.setDenominazioneEnte(resp.getUtenteAggiornamento().getDenominazioneEnte());
					//utenteAggiornamento.setIdEnte(resp.getUtenteAggiornamento().getCodiceEnte());
					utenteAggiornamento.setIdUtente(resp.getUtenteAggiornamento().getIdUtente());
					
					result.setUtenteAggiornamento(utenteAggiornamento);
				}
			}

			return result;
		} catch (Exception ex) {
			throw new SolmrException(ex.getMessage());
		}
	}

}