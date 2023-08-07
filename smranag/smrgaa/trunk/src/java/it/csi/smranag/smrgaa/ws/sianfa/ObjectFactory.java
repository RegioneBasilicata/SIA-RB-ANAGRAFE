
package it.csi.smranag.smrgaa.ws.sianfa;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.csi.smranag.smrgaa.ws.sianfa package. 
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

    private final static QName _IsAlive_QNAME = new QName("http://service.sianfa.agricoltura.aizoon.it/", "isAlive");
    private final static QName _TestResourcesResponse_QNAME = new QName("http://service.sianfa.agricoltura.aizoon.it/", "testResourcesResponse");
    private final static QName _TestResources_QNAME = new QName("http://service.sianfa.agricoltura.aizoon.it/", "testResources");
    private final static QName _AggiornaFascicoloAziendale_QNAME = new QName("http://service.sianfa.agricoltura.aizoon.it/", "aggiornaFascicoloAziendale");
    private final static QName _IsAliveResponse_QNAME = new QName("http://service.sianfa.agricoltura.aizoon.it/", "isAliveResponse");
    private final static QName _AggiornaFascicoloAziendaleResponse_QNAME = new QName("http://service.sianfa.agricoltura.aizoon.it/", "aggiornaFascicoloAziendaleResponse");
    private final static QName _GetAggiornamentiFascicoloResponse_QNAME = new QName("http://service.sianfa.agricoltura.aizoon.it/", "getAggiornamentiFascicoloResponse");
    private final static QName _Exception_QNAME = new QName("http://service.sianfa.agricoltura.aizoon.it/", "Exception");
    private final static QName _GetAggiornamentiFascicolo_QNAME = new QName("http://service.sianfa.agricoltura.aizoon.it/", "getAggiornamentiFascicolo");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.csi.smranag.smrgaa.ws.sianfa
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link IsAlive }
     * 
     */
    public IsAlive createIsAlive() {
        return new IsAlive();
    }

    /**
     * Create an instance of {@link TestResourcesResponse }
     * 
     */
    public TestResourcesResponse createTestResourcesResponse() {
        return new TestResourcesResponse();
    }

    /**
     * Create an instance of {@link TestResources }
     * 
     */
    public TestResources createTestResources() {
        return new TestResources();
    }

    /**
     * Create an instance of {@link AggiornaFascicoloAziendale }
     * 
     */
    public AggiornaFascicoloAziendale createAggiornaFascicoloAziendale() {
        return new AggiornaFascicoloAziendale();
    }

    /**
     * Create an instance of {@link AggiornaFascicoloAziendaleResponse }
     * 
     */
    public AggiornaFascicoloAziendaleResponse createAggiornaFascicoloAziendaleResponse() {
        return new AggiornaFascicoloAziendaleResponse();
    }

    /**
     * Create an instance of {@link IsAliveResponse }
     * 
     */
    public IsAliveResponse createIsAliveResponse() {
        return new IsAliveResponse();
    }

    /**
     * Create an instance of {@link GetAggiornamentiFascicolo }
     * 
     */
    public GetAggiornamentiFascicolo createGetAggiornamentiFascicolo() {
        return new GetAggiornamentiFascicolo();
    }

    /**
     * Create an instance of {@link GetAggiornamentiFascicoloResponse }
     * 
     */
    public GetAggiornamentiFascicoloResponse createGetAggiornamentiFascicoloResponse() {
        return new GetAggiornamentiFascicoloResponse();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link SianEsitoAggFascicolo }
     * 
     */
    public SianEsitoAggFascicolo createSianEsitoAggFascicolo() {
        return new SianEsitoAggFascicolo();
    }

    /**
     * Create an instance of {@link SianEsito }
     * 
     */
    public SianEsito createSianEsito() {
        return new SianEsito();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsAlive }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sianfa.agricoltura.aizoon.it/", name = "isAlive")
    public JAXBElement<IsAlive> createIsAlive(IsAlive value) {
        return new JAXBElement<IsAlive>(_IsAlive_QNAME, IsAlive.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestResourcesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sianfa.agricoltura.aizoon.it/", name = "testResourcesResponse")
    public JAXBElement<TestResourcesResponse> createTestResourcesResponse(TestResourcesResponse value) {
        return new JAXBElement<TestResourcesResponse>(_TestResourcesResponse_QNAME, TestResourcesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestResources }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sianfa.agricoltura.aizoon.it/", name = "testResources")
    public JAXBElement<TestResources> createTestResources(TestResources value) {
        return new JAXBElement<TestResources>(_TestResources_QNAME, TestResources.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AggiornaFascicoloAziendale }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sianfa.agricoltura.aizoon.it/", name = "aggiornaFascicoloAziendale")
    public JAXBElement<AggiornaFascicoloAziendale> createAggiornaFascicoloAziendale(AggiornaFascicoloAziendale value) {
        return new JAXBElement<AggiornaFascicoloAziendale>(_AggiornaFascicoloAziendale_QNAME, AggiornaFascicoloAziendale.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsAliveResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sianfa.agricoltura.aizoon.it/", name = "isAliveResponse")
    public JAXBElement<IsAliveResponse> createIsAliveResponse(IsAliveResponse value) {
        return new JAXBElement<IsAliveResponse>(_IsAliveResponse_QNAME, IsAliveResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AggiornaFascicoloAziendaleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sianfa.agricoltura.aizoon.it/", name = "aggiornaFascicoloAziendaleResponse")
    public JAXBElement<AggiornaFascicoloAziendaleResponse> createAggiornaFascicoloAziendaleResponse(AggiornaFascicoloAziendaleResponse value) {
        return new JAXBElement<AggiornaFascicoloAziendaleResponse>(_AggiornaFascicoloAziendaleResponse_QNAME, AggiornaFascicoloAziendaleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAggiornamentiFascicoloResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sianfa.agricoltura.aizoon.it/", name = "getAggiornamentiFascicoloResponse")
    public JAXBElement<GetAggiornamentiFascicoloResponse> createGetAggiornamentiFascicoloResponse(GetAggiornamentiFascicoloResponse value) {
        return new JAXBElement<GetAggiornamentiFascicoloResponse>(_GetAggiornamentiFascicoloResponse_QNAME, GetAggiornamentiFascicoloResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sianfa.agricoltura.aizoon.it/", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<Exception>(_Exception_QNAME, Exception.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAggiornamentiFascicolo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sianfa.agricoltura.aizoon.it/", name = "getAggiornamentiFascicolo")
    public JAXBElement<GetAggiornamentiFascicolo> createGetAggiornamentiFascicolo(GetAggiornamentiFascicolo value) {
        return new JAXBElement<GetAggiornamentiFascicolo>(_GetAggiornamentiFascicolo_QNAME, GetAggiornamentiFascicolo.class, null, value);
    }

}
