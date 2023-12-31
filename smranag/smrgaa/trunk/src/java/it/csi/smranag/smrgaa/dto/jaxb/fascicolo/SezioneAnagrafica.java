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
 *         &lt;element ref="{}HeadChecklist"/>
 *         &lt;element ref="{}Titolo1sezAnagrafica"/>
 *         &lt;element ref="{}Titolo2sezAnagrafica"/>
 *         &lt;element ref="{}Cuaa"/>
 *         &lt;element ref="{}PartitaIva"/>
 *         &lt;element ref="{}Denominazione"/>
 *         &lt;element ref="{}IndirizzoSedeLeg"/>
 *         &lt;element ref="{}Pec"/>
 *         &lt;element ref="{}email"/>
 *         &lt;element ref="{}Tel"/>
 *         &lt;element ref="{}Ateco"/>
 *         &lt;element ref="{}Registro"/>
 *         &lt;element ref="{}Anno"/>
 *         &lt;element ref="{}FormaConduzione"/>
 *         &lt;element ref="{}AltreInfo"/>
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
    "headChecklist",
    "titolo1SezAnagrafica",
    "titolo2SezAnagrafica",
    "cuaa",
    "partitaIva",
    "denominazione",
    "indirizzoSedeLeg",
    "pec",
    "email",
    "tel",
    "ateco",
    "registro",
    "anno",
    "formaConduzione",
    "altreInfo"
})
@XmlRootElement(name = "SezioneAnagrafica")
public class SezioneAnagrafica {

    @XmlElement(name = "Visibility")
    protected boolean visibility;
    @XmlElement(name = "HeadChecklist")
    protected boolean headChecklist;
    @XmlElement(name = "Titolo1sezAnagrafica", required = true)
    protected String titolo1SezAnagrafica;
    @XmlElement(name = "Titolo2sezAnagrafica", required = true)
    protected String titolo2SezAnagrafica;
    @XmlElement(name = "Cuaa", required = true)
    protected String cuaa;
    @XmlElement(name = "PartitaIva", required = true)
    protected String partitaIva;
    @XmlElement(name = "Denominazione", required = true)
    protected String denominazione;
    @XmlElement(name = "IndirizzoSedeLeg", required = true)
    protected String indirizzoSedeLeg;
    @XmlElement(name = "Pec", required = true)
    protected String pec;
    @XmlElement(required = true)
    protected String email;
    @XmlElement(name = "Tel", required = true)
    protected String tel;
    @XmlElement(name = "Ateco", required = true)
    protected String ateco;
    @XmlElement(name = "Registro", required = true)
    protected String registro;
    @XmlElement(name = "Anno", required = true)
    protected String anno;
    @XmlElement(name = "FormaConduzione", required = true)
    protected String formaConduzione;
    @XmlElement(name = "AltreInfo", required = true)
    protected String altreInfo;

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
     * Gets the value of the headChecklist property.
     * 
     */
    public boolean isHeadChecklist() {
        return headChecklist;
    }

    /**
     * Sets the value of the headChecklist property.
     * 
     */
    public void setHeadChecklist(boolean value) {
        this.headChecklist = value;
    }

    /**
     * Gets the value of the titolo1SezAnagrafica property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitolo1SezAnagrafica() {
        return titolo1SezAnagrafica;
    }

    /**
     * Sets the value of the titolo1SezAnagrafica property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitolo1SezAnagrafica(String value) {
        this.titolo1SezAnagrafica = value;
    }

    /**
     * Gets the value of the titolo2SezAnagrafica property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitolo2SezAnagrafica() {
        return titolo2SezAnagrafica;
    }

    /**
     * Sets the value of the titolo2SezAnagrafica property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitolo2SezAnagrafica(String value) {
        this.titolo2SezAnagrafica = value;
    }

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
     * Gets the value of the indirizzoSedeLeg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndirizzoSedeLeg() {
        return indirizzoSedeLeg;
    }

    /**
     * Sets the value of the indirizzoSedeLeg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndirizzoSedeLeg(String value) {
        this.indirizzoSedeLeg = value;
    }

    /**
     * Gets the value of the pec property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPec() {
        return pec;
    }

    /**
     * Sets the value of the pec property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPec(String value) {
        this.pec = value;
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
     * Gets the value of the ateco property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAteco() {
        return ateco;
    }

    /**
     * Sets the value of the ateco property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAteco(String value) {
        this.ateco = value;
    }

    /**
     * Gets the value of the registro property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegistro() {
        return registro;
    }

    /**
     * Sets the value of the registro property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegistro(String value) {
        this.registro = value;
    }

    /**
     * Gets the value of the anno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnno() {
        return anno;
    }

    /**
     * Sets the value of the anno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnno(String value) {
        this.anno = value;
    }

    /**
     * Gets the value of the formaConduzione property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormaConduzione() {
        return formaConduzione;
    }

    /**
     * Sets the value of the formaConduzione property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormaConduzione(String value) {
        this.formaConduzione = value;
    }

    /**
     * Gets the value of the altreInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAltreInfo() {
        return altreInfo;
    }

    /**
     * Sets the value of the altreInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAltreInfo(String value) {
        this.altreInfo = value;
    }

}
