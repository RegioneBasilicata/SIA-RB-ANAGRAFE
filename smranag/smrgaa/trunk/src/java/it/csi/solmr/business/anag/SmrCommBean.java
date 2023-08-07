package it.csi.solmr.business.anag;

import it.csi.csi.porte.InfoPortaDelegata;
import it.csi.csi.porte.proxy.PDProxy;
import it.csi.csi.util.xml.PDConfigReader;
import it.csi.csi.wrapper.SystemException;
import it.csi.csi.wrapper.UnrecoverableException;
import it.csi.smrcomms.smrcomm.dto.datientiprivati.DatiEntePrivatoVO;
import it.csi.smrcomms.smrcomm.dto.filtro.EntePrivatoFiltroVO;
import it.csi.smrcomms.smrcomm.interfacecsi.ISmrcommCSIInterface;
import it.csi.solmr.dto.comune.AmmCompetenzaVO;
import it.csi.solmr.dto.comune.IntermediarioVO;
import it.csi.solmr.dto.comune.TecnicoAmministrazioneVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.dto.profile.TipoProcedimentoVO;
import it.csi.solmr.dto.profile.UtenteIride2VO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.exception.services.ErrorTypes;
import it.csi.solmr.exception.services.InvalidParameterException;
import it.csi.solmr.interfaceCSI.comune.services.ComuneServiceCSIInterface;
import it.csi.solmr.util.SolmrLogger;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Stateless(name="comp/env/solmr/anag/SmrComm",mappedName="comp/env/solmr/anag/SmrComm")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(value=javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
public class SmrCommBean implements SmrCommLocal {
	
	private static final long serialVersionUID = 7286013823744736565L;

	SessionContext sessionContext;

	String smrCommPortaDelegataXML = (String)SolmrConstants.get("FILE_PD_SMRCOMM");

	@Resource
	public void setSessionContext(SessionContext sessionContext)
	{
		this.sessionContext = sessionContext;
	}

	/**
	 *
	 * @throws Exception
	 * @return ComuneServiceCSIInterface
	 */
	private ComuneServiceCSIInterface getSmrCommClient() throws Exception
	{
		// caricamento porta delegata
		InfoPortaDelegata informazioniPortaDelegata = PDConfigReader.read(getClass().getResourceAsStream(smrCommPortaDelegataXML));
		return (ComuneServiceCSIInterface) PDProxy.newInstance(informazioniPortaDelegata);
	}
	
	private ISmrcommCSIInterface getSmrCommNPClient() throws Exception
  {
    InfoPortaDelegata informazioniPortaDelegata = PDConfigReader.read(getClass()
        .getResourceAsStream(SolmrConstants.FILE_PD_SMRCOMSRV_NP));

    return (ISmrcommCSIInterface) PDProxy.newInstance(informazioniPortaDelegata);
  }




	public AmmCompetenzaVO serviceFindAmmCompetenzaById(Long idAmmCompetenza)
	  throws SolmrException,Exception
	{
		AmmCompetenzaVO result=null;
		try
		{
			result=getSmrCommClient().serviceFindAmmCompetenzaById(idAmmCompetenza);
		}
		catch (InvalidParameterException i)
		{
			SolmrLogger.fatal(this, "serviceFindAmmCompetenzaById - InvalidParameterException: "+i.getMessage());
			throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER);
		}
		catch(java.io.IOException ioex)
		{
			SolmrLogger.fatal(this, "serviceFindAmmCompetenzaById - IOException: "+ioex.getMessage());
			//ioex.printStackTrace();
			throw new SolmrException((String)AnagErrors.get("ERR_SMRCOMM_TO_CONNECT"));
		}
		catch (Exception e)
		{
			SolmrLogger.fatal(this, "serviceFindAmmCompetenzaById - Exception: "+e.getMessage());
			//ioex.printStackTrace();
			throw new SolmrException((String)AnagErrors.get("ERR_SMRCOMM_TO_CONNECT"));
		}
		return result;
	}


	public AmmCompetenzaVO[] serviceFindAmmCompetenzaByIdRange(String idAmmCompetenza[])
	  throws SolmrException,Exception
	{
		AmmCompetenzaVO result[]=null;
		try
		{
			result=getSmrCommClient().serviceFindAmmCompetenzaByIdRange(idAmmCompetenza);
		}
		catch (InvalidParameterException i)
		{
			SolmrLogger.fatal(this, "serviceFindAmmCompetenzaByIdRange - InvalidParameterException: "+i.getMessage());
			throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER);
		}
		catch(java.io.IOException ioex)
		{
			SolmrLogger.fatal(this, "serviceFindAmmCompetenzaByIdRange - IOException: "+ioex.getMessage());
			throw new SolmrException((String)AnagErrors.get("ERR_SMRCOMM_TO_CONNECT"));
		}
		catch (Exception e)
		{
			SolmrLogger.fatal(this, "serviceFindAmmCompetenzaByIdRange - Exception: "+e.getMessage());
			throw new SolmrException((String)AnagErrors.get("ERR_SMRCOMM_TO_CONNECT"));
		}
		return result;
	}


	/*public Vector<UtenteProcedimento> serviceGetUtenteProcedimento(String codiceFiscale,Long idProcedimento,String ruolo, String codiceEnte, String dirittoAccesso, Long idLivello)
	  throws SystemException,SolmrException,Exception
	{
		Vector<UtenteProcedimento> result=new Vector<UtenteProcedimento>();
		try
		{
			result=getSmrCommClient().serviceGetUtenteProcedimento(codiceFiscale,idProcedimento,ruolo,codiceEnte,dirittoAccesso,idLivello);
		}
		catch (InvalidParameterException i)
		{
			SolmrLogger.fatal(this, "serviceGetUtenteProcedimento - InvalidParameterException: "+i.getMessage());
			throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER);
		}
		catch(java.io.IOException ioex)
		{
			SolmrLogger.fatal(this, "serviceGetUtenteProcedimento - IOException: "+ioex.getMessage());
			//ioex.printStackTrace();
			throw new SolmrException((String)AnagErrors.get("ERR_SMRCOMM_TO_CONNECT"));
		}
		catch (Exception e)
		{
			SolmrLogger.fatal(this, "serviceGetUtenteProcedimento - Exception: "+e.getMessage());
			//ioex.printStackTrace();
			throw new SolmrException((String)AnagErrors.get("ERR_SMRCOMM_TO_CONNECT"));
		}
		return result;
	}*/


	public AmmCompetenzaVO[] serviceGetListAmmCompetenzaByComuneCollegato(String istatComune,String tipoAmministrazione)
	throws SolmrException,Exception
	{
		AmmCompetenzaVO result[]=null;
		try
		{
			result=getSmrCommClient().serviceGetListAmmCompetenzaByComuneCollegato(istatComune,tipoAmministrazione);
		}
		catch (InvalidParameterException i)
		{
			SolmrLogger.fatal(this, "serviceGetListAmmCompetenzaByComuneCollegato - InvalidParameterException: "+i.getMessage());
			throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER);
		}
		catch(java.io.IOException ioex)
		{
			SolmrLogger.fatal(this, "serviceGetListAmmCompetenzaByComuneCollegato - IOException: "+ioex.getMessage());
			//ioex.printStackTrace();
			throw new SolmrException((String)AnagErrors.get("ERR_SMRCOMM_TO_CONNECT"));
		}
		catch (Exception e)
		{
			SolmrLogger.fatal(this, "serviceGetListAmmCompetenzaByComuneCollegato - Exception: "+e.getMessage());
			//ioex.printStackTrace();
			throw new SolmrException((String)AnagErrors.get("ERR_SMRCOMM_TO_CONNECT"));
		}
		return result;
	}

	/**
	 * Metodo che si occupa di invocare il servizio di comune che mi restituisce il record della tabella DB_AMM_COMPETENZA a partire
	 * dal codice ente
	 * @param codiceAmm String
	 * @return AmmCompetenzaVO
	 * @throws SolmrException
	 * @throws Exception
	 */
	public AmmCompetenzaVO serviceFindAmmCompetenzaByCodiceAmm(String codiceAmm) throws SolmrException, Exception {
		AmmCompetenzaVO result = null;
		try {
			result = getSmrCommClient().serviceFindAmmCompetenzaByCodiceAmm(codiceAmm);
		}
		catch (InvalidParameterException i) {
			SolmrLogger.error(this, "serviceFindAmmCompetenzaById - InvalidParameterException: "+i.getMessage());
			throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER);
		}
		catch(java.io.IOException ioex) {
			SolmrLogger.error(this, "serviceFindAmmCompetenzaById - IOException: "+ioex.getMessage());
			throw new SolmrException((String)AnagErrors.get("ERR_SMRCOMM_TO_CONNECT"));
		}
		catch (Exception e) {
			SolmrLogger.error(this, "serviceFindAmmCompetenzaById - Exception: "+e.getMessage());
			throw new SolmrException((String)AnagErrors.get("ERR_SMRCOMM_TO_CONNECT"));
		}
		return result;
	}



	private SolmrException checkServicesUnavailable(SystemException ex)
	throws SolmrException
	{
		SolmrLogger.fatal(this,
				"[ComuneBean:checkServicesUnavailable] SystemException rilevata su chiamata a servizio di Comune. Eccezione = " +
				ex.toString());

		if(SolmrConstants.SIGNATURE_NAME_NOT_FOUND_EXCEPTION.equals(
				ex.getNestedExcClassName()) ||
				SolmrConstants.SIGNATURE_COMMUNICATION_EXCEPTION.equals(
						ex.getNestedExcClassName()))
		{
			return new SolmrException(AnagErrors.ERRORE_COMUNE_NON_DISPONIBILE,
					SolmrConstants.CODICE_ERRORE_COMUNE_NON_DISPONIBILE);
		}
		else
		{
			return new SolmrException(AnagErrors.ERRORE_ACCESSO_A_COMUNE,
					SolmrConstants.CODICE_ERRORE_DI_ACCESSO_A_COMUNE);
		}
	}

	public Boolean isUtenteConRuoloSuProcedimento(String codiceFiscale,
			Long idProcedimento)
	throws SystemException, InvalidParameterException, UnrecoverableException,
	SolmrException
	{
		SolmrLogger.debug(this,
		"[ComuneBean::isUtenteConRuoloSuProcedimento] BEGIN.");

		try
		{
			return getSmrCommClient()
			.isUtenteConRuoloSuProcedimento(codiceFiscale, idProcedimento);
		}
		catch(SystemException ex)
		{
			throw checkServicesUnavailable(ex);
		}
		catch(InvalidParameterException ex)
		{
			SolmrLogger.dumpStackTrace(this,
					"[ComuneBean::isUtenteConRuoloSuProcedimento] " +
					AnagErrors.ERRORE_ACCESSO_A_COMUNE, ex);
			throw ex;
		}
		catch(UnrecoverableException ex)
		{
			SolmrLogger.dumpStackTrace(this,
					"[ComuneBean::isUtenteConRuoloSuProcedimento] " +
					AnagErrors.ERRORE_ACCESSO_A_COMUNE, ex);
			throw ex;
		}
		catch(EJBException ex)
		{
			throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_COMUNE,
					SolmrConstants.CODICE_ERRORE_DI_ACCESSO_A_COMUNE);
		}
		catch(Exception ex)
		{
			throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_COMUNE,
					SolmrConstants.CODICE_ERRORE_DI_ACCESSO_A_COMUNE);
		}
		finally
		{
			SolmrLogger.debug(this,
			"[ComuneBean::isUtenteConRuoloSuProcedimento] END.");
		}
	}

	/**
	 * non più usato....da eliminare prox passaggioo
	 * 
	 * 
	 * @param utenteIride2VO
	 * @return
	 * @throws SystemException
	 * @throws InvalidParameterException
	 * @throws UnrecoverableException
	 * @throws SolmrException
	 */
	public Boolean isUtenteAbilitatoProcedimento(UtenteIride2VO utenteIride2VO)
	throws SystemException, InvalidParameterException, UnrecoverableException,
	SolmrException
	{
		SolmrLogger.debug(this, "[ComuneBean::isUtenteAbilitatoProcedimento] BEGIN.");

		try
		{
			return getSmrCommClient()
			.isUtenteAbilitatoProcedimento(utenteIride2VO);
		}
		catch(SystemException ex)
		{
			throw checkServicesUnavailable(ex);
		}
		catch(InvalidParameterException ex)
		{
			SolmrLogger.dumpStackTrace(this,
					"[ComuneBean::isUtenteAbilitatoProcedimento] " +
					AnagErrors.ERRORE_ACCESSO_A_COMUNE, ex);
			throw ex;
		}
		catch(UnrecoverableException ex)
		{
			SolmrLogger.dumpStackTrace(this,
					"[ComuneBean::isUtenteAbilitatoProcedimento] " +
					AnagErrors.ERRORE_ACCESSO_A_COMUNE, ex);
			throw ex;
		}
		catch(EJBException ex)
		{
			throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_COMUNE,
					SolmrConstants.CODICE_ERRORE_DI_ACCESSO_A_COMUNE);
		}
		catch(Exception ex)
		{
			throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_COMUNE,
					SolmrConstants.CODICE_ERRORE_DI_ACCESSO_A_COMUNE);
		}
		finally
		{
			SolmrLogger.debug(this, "[ComuneBean::isUtenteAbilitatoProcedimento] END.");
		}
	}

	/**
	 * probabilmente da eliminare****
	 * 
	 * 
	 * @param utenteIride2VO
	 * @return
	 * @throws SystemException
	 * @throws InvalidParameterException
	 * @throws UnrecoverableException
	 * @throws SolmrException
	 */
	public Long writeAccessLogUser(UtenteIride2VO utenteIride2VO)
	throws SystemException, InvalidParameterException, UnrecoverableException,
	SolmrException
	{
		SolmrLogger.debug(this, "[ComuneBean::writeAccessLogUser] BEGIN.");

		try
		{
			return getSmrCommClient().writeAccessLogUser(utenteIride2VO);
		}
		catch(SystemException ex)
		{
			throw checkServicesUnavailable(ex);
		}
		catch(InvalidParameterException ex)
		{
			SolmrLogger.dumpStackTrace(this,
					"[ComuneBean::writeAccessLogUser] " +
					AnagErrors.ERRORE_ACCESSO_A_COMUNE, ex);
			throw ex;
		}
		catch(UnrecoverableException ex)
		{
			SolmrLogger.dumpStackTrace(this,
					"[ComuneBean::writeAccessLogUser] " +
					AnagErrors.ERRORE_ACCESSO_A_COMUNE, ex);
			throw ex;
		}
		catch(EJBException ex)
		{
			throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_COMUNE,
					SolmrConstants.CODICE_ERRORE_DI_ACCESSO_A_COMUNE);
		}
		catch(Exception ex)
		{
			throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_COMUNE,
					SolmrConstants.CODICE_ERRORE_DI_ACCESSO_A_COMUNE);
		}
		finally
		{
			SolmrLogger.debug(this, "[ComuneBean::writeAccessLogUser] END.");
		}
	}

	public RuoloUtenza loadRoleUser(UtenteIride2VO utenteIride2VO)
	throws SystemException, InvalidParameterException, UnrecoverableException,
	SolmrException
	{
		SolmrLogger.debug(this, "[ComuneBean::loadRoleUser] BEGIN.");

		try
		{
			return getSmrCommClient().loadRoleUser(utenteIride2VO);
		}
		catch(SystemException ex)
		{
			throw checkServicesUnavailable(ex);
		}
		catch(InvalidParameterException ex)
		{
			SolmrLogger.dumpStackTrace(this,
					"[ComuneBean::loadRoleUser] " + AnagErrors.ERRORE_ACCESSO_A_COMUNE, ex);
			throw ex;
		}
		catch(UnrecoverableException ex)
		{
			SolmrLogger.dumpStackTrace(this,
					"[ComuneBean::loadRoleUser] " + AnagErrors.ERRORE_ACCESSO_A_COMUNE, ex);
			throw ex;
		}
		catch(EJBException ex)
		{
			throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_COMUNE,
					SolmrConstants.CODICE_ERRORE_DI_ACCESSO_A_COMUNE);
		}
		catch(Exception ex)
		{
			throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_COMUNE,
					SolmrConstants.CODICE_ERRORE_DI_ACCESSO_A_COMUNE);
		}
		finally
		{
			SolmrLogger.debug(this, "[ComuneBean::loadRoleUser] END.");
		}
	}

	/**
	 * da verificare ma penso eliminabile
	 * 
	 * 
	 * @param descrizione
	 * @return
	 * @throws SystemException
	 * @throws InvalidParameterException
	 * @throws UnrecoverableException
	 * @throws SolmrException
	 */
	public TipoProcedimentoVO serviceFindTipoProcedimentoByDescrizioneProcedimento(
			String descrizione)
	throws SystemException, InvalidParameterException, UnrecoverableException,
	SolmrException
	{
		SolmrLogger.debug(this,
		"[ComuneBean::serviceFindTipoProcedimentoByDescrizioneProcedimento] BEGIN.");

		try
		{
			return getSmrCommClient()
			.serviceFindTipoProcedimentoByDescrizioneProcedimento(descrizione);
		}
		catch(SystemException ex)
		{
			throw checkServicesUnavailable(ex);
		}
		catch(InvalidParameterException ex)
		{
			SolmrLogger.dumpStackTrace(this,
					"[ComuneBean::serviceFindTipoProcedimentoByDescrizioneProcedimento] " +
					AnagErrors.ERRORE_ACCESSO_A_COMUNE, ex);
			throw ex;
		}
		catch(UnrecoverableException ex)
		{
			SolmrLogger.dumpStackTrace(this,
					"[ComuneBean::serviceFindTipoProcedimentoByDescrizioneProcedimento] " +
					AnagErrors.ERRORE_ACCESSO_A_COMUNE, ex);
			throw ex;
		}
		catch(EJBException ex)
		{
			throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_COMUNE,
					SolmrConstants.CODICE_ERRORE_DI_ACCESSO_A_COMUNE);
		}
		catch(Exception ex)
		{
			throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_COMUNE,
					SolmrConstants.CODICE_ERRORE_DI_ACCESSO_A_COMUNE);
		}
		finally
		{
			SolmrLogger.debug(this,
			"[ComuneBean::serviceFindTipoProcedimentoByDescrizioneProcedimento] END.");
		}
	}

	/**
	 * Metodo che invoca un servizio di comune che si occupa di reperire i dati dell'intermediario gestendo
	 * contemporaneamente sia il profilo intermediario che quello di OPR gestore
	 * @param codiceFiscale String
	 * @return IntermediarioVO
	 * @throws Exception
	 * @throws SolmrException
	 */
	public IntermediarioVO serviceFindIntermediarioByCodiceFiscale(String codiceFiscale) throws Exception, SolmrException {
		SolmrLogger.debug(this, "Invocating method serviceFindIntermediarioByCodiceFiscale in SmrCommBean/n");
		try {
			SolmrLogger.debug(this, "Value of parameter 1 [CODICE_FISCALE] for method serviceFindIntermediarioByCodiceFiscale: " +codiceFiscale+"in SmrCommBean\n");
			return getSmrCommClient().serviceFindIntermediarioByCodiceFiscale(codiceFiscale);
		}
		catch(SystemException ex) {
			SolmrLogger.error(this, "Catching SystemException in method serviceFindIntermediarioByCodiceFiscale with this message: "+ex.getMessage()+"\n");
			throw new Exception(ex.getMessage());
		}
		catch(InvalidParameterException ipe) {
			SolmrLogger.error(this, "Catching InvalidParameterException in method serviceFindIntermediarioByCodiceFiscale with this message: "+ipe.getMessage()+"\n");
			throw new Exception(ipe.getMessage());
		}
		catch(UnrecoverableException ue) {
			SolmrLogger.error(this, "Catching UnrecoverableException in method serviceFindIntermediarioByCodiceFiscale with this message: "+ue.getMessage()+"\n");
			throw new Exception(ue.getMessage());
		}
		catch(EJBException ejbex) {
			SolmrLogger.error(this, "Catching EJBException in method serviceFindIntermediarioByCodiceFiscale with this message: "+ejbex.getMessage()+"\n");
			throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_COMUNE, SolmrConstants.CODICE_ERRORE_DI_ACCESSO_A_COMUNE);
		}
		catch(Exception e) {
			SolmrLogger.error(this, "Catching Exception in method serviceFindIntermediarioByCodiceFiscale with this message: "+e.getMessage()+"\n");
			throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_COMUNE, SolmrConstants.CODICE_ERRORE_DI_ACCESSO_A_COMUNE);
		}
		finally {
			SolmrLogger.debug(this, "Invocated method serviceFindIntermediarioByCodiceFiscale in SmrCommBean/n");
		}
	}


	/**
	 * Metodo che invoca un servizio di comune che si occupa di reperire i dati dell'intermediario gestendo
	 * contemporaneamente sia il profilo intermediario che quello di OPR gestore
	 * @param idIntermediario Long
	 * @return IntermediarioVO
	 * @throws Exception
	 * @throws SolmrException
	 */
	public IntermediarioVO serviceFindIntermediarioByIdIntermediario(Long idIntermediario) throws Exception, SolmrException {
		SolmrLogger.debug(this, "Invocating method serviceFindIntermediarioByIdIntermediario in SmrCommBean/n");
		try {
			SolmrLogger.debug(this, "Value of parameter 1 [idIntermediario] for method serviceFindIntermediarioByIdIntermediario: " +idIntermediario+"in SmrCommBean\n");
			return getSmrCommClient().serviceFindIntermediarioByIdIntermediario(idIntermediario);
		}
		catch(SystemException ex) {
			SolmrLogger.error(this, "Catching SystemException in method serviceFindIntermediarioByIdIntermediario with this message: "+ex.getMessage()+"\n");
			throw new Exception(ex.getMessage());
		}
		catch(InvalidParameterException ipe) {
			SolmrLogger.error(this, "Catching InvalidParameterException in method serviceFindIntermediarioByIdIntermediario with this message: "+ipe.getMessage()+"\n");
			throw new Exception(ipe.getMessage());
		}
		catch(UnrecoverableException ue) {
			SolmrLogger.error(this, "Catching UnrecoverableException in method serviceFindIntermediarioByIdIntermediario with this message: "+ue.getMessage()+"\n");
			throw new Exception(ue.getMessage());
		}
		catch(EJBException ejbex) {
			SolmrLogger.error(this, "Catching EJBException in method serviceFindIntermediarioByIdIntermediario with this message: "+ejbex.getMessage()+"\n");
			throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_COMUNE, SolmrConstants.CODICE_ERRORE_DI_ACCESSO_A_COMUNE);
		}
		catch(Exception e) {
			SolmrLogger.error(this, "Catching Exception in method serviceFindIntermediarioByIdIntermediario with this message: "+e.getMessage()+"\n");
			throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_COMUNE, SolmrConstants.CODICE_ERRORE_DI_ACCESSO_A_COMUNE);
		}
		finally {
			SolmrLogger.debug(this, "Invocated method serviceFindIntermediarioByIdIntermediario in SmrCommBean/n");
		}
	}


	public IntermediarioVO serviceFindResponsabileByIdIntermediarioAndIdProcToDateRiferimento(Long idIntermediario, Long idProcedimento, Date dataRiferimento)
	throws Exception, SolmrException
	{
		IntermediarioVO result=null;
		try
		{
			result=getSmrCommClient().serviceFindResponsabileByIdIntermediarioAndIdProcToDateRiferimento(idIntermediario, idProcedimento, dataRiferimento);
		}
		catch (InvalidParameterException i)
		{
			SolmrLogger.fatal(this, "serviceFindAmmCompetenzaById - InvalidParameterException: "+i.getMessage());
			throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER);
		}
		catch(java.io.IOException ioex)
		{
			SolmrLogger.fatal(this, "serviceFindAmmCompetenzaById - IOException: "+ioex.getMessage());
			//ioex.printStackTrace();
			throw new SolmrException((String)AnagErrors.get("ERR_SMRCOMM_TO_CONNECT"));
		}
		catch (Exception e)
		{
			SolmrLogger.fatal(this, "serviceFindAmmCompetenzaById - Exception: "+e.getMessage());
			//ioex.printStackTrace();
			throw new SolmrException((String)AnagErrors.get("ERR_SMRCOMM_TO_CONNECT"));
		}
		return result;

	}
	
	/**
	 * Metodo che richiama un servizio di comune per ottenere l'elenco delle
	 * amministrazione di competenza
	 * 
	 * @return it.csi.solmr.dto.comune.AmmCompetenzaVO[]
	 * @throws Exception
	 * @throws SolmrException
	 */
	public AmmCompetenzaVO[] serviceGetListAmmCompetenza() throws Exception, SolmrException {
		AmmCompetenzaVO[] elencoAmmCompetenza = null;
		try {
			elencoAmmCompetenza = getSmrCommClient().serviceGetListAmmCompetenza();
		}
		catch(InvalidParameterException i) {
			SolmrLogger.error(this, "serviceGetListAmmCompetenza - InvalidParameterException: "+i);
			throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER);
		}
		catch(java.io.IOException ioex) {
			SolmrLogger.error(this, "serviceGetListAmmCompetenza - IOException: "+ioex);
			throw new SolmrException((String)AnagErrors.get("ERR_SMRCOMM_TO_CONNECT"));
		}
		catch(Exception e) {
			SolmrLogger.error(this, "serviceGetListAmmCompetenza - Exception: "+e);
			throw new SolmrException((String)AnagErrors.get("ERR_SMRCOMM_TO_CONNECT"));
		}
		return elencoAmmCompetenza;
	}
	
	/**
	 * Metodo che richiama un servizio di comune per verifica la gerarchia tra
	 * gli utenti di tipo intermediario/OPR
	 * 
	 * @param idUtenteConnesso
	 * @param idUtentePratica
	 * @return boolean
	 * @throws Exception
	 * @throws SolmrException
	 */
	/*public boolean serviceVerificaGerarchia(Long idUtenteConnesso, Long idUtentePratica) throws Exception, SolmrException {
		try {
			return getSmrCommClient().serviceVerificaGerarchia(idUtenteConnesso, idUtentePratica);
		}
		catch(InvalidParameterException i) {
			SolmrLogger.error(this, "serviceVerificaGerarchia - InvalidParameterException: "+i);
			throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER);
		}
		catch(java.io.IOException ioex) {
			SolmrLogger.error(this, "serviceVerificaGerarchia - IOException: "+ioex);
			throw new SolmrException((String)AnagErrors.get("ERR_SMRCOMM_TO_CONNECT"));
		}
		catch(Exception e) {
			SolmrLogger.error(this, "serviceVerificaGerarchia - Exception: "+e);
			throw new SolmrException((String)AnagErrors.get("ERR_SMRCOMM_TO_CONNECT"));
		}
	}*/
	
	
	public TecnicoAmministrazioneVO[] serviceGetTecnicoAmmByIdAmmCompAndIdProcedimento(Long idAmmCompetenza,
	    Long idProcedimento)
    throws SolmrException,Exception
  {
	  TecnicoAmministrazioneVO[] result=null;
    try
    {
      result = getSmrCommClient().serviceGetTecnicoAmmByIdAmmCompAndIdProcedimento(idAmmCompetenza, idProcedimento);
    }
    catch (InvalidParameterException i)
    {
      SolmrLogger.fatal(this, "serviceGetTecnicoAmmByIdAmmCompAndIdProcedimento - InvalidParameterException: "+i.getMessage());
      throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER);
    }
    catch(java.io.IOException ioex)
    {
      SolmrLogger.fatal(this, "serviceGetTecnicoAmmByIdAmmCompAndIdProcedimento - IOException: "+ioex.getMessage());
      //ioex.printStackTrace();
      throw new SolmrException((String)AnagErrors.get("ERR_SMRCOMM_TO_CONNECT"));
    }
    catch (Exception e)
    {
      SolmrLogger.fatal(this, "serviceGetTecnicoAmmByIdAmmCompAndIdProcedimento - Exception: "+e.getMessage());
      //ioex.printStackTrace();
      throw new SolmrException((String)AnagErrors.get("ERR_SMRCOMM_TO_CONNECT"));
    }
    return result;
  }
	
	
	public long[] smrcommSearchListIdEntiPrivatiByCodiceEntePrivatoRange(String[] arrCodiceEntePrivato,
      boolean flagCessazione)
    throws SolmrException,Exception
  {
    long[] result=null;
    try
    {
      result = getSmrCommNPClient().smrcommSearchListIdEntiPrivatiByCodiceEntePrivatoRange(arrCodiceEntePrivato,
          flagCessazione);
    }
    catch (InvalidParameterException i)
    {
      SolmrLogger.fatal(this, "smrcommSearchListIdEntiPrivatiByCodiceEntePrivatoRange - InvalidParameterException: "+i.getMessage());
      throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER);
    }
    catch(java.io.IOException ioex)
    {
      SolmrLogger.fatal(this, "smrcommSearchListIdEntiPrivatiByCodiceEntePrivatoRange - IOException: "+ioex.getMessage());
      //ioex.printStackTrace();
      throw new SolmrException((String)AnagErrors.get("ERR_SMRCOMM_TO_CONNECT"));
    }
    catch (Exception e)
    {
      SolmrLogger.fatal(this, "smrcommSearchListIdEntiPrivatiByCodiceEntePrivatoRange - Exception: "+e.getMessage());
      //ioex.printStackTrace();
      throw new SolmrException((String)AnagErrors.get("ERR_SMRCOMM_TO_CONNECT"));
    }
    return result;
  }
	
	public DatiEntePrivatoVO[] smrcommGetEntiPrivatiByIdEntePrivatoRange(long[] idEntePrivato,
      int tipoRisultato, EntePrivatoFiltroVO filtro)
    throws SolmrException,Exception
  {
	  DatiEntePrivatoVO[] result=null;
    try
    {
      result = getSmrCommNPClient().smrcommGetEntiPrivatiByIdEntePrivatoRange(idEntePrivato, tipoRisultato, filtro); 
    }
    catch (InvalidParameterException i)
    {
      SolmrLogger.fatal(this, "smrcommGetEntiPrivatiByIdEntePrivatoRange - InvalidParameterException: "+i.getMessage());
      throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER);
    }
    catch(java.io.IOException ioex)
    {
      SolmrLogger.fatal(this, "smrcommGetEntiPrivatiByIdEntePrivatoRange - IOException: "+ioex.getMessage());
      //ioex.printStackTrace();
      throw new SolmrException((String)AnagErrors.get("ERR_SMRCOMM_TO_CONNECT"));
    }
    catch (Exception e)
    {
      SolmrLogger.fatal(this, "smrcommGetEntiPrivatiByIdEntePrivatoRange - Exception: "+e.getMessage());
      //ioex.printStackTrace();
      throw new SolmrException((String)AnagErrors.get("ERR_SMRCOMM_TO_CONNECT"));
    }
    return result;
  }
	
}
