
package it.csi.solmr.ws.infoc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for AtecoRI2007Infoc complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AtecoRI2007Infoc">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codAteco2007" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codImportanzaRI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataInizioAteco2007" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AtecoRI2007Infoc", propOrder = {
    "codAteco2007",
    "codImportanzaRI",
    "dataInizioAteco2007"
})
public class AtecoRI2007Infoc {

    @XmlElementRef(name = "codAteco2007", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> codAteco2007;
    @XmlElementRef(name = "codImportanzaRI", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> codImportanzaRI;
    @XmlElementRef(name = "dataInizioAteco2007", namespace = "http://bean.frontend.ls.com/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<XMLGregorianCalendar> dataInizioAteco2007;

    /**
     * Gets the value of the codAteco2007 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCodAteco2007() {
        return codAteco2007;
    }

    /**
     * Sets the value of the codAteco2007 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCodAteco2007(JAXBElement<String> value) {
        this.codAteco2007 = value;
    }

    /**
     * Gets the value of the codImportanzaRI property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCodImportanzaRI() {
        return codImportanzaRI;
    }

    /**
     * Sets the value of the codImportanzaRI property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCodImportanzaRI(JAXBElement<String> value) {
        this.codImportanzaRI = value;
    }

    /**
     * Gets the value of the dataInizioAteco2007 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getDataInizioAteco2007() {
        return dataInizioAteco2007;
    }

    /**
     * Sets the value of the dataInizioAteco2007 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setDataInizioAteco2007(JAXBElement<XMLGregorianCalendar> value) {
        this.dataInizioAteco2007 = value;
    }

}
