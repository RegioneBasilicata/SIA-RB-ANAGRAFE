
package it.csi.smranag.smrgaa.ws.papuaserv.messaggistica;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for listaMessaggi complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listaMessaggi">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="messaggi" type="{http://papuaserv.webservice.business.papuaserv.papua.csi.it/}messaggio" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="numeroMessaggiGenerici" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="numeroMessaggiLogout" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="numeroMessaggiTestata" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="numeroTotaleMessaggi" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listaMessaggi", propOrder = {
    "messaggi",
    "numeroMessaggiGenerici",
    "numeroMessaggiLogout",
    "numeroMessaggiTestata",
    "numeroTotaleMessaggi"
})
public class ListaMessaggi {

    @XmlElement(nillable = true)
    protected List<Messaggio> messaggi;
    protected long numeroMessaggiGenerici;
    protected long numeroMessaggiLogout;
    protected long numeroMessaggiTestata;
    protected long numeroTotaleMessaggi;

    /**
     * Gets the value of the messaggi property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the messaggi property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMessaggi().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Messaggio }
     * 
     * 
     */
    public List<Messaggio> getMessaggi() {
        if (messaggi == null) {
            messaggi = new ArrayList<Messaggio>();
        }
        return this.messaggi;
    }

    /**
     * Gets the value of the numeroMessaggiGenerici property.
     * 
     */
    public long getNumeroMessaggiGenerici() {
        return numeroMessaggiGenerici;
    }

    /**
     * Sets the value of the numeroMessaggiGenerici property.
     * 
     */
    public void setNumeroMessaggiGenerici(long value) {
        this.numeroMessaggiGenerici = value;
    }

    /**
     * Gets the value of the numeroMessaggiLogout property.
     * 
     */
    public long getNumeroMessaggiLogout() {
        return numeroMessaggiLogout;
    }

    /**
     * Sets the value of the numeroMessaggiLogout property.
     * 
     */
    public void setNumeroMessaggiLogout(long value) {
        this.numeroMessaggiLogout = value;
    }

    /**
     * Gets the value of the numeroMessaggiTestata property.
     * 
     */
    public long getNumeroMessaggiTestata() {
        return numeroMessaggiTestata;
    }

    /**
     * Sets the value of the numeroMessaggiTestata property.
     * 
     */
    public void setNumeroMessaggiTestata(long value) {
        this.numeroMessaggiTestata = value;
    }

    /**
     * Gets the value of the numeroTotaleMessaggi property.
     * 
     */
    public long getNumeroTotaleMessaggi() {
        return numeroTotaleMessaggi;
    }

    /**
     * Sets the value of the numeroTotaleMessaggi property.
     * 
     */
    public void setNumeroTotaleMessaggi(long value) {
        this.numeroTotaleMessaggi = value;
    }

}
