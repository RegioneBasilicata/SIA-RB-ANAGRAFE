
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
 * <p>Java class for Sede complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Sede">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cap" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codCausaleCessazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codComune" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codTipoLocalizzazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codiceStatoEstero" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataCessaz" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="dataFineAttivita" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="dataInizioAttivita" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="dataInizioValidita" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="denominazioneSede" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descrStatoEstero" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descrizioneAttivitaSede" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descrizioneCausaleCessazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descrizioneTipoSede" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fax" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="frazioneUL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idTipoSede" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="indirizzo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="indirizzoSede" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="listaAtecoRI2007Infoc" type="{http://bean.frontend.ls.com/xsd}AtecoRI2007Infoc" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="nomeComune" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroCivico" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="siglaProvUL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="siglaProvincia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="telefono" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="toponimo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Sede", propOrder = {
    "cap",
    "codCausaleCessazione",
    "codComune",
    "codTipoLocalizzazione",
    "codiceStatoEstero",
    "dataCessaz",
    "dataFineAttivita",
    "dataInizioAttivita",
    "dataInizioValidita",
    "denominazioneSede",
    "descrStatoEstero",
    "descrizioneAttivitaSede",
    "descrizioneCausaleCessazione",
    "descrizioneTipoSede",
    "email",
    "fax",
    "frazioneUL",
    "idTipoSede",
    "indirizzo",
    "indirizzoSede",
    "listaAtecoRI2007Infoc",
    "nomeComune",
    "numeroCivico",
    "siglaProvUL",
    "siglaProvincia",
    "telefono",
    "toponimo"
})
public class Sede {

    @XmlElementRef(name = "cap", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> cap;
    @XmlElementRef(name = "codCausaleCessazione", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> codCausaleCessazione;
    @XmlElementRef(name = "codComune", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> codComune;
    @XmlElementRef(name = "codTipoLocalizzazione", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> codTipoLocalizzazione;
    @XmlElementRef(name = "codiceStatoEstero", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> codiceStatoEstero;
    @XmlElementRef(name = "dataCessaz", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<XMLGregorianCalendar> dataCessaz;
    @XmlElementRef(name = "dataFineAttivita", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<XMLGregorianCalendar> dataFineAttivita;
    @XmlElementRef(name = "dataInizioAttivita", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<XMLGregorianCalendar> dataInizioAttivita;
    @XmlElementRef(name = "dataInizioValidita", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<XMLGregorianCalendar> dataInizioValidita;
    @XmlElementRef(name = "denominazioneSede", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> denominazioneSede;
    @XmlElementRef(name = "descrStatoEstero", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> descrStatoEstero;
    @XmlElementRef(name = "descrizioneAttivitaSede", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> descrizioneAttivitaSede;
    @XmlElementRef(name = "descrizioneCausaleCessazione", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> descrizioneCausaleCessazione;
    @XmlElementRef(name = "descrizioneTipoSede", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> descrizioneTipoSede;
    @XmlElementRef(name = "email", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> email;
    @XmlElementRef(name = "fax", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> fax;
    @XmlElementRef(name = "frazioneUL", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> frazioneUL;
    @XmlElementRef(name = "idTipoSede", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> idTipoSede;
    @XmlElementRef(name = "indirizzo", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> indirizzo;
    @XmlElementRef(name = "indirizzoSede", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> indirizzoSede;
    @XmlElement(nillable = true)
    protected List<AtecoRI2007Infoc> listaAtecoRI2007Infoc;
    @XmlElementRef(name = "nomeComune", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> nomeComune;
    @XmlElementRef(name = "numeroCivico", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> numeroCivico;
    @XmlElementRef(name = "siglaProvUL", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> siglaProvUL;
    @XmlElementRef(name = "siglaProvincia", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> siglaProvincia;
    @XmlElementRef(name = "telefono", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> telefono;
    @XmlElementRef(name = "toponimo", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> toponimo;

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
     * Gets the value of the codCausaleCessazione property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCodCausaleCessazione() {
        return codCausaleCessazione;
    }

    /**
     * Sets the value of the codCausaleCessazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCodCausaleCessazione(JAXBElement<String> value) {
        this.codCausaleCessazione = value;
    }

    /**
     * Gets the value of the codComune property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCodComune() {
        return codComune;
    }

    /**
     * Sets the value of the codComune property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCodComune(JAXBElement<String> value) {
        this.codComune = value;
    }

    /**
     * Gets the value of the codTipoLocalizzazione property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCodTipoLocalizzazione() {
        return codTipoLocalizzazione;
    }

    /**
     * Sets the value of the codTipoLocalizzazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCodTipoLocalizzazione(JAXBElement<String> value) {
        this.codTipoLocalizzazione = value;
    }

    /**
     * Gets the value of the codiceStatoEstero property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCodiceStatoEstero() {
        return codiceStatoEstero;
    }

    /**
     * Sets the value of the codiceStatoEstero property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCodiceStatoEstero(JAXBElement<String> value) {
        this.codiceStatoEstero = value;
    }

    /**
     * Gets the value of the dataCessaz property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getDataCessaz() {
        return dataCessaz;
    }

    /**
     * Sets the value of the dataCessaz property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setDataCessaz(JAXBElement<XMLGregorianCalendar> value) {
        this.dataCessaz = value;
    }

    /**
     * Gets the value of the dataFineAttivita property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getDataFineAttivita() {
        return dataFineAttivita;
    }

    /**
     * Sets the value of the dataFineAttivita property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setDataFineAttivita(JAXBElement<XMLGregorianCalendar> value) {
        this.dataFineAttivita = value;
    }

    /**
     * Gets the value of the dataInizioAttivita property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getDataInizioAttivita() {
        return dataInizioAttivita;
    }

    /**
     * Sets the value of the dataInizioAttivita property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setDataInizioAttivita(JAXBElement<XMLGregorianCalendar> value) {
        this.dataInizioAttivita = value;
    }

    /**
     * Gets the value of the dataInizioValidita property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getDataInizioValidita() {
        return dataInizioValidita;
    }

    /**
     * Sets the value of the dataInizioValidita property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setDataInizioValidita(JAXBElement<XMLGregorianCalendar> value) {
        this.dataInizioValidita = value;
    }

    /**
     * Gets the value of the denominazioneSede property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDenominazioneSede() {
        return denominazioneSede;
    }

    /**
     * Sets the value of the denominazioneSede property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDenominazioneSede(JAXBElement<String> value) {
        this.denominazioneSede = value;
    }

    /**
     * Gets the value of the descrStatoEstero property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescrStatoEstero() {
        return descrStatoEstero;
    }

    /**
     * Sets the value of the descrStatoEstero property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescrStatoEstero(JAXBElement<String> value) {
        this.descrStatoEstero = value;
    }

    /**
     * Gets the value of the descrizioneAttivitaSede property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescrizioneAttivitaSede() {
        return descrizioneAttivitaSede;
    }

    /**
     * Sets the value of the descrizioneAttivitaSede property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescrizioneAttivitaSede(JAXBElement<String> value) {
        this.descrizioneAttivitaSede = value;
    }

    /**
     * Gets the value of the descrizioneCausaleCessazione property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescrizioneCausaleCessazione() {
        return descrizioneCausaleCessazione;
    }

    /**
     * Sets the value of the descrizioneCausaleCessazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescrizioneCausaleCessazione(JAXBElement<String> value) {
        this.descrizioneCausaleCessazione = value;
    }

    /**
     * Gets the value of the descrizioneTipoSede property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescrizioneTipoSede() {
        return descrizioneTipoSede;
    }

    /**
     * Sets the value of the descrizioneTipoSede property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescrizioneTipoSede(JAXBElement<String> value) {
        this.descrizioneTipoSede = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setEmail(JAXBElement<String> value) {
        this.email = value;
    }

    /**
     * Gets the value of the fax property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getFax() {
        return fax;
    }

    /**
     * Sets the value of the fax property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setFax(JAXBElement<String> value) {
        this.fax = value;
    }

    /**
     * Gets the value of the frazioneUL property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getFrazioneUL() {
        return frazioneUL;
    }

    /**
     * Sets the value of the frazioneUL property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setFrazioneUL(JAXBElement<String> value) {
        this.frazioneUL = value;
    }

    /**
     * Gets the value of the idTipoSede property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIdTipoSede() {
        return idTipoSede;
    }

    /**
     * Sets the value of the idTipoSede property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIdTipoSede(JAXBElement<String> value) {
        this.idTipoSede = value;
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
     * Gets the value of the indirizzoSede property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIndirizzoSede() {
        return indirizzoSede;
    }

    /**
     * Sets the value of the indirizzoSede property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIndirizzoSede(JAXBElement<String> value) {
        this.indirizzoSede = value;
    }

    /**
     * Gets the value of the listaAtecoRI2007Infoc property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaAtecoRI2007Infoc property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaAtecoRI2007Infoc().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AtecoRI2007Infoc }
     * 
     * 
     */
    public List<AtecoRI2007Infoc> getListaAtecoRI2007Infoc() {
        if (listaAtecoRI2007Infoc == null) {
            listaAtecoRI2007Infoc = new ArrayList<AtecoRI2007Infoc>();
        }
        return this.listaAtecoRI2007Infoc;
    }

    /**
     * Gets the value of the nomeComune property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getNomeComune() {
        return nomeComune;
    }

    /**
     * Sets the value of the nomeComune property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setNomeComune(JAXBElement<String> value) {
        this.nomeComune = value;
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
     * Gets the value of the siglaProvUL property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSiglaProvUL() {
        return siglaProvUL;
    }

    /**
     * Sets the value of the siglaProvUL property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSiglaProvUL(JAXBElement<String> value) {
        this.siglaProvUL = value;
    }

    /**
     * Gets the value of the siglaProvincia property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSiglaProvincia() {
        return siglaProvincia;
    }

    /**
     * Sets the value of the siglaProvincia property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSiglaProvincia(JAXBElement<String> value) {
        this.siglaProvincia = value;
    }

    /**
     * Gets the value of the telefono property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTelefono() {
        return telefono;
    }

    /**
     * Sets the value of the telefono property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTelefono(JAXBElement<String> value) {
        this.telefono = value;
    }

    /**
     * Gets the value of the toponimo property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getToponimo() {
        return toponimo;
    }

    /**
     * Sets the value of the toponimo property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setToponimo(JAXBElement<String> value) {
        this.toponimo = value;
    }

}
