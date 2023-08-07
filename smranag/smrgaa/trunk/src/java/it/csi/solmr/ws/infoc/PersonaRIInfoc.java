
package it.csi.solmr.ws.infoc;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for PersonaRIInfoc complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PersonaRIInfoc">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="capResidenza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codComuneNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codComuneRes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cognome" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataNascita" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="descrFrazioneRes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descrStatoNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descrStatoRes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descrToponimoResid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="listaCaricaPersInfoc" type="{http://bean.frontend.ls.com/xsd}CaricaPersonaInfoc" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="nome" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numCivicoResid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="progrOrdineVisura" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="progrPersona" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="sesso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="siglaProvResidenza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="viaResidenza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonaRIInfoc", propOrder = {
    "capResidenza",
    "codComuneNascita",
    "codComuneRes",
    "codiceFiscale",
    "cognome",
    "dataNascita",
    "descrFrazioneRes",
    "descrStatoNascita",
    "descrStatoRes",
    "descrToponimoResid",
    "listaCaricaPersInfoc",
    "nome",
    "numCivicoResid",
    "progrOrdineVisura",
    "progrPersona",
    "sesso",
    "siglaProvResidenza",
    "viaResidenza"
})
public class PersonaRIInfoc {

    @XmlElementRef(name = "capResidenza", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> capResidenza;
    @XmlElementRef(name = "codComuneNascita", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> codComuneNascita;
    @XmlElementRef(name = "codComuneRes", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> codComuneRes;
    @XmlElementRef(name = "codiceFiscale", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> codiceFiscale;
    @XmlElementRef(name = "cognome", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> cognome;
    @XmlElementRef(name = "dataNascita", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<XMLGregorianCalendar> dataNascita;
    @XmlElementRef(name = "descrFrazioneRes", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> descrFrazioneRes;
    @XmlElementRef(name = "descrStatoNascita", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> descrStatoNascita;
    @XmlElementRef(name = "descrStatoRes", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> descrStatoRes;
    @XmlElementRef(name = "descrToponimoResid", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> descrToponimoResid;
    @XmlElement(nillable = true)
    protected List<CaricaPersonaInfoc> listaCaricaPersInfoc;
    @XmlElementRef(name = "nome", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> nome;
    @XmlElementRef(name = "numCivicoResid", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> numCivicoResid;
    protected Long progrOrdineVisura;
    protected Long progrPersona;
    @XmlElementRef(name = "sesso", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> sesso;
    @XmlElementRef(name = "siglaProvResidenza", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> siglaProvResidenza;
    @XmlElementRef(name = "viaResidenza", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> viaResidenza;

    /**
     * Gets the value of the capResidenza property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCapResidenza() {
        return capResidenza;
    }

    /**
     * Sets the value of the capResidenza property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCapResidenza(JAXBElement<String> value) {
        this.capResidenza = value;
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
     * Gets the value of the codComuneRes property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCodComuneRes() {
        return codComuneRes;
    }

    /**
     * Sets the value of the codComuneRes property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCodComuneRes(JAXBElement<String> value) {
        this.codComuneRes = value;
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
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getDataNascita() {
        return dataNascita;
    }

    /**
     * Sets the value of the dataNascita property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setDataNascita(JAXBElement<XMLGregorianCalendar> value) {
        this.dataNascita = value;
    }

    /**
     * Gets the value of the descrFrazioneRes property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescrFrazioneRes() {
        return descrFrazioneRes;
    }

    /**
     * Sets the value of the descrFrazioneRes property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescrFrazioneRes(JAXBElement<String> value) {
        this.descrFrazioneRes = value;
    }

    /**
     * Gets the value of the descrStatoNascita property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescrStatoNascita() {
        return descrStatoNascita;
    }

    /**
     * Sets the value of the descrStatoNascita property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescrStatoNascita(JAXBElement<String> value) {
        this.descrStatoNascita = value;
    }

    /**
     * Gets the value of the descrStatoRes property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescrStatoRes() {
        return descrStatoRes;
    }

    /**
     * Sets the value of the descrStatoRes property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescrStatoRes(JAXBElement<String> value) {
        this.descrStatoRes = value;
    }

    /**
     * Gets the value of the descrToponimoResid property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescrToponimoResid() {
        return descrToponimoResid;
    }

    /**
     * Sets the value of the descrToponimoResid property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescrToponimoResid(JAXBElement<String> value) {
        this.descrToponimoResid = value;
    }

    /**
     * Gets the value of the listaCaricaPersInfoc property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaCaricaPersInfoc property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaCaricaPersInfoc().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CaricaPersonaInfoc }
     * 
     * 
     */
    public List<CaricaPersonaInfoc> getListaCaricaPersInfoc() {
        if (listaCaricaPersInfoc == null) {
            listaCaricaPersInfoc = new ArrayList<CaricaPersonaInfoc>();
        }
        return this.listaCaricaPersInfoc;
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
     * Gets the value of the numCivicoResid property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getNumCivicoResid() {
        return numCivicoResid;
    }

    /**
     * Sets the value of the numCivicoResid property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setNumCivicoResid(JAXBElement<String> value) {
        this.numCivicoResid = value;
    }

    /**
     * Gets the value of the progrOrdineVisura property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getProgrOrdineVisura() {
        return progrOrdineVisura;
    }

    /**
     * Sets the value of the progrOrdineVisura property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setProgrOrdineVisura(Long value) {
        this.progrOrdineVisura = value;
    }

    /**
     * Gets the value of the progrPersona property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getProgrPersona() {
        return progrPersona;
    }

    /**
     * Sets the value of the progrPersona property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setProgrPersona(Long value) {
        this.progrPersona = value;
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
     * Gets the value of the siglaProvResidenza property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSiglaProvResidenza() {
        return siglaProvResidenza;
    }

    /**
     * Sets the value of the siglaProvResidenza property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSiglaProvResidenza(JAXBElement<String> value) {
        this.siglaProvResidenza = value;
    }

    /**
     * Gets the value of the viaResidenza property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getViaResidenza() {
        return viaResidenza;
    }

    /**
     * Sets the value of the viaResidenza property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setViaResidenza(JAXBElement<String> value) {
        this.viaResidenza = value;
    }

}
