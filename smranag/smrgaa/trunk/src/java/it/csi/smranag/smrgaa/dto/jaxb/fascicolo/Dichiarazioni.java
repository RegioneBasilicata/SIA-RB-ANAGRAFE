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
 *         &lt;element ref="{}Visibility"/>
 *         &lt;element ref="{}PrimoBlocco"/>
 *         &lt;element ref="{}BloccoConCheck"/>
 *         &lt;element ref="{}SecondoBlocco"/>
 *         &lt;element ref="{}Firma"/>
 *         &lt;element ref="{}Firma2"/>
 *         &lt;element ref="{}BloccoFinale"/>
 *         &lt;element ref="{}BloccoConCheck2"/>
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
    "visibility",
    "primoBlocco",
    "bloccoConCheck",
    "secondoBlocco",
    "firma",
    "firma2",
    "bloccoFinale",
    "bloccoConCheck2"
})
@XmlRootElement(name = "Dichiarazioni")
public class Dichiarazioni {

    @XmlElement(name = "Visibility")
    protected boolean visibility;
    @XmlElement(name = "PrimoBlocco", required = true)
    protected PrimoBlocco primoBlocco;
    @XmlElement(name = "BloccoConCheck", required = true)
    protected BloccoConCheck bloccoConCheck;
    @XmlElement(name = "SecondoBlocco", required = true)
    protected SecondoBlocco secondoBlocco;
    @XmlElement(name = "Firma", required = true)
    protected Firma firma;
    @XmlElement(name = "Firma2", required = true)
    protected Firma2 firma2;
    @XmlElement(name = "BloccoFinale", required = true)
    protected BloccoFinale bloccoFinale;
    @XmlElement(name = "BloccoConCheck2", required = true)
    protected BloccoConCheck2 bloccoConCheck2;

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
     * Gets the value of the primoBlocco property.
     * 
     * @return
     *     possible object is
     *     {@link PrimoBlocco }
     *     
     */
    public PrimoBlocco getPrimoBlocco() {
        return primoBlocco;
    }

    /**
     * Sets the value of the primoBlocco property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrimoBlocco }
     *     
     */
    public void setPrimoBlocco(PrimoBlocco value) {
        this.primoBlocco = value;
    }

    /**
     * Gets the value of the bloccoConCheck property.
     * 
     * @return
     *     possible object is
     *     {@link BloccoConCheck }
     *     
     */
    public BloccoConCheck getBloccoConCheck() {
        return bloccoConCheck;
    }

    /**
     * Sets the value of the bloccoConCheck property.
     * 
     * @param value
     *     allowed object is
     *     {@link BloccoConCheck }
     *     
     */
    public void setBloccoConCheck(BloccoConCheck value) {
        this.bloccoConCheck = value;
    }

    /**
     * Gets the value of the secondoBlocco property.
     * 
     * @return
     *     possible object is
     *     {@link SecondoBlocco }
     *     
     */
    public SecondoBlocco getSecondoBlocco() {
        return secondoBlocco;
    }

    /**
     * Sets the value of the secondoBlocco property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecondoBlocco }
     *     
     */
    public void setSecondoBlocco(SecondoBlocco value) {
        this.secondoBlocco = value;
    }

    /**
     * Gets the value of the firma property.
     * 
     * @return
     *     possible object is
     *     {@link Firma }
     *     
     */
    public Firma getFirma() {
        return firma;
    }

    /**
     * Sets the value of the firma property.
     * 
     * @param value
     *     allowed object is
     *     {@link Firma }
     *     
     */
    public void setFirma(Firma value) {
        this.firma = value;
    }

    /**
     * Gets the value of the firma2 property.
     * 
     * @return
     *     possible object is
     *     {@link Firma2 }
     *     
     */
    public Firma2 getFirma2() {
        return firma2;
    }

    /**
     * Sets the value of the firma2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Firma2 }
     *     
     */
    public void setFirma2(Firma2 value) {
        this.firma2 = value;
    }

    /**
     * Gets the value of the bloccoFinale property.
     * 
     * @return
     *     possible object is
     *     {@link BloccoFinale }
     *     
     */
    public BloccoFinale getBloccoFinale() {
        return bloccoFinale;
    }

    /**
     * Sets the value of the bloccoFinale property.
     * 
     * @param value
     *     allowed object is
     *     {@link BloccoFinale }
     *     
     */
    public void setBloccoFinale(BloccoFinale value) {
        this.bloccoFinale = value;
    }

    /**
     * Gets the value of the bloccoConCheck2 property.
     * 
     * @return
     *     possible object is
     *     {@link BloccoConCheck2 }
     *     
     */
    public BloccoConCheck2 getBloccoConCheck2() {
        return bloccoConCheck2;
    }

    /**
     * Sets the value of the bloccoConCheck2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link BloccoConCheck2 }
     *     
     */
    public void setBloccoConCheck2(BloccoConCheck2 value) {
        this.bloccoConCheck2 = value;
    }

}
