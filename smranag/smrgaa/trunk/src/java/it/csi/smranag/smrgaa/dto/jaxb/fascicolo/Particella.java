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
 *         &lt;element ref="{}Comune"/>
 *         &lt;element ref="{}Sezione"/>
 *         &lt;element ref="{}Foglio"/>
 *         &lt;element ref="{}Part"/>
 *         &lt;element ref="{}Sub"/>
 *         &lt;element ref="{}SupCatastale"/>
 *         &lt;element ref="{}Cond"/>
 *         &lt;element ref="{}Uso"/>
 *         &lt;element ref="{}UsoSec"/>
 *         &lt;element ref="{}SupUtil"/>
 *         &lt;element ref="{}SupUtilSec"/>
 *         &lt;element ref="{}SupAgron"/>
 *         &lt;element ref="{}SupGis"/>
 *         &lt;element ref="{}CicloOrtivo"/>
 *         &lt;element ref="{}Irrigua"/>
 *         &lt;element ref="{}Vincoli"/>
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
    "comune",
    "sezione",
    "foglio",
    "part",
    "sub",
    "supCatastale",
    "cond",
    "uso",
    "usoSec",
    "supUtil",
    "supUtilSec",
    "supAgron",
    "supGis",
    "cicloOrtivo",
    "irrigua",
    "vincoli"
})
@XmlRootElement(name = "Particella")
public class Particella {

    @XmlElement(name = "Comune", required = true)
    protected String comune;
    @XmlElement(name = "Sezione", required = true)
    protected String sezione;
    @XmlElement(name = "Foglio", required = true)
    protected String foglio;
    @XmlElement(name = "Part", required = true)
    protected String part;
    @XmlElement(name = "Sub", required = true)
    protected String sub;
    @XmlElement(name = "SupCatastale", required = true)
    protected String supCatastale;
    @XmlElement(name = "Cond", required = true)
    protected String cond;
    @XmlElement(name = "Uso", required = true)
    protected String uso;
    @XmlElement(name = "UsoSec", required = true)
    protected String usoSec;
    @XmlElement(name = "SupUtil", required = true)
    protected String supUtil;
    @XmlElement(name = "SupUtilSec", required = true)
    protected String supUtilSec;
    @XmlElement(name = "SupAgron", required = true)
    protected String supAgron;
    @XmlElement(name = "SupGis", required = true)
    protected String supGis;
    @XmlElement(name = "CicloOrtivo", required = true)
    protected String cicloOrtivo;
    @XmlElement(name = "Irrigua", required = true)
    protected String irrigua;
    @XmlElement(name = "Vincoli", required = true)
    protected String vincoli;

    /**
     * Gets the value of the comune property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComune() {
        return comune;
    }

    /**
     * Sets the value of the comune property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComune(String value) {
        this.comune = value;
    }

    /**
     * Gets the value of the sezione property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSezione() {
        return sezione;
    }

    /**
     * Sets the value of the sezione property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSezione(String value) {
        this.sezione = value;
    }

    /**
     * Gets the value of the foglio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFoglio() {
        return foglio;
    }

    /**
     * Sets the value of the foglio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFoglio(String value) {
        this.foglio = value;
    }

    /**
     * Gets the value of the part property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPart() {
        return part;
    }

    /**
     * Sets the value of the part property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPart(String value) {
        this.part = value;
    }

    /**
     * Gets the value of the sub property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSub() {
        return sub;
    }

    /**
     * Sets the value of the sub property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSub(String value) {
        this.sub = value;
    }

    /**
     * Gets the value of the supCatastale property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSupCatastale() {
        return supCatastale;
    }

    /**
     * Sets the value of the supCatastale property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSupCatastale(String value) {
        this.supCatastale = value;
    }

    /**
     * Gets the value of the cond property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCond() {
        return cond;
    }

    /**
     * Sets the value of the cond property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCond(String value) {
        this.cond = value;
    }

    /**
     * Gets the value of the uso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUso() {
        return uso;
    }

    /**
     * Sets the value of the uso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUso(String value) {
        this.uso = value;
    }

    /**
     * Gets the value of the usoSec property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsoSec() {
        return usoSec;
    }

    /**
     * Sets the value of the usoSec property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsoSec(String value) {
        this.usoSec = value;
    }

    /**
     * Gets the value of the supUtil property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSupUtil() {
        return supUtil;
    }

    /**
     * Sets the value of the supUtil property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSupUtil(String value) {
        this.supUtil = value;
    }

    /**
     * Gets the value of the supUtilSec property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSupUtilSec() {
        return supUtilSec;
    }

    /**
     * Sets the value of the supUtilSec property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSupUtilSec(String value) {
        this.supUtilSec = value;
    }

    /**
     * Gets the value of the supAgron property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSupAgron() {
        return supAgron;
    }

    /**
     * Sets the value of the supAgron property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSupAgron(String value) {
        this.supAgron = value;
    }

    /**
     * Gets the value of the supGis property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSupGis() {
        return supGis;
    }

    /**
     * Sets the value of the supGis property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSupGis(String value) {
        this.supGis = value;
    }

    /**
     * Gets the value of the cicloOrtivo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCicloOrtivo() {
        return cicloOrtivo;
    }

    /**
     * Sets the value of the cicloOrtivo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCicloOrtivo(String value) {
        this.cicloOrtivo = value;
    }

    /**
     * Gets the value of the irrigua property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIrrigua() {
        return irrigua;
    }

    /**
     * Sets the value of the irrigua property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIrrigua(String value) {
        this.irrigua = value;
    }

    /**
     * Gets the value of the vincoli property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVincoli() {
        return vincoli;
    }

    /**
     * Sets the value of the vincoli property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVincoli(String value) {
        this.vincoli = value;
    }

}
