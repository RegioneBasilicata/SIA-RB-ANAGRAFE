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
 *         &lt;element ref="{}TitoloUnitaVitate"/>
 *         &lt;element ref="{}RiepiloghiUv"/>
 *         &lt;element ref="{}UnitaVitate"/>
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
    "titoloUnitaVitate",
    "riepiloghiUv",
    "unitaVitate"
})
@XmlRootElement(name = "QuadroUnitaVitate")
public class QuadroUnitaVitate {

    @XmlElement(name = "TitoloUnitaVitate", required = true)
    protected String titoloUnitaVitate;
    @XmlElement(name = "RiepiloghiUv", required = true)
    protected RiepiloghiUv riepiloghiUv;
    @XmlElement(name = "UnitaVitate", required = true)
    protected UnitaVitate unitaVitate;

    /**
     * Gets the value of the titoloUnitaVitate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitoloUnitaVitate() {
        return titoloUnitaVitate;
    }

    /**
     * Sets the value of the titoloUnitaVitate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitoloUnitaVitate(String value) {
        this.titoloUnitaVitate = value;
    }

    /**
     * Gets the value of the riepiloghiUv property.
     * 
     * @return
     *     possible object is
     *     {@link RiepiloghiUv }
     *     
     */
    public RiepiloghiUv getRiepiloghiUv() {
        return riepiloghiUv;
    }

    /**
     * Sets the value of the riepiloghiUv property.
     * 
     * @param value
     *     allowed object is
     *     {@link RiepiloghiUv }
     *     
     */
    public void setRiepiloghiUv(RiepiloghiUv value) {
        this.riepiloghiUv = value;
    }

    /**
     * Gets the value of the unitaVitate property.
     * 
     * @return
     *     possible object is
     *     {@link UnitaVitate }
     *     
     */
    public UnitaVitate getUnitaVitate() {
        return unitaVitate;
    }

    /**
     * Sets the value of the unitaVitate property.
     * 
     * @param value
     *     allowed object is
     *     {@link UnitaVitate }
     *     
     */
    public void setUnitaVitate(UnitaVitate value) {
        this.unitaVitate = value;
    }

}
