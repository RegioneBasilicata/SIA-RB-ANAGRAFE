
package it.csi.solmr.ws.infoc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ListaPersonaCaricaInfoc complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListaPersonaCaricaInfoc">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codFiscaleAzienda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codFiscalePersona" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codiceCarica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataFineCarica" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="descrCarica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListaPersonaCaricaInfoc", propOrder = {
    "codFiscaleAzienda",
    "codFiscalePersona",
    "codiceCarica",
    "dataFineCarica",
    "descrCarica"
})
public class ListaPersonaCaricaInfoc {

    @XmlElementRef(name = "codFiscaleAzienda", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> codFiscaleAzienda;
    @XmlElementRef(name = "codFiscalePersona", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> codFiscalePersona;
    @XmlElementRef(name = "codiceCarica", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> codiceCarica;
    @XmlElementRef(name = "dataFineCarica", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<XMLGregorianCalendar> dataFineCarica;
    @XmlElementRef(name = "descrCarica", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> descrCarica;

    /**
     * Gets the value of the codFiscaleAzienda property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCodFiscaleAzienda() {
        return codFiscaleAzienda;
    }

    /**
     * Sets the value of the codFiscaleAzienda property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCodFiscaleAzienda(JAXBElement<String> value) {
        this.codFiscaleAzienda = value;
    }

    /**
     * Gets the value of the codFiscalePersona property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCodFiscalePersona() {
        return codFiscalePersona;
    }

    /**
     * Sets the value of the codFiscalePersona property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCodFiscalePersona(JAXBElement<String> value) {
        this.codFiscalePersona = value;
    }

    /**
     * Gets the value of the codiceCarica property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCodiceCarica() {
        return codiceCarica;
    }

    /**
     * Sets the value of the codiceCarica property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCodiceCarica(JAXBElement<String> value) {
        this.codiceCarica = value;
    }

    /**
     * Gets the value of the dataFineCarica property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getDataFineCarica() {
        return dataFineCarica;
    }

    /**
     * Sets the value of the dataFineCarica property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setDataFineCarica(JAXBElement<XMLGregorianCalendar> value) {
        this.dataFineCarica = value;
    }

    /**
     * Gets the value of the descrCarica property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescrCarica() {
        return descrCarica;
    }

    /**
     * Sets the value of the descrCarica property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescrCarica(JAXBElement<String> value) {
        this.descrCarica = value;
    }

}
