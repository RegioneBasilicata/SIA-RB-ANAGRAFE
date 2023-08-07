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
 *         &lt;element ref="{}Cuaa"/>
 *         &lt;element ref="{}PartitaIva"/>
 *         &lt;element ref="{}Denominazione"/>
 *         &lt;element ref="{}CodiceEnte"/>
 *         &lt;element ref="{}Comune"/>
 *         &lt;element ref="{}Indirizzo"/>
 *         &lt;element ref="{}CAP"/>
 *         &lt;element ref="{}DataIngresso"/>
 *         &lt;element ref="{}DataUscita"/>
 *         &lt;element ref="{}Variazione"/>
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
    "cuaa",
    "partitaIva",
    "denominazione",
    "codiceEnte",
    "comune",
    "indirizzo",
    "cap",
    "dataIngresso",
    "dataUscita",
    "variazione"
})
@XmlRootElement(name = "Associata")
public class Associata {

    @XmlElement(name = "Cuaa", required = true)
    protected String cuaa;
    @XmlElement(name = "PartitaIva", required = true)
    protected String partitaIva;
    @XmlElement(name = "Denominazione", required = true)
    protected String denominazione;
    @XmlElement(name = "CodiceEnte", required = true)
    protected String codiceEnte;
    @XmlElement(name = "Comune", required = true)
    protected String comune;
    @XmlElement(name = "Indirizzo", required = true)
    protected String indirizzo;
    @XmlElement(name = "CAP", required = true)
    protected String cap;
    @XmlElement(name = "DataIngresso", required = true)
    protected String dataIngresso;
    @XmlElement(name = "DataUscita", required = true)
    protected String dataUscita;
    @XmlElement(name = "Variazione", required = true)
    protected String variazione;

    /**
     * Gets the value of the cuaa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCuaa() {
        return cuaa;
    }

    /**
     * Sets the value of the cuaa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCuaa(String value) {
        this.cuaa = value;
    }

    /**
     * Gets the value of the partitaIva property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartitaIva() {
        return partitaIva;
    }

    /**
     * Sets the value of the partitaIva property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartitaIva(String value) {
        this.partitaIva = value;
    }

    /**
     * Gets the value of the denominazione property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenominazione() {
        return denominazione;
    }

    /**
     * Sets the value of the denominazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenominazione(String value) {
        this.denominazione = value;
    }

    /**
     * Gets the value of the codiceEnte property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceEnte() {
        return codiceEnte;
    }

    /**
     * Sets the value of the codiceEnte property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceEnte(String value) {
        this.codiceEnte = value;
    }

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
     * Gets the value of the indirizzo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndirizzo() {
        return indirizzo;
    }

    /**
     * Sets the value of the indirizzo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndirizzo(String value) {
        this.indirizzo = value;
    }

    /**
     * Gets the value of the cap property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCAP() {
        return cap;
    }

    /**
     * Sets the value of the cap property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCAP(String value) {
        this.cap = value;
    }

    /**
     * Gets the value of the dataIngresso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataIngresso() {
        return dataIngresso;
    }

    /**
     * Sets the value of the dataIngresso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataIngresso(String value) {
        this.dataIngresso = value;
    }

    /**
     * Gets the value of the dataUscita property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataUscita() {
        return dataUscita;
    }

    /**
     * Sets the value of the dataUscita property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataUscita(String value) {
        this.dataUscita = value;
    }

    /**
     * Gets the value of the variazione property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVariazione() {
        return variazione;
    }

    /**
     * Sets the value of the variazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVariazione(String value) {
        this.variazione = value;
    }

}
