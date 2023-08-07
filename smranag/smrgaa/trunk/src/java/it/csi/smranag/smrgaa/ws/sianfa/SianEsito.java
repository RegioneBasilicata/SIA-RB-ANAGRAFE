
package it.csi.smranag.smrgaa.ws.sianfa;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sianEsito complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sianEsito">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="esitiAggFascicolo" type="{http://service.sianfa.agricoltura.aizoon.it/}sianEsitoAggFascicolo" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="esito" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sianEsito", propOrder = {
    "esitiAggFascicolo",
    "esito"
})
public class SianEsito {

    @XmlElement(nillable = true)
    protected List<SianEsitoAggFascicolo> esitiAggFascicolo;
    protected String esito;

    /**
     * Gets the value of the esitiAggFascicolo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the esitiAggFascicolo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEsitiAggFascicolo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SianEsitoAggFascicolo }
     * 
     * 
     */
    public List<SianEsitoAggFascicolo> getEsitiAggFascicolo() {
        if (esitiAggFascicolo == null) {
            esitiAggFascicolo = new ArrayList<SianEsitoAggFascicolo>();
        }
        return this.esitiAggFascicolo;
    }

    /**
     * Gets the value of the esito property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEsito() {
        return esito;
    }

    /**
     * Sets the value of the esito property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEsito(String value) {
        this.esito = value;
    }

}
