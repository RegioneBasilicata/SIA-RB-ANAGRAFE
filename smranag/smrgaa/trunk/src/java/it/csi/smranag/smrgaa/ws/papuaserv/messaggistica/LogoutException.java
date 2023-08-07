
package it.csi.smranag.smrgaa.ws.papuaserv.messaggistica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LogoutException complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LogoutException">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nestedExcClassName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="stackTraceMessage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nestedExcMsg" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LogoutException", propOrder = {
    "nestedExcClassName",
    "stackTraceMessage",
    "nestedExcMsg"
})
public class LogoutException {

    @XmlElement(required = true, nillable = true)
    protected String nestedExcClassName;
    @XmlElement(required = true, nillable = true)
    protected String stackTraceMessage;
    @XmlElement(required = true, nillable = true)
    protected String nestedExcMsg;

    /**
     * Gets the value of the nestedExcClassName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNestedExcClassName() {
        return nestedExcClassName;
    }

    /**
     * Sets the value of the nestedExcClassName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNestedExcClassName(String value) {
        this.nestedExcClassName = value;
    }

    /**
     * Gets the value of the stackTraceMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStackTraceMessage() {
        return stackTraceMessage;
    }

    /**
     * Sets the value of the stackTraceMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStackTraceMessage(String value) {
        this.stackTraceMessage = value;
    }

    /**
     * Gets the value of the nestedExcMsg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNestedExcMsg() {
        return nestedExcMsg;
    }

    /**
     * Sets the value of the nestedExcMsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNestedExcMsg(String value) {
        this.nestedExcMsg = value;
    }

}
