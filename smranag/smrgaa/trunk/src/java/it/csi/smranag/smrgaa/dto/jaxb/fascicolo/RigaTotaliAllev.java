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
 *         &lt;element ref="{}TotCapiDetenuti"/>
 *         &lt;element ref="{}TotCapiPropr"/>
 *         &lt;element ref="{}TotAzoto"/>
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
    "totCapiDetenuti",
    "totCapiPropr",
    "totAzoto"
})
@XmlRootElement(name = "RigaTotaliAllev")
public class RigaTotaliAllev {

    @XmlElement(name = "TotCapiDetenuti", required = true)
    protected String totCapiDetenuti;
    @XmlElement(name = "TotCapiPropr", required = true)
    protected String totCapiPropr;
    @XmlElement(name = "TotAzoto", required = true)
    protected String totAzoto;

    /**
     * Gets the value of the totCapiDetenuti property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotCapiDetenuti() {
        return totCapiDetenuti;
    }

    /**
     * Sets the value of the totCapiDetenuti property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotCapiDetenuti(String value) {
        this.totCapiDetenuti = value;
    }

    /**
     * Gets the value of the totCapiPropr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotCapiPropr() {
        return totCapiPropr;
    }

    /**
     * Sets the value of the totCapiPropr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotCapiPropr(String value) {
        this.totCapiPropr = value;
    }

    /**
     * Gets the value of the totAzoto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotAzoto() {
        return totAzoto;
    }

    /**
     * Sets the value of the totAzoto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotAzoto(String value) {
        this.totAzoto = value;
    }

}
