
package it.csi.solmr.ws.infoc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ProcConcors complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProcConcors">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codAtto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codLiquidazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataAperturaProc" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="dataChiusuraLiquidaz" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="dataEsecConcordPrevent" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="dataFineLiquidaz" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="dataRegistroAtto" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="dataRevocalLiquidaz" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="descIndicatEsecutAtto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descrAltreIndicazioni" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descrCodAtto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descrNotaio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descrTribunale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="localRegistroAtto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numRestistrAtto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="progrLiquidazione" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="siglaProvRegAtto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProcConcors", propOrder = {
    "codAtto",
    "codLiquidazione",
    "dataAperturaProc",
    "dataChiusuraLiquidaz",
    "dataEsecConcordPrevent",
    "dataFineLiquidaz",
    "dataRegistroAtto",
    "dataRevocalLiquidaz",
    "descIndicatEsecutAtto",
    "descrAltreIndicazioni",
    "descrCodAtto",
    "descrNotaio",
    "descrTribunale",
    "localRegistroAtto",
    "numRestistrAtto",
    "progrLiquidazione",
    "siglaProvRegAtto"
})
public class ProcConcors {

    @XmlElementRef(name = "codAtto", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> codAtto;
    @XmlElementRef(name = "codLiquidazione", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> codLiquidazione;
    @XmlElementRef(name = "dataAperturaProc", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<XMLGregorianCalendar> dataAperturaProc;
    @XmlElementRef(name = "dataChiusuraLiquidaz", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<XMLGregorianCalendar> dataChiusuraLiquidaz;
    @XmlElementRef(name = "dataEsecConcordPrevent", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<XMLGregorianCalendar> dataEsecConcordPrevent;
    @XmlElementRef(name = "dataFineLiquidaz", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<XMLGregorianCalendar> dataFineLiquidaz;
    @XmlElementRef(name = "dataRegistroAtto", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<XMLGregorianCalendar> dataRegistroAtto;
    @XmlElementRef(name = "dataRevocalLiquidaz", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<XMLGregorianCalendar> dataRevocalLiquidaz;
    @XmlElementRef(name = "descIndicatEsecutAtto", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> descIndicatEsecutAtto;
    @XmlElementRef(name = "descrAltreIndicazioni", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> descrAltreIndicazioni;
    @XmlElementRef(name = "descrCodAtto", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> descrCodAtto;
    @XmlElementRef(name = "descrNotaio", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> descrNotaio;
    @XmlElementRef(name = "descrTribunale", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> descrTribunale;
    @XmlElementRef(name = "localRegistroAtto", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> localRegistroAtto;
    @XmlElementRef(name = "numRestistrAtto", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> numRestistrAtto;
    protected Long progrLiquidazione;
    @XmlElementRef(name = "siglaProvRegAtto", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> siglaProvRegAtto;

    /**
     * Gets the value of the codAtto property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCodAtto() {
        return codAtto;
    }

    /**
     * Sets the value of the codAtto property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCodAtto(JAXBElement<String> value) {
        this.codAtto = value;
    }

    /**
     * Gets the value of the codLiquidazione property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCodLiquidazione() {
        return codLiquidazione;
    }

    /**
     * Sets the value of the codLiquidazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCodLiquidazione(JAXBElement<String> value) {
        this.codLiquidazione = value;
    }

    /**
     * Gets the value of the dataAperturaProc property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getDataAperturaProc() {
        return dataAperturaProc;
    }

    /**
     * Sets the value of the dataAperturaProc property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setDataAperturaProc(JAXBElement<XMLGregorianCalendar> value) {
        this.dataAperturaProc = value;
    }

    /**
     * Gets the value of the dataChiusuraLiquidaz property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getDataChiusuraLiquidaz() {
        return dataChiusuraLiquidaz;
    }

    /**
     * Sets the value of the dataChiusuraLiquidaz property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setDataChiusuraLiquidaz(JAXBElement<XMLGregorianCalendar> value) {
        this.dataChiusuraLiquidaz = value;
    }

    /**
     * Gets the value of the dataEsecConcordPrevent property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getDataEsecConcordPrevent() {
        return dataEsecConcordPrevent;
    }

    /**
     * Sets the value of the dataEsecConcordPrevent property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setDataEsecConcordPrevent(JAXBElement<XMLGregorianCalendar> value) {
        this.dataEsecConcordPrevent = value;
    }

    /**
     * Gets the value of the dataFineLiquidaz property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getDataFineLiquidaz() {
        return dataFineLiquidaz;
    }

    /**
     * Sets the value of the dataFineLiquidaz property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setDataFineLiquidaz(JAXBElement<XMLGregorianCalendar> value) {
        this.dataFineLiquidaz = value;
    }

    /**
     * Gets the value of the dataRegistroAtto property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getDataRegistroAtto() {
        return dataRegistroAtto;
    }

    /**
     * Sets the value of the dataRegistroAtto property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setDataRegistroAtto(JAXBElement<XMLGregorianCalendar> value) {
        this.dataRegistroAtto = value;
    }

    /**
     * Gets the value of the dataRevocalLiquidaz property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getDataRevocalLiquidaz() {
        return dataRevocalLiquidaz;
    }

    /**
     * Sets the value of the dataRevocalLiquidaz property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setDataRevocalLiquidaz(JAXBElement<XMLGregorianCalendar> value) {
        this.dataRevocalLiquidaz = value;
    }

    /**
     * Gets the value of the descIndicatEsecutAtto property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescIndicatEsecutAtto() {
        return descIndicatEsecutAtto;
    }

    /**
     * Sets the value of the descIndicatEsecutAtto property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescIndicatEsecutAtto(JAXBElement<String> value) {
        this.descIndicatEsecutAtto = value;
    }

    /**
     * Gets the value of the descrAltreIndicazioni property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescrAltreIndicazioni() {
        return descrAltreIndicazioni;
    }

    /**
     * Sets the value of the descrAltreIndicazioni property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescrAltreIndicazioni(JAXBElement<String> value) {
        this.descrAltreIndicazioni = value;
    }

    /**
     * Gets the value of the descrCodAtto property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescrCodAtto() {
        return descrCodAtto;
    }

    /**
     * Sets the value of the descrCodAtto property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescrCodAtto(JAXBElement<String> value) {
        this.descrCodAtto = value;
    }

    /**
     * Gets the value of the descrNotaio property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescrNotaio() {
        return descrNotaio;
    }

    /**
     * Sets the value of the descrNotaio property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescrNotaio(JAXBElement<String> value) {
        this.descrNotaio = value;
    }

    /**
     * Gets the value of the descrTribunale property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescrTribunale() {
        return descrTribunale;
    }

    /**
     * Sets the value of the descrTribunale property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescrTribunale(JAXBElement<String> value) {
        this.descrTribunale = value;
    }

    /**
     * Gets the value of the localRegistroAtto property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLocalRegistroAtto() {
        return localRegistroAtto;
    }

    /**
     * Sets the value of the localRegistroAtto property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLocalRegistroAtto(JAXBElement<String> value) {
        this.localRegistroAtto = value;
    }

    /**
     * Gets the value of the numRestistrAtto property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getNumRestistrAtto() {
        return numRestistrAtto;
    }

    /**
     * Sets the value of the numRestistrAtto property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setNumRestistrAtto(JAXBElement<String> value) {
        this.numRestistrAtto = value;
    }

    /**
     * Gets the value of the progrLiquidazione property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getProgrLiquidazione() {
        return progrLiquidazione;
    }

    /**
     * Sets the value of the progrLiquidazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setProgrLiquidazione(Long value) {
        this.progrLiquidazione = value;
    }

    /**
     * Gets the value of the siglaProvRegAtto property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSiglaProvRegAtto() {
        return siglaProvRegAtto;
    }

    /**
     * Sets the value of the siglaProvRegAtto property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSiglaProvRegAtto(JAXBElement<String> value) {
        this.siglaProvRegAtto = value;
    }

}
