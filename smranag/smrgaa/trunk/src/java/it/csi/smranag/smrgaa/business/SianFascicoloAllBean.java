/**
 * 
 */
package it.csi.smranag.smrgaa.business;

import it.csi.csi.wrapper.SystemException;
import it.csi.smranag.smrgaa.ws.sianfa.ServiceSianfa;
import it.csi.smranag.smrgaa.ws.sianfa.ServiceSianfaImplService;
import it.csi.smranag.smrgaa.ws.sianfa.SianEsito;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.SolmrLogger;

import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.xml.ws.BindingProvider;



@Stateless(name=SianFascicoloAllBean.jndiName,mappedName=SianFascicoloAllBean.jndiName)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SianFascicoloAllBean implements SianFascicoloAllLocal {	
	public static ServiceSianfa portStub = null;

	static {

		try {
		ResourceBundle res = ResourceBundle.getBundle("config");
		String endPointUrl = res.getString("sianFascicoloAll_endpoint_url");

		SolmrLogger.debug("SianFascicoloAllBean","-- endPointUrl sianfa =" + endPointUrl);

		ServiceSianfaImplService ss = new ServiceSianfaImplService();
		portStub = ss.getServiceSianfaImplPort();

		BindingProvider bp = (BindingProvider) portStub;

		Map<String, Object> context = bp.getRequestContext();
		context.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPointUrl);
		} catch (Throwable t) {
			t.printStackTrace();
			throw t;
		}
	}
	
	public final static String jndiName = "comp/env/solmr/gaa/SianFascicoloAll";

	SessionContext                   sessionContext;

	
	@Resource
	public void setSessionContext(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
	}
		
	
	@Override
	public SianEsito getAggiornamentiFascicolo(String cuaa) throws SolmrException {
		SolmrLogger.info(this, "BEGIN getAggiornamentiFascicolo");
		SianEsito esitoAggiornamentoFascicolo = null;
		try {			
			SolmrLogger.debug(this,"-- chiamata al servizio SIANFA getAggiornamentiFascicolo con cuaa in input ="+cuaa);			
			esitoAggiornamentoFascicolo = portStub.getAggiornamentiFascicolo(cuaa);			
		} 
		catch (Exception ex) {
		  SolmrLogger.error(this, "Exception durante la chiamata al servizio SIANFA getAggiornamentiFascicolo ="+ex.getMessage());
		  throw new SolmrException(ex.getMessage());
		}
		finally{
		  SolmrLogger.info(this, "END getAggiornamentiFascicolo");
		}
		return esitoAggiornamentoFascicolo;		
	}
	
	@Override
	public Boolean aggiornaFascicoloAziendale(String cuaa, Long idUtente) throws SolmrException {
		SolmrLogger.info(this, "BEGIN aggiornaFascicoloAziendale");		
		try {			
			SolmrLogger.debug(this,"-- chiamata al servizio SIANFA getAggiornamentiFascicolo con cuaa in input ="+cuaa);			
			return new Boolean(portStub.aggiornaFascicoloAziendale(cuaa, idUtente));			
		} 
		catch (Exception ex) {
		  SolmrLogger.error(this, "Exception durante la chiamata al servizio SIANFA aggiornaFascicoloAziendale ="+ex.getMessage());		  
		  throw new SolmrException(ex.getMessage());
		}
		finally{
		  SolmrLogger.info(this, "END aggiornaFascicoloAziendale");
		}		
	}
	
	
	private SolmrException checkServicesUnavailable(SystemException ex)
			throws SolmrException {
		if (SolmrConstants.NAME_NOT_FOUND_EXCEPTION.equals(ex
				.getNestedExcClassName())
				|| SolmrConstants.COMMUNICATION_EXCEPTION.equals(ex
						.getNestedExcClassName())) {
			return new SolmrException(
					AnagErrors.ERRORE_SERVIZIO_MARCATURA_TEMPORALE_NON_DISPONIBILE, 1);
		} else {
			throw new SolmrException(ex.getMessage());
		}
	}

	
}
