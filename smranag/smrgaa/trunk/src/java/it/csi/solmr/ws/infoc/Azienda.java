
package it.csi.solmr.ws.infoc;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Azienda complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Azienda">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="annoCCIAA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codATECO2007" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codiceCausaleCessazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataCancellazRea" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataCessazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataCostituzione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataIscrRegistroImpr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataIscrizioneRea" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descrATECO2007" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descrizioneCausaleCessazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descrizioneNaturaGiuridica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idNaturaGiuridica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="listaPersoneRI" type="{http://bean.frontend.ls.com/xsd}ListaPersoneRI" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="listaProcConcors" type="{http://bean.frontend.ls.com/xsd}ProcConcors" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="listaSedi" type="{http://bean.frontend.ls.com/xsd}ListaSedi" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="listaSezSpec" type="{http://bean.frontend.ls.com/xsd}SezSpec" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="nRegistroImpreseCCIAA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroCCIAA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="oggettoSociale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="partitaIva" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="postaElettronicaCertificata" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="provinciaCCIAA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ragioneSociale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rappresentanteLegale" type="{http://bean.frontend.ls.com/xsd}RappresentanteLegale" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Azienda", propOrder = {
    "annoCCIAA",
    "codATECO2007",
    "codiceCausaleCessazione",
    "codiceFiscale",
    "dataCancellazRea",
    "dataCessazione",
    "dataCostituzione",
    "dataIscrRegistroImpr",
    "dataIscrizioneRea",
    "descrATECO2007",
    "descrizioneCausaleCessazione",
    "descrizioneNaturaGiuridica",
    "idNaturaGiuridica",
    "listaPersoneRI",
    "listaProcConcors",
    "listaSedi",
    "listaSezSpec",
    "nRegistroImpreseCCIAA",
    "numeroCCIAA",
    "oggettoSociale",
    "partitaIva",
    "postaElettronicaCertificata",
    "provinciaCCIAA",
    "ragioneSociale",
    "rappresentanteLegale"
})
public class Azienda {

    @XmlElementRef(name = "annoCCIAA", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> annoCCIAA;
    @XmlElementRef(name = "codATECO2007", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> codATECO2007;
    @XmlElementRef(name = "codiceCausaleCessazione", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> codiceCausaleCessazione;
    @XmlElementRef(name = "codiceFiscale", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> codiceFiscale;
    @XmlElementRef(name = "dataCancellazRea", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> dataCancellazRea;
    @XmlElementRef(name = "dataCessazione", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> dataCessazione;
    @XmlElementRef(name = "dataCostituzione", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> dataCostituzione;
    @XmlElementRef(name = "dataIscrRegistroImpr", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> dataIscrRegistroImpr;
    @XmlElementRef(name = "dataIscrizioneRea", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> dataIscrizioneRea;
    @XmlElementRef(name = "descrATECO2007", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> descrATECO2007;
    @XmlElementRef(name = "descrizioneCausaleCessazione", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> descrizioneCausaleCessazione;
    @XmlElementRef(name = "descrizioneNaturaGiuridica", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> descrizioneNaturaGiuridica;
    @XmlElementRef(name = "idNaturaGiuridica", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> idNaturaGiuridica;
    @XmlElement(nillable = true)
    protected List<ListaPersoneRI> listaPersoneRI;
    @XmlElement(nillable = true)
    protected List<ProcConcors> listaProcConcors;
    @XmlElement(nillable = true)
    protected List<ListaSedi> listaSedi;
    @XmlElement(nillable = true)
    protected List<SezSpec> listaSezSpec;
    @XmlElementRef(name = "nRegistroImpreseCCIAA", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> nRegistroImpreseCCIAA;
    @XmlElementRef(name = "numeroCCIAA", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> numeroCCIAA;
    @XmlElementRef(name = "oggettoSociale", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> oggettoSociale;
    @XmlElementRef(name = "partitaIva", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> partitaIva;
    @XmlElementRef(name = "postaElettronicaCertificata", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> postaElettronicaCertificata;
    @XmlElementRef(name = "provinciaCCIAA", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> provinciaCCIAA;
    @XmlElementRef(name = "ragioneSociale", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> ragioneSociale;
    @XmlElementRef(name = "rappresentanteLegale", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<RappresentanteLegale> rappresentanteLegale;

    /**
     * Gets the value of the annoCCIAA property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAnnoCCIAA() {
        return annoCCIAA;
    }

    /**
     * Sets the value of the annoCCIAA property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAnnoCCIAA(JAXBElement<String> value) {
        this.annoCCIAA = value;
    }

    /**
     * Gets the value of the codATECO2007 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCodATECO2007() {
        return codATECO2007;
    }

    /**
     * Sets the value of the codATECO2007 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCodATECO2007(JAXBElement<String> value) {
        this.codATECO2007 = value;
    }

    /**
     * Gets the value of the codiceCausaleCessazione property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCodiceCausaleCessazione() {
        return codiceCausaleCessazione;
    }

    /**
     * Sets the value of the codiceCausaleCessazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCodiceCausaleCessazione(JAXBElement<String> value) {
        this.codiceCausaleCessazione = value;
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
     * Gets the value of the dataCancellazRea property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDataCancellazRea() {
        return dataCancellazRea;
    }

    /**
     * Sets the value of the dataCancellazRea property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDataCancellazRea(JAXBElement<String> value) {
        this.dataCancellazRea = value;
    }

    /**
     * Gets the value of the dataCessazione property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDataCessazione() {
        return dataCessazione;
    }

    /**
     * Sets the value of the dataCessazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDataCessazione(JAXBElement<String> value) {
        this.dataCessazione = value;
    }

    /**
     * Gets the value of the dataCostituzione property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDataCostituzione() {
        return dataCostituzione;
    }

    /**
     * Sets the value of the dataCostituzione property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDataCostituzione(JAXBElement<String> value) {
        this.dataCostituzione = value;
    }

    /**
     * Gets the value of the dataIscrRegistroImpr property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDataIscrRegistroImpr() {
        return dataIscrRegistroImpr;
    }

    /**
     * Sets the value of the dataIscrRegistroImpr property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDataIscrRegistroImpr(JAXBElement<String> value) {
        this.dataIscrRegistroImpr = value;
    }

    /**
     * Gets the value of the dataIscrizioneRea property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDataIscrizioneRea() {
        return dataIscrizioneRea;
    }

    /**
     * Sets the value of the dataIscrizioneRea property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDataIscrizioneRea(JAXBElement<String> value) {
        this.dataIscrizioneRea = value;
    }

    /**
     * Gets the value of the descrATECO2007 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescrATECO2007() {
        return descrATECO2007;
    }

    /**
     * Sets the value of the descrATECO2007 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescrATECO2007(JAXBElement<String> value) {
        this.descrATECO2007 = value;
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
     * Gets the value of the descrizioneNaturaGiuridica property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescrizioneNaturaGiuridica() {
        return descrizioneNaturaGiuridica;
    }

    /**
     * Sets the value of the descrizioneNaturaGiuridica property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescrizioneNaturaGiuridica(JAXBElement<String> value) {
        this.descrizioneNaturaGiuridica = value;
    }

    /**
     * Gets the value of the idNaturaGiuridica property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIdNaturaGiuridica() {
        return idNaturaGiuridica;
    }

    /**
     * Sets the value of the idNaturaGiuridica property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIdNaturaGiuridica(JAXBElement<String> value) {
        this.idNaturaGiuridica = value;
    }

    /**
     * Gets the value of the listaPersoneRI property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaPersoneRI property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaPersoneRI().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ListaPersoneRI }
     * 
     * 
     */
    public List<ListaPersoneRI> getListaPersoneRI() {
        if (listaPersoneRI == null) {
            listaPersoneRI = new ArrayList<ListaPersoneRI>();
        }
        return this.listaPersoneRI;
    }

    /**
     * Gets the value of the listaProcConcors property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaProcConcors property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaProcConcors().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProcConcors }
     * 
     * 
     */
    public List<ProcConcors> getListaProcConcors() {
        if (listaProcConcors == null) {
            listaProcConcors = new ArrayList<ProcConcors>();
        }
        return this.listaProcConcors;
    }

    /**
     * Gets the value of the listaSedi property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaSedi property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaSedi().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ListaSedi }
     * 
     * 
     */
    public List<ListaSedi> getListaSedi() {
        if (listaSedi == null) {
            listaSedi = new ArrayList<ListaSedi>();
        }
        return this.listaSedi;
    }

    /**
     * Gets the value of the listaSezSpec property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaSezSpec property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaSezSpec().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SezSpec }
     * 
     * 
     */
    public List<SezSpec> getListaSezSpec() {
        if (listaSezSpec == null) {
            listaSezSpec = new ArrayList<SezSpec>();
        }
        return this.listaSezSpec;
    }

    /**
     * Gets the value of the nRegistroImpreseCCIAA property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getNRegistroImpreseCCIAA() {
        return nRegistroImpreseCCIAA;
    }

    /**
     * Sets the value of the nRegistroImpreseCCIAA property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setNRegistroImpreseCCIAA(JAXBElement<String> value) {
        this.nRegistroImpreseCCIAA = value;
    }

    /**
     * Gets the value of the numeroCCIAA property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getNumeroCCIAA() {
        return numeroCCIAA;
    }

    /**
     * Sets the value of the numeroCCIAA property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setNumeroCCIAA(JAXBElement<String> value) {
        this.numeroCCIAA = value;
    }

    /**
     * Gets the value of the oggettoSociale property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getOggettoSociale() {
        return oggettoSociale;
    }

    /**
     * Sets the value of the oggettoSociale property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setOggettoSociale(JAXBElement<String> value) {
        this.oggettoSociale = value;
    }

    /**
     * Gets the value of the partitaIva property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPartitaIva() {
        return partitaIva;
    }

    /**
     * Sets the value of the partitaIva property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPartitaIva(JAXBElement<String> value) {
        this.partitaIva = value;
    }

    /**
     * Gets the value of the postaElettronicaCertificata property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPostaElettronicaCertificata() {
        return postaElettronicaCertificata;
    }

    /**
     * Sets the value of the postaElettronicaCertificata property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPostaElettronicaCertificata(JAXBElement<String> value) {
        this.postaElettronicaCertificata = value;
    }

    /**
     * Gets the value of the provinciaCCIAA property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getProvinciaCCIAA() {
        return provinciaCCIAA;
    }

    /**
     * Sets the value of the provinciaCCIAA property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setProvinciaCCIAA(JAXBElement<String> value) {
        this.provinciaCCIAA = value;
    }

    /**
     * Gets the value of the ragioneSociale property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getRagioneSociale() {
        return ragioneSociale;
    }

    /**
     * Sets the value of the ragioneSociale property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setRagioneSociale(JAXBElement<String> value) {
        this.ragioneSociale = value;
    }

    /**
     * Gets the value of the rappresentanteLegale property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link RappresentanteLegale }{@code >}
     *     
     */
    public JAXBElement<RappresentanteLegale> getRappresentanteLegale() {
        return rappresentanteLegale;
    }

    /**
     * Sets the value of the rappresentanteLegale property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link RappresentanteLegale }{@code >}
     *     
     */
    public void setRappresentanteLegale(JAXBElement<RappresentanteLegale> value) {
        this.rappresentanteLegale = value;
    }

}
