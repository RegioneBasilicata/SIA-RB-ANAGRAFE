
package it.csi.solmr.ws.infoc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RappresentanteLegale complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RappresentanteLegale">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cap" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codComuneNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codComuneResidenza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cognome" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descrComuneNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descrComuneResidenza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="indirizzo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nome" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroCivico" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sesso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoVia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RappresentanteLegale", propOrder = {
    "cap",
    "codComuneNascita",
    "codComuneResidenza",
    "codiceFiscale",
    "cognome",
    "dataNascita",
    "descrComuneNascita",
    "descrComuneResidenza",
    "indirizzo",
    "nome",
    "numeroCivico",
    "sesso",
    "tipoVia"
})
public class RappresentanteLegale {

    @XmlElementRef(name = "cap", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> cap;
    @XmlElementRef(name = "codComuneNascita", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> codComuneNascita;
    @XmlElementRef(name = "codComuneResidenza", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> codComuneResidenza;
    @XmlElementRef(name = "codiceFiscale", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> codiceFiscale;
    @XmlElementRef(name = "cognome", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> cognome;
    @XmlElementRef(name = "dataNascita", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> dataNascita;
    @XmlElementRef(name = "descrComuneNascita", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> descrComuneNascita;
    @XmlElementRef(name = "descrComuneResidenza", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> descrComuneResidenza;
    @XmlElementRef(name = "indirizzo", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> indirizzo;
    @XmlElementRef(name = "nome", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> nome;
    @XmlElementRef(name = "numeroCivico", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> numeroCivico;
    @XmlElementRef(name = "sesso", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> sesso;
    @XmlElementRef(name = "tipoVia", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> tipoVia;

    /**
     * Gets the value of the cap property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCap() {
        return cap;
    }

    /**
     * Sets the value of the cap property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCap(JAXBElement<String> value) {
        this.cap = value;
    }

    /**
     * Gets the value of the codComuneNascita property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCodComuneNascita() {
        return codComuneNascita;
    }

    /**
     * Sets the value of the codComuneNascita property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCodComuneNascita(JAXBElement<String> value) {
        this.codComuneNascita = value;
    }

    /**
     * Gets the value of the codComuneResidenza property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCodComuneResidenza() {
        return codComuneResidenza;
    }

    /**
     * Sets the value of the codComuneResidenza property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCodComuneResidenza(JAXBElement<String> value) {
        this.codComuneResidenza = value;
    }

    /**
     * Gets the value of the codiceFiscale property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCodiceFiscale() {
        return codiceFiscale;
    }

    /**
     * Sets the value of the codiceFiscale property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCodiceFiscale(JAXBElement<String> value) {
        this.codiceFiscale = value;
    }

    /**
     * Gets the value of the cognome property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCognome() {
        return cognome;
    }

    /**
     * Sets the value of the cognome property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCognome(JAXBElement<String> value) {
        this.cognome = value;
    }

    /**
     * Gets the value of the dataNascita property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDataNascita() {
        return dataNascita;
    }

    /**
     * Sets the value of the dataNascita property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDataNascita(JAXBElement<String> value) {
        this.dataNascita = value;
    }

    /**
     * Gets the value of the descrComuneNascita property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescrComuneNascita() {
        return descrComuneNascita;
    }

    /**
     * Sets the value of the descrComuneNascita property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescrComuneNascita(JAXBElement<String> value) {
        this.descrComuneNascita = value;
    }

    /**
     * Gets the value of the descrComuneResidenza property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescrComuneResidenza() {
        return descrComuneResidenza;
    }

    /**
     * Sets the value of the descrComuneResidenza property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescrComuneResidenza(JAXBElement<String> value) {
        this.descrComuneResidenza = value;
    }

    /**
     * Gets the value of the indirizzo property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIndirizzo() {
        return indirizzo;
    }

    /**
     * Sets the value of the indirizzo property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIndirizzo(JAXBElement<String> value) {
        this.indirizzo = value;
    }

    /**
     * Gets the value of the nome property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getNome() {
        return nome;
    }

    /**
     * Sets the value of the nome property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setNome(JAXBElement<String> value) {
        this.nome = value;
    }

    /**
     * Gets the value of the numeroCivico property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getNumeroCivico() {
        return numeroCivico;
    }

    /**
     * Sets the value of the numeroCivico property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setNumeroCivico(JAXBElement<String> value) {
        this.numeroCivico = value;
    }

    /**
     * Gets the value of the sesso property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSesso() {
        return sesso;
    }

    /**
     * Sets the value of the sesso property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSesso(JAXBElement<String> value) {
        this.sesso = value;
    }

    /**
     * Gets the value of the tipoVia property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTipoVia() {
        return tipoVia;
    }

    /**
     * Sets the value of the tipoVia property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTipoVia(JAXBElement<String> value) {
        this.tipoVia = value;
    }

}
