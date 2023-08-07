package it.csi.smranag.smrgaa.ws.gaaserv.marcatemporale;

import it.csi.smranag.smrgaa.ws.papuaserv.messaggistica.Messaggistica;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.1.8
 * 2016-10-20T16:33:41.037+02:00
 * Generated source version: 3.1.8
 * 
 */
@WebServiceClient(name = "ServiceMarcaTemporaleservImplService", 
                  wsdlLocation = "file:/C:/Users/STEFAN~1.PRU/AppData/Local/Temp/tempdir2751705502382625903.tmp/ServiceMarcaTemporaleservImpl_1.wsdl",
                  targetNamespace = "http://service.gaaserv.agricoltura.aizoon.it/") 
public class ServiceMarcaTemporaleservImplService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://service.gaaserv.agricoltura.aizoon.it/", "ServiceMarcaTemporaleservImplService");
    public final static QName ServiceMarcaTemporaleservImplPort = new QName("http://service.gaaserv.agricoltura.aizoon.it/", "ServiceMarcaTemporaleservImplPort");
    static {
        URL url = ServiceMarcaTemporaleservImplService.class.getResource("/ServiceMarcaTemporaleservImpl.wsdl");        
        WSDL_LOCATION = url;        
    }

    public ServiceMarcaTemporaleservImplService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public ServiceMarcaTemporaleservImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ServiceMarcaTemporaleservImplService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    public ServiceMarcaTemporaleservImplService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public ServiceMarcaTemporaleservImplService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public ServiceMarcaTemporaleservImplService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    




    /**
     *
     * @return
     *     returns ServiceMarcaTemporaleserv
     */
    @WebEndpoint(name = "ServiceMarcaTemporaleservImplPort")
    public ServiceMarcaTemporaleserv getServiceMarcaTemporaleservImplPort() {
        return super.getPort(ServiceMarcaTemporaleservImplPort, ServiceMarcaTemporaleserv.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ServiceMarcaTemporaleserv
     */
    @WebEndpoint(name = "ServiceMarcaTemporaleservImplPort")
    public ServiceMarcaTemporaleserv getServiceMarcaTemporaleservImplPort(WebServiceFeature... features) {
        return super.getPort(ServiceMarcaTemporaleservImplPort, ServiceMarcaTemporaleserv.class, features);
    }

}
