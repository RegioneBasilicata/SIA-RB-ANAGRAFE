
package it.csi.smranag.smrgaa.ws.papuaserv.messaggistica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getDettagliMessaggioResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getDettagliMessaggioResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dettagliMessaggio" type="{http://papuaserv.webservice.business.papuaserv.papua.csi.it/}dettagliMessaggio" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getDettagliMessaggioResponse", propOrder = {
    "dettagliMessaggio"
})
public class GetDettagliMessaggioResponse {

    protected DettagliMessaggio dettagliMessaggio;

    /**
     * Gets the value of the dettagliMessaggio property.
     * 
     * @return
     *     possible object is
     *     {@link DettagliMessaggio }
     *     
     */
    public DettagliMessaggio getDettagliMessaggio() {
        return dettagliMessaggio;
    }

    /**
     * Sets the value of the dettagliMessaggio property.
     * 
     * @param value
     *     allowed object is
     *     {@link DettagliMessaggio }
     *     
     */
    public void setDettagliMessaggio(DettagliMessaggio value) {
        this.dettagliMessaggio = value;
    }

}
