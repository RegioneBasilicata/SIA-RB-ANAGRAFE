//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-661 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.03.02 at 04:07:28 PM CET 
//


package it.csi.smranag.smrgaa.dto.jaxb.fascicolo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}SezioneVuota"/>
 *         &lt;element ref="{}Visibility"/>
 *         &lt;element ref="{}TitoloFabbricati"/>
 *         &lt;element ref="{}Fabbricati"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "sezioneVuota",
    "visibility",
    "titoloFabbricati",
    "fabbricati"
})
@XmlRootElement(name = "QuadroFabbricati")
public class QuadroFabbricati {

    @XmlElement(name = "SezioneVuota")
    protected boolean sezioneVuota;
    @XmlElement(name = "Visibility")
    protected boolean visibility;
    @XmlElement(name = "TitoloFabbricati", required = true)
    protected String titoloFabbricati;
    @XmlElement(name = "Fabbricati", required = true)
    protected Fabbricati fabbricati;

    /**
     * Gets the value of the sezioneVuota property.
     * 
     */
    public boolean isSezioneVuota() {
        return sezioneVuota;
    }

    /**
     * Sets the value of the sezioneVuota property.
     * 
     */
    public void setSezioneVuota(boolean value) {
        this.sezioneVuota = value;
    }

    /**
     * Gets the value of the visibility property.
     * 
     */
    public boolean isVisibility() {
        return visibility;
    }

    /**
     * Sets the value of the visibility property.
     * 
     */
    public void setVisibility(boolean value) {
        this.visibility = value;
    }

    /**
     * Gets the value of the titoloFabbricati property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitoloFabbricati() {
        return titoloFabbricati;
    }

    /**
     * Sets the value of the titoloFabbricati property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitoloFabbricati(String value) {
        this.titoloFabbricati = value;
    }

    /**
     * Gets the value of the fabbricati property.
     * 
     * @return
     *     possible object is
     *     {@link Fabbricati }
     *     
     */
    public Fabbricati getFabbricati() {
        return fabbricati;
    }

    /**
     * Sets the value of the fabbricati property.
     * 
     * @param value
     *     allowed object is
     *     {@link Fabbricati }
     *     
     */
    public void setFabbricati(Fabbricati value) {
        this.fabbricati = value;
    }

}