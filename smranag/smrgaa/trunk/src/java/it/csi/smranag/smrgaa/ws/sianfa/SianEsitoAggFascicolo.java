
package it.csi.smranag.smrgaa.ws.sianfa;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for sianEsitoAggFascicolo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sianEsitoAggFascicolo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codErrorePl" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="cuaa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataFine" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="dataInizio" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="descrErrorePl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="esitoAggiornamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idChiamata" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="idFaseAggiornamento" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="idUtente" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="segnalazioneOutputServizio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sianEsitoAggFascicolo", propOrder = {
    "codErrorePl",
    "cuaa",
    "dataFine",
    "dataInizio",
    "descrErrorePl",
    "esitoAggiornamento",
    "idChiamata",
    "idFaseAggiornamento",
    "idUtente",
    "segnalazioneOutputServizio"
})
public class SianEsitoAggFascicolo {

    protected Long codErrorePl;
    protected String cuaa;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataFine;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataInizio;
    protected String descrErrorePl;
    protected String esitoAggiornamento;
    protected Long idChiamata;
    protected Long idFaseAggiornamento;
    protected Long idUtente;
    protected String segnalazioneOutputServizio;

    /**
     * Gets the value of the codErrorePl property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCodErrorePl() {
        return codErrorePl;
    }

    /**
     * Sets the value of the codErrorePl property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCodErrorePl(Long value) {
        this.codErrorePl = value;
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
     * Gets the value of the dataFine property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataFine() {
        return dataFine;
    }

    /**
     * Sets the value of the dataFine property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataFine(XMLGregorianCalendar value) {
        this.dataFine = value;
    }

    /**
     * Gets the value of the dataInizio property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataInizio() {
        return dataInizio;
    }

    /**
     * Sets the value of the dataInizio property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataInizio(XMLGregorianCalendar value) {
        this.dataInizio = value;
    }

    /**
     * Gets the value of the descrErrorePl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrErrorePl() {
        return descrErrorePl;
    }

    /**
     * Sets the value of the descrErrorePl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrErrorePl(String value) {
        this.descrErrorePl = value;
    }

    /**
     * Gets the value of the esitoAggiornamento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEsitoAggiornamento() {
        return esitoAggiornamento;
    }

    /**
     * Sets the value of the esitoAggiornamento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEsitoAggiornamento(String value) {
        this.esitoAggiornamento = value;
    }

    /**
     * Gets the value of the idChiamata property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdChiamata() {
        return idChiamata;
    }

    /**
     * Sets the value of the idChiamata property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdChiamata(Long value) {
        this.idChiamata = value;
    }

    /**
     * Gets the value of the idFaseAggiornamento property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdFaseAggiornamento() {
        return idFaseAggiornamento;
    }

    /**
     * Sets the value of the idFaseAggiornamento property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdFaseAggiornamento(Long value) {
        this.idFaseAggiornamento = value;
    }

    /**
     * Gets the value of the idUtente property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdUtente() {
        return idUtente;
    }

    /**
     * Sets the value of the idUtente property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdUtente(Long value) {
        this.idUtente = value;
    }

    /**
     * Gets the value of the segnalazioneOutputServizio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSegnalazioneOutputServizio() {
        return segnalazioneOutputServizio;
    }

    /**
     * Sets the value of the segnalazioneOutputServizio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSegnalazioneOutputServizio(String value) {
        this.segnalazioneOutputServizio = value;
    }

}
