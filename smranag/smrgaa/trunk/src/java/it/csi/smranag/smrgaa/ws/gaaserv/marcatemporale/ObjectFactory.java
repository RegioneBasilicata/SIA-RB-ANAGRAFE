
package it.csi.smranag.smrgaa.ws.gaaserv.marcatemporale;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.csi.smranag.smrgaa.ws.gaaserv.marcatemporale package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetMarcaTemporale_QNAME = new QName("http://service.gaaserv.agricoltura.aizoon.it/", "getMarcaTemporale");
    private final static QName _GetMarcaTemporaleResponse_QNAME = new QName("http://service.gaaserv.agricoltura.aizoon.it/", "getMarcaTemporaleResponse");
    private final static QName _IsAlive_QNAME = new QName("http://service.gaaserv.agricoltura.aizoon.it/", "isAlive");
    private final static QName _IsAliveResponse_QNAME = new QName("http://service.gaaserv.agricoltura.aizoon.it/", "isAliveResponse");
    private final static QName _TestResources_QNAME = new QName("http://service.gaaserv.agricoltura.aizoon.it/", "testResources");
    private final static QName _TestResourcesResponse_QNAME = new QName("http://service.gaaserv.agricoltura.aizoon.it/", "testResourcesResponse");
    private final static QName _Exception_QNAME = new QName("http://service.gaaserv.agricoltura.aizoon.it/", "Exception");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.csi.smranag.smrgaa.ws.gaaserv.marcatemporale
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetMarcaTemporale }
     * 
     */
    public GetMarcaTemporale createGetMarcaTemporale() {
        return new GetMarcaTemporale();
    }

    /**
     * Create an instance of {@link GetMarcaTemporaleResponse }
     * 
     */
    public GetMarcaTemporaleResponse createGetMarcaTemporaleResponse() {
        return new GetMarcaTemporaleResponse();
    }

    /**
     * Create an instance of {@link IsAlive }
     * 
     */
    public IsAlive createIsAlive() {
        return new IsAlive();
    }

    /**
     * Create an instance of {@link IsAliveResponse }
     * 
     */
    public IsAliveResponse createIsAliveResponse() {
        return new IsAliveResponse();
    }

    /**
     * Create an instance of {@link TestResources }
     * 
     */
    public TestResources createTestResources() {
        return new TestResources();
    }

    /**
     * Create an instance of {@link TestResourcesResponse }
     * 
     */
    public TestResourcesResponse createTestResourcesResponse() {
        return new TestResourcesResponse();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMarcaTemporale }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.gaaserv.agricoltura.aizoon.it/", name = "getMarcaTemporale")
    public JAXBElement<GetMarcaTemporale> createGetMarcaTemporale(GetMarcaTemporale value) {
        return new JAXBElement<GetMarcaTemporale>(_GetMarcaTemporale_QNAME, GetMarcaTemporale.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMarcaTemporaleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.gaaserv.agricoltura.aizoon.it/", name = "getMarcaTemporaleResponse")
    public JAXBElement<GetMarcaTemporaleResponse> createGetMarcaTemporaleResponse(GetMarcaTemporaleResponse value) {
        return new JAXBElement<GetMarcaTemporaleResponse>(_GetMarcaTemporaleResponse_QNAME, GetMarcaTemporaleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsAlive }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.gaaserv.agricoltura.aizoon.it/", name = "isAlive")
    public JAXBElement<IsAlive> createIsAlive(IsAlive value) {
        return new JAXBElement<IsAlive>(_IsAlive_QNAME, IsAlive.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsAliveResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.gaaserv.agricoltura.aizoon.it/", name = "isAliveResponse")
    public JAXBElement<IsAliveResponse> createIsAliveResponse(IsAliveResponse value) {
        return new JAXBElement<IsAliveResponse>(_IsAliveResponse_QNAME, IsAliveResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestResources }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.gaaserv.agricoltura.aizoon.it/", name = "testResources")
    public JAXBElement<TestResources> createTestResources(TestResources value) {
        return new JAXBElement<TestResources>(_TestResources_QNAME, TestResources.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestResourcesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.gaaserv.agricoltura.aizoon.it/", name = "testResourcesResponse")
    public JAXBElement<TestResourcesResponse> createTestResourcesResponse(TestResourcesResponse value) {
        return new JAXBElement<TestResourcesResponse>(_TestResourcesResponse_QNAME, TestResourcesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.gaaserv.agricoltura.aizoon.it/", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<Exception>(_Exception_QNAME, Exception.class, null, value);
    }

}
