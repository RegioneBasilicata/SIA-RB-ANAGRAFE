
package it.csi.smranag.smrgaa.ws.papuaserv.messaggistica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getAllegatoResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getAllegatoResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="allegato" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAllegatoResponse", propOrder = {
    "allegato"
})
public class GetAllegatoResponse {

    protected byte[] allegato;

    /**
     * Gets the value of the allegato property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getAllegato() {
        return allegato;
    }

    /**
     * Sets the value of the allegato property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setAllegato(byte[] value) {
        this.allegato = value;
    }

}
