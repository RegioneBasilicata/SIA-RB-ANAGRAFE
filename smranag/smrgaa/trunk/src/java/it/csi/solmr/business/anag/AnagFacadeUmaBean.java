package it.csi.solmr.business.anag;

import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.ProvinciaVO;
import it.csi.solmr.dto.StringcodeDescription;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.AnagraficaAzVO;
import it.csi.solmr.dto.anag.DelegaVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.DataControlException;
import it.csi.solmr.exception.NotFoundException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.services.AgriLogger;

import java.util.Date;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class AnagFacadeUmaBean implements SessionBean {

	public void ejbCreate() throws CreateException {
	}

	public void ejbRemove() {
		anagAziendaLocal = null;
		anagDenominazioniLocal = null;
		soggettiLocal = null;
	}

	public void ejbActivate() {
		/** @todo Complete this method */
	}

	public void ejbPassivate() {
		/** @todo Complete this method */
	}

	public void setSessionContext(SessionContext sessionContext) {
		this.sessionContext = sessionContext;

		try {
			InitialContext ctx = new InitialContext();
			anagAziendaLocal = (AnagAziendaLocal) ctx.lookup("java:app/smrgaa/comp/env/solmr/anag/AnagAzienda!it.csi.solmr.business.anag.AnagAziendaLocal");
			anagDenominazioniLocal = (AnagDenominazioniLocal) ctx.lookup("java:app/smrgaa/comp/env/solmr/anag/AnagDenominazioni!it.csi.solmr.business.anag.AnagDenominazioniLocal");
			soggettiLocal = (SoggettiLocal) ctx.lookup("java:app/smrgaa/comp/env/solmr/anag/Soggetti!it.csi.solmr.business.anag.SoggettiLocal");
		} catch (NamingException ex) {
			AgriLogger.error(this, "NamingException in setSessionContext ="
					+ ex.getMessage());
		} catch (Exception ex) {
			AgriLogger.error(this,
					"Exception in setSessionContext =" + ex.getMessage());
		}
	}

	/**
   * 
   */
	private static final long serialVersionUID = 6831070391513108960L;

	SessionContext sessionContext;

	private transient AnagAziendaLocal anagAziendaLocal = null;

	private transient AnagDenominazioniLocal anagDenominazioniLocal = null;

	private transient SoggettiLocal soggettiLocal = null;

	public ProvinciaVO[] getProvinceByRegione(String idRegione)
			throws Exception {
		Vector<ProvinciaVO> result=anagDenominazioniLocal.getProvinceByRegione(idRegione);
		return (ProvinciaVO[]) (result==null ? null : (ProvinciaVO[]) result.toArray(new ProvinciaVO[0]));
	}

	// Metodo per recuperare l'azienda attiva
	public AnagAziendaVO findAziendaAttiva(Long idAzienda)
			throws DataAccessException, NotFoundException, Exception {
		return anagAziendaLocal.findAziendaAttiva(idAzienda);
	}

	public String getRappLegaleTitolareByIdAzienda(Long idAzienda)
			throws Exception, SolmrException {
		return anagAziendaLocal.getRappLegaleTitolareByIdAzienda(idAzienda);
	}

	public AnagraficaAzVO getDatiAziendaPerMacchine(Long idAzienda)
			throws Exception, SolmrException {
		return anagAziendaLocal.getDatiAziendaPerMacchine(idAzienda);
	}

	public PersonaFisicaVO getTitolareORappresentanteLegaleAzienda(
			Long idAzienda, java.util.Date data) throws NotFoundException,
			Exception, SolmrException {
		return anagAziendaLocal.getTitolareORappresentanteLegaleAzienda(
				idAzienda, data);
	}

	public CodeDescription[] getTipiIntermediario()
			throws NotFoundException, Exception {
		
		Vector<CodeDescription> result=anagDenominazioniLocal.getTipiIntermediario();
		return (CodeDescription[]) (result==null ? null : (CodeDescription[]) result.toArray(new CodeDescription[0]));
	}

	public CodeDescription[] getTipiIntermediarioUmaProv()
			throws NotFoundException, Exception {
		Vector<CodeDescription> result=anagDenominazioniLocal.getTipiIntermediarioUmaProv();
		return (CodeDescription[]) (result==null ? null : (CodeDescription[]) result.toArray(new CodeDescription[0]));
	}

	// Metodo per recuperare il flag partita iva relativo ad una specifica forma
	// giuridica
	public String getFlagPartitaIva(Long idTipoFormaGiuridica)
			throws Exception, SolmrException {
		return anagAziendaLocal.getFlagPartitaIva(idTipoFormaGiuridica);
	}

	// Metodo per recuperare il valore del flagCCIAA relativo ad una forma
	// giuridica
	public String getFormaGiuridicaFlagCCIAA(Long idFormaGiuridica)
			throws Exception, SolmrException {
		return anagAziendaLocal.getFormaGiuridicaFlagCCIAA(idFormaGiuridica);
	}

	// Metodo per verificare la correttezza della provincia REA
	public boolean isProvinciaReaValida(String siglaProvincia)
			throws Exception, SolmrException {
		return anagAziendaLocal.isProvinciaReaValida(siglaProvincia);
	}

	// Metodo per recuperare il codiceIstat del comune a partire dal comune
	public String ricercaCodiceComune(String descrizioneComune,
			String siglaProvincia) throws DataAccessException,
			NotFoundException, DataControlException, Exception {
		return anagDenominazioniLocal.ricercaCodiceComune(descrizioneComune,
				siglaProvincia);
	}

	public String ricercaCodiceComuneNonEstinto(String descrizioneComune,
			String siglaProvincia) throws Exception, SolmrException {
		return anagDenominazioniLocal.ricercaCodiceComuneNonEstinto(
				descrizioneComune, siglaProvincia);
	}

	// Metodo per recuerare il codice fiscale del comune
	public String ricercaCodiceFiscaleComune(String descrizioneComune,
			String siglaProvincia) throws Exception, DataControlException {
		return anagDenominazioniLocal.ricercaCodiceFiscaleComune(
				descrizioneComune, siglaProvincia);
	}

	public void checkIsCUAAPresent(String cuaa, Long idAzienda)
			throws Exception, SolmrException {
		anagAziendaLocal.checkIsCUAAPresent(cuaa, idAzienda);
	}

	public void checkPartitaIVA(String partitaIVA, Long idAzienda)
			throws DataAccessException, Exception, SolmrException {
		anagAziendaLocal.checkPartitaIVA(partitaIVA, idAzienda);
	}

	public AnagAziendaVO getAziendaById(Long idAnagAzienda)
			throws NotFoundException, Exception, SolmrException {
		return anagAziendaLocal.getAziendaById(idAnagAzienda);
	}

	public PersonaFisicaVO getPersonaFisica(String cuaa)
			throws DataAccessException, Exception, SolmrException {
		return anagAziendaLocal.getPersonaFisica(cuaa);
	}

	public ComuneVO getComuneByCUAA(String cuaa) throws Exception,
			SolmrException {
		return anagAziendaLocal.getComuneByCUAA(cuaa);
	}


	// Metodo per reperire il rappresentante legale di una società a partire
	// dall'id_anagrafica_azienda
	public PersonaFisicaVO getRappresentanteLegaleFromIdAnagAzienda(
			Long idAnagAzienda) throws Exception, SolmrException {
		return anagAziendaLocal
				.getRappresentanteLegaleFromIdAnagAzienda(idAnagAzienda);
	}

	public AnagAziendaVO findAziendaAttivabyCriterio(String cuaa,
			String partitaIva) throws Exception, SolmrException {
		return anagAziendaLocal.findAziendaAttivabyCriterio(cuaa, partitaIva);
	}

	// Inizio gestione soggetti collegati
	public PersonaFisicaVO[] getSoggetti(Long idAzienda, Date data)
			throws Exception, SolmrException {
		Vector<PersonaFisicaVO> result=soggettiLocal.getSoggetti(idAzienda, data);
		return (PersonaFisicaVO[]) (result==null ? null : (PersonaFisicaVO[]) result.toArray(new PersonaFisicaVO[0]));
	}

	public PersonaFisicaVO[] getSoggetti(Long idAzienda, Boolean storico)
			throws Exception, SolmrException {
		Vector<PersonaFisicaVO> result=soggettiLocal.getSoggetti(idAzienda, storico);
		return (PersonaFisicaVO[]) (result==null ? null : (PersonaFisicaVO[]) result.toArray(new PersonaFisicaVO[0]));
	}

	/**
	 * Metodo che mi restituisce la delega di un dato procedimento relativo ad
	 * una determinata azienda agricola
	 * 
	 * @param idAzienda
	 * @param idProcedimento
	 * @return it.csi.solmr.dto.anag.DelegaVO
	 * @throws Exception
	 */
	public DelegaVO getDelegaByAzienda(Long idAzienda,
			Long idProcedimento) throws Exception {
		return anagAziendaLocal.getDelegaByAziendaAndIdProcedimento(idAzienda,
				idProcedimento);
	}
	

	public String getDenominazioneByIdAzienda(Long idAzienda) throws Exception,
			SolmrException {
		return anagAziendaLocal.getDenominazioneByIdAzienda(idAzienda);
	}

	/**
	 * Metodo che mi restituisce l'elenco degli stati esteri in relazione ai
	 * parametri
	 * 
	 * @param statoEstero
	 * @param estinto
	 * @param flagCatastoAttivo
	 * @return java.util.Vector
	 * @throws DataAccessException
	 */
	public ComuneVO[] ricercaStatoEstero(String statoEstero,
			String estinto, String flagCatastoAttivo) throws Exception {		
		Vector<ComuneVO> result=anagDenominazioniLocal.ricercaStatoEstero(statoEstero, estinto,flagCatastoAttivo);
		return (ComuneVO[]) (result==null ? null : (ComuneVO[]) result.toArray(new ComuneVO[0]));
	}

	public ComuneVO[] getComuniLikeProvAndCom(String provincia,
			String comune) throws NotFoundException, SolmrException {
		try {
			Vector<ComuneVO> result=anagDenominazioniLocal.getComuniLikeByProvAndCom(provincia,comune);
			return (ComuneVO[]) (result==null ? null : (ComuneVO[]) result.toArray(new ComuneVO[0]));
		} catch (Exception ex) {
			throw new SolmrException(ex.getMessage());
		}
	}

	public ComuneVO[] getComuniNonEstintiLikeProvAndCom(String provincia,
			String comune, String flagEstero) throws NotFoundException,
			SolmrException {
		try {
			Vector<ComuneVO> result=anagDenominazioniLocal.getComuniNonEstintiLikeByProvAndCom(provincia, comune, flagEstero);
			return (ComuneVO[]) (result==null ? null : (ComuneVO[]) result.toArray(new ComuneVO[0]));
		} catch (Exception ex) {
			throw new SolmrException(ex.getMessage());
		}
	}

	public StringcodeDescription[] getProvincePiemonte()
			throws Exception, SolmrException {
		Vector<StringcodeDescription> result=anagDenominazioniLocal.getProvincePiemonte();
		return (StringcodeDescription[]) (result==null ? null : (StringcodeDescription[]) result.toArray(new StringcodeDescription[0]));
	}

	public CodeDescription[] getTipiFormaGiuridica(Long idTipologiaAzienda)
			throws NotFoundException, Exception {
		Vector<CodeDescription> result=anagDenominazioniLocal.getTipiFormaGiuridica(idTipologiaAzienda);
		return (CodeDescription[]) (result==null ? null : (CodeDescription[]) result.toArray(new CodeDescription[0]));
	}

}
