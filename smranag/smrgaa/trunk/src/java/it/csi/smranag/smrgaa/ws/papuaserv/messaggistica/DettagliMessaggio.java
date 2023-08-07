
package it.csi.smranag.smrgaa.ws.papuaserv.messaggistica;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dettagliMessaggio complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dettagliMessaggio">
 *   &lt;complexContent>
 *     &lt;extension base="{http://papuaserv.webservice.business.papuaserv.papua.csi.it/}messaggioBase">
 *       &lt;sequence>
 *         &lt;element name="allegati" type="{http://papuaserv.webservice.business.papuaserv.papua.csi.it/}allegato" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="testoMessaggio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="utenteAggiornamento" type="{http://papuaserv.webservice.business.papuaserv.papua.csi.it/}utenteAggiornamento" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dettagliMessaggio", propOrder = {
    "allegati",
    "testoMessaggio",
    "utenteAggiornamento"
})
public class DettagliMessaggio
    extends MessaggioBase
{

    @XmlElement(nillable = true)
    protected List<Allegato> allegati;
    protected String testoMessaggio;
    protected UtenteAggiornamento utenteAggiornamento;

    /**
     * Gets the value of the allegati property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the allegati property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAllegati().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Allegato }
     * 
     * 
     */
    public List<Allegato> getAllegati() {
        if (allegati == null) {
            allegati = new ArrayList<Allegato>();
        }
        return this.allegati;
    }

    /**
     * Gets the value of the testoMessaggio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTestoMessaggio() {
        return testoMessaggio;
    }

    /**
     * Sets the value of the testoMessaggio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTestoMessaggio(String value) {
        this.testoMessaggio = value;
    }

    /**
     * Gets the value of the utenteAggiornamento property.
     * 
     * @return
     *     possible object is
     *     {@link UtenteAggiornamento }
     *     
     */
    public UtenteAggiornamento getUtenteAggiornamento() {
        return utenteAggiornamento;
    }

    /**
     * Sets the value of the utenteAggiornamento property.
     * 
     * @param value
     *     allowed object is
     *     {@link UtenteAggiornamento }
     *     
     */
    public void setUtenteAggiornamento(UtenteAggiornamento value) {
        this.utenteAggiornamento = value;
    }

}
