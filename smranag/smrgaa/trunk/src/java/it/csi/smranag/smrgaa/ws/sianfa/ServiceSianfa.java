package it.csi.smranag.smrgaa.ws.sianfa;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.4.9
 * 2020-01-14T15:41:55.491+01:00
 * Generated source version: 2.4.9
 * 
 */
@WebService(targetNamespace = "http://service.sianfa.agricoltura.aizoon.it/", name = "ServiceSianfa")
@XmlSeeAlso({ObjectFactory.class})
public interface ServiceSianfa {

    @WebMethod
    @RequestWrapper(localName = "isAlive", targetNamespace = "http://service.sianfa.agricoltura.aizoon.it/", className = "it.csi.smranag.smrgaa.ws.sianfa.IsAlive")
    @ResponseWrapper(localName = "isAliveResponse", targetNamespace = "http://service.sianfa.agricoltura.aizoon.it/", className = "it.csi.smranag.smrgaa.ws.sianfa.IsAliveResponse")
    @WebResult(name = "return", targetNamespace = "")
    public boolean isAlive() throws Exception_Exception;

    @WebMethod
    @RequestWrapper(localName = "testResources", targetNamespace = "http://service.sianfa.agricoltura.aizoon.it/", className = "it.csi.smranag.smrgaa.ws.sianfa.TestResources")
    @ResponseWrapper(localName = "testResourcesResponse", targetNamespace = "http://service.sianfa.agricoltura.aizoon.it/", className = "it.csi.smranag.smrgaa.ws.sianfa.TestResourcesResponse")
    @WebResult(name = "return", targetNamespace = "")
    public boolean testResources() throws Exception_Exception;

    @WebMethod
    @RequestWrapper(localName = "aggiornaFascicoloAziendale", targetNamespace = "http://service.sianfa.agricoltura.aizoon.it/", className = "it.csi.smranag.smrgaa.ws.sianfa.AggiornaFascicoloAziendale")
    @ResponseWrapper(localName = "aggiornaFascicoloAziendaleResponse", targetNamespace = "http://service.sianfa.agricoltura.aizoon.it/", className = "it.csi.smranag.smrgaa.ws.sianfa.AggiornaFascicoloAziendaleResponse")
    @WebResult(name = "return", targetNamespace = "")
    public boolean aggiornaFascicoloAziendale(
        @WebParam(name = "arg0", targetNamespace = "")
        java.lang.String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        java.lang.Long arg1
    ) throws Exception_Exception;

    @WebMethod
    @RequestWrapper(localName = "getAggiornamentiFascicolo", targetNamespace = "http://service.sianfa.agricoltura.aizoon.it/", className = "it.csi.smranag.smrgaa.ws.sianfa.GetAggiornamentiFascicolo")
    @ResponseWrapper(localName = "getAggiornamentiFascicoloResponse", targetNamespace = "http://service.sianfa.agricoltura.aizoon.it/", className = "it.csi.smranag.smrgaa.ws.sianfa.GetAggiornamentiFascicoloResponse")
    @WebResult(name = "return", targetNamespace = "")
    public it.csi.smranag.smrgaa.ws.sianfa.SianEsito getAggiornamentiFascicolo(
        @WebParam(name = "arg0", targetNamespace = "")
        java.lang.String arg0
    ) throws Exception_Exception;
}
