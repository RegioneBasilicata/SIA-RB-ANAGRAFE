/**
 * 
 */
package it.csi.smranag.smrgaa.business;

import it.csi.csi.wrapper.SystemException;
import it.csi.smranag.smrgaa.ws.gaaserv.marcatemporale.Exception_Exception;
import it.csi.smranag.smrgaa.ws.gaaserv.marcatemporale.GetMarcaTemporaleResponse;
import it.csi.smranag.smrgaa.ws.gaaserv.marcatemporale.ObjectFactory;
import it.csi.smranag.smrgaa.ws.gaaserv.marcatemporale.ServiceMarcaTemporaleserv;
import it.csi.smranag.smrgaa.ws.gaaserv.marcatemporale.ServiceMarcaTemporaleservImplService;
import it.csi.smranag.smrgaa.ws.papuaserv.messaggistica.IMessaggisticaWS;
import it.csi.smranag.smrgaa.ws.papuaserv.messaggistica.Messaggistica;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.SolmrLogger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

/**
 * @author Stefania Prudente
 *
 * 
 */

@Stateless(name=MarcaTemporaleGaaBean.jndiName,mappedName=MarcaTemporaleGaaBean.jndiName)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class MarcaTemporaleGaaBean implements MarcaTemporaleGaaLocal {
	
	public static ServiceMarcaTemporaleserv portStub = null;

	static {

		ResourceBundle res = ResourceBundle.getBundle("config");
		String endPointUrl = res.getString("marcaTemporaleService_endpoint_url");

		SolmrLogger.debug("MarcaTemporaleGaaBean",
				"-- endPointUrl marcaTemporale =" + endPointUrl);

		ServiceMarcaTemporaleservImplService ss = new ServiceMarcaTemporaleservImplService();
		portStub = ss.getServiceMarcaTemporaleservImplPort();

		BindingProvider bp = (BindingProvider) portStub;

		Map<String, Object> context = bp.getRequestContext();
		context.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPointUrl);

	}
	
	public final static String jndiName = "comp/env/solmr/gaa/MarcaTemporale";

	SessionContext                   sessionContext;

	
	@Resource
	public void setSessionContext(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
	}
		
	
	@Override
	public byte[] getMarcaTemporale(byte[] fileToMark) throws SolmrException {
		byte[] fileMarked = null;
		try {
			SolmrLogger.debug("MarcaTemporaleGaaBean",
					"-- servizio marca temporale ");
			
			fileMarked = portStub.getMarcaTemporale(fileToMark);
		} catch (Exception ex) {
			throw new SolmrException(ex.getMessage());
		}
		return fileMarked;		
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
