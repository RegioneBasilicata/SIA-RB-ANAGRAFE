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
 *         &lt;element ref="{}ruolo"/>
 *         &lt;element ref="{}DataInizioRuolo"/>
 *         &lt;element ref="{}DataFineRuolo"/>
 *         &lt;element ref="{}CodiceFiscale"/>
 *         &lt;element ref="{}CognomeNome"/>
 *         &lt;element ref="{}Comune"/>
 *         &lt;element ref="{}Indirizzo"/>
 *         &lt;element ref="{}CAP"/>
 *         &lt;element ref="{}Tel"/>
 *         &lt;element ref="{}email"/>
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
    "ruolo",
    "dataInizioRuolo",
    "dataFineRuolo",
    "codiceFiscale",
    "cognomeNome",
    "comune",
    "indirizzo",
    "cap",
    "tel",
    "email",
    "variazione"
})
@XmlRootElement(name = "Soggetto")
public class Soggetto {

    @XmlElement(required = true)
    protected String ruolo;
    @XmlElement(name = "DataInizioRuolo", required = true)
    protected String dataInizioRuolo;
    @XmlElement(name = "DataFineRuolo", required = true)
    protected String dataFineRuolo;
    @XmlElement(name = "CodiceFiscale", required = true)
    protected String codiceFiscale;
    @XmlElement(name = "CognomeNome", required = true)
    protected String cognomeNome;
    @XmlElement(name = "Comune", required = true)
    protected String comune;
    @XmlElement(name = "Indirizzo", required = true)
    protected String indirizzo;
    @XmlElement(name = "CAP", required = true)
    protected String cap;
    @XmlElement(name = "Tel", required = true)
    protected String tel;
    @XmlElement(required = true)
    protected String email;
    @XmlElement(name = "Variazione", required = true)
    protected String variazione;

    /**
     * Gets the value of the ruolo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRuolo() {
        return ruolo;
    }

    /**
     * Sets the value of the ruolo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRuolo(String value) {
        this.ruolo = value;
    }

    /**
     * Gets the value of the dataInizioRuolo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataInizioRuolo() {
        return dataInizioRuolo;
    }

    /**
     * Sets the value of the dataInizioRuolo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataInizioRuolo(String value) {
        this.dataInizioRuolo = value;
    }

    /**
     * Gets the value of the dataFineRuolo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataFineRuolo() {
        return dataFineRuolo;
    }

    /**
     * Sets the value of the dataFineRuolo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataFineRuolo(String value) {
        this.dataFineRuolo = value;
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

    /**
     * Gets the value of the cognomeNome property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCognomeNome() {
        return cognomeNome;
    }

    /**
     * Sets the value of the cognomeNome property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCognomeNome(String value) {
        this.cognomeNome = value;
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
     * Gets the value of the tel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTel() {
        return tel;
    }

    /**
     * Sets the value of the tel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTel(String value) {
        this.tel = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
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
