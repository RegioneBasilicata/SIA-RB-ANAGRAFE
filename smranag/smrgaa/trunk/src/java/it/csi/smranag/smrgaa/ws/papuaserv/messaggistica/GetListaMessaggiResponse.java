
package it.csi.smranag.smrgaa.ws.papuaserv.messaggistica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getListaMessaggiResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getListaMessaggiResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="listaMessaggi" type="{http://papuaserv.webservice.business.papuaserv.papua.csi.it/}listaMessaggi" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getListaMessaggiResponse", propOrder = {
    "listaMessaggi"
})
public class GetListaMessaggiResponse {

    protected ListaMessaggi listaMessaggi;

    /**
     * Gets the value of the listaMessaggi property.
     * 
     * @return
     *     possible object is
     *     {@link ListaMessaggi }
     *     
     */
    public ListaMessaggi getListaMessaggi() {
        return listaMessaggi;
    }

    /**
     * Sets the value of the listaMessaggi property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListaMessaggi }
     *     
     */
    public void setListaMessaggi(ListaMessaggi value) {
        this.listaMessaggi = value;
    }

}
