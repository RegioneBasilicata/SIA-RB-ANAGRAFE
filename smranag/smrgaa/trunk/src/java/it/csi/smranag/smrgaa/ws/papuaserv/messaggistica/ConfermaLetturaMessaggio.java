
package it.csi.smranag.smrgaa.ws.papuaserv.messaggistica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for confermaLetturaMessaggio complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="confermaLetturaMessaggio">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idElencoMessaggi" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="codiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "confermaLetturaMessaggio", propOrder = {
    "idElencoMessaggi",
    "codiceFiscale"
})
public class ConfermaLetturaMessaggio {

    protected long idElencoMessaggi;
    protected String codiceFiscale;

    /**
     * Gets the value of the idElencoMessaggi property.
     * 
     */
    public long getIdElencoMessaggi() {
        return idElencoMessaggi;
    }

    /**
     * Sets the value of the idElencoMessaggi property.
     * 
     */
    public void setIdElencoMessaggi(long value) {
        this.idElencoMessaggi = value;
    }

    /**
     * Gets the value of the codiceFiscale property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    /**
     * Sets the value of the codiceFiscale property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceFiscale(String value) {
        this.codiceFiscale = value;
    }

}
